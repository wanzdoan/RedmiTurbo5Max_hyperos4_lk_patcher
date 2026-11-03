package miui.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSmsSaFallBackOptimization extends MiuiSmsOptimization {
    private static final String ACTION_SA_ENABLE_OR_DISABLE = "miui.intent.action.ACTION_SA_ENABLE_OR_DISABLE";
    private static final String EXTRA_PHONE_ID = "phone_id";
    private static final String EXTRA_SA_STATE = "sa_state";
    private static final int SA_FALLBACK_OPTIMIZATION_TIME = 180000;
    private static final String TAG = "MiuiSmsSaFallBackOptimization";
    private static boolean isSupportNAS = false;
    private static BroadcastReceiver mBroadcastReceiver;
    private static IntentFilter mFilter;

    public MiuiSmsSaFallBackOptimization(Phone phone, MiuiSmsOptimizationController miuiSmsOptimizationController) {
        super(phone, miuiSmsOptimizationController);
        mFilter = new IntentFilter();
    }

    @Override // miui.telephony.MiuiSmsOptimization
    public void init(Phone phone, String nowSmscAddress, boolean isIms) {
    }

    @Override // miui.telephony.MiuiSmsOptimization
    public boolean isNeedAndstartOptimization(Phone phone, String nowSmscAddress, boolean isIms, SMSDispatcher.SmsTracker smsTracker) {
        if (isSupportNAS) {
            Log.d(TAG, "miuiSaFallBackOptimization SuppotNAS");
            return false;
        }
        if (!isSupportSmsSaFallBackOptimization(phone)) {
            return false;
        }
        if (this.mMiuiSmsOptimizationController.mMiuiSmsSmscOptimization.mStatus == 0 && isIms) {
            Log.d(TAG, "miuiSaFallBackOptimization smscstatus = init");
            return false;
        }
        if (phone.getServiceState().getRilDataRadioTechnology() == 20) {
            if (this.mStatus == 0) {
                this.mStatus = 1;
                silentDisableSA(phone, 0);
                mFilter.addAction("android.intent.action.SERVICE_STATE");
                registerBroadcastReceiver();
            }
            sendMessageDelayed(obtainMessage(1, smsTracker), 180000L);
        }
        return this.mStatus != 0;
    }

    protected void registerBroadcastReceiver() {
        if (mBroadcastReceiver != null) {
            return;
        }
        mBroadcastReceiver = new BroadcastReceiver() { // from class: miui.telephony.MiuiSmsSaFallBackOptimization.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) {
                    Log.e(MiuiSmsSaFallBackOptimization.TAG, "received null intent or action");
                    return;
                }
                String action = intent.getAction();
                if ("android.intent.action.SERVICE_STATE".equals(action) && MiuiSmsSaFallBackOptimization.this.mPhone.getServiceState().getRilDataRadioTechnology() != 20 && MiuiSmsSaFallBackOptimization.this.mPhone.getServiceState().getState() == 0 && MiuiSmsSaFallBackOptimization.this.mMiuiSmsOptimizationController.mMiuiSmsSaFallBackOptimization.mStatus == 1) {
                    Log.d(MiuiSmsSaFallBackOptimization.TAG, "sendSMSFallbackNR: NR fallback successfully");
                    MiuiSmsSaFallBackOptimization.this.mMiuiSmsOptimizationController.mMiuiSmsSaFallBackOptimization.mStatus = 2;
                    MiuiSmsSaFallBackOptimization.this.mMiuiSmsOptimizationController.misSmsOptimization = true;
                    MiuiSmsSaFallBackOptimization.this.mMiuiSmsOptimizationController.onOptimizationReady("saFallBack");
                    MiuiSmsSaFallBackOptimization.this.unRegisterBroadcastReceiver(MiuiSmsSaFallBackOptimization.this.mContext);
                }
            }
        };
        this.mContext.registerReceiver(mBroadcastReceiver, mFilter, 2);
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                int originalStatus = this.mMiuiSmsOptimizationController.mMiuiSmsSaFallBackOptimization.mStatus;
                onFinish(this.mMiuiSmsOptimizationController.mPhone, "", false);
                if (originalStatus != 0) {
                    this.mMiuiSmsOptimizationController.optimizationTimeOut((SMSDispatcher.SmsTracker) msg.obj);
                    Log.d(TAG, "EVENT_SMS_OPTIMIZATION_TIMEOUT + originalStatus=" + originalStatus);
                }
                break;
        }
    }

    @Override // miui.telephony.MiuiSmsOptimization
    public void onFinish(Phone phone, String nowSmscAddress, boolean isSuccessed) {
        if (this.mStatus != 0) {
            silentDisableSA(phone, 1);
            this.mMiuiSmsOptimizationController.misSmsOptimization = false;
            this.mStatus = 0;
        }
    }

    protected void silentDisableSA(Phone phone, int saState) {
        Log.d(TAG, "silentDisableSA: phoneid =" + phone.getPhoneId() + ", saState =" + saState);
        Intent intent = new Intent();
        intent.setAction(ACTION_SA_ENABLE_OR_DISABLE);
        intent.putExtra(EXTRA_SA_STATE, saState);
        intent.putExtra("phone_id", phone.getPhoneId());
        this.mContext.sendBroadcast(intent);
    }

    public void unRegisterBroadcastReceiver(Context context) {
        if (mBroadcastReceiver != null) {
            Log.d(TAG, "unRegisterBroadcastReceiver...");
            context.unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }

    public static boolean isSupportSmsSaFallBackOptimization(Phone phone) {
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_miui_sms_sa_fallback_optimization");
    }
}
