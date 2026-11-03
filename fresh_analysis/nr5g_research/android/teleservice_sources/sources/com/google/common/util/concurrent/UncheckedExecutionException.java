package com.google.common.util.concurrent;

/* JADX INFO: loaded from: classes.dex */
public class UncheckedExecutionException extends RuntimeException {
    private static final long serialVersionUID = 0;

    @Deprecated
    protected UncheckedExecutionException(String str) {
        super(str);
    }

    public UncheckedExecutionException(Throwable th) {
        super(th);
    }
}
