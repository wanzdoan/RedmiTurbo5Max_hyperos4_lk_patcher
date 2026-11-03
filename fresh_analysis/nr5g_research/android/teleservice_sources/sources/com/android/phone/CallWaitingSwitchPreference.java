package com.android.phone;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.SwitchPreference;
import android.telephony.CarrierConfigManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.settings.SuppServicesUiUtil;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class CallWaitingSwitchPreference extends SwitchPreference {
    private final boolean DBG;
    private boolean mCwClicked;
    private boolean mCwEnabled;
    private ScheduledExecutorService mExecutor;
    private final MyHandler mHandler;
    private boolean mIsDuringUpdateProcess;
    private Phone mPhone;
    private int mQueryStatus;
    private TimeConsumingPreferenceListener mTcpListener;
    private TelephonyManager mTelephonyManager;
    private int mUpdateStatus;
    private boolean mUssdMode;

    public CallWaitingSwitchPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.DBG = true;
        this.mHandler = new MyHandler();
        this.mIsDuringUpdateProcess = false;
        this.mUpdateStatus = 3;
        this.mQueryStatus = 3;
        this.mUssdMode = false;
        this.mCwEnabled = false;
        this.mCwClicked = false;
    }

    public CallWaitingSwitchPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, android.R.attr.switchPreferenceStyle);
    }

    public CallWaitingSwitchPreference(Context context) {
        this(context, null);
    }

    public void init(TimeConsumingPreferenceListener timeConsumingPreferenceListener, boolean z, Phone phone) {
        this.mPhone = phone;
        this.mTcpListener = timeConsumingPreferenceListener;
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.mTelephonyManager = ((TelephonyManager) getContext().getSystemService(TelephonyManager.class)).createForSubscriptionId(phone.getSubId());
        PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService(CarrierConfigManager.class)).getConfigForSubId(phone.getSubId());
        this.mUssdMode = configForSubId != null ? configForSubId.getBoolean("use_call_waiting_ussd_bool", false) : false;
        this.mCwEnabled = false;
        if (z) {
            return;
        }
        Log.d("CallWaitingSwitchPreference", "init getCallWaitingStatus");
        this.mTelephonyManager.getCallWaitingStatus(this.mExecutor, new CallWaitingSwitchPreference$$ExternalSyntheticLambda0(this));
        TimeConsumingPreferenceListener timeConsumingPreferenceListener2 = this.mTcpListener;
        if (timeConsumingPreferenceListener2 != null) {
            timeConsumingPreferenceListener2.onStarted(this, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queryStatusCallBack(int i) {
        Log.d("CallWaitingSwitchPreference", "queryStatusCallBack: CW state " + i);
        this.mQueryStatus = i;
        MyHandler myHandler = this.mHandler;
        myHandler.sendMessage(myHandler.obtainMessage(0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStatusCallBack(int i) {
        Log.d("CallWaitingSwitchPreference", "updateStatusCallBack: CW state " + i + ", and re get");
        this.mUpdateStatus = i;
        if (this.mUssdMode) {
            Log.d("CallWaitingSwitchPreference", "updateStatusCallBack: USSD mode needs to wait 1s since Framework has the limitation");
            final CallWaitingSwitchPreference$$ExternalSyntheticLambda0 callWaitingSwitchPreference$$ExternalSyntheticLambda0 = new CallWaitingSwitchPreference$$ExternalSyntheticLambda0(this);
            try {
                this.mExecutor.schedule(new Runnable() { // from class: com.android.phone.CallWaitingSwitchPreference.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CallWaitingSwitchPreference.this.mTelephonyManager.getCallWaitingStatus(CallWaitingSwitchPreference.this.mExecutor, callWaitingSwitchPreference$$ExternalSyntheticLambda0);
                    }
                }, 1000L, TimeUnit.MILLISECONDS);
                return;
            } catch (Exception e) {
                Log.d("CallWaitingSwitchPreference", "Exception while waiting: " + e);
                return;
            }
        }
        this.mTelephonyManager.getCallWaitingStatus(this.mExecutor, new CallWaitingSwitchPreference$$ExternalSyntheticLambda0(this));
    }

    @Override // android.preference.TwoStatePreference, android.preference.Preference
    protected void onClick() {
        if (PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId()).getBoolean("call_waiting_over_ut_warning_bool", true) && SuppServicesUiUtil.isSsOverUtPrecautions(getContext(), this.mPhone)) {
            SuppServicesUiUtil.showBlockingSuppServicesDialog(getContext(), this.mPhone, getKey()).show();
            return;
        }
        super.onClick();
        boolean zIsChecked = isChecked();
        this.mCwEnabled = zIsChecked;
        this.mCwClicked = true;
        this.mTelephonyManager.setCallWaitingEnabled(zIsChecked, this.mExecutor, new Consumer() { // from class: com.android.phone.CallWaitingSwitchPreference$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.updateStatusCallBack(((Integer) obj).intValue());
            }
        });
        TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
        if (timeConsumingPreferenceListener != null) {
            this.mIsDuringUpdateProcess = true;
            timeConsumingPreferenceListener.onStarted(this, false);
        }
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 0) {
                return;
            }
            updateUi();
        }

        private void updateUi() {
            if (CallWaitingSwitchPreference.this.mTcpListener != null) {
                boolean z = CallWaitingSwitchPreference.this.mIsDuringUpdateProcess;
                CallWaitingSwitchPreference callWaitingSwitchPreference = CallWaitingSwitchPreference.this;
                if (z) {
                    callWaitingSwitchPreference.mTcpListener.onFinished(CallWaitingSwitchPreference.this, false);
                } else {
                    callWaitingSwitchPreference.mTcpListener.onFinished(CallWaitingSwitchPreference.this, true);
                }
            }
            if (CallWaitingSwitchPreference.this.mQueryStatus != 1 && CallWaitingSwitchPreference.this.mQueryStatus != 2 && CallWaitingSwitchPreference.this.mQueryStatus != 3) {
                Log.d("CallWaitingSwitchPreference", "handleGetCallWaitingResponse: Exception:" + CallWaitingSwitchPreference.this.mQueryStatus);
                int i = CallWaitingSwitchPreference.this.mQueryStatus != 5 ? MiRilHook.HOOK_ID_START_URC_BY_AT : 600;
                if (CallWaitingSwitchPreference.this.mTcpListener != null) {
                    CallWaitingSwitchPreference.this.mTcpListener.onError(CallWaitingSwitchPreference.this, i);
                }
                CallWaitingSwitchPreference.this.handleCwFallbackOnError();
                CallWaitingSwitchPreference callWaitingSwitchPreference2 = CallWaitingSwitchPreference.this;
                callWaitingSwitchPreference2.setChecked(callWaitingSwitchPreference2.mCwEnabled);
            } else if (CallWaitingSwitchPreference.this.mQueryStatus == 3 || (CallWaitingSwitchPreference.this.mIsDuringUpdateProcess && CallWaitingSwitchPreference.this.mUpdateStatus != 1 && CallWaitingSwitchPreference.this.mUpdateStatus != 2)) {
                Log.d("CallWaitingSwitchPreference", "handleSetCallWaitingResponse: Exception");
                if (CallWaitingSwitchPreference.this.mTcpListener != null) {
                    CallWaitingSwitchPreference.this.mTcpListener.onError(CallWaitingSwitchPreference.this, 400);
                }
                CallWaitingSwitchPreference.this.handleCwFallbackOnError();
                CallWaitingSwitchPreference callWaitingSwitchPreference3 = CallWaitingSwitchPreference.this;
                callWaitingSwitchPreference3.setChecked(callWaitingSwitchPreference3.mCwEnabled);
            } else {
                CallWaitingSwitchPreference callWaitingSwitchPreference4 = CallWaitingSwitchPreference.this;
                callWaitingSwitchPreference4.mCwEnabled = callWaitingSwitchPreference4.mQueryStatus == 1;
                CallWaitingSwitchPreference callWaitingSwitchPreference5 = CallWaitingSwitchPreference.this;
                callWaitingSwitchPreference5.setChecked(callWaitingSwitchPreference5.mCwEnabled);
            }
            CallWaitingSwitchPreference.this.mIsDuringUpdateProcess = false;
            CallWaitingSwitchPreference.this.mCwClicked = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCwFallbackOnError() {
        if (this.mCwClicked) {
            this.mCwEnabled = !this.mCwEnabled;
        }
    }
}
