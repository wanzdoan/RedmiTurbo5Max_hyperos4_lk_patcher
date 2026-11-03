package com.android.phone.utils;

import android.content.Context;
import android.util.Log;
import com.xiaomi.internal.telephony.DynamicSarService;
import miui.os.Build;

/* JADX INFO: loaded from: classes.dex */
public abstract class VoiceReportUtils {
    public static boolean isSupportVoiceReportV2(Context context) {
        return !Build.IS_INTERNATIONAL_BUILD && isVoiceReportV2(context, "com.miui.voiceassist") && isVoiceReportV2(context, "com.android.mms");
    }

    private static boolean isVoiceReportV2(Context context, String str) {
        try {
            boolean z = context.getPackageManager().getApplicationInfo(str, DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY).metaData.getBoolean("voice_report_v2", false);
            Log.d("VoiceReportUtils", str + " is voice report v2:" + z);
            return z;
        } catch (Exception e) {
            Log.e("VoiceReportUtils", str + " get voice report v2 failed!", e);
            return false;
        }
    }
}
