package com.android.phone;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
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
import android.util.SparseArray;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.datasub.SmartDualSimSwitch;
import com.android.internal.util.ArrayUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.NetworkSceneEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.IModemModule;
import com.android.phone.network.ModemAiQoeController;
import com.android.phone.performance.SelfStudyControllerBase;
import com.android.phone.signalMap.SignalMapCloudController;
import com.android.phone.signalMap.SignalMapSnsControllerBase;
import com.android.phone.signalMap.SignalMapSubwayController;
import com.android.phone.statistics.MiuiQmsStats;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiAppStateMonitor;
import com.android.phone.utils.NetworkSpeedTracker;
import com.android.phone.utils.SignalUtils;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.android.services.telephony.relay.offlinecall.OfflineUtils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.miui.misight.MiEvent;
import com.miui.misight.MiSight;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.joyose.IJoyoseInterface;
import com.xiaomi.joyose.IMiGameBoosterCallback;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import com.xiaomi.modem.sar.DeviceStateListenerBase;
import com.xiaomi.telephony.XmTelephonyManagerInner;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.telephony.DefaultSimManager;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;
import miuix.preference.flexible.AbstractBaseTemplate;
import xiaomi.hypercomm.HyperCommManager;
import xiaomi.hypercomm.SceneAwareCallback;

/* JADX INFO: loaded from: classes.dex */
public class NetworkMetricsController {
    private static boolean mIsHyperEngineSupported;
    private static NetworkMetricsController sInstance;
    private boolean isSupportLBopt;
    private boolean isTputAppInForground;
    private int mActionNetworkScore;
    private boolean mBadCell;
    private CellIdentity[] mCellIdentity;
    private ConnectivityManager mCm;
    private Context mContext;
    public volatile AppType mCurApptype;
    public int mCurUid;
    private String mCurrentForegroundPackageName;
    private final CurrentTopAppObserver mCurrentTopAppObserver;
    private CloudCellDbHelper mDbHelper;
    private ResultReceiver mDdsAssistantInfoReceiver;
    private LinkedList mElapsedRealtimeFastLte;
    private LinkedList mElapsedRealtimeFastNr;
    private LinkedList mElapsedRealtimeGameStall;
    private LinkedList mElapsedRealtimeVoipStall;
    private final BaseEvent.IEventListener mEventListener;
    private FiveGServiceInfo[] mFiveGInfo;
    private FiveGInfoListener mFiveGInfoListener;
    private Set mFivegLBStreamMediaWhiteList;
    private Set mFivegLowBandSet;
    private final List mGameDelayListeners;
    private boolean mGlobalHstState;
    private MetricsHandler mHandler;
    private Set mHdrOptWhiteListForC6C7;
    private boolean mHsrOptEnabled;
    private volatile int mHyperEngineGameOptDbStatus;
    private final BroadcastReceiver mIntentReceiver;
    private boolean mIsCommonSignalWhiteListForground;
    private boolean mIsFivegLBStreamMediaWhiteListForground;
    private boolean mIsFivegLBStreamOptCloudOn;
    private boolean mIsFivegLBStreamOptEffectMonitor;
    private boolean mIsHdrOptWhiteListForground;
    private boolean mIsHolderLevel4;
    private boolean mIsHolderVoipStall;
    private boolean mIsHyperEngineGame;
    private volatile boolean mIsHyperEngineGameCloudOn;
    private volatile boolean mIsHyperEngineGameOptByDb;
    private volatile boolean mIsHyperEngineGameOptByTemp;
    private volatile boolean mIsHyperEngineHstCloudOn;
    private volatile boolean mIsHyperEngineSmartHoCloudOn;
    private volatile boolean mIsInCall;
    private boolean mIsLastGameBattle;
    private boolean mIsPowerSaveOn;
    private boolean mIsScreenOn;
    private boolean mIsThermalScene;
    private boolean mIsUnsolDrbReleaseByRrc;
    private boolean mIsWechatAppForC6C7;
    private long mLastCellChangeTime;
    private int mLastDataStallDirection;
    private volatile long mLastDepriorityTimeInStreamDataStall;
    private int mLastGameDelay;
    private long mLastGameHighDelayTime;
    private long mLastModemAiPeceptionTime;
    private int mLastNetworkScore;
    private volatile int mLastRecoveryAction;
    private volatile long mLastRecoveryActionTime;
    private volatile long mLastSmartDualSimTimeInStreamDataStall;
    private long mLastVoipHighDelayTime;
    private float mLinkPerceptionResult;
    private int[] mLteRsrp;
    private int[] mLteSnr;
    private MiuiAppStateMonitor mMiuiAppStateMonitor;
    private float mModemPerceptionResult;
    private boolean mNeedMtkDataStallNormalAfterCellChange;
    private int[] mNrRsrp;
    private int[] mNrSnr;
    private float mPerceptionResult;
    private PowerManager mPowerManager;
    private LinkedList mQoeScoreSamplesForBasement;
    private NetworkSelfRecovery mRecovery;
    private LinkedList mRenderStallSamplesForBasement;
    private RilHook mRilHook;
    private int[] mRoamingState;
    private int mSameRecoveryActionMinInterval;
    private SceneAwareCallback mSceneAwareCallback;
    private SensorStateCallBack mSensorStateCallBack;
    private ServiceState[] mServiceState;
    private SettingsObserver mSettingsObserver;
    private SignalStrength[] mSignalStrength;
    private SmartNetworkRecoveryController mSmartNetworkRecovery;
    private SpeedListener mSpeedListener;
    private NetworkSpeedTracker mSpeedTracker;
    private Map mStats;
    private final ContentObserver mSubwayStateObserver;
    private Set mTddSet;
    private ConcurrentHashMap mTencentGameStats;
    private ThermalSceneListener mThermalSceneListener;
    private int mVideoQuality;
    private PowerManager.WakeLock mWakeLock;
    private boolean mWeakCongestionOptEnabled;
    private HandlerThread mWorkThread;
    private static final List XIAOMI_JOYOSE_FIXED_CYCLE_SUPPORTED_GAMES = Arrays.asList("com.tencent.tmgp.pubgmhd");
    private static final List XIAOMI_JOYOSE_REAL_TIME_SUPPORTED_GAMES = MiuiPhoneUtils.getFeatureConfig("config_joyose_real_time_supported_games");
    private static final HashMap sLocalLogs = new HashMap();
    private static final Set BASEMENT_VOIP_WHITE_CELL_SET = new HashSet(Arrays.asList(1646944390L, 1615994881L, 1646821508L, 1610797313L, 1614753793L, 1616379907L, 1610797057L, 1646822020L, 1646826116L, 1646825604L, 5831446530L, 5831438341L, 5831434241L, 5831426049L, 5690650627L, 5725298709L, 5753770005L, 5752524822L, 5752525522L, 5752524823L, 5752525523L, 5768237109L, 5768237809L, 55738003715L, 55745044864L, 55744975232L, 55745044608L, 42961108994L, 55736497665L));
    private static boolean mIsAiInfoSupport = false;
    private static final int[] sTmgpGameDelayThresholds = {0, 100, 200, MiRilHook.HOOK_ID_START_URC_BY_AT, 460};
    private static final int[] sLolmGameDelayThresholds = {0, 80, 200, MiRilHook.HOOK_ID_START_URC_BY_AT, 999};
    private static final int[] sJkchessGameDelayThresholds = {0, 100, MiRilHook.HOOK_ID_START_URC_BY_AT, ResponseObject.RELAY_FAILED_CODE, 999};
    private static List mLteNrPingPongWhiteList = new ArrayList();
    private static List mCommonSignalWhiteList = new ArrayList();
    private static final SparseArray mUidCache = new SparseArray();
    private static List mHyperEngineGameWhiteList = new ArrayList();
    private static List mConvVideoWhiteListDefault = new ArrayList();
    private static List mStreamingVideoWhiteListDefault = new ArrayList();
    private static List mWebBrowsingWhiteListDefault = new ArrayList();
    private static List mFileTransferWhiteListDefault = new ArrayList();
    private static List mGamingWhiteListDefault = new ArrayList();
    private static List mShortVideoWhiteListDefault = new ArrayList();
    private boolean VDBG = false;
    private boolean mPingPongNightTest = false;
    private boolean mIsTMGPsGameForeground = false;
    private boolean mIsGameStatisticForeground = false;
    private boolean mIsHuyaForeground = false;
    private boolean mIsVoipForgroud = false;
    private long mLastCommonSignalDsaTime = 0;
    private int mBasementVoipRsrpThreshold = -100;
    private int mBasementVoipLteRsrpEsaThreshold = -115;
    private long mBasementDsaTime = 0;
    private double mBasementRenderRatioBeforeDsa = -1.0d;
    private double mBasementQoeAvgBeforeDsa = -1.0d;
    private long mLastBasementEsaTime = 0;
    private int mBasementEsaRetryCount = 0;
    private boolean mBasementVoipOutTriggerEnabled = false;
    private long mLastSubwayQoeDataStallTime = 0;
    private boolean mCurrentHstState = false;
    private boolean mIsInElevator = false;
    private boolean mIsElevatorDeprioritized = false;
    private boolean mCurrentSubwayState = false;
    private boolean mCurrentMoveLikeState = false;
    private volatile boolean mIsSubwayMode = false;
    private Map mNrCellIdMap = new HashMap();
    private Map mElevatorNrCellIdMap = new HashMap();
    private Map mElevatorLteCellIdMap = new HashMap();
    private CellIdentityNr mNrCi = null;
    private CellIdentityLte mLteCi = null;
    private Map mLteCellIdMap = new HashMap();
    private long mLastSubwaySignalMapOptTime = 0;
    private long mLastHdrCellOptTime = 0;
    private boolean mIsMobileDataOn = false;
    private boolean mIsWifiDataOn = false;
    private boolean mIsNightMode = false;
    private volatile int mDdsPhoneId = -1;
    private boolean mIsAirPlaneMode = false;
    private int mNetId = -1;
    private TeleCallBack mTeleCallBack = null;
    private TeleStaisticCallBack mTeleStaisticCallBack = null;
    private IMiGameBoosterCallback mMiGameBoosterCallback = null;
    private IJoyoseInterface mJs = null;
    private boolean mJsRegistered = false;
    private boolean mIsFeatureEnable = false;
    private boolean mIsSmartDualSimFeatureEnable = false;
    private long mPenaltyForceLteTime = 0;
    private long mPenaltyForceNrTime = 0;
    private boolean mGame = false;
    private boolean mDepriorityNr2Lte = false;
    private boolean mIsBuildingMode = false;
    private int mForceLteCount = 0;
    private int mForceNrCount = 0;
    private int mLteCellCountNdds = 0;
    private long mLte2NrTime = 0;
    private long mNr2LteTime = 0;
    private int mNrDurationExceptionCount = 0;
    private int mNrDurationTotalCount = 0;
    private Scene mLastSceneType = Scene.DUMMY;
    private int mCommonSceneSignalTime = 60;
    private long mTriggerDataStallTime = 0;
    private long mLastDelayLevelStartTimes = 0;
    private long mStateChangeTime = 0;
    private long mNrLteChangeInGameTime = 0;
    private long mLastNrLteChangeInGameTime = 0;
    private boolean mSupportFiveG = false;

    public interface GameDelayListener {
        void onGameBattleStatusChanged(boolean z);

        void onGameDelayChanged(int i);

        void onGameStatusChanged(int i, String str);

        void onGameUpdateStatusChanged(boolean z);
    }

