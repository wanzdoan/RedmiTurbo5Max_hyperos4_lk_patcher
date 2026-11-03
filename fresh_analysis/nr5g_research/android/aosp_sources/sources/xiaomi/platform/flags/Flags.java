package xiaomi.platform.flags;

/* JADX INFO: loaded from: classes.dex */
public final class Flags {
    public static final String FLAG_MTK_ENABLED = "xiaomi.platform.flags.mtk_enabled";
    public static final String FLAG_QCOM_ENABLED = "xiaomi.platform.flags.qcom_enabled";
    public static final String FLAG_SPRD_ENABLED = "xiaomi.platform.flags.sprd_enabled";
    public static final String FLAG_XRING_ENABLED = "xiaomi.platform.flags.xring_enabled";

    public static boolean mtkEnabled() {
        return true;
    }

    public static boolean qcomEnabled() {
        return false;
    }

    public static boolean sprdEnabled() {
        return false;
    }

    public static boolean xringEnabled() {
        return false;
    }
}
