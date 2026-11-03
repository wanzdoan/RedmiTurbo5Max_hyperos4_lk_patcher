package miuix.core.util;

/* JADX INFO: loaded from: classes2.dex */
public abstract class RomUtils {
    private static int HYPER_OS_VERSION_CODE = -1;
    private static int MIUI_VERSION_CODE = -1;

    public static int getHyperOsVersion() {
        if (HYPER_OS_VERSION_CODE == -1) {
            HYPER_OS_VERSION_CODE = getHyperOsVersionNoCache();
        }
        return HYPER_OS_VERSION_CODE;
    }

    public static int getHyperOsVersionNoCache() {
        return SystemProperties.getInt("ro.mi.os.version.code", 0);
    }

    public static int getMiuiVersion() {
        if (MIUI_VERSION_CODE == -1) {
            MIUI_VERSION_CODE = getMiuiVersionNoCache();
        }
        return MIUI_VERSION_CODE;
    }

    public static int getMiuiVersionNoCache() {
        return SystemProperties.getInt("ro.miui.ui.version.code", 0);
    }

    public static boolean isMiuiXVSdkSupported() {
        return getMiuiVersion() >= 15;
    }
}
