package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.internal.util.ArrayUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.signalMap.SignalMapHSTCloudController;
import com.android.phone.statistics.HighSpeedTrainStats;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.telephony.DefaultSimManager;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyStatAdapter;
import xiaomi.hypercomm.HyperCommManager;
import xiaomi.hypercomm.SceneAwareCallback;

/* JADX INFO: loaded from: classes.dex */
public class HighSpeedTrainSmartSwitcher {
    private static final Set DSA_DOWNGRADE_CID_WHITELIST;
    private static final Map eventIdToName;
    private static volatile HighSpeedTrainSmartSwitcher instance;
    private static List mForegroundAppWhitelist;
    private static final HashMap sLocalLogs;
    private Context mContext;
    private volatile boolean mContinuousVideoQualityStall;
    private String mCurrentForegroundPackageName;
    private int mCurrentLteRsrp;
    private int mCurrentLteRsrq;
    private int mCurrentLteSnr;
    private int mCurrentNrRsrp;
    private int mCurrentNrSnr;
    private int mDdsPhoneId;
    private int mDeprioritizeDdsPhoneId;
    private volatile int mDeprioritizeNrLimitedCount;
    private final Queue mDeprioritizeNrRecords;
    private volatile int mForceLte2NrLimitedCount;
    private final BaseEvent.IEventListener mForegroundInfoListener;
    private final BroadcastReceiver mIntentReceiver;
    private volatile boolean mIsBlacklistedCell;
    private volatile boolean mIsDeprioritizeNr;
    private boolean mIsForegroundApp;
    private volatile boolean mIsHstLinkNotReliable;
    private boolean mIsInCall;
    private volatile boolean mIsLinkNotReliable;
    private boolean mIsLteNetworkType;
    private boolean mIsMobileDataOn;
    private boolean mIsNrNetworkType;
    private volatile boolean mIsQoeException;
    private boolean mIsWifiDataOn;
    private volatile boolean mLteWeakSignalTracking;
    private volatile boolean mModemMcsStall;
    private NetworkMetricsController mNetworkMetricsController;
    private int mRestoreNrCheckCount;
    private SceneAwareCallback mSceneAwareCallback;
    private SmartSwitcherHandler mSmartSwitcherHandler;
    private TelephonyInfoCallBack mTelephonyInfoCallBack;
    private volatile boolean mVideoQualityStall;
    private HandlerThread mWorkThread;
    private boolean mIsHighSpeedTrainLine = false;
    private boolean mIsJingHuHstLine = false;
    private boolean mIsHighSpeedTrainMode = false;
    private long mStallStartTime = -1;
    private final Queue mShortStallRecords = new LinkedList();
    private final Queue mForceLte2NrRecords = new LinkedList();
    private boolean mIsContinuousTimerActive = false;
    private boolean mTestCellIdMode = false;
    private long mTestCellId = Long.MAX_VALUE;
    private boolean mTestIsNrCellType = false;
    private boolean mTestSignalMode = false;
    private int UNAVAILABLE = Integer.MAX_VALUE;
    private int mTestLteRsrp = Integer.MAX_VALUE;
    private int mTestLteRsrq = Integer.MAX_VALUE;
    private int mTestNrRsrp = Integer.MAX_VALUE;
    private int mTestLteSnr = Integer.MAX_VALUE;
    private int mTestNrSnr = Integer.MAX_VALUE;
    private boolean mDebugLog = false;
    private boolean mIsJingGuangHstLine = false;
    private final Set mNeedSwitchNrToLteCellSet = new HashSet();
    private long mLastCellId = Long.MAX_VALUE;

    private boolean isLteNetworkType(int i) {
        return i == 14 || i == 19;
    }

    private boolean isNrNetworkType(int i) {
        return i == 20 || i == 30;
    }

    private boolean isShortStall(long j) {
        return j >= MirrorDesktopHelper.TIMEOUT_MILLIS && j <= 10000;
    }

    static {
        ConcurrentHashMap.KeySetView keySetViewNewKeySet = ConcurrentHashMap.newKeySet();
        DSA_DOWNGRADE_CID_WHITELIST = keySetViewNewKeySet;
        keySetViewNewKeySet.add(59603284992L);
        keySetViewNewKeySet.add(10592497665L);
        mForegroundAppWhitelist = new ArrayList();
        sLocalLogs = new HashMap();
        HashMap map = new HashMap();
        eventIdToName = map;
        map.put(1001, "NETWORK_CONNECTIVITY_CHANGED");
        map.put(2001, "CELL_LOCATION_CHANGED");
        map.put(2003, "CALL_STATE_CHANGED");
        map.put(2002, "SIGNAL_STRENGTH_CHANGED");
        map.put(3001, "HST_LINE_STATUS_CHANGED");
        map.put(3002, "HST_MODE_STATUS_CHANGED");
        map.put(4001, "FOREGROUND_INFO_CHANGED");
        map.put(1002, "LINK_RELIABILITY_TIMEOUT");
        map.put(1003, "DEPRIORITIZE_NR_TIMEOUT");
        map.put(2004, "SERVICE_INFO_CHANGED");
        map.put(5001, "VIDEO_STALL_CONTINUOUS_TIMEOUT");
        map.put(6001, "LTE_WEAK_SIGNAL_TIMEOUT");
    }

