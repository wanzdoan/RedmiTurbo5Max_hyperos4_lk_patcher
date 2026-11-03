package com.xiaomi.onetrack.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes2.dex */
class ai extends BroadcastReceiver {
    final /* synthetic */ ah a;

    ai(ah ahVar) {
        this.a = ahVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            if (this.a.d != null) {
                String action = intent.getAction();
                if (TextUtils.isEmpty(action)) {
                    return;
                }
                if (action.equals("android.intent.action.SCREEN_ON")) {
                    this.a.d.sendEmptyMessageDelayed(100, 500L);
                } else if (action.equals("android.intent.action.SCREEN_OFF")) {
                    this.a.d.sendEmptyMessageDelayed(101, 500L);
                }
            }
        } catch (Throwable th) {
            com.xiaomi.onetrack.util.r.b("BroadcastManager", "screenReceiver throwable: " + th.getMessage());
        }
    }
}
