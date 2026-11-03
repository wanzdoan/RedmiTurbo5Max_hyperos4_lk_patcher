package androidx.lifecycle;

/* JADX INFO: compiled from: DefaultLifecycleObserver.kt */
/* JADX INFO: loaded from: classes.dex */
public interface DefaultLifecycleObserver extends LifecycleObserver {
    default void onCreate(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getClass();
    }

    default void onDestroy(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getClass();
    }

    default void onPause(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getClass();
    }

    void onResume(LifecycleOwner lifecycleOwner);

    default void onStart(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getClass();
    }

    default void onStop(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getClass();
    }
}
