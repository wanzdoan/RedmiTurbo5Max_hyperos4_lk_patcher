package com.android.phone.settings;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.preference.Preference;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.WifiCallHelpActivity;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.Arrays;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.ActionBar;
import miuix.preference.RadioButtonPreference;
import miuix.preference.RadioButtonPreferenceCategory;

/* JADX INFO: loaded from: classes.dex */
public class PreferredWifiCallListPreference extends BaseActivity {
    private PreferredWifiCallListPreferenceFragment mPreferredWifiCallListPreferenceFragment;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PreferredWifiCallListPreferenceFragment preferredWifiCallListPreferenceFragment = (PreferredWifiCallListPreferenceFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        this.mPreferredWifiCallListPreferenceFragment = preferredWifiCallListPreferenceFragment;
        if (bundle == null && preferredWifiCallListPreferenceFragment == null) {
            PreferredWifiCallListPreferenceFragment preferredWifiCallListPreferenceFragment2 = new PreferredWifiCallListPreferenceFragment();
            this.mPreferredWifiCallListPreferenceFragment = preferredWifiCallListPreferenceFragment2;
            loadFragment(preferredWifiCallListPreferenceFragment2);
        }
        getWindow().addFlags(134217728);
    }

    @Override // com.android.phone.settings.BaseActivity
    protected void setActionBar() {
        super.setActionBar();
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setTitle(R.string.preferred_wificall_title);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!MiuiImsPhoneUtils.isCustForOperatorVoWifiFeature(MiuiPhoneUtils.getPhone(this.mPreferredWifiCallListPreferenceFragment.mPhoneId))) {
            return false;
        }
        menu.add(0, 1, 0, R.string.wifi_call_help);
        return true;
    }

    @Override // com.android.phone.settings.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 1) {
            Intent intent = new Intent(this, (Class<?>) WifiCallHelpActivity.class);
            intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, this.mPreferredWifiCallListPreferenceFragment.mPhoneId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class PreferredWifiCallListPreferenceFragment extends BaseActivity.BaseFragment {
        private boolean mIsRoaming;
        private int mPhoneId;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            this.mPhoneId = SubscriptionManager.getPhoneIdExtra(getActivity().getIntent(), 0);
            this.mIsRoaming = getActivity().getIntent().getBooleanExtra("wifi_call_roaming_bool", false);
            addPreferencesFromResource(R.xml.preferred_wificall);
            initPreferredWifiCall();
        }

        private void initPreferredWifiCall() {
            final String[] stringArray;
            final int[] intArray;
            RadioButtonPreferenceCategory radioButtonPreferenceCategory = (RadioButtonPreferenceCategory) findPreference("preferred_wificall_key");
            Resources resourcesForSubId = android.telephony.SubscriptionManager.getResourcesForSubId(PhoneGlobals.getInstance(), MiuiPhoneUtils.getPhone(this.mPhoneId).getSubId());
            if (ImsAdapterBase.getInstance().isImsPreferredSupported(MiuiPhoneUtils.getPhone(this.mPhoneId).getSubId())) {
                stringArray = resourcesForSubId.getStringArray(R.array.wificall_with_ims_preferred_entries);
                intArray = resourcesForSubId.getIntArray(R.array.wificall_with_ims_preferred_mode_values);
            } else {
                stringArray = resourcesForSubId.getStringArray(R.array.wificall_preferred_entries);
                intArray = resourcesForSubId.getIntArray(R.array.wificall_preferred_mode_values);
            }
            Preference.OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.PreferredWifiCallListPreference.PreferredWifiCallListPreferenceFragment.1
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    RadioButtonPreference radioButtonPreference = (RadioButtonPreference) preference;
                    if (!radioButtonPreference.isChecked()) {
                        return false;
                    }
                    int iIndexOf = Arrays.asList(stringArray).indexOf(radioButtonPreference.getTitle().toString());
                    if (iIndexOf == PreferredWifiCallListPreferenceFragment.this.getIndexOf(intArray, ImsAdapterBase.getInstance().getWfcMode(PreferredWifiCallListPreferenceFragment.this.getContext(), PreferredWifiCallListPreferenceFragment.this.mPhoneId, PreferredWifiCallListPreferenceFragment.this.mIsRoaming))) {
                        return false;
                    }
                    ImsAdapterBase.getInstance().setWfcMode(PreferredWifiCallListPreferenceFragment.this.getContext(), PreferredWifiCallListPreferenceFragment.this.mPhoneId, intArray[iIndexOf], PreferredWifiCallListPreferenceFragment.this.mIsRoaming);
                    if (PreferredWifiCallListPreferenceFragment.this.mIsRoaming || !ImsAdapterBase.getInstance().isUseWfcHomeNetworkinRoaming(PreferredWifiCallListPreferenceFragment.this.getContext(), MiuiPhoneUtils.getPhone(PreferredWifiCallListPreferenceFragment.this.mPhoneId).getSubId())) {
                        return false;
                    }
                    ImsAdapterBase.getInstance().setWfcMode(PreferredWifiCallListPreferenceFragment.this.getContext(), PreferredWifiCallListPreferenceFragment.this.mPhoneId, intArray[iIndexOf], true);
                    return false;
                }
            };
            for (String str : stringArray) {
                RadioButtonPreference radioButtonPreference = new RadioButtonPreference(getPreferenceManager().getContext(), null);
                radioButtonPreference.setTitle(str);
                radioButtonPreference.setOnPreferenceClickListener(onPreferenceClickListener);
                radioButtonPreferenceCategory.addPreference(radioButtonPreference);
            }
            radioButtonPreferenceCategory.setCheckedPosition(getIndexOf(intArray, ImsAdapterBase.getInstance().getWfcMode(getContext(), this.mPhoneId, this.mIsRoaming)));
            if (MiuiImsPhoneUtils.isCustForOperatorVoWifiFeature(MiuiPhoneUtils.getPhone(this.mPhoneId))) {
                addPreferencesFromResource(R.xml.wifi_call_preferred_summary);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getIndexOf(int[] iArr, int i) {
            for (int i2 = 0; i2 < iArr.length; i2++) {
                if (i == iArr[i2]) {
                    return i2;
                }
            }
            return 0;
        }
    }
}
