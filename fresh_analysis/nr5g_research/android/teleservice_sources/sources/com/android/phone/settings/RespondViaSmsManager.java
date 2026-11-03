package com.android.phone.settings;

import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public abstract class RespondViaSmsManager {
    private static boolean DBG;

    static {
        boolean z = false;
        if (MiuiPhoneUtils.DBG_LEVEL >= 1 && SystemProperties.getInt("ro.debuggable", 0) == 1) {
            z = true;
        }
        DBG = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.e("RespondViaSmsManager", str);
    }

    public class Settings extends BaseActivity {
        private SettingsFragment mSettingsFragment;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            if (RespondViaSmsManager.DBG) {
                RespondViaSmsManager.log("Settings: onCreate()...");
            }
            SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
            this.mSettingsFragment = settingsFragment;
            if (bundle == null && settingsFragment == null) {
                SettingsFragment settingsFragment2 = new SettingsFragment();
                this.mSettingsFragment = settingsFragment2;
                loadFragment(settingsFragment2);
            }
            getWindow().addFlags(134217728);
        }

        @Override // com.android.phone.settings.BaseActivity, android.app.Activity
        public boolean onOptionsItemSelected(MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == 16908332) {
                finish();
                return true;
            }
            if (itemId == R.id.respond_via_message_reset) {
                MiuiSettings.Telephony.setRejectViaSmsEnable(getContentResolver(), true);
                this.mSettingsFragment.mButtonRejectRespondViaSms.setChecked(true);
                int i = 1;
                for (EditTextPreference editTextPreference : this.mSettingsFragment.mResponsePrefs) {
                    MiuiSettings.Telephony.setRejectViaSmsRespone(getContentResolver(), "reject_via_sms_response_" + i, (String) null);
                    String text = editTextPreference.getText();
                    String string = getResources().getString(this.mSettingsFragment.mDefaultValueIdArray[i + (-1)].intValue());
                    if (!string.equals(text)) {
                        editTextPreference.setText(string);
                        editTextPreference.setTitle(string);
                    }
                    editTextPreference.setEnabled(true);
                    i++;
                }
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }

        @Override // android.app.Activity
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.respond_via_message_settings_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        public class SettingsFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener {
            private CheckBoxPreference mButtonRejectRespondViaSms;
            private Integer[] mDefaultValueIdArray = {Integer.valueOf(R.string.respond_via_sms_canned_response_1), Integer.valueOf(R.string.respond_via_sms_canned_response_2), Integer.valueOf(R.string.respond_via_sms_canned_response_3), Integer.valueOf(R.string.respond_via_sms_canned_response_4)};
            private EditTextPreference[] mResponsePrefs;

            @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
            public void onCreatePreferences(Bundle bundle, String str) {
                getPreferenceManager().setSharedPreferencesName("respond_via_sms_prefs");
                addPreferencesFromResource(R.xml.miui_respond_via_sms_settings);
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("button_call_respond_via_sms");
                this.mButtonRejectRespondViaSms = checkBoxPreference;
                checkBoxPreference.setOnPreferenceChangeListener(this);
                EditTextPreference[] editTextPreferenceArr = new EditTextPreference[4];
                this.mResponsePrefs = editTextPreferenceArr;
                editTextPreferenceArr[0] = (EditTextPreference) findPreference("canned_response_pref_1");
                this.mResponsePrefs[1] = (EditTextPreference) findPreference("canned_response_pref_2");
                this.mResponsePrefs[2] = (EditTextPreference) findPreference("canned_response_pref_3");
                this.mResponsePrefs[3] = (EditTextPreference) findPreference("canned_response_pref_4");
                int i = 1;
                for (EditTextPreference editTextPreference : this.mResponsePrefs) {
                    editTextPreference.setOnPreferenceChangeListener(this);
                    String rejectViaSmsResponse = MiuiSettings.Telephony.getRejectViaSmsResponse(getContext().getContentResolver(), "reject_via_sms_response_" + i, getResources().getString(this.mDefaultValueIdArray[i - 1].intValue()));
                    editTextPreference.setTitle(rejectViaSmsResponse);
                    editTextPreference.setText(rejectViaSmsResponse);
                    i++;
                }
            }

            @Override // androidx.fragment.app.Fragment
            public void onResume() {
                super.onResume();
                boolean zIsRejectViaSmsEnable = MiuiSettings.Telephony.isRejectViaSmsEnable(getContext().getContentResolver());
                this.mButtonRejectRespondViaSms.setChecked(zIsRejectViaSmsEnable);
                for (EditTextPreference editTextPreference : this.mResponsePrefs) {
                    editTextPreference.setEnabled(zIsRejectViaSmsEnable);
                }
            }

            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                if (RespondViaSmsManager.DBG) {
                    RespondViaSmsManager.log("onPreferenceChange: key = " + preference.getKey());
                }
                if (preference == this.mButtonRejectRespondViaSms) {
                    Boolean bool = (Boolean) obj;
                    for (EditTextPreference editTextPreference : this.mResponsePrefs) {
                        editTextPreference.setEnabled(bool.booleanValue());
                    }
                    MiuiSettings.Telephony.setRejectViaSmsEnable(getContext().getContentResolver(), bool.booleanValue());
                } else {
                    EditTextPreference editTextPreference2 = (EditTextPreference) preference;
                    String str = (String) obj;
                    if (str != null && TextUtils.isEmpty(str.replaceAll("\\s*", ""))) {
                        Toast.makeText(getContext(), getResources().getString(R.string.respond_via_sms_cannot_empty), 0).show();
                        return false;
                    }
                    editTextPreference2.setTitle(str);
                    String key = editTextPreference2.getKey();
                    MiuiSettings.Telephony.setRejectViaSmsRespone(getContext().getContentResolver(), "reject_via_sms_response_".concat(key.substring(key.length() - 1)), str);
                }
                return true;
            }
        }
    }
}
