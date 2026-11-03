package com.android.services.telephony.relay.phonecall;

import android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class RelayLog {
    private static final String TAG = "RelayLog";

    public static void logi(String str) {
        Rlog.i(TAG, " --> " + str);
    }

    public static void logi(String str, String str2) {
        Rlog.i(TAG, str + " --> " + str2);
    }

    public static void logd(String str, String str2) {
        Rlog.d(TAG, str + " --> " + str2);
    }

    public static void loge(String str, String str2) {
        Rlog.e(TAG, str + " --> " + str2);
    }
}
