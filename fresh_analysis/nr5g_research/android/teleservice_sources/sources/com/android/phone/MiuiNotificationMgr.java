package com.android.phone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.phone.module.ITelephonyModule;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.a.a;
import java.util.List;
import miui.os.Build;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiNotificationMgr extends BroadcastReceiver {
    private static final String LOG_TAG = MiuiNotificationMgr.class.getSimpleName();
    private static Context mContext;
    private boolean DBG;
    private Handler mHandler;
    private NotificationManager mNotificationManager;
    private SharedPreferences mOsuPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener;
    private SettingsObserver mSettingsObserver;
    private SharedPreferences mSharedPreferences;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("voice_roaming_reminder_action".equals(intent.getAction())) {
            this.mSharedPreferences.edit().putBoolean("has_notification", false).commit();
            return;
        }
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            boolean zIsEsimCapable = MiuiPhoneUtils.isEsimCapable();
            Handler handler = this.mHandler;
            if (zIsEsimCapable) {
                handler.sendMessage(handler.obtainMessage(5));
                Handler handler2 = this.mHandler;
                handler2.sendMessage(handler2.obtainMessage(6));
                return;
            }
            handler.sendMessage(handler.obtainMessage(3));
            return;
        }
        if ("miui.intent.action.ACTION_MLOG_STATUS_CHANGED".equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                return;
            }
            int i = extras.getInt(a.J);
            log("receive MLOG_STATUS_CHANGED status :" + i);
            if (i == 0) {
                hideModemLogNotification(context);
                return;
            } else {
                showModemLogNotification(context);
                return;
            }
        }
        if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra("ss");
            log("receive ACTION_SIM_STATE_CHANGED simState = " + stringExtra);
            if ("ABSENT".equals(stringExtra) || "UNKNOWN".equals(stringExtra)) {
                return;
            }
            cancelESimInactiveNotificationForGlobal(context);
        }
    }

    public MiuiNotificationMgr(PhoneGlobals phoneGlobals) {
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 1 && SystemProperties.getInt("ro.debuggable", 0) == 1;
        this.mPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.android.phone.MiuiNotificationMgr.1
            @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
                if ("osu_dialog_last_shown_time".equals(str)) {
                    MiuiNotificationMgr.this.log("Last shown time preference changed");
                    MiuiNotificationMgr.this.rescheduleOsuNotification();
                }
            }
        };
        this.mHandler = new Handler() { // from class: com.android.phone.MiuiNotificationMgr.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        removeMessages(2);
                        sendMessageDelayed(obtainMessage(2), 1500L);
                        break;
                    case 1:
                        for (Phone phone : MiuiPhoneUtils.getPhones()) {
                            if (PhoneAdapter.getInstance().getVoiceRoaming(PhoneProxy.getServiceStateTracker(phone).mSS)) {
                            }
                            break;
                        }
                        removeMessages(2);
                        MiuiNotificationMgr.this.hideVoiceRoamingOn();
                        break;
                    case 2:
                        MiuiNotificationMgr.this.showVoiceRoamingOn();
                        break;
                    case 3:
                    case 4:
                    case 5:
                        if (MiuiPhoneUtils.isEsimCapable()) {
                            MiuiNotificationMgr.this.showESimInactiveNotificationForGlobal(MiuiNotificationMgr.mContext);
                        } else if (MiuiPhoneUtils.isSupportGoogleLPAEsim()) {
                            MiuiNotificationMgr.this.showESimInactiveNotification(MiuiNotificationMgr.mContext);
                        }
                        break;
                    case 6:
                        MiuiNotificationMgr.this.log("receive EVENT_OSU_NOTIFICATION");
                        long j = Settings.Secure.getLong(MiuiNotificationMgr.mContext.getContentResolver(), "osu_interval_time", -1L);
                        if (MiuiPhoneUtils.isEsimCapable() && MiuiPhoneUtils.isSupportOsuFeature() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEsimSecureValue() == 1 && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).shouldShowOsuDialog(j)) {
                            MiuiNotificationMgr.this.showOsuNotification(MiuiNotificationMgr.mContext);
                            break;
                        }
                        break;
                }
            }
        };
        mContext = phoneGlobals;
        this.mNotificationManager = (NotificationManager) phoneGlobals.getSystemService("notification");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("voice_roaming_reminder_action");
        log("MiuiNotificationMgr registerSettingsObserver");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        SettingsObserver settingsObserver = new SettingsObserver(mContext, this.mHandler);
        this.mSettingsObserver = settingsObserver;
        settingsObserver.observe(Settings.Global.getUriFor("device_provisioned"), 4);
        intentFilter.addAction("miui.intent.action.ACTION_MLOG_STATUS_CHANGED");
        mContext.registerReceiver(this, intentFilter, 2);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(phoneGlobals);
        this.mOsuPreferences = defaultSharedPreferences;
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this.mPreferenceListener);
        this.mSharedPreferences = mContext.getSharedPreferences("voice_roaming_reminder_interval", 0);
        registerForVoiceRoaming();
    }

    void registerForVoiceRoaming() {
        PhoneAdapter.getInstance().registerForVoiceRoaming(this.mHandler, 0, 1);
    }

    void showVoiceRoamingOn() {
        if (this.DBG) {
            log("showVoiceRoamingOn()...");
        }
        if (isNoNotificationOnDomesticRoaming() || MiuiSettings.Telephony.isAutoCountryCodeEnable(mContext.getContentResolver()) || this.mSharedPreferences.getInt("delete_deminder_time", 0) >= 3 || this.mSharedPreferences.getBoolean("has_notification", false) || System.currentTimeMillis() - this.mSharedPreferences.getLong("pre_date", 0L) < 2592000000L) {
            return;
        }
        this.mSharedPreferences.edit().putLong("pre_date", System.currentTimeMillis()).putInt("delete_deminder_time", this.mSharedPreferences.getInt("delete_deminder_time", 0) + 1).putBoolean("has_notification", true).commit();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.TelocationSetting"));
        intent.putExtra("automatic_add_country_code", true);
        PendingIntent activity = PendingIntent.getActivity(mContext, 0, intent, 201326592);
        Notification.Builder deleteIntent = new Notification.Builder(mContext).setSmallIcon(android.R.drawable.stat_sys_warning).setContentTitle(mContext.getText(R.string.roaming_notification_title)).setContentText(mContext.getText(R.string.roaming_notification_content)).setAutoCancel(true).setContentIntent(activity).setDeleteIntent(PendingIntent.getBroadcast(mContext, 0, new Intent("voice_roaming_reminder_action"), 201326592));
        MiuiNotificationMgrUtils.setChannelId(deleteIntent, "voiceRoamingOn");
        this.mNotificationManager.notify(1, deleteIntent.build());
    }

    void hideVoiceRoamingOn() {
        if (this.DBG) {
            log("hideVoiceRoamingOn()...");
        }
        this.mNotificationManager.cancel(1);
        this.mSharedPreferences.edit().putBoolean("has_notification", false).commit();
    }

    public static boolean isNoNotificationOnDomesticRoaming() {
        Phone[] phones = MiuiPhoneUtils.getPhones();
        boolean z = false;
        for (int i = 0; i < phones.length; i++) {
            ServiceState serviceState = PhoneProxy.getServiceStateTracker(phones[i]).mSS;
            if (TelephonyManager.isSupportDomesticRoaming() || isSupportedNoNotificationOnDomesticRoaming(serviceState, phones[i])) {
                String operatorNumeric = phones[i].getOperatorNumeric();
                int dataRoamingType = serviceState.getDataRoamingType();
                int voiceRoamingType = serviceState.getVoiceRoamingType();
                Log.d(LOG_TAG, "mccmnc = " + operatorNumeric + ",serviceState = " + serviceState + ",isDomesticRoaming.VoiceRoamingType() = " + voiceRoamingType + ",isDomesticRoaming.DataRoamingType() =" + dataRoamingType);
                if (dataRoamingType == 2 || dataRoamingType == 1 || voiceRoamingType == 2 || voiceRoamingType == 1) {
                    z = true;
                    break;
                }
            }
        }
        Log.d(LOG_TAG, "isNoNotificationOnDomesticRoaming = " + z);
        return z;
    }

    private static boolean isSupportedNoNotificationOnDomesticRoaming(ServiceState serviceState, Phone phone) {
        String operatorNumeric = serviceState.getOperatorNumeric();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) mContext.getSystemService("carrier_config");
        String[] stringArray = null;
        if (carrierConfigManager != null) {
            try {
                PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(phone.getSubId());
                if (configForSubId != null) {
                    stringArray = configForSubId.getStringArray("config_non_roaming_notification_for_domestic_roaming");
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "isSupportedNoNotificationOnDomesticRoaming: carrier config error: " + e.toString());
            }
        }
        if (!ArrayUtils.isEmpty(stringArray) && operatorNumeric != null) {
            for (String str : stringArray) {
                if (!TextUtils.isEmpty(str) && operatorNumeric.startsWith(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showESimInactiveNotification(Context context) {
        if (!MiuiPhoneUtils.isSupportGoogleLPAEsim() || Settings.Secure.getInt(context.getContentResolver(), "device_provisioned", 0) != 1) {
            log("showESimInactiveNotification()..DEVICE_PROVISIONED false");
            return;
        }
        boolean z = MiuiEsimManagerBase.getInstance() != null && MiuiEsimManagerBase.getInstance().isEsimActive();
        boolean z2 = MiuiEsimManagerBase.getInstance() != null && MiuiEsimManagerBase.getInstance().isSupportEsim();
        log("showESimInactiveNotification()..isActive=" + z + ",isSupportEsim =" + z2);
        List availableSubscriptionInfoList = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        if (!z2 || z || availableSubscriptionInfoList == null || availableSubscriptionInfoList.size() != 0) {
            return;
        }
        log("showESimInactiveNotification notify");
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MobileNetworkSettings"));
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 1107296256);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getString(R.string.esim_inactive_settings_title)).setSmallIcon(R.drawable.icon_volte_provision_notfication).setAutoCancel(true).setShowWhen(false).setContentIntent(activity).setStyle(new Notification.BigTextStyle(builder).bigText(context.getString(R.string.esim_inactive_notification_message))).setChannel("eSimInActiveNotification");
        this.mNotificationManager.notify(2, builder.build());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showESimInactiveNotificationForGlobal(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), "device_provisioned", 0) != 1) {
            log("showESimInactiveNotificationForGlobal..DEVICE_PROVISIONED false");
            return;
        }
        if (MiuiPhoneUtils.isEsimCapable()) {
            boolean z = MiuiEsimManagerBase.getInstance() != null && MiuiEsimManagerBase.getInstance().isEsimActive();
            int esimSecureValue = MiuiEsimManagerBase.getEsimSecureValue();
            List availableSubscriptionInfoList = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
            if (MiuiEsimManagerBase.getInstance() == null || !MiuiEsimManagerBase.getInstance().isSupportForRegion()) {
                return;
            }
            if ((esimSecureValue == -1 || MiuiEsimManagerBase.isRemoveEsimSwitch()) && !z && availableSubscriptionInfoList != null && availableSubscriptionInfoList.size() == 0) {
                log("showESimInactiveNotificationForGlobal... notify");
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MobileNetworkSettings"));
                PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 1107296256);
                Notification.Builder builder = new Notification.Builder(context);
                builder.setContentTitle(context.getString(R.string.network_settings_title)).setSmallIcon(R.drawable.ic_esim_global_notify).setAutoCancel(true).setShowWhen(false).setContentIntent(activity).setContentText(context.getString(R.string.esim_inactive_notification_message_global)).setChannel("eSimInActiveNotificationForGlobal");
                this.mNotificationManager.notify(3, builder.build());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showOsuNotification(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), "device_provisioned", 0) != 1) {
            log("showOsuNotification..DEVICE_PROVISIONED false");
            return;
        }
        if (MiuiPhoneUtils.isEsimCapable() && MiuiEsimManagerBase.getEsimSecureValue() == 1) {
            log("showOsuNotification notify");
            Intent intent = new Intent();
            intent.setClass(context, MiuiOsuDialogActivity.class);
            intent.putExtra("esim_os_update", true);
            PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 1107296256);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle(context.getString(R.string.osu_notification_title)).setSmallIcon(R.drawable.ic_esim_global_notify).setAutoCancel(true).setShowWhen(false).setStyle(new Notification.BigTextStyle(builder).bigText(context.getString(R.string.osu_notification_message))).addAction(new Notification.Action.Builder(0, context.getString(R.string.osu_dialog_ok_btn_text), activity).build()).setChannel("osuNotification");
            this.mNotificationManager.notify(6, builder.build());
        }
    }

    private void scheduleOsuNotificationDelayed(long j) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(6);
            this.mHandler.sendEmptyMessageDelayed(6, j);
            log("OSU notification scheduled in " + j + "ms");
        }
        log("handler is null");
    }

    private void scheduleNextOsuNotification() {
        long j = Settings.Secure.getLong(mContext.getContentResolver(), "osu_interval_time", -1L);
        long j2 = this.mOsuPreferences.getLong("osu_dialog_last_shown_time", 0L);
        long jMax = Math.max(0L, (1000 * j) - (System.currentTimeMillis() - j2));
        log("schedualNextOsuNotification: lastShownTime = " + j2 + ", osuIntervalTime = " + j + ", remainingTime = " + jMax);
        scheduleOsuNotificationDelayed(jMax);
    }

    private void cancelScheduledOsuNotification() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(6);
            log("All scheduled OSU notifications cancelled");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rescheduleOsuNotification() {
        log("rescheduleOsuNotification due to preference change");
        cancelScheduledOsuNotification();
        scheduleNextOsuNotification();
    }

    private void cancelESimInactiveNotificationForGlobal(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        for (StatusBarNotification statusBarNotification : notificationManager.getActiveNotifications()) {
            if ("eSimInActiveNotificationForGlobal".equals(statusBarNotification.getNotification().getChannelId())) {
                notificationManager.cancel(3);
                log("cancelESimInactiveNotificationForGlobal done...");
                return;
            }
        }
        log("cancelESimInactiveNotificationForGlobal is not exists...");
    }

    private void showModemLogNotification(Context context) {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return;
        }
        log("showModemLogNotification");
        this.mNotificationManager.notify("modem_log", 4, new Notification.Builder(context).setSmallIcon(android.R.drawable.stat_sys_warning).setContentTitle(context.getString(R.string.modem_log_notification_title)).setContentText(context.getText(R.string.modem_log_notification__message)).setAutoCancel(false).setShowWhen(false).setOngoing(true).setChannelId("alert").build());
    }

    private void hideModemLogNotification(Context context) {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return;
        }
        log("hideModemLogNotification");
        this.mNotificationManager.cancel("modem_log", 4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
