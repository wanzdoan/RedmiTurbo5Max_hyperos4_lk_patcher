package com.android.phone.settings;

import android.os.Bundle;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class GsmUmtsCallerIdSetting extends TimeConsumingPreferenceActivity {
    private static boolean DBG;

    static {
        DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.TimeConsumingPreferenceActivity, com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SimPickerPreference.showSimPicker(this)) {
            return;
        }
        if (DBG) {
            Log.d("GsmUmtsCallerIdSetting", "Creating activity");
        }
        if (bundle == null) {
            loadFragment(new GsmUmtsCallerIdSettingFragment());
        }
    }

    public class GsmUmtsCallerIdSettingFragment extends TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment {
        private CLIRListPreference mCLIRButton;
        private Phone mPhone;

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            boolean zIsChileClaroSim;
            super.onCreatePreferences(bundle, str);
            addPreferencesFromResource(R.xml.gsm_umts_caller_id_setting);
            this.mPhone = MiuiPhoneUtils.getPhone(SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultDataSlotId()));
            this.mCLIRButton = (CLIRListPreference) findPreference("button_clir");
            if (TelephonyManager.isCustFeatureResolveEnabled()) {
                zIsChileClaroSim = HyperOSCustFeatureResolve.getBoolean("set_clir_values_default_only", false);
            } else {
                zIsChileClaroSim = MiuiPhoneUtils.isChileClaroSim(this.mPhone);
            }
            this.mCLIRButton.setEntries(zIsChileClaroSim ? R.array.clir_choices_default_only : R.array.clir_choices);
            this.mCLIRButton.setEntryValues(zIsChileClaroSim ? R.array.clir_values_default_only : R.array.clir_values);
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            this.mCLIRButton.setEnabled(false);
            if (MiuiPhoneUtils.isSimReady(this.mPhone.getPhoneId()) && !this.mPhone.getIccCard().getIccFdnEnabled() && this.mPhone.getPhoneType() == 1) {
                if ((MiuiPhoneUtils.isCmccSim(this.mPhone) && MiuiPhoneUtils.isSSOperatedByUt(this.mPhone)) || CarrierConfigManagerEx.getBooleanConfigForSubId(this.mPhone.getContext(), this.mPhone.getSubId(), "config_disable_caller_id", false)) {
                    this.mCLIRButton.setEnabled(false);
                    this.mCLIRButton.setSummary(R.string.lable_network_not_supported);
                    Log.i("GsmUmtsCallerIdSetting", "In CMCC VoLTE network or ID Telkomsel CLIR is not available!");
                } else {
                    this.mCLIRButton.setEnabled(true);
                    this.mCLIRButton.init(this, false, this.mPhone.getPhoneId());
                }
            }
        }
    }
}
