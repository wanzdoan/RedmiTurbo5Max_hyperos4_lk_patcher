package org.tensorflow.lite;

import java.io.Closeable;

/* JADX INFO: loaded from: classes.dex */
public interface Delegate extends Closeable {
    long getNativeHandle();

    @Override // java.io.Closeable, java.lang.AutoCloseable
    default void close() {
    }
}
