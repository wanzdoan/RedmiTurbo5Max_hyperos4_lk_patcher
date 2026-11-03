package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.util.Log;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.NrSceneConstants$Scene;
import miui.telephony.SubscriptionManager;
import miui.telephony.SubscriptionManagerEx;
import miui.telephony.TelephonyStatAdapter;
import xiaomi.hypercomm.HyperCommManager;

/* JADX INFO: loaded from: classes.dex */
public class WifiCallHelper {
    private Context mContext;
    private volatile boolean mHasInited;
    private volatile boolean mHasSaDisabled;
    private volatile boolean mIsConnectedOnSaMode;
    private volatile boolean mIsPhoneStateIdle;
    private volatile boolean mIsVoWifiRegisted;
    private int mPhoneId;
    private TelephonyCallBack mTelephonyCallBack = null;
    private BroadcastReceiver mReceiver = null;
    private WorkHandler mHandler = new WorkHandler(BackgroundThread.get().getLooper());

    public WifiCallHelper(Context context, int i) {
        this.mContext = context;
        this.mPhoneId = i;
    }

    private void restore() {
        this.mIsVoWifiRegisted = false;
        this.mIsPhoneStateIdle = true;
        this.mIsConnectedOnSaMode = false;
        this.mHasSaDisabled = false;
    }

    public void init() {
        log("init()");
        if (this.mHasInited) {
            return;
        }
        this.mHasInited = true;
        restore();
        registerBroadcastReceiver();
        registerTelephonyCallBack();
        Phone phone = MiuiPhoneUtils.getPhone(this.mPhoneId);
        if (phone != null && MiuiPhoneUtils.isChinaOrMacauTelecomSim(phone)) {
            CallManager.getInstance(this.mContext).registerForPreciseCallStateChanged(this.mHandler, 3, (Object) null);
        }
        updateNrState(this.mPhoneId);
    }

    public void dispose() {
        log("dispose()");
        this.mHasInited = false;
        CallManager.getInstance(this.mContext).unregisterForPreciseCallStateChanged(this.mHandler);
        unRegisterBroadcastReceiver();
        unRegisterTelephonyCallBack();
    }

