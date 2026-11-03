package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.sip.SipManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.Phone;
import com.android.phone.MiPhoneAppStub;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.custompreferences.MiuixTextPreference;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.android.phone.utils.OneTrackImpl;
import com.android.services.telephony.SimpleFeatures;
import miui.enterprise.EnterpriseManagerStub;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.provider.ExtraContacts;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;
import miuix.os.AsyncTaskWithProgress;
import miuix.preference.DropDownPreference;
import miuix.view.HapticCompat;

/* JADX INFO: loaded from: classes.dex */
public class CallAdvancedSetting extends BaseActivity {
    private static final String LOG_TAG = "CallAdvancedSetting";
    private CallAdvancedSettingFragment mCallAdvancedSettingFragment;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        CallAdvancedSettingFragment callAdvancedSettingFragment = (CallAdvancedSettingFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        this.mCallAdvancedSettingFragment = callAdvancedSettingFragment;
        if (bundle == null && callAdvancedSettingFragment == null) {
            CallAdvancedSettingFragment callAdvancedSettingFragment2 = new CallAdvancedSettingFragment();
            this.mCallAdvancedSettingFragment = callAdvancedSettingFragment2;
            loadFragment(callAdvancedSettingFragment2);
        }
    }

    public class CallAdvancedSettingFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
        private boolean DBG;
        private MiuixTextPreference mAutoIP;
        private CheckBoxTitleIconPreference mAutoRedial;
        private CheckBoxPreference mButtonAutoRetry;
        private DropDownPreference mButtonDTMF;
        private CheckBoxPreference mButtonHAC;
        private CheckBoxPreference mButtonRttCall;
        private DropDownPreference mButtonTTY;
        private Preference mCallBarring;
        private DropDownPreference mCallWaitingTone;
        private TitleIconPreference mCallerId;
        private CheckBoxTitleIconPreference mCdmaDisplayPreciseCallState;
        private DropDownPreference mConDisconVibrate;
        private MiuixTextPreference mDialPadTouchTone;
        private Preference mFdn;
        private boolean mFirstInit;
        Handler mHandler;
        private int mIccCardCount;
        private boolean mImsFirstInit;
        private boolean mIsReceiverRegistered;
        public CharSequence[] mMissCallNotifyTimesEntries;
        private DropDownPreference mMissedCallNotifyTimes;
        private Preference mQuickDial;
        private final BroadcastReceiver mReceiver;
        private MiuixTextPreference mRespondViaSms;
        private DropDownPreference mT9IndexingMethod;
        private Preference mVoicemail;

