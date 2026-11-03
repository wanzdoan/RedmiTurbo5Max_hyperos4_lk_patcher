package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.CellIdentity;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.HashMap;
import java.util.LinkedList;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class ServiceStateMonitor {
    private static String LOG_TAG = "ServiceStateMonitor";
    private static ServiceStateMonitor sInstance;
    private CellIdentity[] mCellIdentity;
    private Context mContext;
    private final BaseEvent.IEventListener mDisplayListener;
    private FiveGServiceInfo[] mFiveGInfo;
    private FiveGInfoListener mFiveGInfoListener;
    private ModemPowerHandler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    private HashMap mLastReportMap;
    private HashMap mPingPongEvent;
    private ServiceState[] mServiceState;
    private TeleCallBack mTeleCallBack;
    private HandlerThread mWorkThread;
    private boolean mIsScreenOn = true;
    private boolean mSupportFiveG = false;
    private boolean mOldRatIsNsaMode = false;
    private long mLastScreenOffTime = 0;
    private boolean mIsSleepMode = false;
    private int mDdsPhoneId = -1;
    private boolean[] mNr2LteOpt = {false, false};

    private boolean isLteNwType(int i) {
        return i == 13 || i == 19;
    }

    private boolean isNrNwType(int i) {
        return i == 20 || i == 30;
    }

    public static synchronized ServiceStateMonitor getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new ServiceStateMonitor();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private ServiceStateMonitor() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mFiveGInfo = new FiveGServiceInfo[i];
        this.mServiceState = new ServiceState[i];
        this.mCellIdentity = new CellIdentity[i];
        this.mTeleCallBack = null;
        this.mPingPongEvent = new HashMap();
        this.mLastReportMap = new HashMap();
        this.mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.ServiceStateMonitor.1
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                if ((baseEvent instanceof DeviceStateEvent.DisplayEvent) && ((DeviceStateEvent.DisplayEvent) baseEvent).isDefaultDisplay()) {
                    ServiceStateMonitor.this.mHandler.obtainMessage(2).sendToTarget();
                }
            }
        };
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.ServiceStateMonitor.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                ServiceStateMonitor.log("receive action".concat(action));
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    ServiceStateMonitor.this.mHandler.obtainMessage(3).sendToTarget();
                } else if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    ServiceStateMonitor.this.mHandler.obtainMessage(4, intent).sendToTarget();
                } else if ("com.miui.powerkeeper_sleep_changed".equals(intent.getAction())) {
                    ServiceStateMonitor.this.mHandler.obtainMessage(10, intent).sendToTarget();
                }
            }
        };
        log("ServiceStateMonitor init");
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread(LOG_TAG, 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        ModemPowerHandler modemPowerHandler = new ModemPowerHandler(this.mWorkThread.getLooper());
        this.mHandler = modemPowerHandler;
        modemPowerHandler.sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        initValues();
        registerAllInfo();
    }

    private synchronized void initValues() {
        try {
            this.mDdsPhoneId = getDefaultDataSlotId();
            this.mSupportFiveG = MiuiPhoneUtils.isFiveGCapable();
            this.mPingPongEvent.put("nr_lte", new HashMap());
            this.mPingPongEvent.put("nsa_lte", new HashMap());
            this.mLastReportMap.put("nr_lte", new HashMap());
            this.mLastReportMap.put("nsa_lte", new HashMap());
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                Phone phone = MiuiPhoneUtils.getPhone(i);
                this.mServiceState[i] = new ServiceState();
                this.mCellIdentity[i] = phone.getCurrentCellIdentity();
            }
            if (this.mSupportFiveG) {
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    this.mFiveGInfo[i2] = new FiveGServiceInfo();
                }
                register5GServiceInfoListener();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void registerAllInfo() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
        registerReceiver();
        registerTelephonyInfoCallBack();
    }

    private void register5GServiceInfoListener() {
        this.mFiveGInfoListener = new FiveGInfoListener();
        EventHubManager.getInstance().registerListener("telephony", "fiveg_service_info_changed", this.mFiveGInfoListener);
    }

    class FiveGInfoListener implements BaseEvent.IEventListener {
        FiveGInfoListener() {
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
                ServiceStateMonitor.log("FiveGInfoListener info is null or invalid phone id");
                return;
            }
            ServiceStateMonitor.log("info[" + i + "]= " + fiveGServiceInfo);
            ServiceStateMonitor.this.mHandler.obtainMessage(9, i, 0, fiveGServiceInfo).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateFiveGInfo(int i, FiveGServiceInfo fiveGServiceInfo) {
        try {
            if (this.mSupportFiveG && MiuiPhoneUtils.isValidatePhoneId(i) && i == this.mDdsPhoneId) {
                if (fiveGServiceInfo != null) {
                    this.mFiveGInfo[i].copyFrom(fiveGServiceInfo);
                }
                monitorFiveGInfoChange(i);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("com.miui.powerkeeper_sleep_changed");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    private void processPingpongOptimization(boolean z, int i) {
        if (isPingpongOptSupported()) {
            Bundle bundle = new Bundle();
            bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
            bundle.putBoolean("ping_pong_opt", z);
            EventHubManager.getInstance().postEvent(new TelephonyEvent.NetworkEvent("nr_2_lte_ping_pong_info_changed", bundle));
            return;
        }
        this.mNr2LteOpt[i] = true;
    }

    class ModemPowerHandler extends Handler {
        ModemPowerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ServiceStateMonitor.log("handleMessage=" + message.what);
            switch (message.what) {
                case 1:
                    ServiceStateMonitor.this.initAction();
                    break;
                case 2:
                    ServiceStateMonitor serviceStateMonitor = ServiceStateMonitor.this;
                    serviceStateMonitor.mIsScreenOn = MiuiPhoneUtils.isScreenOn(serviceStateMonitor.mContext);
                    if (ServiceStateMonitor.this.mIsScreenOn) {
                        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                            ServiceStateMonitor.this.recoverySaFromOpt(i);
                        }
                    } else {
                        ServiceStateMonitor.this.mLastScreenOffTime = System.currentTimeMillis();
                    }
                    break;
                case 3:
                    if (ServiceStateMonitor.this.getDefaultDataSlotId() != ServiceStateMonitor.this.mDdsPhoneId) {
                        ServiceStateMonitor serviceStateMonitor2 = ServiceStateMonitor.this;
                        serviceStateMonitor2.recoverySaFromOpt(serviceStateMonitor2.mDdsPhoneId);
                    }
                    break;
                case 4:
                    Intent intent = (Intent) message.obj;
                    String stringExtra = intent.getStringExtra("ss");
                    int intExtra = intent.getIntExtra("phone", -1);
                    if (SubscriptionManager.isValidPhoneId(intExtra) && "ABSENT".equals(stringExtra) && intExtra == ServiceStateMonitor.this.mDdsPhoneId) {
                        ServiceStateMonitor.this.recoverySaFromOpt(intExtra);
                    }
                    break;
                case 5:
                    ServiceStateMonitor.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                    break;
                case 7:
                    ServiceStateMonitor.this.processCellLocationChangeEvent(message.arg1, (CellLocation) message.obj);
                    break;
                case 9:
                    ServiceStateMonitor.this.updateFiveGInfo(message.arg1, (FiveGServiceInfo) message.obj);
                    break;
                case 10:
                    ServiceStateMonitor.this.mIsSleepMode = 1 == ((Intent) message.obj).getIntExtra(MEModuleSignalT1s.EXTRA_STATE, 2);
                    break;
            }
        }
    }

    private void registerTelephonyInfoCallBack() {
        log("registerTelephonyInfoCallBack");
        if (this.mTeleCallBack == null) {
            log("new TeleCallBack");
            TeleCallBack teleCallBack = new TeleCallBack();
            this.mTeleCallBack = teleCallBack;
            TelephonyStatAdapter.registerCallBack(teleCallBack);
        }
    }

    class TeleCallBack implements TelephonyStatAdapter.ICallBack {
        TeleCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            ServiceStateMonitor.this.mHandler.obtainMessage(8, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            ServiceStateMonitor.this.mHandler.obtainMessage(5, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            if (ServiceStateMonitor.this.mNr2LteOpt[i]) {
                ServiceStateMonitor.this.mHandler.obtainMessage(7, i, 0, cellLocation).sendToTarget();
            }
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            ServiceStateMonitor.this.recoverySaFromOpt(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processCellLocationChangeEvent(int i, CellLocation cellLocation) {
        try {
            if (MiuiPhoneUtils.isValidatePhoneId(i) && cellLocation != null && !cellLocation.isEmpty()) {
                log("processCellLocationChangeEvent phoneId=" + i);
                Phone phone = MiuiPhoneUtils.getPhone(i);
                String globalCellId = null;
                CellIdentity currentCellIdentity = phone == null ? null : phone.getCurrentCellIdentity();
                if (currentCellIdentity != null) {
                    globalCellId = currentCellIdentity.getGlobalCellId();
                }
                if (phone != null && currentCellIdentity != null && globalCellId != null) {
                    String globalCellId2 = this.mCellIdentity[i].getGlobalCellId();
                    if (!TextUtils.isEmpty(globalCellId) && !TextUtils.isEmpty(globalCellId2) && !TextUtils.equals(globalCellId, globalCellId2)) {
                        log("processCellLocationChangeEvent newCGI=" + globalCellId + ",preCGI=" + globalCellId2 + ",dds phoneId=" + i);
                        recoverySaFromOpt(i);
                        resetLteNrPingPongInfo(i, (HashMap) this.mPingPongEvent.get("nr_lte"));
                    }
                    this.mCellIdentity[i] = currentCellIdentity;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        try {
            log("processServiceStateChangeEvent phoneId=" + i);
            if (serviceState != null && MiuiPhoneUtils.isValidatePhoneId(i)) {
                if (!serviceState.getIsManualSelection()) {
                    monitorDataNwChange(i, serviceState);
                }
                this.mServiceState[i] = new ServiceState(serviceState);
            } else {
                log("processServiceStateChangeEvent invalid params");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void monitorDataNwChange(int i, ServiceState serviceState) {
        if (this.mServiceState[i] == null || serviceState == null) {
            log("monitorDataNwChange invalid params");
            return;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        int dataNetworkType2 = this.mServiceState[i].getDataNetworkType();
        log("newRat:" + dataNetworkType + ",oldRat:" + dataNetworkType2);
        if (dataNetworkType != dataNetworkType2 && isLteNrNwType(dataNetworkType) && isLteNrNwType(dataNetworkType2)) {
            updatePingPongCacheInfo(i, (HashMap) this.mPingPongEvent.get("nr_lte"));
            if (isPingPongExceptionDetected(i, (HashMap) this.mPingPongEvent.get("nr_lte"))) {
                avoidPingPongIfNeeded(i, "nr_lte", (HashMap) this.mPingPongEvent.get("nr_lte"));
                reportpingpongStatistics(this.mIsScreenOn, i, "nr_lte");
            }
        }
    }

    private void monitorFiveGInfoChange(int i) {
        FiveGServiceInfo fiveGServiceInfo = this.mFiveGInfo[i];
        if (fiveGServiceInfo == null) {
            log("monitorFiveGInfoChange invalid params");
            return;
        }
        boolean zIsConnectedOnNsaMode = fiveGServiceInfo.isConnectedOnNsaMode();
        if (zIsConnectedOnNsaMode != this.mOldRatIsNsaMode && isLteNrNwType(this.mServiceState[i].getDataNetworkType())) {
            updatePingPongCacheInfo(i, (HashMap) this.mPingPongEvent.get("nsa_lte"));
            if (isPingPongExceptionDetected(i, (HashMap) this.mPingPongEvent.get("nsa_lte"))) {
                reportpingpongStatistics(this.mIsScreenOn, i, "nsa_lte");
            }
        }
        this.mOldRatIsNsaMode = zIsConnectedOnNsaMode;
    }

    private void updatePingPongCacheInfo(int i, HashMap map) {
        LinkedList linkedList;
        if (map.get(Integer.valueOf(i)) != null) {
            linkedList = (LinkedList) map.get(Integer.valueOf(i));
        } else {
            linkedList = new LinkedList();
        }
        linkedList.add(Long.valueOf(System.currentTimeMillis()));
        long jLongValue = ((Long) linkedList.getLast()).longValue();
        while (linkedList.size() > 0 && jLongValue - ((Long) linkedList.get(0)).longValue() > 300000) {
            linkedList.remove(0);
        }
        map.put(Integer.valueOf(i), linkedList);
    }

    private void avoidPingPongIfNeeded(int i, String str, HashMap map) {
        if (!isPingpongOptSupported()) {
            log("Ping pong optimization not supported.");
            return;
        }
        if (!"nr_lte".equals(str) || this.mIsScreenOn || System.currentTimeMillis() - this.mLastScreenOffTime < 30000 || this.mNr2LteOpt[i]) {
            return;
        }
        resetLteNrPingPongInfo(i, map);
        this.mNr2LteOpt[i] = true;
        processPingpongOptimization(true, i);
    }

    private boolean isPingPongExceptionDetected(int i, HashMap map) {
        if (map.get(Integer.valueOf(i)) != null && ((LinkedList) map.get(Integer.valueOf(i))).size() >= 5) {
            LinkedList linkedList = (LinkedList) map.get(Integer.valueOf(i));
            log("Ping pong exception detected...phoneId=" + i);
            return linkedList != null;
        }
        if (map.get(Integer.valueOf(i)) != null) {
            log("No pingpong exception detected... size=" + ((LinkedList) map.get(Integer.valueOf(i))).size() + " phoneId=" + i);
        }
        return false;
    }

    public static boolean isPingpongOptSupported() {
        return !TelephonyCloudController.CLOUD_IRAT_PINGPONG_OPT_DISABLED && MiuiPhoneUtils.isSupportIratPingpongOptimization();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recoverySaFromOpt(int i) {
        if (this.mNr2LteOpt[i]) {
            processPingpongOptimization(false, i);
            this.mNr2LteOpt[i] = false;
        }
    }

    private void resetLteNrPingPongInfo(int i, HashMap map) {
        map.remove(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    private boolean isLteNrNwType(int i) {
        return isLteNwType(i) || isNrNwType(i);
    }

    protected static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    private void reportpingpongStatistics(boolean z, int i, String str) {
        if (System.currentTimeMillis() - (((HashMap) this.mLastReportMap.get(str)).get(Integer.valueOf(i)) != null ? ((Long) ((HashMap) this.mLastReportMap.get(str)).get(Integer.valueOf(i))).longValue() : 0L) > 1200000) {
            log("start reporting pingpong event.. type: " + str);
            HashMap map = new HashMap();
            map.put("ScreenStatus", Boolean.valueOf(z));
            map.put("isDds", i == DefaultSimManager.getDefaultDataSlotId() ? "1" : "0");
            map.put("pingPongType", str);
            map.put("sleepMode", Boolean.valueOf(this.mIsSleepMode));
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_pingpong_mode_statistic", map);
            ((HashMap) this.mLastReportMap.get(str)).put(Integer.valueOf(i), Long.valueOf(System.currentTimeMillis()));
            return;
        }
        log("Ping pong event report interval should more than 20 mins");
    }
}
