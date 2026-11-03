package vendor.qti.latency.V2_0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class SlotId {
    public static final String toString(long j) {
        if (j == -1) {
            return "SLOT_UNSPECIFIED";
        }
        if (j == 0) {
            return "SLOT_FIRST";
        }
        if (j == 1) {
            return "SLOT_SECOND";
        }
        if (j == 2) {
            return "SLOT_THIRD";
        }
        return "0x" + Long.toHexString(j);
    }
}