    class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Object obj;
            WifiCallHelper.this.log("handleMessage received event " + message.what);
            int i = message.what;
            if (i == 0) {
                WifiCallHelper.this.processWfcRegStateChanged(message.arg1, message.arg2 == 1);
                return;
            }
            if (i == 1) {
                WifiCallHelper.this.processServiceStateChanged(message.arg1, (ServiceState) message.obj);
                return;
            }
            if (i == 2) {
                WifiCallHelper.this.processPreciseCallStateChanged(message.arg1, message.arg2);
            } else if (i == 3 && (obj = ((AsyncResult) message.obj).result) != null) {
                WifiCallHelper.this.processPhoneStateChanged((Phone) obj);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processWfcRegStateChanged(int i, boolean z) {
        log("processWfcRegStateChanged, old state (wfc reg) : " + this.mIsVoWifiRegisted + " , new state : " + z + " , phoneId : " + i);
        if (this.mIsVoWifiRegisted != z) {
            this.mIsVoWifiRegisted = z;
            changeSaStatus(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServiceStateChanged(int i, ServiceState serviceState) {
        boolean zIsNrSaConfigType = isNrSaConfigType(i, serviceState);
        log("processServiceStateChanged,  old state (connected sa)  : " + this.mIsConnectedOnSaMode + " , new state : " + zIsNrSaConfigType + " , phoneId : " + i);
        if (this.mIsConnectedOnSaMode != zIsNrSaConfigType) {
            this.mIsConnectedOnSaMode = zIsNrSaConfigType;
            changeSaStatus(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPreciseCallStateChanged(int i, int i2) {
        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
        boolean z = (CallManager.getInstance(this.mContext).hasActiveFgCall(subscriptionIdForSlot) || CallManager.getInstance(this.mContext).hasActiveBgCall(subscriptionIdForSlot) || CallManager.getInstance(this.mContext).hasActiveRingingCall(subscriptionIdForSlot)) ? false : true;
        log("processPreciseCallStateChanged,  old state (phone idle)  : " + this.mIsPhoneStateIdle + " , new state : " + z + " , phoneId : " + i);
        if (this.mIsPhoneStateIdle != z) {
            this.mIsPhoneStateIdle = z;
            changeSaStatus(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPhoneStateChanged(Phone phone) {
        if (phone == null || phone.getPhoneId() != this.mPhoneId) {
            return;
        }
        log("processPhoneStateChanged");
        boolean z = phone.getState() == PhoneConstants.State.IDLE;
        if (this.mIsPhoneStateIdle != z) {
            this.mIsPhoneStateIdle = z;
            changeSaStatus(phone.getPhoneId());
        }
    }

    private void changeSaStatus(int i) {
        if (PhoneFactory.getPhone(i) == null) {
            return;
        }
        log("changeSaStatus phoneId=" + i + ", isVoWifiRegisted =" + this.mIsVoWifiRegisted + ", isConnectedOnSaMode=" + this.mIsConnectedOnSaMode + ", isPhoneStateIdle = " + this.mIsPhoneStateIdle + ", hasSaDisabled = " + this.mHasSaDisabled);
        if (this.mIsPhoneStateIdle && this.mHasSaDisabled) {
            log("changeSaStatus enable sa.");
            enableSa(true, i);
        }
        if (!this.mIsVoWifiRegisted || !this.mIsConnectedOnSaMode || this.mIsPhoneStateIdle || this.mHasSaDisabled) {
            return;
        }
        log("changeSaStatus disable sa.");
        enableSa(false, i);
    }

    private void updateNrState(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        ServiceState serviceState = phone != null ? phone.getServiceState() : null;
        log("updateNrState ss : " + serviceState + " , phoneId : " + i);
        this.mIsConnectedOnSaMode = isNrSaConfigType(i, serviceState);
    }

    private void registerBroadcastReceiver() {
        if (this.mReceiver == null) {
            log("register BroadcastReceiver");
            IntentFilter intentFilter = new IntentFilter("android.intent.action.ACTION_IMS_REGISTED");
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.WifiCallHelper.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if ("android.intent.action.ACTION_IMS_REGISTED".equals(intent.getAction())) {
                        int intExtra = intent.getIntExtra(SubscriptionManagerEx.PHONE_KEY, -1);
                        if (MiuiPhoneUtils.isValidatePhoneId(intExtra) && intExtra == WifiCallHelper.this.mPhoneId) {
                            boolean booleanExtra = intent.getBooleanExtra("wfc_state", false);
                            WifiCallHelper.this.log("ims registed state changed,  wfcReg = " + booleanExtra + " phoneId = " + intExtra);
                            WifiCallHelper.this.mHandler.sendMessage(WifiCallHelper.this.mHandler.obtainMessage(0, intExtra, booleanExtra ? 1 : 0));
                        }
                    }
                }
            };
            this.mReceiver = broadcastReceiver;
            this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
        }
    }

    private void unRegisterBroadcastReceiver() {
        if (this.mReceiver != null) {
            log("unregister BroadcastReceiver");
            this.mContext.unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            log("register TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private void unRegisterTelephonyCallBack() {
        if (this.mTelephonyCallBack != null) {
            log("unregister TelephonyCallBack");
            TelephonyStatAdapter.unregisterCallBack(this.mTelephonyCallBack);
            this.mTelephonyCallBack = null;
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            if (MiuiPhoneUtils.isValidatePhoneId(i) && i == WifiCallHelper.this.mPhoneId) {
                WifiCallHelper.this.log("onServiceStateChanged ss : " + serviceState);
                WifiCallHelper.this.mHandler.obtainMessage(1, i, 0, serviceState).sendToTarget();
            }
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            if (MiuiPhoneUtils.isValidatePhoneId(i) && i == WifiCallHelper.this.mPhoneId) {
                WifiCallHelper.this.log("onPreciseCallStateChanged call state : " + i2);
                WifiCallHelper.this.mHandler.removeMessages(2);
                WifiCallHelper.this.mHandler.sendMessageDelayed(WifiCallHelper.this.mHandler.obtainMessage(2, i, i2), 500L);
            }
        }
    }

    private boolean isNrSaConfigType(int i, ServiceState serviceState) {
        return MiuiPhoneUtils.isValidatePhoneId(i) && serviceState != null && getRilDataRadioTechnologyForWwan(serviceState) == 20;
    }

    private int getRilDataRadioTechnologyForWwan(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
        return ServiceState.networkTypeToRilRadioTechnology(networkRegistrationInfo != null ? networkRegistrationInfo.getAccessNetworkTechnology() : 0);
    }

    private void enableSa(boolean z, final int i) {
        if (FiveGPowerController.getInstance() == null) {
            loge("enableSa: FiveGPowerController not init!! ");
            return;
        }
        try {
            log("enableSa [" + i + "], enabled[" + z + "]");
            this.mHasSaDisabled = this.mHasSaDisabled ^ true;
            if (FiveGUtils.isXmRadioSupported()) {
                NrSceneConstants$Scene nrSceneConstants$Scene = NrSceneConstants$Scene.WIFI_CALL;
                int typeByScene = FiveGUtils.getTypeByScene(nrSceneConstants$Scene);
                final String strValueOf = String.valueOf(nrSceneConstants$Scene.getValue());
                HyperCommManager.getDefault().requestNrOperation(i, strValueOf, typeByScene, !z, new ResultReceiver(this.mHandler) { // from class: com.android.phone.WifiCallHelper.2
                    @Override // android.os.ResultReceiver
                    protected void onReceiveResult(int i2, Bundle bundle) {
                        WifiCallHelper.this.log("requestNrOperation result=" + i2 + "; featureId=" + strValueOf + "; slotId=" + i);
                    }
                });
                return;
            }
            FiveGPowerController.getInstance().enableSaAsync(z, i, NrSceneConstants$Scene.WIFI_CALL);
        } catch (Exception e) {
            loge("enableSa: Exception = " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("WifiCallHelper", "[" + this.mPhoneId + "] " + str);
    }

    private void loge(String str) {
        Log.e("WifiCallHelper", "[" + this.mPhoneId + "] " + str);
    }
}
