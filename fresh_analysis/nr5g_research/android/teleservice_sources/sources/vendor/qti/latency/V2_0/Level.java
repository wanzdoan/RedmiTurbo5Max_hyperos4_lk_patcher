package vendor.qti.latency.V2_0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Level {
    public static final String toString(long j) {
        if (j == 1) {
            return "L1";
        }
        if (j == 2) {
            return "L2";
        }
        if (j == 3) {
            return "L3";
        }
        if (j == 4) {
            return "L4";
        }
        if (j == 5) {
            return "L5";
        }
        if (j == 6) {
            return "L6";
        }
        return "0x" + Long.toHexString(j);
    }
}
