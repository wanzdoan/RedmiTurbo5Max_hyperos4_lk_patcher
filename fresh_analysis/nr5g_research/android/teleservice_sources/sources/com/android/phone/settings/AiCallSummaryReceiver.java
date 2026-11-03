package com.android.phone.settings;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.phone.R;
import com.android.phone.utils.OneTrackImpl;
import com.ot.pubsub.a.a;
import java.util.HashMap;
import miuix.preference.flexible.PreferenceMarkLevel;

/* JADX INFO: loaded from: classes.dex */
public class AiCallSummaryReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String str;
        long longExtra = intent.getLongExtra("contactCallLogId", -1L);
        String stringExtra = intent.getStringExtra("ai_summary");
        String stringExtra2 = intent.getStringExtra("ai_summary_title");
        if (stringExtra == null) {
            stringExtra = "";
        }
        if (stringExtra2 == null) {
            stringExtra2 = "";
        }
        if (stringExtra2.contains("已生成")) {
            str = "success";
        } else if (stringExtra2.contains("无法生成")) {
            str = "no_info_skip";
        } else {
            str = "failed_retryable";
        }
        HashMap map = new HashMap();
        map.put(a.J, str);
        OneTrackImpl.trackObjectEvent("ai_call_summary_result", map);
        Log.i("AiCallSummaryReceiver", "AI call summary result: ".concat(str));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        if (notificationManager == null) {
            return;
        }
        Intent intent2 = new Intent("miui.intent.action.AiCallDetail");
        intent2.setPackage("com.xiaomi.phone");
        intent2.putExtra("contactCallLogId", longExtra);
        intent2.setFlags(335544320);
        notificationManager.notify(PreferenceMarkLevel.LEVEL_NORMAL_ONLY_TITLE, new Notification.Builder(context, "ai_call_summary").setSmallIcon(R.drawable.ic_ai_summary_notification).setContentTitle(stringExtra2).setContentText(stringExtra).setPriority(1).setDefaults(1).setAutoCancel(true).setContentIntent(PendingIntent.getActivity(context, (int) longExtra, intent2, 201326592)).build());
        OneTrackImpl.trackEvent("ai_call_summary_notify_expose");
        Log.i("AiCallSummaryReceiver", "AI call summary notify expose");
    }
}
