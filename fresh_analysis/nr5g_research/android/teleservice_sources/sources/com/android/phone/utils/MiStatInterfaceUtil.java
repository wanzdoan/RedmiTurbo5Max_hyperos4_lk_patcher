package com.android.phone.utils;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.ims.ImsManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.statistics.utils.DeviceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.Map;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.SubscriptionManagerEx;
import miui.telephony.TelephonyManager;
import miuix.provider.ExtraSettings$Secure;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiStatInterfaceUtil {
    private static final String LOG_TAG = "MiStatInterfaceUtil";
    private static final MiStatHandler mHandler = new MiStatHandler(MiuiThreadUtil.getWorkThreadLooper());
    private static BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.utils.MiStatInterfaceUtil.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            Log.d(MiStatInterfaceUtil.LOG_TAG, "action = " + action);
            if ("com.miui.clause_agreed".equals(action) || "android.provision.action.PROVISION_COMPLETE".equals(action)) {
                ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).enableOnetrackStatAccessNetwork(context, MiStatInterfaceUtil.useOneTrack());
            }
        }
    };
    public static boolean sIsUserExperienceEnabled = false;

    public static final void init(Context context) {
        if (isAllowedInitStat()) {
            MiStatHandler miStatHandler = mHandler;
            miStatHandler.sendMessageDelayed(miStatHandler.obtainMessage(0, context), 20000L);
        }
    }

    public static final void recordSwitchStatusEvent(String str, boolean z) {
        HashMap map = new HashMap();
        map.put("param_preference_key", str);
        map.put("param_switch_value", Boolean.valueOf(z));
        recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "key_mobile_settings_statistic", map);
    }

    public static final void recordDefaultSlotIdEvent(String str, int i) {
        HashMap map = new HashMap();
        map.put("param_preference_key", str);
        map.put("param_default_slot_id", Integer.valueOf(i));
        recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "key_mobile_settings_statistic", map);
    }

    public static final void recordCountEvent(String str, String str2) {
        if (!sIsUserExperienceEnabled || isForbiddenRecordEvent(str)) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("extra_category", str);
        bundle.putString("extra_key", str2);
        Message messageObtainMessage = mHandler.obtainMessage(1);
        messageObtainMessage.setData(bundle);
        messageObtainMessage.sendToTarget();
    }

    public static final void recordCountEvent(String str, String str2, Map map) {
        HashMap map2 = new HashMap();
        if (map != null) {
            map2.putAll(map);
        }
        recordObjectEvent(str, str2, map2);
    }

    public static final void recordObjectEvent(String str, String str2, Map map) {
        if (com.android.phone.statistics.utils.Utils.IS_INTERNATIONAL_BUILD) {
            if (DeviceUtil.isInternationalXmsRecordEnabled() && isInternationalXmsEvent(str2)) {
                reportXmsEvent(str2, map);
            }
        } else if (DeviceUtil.isXmsRecordEnabled() && isXmsEvent(str2)) {
            reportXmsEvent(str2, map);
        }
        if (sIsUserExperienceEnabled) {
            Map mapRecordMiTelephonyEvent = TelephonyManager.getDefault().recordMiTelephonyEvent(str, str2);
            if (!mapRecordMiTelephonyEvent.isEmpty() && map != null) {
                map.putAll(mapRecordMiTelephonyEvent);
                Log.d(LOG_TAG, "MiTelephony recordObjectEvent category = " + str + ", key " + str2);
            }
            Bundle bundle = new Bundle();
            bundle.putString("extra_category", str);
            bundle.putString("extra_key", str2);
            Message messageObtainMessage = mHandler.obtainMessage(2, map);
            messageObtainMessage.setData(bundle);
            messageObtainMessage.sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public abstract class PhoneSettings {
        private static final String[] PARAMS_FOLD_FEATURES = {"CKEnableFoldAnswer", "CKEnableUnFoldTurnonSpeaker", "CKEnableUnFoldColseCamera", "CKEnableUnFoldShowInCallBubble"};
        private static final String[] FOLD_FEATURES_SETTINGS = {"fold_answer", "unfold_turn_on_speaker", "unfold_close_camera", "unfold_show_incall_bubble"};
        private static final String[] PARAMS_CALL_FEATURES = {"CKEnableSensorAnswer"};
        private static final String[] CALL_FEATURES_SETTINGS = {"sensor_answer"};
        private static final String[] PARAMS_ANSWER_STATE = {"CKEnableAntiStrange", "CKEnableProximitySensor", "CKEnableFlashWhenRing", "CKEnableTurnOverMuteMode", "CKEnableHandonMode", "CKEnableCrescendoMode"};
        private static final String[] ANSWER_STATE_SETTINGS = {"button_antispam_strange", "button_enable_proximity", "flash_when_ring_enabled", "button_turn_over_mute", "button_handon_ringer", "button_crescendo_ringer"};
        private static final String[] PARAMS_AUTO_IP = {"CKEnableAutoIp", "CKCurrentAreaCode", "CKSupportLocalNumber", "CKEnableAddZeroPrefix"};
        private static final String[] AUTO_IP_SETTINGS = {"button_autoip", "current_areacode", "button_auto_ip_support_local_numbers", "button_add_zero_prefix"};
        private static final String[] PARAMS_CALL_ADVANCED = {"CKT9IndexMethod", "CKEnableAutoRedial", "CKMissedCallNotifyTime", "CKEnableConnectDisconnectVibrate", "CKCallWaitingTone", "CKDTMFToneType"};
        private static final String[] CALL_ADVANCED_SETTINGS = {"t9_indexing_key", "button_auto_redial", "button_missed_call_notify_times", "button_connect_disconnect_vibrate", "call_waiting_tone", "dtmf_tone_type", "button_enable_reject_via_sms"};
        private static final String[] PARAMS_CALL_RECORD = {"CKEnableRecordingNotification", "CKEnableAutoRecord", "CKAutoRecordScenarios", "CKEnableUnknownNumberRecord", "CKEnableYellowPageNumberRecord"};
        private static final String[] CALL_RECORD_SETTINGS = {"button_call_recording_notification", "button_auto_record_call", "radio_record_scenario", "button_record_unknown_number", "button_record_yellowpage_number"};
        private static final String[] PARAMS_TELOCATION = {"CKEnableShowLocation", "CKEnableAutoCountryCode", "CKCountryCode"};
        private static final String[] TELOCATION_SETTINGS = {"enable_telocation", "auto_country_code", "persist.radio.countrycode"};

        public static final void recordSettingsEvent(Context context) {
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).oneTrackEvent("key_fold_features_settings", getSettingData(context, PARAMS_FOLD_FEATURES, FOLD_FEATURES_SETTINGS));
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).oneTrackEvent("key_call_features_settings", getSettingData(context, PARAMS_CALL_FEATURES, CALL_FEATURES_SETTINGS));
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).oneTrackEvent("key_answer_state_settings", getSettingData(context, PARAMS_ANSWER_STATE, ANSWER_STATE_SETTINGS));
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).oneTrackEvent("key_auto_answer_settings", getAutoAnswerData(context));
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).oneTrackEvent("key_auto_ip_settings", getAutoIpData(context));
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).oneTrackEvent("key_call_advanced_settings", getSettingData(context, PARAMS_CALL_ADVANCED, CALL_ADVANCED_SETTINGS));
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).oneTrackEvent("key_call_record_settings", getSettingData(context, PARAMS_CALL_RECORD, CALL_RECORD_SETTINGS));
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).oneTrackEvent("key_telocation_settings", getSettingData(context, PARAMS_TELOCATION, TELOCATION_SETTINGS));
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).oneTrackEvent("key_mobile_network_settings", getMobileNetworkData(context));
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).oneTrackEvent("key_call_advanced_settings", getVoiceReportSettingData(context));
        }

        public static final void recordSlotIdInMultiSimInfoEditor(String str, int i) {
            HashMap map = new HashMap();
            map.put("param_slot_id", String.valueOf(i));
            map.put("multisim_info_editor_enter", "multisim_info_editor_enter");
            MiStatInterfaceUtil.recordCountEvent("event_settings", str, map);
        }

        private static Map getAutoAnswerData(Context context) {
            HashMap map = new HashMap();
            MiStatInterfaceUtil.getSharedPreferences(context);
            map.put("CKEnableAutoAnswer", String.valueOf(MiuiSettings.Telephony.isAutoAnswerCall(context.getContentResolver())));
            map.put("CKAutoAnswerDelay", String.valueOf(MiuiSettings.Telephony.getAutoAnswerCallDelayTime(context.getContentResolver())));
            map.put("CKAutoAnswerScenario", String.valueOf(MiuiSettings.Telephony.getAutoAnswerCallScenario(context.getContentResolver())));
            return map;
        }

        private static Map getAutoIpData(Context context) {
            HashMap map = new HashMap();
            ContentResolver contentResolver = context.getContentResolver();
            for (int i = 0; i < android.telephony.TelephonyManager.getDefault().getPhoneCount(); i++) {
                int i2 = 0;
                while (true) {
                    String[] strArr = PARAMS_AUTO_IP;
                    if (i2 < strArr.length) {
                        map.put(strArr[i2] + i, MiuiSettings.System.getString(contentResolver, AUTO_IP_SETTINGS[i2] + i));
                        i2++;
                    }
                }
            }
            return map;
        }

        private static Map getMobileNetworkData(Context context) {
            HashMap map = new HashMap();
            map.put("CKMobileNetworkSystemSelect", String.valueOf(Settings.Global.getInt(context.getContentResolver(), "roaming_settings", 0)));
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                try {
                    map.put("CKMobileNetworkNetworkType_" + i, String.valueOf(android.telephony.TelephonyManager.getIntAtIndex(context.getContentResolver(), "preferred_network_mode", i)));
                } catch (Settings.SettingNotFoundException e) {
                    Log.e(MiStatInterfaceUtil.LOG_TAG, "setting of PREFERRED_NETWORK_MODE is not found " + e);
                }
            }
            map.put("CKMobileNetworkDataEnable", String.valueOf(android.telephony.TelephonyManager.from(context).getDataEnabled()));
            map.put("CKMobileNetwork4gLTE", String.valueOf(ImsManager.isEnhanced4gLteModeSettingEnabledByUser(context)));
            map.put("CKobileNetworkMmsEnabled", String.valueOf(Settings.System.getInt(context.getContentResolver(), "always_enable_mms", 1)));
            if (MiuiPhoneUtils.isMultiSimEnabled()) {
                map.put("CKMobileNetworkDefaultData", String.valueOf(SubscriptionManagerEx.getDefault().getDefaultDataSlotId()));
                map.put("CKMobileNetworkDefaultVoice", String.valueOf(SubscriptionManagerEx.getDefault().getDefaultVoiceSlotId()));
                if (!MiuiPhoneUtils.hidePrefSMSSettings()) {
                    map.put("CKMobileNetworkDefaultSms", String.valueOf(SubscriptionManager.getDefault().getDefaultSmsSlotId()));
                }
            }
            if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED && !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDisableLte() && !MiuiPhoneUtils.isTabletDeviceWithVolteCapability() && !MiuiPhoneUtils.isRemoveDual4GSwitch()) {
                map.put("CKMobileNetworkDual4G", String.valueOf(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled()));
                map.put("CKMobileNetworkViceSlotVolteData", String.valueOf(Settings.Global.getInt(context.getContentResolver(), MiuiPhoneUtils.VICE_SLOT_VOLTE_DATA_ENABLED, 0)));
            }
            if (MiuiPhoneUtils.isSupportVoiceLinkFeature() && com.android.phone.statistics.utils.Utils.IS_INTERNATIONAL_BUILD && !((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isSupportVoiceLinkByCloud()) {
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    if (i2 != DefaultSimManager.getDefaultDataSlotId()) {
                        map.put("CKVoiceLinkEnable_" + i2, String.valueOf(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isCrossSimCallingEnabledByUser(context, i2)));
                    }
                }
            }
            return map;
        }

        private static Map getSettingData(Context context, String[] strArr, String[] strArr2) {
            HashMap map = new HashMap();
            ContentResolver contentResolver = context.getContentResolver();
            for (int i = 0; i < strArr.length; i++) {
                map.put(strArr[i], MiuiSettings.System.getString(contentResolver, strArr2[i]));
            }
            return map;
        }

        private static Map getVoiceReportSettingData(Context context) {
            HashMap map = new HashMap();
            map.put("CKInComingCallReport", Settings.Global.getInt(context.getContentResolver(), "voice_report_method_phone", 2) + "");
            return map;
        }
    }

    class MiStatHandler extends Handler {
        MiStatHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                try {
                    Context context = (Context) message.obj;
                    if (MiStatInterfaceUtil.useOneTrack()) {
                        OneTrackImpl.init(context);
                        Log.d(MiStatInterfaceUtil.LOG_TAG, "OneTrackImpl init done!");
                    } else if (MiuiPhoneUtils.isInternationalStatsSupported()) {
                        PubSubTrackImpl.init(context);
                        Log.d(MiStatInterfaceUtil.LOG_TAG, "PubSubTrackImpl init done!");
                    }
                    PhoneSettings.recordSettingsEvent(context);
                    MiStatInterfaceUtil.registerCloudDataObserver(context);
                    MiStatInterfaceUtil.registerUserExperienceObserver(context);
                    MiStatInterfaceUtil.registerBroadCastReceiver(context);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (i == 1) {
                try {
                    Bundle data = message.getData();
                    if (!MiStatInterfaceUtil.useOneTrack()) {
                        PubSubTrackImpl.trackObjectEvent(data.getString("extra_key"), null);
                        return;
                    } else {
                        OneTrackImpl.trackEvent(data.getString("extra_key"), null);
                        return;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
            if (i != 2) {
                return;
            }
            try {
                Bundle data2 = message.getData();
                if (!MiStatInterfaceUtil.useOneTrack()) {
                    PubSubTrackImpl.trackObjectEvent(data2.getString("extra_key"), (Map) message.obj);
                } else {
                    OneTrackImpl.trackObjectEvent(data2.getString("extra_key"), (Map) message.obj);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean useOneTrack() {
        String str = SystemProperties.get("ro.miui.build.region");
        return "cn".equalsIgnoreCase(str) || "ru".equalsIgnoreCase(str);
    }

    public static void enableOneTrackDebugMode(boolean z) {
        if (z) {
            if (useOneTrack()) {
                OneTrackImpl.enableDebugMode();
                Log.d(LOG_TAG, "OneTrackImpl enableDebugMode!");
            } else if (MiuiPhoneUtils.isInternationalStatsSupported()) {
                PubSubTrackImpl.enableDebugMode();
                Log.d(LOG_TAG, "PubSubTrackImpl enableDebugMode!");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerCloudDataObserver(final Context context) {
        Log.d(LOG_TAG, "registerCloudDataObserver Enter");
        context.getContentResolver().registerContentObserver(MiuiSettings.SettingsCloudData.getCloudDataNotifyUri(), true, new ContentObserver(mHandler) { // from class: com.android.phone.utils.MiStatInterfaceUtil.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                Log.d(MiStatInterfaceUtil.LOG_TAG, "registerCloudDataObserver onChange");
                ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).resetByCause(context, 1);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerUserExperienceObserver(final Context context) {
        Log.d(LOG_TAG, "registerUserExperienceObserver Enter");
        getUserExperienceEnabled(context);
        context.getContentResolver().registerContentObserver(ExtraSettings$Secure.getUriFor("upload_log_pref"), false, new ContentObserver(mHandler) { // from class: com.android.phone.utils.MiStatInterfaceUtil.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                Log.d(MiStatInterfaceUtil.LOG_TAG, "registerUserExperienceObserver onChange");
                ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).resetByCause(context, 0);
                MiStatInterfaceUtil.getUserExperienceEnabled(context);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerBroadCastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.miui.clause_agreed");
        intentFilter.addAction("android.provision.action.PROVISION_COMPLETE");
        context.registerReceiver(mReceiver, intentFilter, 2);
    }

    private static boolean isAllowedInitStat() {
        return !com.android.phone.statistics.utils.Utils.IS_INTERNATIONAL_BUILD || com.android.phone.statistics.utils.Utils.isGlobalStatEnabled();
    }

    private static boolean isForbiddenRecordEvent(String str) {
        return com.android.phone.statistics.utils.Utils.IS_INTERNATIONAL_BUILD && !"modem_ue_statistic".equals(str);
    }

    public static boolean isForbiddenRecordMqsEvent() {
        return com.android.phone.statistics.utils.Utils.IS_INTERNATIONAL_BUILD || !sIsUserExperienceEnabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getUserExperienceEnabled(Context context) {
        sIsUserExperienceEnabled = com.android.phone.statistics.utils.Utils.isUserExperienceProgramEnabled(context);
    }

    private static boolean isXmsEvent(String str) {
        return "data_evaluation_error".equalsIgnoreCase(str) || "data_setup_call_error".equalsIgnoreCase(str) || "call_unexpected_disconnected".equalsIgnoreCase(str) || "call_dial_out_error".equalsIgnoreCase(str) || "data_fake_connection".equalsIgnoreCase(str) || "data_fake_connection_time".equalsIgnoreCase(str) || "nw_out_of_service".equalsIgnoreCase(str) || "event_modem_crash".equalsIgnoreCase(str) || "event_registered_volteFailed".equalsIgnoreCase(str) || "event_volte_cdma1x_failed".equalsIgnoreCase(str) || "event_lte_fiveg_signal_loss".equalsIgnoreCase(str) || "Signal".equalsIgnoreCase(str) || "FiveGSa".equalsIgnoreCase(str) || "FiveGNsa".equalsIgnoreCase(str) || "Rat".equalsIgnoreCase(str) || "event_nw_out_of_service_duration".equalsIgnoreCase(str) || "event_nw_search_duration".equalsIgnoreCase(str) || "event_sim_abnormal".equalsIgnoreCase(str) || "event_arrear_sim".equalsIgnoreCase(str) || "event_speed_limit_card".equalsIgnoreCase(str);
    }

    private static boolean isInternationalXmsEvent(String str) {
        return "data_setup_call_error".equalsIgnoreCase(str) || "call_unexpected_disconnected".equalsIgnoreCase(str) || "call_dial_out_error".equalsIgnoreCase(str) || "data_fake_connection".equalsIgnoreCase(str) || "data_fake_connection_time".equalsIgnoreCase(str) || "nw_out_of_service".equalsIgnoreCase(str) || "event_modem_crash".equalsIgnoreCase(str) || "event_registered_volteFailed".equalsIgnoreCase(str) || "event_lte_fiveg_signal_loss".equalsIgnoreCase(str) || "Signal".equalsIgnoreCase(str) || "FiveGSa".equalsIgnoreCase(str) || "FiveGNsa".equalsIgnoreCase(str) || "Rat".equalsIgnoreCase(str) || "event_nw_out_of_service_duration".equalsIgnoreCase(str) || "event_nw_search_duration".equalsIgnoreCase(str) || "event_sim_abnormal".equalsIgnoreCase(str) || "event_send_sms_result".equalsIgnoreCase(str) || "event_consecutive_data_call_error".equalsIgnoreCase(str) || "LteNas".equalsIgnoreCase(str) || "NrNas".equalsIgnoreCase(str);
    }

    private static void reportXmsEvent(String str, Map map) {
        if ("data_setup_call_error".equalsIgnoreCase(str) && "NA".equals((String) map.get("ErrorCause"))) {
            com.android.phone.statistics.utils.Utils.logD(LOG_TAG, "ignore success event");
            return;
        }
        if ("event_send_sms_result".equalsIgnoreCase(str)) {
            String str2 = (String) map.get("isSentSmsSuccess");
            if (!"0".equalsIgnoreCase(str2)) {
                com.android.phone.statistics.utils.Utils.logD(LOG_TAG, "ignore sms success event");
                return;
            }
        }
        map.put("DeviceName", Build.DEVICE);
        HashMap map2 = new HashMap(map);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("MqsEvent", str);
            jSONObject.put("Time", String.valueOf(System.currentTimeMillis()));
            for (String str3 : map2.keySet()) {
                jSONObject.put(str3, map2.get(str3));
            }
            MQSEventManagerDelegate.getInstance().reportXmsEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, jSONObject.toString());
            com.android.phone.statistics.utils.Utils.logD(LOG_TAG, "reportXmsEvent done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
