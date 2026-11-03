package com.android.phone.statistics.rf;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.ServiceState;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.FiveGServiceInfo;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.TelephonySensorManager;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.INetworkModule;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.HashMap;
import miui.process.ForegroundInfo;
import miui.process.ProcessManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class SixRxStatistic {
    protected static volatile SixRxStatistic mInstance;
    private boolean[] m6RxState;
    private Context mContext;
    private FiveGServiceListener mFiveGServiceListener;
    private SixRxStatisticHandler mHandler;
    private boolean mIsVonrCallType;
    private ServiceState[] mServiceState;
    private TelephonyCallBack mTelephonyCallBack;
    private final long INVAILD_TIME = 0;
    private long mSixRXOnTime = 0;
    private long mSixRXOffTime = 0;

    private SixRxStatistic() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.m6RxState = new boolean[i];
        this.mServiceState = new ServiceState[i];
        this.mIsVonrCallType = false;
        this.mContext = PhoneGlobals.getInstance();
        this.mFiveGServiceListener = new FiveGServiceListener();
        this.mTelephonyCallBack = new TelephonyCallBack();
        SixRxStatisticHandler sixRxStatisticHandler = new SixRxStatisticHandler(MiuiThreadUtil.getWorkThreadLooper());
        this.mHandler = sixRxStatisticHandler;
        sixRxStatisticHandler.obtainMessage(1).sendToTarget();
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            this.m6RxState[i2] = false;
            this.mServiceState[i2] = new ServiceState();
        }
    }

    public static SixRxStatistic make() {
        if (mInstance == null) {
            mInstance = new SixRxStatistic();
        }
        return mInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        TelephonyStatAdapter.registerCallBack(this.mTelephonyCallBack);
        registerFiveGListener();
    }

    public class SixRxStatisticHandler extends Handler {
        public SixRxStatisticHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                SixRxStatistic.this.init();
            } else if (i == 2) {
                SixRxStatistic.this.processServiceStateChange(message.arg1, (ServiceState) message.obj);
            } else {
                if (i != 3) {
                    return;
                }
                SixRxStatistic.this.report6RxStatistic(((Integer) message.obj).intValue());
            }
        }
    }

    class FiveGServiceListener implements BaseEvent.IEventListener {
        private boolean is6Rx(int i) {
            return i == 6;
        }

        private FiveGServiceListener() {
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
                SixRxStatistic.log("FiveGServiceListener info is null or invalid phone id");
                return;
            }
            if (SixRxStatistic.this.isValidatePhoneId(i)) {
                boolean zIs6Rx = is6Rx(fiveGServiceInfo.getRxCount());
                SixRxStatistic.log("onNrIconChange slotId: " + i + ", is6Rx: " + zIs6Rx);
                if (zIs6Rx != SixRxStatistic.this.m6RxState[i]) {
                    SixRxStatistic.this.on6RxStateChanged(i, zIs6Rx);
                    SixRxStatistic.this.m6RxState[i] = zIs6Rx;
                }
            }
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        private TelephonyCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            SixRxStatistic.this.mHandler.obtainMessage(2, i, 0, serviceState).sendToTarget();
        }
    }

    private void registerFiveGListener() {
        EventHubManager.getInstance().registerListener("telephony", "fiveg_service_info_changed", this.mFiveGServiceListener, this.mHandler);
        log("registerFiveGListener successfully");
    }

    public void on6RxStateChanged(int i, boolean z) {
        this.mHandler.obtainMessage(3, Integer.valueOf(i)).sendToTarget();
    }

    public synchronized void report6RxStatistic(int i) {
        try {
            if (isValidatePhoneId(i) && MiuiTelephonyStatistic.getInstance() != null) {
                if (!isStatisticsSixRX(this.m6RxState[i] ? this.mSixRXOnTime : this.mSixRXOffTime)) {
                    log("Statistical frequency limit, phoneId = " + i + ", m6RxState = " + this.m6RxState[i]);
                    return;
                }
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                if (this.m6RxState[i]) {
                    this.mSixRXOnTime = jElapsedRealtime;
                } else {
                    this.mSixRXOffTime = jElapsedRealtime;
                }
                Phone phone = MiuiPhoneUtils.getPhone(i);
                HashMap commonRadioParamInfo = MiuiTelephonyStatistic.getInstance().getCommonRadioParamInfo(i, false);
                commonRadioParamInfo.put("SixRxState", String.valueOf(this.m6RxState[i]));
                commonRadioParamInfo.put("appType", String.valueOf(((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getCurApptype()));
                ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
                if (foregroundInfo != null) {
                    commonRadioParamInfo.put("foregroundPkg", foregroundInfo.mForegroundPackageName);
                }
                if (phone != null) {
                    if (phone.getImsPhone() != null) {
                        phone = phone.getImsPhone();
                    }
                    int phoneType = phone.getPhoneType();
                    if (MiuiPhoneUtils.isPhoneInCall() && phoneType == 5 && Utils.isNr(Utils.getDataRat(this.mServiceState[i]))) {
                        this.mIsVonrCallType = true;
                    } else {
                        this.mIsVonrCallType = false;
                    }
                    commonRadioParamInfo.put("isVonrCall", String.valueOf(this.mIsVonrCallType));
                }
                if (TelephonySensorManager.getInstance() != null) {
                    commonRadioParamInfo.put("SensorGripPostureState", String.valueOf(TelephonySensorManager.getInstance().getSensorGripPostureState()));
                }
                commonRadioParamInfo.put("isCharging", String.valueOf(isDeviceCharging()));
                commonRadioParamInfo.put("BatteryLevel", String.valueOf(((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getBatteryLevel()));
                commonRadioParamInfo.put("isThermalLimit", String.valueOf(Utils.isThermalLimit()));
                long currentDlRateBps = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getCurrentDlRateBps() / 1024;
                long currentUlRateBps = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getCurrentUlRateBps() / 1024;
                commonRadioParamInfo.put("currentULRate", String.valueOf(currentDlRateBps));
                commonRadioParamInfo.put("currentDLRate", String.valueOf(currentUlRateBps));
                log("report6RxStatistic param = " + commonRadioParamInfo);
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "six_rx_state", commonRadioParamInfo);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processServiceStateChange(int i, ServiceState serviceState) {
        if (serviceState != null) {
            if (isValidatePhoneId(i)) {
                this.mServiceState[i] = new ServiceState(serviceState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidatePhoneId(int i) {
        return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
    }

    private boolean isStatisticsSixRX(long j) {
        return j == 0 || SystemClock.elapsedRealtime() - j > 7200000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("SixRxStatistic", str);
    }

    private boolean isDeviceCharging() {
        BatteryManager batteryManager = (BatteryManager) this.mContext.getSystemService("batterymanager");
        return batteryManager != null && batteryManager.isCharging();
    }
}
