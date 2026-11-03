package com.android.phone.statistics.utils;

import android.os.SystemProperties;
import com.android.phone.ModemProvider;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public abstract class DeviceUtil {
    public static final boolean IS_INTERNATIONAL_BUILD = Build.IS_INTERNATIONAL_BUILD;
    public static final boolean IS_STABLE_VERSION = Build.IS_STABLE_VERSION;
    public static final boolean IS_DEV_VERSION = Build.IS_DEVELOPMENT_VERSION;
    public static final boolean IS_RESTRICT_IMEI = "1".equals(SystemProperties.get("ro.miui.restrict_imei"));

    public static boolean isGlobalDeviceStatEnabled() {
        return true;
    }

    public static boolean isLegalReleaseVersion() {
        return true;
    }

    public static boolean isValidVersion() {
        return !IS_INTERNATIONAL_BUILD || isGlobalDeviceStatEnabled();
    }

    public static boolean isQcomPlatform() {
        return ModemProvider.IS_QCOM;
    }

    public static String getVersionType() {
        if (IS_DEV_VERSION) {
            return "DEV";
        }
        if (IS_STABLE_VERSION) {
            return "STABLE";
        }
        return "UNKNOWN";
    }

    public static boolean isInternationalXmsRecordEnabled() {
        return TelephonyManagerEx.getDefault().isInternationalXmsSupported();
    }

    public static boolean isXmsRecordEnabled() {
        return !IS_INTERNATIONAL_BUILD;
    }

    public static boolean isModemOfflineSupported() {
        return HyperOSCustFeatureResolve.getBoolean("is_modem_offline_supported", false);
    }

    public static boolean isModemSceneStatEnabled() {
        return HyperOSCustFeatureResolve.getBoolean("is_modem_scene_stat_enabled", false) || TelephonyManagerEx.getDefault().isPlatformX450();
    }

    public static boolean isModemVolteEnabled() {
        return HyperOSCustFeatureResolve.getBoolean("is_modem_volte_enabled", false);
    }
}
