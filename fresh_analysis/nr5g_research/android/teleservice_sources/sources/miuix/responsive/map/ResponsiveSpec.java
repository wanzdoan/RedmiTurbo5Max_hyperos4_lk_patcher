package miuix.responsive.map;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ResponsiveSpec {
    public static boolean isScreenOrientationMatch(int i, int i2) {
        return i2 == 3 || i == i2;
    }
}
