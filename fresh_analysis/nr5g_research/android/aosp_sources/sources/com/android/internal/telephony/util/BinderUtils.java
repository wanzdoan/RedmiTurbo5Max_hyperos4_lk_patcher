package com.android.internal.telephony.util;

import android.os.Binder;

/* JADX INFO: loaded from: classes.dex */
public class BinderUtils {

    @FunctionalInterface
    public interface ThrowingRunnable<T extends Exception> {
        void run() throws Exception;
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T, E extends Exception> {
        T get() throws Exception;
    }

    public static final <T extends Exception> void withCleanCallingIdentity(ThrowingRunnable<T> throwingRunnable) throws Exception {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            throwingRunnable.run();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public static final <T, E extends Exception> T withCleanCallingIdentity(ThrowingSupplier<T, E> throwingSupplier) throws Exception {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return throwingSupplier.get();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }
}
