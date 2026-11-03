package com.android.phone.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.ArrayList;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class CallBarring extends TimeConsumingPreferenceActivity {
    private static final String LOG_TAG = CallBarring.class.getSimpleName();
    private CallBarringFragment mCallBarringFragment;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.TimeConsumingPreferenceActivity, com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SimPickerPreference.showSimPicker(this)) {
            return;
        }
        CallBarringFragment callBarringFragment = (CallBarringFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        this.mCallBarringFragment = callBarringFragment;
        if (bundle == null && callBarringFragment == null) {
            CallBarringFragment callBarringFragment2 = new CallBarringFragment();
            this.mCallBarringFragment = callBarringFragment2;
            loadFragment(callBarringFragment2);
        }
    }

    @Override // com.android.phone.settings.TimeConsumingPreferenceActivity, android.app.Activity
    protected Dialog onCreateDialog(int i) {
        int i2;
        if (i == 700 || i == 800) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            int i3 = R.string.error_updating_title;
            if (i == 700) {
                builder.setNeutralButton(R.string.close_dialog, this.mCallBarringFragment.mDismiss);
                i2 = android.R.string.permlab_getTasks;
            } else if (i == 800) {
                i2 = R.string.command_error_fdn_failure;
                builder.setNeutralButton(R.string.close_dialog, this.mCallBarringFragment.mDismiss);
            } else {
                i2 = R.string.exception_error;
                builder.setNeutralButton(R.string.close_dialog, this.mCallBarringFragment.mDismissAndFinish);
            }
            builder.setTitle(getText(i3));
            builder.setMessage(getText(i2));
            builder.setCancelable(false);
            return builder.create();
        }
        return super.onCreateDialog(i);
    }

    public class CallBarringFragment extends TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment implements CallBarringInterface {
        private boolean DBG;
        private CallBarringBasePreference mCallAllOutButton;
        private CallBarringResetPreference mCallCancel;
        private Preference mCallChangePassword;
        private CallBarringBasePreference mCallInButton;
        private CallBarringBasePreference mCallInButton2;
        private CallBarringBasePreference mCallInternationalOutButton;
        private CallBarringBasePreference mCallInternationalOutButton2;
        private int mCancelAllIndex;
        private AlertDialog mDialog;
        private int mErrorState;
        private boolean mFirstResume;
        private boolean mHasShowSuppServiceDialog;
        private int mInitIndex;
        private boolean mNeedCancelAllSequence;
        private String mPassword;
        private PreferenceCategory mPrefCategory;
        private ArrayList mPreferences;
        private int mSlotId;

        public CallBarringFragment() {
            this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
            this.mPreferences = new ArrayList();
            this.mInitIndex = 0;
            this.mErrorState = 0;
            this.mHasShowSuppServiceDialog = false;
            this.mSlotId = 0;
            this.mNeedCancelAllSequence = false;
            this.mCancelAllIndex = 0;
            this.mPassword = "";
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            super.onCreatePreferences(bundle, str);
            this.mSlotId = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultDataSlotId());
            addPreferencesFromResource(R.xml.call_barring_options);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mPrefCategory = (PreferenceCategory) preferenceScreen.findPreference("all_category");
            this.mCallAllOutButton = (CallBarringBasePreference) preferenceScreen.findPreference("all_outing_key");
            this.mCallInternationalOutButton = (CallBarringBasePreference) preferenceScreen.findPreference("all_outing_international_key");
            this.mCallInternationalOutButton2 = (CallBarringBasePreference) preferenceScreen.findPreference("all_outing_except_key");
            this.mCallInButton = (CallBarringBasePreference) preferenceScreen.findPreference("all_incoming_key");
            this.mCallInButton2 = (CallBarringBasePreference) preferenceScreen.findPreference("all_incoming_except_key");
            this.mCallCancel = (CallBarringResetPreference) preferenceScreen.findPreference("deactivate_all_key");
            this.mCallChangePassword = preferenceScreen.findPreference("change_password_key");
            if (!isOperatorSupportChangeCancelCB(MiuiPhoneUtils.getPhone(this.mSlotId))) {
                this.mPrefCategory.removePreference(this.mCallCancel);
                this.mPrefCategory.removePreference(this.mCallChangePassword);
            }
            initial();
            this.mPreferences.add(this.mCallAllOutButton);
            this.mPreferences.add(this.mCallInternationalOutButton);
            this.mPreferences.add(this.mCallInternationalOutButton2);
            this.mPreferences.add(this.mCallInButton);
            this.mPreferences.add(this.mCallInButton2);
            this.mCallAllOutButton.setRefreshInterface(this);
            this.mCallInternationalOutButton.setRefreshInterface(this);
            this.mCallInternationalOutButton2.setRefreshInterface(this);
            this.mCallInButton.setRefreshInterface(this);
            this.mCallInButton2.setRefreshInterface(this);
            this.mCallCancel.setCallBarringInterface(this);
            if (MiuiPhoneUtils.getPhone(this.mSlotId).getPhoneType() == 2) {
                ArrayList arrayList = this.mPreferences;
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    setPreferenceNotSupported((Preference) obj);
                }
                setPreferenceNotSupported(this.mCallCancel);
                setPreferenceNotSupported(this.mCallChangePassword);
                return;
            }
            this.mFirstResume = true;
        }

        private boolean isOperatorSupportChangeCancelCB(Phone phone) {
            if (MiuiPhoneUtils.isJioSim(phone.getServiceState().getOperatorNumeric())) {
                return false;
            }
            return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_support_change_and_cancel_cb", true);
        }

        private void setPreferenceNotSupported(Preference preference) {
            preference.setEnabled(false);
            preference.setSummary(preference.getContext().getString(R.string.lable_network_not_supported));
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            if (this.mFirstResume) {
                if (this.mHasShowSuppServiceDialog && getActivity() != null) {
                    getActivity().finish();
                    return;
                }
                if (MiuiPhoneUtils.maybeShowDialogForSuppService(MiuiPhoneUtils.getPhone(this.mSlotId), 0)) {
                    this.mHasShowSuppServiceDialog = true;
                } else if (MiuiPhoneUtils.maybeShowDataInUseForViceCard(MiuiPhoneUtils.getPhone(this.mSlotId), 0)) {
                    showViceDataInUseDialog();
                } else {
                    startUpdate();
                }
            }
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference != this.mCallChangePassword) {
                return false;
            }
            new CallBarringChangePasswordDialog(getContext(), this.mSlotId).show();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startUpdate() {
            this.mInitIndex = 0;
            Preference preference = (Preference) this.mPreferences.get(0);
            if (preference != null) {
                doGetCallState(preference);
            }
            this.mFirstResume = false;
        }

        private void initial() {
            Phone phone = MiuiPhoneUtils.getPhone(this.mSlotId);
            this.mCallAllOutButton.setmFacility("AO");
            this.mCallAllOutButton.setmTitle(R.string.lable_all_outgoing);
            this.mCallAllOutButton.setPhoneInstance(phone);
            this.mCallInternationalOutButton.setmFacility("OI");
            this.mCallInternationalOutButton.setmTitle(R.string.lable_all_outgoing_international);
            this.mCallInternationalOutButton.setPhoneInstance(phone);
            this.mCallInternationalOutButton2.setmFacility("OX");
            this.mCallInternationalOutButton2.setmTitle(R.string.lable_all_out_except);
            this.mCallInternationalOutButton2.setPhoneInstance(phone);
            this.mCallInButton.setmFacility("AI");
            this.mCallInButton.setmTitle(R.string.lable_all_incoming);
            this.mCallInButton.setPhoneInstance(phone);
            this.mCallInButton2.setmFacility("IR");
            this.mCallInButton2.setmTitle(R.string.lable_incoming_calls_except);
            this.mCallInButton2.setPhoneInstance(phone);
            this.mCallCancel.setListener(this);
            this.mCallCancel.setPhoneInstance(phone);
        }

        private void doGetCallState(Preference preference) {
            if (preference instanceof CallBarringBasePreference) {
                ((CallBarringBasePreference) preference).init(this, false);
            }
        }

        private void doSetCallState(CallBarringBasePreference callBarringBasePreference, String str) {
            callBarringBasePreference.setCallState(str);
            this.mCancelAllIndex++;
        }

        @Override // com.android.phone.settings.CallBarringInterface
        public void doCancelAllState() {
            String string = this.mCallAllOutButton.getContext().getString(R.string.lable_disable);
            this.mCallAllOutButton.setSummary(string);
            this.mCallAllOutButton.setChecked(false);
            this.mCallInternationalOutButton.setSummary(string);
            this.mCallInternationalOutButton.setChecked(false);
            this.mCallInternationalOutButton2.setSummary(string);
            this.mCallInternationalOutButton2.setChecked(false);
            this.mCallInButton.setSummary(string);
            this.mCallInButton.setChecked(false);
            this.mCallInButton2.setSummary(string);
            this.mCallInButton2.setChecked(false);
        }

        @Override // com.android.phone.settings.CallBarringInterface
        public void setErrorState(int i) {
            this.mErrorState = i;
        }

        @Override // com.android.phone.settings.CallBarringInterface
        public void cancelAllBySequence(String str) {
            this.mNeedCancelAllSequence = true;
            this.mPassword = str;
            this.mCancelAllIndex = 0;
            doSetCallState((CallBarringBasePreference) this.mPreferences.get(0), this.mPassword);
        }

        public void resetIndex(int i) {
            this.mInitIndex = i;
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            AlertDialog alertDialog = this.mDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            this.mDialog.dismiss();
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.TimeConsumingPreferenceListener
        public void onFinished(Preference preference, boolean z) {
            ArrayList arrayList;
            if (getActivity() == null) {
                return;
            }
            if (this.mInitIndex < this.mPreferences.size() - 1 && !getActivity().isFinishing()) {
                boolean zIsSuccess = ((CallBarringBasePreference) this.mPreferences.get(this.mInitIndex)).isSuccess();
                int i = this.mInitIndex;
                if (zIsSuccess) {
                    this.mInitIndex = i + 1;
                    if (this.DBG) {
                        Log.i(CallBarring.LOG_TAG, "onFinished() is called (init part) mInitIndex is " + this.mInitIndex + "is reading?  " + z);
                    }
                    doGetCallState((Preference) this.mPreferences.get(this.mInitIndex));
                } else {
                    while (true) {
                        int size = this.mPreferences.size();
                        arrayList = this.mPreferences;
                        if (i >= size) {
                            break;
                        }
                        ((Preference) arrayList.get(i)).setEnabled(false);
                        i++;
                    }
                    this.mInitIndex = arrayList.size();
                }
            }
            if (this.mNeedCancelAllSequence && this.mCancelAllIndex < this.mPreferences.size() && !getActivity().isFinishing()) {
                doSetCallState((CallBarringBasePreference) this.mPreferences.get(this.mCancelAllIndex), this.mPassword);
                if (this.mCancelAllIndex == this.mPreferences.size()) {
                    this.mNeedCancelAllSequence = false;
                }
            }
            super.onFinished(preference, z);
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.TimeConsumingPreferenceListener
        public void onError(Preference preference, int i) {
            MiuiPhoneUtils.getOperatorNumeric(MiuiPhoneUtils.getPhone(this.mSlotId));
            if (HyperOSCustFeatureResolve.getBooleanForPhoneId(this.mSlotId, "hide_call_barring_error", false)) {
                getPreferenceScreen().setEnabled(false);
            } else {
                super.onError(preference, i);
                preference.setEnabled(true);
            }
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.TimeConsumingPreferenceListener
        public void onException(Preference preference, CommandException commandException) {
            if (commandException.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE) {
                onError(preference, 600);
            } else {
                preference.setEnabled(false);
                onError(preference, MiRilHook.HOOK_ID_START_URC_BY_AT);
            }
        }

        private void showViceDataInUseDialog() {
            if (this.mDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.vice_card_data_in_use_alert).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallBarring.CallBarringFragment.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        CallBarringFragment.this.startUpdate();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallBarring.CallBarringFragment.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (CallBarringFragment.this.getActivity() != null) {
                            CallBarringFragment.this.getActivity().finish();
                        }
                    }
                }).setCancelable(false);
                this.mDialog = builder.create();
            }
            this.mDialog.show();
        }
    }
}
