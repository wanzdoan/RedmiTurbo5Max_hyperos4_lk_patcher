package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class MiuiEsimRoamingCheckReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("miui.intent.action.ESIM_ROAMING_CHECK".equals(intent.getAction())) {
            Log.d("MiuiEsimRoamingCheckReceiver", "Received ESIM roaming check broadcast");
            Intent intent2 = new Intent(context, (Class<?>) MiuiEsimRoamingDialogActivity.class);
            intent2.addFlags(335544320);
            context.startActivity(intent2);
        }
    }
}
