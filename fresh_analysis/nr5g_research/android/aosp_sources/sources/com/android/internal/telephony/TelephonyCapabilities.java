package com.android.internal.telephony;

import android.R;
import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyCapabilities {
    private static int BOARD_API_LEVEL;
    private static int VENDOR_API_LEVEL;

    public static int getDeviceIdLabel(Phone phone) {
        return R.string.lockscreen_permanent_disabled_sim_message_short;
    }

    public static boolean supportsNetworkSelection(Phone phone) {
        return true;
    }

    static {
        int i = SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT);
        VENDOR_API_LEVEL = i;
        BOARD_API_LEVEL = SystemProperties.getInt("ro.board.api_level", i);
    }

    public static boolean supportsEcm(Phone phone) {
        Rlog.d("TelephonyCapabilities", "supportsEcm: Phone type = " + phone.getPhoneType() + " Ims Phone = " + phone.getImsPhone());
        return phone.getImsPhone() != null;
    }

    public static boolean supportsVoiceMessageCount(Phone phone) {
        return phone.getVoiceMessageCount() != -1;
    }

    private static boolean minimalTelephonyCdmCheck(FeatureFlags featureFlags) {
        return BOARD_API_LEVEL >= 35;
    }

    public static boolean supportsTelephonyCalling(FeatureFlags featureFlags, Context context) {
        if (!SystemProperties.getBoolean("ro.radio.noril", false) && minimalTelephonyCdmCheck(featureFlags)) {
            return context.getPackageManager().hasSystemFeature("android.hardware.telephony.calling");
        }
        return true;
    }

    public static boolean supportsTelephonyMessaging(FeatureFlags featureFlags, Context context) {
        if (minimalTelephonyCdmCheck(featureFlags)) {
            return context.getPackageManager().hasSystemFeature("android.hardware.telephony.messaging");
        }
        return true;
    }

    public static boolean supportsTelephonyData(FeatureFlags featureFlags, Context context) {
        if (minimalTelephonyCdmCheck(featureFlags)) {
            return context.getPackageManager().hasSystemFeature("android.hardware.telephony.data");
        }
        return true;
    }
}
