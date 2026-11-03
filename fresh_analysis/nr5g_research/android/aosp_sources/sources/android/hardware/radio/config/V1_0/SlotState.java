package android.hardware.radio.config.V1_0;

/* JADX INFO: loaded from: classes.dex */
public abstract class SlotState {
    public static final String toString(int i) {
        if (i == 0) {
            return "INACTIVE";
        }
        if (i == 1) {
            return "ACTIVE";
        }
        return "0x" + Integer.toHexString(i);
    }
}
