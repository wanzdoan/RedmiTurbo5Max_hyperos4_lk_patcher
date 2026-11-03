package kotlin.coroutines;

/* JADX INFO: compiled from: ContinuationInterceptor.kt */
/* JADX INFO: loaded from: classes2.dex */
public interface ContinuationInterceptor extends CoroutineContext.Element {
    public static final Key Key = Key.$$INSTANCE;

    void releaseInterceptedContinuation(Continuation continuation);

    /* JADX INFO: compiled from: ContinuationInterceptor.kt */
    public final class Key implements CoroutineContext.Key {
        static final /* synthetic */ Key $$INSTANCE = new Key();

        private Key() {
        }
    }

    /* JADX INFO: compiled from: ContinuationInterceptor.kt */
    public abstract class DefaultImpls {
        public static CoroutineContext.Element get(ContinuationInterceptor continuationInterceptor, CoroutineContext.Key key) {
            CoroutineContext.Element elementTryCast$kotlin_stdlib;
            key.getClass();
            if (key instanceof AbstractCoroutineContextKey) {
                AbstractCoroutineContextKey abstractCoroutineContextKey = (AbstractCoroutineContextKey) key;
                if (!abstractCoroutineContextKey.isSubKey$kotlin_stdlib(continuationInterceptor.getKey()) || (elementTryCast$kotlin_stdlib = abstractCoroutineContextKey.tryCast$kotlin_stdlib(continuationInterceptor)) == null) {
                    return null;
                }
                return elementTryCast$kotlin_stdlib;
            }
            if (ContinuationInterceptor.Key != key) {
                return null;
            }
            continuationInterceptor.getClass();
            return continuationInterceptor;
        }
    }
}
