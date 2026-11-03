package vendor.xiaomi.modem.qms.V1_0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class trafficDirectionE {
    public static final String toString(int i) {
        if (i == 0) {
            return "UPLINK";
        }
        if (i == 1) {
            return "DOWNLINK";
        }
        return "0x" + Integer.toHexString(i);
    }
}
