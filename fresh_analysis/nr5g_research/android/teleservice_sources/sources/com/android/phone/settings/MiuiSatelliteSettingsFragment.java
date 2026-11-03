package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ISatelliteModule;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSatelliteSettingsFragment extends BaseActivity.BaseFragment {
    public static final String TAG = MiuiSatelliteSettings.class.getSimpleName();
    private Preference mBeiDouExplainPref;
    private CheckBoxPreference mButtonBeiDouEnabled;
    private CheckBoxPreference mButtonTianTongEnabled;
    private CheckBoxPreference mButtonTianTongSignalBoostEnabled;
    private CheckBoxPreference mButtonXingwangSatelliteEnabled;
    private ContentObserver mContentObserver;
    private IntentFilter mIntentFilter;
    private PreferenceCategory mPrefBeiDou;
    private PreferenceCategory mPrefTianTong;
    private PreferenceCategory mPrefXingwangSatellite;
    private PreferenceScreen mPreferenceScreen;
    private Preference mTianTongBusinessPref;
    private Preference mTianTongExplainPref;
    private Preference mTianTongPrivacyWithdrawalPref;
    private Preference mXingwangSatelliteExplainPref;
    private Preference mXingwangSatellitePrivacyWithdrawalPref;
    private List mSimInfoRecordList = new ArrayList();
    private final SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.1
        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            if ("agree_satellite_privacy_statement".equals(str)) {
                MiuiSatelliteSettingsFragment.this.updateSatellitePrivacyWithdrawalEnabled();
            }
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String str = MiuiSatelliteSettingsFragment.TAG;
            Log.i(str, "onReceive action " + action);
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                MiuiSatelliteSettingsFragment.this.updateSatelliteEnableButton();
                MiuiSatelliteSettingsFragment.this.updateXingWangSatelliteEnableButton();
                MiuiSatelliteSettingsFragment.this.updateBeiDouUI();
            } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                String stringExtra = intent.getStringExtra("ss");
                Log.d(str, "Received ACTION_SIM_STATE_CHANGED: " + stringExtra);
                intent.getIntExtra("phone", SubscriptionManager.INVALID_SLOT_ID);
                if ("LOADED".equals(stringExtra) || "ABSENT".equals(stringExtra)) {
                    MiuiSatelliteSettingsFragment.this.updateSatelliteEnableButton();
                    MiuiSatelliteSettingsFragment.this.updateXingWangSatelliteEnableButton();
                    MiuiSatelliteSettingsFragment.this.updateSatelliteBusinessDisplay();
                    MiuiSatelliteSettingsFragment.this.updateBeiDouUI();
                }
            }
        }
    };

    @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        addPreferencesFromResource(R.xml.miui_satellite_settings);
        init();
        Log.d(TAG, "onCreatePreferences");
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        init();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        addTianTongPreference();
        addXingwangSateLlitePreference();
        addBeiDouPreference();
        updateSatelliteEnableButton();
        updateXingWangSatelliteEnableButton();
        updateSatelliteBusinessDisplay();
        updateSatelliteSignalBoostButton();
        updateSatellitePrivacyWithdrawalEnabled();
        updateBeiDouUI();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this.mPreferenceChangeListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this.mPreferenceChangeListener);
    }

    @Override // miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(this.mReceiver);
        getContext().getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    private void init() {
        this.mPreferenceScreen = getPreferenceScreen();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        this.mIntentFilter = intentFilter;
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        getContext().registerReceiver(this.mReceiver, this.mIntentFilter, 2);
        this.mContentObserver = new ContentObserver(new Handler(getContext().getMainLooper())) { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                if (MiuiSatelliteSettingsFragment.this.getContext() == null) {
                    return;
                }
                if (MiuiSatelliteSettingsFragment.this.mButtonTianTongEnabled != null) {
                    MiuiSatelliteSettingsFragment.this.mButtonTianTongEnabled.setChecked(Settings.System.getInt(MiuiSatelliteSettingsFragment.this.getContext().getContentResolver(), "satellite_state", 0) == 1);
                }
                if (MiuiSatelliteSettingsFragment.this.mButtonTianTongSignalBoostEnabled != null) {
                    MiuiSatelliteSettingsFragment.this.mButtonTianTongSignalBoostEnabled.setChecked(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteSignalBoostEnabled());
                }
            }
        };
    }

    private void addTianTongPreference() {
        Log.d(TAG, "addTianTongPreference");
        if (this.mPrefTianTong != null) {
            return;
        }
        addPreferencesFromResource(R.xml.miui_tiantong_setting);
        this.mPrefTianTong = (PreferenceCategory) this.mPreferenceScreen.findPreference("tiantong_satellite");
        if (!MiuiPhoneUtils.shouldShowSatelliteButton(getContext())) {
            this.mPreferenceScreen.removePreference(this.mPrefTianTong);
            return;
        }
        getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor("satellite_state"), true, this.mContentObserver);
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mPrefTianTong.findPreference("button_tiantong_enabled_key");
        this.mButtonTianTongEnabled = checkBoxPreference;
        checkBoxPreference.setEnabled(MiuiPhoneUtils.isSatelliteButtonEditable(getContext()));
        this.mButtonTianTongEnabled.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.4
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                boolean zIsSatelliteEnabled = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteEnabled();
                ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSatelliteEnableByUser(MiuiSatelliteSettingsFragment.this.getContext(), !zIsSatelliteEnabled, 0);
                boolean zIsSatelliteEnabled2 = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteEnabled();
                MiuiSatelliteSettingsFragment.this.mButtonTianTongEnabled.setChecked(zIsSatelliteEnabled2);
                Log.d(MiuiSatelliteSettingsFragment.TAG, "satellite state old = " + zIsSatelliteEnabled + ", new = " + zIsSatelliteEnabled2);
                return true;
            }
        });
        this.mButtonTianTongSignalBoostEnabled = (CheckBoxPreference) this.mPrefTianTong.findPreference("button_tiantong_signal_boost_enabled_key");
        if (!MiuiPhoneUtils.shouldShowSatelliteSignalBoostButton()) {
            this.mPrefTianTong.removePreference(this.mButtonTianTongSignalBoostEnabled);
            this.mButtonTianTongSignalBoostEnabled = null;
        } else {
            this.mButtonTianTongSignalBoostEnabled.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.5
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    boolean zIsSatelliteSignalBoostEnabled = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteSignalBoostEnabled();
                    if (!zIsSatelliteSignalBoostEnabled) {
                        Toast.makeText(MiuiSatelliteSettingsFragment.this.getContext(), R.string.satellite_signal_boost_toast, 1).show();
                    }
                    ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSatelliteSignalBoostEnable(!zIsSatelliteSignalBoostEnabled, 0);
                    MiuiSatelliteSettingsFragment.this.mButtonTianTongSignalBoostEnabled.setChecked(!zIsSatelliteSignalBoostEnabled);
                    String str = MiuiSatelliteSettingsFragment.TAG;
                    StringBuilder sb = new StringBuilder("satellite signal boost set state= ");
                    sb.append(!zIsSatelliteSignalBoostEnabled);
                    Log.d(str, sb.toString());
                    return true;
                }
            });
        }
        Preference preferenceFindPreference = this.mPrefTianTong.findPreference("tiantong_explain_key");
        this.mTianTongExplainPref = preferenceFindPreference;
        if (preferenceFindPreference != null) {
            preferenceFindPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.6
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    Log.i(MiuiSatelliteSettingsFragment.TAG, "mSatelliteExplainPref ");
                    if (MiuiPhoneUtils.isWifiConnected() || MiuiPhoneUtils.isMobileDataConnected()) {
                        Locale locale = Locale.getDefault();
                        StringBuffer stringBuffer = new StringBuffer("https://satellite.mi.com/sos/info.html?lang=");
                        if (locale != null) {
                            stringBuffer.append(locale.toString().replace("#", ""));
                        }
                        MiuiSatelliteSettingsFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString())));
                        return true;
                    }
                    Intent intent = new Intent();
                    intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.SatelliteIntroductionActivity");
                    MiuiSatelliteSettingsFragment.this.startActivity(intent);
                    return true;
                }
            });
        }
        this.mTianTongBusinessPref = this.mPrefTianTong.findPreference("tiantong_ct_business_key");
        if (!MiuiPhoneUtils.shouldShowSatelliteBusinessEntrance()) {
            this.mPrefTianTong.removePreference(this.mTianTongBusinessPref);
        }
        Preference preference = this.mTianTongBusinessPref;
        if (preference != null) {
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.7
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference2) {
                    Log.i(MiuiSatelliteSettingsFragment.TAG, "mTianTongBusinessPref ");
                    MiuiSatelliteSettingsFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://satellite.mi.com/sos/redirect.html")));
                    ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).sendSatelliteBusinessEntranceDot();
                    return true;
                }
            });
        }
        Preference preferenceFindPreference2 = this.mPrefTianTong.findPreference("tiantong_privacy_withdrawal_key");
        this.mTianTongPrivacyWithdrawalPref = preferenceFindPreference2;
        if (preferenceFindPreference2 != null) {
            preferenceFindPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.8
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference2) {
                    Log.i(MiuiSatelliteSettingsFragment.TAG, "Satellite privacy statement withdrawn!");
                    Intent intent = new Intent();
                    intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.satellite.SatelliteDialogActivity");
                    intent.setFlags(402653184);
                    intent.putExtra("type", 7);
                    MiuiSatelliteSettingsFragment.this.startActivity(intent);
                    return true;
                }
            });
        }
    }

    private void addXingwangSateLlitePreference() {
        Log.d(TAG, "addXingwangSateLlitePreference");
        if (this.mPrefXingwangSatellite != null) {
            return;
        }
        addPreferencesFromResource(R.xml.miui_satellite_xingwang_settings);
        this.mPrefXingwangSatellite = (PreferenceCategory) this.mPreferenceScreen.findPreference("xingwang_satellite");
        if (!MiuiPhoneUtils.isSupportSatelliteXingwangByDevice(getContext()) || !SystemProperties.getBoolean("radio.satellite.xingwang", false)) {
            this.mPreferenceScreen.removePreference(this.mPrefXingwangSatellite);
            return;
        }
        getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor("xingwang_satellite_state"), true, this.mContentObserver);
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mPreferenceScreen.findPreference("xingwang_satellite_enabled_key");
        this.mButtonXingwangSatelliteEnabled = checkBoxPreference;
        checkBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.9
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                boolean zIsSatelliteXingwangEnabled = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteXingwangEnabled();
                ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSatelliteXingwangEnableByUser(MiuiSatelliteSettingsFragment.this.getContext(), !zIsSatelliteXingwangEnabled, 0);
                boolean zIsSatelliteXingwangEnabled2 = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteXingwangEnabled();
                MiuiSatelliteSettingsFragment.this.mButtonXingwangSatelliteEnabled.setChecked(zIsSatelliteXingwangEnabled2);
                Log.d(MiuiSatelliteSettingsFragment.TAG, "xingwang satellite state old = " + zIsSatelliteXingwangEnabled + ", new = " + zIsSatelliteXingwangEnabled2);
                return true;
            }
        });
        Preference preferenceFindPreference = this.mPreferenceScreen.findPreference("xingwang_satellite_explain_key");
        this.mXingwangSatelliteExplainPref = preferenceFindPreference;
        if (preferenceFindPreference != null) {
            preferenceFindPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.10
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    if (MiuiPhoneUtils.isWifiConnected() || MiuiPhoneUtils.isMobileDataConnected()) {
                        MiuiSatelliteSettingsFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://satellite.mi.com/sos/info.html")));
                        return true;
                    }
                    Intent intent = new Intent();
                    intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.SatelliteIntroductionActivity");
                    MiuiSatelliteSettingsFragment.this.startActivity(intent);
                    return true;
                }
            });
        }
        Preference preferenceFindPreference2 = this.mPreferenceScreen.findPreference("xingwang_satellite_privacy_withdrawal_key");
        this.mXingwangSatellitePrivacyWithdrawalPref = preferenceFindPreference2;
        if (preferenceFindPreference2 != null) {
            preferenceFindPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.11
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    Log.i(MiuiSatelliteSettingsFragment.TAG, "Satellite privacy statement withdrawn!");
                    Intent intent = new Intent();
                    intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.satellite.SatelliteDialogActivity");
                    intent.setFlags(402653184);
                    intent.putExtra("type", 7);
                    MiuiSatelliteSettingsFragment.this.startActivity(intent);
                    return true;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSatelliteEnableButton() {
        CheckBoxPreference checkBoxPreference;
        if (getContext() == null || (checkBoxPreference = this.mButtonTianTongEnabled) == null) {
            return;
        }
        checkBoxPreference.setEnabled(MiuiPhoneUtils.isSatelliteButtonEditable(getContext()));
        this.mButtonTianTongEnabled.setChecked(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteEnabled());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateXingWangSatelliteEnableButton() {
        CheckBoxPreference checkBoxPreference;
        if (getContext() == null || (checkBoxPreference = this.mButtonXingwangSatelliteEnabled) == null) {
            return;
        }
        checkBoxPreference.setChecked(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteXingwangEnabled());
    }

    private void updateSatelliteSignalBoostButton() {
        CheckBoxPreference checkBoxPreference;
        if (getContext() == null || (checkBoxPreference = this.mButtonTianTongSignalBoostEnabled) == null) {
            return;
        }
        checkBoxPreference.setEnabled(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteEnabled());
        this.mButtonTianTongSignalBoostEnabled.setChecked(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteSignalBoostEnabled());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSatelliteBusinessDisplay() {
        if (this.mPrefTianTong == null || this.mTianTongBusinessPref == null) {
            return;
        }
        boolean zShouldShowSatelliteBusinessEntrance = MiuiPhoneUtils.shouldShowSatelliteBusinessEntrance();
        PreferenceCategory preferenceCategory = this.mPrefTianTong;
        if (zShouldShowSatelliteBusinessEntrance) {
            preferenceCategory.addPreference(this.mTianTongBusinessPref);
        } else {
            preferenceCategory.removePreference(this.mTianTongBusinessPref);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSatellitePrivacyWithdrawalEnabled() {
        if (this.mTianTongPrivacyWithdrawalPref == null) {
            return;
        }
        boolean zIsPrivacyStatementAgree = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isPrivacyStatementAgree();
        Preference preference = this.mTianTongPrivacyWithdrawalPref;
        if (!zIsPrivacyStatementAgree) {
            preference.setEnabled(false);
        } else {
            preference.setEnabled(true);
        }
    }

    private void addBeiDouPreference() {
        int i;
        Log.d(TAG, "addBeiDouPreference");
        if (this.mPrefBeiDou != null) {
            return;
        }
        addPreferencesFromResource(R.xml.miui_beidou_setting);
        this.mPrefBeiDou = (PreferenceCategory) this.mPreferenceScreen.findPreference("beidou_satellite");
        if (!MiuiPhoneUtils.shouldShowBeiDouButton(getContext())) {
            this.mPreferenceScreen.removePreference(this.mPrefBeiDou);
            return;
        }
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mPrefBeiDou.findPreference("button_beidou_enabled_key");
        this.mButtonBeiDouEnabled = checkBoxPreference;
        if (checkBoxPreference != null) {
            checkBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.12
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    boolean bdSwitchState = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getBdSwitchState();
                    ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setBeiDouEnableByUser(MiuiSatelliteSettingsFragment.this.getContext(), !bdSwitchState);
                    boolean bdSwitchState2 = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getBdSwitchState();
                    MiuiSatelliteSettingsFragment.this.mButtonBeiDouEnabled.setChecked(bdSwitchState2);
                    Log.d(MiuiSatelliteSettingsFragment.TAG, "beidou newState = " + bdSwitchState2 + ", oldState = " + bdSwitchState);
                    return true;
                }
            });
        }
        if (this.mButtonBeiDouEnabled != null) {
            if (MiuiPhoneUtils.isOnlySupportCmccBeiDou()) {
                i = R.string.beidou_mobile_settings_summary;
            } else {
                i = R.string.beidou_mobile_settings_summary_all;
            }
            this.mButtonBeiDouEnabled.setSummary(i);
        }
        Preference preferenceFindPreference = this.mPrefBeiDou.findPreference("bd_explain_key");
        this.mBeiDouExplainPref = preferenceFindPreference;
        if (preferenceFindPreference != null) {
            preferenceFindPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.MiuiSatelliteSettingsFragment.13
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    String str;
                    Log.i(MiuiSatelliteSettingsFragment.TAG, "mBeiDouExplainPref ");
                    if (MiuiPhoneUtils.isWifiConnected() || MiuiPhoneUtils.isMobileDataConnected()) {
                        Locale locale = Locale.getDefault();
                        if (MiuiPhoneUtils.isOnlySupportCmccBeiDou()) {
                            str = "https://embed.sec.miui.com/beidou-satellite-info?lang=";
                        } else {
                            str = "https://embed.sec.miui.com/beidou-satellite-info-operator?lang=";
                        }
                        StringBuffer stringBuffer = new StringBuffer(str);
                        if (locale != null) {
                            stringBuffer.append(locale.toString().replace("#", ""));
                        }
                        MiuiSatelliteSettingsFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString())));
                        return true;
                    }
                    Intent intent = new Intent();
                    intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.beidou.BeiDouIntroductionActivity");
                    MiuiSatelliteSettingsFragment.this.startActivity(intent);
                    return true;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBeiDouUI() {
        if (this.mButtonBeiDouEnabled == null || getContext() == null) {
            return;
        }
        Iterator it = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList().iterator();
        boolean z = false;
        boolean zIsCmccSim = false;
        boolean zIsCnOperatorSim = false;
        while (it.hasNext()) {
            Phone phone = MiuiPhoneUtils.getPhone(((SubscriptionInfo) it.next()).getSlotId());
            zIsCmccSim |= MiuiPhoneUtils.isCmccSim(phone);
            zIsCnOperatorSim |= MiuiPhoneUtils.isCnOperatorSim(phone);
        }
        boolean z2 = Settings.System.getInt(getContext().getContentResolver(), "airplane_mode_on", -1) == 1;
        Log.d(TAG, "updateBeiDouUI: isAirPlaneEnabled = " + z2 + ", hasCmccCard = " + zIsCmccSim + ", hasCnCard = " + zIsCnOperatorSim + ", switchState = " + ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getBdSwitchState());
        this.mButtonBeiDouEnabled.setChecked(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getBdSwitchState());
        if (!z2 && (!MiuiPhoneUtils.isOnlySupportCmccBeiDou() ? zIsCnOperatorSim : zIsCmccSim)) {
            z = true;
        }
        this.mButtonBeiDouEnabled.setEnabled(z);
    }
}
