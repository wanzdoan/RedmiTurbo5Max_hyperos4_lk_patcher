package vendor.xiaomi.modem.qms.V1_0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class applicationTypeE {
    public static final String toString(int i) {
        if (i == 0) {
            return "UNSPECIFIED";
        }
        if (i == 1) {
            return "CONV_AUDIO";
        }
        if (i == 2) {
            return "CONV_VIDEO";
        }
        if (i == 3) {
            return "STREAMING_AUDIO";
        }
        if (i == 4) {
            return "STREAMING_VIDEO";
        }
        if (i == 5) {
            return "TYPE_GAMING";
        }
        if (i == 6) {
            return "WEB_BROWSING";
        }
        if (i == 7) {
            return "FILE_TRANSFER";
        }
        return "0x" + Integer.toHexString(i);
    }
}
