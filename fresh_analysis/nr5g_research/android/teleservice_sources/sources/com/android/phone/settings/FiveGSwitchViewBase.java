package com.android.phone.settings;

import android.util.Log;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.internal.telephony.Phone;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public abstract class FiveGSwitchViewBase implements Preference.OnPreferenceClickListener {
    public static final String TAG = FiveGSwitchView.class.getSimpleName();
    protected CheckBoxPreference mButton5gEnabled;
    protected Phone mPhone;
    protected PreferenceGroup mPrefGroup;
    protected PreferenceFragment mPreferenceFragment;

    protected abstract boolean performPreferenceClick(Preference preference);

    public FiveGSwitchViewBase(PreferenceFragment preferenceFragment, PreferenceGroup preferenceGroup, Phone phone) {
        this.mPreferenceFragment = preferenceFragment;
        this.mPrefGroup = preferenceGroup;
        this.mPhone = phone;
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preferenceGroup.findPreference("sim" + phone.getPhoneId() + "_fiveg_switch");
        this.mButton5gEnabled = checkBoxPreference;
        checkBoxPreference.setOnPreferenceClickListener(this);
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        if (this.mPreferenceFragment == null) {
            Log.d(TAG, "preference activity is null.");
            return false;
        }
        performPreferenceClick(preference);
        return true;
    }
}
