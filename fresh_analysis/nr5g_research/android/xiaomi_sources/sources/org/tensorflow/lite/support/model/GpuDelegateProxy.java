package org.tensorflow.lite.support.model;

import android.util.Log;
import java.io.Closeable;
import java.io.IOException;
import org.tensorflow.lite.Delegate;

/* JADX INFO: loaded from: classes.dex */
class GpuDelegateProxy implements Delegate, Closeable {
    private static final String TAG = "GpuDelegateProxy";
    private final Closeable proxiedCloseable;
    private final Delegate proxiedDelegate;

    public static GpuDelegateProxy maybeNewInstance() {
        try {
            Class<?> clazz = Class.forName("org.tensorflow.lite.gpu.GpuDelegate");
            Object instance = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return new GpuDelegateProxy(instance);
        } catch (ReflectiveOperationException e) {
            Log.e(TAG, "Failed to create the GpuDelegate dynamically.", e);
            return null;
        }
    }

    @Override // org.tensorflow.lite.Delegate, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            this.proxiedCloseable.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to close the GpuDelegate.", e);
        }
    }

    @Override // org.tensorflow.lite.Delegate
    public long getNativeHandle() {
        return this.proxiedDelegate.getNativeHandle();
    }

    private GpuDelegateProxy(Object instance) {
        this.proxiedCloseable = (Closeable) instance;
        this.proxiedDelegate = (Delegate) instance;
    }
}
