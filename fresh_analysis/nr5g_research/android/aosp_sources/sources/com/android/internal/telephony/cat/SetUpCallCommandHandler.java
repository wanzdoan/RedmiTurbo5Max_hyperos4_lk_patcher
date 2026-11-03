package com.android.internal.telephony.cat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.PreciseCallState;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsReasonInfo;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.PhoneInternalInterface;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class SetUpCallCommandHandler extends Handler {
    private CallStateCallback mCallback;
    private final Handler mCaller;
    private final Context mContext;
    private final Runnable mDurationExceededRunnable;
    private boolean mNeedRedial;
    private CatCmdMessage.CallSettings mSettings;
    private final int mSubId;

    public SetUpCallCommandHandler(Looper looper, Handler handler, Context context, int i) {
        super(looper);
        this.mDurationExceededRunnable = new Runnable() { // from class: com.android.internal.telephony.cat.SetUpCallCommandHandler$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        this.mNeedRedial = false;
        this.mCaller = handler;
        this.mContext = context;
        this.mSubId = i;
    }

    public void start(final CatCmdMessage.CallSettings callSettings) {
        if (!canDial(callSettings.setUpCallType)) {
            finishAndNotifyResult(ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS);
            return;
        }
        this.mSettings = callSettings;
        this.mCallback = new CallStateCallback();
        try {
            TelephonyManager.from(this.mContext).registerTelephonyCallback(new Executor() { // from class: com.android.internal.telephony.cat.SetUpCallCommandHandler$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.lambda$start$1(runnable);
                }
            }, this.mCallback);
            if (callSettings.redial) {
                startRedialTimer(callSettings.duration);
            }
            if (callSettings.setUpCallType == CatCmdMessage.SetUpCallType.DISCONNECT_ALL_OTHERS && TelephonyManager.from(this.mContext).getCallStateForSubscription() != 0) {
                CatLog.d(this, "Wait for disconnecting other calls");
                disconnectAll();
                postDelayed(new Runnable() { // from class: com.android.internal.telephony.cat.SetUpCallCommandHandler$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$start$2(callSettings);
                    }
                }, 2000L);
                return;
            }
            lambda$start$2(callSettings);
        } catch (IllegalStateException e) {
            CatLog.e(this, "Failed to register TelephonyCallback: " + e.getMessage());
            this.mCallback = null;
            finishAndNotifyResult(ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$1(Runnable runnable) {
        super.post(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: dial, reason: merged with bridge method [inline-methods] */
    public void lambda$start$2(CatCmdMessage.CallSettings callSettings) {
        PhoneAccountHandle phoneAccountHandle = getPhoneAccountHandle();
        if (phoneAccountHandle == null) {
            CatLog.e(this, "No PhoneAccountHandle found for subId: " + this.mSubId);
            finishAndNotifyResult(ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS);
            return;
        }
        CallManager callManager = CallManager.getInstance(this.mContext);
        try {
            callManager.dial(callManager.getPhone(this.mSubId), callSettings.address, new PhoneInternalInterface.DialArgs.Builder().setSkipFdnCheck(true).build());
            Bundle bundle = new Bundle();
            bundle.putString("android.telecom.extra.CALL_SUBJECT", callSettings.callMsg.text);
            ((TelecomManager) this.mContext.getSystemService(TelecomManager.class)).addNewUnknownCall(phoneAccountHandle, bundle);
        } catch (CallStateException e) {
            CatLog.e(this, e.getMessage());
            finishAndNotifyResult(ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS);
        }
    }

    private boolean canDial(CatCmdMessage.SetUpCallType setUpCallType) {
        TelecomManager telecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
        CallManager callManager = CallManager.getInstance(this.mContext);
        if (telecomManager.isInEmergencyCall()) {
            return false;
        }
        if (setUpCallType == CatCmdMessage.SetUpCallType.IF_NOT_BUSY && telecomManager.isInCall()) {
            return false;
        }
        return (setUpCallType == CatCmdMessage.SetUpCallType.PUT_OTHERS_HOLD && callManager.hasActiveFgCall() && callManager.hasActiveBgCall()) ? false : true;
    }

    private void disconnectAll() {
        CallManager callManager = CallManager.getInstance(this.mContext);
        callManager.getForegroundCalls().stream().forEach(new Consumer() { // from class: com.android.internal.telephony.cat.SetUpCallCommandHandler$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Call) obj).hangupIfAlive();
            }
        });
        callManager.getBackgroundCalls().stream().forEach(new Consumer() { // from class: com.android.internal.telephony.cat.SetUpCallCommandHandler$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Call) obj).hangupIfAlive();
            }
        });
        callManager.getRingingCalls().stream().forEach(new Consumer() { // from class: com.android.internal.telephony.cat.SetUpCallCommandHandler$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Call) obj).hangupIfAlive();
            }
        });
    }

    private void startRedialTimer(Duration duration) {
        if (duration == null || duration.timeInterval <= 0) {
            return;
        }
        this.mNeedRedial = true;
        postDelayed(this.mDurationExceededRunnable, duration.getIntervalInMillis());
        CatLog.d(this, "Redial timer started for " + duration.getIntervalInMillis());
    }

    private void stopRedialTimer() {
        removeCallbacks(this.mDurationExceededRunnable);
        this.mNeedRedial = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onDurationExceeded, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        CatLog.d(this, "Redial duration exceeded, stop redial attempts");
        this.mNeedRedial = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishAndNotifyResult(ResultCode resultCode) {
        CatLog.d(this, "Finishing with result: " + resultCode);
        stopRedialTimer();
        if (this.mCallback != null) {
            TelephonyManager.from(this.mContext).unregisterTelephonyCallback(this.mCallback);
            this.mCallback = null;
        }
        this.mCaller.obtainMessage(11, resultCode).sendToTarget();
    }

    private PhoneAccountHandle getPhoneAccountHandle() {
        return TelephonyManager.from(this.mContext).getPhoneAccountHandleForSubscriptionId(this.mSubId);
    }

    private class CallStateCallback extends TelephonyCallback implements TelephonyCallback.PreciseCallStateListener, TelephonyCallback.CallDisconnectCauseListener, TelephonyCallback.ImsCallDisconnectCauseListener {
        private boolean mIsNewCallPlaced;

        private CallStateCallback() {
            this.mIsNewCallPlaced = false;
        }

        public void onPreciseCallStateChanged(PreciseCallState preciseCallState) {
            if (preciseCallState.getForegroundCallState() == 3) {
                this.mIsNewCallPlaced = true;
            }
            if (this.mIsNewCallPlaced && preciseCallState.getForegroundCallState() == 1) {
                SetUpCallCommandHandler.this.finishAndNotifyResult(ResultCode.OK);
            }
        }

        @Override // android.telephony.TelephonyCallback.CallDisconnectCauseListener
        public void onCallDisconnectCauseChanged(int i, int i2) {
            if (!this.mIsNewCallPlaced || i == -1 || i == 0) {
                return;
            }
            CatLog.d(this, "onCallDisconnectCauseChanged: " + i + "/" + i2);
            handleDisconnect(i == 3);
        }

        @Override // android.telephony.TelephonyCallback.ImsCallDisconnectCauseListener
        public void onImsCallDisconnectCauseChanged(ImsReasonInfo imsReasonInfo) {
            if (!this.mIsNewCallPlaced || imsReasonInfo == null) {
                return;
            }
            CatLog.d(this, "onImsCallDisconnectCauseChanged: " + imsReasonInfo);
            handleDisconnect(imsReasonInfo.getCode() == 501);
        }

        private void redial() {
            CatLog.d(this, "Attempting redial");
            this.mIsNewCallPlaced = false;
            SetUpCallCommandHandler setUpCallCommandHandler = SetUpCallCommandHandler.this;
            setUpCallCommandHandler.lambda$start$2(setUpCallCommandHandler.mSettings);
        }

        private void handleDisconnect(boolean z) {
            SetUpCallCommandHandler setUpCallCommandHandler = SetUpCallCommandHandler.this;
            if (z) {
                setUpCallCommandHandler.finishAndNotifyResult(ResultCode.USER_CLEAR_DOWN_CALL);
            } else if (setUpCallCommandHandler.mNeedRedial) {
                redial();
            } else {
                SetUpCallCommandHandler.this.finishAndNotifyResult(ResultCode.NETWORK_CRNTLY_UNABLE_TO_PROCESS);
            }
        }
    }
}
