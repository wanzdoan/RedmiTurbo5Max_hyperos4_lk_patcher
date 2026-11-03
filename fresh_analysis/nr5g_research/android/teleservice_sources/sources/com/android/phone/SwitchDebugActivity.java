package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.phone.custompreferences.MiuixTextPreference;
import com.android.phone.settings.BaseActivity;
import com.android.phone.utils.FiveGUtils;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class SwitchDebugActivity extends BaseActivity {
    private static final String TAG = "SwitchDebugActivity";
    private static TelephonyManager mTm;

    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "onCreate");
        mTm = (TelephonyManager) getSystemService("phone");
        if (bundle == null) {
            loadFragment(new SwitchDebugActivityFragment());
        }
    }

    public class SwitchDebugActivityFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceClickListener {
        private static final int PHONE_COUNT = miui.telephony.TelephonyManager.getDefault().getPhoneCount();
        int defaultDataSlotId;
        private CheckBoxPreference mButtonDSDA;
        private MiuixTextPreference mButtonFiveGInfo;
        private CheckBoxPreference mButtonFivegpowerSwitch;
        private CheckBoxPreference mButtonN28;
        private MiuixTextPreference mButtonNrca;
        private MiuixTextPreference mButtonOpconfig;
        private MiuixTextPreference mButtonSa;
        private CheckBoxPreference mButtonUplmn;
        private CheckBoxPreference mButtonVoicesa;
        private CheckBoxPreference mButtonVolte;
        private CheckBoxPreference mButtonVonr;
        private CheckBoxPreference mButtonVowifi;
        private Context mContext;
        private PreferenceCategory mPrefGeneral;
        private PreferenceCategory mPrefOptimization;
        private PreferenceCategory mPrefOther;
        private PreferenceScreen mPreferenceScreen;
        private final BroadcastReceiver mViceSaReceiver = new BroadcastReceiver() { // from class: com.android.phone.SwitchDebugActivity.SwitchDebugActivityFragment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(SwitchDebugActivity.TAG, "action = " + action);
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    Log.d(SwitchDebugActivity.TAG, "ACTION_AIRPLANE_MODE_CHANGED");
                    SwitchDebugActivityFragment.this.updatevoicesaStates();
                    SwitchDebugActivityFragment.this.updateNrcaStates();
                } else if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                    int intExtra = intent.getIntExtra("phone", SubscriptionManager.INVALID_PHONE_ID);
                    Log.d(SwitchDebugActivity.TAG, "ACTION_CARRIER_CONFIG_CHANGED phoneId: " + intExtra);
                    if (intExtra != SubscriptionManager.getDefault().getDefaultDataSlotId()) {
                        SwitchDebugActivityFragment.this.updatevoicesaStates();
                    }
                    SwitchDebugActivityFragment.this.updateNrcaStates();
                }
            }
        };

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            this.mContext = getContext();
            this.defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            addPreferencesFromResource(R.xml.switch_debug_menu);
            initUI();
        }

        private void initUI() {
            PreferenceCategory preferenceCategory;
            MiuixTextPreference miuixTextPreference;
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mPreferenceScreen = preferenceScreen;
            this.mPrefGeneral = (PreferenceCategory) preferenceScreen.findPreference("general_class");
            this.mPrefOptimization = (PreferenceCategory) this.mPreferenceScreen.findPreference("optimization_calss");
            this.mPrefOther = (PreferenceCategory) this.mPreferenceScreen.findPreference("other_calss");
            this.mButtonVolte = (CheckBoxPreference) this.mPreferenceScreen.findPreference("volte_switch");
            this.mButtonVonr = (CheckBoxPreference) this.mPreferenceScreen.findPreference("vonr_switch");
            this.mButtonVowifi = (CheckBoxPreference) this.mPreferenceScreen.findPreference("vowifi_switch");
            this.mButtonVoicesa = (CheckBoxPreference) this.mPreferenceScreen.findPreference("voice_sa_switch");
            this.mButtonSa = (MiuixTextPreference) this.mPreferenceScreen.findPreference("show_sa_switch");
            if (TelephonyManagerEx.getDefault().isNrCaSupported()) {
                this.mButtonNrca = (MiuixTextPreference) this.mPreferenceScreen.findPreference("nrca_switch");
            }
            this.mButtonFivegpowerSwitch = (CheckBoxPreference) this.mPreferenceScreen.findPreference("fiveg_power_switch");
            this.mButtonN28 = (CheckBoxPreference) this.mPreferenceScreen.findPreference("n28_switch");
            this.mButtonDSDA = (CheckBoxPreference) this.mPreferenceScreen.findPreference("dsda_switch");
            this.mButtonFiveGInfo = (MiuixTextPreference) this.mPreferenceScreen.findPreference("fiveg_info");
            this.mButtonOpconfig = (MiuixTextPreference) this.mPreferenceScreen.findPreference("opconfig");
            this.mButtonUplmn = (CheckBoxPreference) this.mPreferenceScreen.findPreference("uplmn_switch");
            if (!Build.isDebuggable()) {
                this.mPrefGeneral.removePreference(this.mButtonVolte);
                this.mButtonVolte = null;
                this.mPrefGeneral.removePreference(this.mButtonVowifi);
                this.mButtonVowifi = null;
                this.mPrefGeneral.removePreference(this.mButtonSa);
                this.mButtonSa = null;
                MiuixTextPreference miuixTextPreference2 = this.mButtonNrca;
                if (miuixTextPreference2 != null) {
                    this.mPrefGeneral.removePreference(miuixTextPreference2);
                    this.mButtonNrca = null;
                }
                this.mPrefOptimization.removePreference(this.mButtonFivegpowerSwitch);
                this.mButtonFivegpowerSwitch = null;
                this.mPrefOptimization.removePreference(this.mButtonDSDA);
                this.mButtonDSDA = null;
                this.mPrefOther.removePreference(this.mButtonFiveGInfo);
                this.mButtonFiveGInfo = null;
                this.mPrefOther.removePreference(this.mButtonOpconfig);
                this.mButtonOpconfig = null;
                this.mPrefOther.removePreference(this.mButtonUplmn);
                this.mButtonUplmn = null;
            }
            if (this.mButtonDSDA != null && !MiuiPhoneUtils.isDSDASupport()) {
                this.mPrefOptimization.removePreference(this.mButtonDSDA);
                this.mButtonDSDA = null;
            }
            if (!isViceSaAvailable()) {
                this.mPrefGeneral.removePreference(this.mButtonVoicesa);
            }
            if (!MiuiPhoneUtils.isModemFeatureSupported(1)) {
                this.mPrefOptimization.removePreference(this.mButtonN28);
                this.mButtonN28 = null;
            }
            if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && (preferenceCategory = this.mPrefOther) != null && (miuixTextPreference = this.mButtonFiveGInfo) != null) {
                preferenceCategory.removePreference(miuixTextPreference);
                this.mButtonFiveGInfo = null;
            }
            PreferenceCategory preferenceCategory2 = this.mPrefOther;
            if (preferenceCategory2 != null && preferenceCategory2.getPreferenceCount() == 0) {
                this.mPreferenceScreen.removePreference(this.mPrefOther);
            }
            PreferenceCategory preferenceCategory3 = this.mPrefOptimization;
            if (preferenceCategory3 == null || preferenceCategory3.getPreferenceCount() != 0) {
                return;
            }
            this.mPreferenceScreen.removePreference(this.mPrefOptimization);
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            Log.d(SwitchDebugActivity.TAG, "onResume. Start updatePreference");
            if (isViceSaAvailable()) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
                intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
                this.mContext.registerReceiver(this.mViceSaReceiver, intentFilter, 2);
            }
            updatevoicesaStates();
            updateNrcaStates();
            updatePreference();
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            Log.d(SwitchDebugActivity.TAG, "onDestroy");
            if (isViceSaAvailable()) {
                this.mContext.unregisterReceiver(this.mViceSaReceiver);
            }
        }

        private static int othersSim() {
            for (int i = 0; i < PHONE_COUNT; i++) {
                if (i != SubscriptionManager.getDefault().getDefaultDataSlotId()) {
                    return i;
                }
            }
            return -1;
        }

        private boolean isViceSimActived() {
            if (!miui.telephony.TelephonyManager.getDefault().hasIccCard(othersSim())) {
                return false;
            }
            try {
                return MiuiPhoneInterfaceManager.getInstance().isIccCardActivate(othersSim());
            } catch (Exception unused) {
                return false;
            }
        }

        public static boolean isViceSaAvailable() {
            return miui.telephony.TelephonyManager.getDefault().isDualSaSupported() && !miui.os.Build.IS_INTERNATIONAL_BUILD;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatevoicesaStates() {
            this.mButtonVoicesa.setOnPreferenceClickListener(this);
            int iOthersSim = othersSim();
            boolean z = false;
            if (iOthersSim == -1) {
                this.mButtonVoicesa.setChecked(false);
                this.mButtonVoicesa.setEnabled(false);
                return;
            }
            boolean zIsDualSaEnabled = MiuiPhoneInterfaceManager.getInstance().isDualSaEnabled();
            this.mButtonVoicesa.setChecked(zIsDualSaEnabled);
            boolean zIsViceSimActived = isViceSimActived();
            boolean zHasIccCard = miui.telephony.TelephonyManager.getDefault().hasIccCard(iOthersSim);
            boolean z2 = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
            boolean z3 = !z2 && zHasIccCard && zIsViceSimActived;
            boolean zIsUserFiveGSaEnabled = miui.telephony.TelephonyManager.getDefault().isUserFiveGSaEnabled(SubscriptionManager.getDefault().getDefaultDataSlotId());
            boolean zIsUserFiveGEnabled = miui.telephony.TelephonyManager.getDefault().isUserFiveGEnabled();
            boolean zIsDualNrEnabled = miui.telephony.TelephonyManager.getDefault().isDualNrEnabled();
            boolean z4 = zIsUserFiveGEnabled && zIsUserFiveGSaEnabled && zIsDualNrEnabled;
            CheckBoxPreference checkBoxPreference = this.mButtonVoicesa;
            if (z3 && z4) {
                z = true;
            }
            checkBoxPreference.setEnabled(z);
            Log.d(SwitchDebugActivity.TAG, "updateState slotId: " + iOthersSim + ", check: " + zIsDualSaEnabled + ", isViceSimActived: " + zIsViceSimActived + ", isSimInsert: " + zHasIccCard + ", airplaneEnable: " + z2 + ", enableOptions: " + z3 + ", isUserFiveGEnabled: " + zIsUserFiveGEnabled + ", isUserFiveGSaEnabled: " + zIsUserFiveGSaEnabled + ", isUserViceFiveGEnabled: " + zIsDualNrEnabled + ", isDefaultSim: " + SubscriptionManager.getDefault().getDefaultDataSlotId());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateNrcaStates() {
            if (this.mButtonNrca == null) {
                return;
            }
            boolean zHasIccCard = miui.telephony.TelephonyManager.getDefault().hasIccCard();
            boolean z = false;
            boolean z2 = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
            MiuixTextPreference miuixTextPreference = this.mButtonNrca;
            if (zHasIccCard && !z2) {
                z = true;
            }
            miuixTextPreference.setEnabled(z);
        }

        private void updatePreference() {
            CheckBoxPreference checkBoxPreference = this.mButtonVolte;
            if (checkBoxPreference != null) {
                checkBoxPreference.setOnPreferenceClickListener(this);
                this.mButtonVolte.setChecked(CarrierConfigLoaderInjector.isVolteForceEnabled(this.mContext));
            }
            CheckBoxPreference checkBoxPreference2 = this.mButtonVonr;
            if (checkBoxPreference2 != null) {
                checkBoxPreference2.setOnPreferenceClickListener(this);
                this.mButtonVonr.setChecked(FiveGUtils.isVonrSwitchVisiable(DefaultSimManager.getDefaultDataSlotId()));
            }
            CheckBoxPreference checkBoxPreference3 = this.mButtonVowifi;
            if (checkBoxPreference3 != null) {
                checkBoxPreference3.setOnPreferenceClickListener(this);
                this.mButtonVowifi.setChecked(CarrierConfigLoaderInjector.isVoWifiForceEnabled(this.mContext));
            }
            MiuixTextPreference miuixTextPreference = this.mButtonSa;
            if (miuixTextPreference != null) {
                miuixTextPreference.setOnPreferenceClickListener(this);
            }
            CheckBoxPreference checkBoxPreference4 = this.mButtonFivegpowerSwitch;
            if (checkBoxPreference4 != null) {
                checkBoxPreference4.setOnPreferenceClickListener(this);
                this.mButtonFivegpowerSwitch.setChecked(Settings.Global.getInt(this.mContext.getContentResolver(), "fiveg_power_saving_visible", 0) == 1);
            }
            CheckBoxPreference checkBoxPreference5 = this.mButtonDSDA;
            if (checkBoxPreference5 != null) {
                checkBoxPreference5.setOnPreferenceClickListener(this);
                int dSDAStateBySetting = FiveGManagerBase.getInstance().getDSDAStateBySetting();
                this.mButtonDSDA.setChecked(dSDAStateBySetting == -1 || dSDAStateBySetting == 1);
            }
            MiuixTextPreference miuixTextPreference2 = this.mButtonOpconfig;
            if (miuixTextPreference2 != null) {
                miuixTextPreference2.setOnPreferenceClickListener(this);
            }
            CheckBoxPreference checkBoxPreference6 = this.mButtonN28;
            if (checkBoxPreference6 != null) {
                checkBoxPreference6.setOnPreferenceClickListener(this);
                int modemFeatureFromDb = MiuiPhoneUtils.getModemFeatureFromDb(this.mContext, 1);
                Log.d(SwitchDebugActivity.TAG, "updatePreference: updateN28Modevalue from DB =" + modemFeatureFromDb);
                this.mButtonN28.setChecked(modemFeatureFromDb == 1);
            }
            CheckBoxPreference checkBoxPreference7 = this.mButtonUplmn;
            if (checkBoxPreference7 != null) {
                checkBoxPreference7.setOnPreferenceClickListener(this);
                this.mButtonUplmn.setChecked(CarrierConfigLoaderInjector.isShowUPlmnPreference(this.mContext));
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            if (preference == null) {
                Log.d(SwitchDebugActivity.TAG, "mPreferenceFragment == null");
                return false;
            }
            String key = preference.getKey();
            key.hashCode();
            switch (key) {
                case "dsda_switch":
                    SwitchDebugActivity.mTm.sendDialerSpecialCode("543732");
                    return true;
                case "volte_switch":
                    SwitchDebugActivity.mTm.sendDialerSpecialCode("86583");
                    return true;
                case "vowifi_switch":
                    SwitchDebugActivity.mTm.sendDialerSpecialCode("869434");
                    return true;
                case "opconfig":
                    SwitchDebugActivity.mTm.sendDialerSpecialCode("67234");
                    return true;
                case "fiveg_power_switch":
                    SwitchDebugActivity.mTm.sendDialerSpecialCode("5476937");
                    return true;
                case "uplmn_switch":
                    SwitchDebugActivity.mTm.sendDialerSpecialCode("46361");
                    return true;
                case "voice_sa_switch":
                    FiveGManagerBase.getInstance().setDualSaEnabled(this.mButtonVoicesa.isChecked());
                    return true;
                case "vonr_switch":
                    SwitchDebugActivity.mTm.sendDialerSpecialCode("8667");
                    return true;
                case "show_sa_switch":
                    SwitchDebugActivity.mTm.sendDialerSpecialCode("726633");
                    int i = Settings.System.getInt(this.mContext.getContentResolver(), "5g_network_mode_selection_visiable", -1);
                    if (i == 0) {
                        this.mButtonSa.setValue(this.mContext.getResources().getString(R.string.according_to_carrier));
                    } else {
                        MiuixTextPreference miuixTextPreference = this.mButtonSa;
                        if (i == 1) {
                            miuixTextPreference.setValue(this.mContext.getResources().getString(R.string.forced_to_hide));
                        } else {
                            miuixTextPreference.setValue(this.mContext.getResources().getString(R.string.forced_to_display));
                        }
                    }
                    return true;
                case "n28_switch":
                    boolean zIsChecked = this.mButtonN28.isChecked();
                    Log.d(SwitchDebugActivity.TAG, "onPreferenceClick: n28 mode = " + (zIsChecked ? 1 : 0));
                    FiveGManagerBase.getInstance().setModemFeatureMode(1, zIsChecked ? 1 : 0);
                    return true;
                default:
                    return true;
            }
        }
    }
}
