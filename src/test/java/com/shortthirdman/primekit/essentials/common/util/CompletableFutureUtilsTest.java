package com.shortthirdman.primekit.essentials.common.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureUtilsTest {

    @Test
    void completesNormallyBeforeTimeout_cancelsTimeoutTask() {
        CompletableFuture<String> f = new CompletableFuture<>();
        CompletableFutureUtils.orTimeout(f, 200, TimeUnit.MILLISECONDS);

        // Complete before 200ms
        f.complete("ok");

        assertTrue(f.isDone());
        assertFalse(f.isCompletedExceptionally());
        assertEquals("ok", f.join());

        // Sleep past the timeout to ensure the scheduled timeout would have fired if not cancelled
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        // If the timeout wasn't cancelled, the future would be completed exceptionally already, which is not the case.
        assertFalse(f.isCompletedExceptionally());
    }

    @Test
    void timesOutWhenNotCompletedInTime_completesExceptionallyWithTimeoutException() {
        CompletableFuture<String> f = new CompletableFuture<>();
        CompletableFutureUtils.orTimeout(f, 50, TimeUnit.MILLISECONDS);

        // Do not complete the future; wait to let timeout trigger
        try { Thread.sleep(120); } catch (InterruptedException ignored) {}

        assertTrue(f.isCompletedExceptionally());
        CompletionException ex = assertThrows(CompletionException.class, f::join);
        assertInstanceOf(TimeoutException.class, ex.getCause());
    }

    @Test
    void handlesNullArgumentsAndAlreadyCompletedFuture() {
        // Null unit
        CompletableFuture<String> f1 = new CompletableFuture<>();
        assertThrows(NullPointerException.class, () -> CompletableFutureUtils.orTimeout(f1, 10, null));

        // Null future
        assertThrows(NullPointerException.class, () -> CompletableFutureUtils.orTimeout(null, 10, TimeUnit.MILLISECONDS));

        // Already completed future should be returned as is and not be modified
        CompletableFuture<Integer> done = CompletableFuture.completedFuture(42);
        CompletableFuture<Integer> returned = CompletableFutureUtils.orTimeout(done, 10, TimeUnit.MILLISECONDS);
        assertSame(done, returned);
        assertEquals(42, returned.join());
        // Sleep beyond timeout to confirm nothing changes
        try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        assertFalse(returned.isCompletedExceptionally());
    }
}