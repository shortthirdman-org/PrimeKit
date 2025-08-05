package com.shortthirdman.primekit.essentials.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class RequestMergerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRequestMerger() {
        Function<List<Long>, Map<Long, String>> batchFunction = ids -> {
            Map<Long, String> results = new HashMap<>();
            for (Long id : ids) {
                results.put(id, "User-" + id);
            }
            return results;
        };
        GenericRequestMerger<Long, String> merger = new GenericRequestMerger<>(20, 3, batchFunction);
        List<CompletableFuture<Optional<String>>> futures = new ArrayList<>();

        // Submit requests
        for (long i = 1; i <= 5; i++) {
            long finalI = i;
            futures.add(merger.get(i).thenApply(result -> {
                System.out.println("Result for " + finalI + ": " + result.orElse("Not found") + " 📦");
                return result;
            }));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        merger.shutdown();
    }
}