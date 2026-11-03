package androidx.lifecycle;

/* JADX INFO: compiled from: WeakReference.jvmAndAndroid.kt */
/* JADX INFO: loaded from: classes.dex */
public final class WeakReference {
    private final java.lang.ref.WeakReference delegate;

    public WeakReference(Object obj) {
        obj.getClass();
        this.delegate = new java.lang.ref.WeakReference(obj);
    }

    public final Object get() {
        return this.delegate.get();
    }
}
