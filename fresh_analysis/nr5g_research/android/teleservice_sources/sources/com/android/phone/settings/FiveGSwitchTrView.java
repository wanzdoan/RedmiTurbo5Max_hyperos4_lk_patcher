package com.android.phone.settings;

import android.content.res.Resources;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.internal.telephony.Phone;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class FiveGSwitchTrView {
    public static final String TAG = "FiveGSwitchTrView";
    private CheckBoxPreference mButtonFiveG;
    private Phone mPhone;
    private PreferenceGroup mPrefGroup;
    private PreferenceFragment mPreferenceFragment;

    public FiveGSwitchTrView(PreferenceFragment preferenceFragment, PreferenceGroup preferenceGroup, Phone phone) {
        this.mPreferenceFragment = preferenceFragment;
        this.mPrefGroup = preferenceGroup;
        this.mPhone = phone;
        initPreferenceUI();
    }

    private void initPreferenceUI() {
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mPrefGroup.findPreference("button_fiveg_key");
        this.mButtonFiveG = checkBoxPreference;
        checkBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.FiveGSwitchTrView.1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (FiveGSwitchTrView.this.mPreferenceFragment == null) {
                    Log.d(FiveGSwitchTrView.TAG, "mPrefActivity == null");
                    return false;
                }
                boolean zIsChecked = FiveGSwitchTrView.this.mButtonFiveG.isChecked();
                if (MiuiPhoneUtils.isDuringCall()) {
                    Log.d(FiveGSwitchTrView.TAG, "isInCall, cannot switch mButton5gEnabled");
                    FiveGSwitchTrView.this.mButtonFiveG.setChecked(!zIsChecked);
                    Toast.makeText(FiveGSwitchTrView.this.mPhone.getContext(), R.string.msim_set_sub_not_supported_phone_in_call, 0).show();
                    return false;
                }
                FiveGManagerBase.getInstance().setUserFiveGEnabled(zIsChecked, FiveGSwitchTrView.this.mPhone.getPhoneId());
                return true;
            }
        });
        if (this.mButtonFiveG != null) {
            Resources resourcesForSubId = SubscriptionManager.getResourcesForSubId(PhoneGlobals.getInstance().getApplicationContext(), this.mPhone.getSubId());
            this.mButtonFiveG.setTitle(resourcesForSubId.getString(R.string.fiveg_switch_per_slot_title));
            this.mButtonFiveG.setSummary(resourcesForSubId.getString(R.string.fiveg_switch_per_slot_summary));
        }
    }

    public void updateFiveGUI() {
        if (this.mButtonFiveG == null || this.mPreferenceFragment == null) {
            return;
        }
        if (this.mPrefGroup.findPreference("button_fiveg_key") == null) {
            this.mPrefGroup.addPreference(this.mButtonFiveG);
        }
        Log.d(TAG, "updateFiveGUI: " + isSupporFiveG());
        this.mButtonFiveG.setChecked(isSupporFiveG());
    }

    private boolean isSupporFiveG() {
        return FiveGManagerBase.getInstance().isUserFiveGEnabled(this.mPhone.getPhoneId());
    }

    public void destroy() {
        this.mPreferenceFragment = null;
    }
}
