package com.shortthirdman.primekit.essentials.core;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @param <K> the key class bean
 * @param <V> the value class
 */
public class CachedRequestMerger<K, V> implements RequestMerger<K, V> {

    private final GenericRequestMerger<K, V> merger;
    private final LoadingCache<K, Optional<V>> cache;

    public CachedRequestMerger(long windowTimeMillis, int maxBatchSize, Function<List<K>, Map<K, V>> batchFunction) {
        this.merger = new GenericRequestMerger<>(windowTimeMillis, maxBatchSize, batchFunction);
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(CacheLoader.from(key -> merger.get(key).join()));
    }

    @Override
    public CompletableFuture<Optional<V>> get(K key) {
        Optional<V> cached = cache.getIfPresent(key);
        if (!Objects.isNull(cached) && cached.isPresent()) {
            return CompletableFuture.completedFuture(cached);
        }
        return merger.get(key).whenComplete((result, ex) -> {
            if (ex == null) cache.put(key, result);
        });
    }

    @Override
    public void shutdown() {
        merger.shutdown();
    }
}
