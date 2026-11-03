package com.android.phone.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.preference.Preference;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.MiuiSuppServiceStatistic;
import com.xiaomi.mirilhook.MiRilHook;
import miui.telephony.CarrierConfigManagerEx;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class CallBarringResetPreference extends Preference implements Preference.OnPreferenceClickListener {
    private static final String LOG_TAG = CallBarringResetPreference.class.getSimpleName();
    private boolean DBG;
    private CallBarringInterface mCallBarringInterface;
    private Context mContext;
    private MyHandler mHandler;
    private String mPassword;
    private Phone mPhone;
    private TimeConsumingPreferenceListener mTcpListener;

    public CallBarringResetPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mCallBarringInterface = null;
        this.mHandler = new MyHandler();
        init(context);
        this.mPhone = MiuiPhoneUtils.getPhone(0);
    }

    public void setListener(TimeConsumingPreferenceListener timeConsumingPreferenceListener) {
        this.mTcpListener = timeConsumingPreferenceListener;
    }

    public void setCallBarringInterface(CallBarringInterface callBarringInterface) {
        this.mCallBarringInterface = callBarringInterface;
    }

    public void setPhoneInstance(Phone phone) {
        this.mPhone = phone;
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        doPreferenceClick(this.mContext.getString(R.string.deactivate_all));
        return true;
    }

    public void doPreferenceClick(String str) {
        if (MiuiPhoneUtils.maybeShowDialogForSuppService(this.mPhone, 0)) {
            return;
        }
        if (MiuiImsPhoneUtils.isCbOverImsNoNeedPassword(this.mPhone, "AB")) {
            if (this.mCallBarringInterface != null) {
                this.mPassword = "";
                setCallState("");
            }
            Log.d(LOG_TAG, "cb over UT don't need password");
            return;
        }
        showPasswordDialog(str, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPasswordDialog(final String str, final Message message) {
        final View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.call_barring_option, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.ViewTop)).setText(this.mContext.getString(R.string.enter_callbarring_password, 4));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(viewInflate);
        builder.setTitle(str);
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallBarringResetPreference.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText editText = (EditText) viewInflate.findViewById(R.id.EditPassword);
                CallBarringResetPreference.this.mPassword = editText.getText().toString();
                CallBarringResetPreference callBarringResetPreference = CallBarringResetPreference.this;
                boolean zValidatePassword = callBarringResetPreference.validatePassword(callBarringResetPreference.mPassword);
                CallBarringResetPreference callBarringResetPreference2 = CallBarringResetPreference.this;
                if (!zValidatePassword) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(callBarringResetPreference2.mContext);
                    builder2.setTitle(str);
                    builder2.setMessage(CallBarringResetPreference.this.mContext.getText(R.string.wrong_password));
                    builder2.setCancelable(false);
                    builder2.setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null);
                    builder2.create().show();
                    return;
                }
                if (callBarringResetPreference2.mCallBarringInterface != null) {
                    if (message != null) {
                        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallBarringOption(CallBarringResetPreference.this.mPhone, "AB", false, CallBarringResetPreference.this.mPassword, message);
                        if (CallBarringResetPreference.this.mTcpListener != null) {
                            CallBarringResetPreference.this.mTcpListener.onStarted(CallBarringResetPreference.this, false);
                            return;
                        }
                        return;
                    }
                    CallBarringResetPreference callBarringResetPreference3 = CallBarringResetPreference.this;
                    callBarringResetPreference3.setCallState(callBarringResetPreference3.mPassword);
                }
            }
        });
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validatePassword(String str) {
        return str != null && str.length() == 4;
    }

    private void init(Context context) {
        this.mContext = context;
        setEnabled(true);
        setOnPreferenceClickListener(this);
    }

    public void setCallState(String str) {
        setCallState(false, str);
    }

    private void setCallState(boolean z, String str) {
        if (this.DBG) {
            Log.i(LOG_TAG, "setCallState() is called password is " + str + "enable is " + z);
        }
        Message messageObtainMessage = this.mHandler.obtainMessage(0, 0, 0, null);
        if (this.mPhone.getPhoneType() == 1) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallBarringOption(this.mPhone, "AB", z, str, messageObtainMessage);
        }
        TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
        if (timeConsumingPreferenceListener != null) {
            timeConsumingPreferenceListener.onStarted(this, false);
        }
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncResult asyncResult;
            int i = message.what;
            if (i == 0) {
                MiuiSuppServiceStatistic.getInstance(CallBarringResetPreference.this.mContext).recordCountClickScene(message.what, 400, CallBarringResetPreference.this.mPhone);
            } else {
                if (i == 1) {
                }
                asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception == null && message.what == 1) {
                    MiuiSuppServiceStatistic.getInstance(CallBarringResetPreference.this.mContext).recordExceptionScene(message.what, 400, asyncResult.exception, CallBarringResetPreference.this.mPhone);
                    return;
                }
            }
            AsyncResult asyncResult2 = (AsyncResult) message.obj;
            if (asyncResult2.exception == null || message.what == 1) {
                CallBarringResetPreference.this.mTcpListener.onFinished(CallBarringResetPreference.this, false);
                if (asyncResult2.exception != null && CarrierConfigManagerEx.getBooleanConfigForSubId(CallBarringResetPreference.this.mPhone.getContext(), CallBarringResetPreference.this.mPhone.getSubId(), "config_support_callbarring_cancel_all_by_sequence", false)) {
                    CallBarringResetPreference.this.mCallBarringInterface.cancelAllBySequence(CallBarringResetPreference.this.mPassword);
                } else {
                    handleSetCallBarringResponse(message);
                }
            } else {
                Message messageObtainMessage = CallBarringResetPreference.this.mHandler.obtainMessage(message.what + 1, message.arg1, message.arg2, message.obj);
                if ("".equals(CallBarringResetPreference.this.mPassword) && !MiuiImsPhoneUtils.isCbOverImsNoNeedPassword(CallBarringResetPreference.this.mPhone, "AB") && (!CallBarringResetPreference.this.mPhone.isUtEnabled() || !MiuiImsPhoneUtils.isCbNoNeedPassword(CallBarringResetPreference.this.mPhone))) {
                    if (CallBarringResetPreference.this.mTcpListener != null) {
                        CallBarringResetPreference.this.mTcpListener.onFinished(CallBarringResetPreference.this, false);
                    }
                    CallBarringResetPreference callBarringResetPreference = CallBarringResetPreference.this;
                    callBarringResetPreference.showPasswordDialog(callBarringResetPreference.mContext.getString(R.string.deactivate_all), messageObtainMessage);
                } else {
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallBarringOption(CallBarringResetPreference.this.mPhone, "AB", false, CallBarringResetPreference.this.mPassword, messageObtainMessage);
                }
            }
            asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception == null) {
            }
        }

        private void handleSetCallBarringResponse(Message message) {
            int i;
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Throwable th = asyncResult.exception;
            CallBarringResetPreference callBarringResetPreference = CallBarringResetPreference.this;
            if (th != null) {
                if (callBarringResetPreference.DBG) {
                    Log.i(CallBarringResetPreference.LOG_TAG, "handleSetCallBarringResponse: ar.exception=" + asyncResult.exception);
                }
                CommandException commandException = asyncResult.exception;
                if (commandException.getCommandError() == CommandException.Error.PASSWORD_INCORRECT) {
                    i = 700;
                } else {
                    i = commandException.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE ? 800 : MiRilHook.HOOK_ID_START_URC_BY_AT;
                }
                CallBarringResetPreference.this.mCallBarringInterface.setErrorState(i);
                CallBarringResetPreference.this.mTcpListener.onError(CallBarringResetPreference.this, i);
                return;
            }
            if (callBarringResetPreference.DBG) {
                Log.i(CallBarringResetPreference.LOG_TAG, "handleSetCallBarringResponse is called without exception");
            }
            boolean z = CallBarringResetPreference.this.mCallBarringInterface instanceof CallBarring.CallBarringFragment;
            CallBarringResetPreference callBarringResetPreference2 = CallBarringResetPreference.this;
            if (z) {
                CallBarring.CallBarringFragment callBarringFragment = (CallBarring.CallBarringFragment) callBarringResetPreference2.mCallBarringInterface;
                CallBarringBasePreference callBarringBasePreference = (CallBarringBasePreference) callBarringFragment.getPreferenceScreen().findPreference("all_outing_key");
                if (callBarringBasePreference != null) {
                    callBarringFragment.resetIndex(0);
                    callBarringBasePreference.init(callBarringFragment, false);
                    return;
                }
                return;
            }
            callBarringResetPreference2.mCallBarringInterface.doCancelAllState();
        }
    }
}
