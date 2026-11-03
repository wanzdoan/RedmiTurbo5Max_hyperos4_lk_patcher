package com.android.libraries.entitlement.utils;

import android.os.Build;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public abstract class DebugUtils {
    public static void logPii(String str) {
        if (isPiiLoggable()) {
            Log.i("ServiceEntitlement", str);
        }
    }

    public static String getBypassEapAkaResponse() {
        String str = SystemProperties.get("persist.entitlement.fake_eap_aka_response");
        return (TextUtils.isEmpty(str) || !isDebugBuild()) ? "" : str;
    }

    private static boolean isDebugBuild() {
        return !"user".equals(Build.TYPE);
    }

    private static boolean isPiiLoggable() {
        if (isDebugBuild()) {
            return SystemProperties.getBoolean("dbg.se.pii_loggable", false);
        }
        return false;
    }
}
