package com.android.phone.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.TelephonyNetworkSpecifier;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.phone.FileUploadMonitor;
import com.android.phone.FiveGManagerBase;
import com.android.phone.FiveGServiceInfo;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.MobileNetworkProber;
import com.android.phone.ModuleDelegate;
import com.android.phone.NetworkMetricsController;
import com.android.phone.NetworkModeRecognition;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.ot.pubsub.util.v;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class NetworkSpeedTracker {
    private static boolean DBG = true;
    private static long DELTA_MAX_NETWORK_BYTE = 524288000;
    private static long MOBILE_DATA_LINGER_TIMER = 20000;
    private static boolean VDBG = false;
    private static boolean mIsFiveGHostedModeSupported;
    private static NetworkSpeedTracker sInstance;
    private long PENALTY_TIMER_15_MINUTES;
    private long PENALTY_TIMER_2_MINUTES;
    private boolean isInDdsChangeMonitorTimer;
    private AudioManager mAudioManager;
    private Calendar mCalendar;
    private RateInfo mCellDlRateInfo;
    private RateInfo mCellUlRateInfo;
    private ConnectivityManager mCm;
    private PhoneConstants.DataState mConnectState;
    private Context mContext;
    private long mCurrentDlRateBps;
    private RateBpsInfo mCurrentULRateInfo;
    private long mCurrentUlRateBps;
    private int mDataStallCount;
    private long mDataStallNotifyTime;
    private boolean mDataStallTest;
    private final BaseEvent.IEventListener mDisplayListener;
    private RateInfo mDlRateInfo;
    private DataStallPolicy mDsPolicy;
    private NetworkSpeedTrackerHandler mHandler;
    private int mHighSpeedThresholdForSpeedOnly;
    private RateInfo mHstTunnelDlRateInfo;
    private RateInfo mHstTunnelUlRateInfo;
    private final BroadcastReceiver mIntentReceiver;
    private boolean mIsCallStateIdle;
    private boolean mIsDummyTest;
    private boolean mIsLingerCallState;
    private boolean mIsScreenOn;
    private volatile boolean mIsSeningMmsOnNondds;
    private boolean mIsSpeedLimitToasted;
    private boolean mIsWeakSignal;
    private boolean mIsWechatVoipForgroud;
    private long mLastDataStallTime;
    private long mLastSpeedHighTime;
    private long mLastTimeForDsdaToDsdsToast;
    private long mLastTimeForRoamingChangeDdsNotify;
    private long mLastTimeForSmartSwithDualSimDialog;
    private int mLastUid;
    private int mLiveHighThroughputCount;
    private int[] mLteRsrp;
    private int[] mLteSnr;
    private HashMap mMaxNetworkAllByte;
    private HashMap mMaxNetworkDlByte;
    private HashMap mMaxNetworkUlByte;
    private long mMobileDataConnTime;
    private final ConnectivityManager.NetworkCallback mNetworkCallback;
    private NetworkModeRecognition mNetworkModeRecognition;
    private NetworkStats mNetworkStats;
    private int[] mNrRsrp;
    private int[] mNrSnr;
    PhoneStateListener mPhoneStateListener;
    private boolean mPollActive;
    private int[] mRoamingState;
    private ServiceState[] mServiceState;
    private SignalQosStatistic mSignalQosStatistic;
    private boolean mSignalpoorTest;
    private int mSmartSwithDualSimDialogCancelCount;
    ContentObserver mSpeedLimitCardToastObserver;
    private int mSpeedLimitCount;
    private ArrayList mSpeedListeners;
    private int mSpeedNoLimitCount;
    private Scene mSpeedScene;
    private TelephonyInfoCallBack mTelephonyInfoCallBack;
    private long mTestDlBps;
    private long mTestUlBps;
    private RateInfo mUlRateInfo;
    private RateInfo mUnsolDlRateInfo;
    private RateInfo mUnsolUlRateInfo;
    private int mUpdateInterval;
    private FiveGServiceInfo[] mVGInfo;
    private VGInfoListener mVGInfoListener;
    private int mVoipDirection;
    private long mWechatVoipForgroudTime;
    private HandlerThread mWorkThread;
    private static final int[] sSpeedThresholds = {0, 10, 100, SmartNetworkRelayScore.TRAFFIC_UNIT, 8192, 1048576, 8388608};
    private static List mSceneDsda2DsdsWhiteList = new ArrayList();
    private static List mSmartSwitchDualSimWhiteList = new ArrayList();
    private static final boolean IS_INDIA = "in".equalsIgnoreCase(SystemProperties.get("ro.miui.build.region"));
    private HashSet mNetworkSpeedRateListeners = new HashSet();
    private boolean mIsMobileDataOn = false;
    private int mDdsPhoneId = -1;
    private NetworkMetricsController mNetworkMetricsController = null;

    public interface ISpeedTrackerListener {
        void onSpeedChanged(long j, long j2, Scene scene);
    }

    private int inRangeOrUnavailable(int i, int i2, int i3) {
        if (i < i2 || i > i3) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    public enum Scene {
        DUMMY(0),
        SCG_SIGNAL_QOS(1),
        WECHAT_VOICE_RATIO_EXCEPTION(3),
        SPEED_LIMIT_CARD_EXCEPTION(4),
        SPEED_LIVE_SPEED_EXCEPTION(5),
        WECHAT_VOICE_SPEED_EXCEPTION(6),
        SPEED_ONLY(7);

        private int value;

        Scene(int i) {
            this.value = i;
        }
    }

    public class NetworkStats {
        public long rxBytes;
        public long rxPackets;
        public long time;
        public long txBytes;
        public long txPackets;

        public NetworkStats(NetworkSpeedTracker networkSpeedTracker) {
            clear();
        }

        public void updateTxRx() {
            this.txBytes = TrafficStats.getMobileTxBytes();
            this.rxBytes = TrafficStats.getMobileRxBytes();
            this.txPackets = TrafficStats.getMobileTxPackets();
            this.rxPackets = TrafficStats.getMobileRxPackets();
            this.time = System.currentTimeMillis();
        }

        public NetworkStats(NetworkSpeedTracker networkSpeedTracker, NetworkStats networkStats) {
            this.txBytes = networkStats.txBytes;
            this.rxBytes = networkStats.rxBytes;
            this.txPackets = networkStats.txPackets;
            this.rxPackets = networkStats.rxPackets;
            this.time = networkStats.time;
        }

        public void clear() {
            this.txBytes = 0L;
            this.rxBytes = 0L;
            this.txPackets = 0L;
            this.rxPackets = 0L;
            this.time = 0L;
        }

        public String toString() {
            return "txBytes=" + this.txBytes + " rxBytes=" + this.rxBytes + " time=" + this.time + "txPackets=" + this.txPackets + "rxPackets=" + this.rxPackets;
        }
    }

    public class SignalQosStatistic {
        private LinkedList mWeakSignalList = new LinkedList();
        public int scgSignalRatio;
        public int scgWeakSignalCn;
        public int totalSignalCn;

        public SignalQosStatistic() {
            clear();
        }

        public boolean isScgSignalPoor() {
            return this.scgSignalRatio >= HyperOSCustFeatureResolve.getInt("config_weak_signal_sampling_rate", 80);
        }

        public void pollUpdateSignalMetrics() {
            this.mWeakSignalList.add(Boolean.valueOf(NetworkSpeedTracker.this.mIsWeakSignal));
            this.scgWeakSignalCn = ((List) this.mWeakSignalList.stream().filter(new Predicate() { // from class: com.android.phone.utils.NetworkSpeedTracker$SignalQosStatistic$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return NetworkSpeedTracker.SignalQosStatistic.$r8$lambda$3UGaBNpTnBTorPTnHmon2QrHChE((Boolean) obj);
                }
            }).collect(Collectors.toList())).size();
            this.totalSignalCn = this.mWeakSignalList.size();
            if (NetworkSpeedTracker.VDBG) {
                NetworkSpeedTracker.log("pollUpdateSignalMetrics mWeakSignalList" + this.mWeakSignalList);
            }
            if (this.mWeakSignalList.size() > 10) {
                this.mWeakSignalList.remove(0);
            }
            if (this.mWeakSignalList.size() > 5) {
                try {
                    this.scgSignalRatio = (this.scgWeakSignalCn * 100) / this.totalSignalCn;
                } catch (ArithmeticException unused) {
                    NetworkSpeedTracker.log("ArithmeticException!!!");
                    clear();
                }
            }
        }

        public static /* synthetic */ boolean $r8$lambda$3UGaBNpTnBTorPTnHmon2QrHChE(Boolean bool) {
            return bool.booleanValue();
        }

        public void clear() {
            this.scgWeakSignalCn = 0;
            this.totalSignalCn = 0;
            this.scgSignalRatio = 0;
            this.mWeakSignalList.clear();
        }

        public String toString() {
            return "scgWeakSignalCn=" + this.scgWeakSignalCn + " totalSignalCn=" + this.totalSignalCn + " scgSignalRatio=" + this.scgSignalRatio;
        }
    }

    class RateInfo {
        long peakRateBps = 0;
        long peakRateBpsSecond = 0;

        RateInfo(NetworkSpeedTracker networkSpeedTracker) {
        }

        void updateRate(long j) {
            long j2 = this.peakRateBps;
            if (j > j2) {
                this.peakRateBpsSecond = j2;
                this.peakRateBps = j;
            } else {
                if (j <= this.peakRateBpsSecond || j == j2) {
                    return;
                }
                this.peakRateBpsSecond = j;
            }
        }

        long getPeakRateBpsSecond() {
            return this.peakRateBpsSecond;
        }

        void reset() {
            this.peakRateBps = 0L;
            this.peakRateBpsSecond = 0L;
        }
    }

    public static synchronized NetworkSpeedTracker getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new NetworkSpeedTracker();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private NetworkSpeedTracker() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mVGInfo = new FiveGServiceInfo[i];
        this.mServiceState = new ServiceState[i];
        this.mLteRsrp = new int[i];
        this.mLteSnr = new int[i];
        this.mNrRsrp = new int[i];
        this.mNrSnr = new int[i];
        this.mIsWeakSignal = false;
        this.mSignalpoorTest = false;
        this.mDataStallTest = false;
        this.mDataStallNotifyTime = 0L;
        this.mTelephonyInfoCallBack = null;
        this.mLastDataStallTime = 0L;
        this.mLastSpeedHighTime = 0L;
        this.PENALTY_TIMER_2_MINUTES = 120000L;
        this.PENALTY_TIMER_15_MINUTES = 900000L;
        this.mMaxNetworkUlByte = new HashMap();
        this.mMaxNetworkDlByte = new HashMap();
        this.mMaxNetworkAllByte = new HashMap();
        this.mMobileDataConnTime = 0L;
        this.mSpeedListeners = new ArrayList();
        this.mDataStallCount = 0;
        this.mIsWechatVoipForgroud = false;
        this.mWechatVoipForgroudTime = 0L;
        this.mSpeedLimitCount = 0;
        this.mSpeedNoLimitCount = 0;
        this.mIsSpeedLimitToasted = false;
        this.mSpeedScene = Scene.DUMMY;
        this.mDsPolicy = new DataStallPolicy();
        this.mIsSeningMmsOnNondds = false;
        this.mTestUlBps = 0L;
        this.mTestDlBps = 0L;
        this.mIsDummyTest = false;
        this.mIsCallStateIdle = true;
        this.mIsLingerCallState = false;
        this.mVoipDirection = 1;
        this.mConnectState = PhoneConstants.DataState.DISCONNECTED;
        this.mCurrentUlRateBps = 0L;
        this.mCurrentDlRateBps = 0L;
        this.mCurrentULRateInfo = new RateBpsInfo(this);
        this.mLastTimeForDsdaToDsdsToast = 0L;
        this.mLiveHighThroughputCount = 0;
        this.mLastUid = -1;
        this.mAudioManager = null;
        this.mNetworkModeRecognition = null;
        this.mLastTimeForRoamingChangeDdsNotify = 0L;
        this.mRoamingState = new int[i];
        this.mUlRateInfo = new RateInfo(this);
        this.mDlRateInfo = new RateInfo(this);
        this.mUnsolUlRateInfo = new RateInfo(this);
        this.mUnsolDlRateInfo = new RateInfo(this);
        this.mCellUlRateInfo = new RateInfo(this);
        this.mCellDlRateInfo = new RateInfo(this);
        this.mHstTunnelUlRateInfo = new RateInfo(this);
        this.mHstTunnelDlRateInfo = new RateInfo(this);
        this.mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.utils.NetworkSpeedTracker.1
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                    if (displayEvent.isDefaultDisplay()) {
                        NetworkSpeedTracker.this.mIsScreenOn = displayEvent.isScreenOn();
                        NetworkSpeedTracker.log("onDisplayChanged mIsScreenOn=" + NetworkSpeedTracker.this.mIsScreenOn);
                        NetworkSpeedTracker.this.mHandler.obtainMessage(4).sendToTarget();
                    }
                }
            }
        };
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.utils.NetworkSpeedTracker.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                NetworkSpeedTracker.log("receive action".concat(action));
                if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    NetworkSpeedTracker.this.mHandler.obtainMessage(3, intent).sendToTarget();
                    return;
                }
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    NetworkSpeedTracker.this.mHandler.obtainMessage(6).sendToTarget();
                    return;
                }
                if (action.equals("networkSpeedTracker.intent.debugall")) {
                    NetworkSpeedTracker.this.mHandler.obtainMessage(100, intent).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.CLOUD_SMART_SWITCH_DONE")) {
                    NetworkSpeedTracker.log("onReceive ACTION_CLOUD_SMART_SWITCH_DONE ");
                    NetworkSpeedTracker.this.initConfigParams();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.CLOUD_DSDA_WHITE_LIST_UPDATE_DONE")) {
                    NetworkSpeedTracker.log("onReceive ACTION_CLOUD_DSDA_WHITE_LIST_UPDATE_DONE");
                    NetworkSpeedTracker.this.initDsdaWhiteList();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.CLOUD_SMART_SWITCH_WHITE_LIST_UPDATE_DONE")) {
                    NetworkSpeedTracker.log("onReceive ACTION_CLOUD_SMART_SWITCH_WHITE_LIST_UPDATE_DONE");
                    NetworkSpeedTracker.this.initSmartSwitchWhiteList();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.WECHAT_VOIP_SCENE")) {
                    NetworkSpeedTracker.this.mHandler.obtainMessage(16, Boolean.valueOf(intent.getBooleanExtra("wechatVoipEnabled", false))).sendToTarget();
                    return;
                }
                if (action.equals("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED")) {
                    boolean z = 3 == SystemProperties.getInt("ril.multisim.voice_capability", 0);
                    NetworkSpeedTracker.this.localLog("dsdaStateChanged", "isCurrentDsdaState:" + z);
                    if (z || NetworkSpeedTracker.this.mIsCallStateIdle) {
                        return;
                    }
                    NetworkSpeedTracker.this.mHandler.obtainMessage(11).sendToTarget();
                    return;
                }
                if (action.equals("android.intent.action.ANY_DATA_STATE")) {
                    NetworkSpeedTracker.this.mHandler.obtainMessage(12, intent).sendToTarget();
                } else if (action.equals("com.android.phone.intent.action.SMART_SWITCH_DUAL_SWITCH_ALERT_DIALOG")) {
                    NetworkSpeedTracker.this.mHandler.obtainMessage(13, intent).sendToTarget();
                }
            }
        };
        this.mPhoneStateListener = new PhoneStateListener() { // from class: com.android.phone.utils.NetworkSpeedTracker.3
            @Override // android.telephony.PhoneStateListener
            public void onDataActivity(int i2) {
            }

            @Override // android.telephony.PhoneStateListener
            public void onCallStateChanged(int i2, String str) {
                NetworkSpeedTracker.this.mHandler.obtainMessage(7, Integer.valueOf(i2)).sendToTarget();
            }
        };
        this.mSpeedLimitCardToastObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.android.phone.utils.NetworkSpeedTracker.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                if (MiuiPhoneUtils.isValidatePhoneId(NetworkSpeedTracker.this.mDdsPhoneId)) {
                    NetworkSpeedTracker networkSpeedTracker = NetworkSpeedTracker.this;
                    ContentResolver contentResolver = networkSpeedTracker.mContext.getContentResolver();
                    StringBuilder sb = new StringBuilder("speedLimitCardToast");
                    sb.append(NetworkSpeedTracker.this.mDdsPhoneId);
                    networkSpeedTracker.mIsSpeedLimitToasted = Settings.Global.getInt(contentResolver, sb.toString(), 0) == 1;
                    NetworkSpeedTracker.log("onChange mIsSpeedLimitToasted=" + NetworkSpeedTracker.this.mIsSpeedLimitToasted + ", mDdsPhoneId=" + NetworkSpeedTracker.this.mDdsPhoneId);
                }
            }
        };
        this.mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.phone.utils.NetworkSpeedTracker.5
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                NetworkSpeedTracker.log("onCapabilitiesChanged");
                NetworkSpeedTracker.this.mHandler.obtainMessage(10, networkCapabilities).sendToTarget();
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                NetworkSpeedTracker.log("onLost");
                NetworkSpeedTracker.this.mHandler.obtainMessage(10).sendToTarget();
            }
        };
        log("NetworkSpeedTracker init");
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("NetworkSpeedTracker", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        NetworkSpeedTrackerHandler networkSpeedTrackerHandler = new NetworkSpeedTrackerHandler(this.mWorkThread.getLooper());
        this.mHandler = networkSpeedTrackerHandler;
        networkSpeedTrackerHandler.sendEmptyMessage(1);
    }

    private void initValues() {
        this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
        this.mPollActive = false;
        this.mDdsPhoneId = getDefaultDataSlotId();
        this.mNetworkStats = new NetworkStats(this);
        this.mSignalQosStatistic = new SignalQosStatistic();
        if (MiuiPhoneUtils.isSubwayOptimizationSupported()) {
            this.mNetworkModeRecognition = NetworkModeRecognition.getInstance();
        }
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            StringBuilder sb = new StringBuilder("speedLimitCardToast");
            sb.append(this.mDdsPhoneId);
            this.mIsSpeedLimitToasted = Settings.Global.getInt(contentResolver, sb.toString(), 0) == 1;
        }
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        fetchSmartSwithDualSimDialogCount();
        mIsFiveGHostedModeSupported = MiuiPhoneUtils.isFiveGHostedModeSupported();
        log("NetworkSpeedTracker mIsFiveGHostedModeSupported: " + mIsFiveGHostedModeSupported);
        this.isInDdsChangeMonitorTimer = false;
    }

    private boolean getFiveGHostedModeStatus() {
        return ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getFiveGHostedModeStatus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initConfigParams() {
        this.mUpdateInterval = FiveGManagerBase.getSpeedIntervalByCloud();
        this.mHighSpeedThresholdForSpeedOnly = FiveGManagerBase.getSpeedOnlyHighSpeedByCloud();
        if (VDBG) {
            log("initConfigParams interval[" + this.mUpdateInterval + "], mHighSpeedThresholdForSpeedOnly[" + this.mHighSpeedThresholdForSpeedOnly + "]");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initDsdaWhiteList() {
        if (mSceneDsda2DsdsWhiteList.size() > 0) {
            mSceneDsda2DsdsWhiteList.clear();
        }
        List dsda2DsdsWhiteListByCloud = FiveGManagerBase.getDsda2DsdsWhiteListByCloud(this.mContext);
        mSceneDsda2DsdsWhiteList = dsda2DsdsWhiteListByCloud;
        if (!VDBG || dsda2DsdsWhiteListByCloud.size() == 0) {
            return;
        }
        log("mSceneDsda2DsdsWhiteList=" + mSceneDsda2DsdsWhiteList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initSmartSwitchWhiteList() {
        if (mSmartSwitchDualSimWhiteList.size() > 0) {
            mSmartSwitchDualSimWhiteList.clear();
        }
        List smartSwitchWhiteListByCloud = FiveGManagerBase.getSmartSwitchWhiteListByCloud(this.mContext);
        mSmartSwitchDualSimWhiteList = smartSwitchWhiteListByCloud;
        if (!VDBG || smartSwitchWhiteListByCloud.size() == 0) {
            return;
        }
        log("mSmartSwitchDualSimWhiteList=" + mSmartSwitchDualSimWhiteList);
    }

    private void postUpdateNetworkRate() {
        postUpdateNetworkRateDelay(0L);
    }

    private void postUpdateNetworkRateDelay(long j) {
        NetworkSpeedTrackerHandler networkSpeedTrackerHandler = this.mHandler;
        if (networkSpeedTrackerHandler != null) {
            networkSpeedTrackerHandler.removeMessages(2);
            if (this.mIsScreenOn && this.mIsMobileDataOn) {
                this.mHandler.sendEmptyMessageDelayed(2, j);
                return;
            }
            if (DBG) {
                log("postUpdateNetworkRateDelay mIsScreenOn=" + this.mIsScreenOn + ",mIsMobileDataOn=" + this.mIsMobileDataOn);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkRate() {
        this.mPollActive = true;
        NetworkStats networkStats = new NetworkStats(this, this.mNetworkStats);
        this.mNetworkStats.updateTxRx();
        NetworkStats networkStats2 = this.mNetworkStats;
        long j = networkStats2.txBytes - networkStats.txBytes;
        long j2 = networkStats2.rxBytes - networkStats.rxBytes;
        if (VDBG && this.mIsDummyTest) {
            j = this.mTestUlBps;
            j2 = this.mTestDlBps;
        }
        long j3 = networkStats.time;
        if (j3 != 0 && networkStats2.time > j3 && j >= 0 && j2 >= 0) {
            int i = this.mUpdateInterval;
            long j4 = (j * 1000) / ((long) i);
            this.mCurrentUlRateBps = j4;
            long j5 = (1000 * j2) / ((long) i);
            this.mCurrentDlRateBps = j5;
            this.mCurrentULRateInfo.update(j4, System.currentTimeMillis());
            updateUlAndDlRate(this.mCurrentUlRateBps, this.mCurrentDlRateBps);
            this.mSignalQosStatistic.pollUpdateSignalMetrics();
            processDataStall(j4, j5);
            monitorSpeedRateChange(j4, j5);
            recordMaxNetworkByte(j, j2);
        }
        postUpdateNetworkRateDelay(this.mUpdateInterval);
    }

    private void updateUlAndDlRate(long j, long j2) {
        updateUlRate(j);
        updateDlRate(j2);
        updateCellUlRate(j);
        updateCellDlRate(j2);
        updateUnsolUlRate(j);
        updateUnsolDlRate(j2);
        updateHstTunnelUlRate(j);
        updateHstTunnelDlRate(j2);
    }

    public void updateUlRate(long j) {
        this.mUlRateInfo.updateRate(j);
    }

    public void updateDlRate(long j) {
        this.mDlRateInfo.updateRate(j);
    }

    public void updateUnsolUlRate(long j) {
        this.mUnsolUlRateInfo.updateRate(j);
    }

    public void updateUnsolDlRate(long j) {
        this.mUnsolDlRateInfo.updateRate(j);
    }

    public void updateCellUlRate(long j) {
        this.mCellUlRateInfo.updateRate(j);
    }

    public void updateCellDlRate(long j) {
        this.mCellDlRateInfo.updateRate(j);
    }

    public long getCurrentDlRateBps() {
        return this.mCurrentDlRateBps;
    }

    public long getCurrentUlRateBps() {
        return this.mCurrentUlRateBps;
    }

    public void updateHstTunnelUlRate(long j) {
        this.mHstTunnelUlRateInfo.updateRate(j);
    }

    public void updateHstTunnelDlRate(long j) {
        this.mHstTunnelDlRateInfo.updateRate(j);
    }

    public long getPeakUlRateBps() {
        return this.mUlRateInfo.getPeakRateBpsSecond();
    }

    public long getPeakDlRateBps() {
        return this.mDlRateInfo.getPeakRateBpsSecond();
    }

    public long getUnsolPeakUlRateBps() {
        return this.mUnsolUlRateInfo.getPeakRateBpsSecond();
    }

    public long getUnsolPeakDlRateBps() {
        return this.mUnsolDlRateInfo.getPeakRateBpsSecond();
    }

    public long getHstTunnelPeakUlRateBps() {
        return this.mHstTunnelUlRateInfo.getPeakRateBpsSecond();
    }

    public long getHstTunnelPeakDlRateBps() {
        return this.mHstTunnelDlRateInfo.getPeakRateBpsSecond();
    }

    public void clearPeakRateBps() {
        this.mUlRateInfo.reset();
        this.mDlRateInfo.reset();
    }

    public void clearUnsolPeakRateBps() {
        this.mUnsolUlRateInfo.reset();
        this.mUnsolDlRateInfo.reset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearCellPeakRateBps() {
        this.mCellUlRateInfo.reset();
        this.mCellDlRateInfo.reset();
    }

    public void clearHstTunnelPeakRateBps() {
        log("clearHstTunnelPeakRateBps");
        this.mHstTunnelUlRateInfo.reset();
        this.mHstTunnelDlRateInfo.reset();
    }

    /* JADX WARN: Code duplicated, block: B:44:0x00a7  */
    /* JADX WARN: Code duplicated, block: B:55:0x00ce  */
    /* JADX WARN: Code duplicated, block: B:57:0x00d6  */
    /* JADX WARN: Code duplicated, block: B:65:? A[RETURN, SYNTHETIC] */
    private void processDataStall(long j, long j2) {
        NetworkMetricsController networkMetricsController;
        Scene scene;
        Scene scene2;
        if (getFiveGHostedModeStatus() && (networkMetricsController = this.mNetworkMetricsController) != null && !networkMetricsController.getTopAppName().equals("com.miui.home") && this.mNetworkMetricsController.isLteNrNwType(getCurrentRat()) && isAllowedToCheckException()) {
            Scene scene3 = Scene.DUMMY;
            int[] iArr = sSpeedThresholds;
            boolean zIsDataStallException = isDataStallException(getSpeedLevel(j * 8, iArr), getSpeedLevel(8 * j2, iArr));
            if ((zIsDataStallException || this.mDataStallTest) && this.mDataStallCount <= 30 && System.currentTimeMillis() - this.mDataStallNotifyTime > 30000) {
                this.mDataStallNotifyTime = System.currentTimeMillis();
                dataStallNotification();
            }
            boolean z = true;
            if ((zIsDataStallException || this.mDataStallTest) && !this.mDsPolicy.isPolicyActiving() && (this.mSignalQosStatistic.isScgSignalPoor() || this.mSignalpoorTest)) {
                this.mDsPolicy.enablePolicy();
                scene = Scene.SCG_SIGNAL_QOS;
            } else {
                if (this.mNetworkMetricsController.isLteNwType(getCurrentRat())) {
                    int i = this.mHighSpeedThresholdForSpeedOnly;
                    if (j > i || j2 > i) {
                        scene = Scene.SPEED_ONLY;
                    }
                    if (Build.IS_INTERNATIONAL_BUILD || scene2 != Scene.SCG_SIGNAL_QOS || (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mVGInfo[this.mDdsPhoneId].isConnectedOnNsaMode() && MiuiPhoneUtils.isFivegHostedModeIndia())) {
                        if (this.mNetworkMetricsController.isHighSpeedTrain()) {
                            log("if on the high-speed train, ignore disable Sa or Endc or reattach");
                            z = false;
                        }
                        if (z) {
                            doRecoveryForFivegHostedMode(j, j2, scene2);
                            this.mDataStallCount = 0;
                            this.mSignalQosStatistic.clear();
                        }
                    }
                    return;
                }
                scene2 = scene3;
                z = false;
                if (Build.IS_INTERNATIONAL_BUILD) {
                }
                if (this.mNetworkMetricsController.isHighSpeedTrain()) {
                    log("if on the high-speed train, ignore disable Sa or Endc or reattach");
                    z = false;
                }
                if (z) {
                    doRecoveryForFivegHostedMode(j, j2, scene2);
                    this.mDataStallCount = 0;
                    this.mSignalQosStatistic.clear();
                }
            }
            scene2 = scene;
            if (Build.IS_INTERNATIONAL_BUILD) {
            }
            if (this.mNetworkMetricsController.isHighSpeedTrain()) {
                log("if on the high-speed train, ignore disable Sa or Endc or reattach");
                z = false;
            }
            if (z) {
                doRecoveryForFivegHostedMode(j, j2, scene2);
                this.mDataStallCount = 0;
                this.mSignalQosStatistic.clear();
            }
        }
    }

    private void doRecoveryForFivegHostedMode(long j, long j2, Scene scene) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (scene == Scene.SPEED_ONLY) {
            long j3 = this.mLastSpeedHighTime;
            if (j3 != 0 && jCurrentTimeMillis < j3 + 2700000) {
                log("has trigger speed high opt during lingering time");
                return;
            }
            long j4 = this.mLastDataStallTime;
            if (j4 == 0 || jCurrentTimeMillis <= this.PENALTY_TIMER_2_MINUTES + j4 || jCurrentTimeMillis >= j4 + 2700000) {
                return;
            }
            this.mLastSpeedHighTime = jCurrentTimeMillis;
            localLog("notifySpeedListenersIfNecessary", "scene:" + scene);
            notifySpeedListenersIfNecessary(j, j2, scene);
            return;
        }
        if (scene == Scene.SCG_SIGNAL_QOS) {
            long j5 = this.mLastDataStallTime;
            if (j5 != 0 && jCurrentTimeMillis < j5 + 2700000) {
                log("has trigger data stall opt during lingering time");
                return;
            }
            this.mLastDataStallTime = jCurrentTimeMillis;
            localLog("notifySpeedListenersIfNecessary", "scene:" + scene);
            notifySpeedListenersIfNecessary(j, j2, scene);
        }
    }

    class NetworkSpeedTrackerHandler extends Handler {
        NetworkSpeedTrackerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (NetworkSpeedTracker.VDBG) {
                NetworkSpeedTracker.log("handleMessage=" + message.what);
            }
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 1:
                        NetworkSpeedTracker.this.initAction();
                        break;
                    case 2:
                        NetworkSpeedTracker.this.updateNetworkRate();
                        break;
                    case 3:
                        NetworkSpeedTracker.this.processMobileStateChanged((Intent) message.obj);
                        break;
                    case 4:
                        NetworkSpeedTracker.this.onUpdateDeviceState(i);
                        break;
                    case 5:
                        NetworkSpeedTracker.this.processVGInfoChanged(message.arg1, (FiveGServiceInfo) message.obj);
                        break;
                    case 6:
                        if (NetworkSpeedTracker.this.getDefaultDataSlotId() != NetworkSpeedTracker.this.mDdsPhoneId) {
                            NetworkSpeedTracker networkSpeedTracker = NetworkSpeedTracker.this;
                            networkSpeedTracker.mDdsPhoneId = networkSpeedTracker.getDefaultDataSlotId();
                            NetworkSpeedTracker.this.mDsPolicy.disablePolicy();
                            NetworkSpeedTracker.this.unregisterSpeedLimitCardToastObserver();
                            NetworkSpeedTracker.this.registerSpeedLimitCardToastObserver();
                            NetworkSpeedTracker.this.resetSignalStrength();
                            if (NetworkSpeedTracker.this.isInDdsChangeMonitorTimer) {
                                NetworkSpeedTracker.this.recordRoamingChangeDds();
                                NetworkSpeedTracker.this.isInDdsChangeMonitorTimer = false;
                            }
                        }
                        break;
                    case 7:
                        NetworkSpeedTracker.this.processCallStateChanged(((Integer) message.obj).intValue());
                        break;
                    case 8:
                        NetworkSpeedTracker.this.onVoiceCallStateChanged();
                        break;
                    default:
                        switch (i) {
                            case 10:
                                NetworkSpeedTracker.this.handleMmsStateChanged(message);
                                break;
                            case 11:
                                long j = NetworkSpeedTracker.this.mCurrentUlRateBps;
                                long j2 = NetworkSpeedTracker.this.mCurrentDlRateBps;
                                if (NetworkSpeedTracker.DBG) {
                                    NetworkSpeedTracker.log("ulRateBpsInDsda=" + j + ", dlRateBpsInDsda=" + j2);
                                }
                                if (NetworkSpeedTracker.this.isRemindInDsda2Dsds(j, j2)) {
                                    new Handler().postDelayed(new Runnable() { // from class: com.android.phone.utils.NetworkSpeedTracker.NetworkSpeedTrackerHandler.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            if (NetworkSpeedTracker.this.mConnectState != PhoneConstants.DataState.DISCONNECTED || NetworkSpeedTracker.this.isLingerTimeForDsda2DsdsToast()) {
                                                return;
                                            }
                                            boolean zIsSmartSwitchDualSimSupported = MiuiPhoneUtils.isSmartSwitchDualSimSupported();
                                            NetworkSpeedTrackerHandler networkSpeedTrackerHandler = NetworkSpeedTrackerHandler.this;
                                            if (zIsSmartSwitchDualSimSupported) {
                                                Toast.makeText(NetworkSpeedTracker.this.mContext, R.string.data_interruption_remind_for_t, 1).show();
                                            } else {
                                                Toast.makeText(NetworkSpeedTracker.this.mContext, R.string.data_interruption_remind, 1).show();
                                            }
                                            NetworkSpeedTracker.this.recordDsda2DsdsRemind(NetworkSpeedTracker.this.mDdsPhoneId != 0 ? 0 : 1);
                                        }
                                    }, 2000L);
                                }
                                break;
                            case 12:
                                NetworkSpeedTracker.this.processDataConnectionStateChanged((Intent) message.obj);
                                break;
                            case 13:
                                NetworkSpeedTracker.this.processSmartSwitchAlertDialog(i);
                                break;
                            case 14:
                                NetworkSpeedTracker.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                                break;
                            case 15:
                                NetworkSpeedTracker.this.processSignalStrengthEvent(message.arg1, (SignalStrength) message.obj);
                                break;
                            case 16:
                                boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                                if (NetworkSpeedTracker.this.mIsWechatVoipForgroud != zBooleanValue) {
                                    NetworkSpeedTracker.this.mIsWechatVoipForgroud = zBooleanValue;
                                    NetworkSpeedTracker networkSpeedTracker2 = NetworkSpeedTracker.this;
                                    networkSpeedTracker2.mWechatVoipForgroudTime = networkSpeedTracker2.mIsWechatVoipForgroud ? System.currentTimeMillis() : 0L;
                                }
                                break;
                            case 17:
                                NetworkSpeedTracker.this.processRoamingStateChanged();
                                break;
                            case 18:
                                NetworkSpeedTracker.this.isInDdsChangeMonitorTimer = false;
                                break;
                        }
                        break;
                }
                return;
            }
            Intent intent = (Intent) message.obj;
            NetworkSpeedTracker.this.mUpdateInterval = intent.getIntExtra("update_interval", FiveGManagerBase.getSpeedIntervalByCloud());
            NetworkSpeedTracker.this.mTestUlBps = intent.getLongExtra("dummy_test_ulBps", 0L);
            NetworkSpeedTracker.this.mTestDlBps = intent.getLongExtra("dummy_test_dlBps", 0L);
            NetworkSpeedTracker.this.mIsDummyTest = intent.getBooleanExtra("dummy_test", false);
            NetworkSpeedTracker.this.mLastTimeForDsdaToDsdsToast = intent.getLongExtra("last_time_toast", 0L);
            NetworkSpeedTracker.this.mLastTimeForSmartSwithDualSimDialog = intent.getLongExtra("last_time_dialog", 0L);
            NetworkSpeedTracker.this.mSmartSwithDualSimDialogCancelCount = intent.getIntExtra("dialog_cancel_count", 0);
            NetworkSpeedTracker.VDBG = true;
            NetworkSpeedTracker.this.mSignalpoorTest = intent.getBooleanExtra("signalpoor", false);
            NetworkSpeedTracker.this.mDataStallTest = intent.getBooleanExtra("datastall", false);
            if (NetworkSpeedTracker.VDBG) {
                NetworkSpeedTracker.log("EVENT_DEBUG_ALL interval[" + NetworkSpeedTracker.this.mUpdateInterval + "], dummyTest[" + NetworkSpeedTracker.this.mIsDummyTest + "], testUlBps[" + NetworkSpeedTracker.this.mTestUlBps + "], testDlBps[" + NetworkSpeedTracker.this.mTestDlBps + "]");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        this.mCm = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        registerAllInfo();
        initValues();
        initConfigParams();
        initDsdaWhiteList();
        initSmartSwitchWhiteList();
    }

    private void registerAllInfo() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
        registerReceiver();
        registerPhoneStateListener();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            MiuiPhoneUtils.getPhone(i);
            this.mServiceState[i] = new ServiceState();
            this.mLteRsrp[i] = Integer.MAX_VALUE;
            this.mLteSnr[i] = Integer.MAX_VALUE;
            this.mNrRsrp[i] = Integer.MAX_VALUE;
            this.mNrSnr[i] = Integer.MAX_VALUE;
            this.mVGInfo[i] = new FiveGServiceInfo();
            this.mRoamingState[i] = Integer.MAX_VALUE;
        }
        if (MiuiPhoneUtils.isFiveGCapable()) {
            register5GServiceInfoListener();
        }
        registerTelephonyInfoCallBack();
        registerMmsNetworkCallback();
        registerSpeedLimitCardToastObserver();
        registerRoamingStateCallBack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onUpdateDeviceState(int i) {
        try {
            log("onUpdateDeviceState eventType=" + i + ",mIsScreenOn=" + this.mIsScreenOn + ",mIsMobileDataOn=" + this.mIsMobileDataOn);
            boolean z = this.mIsScreenOn;
            if (z && this.mIsMobileDataOn) {
                startDataPoll();
            } else if (!z || !this.mIsMobileDataOn) {
                stopDataPoll();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.WECHAT_VOIP_SCENE");
        intentFilter.addAction("networkSpeedTracker.intent.debugall");
        intentFilter.addAction("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_DSDA_WHITE_LIST_UPDATE_DONE");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_SMART_SWITCH_WHITE_LIST_UPDATE_DONE");
        intentFilter.addAction("com.android.phone.intent.action.SMART_SWITCH_DUAL_SWITCH_ALERT_DIALOG");
        intentFilter.addAction("android.intent.action.ANY_DATA_STATE");
        if (MiuiPhoneUtils.isFiveGHostedModeSupported()) {
            intentFilter.addAction("com.android.phone.intent.action.CLOUD_SMART_SWITCH_DONE");
        }
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    private void startDataPoll() {
        if (DBG) {
            log("startDataPoll mPollActive=" + this.mPollActive);
        }
        if (this.mPollActive || this.mHandler.hasMessages(2)) {
            log("pollActive or message contains update,then ignore!");
            return;
        }
        stopDataPoll();
        this.mCalendar = Calendar.getInstance();
        this.mDataStallCount = 0;
        postUpdateNetworkRate();
    }

    private void stopDataPoll() {
        if (DBG) {
            log("stopDataPoll");
        }
        this.mPollActive = false;
        if (this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
        }
        this.mNetworkStats.clear();
        this.mSignalQosStatistic.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMobileStateChanged(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null || networkInfo.getType() != 0) {
            return;
        }
        if (networkInfo.getState() == NetworkInfo.State.CONNECTED && !this.mIsMobileDataOn) {
            this.mIsMobileDataOn = true;
            this.mMobileDataConnTime = System.currentTimeMillis();
            onUpdateDeviceState(3);
        } else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED && this.mIsMobileDataOn) {
            this.mIsMobileDataOn = false;
            this.mMobileDataConnTime = 0L;
            onUpdateDeviceState(3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDataConnectionStateChanged(Intent intent) {
        String stringExtra = intent.getStringExtra("apnType");
        if (TextUtils.isEmpty(stringExtra) || !stringExtra.contains("default")) {
            return;
        }
        if (SubscriptionManager.getDefaultDataSubscriptionId() != intent.getIntExtra("subscription", -1)) {
            return;
        }
        try {
            this.mConnectState = Enum.valueOf(PhoneConstants.DataState.class, intent.getStringExtra(MEModuleSignalT1s.EXTRA_STATE));
            log("mConnectState: " + this.mConnectState);
        } catch (IllegalArgumentException e) {
            log("Exception: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processVGInfoChanged(int i, FiveGServiceInfo fiveGServiceInfo) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && i == this.mDdsPhoneId) {
            if (fiveGServiceInfo != null) {
                this.mVGInfo[i].copyFrom(fiveGServiceInfo);
            }
            return;
        }
        log("ignore on phoneId " + i + ",mDdsPhoneId=" + this.mDdsPhoneId);
    }

    class VGInfoListener implements BaseEvent.IEventListener {
        VGInfoListener() {
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
                NetworkSpeedTracker.log("VGInfoListener info is null or invalid phone id");
                return;
            }
            if (NetworkSpeedTracker.VDBG) {
                NetworkSpeedTracker.log("info[" + i + "]= " + fiveGServiceInfo);
            }
            NetworkSpeedTracker.this.mHandler.obtainMessage(5, i, 0, fiveGServiceInfo).sendToTarget();
        }
    }

    private void register5GServiceInfoListener() {
        this.mVGInfoListener = new VGInfoListener();
        EventHubManager.getInstance().registerListener("telephony", "fiveg_service_info_changed", this.mVGInfoListener);
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0052  */
    private boolean isWeakSignal() {
        ServiceState serviceState;
        NetworkMetricsController networkMetricsController;
        int i;
        int i2;
        boolean z = false;
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && (serviceState = this.mServiceState[this.mDdsPhoneId]) != null && !serviceState.getIsManualSelection() && (networkMetricsController = this.mNetworkMetricsController) != null && !networkMetricsController.getTopAppName().equals("com.miui.home")) {
            int currentRat = getCurrentRat();
            if (this.mNetworkMetricsController.isLteNwType(currentRat)) {
                int[] iArr = this.mLteRsrp;
                int i3 = this.mDdsPhoneId;
                int i4 = iArr[i3];
                if (i4 != Integer.MAX_VALUE && (i2 = this.mLteSnr[i3]) != Integer.MAX_VALUE) {
                    if (i4 <= -105 || (i2 <= -4 && i4 <= -100)) {
                        z = true;
                    }
                    log("isWeakSignal rat=" + currentRat + ",rst=" + z);
                }
            } else if (this.mNetworkMetricsController.isNrNwType(currentRat)) {
                int[] iArr2 = this.mNrRsrp;
                int i5 = this.mDdsPhoneId;
                int i6 = iArr2[i5];
                if (i6 != Integer.MAX_VALUE && (i = this.mNrSnr[i5]) != Integer.MAX_VALUE) {
                    if (i6 <= -105 || (i <= -4 && i6 <= -100)) {
                        z = true;
                    }
                    log("isWeakSignal rat=" + currentRat + ",rst=" + z);
                }
            } else {
                log("isWeakSignal rat=" + currentRat + ",rst=" + z);
            }
        }
        return z;
    }

    private boolean isPhoneStateIdle() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null && phone.getState() != PhoneConstants.State.IDLE) {
                log("isPhoneStateIdle false: Voice call active on phone" + i);
                return false;
            }
        }
        return true;
    }

    private boolean isVicePhoneStateIdle() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            return true;
        }
        Phone phone = MiuiPhoneUtils.getPhone(this.mDdsPhoneId == 0 ? 1 : 0);
        if (phone == null || phone.getState() == PhoneConstants.State.IDLE) {
            return true;
        }
        log("isVicePhoneStateIdle " + phone.getState());
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVoiceCallStateChanged() {
        this.mIsLingerCallState = false;
    }

    private void registerPhoneStateListener() {
        ((TelephonyManager) this.mContext.getSystemService("phone")).listen(this.mPhoneStateListener, 160);
    }

    private void registerTelephonyInfoCallBack() {
        log("registerTelephonyInfoCallBack");
        if (this.mTelephonyInfoCallBack == null) {
            log("new TelephonyInfoCallBack");
            TelephonyInfoCallBack telephonyInfoCallBack = new TelephonyInfoCallBack();
            this.mTelephonyInfoCallBack = telephonyInfoCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyInfoCallBack);
        }
    }

    class TelephonyInfoCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyInfoCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            NetworkSpeedTracker.this.mHandler.obtainMessage(15, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            NetworkSpeedTracker.this.mHandler.obtainMessage(14, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            NetworkSpeedTracker.this.clearCellPeakRateBps();
        }
    }

    private void recordMaxNetworkByte(long j, long j2) {
        Phone phone;
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j3 = this.mMobileDataConnTime;
        if (j3 == 0 || jCurrentTimeMillis - j3 < MOBILE_DATA_LINGER_TIMER) {
            return;
        }
        if ((VDBG && this.mIsDummyTest) || Build.IS_STABLE_VERSION || !MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            return;
        }
        long jLongValue = ((Long) this.mMaxNetworkUlByte.getOrDefault("UL", 0L)).longValue();
        long jLongValue2 = ((Long) this.mMaxNetworkDlByte.getOrDefault("DL", 0L)).longValue();
        long jLongValue3 = ((Long) this.mMaxNetworkAllByte.getOrDefault("All", 0L)).longValue();
        long j4 = j + j2;
        long j5 = j - jLongValue;
        long j6 = DELTA_MAX_NETWORK_BYTE;
        if (j5 > j6 || j2 - jLongValue2 > j6) {
            log("invalid network byte");
            return;
        }
        if ((j <= jLongValue && j2 <= jLongValue2 && j4 <= jLongValue3) || (phone = MiuiPhoneUtils.getPhone(this.mDdsPhoneId)) == null || phone.getServiceState() == null) {
            return;
        }
        int dataNetworkType = phone.getServiceState().getDataNetworkType();
        if (j > jLongValue) {
            this.mMaxNetworkUlByte.put("UL", Long.valueOf(j));
            this.mMaxNetworkUlByte.put("NW", Long.valueOf(dataNetworkType));
        }
        if (j2 > jLongValue2) {
            this.mMaxNetworkDlByte.put("DL", Long.valueOf(j2));
            this.mMaxNetworkDlByte.put("NW", Long.valueOf(dataNetworkType));
        }
        if (j4 > jLongValue3) {
            this.mMaxNetworkAllByte.put("All", Long.valueOf(j4));
            this.mMaxNetworkAllByte.put("NW", Long.valueOf(dataNetworkType));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordDsda2DsdsRemind(int i) {
        HashMap map = new HashMap();
        map.put("Network", MiuiPhoneUtils.getOperatorNumeric(MiuiPhoneUtils.getPhone(i)));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_dsda_to_dsds_remind", map);
    }

    private void recordSmartSwitchAlertDialog(int i) {
        HashMap map = new HashMap();
        map.put("Network", MiuiPhoneUtils.getOperatorNumeric(MiuiPhoneUtils.getPhone(i)));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_smart_switch_alert_dialog", map);
    }

    public ArrayList getMaxNetworkByteList() {
        ArrayList arrayList = new ArrayList();
        if (this.mMaxNetworkUlByte.size() > 0) {
            arrayList.add(new HashMap(this.mMaxNetworkUlByte));
        }
        if (this.mMaxNetworkDlByte.size() > 0) {
            arrayList.add(new HashMap(this.mMaxNetworkDlByte));
        }
        if (this.mMaxNetworkAllByte.size() > 0) {
            arrayList.add(new HashMap(this.mMaxNetworkAllByte));
        }
        return arrayList;
    }

    public void clearMaxNetworkByteList() {
        this.mMaxNetworkUlByte.clear();
        this.mMaxNetworkDlByte.clear();
        this.mMaxNetworkAllByte.clear();
    }

    private void monitorSpeedRateChange(long j, long j2) {
        int[] iArr = sSpeedThresholds;
        int speedLevel = getSpeedLevel(j * 8, iArr);
        int speedLevel2 = getSpeedLevel(8 * j2, iArr);
        if (VDBG) {
            log("monitorSpeedRateChange ul[" + j + "]Bps, dl[" + j2 + "]Bps");
        }
        if (FileUploadMonitor.getInstance() != null) {
            FileUploadMonitor.getInstance().handleSpeedRateUpdate(j, j2);
        }
        checkSpeedLevel(speedLevel, speedLevel2, j, j2);
    }

    private void checkSpeedLevel(int i, int i2, long j, long j2) {
        if (!isAllowedToCheckException()) {
            log("checkSpeedLevel: just return for exception!");
            return;
        }
        Scene scene = Scene.DUMMY;
        boolean z = true;
        if (isVoipSpeedException(j, j2)) {
            scene = Scene.WECHAT_VOICE_RATIO_EXCEPTION;
            this.mVoipDirection = getDirectionForWechatVideo(j, j2);
        } else if (isVoipLowSpeedException(j, j2)) {
            scene = Scene.WECHAT_VOICE_SPEED_EXCEPTION;
        } else if (isSpeedLimitCardException(i, i2)) {
            scene = Scene.SPEED_LIMIT_CARD_EXCEPTION;
        } else if (isSubwayState() && isLiveSpeedException(j, j2)) {
            scene = Scene.SPEED_LIVE_SPEED_EXCEPTION;
        } else {
            z = false;
        }
        if (scene == this.mSpeedScene) {
            return;
        }
        this.mSpeedScene = scene;
        if (z && MiuiPhoneUtils.isSpeedWarningFeatureSupported()) {
            localLog("notifySpeedListenersIfNecessary local", "scene:" + scene);
            notifySpeedListenersIfNecessary(j, j2, scene);
        }
    }

    public int getVoipDirection() {
        return this.mVoipDirection;
    }

    private int getDirectionForWechatVideo(long j, long j2) {
        boolean z = this.mIsWechatVoipForgroud;
        int i = (!z || j >= 50000) ? 1 : 0;
        if (!z || j2 >= 50000) {
            return i;
        }
        return 1;
    }

    private static int getSpeedLevel(long j, int[] iArr) {
        if (j > iArr[6]) {
            return 6;
        }
        if (j > iArr[5]) {
            return 5;
        }
        if (j > iArr[4]) {
            return 4;
        }
        if (j > iArr[3]) {
            return 3;
        }
        if (j > iArr[2]) {
            return 2;
        }
        if (j > iArr[1]) {
            return 1;
        }
        return j > ((long) iArr[0]) ? 0 : -1;
    }

    private boolean isVoipSpeedException(long j, long j2) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        boolean z = false;
        if (this.mIsWechatVoipForgroud && jCurrentTimeMillis - this.mWechatVoipForgroudTime >= 15000) {
            if ((j2 != 0 && j / j2 == 3) || (j != 0 && j2 / j == 3)) {
                z = true;
            }
            if (j2 != 0 && j != 0 && VDBG) {
                log("isVoipSpeedException ulRatio[" + (j / j2) + "],dlRatio[" + (j2 / j) + "]");
            }
        }
        return z;
    }

    private boolean isVoipLowSpeedException(long j, long j2) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (!this.mIsWechatVoipForgroud || jCurrentTimeMillis - this.mWechatVoipForgroudTime < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS || j2 >= 100) {
            return false;
        }
        log("isVoipLowSpeedException, dlRateBps：" + j2 + ", ulRateBps：" + j);
        return true;
    }

    private boolean isDataStallException(int i, int i2) {
        if (i >= 2 && (i2 == -1 || i2 == 0)) {
            this.mDataStallCount++;
        } else {
            this.mDataStallCount = 0;
        }
        boolean z = this.mDataStallCount >= 4;
        if (z) {
            log("isDataStallException mDataStallCount[" + this.mDataStallCount + "]");
        }
        return z;
    }

    private void dataStallNotification() {
        localLog("dataStallNotification", "network speed data stall");
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.NETWORK_SPEED_DATA_STALL");
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
        MobileNetworkProber mobileNetworkProber = MobileNetworkProber.getInstance();
        if (mobileNetworkProber != null) {
            mobileNetworkProber.startNetworkProber(MobileNetworkProber.FakeDataTriggers.NETWORK_SPEED_DATA_STALL);
        }
    }

    public boolean isDataHighTputByNetworkSpeed() {
        boolean z = getSpeedLevel(this.mCurrentDlRateBps * 8, sSpeedThresholds) == 6;
        log("isDataHighTputByNetworkSpeed rst=" + z);
        return z;
    }

    private boolean isSpeedLimitCardException(int i, int i2) {
        Calendar calendar = this.mCalendar;
        int i3 = calendar != null ? calendar.get(5) : -1;
        if (VDBG) {
            log("isSpeedLimitCardException dayOfMonth=" + i3 + ",ulLevel=" + i + ",dlLevel=" + i2 + ",mDdsPhoneId=" + this.mDdsPhoneId);
        }
        if (!this.mIsSpeedLimitToasted && i3 > 15) {
            return isSpeedLowForSignalGood(i, i2);
        }
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && i3 != -1 && i3 <= 15 && this.mIsSpeedLimitToasted) {
            Settings.Global.putInt(this.mContext.getContentResolver(), "speedLimitCardToast" + this.mDdsPhoneId, 0);
        }
        return false;
    }

    private synchronized boolean isSpeedLowForSignalGood(int i, int i2) {
        boolean z = false;
        if (i <= 5 && i2 <= 5 && (i >= 4 || i2 >= 4)) {
            try {
                NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
                if (networkMetricsController != null && networkMetricsController.isLteNrNwType(getCurrentRat()) && !this.mNetworkMetricsController.isNrPoorOrLtePoor(this.mDdsPhoneId) && isPhoneStateIdle()) {
                    if (i <= 4 && i2 <= 5) {
                        this.mSpeedLimitCount++;
                    } else if (this.mSpeedLimitCount != 0) {
                        this.mSpeedNoLimitCount++;
                    }
                    if (VDBG) {
                        log("isSpeedLowForSignalGood mSpeedLimitCount=" + this.mSpeedLimitCount + ",mSpeedNoLimitCount=" + this.mSpeedNoLimitCount);
                    }
                    int i3 = this.mSpeedLimitCount;
                    int i4 = this.mSpeedNoLimitCount + i3;
                    if (i4 < 30) {
                        return false;
                    }
                    if ((i3 * 100) / i4 > 60) {
                        if (this.mNetworkMetricsController.isLteNrPingPongWhiteList()) {
                            z = true;
                        } else if (this.mSpeedLimitCount != 0) {
                            resetSpeedLimitInfo();
                        }
                    }
                    return z;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (this.mSpeedLimitCount != 0) {
            resetSpeedLimitInfo();
        }
        return false;
    }

    private boolean isLiveSpeedException(long j, long j2) {
        NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
        if (networkMetricsController == null) {
            return false;
        }
        boolean z = networkMetricsController.mCurApptype == NetworkMetricsController.AppType.STREAMING_VIDEO;
        int i = this.mNetworkMetricsController.mCurUid;
        int i2 = this.mLastUid;
        if (i2 != -1 && i2 != i) {
            log("isLiveSpeedException: LiveApp change mLastUid:" + this.mLastUid + " curUid" + i);
            this.mLastUid = i;
            this.mLiveHighThroughputCount = 0;
            return false;
        }
        this.mLastUid = i;
        if (!z) {
            this.mLiveHighThroughputCount = 0;
            return false;
        }
        if (!isMusicActive()) {
            log("isLiveSpeedException: LiveApp Music inactive");
            this.mLiveHighThroughputCount = 0;
            return false;
        }
        if (j2 > 50000 && j > 6000) {
            this.mLiveHighThroughputCount++;
            log("isLiveSpeedException u/dlRateBps meet requirements: mLiveHighThroughputCount :" + this.mLiveHighThroughputCount);
            return false;
        }
        if (this.mLiveHighThroughputCount >= 5 && j2 < 10000 && j > 6000) {
            this.mLiveHighThroughputCount = 0;
            log("isLiveSpeedException is true");
            return true;
        }
        log("isLiveSpeedException: mLiveHighThroughputCount :" + this.mLiveHighThroughputCount);
        this.mLiveHighThroughputCount = 0;
        return false;
    }

    private boolean isMusicActive() {
        AudioManager audioManager = this.mAudioManager;
        if (audioManager == null) {
            log("mAudioManager is null");
            return false;
        }
        return audioManager.isMusicActive();
    }

    private int getCurrentRat() {
        ServiceState serviceState;
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || (serviceState = this.mServiceState[this.mDdsPhoneId]) == null) {
            return -1;
        }
        return serviceState.getDataNetworkType();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSpeedLimitCardToastObserver() {
        log("registerSpeedLimitCardToastObserver");
        int defaultDataSlotId = getDefaultDataSlotId();
        this.mDdsPhoneId = defaultDataSlotId;
        if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId)) {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            StringBuilder sb = new StringBuilder("speedLimitCardToast");
            sb.append(this.mDdsPhoneId);
            this.mIsSpeedLimitToasted = Settings.Global.getInt(contentResolver, sb.toString(), 0) == 1;
            this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("speedLimitCardToast" + this.mDdsPhoneId), false, this.mSpeedLimitCardToastObserver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterSpeedLimitCardToastObserver() {
        if (this.mSpeedLimitCardToastObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mSpeedLimitCardToastObserver);
        }
    }

    private void resetSpeedLimitInfo() {
        this.mSpeedLimitCount = 0;
        this.mSpeedNoLimitCount = 0;
    }

    public int getDataStallCount() {
        return this.mDataStallCount;
    }

    public void registerSpeedListener(ISpeedTrackerListener iSpeedTrackerListener) {
        log("registerSpeedListener");
        this.mSpeedListeners.add(iSpeedTrackerListener);
    }

    public void unregisterSpeedListener(ISpeedTrackerListener iSpeedTrackerListener) {
        log("unregisterSpeedListener");
        this.mSpeedListeners.remove(iSpeedTrackerListener);
    }

    private synchronized void notifySpeedListenersIfNecessary(long j, long j2, Scene scene) {
        ArrayList arrayList = this.mSpeedListeners;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            long j3 = j;
            long j4 = j2;
            Scene scene2 = scene;
            ((ISpeedTrackerListener) obj).onSpeedChanged(j3, j4, scene2);
            j = j3;
            j2 = j4;
            scene = scene2;
        }
    }

    class DataStallPolicy {
        private boolean policyActive;
        private long policyExpireTime;

        private DataStallPolicy(NetworkSpeedTracker networkSpeedTracker) {
        }

        public boolean isPolicyActiving() {
            if (System.currentTimeMillis() >= this.policyExpireTime) {
                disablePolicy();
            }
            return this.policyActive;
        }

        public void enablePolicy() {
            this.policyActive = true;
            this.policyExpireTime = System.currentTimeMillis() + 900000;
        }

        public void disablePolicy() {
            this.policyActive = false;
            this.policyExpireTime = 0L;
        }
    }

    private void registerMmsNetworkCallback() {
        NetworkRequest networkRequestBuild = new NetworkRequest.Builder().addTransportType(0).addCapability(0).build();
        ConnectivityManager connectivityManager = this.mCm;
        if (connectivityManager != null) {
            try {
                connectivityManager.registerNetworkCallback(networkRequestBuild, this.mNetworkCallback);
            } catch (Exception e) {
                log("registerMmsNetworkCallback e=" + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMmsStateChanged(Message message) {
        Object obj = message.obj;
        if (obj != null && (obj instanceof NetworkCapabilities)) {
            try {
                NetworkCapabilities networkCapabilities = (NetworkCapabilities) obj;
                log("handleMmsStateChanged");
                if (networkCapabilities == null || !networkCapabilities.hasCapability(0)) {
                    return;
                }
                NetworkSpecifier networkSpecifier = networkCapabilities.getNetworkSpecifier();
                if (networkSpecifier instanceof TelephonyNetworkSpecifier) {
                    int subscriptionId = ((TelephonyNetworkSpecifier) networkSpecifier).getSubscriptionId();
                    int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
                    log("activeSubId:" + activeDataSubscriptionId + ", mmsSubId:" + subscriptionId);
                    if (SubscriptionManager.isValidSubscriptionId(activeDataSubscriptionId) && SubscriptionManager.isValidSubscriptionId(subscriptionId) && subscriptionId != activeDataSubscriptionId) {
                        this.mIsSeningMmsOnNondds = true;
                        return;
                    }
                    return;
                }
                return;
            } catch (Exception e) {
                log("handleMmsNetworkChanged Exception:" + e);
                return;
            }
        }
        if (this.mIsSeningMmsOnNondds) {
            this.mIsSeningMmsOnNondds = false;
        }
    }

    private boolean isAllowedToCheckException() {
        boolean z = (this.mIsSeningMmsOnNondds || !this.mIsCallStateIdle || this.mIsLingerCallState) ? false : true;
        if (!z) {
            log("isAllowedToCheckException:" + this.mIsSeningMmsOnNondds + ",mIsCallStateIdle:" + this.mIsCallStateIdle + ",mIsLingerCallState:" + this.mIsLingerCallState);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCallStateChanged(int i) {
        log("processCallStateChanged onCallStateChanged: state=" + i);
        boolean z = i == 0;
        if (z != this.mIsCallStateIdle) {
            if (VDBG) {
                log("processCallStateChanged: isCallIdle=" + z + ",mIsCallStateIdle=" + this.mIsCallStateIdle);
            }
            this.mHandler.removeMessages(8);
            if (z) {
                this.mIsLingerCallState = true;
                NetworkSpeedTrackerHandler networkSpeedTrackerHandler = this.mHandler;
                networkSpeedTrackerHandler.sendMessageDelayed(networkSpeedTrackerHandler.obtainMessage(8), 120000L);
            } else {
                this.mHandler.obtainMessage(8).sendToTarget();
            }
        }
        this.mIsCallStateIdle = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRemindInDsda2Dsds(long j, long j2) {
        boolean z = Settings.Global.getInt(this.mContext.getContentResolver(), "vice_slot_volte_data_enabled", 0) != 0;
        boolean zIsVicePhoneStateIdle = isVicePhoneStateIdle();
        boolean zIsMobileDataEnabled = MiuiPhoneUtils.isMobileDataEnabled(this.mContext);
        int networkType = MiuiPhoneUtils.getNetworkType(this.mContext);
        boolean z2 = MiuiPhoneUtils.getInsertedSimCount() > 1;
        boolean z3 = Settings.Global.getInt(this.mContext.getContentResolver(), "gb_gwsd", 0) != 0;
        boolean zShouldHideIntelligentDualSimButton = MiuiPhoneUtils.shouldHideIntelligentDualSimButton();
        StringBuilder sb = new StringBuilder("isVicePhoneStateIdle: ");
        sb.append(zIsVicePhoneStateIdle);
        sb.append(", isMobileDataEnabled: ");
        sb.append(zIsMobileDataEnabled);
        sb.append(", mDdsPhoneId: ");
        sb.append(this.mDdsPhoneId);
        sb.append(", NetworkState: ");
        sb.append(networkType);
        sb.append(", isGameBoosting: ");
        sb.append(z3);
        sb.append(", isSupportIntelligentDualSim");
        sb.append(!zShouldHideIntelligentDualSimButton);
        log(sb.toString());
        return this.mIsScreenOn && zShouldHideIntelligentDualSimButton && z2 && (!zIsVicePhoneStateIdle && !z) && (zIsMobileDataEnabled && (networkType == 0 || networkType == -1) && ((j > 100L ? 1 : (j == 100L ? 0 : -1)) > 0 && (j2 > 100L ? 1 : (j2 == 100L ? 0 : -1)) > 0)) && !z3 && isWhiteList(mSceneDsda2DsdsWhiteList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLingerTimeForDsda2DsdsToast() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mLastTimeForDsdaToDsdsToast;
        if (j != 0 && jCurrentTimeMillis < j + 172800000) {
            log("do NOT toast data interruption during lingering time");
            return true;
        }
        this.mLastTimeForDsdaToDsdsToast = jCurrentTimeMillis;
        return false;
    }

    private boolean isLingerTimeForSmartSwitchDualSimDialog() {
        int i;
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mLastTimeForSmartSwithDualSimDialog;
        if (j != 0 && ((i = this.mSmartSwithDualSimDialogCancelCount) == 0 || i == 1 ? jCurrentTimeMillis < j + v.b : !(i == 2 ? jCurrentTimeMillis >= j + v.a : i != 3))) {
            log("mSmartSwithDualSimDialogCancelCount=" + this.mSmartSwithDualSimDialogCancelCount + ", do NOT alert smart switch dual sim dialog during lingering time");
            return true;
        }
        this.mLastTimeForSmartSwithDualSimDialog = jCurrentTimeMillis;
        return false;
    }

    private boolean isWhiteList(List list) {
        ActivityManager activityManager;
        if (list.size() == 0 || (activityManager = (ActivityManager) this.mContext.getSystemService("activity")) == null) {
            return false;
        }
        String topAppName = getTopAppName(activityManager);
        log("topAppName:" + topAppName);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (topAppName.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    private String getTopAppName(ActivityManager activityManager) {
        try {
            return (activityManager.getRunningTasks(1) == null || activityManager.getRunningTasks(1).get(0) == null) ? "" : activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
        } catch (IndexOutOfBoundsException e) {
            log("getTopAppName exception:" + e.toString());
            return "";
        } catch (Exception e2) {
            log("getTopAppName exception:" + e2.toString());
            return "";
        }
    }

    private void fetchSmartSwithDualSimDialogCount() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("preference_smart_switch_dual_sim", 0);
        this.mSmartSwithDualSimDialogCancelCount = sharedPreferences.getInt("SMART_SWITCH_DIALOG_CANCEL_COUNT", 0);
        this.mLastTimeForSmartSwithDualSimDialog = sharedPreferences.getLong("LAST_TIME_FOR_SMART_SWITCH_DIALOG", 0L);
    }

    public void updateSmartSwithDualSimDialogCount(boolean z) {
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("preference_smart_switch_dual_sim", 0).edit();
        if (z) {
            this.mSmartSwithDualSimDialogCancelCount = 0;
            editorEdit.putInt("SMART_SWITCH_DIALOG_CANCEL_COUNT", 0);
            editorEdit.putLong("LAST_TIME_FOR_SMART_SWITCH_DIALOG", this.mLastTimeForSmartSwithDualSimDialog);
        } else {
            int i = this.mSmartSwithDualSimDialogCancelCount;
            if (i < 3) {
                int i2 = i + 1;
                this.mSmartSwithDualSimDialogCancelCount = i2;
                editorEdit.putInt("SMART_SWITCH_DIALOG_CANCEL_COUNT", i2);
                editorEdit.putLong("LAST_TIME_FOR_SMART_SWITCH_DIALOG", this.mLastTimeForSmartSwithDualSimDialog);
            }
        }
        editorEdit.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSmartSwitchAlertDialog(int i) {
        try {
            boolean zShouldHideIntelligentDualSimButton = MiuiPhoneUtils.shouldHideIntelligentDualSimButton();
            StringBuilder sb = new StringBuilder("processSmartSwitchAlertDialog eventType=");
            sb.append(i);
            sb.append(", mCurrentUlRateBps=");
            sb.append(this.mCurrentUlRateBps);
            sb.append(", mCurrentDlRateBps=");
            sb.append(this.mCurrentDlRateBps);
            sb.append(", mIsScreenOn=");
            sb.append(this.mIsScreenOn);
            sb.append(", mIsCallStateIdle=");
            sb.append(this.mIsCallStateIdle);
            sb.append(", isSupportIntelligentDualSim");
            sb.append(!zShouldHideIntelligentDualSimButton);
            log(sb.toString());
            boolean z = this.mCurrentUlRateBps < 1048576 && this.mCurrentDlRateBps < 1048576;
            if (zShouldHideIntelligentDualSimButton && z && this.mIsScreenOn && this.mIsCallStateIdle && isWhiteList(mSmartSwitchDualSimWhiteList) && !isLingerTimeForSmartSwitchDualSimDialog()) {
                log("show smartswitchdualsim alert dialog");
                MiuiPhoneUtils.showSmartSwitchDualSimDialog();
                recordSmartSwitchAlertDialog(this.mDdsPhoneId);
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
                this.mServiceState[i] = new ServiceState(serviceState);
            } else {
                log("processServiceStateChangeEvent invalid params!!!");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSignalStrengthEvent(int i, SignalStrength signalStrength) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && signalStrength != null) {
            updateSignalInfo(i, signalStrength);
        } else {
            log("processSignalStrengthEvent-invalid phone id!");
        }
    }

    private void updateSignalInfo(int i, SignalStrength signalStrength) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || i != this.mDdsPhoneId) {
            log("updateNwSignalInfo not allowed ,ignore");
            return;
        }
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) cellSignalStrength;
                this.mLteRsrp[i] = inRangeOrUnavailable(cellSignalStrengthLte.getRsrp(), -140, -43);
                this.mLteSnr[i] = inRangeOrUnavailable(cellSignalStrengthLte.getRssnr(), -23, 40);
                log("updateSignalInfo lteRsrp=" + this.mLteRsrp[i] + "lteSnr=" + this.mLteSnr[i] + ",phoneId=" + i);
            } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrength;
                this.mNrRsrp[i] = inRangeOrUnavailable(cellSignalStrengthNr.getSsRsrp(), -140, -44);
                this.mNrSnr[i] = inRangeOrUnavailable(cellSignalStrengthNr.getSsSinr(), -23, 40);
                log("updateSignalInfo nrRsrp= " + this.mNrRsrp[i] + ",nrSnr= " + this.mNrSnr[i] + ",phoneId= " + i);
            }
        }
        this.mIsWeakSignal = isWeakSignal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetSignalStrength() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mLteRsrp[i] = Integer.MAX_VALUE;
            this.mLteSnr[i] = Integer.MAX_VALUE;
            this.mNrRsrp[i] = Integer.MAX_VALUE;
            this.mNrSnr[i] = Integer.MAX_VALUE;
        }
    }

    private boolean isSubwayState() {
        NetworkModeRecognition networkModeRecognition = this.mNetworkModeRecognition;
        if (networkModeRecognition == null) {
            return false;
        }
        return networkModeRecognition.isInSubwayState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processRoamingStateChanged() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return;
        }
        if (MiuiPhoneUtils.getInsertedSimCount() > 1 && isDdsDataRoamingEnabled() && this.mIsMobileDataOn) {
            if (MiuiPhoneUtils.isDataRegistered(this.mDdsPhoneId != 0 ? 0 : 1) && this.mIsScreenOn && !isAirPlaneModeOn() && this.mIsCallStateIdle && isDdsRoamingNonDdsHome() && !isLingerTimeForRoamingChangeDds()) {
                showRoamingChangeDdsNotification();
                recordRoamingChangeDdsNotification();
                enterMonitorDdsChangeTimer();
            }
        }
    }

    private void enterMonitorDdsChangeTimer() {
        log("enterMonitorDdsChangeTimer");
        this.isInDdsChangeMonitorTimer = true;
        NetworkSpeedTrackerHandler networkSpeedTrackerHandler = this.mHandler;
        networkSpeedTrackerHandler.sendMessageDelayed(networkSpeedTrackerHandler.obtainMessage(18), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
    }

    private boolean isDdsDataRoamingEnabled() {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        boolean zIsDataRoamingEnabled = telephonyManager != null ? telephonyManager.isDataRoamingEnabled() : false;
        log("isDdsDataRoamingEnabled rst=" + zIsDataRoamingEnabled);
        return zIsDataRoamingEnabled;
    }

    private void recordRoamingChangeDdsNotification() {
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_roaming_change_dds_notify", new HashMap());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordRoamingChangeDds() {
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_roaming_change_dds", new HashMap());
    }

    private void showRoamingChangeDdsNotification() {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        Intent intent = new Intent();
        intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MobileNetworkSettings");
        intent.addFlags(268435456);
        notificationManager.createNotificationChannel(new NotificationChannel("roaming_notify", this.mContext.getString(R.string.roaming_change_dds_notification), 4));
        Notification notificationBuild = new Notification.Builder(this.mContext, "roaming_notify").setOngoing(true).setSmallIcon(android.R.drawable.stat_sys_warning).setContentText(this.mContext.getString(R.string.roaming_change_dds_notification_text)).setContentTitle(this.mContext.getString(R.string.roaming_change_dds_notification_title)).setContentIntent(PendingIntent.getActivity(this.mContext, 0, intent, 201326592)).setAutoCancel(true).setChannelId("roaming_notify").build();
        log("showRoamingChangeDdsNotification");
        notificationManager.notify(1, notificationBuild);
    }

    private boolean isDdsRoamingNonDdsHome() {
        boolean z = false;
        if (MiuiPhoneUtils.getInsertedSimCount() >= 2 && MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            int i = this.mDdsPhoneId;
            int i2 = i == 0 ? 1 : 0;
            int[] iArr = this.mRoamingState;
            int i3 = iArr[i];
            int i4 = iArr[i2];
            boolean z2 = i4 == 0 || (i4 == 2 && MiuiPhoneUtils.isSimReady(i2));
            if (i3 == 1 && z2) {
                z = true;
            }
            log("isDdsRoamingNonDdsHome ddsState=" + i3 + ",nDdsState=" + i4);
        }
        return z;
    }

    private boolean isLingerTimeForRoamingChangeDds() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mLastTimeForRoamingChangeDdsNotify;
        if (j != 0 && jCurrentTimeMillis < j + 259200000) {
            log("do show roaming change dds notify in lingering time");
            return true;
        }
        this.mLastTimeForRoamingChangeDdsNotify = jCurrentTimeMillis;
        return false;
    }

    private void registerRoamingStateCallBack() {
        EventHubManager.getInstance().registerListener("telephony", "roaming_mode_changed", new BaseEvent.IEventListener() { // from class: com.android.phone.utils.NetworkSpeedTracker$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$registerRoamingStateCallBack$0(baseEvent);
            }
        }, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerRoamingStateCallBack$0(BaseEvent baseEvent) {
        Bundle bundle;
        if (!(baseEvent instanceof TelephonyEvent.NetworkEvent) || (bundle = ((TelephonyEvent.NetworkEvent) baseEvent).getBundle()) == null) {
            return;
        }
        int i = bundle.getInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, -1);
        int i2 = bundle.getInt("roaming_mode", 0);
        log("roamingModeChanged phoneId=" + i + ", mode=" + i2 + ", lastMode=" + this.mRoamingState[i]);
        int[] iArr = this.mRoamingState;
        if (iArr[i] != i2) {
            iArr[i] = i2;
            if (this.mHandler.hasMessages(17)) {
                return;
            }
            NetworkSpeedTrackerHandler networkSpeedTrackerHandler = this.mHandler;
            networkSpeedTrackerHandler.sendMessageDelayed(networkSpeedTrackerHandler.obtainMessage(17), MirrorDesktopHelper.TIMEOUT_MILLIS);
        }
    }

    private boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    public boolean getScreenOnState() {
        return this.mIsScreenOn;
    }

    public class RateBpsInfo {
        private long rateBps = 0;
        private long updateTime = 0;

        public RateBpsInfo(NetworkSpeedTracker networkSpeedTracker) {
        }

        public void update(long j, long j2) {
            this.rateBps = j;
            this.updateTime = j2;
        }

        public long getRateBps() {
            return this.rateBps;
        }

        public long getUpdateTimes() {
            return this.updateTime;
        }

        public String toString() {
            return "rateBps=" + this.rateBps + ", updateTime=" + this.updateTime;
        }
    }

    public RateBpsInfo getRateBpsInfo() {
        return this.mCurrentULRateInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void localLog(String str, String str2) {
        NetworkMetricsController.localLog("NetworkSpeedTracker", str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("NetworkSpeedTracker", str);
    }
}
