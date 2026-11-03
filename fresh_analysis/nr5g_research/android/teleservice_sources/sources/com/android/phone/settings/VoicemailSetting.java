package com.android.phone.settings;

import android.app.ComponentCaller;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.media.ExtraRingtone;
import android.media.ExtraRingtoneManager;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.utils.Utils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.ot.pubsub.i.a;
import gov.nist.javax.sip.address.GenericURI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;
import miuix.preference.DropDownPreference;

/* JADX INFO: loaded from: classes.dex */
public class VoicemailSetting extends TimeConsumingPreferenceActivity {
    public static final CallForwardInfo[] FWD_SETTINGS_DONT_TOUCH = null;
    private VoicemailSettingFragment mVoicemailSettingFragment;
    private static final String[] NUM_PROJECTION = {"data1"};
    static final int[] FORWARDING_SETTINGS_REASONS = {0, 1, 2, 3};

    @Override // android.app.Activity
    protected void onPrepareDialog(int i, Dialog dialog, Bundle bundle) {
        super.onPrepareDialog(i, dialog, bundle);
        this.mVoicemailSettingFragment.mCurrentDialogId = i;
    }

    @Override // com.android.phone.settings.TimeConsumingPreferenceActivity, android.app.Activity
    protected Dialog onCreateDialog(int i) {
        int i2;
        int i3;
        if (i != 1500 && i != 1400 && i != 1501 && i != 1502 && i != 1600) {
            if (i == 1601 || i == 1602 || i == 1603) {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                if (i == 1601) {
                    i3 = R.string.updating_settings;
                } else if (i == 1603) {
                    i3 = R.string.reverting_settings;
                } else {
                    i3 = R.string.reading_settings;
                }
                progressDialog.setMessage(getText(i3));
                return progressDialog;
            }
            return super.onCreateDialog(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int i4 = R.string.error_updating_title;
        if (i == 1400) {
            i2 = R.string.no_change;
            i4 = R.string.voicemail;
            builder.setNegativeButton(R.string.close_dialog, this.mVoicemailSettingFragment.mDismiss);
        } else if (i == 1600) {
            i2 = R.string.vm_changed;
            i4 = R.string.voicemail;
            builder.setNegativeButton(R.string.close_dialog, this.mVoicemailSettingFragment.mDismiss);
        } else {
            switch (i) {
                case 1500:
                    i2 = R.string.vm_change_failed;
                    builder.setPositiveButton(R.string.close_dialog, this.mVoicemailSettingFragment.mDismiss);
                    break;
                case 1501:
                    i2 = R.string.fw_change_failed;
                    builder.setPositiveButton(R.string.close_dialog, this.mVoicemailSettingFragment.mDismiss);
                    break;
                case 1502:
                    i2 = R.string.fw_get_in_vm_failed;
                    builder.setPositiveButton(R.string.alert_dialog_yes, this.mVoicemailSettingFragment.mDismiss);
                    builder.setNegativeButton(R.string.alert_dialog_no, this.mVoicemailSettingFragment.mDismiss);
                    break;
                default:
                    i2 = R.string.exception_error;
                    builder.setNeutralButton(R.string.close_dialog, this.mVoicemailSettingFragment.mDismissAndFinish);
                    break;
            }
        }
        builder.setTitle(getText(i4));
        builder.setMessage(getText(i2).toString());
        builder.setCancelable(false);
        AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().addFlags(2);
        return alertDialogCreate;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.TimeConsumingPreferenceActivity, com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SimPickerPreference.showSimPicker(this)) {
            return;
        }
        VoicemailSettingFragment voicemailSettingFragment = (VoicemailSettingFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        this.mVoicemailSettingFragment = voicemailSettingFragment;
        if (bundle == null && voicemailSettingFragment == null) {
            VoicemailSettingFragment voicemailSettingFragment2 = new VoicemailSettingFragment();
            this.mVoicemailSettingFragment = voicemailSettingFragment2;
            loadFragment(voicemailSettingFragment2);
        }
    }

    public class VoicemailSettingFragment extends TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment implements Preference.OnPreferenceChangeListener, EditPhoneNumberPreference.OnDialogClosedListener, EditPhoneNumberPreference.GetDefaultNumberListener {
        private boolean DBG;
        boolean mChangingVMorFwdDueToProviderChange;
        int mCurrentDialogId;
        private Collection mExpectedChangeResultReasons;
        private boolean mForeground;
        private Map mForwardingChangeResults;
        CallForwardInfo[] mForwardingReadResults;
        boolean mFwdChangesRequireRollback;
        private final Handler mGetOptionComplete;
        private CallForwardInfo[] mNewFwdSettings;
        String mNewVMNumber;
        private String mOldVmNumber;
        SharedPreferences mPerProviderSavedVMNumbers;
        private Phone mPhone;
        String mPreviousVMProviderKey;
        private boolean mReadingSettingsForDefaultProvider;
        private final Handler mRevertOptionComplete;
        private final Handler mRingtoneLookupComplete;
        private Runnable mRingtoneLookupRunnable;
        private final Handler mSetOptionComplete;
        private EditPhoneNumberPreference mSubMenuVoicemailSettings;
        boolean mVMChangeCompletedSuccesfully;
        int mVMOrFwdSetError;
        boolean mVMProviderSettingsForced;
        private final Map mVMProvidersData;
        private VoiceMailProviderSettings mVoiceMailProviderSettings;
        AsyncResult mVoicemailChangeResult;
        private Preference mVoicemailNotificationRingtone;
        private CheckBoxPreference mVoicemailNotificationVibrate;
        private DropDownPreference mVoicemailProviders;

        public VoicemailSettingFragment() {
            this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
            this.mRingtoneLookupComplete = new Handler() { // from class: com.android.phone.settings.VoicemailSetting.VoicemailSettingFragment.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    if (message.what != 2) {
                        return;
                    }
                    VoicemailSettingFragment.this.mVoicemailNotificationRingtone.setSummary((CharSequence) message.obj);
                }
            };
            this.mForwardingReadResults = null;
            this.mForwardingChangeResults = null;
            this.mExpectedChangeResultReasons = null;
            this.mVoicemailChangeResult = null;
            this.mPreviousVMProviderKey = null;
            this.mCurrentDialogId = 0;
            this.mVMProviderSettingsForced = false;
            this.mChangingVMorFwdDueToProviderChange = false;
            this.mVMChangeCompletedSuccesfully = false;
            this.mFwdChangesRequireRollback = false;
            this.mVMOrFwdSetError = 0;
            this.mVMProvidersData = new HashMap();
            this.mReadingSettingsForDefaultProvider = false;
            this.mGetOptionComplete = new Handler() { // from class: com.android.phone.settings.VoicemailSetting.VoicemailSettingFragment.2
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    if (message.what != 502) {
                        return;
                    }
                    VoicemailSettingFragment.this.handleForwardingSettingsReadResult(asyncResult, message.arg1);
                }
            };
            this.mSetOptionComplete = new Handler() { // from class: com.android.phone.settings.VoicemailSetting.VoicemailSettingFragment.3
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    int i = message.what;
                    if (i == 500) {
                        VoicemailSettingFragment voicemailSettingFragment = VoicemailSettingFragment.this;
                        voicemailSettingFragment.mVoicemailChangeResult = asyncResult;
                        voicemailSettingFragment.mVMChangeCompletedSuccesfully = voicemailSettingFragment.checkVMChangeSuccess() == null;
                        if (VoicemailSettingFragment.this.DBG) {
                            VoicemailSettingFragment.log("VM change complete msg, VM change done = " + String.valueOf(VoicemailSettingFragment.this.mVMChangeCompletedSuccesfully));
                        }
                    } else {
                        if (i != 501) {
                            return;
                        }
                        VoicemailSettingFragment.this.mForwardingChangeResults.put(Integer.valueOf(message.arg1), asyncResult);
                        Throwable th = asyncResult.exception;
                        VoicemailSettingFragment voicemailSettingFragment2 = VoicemailSettingFragment.this;
                        if (th != null) {
                            if (voicemailSettingFragment2.DBG) {
                                VoicemailSettingFragment.log("Error in setting fwd# " + message.arg1 + ": " + asyncResult.exception.getMessage());
                            }
                        } else if (voicemailSettingFragment2.DBG) {
                            VoicemailSettingFragment.log("Success in setting fwd# " + message.arg1);
                        }
                        if (!VoicemailSettingFragment.this.checkForwardingCompleted()) {
                            return;
                        }
                        String strCheckFwdChangeSuccess = VoicemailSettingFragment.this.checkFwdChangeSuccess();
                        VoicemailSettingFragment voicemailSettingFragment3 = VoicemailSettingFragment.this;
                        if (strCheckFwdChangeSuccess == null) {
                            if (voicemailSettingFragment3.DBG) {
                                VoicemailSettingFragment.log("Overall fwd changes completed ok, starting vm change");
                            }
                            VoicemailSettingFragment.this.setVMNumberWithCarrier();
                            return;
                        }
                        if (voicemailSettingFragment3.DBG) {
                            VoicemailSettingFragment.log("Overall fwd changes completed, failure");
                        }
                        VoicemailSettingFragment voicemailSettingFragment4 = VoicemailSettingFragment.this;
                        voicemailSettingFragment4.mFwdChangesRequireRollback = false;
                        Iterator it = voicemailSettingFragment4.mForwardingChangeResults.entrySet().iterator();
                        while (it.hasNext()) {
                            if (((AsyncResult) ((Map.Entry) it.next()).getValue()).exception == null) {
                                if (VoicemailSettingFragment.this.DBG) {
                                    VoicemailSettingFragment.log("Rollback will be required");
                                }
                                VoicemailSettingFragment.this.mFwdChangesRequireRollback = true;
                                break;
                            }
                        }
                    }
                    if (VoicemailSettingFragment.this.DBG) {
                        VoicemailSettingFragment.log("All VM provider related changes done");
                    }
                    if (VoicemailSettingFragment.this.mForwardingChangeResults != null) {
                        VoicemailSettingFragment.this.dismissDialogSafely(1601);
                    }
                    VoicemailSettingFragment.this.handleSetVMOrFwdMessage();
                }
            };
            this.mRevertOptionComplete = new Handler() { // from class: com.android.phone.settings.VoicemailSetting.VoicemailSettingFragment.4
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    int i = message.what;
                    if (i == 500) {
                        VoicemailSettingFragment voicemailSettingFragment = VoicemailSettingFragment.this;
                        voicemailSettingFragment.mVoicemailChangeResult = asyncResult;
                        if (voicemailSettingFragment.DBG) {
                            VoicemailSettingFragment.log("VM revert complete msg");
                        }
                    } else if (i == 501) {
                        VoicemailSettingFragment.this.mForwardingChangeResults.put(Integer.valueOf(message.arg1), asyncResult);
                        Throwable th = asyncResult.exception;
                        VoicemailSettingFragment voicemailSettingFragment2 = VoicemailSettingFragment.this;
                        if (th != null) {
                            if (voicemailSettingFragment2.DBG) {
                                VoicemailSettingFragment.log("Error in reverting fwd# " + message.arg1 + ": " + asyncResult.exception.getMessage());
                            }
                        } else if (voicemailSettingFragment2.DBG) {
                            VoicemailSettingFragment.log("Success in reverting fwd# " + message.arg1);
                        }
                        if (VoicemailSettingFragment.this.DBG) {
                            VoicemailSettingFragment.log("FWD revert complete msg ");
                        }
                    }
                    VoicemailSettingFragment voicemailSettingFragment3 = VoicemailSettingFragment.this;
                    if (voicemailSettingFragment3.mVMChangeCompletedSuccesfully && voicemailSettingFragment3.mVoicemailChangeResult == null) {
                        return;
                    }
                    if (!voicemailSettingFragment3.mFwdChangesRequireRollback || voicemailSettingFragment3.checkForwardingCompleted()) {
                        if (VoicemailSettingFragment.this.DBG) {
                            VoicemailSettingFragment.log("All VM reverts done");
                        }
                        VoicemailSettingFragment.this.dismissDialogSafely(1603);
                        VoicemailSettingFragment.this.onRevertDone();
                    }
                }
            };
        }

        class VoiceMailProvider {
            public Intent intent;
            public String name;

            public VoiceMailProvider(VoicemailSettingFragment voicemailSettingFragment, String str, Intent intent) {
                this.name = str;
                this.intent = intent;
            }
        }

        public class VoiceMailProviderSettings {
            public CallForwardInfo[] forwardingSettings;
            public String voicemailNumber;

            public VoiceMailProviderSettings(VoicemailSettingFragment voicemailSettingFragment, String str, String str2, int i) {
                this.voicemailNumber = str;
                if (str2 == null || str2.length() == 0) {
                    this.forwardingSettings = VoicemailSetting.FWD_SETTINGS_DONT_TOUCH;
                    return;
                }
                this.forwardingSettings = new CallForwardInfo[VoicemailSetting.FORWARDING_SETTINGS_REASONS.length];
                for (int i2 = 0; i2 < this.forwardingSettings.length; i2++) {
                    CallForwardInfo callForwardInfo = new CallForwardInfo();
                    this.forwardingSettings[i2] = callForwardInfo;
                    int i3 = VoicemailSetting.FORWARDING_SETTINGS_REASONS[i2];
                    callForwardInfo.reason = i3;
                    callForwardInfo.status = i3 == 0 ? 0 : 1;
                    callForwardInfo.serviceClass = 1;
                    callForwardInfo.toa = 145;
                    callForwardInfo.number = str2;
                    callForwardInfo.timeSeconds = i;
                }
            }

            public VoiceMailProviderSettings(VoicemailSettingFragment voicemailSettingFragment, String str, CallForwardInfo[] callForwardInfoArr) {
                this.voicemailNumber = str;
                this.forwardingSettings = callForwardInfoArr;
            }

            public boolean equals(Object obj) {
                if (obj == null || !(obj instanceof VoiceMailProviderSettings)) {
                    return false;
                }
                VoiceMailProviderSettings voiceMailProviderSettings = (VoiceMailProviderSettings) obj;
                String str = this.voicemailNumber;
                return ((str == null && voiceMailProviderSettings.voicemailNumber == null) || (str != null && str.equals(voiceMailProviderSettings.voicemailNumber))) && forwardingSettingsEqual(this.forwardingSettings, voiceMailProviderSettings.forwardingSettings);
            }

            private boolean forwardingSettingsEqual(CallForwardInfo[] callForwardInfoArr, CallForwardInfo[] callForwardInfoArr2) {
                if (callForwardInfoArr == callForwardInfoArr2) {
                    return true;
                }
                if (callForwardInfoArr == null || callForwardInfoArr2 == null || callForwardInfoArr.length != callForwardInfoArr2.length) {
                    return false;
                }
                for (int i = 0; i < callForwardInfoArr.length; i++) {
                    CallForwardInfo callForwardInfo = callForwardInfoArr[i];
                    CallForwardInfo callForwardInfo2 = callForwardInfoArr2[i];
                    if (callForwardInfo.status != callForwardInfo2.status || callForwardInfo.reason != callForwardInfo2.reason || callForwardInfo.serviceClass != callForwardInfo2.serviceClass || callForwardInfo.toa != callForwardInfo2.toa || callForwardInfo.number != callForwardInfo2.number || callForwardInfo.timeSeconds != callForwardInfo2.timeSeconds) {
                        return false;
                    }
                }
                return true;
            }

            public String toString() {
                String str;
                StringBuilder sb = new StringBuilder();
                sb.append(this.voicemailNumber);
                if (this.forwardingSettings != null) {
                    str = ", " + this.forwardingSettings.toString();
                } else {
                    str = "";
                }
                sb.append(str);
                return sb.toString();
            }
        }

        public VoiceMailProviderSettings getVoiceMailProviderSettings(String str, String str2, int i) {
            if (this.mVoiceMailProviderSettings == null) {
                this.mVoiceMailProviderSettings = new VoiceMailProviderSettings(this, str, str2, i);
            }
            return this.mVoiceMailProviderSettings;
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            this.mForeground = false;
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference != this.mSubMenuVoicemailSettings || preference.getIntent() == null) {
                return false;
            }
            if (this.DBG) {
                log("Invoking cfg intent " + preference.getIntent().getPackage());
            }
            startActivityForResult(preference.getIntent(), 2);
            return true;
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            if (preference == this.mVoicemailProviders) {
                String currentVoicemailProviderKey = getCurrentVoicemailProviderKey();
                String str = (String) obj;
                if (this.DBG) {
                    log("VM provider changes to " + str + " from " + this.mPreviousVMProviderKey);
                }
                if (this.mPreviousVMProviderKey.equals(str)) {
                    if (this.DBG) {
                        log("No change ");
                    }
                    return true;
                }
                updateVMPreferenceWidgets(str);
                this.mPreviousVMProviderKey = currentVoicemailProviderKey;
                VoiceMailProviderSettings voiceMailProviderSettingsLoadSettingsForVoiceMailProvider = loadSettingsForVoiceMailProvider(str);
                boolean z = this.DBG;
                if (voiceMailProviderSettingsLoadSettingsForVoiceMailProvider == null) {
                    if (z) {
                        log("Saved preferences not found - invoking config");
                    }
                    this.mVMProviderSettingsForced = true;
                    simulatePreferenceClick(this.mSubMenuVoicemailSettings);
                } else {
                    if (z) {
                        log("Saved preferences found - switching to them");
                    }
                    this.mChangingVMorFwdDueToProviderChange = true;
                    saveVoiceMailAndForwardingNumber(str, voiceMailProviderSettingsLoadSettingsForVoiceMailProvider);
                }
            } else if (preference == this.mVoicemailNotificationVibrate) {
                String preferenceKey = MiuiPhoneUtils.getPreferenceKey("button_voicemail_notification_vibrate_key", this.mPhone.getPhoneId());
                SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mPhone.getContext()).edit();
                editorEdit.putBoolean(preferenceKey, ((Boolean) obj).booleanValue());
                editorEdit.commit();
            }
            return true;
        }

        @Override // com.android.phone.settings.EditPhoneNumberPreference.OnDialogClosedListener
        public void onDialogClosed(EditPhoneNumberPreference editPhoneNumberPreference, int i) {
            if (this.DBG) {
                log("onPreferenceClick: request preference click on dialog close: " + i);
            }
            if (i == -2 || editPhoneNumberPreference == null || editPhoneNumberPreference != this.mSubMenuVoicemailSettings) {
                return;
            }
            handleVMBtnClickRequest();
        }

        @Override // com.android.phone.settings.EditPhoneNumberPreference.GetDefaultNumberListener
        public String onGetDefaultNumber(EditPhoneNumberPreference editPhoneNumberPreference) {
            if (editPhoneNumberPreference == this.mSubMenuVoicemailSettings) {
                if (this.DBG) {
                    log("updating default for voicemail dialog");
                }
                updateVoiceNumberField();
                return null;
            }
            String voiceMailNumber = this.mPhone.getVoiceMailNumber();
            if (TextUtils.isEmpty(voiceMailNumber)) {
                return null;
            }
            if (this.DBG) {
                log("updating default for call forwarding dialogs");
            }
            return getString(R.string.voicemail_abbreviated) + " " + voiceMailNumber;
        }

        private void switchToPreviousVoicemailProvider() {
            if (this.DBG) {
                log("switchToPreviousVoicemailProvider " + this.mPreviousVMProviderKey);
            }
            if (this.mPreviousVMProviderKey != null) {
                if (this.mVMChangeCompletedSuccesfully || this.mFwdChangesRequireRollback) {
                    showDialogIfForeground(1603);
                    VoiceMailProviderSettings voiceMailProviderSettingsLoadSettingsForVoiceMailProvider = loadSettingsForVoiceMailProvider(this.mPreviousVMProviderKey);
                    if (this.mVMChangeCompletedSuccesfully) {
                        this.mNewVMNumber = voiceMailProviderSettingsLoadSettingsForVoiceMailProvider.voicemailNumber;
                        Phone phone = this.mPhone;
                        phone.setVoiceMailNumber(phone.getVoiceMailAlphaTag().toString(), this.mNewVMNumber, Message.obtain(this.mRevertOptionComplete, ResponseObject.RELAY_FAILED_CODE));
                    }
                    if (this.mFwdChangesRequireRollback) {
                        if (this.DBG) {
                            log("have to revert fwd");
                        }
                        CallForwardInfo[] callForwardInfoArr = voiceMailProviderSettingsLoadSettingsForVoiceMailProvider == null ? null : voiceMailProviderSettingsLoadSettingsForVoiceMailProvider.forwardingSettings;
                        if (callForwardInfoArr != null) {
                            Map map = this.mForwardingChangeResults;
                            resetForwardingChangeState();
                            for (int i = 0; i < callForwardInfoArr.length; i++) {
                                CallForwardInfo callForwardInfo = callForwardInfoArr[i];
                                if (this.DBG) {
                                    log("Reverting fwd #: " + i + ": " + callForwardInfo.toString());
                                }
                                AsyncResult asyncResult = (AsyncResult) map.get(Integer.valueOf(callForwardInfo.reason));
                                if (asyncResult != null && asyncResult.exception == null) {
                                    this.mExpectedChangeResultReasons.add(Integer.valueOf(callForwardInfo.reason));
                                    this.mPhone.setCallForwardingOption(callForwardInfo.status == 1 ? 3 : 0, callForwardInfo.reason, callForwardInfo.number, callForwardInfo.timeSeconds, this.mRevertOptionComplete.obtainMessage(501, i, 0));
                                }
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (this.DBG) {
                    log("No need to revert");
                }
                onRevertDone();
            }
        }

        void onRevertDone() {
            if (this.DBG) {
                log("Flipping provider key back to " + this.mPreviousVMProviderKey);
            }
            this.mVoicemailProviders.setValue(this.mPreviousVMProviderKey);
            updateVMPreferenceWidgets(this.mPreviousVMProviderKey);
            updateVoiceNumberField();
            int i = this.mVMOrFwdSetError;
            if (i != 0) {
                showVMDialog(i);
                this.mVMOrFwdSetError = 0;
            }
        }

        private void handleVMBtnClickRequest() {
            saveVoiceMailAndForwardingNumber(getCurrentVoicemailProviderKey(), new VoiceMailProviderSettings(this, this.mSubMenuVoicemailSettings.getPhoneNumber(), VoicemailSetting.FWD_SETTINGS_DONT_TOUCH));
        }

        private void showDialogIfForeground(int i) {
            if (!this.mForeground || getActivity() == null) {
                return;
            }
            getActivity().showDialog(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dismissDialogSafely(int i) {
            try {
                if (getActivity() != null) {
                    getActivity().dismissDialog(i);
                }
            } catch (IllegalArgumentException unused) {
            }
        }

        private void saveVoiceMailAndForwardingNumber(String str, VoiceMailProviderSettings voiceMailProviderSettings) {
            String str2 = voiceMailProviderSettings.voicemailNumber;
            this.mNewVMNumber = str2;
            if (str2 == null) {
                this.mNewVMNumber = "";
            }
            this.mNewFwdSettings = voiceMailProviderSettings.forwardingSettings;
            if (this.mPhone.getPhoneType() == 2) {
                if (this.DBG) {
                    log("ignoring forwarding setting since this is CDMA phone");
                }
                this.mNewFwdSettings = VoicemailSetting.FWD_SETTINGS_DONT_TOUCH;
                if ("*86".equals(this.mOldVmNumber) && this.mNewVMNumber.isEmpty()) {
                    showVMDialog(700);
                    return;
                }
            }
            if (this.mNewVMNumber.equals(this.mOldVmNumber) && this.mNewFwdSettings == VoicemailSetting.FWD_SETTINGS_DONT_TOUCH) {
                showVMDialog(700);
                return;
            }
            maybeSaveSettingsForVoicemailProvider(str, voiceMailProviderSettings);
            this.mVMChangeCompletedSuccesfully = false;
            this.mFwdChangesRequireRollback = false;
            this.mVMOrFwdSetError = 0;
            if (!str.equals(this.mPreviousVMProviderKey)) {
                this.mReadingSettingsForDefaultProvider = this.mPreviousVMProviderKey.equals("");
                if (this.DBG) {
                    log("Reading current forwarding settings");
                }
                this.mForwardingReadResults = new CallForwardInfo[VoicemailSetting.FORWARDING_SETTINGS_REASONS.length];
                int i = 0;
                while (true) {
                    int[] iArr = VoicemailSetting.FORWARDING_SETTINGS_REASONS;
                    if (i < iArr.length) {
                        this.mForwardingReadResults[i] = null;
                        this.mPhone.getCallForwardingOption(iArr[i], this.mGetOptionComplete.obtainMessage(502, i, 0));
                        i++;
                    } else {
                        showDialogIfForeground(1602);
                        return;
                    }
                }
            } else {
                saveVoiceMailAndForwardingNumberStage2();
            }
        }

        void handleForwardingSettingsReadResult(AsyncResult asyncResult, int i) {
            Throwable th;
            if (this.DBG) {
                Log.d("VoicemailSetting", "handleForwardingSettingsReadResult: " + i);
            }
            CallForwardInfo callForwardInfo = null;
            if (asyncResult.exception != null) {
                if (this.DBG) {
                    Log.d("VoicemailSetting", "FwdRead: ar.exception=" + asyncResult.exception.getMessage());
                }
                th = asyncResult.exception;
            } else {
                th = null;
            }
            if (asyncResult.userObj instanceof Throwable) {
                if (this.DBG) {
                    Log.d("VoicemailSetting", "FwdRead: userObj=" + ((Throwable) asyncResult.userObj).getMessage());
                }
                th = (Throwable) asyncResult.userObj;
            }
            if (this.mForwardingReadResults == null) {
                if (this.DBG) {
                    Log.d("VoicemailSetting", "ignoring fwd reading result: " + i);
                    return;
                }
                return;
            }
            if (th != null) {
                if (this.DBG) {
                    Log.d("VoicemailSetting", "Error discovered for fwd read : " + i);
                }
                this.mForwardingReadResults = null;
                dismissDialogSafely(1602);
                showVMDialog(402);
                return;
            }
            for (CallForwardInfo callForwardInfo2 : (CallForwardInfo[]) asyncResult.result) {
                if ((callForwardInfo2.serviceClass & 1) != 0) {
                    callForwardInfo = callForwardInfo2;
                    break;
                }
            }
            if (callForwardInfo == null) {
                if (this.DBG) {
                    Log.d("VoicemailSetting", "Creating default info for " + i);
                }
                callForwardInfo = new CallForwardInfo();
                callForwardInfo.status = 0;
                callForwardInfo.reason = VoicemailSetting.FORWARDING_SETTINGS_REASONS[i];
                callForwardInfo.serviceClass = 1;
            } else {
                String str = callForwardInfo.number;
                if (str == null || str.length() == 0) {
                    callForwardInfo.status = 0;
                }
                if (this.DBG) {
                    Log.d("VoicemailSetting", "Got  " + callForwardInfo.toString() + " for " + i);
                }
            }
            this.mForwardingReadResults[i] = callForwardInfo;
            int i2 = 0;
            while (true) {
                CallForwardInfo[] callForwardInfoArr = this.mForwardingReadResults;
                if (i2 < callForwardInfoArr.length) {
                    if (callForwardInfoArr[i2] == null) {
                        if (this.DBG) {
                            Log.d("VoicemailSetting", "Not done receiving fwd info");
                            return;
                        }
                        return;
                    }
                    i2++;
                } else {
                    if (this.DBG) {
                        Log.d("VoicemailSetting", "Done receiving fwd info");
                    }
                    dismissDialogSafely(1602);
                    if (this.mReadingSettingsForDefaultProvider) {
                        maybeSaveSettingsForVoicemailProvider("", new VoiceMailProviderSettings(this, this.mOldVmNumber, this.mForwardingReadResults));
                        this.mReadingSettingsForDefaultProvider = false;
                    }
                    saveVoiceMailAndForwardingNumberStage2();
                    return;
                }
            }
        }

        private CallForwardInfo infoForReason(CallForwardInfo[] callForwardInfoArr, int i) {
            if (callForwardInfoArr == null) {
                return null;
            }
            for (CallForwardInfo callForwardInfo : callForwardInfoArr) {
                if (callForwardInfo.reason == i) {
                    return callForwardInfo;
                }
            }
            return null;
        }

        private boolean isUpdateRequired(CallForwardInfo callForwardInfo, CallForwardInfo callForwardInfo2) {
            return (callForwardInfo2.status == 0 && callForwardInfo != null && callForwardInfo.status == 0) ? false : true;
        }

        private void resetForwardingChangeState() {
            this.mForwardingChangeResults = new HashMap();
            this.mExpectedChangeResultReasons = new HashSet();
        }

        private void saveVoiceMailAndForwardingNumberStage2() {
            this.mForwardingChangeResults = null;
            this.mVoicemailChangeResult = null;
            if (this.mNewFwdSettings != VoicemailSetting.FWD_SETTINGS_DONT_TOUCH) {
                resetForwardingChangeState();
                int i = 0;
                while (true) {
                    CallForwardInfo[] callForwardInfoArr = this.mNewFwdSettings;
                    if (i < callForwardInfoArr.length) {
                        CallForwardInfo callForwardInfo = callForwardInfoArr[i];
                        if (isUpdateRequired(infoForReason(this.mForwardingReadResults, callForwardInfo.reason), callForwardInfo)) {
                            if (this.DBG) {
                                log("Setting fwd #: " + i + ": " + callForwardInfo.toString());
                            }
                            this.mExpectedChangeResultReasons.add(Integer.valueOf(i));
                            Phone phone = this.mPhone;
                            int i2 = callForwardInfo.status == 1 ? 3 : 0;
                            int i3 = callForwardInfo.reason;
                            phone.setCallForwardingOption(i2, i3, callForwardInfo.number, callForwardInfo.timeSeconds, this.mSetOptionComplete.obtainMessage(501, i3, 0));
                        }
                        i++;
                    } else {
                        showDialogIfForeground(1601);
                        return;
                    }
                }
            } else {
                if (this.DBG) {
                    log("Not touching fwd #");
                }
                setVMNumberWithCarrier();
            }
        }

        void setVMNumberWithCarrier() {
            Phone phone = this.mPhone;
            phone.setVoiceMailNumber(phone.getVoiceMailAlphaTag().toString(), this.mNewVMNumber, Message.obtain(this.mSetOptionComplete, ResponseObject.RELAY_FAILED_CODE));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean checkForwardingCompleted() {
            if (this.mForwardingChangeResults == null) {
                return true;
            }
            Iterator it = this.mExpectedChangeResultReasons.iterator();
            while (it.hasNext()) {
                if (this.mForwardingChangeResults.get((Integer) it.next()) == null) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String checkFwdChangeSuccess() {
            Iterator it = this.mForwardingChangeResults.entrySet().iterator();
            while (it.hasNext()) {
                Throwable th = ((AsyncResult) ((Map.Entry) it.next()).getValue()).exception;
                if (th != null) {
                    String message = th.getMessage();
                    return message == null ? "" : message;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String checkVMChangeSuccess() {
            Throwable th = this.mVoicemailChangeResult.exception;
            if (th == null) {
                return null;
            }
            String message = th.getMessage();
            return message == null ? "" : message;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code duplicated, block: B:16:0x0026 A[PHI: r0
  0x0026: PHI (r0v5 java.lang.String) = (r0v4 java.lang.String), (r0v14 java.lang.String) binds: [B:12:0x001d, B:14:0x0023] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Code duplicated, block: B:18:0x0029  */
        /* JADX WARN: Code duplicated, block: B:20:0x002d  */
        /* JADX WARN: Code duplicated, block: B:23:0x003b A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:24:0x003d  */
        /* JADX WARN: Code duplicated, block: B:26:0x0054  */
        /* JADX WARN: Instruction removed from duplicated block: B:24:0x003d, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:26:0x0054, please report this as an issue */
        public void handleSetVMOrFwdMessage() {
            String strCheckVMChangeSuccess;
            boolean z;
            if (this.DBG) {
                log("handleSetVMMessage: set VM request complete");
            }
            boolean z2 = true;
            if (this.mForwardingChangeResults == null) {
                strCheckVMChangeSuccess = "";
            } else {
                strCheckVMChangeSuccess = checkFwdChangeSuccess();
                if (strCheckVMChangeSuccess != null) {
                    z = true;
                    z2 = false;
                }
                if ((z2 || (strCheckVMChangeSuccess = checkVMChangeSuccess()) == null) ? z2 : false) {
                    if (this.DBG) {
                        log("change VM success!");
                    }
                    handleVMAndFwdSetSuccess(600);
                    updateVoiceNumberField();
                    return;
                }
                if (z) {
                    log("change FW failed: " + strCheckVMChangeSuccess);
                    handleVMOrFwdSetError(a.b);
                    return;
                }
                log("change VM failed: " + strCheckVMChangeSuccess);
                handleVMOrFwdSetError(400);
            }
            z = false;
            if ((z2 || (strCheckVMChangeSuccess = checkVMChangeSuccess()) == null) ? z2 : false) {
                if (this.DBG) {
                    log("change VM success!");
                }
                handleVMAndFwdSetSuccess(600);
                updateVoiceNumberField();
                return;
            }
            if (z) {
                log("change FW failed: " + strCheckVMChangeSuccess);
                handleVMOrFwdSetError(a.b);
                return;
            }
            log("change VM failed: " + strCheckVMChangeSuccess);
            handleVMOrFwdSetError(400);
        }

        private void handleVMOrFwdSetError(int i) {
            if (this.mChangingVMorFwdDueToProviderChange) {
                this.mVMOrFwdSetError = i;
                this.mChangingVMorFwdDueToProviderChange = false;
                switchToPreviousVoicemailProvider();
            } else {
                this.mChangingVMorFwdDueToProviderChange = false;
                showVMDialog(i);
                updateVoiceNumberField();
            }
        }

        private void handleVMAndFwdSetSuccess(int i) {
            this.mChangingVMorFwdDueToProviderChange = false;
            showVMDialog(i);
        }

        private void updateVoiceNumberField() {
            if (this.mSubMenuVoicemailSettings == null) {
                return;
            }
            String voiceMailNumber = this.mPhone.getVoiceMailNumber();
            this.mOldVmNumber = voiceMailNumber;
            if (voiceMailNumber == null) {
                this.mOldVmNumber = "";
            }
            this.mSubMenuVoicemailSettings.setPhoneNumber(this.mOldVmNumber);
            this.mSubMenuVoicemailSettings.setSummary(this.mOldVmNumber.length() > 0 ? Utils.localizeNumber(this.mOldVmNumber) : getString(R.string.voicemail_number_not_set));
        }

        private void showVMDialog(int i) {
            if (i == 600) {
                showDialogIfForeground(1600);
                return;
            }
            if (i != 700) {
                switch (i) {
                    case 400:
                        showDialogIfForeground(1500);
                        break;
                    case a.b /* 401 */:
                        showDialogIfForeground(1501);
                        break;
                    case 402:
                        showDialogIfForeground(1502);
                        break;
                }
                return;
            }
            showDialogIfForeground(1400);
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            super.onCreatePreferences(bundle, str);
            if (this.DBG) {
                log("Creating fragment");
            }
            this.mPhone = MiuiPhoneUtils.getPhone(SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId()));
            addPreferencesFromResource(R.xml.voicemail_setting);
            EditPhoneNumberPreference editPhoneNumberPreference = (EditPhoneNumberPreference) findPreference("button_voicemail");
            this.mSubMenuVoicemailSettings = editPhoneNumberPreference;
            if (editPhoneNumberPreference != null) {
                if (isVoiceMailNumberFixed()) {
                    this.mSubMenuVoicemailSettings.setEnabled(false);
                }
                this.mSubMenuVoicemailSettings.setIntentFragment(this, 1, this);
                this.mSubMenuVoicemailSettings.setDialogOnClosedListener(this);
                this.mSubMenuVoicemailSettings.setDialogTitle(R.string.voicemail_settings_number_label);
            }
            DropDownPreference dropDownPreference = (DropDownPreference) findPreference("button_voicemail_provider");
            this.mVoicemailProviders = dropDownPreference;
            if (dropDownPreference != null) {
                dropDownPreference.setOnPreferenceChangeListener(this);
                this.mVoicemailNotificationRingtone = findPreference("button_voicemail_notification_ringtone_key");
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("button_voicemail_notification_vibrate_key");
                this.mVoicemailNotificationVibrate = checkBoxPreference;
                checkBoxPreference.setOnPreferenceChangeListener(this);
                initVoiceMailProviders();
            }
            if (bundle == null && getActivity().getIntent().getAction() != null && getActivity().getIntent().getAction().equals("com.android.phone.CallFeaturesSetting.ADD_VOICEMAIL") && this.mVoicemailProviders != null) {
                int size = this.mVMProvidersData.size();
                DropDownPreference dropDownPreference2 = this.mVoicemailProviders;
                if (size > 1) {
                    simulatePreferenceClick(dropDownPreference2);
                } else {
                    onPreferenceChange(dropDownPreference2, "");
                    this.mVoicemailProviders.setValue("");
                }
            }
            updateVoiceNumberField();
            this.mVMProviderSettingsForced = false;
            this.mRingtoneLookupRunnable = new Runnable() { // from class: com.android.phone.settings.VoicemailSetting.VoicemailSettingFragment.5
                @Override // java.lang.Runnable
                public void run() {
                    if (VoicemailSettingFragment.this.mVoicemailNotificationRingtone != null) {
                        VoicemailSettingFragment voicemailSettingFragment = VoicemailSettingFragment.this;
                        voicemailSettingFragment.updateRingtoneName(2, voicemailSettingFragment.mVoicemailNotificationRingtone, 2);
                    }
                }
            };
            this.mVoicemailNotificationVibrate.setVisible(false);
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            this.mForeground = true;
            updateVoiceNumberField();
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mPhone.getContext());
            migrateVoicemailVibrationSettingsIfNeeded(defaultSharedPreferences, this.mPhone.getPhoneId());
            this.mVoicemailNotificationVibrate.setChecked(defaultSharedPreferences.getBoolean(MiuiPhoneUtils.getPreferenceKey("button_voicemail_notification_vibrate_key", this.mPhone.getPhoneId()), false));
            lookupRingtoneName();
        }

        public static boolean migrateVoicemailVibrationSettingsIfNeeded(SharedPreferences sharedPreferences, int i) {
            String preferenceKey = MiuiPhoneUtils.getPreferenceKey("button_voicemail_notification_vibrate_key", i);
            if (sharedPreferences.contains(preferenceKey)) {
                return false;
            }
            boolean zEquals = sharedPreferences.getString("button_voicemail_notification_vibrate_when_key", "never").equals("always");
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putBoolean(preferenceKey, zEquals);
            editorEdit.commit();
            return true;
        }

        private void lookupRingtoneName() {
            new Thread(this.mRingtoneLookupRunnable).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void log(String str) {
            Log.d("VoicemailSetting", str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateRingtoneName(int i, Preference preference, int i2) {
            if (preference == null) {
                return;
            }
            String ringtoneTitle = ExtraRingtone.getRingtoneTitle(getContext(), ExtraRingtoneManager.getRingtoneUri(getContext(), i), true);
            Handler handler = this.mRingtoneLookupComplete;
            handler.sendMessage(handler.obtainMessage(i2, ringtoneTitle));
        }

        private void updateVMPreferenceWidgets(String str) {
            VoiceMailProvider voiceMailProvider = (VoiceMailProvider) this.mVMProvidersData.get(str);
            if (voiceMailProvider == null) {
                this.mSubMenuVoicemailSettings.setEnabled(false);
                this.mSubMenuVoicemailSettings.setIntent(null);
                this.mVoicemailNotificationVibrate.setEnabled(false);
            } else {
                if (!isVoiceMailNumberFixed()) {
                    this.mSubMenuVoicemailSettings.setEnabled(true);
                }
                this.mSubMenuVoicemailSettings.setIntent(voiceMailProvider.intent);
                this.mVoicemailNotificationVibrate.setEnabled(true);
            }
        }

        private void initVoiceMailProviders() {
            String stringExtra;
            this.mPerProviderSavedVMNumbers = getContext().getApplicationContext().getSharedPreferences(MiuiPhoneUtils.getPreferenceKey("vm_numbers", this.mPhone.getPhoneId()), 0);
            if (getActivity() == null || getActivity().getIntent().getAction() == null || !getActivity().getIntent().getAction().equals("com.android.phone.CallFeaturesSetting.ADD_VOICEMAIL")) {
                stringExtra = null;
            } else {
                if (this.DBG) {
                    log("ACTION_ADD_VOICEMAIL");
                }
                stringExtra = getActivity().getIntent().hasExtra("com.android.phone.ProviderToIgnore") ? getActivity().getIntent().getStringExtra("com.android.phone.ProviderToIgnore") : null;
                if (this.DBG) {
                    log("providerToIgnore=" + stringExtra);
                }
                if (stringExtra != null) {
                    deleteSettingsForVoicemailProvider(stringExtra);
                }
            }
            this.mVMProvidersData.clear();
            String string = getString(R.string.voicemail_default);
            this.mVMProvidersData.put("", new VoiceMailProvider(this, string, null));
            PackageManager packageManager = getContext().getPackageManager();
            Intent intent = new Intent();
            intent.setAction("com.android.phone.CallFeaturesSetting.CONFIGURE_VOICEMAIL");
            List<ResolveInfo> listQueryIntentActivities = packageManager.queryIntentActivities(intent, 0);
            int i = 1;
            int size = listQueryIntentActivities.size() + 1;
            for (int i2 = 0; i2 < listQueryIntentActivities.size(); i2++) {
                ResolveInfo resolveInfo = listQueryIntentActivities.get(i2);
                ActivityInfo activityInfo = resolveInfo.activityInfo;
                String strMakeKeyForActivity = makeKeyForActivity(activityInfo);
                if (this.DBG) {
                    log("Loading " + strMakeKeyForActivity);
                }
                if (strMakeKeyForActivity.equals(stringExtra)) {
                    if (this.DBG) {
                        log("Ignoring ".concat(strMakeKeyForActivity));
                    }
                    size--;
                } else {
                    String string2 = resolveInfo.loadLabel(packageManager).toString();
                    Intent intent2 = new Intent();
                    intent2.setAction("com.android.phone.CallFeaturesSetting.CONFIGURE_VOICEMAIL");
                    intent2.setClassName(activityInfo.packageName, activityInfo.name);
                    this.mVMProvidersData.put(strMakeKeyForActivity, new VoiceMailProvider(this, string2, intent2));
                }
            }
            String[] strArr = new String[size];
            String[] strArr2 = new String[size];
            strArr[0] = string;
            strArr2[0] = "";
            for (int i3 = 0; i3 < listQueryIntentActivities.size(); i3++) {
                String strMakeKeyForActivity2 = makeKeyForActivity(listQueryIntentActivities.get(i3).activityInfo);
                if (this.mVMProvidersData.containsKey(strMakeKeyForActivity2)) {
                    strArr[i] = ((VoiceMailProvider) this.mVMProvidersData.get(strMakeKeyForActivity2)).name;
                    strArr2[i] = strMakeKeyForActivity2;
                    i++;
                }
            }
            this.mVoicemailProviders.setEntries(strArr);
            this.mVoicemailProviders.setEntryValues(strArr2);
            String currentVoicemailProviderKey = getCurrentVoicemailProviderKey();
            this.mPreviousVMProviderKey = currentVoicemailProviderKey;
            updateVMPreferenceWidgets(currentVoicemailProviderKey);
        }

        private String makeKeyForActivity(ActivityInfo activityInfo) {
            return activityInfo.name;
        }

        private void simulatePreferenceClick(Preference preference) {
            preference.performClick();
        }

        private void maybeSaveSettingsForVoicemailProvider(String str, VoiceMailProviderSettings voiceMailProviderSettings) {
            if (this.mVoicemailProviders == null) {
                return;
            }
            boolean zEquals = voiceMailProviderSettings.equals(loadSettingsForVoiceMailProvider(str));
            boolean z = this.DBG;
            if (zEquals) {
                if (z) {
                    log("Not saving setting for " + str + " since they have not changed");
                    return;
                }
                return;
            }
            if (z) {
                log("Saving settings for " + str + ": " + voiceMailProviderSettings.toString());
            }
            SharedPreferences.Editor editorEdit = this.mPerProviderSavedVMNumbers.edit();
            editorEdit.putString(str + "#VMNumber", voiceMailProviderSettings.voicemailNumber);
            String str2 = str + "#FWDSettings";
            CallForwardInfo[] callForwardInfoArr = voiceMailProviderSettings.forwardingSettings;
            if (callForwardInfoArr != VoicemailSetting.FWD_SETTINGS_DONT_TOUCH) {
                editorEdit.putInt(str2.concat("#Length"), callForwardInfoArr.length);
                for (int i = 0; i < callForwardInfoArr.length; i++) {
                    String str3 = str2 + "#Setting" + String.valueOf(i);
                    CallForwardInfo callForwardInfo = callForwardInfoArr[i];
                    editorEdit.putInt(str3.concat("#Status"), callForwardInfo.status);
                    editorEdit.putInt(str3.concat("#Reason"), callForwardInfo.reason);
                    editorEdit.putString(str3.concat("#Number"), callForwardInfo.number);
                    editorEdit.putInt(str3.concat("#Time"), callForwardInfo.timeSeconds);
                }
            } else {
                editorEdit.putInt(str2.concat("#Length"), 0);
            }
            editorEdit.apply();
        }

        private VoiceMailProviderSettings loadSettingsForVoiceMailProvider(String str) {
            String string = this.mPerProviderSavedVMNumbers.getString(str + "#VMNumber", null);
            if (string == null) {
                if (this.DBG) {
                    log("Settings for " + str + " not found");
                }
                return null;
            }
            CallForwardInfo[] callForwardInfoArr = VoicemailSetting.FWD_SETTINGS_DONT_TOUCH;
            String str2 = str + "#FWDSettings";
            int i = this.mPerProviderSavedVMNumbers.getInt(str2.concat("#Length"), 0);
            if (i > 0) {
                callForwardInfoArr = new CallForwardInfo[i];
                for (int i2 = 0; i2 < i; i2++) {
                    String str3 = str2 + "#Setting" + String.valueOf(i2);
                    CallForwardInfo callForwardInfo = new CallForwardInfo();
                    callForwardInfoArr[i2] = callForwardInfo;
                    callForwardInfo.status = this.mPerProviderSavedVMNumbers.getInt(str3.concat("#Status"), 0);
                    callForwardInfoArr[i2].reason = this.mPerProviderSavedVMNumbers.getInt(str3.concat("#Reason"), 5);
                    CallForwardInfo callForwardInfo2 = callForwardInfoArr[i2];
                    callForwardInfo2.serviceClass = 1;
                    callForwardInfo2.toa = 145;
                    callForwardInfo2.number = this.mPerProviderSavedVMNumbers.getString(str3.concat("#Number"), "");
                    callForwardInfoArr[i2].timeSeconds = this.mPerProviderSavedVMNumbers.getInt(str3.concat("#Time"), 20);
                }
            }
            VoiceMailProviderSettings voiceMailProviderSettings = new VoiceMailProviderSettings(this, string, callForwardInfoArr);
            if (this.DBG) {
                log("Loaded settings for " + str + ": " + voiceMailProviderSettings.toString());
            }
            return voiceMailProviderSettings;
        }

        private void deleteSettingsForVoicemailProvider(String str) {
            if (this.DBG) {
                log("Deleting settings for" + str);
            }
            if (this.mVoicemailProviders == null) {
                return;
            }
            this.mPerProviderSavedVMNumbers.edit().putString(str + "#VMNumber", null).putInt(str + "#FWDSettings#Length", 0).commit();
        }

        private String getCurrentVoicemailProviderKey() {
            String value = this.mVoicemailProviders.getValue();
            return value != null ? value : "";
        }

        private boolean isVoiceMailNumberFixed() {
            PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
            if (TelephonyManager.isCustForMxTelcel() || TelephonyManager.isCustForHkH3g() || isDlcLock() || isDpcLock()) {
                return true;
            }
            return (carrierConfigForSubId == null || carrierConfigForSubId.getBoolean("editable_voicemail_number_setting_bool")) ? false : true;
        }

        private boolean isDlcLock() {
            return SystemProperties.getBoolean("persist.sys.dlc.lock", false);
        }

        private boolean isDpcLock() {
            return SystemProperties.getBoolean("persist.sys.dpc.lock", false);
        }

        @Override // androidx.fragment.app.Fragment
        public void startActivityForResult(Intent intent, int i) {
            if (i == -1) {
                super.startActivityForResult(intent, i);
                return;
            }
            if (this.DBG) {
                log("startSubActivity: starting requested subactivity");
            }
            super.startActivityForResult(intent, i);
        }

        /* JADX WARN: Code duplicated, block: B:104:0x01c6  */
        /* JADX WARN: Code duplicated, block: B:114:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:41:0x00a8  */
        /* JADX WARN: Code duplicated, block: B:43:0x00ac  */
        /* JADX WARN: Code duplicated, block: B:45:0x00b3  */
        /* JADX WARN: Code duplicated, block: B:47:0x00b7  */
        /* JADX WARN: Code duplicated, block: B:49:0x00bb  */
        /* JADX WARN: Code duplicated, block: B:51:0x00c1  */
        /* JADX WARN: Code duplicated, block: B:53:0x00d5  */
        /* JADX WARN: Code duplicated, block: B:55:0x00df  */
        /* JADX WARN: Code duplicated, block: B:56:0x00e2  */
        @Override // androidx.fragment.app.Fragment
        public void onActivityResult(int i, int i2, Intent intent) {
            String string;
            String stringExtra;
            String str;
            Cursor cursorQuery = null;
            stringExtra = null;
            stringExtra = null;
            stringExtra = null;
            String stringExtra2 = null;
            boolean z = false;
            if (i == 2) {
                if (this.DBG) {
                    log("mVMProviderSettingsForced: " + this.mVMProviderSettingsForced);
                }
                boolean z2 = this.mVMProviderSettingsForced;
                this.mVMProviderSettingsForced = false;
                if (i2 != -1) {
                    if (this.DBG) {
                        log("onActivityResult: vm provider cfg result not OK.");
                    }
                } else {
                    if (intent != null) {
                        if (intent.getBooleanExtra("com.android.phone.Signout", false)) {
                            if (this.DBG) {
                                log("Provider requested signout");
                            }
                            if (z2) {
                                if (this.DBG) {
                                    log("Going back to previous provider on signout");
                                }
                                switchToPreviousVoicemailProvider();
                                return;
                            }
                            String currentVoicemailProviderKey = getCurrentVoicemailProviderKey();
                            if (this.DBG) {
                                log("Relaunching activity and ignoring " + currentVoicemailProviderKey);
                            }
                            Intent intent2 = new Intent("com.android.phone.CallFeaturesSetting.ADD_VOICEMAIL");
                            intent2.putExtra("com.android.phone.ProviderToIgnore", currentVoicemailProviderKey);
                            intent2.setFlags(67108864);
                            startActivity(intent2);
                            return;
                        }
                        stringExtra2 = intent.getStringExtra("com.android.phone.VoicemailNumber");
                        if (stringExtra2 == null || stringExtra2.length() == 0) {
                            if (this.DBG) {
                                log("onActivityResult: vm provider cfg result has no vmnum");
                            }
                        }
                        if (z) {
                            if (this.DBG) {
                                log("Failure in return from voicemail provider");
                            }
                            if (z2) {
                                switchToPreviousVoicemailProvider();
                                return;
                            } else {
                                if (this.DBG) {
                                    log("Not switching back the provider since this is not forced config");
                                    return;
                                }
                                return;
                            }
                        }
                        this.mChangingVMorFwdDueToProviderChange = z2;
                        stringExtra = intent.getStringExtra("com.android.phone.ForwardingNumber");
                        int intExtra = intent.getIntExtra("com.android.phone.ForwardingNumberTime", 20);
                        if (this.DBG) {
                            StringBuilder sb = new StringBuilder("onActivityResult: vm provider cfg result ");
                            if (stringExtra != null) {
                                str = "has";
                            } else {
                                str = " does not have";
                            }
                            sb.append(str);
                            sb.append(" forwarding number");
                            log(sb.toString());
                        }
                        saveVoiceMailAndForwardingNumber(getCurrentVoicemailProviderKey(), getVoiceMailProviderSettings(stringExtra2, stringExtra, intExtra));
                        return;
                    }
                    if (this.DBG) {
                        log("onActivityResult: vm provider cfg result has no data");
                    }
                }
                z = true;
                if (z) {
                    if (this.DBG) {
                        log("Failure in return from voicemail provider");
                    }
                    if (z2) {
                        switchToPreviousVoicemailProvider();
                        return;
                    } else {
                        if (this.DBG) {
                            log("Not switching back the provider since this is not forced config");
                            return;
                        }
                        return;
                    }
                }
                this.mChangingVMorFwdDueToProviderChange = z2;
                stringExtra = intent.getStringExtra("com.android.phone.ForwardingNumber");
                int intExtra2 = intent.getIntExtra("com.android.phone.ForwardingNumberTime", 20);
                if (this.DBG) {
                    StringBuilder sb2 = new StringBuilder("onActivityResult: vm provider cfg result ");
                    if (stringExtra != null) {
                        str = "has";
                    } else {
                        str = " does not have";
                    }
                    sb2.append(str);
                    sb2.append(" forwarding number");
                    log(sb2.toString());
                }
                saveVoiceMailAndForwardingNumber(getCurrentVoicemailProviderKey(), getVoiceMailProviderSettings(stringExtra2, stringExtra, intExtra2));
                return;
            }
            if (i2 != -1) {
                if (this.DBG) {
                    log("onActivityResult: contact picker result not OK.");
                    return;
                }
                return;
            }
            try {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                ComponentCaller currentCaller = activity.getCurrentCaller();
                if (currentCaller == null) {
                    Log.e("VoicemailSetting", "onActivityResult: Current caller is null, cannot check permissions.");
                    return;
                }
                Uri data = intent != null ? intent.getData() : null;
                if (data == null) {
                    Log.w("VoicemailSetting", "onActivityResult: Intent data or contact URI is null.");
                    return;
                }
                if (currentCaller.checkContentUriPermission(data, 1) == -1) {
                    EventLog.writeEvent(1397638484, "337785563", Integer.valueOf(currentCaller.getUid()), "Permission denied, cannot access contact");
                    throw new SecurityException(String.format("Permission denial: Caller (uid=%d, pkg=%s) lacks specific permission grant %s to access contact URI %s.", Integer.valueOf(currentCaller.getUid()), currentCaller.getPackage(), "FLAG_GRANT_READ_URI_PERMISSION", data));
                }
                Uri data2 = intent.getData();
                if (!data2.getScheme().equals(GenericURI.TEL)) {
                    cursorQuery = getContext().getContentResolver().query(intent.getData(), VoicemailSetting.NUM_PROJECTION, null, null, null);
                    if (cursorQuery != null && cursorQuery.moveToFirst()) {
                        string = cursorQuery.getString(0);
                    }
                    log("onActivityResult: bad contact data, no results found.");
                    if (cursorQuery != null) {
                        cursorQuery.close();
                        return;
                    }
                    return;
                }
                string = data2.getSchemeSpecificPart();
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                if (i != 1) {
                    return;
                }
                this.mSubMenuVoicemailSettings.onPickActivityResult(string);
            } catch (IllegalArgumentException unused) {
                if (0 != 0) {
                    cursorQuery.close();
                }
                string = "";
            } catch (Exception unused2) {
                if (0 != 0) {
                    cursorQuery.close();
                }
                string = "";
            } catch (Throwable th) {
                if (0 == 0) {
                    throw th;
                }
                cursorQuery.close();
                throw th;
            }
        }
    }
}
