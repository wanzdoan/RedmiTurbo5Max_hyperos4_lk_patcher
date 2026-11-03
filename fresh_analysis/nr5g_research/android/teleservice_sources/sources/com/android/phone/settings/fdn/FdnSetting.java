package com.android.phone.settings.fdn;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.Phone;
import com.android.phone.CallFeaturesSetting;
import com.android.phone.PhoneGlobals;
import com.android.phone.PhoneUtils;
import com.android.phone.R;
import com.android.phone.SubscriptionInfoHelper;
import com.mediatek.settings.CallSettingUtils;
import com.mediatek.settings.TelephonyUtils;
import com.xiaomi.mirilhook.MiRilHook;

/* JADX INFO: loaded from: classes.dex */
public class FdnSetting extends PreferenceActivity implements EditPinPreference.OnPinEnteredListener, FdnErrorDialogFragment.Listener, PhoneGlobals.SubInfoUpdateListener, Preference.OnPreferenceClickListener {
    private EditPinPreference mButtonChangePin2;
    private EditPinPreference mButtonEnableFDN;
    private Preference mButtonFDNList;
    private boolean mIsPuk2Locked;
    private String mNewPin;
    private String mOldPin;
    private Phone mPhone;
    private int mPinChangeState;
    private String mPuk2;
    private SubscriptionInfoHelper mSubscriptionInfoHelper;
    private final Handler mFDNHandler = new Handler() { // from class: com.android.phone.settings.fdn.FdnSetting.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 100) {
                FdnSetting.this.log("Handle EVENT_PIN2_ENTRY_COMPLETE");
                CommandException commandException = ((AsyncResult) message.obj).exception;
                if (commandException != null) {
                    if (commandException instanceof CommandException) {
                        int i2 = message.arg1;
                        int i3 = AnonymousClass3.$SwitchMap$com$android$internal$telephony$CommandException$Error[commandException.getCommandError().ordinal()];
                        if (i3 == 1) {
                            FdnSetting.this.showFdnErrorDialog(11, 0, 0);
                        } else {
                            FdnSetting fdnSetting = FdnSetting.this;
                            if (i3 == 2) {
                                fdnSetting.showFdnErrorDialog(15, R.string.pin2_invalid, i2);
                            } else {
                                fdnSetting.showFdnErrorDialog(16, R.string.fdn_failed, i2);
                            }
                        }
                    } else {
                        FdnSetting.this.displayMessage(R.string.pin2_error_exception);
                    }
                }
                FdnSetting fdnSetting2 = FdnSetting.this;
                fdnSetting2.updateWholeScreen(fdnSetting2.mSubId, true);
                return;
            }
            if (i == 200 || i == 300) {
                FdnSetting.this.log("Handle EVENT_PIN2_CHANGE_COMPLETE");
                AsyncResult asyncResult = (AsyncResult) message.obj;
                Throwable th = asyncResult.exception;
                if (th != null) {
                    if (th instanceof CommandException) {
                        int i4 = message.arg1;
                        FdnSetting.this.log("Handle EVENT_PIN2_CHANGE_COMPLETE attemptsRemaining=" + i4);
                        CommandException commandException2 = asyncResult.exception;
                        if (FdnSetting.this.handleChangePIN2ErrorForMTK(commandException2)) {
                            FdnSetting.this.log("Handle handleChangePIN2ErrorForMTK Enter~");
                        } else {
                            CommandException.Error commandError = commandException2.getCommandError();
                            CommandException.Error error = CommandException.Error.SIM_PUK2;
                            FdnSetting fdnSetting3 = FdnSetting.this;
                            if (commandError == error) {
                                fdnSetting3.showFdnErrorDialog(12, 0, 0);
                            } else if (fdnSetting3.mIsPuk2Locked && i4 == 0) {
                                FdnSetting.this.showFdnErrorDialog(10, 0, 0);
                            } else {
                                boolean z = FdnSetting.this.mIsPuk2Locked;
                                FdnSetting fdnSetting4 = FdnSetting.this;
                                if (!z) {
                                    fdnSetting4.showFdnErrorDialog(13, R.string.badPin2, i4);
                                    FdnSetting.this.resetPinChangeState();
                                } else {
                                    fdnSetting4.showFdnErrorDialog(13, R.string.badPuk2, i4);
                                    FdnSetting.this.resetPinChangeStateForPUK2();
                                }
                            }
                        }
                    } else {
                        FdnSetting.this.displayMessage(R.string.pin2_error_exception);
                    }
                } else {
                    int i5 = FdnSetting.this.mPinChangeState;
                    FdnSetting fdnSetting5 = FdnSetting.this;
                    if (i5 == 3) {
                        fdnSetting5.displayMessage(R.string.pin2_unblocked);
                    } else {
                        fdnSetting5.displayMessage(R.string.pin2_changed);
                    }
                    if (message.what == 300) {
                        FdnSetting.this.log("Handle EVENT_PIN2_CHANGE_COMPLETE_TOGGLE_FDN");
                        FdnSetting.this.toggleFDNEnable(true);
                    }
                    FdnSetting fdnSetting6 = FdnSetting.this;
                    fdnSetting6.resetPinChangeState(fdnSetting6.mSubId, true);
                }
                FdnSetting fdnSetting7 = FdnSetting.this;
                fdnSetting7.updateWholeScreen(fdnSetting7.mSubId, true);
                FdnSetting.this.mButtonChangePin2.setText("");
                FdnSetting.this.mButtonEnableFDN.setText("");
            }
        }
    };
    private int mSubId = -1;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.fdn.FdnSetting.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("mediatek.intent.action.PHB_STATE_CHANGED".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("subscription", -1);
                FdnSetting.this.log("PHB_STATE_CHANGED, subId=" + intExtra + ", mSubId=" + FdnSetting.this.mSubId);
                if (intExtra == FdnSetting.this.mSubId) {
                    FdnSetting fdnSetting = FdnSetting.this;
                    fdnSetting.updateWholeScreen(fdnSetting.mSubId, false);
                    return;
                }
                return;
            }
            if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                int intExtra2 = intent.getIntExtra("subscription", -1);
                FdnSetting.this.log("SIM_STATE_CHANGED, subId=" + intExtra2 + ", mSubId=" + FdnSetting.this.mSubId);
                if (intExtra2 == FdnSetting.this.mSubId) {
                    FdnSetting fdnSetting2 = FdnSetting.this;
                    fdnSetting2.updateWholeScreen(fdnSetting2.mSubId, false);
                    return;
                }
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                FdnSetting.this.log("AIRPLANE_MODE_CHANGED, so finish FDN Settings");
                FdnSetting.this.finish();
            }
        }
    };

    @Override // com.android.phone.settings.fdn.EditPinPreference.OnPinEnteredListener
    public void onPinEntered(EditPinPreference editPinPreference, boolean z) {
        if (editPinPreference == this.mButtonEnableFDN && (!this.mIsPuk2Locked || !z)) {
            toggleFDNEnable(z);
        } else {
            updatePINChangeState(editPinPreference, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleFDNEnable(boolean z) {
        log("[toggleFDNEnable] positiveResult: " + z);
        updateEnableFDNDialog();
        if (!z) {
            if (!this.mIsPuk2Locked) {
                resetPinChangeState();
                return;
            } else {
                resetPinChangeStateForPUK2();
                return;
            }
        }
        String text = this.mButtonEnableFDN.getText();
        if (validatePin(text, false)) {
            boolean iccFdnEnabled = this.mPhone.getIccCard().getIccFdnEnabled();
            Message messageObtainMessage = this.mFDNHandler.obtainMessage(100);
            log("[toggleFDNEnable] isEnabled: " + iccFdnEnabled);
            this.mPhone.getIccCard().setIccFdnEnabled(iccFdnEnabled ^ true, text, messageObtainMessage);
        } else {
            displayMessage(R.string.invalidPin2);
        }
        this.mButtonEnableFDN.setText("");
    }

    private void updatePINChangeState(EditPinPreference editPinPreference, boolean z) {
        Message messageObtainMessage;
        log("updatePINChangeState positive=" + z + " mPinChangeState=" + this.mPinChangeState + " mIsPuk2Locked=" + this.mIsPuk2Locked);
        if (!z) {
            boolean z2 = this.mIsPuk2Locked;
            int i = this.mSubId;
            if (!z2) {
                resetPinChangeState(i, true);
                return;
            } else {
                resetPinChangeStateForPUK2(i, true);
                return;
            }
        }
        int i2 = this.mPinChangeState;
        if (i2 == 0) {
            this.mOldPin = editPinPreference.getText();
            editPinPreference.setText("");
            if (validatePin(this.mOldPin, false)) {
                this.mPinChangeState = 1;
                displayPinChangeDialog(editPinPreference);
                return;
            } else {
                displayPinChangeDialog(editPinPreference, R.string.invalidPin2, true);
                return;
            }
        }
        if (i2 == 1) {
            this.mNewPin = editPinPreference.getText();
            editPinPreference.setText("");
            if (validatePin(this.mNewPin, false)) {
                this.mPinChangeState = 2;
                displayPinChangeDialog(editPinPreference);
                return;
            } else {
                displayPinChangeDialog(editPinPreference, R.string.invalidPin2, true);
                return;
            }
        }
        if (i2 == 2) {
            if (validatePin(editPinPreference.getText(), false)) {
                if (!this.mNewPin.equals(editPinPreference.getText())) {
                    this.mPinChangeState = 1;
                    editPinPreference.setText("");
                    displayPinChangeDialog(editPinPreference, R.string.mismatchPin2, true);
                    return;
                }
                editPinPreference.setText("");
                Message messageObtainMessage2 = this.mFDNHandler.obtainMessage(200);
                boolean z3 = this.mIsPuk2Locked;
                Phone phone = this.mPhone;
                if (!z3) {
                    phone.getIccCard().changeIccFdnPassword(this.mOldPin, this.mNewPin, messageObtainMessage2);
                    return;
                } else {
                    phone.getIccCard().supplyPuk2(this.mPuk2, this.mNewPin, messageObtainMessage2);
                    return;
                }
            }
            editPinPreference.setText("");
            displayPinChangeDialog(editPinPreference, R.string.invalidPin2, true);
            return;
        }
        if (i2 == 3) {
            this.mPuk2 = editPinPreference.getText();
            editPinPreference.setText("");
            if (validatePin(this.mPuk2, true)) {
                this.mPinChangeState = 4;
                displayPinChangeDialog(editPinPreference);
                return;
            } else {
                displayPinChangeDialog(editPinPreference, R.string.invalidPuk2, true);
                return;
            }
        }
        if (i2 == 4) {
            this.mNewPin = editPinPreference.getText();
            editPinPreference.setText("");
            if (validatePin(this.mNewPin, false)) {
                this.mPinChangeState = 5;
                displayPinChangeDialog(editPinPreference);
                return;
            } else {
                displayPinChangeDialog(editPinPreference, R.string.invalidPin2, true);
                return;
            }
        }
        if (i2 != 5) {
            return;
        }
        if (!this.mNewPin.equals(editPinPreference.getText())) {
            this.mPinChangeState = 4;
            editPinPreference.setText("");
            displayPinChangeDialog(editPinPreference, R.string.mismatchPin2, true);
        } else {
            if (editPinPreference == this.mButtonChangePin2) {
                editPinPreference.setText("");
                messageObtainMessage = this.mFDNHandler.obtainMessage(200);
            } else {
                messageObtainMessage = this.mFDNHandler.obtainMessage(MiRilHook.HOOK_ID_START_URC_BY_AT);
            }
            this.mPhone.getIccCard().supplyPuk2(this.mPuk2, this.mNewPin, messageObtainMessage);
        }
    }

    /* JADX INFO: renamed from: com.android.phone.settings.fdn.FdnSetting$3, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error;

        static {
            int[] iArr = new int[CommandException.Error.values().length];
            $SwitchMap$com$android$internal$telephony$CommandException$Error = iArr;
            try {
                iArr[CommandException.Error.SIM_PUK2.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.PASSWORD_INCORRECT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private void displayMessage(int i, int i2) {
        String string = getString(i);
        if (i == R.string.badPin2 || i == R.string.badPuk2 || i == R.string.pin2_invalid) {
            if (i2 >= 0) {
                string = getString(i) + getString(R.string.pin2_attempts, new Object[]{Integer.valueOf(i2)});
            } else {
                string = getString(i);
            }
        }
        log("displayMessage: attemptsRemaining=" + i2 + " s=" + string);
        Toast.makeText(this, string, 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayMessage(int i) {
        displayMessage(i, -1);
    }

    private void displayPinChangeDialog(EditPinPreference editPinPreference) {
        displayPinChangeDialog(editPinPreference, 0, true);
    }

    /* JADX WARN: Code duplicated, block: B:12:0x003d  */
    /* JADX WARN: Code duplicated, block: B:13:0x0040  */
    private void displayPinChangeDialog(EditPinPreference editPinPreference, int i, boolean z) {
        int i2;
        log("[displayPinChangeDialog] mPinChangeState : " + this.mPinChangeState);
        int i3 = this.mPinChangeState;
        String str = "";
        if (i3 != 0) {
            if (i3 == 1) {
                i2 = R.string.newPin2Label;
            } else if (i3 == 2) {
                i2 = R.string.confirmPin2Label;
            } else if (i3 == 4) {
                i2 = R.string.newPin2Label;
            } else if (i3 == 5) {
                i2 = R.string.confirmPin2Label;
            } else {
                i2 = R.string.label_puk2_code;
                str = "\n" + CallSettingUtils.getPinPuk2RetryLeftNumTips(this, this.mSubId, false);
            }
        } else if (editPinPreference == this.mButtonEnableFDN) {
            i2 = R.string.enter_pin2_text;
        } else {
            i2 = R.string.oldPin2Label;
            str = "\n" + CallSettingUtils.getPinPuk2RetryLeftNumTips(this, this.mSubId, true);
        }
        if (i != 0) {
            editPinPreference.setDialogMessage(((Object) getText(i2)) + str + "\n" + ((Object) getText(i)));
        } else {
            editPinPreference.setDialogMessage(((Object) getText(i2)) + str);
        }
        if (z) {
            editPinPreference.showPinDialog();
            editPinPreference.getDialog().getWindow().setSoftInputMode(3);
        } else if (editPinPreference.getDialog() != null) {
            editPinPreference.getDialog().dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void resetPinChangeState() {
        log("resetPinChangeState");
        this.mPinChangeState = 0;
        displayPinChangeDialog(this.mButtonEnableFDN, 0, false);
        displayPinChangeDialog(this.mButtonChangePin2, 0, false);
        this.mNewPin = "";
        this.mOldPin = "";
        this.mIsPuk2Locked = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void resetPinChangeStateForPUK2() {
        log("resetPinChangeStateForPUK2");
        this.mPinChangeState = 3;
        displayPinChangeDialog(this.mButtonEnableFDN, 0, false);
        displayPinChangeDialog(this.mButtonChangePin2, 0, false);
        this.mPuk2 = "";
        this.mNewPin = "";
        this.mOldPin = "";
        this.mIsPuk2Locked = true;
    }

    private boolean validatePin(String str, boolean z) {
        return str != null && str.length() >= (z ? 8 : 4) && str.length() <= 8;
    }

    private void updateEnableFDN() {
        boolean iccFdnEnabled = this.mPhone.getIccCard().getIccFdnEnabled();
        int i = this.mSubId;
        if (iccFdnEnabled) {
            log("updateEnableFDN: FDN is enabled, mSubId=" + i);
            this.mButtonEnableFDN.setTitle(R.string.enable_fdn_ok);
            this.mButtonEnableFDN.setSummary(R.string.fdn_enabled);
            this.mButtonEnableFDN.setDialogTitle(R.string.disable_fdn);
            return;
        }
        log("updateEnableFDN: FDN is disabled, mSubId=" + i);
        this.mButtonEnableFDN.setTitle(R.string.disable_fdn_ok);
        this.mButtonEnableFDN.setSummary(R.string.fdn_disabled);
        this.mButtonEnableFDN.setDialogTitle(R.string.enable_fdn);
    }

    private void updateChangePIN2() {
        if (this.mPhone.getIccCard().getIccPuk2Blocked()) {
            showFdnErrorDialog(10, 0, 0);
            resetPinChangeStateForPUK2();
        } else if (this.mPhone.getIccCard().getIccPin2Blocked()) {
            resetPinChangeStateForPUK2();
        } else {
            resetPinChangeState();
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        SubscriptionInfoHelper subscriptionInfoHelper = new SubscriptionInfoHelper(this, getIntent());
        this.mSubscriptionInfoHelper = subscriptionInfoHelper;
        Phone phone = subscriptionInfoHelper.getPhone();
        this.mPhone = phone;
        if (phone == null) {
            log("onCreate: mPhone is null, finish!!!");
            finish();
            return;
        }
        addPreferencesFromResource(R.xml.fdn_setting);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        this.mButtonEnableFDN = (EditPinPreference) preferenceScreen.findPreference("button_fdn_enable_key");
        this.mButtonChangePin2 = (EditPinPreference) preferenceScreen.findPreference("button_change_pin2_key");
        this.mButtonEnableFDN.setOnPinEnteredListener(this);
        updateEnableFDN();
        this.mButtonChangePin2.setOnPinEnteredListener(this);
        ((PreferenceScreen) preferenceScreen.findPreference("fdn_list_pref_screen_key")).setIntent(this.mSubscriptionInfoHelper.getIntent(FdnList.class));
        if (bundle == null) {
            resetPinChangeState();
        } else {
            this.mIsPuk2Locked = bundle.getBoolean("skip_old_pin_key");
            this.mPinChangeState = bundle.getInt("pin_change_state_key");
            this.mOldPin = bundle.getString("old_pin_key");
            this.mNewPin = bundle.getString("new_pin_key");
            this.mPuk2 = bundle.getString("puk_key");
            this.mButtonChangePin2.setDialogMessage(bundle.getString("dialog_message_key"));
            this.mButtonChangePin2.setText(bundle.getString("dialog_pin_entry_key"));
            this.mButtonEnableFDN.setDialogMessage(bundle.getString("fdn_dialog_message_key"));
            this.mButtonEnableFDN.setText(bundle.getString("fdn_dialog_pin_entry_key"));
        }
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            this.mSubscriptionInfoHelper.setActionBarTitle(actionBar, getResources(), R.string.fdn_with_label);
        }
        this.mSubId = this.mPhone.getSubId();
        onCreateMTK(preferenceScreen);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("mediatek.intent.action.PHB_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        registerReceiver(this.mReceiver, intentFilter);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        updateWholeScreen(this.mSubId, true);
        if (getIntent().getBooleanExtra("auto_change_pin2", false)) {
            getIntent().putExtra("auto_change_pin2", false);
            if (this.mPhone.getIccCard().getIccPin2Blocked()) {
                resetPinChangeStateForPUK2();
                displayPinChangeDialog(this.mButtonChangePin2, 0, true);
            }
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("skip_old_pin_key", this.mIsPuk2Locked);
        bundle.putInt("pin_change_state_key", this.mPinChangeState);
        bundle.putString("old_pin_key", this.mOldPin);
        bundle.putString("new_pin_key", this.mNewPin);
        bundle.putString("puk_key", this.mPuk2);
        bundle.putInt("com.android.phone.settings.SubscriptionInfoHelper.SubscriptionId", this.mSubId);
        if (this.mButtonChangePin2.isEnabled()) {
            bundle.putString("dialog_message_key", this.mButtonChangePin2.getDialogMessage().toString());
            bundle.putString("dialog_pin_entry_key", this.mButtonChangePin2.getText());
        }
        if (this.mButtonEnableFDN.isEnabled()) {
            if (this.mButtonEnableFDN.getDialogMessage() != null) {
                bundle.putString("fdn_dialog_message_key", this.mButtonEnableFDN.getDialogMessage().toString());
            }
            bundle.putString("fdn_dialog_pin_entry_key", this.mButtonEnableFDN.getText());
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            CallFeaturesSetting.goUpToTopLevelSetting(this, this.mSubscriptionInfoHelper);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("PhoneGlobals", "FdnSetting: " + str);
    }

    @Override // com.android.phone.settings.fdn.FdnErrorDialogFragment.Listener
    public void onRequestPuk2(int i) {
        resetPinChangeStateForPUK2();
        displayPinChangeDialog(i == 12 ? this.mButtonChangePin2 : this.mButtonEnableFDN, 0, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFdnErrorDialog(int i, int i2, int i3) {
        FragmentManager fragmentManager = getFragmentManager();
        FdnErrorDialogFragment fdnErrorDialogFragment = (FdnErrorDialogFragment) fragmentManager.findFragmentByTag("tag_pin2_locked_dialog");
        if (fdnErrorDialogFragment == null) {
            FdnErrorDialogFragment fdnErrorDialogFragment2 = new FdnErrorDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("key_dialog_id", i);
            bundle.putInt("key_error_msg", i2);
            bundle.putInt("key_attempts_remaining", i3);
            fdnErrorDialogFragment2.setArguments(bundle);
            fdnErrorDialogFragment2.show(fragmentManager, "tag_pin2_locked_dialog");
            return;
        }
        FragmentTransaction fragmentTransactionBeginTransaction = fragmentManager.beginTransaction();
        fragmentTransactionBeginTransaction.show(fdnErrorDialogFragment);
        fragmentTransactionBeginTransaction.commitNow();
    }

    private void onCreateMTK(PreferenceScreen preferenceScreen) {
        PreferenceScreen preferenceScreen2 = (PreferenceScreen) preferenceScreen.findPreference("fdn_list_pref_screen_key");
        this.mButtonFDNList = preferenceScreen2;
        if (preferenceScreen2 != null) {
            preferenceScreen2.setOnPreferenceClickListener(this);
        }
        EditPinPreference editPinPreference = this.mButtonEnableFDN;
        if (editPinPreference != null) {
            editPinPreference.setOnPreferenceClickListener(this);
        }
        PhoneGlobals.getInstance().addSubInfoUpdateListener(this);
        if (PhoneUtils.isValidSubId(this.mSubId)) {
            return;
        }
        finish();
        log("onCreate, finish for invalid sub = " + this.mSubId);
    }

    @Override // android.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        log("[onPreferenceClick]");
        if (preference == this.mButtonEnableFDN) {
            if (CallSettingUtils.getPin2RetryNumber(this.mSubId) != 0) {
                return false;
            }
            log("[onPreferenceClick] retry number is 0, tips...");
            if (this.mButtonEnableFDN.getDialog() != null) {
                this.mButtonEnableFDN.getDialog().dismiss();
            }
            displayMessage(R.string.fdn_puk_need_tips);
            return true;
        }
        if (preference != this.mButtonFDNList) {
            return false;
        }
        if (CallSettingUtils.isPhoneBookReady(this, this.mSubId)) {
            startActivity(preference.getIntent().putExtra("com.android.phone.settings.SubscriptionInfoHelper.SubscriptionId", this.mSubId));
        }
        return true;
    }

    private void updateEnableFDNDialog() {
        this.mButtonEnableFDN.setDialogMessage(getString(R.string.enter_pin2_text) + "\n" + CallSettingUtils.getPinPuk2RetryLeftNumTips(this, this.mSubId, true));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void resetPinChangeState(int i, boolean z) {
        log("resetPinChangeState...");
        if (CallSettingUtils.getPin2RetryNumber(i) == 0) {
            resetPinChangeStateForPUK2(this.mSubId, z);
        } else {
            resetPinChangeState();
            updateEnableFDNDialog();
        }
    }

    private final void resetPinChangeStateForPUK2(int i, boolean z) {
        log("resetPinChangeStateForPUK2: shouldDisplay=" + z);
        if (CallSettingUtils.getPuk2RetryNumber(i) == 0) {
            log("[resetPinChangeStateForPUK2] PUK Retry number is 0, PUK2 Locked!!");
            if (z) {
                displayMessage(R.string.puk2_blocked);
                return;
            }
            return;
        }
        resetPinChangeStateForPUK2();
        updateEnableFDNDialog();
    }

    @Override // android.preference.PreferenceActivity, android.app.ListActivity, android.app.Activity
    public void onDestroy() {
        PhoneGlobals.getInstance().removeSubInfoUpdateListener(this);
        unregisterReceiver(this.mReceiver);
        super.onDestroy();
    }

    @Override // com.android.phone.PhoneGlobals.SubInfoUpdateListener
    public void handleSubInfoUpdate() {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWholeScreen(int i, boolean z) {
        if (TelephonyUtils.isAirplaneModeOn(this) && !TelephonyUtils.isImsServiceAvailable(this, i)) {
            log("[updateWholeScreen] AirplaneMode ON, so finish FdnSettings");
            finish();
        }
        IccCard iccCard = this.mPhone.getIccCard();
        if (iccCard != null) {
            boolean iccFdnAvailable = iccCard.getIccFdnAvailable();
            log("[updateWholeScreen] FDN available = " + iccFdnAvailable);
            this.mButtonEnableFDN.setEnabled(iccFdnAvailable);
            this.mButtonFDNList.setEnabled(iccFdnAvailable);
            this.mButtonChangePin2.setEnabled(CallSettingUtils.getPin2RetryNumber(this.mSubId) != 255);
            updateEnableFDN();
            updateChangePIN2();
            resetPinChangeState(i, z);
            updateEnableFDNDialog();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleChangePIN2ErrorForMTK(CommandException commandException) {
        log("Handle EVENT_PIN2_CHANGE_COMPLETE Error Code =" + commandException.getCommandError().toString());
        if (CommandException.Error.RADIO_NOT_AVAILABLE != commandException.getCommandError()) {
            return false;
        }
        displayMessage(R.string.fdn_errorcode_unknown_info);
        return true;
    }
}
