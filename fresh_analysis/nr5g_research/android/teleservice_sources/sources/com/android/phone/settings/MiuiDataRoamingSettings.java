package com.android.phone.settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDataRoamingSettings extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new MiuiDataRoamingSettingsFragment());
        }
    }

    public class MiuiDataRoamingSettingsFragment extends BaseActivity.BaseFragment {
        private Preference mPreferenceRoamingCustomerService;
        private Preference mPreferenceRoamingUserPackage;
        private PreferenceScreen mPreferenceScreen;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.miui_roaming_select);
            initUI();
        }

        private void initUI() {
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mPreferenceScreen = preferenceScreen;
            this.mPreferenceRoamingUserPackage = preferenceScreen.findPreference("roaming_user_package");
            this.mPreferenceRoamingCustomerService = this.mPreferenceScreen.findPreference("roaming_customer_service");
            if (MiuiKoreaOperatorCustimizedUtils.isCustForKrKt()) {
                this.mPreferenceRoamingUserPackage.setSummary((CharSequence) null);
                this.mPreferenceRoamingCustomerService.setSummary((CharSequence) null);
            }
            if (getActivity() == null) {
                return;
            }
            if (MiuiKoreaOperatorCustimizedUtils.isCustForKrSkt()) {
                getActivity().setTitle(R.string.international_roaming_setting_kr_skt);
            } else {
                getActivity().setTitle(R.string.international_roaming_setting_kr);
            }
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference == this.mPreferenceRoamingUserPackage) {
                MiuiKoreaOperatorCustimizedUtils.showRoamingPackage(getContext());
                return true;
            }
            if (preference != this.mPreferenceRoamingCustomerService) {
                return false;
            }
            if (MiuiKoreaOperatorCustimizedUtils.isCustForKrKt()) {
                popupRoamingCustomerServiceDialog();
            } else {
                MiuiKoreaOperatorCustimizedUtils.dialCustomerService(getContext());
            }
            return true;
        }

        private void popupRoamingCustomerServiceDialog() {
            Intent errorDialogActivityIntent = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getErrorDialogActivityIntent();
            errorDialogActivityIntent.setFlags(268435456);
            errorDialogActivityIntent.putExtra("dialog_type", 11);
            startActivity(errorDialogActivityIntent);
        }
    }
}
