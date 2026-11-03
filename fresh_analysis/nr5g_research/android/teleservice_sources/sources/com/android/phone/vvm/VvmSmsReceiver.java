package com.android.phone.vvm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SubscriptionManager;
import android.telephony.VisualVoicemailSms;

/* JADX INFO: loaded from: classes.dex */
public class VvmSmsReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        VisualVoicemailSms visualVoicemailSms = (VisualVoicemailSms) intent.getExtras().getParcelable("android.provider.extra.VOICEMAIL_SMS");
        if (visualVoicemailSms.getPhoneAccountHandle() == null) {
            VvmLog.e("VvmSmsReceiver", "onReceive: Received message for null phone account");
            return;
        }
        int subId = PhoneAccountHandleConverter.toSubId(visualVoicemailSms.getPhoneAccountHandle());
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            VvmLog.e("VvmSmsReceiver", "onReceive: Received message for invalid subId");
            return;
        }
        String string = intent.getExtras().getString("android.provider.extra.TARGET_PACAKGE");
        if (RemoteVvmTaskManager.hasRemoteService(context, subId, string)) {
            VvmLog.i("VvmSmsReceiver", "onReceive: Sending SMS received event to remote service");
            RemoteVvmTaskManager.startSmsReceived(context, visualVoicemailSms, string);
        } else {
            VvmLog.w("VvmSmsReceiver", "onReceive: No remote service to handle SMS received event");
        }
    }
}
