package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneProxy;
import com.android.phone.R;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;

/* JADX INFO: loaded from: classes.dex */
public class EditPinDialog implements DialogInterface.OnClickListener, TextWatcher {
    private Context mContext;
    private AlertDialog mDialog;
    private EditText mEditTextConfirmNewPin;
    private EditText mEditTextNewPin;
    private EditText mEditTextOldPinPuk;
    private IccCard mIccCard;
    private DialogClosedListener mListener;
    private Mode mMode;
    private Phone mPhone;
    private ProgressDialog mProgressDialog;
    private TextView mTextViewPrompt;
    int slotId = 0;
    private HotSwapBroadcastReceiver mHotSwapReceiver = new HotSwapBroadcastReceiver();
    private int mAttemptsRemaining = 0;
    private Handler mHandler = new Handler() { // from class: com.android.phone.settings.EditPinDialog.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    EditPinDialog.this.mProgressDialog.show();
                    EditPinDialog.this.process();
                    break;
                case 1:
                    EditPinDialog.this.mErrorMessage = (String) message.obj;
                    EditPinDialog.this.show();
                    EditPinDialog.this.mProgressDialog.dismiss();
                    break;
                case 2:
                    Throwable th = ((AsyncResult) message.obj).exception;
                    EditPinDialog editPinDialog = EditPinDialog.this;
                    if (th != null) {
                        obtainMessage(1, editPinDialog.mContext.getString(R.string.bad_pin)).sendToTarget();
                    } else {
                        editPinDialog.dismiss(false);
                    }
                    break;
                case 3:
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    IccPinStateHelper.updatePin2State(asyncResult, EditPinDialog.this.mPhone.getPhoneId());
                    Throwable th2 = asyncResult.exception;
                    if (th2 != null) {
                        boolean z = th2 instanceof CommandException;
                        EditPinDialog editPinDialog2 = EditPinDialog.this;
                        if (z) {
                            editPinDialog2.mAttemptsRemaining = message.arg1;
                            int i = AnonymousClass4.$SwitchMap$com$android$internal$telephony$CommandException$Error[asyncResult.exception.getCommandError().ordinal()];
                            if (i != 1 && i != 2) {
                                obtainMessage(1, EditPinDialog.this.mContext.getString(R.string.fdn_failed)).sendToTarget();
                            } else {
                                obtainMessage(1, EditPinDialog.this.mContext.getString(R.string.bad_pin2, 2)).sendToTarget();
                            }
                        } else {
                            obtainMessage(1, editPinDialog2.mContext.getString(R.string.pin2_error_exception)).sendToTarget();
                        }
                    } else {
                        EditPinDialog.this.dismiss(false);
                        Intent intent = new Intent("action_fdn_change");
                        intent.putExtra(SubscriptionManager.SLOT_KEY, EditPinDialog.this.mPhone.getPhoneId());
                        EditPinDialog.this.mContext.sendBroadcast(intent);
                    }
                    break;
                case 4:
                    Throwable th3 = ((AsyncResult) message.obj).exception;
                    EditPinDialog editPinDialog3 = EditPinDialog.this;
                    if (th3 != null) {
                        boolean zIsPin1Locked = IccPinStateHelper.isPin1Locked(editPinDialog3.mPhone.getPhoneId());
                        EditPinDialog editPinDialog4 = EditPinDialog.this;
                        if (!zIsPin1Locked) {
                            obtainMessage(1, editPinDialog4.mContext.getString(R.string.bad_pin)).sendToTarget();
                        } else {
                            editPinDialog4.dismiss(false);
                            new AlertDialog.Builder(EditPinDialog.this.mContext).setIconAttribute(android.R.attr.alertDialogIcon).setMessage(R.string.puk_requested).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.EditPinDialog.2.1
                                @Override // android.content.DialogInterface.OnClickListener
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    obtainMessage(1).sendToTarget();
                                }
                            }).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).show();
                        }
                    } else {
                        editPinDialog3.showToast(editPinDialog3.mContext.getString(R.string.pin_changed));
                        EditPinDialog.this.dismiss(false);
                    }
                    break;
                case 5:
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    IccPinStateHelper.updatePin2State(asyncResult2, EditPinDialog.this.mPhone.getPhoneId());
                    Throwable th4 = asyncResult2.exception;
                    EditPinDialog editPinDialog5 = EditPinDialog.this;
                    if (th4 != null) {
                        boolean zIsPin2Locked = IccPinStateHelper.isPin2Locked(editPinDialog5.mPhone.getPhoneId());
                        EditPinDialog editPinDialog6 = EditPinDialog.this;
                        if (!zIsPin2Locked) {
                            editPinDialog6.mAttemptsRemaining = message.arg1;
                            obtainMessage(1, EditPinDialog.this.mContext.getString(R.string.bad_pin2_old, 2)).sendToTarget();
                        } else {
                            editPinDialog6.mAttemptsRemaining = 0;
                            EditPinDialog.this.dismiss(false);
                            new AlertDialog.Builder(EditPinDialog.this.mContext).setIconAttribute(android.R.attr.alertDialogIcon).setMessage(EditPinDialog.this.mContext.getString(R.string.fdn_enable_puk2_requested, 2, 2)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.EditPinDialog.2.2
                                @Override // android.content.DialogInterface.OnClickListener
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    obtainMessage(1).sendToTarget();
                                }
                            }).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).show();
                        }
                    } else {
                        editPinDialog5.showToast(editPinDialog5.mContext.getString(R.string.pin2_changed, 2));
                        EditPinDialog.this.dismiss(false);
                    }
                    break;
                case 6:
                    Throwable th5 = ((AsyncResult) message.obj).exception;
                    EditPinDialog editPinDialog7 = EditPinDialog.this;
                    if (th5 != null) {
                        obtainMessage(1, editPinDialog7.mContext.getString(R.string.bad_puk)).sendToTarget();
                    } else {
                        editPinDialog7.showToast(editPinDialog7.mContext.getString(R.string.pin_changed));
                        EditPinDialog.this.dismiss(false);
                    }
                    break;
                case 7:
                    AsyncResult asyncResult3 = (AsyncResult) message.obj;
                    IccPinStateHelper.updatePin2State(asyncResult3, EditPinDialog.this.mPhone.getPhoneId());
                    Throwable th6 = asyncResult3.exception;
                    EditPinDialog editPinDialog8 = EditPinDialog.this;
                    if (th6 != null) {
                        editPinDialog8.mAttemptsRemaining = message.arg1;
                        obtainMessage(1, EditPinDialog.this.mContext.getString(R.string.bad_puk2, 2)).sendToTarget();
                    } else {
                        editPinDialog8.showToast(editPinDialog8.mContext.getString(R.string.pin2_changed, 2));
                        EditPinDialog.this.dismiss(false);
                    }
                    break;
            }
        }
    };
    private String mErrorMessage = null;

    public interface DialogClosedListener {
        void OnDialogClose(EditPinDialog editPinDialog, boolean z);
    }

    public enum Mode {
        Unknow,
        ICC_LOCK,
        FDN,
        PIN1,
        PIN2,
        PUK1,
        PUK2
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public EditPinDialog(Context context, Mode mode, DialogClosedListener dialogClosedListener, Phone phone) {
        this.mContext = context;
        this.mMode = mode;
        this.mListener = dialogClosedListener;
        this.mDialog = new AlertDialog.Builder(context).setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) null).setNegativeButton(android.R.string.cancel, this).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.phone.settings.EditPinDialog.1
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                if (EditPinDialog.this.mListener != null) {
                    EditPinDialog.this.mListener.OnDialogClose(EditPinDialog.this, true);
                }
            }
        }).create();
        View viewInflate = LayoutInflater.from(this.mContext).inflate(R.layout.edit_pin_dialog, (ViewGroup) null);
        this.mDialog.setView(viewInflate);
        this.mTextViewPrompt = (TextView) viewInflate.findViewById(R.id.edit_pin_dialog_prompt);
        this.mEditTextOldPinPuk = (EditText) viewInflate.findViewById(R.id.edit_pin_dialog_old_pin_puk);
        this.mEditTextNewPin = (EditText) viewInflate.findViewById(R.id.edit_pin_dialog_new_pin);
        this.mEditTextConfirmNewPin = (EditText) viewInflate.findViewById(R.id.edit_pin_dialog_confirm_new_pin);
        this.mPhone = phone;
        this.mIccCard = phone.getIccCard();
        this.mEditTextOldPinPuk.setSingleLine(true);
        this.mEditTextOldPinPuk.requestFocus();
        this.mEditTextOldPinPuk.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.mEditTextOldPinPuk.setKeyListener(DigitsKeyListener.getInstance());
        this.mEditTextOldPinPuk.setTextAlignment(5);
        this.mEditTextNewPin.setSingleLine(true);
        this.mEditTextNewPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.mEditTextNewPin.setKeyListener(DigitsKeyListener.getInstance());
        this.mEditTextNewPin.setTextAlignment(5);
        this.mEditTextConfirmNewPin.setSingleLine(true);
        this.mEditTextConfirmNewPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.mEditTextConfirmNewPin.setKeyListener(DigitsKeyListener.getInstance());
        this.mEditTextConfirmNewPin.setTextAlignment(5);
        this.mEditTextOldPinPuk.addTextChangedListener(this);
        this.mEditTextNewPin.addTextChangedListener(this);
        this.mEditTextConfirmNewPin.addTextChangedListener(this);
        ProgressDialog progressDialog = new ProgressDialog(this.mContext);
        this.mProgressDialog = progressDialog;
        progressDialog.setCancelable(false);
        this.mProgressDialog.setIndeterminate(true);
        this.mProgressDialog.getWindow().setType(2008);
        this.mProgressDialog.getWindow().addFlags(2);
        IccPinStateHelper.updateIccCardStatus();
    }

    public void setMode(Mode mode) {
        this.mMode = mode;
        this.mErrorMessage = null;
        this.mAttemptsRemaining = 0;
    }

    public Mode getMode() {
        return this.mMode;
    }

    private boolean updateDialog() {
        int i;
        int i2;
        String quantityString;
        String string;
        String string2;
        String string3;
        String string4;
        String str;
        Mode mode = this.mMode;
        Mode mode2 = Mode.PIN1;
        if ((mode == mode2 || mode == Mode.ICC_LOCK) && IccPinStateHelper.isPin1Locked(this.mPhone.getPhoneId())) {
            return false;
        }
        Mode mode3 = this.mMode;
        Mode mode4 = Mode.PIN2;
        if (mode3 == mode4 && IccPinStateHelper.isPin2Locked(this.mPhone.getPhoneId())) {
            this.mMode = Mode.PUK2;
        } else if (this.mMode == Mode.FDN && IccPinStateHelper.isPin2Locked(this.mPhone.getPhoneId())) {
            new AlertDialog.Builder(this.mContext).setIconAttribute(android.R.attr.alertDialogIcon).setTitle(R.string.fdn).setMessage(this.mContext.getString(R.string.fdn_enable_puk2_requested, 2, 2)).setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) null).show();
            return false;
        }
        Mode mode5 = this.mMode;
        Mode mode6 = Mode.ICC_LOCK;
        if (mode5 == mode6 || mode5 == mode2 || mode5 == Mode.PUK1) {
            if (mode5 == mode6) {
                i = this.mIccCard.getIccLockEnabled() ? R.string.disable_sim_pin : R.string.enable_sim_pin;
            } else {
                i = R.string.change_pin;
            }
            i2 = i;
            Mode mode7 = this.mMode;
            if (mode7 == mode2 || mode7 == mode6) {
                quantityString = this.mErrorMessage;
                string = this.mContext.getString(R.string.old_pin_label);
                string2 = this.mContext.getString(R.string.new_pin_label);
                string3 = this.mContext.getString(R.string.confirm_pin_label);
            } else {
                quantityString = this.mErrorMessage;
                string = this.mContext.getString(R.string.puk_label);
                string2 = this.mContext.getString(R.string.new_pin_label);
                string3 = this.mContext.getString(R.string.confirm_pin_label);
            }
        } else {
            Mode mode8 = Mode.FDN;
            if (mode5 != mode8 && mode5 != mode4 && mode5 != Mode.PUK2) {
                return false;
            }
            i2 = mode5 == mode8 ? R.string.fdn : R.string.change_pin2;
            if (mode5 == mode4 || mode5 == mode8) {
                int i3 = this.mAttemptsRemaining;
                if (i3 > 0) {
                    quantityString = this.mContext.getResources().getQuantityString(R.plurals.pin_attempts, i3, Integer.valueOf(i3));
                    if (!TextUtils.isEmpty(this.mErrorMessage)) {
                        quantityString = this.mErrorMessage + quantityString;
                    }
                    if (i3 == 1) {
                        str = quantityString + this.mContext.getString(R.string.pin2_lock_warning, 2);
                    }
                    string = this.mContext.getString(R.string.old_pin2_label, 2);
                    string2 = this.mContext.getString(R.string.new_pin2_label, 2);
                    string3 = this.mContext.getString(R.string.confirm_pin2_label, 2);
                } else {
                    str = this.mErrorMessage;
                }
                quantityString = str;
                string = this.mContext.getString(R.string.old_pin2_label, 2);
                string2 = this.mContext.getString(R.string.new_pin2_label, 2);
                string3 = this.mContext.getString(R.string.confirm_pin2_label, 2);
            } else {
                int i4 = this.mAttemptsRemaining;
                if (i4 > 0) {
                    quantityString = this.mContext.getResources().getQuantityString(R.plurals.pin_attempts, i4, Integer.valueOf(i4));
                    if (!TextUtils.isEmpty(this.mErrorMessage)) {
                        quantityString = this.mErrorMessage + quantityString;
                    }
                    if (i4 == 1) {
                        quantityString = quantityString + this.mContext.getString(R.string.puk_lock_warning);
                    }
                } else {
                    quantityString = this.mErrorMessage;
                }
                string = this.mContext.getString(R.string.puk2_label, 2);
                string2 = this.mContext.getString(R.string.new_pin2_label, 2);
                string3 = this.mContext.getString(R.string.confirm_pin2_label, 2);
            }
        }
        int iOrdinal = this.mMode.ordinal();
        if (iOrdinal == 1) {
            string4 = this.mContext.getString(R.string.pin_progress_set_icc_lock);
        } else if (iOrdinal == 2) {
            string4 = this.mContext.getString(R.string.pin_progress_set_fdn_lock);
        } else if (iOrdinal == 3) {
            string4 = this.mContext.getString(R.string.pin_progress_change_pin);
        } else if (iOrdinal == 4 || iOrdinal == 6) {
            string4 = this.mContext.getString(R.string.pin_progress_change_pin2, 2);
        } else {
            string4 = this.mContext.getString(R.string.pin_progress_unlock_icc);
        }
        Mode mode9 = this.mMode;
        if (mode9 == Mode.FDN || mode9 == mode6) {
            this.mEditTextNewPin.setVisibility(8);
            this.mEditTextConfirmNewPin.setVisibility(8);
        } else {
            this.mEditTextNewPin.setVisibility(0);
            this.mEditTextConfirmNewPin.setVisibility(0);
        }
        this.mDialog.setTitle(i2);
        boolean zIsEmpty = TextUtils.isEmpty(quantityString);
        TextView textView = this.mTextViewPrompt;
        if (!zIsEmpty) {
            textView.setText(quantityString);
            this.mTextViewPrompt.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
        this.mEditTextOldPinPuk.setHint(string);
        this.mEditTextOldPinPuk.setText("");
        this.mEditTextNewPin.setHint(string2);
        this.mEditTextNewPin.setText("");
        this.mEditTextConfirmNewPin.setHint(string3);
        this.mEditTextConfirmNewPin.setText("");
        this.mProgressDialog.setMessage(string4);
        return true;
    }

    public void show() {
        if (!IccPinStateHelper.isInitialized(this.mPhone.getPhoneId())) {
            this.mProgressDialog.show();
            IccPinStateHelper.waitForInitialization(this.mPhone.getPhoneId());
            this.mProgressDialog.dismiss();
        }
        if (updateDialog()) {
            this.mDialog.show();
            afterTextChanged(null);
        } else {
            dismiss();
        }
    }

    static boolean validatePin(String str, boolean z) {
        return str != null && str.length() >= (z ? 8 : 4) && str.length() <= 8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showToast(String str) {
        Toast.makeText(this.mContext, str, 1).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void process() {
        String string = this.mEditTextOldPinPuk.getText().toString();
        String string2 = this.mEditTextNewPin.getText().toString();
        String string3 = this.mEditTextConfirmNewPin.getText().toString();
        Message messageObtainMessage = this.mHandler.obtainMessage(1);
        switch (this.mMode.ordinal()) {
            case 1:
                if (validatePin(string, false)) {
                    messageObtainMessage.what = 2;
                    IccCard iccCard = this.mIccCard;
                    iccCard.setIccLockEnabled(!iccCard.getIccLockEnabled(), string, messageObtainMessage);
                } else {
                    messageObtainMessage.obj = this.mContext.getString(R.string.invalid_pin, 4, 8);
                    messageObtainMessage.sendToTarget();
                }
                break;
            case 2:
                if (validatePin(string, false)) {
                    messageObtainMessage.what = 3;
                    IccCard iccCard2 = this.mIccCard;
                    iccCard2.setIccFdnEnabled(!iccCard2.getIccFdnEnabled(), string, messageObtainMessage);
                } else {
                    messageObtainMessage.obj = this.mContext.getString(R.string.invalid_pin2, 2, 4, 8);
                    messageObtainMessage.sendToTarget();
                }
                break;
            case 3:
                if (validatePin(string, false)) {
                    if (!TextUtils.equals(string2, string3)) {
                        messageObtainMessage.obj = this.mContext.getString(R.string.mismatch_pin);
                        messageObtainMessage.sendToTarget();
                    } else if (validatePin(string2, false)) {
                        messageObtainMessage.what = 4;
                        this.mIccCard.changeIccLockPassword(string, string2, messageObtainMessage);
                    } else {
                        messageObtainMessage.obj = this.mContext.getString(R.string.invalid_pin, 4, 8);
                        messageObtainMessage.sendToTarget();
                    }
                } else {
                    messageObtainMessage.obj = this.mContext.getString(R.string.invalid_pin, 4, 8);
                    messageObtainMessage.sendToTarget();
                }
                break;
            case 4:
                if (validatePin(string, false)) {
                    if (TextUtils.equals(string2, string3)) {
                        if (validatePin(string2, false)) {
                            messageObtainMessage.what = 5;
                            this.mIccCard.changeIccFdnPassword(string, string2, messageObtainMessage);
                        } else {
                            messageObtainMessage.obj = this.mContext.getString(R.string.invalid_pin2, 2, 4, 8);
                            messageObtainMessage.sendToTarget();
                        }
                    } else {
                        messageObtainMessage.obj = this.mContext.getString(R.string.mismatch_pin2, 2);
                        messageObtainMessage.sendToTarget();
                    }
                } else {
                    messageObtainMessage.obj = this.mContext.getString(R.string.invalid_pin2, 2, 4, 8);
                    messageObtainMessage.sendToTarget();
                }
                break;
            case 5:
                if (validatePin(string, true)) {
                    if (!TextUtils.equals(string2, string3)) {
                        messageObtainMessage.obj = this.mContext.getString(R.string.mismatch_pin);
                        messageObtainMessage.sendToTarget();
                    } else if (validatePin(string2, false)) {
                        messageObtainMessage.what = 6;
                        this.mIccCard.supplyPuk(string, string2, messageObtainMessage);
                    } else {
                        messageObtainMessage.obj = this.mContext.getString(R.string.invalid_pin, 4, 8);
                        messageObtainMessage.sendToTarget();
                    }
                } else {
                    messageObtainMessage.obj = this.mContext.getString(R.string.invalid_puk, 4, 8);
                    messageObtainMessage.sendToTarget();
                }
                break;
            case 6:
                if (!validatePin(string, true)) {
                    messageObtainMessage.obj = this.mContext.getString(R.string.invalid_puk2, 2, 8);
                    messageObtainMessage.sendToTarget();
                } else if (!TextUtils.equals(string2, string3)) {
                    messageObtainMessage.obj = this.mContext.getString(R.string.mismatch_pin2, 2);
                    messageObtainMessage.sendToTarget();
                } else if (!validatePin(string2, false)) {
                    messageObtainMessage.obj = this.mContext.getString(R.string.invalid_pin2, 2, 4, 8);
                    messageObtainMessage.sendToTarget();
                } else {
                    messageObtainMessage.what = 7;
                    this.mIccCard.supplyPuk2(string, string2, messageObtainMessage);
                }
                break;
        }
    }

    /* JADX INFO: renamed from: com.android.phone.settings.EditPinDialog$4, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass4 {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void dismiss(boolean z) {
        if (this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
        if (this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
        DialogClosedListener dialogClosedListener = this.mListener;
        if (dialogClosedListener != null) {
            dialogClosedListener.OnDialogClose(this, z);
        }
    }

    public void dismiss() {
        dismiss(true);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (this.mDialog.isShowing()) {
            boolean z = this.mEditTextOldPinPuk.getText().length() != 0;
            if (z && this.mEditTextNewPin.getVisibility() == 0 && this.mEditTextNewPin.getText().length() == 0) {
                z = false;
            }
            this.mDialog.getButton(-1).setEnabled((z && this.mEditTextConfirmNewPin.getVisibility() == 0 && this.mEditTextConfirmNewPin.getText().length() == 0) ? false : z);
            this.mDialog.getButton(-1).setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.EditPinDialog.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    EditPinDialog.this.mHandler.obtainMessage(0).sendToTarget();
                }
            });
        }
    }

    public void registerHotSwapReceiver(Context context) {
        context.registerReceiver(this.mHotSwapReceiver, new IntentFilter("android.intent.action.SIM_HOT_SWAP"), 2);
    }

    public void unregisterHotSwapReceiver(Context context) {
        context.unregisterReceiver(this.mHotSwapReceiver);
    }

    class HotSwapBroadcastReceiver extends BroadcastReceiver {
        private HotSwapBroadcastReceiver(EditPinDialog editPinDialog) {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "android.intent.action.SIM_HOT_SWAP".equalsIgnoreCase(intent.getAction())) {
                IccPinStateHelper.updateIccCardStatus();
            }
        }
    }

    abstract class IccPinStateHelper {
        private static boolean[] sInitialized;
        private static Object[] sLocker;
        private static boolean[] sPin2Locked;

        static {
            int i = MiuiPhoneUtils.PHONE_COUNT;
            sInitialized = new boolean[i];
            sPin2Locked = new boolean[i];
            sLocker = new Object[i];
        }

        public static void updateIccCardStatus() {
            new Thread() { // from class: com.android.phone.settings.EditPinDialog.IccPinStateHelper.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    super.run();
                    Looper.prepare();
                    for (final int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                        IccPinStateHelper.sInitialized[i] = false;
                        IccPinStateHelper.sPin2Locked[i] = false;
                        IccPinStateHelper.sLocker[i] = new Object();
                        IccPinStateHelper.getCommandsInterface(i).getIccCardStatus(new Handler(this) { // from class: com.android.phone.settings.EditPinDialog.IccPinStateHelper.1.1
                            @Override // android.os.Handler
                            public void handleMessage(Message message) {
                                IccPinStateHelper.handlerIccCardStatus((AsyncResult) message.obj, i);
                            }
                        }.obtainMessage());
                    }
                    Looper.loop();
                }
            }.start();
        }

        public static boolean isInitialized(int i) {
            return sInitialized[i];
        }

        public static void waitForInitialization(int i) {
            synchronized (sLocker[i]) {
                if (!sInitialized[i]) {
                    try {
                        sLocker[i].wait();
                    } catch (InterruptedException unused) {
                    }
                }
            }
        }

        public static boolean isPin1Locked(int i) {
            return MiuiPhoneUtils.getPhone(i).getIccCard().getState() == IccCardConstants.State.PUK_REQUIRED;
        }

        public static boolean isPin2Locked(int i) {
            return sPin2Locked[i];
        }

        public static void updatePin2State(AsyncResult asyncResult, int i) {
            CommandException commandException = asyncResult.exception;
            if (commandException == null) {
                sPin2Locked[i] = false;
            } else if ((commandException instanceof CommandException) && commandException.getCommandError() == CommandException.Error.SIM_PUK2) {
                sPin2Locked[i] = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static CommandsInterface getCommandsInterface(int i) {
            return PhoneProxy.getCommandsInterface(MiuiPhoneUtils.getPhone(i));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handlerIccCardStatus(AsyncResult asyncResult, int i) {
            int i2;
            IccCardApplicationStatus iccCardApplicationStatus;
            IccCardStatus iccCardStatus = (IccCardStatus) asyncResult.result;
            sPin2Locked[i] = false;
            if (iccCardStatus != null) {
                int phoneType = MiuiPhoneUtils.getPhone(0).getPhoneType();
                if (phoneType == 1) {
                    i2 = iccCardStatus.mGsmUmtsSubscriptionAppIndex;
                } else {
                    i2 = phoneType == 2 ? iccCardStatus.mCdmaSubscriptionAppIndex : -1;
                }
                if (i2 >= 0) {
                    IccCardApplicationStatus[] iccCardApplicationStatusArr = iccCardStatus.mApplications;
                    if (i2 < iccCardApplicationStatusArr.length && (iccCardApplicationStatus = iccCardApplicationStatusArr[i2]) != null) {
                        sPin2Locked[i] = iccCardApplicationStatus.pin2 == IccCardStatus.PinState.PINSTATE_ENABLED_BLOCKED;
                    }
                }
            }
            synchronized (sLocker[i]) {
                sInitialized[i] = true;
                sLocker[i].notifyAll();
            }
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            dismiss();
        } else if (i == -1) {
            this.mHandler.obtainMessage(0).sendToTarget();
        }
    }
}
