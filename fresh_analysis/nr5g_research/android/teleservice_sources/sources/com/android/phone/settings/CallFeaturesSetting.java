package com.android.phone.settings;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.Phone;
import com.android.phone.MiPhoneAppStub;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.R;
import com.android.phone.TelephonyCapabilities;
import com.android.phone.custompreferences.MiuixTextPreference;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiJpOperatorCustimizedUtils;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.android.phone.utils.MiuiNewCallUtils;
import com.android.phone.utils.OneTrackImpl;
import com.android.phone.utils.ScreenDisplayUtils;
import com.android.phone.utils.Utils;
import com.android.phone.utils.VoiceReportUtils;
import com.android.services.telephony.SimpleFeatures;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.settingsdk.backup.SettingsBackupConsts;
import java.util.Iterator;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miui.yellowpage.YellowPageUtils;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;
import miuix.preference.DropDownPreference;

/* JADX INFO: loaded from: classes.dex */
public class CallFeaturesSetting extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        MultiAppFloatingActivitySwitcher.configureFloatingService(getIntent(), getPackageName());
        super.onCreate(bundle);
        if (UserHandle.myUserId() != 0) {
            Toast.makeText(this, R.string.call_settings_primary_user_only, 0).show();
            finish();
        } else if (bundle == null) {
            loadFragment(new CallFeaturesSettingFragment());
        }
    }

    public static boolean removePreference(PreferenceGroup preferenceGroup, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int preferenceCount = preferenceGroup.getPreferenceCount();
        boolean zRemovePreference = false;
        for (int i = 0; i < preferenceCount && !zRemovePreference; i++) {
            Preference preference = preferenceGroup.getPreference(i);
            if (str.equals(preference.getKey())) {
                zRemovePreference = preferenceGroup.removePreference(preference);
            } else if (preference instanceof PreferenceGroup) {
                zRemovePreference = removePreference((PreferenceGroup) preference, str);
            }
        }
        return zRemovePreference;
    }

    public static boolean isAntiSpamNewVersion(Context context) throws Throwable {
        boolean z = false;
        if (context == null) {
            Log.w("CallFeaturesSetting", "isAntiSpamNewVersion context is null");
            return false;
        }
        ContentProviderClient contentProviderClient = null;
        try {
            try {
                ContentProviderClient contentProviderClientAcquireUnstableContentProviderClient = context.getContentResolver().acquireUnstableContentProviderClient(Uri.parse("content://antispam"));
                if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                    try {
                        Bundle bundleCall = contentProviderClientAcquireUnstableContentProviderClient.call("isAntiSpamNewVersion", null, null);
                        if (bundleCall != null) {
                            z = bundleCall.getBoolean("isAntiSpamNewVersion", false);
                            Log.i("CallFeaturesSetting", "isAntiSpamNewVersion=" + z);
                        } else {
                            Log.i("CallFeaturesSetting", "isAntiSpamNewVersion bundle is null");
                        }
                    } catch (Exception e) {
                        e = e;
                        contentProviderClient = contentProviderClientAcquireUnstableContentProviderClient;
                        Log.e("CallFeaturesSetting", "isAntiSpamNewVersion failed", e);
                        if (contentProviderClient != null) {
                            contentProviderClient.close();
                        }
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        contentProviderClient = contentProviderClientAcquireUnstableContentProviderClient;
                        if (contentProviderClient != null) {
                            contentProviderClient.close();
                        }
                        throw th;
                    }
                }
                if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                    contentProviderClientAcquireUnstableContentProviderClient.close();
                }
                return z;
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public class CallFeaturesSettingFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
        private Preference mAiAssistant;
        private Preference mAnswerState;
        private MiuixTextPreference mAntispamSetting;
        private MiuixTextPreference mAutoAnswer;
        private Preference mCallAdvancedSetting;
        private Preference mCallFoldSetting;
        private Preference mCallForwarding;
        private MiuixTextPreference mCallRecord;
        private Preference mCallWaiting;
        private MiuixTextPreference mCloudAntispam;
        private CheckBoxPreference mFlipAutoAnswerCall;
        private CheckBoxPreference mFlipAutoHangupCall;
        private int mIccCardCount;
        private Preference mNetworkSetting;
        private Preference mNewCallSettings;
        private Phone mPhone;
        private Preference mPhoneFeedback;
        private MiuixTextPreference mPrivacyPolicy;
        private CheckBoxPreference mSensorAnswer;
        private Preference mTelocation;
        private Preference mVoiceMailCallForward;
        private DropDownPreference mVoiceReporter;
        private final boolean[] mIMSState = new boolean[2];
        private final boolean[] mWFCState = new boolean[2];
        private boolean mIsReceiverRegistered = false;
        private boolean mFirstInit = true;
        private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.CallFeaturesSetting.CallFeaturesSettingFragment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    return;
                }
                String action = intent.getAction();
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    int iccCardCount = TelephonyManager.getDefault().getIccCardCount();
                    if (MiuiPhoneUtils.isDcOnlyVirtualSim(context)) {
                        iccCardCount--;
                    }
                    if (iccCardCount == CallFeaturesSettingFragment.this.mIccCardCount) {
                        return;
                    } else {
                        CallFeaturesSettingFragment.this.mIccCardCount = iccCardCount;
                    }
                } else if ("android.intent.action.RADIO_TECHNOLOGY".equals(action)) {
                    if (CallFeaturesSettingFragment.this.mFirstInit) {
                        CallFeaturesSettingFragment.this.mFirstInit = false;
                        return;
                    }
                } else if (!"android.intent.action.AIRPLANE_MODE".equals(action)) {
                    if (!"android.intent.action.ACTION_IMS_REGISTED".equals(action)) {
                        return;
                    }
                    int intExtra = intent.getIntExtra("phone", -1);
                    boolean booleanExtra = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                    boolean booleanExtra2 = intent.getBooleanExtra("wfc_state", false);
                    if (intExtra == 0) {
                        if (booleanExtra == CallFeaturesSettingFragment.this.mIMSState[0] && booleanExtra2 == CallFeaturesSettingFragment.this.mWFCState[0]) {
                            return;
                        }
                        CallFeaturesSettingFragment.this.mIMSState[0] = booleanExtra;
                        CallFeaturesSettingFragment.this.mWFCState[0] = booleanExtra2;
                    } else {
                        if (intExtra != 1) {
                            return;
                        }
                        if (booleanExtra == CallFeaturesSettingFragment.this.mIMSState[1] && booleanExtra2 == CallFeaturesSettingFragment.this.mWFCState[1]) {
                            return;
                        }
                        CallFeaturesSettingFragment.this.mIMSState[1] = booleanExtra;
                        CallFeaturesSettingFragment.this.mWFCState[1] = booleanExtra2;
                    }
                }
                CallFeaturesSettingFragment.this.mHandler.removeMessages(1);
                CallFeaturesSettingFragment.this.mHandler.sendEmptyMessageDelayed(1, 2000L);
            }
        };
        Handler mHandler = new Handler() { // from class: com.android.phone.settings.CallFeaturesSetting.CallFeaturesSettingFragment.2
            @Override // android.os.Handler
            public void handleMessage(Message message) throws Throwable {
                if (message.what != 1) {
                    return;
                }
                CallFeaturesSettingFragment.this.getPreferenceScreen().removeAll();
                CallFeaturesSettingFragment.this.initScreen();
                CallFeaturesSettingFragment.this.updateScreen();
            }
        };

        private void configureFloatingServiceSafely(Intent intent) {
            if (getActivity() instanceof AppCompatActivity) {
                MultiAppFloatingActivitySwitcher.configureFloatingService(intent, (AppCompatActivity) getActivity());
            }
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) throws Throwable {
            this.mPhone = MiuiPhoneUtils.getPhone(0);
            this.mIMSState[0] = MiuiPhoneUtils.getPhone(0) == null ? false : MiuiPhoneUtils.getPhone(0).isImsRegistered();
            this.mIMSState[1] = MiuiPhoneUtils.getPhone(1) == null ? false : MiuiPhoneUtils.getPhone(1).isImsRegistered();
            this.mWFCState[0] = MiuiPhoneUtils.getPhone(0) == null ? false : MiuiPhoneUtils.getPhone(0).isWifiCallingEnabled();
            this.mWFCState[1] = MiuiPhoneUtils.getPhone(1) != null ? MiuiPhoneUtils.getPhone(1).isWifiCallingEnabled() : false;
            this.mIccCardCount = TelephonyManager.getDefault().getIccCardCount();
            if (MiuiPhoneUtils.isDcOnlyVirtualSim(getContext())) {
                this.mIccCardCount--;
            }
            initScreen();
            IntentFilter intentFilter = new IntentFilter("android.intent.action.RADIO_TECHNOLOGY");
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
            intentFilter.addAction("android.intent.action.ACTION_IMS_REGISTED");
            getContext().registerReceiver(this.mReceiver, intentFilter, 2);
            this.mIsReceiverRegistered = true;
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            updateScreen();
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            this.mHandler.removeMessages(1);
            if (this.mIsReceiverRegistered) {
                this.mIsReceiverRegistered = false;
                getContext().unregisterReceiver(this.mReceiver);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initScreen() throws Throwable {
            addPreferencesFromResource(R.xml.miui_call_feature_setting);
            Preference preferenceFindPreference = findPreference("pref_key_call_network_setting");
            this.mNetworkSetting = preferenceFindPreference;
            preferenceFindPreference.setOnPreferenceClickListener(this);
            this.mFlipAutoAnswerCall = (CheckBoxPreference) findPreference("button_filp_auto_answer_call_key");
            this.mFlipAutoHangupCall = (CheckBoxPreference) findPreference("button_filp_auto_hangup_call_key");
            MiuixTextPreference miuixTextPreference = (MiuixTextPreference) findPreference("pref_key_call_record_setting");
            this.mCallRecord = miuixTextPreference;
            miuixTextPreference.setOnPreferenceClickListener(this);
            Preference preferenceFindPreference2 = findPreference("button_call_forwarding");
            this.mCallForwarding = preferenceFindPreference2;
            if (preferenceFindPreference2 != null) {
                preferenceFindPreference2.setOnPreferenceClickListener(this);
            }
            Preference preferenceFindPreference3 = findPreference("button_call_waiting");
            this.mCallWaiting = preferenceFindPreference3;
            if (preferenceFindPreference3 != null) {
                preferenceFindPreference3.setOnPreferenceClickListener(this);
            }
            Preference preferenceFindPreference4 = findPreference("button_answer_state_setting");
            this.mAnswerState = preferenceFindPreference4;
            preferenceFindPreference4.setOnPreferenceClickListener(this);
            MiuixTextPreference miuixTextPreference2 = (MiuixTextPreference) findPreference("pref_key_cloud_antispam");
            this.mCloudAntispam = miuixTextPreference2;
            if (miuixTextPreference2 != null) {
                miuixTextPreference2.setOnPreferenceClickListener(this);
            }
            Preference preferenceFindPreference5 = findPreference("button_ai_assistant_setting");
            this.mAiAssistant = preferenceFindPreference5;
            if (preferenceFindPreference5 != null) {
                boolean zIsNewAiCallSettingSupported = isNewAiCallSettingSupported();
                Preference preference = this.mAiAssistant;
                if (zIsNewAiCallSettingSupported) {
                    preference.setTitle(R.string.ai_call_setting_title);
                } else {
                    preference.setTitle(Utils.supportXiaoAi(getContext()) ? R.string.xiaoai_assistant_setting_title : R.string.ai_assistant_setting_title);
                }
                this.mAiAssistant.setOnPreferenceClickListener(this);
            }
            MiuixTextPreference miuixTextPreference3 = (MiuixTextPreference) findPreference("pref_key_antispam_setting");
            this.mAntispamSetting = miuixTextPreference3;
            if (miuixTextPreference3 != null) {
                miuixTextPreference3.setOnPreferenceClickListener(this);
            }
            MiuixTextPreference miuixTextPreference4 = (MiuixTextPreference) findPreference("button_auto_answer_screen");
            this.mAutoAnswer = miuixTextPreference4;
            miuixTextPreference4.setOnPreferenceClickListener(this);
            Preference preferenceFindPreference6 = findPreference("pref_key_telocation");
            this.mTelocation = preferenceFindPreference6;
            if (preferenceFindPreference6 != null) {
                preferenceFindPreference6.setOnPreferenceClickListener(this);
            }
            MiuixTextPreference miuixTextPreference5 = (MiuixTextPreference) findPreference("pref_key_privacy_policy");
            this.mPrivacyPolicy = miuixTextPreference5;
            miuixTextPreference5.setOnPreferenceClickListener(this);
            Preference preferenceFindPreference7 = findPreference("pref_key_feedback");
            this.mPhoneFeedback = preferenceFindPreference7;
            preferenceFindPreference7.setOnPreferenceClickListener(this);
            this.mVoiceReporter = (DropDownPreference) findPreference("button_incoming_call_report");
            this.mSensorAnswer = (CheckBoxPreference) findPreference("perf_sensor_answer");
            this.mNewCallSettings = findPreference("button_newcall");
            Preference preferenceFindPreference8 = findPreference("call_fold_setting");
            this.mCallFoldSetting = preferenceFindPreference8;
            if (preferenceFindPreference8 != null) {
                preferenceFindPreference8.setOnPreferenceClickListener(this);
            }
            Preference preferenceFindPreference9 = findPreference("call_advanced_setting");
            this.mCallAdvancedSetting = preferenceFindPreference9;
            if (preferenceFindPreference9 != null) {
                preferenceFindPreference9.setOnPreferenceClickListener(this);
            }
            final PreferenceScreen preferenceScreen = getPreferenceScreen();
            if (!TelephonyManager.isCustForJpSb()) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_xcap_setting");
            }
            if (!MiuiJpOperatorCustimizedUtils.isCustForJpKd() || !MiuiPhoneUtils.needRemoveCallForwardOption()) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_voicemail_callforward");
            } else {
                Preference preferenceFindPreference10 = findPreference("button_voicemail_callforward");
                this.mVoiceMailCallForward = preferenceFindPreference10;
                preferenceFindPreference10.setOnPreferenceClickListener(this);
            }
            if (!VoiceReportUtils.isSupportVoiceReportV2(getContext())) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_incoming_call_report");
            } else {
                this.mVoiceReporter.setOnPreferenceChangeListener(this);
            }
            MiuixTextPreference miuixTextPreference6 = this.mCallRecord;
            if (miuixTextPreference6 != null) {
                miuixTextPreference6.setShowRightArrow(true);
                if (!TelephonyCapabilities.supportsCallRecording(getContext())) {
                    CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_call_record_setting");
                }
                if (PhoneHelperStub.getInstance().isAutoRecordPhoneCall() || MiuiPhoneUtils.isSatelliteEnable(getContext())) {
                    this.mCallRecord.setEnabled(false);
                }
            }
            if (!isVoipAvailable()) {
                CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_voip_setting");
            }
            if (MiuiPhoneUtils.isTabletDeviceWithVolteCapability()) {
                this.mAnswerState.setSummary((CharSequence) null);
                CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_antispam_setting");
                CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_telocation");
                this.mAntispamSetting = null;
                this.mTelocation = null;
            }
            if (TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(getContext())) {
                if (this.mCloudAntispam != null) {
                    CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_cloud_antispam");
                    this.mCloudAntispam = null;
                }
                if (this.mAntispamSetting != null) {
                    CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_antispam_setting");
                    this.mAntispamSetting = null;
                }
                if (this.mTelocation != null) {
                    CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_telocation");
                    this.mTelocation = null;
                }
                if (!isExistNonSimAccounts() && !isCtsVerifierInstalled(getContext())) {
                    CallFeaturesSetting.removePreference(preferenceScreen, "button_phone_account");
                }
            } else {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_phone_account");
            }
            MiuixTextPreference miuixTextPreference7 = this.mCloudAntispam;
            if (miuixTextPreference7 != null) {
                miuixTextPreference7.setShowRightArrow(true);
                if (!YellowPageUtils.isYellowPageAvailable(getContext()) || this.mCloudAntispam.getIntent().resolveActivity(getContext().getPackageManager()) == null) {
                    CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_cloud_antispam");
                }
            }
            MiuixTextPreference miuixTextPreference8 = this.mAntispamSetting;
            if (miuixTextPreference8 != null) {
                miuixTextPreference8.setShowRightArrow(true);
                updateAntispamSetting();
            }
            MiuixTextPreference miuixTextPreference9 = this.mAutoAnswer;
            if (miuixTextPreference9 != null) {
                miuixTextPreference9.setShowRightArrow(true);
                if (PhoneHelperStub.getInstance().isAutoRecordPhoneCall()) {
                    this.mAutoAnswer.setEnabled(false);
                }
            }
            boolean z = Build.IS_INTERNATIONAL_BUILD;
            if (z || ModemProvider.IS_SPRD) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_ai_assistant_setting");
                this.mAiAssistant = null;
            } else if (!isNewAiCallSettingSupported()) {
                if (!Utils.isInstalled(getContext(), "com.xiaomi.aiasst.service")) {
                    CallFeaturesSetting.removePreference(preferenceScreen, "button_ai_assistant_setting");
                    this.mAiAssistant = null;
                } else {
                    Utils.isAiAvailableAsync(getContext(), new Utils.AiCallback() { // from class: com.android.phone.settings.CallFeaturesSetting.CallFeaturesSettingFragment.3
                        @Override // com.android.phone.utils.Utils.AiCallback
                        public void onComplete(boolean z2) {
                            if (z2) {
                                return;
                            }
                            CallFeaturesSetting.removePreference(preferenceScreen, "button_ai_assistant_setting");
                            CallFeaturesSettingFragment.this.mAiAssistant = null;
                        }
                    });
                }
            }
            boolean zIsNumberEncryptEnabled = PhoneHelperStub.getInstance().isNumberEncryptEnabled();
            if (z || zIsNumberEncryptEnabled) {
                CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_auto_ip");
            }
            if (!Utils.isSupportSensorAnswer(getContext())) {
                CallFeaturesSetting.removePreference(preferenceScreen, "perf_sensor_answer");
                this.mSensorAnswer = null;
                Log.i("CallFeaturesSetting", "Sensor answer is not supported.");
            }
            CheckBoxPreference checkBoxPreference = this.mSensorAnswer;
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(TelephonySettings.isSensorAnswerEnabled(getContext().getContentResolver()));
                this.mSensorAnswer.setOnPreferenceChangeListener(this);
            }
            if (!MiuiPhoneUtils.isDeviceSupportFold()) {
                CallFeaturesSetting.removePreference(preferenceScreen, "call_fold_setting");
            }
            if (!MiuiPhoneUtils.isDeviceSupportFlip()) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_filp_auto_answer_call_key");
                CallFeaturesSetting.removePreference(preferenceScreen, "button_filp_auto_hangup_call_key");
                this.mFlipAutoAnswerCall = null;
                this.mFlipAutoHangupCall = null;
                Log.i("CallFeaturesSetting", "Flip auto call is not supported.");
            }
            CheckBoxPreference checkBoxPreference2 = this.mFlipAutoAnswerCall;
            if (checkBoxPreference2 != null) {
                checkBoxPreference2.setChecked(TelephonySettings.isFlipAutoAnswerCall(getContext().getContentResolver()));
                this.mFlipAutoAnswerCall.setOnPreferenceChangeListener(this);
            }
            CheckBoxPreference checkBoxPreference3 = this.mFlipAutoHangupCall;
            if (checkBoxPreference3 != null) {
                checkBoxPreference3.setChecked(TelephonySettings.isFlipAutoHangupCall(getContext().getContentResolver()));
                this.mFlipAutoHangupCall.setOnPreferenceChangeListener(this);
            }
            if (z || !MiuiNewCallUtils.isNewCallFeatureEnabled() || !MiuiNewCallUtils.isNewCallSdkInstalled()) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_newcall");
            } else {
                this.mNewCallSettings.setOnPreferenceChangeListener(this);
            }
        }

        public boolean isExistNonSimAccounts() {
            TelecomManager telecomManagerFrom = TelecomManager.from(getContext());
            Iterator it = telecomManagerFrom.getCallCapablePhoneAccounts(true).iterator();
            while (it.hasNext()) {
                PhoneAccount phoneAccount = telecomManagerFrom.getPhoneAccount((PhoneAccountHandle) it.next());
                if (phoneAccount != null && !phoneAccount.hasCapabilities(4)) {
                    return true;
                }
            }
            return false;
        }

        private static boolean isCtsVerifierInstalled(Context context) {
            boolean z = false;
            try {
                z = context.getPackageManager().getApplicationInfo("com.android.cts.verifier", 0) != null;
                Log.i("CallFeaturesSetting", "isCtsVerifierInstalled=" + z);
                return z;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return z;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateScreen() {
            boolean z;
            boolean z2;
            boolean z3;
            String string;
            String string2;
            Preference preference = this.mNetworkSetting;
            if (preference != null) {
                preference.setTitle(R.string.network_settings_title);
            }
            boolean z4 = false;
            if (this.mCallRecord != null) {
                boolean zIsAutoRecordOn = CallRecordSetting.isAutoRecordOn(getContext());
                MiuixTextPreference miuixTextPreference = this.mCallRecord;
                if (zIsAutoRecordOn) {
                    miuixTextPreference.setValue(R.string.call_record_on_summary);
                } else {
                    miuixTextPreference.setValue(R.string.call_record_off_summary);
                }
                if (PhoneHelperStub.getInstance().isAutoRecordPhoneCall() || MiuiPhoneUtils.isSatelliteEnable(getContext())) {
                    this.mCallRecord.setEnabled(false);
                }
            }
            if (this.mVoiceMailCallForward != null && SimpleFeatures.isHideVoicemailSettings(getContext())) {
                this.mVoiceMailCallForward.setTitle(getString(R.string.call_forwarding_settings));
            }
            if (this.mVoiceReporter != null) {
                int i = Settings.Global.getInt(getContext().getContentResolver(), "voice_report_method_phone", 2);
                this.mVoiceReporter.setValueIndex(this.mVoiceReporter.findIndexOfValue(i + ""));
            }
            if (this.mTelocation != null) {
                if (MiuiSettings.Telephony.isTelocationEnable(getContext().getContentResolver())) {
                    string = getString(R.string.preference_telocation_summary_teloc_on);
                } else {
                    string = getString(R.string.preference_telocation_summary_teloc_off);
                }
                if (MiuiSettings.Telephony.isAutoCountryCodeEnable(getContext().getContentResolver())) {
                    string2 = getString(R.string.preference_telocation_summary_countrycode_on);
                } else {
                    string2 = getString(R.string.preference_telocation_summary_countrycode_off);
                }
                boolean zIsRemoveCallerloc = TelephonyManager.isRemoveCallerloc();
                Preference preference2 = this.mTelocation;
                if (zIsRemoveCallerloc) {
                    preference2.setTitle(getString(R.string.preference_countrycode_title));
                    this.mTelocation.setSummary(string2);
                } else {
                    preference2.setSummary(string + ", " + string2);
                }
            }
            if (this.mSensorAnswer != null) {
                if (MiuiPhoneUtils.isSatelliteEnable(getContext())) {
                    Log.i("CallFeaturesSetting", "Sensor answer can't use in satellite mode!");
                    setEnabledForKey(false, "perf_sensor_answer");
                } else {
                    setEnabledForKey(true, "perf_sensor_answer");
                }
            }
            if (this.mAutoAnswer != null) {
                if (!MiuiSettings.Telephony.isAutoAnswerCall(getContext().getContentResolver())) {
                    this.mAutoAnswer.setValue(R.string.auto_answer_summary_not_enabled);
                    this.mAutoAnswer.setSummary("");
                } else {
                    int autoAnswerCallDelayTime = MiuiSettings.Telephony.getAutoAnswerCallDelayTime(getContext().getContentResolver());
                    this.mAutoAnswer.setValue(getResources().getStringArray(R.array.auto_answer_scenario_entries)[MiuiSettings.Telephony.getAutoAnswerCallScenario(getContext().getContentResolver())]);
                    this.mAutoAnswer.setSummary(getResources().getQuantityString(R.plurals.auto_answer_summary_delay, autoAnswerCallDelayTime, Integer.valueOf(autoAnswerCallDelayTime)));
                }
            }
            boolean z5 = Settings.System.getInt(getContext().getContentResolver(), "airplane_mode_on", 0) > 0;
            if (!z5) {
                int phoneCount = TelephonyManager.getDefault().getPhoneCount();
                z2 = false;
                z3 = false;
                for (int i2 = 0; i2 < phoneCount; i2++) {
                    Phone phone = MiuiPhoneUtils.getPhone(i2);
                    if (!MiPhoneAppStub.get().isDataOnlySlot(getContext(), phone)) {
                        boolean zHasIccCard = phone.getIccCard().hasIccCard();
                        z2 |= zHasIccCard && !phone.getIccCard().getIccFdnEnabled();
                        z3 |= zHasIccCard;
                    }
                }
                z = false;
            } else {
                z = MiuiImsPhoneUtils.isWifiCallingEnabled() && !MiuiPhoneUtils.hasDcOnlyEsim(getContext());
                z2 = false;
                z3 = false;
            }
            if (MiuiKoreaOperatorCustimizedUtils.isCustForKrLgu()) {
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_call_forwarding");
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_call_waiting");
                this.mCallForwarding = null;
                this.mCallWaiting = null;
                return;
            }
            if ((z2 && z3 && !z5) || (z && !MiuiPhoneUtils.disableSSOverWfcEnableAirplane())) {
                z4 = true;
            }
            boolean zIsNumberEncryptEnabled = PhoneHelperStub.getInstance().isNumberEncryptEnabled();
            if (MiuiPhoneUtils.needRemoveCallForwardOption() || zIsNumberEncryptEnabled) {
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_call_forwarding");
            } else {
                setEnabledForKey(z4, "button_call_forwarding");
            }
            if (MiuiPhoneUtils.needRemoveCallWaitingOption()) {
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_call_waiting");
            } else if (z && MiuiPhoneUtils.setCallWaitingEnabled()) {
                setEnabledForKey(true, "button_call_waiting");
            } else {
                setEnabledForKey(z4, "button_call_waiting");
            }
            setEnabledForKey(z4, "button_voicemail_callforward");
        }

        private void setEnabledForKey(boolean z, String str) {
            Preference preferenceFindPreference = getPreferenceScreen().findPreference(str);
            if (preferenceFindPreference != null) {
                preferenceFindPreference.setEnabled(z);
            }
        }

        private boolean isNewAiCallSettingSupported() {
            return Utils.isAiAssistAnswerSupported(getContext());
        }

        private void updateAntispamSetting() throws Throwable {
            if (this.mAntispamSetting == null) {
                return;
            }
            boolean zIsAntiSpamNewVersion = CallFeaturesSetting.isAntiSpamNewVersion(getContext());
            MiuixTextPreference miuixTextPreference = this.mAntispamSetting;
            if (zIsAntiSpamNewVersion) {
                miuixTextPreference.setTitle(R.string.call_antispam_setting);
                Intent intent = new Intent();
                intent.setAction("com.miui.antispam.action.ANTISPAM_CALL_SETTINGS");
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.antispam.ui.activity.AntiSpamCallSettingsNewActivity"));
                this.mAntispamSetting.setIntent(intent);
                return;
            }
            miuixTextPreference.setTitle(R.string.antispam_setting);
        }

        private boolean isVoipAvailable() {
            if (!getResources().getBoolean(R.bool.voip_enabled)) {
                return false;
            }
            try {
                this.mPhone.getContext().getPackageManager().getPackageInfo("com.miui.voip", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
                return true;
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (preference.getKey().equals("button_incoming_call_report")) {
                Settings.Global.putInt(getContext().getContentResolver(), "voice_report_method_phone", this.mVoiceReporter.findIndexOfValue((String) obj));
                return true;
            }
            if (preference.getKey().equals("perf_sensor_answer")) {
                TelephonySettings.setSensorAnswerEnabled(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
                return true;
            }
            if (preference.getKey().equals("button_filp_auto_answer_call_key")) {
                Boolean bool = (Boolean) obj;
                TelephonySettings.setFlipAutoAnswerCall(getContext().getContentResolver(), bool.booleanValue());
                MiStatInterfaceUtil.recordSwitchStatusEvent("button_filp_auto_answer_call_key", bool.booleanValue());
                return true;
            }
            if (!preference.getKey().equals("button_filp_auto_hangup_call_key")) {
                return true;
            }
            Boolean bool2 = (Boolean) obj;
            TelephonySettings.setFlipAutoHangupCall(getContext().getContentResolver(), bool2.booleanValue());
            MiStatInterfaceUtil.recordSwitchStatusEvent("button_filp_auto_hangup_call_key", bool2.booleanValue());
            return true;
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            Intent intent;
            Context context = getContext();
            if (context == null) {
                return true;
            }
            MiuixTextPreference miuixTextPreference = this.mCloudAntispam;
            if (preference == miuixTextPreference) {
                try {
                    Intent intent2 = miuixTextPreference.getIntent();
                    if ((getActivity().getIntent().getMiuiFlags() & 4) == 0) {
                        intent2.putExtra("fromSettingSplit", false);
                    }
                    configureFloatingServiceSafely(intent2);
                    context.startActivity(intent2);
                } catch (ActivityNotFoundException unused) {
                    Log.e("CallFeaturesSetting", "cloud antispam activity not found.");
                }
                OneTrackImpl.trackEvent("key_enter_cloud_antispam");
            } else if (preference == this.mPrivacyPolicy) {
                try {
                    Intent intent3 = new Intent(context, (Class<?>) PrivacySettingActivity.class);
                    ScreenDisplayUtils.shouldForceSplit(intent3, getActivity().getIntent().getMiuiFlags());
                    configureFloatingServiceSafely(intent3);
                    context.startActivity(intent3);
                } catch (ActivityNotFoundException unused2) {
                    Log.e("CallFeaturesSetting", "PrivacySettingActivity not found.");
                }
            } else if (preference == this.mPhoneFeedback) {
                try {
                    Intent intent4 = new Intent("miui.intent.action.BUGREPORT");
                    intent4.putExtra(SettingsBackupConsts.EXTRA_PACKAGE_NAME, context.getPackageName());
                    ScreenDisplayUtils.shouldForceSplit(intent4, getActivity().getIntent().getMiuiFlags());
                    configureFloatingServiceSafely(intent4);
                    context.startActivity(intent4);
                } catch (ActivityNotFoundException unused3) {
                    Log.e("CallFeaturesSetting", "miui.intent.action.BUGREPORT not found.");
                }
            } else if (preference == this.mAiAssistant) {
                if (isNewAiCallSettingSupported()) {
                    intent = new Intent("miui.intent.action.AiCallSetting");
                    intent.setPackage("com.xiaomi.phone");
                } else {
                    intent = new Intent("com.xiaomi.aiasst.service.aicall.settings");
                    intent.setPackage("com.xiaomi.aiasst.service");
                    intent.putExtra("from_package", "com.android.incallui");
                    configureFloatingServiceSafely(intent);
                    OneTrackImpl.trackEvent("key_enter_ai_assistant");
                }
                ScreenDisplayUtils.shouldForceSplit(intent, getActivity().getIntent().getMiuiFlags());
                startActivity(intent);
            } else if (preference == this.mVoiceMailCallForward) {
                try {
                    Intent intent5 = new Intent(context, (Class<?>) MiuiVoiceMailCallForwardActivity.class);
                    configureFloatingServiceSafely(intent5);
                    context.startActivity(intent5);
                } catch (ActivityNotFoundException unused4) {
                    Log.e("CallFeaturesSetting", "MiuiVoiceMailCallForwardActivity not found.");
                }
            } else {
                Preference preference2 = this.mNetworkSetting;
                if (preference == preference2) {
                    Intent intent6 = preference2.getIntent();
                    configureFloatingServiceSafely(intent6);
                    context.startActivity(intent6);
                    OneTrackImpl.trackEvent("key_enter_network_settings");
                } else {
                    MiuixTextPreference miuixTextPreference2 = this.mCallRecord;
                    if (preference == miuixTextPreference2) {
                        Intent intent7 = miuixTextPreference2.getIntent();
                        configureFloatingServiceSafely(intent7);
                        context.startActivity(intent7);
                        OneTrackImpl.trackEvent("key_enter_call_record_settings");
                    } else {
                        Preference preference3 = this.mCallForwarding;
                        if (preference == preference3) {
                            Intent intent8 = preference3.getIntent();
                            configureFloatingServiceSafely(intent8);
                            context.startActivity(intent8);
                            OneTrackImpl.trackEvent("key_enter_call_forward_settings");
                        } else {
                            Preference preference4 = this.mCallWaiting;
                            if (preference == preference4) {
                                Intent intent9 = preference4.getIntent();
                                configureFloatingServiceSafely(intent9);
                                context.startActivity(intent9);
                                OneTrackImpl.trackEvent("key_enter_call_waiting_settings");
                            } else {
                                Preference preference5 = this.mAnswerState;
                                if (preference == preference5) {
                                    Intent intent10 = preference5.getIntent();
                                    configureFloatingServiceSafely(intent10);
                                    context.startActivity(intent10);
                                    OneTrackImpl.trackEvent("key_enter_answer_state_settings");
                                } else {
                                    MiuixTextPreference miuixTextPreference3 = this.mAntispamSetting;
                                    if (preference == miuixTextPreference3) {
                                        Intent intent11 = miuixTextPreference3.getIntent();
                                        configureFloatingServiceSafely(intent11);
                                        context.startActivity(intent11);
                                        OneTrackImpl.trackEvent("key_enter_antispam");
                                    } else {
                                        Preference preference6 = this.mTelocation;
                                        if (preference == preference6) {
                                            Intent intent12 = preference6.getIntent();
                                            configureFloatingServiceSafely(intent12);
                                            context.startActivity(intent12);
                                            OneTrackImpl.trackEvent("key_enter_telocation_settings");
                                        } else {
                                            MiuixTextPreference miuixTextPreference4 = this.mAutoAnswer;
                                            if (preference == miuixTextPreference4) {
                                                Intent intent13 = miuixTextPreference4.getIntent();
                                                configureFloatingServiceSafely(intent13);
                                                context.startActivity(intent13);
                                                OneTrackImpl.trackEvent("key_enter_auto_answer_settings");
                                            } else {
                                                Preference preference7 = this.mNewCallSettings;
                                                if (preference == preference7) {
                                                    Intent intent14 = preference7.getIntent();
                                                    configureFloatingServiceSafely(intent14);
                                                    context.startActivity(intent14);
                                                    OneTrackImpl.trackEvent("key_enter_new_call_settings");
                                                } else if (preference == this.mCallFoldSetting) {
                                                    Intent intent15 = new Intent("android.intent.action.MAIN");
                                                    intent15.setClassName(context.getPackageName(), CallFoldSetting.class.getName());
                                                    configureFloatingServiceSafely(intent15);
                                                    context.startActivity(intent15);
                                                } else if (preference == this.mCallAdvancedSetting) {
                                                    Intent intent16 = new Intent("android.intent.action.MAIN");
                                                    intent16.setClassName(context.getPackageName(), CallAdvancedSetting.class.getName());
                                                    configureFloatingServiceSafely(intent16);
                                                    context.startActivity(intent16);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
}
