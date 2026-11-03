package com.android.phone.settings;

import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import java.util.ArrayList;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class GsmUmtsAdditionalCallOptions extends TimeConsumingPreferenceActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.TimeConsumingPreferenceActivity, com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new GsmUmtsAdditionalCallOptionsFragment());
        }
    }

    public class GsmUmtsAdditionalCallOptionsFragment extends TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment {
        private boolean DBG;
        private CLIRListPreference mCLIRButton;
        private CallWaitingCheckBoxPreference mCWButton;
        private int mInitIndex;
        private final ArrayList mPreferences;
        private int mSubscription;

        public GsmUmtsAdditionalCallOptionsFragment() {
            this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
            this.mPreferences = new ArrayList();
            this.mInitIndex = 0;
            this.mSubscription = 0;
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            super.onCreatePreferences(bundle, str);
            addPreferencesFromResource(R.xml.miui_gsm_umts_additional_options);
            this.mSubscription = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
            if (this.DBG) {
                Log.d("GsmUmtsAdditionalCallOptions", "GsmUmtsAdditionalCallOptions onCreate, subscription: " + this.mSubscription);
            }
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mCLIRButton = (CLIRListPreference) preferenceScreen.findPreference("button_clir_key");
            this.mCWButton = (CallWaitingCheckBoxPreference) preferenceScreen.findPreference("button_cw_key");
            this.mPreferences.add(this.mCLIRButton);
            this.mPreferences.add(this.mCWButton);
            boolean z = this.DBG;
            if (bundle == null) {
                if (z) {
                    Log.d("GsmUmtsAdditionalCallOptions", "start to init ");
                }
                this.mCLIRButton.init(this, false, this.mSubscription);
                return;
            }
            if (z) {
                Log.d("GsmUmtsAdditionalCallOptions", "restore stored states");
            }
            this.mInitIndex = this.mPreferences.size();
            this.mCLIRButton.init(this, true, this.mSubscription);
            this.mCWButton.init(this, true, this.mSubscription);
            int[] intArray = bundle.getIntArray(this.mCLIRButton.getKey());
            if (intArray != null) {
                if (this.DBG) {
                    Log.d("GsmUmtsAdditionalCallOptions", "onCreate:  clirArray[0]=" + intArray[0] + ", clirArray[1]=" + intArray[1]);
                }
                this.mCLIRButton.handleGetCLIRResult(intArray);
                return;
            }
            this.mCLIRButton.init(this, false, this.mSubscription);
        }

        @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
        public void onSaveInstanceState(Bundle bundle) {
            super.onSaveInstanceState(bundle);
            CLIRListPreference cLIRListPreference = this.mCLIRButton;
            if (cLIRListPreference.clirArray != null) {
                bundle.putIntArray(cLIRListPreference.getKey(), this.mCLIRButton.clirArray);
            }
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.TimeConsumingPreferenceListener
        public void onFinished(Preference preference, boolean z) {
            if (this.mInitIndex < this.mPreferences.size() - 1 && getActivity() != null && !getActivity().isFinishing()) {
                int i = this.mInitIndex + 1;
                this.mInitIndex = i;
                Preference preference2 = (Preference) this.mPreferences.get(i);
                if (preference2 instanceof CallWaitingCheckBoxPreference) {
                    ((CallWaitingCheckBoxPreference) preference2).init(this, false, this.mSubscription);
                }
            }
            super.onFinished(preference, z);
        }
    }
}
