package com.android.phone.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.phone.PhoneGlobals;
import com.android.phone.PhoneProxy;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiOpconfigUtils {
    public static Context sContext = PhoneGlobals.getInstance();

    public static boolean supportCarrierApex() {
        return TelephonyManager.isCarrierApexEnabled();
    }

    public static boolean supportCarrierCota() {
        return TelephonyManager.isCarrierCotaSupported();
    }

    public static void sendOpconfigUpdateBroadcast(String str) {
        Intent intent = new Intent("miui.intent.action.ACTION_OPCONFIG_CHANGED");
        intent.putExtra("opconfig_version", str);
        ActivityManager.broadcastStickyIntent(intent, 51, -1);
        Log.i("MiuiOpconfigUtils", "sendOpconfigUpdateBroadcast version:" + str);
    }

    public static String getOpconfigVersion() {
        if (supportCarrierApex()) {
            return PhoneProxy.getOpconfigVersion();
        }
        if (supportCarrierCota()) {
            return Long.toString(TelephonyManager.getDefault().getCotaOpconfigVersionCode());
        }
        return null;
    }

    public static String getOpconfigName() {
        if (supportCarrierApex()) {
            return PhoneProxy.getOpconfigName();
        }
        if (supportCarrierCota()) {
            return TelephonyManager.getDefault().getCotaOpconfigVersionName();
        }
        return null;
    }
}
