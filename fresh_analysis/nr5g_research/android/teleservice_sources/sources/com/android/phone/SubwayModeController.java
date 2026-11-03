package com.android.phone;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.SettingsObserver;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.network.ModemNetworkPerception;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.NetworkSpeedTracker;
import com.android.phone.utils.VideoPreloadScene;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;
import miui.telephony.qms.DubiousCellInfoS;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.NrDubiousCellInfoS;
import miui.telephony.qms.PlmnIdS;

/* JADX INFO: loaded from: classes.dex */
public class SubwayModeController {
    protected static volatile SubwayModeController mInstance;
    private int mDdsPhoneId;
    private SubwayModeHandler mHandler;
    private boolean mIsAirPlaneMode;
    private boolean mIsMobileConnected;
    private boolean mIsScreenOn;
    private boolean mIsWifiConnected;
    private ModemNetworkPerception mModemNetworkPerception;
    private NetworkModeRecognition mNetworkModeRecognition;
    private ContentResolver mResolver;
    private SettingsObserver mSettingsObserver;
    private SubwayModeStats mSubwayModeStats;
    private HandlerThread mWorkThread;
    protected static final Map mActiveBand = new HashMap();
    private static Context mContext = null;
    private static TelephonyInfoCallBack mTelephonyInfoCallBack = null;
    private static Map mBandMappingScs = new HashMap();
    private static List mResolutionVideoWhiteList = new ArrayList();
    private static List mPreloadVideoWhiteList = new ArrayList();
    private static List mRuningWhiteList = new ArrayList();
    private static List mSignalThresholdList = new ArrayList();
    private static ModemNetworkPerceptionCallBack mModemNetworkPerceptionCallBack = null;
    private boolean VDBG = false;
    private int mSubwayState = 0;
    private boolean mInSubwayByStartStop = false;
    private ArrayList mNrDubiousCellInfoSList = new ArrayList();
    private ArrayList mLteDubiousCellInfoSList = new ArrayList();
    private int mCurrentRsrp = Integer.MAX_VALUE;
    private boolean mIsResolutionSignalMonitorStatus = false;
    private int mEnterMonitorLowerLimit = Integer.MAX_VALUE;
    private int mEnterMonitorUpperLimit = Integer.MAX_VALUE;
    private int mExitMonitorThreshold = Integer.MAX_VALUE;
    private int mEnableResolutionThreshold = Integer.MAX_VALUE;
    private int mDisableResolutionThreshold = Integer.MAX_VALUE;
    private int mSnsSubwayState = Integer.MAX_VALUE;
    private boolean mIsResolutionAdjust = false;
    private boolean mIsPreloadSignalMonitorStatus = false;
    private int mEnablePreloadThreshold = -110;
    private AudioManager mHstAudioManager = null;
    private boolean mIsFirstVideoPreloaded = false;
    private boolean mIsHighSpeedTrain = false;
    private boolean mIsLongTermFreezeRecovery = false;
    private boolean mIsStrongFieldToPreload = false;
    private long mHstLastPreloadTime = 0;
    private Set mModemSubwayPlaformTimes = new HashSet();
    private long mSubwayStartTime = 0;
    private long mSubwayStopTime = 0;
    private int mModemSubwayPlatformCount = 0;
    private int mModemSubwayPlatformCorrectCount = 0;
    private int mModemSubwayPlatformMissCount = 0;
    private int mSubwayRealPlatformCount = 0;
    private boolean mInSubwayByEnterExit = false;
    private SensorStateCallBack mSensorStateCallBack = null;
    private SnsSubwayStateCallBack mSnsSubwayStateCallBack = null;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.SubwayModeController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                if (!MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) || SubwayModeController.this.mDdsPhoneId == defaultDataSlotId) {
                    return;
                }
                SubwayModeController.this.mDdsPhoneId = defaultDataSlotId;
                SubwayModeController.this.mCurrentRsrp = Integer.MAX_VALUE;
                SubwayModeController.this.mHandler.sendEmptyMessage(5);
                return;
            }
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                SubwayModeController.this.mHandler.obtainMessage(6, intent).sendToTarget();
                return;
            }
            if (action.equals("com.android.phone.intent.action.ACTION_CLOUD_RESOLUTION_VIDEO_APPS_LIST_DONE")) {
                SubwayModeController.this.log("onReceive ACTION_CLOUD_RESOLUTION_VIDEO_APPS_LIST_DONE");
                SubwayModeController.this.initResolutionVideoWhiteList();
                return;
            }
            if (action.equals("com.android.phone.intent.action.ACTION_CLOUD_SUBWAY_MODE_SIGNAL_THRESHOLD_DONE")) {
                SubwayModeController.this.log("onReceive ACTION_CLOUD_SUBWAY_MODE_SIGNAL_THRESHOLD_DONE");
                SubwayModeController.this.mHandler.sendEmptyMessage(11);
                return;
            }
            if (action.equals("com.android.phone.intent.action.ACTION_CLOUD_PRELOAD_VIDEO_APPS_LIST_DONE")) {
                SubwayModeController.this.log("onReceive ACTION_CLOUD_PRELOAD_VIDEO_APPS_LIST_DONE");
                SubwayModeController.this.initPreloadVideoWhiteList();
                return;
            }
            if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                SubwayModeController.this.mHandler.obtainMessage(10).sendToTarget();
                return;
            }
            if (action.equals("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED")) {
                SubwayModeController.this.mHandler.obtainMessage(14, intent).sendToTarget();
                return;
            }
            if (action.equals("miui.intent.action.VIDEO_INFO")) {
                SubwayModeController.this.mHandler.obtainMessage(15, intent).sendToTarget();
            } else if (action.equals("subwayModeController.intent.debugall")) {
                SubwayModeController.this.mHandler.obtainMessage(100, intent).sendToTarget();
            } else if (action.equals("com.android.phone.intent.action.PROBLEMATIC_CELL_AVOID_NOTI_ACTION")) {
                SubwayModeController.this.mHandler.obtainMessage(13, intent).sendToTarget();
            }
        }
    };
    private final BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.SubwayModeController.2
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                if (!displayEvent.isDefaultDisplay() || SubwayModeController.this.mHandler == null) {
                    return;
                }
                SubwayModeController.this.mHandler.obtainMessage(3, displayEvent).sendToTarget();
            }
        }
    };

    private int inRangeOrUnavailableLteRsrp(int i) {
        if (i < -140 || i > -43) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    private int inRangeOrUnavailableNrRsrp(int i) {
        if (i < -140 || i > -44) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    public static SubwayModeController make() {
        if (mInstance == null) {
            synchronized (SubwayModeController.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new SubwayModeController();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    private SubwayModeController() {
        this.mWorkThread = null;
        this.mHandler = null;
        this.mNetworkModeRecognition = null;
        this.mDdsPhoneId = -1;
        this.mModemNetworkPerception = null;
        log("SubwayModeController init");
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        mContext = phoneGlobals;
        this.mResolver = phoneGlobals.getContentResolver();
        this.mNetworkModeRecognition = NetworkModeRecognition.getInstance();
        HandlerThread handlerThread = new HandlerThread("SubwayModeController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mSubwayModeStats = new SubwayModeStats();
        this.mIsAirPlaneMode = isAirPlaneModeOn();
        this.mDdsPhoneId = DefaultSimManager.getDefaultDataSlotId();
        if (MiuiPhoneUtils.isModemNetworkPerceptionSupported()) {
            this.mModemNetworkPerception = ModemNetworkPerception.getInstance();
        }
        this.mHandler = new SubwayModeHandler(this.mWorkThread.getLooper());
        this.mSettingsObserver = new SettingsObserver(mContext, this.mHandler);
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        initResolutionVideoWhiteList();
        initSignalThreshold();
        initPreloadVideoWhiteList();
        initBandData();
        initBandMappingScs();
        if (MiuiPhoneUtils.isSubwayEnhancedSupported()) {
            regSensorStateCallBack();
            regSnsSubwayStateCallBack();
        }
        registerSettingsCallbacks();
        registerReceiver();
    }

    private void registerSettingsCallbacks() {
        this.mSettingsObserver.observe(Settings.Global.getUriFor("network_mode_state"), 2);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.ACTION_CLOUD_RESOLUTION_VIDEO_APPS_LIST_DONE");
        intentFilter.addAction("com.android.phone.intent.action.ACTION_CLOUD_SUBWAY_MODE_SIGNAL_THRESHOLD_DONE");
        intentFilter.addAction("com.android.phone.intent.action.ACTION_CLOUD_PRELOAD_VIDEO_APPS_LIST_DONE");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.android.phone.intent.action.PROBLEMATIC_CELL_AVOID_NOTI_ACTION");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("miui.intent.action.VIDEO_INFO");
        intentFilter.addAction("subwayModeController.intent.debugall");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    class SubwayModeHandler extends Handler {
        SubwayModeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SubwayModeController.this.log("msg.what = " + message.what);
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 1:
                        SubwayModeController.this.initAction();
                        break;
                    case 2:
                        SubwayModeController.this.processSubwayStateChangeEvent();
                        break;
                    case 3:
                        boolean zIsScreenOn = ((DeviceStateEvent.DisplayEvent) message.obj).isScreenOn();
                        if (SubwayModeController.this.VDBG) {
                            SubwayModeController.this.log("onDisplayChanged isScreenOn=" + zIsScreenOn + ",mIsScreenOn=" + SubwayModeController.this.mIsScreenOn);
                        }
                        if (zIsScreenOn != SubwayModeController.this.mIsScreenOn) {
                            SubwayModeController.this.mIsScreenOn = zIsScreenOn;
                            SubwayModeController.this.processScreenStateChangeEvent();
                            break;
                        }
                        break;
                    case 4:
                        SubwayModeController.this.processSignalStrengthChangeEvent(message.arg1, (SignalStrength) message.obj);
                        break;
                    case 5:
                        SubwayModeController.this.processDefaultDataChanged();
                        break;
                    case 6:
                        SubwayModeController.this.processNetworkStateChanged((Intent) message.obj);
                        break;
                    case 7:
                        SubwayModeController.this.exitResolutionSignalMonitorStatus(ExitCode.TIMEOUT);
                        break;
                    case 8:
                        SubwayModeController.this.processSignalGoodLineger();
                        break;
                    case 9:
                        SubwayModeController.this.processSubwayStateUpdateLinger();
                        break;
                    case 10:
                        SubwayModeController subwayModeController = SubwayModeController.this;
                        subwayModeController.mIsAirPlaneMode = subwayModeController.isAirPlaneModeOn();
                        break;
                    case 11:
                        SubwayModeController.this.initSignalThreshold();
                        break;
                    case 12:
                        SubwayModeController.this.exitPreloadSignalMonitorStatus(ExitCode.TIMEOUT);
                        break;
                    case 13:
                        if (MiuiPhoneUtils.isSnsOptimizationSupported() && SubwayModeController.this.isCommonSceneForProblematicCell()) {
                            SubwayModeController.this.processProblematicCellAvoidEvent((Intent) message.obj);
                            break;
                        }
                        break;
                    case 14:
                        SubwayModeController.this.processHstStateChangeEvent((Intent) message.obj);
                        break;
                    case 15:
                        if (SubwayModeController.this.mIsHighSpeedTrain) {
                            SubwayModeController.this.processMiuiVideoInfoEvent((Intent) message.obj);
                        }
                        break;
                    case 16:
                        SubwayModeController.this.processMiuiVideoInfoDelayEvent();
                        break;
                    case 17:
                        SubwayModeController.this.processSaSubwayPlatformRecognizeEvent(message.arg1, message.arg2);
                        break;
                }
                return;
            }
            SubwayModeController.this.VDBG = true;
            Intent intent = (Intent) message.obj;
            SubwayModeController.this.mEnterMonitorLowerLimit = intent.getIntExtra("monitor_lower_limit_rsrp", -108);
            SubwayModeController.this.mEnterMonitorUpperLimit = intent.getIntExtra("monitor_upper_limit_rsrp", -100);
            SubwayModeController.this.mExitMonitorThreshold = intent.getIntExtra("exit_monitor_rsrp", -90);
            SubwayModeController.this.mEnableResolutionThreshold = intent.getIntExtra("enable_resolution_rsrp", -110);
            SubwayModeController.this.mDisableResolutionThreshold = intent.getIntExtra("disable_resolution_rsrp", -90);
            SubwayModeController.this.mEnablePreloadThreshold = intent.getIntExtra("enable_preload_rsrp", -110);
            SubwayModeController.this.log("EVENT_DEBUG_ALL, mEnterMonitorLowerLimit=" + SubwayModeController.this.mEnterMonitorLowerLimit + ", mEnterMonitorUpperLimit=" + SubwayModeController.this.mEnterMonitorUpperLimit + ", mExitMonitorThreshold=" + SubwayModeController.this.mExitMonitorThreshold + ", mEnableResolutionThreshold=" + SubwayModeController.this.mEnableResolutionThreshold + ", mDisableResolutionThreshold=" + SubwayModeController.this.mDisableResolutionThreshold + ", mEnablePreloadThreshold=" + SubwayModeController.this.mEnablePreloadThreshold);
        }
    }

    private boolean isCommonScene() {
        boolean z = !this.mIsAirPlaneMode && this.mIsScreenOn && this.mIsMobileConnected;
        if (!z) {
            log("isCommonScene: mIsAirPlaneMode=" + this.mIsAirPlaneMode + "mIsScreenOn=" + this.mIsScreenOn + ", mIsMobileConnected=" + this.mIsMobileConnected);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(this.mResolver, "airplane_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processScreenStateChangeEvent() {
        logv("processScreenStateChangeEvent mIsScreenOn=" + this.mIsScreenOn);
        if (!this.mIsScreenOn) {
            if (this.mIsResolutionSignalMonitorStatus) {
                exitResolutionSignalMonitorStatus(ExitCode.SCREEN_OFF);
            }
        } else if (this.mIsResolutionAdjust && isCommonScene() && isInRangeDisableResolution(this.mCurrentRsrp)) {
            exitResolutionAdjust(OptimizeExitCode.SCREEN_AND_SIGNAL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSignalStrengthChangeEvent(int i, SignalStrength signalStrength) {
        if (!isDdsPhoneId(i) || signalStrength == null) {
            logv("is not dds phoneId or signal is null");
            return;
        }
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                this.mCurrentRsrp = inRangeOrUnavailableLteRsrp(((CellSignalStrengthLte) cellSignalStrength).getRsrp());
                log("updateSignalInfo lteRsrp=" + this.mCurrentRsrp + ", phoneId=" + i);
            } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                this.mCurrentRsrp = inRangeOrUnavailableNrRsrp(((CellSignalStrengthNr) cellSignalStrength).getSsRsrp());
                log("updateSignalInfo nrRsrp=" + this.mCurrentRsrp + ", phoneId=" + i);
            }
        }
        if (!this.mIsResolutionSignalMonitorStatus) {
            if (!this.mIsResolutionAdjust && isLteNrNwTypeByPhoneId(i) && isInRangeEnterRsrpMonitor(this.mCurrentRsrp) && isCommonScene()) {
                enterResolutionSignalMonitorStatus(i);
            }
        } else if (!isLteNrNwTypeByPhoneId(i)) {
            exitResolutionSignalMonitorStatus(ExitCode.INVALID_RAT);
        } else if (isInRangeExitRsrpMonitor(this.mCurrentRsrp)) {
            exitResolutionSignalMonitorStatus(ExitCode.SIGNAL_GOOD_ENOUGH);
        } else if (isInRangeEnableResolution(this.mCurrentRsrp) && isCommonScene()) {
            exitResolutionSignalMonitorStatus(ExitCode.NORMAL_EXIT);
            if (isResolutionVideoWhiteList()) {
                enterResolutionAdjust(new Random().nextInt(10) + 1);
            }
        }
        if (this.mIsResolutionAdjust && isInRangeDisableResolution(this.mCurrentRsrp)) {
            startMonitorTimerForSignalGood();
        }
        if (this.mIsPreloadSignalMonitorStatus && isInRangeEnableProload(this.mCurrentRsrp) && isCommonScene() && isPreloadVideoWhiteList()) {
            videoAppsPolicyNotify(1, -1, -1, VideoPreloadScene.SUBWAY_SIGNAL_MODEL.getValue());
            exitPreloadSignalMonitorStatus(ExitCode.NORMAL_EXIT);
        }
    }

    private void enterResolutionSignalMonitorStatus(int i) {
        if (this.mIsResolutionSignalMonitorStatus) {
            return;
        }
        log("enterResolutionSignalMonitorStatus");
        this.mIsResolutionSignalMonitorStatus = true;
        startMonitorTimerForSignal(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitResolutionSignalMonitorStatus(ExitCode exitCode) {
        if (this.mIsResolutionSignalMonitorStatus) {
            log("exitResolutionSignalMonitorStatus code=" + exitCode);
            stopMonitorTimerForSignal();
            this.mIsResolutionSignalMonitorStatus = false;
        }
    }

    private void startMonitorTimerForSignal(int i) {
        logv("startMonitorTimerForSignal");
        stopMonitorTimerForSignal();
        SubwayModeHandler subwayModeHandler = this.mHandler;
        subwayModeHandler.sendMessageDelayed(subwayModeHandler.obtainMessage(7, Integer.valueOf(i)), 180000L);
    }

    private void stopMonitorTimerForSignal() {
        if (this.mHandler.hasMessages(7)) {
            log("stopMonitorTimerForSignal");
            this.mHandler.removeMessages(7);
        }
    }

    private void enterPreloadSignalMonitorStatus(int i) {
        log("enterPreloadSignalMonitorStatus");
        this.mIsPreloadSignalMonitorStatus = true;
        startPreloadMonitorTimerForSignal(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitPreloadSignalMonitorStatus(ExitCode exitCode) {
        if (this.mIsPreloadSignalMonitorStatus) {
            log("exitPreloadSignalMonitorStatus code=" + exitCode);
            stopPreloadMonitorTimerForSignal();
            this.mIsPreloadSignalMonitorStatus = false;
        }
    }

    private void startPreloadMonitorTimerForSignal(int i) {
        logv("startPreloadMonitorTimerForSignal");
        stopPreloadMonitorTimerForSignal();
        SubwayModeHandler subwayModeHandler = this.mHandler;
        subwayModeHandler.sendMessageDelayed(subwayModeHandler.obtainMessage(12, Integer.valueOf(i)), 240000L);
    }

    private void stopPreloadMonitorTimerForSignal() {
        if (this.mHandler.hasMessages(12)) {
            log("stopPreloadMonitorTimerForSignal");
            this.mHandler.removeMessages(12);
        }
    }

    private void startMonitorTimerForSignalGood() {
        logv("startMonitorTimerForSignalGood");
        stopMonitorTimerForSignalGood();
        SubwayModeHandler subwayModeHandler = this.mHandler;
        subwayModeHandler.sendMessageDelayed(subwayModeHandler.obtainMessage(8), MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    private void stopMonitorTimerForSignalGood() {
        if (this.mHandler.hasMessages(8)) {
            log("stopMonitorTimerForSignalGood");
            this.mHandler.removeMessages(8);
        }
    }

    private void startMonitorTimerForSubwayUpdate() {
        logv("startMonitorTimerForSubwayUpdate");
        stopMonitorTimerForSubwayUpdate();
        SubwayModeHandler subwayModeHandler = this.mHandler;
        subwayModeHandler.sendMessageDelayed(subwayModeHandler.obtainMessage(9), 300000L);
    }

    private void stopMonitorTimerForSubwayUpdate() {
        if (this.mHandler.hasMessages(9)) {
            log("stopMonitorTimerForSubwayUpdate");
            this.mHandler.removeMessages(9);
        }
    }

    private boolean isInRangeEnableProload(int i) {
        int i2;
        return (i == Integer.MAX_VALUE || (i2 = this.mEnablePreloadThreshold) == Integer.MAX_VALUE || i > i2) ? false : true;
    }

    private boolean isInRangeEnableResolution(int i) {
        int i2;
        return (i == Integer.MAX_VALUE || (i2 = this.mEnableResolutionThreshold) == Integer.MAX_VALUE || i > i2) ? false : true;
    }

    private boolean isInRangeDisableResolution(int i) {
        int i2;
        return (i == Integer.MAX_VALUE || (i2 = this.mDisableResolutionThreshold) == Integer.MAX_VALUE || i <= i2) ? false : true;
    }

    private boolean isInRangeExitRsrpMonitor(int i) {
        int i2;
        return (i == Integer.MAX_VALUE || (i2 = this.mExitMonitorThreshold) == Integer.MAX_VALUE || i <= i2) ? false : true;
    }

    private boolean isInRangeEnterRsrpMonitor(int i) {
        int i2;
        int i3;
        return (i == Integer.MAX_VALUE || (i2 = this.mEnterMonitorUpperLimit) == Integer.MAX_VALUE || (i3 = this.mEnterMonitorLowerLimit) == Integer.MAX_VALUE || i > i2 || i < i3) ? false : true;
    }

    private boolean isLteNrNwTypeByPhoneId(int i) {
        boolean z = false;
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            return false;
        }
        ServiceState serviceState = MiuiPhoneUtils.getPhone(i).getServiceState();
        if (serviceState != null) {
            int dataNetworkType = serviceState.getDataNetworkType();
            if (MiuiPhoneUtils.isSimReady(i) && isLteNrNwType(dataNetworkType)) {
                z = true;
            }
        }
        logv("isLteNrNwTypeByPhoneId rst=" + z);
        return z;
    }

    private boolean isLteNrNwType(int i) {
        return Utils.isLte(i) || Utils.isNr(i);
    }

    private boolean isDdsPhoneId(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            return false;
        }
        log("mDdsPhoneId = " + this.mDdsPhoneId + ", phoneId=" + i);
        return i == this.mDdsPhoneId;
    }

    private void enterResolutionAdjust(int i) {
        if (this.mIsResolutionAdjust) {
            return;
        }
        log("enterResolutionAdjust");
        this.mIsResolutionAdjust = true;
        videoAppsPolicyNotify(2, i, -1, VideoPreloadScene.SUBWAY_SIGNAL_MODEL.getValue());
    }

    private void exitResolutionAdjust(OptimizeExitCode optimizeExitCode) {
        if (this.mIsResolutionAdjust) {
            log("exitResolutionAdjust code=" + optimizeExitCode);
            this.mIsResolutionAdjust = false;
            videoAppsPolicyNotify(2, -1, -1, VideoPreloadScene.SUBWAY_SIGNAL_MODEL.getValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSubwayStateUpdateLinger() {
        log("processSubwayStateUpdateLinger mSubwayState=" + this.mSubwayState);
        exitResolutionAdjust(OptimizeExitCode.EXIT_SUBWAY);
        exitResolutionSignalMonitorStatus(ExitCode.EXIT_SUBWAY);
        unregisterTelephonyInfoCallBack();
        unregisterScreenState();
        unregisterModemNetworkPerception();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSignalGoodLineger() {
        if (this.mIsResolutionAdjust && isInRangeDisableResolution(this.mCurrentRsrp)) {
            exitResolutionAdjust(OptimizeExitCode.SIGNAL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDefaultDataChanged() {
        log("processDefaultDataChanged cur ddsPhonId=" + this.mDdsPhoneId);
        exitResolutionSignalMonitorStatus(ExitCode.DDS_CHANGE);
        exitResolutionAdjust(OptimizeExitCode.DDS_CHANGED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNetworkStateChanged(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null) {
            return;
        }
        NetworkInfo.State state = networkInfo.getState();
        NetworkInfo.State state2 = NetworkInfo.State.CONNECTED;
        if (state == state2 || state == NetworkInfo.State.DISCONNECTED) {
            int type = networkInfo.getType();
            boolean z = state == state2;
            log("networkType=" + type + ",connected=" + z);
            if (type == 0) {
                if (this.mIsMobileConnected == z) {
                    return;
                }
                this.mIsMobileConnected = z;
                log("mobile=" + this.mIsMobileConnected);
                if (this.mIsMobileConnected) {
                    return;
                }
                exitResolutionSignalMonitorStatus(ExitCode.DATA_DISCONNECT);
                exitResolutionAdjust(OptimizeExitCode.DATA_DISCONNECT);
                return;
            }
            if (type == 1) {
                log("mIsWifiConnected=" + this.mIsWifiConnected + ", connected=" + z);
                if (this.mIsWifiConnected == z) {
                    return;
                }
                this.mIsWifiConnected = z;
                if (z) {
                    exitResolutionSignalMonitorStatus(ExitCode.WIFI_CONNECTED);
                    exitResolutionAdjust(OptimizeExitCode.WIFI_CONNECTED);
                    if (this.mInSubwayByEnterExit) {
                        setSnsSubwayStateOut();
                        this.mInSubwayByEnterExit = false;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCommonSceneForProblematicCell() {
        boolean z = this.mIsMobileConnected && !this.mIsWifiConnected && isPhoneStateIdle();
        if (!z) {
            log("isCommonSceneForProblematicCell: mIsMobileConnected=" + this.mIsMobileConnected + ", mIsWifiConnected=" + this.mIsWifiConnected + ", isPhoneStateIdle=" + isPhoneStateIdle());
        }
        return z;
    }

    private boolean isPhoneStateIdle() {
        return !MiuiPhoneUtils.isDuringCall();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processProblematicCellAvoidEvent(Intent intent) {
        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("dubiousCellData");
        logv("processProblematicCellAvoidEvent dubiousCellData=" + stringArrayListExtra);
        setLteNrDubiousCellIfNeed(stringArrayListExtra, "subway");
    }

    private void setLteNrDubiousCellIfNeed(List list, String str) {
        if (updateLteNrDubiousData(list)) {
            if (!this.mLteDubiousCellInfoSList.isEmpty() && NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setLteDubiousCell(this.mDdsPhoneId, this.mLteDubiousCellInfoSList, 0, str);
            }
            if (this.mNrDubiousCellInfoSList.isEmpty() || NetworkRecoveryStrategyBase.getInstance() == null) {
                return;
            }
            NetworkRecoveryStrategyBase.getInstance().setNrDubiousCell(this.mDdsPhoneId, this.mNrDubiousCellInfoSList, 0, str);
        }
    }

    private boolean updateLteNrDubiousData(List list) {
        if (list == null || list.isEmpty() || mActiveBand.isEmpty()) {
            log("updateLteNrDubiousData: dubiousCellData empty");
            return false;
        }
        this.mLteDubiousCellInfoSList.clear();
        this.mNrDubiousCellInfoSList.clear();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String[] strArrSplit = ((String) it.next()).split("\\|");
            DubiousCellInfoS commonCellInfo = parseCommonCellInfo(strArrSplit);
            if (commonCellInfo == null) {
                return false;
            }
            try {
                long j = Long.parseLong(strArrSplit[3]);
                String str = strArrSplit[0];
                if ("lte".equals(str)) {
                    LteDubiousCellInfoS lteDubiousCellInfoS = new LteDubiousCellInfoS();
                    lteDubiousCellInfoS.cellInfo = commonCellInfo;
                    lteDubiousCellInfoS.cgi = (int) j;
                    this.mLteDubiousCellInfoSList.add(lteDubiousCellInfoS);
                } else if ("nr".equals(str)) {
                    String[] strArrSplit2 = strArrSplit[4].split("_");
                    if (strArrSplit2.length != 3) {
                        return false;
                    }
                    int i = Integer.parseInt(strArrSplit2[2]);
                    NrDubiousCellInfoS nrDubiousCellInfoS = new NrDubiousCellInfoS();
                    nrDubiousCellInfoS.cellInfo = commonCellInfo;
                    nrDubiousCellInfoS.cgi = j;
                    Integer num = (Integer) mBandMappingScs.get(Integer.valueOf(i));
                    if (num == null) {
                        nrDubiousCellInfoS.nrScsType = getNrDubiousCellScs(strArrSplit[8]);
                    } else {
                        nrDubiousCellInfoS.nrScsType = getNrDubiousCellScs(String.valueOf(num));
                    }
                    this.mNrDubiousCellInfoSList.add(nrDubiousCellInfoS);
                } else {
                    log("updateLteNrDubiousData: network_type exception");
                    return false;
                }
            } catch (Exception e) {
                log("updateLteNrDubiousData exception:" + e);
                return false;
            }
        }
        logv("updateLteNrDubiousData lteList=" + this.mLteDubiousCellInfoSList + ", nrList=" + this.mNrDubiousCellInfoSList);
        return true;
    }

    private boolean checkCellInfo(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            log("parseCommonCellInfo: cellInfo exception");
            return false;
        }
        String str = strArr[0];
        if (!TextUtils.isEmpty(str) && ((!"lte".equals(str) || strArr.length == 8) && ((!"nr".equals(str) || strArr.length == 9) && ("lte".equals(str) || "nr".equals(str))))) {
            return true;
        }
        log("parseCommonCellInfo: cellInfo exception");
        return false;
    }

    private DubiousCellInfoS parseCommonCellInfo(String[] strArr) {
        if (!checkCellInfo(strArr)) {
            return null;
        }
        try {
            short s = Short.parseShort(strArr[1]);
            short s2 = Short.parseShort(strArr[2]);
            int i = Integer.parseInt(strArr[5]);
            short s3 = Short.parseShort(strArr[6]);
            int dubiousCellCause = getDubiousCellCause(strArr[7].split(":"));
            PlmnIdS plmnIdS = new PlmnIdS();
            plmnIdS.mcc = s;
            plmnIdS.mnc = s2;
            DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
            dubiousCellInfoS.plmnId = plmnIdS;
            Map map = mActiveBand;
            if (map.get(strArr[4]) == null) {
                log("parseCommonCellInfo: a non-existent band");
                return null;
            }
            dubiousCellInfoS.activeBand = ((Integer) map.get(strArr[4])).intValue();
            dubiousCellInfoS.arfcn = i;
            dubiousCellInfoS.pci = s3;
            dubiousCellInfoS.causeCodeBitMask = dubiousCellCause;
            return dubiousCellInfoS;
        } catch (Exception e) {
            log("parseCommonCellInfo exception:" + e);
            return null;
        }
    }

    private int getNrDubiousCellScs(String str) {
        if ("15".equals(str)) {
            return 0;
        }
        if ("30".equals(str)) {
            return 1;
        }
        if ("60".equals(str)) {
            return 2;
        }
        if ("120".equals(str)) {
            return 3;
        }
        return "240".equals(str) ? 4 : 0;
    }

    private int getDubiousCellCause(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return 0;
        }
        int i = 0;
        for (String str : strArr) {
            if ("CEF".equals(str)) {
                i |= 1;
            } else if ("RLF".equals(str)) {
                i |= 2;
            } else if ("PINGPONG".equals(str)) {
                i |= 4;
            } else if ("LOW_DATA_RATE_PS".equals(str)) {
                i |= 8;
            } else if ("LOW_DATA_RATE_IMS".equals(str)) {
                i |= 16;
            } else if ("NAS_REJ_CELLBARRING".equals(str)) {
                i |= 32;
            }
        }
        return i;
    }

    private void initBandData() {
        for (String str : "BC_0 = 0,BC_1 = 1,BC_3 = 3,BC_4 = 4,BC_5 = 5,BC_6 = 6,BC_7 = 7,BC_8 = 8,BC_9 = 9,BC_10 = 10,BC_11 = 11,BC_12 = 12,BC_13 = 13,BC_14 = 14,BC_15 = 15,BC_16 = 16,BC_17 = 17,BC_18 = 18,BC_19 = 19,GSM_450 = 40,GSM_480 = 41,GSM_750 = 42,GSM_850 = 43,GSM_900_EXTENDED = 44,GSM_900_PRIMARY = 45,GSM_900_RAILWAYS = 46,GSM_1800 = 47,GSM_1900 = 48,WCDMA_2100 = 80,WCDMA_PCS_1900 = 81,WCDMA_DCS_1800 = 82,WCDMA_1700_US = 83,WCDMA_850 = 84,WCDMA_800 = 85,WCDMA_2600 = 86,WCDMA_900 = 87,WCDMA_1700_JAPAN = 88,WCDMA_1500_JAPAN = 90,WCDMA_850_JAPAN = 91,E_UTRA_OPERATING_BAND_1 = 120,E_UTRA_OPERATING_BAND_2 = 121,E_UTRA_OPERATING_BAND_3 = 122,E_UTRA_OPERATING_BAND_4 = 123,E_UTRA_OPERATING_BAND_5 = 124,E_UTRA_OPERATING_BAND_6 = 125,E_UTRA_OPERATING_BAND_7 = 126,E_UTRA_OPERATING_BAND_8 = 127,E_UTRA_OPERATING_BAND_9 = 128,E_UTRA_OPERATING_BAND_10 = 129,E_UTRA_OPERATING_BAND_11 = 130,E_UTRA_OPERATING_BAND_12 = 131,E_UTRA_OPERATING_BAND_13 = 132,E_UTRA_OPERATING_BAND_14 = 133,E_UTRA_OPERATING_BAND_17 = 134,E_UTRA_OPERATING_BAND_33 = 135,E_UTRA_OPERATING_BAND_34 = 136,E_UTRA_OPERATING_BAND_35 = 137,E_UTRA_OPERATING_BAND_36 = 138,E_UTRA_OPERATING_BAND_37 = 139,E_UTRA_OPERATING_BAND_38 = 140,E_UTRA_OPERATING_BAND_39 = 141,E_UTRA_OPERATING_BAND_40 = 142,E_UTRA_OPERATING_BAND_18 = 143,E_UTRA_OPERATING_BAND_19 = 144,E_UTRA_OPERATING_BAND_20 = 145,E_UTRA_OPERATING_BAND_21 = 146,E_UTRA_OPERATING_BAND_24 = 147,E_UTRA_OPERATING_BAND_25 = 148,E_UTRA_OPERATING_BAND_41 = 149,E_UTRA_OPERATING_BAND_42 = 150,E_UTRA_OPERATING_BAND_43 = 151,E_UTRA_OPERATING_BAND_23 = 152,E_UTRA_OPERATING_BAND_26 = 153,E_UTRA_OPERATING_BAND_32 = 154,E_UTRA_OPERATING_BAND_125 = 155,E_UTRA_OPERATING_BAND_126 = 156,E_UTRA_OPERATING_BAND_127 = 157,E_UTRA_OPERATING_BAND_28 = 158,E_UTRA_OPERATING_BAND_29 = 159,E_UTRA_OPERATING_BAND_30 = 160,E_UTRA_OPERATING_BAND_66 = 161,E_UTRA_OPERATING_BAND_250 = 162,E_UTRA_OPERATING_BAND_46 = 163,E_UTRA_OPERATING_BAND_27 = 164,E_UTRA_OPERATING_BAND_31 = 165,E_UTRA_OPERATING_BAND_71 = 166,E_UTRA_OPERATING_BAND_47 = 167,E_UTRA_OPERATING_BAND_48 = 168,E_UTRA_OPERATING_BAND_67 = 169,E_UTRA_OPERATING_BAND_68 = 170,E_UTRA_OPERATING_BAND_49 = 171,E_UTRA_OPERATING_BAND_85 = 172,E_UTRA_OPERATING_BAND_72 = 173,E_UTRA_OPERATING_BAND_73 = 174,E_UTRA_OPERATING_BAND_86 = 175,E_UTRA_OPERATING_BAND_53 = 176,E_UTRA_OPERATING_BAND_87 = 177,E_UTRA_OPERATING_BAND_88 = 178,E_UTRA_OPERATING_BAND_70 = 179,TDSCDMA_BAND_A = 200,TDSCDMA_BAND_B = 201,TDSCDMA_BAND_C = 202,TDSCDMA_BAND_D = 203,TDSCDMA_BAND_E = 204,TDSCDMA_BAND_F = 205,NR5G_BAND_1 = 250,NR5G_BAND_2 = 251,NR5G_BAND_3 = 252,NR5G_BAND_5 = 253,NR5G_BAND_7 = 254,NR5G_BAND_8 = 255,NR5G_BAND_20 = 256,NR5G_BAND_28 = 257,NR5G_BAND_38 = 258,NR5G_BAND_41 = 259,NR5G_BAND_50 = 260,NR5G_BAND_51 = 261,NR5G_BAND_66 = 262,NR5G_BAND_70 = 263,NR5G_BAND_71 = 264,NR5G_BAND_74 = 265,NR5G_BAND_75 = 266,NR5G_BAND_76 = 267,NR5G_BAND_77 = 268,NR5G_BAND_78 = 269,NR5G_BAND_79 = 270,NR5G_BAND_80 = 271,NR5G_BAND_81 = 272,NR5G_BAND_82 = 273,NR5G_BAND_83 = 274,NR5G_BAND_84 = 275,NR5G_BAND_85 = 276,NR5G_BAND_257 = 277,NR5G_BAND_258 = 278,NR5G_BAND_259 = 279,NR5G_BAND_260 = 280,NR5G_BAND_261 = 281,NR5G_BAND_12 = 282,NR5G_BAND_25 = 283,NR5G_BAND_34 = 284,NR5G_BAND_39 = 285,NR5G_BAND_40 = 286,NR5G_BAND_65 = 287,NR5G_BAND_86 = 288,NR5G_BAND_48 = 289,NR5G_BAND_14 = 290,NR5G_BAND_13 = 291,NR5G_BAND_18 = 292,NR5G_BAND_26 = 293,NR5G_BAND_30 = 294,NR5G_BAND_29 = 295,NR5G_BAND_53 = 296,NR5G_BAND_46 = 297,NR5G_BAND_91 = 298,NR5G_BAND_92 = 299,NR5G_BAND_93 = 300,NR5G_BAND_94 = 301".split(",")) {
            String[] strArrSplit = str.split("=");
            mActiveBand.put(strArrSplit[0].trim(), Integer.valueOf(Integer.parseInt(strArrSplit[1].trim())));
        }
    }

    private void initBandMappingScs() {
        for (String str : mContext.getResources().getStringArray(R.array.dubious_cell_band_mapping_scs)) {
            String[] strArrSplit = str.split(",");
            if (strArrSplit.length != 2) {
                log("bandMappingScs config exception");
                return;
            }
            String str2 = strArrSplit[0];
            if (str2 == null || strArrSplit[1] == null) {
                log("initBandMappingScs exception");
                return;
            }
            mBandMappingScs.put(Integer.valueOf(Integer.parseInt(str2)), Integer.valueOf(Integer.parseInt(strArrSplit[1])));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHstStateChangeEvent(Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra("mIsHstMode", false);
        this.mIsHighSpeedTrain = booleanExtra;
        if (booleanExtra) {
            this.mIsFirstVideoPreloaded = false;
            long currentDlRateBps = NetworkSpeedTracker.getInstance().getCurrentDlRateBps() / 1024;
            if (this.mIsMobileConnected && isPreloadVideoWhiteList() && currentDlRateBps >= 600 && isHstPreloadEeceedPenaltyTimer()) {
                videoAppsPolicyNotify(1, -1, -1, VideoPreloadScene.HST_VIDEO_QULITY.getValue());
                log("videoAppsPolicyNotify is effective");
                this.mIsFirstVideoPreloaded = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMiuiVideoInfoEvent(Intent intent) {
        if (this.mIsMobileConnected && isPreloadVideoWhiteList()) {
            int intExtra = intent.getIntExtra("render-quality", -1);
            log("HSTVideoPreload video quality:" + intExtra);
            if (intExtra == 0) {
                this.mIsLongTermFreezeRecovery = true;
                if (this.mHandler.hasMessages(16)) {
                    this.mHandler.removeMessages(16);
                }
                SubwayModeHandler subwayModeHandler = this.mHandler;
                subwayModeHandler.sendMessageDelayed(subwayModeHandler.obtainMessage(16), 6000L);
                return;
            }
            if (intExtra == 1) {
                long currentDlRateBps = NetworkSpeedTracker.getInstance().getCurrentDlRateBps() / 1024;
                if ((!this.mIsFirstVideoPreloaded || this.mIsStrongFieldToPreload) && currentDlRateBps >= 600 && isHstPreloadEeceedPenaltyTimer()) {
                    videoAppsPolicyNotify(1, -1, -1, VideoPreloadScene.HST_VIDEO_QULITY.getValue());
                    log("videoAppsPolicyNotify is effective");
                    this.mIsFirstVideoPreloaded = true;
                    this.mIsStrongFieldToPreload = false;
                }
                this.mIsLongTermFreezeRecovery = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMiuiVideoInfoDelayEvent() {
        long currentDlRateBps = NetworkSpeedTracker.getInstance().getCurrentDlRateBps() / 1024;
        log("processMiuiVideoInfoDelayEvent hstDlRateKBps=" + currentDlRateBps);
        if (this.mIsLongTermFreezeRecovery && currentDlRateBps < 100 && this.mIsHighSpeedTrain && this.mIsMobileConnected && isPreloadVideoWhiteList()) {
            this.mIsStrongFieldToPreload = true;
        }
    }

    private boolean isHstPreloadEeceedPenaltyTimer() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mHstLastPreloadTime;
        if (j != 0 && jCurrentTimeMillis < j + 300000) {
            log("isHstPreloadEeceedPenaltyTimer");
            return false;
        }
        this.mHstLastPreloadTime = jCurrentTimeMillis;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSubwayStateChangeEvent() {
        int subwayState = getSubwayState();
        if (subwayState != this.mSubwayState) {
            this.mSubwayState = subwayState;
            if (subwayState == 0) {
                exitResolutionAdjust(OptimizeExitCode.EXIT_SUBWAY);
                exitResolutionSignalMonitorStatus(ExitCode.EXIT_SUBWAY);
                unregisterTelephonyInfoCallBack();
                unregisterModemNetworkPerception();
                unregisterScreenState();
                if (this.mInSubwayByStartStop) {
                    updateSubwayBand(false);
                    this.mInSubwayByStartStop = false;
                    recordModemSubwayRecognition();
                    clearModemSubwayStatisticInfo();
                    setSnsSubwayStateOut();
                    this.mInSubwayByEnterExit = false;
                    return;
                }
                return;
            }
            if (subwayState == 1) {
                registerModemNetworkPerception();
                this.mSubwayStartTime = System.currentTimeMillis();
                updateModemSubwayCountWhileStart();
                if (!this.mInSubwayByStartStop) {
                    this.mInSubwayByStartStop = true;
                    updateSubwayBand(true);
                    setSnsSubwayStateCarriage();
                }
                stopMonitorTimerForSubwayUpdate();
                return;
            }
            if (subwayState != 2) {
                return;
            }
            this.mSubwayStopTime = System.currentTimeMillis();
            registerTelephonyInfoCallBack();
            this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(mContext);
            registerScreenState();
            exitResolutionSignalMonitorStatus(ExitCode.SUBWAY_STOP);
            enterPreloadSignalMonitorStatus(this.mDdsPhoneId);
            startMonitorTimerForSubwayUpdate();
            recordCountSubwayModeStats("subwayStopCount");
        }
    }

    private void clearModemSubwayStatisticInfo() {
        log("clearModemSubwayStatisticInfo");
        this.mSubwayStartTime = 0L;
        this.mSubwayStopTime = 0L;
        this.mModemSubwayPlatformCorrectCount = 0;
        this.mModemSubwayPlatformCount = 0;
        this.mModemSubwayPlatformMissCount = 0;
        this.mSubwayRealPlatformCount = 0;
        this.mModemSubwayPlaformTimes.clear();
    }

    private int getSubwayState() {
        if (this.mNetworkModeRecognition == null) {
            return 0;
        }
        int i = Settings.Global.getInt(this.mResolver, "network_mode_state", 0);
        if (!this.mNetworkModeRecognition.isSubwayStartStopStateUpdate(i)) {
            return 0;
        }
        if (this.mNetworkModeRecognition.isSubwayStart(i)) {
            return 1;
        }
        return this.mNetworkModeRecognition.isSubwayStop(i) ? 2 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initResolutionVideoWhiteList() {
        if (mResolutionVideoWhiteList.size() > 0) {
            mResolutionVideoWhiteList.clear();
        }
        List resolutionVideoAppsWhiteListByCloud = FiveGManagerBase.getResolutionVideoAppsWhiteListByCloud(mContext);
        mResolutionVideoWhiteList = resolutionVideoAppsWhiteListByCloud;
        if (resolutionVideoAppsWhiteListByCloud.size() != 0) {
            log("mResolutionVideoWhiteList=" + mResolutionVideoWhiteList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initSignalThreshold() {
        if (mSignalThresholdList.size() > 0) {
            mSignalThresholdList.clear();
        }
        List subwayModeSignalThresholdByCloud = FiveGManagerBase.getSubwayModeSignalThresholdByCloud(mContext);
        mSignalThresholdList = subwayModeSignalThresholdByCloud;
        if (subwayModeSignalThresholdByCloud.size() != 0) {
            log("mSignalThresholdList=" + mSignalThresholdList);
        }
        try {
            this.mEnterMonitorLowerLimit = Integer.parseInt((String) mSignalThresholdList.get(0));
            this.mEnterMonitorUpperLimit = Integer.parseInt((String) mSignalThresholdList.get(1));
            this.mExitMonitorThreshold = Integer.parseInt((String) mSignalThresholdList.get(2));
            this.mEnableResolutionThreshold = Integer.parseInt((String) mSignalThresholdList.get(3));
            this.mDisableResolutionThreshold = Integer.parseInt((String) mSignalThresholdList.get(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPreloadVideoWhiteList() {
        if (mPreloadVideoWhiteList.size() > 0) {
            mPreloadVideoWhiteList.clear();
        }
        List preloadVideoAppsWhiteListByCloud = FiveGManagerBase.getPreloadVideoAppsWhiteListByCloud(mContext);
        mPreloadVideoWhiteList = preloadVideoAppsWhiteListByCloud;
        if (preloadVideoAppsWhiteListByCloud.size() != 0) {
            log("mPreloadVideoWhiteList=" + mPreloadVideoWhiteList);
        }
    }

    private boolean isPreloadVideoWhiteList() {
        if (mPreloadVideoWhiteList.size() == 0) {
            return false;
        }
        String topAppName = getTopAppName();
        log("isPreloadVideoWhiteList topAppName:" + topAppName);
        Iterator it = mPreloadVideoWhiteList.iterator();
        while (it.hasNext()) {
            if (topAppName.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    private boolean isResolutionVideoWhiteList() {
        if (mResolutionVideoWhiteList.size() == 0) {
            return false;
        }
        String topAppName = getTopAppName();
        log("isResolutionVideoWhiteList topAppName:" + topAppName);
        Iterator it = mResolutionVideoWhiteList.iterator();
        while (it.hasNext()) {
            if (topAppName.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    public void videoAppsPolicyNotify(int i, int i2, int i3, int i4) {
        log("videoAppsPolicyNotify type=" + i + ", duration=" + i2 + ", length=" + i3);
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().videoAppsPolicyNotify(i, i2, i3, i4);
        }
        if (i == 1) {
            recordCountSubwayModeStats("subwayPreloadCount");
        } else if (i == 2) {
            recordCountSubwayModeStats("subwayResolutionCount");
        }
    }

    private String getTopAppName() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService("activity");
        if (activityManager == null) {
            log("getTopAppName activityManager is null");
            return "";
        }
        try {
            if (activityManager.getRunningTasks(1) != null && activityManager.getRunningTasks(1).get(0) != null) {
                ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
                log("getTopAppName packageName =" + componentName.getPackageName());
                return componentName.getPackageName();
            }
        } catch (Exception e) {
            log("getTopAppName exception:" + e.toString());
        }
        return "";
    }

    private void registerScreenState() {
        logv("registerScreenState");
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
    }

    private void unregisterScreenState() {
        logv("unregisterScreenState");
        EventHubManager.getInstance().unregisterListener(this.mDisplayListener);
    }

    private void registerTelephonyInfoCallBack() {
        if (mTelephonyInfoCallBack == null) {
            log("registerTelephonyInfoCallBack");
            TelephonyInfoCallBack telephonyInfoCallBack = new TelephonyInfoCallBack();
            mTelephonyInfoCallBack = telephonyInfoCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyInfoCallBack);
        }
    }

    private void unregisterTelephonyInfoCallBack() {
        if (mTelephonyInfoCallBack != null) {
            log("unregisterTelephonyInfoCallBack");
            TelephonyStatAdapter.unregisterCallBack(mTelephonyInfoCallBack);
            mTelephonyInfoCallBack = null;
        }
    }

    class TelephonyInfoCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyInfoCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            SubwayModeController.this.mHandler.obtainMessage(4, i, 0, signalStrength).sendToTarget();
        }
    }

    private void recordCountSubwayModeStats(String str) {
        SubwayModeStats subwayModeStats = this.mSubwayModeStats;
        if (subwayModeStats != null) {
            subwayModeStats.recordCount(str);
        }
    }

    public void reportStatistics() {
        SubwayModeStats subwayModeStats = this.mSubwayModeStats;
        if (subwayModeStats == null) {
            return;
        }
        subwayModeStats.reportStatistic();
        this.mSubwayModeStats.clear();
    }

    public void reloadSubwayModeStatsFromSP() {
        SubwayModeStats subwayModeStats = this.mSubwayModeStats;
        if (subwayModeStats == null) {
            return;
        }
        subwayModeStats.reloadStatsFromSP();
    }

    private void updateSubwayBand(boolean z) {
        if (MiuiTelephonyStatistic.getInstance() == null) {
            return;
        }
        log("updateSubwayBand isSubwayMode = " + z);
        if (!z) {
            MiuiTelephonyStatistic.getInstance().updateNwBandInfo(this.mDdsPhoneId, null, true);
        }
        if (MiuiTelephonyStatistic.getInstance().mSubwayModemStats != null) {
            MiuiTelephonyStatistic.getInstance().mSubwayModemStats.updateSubwayState(z);
        }
    }

    public boolean isInSubway() {
        return this.mInSubwayByStartStop;
    }

    private void registerModemNetworkPerception() {
        if (this.mModemNetworkPerception != null && mModemNetworkPerceptionCallBack == null) {
            log("registerModemNetworkPerception");
            ModemNetworkPerceptionCallBack modemNetworkPerceptionCallBack = new ModemNetworkPerceptionCallBack();
            mModemNetworkPerceptionCallBack = modemNetworkPerceptionCallBack;
            this.mModemNetworkPerception.registerModemNetworkPerception(modemNetworkPerceptionCallBack);
        }
    }

    private void unregisterModemNetworkPerception() {
        if (this.mModemNetworkPerception == null || mModemNetworkPerceptionCallBack == null) {
            return;
        }
        log("unregisterModemNetworkPerception");
        this.mModemNetworkPerception.unRegisterModemNetworkPerception(mModemNetworkPerceptionCallBack);
        mModemNetworkPerceptionCallBack = null;
    }

    class ModemNetworkPerceptionCallBack implements ModemNetworkPerception.IModemNetworkPerception {
        private ModemNetworkPerceptionCallBack() {
        }

        @Override // com.android.phone.network.ModemNetworkPerception.IModemNetworkPerception
        public void onSaSubwayPlatformRecognize(int i, int i2) {
            SubwayModeController.this.log("onSaSubwayPlatformRecognize phoneId=" + i + ", type=" + i2);
            SubwayModeController.this.mHandler.obtainMessage(17, i, i2).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSaSubwayPlatformRecognizeEvent(int i, int i2) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && i2 == 0) {
            this.mModemSubwayPlaformTimes.add(Long.valueOf(System.currentTimeMillis()));
        }
    }

    private void updateModemSubwayCountWhileStart() {
        log("updateModemSubwayCountWhileStart");
        if (this.mSubwayStopTime == 0 || this.mSubwayStartTime == 0) {
            return;
        }
        this.mSubwayRealPlatformCount++;
        if (this.mModemSubwayPlaformTimes.isEmpty()) {
            this.mModemSubwayPlatformMissCount++;
            return;
        }
        long j = this.mSubwayStopTime - 10000;
        Iterator it = this.mModemSubwayPlaformTimes.iterator();
        while (it.hasNext()) {
            long jLongValue = ((Long) it.next()).longValue();
            this.mModemSubwayPlatformCount++;
            if (jLongValue >= j && jLongValue <= this.mSubwayStartTime) {
                this.mModemSubwayPlatformCorrectCount++;
            }
        }
        this.mModemSubwayPlaformTimes.clear();
    }

    class SnsSubwayStateCallBack implements QmsClientBase.ISubwayStateListener {
        SnsSubwayStateCallBack() {
        }

        @Override // com.android.phone.QmsClientBase.ISubwayStateListener
        public void onSubwayStateReportChanged(int i) {
            SubwayModeController.this.log("onSubwayStateReportChanged state=" + i);
            if (i == 0) {
                if (SubwayModeController.this.mInSubwayByEnterExit && SubwayModeController.this.mInSubwayByStartStop) {
                    SubwayModeController.this.setSnsSubwayStateIn();
                    SubwayModeController.this.setSnsSubwayStateCarriage();
                    return;
                }
                boolean z = SubwayModeController.this.mInSubwayByEnterExit;
                SubwayModeController subwayModeController = SubwayModeController.this;
                if (z) {
                    subwayModeController.setSnsSubwayStateIn();
                } else {
                    subwayModeController.setSnsSubwayStateOut();
                }
            }
        }
    }

    private void regSnsSubwayStateCallBack() {
        if (this.mSnsSubwayStateCallBack == null) {
            log("regSnsSubwayStateCallBack");
            this.mSnsSubwayStateCallBack = new SnsSubwayStateCallBack();
            QmsClientBase qmsClientBase = QmsClientBase.getInstance();
            if (qmsClientBase != null) {
                qmsClientBase.registerSubwayStateListener(this.mSnsSubwayStateCallBack);
            }
        }
    }

    class SensorStateCallBack implements TelephonySensorManager.SensorStateListener {
        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onDriveStateChanged(boolean z) {
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onHstStateChanged(boolean z) {
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onScreenStateChanged(boolean z) {
        }

        SensorStateCallBack() {
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onSubwayStateChanged(boolean z) {
            boolean z2 = SubwayModeController.this.mIsWifiConnected;
            SubwayModeController subwayModeController = SubwayModeController.this;
            if (z2) {
                subwayModeController.log("onSubwayStateChanged ignore for wifi connected, cur=" + z);
                return;
            }
            subwayModeController.log("onSubwayStateChanged, last=" + SubwayModeController.this.mInSubwayByEnterExit + ", cur=" + z);
            if (SubwayModeController.this.mInSubwayByEnterExit == z) {
                return;
            }
            SubwayModeController.this.mInSubwayByEnterExit = z;
            boolean z3 = SubwayModeController.this.mInSubwayByEnterExit;
            SubwayModeController subwayModeController2 = SubwayModeController.this;
            if (z3) {
                subwayModeController2.setSnsSubwayStateIn();
            } else {
                subwayModeController2.setSnsSubwayStateOut();
            }
        }
    }

    private void regSensorStateCallBack() {
        TelephonySensorManager telephonySensorManager = TelephonySensorManager.getInstance();
        if (this.mSensorStateCallBack != null || telephonySensorManager == null) {
            return;
        }
        log("regSensorStateCallBack");
        SensorStateCallBack sensorStateCallBack = new SensorStateCallBack();
        this.mSensorStateCallBack = sensorStateCallBack;
        telephonySensorManager.registerSensorStateListener(sensorStateCallBack);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSnsSubwayStateIn() {
        if (MiuiPhoneUtils.isSubwayEnhancedSupported()) {
            log("setSnsSubwayStateIn");
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setSubwayState(1, "subway");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSnsSubwayStateCarriage() {
        if (MiuiPhoneUtils.isSubwayEnhancedSupported()) {
            log("setSnsSubwayStateCarriage");
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setSubwayState(2, "subway");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSnsSubwayStateOut() {
        if (MiuiPhoneUtils.isSubwayEnhancedSupported()) {
            log("setSnsSubwayStateOut");
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setSubwayState(3, "subway");
            }
        }
    }

    private void recordModemSubwayRecognition() {
        if (MiuiPhoneUtils.isModemNetworkPerceptionSupported()) {
            log("recordModemSubwayRecognition");
            HashMap map = new HashMap();
            map.put("correctCount", Integer.valueOf(this.mModemSubwayPlatformCorrectCount));
            map.put("allCount", Integer.valueOf(this.mModemSubwayPlatformCount));
            map.put("missCount", Integer.valueOf(this.mModemSubwayPlatformMissCount));
            map.put("realCount", Integer.valueOf(this.mSubwayRealPlatformCount));
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_modem_subway_platform_statistic", map);
        }
    }

    class SubwayModeStats {
        private ConcurrentHashMap mStats;

        private SubwayModeStats() {
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
                SubwayModeController.this.logv("recordCount key=" + str + ", mStats=" + this.mStats);
                DefaultPreferenceHelper.getInstance().setSubwayModeStats(ObjectSerializer.serialize(this.mStats));
            } catch (Exception e) {
                SubwayModeController.this.log("record exception:" + e);
            }
        }

        public void reportStatistic() {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
                return;
            }
            HashMap map = new HashMap(this.mStats);
            SubwayModeController.this.log("reportStatistic mStats=" + this.mStats);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_subway_mode_statistic", map);
        }

        public void reloadStatsFromSP() {
            try {
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) ObjectSerializer.deserialize(DefaultPreferenceHelper.getInstance().getSubwayModeStats());
                if (concurrentHashMap != null) {
                    this.mStats = concurrentHashMap;
                }
                SubwayModeController.this.logv("reloadStatsFromSP mStats=" + this.mStats);
            } catch (Exception e) {
                SubwayModeController.this.log("reloadStatsFromSP exception:" + e);
            }
        }

        public void clear() {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null) {
                return;
            }
            concurrentHashMap.clear();
            try {
                DefaultPreferenceHelper.getInstance().setSubwayModeStats(ObjectSerializer.serialize(this.mStats));
            } catch (Exception e) {
                SubwayModeController.this.log("clear exception:" + e);
            }
        }
    }

    enum ExitCode {
        DUMMY(0),
        INVALID_RAT(1),
        SUBWAY_STOP(2),
        SIGNAL_GOOD_ENOUGH(3),
        SCREEN_OFF(4),
        DATA_DISCONNECT(5),
        TIMEOUT(6),
        WIFI_CONNECTED(7),
        DDS_CHANGE(8),
        EXIT_SUBWAY(9),
        NORMAL_EXIT(10);

        private int value;

        ExitCode(int i) {
            this.value = i;
        }
    }

    enum OptimizeExitCode {
        DUMMY(0),
        EXIT_SUBWAY(1),
        SIGNAL(2),
        SCREEN_AND_SIGNAL(3),
        DDS_CHANGED(4),
        DATA_DISCONNECT(5),
        WIFI_CONNECTED(6);

        private int value;

        OptimizeExitCode(int i) {
            this.value = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("SubwayModeController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logv(String str) {
        if (this.VDBG) {
            Log.d("SubwayModeController", str);
        }
    }
}
