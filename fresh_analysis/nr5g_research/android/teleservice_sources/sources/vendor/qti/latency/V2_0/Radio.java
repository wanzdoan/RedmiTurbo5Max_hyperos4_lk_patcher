package vendor.qti.latency.V2_0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Radio {
    public static final String toString(long j) {
        if (j == 0) {
            return "WWAN";
        }
        if (j == 1) {
            return "WLAN";
        }
        return "0x" + Long.toHexString(j);
    }
}
