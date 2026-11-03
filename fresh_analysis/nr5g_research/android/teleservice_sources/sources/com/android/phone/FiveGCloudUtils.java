package com.android.phone;

import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import java.util.List;
import java.util.Set;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class FiveGCloudUtils {
    public static void saveListToDb(List list, String str) {
        MiuiPhoneUtils.saveListToDb(list, str);
    }

    public static void saveListToDb(Set set, String str) {
        MiuiPhoneUtils.saveListToDb(set, str);
    }

    public static void saveStatusToDb(String str, String str2) {
        if (str != null) {
            Settings.Global.putString(PhoneGlobals.getInstance().getContentResolver(), str2, str);
        }
    }

    public static boolean modelMatched(String str) {
        return "All".equalsIgnoreCase(str) || Build.DEVICE.equalsIgnoreCase(str);
    }

    public static boolean versionTypeMatched(String str) {
        return "All".equalsIgnoreCase(str) || getDeviceVersionType().equalsIgnoreCase(str);
    }

    public static boolean customizedRegionMatched(String str) {
        return "All".equalsIgnoreCase(str) || getCustomizedRegion().equalsIgnoreCase(str);
    }

    public static boolean simOperatorMatched(String str) {
        if ("All".equalsIgnoreCase(str)) {
            return true;
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(i);
            if (!TextUtils.isEmpty(simOperatorForSlot) && str.contains(simOperatorForSlot)) {
                return true;
            }
        }
        return false;
    }

    private static String getDeviceVersionType() {
        if (miui.os.Build.IS_DEVELOPMENT_VERSION) {
            return "DEV";
        }
        if (miui.os.Build.IS_STABLE_VERSION) {
            return "STABLE";
        }
        return "UNKNOWN";
    }

    private static String getCustomizedRegion() {
        return SystemProperties.get("ro.miui.customized.region", "");
    }
}
