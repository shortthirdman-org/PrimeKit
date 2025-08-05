package com.shortthirdman.primekit.essentials.core;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

public class FastFailingRequestMerger<K, V> implements RequestMerger<K, V> {

    private final ConcurrentHashMap<K, CompletableFuture<Optional<V>>> pendingRequests = new ConcurrentHashMap<>();
    private final List<K> keyQueue = Collections.synchronizedList(new ArrayList<>());
    private final Function<List<K>, Map<K, V>> batchFunction;
    private final long windowTimeMillis;
    private final int maxBatchSize;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService batchExecutor = Executors.newFixedThreadPool(2);
    private final Semaphore backpressure = new Semaphore(5);
    private final CircuitBreaker circuitBreaker;

    public FastFailingRequestMerger(long windowTimeMillis, int maxBatchSize, Function<List<K>, Map<K, V>> batchFunction) {
        this.windowTimeMillis = windowTimeMillis;
        this.maxBatchSize = maxBatchSize;
        this.batchFunction = batchFunction;
        this.circuitBreaker = CircuitBreaker.of("requestMerger", CircuitBreakerConfig.ofDefaults());
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

    @Override
    public void shutdown() {
        scheduler.shutdown();
        circuitBreaker.reset();
    }

    private void processBatch() {
        List<K> batch;
        synchronized (keyQueue) {
            if (keyQueue.isEmpty()) return;
            batch = new ArrayList<>(keyQueue);
            keyQueue.clear();
        }
        try {
            backpressure.acquire();
            batchExecutor.submit(() -> {
                try {
                    Map<K, V> results = circuitBreaker.executeSupplier(() -> batchFunction.apply(batch));
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
                } finally {
                    backpressure.release();
                }
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
