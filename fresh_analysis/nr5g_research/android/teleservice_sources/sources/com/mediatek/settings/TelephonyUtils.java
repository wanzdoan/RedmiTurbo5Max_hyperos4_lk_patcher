package com.mediatek.settings;

import android.content.Context;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.phone.PhoneUtils;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class TelephonyUtils {
    public static boolean isGeminiProject() {
        boolean zIsMultiSimEnabled = TelephonyManager.getDefault().isMultiSimEnabled();
        log("isGeminiProject : " + zIsMultiSimEnabled);
        return zIsMultiSimEnabled;
    }

    public static boolean isInCall(Context context, int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            log("isInCall[" + i + "] invalid subId: " + i);
            return true;
        }
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
        boolean z = false;
        if (telephonyManagerCreateForSubscriptionId != null) {
            z = telephonyManagerCreateForSubscriptionId.getCallState(i) != 0;
        }
        log("isInCall[" + i + "] = " + z);
        return z;
    }

    public static boolean isAirplaneModeOn(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    public static boolean isImsServiceAvailable(Context context, int i) {
        boolean zIsImsRegistered = PhoneUtils.isValidSubId(i) ? MtkTelephonyManagerEx.getDefault().isImsRegistered(i) : false;
        log("isImsServiceAvailable[" + i + "], available = " + zIsImsRegistered);
        return zIsImsRegistered;
    }

    public static boolean isHotSwapHanppened(List list, List list2) {
        boolean z = true;
        if (list.size() != list2.size()) {
            return true;
        }
        for (int i = 0; i < list2.size(); i++) {
            if (!((SubscriptionInfo) list2.get(i)).getIccId().equals(((SubscriptionInfo) list.get(i)).getIccId())) {
                log("isHotSwapHanppened : " + z);
                return z;
            }
        }
        z = false;
        log("isHotSwapHanppened : " + z);
        return z;
    }

    private static void log(String str) {
        Log.d("TelephonyUtils", str);
    }
}
