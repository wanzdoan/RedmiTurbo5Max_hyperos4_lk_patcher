package androidx.collection.internal;

/* JADX INFO: compiled from: RuntimeHelpers.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class RuntimeHelpersKt {
    public static final void throwIllegalStateException(String str) {
        str.getClass();
        throw new IllegalStateException(str);
    }

    public static final void throwIllegalArgumentException(String str) {
        str.getClass();
        throw new IllegalArgumentException(str);
    }
}
