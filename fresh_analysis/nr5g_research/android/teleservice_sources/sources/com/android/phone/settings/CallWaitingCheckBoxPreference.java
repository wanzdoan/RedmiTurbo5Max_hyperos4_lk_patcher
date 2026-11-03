package com.android.phone.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.custompreferences.CustomCheckBoxPreference;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.MiuiSuppServiceStatistic;
import com.android.phone.utils.MiuiJpOperatorCustimizedUtils;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class CallWaitingCheckBoxPreference extends CustomCheckBoxPreference {
    private boolean DBG;
    private boolean isOpen;
    private final MyHandler mHandler;
    private Phone mPhone;
    private TimeConsumingPreferenceListener mTcpListener;
    public AlertDialog openComfirm;

    public CallWaitingCheckBoxPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
        this.isOpen = false;
    }

    public CallWaitingCheckBoxPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
        this.isOpen = false;
    }

    public CallWaitingCheckBoxPreference(Context context) {
        super(context);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
        this.isOpen = false;
    }

    public void init(TimeConsumingPreferenceListener timeConsumingPreferenceListener, boolean z, int i) {
        if (this.DBG) {
            Log.d("CallWaitingCheckBoxPreference", "CallWaitingCheckBoxPreference init, subscription :" + i);
        }
        this.mPhone = MiuiPhoneUtils.getPhone(i);
        this.mTcpListener = timeConsumingPreferenceListener;
        Log.d("CallWaitingCheckBoxPreference", "mPhone.getPhoneType() :" + this.mPhone.getPhoneType());
        if (this.mPhone.getPhoneType() == 7) {
            setEnabled(false);
            return;
        }
        if (z) {
            return;
        }
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getCallWaitingOption(this.mPhone, this.mHandler.obtainMessage(0, 0, 0));
        TimeConsumingPreferenceListener timeConsumingPreferenceListener2 = this.mTcpListener;
        if (timeConsumingPreferenceListener2 != null) {
            timeConsumingPreferenceListener2.onStarted(this, true);
        }
    }

    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    protected void onClick() {
        super.onClick();
        if (isChecked()) {
            this.isOpen = false;
            if (this.openComfirm == null) {
                this.openComfirm = new AlertDialog.Builder(getContext()).setMessage(R.string.callwait_open_hint).setPositiveButton(R.string.open_miui, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallWaitingCheckBoxPreference.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CallWaitingCheckBoxPreference.this.isOpen = true;
                        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallWaitingOption(CallWaitingCheckBoxPreference.this.mPhone, true, CallWaitingCheckBoxPreference.this.mHandler.obtainMessage(4));
                        if (CallWaitingCheckBoxPreference.this.mTcpListener != null) {
                            CallWaitingCheckBoxPreference.this.mTcpListener.onStarted(CallWaitingCheckBoxPreference.this, false);
                        }
                    }
                }).setNegativeButton(R.string.back, (DialogInterface.OnClickListener) null).create();
            }
            this.openComfirm.show();
            this.openComfirm.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.phone.settings.CallWaitingCheckBoxPreference.2
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialogInterface) {
                    if (CallWaitingCheckBoxPreference.this.isOpen) {
                        return;
                    }
                    CallWaitingCheckBoxPreference.this.setChecked(false);
                }
            });
            return;
        }
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallWaitingOption(this.mPhone, false, this.mHandler.obtainMessage(4));
        TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
        if (timeConsumingPreferenceListener != null) {
            timeConsumingPreferenceListener.onStarted(this, false);
        }
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        /* JADX WARN: Code duplicated, block: B:31:0x00ac  */
        /* JADX WARN: Code duplicated, block: B:36:? A[RETURN, SYNTHETIC] */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i;
            AsyncResult asyncResult = (AsyncResult) message.obj;
            int i2 = message.what;
            if (i2 == 0) {
                if (message.arg2 != 4) {
                    MiuiSuppServiceStatistic.getInstance(CallWaitingCheckBoxPreference.this.getContext()).recordCountClickScene(message.what, 200, CallWaitingCheckBoxPreference.this.mPhone);
                }
            } else {
                if (i2 != 1 && i2 != 2) {
                    if (i2 == 4) {
                        MiuiSuppServiceStatistic.getInstance(CallWaitingCheckBoxPreference.this.getContext()).recordCountClickScene(message.what, 200, CallWaitingCheckBoxPreference.this.mPhone);
                    } else if (i2 == 5 || i2 == 6) {
                    }
                    if (asyncResult.exception == null || message.what == 6) {
                        handleSetCallWaitingResponse(message);
                    } else {
                        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallWaitingOption(CallWaitingCheckBoxPreference.this.mPhone, CallWaitingCheckBoxPreference.this.isChecked(), CallWaitingCheckBoxPreference.this.mHandler.obtainMessage(message.what + 1));
                    }
                }
                if (asyncResult.exception != null) {
                    i = message.what;
                    if (i != 2 || i == 6) {
                        MiuiSuppServiceStatistic.getInstance(CallWaitingCheckBoxPreference.this.getContext()).recordExceptionScene(message.what, 200, asyncResult.exception, CallWaitingCheckBoxPreference.this.mPhone);
                    }
                    return;
                }
            }
            if (asyncResult.exception == null || message.what == 2) {
                handleGetCallWaitingResponse(message);
            } else {
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getCallWaitingOption(CallWaitingCheckBoxPreference.this.mPhone, CallWaitingCheckBoxPreference.this.mHandler.obtainMessage(message.what + 1, message.arg1, message.arg2));
            }
            if (asyncResult.exception != null) {
                i = message.what;
                if (i != 2) {
                }
                MiuiSuppServiceStatistic.getInstance(CallWaitingCheckBoxPreference.this.getContext()).recordExceptionScene(message.what, 200, asyncResult.exception, CallWaitingCheckBoxPreference.this.mPhone);
            }
        }

        private void handleGetCallWaitingResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            boolean z = false;
            if (CallWaitingCheckBoxPreference.this.mTcpListener != null) {
                int i = message.arg2;
                CallWaitingCheckBoxPreference callWaitingCheckBoxPreference = CallWaitingCheckBoxPreference.this;
                if (i == 4) {
                    callWaitingCheckBoxPreference.mTcpListener.onFinished(CallWaitingCheckBoxPreference.this, false);
                } else {
                    callWaitingCheckBoxPreference.mTcpListener.onFinished(CallWaitingCheckBoxPreference.this, true);
                }
            }
            if (shouldSkipErrorDialog(asyncResult)) {
                CallWaitingCheckBoxPreference.this.setChecked(false);
                return;
            }
            if (asyncResult.exception != null) {
                if (CallWaitingCheckBoxPreference.this.DBG) {
                    Log.d("CallWaitingCheckBoxPreference", "handleGetCallWaitingResponse: ar.exception=" + asyncResult.exception);
                }
                if (CallWaitingCheckBoxPreference.this.mTcpListener != null) {
                    CallWaitingCheckBoxPreference.this.mTcpListener.onException(CallWaitingCheckBoxPreference.this, (CommandException) asyncResult.exception);
                    return;
                }
                return;
            }
            boolean z2 = asyncResult.userObj instanceof Throwable;
            CallWaitingCheckBoxPreference callWaitingCheckBoxPreference2 = CallWaitingCheckBoxPreference.this;
            if (z2) {
                if (callWaitingCheckBoxPreference2.mTcpListener != null) {
                    CallWaitingCheckBoxPreference.this.mTcpListener.onError(CallWaitingCheckBoxPreference.this, 400);
                    return;
                }
                return;
            }
            if (callWaitingCheckBoxPreference2.DBG) {
                Log.d("CallWaitingCheckBoxPreference", "handleGetCallWaitingResponse: CW state successfully queried.");
            }
            int[] iArr = (int[]) asyncResult.result;
            try {
                CallWaitingCheckBoxPreference callWaitingCheckBoxPreference3 = CallWaitingCheckBoxPreference.this;
                if (iArr[0] == 1 && (iArr[1] & 1) == 1) {
                    z = true;
                }
                callWaitingCheckBoxPreference3.setChecked(z);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("CallWaitingCheckBoxPreference", "handleGetCallWaitingResponse: improper result: err =" + e.getMessage());
            }
        }

        private void handleSetCallWaitingResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null && CallWaitingCheckBoxPreference.this.DBG) {
                Log.d("CallWaitingCheckBoxPreference", "handleSetCallWaitingResponse: ar.exception=" + asyncResult.exception);
            }
            if (CallWaitingCheckBoxPreference.this.DBG) {
                Log.d("CallWaitingCheckBoxPreference", "handleSetCallWaitingResponse: re get");
            }
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getCallWaitingOption(CallWaitingCheckBoxPreference.this.mPhone, obtainMessage(0, 4, 4, asyncResult.exception));
        }

        private boolean shouldSkipErrorDialog(AsyncResult asyncResult) {
            if (MiuiJpOperatorCustimizedUtils.isCustForJpKd()) {
                return asyncResult.exception != null || (asyncResult.userObj instanceof Throwable);
            }
            return false;
        }
    }
}
