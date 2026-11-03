package com.android.phone.settings;

import android.util.Log;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.internal.telephony.Phone;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class FiveGSwitchView extends FiveGSwitchViewBase {
    public FiveGSwitchView(PreferenceFragment preferenceFragment, PreferenceGroup preferenceGroup, Phone phone) {
        super(preferenceFragment, preferenceGroup, phone);
    }

    public void updateSwitchButton() {
        if (this.mButton5gEnabled != null) {
            this.mButton5gEnabled.setChecked(FiveGManagerBase.getInstance().isUserFiveGEnabled(this.mPhone.getPhoneId()));
        }
    }

    @Override // com.android.phone.settings.FiveGSwitchViewBase
    protected boolean performPreferenceClick(Preference preference) {
        CheckBoxPreference checkBoxPreference = this.mButton5gEnabled;
        if (checkBoxPreference != null && checkBoxPreference == preference) {
            boolean zIsChecked = checkBoxPreference.isChecked();
            if (MiuiPhoneUtils.isDuringCall()) {
                Log.d(FiveGSwitchViewBase.TAG, "isInCall, cannot switch mButton5gEnabled");
                this.mButton5gEnabled.setChecked(!zIsChecked);
                Toast.makeText(this.mPhone.getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                return false;
            }
            Log.d(FiveGSwitchViewBase.TAG, "enable5g requested: " + zIsChecked + ", phoneId = " + this.mPhone.getPhoneId());
            FiveGManagerBase.getInstance().setUserFiveGEnabled(zIsChecked, this.mPhone.getPhoneId());
        }
        return true;
    }
}
