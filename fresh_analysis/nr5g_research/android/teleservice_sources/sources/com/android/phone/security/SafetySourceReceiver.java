package com.android.phone.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.android.internal.telephony.Phone;
import com.android.phone.PhoneGlobals;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class SafetySourceReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String stringExtra;
        if ("android.safetycenter.action.REFRESH_SAFETY_SOURCES".equals(intent.getAction()) && (stringExtra = intent.getStringExtra("android.safetycenter.extra.REFRESH_SAFETY_SOURCES_BROADCAST_ID")) != null && context.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
            refreshSafetySources(stringExtra);
        }
    }

    private void refreshSafetySources(String str) {
        Phone defaultPhone = getDefaultPhone();
        if (defaultPhone != null) {
            defaultPhone.refreshSafetySources(str);
        }
    }

    public Phone getDefaultPhone() {
        try {
            return PhoneGlobals.getPhone();
        } catch (IllegalStateException e) {
            Rlog.i("TelephonySafetySourceReceiver", "Unable to get phone. Skipping safety source refresh: " + e.getMessage());
            return null;
        }
    }
}
