package com.android.phone.settings.handler;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.PhoneProxy;
import com.android.phone.utils.Utils;

/* JADX INFO: loaded from: classes.dex */
public class FullForwardingHandler extends Handler {
    private static final String LOG_TAG = "FullForwardingHandler";
    private int mAction;
    private FullForwardingListener mListener;
    private String mNumber;
    private Phone mPhone;
    private int mReason;
    private int mServiceClass;
    private int mTimeSeconds;

    public interface FullForwardingListener {
        void onCallForwardFail(boolean z, CommandException commandException);

        void onCallForwardStart(boolean z);

        void onCallForwardSuccess(boolean z, CallForwardInfo callForwardInfo);
    }

    public FullForwardingHandler(FullForwardingListener fullForwardingListener, Phone phone) {
        super(Looper.getMainLooper());
        this.mReason = 0;
        this.mServiceClass = 1;
        this.mTimeSeconds = 0;
        this.mNumber = "";
        this.mListener = fullForwardingListener;
        this.mPhone = phone;
    }

    public void startQuery() {
        FullForwardingListener fullForwardingListener = this.mListener;
        if (fullForwardingListener != null) {
            fullForwardingListener.onCallForwardStart(true);
        }
        PhoneProxy.getCallForwardingOption(this.mPhone, this.mReason, this.mServiceClass, obtainMessage(0, 0, 0, null));
    }

    public void enableCallForward(String str) {
        FullForwardingListener fullForwardingListener = this.mListener;
        if (fullForwardingListener != null) {
            fullForwardingListener.onCallForwardStart(false);
        }
        this.mNumber = str;
        this.mAction = 3;
        PhoneProxy.setCallForwardingOption(this.mPhone, 3, this.mReason, str, this.mServiceClass, this.mTimeSeconds, obtainMessage(4, 3, 4, null));
    }

    public void disableCallForward() {
        FullForwardingListener fullForwardingListener = this.mListener;
        if (fullForwardingListener != null) {
            fullForwardingListener.onCallForwardStart(false);
        }
        this.mAction = 0;
        PhoneProxy.setCallForwardingOption(this.mPhone, 0, this.mReason, this.mNumber, this.mServiceClass, this.mTimeSeconds, obtainMessage(4, 0, 4, null));
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 0 || i == 1 || i == 2) {
            getCFResponse(message);
            return;
        }
        if (i == 4 || i == 5 || i == 6) {
            setCFResponse(message);
            return;
        }
        if (i == 12) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Utils.Cfu cfu = (Utils.Cfu) asyncResult.userObj;
            if (asyncResult.exception == null && (cfu.mServiceClass & 1) != 0) {
                this.mPhone.setVoiceCallForwardingFlag(1, message.arg1 == 1, cfu.mSetCfNumber);
            }
            Message message2 = cfu.mOnComplete;
            if (message2 != null) {
                AsyncResult.forMessage(message2, asyncResult.result, asyncResult.exception);
                setCFResponse(cfu.mOnComplete);
                return;
            }
            return;
        }
        if (i != 13) {
            return;
        }
        AsyncResult asyncResult2 = (AsyncResult) message.obj;
        CallForwardInfo[] callForwardInfoArr = (CallForwardInfo[]) asyncResult2.result;
        if (asyncResult2.exception == null) {
            if (callForwardInfoArr == null || callForwardInfoArr.length == 0) {
                this.mPhone.setVoiceCallForwardingFlag(1, false, (String) null);
            } else {
                for (CallForwardInfo callForwardInfo : callForwardInfoArr) {
                    if ((callForwardInfo.serviceClass & 1) != 0) {
                        this.mPhone.setVoiceCallForwardingFlag(1, callForwardInfo.status == 1, callForwardInfo.number);
                        break;
                    }
                }
            }
        }
        Message message3 = (Message) asyncResult2.userObj;
        if (message3 != null) {
            AsyncResult.forMessage(message3, asyncResult2.result, asyncResult2.exception);
            getCFResponse(message3);
        }
    }

    private void getCFResponse(Message message) {
        int i = message.what;
        if (i != 2) {
            Object obj = message.obj;
            if (((AsyncResult) obj).exception != null) {
                PhoneProxy.getCallForwardingOption(this.mPhone, this.mReason, this.mServiceClass, obtainMessage(i + 1, message.arg1, message.arg2, obj));
                return;
            }
        }
        handleGetCFResponse(message);
    }

    private void setCFResponse(Message message) {
        int i = message.what;
        if (i != 6 && ((AsyncResult) message.obj).exception != null) {
            PhoneProxy.setCallForwardingOption(this.mPhone, this.mAction, this.mReason, this.mNumber, this.mServiceClass, this.mTimeSeconds, obtainMessage(i + 1, message.arg1, message.arg2));
        } else {
            handleSetCFResponse(message);
        }
    }

    private void handleGetCFResponse(Message message) {
        logd("handleGetCFResponse: done");
        if (this.mListener == null) {
            logd("mListener==null");
            return;
        }
        boolean z = message.arg2 == 0;
        AsyncResult asyncResult = (AsyncResult) message.obj;
        if (asyncResult.exception != null) {
            logd("handleGetCFResponse: ar.exception=" + asyncResult.exception);
            this.mListener.onCallForwardFail(z, (CommandException) asyncResult.exception);
            return;
        }
        CallForwardInfo[] callForwardInfoArr = (CallForwardInfo[]) asyncResult.result;
        if (callForwardInfoArr.length == 0) {
            logd("handleGetCFResponse: cfInfoArray.length==0");
            this.mListener.onCallForwardFail(z, null);
            return;
        }
        int length = callForwardInfoArr.length;
        for (int i = 0; i < length; i++) {
            logd("handleGetCFResponse, cfInfoArray[" + i + "]=" + callForwardInfoArr[i]);
            int i2 = this.mServiceClass;
            CallForwardInfo callForwardInfo = callForwardInfoArr[i];
            if ((i2 & callForwardInfo.serviceClass) != 0) {
                this.mListener.onCallForwardSuccess(z, callForwardInfo);
            }
        }
    }

    private void handleSetCFResponse(Message message) {
        logd("handleSetCFResponse: done");
        if (this.mListener == null) {
            logd("mListener==null");
            return;
        }
        boolean z = message.arg2 == 0;
        AsyncResult asyncResult = (AsyncResult) message.obj;
        Throwable th = asyncResult.exception;
        if (th != null) {
            logd("handleSetCFResponse: ar.exception=" + asyncResult.exception);
            this.mListener.onCallForwardFail(z, (CommandException) asyncResult.exception);
            return;
        }
        PhoneProxy.getCallForwardingOption(this.mPhone, this.mReason, this.mServiceClass, obtainMessage(0, message.arg1, 4, th));
    }

    public void destroy() {
        removeCallbacksAndMessages(null);
        this.mListener = null;
    }

    private void logd(String str) {
        Log.d(LOG_TAG, str);
    }
}