    public int inRangeOrUnavailable(int i, int i2, int i3) {
        if (i < i2 || i > i3) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    public boolean isLteNwType(int i) {
        return i == 13 || i == 19;
    }

    public boolean isNrNwType(int i) {
        return i == 20 || i == 30;
    }

    class TimedSample {
        final long time;
        final int value;

        TimedSample(long j, int i) {
            this.time = j;
            this.value = i;
        }
    }

    public enum Scene {
        DUMMY(0),
        THERMAL(1),
        GAME(2),
        LINK_QOS(3),
        SPEED_RATE(4),
        VOIP(5),
        PING_PONG_COMMON(6),
        DATA_NETWORK_FAILED(7),
        HIGH_SPEED_TRAIN(8),
        OOS(9),
        DATA_SLOT(10),
        SETTINGS_NW_OPT_FEATURE(11),
        AIRPLANE_MODE(12),
        SIM_STATE(13),
        SETTINGS_FIVEG(14),
        OUT_ELEVATOR(15),
        TIMEOUT(16),
        CELLCHANGED(17),
        LTENRDEREGISTERED(18),
        BUILDING_MODE(19),
        SIGNAL_WHITE_LIST_FG(20),
        SIGNAL_WHITE_LIST_BG(21),
        SIGNAL_STRENGTH(22),
        DATA_STALL_SIGNALQOS(23),
        DATA_STALL_RESET(25),
        LIVE(26),
        SIGNAL_MAP(27),
        DATA_STALL_SELFSTUDY(28),
        POWER_SAVE(29),
        PING_PONG_NIGHT(30),
        SCREEN_ON_PING_PONG_NIGHT(31),
        NRDEREGISTEREDNDDS(32),
        OUT_BASEMENT(33),
        SPEED_HIGH_ONLY(34),
        SMART_NETWORK_PECEPTION(35),
        MODEM_AI_PECEPTION(36),
        SUBWAY_ABNORMAL_CELL(37),
        GAME_RESIDENT_CELL(38),
        IN_HST_OR_SUBWAY_OR_MOVELIKE(39),
        GLOBAL_HST_DEREGISTERED(40),
        STREAM_DATA_STALL(41),
        BASEMENT_VOIP_STALL(42),
        SUBWAY_QOE_DATA_STALL(43),
        HST_NW_WITHOUT_NR(44);

        private int value;

        Scene(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum AppType {
        NONE(0),
        CONV_AUDIO(1),
        CONV_VIDEO(2),
        STREAMING_AUDIO(3),
        STREAMING_VIDEO(4),
        TYPE_GAMING(5),
        WEB_BROWSING(6),
        FILE_TRANSFER(7);

        private int value;

        AppType(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static synchronized NetworkMetricsController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new NetworkMetricsController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private NetworkMetricsController() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mServiceState = new ServiceState[i];
        this.mSignalStrength = new SignalStrength[i];
        this.mFiveGInfo = new FiveGServiceInfo[i];
        this.mCellIdentity = new CellIdentity[i];
        this.mLteRsrp = new int[i];
        this.mLteSnr = new int[i];
        this.mNrRsrp = new int[i];
        this.mNrSnr = new int[i];
        this.mTencentGameStats = new ConcurrentHashMap();
        this.mLastGameDelay = 0;
        this.mIsLastGameBattle = false;
        this.mLastGameHighDelayTime = 0L;
        this.mIsHolderLevel4 = false;
        this.mVideoQuality = -1;
        this.mLastVoipHighDelayTime = 0L;
        this.mIsHolderVoipStall = false;
        this.mThermalSceneListener = null;
        this.mIsThermalScene = false;
        this.mIsPowerSaveOn = false;
        this.isTputAppInForground = false;
        this.mIsCommonSignalWhiteListForground = false;
        this.mIsWechatAppForC6C7 = false;
        this.mHdrOptWhiteListForC6C7 = new HashSet(MiuiPhoneUtils.getFeatureConfig("sns_hdr_opt_white_list_for_c6c7"));
        this.mIsHdrOptWhiteListForground = false;
        this.mLastDataStallDirection = -1;
        this.mNeedMtkDataStallNormalAfterCellChange = false;
        this.mLastCellChangeTime = -1L;
        this.mCurUid = -1;
        this.mCurrentTopAppObserver = new CurrentTopAppObserver();
        this.mCurApptype = AppType.NONE;
        this.mBadCell = false;
        this.mLastModemAiPeceptionTime = 0L;
        this.mGlobalHstState = false;
        this.mGameDelayListeners = new ArrayList();
        this.mStats = new HashMap();
        this.mPerceptionResult = 0.0f;
        this.mLinkPerceptionResult = 0.0f;
        this.mModemPerceptionResult = 0.0f;
        this.mSmartNetworkRecovery = null;
        this.mRoamingState = new int[i];
        this.mCurrentForegroundPackageName = "";
        this.mSensorStateCallBack = null;
        this.mSceneAwareCallback = null;
        this.mIsUnsolDrbReleaseByRrc = false;
        this.mHsrOptEnabled = false;
        this.mWeakCongestionOptEnabled = false;
        this.mIsInCall = false;
        this.mIsHyperEngineGameOptByDb = true;
        this.mIsHyperEngineGameOptByTemp = true;
        this.mHyperEngineGameOptDbStatus = -1;
        this.mIsHyperEngineGame = false;
        this.mFivegLBStreamMediaWhiteList = new HashSet(MiuiPhoneUtils.getFeatureConfig("fiveg_low_band_stream_media_white_list"));
        this.mIsFivegLBStreamMediaWhiteListForground = false;
        this.mLastNetworkScore = -1;
        this.mActionNetworkScore = -1;
        this.mTddSet = new HashSet();
        this.mFivegLowBandSet = new HashSet();
        this.mLastRecoveryAction = -1;
        this.mLastRecoveryActionTime = -1L;
        this.mLastSmartDualSimTimeInStreamDataStall = -1L;
        this.mLastDepriorityTimeInStreamDataStall = -1L;
        this.mSameRecoveryActionMinInterval = 1200000;
        this.mIsFivegLBStreamOptCloudOn = false;
        this.mIsFivegLBStreamOptEffectMonitor = false;
        this.isSupportLBopt = MiuiPhoneUtils.isFivegLBStreamMediaOptSupported();
        this.mSubwayStateObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.NetworkMetricsController.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                NetworkMetricsController.this.updateSubwayModeBySettings();
            }
        };
        this.mEventListener = new BaseEvent.IEventListener() { // from class: com.android.phone.NetworkMetricsController.2
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                ForegroundInfo foregroundInfo;
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    if (((DeviceStateEvent.DisplayEvent) baseEvent).isDefaultDisplay()) {
                        NetworkMetricsController.this.mHandler.obtainMessage(3).sendToTarget();
                    }
                } else {
                    if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null) {
                        return;
                    }
                    NetworkMetricsController.this.mHandler.sendMessageDelayed(NetworkMetricsController.this.mHandler.obtainMessage(26, foregroundInfo), 1000L);
                }
            }
        };
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.NetworkMetricsController.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                boolean booleanExtra;
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                NetworkMetricsController.log("receive action".concat(action));
                boolean z = true;
                if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    if (networkInfo != null && networkInfo.getType() == 0) {
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED && !NetworkMetricsController.this.mIsMobileDataOn) {
                            NetworkMetricsController.this.mHandler.obtainMessage(2, Boolean.TRUE).sendToTarget();
                            return;
                        } else {
                            if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED && NetworkMetricsController.this.mIsMobileDataOn) {
                                NetworkMetricsController.this.mHandler.obtainMessage(2, Boolean.FALSE).sendToTarget();
                                return;
                            }
                            return;
                        }
                    }
                    if (networkInfo == null || networkInfo.getType() != 1) {
                        return;
                    }
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED && !NetworkMetricsController.this.mIsWifiDataOn) {
                        NetworkMetricsController.this.mHandler.obtainMessage(21, Boolean.TRUE).sendToTarget();
                        return;
                    } else {
                        if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED && NetworkMetricsController.this.mIsWifiDataOn) {
                            NetworkMetricsController.this.mHandler.obtainMessage(21, Boolean.FALSE).sendToTarget();
                            return;
                        }
                        return;
                    }
                }
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    NetworkMetricsController.this.mHandler.obtainMessage(5).sendToTarget();
                    return;
                }
                if (action.equals("com.miui.powerkeeper_sleep_changed")) {
                    NetworkMetricsController.this.mHandler.obtainMessage(74, intent).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.LINK_RELIABILITY")) {
                    NetworkMetricsController.log("onReceive ACTION_LINK_RELIABILITY ");
                    NetworkMetricsController.this.mHandler.removeMessages(52);
                    NetworkMetricsController.this.mHandler.sendMessageDelayed(NetworkMetricsController.this.mHandler.obtainMessage(52, intent.getIntExtra("netId", -1), intent.getIntExtra("metricsCause", -1)), 100L);
                    return;
                }
                if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                    NetworkMetricsController.this.mHandler.obtainMessage(10).sendToTarget();
                    return;
                }
                if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    NetworkMetricsController.this.mHandler.obtainMessage(11, intent).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.CHANGE_LEVEL")) {
                    NetworkMetricsController.this.mHandler.obtainMessage(12, intent).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.CLOUD_PING_PONG_LIST_DONE")) {
                    NetworkMetricsController.this.mHandler.obtainMessage(22).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.CLOUD_COMMON_SIGNAL_LIST_DONE")) {
                    NetworkMetricsController.this.mHandler.obtainMessage(27).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.TPUT_OPTIMIZATION")) {
                    NetworkMetricsController.this.isTputAppInForground = intent.getBooleanExtra("optimizationEnabled", false);
                    NetworkMetricsController.log("tput test app status is " + NetworkMetricsController.this.isTputAppInForground);
                    return;
                }
                if (action.equals("com.android.phone.intent.action.DATA_NETWORK_FAIL")) {
                    int intExtra = intent.getIntExtra("dataNetworkFailCause", 3000008);
                    String stringExtra = intent.getStringExtra("triggerScene");
                    NetworkMetricsController.this.mIsUnsolDrbReleaseByRrc = intent.getBooleanExtra("isDelay", false);
                    NetworkMetricsController.this.mHandler.sendMessageDelayed(NetworkMetricsController.this.mHandler.obtainMessage(20, intExtra, 0, stringExtra), NetworkMetricsController.this.mIsUnsolDrbReleaseByRrc ? 3000 : 0);
                    return;
                }
                if (action.equals("com.android.phone.intent.action.BUILDING_MODE_STATUS_CHANGED")) {
                    boolean booleanExtra2 = intent.getBooleanExtra("mIsBuildingModeStatus", false);
                    NetworkMetricsController.log("Building mode status is " + booleanExtra2);
                    NetworkMetricsController.this.mHandler.obtainMessage(23, Boolean.valueOf(booleanExtra2)).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.WECHAT_VOIP_SCENE") || action.equals("com.android.phone.intent.action.MEETING_VOIP_SCENE")) {
                    if (NetworkMetricsController.this.mIsWechatAppForC6C7) {
                        booleanExtra = intent.getBooleanExtra("meetingVoipEnabled", false);
                    } else {
                        if (!intent.getBooleanExtra("wechatVoipEnabled", false) && !intent.getBooleanExtra("meetingVoipEnabled", false)) {
                            z = false;
                        }
                        booleanExtra = z;
                    }
                    NetworkMetricsController.this.mHandler.obtainMessage(24, Boolean.valueOf(booleanExtra)).sendToTarget();
                    return;
                }
                if (action.equals("miui.intent.action.VIDEO_INFO")) {
                    NetworkMetricsController.this.mHandler.obtainMessage(34, Integer.valueOf(intent.getIntExtra("render-quality", -1))).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.MODEM_AI_LINKQOS_CHANGED")) {
                    NetworkMetricsController.log("onReceive ACTION_LINKQOS_PERCEPTION ");
                    if (NetworkMetricsController.this.mHandler.hasMessages(59)) {
                        NetworkMetricsController.this.mHandler.removeMessages(59);
                    }
                    NetworkMetricsController.this.mHandler.obtainMessage(59, intent).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.MODEM_AI_MODEMQOE_CHANGED")) {
                    NetworkMetricsController.log("onReceive ACTION_MODEM_QOE_PERCEPTION ");
                    if (NetworkMetricsController.this.mHandler.hasMessages(62)) {
                        NetworkMetricsController.this.mHandler.removeMessages(62);
                    }
                    NetworkMetricsController.this.mHandler.obtainMessage(62, intent).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED".equals(action)) {
                    NetworkMetricsController.this.mHandler.obtainMessage(60, intent).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.MOVELIKE_STATE_CHANGED".equals(action)) {
                    NetworkMetricsController.this.mHandler.obtainMessage(63, intent).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.CLOUD_HYPER_ENGINE_GAME_SWITCH".equals(action)) {
                    NetworkMetricsController networkMetricsController = NetworkMetricsController.this;
                    networkMetricsController.mIsHyperEngineGameCloudOn = networkMetricsController.getHyperEngineGameSwitch();
                    NetworkMetricsController.localLog("cloudRecords", "isHyperEngineGameCloudOn=" + NetworkMetricsController.this.mIsHyperEngineGameCloudOn);
                    return;
                }
                if ("com.android.phone.intent.action.CLOUD_HYPER_ENGINE_HST_SWITCH".equals(action)) {
                    NetworkMetricsController networkMetricsController2 = NetworkMetricsController.this;
                    networkMetricsController2.mIsHyperEngineHstCloudOn = networkMetricsController2.getHyperEngineHstSwitch();
                    NetworkMetricsController.localLog("cloudRecords", "isHyperEngineHstCloudOn=" + NetworkMetricsController.this.mIsHyperEngineHstCloudOn);
                    return;
                }
                if ("com.android.phone.intent.action.CLOUD_HYPER_ENGINE_SMARTHO_SWITCH".equals(action)) {
                    NetworkMetricsController networkMetricsController3 = NetworkMetricsController.this;
                    networkMetricsController3.mIsHyperEngineSmartHoCloudOn = networkMetricsController3.getHyperEngineSmartHoSwitch();
                    NetworkMetricsController.localLog("cloudRecords", "isHyperEngineSmartHoCloudOn=" + NetworkMetricsController.this.mIsHyperEngineSmartHoCloudOn);
                    return;
                }
                if ("com.android.phone.intent.action.ACTION_CLOUD_HYPER_ENGINE_GAME_WHITE_LIST_DONE".equals(action)) {
                    NetworkMetricsController.this.mHandler.obtainMessage(40).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.ACTION_CLOUD_FIVEG_LB_STREAM_OPT_DONE".equals(action)) {
                    NetworkMetricsController.this.mHandler.obtainMessage(41).sendToTarget();
                } else if (action.equals("networkMetricsController.intent.debugall")) {
                    NetworkMetricsController.this.mHandler.obtainMessage(100, intent).sendToTarget();
                } else if (action.equals("networkMetricsController.intent.force_disable_nr2lte_for_lab_test")) {
                    NetworkMetricsController.this.mHandler.obtainMessage(101, intent).sendToTarget();
                }
            }
        };
        log("NetworkMetricsController init");
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("NetworkMetricsController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        MetricsHandler metricsHandler = new MetricsHandler(this.mWorkThread.getLooper());
        this.mHandler = metricsHandler;
        metricsHandler.sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        this.mCm = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mRecovery = NetworkSelfRecovery.make(this.mContext);
        this.mRilHook = RilHook.init(this.mContext);
        PowerManager powerManager = (PowerManager) this.mContext.getSystemService("power");
        this.mPowerManager = powerManager;
        this.mWakeLock = powerManager.newWakeLock(1, "NetworkMetricsController");
        mIsAiInfoSupport = MiuiPhoneUtils.isAiInfoSupported();
        boolean zIsHyperEngineSupported = MiuiPhoneUtils.isHyperEngineSupported();
        mIsHyperEngineSupported = zIsHyperEngineSupported;
        if (zIsHyperEngineSupported && MiuiPhoneUtils.isSupportHEGException()) {
            HyperEngineGameController.init(this.mContext);
        }
        initValues();
        initBandData();
        initPingPongWhiteList();
        initCommonSignalWhiteList();
        initHyperEngineGameWhiteList();
        SystemProperties.set("ril.smap.badcell", "");
        this.mIsHyperEngineGameCloudOn = getHyperEngineGameSwitch();
        this.mIsHyperEngineHstCloudOn = getHyperEngineHstSwitch();
        this.mIsHyperEngineSmartHoCloudOn = getHyperEngineSmartHoSwitch();
        this.mHyperEngineGameOptDbStatus = getHyperEngineGameOptDbStatusByDb();
        this.mIsHyperEngineGameOptByDb = isHyperEngineGameOptEnabledByDb();
        log("mHyperEngineGameOptDbStatus=" + this.mHyperEngineGameOptDbStatus);
        this.mIsFivegLBStreamOptCloudOn = getFivegLBStreamOpt();
        this.mDdsAssistantInfoReceiver = createDdsAssistantInfoReceiver();
        registerAllInfo();
    }

    private synchronized void initValues() {
        try {
            this.mSupportFiveG = MiuiPhoneUtils.isFiveGCapable();
            this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
            this.mDdsPhoneId = getDefaultDataSlotId();
            this.mIsAirPlaneMode = isAirPlaneModeOn();
            this.mElapsedRealtimeFastLte = new LinkedList();
            this.mElapsedRealtimeFastNr = new LinkedList();
            this.mElapsedRealtimeGameStall = new LinkedList();
            this.mElapsedRealtimeVoipStall = new LinkedList();
            this.mRenderStallSamplesForBasement = new LinkedList();
            this.mQoeScoreSamplesForBasement = new LinkedList();
            this.mTencentGameStats = new ConcurrentHashMap();
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                Phone phone = MiuiPhoneUtils.getPhone(i);
                this.mServiceState[i] = new ServiceState();
                this.mSignalStrength[i] = new SignalStrength();
                this.mCellIdentity[i] = phone.getCurrentCellIdentity();
                this.mLteRsrp[i] = Integer.MAX_VALUE;
                this.mLteSnr[i] = Integer.MAX_VALUE;
                this.mNrRsrp[i] = Integer.MAX_VALUE;
                this.mNrSnr[i] = Integer.MAX_VALUE;
                this.mRoamingState[i] = Integer.MAX_VALUE;
            }
            if (this.mSupportFiveG) {
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    this.mFiveGInfo[i2] = new FiveGServiceInfo();
                }
                register5GServiceInfoListener();
                ThermalSceneListener thermalSceneListener = new ThermalSceneListener("/sys/class/thermal/thermal_message/poor_modem_limit");
                this.mThermalSceneListener = thermalSceneListener;
                thermalSceneListener.startWatching();
            }
            this.mIsFeatureEnable = MiuiPhoneUtils.isSpecialNetworkModeEnabled(this.mContext);
            this.mIsSmartDualSimFeatureEnable = isSmartDualSimSettingsOn();
            if (this.mIsFeatureEnable) {
                registerSpeedTrackerListener();
            }
            initBadNrCell();
            initBadLteCell();
            initElevatorCell();
            this.mSmartNetworkRecovery = SmartNetworkRecoveryController.getInstance();
            initSnsWhiteList();
        } catch (Throwable th) {
            throw th;
        }
    }

    private void initSnsWhiteList() {
        mConvVideoWhiteListDefault.addAll(getOptWhiteList("sns_datastall_conv_video_white_list_default"));
        mStreamingVideoWhiteListDefault.addAll(getOptWhiteList("sns_datastall_streaming_video_white_list_default"));
        mWebBrowsingWhiteListDefault.addAll(getOptWhiteList("sns_datastall_web_browsing_white_list_default"));
        mFileTransferWhiteListDefault.addAll(getOptWhiteList("sns_datastall_file_transfer_white_list_default"));
        mGamingWhiteListDefault.addAll(getOptWhiteList("sns_datastall_gaming_white_list_default"));
        mShortVideoWhiteListDefault.addAll(getOptWhiteList("sns_datastall_short_video_white_list_default"));
        log("initModemAiQoeWhiteList:" + mConvVideoWhiteListDefault.toString() + mStreamingVideoWhiteListDefault.toString() + mWebBrowsingWhiteListDefault.toString() + mFileTransferWhiteListDefault.toString() + mGamingWhiteListDefault.toString() + mShortVideoWhiteListDefault.toString());
    }

    public List getOptWhiteList(String str) {
        if (str == null) {
            return new ArrayList();
        }
        List stringArray = HyperOSCustFeatureResolve.getStringArray(str);
        return stringArray != null ? stringArray : new ArrayList();
    }

    private void initBadNrCell() {
        this.mNrCellIdMap.put(5679759364L, 664);
        this.mNrCellIdMap.put(5679759365L, 665);
        this.mNrCellIdMap.put(5725405188L, 997);
        this.mNrCellIdMap.put(5767893023L, 120);
        this.mNrCellIdMap.put(5767893723L, 120);
        this.mNrCellIdMap.put(5801672716L, 616);
        this.mNrCellIdMap.put(5801673416L, 616);
        this.mNrCellIdMap.put(5801672715L, 615);
        this.mNrCellIdMap.put(5801673415L, 615);
        this.mNrCellIdMap.put(5752762380L, 49);
        this.mNrCellIdMap.put(5752763080L, 49);
        this.mNrCellIdMap.put(5767893013L, 453);
        this.mNrCellIdMap.put(5767893713L, 453);
        this.mNrCellIdMap.put(5767893014L, 454);
        this.mNrCellIdMap.put(5767893714L, 454);
        this.mNrCellIdMap.put(5752999958L, 800);
        this.mNrCellIdMap.put(5753000658L, 800);
        this.mNrCellIdMap.put(5752999957L, 655);
        this.mNrCellIdMap.put(5753000657L, 655);
        this.mNrCellIdMap.put(5807325185L, 846);
    }

    private void initBadLteCell() {
        this.mLteCellIdMap.put(227985097L, 369);
    }

    private void initElevatorCell() {
        this.mElevatorNrCellIdMap.put(42961108996L, 851);
        this.mElevatorLteCellIdMap.put(87419652L, 468);
        this.mElevatorLteCellIdMap.put(87419657L, 143);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPingPongWhiteList() {
        if (mLteNrPingPongWhiteList.size() > 0) {
            mLteNrPingPongWhiteList.clear();
        }
        List pingPongWhiteListByCloud = FiveGManagerBase.getPingPongWhiteListByCloud(this.mContext);
        mLteNrPingPongWhiteList = pingPongWhiteListByCloud;
        if (!this.VDBG || pingPongWhiteListByCloud.size() == 0) {
            return;
        }
        log("mLteNrPingPongWhiteList=" + mLteNrPingPongWhiteList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initHyperEngineGameWhiteList() {
        if (mHyperEngineGameWhiteList.size() > 0) {
            mHyperEngineGameWhiteList.clear();
        }
        mHyperEngineGameWhiteList = FiveGManagerBase.getHyperEngineGameWhiteListByCloud(this.mContext);
        localLog("cloudRecords", "hyperEngineGameListSize=" + mHyperEngineGameWhiteList.size());
        if (mHyperEngineGameWhiteList.size() != 0) {
            log("mHyperEngineGameWhiteList=" + mHyperEngineGameWhiteList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initCommonSignalWhiteList() {
        if (mCommonSignalWhiteList.size() > 0) {
            mCommonSignalWhiteList.clear();
        }
        List commonSignalWhiteListByCloud = FiveGManagerBase.getCommonSignalWhiteListByCloud(this.mContext);
        mCommonSignalWhiteList = commonSignalWhiteListByCloud;
        if (!this.VDBG || commonSignalWhiteListByCloud.size() == 0) {
            return;
        }
        log("mCommonSignalWhiteList=" + mCommonSignalWhiteList);
    }

    class MetricsHandler extends Handler {
        MetricsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (NetworkMetricsController.this.VDBG) {
                NetworkMetricsController.log("handleMessage=" + message.what);
            }
            int i = message.what;
            if (i == 1) {
                NetworkMetricsController.this.initAction();
                return;
            }
            if (i == 2) {
                NetworkMetricsController.this.processMobileStateEvent(((Boolean) message.obj).booleanValue());
                return;
            }
            if (i == 3) {
                boolean zIsScreenOn = MiuiPhoneUtils.isScreenOn(NetworkMetricsController.this.mContext);
                if (NetworkMetricsController.this.mIsScreenOn != zIsScreenOn) {
                    NetworkMetricsController.this.mIsScreenOn = zIsScreenOn;
                    NetworkMetricsController.this.processScreenStateChanged();
                    return;
                }
                return;
            }
            if (i == 15) {
                NetworkMetricsController.this.processDepriorityNr2Lte();
                return;
            }
            if (i == 100) {
                Intent intent = (Intent) message.obj;
                if (intent == null) {
                    NetworkMetricsController.log("EVENT_DEBUG_ALL: intent is null, skip");
                    return;
                }
                NetworkMetricsController.this.mCommonSceneSignalTime = intent.getIntExtra("mCommonSceneSignalTime", 60);
                NetworkMetricsController.this.mPingPongNightTest = intent.getBooleanExtra("mPingPongNightTest", false);
                NetworkMetricsController.this.mElapsedRealtimeFastNr.clear();
                NetworkMetricsController.this.mPenaltyForceNrTime = 0L;
                NetworkMetricsController.this.mElapsedRealtimeFastLte.clear();
                NetworkMetricsController.this.mPenaltyForceLteTime = 0L;
                NetworkMetricsController.this.mSameRecoveryActionMinInterval = intent.getIntExtra("mSameRecoveryActionMinInterval", 1200000);
                NetworkMetricsController.this.mBasementVoipRsrpThreshold = intent.getIntExtra("mBasementVoipRsrpThreshold", -100);
                NetworkMetricsController.log("EVENT_DEBUG_ALL mBasementVoipRsrpThreshold=" + NetworkMetricsController.this.mBasementVoipRsrpThreshold);
                NetworkMetricsController.this.mBasementVoipLteRsrpEsaThreshold = intent.getIntExtra("mBasementVoipLteRsrpEsaThreshold", -115);
                NetworkMetricsController.log("EVENT_DEBUG_ALL mBasementVoipLteRsrpEsaThreshold=" + NetworkMetricsController.this.mBasementVoipLteRsrpEsaThreshold);
                NetworkMetricsController.this.mBasementVoipOutTriggerEnabled = intent.getBooleanExtra("mBasementVoipOutTriggerEnabled", false);
                NetworkMetricsController.log("EVENT_DEBUG_ALL mBasementVoipOutTriggerEnabled=" + NetworkMetricsController.this.mBasementVoipOutTriggerEnabled);
                long longExtra = intent.getLongExtra("mBasementVoipTestCellId", 0L);
                if (longExtra != 0) {
                    NetworkMetricsController.BASEMENT_VOIP_WHITE_CELL_SET.add(Long.valueOf(longExtra));
                    NetworkMetricsController.log("EVENT_DEBUG_ALL added testCellId=" + longExtra + ", whitelist=" + NetworkMetricsController.BASEMENT_VOIP_WHITE_CELL_SET);
                }
                boolean booleanExtra = intent.getBooleanExtra("testSelfStudySns", false);
                boolean booleanExtra2 = intent.getBooleanExtra("testSelfStudyForce", false);
                if (booleanExtra) {
                    NetworkMetricsController.this.setDataStallOptimization(1, booleanExtra2, Scene.DATA_STALL_SELFSTUDY);
                }
                if (intent.getBooleanExtra("testVoipStall", false)) {
                    NetworkMetricsController.this.mIsVoipForgroud = true;
                    NetworkMetricsController.this.processDataStall(Scene.VOIP);
                    NetworkMetricsController.this.mIsVoipForgroud = false;
                }
                if (intent.getBooleanExtra("testLiveStall", false)) {
                    NetworkMetricsController.this.processDataStall(Scene.LIVE);
                }
                if (intent.getBooleanExtra("testGameStall", false)) {
                    NetworkMetricsController.this.mIsGameStatisticForeground = true;
                    NetworkMetricsController.this.processDataStall(Scene.GAME);
                    NetworkMetricsController.this.mIsGameStatisticForeground = false;
                }
                NetworkMetricsController.this.VDBG = true;
                NetworkMetricsController.log("EVENT_DEBUG_ALL Enter mCommonSceneSignalTime" + NetworkMetricsController.this.mCommonSceneSignalTime + ", mSameRecoveryActionMinInterval" + NetworkMetricsController.this.mSameRecoveryActionMinInterval);
                return;
            }
            if (i != 101) {
                switch (i) {
                    case 5:
                        int defaultDataSlotId = NetworkMetricsController.this.getDefaultDataSlotId();
                        if (NetworkMetricsController.this.mIsFeatureEnable && defaultDataSlotId != NetworkMetricsController.this.mDdsPhoneId) {
                            if (NetworkMetricsController.this.mLastDataStallDirection != -1) {
                                NetworkMetricsController networkMetricsController = NetworkMetricsController.this;
                                networkMetricsController.resetDataStallOptimization(networkMetricsController.mDdsPhoneId);
                            }
                            NetworkMetricsController.this.restoreNrIfIndeed(Scene.DATA_SLOT);
                            NetworkMetricsController.this.mIsElevatorDeprioritized = false;
                            NetworkMetricsController.this.resetLteNrPingPongInfo();
                            NetworkMetricsController.this.resetSignalStrength();
                            NetworkMetricsController.this.mLastRecoveryAction = -1;
                        }
                        NetworkMetricsController.this.mDdsPhoneId = defaultDataSlotId;
                        break;
                    case 6:
                        NetworkMetricsController.this.updateFiveGInfo(message.arg1, (FiveGServiceInfo) message.obj);
                        break;
                    case 7:
                        boolean zIsSpecialNetworkModeEnabled = MiuiPhoneUtils.isSpecialNetworkModeEnabled(NetworkMetricsController.this.mContext);
                        if (zIsSpecialNetworkModeEnabled != NetworkMetricsController.this.mIsFeatureEnable) {
                            NetworkMetricsController networkMetricsController2 = NetworkMetricsController.this;
                            if (zIsSpecialNetworkModeEnabled) {
                                networkMetricsController2.registerSpeedTrackerListener();
                            } else {
                                networkMetricsController2.unregisterSpeedTrackerListener();
                                NetworkMetricsController.this.restoreNrIfIndeed(Scene.SETTINGS_NW_OPT_FEATURE);
                            }
                        }
                        NetworkMetricsController.this.mIsFeatureEnable = zIsSpecialNetworkModeEnabled;
                        break;
                    case 8:
                        boolean zIsLteNrRegistered = NetworkMetricsController.this.isLteNrRegistered();
                        NetworkMetricsController networkMetricsController3 = NetworkMetricsController.this;
                        if (!zIsLteNrRegistered) {
                            if (networkMetricsController3.isNecessaryCase(networkMetricsController3.mDdsPhoneId) && !NetworkMetricsController.this.isSmartDualSimActive()) {
                                NetworkMetricsController.this.doRecoveryForFastLte(4);
                                if (NetworkMetricsController.this.mForceLteCount < (NetworkMetricsController.this.mGlobalHstState ? 3 : 1)) {
                                    int i2 = (NetworkMetricsController.this.mForceLteCount * 20000) + 20000;
                                    NetworkMetricsController.this.mForceLteCount++;
                                    removeMessages(18);
                                    sendMessageDelayed(obtainMessage(18), i2);
                                }
                                break;
                            }
                        } else {
                            networkMetricsController3.stopForceLteMonitorTimer();
                            NetworkMetricsController.log("4/5G not need forceLTE");
                            break;
                        }
                        break;
                    case 9:
                        NetworkMetricsController networkMetricsController4 = NetworkMetricsController.this;
                        networkMetricsController4.mIsSmartDualSimFeatureEnable = networkMetricsController4.isSmartDualSimSettingsOn();
                        break;
                    case 10:
                        NetworkMetricsController networkMetricsController5 = NetworkMetricsController.this;
                        networkMetricsController5.mIsAirPlaneMode = networkMetricsController5.isAirPlaneModeOn();
                        if (NetworkMetricsController.this.mIsFeatureEnable && NetworkMetricsController.this.mIsAirPlaneMode) {
                            NetworkMetricsController.this.restoreNrIfIndeed(Scene.AIRPLANE_MODE);
                            NetworkMetricsController.this.stopForceLteMonitorTimer();
                            NetworkMetricsController.this.mLastRecoveryAction = -1;
                        }
                        NetworkMetricsController.this.mStateChangeTime = System.currentTimeMillis();
                        break;
                    case 11:
                        Intent intent2 = (Intent) message.obj;
                        String stringExtra = intent2.getStringExtra("ss");
                        int intExtra = intent2.getIntExtra("phone", -1);
                        if (SubscriptionManager.isValidPhoneId(intExtra)) {
                            NetworkMetricsController.log("onSimStateChanged mIsFeatureEnable= " + NetworkMetricsController.this.mIsFeatureEnable + ", simState=" + stringExtra + ", phoneId=" + intExtra + ", mDdsPhoneId=" + NetworkMetricsController.this.mDdsPhoneId);
                            if (NetworkMetricsController.this.mIsFeatureEnable && "ABSENT".equals(stringExtra) && intExtra == NetworkMetricsController.this.mDdsPhoneId) {
                                NetworkMetricsController.this.restoreNrIfIndeed(Scene.SIM_STATE);
                                NetworkMetricsController.this.stopForceLteMonitorTimer();
                                NetworkMetricsController.this.mLastRecoveryAction = -1;
                            }
                            NetworkMetricsController.this.mStateChangeTime = System.currentTimeMillis();
                            break;
                        }
                        break;
                    case 12:
                        Intent intent3 = (Intent) message.obj;
                        boolean z = intent3.getLongExtra("Level_UL", 1L) == 2 && intent3.getLongExtra("Level_DL", 1L) == 2;
                        if (!NetworkMetricsController.mIsAiInfoSupport) {
                            NetworkMetricsController.this.processGameStatus(z);
                        }
                        NetworkMetricsController.log("EVENT_GAME_SCENE_CHANGED game=" + z + ",feature=" + NetworkMetricsController.this.mIsFeatureEnable + ",depri=" + NetworkMetricsController.this.mDepriorityNr2Lte);
                        if (NetworkMetricsController.this.mIsFeatureEnable && z != NetworkMetricsController.this.mGame && !z && NetworkMetricsController.this.mDepriorityNr2Lte) {
                            NetworkMetricsController.this.mDepriorityNr2Lte = false;
                            NetworkMetricsController.this.restoreNrIfIndeed(Scene.GAME);
                        }
                        NetworkMetricsController.this.mGame = z;
                        if (NetworkMetricsController.mIsHyperEngineSupported) {
                            boolean z2 = z && NetworkMetricsController.this.isHyperEngineWhiteList();
                            NetworkMetricsController.log("hyperEngineGameStateChange last=" + NetworkMetricsController.this.mIsHyperEngineGame + ", cur=" + z2);
                            if (NetworkMetricsController.this.mIsHyperEngineGame != z2) {
                                NetworkMetricsController.this.mIsHyperEngineGame = z2;
                                NetworkMetricsController.this.processGameStatusForHyperEngine();
                                NetworkMetricsController networkMetricsController6 = NetworkMetricsController.this;
                                networkMetricsController6.updateHsrOpt(networkMetricsController6.mCurrentHstState);
                            }
                        }
                        break;
                    case 13:
                        if (NetworkMetricsController.this.mIsFeatureEnable) {
                            NetworkMetricsController.this.restoreNrIfIndeed(Scene.SETTINGS_FIVEG);
                        }
                        break;
                    default:
                        switch (i) {
                            case 18:
                                boolean zIsLteNrRegistered2 = NetworkMetricsController.this.isLteNrRegistered();
                                NetworkMetricsController.log("force lte retry,isLteNrRegistered =" + zIsLteNrRegistered2);
                                if (!zIsLteNrRegistered2) {
                                    NetworkMetricsController networkMetricsController7 = NetworkMetricsController.this;
                                    if (networkMetricsController7.isNecessaryCase(networkMetricsController7.mDdsPhoneId)) {
                                        sendMessage(obtainMessage(8));
                                    }
                                }
                                break;
                            case 19:
                                NetworkMetricsController.log("EVENT_SPEED_CHANGE");
                                NetworkMetricsController.this.processSpeedChange((NetworkSpeedTracker.Scene) message.obj);
                                break;
                            case 20:
                                String str = (String) message.obj;
                                if ("SETUP_DATA_FAIL".equals(str) && (!NetworkMetricsController.this.mIsMobileDataOn || NetworkMetricsController.this.VDBG)) {
                                    NetworkMetricsController.this.processDataNetworkFailed(message.arg1);
                                } else if ("FAKE_CONNECTION".equals(str) && (!NetworkMetricsController.this.mIsWifiDataOn || NetworkMetricsController.this.VDBG)) {
                                    NetworkMetricsController.this.processDataNetworkFailed(message.arg1);
                                }
                                NetworkMetricsController.this.mIsUnsolDrbReleaseByRrc = false;
                                break;
                            case 21:
                                NetworkMetricsController.this.processWifiStateEvent(((Boolean) message.obj).booleanValue());
                                NetworkMetricsController.this.processGameDelayWifiStateChanged(((Boolean) message.obj).booleanValue());
                                break;
                            case 22:
                                NetworkMetricsController.log("EVENT_LTENR_PING_PONG_WHIST_LIST_CHANGED");
                                NetworkMetricsController.this.initPingPongWhiteList();
                                break;
                            case 23:
                                boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                                if (NetworkMetricsController.this.mIsFeatureEnable && zBooleanValue != NetworkMetricsController.this.mIsBuildingMode && !zBooleanValue && NetworkMetricsController.this.mDepriorityNr2Lte) {
                                    NetworkMetricsController.log("EVENT_BUILDING_MODE_STATUS_CHANGED");
                                    NetworkMetricsController.this.mDepriorityNr2Lte = false;
                                    NetworkMetricsController.this.restoreNrIfIndeed(Scene.BUILDING_MODE);
                                }
                                NetworkMetricsController.this.mIsBuildingMode = zBooleanValue;
                                break;
                            case 24:
                                boolean zBooleanValue2 = ((Boolean) message.obj).booleanValue();
                                if (zBooleanValue2 != NetworkMetricsController.this.mIsVoipForgroud) {
                                    NetworkMetricsController.this.mIsVoipForgroud = zBooleanValue2;
                                    if (!NetworkMetricsController.this.mIsVoipForgroud) {
                                        NetworkMetricsController.this.mVideoQuality = -1;
                                        NetworkMetricsController.this.doEsaForBasement();
                                    }
                                    NetworkMetricsController.this.processCommonSignalScene(Scene.VOIP);
                                    if (!NetworkMetricsController.this.mIsVoipForgroud && NetworkMetricsController.this.mLastDataStallDirection != -1 && MiuiPhoneUtils.isSnsOptimizationSupported()) {
                                        NetworkMetricsController networkMetricsController8 = NetworkMetricsController.this;
                                        networkMetricsController8.resetDataStallOptimization(networkMetricsController8.mDdsPhoneId);
                                        break;
                                    }
                                }
                                break;
                            default:
                                switch (i) {
                                    case 26:
                                        NetworkMetricsController.this.processForegroundInfoEvent((ForegroundInfo) message.obj);
                                        break;
                                    case 27:
                                        NetworkMetricsController.log("EVENT_COMMON_SIGNAL_WHIST_LIST_CHANGED");
                                        NetworkMetricsController.this.initCommonSignalWhiteList();
                                        break;
                                    case DeviceStateListenerBase.TYPE_HORIZONTAL_GAME_LIST_STATE /* 28 */:
                                        NetworkMetricsController.log("EVENT_SETUP_DATA_FAIL_RECOVERY");
                                        NetworkMetricsController.this.recordSmartNetworkAutonomyTrigger("SETUP_DATA_FAIL", message.arg1, message.arg2);
                                        break;
                                    case 29:
                                        boolean zIsNrRegisteredforNdds = NetworkMetricsController.this.isNrRegisteredforNdds();
                                        NetworkMetricsController networkMetricsController9 = NetworkMetricsController.this;
                                        if (!zIsNrRegisteredforNdds) {
                                            if (networkMetricsController9.isNecessaryCase(NetworkMetricsController.getAnotherPhoneId(networkMetricsController9.mDdsPhoneId)) && !NetworkMetricsController.this.isSmartDualSimActive() && NetworkMetricsController.this.needLteToNrforNdds()) {
                                                NetworkMetricsController.this.doRecoveryForFastNrNdds(9);
                                                if (NetworkMetricsController.this.mForceNrCount < 2) {
                                                    int i3 = (NetworkMetricsController.this.mForceNrCount * 20000) + 20000;
                                                    NetworkMetricsController.this.mForceNrCount++;
                                                    removeMessages(30);
                                                    sendMessageDelayed(obtainMessage(30), i3);
                                                }
                                                break;
                                            }
                                        } else {
                                            networkMetricsController9.stopForceNrMonitorTimer();
                                            NetworkMetricsController.log("5G not need forceNr");
                                            break;
                                        }
                                        break;
                                    case DeviceStateListenerBase.TYPE_HST_ENHANCEMENT_STATE /* 30 */:
                                        boolean zIsNrRegisteredforNdds2 = NetworkMetricsController.this.isNrRegisteredforNdds();
                                        NetworkMetricsController.log("force nr retry,isNrRegistered =" + zIsNrRegisteredforNdds2);
                                        if (!zIsNrRegisteredforNdds2) {
                                            NetworkMetricsController networkMetricsController10 = NetworkMetricsController.this;
                                            if (networkMetricsController10.isNecessaryCase(NetworkMetricsController.getAnotherPhoneId(networkMetricsController10.mDdsPhoneId))) {
                                                sendMessage(obtainMessage(29));
                                            }
                                        }
                                        break;
                                    case 31:
                                        NetworkMetricsController.this.doRecoveryForFastLte(4);
                                        break;
                                    case 33:
                                        if (!NetworkMetricsController.this.mIsHolderLevel4) {
                                        }
                                    case DynamicSarService.WIFI_DATA_STATE_TCP_STATE /* 32 */:
                                        NetworkMetricsController.this.processDataStall(Scene.GAME);
                                        break;
                                    case 34:
                                        int iIntValue = ((Integer) message.obj).intValue();
                                        if (iIntValue != NetworkMetricsController.this.mVideoQuality) {
                                            NetworkMetricsController.log("EVENT_VIDEO_INFO_CHANGED, videoQuality = " + iIntValue);
                                            NetworkMetricsController.this.mVideoQuality = iIntValue;
                                            NetworkMetricsController.this.processVideoInfoChanged();
                                        }
                                        break;
                                    case 36:
                                        if (!NetworkMetricsController.this.mIsHolderVoipStall) {
                                        }
                                    case MiRilHookBase.EVENT_OEMHOOK_NV_EFS_SYNC /* 35 */:
                                        boolean z3 = NetworkMetricsController.this.mIsVoipForgroud;
                                        NetworkMetricsController networkMetricsController11 = NetworkMetricsController.this;
                                        if (z3) {
                                            networkMetricsController11.processDataStall(Scene.VOIP);
                                        } else if (networkMetricsController11.isLiveStreamVideo()) {
                                            NetworkMetricsController.this.processDataStall(Scene.LIVE);
                                        }
                                        break;
                                    case 37:
                                        NetworkMetricsController.log("EVENT_FAKE_CONNECTION_RECOVERY");
                                        NetworkMetricsController.this.recordSmartNetworkAutonomyTrigger("FAKE_CONNECTION", -1, ((Integer) message.obj).intValue());
                                        break;
                                    case 38:
                                        NetworkMetricsController.log("EVENT_SETUP_DATA_FAIL_RECOVERY_TIME");
                                        NetworkMetricsController.this.recordSmartNetworkAutonomyEffect("SETUP_DATA_FAIL", message.arg1, message.arg2, ((Long) message.obj).longValue());
                                        break;
                                    case MiRilHookBase.EVENT_OEMHOOK_CARD_SLOT_STATUS_QUERY /* 39 */:
                                        NetworkMetricsController.log("EVENT_FAKE_CONNECTION_RECOVERY_TIME");
                                        NetworkMetricsController.this.recordSmartNetworkAutonomyEffect("FAKE_CONNECTION", -1, message.arg2, ((Long) message.obj).longValue());
                                        break;
                                    case 40:
                                        NetworkMetricsController.log("EVENT_HYPER_ENGINE_GAME_WHIST_LIST_CHANGED");
                                        NetworkMetricsController.this.initHyperEngineGameWhiteList();
                                        break;
                                    case 41:
                                        NetworkMetricsController networkMetricsController12 = NetworkMetricsController.this;
                                        networkMetricsController12.mIsFivegLBStreamOptCloudOn = networkMetricsController12.getFivegLBStreamOpt();
                                        if (!NetworkMetricsController.this.mIsFivegLBStreamOptCloudOn) {
                                            NetworkMetricsController.this.restoreNrInStreamMediaOptIfNeed();
                                        }
                                        break;
                                    case 42:
                                        if (NetworkMetricsController.this.mBasementVoipOutTriggerEnabled) {
                                            NetworkMetricsController.this.tryDsaForBasementVoip(true);
                                        }
                                        break;
                                    case 43:
                                        NetworkMetricsController.this.processBasementCellQoeChanged(message.arg1);
                                        break;
                                    case MiRilHookBase.EVENT_OEMHOOK_HVOLTE_MBN_CFG /* 44 */:
                                        NetworkMetricsController.this.tryEarlyEsaForBasement();
                                        break;
                                    case AbstractBaseTemplate.BIG_ICON_WIDTH_THRESHOLD_DP /* 45 */:
                                        if (!NetworkMetricsController.this.isBasementOptForeground() && NetworkMetricsController.this.mBasementDsaTime != 0) {
                                            NetworkMetricsController.this.doEsaForBasement();
                                        } else {
                                            NetworkMetricsController.this.checkLteRsrpForBasementEsa();
                                        }
                                        break;
                                    case 46:
                                        if (NetworkMetricsController.this.mIsFivegLBStreamOptEffectMonitor) {
                                            NetworkMetricsController networkMetricsController13 = NetworkMetricsController.this;
                                            networkMetricsController13.recordFivegLBStreamOpt(networkMetricsController13.mActionNetworkScore, NetworkMetricsController.this.mLastNetworkScore);
                                            NetworkMetricsController.this.mIsFivegLBStreamOptEffectMonitor = false;
                                        }
                                        break;
                                    default:
                                        switch (i) {
                                            case 50:
                                                NetworkMetricsController.this.processSignalStrengthEvent(message.arg1, (SignalStrength) message.obj);
                                                break;
                                            case 51:
                                                NetworkMetricsController.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                                                break;
                                            case 52:
                                                NetworkMetricsController.this.processLinkReliability(message.arg1, message.arg2);
                                                break;
                                            case 53:
                                                NetworkMetricsController.this.processCellLocationChangeEvent(message.arg1, (CellLocation) message.obj);
                                                break;
                                            case 54:
                                                NetworkMetricsController.this.processOosState(((Integer) message.obj).intValue());
                                                break;
                                            case 55:
                                                NetworkMetricsController.this.resetLteNrPingPongInfo();
                                                break;
                                            case 56:
                                                NetworkMetricsController.this.processGameDelay(((Integer) message.obj).intValue());
                                                break;
                                            case 57:
                                                NetworkMetricsController.this.processGameBattle(((Boolean) message.obj).booleanValue());
                                                break;
                                            case 58:
                                                NetworkMetricsController.this.processUidChangeEvent(message.arg1, (ArrayList) message.obj);
                                                break;
                                            case 59:
                                            case MiRilHookBase.EVENT_OEMHOOK_MODEM_STATS_DATA_GET /* 62 */:
                                                Intent intent4 = (Intent) message.obj;
                                                NetworkMetricsController.this.processModemAiPerceptionChanged(intent4.getIntExtra("modemAiScene", 0), intent4.getFloatExtra(a.L, 0.0f), intent4.getFloatExtra("appType", -1.0f));
                                                break;
                                            case 60:
                                                boolean booleanExtra3 = ((Intent) message.obj).getBooleanExtra("mIsHstMode", false);
                                                NetworkMetricsController.log("EVENT_HST_MODE_STATUS_CHANGED, hstState = " + booleanExtra3 + ", lastState=" + NetworkMetricsController.this.mCurrentHstState);
                                                if (Build.IS_INTERNATIONAL_BUILD) {
                                                    NetworkMetricsController.this.mGlobalHstState = booleanExtra3;
                                                } else {
                                                    if (NetworkMetricsController.mIsHyperEngineSupported && NetworkMetricsController.this.mCurrentHstState != booleanExtra3) {
                                                        NetworkMetricsController.this.updateHsrOpt(booleanExtra3);
                                                    }
                                                    NetworkMetricsController.this.mCurrentHstState = booleanExtra3;
                                                    if (NetworkMetricsController.this.mCurrentHstState) {
                                                        NetworkMetricsController.this.processRestoreSaForDepriorityNr2Lte();
                                                        NetworkMetricsController.this.processHstOptimization();
                                                    }
                                                }
                                                break;
                                            case 61:
                                                if (NetworkMetricsController.this.mVideoQuality == 0 && NetworkMetricsController.this.isSubwayAbnormalCellMatch()) {
                                                    NetworkMetricsController.this.processSubwaySignalMapOpt();
                                                    break;
                                                }
                                                break;
                                            case 63:
                                                NetworkMetricsController.this.mCurrentMoveLikeState = ((Intent) message.obj).getBooleanExtra("isMovelikeState", false);
                                                NetworkMetricsController.log("EVENT_MOVE_LIKE_STATUS_CHANGED, mCurrentMoveLikeState = " + NetworkMetricsController.this.mCurrentHstState);
                                                if (NetworkMetricsController.this.mCurrentMoveLikeState) {
                                                    NetworkMetricsController.this.processRestoreSaForDepriorityNr2Lte();
                                                }
                                                break;
                                            case DynamicSarService.WIFI_DATA_STATE_EXTRA_APP_STATE /* 64 */:
                                                NetworkMetricsController.log("EVENT_SUBWAY_IN, mCurrentSubwayState = " + NetworkMetricsController.this.mCurrentHstState);
                                                if (NetworkMetricsController.this.mCurrentSubwayState) {
                                                    NetworkMetricsController.this.processRestoreSaForDepriorityNr2Lte();
                                                }
                                                break;
                                            case 65:
                                                Bundle bundle = ((TelephonyEvent.NetworkEvent) message.obj).getBundle();
                                                if (bundle == null) {
                                                    NetworkMetricsController.log("onRoamingStateChanged extra is null");
                                                } else {
                                                    NetworkMetricsController.this.mRoamingState[bundle.getInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID)] = bundle.getInt("roaming_mode", 0);
                                                }
                                                break;
                                            case 66:
                                                NetworkMetricsController.this.processGameUpdateStatus(((Boolean) message.obj).booleanValue());
                                                break;
                                            case 67:
                                                NetworkMetricsController.this.restoreNrInStreamMediaOptIfNeed();
                                                break;
                                            case 68:
                                                NetworkMetricsController.this.processSubwayQoeChanged(message.arg1, message.arg2);
                                                break;
                                            case 69:
                                                NetworkMetricsController.this.mIsSubwayMode = message.arg1 != 0;
                                                NetworkMetricsController.log("EVENT_SUBWAY_MODE_STATUS_CHANGED mIsSubwayMode=" + NetworkMetricsController.this.mIsSubwayMode);
                                                break;
                                            case 70:
                                                NetworkMetricsController.this.registerSceneAwareCallback();
                                                break;
                                            case 71:
                                                NetworkMetricsController.log("EVENT_HYPER_ENGINE_GAME_OPT_CHANGED");
                                                NetworkMetricsController.this.updateHyperEngineGameOptStatus();
                                                break;
                                            case 72:
                                                NetworkMetricsController.this.processElevatorStateChanged(((Boolean) message.obj).booleanValue());
                                                break;
                                            case 73:
                                                NetworkMetricsController.this.processElevatorTimeout();
                                                break;
                                            case 74:
                                                NetworkMetricsController.this.mIsNightMode = 1 == ((Intent) message.obj).getIntExtra(MEModuleSignalT1s.EXTRA_STATE, 2);
                                                NetworkMetricsController.log("ACTION_SLEEP_CHANGED mIsNightMode:" + NetworkMetricsController.this.mIsNightMode);
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                }
                return;
            }
            Intent intent5 = (Intent) message.obj;
            if (intent5 == null) {
                NetworkMetricsController.log("EVENT_FORCE_DISABLE_NR2LTE_FOR_LAB_TEST: intent is null, skip");
                return;
            }
            boolean booleanExtra4 = intent5.getBooleanExtra("forceDisableNr2LteForLabTest", false);
            NetworkRecoveryStrategyBase networkRecoveryStrategyBase = NetworkRecoveryStrategyBase.getInstance();
            if (networkRecoveryStrategyBase instanceof NetworkRecoveryStrategyImpl) {
                ((NetworkRecoveryStrategyImpl) networkRecoveryStrategyBase).setForceDisableNr2LteForLabTest(booleanExtra4);
            }
            MiuiPhoneUtils.isSpecialTestEnv(NetworkMetricsController.this.mDdsPhoneId);
            NetworkMetricsController.log("EVENT_FORCE_DISABLE_NR2LTE_FOR_LAB_TEST forceDisableNr2LteForLabTest=" + booleanExtra4);
        }
    }

    private void registerAllInfo() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mEventListener);
        registerReceiver();
        registerTelephonyInfoCallBack();
        registerTeleStaisticCallBack();
        this.mSettingsObserver = new SettingsObserver(this, this.mContext, this.mHandler);
        registerSettingsCallbacks();
        registerForegroundInfoListener();
        regAppStateMonitor();
        registerRoamingStateCallBack();
        regSensorStateCallBack();
        registerSubwayStateObserver();
        registerSceneAwareCallback();
    }

    private void registerSubwayStateObserver() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("network_mode_state"), false, this.mSubwayStateObserver);
        updateSubwayModeBySettings();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSubwayModeBySettings() {
        boolean z = (Settings.Global.getInt(this.mContext.getContentResolver(), "network_mode_state", 0) & 3) != 0;
        if (this.mIsSubwayMode != z) {
            this.mIsSubwayMode = z;
            log("updateSubwayModeBySettings mIsSubwayMode=" + this.mIsSubwayMode);
        }
    }

    private void regAppStateMonitor() {
        MiuiAppStateMonitor miuiAppStateMonitorInit = MiuiAppStateMonitor.init();
        this.mMiuiAppStateMonitor = miuiAppStateMonitorInit;
        miuiAppStateMonitorInit.registerAppObserver(this.mCurrentTopAppObserver);
    }

    class CurrentTopAppObserver implements MiuiAppStateMonitor.TopAppObserver {
        private CurrentTopAppObserver() {
        }

        @Override // com.android.phone.utils.MiuiAppStateMonitor.TopAppObserver
        public void onTopAppChanged(List list, int i) {
            NetworkMetricsController.this.mHandler.obtainMessage(58, i, 0, list).sendToTarget();
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.LINK_RELIABILITY");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.CHANGE_LEVEL");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_PING_PONG_LIST_DONE");
        intentFilter.addAction("com.android.phone.intent.action.TPUT_OPTIMIZATION");
        intentFilter.addAction("com.android.phone.intent.action.DATA_NETWORK_FAIL");
        intentFilter.addAction("com.android.phone.intent.action.BUILDING_MODE_STATUS_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.WECHAT_VOIP_SCENE");
        intentFilter.addAction("com.android.phone.intent.action.MEETING_VOIP_SCENE");
        intentFilter.addAction("com.android.phone.intent.action.MODEM_AI_MODEMQOE_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.MODEM_AI_LINKQOS_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("networkMetricsController.intent.debugall");
        intentFilter.addAction("networkMetricsController.intent.force_disable_nr2lte_for_lab_test");
        intentFilter.addAction("miui.intent.action.VIDEO_INFO");
        intentFilter.addAction("com.android.phone.intent.action.MOVELIKE_STATE_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_HYPER_ENGINE_GAME_SWITCH");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_HYPER_ENGINE_HST_SWITCH");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_HYPER_ENGINE_SMARTHO_SWITCH");
        intentFilter.addAction("com.android.phone.intent.action.ACTION_CLOUD_HYPER_ENGINE_GAME_WHITE_LIST_DONE");
        intentFilter.addAction("com.android.phone.intent.action.ACTION_CLOUD_FIVEG_LB_STREAM_OPT_DONE");
        intentFilter.addAction("com.miui.powerkeeper_sleep_changed");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHstOptimization() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null || this.mFiveGInfo[this.mDdsPhoneId] == null) {
            log("processHstOptimization invalid params!");
            return;
        }
        int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
        boolean zIsConnectedOnNsaMode = this.mFiveGInfo[this.mDdsPhoneId].isConnectedOnNsaMode();
        log("processHstOptimization mCurrentHstState :" + this.mCurrentHstState + ",dataType=" + dataNetworkType + ",isNsaMode=" + zIsConnectedOnNsaMode);
        if (!isLteNwType(dataNetworkType) || zIsConnectedOnNsaMode || !isLtePoorThanNr(Scene.DUMMY, this.mDdsPhoneId) || 1 == getOperatorScoreByHST()) {
            return;
        }
        doRecoveryInHst(this.mDdsPhoneId);
    }

    private int getCurrentEndcStatus() {
        int i;
        int defaultDataSlotId = getDefaultDataSlotId();
        if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId)) {
            boolean[] endcPowerEnable = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getEndcPowerEnable();
            i = (endcPowerEnable.length <= 0 || !endcPowerEnable[defaultDataSlotId]) ? 0 : 1;
        } else {
            i = -1;
        }
        if (this.VDBG) {
            log("getCurrentEndcStatus = " + i);
        }
        return i;
    }

    private int getCurrentSaStatus() {
        int i;
        int defaultDataSlotId = getDefaultDataSlotId();
        if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId)) {
            boolean[] saPowerEnable = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSaPowerEnable();
            i = (saPowerEnable.length <= 0 || !saPowerEnable[defaultDataSlotId]) ? 0 : 1;
        } else {
            i = -1;
        }
        if (this.VDBG) {
            log("getCurrentSaStatus = " + i);
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    private void registerJoyoseInfoCallBack() {
        IMiGameBoosterCallback iMiGameBoosterCallback;
        log("registerJoyoseInfoCallBack");
        IJoyoseInterface iJoyoseInterface = this.mJs;
        if (iJoyoseInterface == null || (iMiGameBoosterCallback = this.mMiGameBoosterCallback) == null || this.mJsRegistered) {
            return;
        }
        try {
            iJoyoseInterface.registerCallbackInner(22, RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, iMiGameBoosterCallback);
            this.mJsRegistered = true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void unregisterJoyoseInfoCallBack() {
        IMiGameBoosterCallback iMiGameBoosterCallback;
        log("unregisterJoyoseInfoCallBack");
        IJoyoseInterface iJoyoseInterface = this.mJs;
        if (iJoyoseInterface == null || (iMiGameBoosterCallback = this.mMiGameBoosterCallback) == null || !this.mJsRegistered) {
            return;
        }
        try {
            iJoyoseInterface.unRegisterCallbackInner(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, iMiGameBoosterCallback);
            this.mLastDelayLevelStartTimes = 0L;
            this.mLastGameDelay = 0;
            this.mJsRegistered = false;
        } catch (RemoteException e) {
            e.printStackTrace();
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
            NetworkMetricsController.this.mHandler.obtainMessage(50, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            NetworkMetricsController.this.mHandler.obtainMessage(51, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            NetworkMetricsController.this.mHandler.obtainMessage(53, i, 0, cellLocation).sendToTarget();
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            HyperEngineGameController hyperEngineGameController;
            boolean z = i2 != 0;
            NetworkMetricsController.log("onPreciseCallStateChanged phoneId=" + i + ", foregroundCallState=" + i2 + ", isInCall=" + z);
            NetworkMetricsController.this.mIsInCall = z;
            if (NetworkMetricsController.mIsHyperEngineSupported && (hyperEngineGameController = HyperEngineGameController.getInstance()) != null) {
                hyperEngineGameController.onCallStateChanged(z);
            }
            NetworkMetricsController.this.resetLteNrPingPongInfo();
        }
    }

    private void registerTeleStaisticCallBack() {
        log("registerTelephonyInfoCallBack");
        if (this.mTeleStaisticCallBack == null) {
            log("new TeleStaisticCallBack");
            TeleStaisticCallBack teleStaisticCallBack = new TeleStaisticCallBack();
            this.mTeleStaisticCallBack = teleStaisticCallBack;
            TelephonyStatAdapter.registerStaisticCallBack(teleStaisticCallBack);
        }
    }

    class TeleStaisticCallBack implements TelephonyStatAdapter.IStaisticCallBack {
        TeleStaisticCallBack() {
        }

        public void onSetupDataFailRecovery(int i, int i2) {
            NetworkMetricsController.this.mHandler.obtainMessage(28, i, i2).sendToTarget();
        }

        public void onSetupDataFailRecoveryTime(int i, int i2, long j) {
            NetworkMetricsController.this.mHandler.obtainMessage(38, i, i2, Long.valueOf(j)).sendToTarget();
        }

        public void onFakeConnectionRecovery(int i) {
            NetworkMetricsController.this.mHandler.obtainMessage(37, Integer.valueOf(i)).sendToTarget();
        }

        public void onFakeConnectionRecoveryTime(int i, long j) {
            NetworkMetricsController.this.mHandler.obtainMessage(39, -1, i, Long.valueOf(j)).sendToTarget();
        }
    }

    public void recordSmartNetworkAutonomyTrigger(String str, int i, int i2) {
        localLog("smartNetworkAutonomyTrigger", str + "," + i + "," + i2);
        HashMap map = new HashMap();
        map.put("SmartNetworkAutonomyScene", str);
        map.put("SmartNetworkAutonomyFailCause", Integer.valueOf(i));
        map.put("SmartNetworkAutonomyRecoveryAction", Integer.valueOf(i2));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_smart_network_autonomy_trigger_statistic", map);
    }

    public void recordSmartNetworkAutonomyEffect(String str, int i, int i2, long j) {
        localLog("smartNetworkAutonomyEffect", str + "," + i + "," + i2 + "," + j);
        HashMap map = new HashMap();
        map.put("SmartNetworkAutonomyScene", str);
        map.put("SmartNetworkAutonomyFailCause", Integer.valueOf(i));
        map.put("SmartNetworkAutonomyRecoveryAction", Integer.valueOf(i2));
        map.put("SmartNetworkAutonomyRecoveryActionTime", Long.valueOf(j));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_smart_network_autonomy_effect_statistic", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSignalStrengthEvent(int i, SignalStrength signalStrength) {
        try {
            if (MiuiPhoneUtils.isValidatePhoneId(i) && signalStrength != null) {
                this.mSignalStrength[i] = new SignalStrength(signalStrength);
                updateSignalInfo(i, signalStrength);
            } else {
                log("processSignalStrengthEvent-invalid phone id!");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void updateSignalInfo(int i, SignalStrength signalStrength) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || i != this.mDdsPhoneId) {
            log("updateNwSignalInfo not allowed ,ignore");
            return;
        }
        String str = "";
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) cellSignalStrength;
                this.mLteRsrp[i] = inRangeOrUnavailable(cellSignalStrengthLte.getRsrp(), -140, -43);
                this.mLteSnr[i] = inRangeOrUnavailable(cellSignalStrengthLte.getRssnr(), -23, 40);
                log("updateSignalInfo lteRsrp=" + this.mLteRsrp[i] + ",lteSnr=" + this.mLteSnr[i] + ",phoneId=" + i);
                checkLteRsrpForBasementEsa();
                str = "lte";
            } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrength;
                this.mNrRsrp[i] = inRangeOrUnavailable(cellSignalStrengthNr.getSsRsrp(), -140, -44);
                this.mNrSnr[i] = inRangeOrUnavailable(cellSignalStrengthNr.getSsSinr(), -23, 40);
                log("updateSignalInfo nrRsrp= " + this.mNrRsrp[i] + ",nrSnr= " + this.mNrSnr[i] + ",phoneId= " + i);
                str = "nr";
            }
            if (this.mServiceState[this.mDdsPhoneId] == null) {
                return;
            }
            int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
            if ((str.equals("lte") && isLteNwType(dataNetworkType)) || (str.equals("nr") && isNrNwType(dataNetworkType))) {
                processCommonSignalScene(Scene.SIGNAL_STRENGTH);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processCommonSignalScene(Scene scene) {
        Scene scene2;
        Scene scene3;
        try {
            if (scene == null) {
                log("processCommonSignalScene sceneType is null");
                return;
            }
            if (isAllowedToRestoreNr()) {
                if (!this.mIsCommonSignalWhiteListForground && !this.mIsWechatAppForC6C7 && scene == (scene3 = Scene.SIGNAL_WHITE_LIST_BG) && this.mLastRecoveryAction != 1) {
                    restoreNrIfIndeed(scene3);
                    return;
                } else if (!this.mIsVoipForgroud && scene == (scene2 = Scene.VOIP)) {
                    restoreNrIfIndeed(scene2);
                    return;
                }
            }
            if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null && (this.mIsVoipForgroud || this.mIsCommonSignalWhiteListForground || this.mIsWechatAppForC6C7)) {
                log("processCommonSignalScene" + this.mIsVoipForgroud + "," + this.mIsCommonSignalWhiteListForground + "," + this.mIsWechatAppForC6C7);
                int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
                int operatorScoreByHST = getOperatorScoreByHST();
                if (isNrNwType(dataNetworkType) && isNrPoorForWhiteList(this.mDdsPhoneId) && 1 != operatorScoreByHST && (!MiuiPhoneUtils.isSupportVideoDataStallOptimization() || this.mCurApptype != AppType.STREAMING_VIDEO)) {
                    localLog("DSA", "s[" + scene.getValue() + "]");
                    doRecoveryIfNeed(53, this.mDdsPhoneId, false, scene);
                    dfsDeprioritizeOrRestoreSa(1, scene);
                    this.mLastCommonSignalDsaTime = System.currentTimeMillis();
                } else if (isLteNwType(dataNetworkType)) {
                    if (isBadLteCellForC6C7()) {
                        setDataStallOptimization(1, false, Scene.CELLCHANGED);
                    } else if (isLtePoorThanNr(scene, this.mDdsPhoneId) && isAllowedToRestoreNr() && 1 != operatorScoreByHST && System.currentTimeMillis() - this.mLastCommonSignalDsaTime > RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
                        restoreNrIfIndeed(scene);
                        this.mLastCommonSignalDsaTime = 0L;
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void processCellSignalSceneForC6C7(CellIdentity cellIdentity, int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && cellIdentity != null) {
            log("processCellSignalSceneForC6C7 phoneId=" + i);
            if (this.mIsHdrOptWhiteListForground && (cellIdentity instanceof CellIdentityNr) && isGoodNrHdrCellForC6C7()) {
                setHdrOptimizationForC6C7();
                return;
            }
            if (this.mIsVoipForgroud || this.mIsCommonSignalWhiteListForground || this.mIsWechatAppForC6C7) {
                if (cellIdentity instanceof CellIdentityNr) {
                    Scene scene = Scene.CELLCHANGED;
                    if (isBadNrCellForC6C7() && isNrPoorForWhiteList(i) && 1 != getOperatorScoreByHST()) {
                        localLog("DSA", "s[" + scene.getValue() + "]");
                        doRecoveryIfNeed(53, i, false, scene);
                        dfsDeprioritizeOrRestoreSa(1, scene);
                        this.mLastCommonSignalDsaTime = System.currentTimeMillis();
                    }
                } else if ((cellIdentity instanceof CellIdentityLte) && isBadLteCellForC6C7()) {
                    setDataStallOptimization(1, false, Scene.CELLCHANGED);
                }
            }
        }
    }

    private synchronized void processCellSignalSceneForElevator(CellIdentity cellIdentity, int i) {
        try {
            try {
                if (this.mIsInElevator && MiuiPhoneUtils.isValidatePhoneId(i) && cellIdentity != null) {
                    log("processCellSignalSceneForElevator phoneId=" + i);
                    if (cellIdentity instanceof CellIdentityNr) {
                        if (isBadNrCellForElevator() && !this.mIsWifiDataOn && isNrPoorForWhiteList(i)) {
                            doElevatorDeprioritize(i, Scene.CELLCHANGED);
                        }
                    } else if ((cellIdentity instanceof CellIdentityLte) && isBadLteCellForElevator() && !this.mIsWifiDataOn) {
                        doElevatorDeprioritize(i, Scene.CELLCHANGED);
                    }
                }
            } catch (Exception e) {
                log("processCellSignalSceneForElevator fail:" + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void doElevatorDeprioritize(int i, Scene scene) {
        localLog("DSA", "s[" + scene.getValue() + "]");
        doRecoveryIfNeed(53, i, false, scene);
        dfsDeprioritizeOrRestoreSa(1, scene);
        this.mIsElevatorDeprioritized = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processElevatorStateChanged(boolean z) {
        try {
            log("processElevatorStateChanged isInElevator=" + z + ", lastState=" + this.mIsInElevator);
            if (z) {
                enterElevator();
            } else {
                exitElevator(Scene.OUT_ELEVATOR);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void enterElevator() {
        try {
            this.mIsInElevator = true;
            this.mHandler.removeMessages(73);
            this.mHandler.sendEmptyMessageDelayed(73, 600000L);
            if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null) {
                CellIdentity cellIdentity = this.mCellIdentity[this.mDdsPhoneId];
                if (cellIdentity != null) {
                    processCellSignalSceneForElevator(cellIdentity, this.mDdsPhoneId);
                }
            }
        } catch (Exception e) {
            log("enterElevator fail:" + e);
        }
    }

    private synchronized void exitElevator(Scene scene) {
        try {
            this.mHandler.removeMessages(73);
            this.mIsInElevator = false;
            if (this.mIsElevatorDeprioritized) {
                restoreNrIfIndeed(scene);
                this.mIsElevatorDeprioritized = false;
            }
        } catch (Exception e) {
            log("exitElevator fail:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processElevatorTimeout() {
        log("processElevatorTimeout");
        exitElevator(Scene.OUT_ELEVATOR);
    }

    private boolean isBadNrCellForC6C7() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null) {
            return false;
        }
        int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
        if (this.mNrCi == null || !isNrNwType(dataNetworkType)) {
            return false;
        }
        long nci = this.mNrCi.getNci();
        int pci = this.mNrCi.getPci();
        if (!this.mNrCellIdMap.containsKey(Long.valueOf(nci)) || pci != ((Integer) this.mNrCellIdMap.get(Long.valueOf(nci))).intValue()) {
            return false;
        }
        localLog("isB_C_N", nci + "," + pci);
        return true;
    }

    private boolean isBadNrCellForElevator() {
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null && this.mNrCi != null && isNrNwType(this.mServiceState[this.mDdsPhoneId].getDataNetworkType())) {
            long nci = this.mNrCi.getNci();
            int pci = this.mNrCi.getPci();
            if (this.mElevatorNrCellIdMap.containsKey(Long.valueOf(nci)) && pci == ((Integer) this.mElevatorNrCellIdMap.get(Long.valueOf(nci))).intValue()) {
                localLog("isB_NE_N", nci + "," + pci);
                return true;
            }
        }
        return false;
    }

    private boolean isBadLteCellForC6C7() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null) {
            return false;
        }
        int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
        if (this.mLteCi == null || !isLteNwType(dataNetworkType)) {
            return false;
        }
        long ci = this.mLteCi.getCi();
        int pci = this.mLteCi.getPci();
        if (!this.mLteCellIdMap.containsKey(Long.valueOf(ci)) || pci != ((Integer) this.mLteCellIdMap.get(Long.valueOf(ci))).intValue()) {
            return false;
        }
        localLog("isB_C_L", ci + "," + pci);
        return true;
    }

    private boolean isBadLteCellForElevator() {
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null && this.mLteCi != null && isLteNwType(this.mServiceState[this.mDdsPhoneId].getDataNetworkType())) {
            long ci = this.mLteCi.getCi();
            int pci = this.mLteCi.getPci();
            if (this.mElevatorLteCellIdMap.containsKey(Long.valueOf(ci)) && pci == ((Integer) this.mElevatorLteCellIdMap.get(Long.valueOf(ci))).intValue()) {
                localLog("isB_NE_L", ci + "," + pci);
                return true;
            }
        }
        return false;
    }

    private boolean isGoodNrHdrCellForC6C7() {
        Phone phone;
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null && (phone = MiuiPhoneUtils.getPhone(this.mDdsPhoneId)) != null && MiuiPhoneUtils.isCmccSim(phone)) {
            int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
            if (this.mNrCi != null && isNrNwType(dataNetworkType) && this.mNrRsrp[this.mDdsPhoneId] != Integer.MAX_VALUE && this.mNrRsrp[this.mDdsPhoneId] >= -108) {
                long nci = this.mNrCi.getNci();
                if (5807325190L == nci) {
                    localLog("isG_C_N", String.valueOf(nci));
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:23:0x0043  */
    /* JADX WARN: Code duplicated, block: B:42:0x007b  */
    /* JADX WARN: Code duplicated, block: B:44:0x0083  */
    /* JADX WARN: Code duplicated, block: B:52:0x009b  */
    /* JADX WARN: Code duplicated, block: B:54:0x00a3  */
    /* JADX WARN: Code duplicated, block: B:59:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:61:0x00bc  */
    /* JADX WARN: Code duplicated, block: B:63:0x00c1  */
    /* JADX WARN: Code duplicated, block: B:72:0x00fc  */
    /* JADX WARN: Code duplicated, block: B:74:0x0102  */
    /* JADX WARN: Code duplicated, block: B:76:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Instruction removed from duplicated block: B:63:0x00c1, please report this as an issue */
    public void processForegroundInfoEvent(ForegroundInfo foregroundInfo) {
        boolean z;
        boolean zEquals;
        this.mCurrentForegroundPackageName = foregroundInfo.mForegroundPackageName;
        if (isBadNrCellForC6C7() || isBadLteCellForC6C7()) {
            if (MiuiPhoneUtils.isContainsPackage("config_bad_cell_app_list", this.mCurrentForegroundPackageName) && !this.mIsWechatAppForC6C7) {
                this.mIsWechatAppForC6C7 = true;
            } else if (MiuiPhoneUtils.isContainsPackage("config_bad_cell_app_list", this.mCurrentForegroundPackageName) || !this.mIsWechatAppForC6C7) {
                z = false;
            } else {
                this.mIsWechatAppForC6C7 = false;
            }
            z = true;
        } else if (MiuiPhoneUtils.isContainsPackage("config_bad_cell_app_list", this.mCurrentForegroundPackageName) || !this.mIsWechatAppForC6C7) {
            z = false;
        } else {
            this.mIsWechatAppForC6C7 = false;
            z = true;
        }
        if (isCommonSignalWhiteList(this.mCurrentForegroundPackageName) && !this.mIsCommonSignalWhiteListForground) {
            this.mIsCommonSignalWhiteListForground = true;
        } else {
            if (!isCommonSignalWhiteList(this.mCurrentForegroundPackageName) && this.mIsCommonSignalWhiteListForground) {
                this.mIsCommonSignalWhiteListForground = false;
            }
            if (!isHdrOptWhiteList(this.mCurrentForegroundPackageName) && !this.mIsHdrOptWhiteListForground) {
                this.mIsHdrOptWhiteListForground = true;
                if (isGoodNrHdrCellForC6C7()) {
                    setHdrOptimizationForC6C7();
                }
            } else if (!isHdrOptWhiteList(this.mCurrentForegroundPackageName) && this.mIsHdrOptWhiteListForground) {
                this.mIsHdrOptWhiteListForground = false;
            }
            if (!isFivegLBStreamMediaWhiteList(this.mCurrentForegroundPackageName) && !this.mIsFivegLBStreamMediaWhiteListForground) {
                this.mIsFivegLBStreamMediaWhiteListForground = true;
                stopStreamMediaExitTimerIfNeed();
            } else if (!isFivegLBStreamMediaWhiteList(this.mCurrentForegroundPackageName) && this.mIsFivegLBStreamMediaWhiteListForground) {
                this.mIsFivegLBStreamMediaWhiteListForground = false;
                startStreamMediaExitTimerIfNeed();
            }
            zEquals = "com.duowan.kiwi".equals(this.mCurrentForegroundPackageName);
            if (zEquals != this.mIsHuyaForeground) {
                this.mIsHuyaForeground = zEquals;
                if (!zEquals) {
                    doEsaForBasement();
                }
            }
            if (z) {
                localLog("processForegroundInfoEvent", this.mCurrentForegroundPackageName + "," + this.mIsCommonSignalWhiteListForground + "," + this.mIsWechatAppForC6C7);
                if (!this.mIsCommonSignalWhiteListForground || this.mIsWechatAppForC6C7) {
                    if (isBadLteCellForC6C7()) {
                        setDataStallOptimization(1, false, Scene.CELLCHANGED);
                    } else {
                        processCommonSignalScene(Scene.SIGNAL_WHITE_LIST_FG);
                    }
                }
                processCommonSignalScene(Scene.SIGNAL_WHITE_LIST_BG);
                return;
            }
        }
        z = true;
        if (!isHdrOptWhiteList(this.mCurrentForegroundPackageName)) {
            if (!isHdrOptWhiteList(this.mCurrentForegroundPackageName)) {
                this.mIsHdrOptWhiteListForground = false;
            }
        } else if (!isHdrOptWhiteList(this.mCurrentForegroundPackageName)) {
            this.mIsHdrOptWhiteListForground = false;
        }
        if (!isFivegLBStreamMediaWhiteList(this.mCurrentForegroundPackageName)) {
            if (!isFivegLBStreamMediaWhiteList(this.mCurrentForegroundPackageName)) {
                this.mIsFivegLBStreamMediaWhiteListForground = false;
                startStreamMediaExitTimerIfNeed();
            }
        } else if (!isFivegLBStreamMediaWhiteList(this.mCurrentForegroundPackageName)) {
            this.mIsFivegLBStreamMediaWhiteListForground = false;
            startStreamMediaExitTimerIfNeed();
        }
        zEquals = "com.duowan.kiwi".equals(this.mCurrentForegroundPackageName);
        if (zEquals != this.mIsHuyaForeground) {
            this.mIsHuyaForeground = zEquals;
            if (!zEquals) {
                doEsaForBasement();
            }
        }
        if (z) {
            localLog("processForegroundInfoEvent", this.mCurrentForegroundPackageName + "," + this.mIsCommonSignalWhiteListForground + "," + this.mIsWechatAppForC6C7);
            if (!this.mIsCommonSignalWhiteListForground) {
            }
            if (isBadLteCellForC6C7()) {
                setDataStallOptimization(1, false, Scene.CELLCHANGED);
            } else {
                processCommonSignalScene(Scene.SIGNAL_WHITE_LIST_FG);
            }
        }
    }

    private void startStreamMediaExitTimerIfNeed() {
        if (this.mLastRecoveryAction != 1) {
            return;
        }
        stopStreamMediaExitTimerIfNeed();
        this.mHandler.sendEmptyMessageDelayed(67, 30000L);
        log("startStreamMediaExitTimer");
    }

    private void stopStreamMediaExitTimerIfNeed() {
        if (this.mHandler.hasMessages(67)) {
            this.mHandler.removeMessages(67);
            log("stopStreamMediaExitTimer");
        }
    }

    public String getCurrentForegroundPackageName() {
        return this.mCurrentForegroundPackageName;
    }

    public boolean isCommonSignalWhiteList(String str) {
        if (str == null || mCommonSignalWhiteList.size() == 0) {
            return false;
        }
        Iterator it = mCommonSignalWhiteList.iterator();
        while (it.hasNext()) {
            if (str.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    private boolean isHdrOptWhiteList(String str) {
        if (TextUtils.isEmpty(str) || this.mHdrOptWhiteListForC6C7.isEmpty()) {
            return false;
        }
        return this.mHdrOptWhiteListForC6C7.contains(str);
    }

    private boolean isFivegLBStreamMediaWhiteList(String str) {
        if (TextUtils.isEmpty(str) || this.mFivegLBStreamMediaWhiteList.isEmpty()) {
            return false;
        }
        return this.mFivegLBStreamMediaWhiteList.contains(str);
    }

    private void registerForegroundInfoListener() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mEventListener);
    }

    private static void sendEvent(MiEvent miEvent) {
        if (TelephonyManagerEx.getDefault().isMievnetSupported()) {
            MiSight.sendEvent(miEvent);
        }
    }

    private boolean isNrPoorForWhiteList(int i) {
        int gameDelayLevel;
        int i2;
        int i3;
        int i4;
        int i5 = -110;
        if (this.mIsTMGPsGameForeground) {
            gameDelayLevel = getGameDelayLevel(this.mLastGameDelay, sTmgpGameDelayThresholds);
            i2 = -110;
        } else {
            gameDelayLevel = -1;
            i2 = -120;
        }
        boolean z = false;
        if (this.mIsVoipForgroud) {
            i3 = -4;
        } else {
            i5 = i2;
            i3 = 0;
        }
        int i6 = isBadNrCellForC6C7() ? -120 : i5;
        int i7 = this.mNrRsrp[i];
        boolean z2 = i7 != Integer.MAX_VALUE && i7 <= i6 && (i4 = this.mNrSnr[i]) != Integer.MAX_VALUE && i4 < i3;
        if ((!this.mCurrentHstState || 1 != operatorForDds()) && ((!this.mIsTMGPsGameForeground || gameDelayLevel >= 3) && (!this.mIsVoipForgroud || this.mVideoQuality == 0))) {
            z = z2;
        }
        log("isNrPoorForWhiteList  rst=" + z + ",nrrsrp= " + this.mNrRsrp[i] + ",nrsnr= " + this.mNrSnr[i] + ",nrRsrpThreshold" + i6 + ",nrSnrThreshold" + i3);
        return z;
    }

    private boolean isLtePoorThanNr(Scene scene, int i) {
        int i2;
        if (this.VDBG) {
            log("isLtePoorThanNr  nrrsrp= " + this.mNrRsrp[i] + ",nrsnr= " + this.mNrSnr[i] + ",lteRsrp= " + this.mLteRsrp[i]);
        }
        int i3 = this.mNrRsrp[i];
        if (i3 == Integer.MAX_VALUE || (i2 = this.mLteRsrp[i]) == Integer.MAX_VALUE || i2 > -110 || i3 > -110) {
            return false;
        }
        boolean z = this.mCurrentHstState;
        if (!z || i3 < i2) {
            return !z && i3 >= i2 + 3;
        }
        return true;
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

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        try {
            log("processServiceStateChangeEvent phoneId=" + i);
            if (serviceState != null && MiuiPhoneUtils.isValidatePhoneId(i)) {
                if (!serviceState.getIsManualSelection() && isDefaultDataSlotId(i)) {
                    monitorDataNwChange(i, serviceState);
                    updateDdsOosState(i, serviceState);
                } else if (!serviceState.getIsManualSelection() && i == getAnotherPhoneId(this.mDdsPhoneId)) {
                    monitorDataNwChangeforNdds(i, serviceState);
                }
                this.mServiceState[i] = new ServiceState(serviceState);
            } else {
                log("processServiceStateChangeEvent invalid params!!!");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processUidChangeEvent(int i, ArrayList arrayList) {
        if (arrayList == null) {
            return;
        }
        if (i > 0) {
            log("processUidChangeEvent : Not supported under Split Screen");
        } else {
            try {
                ComponentName componentName = (ComponentName) arrayList.get(0);
                if (componentName == null) {
                    log("processUidChangeEvent : component is: null");
                    return;
                }
                log("processUidChangeEvent :activity is:" + componentName);
                AppType foregroundAppTypeFromPkg = getForegroundAppTypeFromPkg(componentName.getPackageName());
                log("processUidChangeEvent :mCurApptype is " + this.mCurApptype + " tempAppType is " + foregroundAppTypeFromPkg);
                if (foregroundAppTypeFromPkg != this.mCurApptype) {
                    this.mCurApptype = foregroundAppTypeFromPkg;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public AppType getCurApptype() {
        return this.mCurApptype;
    }

    private void monitorDataNwChangeforNdds(int i, ServiceState serviceState) {
        if (this.mServiceState[i] == null || serviceState == null) {
            log("monitorDataNwChangeforNdds invalid params!");
            return;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        int dataNetworkType2 = this.mServiceState[i].getDataNetworkType();
        log("newRat:" + dataNetworkType + ",oldRat:" + dataNetworkType2 + ",mIsFeatureEnable:" + this.mIsFeatureEnable);
        boolean z = isNrNwType(dataNetworkType) && !isNrNwType(dataNetworkType2);
        this.mServiceState[i] = new ServiceState(serviceState);
        if (z) {
            stopForceNrMonitorTimer();
            return;
        }
        if (dataNetworkType2 != dataNetworkType && isLteNwType(dataNetworkType) && isNecessaryCase(i) && !isSmartDualSimActive() && needLteToNrforNdds()) {
            startForceNrMonitorTimer(OfflineUtils.Offline.SAMPLE_RATE_VALUE_8KHZ);
        }
    }

    private void startForceNrMonitorTimer(int i) {
        if ((this.mForceNrCount == 0 || !this.mHandler.hasMessages(30)) && !this.mHandler.hasMessages(29)) {
            log("startForceNrMonitorTimer delayInMs:" + i);
            stopForceNrMonitorTimer();
            MetricsHandler metricsHandler = this.mHandler;
            metricsHandler.sendMessageDelayed(metricsHandler.obtainMessage(29), i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopForceNrMonitorTimer() {
        this.mForceNrCount = 0;
        this.mHandler.removeMessages(29);
        this.mHandler.removeMessages(30);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doRecoveryForFastNrNdds(int i) {
        ServiceState serviceState;
        int anotherPhoneId = getAnotherPhoneId(this.mDdsPhoneId);
        if (!MiuiPhoneUtils.isValidatePhoneId(anotherPhoneId) || (serviceState = this.mServiceState[anotherPhoneId]) == null || serviceState.getIsManualSelection() || !needLteToNrforNdds()) {
            log("doRecoveryForFastNrNdds invalid params or manual network selection, forceL2Nr is not executed!");
            return;
        }
        trimElapsedRealtime(this.mElapsedRealtimeFastNr, 1800);
        if (this.mElapsedRealtimeFastNr.size() > 2) {
            log("Already forceNr more than 2 times in last 30 mins.");
            this.mPenaltyForceNrTime = System.currentTimeMillis() + 1800000;
            return;
        }
        long j = this.mPenaltyForceNrTime;
        if (j != 0 && j - System.currentTimeMillis() > 0) {
            log("exist penalty time and do nothing!");
            return;
        }
        this.mPenaltyForceNrTime = 0L;
        this.mElapsedRealtimeFastNr.add(Long.valueOf(SystemClock.elapsedRealtime()));
        localLog("F_N", "count =" + this.mForceNrCount);
        doRecoveryIfNeed(i, anotherPhoneId, false, Scene.NRDEREGISTEREDNDDS);
        log("doRecoveryForFastNrNdds cause=" + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needLteToNrforNdds() {
        ServiceState serviceState;
        if (MiuiPhoneUtils.isDuringCall() || ((!MiuiPhoneUtils.isSnsOptimizationSupported() && !ModemProvider.IS_XRING) || !MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || !MiuiPhoneUtils.isScreenOn(this.mContext) || isAirPlaneModeOn())) {
            log("needLteToNrforNdds, ignore");
            return false;
        }
        int anotherPhoneId = getAnotherPhoneId(this.mDdsPhoneId);
        if (MiuiPhoneUtils.getInsertedSimCount() != 2 || !MiuiPhoneUtils.isValidatePhoneId(anotherPhoneId) || !MiuiPhoneUtils.isSimReady(anotherPhoneId) || this.mRecovery.isSaOffInRecoveryAction(anotherPhoneId) || isSameServicePrviderforDualSim() || (serviceState = this.mServiceState[anotherPhoneId]) == null) {
            log("anotherPhoneId invalid, ignore");
            return false;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        boolean zIsNrNwMode = isNrNwMode(anotherPhoneId);
        boolean zIsLteNwType = isLteNwType(dataNetworkType);
        boolean zIsDataRegistered = MiuiPhoneUtils.isDataRegistered(anotherPhoneId);
        log("isNrNwMode:" + zIsNrNwMode + " isLteType: " + zIsLteNwType + " isDataRegistered: " + zIsDataRegistered);
        if (!zIsNrNwMode || !zIsLteNwType || !zIsDataRegistered) {
            return false;
        }
        log("needReturnToNr");
        return true;
    }

    private static boolean isSameServicePrviderforDualSim() {
        if (MiuiPhoneUtils.getInsertedSimCount() == 2) {
            Phone phone = MiuiPhoneUtils.getPhone(0);
            Phone phone2 = MiuiPhoneUtils.getPhone(1);
            if (phone != null && phone2 != null) {
                String operatorNumeric = phone.getOperatorNumeric();
                String operatorNumeric2 = phone2.getOperatorNumeric();
                log("isSameServicePrvider:" + operatorNumeric + "," + operatorNumeric2);
                if (!TextUtils.isEmpty(operatorNumeric) && !TextUtils.isEmpty(operatorNumeric2)) {
                    return ServiceProviderUtils.isSameServiceProvider(operatorNumeric, operatorNumeric2);
                }
            }
        }
        return false;
    }

    private boolean isNrNwMode(int i) {
        int preferredNwModeFromDb = getPreferredNwModeFromDb(i);
        boolean z = (preferredNwModeFromDb == 0 || preferredNwModeFromDb == 1 || preferredNwModeFromDb == 2 || preferredNwModeFromDb == 3 || preferredNwModeFromDb == 5 || preferredNwModeFromDb == 6 || preferredNwModeFromDb == 7 || preferredNwModeFromDb == 13 || preferredNwModeFromDb == 14 || preferredNwModeFromDb == 16 || preferredNwModeFromDb == 18 || preferredNwModeFromDb == 21 || preferredNwModeFromDb == 13 || preferredNwModeFromDb == 19) ? false : true;
        if (!this.VDBG) {
            return z;
        }
        log("ignore rst while VDBG is true");
        return true;
    }

    private void updateDdsOosState(int i, ServiceState serviceState) {
        if (this.mServiceState[i] == null || serviceState == null) {
            return;
        }
        int dataRegistrationState = serviceState.getDataRegistrationState();
        if (dataRegistrationState == 0) {
            if (this.mHandler.hasMessages(54)) {
                this.mHandler.removeMessages(54);
                return;
            }
            return;
        }
        int dataNetworkType = this.mServiceState[i].getDataNetworkType();
        if (this.mServiceState[i].getDataRegistrationState() == 0 && isLteNrNwType(dataNetworkType) && dataRegistrationState == 1 && isNormalOosScene(i)) {
            log("oos from LTE/NR, nw redirection: " + this.mDepriorityNr2Lte);
            if (isAllowedToRestoreNr()) {
                this.mHandler.removeMessages(54);
                MetricsHandler metricsHandler = this.mHandler;
                metricsHandler.sendMessageDelayed(metricsHandler.obtainMessage(54, Integer.valueOf(i)), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processOosState(int i) {
        ServiceState serviceState;
        if (MiuiPhoneUtils.isValidatePhoneId(i) && (serviceState = this.mServiceState[i]) != null && serviceState.getDataRegistrationState() == 1 && isAllowedToRestoreNr()) {
            restoreNrIfIndeed(Scene.OOS);
        }
    }

    private void monitorDataNwChange(int i, ServiceState serviceState) {
        if (this.mServiceState[i] == null || serviceState == null) {
            log("monitorDataNwChange invalid params!");
            return;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        int dataNetworkType2 = this.mServiceState[i].getDataNetworkType();
        log("newRat:" + dataNetworkType + ",oldRat:" + dataNetworkType2 + ",mIsFeatureEnable:" + this.mIsFeatureEnable + ",isDds:" + isDefaultDataSlotId(i) + ",mCurrentHstState:" + this.mCurrentHstState);
        boolean z = true;
        boolean z2 = isLteNrNwType(dataNetworkType) && !isLteNrNwType(dataNetworkType2);
        boolean z3 = !isLteNrNwType(dataNetworkType) && isLteNrNwType(dataNetworkType2);
        boolean z4 = isLteNwType(dataNetworkType) && isNrNwType(dataNetworkType2);
        if (!isLteNwType(dataNetworkType2) || !isNrNwType(dataNetworkType)) {
            z = false;
        }
        if (z2) {
            stopForceLteMonitorTimer();
        } else if (z3 && isNecessaryCase(i) && !isSmartDualSimActive()) {
            startForceLteMonitorTimer((this.mCurrentHstState || this.mGlobalHstState) ? 3000 : OfflineUtils.Offline.SAMPLE_RATE_VALUE_8KHZ);
            tryRestoreSaFor2G3G(serviceState);
        } else if (z4 && isNr2LteRedirection(i)) {
            depriorityNr2Lte();
        }
        if (this.mDepriorityNr2Lte && isNrNwType(dataNetworkType)) {
            recordRegNrAfterDeprioritize(i, dataNetworkType != dataNetworkType2);
        }
        TelephonySensorManager telephonySensorManager = TelephonySensorManager.getInstance();
        if (telephonySensorManager != null && !telephonySensorManager.isInElevator()) {
            avoidLteNrPingPong(dataNetworkType, dataNetworkType2, z, z4, i);
        } else {
            resetLteNrPingPongInfo();
        }
    }

    private void tryRestoreSaFor2G3G(ServiceState serviceState) {
        if (serviceState == null) {
            return;
        }
        int dataRegistrationState = serviceState.getDataRegistrationState();
        if (isAllowedToRestoreNr() && dataRegistrationState == 0) {
            restoreNrIfIndeed(Scene.LTENRDEREGISTERED);
        }
    }

    private void avoidLteNrPingPong(int i, int i2, boolean z, boolean z2, int i3) {
        long j = this.mLte2NrTime;
        if (z) {
            if (j == 0 && !this.mHandler.hasMessages(55)) {
                MetricsHandler metricsHandler = this.mHandler;
                metricsHandler.sendMessageDelayed(metricsHandler.obtainMessage(55), 1200000L);
            }
            this.mLte2NrTime = System.currentTimeMillis();
            return;
        }
        if (j != 0 && z2) {
            this.mNr2LteTime = System.currentTimeMillis();
            doRecoveryForLteNrPingPong(i3);
        } else if (i != i2) {
            resetLteNrPingPongInfo();
        }
    }

    private void doRecoveryForLteNrPingPong(int i) {
        if (isLteNrPingPongException(i) || this.mPingPongNightTest) {
            if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.isTputAppInForground) {
                log("dds= " + this.mDdsPhoneId + ",tputApp=" + this.isTputAppInForground);
                return;
            }
            if (isNecessaryCase(this.mDdsPhoneId) && !this.mRecovery.isSaOffInRecoveryAction(this.mDdsPhoneId) && isSignalSuitAvoidPingPong(this.mDdsPhoneId)) {
                if (isLteNrPingPongWhiteList()) {
                    StringBuilder sb = new StringBuilder("s[");
                    Scene scene = Scene.PING_PONG_COMMON;
                    sb.append(scene.getValue());
                    sb.append("]");
                    localLog("DSA", sb.toString());
                    doRecoveryIfNeed(53, this.mDdsPhoneId, false, scene);
                    dfsDeprioritizeOrRestoreSa(1, scene);
                } else {
                    resetLteNrPingPongInfo();
                    return;
                }
            } else if (isNightSuitAvoidPingPong() && !this.mRecovery.isSaOffInRecoveryAction(this.mDdsPhoneId)) {
                StringBuilder sb2 = new StringBuilder("s[");
                Scene scene2 = Scene.PING_PONG_NIGHT;
                sb2.append(scene2.getValue());
                sb2.append("]");
                localLog("DSA", sb2.toString());
                synchronized (this.mWakeLock) {
                    acquireWakeLock();
                }
                doRecoveryIfNeed(53, this.mDdsPhoneId, true, scene2);
                dfsDeprioritizeOrRestoreSa(1, scene2);
                int anotherPhoneId = getAnotherPhoneId(this.mDdsPhoneId);
                if (MiuiPhoneUtils.getInsertedSimCount() == 2 && MiuiPhoneUtils.isValidatePhoneId(anotherPhoneId) && MiuiPhoneUtils.isSimReady(anotherPhoneId) && !this.mRecovery.isSaOffInRecoveryAction(anotherPhoneId)) {
                    processPingPongForNonDds(53, anotherPhoneId, scene2);
                }
                synchronized (this.mWakeLock) {
                    releaseWakeLock();
                }
            }
            resetLteNrPingPongInfo();
        }
    }

    private void acquireWakeLock() {
        PowerManager.WakeLock wakeLock = this.mWakeLock;
        if (wakeLock == null || wakeLock.isHeld()) {
            return;
        }
        log("acquire WakeLock.");
        this.mWakeLock.acquire();
    }

    private void releaseWakeLock() {
        PowerManager.WakeLock wakeLock = this.mWakeLock;
        if (wakeLock == null || !wakeLock.isHeld()) {
            return;
        }
        log("release WakeLock.");
        this.mWakeLock.release();
    }

    private void processPingPongForNonDds(final int i, final int i2, final Scene scene) {
        if (MiuiPhoneUtils.getInsertedSimCount() == 2) {
            new Thread(new Runnable() { // from class: com.android.phone.NetworkMetricsController.4
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    NetworkMetricsController.this.doRecoveryIfNeed(i, i2, true, scene);
                }
            }).start();
        }
    }

    private boolean isNightSuitAvoidPingPong() {
        Calendar calendar = Calendar.getInstance();
        if (calendar != null && MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            int i = calendar.get(11);
            boolean zIsNightMode = isNightMode();
            if ((i >= 23 && i < 24) || (i >= 0 && i < 7)) {
                if (this.mIsFeatureEnable && !this.mIsAirPlaneMode && MiuiPhoneUtils.isSimReady(this.mDdsPhoneId) && !MiuiPhoneUtils.isDuringCall() && isLteNrNwMode(this.mDdsPhoneId) && !this.mIsScreenOn && !zIsNightMode) {
                    return true;
                }
                log("isNightSuitAvoidPingPong ignore," + i + "," + this.mIsScreenOn + "," + zIsNightMode);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getAnotherPhoneId(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            return i == 0 ? 1 : 0;
        }
        return -1;
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0023  */
    private boolean isLteNrPingPongException(int i) {
        long j = this.mNr2LteTime;
        if (j != 0) {
            long j2 = this.mLte2NrTime;
            if (j2 != 0 && j - j2 < 240000) {
                this.mNrDurationExceptionCount++;
                this.mNrDurationTotalCount++;
            } else if (j != 0 && this.mLte2NrTime != 0) {
                this.mNrDurationTotalCount++;
            }
        } else if (j != 0) {
            this.mNrDurationTotalCount++;
        }
        boolean z = this.mNrDurationTotalCount >= 2 && this.mNrDurationExceptionCount >= 1;
        log("isLteNrPingPongException rst[" + z + "],mNrDurationExceptionCount[" + this.mNrDurationExceptionCount + "],mNrDurationTotalCount[" + this.mNrDurationTotalCount + "]");
        if (z) {
            recordPingPongException(this.mGame, MiuiPhoneUtils.isDuringCall(), i);
            reportPingPongException(i);
        }
        return z;
    }

    private void reportPingPongException(int i) {
        log("reportPingPongException slotId:" + i);
        Intent intent = new Intent("com.android.phone.intent.action.PINGPONG_EXCEPTION");
        intent.putExtra("phoneId", i);
        this.mContext.sendBroadcast(intent);
    }

    private boolean isSignalSuitAvoidPingPong(int i) {
        int i2;
        int i3;
        log("isSignalSuitAvoidPingPong nrrsrp= " + this.mNrRsrp[i] + ",nrsnr= " + this.mNrSnr[i] + ",lteRsrp= " + this.mLteRsrp[i]);
        int i4 = this.mNrRsrp[i];
        if (i4 == Integer.MAX_VALUE || (i2 = this.mNrSnr[i]) == Integer.MAX_VALUE || (i3 = this.mLteRsrp[i]) == Integer.MAX_VALUE) {
            return false;
        }
        if (i4 < -110 || i2 < 0) {
            return i3 > -110 || i3 + 3 > i4;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetLteNrPingPongInfo() {
        this.mLte2NrTime = 0L;
        this.mNr2LteTime = 0L;
        this.mNrDurationExceptionCount = 0;
        this.mNrDurationTotalCount = 0;
    }

    private void doRecoveryInHst(int i) {
        Scene scene = Scene.HIGH_SPEED_TRAIN;
        if (this.mRecovery.isNsaOffInRecoveryAction(i)) {
            localLog("ENSA", "s[" + scene.getValue() + "]");
            doRecoveryIfNeed(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, i, false, scene);
            return;
        }
        if (this.mRecovery.isSaOffInRecoveryAction(i)) {
            localLog("ESA", "s[" + scene.getValue() + "]");
            doRecoveryIfNeed(55, i, false, scene);
            dfsDeprioritizeOrRestoreSa(2, scene);
            return;
        }
        this.mRecovery.doRecoveryInHst(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processScreenStateChanged() {
        if (this.mIsScreenOn) {
            releaseWakeLock();
            if (!isNrRegisteredforNdds() && isNecessaryCase(getAnotherPhoneId(this.mDdsPhoneId)) && !isSmartDualSimActive() && needLteToNrforNdds()) {
                startForceNrMonitorTimer(OfflineUtils.Offline.SAMPLE_RATE_VALUE_8KHZ);
            }
            if (!isLteNrRegistered() && isNecessaryCase(this.mDdsPhoneId) && !isSmartDualSimActive()) {
                startForceLteMonitorTimer(5000);
            }
            if (this.mLastSceneType == Scene.PING_PONG_NIGHT) {
                if (this.mRecovery.isSaOffInRecoveryAction(this.mDdsPhoneId) || this.mRecovery.isSaOffInRecoveryAction(getAnotherPhoneId(this.mDdsPhoneId))) {
                    StringBuilder sb = new StringBuilder("s[");
                    Scene scene = Scene.SCREEN_ON_PING_PONG_NIGHT;
                    sb.append(scene.getValue());
                    sb.append("]");
                    localLog("ESA", sb.toString());
                    doRecoveryIfNeed(55, this.mDdsPhoneId, true, scene);
                    dfsDeprioritizeOrRestoreSa(2, scene);
                    return;
                }
                return;
            }
            return;
        }
        if (this.mIsBuildingMode && this.mDepriorityNr2Lte) {
            this.mDepriorityNr2Lte = false;
            restoreNrIfIndeed(Scene.BUILDING_MODE);
        }
        if (!isDdsRoaming() || is3GPlusRegistered() || !isNecessaryCaseForRoaming() || isSmartDualSimActive() || this.mHandler.hasMessages(31)) {
            return;
        }
        this.mHandler.obtainMessage(31).sendToTarget();
        recordRoamingForceLte();
    }

    private void recordRoamingForceLte() {
        String operatorNumeric;
        HashMap map = new HashMap();
        map.put("Network", MiuiPhoneUtils.getOperatorNumeric(MiuiPhoneUtils.getPhone(this.mDdsPhoneId)));
        if (this.mServiceState[this.mDdsPhoneId] != null && this.mServiceState[this.mDdsPhoneId].getOperatorNumeric() != null) {
            operatorNumeric = this.mServiceState[this.mDdsPhoneId].getOperatorNumeric();
        } else {
            operatorNumeric = "";
        }
        map.put("Network_roaming", operatorNumeric);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_roaming_force_lte", map);
    }

    private boolean isNecessaryCaseForRoaming() {
        boolean z = !Build.IS_INTERNATIONAL_BUILD && this.mIsFeatureEnable && !this.mIsAirPlaneMode && MiuiPhoneUtils.isSimReady(this.mDdsPhoneId) && !MiuiPhoneUtils.isDuringCall() && isLteNrNwMode(this.mDdsPhoneId);
        if (this.VDBG) {
            log("isNecessaryCaseForRoaming rst=" + z);
        }
        return z;
    }

    private boolean is3GPlusRegistered() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null) {
            log("is3GPlusRegistered invalid params!");
            return false;
        }
        boolean zIsDataRegistered = MiuiPhoneUtils.isDataRegistered(this.mDdsPhoneId);
        long bitMaskForNetworkType = TelephonyManager.getBitMaskForNetworkType(this.mServiceState[this.mDdsPhoneId].getDataNetworkType());
        if (zIsDataRegistered) {
            return ((93108 & bitMaskForNetworkType) == 0 && (397312 & bitMaskForNetworkType) == 0 && (bitMaskForNetworkType & 524288) == 0) ? false : true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLteNrRegistered() {
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null) {
            return MiuiPhoneUtils.isDataRegistered(this.mDdsPhoneId) && isLteNrNwType(this.mServiceState[this.mDdsPhoneId].getDataNetworkType());
        }
        log("isLteNrRegistered invalid params!");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNrRegisteredforNdds() {
        int anotherPhoneId = getAnotherPhoneId(this.mDdsPhoneId);
        if (MiuiPhoneUtils.getInsertedSimCount() == 2 && MiuiPhoneUtils.isValidatePhoneId(anotherPhoneId) && this.mServiceState[anotherPhoneId] != null) {
            return MiuiPhoneUtils.isDataRegistered(anotherPhoneId) && isNrNwType(this.mServiceState[anotherPhoneId].getDataNetworkType());
        }
        log("isNrRegistered invalid params!");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNecessaryCase(int i) {
        boolean z = false;
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            return false;
        }
        if (this.mIsFeatureEnable && this.mIsScreenOn && !this.mIsAirPlaneMode && MiuiPhoneUtils.isSimReady(i) && !MiuiPhoneUtils.isDuringCall() && isLteNrNwMode(i)) {
            z = true;
        }
        if (this.VDBG) {
            log("isNecessaryCase rst[" + z + "],phoneId[" + i + "]");
        }
        return z;
    }

    public boolean isNecessaryCaseForSmartNetwork() {
        boolean z = false;
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            return false;
        }
        if (this.mIsScreenOn && !this.mIsAirPlaneMode && MiuiPhoneUtils.isSimReady(this.mDdsPhoneId) && !MiuiPhoneUtils.isDuringCall() && isLteNrNwMode(this.mDdsPhoneId)) {
            z = true;
        }
        if (this.VDBG) {
            log("isNecessaryCaseForSmartNetwork rst[" + z + "],mDdsPhoneId[" + this.mDdsPhoneId + "]");
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSmartDualSimActive() {
        return this.mIsSmartDualSimFeatureEnable && !MiuiPhoneUtils.shouldHideSmartDualSimButton();
    }

    public boolean isLteNrNwMode(int i) {
        int preferredNwModeFromDb = getPreferredNwModeFromDb(i);
        boolean z = (preferredNwModeFromDb == 0 || preferredNwModeFromDb == 1 || preferredNwModeFromDb == 2 || preferredNwModeFromDb == 3 || preferredNwModeFromDb == 5 || preferredNwModeFromDb == 6 || preferredNwModeFromDb == 7 || preferredNwModeFromDb == 13 || preferredNwModeFromDb == 14 || preferredNwModeFromDb == 16 || preferredNwModeFromDb == 18 || preferredNwModeFromDb == 21) ? false : true;
        if (!this.VDBG) {
            return z;
        }
        log("ignore rst while VDBG is true");
        return true;
    }

    public boolean isLteNrNwType(int i) {
        return isLteNwType(i) || isNrNwType(i);
    }

    private int getDefaultNetworkNetId() {
        Network activeNetwork = this.mCm.getActiveNetwork();
        if (activeNetwork != null) {
            return activeNetwork.getNetId();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLinkReliability(int i, int i2) {
        if (i < 100 || i != this.mNetId || i2 == 0 || this.mGame) {
            log("ignore recovery netId[" + i + "],mNetId[" + this.mNetId + "],mGame[" + this.mGame + "]");
            return;
        }
        if ((i2 & 33) != 0) {
            log("ignore recovery cause[" + i2 + "]");
            return;
        }
        if (isSMapBadCell()) {
            distributeDubiousCellBySMap();
            recordCount("triggerDubiousCell");
        } else if (i2 <= 1023) {
            doRecoveryByStrategy(i2);
        }
    }

    private int getDataRat(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getRilDataRadioTechnology();
    }

    private int getDbm(SignalStrength signalStrength) {
        if (signalStrength != null) {
            return signalStrength.getDbm();
        }
        return Integer.MAX_VALUE;
    }

    private int getSnr(int i) {
        FiveGServiceInfo fiveGServiceInfo;
        SignalStrength signalStrength;
        int voiceNetworkType = this.mServiceState[i].getVoiceNetworkType();
        if (Utils.isLte(voiceNetworkType) && (signalStrength = this.mSignalStrength[i]) != null) {
            return signalStrength.getLteRssnr();
        }
        if (!Utils.isNr(voiceNetworkType) || (fiveGServiceInfo = this.mFiveGInfo[i]) == null) {
            return -1;
        }
        return fiveGServiceInfo.getSnr();
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0018  */
    private int getPciByCellIdentity(CellIdentity cellIdentity) {
        int pci;
        if (cellIdentity == null) {
            pci = 0;
        } else if (cellIdentity instanceof CellIdentityLte) {
            pci = ((CellIdentityLte) cellIdentity).getPci();
        } else if (cellIdentity instanceof CellIdentityNr) {
            pci = ((CellIdentityNr) cellIdentity).getPci();
        } else {
            pci = 0;
        }
        if (this.VDBG) {
            log("getCidByCellIdentity: pci" + pci);
        }
        return pci;
    }

    public int getPci() {
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            return getPciByCellIdentity(this.mCellIdentity[this.mDdsPhoneId]);
        }
        return 0;
    }

    public boolean getDdsMobileDataOn() {
        return this.mIsMobileDataOn;
    }

    public boolean isInCall() {
        return this.mIsInCall;
    }

    public boolean isNightMode() {
        return this.mIsNightMode;
    }

    private boolean isSMapBadCell() {
        HashMap snsBlackCellMap = SignalMapCloudController.getInstance().getSnsBlackCellMap();
        if (snsBlackCellMap == null || snsBlackCellMap.isEmpty()) {
            return false;
        }
        String ddsLteNrCid = getDdsLteNrCid();
        if (TextUtils.isEmpty(ddsLteNrCid)) {
            log("cid is empty ");
            return false;
        }
        return snsBlackCellMap.containsKey(ddsLteNrCid);
    }

    private void distributeDubiousCellBySMap() {
        String str = (String) SignalMapCloudController.getInstance().getSnsBlackCellMap().get(getDdsLteNrCid());
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        log("distributeDubiousCellBySMap cellInfo " + str);
        SignalMapSnsControllerBase.getInstance().setLteNrDubiousCellIfNeed(arrayList, "signalMap");
    }

    public String getDdsLteNrCid() {
        Phone phone;
        long nci;
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || (phone = MiuiPhoneUtils.getPhone(this.mDdsPhoneId)) == null || phone.getCurrentCellIdentity() == null) {
            return null;
        }
        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
        if (currentCellIdentity instanceof CellIdentityLte) {
            nci = ((CellIdentityLte) currentCellIdentity).getCi();
        } else if (currentCellIdentity instanceof CellIdentityNr) {
            nci = ((CellIdentityNr) currentCellIdentity).getNci();
        } else {
            return "2G3G";
        }
        return String.valueOf(nci);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDataNetworkFailed(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null || this.isTputAppInForground) {
            log("processDataNetworkFailed dds= " + this.mDdsPhoneId + "and Tput is " + this.isTputAppInForground);
            return;
        }
        if (i == 3000007) {
            doRecoveryByStrategy(i);
        } else if (i == 3000008) {
            restoreNrIfIndeed(Scene.DATA_NETWORK_FAILED);
        }
    }

    private boolean isDefaultDataSlotId(int i) {
        return i == this.mDdsPhoneId;
    }

    private int getPreferredNwModeFromDb(int i) {
        return ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
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
                NetworkMetricsController.log("FiveGInfoListener info is null or invalid phone id");
                return;
            }
            NetworkMetricsController.log("info[" + i + "]= " + fiveGServiceInfo);
            NetworkMetricsController.this.mHandler.obtainMessage(6, i, 0, fiveGServiceInfo).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateFiveGInfo(int i, FiveGServiceInfo fiveGServiceInfo) {
        if (this.mSupportFiveG && MiuiPhoneUtils.isValidatePhoneId(i) && i == this.mDdsPhoneId) {
            if (fiveGServiceInfo != null) {
                this.mFiveGInfo[i].copyFrom(fiveGServiceInfo);
            }
        }
    }

    private synchronized void doRecoveryByStrategy(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && !this.isTputAppInForground) {
            int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
            Scene sceneMapCauseToScene = mapCauseToScene(i);
            log("doRecoveryByStrategy dataType=" + dataNetworkType + ",cause=" + i + ",mCurrentHstState=" + this.mCurrentHstState);
            if (!this.mCurrentHstState && shouldHandleDataStall(sceneMapCauseToScene)) {
                handleDataStallScenario(sceneMapCauseToScene);
                return;
            }
            if (this.mCurrentHstState && (sceneMapCauseToScene == Scene.LINK_QOS || sceneMapCauseToScene == Scene.VOIP)) {
                return;
            }
            boolean z = 1 != getOperatorScoreByHST() || this.mIsUnsolDrbReleaseByRrc;
            this.mIsUnsolDrbReleaseByRrc = false;
            if (isNrNwType(dataNetworkType) && z) {
                localLog("DSA", "s[" + sceneMapCauseToScene.getValue() + "]");
                doRecoveryIfNeed(53, this.mDdsPhoneId, false, sceneMapCauseToScene);
                dfsDeprioritizeOrRestoreSa(1, sceneMapCauseToScene);
            } else if (isLteNwType(dataNetworkType) && this.mFiveGInfo[this.mDdsPhoneId].isConnectedOnNsaMode() && getCurrentEndcStatus() == 1 && z && i != 3000006) {
                localLog("DNSA", "s[" + sceneMapCauseToScene.getValue() + "]");
                doRecoveryIfNeed(103, this.mDdsPhoneId, false, sceneMapCauseToScene);
            } else if (isLteNwType(dataNetworkType)) {
                if (isAllowedToRestoreNsa(i)) {
                    localLog("ENSA", "s[" + sceneMapCauseToScene.getValue() + "]");
                    doRecoveryIfNeed(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, this.mDdsPhoneId, false, sceneMapCauseToScene);
                } else if (isAllowedToRestoreSa(i, sceneMapCauseToScene)) {
                    localLog("ESA", "s[" + sceneMapCauseToScene.getValue() + "]");
                    doRecoveryIfNeed(55, this.mDdsPhoneId, false, sceneMapCauseToScene);
                    dfsDeprioritizeOrRestoreSa(2, sceneMapCauseToScene);
                } else if (isAllowedToReattachNetwork(i)) {
                    doRecoveryIfNeed(3, this.mDdsPhoneId, false, sceneMapCauseToScene);
                    sendEvent(MiSight.constructEvent(915100001, new Object[]{"reattach", Integer.valueOf(i)}));
                    sendEvent(MiSight.constructEvent(815100001, new Object[]{"RecoveryResult", "false", "ProblemCode", 915100001}));
                }
            } else {
                log("doRecoveryByStrategy no action!");
                return;
            }
            return;
        }
        log("ignore doRecoveryByStrategy while id is " + this.mDdsPhoneId + "and Tput is " + this.isTputAppInForground);
    }

    private boolean shouldHandleDataStall(Scene scene) {
        if (this.mCurrentHstState || isDataStallCombineSaOptimization(scene) || isSubwayAbnormalCellMatch()) {
            return false;
        }
        return scene == Scene.LINK_QOS || scene == Scene.VOIP || scene == Scene.LIVE || scene == Scene.MODEM_AI_PECEPTION;
    }

    private void handleDataStallScenario(Scene scene) {
        int i = isSubwayState() ? 180000 : 360000;
        if (this.mTriggerDataStallTime != 0 && System.currentTimeMillis() - this.mTriggerDataStallTime < i) {
            log("handleDataStallScenario DATA_STALL Linger Time");
            return;
        }
        log("handleDataStallScenario DATA_LINK_DATA_STALL");
        if (isLteNrRegistered() && isNecessaryCase(this.mDdsPhoneId) && MiuiPhoneUtils.isSnsOptimizationSupported()) {
            Scene scene2 = Scene.VOIP;
            if (scene == scene2) {
                if (!isSatifySignalForVoipStall() && !isSubwayState()) {
                    return;
                }
                NetworkSpeedTracker networkSpeedTracker = this.mSpeedTracker;
                setDataStallOptimization(networkSpeedTracker != null ? networkSpeedTracker.getVoipDirection() : 1, false, scene);
            } else {
                setDataStallOptimization(1, false, scene);
            }
            localLog("SNS_DS", "s[" + scene.getValue() + "]");
            this.mTriggerDataStallTime = System.currentTimeMillis();
            if (scene == Scene.SPEED_RATE || scene == Scene.LINK_QOS) {
                MiuiQmsStats.getInstance().linkReliabilityChange();
            }
            if (scene == scene2) {
                MiuiQmsStats.getInstance().dataLinkStall();
            }
        }
    }

    private Scene mapCauseToScene(int i) {
        switch (i) {
            case 3000001:
            case 3000005:
                return Scene.VOIP;
            case 3000002:
                return Scene.DATA_STALL_SIGNALQOS;
            case 3000003:
            case 3000008:
            default:
                return Scene.LINK_QOS;
            case 3000004:
                return Scene.LIVE;
            case 3000006:
                return Scene.SPEED_HIGH_ONLY;
            case 3000007:
                return Scene.DATA_NETWORK_FAILED;
            case 3000009:
                return Scene.MODEM_AI_PECEPTION;
        }
    }

    private boolean isAllowedToRestoreNr() {
        return (this.mDepriorityNr2Lte || !this.mIsFeatureEnable || this.mIsThermalScene || this.mIsBuildingMode) ? false : true;
    }

    private boolean isAllowedToReattachNetwork(int i) {
        boolean z = (isNeedToRestoreFiveG() || !SignalUtils.isLteSignalSuitForReattach(this.mDdsPhoneId) || i == 3000001 || i == 3000005 || i == 3000004 || i == 3000009 || i == 3000006) ? false : true;
        if (Build.IS_INTERNATIONAL_BUILD) {
            return z && i != 3000007;
        }
        return z;
    }

    private boolean isAllowedToRestoreNsa(int i) {
        if (this.mRecovery.isNsaOffInRecoveryAction(this.mDdsPhoneId)) {
            return SignalUtils.isLteSignalSuitForRestoreFiveG(this.mDdsPhoneId) || 16 == i || 528 == i || 3000006 == i || 3000007 == i || 3000008 == i;
        }
        return false;
    }

    public boolean isAllowedToRestoreSa(int i, Scene scene) {
        if (this.mRecovery.isSaOffInRecoveryAction(this.mDdsPhoneId)) {
            return SignalUtils.isLteSignalSuitForRestoreFiveG(this.mDdsPhoneId) || 16 == i || 528 == i || 3000006 == i || 3000007 == i || 3000008 == i || 3000010 == i;
        }
        return false;
    }

    private boolean isNeedToRestoreFiveG() {
        return this.mRecovery.isNsaOffInRecoveryAction(this.mDdsPhoneId) || this.mRecovery.isSaOffInRecoveryAction(this.mDdsPhoneId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doRecoveryForFastLte(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null || this.mServiceState[this.mDdsPhoneId].getIsManualSelection() || isColddownPeriod()) {
            log("doRecoveryForFastLte invalid params or manual network selection or colddown period, forcelte is not executed!");
            return;
        }
        trimElapsedRealtime(this.mElapsedRealtimeFastLte, MiRilHook.HOOK_ID_START_URC_BY_AT);
        int i2 = this.mGlobalHstState ? 3 : 1;
        if (this.mElapsedRealtimeFastLte.size() >= i2) {
            log("Already forceLte more than " + i2 + " times in last 5 mins.");
            this.mPenaltyForceLteTime = System.currentTimeMillis() + 900000;
            return;
        }
        long j = this.mPenaltyForceLteTime;
        if (j != 0 && j - System.currentTimeMillis() > 0) {
            log("exist penalty time and do nothing!");
            return;
        }
        this.mPenaltyForceLteTime = 0L;
        this.mElapsedRealtimeFastLte.add(Long.valueOf(SystemClock.elapsedRealtime()));
        localLog("F_L", "count =" + this.mForceLteCount);
        doRecoveryIfNeed(i, this.mDdsPhoneId, false, this.mGlobalHstState ? Scene.GLOBAL_HST_DEREGISTERED : Scene.LTENRDEREGISTERED);
        log("doRecoveryForFastLte cause=" + i);
    }

    private boolean isColddownPeriod() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mStateChangeTime;
        return j != 0 && jCurrentTimeMillis - j < 10000;
    }

    public boolean isLteNrPingPongWhiteList() {
        if (mLteNrPingPongWhiteList.size() == 0) {
            return false;
        }
        String topAppName = getTopAppName();
        log("topAppName:" + topAppName);
        Iterator it = mLteNrPingPongWhiteList.iterator();
        while (it.hasNext()) {
            if (topAppName.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    public boolean isHyperEngineWhiteList() {
        if (mHyperEngineGameWhiteList.size() == 0) {
            return false;
        }
        String topAppName = getTopAppName();
        log("topAppName:" + topAppName);
        Iterator it = mHyperEngineGameWhiteList.iterator();
        while (it.hasNext()) {
            if (topAppName.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    public String getTopAppName() {
        ActivityManager activityManager = (ActivityManager) this.mContext.getSystemService("activity");
        if (activityManager == null) {
            log("getTopAppName activityManager is null");
            return "";
        }
        try {
            if (activityManager.getRunningTasks(1) != null && activityManager.getRunningTasks(1).get(0) != null) {
                return activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
            }
        } catch (Exception e) {
            log("getTopAppName exception:" + e.toString());
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameStatusForHyperEngine() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            log("processGameStatusForHyperEngine invalid phone id");
            return;
        }
        if (!isHyperEngineGameOptAllowed() || !this.mIsHyperEngineGameCloudOn || !this.mIsHyperEngineSmartHoCloudOn) {
            logHyperEngineGameOptBlocked("weakCongestionOpt");
            if (this.mWeakCongestionOptEnabled) {
                this.mRecovery.enableWeakOpt(this.mDdsPhoneId, false);
                this.mRecovery.enableCongestionOpt(this.mDdsPhoneId, false);
                this.mWeakCongestionOptEnabled = false;
                localLog("weakCongestionOpt", "enable=" + this.mWeakCongestionOptEnabled);
                return;
            }
            return;
        }
        boolean z = this.mIsHyperEngineGame;
        if (z && !this.mWeakCongestionOptEnabled) {
            this.mRecovery.enableWeakOpt(this.mDdsPhoneId, true);
            this.mRecovery.enableCongestionOpt(this.mDdsPhoneId, true);
            this.mWeakCongestionOptEnabled = true;
            startHyperEngineGameMonitor();
            localLog("weakCongestionOpt", "enable=" + this.mWeakCongestionOptEnabled);
            return;
        }
        if (z || !this.mWeakCongestionOptEnabled) {
            return;
        }
        this.mRecovery.enableWeakOpt(this.mDdsPhoneId, false);
        this.mRecovery.enableCongestionOpt(this.mDdsPhoneId, false);
        this.mWeakCongestionOptEnabled = false;
        localLog("weakCongestionOpt", "enable=" + this.mWeakCongestionOptEnabled);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateHsrOpt(boolean z) {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            log("updateHsrOpt invalid phone id");
            return;
        }
        if (isHyperEngineGameOptAllowed() && this.mIsHyperEngineHstCloudOn) {
            if (z && this.mIsHyperEngineGame) {
                if (!this.mHsrOptEnabled) {
                    this.mRecovery.enableHsrOpt(this.mDdsPhoneId, true);
                    this.mHsrOptEnabled = true;
                    startHyperEngineGameMonitor();
                    localLog("hsrOpt", "enable=" + this.mHsrOptEnabled);
                }
            } else if (this.mHsrOptEnabled) {
                this.mRecovery.enableHsrOpt(this.mDdsPhoneId, false);
                this.mHsrOptEnabled = false;
                localLog("hsrOpt", "enable=" + this.mHsrOptEnabled);
            }
            return;
        }
        logHyperEngineGameOptBlocked("hsrOpt");
        if (this.mHsrOptEnabled) {
            this.mRecovery.enableHsrOpt(this.mDdsPhoneId, false);
            this.mHsrOptEnabled = false;
            localLog("hsrOpt", "enable=" + this.mHsrOptEnabled);
        }
    }

    private int getHyperEngineGameOptDbStatusByDb() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "hyper_engine_game_optimization_enabled", -1);
    }

    private boolean isHyperEngineGameOptEnabledByDb() {
        int hyperEngineGameOptDbStatusByDb = getHyperEngineGameOptDbStatusByDb();
        return hyperEngineGameOptDbStatusByDb == -1 || hyperEngineGameOptDbStatusByDb == 1;
    }

    private boolean isHyperEngineGameOptAllowed() {
        return this.mIsHyperEngineGameOptByDb && this.mIsHyperEngineGameOptByTemp;
    }

    private void logHyperEngineGameOptBlocked(String str) {
        String str2;
        if (!this.mIsHyperEngineGameOptByDb) {
            str2 = "DB";
        } else if (!this.mIsHyperEngineGameOptByTemp) {
            str2 = "TEMP";
        } else if (!this.mIsHyperEngineGameCloudOn) {
            str2 = "GAME_CLOUD";
        } else if (!this.mIsHyperEngineSmartHoCloudOn && "weakCongestionOpt".equals(str)) {
            str2 = "SMARTHO_CLOUD";
        } else if (!this.mIsHyperEngineHstCloudOn && "hsrOpt".equals(str)) {
            str2 = "HST_CLOUD";
        } else {
            str2 = "UNKNOWN";
        }
        log("block HyperEngine game opt, scene=" + str + ", reason=" + str2 + ", byDb=" + this.mIsHyperEngineGameOptByDb + ", byTemp=" + this.mIsHyperEngineGameOptByTemp + ", dbStatus=" + this.mHyperEngineGameOptDbStatus + ", gameCloud=" + this.mIsHyperEngineGameCloudOn + ", smartHoCloud=" + this.mIsHyperEngineSmartHoCloudOn + ", hstCloud=" + this.mIsHyperEngineHstCloudOn + ", isGame=" + this.mIsHyperEngineGame + ", weakOpt=" + this.mWeakCongestionOptEnabled + ", hsrOpt=" + this.mHsrOptEnabled);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHyperEngineGameOptStatus() {
        this.mHyperEngineGameOptDbStatus = getHyperEngineGameOptDbStatusByDb();
        boolean zIsHyperEngineGameOptEnabledByDb = isHyperEngineGameOptEnabledByDb();
        if (this.mIsHyperEngineGameOptByDb == zIsHyperEngineGameOptEnabledByDb) {
            return;
        }
        this.mIsHyperEngineGameOptByDb = zIsHyperEngineGameOptEnabledByDb;
        log("mHyperEngineGameOptDbStatus=" + this.mHyperEngineGameOptDbStatus);
        localLogHyperEngineGameSwitch(zIsHyperEngineGameOptEnabledByDb);
        log("mIsHyperEngineGameOptByDb=" + this.mIsHyperEngineGameOptByDb);
        if (zIsHyperEngineGameOptEnabledByDb) {
            this.mIsHyperEngineGameOptByTemp = true;
        } else {
            disableHyperEngineGameOptByDb(this.mHyperEngineGameOptDbStatus, zIsHyperEngineGameOptEnabledByDb);
        }
    }

    private void localLogHyperEngineGameSwitch(boolean z) {
        String string = Settings.Secure.getString(this.mContext.getContentResolver(), "hyper_engine_game_optimization_reason");
        if (TextUtils.isEmpty(string)) {
            string = "REASON_UNKNOWN";
        }
        localLogHyperEngineGameSwitch(z, string);
    }

    private void localLogHyperEngineGameSwitch(boolean z, String str) {
        StringBuilder sb = new StringBuilder("HyperEngine game switch = ");
        sb.append(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF);
        sb.append(", reason: ");
        sb.append(str);
        localLog("HEGS", sb.toString());
        recordHyperEngineGameSwitchStatistic(z, str);
    }

    private void recordHyperEngineGameSwitchStatistic(boolean z, String str) {
        HashMap map = new HashMap();
        map.put("hyper_engine_game_switch", z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF);
        map.put("reason", str);
        map.put("hyper_engine_game_active", Boolean.valueOf(this.mIsHyperEngineGame));
        map.put("hyper_engine_weak_opt", Boolean.valueOf(this.mWeakCongestionOptEnabled));
        map.put("hyper_engine_hsr_opt", Boolean.valueOf(this.mHsrOptEnabled));
        map.put("game_delay", Integer.valueOf(this.mLastGameDelay));
        map.put("DdsPhoneId", String.valueOf(this.mDdsPhoneId));
        map.put("appPackageName", this.mCurrentForegroundPackageName);
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            ServiceState serviceState = this.mServiceState[this.mDdsPhoneId];
            map.put("RAT", ServiceState.rilRadioTechnologyToString(getDataRat(serviceState)));
            map.put("rsrp", String.valueOf(getHyperEngineGameRsrp()));
            if (serviceState != null) {
                map.put("snr", String.valueOf(getSnr(this.mDdsPhoneId)));
                map.put("Band", Utils.getBand(this.mDdsPhoneId, serviceState));
            }
            map.put("CellIdentity", getDdsLteNrCid());
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_hyper_engine_game_switch_statistic", map);
    }

    private void disableHyperEngineGameOptByDb(int i, boolean z) {
        log("disableHyperEngineGameOptByDb dbStatus=" + i + ", enabled=" + z);
        disableHyperEngineGameOptInternal();
    }

    public void disableHyperEngineGameOptByTemp(String str) {
        if (!this.mIsHyperEngineGameOptByTemp) {
            log("ignore duplicated disableHyperEngineGameOptByTemp reason=" + str);
            return;
        }
        if (!this.mIsMobileDataOn) {
            log("ignore disableHyperEngineGameOptByTemp without mobile data, reason=" + str);
        } else if (this.mIsInCall) {
            log("ignore disableHyperEngineGameOptByTemp during call, reason=" + str);
        } else {
            this.mIsHyperEngineGameOptByTemp = false;
            localLogHyperEngineGameSwitch(false, str);
            log("disableHyperEngineGameOptByTemp reason=" + str);
            disableHyperEngineGameOptInternal();
        }
    }

    private void disableHyperEngineGameOptInternal() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            log("disableHyperEngineGameOptInternal invalid phone id");
            return;
        }
        if (this.mWeakCongestionOptEnabled) {
            this.mRecovery.enableWeakOpt(this.mDdsPhoneId, false);
            this.mRecovery.enableCongestionOpt(this.mDdsPhoneId, false);
            this.mWeakCongestionOptEnabled = false;
            localLog("weakCongestionOpt", "enable=" + this.mWeakCongestionOptEnabled);
        }
        if (this.mHsrOptEnabled) {
            this.mRecovery.enableHsrOpt(this.mDdsPhoneId, false);
            this.mHsrOptEnabled = false;
            localLog("hsrOpt", "enable=" + this.mHsrOptEnabled);
        }
        stopHyperEngineGameMonitor();
    }

    private void startHyperEngineGameMonitor() {
        HyperEngineGameController.init(this.mContext).registerNetworkMetricsController(this);
    }

    private void stopHyperEngineGameMonitor() {
        HyperEngineGameController hyperEngineGameController = HyperEngineGameController.getInstance();
        if (hyperEngineGameController != null) {
            hyperEngineGameController.unregisterNetworkMetricsController();
        }
    }

    public boolean isHyperEngineGameOptActive() {
        if (isHyperEngineGameOptAllowed() && this.mIsHyperEngineGame) {
            return this.mWeakCongestionOptEnabled || this.mHsrOptEnabled;
        }
        return false;
    }

    public int getHyperEngineGameRsrp() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            return Integer.MAX_VALUE;
        }
        int i = this.mNrRsrp[this.mDdsPhoneId];
        return i != Integer.MAX_VALUE ? i : this.mLteRsrp[this.mDdsPhoneId];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameStatus(boolean z) {
        unregisterJoyoseInfoCallBack();
        this.mMiGameBoosterCallback = new IMiGameBoosterCallback.Stub() { // from class: com.android.phone.NetworkMetricsController.5
            public Bundle onEventSync(int i, Bundle bundle) {
                return bundle;
            }

            public void onEvent(int i, Bundle bundle) {
                if (bundle == null) {
                    return;
                }
                if (i == 2) {
                    NetworkMetricsController.this.handleNetDelayEvent(i, bundle);
                } else if (i == 4) {
                    NetworkMetricsController.this.handleGameStartEvent(i, bundle);
                } else {
                    if (i != 16) {
                        return;
                    }
                    NetworkMetricsController.this.handleGameUpdateEvent(i, bundle);
                }
            }
        };
        this.mJs = IJoyoseInterface.Stub.asInterface(ServiceManager.getService("xiaomi.joyose"));
        String topAppName = getTopAppName();
        if (z && "com.tencent.tmgp.sgame".equals(topAppName)) {
            this.mIsTMGPsGameForeground = true;
            this.mIsGameStatisticForeground = true;
            registerJoyoseInfoCallBack();
            notifyGameStatus(2, topAppName);
            return;
        }
        if (z && XIAOMI_JOYOSE_REAL_TIME_SUPPORTED_GAMES.contains(topAppName)) {
            this.mIsTMGPsGameForeground = false;
            this.mIsGameStatisticForeground = true;
            registerJoyoseInfoCallBack();
            notifyGameStatus(2, topAppName);
            return;
        }
        if (z && XIAOMI_JOYOSE_FIXED_CYCLE_SUPPORTED_GAMES.contains(topAppName)) {
            this.mIsTMGPsGameForeground = false;
            this.mIsGameStatisticForeground = false;
            registerJoyoseInfoCallBack();
            notifyGameStatus(1, topAppName);
            return;
        }
        this.mIsTMGPsGameForeground = false;
        this.mIsGameStatisticForeground = false;
        unregisterJoyoseInfoCallBack();
        notifyGameStatus(0, topAppName);
    }

    private int getGameDelayLevel(int i, int[] iArr) {
        if (i >= iArr[4]) {
            return 4;
        }
        if (i >= iArr[3]) {
            return 3;
        }
        if (i >= iArr[2]) {
            return 2;
        }
        if (i >= iArr[1]) {
            return 1;
        }
        return i >= iArr[0] ? 0 : -1;
    }

    private int getGameUserScene() {
        if (this.mIsMobileDataOn) {
            return 1;
        }
        if (this.mIsWifiDataOn) {
            return 2;
        }
        if (MiuiPhoneUtils.isDuringCall()) {
            return 4;
        }
        if (this.mIsAirPlaneMode) {
            return 8;
        }
        return isInStaticStatus() ? 16 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processVideoInfoChanged() {
        if (!this.mIsMobileDataOn || !MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.isTputAppInForground || !this.mIsFeatureEnable) {
            log("mIsMobileDataOn" + this.mIsMobileDataOn + ",dds= " + this.mDdsPhoneId + ",tputApp=" + this.isTputAppInForground + ",mIsFeatureEnable=" + this.mIsFeatureEnable);
            return;
        }
        if (this.mVideoQuality == 1 && this.mHandler.hasMessages(61)) {
            this.mHandler.removeMessages(61);
        }
        if (this.mVideoQuality == 0 && isSubwayAbnormalCellMatch()) {
            log("processVideoInfoChanged video stall in Subway AbCell");
            if (this.mCurApptype == AppType.STREAMING_VIDEO && !this.mHandler.hasMessages(61)) {
                MetricsHandler metricsHandler = this.mHandler;
                metricsHandler.sendMessageDelayed(metricsHandler.obtainMessage(61), 2000L);
                return;
            } else {
                processSubwaySignalMapOpt();
                return;
            }
        }
        trimElapsedRealtime(this.mElapsedRealtimeVoipStall, 10);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mIsVoipForgroud || isLiveStreamVideo()) {
            boolean z = false;
            if (this.mVideoQuality == 0) {
                log("video concurrent stall");
                this.mLastVoipHighDelayTime = jElapsedRealtime;
                recordRenderSampleForBasement(0);
                this.mElapsedRealtimeVoipStall.add(Long.valueOf(jElapsedRealtime));
                if (this.mElapsedRealtimeVoipStall.size() >= 3) {
                    if (this.VDBG) {
                        log("More than 3 times  within 10S, also marked as video stall.");
                    }
                    z = true;
                }
                this.mIsHolderVoipStall = true;
                this.mHandler.removeMessages(36);
                MetricsHandler metricsHandler2 = this.mHandler;
                metricsHandler2.sendMessageDelayed(metricsHandler2.obtainMessage(36), MirrorDesktopHelper.TIMEOUT_MILLIS);
            } else {
                recordRenderSampleForBasement(1);
                if (this.mIsHolderVoipStall) {
                    this.mIsHolderVoipStall = false;
                }
                if (this.mHandler.hasMessages(36)) {
                    this.mHandler.removeMessages(36);
                }
            }
            if (z) {
                if (isBasementVoipSceneActive()) {
                    log("processVideoInfoChanged skip legacy VOIP stall path: basement scene active");
                    return;
                }
                localLog("V_S", "size:" + this.mElapsedRealtimeVoipStall.size() + ",isHold:" + this.mIsHolderVoipStall);
                this.mHandler.obtainMessage(35).sendToTarget();
            }
        }
    }

    private boolean isBasementVoipSceneActive() {
        return this.mBasementDsaTime != 0 || isBasementVoipWhiteCellMatch();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBasementOptForeground() {
        return this.mIsVoipForgroud || isLiveStreamVideo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLiveStreamVideo() {
        return this.mIsHuyaForeground;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSubwaySignalMapOpt() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null) {
            return;
        }
        int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (isNrNwType(dataNetworkType) && jCurrentTimeMillis - this.mLastSubwaySignalMapOptTime > 30000) {
            StringBuilder sb = new StringBuilder("s[");
            Scene scene = Scene.SUBWAY_ABNORMAL_CELL;
            sb.append(scene.getValue());
            sb.append("]");
            localLog("DSA", sb.toString());
            doRecoveryIfNeed(53, this.mDdsPhoneId, false, scene);
            return;
        }
        if (isLteNwType(dataNetworkType)) {
            this.mLastSubwaySignalMapOptTime = jCurrentTimeMillis;
            restoreNrIfIndeed(Scene.SUBWAY_ABNORMAL_CELL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:55:0x0123  */
    public void processGameDelay(int i) {
        int gameDelayLevel;
        int gameDelayLevel2;
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.isTputAppInForground || !this.mIsFeatureEnable) {
            log("dds= " + this.mDdsPhoneId + ",tputApp=" + this.isTputAppInForground + ",mIsFeatureEnable=" + this.mIsFeatureEnable);
            return;
        }
        notifyGameDelay(i);
        if (this.mIsGameStatisticForeground) {
            trimElapsedRealtime(this.mElapsedRealtimeGameStall, 10);
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            String topAppName = getTopAppName();
            if ("com.tencent.tmgp.sgame".equals(topAppName)) {
                int[] iArr = sTmgpGameDelayThresholds;
                gameDelayLevel = getGameDelayLevel(i, iArr);
                gameDelayLevel2 = getGameDelayLevel(this.mLastGameDelay, iArr);
            } else if ("com.tencent.lolm".equals(topAppName)) {
                int[] iArr2 = sLolmGameDelayThresholds;
                gameDelayLevel = getGameDelayLevel(i, iArr2);
                gameDelayLevel2 = getGameDelayLevel(this.mLastGameDelay, iArr2);
            } else if ("com.tencent.jkchess".equals(topAppName)) {
                int[] iArr3 = sJkchessGameDelayThresholds;
                gameDelayLevel = getGameDelayLevel(i, iArr3);
                gameDelayLevel2 = getGameDelayLevel(this.mLastGameDelay, iArr3);
            } else {
                gameDelayLevel = -1;
                gameDelayLevel2 = -1;
            }
            if (gameDelayLevel2 != gameDelayLevel && this.mIsMobileDataOn) {
                calcGameDelayLevelDuration(gameDelayLevel2);
            }
            boolean z = true;
            if (gameDelayLevel >= 1) {
                localLog("G_D", "G_D = " + i + ", L_G_D =" + this.mLastGameDelay + ", userScene =" + getGameUserScene() + ", GameName = " + topAppName);
            }
            this.mLastGameDelay = i;
            boolean z2 = false;
            if (gameDelayLevel >= 4) {
                this.mIsHolderLevel4 = true;
                this.mHandler.removeMessages(33);
                MetricsHandler metricsHandler = this.mHandler;
                metricsHandler.sendMessageDelayed(metricsHandler.obtainMessage(33), 3000L);
            } else {
                if (this.mIsHolderLevel4) {
                    this.mIsHolderLevel4 = false;
                }
                if (this.mHandler.hasMessages(33)) {
                    this.mHandler.removeMessages(33);
                }
            }
            if (gameDelayLevel >= 2 && gameDelayLevel2 >= 2) {
                if (jElapsedRealtime - this.mLastGameHighDelayTime < 2000) {
                    log("The interval between two high time delays exceeds two seconds：mLastGameHighDelayTime = " + this.mLastGameHighDelayTime);
                    return;
                } else {
                    if (this.VDBG) {
                        log("Exceeds 200ms twice in a row, it can also be marked as game stall.");
                    }
                    z2 = true;
                }
            }
            if (gameDelayLevel >= 2) {
                this.mLastGameHighDelayTime = jElapsedRealtime;
                this.mElapsedRealtimeGameStall.add(Long.valueOf(jElapsedRealtime));
                if (this.mElapsedRealtimeGameStall.size() < 3) {
                    z = z2;
                } else if (this.VDBG) {
                    log("More than 3 times in 200ms within 10S, also marked as game stall.");
                }
            } else {
                z = z2;
            }
            if (z) {
                localLog("G_S", "G_D_L[" + gameDelayLevel + "]");
                this.mHandler.obtainMessage(32).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDataStall(Scene scene) {
        NetworkSpeedTracker networkSpeedTracker;
        if (!this.mIsGameStatisticForeground && !this.mIsVoipForgroud && !isLiveStreamVideo()) {
            log("processDataStall skip : Only for game and voip and live");
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis() - this.mTriggerDataStallTime;
        if (!isSatifyDataStallLteNrOpt(scene) || this.mTriggerDataStallTime == 0 || jCurrentTimeMillis <= MirrorDesktopHelper.TIMEOUT_MILLIS || jCurrentTimeMillis >= 15000 || !doNr2LteOrRestoreForDataStallInvalid()) {
            long jCurrentTimeMillis2 = System.currentTimeMillis() - this.mNrLteChangeInGameTime;
            boolean z = false;
            if (isSatifyDataStallLteNrOpt(scene) && this.mDepriorityNr2Lte && this.mNrLteChangeInGameTime != 0 && jCurrentTimeMillis2 > 3000 && jCurrentTimeMillis2 < 13000) {
                this.mDepriorityNr2Lte = false;
                restoreNrIfIndeed(Scene.GAME);
                return;
            }
            if (this.mTriggerDataStallTime != 0 && jCurrentTimeMillis <= 90000) {
                log("processDataStall skip : Only one data stall will be reported within MAX_DATA_STALL_RECOVERY_TIME");
                return;
            }
            this.mNrLteChangeInGameTime = 0L;
            if ((scene == Scene.VOIP && (isSatifySignalForVoipStall() || this.VDBG)) || (scene == Scene.LIVE && (isSatifySignalForCommonStall(scene) || this.VDBG))) {
                int voipDirection = (scene == Scene.LIVE || (networkSpeedTracker = this.mSpeedTracker) == null) ? 1 : networkSpeedTracker.getVoipDirection();
                this.mTriggerDataStallTime = System.currentTimeMillis();
                if (this.mIsHolderVoipStall && !this.mHandler.hasMessages(36) && this.mLastCellChangeTime != -1 && System.currentTimeMillis() - this.mLastCellChangeTime > MirrorDesktopHelper.TIMEOUT_MILLIS && System.currentTimeMillis() - this.mLastCellChangeTime < 7000) {
                    z = true;
                }
                setDataStallOptimization(voipDirection, this.VDBG ? true : z, scene);
                MiuiQmsStats.getInstance().dataLinkStall();
                return;
            }
            if (scene == Scene.GAME) {
                if (isSatifySignalForCommonStall(scene) || this.VDBG) {
                    this.mTriggerDataStallTime = System.currentTimeMillis();
                    if (this.mIsHolderLevel4 && !this.mHandler.hasMessages(33) && this.mLastCellChangeTime != -1 && System.currentTimeMillis() - this.mLastCellChangeTime > 3000 && System.currentTimeMillis() - this.mLastCellChangeTime < MirrorDesktopHelper.TIMEOUT_MILLIS) {
                        z = true;
                    }
                    if (this.VDBG) {
                        z = true;
                    }
                    setDataStallOptimization(1, z, scene);
                    MiuiQmsStats.getInstance().gameDelay();
                }
            }
        }
    }

    private boolean isSatifyDataStallLteNrOpt(Scene scene) {
        TelephonySensorManager telephonySensorManager;
        return (scene != Scene.GAME || this.mCurrentMoveLikeState || this.mCurrentHstState || this.mCurrentSubwayState || (telephonySensorManager = TelephonySensorManager.getInstance()) == null || telephonySensorManager.isInElevator()) ? false : true;
    }

    private boolean doNr2LteOrRestoreForDataStallInvalid() {
        boolean zDepriorityNr2Lte = false;
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null) {
            if (this.mNrLteChangeInGameTime != 0) {
                return false;
            }
            if (System.currentTimeMillis() - this.mLastNrLteChangeInGameTime < 1200000) {
                log("doNr2LteOrRestoreForDataStallInvalid skip for timeLimit");
                return false;
            }
            if (!this.mDepriorityNr2Lte && isNrNwType(this.mServiceState[this.mDdsPhoneId].getDataNetworkType())) {
                zDepriorityNr2Lte = depriorityNr2Lte();
                if (zDepriorityNr2Lte) {
                    log("doNr2LteOrRestoreForDataStallInvalid DSA");
                }
            } else if (this.mDepriorityNr2Lte) {
                this.mDepriorityNr2Lte = false;
                restoreNrIfIndeed(Scene.GAME);
                log("doNr2LteOrRestoreForDataStallInvalid ESA");
                zDepriorityNr2Lte = true;
            }
            if (zDepriorityNr2Lte) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                this.mLastNrLteChangeInGameTime = jCurrentTimeMillis;
                this.mNrLteChangeInGameTime = jCurrentTimeMillis;
            }
        }
        return zDepriorityNr2Lte;
    }

    /* JADX WARN: Code duplicated, block: B:18:0x004b  */
    private boolean isSatifySignalForVoipStall() {
        boolean z = false;
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null) {
            int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
            if (isLteNwType(dataNetworkType)) {
                if (this.mLteRsrp[this.mDdsPhoneId] != Integer.MAX_VALUE && this.mLteSnr[this.mDdsPhoneId] != Integer.MAX_VALUE) {
                    if (this.mLteRsrp[this.mDdsPhoneId] < -102 || this.mLteSnr[this.mDdsPhoneId] < 5) {
                        z = true;
                    }
                    log("isSatifySignalForVoipStall rat=" + dataNetworkType + ",rst=" + z);
                }
            } else if (isNrNwType(dataNetworkType)) {
                if (this.mNrRsrp[this.mDdsPhoneId] != Integer.MAX_VALUE && this.mNrSnr[this.mDdsPhoneId] != Integer.MAX_VALUE) {
                    if (this.mNrRsrp[this.mDdsPhoneId] < -102 || this.mNrSnr[this.mDdsPhoneId] < 5) {
                        z = true;
                    }
                    log("isSatifySignalForVoipStall rat=" + dataNetworkType + ",rst=" + z);
                }
            } else {
                log("isSatifySignalForVoipStall rat=" + dataNetworkType + ",rst=" + z);
            }
        }
        return z;
    }

    private boolean isSmartDualSimSwitchState() {
        return "true".equals(SystemProperties.get("ril.temp.dds.state", "false"));
    }

    private boolean isSatifySignalForCommonStall(Scene scene) {
        boolean z = false;
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null) {
            log("isSatifySignalForCommonStall invalid phoneid");
            return false;
        }
        int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
        if ((isNrNwType(dataNetworkType) && this.mNrRsrp[this.mDdsPhoneId] >= -105) || (isLteNwType(dataNetworkType) && this.mLteRsrp[this.mDdsPhoneId] >= -105)) {
            z = true;
        }
        boolean z2 = (scene == Scene.GAME && this.mIsHolderLevel4 && !this.mHandler.hasMessages(33)) ? true : z;
        if (!z2) {
            log("isSatifySignalForCommonStall rst=" + z2);
        }
        return z2;
    }

    public void registerGameDelayListener(GameDelayListener gameDelayListener) {
        if (gameDelayListener == null) {
            return;
        }
        synchronized (this.mGameDelayListeners) {
            try {
                if (findGameDelayListener(gameDelayListener) < 0) {
                    this.mGameDelayListeners.add(gameDelayListener);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void unregisterGameDelayListener(GameDelayListener gameDelayListener) {
        if (gameDelayListener == null) {
            return;
        }
        synchronized (this.mGameDelayListeners) {
            try {
                int iFindGameDelayListener = findGameDelayListener(gameDelayListener);
                if (iFindGameDelayListener >= 0) {
                    this.mGameDelayListeners.remove(iFindGameDelayListener);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private int findGameDelayListener(GameDelayListener gameDelayListener) {
        int size = this.mGameDelayListeners.size();
        for (int i = 0; i < size; i++) {
            if (this.mGameDelayListeners.get(i) == gameDelayListener) {
                return i;
            }
        }
        return -1;
    }

    private void notifyGameDelay(int i) {
        synchronized (this.mGameDelayListeners) {
            try {
                int size = this.mGameDelayListeners.size();
                for (int i2 = 0; i2 < size; i2++) {
                    ((GameDelayListener) this.mGameDelayListeners.get(i2)).onGameDelayChanged(i);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt("game_delay_level", i);
        EventHubManager.getInstance().postEvent(new NetworkSceneEvent.GameDelayEvent("game_delay_level_changed", bundle));
    }

    private void notifyGameStatus(int i, String str) {
        synchronized (this.mGameDelayListeners) {
            try {
                int size = this.mGameDelayListeners.size();
                for (int i2 = 0; i2 < size; i2++) {
                    ((GameDelayListener) this.mGameDelayListeners.get(i2)).onGameStatusChanged(i, str);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt("game_delay_type", i);
        bundle.putString("game_delay_top_game", str);
        EventHubManager.getInstance().postEvent(new NetworkSceneEvent.GameDelayEvent("game_type_status_changed", bundle));
    }

    private void notifyGameBattleStatus(boolean z) {
        synchronized (this.mGameDelayListeners) {
            try {
                int size = this.mGameDelayListeners.size();
                for (int i = 0; i < size; i++) {
                    ((GameDelayListener) this.mGameDelayListeners.get(i)).onGameBattleStatusChanged(z);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("game_delay_battle", z);
        EventHubManager.getInstance().postEvent(new NetworkSceneEvent.GameDelayEvent("game_battle_status_changed", bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetDelayEvent(int i, Bundle bundle) {
        int i2 = bundle.getInt("net_delay_info", 0);
        if (this.VDBG) {
            log("onEvent, event = " + i + ", delay =" + i2);
        }
        if (i2 != 0) {
            this.mHandler.obtainMessage(56, Integer.valueOf(i2)).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGameStartEvent(int i, Bundle bundle) {
        boolean z = bundle.getBoolean("game_start", false);
        if (this.VDBG) {
            log("onEvent, event = " + i + ", isGameBattle =" + z);
        }
        if (z != this.mIsLastGameBattle) {
            this.mHandler.obtainMessage(57, Boolean.valueOf(z)).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGameUpdateEvent(int i, Bundle bundle) {
        boolean z = bundle.getBoolean("game_update", false);
        if (this.VDBG) {
            log("onEvent, event = " + i + ", isGameUpdating =" + z);
        }
        this.mHandler.obtainMessage(66, Boolean.valueOf(z)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameUpdateStatus(boolean z) {
        log("processGameUpdateStatus isGameUpdating=" + z);
        notifyGameUpdateStatus(z);
    }

    private void notifyGameUpdateStatus(boolean z) {
        synchronized (this.mGameDelayListeners) {
            try {
                int size = this.mGameDelayListeners.size();
                for (int i = 0; i < size; i++) {
                    ((GameDelayListener) this.mGameDelayListeners.get(i)).onGameUpdateStatusChanged(z);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameBattle(boolean z) {
        int gameDelayLevel;
        String topAppName = getTopAppName();
        localLog("G_B", "G_B = " + z + ", L_G_B =" + this.mIsLastGameBattle + " topAppName = " + topAppName);
        this.mIsLastGameBattle = z;
        notifyGameBattleStatus(z);
        if (z) {
            if (this.mIsMobileDataOn) {
                this.mLastDelayLevelStartTimes = SystemClock.elapsedRealtime();
                return;
            }
            return;
        }
        this.mLastGameDelay = 0;
        if (this.mLastDelayLevelStartTimes != 0 && this.mIsMobileDataOn) {
            if ("com.tencent.tmgp.sgame".equals(topAppName)) {
                gameDelayLevel = getGameDelayLevel(this.mLastGameDelay, sTmgpGameDelayThresholds);
            } else if ("com.tencent.lolm".equals(topAppName)) {
                gameDelayLevel = getGameDelayLevel(this.mLastGameDelay, sLolmGameDelayThresholds);
            } else {
                gameDelayLevel = "com.tencent.jkchess".equals(topAppName) ? getGameDelayLevel(this.mLastGameDelay, sJkchessGameDelayThresholds) : -1;
            }
            calcGameDelayLevelDuration(gameDelayLevel);
        }
        ConcurrentHashMap concurrentHashMap = this.mTencentGameStats;
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            return;
        }
        reportGameStatistic();
    }

    private void calcGameDelayLevelDuration(int i) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mLastDelayLevelStartTimes;
        if (j == 0) {
            this.mLastDelayLevelStartTimes = jElapsedRealtime;
            return;
        }
        long j2 = jElapsedRealtime - j;
        if (j2 < 1 || (i == 4 && j2 > 120000)) {
            this.mLastDelayLevelStartTimes = jElapsedRealtime;
            return;
        }
        if (i == 0) {
            recordDuration(this.mTencentGameStats, "level_0", j2);
        } else if (i == 1) {
            recordDuration(this.mTencentGameStats, "level_1", j2);
        } else if (i == 2) {
            recordDuration(this.mTencentGameStats, "level_2", j2);
        } else if (i == 3) {
            recordDuration(this.mTencentGameStats, "level_3", j2);
        } else if (i == 4) {
            recordDuration(this.mTencentGameStats, "level_4", j2);
        }
        this.mLastDelayLevelStartTimes = jElapsedRealtime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameDelayWifiStateChanged(boolean z) {
        int gameDelayLevel;
        if (!z && this.mIsLastGameBattle && this.mIsMobileDataOn) {
            this.mLastDelayLevelStartTimes = SystemClock.elapsedRealtime();
            return;
        }
        if (z && this.mLastDelayLevelStartTimes != 0 && this.mIsLastGameBattle) {
            String topAppName = getTopAppName();
            if ("com.tencent.tmgp.sgame".equals(topAppName)) {
                gameDelayLevel = getGameDelayLevel(this.mLastGameDelay, sTmgpGameDelayThresholds);
            } else if ("com.tencent.lolm".equals(topAppName)) {
                gameDelayLevel = getGameDelayLevel(this.mLastGameDelay, sLolmGameDelayThresholds);
            } else {
                gameDelayLevel = "com.tencent.jkchess".equals(topAppName) ? getGameDelayLevel(this.mLastGameDelay, sJkchessGameDelayThresholds) : -1;
            }
            calcGameDelayLevelDuration(gameDelayLevel);
        }
    }

    public void recordDuration(ConcurrentHashMap concurrentHashMap, String str, long j) {
        if (concurrentHashMap == null) {
            return;
        }
        concurrentHashMap.put(str, Long.valueOf((concurrentHashMap.get(str) != null ? ((Long) concurrentHashMap.get(str)).longValue() : 0L) + j));
    }

    public void reportGameStatistic() {
        ConcurrentHashMap concurrentHashMap = this.mTencentGameStats;
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : this.mTencentGameStats.entrySet()) {
            String str = (String) entry.getKey();
            Object value = entry.getValue();
            if (!TextUtils.isEmpty(str)) {
                HashMap map = new HashMap();
                map.put("Key", str);
                map.put("Value", String.valueOf(value));
                arrayList.add(map);
            }
        }
        if (arrayList.size() > 0) {
            HashMap map2 = new HashMap();
            map2.put("Params", arrayList);
            map2.put("RAT", ServiceState.rilRadioTechnologyToString(getDataRat(this.mServiceState[this.mDdsPhoneId])));
            map2.put("rsrp", String.valueOf(getDbm(this.mSignalStrength[this.mDdsPhoneId])));
            map2.put("snr", String.valueOf(getSnr(this.mDdsPhoneId)));
            map2.put("Band", Utils.getBand(this.mDdsPhoneId, this.mServiceState[this.mDdsPhoneId]));
            map2.put("CellIdentity", getDdsLteNrCid());
            map2.put("appPackageName", this.mCurrentForegroundPackageName);
            FiveGCloudController fiveGCloudController = FiveGCloudController.getInstance();
            String province = "";
            String city = fiveGCloudController != null ? fiveGCloudController.getCity() : "";
            if (fiveGCloudController != null) {
                province = fiveGCloudController.getProvince();
            }
            map2.put("c", city);
            map2.put("p", province);
            if (MiuiTelephonyStatistic.getInstance() != null) {
                map2.put("sceneType", MiuiTelephonyStatistic.getInstance().getSceneType());
                map2.put("SimOperator", MiuiTelephonyStatistic.getInstance().getSimOperator());
            }
            map2.put("DdsPhoneId", String.valueOf(this.mDdsPhoneId));
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_tencent_game_delay_statistic", map2);
            this.mTencentGameStats.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processCellLocationChangeEvent(int i, CellLocation cellLocation) {
        int i2;
        try {
            if (MiuiPhoneUtils.isValidatePhoneId(i) && cellLocation != null && !cellLocation.isEmpty()) {
                log("processCellLocationChangeEvent phoneId=" + i);
                Phone phone = MiuiPhoneUtils.getPhone(i);
                if (phone != null && phone.getCurrentCellIdentity() != null && phone.getCurrentCellIdentity().getGlobalCellId() != null) {
                    if (isDefaultDataSlotId(i)) {
                        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
                        CellIdentity cellIdentity = this.mCellIdentity[i];
                        String globalCellId = currentCellIdentity.getGlobalCellId();
                        String globalCellId2 = cellIdentity.getGlobalCellId();
                        if (!TextUtils.isEmpty(globalCellId) && !TextUtils.isEmpty(globalCellId2) && !TextUtils.equals(globalCellId, globalCellId2)) {
                            log("processCellLocationChangeEvent newCGI=" + globalCellId + ",preCGI=" + globalCellId2 + ",dds phoneId=" + i);
                            if (currentCellIdentity instanceof CellIdentityNr) {
                                this.mNrCi = (CellIdentityNr) currentCellIdentity;
                            } else if (currentCellIdentity instanceof CellIdentityLte) {
                                this.mLteCi = (CellIdentityLte) currentCellIdentity;
                            }
                            processCellSignalSceneForC6C7(currentCellIdentity, i);
                            processCellSignalSceneForElevator(currentCellIdentity, i);
                            tryDsaForBasementVoip(false);
                            this.mRecovery.updateCellGobalId(globalCellId, i);
                            MiuiQmsStats.getInstance().cellChange();
                            updateSignalMapBadCell(i);
                            this.mLastCellChangeTime = System.currentTimeMillis();
                            if (ModemProvider.IS_MTK && MiuiPhoneUtils.isNetworkQoeSupported() && this.mNeedMtkDataStallNormalAfterCellChange && (i2 = this.mLastDataStallDirection) != -1) {
                                this.mRecovery.resetDataStallOptimization(i, i2, Scene.CELLCHANGED);
                                this.mNeedMtkDataStallNormalAfterCellChange = false;
                            }
                        }
                        if (!isLteNrRegistered()) {
                            cellChangeTryForceLte(globalCellId, globalCellId2, i);
                        }
                        this.mCellIdentity[i] = currentCellIdentity;
                        SmartNetworkRecoveryController smartNetworkRecoveryController = this.mSmartNetworkRecovery;
                        if (smartNetworkRecoveryController != null) {
                            smartNetworkRecoveryController.mHandler.obtainMessage(53, i, 0, currentCellIdentity).sendToTarget();
                        }
                    } else if (MiuiPhoneUtils.getInsertedSimCount() == 2 && i == getAnotherPhoneId(this.mDdsPhoneId)) {
                        ServiceState serviceState = this.mServiceState[i];
                        if (serviceState == null) {
                            log("processCellLocationChangeEvent ignore for ndds");
                            return;
                        }
                        int dataNetworkType = serviceState.getDataNetworkType();
                        if (!isLteNwType(dataNetworkType)) {
                            log("processCellLocationChangeEvent invalid networkType" + dataNetworkType);
                            this.mLteCellCountNdds = 0;
                            return;
                        }
                        CellIdentity currentCellIdentity2 = phone.getCurrentCellIdentity();
                        CellIdentity cellIdentity2 = this.mCellIdentity[i];
                        String globalCellId3 = currentCellIdentity2.getGlobalCellId();
                        String globalCellId4 = cellIdentity2.getGlobalCellId();
                        if (!TextUtils.isEmpty(globalCellId3) && !TextUtils.isEmpty(globalCellId4) && !TextUtils.equals(globalCellId3, globalCellId4)) {
                            log("processCellLocationChangeEvent newCGI=" + globalCellId3 + ",preCGI=" + globalCellId4 + ",ndds phoneId=" + i + "mLteCellCountNdds=" + this.mLteCellCountNdds);
                            this.mLteCellCountNdds = this.mLteCellCountNdds + 1;
                        }
                        if (!isNrRegisteredforNdds()) {
                            cellChangeTryForceL2NRForNdds(globalCellId3, globalCellId4, i);
                        }
                        this.mCellIdentity[i] = currentCellIdentity2;
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void updateSignalMapBadCell(int i) {
        if (i != this.mDdsPhoneId) {
            return;
        }
        try {
            boolean zIsSMapBadCell = isSMapBadCell();
            boolean z = this.mBadCell;
            if (!zIsSMapBadCell) {
                if (z) {
                    SystemProperties.set("ril.smap.badcell", "");
                    this.mBadCell = false;
                    return;
                }
                return;
            }
            if (z) {
                return;
            }
            SystemProperties.set("ril.smap.badcell", "smap_bad_cell");
            this.mBadCell = true;
            recordCount("cellHit");
        } catch (Exception unused) {
            log("SYSTEM_PROP_SMAP_BAD_CELL write fail");
        }
    }

    private void recordCount(String str) {
        Integer num = (Integer) this.mStats.get(str);
        Map map = this.mStats;
        if (num == null) {
            map.put(str, 1);
        } else {
            map.put(str, Integer.valueOf(num.intValue() + 1));
        }
    }

    public void reportStatistics() {
        CloudCellDbHelper cloudCellDbHelper = CloudCellDbHelper.getInstance(this.mContext);
        this.mDbHelper = cloudCellDbHelper;
        try {
            this.mStats.put("dbCellCount", Integer.valueOf(cloudCellDbHelper.getSignalMapCellCount()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SignalMapCloudController signalMapCloudController = SignalMapCloudController.getInstance();
        this.mStats.put("cloudIssued", Integer.valueOf(signalMapCloudController.recordCloudIssued()));
        HashMap snsBlackCellMap = signalMapCloudController.getSnsBlackCellMap();
        if (snsBlackCellMap != null && snsBlackCellMap.size() != 0) {
            this.mStats.put("memoryCellCnt", Integer.valueOf(snsBlackCellMap.size()));
        }
        Map map = this.mStats;
        if (map == null || map.isEmpty()) {
            return;
        }
        HashMap map2 = new HashMap(this.mStats);
        log("reportStatistics mStats=" + this.mStats);
        this.mStats.clear();
        MiStatInterfaceUtil.recordObjectEvent("category", "event_singalmap_sns", map2);
    }

    private void cellChangeTryForceLte(String str, String str2, int i) {
        if (isDefaultDataSlotId(i)) {
            if ((TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) || TextUtils.equals(str, str2) || !isNecessaryCase(i) || isSmartDualSimActive()) {
                return;
            }
            int i2 = this.mForceLteCount;
            if ((i2 == 1 || i2 == 0) && !this.mHandler.hasMessages(18)) {
                log("cellChangeTryForceLte, try force lte");
                this.mForceLteCount = 0;
                startForceLteMonitorTimer(OfflineUtils.Offline.SAMPLE_RATE_VALUE_8KHZ);
            }
        }
    }

    private void cellChangeTryForceL2NRForNdds(String str, String str2, int i) {
        if (isDefaultDataSlotId(i)) {
            return;
        }
        if ((TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) || TextUtils.equals(str, str2) || !isNecessaryCase(i) || isSmartDualSimActive()) {
            return;
        }
        int i2 = this.mForceNrCount;
        if ((i2 == 2 || i2 == 0) && !this.mHandler.hasMessages(30) && this.mLteCellCountNdds == 2) {
            log("cellChangeTryForceL2NR, try force nr");
            this.mForceNrCount = 0;
            startForceNrMonitorTimer(OfflineUtils.Offline.SAMPLE_RATE_VALUE_8KHZ);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMobileStateEvent(boolean z) {
        this.mIsMobileDataOn = z;
        this.mNetId = z ? getDefaultNetworkNetId() : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processWifiStateEvent(boolean z) {
        this.mIsWifiDataOn = z;
    }

    private void startForceLteMonitorTimer(int i) {
        if ((this.mForceLteCount == 0 || !this.mHandler.hasMessages(18)) && !this.mHandler.hasMessages(8)) {
            log("startForceLteMonitorTimer delayInMs:" + i);
            stopForceLteMonitorTimer();
            MetricsHandler metricsHandler = this.mHandler;
            metricsHandler.sendMessageDelayed(metricsHandler.obtainMessage(8), i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopForceLteMonitorTimer() {
        this.mForceLteCount = 0;
        this.mHandler.removeMessages(8);
        this.mHandler.removeMessages(18);
    }

    class SettingsObserver extends ContentObserver {
        private final Context mContext;
        private final Handler mHandler;
        private final HashMap mUriEventMap;

        SettingsObserver(NetworkMetricsController networkMetricsController, Context context, Handler handler) {
            super(null);
            this.mUriEventMap = new HashMap();
            this.mContext = context;
            this.mHandler = handler;
        }

        void observe(Uri uri, int i) {
            this.mUriEventMap.put(uri, Integer.valueOf(i));
            this.mContext.getContentResolver().registerContentObserver(uri, false, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            NetworkMetricsController.log("Should never be reached.");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            Integer num = (Integer) this.mUriEventMap.get(uri);
            if (7 == num.intValue()) {
                this.mHandler.obtainMessage(7).sendToTarget();
                return;
            }
            if (9 == num.intValue()) {
                this.mHandler.obtainMessage(9).sendToTarget();
                return;
            }
            if (13 == num.intValue()) {
                this.mHandler.obtainMessage(13).sendToTarget();
            } else {
                if (71 == num.intValue()) {
                    this.mHandler.obtainMessage(71).sendToTarget();
                    return;
                }
                NetworkMetricsController.log("No matching event to send for URI=" + uri);
            }
        }
    }

    private void registerSettingsCallbacks() {
        this.mSettingsObserver.observe(Settings.Global.getUriFor("special_network_optimization"), 7);
        this.mSettingsObserver.observe(Settings.Global.getUriFor("smart_dual_sim"), 9);
        this.mSettingsObserver.observe(Settings.Global.getUriFor("fiveg_user_enable"), 13);
        this.mSettingsObserver.observe(Settings.Secure.getUriFor("hyper_engine_game_optimization_enabled"), 71);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSmartDualSimSettingsOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "smart_dual_sim", 0) == 1;
    }

    public boolean isHighSpeedTrain() {
        return this.mCurrentHstState;
    }

    private boolean isInStaticStatus() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_state_extent_move", 2) == 2;
    }

    private int getOperatorScoreByHST() {
        HighSpeedTrainModeController highSpeedTrainModeController = HighSpeedTrainModeController.getInstance();
        if (highSpeedTrainModeController != null) {
            return highSpeedTrainModeController.getOperatorScore();
        }
        return 0;
    }

    private int operatorForDds() {
        Phone phone;
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || (phone = MiuiPhoneUtils.getPhone(this.mDdsPhoneId)) == null) {
            return 0;
        }
        if (MiuiPhoneUtils.isCmccSim(phone)) {
            return 1;
        }
        if (MiuiPhoneUtils.isChinaUnicomSim(phone)) {
            return 2;
        }
        return MiuiPhoneUtils.isChinaTelecomSim(phone) ? 3 : 0;
    }

    private void trimElapsedRealtime(LinkedList linkedList, int i) {
        Iterator it = linkedList.iterator();
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        while (it.hasNext() && jElapsedRealtime - ((Long) it.next()).longValue() > TimeUnit.SECONDS.toMillis(i)) {
            it.remove();
        }
    }

    public void restoreNrIfIndeed(Scene scene) {
        if (scene == null) {
            log("restoreNrIfIndeed sceneType is null");
            return;
        }
        if (this.mRecovery.isNsaOffInRecoveryAction(this.mDdsPhoneId)) {
            localLog("ENSA", "s[" + scene.getValue() + "]");
            doRecoveryIfNeed(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, this.mDdsPhoneId, true, scene);
            return;
        }
        if (this.mRecovery.isSaOffInRecoveryAction(this.mDdsPhoneId)) {
            localLog("ESA", "s[" + scene.getValue() + "]");
            doRecoveryIfNeed(55, this.mDdsPhoneId, true, scene);
            dfsDeprioritizeOrRestoreSa(2, scene);
        }
    }

    private boolean isNr2LteRedirection(int i) {
        return isNecessaryCase(i) && isDepriorityLteScene() && getCurrentSaStatus() == 1 && !this.mRecovery.isSaOffInRecoveryAction(i) && isNrPoorAndLteGood(i);
    }

    private boolean isNrPoorAndLteGood(int i) {
        int i2;
        log("isNrPoorAndLteGood nrrsrp = " + this.mNrRsrp[i] + ",lteRsrp= " + this.mLteRsrp[i] + ",mGame = " + this.mGame);
        int i3 = HyperOSCustFeatureResolve.getInt("cust_rsrp_offset_threshold", 0);
        boolean z = this.mGame;
        int i4 = -110;
        if (z && z) {
            i4 = -100;
        }
        int i5 = i4 - i3;
        int i6 = (-105) + i3;
        int i7 = this.mNrRsrp[i];
        return i7 != Integer.MAX_VALUE && i7 < i5 && (i2 = this.mLteRsrp[i]) != Integer.MAX_VALUE && i2 > i6 && i7 < i2;
    }

    private boolean depriorityNr2Lte() {
        if (MiuiPhoneUtils.isSpecialTestEnv(this.mDdsPhoneId)) {
            log("skip depriorityNr2Lte for special test env");
            return false;
        }
        log("try to depriorityNr2Lte");
        this.mHandler.sendEmptyMessage(15);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDepriorityNr2Lte() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.isTputAppInForground) {
            log("dds= " + this.mDdsPhoneId + ",tputApp=" + this.isTputAppInForground);
            return;
        }
        boolean z = this.mIsThermalScene;
        if (z || ((this.mGame && !this.mCurrentHstState) || this.mIsBuildingMode || this.mIsPowerSaveOn)) {
            this.mDepriorityNr2Lte = true;
            Scene scene = Scene.GAME;
            if (this.mIsPowerSaveOn) {
                scene = Scene.POWER_SAVE;
            } else if (z) {
                scene = Scene.THERMAL;
            } else if (this.mIsBuildingMode) {
                scene = Scene.BUILDING_MODE;
            }
            localLog("DSA", "s[" + scene.getValue() + "]");
            doRecoveryIfNeed(53, this.mDdsPhoneId, false, scene);
            dfsDeprioritizeOrRestoreSa(1, scene);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processRestoreSaForDepriorityNr2Lte() {
        if ((this.mCurrentHstState || this.mCurrentSubwayState || this.mCurrentMoveLikeState) && this.mDepriorityNr2Lte) {
            this.mDepriorityNr2Lte = false;
            restoreNrIfIndeed(Scene.IN_HST_OR_SUBWAY_OR_MOVELIKE);
        }
    }

    public void onBasementQoeChanged(int i, int i2) {
        this.mHandler.obtainMessage(43, i2, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processBasementCellQoeChanged(int i) {
        if (!isBasementOptForeground() || i < 0) {
            return;
        }
        log("processBasementCellQoeChanged networkScore=" + i);
        recordQoeSampleForBasement(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSceneAwareCallback() {
        if (this.mSceneAwareCallback != null) {
            return;
        }
        try {
            HyperCommManager hyperCommManager = HyperCommManager.getDefault();
            if (hyperCommManager == null) {
                log("HyperCommManager is not available");
                retryRegisterSceneAwareCallback();
                return;
            }
            NetworkMetricsSceneAwareCallback networkMetricsSceneAwareCallback = new NetworkMetricsSceneAwareCallback(new HandlerExecutor(this.mHandler));
            boolean zRegisterSceneAwareCallback = hyperCommManager.registerSceneAwareCallback(networkMetricsSceneAwareCallback);
            if (zRegisterSceneAwareCallback) {
                this.mSceneAwareCallback = networkMetricsSceneAwareCallback;
            } else {
                retryRegisterSceneAwareCallback();
            }
            log("registerSceneAwareCallback result=" + zRegisterSceneAwareCallback);
        } catch (Exception e) {
            log("registerSceneAwareCallback exception: " + e);
            retryRegisterSceneAwareCallback();
        }
    }

    private void retryRegisterSceneAwareCallback() {
        MetricsHandler metricsHandler = this.mHandler;
        if (metricsHandler == null || this.mSceneAwareCallback != null) {
            return;
        }
        metricsHandler.removeMessages(70);
        this.mHandler.sendEmptyMessageDelayed(70, 30000L);
    }

    class NetworkMetricsSceneAwareCallback extends SceneAwareCallback implements SceneAwareCallback.SubwayStateListener {
        NetworkMetricsSceneAwareCallback(HandlerExecutor handlerExecutor) {
            super(handlerExecutor);
        }

        public void onSubwayStateChanged(boolean z, boolean z2) {
            NetworkMetricsController.log("onSubwayStateChanged subwayState=" + z + ", isDomestic=" + z2);
            if (z2) {
                NetworkMetricsController.this.mHandler.obtainMessage(69, z ? 1 : 0, 0).sendToTarget();
            }
        }
    }

    public void onSubwayQoeChanged(int i, int i2, int i3) {
        this.mHandler.obtainMessage(68, i3, i2).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSubwayQoeChanged(int i, int i2) {
        if (i2 == -1) {
            if (this.mLastDataStallDirection != -1) {
                resetDataStallOptimization(this.mDdsPhoneId);
                return;
            }
            return;
        }
        if (!this.mIsFeatureEnable || this.isTputAppInForground) {
            log("processSubwayQoeChanged skip: feature=" + this.mIsFeatureEnable + ",tputApp=" + this.isTputAppInForground);
            return;
        }
        if (i < 0 || i >= 80 || !this.mIsSubwayMode) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (jElapsedRealtime - this.mLastSubwayQoeDataStallTime < 20000) {
            log("processSubwayQoeChanged skip: rate limited, elapsed=" + (jElapsedRealtime - this.mLastSubwayQoeDataStallTime) + "ms");
            return;
        }
        log("processSubwayQoeChanged subway qoe stall, networkScore=" + i);
        this.mLastSubwayQoeDataStallTime = jElapsedRealtime;
        setDataStallOptimization(1, true, Scene.SUBWAY_QOE_DATA_STALL);
    }

    public boolean isSubwayRunning() {
        return this.mIsSubwayMode;
    }

    private void recordRenderSampleForBasement(int i) {
        if (this.mRenderStallSamplesForBasement == null || !isBasementOptForeground()) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        this.mRenderStallSamplesForBasement.add(new TimedSample(jElapsedRealtime, i));
        trimTimedSamples(this.mRenderStallSamplesForBasement, jElapsedRealtime);
        onVoipSampleForBasement(i == 0);
    }

    private void recordQoeSampleForBasement(int i) {
        if (this.mQoeScoreSamplesForBasement == null || !isBasementOptForeground()) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        this.mQoeScoreSamplesForBasement.add(new TimedSample(jElapsedRealtime, i));
        trimTimedSamples(this.mQoeScoreSamplesForBasement, jElapsedRealtime);
        onVoipSampleForBasement(i < 80);
    }

    private void onVoipSampleForBasement(boolean z) {
        if (this.mBasementDsaTime != 0) {
            tryEarlyEsaForBasement();
        } else if (z) {
            tryDsaForBasementVoip(false);
        }
    }

    private void trimTimedSamples(LinkedList linkedList, long j) {
        while (linkedList.size() > 1 && j - ((TimedSample) linkedList.get(1)).time > 10000) {
            linkedList.removeFirst();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryEarlyEsaForBasement() {
        if (this.mBasementDsaTime != 0 && MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null && isLteNwType(this.mServiceState[this.mDdsPhoneId].getDataNetworkType())) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (jElapsedRealtime - this.mBasementDsaTime < 10000) {
                return;
            }
            long j = jElapsedRealtime - 10000;
            double dRenderStallRatio = renderStallRatio(j, jElapsedRealtime);
            double d = this.mBasementRenderRatioBeforeDsa;
            boolean z = false;
            boolean z2 = d >= 0.0d && dRenderStallRatio >= 0.0d && dRenderStallRatio >= d;
            double dQoeWeightedAvg = qoeWeightedAvg(j, jElapsedRealtime);
            double d2 = this.mBasementQoeAvgBeforeDsa;
            if (d2 >= 0.0d && dQoeWeightedAvg >= 0.0d && dQoeWeightedAvg <= d2) {
                z = true;
            }
            log("tryEarlyEsaForBasement render before=" + this.mBasementRenderRatioBeforeDsa + ",after=" + dRenderStallRatio + ",qoe before=" + this.mBasementQoeAvgBeforeDsa + ",after=" + dQoeWeightedAvg);
            if (z2 && z) {
                log("tryEarlyEsaForBasement LTE no better than before DSA, early ESA");
                doEsaForBasement();
            }
        }
    }

    private double renderStallRatio(long j, long j2) {
        long jZohValueDuration = zohValueDuration(this.mRenderStallSamplesForBasement, j, j2, 0);
        if (jZohValueDuration < 0) {
            return -1.0d;
        }
        long j3 = j2 - j;
        if (j3 > 0) {
            return jZohValueDuration / j3;
        }
        return -1.0d;
    }

    private double qoeWeightedAvg(long j, long j2) {
        LinkedList linkedList = this.mQoeScoreSamplesForBasement;
        if (linkedList == null || linkedList.isEmpty()) {
            return -1.0d;
        }
        long j3 = 0;
        double d = 0.0d;
        int i = 0;
        while (i < linkedList.size()) {
            TimedSample timedSample = (TimedSample) linkedList.get(i);
            long jMax = Math.max(timedSample.time, j);
            i++;
            long jMin = i < linkedList.size() ? Math.min(((TimedSample) linkedList.get(i)).time, j2) : j2;
            if (jMin > jMax) {
                long j4 = jMin - jMax;
                d += ((double) timedSample.value) * j4;
                j3 += j4;
            }
            linkedList = linkedList;
        }
        if (j3 < 3000) {
            return -1.0d;
        }
        return d / j3;
    }

    private long zohValueDuration(LinkedList linkedList, long j, long j2, int i) {
        if (linkedList == null || linkedList.isEmpty()) {
            return -1L;
        }
        long j3 = 0;
        int i2 = 0;
        long j4 = 0;
        while (i2 < linkedList.size()) {
            TimedSample timedSample = (TimedSample) linkedList.get(i2);
            long jMax = Math.max(timedSample.time, j);
            i2++;
            long jMin = i2 < linkedList.size() ? Math.min(((TimedSample) linkedList.get(i2)).time, j2) : j2;
            if (jMin > jMax) {
                long j5 = jMin - jMax;
                j3 += j5;
                if (timedSample.value == i) {
                    j4 += j5;
                }
            }
        }
        if (j3 < 3000) {
            return -1L;
        }
        return j4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkLteRsrpForBasementEsa() {
        int i;
        if (this.mBasementDsaTime == 0 || !MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || SystemClock.elapsedRealtime() - this.mBasementDsaTime < 3000 || this.mServiceState[this.mDdsPhoneId] == null || !isLteNwType(this.mServiceState[this.mDdsPhoneId].getDataNetworkType()) || (i = this.mLteRsrp[this.mDdsPhoneId]) == Integer.MAX_VALUE || i >= this.mBasementVoipLteRsrpEsaThreshold) {
            return;
        }
        log("checkLteRsrpForBasementEsa LTE RSRP too poor=" + i + " < " + this.mBasementVoipLteRsrpEsaThreshold + ", ESA");
        doEsaForBasement();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doEsaForBasement() {
        if (this.mBasementDsaTime == 0) {
            return;
        }
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null || !isLteNwType(this.mServiceState[this.mDdsPhoneId].getDataNetworkType())) {
            int i = this.mBasementEsaRetryCount;
            if (i < 2) {
                this.mBasementEsaRetryCount = i + 1;
                log("doEsaForBasement defer: invalid phoneId or dds not on LTE, retry=" + this.mBasementEsaRetryCount);
                MetricsHandler metricsHandler = this.mHandler;
                metricsHandler.sendMessageDelayed(metricsHandler.obtainMessage(45), MirrorDesktopHelper.TIMEOUT_MILLIS);
                return;
            }
            log("doEsaForBasement skip restore: invalid phoneId or dds not on LTE after retry");
        } else {
            this.mHandler.removeMessages(44);
            this.mHandler.removeMessages(45);
            restoreNrIfIndeed(Scene.BASEMENT_VOIP_STALL);
            this.mLastBasementEsaTime = SystemClock.elapsedRealtime();
        }
        this.mBasementDsaTime = 0L;
        this.mBasementRenderRatioBeforeDsa = -1.0d;
        this.mBasementQoeAvgBeforeDsa = -1.0d;
        this.mBasementEsaRetryCount = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryDsaForBasementVoip(boolean z) {
        if (!this.mIsFeatureEnable || !MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || !this.mIsMobileDataOn || this.isTputAppInForground) {
            log("tryDsaForBasementVoip skip: feature=" + this.mIsFeatureEnable + ",dds=" + this.mDdsPhoneId + ",mobileData=" + this.mIsMobileDataOn + ",tputApp=" + this.isTputAppInForground);
            return;
        }
        if (this.mBasementDsaTime != 0) {
            return;
        }
        if (!isBasementOptForeground()) {
            log("tryDsaForBasementVoip skip: no basement opt foreground");
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mLastBasementEsaTime;
        if (j != 0 && jElapsedRealtime - j < 300000) {
            log("tryDsaForBasementVoip skip: in DSA cooldown");
            return;
        }
        if (this.mIsSubwayMode || this.mCurrentHstState) {
            log("tryDsaForBasementVoip skip: in subway/hst, subway=" + this.mIsSubwayMode + ",hst=" + this.mCurrentHstState);
            return;
        }
        if (this.mServiceState[this.mDdsPhoneId] == null || !isNrNwType(this.mServiceState[this.mDdsPhoneId].getDataNetworkType())) {
            log("tryDsaForBasementVoip skip: dds not on NR");
            return;
        }
        boolean z2 = z || isBasementVoipWhiteCellMatch();
        int ddsLteNrRsrp = getDdsLteNrRsrp();
        boolean z3 = ddsLteNrRsrp != Integer.MAX_VALUE && ddsLteNrRsrp < this.mBasementVoipRsrpThreshold;
        long j2 = jElapsedRealtime - 10000;
        double dRenderStallRatio = renderStallRatio(j2, jElapsedRealtime);
        double dQoeWeightedAvg = qoeWeightedAvg(j2, jElapsedRealtime);
        boolean z4 = ((dRenderStallRatio > 0.0d ? 1 : (dRenderStallRatio == 0.0d ? 0 : -1)) >= 0 && (dRenderStallRatio > 0.4d ? 1 : (dRenderStallRatio == 0.4d ? 0 : -1)) >= 0) || ((dQoeWeightedAvg > 0.0d ? 1 : (dQoeWeightedAvg == 0.0d ? 0 : -1)) >= 0 && (dQoeWeightedAvg > 80.0d ? 1 : (dQoeWeightedAvg == 80.0d ? 0 : -1)) < 0);
        log("tryDsaForBasementVoip outBasement=" + z + ",sceneMatch=" + z2 + ",renderRatio=" + dRenderStallRatio + ",qoeAvg=" + dQoeWeightedAvg + ",sustainedStall=" + z4 + ",rsrp=" + ddsLteNrRsrp);
        if (z2 && z4 && z3) {
            this.mBasementRenderRatioBeforeDsa = dRenderStallRatio;
            this.mBasementQoeAvgBeforeDsa = dQoeWeightedAvg;
            StringBuilder sb = new StringBuilder("s[");
            Scene scene = Scene.BASEMENT_VOIP_STALL;
            sb.append(scene.getValue());
            sb.append("]");
            localLog("DSA", sb.toString());
            doRecoveryIfNeed(53, this.mDdsPhoneId, true, scene);
            dfsDeprioritizeOrRestoreSa(1, scene);
            this.mBasementDsaTime = jElapsedRealtime;
            this.mBasementEsaRetryCount = 0;
            this.mHandler.removeMessages(44);
            MetricsHandler metricsHandler = this.mHandler;
            metricsHandler.sendMessageDelayed(metricsHandler.obtainMessage(44), 10000L);
            this.mHandler.removeMessages(45);
            MetricsHandler metricsHandler2 = this.mHandler;
            metricsHandler2.sendMessageDelayed(metricsHandler2.obtainMessage(45), 3000L);
        }
    }

    private boolean isBasementVoipWhiteCellMatch() {
        CellIdentityLte cellIdentityLte;
        CellIdentityNr cellIdentityNr;
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null) {
            int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
            if (isNrNwType(dataNetworkType) && (cellIdentityNr = this.mNrCi) != null) {
                return BASEMENT_VOIP_WHITE_CELL_SET.contains(Long.valueOf(cellIdentityNr.getNci()));
            }
            if (isLteNwType(dataNetworkType) && (cellIdentityLte = this.mLteCi) != null) {
                return BASEMENT_VOIP_WHITE_CELL_SET.contains(Long.valueOf(cellIdentityLte.getCi()));
            }
        }
        return false;
    }

    class SensorStateCallBack implements TelephonySensorManager.SensorStateListener {
        SensorStateCallBack() {
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onSubwayStateChanged(boolean z) {
            if (NetworkMetricsController.this.mCurrentSubwayState != z) {
                NetworkMetricsController.this.mCurrentSubwayState = z;
                if (NetworkMetricsController.this.mCurrentSubwayState) {
                    NetworkMetricsController.this.mHandler.obtainMessage(64).sendToTarget();
                }
            }
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onBasementStateChanged(boolean z) {
            if (z) {
                NetworkMetricsController.this.mHandler.obtainMessage(42).sendToTarget();
            }
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onElevatorStateChanged(boolean z) {
            NetworkMetricsController.this.mHandler.obtainMessage(72, Boolean.valueOf(z)).sendToTarget();
        }
    }

    private void regSensorStateCallBack() {
        TelephonySensorManager telephonySensorManagerInit = TelephonySensorManager.init(this.mContext);
        if (this.mSensorStateCallBack != null || telephonySensorManagerInit == null) {
            return;
        }
        SensorStateCallBack sensorStateCallBack = new SensorStateCallBack();
        this.mSensorStateCallBack = sensorStateCallBack;
        telephonySensorManagerInit.registerSensorStateListener(sensorStateCallBack);
    }

    public void execPowerSaveOptimization(boolean z) {
        log("lastPowerSaveOn=" + this.mIsPowerSaveOn + ", curPowerSaveOn=" + z + ", mDepriorityNr2Lte=" + this.mDepriorityNr2Lte);
        if (this.mIsFeatureEnable && z != this.mIsPowerSaveOn && !z && (!this.mDepriorityNr2Lte || !this.mGame)) {
            this.mDepriorityNr2Lte = false;
            restoreNrIfIndeed(Scene.POWER_SAVE);
        }
        this.mIsPowerSaveOn = z;
    }

    class ThermalSceneListener extends FileObserver {
        public ThermalSceneListener(String str) throws Throwable {
            super(str);
            NetworkMetricsController.this.processThermalSceneOptimize();
        }

        @Override // android.os.FileObserver
        public void onEvent(int i, String str) throws Throwable {
            if (i != 2) {
                return;
            }
            NetworkMetricsController.this.processThermalSceneOptimize();
        }
    }

    private boolean isNormalOosScene(int i) {
        return !this.mIsAirPlaneMode && MiuiPhoneUtils.isSimReady(i);
    }

    private boolean isDepriorityLteScene() {
        boolean z = this.mCurrentHstState;
        boolean z2 = true;
        boolean z3 = isSubwayState() || isSubwaylikeState();
        if (z || z3 || (!isSatifyDepriorityLteInGame() && !this.mIsThermalScene && !this.mIsBuildingMode && !this.mIsPowerSaveOn)) {
            z2 = false;
        }
        log("rst=" + z2 + ",hstState=" + z + ",subwayState=" + z3 + ",mGame=" + this.mGame + ",mIsMobileDataOn=" + this.mIsMobileDataOn + ",mThermal=" + this.mIsThermalScene + ",mIsBuildingMode=" + this.mIsBuildingMode + ",mIsPowerSaveOn=" + this.mIsPowerSaveOn);
        return z2;
    }

    private boolean isSatifyDepriorityLteInGame() {
        boolean z = false;
        boolean z2 = HyperOSCustFeatureResolve.getBoolean("is_support_depriority_lte_in_game", false) || MiuiPhoneUtils.isPlatformDE30() || MiuiPhoneUtils.isPlatformDE50() || MiuiPhoneUtils.isPlatformDE70();
        if (this.mGame && this.mIsMobileDataOn && z2) {
            z = true;
        }
        log("isSatifyDepriorityLteInGame rst=" + z + ", isSupport=" + z2);
        return z;
    }

    /* JADX WARN: Code duplicated, block: B:43:0x0073 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private String readLine(String str) throws Throwable {
        BufferedReader bufferedReader;
        StringBuilder sb;
        BufferedReader bufferedReader2 = null;
        line = null;
        line = null;
        line = null;
        line = null;
        line = null;
        String line = null;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(str), SmartNetworkRelayScore.TRAFFIC_UNIT);
                try {
                    line = bufferedReader.readLine();
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e = e;
                        sb = new StringBuilder("readLine fail:");
                        sb.append(e);
                        log(sb.toString());
                    }
                } catch (IOException e2) {
                    e = e2;
                    log("readLine fail:" + e);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e3) {
                            e = e3;
                            sb = new StringBuilder("readLine fail:");
                            sb.append(e);
                            log(sb.toString());
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                    log("readLine fail:" + e);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e5) {
                            e = e5;
                            sb = new StringBuilder("readLine fail:");
                            sb.append(e);
                            log(sb.toString());
                        }
                    }
                }
            } catch (Throwable th) {
                th = th;
                bufferedReader2 = bufferedReader;
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e6) {
                        log("readLine fail:" + e6);
                    }
                }
                throw th;
            }
        } catch (IOException e7) {
            e = e7;
            bufferedReader = null;
        } catch (Exception e8) {
            e = e8;
            bufferedReader = null;
        } catch (Throwable th2) {
            th = th2;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            throw th;
        }
        return line;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processThermalSceneOptimize() throws Throwable {
        try {
            String line = readLine("/sys/class/thermal/thermal_message/poor_modem_limit");
            if (line != null) {
                checkRestoreNrForThermalScene(Integer.parseInt(line.trim()));
            }
        } catch (Exception unused) {
        }
    }

    private void checkRestoreNrForThermalScene(int i) {
        boolean z = i == 1;
        log("lastThermal=" + this.mIsThermalScene + ",curThermal=" + z + ",mDepriorityNr2Lte=" + this.mDepriorityNr2Lte);
        if (this.mIsFeatureEnable && z != this.mIsThermalScene && !z && this.mDepriorityNr2Lte) {
            this.mDepriorityNr2Lte = false;
            restoreNrIfIndeed(Scene.THERMAL);
        }
        this.mIsThermalScene = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSpeedTrackerListener() {
        NetworkSpeedTracker networkSpeedTracker = NetworkSpeedTracker.getInstance();
        this.mSpeedTracker = networkSpeedTracker;
        if (networkSpeedTracker != null) {
            SpeedListener speedListener = new SpeedListener();
            this.mSpeedListener = speedListener;
            this.mSpeedTracker.registerSpeedListener(speedListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterSpeedTrackerListener() {
        NetworkSpeedTracker networkSpeedTracker = this.mSpeedTracker;
        if (networkSpeedTracker != null) {
            networkSpeedTracker.unregisterSpeedListener(this.mSpeedListener);
            this.mSpeedTracker = null;
        }
    }

    class SpeedListener implements NetworkSpeedTracker.ISpeedTrackerListener {
        SpeedListener() {
        }

        @Override // com.android.phone.utils.NetworkSpeedTracker.ISpeedTrackerListener
        public void onSpeedChanged(long j, long j2, NetworkSpeedTracker.Scene scene) {
            NetworkMetricsController.log("onSpeedChanged ul[" + j + "]Bps,dl[" + j2 + "]Bps,scene[" + scene + "]");
            NetworkMetricsController.this.mHandler.obtainMessage(19, scene).sendToTarget();
        }
    }

    /* JADX INFO: renamed from: com.android.phone.NetworkMetricsController$7, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$com$android$phone$utils$NetworkSpeedTracker$Scene;

        static {
            int[] iArr = new int[NetworkSpeedTracker.Scene.values().length];
            $SwitchMap$com$android$phone$utils$NetworkSpeedTracker$Scene = iArr;
            try {
                iArr[NetworkSpeedTracker.Scene.SCG_SIGNAL_QOS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$phone$utils$NetworkSpeedTracker$Scene[NetworkSpeedTracker.Scene.SPEED_ONLY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$phone$utils$NetworkSpeedTracker$Scene[NetworkSpeedTracker.Scene.WECHAT_VOICE_RATIO_EXCEPTION.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$phone$utils$NetworkSpeedTracker$Scene[NetworkSpeedTracker.Scene.SPEED_LIMIT_CARD_EXCEPTION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$phone$utils$NetworkSpeedTracker$Scene[NetworkSpeedTracker.Scene.SPEED_LIVE_SPEED_EXCEPTION.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$phone$utils$NetworkSpeedTracker$Scene[NetworkSpeedTracker.Scene.WECHAT_VOICE_SPEED_EXCEPTION.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:29:0x007d  */
    public synchronized void processSpeedChange(NetworkSpeedTracker.Scene scene) {
        int i;
        try {
            switch (AnonymousClass7.$SwitchMap$com$android$phone$utils$NetworkSpeedTracker$Scene[scene.ordinal()]) {
                case 1:
                    i = 3000002;
                    break;
                case 2:
                    i = 3000006;
                    break;
                case 3:
                    if ((!isNrPoorOrLtePoor(this.mDdsPhoneId) && !isSubwayState()) || isBadNrCellForC6C7() || isBadLteCellForC6C7()) {
                        i = 0;
                    } else {
                        i = 3000001;
                    }
                    break;
                case 4:
                    if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mRilHook.isSpeedLimit(this.mDdsPhoneId)) {
                        Settings.Global.putInt(this.mContext.getContentResolver(), "speedLimitCardToast" + this.mDdsPhoneId, 1);
                        showSpeedLimitNotification();
                        recordSpeedLimitCard(this.mDdsPhoneId);
                    }
                    i = 0;
                    break;
                case 5:
                    if (!isNrPoorOrLtePoor(this.mDdsPhoneId)) {
                        i = 0;
                    } else {
                        i = 3000004;
                    }
                    break;
                case 6:
                    if (!isNrPoorOrLtePoor(this.mDdsPhoneId)) {
                        i = 0;
                    } else {
                        i = 3000005;
                    }
                    break;
                default:
                    i = 0;
                    break;
            }
            if (i != 0) {
                doRecoveryByStrategy(i);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processModemAiPerceptionChanged(int i, float f, float f2) {
        ModemAiQoeController.ModemAiScene modemAiScene = ModemAiQoeController.ModemAiScene.MOVEMENT_TCPIP_QOE_PREDICTION;
        if (ModemAiQoeController.ModemAiScene.isEqual(modemAiScene, i)) {
            this.mLinkPerceptionResult = f;
        } else if (ModemAiQoeController.ModemAiScene.isEqual(ModemAiQoeController.ModemAiScene.MOVEMENT_MODEM_QOE_PREDICTION, i)) {
            this.mModemPerceptionResult = f;
        }
        if (this.mCurrentHstState && (this.mLinkPerceptionResult > 0.5f || this.mModemPerceptionResult > 0.5f)) {
            if (this.VDBG) {
                log("processModemAiPerceptionChanged send to hyperComm");
            }
            this.mContext.sendBroadcast(new Intent("com.android.phone.intent.action.hstModemAiPerception"));
            return;
        }
        if (f2 == 1.0f) {
            this.mPerceptionResult = (this.mLinkPerceptionResult * 0.3f) + (this.mModemPerceptionResult * 0.7f);
        } else if (f2 == 2.0f) {
            this.mPerceptionResult = (this.mLinkPerceptionResult * 0.1f) + (this.mModemPerceptionResult * 0.9f);
        }
        if (this.VDBG) {
            log("processModemAiPerceptionChanged scene " + i + " appType=" + f2 + " aiPerceptionResult=" + f + " mLinkPerceptionResult=" + this.mLinkPerceptionResult + ",mModemPerceptionResult=" + this.mModemPerceptionResult + ",mPerceptionResult=" + this.mPerceptionResult);
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if ((!MiuiPhoneUtils.isSupportModemAiSignalSupported() || this.mPerceptionResult <= 0.75f) && !(MiuiPhoneUtils.isSupportModemAiV1Supported() && ModemAiQoeController.ModemAiScene.isEqual(modemAiScene, i) && this.mLinkPerceptionResult > 0.75f)) {
            return;
        }
        long j = this.mLastModemAiPeceptionTime;
        if (j != 0 && jCurrentTimeMillis - j < 120000) {
            log("processModemAiPerceptionChanged skip: penalty time active");
        } else {
            doRecoveryByStrategy(3000009);
            this.mLastModemAiPeceptionTime = jCurrentTimeMillis;
        }
    }

    private void showSpeedLimitNotification() {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        notificationManager.createNotificationChannel(new NotificationChannel("speed_limit_sim_rminders", this.mContext.getString(R.string.speed_limit_sim_notification), 4));
        Notification notificationBuild = new Notification.Builder(this.mContext, "speed_limit_sim_rminders").setOngoing(true).setSmallIcon(android.R.drawable.stat_sys_warning).setContentText(this.mContext.getString(R.string.speed_limit_sim_summary)).setContentTitle(this.mContext.getString(R.string.speed_limit_sim_title)).setAutoCancel(true).setChannelId("speed_limit_sim_rminders").build();
        log(" start notification");
        notificationManager.notify(1, notificationBuild);
    }

    private void recordSpeedLimitCard(int i) {
        localLog("isS_L_C", "PhoneID = " + i);
        HashMap map = new HashMap();
        map.put("Network", MiuiPhoneUtils.getOperatorNumeric(MiuiPhoneUtils.getPhone(i)));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_speed_limit_card", map);
    }

    public boolean isNrPoorOrLtePoor(int i) {
        if (this.VDBG) {
            log("isNrPoorOrLtePoor nrrsrp = " + this.mNrRsrp[i] + ",lteRsrp=" + this.mLteRsrp[i]);
        }
        int i2 = this.mNrRsrp[i];
        if (i2 != Integer.MAX_VALUE && i2 <= -105) {
            return true;
        }
        int i3 = this.mLteRsrp[i];
        return i3 != Integer.MAX_VALUE && i3 <= -105;
    }

    private void recordPingPongException(boolean z, boolean z2, int i) {
        HashMap map = new HashMap();
        map.put("isInCall", Boolean.valueOf(z2));
        map.put("isInGame", Boolean.valueOf(z));
        map.put("pingPongSlot", Integer.valueOf(i));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_rat_pingpong_exception", map);
        if (MiuiPhoneUtils.isModemLogUploadSupported()) {
            ((IModemModule) ModuleDelegate.get(IModemModule.class)).startCaptureModemLog("pingPong", "modem_oos_nas");
        }
    }

    private void recordRegNrAfterDeprioritize(int i, boolean z) {
        HashMap map = new HashMap();
        int i2 = i == this.mDdsPhoneId ? 1 : 0;
        map.put("slotId", Integer.valueOf(i));
        map.put("isDds", Integer.valueOf(i2));
        map.put("flag", Integer.valueOf(z ? 1 : 0));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_reg_nr_after_depri", map);
    }

    public static void localLog(String str, String str2) {
        localLog("NetworkMetricsController", str, str2);
    }

    public static void localLog(String str, String str2, String str3) {
        String str4 = str + "." + str2;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(mIsAiInfoSupport ? 1800 : 60));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d(str, str2 + " " + str3);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("NetworkMetricsController dump start------");
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
        printWriter.println("NetworkMetricsController dump end------");
        printWriter.flush();
    }

    private void dfsDeprioritizeOrRestoreSa(int i, Scene scene) {
        if (scene == null) {
            log("dfsDeprioritizeOrRestoreSa sceneType is null");
            return;
        }
        int i2 = 13;
        int i3 = 20;
        if (i == 1) {
            i3 = 13;
            i2 = 20;
        } else if (i != 2) {
            i2 = 0;
            i3 = 0;
        }
        sendEvent(MiSight.constructEvent(915100003, new Object[]{"fromRAT", Integer.valueOf(i2), "toRAT", Integer.valueOf(i3), "causeValue", Integer.valueOf(scene.getValue())}));
    }

    public synchronized void doRecoveryIfNeed(int i, int i2, boolean z, Scene scene) {
        try {
            if (scene == null) {
                log("doRecoveryIfNeed sceneType is null");
                return;
            }
            if (shouldAvoidByScene(scene)) {
                log("doRecoveryIfNeed avoid Scene : " + scene.getValue());
            } else {
                this.mLastSceneType = scene;
                this.mRecovery.doRecoveryIfNeed(i, i2, z, scene);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public boolean shouldAvoidByScene(Scene scene) {
        boolean zShouldAvoidBySceneGl;
        boolean z = true;
        if (!MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            return true;
        }
        if (Build.IS_INTERNATIONAL_BUILD) {
            zShouldAvoidBySceneGl = shouldAvoidBySceneGl(scene);
        } else {
            if (!shouldAvoidBySceneForQcCn(scene) && !shouldAvoidBySceneForMtkCn(scene) && !shouldAvoidBySceneForXringCn(scene)) {
                z = false;
            }
            zShouldAvoidBySceneGl = z;
        }
        log("shouldAvoidByScene" + zShouldAvoidBySceneGl);
        return zShouldAvoidBySceneGl;
    }

    private boolean shouldAvoidBySceneForMtkCn(Scene scene) {
        if (!ModemProvider.IS_MTK) {
            return false;
        }
        if (scene == Scene.LINK_QOS || scene == Scene.VOIP || scene == Scene.PING_PONG_NIGHT || scene == Scene.PING_PONG_COMMON || scene == Scene.DATA_STALL_SIGNALQOS || scene == Scene.SPEED_HIGH_ONLY || scene == Scene.BUILDING_MODE || scene == Scene.SIGNAL_STRENGTH || scene == Scene.SIGNAL_WHITE_LIST_FG || scene == Scene.NRDEREGISTEREDNDDS || scene == Scene.HST_NW_WITHOUT_NR) {
            return true;
        }
        return (scene == Scene.OUT_BASEMENT || scene == Scene.OUT_ELEVATOR) && isCloseForceNrOpt();
    }

    private boolean shouldAvoidBySceneForXringCn(Scene scene) {
        if (!ModemProvider.IS_XRING) {
            return false;
        }
        if (scene == Scene.LINK_QOS || scene == Scene.VOIP || scene == Scene.PING_PONG_NIGHT || scene == Scene.PING_PONG_COMMON || scene == Scene.DATA_STALL_SIGNALQOS || scene == Scene.SPEED_HIGH_ONLY || scene == Scene.BUILDING_MODE || scene == Scene.SIGNAL_STRENGTH || scene == Scene.SIGNAL_WHITE_LIST_FG || scene == Scene.SUBWAY_ABNORMAL_CELL) {
            return true;
        }
        return (scene == Scene.OUT_BASEMENT || scene == Scene.OUT_ELEVATOR) && isCloseForceNrOpt();
    }

    public boolean isCloseForceNrOpt() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null) {
            log("isCloseForceNrOpt: invalid phoneId or serviceState, return true");
            return true;
        }
        if (MiuiPhoneUtils.isMtkForceSearchNrSupported()) {
            log("isCloseForceNrOpt: mtkForceSearchNrSupported, return false");
            return false;
        }
        int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
        boolean zIsLteNwType = isLteNwType(dataNetworkType);
        log("isCloseForceNrOpt: dataType=" + dataNetworkType + " isLte2NrCommand=" + zIsLteNwType);
        return zIsLteNwType;
    }

    private boolean shouldAvoidBySceneForQcCn(Scene scene) {
        if (ModemProvider.IS_QCOM) {
            return scene == Scene.DUMMY || scene == Scene.SPEED_HIGH_ONLY;
        }
        return false;
    }

    private boolean shouldAvoidBySceneGl(Scene scene) {
        return scene == Scene.LINK_QOS || scene == Scene.SPEED_RATE || scene == Scene.PING_PONG_NIGHT || scene == Scene.NRDEREGISTEREDNDDS || scene == Scene.SIGNAL_STRENGTH || scene == Scene.SIGNAL_WHITE_LIST_FG;
    }

    public boolean isSaOffBySpecialNetworkOptimization() {
        return this.mRecovery.isSaOffInRecoveryAction(this.mDdsPhoneId);
    }

    public void setDataStallOptimization(int i, boolean z, Scene scene) {
        SelfStudyControllerBase selfStudyControllerBase;
        log("setDataStallOptimization trafficDirection: " + i + " isForce :" + z + " sceneType :" + scene.getValue() + " mLastDataStallDirection:" + this.mLastDataStallDirection);
        if (!z && this.mLastCellChangeTime != -1 && System.currentTimeMillis() - this.mLastCellChangeTime < MirrorDesktopHelper.TIMEOUT_MILLIS) {
            log("setDataStallOptimization skip : cell changed");
            return;
        }
        if (isSpecialNetworkOptScene(scene) && (this.mCurrentHstState || isSmartDualSimSwitchState())) {
            log("setDataStallOptimization skip : in HstState or TempSmartDualSimSwitch");
            return;
        }
        if (!z && isSpecialNetworkOptScene(scene) && !isSatisfyDataStallSceneForSignal(scene)) {
            log("isSatisfyDataStallSceneForSignal skip : neighbor cell is weak signal or invalid");
            return;
        }
        if (!z && scene == Scene.GAME_RESIDENT_CELL && !isSatisfyDataStallSceneForSignal(scene)) {
            log("GAME_RESIDENT_CELL isSatisfyDataStallSceneForSignal skip : neighbor cell is weak signal or invalid");
            return;
        }
        if (scene == Scene.GAME_RESIDENT_CELL && (selfStudyControllerBase = SelfStudyControllerBase.getInstance()) != null) {
            selfStudyControllerBase.isSetDataStallForGameOpt(true);
        }
        boolean z2 = ModemProvider.IS_MTK && MiuiPhoneUtils.isNetworkQoeSupported() && scene != Scene.CELLCHANGED;
        if (i == 0) {
            this.mLastDataStallDirection = 0;
            this.mNeedMtkDataStallNormalAfterCellChange = z2;
            doRecoveryIfNeed(56, this.mDdsPhoneId, z, scene);
        } else if (i == 1) {
            this.mLastDataStallDirection = 1;
            this.mNeedMtkDataStallNormalAfterCellChange = z2;
            doRecoveryIfNeed(57, this.mDdsPhoneId, z, scene);
        }
    }

    private boolean isSatisfyDataStallSceneForSignal(Scene scene) {
        boolean z;
        boolean z2 = false;
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mServiceState[this.mDdsPhoneId] != null) {
            try {
                int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
                log("isSatisfyDataStallSceneForSignal nrrsrp= " + this.mNrRsrp[this.mDdsPhoneId] + ",lteRsrp= " + this.mLteRsrp[this.mDdsPhoneId] + ",dataType= " + dataNetworkType);
                float[] neighborRsrpRsrqInfo = getNeighborRsrpRsrqInfo(this.mDdsPhoneId, dataNetworkType);
                if (neighborRsrpRsrqInfo == null) {
                    return false;
                }
                if (isLteNwType(dataNetworkType)) {
                    z = inRangeOrUnavailable((int) neighborRsrpRsrqInfo[0], -140, -43) != Integer.MAX_VALUE && inRangeOrUnavailable((int) neighborRsrpRsrqInfo[1], -34, 3) != Integer.MAX_VALUE && neighborRsrpRsrqInfo[0] > -110.0f && neighborRsrpRsrqInfo[1] > -15.0f;
                } else {
                    if (isNrNwType(dataNetworkType)) {
                        int iInRangeOrUnavailable = inRangeOrUnavailable((int) neighborRsrpRsrqInfo[0], -140, -44);
                        int iInRangeOrUnavailable2 = inRangeOrUnavailable((int) neighborRsrpRsrqInfo[1], -43, 20);
                        if (iInRangeOrUnavailable == Integer.MAX_VALUE || iInRangeOrUnavailable2 == Integer.MAX_VALUE || neighborRsrpRsrqInfo[0] <= -108.0f || neighborRsrpRsrqInfo[1] <= -15.0f) {
                        }
                    }
                }
                try {
                    log("isSatisfyDataStallSceneForSignal curNeighborSignal[0]:" + neighborRsrpRsrqInfo[0] + "curNeighborSignal[1]:" + neighborRsrpRsrqInfo[1] + " rst:" + z);
                    return z;
                } catch (Exception e) {
                    z2 = z;
                    e = e;
                    e.printStackTrace();
                    return z2;
                }
            } catch (Exception e2) {
                e = e2;
            }
        }
        return z2;
    }

    private boolean isSpecialNetworkOptScene(Scene scene) {
        return scene == Scene.LINK_QOS || scene == Scene.VOIP || scene == Scene.LIVE || scene == Scene.MODEM_AI_PECEPTION || scene == Scene.CELLCHANGED || scene == Scene.SMART_NETWORK_PECEPTION || scene == Scene.GAME;
    }

    private float[] getNeighborRsrpRsrqInfo(int i, int i2) {
        ByteBuffer nr5gBestNbrMeasInfo;
        try {
            if (isLteNwType(i2)) {
                nr5gBestNbrMeasInfo = this.mRilHook.getLteBestNbrMeasInfo(i);
            } else {
                nr5gBestNbrMeasInfo = isNrNwType(i2) ? this.mRilHook.getNr5gBestNbrMeasInfo(i) : null;
            }
            if (nr5gBestNbrMeasInfo == null) {
                log("getNeighborRsrpRsrqInfo return null");
                return null;
            }
            nr5gBestNbrMeasInfo.order(ByteOrder.nativeOrder());
            byte b = nr5gBestNbrMeasInfo.get();
            log("getNeighborRsrpRsrqInfo version:" + ((int) b) + " remaining:" + nr5gBestNbrMeasInfo.remaining());
            float f = nr5gBestNbrMeasInfo.getFloat();
            log("getNeighborRsrpRsrqInfo curNeighborRsrp:" + f + " remaining:" + nr5gBestNbrMeasInfo.remaining());
            float f2 = nr5gBestNbrMeasInfo.getFloat();
            log("getNeighborRsrpRsrqInfo version:" + ((int) b) + "curNeighborRsrp:" + f + " curNeighborRsrq:" + f2 + " remaining:" + nr5gBestNbrMeasInfo.remaining());
            return new float[]{f, f2};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void resetDataStallOptimization(int i) {
        log("resetDataStallOptimization");
        this.mRecovery.resetDataStallOptimization(i, this.mLastDataStallDirection, Scene.DATA_STALL_RESET);
        this.mLastDataStallDirection = -1;
        this.mNeedMtkDataStallNormalAfterCellChange = false;
    }

    private boolean isDataStallCombineSaOptimization(Scene scene) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mTriggerDataStallTime;
        long j2 = jCurrentTimeMillis - j;
        return (scene == Scene.LIVE || scene == Scene.MODEM_AI_PECEPTION) && j != 0 && j2 > 90000 && j2 < 360000;
    }

    private void setHdrOptimizationForC6C7() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            log("setHdrOptimizationForC6C7 invalid phoneid");
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.mLastHdrCellOptTime > 1800000 && MiuiPhoneUtils.isCellPreferOptSupported() && MiuiPhoneUtils.isSNSV4SupportedByQcom()) {
            CellPreferManager.getInstance().processSetHdrCellInfo("nr,460,0,5807325190,NR5G_BAND_41,504990,866");
            this.mLastHdrCellOptTime = jCurrentTimeMillis;
        }
    }

    private AppType getForegroundAppTypeFromPkg(String str) {
        AppType appType = AppType.NONE;
        if (ArrayUtils.contains(mConvVideoWhiteListDefault, str)) {
            return AppType.CONV_VIDEO;
        }
        if (ArrayUtils.contains(mStreamingVideoWhiteListDefault, str) || (MiuiPhoneUtils.isSupportVideoDataStallOptimization() && ArrayUtils.contains(mShortVideoWhiteListDefault, str))) {
            return AppType.STREAMING_VIDEO;
        }
        if (ArrayUtils.contains(mWebBrowsingWhiteListDefault, str)) {
            return AppType.WEB_BROWSING;
        }
        if (ArrayUtils.contains(mFileTransferWhiteListDefault, str)) {
            return AppType.FILE_TRANSFER;
        }
        return ArrayUtils.contains(mGamingWhiteListDefault, str) ? AppType.TYPE_GAMING : appType;
    }

    private boolean isDdsRoaming() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            return false;
        }
        boolean z = this.mRoamingState[this.mDdsPhoneId] == 1;
        log("isDdsRoaming ddsState=" + this.mRoamingState[this.mDdsPhoneId]);
        return z;
    }

    private void registerRoamingStateCallBack() {
        EventHubManager.getInstance().registerListener("telephony", "roaming_mode_changed", new BaseEvent.IEventListener() { // from class: com.android.phone.NetworkMetricsController$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$registerRoamingStateCallBack$0(baseEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerRoamingStateCallBack$0(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.NetworkEvent) {
            MetricsHandler metricsHandler = this.mHandler;
            metricsHandler.sendMessage(metricsHandler.obtainMessage(65, (TelephonyEvent.NetworkEvent) baseEvent));
        }
    }

    private void initBandData() {
        this.mTddSet.addAll(Arrays.asList(this.mContext.getResources().getStringArray(R.array.band_tdd)));
        this.mFivegLowBandSet.addAll(Arrays.asList(this.mContext.getResources().getStringArray(R.array.fiveg_low_band)));
        log("initBandData");
    }

    private boolean isTddCellByPhoneId(int i) {
        return !this.mTddSet.isEmpty() && this.mTddSet.contains(getBandByPhoneId(i));
    }

    private String getBandByPhoneId(int i) {
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

    private ResultReceiver createDdsAssistantInfoReceiver() {
        return new ResultReceiver(this.mHandler) { // from class: com.android.phone.NetworkMetricsController.6
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                NetworkMetricsController.log("getDdsAssistantInfo result: resultCode=" + i + ", resultData=" + bundle);
                if (i == 0 && bundle != null && bundle.containsKey("dds_assistant_info")) {
                    int[] intArray = bundle.getIntArray("dds_assistant_info");
                    if (intArray == null || intArray.length < 17) {
                        StringBuilder sb = new StringBuilder("getDdsAssistantInfo: invalid cellListInfo, length=");
                        sb.append(intArray != null ? Integer.valueOf(intArray.length) : "null");
                        NetworkMetricsController.log(sb.toString());
                        return;
                    }
                    int i2 = intArray[5];
                    NetworkMetricsController.log("getDdsAssistantInfo: dlBw=" + i2);
                    if (i2 > 20) {
                        return;
                    }
                    NetworkMetricsController.this.doRecoveryForNetworkQoeChanged();
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doRecoveryForNetworkQoeChanged() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mLastRecoveryAction < 0 && isTimeSatisfyForDualSimSwitch(jElapsedRealtime)) {
            SmartDualSimSwitch smartDualSimSwitch = SmartDualSimSwitch.getInstance();
            if (isSatifyDualSimSwitch(smartDualSimSwitch)) {
                localLog("DualSimSwitch", "true");
                smartDualSimSwitch.processStreamDataStall(true);
                this.mLastRecoveryAction = 0;
                this.mLastRecoveryActionTime = jElapsedRealtime;
                this.mLastSmartDualSimTimeInStreamDataStall = jElapsedRealtime;
                this.mActionNetworkScore = this.mLastNetworkScore;
                startStreamOptEffectMonitorTimer();
                return;
            }
        }
        if (this.mLastRecoveryAction < 1 && isTimeSatisfyForDepriorityNr(jElapsedRealtime) && !this.mRecovery.isSaOffInRecoveryAction(this.mDdsPhoneId)) {
            StringBuilder sb = new StringBuilder("s[");
            Scene scene = Scene.STREAM_DATA_STALL;
            sb.append(scene.getValue());
            sb.append("]");
            localLog("DSA", sb.toString());
            doRecoveryIfNeed(53, this.mDdsPhoneId, false, scene);
            this.mLastRecoveryAction = 1;
            this.mLastRecoveryActionTime = jElapsedRealtime;
            this.mLastDepriorityTimeInStreamDataStall = jElapsedRealtime;
            this.mActionNetworkScore = this.mLastNetworkScore;
            startStreamOptEffectMonitorTimer();
            return;
        }
        restoreNrInStreamMediaOptIfNeed();
    }

    private void startStreamOptEffectMonitorTimer() {
        stopStreamOptEffectMonitorTimer();
        this.mHandler.sendEmptyMessageDelayed(46, 30000L);
        this.mIsFivegLBStreamOptEffectMonitor = true;
        log("startStreamOptEffectMonitorTimer");
    }

    private void stopStreamOptEffectMonitorTimer() {
        if (this.mIsFivegLBStreamOptEffectMonitor) {
            this.mHandler.removeMessages(46);
            this.mIsFivegLBStreamOptEffectMonitor = false;
            log("stopStreamOptEffectMonitorTimer");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordFivegLBStreamOpt(int i, int i2) {
        HashMap map = new HashMap();
        map.put("opt_code", Integer.valueOf(this.mLastRecoveryAction));
        map.put("before_s", Integer.valueOf(i));
        map.put("after_s", Integer.valueOf(i2));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_fiveg_lb_stream_opt", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreNrInStreamMediaOptIfNeed() {
        if (this.mLastRecoveryAction == 1) {
            restoreNrIfIndeed(Scene.STREAM_DATA_STALL);
            this.mLastRecoveryAction = -1;
            this.mLastRecoveryActionTime = SystemClock.elapsedRealtime();
        }
    }

    private boolean isTimeSatisfyForDualSimSwitch(long j) {
        if (this.mLastSmartDualSimTimeInStreamDataStall == -1 || j - this.mLastSmartDualSimTimeInStreamDataStall >= this.mSameRecoveryActionMinInterval) {
            return true;
        }
        log("isTimeSatisfyForDualSimSwitch false");
        return false;
    }

    private boolean isTimeSatisfyForDepriorityNr(long j) {
        if (this.mLastDepriorityTimeInStreamDataStall == -1 || j - this.mLastDepriorityTimeInStreamDataStall >= this.mSameRecoveryActionMinInterval) {
            return true;
        }
        log("isTimeSatisfyForDepriorityNr false");
        return false;
    }

    private boolean isSatifyDualSimSwitch(SmartDualSimSwitch smartDualSimSwitch) {
        boolean z = false;
        if (smartDualSimSwitch == null) {
            return false;
        }
        boolean zIsSmartDualSimActive = isSmartDualSimActive();
        boolean zIsSameServicePrviderforDualSim = isSameServicePrviderforDualSim();
        boolean zIsDualDataConnected = MiuiPhoneUtils.isDualDataConnected();
        boolean zIsGoodSignalForStreamDataStall = smartDualSimSwitch.isGoodSignalForStreamDataStall(getAnotherPhoneId(this.mDdsPhoneId));
        boolean zIsTddCellByPhoneId = isTddCellByPhoneId(getAnotherPhoneId(this.mDdsPhoneId));
        if (zIsSmartDualSimActive && !zIsSameServicePrviderforDualSim && !zIsDualDataConnected && zIsGoodSignalForStreamDataStall && zIsTddCellByPhoneId) {
            z = true;
        }
        if (!z) {
            log("isSmartDualSimActive=" + zIsSmartDualSimActive + ", isSameServicePrvider=" + zIsSameServicePrviderforDualSim + ", isDualData=" + zIsDualDataConnected + ", isGoodSignal=" + zIsGoodSignalForStreamDataStall + ", isTddBand=" + zIsTddCellByPhoneId);
        }
        return z;
    }

    public void onNetworkQoeChanged(int i, int i2, int i3, int i4, int i5) {
        SmartDualSimSwitch smartDualSimSwitch;
        if (this.isSupportLBopt && this.mIsFivegLBStreamOptCloudOn) {
            if (!isValidQoeChanged(i)) {
                stopStreamOptEffectMonitorTimer();
                return;
            }
            if (i5 == -1) {
                return;
            }
            recordOptResultIfNeed(i5);
            this.mLastNetworkScore = i5;
            if (i5 > 80 || !isRecoveryActionTimeSatisfy()) {
                return;
            }
            if (isSmartDualSimSwitchState() && (smartDualSimSwitch = SmartDualSimSwitch.getInstance()) != null) {
                localLog("DualSimSwitch", "false");
                smartDualSimSwitch.processStreamDataStall(false);
                this.mLastRecoveryActionTime = SystemClock.elapsedRealtime();
            } else if (isNwTypeAndBandSatisfy()) {
                getDdsAssistantInfo();
            }
        }
    }

    private void recordOptResultIfNeed(int i) {
        if (!this.mIsFivegLBStreamOptEffectMonitor || this.mLastRecoveryActionTime == -1) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime() - this.mLastRecoveryActionTime;
        if (jElapsedRealtime <= 10000 || jElapsedRealtime >= 30000) {
            return;
        }
        recordFivegLBStreamOpt(this.mActionNetworkScore, i);
        stopStreamOptEffectMonitorTimer();
    }

    private boolean isNwTypeAndBandSatisfy() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null) {
            return false;
        }
        String bandByPhoneId = getBandByPhoneId(this.mDdsPhoneId);
        int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
        log("isNwTypeAndBandSatisfy: dataType=" + dataNetworkType + ", band=" + bandByPhoneId);
        if (!isNrNwType(dataNetworkType)) {
            log("isNwTypeAndBandSatisfy: skip for rat");
            restoreNrInStreamMediaOptIfNeed();
            return false;
        }
        if (isNrNwType(dataNetworkType) && this.mLastRecoveryAction == 1) {
            this.mLastRecoveryAction = -1;
            this.mLastRecoveryActionTime = SystemClock.elapsedRealtime();
        }
        if (this.mFivegLowBandSet.contains(bandByPhoneId)) {
            return true;
        }
        log("isNwTypeAndBandSatisfy: skip for band");
        return false;
    }

    private boolean isRecoveryActionTimeSatisfy() {
        if (this.mLastRecoveryActionTime == -1 || SystemClock.elapsedRealtime() - this.mLastRecoveryActionTime >= 30000) {
            return true;
        }
        log("isRecoveryActionTimeSatisfy false");
        return false;
    }

    private boolean isValidQoeChanged(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("isValidQoeChanged: skip for invalid slotId = " + i);
            return false;
        }
        if (!this.mIsScreenOn) {
            log("isValidQoeChanged: skip for screen off");
            return false;
        }
        if (MiuiPhoneUtils.isDuringCall()) {
            log("isValidQoeChanged: skip for during call");
            return false;
        }
        if (this.mIsWifiDataOn || !this.mIsMobileDataOn) {
            log("isValidQoeChanged: skip for networkstate, mIsWifiDataOn=" + this.mIsWifiDataOn + ", mIsMobileDataOn=" + this.mIsMobileDataOn);
            return false;
        }
        String topAppName = getTopAppName();
        log("isValidQoeChanged: topAppName=" + topAppName);
        return isFivegLBStreamMediaWhiteList(topAppName);
    }

    private void getDdsAssistantInfo() {
        log("getDdsAssistantInfo");
        XmTelephonyManagerInner.getDefault().getDdsAssistantInfo(this.mDdsAssistantInfoReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSubwayAbnormalCellMatch() {
        return SignalMapSubwayController.getInstance().isSubwayAbnormalCellMatch();
    }

    public boolean isSubwayState() {
        return SignalMapSubwayController.getInstance().getCurrentSubwayState();
    }

    private boolean isSubwaylikeState() {
        return NetworkModeRecognition.getInstance().isMovelikeState();
    }

    public int getDdsLteNrRsrp() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mServiceState[this.mDdsPhoneId] == null) {
            log("getLteNrRsrp invalid phoneid");
            return Integer.MAX_VALUE;
        }
        int dataNetworkType = this.mServiceState[this.mDdsPhoneId].getDataNetworkType();
        if (isNrNwType(dataNetworkType)) {
            return this.mNrRsrp[this.mDdsPhoneId];
        }
        if (isLteNwType(dataNetworkType)) {
            return this.mLteRsrp[this.mDdsPhoneId];
        }
        return Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getHyperEngineGameSwitch() {
        Context context = this.mContext;
        if (context == null) {
            return false;
        }
        boolean hyperEngineGameSwitchByCloud = FiveGManagerBase.getHyperEngineGameSwitchByCloud(context);
        log("getHyperEngineGameSwitch = " + hyperEngineGameSwitchByCloud);
        return hyperEngineGameSwitchByCloud;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getHyperEngineHstSwitch() {
        Context context = this.mContext;
        if (context == null) {
            return false;
        }
        boolean hyperEngineHstSwitchByCloud = FiveGManagerBase.getHyperEngineHstSwitchByCloud(context);
        log("getHyperEngineHstSwitch = " + hyperEngineHstSwitchByCloud);
        return hyperEngineHstSwitchByCloud;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getHyperEngineSmartHoSwitch() {
        Context context = this.mContext;
        if (context == null) {
            return false;
        }
        boolean hyperEngineSmartHoSwitchByCloud = FiveGManagerBase.getHyperEngineSmartHoSwitchByCloud(context);
        log("getHyperEngineSmartHoSwitch = " + hyperEngineSmartHoSwitchByCloud);
        return hyperEngineSmartHoSwitchByCloud;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getFivegLBStreamOpt() {
        Context context = this.mContext;
        if (context == null) {
            return false;
        }
        boolean fivegLBStreamOptByCloud = FiveGManagerBase.getFivegLBStreamOptByCloud(context);
        log("getFivegLBStreamOpt = " + fivegLBStreamOptByCloud);
        return fivegLBStreamOptByCloud;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("NetworkMetricsController", str);
    }
}
