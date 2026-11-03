package androidx.core.math;

/* JADX INFO: loaded from: classes.dex */
public abstract class MathUtils {
    public static int clamp(int i, int i2, int i3) {
        if (i < i2) {
            return i2;
        }
        return i > i3 ? i3 : i;
    }
}
