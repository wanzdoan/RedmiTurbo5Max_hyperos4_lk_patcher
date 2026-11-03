package com.android.phone;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.internal.telephony.DynamicSarService;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.TelephonyManagerEx;
import miuix.core.util.SystemProperties;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyCapabilities {
    public static boolean isInCallSupportGlobalCallRecorder(Context context) {
        Bundle bundle;
        boolean z = false;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.android.incallui", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            z = ((applicationInfo == null || (bundle = applicationInfo.metaData) == null) ? 0 : bundle.getInt("global_call_recorder", 0)) > 0;
            Log.i("TelephonyCapabilities", "isInCallSupportGlobalCallRecorder =" + z);
            return z;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.i("TelephonyCapabilities", "isInCallSupportGlobalCallRecorder Failed to get incall retry code.");
            return z;
        }
    }

    public static boolean supportsCallRecording(Context context) {
        if (Build.IS_INTERNATIONAL_BUILD) {
            if (isInCallSupportGlobalCallRecorder(context) && HyperOSCustFeatureResolve.getBoolean("unsupport_call_recording_gl", false)) {
                return false;
            }
            if (!context.getResources().getBoolean(R.bool.config_call_recording) && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(context)) {
                return false;
            }
        }
        return true;
    }

    public static boolean supportsDefaultAntiStrange(Context context) {
        return context.getResources().getBoolean(R.bool.config_default_anti_strange);
    }

    public static boolean supportsDialingEmergencyNumberDirectly(Context context) {
        return context.getResources().getBoolean(R.bool.config_support_dialing_emergency_number_directly);
    }

    public static boolean supportsDialingEmergencyNumberWithSuffix(Context context) {
        return !TextUtils.isEmpty(context.getResources().getString(R.string.config_dialing_emergency_number_suffix));
    }

    public static boolean supportCallStreamRecording(Context context) {
        return context.getResources().getBoolean(R.bool.config_call_stream_recording);
    }

    public static boolean supportShow2GNetwork(Context context) {
        return context.getResources().getBoolean(R.bool.config_show_2g_network);
    }

    public static boolean isSmscAddressEncoded(Context context) {
        return context.getResources().getBoolean(R.bool.config_smsc_address_encoded);
    }

    public static boolean supportShowPLMNPreference() {
        return (CarrierConfigLoaderInjector.isShowUPlmnPreference(PhoneGlobals.getInstance()) && !Build.IS_INTERNATIONAL_BUILD) || Build.IS_CM_CUSTOMIZATION;
    }

    public static boolean supportSimAutoAdapt() {
        return isLTEALL();
    }

    public static boolean isLTEALL() {
        String str = SystemProperties.get("persist.radio.modem");
        return TextUtils.isEmpty(str) || !"X7-LTE".equals(str);
    }

    public static boolean slotNonsupportWifiCall(int i) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "config_slot_nonsupport_wifi_call", false) && MiuiPhoneUtils.getInsertedSimCount() == 2 && i == 1;
    }
}
