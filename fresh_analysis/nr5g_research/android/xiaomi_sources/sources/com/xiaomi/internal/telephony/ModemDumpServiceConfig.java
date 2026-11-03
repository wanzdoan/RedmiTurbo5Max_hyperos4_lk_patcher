package com.xiaomi.internal.telephony;

import android.content.Context;
import android.content.Intent;
import com.xiaomi.modem.ModemUtils;

/* JADX INFO: loaded from: classes.dex */
public class ModemDumpServiceConfig {
    private static final boolean DBG = true;
    private static final String LOG_TAG = "MtkModemDumpServiceConfig";

    public static void startService(Context context) {
        log("startService");
        context.startService(new Intent(context, (Class<?>) ModemDebugService.class));
        log("start ModemDebugService for Mi product");
    }

    private static void log(String msg) {
        ModemUtils.logd(LOG_TAG, msg);
    }
}
