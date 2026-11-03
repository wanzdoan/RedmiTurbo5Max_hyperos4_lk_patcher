package com.android.phone.settings;

import android.os.Bundle;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import miui.enterprise.PhoneHelperStub;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class GsmUmtsCallWaitingSetting extends TimeConsumingPreferenceActivity {
    private boolean DBG;

    public GsmUmtsCallWaitingSetting() {
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.TimeConsumingPreferenceActivity, com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SimPickerPreference.showSimPicker(this)) {
            return;
        }
        if (this.DBG) {
            Log.d("GsmUmtsCallWaitingSetting", "Creating activity");
        }
        if (bundle == null) {
            loadFragment(new GsmUmtsCallWaitingSettingFragment());
        }
    }

    public class GsmUmtsCallWaitingSettingFragment extends TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment {
        private CallWaitingCheckBoxPreference mCWButton;
        private boolean mFirstResume;
        private boolean mHasShowSuppServiceDialog = false;
        private Phone mPhone;

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            super.onCreatePreferences(bundle, str);
            this.mPhone = MiuiPhoneUtils.getPhone(SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId()));
            addPreferencesFromResource(R.xml.gsm_umts_call_waiting_setting);
            this.mCWButton = (CallWaitingCheckBoxPreference) findPreference("button_cw");
            this.mFirstResume = true;
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            if (this.mFirstResume) {
                if (this.mHasShowSuppServiceDialog && getActivity() != null) {
                    getActivity().finish();
                    return;
                }
                if (MiuiPhoneUtils.maybeShowDialogForSuppService(this.mPhone, 2)) {
                    this.mHasShowSuppServiceDialog = true;
                    return;
                }
                this.mCWButton.setEnabled(false);
                if (PhoneHelperStub.getInstance().isCallOutWaitingRestriction()) {
                    this.mCWButton.setChecked(false);
                    return;
                }
                if (MiuiPhoneUtils.isSimReady(this.mPhone.getPhoneId()) && !this.mPhone.getIccCard().getIccFdnEnabled()) {
                    this.mCWButton.setEnabled(true);
                    this.mCWButton.init(this, false, this.mPhone.getPhoneId());
                }
                this.mFirstResume = false;
            }
        }
    }
}
