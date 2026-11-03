package kotlinx.coroutines;

/* JADX INFO: compiled from: DebugStrings.kt */
/* JADX INFO: loaded from: classes2.dex */
public abstract class DebugStringsKt {
    public static final String getHexAddress(Object obj) {
        obj.getClass();
        String hexString = Integer.toHexString(System.identityHashCode(obj));
        hexString.getClass();
        return hexString;
    }

    public static final String getClassSimpleName(Object obj) {
        obj.getClass();
        return obj.getClass().getSimpleName();
    }
}
