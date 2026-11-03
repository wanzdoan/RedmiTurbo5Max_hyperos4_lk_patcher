package com.google.common.util.concurrent;

/* JADX INFO: loaded from: classes.dex */
public class ExecutionError extends Error {
    private static final long serialVersionUID = 0;

    @Deprecated
    protected ExecutionError(String str) {
        super(str);
    }

    public ExecutionError(Error error) {
        super(error);
    }
}
