package com.android.phone.performance;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.phone.CloudCellDbHelper;
import com.android.phone.FiveGCloudController;
import com.android.phone.FiveGCloudUtils;
import com.android.phone.ManualSubCellInfo;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.NetworkRecoveryStrategyBase;
import com.android.phone.OffsetCell;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.SubCellInfo;
import com.android.phone.TelephonySensorManager;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.ModemEvent;
import com.android.phone.module.IModemModule;
import com.android.phone.module.INetworkModule;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiAppStateMonitor;
import com.android.phone.utils.VideoPreloadScene;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.ot.pubsub.b.a;
import com.ot.pubsub.util.v;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import com.xiaomi.telephony.XmTelephonyManagerInner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.IActivityChangeListener;
import miui.process.ProcessManager;
import miui.provider.ExtraCalendarContracts;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;
import miui.telephony.PhoneDebug;
import miui.telephony.SubscriptionManagerEx;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public abstract class SelfStudyControllerBase extends StateMachine {
    protected static List SHORT_VIDEO_PACKAGES = null;
    public static boolean isBeginCollectCellData = false;
    public static volatile boolean isCollectingBadCell = false;
    protected static int mDefaultRealTimeOptimizationStatus = 0;
    protected static volatile SelfStudyControllerBase mInstance = null;
    private static boolean mIsNeedUpdateWhiteList = false;
    public final int CELL_UNIT;
    public final List GAME_WHITELIST_PACKAGES;
    private final Set LAB_MCCMNC_SET;
    private final String[] LIVE_STREAMING_PKGS;
    public final int MAX_CELL_COUNT_COMPARE;
    private final String XIAOMI_MARKET_PACKAGES;
    private PendingIntent[] alarmPendingIntents;
    public List allCellRouteList;
    public Set badCellRouteIds;
    public int cellOffset;
    public List cellTempRouteInfoList;
    public CellUnit cellUnitScore;
    public boolean cloudCallMessageOn;
    public boolean cloudGameResidentOn;
    public boolean cloudLowPowerOn;
    public boolean cloudStreamMediaOn;
    public boolean cloudVideoOn;
    public boolean cloudWechatOn;
    public int dataType;
    public SelfStudyDatabaseHelper dbHelper;
    public int[] durationStatistic;
    public int[] gameStatistic;
    public String[] globalIds;
    public boolean hasBeginRecordMorning;
    public boolean hasBeginRecordingCellId;
    public int hasRecordCellCount;
    public boolean hasRegistStudyCallback;
    public boolean hasRegisterBadCellCallback;
    public boolean hasRegisterSampleCallBack;
    public boolean hasRegisterTelephonyCallback;
    public boolean hasSendColdDlDataStallAction;
    public boolean hasSendDataStallAction;
    public boolean hasSendDlDataStallAction;
    public boolean hasSendDlDataStallActionForLongTime;
    public boolean hasSendSbpAction;
    public boolean hasSetA2;
    public boolean hasSetPreDl;
    protected boolean haveStartedCollectBadCellOnEveningPeak;
    protected boolean haveStartedCollectBadCellOnMorningPeak;
    private boolean[] isCellActionOn;
    public boolean isCellUpdating;
    public boolean isEndcOrNr5G;
    public boolean isGameAiLag;
    public boolean isGameAiOpt;
    protected boolean isGameDebug;
    private boolean isGameSetDataStall;
    private boolean isGameSetOffsetFromModem;
    public boolean isInGame;
    private boolean isNeedDelayStudyByDataConnection;
    public boolean isSame;
    public boolean isSampling;
    public boolean isSupportCellAction;
    public boolean isSupportPredictLoad;
    public boolean isVideoFront;
    public boolean isVideoOpt;
    public boolean isWechatOpt;
    public boolean lastCellMatch;
    public ArrayList lastColdLteStreamModes;
    public ArrayList lastColdNrStreamModes;
    public ArrayList lastLongLteStreamModes;
    public ArrayList lastLongNrStreamModes;
    public HashSet lastLteModes;
    public ArrayList lastLteStreamModes;
    public HashSet lastNrModes;
    public ArrayList lastNrStreamModes;
    public int lastQuality;
    private IActivityChangeListener.Stub mActivityChangeListener;
    protected List mAiGameOffsetCells;
    protected AlarmManager mAlarmManager;
    private HashSet mAppWhitelist;
    public CellCallBack mBadCellCallBack_V2;
    public List mBadCellListInfos;
    public long mBeginDay;
    private AtomicBoolean mBootHandled;
    protected BroadcastReceiver mBroadcastReceiver;
    private final SimpleDateFormat mCachedTimeFormat;
    public long mCellTime;
    private ContentObserver mCellularNetworkStatusObserver;
    public volatile boolean mCellularNetworkSwitchOn;
    public CloudCellDbHelper mCloudCellDbHelper;
    private String[] mCloudLiveWhiteList;
    private String[] mCloudShortWhiteList;
    public ConnectivityManager mCm;
    public int mCollectActionType;
    private ContentResolver mContentResolver;
    protected Context mContext;
    private int mCurrentDay;
    protected String[] mCurrentRouteCellIds;
    private final CurrentTopAppObserver mCurrentTopAppObserver;
    private long mDataTypeStartTime;
    public int mDdsPhoneId;
    public List mExcepitonCells;
    protected boolean mFeatureEnable;
    private long mFetchTime;
    protected FiveGCloudController mFiveGCloudController;
    public long mGameDataStallTime;
    private int[] mGameDelayLevelCount;
    private final List mGameDelayListeners;
    public int mGameLastDelayLevel;
    private boolean mGameNetworkSwitchStatus;
    protected List mGameOffsetCells;
    protected ConcurrentHashMap mGameStats;
    private ContentObserver mGameStatusObserver;
    private HashSet mGameWhiteListAppUids;
    public String mGlobalCurrentCellId;
    private boolean mHasNotify3rdAppOpt;
    public boolean mHasRegisterUserCallback;
    protected HydraSceneListener mHydraSceneListener;
    public volatile boolean mIsCompany;
    protected boolean mIsDataStallApp;
    public boolean mIsDdsChange;
    protected boolean mIsDriveStateIn;
    public volatile boolean mIsHome;
    protected boolean mIsHstStateIn;
    protected boolean mIsIgnoreGameStuck;
    public boolean mIsInWeChatVoip;
    protected boolean mIsMobileDataOn;
    public boolean mIsModForDouYin;
    public boolean mIsModForDouYinForLongTime;
    public boolean mIsNeedRecordForInHome;
    public boolean mIsNeedResetDl;
    public boolean mIsNeedResetDlForLongTime;
    public boolean mIsPeak;
    public boolean mIsProcessedWechatEnter;
    public boolean mIsResidentCell;
    protected boolean mIsResidentCellsOptimizationSupported;
    public boolean mIsScreenOn;
    public boolean mIsStreamStall;
    protected boolean mIsStudyingCompanyResidentCells;
    protected boolean mIsStudyingHomeResidentCells;
    protected boolean mIsStudyingLowPowerModeCells;
    protected boolean mIsStudyingPingpongCells;
    public volatile boolean mIsSubway;
    protected boolean mIsSubwayStateIn;
    protected boolean mIsSupportLowPowerModeOptimization;
    protected boolean mIsSupportMtkVideoSbpOptimization;
    protected boolean mIsWorkDay;
    public int mLastDataType;
    public long mLastDownRatTimeForHome;
    public long mLastStartDataStallTime;
    public long mLastStopCollectTime;
    public long mLastStopDataStallTime;
    public int mLastToModemDataType;
    protected List mLiveStreamingOffsetCells;
    private LocationManager mLocationManager;
    public final Object mLock;
    public boolean mManualSubCellInfoByGci1;
    private MiuiAppStateMonitor mMiuiAppStateMonitor;
    private int[] mModelCfgData;
    private ResultReceiver mModelCfgResultReceiver;
    private ResultReceiver mModelEnableResultReceiver;
    protected NeighborCellManager mNeighborCellManager;
    private int mNetId;
    public int mNotifyModemDdsId;
    public int mNotifySceneType;
    private PackageManager mPackageManager;
    private volatile boolean mPendingGameOptAfterNeighborCheck;
    protected SelfStudyPreferenceUtils mPreferenceUtils;
    private State mRecognizeBeginCellState;
    protected List mResidentCellIds;
    private final ResultReceiver mResultReceiver;
    public ArrayList mSafDataList;
    public List mSampingCellInfoList;
    Map mSampingCellMap;
    private BroadcastReceiver mSelfStudyAlarmReceiver;
    protected SelfStudyCloudChangeListener mSelfStudyCloudChangeListener;
    private SelfStudySensorStateListener mSelfStudySensorStateListener;
    private HashSet mShortedVideoAppUids;
    public ArrayList mStallDetailsList;
    public long mStallDurTime;
    public long mStartStallTime;
    public int[] mStaticState;
    public long mStopStallTime;
    public boolean mStreamMediaOptimizationRst;
    protected ConcurrentHashMap mStreamMediaStats;
    private State mStudyState;
    public SubCellInfo mSubCellInfo;
    public volatile String mSubwayName;
    public CellCallBack mTelephonyCallBack_V2;
    private TelephonyCallBack mTelephonyCommonCallBack;
    public Timer mTimer;
    private long mTrigged3rdTime;
    public TelephonyStatAdapter.ICallBack mUserCallBack;
    private boolean mUserSettingOn;
    private State mUsingState;
    private ConcurrentHashMap mVideoAndGameAiStatsList;
    private long mVideoStallStartTime;
    protected ConcurrentHashMap mVideoStats;
    private HashSet mWebPageShortedVideoAppUids;
    protected List mWechatVideoOffsetCells;
    private HashSet mWhitelistAppPkgs;
    protected Handler mWorkHandler;
    private HandlerThread mWorkThread;
    private int mXiaomiMarketAppUid;
    public int max_lte_offset;
    public int max_nr_offset;
    public int routeIndex;
    private PendingIntent startListenScreenStatusPendingIntent;
    private PendingIntent stopListenScreenStatusPendingIntent;
    public CellCallBack studyCallBack_V2;
    private final String[] studyResidentCellsAlarmAction;
    private final int[] studyResidentCellsAlarmTime;
    public TelephonyManager tmManager;
    private static final HashMap sLocalLogs = new HashMap();
    public static final int MTK_DATA_OPT_VERSION = HyperOSCustFeatureResolve.getInt("config_mtk_data_optimization_version", 0);
    protected static boolean mEnterGameOptimizer = false;
    protected static boolean mEnterGameAiOptimizer = false;
    protected static final List COMMON_GAME_DELAY_THRESHOLDS_PACKAGES = Arrays.asList("com.tencent.tmgp.sgame");
    protected static final int[] sCommonGameDelayThresholds = {100, 200, MiRilHook.HOOK_ID_START_URC_BY_AT};
    protected static final int[] sLolmGameDelayThresholds = {80, 200, MiRilHook.HOOK_ID_START_URC_BY_AT};
    protected static final int[] sJkchessGameDelayThresholds = {100, MiRilHook.HOOK_ID_START_URC_BY_AT, ResponseObject.RELAY_FAILED_CODE};
    protected static final int[] sDfmGameDelayThresholds = {125, 250, 360};
    public static volatile boolean isStudying = false;
    public static volatile boolean isPredicting = false;
    public static int mRatChangeCount = 0;
    public static int mRouteConut = 0;
    public static volatile boolean isUsing = false;
    public static final List STATIC_GAME_CELL_WHITELIST_PACKAGES = MiuiPhoneUtils.getFeatureConfig("config_static_game_cell_delay_whitelist_packages");
    public static final List STATIC_GAME_AI_CELL_NO_DELAY_WHITELIST_PACKAGES = MiuiPhoneUtils.getFeatureConfig("config_static_game_ai_cell_no_delay_whitelist_packages");
    public static final List INVALID_SIM_OPT_WHITELIST_PACKAGES = MiuiPhoneUtils.getFeatureConfig("config_invalid_sim_opt_whitelist_packages");
    public static final String[] CONGESTION_LIVE_STREAM_WHITE_LIST_DEFAULT = {"com.duowan.kiwi.liveroom.ChannelPage", "com.ss.android.ugc.aweme.live.LivePlayActivity", "com.kuaishou.live.core.basic.activity.LiveSlideActivity"};
    public static final String[] CONGESTION_SHORT_VIDEO_WHITE_LIST_DEFAULT = {"com.ss.android.ugc.aweme.main.MainActivity", "com.ss.android.ugc.aweme.splash.SplashActivity", "com.ss.android.ugc.aweme.detail.ui.DetailActivity", "com.ss.android.ugc.aweme.detail.ultra.ui.UltraDetailActivity", "com.yxcorp.gifshow.HomeActivity", "com.yxcorp.gifshow.detail.PhotoDetailActivity", "com.bilibili.video.story.StoryVideoActivity", "tv.danmaku.bili.ui.video.VideoDetailsActivity", "com.bilibili.bangumi.ui.page.detail.BangumiDetailActivityV3", "tv.danmaku.bili.MainActivityV2"};
    protected static int encryptedKey = -1;
    public static HashSet cbs = new HashSet();

    public interface CellCallBack {
        void onCellLocationChanged(int i, CellUnit cellUnit);
    }

    public class CellRouteInfo {
        public long cellId = 0;
        public int cellRat = 0;
        public int cellPci = 0;
        public int cellFreq = 0;
        public int action = 0;
    }

    public class CellUnit {
        public int cellFreq;
        public long cellId;
        public int cellPci;
        public int socre;
    }

    public interface GameDelayListener {
        void onGameDelayChanged(boolean z);
    }

    public void beginStreamDataStall(int i, int i2) {
    }

    public abstract void checkLevelOverTime(int i);

    public abstract void checkLteAfterDisableSa();

    protected abstract void delayProcessWechatEnter();

    public boolean enableNr(boolean z) {
        return false;
    }

    public void getGameAiLagTime(String str, String str2) {
    }

    public String getGameCellResidenceAndLagTime() {
        return null;
    }

    protected void getMdWechatStallInfo() {
    }

    public abstract void handleGameStall();

    protected abstract void handlePlatformDependentMessage(Message message);

    protected boolean isCellularNetworkSwitchOn() {
        return false;
    }

    public abstract boolean isGameResidentLag();

    public boolean isInAICompanyScene() {
        return false;
    }

    public boolean isInAIHomeScene() {
        return false;
    }

    public boolean isInAISubWayScene() {
        return false;
    }

    public boolean isLteNwType(int i) {
        return i == 13 || i == 19;
    }

    public boolean isNrNwType(int i) {
        return i == 20 || i == 30;
    }

    protected boolean isValidSignalValue(int i) {
        return i != Integer.MAX_VALUE;
    }

    protected void notifyCloudDataChanged(MiuiSettings.SettingsCloudData.CloudData cloudData) {
    }

    public boolean performActionForVonrOpt(String str, int i) {
        return false;
    }

    protected abstract void processCallStateChange(int i, int i2);

    protected abstract void processCellLocationChanged(CellLocation cellLocation, int i);

    public abstract void processDataTypeChanged(int i);

    protected void processDefaultDataSlotChange() {
    }

    protected void processGameCellLocationChanged(CellLocation cellLocation, int i) {
    }

    protected void processMobileDataStateChange(boolean z) {
    }

    public void processStartDataStall(int i) {
    }

    public abstract void processStopDataStall();

    public void processVideoQualityChanged(int i) {
    }

    protected void processWechatEnter() {
    }

    protected abstract void processWechatExit();

    public abstract int queryResidentCellSenceHomeOrCompany();

    public void reSetBaredCells() {
    }

    protected abstract void regUnregOtherReceiver(boolean z);

    public abstract void registerGameBoosterGameDelayListener();

    public abstract void resetGameOffsetOptimization();

    protected void resetLowPowerOpt(boolean z) {
    }

    public abstract void resetResidentGameCellOptimization(boolean z);

    protected void resetSmartNetworkBadCellAction() {
    }

    public abstract void sendBadCellToModem(List list);

    protected void sendDdsToModem(int i) {
    }

    protected abstract void sendSelfStudyRequest(int i, int i2, byte[] bArr);

    public abstract void sendSelfStudyRequest(int i, int i2, int[] iArr);

    protected void startCollectMultiRouteAndBeginPredict() {
    }

    protected void startPingpongStudy() {
    }

    protected abstract void startStudyCompanyResidentCells();

    protected abstract void startStudyHomeResidentCells();

    protected void startStudyLowPowerModeCells() {
    }

    protected void stopCollectMultiRouteAndPredict(boolean z) {
    }

    protected void stopPingpongStudy() {
    }

    protected void stopStudyLowPowerModeCells() {
    }

    protected abstract void stopStudyResidentCells(int i);

    protected void tryDataStallOptimization(boolean z, int i, int i2, String str) {
    }

    public abstract void unRegisterGameBoosterGameDelayListener();

    protected void wechatOptTryRecovery() {
    }

    public static SelfStudyControllerBase getInstance() {
        return mInstance;
    }

    protected SelfStudyControllerBase() {
        super("SelfStudyController");
        this.mIsDataStallApp = false;
        this.mCollectActionType = -1;
        this.mIsStreamStall = false;
        this.hasSendDlDataStallAction = false;
        this.hasSendColdDlDataStallAction = false;
        this.hasSendDlDataStallActionForLongTime = false;
        this.mLastStopCollectTime = 0L;
        this.mStallDurTime = 0L;
        this.mIsResidentCell = false;
        this.mIsSubway = false;
        this.mSubwayName = "";
        this.mIsHome = false;
        this.mIsCompany = false;
        this.mLastToModemDataType = 0;
        this.mIsModForDouYin = false;
        this.mIsNeedResetDl = false;
        this.mIsModForDouYinForLongTime = false;
        this.mIsNeedResetDlForLongTime = false;
        this.mLastDataType = 0;
        this.mDdsPhoneId = -1;
        this.mIsDdsChange = false;
        this.mNotifySceneType = 0;
        this.mIsPeak = false;
        this.mStreamMediaOptimizationRst = true;
        this.mIsNeedRecordForInHome = true;
        this.mLastDownRatTimeForHome = -1L;
        this.cloudWechatOn = true;
        this.cloudVideoOn = true;
        this.cloudStreamMediaOn = true;
        this.cloudGameResidentOn = true;
        this.mCloudShortWhiteList = new String[0];
        this.mCloudLiveWhiteList = new String[0];
        this.cloudCallMessageOn = true;
        this.cloudLowPowerOn = true;
        this.cellOffset = -2;
        this.max_lte_offset = -9;
        this.max_nr_offset = -6;
        this.mManualSubCellInfoByGci1 = true;
        this.mCachedTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        this.mGameDataStallTime = 0L;
        this.mTrigged3rdTime = 0L;
        this.mHasNotify3rdAppOpt = false;
        this.mBootHandled = new AtomicBoolean(false);
        this.mGameNetworkSwitchStatus = true;
        this.mPendingGameOptAfterNeighborCheck = false;
        this.mCellularNetworkSwitchOn = true;
        this.isSampling = false;
        this.MAX_CELL_COUNT_COMPARE = 3;
        this.hasSetA2 = false;
        this.isEndcOrNr5G = false;
        this.mExcepitonCells = new ArrayList();
        this.routeIndex = 0;
        this.mGameLastDelayLevel = 0;
        this.isVideoOpt = false;
        this.isWechatOpt = false;
        this.isGameAiOpt = false;
        this.isGameAiLag = false;
        this.isGameSetOffsetFromModem = false;
        this.isGameSetDataStall = false;
        this.isSame = false;
        this.CELL_UNIT = 5;
        this.mNotifyModemDdsId = -1;
        this.LIVE_STREAMING_PKGS = new String[]{"com.duowan.kiwi", "air.tv.douyu.android"};
        this.XIAOMI_MARKET_PACKAGES = "com.xiaomi.market";
        this.mXiaomiMarketAppUid = -1;
        this.GAME_WHITELIST_PACKAGES = Arrays.asList("com.miHoYo.Yuanshen", "com.miHoYo.ys.mi", "com.netease.party", "com.netease.party.mi", "com.netease.dwrg.mi", "com.netease.dwrg", "com.tencent.tmgp.sgame", "com.tencent.jkchess");
        this.LAB_MCCMNC_SET = new HashSet(Arrays.asList("46045"));
        this.mCurrentTopAppObserver = new CurrentTopAppObserver();
        this.hasSetPreDl = false;
        this.isInGame = false;
        this.mTelephonyCommonCallBack = null;
        this.mTimer = null;
        this.mIsScreenOn = false;
        this.hasRegisterTelephonyCallback = false;
        this.hasRecordCellCount = 0;
        this.lastCellMatch = false;
        this.isNeedDelayStudyByDataConnection = false;
        this.hasBeginRecordingCellId = false;
        this.hasRegistStudyCallback = false;
        this.mIsMobileDataOn = false;
        this.mNetId = -1;
        this.mCellTime = 0L;
        this.mHasRegisterUserCallback = false;
        this.mCurrentDay = -1;
        this.hasRegisterBadCellCallback = false;
        this.hasBeginRecordMorning = false;
        this.mBeginDay = -1L;
        this.isCellUpdating = false;
        this.hasRegisterSampleCallBack = false;
        this.mLock = new Object();
        this.mGlobalCurrentCellId = "";
        this.mGameDelayListeners = new ArrayList();
        this.mCurrentRouteCellIds = new String[3];
        this.mLastStartDataStallTime = -1L;
        this.mLastStopDataStallTime = -1L;
        this.hasSendDataStallAction = false;
        this.hasSendSbpAction = false;
        this.isVideoFront = false;
        this.lastQuality = -1;
        this.mIsProcessedWechatEnter = false;
        this.studyResidentCellsAlarmAction = new String[]{"start_study_resident_company_cells_action", "start_study_resident_home_cells_action", "stop_study_resident_company_cells_action", "stop_study_resident_home_cells_action"};
        this.studyResidentCellsAlarmTime = new int[]{10, 22, 17, 5};
        this.isGameDebug = false;
        this.mActivityChangeListener = new IActivityChangeListener.Stub() { // from class: com.android.phone.performance.SelfStudyControllerBase.4
            public void onActivityChanged(ComponentName componentName, ComponentName componentName2) {
                boolean z;
                boolean z2;
                if (componentName != null && componentName2 != null) {
                    SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase.mIsMobileDataOn || selfStudyControllerBase.mLastToModemDataType != 0) {
                        String string = componentName2.toString();
                        String string2 = componentName.toString();
                        SelfStudyControllerBase.this.logV("onActivityChanged " + string2 + "->" + string + ",dataType:" + SelfStudyControllerBase.this.dataType);
                        SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                        int i = selfStudyControllerBase2.dataType;
                        String[] liveWhiteList = selfStudyControllerBase2.getLiveWhiteList();
                        int length = liveWhiteList.length;
                        int i2 = 0;
                        while (true) {
                            z = true;
                            if (i2 >= length) {
                                z2 = false;
                                break;
                            }
                            String str = liveWhiteList[i2];
                            if (!TextUtils.isEmpty(string) && string.contains(str)) {
                                SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                                selfStudyControllerBase3.dataType = 2;
                                selfStudyControllerBase3.logV("onActivityChanged in live list.");
                                z2 = true;
                                break;
                            }
                            i2++;
                        }
                        String[] shortWhiteList = SelfStudyControllerBase.this.getShortWhiteList();
                        int length2 = shortWhiteList.length;
                        int i3 = 0;
                        while (true) {
                            if (i3 >= length2) {
                                z = false;
                                break;
                            }
                            String str2 = shortWhiteList[i3];
                            if (!TextUtils.isEmpty(string) && string.contains(str2)) {
                                SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                                selfStudyControllerBase4.dataType = 4;
                                selfStudyControllerBase4.logV("onActivityChanged in short list.");
                                break;
                            }
                            i3++;
                        }
                        if (!z2) {
                            SelfStudyControllerBase selfStudyControllerBase5 = SelfStudyControllerBase.this;
                            if (selfStudyControllerBase5.dataType == 2) {
                                selfStudyControllerBase5.dataType = 0;
                                selfStudyControllerBase5.logV("onActivityChanged not in live list.");
                            }
                        }
                        if (!z) {
                            SelfStudyControllerBase selfStudyControllerBase6 = SelfStudyControllerBase.this;
                            if (selfStudyControllerBase6.dataType == 4) {
                                selfStudyControllerBase6.dataType = 0;
                                selfStudyControllerBase6.logV("onActivityChanged not in short list.");
                            }
                        }
                        SelfStudyControllerBase selfStudyControllerBase7 = SelfStudyControllerBase.this;
                        int i4 = selfStudyControllerBase7.dataType;
                        if (i4 == 2 || i4 == 4) {
                            selfStudyControllerBase7.mIsResidentCell = selfStudyControllerBase7.isResidentCellScene();
                            SelfStudyControllerBase selfStudyControllerBase8 = SelfStudyControllerBase.this;
                            selfStudyControllerBase8.mIsSubway = selfStudyControllerBase8.isSubwayScene();
                            SelfStudyControllerBase selfStudyControllerBase9 = SelfStudyControllerBase.this;
                            selfStudyControllerBase9.initDataOptimizationState(selfStudyControllerBase9.mIsResidentCell);
                        }
                        SelfStudyControllerBase.this.processStreamBenefitStatistic();
                        SelfStudyControllerBase.this.logV("onActivityChanged tempDataType:" + i + " , mIsResidentCell:" + SelfStudyControllerBase.this.mIsResidentCell + " , mIsSubway:" + SelfStudyControllerBase.this.mIsSubway + " , dataType:" + SelfStudyControllerBase.this.dataType + " , mLastToModemDataType:" + SelfStudyControllerBase.this.mLastToModemDataType);
                        SelfStudyControllerBase selfStudyControllerBase10 = SelfStudyControllerBase.this;
                        int i5 = selfStudyControllerBase10.dataType;
                        if (i != i5) {
                            selfStudyControllerBase10.processDelayDataAppTypeChange(i5, "ACTIVITY_CHANGE");
                            SelfStudyControllerBase selfStudyControllerBase11 = SelfStudyControllerBase.this;
                            selfStudyControllerBase11.processUidAndActivityDataTypeChange(i, selfStudyControllerBase11.dataType);
                            return;
                        } else {
                            if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                                SelfStudyControllerBase selfStudyControllerBase12 = SelfStudyControllerBase.this;
                                if (selfStudyControllerBase12.mLastToModemDataType != 0) {
                                    selfStudyControllerBase12.processDelayDataAppTypeChange(selfStudyControllerBase12.dataType, "ACTIVITY_CHANGE");
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                    }
                }
                SelfStudyControllerBase.this.logV("ignore activity change for mIsMobileDataOn is false and DATA_TYPE_OTHER");
            }
        };
        this.mDataTypeStartTime = 0L;
        this.mVideoStallStartTime = 0L;
        this.mTelephonyCallBack_V2 = new AnonymousClass7();
        this.studyCallBack_V2 = new CellCallBack() { // from class: com.android.phone.performance.SelfStudyControllerBase.8
            @Override // com.android.phone.performance.SelfStudyControllerBase.CellCallBack
            public void onCellLocationChanged(int i, CellUnit cellUnit) {
                SelfStudyControllerBase.this.log("studyCallBack,cellLocation,phoneId is " + i);
                if (MiuiPhoneUtils.getActiveDataPhone() == null || i != MiuiPhoneUtils.getActiveDataPhone().getPhoneId()) {
                    return;
                }
                boolean zIsLteOrNr = SelfStudyControllerBase.this.isLteOrNr();
                SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                if (!zIsLteOrNr) {
                    try {
                        selfStudyControllerBase.mTimer.cancel();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (selfStudyControllerBase.getCellIdentity() != null) {
                    String combineString = SelfStudyControllerBase.this.getCombineString(String.valueOf(cellUnit.cellFreq) + cellUnit.cellPci, Long.toString(cellUnit.cellId, 16));
                    SelfStudyControllerBase.this.log("globalId = " + combineString);
                    boolean z = SelfStudyControllerBase.isStudying;
                    SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                    if (!z) {
                        if (selfStudyControllerBase2.lastCellMatch && !selfStudyControllerBase2.isMatchGoolsId(combineString)) {
                            SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                            if (selfStudyControllerBase3.mIsMobileDataOn) {
                                selfStudyControllerBase3.beginStudy();
                                try {
                                    SelfStudyControllerBase.this.triggerAtSpecialTime(10, SelfStudyControllerBase.this.new StudyTask());
                                    return;
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                    return;
                                }
                            }
                            selfStudyControllerBase3.isNeedDelayStudyByDataConnection = true;
                            return;
                        }
                        SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                        selfStudyControllerBase4.lastCellMatch = selfStudyControllerBase4.isMatchGoolsId(combineString);
                        return;
                    }
                    try {
                        selfStudyControllerBase2.mTimer.cancel();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    SelfStudyControllerBase selfStudyControllerBase5 = SelfStudyControllerBase.this;
                    selfStudyControllerBase5.triggerAtSpecialTime(selfStudyControllerBase5.whetherNeedAutoStop() ? 0 : 10, SelfStudyControllerBase.this.new StudyTask());
                }
            }
        };
        this.mUserCallBack = new TelephonyStatAdapter.ICallBack() { // from class: com.android.phone.performance.SelfStudyControllerBase.9
            public void onCellLocationChanged(final int i, CellLocation cellLocation) {
                SelfStudyControllerBase.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase.9.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SelfStudyControllerBase.this.log("mUserCallBack,onCellLocationChanged");
                        if (MiuiPhoneUtils.getActiveDataPhone() != null && i == MiuiPhoneUtils.getActiveDataPhone().getPhoneId() && SelfStudyControllerBase.this.isLteOrNr()) {
                            try {
                                SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                                String[] strArr = selfStudyControllerBase.mCurrentRouteCellIds;
                                strArr[0] = strArr[1];
                                strArr[1] = strArr[2];
                                strArr[2] = selfStudyControllerBase.getCurrentDataPhoneCellId();
                                SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                                if (selfStudyControllerBase2.haveStartedCollectBadCellOnMorningPeak || selfStudyControllerBase2.haveStartedCollectBadCellOnEveningPeak) {
                                    selfStudyControllerBase2.updateRealTimeCellInfo(selfStudyControllerBase2.mCurrentRouteCellIds[2], i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        };
        this.mBadCellCallBack_V2 = new CellCallBack() { // from class: com.android.phone.performance.SelfStudyControllerBase.10
            @Override // com.android.phone.performance.SelfStudyControllerBase.CellCallBack
            public void onCellLocationChanged(int i, CellUnit cellUnit) {
                SelfStudyControllerBase.this.log("mBadCellCallBack,onCellLocationChanged");
                if (MiuiPhoneUtils.getActiveDataPhone() == null || i != MiuiPhoneUtils.getActiveDataPhone().getPhoneId()) {
                    return;
                }
                try {
                    SelfStudyControllerBase.this.mTimer.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SelfStudyControllerBase.this.triggerAtSpecialTime(10, SelfStudyControllerBase.this.new BadCellStudyTimerTask());
            }
        };
        this.mContext = PhoneGlobals.getInstance();
        this.mNeighborCellManager = NeighborCellManager.getInstance();
        initMemberVariables();
        try {
            this.mModelCfgData = this.mContext.getResources().getIntArray(R.array.config_modem_model_cfg_raw_data);
        } catch (Exception e) {
            Log.e("SelfStudyController", "failed to load config_modem_model_cfg_raw_data: " + e.getMessage());
        }
        HandlerThread handlerThread = new HandlerThread("SelfStudyController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mWorkHandler = new SelfStudyHandler(this.mWorkThread.getLooper());
        this.mResultReceiver = new ResultReceiver(this.mWorkHandler) { // from class: com.android.phone.performance.SelfStudyControllerBase.1
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                int[] intArray;
                Log.d("SelfStudyController", "modem reason:" + i + bundle);
                if (i != 0 || bundle == null) {
                    return;
                }
                int[] intArray2 = bundle.getIntArray("modem_scene_info");
                if (intArray2 != null && intArray2.length > 0 && (intArray2[0] & 1) != 0) {
                    int[] iArr = SelfStudyControllerBase.this.mStaticState;
                    iArr[1] = iArr[1] + 1;
                }
                if (!bundle.containsKey("dds_assistant_info") || (intArray = bundle.getIntArray("dds_assistant_info")) == null) {
                    return;
                }
                SelfStudyControllerBase.this.handleNeighborCellResult(NeighborCellManager.parseNeighborCellInfo(intArray));
            }
        };
        this.mModelEnableResultReceiver = new ResultReceiver(this.mWorkHandler) { // from class: com.android.phone.performance.SelfStudyControllerBase.2
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                SelfStudyControllerBase.this.log("modem enable model:" + i);
            }
        };
        this.mModelCfgResultReceiver = new ResultReceiver(this.mWorkHandler) { // from class: com.android.phone.performance.SelfStudyControllerBase.3
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                SelfStudyControllerBase.this.log("modem model cfg:" + i + " resultData:" + bundle);
                if (i == 0) {
                    XmTelephonyManagerInner.getDefault().setModemCallbackSwitch(true, SelfStudyControllerBase.this.mModelEnableResultReceiver);
                }
            }
        };
    }

    private void initMemberVariables() {
        this.isCellActionOn = new boolean[3];
        this.lastLteStreamModes = new ArrayList();
        this.lastNrStreamModes = new ArrayList();
        this.lastColdLteStreamModes = new ArrayList();
        this.lastColdNrStreamModes = new ArrayList();
        this.lastLongLteStreamModes = new ArrayList();
        this.lastLongNrStreamModes = new ArrayList();
        this.mSampingCellMap = new HashMap();
        this.lastLteModes = new HashSet();
        this.lastNrModes = new HashSet();
        this.mLiveStreamingOffsetCells = new LinkedList();
        this.mGameOffsetCells = new LinkedList();
        this.mAiGameOffsetCells = new LinkedList();
        this.mWechatVideoOffsetCells = new LinkedList();
        this.mGameDelayLevelCount = new int[]{0, 1, 2, 3};
        this.allCellRouteList = new ArrayList();
        this.badCellRouteIds = new HashSet();
        this.cellTempRouteInfoList = new ArrayList();
        this.durationStatistic = new int[10];
        this.gameStatistic = new int[10];
        SHORT_VIDEO_PACKAGES = new LinkedList();
        this.mShortedVideoAppUids = new HashSet();
        this.mWebPageShortedVideoAppUids = new HashSet();
        this.mGameWhiteListAppUids = new HashSet();
        this.mAppWhitelist = new HashSet();
        this.mWhitelistAppPkgs = new HashSet();
        this.globalIds = new String[3];
        this.cellUnitScore = new CellUnit();
        this.mBadCellListInfos = new ArrayList();
        this.mSampingCellInfoList = new ArrayList();
        this.alarmPendingIntents = new PendingIntent[4];
        this.mResidentCellIds = new ArrayList(8);
        this.mVideoAndGameAiStatsList = new ConcurrentHashMap();
        this.mVideoStats = new ConcurrentHashMap();
        this.mGameStats = new ConcurrentHashMap();
        this.mStreamMediaStats = new ConcurrentHashMap();
        this.mSafDataList = new ArrayList();
        this.mStallDetailsList = new ArrayList();
        this.mStaticState = new int[2];
    }

    public enum Scene {
        Company(0),
        Home(1);

        public int value;

        Scene(int i) {
            this.value = i;
        }
    }

    public enum Rat {
        Lte(1),
        Nr(2);

        public int value;

        Rat(int i) {
            this.value = i;
        }
    }

    enum SelfStudyCloudType {
        Default(0),
        Game(1),
        WechatOptimization(2),
        VideoOptimization(3),
        StreamMedia(4),
        StreamWhiteList(5),
        call(6),
        LowPower(7);

        private int value;

        SelfStudyCloudType(int i) {
            this.value = i;
        }
    }

    enum MdOptimizationScene {
        Default(0),
        ShortVideo(1),
        WechatVideo(2),
        GameAi(3),
        Game(4);

        private int value;

        MdOptimizationScene(int i) {
            this.value = i;
        }
    }

    public class SelfStudyHandler extends Handler {
        public SelfStudyHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Code duplicated, block: B:155:0x032b A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:156:0x032d  */
        /* JADX WARN: Code duplicated, block: B:210:0x044b  */
        /* JADX WARN: Code duplicated, block: B:215:0x045e  */
        /* JADX WARN: Code duplicated, block: B:217:0x0466  */
        /* JADX WARN: Code duplicated, block: B:218:0x046b  */
        /* JADX WARN: Code duplicated, block: B:220:0x0475  */
        /* JADX WARN: Code duplicated, block: B:221:0x047a  */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SelfStudyControllerBase selfStudyControllerBase;
            boolean zIsContainsPackage;
            SelfStudyControllerBase selfStudyControllerBase2;
            SelfStudyControllerBase selfStudyControllerBase3;
            int i;
            Log.d("SelfStudyController", "handleMessage msg.what is " + message.what);
            int i2 = message.what;
            if (i2 == 1009) {
                SelfStudyControllerBase.isStudying = false;
                postDelayed(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$SelfStudyHandler$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleMessage$0();
                    }
                }, 1000L);
                return;
            }
            if (i2 == 1010) {
                if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                    SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                    if (!selfStudyControllerBase4.mIsMobileDataOn && selfStudyControllerBase4.mLastToModemDataType == 0) {
                        Log.d("SelfStudyController", "ignore uid change for mIsMobileDataOn is false and DATA_TYPE_OTHER");
                        return;
                    }
                }
                if (message.arg1 > 0) {
                    Log.d("SelfStudyController", "Not supported under Split Screen");
                    return;
                }
                ComponentName componentName = (ComponentName) ((ArrayList) message.obj).get(0);
                int i3 = SelfStudyControllerBase.this.dataType;
                if (componentName == null) {
                    return;
                }
                String packageName = componentName.getPackageName();
                SelfStudyControllerBase selfStudyControllerBase5 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase5.mIsInWeChatVoip) {
                    selfStudyControllerBase5.dataType = 3;
                } else {
                    boolean zEquals = "com.xiaomi.market".equals(packageName);
                    SelfStudyControllerBase selfStudyControllerBase6 = SelfStudyControllerBase.this;
                    if (zEquals) {
                        selfStudyControllerBase6.dataType = 7;
                    } else if (selfStudyControllerBase6.mWhitelistAppPkgs.contains(packageName)) {
                        SelfStudyControllerBase selfStudyControllerBase7 = SelfStudyControllerBase.this;
                        if (!selfStudyControllerBase7.isInGame) {
                            selfStudyControllerBase7.dataType = 2;
                            if (selfStudyControllerBase7.mWorkHandler.hasMessages(1025)) {
                                SelfStudyControllerBase.this.mWorkHandler.removeMessages(1025);
                            }
                        } else {
                            selfStudyControllerBase = SelfStudyControllerBase.this;
                            if (!selfStudyControllerBase.isInGame && selfStudyControllerBase.GAME_WHITELIST_PACKAGES.contains(packageName)) {
                                SelfStudyControllerBase.this.dataType = 1;
                            } else if (SelfStudyControllerBase.SHORT_VIDEO_PACKAGES.contains(packageName)) {
                                SelfStudyControllerBase.this.dataType = 4;
                            } else {
                                zIsContainsPackage = MiuiPhoneUtils.isContainsPackage("config_webpage_video_packages", packageName);
                                selfStudyControllerBase2 = SelfStudyControllerBase.this;
                                if (zIsContainsPackage) {
                                    selfStudyControllerBase2.dataType = 8;
                                } else {
                                    selfStudyControllerBase2.dataType = 0;
                                }
                            }
                        }
                    } else {
                        selfStudyControllerBase = SelfStudyControllerBase.this;
                        if (!selfStudyControllerBase.isInGame) {
                            if (SelfStudyControllerBase.SHORT_VIDEO_PACKAGES.contains(packageName)) {
                                SelfStudyControllerBase.this.dataType = 4;
                            } else {
                                zIsContainsPackage = MiuiPhoneUtils.isContainsPackage("config_webpage_video_packages", packageName);
                                selfStudyControllerBase2 = SelfStudyControllerBase.this;
                                if (zIsContainsPackage) {
                                    selfStudyControllerBase2.dataType = 8;
                                } else {
                                    selfStudyControllerBase2.dataType = 0;
                                }
                            }
                        } else if (SelfStudyControllerBase.SHORT_VIDEO_PACKAGES.contains(packageName)) {
                            SelfStudyControllerBase.this.dataType = 4;
                        } else {
                            zIsContainsPackage = MiuiPhoneUtils.isContainsPackage("config_webpage_video_packages", packageName);
                            selfStudyControllerBase2 = SelfStudyControllerBase.this;
                            if (zIsContainsPackage) {
                                selfStudyControllerBase2.dataType = 8;
                            } else {
                                selfStudyControllerBase2.dataType = 0;
                            }
                        }
                    }
                }
                if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                    if (SelfStudyControllerBase.this.isInGame && SelfStudyControllerBase.STATIC_GAME_CELL_WHITELIST_PACKAGES.contains(packageName)) {
                        SelfStudyControllerBase.this.dataType = 1;
                    }
                    String string = componentName.toString();
                    SelfStudyControllerBase.this.logV("UID_CHANGED pkg = " + packageName + " , cur = " + string + " , getTopActivityName = " + SelfStudyControllerBase.this.getTopActivityName());
                    if (packageName.equals(SelfStudyControllerBase.this.getTopAppName()) && !string.contains(SelfStudyControllerBase.this.getTopActivityName())) {
                        string = SelfStudyControllerBase.this.getTopActivityName();
                    }
                    boolean z = false;
                    for (String str : SelfStudyControllerBase.this.getLiveWhiteList()) {
                        if (!TextUtils.isEmpty(string) && string.contains(str)) {
                            SelfStudyControllerBase.this.dataType = 2;
                            z = true;
                        }
                    }
                    boolean z2 = false;
                    for (String str2 : SelfStudyControllerBase.this.getShortWhiteList()) {
                        if (!TextUtils.isEmpty(string) && string.contains(str2)) {
                            SelfStudyControllerBase.this.dataType = 4;
                            z2 = true;
                        }
                    }
                    if (!z) {
                        SelfStudyControllerBase selfStudyControllerBase8 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase8.dataType == 2) {
                            selfStudyControllerBase8.dataType = 0;
                            Log.d("SelfStudyController", "UID is LIVE_STREAMING, but activity is not.");
                        }
                    }
                    if (!z2) {
                        SelfStudyControllerBase selfStudyControllerBase9 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase9.dataType == 4) {
                            selfStudyControllerBase9.dataType = 0;
                            Log.d("SelfStudyController", "UID is SHORT_VIDEO, but activity is not.");
                        }
                    }
                    SelfStudyControllerBase selfStudyControllerBase10 = SelfStudyControllerBase.this;
                    int i4 = selfStudyControllerBase10.dataType;
                    if (i4 == 2 || i4 == 4) {
                        selfStudyControllerBase10.mIsResidentCell = selfStudyControllerBase10.isResidentCellScene();
                        SelfStudyControllerBase selfStudyControllerBase11 = SelfStudyControllerBase.this;
                        selfStudyControllerBase11.mIsSubway = selfStudyControllerBase11.isSubwayScene();
                        SelfStudyControllerBase selfStudyControllerBase12 = SelfStudyControllerBase.this;
                        selfStudyControllerBase12.initDataOptimizationState(selfStudyControllerBase12.mIsResidentCell);
                    }
                }
                SelfStudyControllerBase.this.processStreamBenefitStatistic();
                if (i3 != SelfStudyControllerBase.this.dataType) {
                    Log.d("SelfStudyController", "UID_CHANGED dataType is " + SelfStudyControllerBase.this.dataType + ", mIsResidentCell = " + SelfStudyControllerBase.this.mIsResidentCell + " , mIsSubway = " + SelfStudyControllerBase.this.mIsSubway);
                    boolean zIsSupportMtkDataOptimization = MiuiPhoneUtils.isSupportMtkDataOptimization();
                    SelfStudyControllerBase selfStudyControllerBase13 = SelfStudyControllerBase.this;
                    if (zIsSupportMtkDataOptimization) {
                        if (selfStudyControllerBase13.mWorkHandler.hasMessages(1043)) {
                            SelfStudyControllerBase.this.mWorkHandler.removeMessages(1043);
                        }
                        SelfStudyControllerBase selfStudyControllerBase14 = SelfStudyControllerBase.this;
                        Handler handler = selfStudyControllerBase14.mWorkHandler;
                        handler.sendMessageDelayed(handler.obtainMessage(1043, Integer.valueOf(selfStudyControllerBase14.dataType)), 30000L);
                    } else {
                        selfStudyControllerBase13.sendSelfStudyRequest(15, 1, new byte[]{(byte) selfStudyControllerBase13.dataType});
                    }
                    SelfStudyControllerBase selfStudyControllerBase15 = SelfStudyControllerBase.this;
                    selfStudyControllerBase15.processUidAndActivityDataTypeChange(i3, selfStudyControllerBase15.dataType);
                    return;
                }
                if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                    SelfStudyControllerBase selfStudyControllerBase16 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase16.mLastToModemDataType != 0) {
                        selfStudyControllerBase16.processDelayDataAppTypeChange(selfStudyControllerBase16.dataType, "UID_CHANGE");
                        return;
                    }
                    return;
                }
                return;
            }
            if (i2 == 1014) {
                SelfStudyControllerBase.this.processMobileStateEvent(((Boolean) message.obj).booleanValue());
                return;
            }
            if (i2 == 1015) {
                SelfStudyControllerBase.this.processLinkReliability(message.arg1, message.arg2);
                return;
            }
            if (i2 == 1017) {
                SelfStudyControllerBase.this.updateCloudSelfStudyFeatureStatus();
                return;
            }
            if (i2 == 1020) {
                if (SelfStudyControllerBase.isStudying) {
                    SelfStudyControllerBase.isStudying = false;
                    SelfStudyControllerBase.this.mPreferenceUtils.resetAllStudyStatus();
                    SelfStudyControllerBase.this.resetTables();
                    SelfStudyControllerBase.this.cancelStudyOvertimeAlarmClockIfHas();
                    SelfStudyControllerBase selfStudyControllerBase17 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase17.hasRegistStudyCallback) {
                        selfStudyControllerBase17.unregisterCallBack(selfStudyControllerBase17.studyCallBack_V2);
                        SelfStudyControllerBase.this.hasRegistStudyCallback = false;
                    }
                    try {
                        SelfStudyControllerBase.this.mTimer.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SelfStudyControllerBase.this.updateDatas(true);
                }
                SelfStudyControllerBase.this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$SelfStudyHandler$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleMessage$1();
                    }
                }, 15000L);
                return;
            }
            if (i2 == 1026) {
                boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                SelfStudyControllerBase.this.log("isWechatVoipEnabled " + zBooleanValue + " dataType = " + SelfStudyControllerBase.this.dataType);
                if (zBooleanValue) {
                    SelfStudyControllerBase selfStudyControllerBase18 = SelfStudyControllerBase.this;
                    if (!selfStudyControllerBase18.mIsInWeChatVoip && selfStudyControllerBase18.dataType != 3) {
                        selfStudyControllerBase18.dataType = 3;
                        Log.d("SelfStudyController", "dataType is " + SelfStudyControllerBase.this.dataType);
                        SelfStudyControllerBase selfStudyControllerBase19 = SelfStudyControllerBase.this;
                        selfStudyControllerBase19.sendSelfStudyRequest(15, 1, new byte[]{(byte) selfStudyControllerBase19.dataType});
                    } else if (!zBooleanValue) {
                        selfStudyControllerBase3 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase3.mIsInWeChatVoip && (i = selfStudyControllerBase3.dataType) == 3) {
                            selfStudyControllerBase3.mLastDataType = i;
                            selfStudyControllerBase3.dataType = 0;
                            Log.d("SelfStudyController", "dataType is " + SelfStudyControllerBase.this.dataType);
                            SelfStudyControllerBase selfStudyControllerBase20 = SelfStudyControllerBase.this;
                            selfStudyControllerBase20.sendSelfStudyRequest(15, 1, new byte[]{(byte) selfStudyControllerBase20.dataType});
                            Handler handler2 = SelfStudyControllerBase.this.mWorkHandler;
                            handler2.sendMessage(handler2.obtainMessage(1023));
                        }
                    }
                } else if (!zBooleanValue) {
                    selfStudyControllerBase3 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase3.mIsInWeChatVoip) {
                        selfStudyControllerBase3.mLastDataType = i;
                        selfStudyControllerBase3.dataType = 0;
                        Log.d("SelfStudyController", "dataType is " + SelfStudyControllerBase.this.dataType);
                        SelfStudyControllerBase selfStudyControllerBase21 = SelfStudyControllerBase.this;
                        selfStudyControllerBase21.sendSelfStudyRequest(15, 1, new byte[]{(byte) selfStudyControllerBase21.dataType});
                        Handler handler3 = SelfStudyControllerBase.this.mWorkHandler;
                        handler3.sendMessage(handler3.obtainMessage(1023));
                    }
                }
                SelfStudyControllerBase selfStudyControllerBase22 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase22.mIsInWeChatVoip != zBooleanValue) {
                    selfStudyControllerBase22.mIsInWeChatVoip = zBooleanValue;
                    selfStudyControllerBase22.onWechatStateChanged(zBooleanValue, selfStudyControllerBase22.isVideoFront);
                    return;
                }
                return;
            }
            if (i2 == 1028) {
                SelfStudyControllerBase.this.processCellLocationChanged((CellLocation) message.obj, message.arg1);
                SelfStudyControllerBase.this.processCellLocationChangedForStatistic((CellLocation) message.obj, message.arg1);
                SelfStudyControllerBase.this.processGameCellLocationChanged((CellLocation) message.obj, message.arg1);
                return;
            }
            if (i2 == 1050) {
                SelfStudyControllerBase.this.processDefaultDataSlotChange();
                return;
            }
            if (i2 == 1031) {
                SelfStudyControllerBase.this.delayProcessWechatEnter();
                return;
            }
            if (i2 == 1032) {
                SelfStudyControllerBase.this.processWechatExit();
                return;
            }
            if (i2 == 1038) {
                int i5 = message.arg1;
                int i6 = message.arg2;
                SelfStudyControllerBase selfStudyControllerBase23 = SelfStudyControllerBase.this;
                selfStudyControllerBase23.durationStatistic[i6] = 0;
                selfStudyControllerBase23.logV("EVENT_BEGIN_VIDEO_AND_GAME_AI_BENEFIT scene = " + i5 + " , lastQuality = " + SelfStudyControllerBase.this.lastQuality + " , mIsStreamStall = " + SelfStudyControllerBase.this.mIsStreamStall);
                if (i5 == MdOptimizationScene.ShortVideo.value || i5 == MdOptimizationScene.WechatVideo.value) {
                    SelfStudyControllerBase selfStudyControllerBase24 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase24.lastQuality == 0 || selfStudyControllerBase24.mIsStreamStall) {
                        int[] iArr = selfStudyControllerBase24.durationStatistic;
                        iArr[i6] = iArr[i6] | 1;
                    }
                    if (i5 == MdOptimizationScene.WechatVideo.value) {
                        long currentDlRateBps = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getCurrentDlRateBps() / 1024;
                        long currentUlRateBps = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getCurrentUlRateBps() / 1024;
                        if (currentDlRateBps < 80 || currentUlRateBps < 80) {
                            int[] iArr2 = SelfStudyControllerBase.this.durationStatistic;
                            iArr2[i6] = iArr2[i6] | 2;
                        }
                    }
                } else if (i5 == MdOptimizationScene.GameAi.value) {
                    SelfStudyControllerBase selfStudyControllerBase25 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase25.isGameAiLag) {
                        int[] iArr3 = selfStudyControllerBase25.durationStatistic;
                        iArr3[i6] = iArr3[i6] | 1;
                    }
                } else if (i5 == MdOptimizationScene.Game.value) {
                    if (SelfStudyControllerBase.this.isGameResidentLag()) {
                        int[] iArr4 = SelfStudyControllerBase.this.durationStatistic;
                        iArr4[i6] = iArr4[i6] | 1;
                    }
                    SelfStudyControllerBase selfStudyControllerBase26 = SelfStudyControllerBase.this;
                    selfStudyControllerBase26.gameStatistic[i6] = selfStudyControllerBase26.mGameLastDelayLevel;
                }
                Handler handler4 = SelfStudyControllerBase.this.mWorkHandler;
                handler4.sendMessageDelayed(handler4.obtainMessage(1038, i5, (i6 + 1) % 10), 1000L);
                return;
            }
            if (i2 == 1039) {
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) message.obj;
                int i7 = message.arg1;
                int i8 = SelfStudyControllerBase.this.dataType;
                if ((i8 == 2 || i8 == 4) && i7 == MdOptimizationScene.ShortVideo.value) {
                    SelfStudyControllerBase selfStudyControllerBase27 = SelfStudyControllerBase.this;
                    selfStudyControllerBase27.updateVideoAndGameAiStats("short_video_benefit", selfStudyControllerBase27.getTotalDuration(concurrentHashMap, i7, "After", false));
                    return;
                }
                SelfStudyControllerBase selfStudyControllerBase28 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase28.dataType == 3 && selfStudyControllerBase28.isVideoFront && i7 == MdOptimizationScene.WechatVideo.value) {
                    SelfStudyControllerBase selfStudyControllerBase29 = SelfStudyControllerBase.this;
                    selfStudyControllerBase29.updateVideoAndGameAiStats("wechat_video_benefit", selfStudyControllerBase29.getTotalDuration(concurrentHashMap, i7, "After", false));
                    return;
                } else if (SelfStudyControllerBase.this.isInGame && i7 == MdOptimizationScene.GameAi.value) {
                    SelfStudyControllerBase selfStudyControllerBase30 = SelfStudyControllerBase.this;
                    selfStudyControllerBase30.updateVideoAndGameAiStats("game_ai_benefit", selfStudyControllerBase30.getTotalDuration(concurrentHashMap, i7, "After", false));
                    return;
                } else {
                    if (SelfStudyControllerBase.this.isInGame && i7 == MdOptimizationScene.Game.value) {
                        SelfStudyControllerBase selfStudyControllerBase31 = SelfStudyControllerBase.this;
                        selfStudyControllerBase31.updateVideoAndGameAiStats("event_game_resident_cell_optimization_statistic", selfStudyControllerBase31.getTotalDuration(concurrentHashMap, i7, "After", false));
                        return;
                    }
                    return;
                }
            }
            if (i2 == 1041) {
                SelfStudyControllerBase selfStudyControllerBase32 = SelfStudyControllerBase.this;
                selfStudyControllerBase32.mIsStreamStall = false;
                if (SelfStudyControllerBase.isBeginCollectCellData) {
                    selfStudyControllerBase32.stopToCollectCellData(selfStudyControllerBase32.mIsWorkDay, 1);
                }
                if (SelfStudyControllerBase.this.mWorkHandler.hasMessages(1053)) {
                    SelfStudyControllerBase.this.logV("remove long stall event lasted for 10s.");
                    SelfStudyControllerBase.this.mWorkHandler.removeMessages(1053);
                    return;
                }
                return;
            }
            if (i2 == 1042) {
                SelfStudyControllerBase.this.log("registerSceneStateCallBack init");
                SelfStudyControllerBase selfStudyControllerBase33 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase33.mHydraSceneListener == null) {
                    selfStudyControllerBase33.mHydraSceneListener = new HydraSceneListener();
                }
                EventHubManager.getInstance().registerListener("modem", "hydra_scene_state_changed", SelfStudyControllerBase.this.mHydraSceneListener, this);
                return;
            }
            if (i2 != 1052) {
                if (i2 == 1053) {
                    if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                        return;
                    }
                    SelfStudyControllerBase selfStudyControllerBase34 = SelfStudyControllerBase.this;
                    selfStudyControllerBase34.beginStreamDataStall(selfStudyControllerBase34.dataType, 2);
                    return;
                }
                if (i2 != 1055 && i2 != 1056) {
                    switch (i2) {
                        case 1044:
                            SelfStudyControllerBase selfStudyControllerBase35 = SelfStudyControllerBase.this;
                            if (selfStudyControllerBase35.mIsDataStallApp) {
                                selfStudyControllerBase35.processDataStallAppStatistic(false, 1);
                            }
                            SelfStudyControllerBase.this.mIsStreamStall = false;
                            break;
                        case 1045:
                            SelfStudyControllerBase selfStudyControllerBase36 = SelfStudyControllerBase.this;
                            selfStudyControllerBase36.beginStreamDataStall(selfStudyControllerBase36.dataType, 0);
                            SelfStudyControllerBase selfStudyControllerBase37 = SelfStudyControllerBase.this;
                            selfStudyControllerBase37.mIsStreamStall = true;
                            selfStudyControllerBase37.processDataStallAppStatistic(true, 3);
                            SelfStudyControllerBase.this.updateRecordStateForHome();
                            boolean zIsModemEnteredStreamScene = SelfStudyControllerBase.this.isModemEnteredStreamScene();
                            SelfStudyControllerBase selfStudyControllerBase38 = SelfStudyControllerBase.this;
                            if (!zIsModemEnteredStreamScene) {
                                selfStudyControllerBase38.log("modem does not enter the streaming scene for video stall");
                            } else {
                                if (selfStudyControllerBase38.mIsResidentCell || selfStudyControllerBase38.mIsSubway) {
                                    SelfStudyControllerBase selfStudyControllerBase39 = SelfStudyControllerBase.this;
                                    selfStudyControllerBase39.beginToCollectCellData(selfStudyControllerBase39.mIsWorkDay, 3);
                                }
                                SelfStudyControllerBase.this.getOffSetFormModem(3, -1001);
                                if (!SelfStudyControllerBase.this.mWorkHandler.hasMessages(1053)) {
                                    SelfStudyControllerBase.this.logV("send long stall msg for VideoStall.");
                                    Handler handler5 = SelfStudyControllerBase.this.mWorkHandler;
                                    handler5.sendMessageDelayed(handler5.obtainMessage(1053), 10000L);
                                }
                            }
                            break;
                        case 1046:
                            SelfStudyControllerBase.this.processCallStateChange(message.arg1, message.arg2);
                            break;
                        default:
                            try {
                                SelfStudyControllerBase.this.handlePlatformDependentMessage(message);
                            } catch (Exception e2) {
                                Log.d("SelfStudyController", "Unexpected exception in handlePlatformDependentMessage", e2);
                                return;
                            }
                            break;
                    }
                    return;
                }
            }
            SelfStudyControllerBase.this.notify3rdAppOpt();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$0() {
            if (SelfStudyControllerBase.this.allCellRouteList.size() > 0) {
                SelfStudyControllerBase.this.judgeOrStoreRoute();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$1() {
            SelfStudyControllerBase.this.sendSelfStudyRequest(100, 1, new int[]{SelfStudyControllerBase.mDefaultRealTimeOptimizationStatus});
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            int i = selfStudyControllerBase.dataType;
            if (i == 1 || ((i == 3 && selfStudyControllerBase.isVideoFront) || i == 2 || i == 4 || (i == 8 && selfStudyControllerBase.hasSendDataStallAction))) {
                selfStudyControllerBase.log("restart data stall due to modem crash");
                SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                int i2 = selfStudyControllerBase2.dataType;
                selfStudyControllerBase2.processStartDataStall(i2 != 8 ? i2 : 4);
            }
            if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                SelfStudyControllerBase.this.sendSelfStudyRequest(400, 1, new int[]{0});
            }
            if (SubscriptionManager.isValidPhoneId(SelfStudyControllerBase.this.mNotifyModemDdsId)) {
                SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                selfStudyControllerBase3.sendDdsToModem(selfStudyControllerBase3.mNotifyModemDdsId);
            }
        }
    }

    public void registerActivityChangeListener(int i) {
        if (this.mActivityChangeListener != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(getShortWhiteList()));
            arrayList.addAll(Arrays.asList(getLiveWhiteList()));
            ProcessManager.unregisterActivityChanageListener(this.mActivityChangeListener);
            ProcessManager.registerActivityChangeListener((List) null, arrayList, this.mActivityChangeListener);
            logV("registerActivityChangeListener done for reason:" + i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] getShortWhiteList() {
        if (isBelowMtkDataOptVersionFive()) {
            return CONGESTION_SHORT_VIDEO_WHITE_LIST_DEFAULT;
        }
        String[] strArr = this.mCloudShortWhiteList;
        return (strArr == null || strArr.length <= 0 || mIsNeedUpdateWhiteList) ? CONGESTION_SHORT_VIDEO_WHITE_LIST_DEFAULT : strArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] getLiveWhiteList() {
        if (isBelowMtkDataOptVersionFive()) {
            return CONGESTION_LIVE_STREAM_WHITE_LIST_DEFAULT;
        }
        String[] strArr = this.mCloudLiveWhiteList;
        return (strArr == null || strArr.length <= 0 || mIsNeedUpdateWhiteList) ? CONGESTION_LIVE_STREAM_WHITE_LIST_DEFAULT : strArr;
    }

    public void processUidAndActivityDataTypeChange(int i, int i2) {
        calcDataTypeDuration(i, i2);
        calcVideoQualityDuration(i, this.lastQuality, 1);
        this.mVideoStallStartTime = 0L;
        processDataTypeChanged(i);
    }

    public void processDelayDataAppTypeChange(int i, String str) {
        logV("processDelayDataAppTypeChange dataType:" + i + ",scene:" + str + ",mLastToModemDataType:" + this.mLastToModemDataType);
        if (this.mWorkHandler.hasMessages(1043)) {
            if (this.mWorkHandler.hasMessages(1043, Integer.valueOf(i))) {
                logV("return for same dataType");
                return;
            } else {
                logV("remove for dataTYpe not match ");
                this.mWorkHandler.removeMessages(1043);
                removeMsgForExitSceneBreak();
            }
        }
        int i2 = i != 4 ? 30000 : 0;
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1043, Integer.valueOf(i)), i2);
    }

    public void processDataStallAppStatistic(boolean z, int i) {
        if (this.mIsMobileDataOn) {
            int i2 = this.dataType;
            if (i2 == 2 || i2 == 4) {
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                if (this.mIsDataStallApp != z || (z && jElapsedRealtime - this.mFetchTime >= 300000)) {
                    this.mIsDataStallApp = z;
                    String topAppName = getTopAppName();
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    concurrentHashMap.put("sourceType", Integer.valueOf(i));
                    concurrentHashMap.put("sceneType", Integer.valueOf(this.dataType));
                    concurrentHashMap.put("pkg", topAppName);
                    concurrentHashMap.put("isDataStallApp", Boolean.valueOf(z));
                    ((IModemModule) ModuleDelegate.get(IModemModule.class)).modemHydraProcess(123, concurrentHashMap);
                    if (this.mWorkHandler.hasMessages(1044)) {
                        this.mWorkHandler.removeMessages(1044);
                    }
                    if (z && 1 == i) {
                        Handler handler = this.mWorkHandler;
                        handler.sendMessageDelayed(handler.obtainMessage(1044), 10000L);
                    }
                    this.mFetchTime = z ? SystemClock.elapsedRealtime() : 0L;
                }
            }
        }
    }

    public boolean isInHomeScence() {
        boolean z = true;
        if (queryResidentCellSenceHomeOrCompany() == 1) {
            return true;
        }
        if (!isInAIHomeScene() && !this.mIsHome) {
            z = false;
        }
        Log.d("SelfStudyController", "isInHomeOrCompanyScence isInAIHomeScene = " + isInAIHomeScene() + ", mIsHome = " + this.mIsHome + " , queryResidentCellSenceHomeOrCompany != 1 ");
        return z;
    }

    public boolean isInCompanyScence() {
        boolean z = true;
        if (queryResidentCellSenceHomeOrCompany() == 0) {
            return true;
        }
        if (!isInAICompanyScene() && !this.mIsCompany) {
            z = false;
        }
        Log.d("SelfStudyController", "isInHomeOrCompanyScence isInAICompanyScene = " + isInAICompanyScene() + ", mIsCompany = " + this.mIsCompany + " , queryResidentCellSenceHomeOrCompany != 0 ");
        return z;
    }

    public boolean isInHomeOrCompanyFromXiaoAiScene() {
        return isInAICompanyScene() || this.mIsCompany || isInAIHomeScene() || this.mIsHome;
    }

    public boolean isInHomeOrCompanyScene(int i) {
        return i == 0 || i == 1 || isMandatoryInHomeOrCompany();
    }

    public boolean isMandatoryInHomeOrCompany() {
        return SystemProperties.getBoolean("persist.radio.home.or.company", false);
    }

    public ConcurrentHashMap getTotalDuration(ConcurrentHashMap concurrentHashMap, int i, String str, boolean z) {
        if (concurrentHashMap == null) {
            return null;
        }
        if (concurrentHashMap.size() <= 20) {
            int i2 = 0;
            if (i == MdOptimizationScene.ShortVideo.value) {
                int i3 = 0;
                while (i2 < 10) {
                    if (this.durationStatistic[i2] != 0) {
                        i3++;
                    }
                    concurrentHashMap.put("abnormal" + str, Integer.valueOf(i3));
                    concurrentHashMap.put("normal" + str, Integer.valueOf(10 - i3));
                    i2++;
                }
            } else {
                if (i == MdOptimizationScene.WechatVideo.value) {
                    int i4 = 0;
                    int i5 = 0;
                    int i6 = 0;
                    while (i2 < 10) {
                        int i7 = this.durationStatistic[i2];
                        if (i7 == 0) {
                            i6++;
                        } else if (i7 == 1) {
                            i5++;
                        } else if (i7 == 2) {
                            i4++;
                        } else if (i7 == 3) {
                            i4++;
                            i5++;
                        }
                        i2++;
                    }
                    concurrentHashMap.put("ulDlRate" + str, Integer.valueOf(i4));
                    concurrentHashMap.put("audio" + str, Integer.valueOf(i5));
                    concurrentHashMap.put("normal" + str, Integer.valueOf(i6));
                    return concurrentHashMap;
                }
                if (i == MdOptimizationScene.GameAi.value) {
                    int i8 = 0;
                    while (i2 < 10) {
                        if (this.durationStatistic[i2] == 1) {
                            i8++;
                        }
                        i2++;
                    }
                    concurrentHashMap.put("abnormal" + str, Integer.valueOf(i8));
                    concurrentHashMap.put("normal" + str, Integer.valueOf(10 - i8));
                    return concurrentHashMap;
                }
                if (i == MdOptimizationScene.Game.value) {
                    if (!z) {
                        int i9 = 0;
                        while (i2 < 10) {
                            if (this.durationStatistic[i2] == 1) {
                                i9++;
                            }
                            i2++;
                        }
                        concurrentHashMap.put("gameResidentCellModemOffsetLagCnt_" + str, Integer.valueOf(i9));
                        concurrentHashMap.put("gameResidentCellModemOffsetAllCnt_" + str, Integer.valueOf(10 - i9));
                        log("getTotalDuration_gameresident_map " + concurrentHashMap);
                        return concurrentHashMap;
                    }
                    getGameDelayDur(z, str, concurrentHashMap);
                    log("isDelayLevel_map " + concurrentHashMap);
                }
            }
        }
        return concurrentHashMap;
    }

    protected void processCellLocationChangedForStatistic(CellLocation cellLocation, int i) {
        Phone phone;
        CellIdentity currentCellIdentity;
        if (!MiuiPhoneUtils.isDefaultDataSlotId(i) || cellLocation == null || cellLocation.isEmpty() || (phone = MiuiPhoneUtils.getPhone(i)) == null || (currentCellIdentity = phone.getCurrentCellIdentity()) == null || currentCellIdentity.getGlobalCellId() == null) {
            return;
        }
        int i2 = this.dataType;
        if ((i2 == 2 || i2 == 4) && this.isVideoOpt) {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            MdOptimizationScene mdOptimizationScene = MdOptimizationScene.ShortVideo;
            getTotalDuration(concurrentHashMap, mdOptimizationScene.value, "Before", false);
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1039, mdOptimizationScene.value, 0, concurrentHashMap), 10000L);
            this.isVideoOpt = false;
            return;
        }
        if (i2 == 3 && this.isWechatOpt) {
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            MdOptimizationScene mdOptimizationScene2 = MdOptimizationScene.WechatVideo;
            getTotalDuration(concurrentHashMap2, mdOptimizationScene2.value, "Before", false);
            Handler handler2 = this.mWorkHandler;
            handler2.sendMessageDelayed(handler2.obtainMessage(1039, mdOptimizationScene2.value, 0, concurrentHashMap2), 10000L);
            this.isWechatOpt = false;
            return;
        }
        if (this.isInGame && this.isGameAiOpt) {
            ConcurrentHashMap concurrentHashMap3 = new ConcurrentHashMap();
            MdOptimizationScene mdOptimizationScene3 = MdOptimizationScene.GameAi;
            getTotalDuration(concurrentHashMap3, mdOptimizationScene3.value, "Before", false);
            Handler handler3 = this.mWorkHandler;
            handler3.sendMessageDelayed(handler3.obtainMessage(1039, mdOptimizationScene3.value, 0, concurrentHashMap3), 10000L);
            return;
        }
        if (i2 == 1) {
            if ((MiuiPhoneUtils.isSupportMtkDataOptimization() || MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) && this.isInGame && isEnterGameOptimizer()) {
                if (MiuiPhoneUtils.isSupportMtkDataOptimization() && this.isGameSetOffsetFromModem) {
                    ConcurrentHashMap concurrentHashMap4 = new ConcurrentHashMap();
                    MdOptimizationScene mdOptimizationScene4 = MdOptimizationScene.Game;
                    getTotalDuration(concurrentHashMap4, mdOptimizationScene4.value, "Before", false);
                    Handler handler4 = this.mWorkHandler;
                    handler4.sendMessageDelayed(handler4.obtainMessage(1039, mdOptimizationScene4.value, 0, concurrentHashMap4), 10000L);
                    isGameSetOffsetFromModem(false);
                    return;
                }
                if (MiuiPhoneUtils.isSupportQcGameResidentCellOptimization() && this.isGameSetDataStall) {
                    ConcurrentHashMap concurrentHashMap5 = new ConcurrentHashMap();
                    MdOptimizationScene mdOptimizationScene5 = MdOptimizationScene.Game;
                    getTotalDuration(concurrentHashMap5, mdOptimizationScene5.value, "Before", false);
                    Handler handler5 = this.mWorkHandler;
                    handler5.sendMessageDelayed(handler5.obtainMessage(1039, mdOptimizationScene5.value, 0, concurrentHashMap5), 10000L);
                    isSetDataStallForGameOpt(false);
                }
            }
        }
    }

    public boolean isProcessedWechatVideoEnter() {
        return this.mIsProcessedWechatEnter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calcDataTypeDuration(int i, int i2) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        log("calcDataTypeDuration : lastDataType = " + i + " curDataType = " + i2);
        long j = this.mDataTypeStartTime;
        if (j == 0) {
            if (i2 != 8 || this.isVideoFront) {
                this.mDataTypeStartTime = jElapsedRealtime;
                return;
            }
            return;
        }
        long j2 = (jElapsedRealtime - j) / 1000;
        if (i == 2) {
            recordDuration("liveStreamingDur", j2, "video");
        } else if (i == 4) {
            recordDuration("shortVideoDur", j2, "video");
        } else if (i == 8) {
            recordDuration("webPageShortVideoDur", j2, "video");
        }
        if (i2 == 2 || i2 == 4 || (i2 == 8 && this.isVideoFront)) {
            this.mDataTypeStartTime = jElapsedRealtime;
        } else {
            this.mDataTypeStartTime = 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:13:0x001e  */
    public void calcVideoQualityDuration(int i, int i2, int i3) {
        long j;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (i2 == 1 && i3 == -1) {
            return;
        }
        if (i2 == 0 && i3 != 0) {
            long j2 = this.mVideoStallStartTime;
            if (j2 != 0) {
                j = (jElapsedRealtime - j2) / 1000;
                this.mVideoStallStartTime = 0L;
            } else {
                if (i2 != 1) {
                }
                j = 0;
            }
        } else {
            if (i2 != 1 && i3 == 0) {
                this.mVideoStallStartTime = jElapsedRealtime;
                return;
            }
            j = 0;
        }
        if (j <= 0 || j > 30) {
            log("video stall duration more than 30 secs, ignore");
            return;
        }
        if (i == 2) {
            recordDuration("liveStreamingStallDur", j, "video");
        } else if (i == 4) {
            recordDuration("shortVideoStallDur", j, "video");
        } else {
            if (i != 8) {
                return;
            }
            recordDuration("webPageShortVideoStallDur", j, "video");
        }
    }

    private void reloadVideoStatisticFromSp() {
        try {
            String string = this.mPreferenceUtils.getString("video_statistic_stats", "");
            if (string.isEmpty()) {
                return;
            }
            this.mVideoStats = (ConcurrentHashMap) ObjectSerializer.deserialize(string);
        } catch (Exception e) {
            Utils.logD("SelfStudyController", "set video statistic stats exception:" + e);
        }
    }

    private void clearVideoStatistic(String str) {
        if ("video".equalsIgnoreCase(str)) {
            this.mVideoStats.clear();
            try {
                this.mPreferenceUtils.putString("video_statistic_stats", ObjectSerializer.serialize(this.mVideoStats));
            } catch (Exception e) {
                Utils.logD("SelfStudyController", "set video statistic stats exception:" + e);
            }
            this.mDataTypeStartTime = 0L;
            this.mVideoStallStartTime = 0L;
            return;
        }
        if ("game".equalsIgnoreCase(str)) {
            this.mGameStats.clear();
            try {
                this.mPreferenceUtils.putString("gameResidentCellStatisticStats", ObjectSerializer.serialize(this.mGameStats));
                return;
            } catch (Exception e2) {
                Utils.logD("SelfStudyController", "set game statistic stats exception:" + e2);
                return;
            }
        }
        if ("streamMedia".equalsIgnoreCase(str)) {
            this.mStreamMediaStats.clear();
            try {
                this.mPreferenceUtils.putString("streamMediaStatisticStats", ObjectSerializer.serialize(this.mStreamMediaStats));
            } catch (Exception e3) {
                Utils.logD("SelfStudyController", "set streamMedia statistic stats exception:" + e3);
            }
        }
    }

    public void recordDuration(String str, long j, String str2) {
        ConcurrentHashMap concurrentHashMap;
        if ("video".equalsIgnoreCase(str2)) {
            ConcurrentHashMap concurrentHashMap2 = this.mVideoStats;
            if (concurrentHashMap2 == null) {
                return;
            }
            this.mVideoStats.put(str, Long.valueOf((concurrentHashMap2.get(str) != null ? ((Long) this.mVideoStats.get(str)).longValue() : 0L) + j));
            try {
                this.mPreferenceUtils.putString("video_statistic_stats", ObjectSerializer.serialize(this.mVideoStats));
                return;
            } catch (Exception e) {
                Utils.logD("SelfStudyController", "set video duration stats exception:" + e);
                return;
            }
        }
        if (!"game".equalsIgnoreCase(str2) || (concurrentHashMap = this.mGameStats) == null || concurrentHashMap.size() > 20) {
            return;
        }
        this.mGameStats.put(str, Long.valueOf((this.mGameStats.get(str) != null ? ((Long) this.mGameStats.get(str)).longValue() : 0L) + j));
        try {
            this.mPreferenceUtils.putString("gameResidentCellStatisticStats", ObjectSerializer.serialize(this.mGameStats));
        } catch (Exception e2) {
            Utils.logD("SelfStudyController", "set game duration stats exception:" + e2);
        }
    }

    public void recordCount(String str, String str2) {
        ConcurrentHashMap concurrentHashMap;
        if ("video".equalsIgnoreCase(str2)) {
            ConcurrentHashMap concurrentHashMap2 = this.mVideoStats;
            if (concurrentHashMap2 == null) {
                return;
            }
            this.mVideoStats.put(str, Integer.valueOf((concurrentHashMap2.get(str) != null ? ((Integer) this.mVideoStats.get(str)).intValue() : 0) + 1));
            try {
                this.mPreferenceUtils.putString("video_statistic_stats", ObjectSerializer.serialize(this.mVideoStats));
                return;
            } catch (Exception e) {
                Utils.logD("SelfStudyController", "set video counts stats exception:" + e);
                return;
            }
        }
        if ("game".equalsIgnoreCase(str2)) {
            recordGameCount(str, str2, 0);
            return;
        }
        if (!"streamMedia".equalsIgnoreCase(str2) || (concurrentHashMap = this.mStreamMediaStats) == null) {
            return;
        }
        this.mStreamMediaStats.put(str, Integer.valueOf((concurrentHashMap.get(str) != null ? ((Integer) this.mStreamMediaStats.get(str)).intValue() : 0) + 1));
        try {
            this.mPreferenceUtils.putString("streamMediaStatisticStats", ObjectSerializer.serialize(this.mStreamMediaStats));
            Log.d("SelfStudyController", "recordCount mStreamMediaStats:" + this.mStreamMediaStats);
        } catch (Exception e2) {
            Utils.logD("SelfStudyController", "set streamMedia counts stats exception:" + e2);
        }
    }

    public void recordGameCount(String str, String str2, int i) {
        ConcurrentHashMap concurrentHashMap = this.mGameStats;
        if (concurrentHashMap == null || concurrentHashMap.size() > 20) {
            return;
        }
        if (this.mGameStats.get(str) != null) {
            i = ((Integer) this.mGameStats.get(str)).intValue();
        }
        this.mGameStats.put(str, Integer.valueOf(i + 1));
        try {
            this.mPreferenceUtils.putString("gameResidentCellStatisticStats", ObjectSerializer.serialize(this.mGameStats));
            Log.d("SelfStudyController", "recordCount mGameStats:" + this.mGameStats);
        } catch (Exception e) {
            Utils.logD("SelfStudyController", "set game counts stats exception:" + e);
        }
    }

    public void reportVideoStatistic() {
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_video_optimization_statistic", new HashMap(this.mVideoStats));
        clearVideoStatistic("video");
    }

    public void reportStreamMediaStatistic() {
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_stream_media_state_statistic", new HashMap(this.mStreamMediaStats));
        clearVideoStatistic("streamMedia");
    }

    public void reportGameResidentCellStatistic() {
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_game_resident_cell_optimization_statistic", new HashMap(this.mGameStats));
        clearVideoStatistic("game");
    }

    public void reportSafStatistic() {
        ArrayList arrayList = new ArrayList(this.mSafDataList);
        HashMap map = new HashMap();
        map.put("Params", arrayList);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_saf_statistic", map);
        this.mSafDataList.clear();
    }

    public void reportStallStatistic() {
        if (isBelowMtkDataOptVersionFive()) {
            return;
        }
        ArrayList arrayList = new ArrayList(this.mStallDetailsList);
        HashMap map = new HashMap();
        map.put("Params", arrayList);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_stall_details_statistic", map);
        this.mStallDetailsList.clear();
    }

    public void recordStallDetailsStatistic(boolean z, int i, int i2, int i3, ArrayList arrayList) {
        if (isBelowMtkDataOptVersionFive()) {
            return;
        }
        if (arrayList.size() >= 60) {
            arrayList.remove(0);
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("isStall", Boolean.valueOf(z));
        concurrentHashMap.put("reason", Integer.valueOf(i));
        concurrentHashMap.put("dataType", Integer.valueOf(i2));
        concurrentHashMap.put("stallType", Integer.valueOf(i3));
        arrayList.add(concurrentHashMap);
    }

    public void updateVideoAndGameAiStats(String str, ConcurrentHashMap concurrentHashMap) {
        if (concurrentHashMap == null) {
            return;
        }
        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap(concurrentHashMap);
        ArrayList arrayList = (ArrayList) this.mVideoAndGameAiStatsList.get(str);
        if (arrayList == null) {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(concurrentHashMap2);
            this.mVideoAndGameAiStatsList.put(str, arrayList2);
        } else {
            arrayList.add(concurrentHashMap2);
        }
        concurrentHashMap.clear();
    }

    public void reportVideoAndGameAiBenefitStatistic() {
        ConcurrentHashMap concurrentHashMap = this.mVideoAndGameAiStatsList;
        if (concurrentHashMap != null && concurrentHashMap.size() > 0) {
            for (String str : this.mVideoAndGameAiStatsList.keySet()) {
                HashMap map = new HashMap();
                map.put("Params", this.mVideoAndGameAiStatsList.get(str));
                MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, str, map);
            }
        }
        this.mVideoAndGameAiStatsList.clear();
    }

    private void reloadStreamMediaStatisticFromSp() {
        try {
            String string = this.mPreferenceUtils.getString("streamMediaStatisticStats", "");
            if (string.isEmpty()) {
                return;
            }
            this.mStreamMediaStats = (ConcurrentHashMap) ObjectSerializer.deserialize(string);
        } catch (Exception e) {
            Utils.logD("SelfStudyController", "set streamMedia statistic stats exception:" + e);
        }
    }

    private void reloadGameResidentCellStatisticFromSp() {
        try {
            String string = this.mPreferenceUtils.getString("gameResidentCellStatisticStats", "");
            if (string.isEmpty()) {
                return;
            }
            this.mGameStats = (ConcurrentHashMap) ObjectSerializer.deserialize(string);
        } catch (Exception e) {
            Utils.logD("SelfStudyController", "set game statistic stats exception:" + e);
        }
    }

    class RecognizeBeginCellState extends State {
        public void exit() {
        }

        RecognizeBeginCellState() {
        }

        public void enter() {
            SelfStudyControllerBase.this.saveMachineState(1);
        }

        /* JADX WARN: Code duplicated, block: B:17:0x005b  */
        public boolean processMessage(Message message) {
            if (message.what != 0) {
                return false;
            }
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            if (selfStudyControllerBase.hasBeginRecordMorning) {
                return true;
            }
            int beginCellCounts = selfStudyControllerBase.getBeginCellCounts();
            SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
            if (beginCellCounts >= 1) {
                selfStudyControllerBase2.transitionTo(selfStudyControllerBase2.mStudyState);
                SelfStudyControllerBase.this.sendMessage(0);
            } else {
                if (selfStudyControllerBase2.getBeginDay() >= 0) {
                    int currentDay = SelfStudyControllerBase.this.getCurrentDay(System.currentTimeMillis());
                    SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                    if (currentDay <= selfStudyControllerBase3.getCurrentDay(selfStudyControllerBase3.getBeginDay()) + 5) {
                        int currentDay2 = SelfStudyControllerBase.this.getCurrentDay(System.currentTimeMillis());
                        SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                        if (currentDay2 < selfStudyControllerBase4.getCurrentDay(selfStudyControllerBase4.getBeginDay())) {
                            SelfStudyControllerBase.this.removeBeginCellStatus();
                        }
                    } else {
                        SelfStudyControllerBase.this.removeBeginCellStatus();
                    }
                }
                int currentDay3 = SelfStudyControllerBase.this.getCurrentDay(System.currentTimeMillis());
                SelfStudyControllerBase selfStudyControllerBase5 = SelfStudyControllerBase.this;
                if (currentDay3 != selfStudyControllerBase5.getCurrentDay(selfStudyControllerBase5.getBeginDay())) {
                    SelfStudyControllerBase.this.judgeWhetherStoreBeginCell();
                }
            }
            return true;
        }
    }

    class StudyState extends State {
        StudyState() {
        }

        public void enter() {
            SelfStudyControllerBase.this.saveMachineState(3);
            SelfStudyControllerBase.this.regUnregForModemReset(true);
        }

        public void exit() {
            SelfStudyControllerBase.this.regUnregForModemReset(false);
        }

        public boolean processMessage(Message message) {
            int i = message.what;
            if (i != 0) {
                if (i == 1) {
                    SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                    selfStudyControllerBase.transitionTo(selfStudyControllerBase.mRecognizeBeginCellState);
                    return true;
                }
                if (i != 3) {
                    return false;
                }
                SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                selfStudyControllerBase2.transitionTo(selfStudyControllerBase2.mUsingState);
                return true;
            }
            if (SelfStudyControllerBase.isStudying || SelfStudyControllerBase.this.hasStudy()) {
                if (SelfStudyControllerBase.this.hasStudy()) {
                    SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase3.getCurrentDay(selfStudyControllerBase3.getStudyTime()) != SelfStudyControllerBase.this.getCurrentDay(System.currentTimeMillis())) {
                        if (SelfStudyControllerBase.this.dbHelper.getCellRouteInfoCount() >= 10) {
                            SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                            selfStudyControllerBase4.transitionTo(selfStudyControllerBase4.mUsingState);
                        } else {
                            SelfStudyControllerBase.isStudying = false;
                            SelfStudyControllerBase.this.mPreferenceUtils.resetAllStudyStatus();
                            SelfStudyControllerBase.this.resetTables();
                        }
                        SelfStudyControllerBase.this.sendMessage(0);
                    }
                }
            } else if (SelfStudyControllerBase.this.getBeginDay() >= 0) {
                long jAbs = Math.abs(System.currentTimeMillis() - SelfStudyControllerBase.this.getBeginDay());
                SelfStudyControllerBase selfStudyControllerBase5 = SelfStudyControllerBase.this;
                if (jAbs < 1296000000) {
                    if (!selfStudyControllerBase5.hasRegistStudyCallback) {
                        selfStudyControllerBase5.judgeWhetherBeginStudy();
                    }
                } else {
                    selfStudyControllerBase5.removeBeginCellStatus();
                    SelfStudyControllerBase selfStudyControllerBase6 = SelfStudyControllerBase.this;
                    selfStudyControllerBase6.transitionTo(selfStudyControllerBase6.mRecognizeBeginCellState);
                    SelfStudyControllerBase.this.screenOnBeginStudy();
                }
            }
            return true;
        }
    }

    class UsingState extends State {
        private boolean isPredictingOnMorningPeak;
        private boolean isUsingOnMorningPeak;
        private long routeCellCounts;

        public void exit() {
        }

        UsingState() {
        }

        public void enter() {
            SelfStudyControllerBase.this.saveMachineState(4);
            this.isUsingOnMorningPeak = false;
            this.isPredictingOnMorningPeak = false;
            this.routeCellCounts = SelfStudyControllerBase.this.dbHelper.getCellRouteInfoCount();
        }

        /* JADX WARN: Code duplicated, block: B:44:0x0122  */
        /* JADX WARN: Code duplicated, block: B:46:0x012a  */
        /* JADX WARN: Code duplicated, block: B:48:0x012e  */
        /* JADX WARN: Code duplicated, block: B:51:0x0139  */
        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        public boolean processMessage(Message message) {
            int i = message.what;
            boolean z = false;
            if (i != 0) {
                if (i != 1) {
                    return false;
                }
                SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                selfStudyControllerBase.transitionTo(selfStudyControllerBase.mRecognizeBeginCellState);
                return true;
            }
            if (SelfStudyControllerBase.this.currentIsMorning()) {
                SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase2.mIsWorkDay && selfStudyControllerBase2.mCurrentDay != SelfStudyControllerBase.this.getCurrentDay(System.currentTimeMillis()) && !TextUtils.isEmpty(SelfStudyControllerBase.this.getCurrentGlobal())) {
                    SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                    selfStudyControllerBase3.mCurrentDay = selfStudyControllerBase3.getCurrentDay(System.currentTimeMillis());
                    int differentCellCount = SelfStudyControllerBase.this.getDifferentCellCount();
                    if (SelfStudyControllerBase.this.getCurrentUseDate() != SelfStudyControllerBase.this.getCurrentDay(System.currentTimeMillis())) {
                        SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                        boolean zIsMatchGoolsId = selfStudyControllerBase4.isMatchGoolsId(selfStudyControllerBase4.getCurrentGlobal());
                        int i2 = zIsMatchGoolsId ? 0 : differentCellCount + 1;
                        SelfStudyControllerBase.this.log("judeyWhetherResetAllResultOrUser count = " + i2);
                        SelfStudyControllerBase selfStudyControllerBase5 = SelfStudyControllerBase.this;
                        selfStudyControllerBase5.updateUseDays(selfStudyControllerBase5.getUseDays() + 1);
                        differentCellCount = i2;
                        z = zIsMatchGoolsId;
                    }
                    if (differentCellCount < 3 && this.routeCellCounts >= 10 && SelfStudyControllerBase.this.getUseDays() < 7) {
                        if (SelfStudyControllerBase.this.getCurrentUseDate() != SelfStudyControllerBase.this.getCurrentDay(System.currentTimeMillis())) {
                            SelfStudyControllerBase.this.updateDifferentCellCount(differentCellCount);
                        }
                        SelfStudyControllerBase selfStudyControllerBase6 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase6.isSupportCellAction) {
                            selfStudyControllerBase6.beginToUse();
                            this.isUsingOnMorningPeak = true;
                        }
                        SelfStudyControllerBase selfStudyControllerBase7 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase7.isSupportPredictLoad && z) {
                            selfStudyControllerBase7.beginPredict();
                            this.isPredictingOnMorningPeak = true;
                        }
                        if (SelfStudyControllerBase.this.getCurrentUseDate() != SelfStudyControllerBase.this.getCurrentDay(System.currentTimeMillis())) {
                            SelfStudyControllerBase selfStudyControllerBase8 = SelfStudyControllerBase.this;
                            selfStudyControllerBase8.updateCurrentUseDate(selfStudyControllerBase8.getCurrentDay(System.currentTimeMillis()));
                        }
                    } else {
                        SelfStudyControllerBase.this.stopToUse();
                        SelfStudyControllerBase.this.stopPredict();
                        SelfStudyControllerBase.this.removeBeginCellStatus();
                        SelfStudyControllerBase.this.mPreferenceUtils.resetAllStudyStatus();
                        SelfStudyControllerBase.this.resetRouteStudyJudgeCell();
                        SelfStudyControllerBase.this.resetTables();
                        SelfStudyControllerBase selfStudyControllerBase9 = SelfStudyControllerBase.this;
                        selfStudyControllerBase9.transitionTo(selfStudyControllerBase9.mRecognizeBeginCellState);
                        SelfStudyControllerBase.this.screenOnBeginStudy();
                        return true;
                    }
                } else if (SelfStudyControllerBase.this.currentIsEveningPeak()) {
                    if (this.isUsingOnMorningPeak) {
                        SelfStudyControllerBase.this.beginToUse();
                        this.isUsingOnMorningPeak = false;
                    }
                    if (this.isPredictingOnMorningPeak) {
                        SelfStudyControllerBase.this.beginPredict();
                        this.isPredictingOnMorningPeak = false;
                    }
                }
            } else if (SelfStudyControllerBase.this.currentIsEveningPeak()) {
                if (this.isUsingOnMorningPeak) {
                    SelfStudyControllerBase.this.beginToUse();
                    this.isUsingOnMorningPeak = false;
                }
                if (this.isPredictingOnMorningPeak) {
                    SelfStudyControllerBase.this.beginPredict();
                    this.isPredictingOnMorningPeak = false;
                }
            }
            return true;
        }
    }

    private void observeGameStatus(boolean z) {
        ContentObserver contentObserver;
        if (!z || this.mGameStatusObserver != null) {
            if (z || (contentObserver = this.mGameStatusObserver) == null) {
                return;
            }
            this.mContentResolver.unregisterContentObserver(contentObserver);
            this.mGameStatusObserver = null;
            return;
        }
        if (this.mWorkHandler == null || this.mContentResolver == null) {
            Log.w("SelfStudyController", "observeGameStatus: not initialized");
            return;
        }
        Uri uriFor = Settings.Secure.getUriFor("gb_boosting");
        ContentObserver contentObserver2 = new ContentObserver(this.mWorkHandler) { // from class: com.android.phone.performance.SelfStudyControllerBase.5
            @Override // android.database.ContentObserver
            public void onChange(boolean z2) {
                SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                selfStudyControllerBase.isInGame = Settings.Secure.getInt(selfStudyControllerBase.mContentResolver, "gb_boosting", 0) == 1;
                SelfStudyControllerBase.this.log("game status mIsInGame = " + SelfStudyControllerBase.this.isInGame);
                SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                if (!selfStudyControllerBase2.isInGame) {
                    Handler handler = selfStudyControllerBase2.mWorkHandler;
                    handler.sendMessage(handler.obtainMessage(34));
                    if (SelfStudyControllerBase.this.mWorkHandler.hasMessages(35)) {
                        SelfStudyControllerBase.this.mWorkHandler.removeMessages(35);
                    }
                    SelfStudyControllerBase.this.resetBenefitStatisticList();
                    return;
                }
                Handler handler2 = selfStudyControllerBase2.mWorkHandler;
                handler2.sendMessage(handler2.obtainMessage(35));
                if (SelfStudyControllerBase.this.mWorkHandler.hasMessages(34)) {
                    SelfStudyControllerBase.this.mWorkHandler.removeMessages(34);
                }
            }
        };
        this.mGameStatusObserver = contentObserver2;
        this.mContentResolver.registerContentObserver(uriFor, false, contentObserver2);
    }

    private void triggerCollectBadCellsAndDoAction() {
        if (this.mIsMobileDataOn) {
            if (this.mWorkHandler == null || this.mPreferenceUtils == null) {
                Log.w("SelfStudyController", "triggerCollectBadCellsAndDoAction: not initialized");
                return;
            }
            if (!this.isCellActionOn[0] && currentIsMorning()) {
                this.isCellActionOn[0] = true;
                this.mPreferenceUtils.putBoolean("action_on_morning", true);
                miSmartNetworkDoCellAction(this.mIsWorkDay, true);
                if (isCollectingBadCell) {
                    return;
                }
                beginToCollectBadCells(this.mIsWorkDay);
                return;
            }
            if (!this.isCellActionOn[1] && currentIsNonPeak()) {
                this.isCellActionOn[1] = true;
                this.mPreferenceUtils.putBoolean("action_on_non_peak", true);
                resetSmartNetworkBadCellAction();
                this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$triggerCollectBadCellsAndDoAction$0();
                    }
                }, 3000L);
                return;
            }
            if (this.isCellActionOn[2] || !currentIsEveningPeak()) {
                return;
            }
            this.isCellActionOn[2] = true;
            this.mPreferenceUtils.putBoolean("action_on_evening_peak", true);
            resetSmartNetworkBadCellAction();
            this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$triggerCollectBadCellsAndDoAction$1();
                }
            }, 3000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerCollectBadCellsAndDoAction$0() {
        miSmartNetworkDoCellAction(this.mIsWorkDay, false);
        if (isCollectingBadCell) {
            return;
        }
        beginToCollectBadCells(this.mIsWorkDay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerCollectBadCellsAndDoAction$1() {
        miSmartNetworkDoCellAction(this.mIsWorkDay, true);
        if (!isCollectingBadCell) {
            beginToCollectBadCells(this.mIsWorkDay);
        }
        this.isCellActionOn[1] = false;
        this.mPreferenceUtils.putBoolean("action_on_non_peak", false);
    }

    class SelfStudyAlarmReceiver extends BroadcastReceiver {
        SelfStudyAlarmReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            if (intent == null) {
                Log.d("SelfStudyController", "Null action");
                return;
            }
            Handler handler = SelfStudyControllerBase.this.mWorkHandler;
            if (handler == null) {
                Log.w("SelfStudyController", "SelfStudyAlarmReceiver.onReceive: mWorkHandler is null");
            } else {
                handler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$SelfStudyAlarmReceiver$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0(intent);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(Intent intent) {
            String action = intent.getAction();
            Log.d("SelfStudyController", "Receive action is  " + action);
            int i = SelfStudyControllerBase.this.getCurrentCalendar(System.currentTimeMillis()).get(11);
            for (int i2 = 0; i2 < SelfStudyControllerBase.this.studyResidentCellsAlarmAction.length; i2++) {
                if (SelfStudyControllerBase.this.studyResidentCellsAlarmAction[i2].equals(action)) {
                    SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase.mAlarmManager != null && selfStudyControllerBase.alarmPendingIntents[i2] != null) {
                        SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                        selfStudyControllerBase2.mAlarmManager.setExactAndAllowWhileIdle(0, selfStudyControllerBase2.getTimeInMillsFromCalendar(selfStudyControllerBase2.studyResidentCellsAlarmTime[i2], 0), SelfStudyControllerBase.this.alarmPendingIntents[i2]);
                    }
                    if (i != SelfStudyControllerBase.this.studyResidentCellsAlarmTime[i2]) {
                        SelfStudyControllerBase.this.log("ignore alarm receiver");
                        return;
                    }
                    if (i2 == 0) {
                        SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase3.mIsResidentCellsOptimizationSupported) {
                            selfStudyControllerBase3.startStudyCompanyResidentCells();
                            return;
                        }
                        return;
                    }
                    if (i2 == 1) {
                        SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase4.mIsResidentCellsOptimizationSupported) {
                            selfStudyControllerBase4.startStudyHomeResidentCells();
                        }
                        SelfStudyControllerBase selfStudyControllerBase5 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase5.mIsSupportLowPowerModeOptimization && selfStudyControllerBase5.cloudLowPowerOn) {
                            selfStudyControllerBase5.startStudyLowPowerModeCells();
                            SelfStudyControllerBase.this.startPingpongStudy();
                            return;
                        }
                        return;
                    }
                    if (i2 == 2) {
                        SelfStudyControllerBase selfStudyControllerBase6 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase6.mIsResidentCellsOptimizationSupported) {
                            selfStudyControllerBase6.stopStudyResidentCells(Scene.Company.value);
                            return;
                        }
                        return;
                    }
                    if (i2 != 3) {
                        return;
                    }
                    SelfStudyControllerBase selfStudyControllerBase7 = SelfStudyControllerBase.this;
                    selfStudyControllerBase7.mIsWorkDay = selfStudyControllerBase7.isWorkDay(System.currentTimeMillis());
                    SelfStudyControllerBase selfStudyControllerBase8 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase8.mIsResidentCellsOptimizationSupported) {
                        selfStudyControllerBase8.stopStudyResidentCells(Scene.Home.value);
                    }
                    SelfStudyControllerBase selfStudyControllerBase9 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase9.mIsSupportLowPowerModeOptimization && selfStudyControllerBase9.cloudLowPowerOn) {
                        selfStudyControllerBase9.stopStudyLowPowerModeCells();
                        SelfStudyControllerBase.this.stopPingpongStudy();
                        return;
                    }
                    return;
                }
            }
        }
    }

    private void regUnregAlarmReceiver(boolean z) {
        BroadcastReceiver broadcastReceiver;
        if (!z || this.mSelfStudyAlarmReceiver != null) {
            if (z || (broadcastReceiver = this.mSelfStudyAlarmReceiver) == null) {
                return;
            }
            this.mContext.unregisterReceiver(broadcastReceiver);
            this.mSelfStudyAlarmReceiver = null;
            return;
        }
        this.mSelfStudyAlarmReceiver = new SelfStudyAlarmReceiver();
        IntentFilter intentFilter = new IntentFilter();
        for (String str : this.studyResidentCellsAlarmAction) {
            intentFilter.addAction(str);
        }
        this.mContext.registerReceiver(this.mSelfStudyAlarmReceiver, intentFilter, 2);
    }

    private void regUnregResidentCellsAlarmListening(boolean z) {
        if (z) {
            for (int i = 0; i < this.studyResidentCellsAlarmAction.length; i++) {
                this.alarmPendingIntents[i] = PendingIntent.getBroadcast(this.mContext, 0, new Intent(this.studyResidentCellsAlarmAction[i]), 201326592);
                try {
                    this.mAlarmManager.setExactAndAllowWhileIdle(0, getTimeInMillsFromCalendar(this.studyResidentCellsAlarmTime[i], 0), this.alarmPendingIntents[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        if (this.mAlarmManager != null) {
            for (int i2 = 0; i2 < this.studyResidentCellsAlarmAction.length; i2++) {
                PendingIntent pendingIntent = this.alarmPendingIntents[i2];
                if (pendingIntent != null) {
                    try {
                        this.mAlarmManager.cancel(pendingIntent);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    protected void loadResidentCellIds() {
        SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
        if (selfStudyDatabaseHelper == null) {
            Log.w("SelfStudyController", "loadResidentCellIds: dbHelper is null");
            return;
        }
        List residentCellIds = selfStudyDatabaseHelper.getResidentCellIds();
        this.mResidentCellIds.clear();
        this.mResidentCellIds.addAll(residentCellIds);
    }

    private void miSmartNetworkDoCellAction(boolean z, boolean z2) {
        miSmartNetworkQueryBadCellInfoAndDoCellAction(1, z ? 1 : 0, z2 ? 1 : 0);
        miSmartNetworkQueryBadCellInfoAndDoCellAction(2, z ? 1 : 0, z2 ? 1 : 0);
    }

    private void miSmartNetworkQueryBadCellInfoAndDoCellAction(int i, int i2, int i3) {
        String[] strArr = {String.valueOf(i2), String.valueOf(i3)};
        String[] strArr2 = {"pci", "freq", "bad_cell_score", "id"};
        String str = i == 1 ? "lte_bad_cell_table_v2" : "nr_bad_cell_table_v2";
        Cursor cursorQuery = this.dbHelper.query(str, strArr2, " is_work_day = ? and is_peak = ? ", strArr, null, null, null);
        int count = cursorQuery.getCount();
        ArrayList arrayList = new ArrayList(count);
        ArrayList arrayList2 = new ArrayList(count);
        if (cursorQuery.moveToFirst()) {
            while (!cursorQuery.isAfterLast()) {
                int i4 = cursorQuery.getInt(0);
                int i5 = cursorQuery.getInt(1);
                int i6 = cursorQuery.getInt(2);
                arrayList.add(Integer.valueOf(i4));
                arrayList2.add(Integer.valueOf(i5));
                doCellAction(0L, i4, i5, i, i6);
                cursorQuery.moveToNext();
            }
        }
        cursorQuery.close();
        this.dbHelper.updateMiSmartNetworkActionCells(str, arrayList, arrayList2);
    }

    public void beginToCollectBadCells(boolean z) {
        if (isCollectingBadCell) {
            return;
        }
        isCollectingBadCell = true;
        sendSelfStudyRequest(27, 3, new int[]{1, z ? 1 : 0, getTimeZoneOffset()});
    }

    public void stopToCollectBadCells(boolean z) {
        if (isCollectingBadCell) {
            resetSmartNetworkBadCellAction();
            isCollectingBadCell = false;
            sendSelfStudyRequest(27, 3, new int[]{0, z ? 1 : 0, getTimeZoneOffset()});
        }
    }

    public CellInfo getCurrentCellInfoV3() {
        int pci;
        int nrarfcn;
        String globalCellId;
        int i;
        long nci;
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            return null;
        }
        CellIdentity currentCellIdentity = activeDataPhone.getCurrentCellIdentity();
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            pci = cellIdentityLte.getPci();
            nrarfcn = cellIdentityLte.getEarfcn();
            globalCellId = cellIdentityLte.getGlobalCellId();
            i = Rat.Lte.value;
            nci = cellIdentityLte.getCi();
        } else if (currentCellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
            pci = cellIdentityNr.getPci();
            nrarfcn = cellIdentityNr.getNrarfcn();
            globalCellId = cellIdentityNr.getGlobalCellId();
            i = Rat.Nr.value;
            nci = cellIdentityNr.getNci();
        } else {
            log("getCurrentCellInfoV3 cell type not supported, return");
            return null;
        }
        CellInfo cellInfo = new CellInfo();
        cellInfo.pci = pci;
        cellInfo.freq = nrarfcn;
        cellInfo.globalCellId = globalCellId;
        cellInfo.rat = i;
        cellInfo.cid = nci;
        return cellInfo;
    }

    public String getPlmn() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null || this.tmManager == null) {
            return "";
        }
        return this.tmManager.getNetworkOperatorForPhone(activeDataPhone.getPhoneId());
    }

    public synchronized String getCurrentTimestamp() {
        return this.mCachedTimeFormat.format(new Date());
    }

    public long getMobileTxRate(Context context) {
        if (context == null) {
            return -1L;
        }
        try {
            NetworkStats networkStatsQuerySummary = ((NetworkStatsManager) context.getSystemService("netstats")).querySummary(0, null, System.currentTimeMillis() - 1000, System.currentTimeMillis());
            long[] jArr = new long[2];
            getHistoryForUid(jArr, networkStatsQuerySummary, -1);
            long j = jArr[1];
            logV("getMobileTxRate txBytes:" + j);
            networkStatsQuerySummary.close();
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    private void getHistoryForUid(long[] jArr, NetworkStats networkStats, int i) {
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            if (i == -1 || bucket.getUid() == i) {
                if (bucket.getRxBytes() > 0) {
                    jArr[0] = jArr[0] + bucket.getRxBytes();
                }
                if (bucket.getTxBytes() > 0) {
                    jArr[1] = jArr[1] + bucket.getTxBytes();
                }
            }
        }
    }

    public void beginToCollectCellData(boolean z, int i) {
        int i2;
        if (!this.cloudStreamMediaOn || !MiuiPhoneUtils.isSupportMtkDataOptimization() || !this.mIsMobileDataOn || ((!this.mIsResidentCell && !this.mIsSubway) || ((i2 = this.dataType) != 2 && i2 != 4))) {
            log("beginToCollectCellData dataType is not match.");
            return;
        }
        if (isBeginCollectCellData || (this.mLastStopCollectTime > 0 && SystemClock.elapsedRealtime() - this.mLastStopCollectTime < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS)) {
            log("beginToCollectCellData isBegin or travel too less.");
            return;
        }
        if (this.mSampingCellMap.containsKey(Integer.valueOf(i)) && this.mSampingCellMap.get(Integer.valueOf(i)) == getCurrentCellInfoV3()) {
            log("beginToCollectCellData cell is already in map.");
            return;
        }
        log("beginToCollectCellData");
        if (this.mStartStallTime == 0) {
            this.mStartStallTime = System.currentTimeMillis();
        }
        this.mSampingCellMap.put(Integer.valueOf(i), getCurrentCellInfoV3());
        this.mCollectActionType = i;
        isBeginCollectCellData = true;
        this.mIsResidentCell = isResidentCellScene();
        this.mIsSubway = isSubwayScene();
        sendSelfStudyRequest(405, 4, new int[]{1, z ? 1 : 0, getTimeZoneOffset(), this.mCollectActionType});
        recordCount("streamMediaBeginStudyCnt", "streamMedia");
    }

    public void stopToCollectCellData(boolean z, int i) {
        Handler handler = this.mWorkHandler;
        if (handler == null) {
            Log.w("SelfStudyController", "stopToCollectCellData: mWorkHandler is null");
            return;
        }
        if (handler.hasMessages(1053)) {
            logV("remove long stall event lasted for stop.");
            this.mWorkHandler.removeMessages(1053);
        }
        if (!this.cloudStreamMediaOn || (!isBeginCollectCellData && 7 != i)) {
            logV("return stop collect cloudState:" + this.cloudStreamMediaOn + " ,isBegin:" + isBeginCollectCellData + " ,sceneType:" + i);
            return;
        }
        this.mStallDurTime = System.currentTimeMillis() - this.mStartStallTime;
        this.mStopStallTime = 0L;
        this.mStartStallTime = 0L;
        sendSelfStudyRequest(405, 4, new int[]{0, z ? 1 : 0, getTimeZoneOffset(), i});
        this.mLastStopCollectTime = SystemClock.elapsedRealtime();
        isBeginCollectCellData = false;
        this.mCollectActionType = -1;
        removeMsgForExitSceneBreak();
        recordCount("streamMediaEndStudyCnt", "streamMedia");
        log("stopToCollectCellData done isWorkDay = " + z + " , sceneType = " + i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void notifySceneChangeToModem(int i) {
        logV("notifySceneChangeToModem scene:" + i + " ,dataType:" + this.dataType);
        int i2 = !currentIsNonPeak();
        this.mIsPeak = i2;
        sendSelfStudyRequest(406, 4, new int[]{i, this.mIsWorkDay, i2, this.dataType});
        this.mNotifySceneType = i;
    }

    public void processSceneTypeChange(String str, boolean z) {
        logV("processSceneTypeChange scene:" + str + " ,state:" + z + " ,dataType:" + this.dataType);
        str.hashCode();
        switch (str) {
            case "dataTypeChange":
                if (z) {
                    if (this.mNotifySceneType == 0) {
                        if (isInCompanyScence()) {
                            notifySceneChangeToModem(2);
                        } else if (isInHomeScence()) {
                            notifySceneChangeToModem(3);
                        } else if (isSubwayScene()) {
                            notifySceneChangeToModem(1);
                        }
                    }
                    break;
                } else {
                    notifySceneChangeToModem(0);
                    break;
                }
                break;
            case "cellChange":
                if (z) {
                    int i = this.dataType;
                    if (i == 4 || i == 2) {
                        int i2 = this.mNotifySceneType;
                        if (i2 == 0) {
                            if (isInCompanyScence()) {
                                notifySceneChangeToModem(2);
                            } else if (isInHomeScence()) {
                                notifySceneChangeToModem(3);
                            } else if (isSubwayScene()) {
                                notifySceneChangeToModem(1);
                            }
                        } else if (i2 == 1) {
                            if (isInCompanyScence()) {
                                notifySceneChangeToModem(2);
                            } else if (isInHomeScence()) {
                                notifySceneChangeToModem(3);
                            }
                        }
                    }
                } else {
                    int i3 = this.mNotifySceneType;
                    if (i3 == 2 || i3 == 3) {
                        notifySceneChangeToModem(0);
                    }
                }
                if (this.mIsPeak == currentIsNonPeak()) {
                    log("notifySceneChangeToModem mNotifySceneType = " + this.mNotifySceneType + " for peak change.");
                    notifySceneChangeToModem(this.mNotifySceneType);
                    break;
                }
                break;
            case "subway":
                if (z) {
                    int i4 = this.dataType;
                    if ((i4 == 4 || i4 == 2) && this.mNotifySceneType == 0) {
                        notifySceneChangeToModem(1);
                    }
                    break;
                } else {
                    if (this.mNotifySceneType == 1) {
                        notifySceneChangeToModem(0);
                    }
                    break;
                }
                break;
        }
    }

    public void updateRecordStateForHome() {
        if (isBelowMtkDataOptVersionFive()) {
            return;
        }
        logV("updateRecordStateForHome mNotifySceneType:" + this.mNotifySceneType + " ,mStreamMediaOptimizationRst:" + this.mStreamMediaOptimizationRst);
        if (this.mNotifySceneType != 3 || this.mStreamMediaOptimizationRst) {
            return;
        }
        this.mIsNeedRecordForInHome = false;
    }

    public boolean isModemEnteredStreamScene() {
        int i = this.mLastToModemDataType;
        return i == 2 || i == 4;
    }

    public int getSceneType() {
        int i;
        boolean z;
        String topActivityName = getTopActivityName();
        String topAppName = getTopAppName();
        boolean z2 = true;
        int i2 = 0;
        if (this.mIsInWeChatVoip) {
            i = 3;
        } else if ("com.xiaomi.market".equals(topAppName)) {
            i = 7;
        } else if (this.mWhitelistAppPkgs.contains(topAppName) && !this.isInGame) {
            Handler handler = this.mWorkHandler;
            if (handler != null && handler.hasMessages(1025)) {
                this.mWorkHandler.removeMessages(1025);
            }
            i = 2;
        } else if (this.isInGame && (this.GAME_WHITELIST_PACKAGES.contains(topAppName) || STATIC_GAME_CELL_WHITELIST_PACKAGES.contains(topAppName))) {
            i = 1;
        } else if (SHORT_VIDEO_PACKAGES.contains(topAppName)) {
            i = 4;
        } else {
            i = MiuiPhoneUtils.isContainsPackage("config_webpage_video_packages", topAppName) ? 8 : 0;
        }
        String[] liveWhiteList = getLiveWhiteList();
        int length = liveWhiteList.length;
        int i3 = 0;
        while (true) {
            if (i3 >= length) {
                z = false;
                break;
            }
            String str = liveWhiteList[i3];
            if (!TextUtils.isEmpty(topActivityName) && topActivityName.contains(str)) {
                logV("getSceneType return live for activity match.");
                i = 2;
                z = true;
                break;
            }
            i3++;
        }
        String[] shortWhiteList = getShortWhiteList();
        int length2 = shortWhiteList.length;
        int i4 = 0;
        while (true) {
            if (i4 >= length2) {
                z2 = false;
                break;
            }
            String str2 = shortWhiteList[i4];
            if (!TextUtils.isEmpty(topActivityName) && topActivityName.contains(str2)) {
                logV("getSceneType return video for activity match.");
                i = 4;
                break;
            }
            i4++;
        }
        logV("getSceneType curActivityIsLive:" + z + ",curActivityIsShortVideo:" + z2 + ",type:" + i);
        if (!z && i == 2) {
            logV("getSceneType return default for live activity not match.");
            i = 0;
        }
        if (z2 || i != 4) {
            i2 = i;
        } else {
            logV("getSceneType return default for short activity not match.");
        }
        this.mIsResidentCell = isResidentCellScene();
        this.mIsSubway = isSubwayScene();
        this.dataType = i2;
        processStreamBenefitStatistic();
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processStreamBenefitStatistic() {
        int i = this.dataType;
        if (i == 4 || i == 2) {
            if (this.mWorkHandler.hasMessages(1038)) {
                return;
            }
            Handler handler = this.mWorkHandler;
            handler.sendMessage(handler.obtainMessage(1038, MdOptimizationScene.ShortVideo.value, 0));
            return;
        }
        resetBenefitStatisticList();
    }

    public void getOffSetFormModem(int i, int i2) {
        sendSelfStudyRequest(101, 3, new int[]{0, i, i2});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [com.android.phone.ManualSubCellInfo, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v15 */
    /* JADX WARN: Type inference failed for: r0v16 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v8, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.phone.CloudCellDbHelper] */
    /* JADX WARN: Type inference failed for: r2v5, types: [com.android.phone.CloudCellDbHelper] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public ManualSubCellInfo getManualCellInfoByGci() {
        Exception e;
        ?? r0;
        ?? r1;
        CellInfo currentCellInfoV3 = getCurrentCellInfoV3();
        logV("getManualCellInfoByGci cellInfo:" + currentCellInfoV3);
        ?? r2 = 0;
        r2 = 0;
        r2 = 0;
        r2 = 0;
        if (currentCellInfoV3 != null) {
            try {
                if (this.mCloudCellDbHelper != null) {
                    r0 = getPlmn() + currentCellInfoV3.cid;
                    List manualSubCellInfoByGci1 = this.mCloudCellDbHelper.getManualSubCellInfoByGci1(r0);
                    try {
                        if (manualSubCellInfoByGci1 != null && !manualSubCellInfoByGci1.isEmpty()) {
                            ManualSubCellInfo manualSubCellInfo = (ManualSubCellInfo) manualSubCellInfoByGci1.get(0);
                            this.mManualSubCellInfoByGci1 = true;
                            r0 = manualSubCellInfo;
                        } else {
                            List manualSubCellInfoByGci2 = this.mCloudCellDbHelper.getManualSubCellInfoByGci2(r0);
                            if (manualSubCellInfoByGci2 != null && !manualSubCellInfoByGci2.isEmpty()) {
                                ManualSubCellInfo manualSubCellInfo2 = (ManualSubCellInfo) manualSubCellInfoByGci2.get(0);
                                this.mManualSubCellInfoByGci1 = false;
                                r0 = manualSubCellInfo2;
                            }
                        }
                        r2 = r0;
                    } catch (Exception e2) {
                        e = e2;
                        e.printStackTrace();
                        r1 = r0;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                r0 = 0;
            }
        }
        r1 = r2;
        logV("getManualCellInfoByGci manualSubCellInfo = " + r1 + " , mManualSubCellInfoByGci1 = " + this.mManualSubCellInfoByGci1);
        return r1;
    }

    public boolean isCityFoundInCellMap() {
        CellInfo currentCellInfoV3 = getCurrentCellInfoV3();
        Location lastKnownLocation = getLastKnownLocation();
        logV("isCityFoundInCellMap cellInfo:" + currentCellInfoV3);
        boolean z = false;
        try {
            if (this.mCloudCellDbHelper == null || lastKnownLocation == null || currentCellInfoV3 == null) {
                return false;
            }
            String str = getPlmn() + currentCellInfoV3.cid;
            boolean zIsCityFoundInCellMap = this.mCloudCellDbHelper.isCityFoundInCellMap(getCityFromLocationSync(lastKnownLocation));
            if (zIsCityFoundInCellMap) {
                try {
                    List subCellInfoByGci = this.mCloudCellDbHelper.getSubCellInfoByGci(str);
                    logV("isCityFoundInCellMap subCellInfoList = " + subCellInfoByGci);
                    if (subCellInfoByGci != null && !subCellInfoByGci.isEmpty()) {
                        this.mSubCellInfo = (SubCellInfo) subCellInfoByGci.get(0);
                        logV("isCityFoundInCellMap mSubCellInfo = " + this.mSubCellInfo);
                        return zIsCityFoundInCellMap;
                    }
                } catch (Exception e) {
                    e = e;
                    z = zIsCityFoundInCellMap;
                }
            }
            return zIsCityFoundInCellMap;
        } catch (Exception e2) {
            e = e2;
        }
        e.printStackTrace();
        return z;
    }

    public SubCellInfo getSubCellInfo() {
        return this.mSubCellInfo;
    }

    public boolean isTimeInCellMap(SubCellInfo subCellInfo) {
        if (subCellInfo != null && subCellInfo.abnormalPeriod != null) {
            int i = getCurrentTimes()[0];
            for (String str : subCellInfo.abnormalPeriod.split(",")) {
                String[] strArrSplit = str.split("-");
                if (strArrSplit.length == 2) {
                    try {
                        int i2 = Integer.parseInt(strArrSplit[0].trim());
                        int i3 = Integer.parseInt(strArrSplit[1].trim());
                        if (i >= i2 && i < i3) {
                            return true;
                        }
                        if (i2 > i3 && (i >= i2 || i < i3)) {
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public boolean isProjectTypeMatched(SubCellInfo subCellInfo) {
        String str;
        String str2;
        if (subCellInfo == null || (str = subCellInfo.projectType) == null) {
            return false;
        }
        if (MiuiTelephony.IS_MTK) {
            str2 = "mtk";
        } else if (MiuiTelephony.IS_QCOM) {
            str2 = "qc";
        } else if (!MiuiTelephony.IS_XRING) {
            str2 = "others";
        } else {
            str2 = "xring";
        }
        boolean zContains = str.toLowerCase().contains(str2);
        logV("isProjectTypeMatched isMatch = " + zContains + " , matchContent = " + str2);
        return zContains;
    }

    public boolean isUpdateTimeValid(SubCellInfo subCellInfo) {
        String str;
        if (subCellInfo != null && (str = subCellInfo.updateTime) != null) {
            try {
                long jBetween = ChronoUnit.DAYS.between(Instant.ofEpochSecond(Long.parseLong(str)), Instant.now());
                logV("isUpdateTimeValid daysBetween = " + jBetween);
                return Math.abs(jBetween) <= 30;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Location getLastKnownLocation() {
        if (this.mLocationManager == null) {
            this.mLocationManager = (LocationManager) this.mContext.getSystemService("location");
        }
        try {
            Location lastKnownLocation = this.mLocationManager.getLastKnownLocation("gps");
            if (lastKnownLocation != null) {
                return lastKnownLocation;
            }
            Location lastKnownLocation2 = this.mLocationManager.getLastKnownLocation("network");
            return lastKnownLocation2 != null ? lastKnownLocation2 : this.mLocationManager.getLastKnownLocation("passive");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCityFromLocationSync(final Location location) {
        if (location == null || !Geocoder.isPresent()) {
            return "";
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final String[] strArr = {""};
        new Thread(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getCityFromLocationSync$2(location, strArr, countDownLatch);
            }
        }).start();
        try {
            countDownLatch.await(5L, TimeUnit.SECONDS);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
        logV("getCityFromLocationSync result = " + strArr[0]);
        return strArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCityFromLocationSync$2(Location location, String[] strArr, CountDownLatch countDownLatch) {
        try {
            List<Address> fromLocation = new Geocoder(this.mContext, Locale.ENGLISH).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (fromLocation != null && !fromLocation.isEmpty()) {
                strArr[0] = formatAddress(fromLocation.get(0).getLocality());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    private String formatAddress(String str) {
        if (str != null) {
            return str.contains(" ") ? str.substring(0, str.indexOf(" ")) : str;
        }
        return "";
    }

    public void collectCellDataForTest(boolean z, boolean z2) {
        int timeZoneOffset = getTimeZoneOffset();
        sendSelfStudyRequest(405, 4, new int[]{z2 ? 1 : 0, z ? 1 : 0, timeZoneOffset, 6});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processScreenStateChange(boolean z) {
        if (this.mWorkHandler == null) {
            Log.w("SelfStudyController", "processScreenStateChange: mWorkHandler is null");
            return;
        }
        if (z) {
            int sceneType = getSceneType();
            Log.d("SelfStudyController", "SCREEN_ON type stat is " + this.mLastToModemDataType + " , " + sceneType);
            if (this.mLastToModemDataType != sceneType) {
                processDelayDataAppTypeChange(sceneType, "SCREEN_ON");
                processUidAndActivityDataTypeChange(this.mLastToModemDataType, sceneType);
                this.dataType = sceneType;
                return;
            }
            removeMsgForExitSceneBreak();
            return;
        }
        Log.d("SelfStudyController", "SCREEN_OFF type stat is " + this.mLastToModemDataType);
        if (this.mLastToModemDataType != 0) {
            processDelayDataAppTypeChange(0, "SCREEN_OFF");
            processUidAndActivityDataTypeChange(this.dataType, 0);
            this.mLastDataType = this.dataType;
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1023), 30000L);
            return;
        }
        if (this.mWorkHandler.hasMessages(1043)) {
            this.mWorkHandler.removeMessages(1043);
        }
        resetBenefitStatisticList();
        removeMsgForExitSceneBreak();
    }

    public void removeMsgForExitSceneBreak() {
        log("removeMsgForExitSceneBreak");
        Handler handler = this.mWorkHandler;
        if (handler == null) {
            Log.w("SelfStudyController", "removeMsgForExitSceneBreak: mWorkHandler is null");
            return;
        }
        if (handler.hasMessages(1025)) {
            this.mWorkHandler.removeMessages(1025);
        }
        if (this.mWorkHandler.hasMessages(1023)) {
            this.mWorkHandler.removeMessages(1023);
        }
        if (this.mWorkHandler.hasMessages(1041)) {
            this.mWorkHandler.removeMessages(1041);
        }
    }

    private int getTimeZoneOffset() {
        return TimeZone.getDefault().getRawOffset() / v.d;
    }

    public void gameLastDelayLevel(int i) {
        this.mGameLastDelayLevel = i;
    }

    public boolean isGameSetOffsetFromModem(boolean z) {
        this.isGameSetOffsetFromModem = z;
        return z;
    }

    public void recordGameDelayLevelDuration(long j, int i, boolean z) {
        if (shouldDoActionForGameOpt()) {
            if (isEnterGameOptimizer() || z) {
                if (i == -1 || i == 0) {
                    recordDuration("gameResidentCellOpt_level_0", j, "game");
                    return;
                }
                if (i == 1) {
                    recordDuration("gameResidentCellOpt_level_1", j, "game");
                } else if (i == 2) {
                    recordDuration("gameResidentCellOpt_level_2", j, "game");
                } else {
                    if (i != 3) {
                        return;
                    }
                    recordDuration("gameResidentCellOpt_level_3", j, "game");
                }
            }
        }
    }

    protected void setGameSceneProperty(boolean z) {
        SystemProperties.set("gsm.game.scene", z ? "1" : "0");
    }

    public void enterOptimizationForModemGameResidentCell() {
        Handler handler = this.mWorkHandler;
        if (handler == null) {
            Log.w("SelfStudyController", "enterOptimizationForModemGameResidentCell: mWorkHandler is null");
        } else {
            handler.obtainMessage(1038, MdOptimizationScene.Game.value, 0).sendToTarget();
        }
    }

    public void resetBenefitStatisticList() {
        for (int i = 0; i < 10; i++) {
            this.durationStatistic[i] = 0;
        }
        Handler handler = this.mWorkHandler;
        if (handler != null) {
            handler.removeMessages(1038);
            this.mWorkHandler.removeMessages(1039);
        }
        this.isWechatOpt = false;
        this.isVideoOpt = false;
        this.isGameAiOpt = false;
    }

    public class SelfStudyPreferenceUtils {
        private final int MODE = 0;
        private final SharedPreferences mSp;

        public SelfStudyPreferenceUtils(SelfStudyControllerBase selfStudyControllerBase, Context context) {
            this.mSp = context.getSharedPreferences("self_study", 0);
        }

        public void resetAllStudyStatus() {
            remove(Arrays.asList("user_enable_study", "study_timer"));
        }

        public void remove(List list) {
            SharedPreferences.Editor editorEdit = this.mSp.edit();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                editorEdit.remove((String) it.next());
            }
            editorEdit.apply();
        }

        public int getInt(String str, int i) {
            return this.mSp.getInt(str, i);
        }

        public void putInt(String str, int i) {
            this.mSp.edit().putInt(str, i).apply();
        }

        public boolean getBoolean(String str, boolean z) {
            return this.mSp.getBoolean(str, z);
        }

        public void putBoolean(String str, boolean z) {
            this.mSp.edit().putBoolean(str, z).apply();
        }

        public float getFloat(String str, float f) {
            return this.mSp.getFloat(str, f);
        }

        public void putFloat(String str, float f) {
            this.mSp.edit().putFloat(str, f).apply();
        }

        public long getLong(String str, long j) {
            return this.mSp.getLong(str, j);
        }

        public void putLong(String str, long j) {
            this.mSp.edit().putLong(str, j).apply();
        }

        public String getString(String str, String str2) {
            return this.mSp.getString(str, str2);
        }

        public void putString(String str, String str2) {
            this.mSp.edit().putString(str, str2).apply();
        }
    }

    public void registerGameDelayListener(GameDelayListener gameDelayListener) {
        if (gameDelayListener == null) {
            return;
        }
        synchronized (this) {
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
        synchronized (this) {
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

    protected void notifyGameDelay(boolean z) {
        int size = this.mGameDelayListeners.size();
        for (int i = 0; i < size; i++) {
            ((GameDelayListener) this.mGameDelayListeners.get(i)).onGameDelayChanged(z);
        }
    }

    class SelfStudyCloudChangeListener implements BaseEvent.IEventListener {
        private SelfStudyCloudChangeListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            SelfStudyControllerBase.this.mWorkHandler.sendEmptyMessage(1017);
        }
    }

    class HydraSceneListener implements BaseEvent.IEventListener {
        private HydraSceneListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            Bundle bundle;
            if ((baseEvent instanceof ModemEvent) && (bundle = ((ModemEvent) baseEvent).getBundle()) != null) {
                int i = bundle.getInt("sceneType", -1);
                boolean z = bundle.getInt("isEnter", 0) == 1;
                String string = bundle.getString("sceneName", "");
                Log.d("SelfStudyController", "HydraSceneListener sceneType = " + i + " , isEnter = " + z + " , sceneName = " + string);
                switch (i) {
                    case 10000001:
                        SelfStudyControllerBase.this.mIsSubway = z;
                        SelfStudyControllerBase.this.mSubwayName = string;
                        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                            SelfStudyControllerBase.this.processSceneTypeChange("subway", z);
                        }
                        break;
                    case 10000006:
                        SelfStudyControllerBase.this.mIsHome = z;
                        break;
                    case 10000007:
                        SelfStudyControllerBase.this.mIsCompany = z;
                        break;
                }
            }
        }
    }

    protected void initCommon() {
        this.dbHelper = new SelfStudyDatabaseHelper(this.mContext);
        this.mPreferenceUtils = new SelfStudyPreferenceUtils(this, this.mContext);
        this.mContentResolver = this.mContext.getContentResolver();
        registerObserver();
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        cancelStudyOvertimeAlarmClockIfHas();
        registerTelephony();
        this.mUserSettingOn = true;
        this.mFeatureEnable = PreferenceUtils.getInstance().getBooleanPreference("TelephonySelfStudyFeatureEnable", Boolean.TRUE).booleanValue();
        this.mFiveGCloudController = FiveGCloudController.getInstance();
        this.mSelfStudyCloudChangeListener = new SelfStudyCloudChangeListener();
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", this.mSelfStudyCloudChangeListener, this.mWorkHandler);
        if (this.mFeatureEnable) {
            regUnregReceiver(this.mUserSettingOn);
            regUnregSensorStateListener(this.mUserSettingOn);
            SHORT_VIDEO_PACKAGES.add(HyperOSCustFeatureResolve.getString("config_data_stall_short_video_app_packages_v1", ""));
            regUnregAppStateMonitor(this.mUserSettingOn);
            observeGameStatus(this.mUserSettingOn);
        }
        initMachineState(this.mFeatureEnable);
        this.tmManager = TelephonyManager.from(this.mContext);
        this.mCm = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        this.mLastStopDataStallTime = jElapsedRealtime;
        this.mLastStartDataStallTime = jElapsedRealtime;
        this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$initCommon$3();
            }
        });
        this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$initCommon$4();
            }
        }, MirrorDesktopHelper.TIMEOUT_MILLIS);
        reloadVideoStatisticFromSp();
        if (MiuiPhoneUtils.isSupportMtkDataOptimization() || MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) {
            this.mCloudCellDbHelper = CloudCellDbHelper.getInstance(this.mContext);
            this.mLocationManager = (LocationManager) this.mContext.getSystemService("location");
            reloadGameResidentCellStatisticFromSp();
            reloadStreamMediaStatisticFromSp();
        }
        this.mDdsPhoneId = DefaultSimManager.getDefaultDataSlotId();
        if (MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) {
            reloadGameResidentCellStatisticFromSp();
            observeCellularNetworkSwitchStatus(true);
            if (isCellularNetworkSwitchStillOn()) {
                registerGameBoosterGameDelayListener();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initCommon$3() {
        this.mIsResidentCellsOptimizationSupported = MiuiPhoneUtils.isResidentCellsOptimizationSupported();
        this.mIsSupportMtkVideoSbpOptimization = MiuiPhoneUtils.isSupportMtkVideoSbpOptimization();
        this.mIsSupportLowPowerModeOptimization = MiuiPhoneUtils.isSupportLowPowerModeOptimization();
        startAlarmToUnregScreenStatusReceiver(this.mFeatureEnable);
        regUnregForModemReset(true);
        regUnregAlarmReceiver(this.mFeatureEnable);
        regUnregResidentCellsAlarmListening(this.mFeatureEnable);
        if (this.mPreferenceUtils.getInt("study_resident_cells_days", 0) >= 2) {
            loadResidentCellIds();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initCommon$4() {
        mDefaultRealTimeOptimizationStatus = this.mContext.getResources().getInteger(R.integer.config_real_time_optimization_bit_mask);
        if (!isCellularNetworkSwitchOn()) {
            mDefaultRealTimeOptimizationStatus &= -3;
        }
        log("self init mask is " + mDefaultRealTimeOptimizationStatus);
        sendSelfStudyRequest(100, 1, new int[]{mDefaultRealTimeOptimizationStatus});
    }

    public void changeModemGameFeatureStatus(boolean z) {
        if (z) {
            mDefaultRealTimeOptimizationStatus |= 2;
        } else {
            mDefaultRealTimeOptimizationStatus &= -3;
        }
        sendSelfStudyRequest(100, 1, new int[]{mDefaultRealTimeOptimizationStatus});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void regUnregSensorStateListener(boolean z) {
        SelfStudySensorStateListener selfStudySensorStateListener;
        TelephonySensorManager telephonySensorManager = TelephonySensorManager.getInstance();
        if (z && this.mSelfStudySensorStateListener == null && telephonySensorManager != null) {
            SelfStudySensorStateListener selfStudySensorStateListener2 = new SelfStudySensorStateListener();
            this.mSelfStudySensorStateListener = selfStudySensorStateListener2;
            telephonySensorManager.registerSensorStateListener(selfStudySensorStateListener2);
        } else {
            if (z || (selfStudySensorStateListener = this.mSelfStudySensorStateListener) == null || telephonySensorManager == null) {
                return;
            }
            telephonySensorManager.unregisterSensorStateListener(selfStudySensorStateListener);
            this.mSelfStudySensorStateListener = null;
        }
    }

    private void setAlarmForMiSmartNetwork(boolean z) {
        AlarmManager alarmManager = this.mAlarmManager;
        if (alarmManager == null) {
            Log.w("SelfStudyController", "setAlarmForMiSmartNetwork: mAlarmManager is null");
            return;
        }
        if (z) {
            try {
                this.startListenScreenStatusPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("over_time_start_listen_screen_status"), 201326592);
                this.mAlarmManager.setExactAndAllowWhileIdle(0, getTimeInMillsFromCalendar(23, 0), this.startListenScreenStatusPendingIntent);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        PendingIntent pendingIntent = this.startListenScreenStatusPendingIntent;
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            this.startListenScreenStatusPendingIntent = null;
        }
    }

    private void startAlarmToUnregScreenStatusReceiver(boolean z) {
        AlarmManager alarmManager = this.mAlarmManager;
        if (alarmManager == null) {
            Log.w("SelfStudyController", "startAlarmToUnregScreenStatusReceiver: mAlarmManager is null");
            return;
        }
        if (z) {
            try {
                this.stopListenScreenStatusPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("over_time_stop_listen_screen_status"), 201326592);
                this.mAlarmManager.setExactAndAllowWhileIdle(0, getTimeInMillsFromCalendar(10, 30), this.stopListenScreenStatusPendingIntent);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        PendingIntent pendingIntent = this.stopListenScreenStatusPendingIntent;
        if (pendingIntent == null || alarmManager == null) {
            return;
        }
        alarmManager.cancel(pendingIntent);
        this.stopListenScreenStatusPendingIntent = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelStudyOvertimeAlarmClockIfHas() {
        if (this.mAlarmManager == null) {
            Log.w("SelfStudyController", "cancelStudyOvertimeAlarmClockIfHas: mAlarmManager is null");
            return;
        }
        try {
            this.mAlarmManager.cancel(PendingIntent.getBroadcast(this.mContext, 0, new Intent("study_over_time_intent"), 201326592));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected long getTimeInMillsFromCalendar(int i, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long jCurrentTimeMillis = System.currentTimeMillis();
        calendar.set(11, i);
        calendar.set(12, i2);
        calendar.set(13, 0);
        calendar.set(14, 0);
        if (jCurrentTimeMillis > calendar.getTimeInMillis()) {
            calendar.add(5, 1);
        }
        return calendar.getTimeInMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void regUnregForModemReset(boolean z) {
        if (this.mWorkHandler == null) {
            Log.w("SelfStudyController", "regUnregForModemReset: mWorkHandler is null");
            return;
        }
        Phone phone = MiuiPhoneUtils.getPhone(0);
        if (phone != null) {
            CommandsInterface commandsInterface = phone.mCi;
            if (z) {
                commandsInterface.registerForModemReset(this.mWorkHandler, 1020, (Object) null);
            } else {
                commandsInterface.unregisterForModemReset(this.mWorkHandler);
            }
        }
    }

    private void initMachineState(boolean z) {
        if (z) {
            this.mRecognizeBeginCellState = new RecognizeBeginCellState();
            this.mStudyState = new StudyState();
            this.mUsingState = new UsingState();
            addState(this.mRecognizeBeginCellState);
            addState(this.mStudyState);
            addState(this.mUsingState);
            int initStateFromSharedPreferences = getInitStateFromSharedPreferences();
            if (initStateFromSharedPreferences == 1) {
                setInitialState(this.mRecognizeBeginCellState);
            } else if (initStateFromSharedPreferences == 3) {
                setInitialState(this.mStudyState);
            } else if (initStateFromSharedPreferences == 4) {
                setInitialState(this.mUsingState);
            }
            start();
            return;
        }
        if (this.mRecognizeBeginCellState == null || this.mStudyState == null || this.mUsingState == null) {
            return;
        }
        try {
            quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getInitStateFromSharedPreferences() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "getInitStateFromSharedPreferences: mPreferenceUtils is null");
            return 1;
        }
        return selfStudyPreferenceUtils.getInt("init_machine_state", 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveMachineState(int i) {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "saveMachineState: mPreferenceUtils is null");
        } else {
            selfStudyPreferenceUtils.putInt("init_machine_state", i);
        }
    }

    protected void regUnregAppStateMonitor(boolean z) {
        MiuiAppStateMonitor miuiAppStateMonitor;
        if (!z || this.mMiuiAppStateMonitor != null) {
            if (z || (miuiAppStateMonitor = this.mMiuiAppStateMonitor) == null) {
                return;
            }
            miuiAppStateMonitor.unregisterAppObserver(this.mCurrentTopAppObserver);
            this.mMiuiAppStateMonitor = null;
            return;
        }
        this.mPackageManager = this.mContext.getPackageManager();
        this.mWhitelistAppPkgs.clear();
        this.mWhitelistAppPkgs = new HashSet(Arrays.asList(this.LIVE_STREAMING_PKGS));
        MiuiAppStateMonitor miuiAppStateMonitorInit = MiuiAppStateMonitor.init();
        this.mMiuiAppStateMonitor = miuiAppStateMonitorInit;
        miuiAppStateMonitorInit.registerAppObserver(this.mCurrentTopAppObserver);
    }

    public void updateCloudSelfStudyFeatureStatus() {
        MiuiSettings.SettingsCloudData.CloudData cloudData;
        boolean z;
        FiveGCloudController fiveGCloudController = this.mFiveGCloudController;
        if (fiveGCloudController == null) {
            Log.w("SelfStudyController", "updateCloudSelfStudyFeatureStatus: mFiveGCloudController is null");
            return;
        }
        Iterator it = fiveGCloudController.fetchSelfStudyCloudStatus().iterator();
        while (it.hasNext() && (cloudData = (MiuiSettings.SettingsCloudData.CloudData) it.next()) != null) {
            int i = cloudData.getInt("type", SelfStudyCloudType.Default.value);
            if (i == SelfStudyCloudType.Game.value) {
                notifyCloudDataChanged(cloudData);
                if (MiuiPhoneUtils.isSupportMtkDataOptimization() || MiuiPhoneUtils.isSupportQcGameResidentCellOptimization() || MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) {
                    int i2 = cloudData.getInt("gameResidentOptStatus", 1);
                    z = i2 != 0;
                    if (z != this.cloudGameResidentOn) {
                        this.cloudGameResidentOn = z;
                        Log.d("SelfStudyController", " gameResidentOn= " + this.cloudGameResidentOn + " gameResidentStatus: " + i2 + ", cloudStatus: " + z);
                        return;
                    }
                    return;
                }
                return;
            }
            if (i == SelfStudyCloudType.LowPower.value) {
                if (MiuiPhoneUtils.isSupportLowPowerModeOptimization()) {
                    z = cloudData.getInt("lowPowerOptStatus", 1) != 0;
                    if (z != this.cloudLowPowerOn) {
                        this.cloudLowPowerOn = z;
                        Log.d("SelfStudyController", "cloudLowPowerOn= " + this.cloudLowPowerOn);
                        if (this.cloudLowPowerOn) {
                            return;
                        }
                        resetLowPowerOptByCloud();
                        return;
                    }
                    return;
                }
                return;
            }
            if (i == SelfStudyCloudType.call.value) {
                int i3 = cloudData.getInt("callMessageOptStatus", 1);
                boolean z2 = i3 != 0;
                if (z2 != this.cloudCallMessageOn) {
                    this.cloudCallMessageOn = z2;
                    Log.d("SelfStudyController", " cloudCallMessageOn= " + this.cloudCallMessageOn + " callMessageStatus: " + i3 + ", cloudStatus: " + z2);
                }
            }
            if (i == SelfStudyCloudType.WechatOptimization.value) {
                z = cloudData.getInt("wechatOptStatus", 1) != 0;
                if (z != this.cloudWechatOn) {
                    this.cloudWechatOn = z;
                    return;
                }
                return;
            }
            if (i == SelfStudyCloudType.StreamMedia.value) {
                z = cloudData.getInt("streamMediaOptStatus", 1) != 0;
                if (z != this.cloudStreamMediaOn) {
                    this.cloudStreamMediaOn = z;
                    return;
                }
                return;
            }
            if (i == SelfStudyCloudType.VideoOptimization.value) {
                int i4 = cloudData.getInt("videoOptStatus", 1);
                String string = cloudData.getString("SimOperator", "NA");
                z = i4 != 0;
                if (!FiveGCloudUtils.simOperatorMatched(string) || z == this.cloudVideoOn) {
                    return;
                }
                this.cloudVideoOn = z;
                return;
            }
            if (i == SelfStudyCloudType.StreamWhiteList.value) {
                processStreamWhiteListFromCloudChange(cloudData);
            }
            notifyModemCloudDataChanged(cloudData);
            boolean z3 = cloudData.getInt("STATUS", 1) != 0;
            if (z3 != this.mFeatureEnable) {
                this.mFeatureEnable = z3;
                PreferenceUtils.getInstance().setBooleanPreference("TelephonySelfStudyFeatureEnable", this.mFeatureEnable);
                if (!this.mFeatureEnable) {
                    stopSelfStudyByCloudController();
                    regUnregAppStateMonitor(false);
                    regUnregReceiver(false);
                    regUnregSensorStateListener(false);
                    observeGameStatus(false);
                    if (getInitStateFromSharedPreferences() == 3) {
                        regUnregForModemReset(false);
                    }
                    saveMachineState(1);
                } else if (this.mUserSettingOn) {
                    regUnregReceiver(true);
                    regUnregSensorStateListener(true);
                    regUnregAppStateMonitor(true);
                    observeGameStatus(true);
                }
                initMachineState(this.mFeatureEnable);
                if (this.isSupportCellAction) {
                    setAlarmForMiSmartNetwork(this.mFeatureEnable);
                }
                startAlarmToUnregScreenStatusReceiver(this.mFeatureEnable);
                regUnregAlarmReceiver(this.mFeatureEnable);
                regUnregResidentCellsAlarmListening(this.mFeatureEnable);
            }
        }
    }

    private void stopSelfStudyByCloudController() {
        if (isStudying) {
            stopStudy();
        } else {
            stopToUse();
            stopPredict();
        }
        if (this.isSupportCellAction) {
            stopToCollectBadCells(this.mIsWorkDay);
        }
        Handler handler = this.mWorkHandler;
        if (handler != null && this.mPreferenceUtils != null) {
            handler.postDelayed(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$stopSelfStudyByCloudController$5();
                }
            }, 3000L);
        } else {
            Log.w("SelfStudyController", "stopSelfStudyByCloudController: mWorkHandler or mPreferenceUtils is null");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopSelfStudyByCloudController$5() {
        removeBeginCellStatus();
        this.mPreferenceUtils.resetAllStudyStatus();
        resetRouteStudyJudgeCell();
        resetTables();
    }

    private void notifyModemCloudDataChanged(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        if (cloudData == null) {
            return;
        }
        sendSelfStudyRequest(100, 1, new int[]{cloudData.getInt("real_time_optimization_status", mDefaultRealTimeOptimizationStatus)});
        int[] iArr = {cloudData.getInt("NR_BAD_RSRP", 51), cloudData.getInt("NR_BAD_RSRQ", 61), cloudData.getInt("LTE_BAD_RSRP", 25), cloudData.getInt("LTE_BAD_RSRQ", 15), cloudData.getInt("CLOUD_KEY_NR_UL_BLER", 60), cloudData.getInt("LTE_UL_BLER", 30), cloudData.getInt("LIVE_STREAMING_SPEED", 200), cloudData.getInt("GAME_SPEED", 25)};
        this.cellOffset = cloudData.getInt("offset", -2);
        this.max_lte_offset = cloudData.getInt("MAX_LTE_OFFSET", -9);
        this.max_nr_offset = cloudData.getInt("MAX_NR_OFFSET", -6);
        sendSelfStudyRequest(25, 8, iArr);
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils != null) {
            selfStudyPreferenceUtils.putInt("NR_BAD_RSRP", iArr[0]);
            this.mPreferenceUtils.putInt("NR_BAD_RSRQ", iArr[1]);
            this.mPreferenceUtils.putInt("LTE_BAD_RSRP", iArr[2]);
            this.mPreferenceUtils.putInt("LTE_BAD_RSRQ", iArr[3]);
            this.mPreferenceUtils.putInt("CLOUD_KEY_NR_UL_BLER", iArr[4]);
            this.mPreferenceUtils.putInt("LTE_UL_BLER", iArr[5]);
            this.mPreferenceUtils.putInt("LIVE_STREAMING_SPEED", iArr[6]);
            this.mPreferenceUtils.putInt("GAME_SPEED", iArr[7]);
            this.mPreferenceUtils.putInt("offset", this.cellOffset);
            this.mPreferenceUtils.putInt("MAX_LTE_OFFSET", this.max_lte_offset);
            this.mPreferenceUtils.putInt("MAX_NR_OFFSET", this.max_nr_offset);
        }
    }

    private void processStreamWhiteListFromCloudChange(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        if (!MiuiPhoneUtils.isSupportMtkDataOptimization() || isBelowMtkDataOptVersionFive() || cloudData == null) {
            return;
        }
        String string = cloudData.getString("cloudShortWhiteList", "");
        String string2 = cloudData.getString("cloudLiveWhiteList", "");
        this.mCloudShortWhiteList = string.split(",");
        this.mCloudLiveWhiteList = string2.split(",");
        logV("ShortWhiteList: " + Arrays.toString(this.mCloudShortWhiteList) + ",LiveWhiteList:" + Arrays.toString(this.mCloudLiveWhiteList));
        if (this.dataType != 0) {
            mIsNeedUpdateWhiteList = true;
        } else {
            mIsNeedUpdateWhiteList = false;
            registerActivityChangeListener(1);
        }
    }

    public void processWhiteListForSceneExit() {
        if (!isBelowMtkDataOptVersionFive() && MiuiPhoneUtils.isSupportMtkDataOptimization() && mIsNeedUpdateWhiteList) {
            mIsNeedUpdateWhiteList = false;
            registerActivityChangeListener(2);
        }
    }

    public int calculateBadCellPoints(CellInfo cellInfo) {
        if (cellInfo.getDuration() <= 5) {
            return 0;
        }
        int i = (cellInfo.getLowUlGrantCount() * 25) / cellInfo.getDuration() >= 20 ? 1 : 0;
        if ((cellInfo.getHighBlerCount() * 100) / cellInfo.getDuration() >= 30) {
            i++;
        }
        if ((cellInfo.getBadRsrpDuration() * 100) / cellInfo.getDuration() >= 30) {
            i++;
        }
        return (cellInfo.getLowDlGrantCount() * 100) / cellInfo.getDuration() >= 10 ? i + 1 : i;
    }

    protected void restoreOrUpdate(List list) {
        String[] strArr = {"rrc_exception", "high_bler_count", "low_ul_grant", "rlf_count", "bad_rsrp_duration", "duration", "is_bad_cell", "low_dl_grant"};
        Iterator it = list.iterator();
        while (it.hasNext()) {
            CellInfo cellInfo = (CellInfo) it.next();
            Log.d("SelfStudyController", cellInfo.toString());
            Cursor cursorQuery = this.dbHelper.query("cell_table", strArr, "cid = ? and pci = ? and freq = ? and rat = ?", new String[]{String.valueOf(cellInfo.getCid()), String.valueOf(cellInfo.getPci()), String.valueOf(cellInfo.getFreq()), String.valueOf(cellInfo.getRat())}, null, null, null);
            if (cursorQuery != null) {
                if (cursorQuery.moveToFirst()) {
                    int i = cursorQuery.getInt(0);
                    int i2 = cursorQuery.getInt(1);
                    int i3 = cursorQuery.getInt(2);
                    int i4 = cursorQuery.getInt(3);
                    int i5 = cursorQuery.getInt(4);
                    int i6 = cursorQuery.getInt(5);
                    cellInfo.setRrcExceptionCount(i + cellInfo.getRrcExceptionCount());
                    cellInfo.setHighBlerCount(i2 + cellInfo.getHighBlerCount());
                    cellInfo.setLowUlGrantCount(i3 + cellInfo.getLowUlGrantCount());
                    cellInfo.setRlfCount(i4 + cellInfo.getRlfCount());
                    cellInfo.setDuration(i6 + cellInfo.getDuration());
                    cellInfo.setBadRsrpDuration(i5 + cellInfo.getBadRsrpDuration());
                    cellInfo.setIsBadCell(cursorQuery.getInt(6) + cellInfo.getIsBadCell());
                    cellInfo.setLowDlGrantCount(cursorQuery.getInt(7) + cellInfo.getLowDlGrantCount());
                    doUpdateCellInfo(cellInfo);
                } else {
                    doRecordCellInfo(cellInfo);
                }
                cursorQuery.close();
            } else {
                doRecordCellInfo(cellInfo);
            }
        }
    }

    public void doRecordCellInfo(CellInfo cellInfo) {
        SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
        if (selfStudyDatabaseHelper == null) {
            Log.w("SelfStudyController", "doRecordCellInfo: dbHelper is null");
        } else {
            selfStudyDatabaseHelper.insert("cell_table", cellInfo);
        }
    }

    public void doUpdateCellInfo(CellInfo cellInfo) {
        if (this.dbHelper == null) {
            Log.w("SelfStudyController", "doUpdateCellInfo: dbHelper is null");
            return;
        }
        String[] strArr = {String.valueOf(cellInfo.getCid()), String.valueOf(cellInfo.getPci()), String.valueOf(cellInfo.getFreq()), String.valueOf(cellInfo.getRat())};
        this.dbHelper.update("cell_table", constructionContentValues(cellInfo), "cid = ? and pci = ? and freq = ? and rat = ?", strArr);
    }

    public ContentValues constructionContentValues(CellInfo cellInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("cid", Long.valueOf(cellInfo.getCid()));
        contentValues.put("freq", Integer.valueOf(cellInfo.getFreq()));
        contentValues.put("pci", Integer.valueOf(cellInfo.getPci()));
        contentValues.put("rat", Integer.valueOf(cellInfo.getRat()));
        contentValues.put("scs", Byte.valueOf(cellInfo.getScs()));
        contentValues.put("is_bad_cell", Integer.valueOf(cellInfo.getIsBadCell()));
        contentValues.put("current_cell_count", Integer.valueOf(cellInfo.getCurrentCellCount()));
        contentValues.put("rrc_exception", Integer.valueOf(cellInfo.getRrcExceptionCount()));
        contentValues.put("high_bler_count", Integer.valueOf(cellInfo.getHighBlerCount()));
        contentValues.put("low_ul_grant", Integer.valueOf(cellInfo.getLowUlGrantCount()));
        contentValues.put("rlf_count", Integer.valueOf(cellInfo.getRlfCount()));
        contentValues.put("bad_rsrp_duration", Integer.valueOf(cellInfo.getBadRsrpDuration()));
        contentValues.put("bad_rsrq_duration", Integer.valueOf(cellInfo.getBadRsrqDuration()));
        contentValues.put("duration", Integer.valueOf(cellInfo.getDuration()));
        contentValues.put("low_dl_grant", Integer.valueOf(cellInfo.getLowDlGrantCount()));
        contentValues.put("data_type", Integer.valueOf(cellInfo.getDataType()));
        return contentValues;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notify3rdAppOpt() {
        if (this.mTrigged3rdTime > 0 && SystemClock.elapsedRealtime() - this.mTrigged3rdTime <= 120000) {
            log("notify3rdAppOpt not support or frequent trigger");
            return;
        }
        if (MiuiPhoneUtils.isTopAppForFeatureConfig("config_self_study_data_stall_preload_app_list")) {
            this.mTrigged3rdTime = SystemClock.elapsedRealtime();
            recordSelfStudyFor3rdOrQms(1);
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().videoAppsPolicyNotify(1, -1, -1, VideoPreloadScene.SELF_STUDY.getValue());
                return;
            }
            return;
        }
        if (MiuiPhoneUtils.isTopAppForFeatureConfig("config_self_study_data_stall_low_resolution_app_list")) {
            this.mTrigged3rdTime = SystemClock.elapsedRealtime();
            int iNextInt = new Random().nextInt(10) + 1;
            recordSelfStudyFor3rdOrQms(2);
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().videoAppsPolicyNotify(2, iNextInt, -1, VideoPreloadScene.SELF_STUDY.getValue());
            }
        }
    }

    public void recordSelfStudyFor3rdOrQms(int i) {
        HashMap map = new HashMap();
        map.put("SelfStudyType", Integer.valueOf(i));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_selfstudy_mode_statistic", map);
    }

    public void recordResidentCellOptimizationBenefit(float f, float f2, float f3, List list) {
        HashMap map = new HashMap();
        map.put("SelfStudyType", 4);
        map.put("SelfStudyDataBenefit", Float.valueOf(f));
        map.put("SelfStudyDataCountBenefit", Float.valueOf(f2));
        map.put("SelfStudyCallCountBenefit", Float.valueOf(f3));
        map.put("Params", list);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_selfstudy_mode_statistic", map);
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

    public String getTopActivityName() {
        ActivityManager activityManager = (ActivityManager) this.mContext.getSystemService("activity");
        if (activityManager == null) {
            log("getTopActivityName activityManager is null");
            return "";
        }
        try {
            if (activityManager.getRunningTasks(1) != null && activityManager.getRunningTasks(1).get(0) != null) {
                return activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            }
        } catch (Exception e) {
            log("getTopActivityName exception:" + e.toString());
        }
        return "";
    }

    public boolean isColdStart(String str) {
        MiuiAppStateMonitor miuiAppStateMonitor = this.mMiuiAppStateMonitor;
        if (miuiAppStateMonitor != null) {
            MiuiAppStateMonitor.ForegroundAppInfo foregroundAppInfo = miuiAppStateMonitor.getForegroundAppInfo();
            logV("isColdStart foregroundAppInfo:" + foregroundAppInfo);
            if (foregroundAppInfo != null && foregroundAppInfo.mPackageName.equalsIgnoreCase(str) && foregroundAppInfo.mIsCold) {
                return true;
            }
        }
        return false;
    }

    public boolean isSameCellRouteInfo(CellRouteInfo cellRouteInfo, CellRouteInfo cellRouteInfo2) {
        return cellRouteInfo.cellRat == cellRouteInfo2.cellRat && cellRouteInfo.cellPci == cellRouteInfo2.cellPci && cellRouteInfo.cellFreq == cellRouteInfo2.cellFreq;
    }

    public void updateRatChangedCount(int i) {
        mRatChangeCount = i;
        Log.d("SelfStudyController", "updateRatChangedCount mRatChangeCount is " + mRatChangeCount);
    }

    public void notifyUser() {
        stopStudy();
    }

    public void stopStudy() {
        if (isStudying) {
            cancelStudyOvertimeAlarmClockIfHas();
            if (this.hasRegistStudyCallback) {
                unregisterCallBack(this.studyCallBack_V2);
                this.hasRegistStudyCallback = false;
            }
            try {
                this.mTimer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateDatas(true);
            Log.d("SelfStudyController", "end study");
            sendSelfStudyRequest(6, 1, new byte[]{0});
        }
    }

    public void judgeOrStoreRoute() {
        String string;
        int i;
        SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
        if (selfStudyDatabaseHelper == null) {
            Log.w("SelfStudyController", "judgeOrStoreRoute: dbHelper is null");
            return;
        }
        Cursor cursorQuery = selfStudyDatabaseHelper.query("cell_route_table", new String[]{"id", "compare_count", "route"}, null, null, null, null, null);
        if (cursorQuery != null && cursorQuery.moveToFirst()) {
            i = cursorQuery.getInt(1);
            string = cursorQuery.getString(2);
        } else {
            string = "";
            i = 0;
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        Log.d("SelfStudyController", "judgeOrStoreRoute---route is " + string);
        List dBRouteList = getDBRouteList();
        if (dBRouteList.size() > 0) {
            compareRoute(dBRouteList, i);
        } else {
            insertOrUpdateRouteList(true, 0);
        }
    }

    public List getDBRouteList() {
        SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
        if (selfStudyDatabaseHelper == null) {
            Log.w("SelfStudyController", "getDBRouteList: dbHelper is null");
            return new ArrayList();
        }
        return selfStudyDatabaseHelper.getDBRouteList(encryptedKey);
    }

    public void compareRoute(List list, int i) {
        Log.d("SelfStudyController", "compareRoute");
        if (this.allCellRouteList.size() > list.size() && i < 5) {
            insertOrUpdateRouteList(false, i + 1);
            return;
        }
        this.routeIndex = 0;
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (hasIn((List) this.allCellRouteList.get(this.routeIndex), (List) list.get(i2))) {
                arrayList = list.subList(0, i2);
                arrayList2 = list.subList(i2 + 1, list.size());
                this.routeIndex++;
                break;
            }
        }
        Log.d("SelfStudyController", "compareRoute routeIndex is " + this.routeIndex);
        if (this.routeIndex >= this.allCellRouteList.size()) {
            this.isSame = true;
        }
        if (!this.isSame) {
            searchCompare(arrayList2, (List) this.allCellRouteList.get(this.routeIndex), true);
        }
        if (!this.isSame) {
            searchCompare(arrayList, (List) this.allCellRouteList.get(this.routeIndex), false);
        }
        if (this.isSame || i >= 5) {
            return;
        }
        insertOrUpdateRouteList(false, i + 1);
    }

    public void searchCompare(List list, List list2, boolean z) {
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        if (list.size() <= 0) {
            return;
        }
        if (list.size() == 1) {
            this.isSame = hasIn(list2, (List) list.get(0));
            Log.d("SelfStudyController", "searchCompare isSame is " + this.isSame);
            return;
        }
        if (z) {
            for (int i = 0; i < list.size(); i++) {
                if (hasIn(list2, (List) list.get(i))) {
                    arrayList2 = list.subList(i + 1, list.size());
                    this.routeIndex++;
                    break;
                }
            }
            if (this.routeIndex >= this.allCellRouteList.size()) {
                this.isSame = true;
                return;
            } else {
                searchCompare(arrayList2, (List) this.allCellRouteList.get(this.routeIndex), true);
                return;
            }
        }
        for (int size = list.size() - 1; size >= 0; size--) {
            if (hasIn(list2, (List) list.get(size))) {
                arrayList = list.subList(0, size);
                this.routeIndex++;
                break;
            }
        }
        if (this.routeIndex >= this.allCellRouteList.size()) {
            this.isSame = true;
        } else {
            searchCompare(arrayList, (List) this.allCellRouteList.get(this.routeIndex), false);
        }
    }

    public void insertOrUpdateRouteList(boolean z, int... iArr) {
        ArrayList arrayList = new ArrayList();
        if (!z) {
            this.dbHelper.delete("cell_route_table_v2", null, null);
            this.dbHelper.delete("cell_route_table", null, null);
        }
        Iterator it = this.allCellRouteList.iterator();
        while (true) {
            if (it.hasNext()) {
                List list = (List) it.next();
                if (list.size() != 5) {
                    Log.d("SelfStudyController", "insertOrUpdateRouteList,error route list");
                    return;
                } else if (((CellRouteInfo) list.get(0)).cellFreq > 0) {
                    for (int i = 1; i < list.size(); i++) {
                        removeDuplicateData((CellRouteInfo) list.get(i));
                    }
                    arrayList.add(Long.valueOf(this.dbHelper.insertCellRouteInfo(list, encryptedKey)));
                }
            } else {
                Log.d("SelfStudyController", "insertOrUpdateRouteList ids length is " + arrayList.size());
                StringBuilder sb = new StringBuilder();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    sb.append(arrayList.get(i2));
                    sb.append(",");
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("compare_count", Integer.valueOf(iArr.length > 0 ? iArr[0] : 0));
                contentValues.put("rat_change_count", Integer.valueOf(mRatChangeCount));
                contentValues.put("route", sb.toString());
                Log.d("SelfStudyController", "insertOrUpdateRouteList routeRowId is " + this.dbHelper.insert("cell_route_table", contentValues));
                if (this.allCellRouteList.size() < 10) {
                    recordLessTwentyExpiredDays(1);
                } else {
                    recordLessTwentyExpiredDays(0);
                }
                this.allCellRouteList.clear();
                return;
            }
        }
    }

    private void removeDuplicateData(CellRouteInfo cellRouteInfo) {
        for (int i = 0; i < this.allCellRouteList.size(); i++) {
            if (isSameCellRouteInfo(cellRouteInfo, (CellRouteInfo) ((List) this.allCellRouteList.get(i)).get(0))) {
                cellRouteInfo.cellRat = 0;
                cellRouteInfo.cellFreq = 0;
                cellRouteInfo.cellPci = 0;
            }
        }
    }

    public boolean hasIn(List list, List list2) {
        boolean z;
        boolean z2 = false;
        if (list2.size() == 5) {
            int i = 0;
            boolean z3 = false;
            while (i < list.size() && ((CellRouteInfo) list.get(i)).cellRat > 0) {
                int i2 = 0;
                while (true) {
                    if (i2 >= list2.size() || ((CellRouteInfo) list2.get(i2)).cellRat <= 0) {
                        z = false;
                        break;
                    }
                    if (isSameCellRouteInfo((CellRouteInfo) list.get(i), (CellRouteInfo) list2.get(i2))) {
                        z = true;
                        break;
                    }
                    i2++;
                }
                if (i2 >= list.size() || ((CellRouteInfo) list.get(i2)).cellRat == 0) {
                    z2 = z;
                } else {
                    i++;
                    z3 = z;
                }
            }
            z2 = z3;
        }
        Log.d("SelfStudyController", "hasIn is " + z2);
        return z2;
    }

    public void stopPredict() {
        if (isPredicting) {
            this.cellTempRouteInfoList.clear();
            isPredicting = false;
            if (this.mHasRegisterUserCallback) {
                TelephonyStatAdapter.unregisterCallBack(this.mUserCallBack);
                this.mHasRegisterUserCallback = false;
            }
        }
    }

    public void stopToUse() {
        Log.d("SelfStudyController", "stop Use");
        if (isUsing) {
            this.mExcepitonCells.clear();
            this.mCellTime = 0L;
            updateBadCellScoreStatus(false);
            isUsing = false;
            sendSelfStudyRequest(10, 1, new byte[]{0});
        }
    }

    public void beginStudy() {
        if (isStudying) {
            return;
        }
        Log.d("SelfStudyController", "begin study");
        isStudying = true;
        Handler handler = this.mWorkHandler;
        if (handler != null && this.dbHelper != null) {
            handler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$beginStudy$6();
                }
            });
        } else {
            Log.w("SelfStudyController", "beginStudy: mWorkHandler or dbHelper is null");
        }
        updateUseDays(0);
        PendingIntent broadcast = PendingIntent.getBroadcast(this.mContext, 0, new Intent("study_over_time_intent"), 201326592);
        AlarmManager alarmManager = this.mAlarmManager;
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(0, System.currentTimeMillis() + 5400000, broadcast);
        } else {
            Log.w("SelfStudyController", "beginStudy: mAlarmManager is null");
        }
        this.allCellRouteList.clear();
        mRatChangeCount = 0;
        sendSelfStudyRequest(6, 1, new byte[]{1});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$beginStudy$6() {
        this.dbHelper.resetTable("cell_route_table");
        this.dbHelper.resetTable("cell_table");
        this.dbHelper.resetTable("cell_route_table_v2");
    }

    public void beginPredict() {
        SelfStudyDatabaseHelper selfStudyDatabaseHelper;
        this.cellTempRouteInfoList.clear();
        if (!this.mHasRegisterUserCallback) {
            TelephonyStatAdapter.registerCallBack(this.mUserCallBack);
            this.mHasRegisterUserCallback = true;
        }
        isPredicting = true;
        if (this.badCellRouteIds.size() != 0 || (selfStudyDatabaseHelper = this.dbHelper) == null) {
            return;
        }
        selfStudyDatabaseHelper.updateBadCellRouteIds(this.badCellRouteIds, encryptedKey);
    }

    public void beginToUse() {
        log("begin to use");
        if (isUsing) {
            return;
        }
        this.mExcepitonCells.clear();
        setCellUnitScore(getCellIdentity());
        this.mCellTime = SystemClock.elapsedRealtime();
        isUsing = true;
        sendSelfStudyRequest(10, 1, new byte[]{1});
        updateBadCellScoreStatus(true);
        queryBadCellInfoAndDoCellAction();
    }

    private void queryBadCellInfoAndDoCellAction() {
        SelfStudyControllerBase selfStudyControllerBase;
        Cursor cursorQuery = this.dbHelper.query("cell_table", new String[]{"rat", "pci", "freq", "is_bad_cell"}, "is_bad_cell >= ? and duration <= ? ", new String[]{"1", "120"}, null, null, null);
        if (cursorQuery.moveToFirst()) {
            while (!cursorQuery.isAfterLast()) {
                int i = cursorQuery.getInt(0);
                int i2 = cursorQuery.getInt(1);
                int i3 = cursorQuery.getInt(2);
                int i4 = cursorQuery.getInt(3);
                if (this.dbHelper.getIdFromCellRouteTable(i, i2, i3, encryptedKey) != -1) {
                    selfStudyControllerBase = this;
                    selfStudyControllerBase.doCellAction(0L, i2, i3, i, i4);
                } else {
                    selfStudyControllerBase = this;
                }
                cursorQuery.moveToNext();
                this = selfStudyControllerBase;
            }
        }
        cursorQuery.close();
    }

    public void doCellAction(long j, int i, int i2, int i3, int i4) {
        Log.d("SelfStudyController", "doCellAction");
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    protected void updateRealTimeCellInfo(String str, int i) {
        if (str == null) {
            return;
        }
        boolean zCurrentIsEveningPeak = currentIsEveningPeak();
        boolean zIsBadCellInMultiRouteTable = this.dbHelper.isBadCellInMultiRouteTable(str, zCurrentIsEveningPeak ? 1 : 0);
        tryDataStallOptimization(zIsBadCellInMultiRouteTable, i, 1, "selfStudy");
        if (zIsBadCellInMultiRouteTable || !this.dbHelper.isPreviousCellOfBadCell(str, zCurrentIsEveningPeak ? 1 : 0)) {
            return;
        }
        log("reportNextCellStatus bad cell");
        notify3rdAppOpt();
    }

    public void resetTables() {
        Log.d("SelfStudyController", "clear new tables,drop ");
        Handler handler = this.mWorkHandler;
        if (handler != null && this.dbHelper != null) {
            handler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$resetTables$7();
                }
            });
        } else {
            Log.w("SelfStudyController", "resetTables: mWorkHandler or dbHelper is null");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetTables$7() {
        this.dbHelper.resetTables();
    }

    class CurrentTopAppObserver implements MiuiAppStateMonitor.TopAppObserver {
        private CurrentTopAppObserver() {
        }

        @Override // com.android.phone.utils.MiuiAppStateMonitor.TopAppObserver
        public void onTopAppChanged(List list, int i) {
            Handler handler = SelfStudyControllerBase.this.mWorkHandler;
            if (handler == null) {
                Log.w("SelfStudyController", "CurrentTopAppObserver.onTopAppChanged: mWorkHandler is null");
            } else {
                SelfStudyControllerBase.this.mWorkHandler.sendMessageDelayed(handler.obtainMessage(1010, i, 0, list), 500L);
            }
        }
    }

    public class SelfStudyReceiver extends BroadcastReceiver {
        public String TAG = "SelfStudyReceiver";

        public SelfStudyReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            int i;
            if (intent == null) {
                Log.d(this.TAG, "Null action");
                return;
            }
            if (SelfStudyControllerBase.this.mWorkHandler == null) {
                Log.w(this.TAG, "SelfStudyReceiver.onReceive: mWorkHandler is null");
                return;
            }
            intent.getAction();
            if ("com.android.phone.intent.action.LINK_RELIABILITY_SELF_STUDY".equalsIgnoreCase(intent.getAction())) {
                SelfStudyControllerBase.this.log("ACTION_LINK_RELIABILITY");
                boolean zNeedGetBadCellScore = SelfStudyControllerBase.this.needGetBadCellScore();
                SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                if (!zNeedGetBadCellScore) {
                    selfStudyControllerBase.log("ACTION_LINK_RELIABILITY do not need");
                    return;
                }
                selfStudyControllerBase.mWorkHandler.removeMessages(1015);
                if (SelfStudyControllerBase.this.mCellTime <= 0 || SystemClock.elapsedRealtime() - SelfStudyControllerBase.this.mCellTime < 4000) {
                    return;
                }
                int intExtra = intent.getIntExtra("netId", -1);
                int intExtra2 = intent.getIntExtra("metricsCause", -1);
                Handler handler = SelfStudyControllerBase.this.mWorkHandler;
                handler.sendMessageDelayed(handler.obtainMessage(1015, intExtra, intExtra2), 100L);
                return;
            }
            if ("com.android.phone.intent.action.LINK_RELIABILITY".equals(intent.getAction())) {
                int intExtra3 = intent.getIntExtra("netId", -1);
                int intExtra4 = intent.getIntExtra("metricsCause", -1);
                SelfStudyControllerBase.this.log("receiver LINK QOS action netId:" + intExtra3 + " cause:" + intExtra4);
                SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                int i2 = selfStudyControllerBase2.dataType;
                if (i2 != 2 && i2 != 4) {
                    selfStudyControllerBase2.log("ignore handler qos for dataType not match.");
                    return;
                }
                SelfStudyControllerBase.localLog(this.TAG + ".LinkQosStallReason", "netId:" + intExtra3 + ",cause:" + intExtra4 + ",dataType:" + SelfStudyControllerBase.this.dataType, 50);
                SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                selfStudyControllerBase3.mIsStreamStall = true;
                selfStudyControllerBase3.recordStallDetailsStatistic(true, intExtra4, selfStudyControllerBase3.dataType, 1, selfStudyControllerBase3.mStallDetailsList);
                SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                selfStudyControllerBase4.beginStreamDataStall(selfStudyControllerBase4.dataType, 0);
                SelfStudyControllerBase.this.processDataStallAppStatistic(true, 1);
                boolean zIsModemEnteredStreamScene = SelfStudyControllerBase.this.isModemEnteredStreamScene();
                SelfStudyControllerBase selfStudyControllerBase5 = SelfStudyControllerBase.this;
                if (!zIsModemEnteredStreamScene) {
                    selfStudyControllerBase5.log("modem does not enter the streaming scene for qos stall");
                    return;
                }
                selfStudyControllerBase5.getOffSetFormModem(1, intExtra4);
                SelfStudyControllerBase.this.updateRecordStateForHome();
                if (SelfStudyControllerBase.this.mWorkHandler.hasMessages(1041)) {
                    SelfStudyControllerBase.this.mWorkHandler.removeMessages(1041);
                    Handler handler2 = SelfStudyControllerBase.this.mWorkHandler;
                    handler2.sendMessageDelayed(handler2.obtainMessage(1041), 10000L);
                    return;
                }
                if (!SelfStudyControllerBase.isBeginCollectCellData) {
                    SelfStudyControllerBase selfStudyControllerBase6 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase6.mIsResidentCell || selfStudyControllerBase6.mIsSubway) {
                        SelfStudyControllerBase selfStudyControllerBase7 = SelfStudyControllerBase.this;
                        selfStudyControllerBase7.beginToCollectCellData(selfStudyControllerBase7.mIsWorkDay, 1);
                        Handler handler3 = SelfStudyControllerBase.this.mWorkHandler;
                        handler3.sendMessageDelayed(handler3.obtainMessage(1041), 10000L);
                    }
                }
                if (SelfStudyControllerBase.this.mWorkHandler.hasMessages(1053)) {
                    return;
                }
                SelfStudyControllerBase.this.logV("send long stall msg for linkStall.");
                Handler handler4 = SelfStudyControllerBase.this.mWorkHandler;
                handler4.sendMessageDelayed(handler4.obtainMessage(1053), 10000L);
                return;
            }
            if ("com.android.phone.action.VIDEO_PRELOAD_EVENT_CALLBACK".equalsIgnoreCase(intent.getAction())) {
                int intExtra5 = intent.getIntExtra(a.b, -1);
                String stringExtra = intent.getStringExtra(com.ot.pubsub.a.a.m);
                SelfStudyControllerBase.this.log("receiver video preload action event:" + intExtra5 + " msg:" + stringExtra);
                SelfStudyControllerBase selfStudyControllerBase8 = SelfStudyControllerBase.this;
                if (intExtra5 != 5) {
                    selfStudyControllerBase8.mIsStreamStall = false;
                    selfStudyControllerBase8.processDataStallAppStatistic(false, 2);
                    if (SelfStudyControllerBase.this.mWorkHandler.hasMessages(1041)) {
                        SelfStudyControllerBase.this.mWorkHandler.removeMessages(1041);
                        SelfStudyControllerBase selfStudyControllerBase9 = SelfStudyControllerBase.this;
                        selfStudyControllerBase9.stopToCollectCellData(selfStudyControllerBase9.mIsWorkDay, 2);
                    }
                    if (SelfStudyControllerBase.isBeginCollectCellData) {
                        SelfStudyControllerBase selfStudyControllerBase10 = SelfStudyControllerBase.this;
                        selfStudyControllerBase10.stopToCollectCellData(selfStudyControllerBase10.mIsWorkDay, 2);
                    }
                    if (SelfStudyControllerBase.this.mWorkHandler.hasMessages(1053)) {
                        SelfStudyControllerBase.this.logV("remove long stall event lasted for dyStall.");
                        SelfStudyControllerBase.this.mWorkHandler.removeMessages(1053);
                        return;
                    }
                    return;
                }
                selfStudyControllerBase8.mIsStreamStall = true;
                selfStudyControllerBase8.beginStreamDataStall(selfStudyControllerBase8.dataType, 0);
                boolean zIsModemEnteredStreamScene2 = SelfStudyControllerBase.this.isModemEnteredStreamScene();
                SelfStudyControllerBase selfStudyControllerBase11 = SelfStudyControllerBase.this;
                if (!zIsModemEnteredStreamScene2) {
                    selfStudyControllerBase11.log("modem does not enter the streaming scene for DY stall");
                    return;
                }
                selfStudyControllerBase11.updateRecordStateForHome();
                if (!SelfStudyControllerBase.isBeginCollectCellData) {
                    SelfStudyControllerBase selfStudyControllerBase12 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase12.mIsResidentCell || selfStudyControllerBase12.mIsSubway) {
                        SelfStudyControllerBase selfStudyControllerBase13 = SelfStudyControllerBase.this;
                        selfStudyControllerBase13.beginToCollectCellData(selfStudyControllerBase13.mIsWorkDay, 2);
                    }
                }
                SelfStudyControllerBase.this.getOffSetFormModem(2, -1001);
                SelfStudyControllerBase.this.processDataStallAppStatistic(true, 2);
                if (SelfStudyControllerBase.this.mWorkHandler.hasMessages(1053)) {
                    return;
                }
                SelfStudyControllerBase.this.logV("send long stall msg for dyStall.");
                Handler handler5 = SelfStudyControllerBase.this.mWorkHandler;
                handler5.sendMessageDelayed(handler5.obtainMessage(1053), 10000L);
                return;
            }
            if ("android.intent.action.BOOT_COMPLETED".equalsIgnoreCase(intent.getAction())) {
                SelfStudyControllerBase.this.log("ACTION_BOOT_COMPLETED received from broadcast");
                SelfStudyControllerBase.this.lambda$regUnregReceiver$8();
                return;
            }
            if ("android.net.conn.CONNECTIVITY_CHANGE".equalsIgnoreCase(intent.getAction())) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (networkInfo == null || networkInfo.getType() != 0) {
                    return;
                }
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    SelfStudyControllerBase selfStudyControllerBase14 = SelfStudyControllerBase.this;
                    if (!selfStudyControllerBase14.mIsMobileDataOn) {
                        selfStudyControllerBase14.mWorkHandler.obtainMessage(1014, Boolean.TRUE).sendToTarget();
                        return;
                    }
                }
                if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                    SelfStudyControllerBase selfStudyControllerBase15 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase15.mIsMobileDataOn) {
                        selfStudyControllerBase15.mWorkHandler.obtainMessage(1014, Boolean.FALSE).sendToTarget();
                        return;
                    }
                    return;
                }
                return;
            }
            if ("study_over_time_intent".equals(intent.getAction())) {
                SelfStudyControllerBase.this.stopStudy();
                return;
            }
            if ("over_time_start_listen_screen_status".equals(intent.getAction())) {
                SelfStudyControllerBase.this.log("OVER_TIME_START_LISTEN_SCREEN_STATUS");
                SelfStudyControllerBase selfStudyControllerBase16 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase16.mAlarmManager != null && selfStudyControllerBase16.startListenScreenStatusPendingIntent != null) {
                    SelfStudyControllerBase selfStudyControllerBase17 = SelfStudyControllerBase.this;
                    selfStudyControllerBase17.mAlarmManager.setExactAndAllowWhileIdle(0, selfStudyControllerBase17.getTimeInMillsFromCalendar(23, 0), SelfStudyControllerBase.this.startListenScreenStatusPendingIntent);
                }
                int i3 = SelfStudyControllerBase.this.getCurrentCalendar(System.currentTimeMillis()).get(11);
                SelfStudyControllerBase selfStudyControllerBase18 = SelfStudyControllerBase.this;
                if (i3 != 23) {
                    selfStudyControllerBase18.log("ingore OVER_TIME_START_LISTEN_SCREEN_STATUS");
                    return;
                } else {
                    selfStudyControllerBase18.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$SelfStudyReceiver$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onReceive$0();
                        }
                    });
                    return;
                }
            }
            if ("over_time_stop_listen_screen_status".equals(intent.getAction())) {
                SelfStudyControllerBase.this.log("OVER_TIME_STOP_LISTEN_SCREEN_STATUS");
                SelfStudyControllerBase selfStudyControllerBase19 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase19.mAlarmManager != null && selfStudyControllerBase19.stopListenScreenStatusPendingIntent != null) {
                    SelfStudyControllerBase selfStudyControllerBase20 = SelfStudyControllerBase.this;
                    selfStudyControllerBase20.mAlarmManager.setExactAndAllowWhileIdle(0, selfStudyControllerBase20.getTimeInMillsFromCalendar(10, 30), SelfStudyControllerBase.this.stopListenScreenStatusPendingIntent);
                }
                int i4 = SelfStudyControllerBase.this.getCurrentCalendar(System.currentTimeMillis()).get(11);
                SelfStudyControllerBase selfStudyControllerBase21 = SelfStudyControllerBase.this;
                if (i4 != 10) {
                    selfStudyControllerBase21.log("ingore OVER_TIME_STOP_LISTEN_SCREEN_STATUS");
                    return;
                }
                if (selfStudyControllerBase21.hasRegistStudyCallback) {
                    selfStudyControllerBase21.unregisterCallBack(selfStudyControllerBase21.studyCallBack_V2);
                    SelfStudyControllerBase.this.hasRegistStudyCallback = false;
                }
                SelfStudyControllerBase.this.stopToUse();
                SelfStudyControllerBase.this.stopPredict();
                return;
            }
            if ("android.intent.action.SERVICE_STATE".equalsIgnoreCase(intent.getAction())) {
                SelfStudyControllerBase selfStudyControllerBase22 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase22.mTimer == null || selfStudyControllerBase22.isLteOrNr()) {
                    return;
                }
                SelfStudyControllerBase selfStudyControllerBase23 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase23.isSampling) {
                    return;
                }
                try {
                    selfStudyControllerBase23.mTimer.cancel();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if ("notify_action".equals(intent.getAction())) {
                SelfStudyControllerBase.this.notify3rdAppOpt();
                return;
            }
            if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY".equals(intent.getAction()) || "miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(intent.getAction())) {
                int intExtra6 = intent.getIntExtra(SubscriptionManagerEx.PHONE_KEY, -1);
                if (intExtra6 == -1 || ((i = SelfStudyControllerBase.this.mNotifyModemDdsId) != -1 && i != intExtra6)) {
                    SelfStudyControllerBase.this.wechatOptTryRecovery();
                }
                if (SubscriptionManager.isValidPhoneId(intExtra6)) {
                    SelfStudyControllerBase selfStudyControllerBase24 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase24.mNotifyModemDdsId != intExtra6) {
                        selfStudyControllerBase24.log("phone id is " + intExtra6);
                        SelfStudyControllerBase selfStudyControllerBase25 = SelfStudyControllerBase.this;
                        selfStudyControllerBase25.mNotifyModemDdsId = intExtra6;
                        selfStudyControllerBase25.sendDdsToModem(intExtra6);
                    }
                }
                if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(intent.getAction())) {
                    int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                    if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId)) {
                        SelfStudyControllerBase selfStudyControllerBase26 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase26.mDdsPhoneId != defaultDataSlotId) {
                            selfStudyControllerBase26.mDdsPhoneId = defaultDataSlotId;
                            selfStudyControllerBase26.mIsDdsChange = true;
                            selfStudyControllerBase26.mWorkHandler.sendEmptyMessage(1050);
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            if ("com.android.phone.intent.action.WECHAT_VOIP_SCENE".equals(intent.getAction())) {
                boolean booleanExtra = intent.getBooleanExtra("wechatVoipEnabled", false);
                Handler handler6 = SelfStudyControllerBase.this.mWorkHandler;
                handler6.sendMessage(handler6.obtainMessage(1026, Boolean.valueOf(booleanExtra)));
                return;
            }
            if ("miui.intent.action.VIDEO_INFO".equals(intent.getAction())) {
                final int intExtra7 = intent.getIntExtra("render-quality", -1);
                Log.d(this.TAG, "video quality is " + intExtra7);
                final int intExtra8 = intent.getIntExtra("render-start", -1);
                if (intExtra8 == 1) {
                    SelfStudyControllerBase.this.isVideoFront = true;
                } else if (intExtra8 == 0) {
                    SelfStudyControllerBase.this.isVideoFront = false;
                }
                SelfStudyControllerBase.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$SelfStudyReceiver$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$1(intExtra8, intExtra7);
                    }
                });
                SelfStudyControllerBase selfStudyControllerBase27 = SelfStudyControllerBase.this;
                int i5 = selfStudyControllerBase27.lastQuality;
                if (i5 != intExtra7 && selfStudyControllerBase27.isVideoFront) {
                    selfStudyControllerBase27.calcVideoQualityDuration(selfStudyControllerBase27.dataType, i5, intExtra7);
                }
                if (intExtra8 != -1) {
                    SelfStudyControllerBase.this.lastQuality = 1;
                }
                if (intExtra7 != -1) {
                    SelfStudyControllerBase.this.lastQuality = intExtra7;
                }
                Log.d(this.TAG, "isVideoFront is " + SelfStudyControllerBase.this.isVideoFront + " lastQuality " + SelfStudyControllerBase.this.lastQuality);
                SelfStudyControllerBase selfStudyControllerBase28 = SelfStudyControllerBase.this;
                int i6 = selfStudyControllerBase28.dataType;
                if (i6 == 8) {
                    selfStudyControllerBase28.calcDataTypeDuration(i6, i6);
                }
                SelfStudyControllerBase selfStudyControllerBase29 = SelfStudyControllerBase.this;
                selfStudyControllerBase29.processVideoQualityChanged(selfStudyControllerBase29.lastQuality);
                return;
            }
            if ("com.xiaomi.modemai.game.FORECAST_RES".equals(intent.getAction())) {
                SelfStudyControllerBase.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$SelfStudyReceiver$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$2(intent);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            selfStudyControllerBase.stopToCollectBadCells(selfStudyControllerBase.mIsWorkDay);
            int i = 0;
            while (true) {
                SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                if (i < 3) {
                    selfStudyControllerBase2.isCellActionOn[i] = false;
                    i++;
                } else {
                    selfStudyControllerBase2.mPreferenceUtils.putBoolean("action_on_morning", false);
                    SelfStudyControllerBase.this.mPreferenceUtils.putBoolean("action_on_non_peak", false);
                    SelfStudyControllerBase.this.mPreferenceUtils.putBoolean("action_on_evening_peak", false);
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$1(int i, int i2) {
            if (SelfStudyControllerBase.this.shouldDoActionForWechatOpt()) {
                SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                if (selfStudyControllerBase.dataType == 3) {
                    if (i != -1) {
                        selfStudyControllerBase.onWechatStateChanged(selfStudyControllerBase.mIsInWeChatVoip, selfStudyControllerBase.isVideoFront);
                    }
                    if (i2 == 0) {
                        SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                        if (selfStudyControllerBase2.isVideoFront && selfStudyControllerBase2.mIsProcessedWechatEnter) {
                            selfStudyControllerBase2.getMdWechatStallInfo();
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$2(Intent intent) {
            SelfStudyControllerBase.this.log("receive ACTION_MODEM_AI_GAME_RESULT");
            String stringExtra = intent.getStringExtra("FORECAST_RES");
            String stringExtra2 = intent.getStringExtra("FORECAST_MODEL");
            if (SelfStudyControllerBase.this.shouldDoActionForGameOpt()) {
                SelfStudyControllerBase.this.getGameAiLagTime(stringExtra, stringExtra2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onWechatStateChanged(boolean z, boolean z2) {
        if (!this.cloudWechatOn || !MiuiPhoneUtils.isSupportWechatVideoOptimization()) {
            log("wechat video optimization disable by cloud or device does not support");
            return;
        }
        log("onWechatStateChanged enterWechat = " + z + " isVideo = " + z2);
        if (z2 && z) {
            if (this.mWorkHandler.hasMessages(1023)) {
                this.mWorkHandler.removeMessages(1023);
                processStopDataStall();
            }
            Handler handler = this.mWorkHandler;
            handler.sendMessage(handler.obtainMessage(SmartNetworkRelayScore.TRAFFIC_UNIT, 3));
            boolean zHasMessages = this.mWorkHandler.hasMessages(1032);
            Handler handler2 = this.mWorkHandler;
            if (zHasMessages) {
                handler2.removeMessages(1032);
                delayProcessWechatEnter();
                log("enter WeChat again within 5 seconds, skip");
                return;
            } else {
                handler2.sendMessageDelayed(handler2.obtainMessage(1031), 20000L);
                processWechatEnter();
                Handler handler3 = this.mWorkHandler;
                handler3.sendMessage(handler3.obtainMessage(1038, MdOptimizationScene.WechatVideo.value, 0));
            }
        }
        if (z) {
            return;
        }
        Handler handler4 = this.mWorkHandler;
        handler4.sendMessageDelayed(handler4.obtainMessage(1032), MirrorDesktopHelper.TIMEOUT_MILLIS);
        resetBenefitStatisticList();
    }

    public boolean shouldDoActionForStreamMediaOpt() {
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        if (!MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            return true;
        }
        if (!this.cloudStreamMediaOn || !this.mIsMobileDataOn || MiuiPhoneUtils.isDuringCall() || !isChineseOperator(activeDataPhoneId) || isNeedSkipOperation()) {
            return false;
        }
        int i = this.dataType;
        return i == 2 || i == 4;
    }

    public boolean shouldDoActionForWechatOpt() {
        return this.cloudWechatOn && MiuiPhoneUtils.isSupportWechatVideoOptimization() && this.mIsInWeChatVoip && this.mIsMobileDataOn && !MiuiPhoneUtils.isDuringCall() && isChineseOperator(MiuiPhoneUtils.getActiveDataPhoneId());
    }

    public boolean shouldDoActionForGameOpt() {
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        if ((MiuiPhoneUtils.isSupportMtkDataOptimization() || MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) && this.cloudGameResidentOn && isChineseOperator(activeDataPhoneId) && !MiuiPhoneUtils.isDuringCall() && this.mIsMobileDataOn && isCellularNetworkSwitchStillOn()) {
            return true;
        }
        Log.d("SelfStudyController", "cloudGameResidentOn: " + this.cloudGameResidentOn + " mIsMobileDataOn: " + this.mIsMobileDataOn + " isCellularNetworkSwitchStillOn: " + isCellularNetworkSwitchStillOn());
        return false;
    }

    public boolean shouldDoActionForGameQcOpt() {
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        if ((MiuiPhoneUtils.isSupportQcGameResidentCellOptimization() || MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) && this.cloudGameResidentOn && isChineseOperator(activeDataPhoneId) && !MiuiPhoneUtils.isDuringCall() && this.mIsMobileDataOn && isCellularNetworkSwitchStillOn()) {
            return true;
        }
        Log.d("SelfStudyController", "cloudGameResidentOn: " + this.cloudGameResidentOn + " mIsMobileDataOn: " + this.mIsMobileDataOn);
        return false;
    }

    public boolean shouldDoActionForGameOptForOS4() {
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        if (MiuiPhoneUtils.isSupportGameResidentCellOptForOS4() && this.cloudGameResidentOn && isChineseOperator(activeDataPhoneId) && !MiuiPhoneUtils.isDuringCall() && this.mIsMobileDataOn && isCellularNetworkSwitchStillOn()) {
            return true;
        }
        Log.d("SelfStudyController", "cloudGameResidentOn: " + this.cloudGameResidentOn + " mIsMobileDataOn: " + this.mIsMobileDataOn);
        return false;
    }

    public boolean isCloudGameResidentOn() {
        return this.cloudGameResidentOn;
    }

    public boolean isChineseOperator(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null) {
            String operatorNumeric = phone.getOperatorNumeric();
            if (!TextUtils.isEmpty(operatorNumeric) && operatorNumeric.startsWith("460")) {
                return true;
            }
        }
        return false;
    }

    public boolean isNeedSkipOperation() {
        String networkNumeric = MiuiPhoneUtils.getNetworkNumeric(DefaultSimManager.getDefaultDataSlotId());
        return !TextUtils.isEmpty(networkNumeric) && this.LAB_MCCMNC_SET.contains(networkNumeric);
    }

    public boolean isVideoDataStallOptimizationSupported() {
        return MiuiPhoneUtils.isSupportVideoDataStallOptimization();
    }

    class SelfStudySensorStateListener implements TelephonySensorManager.SensorStateListener {
        SelfStudySensorStateListener() {
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onScreenStateChanged(final boolean z) {
            final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            SelfStudyControllerBase.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$SelfStudySensorStateListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onScreenStateChanged$0(z, atomicBoolean);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onScreenStateChanged$0(boolean z, AtomicBoolean atomicBoolean) {
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            boolean z2 = selfStudyControllerBase.mIsScreenOn;
            if (!z2 && z) {
                selfStudyControllerBase.log("screen on");
                SelfStudyControllerBase.this.actionScreenOn();
                atomicBoolean.set(true);
            } else if (z2 && !z) {
                selfStudyControllerBase.log("screen off");
                SelfStudyControllerBase.this.actionScreenOff();
                atomicBoolean.set(true);
            }
            SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
            selfStudyControllerBase2.mIsScreenOn = z;
            selfStudyControllerBase2.logV("screenStateChanged = " + atomicBoolean.get());
            if (atomicBoolean.get() && SelfStudyControllerBase.this.mIsMobileDataOn) {
                if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                    SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                    selfStudyControllerBase3.processScreenStateChange(selfStudyControllerBase3.mIsScreenOn);
                    return;
                }
                SelfStudyControllerBase.this.sendSelfStudyRequest(15, 1, new byte[]{(byte) (z ? SelfStudyControllerBase.this.dataType : 0)});
                if (z) {
                    return;
                }
                boolean zHasMessages = SelfStudyControllerBase.this.mWorkHandler.hasMessages(1023);
                SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                if (zHasMessages) {
                    selfStudyControllerBase4.mWorkHandler.removeMessages(1023);
                    SelfStudyControllerBase.this.processStopDataStall();
                } else {
                    Handler handler = selfStudyControllerBase4.mWorkHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(1023), 30000L);
                }
            }
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onSubwayStateChanged(final boolean z) {
            SelfStudyControllerBase.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$SelfStudySensorStateListener$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onSubwayStateChanged$1(z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSubwayStateChanged$1(boolean z) {
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            boolean z2 = selfStudyControllerBase.mIsSubwayStateIn;
            if (!z2 && z) {
                selfStudyControllerBase.mIsSubwayStateIn = true;
                selfStudyControllerBase.log("subway in");
            } else {
                if (!z2 || z) {
                    return;
                }
                selfStudyControllerBase.mIsSubwayStateIn = false;
                selfStudyControllerBase.log("subway out");
            }
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onDriveStateChanged(final boolean z) {
            SelfStudyControllerBase.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$SelfStudySensorStateListener$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDriveStateChanged$2(z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDriveStateChanged$2(boolean z) {
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            boolean z2 = selfStudyControllerBase.mIsDriveStateIn;
            if (!z2 && z) {
                selfStudyControllerBase.mIsDriveStateIn = true;
                selfStudyControllerBase.log("drive state in");
            } else {
                if (!z2 || z) {
                    return;
                }
                selfStudyControllerBase.mIsDriveStateIn = false;
                selfStudyControllerBase.log("drive state out");
            }
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onHstStateChanged(final boolean z) {
            SelfStudyControllerBase.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$SelfStudySensorStateListener$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onHstStateChanged$3(z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHstStateChanged$3(boolean z) {
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            boolean z2 = selfStudyControllerBase.mIsHstStateIn;
            if (!z2 && z) {
                selfStudyControllerBase.mIsHstStateIn = true;
                selfStudyControllerBase.log("hst in");
            } else {
                if (!z2 || z) {
                    return;
                }
                selfStudyControllerBase.mIsHstStateIn = false;
                selfStudyControllerBase.log("hst out");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void actionScreenOff() {
        try {
            if (MiuiPhoneUtils.getActiveDataPhone() != null && MiuiPhoneUtils.getActiveDataPhone().getServiceState().getState() == 0) {
                stopCollectMultiRouteAndPredict(false);
                judeyWhetherResetRecordDays();
                if (getRecordLessTwentyExpiredDays() >= 3) {
                    log("getRecordLessTwentyExpiredDays " + getRecordLessTwentyExpiredDays());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initMultiRouteStudyState() {
        if (!this.mHasRegisterUserCallback) {
            String currentDataPhoneCellId = getCurrentDataPhoneCellId();
            int i = 0;
            while (true) {
                String[] strArr = this.mCurrentRouteCellIds;
                if (i >= strArr.length) {
                    break;
                }
                strArr[i] = currentDataPhoneCellId;
                i++;
            }
            TelephonyStatAdapter.registerCallBack(this.mUserCallBack);
            this.mHasRegisterUserCallback = true;
        }
        this.dbHelper.deleteOutOfDateRows("multi_route_bad_cell_table", v.a);
    }

    protected void initDataOptimizationState(boolean z) {
        long j = z ? 259200000L : v.a;
        SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
        if (selfStudyDatabaseHelper != null) {
            selfStudyDatabaseHelper.deleteOutOfDateRowsForStreamMedia(j, z ? 1 : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void actionScreenOn() {
        try {
            if (MiuiPhoneUtils.getActiveDataPhone() != null && MiuiPhoneUtils.getActiveDataPhone().getServiceState().getState() == 0) {
                if (!MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                    judeyWhetherResetRecordDays();
                    if (getRecordLessTwentyExpiredDays() >= 3) {
                        log("getRecordLessTwentyExpiredDays " + getRecordLessTwentyExpiredDays());
                        return;
                    }
                }
                if (this.isSupportCellAction) {
                    if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                        return;
                    }
                    triggerCollectBadCellsAndDoAction();
                    return;
                }
                startCollectMultiRouteAndBeginPredict();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void judeyWhetherResetRecordDays() {
        if (getRecordLessTwentyExpiredTimes() <= 0 || getCurrentDay(System.currentTimeMillis()) - getCurrentDay(getRecordLessTwentyExpiredTimes()) < 7) {
            return;
        }
        removeBeginCellStatus();
        resetRouteStudyJudgeCell();
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils != null) {
            selfStudyPreferenceUtils.resetAllStudyStatus();
        }
        resetTables();
        transitionTo(this.mRecognizeBeginCellState);
    }

    public void screenOnBeginStudy() {
        sendMessage(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDatas(boolean z) {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "updateDatas: mPreferenceUtils is null");
            return;
        }
        selfStudyPreferenceUtils.putBoolean("user_enable_study", z);
        if (z) {
            this.mPreferenceUtils.putLong("study_timer", System.currentTimeMillis());
        }
    }

    public long getStudyTime() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "getStudyTime: mPreferenceUtils is null");
            return System.currentTimeMillis();
        }
        return selfStudyPreferenceUtils.getLong("study_timer", System.currentTimeMillis());
    }

    public boolean hasStudy() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "hasStudy: mPreferenceUtils is null");
            return false;
        }
        return selfStudyPreferenceUtils.getBoolean("user_enable_study", false);
    }

    public boolean isSameCell(CellInfo cellInfo, CellInfo cellInfo2) {
        return cellInfo.getFreq() == cellInfo2.getFreq() && cellInfo.getPci() == cellInfo2.getPci();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void regUnregReceiver(boolean z) {
        BroadcastReceiver broadcastReceiver;
        if (z && this.mBroadcastReceiver == null) {
            this.mBroadcastReceiver = new SelfStudyReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("miui.intent.action.VIDEO_INFO");
            intentFilter.addAction("com.android.phone.intent.action.LINK_RELIABILITY_SELF_STUDY");
            if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                intentFilter.addAction("com.android.phone.intent.action.LINK_RELIABILITY");
                intentFilter.addAction("com.android.phone.action.VIDEO_PRELOAD_EVENT_CALLBACK");
            }
            intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            intentFilter.addAction("study_over_time_intent");
            intentFilter.addAction("over_time_start_listen_screen_status");
            intentFilter.addAction("over_time_stop_listen_screen_status");
            intentFilter.addAction("notify_action");
            intentFilter.addAction("android.intent.action.SERVICE_STATE");
            intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY");
            intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
            intentFilter.addAction("com.android.phone.intent.action.WECHAT_VOIP_SCENE");
            intentFilter.addAction("com.xiaomi.modemai.game.FORECAST_RES");
            this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
            if (isBootCompleted()) {
                log("Boot already completed when registering receiver, trigger handleBootCompleted");
                this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$regUnregReceiver$8();
                    }
                });
            }
        } else if (!z && (broadcastReceiver = this.mBroadcastReceiver) != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
            this.mBroadcastReceiver = null;
        }
        regUnregOtherReceiver(z);
    }

    private boolean isBootCompleted() {
        return "1".equals(SystemProperties.get("sys.boot_completed"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleBootCompleted, reason: merged with bridge method [inline-methods] */
    public void lambda$regUnregReceiver$8() {
        if (!this.mBootHandled.compareAndSet(false, true)) {
            log("handleBootCompleted already handled, skip");
        } else {
            this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$handleBootCompleted$9();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleBootCompleted$9() {
        this.mIsWorkDay = isWorkDay(System.currentTimeMillis());
        this.mIsSupportLowPowerModeOptimization = MiuiPhoneUtils.isSupportLowPowerModeOptimization();
        log("handleBootCompleted, mIsSupportLowPowerModeOptimization = " + this.mIsSupportLowPowerModeOptimization);
        if (this.mIsSupportLowPowerModeOptimization) {
            resetLowPowerOpt(true);
        }
        if (!MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            if (this.mFeatureEnable && this.isSupportCellAction) {
                this.isCellActionOn[0] = this.mPreferenceUtils.getBoolean("action_on_morning", false);
                this.isCellActionOn[1] = this.mPreferenceUtils.getBoolean("action_on_non_peak", false);
                this.isCellActionOn[2] = this.mPreferenceUtils.getBoolean("action_on_evening_peak", false);
                setAlarmForMiSmartNetwork(true);
                boolean[] zArr = this.isCellActionOn;
                if ((zArr[0] || zArr[2] || zArr[1]) && !isCollectingBadCell) {
                    beginToCollectBadCells(this.mIsWorkDay);
                }
            }
            int[] iArr = {this.mPreferenceUtils.getInt("NR_BAD_RSRP", 51), this.mPreferenceUtils.getInt("NR_BAD_RSRQ", 61), this.mPreferenceUtils.getInt("LTE_BAD_RSRP", 25), this.mPreferenceUtils.getInt("LTE_BAD_RSRQ", 15), this.mPreferenceUtils.getInt("CLOUD_KEY_NR_UL_BLER", 60), this.mPreferenceUtils.getInt("LTE_UL_BLER", 30), this.mPreferenceUtils.getInt("LIVE_STREAMING_SPEED", 200), this.mPreferenceUtils.getInt("GAME_SPEED", 25)};
            this.cellOffset = this.mPreferenceUtils.getInt("offset", -2);
            this.max_lte_offset = this.mPreferenceUtils.getInt("MAX_LTE_OFFSET", -9);
            this.max_nr_offset = this.mPreferenceUtils.getInt("MAX_NR_OFFSET", -6);
            sendSelfStudyRequest(25, 8, iArr);
        } else {
            log("ACTION_BOOT_COMPLETED sendSelfStudyRequest 400 init");
            sendSelfStudyRequest(400, 1, new int[]{0});
        }
        log("registerSceneStateCallBack init for BOOT_COMPLETED");
        if (this.mHydraSceneListener == null) {
            this.mHydraSceneListener = new HydraSceneListener();
        }
        EventHubManager.getInstance().registerListener("modem", "hydra_scene_state_changed", this.mHydraSceneListener, this.mWorkHandler);
    }

    private void registerObserver() {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("route_prediction_mode_user_enable"), false, new ContentObserver(this.mWorkHandler) { // from class: com.android.phone.performance.SelfStudyControllerBase.6
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                String lastPathSegment = uri.getLastPathSegment();
                SelfStudyControllerBase.this.log(lastPathSegment + " settings changed");
                SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                selfStudyControllerBase.mUserSettingOn = Settings.Secure.getInt(selfStudyControllerBase.mContentResolver, "route_prediction_mode_user_enable", 0) == 1;
                SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase2.mFeatureEnable) {
                    selfStudyControllerBase2.regUnregReceiver(selfStudyControllerBase2.mUserSettingOn);
                    SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                    selfStudyControllerBase3.regUnregSensorStateListener(selfStudyControllerBase3.mUserSettingOn);
                    SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                    selfStudyControllerBase4.regUnregAppStateMonitor(selfStudyControllerBase4.mUserSettingOn);
                }
            }
        });
    }

    protected void log(String str) {
        Rlog.d("SelfStudyController", str);
    }

    protected void logV(String str) {
        if (PhoneDebug.VDBG) {
            Rlog.d("SelfStudyController", str);
        }
    }

    public boolean isWorkDay(long j) {
        return !isHoliday(this.mContext, getCurrentCalendar(j));
    }

    private boolean isHoliday(Context context, Calendar calendar) {
        Cursor cursorQuery = context.getContentResolver().query(ContentUris.withAppendedId(ExtraCalendarContracts.HolidayContracts.HOLIDAY_CONTENT_URI, calendar.getTimeInMillis()), null, null, null, null);
        boolean z = false;
        if (cursorQuery != null) {
            log("query CalendarProvider, cursor is not null");
            try {
                if (cursorQuery.moveToFirst()) {
                    int i = cursorQuery.getInt(2);
                    log("holidayType: " + i);
                    if (1 == i || (2 != i && isWeekEnd(calendar))) {
                        z = true;
                    }
                    return z;
                }
            } finally {
                cursorQuery.close();
            }
        }
        return false;
    }

    private boolean isWeekEnd(Calendar calendar) {
        return calendar.get(7) == 7 || calendar.get(7) == 1;
    }

    public int getCurrentDay(long j) {
        if (j <= 0) {
            return -1;
        }
        return getCurrentCalendar(j).get(6);
    }

    public int[] getCurrentTimes() {
        Calendar currentCalendar = getCurrentCalendar(System.currentTimeMillis());
        return new int[]{currentCalendar.get(11), currentCalendar.get(12)};
    }

    public boolean currentIsMorning() {
        int i = getCurrentTimes()[0];
        return i >= 7 && i <= 9;
    }

    public boolean currentIsEveningPeak() {
        int i = getCurrentTimes()[0];
        return i >= 18 && i < 21;
    }

    public boolean currentIsNonPeak() {
        int i = getCurrentTimes()[0];
        if (i < 10 || i >= 18) {
            return i >= 21 && i < 23;
        }
        return true;
    }

    public void triggerAtSpecialTime(int i, TimerTask timerTask) {
        if (i < 0) {
            return;
        }
        Calendar currentCalendar = getCurrentCalendar(System.currentTimeMillis());
        currentCalendar.add(12, i);
        Date time = currentCalendar.getTime();
        if (i <= 0 || !time.before(new Date())) {
            Timer timer = new Timer();
            this.mTimer = timer;
            timer.schedule(timerTask, time);
        }
    }

    public Calendar getCurrentCalendar(long j) {
        Date date = new Date(j);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public void storeBeginCell(String str) {
        log("storeBeginCell cellIds is " + str);
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "storeBeginCell: mPreferenceUtils is null");
            return;
        }
        String str2 = selfStudyPreferenceUtils.getString("begin_cells", "") + str;
        int i = this.mPreferenceUtils.getInt("record_count", 0) + 1;
        this.mPreferenceUtils.putString("begin_cells", str2);
        this.mPreferenceUtils.putInt("record_count", i);
        updateBeginDay(System.currentTimeMillis());
        String[] strArr = this.globalIds;
        strArr[0] = "";
        strArr[1] = "";
        strArr[2] = "";
    }

    public void removeBeginCellStatus() {
        if (this.mPreferenceUtils == null) {
            Log.w("SelfStudyController", "removeBeginCellStatus: mPreferenceUtils is null");
            return;
        }
        this.mPreferenceUtils.remove(Arrays.asList("begin_day", "begin_cells", "record_count"));
        this.hasBeginRecordMorning = false;
    }

    public String getBeginCells() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "getBeginCells: mPreferenceUtils is null");
            return "";
        }
        return selfStudyPreferenceUtils.getString("begin_cells", "");
    }

    public int getBeginCellCounts() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "getBeginCellCounts: mPreferenceUtils is null");
            return 0;
        }
        int i = selfStudyPreferenceUtils.getInt("record_count", 0);
        log("getBeginCellCounts count = " + i);
        return i;
    }

    public void recordLessTwentyExpiredDays(int i) {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "recordLessTwentyExpiredDays: mPreferenceUtils is null");
            return;
        }
        int i2 = i > 0 ? selfStudyPreferenceUtils.getInt("less_twenty_expired_days", 0) + i : 0;
        this.mPreferenceUtils.putInt("less_twenty_expired_days", i2);
        if (i2 >= 3) {
            recordLessTwentyExpiredTimes();
        }
    }

    public void recordLessTwentyExpiredTimes() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "recordLessTwentyExpiredTimes: mPreferenceUtils is null");
        } else {
            selfStudyPreferenceUtils.putLong("less_twenty_expired_times", System.currentTimeMillis());
        }
    }

    public long getRecordLessTwentyExpiredTimes() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "getRecordLessTwentyExpiredTimes: mPreferenceUtils is null");
            return -1L;
        }
        return selfStudyPreferenceUtils.getLong("less_twenty_expired_times", -1L);
    }

    public int getRecordLessTwentyExpiredDays() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "getRecordLessTwentyExpiredDays: mPreferenceUtils is null");
            return 0;
        }
        return selfStudyPreferenceUtils.getInt("less_twenty_expired_days", 0);
    }

    public void updateBeginDay(long j) {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "updateBeginDay: mPreferenceUtils is null");
            return;
        }
        selfStudyPreferenceUtils.putLong("begin_day", j);
        this.hasBeginRecordMorning = false;
        this.mBeginDay = j;
    }

    public long getBeginDay() {
        long j = this.mBeginDay;
        if (j > 0) {
            return j;
        }
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "getBeginDay: mPreferenceUtils is null");
            return -1L;
        }
        return selfStudyPreferenceUtils.getLong("begin_day", -1L);
    }

    public void resetRouteStudyJudgeCell() {
        if (this.mPreferenceUtils == null) {
            Log.w("SelfStudyController", "resetRouteStudyJudgeCell: mPreferenceUtils is null");
        } else {
            this.mPreferenceUtils.remove(Arrays.asList("need_listen_screen", "different_cell_count", "need_get_bad_cell", "has_user_days", "use_sample_cells", "need_sample_cells", "expired_end_cells", "current_use_date", "less_twenty_expired_days", "less_twenty_expired_times", "sample_days"));
        }
    }

    public int getDifferentCellCount() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "getDifferentCellCount: mPreferenceUtils is null");
            return 0;
        }
        return selfStudyPreferenceUtils.getInt("different_cell_count", 0);
    }

    public void updateDifferentCellCount(int i) {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "updateDifferentCellCount: mPreferenceUtils is null");
        } else {
            selfStudyPreferenceUtils.putInt("different_cell_count", i);
        }
    }

    public boolean needGetBadCellScore() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "needGetBadCellScore: mPreferenceUtils is null");
            return false;
        }
        return selfStudyPreferenceUtils.getBoolean("need_get_bad_cell", false);
    }

    public void updateBadCellScoreStatus(boolean z) {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "updateBadCellScoreStatus: mPreferenceUtils is null");
        } else {
            selfStudyPreferenceUtils.putBoolean("need_get_bad_cell", z);
        }
    }

    public int getCurrentUseDate() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "getCurrentUseDate: mPreferenceUtils is null");
            return -1;
        }
        return selfStudyPreferenceUtils.getInt("current_use_date", -1);
    }

    public void updateCurrentUseDate(int i) {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "updateCurrentUseDate: mPreferenceUtils is null");
        } else {
            selfStudyPreferenceUtils.putInt("current_use_date", i);
        }
    }

    public void updateUseDays(int i) {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "updateUseDays: mPreferenceUtils is null");
        } else {
            selfStudyPreferenceUtils.putInt("has_user_days", i);
        }
    }

    public int getUseDays() {
        SelfStudyPreferenceUtils selfStudyPreferenceUtils = this.mPreferenceUtils;
        if (selfStudyPreferenceUtils == null) {
            Log.w("SelfStudyController", "getUseDays: mPreferenceUtils is null");
            return 0;
        }
        int i = selfStudyPreferenceUtils.getInt("has_user_days", 0);
        log("getUseDays is " + i);
        return i;
    }

    public void storeExpiredEndCell(CellUnit cellUnit) {
        if (this.mPreferenceUtils == null) {
            Log.w("SelfStudyController", "storeExpiredEndCell: mPreferenceUtils is null");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(cellUnit.cellFreq);
        sb.append(",");
        sb.append(cellUnit.cellPci);
        log("storeExpiredEndCell store list = ".concat(sb.toString()));
        this.mPreferenceUtils.putString("expired_end_cells", sb.toString());
    }

    /* JADX INFO: renamed from: com.android.phone.performance.SelfStudyControllerBase$7, reason: invalid class name */
    class AnonymousClass7 implements CellCallBack {
        AnonymousClass7() {
        }

        @Override // com.android.phone.performance.SelfStudyControllerBase.CellCallBack
        public void onCellLocationChanged(int i, CellUnit cellUnit) {
            if (MiuiPhoneUtils.getActiveDataPhone() == null || i != MiuiPhoneUtils.getActiveDataPhone().getPhoneId()) {
                return;
            }
            boolean zIsLteOrNr = SelfStudyControllerBase.this.isLteOrNr();
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            if (!zIsLteOrNr) {
                try {
                    selfStudyControllerBase.mTimer.cancel();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (selfStudyControllerBase.getBeginCellCounts() < 1) {
                if (SelfStudyControllerBase.this.getCellIdentity() != null) {
                    SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase2.hasRecordCellCount < selfStudyControllerBase2.globalIds.length) {
                        String combineString = SelfStudyControllerBase.this.getCombineString(String.valueOf(cellUnit.cellFreq) + cellUnit.cellPci, Long.toString(cellUnit.cellId, 16));
                        int i2 = 0;
                        while (true) {
                            SelfStudyControllerBase selfStudyControllerBase3 = SelfStudyControllerBase.this;
                            int i3 = selfStudyControllerBase3.hasRecordCellCount;
                            if (i2 <= i3) {
                                if (combineString.equalsIgnoreCase(selfStudyControllerBase3.globalIds[i2])) {
                                    return;
                                } else {
                                    i2++;
                                }
                            } else {
                                String[] strArr = selfStudyControllerBase3.globalIds;
                                selfStudyControllerBase3.hasRecordCellCount = i3 + 1;
                                strArr[i3] = combineString;
                                break;
                            }
                        }
                    }
                }
                if (TextUtils.isEmpty(SelfStudyControllerBase.this.globalIds[0])) {
                    return;
                }
                SelfStudyControllerBase selfStudyControllerBase4 = SelfStudyControllerBase.this;
                if (selfStudyControllerBase4.hasRecordCellCount >= selfStudyControllerBase4.globalIds.length) {
                    SelfStudyControllerBase.this.storeBeginCell(SelfStudyControllerBase.this.globalIds[0] + "," + SelfStudyControllerBase.this.globalIds[1] + "," + SelfStudyControllerBase.this.globalIds[2] + ",");
                    SelfStudyControllerBase selfStudyControllerBase5 = SelfStudyControllerBase.this;
                    if (selfStudyControllerBase5.hasRegisterTelephonyCallback) {
                        selfStudyControllerBase5.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$7$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onCellLocationChanged$0();
                            }
                        }, 1000L);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCellLocationChanged$0() {
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            selfStudyControllerBase.unregisterCallBack(selfStudyControllerBase.mTelephonyCallBack_V2);
            SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
            selfStudyControllerBase2.hasRegisterTelephonyCallback = false;
            selfStudyControllerBase2.screenOnBeginStudy();
        }
    }

    public boolean whetherNeedAutoStop() {
        return !currentIsMorning();
    }

    public void judgeWhetherStoreBeginCell() {
        log("judgeWhetherStoreBeginCell ");
        if (currentIsMorning() && this.mIsWorkDay) {
            this.hasBeginRecordMorning = true;
            try {
                String[] strArr = this.globalIds;
                this.hasRecordCellCount = 1;
                strArr[0] = getCurrentGlobal();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.hasRegisterTelephonyCallback) {
                return;
            }
            registerCallBack(this.mTelephonyCallBack_V2);
            this.hasRegisterTelephonyCallback = true;
        }
    }

    public boolean isMatchGoolsId(String str) {
        log("isMatchGoolsId,gid is " + str);
        return getBeginCells().contains(str);
    }

    public void judgeWhetherBeginStudy() {
        log("judgeWhetherBeginStudy");
        if (this.mIsWorkDay && currentIsMorning()) {
            if (getCellIdentity() != null && !isStudying) {
                this.lastCellMatch = isMatchGoolsId(getCurrentGlobal());
            }
            if (this.hasRegistStudyCallback) {
                return;
            }
            registerCallBack(this.studyCallBack_V2);
            this.hasRegistStudyCallback = true;
        }
    }

    public void resetLowPowerOptByCloud() {
        this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$resetLowPowerOptByCloud$10();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetLowPowerOptByCloud$10() {
        stopStudyLowPowerModeCells();
        stopPingpongStudy();
        resetLowPowerOpt(false);
    }

    public boolean hasTriggeredOffsetOptimizationForCurrentCell() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            return true;
        }
        String globalCellId = activeDataPhone.getCurrentCellIdentity().getGlobalCellId();
        if (TextUtils.isEmpty(globalCellId)) {
            return true;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        for (OffsetCell offsetCell : this.mGameOffsetCells) {
            if (globalCellId.equals(offsetCell.globalCellId)) {
                long j = offsetCell.optimizationTime;
                if (j != 0 && jElapsedRealtime - j > 600000) {
                    break;
                }
                return true;
            }
        }
        return false;
    }

    protected OffsetCell getCurrentCellInfo(Phone phone) {
        int pci;
        int nrarfcn;
        int i;
        if (phone == null) {
            return null;
        }
        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
        String globalCellId = currentCellIdentity.getGlobalCellId();
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            pci = cellIdentityLte.getPci();
            nrarfcn = cellIdentityLte.getEarfcn();
            i = Rat.Lte.value;
        } else {
            if (!(currentCellIdentity instanceof CellIdentityNr)) {
                return null;
            }
            CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
            pci = cellIdentityNr.getPci();
            nrarfcn = cellIdentityNr.getNrarfcn();
            i = Rat.Nr.value;
        }
        int i2 = i;
        return new OffsetCell(nrarfcn, pci, 0, currentCellIdentity.getPlmn(), i2, globalCellId);
    }

    class StudyTask extends TimerTask {
        StudyTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            SelfStudyControllerBase.this.log("StudyTask");
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            selfStudyControllerBase.mGlobalCurrentCellId = "";
            synchronized (selfStudyControllerBase.mLock) {
                try {
                    SelfStudyControllerBase.this.sendSelfStudyRequest(23, 1, new byte[]{1});
                    SelfStudyControllerBase.this.mLock.wait(2000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(SelfStudyControllerBase.this.mGlobalCurrentCellId)) {
                SelfStudyControllerBase selfStudyControllerBase2 = SelfStudyControllerBase.this;
                if (!selfStudyControllerBase2.isMatchGoolsId(selfStudyControllerBase2.mGlobalCurrentCellId)) {
                    if (SelfStudyControllerBase.isStudying) {
                        SelfStudyControllerBase.this.stopStudy();
                    }
                    try {
                        CellUnit cellUnit = new CellUnit();
                        CellIdentity cellIdentity = SelfStudyControllerBase.this.getCellIdentity();
                        if (cellIdentity != null) {
                            if (cellIdentity instanceof CellIdentityLte) {
                                cellUnit.cellPci = ((CellIdentityLte) cellIdentity).getPci();
                                cellUnit.cellFreq = ((CellIdentityLte) cellIdentity).getEarfcn();
                                cellUnit.cellId = Long.parseLong(cellIdentity.getGlobalCellId().substring(5), 16);
                            } else if (cellIdentity instanceof CellIdentityNr) {
                                cellUnit.cellPci = ((CellIdentityNr) cellIdentity).getPci();
                                cellUnit.cellFreq = ((CellIdentityNr) cellIdentity).getNrarfcn();
                                cellUnit.cellId = Long.parseLong(cellIdentity.getGlobalCellId().substring(5), 16);
                            }
                            SelfStudyControllerBase.this.storeExpiredEndCell(cellUnit);
                            return;
                        }
                        return;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
            }
            SelfStudyControllerBase.this.log("CellIdExpiredTimerTask invalid  mGlobalCurrentCellId is " + SelfStudyControllerBase.this.mGlobalCurrentCellId);
        }
    }

    public void updateCellsInfoDB(List list) {
        log("updateCellsInfoDB");
        if (list == null) {
            return;
        }
        SQLiteDatabase writableDatabase = this.dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_bad_cell", (Integer) 0);
        writableDatabase.update("cell_table", contentValues, "score = ?", new String[]{"0"});
        for (int i = 0; i < list.size(); i++) {
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("is_bad_cell", Integer.valueOf(((CellInfo) list.get(i)).getIsBadCell()));
            contentValues2.put("current_cell_count", Integer.valueOf(((CellInfo) list.get(i)).getCurrentCellCount()));
            contentValues2.put("rrc_exception", Integer.valueOf(((CellInfo) list.get(i)).getRrcExceptionCount()));
            contentValues2.put("high_bler_count", Integer.valueOf(((CellInfo) list.get(i)).getHighBlerCount()));
            contentValues2.put("low_ul_grant", Integer.valueOf(((CellInfo) list.get(i)).getLowUlGrantCount()));
            contentValues2.put("score", (Integer) 0);
            contentValues2.put("rlf_count", Integer.valueOf(((CellInfo) list.get(i)).getRlfCount()));
            contentValues2.put("bad_rsrp_duration", Integer.valueOf(((CellInfo) list.get(i)).getBadRsrpDuration()));
            contentValues2.put("bad_rsrq_duration", Integer.valueOf(((CellInfo) list.get(i)).getBadRsrqDuration()));
            contentValues2.put("duration", Integer.valueOf(((CellInfo) list.get(i)).getDuration()));
            contentValues2.put("low_dl_grant", Integer.valueOf(((CellInfo) list.get(i)).getLowDlGrantCount()));
            contentValues2.put("data_type", Integer.valueOf(((CellInfo) list.get(i)).getDataType()));
            contentValues2.put("have_been_optimized", (Integer) 0);
            writableDatabase.update("cell_table", contentValues2, " pci = ? and freq = ? and rat = ?", new String[]{String.valueOf(((CellInfo) list.get(i)).getPci()), String.valueOf(((CellInfo) list.get(i)).getFreq()), String.valueOf(((CellInfo) list.get(i)).getRat())});
        }
        writableDatabase.close();
    }

    public String getCurrentDataPhoneCellId() {
        CellIdentity currentCellIdentity;
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null || (currentCellIdentity = activeDataPhone.getCurrentCellIdentity()) == null) {
            return null;
        }
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            return cellIdentityLte.getPlmn() + "_" + cellIdentityLte.getCi();
        }
        if (!(currentCellIdentity instanceof CellIdentityNr)) {
            return null;
        }
        CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
        return cellIdentityNr.getPlmn() + "_" + cellIdentityNr.getNci();
    }

    public String getCellIdByPhoneId(int i) {
        CellIdentity currentCellIdentity;
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || (currentCellIdentity = phone.getCurrentCellIdentity()) == null) {
            return null;
        }
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            return cellIdentityLte.getPlmn() + "_" + cellIdentityLte.getCi();
        }
        if (!(currentCellIdentity instanceof CellIdentityNr)) {
            return null;
        }
        CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
        return cellIdentityNr.getPlmn() + "_" + cellIdentityNr.getNci();
    }

    public void processMobileStateEvent(boolean z) {
        if (!this.mIsMobileDataOn && z && this.isNeedDelayStudyByDataConnection) {
            this.isNeedDelayStudyByDataConnection = false;
            beginStudy();
            try {
                triggerAtSpecialTime(10, new StudyTask());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.mIsMobileDataOn = z;
        if (z && this.mWorkHandler.hasMessages(1022)) {
            log("NR pdu is exception and now is lte and it can be SA");
            this.mWorkHandler.removeMessages(1022);
            enableNr(true);
        }
        if (!this.mIsMobileDataOn) {
            this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processMobileStateEvent$11();
                }
            }, 3000L);
        }
        this.mNetId = z ? getDefaultNetworkNetId() : -1;
        if (z && this.isVideoFront && this.mIsInWeChatVoip) {
            delayProcessWechatEnter();
        }
        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            processMobileDataStateChange(this.mIsMobileDataOn);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processMobileStateEvent$11() {
        if (this.mIsMobileDataOn) {
            return;
        }
        stopCollectMultiRouteAndPredict(true);
    }

    private int getDefaultNetworkNetId() {
        Network activeNetwork = this.mCm.getActiveNetwork();
        if (activeNetwork != null) {
            return activeNetwork.getNetId();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLinkReliability(int i, int i2) {
        log("processLinkReliability");
        if (i < 100 || i != this.mNetId || i2 == 0 || getCellIdentity() == null) {
            return;
        }
        while (i2 != 0) {
            if ((i2 & 1) > 0) {
                this.cellUnitScore.socre++;
            }
            i2 >>= 1;
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            SelfStudyControllerBase.this.mWorkHandler.obtainMessage(1035, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            SelfStudyControllerBase.this.mWorkHandler.obtainMessage(1027, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            if (Build.IS_INTERNATIONAL_BUILD) {
                return;
            }
            SelfStudyControllerBase.this.mWorkHandler.obtainMessage(1028, i, 0, cellLocation).sendToTarget();
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            SelfStudyControllerBase.this.mWorkHandler.obtainMessage(1046, i, i2).sendToTarget();
        }
    }

    public CellIdentity getCellIdentity() {
        if (isLteOrNr()) {
            return MiuiPhoneUtils.getActiveDataPhone().getServiceState().getNetworkRegistrationInfo(2, 1).getCellIdentity();
        }
        return null;
    }

    public boolean isLteOrNr() {
        if (MiuiPhoneUtils.getActiveDataPhone() != null) {
            return MiuiPhoneUtils.getActiveDataPhone().getServiceState().getRilDataRadioTechnology() == 14 || MiuiPhoneUtils.getActiveDataPhone().getServiceState().getRilDataRadioTechnology() == 19 || MiuiPhoneUtils.getActiveDataPhone().getServiceState().getRilDataRadioTechnology() == 20;
        }
        return false;
    }

    public void setCellUnitScore(CellIdentity cellIdentity) {
        if (cellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
            this.cellUnitScore.cellPci = cellIdentityLte.getPci();
            this.cellUnitScore.cellFreq = cellIdentityLte.getEarfcn();
            String globalCellId = cellIdentity.getGlobalCellId();
            if (globalCellId != null) {
                this.cellUnitScore.cellId = Long.parseLong(globalCellId.substring(5), 16);
            }
        } else {
            boolean z = cellIdentity instanceof CellIdentityNr;
            CellUnit cellUnit = this.cellUnitScore;
            if (z) {
                CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
                cellUnit.cellPci = cellIdentityNr.getPci();
                this.cellUnitScore.cellFreq = cellIdentityNr.getNrarfcn();
                String globalCellId2 = cellIdentity.getGlobalCellId();
                if (globalCellId2 != null) {
                    this.cellUnitScore.cellId = Long.parseLong(globalCellId2.substring(5), 16);
                }
            } else {
                cellUnit.cellFreq = 0;
                cellUnit.cellPci = 0;
                cellUnit.cellId = 0L;
            }
        }
        this.cellUnitScore.socre = 0;
    }

    class BadCellStudyTimerTask extends TimerTask {
        BadCellStudyTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            SelfStudyControllerBase.this.log("BadCellStudyTimerTask");
            SelfStudyControllerBase.this.sendBadCellToModem(new ArrayList());
            SelfStudyControllerBase.this.stopToUse();
            SelfStudyControllerBase.this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$BadCellStudyTimerTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$run$0();
                }
            }, 1000L);
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            if (selfStudyControllerBase.hasRegisterBadCellCallback) {
                selfStudyControllerBase.hasRegisterBadCellCallback = false;
                selfStudyControllerBase.unregisterCallBack(selfStudyControllerBase.mBadCellCallBack_V2);
            }
            SelfStudyControllerBase.this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.performance.SelfStudyControllerBase$BadCellStudyTimerTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$run$1();
                }
            }, 1500L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
            selfStudyControllerBase.updateCellsInfoDB(selfStudyControllerBase.mBadCellListInfos);
            SelfStudyControllerBase.this.mBadCellListInfos.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1() {
            SelfStudyControllerBase.this.beginToUse();
            SelfStudyControllerBase.this.isCellUpdating = false;
        }
    }

    public String getCurrentGlobal() {
        synchronized (this.mLock) {
            try {
                this.mGlobalCurrentCellId = "";
                sendSelfStudyRequest(23, 1, new byte[]{1});
                this.mLock.wait(2000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.mGlobalCurrentCellId;
    }

    public void registerCallBack(CellCallBack cellCallBack) {
        if (cbs.size() <= 0) {
            sendSelfStudyRequest(24, 1, new byte[]{1});
        }
        cbs.add(cellCallBack);
    }

    public void unregisterCallBack(CellCallBack cellCallBack) {
        cbs.remove(cellCallBack);
        if (cbs.size() <= 0) {
            sendSelfStudyRequest(24, 1, new byte[]{0});
        }
    }

    protected void registerTelephony() {
        if (this.mTelephonyCommonCallBack == null) {
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCommonCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
        log("registerTelephony");
    }

    protected void unRegisterTelephony() {
        TelephonyCallBack telephonyCallBack = this.mTelephonyCommonCallBack;
        if (telephonyCallBack != null) {
            TelephonyStatAdapter.unregisterCallBack(telephonyCallBack);
            this.mTelephonyCommonCallBack = null;
        }
        log("unRegisterTelephony()");
    }

    public String getCombineString(String str, String str2) {
        if (str == null || str2 == null) {
            return "";
        }
        if (str2.length() >= 4) {
            return str.concat(str2.substring(0, 4));
        }
        return str.concat(str2);
    }

    public boolean isCurrentCellResident() {
        String currentDataPhoneCellId;
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (!Build.IS_INTERNATIONAL_BUILD && !this.mResidentCellIds.isEmpty() && activeDataPhone != null && (currentDataPhoneCellId = getCurrentDataPhoneCellId()) != null && this.mPreferenceUtils.getInt("study_resident_cells_days", 0) >= 2) {
            Iterator it = this.mResidentCellIds.iterator();
            while (it.hasNext()) {
                String[] strArrSplit = ((String) it.next()).split("_");
                if (strArrSplit.length >= 4) {
                    if (currentDataPhoneCellId.equals(strArrSplit[2] + "_" + strArrSplit[3])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isResidentCellByGci(String str) {
        if (!Build.IS_INTERNATIONAL_BUILD && !this.mResidentCellIds.isEmpty() && str != null) {
            Iterator it = this.mResidentCellIds.iterator();
            while (it.hasNext()) {
                String[] strArrSplit = ((String) it.next()).split("_");
                if (strArrSplit.length >= 4) {
                    if (str.equals(strArrSplit[2] + strArrSplit[3])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isResidentCellScene() {
        return isCurrentCellResident() || isInHomeScence() || isInCompanyScence();
    }

    public boolean isSubwayScene() {
        return this.mIsSubwayStateIn || isInAISubWayScene() || this.mIsSubway;
    }

    public void updateResidentCellDbForVonrOpt(String str, int i, boolean z, String str2, long j) {
        SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
        if (selfStudyDatabaseHelper == null || selfStudyDatabaseHelper.getWritableDatabase() == null) {
            return;
        }
        SelfStudyDatabaseHelper selfStudyDatabaseHelper2 = this.dbHelper;
        selfStudyDatabaseHelper2.updateBarCellOrVonrLimitFlagForResidentCells(selfStudyDatabaseHelper2.getWritableDatabase(), str, i, j, str2, z);
    }

    public boolean isEnterGameOptimizer() {
        return mEnterGameOptimizer;
    }

    public long queryLatestGameCellTime(String str) {
        long j = 0;
        try {
            Cursor cursorQuery = this.dbHelper.query(str, new String[]{"MAX(update_time_mills)"}, null, null, null, null, null);
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.moveToFirst() && !cursorQuery.isNull(0)) {
                        j = cursorQuery.getLong(0);
                        log("queryLatestGameCellTime latestGameCellTime = " + j);
                    }
                } catch (Throwable th) {
                    try {
                        cursorQuery.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                }
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            return j;
        } catch (Exception e) {
            Log.e("QueryError", "Failed to query latest game cell time", e);
            return j;
        }
    }

    /* JADX WARN: Code duplicated, block: B:27:0x0080 A[RETURN] */
    public boolean isTopGameCell(String str, int i, int i2, long j) {
        String[] strArr = {"gci", "freq", "pci", "rat", "update_time_mills", "average_game_data_lag_ratio"};
        String[] strArr2 = {String.valueOf(j), String.valueOf(queryLatestGameCellTime("game_bad_cell_table") - 1200000)};
        boolean z = false;
        try {
            Cursor cursorQuery = this.dbHelper.query("game_bad_cell_table", strArr, "rat = ? AND update_time_mills > ?", strArr2, null, null, "average_game_data_lag_ratio ASC");
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.getCount() <= 0) {
                        log("no need set offset");
                        if (cursorQuery == null) {
                            return false;
                        }
                    } else if (cursorQuery.moveToFirst()) {
                        if (cursorQuery.getString(0).equals(str) && cursorQuery.getInt(1) == i2 && cursorQuery.getInt(2) == i) {
                            log("isTopGameCell top1");
                            z = true;
                        } else {
                            log("not topGameCell ");
                        }
                    }
                } catch (Throwable th) {
                    if (cursorQuery == null) {
                        throw th;
                    }
                    try {
                        cursorQuery.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                }
            } else {
                log("no need set offset");
                if (cursorQuery == null) {
                    return false;
                }
            }
            cursorQuery.close();
            return z;
        } catch (Exception e) {
            Log.e("QueryError", "Failed to check top game cell", e);
            return z;
        }
    }

    public int parseStringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    public boolean isSetDataStallForGameOpt(boolean z) {
        this.isGameSetDataStall = z;
        return z;
    }

    public boolean isCellularNetworkSwitchStillOn() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "gamebooster_cellular_network", 1) == 1;
    }

    public void observeCellularNetworkSwitchStatus(boolean z) {
        ContentObserver contentObserver;
        if (z && this.mCellularNetworkStatusObserver == null) {
            Uri uriFor = Settings.Secure.getUriFor("gamebooster_cellular_network");
            ContentObserver contentObserver2 = new ContentObserver(this.mWorkHandler) { // from class: com.android.phone.performance.SelfStudyControllerBase.11
                @Override // android.database.ContentObserver
                public void onChange(boolean z2) {
                    boolean z3 = SelfStudyControllerBase.this.mCellularNetworkSwitchOn;
                    SelfStudyControllerBase selfStudyControllerBase = SelfStudyControllerBase.this;
                    selfStudyControllerBase.mCellularNetworkSwitchOn = selfStudyControllerBase.isCellularNetworkSwitchStillOn();
                    SelfStudyControllerBase.this.log("mCellularNetworkSwitchOn: " + SelfStudyControllerBase.this.mCellularNetworkSwitchOn);
                    if (z3 && !SelfStudyControllerBase.this.mCellularNetworkSwitchOn) {
                        SelfStudyControllerBase.this.mGameNetworkSwitchStatus = false;
                        SelfStudyControllerBase.this.resetResidentGameCellOptimization(true);
                        SelfStudyControllerBase.this.unRegisterGameBoosterGameDelayListener();
                    } else {
                        if (!SelfStudyControllerBase.this.mCellularNetworkSwitchOn || z3) {
                            return;
                        }
                        SelfStudyControllerBase.this.mGameNetworkSwitchStatus = true;
                        SelfStudyControllerBase.this.registerGameBoosterGameDelayListener();
                    }
                }
            };
            this.mCellularNetworkStatusObserver = contentObserver2;
            this.mContentResolver.registerContentObserver(uriFor, false, contentObserver2);
            return;
        }
        if (z || (contentObserver = this.mCellularNetworkStatusObserver) == null) {
            return;
        }
        this.mContentResolver.unregisterContentObserver(contentObserver);
        this.mCellularNetworkStatusObserver = null;
    }

    public int getLastGameDelay(int i) {
        return Math.min(Math.max(i, 0), this.mGameDelayLevelCount.length - 1);
    }

    public void getGameDelayDur(boolean z, String str, ConcurrentHashMap concurrentHashMap) {
        if (concurrentHashMap == null || isBelowMtkDataOptVersionFive() || !z) {
            return;
        }
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < 10; i5++) {
            int i6 = this.gameStatistic[i5];
            if (i6 == 0) {
                i++;
            } else if (i6 == 1) {
                i2++;
            } else if (i6 == 2) {
                i3++;
            } else if (i6 == 3) {
                i4++;
            }
        }
        concurrentHashMap.put("level0" + str, Integer.valueOf(i));
        concurrentHashMap.put("level1" + str, Integer.valueOf(i2));
        concurrentHashMap.put("level2" + str, Integer.valueOf(i3));
        concurrentHashMap.put("level3" + str, Integer.valueOf(i4));
    }

    public void processgameDelayChangedForHasDelayGame(int i) {
        if (this.mIsMobileDataOn) {
            log("processgameDelayChangedForHasDelayGame:  gameDelayLevel: " + i);
            if (i >= 3) {
                log("gameDelayLevel >= 3");
                this.mWorkHandler.removeMessages(1, 3);
                this.mWorkHandler.removeMessages(1, 4);
                if (this.mWorkHandler.hasMessages(1, 2)) {
                    return;
                }
                this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(1, 2), 3000L);
                return;
            }
            if (i == 2) {
                log("gameDelayLevel 2");
                this.mWorkHandler.removeMessages(1, 4);
                if (this.mWorkHandler.hasMessages(1, 3)) {
                    return;
                }
                this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(1, 3), MirrorDesktopHelper.TIMEOUT_MILLIS);
                return;
            }
            if (i != 1) {
                this.mWorkHandler.removeMessages(1, 2);
                this.mWorkHandler.removeMessages(1, 3);
                this.mWorkHandler.removeMessages(1, 4);
            } else {
                log("gameDelayLevel 1");
                if (this.mWorkHandler.hasMessages(1, 4)) {
                    return;
                }
                this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(1, 4), 10000L);
            }
        }
    }

    public void delayCheck(int i) {
        if (i == 2) {
            checkLevelOverTime(3);
            return;
        }
        if (i == 3) {
            checkLevelOverTime(2);
            return;
        }
        if (i == 4) {
            checkLevelOverTime(1);
        } else if (i == 5) {
            checkLteAfterDisableSa();
        } else {
            if (i != 6) {
                return;
            }
            handleNeighborCheckTimeout();
        }
    }

    protected void handleNeighborCheckTimeout() {
        Log.d("SelfStudyController", "Neighbor check timeout, skip optimization");
        this.mPendingGameOptAfterNeighborCheck = false;
    }

    protected void requestNeighborCellInfoCommon() {
        requestNeighborCellInfo();
        this.mPendingGameOptAfterNeighborCheck = true;
        this.mWorkHandler.removeMessages(1, 6);
        this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(1, 6), 2000L);
        removeGameDelayCheckMessages();
    }

    public int getGameDelayLevel(int i, String str) {
        int[] iArr;
        if (str == null) {
            return 0;
        }
        if (COMMON_GAME_DELAY_THRESHOLDS_PACKAGES.contains(str)) {
            iArr = sCommonGameDelayThresholds;
        } else if (str.equals("com.tencent.lolm")) {
            iArr = sLolmGameDelayThresholds;
        } else if (str.equals("com.tencent.jkchess")) {
            iArr = sJkchessGameDelayThresholds;
        } else {
            iArr = str.equals("com.tencent.tmgp.dfm") ? sDfmGameDelayThresholds : null;
        }
        if (iArr != null) {
            for (int length = iArr.length - 1; length >= 0; length--) {
                if (i > iArr[length]) {
                    return length + 1;
                }
            }
        }
        return 0;
    }

    public void removeGameDelayCheckMessages() {
        this.mWorkHandler.removeMessages(1, 2);
        this.mWorkHandler.removeMessages(1, 3);
        this.mWorkHandler.removeMessages(1, 4);
        this.mWorkHandler.removeMessages(1, 5);
    }

    public void checkStaticState(boolean z) {
        int[] iArr = this.mStaticState;
        iArr[0] = iArr[0] + 1;
        if (z) {
            XmTelephonyManagerInner.getDefault().getModemScene(this.mResultReceiver);
        }
        Log.d("SelfStudyController", "checkStaticState: " + this.mStaticState[0] + "," + this.mStaticState[1]);
    }

    public boolean isStaticScene() {
        int[] iArr;
        int i;
        boolean z = true;
        if (!this.isGameDebug && ((i = (iArr = this.mStaticState)[0]) <= 0 || iArr[1] <= ((double) i) * 0.8d)) {
            z = false;
        }
        SystemProperties.set("gsm.game.scene", z ? "1" : "0");
        Log.d("SelfStudyController", "IsStaticScene: " + z);
        return z;
    }

    public void resetStaticState() {
        Arrays.fill(this.mStaticState, 0);
    }

    public void requestNeighborCellInfo() {
        log("requestNeighborCellInfo ");
        XmTelephonyManagerInner.getDefault().getDdsAssistantInfo(this.mResultReceiver);
    }

    protected void startModemModel() {
        if (MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) {
            int[] iArr = this.mModelCfgData;
            if (iArr == null || iArr.length == 0) {
                log("startModemModel: mModelCfgData is empty");
            } else {
                XmTelephonyManagerInner.getDefault().setModemModelCfg(this.mModelCfgData, this.mModelCfgResultReceiver);
            }
        }
    }

    protected void stopModemModel() {
        if (MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) {
            XmTelephonyManagerInner.getDefault().setModemCallbackSwitch(false, this.mModelEnableResultReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNeighborCellResult(ArrayList arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            this.mNeighborCellManager.resetNeighborCellState();
            Log.d("SelfStudyController", "No neighbor cells");
            return;
        }
        this.mNeighborCellManager.handleNeighborCellResult(arrayList, getServiceCellRat());
        Log.d("SelfStudyController", "Neighbor cells count=" + arrayList.size() + ", mPendingGameOptAfterNeighborCheck=" + this.mPendingGameOptAfterNeighborCheck);
        if (this.mPendingGameOptAfterNeighborCheck) {
            this.mWorkHandler.removeMessages(1, 6);
            if (this.mNeighborCellManager.shouldDegrade()) {
                Log.d("SelfStudyController", "Neighbor cells meet degrade condition, call handleGameStall");
                handleGameStall();
            } else if (this.mNeighborCellManager.shouldTriggerDataStall()) {
                Log.d("SelfStudyController", "Neighbor cells meet dataStall condition, call handleGameStall");
                handleGameStall();
            } else {
                Log.d("SelfStudyController", "No good neighbor cells, skip optimization");
            }
            this.mPendingGameOptAfterNeighborCheck = false;
        }
        this.mNeighborCellManager.resetNeighborCellState();
    }

    private int getServiceCellRat() {
        CellInfo currentCellInfoV3 = getCurrentCellInfoV3();
        if (currentCellInfoV3 != null) {
            return currentCellInfoV3.rat;
        }
        return 0;
    }

    public boolean isTopCellForStaticGame(String str, List list) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return list.contains(str);
    }

    public void saveTopCellForStaticGame(String str, long j, ConcurrentHashMap concurrentHashMap, List list) {
        list.clear();
        if (isValidParameters(str, j, concurrentHashMap)) {
            double d = Double.MAX_VALUE;
            for (Map.Entry entry : concurrentHashMap.entrySet()) {
                String str2 = (String) entry.getKey();
                ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) entry.getValue();
                if (concurrentHashMap2 != null && ((Long) concurrentHashMap2.get("rat")) != null) {
                    long jLongValue = ((Long) concurrentHashMap2.getOrDefault("lagdur", 0L)).longValue();
                    long jLongValue2 = ((Long) concurrentHashMap2.getOrDefault("dur", 0L)).longValue();
                    if (jLongValue2 != 0) {
                        double d2 = jLongValue / jLongValue2;
                        if (d2 < d) {
                            list.clear();
                            list.add(str2);
                            d = d2;
                        } else if (Math.abs(d2 - d) < 1.0E-6d) {
                            list.add(str2);
                        }
                    }
                }
            }
        }
    }

    private boolean isValidParameters(String str, long j, ConcurrentHashMap concurrentHashMap) {
        return ((j != 1 && j != 2) || concurrentHashMap == null || concurrentHashMap.isEmpty() || str == null || str.isEmpty()) ? false : true;
    }

    private String makeGci(String str) {
        if (str == null || str.isEmpty()) {
            return "MISSING";
        }
        String[] strArrSplit = str.split("_");
        if (strArrSplit.length == 0) {
            return "MISSING";
        }
        return makeSensitive(strArrSplit[strArrSplit.length - 1], 6);
    }

    private String makeSensitive(String str, int i) {
        if (str == null || str.length() <= i) {
            return str;
        }
        int length = str.length() - i;
        return "*".repeat(length) + str.substring(length);
    }

    public static boolean isBelowMtkDataOptVersionFive() {
        return MTK_DATA_OPT_VERSION < 5;
    }

    public static void localLog(String str, String str2, int i) {
        if (isBelowMtkDataOptVersionFive()) {
            return;
        }
        localLog("SelfStudyController", str, str2, i);
    }

    private static void localLog(String str, String str2, String str3, int i) {
        String str4 = str + "." + str2;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(i));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("SelfStudyController dump start------");
        if (this.mIsResidentCellsOptimizationSupported) {
            dumpResidentCellsTable(printWriter);
        } else {
            printWriter.println("Resident cells optimization disabled");
        }
        dumpLocalLogs(printWriter);
        printWriter.println("SelfStudyController dump end------");
        printWriter.flush();
    }

    private void dumpResidentCellsTable(PrintWriter printWriter) {
        String[] strArr = {"plmn", "pci", "freq", "rat", "scene", "subId", "have_been_optimized", "store_time", "call_score", "data_score", "data_count", "call_count", "rsrp_avg", "sinr_avg", "global_cell_id"};
        SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
        if (selfStudyDatabaseHelper == null) {
            printWriter.println("Error: Database helper is null");
            return;
        }
        try {
            Cursor cursorQuery = selfStudyDatabaseHelper.query("resident_cells_table", strArr, null, null, null, null, null);
            try {
                dumpCursorData(printWriter, cursorQuery, "resident_cells_table", strArr);
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } catch (Throwable th) {
                if (cursorQuery == null) {
                    throw th;
                }
                try {
                    cursorQuery.close();
                    throw th;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                    throw th;
                }
            }
        } catch (Exception e) {
            printWriter.println("SQLite error: " + e.getMessage());
        }
    }

    private void dumpCursorData(PrintWriter printWriter, Cursor cursor, String str, String[] strArr) {
        printWriter.println("Table name: " + str);
        printWriter.println("Row count: " + cursor.getCount());
        if (!cursor.moveToFirst()) {
            printWriter.println("No data found");
            return;
        }
        int[] iArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            iArr[i] = cursor.getColumnIndex(strArr[i]);
        }
        do {
            printCursorRow(printWriter, cursor, strArr, iArr);
        } while (cursor.moveToNext());
    }

    private void printCursorRow(PrintWriter printWriter, Cursor cursor, String[] strArr, int[] iArr) {
        StringBuilder sb = new StringBuilder("Row: ");
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            int i2 = iArr[i];
            if (i2 >= 0) {
                if ("global_cell_id".equals(str)) {
                    String strMakeGci = makeGci(cursor.getString(i2));
                    sb.append("key=");
                    sb.append(strMakeGci);
                } else {
                    sb.append(str);
                    sb.append("=");
                    sb.append(cursor.getString(i2));
                }
            } else {
                sb.append(str);
                sb.append("=MISSING");
            }
            if (i < strArr.length - 1) {
                sb.append(", ");
            }
        }
        printWriter.println(sb.toString());
    }

    private void dumpLocalLogs(PrintWriter printWriter) {
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
    }
}
