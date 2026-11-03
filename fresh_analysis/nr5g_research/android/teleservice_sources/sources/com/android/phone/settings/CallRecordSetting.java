package com.android.phone.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MiuiSettings;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.UIDataLoadTask;
import com.android.phone.custompreferences.MiuixTextPreference;
import java.util.Arrays;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.yellowpage.YellowPageUtils;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.SingleChoicePreference;
import miuix.preference.SingleChoicePreferenceCategory;

/* JADX INFO: loaded from: classes.dex */
public class CallRecordSetting extends BaseActivity {
    private static AlertDialog mCallRecordDialog;
    private boolean DBG;

    public CallRecordSetting() {
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new CallRecordSettingFragment());
        }
    }

    public static boolean isAutoRecordOn(Context context) {
        return MiuiSettings.Telephony.isAutoRecordEnabled(context.getContentResolver());
    }

    public class CallRecordSettingFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener {
        private CheckBoxPreference mAutoRecordSwitcher;
        private PreferenceCategory mCustomCategory;
        private CheckBoxPreference mRecordNotifySwitcher;
        private SingleChoicePreferenceCategory mScenarios;
        private CheckBoxPreference mUnknownNumberRecord;
        private MiuixTextPreference mWhitelist;
        private CheckBoxPreference mYellowpageNumberRecord;

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (preference == this.mAutoRecordSwitcher) {
                Boolean bool = (Boolean) obj;
                if (bool.booleanValue() && Build.IS_INTERNATIONAL_BUILD) {
                    showCallRecordDisclaimerDialog();
                } else {
                    this.mScenarios.setEnabled(bool.booleanValue());
                    updateCustomCategory(bool.booleanValue() && this.mScenarios.getValueIndex() == 1);
                    MiuiSettings.Telephony.setAutoRecordEnabled(getContext().getContentResolver(), bool.booleanValue());
                }
            } else if (preference == this.mRecordNotifySwitcher) {
                MiuiSettings.Telephony.setRecordNotificationEnabled(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
            } else if (preference == this.mUnknownNumberRecord) {
                MiuiSettings.Telephony.setUnknownNumberRecordEnabled(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
            } else if (preference == this.mYellowpageNumberRecord) {
                MiuiSettings.Telephony.setYellowpageRecordEnabled(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
            }
            return true;
        }

        private void showCallRecordDisclaimerDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.call_recording_disclaimer_title);
            builder.setMessage(R.string.call_recording_disclaimer_msg);
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallRecordSetting.CallRecordSettingFragment.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    MiuiSettings.Telephony.setAutoRecordEnabled(CallRecordSettingFragment.this.getContext().getContentResolver(), false);
                    CallRecordSettingFragment.this.mAutoRecordSwitcher.setChecked(MiuiSettings.Telephony.isAutoRecordEnabled(CallRecordSettingFragment.this.getContext().getContentResolver()));
                    CallRecordSettingFragment.this.mScenarios.setEnabled(false);
                }
            });
            builder.setPositiveButton(R.string.start_call_record, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallRecordSetting.CallRecordSettingFragment.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    MiuiSettings.Telephony.setAutoRecordEnabled(CallRecordSettingFragment.this.getContext().getContentResolver(), true);
                    CallRecordSettingFragment.this.mAutoRecordSwitcher.setChecked(MiuiSettings.Telephony.isAutoRecordEnabled(CallRecordSettingFragment.this.getContext().getContentResolver()));
                    CallRecordSettingFragment.this.mScenarios.setEnabled(true);
                    CallRecordSettingFragment callRecordSettingFragment = CallRecordSettingFragment.this;
                    callRecordSettingFragment.updateCustomCategory(callRecordSettingFragment.mScenarios.getValueIndex() == 1 && !MiuiPhoneUtils.isSatelliteEnable(CallRecordSettingFragment.this.getContext()));
                }
            });
            CallRecordSetting.mCallRecordDialog = builder.create();
            CallRecordSetting.mCallRecordDialog.setCanceledOnTouchOutside(false);
            CallRecordSetting.mCallRecordDialog.show();
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            if (CallRecordSetting.mCallRecordDialog != null) {
                CallRecordSetting.mCallRecordDialog.dismiss();
                CallRecordSetting.mCallRecordDialog = null;
            }
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.call_record_setting);
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("button_call_recording_notification");
            this.mRecordNotifySwitcher = checkBoxPreference;
            checkBoxPreference.setOnPreferenceChangeListener(this);
            CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) findPreference("button_auto_record_call");
            this.mAutoRecordSwitcher = checkBoxPreference2;
            checkBoxPreference2.setOnPreferenceChangeListener(this);
            this.mScenarios = (SingleChoicePreferenceCategory) findPreference("button_auto_record_scenario");
            this.mCustomCategory = (PreferenceCategory) findPreference("auto_record_custom_category");
            MiuixTextPreference miuixTextPreference = (MiuixTextPreference) findPreference("button_call_record_auto_whitelist");
            this.mWhitelist = miuixTextPreference;
            miuixTextPreference.setShowRightArrow(true);
            CheckBoxPreference checkBoxPreference3 = (CheckBoxPreference) findPreference("button_auto_record_unknown");
            this.mUnknownNumberRecord = checkBoxPreference3;
            checkBoxPreference3.setOnPreferenceChangeListener(this);
            CheckBoxPreference checkBoxPreference4 = (CheckBoxPreference) findPreference("button_auto_record_yellowpage");
            this.mYellowpageNumberRecord = checkBoxPreference4;
            checkBoxPreference4.setOnPreferenceChangeListener(this);
            createScenarios();
            checkRemovePreferences();
            this.mRecordNotifySwitcher.setChecked(MiuiSettings.Telephony.isRecordNotificationEnabled(getContext().getContentResolver()));
            this.mAutoRecordSwitcher.setChecked(MiuiSettings.Telephony.isAutoRecordEnabled(getContext().getContentResolver()));
            this.mScenarios.setEnabled(this.mAutoRecordSwitcher.isChecked());
            this.mUnknownNumberRecord.setChecked(MiuiSettings.Telephony.isUnknownNumberRecordEnabled(getContext().getContentResolver()));
            this.mYellowpageNumberRecord.setChecked(MiuiSettings.Telephony.isYellowpageRecordEnabled(getContext().getContentResolver()));
            int recordScenario = MiuiSettings.Telephony.getRecordScenario(getContext().getContentResolver());
            if (recordScenario <= -1 || recordScenario >= 2) {
                return;
            }
            Preference preferenceFindPreference = this.mScenarios.findPreference(getResources().getStringArray(R.array.auto_record_scenario_entries)[recordScenario]);
            if (preferenceFindPreference != null) {
                this.mScenarios.setCheckedPreference(preferenceFindPreference);
            }
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            needShowCustomCatogry();
        }

        private void needShowCustomCatogry() {
            boolean z;
            boolean zIsChecked = this.mAutoRecordSwitcher.isChecked();
            SingleChoicePreferenceCategory singleChoicePreferenceCategory = this.mScenarios;
            if (zIsChecked) {
                singleChoicePreferenceCategory.setEnabled(true);
                if (this.mScenarios.getValueIndex() == 1 && !MiuiPhoneUtils.isSatelliteEnable(getContext())) {
                    z = true;
                }
                updateCustomCategory(z);
                if (!PhoneHelperStub.getInstance().isAutoRecordPhoneCall() || MiuiPhoneUtils.isSatelliteEnable(getContext())) {
                    this.mScenarios.setEnabled(false);
                    this.mAutoRecordSwitcher.setEnabled(false);
                    this.mRecordNotifySwitcher.setEnabled(false);
                } else {
                    this.mAutoRecordSwitcher.setEnabled(true);
                    this.mRecordNotifySwitcher.setEnabled(true);
                    return;
                }
            }
            singleChoicePreferenceCategory.setEnabled(false);
            z = false;
            updateCustomCategory(z);
            if (!PhoneHelperStub.getInstance().isAutoRecordPhoneCall()) {
            }
            this.mScenarios.setEnabled(false);
            this.mAutoRecordSwitcher.setEnabled(false);
            this.mRecordNotifySwitcher.setEnabled(false);
        }

        private void createScenarios() {
            final String[] stringArray = getResources().getStringArray(R.array.auto_record_scenario_entries);
            Preference.OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.CallRecordSetting.CallRecordSettingFragment.3
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    SingleChoicePreference singleChoicePreference = (SingleChoicePreference) preference;
                    if (singleChoicePreference.isChecked()) {
                        int iIndexOf = Arrays.asList(stringArray).indexOf(singleChoicePreference.getTitle().toString());
                        MiuiSettings.Telephony.setRecordScenario(CallRecordSettingFragment.this.getContext().getContentResolver(), iIndexOf);
                        CallRecordSettingFragment.this.updateCustomCategory(iIndexOf == 1);
                    }
                    return false;
                }
            };
            for (int i = 0; i < stringArray.length; i++) {
                SingleChoicePreference singleChoicePreference = new SingleChoicePreference(getPreferenceManager().getContext());
                singleChoicePreference.setTitle(stringArray[i]);
                singleChoicePreference.setValue(stringArray[i]);
                singleChoicePreference.setKey(stringArray[i]);
                singleChoicePreference.setOnPreferenceClickListener(onPreferenceClickListener);
                this.mScenarios.addPreference(singleChoicePreference);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateCustomCategory(boolean z) {
            if (z) {
                getPreferenceScreen().addPreference(this.mCustomCategory);
                new UIDataLoadTask(this) { // from class: com.android.phone.settings.CallRecordSetting.CallRecordSettingFragment.4
                    @Override // com.android.phone.UIDataLoadTask
                    protected void doPrepare() {
                    }

                    @Override // com.android.phone.UIDataLoadTask
                    protected Object doTask(Object[] objArr) {
                        return AutoRecordWhiteListSetting.getSummary(PhoneGlobals.getInstance());
                    }

                    @Override // com.android.phone.UIDataLoadTask
                    protected void doUI(Object obj) {
                        CallRecordSettingFragment.this.mWhitelist.setValue((String) obj);
                    }
                }.execute(new Object[0]);
            } else {
                getPreferenceScreen().removePreference(this.mCustomCategory);
                this.mWhitelist.setValue((String) null);
            }
        }

        private void checkRemovePreferences() {
            if (YellowPageUtils.isYellowPageAvailable(getContext())) {
                return;
            }
            this.mCustomCategory.removePreference(findPreference("button_auto_record_yellowpage"));
        }
    }
}
