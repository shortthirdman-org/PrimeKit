package com.shortthirdman.primekit.essentials.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class GenericRequestMerger<K, V> implements RequestMerger<K, V> {

    private final ConcurrentHashMap<K, CompletableFuture<Optional<V>>> pendingRequests = new ConcurrentHashMap<>();
    private final List<K> keyQueue = Collections.synchronizedList(new ArrayList<>());
    private final Function<List<K>, Map<K, V>> batchFunction;
    private final long windowTimeMillis;
    private final int maxBatchSize;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public GenericRequestMerger(long windowTimeMillis, int maxBatchSize, Function<List<K>, Map<K, V>> batchFunction) {
        this.windowTimeMillis = windowTimeMillis;
        this.maxBatchSize = maxBatchSize;
        this.batchFunction = batchFunction;
    }

    @Override
    public CompletableFuture<Optional<V>> get(K key) {
        CompletableFuture<Optional<V>> future = new CompletableFuture<>();
        CompletableFuture<Optional<V>> existingFuture = pendingRequests.putIfAbsent(key, future);
        if (existingFuture != null) {
            return existingFuture; // Return existing future if key is already queued
        }
        synchronized (keyQueue) {
            keyQueue.add(key);
            if (keyQueue.size() == 1) {
                scheduler.schedule(this::processBatch, windowTimeMillis, TimeUnit.MILLISECONDS);
            } else if (keyQueue.size() >= maxBatchSize) {
                processBatch();
            }
        }
        return future;
    }

    private void processBatch() {
        List<K> batch;
        synchronized (keyQueue) {
            if (keyQueue.isEmpty()) return;
            batch = new ArrayList<>(keyQueue);
            keyQueue.clear();
        }
        try {
            Map<K, V> results = batchFunction.apply(batch);
            for (K key : batch) {
                CompletableFuture<Optional<V>> future = pendingRequests.remove(key);
                if (future != null) {
                    future.complete(Optional.ofNullable(results.get(key)));
                }
            }
        } catch (Exception e) {
            for (K key : batch) {
                CompletableFuture<Optional<V>> future = pendingRequests.remove(key);
                if (future != null) {
                    future.completeExceptionally(e);
                }
            }
        }
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
    }
}
