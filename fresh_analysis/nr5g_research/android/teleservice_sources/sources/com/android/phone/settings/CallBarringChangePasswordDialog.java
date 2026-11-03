package com.android.phone.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneProxy;
import com.android.phone.R;
import com.xiaomi.mirilhook.MiRilHook;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;

/* JADX INFO: loaded from: classes.dex */
public class CallBarringChangePasswordDialog implements DialogInterface.OnClickListener, TextWatcher {
    private static final String LOG_TAG = CallBarringChangePasswordDialog.class.getSimpleName();
    private boolean DBG;
    private TextView mCangePasswordPrompt;
    private Context mContext;
    private AlertDialog mDialog;
    private String mErrorMessage;
    private Handler mHandler;
    private EditText mNewPassword;
    private EditText mNewPasswordConfirm;
    private EditText mOldPassword;
    private Phone mPhone;
    private ProgressDialog mProgressDialog;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public CallBarringChangePasswordDialog(Context context, int i) {
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new Handler() { // from class: com.android.phone.settings.CallBarringChangePasswordDialog.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i2;
                int i3 = message.what;
                if (i3 == 0) {
                    CallBarringChangePasswordDialog.this.process();
                    return;
                }
                if (i3 == 1) {
                    CallBarringChangePasswordDialog.this.mErrorMessage = (String) message.obj;
                    CallBarringChangePasswordDialog.this.show();
                    CallBarringChangePasswordDialog.this.mProgressDialog.dismiss();
                    return;
                }
                if (i3 != 2) {
                    return;
                }
                AsyncResult asyncResult = (AsyncResult) message.obj;
                CallBarringChangePasswordDialog.this.dismiss();
                if (asyncResult.exception != null) {
                    if (CallBarringChangePasswordDialog.this.DBG) {
                        Log.i(CallBarringChangePasswordDialog.LOG_TAG, "handlePasswordChanged: ar.exception=" + asyncResult.exception);
                    }
                    CommandException commandException = asyncResult.exception;
                    if (commandException.getCommandError() == CommandException.Error.PASSWORD_INCORRECT) {
                        i2 = 100;
                    } else {
                        i2 = commandException.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE ? 200 : MiRilHook.HOOK_ID_START_URC_BY_AT;
                    }
                    CallBarringChangePasswordDialog.this.showErrDialog(i2);
                    return;
                }
                boolean z = asyncResult.userObj instanceof Throwable;
                CallBarringChangePasswordDialog callBarringChangePasswordDialog = CallBarringChangePasswordDialog.this;
                if (z) {
                    callBarringChangePasswordDialog.showErrDialog(400);
                    return;
                }
                if (callBarringChangePasswordDialog.DBG) {
                    Log.i(CallBarringChangePasswordDialog.LOG_TAG, "handlePasswordChanged is called without exception");
                }
                Toast.makeText(CallBarringChangePasswordDialog.this.mContext, CallBarringChangePasswordDialog.this.mContext.getString(R.string.password_changed), 0).show();
            }
        };
        this.mContext = context;
        this.mErrorMessage = null;
        AlertDialog alertDialogCreate = new AlertDialog.Builder(context).setPositiveButton(android.R.string.ok, this).setNegativeButton(android.R.string.cancel, this).create();
        this.mDialog = alertDialogCreate;
        alertDialogCreate.getWindow().addFlags(524288);
        View viewInflate = LayoutInflater.from(this.mContext).inflate(R.layout.call_barring_change_password, (ViewGroup) null);
        this.mDialog.setView(viewInflate);
        this.mCangePasswordPrompt = (TextView) viewInflate.findViewById(R.id.change_password_prompt);
        this.mOldPassword = (EditText) viewInflate.findViewById(R.id.call_carring_old_password);
        this.mNewPassword = (EditText) viewInflate.findViewById(R.id.call_carring_new_password);
        this.mNewPasswordConfirm = (EditText) viewInflate.findViewById(R.id.call_carring_new_password_confirm);
        this.mPhone = MiuiPhoneUtils.getPhone(i);
        this.mOldPassword.setSingleLine(true);
        this.mOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.mOldPassword.setKeyListener(DigitsKeyListener.getInstance());
        this.mOldPassword.setTextAlignment(5);
        this.mNewPassword.setSingleLine(true);
        this.mNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.mNewPassword.setKeyListener(DigitsKeyListener.getInstance());
        this.mNewPassword.setTextAlignment(5);
        this.mNewPasswordConfirm.setSingleLine(true);
        this.mNewPasswordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.mNewPasswordConfirm.setKeyListener(DigitsKeyListener.getInstance());
        this.mNewPasswordConfirm.setTextAlignment(5);
        this.mOldPassword.addTextChangedListener(this);
        this.mNewPassword.addTextChangedListener(this);
        this.mNewPasswordConfirm.addTextChangedListener(this);
        ProgressDialog progressDialog = new ProgressDialog(this.mContext);
        this.mProgressDialog = progressDialog;
        progressDialog.setCancelable(false);
        this.mProgressDialog.setIndeterminate(true);
        this.mProgressDialog.getWindow().setType(2008);
        this.mProgressDialog.getWindow().addFlags(2);
    }

    public void show() {
        boolean zIsEmpty = TextUtils.isEmpty(this.mErrorMessage);
        TextView textView = this.mCangePasswordPrompt;
        if (!zIsEmpty) {
            textView.setText(this.mErrorMessage);
            this.mCangePasswordPrompt.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
        this.mDialog.setTitle(R.string.change_password);
        this.mOldPassword.setHint(this.mContext.getResources().getString(R.string.old_password_label, 4));
        this.mOldPassword.setText("");
        this.mOldPassword.requestFocus();
        this.mNewPassword.setHint(this.mContext.getResources().getString(R.string.new_password_label, 4));
        this.mNewPassword.setText("");
        this.mNewPasswordConfirm.setHint(R.string.confirm_password_label);
        this.mNewPasswordConfirm.setText("");
        this.mProgressDialog.setMessage(this.mContext.getText(R.string.updating_settings));
        this.mDialog.show();
        afterTextChanged(null);
    }

    private void doChangePassword(String str, String str2) {
        if (this.DBG) {
            Log.d(LOG_TAG, "doChangePassword() is called with oldPassword is " + str + "newPassword is " + str2);
        }
        Message messageObtainMessage = this.mHandler.obtainMessage(2, 0, 2, null);
        if (this.mPhone.getPhoneType() == 1) {
            PhoneProxy.getCommandsInterface(this.mPhone).changeBarringPassword("AB", str, str2, messageObtainMessage);
        }
    }

    private static boolean validatePassword(String str) {
        return str != null && str.length() == 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void process() {
        String string = this.mOldPassword.getText().toString();
        String string2 = this.mNewPassword.getText().toString();
        String string3 = this.mNewPasswordConfirm.getText().toString();
        Message messageObtainMessage = this.mHandler.obtainMessage(1);
        if (!validatePassword(string)) {
            messageObtainMessage.obj = this.mContext.getString(R.string.invalid_password, 4);
            messageObtainMessage.sendToTarget();
            return;
        }
        if (!TextUtils.equals(string2, string3)) {
            messageObtainMessage.obj = this.mContext.getString(R.string.mismatch_password);
            messageObtainMessage.sendToTarget();
        } else if (!validatePassword(string2)) {
            messageObtainMessage.obj = this.mContext.getString(R.string.invalid_password, 4);
            messageObtainMessage.sendToTarget();
        } else {
            messageObtainMessage.what = 2;
            this.mProgressDialog.show();
            doChangePassword(string, string2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showErrDialog(int i) {
        int i2;
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        int i3 = R.string.error_updating_title;
        if (i == 100) {
            i2 = R.string.wrong_password;
        } else if (i == 200) {
            i2 = R.string.command_error_fdn_failure;
        } else if (i == 400) {
            i2 = R.string.response_error;
        } else {
            i2 = R.string.exception_error;
        }
        builder.setNeutralButton(R.string.close_dialog, new DialogInterface.OnClickListener(this) { // from class: com.android.phone.settings.CallBarringChangePasswordDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i4) {
                dialogInterface.dismiss();
            }
        });
        builder.setTitle(i3);
        builder.setMessage(i2);
        builder.setCancelable(false);
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismiss() {
        if (this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
        if (this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (this.mDialog.isShowing()) {
            boolean z = this.mOldPassword.getText().length() != 0;
            if (z && this.mNewPassword.getVisibility() == 0 && this.mNewPassword.getText().length() == 0) {
                z = false;
            }
            this.mDialog.getButton(-1).setEnabled((z && this.mNewPasswordConfirm.getVisibility() == 0 && this.mNewPasswordConfirm.getText().length() == 0) ? false : z);
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
