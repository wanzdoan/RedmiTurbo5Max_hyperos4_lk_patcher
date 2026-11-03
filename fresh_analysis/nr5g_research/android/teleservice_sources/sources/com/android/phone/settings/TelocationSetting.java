package com.android.phone.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.utils.Utils;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.TelephonyManager;
import miui.telephony.phonenumber.CountryCodeConverter;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class TelocationSetting extends BaseActivity {
    private boolean DBG;
    private TelocationSettingFragment mTelocationSettingFragment;

    public TelocationSetting() {
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.DBG) {
            log("Creating activity");
        }
        TelocationSettingFragment telocationSettingFragment = (TelocationSettingFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        this.mTelocationSettingFragment = telocationSettingFragment;
        if (bundle == null && telocationSettingFragment == null) {
            TelocationSettingFragment telocationSettingFragment2 = new TelocationSettingFragment();
            this.mTelocationSettingFragment = telocationSettingFragment2;
            loadFragment(telocationSettingFragment2);
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        if (i == 100) {
            return new AlertDialog.Builder(this).setTitle(R.string.telocation_country_code_enabled_title).setMessage(R.string.telocation_country_code_enabled_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.TelocationSetting.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    boolean zIsEmpty = TextUtils.isEmpty(MiuiSettings.Telephony.getContactCountrycode(TelocationSetting.this.getContentResolver()));
                    TelocationSetting telocationSetting = TelocationSetting.this;
                    if (zIsEmpty) {
                        telocationSetting.mTelocationSettingFragment.mIsAutoAddCountryCode = true;
                        TelocationSetting.this.mTelocationSettingFragment.simulatePreferenceClick(TelocationSetting.this.mTelocationSettingFragment.mTextCountryCode);
                    } else {
                        MiuiSettings.Telephony.setAutoCountryCodeEnable(telocationSetting.getContentResolver(), true);
                        TelocationSetting.this.mTelocationSettingFragment.mAutoCountryCode.setChecked(true);
                    }
                }
            }).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).create();
        }
        return super.onCreateDialog(i);
    }

    @Override // com.android.phone.settings.BaseActivity
    protected void setActionBar() {
        super.setActionBar();
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setDisplayHomeAsUpEnabled(true);
            if (TelephonyManager.isRemoveCallerloc()) {
                appCompatActionBar.setTitle(getResources().getString(R.string.preference_countrycode_title));
                setTitle(R.string.preference_countrycode_title);
            }
        }
    }

    private static void log(String str) {
        Log.d("TelocationSetting", str);
    }

    public class TelocationSettingFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
        private CheckBoxPreference mAutoCountryCode;
        private String mCountryCode = "";
        private boolean mIsAutoAddCountryCode;
        private Phone mPhone;
        private CheckBoxPreference mShowLocationEnable;
        private PreferenceCategory mTelocationCategory;
        private LTREditTextPreference mTextCountryCode;

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (preference == this.mShowLocationEnable) {
                Settings.System.putInt(this.mPhone.getContext().getContentResolver(), "enable_telocation", ((Boolean) obj).booleanValue() ? 1 : 0);
            } else if (preference == this.mAutoCountryCode) {
                MiuiSettings.Telephony.setAutoCountryCodeEnable(getContext().getContentResolver(), ((Boolean) obj).booleanValue());
            } else if (preference == this.mTextCountryCode) {
                final String strSubstring = (String) obj;
                if (strSubstring.length() > 0) {
                    if (strSubstring.startsWith("+")) {
                        strSubstring = strSubstring.substring(1);
                    } else if (strSubstring.startsWith("00")) {
                        strSubstring = strSubstring.substring(2);
                    }
                    if (TextUtils.isDigitsOnly(strSubstring)) {
                        if (!PhoneNumberUtils.PhoneNumber.isValidCountryCode(strSubstring)) {
                            new AlertDialog.Builder(getActivity()).setTitle(R.string.telocation_countrycode_alert_title).setMessage(R.string.telocation_countrycode_alert_message_warning).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.TelocationSetting.TelocationSettingFragment.2
                                @Override // android.content.DialogInterface.OnClickListener
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    TelocationSettingFragment.this.setCountryCode(strSubstring);
                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.TelocationSetting.TelocationSettingFragment.1
                                @Override // android.content.DialogInterface.OnClickListener
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    TelocationSettingFragment telocationSettingFragment = TelocationSettingFragment.this;
                                    telocationSettingFragment.simulatePreferenceClick(telocationSettingFragment.mTextCountryCode);
                                }
                            }).show();
                        } else {
                            setCountryCode(strSubstring);
                        }
                    } else {
                        new AlertDialog.Builder(getActivity()).setTitle(R.string.telocation_countrycode_alert_title).setMessage(R.string.telocation_countrycode_alert_message_wrong).setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.TelocationSetting.TelocationSettingFragment.3
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TelocationSettingFragment telocationSettingFragment = TelocationSettingFragment.this;
                                telocationSettingFragment.simulatePreferenceClick(telocationSettingFragment.mTextCountryCode);
                            }
                        }).show();
                    }
                } else {
                    setCountryCode(null);
                }
            }
            return true;
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            this.mPhone = PhoneFactory.getDefaultPhone();
            addPreferencesFromResource(R.xml.location_setting);
            this.mTelocationCategory = (PreferenceCategory) findPreference("telocation_category");
            this.mShowLocationEnable = (CheckBoxPreference) findPreference("button_enable_telocation");
            this.mAutoCountryCode = (CheckBoxPreference) findPreference("button_auto_country_code");
            LTREditTextPreference lTREditTextPreference = (LTREditTextPreference) findPreference("button_text_telocation_contacts_countrycode");
            this.mTextCountryCode = lTREditTextPreference;
            lTREditTextPreference.setSummaryLTR(false);
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                boolean booleanExtra = intent.getBooleanExtra("automatic_add_country_code", false);
                if (booleanExtra) {
                    getContext().getSharedPreferences("voice_roaming_reminder_interval", 0).edit().putBoolean("has_notification", false).commit();
                }
                if (booleanExtra && !MiuiSettings.Telephony.isAutoCountryCodeEnable(getContext().getContentResolver())) {
                    getActivity().showDialog(100);
                }
            }
            if (HyperOSCustFeatureResolve.getBoolean("support_device_auto_roaming", false)) {
                this.mAutoCountryCode.setSummary(R.string.telocation_auto_country_code_summary_while_roaming);
            }
            if (TelephonyManager.isRemoveCallerloc()) {
                getPreferenceScreen().removePreference(this.mTelocationCategory);
            }
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            this.mShowLocationEnable.setOnPreferenceChangeListener(this);
            this.mShowLocationEnable.setChecked(Settings.System.getInt(getContext().getContentResolver(), "enable_telocation", 1) != 0);
            this.mAutoCountryCode.setOnPreferenceChangeListener(this);
            this.mAutoCountryCode.setChecked(MiuiSettings.Telephony.isAutoCountryCodeEnable(getContext().getContentResolver()));
            this.mTextCountryCode.setOnPreferenceChangeListener(this);
            this.mTextCountryCode.setOnPreferenceClickListener(this);
            String contactCountrycode = MiuiSettings.Telephony.getContactCountrycode(getContext().getContentResolver());
            this.mCountryCode = contactCountrycode;
            if (TextUtils.isEmpty(contactCountrycode)) {
                this.mCountryCode = getDefaultCountryCode();
            }
            setCountryCodeSummary();
        }

        private String getDefaultCountryCode() {
            if (MiuiPhoneUtils.isDcOnlyVirtualSim(getContext())) {
                String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(MiuiSettings.VirtualSim.getVirtualSimSlotId(getContext()) == 0 ? 1 : 0);
                if (!TextUtils.isEmpty(simOperatorForSlot) && simOperatorForSlot.length() >= 3) {
                    return CountryCodeConverter.getCountryCode(simOperatorForSlot.substring(0, 3));
                }
                return "";
            }
            return PhoneNumberUtils.PhoneNumber.getDefaultCountryCode();
        }

        @Override // miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
        public void onStop() {
            super.onStop();
            if (getActivity() == null || getActivity().getIntent() == null || !getActivity().getIntent().getBooleanExtra("automatic_add_country_code", false)) {
                return;
            }
            getActivity().finish();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCountryCode(String str) {
            if (TextUtils.isEmpty(str)) {
                str = getDefaultCountryCode();
            }
            this.mCountryCode = str;
            MiuiSettings.Telephony.setContactCountrycode(getContext().getContentResolver(), str);
            SystemProperties.set("persist.radio.countrycode", str);
            this.mTextCountryCode.setText(str);
            setCountryCodeSummary();
            if (this.mIsAutoAddCountryCode) {
                MiuiSettings.Telephony.setAutoCountryCodeEnable(getContext().getContentResolver(), true);
                this.mAutoCountryCode.setChecked(true);
                this.mIsAutoAddCountryCode = false;
            }
        }

        private void setCountryCodeSummary() {
            boolean zIsLanguageOfRTL = Utils.isLanguageOfRTL();
            LTREditTextPreference lTREditTextPreference = this.mTextCountryCode;
            if (zIsLanguageOfRTL) {
                lTREditTextPreference.setSummary(getString(R.string.telocation_contacts_countrycode_summary, Utils.localizeNumber(this.mCountryCode) + "+"));
                return;
            }
            lTREditTextPreference.setSummary(getString(R.string.telocation_contacts_countrycode_summary, "+" + this.mCountryCode));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void simulatePreferenceClick(Preference preference) {
            preference.performClick();
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            LTREditTextPreference lTREditTextPreference = this.mTextCountryCode;
            if (preference != lTREditTextPreference) {
                return true;
            }
            lTREditTextPreference.setText(this.mCountryCode);
            return true;
        }
    }
}