        public CallAdvancedSettingFragment() {
            this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
            this.mFirstInit = true;
            this.mImsFirstInit = true;
            this.mIsReceiverRegistered = false;
            this.mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.CallAdvancedSetting.CallAdvancedSettingFragment.1
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
                        if (iccCardCount == CallAdvancedSettingFragment.this.mIccCardCount) {
                            return;
                        } else {
                            CallAdvancedSettingFragment.this.mIccCardCount = iccCardCount;
                        }
                    } else if ("android.intent.action.RADIO_TECHNOLOGY".equals(action)) {
                        if (CallAdvancedSettingFragment.this.mFirstInit) {
                            CallAdvancedSettingFragment.this.mFirstInit = false;
                            return;
                        }
                    } else if (!"android.intent.action.AIRPLANE_MODE".equals(action)) {
                        if (!"android.intent.action.ACTION_IMS_REGISTED".equals(action)) {
                            return;
                        }
                        if (CallAdvancedSettingFragment.this.mImsFirstInit) {
                            CallAdvancedSettingFragment.this.mImsFirstInit = false;
                            return;
                        }
                    }
                    if (CallAdvancedSettingFragment.this.isAdded()) {
                        CallAdvancedSettingFragment.this.mHandler.removeMessages(1);
                        CallAdvancedSettingFragment.this.mHandler.sendEmptyMessageDelayed(1, 2000L);
                    }
                }
            };
            this.mHandler = new Handler() { // from class: com.android.phone.settings.CallAdvancedSetting.CallAdvancedSettingFragment.2
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    if (message.what == 1 && CallAdvancedSettingFragment.this.isAdded()) {
                        CallAdvancedSettingFragment.this.getPreferenceScreen().removeAll();
                        CallAdvancedSettingFragment.this.initScreen();
                        CallAdvancedSettingFragment.this.updateScreen();
                    }
                }
            };
        }

        private void configureFloatingServiceSafely(Intent intent) {
            if (getActivity() instanceof AppCompatActivity) {
                MultiAppFloatingActivitySwitcher.configureFloatingService(intent, (AppCompatActivity) getActivity());
            }
        }

        private void showRebuildT9IndexDialog(final Context context) {
            new AsyncTaskWithProgress(this, getFragmentManager()) { // from class: com.android.phone.settings.CallAdvancedSetting.CallAdvancedSettingFragment.3
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public Void doInBackground(Void... voidArr) {
                    context.getContentResolver().update(ExtraContacts.SmartDialer.CONTENT_REBUILDT9_URI, new ContentValues(), null, null);
                    return null;
                }
            }.setMessage(R.string.t9_indexing_rebuilding_message).setCancelable(false).execute(new Void[0]);
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
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
        public void initScreen() {
            boolean z;
            boolean z2;
            addPreferencesFromResource(R.xml.call_advanced_setting);
            this.mDialPadTouchTone = (MiuixTextPreference) findPreference("pref_key_dial_pad_touch_tone");
            this.mT9IndexingMethod = (DropDownPreference) findPreference("button_t9_index_method_settings");
            Preference preferenceFindPreference = findPreference("button_quick_dial");
            this.mQuickDial = preferenceFindPreference;
            preferenceFindPreference.setOnPreferenceClickListener(this);
            this.mAutoRedial = (CheckBoxTitleIconPreference) findPreference("button_auto_redial");
            MiuixTextPreference miuixTextPreference = (MiuixTextPreference) findPreference("pref_key_auto_ip");
            this.mAutoIP = miuixTextPreference;
            miuixTextPreference.setOnPreferenceClickListener(this);
            DropDownPreference dropDownPreference = (DropDownPreference) findPreference("button_missed_call_notify_times");
            this.mMissedCallNotifyTimes = dropDownPreference;
            dropDownPreference.setSummary(getResources().getString(R.string.missed_call_notify_times_summary, 5));
            CharSequence[] charSequenceArr = new CharSequence[6];
            this.mMissCallNotifyTimesEntries = charSequenceArr;
            charSequenceArr[0] = getResources().getString(R.string.missed_call_notify_none);
            this.mMissCallNotifyTimesEntries[1] = getResources().getString(R.string.missed_call_notify_once, 1);
            this.mMissCallNotifyTimesEntries[2] = getResources().getString(R.string.missed_call_notify_twice, 2);
            this.mMissCallNotifyTimesEntries[3] = getResources().getString(R.string.missed_call_notify_thrice, 3);
            this.mMissCallNotifyTimesEntries[4] = getResources().getString(R.string.missed_call_notify_five_times, 5);
            this.mMissCallNotifyTimesEntries[5] = getResources().getString(R.string.missed_call_notify_ten_times, 10);
            this.mMissedCallNotifyTimes.setEntries(this.mMissCallNotifyTimesEntries);
            this.mConDisconVibrate = (DropDownPreference) findPreference("button_connect_disconnect_vibrate");
            this.mCdmaDisplayPreciseCallState = (CheckBoxTitleIconPreference) findPreference("button_display_precise_call_state");
            this.mCallWaitingTone = (DropDownPreference) findPreference("button_call_waiting_tone");
            MiuixTextPreference miuixTextPreference2 = (MiuixTextPreference) findPreference("button_respond_via_sms_key");
            this.mRespondViaSms = miuixTextPreference2;
            miuixTextPreference2.setOnPreferenceClickListener(this);
            this.mButtonRttCall = (CheckBoxPreference) findPreference("button_rtt_call");
            this.mCallerId = (TitleIconPreference) findPreference("button_caller_id");
            this.mFdn = findPreference("button_fdn");
            this.mVoicemail = findPreference("button_voicemail");
            this.mCallBarring = findPreference("button_call_barring");
            this.mButtonAutoRetry = (CheckBoxPreference) findPreference("button_auto_retry");
            this.mButtonDTMF = (DropDownPreference) findPreference("button_dtmf_settings");
            this.mButtonHAC = (CheckBoxPreference) findPreference("button_hac");
            this.mButtonTTY = (DropDownPreference) findPreference("button_tty_mode");
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            initForPhoneType();
            if (MiuiPhoneUtils.isTabletDeviceWithVolteCapability()) {
                CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_auto_ip");
                CallFeaturesSetting.removePreference(preferenceScreen, "button_auto_redial");
                CallFeaturesSetting.removePreference(preferenceScreen, "button_sip_settings");
                CallFeaturesSetting.removePreference(preferenceScreen, "button_connect_disconnect_vibrate");
                CallFeaturesSetting.removePreference(preferenceScreen, "button_voice_privacy");
                this.mAutoIP = null;
                this.mAutoRedial = null;
                this.mConDisconVibrate = null;
            }
            if (TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(getContext())) {
                CallFeaturesSetting.removePreference(preferenceScreen, "pref_catory_key_dial_related_setting");
            }
            if (!MiuiPhoneUtils.isVibratorSupported()) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_connect_disconnect_vibrate");
                this.mConDisconVibrate = null;
            }
            MiuixTextPreference miuixTextPreference3 = this.mDialPadTouchTone;
            if (miuixTextPreference3 != null) {
                miuixTextPreference3.setOnPreferenceClickListener(this);
                this.mDialPadTouchTone.setShowRightArrow(true);
            }
            CheckBoxTitleIconPreference checkBoxTitleIconPreference = this.mAutoRedial;
            if (checkBoxTitleIconPreference != null) {
                checkBoxTitleIconPreference.setOnPreferenceClickListener(this);
            }
            MiuixTextPreference miuixTextPreference4 = this.mAutoIP;
            if (miuixTextPreference4 != null) {
                miuixTextPreference4.setShowRightArrow(true);
                boolean zIsNumberEncryptEnabled = PhoneHelperStub.getInstance().isNumberEncryptEnabled();
                if (Build.IS_INTERNATIONAL_BUILD || zIsNumberEncryptEnabled) {
                    CallFeaturesSetting.removePreference(preferenceScreen, "pref_key_auto_ip");
                }
            }
            MiuixTextPreference miuixTextPreference5 = this.mRespondViaSms;
            if (miuixTextPreference5 != null) {
                miuixTextPreference5.setShowRightArrow(true);
            }
            if (this.mButtonRttCall != null) {
                boolean zSupportXiaomiRtt = MiuiPhoneUtils.supportXiaomiRtt(getContext());
                CheckBoxPreference checkBoxPreference = this.mButtonRttCall;
                if (zSupportXiaomiRtt) {
                    checkBoxPreference.setOnPreferenceChangeListener(this);
                } else {
                    CallFeaturesSetting.removePreference(preferenceScreen, checkBoxPreference.getKey());
                }
            }
            if (this.mT9IndexingMethod != null) {
                boolean zIsAllowShowT9Index = isAllowShowT9Index();
                DropDownPreference dropDownPreference2 = this.mT9IndexingMethod;
                if (zIsAllowShowT9Index) {
                    dropDownPreference2.setOnPreferenceChangeListener(this);
                } else {
                    CallFeaturesSetting.removePreference(preferenceScreen, dropDownPreference2.getKey());
                }
            }
            DropDownPreference dropDownPreference3 = this.mMissedCallNotifyTimes;
            if (dropDownPreference3 != null) {
                dropDownPreference3.setOnPreferenceChangeListener(this);
            }
            CheckBoxTitleIconPreference checkBoxTitleIconPreference2 = this.mCdmaDisplayPreciseCallState;
            if (checkBoxTitleIconPreference2 != null) {
                checkBoxTitleIconPreference2.setOnPreferenceChangeListener(this);
            }
            if (this.mCallBarring != null && !MiuiPhoneUtils.shouldShowCallBarring()) {
                CallFeaturesSetting.removePreference(preferenceScreen, this.mCallBarring.getKey());
                this.mCallBarring = null;
            }
            if (this.mButtonAutoRetry != null) {
                boolean z3 = getResources().getBoolean(R.bool.auto_retry_enabled);
                CheckBoxPreference checkBoxPreference2 = this.mButtonAutoRetry;
                if (z3) {
                    checkBoxPreference2.setOnPreferenceChangeListener(this);
                } else {
                    CallFeaturesSetting.removePreference(preferenceScreen, checkBoxPreference2.getKey());
                    this.mButtonAutoRetry = null;
                }
            }
            if (this.mButtonDTMF != null) {
                if (getResources().getBoolean(R.bool.dtmf_type_enabled) && !TelephonyManager.isCustForMxTelcel()) {
                    this.mButtonDTMF.setOnPreferenceChangeListener(this);
                } else {
                    CallFeaturesSetting.removePreference(preferenceScreen, this.mButtonDTMF.getKey());
                    this.mButtonDTMF = null;
                }
            }
            if (this.mButtonHAC != null) {
                boolean z4 = getResources().getBoolean(R.bool.hac_enabled);
                CheckBoxPreference checkBoxPreference3 = this.mButtonHAC;
                if (z4) {
                    checkBoxPreference3.setOnPreferenceClickListener(this);
                } else {
                    CallFeaturesSetting.removePreference(preferenceScreen, checkBoxPreference3.getKey());
                    this.mButtonHAC = null;
                }
            }
            if (this.mButtonTTY != null) {
                boolean z5 = getResources().getBoolean(R.bool.tty_enabled);
                DropDownPreference dropDownPreference4 = this.mButtonTTY;
                if (z5) {
                    dropDownPreference4.setOnPreferenceChangeListener(this);
                } else {
                    CallFeaturesSetting.removePreference(preferenceScreen, dropDownPreference4.getKey());
                    this.mButtonTTY = null;
                }
            }
            if (TelephonyManager.isCustFeatureResolveEnabled()) {
                z = HyperOSCustFeatureResolve.getBoolean("hide_sip_setting_preference", false);
            } else {
                z = Build.checkRegion("ZA") || TelephonyManager.isCustForLmClaro() || TelephonyManager.isCustForMxTelcel();
            }
            if (!Build.IS_INTERNATIONAL_BUILD || !SipManager.isApiSupported(getContext()) || z) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_sip_settings");
            }
            if (TelephonyManager.isCustFeatureResolveEnabled()) {
                z2 = HyperOSCustFeatureResolve.getBoolean("hide_caller_id_preference", false);
            } else {
                z2 = TelephonyManager.isCustForMxTelcel() || MiuiKoreaOperatorCustimizedUtils.isCustForKrOps();
            }
            if (z2) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_caller_id");
            }
        }

        private boolean isAllowShowT9Index() {
            if (TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(getContext())) {
                return false;
            }
            String region = Build.getRegion();
            return !Build.IS_INTERNATIONAL_BUILD || region.equals("HK") || region.equals("TW") || region.equals("SG") || region.equals("ID");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateScreen() {
            boolean z;
            boolean z2;
            boolean z3;
            int i;
            int i2;
            int i3;
            boolean z4 = true;
            if (this.mDialPadTouchTone != null) {
                if (Settings.System.getInt(getContext().getContentResolver(), "dtmf_tone", 0) == 1) {
                    if (Settings.System.getInt(getContext().getContentResolver(), "dial_pad_touch_tone", -1) == 0) {
                        i3 = R.string.preference_dial_pad_touch_tone_piano;
                    } else {
                        i3 = R.string.preference_dial_pad_touch_tone_default;
                    }
                    this.mDialPadTouchTone.setValue(i3);
                } else {
                    this.mDialPadTouchTone.setValue(R.string.preference_dial_pad_touch_tone_off);
                }
            }
            if (this.mT9IndexingMethod != null) {
                this.mT9IndexingMethod.setValueIndex(Settings.System.getInt(getContext().getContentResolver(), "t9_indexing_key", MiuiSettings.System.getT9IndexingKeyDefault()));
            }
            if (this.mAutoRedial != null) {
                this.mAutoRedial.setChecked(MiuiSettings.Telephony.getEnabledAutoRedial(getContext().getContentResolver()));
                if (EnterpriseManagerStub.ENTERPRISE_ACTIVATED) {
                    this.mAutoRedial.setEnabled(!PhoneHelperStub.getInstance().isDisableAutoRedial());
                }
            }
            MiuixTextPreference miuixTextPreference = this.mAutoIP;
            if (miuixTextPreference != null) {
                miuixTextPreference.setEnabled(this.mIccCardCount > 0);
            }
            if (this.mMissedCallNotifyTimes != null) {
                this.mMissedCallNotifyTimes.setValueIndex(this.mMissedCallNotifyTimes.findIndexOfValue(String.valueOf(MiuiSettings.Telephony.getMissedCallNotifyTimes(getContext().getContentResolver()))));
            }
            DropDownPreference dropDownPreference = this.mConDisconVibrate;
            if (dropDownPreference != null) {
                dropDownPreference.setOnPreferenceChangeListener(this);
                boolean zDoesSupportHaptic = HapticCompat.doesSupportHaptic("2.0");
                Resources resources = getResources();
                if (zDoesSupportHaptic) {
                    i = R.array.connect_disconnect_vibrate_entries_support_haptic2;
                } else {
                    i = R.array.connect_disconnect_vibrate_entries;
                }
                this.mConDisconVibrate.setEntries(resources.getStringArray(i));
                Resources resources2 = getResources();
                if (zDoesSupportHaptic) {
                    i2 = R.array.connect_disconnect_vibrate_entry_values_support_haptic2;
                } else {
                    i2 = R.array.connect_disconnect_vibrate_entry_values;
                }
                this.mConDisconVibrate.setEntryValues(resources2.getStringArray(i2));
                String vibrateKey = MiuiSettings.Telephony.getVibrateKey(getContext().getContentResolver());
                DropDownPreference dropDownPreference2 = this.mConDisconVibrate;
                if (zDoesSupportHaptic && "50".equals(vibrateKey)) {
                    vibrateKey = "100";
                }
                this.mConDisconVibrate.setValueIndex(dropDownPreference2.findIndexOfValue(vibrateKey));
            }
            if (this.mCdmaDisplayPreciseCallState != null) {
                this.mCdmaDisplayPreciseCallState.setChecked(Settings.System.getInt(getContext().getContentResolver(), "cdma_precise_answer_state", 1) == 1);
            }
            DropDownPreference dropDownPreference3 = this.mCallWaitingTone;
            if (dropDownPreference3 != null) {
                dropDownPreference3.setOnPreferenceChangeListener(this);
                getResources().getStringArray(R.array.call_waiting_tone_entries);
                this.mCallWaitingTone.setValueIndex(MiuiSettings.Telephony.getCallWaitingTone(getContext()));
            }
            if (this.mRespondViaSms != null) {
                boolean zIsRejectViaSmsEnable = MiuiSettings.Telephony.isRejectViaSmsEnable(getContext().getContentResolver());
                MiuixTextPreference miuixTextPreference2 = this.mRespondViaSms;
                if (zIsRejectViaSmsEnable) {
                    miuixTextPreference2.setValue(R.string.call_record_on_summary);
                } else {
                    miuixTextPreference2.setValue(R.string.call_record_off_summary);
                }
            }
            CheckBoxPreference checkBoxPreference = this.mButtonRttCall;
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(TelephonySettings.isRttOn(getContext().getContentResolver()));
            }
            if (this.mButtonDTMF != null) {
                this.mButtonDTMF.setValueIndex(Settings.System.getInt(getContext().getContentResolver(), "dtmf_tone_type", 0));
            }
            if (this.mButtonAutoRetry != null) {
                this.mButtonAutoRetry.setChecked(Settings.Global.getInt(getContext().getContentResolver(), "call_auto_retry", 0) != 0);
            }
            if (this.mButtonHAC != null) {
                this.mButtonHAC.setChecked(Settings.System.getInt(getContext().getContentResolver(), "hearing_aid", 0) != 0);
            }
            if (this.mButtonTTY != null) {
                int i4 = Settings.Secure.getInt(getContext().getContentResolver(), "preferred_tty_mode", 0);
                this.mButtonTTY.setValue(Integer.toString(i4));
                updatePreferredTtyModeSummary(i4);
            }
            if (SimpleFeatures.isHideVoicemailSettings(getContext())) {
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_voicemail");
                this.mVoicemail = null;
            }
            Preference preference = this.mVoicemail;
            if (preference != null) {
                preference.setOnPreferenceClickListener(this);
            }
            if (SimpleFeatures.isHidePhoneAccountSettings(getContext()) || (TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(getContext()))) {
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_phone_account");
            }
            if (MiuiPhoneUtils.isRemoveFdn()) {
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_fdn");
                this.mFdn = null;
            }
            Preference preference2 = this.mFdn;
            if (preference2 != null) {
                preference2.setOnPreferenceClickListener(this);
            }
            boolean z5 = Settings.System.getInt(getContext().getContentResolver(), "airplane_mode_on", 0) > 0;
            if (!z5) {
                int phoneCount = TelephonyManager.getDefault().getPhoneCount();
                z2 = false;
                z3 = false;
                for (int i5 = 0; i5 < phoneCount; i5++) {
                    Phone phone = MiuiPhoneUtils.getPhone(i5);
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
            boolean z6 = z2 && z3 && !z5;
            setEnabledForKey(z6, "button_voice_privacy");
            setEnabledForKey(z6 || (z && !MiuiPhoneUtils.disableSSOverWfcEnableAirplane()), "button_caller_id");
            setEnabledForKey(z3 && !z5, "button_fdn");
            setEnabledForKey(z3 && !z5, "button_voicemail");
            if ((!z3 || z5) && (!z || MiuiPhoneUtils.disableSSOverWfcEnableAirplane())) {
                z4 = false;
            }
            setEnabledForKey(z4, "button_call_barring");
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            DropDownPreference dropDownPreference = this.mT9IndexingMethod;
            if (preference == dropDownPreference) {
                int iFindIndexOfValue = dropDownPreference.findIndexOfValue((String) obj);
                if (iFindIndexOfValue == Settings.System.getInt(getContext().getContentResolver(), "t9_indexing_key", MiuiSettings.System.getT9IndexingKeyDefault())) {
                    return true;
                }
                Settings.System.putInt(getContext().getContentResolver(), "t9_indexing_key", iFindIndexOfValue);
                showRebuildT9IndexDialog(getContext());
                this.mT9IndexingMethod.setValueIndex(iFindIndexOfValue);
                OneTrackImpl.trackEvent("switch_t9_indexing_method");
                return true;
            }
            if (preference == this.mMissedCallNotifyTimes) {
                String str = (String) obj;
                MiuiSettings.Telephony.setMissedCallNotifyTimes(getContext().getContentResolver(), Integer.parseInt(str));
                this.mMissedCallNotifyTimes.setValueIndex(this.mMissedCallNotifyTimes.findIndexOfValue(str));
                OneTrackImpl.trackEvent("switch_missed_call_notify_times");
                return true;
            }
            DropDownPreference dropDownPreference2 = this.mConDisconVibrate;
            if (preference == dropDownPreference2) {
                String str2 = (String) obj;
                int iFindIndexOfValue2 = dropDownPreference2.findIndexOfValue(str2);
                String[] stringArray = getResources().getStringArray(R.array.connect_disconnect_vibrate_entries);
                MiuiSettings.Telephony.setVibrateKey(getContext().getContentResolver(), str2);
                this.mConDisconVibrate.setValue(stringArray[iFindIndexOfValue2]);
                OneTrackImpl.trackEvent("switch_con_discon_vibrate");
                return true;
            }
            if (preference == this.mCdmaDisplayPreciseCallState) {
                if (((Boolean) obj).booleanValue()) {
                    new AlertDialog.Builder(getActivity()).setTitle(R.string.cdma_display_precise_call_state_title).setMessage(R.string.cdma_display_precise_call_state_message).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallAdvancedSetting.CallAdvancedSettingFragment.4
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CallAdvancedSettingFragment.this.mCdmaDisplayPreciseCallState.setChecked(true);
                            Settings.System.putInt(CallAdvancedSettingFragment.this.getContext().getContentResolver(), "cdma_precise_answer_state", 1);
                        }
                    }).show();
                } else {
                    this.mCdmaDisplayPreciseCallState.setChecked(false);
                    Settings.System.putInt(getContext().getContentResolver(), "cdma_precise_answer_state", 0);
                }
                return false;
            }
            DropDownPreference dropDownPreference3 = this.mCallWaitingTone;
            if (preference == dropDownPreference3) {
                int iFindIndexOfValue3 = dropDownPreference3.findIndexOfValue((String) obj);
                this.mCallWaitingTone.setValue(getResources().getStringArray(R.array.call_waiting_tone_entries)[iFindIndexOfValue3]);
                MiuiSettings.Telephony.setCallWaitingTone(getContext(), iFindIndexOfValue3);
                OneTrackImpl.trackEvent("switch_call_waiting_tone");
                return true;
            }
            DropDownPreference dropDownPreference4 = this.mButtonDTMF;
            if (preference == dropDownPreference4) {
                Settings.System.putInt(getContext().getContentResolver(), "dtmf_tone_type", dropDownPreference4.findIndexOfValue((String) obj));
                OneTrackImpl.trackEvent("switch_button_dtmf");
                return true;
            }
            if (preference == this.mButtonTTY) {
                handleTTYChange(preference, obj);
                return true;
            }
            if (preference != this.mButtonRttCall) {
                return true;
            }
            TelephonySettings.setRttOn(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
            return true;
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            Context context = getContext();
            if (context == null) {
                return false;
            }
            if (preference == this.mDialPadTouchTone) {
                Intent intent = new Intent("miui.intent.action.APP_SETTINGS");
                intent.setClassName("com.android.contacts", "com.android.contacts.preference.ContactsPreferenceActivity");
                intent.putExtra(":android:show_fragment", "com.android.contacts.preference.DialpadTouchTonePreferenceFragment");
                intent.putExtra(":android:show_fragment_title", getResources().getString(R.string.preference_dial_pad_touch_tone_title));
                MiuiPhoneUtils.addWindowLable(intent, getResources().getString(R.string.preference_dial_pad_touch_tone_title));
                configureFloatingServiceSafely(intent);
                startActivity(intent);
                OneTrackImpl.trackEvent("enter_dialpad_touch_tone_settings");
                return true;
            }
            if (preference == this.mAutoRedial) {
                MiuiSettings.Telephony.setAutoRedialEnabled(getContext().getContentResolver(), this.mAutoRedial.isChecked());
                OneTrackImpl.trackEvent("switch_auto_redial");
                return true;
            }
            if (preference == this.mButtonAutoRetry) {
                Settings.Global.putInt(getContext().getContentResolver(), "call_auto_retry", this.mButtonAutoRetry.isChecked() ? 1 : 0);
                return true;
            }
            CheckBoxPreference checkBoxPreference = this.mButtonHAC;
            if (preference == checkBoxPreference) {
                boolean zIsChecked = checkBoxPreference.isChecked();
                Settings.System.putInt(getContext().getContentResolver(), "hearing_aid", zIsChecked ? 1 : 0);
                ((AudioManager) getContext().getSystemService("audio")).setParameters(String.format("%s=%s", "HACSetting", zIsChecked ? "ON" : "OFF"));
                return true;
            }
            Preference preference2 = this.mQuickDial;
            if (preference == preference2) {
                Intent intent2 = preference2.getIntent();
                configureFloatingServiceSafely(intent2);
                context.startActivity(intent2);
                OneTrackImpl.trackEvent("enter_quick_dial_settings");
                return true;
            }
            MiuixTextPreference miuixTextPreference = this.mAutoIP;
            if (preference == miuixTextPreference) {
                Intent intent3 = miuixTextPreference.getIntent();
                configureFloatingServiceSafely(intent3);
                context.startActivity(intent3);
                OneTrackImpl.trackEvent("enter_auto_ip_settings");
                return true;
            }
            MiuixTextPreference miuixTextPreference2 = this.mRespondViaSms;
            if (preference == miuixTextPreference2) {
                Intent intent4 = miuixTextPreference2.getIntent();
                configureFloatingServiceSafely(intent4);
                context.startActivity(intent4);
                OneTrackImpl.trackEvent("enter_respond_via_sms_settings");
                return true;
            }
            TitleIconPreference titleIconPreference = this.mCallerId;
            if (preference == titleIconPreference) {
                Intent intent5 = titleIconPreference.getIntent();
                configureFloatingServiceSafely(intent5);
                context.startActivity(intent5);
                OneTrackImpl.trackEvent("enter_caller_id_settings");
                return true;
            }
            Preference preference3 = this.mFdn;
            if (preference == preference3) {
                Intent intent6 = preference3.getIntent();
                configureFloatingServiceSafely(intent6);
                context.startActivity(intent6);
                OneTrackImpl.trackEvent("enter_fdn_settings");
                return true;
            }
            Preference preference4 = this.mVoicemail;
            if (preference != preference4) {
                return false;
            }
            Intent intent7 = preference4.getIntent();
            configureFloatingServiceSafely(intent7);
            context.startActivity(intent7);
            OneTrackImpl.trackEvent("enter_voicemail_settings");
            return true;
        }

        private void initForPhoneType() {
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            boolean z = MiuiPhoneUtils.getCdmaPhone() != null;
            if (TelephonyManager.getDefault().getIccCardCount() <= 1 && z) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_caller_id");
                CallFeaturesSetting.removePreference(preferenceScreen, "button_auto_redial");
                this.mCallerId = null;
            } else if (!z) {
                CallFeaturesSetting.removePreference(preferenceScreen, "button_display_precise_call_state");
                CallFeaturesSetting.removePreference(preferenceScreen, "button_voice_privacy");
            }
            initSimIcon();
        }

        private void initSimIcon() {
            int i;
            int i2;
            Phone cdmaPhone = MiuiPhoneUtils.getCdmaPhone();
            CdmaVoicePrivacyCheckBoxPreference cdmaVoicePrivacyCheckBoxPreference = (CdmaVoicePrivacyCheckBoxPreference) findPreference("button_voice_privacy");
            if (cdmaVoicePrivacyCheckBoxPreference != null && cdmaPhone != null) {
                cdmaVoicePrivacyCheckBoxPreference.init(cdmaPhone);
            }
            if (this.mIccCardCount <= 1 || cdmaPhone == null) {
                i = 0;
            } else {
                i = cdmaPhone.getPhoneId() == 0 ? R.drawable.small_sim_icon_slot_1 : R.drawable.small_sim_icon_slot_2;
            }
            if (cdmaVoicePrivacyCheckBoxPreference != null) {
                cdmaVoicePrivacyCheckBoxPreference.setTitleIconResource(i);
            }
            CheckBoxTitleIconPreference checkBoxTitleIconPreference = this.mCdmaDisplayPreciseCallState;
            if (checkBoxTitleIconPreference != null) {
                checkBoxTitleIconPreference.setTitleIconResource(i);
            }
            if (i != 0) {
                i2 = R.drawable.small_sim_icon_slot_1;
                if (i == i2) {
                    i2 = R.drawable.small_sim_icon_slot_2;
                }
            } else {
                i2 = 0;
            }
            CheckBoxTitleIconPreference checkBoxTitleIconPreference2 = this.mAutoRedial;
            if (checkBoxTitleIconPreference2 != null) {
                checkBoxTitleIconPreference2.setTitleIconResource(i2);
            }
            TitleIconPreference titleIconPreference = this.mCallerId;
            if (titleIconPreference != null) {
                titleIconPreference.setOnPreferenceClickListener(this);
                this.mCallerId.setTitleIconResource(i2);
                if (cdmaPhone != null) {
                    SubscriptionManager.putSlotIdExtra(this.mCallerId.getIntent(), cdmaPhone.getPhoneId() != 0 ? 0 : 1);
                }
            }
        }

        private void setEnabledForKey(boolean z, String str) {
            Preference preferenceFindPreference = getPreferenceScreen().findPreference(str);
            if (preferenceFindPreference != null) {
                preferenceFindPreference.setEnabled(z);
            }
        }

        private void handleTTYChange(Preference preference, Object obj) {
            int iIntValue = Integer.valueOf((String) obj).intValue();
            int i = Settings.Secure.getInt(getContext().getContentResolver(), "preferred_tty_mode", 0);
            if (this.DBG) {
                CallAdvancedSetting.log("handleTTYChange: requesting set TTY mode enable (TTY) to" + Integer.toString(iIntValue));
            }
            if (iIntValue != i) {
                if (iIntValue == 0 || iIntValue == 1 || iIntValue == 2 || iIntValue == 3) {
                    Settings.Secure.putInt(getContext().getContentResolver(), "preferred_tty_mode", iIntValue);
                } else {
                    iIntValue = 0;
                }
                this.mButtonTTY.setValue(Integer.toString(iIntValue));
                updatePreferredTtyModeSummary(iIntValue);
            }
        }

        private void updatePreferredTtyModeSummary(int i) {
            String[] stringArray = getResources().getStringArray(R.array.tty_mode_entries);
            if (i == 0 || i == 1 || i == 2 || i == 3) {
                this.mButtonTTY.setSummary(stringArray[i]);
            } else {
                this.mButtonTTY.setEnabled(false);
                this.mButtonTTY.setSummary(stringArray[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