    private HighSpeedTrainSmartSwitcher() {
        int i = this.UNAVAILABLE;
        this.mCurrentNrRsrp = i;
        this.mCurrentLteRsrp = i;
        this.mCurrentNrSnr = i;
        this.mCurrentLteSnr = i;
        this.mCurrentLteRsrq = i;
        this.mLteWeakSignalTracking = false;
        this.mIsHstLinkNotReliable = false;
        this.mContinuousVideoQualityStall = false;
        this.mModemMcsStall = false;
        this.mIsQoeException = false;
        this.mIsLinkNotReliable = false;
        this.mIsDeprioritizeNr = false;
        this.mDdsPhoneId = -1;
        this.mDeprioritizeDdsPhoneId = -1;
        this.mIsInCall = false;
        this.mVideoQualityStall = false;
        this.mIsNrNetworkType = false;
        this.mIsLteNetworkType = false;
        this.mRestoreNrCheckCount = 0;
        this.mCurrentForegroundPackageName = "";
        this.mForceLte2NrLimitedCount = 0;
        this.mDeprioritizeNrLimitedCount = 0;
        this.mDeprioritizeNrRecords = new LinkedList();
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.HighSpeedTrainSmartSwitcher.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                HighSpeedTrainSmartSwitcher.this.logd("receive action".concat(action));
                action.hashCode();
                switch (action) {
                    case "com.android.phone.intent.action.HST_MODE_STATUS_CHANGED":
                        HighSpeedTrainSmartSwitcher.this.handleHSTModeStatusChanged(intent);
                        break;
                    case "miui.intent.action.VIDEO_INFO":
                        HighSpeedTrainSmartSwitcher.this.handleVideoInfo(intent);
                        break;
                    case "com.android.phone.SET_TEST_PARAMS":
                        HighSpeedTrainSmartSwitcher.this.handleSetTestParams(intent);
                        break;
                    case "android.net.conn.CONNECTIVITY_CHANGE":
                        HighSpeedTrainSmartSwitcher.this.handleConnectivityChanged(intent);
                        break;
                    case "com.android.phone.intent.action.SCENE_LINK_RELIABILITY":
                        HighSpeedTrainSmartSwitcher.this.handleSceneLinkReliability(intent);
                        break;
                    case "com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED":
                        HighSpeedTrainSmartSwitcher.this.handleHSTLineStatusChanged(intent);
                        break;
                    case "miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED":
                        HighSpeedTrainSmartSwitcher.this.handleDataSlotChanged();
                        break;
                    case "com.android.phone.intent.action.LINK_RELIABILITY":
                        HighSpeedTrainSmartSwitcher.this.handleLinkReliability();
                        break;
                    case "miui.intent.action.ACTION_MODEM_MCS_DROP_EXCEPTION":
                        HighSpeedTrainSmartSwitcher.this.handleMcsStatusChanged(intent);
                        break;
                }
            }
        };
        this.mForegroundInfoListener = new BaseEvent.IEventListener() { // from class: com.android.phone.HighSpeedTrainSmartSwitcher.2
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                ForegroundInfo foregroundInfo;
                if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null) {
                    return;
                }
                HighSpeedTrainSmartSwitcher.this.mSmartSwitcherHandler.removeMessages(4001);
                HighSpeedTrainSmartSwitcher.this.mSmartSwitcherHandler.sendMessageDelayed(HighSpeedTrainSmartSwitcher.this.mSmartSwitcherHandler.obtainMessage(4001, foregroundInfo), 1000L);
            }
        };
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("HighSpeedTrainSmartSwitcher", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mSmartSwitcherHandler = new SmartSwitcherHandler(this.mWorkThread.getLooper());
        this.mDdsPhoneId = getDefaultDataSlotId();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        initWhiteList();
        registerAllInfo();
        logd("Instance initialized");
    }

    public static synchronized HighSpeedTrainSmartSwitcher getInstance() {
        try {
            if (instance == null) {
                synchronized (HighSpeedTrainSmartSwitcher.class) {
                    try {
                        if (instance == null) {
                            instance = new HighSpeedTrainSmartSwitcher();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        } catch (Throwable th2) {
            throw th2;
        }
        return instance;
    }

    private void registerAllInfo() {
        registerTelephonyInfoCallBack();
        registerReceiver();
        registerForegroundInfoListener();
        registerNetworkQoeListener();
        logd("All information registrations completed");
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.LINK_RELIABILITY");
        intentFilter.addAction("com.android.phone.intent.action.SCENE_LINK_RELIABILITY");
        intentFilter.addAction("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_MODEM_MCS_DROP_EXCEPTION");
        intentFilter.addAction("com.android.phone.SET_TEST_PARAMS");
        intentFilter.addAction("miui.intent.action.VIDEO_INFO");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    private void initWhiteList() {
        List stringArray;
        if (Build.IS_INTERNATIONAL_BUILD) {
            stringArray = HyperOSCustFeatureResolve.getStringArray("global_hst_smart_switch_white_list_default");
        } else {
            stringArray = HyperOSCustFeatureResolve.getStringArray("hst_smart_switch_white_list_default");
        }
        if (stringArray == null) {
            stringArray = new ArrayList();
        }
        mForegroundAppWhitelist = stringArray;
        logd("White list initialized, size: " + mForegroundAppWhitelist.size());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectivityChanged(Intent intent) {
        this.mSmartSwitcherHandler.obtainMessage(1001, intent).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHSTLineStatusChanged(Intent intent) {
        this.mSmartSwitcherHandler.obtainMessage(3001, intent).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHSTModeStatusChanged(Intent intent) {
        this.mSmartSwitcherHandler.obtainMessage(3002, intent).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMcsStatusChanged(Intent intent) {
        logd("onReceive ACTION_MODEM_MCS_DROP_EXCEPTION");
        if (isHstModeAndNotJingHuLine()) {
            this.mModemMcsStall = true;
            shouldDeprioritize5GToLte();
            this.mModemMcsStall = false;
        }
    }

    private boolean isHstModeAndNotJingHuLine() {
        return this.mIsHighSpeedTrainMode && !this.mIsJingHuHstLine;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetTestParams(Intent intent) {
        if (intent == null) {
            return;
        }
        boolean booleanExtra = intent.getBooleanExtra("test_cell_id_mode", false);
        this.mTestCellIdMode = booleanExtra;
        if (booleanExtra) {
            this.mTestCellId = intent.getLongExtra("test_cell_id", Long.MAX_VALUE);
            this.mTestIsNrCellType = intent.getBooleanExtra("test_is_nr", false);
            handleCellLocationChanged(0, null);
        } else {
            this.mTestCellId = Long.MAX_VALUE;
            this.mTestIsNrCellType = false;
        }
        boolean booleanExtra2 = intent.getBooleanExtra("test_signal_mode", false);
        this.mTestSignalMode = booleanExtra2;
        int i = this.UNAVAILABLE;
        if (booleanExtra2) {
            this.mTestLteRsrp = intent.getIntExtra("test_lte_rsrp", i);
            this.mTestLteRsrq = intent.getIntExtra("test_lte_rsrq", this.UNAVAILABLE);
            this.mTestNrRsrp = intent.getIntExtra("test_nr_rsrp", this.UNAVAILABLE);
            this.mTestLteSnr = intent.getIntExtra("test_lte_snr", this.UNAVAILABLE);
            this.mTestNrSnr = intent.getIntExtra("test_nr_snr", this.UNAVAILABLE);
            handleSignalStrengthChanged(0, null);
        } else {
            this.mTestLteRsrp = i;
            this.mTestLteRsrq = i;
            this.mTestNrRsrp = i;
            this.mTestLteSnr = i;
            this.mTestNrSnr = i;
        }
        this.mDebugLog = intent.getBooleanExtra("test_debug_log", this.mDebugLog);
        long longExtra = intent.getLongExtra("test_dsa_cid", Long.MAX_VALUE);
        if (longExtra != Long.MAX_VALUE) {
            DSA_DOWNGRADE_CID_WHITELIST.add(Long.valueOf(longExtra));
            this.mIsHighSpeedTrainMode = true;
            logd("handleSetTestParams: added CID " + longExtra + " to DSA whitelist, mIsHighSpeedTrainMode=true");
        }
        logd("handleSetTestParamsV1 cellIdMode=" + this.mTestCellIdMode + ", cellId=" + this.mTestCellId + ", isNr=" + this.mTestIsNrCellType + " | signalMode=" + this.mTestSignalMode + ", lteRsrp=" + this.mTestLteRsrp + ", nrRsrp=" + this.mTestNrRsrp + ", debugLog=" + this.mDebugLog + ", dsaCid=" + longExtra);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectivityChange(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null) {
            return;
        }
        if (networkInfo.getType() == 0) {
            handleMobileNetworkChange(networkInfo);
        } else if (networkInfo.getType() == 1) {
            handleWifiNetworkChange(networkInfo);
        }
    }

    private void handleMobileNetworkChange(NetworkInfo networkInfo) {
        boolean z = networkInfo.getState() == NetworkInfo.State.CONNECTED;
        if (z != this.mIsMobileDataOn) {
            this.mIsMobileDataOn = z;
            logd("Mobile data state changed: ".concat(z ? "CONNECTED" : "DISCONNECTED"));
        }
    }

    private void handleWifiNetworkChange(NetworkInfo networkInfo) {
        boolean z = networkInfo.getState() == NetworkInfo.State.CONNECTED;
        if (z != this.mIsWifiDataOn) {
            this.mIsWifiDataOn = z;
            logd("WiFi state changed: ".concat(z ? "CONNECTED" : "DISCONNECTED"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSceneLinkReliability(Intent intent) {
        logd("onReceive ACTION_SCENE_LINK_RELIABILITY");
        if (intent.getIntExtra("extra_scene_type", -1) != 1) {
            return;
        }
        if (isHstModeAndNotJingHuLine()) {
            this.mIsHstLinkNotReliable = true;
            shouldDeprioritize5GToLte();
            this.mIsHstLinkNotReliable = false;
        } else if (isHstDsaDowngradeAllowed()) {
            logd("handleSceneLinkReliability: whitelisted CID matched, trigger DSA downgrade");
            this.mIsQoeException = true;
            shouldDeprioritize5GToLte();
            this.mIsQoeException = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLinkReliability() {
        logd("onReceive ACTION_LINK_RELIABILITY");
        this.mIsLinkNotReliable = true;
        if (this.mSmartSwitcherHandler.hasMessages(1002)) {
            this.mSmartSwitcherHandler.removeMessages(1002);
        }
        SmartSwitcherHandler smartSwitcherHandler = this.mSmartSwitcherHandler;
        smartSwitcherHandler.sendMessageDelayed(smartSwitcherHandler.obtainMessage(1002), 3000L);
        this.mSmartSwitcherHandler.post(new Runnable() { // from class: com.android.phone.HighSpeedTrainSmartSwitcher$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleLinkReliability$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLinkReliability$0() {
        if (this.mIsBlacklistedCell && this.mIsHighSpeedTrainMode) {
            shouldDeprioritize5GToLte();
            this.mIsLinkNotReliable = false;
        } else if (isHstDsaDowngradeAllowed()) {
            logd("handleLinkReliability: whitelisted CID matched, trigger DSA downgrade");
            this.mIsQoeException = true;
            shouldDeprioritize5GToLte();
            this.mIsQoeException = false;
            this.mIsLinkNotReliable = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLinkReliabilityTimeout() {
        this.mIsLinkNotReliable = false;
    }

    class SmartSwitcherHandler extends Handler {
        SmartSwitcherHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                int i = message.what;
                if (i == 3001) {
                    HighSpeedTrainSmartSwitcher.this.handleHighSpeedLineChanged((Intent) message.obj);
                    return;
                }
                if (i == 3002) {
                    HighSpeedTrainSmartSwitcher.this.handleHighSpeedTrainModeChanged((Intent) message.obj);
                    return;
                }
                if (i == 4001) {
                    HighSpeedTrainSmartSwitcher.this.handleForegroundInfoChanged((ForegroundInfo) message.obj);
                    return;
                }
                if (i == 5001) {
                    HighSpeedTrainSmartSwitcher.this.handleVideoDataStall();
                    return;
                }
                if (i != 6001) {
                    switch (i) {
                        case 1001:
                            HighSpeedTrainSmartSwitcher.this.handleConnectivityChange((Intent) message.obj);
                            break;
                        case 1002:
                            HighSpeedTrainSmartSwitcher.this.handleLinkReliabilityTimeout();
                            break;
                        case 1003:
                            HighSpeedTrainSmartSwitcher.this.mRestoreNrCheckCount++;
                            HighSpeedTrainSmartSwitcher.this.lambda$handleCallStateChanged$1();
                            break;
                        default:
                            switch (i) {
                                case 2001:
                                    HighSpeedTrainSmartSwitcher.this.handleCellLocationChanged(message.arg1, (CellLocation) message.obj);
                                    break;
                                case 2002:
                                    HighSpeedTrainSmartSwitcher.this.handleSignalStrengthChanged(message.arg1, (SignalStrength) message.obj);
                                    break;
                                case 2003:
                                    HighSpeedTrainSmartSwitcher.this.handleCallStateChanged(message.arg1, message.arg2);
                                    break;
                                case 2004:
                                    HighSpeedTrainSmartSwitcher.this.handleServiceStateChanged(message.arg1, (ServiceState) message.obj);
                                    break;
                                default:
                                    switch (i) {
                                        case 7001:
                                            HighSpeedTrainStats.finishSmartSwitchOptIfNeeded(true);
                                            break;
                                        case 7002:
                                            HighSpeedTrainStats.finishSmartSwitchOptIfNeeded(false);
                                            break;
                                        case 7003:
                                            Object obj = message.obj;
                                            boolean z = obj instanceof Integer;
                                            HighSpeedTrainSmartSwitcher highSpeedTrainSmartSwitcher = HighSpeedTrainSmartSwitcher.this;
                                            if (z) {
                                                highSpeedTrainSmartSwitcher.handleNetworkQoeChanged(message.arg1, message.arg2, ((Integer) obj).intValue());
                                            } else {
                                                highSpeedTrainSmartSwitcher.logd("EVENT_NETWORK_QOE_CHANGED: invalid msg.obj, skip");
                                            }
                                            break;
                                        default:
                                            HighSpeedTrainSmartSwitcher.this.logd("Unhandled message: " + message.what);
                                            break;
                                    }
                                    break;
                            }
                            break;
                    }
                    return;
                }
                HighSpeedTrainSmartSwitcher.this.tryForceLte2Nr();
                HighSpeedTrainSmartSwitcher.this.mLteWeakSignalTracking = false;
            } catch (Exception e) {
                Log.e("HighSpeedTrainSmartSwitcher", "Error handling message: " + message.what, e);
            }
        }
    }

    private void loadHighPriorityNrToLteCells() {
        SignalMapHSTCloudController signalMapHSTCloudController = SignalMapHSTCloudController.getInstance();
        if (signalMapHSTCloudController != null) {
            Set hSTNrToLteCellFromDb = signalMapHSTCloudController.getHSTNrToLteCellFromDb(4);
            if (hSTNrToLteCellFromDb != null) {
                this.mNeedSwitchNrToLteCellSet.addAll(hSTNrToLteCellFromDb);
                logd("Preloading cell set loaded, size: " + hSTNrToLteCellFromDb.size());
            }
            Set hSTNrToLteCellFromDb2 = signalMapHSTCloudController.getHSTNrToLteCellFromDb(5);
            if (hSTNrToLteCellFromDb2 != null) {
                this.mNeedSwitchNrToLteCellSet.addAll(hSTNrToLteCellFromDb2);
                logd("Low priority cell set loaded, size: " + hSTNrToLteCellFromDb2.size());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCallStateChanged(int i, int i2) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && MiuiPhoneUtils.isDefaultDataSlotId(i)) {
            boolean z = this.mIsInCall;
            this.mIsInCall = i2 != 0;
            if (this.mIsDeprioritizeNr && z && !this.mIsInCall) {
                this.mSmartSwitcherHandler.post(new Runnable() { // from class: com.android.phone.HighSpeedTrainSmartSwitcher$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleCallStateChanged$1();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleVideoInfo(Intent intent) {
        boolean z = intent.getIntExtra("render-quality", 1) == 0;
        this.mVideoQualityStall = z;
        if (shouldResetTracking()) {
            resetStallTracking();
        } else {
            handleVideoStallStateChange(z);
        }
    }

    private void handleVideoStallStateChange(boolean z) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (z) {
            handleVideoStallStart(jElapsedRealtime);
        } else {
            handleVideoStallEnd(jElapsedRealtime);
        }
    }

    private void handleVideoStallStart(long j) {
        if (this.mStallStartTime != -1) {
            return;
        }
        this.mStallStartTime = j;
        logd("Video stall started at: " + this.mStallStartTime);
        if (this.mIsContinuousTimerActive) {
            return;
        }
        this.mSmartSwitcherHandler.sendEmptyMessageDelayed(5001, 10000L);
        this.mIsContinuousTimerActive = true;
    }

    private void handleVideoStallEnd(long j) {
        if (this.mStallStartTime == -1) {
            return;
        }
        if (this.mIsContinuousTimerActive) {
            this.mSmartSwitcherHandler.removeMessages(5001);
            this.mIsContinuousTimerActive = false;
            logd("Continuous stall timer cancelled");
        }
        long j2 = j - this.mStallStartTime;
        logd("Video stall ended. Duration: " + j2 + "ms");
        if (isShortStall(j2)) {
            this.mShortStallRecords.add(Long.valueOf(j));
            trimElapsedRealtime(this.mShortStallRecords, 30000L);
            if (this.mShortStallRecords.size() >= 2) {
                tryForceLte2Nr();
            }
        }
        this.mStallStartTime = -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleVideoDataStall() {
        logd("handleVideoDataStall mIsHighSpeedTrainMode: " + this.mIsHighSpeedTrainMode + ", mVideoQualityStall: " + this.mVideoQualityStall + ", mIsNrNetworkType: " + this.mIsNrNetworkType);
        if (isHstModeAndNotJingHuLine()) {
            this.mContinuousVideoQualityStall = true;
            shouldDeprioritize5GToLte();
            this.mContinuousVideoQualityStall = false;
        }
        if (shouldResetTracking()) {
            resetStallTracking();
        } else {
            tryForceLte2Nr();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryForceLte2Nr() {
        if (shouldResetForce2NrTracking()) {
            resetLteSignalTracking();
            return;
        }
        trimElapsedRealtime(this.mForceLte2NrRecords, 1200000L);
        if (this.mForceLte2NrRecords.size() >= 2) {
            logd("Exceed max force times(2) in 20 minutes");
            if (this.mDdsPhoneId == -1 || !this.mIsLteNetworkType) {
                return;
            }
            this.mForceLte2NrLimitedCount++;
            localLog("tryForceLte2Nr", "LimitedCount:" + this.mForceLte2NrLimitedCount);
            return;
        }
        if (this.mDdsPhoneId == -1 || !this.mIsLteNetworkType) {
            return;
        }
        localLog("tryForceLte2Nr", "ForceLte2Nr");
        NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
        if (networkMetricsController == null) {
            return;
        }
        if (networkMetricsController.shouldAvoidByScene(NetworkMetricsController.Scene.HST_NW_WITHOUT_NR)) {
            logd("skip forceLte2Nr because shouldAvoidByScene(HST_NW_WITHOUT_NR) = true");
            return;
        }
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().forceLte2Nr(this.mDdsPhoneId);
        }
        this.mForceLte2NrRecords.add(Long.valueOf(SystemClock.elapsedRealtime()));
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "hst_smart_switch_force_lte2nr_event");
    }

    private boolean shouldResetForce2NrTracking() {
        return shouldResetTracking() || !this.mIsLteNetworkType;
    }

    private boolean shouldResetTracking() {
        return !this.mIsHighSpeedTrainMode || this.mIsDeprioritizeNr || !this.mIsForegroundApp || this.mIsWifiDataOn || isSaDeprioritized();
    }

    private boolean isSaDeprioritized() {
        return SystemProperties.getBoolean("ril.deprioritize.sa.slot" + this.mDdsPhoneId, false);
    }

    private void resetStallTracking() {
        this.mStallStartTime = -1L;
        this.mShortStallRecords.clear();
        if (this.mIsContinuousTimerActive) {
            this.mSmartSwitcherHandler.removeMessages(5001);
            this.mIsContinuousTimerActive = false;
        }
        logd("resetStallTracking Stall tracking reset");
    }

    private void trimElapsedRealtime(Queue queue, long j) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        while (!queue.isEmpty() && jElapsedRealtime - ((Long) queue.peek()).longValue() > j) {
            queue.poll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHighSpeedLineChanged(Intent intent) {
        if (intent == null) {
            return;
        }
        Map map = (Map) intent.getSerializableExtra("mHitHstLineStatus");
        if (map != null) {
            String strJoin = TextUtils.join(",", map.keySet());
            logd("hstLine = " + strJoin);
            this.mIsJingGuangHstLine = strJoin.contains("G79");
            this.mIsJingHuHstLine = strJoin.contains("G21");
        }
        this.mIsHighSpeedTrainLine = this.mIsJingHuHstLine || this.mIsJingGuangHstLine;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHighSpeedTrainModeChanged(Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra("mIsHstMode", false);
        if (this.mIsHighSpeedTrainMode != booleanExtra) {
            this.mIsHighSpeedTrainMode = booleanExtra;
            if (booleanExtra) {
                return;
            }
            resetLteSignalTracking();
            this.mDeprioritizeNrLimitedCount = 0;
            this.mForceLte2NrLimitedCount = 0;
            this.mDeprioritizeNrRecords.clear();
            this.mForceLte2NrRecords.clear();
            resetStallTracking();
            this.mLastCellId = Long.MAX_VALUE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCellLocationChanged(int i, CellLocation cellLocation) {
        handleTestCellIdMode(i, cellLocation);
        if (this.mIsHighSpeedTrainMode && MiuiPhoneUtils.isDefaultDataSlotId(i)) {
            CellIdentity currentCellIdentity = MiuiPhoneUtils.getPhone(i).getCurrentCellIdentity();
            if (cellLocation == null || cellLocation.isEmpty() || currentCellIdentity == null) {
                return;
            }
            if (!(currentCellIdentity instanceof CellIdentityNr)) {
                this.mIsBlacklistedCell = false;
                return;
            }
            long nci = ((CellIdentityNr) currentCellIdentity).getNci();
            if (isCellIdInvalid(nci, this.mLastCellId)) {
                return;
            }
            this.mLastCellId = nci;
            if (checkInitialConditions(i, cellLocation)) {
                checkCellSwitchConditions(nci);
            }
        }
    }

    private boolean checkInitialConditions(int i, CellLocation cellLocation) {
        return this.mIsJingGuangHstLine && this.mIsNrNetworkType && MiuiPhoneUtils.isValidatePhoneId(i) && MiuiPhoneUtils.isDefaultDataSlotId(i);
    }

    private boolean isCellIdInvalid(long j, long j2) {
        return j == j2 || j == ((long) this.UNAVAILABLE) || j == Long.MAX_VALUE;
    }

    private void handleTestCellIdMode(int i, CellLocation cellLocation) {
        if (this.mTestCellIdMode) {
            logd("handleCellLocationChanged cellId: " + this.mTestCellId);
            long j = this.mTestCellId;
            this.mLastCellId = j;
            checkCellSwitchConditions(j);
        }
    }

    private void checkCellSwitchConditions(long j) {
        if (this.mIsJingGuangHstLine && this.mNeedSwitchNrToLteCellSet.isEmpty()) {
            loadHighPriorityNrToLteCells();
        }
        String strValueOf = String.valueOf(j);
        boolean z = !this.mNeedSwitchNrToLteCellSet.isEmpty() && this.mNeedSwitchNrToLteCellSet.contains(strValueOf);
        if (PhoneDebug.VDBG || this.mDebugLog) {
            logd("HighSpeedTrainCurrentCellIdentity: " + strValueOf + ", isBlacklisted: " + z + ", mIsJingGuangHstLine: " + this.mIsJingGuangHstLine);
        }
        if (this.mIsBlacklistedCell != z) {
            this.mIsBlacklistedCell = z;
            if (z) {
                shouldDeprioritize5GToLte();
            }
        }
    }

    private void shouldDeprioritize5GToLte() {
        trimElapsedRealtime(this.mDeprioritizeNrRecords, 1200000L);
        boolean z = this.mIsNrNetworkType && !this.mIsDeprioritizeNr && this.mIsForegroundApp && !this.mIsWifiDataOn && this.mIsMobileDataOn && !this.mIsInCall && checkDegrade5GToLte();
        if (this.mDeprioritizeNrRecords.size() >= 3) {
            if (z) {
                this.mDeprioritizeNrLimitedCount++;
                printDegrade5GLog(String.format("LimitedDeprioritizeNrCount: %d", Integer.valueOf(this.mDeprioritizeNrLimitedCount)));
                return;
            }
            return;
        }
        if (z) {
            printDegrade5GLog("");
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().enableSa(false, this.mDdsPhoneId);
                if (!FiveGUtils.isXmRadioSupported()) {
                    NetworkRecoveryStrategyBase.getInstance().enableNsa(false, this.mDdsPhoneId);
                }
            }
            this.mDeprioritizeNrRecords.add(Long.valueOf(SystemClock.elapsedRealtime()));
            this.mDeprioritizeDdsPhoneId = this.mDdsPhoneId;
            this.mIsDeprioritizeNr = true;
            SmartSwitcherHandler smartSwitcherHandler = this.mSmartSwitcherHandler;
            smartSwitcherHandler.sendMessageDelayed(smartSwitcherHandler.obtainMessage(1003), 10000L);
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "hst_smart_switch_deprioritize_nr_event");
            HighSpeedTrainStats.beginSmartSwitchOpt();
            this.mSmartSwitcherHandler.removeMessages(7002);
            SmartSwitcherHandler smartSwitcherHandler2 = this.mSmartSwitcherHandler;
            smartSwitcherHandler2.sendMessageDelayed(smartSwitcherHandler2.obtainMessage(7002), 30000L);
            this.mSmartSwitcherHandler.removeMessages(7001);
        }
    }

    private boolean checkDegrade5GToLte() {
        return this.mIsHstLinkNotReliable || this.mContinuousVideoQualityStall || this.mModemMcsStall || this.mIsQoeException || (this.mIsBlacklistedCell && (this.mIsLinkNotReliable || this.mVideoQualityStall));
    }

    private void printDegrade5GLog(String str) {
        String str2 = String.format("%s | mIsNrNetworkType: %b | mIsDeprioritizeNr: %b | mIsForegroundApp: %b | mIsBlacklistedCell: %b | mCurrentNrRsrp: %d | mCurrentNrSnr: %d | mIsHstLinkNotReliable: %b | mModemMcsStall: %b | mIsQoeException: %b | mIsLinkNotReliable: %b | mVideoQualityStall: %b | mIsHighSpeedTrainMode: %b | mIsWifiDataOn: %b | mIsMobileDataOn: %b | mIsInCall: %b | mDeprioritizeNrRecords: %d", str, Boolean.valueOf(this.mIsNrNetworkType), Boolean.valueOf(this.mIsDeprioritizeNr), Boolean.valueOf(this.mIsForegroundApp), Boolean.valueOf(this.mIsBlacklistedCell), Integer.valueOf(this.mCurrentNrRsrp), Integer.valueOf(this.mCurrentNrSnr), Boolean.valueOf(this.mIsHstLinkNotReliable), Boolean.valueOf(this.mModemMcsStall), Boolean.valueOf(this.mIsQoeException), Boolean.valueOf(this.mIsLinkNotReliable), Boolean.valueOf(this.mVideoQualityStall), Boolean.valueOf(this.mIsHighSpeedTrainMode), Boolean.valueOf(this.mIsWifiDataOn), Boolean.valueOf(this.mIsMobileDataOn), Boolean.valueOf(this.mIsInCall), Integer.valueOf(this.mDeprioritizeNrRecords.size()));
        if (!TextUtils.isEmpty(str)) {
            localLog("shouldDeprioritize5GToLte", str2);
        } else {
            localLog("Deprioritize5GToLte", str2);
        }
    }

    private void printRestore5GLog(boolean z) {
        String str = "mIsNrNetworkType: " + this.mIsNrNetworkType + ", mIsDeprioritizeNr: " + this.mIsDeprioritizeNr + ", mIsForegroundApp: " + this.mIsForegroundApp + ", mCurrentLteRsrp: " + this.mCurrentLteRsrp + ", mCurrentLteSnr: " + this.mCurrentLteSnr + ", mIsWifiDataOn: " + this.mIsWifiDataOn + ", mIsMobileDataOn: " + this.mIsMobileDataOn + ", mIsInCall: " + this.mIsInCall + ", mRestoreNrCheckCount: " + this.mRestoreNrCheckCount;
        if (z) {
            localLog("RestoreLteTo5G", str);
        } else {
            localLog("shouldRestoreLteTo5G", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: shouldRestoreLteTo5G, reason: merged with bridge method [inline-methods] */
    public void lambda$handleCallStateChanged$1() {
        printRestore5GLog(false);
        boolean z = this.mIsNrNetworkType;
        boolean z2 = this.mIsDeprioritizeNr;
        if (z) {
            if (z2) {
                forceRestoreTo5G();
            }
        } else if (z2) {
            int i = this.mRestoreNrCheckCount;
            if (i >= 3) {
                forceRestoreTo5G();
                return;
            }
            reevaluateRestoreLteTo5G(i);
            if (this.mIsDeprioritizeNr) {
                this.mSmartSwitcherHandler.removeMessages(1003);
                SmartSwitcherHandler smartSwitcherHandler = this.mSmartSwitcherHandler;
                smartSwitcherHandler.sendMessageDelayed(smartSwitcherHandler.obtainMessage(1003), MirrorDesktopHelper.TIMEOUT_MILLIS);
            }
        }
    }

    private void reevaluateRestoreLteTo5G(int i) {
        int i2;
        if (this.mIsInCall) {
            return;
        }
        if (isRestoreLteTo5G(i) && this.mIsForegroundApp && !this.mIsWifiDataOn && ((i2 = this.mDeprioritizeDdsPhoneId) == -1 || i2 == this.mDdsPhoneId)) {
            return;
        }
        forceRestoreTo5G();
    }

    private boolean isRestoreLteTo5G(int i) {
        if (i == 1) {
            return isRestoreLteTo5GLevel1();
        }
        if (i == 2) {
            return isRestoreLteTo5GLevel2();
        }
        return false;
    }

    private boolean isRestoreLteTo5GLevel1() {
        int i;
        int i2 = this.mCurrentLteRsrp;
        int i3 = this.UNAVAILABLE;
        return i2 != i3 && i2 > -90 && (i = this.mCurrentLteSnr) != i3 && i > 10;
    }

    private boolean isRestoreLteTo5GLevel2() {
        int i;
        int i2 = this.mCurrentLteRsrp;
        int i3 = this.UNAVAILABLE;
        return i2 != i3 && i2 > -80 && (i = this.mCurrentLteSnr) != i3 && i > 15;
    }

    private void forceRestoreTo5G() {
        if (this.mIsInCall) {
            return;
        }
        printRestore5GLog(true);
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            if (!FiveGUtils.isXmRadioSupported()) {
                NetworkRecoveryStrategyBase.getInstance().enableNsa(true, this.mDeprioritizeDdsPhoneId);
            }
            NetworkRecoveryStrategyBase.getInstance().enableSa(true, this.mDeprioritizeDdsPhoneId);
        }
        this.mIsDeprioritizeNr = false;
        this.mDeprioritizeDdsPhoneId = -1;
        this.mRestoreNrCheckCount = 0;
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "hst_smart_switch_restore_nr_event");
        this.mSmartSwitcherHandler.removeMessages(1003);
        this.mSmartSwitcherHandler.removeMessages(7001);
        this.mSmartSwitcherHandler.removeMessages(7002);
        HighSpeedTrainStats.finishSmartSwitchOptIfNeeded(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDataSlotChanged() {
        int defaultDataSlotId = getDefaultDataSlotId();
        logd("mDdsPhoneId=" + this.mDdsPhoneId + " newDdsPhoneId=" + defaultDataSlotId);
        if (defaultDataSlotId != this.mDdsPhoneId) {
            this.mDdsPhoneId = defaultDataSlotId;
            this.mDeprioritizeNrRecords.clear();
            this.mDeprioritizeNrLimitedCount = 0;
            this.mForceLte2NrLimitedCount = 0;
            resetLteSignalTracking();
        }
        lambda$handleCallStateChanged$1();
    }

    private int getDefaultDataSlotId() {
        DefaultSimManager.getInstance();
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSignalStrengthChanged(int i, SignalStrength signalStrength) {
        if (this.mTestSignalMode) {
            handleTestSignalMode();
            return;
        }
        if (MiuiPhoneUtils.isValidatePhoneId(i) && MiuiPhoneUtils.isDefaultDataSlotId(i)) {
            for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
                if (cellSignalStrength instanceof CellSignalStrengthLte) {
                    CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) cellSignalStrength;
                    this.mCurrentLteRsrp = inRangeOrUnavailable(cellSignalStrengthLte.getRsrp(), -140, -43);
                    this.mCurrentLteSnr = inRangeOrUnavailable(cellSignalStrengthLte.getRssnr(), -20, 30);
                    this.mCurrentLteRsrq = inRangeOrUnavailable(cellSignalStrengthLte.getRsrq(), -34, 3);
                    int i2 = this.UNAVAILABLE;
                    this.mCurrentNrSnr = i2;
                    this.mCurrentNrRsrp = i2;
                    int i3 = this.mRestoreNrCheckCount;
                    if (i3 == 1 || i3 == 2) {
                        reevaluateRestoreLteTo5G(i3);
                    }
                    handleLteSignalTracking();
                } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                    CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrength;
                    this.mCurrentNrRsrp = inRangeOrUnavailable(cellSignalStrengthNr.getSsRsrp(), -140, -43);
                    this.mCurrentNrSnr = inRangeOrUnavailable(cellSignalStrengthNr.getSsSinr(), -23, 23);
                    int i4 = this.UNAVAILABLE;
                    this.mCurrentLteRsrp = i4;
                    this.mCurrentLteSnr = i4;
                    this.mCurrentLteRsrq = i4;
                } else {
                    resetSignalValues();
                }
            }
        }
    }

    private void resetSignalValues() {
        int i = this.UNAVAILABLE;
        this.mCurrentLteRsrp = i;
        this.mCurrentNrRsrp = i;
        this.mCurrentNrSnr = i;
        this.mCurrentLteSnr = i;
        this.mCurrentLteRsrq = i;
    }

    private void handleLteSignalTracking() {
        if (shouldResetForce2NrTracking()) {
            resetLteSignalTracking();
            return;
        }
        int i = this.mCurrentLteRsrp;
        int i2 = this.UNAVAILABLE;
        boolean z = i != i2 && i < -112;
        int i3 = this.mCurrentLteRsrq;
        boolean z2 = z || (i3 != i2 && i3 < -15);
        logd("handleLteSignalTracking mCurrentLteRsrp: " + this.mCurrentLteRsrp + ", mCurrentLteSnr: " + this.mCurrentLteSnr + ", mCurrentLteRsrq: " + this.mCurrentLteRsrq + ", isLteWeakSignal: " + z2);
        if (z2) {
            if (this.mLteWeakSignalTracking) {
                return;
            }
            this.mSmartSwitcherHandler.sendEmptyMessageDelayed(6001, MirrorDesktopHelper.TIMEOUT_MILLIS);
            this.mLteWeakSignalTracking = true;
            return;
        }
        resetLteSignalTracking();
    }

    private void resetLteSignalTracking() {
        if (this.mLteWeakSignalTracking) {
            this.mSmartSwitcherHandler.removeMessages(6001);
            this.mLteWeakSignalTracking = false;
            logd("resetLteSignalTracking LTE weak signal tracking reset");
        }
    }

    private void handleTestSignalMode() {
        if (this.mTestIsNrCellType) {
            this.mCurrentNrRsrp = this.mTestNrRsrp;
            this.mCurrentNrSnr = this.mTestNrSnr;
            int i = this.UNAVAILABLE;
            this.mCurrentLteRsrq = i;
            this.mCurrentLteRsrp = i;
            this.mCurrentLteSnr = i;
            shouldDeprioritize5GToLte();
            return;
        }
        this.mCurrentLteRsrp = this.mTestLteRsrp;
        this.mCurrentLteSnr = this.mTestLteSnr;
        this.mCurrentLteRsrq = this.mTestLteRsrq;
        int i2 = this.UNAVAILABLE;
        this.mCurrentNrRsrp = i2;
        this.mCurrentNrSnr = i2;
        lambda$handleCallStateChanged$1();
        handleLteSignalTracking();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleServiceStateChanged(int i, ServiceState serviceState) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && MiuiPhoneUtils.isDefaultDataSlotId(i) && serviceState != null) {
            int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
            if (isNrNetworkType(rilDataRadioTechnology)) {
                this.mIsNrNetworkType = true;
                this.mIsLteNetworkType = false;
                this.mSmartSwitcherHandler.removeMessages(7001);
            } else {
                if (isLteNetworkType(rilDataRadioTechnology)) {
                    this.mIsLteNetworkType = true;
                    this.mIsNrNetworkType = false;
                    if (this.mIsDeprioritizeNr) {
                        this.mSmartSwitcherHandler.removeMessages(7001);
                        SmartSwitcherHandler smartSwitcherHandler = this.mSmartSwitcherHandler;
                        smartSwitcherHandler.sendMessageDelayed(smartSwitcherHandler.obtainMessage(7001), MirrorDesktopHelper.TIMEOUT_MILLIS);
                        return;
                    }
                    return;
                }
                this.mIsNrNetworkType = false;
                this.mIsLteNetworkType = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleForegroundInfoChanged(ForegroundInfo foregroundInfo) {
        if (this.mCurrentForegroundPackageName.equals(foregroundInfo.mForegroundPackageName)) {
            return;
        }
        this.mCurrentForegroundPackageName = foregroundInfo.mForegroundPackageName;
        logd("mCurrentForegroundPackageName: " + this.mCurrentForegroundPackageName);
        this.mIsForegroundApp = false;
        if (isInWhiteList(foregroundInfo.mForegroundPackageName)) {
            this.mIsForegroundApp = true;
        }
    }

    private boolean isInWhiteList(String str) {
        if (mForegroundAppWhitelist.size() == 0) {
            return false;
        }
        return ArrayUtils.contains(mForegroundAppWhitelist, str);
    }

    private void registerForegroundInfoListener() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mForegroundInfoListener);
    }

    private void registerNetworkQoeListener() {
        try {
            HyperCommManager hyperCommManager = HyperCommManager.getDefault();
            if (hyperCommManager == null) {
                logd("HyperCommManager is not available, skip NetworkQoeListener registration");
                return;
            }
            HstSceneAwareCallback hstSceneAwareCallback = new HstSceneAwareCallback(new HandlerExecutor(this.mSmartSwitcherHandler));
            this.mSceneAwareCallback = hstSceneAwareCallback;
            if (!hyperCommManager.registerSceneAwareCallback(hstSceneAwareCallback)) {
                logd("registerSceneAwareCallback failed");
                this.mSceneAwareCallback = null;
            } else {
                logd("registerSceneAwareCallback success");
            }
        } catch (Exception e) {
            logd("registerNetworkQoeListener failed: " + e.getMessage());
            this.mSceneAwareCallback = null;
        }
    }

    class HstSceneAwareCallback extends SceneAwareCallback implements SceneAwareCallback.NetworkQoeListener {
        HstSceneAwareCallback(Executor executor) {
            super(executor);
        }

        public void onNetworkQoeChanged(int i, int i2, int i3, int i4, int i5) {
            HighSpeedTrainSmartSwitcher.this.logd("onNetworkQoeChanged: slotId=" + i + ", pci=" + i2 + ", appType=" + i3 + ", reason=" + i4 + ", networkScore=" + i5);
            HighSpeedTrainSmartSwitcher.this.mSmartSwitcherHandler.obtainMessage(7003, i4, i5, Integer.valueOf(i)).sendToTarget();
        }
    }

    private boolean isCidInDsaWhitelist() {
        long j = this.mLastCellId;
        return j != Long.MAX_VALUE && DSA_DOWNGRADE_CID_WHITELIST.contains(Long.valueOf(j));
    }

    private boolean isHstDsaDowngradeAllowed() {
        return this.mIsHighSpeedTrainMode && isCidInDsaWhitelist();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetworkQoeChanged(int i, int i2, int i3) {
        int i4;
        if (i != 0 || (i4 = this.mDdsPhoneId) == -1 || i3 != i4 || i2 < 0 || i2 >= 75 || !isHstDsaDowngradeAllowed()) {
            return;
        }
        logd("handleNetworkQoeChanged: whitelisted CID matched, reason=CONGESTION_TYPE_UNCLASSIFIED, trigger DSA downgrade");
        this.mIsQoeException = true;
        shouldDeprioritize5GToLte();
        this.mIsQoeException = false;
    }

    private void registerTelephonyInfoCallBack() {
        if (this.mTelephonyInfoCallBack == null) {
            logd("Creating new TelephonyInfoCallBack");
            TelephonyInfoCallBack telephonyInfoCallBack = new TelephonyInfoCallBack();
            this.mTelephonyInfoCallBack = telephonyInfoCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyInfoCallBack);
        }
    }

    class TelephonyInfoCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyInfoCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            HighSpeedTrainSmartSwitcher.this.mSmartSwitcherHandler.obtainMessage(2002, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            HighSpeedTrainSmartSwitcher.this.mSmartSwitcherHandler.obtainMessage(2004, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            HighSpeedTrainSmartSwitcher.this.mSmartSwitcherHandler.obtainMessage(2001, i, 0, cellLocation).sendToTarget();
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            HighSpeedTrainSmartSwitcher.this.mSmartSwitcherHandler.obtainMessage(2003, i, i2).sendToTarget();
        }
    }

    public int inRangeOrUnavailable(int i, int i2, int i3) {
        return (i < i2 || i > i3) ? this.UNAVAILABLE : i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Log.d("HighSpeedTrainSmartSwitcher", str);
    }

    private static void localLog(String str, String str2) {
        localLog("HighSpeedTrainSmartSwitcher", str, str2);
    }

    private static void localLog(String str, String str2, String str3) {
        String str4 = str + "." + str2;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(250));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d(str, str2 + " " + str3);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("HighSpeedTrainSmartSwitcher dump start------");
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    printWriter.println((String) entry.getKey());
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.println("HighSpeedTrainSmartSwitcher dump end------");
        printWriter.flush();
    }
}
