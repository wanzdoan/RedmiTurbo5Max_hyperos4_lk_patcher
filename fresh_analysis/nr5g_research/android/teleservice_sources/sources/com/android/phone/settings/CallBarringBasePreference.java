package com.android.phone.settings;

import android.R;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.custompreferences.CustomCheckBoxPreference;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.MiuiSuppServiceStatistic;
import com.xiaomi.mirilhook.MiRilHook;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class CallBarringBasePreference extends CustomCheckBoxPreference implements Preference.OnPreferenceClickListener {
    private static final String LOG_TAG = CallBarringBasePreference.class.getSimpleName();
    private boolean DBG;
    private CallBarringInterface mCallBarringInterface;
    private Context mContext;
    private boolean mCurrentClickState;
    private String mFacility;
    private MyHandler mHandler;
    private String mPassword;
    private Phone mPhone;
    private boolean mResult;
    private int mServiceClass;
    private TimeConsumingPreferenceListener mTcpListener;
    private int mTitle;

    public CallBarringBasePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
        this.mFacility = "";
        this.mCurrentClickState = false;
        this.mCallBarringInterface = null;
        this.mResult = true;
        this.mServiceClass = 1;
        setEnabled(false);
        this.mContext = context;
        this.mPhone = MiuiPhoneUtils.getPhone(0);
    }

    public void setRefreshInterface(CallBarringInterface callBarringInterface) {
        this.mCallBarringInterface = callBarringInterface;
    }

    public void setmTitle(int i) {
        this.mTitle = i;
    }

    public void setmFacility(String str) {
        this.mFacility = str;
    }

    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    protected void onClick() {
        this.mCurrentClickState = !isChecked();
    }

    @Override // com.android.phone.custompreferences.CustomCheckBoxPreference, androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View viewFindViewById = preferenceViewHolder.itemView.findViewById(R.id.title);
        if (viewFindViewById == null || !(viewFindViewById instanceof TextView)) {
            return;
        }
        ((TextView) viewFindViewById).setEllipsize(TextUtils.TruncateAt.valueOf("MIDDLE"));
    }

    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    public boolean shouldDisableDependents() {
        return !super.shouldDisableDependents();
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        doPreferenceClick(this.mContext.getString(this.mTitle));
        return true;
    }

    public void setPhoneInstance(Phone phone) {
        this.mPhone = phone;
    }

    public void setCallState(String str) {
        setCallState(this.mFacility, false, str);
        TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
        if (timeConsumingPreferenceListener != null) {
            timeConsumingPreferenceListener.onStarted(this, false);
        }
    }

    private void doPreferenceClick(String str) {
        if (MiuiPhoneUtils.maybeShowDialogForSuppService(this.mPhone, 0)) {
            return;
        }
        if (MiuiImsPhoneUtils.isCbOverImsNoNeedPassword(this.mPhone, this.mFacility)) {
            TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
            if (timeConsumingPreferenceListener != null) {
                timeConsumingPreferenceListener.onStarted(this, false);
                this.mPassword = "";
                setCallState(this.mFacility, this.mCurrentClickState, "");
            }
            Log.d(LOG_TAG, "cb over UT don't need password");
            return;
        }
        showPasswordDialog(str, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPasswordDialog(final String str, final Message message) {
        final View viewInflate = LayoutInflater.from(getContext()).inflate(com.android.phone.R.layout.call_barring_option, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(com.android.phone.R.id.ViewTop)).setText(this.mContext.getString(com.android.phone.R.string.enter_callbarring_password, 4));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(viewInflate);
        builder.setTitle(str);
        builder.setNegativeButton(com.android.phone.R.string.cancel, (DialogInterface.OnClickListener) null);
        builder.setPositiveButton(com.android.phone.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallBarringBasePreference.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                String string = ((EditText) viewInflate.findViewById(com.android.phone.R.id.EditPassword)).getText().toString();
                boolean zValidatePassword = CallBarringBasePreference.this.validatePassword(string);
                CallBarringBasePreference callBarringBasePreference = CallBarringBasePreference.this;
                if (!zValidatePassword) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(callBarringBasePreference.mContext);
                    builder2.setTitle(str);
                    builder2.setMessage(CallBarringBasePreference.this.mContext.getText(com.android.phone.R.string.wrong_password));
                    builder2.setCancelable(false);
                    builder2.setPositiveButton(com.android.phone.R.string.ok, (DialogInterface.OnClickListener) null);
                    builder2.create().show();
                    return;
                }
                callBarringBasePreference.mPassword = string;
                if (CallBarringBasePreference.this.mTcpListener != null) {
                    CallBarringBasePreference.this.mTcpListener.onStarted(CallBarringBasePreference.this, false);
                    if (message != null) {
                        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallBarringOption(CallBarringBasePreference.this.mPhone, CallBarringBasePreference.this.mFacility, CallBarringBasePreference.this.mCurrentClickState, CallBarringBasePreference.this.mPassword, message);
                    } else {
                        CallBarringBasePreference callBarringBasePreference2 = CallBarringBasePreference.this;
                        callBarringBasePreference2.setCallState(callBarringBasePreference2.mFacility, CallBarringBasePreference.this.mCurrentClickState, string);
                    }
                }
            }
        });
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCallState(String str, String str2, boolean z) {
        Message messageObtainMessage;
        if (this.DBG) {
            Log.i(LOG_TAG, "getCallState() is called with facility is " + str + "password is " + str2 + "reason is " + z);
        }
        if (z) {
            if (isCallBarringFacilityNotSupported(str)) {
                this.mResult = true;
                TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
                if (timeConsumingPreferenceListener != null) {
                    timeConsumingPreferenceListener.onFinished(this, true);
                }
                setEnabled(false);
                setSummary(this.mContext.getString(com.android.phone.R.string.lable_network_not_supported));
                return;
            }
            messageObtainMessage = this.mHandler.obtainMessage(0, 0, 0, null);
        } else {
            messageObtainMessage = this.mHandler.obtainMessage(0, 0, 2, null);
        }
        if (this.mPhone.getPhoneType() == 1) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getCallBarringOption(this.mPhone, str, str2, messageObtainMessage);
        }
    }

    private boolean isCallBarringFacilityNotSupported(String str) {
        if (!MiuiPhoneUtils.isCmccSim(this.mPhone) || !MiuiPhoneUtils.isSSOperatedByUt(this.mPhone) || str.equals("AI") || str.equals("IR")) {
            return false;
        }
        Log.i(LOG_TAG, "isCallBarringFacilitySupportedInVoLTE facility=".concat(str));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCallState(String str, boolean z, String str2) {
        if (this.DBG) {
            Log.i(LOG_TAG, "setCallState() is called with facility is " + str + "password is " + str2 + "enable is " + z);
        }
        Message messageObtainMessage = this.mHandler.obtainMessage(2, 0, 2, null);
        if (this.mPhone.getPhoneType() == 1) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallBarringOption(this.mPhone, str, z, str2, messageObtainMessage);
        }
    }

    void init(TimeConsumingPreferenceListener timeConsumingPreferenceListener, boolean z) {
        setOnPreferenceClickListener(this);
        this.mTcpListener = timeConsumingPreferenceListener;
        if (z) {
            return;
        }
        if (timeConsumingPreferenceListener != null) {
            Log.i(LOG_TAG, "init() is called");
            this.mTcpListener.onStarted(this, true);
        }
        getCallState(this.mFacility, "", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validatePassword(String str) {
        return str != null && str.length() == 4;
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        /* JADX WARN: Code duplicated, block: B:40:0x0137  */
        /* JADX WARN: Code duplicated, block: B:45:? A[RETURN, SYNTHETIC] */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncResult asyncResult;
            int i;
            int i2 = message.what;
            if (i2 == 0) {
                if (message.arg2 != 2) {
                    MiuiSuppServiceStatistic.getInstance(CallBarringBasePreference.this.mContext).recordCountClickScene(message.what, MiRilHook.HOOK_ID_START_URC_BY_AT, CallBarringBasePreference.this.mPhone);
                }
            } else {
                if (i2 != 1) {
                    if (i2 == 2) {
                        MiuiSuppServiceStatistic.getInstance(CallBarringBasePreference.this.mContext).recordCountClickScene(message.what, MiRilHook.HOOK_ID_START_URC_BY_AT, CallBarringBasePreference.this.mPhone);
                    } else if (i2 == 3) {
                    }
                    if (((AsyncResult) message.obj).exception == null || message.what == 3) {
                        handleSetCallBarringResponse(message);
                    } else {
                        Message messageObtainMessage = CallBarringBasePreference.this.mHandler.obtainMessage(message.what + 1, message.arg1, message.arg2, message.obj);
                        if ("".equals(CallBarringBasePreference.this.mPassword) && !MiuiImsPhoneUtils.isCbOverImsNoNeedPassword(CallBarringBasePreference.this.mPhone, CallBarringBasePreference.this.mFacility) && (!CallBarringBasePreference.this.mPhone.isUtEnabled() || !MiuiImsPhoneUtils.isCbNoNeedPassword(CallBarringBasePreference.this.mPhone))) {
                            if (CallBarringBasePreference.this.mTcpListener != null) {
                                CallBarringBasePreference.this.mTcpListener.onFinished(CallBarringBasePreference.this, false);
                            }
                            CallBarringBasePreference callBarringBasePreference = CallBarringBasePreference.this;
                            callBarringBasePreference.showPasswordDialog(callBarringBasePreference.mContext.getString(CallBarringBasePreference.this.mTitle), messageObtainMessage);
                        } else {
                            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallBarringOption(CallBarringBasePreference.this.mPhone, CallBarringBasePreference.this.mFacility, CallBarringBasePreference.this.mCurrentClickState, CallBarringBasePreference.this.mPassword, messageObtainMessage);
                        }
                    }
                }
                asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception != null) {
                    i = message.what;
                    if (i != 1 || i == 3) {
                        MiuiSuppServiceStatistic.getInstance(CallBarringBasePreference.this.getContext()).recordExceptionScene(message.what, MiRilHook.HOOK_ID_START_URC_BY_AT, asyncResult.exception, CallBarringBasePreference.this.mPhone);
                    }
                    return;
                }
            }
            if (((AsyncResult) message.obj).exception == null || message.what == 1) {
                handleGetCallBarringResponse(message);
            } else {
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getCallBarringOption(CallBarringBasePreference.this.mPhone, CallBarringBasePreference.this.mFacility, CallBarringBasePreference.this.mPassword, CallBarringBasePreference.this.mHandler.obtainMessage(message.what + 1, message.arg1, message.arg2, message.obj));
            }
            asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                i = message.what;
                if (i != 1) {
                }
                MiuiSuppServiceStatistic.getInstance(CallBarringBasePreference.this.getContext()).recordExceptionScene(message.what, MiRilHook.HOOK_ID_START_URC_BY_AT, asyncResult.exception, CallBarringBasePreference.this.mPhone);
            }
        }

        private void handleSetCallBarringResponse(Message message) {
            int i;
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Throwable th = asyncResult.exception;
            CallBarringBasePreference callBarringBasePreference = CallBarringBasePreference.this;
            if (th != null) {
                if (callBarringBasePreference.DBG) {
                    Log.i(CallBarringBasePreference.LOG_TAG, "handleSetCallBarringResponse: ar.exception=" + asyncResult.exception);
                }
                CommandException commandException = asyncResult.exception;
                if (commandException.getCommandError() == CommandException.Error.PASSWORD_INCORRECT) {
                    i = 700;
                } else {
                    i = commandException.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE ? 800 : MiRilHook.HOOK_ID_START_URC_BY_AT;
                }
                CallBarringBasePreference.this.mCallBarringInterface.setErrorState(i);
                if (CallBarringBasePreference.this.mTcpListener != null) {
                    CallBarringBasePreference.this.mTcpListener.onFinished(CallBarringBasePreference.this, false);
                }
                CallBarringBasePreference callBarringBasePreference2 = CallBarringBasePreference.this;
                if (!callBarringBasePreference2.shouldShowErrorDialog(callBarringBasePreference2.mFacility) || CallBarringBasePreference.this.mTcpListener == null) {
                    return;
                }
                CallBarringBasePreference.this.mTcpListener.onError(CallBarringBasePreference.this, i);
                return;
            }
            if (callBarringBasePreference.DBG) {
                Log.i(CallBarringBasePreference.LOG_TAG, "handleSetCallBarringResponse is called without exception");
            }
            CallBarringBasePreference callBarringBasePreference3 = CallBarringBasePreference.this;
            callBarringBasePreference3.getCallState(callBarringBasePreference3.mFacility, "", false);
        }

        private void handleGetCallBarringResponse(Message message) {
            String string;
            int i;
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Throwable th = asyncResult.exception;
            CallBarringBasePreference callBarringBasePreference = CallBarringBasePreference.this;
            if (th != null) {
                callBarringBasePreference.mResult = false;
                if (CallBarringBasePreference.this.DBG) {
                    Log.i(CallBarringBasePreference.LOG_TAG, "handleGetCallBarringResponse: ar.exception=" + asyncResult.exception);
                }
                CommandException commandException = asyncResult.exception;
                if (commandException.getCommandError() == CommandException.Error.PASSWORD_INCORRECT) {
                    i = 700;
                } else {
                    i = commandException.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE ? 800 : MiRilHook.HOOK_ID_START_URC_BY_AT;
                }
                CallBarringBasePreference.this.mCallBarringInterface.setErrorState(i);
                try {
                    CallBarringBasePreference callBarringBasePreference2 = CallBarringBasePreference.this;
                    if (callBarringBasePreference2.shouldShowErrorDialog(callBarringBasePreference2.mFacility) && CallBarringBasePreference.this.mTcpListener != null) {
                        CallBarringBasePreference.this.mTcpListener.onError(CallBarringBasePreference.this, i);
                    }
                } catch (WindowManager.BadTokenException unused) {
                    if (CallBarringBasePreference.this.DBG) {
                        Log.d(CallBarringBasePreference.LOG_TAG, "BadTokenException");
                    }
                }
            } else {
                if (callBarringBasePreference.DBG) {
                    Log.i(CallBarringBasePreference.LOG_TAG, "handleGetCallBarringResponse is called without exception");
                }
                CallBarringBasePreference.this.setEnabled(true);
                int[] iArr = (int[]) asyncResult.result;
                if (iArr != null && iArr.length > 0) {
                    CallBarringBasePreference.this.mResult = true;
                    int i2 = iArr[0];
                    Log.i(CallBarringBasePreference.LOG_TAG, "Current value = " + i2 + "  Current serviceClass = " + CallBarringBasePreference.this.mServiceClass);
                    int i3 = i2 & CallBarringBasePreference.this.mServiceClass;
                    Log.i(CallBarringBasePreference.LOG_TAG, "After value & mServiceClass = " + i3);
                    if (CallBarringBasePreference.this.DBG) {
                        Log.i(CallBarringBasePreference.LOG_TAG, "Value is " + i3);
                    }
                    CallBarringBasePreference callBarringBasePreference3 = CallBarringBasePreference.this;
                    if (i3 == 0) {
                        string = callBarringBasePreference3.mContext.getString(com.android.phone.R.string.lable_disable);
                        CallBarringBasePreference.this.setChecked(false);
                    } else {
                        string = callBarringBasePreference3.mContext.getString(com.android.phone.R.string.lable_enable);
                        CallBarringBasePreference.this.setChecked(true);
                    }
                    CallBarringBasePreference.this.setSummary(string);
                } else {
                    CallBarringBasePreference.this.mResult = false;
                    if (CallBarringBasePreference.this.DBG) {
                        Log.i(CallBarringBasePreference.LOG_TAG, "handleGetCallBarringResponse ar.result get error");
                    }
                }
            }
            int i4 = message.arg2;
            if (i4 == 0 || i4 == 1) {
                if (CallBarringBasePreference.this.mTcpListener != null) {
                    CallBarringBasePreference.this.mTcpListener.onFinished(CallBarringBasePreference.this, true);
                }
            } else if (CallBarringBasePreference.this.mTcpListener != null) {
                CallBarringBasePreference.this.mTcpListener.onFinished(CallBarringBasePreference.this, false);
            }
        }
    }

    public boolean isSuccess() {
        return this.mResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldShowErrorDialog(String str) {
        if ((MiuiPhoneUtils.isNovaSim(this.mPhone) && str.equals("OX")) || ((MiuiPhoneUtils.isByA1Sim(this.mPhone) && str.equals("IR")) || (MiuiPhoneUtils.isBGA1Sim(this.mPhone) && !str.equals("AI") && !str.equals("IR") && !str.equals("AO")))) {
            Log.i(LOG_TAG, "isBAICRNotSupported false facility=" + str);
            return false;
        }
        Log.d(LOG_TAG, "isBAICRNotSupported true facility=" + str);
        return true;
    }
}
