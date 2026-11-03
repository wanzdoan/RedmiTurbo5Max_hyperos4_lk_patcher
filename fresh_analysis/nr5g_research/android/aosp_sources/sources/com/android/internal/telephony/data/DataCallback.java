package com.android.internal.telephony.data;

import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class DataCallback {
    private final Executor mExecutor;

    public DataCallback(Executor executor) {
        this.mExecutor = executor;
    }

    public Executor getExecutor() {
        return this.mExecutor;
    }

    public void invokeFromExecutor(Runnable runnable) {
        this.mExecutor.execute(runnable);
    }
}
