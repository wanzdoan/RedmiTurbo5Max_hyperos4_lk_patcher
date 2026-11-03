package kotlin.coroutines;

/* JADX INFO: compiled from: CoroutineContext.kt */
/* JADX INFO: loaded from: classes2.dex */
public interface CoroutineContext {

    /* JADX INFO: compiled from: CoroutineContext.kt */
    public interface Element extends CoroutineContext {
        Key getKey();
    }

    /* JADX INFO: compiled from: CoroutineContext.kt */
    public interface Key {
    }

    Element get(Key key);
}
