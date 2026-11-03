package com.android.phone.freqopt.util;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public abstract class LogUtils {
    private static volatile boolean sDebugEnabled = false;

    public static void setDebugEnabled(boolean z) {
        sDebugEnabled = z;
    }

    public static void d(String str, String str2) {
        if (sDebugEnabled) {
            Log.d(str, str2);
        }
    }

    public static void i(String str, String str2) {
        Log.i(str, str2);
    }

    public static void w(String str, String str2) {
        Log.w(str, str2);
    }

    public static void w(String str, String str2, Throwable th) {
        Log.w(str, str2, th);
    }

    public static void e(String str, String str2) {
        Log.e(str, str2);
    }
}
