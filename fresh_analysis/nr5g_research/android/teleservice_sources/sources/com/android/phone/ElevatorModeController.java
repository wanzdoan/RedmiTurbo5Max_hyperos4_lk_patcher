package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorEvent;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.VideoPreloadScene;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class ElevatorModeController {
    private final BroadcastReceiver mBroadcastReceiver;
    private Context mContext;
    private int mCurBasementStatus;
    private int mDdsPhoneId;
    private ElevatorModeStats mElevatorModeStats;
    private int mElevatorPreloadVerifyFail;
    private Set mFddSet;
    private ElevatorModeHandler mHandler;
    private boolean mIsInElevator;
    private boolean mIsInGame;
    private boolean mIsOosElevator;
    private boolean mIsPoorSignalElevator;
    private boolean mIsWifiConnected;
    private int[] mLteCount;
    private NetworkMetricsController mNetworkMetricsController;
    private int[] mNrCount;
    private String mOutElevatorRecordCell;
    private long mOutElevatorRecordTime;
    private Phone[] mPhones;
    private PowerManager mPowerManager;
    private RilHook mRilHook;
    private int mScene;
    private long mStateChangeTime;
    private TelephonyManager mTelephonyManager;
    private boolean mTestReturnNr;
    private String mTriggerReason;
    LRU outElevatorCellLru;
    private static List mPreloadVideoWhiteList = new ArrayList();
    private static List mResolutionVideoWhiteList = new ArrayList();
    private static ElevatorModeController mIntance = null;
    private boolean VDBG = false;
    public int mCoverageTypeV3 = -1;
    private long mLastOutElevatorTime = 0;
    private String mLastPredictCellInfo = "";
    private int[] mLteDealyTimer = {3000, 10000, 20000};
    private int[] mNrDealyTimer = {20000, 20000, 20000};
    private int mActiveDataSubId = -1;
    private int mAnotherDataSubId = -1;

    private int inRangeOrUnavailable(int i, int i2, int i3) {
        if (i < i2 || i > i3) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    public static synchronized ElevatorModeController init(Context context) {
        try {
            if (mIntance == null) {
                mIntance = new ElevatorModeController(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return mIntance;
    }

    public static ElevatorModeController getInstance() {
        return mIntance;
    }

    private ElevatorModeController(Context context) {
        this.mContext = null;
        this.mHandler = null;
        this.mTelephonyManager = null;
        this.mPowerManager = null;
        this.mRilHook = null;
        this.mPhones = null;
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mLteCount = new int[i];
        this.mNrCount = new int[i];
        this.mNetworkMetricsController = null;
        this.mTestReturnNr = false;
        this.mIsInGame = false;
        this.mElevatorPreloadVerifyFail = 0;
        this.mScene = Integer.MIN_VALUE;
        this.mTriggerReason = "";
        this.mIsInElevator = false;
        this.mIsPoorSignalElevator = false;
        this.mIsOosElevator = false;
        this.outElevatorCellLru = new LRU(10, 0.75f, true);
        this.mIsWifiConnected = false;
        this.mOutElevatorRecordCell = "";
        this.mOutElevatorRecordTime = 0L;
        this.mDdsPhoneId = -1;
        this.mFddSet = new HashSet();
        this.mCurBasementStatus = -1;
        this.mStateChangeTime = 0L;
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.ElevatorModeController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action;
                if (intent == null || (action = intent.getAction()) == null) {
                    return;
                }
                if (action.equals("com.android.phone.intent.action.GAME_SCENE")) {
                    ElevatorModeController.this.mIsInGame = intent.getBooleanExtra("gameEnabled", false);
                    ElevatorModeController.this.log("game mode =" + ElevatorModeController.this.mIsInGame);
                    return;
                }
                if (action.equals("android.net.wifi.STATE_CHANGE")) {
                    ElevatorModeController.this.mHandler.obtainMessage(111, intent).sendToTarget();
                    return;
                }
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    ElevatorModeController.this.mHandler.obtainMessage(ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.ACTION_CLOUD_PRELOAD_VIDEO_APPS_LIST_DONE")) {
                    ElevatorModeController.this.log("onReceive ACTION_CLOUD_PRELOAD_VIDEO_APPS_LIST_DONE");
                    ElevatorModeController.this.initPreloadVideoWhiteList();
                } else if (action.equals("com.android.phone.intent.action.ACTION_CLOUD_RESOLUTION_VIDEO_APPS_LIST_DONE")) {
                    ElevatorModeController.this.log("onReceive ACTION_CLOUD_RESOLUTION_VIDEO_APPS_LIST_DONE");
                    ElevatorModeController.this.initResolutionVideoWhiteList();
                } else if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                    ElevatorModeController.this.mHandler.obtainMessage(118).sendToTarget();
                } else if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    ElevatorModeController.this.mHandler.obtainMessage(119).sendToTarget();
                }
            }
        };
        this.mContext = context;
        this.mRilHook = RilHook.init(context);
        this.mHandler = new ElevatorModeHandler();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mPowerManager = (PowerManager) this.mContext.getSystemService("power");
        this.mPhones = PhoneFactory.getPhones();
        this.mDdsPhoneId = getDefaultDataSlotId();
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            this.mLteCount[i2] = 0;
            this.mNrCount[i2] = 0;
        }
        registerBroadcastReceiver();
        initPreloadVideoWhiteList();
        initResolutionVideoWhiteList();
        this.mElevatorModeStats = new ElevatorModeStats();
        if (MiuiPhoneUtils.isBasementV3Supported()) {
            initFddBand();
        }
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.GAME_SCENE");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("com.android.phone.intent.action.ACTION_CLOUD_PRELOAD_VIDEO_APPS_LIST_DONE");
        intentFilter.addAction("com.android.phone.intent.action.ACTION_CLOUD_RESOLUTION_VIDEO_APPS_LIST_DONE");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPreloadVideoWhiteList() {
        if (mPreloadVideoWhiteList.size() > 0) {
            mPreloadVideoWhiteList.clear();
        }
        List preloadVideoAppsWhiteListByCloud = FiveGManagerBase.getPreloadVideoAppsWhiteListByCloud(this.mContext);
        mPreloadVideoWhiteList = preloadVideoAppsWhiteListByCloud;
        if (preloadVideoAppsWhiteListByCloud.size() != 0) {
            log("mPreloadVideoWhiteList=" + mPreloadVideoWhiteList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initResolutionVideoWhiteList() {
        if (mResolutionVideoWhiteList.size() > 0) {
            mResolutionVideoWhiteList.clear();
        }
        List resolutionVideoAppsWhiteListByCloud = FiveGManagerBase.getResolutionVideoAppsWhiteListByCloud(this.mContext);
        mResolutionVideoWhiteList = resolutionVideoAppsWhiteListByCloud;
        if (resolutionVideoAppsWhiteListByCloud.size() != 0) {
            log("mResolutionVideoWhiteList=" + mResolutionVideoWhiteList);
        }
    }

    class ElevatorModeHandler extends Handler {
        private ElevatorModeHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 101:
                case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET /* 116 */:
                    ElevatorModeController.this.log(" Network recovery  by: " + message.what);
                    if (ElevatorModeController.this.mNetworkMetricsController != null) {
                        NetworkMetricsController.Scene scene = NetworkMetricsController.Scene.OUT_ELEVATOR;
                        if (HyperOSCustFeatureResolve.getBoolean("config_restore_nr_if_need", false)) {
                            ElevatorModeController.this.mNetworkMetricsController.restoreNrIfIndeed(scene);
                        }
                        if (ElevatorModeController.this.mIsInGame || ElevatorModeController.this.isColddownPeriod()) {
                            ElevatorModeController.this.log("restrict by game_mode or state_change");
                        } else if (!ElevatorModeController.this.mNetworkMetricsController.shouldAvoidByScene(scene)) {
                            ElevatorModeController.this.handleOutElevatorOrBasement(scene);
                        }
                        break;
                    }
                    break;
                case 102:
                    ElevatorModeController.this.startLteMonitor(((Integer) message.obj).intValue());
                    break;
                case 103:
                    ElevatorModeController.this.startNrMonitor(((Integer) message.obj).intValue());
                    break;
                case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                    if (ElevatorModeController.this.mElevatorModeStats.listenServiceState()) {
                        ElevatorModeController.this.mElevatorModeStats.serviceStateChange(message.arg1, (ServiceState) message.obj);
                    }
                    int i = message.arg1;
                    if (ElevatorModeController.this.isCommutingTime() && ElevatorModeController.this.mIsInElevator && !ElevatorModeController.this.mIsOosElevator && !ElevatorModeController.this.mIsWifiConnected && ElevatorModeController.this.mDdsPhoneId == i) {
                        ElevatorModeController.this.processServiceStateChangeEvent(i, (ServiceState) message.obj);
                        break;
                    }
                    break;
                case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                    int i2 = message.arg1;
                    if (!ElevatorModeController.this.mIsWifiConnected && MiuiPhoneUtils.isDataRegistered(ElevatorModeController.this.mDdsPhoneId) && ElevatorModeController.this.mDdsPhoneId == i2 && ElevatorModeController.this.isCommutingTime()) {
                        if (ElevatorModeController.this.mOutElevatorRecordTime != 0) {
                            long jCurrentTimeMillis = System.currentTimeMillis() - ElevatorModeController.this.mOutElevatorRecordTime;
                            ElevatorModeController elevatorModeController = ElevatorModeController.this;
                            if (jCurrentTimeMillis > 300000) {
                                elevatorModeController.mOutElevatorRecordTime = 0L;
                            } else {
                                elevatorModeController.updateCellInfo(i2, (CellLocation) message.obj);
                                ElevatorModeController.this.mOutElevatorRecordTime = 0L;
                            }
                            break;
                        } else if (!ElevatorModeController.this.mIsInElevator && !ElevatorModeController.this.mIsWifiConnected) {
                            ElevatorModeController.this.predictNextCellStatus(i2, (CellLocation) message.obj);
                            break;
                        }
                    }
                    break;
                case 106:
                    if (MiuiPhoneUtils.isValidatePhoneId(ElevatorModeController.this.mDdsPhoneId) && ElevatorModeController.this.mNetworkMetricsController != null) {
                        if (ElevatorModeController.this.mNetworkMetricsController.isLteNrNwType(ElevatorModeController.this.mTelephonyManager.getDataNetworkType(ElevatorModeController.this.mDdsPhoneId)) && !ElevatorModeController.this.mIsWifiConnected && ElevatorModeController.this.isCommutingTime()) {
                            ElevatorModeController.this.recordOutElevatorCell();
                            break;
                        }
                    }
                    break;
                case 107:
                    long jCurrentTimeMillis2 = System.currentTimeMillis();
                    if (!TextUtils.isEmpty(ElevatorModeController.this.mLastPredictCellInfo) && ElevatorModeController.this.mLastOutElevatorTime + 600000 < jCurrentTimeMillis2) {
                        ElevatorModeController.this.log("EVENT_PREDICT_CELL_VERIFY fail");
                        ElevatorModeController elevatorModeController2 = ElevatorModeController.this;
                        elevatorModeController2.outElevatorCellLru.remove(elevatorModeController2.mLastPredictCellInfo);
                        ElevatorModeController.this.mElevatorPreloadVerifyFail++;
                        break;
                    }
                    break;
                case 108:
                    ElevatorModeController.this.mIsInElevator = true;
                    ElevatorModeController.this.mHandler.sendMessageDelayed(ElevatorModeController.this.mHandler.obtainMessage(109), 120000L);
                    ElevatorModeController.this.log("EVENT_IN_ELEVATOR mIsInElevator : " + ElevatorModeController.this.mIsInElevator);
                    break;
                case 109:
                    ElevatorModeController.this.removeElevatorMessages();
                    ElevatorModeController.this.mIsInElevator = false;
                    ElevatorModeController.this.mIsPoorSignalElevator = false;
                    ElevatorModeController.this.mIsOosElevator = false;
                    ElevatorModeController.this.log("EVENT_IN_ELEVATOR_DELAY mIsInElevator : " + ElevatorModeController.this.mIsInElevator);
                    break;
                case 110:
                    int i3 = message.arg1;
                    ElevatorModeController.this.log("EVENT_SIGNAL_STRENGTH_CHANGED mIsInElevator = " + ElevatorModeController.this.mIsInElevator + " mIsPoorSignalElevator = " + ElevatorModeController.this.mIsPoorSignalElevator + " mIsWifiConnected = " + ElevatorModeController.this.mIsWifiConnected);
                    if (ElevatorModeController.this.mIsInElevator && !ElevatorModeController.this.mIsPoorSignalElevator && !ElevatorModeController.this.mIsWifiConnected && i3 == ElevatorModeController.this.mDdsPhoneId && ElevatorModeController.this.isCommutingTime()) {
                        ElevatorModeController.this.processSignalStrengthEvent(i3, (SignalStrength) message.obj);
                        break;
                    }
                    break;
                case 111:
                    NetworkInfo networkInfo = (NetworkInfo) ((Intent) message.obj).getParcelableExtra("networkInfo");
                    ElevatorModeController.this.mIsWifiConnected = networkInfo != null && networkInfo.isConnected();
                    break;
                case 112:
                    int iIntValue = ((Integer) message.obj).intValue();
                    ElevatorModeController elevatorModeController3 = ElevatorModeController.this;
                    if (ElevatorModeController.this.mLastPredictCellInfo.equals(elevatorModeController3.getCellInfoKey(elevatorModeController3.mDdsPhoneId))) {
                        ElevatorModeController.this.notify3rdAppOpt(iIntValue);
                        ElevatorModeController.this.mHandler.sendMessageDelayed(ElevatorModeController.this.mHandler.obtainMessage(107), 600000L);
                    }
                    break;
                case ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE /* 113 */:
                    ElevatorModeController.this.mDdsPhoneId = ElevatorModeController.this.getDefaultDataSlotId();
                    ElevatorModeController.this.mStateChangeTime = System.currentTimeMillis();
                    break;
                case ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX /* 114 */:
                    if (ElevatorModeController.this.mNetworkMetricsController != null) {
                        if (ElevatorModeController.this.mIsInGame || ElevatorModeController.this.isColddownPeriod()) {
                            ElevatorModeController.this.log("EVENT_OUT_BASEMENT: restrict by game_mode or state_change");
                        } else {
                            NetworkMetricsController.Scene scene2 = NetworkMetricsController.Scene.OUT_BASEMENT;
                            if (ElevatorModeController.this.mNetworkMetricsController.shouldAvoidByScene(scene2)) {
                                ElevatorModeController.this.log("EVENT_OUT_BASEMENT: avoid by scene");
                            } else {
                                boolean zIsBasementV3Supported = MiuiPhoneUtils.isBasementV3Supported();
                                ElevatorModeController elevatorModeController4 = ElevatorModeController.this;
                                if (zIsBasementV3Supported) {
                                    elevatorModeController4.handleOutBasementV3(scene2);
                                } else {
                                    elevatorModeController4.handleOutElevatorOrBasement(scene2);
                                }
                            }
                        }
                        break;
                    }
                    break;
                case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET /* 115 */:
                    if (ElevatorModeController.this.mCurBasementStatus == 1) {
                        ElevatorModeController elevatorModeController5 = ElevatorModeController.this;
                        elevatorModeController5.setCoverageStateV3(elevatorModeController5.mDdsPhoneId, 2, "coverage");
                    }
                    break;
                case 117:
                    if (ElevatorModeController.this.mElevatorModeStats.listenServiceState()) {
                        ElevatorModeController.this.mElevatorModeStats.recordAbnormalEndOutCoverageStatetimeV3(ElevatorModeController.this.mDdsPhoneId, -2L);
                    }
                    break;
                case 118:
                case 119:
                    ElevatorModeController.this.mStateChangeTime = System.currentTimeMillis();
                    break;
            }
        }
    }

    public synchronized void onOutElevatorOrBasementEvent(SensorEvent sensorEvent) {
        if (ModemProvider.IS_XRING) {
            log("onOutElevatorEvent xring ignore");
            return;
        }
        int i = Integer.MIN_VALUE;
        this.mScene = Integer.MIN_VALUE;
        try {
            i = (int) sensorEvent.values[0];
        } catch (Exception e) {
            log("onOutElevatorEvent Exception:" + e);
        }
        log("onOutElevatorEvent Sensor State:" + i);
        boolean z = ModemProvider.IS_QCOM;
        int i2 = z ? 1000 : 3000;
        if ((z || isSupportElevatorModeV3ForMtk()) && i == 2) {
            removeElevatorMessages();
            this.mHandler.obtainMessage(108).sendToTarget();
        } else if (((z || isSupportElevatorModeV3ForMtk()) && i == 1) || (ModemProvider.IS_MTK && i == 4)) {
            this.mScene = i;
            removeElevatorMessages();
            ElevatorModeHandler elevatorModeHandler = this.mHandler;
            elevatorModeHandler.sendMessageDelayed(elevatorModeHandler.obtainMessage(101), i2);
        } else if (z && i == 3) {
            this.mScene = i;
            removeElevatorMessages();
            ElevatorModeHandler elevatorModeHandler2 = this.mHandler;
            elevatorModeHandler2.sendMessageDelayed(elevatorModeHandler2.obtainMessage(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET), 3000L);
        } else if (i == -1) {
            this.mScene = i;
            removeElevatorMessages();
            ElevatorModeHandler elevatorModeHandler3 = this.mHandler;
            elevatorModeHandler3.sendMessageDelayed(elevatorModeHandler3.obtainMessage(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX), i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeElevatorMessages() {
        this.mHandler.removeMessages(108);
        this.mHandler.removeMessages(101);
        this.mHandler.removeMessages(102);
        this.mHandler.removeMessages(103);
        this.mHandler.removeMessages(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX);
        this.mHandler.removeMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET);
    }

    private boolean isSupportElevatorModeV3ForMtk() {
        return HyperOSCustFeatureResolve.getBoolean("support_elevator_mode_v3", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleOutElevatorOrBasement(NetworkMetricsController.Scene scene) {
        try {
            this.mActiveDataSubId = SubscriptionManager.getActiveDataSubscriptionId();
            log("handleOutElevatorOrBasement active data subId " + this.mActiveDataSubId);
            if (SubscriptionManager.isValidSubscriptionId(this.mActiveDataSubId) && this.mNetworkMetricsController != null) {
                int phoneId = SubscriptionManager.getPhoneId(this.mActiveDataSubId);
                if (MiuiPhoneUtils.isValidatePhoneId(phoneId)) {
                    if (needReturnToLte(this.mActiveDataSubId)) {
                        this.mLteCount[phoneId] = 0;
                        startFastReturnLte(phoneId);
                    }
                    if (needReturnToNr(this.mActiveDataSubId) && !this.mNetworkMetricsController.isSaOffBySpecialNetworkOptimization()) {
                        this.mNrCount[phoneId] = 0;
                        startFastReturnNr(phoneId);
                    }
                    if (MiuiPhoneUtils.getInsertedSimCount() == 2) {
                        int anotherPhoneId = getAnotherPhoneId(phoneId);
                        if (!MiuiPhoneUtils.isValidatePhoneId(anotherPhoneId)) {
                            return;
                        }
                        if (!MiuiPhoneUtils.isIccCardActivated(anotherPhoneId)) {
                            return;
                        }
                        this.mAnotherDataSubId = getSubId(anotherPhoneId);
                        log("handleOutElevatorOrBasement another data subId " + this.mAnotherDataSubId);
                        if (SubscriptionManager.isValidSubscriptionId(this.mAnotherDataSubId) && needReturnToLte(this.mAnotherDataSubId)) {
                            this.mLteCount[anotherPhoneId] = 0;
                            startFastReturnLte(anotherPhoneId);
                        }
                    }
                    if (scene == NetworkMetricsController.Scene.OUT_ELEVATOR) {
                        if (!this.mIsInElevator) {
                            log("Exception reported by elevator sensor!");
                            return;
                        }
                        this.mHandler.removeMessages(109);
                        this.mIsInElevator = false;
                        this.mLastOutElevatorTime = System.currentTimeMillis();
                        if (!isCommutingTime()) {
                            resetElevatorAbnormal();
                        } else if (!this.mIsOosElevator && !this.mIsPoorSignalElevator) {
                            log("recordOutElevatorCell : nothing abnormal occurred in the elevator!");
                        } else {
                            resetElevatorAbnormal();
                            ElevatorModeHandler elevatorModeHandler = this.mHandler;
                            elevatorModeHandler.sendMessageDelayed(elevatorModeHandler.obtainMessage(106), 10000L);
                        }
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOutBasementV3(NetworkMetricsController.Scene scene) {
        log("handleOutBasementV3 active data subId " + this.mDdsPhoneId);
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            if (needReturnToLte(getSubId(this.mDdsPhoneId))) {
                int[] iArr = this.mLteCount;
                int i = this.mDdsPhoneId;
                iArr[i] = 0;
                startFastReturnLte(i);
                return;
            }
            if (needExecuteBasementV3(this.mDdsPhoneId)) {
                trySetCoverageStateBasementV3();
            }
        }
    }

    private void trySetCoverageStateBasementV3() {
        if (this.mHandler.hasMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET)) {
            this.mHandler.removeMessages(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET);
        }
        log("trySetCoverageStateBasementV3 need Execute BasementV3");
        setCoverageStateV3(this.mDdsPhoneId, 1, "coverage");
        ElevatorModeHandler elevatorModeHandler = this.mHandler;
        elevatorModeHandler.sendMessageDelayed(elevatorModeHandler.obtainMessage(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET), 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCoverageStateV3(int i, int i2, String str) {
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            if (i2 == 2) {
                recordOutCoverageStateV3(i, str);
            }
            this.mCurBasementStatus = i2;
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setCoverageState(i, i2, str);
            }
        }
    }

    private void resetElevatorAbnormal() {
        this.mIsPoorSignalElevator = false;
        this.mIsOosElevator = false;
    }

    public void recordOutCoverageStateV3(int i, String str) {
        if (this.mTelephonyManager == null) {
            return;
        }
        if (this.mHandler.hasMessages(117)) {
            this.mHandler.removeMessages(117);
        }
        ElevatorModeHandler elevatorModeHandler = this.mHandler;
        elevatorModeHandler.sendMessageDelayed(elevatorModeHandler.obtainMessage(117), 120000L);
        int dataNetworkType = this.mTelephonyManager.getDataNetworkType(i);
        if (Utils.isLte(dataNetworkType)) {
            this.mCoverageTypeV3 = 1;
        } else if (Utils.isNr(dataNetworkType)) {
            this.mCoverageTypeV3 = 2;
        }
        this.mElevatorModeStats.recordStartOutCoverageStatetimeV3(i, str);
    }

    private int getAnotherPhoneId(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            return i == 0 ? 1 : 0;
        }
        return -1;
    }

    private int getSubId(int i) {
        Phone phone;
        Phone[] phoneArr = this.mPhones;
        if (phoneArr == null || (phone = phoneArr[i]) == null) {
            return -1;
        }
        return phone.getSubId();
    }

    private boolean needReturnToLte(int i) {
        if (MiuiPhoneUtils.isDuringCall() || this.mTelephonyManager == null || this.mNetworkMetricsController == null) {
            return false;
        }
        if (!isScreenOn()) {
            log("screen off state, ignore");
            return false;
        }
        if (isAirPlaneModeOn()) {
            log("air plane mode, ignore");
            return false;
        }
        int dataNetworkType = this.mTelephonyManager.getDataNetworkType(i);
        int phoneId = SubscriptionManager.getPhoneId(i);
        boolean zIsLteNrNwMode = isLteNrNwMode(phoneId);
        boolean zIsLteNrNwType = this.mNetworkMetricsController.isLteNrNwType(dataNetworkType);
        boolean zIsDataRegistered = MiuiPhoneUtils.isDataRegistered(phoneId);
        log("isDataRegistered:" + zIsDataRegistered + " networkType: " + dataNetworkType + " isLteNrNwType:" + zIsLteNrNwType + " isLteNrNwMode: " + zIsLteNrNwMode);
        if (!(zIsDataRegistered && zIsLteNrNwType) && ((zIsLteNrNwMode || this.VDBG) && isAutoSelection(phoneId))) {
            return true;
        }
        log("needReturnToLte : is L/N nw return false");
        return false;
    }

    private boolean needReturnToNr(int i) {
        if (!MiuiPhoneUtils.isDuringCall() && this.mTelephonyManager != null && this.mNetworkMetricsController != null && (MiuiPhoneUtils.isSnsOptimizationSupported() || ModemProvider.IS_MTK)) {
            if (!isScreenOn()) {
                log("screen off state, ignore");
                return false;
            }
            if (isAirPlaneModeOn()) {
                log("air plane mode, ignore");
                return false;
            }
            if (SubscriptionManager.getActiveDataSubscriptionId() != i) {
                log("ActiveDataSbuId isn't equal current subId, ignore");
                return false;
            }
            int dataNetworkType = this.mTelephonyManager.getDataNetworkType(i);
            int phoneId = SubscriptionManager.getPhoneId(i);
            boolean zIsNrNwMode = isNrNwMode(phoneId);
            boolean zIsLteNwType = this.mNetworkMetricsController.isLteNwType(dataNetworkType);
            boolean zIsDataRegistered = MiuiPhoneUtils.isDataRegistered(phoneId);
            log("isNrNwMode:" + zIsNrNwMode + " isLteType: " + zIsLteNwType + " isDataRegistered: " + zIsDataRegistered);
            if (zIsNrNwMode && zIsLteNwType && zIsDataRegistered && isAutoSelection(phoneId)) {
                log("needReturnToNr");
                return true;
            }
        }
        return false;
    }

    private boolean needExecuteBasementV3(int i) {
        if (!MiuiPhoneUtils.isDuringCall() && this.mTelephonyManager != null && this.mNetworkMetricsController != null) {
            if (!isScreenOn()) {
                log("needExecuteBasementV3 screen off state, ignore");
                return false;
            }
            if (isAirPlaneModeOn()) {
                log("needExecuteBasementV3 air plane mode, ignore");
                return false;
            }
            int dataNetworkType = this.mTelephonyManager.getDataNetworkType(getSubId(i));
            boolean zIsLteNrNwMode = isLteNrNwMode(i);
            boolean zIsLteNwType = this.mNetworkMetricsController.isLteNwType(dataNetworkType);
            boolean zIsNrNwType = this.mNetworkMetricsController.isNrNwType(dataNetworkType);
            boolean zIsDataRegistered = MiuiPhoneUtils.isDataRegistered(i);
            log("needExecuteBasementV3 isDataRegistered:" + zIsDataRegistered + " networkType: " + dataNetworkType + " isLteNwType:" + zIsLteNwType + " isNrNwType: " + zIsNrNwType + " isLteNrNwMode: " + zIsLteNrNwMode);
            if (zIsDataRegistered && ((zIsLteNwType || (zIsNrNwType && isFrequencyDivisionDuplex(i))) && ((isAutoSelection(i) || this.VDBG) && (zIsLteNrNwMode || this.VDBG)))) {
                log("needExecuteBasementV3: return true");
                return true;
            }
        }
        return false;
    }

    private synchronized void startFastReturnLte(int i) {
        try {
            if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                log("startFastReturnLte  mLteCount: " + this.mLteCount[i]);
                this.mElevatorModeStats.recordStartScanLteTime(SystemClock.elapsedRealtime(), i);
                int i2 = this.mLteCount[i];
                if (i2 < 3) {
                    forceScanLte(i, i2);
                    ElevatorModeHandler elevatorModeHandler = this.mHandler;
                    elevatorModeHandler.sendMessageDelayed(elevatorModeHandler.obtainMessage(102, Integer.valueOf(getSubId(i))), this.mLteDealyTimer[i2]);
                    int[] iArr = this.mLteCount;
                    iArr[i] = iArr[i] + 1;
                } else {
                    this.mElevatorModeStats.recordEndScanLteTime(-1L, i);
                    log("current can't return to LTE!!");
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void startFastReturnNr(int i) {
        try {
            if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                log("startFastReturnNr  mNrCount: " + this.mNrCount[i]);
                this.mElevatorModeStats.recordStartL2NrTime(SystemClock.elapsedRealtime(), i);
                int i2 = this.mNrCount[i];
                if (i2 < 3) {
                    forceLte2Nr(i, i2);
                    ElevatorModeHandler elevatorModeHandler = this.mHandler;
                    elevatorModeHandler.sendMessageDelayed(elevatorModeHandler.obtainMessage(103, Integer.valueOf(getSubId(i))), this.mNrDealyTimer[i2]);
                    int[] iArr = this.mNrCount;
                    iArr[i] = iArr[i] + 1;
                } else {
                    this.mElevatorModeStats.recordEndL2NrTime(-1L, i);
                    log("current can't return to Nr!!");
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startLteMonitor(int i) {
        try {
            log("startLteMonitor subId " + i);
            int phoneId = SubscriptionManager.getPhoneId(i);
            if (SubscriptionManager.isValidSubscriptionId(i) && MiuiPhoneUtils.isValidatePhoneId(phoneId) && this.mNetworkMetricsController != null) {
                if (needReturnToLte(i)) {
                    startFastReturnLte(phoneId);
                } else {
                    this.mLteCount[phoneId] = 0;
                    if (phoneId == this.mDdsPhoneId && needReturnToNr(i) && !this.mNetworkMetricsController.isSaOffBySpecialNetworkOptimization() && !MiuiPhoneUtils.isBasementV3Supported()) {
                        this.mNrCount[phoneId] = 0;
                        startFastReturnNr(phoneId);
                    } else if (phoneId == this.mDdsPhoneId && MiuiPhoneUtils.isBasementV3Supported() && needExecuteBasementV3(phoneId)) {
                        trySetCoverageStateBasementV3();
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startNrMonitor(int i) {
        try {
            log("startNrMonitor subId " + i);
            int phoneId = SubscriptionManager.getPhoneId(i);
            if (SubscriptionManager.isValidSubscriptionId(i) && MiuiPhoneUtils.isValidatePhoneId(phoneId) && this.mNetworkMetricsController != null) {
                if (needReturnToNr(i) && !this.mNetworkMetricsController.isSaOffBySpecialNetworkOptimization()) {
                    startFastReturnNr(phoneId);
                } else {
                    this.mNrCount[phoneId] = 0;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private int getPreferredNwModeFromDb(int i) {
        return ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
    }

    private boolean isLteNrNwMode(int i) {
        int preferredNwModeFromDb = getPreferredNwModeFromDb(i);
        return (preferredNwModeFromDb == 0 || preferredNwModeFromDb == 1 || preferredNwModeFromDb == 2 || preferredNwModeFromDb == 3 || preferredNwModeFromDb == 5 || preferredNwModeFromDb == 6 || preferredNwModeFromDb == 7 || preferredNwModeFromDb == 13 || preferredNwModeFromDb == 14 || preferredNwModeFromDb == 16 || preferredNwModeFromDb == 18 || preferredNwModeFromDb == 21) ? false : true;
    }

    private boolean isNrNwMode(int i) {
        int preferredNwModeFromDb = getPreferredNwModeFromDb(i);
        return (preferredNwModeFromDb == 0 || preferredNwModeFromDb == 1 || preferredNwModeFromDb == 2 || preferredNwModeFromDb == 3 || preferredNwModeFromDb == 5 || preferredNwModeFromDb == 6 || preferredNwModeFromDb == 7 || preferredNwModeFromDb == 13 || preferredNwModeFromDb == 14 || preferredNwModeFromDb == 16 || preferredNwModeFromDb == 18 || preferredNwModeFromDb == 21 || preferredNwModeFromDb == 13 || preferredNwModeFromDb == 19) ? false : true;
    }

    private void forceScanLte(int i, int i2) {
        NetworkMetricsController.Scene scene;
        if (this.mScene == -1) {
            scene = NetworkMetricsController.Scene.OUT_BASEMENT;
        } else {
            scene = NetworkMetricsController.Scene.OUT_ELEVATOR;
        }
        localLog("F_L", "phoneId =" + i + ", count =" + i2 + ", scene =" + scene.getValue());
        RilHook rilHook = this.mRilHook;
        if (rilHook != null) {
            rilHook.forceScanLte(i);
        }
    }

    private void forceLte2Nr(int i, int i2) {
        NetworkMetricsController.Scene scene;
        RilHook rilHook;
        if (this.mScene == -1) {
            scene = NetworkMetricsController.Scene.OUT_BASEMENT;
        } else {
            scene = NetworkMetricsController.Scene.OUT_ELEVATOR;
        }
        boolean zIsMovelikeState = NetworkModeRecognition.getInstance().isMovelikeState();
        localLog("F_N", "phoneId =" + i + ", count =" + i2 + ", scene =" + scene.getValue() + ", subway=" + zIsMovelikeState);
        if ((zIsMovelikeState && (scene == NetworkMetricsController.Scene.OUT_ELEVATOR || scene == NetworkMetricsController.Scene.OUT_BASEMENT)) || (rilHook = this.mRilHook) == null) {
            return;
        }
        rilHook.forceLte2Nr(i);
    }

    private boolean isScreenOn() {
        PowerManager powerManager = this.mPowerManager;
        if (powerManager != null) {
            return powerManager.isScreenOn();
        }
        return false;
    }

    private boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    private void localLog(String str, String str2) {
        NetworkMetricsController.localLog("ElevatorModeController", str, str2);
    }

    public void reportStatistics() {
        ElevatorModeStats elevatorModeStats = this.mElevatorModeStats;
        if (elevatorModeStats == null) {
            return;
        }
        elevatorModeStats.reportStatistics();
        this.mElevatorModeStats.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("ElevatorModeController", str);
    }

    class ElevatorModeStats {
        List basementFDD2TDDDurationsV3;
        List basementFDD2TDDTriggerReasonV3;
        List basementL2NrDurations;
        List basementL2NrDurationsV3;
        List basementL2NrTriggerReasonV3;
        List basementScanLteDurations;
        List elevatorL2NrDurations;
        List elevatorPreloadPkgName;
        List elevatorScanLteDurations;
        private TelephonyCallBack mTelephonyCallBack;
        private long[] startCoverageTimeV3;
        private long[] startL2NrTime;
        private long[] startScanLteTime;

        public ElevatorModeStats() {
            int i = MiuiPhoneUtils.PHONE_COUNT;
            this.startL2NrTime = new long[i];
            this.startCoverageTimeV3 = new long[i];
            this.startScanLteTime = new long[i];
            this.elevatorL2NrDurations = new ArrayList();
            this.basementL2NrDurations = new ArrayList();
            this.elevatorScanLteDurations = new ArrayList();
            this.basementL2NrDurationsV3 = new ArrayList();
            this.basementFDD2TDDDurationsV3 = new ArrayList();
            this.basementScanLteDurations = new ArrayList();
            this.basementL2NrTriggerReasonV3 = new ArrayList();
            this.basementFDD2TDDTriggerReasonV3 = new ArrayList();
            this.elevatorPreloadPkgName = new ArrayList();
            this.mTelephonyCallBack = null;
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                this.startScanLteTime[i2] = -1;
                this.startL2NrTime[i2] = -1;
                this.startCoverageTimeV3[i2] = -1;
            }
            registerTelephonyCallBack();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordStartL2NrTime(long j, int i) {
            ElevatorModeController.this.log("recordStartL2NrTime time = " + j + ",phoneId = " + i);
            long[] jArr = this.startL2NrTime;
            if (jArr[i] != -1) {
                return;
            }
            jArr[i] = j;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordStartScanLteTime(long j, int i) {
            ElevatorModeController.this.log("recordStartScanLteTime time = " + j + ",phoneId = " + i);
            long[] jArr = this.startScanLteTime;
            if (jArr[i] != -1) {
                return;
            }
            jArr[i] = j;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordEndL2NrTime(long j, int i) {
            long j2 = this.startL2NrTime[i];
            if (j2 == -1) {
                return;
            }
            long j3 = j == -1 ? -1L : (j - j2) / 1000;
            ElevatorModeController.this.log("recordEndL2NrTime duraiont = " + j3 + " scene = " + ElevatorModeController.this.mScene);
            if (ElevatorModeController.this.mScene == -1) {
                this.basementL2NrDurations.add(Long.valueOf(j3));
            } else {
                this.elevatorL2NrDurations.add(Long.valueOf(j3));
            }
            this.startL2NrTime[i] = -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordEndScanLteTime(long j, int i) {
            long j2 = this.startScanLteTime[i];
            if (j2 == -1) {
                return;
            }
            long j3 = j == -1 ? -1L : (j - j2) / 1000;
            ElevatorModeController.this.log("scanLteDuration duraiont = " + j3 + " scene = " + ElevatorModeController.this.mScene);
            if (ElevatorModeController.this.mScene == -1) {
                this.basementScanLteDurations.add(Long.valueOf(j3));
            } else {
                this.elevatorScanLteDurations.add(Long.valueOf(j3));
            }
            this.startScanLteTime[i] = -1;
        }

        public void serviceStateChange(int i, ServiceState serviceState) {
            if (serviceState == null || !MiuiPhoneUtils.isValidatePhoneId(i)) {
                return;
            }
            int voiceNetworkType = serviceState.getVoiceNetworkType();
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (Utils.isLte(voiceNetworkType) || Utils.isNr(voiceNetworkType)) {
                recordEndScanLteTime(jElapsedRealtime, i);
            }
            if (Utils.isNr(voiceNetworkType)) {
                recordEndL2NrTime(jElapsedRealtime, i);
            }
            String bandByPhoneId = ElevatorModeController.this.getBandByPhoneId(i);
            if (MiuiPhoneUtils.isBasementV3Supported() && Utils.isNr(voiceNetworkType)) {
                ElevatorModeController elevatorModeController = ElevatorModeController.this;
                int i2 = elevatorModeController.mCoverageTypeV3;
                if (i2 == 1 || !(i2 != 2 || elevatorModeController.mFddSet.isEmpty() || ElevatorModeController.this.mFddSet.contains(bandByPhoneId))) {
                    recordEndOutCoverageStatetimeV3(jElapsedRealtime, i);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordStartOutCoverageStatetimeV3(int i, String str) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (this.startCoverageTimeV3[i] != -1) {
                recordAbnormalEndOutCoverageStatetimeV3(i, -1L);
            }
            this.startCoverageTimeV3[i] = jElapsedRealtime;
            int i2 = ElevatorModeController.this.mCoverageTypeV3;
            if (i2 == 1) {
                this.basementL2NrTriggerReasonV3.add(str);
            } else if (i2 == 2) {
                this.basementFDD2TDDTriggerReasonV3.add(str);
            }
            ElevatorModeController.this.log("recordStartOutCoverageStatetimeV3");
        }

        private void recordEndOutCoverageStatetimeV3(long j, int i) {
            long j2 = this.startCoverageTimeV3[i];
            if (j2 == -1) {
                return;
            }
            long j3 = j == -1 ? -1L : (j - j2) / 1000;
            int i2 = ElevatorModeController.this.mCoverageTypeV3;
            if (i2 == 1) {
                this.basementL2NrDurationsV3.add(Long.valueOf(j3));
            } else if (i2 == 2) {
                this.basementFDD2TDDDurationsV3.add(Long.valueOf(j3));
            }
            ElevatorModeController.this.log("recordEndOutCoverageStatetimeV3 duraiont = " + j3 + " mCoverageTypeV3 = " + ElevatorModeController.this.mCoverageTypeV3);
            this.startCoverageTimeV3[i] = -1;
            ElevatorModeController.this.mCoverageTypeV3 = -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordAbnormalEndOutCoverageStatetimeV3(int i, long j) {
            int i2 = ElevatorModeController.this.mCoverageTypeV3;
            if (i2 == 1) {
                this.basementL2NrDurationsV3.add(Long.valueOf(j));
            } else if (i2 == 2) {
                this.basementFDD2TDDDurationsV3.add(Long.valueOf(j));
            }
            ElevatorModeController.this.log("recordAbnormalEndOutCoverageStatetimeV3 = " + ElevatorModeController.this.mCoverageTypeV3);
            this.startCoverageTimeV3[i] = -1;
            ElevatorModeController.this.mCoverageTypeV3 = -1;
        }

        private void registerTelephonyCallBack() {
            if (this.mTelephonyCallBack == null) {
                ElevatorModeController.this.log("registerTelephonyCallBack");
                TelephonyCallBack telephonyCallBack = ElevatorModeController.this.new TelephonyCallBack();
                this.mTelephonyCallBack = telephonyCallBack;
                TelephonyStatAdapter.registerCallBack(telephonyCallBack);
            }
        }

        public void reportStatistics() {
            HashMap map = new HashMap();
            if (!this.elevatorL2NrDurations.isEmpty()) {
                map.put("elevator_l2nr_durations", new ArrayList(this.elevatorL2NrDurations));
            }
            if (!this.elevatorScanLteDurations.isEmpty()) {
                map.put("elevator_scan_lte_durations", new ArrayList(this.elevatorScanLteDurations));
            }
            if (!this.basementL2NrTriggerReasonV3.isEmpty()) {
                map.put("basement_l2nr_trigger_reason_v3", new ArrayList(this.basementL2NrTriggerReasonV3));
            }
            if (!this.basementL2NrDurations.isEmpty()) {
                map.put("basement_l2nr_durations", new ArrayList(this.basementL2NrDurations));
            }
            if (!this.basementL2NrDurationsV3.isEmpty()) {
                map.put("basement_l2nr_durations_v3", new ArrayList(this.basementL2NrDurationsV3));
                map.put("basement_l2nr_frequency_v3", Integer.valueOf(this.basementL2NrDurationsV3.size()));
            }
            if (!this.basementFDD2TDDTriggerReasonV3.isEmpty()) {
                map.put("basement_FDD2TDD_trigger_reason_v3", new ArrayList(this.basementFDD2TDDTriggerReasonV3));
            }
            if (!this.basementFDD2TDDDurationsV3.isEmpty()) {
                map.put("basement_FDD2TDD_durations_v3", new ArrayList(this.basementFDD2TDDDurationsV3));
                map.put("basement_FDD2TDD_frequency_v3", Integer.valueOf(this.basementFDD2TDDDurationsV3.size()));
            }
            if (!this.basementScanLteDurations.isEmpty()) {
                map.put("basement_scan_lte_durations", new ArrayList(this.basementScanLteDurations));
            }
            if (!this.elevatorPreloadPkgName.isEmpty()) {
                map.put("elevator_preload_package_name", new ArrayList(this.elevatorPreloadPkgName));
            }
            if (ElevatorModeController.this.mElevatorPreloadVerifyFail != 0) {
                map.put("elevator_preload_verify_fail", Integer.valueOf(ElevatorModeController.this.mElevatorPreloadVerifyFail));
            }
            ElevatorModeController.this.log("reportStatistics data = " + map);
            if (map.isEmpty()) {
                return;
            }
            if (ElevatorModeController.this.mTelephonyManager != null) {
                String[] strArr = new String[MiuiPhoneUtils.PHONE_COUNT];
                for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                    strArr[i] = getAvailableOperatorNumeric(i);
                }
                map.put("plmn", Arrays.toString(strArr));
            }
            MiStatInterfaceUtil.recordObjectEvent("elevator", "event_elevator_mode_statistic", map);
        }

        private String getAvailableOperatorNumeric(int i) {
            String networkOperatorForPhone = ElevatorModeController.this.mTelephonyManager.getNetworkOperatorForPhone(i);
            return TextUtils.isEmpty(networkOperatorForPhone) ? ElevatorModeController.this.mTelephonyManager.getSimOperatorNumericForPhone(i) : networkOperatorForPhone;
        }

        public void clear() {
            this.elevatorL2NrDurations.clear();
            this.elevatorScanLteDurations.clear();
            this.basementL2NrDurations.clear();
            this.basementL2NrDurationsV3.clear();
            this.basementFDD2TDDDurationsV3.clear();
            this.basementScanLteDurations.clear();
            this.elevatorPreloadPkgName.clear();
            ElevatorModeController.this.mScene = Integer.MIN_VALUE;
            ElevatorModeController.this.mElevatorPreloadVerifyFail = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean listenServiceState() {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                if (this.startL2NrTime[i] != -1 || this.startScanLteTime[i] != -1 || this.startCoverageTimeV3[i] != -1) {
                    return true;
                }
            }
            return false;
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            ElevatorModeController.this.mHandler.obtainMessage(110, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            ElevatorModeController.this.mHandler.obtainMessage(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            if (ElevatorModeController.this.mIsWifiConnected) {
                return;
            }
            ElevatorModeController.this.mHandler.obtainMessage(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, i, 0, cellLocation).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        log("processServiceStateChangeEvent phoneId=" + i);
        if (MiuiPhoneUtils.isValidatePhoneId(i) && serviceState != null && !serviceState.getIsManualSelection() && this.mIsInElevator && this.mDdsPhoneId == i) {
            this.mIsOosElevator = isPoorOosForElevator(serviceState, i);
            log("processServiceStateChangeEvent phoneId : " + i + ", mIsOosElevator" + this.mIsOosElevator);
        }
    }

    private boolean isPoorOosForElevator(ServiceState serviceState, int i) {
        if (serviceState == null || this.mNetworkMetricsController == null) {
            return false;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        boolean zIsLteNrNwMode = isLteNrNwMode(i);
        boolean zIsLteNrNwType = this.mNetworkMetricsController.isLteNrNwType(dataNetworkType);
        boolean zIsDataRegistered = MiuiPhoneUtils.isDataRegistered(i);
        log("isPoorOosForElevator isDataRegistered:" + zIsDataRegistered + " curRat: " + dataNetworkType + " isLteNrNwType:" + zIsLteNrNwType + " isLteNrNwMode: " + zIsLteNrNwMode);
        return (zIsLteNrNwMode || this.VDBG) && !(zIsDataRegistered && zIsLteNrNwType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSignalStrengthEvent(int i, SignalStrength signalStrength) {
        log("processSignalStrengthEvent phoneId : " + i + ", mDdsPhoneId" + this.mDdsPhoneId);
        if (MiuiPhoneUtils.isValidatePhoneId(i) && signalStrength != null && this.mIsInElevator && this.mDdsPhoneId == i) {
            this.mIsPoorSignalElevator = isPoorSignalForElevator(signalStrength);
            log("processSignalStrengthEvent phoneId : " + i + ", mIsPoorSignalElevator : " + this.mIsPoorSignalElevator);
        }
    }

    private boolean isPoorSignalForElevator(SignalStrength signalStrength) {
        if (signalStrength == null) {
            return false;
        }
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                int iInRangeOrUnavailable = inRangeOrUnavailable(((CellSignalStrengthLte) cellSignalStrength).getRsrp(), -140, -43);
                log("isPoorSignalForElevator lteRsrp=" + iInRangeOrUnavailable);
                if (iInRangeOrUnavailable <= -115) {
                    return true;
                }
            } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                int iInRangeOrUnavailable2 = inRangeOrUnavailable(((CellSignalStrengthNr) cellSignalStrength).getSsRsrp(), -140, -44);
                log("isPoorSignalForElevator nrRsrp=" + iInRangeOrUnavailable2);
                if (iInRangeOrUnavailable2 <= -110) {
                    return true;
                }
            } else {
                continue;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void recordOutElevatorCell() {
        CellIdentity currentCellIdentity;
        try {
            if (this.mPhones != null && MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
                Phone[] phoneArr = this.mPhones;
                int i = this.mDdsPhoneId;
                Phone phone = phoneArr[i];
                if (phone != null) {
                    boolean zIsDataRegistered = MiuiPhoneUtils.isDataRegistered(i);
                    if (phone != null && (currentCellIdentity = phone.getCurrentCellIdentity()) != null && phone.getServiceState() != null && zIsDataRegistered) {
                        if (currentCellIdentity instanceof CellIdentityLte) {
                            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
                            this.mOutElevatorRecordTime = System.currentTimeMillis();
                            this.mOutElevatorRecordCell = cellIdentityLte.getPlmn() + String.valueOf(cellIdentityLte.getCi()) + String.valueOf(cellIdentityLte.getPci());
                            StringBuilder sb = new StringBuilder("recordOutElevatorCell CellIdentityLte :");
                            sb.append(this.mOutElevatorRecordCell);
                            log(sb.toString());
                        } else if (currentCellIdentity instanceof CellIdentityNr) {
                            CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
                            this.mOutElevatorRecordTime = System.currentTimeMillis();
                            this.mOutElevatorRecordCell = cellIdentityNr.getPlmn() + String.valueOf(cellIdentityNr.getNci()) + String.valueOf(cellIdentityNr.getPci());
                            StringBuilder sb2 = new StringBuilder("recordOutElevatorCell CellIdentityNr :");
                            sb2.append(this.mOutElevatorRecordCell);
                            log(sb2.toString());
                        }
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateCellInfo(int i, CellLocation cellLocation) {
        if (cellLocation != null) {
            try {
                if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                    String cellInfoKey = getCellInfoKey(i);
                    if (!TextUtils.isEmpty(this.mOutElevatorRecordCell) && !TextUtils.isEmpty(cellInfoKey) && !cellInfoKey.equals(this.mOutElevatorRecordCell)) {
                        int iCurrentTimeMillis = (int) (System.currentTimeMillis() - this.mOutElevatorRecordTime);
                        log("updateCellInfo cellInfo :" + cellInfoKey + " mOutElevatorRecordCell: " + this.mOutElevatorRecordCell + " cellRegDuration " + iCurrentTimeMillis);
                        boolean zContainsKey = this.outElevatorCellLru.containsKey(this.mOutElevatorRecordCell);
                        LRU lru = this.outElevatorCellLru;
                        if (zContainsKey) {
                            String str = this.mOutElevatorRecordCell;
                            lru.put(str, Integer.valueOf((((Integer) lru.get(str)).intValue() + iCurrentTimeMillis) / 2));
                        } else {
                            lru.put(this.mOutElevatorRecordCell, Integer.valueOf(iCurrentTimeMillis));
                        }
                        this.mOutElevatorRecordCell = "";
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public synchronized void predictNextCellStatus(int i, CellLocation cellLocation) {
        int i2;
        if (cellLocation != null) {
            if (MiuiPhoneUtils.isValidatePhoneId(i) && i == (i2 = this.mDdsPhoneId)) {
                String cellInfoKey = getCellInfoKey(i2);
                if (TextUtils.isEmpty(cellInfoKey)) {
                    return;
                }
                log("predictNextCellStatus CellIdentityLte :" + cellInfoKey);
                if (this.outElevatorCellLru.containsKey(cellInfoKey) && System.currentTimeMillis() - this.mLastOutElevatorTime > 120000) {
                    if (this.mHandler.hasMessages(107)) {
                        log("predictNextCellStatus : during cell verify!");
                        return;
                    }
                    int iIntValue = ((Integer) this.outElevatorCellLru.get(cellInfoKey)).intValue() / 2;
                    this.mLastPredictCellInfo = cellInfoKey;
                    ElevatorModeHandler elevatorModeHandler = this.mHandler;
                    elevatorModeHandler.sendMessageDelayed(elevatorModeHandler.obtainMessage(112, Integer.valueOf(iIntValue / 1000)), iIntValue);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCellInfoKey(int i) {
        Phone phone;
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || (phone = this.mPhones[i]) == null || phone.getCurrentCellIdentity() == null || !MiuiPhoneUtils.isDataRegistered(i)) {
            return "";
        }
        CellIdentity currentCellIdentity = this.mPhones[i].getCurrentCellIdentity();
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            return cellIdentityLte.getPlmn() + String.valueOf(cellIdentityLte.getCi()) + String.valueOf(cellIdentityLte.getPci());
        }
        if (!(currentCellIdentity instanceof CellIdentityNr)) {
            return "";
        }
        CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
        return cellIdentityNr.getPlmn() + String.valueOf(cellIdentityNr.getNci()) + String.valueOf(cellIdentityNr.getPci());
    }

    public void notify3rdAppOpt(int i) {
        String topAppName = MiuiPhoneUtils.getTopAppName();
        int iNextInt = new Random().nextInt(10) + i;
        if (isPreloadVideoWhiteList(topAppName)) {
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().videoAppsPolicyNotify(1, iNextInt, -1, VideoPreloadScene.ELEVATOR.getValue());
            }
            this.mElevatorModeStats.elevatorPreloadPkgName.add(topAppName);
        } else if (isResolutionVideoWhiteList(topAppName)) {
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().videoAppsPolicyNotify(2, iNextInt, -1, VideoPreloadScene.ELEVATOR.getValue());
            }
            this.mElevatorModeStats.elevatorPreloadPkgName.add(topAppName);
        }
    }

    private boolean isPreloadVideoWhiteList(String str) {
        if (mPreloadVideoWhiteList.size() == 0) {
            return false;
        }
        log("isPreloadVideoWhiteList topAppName:" + str);
        Iterator it = mPreloadVideoWhiteList.iterator();
        while (it.hasNext()) {
            if (str.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    private boolean isResolutionVideoWhiteList(String str) {
        if (mResolutionVideoWhiteList.size() == 0) {
            return false;
        }
        log("isResolutionVideoWhiteList topAppName:" + str);
        Iterator it = mResolutionVideoWhiteList.iterator();
        while (it.hasNext()) {
            if (str.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isColddownPeriod() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mStateChangeTime;
        return j != 0 && jCurrentTimeMillis - j < 10000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCommutingTime() {
        if (isWeekEnd(getCurrentCalendar(System.currentTimeMillis()))) {
            return false;
        }
        return currentIsMorningPeak() || currentIsEveningPeak();
    }

    private boolean isWeekEnd(Calendar calendar) {
        return calendar.get(7) == 7 || calendar.get(7) == 1;
    }

    public boolean currentIsMorningPeak() {
        int i = getCurrentTimes()[0];
        return i >= 7 && i <= 10;
    }

    public boolean currentIsEveningPeak() {
        int i = getCurrentTimes()[0];
        return i >= 18 && i < 22;
    }

    public int[] getCurrentTimes() {
        Calendar currentCalendar = getCurrentCalendar(System.currentTimeMillis());
        return new int[]{currentCalendar.get(11), currentCalendar.get(12)};
    }

    public Calendar getCurrentCalendar(long j) {
        Date date = new Date(j);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private boolean isAutoSelection(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return false;
        }
        ServiceState serviceState = phone.getServiceState();
        if (MiuiPhoneUtils.isValidatePhoneId(i) && serviceState != null && !serviceState.getIsManualSelection()) {
            return true;
        }
        log("isAutoSelection invalid params or manual network selection");
        return false;
    }

    private boolean isFrequencyDivisionDuplex(int i) {
        return !this.mFddSet.isEmpty() && this.mFddSet.contains(getBandByPhoneId(i));
    }

    private void initFddBand() {
        Set set = this.mFddSet;
        if (set != null) {
            set.add("N1");
            this.mFddSet.add("N2");
            this.mFddSet.add("N3");
            this.mFddSet.add("N5");
            this.mFddSet.add("N7");
            this.mFddSet.add("N8");
            this.mFddSet.add("N20");
            this.mFddSet.add("N25");
            this.mFddSet.add("N28");
            this.mFddSet.add("N66");
            this.mFddSet.add("N70");
            this.mFddSet.add("N71");
            this.mFddSet.add("N74");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBandByPhoneId(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return "";
        }
        ServiceState serviceState = phone.getServiceState();
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || serviceState == null) {
            log("getBandByPhoneId invalid params");
            return "";
        }
        String band = Utils.getBand(i, serviceState);
        log("getBandByPhoneId band=" + band);
        return band;
    }

    public class LRU extends LinkedHashMap {
        private int capacity;

        public LRU(int i, float f, boolean z) {
            super(i, f, z);
            this.capacity = i;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry entry) {
            return size() > this.capacity;
        }
    }
}
