package vendor.xiaomi.modem.qms.V1_0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class nr5gScsTypeE {
    public static final String toString(int i) {
        if (i == 0) {
            return "NR5G_SCS_15";
        }
        if (i == 1) {
            return "NR5G_SCS_30";
        }
        if (i == 2) {
            return "NR5G_SCS_60";
        }
        if (i == 3) {
            return "NR5G_SCS_120";
        }
        if (i == 4) {
            return "NR5G_SCS_240";
        }
        return "0x" + Integer.toHexString(i);
    }
}
