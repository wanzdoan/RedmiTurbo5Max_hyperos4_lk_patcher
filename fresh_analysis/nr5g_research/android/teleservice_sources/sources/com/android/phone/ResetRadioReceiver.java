package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.telephony.Rlog;
import com.android.internal.os.BackgroundThread;

/* JADX INFO: loaded from: classes.dex */
public class ResetRadioReceiver extends BroadcastReceiver {
    public static String EXTRA_KEY_RESET_RADIO_FOR_HANGUP_OPTIMIZE = "reset_radio_for_hangup_optimize";
    private static String TAG = "ResetRadioReceiver";
    private static ResetRadioReceiver sInstance;

    public static void register(Context context) {
        if (sInstance == null) {
            sInstance = new ResetRadioReceiver(context);
        }
    }

    private ResetRadioReceiver(Context context) {
        if (context == null) {
            Rlog.d(TAG, "context is null...");
        } else {
            Rlog.d(TAG, "register ResetRadioReceiver");
            context.registerReceiver(this, new IntentFilter("miui.intent.action.RESET_RADIO"), "miui.permission.RESET_RADIO", null, 2);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra("reset_rild", false)) {
            Rlog.d(TAG, "resetRadioPower reset_rild return");
        } else if (intent.getBooleanExtra(EXTRA_KEY_RESET_RADIO_FOR_HANGUP_OPTIMIZE, false) && !MiuiPhoneUtils.isPhoneInCall()) {
            Rlog.d(TAG, "resetRadioPower not need for hangup optimize return");
        } else {
            resetRadioPower();
        }
    }

    public void resetRadioPower() {
        Rlog.d(TAG, "resetRadioPower");
        new Handler(BackgroundThread.get().getLooper()).postDelayed(new Runnable(this) { // from class: com.android.phone.ResetRadioReceiver.1
            @Override // java.lang.Runnable
            public void run() {
                PhoneUtils.setRadioPower(false);
                PhoneUtils.setRadioPower(true);
            }
        }, 100L);
    }
}
