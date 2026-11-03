package androidx.core.util;

import gov.nist.core.GenericObject$$ExternalSyntheticBUOutline0;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public abstract class ObjectsCompat {
    public static boolean equals(Object obj, Object obj2) {
        return Objects.equals(obj, obj2);
    }

    public static int hashCode(Object obj) {
        if (obj != null) {
            return obj.hashCode();
        }
        return 0;
    }

    public static int hash(Object... objArr) {
        return Objects.hash(objArr);
    }

    public static Object requireNonNull(Object obj) {
        obj.getClass();
        return obj;
    }

    public static Object requireNonNull(Object obj, String str) {
        if (obj != null) {
            return obj;
        }
        GenericObject$$ExternalSyntheticBUOutline0.m(str);
        return null;
    }
}
