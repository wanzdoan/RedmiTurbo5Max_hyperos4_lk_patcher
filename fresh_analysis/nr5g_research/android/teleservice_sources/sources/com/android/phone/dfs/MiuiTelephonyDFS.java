package com.android.phone.dfs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;
import com.android.phone.FiveGServiceInfo;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.statistics.utils.Utils;
import com.miui.misight.MiEvent;
import com.miui.misight.MiSight;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import miui.os.Build;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class MiuiTelephonyDFS {
    private static MiuiTelephonyDFS sInstance;
    private Context mContext;
    private FiveGServiceInfo[] mFiveGInfo;
    private FiveGServiceInfoListener mFiveGServiceInfoListener;
    private MiuiTelephonyDFSHandler mHandler;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.dfs.MiuiTelephonyDFS.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            MiuiTelephonyDFS.this.log("receive action".concat(action));
        }
    };
    private ServiceState[] mServiceState;
    private SignalStrength[] mSignalStrength;
    private boolean mSupportFiveG;
    private TelephonyCallBack mTelephonyCallBack;
    private HandlerThread mWorkThread;

    private MiuiTelephonyDFS() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mServiceState = new ServiceState[i];
        this.mSignalStrength = new SignalStrength[i];
        this.mFiveGInfo = new FiveGServiceInfo[i];
        init();
    }

    public static synchronized MiuiTelephonyDFS getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new MiuiTelephonyDFS();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private void init() {
        this.mSupportFiveG = MiuiPhoneUtils.isFiveGCapable();
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("MiuiTelephonyDFS", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new MiuiTelephonyDFSHandler(this.mWorkThread.getLooper());
        registerBroadcastReceiver();
        registerTelephonyCallBack();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mServiceState[i] = new ServiceState();
            this.mSignalStrength[i] = new SignalStrength();
        }
        if (this.mSupportFiveG) {
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                this.mFiveGInfo[i2] = new FiveGServiceInfo();
            }
            register5GServiceInfoListener();
        }
    }

    private void registerBroadcastReceiver() {
        this.mContext.registerReceiver(this.mIntentReceiver, new IntentFilter(), 2);
    }

    class MiuiTelephonyDFSHandler extends Handler {
        MiuiTelephonyDFSHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            MiuiTelephonyDFS.this.log("msg.what = " + message.what);
            int i = message.what;
            if (i == 1) {
                MiuiTelephonyDFS.this.processServiceStateChange(message.arg1, (ServiceState) message.obj);
            } else if (i == 2) {
                MiuiTelephonyDFS.this.processSignalStrengthChange(message.arg1, (SignalStrength) message.obj);
            } else {
                if (i != 3) {
                    return;
                }
                MiuiTelephonyDFS.this.updateFiveGInfo(message.arg1, (FiveGServiceInfo) message.obj, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateFiveGInfo(int i, FiveGServiceInfo fiveGServiceInfo, boolean z) {
        if (this.mSupportFiveG && MiuiPhoneUtils.isValidatePhoneId(i)) {
            if (fiveGServiceInfo != null) {
                this.mFiveGInfo[i].copyFrom(fiveGServiceInfo);
            }
        }
    }

    private static void sendEvent(MiEvent miEvent) {
        if (TelephonyManagerEx.getDefault().isMievnetSupported()) {
            MiSight.sendEvent(miEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServiceStateChange(int i, ServiceState serviceState) {
        ServiceState serviceState2;
        if (serviceState == null || !MiuiPhoneUtils.isValidatePhoneId(i) || (serviceState2 = this.mServiceState[i]) == serviceState) {
            return;
        }
        int dataNetworkType = serviceState2.getDataNetworkType();
        int dataNetworkType2 = serviceState.getDataNetworkType();
        if (dataNetworkType != dataNetworkType2) {
            sendEvent(MiSight.constructEvent(915100002, new Object[]{"fromRAT", Integer.valueOf(dataNetworkType), "toRAT", Integer.valueOf(dataNetworkType2)}));
        }
        this.mServiceState[i] = serviceState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSignalStrengthChange(int i, SignalStrength signalStrength) {
        if (signalStrength == null || !MiuiPhoneUtils.isValidatePhoneId(i)) {
            return;
        }
        SignalStrength[] signalStrengthArr = this.mSignalStrength;
        if (signalStrengthArr[i] == signalStrength) {
            return;
        }
        signalStrengthArr[i] = signalStrength;
        poorSignalJudge(i);
    }

    private void poorSignalJudge(int i) {
        int rsrp = getRsrp(i);
        int snr = getSnr(i);
        if (Build.IS_INTERNATIONAL_BUILD) {
            if (-140 < rsrp && rsrp <= -105) {
                sendEvent(MiSight.constructEvent(915100004, new Object[]{"rsrpValue", Integer.valueOf(rsrp)}));
            }
        } else if (-140 < rsrp && rsrp <= -110) {
            sendEvent(MiSight.constructEvent(915100004, new Object[]{"rsrpValue", Integer.valueOf(rsrp)}));
        }
        if (-23 >= snr || snr > -2) {
            return;
        }
        sendEvent(MiSight.constructEvent(915100005, new Object[]{"SNRValue", Integer.valueOf(snr)}));
    }

    public int getRat(int i) {
        return getVoiceRat(i);
    }

    public int getVoiceRat(int i) {
        ServiceState serviceState = this.mServiceState[i];
        if (serviceState == null) {
            return -1;
        }
        int voiceNetworkType = serviceState.getVoiceNetworkType();
        log("getVoiceRat = " + voiceNetworkType);
        return voiceNetworkType;
    }

    public int getRsrp(int i) {
        int rsrp;
        FiveGServiceInfo fiveGServiceInfo;
        SignalStrength signalStrength;
        int rat = getRat(i);
        if (Utils.isLte(rat) && (signalStrength = this.mSignalStrength[i]) != null) {
            rsrp = signalStrength.getLteRsrp();
        } else {
            rsrp = (!Utils.isNr(rat) || (fiveGServiceInfo = this.mFiveGInfo[i]) == null) ? -1 : fiveGServiceInfo.getRsrp();
        }
        log("getRsrp = " + rsrp);
        return rsrp;
    }

    public int getSnr(int i) {
        int snr;
        FiveGServiceInfo fiveGServiceInfo;
        SignalStrength signalStrength;
        int rat = getRat(i);
        if (Utils.isLte(rat) && (signalStrength = this.mSignalStrength[i]) != null) {
            snr = signalStrength.getLteRssnr();
        } else {
            snr = (!Utils.isNr(rat) || (fiveGServiceInfo = this.mFiveGInfo[i]) == null) ? -24 : fiveGServiceInfo.getSnr();
        }
        log("getSnr = " + snr);
        return snr;
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private void register5GServiceInfoListener() {
        this.mFiveGServiceInfoListener = new FiveGServiceInfoListener();
        EventHubManager.getInstance().registerListener("telephony", "fiveg_service_info_changed", this.mFiveGServiceInfoListener);
    }

    class FiveGServiceInfoListener implements BaseEvent.IEventListener {
        FiveGServiceInfoListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            TelephonyEvent.FiveGEvent fiveGEvent;
            Bundle bundle;
            if (!(baseEvent instanceof TelephonyEvent.FiveGEvent) || (bundle = (fiveGEvent = (TelephonyEvent.FiveGEvent) baseEvent).getBundle()) == null) {
                return;
            }
            int i = bundle.getInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID);
            FiveGServiceInfo fiveGServiceInfo = fiveGEvent.getFiveGServiceInfo();
            if (fiveGServiceInfo == null || !MiuiPhoneUtils.isValidatePhoneId(i)) {
                MiuiTelephonyDFS.this.log("FiveGServiceListener info is null or invalid phone id");
            } else {
                MiuiTelephonyDFS.this.mHandler.obtainMessage(3, i, 0, fiveGServiceInfo).sendToTarget();
            }
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            MiuiTelephonyDFS.this.mHandler.obtainMessage(2, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            MiuiTelephonyDFS.this.mHandler.obtainMessage(1, i, 0, serviceState).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("MiuiTelephonyDFS", str);
    }
}
