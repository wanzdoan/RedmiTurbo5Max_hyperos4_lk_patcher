package com.android.phone.settings;

import android.os.Bundle;
import android.provider.MiuiSettings;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import com.android.phone.R;
import miui.telephony.SubscriptionManager;
import miuix.preference.DropDownPreference;

/* JADX INFO: loaded from: classes.dex */
public class IvrParameterSettings extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!SimPickerPreference.showSimPicker(this) && bundle == null) {
            loadFragment(new IvrParameterSettingsFragment());
        }
    }

    public class IvrParameterSettingsFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener {
        private EditTextPreference mIvrMoAreaCode;
        private DropDownPreference mIvrType;
        private int mSlotId;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.ivr_parameter_settings);
            this.mSlotId = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
            this.mIvrMoAreaCode = (EditTextPreference) findPreference("ivr_mo_area_code");
            this.mIvrType = (DropDownPreference) findPreference("ivr_type");
            this.mIvrMoAreaCode.setOnPreferenceChangeListener(this);
            this.mIvrType.setOnPreferenceChangeListener(this);
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            this.mIvrMoAreaCode.setText(MiuiSettings.Telephony.getIVRParameterValue(getContext().getContentResolver(), getIvrMoAreaCodeKey()));
            this.mIvrType.setValue(MiuiSettings.Telephony.getIVRParameterValue(getContext().getContentResolver(), getIvrTypeKey()));
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            String string = obj.toString();
            if (preference == this.mIvrMoAreaCode) {
                MiuiSettings.Telephony.setIVRParameterValue(getContext().getContentResolver(), getIvrMoAreaCodeKey(), string);
                return true;
            }
            if (preference != this.mIvrType) {
                return true;
            }
            MiuiSettings.Telephony.setIVRParameterValue(getContext().getContentResolver(), getIvrTypeKey(), string);
            return true;
        }

        private String getIvrMoAreaCodeKey() {
            if (this.mSlotId == 0) {
                return "ivr_mo_area_code_slot_1";
            }
            return "ivr_mo_area_code_slot_2";
        }

        private String getIvrTypeKey() {
            if (this.mSlotId == 0) {
                return "ivr_type_slot_1";
            }
            return "ivr_type_slot_2";
        }
    }
}
