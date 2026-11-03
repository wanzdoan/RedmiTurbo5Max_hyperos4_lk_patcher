package com.android.phone.settings;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import com.android.phone.R;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.TelephonyManager;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$style;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AlertDialog;
import miuix.internal.util.AttributeResolver;
import miuix.preference.DropDownPreference;

/* JADX INFO: loaded from: classes.dex */
public class UPLMNEditor extends BaseActivity {
    private UPLMNEditorFragment mUPLMNEditorFragment;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getApplicationInfo().targetSdkVersion >= 35) {
            getWindow().addFlags(134217728);
        }
        UPLMNEditorFragment uPLMNEditorFragment = (UPLMNEditorFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        this.mUPLMNEditorFragment = uPLMNEditorFragment;
        if (bundle == null && uPLMNEditorFragment == null) {
            UPLMNEditorFragment uPLMNEditorFragment2 = new UPLMNEditorFragment();
            this.mUPLMNEditorFragment = uPLMNEditorFragment2;
            loadFragment(uPLMNEditorFragment2);
        }
    }

    @Override // com.android.phone.settings.BaseActivity
    protected void setActionBar() {
        super.setActionBar();
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setTitle(R.string.uplmn_list_setting_title);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (!getIntent().getBooleanExtra("uplmn_add", false)) {
            menu.add(0, 1, 0, android.R.string.factorytest_not_system).setIcon(AttributeResolver.resolveDrawable(this, R$attr.actionBarDeleteIcon)).setShowAsAction(1);
        }
        menu.add(0, 2, 0, R.string.save).setIcon(AttributeResolver.resolveDrawable(this, R$attr.actionBarSaveIcon)).setShowAsAction(1);
        menu.add(0, 3, 0, android.R.string.cancel).setIcon(AttributeResolver.resolveDrawable(this, R$attr.actionBarDiscardIcon)).setShowAsAction(1);
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onMenuOpened(int i, Menu menu) {
        super.onMenuOpened(i, menu);
        boolean z = false;
        boolean z2 = this.mUPLMNEditorFragment.mNoSet.equals(this.mUPLMNEditorFragment.mNWIDPref.getSummary()) || this.mUPLMNEditorFragment.mNoSet.equals(this.mUPLMNEditorFragment.mPRIpref.getSummary());
        if (menu != null) {
            menu.setGroupEnabled(0, !this.mUPLMNEditorFragment.mAirplaneModeOn);
            if (getIntent().getBooleanExtra("uplmn_add", true)) {
                MenuItem item = menu.getItem(0);
                if (!this.mUPLMNEditorFragment.mAirplaneModeOn && !z2) {
                    z = true;
                }
                item.setEnabled(z);
            } else {
                MenuItem item2 = menu.getItem(1);
                if (!this.mUPLMNEditorFragment.mAirplaneModeOn && !z2) {
                    z = true;
                }
                item2.setEnabled(z);
            }
        }
        return true;
    }

    @Override // com.android.phone.settings.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 2) {
            this.mUPLMNEditorFragment.setSavedNWInfo();
        } else if (menuItem.getItemId() == 1) {
            this.mUPLMNEditorFragment.setRemovedNWInfo();
        } else {
            if (menuItem.getItemId() == 3) {
                finish();
                return true;
            }
            if (menuItem.getItemId() == 16908332) {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.app.Activity
    public Dialog onCreateDialog(int i) {
        if (i != 0) {
            return null;
        }
        this.mUPLMNEditorFragment.mNWIDText = new EditText(this, null, 0, R$style.Widget_EditText_Dialog);
        if (!this.mUPLMNEditorFragment.mNoSet.equals(this.mUPLMNEditorFragment.mNWIDPref.getSummary())) {
            this.mUPLMNEditorFragment.mNWIDText.setText(this.mUPLMNEditorFragment.mNWIDPref.getSummary());
        }
        this.mUPLMNEditorFragment.mNWIDText.addTextChangedListener(this.mUPLMNEditorFragment);
        this.mUPLMNEditorFragment.mNWIDText.setInputType(2);
        this.mUPLMNEditorFragment.mNWIDText.requestFocus();
        this.mUPLMNEditorFragment.mNWIDDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.network_id)).setView(this.mUPLMNEditorFragment.mNWIDText).setPositiveButton(getResources().getString(android.R.string.ok), this.mUPLMNEditorFragment.mNWIDPrefListener).setNegativeButton(getResources().getString(android.R.string.cancel), (DialogInterface.OnClickListener) null).create();
        this.mUPLMNEditorFragment.mNWIDDialog.getWindow().setSoftInputMode(4);
        return this.mUPLMNEditorFragment.mNWIDDialog;
    }

    public class UPLMNEditorFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener, TextWatcher {
        private IntentFilter mIntentFilter;
        private EditText mNWIDText;
        private int mSlotId;
        private Preference mNWIDPref = null;
        private EditTextPreference mPRIpref = null;
        private DropDownPreference mNWMPref = null;
        private String mNoSet = null;
        private boolean mAirplaneModeOn = false;
        private AlertDialog mNWIDDialog = null;
        private String mOperatorNumeric = null;
        private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.UPLMNEditor.UPLMNEditorFragment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                    UPLMNEditorFragment.this.mAirplaneModeOn = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                    UPLMNEditorFragment.this.setScreenEnabled();
                }
            }
        };
        private DialogInterface.OnClickListener mNWIDPrefListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.UPLMNEditor.UPLMNEditorFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    UPLMNEditorFragment uPLMNEditorFragment = UPLMNEditorFragment.this;
                    String strGenText = uPLMNEditorFragment.genText(uPLMNEditorFragment.mNWIDText.getText().toString());
                    Log.d("UPLMNEditor", "input network id is " + strGenText);
                    UPLMNEditorFragment.this.mNWIDPref.setSummary(strGenText);
                }
            }
        };

        public static int convertApMode2EF(int i) {
            if (i == 3) {
                return 13;
            }
            if (i == 2) {
                return 8;
            }
            return i == 1 ? 4 : 1;
        }

        public static int convertEFMode2Ap(int i) {
            if (i == 13) {
                return 3;
            }
            if (i == 4) {
                return 1;
            }
            return i == 8 ? 2 : 0;
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.uplmn_editor);
            this.mNoSet = getResources().getString(R.string.voicemail_number_not_set);
            this.mNWIDPref = findPreference("network_id_key");
            this.mPRIpref = (EditTextPreference) findPreference("priority_key");
            this.mNWMPref = (DropDownPreference) findPreference("network_mode_key");
            this.mPRIpref.setOnPreferenceChangeListener(this);
            this.mNWMPref.setOnPreferenceChangeListener(this);
            this.mIntentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
            getContext().registerReceiver(this.mReceiver, this.mIntentFilter, 2);
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            if (getActivity() != null) {
                displayNetworkInfo(getActivity().getIntent());
            }
            this.mAirplaneModeOn = Settings.System.getInt(getContext().getContentResolver(), "airplane_mode_on", -1) == 1;
            setScreenEnabled();
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            getContext().unregisterReceiver(this.mReceiver);
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            String string = obj.toString();
            EditTextPreference editTextPreference = this.mPRIpref;
            if (preference == editTextPreference) {
                editTextPreference.setSummary(genText(string));
                return true;
            }
            DropDownPreference dropDownPreference = this.mNWMPref;
            if (preference != dropDownPreference) {
                return true;
            }
            dropDownPreference.setValue(string);
            this.mNWMPref.setSummary(getNetWorkModeString(this.mOperatorNumeric)[Integer.parseInt(string)]);
            return true;
        }

        public String[] getNetWorkModeString(String str) {
            if (TextUtils.isEmpty(str)) {
                str = TelephonyManager.getDefault().getSimOperatorForSlot(this.mSlotId);
            }
            if (ServiceProviderUtils.isChinaMobile(str)) {
                return getResources().getStringArray(R.array.uplmn_prefer_network_mode_td_choices);
            }
            if (ServiceProviderUtils.isChinaUnicom(str)) {
                return getResources().getStringArray(R.array.uplmn_prefer_network_mode_w_choices);
            }
            Log.w("UPLMNEditor", "getNetWorkModeString: operatorNumeric is unavailable, operatorNumeric = " + str + ", return default strings.");
            return getResources().getStringArray(R.array.uplmn_prefer_network_mode_w_choices);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSavedNWInfo() {
            genNWInfoToIntent(new Intent(getContext(), (Class<?>) UserPLMNListPreference.class), 101);
        }

        /* JADX WARN: Code duplicated, block: B:19:0x0072  */
        private void genNWInfoToIntent(Intent intent, int i) {
            int i2;
            if (TextUtils.isEmpty(this.mNWIDPref.getSummary()) || this.mNoSet.equals(this.mNWIDPref.getSummary()) || getActivity() == null) {
                Toast.makeText(getContext(), getString(R.string.uplmn_numeric_is_empty), 1).show();
                return;
            }
            intent.putExtra("uplmn_code", this.mNWIDPref.getSummary());
            int intExtra = getActivity().getIntent().getIntExtra("uplmn_size", 0);
            try {
                i2 = Integer.parseInt(String.valueOf(this.mPRIpref.getSummary()));
            } catch (NumberFormatException unused) {
                Log.d("UPLMNEditor", "parse value of basband error.");
                i2 = 0;
            }
            if (getActivity().getIntent().getBooleanExtra("uplmn_add", false)) {
                if (i2 <= intExtra) {
                    intExtra = i2;
                }
            } else if (i2 >= intExtra) {
                intExtra--;
            } else {
                intExtra = i2;
            }
            intent.putExtra("uplmn_priority", intExtra);
            try {
                intent.putExtra("uplmn_service", convertApMode2EF(Integer.parseInt(String.valueOf(this.mNWMPref.getValue()))));
            } catch (NumberFormatException unused2) {
                intent.putExtra("uplmn_service", convertApMode2EF(0));
            }
            getActivity().setResult(i, intent);
            getActivity().finish();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setRemovedNWInfo() {
            genNWInfoToIntent(new Intent(getContext(), (Class<?>) UserPLMNListPreference.class), 102);
        }

        private void displayNetworkInfo(Intent intent) {
            int i = 0;
            this.mSlotId = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, 0);
            String stringExtra = intent.getStringExtra("uplmn_code");
            this.mOperatorNumeric = stringExtra;
            this.mNWIDPref.setSummary(genText(stringExtra));
            int intExtra = intent.getIntExtra("uplmn_priority", 0);
            this.mPRIpref.setSummary(String.valueOf(intExtra));
            this.mPRIpref.setText(String.valueOf(intExtra));
            int intExtra2 = intent.getIntExtra("uplmn_service", 0);
            Log.d("UPLMNEditor", "act = " + intExtra2);
            int iConvertEFMode2Ap = convertEFMode2Ap(intExtra2);
            if (iConvertEFMode2Ap >= 0 && iConvertEFMode2Ap <= 3) {
                i = iConvertEFMode2Ap;
            }
            this.mNWMPref.setEntries(getNetWorkModeString(this.mOperatorNumeric));
            this.mNWMPref.setSummary(getNetWorkModeString(this.mOperatorNumeric)[i]);
            this.mNWMPref.setValue(String.valueOf(i));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String genText(String str) {
            return (str == null || str.length() == 0) ? this.mNoSet : str;
        }

        public void buttonEnabled() {
            int length = this.mNWIDText.getText().toString().length();
            boolean z = length >= 5 && length <= 6;
            AlertDialog alertDialog = this.mNWIDDialog;
            if (alertDialog != null) {
                alertDialog.getButton(-1).setEnabled(z);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setScreenEnabled() {
            getPreferenceScreen().setEnabled(!this.mAirplaneModeOn);
            if (getActivity() != null) {
                getActivity().invalidateOptionsMenu();
            }
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference == this.mNWIDPref && getActivity() != null) {
                getActivity().removeDialog(0);
                getActivity().showDialog(0);
                buttonEnabled();
            }
            return super.onPreferenceTreeClick(preference);
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            buttonEnabled();
        }
    }
}
