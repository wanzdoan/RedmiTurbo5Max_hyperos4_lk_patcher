package com.android.phone.settings;

import android.content.Context;
import android.os.Bundle;
import android.provider.MiuiSettings;
import android.util.Log;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import java.util.Arrays;
import miui.enterprise.PhoneHelperStub;
import miuix.preference.DropDownPreference;
import miuix.preference.SingleChoicePreference;
import miuix.preference.SingleChoicePreferenceCategory;

/* JADX INFO: loaded from: classes.dex */
public class AutoAnswerSetting extends BaseActivity {
    private boolean DBG;

    public AutoAnswerSetting() {
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.DBG) {
            Log.d("AutoAnswerSetting", "Creating activity");
        }
        if (bundle == null) {
            loadFragment(new AutoAnswerSettingFragment());
        }
    }

    public class AutoAnswerSettingFragment extends BaseActivity.BaseFragment {
        private Context mContext;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            this.mContext = getContext().getApplicationContext();
            addPreferencesFromResource(R.xml.auto_answer_setting);
            createScenarios();
            Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() { // from class: com.android.phone.settings.AutoAnswerSetting.AutoAnswerSettingFragment.1
                @Override // androidx.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    Boolean bool = (Boolean) obj;
                    AutoAnswerSettingFragment.this.findPreference("button_auto_answer_delay").setEnabled(bool.booleanValue());
                    AutoAnswerSettingFragment.this.findPreference("button_auto_answer_scenario").setEnabled(bool.booleanValue());
                    MiuiSettings.Telephony.setAutoAnswerCall(AutoAnswerSettingFragment.this.mContext.getContentResolver(), bool.booleanValue());
                    return true;
                }
            };
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("button_auto_answer");
            checkBoxPreference.setChecked(MiuiSettings.Telephony.isAutoAnswerCall(this.mContext.getContentResolver()));
            checkBoxPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
            onPreferenceChangeListener.onPreferenceChange(checkBoxPreference, Boolean.valueOf(checkBoxPreference.isChecked()));
            if (PhoneHelperStub.getInstance().isAutoRecordPhoneCall()) {
                checkBoxPreference.setEnabled(false);
            }
            DropDownPreference dropDownPreference = (DropDownPreference) findPreference("button_auto_answer_delay");
            dropDownPreference.setEntries(new CharSequence[]{getResources().getString(R.string.auto_answer_delay_entries_immediately), getResources().getString(R.string.auto_answer_delay_entries_delay_3s, 3), getResources().getString(R.string.auto_answer_delay_entries_delay_5s, 5), getResources().getString(R.string.auto_answer_delay_entries_delay_10s, 10)});
            dropDownPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.android.phone.settings.AutoAnswerSetting.AutoAnswerSettingFragment.2
                @Override // androidx.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object obj) {
                    MiuiSettings.Telephony.setAutoAnswerCallDelayTime(AutoAnswerSettingFragment.this.mContext.getContentResolver(), Integer.valueOf((String) obj).intValue());
                    return true;
                }
            });
            dropDownPreference.setValue(String.valueOf(MiuiSettings.Telephony.getAutoAnswerCallDelayTime(this.mContext.getContentResolver())));
        }

        private void createScenarios() {
            SingleChoicePreferenceCategory singleChoicePreferenceCategory = (SingleChoicePreferenceCategory) findPreference("button_auto_answer_scenario");
            final String[] stringArray = getResources().getStringArray(R.array.auto_answer_scenario_entries);
            Preference.OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.AutoAnswerSetting.AutoAnswerSettingFragment.3
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    SingleChoicePreference singleChoicePreference = (SingleChoicePreference) preference;
                    if (!singleChoicePreference.isChecked()) {
                        return false;
                    }
                    MiuiSettings.Telephony.setAutoAnswerCallScenario(AutoAnswerSettingFragment.this.mContext.getContentResolver(), Arrays.asList(stringArray).indexOf(singleChoicePreference.getTitle().toString()));
                    return false;
                }
            };
            int autoAnswerCallScenario = MiuiSettings.Telephony.getAutoAnswerCallScenario(this.mContext.getContentResolver());
            for (int i = 0; i < stringArray.length; i++) {
                SingleChoicePreference singleChoicePreference = new SingleChoicePreference(getPreferenceManager().getContext(), null);
                singleChoicePreference.setTitle(stringArray[i]);
                singleChoicePreference.setValue(stringArray[i]);
                singleChoicePreference.setOnPreferenceClickListener(onPreferenceClickListener);
                singleChoicePreferenceCategory.addPreference(singleChoicePreference);
            }
            singleChoicePreferenceCategory.setCheckedPreference(singleChoicePreferenceCategory.getPreference(autoAnswerCallScenario));
        }
    }
}
