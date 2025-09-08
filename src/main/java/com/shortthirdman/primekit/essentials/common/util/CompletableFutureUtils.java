package com.shortthirdman.primekit.essentials.common.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

/**
 * A utility class to add JDK 9+ timeout functionality to CompletableFuture in JDK.
 * Inspired by JDK 9's CompletableFuture implementation.
 *
 * @author ShortThirdMan
 */
public final class CompletableFutureUtils {

    private CompletableFutureUtils() {}

    /**
     * If not already completed, causes this CompletableFuture to be
     * completed exceptionally with a {@link TimeoutException} after the
     * given timeout.
     *
     * @param future  the CompletableFuture to apply the timeout to
     * @param timeout how long to wait before completing exceptionally
     * @param unit    the time unit of the timeout argument
     * @return the original CompletableFuture
     */
    public static <T> CompletableFuture<T> orTimeout(CompletableFuture<T> future, long timeout, TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("Time unit cannot be null");
        }
        if (future == null) {
            throw new NullPointerException("CompletableFuture cannot be null");
        }
        // If the future is already done, just return it.
        if (future.isDone()) {
            return future;
        }
        // Schedule a task to complete the future exceptionally after the timeout.
        // The Canceller will cancel this scheduled task if the future completes normally.
        return future.whenComplete(new Canceller(Delayer.delay(new Timeout(future), timeout, unit)));
    }

    /**
     * Inner class to handle the actual timeout logic
     */
    static final class Timeout implements Runnable {
        final CompletableFuture<?> future;
        Timeout(CompletableFuture<?> future) {
            this.future = future;
        }
        public void run() {
            if (future != null && !future.isDone()) {
                future.completeExceptionally(new TimeoutException());
            }
        }
    }

    /**
     * Inner class to handle cancellation of the timeout task
     */
    static final class Canceller implements BiConsumer<Object, Throwable> {
        final Future<?> future;
        Canceller(Future<?> future) {
            this.future = future;
        }
        public void accept(Object ignore, Throwable ex) {
            // If the original future completed (ex is null) and the timeout task
            // hasn't run yet, cancel the timeout task.
            if (ex == null && future != null && !future.isDone()) {
                future.cancel(false);
            }
        }
    }

    /**
     * Singleton delayed scheduler
     */
    static final class Delayer {
        static ScheduledFuture<?> delay(Runnable command, long delay, TimeUnit unit) {
            return delayer.schedule(command, delay, unit);
        }
        private static final ScheduledThreadPoolExecutor delayer;
        static {
            delayer = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
            delayer.setRemoveOnCancelPolicy(true);
        }
        static final class DaemonThreadFactory implements ThreadFactory {
            public Thread newThread(@NotNull Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("CompletableFutureUtilsDelayScheduler");
                return t;
            }
        }
    }
}
