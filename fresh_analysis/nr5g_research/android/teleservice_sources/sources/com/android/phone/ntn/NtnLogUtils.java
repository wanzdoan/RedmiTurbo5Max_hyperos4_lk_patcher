package com.android.phone.ntn;

import android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public abstract class NtnLogUtils {
    public static void logi(String str, String str2) {
        Rlog.i("Ntn-Tele", str + " --> " + str2);
    }

    public static void logd(String str, String str2) {
        Rlog.d("Ntn-Tele", str + " --> " + str2);
    }

    public static void loge(String str, String str2) {
        Rlog.e("Ntn-Tele", str + " --> " + str2);
    }

    public static void logw(String str, String str2) {
        Rlog.w("Ntn-Tele", str + " --> " + str2);
    }
}
