package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;

/* JADX INFO: loaded from: classes.dex */
public class XtsBroadcastReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        log("Received: " + intent + ", context = " + context + ", action = " + action);
        ModemUtils.onInitContext(context);
        if ("android.intent.action.BOOT_COMPLETED".equals(action) || "android.intent.action.LOCKED_BOOT_COMPLETED".equals(action)) {
            log("Intent.ACTION_BOOT_COMPLETED or Intent.ACTION_LOCKED_BOOT_COMPLETED");
            if (context == null) {
                log("context is null");
                return;
            }
            return;
        }
        if ("android.provider.Telephony.SECRET_CODE".equals(action)) {
            String host = intent.getData() != null ? intent.getData().getHost() : null;
            new Intent("android.intent.action.MAIN");
            log("TelephonyIntents.SECRET_CODE_ACTION, host: " + host);
            if ("717".equals(host)) {
                if (OemHookAgent.getHookType() == 2) {
                    log("HOST_CODE(717) is only used to wake up XTS of QCOM");
                    return;
                } else if (OemHookAgent.getHookType() == 1) {
                    log("HOST_CODE(717) is only used to wake up XTS of MTK");
                    return;
                } else {
                    log("HOST_CODE(717) is only used to wake up XTS of UNKNOWN");
                    return;
                }
            }
            if ("284".equals(host)) {
                log("HOST_CODE(284) is only used to wake up XTS");
                return;
            } else {
                log("MTB XtsBroadcastReceiver(SECRET_CODE_ACTION) do nothing");
                return;
            }
        }
        log("MTB XtsBroadcastReceiver do nothing");
    }

    private static void log(String str) {
        ModemUtils.logd("XtsBroadcastReceiver", str);
    }
}
