package com.google.common.util.concurrent;

import java.util.Locale;
import java.util.logging.Level;

/* JADX INFO: loaded from: classes.dex */
final class UncaughtExceptionHandlers$Exiter implements Thread.UncaughtExceptionHandler {
    private static final LazyLogger logger = new LazyLogger(UncaughtExceptionHandlers$Exiter.class);
    private final Runtime runtime;

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        try {
            logger.get().log(Level.SEVERE, String.format(Locale.ROOT, "Caught an exception in %s.  Shutting down.", thread), th);
        } catch (Throwable th2) {
            try {
                System.err.println(th.getMessage());
                System.err.println(th2.getMessage());
            } finally {
                this.runtime.exit(1);
            }
        }
    }
}
