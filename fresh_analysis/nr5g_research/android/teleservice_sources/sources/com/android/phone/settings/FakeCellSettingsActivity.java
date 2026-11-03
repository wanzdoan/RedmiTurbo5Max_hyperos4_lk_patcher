package com.android.phone.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.RilHook;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.CommentPreference;

/* JADX INFO: loaded from: classes.dex */
public class FakeCellSettingsActivity extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        LOG.d("oncreate");
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new FakeCellSettingsFragment());
        }
    }

    @Override // com.android.phone.settings.BaseActivity
    protected void setActionBar() {
        super.setActionBar();
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setTitle(R.string.activity_title_fakecell);
        }
    }

    public class FakeCellSettingsFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceChangeListener {
        private AlertDialog.Builder mBuilder;
        private Context mContext;
        private CommentPreference mForbiddenFakecellDetailsPreference;
        private CheckBoxPreference mForbiddenFakecellPreference;
        private int mJudgeValue;
        private PreferenceCategory mPrefCategory;
        private String mReadItem;
        private RilHook mRilHook;
        private PreferenceScreen mScreenPreference;
        private TelephonyManager mTelephonyManager;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            Context context = getContext();
            this.mContext = context;
            this.mRilHook = RilHook.init(context);
            addPreferencesFromResource(R.xml.main);
            this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mScreenPreference = preferenceScreen;
            this.mPrefCategory = (PreferenceCategory) preferenceScreen.findPreference("fakecell_category");
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mScreenPreference.findPreference("forbidden_fakecell");
            this.mForbiddenFakecellPreference = checkBoxPreference;
            checkBoxPreference.setOnPreferenceChangeListener(this);
            this.mForbiddenFakecellDetailsPreference = (CommentPreference) this.mScreenPreference.findPreference("forbidden_fakecell_details");
            updatePreference();
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (preference != this.mForbiddenFakecellPreference) {
                return false;
            }
            new changeModemValueTask().execute(Integer.valueOf(((Boolean) obj).booleanValue() ? 3 : 4));
            return true;
        }

        private void updatePreference() {
            CheckBoxPreference checkBoxPreference = this.mForbiddenFakecellPreference;
            if (checkBoxPreference != null && this.mForbiddenFakecellDetailsPreference != null) {
                this.mPrefCategory.removePreference(checkBoxPreference);
                this.mScreenPreference.removePreference(this.mForbiddenFakecellDetailsPreference);
            }
            new updatePreferenceTask().execute(new Void[0]);
        }

        class updatePreferenceTask extends AsyncTask {
            private boolean isSupportForbiddenFakecell = false;

            public updatePreferenceTask() {
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Boolean doInBackground(Void... voidArr) {
                if (FakeCellSettingsFragment.this.mTelephonyManager != null) {
                    FakeCellSettingsFragment fakeCellSettingsFragment = FakeCellSettingsFragment.this;
                    fakeCellSettingsFragment.mJudgeValue = fakeCellSettingsFragment.readValueOfModem();
                    LOG.d("readValueOfModem()=" + FakeCellSettingsFragment.this.readValueOfModem());
                    if (FakeCellSettingsFragment.this.mJudgeValue != 0) {
                        this.isSupportForbiddenFakecell = true;
                    }
                }
                return Boolean.valueOf(this.isSupportForbiddenFakecell);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean bool) {
                if (bool.booleanValue()) {
                    FakeCellSettingsFragment.this.mPrefCategory.addPreference(FakeCellSettingsFragment.this.mForbiddenFakecellPreference);
                    FakeCellSettingsFragment.this.mScreenPreference.addPreference(FakeCellSettingsFragment.this.mForbiddenFakecellDetailsPreference);
                    FakeCellSettingsFragment.this.setForbiddenFakecellCheckValue();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setForbiddenFakecellCheckValue() {
            this.mForbiddenFakecellPreference.setChecked(this.mJudgeValue == 1);
            this.mForbiddenFakecellPreference.setEnabled(true);
        }

        class changeModemValueTask extends AsyncTask {
            private boolean isSuccess = false;

            public changeModemValueTask() {
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Boolean doInBackground(Integer... numArr) {
                int iIntValue = numArr[0].intValue();
                this.isSuccess = FakeCellSettingsFragment.this.writeValueOfModem(iIntValue);
                LOG.d("operation: " + String.valueOf(iIntValue) + "isSuccess: " + this.isSuccess);
                return Boolean.valueOf(this.isSuccess);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean bool) {
                if (!this.isSuccess) {
                    SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(FakeCellSettingsFragment.this.mContext);
                    defaultSharedPreferences.edit();
                    FakeCellSettingsFragment.this.mForbiddenFakecellPreference.setChecked(!defaultSharedPreferences.getBoolean("forbidden_fakecell", false));
                    return;
                }
                if (MiuiPhoneUtils.isSupportFakeCellV2()) {
                    return;
                }
                FakeCellSettingsFragment.this.showWhetherRestartDialog();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showWhetherRestartDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            this.mBuilder = builder;
            builder.setTitle(R.string.forbidden_fakecell);
            this.mBuilder.setMessage(R.string.restart_details);
            this.mBuilder.setPositiveButton(R.string.restart_now, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.FakeCellSettingsActivity.FakeCellSettingsFragment.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    new Thread(new Runnable() { // from class: com.android.phone.settings.FakeCellSettingsActivity.FakeCellSettingsFragment.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ((PowerManager) FakeCellSettingsFragment.this.getActivity().getSystemService("power")).reboot(null);
                        }
                    }).start();
                }
            });
            this.mBuilder.setNegativeButton(R.string.restart_later, new DialogInterface.OnClickListener(this) { // from class: com.android.phone.settings.FakeCellSettingsActivity.FakeCellSettingsFragment.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            this.mBuilder.setCancelable(true);
            this.mBuilder.create().show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int readValueOfModem() {
            int i;
            String str;
            String strNvReadItem;
            RilHook rilHook;
            RilHook rilHook2;
            if (MiuiPhoneUtils.isSupportFakeCellV2() && (rilHook2 = this.mRilHook) != null) {
                return rilHook2.getFakeCellStatusMsg(0) ? 1 : 2;
            }
            if (this.mTelephonyManager == null) {
                return 0;
            }
            try {
                if (MiuiPhoneUtils.isSupportFakeCellV1() && (rilHook = this.mRilHook) != null) {
                    strNvReadItem = rilHook.readOemNvItem(6854);
                } else {
                    strNvReadItem = this.mTelephonyManager.nvReadItem(6854);
                }
                this.mReadItem = strNvReadItem;
            } catch (Exception e) {
                LOG.e("nvReadItem error: " + e);
            }
            LOG.d("mReadItem: " + this.mReadItem);
            String str2 = this.mReadItem;
            if (str2 == null) {
                return 0;
            }
            int iIndexOf = str2.indexOf(59);
            if (iIndexOf == -1 || (str = this.mReadItem) == null || str.isEmpty()) {
                i = 0;
            } else {
                try {
                    i = Integer.parseInt(this.mReadItem.substring(0, iIndexOf));
                } catch (NumberFormatException e2) {
                    LOG.e("readValueOfModem error: " + e2);
                    i = 0;
                }
            }
            LOG.d("bit: " + String.valueOf(i));
            if (i <= 0 || (i & 1) == 0) {
                return 0;
            }
            int i2 = i >> 1;
            int i3 = 0;
            for (int i4 = 0; i4 < 2; i4++) {
                if ((i2 & 1) != 0) {
                    i3 |= 1 << i4;
                }
                i2 >>= 1;
            }
            return i3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean writeValueOfModem(int i) {
            int i2;
            RilHook rilHook;
            int iIntValue = 0;
            if (!MiuiPhoneUtils.isSupportFakeCellV2() || (rilHook = this.mRilHook) == null) {
                String str = this.mReadItem;
                if (str != null && !str.isEmpty() && (i2 = this.mJudgeValue) != 0) {
                    if (i2 == 2 && i == 3) {
                        int iIndexOf = this.mReadItem.indexOf(";");
                        String strSubstring = this.mReadItem.substring(iIndexOf);
                        try {
                            iIntValue = Integer.parseInt(this.mReadItem.substring(0, iIndexOf));
                        } catch (NumberFormatException e) {
                            LOG.e("writeValueOfModem error: " + e);
                        }
                        return writeVaule(2, 1, iIntValue, strSubstring);
                    }
                    if (i2 == 1 && i == 4) {
                        int iIndexOf2 = this.mReadItem.indexOf(";");
                        String strSubstring2 = this.mReadItem.substring(iIndexOf2);
                        try {
                            iIntValue = Integer.valueOf(this.mReadItem.substring(0, iIndexOf2)).intValue();
                        } catch (NumberFormatException e2) {
                            LOG.e("writeValueOfModem error: " + e2);
                        }
                        return writeVaule(1, 2, iIntValue, strSubstring2);
                    }
                }
            } else {
                if (i == 3) {
                    return rilHook.setFakeCellStatusMsg(0, true);
                }
                if (i == 4) {
                    return rilHook.setFakeCellStatusMsg(0, false);
                }
            }
            return false;
        }

        private boolean writeVaule(int i, int i2, int i3, String str) {
            boolean zNvWriteItem;
            RilHook rilHook;
            int i4 = ((~(1 << i)) & i3) | (1 << i2);
            Boolean boolValueOf = Boolean.FALSE;
            String str2 = String.valueOf(i4) + str;
            LOG.d("str: ".concat(str2));
            try {
                if (this.mTelephonyManager != null) {
                    try {
                        if (MiuiPhoneUtils.isSupportFakeCellV1() && (rilHook = this.mRilHook) != null) {
                            zNvWriteItem = rilHook.writeOemNvItem(6854, str2);
                        } else {
                            zNvWriteItem = this.mTelephonyManager.nvWriteItem(6854, str2);
                        }
                        boolValueOf = Boolean.valueOf(zNvWriteItem);
                        LOG.d("temp: " + boolValueOf);
                    } catch (Exception e) {
                        LOG.e("nvWriteItem error: " + e);
                    }
                }
                return boolValueOf.booleanValue();
            } finally {
                this.mJudgeValue = readValueOfModem();
            }
        }
    }
}
