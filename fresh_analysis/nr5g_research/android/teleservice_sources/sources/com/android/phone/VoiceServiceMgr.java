package com.android.phone;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.MiuiSettings;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class VoiceServiceMgr extends BroadcastReceiver {
    private static final String LOG_TAG = VoiceServiceMgr.class.getSimpleName();
    private static VoiceServiceMgr mInstance = null;
    private boolean isImsRegisted = false;
    private Context mContext;
    private NotificationManager mNotificationManager;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra("ss");
            if ("ABSENT".equals(stringExtra)) {
                hideVoiceServieNotAvailableNotification();
                return;
            } else {
                if (!"LOADED".equals(stringExtra) || TelephonyManager.getDefault().isImsRegistered(intent.getIntExtra("phone", 0))) {
                    return;
                }
                showVoiceServieNotAvailableNotification();
                return;
            }
        }
        if ("android.intent.action.ACTION_IMS_REGISTED".equals(intent.getAction())) {
            boolean booleanExtra = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
            this.isImsRegisted = booleanExtra;
            if (booleanExtra) {
                hideVoiceServieNotAvailableNotification();
            } else {
                showVoiceServieNotAvailableNotification();
            }
        }
    }

    public VoiceServiceMgr(PhoneGlobals phoneGlobals) {
        this.mContext = phoneGlobals;
        mInstance = this;
        this.mNotificationManager = (NotificationManager) phoneGlobals.getSystemService("notification");
        boolean enabledVoiceService = MiuiSettings.Telephony.getEnabledVoiceService(this.mContext.getContentResolver());
        log("init Voice Service, isVoiceServiceEnabled: " + enabledVoiceService);
        if (enabledVoiceService) {
            register();
        }
    }

    public static VoiceServiceMgr getInstance() {
        if (mInstance == null) {
            Log.e(LOG_TAG, "VoiceServiceMgr has not initialized.");
        }
        return mInstance;
    }

    private void register() {
        log("register receiver.");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_IMS_REGISTED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this, intentFilter, 2);
    }

    private void unregister() {
        log("unregister receiver.");
        this.isImsRegisted = false;
        try {
            this.mContext.unregisterReceiver(this);
        } catch (IllegalArgumentException unused) {
        }
    }

    private void showVoiceServieNotAvailableNotification() {
        log("showVoiceServieNotAvailableNotification...");
        this.mContext.getText(R.string.roaming_notification_content);
        Notification.Builder ongoing = new Notification.Builder(this.mContext).setSmallIcon(android.R.drawable.stat_sys_warning).setContentTitle(this.mContext.getText(R.string.voice_service_cannot_call_notification_title)).setContentText(this.mContext.getText(R.string.voice_service_cannot_call_notification_content)).setAutoCancel(false).setOngoing(true);
        MiuiNotificationMgrUtils.setChannelId(ongoing, "voiceNotAvailable");
        this.mNotificationManager.notify(1, ongoing.build());
    }

    private void hideVoiceServieNotAvailableNotification() {
        if (this.mNotificationManager != null) {
            log("hideVoiceServieNotAvailableNotification...");
            this.mNotificationManager.cancel(1);
        }
    }

    public void enableVoiceService(Phone phone, boolean z) {
        log("enable voice service : " + z);
        MiuiSettings.Telephony.setVoiceServiceEnabled(this.mContext.getContentResolver(), z);
        ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(this.mContext, z, phone.getPhoneId());
        VolteEnableManager.getInstance().saveUserSelectedVolteState(phone.getSubId(), z);
        if (z) {
            register();
        } else {
            unregister();
            hideVoiceServieNotAvailableNotification();
        }
    }

    private void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
