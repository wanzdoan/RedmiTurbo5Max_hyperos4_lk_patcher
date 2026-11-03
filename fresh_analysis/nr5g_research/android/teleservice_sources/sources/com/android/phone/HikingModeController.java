package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SettingsObserver;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class HikingModeController {
    private static Context mContext;
    private static HikingModeController sInstance;
    private int mDelayUpateOosTimer;
    private HikingModeHandler mHandler;
    public HikingModeStats mHikingModeStats;
    private final BroadcastReceiver mIntentReceiver;
    private boolean[] mIsOosTimerWorking;
    private boolean[] mIsSimBplmnSet;
    private boolean mIsSnsOptimizationSupported;
    private boolean[] mIsSnsSetOosScanState;
    private long mLastHikingModeEnterTime;
    private NetworkModeRecognition mNetworkModeRecognition;
    private RilHook mRilHook;
    private SettingsObserver mSettingsObserver;
    private HandlerThread mWorkThread;
    private boolean VDBG = false;
    private TelephonyInfoCallBack mTelephonyInfoCallBack = null;
    private boolean mIsHikingIn = false;
    private int mHikingState = -1;

    public static synchronized HikingModeController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new HikingModeController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private HikingModeController() {
        this.mWorkThread = null;
        this.mHandler = null;
        this.mNetworkModeRecognition = null;
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mIsOosTimerWorking = new boolean[i];
        this.mIsSimBplmnSet = new boolean[i];
        this.mLastHikingModeEnterTime = -1L;
        this.mIsSnsOptimizationSupported = false;
        this.mIsSnsSetOosScanState = new boolean[i];
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.HikingModeController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("hikingModeController.intent.debugall")) {
                    HikingModeController.this.mHandler.obtainMessage(100, Integer.valueOf(intent.getIntExtra("delay_timer", HikingModeController.this.mIsSnsOptimizationSupported ? 30000 : 600000))).sendToTarget();
                }
            }
        };
        log("HikingModeController init");
        mContext = PhoneGlobals.getInstance();
        this.mHikingModeStats = new HikingModeStats();
        HandlerThread handlerThread = new HandlerThread("HikingModeController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new HikingModeHandler(this.mWorkThread.getLooper());
        this.mRilHook = RilHook.init(mContext);
        this.mSettingsObserver = new SettingsObserver(mContext, this.mHandler);
        this.mNetworkModeRecognition = NetworkModeRecognition.getInstance();
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        boolean zIsSnsOptimizationSupported = MiuiPhoneUtils.isSnsOptimizationSupported();
        this.mIsSnsOptimizationSupported = zIsSnsOptimizationSupported;
        this.mDelayUpateOosTimer = zIsSnsOptimizationSupported ? 30000 : 600000;
        registerSettingsCallbacks();
        registerReceiver();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mIsOosTimerWorking[i] = false;
            this.mIsSimBplmnSet[i] = false;
            this.mIsSnsSetOosScanState[i] = false;
        }
    }

    private void registerSettingsCallbacks() {
        this.mSettingsObserver.observe(Settings.Global.getUriFor("network_mode_state"), 2);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("hikingModeController.intent.debugall");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    class HikingModeHandler extends Handler {
        HikingModeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            HikingModeController.this.log("msg.what = " + message.what);
            int i = message.what;
            if (i == 1) {
                HikingModeController.this.initAction();
                return;
            }
            if (i == 2) {
                HikingModeController.this.processHikingStateChangeEvent();
                return;
            }
            if (i == 3) {
                HikingModeController.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                return;
            }
            if (i == 4 || i == 5) {
                HikingModeController.this.processOosStateEvent(((Integer) message.obj).intValue());
                return;
            }
            if (i != 100) {
                return;
            }
            HikingModeController.this.VDBG = true;
            HikingModeController.this.mDelayUpateOosTimer = ((Integer) message.obj).intValue();
            HikingModeController.this.log("debug action mDelayUpateOosTimer: " + HikingModeController.this.mDelayUpateOosTimer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHikingStateChangeEvent() {
        int hikingState = getHikingState();
        log("processHikingStateChangeEvent hikingState=" + hikingState + ", mHikingState=" + this.mHikingState + ", mIsHikingIn=" + this.mIsHikingIn);
        if (hikingState == -1 || hikingState == 0 || hikingState == this.mHikingState) {
            return;
        }
        this.mHikingState = hikingState;
        if (hikingState == 1) {
            this.mIsHikingIn = true;
            registerTelephonyInfoCallBack();
            if (MiuiTelephonyStatistic.getInstance() != null && MiuiTelephonyStatistic.getInstance().mHikingModemStats != null) {
                MiuiTelephonyStatistic.getInstance().mHikingModemStats.updateHikingState(true);
            }
        } else {
            this.mIsHikingIn = false;
            int i = 0;
            while (true) {
                boolean[] zArr = this.mIsSnsSetOosScanState;
                if (i >= zArr.length) {
                    break;
                }
                if (zArr[i]) {
                    snsSetOutOfServiceStateIfNeed(i, 1);
                    this.mIsSnsSetOosScanState[i] = false;
                }
                i++;
            }
            unregisterTelephonyInfoCallBack();
            if (MiuiTelephonyStatistic.getInstance() != null && MiuiTelephonyStatistic.getInstance().mHikingModemStats != null) {
                MiuiTelephonyStatistic.getInstance().updateNwBandInfo(DefaultSimManager.getDefaultDataSlotId(), null, true);
                MiuiTelephonyStatistic.getInstance().mHikingModemStats.updateHikingState(false);
            }
        }
        calcHikingModeStatistcIfNeed();
    }

    private void snsSetOutOfServiceStateIfNeed(int i, int i2) {
        if (this.mIsSnsOptimizationSupported && NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().setOutOfServiceState(i, i2, "hiking");
        }
    }

    private int getHikingState() {
        if (this.mNetworkModeRecognition == null) {
            return -1;
        }
        int i = Settings.Global.getInt(mContext.getContentResolver(), "network_mode_state", 0);
        if (!this.mNetworkModeRecognition.isHikingStateUpdate(i)) {
            return -1;
        }
        if (this.mNetworkModeRecognition.isHikingIn(i)) {
            return 1;
        }
        return this.mNetworkModeRecognition.isHikingOut(i) ? 2 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        if (this.VDBG) {
            log("processServiceStateChangeEvent phoneId=" + i + ", manual=" + serviceState.getIsManualSelection() + ", mIsHikingIn=" + this.mIsHikingIn + ", mIsOosTimerWorking=" + this.mIsOosTimerWorking[i] + ", mIsSimBplmnSet=" + this.mIsSimBplmnSet[i] + ", mIsSnsSetOos=" + this.mIsSnsSetOosScanState[i]);
        }
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || !MiuiPhoneUtils.isSimReady(i) || serviceState == null || serviceState.getIsManualSelection()) {
            return;
        }
        int dataRegistrationState = serviceState.getDataRegistrationState();
        int voiceRegState = serviceState.getVoiceRegState();
        if (dataRegistrationState != 1 || voiceRegState != 1) {
            this.mIsSimBplmnSet[i] = false;
            stopMonitorTimerForOosUpdate(i);
        } else {
            if (!this.mIsHikingIn || this.mIsOosTimerWorking[i]) {
                return;
            }
            boolean z = this.mIsSnsOptimizationSupported;
            if ((z || this.mIsSimBplmnSet[i]) && (!z || this.mIsSnsSetOosScanState[i])) {
                return;
            }
            startMonitorTimerForOosUpdate(i);
        }
    }

    void processOosStateEvent(int i) {
        log("processOosStateEvent phoneId=" + i + ", mIsHikingIn=" + this.mIsHikingIn);
        this.mIsOosTimerWorking[i] = false;
        if (this.mIsHikingIn && isNormalCase(i) && isOosState(i)) {
            if (!this.mIsSnsOptimizationSupported) {
                localLog("processOosStateEvent", "setInvalidSimState, phoneId=" + i);
                this.mRilHook.setInvalidSimState(i, 1);
                this.mIsSimBplmnSet[i] = true;
            } else if (!this.mIsSnsSetOosScanState[i]) {
                localLog("processOosStateEvent", "snsSet, phoneId=" + i);
                snsSetOutOfServiceStateIfNeed(i, 0);
                this.mIsSnsSetOosScanState[i] = true;
            }
            recordCountHikingModeStats("hikingModeOosOptCount");
        }
    }

    private boolean isNormalCase(int i) {
        boolean z = TelephonyManager.getDefault().getSimState(i) == 5;
        boolean z2 = Settings.Global.getInt(mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
        log("simReady = " + z + " airplane mode = " + z2);
        return !z2 && z;
    }

    private void startMonitorTimerForOosUpdate(int i) {
        log("startMonitorTimerForOosUpdate phoneId=" + i);
        stopMonitorTimerForOosUpdate(i);
        this.mIsOosTimerWorking[i] = true;
        if (i == 0) {
            HikingModeHandler hikingModeHandler = this.mHandler;
            hikingModeHandler.sendMessageDelayed(hikingModeHandler.obtainMessage(4, Integer.valueOf(i)), this.mDelayUpateOosTimer);
        } else {
            if (i != 1) {
                return;
            }
            HikingModeHandler hikingModeHandler2 = this.mHandler;
            hikingModeHandler2.sendMessageDelayed(hikingModeHandler2.obtainMessage(5, Integer.valueOf(i)), this.mDelayUpateOosTimer);
        }
    }

    private void stopMonitorTimerForOosUpdate(int i) {
        if (i == 0) {
            if (this.mHandler.hasMessages(4)) {
                log("stopMonitorTimerForOosUpdate");
                this.mIsOosTimerWorking[i] = false;
                this.mHandler.removeMessages(4);
                return;
            }
            return;
        }
        if (i == 1 && this.mHandler.hasMessages(5)) {
            log("stopMonitorTimerForOosUpdate");
            this.mIsOosTimerWorking[i] = false;
            this.mHandler.removeMessages(5);
        }
    }

    private void registerTelephonyInfoCallBack() {
        log("registerTelephonyInfoCallBack");
        if (this.mTelephonyInfoCallBack == null) {
            TelephonyInfoCallBack telephonyInfoCallBack = new TelephonyInfoCallBack();
            this.mTelephonyInfoCallBack = telephonyInfoCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyInfoCallBack);
        }
    }

    private void unregisterTelephonyInfoCallBack() {
        TelephonyInfoCallBack telephonyInfoCallBack = this.mTelephonyInfoCallBack;
        if (telephonyInfoCallBack != null) {
            TelephonyStatAdapter.unregisterCallBack(telephonyInfoCallBack);
            this.mTelephonyInfoCallBack = null;
        }
    }

    class TelephonyInfoCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyInfoCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            HikingModeController.this.mHandler.obtainMessage(3, i, 0, serviceState).sendToTarget();
        }
    }

    private boolean isOosState(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        boolean z = false;
        if (phone == null) {
            return false;
        }
        ServiceState serviceState = phone.getServiceState();
        if (serviceState != null) {
            int dataRegistrationState = serviceState.getDataRegistrationState();
            int voiceRegState = serviceState.getVoiceRegState();
            if (dataRegistrationState == 1 && voiceRegState == 1) {
                z = true;
            }
        }
        log("isOosState=" + z + ", phoneId=" + i);
        return z;
    }

    private synchronized void calcHikingModeStatistcIfNeed() {
        try {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (this.mIsHikingIn) {
                this.mLastHikingModeEnterTime = jElapsedRealtime;
                recordCountHikingModeStats("hikingModeCount");
                NetworkModeRecognition networkModeRecognition = this.mNetworkModeRecognition;
                recordListHikingModeStats("hikingModeCity", Integer.valueOf(networkModeRecognition == null ? -1 : networkModeRecognition.mHikingModeCityIndex));
            } else {
                recordListHikingModeStats("hikingModeDuration", Long.valueOf((jElapsedRealtime - this.mLastHikingModeEnterTime) / 1000));
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void recordCountHikingModeStats(String str) {
        HikingModeStats hikingModeStats = this.mHikingModeStats;
        if (hikingModeStats != null) {
            hikingModeStats.recordCount(str);
        }
    }

    private void recordListHikingModeStats(String str, Object obj) {
        HikingModeStats hikingModeStats = this.mHikingModeStats;
        if (hikingModeStats != null) {
            hikingModeStats.recordList(str, obj);
        }
    }

    public void reportStatistics() {
        HikingModeStats hikingModeStats = this.mHikingModeStats;
        if (hikingModeStats != null) {
            hikingModeStats.reportStatistic();
            this.mHikingModeStats.clear();
        }
    }

    public void reloadHikingModeStatsFromSP() {
        HikingModeStats hikingModeStats = this.mHikingModeStats;
        if (hikingModeStats != null) {
            hikingModeStats.reloadStatsFromSP();
        }
    }

    class HikingModeStats {
        private ConcurrentHashMap mStats;

        private HikingModeStats() {
            this.mStats = new ConcurrentHashMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordCount(String str) {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null) {
                return;
            }
            concurrentHashMap.put(str, Integer.valueOf(((Integer) concurrentHashMap.getOrDefault(str, 0)).intValue() + 1));
            try {
                if (HikingModeController.this.VDBG) {
                    HikingModeController.this.log("recordCount key=" + str + ", mStats=" + this.mStats);
                }
                DefaultPreferenceHelper.getInstance().setHikingModeStats(ObjectSerializer.serialize(this.mStats));
            } catch (Exception e) {
                HikingModeController.this.log("record exception:" + e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordList(String str, Object obj) {
            if (this.mStats == null) {
                return;
            }
            if (HikingModeController.this.VDBG) {
                HikingModeController.this.log("record key=" + str + ", value=" + obj);
            }
            if (obj instanceof Integer) {
                ArrayList arrayListObjToList = HikingModeController.objToList(this.mStats.getOrDefault(str, null), Integer.class);
                arrayListObjToList.add((Integer) obj);
                this.mStats.put(str, arrayListObjToList);
            } else if (obj instanceof Long) {
                ArrayList arrayListObjToList2 = HikingModeController.objToList(this.mStats.getOrDefault(str, null), Long.class);
                arrayListObjToList2.add((Long) obj);
                this.mStats.put(str, arrayListObjToList2);
            } else {
                if (HikingModeController.this.VDBG) {
                    HikingModeController.this.log("record key=" + str + ", value type not support");
                    return;
                }
                return;
            }
            try {
                if (HikingModeController.this.VDBG) {
                    HikingModeController.this.log("recordList mStats=" + this.mStats);
                }
                DefaultPreferenceHelper.getInstance().setHikingModeStats(ObjectSerializer.serialize(this.mStats));
            } catch (Exception e) {
                HikingModeController.this.log("record exception:" + e);
            }
        }

        public void reportStatistic() {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
                return;
            }
            HashMap map = new HashMap(this.mStats);
            HikingModeController.this.log("reportStatistic mStats=" + this.mStats);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_hiking_mode_statistic", map);
        }

        public void reloadStatsFromSP() {
            try {
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) ObjectSerializer.deserialize(DefaultPreferenceHelper.getInstance().getHikingModeStats());
                if (concurrentHashMap != null) {
                    this.mStats = concurrentHashMap;
                }
                if (HikingModeController.this.VDBG) {
                    HikingModeController.this.log("reloadStatsFromSP mStats=" + this.mStats);
                }
            } catch (Exception e) {
                HikingModeController.this.log("reloadStatsFromSP exception:" + e);
            }
        }

        public void clear() {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null) {
                return;
            }
            concurrentHashMap.clear();
            try {
                DefaultPreferenceHelper.getInstance().setHikingModeStats(ObjectSerializer.serialize(this.mStats));
            } catch (Exception e) {
                HikingModeController.this.log("clear exception:" + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ArrayList objToList(Object obj, Class cls) {
        ArrayList arrayList = new ArrayList();
        try {
            if (obj instanceof ArrayList) {
                ArrayList arrayList2 = (ArrayList) obj;
                int size = arrayList2.size();
                int i = 0;
                while (i < size) {
                    Object obj2 = arrayList2.get(i);
                    i++;
                    arrayList.add(cls.cast(obj2));
                }
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return arrayList;
        }
    }

    private void localLog(String str, String str2) {
        NetworkMetricsController.localLog("HikingModeController", str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("HikingModeController", str);
    }
}
