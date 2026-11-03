package com.android.phone.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.phone.R;
import com.android.phone.custompreferences.CustomCheckBoxPreference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miuix.preference.DropDownPreference;

/* JADX INFO: loaded from: classes.dex */
public class PhoneAccountSettingsActivity extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!TelephonyManager.isGoogleCsp() || TelephonyManagerEx.getDefault().isGoogleDialerDeleted(this) || isCtsVerifierInstalled(this)) {
            if (bundle == null) {
                loadFragment(new MiuiPhoneAccountSettingsFragment());
            }
        } else {
            Intent intent = getIntent();
            if (intent == null) {
                intent = new Intent();
            }
            intent.setComponent(new ComponentName(this, "com.android.phone.settings.CallFeaturesSetting"));
            startActivity(intent);
            finish();
        }
    }

    @Override // com.android.phone.settings.BaseActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public void setTheme(int i) {
        super.setTheme(R.style.Miui_Theme_Settings_Floating);
    }

    private static boolean isCtsVerifierInstalled(Context context) {
        try {
            context.getPackageManager().getApplicationInfo("com.android.cts.verifier", 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public class MiuiPhoneAccountSettingsFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener {
        private List mEnabledNonSimAccounts = new ArrayList();
        private PreferenceCategory mPrefAccountsInfo;
        private DropDownPreference mPreferredPhoneAccount;
        private TelecomManager mTelecomManager;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.miui_phone_account_settings);
            this.mPrefAccountsInfo = (PreferenceCategory) findPreference("accounts_category");
            DropDownPreference dropDownPreference = (DropDownPreference) findPreference("button_preferred_phone_account");
            this.mPreferredPhoneAccount = dropDownPreference;
            dropDownPreference.setOnPreferenceChangeListener(this);
            this.mTelecomManager = TelecomManager.from(getContext());
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            updateView();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateView() {
            this.mPrefAccountsInfo.removeAll();
            this.mEnabledNonSimAccounts.clear();
            Iterator it = this.mTelecomManager.getCallCapablePhoneAccounts(true).iterator();
            while (it.hasNext()) {
                PhoneAccount phoneAccount = this.mTelecomManager.getPhoneAccount((PhoneAccountHandle) it.next());
                if (phoneAccount != null && !phoneAccount.hasCapabilities(4)) {
                    this.mPrefAccountsInfo.addPreference(new AccountSwitchPreference(getPreferenceManager().getContext(), phoneAccount));
                    if (phoneAccount.isEnabled()) {
                        this.mEnabledNonSimAccounts.add(phoneAccount);
                    }
                }
            }
            updatePreferredPhoneAcount();
        }

        private void updatePreferredPhoneAcount() {
            int size = this.mEnabledNonSimAccounts.size();
            PhoneAccountHandle userSelectedOutgoingPhoneAccount = this.mTelecomManager.getUserSelectedOutgoingPhoneAccount();
            int i = size + 1;
            String[] strArr = new String[i];
            CharSequence[] charSequenceArr = new CharSequence[i];
            int i2 = size;
            int i3 = 0;
            while (i3 < size) {
                charSequenceArr[i3] = String.valueOf(((PhoneAccount) this.mEnabledNonSimAccounts.get(i3)).getLabel());
                strArr[i3] = Integer.toString(i3);
                if (Objects.equals(userSelectedOutgoingPhoneAccount, ((PhoneAccount) this.mEnabledNonSimAccounts.get(i3)).getAccountHandle())) {
                    i2 = i3;
                }
                i3++;
            }
            strArr[i3] = Integer.toString(i3);
            charSequenceArr[i3] = getString(R.string.sim_accounts);
            this.mPreferredPhoneAccount.setEntryValues(strArr);
            this.mPreferredPhoneAccount.setEntries(charSequenceArr);
            this.mPreferredPhoneAccount.setValueIndex(i2);
            this.mPreferredPhoneAccount.setEnabled(i3 != 0);
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (preference != this.mPreferredPhoneAccount) {
                return true;
            }
            int i = Integer.parseInt((String) obj);
            this.mTelecomManager.setUserSelectedOutgoingPhoneAccount(i < this.mEnabledNonSimAccounts.size() ? ((PhoneAccount) this.mEnabledNonSimAccounts.get(i)).getAccountHandle() : null);
            DropDownPreference dropDownPreference = this.mPreferredPhoneAccount;
            dropDownPreference.setValue(dropDownPreference.getEntries()[i].toString());
            return true;
        }

        final class AccountSwitchPreference extends CustomCheckBoxPreference {
            private final PhoneAccount mAccount;

            public AccountSwitchPreference(Context context, PhoneAccount phoneAccount) {
                super(context);
                this.mAccount = phoneAccount;
                setTitle(phoneAccount.getLabel());
                setSummary(phoneAccount.getShortDescription());
                Icon icon = phoneAccount.getIcon();
                if (icon != null) {
                    setIcon(icon.loadDrawable(context));
                }
                setChecked(phoneAccount.isEnabled());
            }

            @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
            protected void onClick() {
                super.onClick();
                MiuiPhoneAccountSettingsFragment.this.mTelecomManager.enablePhoneAccount(this.mAccount.getAccountHandle(), isChecked());
                MiuiPhoneAccountSettingsFragment.this.updateView();
            }
        }
    }
}
