package com.android.phone.settings.search.provider;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Binder;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.VirtualSim;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.TelephonyCapabilities;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.settings.CallFeaturesSetting;
import com.android.phone.settings.search.utils.SearchContract;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiuiJpOperatorCustimizedUtils;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.android.phone.utils.MiuiNewCallUtils;
import com.android.phone.utils.Utils;
import com.android.phone.utils.VoiceReportUtils;
import com.android.services.telephony.SimpleFeatures;
import com.android.services.telephony.ims.ImsAdapterBase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.FeatureConfiguration;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;
import miui.yellowpage.YellowPageUtils;

/* JADX INFO: loaded from: classes.dex */
public class PhoneStatusSearchProvider extends ContentProvider {
    private static final String ANTI_SPAM_CALL_SETTINGS_ACTION = "com.miui.antispam.action.ANTISPAM_CALL_SETTINGS";
    private static final String ANTI_SPAM_CALL_SETTINGS_ACTIVITY = "com.miui.antispam.ui.activity.AntiSpamCallSettingsNewActivity";
    private static final int[] BIG_SIM_SLOT_ICON = {R.drawable.ic_sim_1_big, R.drawable.ic_sim_2_big};
    private static final String MI_MOBILE_URL = "com.miui.virtualsim://main?url=virtualsim://eventWebView?activityUrl=https://partner.10046.mi.com/miMobileOrder/card_sell?channel=b20210406";
    private static final String PACKAGES_NAME = "com.android.phone";
    private static final String TAG = "PhoneStatusSearchProvider";
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.search.provider.PhoneStatusSearchProvider.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Intent intent2 = new Intent("miui.intent.action.SEPARATE_APP_SEARCH_RESULT_UPDATE");
            intent2.setClassName("com.android.settings", "com.android.settings.search.provider.UpdateReceiver");
            Log.d(PhoneStatusSearchProvider.TAG, "action: " + action);
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                PhoneStatusSearchProvider.this.getContext().sendBroadcast(intent2);
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                PhoneStatusSearchProvider.this.getContext().sendBroadcast(intent2);
                return;
            }
            if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                PhoneStatusSearchProvider.this.getContext().sendBroadcast(intent2);
            } else if ("android.provider.Telephony.SECRET_CODE".equals(action)) {
                PhoneStatusSearchProvider.this.getContext().sendBroadcast(intent2);
            } else {
                Log.d(PhoneStatusSearchProvider.TAG, "unknown action");
            }
        }
    };

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    /* JADX WARN: Code duplicated, block: B:176:0x0f36  */
    public List prepareData() throws Throwable {
        RawData rawData;
        boolean z;
        RawData rawData2;
        int i;
        RawData rawData3;
        SubscriptionInfo subscriptionInfo;
        RawData rawData4;
        String str;
        ArrayList arrayList = new ArrayList();
        Resources resources = getContext().getResources();
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(TelephonyManager.class);
        boolean zIsDataCapable = telephonyManager.isDataCapable();
        boolean zIsVoiceCapable = telephonyManager.isVoiceCapable();
        Log.d(TAG, "prepareData packages=" + getContext().getPackageName());
        boolean z2 = Settings.System.getInt(getContext().getContentResolver(), "airplane_mode_on", -1) == 1;
        boolean z3 = miui.telephony.TelephonyManager.getDefault().getIccCardCount() > 0 && (!z2 || (MiuiImsPhoneUtils.isWifiCallingEnabled() && !MiuiPhoneUtils.disableSSOverWfcEnableAirplane()));
        if (!zIsVoiceCapable || (miui.telephony.TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(getContext()))) {
            rawData = null;
        } else {
            if (!SimpleFeatures.isHidePhoneAccountSettings(getContext())) {
                RawData rawData5 = new RawData(resources.getString(R.string.phone_account_settings), "", "com.android.phone", "com.android.phone.settings.PhoneAccountSettingsActivity");
                rawData5.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.phone_account_settings));
                arrayList.add(rawData5);
            }
            if (MiuiSettings.Telephony.getRecordScenario(getContext().getContentResolver()) == 1) {
                if (MiuiSettings.Telephony.isAutoRecordEnabled(getContext().getContentResolver())) {
                    RawData rawData6 = new RawData(resources.getString(R.string.call_record_auto_unknown_number), "", "com.android.phone", "com.android.phone.settings.CallRecordSetting");
                    rawData6.setPath(resources.getString(R.string.path_call_record_setting));
                    arrayList.add(rawData6);
                }
                if (YellowPageUtils.isYellowPageAvailable(getContext())) {
                    RawData rawData7 = new RawData(resources.getString(R.string.auto_record_yellowpage), "", "com.android.phone", "com.android.phone.settings.CallRecordSetting");
                    rawData7.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.auto_record_yellowpage));
                    arrayList.add(rawData7);
                }
                RawData rawData8 = new RawData(resources.getString(R.string.call_record_auto_whitelist), "", "com.android.phone", "com.android.phone.settings.CallRecordSetting");
                rawData8.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_record_auto_whitelist));
                arrayList.add(rawData8);
                arrayList.add(rawData8);
            }
            if (TelephonyCapabilities.supportsCallRecording(getContext())) {
                RawData rawData9 = new RawData(resources.getString(R.string.call_recording_setting), "", "com.android.phone", "com.android.phone.settings.CallRecordSetting");
                rawData9.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_recording_setting));
                arrayList.add(rawData9);
                RawData rawData10 = new RawData(resources.getString(R.string.call_recording_notification_toggle_title), "", "com.android.phone", "com.android.phone.settings.CallRecordSetting");
                rawData10.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_recording_setting) + "/" + resources.getString(R.string.call_recording_notification_toggle_title));
                arrayList.add(rawData10);
                RawData rawData11 = new RawData(resources.getString(R.string.call_record_auto_title), "", "com.android.phone", "com.android.phone.settings.CallRecordSetting");
                rawData11.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_recording_setting) + "/" + resources.getString(R.string.call_record_auto_title));
                arrayList.add(rawData11);
            }
            if (YellowPageUtils.isYellowPageAvailable(getContext()) && new Intent("miui.intent.action.TURN_ON_SMART_ANTISPAM").setPackage("com.miui.yellowpage").resolveActivity(getContext().getPackageManager()) != null) {
                RawData rawData12 = new RawData(resources.getString(R.string.cloud_antispam_title), "", "com.miui.yellowpage", "com.miui.yellowpage.activity.SettingActivity");
                rawData12.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.cloud_antispam_title));
                arrayList.add(rawData12);
            }
            RawData rawData13 = new RawData(resources.getString(R.string.turnover_mute_title), "", "com.android.phone", "com.android.phone.settings.AnswerStateSetting");
            rawData13.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.answer_state_setting_title) + "/" + resources.getString(R.string.turnover_mute_title));
            arrayList.add(rawData13);
            if (MiuiPhoneUtils.isSensorProximitySupported()) {
                RawData rawData14 = new RawData(resources.getString(R.string.enable_proximity_title), "", "com.android.phone", "com.android.phone.settings.AnswerStateSetting");
                rawData14.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.answer_state_setting_title) + "/" + resources.getString(R.string.enable_proximity_title));
                arrayList.add(rawData14);
            }
            RawData rawData15 = new RawData(resources.getString(R.string.enable_antispam_strange_title), "", "com.android.phone", "com.android.phone.settings.AnswerStateSetting");
            rawData15.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.answer_state_setting_title) + "/" + resources.getString(R.string.enable_antispam_strange_title));
            arrayList.add(rawData15);
            boolean zIsAntiSpamNewVersion = CallFeaturesSetting.isAntiSpamNewVersion(getContext());
            String string = resources.getString(zIsAntiSpamNewVersion ? R.string.call_antispam_setting : R.string.antispam_setting);
            String str2 = zIsAntiSpamNewVersion ? ANTI_SPAM_CALL_SETTINGS_ACTION : "";
            if (zIsAntiSpamNewVersion) {
                str = ANTI_SPAM_CALL_SETTINGS_ACTIVITY;
            } else {
                str = "com.miui.antispam.ui.activity.AntiSpamNewSettingsActivity";
            }
            RawData rawData16 = new RawData(string, str2, "com.miui.securitycenter", str);
            rawData16.setPath(resources.getString(R.string.telephony_setting) + "/" + string);
            arrayList.add(rawData16);
            RawData rawData17 = new RawData(resources.getString(R.string.missed_call_notify_times_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
            rawData17.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.missed_call_notify_times_title));
            arrayList.add(rawData17);
            RawData rawData18 = new RawData(resources.getString(R.string.preference_telocation_title), "", "com.android.phone", "com.android.phone.settings.TelocationSetting");
            rawData18.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.preference_telocation_title));
            arrayList.add(rawData18);
            RawData rawData19 = new RawData(resources.getString(R.string.telocation_enable_title), "", "com.android.phone", "com.android.phone.settings.TelocationSetting");
            rawData19.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.preference_telocation_title) + "/" + resources.getString(R.string.telocation_enable_title));
            arrayList.add(rawData19);
            RawData rawData20 = new RawData(resources.getString(R.string.telocation_contacts_countrycode), "", "com.android.phone", "com.android.phone.settings.TelocationSetting");
            rawData20.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.preference_telocation_title) + "/" + resources.getString(R.string.telocation_contacts_countrycode));
            arrayList.add(rawData20);
            RawData rawData21 = new RawData(resources.getString(R.string.telocation_auto_country_code), "", "com.android.phone", "com.android.phone.settings.TelocationSetting");
            rawData21.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.preference_telocation_title) + "/" + resources.getString(R.string.telocation_auto_country_code));
            arrayList.add(rawData21);
            RawData rawData22 = new RawData(resources.getString(R.string.preference_dial_pad_touch_tone_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
            rawData22.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.preference_dial_pad_touch_tone_title));
            arrayList.add(rawData22);
            RawData rawData23 = new RawData(resources.getString(R.string.t9_indexing_method_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
            rawData23.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.t9_indexing_method_title));
            arrayList.add(rawData23);
            RawData rawData24 = new RawData(resources.getString(R.string.quick_dial_setting_title), "", "com.android.contacts", "com.android.contacts.quickdial.QuickDialSettingActivity");
            rawData24.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.quick_dial_setting_title));
            arrayList.add(rawData24);
            rawData = new RawData(resources.getString(R.string.auto_retry_mode_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
            rawData.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.auto_retry_mode_title));
            arrayList.add(rawData);
        }
        if (zIsVoiceCapable) {
            if (z3) {
                RawData rawData25 = new RawData(resources.getString(R.string.call_forwarding_settings), "", "com.android.phone", "com.android.phone.settings.CallForwardActivity");
                rawData25.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_forwarding_settings));
                arrayList.add(rawData25);
                if (MiuiJpOperatorCustimizedUtils.isCustForJpKd()) {
                    arrayList.add(new RawData(resources.getString(R.string.voicemail_callforward_settings), "", "com.android.phone", ".settings.MiuiVoiceMailCallForwardActivity"));
                }
                RawData rawData26 = new RawData(resources.getString(R.string.call_waiting_title), "", "com.android.phone", "com.android.phone.settings.CallWaitingActivity");
                rawData26.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_waiting_title));
                arrayList.add(rawData26);
            }
            RawData rawData27 = new RawData(resources.getString(R.string.answer_state_setting_title), "", "com.android.phone", "com.android.phone.settings.AnswerStateSetting");
            rawData27.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.answer_state_setting_title));
            arrayList.add(rawData27);
            RawData rawData28 = new RawData(resources.getString(R.string.handon_ringer_title), "", "com.android.phone", "com.android.phone.settings.AnswerStateSetting");
            rawData28.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.answer_state_setting_title) + "/" + resources.getString(R.string.handon_ringer_title));
            arrayList.add(rawData28);
            RawData rawData29 = new RawData(resources.getString(R.string.crescendo_ringer_title), "", "com.android.phone", "com.android.phone.settings.AnswerStateSetting");
            rawData29.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.answer_state_setting_title) + "/" + resources.getString(R.string.crescendo_ringer_title));
            arrayList.add(rawData29);
            if (Utils.isSupportFlashlight(getContext())) {
                RawData rawData30 = new RawData(resources.getString(R.string.flash_when_ring_title), "", "com.android.phone", "com.android.phone.settings.AnswerStateSetting");
                rawData30.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.answer_state_setting_title) + "/" + resources.getString(R.string.flash_when_ring_title));
                arrayList.add(rawData30);
            }
            boolean z4 = Build.IS_INTERNATIONAL_BUILD;
            if (!z4 && !ModemProvider.IS_SPRD) {
                if (Utils.isAiAssistAnswerSupported(getContext())) {
                    RawData rawData31 = new RawData(resources.getString(R.string.ai_call_setting_title), "", "com.xiaomi.phone", "com.xiaomi.phone.settings.AiCallSetting");
                    rawData31.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.ai_call_setting_title));
                    arrayList.add(rawData31);
                } else if (Utils.supportXiaoAi(getContext()) && Utils.isAiAvailable(getContext())) {
                    RawData rawData32 = new RawData(resources.getString(R.string.xiaoai_assistant_setting_title), "", "com.xiaomi.aiasst.service", "com.xiaomi.aiasst.service.aicall.settings.main.CallLogAndSettingsActivity");
                    rawData32.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.xiaoai_assistant_setting_title));
                    arrayList.add(rawData32);
                } else if (Utils.isAiAvailable(getContext())) {
                    RawData rawData33 = new RawData(resources.getString(R.string.ai_assistant_setting_title), "", "com.xiaomi.aiasst.service", "com.xiaomi.aiasst.service.aicall.settings.main.CallLogAndSettingsActivity");
                    rawData33.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.ai_assistant_setting_title));
                    arrayList.add(rawData33);
                }
            }
            RawData rawData34 = new RawData(resources.getString(R.string.auto_answer_pref_title), "", "com.android.phone", "com.android.phone.settings.AutoAnswerSetting");
            rawData34.setPath(resources.getString(R.string.path_auto_answer_pref_title));
            arrayList.add(rawData34);
            if (PhoneHelperStub.getInstance().isAutoRecordPhoneCall()) {
                RawData rawData35 = new RawData(resources.getString(R.string.auto_answer_pref_title), "", "com.android.phone", "com.android.phone.settings.AutoAnswerSetting");
                rawData35.setPath(resources.getString(R.string.path_auto_answer_pref_title) + "/" + resources.getString(R.string.auto_answer_delay_title));
                arrayList.add(rawData35);
            }
            if (Utils.isSupportSensorAnswer(getContext())) {
                RawData rawData36 = new RawData(resources.getString(R.string.sensor_answer_title), "", "com.android.phone", "com.android.phone.settings.CallFeaturesSetting");
                rawData36.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.sensor_answer_title));
                arrayList.add(rawData36);
            }
            if (VoiceReportUtils.isSupportVoiceReportV2(getContext())) {
                RawData rawData37 = new RawData(resources.getString(R.string.phone_report_title), "", "com.android.phone", "com.android.phone.settings.CallFeaturesSetting");
                rawData37.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.phone_report_title));
                arrayList.add(rawData37);
            }
            RawData rawData38 = new RawData(resources.getString(R.string.call_advanced_setting_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
            rawData38.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title));
            arrayList.add(rawData38);
            if (!z4) {
                RawData rawData39 = new RawData(resources.getString(R.string.autoip), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
                rawData39.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.autoip));
                arrayList.add(rawData39);
            }
            if (MiuiPhoneUtils.isVibratorSupported()) {
                RawData rawData40 = new RawData(resources.getString(R.string.connect_disconnect_vibrate_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
                rawData40.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.connect_disconnect_vibrate_title));
                arrayList.add(rawData40);
            }
            RawData rawData41 = new RawData(resources.getString(R.string.call_waiting_tone_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
            rawData41.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.call_waiting_tone_title));
            arrayList.add(rawData41);
            RawData rawData42 = new RawData(resources.getString(R.string.reject_via_sms), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
            rawData42.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.reject_via_sms));
            arrayList.add(rawData42);
            if (MiuiPhoneUtils.supportXiaomiRtt(getContext())) {
                rawData42 = new RawData(resources.getString(R.string.rtt_settings_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
                rawData42.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.rtt_settings_title));
                arrayList.add(rawData42);
            }
            if (MiuiPhoneUtils.getCdmaPhone() != null) {
                arrayList.add(new RawData(resources.getString(R.string.cdma_display_precise_call_state_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting"));
                rawData42 = new RawData(resources.getString(R.string.voice_privacy), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
                arrayList.add(rawData42);
            }
            if (MiuiPhoneUtils.shouldShowCallBarring() && z3) {
                rawData42 = new RawData(resources.getString(R.string.lable_call_barring), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
                rawData42.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.lable_call_barring));
                arrayList.add(rawData42);
            }
            if ((!hasAnyCdmaPhone() || miui.telephony.TelephonyManager.getDefault().getIccCardCount() > 1) && !miui.telephony.TelephonyManager.isCustForMxTelcel() && z3) {
                rawData42 = new RawData(resources.getString(R.string.caller_id_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
                rawData42.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.caller_id_title));
                arrayList.add(rawData42);
            }
            if (!MiuiPhoneUtils.isRemoveFdn()) {
                rawData42 = new RawData(resources.getString(R.string.fdn), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
                rawData42.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.fdn));
                arrayList.add(rawData42);
            }
            if (!SimpleFeatures.isHideVoicemailSettings(getContext())) {
                rawData42 = new RawData(resources.getString(R.string.voicemail_settings), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
                rawData42.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.voicemail_settings));
                arrayList.add(rawData42);
            }
            rawData = rawData42;
            if (getContext().getResources().getBoolean(R.bool.dtmf_type_enabled) && !miui.telephony.TelephonyManager.isCustForMxTelcel()) {
                rawData = new RawData(resources.getString(R.string.dtmf_tones_title), "", "com.android.phone", "com.android.phone.settings.CallAdvancedSetting");
                rawData.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_advanced_setting_title) + "/" + resources.getString(R.string.dtmf_tones_title));
                arrayList.add(rawData);
            }
        }
        if (MiuiPhoneUtils.isDeviceSupportFold()) {
            RawData rawData43 = new RawData(resources.getString(R.string.call_fold_setting_title), "", "com.android.phone", "com.android.phone.settings.CallFoldSetting");
            rawData43.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_fold_setting_title));
            arrayList.add(rawData43);
            RawData rawData44 = new RawData(resources.getString(R.string.fold_answer_pref_title), "", "com.android.phone", "com.android.phone.settings.CallFoldSetting");
            rawData44.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_fold_setting_title) + "/" + resources.getString(R.string.fold_answer_pref_title));
            arrayList.add(rawData44);
            RawData rawData45 = new RawData(resources.getString(R.string.unfold_speaker_pref_title), "", "com.android.phone", "com.android.phone.settings.CallFoldSetting");
            rawData45.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_fold_setting_title) + "/" + resources.getString(R.string.unfold_speaker_pref_title));
            arrayList.add(rawData45);
            if (!MiuiPhoneUtils.isNotSupportUnfoldCloseCameraFeature()) {
                RawData rawData46 = new RawData(resources.getString(R.string.unfold_close_camera_pref_title), "", "com.android.phone", "com.android.phone.settings.CallFoldSetting");
                rawData46.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_fold_setting_title) + "/" + resources.getString(R.string.unfold_close_camera_pref_title));
                arrayList.add(rawData46);
            }
            rawData = new RawData(resources.getString(R.string.unfold_show_bublle_pref_title), "", "com.android.phone", "com.android.phone.settings.CallFoldSetting");
            rawData.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.call_fold_setting_title) + "/" + resources.getString(R.string.unfold_show_bublle_pref_title));
            arrayList.add(rawData);
        }
        if (MiuiPhoneUtils.isDeviceSupportFlip()) {
            RawData rawData47 = new RawData(resources.getString(R.string.filp_auto_answer_call_title), "", "com.android.phone", "com.android.phone.settings.CallFeaturesSetting");
            rawData47.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.filp_auto_answer_call_title));
            arrayList.add(rawData47);
            rawData = new RawData(resources.getString(R.string.filp_auto_hangup_call_title), "", "com.android.phone", "com.android.phone.settings.CallFeaturesSetting");
            rawData.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.filp_auto_hangup_call_title));
            arrayList.add(rawData);
        }
        if (!Build.IS_INTERNATIONAL_BUILD && MiuiNewCallUtils.isNewCallFeatureEnabled() && MiuiNewCallUtils.isNewCallSdkInstalled()) {
            rawData = new RawData(resources.getString(R.string.newcall_settings_title), "", "com.newcall", "com.newcall.setting.SettingsActivity");
            rawData.setPath(resources.getString(R.string.telephony_setting) + "/" + resources.getString(R.string.newcall_settings_title));
            arrayList.add(rawData);
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        List availableSubscriptionInfoList = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        int virtualSimSlot = MiuiPhoneUtils.getVirtualSimSlot();
        Class<ITelephonyModule> cls = ITelephonyModule.class;
        if (!z2 && availableSubscriptionInfoList != null && availableSubscriptionInfoList.size() > 0) {
            for (Iterator it = availableSubscriptionInfoList.iterator(); it.hasNext(); it = it) {
                SubscriptionInfo subscriptionInfo2 = (SubscriptionInfo) it.next();
                boolean z5 = virtualSimSlot == subscriptionInfo2.getSlotId();
                List list = availableSubscriptionInfoList;
                boolean z6 = zIsDataCapable;
                String simDisplayName = getSimDisplayName(subscriptionInfo2, z5);
                if (z5) {
                    i = R.drawable.mi_big_sim_icon;
                } else {
                    i = BIG_SIM_SLOT_ICON[subscriptionInfo2.getSlotId()];
                }
                int i2 = i;
                boolean z7 = z2;
                RawData rawData48 = rawData;
                if (MiuiPhoneUtils.isMultiSimEnabled()) {
                    RawData rawData49 = new RawData(getContext().getString(R.string.msim_info_editor_name), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    rawData49.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.msim_info_editor_name));
                    rawData49.setIconResId(i2);
                    StringBuilder sb = new StringBuilder();
                    sb.append(SubscriptionManager.SLOT_KEY);
                    sb.append(":");
                    sb.append(subscriptionInfo2.getSlotId());
                    rawData49.setExtras(sb.toString());
                    arrayList.add(rawData49);
                    rawData3 = new RawData(getContext().getString(R.string.msim_info_editor_number), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.msim_info_editor_number));
                    rawData3.setIconResId(i2);
                } else {
                    rawData3 = rawData48;
                }
                if (MiuiPhoneUtils.isEsimCapable() && ((ITelephonyModule) ModuleDelegate.get(cls)).isPsim2EsimAvailable(subscriptionInfo2.getSubscriptionId())) {
                    rawData3 = new RawData(getContext().getString(R.string.convert_to_esim_title), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.convert_to_esim_title));
                    rawData3.setIconResId(i2);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(SubscriptionManager.SLOT_KEY);
                    sb2.append(":");
                    sb2.append(subscriptionInfo2.getSlotId());
                    rawData3.setExtras(sb2.toString());
                    arrayList.add(rawData3);
                }
                if (z6) {
                    String operatorNumeric = MiuiPhoneUtils.getOperatorNumeric(MiuiPhoneUtils.getPhone(subscriptionInfo2.getSlotId()));
                    if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && ServiceProviderUtils.isChinaTelecom(operatorNumeric)) {
                        subscriptionInfo = subscriptionInfo2;
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        String str3 = SubscriptionManager.SLOT_KEY;
                        sb3.append(str3);
                        sb3.append(":");
                        subscriptionInfo = subscriptionInfo2;
                        sb3.append(subscriptionInfo.getSlotId());
                        rawData3.setExtras(sb3.toString());
                        rawData3 = new RawData(getContext().getString(R.string.networks_title), "", "com.android.phone", "com.android.phone.settings.NetworkSetting");
                        rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.networks_title));
                        rawData3.setIconResId(i2);
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str3);
                        sb4.append(":");
                        sb4.append(subscriptionInfo.getSlotId());
                        rawData3.setExtras(sb4.toString());
                        arrayList.add(rawData3);
                    }
                } else {
                    subscriptionInfo = subscriptionInfo2;
                }
                if (Build.IS_INTERNATIONAL_BUILD && !((ITelephonyModule) ModuleDelegate.get(cls)).isRemoveNetworkModeSettings(PhoneGlobals.getPhone(subscriptionInfo.getSubscriptionId())) && z6) {
                    rawData3 = new RawData(getContext().getString(R.string.preferred_network_type_title), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.preferred_network_type_title));
                    rawData3.setIconResId(i2);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(SubscriptionManager.SLOT_KEY);
                    sb5.append(":");
                    sb5.append(subscriptionInfo.getSlotId());
                    rawData3.setExtras(sb5.toString());
                    arrayList.add(rawData3);
                }
                if (!z5 && (MiuiPhoneUtils.getIccCardCountExcludeSlot(subscriptionInfo.getSlotId()) >= 1 || !subscriptionInfo.isActivated())) {
                    rawData3 = new RawData(getContext().getString(R.string.msim_enable_sim), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.msim_enable_sim));
                    rawData3.setIconResId(i2);
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(SubscriptionManager.SLOT_KEY);
                    sb6.append(":");
                    sb6.append(subscriptionInfo.getSlotId());
                    rawData3.setExtras(sb6.toString());
                    arrayList.add(rawData3);
                }
                if (z5) {
                    String virtualSimCarrierName = MiuiPhoneUtils.getVirtualSimCarrierName();
                    RawData rawData50 = new RawData(virtualSimCarrierName, "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    rawData50.setPath(resources.getString(R.string.network_settings_title) + "/" + virtualSimCarrierName);
                    rawData50.setIconResId(i2);
                    rawData50.setExtras(SubscriptionManager.SLOT_KEY + ":" + subscriptionInfo.getSlotId());
                    arrayList.add(rawData50);
                    rawData3 = rawData50;
                }
                if (TelephonyCapabilities.supportShowPLMNPreference()) {
                    rawData3 = new RawData(getContext().getString(R.string.uplmn_list_setting_title), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.uplmn_list_setting_title));
                    rawData3.setIconResId(i2);
                    arrayList.add(rawData3);
                }
                if (FiveGManagerBase.isFiveGCapable() && FiveGUtils.shouldShowViceSaSwitch(subscriptionInfo.getSlotId())) {
                    rawData3 = new RawData(getContext().getString(R.string.fiveg_sa_vice_switch_title), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.fiveg_sa_vice_switch_title));
                    rawData3.setIconResId(i2);
                    arrayList.add(rawData3);
                }
                if (subscriptionInfo.getSlotId() == DefaultSimManager.getDefaultDataSlotId() && z6) {
                    rawData3 = new RawData(getContext().getString(R.string.apn_settings), "", "com.android.settings", "com.android.settings.Settings$ApnSettingsActivity");
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.apn_settings));
                    rawData3.setIconResId(i2);
                    arrayList.add(rawData3);
                }
                if (MiuiPhoneUtils.isHideStkIcon() && !MiuiPhoneUtils.isRemoveStkSettings(subscriptionInfo.getSlotId())) {
                    rawData3 = new RawData(getContext().getString(R.string.hide_stk_name), "", "com.android.stk", "com.android.stk.StkMainHide");
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.hide_stk_name));
                    rawData3.setIconResId(i2);
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(SubscriptionManager.SLOT_KEY);
                    sb7.append(":");
                    sb7.append(subscriptionInfo.getSlotId());
                    rawData3.setExtras(sb7.toString());
                    arrayList.add(rawData3);
                }
                if (MiuiPhoneUtils.isSupportVoiceLinkFeature() && !TelephonyCloudController.CLOUD_VOICE_LINK_FEATURE_DISABLED) {
                    rawData3 = new RawData(getContext().getString(R.string.voice_link_switch_title), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.voice_link_switch_title));
                    rawData3.setIconResId(i2);
                    arrayList.add(rawData3);
                }
                if (ImsAdapterBase.getInstance().isVolteSupportedByDevice(getContext()) && !MiuiPhoneUtils.shouldHideEnhanced4gLteSettings(getContext(), MiuiPhoneUtils.getPhone(subscriptionInfo.getSlotId())) && MiuiPhoneUtils.isIccCardActivated(subscriptionInfo.getSlotId())) {
                    if (MiuiPhoneUtils.isMultiSimEnabled() && !miui.telephony.TelephonyManager.isCustSingleSimDevice()) {
                        rawData3 = new RawData(getContext().getString(R.string.volte_switch_title), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    } else {
                        rawData3 = new RawData(getContext().getString(R.string.volte_switch_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                    }
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.volte_switch_title));
                    rawData3.setIconResId(i2);
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(SubscriptionManager.SLOT_KEY);
                    sb8.append(":");
                    sb8.append(subscriptionInfo.getSlotId());
                    rawData3.setExtras(sb8.toString());
                    arrayList.add(rawData3);
                }
                if (MiuiImsPhoneUtils.showWifiCallSwitchView(MiuiPhoneUtils.getPhone(subscriptionInfo.getSlotId())) && MiuiPhoneUtils.isIccCardActivated(subscriptionInfo.getSlotId())) {
                    if (MiuiPhoneUtils.isMultiSimEnabled() && !miui.telephony.TelephonyManager.isCustSingleSimDevice()) {
                        rawData3 = new RawData(getContext().getString(R.string.wificall_switch_title), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    } else {
                        rawData3 = new RawData(getContext().getString(R.string.wificall_switch_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                    }
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.wificall_switch_title));
                    rawData3.setIconResId(i2);
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(SubscriptionManager.SLOT_KEY);
                    sb9.append(":");
                    sb9.append(subscriptionInfo.getSlotId());
                    rawData3.setExtras(sb9.toString());
                    arrayList.add(rawData3);
                }
                if (MiuiPhoneUtils.isSupportEnable2GSwitch(MiuiPhoneUtils.getPhone(subscriptionInfo.getSlotId())) && MiuiPhoneUtils.isIccCardActivated(subscriptionInfo.getSlotId())) {
                    if (MiuiPhoneUtils.isMultiSimEnabled() && !miui.telephony.TelephonyManager.isCustSingleSimDevice()) {
                        rawData3 = new RawData(getContext().getString(R.string.enable_2g_title_eu), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    } else {
                        rawData3 = new RawData(getContext().getString(R.string.enable_2g_title_eu), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                    }
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.enable_2g_title_eu));
                    rawData3.setIconResId(i2);
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(SubscriptionManager.SLOT_KEY);
                    sb10.append(":");
                    sb10.append(subscriptionInfo.getSlotId());
                    rawData3.setExtras(sb10.toString());
                    arrayList.add(rawData3);
                }
                if (FiveGUtils.isVonrSwitchVisiable(subscriptionInfo.getSlotId()) && MiuiPhoneUtils.isIccCardActivated(subscriptionInfo.getSlotId())) {
                    rawData3 = new RawData(getContext().getString(R.string.vonr_switch_title), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                    rawData3.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.vonr_switch_title));
                    rawData3.setIconResId(i2);
                    arrayList.add(rawData3);
                }
                if (MiuiPhoneUtils.isFiveGCapable() && FiveGManagerBase.getInstance().isUserFiveGEnabled() && FiveGUtils.shouldShow5GSaSwitch() && DefaultSimManager.getDefaultDataSlotId() == subscriptionInfo.getSlotId()) {
                    if (miui.telephony.TelephonyManager.isCustSingleSimDevice()) {
                        rawData4 = new RawData(getContext().getString(R.string.fiveg_sa_switch_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                        rawData4.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.fiveg_sa_switch_title));
                    } else {
                        RawData rawData51 = new RawData(getContext().getString(R.string.fiveg_sa_switch_title), "", "com.android.phone", "com.android.phone.settings.MultiSimInfoEditorActivity");
                        rawData51.setPath(resources.getString(R.string.network_settings_title) + "/" + simDisplayName + "/" + resources.getString(R.string.fiveg_sa_switch_title));
                        rawData4 = rawData51;
                    }
                    rawData4.setIconResId(i2);
                    rawData4.setExtras(SubscriptionManager.SLOT_KEY + ":" + subscriptionInfo.getSlotId());
                    arrayList.add(rawData4);
                    rawData = rawData4;
                } else {
                    rawData = rawData3;
                }
                availableSubscriptionInfoList = list;
                zIsDataCapable = z6;
                virtualSimSlot = virtualSimSlot;
                z2 = z7;
                cls = cls;
            }
        }
        List list2 = availableSubscriptionInfoList;
        boolean z8 = zIsDataCapable;
        boolean z9 = z2;
        Class<ITelephonyModule> cls2 = cls;
        if (MiuiKoreaOperatorCustimizedUtils.isCustForKrOps()) {
            RawData rawData52 = new RawData(resources.getString(R.string.data_enable_always_warn), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData52.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.data_enable_always_warn));
            rawData52.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData52);
        }
        if (z8) {
            RawData rawData53 = new RawData(resources.getString(R.string.network_settings_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData53.setPath(resources.getString(R.string.network_settings_title));
            rawData53.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData53);
            RawData rawData54 = new RawData(resources.getString(R.string.mobile_settings_screen_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData54.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.mobile_settings_screen_title));
            rawData54.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData54);
        }
        if (MiuiPhoneUtils.isFiveGHostedModeSupported() && (!MiuiPhoneUtils.overMiuiFifteen() || MiuiPhoneUtils.isFivegHostedModeIndia())) {
            RawData rawData55 = new RawData(resources.getString(R.string.fiveg_hosted_mode_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData55.setPath(resources.getString(R.string.fiveg_hosted_mode_title));
            rawData55.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData55);
        }
        if (TelephonyManagerEx.getDefault().isSupportSatelliteByDevice()) {
            RawData rawData56 = new RawData(resources.getString(R.string.satellite_mobile_settings_title), "", "com.android.phone", "com.android.phone.settings.MiuiSatelliteSettings");
            rawData56.setPath(resources.getString(R.string.satellite_management_title) + "/" + resources.getString(R.string.satellite_mobile_settings_title));
            rawData56.setIconResId(R.drawable.ic_settings_satellite);
            arrayList.add(rawData56);
            if (TelephonyManagerEx.getDefault().isSupportSatelliteSignalBoost()) {
                RawData rawData57 = new RawData(resources.getString(R.string.satellite_signal_boost_setting_title), "", "com.android.phone", "com.android.phone.settings.MiuiSatelliteSettings");
                rawData57.setPath(resources.getString(R.string.satellite_management_title) + "/" + resources.getString(R.string.satellite_signal_boost_setting_title));
                rawData57.setIconResId(R.drawable.ic_settings_satellite);
                arrayList.add(rawData57);
            }
            RawData rawData58 = new RawData(resources.getString(R.string.satellite_findout_settings_title), "", "com.android.phone", "com.android.phone.settings.MiuiSatelliteSettings");
            rawData58.setPath(resources.getString(R.string.satellite_management_title) + "/" + resources.getString(R.string.satellite_signal_boost_setting_title));
            rawData58.setIconResId(R.drawable.ic_settings_satellite);
            arrayList.add(rawData58);
            if (MiuiPhoneUtils.shouldShowSatelliteBusinessEntrance()) {
                RawData rawData59 = new RawData(resources.getString(R.string.satellite_open_tiantong_business_title), "", "com.android.phone", "com.android.phone.settings.MiuiSatelliteSettings");
                rawData59.setPath(resources.getString(R.string.satellite_management_title) + "/" + resources.getString(R.string.satellite_open_tiantong_business_title));
                rawData59.setIconResId(R.drawable.ic_settings_satellite);
                arrayList.add(rawData59);
            }
            RawData rawData60 = new RawData(resources.getString(R.string.satellite_privacy_withdrawal_setting), "", "com.android.phone", "com.android.phone.settings.MiuiSatelliteSettings");
            rawData60.setPath(resources.getString(R.string.satellite_management_title) + "/" + resources.getString(R.string.satellite_privacy_withdrawal_setting));
            rawData60.setIconResId(R.drawable.ic_settings_satellite);
            arrayList.add(rawData60);
        }
        if (TelephonyManagerEx.getDefault().isSupportBeiDouByDevice() && MiuiPhoneUtils.shouldShowBeiDouButton(getContext())) {
            RawData rawData61 = new RawData(resources.getString(R.string.beidou_mobile_settings_title), "", "com.android.phone", "com.android.phone.settings.MiuiSatelliteSettings");
            rawData61.setPath(resources.getString(R.string.satellite_management_title) + "/" + resources.getString(R.string.beidou_mobile_settings_title));
            rawData61.setIconResId(R.drawable.ic_settings_satellite);
            arrayList.add(rawData61);
            RawData rawData62 = new RawData(resources.getString(R.string.beidou_findout_settings_title), "", "com.android.phone", "com.android.phone.settings.MiuiSatelliteSettings");
            rawData62.setPath(resources.getString(R.string.satellite_management_title) + "/" + resources.getString(R.string.beidou_findout_settings_title));
            rawData62.setIconResId(R.drawable.ic_settings_satellite);
            arrayList.add(rawData62);
        }
        if (MiuiPhoneUtils.isMultiSimEnabled() && !miui.telephony.TelephonyManager.isCustSingleSimDevice()) {
            if (Utils.IS_MIUI_LITE_DEVICE) {
                RawData rawData63 = new RawData(resources.getString(R.string.default_voice_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData63.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.default_voice_title));
                rawData63.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData63);
                RawData rawData64 = new RawData(resources.getString(R.string.default_data_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData64.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.default_data_title));
                rawData64.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData64);
                if (!MiuiPhoneUtils.hidePrefSMSSettings()) {
                    RawData rawData65 = new RawData(resources.getString(R.string.sms_messages_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                    rawData65.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.sms_messages_title));
                    rawData65.setIconResId(R.drawable.ic_settings_dualsim);
                    arrayList.add(rawData65);
                }
            } else if (list2.size() > 1) {
                RawData rawData66 = new RawData(resources.getString(R.string.msim_setting_default_voice), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData66.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.msim_setting_default_voice));
                rawData66.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData66);
                RawData rawData67 = new RawData(resources.getString(R.string.msim_setting_default_data), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData67.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.msim_setting_default_data));
                rawData67.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData67);
                if (!MiuiPhoneUtils.hidePrefSMSSettings() && !z9) {
                    RawData rawData68 = new RawData(resources.getString(R.string.msim_setting_default_sms), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                    rawData68.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.msim_setting_default_sms));
                    rawData68.setIconResId(R.drawable.ic_settings_dualsim);
                    arrayList.add(rawData68);
                }
            }
        }
        boolean z10 = false;
        for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
            z10 = z10 || MiuiPhoneUtils.showFiveGSwitch(i3);
        }
        if (z10 && !FiveGUtils.isFiveGNetWorkInVisible()) {
            if (list2 != null && list2.size() > 0) {
                rawData2 = new RawData(resources.getString(R.string.fiveg_network_title), "", "com.android.phone", "com.android.phone.settings.MiuiFiveGNetworkSetting");
            } else {
                rawData2 = new RawData(resources.getString(R.string.fiveg_network_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            }
            rawData2.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.fiveg_network_title));
            rawData2.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData2);
        }
        if (MiuiPhoneUtils.isEsimCapable() && ((ITelephonyModule) ModuleDelegate.get(cls2)).isSupportForRegion()) {
            if (!((ITelephonyModule) ModuleDelegate.get(cls2)).isRemoveEsimSwitch()) {
                RawData rawData69 = new RawData(resources.getString(R.string.enable_esim_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData69.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.enable_esim_title));
                rawData69.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData69);
            }
            if (((ITelephonyModule) ModuleDelegate.get(cls2)).getEsimSecureValue() == 1) {
                RawData rawData70 = new RawData(resources.getString(R.string.enter_esim_manager_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData70.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.enter_esim_manager_title));
                rawData70.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData70);
                if (MiuiPhoneUtils.showEsimSupportedCarriersSetting()) {
                    RawData rawData71 = new RawData(resources.getString(R.string.enter_esim_service_support_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                    rawData71.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.enter_esim_service_support_title));
                    rawData71.setIconResId(R.drawable.ic_settings_dualsim);
                    arrayList.add(rawData71);
                }
            }
        }
        if (!MiuiPhoneUtils.shouldHideIntelligentDualSimButton() && list2.size() > 1) {
            RawData rawData72 = new RawData(resources.getString(R.string.intelligent_dual_sim_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData72.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.intelligent_dual_sim_title));
            rawData72.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData72);
        } else if (MiuiPhoneUtils.getInsertedSimCount() > 1 && MiuiPhoneUtils.isSmartSwitchDualSimSupported()) {
            if (!MiuiPhoneUtils.shouldHideSmartDualSimButton() && !MiuiPhoneUtils.shouldHideViceSlotVolteDataButton()) {
                RawData rawData73 = new RawData(resources.getString(R.string.smart_switch_dual_sim_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData73.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.smart_switch_dual_sim_title));
                rawData73.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData73);
            } else if (!MiuiPhoneUtils.shouldHideViceSlotVolteDataButton()) {
                RawData rawData74 = new RawData(resources.getString(R.string.vice_slot_volte_data_switch_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData74.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.vice_slot_volte_data_switch_title));
                rawData74.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData74);
            } else if (!MiuiPhoneUtils.shouldHideSmartDualSimButton()) {
                RawData rawData75 = new RawData(resources.getString(R.string.smart_dual_sim_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData75.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.smart_dual_sim_title));
                rawData75.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData75);
            }
        } else if (MiuiPhoneUtils.getInsertedSimCount() > 1) {
            if (!MiuiPhoneUtils.shouldHideViceSlotVolteDataButton()) {
                RawData rawData76 = new RawData(resources.getString(R.string.vice_slot_volte_data_switch_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData76.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.vice_slot_volte_data_switch_title));
                rawData76.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData76);
            }
            if (!MiuiPhoneUtils.shouldHideSmartDualSimButton()) {
                RawData rawData77 = new RawData(resources.getString(R.string.smart_dual_sim_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData77.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.smart_dual_sim_title));
                rawData77.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData77);
            }
        }
        if (MiuiPhoneUtils.getInsertedSimCount() > 1 && MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED && !((ITelephonyModule) ModuleDelegate.get(cls2)).isDisableLte() && !MiuiPhoneUtils.isRemoveDual4GSwitch()) {
            RawData rawData78 = new RawData(resources.getString(R.string.dual_4g_switch_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData78.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.dual_4g_switch_title));
            rawData78.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData78);
        }
        if (!miui.telephony.TelephonyManager.isGoogleCsp() && !MiuiPhoneUtils.isHideAlwaysReceiveMms()) {
            RawData rawData79 = new RawData(resources.getString(R.string.always_enable_mms), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData79.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.always_enable_mms));
            rawData79.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData79);
        }
        if ("cn".equalsIgnoreCase(SystemProperties.get("ro.miui.build.region")) && Utils.IS_MIUI_LITE_DEVICE) {
            RawData rawData80 = new RawData(resources.getString(R.string.business_hall_settings_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData80.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.business_hall_settings_title));
            rawData80.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData80);
        }
        if (MiuiPhoneUtils.supportChinaMobilePalm() && MiuiPhoneUtils.hasCmccSim()) {
            RawData rawData81 = new RawData(resources.getString(R.string.china_mobile_palm_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData81.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.china_mobile_palm_title));
            rawData81.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData81);
        }
        if (MiuiPhoneUtils.hideSimCallBack(getContext())) {
            z = true;
        } else {
            z = true;
            if (list2.size() > 1) {
                RawData rawData82 = new RawData(resources.getString(R.string.last_sim_callback_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
                rawData82.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.last_sim_callback_title));
                rawData82.setIconResId(R.drawable.ic_settings_dualsim);
                arrayList.add(rawData82);
            }
        }
        if (((ITelephonyModule) ModuleDelegate.get(cls2)).isSmartCallForwardSupported()) {
            RawData rawData83 = new RawData(resources.getString(R.string.dual_sim_smart_call_forward_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData83.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.dual_sim_smart_call_forward_title));
            rawData83.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData83);
        }
        if (list2 != null && list2.size() > 0 && z8) {
            RawData rawData84 = new RawData(getContext().getString(R.string.international_roaming_setting), "", "com.miui.securitycenter", "com.miui.networkassistant.ui.activity.InternationalRoamingSettingActivity");
            rawData84.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.international_roaming_setting));
            rawData84.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData84);
        } else if (z8) {
            RawData rawData85 = new RawData(resources.getString(R.string.international_roaming_setting), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData85.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.international_roaming_setting));
            rawData85.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData85);
        }
        boolean z11 = Settings.Secure.getInt(getContext().getContentResolver(), "bluetooth_call_enable", 0) != 0 ? z : false;
        if (miui.telephony.TelephonyManager.getDefault().isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_OFFLINE_CALL) && z11) {
            RawData rawData86 = new RawData(resources.getString(R.string.offline_call), "", "com.xiaomi.bluetooth", "com.android.bluetooth.ble.app.call.HyperCallActivity");
            rawData86.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.offline_call));
            rawData86.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData86);
        }
        if (MiuiPhoneUtils.isGeoLocationSupported() && list2 != null && list2.size() > 0 && !z9 && !MiuiPhoneUtils.overMiuiFifteen()) {
            RawData rawData87 = new RawData(resources.getString(R.string.mobile_geo_location_config_title), "", "com.android.phone", "com.android.phone.settings.MobileNetworkSettings");
            rawData87.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.mobile_geo_location_config_title));
            rawData87.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData87);
        }
        if (Utils.isSupportMiMobile(getContext())) {
            RawData rawData88 = new RawData(getContext().getString(R.string.mi_mobile_settings_title), "", "com.miui.virtualsim", "com.miui.virtualsim.ui.MainActivity");
            rawData88.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.mi_mobile_settings_title));
            rawData88.setIconResId(R.drawable.ic_settings_dualsim);
            rawData88.setIntentUri(MI_MOBILE_URL);
            arrayList.add(rawData88);
        }
        if (VirtualSim.isSupportMiSim(getContext())) {
            RawData rawData89 = new RawData(getContext().getString(R.string.mi_sim_settings_title), "", "com.miui.virtualsim", "com.miui.mimobile.ui.MmRouterActivity");
            rawData89.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.mi_sim_settings_title));
            rawData89.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData89);
        }
        if (VirtualSim.isSupportVirtualSim(getContext())) {
            RawData rawData90 = new RawData(getContext().getString(R.string.sim_roaming_settings_title), "", "com.miui.virtualsim", "com.miui.virtualsim.ui.MainActivity");
            rawData90.setPath(resources.getString(R.string.network_settings_title) + "/" + resources.getString(R.string.sim_roaming_settings_title));
            rawData90.setIconResId(R.drawable.ic_settings_dualsim);
            arrayList.add(rawData90);
        }
        return arrayList;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        startListen();
        return false;
    }

    private void startListen() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        getContext().registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        IntentFilter intentFilter2 = new IntentFilter("android.provider.Telephony.SECRET_CODE");
        intentFilter2.addDataScheme("android_secret_code");
        getContext().registerReceiver(this.mBroadcastReceiver, intentFilter2, 2);
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (UserHandle.myUserId() != 0) {
            Log.d(TAG, "special mode, return!");
            return null;
        }
        MatrixCursor matrixCursor = new MatrixCursor(SearchContract.SEARCH_RESULT_COLUMNS);
        for (RawData rawData : prepareData()) {
            matrixCursor.newRow().add(SearchContract.SearchResultColumn.COLUMN_TITLE, rawData.title).add(SearchContract.SearchResultColumn.COLUMN_INTENT_ACTION, rawData.intentAction).add(SearchContract.SearchResultColumn.COLUMN_INTENT_TARGET_PACKAGE, rawData.intentTargetPackage).add(SearchContract.SearchResultColumn.COLUMN_SUMMARY_ON, rawData.summaryOn).add(SearchContract.SearchResultColumn.COLUMN_INTENT_TARGET_CLASS, rawData.intentTargetClass).add(SearchContract.SearchResultColumn.COLUMN_URI_STRING, rawData.intentUri).add(SearchContract.SearchResultColumn.COLUMN_ICON_RESID, Integer.valueOf(rawData.iconResId)).add(SearchContract.SearchResultColumn.COLUMN_EXTRAS, rawData.extras);
        }
        return matrixCursor;
    }

    private String getSimDisplayName(SubscriptionInfo subscriptionInfo, boolean z) {
        String string;
        if (z) {
            string = MiuiPhoneUtils.getVirtualSimCarrierName();
        } else {
            string = subscriptionInfo.getDisplayName().toString();
        }
        return TextUtils.isEmpty(string) ? getContext().getResources().getString(R.string.msim_setting_no_sim_number) : string;
    }

    private boolean hasAnyCdmaPhone() {
        miui.telephony.TelephonyManager telephonyManager = miui.telephony.TelephonyManager.getDefault();
        for (int i = 0; i < telephonyManager.getPhoneCount(); i++) {
            if (telephonyManager.getPhoneTypeForSlot(i) == 2 && telephonyManager.hasIccCard(i)) {
                return true;
            }
        }
        return false;
    }
}
