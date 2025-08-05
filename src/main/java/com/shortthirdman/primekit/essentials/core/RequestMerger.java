package com.shortthirdman.primekit.essentials.core;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface RequestMerger<K, V> {

    /**
     * @param key the key class
     * @return
     */
    CompletableFuture<Optional<V>> get(K key);

    void shutdown();
}
