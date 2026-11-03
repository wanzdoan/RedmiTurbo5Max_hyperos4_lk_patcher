package com.android.phone.utils;

import android.content.Intent;
import android.telephony.Rlog;
import com.android.phone.MiuiPhoneUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class ScreenDisplayUtils {
    public static final String TAG = "ScreenDisplayUtils";

    public static void shouldForceSplit(Intent intent, int i) {
        if (intent == null || !MiuiPhoneUtils.isSupportSplit() || (i & 4) == 0) {
            return;
        }
        Rlog.i(TAG, "ScreenDisplayUtils activity force split display");
        intent.addMiuiFlags(16);
    }
}
