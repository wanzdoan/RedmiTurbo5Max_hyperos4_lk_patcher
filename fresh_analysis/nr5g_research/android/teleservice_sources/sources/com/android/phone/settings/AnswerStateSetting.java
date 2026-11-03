package com.android.phone.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MiuiSettings;
import android.telephony.Rlog;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import com.android.phone.BatteryStateManager;
import com.android.phone.FlashManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.utils.Utils;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class AnswerStateSetting extends BaseActivity {
    private boolean DBG;

    public AnswerStateSetting() {
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new AnswerStateSettingFragment());
        }
    }

    public class AnswerStateSettingFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener, BatteryStateManager.BatteryStateListener {
        private CheckBoxPreference mAntiStrange;
        private CheckBoxPreference mCrescendoMode;
        private CheckBoxPreference mFlashWhenRing;
        private CheckBoxPreference mHandonMode;
        private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener;
        private CheckBoxPreference mProximitySensor;
        private CheckBoxPreference mTurnOverMuteMode;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.answer_state_setting);
            if (TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(getContext())) {
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_enable_antispam_strange");
            } else {
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("button_enable_antispam_strange");
                this.mAntiStrange = checkBoxPreference;
                checkBoxPreference.setOnPreferenceChangeListener(this);
            }
            CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) findPreference("button_crescendo_ringer");
            this.mCrescendoMode = checkBoxPreference2;
            checkBoxPreference2.setOnPreferenceChangeListener(this);
            if (MiuiPhoneUtils.isTabletDeviceWithVolteCapability()) {
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_flash_when_ring");
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_turnover_mute");
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_handon_ringer");
                CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_enable_proximity");
            } else {
                if (TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(getContext())) {
                    CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_enable_proximity");
                    CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_enable_antispam_strange");
                    CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_turnover_mute");
                } else {
                    CheckBoxPreference checkBoxPreference3 = (CheckBoxPreference) findPreference("button_turnover_mute");
                    this.mTurnOverMuteMode = checkBoxPreference3;
                    checkBoxPreference3.setOnPreferenceChangeListener(this);
                }
                if (!Utils.isSupportFlashlight(getContext())) {
                    CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_flash_when_ring");
                } else {
                    CheckBoxPreference checkBoxPreference4 = (CheckBoxPreference) findPreference("button_flash_when_ring");
                    this.mFlashWhenRing = checkBoxPreference4;
                    checkBoxPreference4.setOnPreferenceChangeListener(this);
                }
                CheckBoxPreference checkBoxPreference5 = (CheckBoxPreference) findPreference("button_handon_ringer");
                this.mHandonMode = checkBoxPreference5;
                checkBoxPreference5.setOnPreferenceChangeListener(this);
                if ((TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(getContext())) || !MiuiPhoneUtils.isSensorProximitySupported()) {
                    CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_enable_proximity");
                } else {
                    CheckBoxPreference checkBoxPreference6 = (CheckBoxPreference) findPreference("button_enable_proximity");
                    this.mProximitySensor = checkBoxPreference6;
                    checkBoxPreference6.setOnPreferenceChangeListener(this);
                }
            }
            if (MiuiPhoneUtils.isSensorSupported(getContext())) {
                return;
            }
            CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_turnover_mute");
            CallFeaturesSetting.removePreference(getPreferenceScreen(), "button_handon_ringer");
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            if (this.mProximitySensor != null) {
                this.mProximitySensor.setChecked(MiuiSettings.Telephony.isProximitySensorEnable(getContext().getContentResolver()));
            }
            if (this.mFlashWhenRing != null) {
                if (Utils.isBCLStrategyEnable()) {
                    this.mPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.android.phone.settings.AnswerStateSetting.AnswerStateSettingFragment.1
                        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
                        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
                            if ("real_flash_when_ring".equals(str)) {
                                AnswerStateSettingFragment.this.updateFlashUi();
                            }
                        }
                    };
                    PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this.mPreferenceChangeListener);
                    FlashManager.getInstance().registerListener();
                    BatteryStateManager.getInstance().registerBatteryStateListener(this);
                }
                updateFlashUi();
            }
            if (this.mTurnOverMuteMode != null) {
                this.mTurnOverMuteMode.setChecked(MiuiSettings.Telephony.isTurnOverMuteEnabled(getContext().getContentResolver()));
            }
            if (this.mHandonMode != null) {
                this.mHandonMode.setChecked(MiuiSettings.Telephony.isHandonRingerEnabled(getContext().getContentResolver()));
            }
            this.mCrescendoMode.setChecked(MiuiSettings.Telephony.isCrescendoRingerEnable(getContext().getContentResolver()));
            if (this.mAntiStrange != null) {
                this.mAntiStrange.setChecked(MiuiSettings.Telephony.isAntispamStangerEnabled(getContext().getContentResolver()));
            }
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (preference == this.mProximitySensor) {
                MiuiSettings.Telephony.setProximitySensorEnable(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
                return true;
            }
            if (preference == this.mFlashWhenRing) {
                MiuiSettings.System.putBoolean(getContext().getContentResolver(), "flash_when_ring_enabled", ((Boolean) obj).booleanValue());
                return true;
            }
            if (preference == this.mTurnOverMuteMode) {
                MiuiSettings.Telephony.setTurnOverMuteEnabled(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
                return true;
            }
            if (preference == this.mHandonMode) {
                MiuiSettings.Telephony.setHandonRingerEnabled(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
                return true;
            }
            if (preference == this.mCrescendoMode) {
                MiuiSettings.Telephony.setCrescendoRingerEnable(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
                return true;
            }
            if (preference != this.mAntiStrange) {
                return true;
            }
            MiuiSettings.Telephony.setAntispamStangerEnabled(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
            return true;
        }

        @Override // com.android.phone.BatteryStateManager.BatteryStateListener
        public void notifyBatteryStateChanged(int i) {
            if (i < 7) {
                updateFlashUi();
            }
        }

        public void updateFlashUi() {
            int i;
            if (this.mFlashWhenRing == null) {
                return;
            }
            FlashManager flashManager = FlashManager.getInstance();
            boolean zIsFlashEnabled = flashManager.isFlashEnabled();
            Rlog.i("AnswerStateSetting", "isFlashEnabled = " + zIsFlashEnabled);
            this.mFlashWhenRing.setChecked(zIsFlashEnabled ? flashManager.isFlashWhenRingEnabled() : false);
            this.mFlashWhenRing.setEnabled(zIsFlashEnabled);
            if (zIsFlashEnabled) {
                i = R.string.flash_when_ring_summary;
            } else {
                i = R.string.low_battery_flash_when_ring_button_tip;
            }
            this.mFlashWhenRing.setSummary(i);
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            if (Utils.isBCLStrategyEnable()) {
                BatteryStateManager.getInstance().unregisterBatteryStateListener(this);
                FlashManager.getInstance().unregisterListener();
                if (this.mPreferenceChangeListener != null) {
                    PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this.mPreferenceChangeListener);
                    this.mPreferenceChangeListener = null;
                }
            }
        }
    }
}
