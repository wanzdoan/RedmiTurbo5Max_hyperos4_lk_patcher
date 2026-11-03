package com.android.phone.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.ListPreference;
import android.preference.Preference;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import com.android.phone.R;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class AccountSelectionPreference extends ListPreference implements Preference.OnPreferenceChangeListener {
    private PhoneAccountHandle[] mAccounts;
    private final Context mContext;
    private CharSequence[] mEntries;
    private String[] mEntryValues;
    private AccountSelectionListener mListener;
    private boolean mShowSelectionInSummary;

    public interface AccountSelectionListener {
        void onAccountChanged(AccountSelectionPreference accountSelectionPreference);

        boolean onAccountSelected(AccountSelectionPreference accountSelectionPreference, PhoneAccountHandle phoneAccountHandle);

        void onAccountSelectionDialogShow(AccountSelectionPreference accountSelectionPreference);
    }

    public AccountSelectionPreference(Context context) {
        this(context, null);
    }

    public AccountSelectionPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mShowSelectionInSummary = true;
        this.mContext = context;
        setOnPreferenceChangeListener(this);
    }

    public void setListener(AccountSelectionListener accountSelectionListener) {
        this.mListener = accountSelectionListener;
    }

    public void setModel(TelecomManager telecomManager, List list, PhoneAccountHandle phoneAccountHandle, CharSequence charSequence) {
        String strValueOf;
        PhoneAccountHandle[] phoneAccountHandleArr = (PhoneAccountHandle[]) list.toArray(new PhoneAccountHandle[list.size()]);
        this.mAccounts = phoneAccountHandleArr;
        this.mEntryValues = new String[phoneAccountHandleArr.length + 1];
        this.mEntries = new CharSequence[phoneAccountHandleArr.length + 1];
        PackageManager packageManager = this.mContext.getPackageManager();
        int length = this.mAccounts.length;
        int i = 0;
        while (true) {
            PhoneAccountHandle[] phoneAccountHandleArr2 = this.mAccounts;
            if (i >= phoneAccountHandleArr2.length) {
                break;
            }
            PhoneAccount phoneAccount = telecomManager.getPhoneAccount(phoneAccountHandleArr2[i]);
            if (phoneAccount != null) {
                CharSequence label = phoneAccount.getLabel();
                if (label != null) {
                    label = packageManager.getUserBadgedLabel(label, this.mAccounts[i].getUserHandle());
                }
                boolean zHasCapabilities = phoneAccount.hasCapabilities(4);
                CharSequence[] charSequenceArr = this.mEntries;
                if (TextUtils.isEmpty(label) && zHasCapabilities) {
                    strValueOf = this.mContext.getString(R.string.phone_accounts_default_account_label);
                } else {
                    strValueOf = String.valueOf(label);
                }
                charSequenceArr[i] = strValueOf;
                this.mEntryValues[i] = Integer.toString(i);
                if (Objects.equals(phoneAccountHandle, this.mAccounts[i])) {
                    length = i;
                }
            } else {
                Log.e("AccountSelectionPreference", "setModel no PhoneAccount for: " + this.mAccounts[i]);
            }
            i++;
        }
        this.mEntryValues[i] = Integer.toString(i);
        this.mEntries[i] = charSequence;
        setEntryValues(this.mEntryValues);
        setEntries(this.mEntries);
        setValueIndex(length);
        if (this.mShowSelectionInSummary) {
            Log.d("AccountSelectionPreference", "setModel selectedIndex:" + length + ", setSummary:" + ((Object) this.mEntries[length]));
            setSummary(this.mEntries[length]);
        }
    }

    @Override // android.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (this.mListener == null) {
            return false;
        }
        int i = Integer.parseInt((String) obj);
        PhoneAccountHandle[] phoneAccountHandleArr = this.mAccounts;
        if (!this.mListener.onAccountSelected(this, i < phoneAccountHandleArr.length ? phoneAccountHandleArr[i] : null)) {
            return false;
        }
        if (this.mShowSelectionInSummary) {
            Log.d("AccountSelectionPreference", "onPreferenceChange index:" + i + ", setSummary:" + ((Object) this.mEntries[i]));
            setSummary(this.mEntries[i]);
        }
        if (i == findIndexOfValue(getValue())) {
            return true;
        }
        setValueIndex(i);
        this.mListener.onAccountChanged(this);
        return true;
    }

    @Override // android.preference.ListPreference, android.preference.DialogPreference
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        this.mListener.onAccountSelectionDialogShow(this);
        super.onPrepareDialogBuilder(builder);
    }

    public void doPause() {
        Log.d("AccountSelectionPreference", "doPause");
        dismissAccountSelectionDialog();
    }

    private void dismissAccountSelectionDialog() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
