package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Network;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.util.ArrayUtils;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.IModemModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.network.MiuiNetworkMonitor;
import com.android.phone.performance.CellInfo;
import com.android.phone.performance.SelfStudyControllerBase;
import com.android.phone.performance.SelfStudyDatabaseHelper;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.a.a;
import com.ot.pubsub.g.f;
import com.ot.pubsub.util.v;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyUtils;
import miuix.preference.flexible.AbstractBaseTemplate;
import miuix.preference.flexible.PreferenceMarkLevel;

/* JADX INFO: loaded from: classes.dex */
public class MtkSelfStudyController extends SelfStudyControllerBase {
    private boolean VDBG;
    String curGameGpfr;
    long curGameRat;
    int curGameScene;
    long endGameTime;
    int enterGameResidentDays;
    int enterWechatVideoDays;
    private boolean isBattle;
    private boolean isCellChanged;
    private boolean isEnterGame;
    private boolean isFirst20min;
    private boolean isGameAiLag;
    private boolean isGameLag;
    private boolean isGameLagFromModem;
    private boolean isGameTraModelLag;
    private boolean isLastAiAndTraModelLag;
    private boolean isLte;
    private boolean isNr2Lte;
    boolean isResidentCell;
    boolean isSetBWEable;
    private boolean[] isSetGameAtCmd;
    private boolean isSetGameTopCell;
    private boolean isSetNrTop;
    private boolean isSetOffsetFromModem;
    private boolean isStaticCell;
    private int[] lowerPowerOpt;
    int[] mActionForStream;
    private Runnable mAutoStopColdDataStallTask;
    private boolean mBaseModelIsInit;
    private String[] mChinaCMCC;
    private ConcurrentHashMap mCurrentCellStats;
    private ConcurrentHashMap mCurrentGameCellStats;
    private int mCurrentGameDelay;
    private int mCurrentGameDelayLevel;
    private ConcurrentHashMap mCurrentStreamMediaCellStats;
    ContentObserver mDataEnabledObserver;
    private ConcurrentHashMap mDoNextCellFromMd;
    private int mEnterGameAiBoosterCount;
    int mEnterGameBattleCount;
    boolean mEnterGameBattleState;
    int mEnterGameBoosterCount;
    protected GameBoosterGameDelayListener mGameBoosterGameDelayListener;
    private GameCellDdsSaOpt mGameCellOpt;
    String mGameLagGpfrModem;
    private ConcurrentHashMap mGameOffsetCell;
    private BroadcastReceiver mGlobalMtkSelfStudyBroadCastReceiver;
    private boolean mInSleepMode;
    private boolean mIsAirPlane;
    private boolean mIsDuringCall;
    private boolean mIsEnableLLM;
    private boolean mIsEnableLargeBW;
    long mLastStreamMediaCellTime;
    private int[] mLastTargetFreq;
    String mLastTopGamePackage;
    private HashMap[] mLowPowerSnapshot;
    private ConcurrentHashMap mLteCellStats;
    private ConcurrentHashMap mLteGameCellStats;
    private ArrayList mModemAiOffsetList;
    private ConcurrentHashMap mMtkSbpHistoryStats;
    private BroadcastReceiver mMtkSelfStudyBroadCastReceiver;
    protected NetworkMetricsController mNetworkMetricsController;
    private ConcurrentHashMap mNrCellStats;
    private ConcurrentHashMap mNrGameCellStats;
    private SparseArray mPingPongRecords;
    private boolean mRealTimeOptimizationIsInit;
    private NetworkSelfRecovery mRecovery;
    private long mRecoveryTime;
    private ServiceState[] mServiceState;
    private boolean mSetGameOptNr2Lte;
    private long mSetPenaltTime;
    protected SmartNetworkGameController mSmartNetworkGameController;
    private ConcurrentHashMap mTop2Cell;
    private List mTopCellForStaticGame;
    private String mTopGamePackage;
    private WechatDdsSaOpt mWechatOpt;
    private ConcurrentHashMap mWechatOptStats;
    private ArrayList mYesterdayStats;
    int maxSavedGameCells;
    long minsertDbTime;
    long mlastCellTime;
    long mlastGameCellTime;
    private int mlastGameDelay;
    private long mlastGameLevelTime;
    int mstage1;
    int mstage2;
    int mstage3;
    protected MiuiNetworkMonitor.IMiuiNetworkMonitorListener networkMonitorListener;
    int oldFreq;
    int oldGameFreq;
    String oldGameGci;
    int oldGamePci;
    long oldGameRat;
    int oldGameRsrp;
    private int oldGameRsrq;
    int oldGameSnr;
    int oldPci;
    long oldRat;
    int oldStreamFreq;
    OffsetCell oldStreamOffSetCell;
    int oldStreamPci;
    long oldStreamRat;
    private int sceneCount;
    int stageA3;
    int stageA4;
    int stageA5;
    private long startGameLagFromGameAi;
    long startGameLagFromModem;
    private long startGameLagFromTraModel;
    long startGameLagTime;
    long startGameTime;
    private int staticCount;
    int studyBadCellDays;
    int studyGameResidentCellDays;
    public final int MAX_REQ_ROUTE_UNIT = 6;
    public final int ROUTE_LENGTH_V2 = 30;
    public long mTempCellId = 0;
    public String ulAllocRate = "0xFFFFFFFF";
    public String ulStallUlTput = "9";
    public String ulStallDlTput = "0xFFFFFFFF";
    public String ulStallUlPendData = "15000";
    int maxSavedCells = 32;
    int maxSavedResidentCells = 16;
    int maxSavedDynamicCells = 64;
    private boolean mWechatVideoOptimizationRst = true;
    private boolean mWechatA3ActionOptRst = false;
    private final String curRat = "rat";
    private final String duration = "duration";
    private final String newUpdateTime = "updateTime";
    private final String totalCnt = "count";
    private int wechatOptAction = -1;

    private double calculateRatio(long j, long j2) {
        if (j2 == 0) {
            return 0.0d;
        }
        return j / j2;
    }

    class PingPongRecord {
        int arfcn;
        int band;
        long cellId;
        int pci;
        String plmn;
        int rat;

        private PingPongRecord() {
        }
    }

    enum MtkAtCmd {
        ENABLE_54G_INDOOR_MODE("AT+ESBP=5,\"SBP_MODEM_54G_INDOOR_MODE\",1", "+ESBP"),
        DISABLE_54G_INDOOR_MODE("AT+ESBP=5,\"SBP_MODEM_54G_INDOOR_MODE\",0", "+ESBP"),
        ENABLE_SBP_DATA_AM_ERLCDL_REORDER_TIMER_OPT("AT+ESBP=6,\"SBP_DATA_AM_ERLCDL_REORDER_TIMER_OPT\",2", "+ESBP"),
        DISABLE_SBP_DATA_AM_ERLCDL_REORDER_TIMER_OPT("AT+ESBP=6,\"SBP_DATA_AM_ERLCDL_REORDER_TIMER_OPT\",0", "+ESBP"),
        ENABLE_NRRC_INTRA_RAT_BLIND_REDIRECT_BY_A2_ENH("AT+ESBP=5,\"SBP_NRRC_INTRA_RAT_BLIND_REDIRECT_BY_A2_ENH\",1", "+ESBP"),
        DISABLE_NRRC_INTRA_RAT_BLIND_REDIRECT_BY_A2_ENH("AT+ESBP=5,\"SBP_NRRC_INTRA_RAT_BLIND_REDIRECT_BY_A2_ENH\",0", "+ESBP"),
        ENABLE_NR_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH("AT+ESBP=5,\"SBP_NR_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH\",3", "+ESBP"),
        DISABLE_NR_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH("AT+ESBP=5,\"SBP_NR_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH\",0", "+ESBP"),
        ENABLE_LTE_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH("AT+ESBP=5,\"SBP_LTE_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH\",3", "+ESBP"),
        DISABLE_LTE_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH("AT+ESBP=5,\"SBP_LTE_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH\",0", "+ESBP"),
        ENABLE_HIGH_PRIORITY_CELL_NR_A2("AT+ESBP=5,\"SBP_HIGH_PRIORITY_CELL_NR_A2\",1", "+ESBP"),
        DISABLE_HIGH_PRIORITY_CELL_NR_A2("AT+ESBP=5,\"SBP_HIGH_PRIORITY_CELL_NR_A2\",0", "+ESBP"),
        ENABLE_HIGH_PRIORITY_CELL_NR_B2("AT+ESBP=5,\"SBP_HIGH_PRIORITY_CELL_NR_B2\",1", "+ESBP"),
        DISABLE_HIGH_PRIORITY_CELL_NR_B2("AT+ESBP=5,\"SBP_HIGH_PRIORITY_CELL_NR_B2\",0", "+ESBP"),
        ENABLE_HIGH_PRIORITY_CELL_NR_B1("AT+ESBP=5,\"SBP_HIGH_PRIORITY_CELL_NR_B1\",1", "+ESBP"),
        DISABLE_HIGH_PRIORITY_CELL_NR_B1("AT+ESBP=5,\"SBP_HIGH_PRIORITY_CELL_NR_B1\",0", "+ESBP"),
        ENABLE_ELEVATOR_MODE("AT+ESBP=5,\"SBP_ELEVATOR_MODE_SA_4G5\",1", "+ESBP"),
        DISABLE_ELEVATOR_MODE("AT+ESBP=5,\"SBP_ELEVATOR_MODE_SA_4G5\",0", "+ESBP"),
        ENABLE_SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE("AT+ESBP=6,\"SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE\",15", "+ESBP"),
        DISABLE_SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE("AT+ESBP=6,\"SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE\",36", "+ESBP"),
        ENABLE_NR_SKIP_RPT("AT+ESBP=5,\"SBP_ERRC_THRESH_BASED_SKIP_NR_CELL_REPORT\",1", "+ESBP"),
        DISABLE_NR_SKIP_RPT("AT+ESBP=5,\"SBP_ERRC_THRESH_BASED_SKIP_NR_CELL_REPORT\",0", "+ESBP"),
        RESTORE_DISCARD_TIMER("AT+EGCMD=398,4,\"06000000\"", "+EGCMD"),
        SET_DISCARD_TIMER1("AT+EGCMD=398,4,\"00000BB8\"", "+EGCMD"),
        SET_DISCARD_TIMER2("AT+EGCMD=398,4,\"00010BB8\"", "+EGCMD"),
        SET_SA_STATUS("AT+EGMC=1,\"sa_silence\",2", "+EGMC"),
        ENABLE_SA("AT+EGMC=1,\"sa_silence\",0", "+EGMC"),
        RESTORE_A2_TIMER("AT+ESBP=5,\"SBP_NR_POSTPONE_A2_FOR_B1B2\",0", "+ESBP"),
        SET_A2_TIMER("AT+ESBP=5,\"SBP_NR_POSTPONE_A2_FOR_B1B2\",1", "+ESBP"),
        SET_A2_TIMER_EGMC("AT+EGMC=1,\"nr_postpone_a2_for_b1b2\",800,-490,-62,-2", "+EGMC"),
        SET_NR_ERRC_THR("AT+EGMC=1,\"errc_nr_threshold\",1, -456,-55,0,-424,-48,0", "+EGMC"),
        SET_NR_ERRC_THR_FOR_ACTION_A3("AT+EGMC=1,\"errc_nr_threshold\",1, -400,-55,0,-400,-48,0", "+EGMC"),
        SET_NR_ERRC_THR_FOR_GAME("AT+EGMC=1,\"errc_nr_threshold\",1, -432,-55,0,-424,-48,0", "+EGMC"),
        ENABLE_AFR_TO_SA("AT+ESBP=5,\"SBP_AFR_TO_5G_FOR_SA_SILENCE_OFF\",1", "+ESBP"),
        DISABLE_AFR_TO_SA("AT+ESBP=5,\"SBP_AFR_TO_5G_FOR_SA_SILENCE_OFF\",0", "+ESBP"),
        GET_MD_DATA("AT+EDMFAPP=20004,30", "+EDMFAPP"),
        NOTIFY_MD_COLLECT_DATA("AT+EDMFAPP=20004,31", "+EDMFAPP");

        String atCmd0;
        String atCmd1;

        MtkAtCmd(String str, String str2) {
            this.atCmd0 = str;
            this.atCmd1 = str2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String[] cmd() {
            String str = this.atCmd1;
            String str2 = this.atCmd0;
            if (str != null) {
                return new String[]{str2, str};
            }
            return new String[]{str2};
        }
    }

    enum LowPowerState {
        LOW_POWER_STATE_STOP(0),
        LOW_POWER_STATE_START(1),
        LOW_POWER_STATE_SUSPEND(2),
        LOW_POWER_STATE_ERROR(3);

        private final int value;

        LowPowerState(int i) {
            this.value = i;
        }
    }

    private MtkSelfStudyController() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mServiceState = new ServiceState[i];
        this.mIsAirPlane = false;
        this.mIsDuringCall = false;
        this.mIsEnableLargeBW = false;
        this.mIsEnableLLM = false;
        this.mPingPongRecords = new SparseArray();
        this.mRecovery = null;
        this.mInSleepMode = false;
        this.lowerPowerOpt = new int[i];
        this.mLowPowerSnapshot = new HashMap[i];
        this.mLastTargetFreq = new int[i];
        this.mRealTimeOptimizationIsInit = false;
        this.mBaseModelIsInit = false;
        this.mChinaCMCC = new String[]{"46000", "46002", "46004", "46007", "46008"};
        this.mLteCellStats = new ConcurrentHashMap();
        this.mNrCellStats = new ConcurrentHashMap();
        this.mCurrentCellStats = new ConcurrentHashMap();
        this.mMtkSbpHistoryStats = new ConcurrentHashMap();
        this.mYesterdayStats = new ArrayList();
        this.mWechatOptStats = new ConcurrentHashMap();
        this.VDBG = false;
        this.isCellChanged = false;
        this.enterWechatVideoDays = 0;
        this.studyBadCellDays = 7;
        this.stageA3 = -1;
        this.stageA4 = -2;
        this.stageA5 = -3;
        this.oldFreq = 0;
        this.oldPci = 0;
        this.oldRat = 0L;
        this.mlastCellTime = 0L;
        this.mActionForStream = new int[]{-1, -1};
        this.oldStreamFreq = 0;
        this.oldStreamPci = 0;
        this.oldStreamRat = 0L;
        this.mLastStreamMediaCellTime = 0L;
        this.isResidentCell = false;
        this.oldStreamOffSetCell = null;
        this.mCurrentStreamMediaCellStats = new ConcurrentHashMap();
        this.mDataEnabledObserver = new ContentObserver(this.mWorkHandler) { // from class: com.android.phone.MtkSelfStudyController.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                if (MtkSelfStudyController.this.isMobileDataSettingOn() || !((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.hasMessages(1022)) {
                    return;
                }
                MtkSelfStudyController.this.enableNr(true);
                ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.removeMessages(1022);
            }
        };
        this.mModemAiOffsetList = new ArrayList();
        this.maxSavedGameCells = 6;
        this.mstage1 = 1;
        this.mstage2 = 0;
        this.mstage3 = -1;
        this.oldGameFreq = 0;
        this.oldGamePci = 0;
        this.oldGameRat = 0L;
        this.oldGameRsrp = 0;
        this.oldGameSnr = 0;
        this.curGameRat = 0L;
        this.startGameLagTime = 0L;
        this.startGameLagFromModem = 0L;
        this.startGameTime = 0L;
        this.endGameTime = 0L;
        this.oldGameGci = "";
        this.curGameGpfr = "";
        this.mlastGameCellTime = 0L;
        this.minsertDbTime = 0L;
        this.mEnterGameBattleCount = 0;
        this.mEnterGameBoosterCount = 0;
        this.mEnterGameAiBoosterCount = 0;
        this.curGameScene = 2;
        this.enterGameResidentDays = 0;
        this.studyGameResidentCellDays = 0;
        this.mEnterGameBattleState = false;
        this.isSetBWEable = false;
        this.mGameLagGpfrModem = "";
        this.mLastTopGamePackage = "";
        this.oldGameRsrq = 0;
        this.mSetGameOptNr2Lte = false;
        this.isEnterGame = false;
        this.isSetNrTop = false;
        this.isSetGameTopCell = false;
        this.isGameLag = false;
        this.isGameLagFromModem = false;
        this.isSetGameAtCmd = new boolean[2];
        this.isSetOffsetFromModem = false;
        this.isBattle = false;
        this.mTopGamePackage = "";
        this.startGameLagFromGameAi = 0L;
        this.startGameLagFromTraModel = 0L;
        this.mlastGameLevelTime = 0L;
        this.isGameAiLag = false;
        this.isGameTraModelLag = false;
        this.isLastAiAndTraModelLag = false;
        this.isNr2Lte = false;
        this.isStaticCell = false;
        this.isFirst20min = true;
        this.mLteGameCellStats = new ConcurrentHashMap();
        this.mNrGameCellStats = new ConcurrentHashMap();
        this.mTop2Cell = new ConcurrentHashMap();
        this.mGameOffsetCell = new ConcurrentHashMap();
        this.mDoNextCellFromMd = new ConcurrentHashMap();
        this.mTopCellForStaticGame = new ArrayList();
        this.mCurrentGameCellStats = new ConcurrentHashMap();
        this.networkMonitorListener = new MiuiNetworkMonitor.IMiuiNetworkMonitorListener() { // from class: com.android.phone.MtkSelfStudyController.6
            @Override // com.android.phone.network.MiuiNetworkMonitor.IMiuiNetworkMonitorListener
            public void onTcpRTTWorse(Network network, int i2, int i3) {
                MtkSelfStudyController.this.log("onTcpRTTWorse network: " + network + " , level: " + i2 + " , version: " + i3);
                MtkSelfStudyController mtkSelfStudyController = MtkSelfStudyController.this;
                mtkSelfStudyController.mIsStreamStall = true;
                mtkSelfStudyController.beginStreamDataStall(mtkSelfStudyController.dataType, 0);
                boolean zIsModemEnteredStreamScene = MtkSelfStudyController.this.isModemEnteredStreamScene();
                MtkSelfStudyController mtkSelfStudyController2 = MtkSelfStudyController.this;
                if (!zIsModemEnteredStreamScene) {
                    mtkSelfStudyController2.log("modem does not enter the streaming scene for SNA stall");
                    return;
                }
                mtkSelfStudyController2.updateRecordStateForHome();
                if (!SelfStudyControllerBase.isBeginCollectCellData) {
                    MtkSelfStudyController mtkSelfStudyController3 = MtkSelfStudyController.this;
                    if (mtkSelfStudyController3.mIsResidentCell || mtkSelfStudyController3.mIsSubway) {
                        MtkSelfStudyController mtkSelfStudyController4 = MtkSelfStudyController.this;
                        mtkSelfStudyController4.beginToCollectCellData(((SelfStudyControllerBase) mtkSelfStudyController4).mIsWorkDay, 8);
                    }
                }
                MtkSelfStudyController.this.getOffSetFormModem(8, -1001);
                MtkSelfStudyController.this.processDataStallAppStatistic(true, 8);
                if (((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.hasMessages(1053)) {
                    return;
                }
                MtkSelfStudyController.this.logV("send long stall msg for SNA.");
                ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.sendMessageDelayed(((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.obtainMessage(1053), 10000L);
            }

            @Override // com.android.phone.network.MiuiNetworkMonitor.IMiuiNetworkMonitorListener
            public void onTcpRTTBetter(Network network, int i2) {
                MtkSelfStudyController.this.log("onTcpRTTBetter network: " + network + " , version: " + i2);
                MtkSelfStudyController mtkSelfStudyController = MtkSelfStudyController.this;
                mtkSelfStudyController.mIsStreamStall = false;
                mtkSelfStudyController.processDataStallAppStatistic(false, 8);
                if (((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.hasMessages(1041)) {
                    ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.removeMessages(1041);
                    MtkSelfStudyController mtkSelfStudyController2 = MtkSelfStudyController.this;
                    mtkSelfStudyController2.stopToCollectCellData(((SelfStudyControllerBase) mtkSelfStudyController2).mIsWorkDay, 8);
                }
                if (SelfStudyControllerBase.isBeginCollectCellData) {
                    MtkSelfStudyController mtkSelfStudyController3 = MtkSelfStudyController.this;
                    mtkSelfStudyController3.stopToCollectCellData(((SelfStudyControllerBase) mtkSelfStudyController3).mIsWorkDay, 8);
                }
                if (((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.hasMessages(1053)) {
                    MtkSelfStudyController.this.logV("remove long stall event lasted for SNA.");
                    ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.removeMessages(1053);
                }
            }
        };
        if (Build.IS_INTERNATIONAL_BUILD) {
            boolean z = HyperOSCustFeatureResolve.getBoolean("config_modem_base_model", false);
            log("mIsSupportModemBaseModel:" + z);
            if (z) {
                this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$new$0();
                    }
                }, MirrorDesktopHelper.TIMEOUT_MILLIS);
                if (this.mGlobalMtkSelfStudyBroadCastReceiver == null) {
                    log("register global sim BroadcastReceiver");
                    IntentFilter intentFilter = new IntentFilter("android.telephony.action.SIM_CARD_STATE_CHANGED");
                    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.MtkSelfStudyController.1
                        @Override // android.content.BroadcastReceiver
                        public void onReceive(Context context, Intent intent) {
                            if ("android.telephony.action.SIM_CARD_STATE_CHANGED".equals(intent.getAction())) {
                                MtkSelfStudyController.this.initBaseModel(intent.getIntExtra("android.telephony.extra.SIM_STATE", 1));
                            }
                        }
                    };
                    this.mGlobalMtkSelfStudyBroadCastReceiver = broadcastReceiver;
                    this.mContext.registerReceiver(broadcastReceiver, intentFilter);
                }
            }
            this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$1();
                }
            });
            return;
        }
        super.initCommon();
        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            MiuiNetworkMonitor.getInstance().registerMiuiNetworkMonitorListener(this.networkMonitorListener);
            int sceneType = getSceneType();
            if (2 == sceneType || 4 == sceneType) {
                Handler handler = this.mWorkHandler;
                handler.sendMessageDelayed(handler.obtainMessage(1051), 10000L);
            }
            this.mDdsPhoneId = DefaultSimManager.getDefaultDataSlotId();
        }
        this.mGameBoosterGameDelayListener = new GameBoosterGameDelayListener();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        this.isSupportCellAction = true;
        if (MiuiPhoneUtils.isSupportWechatVideoOptimization() && this.cloudWechatOn) {
            this.mWechatOpt = new WechatDdsSaOpt();
        }
        if ((MiuiPhoneUtils.isSupportMtkDataOptimization() || MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) && isCloudGameResidentOn()) {
            this.mGameCellOpt = new GameCellDdsSaOpt();
            resetResidentGameCellOptimization(true);
        }
        this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$2();
            }
        });
        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            registerActivityChangeListener(0);
        }
        makeNetworkSelfRecovery();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        sendSelfStudyRequest(400, 1, new int[]{0});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (MiuiPhoneUtils.isSupportSmartNetworkGameOptimization()) {
            this.mSmartNetworkGameController = SmartNetworkGameController.init(this);
        }
        sendAtCmd(constructAtCmd(20103, !this.mFeatureEnable ? 1 : 0), (Message) null, 0);
    }

    private void makeNetworkSelfRecovery() {
        if (!MiuiPhoneUtils.isSpecialNetorkOptimizationSupported() || SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
            return;
        }
        this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        this.mRecovery = NetworkSelfRecovery.make(this.mContext);
    }

    public static void make() {
        if (SelfStudyControllerBase.mInstance instanceof MtkSelfStudyController) {
            return;
        }
        SelfStudyControllerBase.mInstance = new MtkSelfStudyController();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: initForInternationalBuild, reason: merged with bridge method [inline-methods] */
    public void lambda$new$1() {
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        boolean zIsSupportShortVideoOptimizationForRegion = dataPhone == null ? false : isSupportShortVideoOptimizationForRegion(dataPhone.getOperatorNumeric());
        regUnregShortVideoOptimizationReceiver(zIsSupportShortVideoOptimizationForRegion);
        regUnregAppStateMonitor(zIsSupportShortVideoOptimizationForRegion);
        registerTelephony();
    }

    private void regUnregShortVideoOptimizationReceiver(boolean z) {
        if (z && this.mBroadcastReceiver == null) {
            SelfStudyControllerBase.SHORT_VIDEO_PACKAGES.add("com.ss.android.ugc.trill");
            this.mBroadcastReceiver = new SelfStudyControllerBase.SelfStudyReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme(f.a.e);
            this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
            return;
        }
        if (z || this.mBroadcastReceiver == null) {
            return;
        }
        SelfStudyControllerBase.SHORT_VIDEO_PACKAGES.clear();
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mBroadcastReceiver = null;
    }

    private boolean isSupportShortVideoOptimizationForRegion(String str) {
        String[] stringArray;
        if (!TextUtils.isEmpty(str) && (stringArray = this.mContext.getResources().getStringArray(R.array.country_support_short_video_optimization)) != null) {
            for (String str2 : stringArray) {
                if (str2.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void regUnregOtherReceiver(boolean z) {
        BroadcastReceiver broadcastReceiver;
        regUnRegisterAtUrcInd(z);
        sendSelfStudyRequest(26, 1, new byte[]{0});
        if (z && this.mMtkSelfStudyBroadCastReceiver == null) {
            this.mMtkSelfStudyBroadCastReceiver = new MtkSelfStudyReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("selfStudyController.wechat.intent.debugall");
            intentFilter.addAction("miui.intent.action.test.stream");
            intentFilter.addAction("com.miui.powerkeeper_sleep_changed");
            intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
            this.mContext.registerReceiver(this.mMtkSelfStudyBroadCastReceiver, intentFilter, 2);
        } else if (!z && (broadcastReceiver = this.mMtkSelfStudyBroadCastReceiver) != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
            this.mMtkSelfStudyBroadCastReceiver = null;
        }
        this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$regUnregOtherReceiver$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$regUnregOtherReceiver$3() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(getMobileDataSettingName()), false, this.mDataEnabledObserver);
    }

    class MtkSelfStudyReceiver extends BroadcastReceiver {
        MtkSelfStudyReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$MtkSelfStudyReceiver$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$3(intent);
                    }
                });
            } else {
                Log.d("MtkSelfStudyController", "Null action");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code duplicated, block: B:64:0x02a9 A[Catch: all -> 0x02a6, TRY_LEAVE, TryCatch #1 {all -> 0x02a6, blocks: (B:59:0x028a, B:61:0x0290, B:64:0x02a9), top: B:172:0x028a, outer: #3 }] */
        public /* synthetic */ void lambda$onReceive$3(final Intent intent) {
            String action = intent.getAction();
            Log.d("MtkSelfStudyController", "Receive action is  " + action);
            if ("selfStudyController.wechat.intent.debugall".equals(action)) {
                MtkSelfStudyController.this.VDBG = intent.getBooleanExtra("wechat_debug", false);
                MtkSelfStudyController.this.maxSavedCells = intent.getIntExtra("maxSavedCells", 32);
                MtkSelfStudyController.this.ulAllocRate = intent.getStringExtra("ulAllocRate") == null ? "0xFFFFFFFF" : intent.getStringExtra("ulAllocRate");
                MtkSelfStudyController.this.ulStallUlTput = intent.getStringExtra("ulStallUlTput") == null ? "9" : intent.getStringExtra("ulStallUlTput");
                MtkSelfStudyController.this.ulStallDlTput = intent.getStringExtra("ulStallDlTput") != null ? intent.getStringExtra("ulStallDlTput") : "0xFFFFFFFF";
                MtkSelfStudyController.this.ulStallUlPendData = intent.getStringExtra("ulStallUlPendData") == null ? "15000" : intent.getStringExtra("ulStallUlPendData");
                MtkSelfStudyController.this.log("ulAllocRate = " + MtkSelfStudyController.this.ulAllocRate + " ulStallUlTput = " + MtkSelfStudyController.this.ulStallUlTput + " ulStallDlTput = " + MtkSelfStudyController.this.ulStallDlTput + " ulStallUlPendData = " + MtkSelfStudyController.this.ulStallUlPendData + " maxSavedCells = " + MtkSelfStudyController.this.maxSavedCells);
                return;
            }
            if ("miui.intent.action.test.stream".equals(action)) {
                String stringExtra = intent.getStringExtra("operation");
                MtkSelfStudyController.this.log("operation = " + stringExtra);
                if ("insertResidentData".equalsIgnoreCase(stringExtra)) {
                    String stringExtra2 = intent.getStringExtra("cellData");
                    MtkSelfStudyController.this.log("insertResidentData data = " + stringExtra2);
                    MtkSelfStudyController.this.insertStreamDataForTest(stringExtra2, true);
                    return;
                }
                if ("insertDynamicData".equalsIgnoreCase(stringExtra)) {
                    String stringExtra3 = intent.getStringExtra("cellData");
                    MtkSelfStudyController.this.log("insertDynamicData data = " + stringExtra3);
                    MtkSelfStudyController.this.insertStreamDataForTest(stringExtra3, false);
                    return;
                }
                if ("startCollect".equalsIgnoreCase(stringExtra)) {
                    MtkSelfStudyController mtkSelfStudyController = MtkSelfStudyController.this;
                    mtkSelfStudyController.collectCellDataForTest(((SelfStudyControllerBase) mtkSelfStudyController).mIsWorkDay, true);
                    return;
                }
                if ("stopCollect".equalsIgnoreCase(stringExtra)) {
                    MtkSelfStudyController mtkSelfStudyController2 = MtkSelfStudyController.this;
                    mtkSelfStudyController2.collectCellDataForTest(((SelfStudyControllerBase) mtkSelfStudyController2).mIsWorkDay, false);
                    return;
                }
                if ("modemUrc".equalsIgnoreCase(stringExtra)) {
                    String stringExtra4 = intent.getStringExtra("urcData");
                    MtkSelfStudyController.this.log("modemUrc data = " + stringExtra4);
                    Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
                    if (activeDataPhone == null) {
                        MtkSelfStudyController.this.log("modemUrc dataPhone is null.");
                        return;
                    } else {
                        ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.sendMessage(((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.obtainMessage(1000, new AsyncResult(activeDataPhone, stringExtra4, (Throwable) null)));
                        return;
                    }
                }
                if ("getAction".equalsIgnoreCase(stringExtra)) {
                    int intExtra = intent.getIntExtra("dataType", 100);
                    String stringExtra5 = intent.getStringExtra("gci");
                    MtkSelfStudyController.this.log("getAction gci = " + stringExtra5 + " , type = " + intExtra);
                    SelfStudyDatabaseHelper selfStudyDatabaseHelper = MtkSelfStudyController.this.dbHelper;
                    if (selfStudyDatabaseHelper != null) {
                        selfStudyDatabaseHelper.getActionFromStreamMediaByGciAndType(stringExtra5, intExtra);
                        return;
                    }
                    return;
                }
                if ("getRsrp".equalsIgnoreCase(stringExtra)) {
                    String stringExtra6 = intent.getStringExtra("gci");
                    int intExtra2 = intent.getIntExtra("dataType", 100);
                    int intExtra3 = intent.getIntExtra("isWorkDay", 100);
                    int intExtra4 = intent.getIntExtra("isPeak", 100);
                    boolean booleanExtra = intent.getBooleanExtra("isResident", false);
                    MtkSelfStudyController.this.log("getRsrp gci = " + stringExtra6 + " , type = " + intExtra2 + " , isWorkDay = " + intExtra3 + " , isPeak = " + intExtra4 + " , isResident = " + booleanExtra);
                    SelfStudyDatabaseHelper selfStudyDatabaseHelper2 = MtkSelfStudyController.this.dbHelper;
                    if (selfStudyDatabaseHelper2 != null) {
                        selfStudyDatabaseHelper2.getRsrpFormStreamDataTableByGciAndType(stringExtra6, intExtra2, intExtra3, intExtra4, booleanExtra);
                        return;
                    }
                    return;
                }
                if ("getStreamMedia".equalsIgnoreCase(stringExtra)) {
                    String stringExtra7 = intent.getStringExtra("gci");
                    MtkSelfStudyController.this.log("getStreamMedia gci = " + stringExtra7);
                    SelfStudyDatabaseHelper selfStudyDatabaseHelper3 = MtkSelfStudyController.this.dbHelper;
                    if (selfStudyDatabaseHelper3 != null) {
                        try {
                            Cursor streamMediaByGci = selfStudyDatabaseHelper3.getStreamMediaByGci(stringExtra7);
                            if (streamMediaByGci == null) {
                                MtkSelfStudyController.this.log("getStreamMedia for gci is not match.");
                            } else {
                                try {
                                    if (!streamMediaByGci.moveToFirst()) {
                                        MtkSelfStudyController.this.log("getStreamMedia for gci is not match.");
                                    } else {
                                        MtkSelfStudyController.this.log("getStreamMedia id = " + streamMediaByGci.getInt(0));
                                    }
                                } catch (Throwable th) {
                                    if (streamMediaByGci == null) {
                                        throw th;
                                    }
                                    try {
                                        streamMediaByGci.close();
                                        throw th;
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                        throw th;
                                    }
                                }
                            }
                            if (streamMediaByGci != null) {
                                streamMediaByGci.close();
                                return;
                            }
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    return;
                }
                if ("setOffsetCell".equalsIgnoreCase(stringExtra)) {
                    int intExtra5 = intent.getIntExtra("freq", 100);
                    int intExtra6 = intent.getIntExtra("pci", 100);
                    String stringExtra8 = intent.getStringExtra("plmn");
                    MtkSelfStudyController.this.log("setOffsetCell freq = " + intExtra5 + " , pci = " + intExtra6 + " , plmn = " + stringExtra8);
                    Phone activeDataPhone2 = MiuiPhoneUtils.getActiveDataPhone();
                    if (activeDataPhone2 == null || MtkSelfStudyController.this.getCurrentCellInfo(activeDataPhone2) == null) {
                        return;
                    }
                    MtkSelfStudyController.this.insertOrUpdateOffsetCellDatabase(new OffsetCell(intExtra5, intExtra6, 0, stringExtra8, MtkSelfStudyController.this.getCurrentCellInfo(activeDataPhone2).rat, MtkSelfStudyController.this.getCurrentCellInfo(activeDataPhone2).globalCellId), activeDataPhone2.getPhoneId());
                    return;
                }
                if ("isLteLagMoreThanNr".equalsIgnoreCase(stringExtra)) {
                    MtkSelfStudyController.this.isLteLagMoreThanNr();
                    return;
                }
                if ("getGameCellResidenceAndLagTime".equalsIgnoreCase(stringExtra)) {
                    MtkSelfStudyController.this.getGameCellResidenceAndLagTime();
                    return;
                }
                if ("getGameAiLagTime".equalsIgnoreCase(stringExtra)) {
                    ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.sendMessageDelayed(((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.obtainMessage(43), 20000L);
                    ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.sendMessageDelayed(((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.obtainMessage(41), 20000L);
                    MtkSelfStudyController.this.getGameAiLagTime("HIGH", "TRADITIONAL");
                    return;
                }
                if ("isSetGameTopCell".equalsIgnoreCase(stringExtra)) {
                    String stringExtra9 = intent.getStringExtra("type");
                    if ("set".equalsIgnoreCase(stringExtra9)) {
                        MtkSelfStudyController.this.isSetGameTopCell = true;
                        return;
                    }
                    if ("degrade".equalsIgnoreCase(stringExtra9)) {
                        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                        MtkSelfStudyController.this.log("handleGameStall, request neighbor cell info before optimization  ddsId:" + defaultDataSlotId);
                        MtkSelfStudyController.this.triggerGameResidentNetworkOptimization(defaultDataSlotId);
                        return;
                    }
                    if ("offset".equalsIgnoreCase(stringExtra9)) {
                        MtkSelfStudyController.this.doCellActionForGame(MtkSelfStudyController.this.oldGameGci + "-" + MtkSelfStudyController.this.oldGamePci + "-" + MtkSelfStudyController.this.oldGameFreq, MtkSelfStudyController.this.oldGameRat, -5, false);
                        MtkSelfStudyController.this.mGameOffsetCell.put(MtkSelfStudyController.this.oldGameGci + "-" + MtkSelfStudyController.this.oldGamePci + "-" + MtkSelfStudyController.this.oldGameFreq + "-" + MtkSelfStudyController.this.oldGameRat, -5);
                        return;
                    }
                    return;
                }
                if ("isStaticCell".equalsIgnoreCase(stringExtra)) {
                    String stringExtra10 = intent.getStringExtra(a.p);
                    String stringExtra11 = intent.getStringExtra("rat");
                    boolean zEqualsIgnoreCase = "true".equalsIgnoreCase(stringExtra10);
                    MtkSelfStudyController.this.isStaticCell = zEqualsIgnoreCase;
                    MtkSelfStudyController.this.log("isStaticCell value = " + stringExtra10 + ", rat = " + stringExtra11);
                    MtkSelfStudyController mtkSelfStudyController3 = MtkSelfStudyController.this;
                    if (zEqualsIgnoreCase) {
                        ((SelfStudyControllerBase) mtkSelfStudyController3).isGameDebug = true;
                        MtkSelfStudyController.this.mTopCellForStaticGame.add(MtkSelfStudyController.this.oldGameGci + "-" + MtkSelfStudyController.this.oldGamePci + "-" + MtkSelfStudyController.this.oldGameFreq);
                        MtkSelfStudyController mtkSelfStudyController4 = MtkSelfStudyController.this;
                        StringBuilder sb = new StringBuilder("mTopCellForStaticGame: ");
                        sb.append(MtkSelfStudyController.this.mTopCellForStaticGame);
                        mtkSelfStudyController4.log(sb.toString());
                        if (stringExtra11 != null && !stringExtra11.isEmpty()) {
                            try {
                                MtkSelfStudyController.this.setGoodLink(Long.parseLong(stringExtra11));
                            } catch (NumberFormatException unused) {
                                MtkSelfStudyController.this.log("rat parseLong error, use oldGameRat, rat=".concat(stringExtra11));
                                MtkSelfStudyController mtkSelfStudyController5 = MtkSelfStudyController.this;
                                mtkSelfStudyController5.setGoodLink(mtkSelfStudyController5.oldGameRat);
                            }
                        } else {
                            MtkSelfStudyController mtkSelfStudyController6 = MtkSelfStudyController.this;
                            mtkSelfStudyController6.setGoodLink(mtkSelfStudyController6.oldGameRat);
                        }
                        MtkSelfStudyController.this.checkStaticState(true);
                        MtkSelfStudyController.this.requestNeighborCellInfoCommon();
                        return;
                    }
                    mtkSelfStudyController3.mTopCellForStaticGame.clear();
                    return;
                }
                return;
            }
            if ("android.telephony.action.SIM_CARD_STATE_CHANGED".equals(action)) {
                ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.sendMessage(((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.obtainMessage(3002, intent));
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                boolean z = Settings.Global.getInt(((SelfStudyControllerBase) MtkSelfStudyController.this).mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
                if (z && MtkSelfStudyController.this.mWechatOpt != null) {
                    MtkSelfStudyController.this.mWechatOpt.tryRecovery();
                }
                if (MiuiPhoneUtils.isSupportMtkDataOptimization() || MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) {
                    AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                    if (!MtkSelfStudyController.this.mIsAirPlane && z) {
                        MtkSelfStudyController.this.mIsAirPlane = true;
                        atomicBoolean.set(true);
                        MtkSelfStudyController mtkSelfStudyController7 = MtkSelfStudyController.this;
                        mtkSelfStudyController7.processAirPlaneForGameOpt(mtkSelfStudyController7.mIsAirPlane);
                    } else if (MtkSelfStudyController.this.mIsAirPlane && !z) {
                        MtkSelfStudyController.this.mIsAirPlane = false;
                        atomicBoolean.set(true);
                    }
                    if (atomicBoolean.get()) {
                        MtkSelfStudyController mtkSelfStudyController8 = MtkSelfStudyController.this;
                        mtkSelfStudyController8.processAirPlaneStateChange(mtkSelfStudyController8.mIsAirPlane);
                    }
                }
                if (z && ((SelfStudyControllerBase) MtkSelfStudyController.this).mIsSupportLowPowerModeOptimization) {
                    MtkSelfStudyController mtkSelfStudyController9 = MtkSelfStudyController.this;
                    if (mtkSelfStudyController9.cloudLowPowerOn) {
                        ((SelfStudyControllerBase) mtkSelfStudyController9).mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$MtkSelfStudyReceiver$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onReceive$0();
                            }
                        });
                        return;
                    }
                    return;
                }
                return;
            }
            if ("com.xiaomi.modemai.game.FORECAST_RES".equals(action)) {
                ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$MtkSelfStudyReceiver$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$1(intent);
                    }
                });
                return;
            }
            if ("com.miui.powerkeeper_sleep_changed".equals(action) && ((SelfStudyControllerBase) MtkSelfStudyController.this).mIsSupportLowPowerModeOptimization && MtkSelfStudyController.this.cloudLowPowerOn) {
                final boolean z2 = 1 == intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, 2);
                MtkSelfStudyController.this.log("receive ACTION_SLEEP_CHANGED, isSleep = " + z2);
                ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$MtkSelfStudyReceiver$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() throws Throwable {
                        this.f$0.lambda$onReceive$2(z2);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            MtkSelfStudyController.this.resetLowPowerOpt(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$1(Intent intent) {
            MtkSelfStudyController.this.log("receive ACTION_MODEM_AI_GAME_RESULT");
            String stringExtra = intent.getStringExtra("FORECAST_RES");
            String stringExtra2 = intent.getStringExtra("FORECAST_MODEL");
            if (MtkSelfStudyController.this.shouldDoActionForGameOpt() && SelfStudyControllerBase.mEnterGameAiOptimizer) {
                MtkSelfStudyController.this.getGameAiLagTime(stringExtra, stringExtra2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$2(boolean z) throws Throwable {
            MtkSelfStudyController.this.handleSleepChanged(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSleepChanged(boolean z) throws Throwable {
        int i = 0;
        if (z) {
            while (i < MiuiPhoneUtils.PHONE_COUNT) {
                try {
                    if (takeLowPowerOption(i)) {
                        int[] iArr = this.lowerPowerOpt;
                        LowPowerState lowPowerState = LowPowerState.LOW_POWER_STATE_START;
                        iArr[i] = lowPowerState.value;
                        sendStateToModem(lowPowerState.value, i);
                        snapshotLowPowerInfo(i, getLastTargetFreq(i), MiuiPhoneUtils.isMobileDataEnabled(this.mContext));
                    }
                } catch (Exception e) {
                    Log.e("MtkSelfStudyController", "takeLowPowerOption failed for phoneId: " + i, e);
                }
                i++;
            }
        } else {
            while (i < MiuiPhoneUtils.PHONE_COUNT) {
                int i2 = this.lowerPowerOpt[i];
                LowPowerState lowPowerState2 = LowPowerState.LOW_POWER_STATE_STOP;
                if (i2 != lowPowerState2.value) {
                    if (resetLowPowerOption(i)) {
                        sendStateToModem(lowPowerState2.value, i);
                        this.lowerPowerOpt[i] = lowPowerState2.value;
                    } else {
                        Log.w("MtkSelfStudyController", "resetLowPowerOption failed for phoneId: " + i);
                    }
                    reportLowPowerStatistic(i);
                }
                i++;
            }
        }
        this.mInSleepMode = z;
    }

    private boolean takeLowPowerOption(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            log("takeLowPowerOption phone is null");
            return false;
        }
        int subId = phone.getSubId();
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            log("takeLowPowerOption invalid subId: " + subId);
            return false;
        }
        SQLiteDatabase readableDatabase = this.dbHelper.getReadableDatabase();
        if (readableDatabase == null || !readableDatabase.isOpen()) {
            log("takeLowPowerOption db is null or closed");
            return false;
        }
        String cellIdByPhoneId = getCellIdByPhoneId(i);
        if (cellIdByPhoneId == null) {
            log("takeLowPowerOption currentCellId is null, skip");
            return false;
        }
        if (!this.dbHelper.isCurrentCellInResidentTable(readableDatabase, cellIdByPhoneId)) {
            log("takeLowPowerOption not in resident table, skip");
            return false;
        }
        boolean zIsMobileDataEnabled = MiuiPhoneUtils.isMobileDataEnabled(this.mContext);
        List lowerPowerCellInfoBySubId = this.dbHelper.getLowerPowerCellInfoBySubId(readableDatabase, subId, zIsMobileDataEnabled);
        log("takeLowPowerOption lowerCellInfo size: " + lowerPowerCellInfoBySubId.size() + ", dataOn: " + zIsMobileDataEnabled);
        if (lowerPowerCellInfoBySubId.size() < 2) {
            log("takeLowPowerOption cell info not enough, need at least 2");
            return false;
        }
        try {
            HashMap map = (HashMap) lowerPowerCellInfoBySubId.get(0);
            if (!isCellDataFresh(map)) {
                log("takeLowPowerOption data expired or invalid");
                return false;
            }
            int iIntValue = ((Integer) map.get("freq")).intValue();
            boolean zSendAtCmdSync = FiveGManagerBase.getInstance().sendAtCmdSync(i, new String[]{"MIPC_SET_ARFCN_CONFIG", "5", "1", iIntValue + ",32"}, null);
            log("takeLowPowerOption sendAtCmd result: " + zSendAtCmdSync + ", freq: " + iIntValue);
            if (zSendAtCmdSync) {
                this.mLastTargetFreq[i] = iIntValue;
            }
            return zSendAtCmdSync;
        } catch (Exception e) {
            Log.e("MtkSelfStudyController", "takeLowPowerOption exception", e);
            return false;
        }
    }

    private boolean isCellDataFresh(HashMap map) {
        String str = (String) map.get("update_time_mills");
        if (str != null && !str.isEmpty()) {
            try {
                return (System.currentTimeMillis() - new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(str.trim()).getTime()) / v.b < 1;
            } catch (Exception e) {
                Log.e("MtkSelfStudyController", "isCellDataFresh parse error", e);
            }
        }
        return false;
    }

    private void sendStateToModem(int i, int i2) {
        sendAtCmd(new String[]{"AT+EDMFAPP=20107,112," + i + ",0,0,0,0", "+EDMFAPP"}, (Message) null, i2);
    }

    private int getLastTargetFreq(int i) {
        return this.mLastTargetFreq[i];
    }

    private float calcWeightedScore(List list, boolean z) {
        String str = z ? "sleep_dataon_score" : "sleep_dataoff_score";
        Iterator it = list.iterator();
        int i = 0;
        float fFloatValue = 0.0f;
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            int iIntValue = ((Integer) map.get("store_time")).intValue();
            fFloatValue += ((Number) map.get(str)).floatValue() * iIntValue;
            i += iIntValue;
        }
        if (i > 0) {
            return fFloatValue / i;
        }
        return 0.0f;
    }

    private void snapshotLowPowerInfo(int i, int i2, boolean z) throws Throwable {
        MtkSelfStudyController mtkSelfStudyController;
        int i3;
        int i4;
        MtkSelfStudyController mtkSelfStudyController2 = this;
        String str = "band_score";
        Object obj = "ulduty_score";
        if (mtkSelfStudyController2.mLowPowerSnapshot[i] != null) {
            mtkSelfStudyController2.log("snapshotLowPowerInfo already exists for phoneId=" + i + ", skip");
            return;
        }
        try {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone == null) {
                return;
            }
            int subId = phone.getSubId();
            try {
                SQLiteDatabase readableDatabase = mtkSelfStudyController2.dbHelper.getReadableDatabase();
                if (readableDatabase != null && readableDatabase.isOpen()) {
                    Object obj2 = "cdrx_score";
                    List allLowerPowerCellDetailsBySubId = mtkSelfStudyController2.dbHelper.getAllLowerPowerCellDetailsBySubId(readableDatabase, subId);
                    if (allLowerPowerCellDetailsBySubId.isEmpty()) {
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    Iterator it = allLowerPowerCellDetailsBySubId.iterator();
                    while (true) {
                        i4 = subId;
                        if (!it.hasNext()) {
                            break;
                        }
                        try {
                            HashMap map = (HashMap) it.next();
                            List list = allLowerPowerCellDetailsBySubId;
                            HashMap map2 = new HashMap();
                            ArrayList arrayList2 = arrayList;
                            map2.put("rsrp_avg", map.get("rsrp_avg"));
                            map2.put("sinr_avg", map.get("sinr_avg"));
                            map2.put("paging_cycle", map.get("paging_cycle"));
                            map2.put("band", map.get("band"));
                            map2.put("avg_pusch_tx_power", map.get("avg_pusch_tx_power"));
                            map2.put("rrc_connect_time", map.get("rrc_connect_time"));
                            map2.put("cdrx_inactive_time", map.get("cdrx_inactive_time"));
                            map2.put("sub_carrier_spacing", map.get("sub_carrier_spacing"));
                            map2.put("pusch_tx_slot_cnt", map.get("pusch_tx_slot_cnt"));
                            map2.put("store_time", map.get("store_time"));
                            map2.put("paging_score", map.get("paging_score"));
                            map2.put(str, map.get(str));
                            Object obj3 = obj2;
                            String str2 = str;
                            map2.put(obj3, map.get(obj3));
                            Object obj4 = obj;
                            map2.put(obj4, map.get(obj4));
                            map2.put("update_time_mills", map.get("update_time_mills"));
                            arrayList2.add(map2);
                            obj = obj4;
                            str = str2;
                            obj2 = obj3;
                            arrayList = arrayList2;
                            allLowerPowerCellDetailsBySubId = list;
                            subId = i4;
                            mtkSelfStudyController2 = this;
                        } catch (Exception e) {
                            e = e;
                            mtkSelfStudyController = this;
                        }
                    }
                    List list2 = allLowerPowerCellDetailsBySubId;
                    mtkSelfStudyController = mtkSelfStudyController2;
                    ArrayList arrayList3 = arrayList;
                    try {
                        float fCalcWeightedScore = mtkSelfStudyController.calcWeightedScore(list2, z);
                        String operatorNumeric = phone.getOperatorNumeric();
                        HashMap map3 = new HashMap();
                        if (operatorNumeric == null) {
                            operatorNumeric = "";
                        }
                        map3.put("plmn", operatorNumeric);
                        map3.put("data_on", Boolean.valueOf(z));
                        map3.put("db_cell_count", Integer.valueOf(list2.size()));
                        map3.put("opt_target_freq", Integer.valueOf(i2));
                        map3.put("weighted_score_before", Float.valueOf(fCalcWeightedScore));
                        map3.put("cell_info_list", arrayList3);
                        map3.put("snapshot_update_time", ((HashMap) list2.get(0)).get("update_time_mills"));
                        map3.put("subId", Integer.valueOf(i4));
                        map3.put("sleep_start_time", Long.valueOf(System.currentTimeMillis()));
                        mtkSelfStudyController.mLowPowerSnapshot[i] = map3;
                        StringBuilder sb = new StringBuilder();
                        sb.append("snapshotLowPowerInfo phoneId=");
                        i3 = i;
                        try {
                            sb.append(i3);
                            sb.append(" freq=");
                            sb.append(i2);
                            sb.append(" cells=");
                            sb.append(list2.size());
                            sb.append(" scoreBefore=");
                            sb.append(fCalcWeightedScore);
                            mtkSelfStudyController.log(sb.toString());
                            return;
                        } catch (Exception e2) {
                            e = e2;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        i3 = i;
                    }
                    i3 = i;
                }
                return;
            } catch (Exception e4) {
                e = e4;
                mtkSelfStudyController = mtkSelfStudyController2;
            }
        } catch (Exception e5) {
            e = e5;
            mtkSelfStudyController = mtkSelfStudyController2;
            i3 = i;
        }
        Log.e("MtkSelfStudyController", "snapshotLowPowerInfo error", e);
        mtkSelfStudyController.mLowPowerSnapshot[i3] = null;
    }

    /* JADX WARN: Code duplicated, block: B:32:0x00c8  */
    private void reportLowPowerStatistic(int i) throws Throwable {
        int earfcn;
        HashMap[] mapArr = this.mLowPowerSnapshot;
        HashMap map = mapArr[i];
        mapArr[i] = null;
        if (map == null) {
            return;
        }
        try {
            Integer num = (Integer) map.get("opt_target_freq");
            int iIntValue = num.intValue();
            if (iIntValue == 0) {
                log("reportLowPowerStatistic skip: no target freq");
                return;
            }
            int iIntValue2 = ((Integer) map.get("subId")).intValue();
            SQLiteDatabase readableDatabase = this.dbHelper.getReadableDatabase();
            if (readableDatabase == null || !readableDatabase.isOpen()) {
                log("reportLowPowerStatistic skip: db unavailable");
                return;
            }
            List allLowerPowerCellDetailsBySubId = this.dbHelper.getAllLowerPowerCellDetailsBySubId(readableDatabase, iIntValue2);
            if (allLowerPowerCellDetailsBySubId.isEmpty()) {
                log("reportLowPowerStatistic skip: no cells after sleep");
                return;
            }
            String str = (String) map.get("snapshot_update_time");
            String str2 = (String) ((HashMap) allLowerPowerCellDetailsBySubId.get(0)).get("update_time_mills");
            if (str != null && str.equals(str2)) {
                log("reportLowPowerStatistic skip: data not updated during sleep");
                return;
            }
            float fCalcWeightedScore = calcWeightedScore(allLowerPowerCellDetailsBySubId, ((Boolean) map.get("data_on")).booleanValue());
            long jCurrentTimeMillis = System.currentTimeMillis() - ((Long) map.get("sleep_start_time")).longValue();
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null) {
                CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
                if (currentCellIdentity instanceof CellIdentityNr) {
                    earfcn = ((CellIdentityNr) currentCellIdentity).getNrarfcn();
                } else if (currentCellIdentity instanceof CellIdentityLte) {
                    earfcn = ((CellIdentityLte) currentCellIdentity).getEarfcn();
                } else {
                    earfcn = 0;
                }
            } else {
                earfcn = 0;
            }
            boolean z = earfcn == iIntValue;
            HashMap map2 = new HashMap();
            map2.put("plmn", map.get("plmn"));
            map2.put("data_on", map.get("data_on"));
            map2.put("db_cell_count", map.get("db_cell_count"));
            map2.put("opt_target_freq", num);
            map2.put("weighted_score_before", map.get("weighted_score_before"));
            map2.put("weighted_score_after", Float.valueOf(fCalcWeightedScore));
            map2.put("sleep_duration_ms", Long.valueOf(jCurrentTimeMillis));
            map2.put("actual_camped_freq", Integer.valueOf(earfcn));
            map2.put("freq_match", Boolean.valueOf(z));
            map2.put("Params", map.get("cell_info_list"));
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_low_power_optimization_statistic", map2);
            log("reportLowPowerStatistic reported: freq=" + iIntValue + " actualFreq=" + earfcn + " freqMatch=" + z + " sleepMs=" + jCurrentTimeMillis + " scoreBefore=" + map.get("weighted_score_before") + " scoreAfter=" + fCalcWeightedScore);
        } catch (Exception e) {
            Log.e("MtkSelfStudyController", "reportLowPowerStatistic error", e);
        }
    }

    private boolean resetLowPowerOption(int i) {
        if (MiuiPhoneUtils.getPhone(i) == null) {
            log("resetLowPowerOption phone is null for phoneId: " + i);
            return false;
        }
        return FiveGManagerBase.getInstance().sendAtCmdSync(i, new String[]{"MIPC_SET_ARFCN_CONFIG", "5", "1", "0,0"}, null);
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void resetLowPowerOpt(boolean z) {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            log("resetLowPowerOpt lowerPowerOpt[" + i + "] = " + this.lowerPowerOpt[i] + " forceFlag = " + z);
            int i2 = this.lowerPowerOpt[i];
            LowPowerState lowPowerState = LowPowerState.LOW_POWER_STATE_STOP;
            if ((i2 != lowPowerState.value || z) && resetLowPowerOption(i)) {
                sendStateToModem(lowPowerState.value, i);
                this.lowerPowerOpt[i] = lowPowerState.value;
            }
            this.mLowPowerSnapshot[i] = null;
            this.mLastTargetFreq[i] = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAirPlaneStateChange(boolean z) {
        int sceneType;
        int i = this.dataType;
        if (i != 2 && i != 4) {
            log("processAirPlaneStateChange not in the stream scene");
            return;
        }
        if (z) {
            log("reset for airPlane enable");
            if (this.mWorkHandler.hasMessages(1023)) {
                this.mWorkHandler.removeMessages(1023);
            }
            this.mLastDataType = this.dataType;
            processStopDataStall();
            stopToCollectCellData(this.mIsWorkDay, this.mCollectActionType);
            resetBenefitStatisticList();
            removeMsgForExitSceneBreak();
            if (this.mStreamMediaOptimizationRst) {
                return;
            }
            enableSaForStreamMedia(true);
            return;
        }
        log("remove stop data stall msg for airPlane disable");
        if (this.mWorkHandler.hasMessages(1023)) {
            this.mWorkHandler.removeMessages(1023);
        }
        if (!this.mIsMobileDataOn || this.mLastToModemDataType == (sceneType = getSceneType())) {
            return;
        }
        this.dataType = sceneType;
        processDelayDataAppTypeChange(sceneType, "AIRPLANE_OFF");
        processUidAndActivityDataTypeChange(this.mLastToModemDataType, sceneType);
    }

    private void regUnRegisterAtUrcInd(boolean z) {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            Handler handler = this.mWorkHandler;
            if (z) {
                phone.registerForOemIndication(handler, 1000, phone);
            } else {
                phone.unregisterForOemIndication(handler);
            }
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void handlePlatformDependentMessage(Message message) throws Throwable {
        int i;
        Phone activeDataPhone;
        OffsetCell currentCellInfo;
        log("handlePlatformDependentMessage msg.what is " + message.what);
        int i2 = message.what;
        boolean z = true;
        if (i2 == 1) {
            if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                return;
            }
            int iIntValue = ((Integer) message.obj).intValue();
            log("EVENT_GAME_DELAY_CHECK type: " + iIntValue);
            delayCheck(iIntValue);
            return;
        }
        int i3 = 0;
        if (i2 == 1000) {
            try {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                Phone phone = (Phone) asyncResult.userObj;
                int phoneId = phone.getPhoneId();
                String str = (String) asyncResult.result;
                Log.d("MtkSelfStudyController", "PhoneId is " + phoneId + " urcAtString is " + str);
                if (str.startsWith("+EDMFAPP:")) {
                    String[] strArrSplit = str.split(",");
                    int i4 = Integer.parseInt(strArrSplit[0].split(":")[1].trim());
                    if (i4 == 20107) {
                        analysisModemBaseInfo(strArrSplit, phoneId);
                        return;
                    } else if (i4 == 20105) {
                        if (this.mIsSupportLowPowerModeOptimization && this.cloudLowPowerOn) {
                            if (Integer.parseInt(strArrSplit[1].trim()) == 337) {
                                handlePingpongCellInfo(strArrSplit, phone.getSubId());
                                return;
                            }
                        }
                        return;
                    }
                }
                Phone activeDataPhone2 = MiuiPhoneUtils.getActiveDataPhone();
                if (activeDataPhone2 != null && phoneId == activeDataPhone2.getPhoneId()) {
                    if (str.startsWith("+EDMFAPP:")) {
                        String[] strArrSplit2 = str.split(",");
                        int i5 = Integer.parseInt(strArrSplit2[0].split(":")[1].trim());
                        Log.d("MtkSelfStudyController", "appId is " + i5 + "data.length is " + strArrSplit2.length);
                        if (i5 != 20002 && i5 != 20003 && i5 != 20006) {
                            if (i5 == 20004) {
                                analysisWechatStallInfo(strArrSplit2);
                                return;
                            }
                            return;
                        }
                        int i6 = Integer.parseInt(strArrSplit2[1].trim());
                        if (i6 == 7) {
                            analysisAndRestoreCellInfoList(str);
                            return;
                        }
                        if (i6 == 8) {
                            notifyUser();
                            return;
                        }
                        if (i6 == 11) {
                            analysisAndRestoreCellRouteV2(str);
                            return;
                        }
                        if (i6 == 13) {
                            updateRatChangedCount(Integer.parseInt(strArrSplit2[2].trim()));
                            return;
                        }
                        if (i6 == 17) {
                            SelfStudyControllerBase.CellUnit cellUnit = new SelfStudyControllerBase.CellUnit();
                            cellUnit.cellId = Long.parseLong(strArrSplit2[2].trim());
                            cellUnit.cellFreq = Integer.parseInt(strArrSplit2[3].trim());
                            cellUnit.cellPci = Integer.parseInt(strArrSplit2[4].trim());
                            log("unit.cellid is " + cellUnit.cellId + "  mTempCellId is " + this.mTempCellId);
                            long j = cellUnit.cellId;
                            if (j == this.mTempCellId || cellUnit.cellFreq <= 0) {
                                return;
                            }
                            this.mTempCellId = j;
                            Iterator it = SelfStudyControllerBase.cbs.iterator();
                            while (it.hasNext()) {
                                ((SelfStudyControllerBase.CellCallBack) it.next()).onCellLocationChanged(phoneId, cellUnit);
                            }
                            return;
                        }
                        if (i6 == 20) {
                            updateBadCells(str);
                            return;
                        }
                        if (i6 == 27) {
                            analysisAndRestoreBadCellsV2(strArrSplit2);
                            return;
                        }
                        if (i6 == 29) {
                            int dataNetworkType = this.tmManager.getDataNetworkType();
                            TelephonyManager telephonyManager = this.tmManager;
                            if (dataNetworkType == 13 || telephonyManager.getDataNetworkType() == 19) {
                                NetworkRecoveryStrategyBase.getInstance().reAttach(phoneId);
                                return;
                            }
                            return;
                        }
                        if (i6 == 31) {
                            if (this.tmManager.getDataNetworkType() == 20 && enableNr(false)) {
                                Handler handler = this.mWorkHandler;
                                handler.sendMessageDelayed(handler.obtainMessage(1022), 15000L);
                                return;
                            }
                            return;
                        }
                        if (i6 == 407) {
                            if (MiuiPhoneUtils.isSupportMtkDataOptimization() && !SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && strArrSplit2.length > 3 && (i = this.dataType) == 4 && i == Integer.parseInt(strArrSplit2[2]) && isSubwayScene() && Integer.parseInt(strArrSplit2[3]) == 1) {
                                Handler handler2 = this.mWorkHandler;
                                handler2.sendMessage(handler2.obtainMessage(1056));
                                return;
                            }
                            return;
                        }
                        if (i6 == 303) {
                            if (Integer.parseInt(strArrSplit2[2].trim()) != 1) {
                                z = false;
                            }
                            notifyGameDelay(z);
                            return;
                        }
                        if (i6 != 304) {
                            switch (i6) {
                                case 101:
                                    if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && strArrSplit2.length > 6) {
                                        doOffsetFromMd(strArrSplit2);
                                        break;
                                    }
                                case 102:
                                    int i7 = Integer.parseInt(strArrSplit2[2].trim());
                                    int i8 = Integer.parseInt(strArrSplit2[3].trim());
                                    int i9 = Integer.parseInt(strArrSplit2[4].trim());
                                    int i10 = Integer.parseInt(strArrSplit2[5].trim());
                                    int i11 = Integer.parseInt(strArrSplit2[6].trim());
                                    Phone activeDataPhone3 = MiuiPhoneUtils.getActiveDataPhone();
                                    if (activeDataPhone3 == null) {
                                        return;
                                    }
                                    if (i6 == 102) {
                                        offsetForModemAi(activeDataPhone3, i8, i9, i10, i11);
                                        return;
                                    }
                                    OffsetCell currentCellInfo2 = getCurrentCellInfo(activeDataPhone3);
                                    if (currentCellInfo2 != null && currentCellInfo2.rat == i11 && currentCellInfo2.pci == i9 && currentCellInfo2.freq == i8) {
                                        currentCellInfo2.offset = i10 * 4;
                                        if (i7 != 2 && i7 != 4) {
                                            if (i7 == 10) {
                                                if (TelephonyCloudController.CLOUD_SMART_NETWORK_GAME_OPT_DISABLED) {
                                                    return;
                                                }
                                                handleAiGameOffsetInd(currentCellInfo2, activeDataPhone3.getPhoneId());
                                                return;
                                            } else {
                                                if (this.mSmartNetworkGameController == null || i5 == 20006) {
                                                    return;
                                                }
                                                handleGameOffsetInd(currentCellInfo2, activeDataPhone3.getPhoneId(), i7);
                                                return;
                                            }
                                        }
                                        if (!shouldDoActionForStreamMediaOpt()) {
                                            log("ignore process offset action");
                                            return;
                                        } else {
                                            handleLiveStreamingOffsetInd(currentCellInfo2, activeDataPhone3.getPhoneId());
                                            return;
                                        }
                                    }
                                    return;
                                case 103:
                                    if (!SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && (activeDataPhone = MiuiPhoneUtils.getActiveDataPhone()) != null) {
                                        doNextCellFromModemForGame(strArrSplit2);
                                        if (MiuiPhoneUtils.isSupportCongestionOptimization() || (currentCellInfo = getCurrentCellInfo(activeDataPhone)) == null) {
                                            return;
                                        }
                                        try {
                                            currentCellInfo.offset = Integer.parseInt(strArrSplit2[6].trim()) * 4;
                                            if (!shouldDoActionForStreamMediaOpt()) {
                                                log("ignore process cell offset action");
                                                return;
                                            } else {
                                                logV("trigger offset form cellMap.");
                                                handleLiveStreamingOffsetInd(currentCellInfo, activeDataPhone.getPhoneId());
                                                return;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            return;
                                        }
                                    }
                                    return;
                                case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                                    SelfStudyControllerBase.isBelowMtkDataOptVersionFive();
                                    return;
                                case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                                    if (strArrSplit2.length >= 5) {
                                        defaultResetFromModemForGame(strArrSplit2);
                                        return;
                                    }
                                    return;
                                case 106:
                                    if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                                        return;
                                    }
                                    getOffsetFromMdForGame(strArrSplit2);
                                    return;
                                case 107:
                                    if (!shouldDoActionForGameOpt() || SelfStudyControllerBase.isBelowMtkDataOptVersionFive() || strArrSplit2.length < 3) {
                                        return;
                                    }
                                    getStaticSceneScore(strArrSplit2, i5);
                                    return;
                                default:
                                    switch (i6) {
                                        case com.ot.pubsub.i.a.b /* 401 */:
                                        case 402:
                                        case 403:
                                        case com.ot.pubsub.i.a.c /* 404 */:
                                            processStreamMediaDataInfo(str, i6);
                                            return;
                                        default:
                                            return;
                                    }
                            }
                        } else {
                            if (Integer.parseInt(strArrSplit2[2].trim()) != 1) {
                                z = false;
                            }
                            this.mIsIgnoreGameStuck = z;
                            SmartNetworkGameController smartNetworkGameController = this.mSmartNetworkGameController;
                            if (smartNetworkGameController != null) {
                                smartNetworkGameController.notifyIgnoreStuckSceneInd(z);
                                return;
                            }
                            return;
                        }
                    } else {
                        Log.d("MtkSelfStudyController", "not dmf urc ignore");
                        return;
                    }
                }
                return;
            } catch (Exception e2) {
                Log.d("MtkSelfStudyController", "receive exception");
                e2.printStackTrace();
                return;
            }
        }
        if (i2 != 1016) {
            if (i2 == 1027) {
                int i12 = message.arg1;
                ServiceState serviceState = (ServiceState) message.obj;
                if (Build.IS_INTERNATIONAL_BUILD) {
                    processServiceStateChangeForInternationalBuild(i12, serviceState);
                    return;
                } else {
                    processServiceStateChange(i12, serviceState);
                    processServiceStateChangeForGame(i12, serviceState);
                    return;
                }
            }
            if (i2 == 1030) {
                log("EVENT_ABNORMAL_DEPRIORITIZE_SA");
                WechatDdsSaOpt wechatDdsSaOpt = this.mWechatOpt;
                if (wechatDdsSaOpt != null) {
                    Objects.requireNonNull(wechatDdsSaOpt);
                    wechatDdsSaOpt.updateActionPhase(1);
                    return;
                }
                return;
            }
            if (i2 == 1033) {
                setSbpParam(true);
                this.hasSendSbpAction = false;
                return;
            }
            if (i2 == 1043) {
                int iIntValue2 = ((Integer) message.obj).intValue();
                logV("DATA_APP_TYPE_CHANGED - toModemDataType = " + iIntValue2 + " , mLastToModemDataType = " + this.mLastToModemDataType);
                if (iIntValue2 != this.mLastToModemDataType) {
                    if (iIntValue2 == 0) {
                        processModemSceneExit(iIntValue2);
                        return;
                    } else {
                        if (this.mIsMobileDataOn) {
                            processModemSceneEnter(iIntValue2);
                            return;
                        }
                        return;
                    }
                }
                return;
            }
            if (i2 == 1051) {
                this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handlePlatformDependentMessage$5();
                    }
                });
                stopToCollectCellData(this.mIsWorkDay, 7);
                resetStreamOptForPhoneCrash();
                return;
            }
            if (i2 == 1057) {
                handleGameStopSearchNwGuard();
                return;
            }
            if (i2 != 1018) {
                if (i2 == 1019) {
                    try {
                        AsyncResult asyncResult2 = (AsyncResult) message.obj;
                        if (asyncResult2 != null && asyncResult2.exception == null) {
                            SelfStudyControllerBase.encryptedKey = Integer.parseInt(((String[]) asyncResult2.result)[0].split(",")[2].trim());
                        }
                        synchronized (this.mLock) {
                            try {
                                this.mLock.notifyAll();
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                        return;
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        return;
                    }
                }
                if (i2 == 1036) {
                    regUnRegisterAtUrcInd(true);
                    sendAtCmd(new String[]{"AT+EDMFAPP=20002,102,1,10", "+EDMFAPP"}, (Message) null, MiuiPhoneUtils.getActiveDataPhoneId());
                    this.isGameAiOpt = true;
                    return;
                }
                if (i2 == 1037) {
                    revertForModemAi();
                    return;
                }
                switch (i2) {
                    case DynamicSarService.WIFI_DATA_STATE_TCP_STATE /* 32 */:
                        this.enterGameResidentDays = this.mPreferenceUtils.getInt("game_resident_enter_days", 0);
                        log("EVENT_GAME_STATISTIC_5S isEnterGame = " + this.isEnterGame + " enterGameResidentDays: " + this.enterGameResidentDays);
                        GameCellDdsSaOpt gameCellDdsSaOpt = this.mGameCellOpt;
                        if (gameCellDdsSaOpt != null && this.isEnterGame) {
                            SelfStudyControllerBase.mEnterGameOptimizer = true;
                            gameCellDdsSaOpt.updateActionPhase(5);
                            if (isHasDelayApiGame()) {
                                log("enter has delay game");
                                sendGameOffset("GAME");
                            } else if (isNoDelayApiGame()) {
                                log("enter no delay game");
                                sendGameOffset("AI");
                                SelfStudyControllerBase.mEnterGameAiOptimizer = true;
                                saveGameCellStudyDays();
                                Handler handler3 = this.mWorkHandler;
                                handler3.sendMessageDelayed(handler3.obtainMessage(42), MirrorDesktopHelper.TIMEOUT_MILLIS);
                            }
                            recordCount("gameResidentCellOptCnt", "game");
                            if (isInHomeOrCompanyScene(this.curGameScene)) {
                                recordCount("ResidentCellCnt", "game");
                            }
                            if (isInHomeOrCompanyFromXiaoAiScene()) {
                                recordCount("XiaoAiResidentCellCnt", "game");
                            }
                        }
                        this.mWorkHandler.removeMessages(32);
                        return;
                    case 33:
                        setGameCellPenalt((String) message.obj);
                        this.mWorkHandler.removeMessages(33);
                        return;
                    case 34:
                        if (MiuiPhoneUtils.isSupportMtkDataOptimization() || MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) {
                            handleExitGameStartSearchNw();
                            Handler handler4 = this.mWorkHandler;
                            handler4.sendMessageDelayed(handler4.obtainMessage(38), 30000L);
                            return;
                        }
                        return;
                    case MiRilHookBase.EVENT_OEMHOOK_NV_EFS_SYNC /* 35 */:
                        log("mIsMobileDataOn " + this.mIsMobileDataOn + " shouldDoActionForGameOpt: " + shouldDoActionForGameOpt());
                        if (this.mIsMobileDataOn && shouldDoActionForGameOpt()) {
                            handleEnterGameStopSearchNw();
                            if (isInHomeScence()) {
                                this.curGameScene = 1;
                            } else if (isInCompanyScence()) {
                                this.curGameScene = 0;
                            }
                            if (isInHomeOrCompanyScene(this.curGameScene)) {
                                if (this.mWorkHandler.hasMessages(38)) {
                                    this.mWorkHandler.removeMessages(38);
                                }
                                this.isEnterGame = true;
                                setGameSceneProperty(true);
                                enterOptimizationForModemGameResidentCell();
                            } else if (!SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && (this.isStaticCell || this.isFirst20min)) {
                                this.isEnterGame = true;
                                log("enter static game scene");
                                if (this.mWorkHandler.hasMessages(38)) {
                                    this.mWorkHandler.removeMessages(38);
                                }
                            }
                            if (this.isEnterGame) {
                                startModemModel();
                                return;
                            }
                            return;
                        }
                        return;
                    case 36:
                        gameStatistic20Min();
                        return;
                    case 37:
                        if ((isHasDelayApiGame() && !this.isGameLag && this.mEnterGameBattleState) || (isNoDelayApiGame() && !this.isGameAiLag && !this.isGameTraModelLag)) {
                            if (isInHomeOrCompanyScene(this.curGameScene)) {
                                resetActionForGameCell((String) message.obj, true);
                            } else if (!SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && this.isStaticCell) {
                                log("EVENT_GAME_CELL_CHANGE_20S");
                                resetActionForGameCell((String) message.obj, false);
                            }
                        }
                        this.mWorkHandler.removeMessages(37);
                        return;
                    case 38:
                        if (SelfStudyControllerBase.STATIC_GAME_CELL_WHITELIST_PACKAGES.contains(this.mLastTopGamePackage) || SelfStudyControllerBase.STATIC_GAME_AI_CELL_NO_DELAY_WHITELIST_PACKAGES.contains(this.mLastTopGamePackage)) {
                            if (this.isEnterGame && isEnterGameOptimizer()) {
                                sameGameCell(System.currentTimeMillis());
                            }
                            log("EVENT_EXIT_GAME_30S : isEnterGame = " + this.isEnterGame + " isHasDelayApiGame = " + isHasDelayApiGame());
                            processGameExit();
                            return;
                        }
                        return;
                    case MiRilHookBase.EVENT_OEMHOOK_CARD_SLOT_STATUS_QUERY /* 39 */:
                        this.mWorkHandler.removeMessages(39);
                        return;
                    default:
                        switch (i2) {
                            case 41:
                                this.mWorkHandler.removeMessages(41);
                                return;
                            case 42:
                                Handler handler5 = this.mWorkHandler;
                                handler5.sendMessageDelayed(handler5.obtainMessage(36), 1200000L);
                                Handler handler6 = this.mWorkHandler;
                                handler6.sendMessageDelayed(handler6.obtainMessage(37, this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq + "-" + this.oldGameRat), 20000L);
                                return;
                            case 43:
                                if (isNoDelayApiGame() && SelfStudyControllerBase.mEnterGameAiOptimizer && this.isGameTraModelLag) {
                                    recordCount("gameResidentTop1NextCellLagCnt", "game");
                                }
                                this.mWorkHandler.removeMessages(43);
                                return;
                            case MiRilHookBase.EVENT_OEMHOOK_HVOLTE_MBN_CFG /* 44 */:
                                if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                                    return;
                                }
                                updateVideoAndGameAiStats("event_game_resident_cell_optimization_statistic", getTotalDuration((ConcurrentHashMap) message.obj, 4, this.isSetGameTopCell ? "top" : "notop", true));
                                return;
                            case AbstractBaseTemplate.BIG_ICON_WIDTH_THRESHOLD_DP /* 45 */:
                                if (!SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && this.mSetGameOptNr2Lte && this.curGameRat == 1 && SelfStudyControllerBase.mEnterGameOptimizer) {
                                    recordCount("gameResidentCellNr2LteNoRecovery", "game");
                                    this.isNr2Lte = false;
                                    return;
                                }
                                return;
                            case 46:
                                if (!SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && this.isEnterGame && this.isFirst20min) {
                                    setStaticCmdAndMessage();
                                    if (MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) {
                                        return;
                                    }
                                    sendAtCmd(new String[]{"AT+EDMFAPP=20006,107,1,0", "+EDMFAPP"}, (Message) null, MiuiPhoneUtils.getActiveDataPhoneId());
                                    return;
                                }
                                return;
                            default:
                                switch (i2) {
                                    case 1021:
                                        AsyncResult asyncResult3 = (AsyncResult) message.obj;
                                        if (asyncResult3 == null || asyncResult3.exception != null) {
                                            return;
                                        }
                                        String[] strArr = (String[]) asyncResult3.result;
                                        String[] strArrSplit3 = strArr[0].split(",");
                                        String[] strArrSplit4 = strArr[1].split(",");
                                        SQLiteDatabase writableDatabase = this.dbHelper.getWritableDatabase();
                                        analyzeAndRestoreResidentCells(strArrSplit3, writableDatabase);
                                        analyzeAndRestoreResidentCells(strArrSplit4, writableDatabase);
                                        int i13 = Integer.parseInt(strArrSplit3[2].trim());
                                        log("sceneType : " + i13 + " cell ping pong counts :" + getCellPingPongCounts());
                                        if (i13 == SelfStudyControllerBase.Scene.Home.value) {
                                            int i14 = this.mPreferenceUtils.getInt("study_resident_cells_days", 0);
                                            log("studyResidentCellsDays : " + i14);
                                            if (i14 > 1) {
                                                while (i3 < MiuiPhoneUtils.PHONE_COUNT) {
                                                    Phone phone2 = MiuiPhoneUtils.getPhone(i3);
                                                    if (phone2 != null) {
                                                        int subId = phone2.getSubId();
                                                        SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
                                                        SelfStudyControllerBase.Scene scene = SelfStudyControllerBase.Scene.Home;
                                                        int i15 = scene.value;
                                                        SelfStudyControllerBase.Rat rat = SelfStudyControllerBase.Rat.Lte;
                                                        selfStudyDatabaseHelper.statisticsResidentCellsInfo(writableDatabase, i15, rat.value, subId);
                                                        SelfStudyDatabaseHelper selfStudyDatabaseHelper2 = this.dbHelper;
                                                        int i16 = scene.value;
                                                        SelfStudyControllerBase.Rat rat2 = SelfStudyControllerBase.Rat.Nr;
                                                        selfStudyDatabaseHelper2.statisticsResidentCellsInfo(writableDatabase, i16, rat2.value, subId);
                                                        SelfStudyDatabaseHelper selfStudyDatabaseHelper3 = this.dbHelper;
                                                        SelfStudyControllerBase.Scene scene2 = SelfStudyControllerBase.Scene.Company;
                                                        selfStudyDatabaseHelper3.statisticsResidentCellsInfo(writableDatabase, scene2.value, rat.value, subId);
                                                        this.dbHelper.statisticsResidentCellsInfo(writableDatabase, scene2.value, rat2.value, subId);
                                                    }
                                                    i3++;
                                                }
                                                loadResidentCellIds();
                                                statResidentCellOptimizationBenefit(writableDatabase);
                                                doResidentCellAction(writableDatabase);
                                                if (this.mIsSupportLowPowerModeOptimization && this.cloudLowPowerOn) {
                                                    updateLowPowerInfo(writableDatabase);
                                                }
                                            } else {
                                                this.mPreferenceUtils.putInt("study_resident_cells_days", i14 + 1);
                                            }
                                        }
                                        writableDatabase.close();
                                        return;
                                    case 1022:
                                        enableNr(true);
                                        if (this.mWorkHandler.hasMessages(1022)) {
                                            this.mWorkHandler.removeMessages(1022);
                                            return;
                                        }
                                        return;
                                    case 1023:
                                        Log.d("MtkSelfStudyController", "EVENT_STOP_DATA_STALL  hasSendDataStallAction = " + this.hasSendDataStallAction + " lteLastModes" + this.lastLteModes + " nrLastModes = " + this.lastNrModes);
                                        stopStreamDataStall(0);
                                        processStopDataStall();
                                        return;
                                    case SmartNetworkRelayScore.TRAFFIC_UNIT /* 1024 */:
                                        int iIntValue3 = ((Integer) message.obj).intValue();
                                        Log.d("MtkSelfStudyController", "EVENT_START_DATA_STALL type = " + iIntValue3);
                                        processStartDataStall(iIntValue3);
                                        return;
                                    case 1025:
                                        handleExitLiveStreamingApps();
                                        return;
                                    default:
                                        switch (i2) {
                                            case 1047:
                                                this.mCurrentGameDelay = ((Integer) message.obj).intValue();
                                                log("mCurrentGameDelay: " + this.mCurrentGameDelay);
                                                setGameDelayLevel(getGameDelayLevel(this.mCurrentGameDelay, this.mTopGamePackage));
                                                return;
                                            case 1048:
                                                if (shouldDoActionForGameOpt()) {
                                                    this.mCurrentGameDelay = 0;
                                                    List list = SelfStudyControllerBase.STATIC_GAME_CELL_WHITELIST_PACKAGES;
                                                    if (list.contains((String) message.obj)) {
                                                        this.mEnterGameBoosterCount++;
                                                    }
                                                    List list2 = SelfStudyControllerBase.STATIC_GAME_AI_CELL_NO_DELAY_WHITELIST_PACKAGES;
                                                    if (list2.contains((String) message.obj)) {
                                                        this.mEnterGameAiBoosterCount++;
                                                    }
                                                    if ((!list.contains(this.mTopGamePackage) && list.contains((String) message.obj)) || (!list2.contains(this.mTopGamePackage) && list2.contains((String) message.obj))) {
                                                        this.mLastTopGamePackage = (String) message.obj;
                                                    }
                                                    this.mTopGamePackage = (String) message.obj;
                                                    log("mTopGamePackage = " + this.mTopGamePackage + " isHasDelayApiGame(): " + isHasDelayApiGame() + " mLastTopGamePackage: " + this.mLastTopGamePackage + " mEnterGameBoosterCount: " + this.mEnterGameBoosterCount + "mEnterGameAiBoosterCount: " + this.mEnterGameAiBoosterCount + " isNoDelayApiGame: " + isNoDelayApiGame());
                                                    if (isHasDelayApiGame() && this.mEnterGameBoosterCount == 1) {
                                                        log("enter delay api game");
                                                        recordCount("gameResidentCellEnterAllCnt", "game");
                                                        if (isInHomeOrCompanyScene(this.curGameScene)) {
                                                            getCurrentCellInfoForGame();
                                                            Handler handler7 = this.mWorkHandler;
                                                            handler7.sendMessageDelayed(handler7.obtainMessage(32), MirrorDesktopHelper.TIMEOUT_MILLIS);
                                                            this.mEnterGameBoosterCount++;
                                                            return;
                                                        }
                                                        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive() || this.isStaticCell || !this.isFirst20min) {
                                                            return;
                                                        }
                                                        log("enter delay api game for static scene");
                                                        recordCount("gameStaticCellEnterAllCnt", "game");
                                                        getCurrentCellInfoForGame();
                                                        this.mEnterGameBoosterCount++;
                                                        setStaticCmdAndMessage();
                                                        return;
                                                    }
                                                    if (isNoDelayApiGame() && this.mEnterGameAiBoosterCount == 1) {
                                                        log("enter no delay api game");
                                                        if (isInHomeOrCompanyScene(this.curGameScene)) {
                                                            getCurrentCellInfoForGame();
                                                            Handler handler8 = this.mWorkHandler;
                                                            handler8.sendMessageDelayed(handler8.obtainMessage(32), MirrorDesktopHelper.TIMEOUT_MILLIS);
                                                            this.mEnterGameAiBoosterCount++;
                                                            return;
                                                        }
                                                        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive() || this.isStaticCell || !this.isFirst20min) {
                                                            return;
                                                        }
                                                        log("enter no delay api game for static scene");
                                                        getCurrentCellInfoForGame();
                                                        this.mEnterGameBoosterCount++;
                                                        Handler handler9 = this.mWorkHandler;
                                                        handler9.sendMessageDelayed(handler9.obtainMessage(42), MirrorDesktopHelper.TIMEOUT_MILLIS);
                                                        setStaticCmdAndMessage();
                                                        return;
                                                    }
                                                    return;
                                                }
                                                return;
                                            case 1049:
                                                this.isBattle = ((Boolean) message.obj).booleanValue();
                                                if (shouldDoActionForGameOpt() && this.isEnterGame && isHasDelayApiGame()) {
                                                    if (isInHomeOrCompanyScene(this.curGameScene) || (!SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && isStaticCell())) {
                                                        boolean z2 = this.isBattle;
                                                        boolean z3 = this.mEnterGameBattleState;
                                                        if (!z2) {
                                                            if (!z3) {
                                                                log("battle end ignored, not in battle");
                                                                return;
                                                            }
                                                            this.mWorkHandler.removeMessages(1049, Boolean.FALSE);
                                                            log("battle end");
                                                            long jCurrentTimeMillis = System.currentTimeMillis();
                                                            this.endGameTime = jCurrentTimeMillis;
                                                            addOrUpdateGameStallDur(this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, this.mlastGameCellTime), this.oldGameGci, this.endGameTime);
                                                            long j2 = this.endGameTime;
                                                            this.mlastGameCellTime = j2;
                                                            if (this.isGameLag) {
                                                                addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j2, this.startGameLagTime) * ((long) getLastGameDelay(this.mlastGameDelay)), this.endGameTime, this.mCurrentGameCellStats, "AP");
                                                                this.startGameLagTime = this.endGameTime;
                                                            }
                                                            if (this.mWorkHandler.hasMessages(37)) {
                                                                this.mWorkHandler.removeMessages(37);
                                                            }
                                                            this.mEnterGameBattleState = false;
                                                            return;
                                                        }
                                                        if (z3) {
                                                            return;
                                                        }
                                                        log("start game with battle");
                                                        this.startGameTime = System.currentTimeMillis();
                                                        String str2 = this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq + "-" + this.oldGameRat;
                                                        if (!this.mWorkHandler.hasMessages(37)) {
                                                            Handler handler10 = this.mWorkHandler;
                                                            handler10.sendMessageDelayed(handler10.obtainMessage(37, str2), 20000L);
                                                        }
                                                        this.mEnterGameBattleState = true;
                                                        defaultResetFromModemForGame();
                                                        saveGameCellStudyDays();
                                                        return;
                                                    }
                                                    return;
                                                }
                                                return;
                                            default:
                                                switch (i2) {
                                                    case 3001:
                                                        try {
                                                            AsyncResult asyncResult4 = (AsyncResult) message.obj;
                                                            if (asyncResult4 == null || asyncResult4.exception != null) {
                                                                return;
                                                            }
                                                            String[] strArr2 = (String[]) asyncResult4.result;
                                                            String[] strArrSplit5 = strArr2[0].split(",");
                                                            int length = strArr2.length;
                                                            while (i3 < length) {
                                                                log("AT RESP:" + strArr2[i3]);
                                                                i3++;
                                                            }
                                                            int i17 = Integer.parseInt(strArrSplit5[1]);
                                                            int i18 = Integer.parseInt(strArrSplit5[2]);
                                                            long j3 = Long.parseLong(strArrSplit5[3]);
                                                            int i19 = Integer.parseInt(strArrSplit5[4]);
                                                            int i20 = Integer.parseInt(strArrSplit5[5]);
                                                            int i21 = Integer.parseInt(strArrSplit5[6]);
                                                            int i22 = Integer.parseInt(strArrSplit5[7]);
                                                            int i23 = Integer.parseInt(strArrSplit5[8]);
                                                            log("get modem info: evt:" + i17 + " source:" + i18 + " rat:" + j3 + " freq:" + i19 + " pci:" + i20 + " action:" + i21 + " offset:" + i22 + " b1Thr:" + i23);
                                                            if (i21 != 255) {
                                                                processWechatStallEvent(j3, i19, i20, i21, i23, i22);
                                                                addOrUpdateWechatStallCount(i19, i20, j3);
                                                                return;
                                                            }
                                                            return;
                                                        } catch (Exception e4) {
                                                            e4.printStackTrace();
                                                            return;
                                                        }
                                                    case 3002:
                                                        int intExtra = ((Intent) message.obj).getIntExtra("android.telephony.extra.SIM_STATE", 1);
                                                        WechatDdsSaOpt wechatDdsSaOpt2 = this.mWechatOpt;
                                                        if (wechatDdsSaOpt2 != null) {
                                                            wechatDdsSaOpt2.onSimStateChanged(intExtra);
                                                        }
                                                        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                                                            processSimStateChangeForStream(intExtra);
                                                            initBaseModel(intExtra);
                                                        }
                                                        initRealTimeOptimization(intExtra);
                                                        if (this.mIsSupportLowPowerModeOptimization && this.cloudLowPowerOn && intExtra == 1) {
                                                            this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda7
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    this.f$0.lambda$handlePlatformDependentMessage$4();
                                                                }
                                                            });
                                                            return;
                                                        }
                                                        return;
                                                    case 3003:
                                                        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
                                                        int iIntValue4 = ((Integer) message.obj).intValue();
                                                        if (iIntValue4 == 2) {
                                                            enableSaForWechat(true);
                                                        } else if (iIntValue4 == 3) {
                                                            sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_SA, (Message) null);
                                                            this.mWorkHandler.removeMessages(3006);
                                                            this.mWechatA3ActionOptRst = false;
                                                        }
                                                        this.wechatOptAction = -1;
                                                        return;
                                                    default:
                                                        switch (i2) {
                                                            case 3006:
                                                                MiuiPhoneUtils.getActiveDataPhoneId();
                                                                return;
                                                            case 3007:
                                                                if (this.isCellChanged) {
                                                                    recordActionCount("action1EffCnt", this.mWechatOptStats);
                                                                }
                                                                this.isCellChanged = false;
                                                                return;
                                                            case 3008:
                                                                if (this.isLte && this.isCellChanged) {
                                                                    recordActionCount("action2EffCnt", this.mWechatOptStats);
                                                                }
                                                                this.isCellChanged = false;
                                                                return;
                                                            case 3009:
                                                                if (this.isLte && this.isCellChanged) {
                                                                    recordActionCount("action3EffCnt", this.mWechatOptStats);
                                                                }
                                                                this.isCellChanged = false;
                                                                return;
                                                            case 3010:
                                                                recordActionCount("action4EffCnt", this.mWechatOptStats);
                                                                this.isCellChanged = false;
                                                                return;
                                                            default:
                                                                return;
                                                        }
                                                }
                                        }
                                }
                        }
                }
            }
        }
        try {
            AsyncResult asyncResult5 = (AsyncResult) message.obj;
            if (asyncResult5 != null && asyncResult5.exception == null) {
                String[] strArrSplit6 = ((String[]) asyncResult5.result)[0].split(",");
                SelfStudyControllerBase.CellUnit cellUnit2 = new SelfStudyControllerBase.CellUnit();
                cellUnit2.cellId = Long.parseLong(strArrSplit6[1].trim());
                cellUnit2.cellFreq = Integer.parseInt(strArrSplit6[2].trim());
                cellUnit2.cellPci = Integer.parseInt(strArrSplit6[3].trim());
                log("cellId = " + cellUnit2.cellId + " cellFreq " + cellUnit2.cellFreq + "  cellPci " + cellUnit2.cellPci);
                int i24 = message.what;
                if (i24 == 1016) {
                    this.mSampingCellInfoList.add(cellUnit2);
                } else if (i24 == 1018) {
                    this.mGlobalCurrentCellId = String.valueOf(cellUnit2.cellFreq) + cellUnit2.cellPci;
                    this.mGlobalCurrentCellId = getCombineString(this.mGlobalCurrentCellId, Long.toString(cellUnit2.cellId, 16));
                }
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        synchronized (this.mLock) {
            try {
                this.mLock.notifyAll();
            } catch (Exception e6) {
                e6.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handlePlatformDependentMessage$4() {
        resetLowPowerOpt(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handlePlatformDependentMessage$5() {
        this.mIsWorkDay = isWorkDay(System.currentTimeMillis());
    }

    private void processModemSceneEnter(int i) {
        if (this.cloudStreamMediaOn || !(i == 2 || i == 4)) {
            sendSelfStudyRequest(15, 1, new int[]{i});
            if (i == 2 || i == 4) {
                sendStreamCellInfoToModem();
                doActionForManualCellMap();
                doActionForCellMap();
                recordCount("streamMediaEnterCnt", "streamMedia");
                processSceneTypeChange("dataTypeChange", true);
            }
            this.mLastToModemDataType = i;
            logV("processModemSceneEnter done.");
        }
    }

    private void processModemSceneExit(int i) {
        sendSelfStudyRequest(15, 1, new int[]{i});
        this.mLastToModemDataType = i;
        if (i != 2 && i != 4 && !this.mStreamMediaOptimizationRst) {
            enableSaForStreamMedia(true);
        }
        stopToCollectCellData(this.mIsWorkDay, this.mCollectActionType);
        resetBenefitStatisticList();
        processSceneTypeChange("dataTypeChange", false);
        processWhiteListForSceneExit();
        logV("processModemSceneExit done.");
    }

    /* JADX WARN: Code duplicated, block: B:100:0x0197  */
    private void processServiceStateChange(int i, ServiceState serviceState) {
        int lteRsrp;
        int lteRssnr;
        SignalStrength signalStrength;
        if (this.enterWechatVideoDays > this.studyBadCellDays && i == MiuiPhoneUtils.getActiveDataPhoneId() && MiuiPhoneUtils.isValidatePhoneId(i) && this.mWechatOpt != null) {
            int dataNetworkType = serviceState == null ? 0 : serviceState.getDataNetworkType();
            ServiceState serviceState2 = this.mServiceState[i];
            int dataNetworkType2 = serviceState2 == null ? 0 : serviceState2.getDataNetworkType();
            log("processServiceStateChange: newRat = " + dataNetworkType + "oldRat = " + dataNetworkType2);
            if (dataNetworkType2 == dataNetworkType) {
                log("same rat, return");
                return;
            }
            boolean z = isNrNwType(dataNetworkType2) && !isNrNwType(dataNetworkType);
            boolean z2 = isLteNwType(dataNetworkType) && isNrNwType(dataNetworkType2);
            boolean z3 = isLteNwType(dataNetworkType2) && !isLteNwType(dataNetworkType);
            boolean zIsDataRegistered = MiuiPhoneUtils.isDataRegistered(i);
            this.isLte = isLteNwType(dataNetworkType);
            boolean[] saPowerEnable = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSaPowerEnable();
            log("processServiceStateChange: wechatOptAction = " + this.wechatOptAction + " mSaPowerEnabled = " + saPowerEnable + " mWechatA3ActionOptRst = " + this.mWechatA3ActionOptRst);
            if (shouldDoActionForWechatOpt() && this.isVideoFront && z2 && saPowerEnable.length > 0 && saPowerEnable[i] && !this.mWechatA3ActionOptRst) {
                Phone phone = MiuiPhoneUtils.getPhone(i);
                if (phone == null || (signalStrength = phone.getSignalStrength()) == null) {
                    lteRsrp = -110;
                    lteRssnr = 0;
                } else {
                    lteRsrp = signalStrength.getLteRsrp();
                    lteRssnr = signalStrength.getLteRssnr();
                }
                log("processServiceStateChange: rsrp = " + lteRsrp + "snr = " + lteRssnr);
                if (lteRsrp > -105 && lteRssnr > 0) {
                    WechatDdsSaOpt wechatDdsSaOpt = this.mWechatOpt;
                    if (wechatDdsSaOpt != null) {
                        Objects.requireNonNull(wechatDdsSaOpt);
                        wechatDdsSaOpt.updateActionPhase(1);
                    }
                } else {
                    this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(1030), 10000L);
                }
            }
            if (z3 && this.mWorkHandler.hasMessages(1030)) {
                this.mWorkHandler.removeMessages(1030);
            }
            if (z && this.mWorkHandler.hasMessages(3003)) {
                this.mWorkHandler.removeMessages(3003);
            }
            int i2 = this.wechatOptAction;
            if (i2 == 1) {
                if (this.isLte || (!zIsDataRegistered && this.mWechatOpt != null)) {
                    WechatDdsSaOpt wechatDdsSaOpt2 = this.mWechatOpt;
                    Objects.requireNonNull(wechatDdsSaOpt2);
                    wechatDdsSaOpt2.updateActionPhase(4);
                    this.mWorkHandler.removeMessages(3006);
                }
            } else if (i2 == 2) {
                if (z || !zIsDataRegistered) {
                    enableSaForWechat(true);
                }
                if (!this.isLte || (!zIsDataRegistered && this.mWechatOpt != null)) {
                    WechatDdsSaOpt wechatDdsSaOpt3 = this.mWechatOpt;
                    Objects.requireNonNull(wechatDdsSaOpt3);
                    wechatDdsSaOpt3.updateActionPhase(4);
                }
            } else if (i2 == 3) {
                if (z || !zIsDataRegistered) {
                    sendAtCmd(i, MtkAtCmd.ENABLE_SA, (Message) null);
                    this.mWechatA3ActionOptRst = false;
                    if (this.isLte) {
                        this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(3006), MirrorDesktopHelper.TIMEOUT_MILLIS);
                    }
                }
                if (this.isLte) {
                    WechatDdsSaOpt wechatDdsSaOpt4 = this.mWechatOpt;
                    Objects.requireNonNull(wechatDdsSaOpt4);
                    wechatDdsSaOpt4.updateActionPhase(4);
                    this.mWorkHandler.removeMessages(3006);
                } else {
                    WechatDdsSaOpt wechatDdsSaOpt5 = this.mWechatOpt;
                    Objects.requireNonNull(wechatDdsSaOpt5);
                    wechatDdsSaOpt5.updateActionPhase(4);
                    this.mWorkHandler.removeMessages(3006);
                }
            }
            if (serviceState != null) {
                this.mServiceState[i] = new ServiceState(serviceState);
            }
        }
    }

    private void processServiceStateChangeForInternationalBuild(int i, ServiceState serviceState) {
        if (serviceState != null && MiuiPhoneUtils.isValidatePhoneId(i) && i == MiuiPhoneUtils.getActiveDataPhoneId()) {
            boolean zIsSupportShortVideoOptimizationForRegion = isSupportShortVideoOptimizationForRegion(serviceState.getOperatorNumeric());
            regUnregShortVideoOptimizationReceiver(zIsSupportShortVideoOptimizationForRegion);
            regUnregAppStateMonitor(zIsSupportShortVideoOptimizationForRegion);
        }
    }

    /* JADX WARN: Code duplicated, block: B:55:0x0168  */
    /* JADX WARN: Code duplicated, block: B:57:0x0197  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void processCellLocationChanged(CellLocation cellLocation, int i) {
        int i2;
        int i3;
        long j;
        long j2;
        Phone phone;
        int i4;
        int nrarfcn;
        long j3;
        Phone phone2;
        long j4;
        int nrarfcn2;
        int pci;
        int i5;
        int i6;
        CellInfo currentCellInfoV3;
        int rsrpFormStreamDataTableByGciAndType;
        log("processCellLocationChanged + old pci" + this.oldPci + " old freq = " + this.oldFreq + "old rat = " + this.oldRat + " mlastCellTime = " + this.mlastCellTime);
        if (!MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            i2 = i;
            i3 = 0;
            j = 0;
            j2 = 1000;
        } else {
            if (!MiuiPhoneUtils.isDefaultDataSlotId(i) || cellLocation == null || cellLocation.isEmpty() || !this.mIsMobileDataOn) {
                return;
            }
            int i7 = this.dataType;
            if ((i7 != 2 && i7 != 4) || (phone2 = MiuiPhoneUtils.getPhone(i)) == null || phone2.getCurrentCellIdentity() == null) {
                return;
            }
            CellIdentity currentCellIdentity = phone2.getCurrentCellIdentity();
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (currentCellIdentity instanceof CellIdentityLte) {
                CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
                pci = cellIdentityLte.getPci();
                nrarfcn2 = cellIdentityLte.getEarfcn();
                j4 = 1;
            } else if (currentCellIdentity instanceof CellIdentityNr) {
                CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
                pci = cellIdentityNr.getPci();
                nrarfcn2 = cellIdentityNr.getNrarfcn();
                j4 = 2;
            } else {
                log("Stream cell type not supported, return");
                j4 = 0;
                nrarfcn2 = 0;
                pci = 0;
            }
            j2 = 1000;
            logV("processCellLocationChanged pci:" + this.oldStreamPci + "->" + pci + ",freq:" + this.oldStreamFreq + "->" + nrarfcn2 + ",rat:" + this.oldStreamRat + "->" + j4 + ",isResident:" + this.isResidentCell + ",time:" + this.mLastStreamMediaCellTime);
            int i8 = this.oldStreamPci;
            if (!(pci == i8 && nrarfcn2 == this.oldStreamFreq) && ((i5 = this.dataType) == 4 || i5 == 2)) {
                long j5 = this.mLastStreamMediaCellTime;
                if (j5 != 0) {
                    i6 = pci;
                    long j6 = this.oldStreamRat;
                    if (j6 != 0 && this.isResidentCell) {
                        long j7 = (jCurrentTimeMillis - j5) / 1000;
                        nrarfcn2 = nrarfcn2;
                        j = 0;
                        addOrUpdateStreamMediaCellInfo(this.oldStreamFreq, i8, j6, j7);
                        OffsetCell offsetCell = this.oldStreamOffSetCell;
                        if (offsetCell != null && j7 > 0) {
                            updateLiveStreamingOffsetCellScore(offsetCell, (this.mStallDurTime * 100) / j7);
                            this.mStallDurTime = 0L;
                        }
                        this.mLastStreamMediaCellTime = jCurrentTimeMillis;
                        log("processCellLocationChanged Stream dur = " + j7 + " , oldStreamOffSetCell is " + this.oldStreamOffSetCell);
                    }
                    currentCellInfoV3 = getCurrentCellInfoV3();
                    if (currentCellInfoV3 != null) {
                        String str = currentCellInfoV3.globalCellId;
                        rsrpFormStreamDataTableByGciAndType = this.dbHelper.getRsrpFormStreamDataTableByGciAndType(str, this.dataType, this.mIsWorkDay ? 1 : 0, !currentIsNonPeak() ? 1 : 0, this.mIsResidentCell);
                        this.mActionForStream = this.dbHelper.getActionFromStreamMediaByGciAndType(str, this.dataType);
                        if (rsrpFormStreamDataTableByGciAndType != Integer.MAX_VALUE) {
                            sendSelfStudyRequest(403, 3, new int[]{nrarfcn2, i6, rsrpFormStreamDataTableByGciAndType});
                            recordCount("streamMediaUseDataCnt", "streamMedia");
                        }
                    }
                    i2 = i;
                    processCellChangeInHomeScene(i2);
                    doActionForManualCellMap();
                    doActionForCellMap();
                } else {
                    i6 = pci;
                }
                j = 0;
                currentCellInfoV3 = getCurrentCellInfoV3();
                if (currentCellInfoV3 != null) {
                    String str2 = currentCellInfoV3.globalCellId;
                    rsrpFormStreamDataTableByGciAndType = this.dbHelper.getRsrpFormStreamDataTableByGciAndType(str2, this.dataType, this.mIsWorkDay ? 1 : 0, !currentIsNonPeak() ? 1 : 0, this.mIsResidentCell);
                    this.mActionForStream = this.dbHelper.getActionFromStreamMediaByGciAndType(str2, this.dataType);
                    if (rsrpFormStreamDataTableByGciAndType != Integer.MAX_VALUE) {
                        sendSelfStudyRequest(403, 3, new int[]{nrarfcn2, i6, rsrpFormStreamDataTableByGciAndType});
                        recordCount("streamMediaUseDataCnt", "streamMedia");
                    }
                }
                i2 = i;
                processCellChangeInHomeScene(i2);
                doActionForManualCellMap();
                doActionForCellMap();
            } else {
                nrarfcn2 = nrarfcn2;
                i6 = pci;
                j = 0;
                i2 = i;
            }
            this.oldStreamRat = j4;
            this.oldStreamFreq = nrarfcn2;
            this.oldStreamPci = i6;
            this.isResidentCell = isCurrentCellResident();
            processSceneTypeChange("cellChange", isResidentCellScene());
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone != null) {
                this.oldStreamOffSetCell = getCurrentCellInfo(activeDataPhone);
            }
            if (this.mIsModForDouYin) {
                this.mIsNeedResetDl = true;
                logV("reset dataStall for cellChange.");
                i3 = 0;
                beginStreamDataStall(this.dataType, 0);
            } else {
                i3 = 0;
            }
            if (this.mIsModForDouYinForLongTime) {
                this.mIsNeedResetDlForLongTime = true;
                logV("reset long dataStall for cellChange.");
                beginStreamDataStall(this.dataType, 2);
            }
            if (this.mWorkHandler.hasMessages(1053)) {
                logV("remove long stall event for cellChange.");
                this.mWorkHandler.removeMessages(1053);
            }
            if (this.mIsStreamStall) {
                logV("send log stall event for cellChange.");
                Handler handler = this.mWorkHandler;
                handler.sendMessageDelayed(handler.obtainMessage(1053), 10000L);
            }
        }
        if (!MiuiPhoneUtils.isDefaultDataSlotId(i2) || cellLocation == null || cellLocation.isEmpty() || !shouldDoActionForWechatOpt() || !this.isVideoFront || (phone = MiuiPhoneUtils.getPhone(i2)) == null || phone.getCurrentCellIdentity() == null) {
            return;
        }
        CellIdentity currentCellIdentity2 = phone.getCurrentCellIdentity();
        long jCurrentTimeMillis2 = System.currentTimeMillis();
        if (currentCellIdentity2 instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte2 = (CellIdentityLte) currentCellIdentity2;
            int pci2 = cellIdentityLte2.getPci();
            nrarfcn = cellIdentityLte2.getEarfcn();
            i4 = pci2;
            j3 = 1;
        } else if (currentCellIdentity2 instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr2 = (CellIdentityNr) currentCellIdentity2;
            int pci3 = cellIdentityNr2.getPci();
            nrarfcn = cellIdentityNr2.getNrarfcn();
            i4 = pci3;
            j3 = 2;
        } else {
            log("cell type not supported, return");
            i4 = i3;
            nrarfcn = i4;
            j3 = j;
        }
        log("processCellLocationChanged new pci = " + i4 + " new freq =  " + nrarfcn + " new rat = " + j3);
        int i9 = this.oldPci;
        if (i4 == i9 && nrarfcn == this.oldFreq) {
            return;
        }
        long j8 = this.mlastCellTime;
        if (j8 != j) {
            long j9 = this.oldRat;
            if (j9 != j) {
                long j10 = (jCurrentTimeMillis2 - j8) / j2;
                addOrUpdateWechatStallDur(this.oldFreq, i9, j9, j10);
                log("processCellLocationChanged dur = " + j10);
            }
        }
        this.isCellChanged = true;
        this.mlastCellTime = jCurrentTimeMillis2;
        this.oldRat = j3;
        this.oldFreq = nrarfcn;
        this.oldPci = i4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableSaForWechat(boolean z) {
        log("enableSaForWechat  mWechatVoipOptimizationRst = " + this.mWechatVideoOptimizationRst + " enable = " + z);
        if (this.mWechatVideoOptimizationRst != z) {
            log("enableSaForWechat");
            ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).enableSaForWechat(z);
            this.mWechatVideoOptimizationRst = z;
        }
    }

    private void enableSaForStreamMedia(boolean z) {
        logV("enableSaForStreamMedia  mStreamMediaOptimizationRst = " + this.mStreamMediaOptimizationRst + " enable = " + z);
        if (this.mStreamMediaOptimizationRst != z) {
            ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).enableSaForStreamMedia(z);
            this.mStreamMediaOptimizationRst = z;
            recordCount(z ? "streamMediaUpRatCnt" : "streamMediaDownRatCnt", "streamMedia");
            this.isVideoOpt = true;
            handleHomeSceneLogic();
            logV("enableSaForStreamMedia done.");
        }
    }

    private void handleHomeSceneLogic() {
        CellInfo currentCellInfoV3;
        logV("handleHomeSceneLogic  mNotifySceneType:" + this.mNotifySceneType + " ,mStreamMediaOptimizationRst:" + this.mStreamMediaOptimizationRst + " ,mIsNeedRecordForInHome:" + this.mIsNeedRecordForInHome);
        if (this.mNotifySceneType == 3 && !this.mStreamMediaOptimizationRst && !this.mIsNeedRecordForInHome) {
            this.mIsNeedRecordForInHome = true;
            return;
        }
        if (this.mIsNeedRecordForInHome) {
            this.mIsNeedRecordForInHome = false;
            return;
        }
        if (!this.mStreamMediaOptimizationRst || (currentCellInfoV3 = getCurrentCellInfoV3()) == null) {
            return;
        }
        String str = currentCellInfoV3.globalCellId;
        if (isMatchedHomeStreamMediaBadCell(str)) {
            deleteHomeStreamMediaBadCell(str);
            this.mLastDownRatTimeForHome = 0L;
        }
    }

    private void processCallStateForStream(int i, int i2) {
        int i3 = this.dataType;
        if ((i3 != 2 && i3 != 4) || !this.mIsMobileDataOn) {
            log("processCallStateChange not in the stream scene");
            return;
        }
        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            if (!this.mIsDuringCall && MiuiPhoneUtils.isDuringCall()) {
                log("processCallStateChange process inCall");
                this.mIsDuringCall = true;
                stopToCollectCellData(this.mIsWorkDay, this.mCollectActionType);
                removeMsgForExitSceneBreak();
                return;
            }
            if (!this.mIsDuringCall || MiuiPhoneUtils.isDuringCall()) {
                return;
            }
            log("processCallStateChange process Idle");
            this.mIsDuringCall = false;
        }
    }

    private void processSimStateChangeForStream(int i) {
        if (i == 1) {
            if (this.mWorkHandler.hasMessages(1023)) {
                this.mWorkHandler.removeMessages(1023);
            }
            this.mLastDataType = this.dataType;
            processStopDataStall();
            stopToCollectCellData(this.mIsWorkDay, this.mCollectActionType);
            removeMsgForExitSceneBreak();
            if (this.mStreamMediaOptimizationRst) {
                return;
            }
            enableSaForStreamMedia(true);
        }
    }

    private void initRealTimeOptimization(int i) {
        if (this.mRealTimeOptimizationIsInit || i != 11) {
            return;
        }
        this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$initRealTimeOptimization$6();
            }
        }, MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initRealTimeOptimization$6() {
        sendSelfStudyRequest(100, 1, new int[]{SelfStudyControllerBase.mDefaultRealTimeOptimizationStatus});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initBaseModel(int i) {
        if (this.mBaseModelIsInit || i != 11) {
            return;
        }
        this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$initBaseModel$7();
            }
        }, MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initBaseModel$7() {
        sendSelfStudyRequest(400, 1, new int[]{0});
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void processWechatEnter() {
        if (this.mIsProcessedWechatEnter) {
            return;
        }
        if (this.VDBG) {
            this.studyBadCellDays = 3;
        }
        this.enterWechatVideoDays = this.mPreferenceUtils.getInt("wechat_enter_days", 0);
        String string = this.mPreferenceUtils.getString("enter_wechat_yesterday_stats", "");
        if (!string.isEmpty()) {
            try {
                this.mYesterdayStats = (ArrayList) ObjectSerializer.deserialize(string);
            } catch (Exception e) {
                Utils.logD("MtkSelfStudyController", "get yesterday data stats exception:" + e);
            }
        }
        long j = this.mPreferenceUtils.getLong("last_enter_wechat_times", 0L);
        if ((j > 0 && getCurrentDay(System.currentTimeMillis()) - getCurrentDay(j) >= 1) || this.VDBG) {
            reportWechatVideoOptStatistic();
            int i = this.enterWechatVideoDays + 1;
            this.enterWechatVideoDays = i;
            this.mPreferenceUtils.putInt("wechat_enter_days", i);
            if (this.enterWechatVideoDays >= this.studyBadCellDays + 2) {
                periodicUpdateWechatBadCellInfo();
                if (this.mYesterdayStats.size() > 0) {
                    updateHistoryWechatData(this.mYesterdayStats);
                }
                this.mYesterdayStats.clear();
                try {
                    this.mPreferenceUtils.putString("enter_wechat_yesterday_stats", ObjectSerializer.serialize(this.mYesterdayStats));
                } catch (Exception e2) {
                    Utils.logD("MtkSelfStudyController", "set yesterday data stats exception:" + e2);
                }
            }
        }
        log("processWechatEnter VDBG = " + this.VDBG + " enterWechatVideoDays =  " + this.enterWechatVideoDays);
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        Phone phone = MiuiPhoneUtils.getPhone(activeDataPhoneId);
        if (phone == null) {
            return;
        }
        this.mServiceState[activeDataPhoneId] = phone.getServiceState();
        if (this.enterWechatVideoDays > this.studyBadCellDays) {
            queryWechatBadCellInfo();
        }
        getCurrentCellInfoForWechat();
    }

    protected void startCollectWechatData(int i) {
        sendAtCmd(i, MtkAtCmd.NOTIFY_MD_COLLECT_DATA, (Message) null);
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void getMdWechatStallInfo() {
        sendAtCmd(MiuiPhoneUtils.getActiveDataPhoneId(), MtkAtCmd.GET_MD_DATA, this.mWorkHandler.obtainMessage(3001));
    }

    private void getCurrentCellInfoForWechat() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            log("getCurrentCellInfoForWechat dataPhone is null");
            return;
        }
        CellIdentity currentCellIdentity = activeDataPhone.getCurrentCellIdentity();
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            this.oldPci = cellIdentityLte.getPci();
            this.oldFreq = cellIdentityLte.getEarfcn();
            this.oldRat = 1L;
        } else if (currentCellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
            this.oldPci = cellIdentityNr.getPci();
            this.oldFreq = cellIdentityNr.getNrarfcn();
            this.oldRat = 2L;
        } else {
            log("cell type not supported, return");
            return;
        }
        log("getCurrentCellInfoForWechat old pci = " + this.oldPci + " old freq = " + this.oldFreq + " old rat = " + this.oldRat);
        Pair pair = new Pair(Integer.valueOf(this.oldFreq), Integer.valueOf(this.oldPci));
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("count", 0L);
        concurrentHashMap.put("duration", 0L);
        concurrentHashMap.put("rat", Long.valueOf(this.oldRat));
        this.mlastCellTime = System.currentTimeMillis();
        this.mCurrentCellStats.clear();
        this.mCurrentCellStats.put(pair, concurrentHashMap);
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void delayProcessWechatEnter() {
        if (this.mIsMobileDataOn) {
            int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
            startCollectWechatData(activeDataPhoneId);
            if (shouldDoActionForWechatOpt() && !this.mIsProcessedWechatEnter && this.isVideoFront) {
                this.mIsProcessedWechatEnter = true;
                sendAtCmd(activeDataPhoneId, MtkAtCmd.SET_DISCARD_TIMER1, (Message) null);
                sendAtCmd(activeDataPhoneId, MtkAtCmd.SET_DISCARD_TIMER2, (Message) null);
                sendAtCmd(activeDataPhoneId, MtkAtCmd.SET_A2_TIMER_EGMC, (Message) null);
                sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_54G_INDOOR_MODE, (Message) null);
                sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_ELEVATOR_MODE, (Message) null);
                if (this.enterWechatVideoDays > this.studyBadCellDays) {
                    sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_AFR_TO_SA, (Message) null);
                    if (this.mNrCellStats.size() > 0) {
                        setOrResetWechatOffset(this.mNrCellStats, 2, false);
                    }
                    if (this.mLteCellStats.size() > 0) {
                        setOrResetWechatOffset(this.mLteCellStats, 1, false);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOrResetWechatOffset(ConcurrentHashMap concurrentHashMap, int i, boolean z) {
        MtkSelfStudyController mtkSelfStudyController;
        log("setWechatOffset rat = " + i);
        for (Pair pair : concurrentHashMap.keySet()) {
            int iIntValue = ((Integer) pair.first).intValue();
            int iIntValue2 = ((Integer) pair.second).intValue();
            int iIntValue3 = ((Integer) concurrentHashMap.get(pair)).intValue();
            if (z) {
                mtkSelfStudyController = this;
                mtkSelfStudyController.resetCellActionForWechat(iIntValue2, iIntValue, i, iIntValue3);
            } else {
                mtkSelfStudyController = this;
                mtkSelfStudyController.doCellActionForWechat(iIntValue2, iIntValue, i, iIntValue3);
            }
            this = mtkSelfStudyController;
        }
    }

    public void doCellActionForWechat(int i, int i2, long j, int i3) {
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        String networkOperatorForPhone = this.tmManager.getNetworkOperatorForPhone(activeDataPhoneId);
        int i4 = j == 1 ? 1 : 0;
        log("doCellActionForWechat  pci = " + i + " freq = " + i2 + " offset = " + i3);
        if (TextUtils.isEmpty(networkOperatorForPhone)) {
            return;
        }
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).teleAction(2, TelephonyActionBase.ActionReason.WECHAT_OPT.value(), Integer.valueOf(activeDataPhoneId), String.valueOf(i3 * 4), Integer.valueOf(i4), networkOperatorForPhone, Integer.valueOf(i2), Integer.valueOf(i));
    }

    public void resetCellActionForWechat(int i, int i2, long j, int i3) {
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        String networkOperatorForPhone = this.tmManager.getNetworkOperatorForPhone(activeDataPhoneId);
        log("resetCellAction pci = " + i + " freq = " + i2 + " offset = " + i3);
        if (TextUtils.isEmpty(networkOperatorForPhone)) {
            return;
        }
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).revertTeleAction(2, TelephonyActionBase.ActionReason.WECHAT_OPT.value(), Integer.valueOf(activeDataPhoneId), String.valueOf(i3 * 4), Integer.valueOf(j == 1 ? 1 : 0), networkOperatorForPhone, Integer.valueOf(i2), Integer.valueOf(i));
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void processWechatExit() {
        MtkSelfStudyController mtkSelfStudyController;
        if (!this.mIsProcessedWechatEnter || this.mWechatOpt == null) {
            return;
        }
        unRegisterTelephony();
        this.mWechatOpt.tryRecovery();
        this.mIsProcessedWechatEnter = false;
        this.isLte = false;
        this.isCellChanged = false;
        if (this.mlastCellTime != 0) {
            long jCurrentTimeMillis = (System.currentTimeMillis() - this.mlastCellTime) / 1000;
            log("processWechatExit dur = " + jCurrentTimeMillis);
            mtkSelfStudyController = this;
            mtkSelfStudyController.addOrUpdateWechatStallDur(this.oldFreq, this.oldPci, this.oldRat, jCurrentTimeMillis);
            mtkSelfStudyController.mlastCellTime = 0L;
        } else {
            mtkSelfStudyController = this;
        }
        long jCurrentTimeMillis2 = System.currentTimeMillis();
        mtkSelfStudyController.mPreferenceUtils.putLong("last_enter_wechat_times", jCurrentTimeMillis2);
        int i = mtkSelfStudyController.enterWechatVideoDays;
        int i2 = mtkSelfStudyController.studyBadCellDays;
        ConcurrentHashMap concurrentHashMap = mtkSelfStudyController.mCurrentCellStats;
        if (i <= i2) {
            mtkSelfStudyController.studyWechatBadCellInfo(concurrentHashMap, jCurrentTimeMillis2);
        } else {
            mtkSelfStudyController.storeHistoryWechatBadCellInfo(concurrentHashMap, jCurrentTimeMillis2);
        }
        mtkSelfStudyController.mCurrentCellStats.clear();
        mtkSelfStudyController.mLteCellStats.clear();
        mtkSelfStudyController.mNrCellStats.clear();
        mtkSelfStudyController.mYesterdayStats.clear();
    }

    private void storeHistoryWechatBadCellInfo(ConcurrentHashMap concurrentHashMap, long j) {
        ConcurrentHashMap concurrentHashMap2 = concurrentHashMap;
        log("storeHistoryWechatBadCellInfo + mYesterdayStats = " + this.mYesterdayStats + " mCurrentStats = " + concurrentHashMap2);
        if (concurrentHashMap2.size() > 0) {
            Iterator it = concurrentHashMap2.keySet().iterator();
            while (it.hasNext()) {
                Pair pair = (Pair) it.next();
                ConcurrentHashMap concurrentHashMap3 = (ConcurrentHashMap) concurrentHashMap2.get(pair);
                Integer num = (Integer) pair.first;
                int iIntValue = num.intValue();
                Integer num2 = (Integer) pair.second;
                int iIntValue2 = num2.intValue();
                Long l = (Long) concurrentHashMap3.get("rat");
                l.longValue();
                Long l2 = (Long) concurrentHashMap3.get("count");
                long jLongValue = l2.longValue();
                Long l3 = (Long) concurrentHashMap3.get("duration");
                long jLongValue2 = l3.longValue();
                if (jLongValue2 == 0) {
                    concurrentHashMap2 = concurrentHashMap;
                } else if (jLongValue != 0) {
                    ArrayList arrayList = this.mYesterdayStats;
                    Iterator it2 = it;
                    int size = arrayList.size();
                    boolean z = false;
                    int i = 0;
                    while (true) {
                        long j2 = jLongValue2;
                        if (i >= size) {
                            break;
                        }
                        Object obj = arrayList.get(i);
                        i++;
                        ArrayList arrayList2 = arrayList;
                        ConcurrentHashMap concurrentHashMap4 = (ConcurrentHashMap) obj;
                        if (iIntValue == ((Integer) concurrentHashMap4.get("freq")).intValue() && iIntValue2 == ((Integer) concurrentHashMap4.get("pci")).intValue()) {
                            long jLongValue3 = ((Long) concurrentHashMap4.get("count")).longValue();
                            long jLongValue4 = ((Long) concurrentHashMap4.get("duration")).longValue();
                            concurrentHashMap4.put("count", Long.valueOf(jLongValue3 + jLongValue));
                            concurrentHashMap4.put("duration", Long.valueOf(jLongValue4 + j2));
                            z = true;
                        }
                        jLongValue2 = j2;
                        arrayList = arrayList2;
                    }
                    if (!z) {
                        ConcurrentHashMap concurrentHashMap5 = new ConcurrentHashMap();
                        concurrentHashMap5.put("freq", num);
                        concurrentHashMap5.put("pci", num2);
                        concurrentHashMap5.put("rat", l);
                        concurrentHashMap5.put("count", l2);
                        concurrentHashMap5.put("duration", l3);
                        concurrentHashMap5.put("updateTime", Long.valueOf(j));
                        this.mYesterdayStats.add(concurrentHashMap5);
                    }
                    concurrentHashMap2 = concurrentHashMap;
                    it = it2;
                }
            }
        }
        log("storeHistoryWechatBadCellInfo: update mYesterdayStats = " + this.mYesterdayStats);
        try {
            this.mPreferenceUtils.putString("enter_wechat_yesterday_stats", ObjectSerializer.serialize(this.mYesterdayStats));
        } catch (Exception e) {
            Utils.logE("MtkSelfStudyController", "storeHistoryWechatBadCellInfo exception:" + e);
        }
    }

    private void studyWechatBadCellInfo(ConcurrentHashMap concurrentHashMap, long j) {
        MtkSelfStudyController mtkSelfStudyController = this;
        mtkSelfStudyController.log("studyWechatBadCellInfo map = " + concurrentHashMap);
        if (concurrentHashMap.size() > 0) {
            for (Pair pair : concurrentHashMap.keySet()) {
                ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) concurrentHashMap.get(pair);
                long jLongValue = ((Long) concurrentHashMap2.get("rat")).longValue();
                int iIntValue = ((Integer) pair.first).intValue();
                int iIntValue2 = ((Integer) pair.second).intValue();
                long jLongValue2 = ((Long) concurrentHashMap2.get("count")).longValue();
                long jLongValue3 = ((Long) concurrentHashMap2.get("duration")).longValue();
                if (jLongValue2 != 0) {
                    if (jLongValue3 != 0) {
                        mtkSelfStudyController.insertOrUpdateWechatDatabase(jLongValue, iIntValue, iIntValue2, jLongValue2, jLongValue3, j);
                    }
                }
                mtkSelfStudyController = this;
            }
        }
    }

    public void queryWechatBadCellInfo() {
        Cursor cursorQuery = this.dbHelper.query("wechat_bad_cell_table", new String[]{"freq", "pci", "rat"}, null, null, null, null, "average_data_stall_count");
        int count = cursorQuery.getCount();
        if (count <= 0) {
            return;
        }
        log("queryWechatBadCellInfo rat =  count = " + count);
        int i = 1;
        if (count >= 3) {
            int i2 = count / 3;
            i = count % 3 > 1 ? i2 + 1 : i2;
        }
        getWechatOffset(cursorQuery, i);
    }

    private void getWechatOffset(Cursor cursor, int i) {
        ConcurrentHashMap concurrentHashMap;
        log("getWechatBadCellInfo stageCount = " + i);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int i2 = 0;
                while (!cursor.isAfterLast()) {
                    int i3 = cursor.getInt(0);
                    int i4 = cursor.getInt(1);
                    int i5 = cursor.getInt(2);
                    log("getWechatBadCellInfo freq = " + i3 + "pci = " + i4 + "rat = " + i5);
                    Pair pair = new Pair(Integer.valueOf(i3), Integer.valueOf(i4));
                    if (i5 == 1) {
                        concurrentHashMap = this.mLteCellStats;
                    } else {
                        concurrentHashMap = this.mNrCellStats;
                    }
                    if (i2 < i) {
                        concurrentHashMap.put(pair, Integer.valueOf(this.stageA3));
                    } else if (i2 < i * 2) {
                        concurrentHashMap.put(pair, Integer.valueOf(this.stageA4));
                    } else {
                        concurrentHashMap.put(pair, Integer.valueOf(this.stageA5));
                    }
                    i2++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
    }

    private void periodicUpdateWechatBadCellInfo() {
        Cursor cursorQuery = this.dbHelper.query("wechat_bad_cell_table", new String[]{"id", "data_stall_count", "data_stall_duration"}, null, null, null, null, null);
        log("periodicUpdateWechatBadCellInfo count =  " + cursorQuery.getCount());
        cursorQuery.moveToFirst();
        while (!cursorQuery.isAfterLast()) {
            int i = cursorQuery.getInt(0);
            float f = cursorQuery.getFloat(1);
            this.dbHelper.periodicUpdateWechatBadCell((f * 6.0f) / 7.0f, (cursorQuery.getFloat(2) * 6.0f) / 7.0f, i);
            cursorQuery.moveToNext();
        }
        cursorQuery.close();
    }

    private void updateHistoryWechatData(ArrayList arrayList) {
        MtkSelfStudyController mtkSelfStudyController = this;
        mtkSelfStudyController.log("updateHistoryWechatData oldList  " + arrayList);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) arrayList.get(i);
            mtkSelfStudyController.insertOrUpdateWechatDatabase(((Long) concurrentHashMap.get("rat")).longValue(), ((Integer) concurrentHashMap.get("freq")).intValue(), ((Integer) concurrentHashMap.get("pci")).intValue(), ((Long) concurrentHashMap.get("count")).longValue(), ((Long) concurrentHashMap.get("duration")).longValue(), ((Long) concurrentHashMap.get("updateTime")).longValue());
            mtkSelfStudyController = this;
        }
    }

    private void insertOrUpdateWechatDatabase(long j, int i, int i2, long j2, long j3, long j4) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        try {
            Cursor wechatBadCellByPciAndFreq = this.dbHelper.getWechatBadCellByPciAndFreq(j, i, i2);
            try {
                log("getWechatBadCellByPciAndFreq count = " + wechatBadCellByPciAndFreq.getCount());
                if (wechatBadCellByPciAndFreq.moveToFirst()) {
                    int i3 = wechatBadCellByPciAndFreq.getInt(0);
                    float f = wechatBadCellByPciAndFreq.getFloat(1);
                    float f2 = wechatBadCellByPciAndFreq.getFloat(2);
                    log("getWechatBadCellByPciAndFreq oldCnt = " + f + " olDur = " + f2);
                    float f3 = f + ((float) j2);
                    float f4 = f2 + ((float) j3);
                    this.dbHelper.updateWechatBadCell(f3, f4, j4, f3 / f4, i3);
                    log("insertOrUpdateWechatBadCell pci= " + i2 + "freq = " + i + "newCnt = " + f3 + "newDur" + f4);
                } else {
                    CellInfo cellInfo = new CellInfo();
                    cellInfo.freq = i;
                    cellInfo.pci = i2;
                    cellInfo.wechatDataStallDur = j3;
                    cellInfo.wechatDataStallCount = j2;
                    cellInfo.updateTimeMills = j4;
                    if (j == 1) {
                        arrayList.add(cellInfo);
                    } else {
                        arrayList2.add(cellInfo);
                    }
                }
                this.dbHelper.insertWechatBadCell(arrayList2, 2, this.maxSavedCells);
                this.dbHelper.insertWechatBadCell(arrayList, 1, this.maxSavedCells);
                wechatBadCellByPciAndFreq.close();
            } catch (Throwable th) {
                if (wechatBadCellByPciAndFreq == null) {
                    throw th;
                }
                try {
                    wechatBadCellByPciAndFreq.close();
                    throw th;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                    throw th;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recordActionCount(String str, ConcurrentHashMap concurrentHashMap) {
        if (concurrentHashMap == null) {
            return;
        }
        concurrentHashMap.put(str, Integer.valueOf((concurrentHashMap.get(str) != null ? ((Integer) concurrentHashMap.get(str)).intValue() : 0) + 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordWechatAction(int i) {
        this.mWorkHandler.removeMessages(3007);
        this.mWorkHandler.removeMessages(3008);
        this.mWorkHandler.removeMessages(3009);
        this.mWorkHandler.removeMessages(3010);
        if (this.isCellChanged) {
            this.isCellChanged = false;
        }
        if (i == 1) {
            recordActionCount("action1Cnt", this.mWechatOptStats);
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(3007), 10000L);
            return;
        }
        if (i == 2) {
            recordActionCount("action2Cnt", this.mWechatOptStats);
            Handler handler2 = this.mWorkHandler;
            handler2.sendMessageDelayed(handler2.obtainMessage(3008), 10000L);
        } else if (i == 3) {
            recordActionCount("action3Cnt", this.mWechatOptStats);
            Handler handler3 = this.mWorkHandler;
            handler3.sendMessageDelayed(handler3.obtainMessage(3009), 10000L);
        } else {
            if (i != 4) {
                return;
            }
            recordActionCount("action4Cnt", this.mWechatOptStats);
            Handler handler4 = this.mWorkHandler;
            handler4.sendMessageDelayed(handler4.obtainMessage(3010), 10000L);
        }
    }

    private void reportWechatVideoOptStatistic() {
        ArrayList arrayList;
        ConcurrentHashMap concurrentHashMap;
        float f;
        int i = this.enterWechatVideoDays;
        int i2 = this.studyBadCellDays;
        int i3 = 0;
        if (i == i2) {
            Cursor cursorQuery = this.dbHelper.query("wechat_bad_cell_table", new String[]{"data_stall_count", "data_stall_duration"}, null, null, null, null, null);
            HashMap map = new HashMap();
            float f2 = 0.0f;
            if (cursorQuery != null) {
                cursorQuery.moveToFirst();
                f = 0.0f;
                while (!cursorQuery.isAfterLast()) {
                    f2 += cursorQuery.getFloat(0);
                    f += cursorQuery.getFloat(1);
                    cursorQuery.moveToNext();
                }
                cursorQuery.close();
            } else {
                f = 0.0f;
            }
            map.put("stallCnt", Float.valueOf(f2));
            map.put("stallDur", Float.valueOf(f));
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "before_mtk_wechat_video_optimization", map);
            return;
        }
        if (i <= i2 || (arrayList = this.mYesterdayStats) == null || arrayList.size() <= 0 || (concurrentHashMap = this.mWechatOptStats) == null || concurrentHashMap.size() <= 0) {
            return;
        }
        ArrayList arrayList2 = this.mYesterdayStats;
        int size = arrayList2.size();
        long jLongValue = 0;
        long jLongValue2 = 0;
        while (i3 < size) {
            Object obj = arrayList2.get(i3);
            i3++;
            ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) obj;
            jLongValue += ((Long) concurrentHashMap2.get("count")).longValue();
            jLongValue2 += ((Long) concurrentHashMap2.get("duration")).longValue();
        }
        this.mWechatOptStats.put("stallCnt", Long.valueOf(jLongValue));
        this.mWechatOptStats.put("stallDur", Long.valueOf(jLongValue2));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "after_mtk_wechat_video_optimization", new HashMap(this.mWechatOptStats));
        this.mWechatOptStats.clear();
    }

    private void analyzeAndRestoreResidentCells(String[] strArr, SQLiteDatabase sQLiteDatabase) {
        int i;
        String[] strArr2 = strArr;
        if (strArr2.length != 99) {
            log("error residentCells  At cmd");
            return;
        }
        int i2 = 1;
        int i3 = Integer.parseInt(strArr2[1].trim());
        if (MiuiPhoneUtils.isValidatePhoneId(i3)) {
            int subId = MiuiPhoneUtils.getPhone(i3).getSubId();
            int i4 = Integer.parseInt(strArr2[2].trim());
            ArrayList arrayList = new ArrayList();
            boolean z = true;
            int i5 = 0;
            while (i5 < 8) {
                StringBuilder sb = new StringBuilder();
                String strTrim = strArr2[i5 + 3].trim();
                String strTrim2 = strArr2[i5 + 11].trim();
                StringBuilder sb2 = new StringBuilder();
                if (strTrim2.length() == i2) {
                    sb2.append('0');
                }
                sb2.append(strTrim2);
                sb.append(i4);
                sb.append("_");
                sb.append(subId);
                sb.append("_");
                sb.append(strTrim);
                sb.append((CharSequence) sb2);
                sb.append("_");
                sb.append(strArr2[i5 + 19].trim());
                int i6 = Integer.parseInt(strArr2[i5 + 27].trim());
                int i7 = Integer.parseInt(strArr2[i5 + 35].trim());
                long j = Long.parseLong(strArr2[i5 + 43].trim());
                if (i7 == 0 || j < 30) {
                    i = i5;
                } else {
                    int i8 = (i5 < 4 ? SelfStudyControllerBase.Rat.Lte : SelfStudyControllerBase.Rat.Nr).value;
                    int i9 = Integer.parseInt(strArr2[i5 + 51].trim());
                    int i10 = Integer.parseInt(strArr2[i5 + 59].trim());
                    int i11 = Integer.parseInt(strArr2[i5 + 67].trim());
                    int i12 = Integer.parseInt(strArr2[i5 + 75].trim());
                    int i13 = Integer.parseInt(strArr2[i5 + 83].trim());
                    int i14 = Integer.parseInt(strArr2[i5 + 91].trim());
                    arrayList.add(sb.toString());
                    i = i5;
                    if (this.dbHelper.insertOrUpdateResidentCells(sQLiteDatabase, strTrim + ((Object) sb2), sb.toString(), i6, i7, i8, i4, subId, j, i9, i10, i11, i12, i13, i14)) {
                        z = false;
                    }
                }
                i5 = i + 1;
                strArr2 = strArr;
                i2 = 1;
            }
            if (z && DefaultSimManager.getDefaultDataSlotId() == i3) {
                for (int i15 = 0; i15 < MiuiPhoneUtils.PHONE_COUNT; i15++) {
                    this.dbHelper.deleteOldResidentCells(sQLiteDatabase, arrayList, i4, MiuiPhoneUtils.getPhone(i15).getSubId());
                }
                this.mPreferenceUtils.putInt("study_resident_cells_days", 0);
                this.mPreferenceUtils.putBoolean("stat_benefit_ratio", false);
            }
        }
    }

    private void analysisAndRestoreBadCellsV2(String[] strArr) {
        int i;
        if (strArr.length != 131) {
            log("error badCells  At cmd");
            return;
        }
        int i2 = Integer.parseInt(strArr[2].trim());
        ArrayList arrayList = new ArrayList(32);
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = jCurrentTimeMillis - 600000;
        for (int i3 = 3; i3 < 35 && (i = Integer.parseInt(strArr[i3].trim())) != 0; i3++) {
            CellInfo cellInfo = new CellInfo();
            cellInfo.pci = i;
            arrayList.add(cellInfo);
        }
        int size = arrayList.size();
        for (int i4 = 0; i4 < size; i4++) {
            CellInfo cellInfo2 = (CellInfo) arrayList.get(i4);
            cellInfo2.freq = Integer.parseInt(strArr[i4 + 35].trim());
            cellInfo2.isBadCell = Integer.parseInt(strArr[i4 + 67].trim());
            int i5 = Integer.parseInt(strArr[i4 + 99].trim());
            int i6 = i5 & 1;
            cellInfo2.isPeak = i6;
            cellInfo2.isWorkDay = (i5 >> 1) & 1;
            cellInfo2.updateTimeMills = i6 == 1 ? jCurrentTimeMillis : j;
        }
        this.dbHelper.inertOrUpdateBadCellTable(i2, arrayList);
    }

    private void analysisAndRestoreCellRouteV2(String str) {
        String[] strArrSplit = str.split(":")[1].split(",");
        if (Integer.parseInt(strArrSplit[0].trim()) == 20002 || Integer.parseInt(strArrSplit[1].trim()) == 11) {
            for (int i = 0; i < (strArrSplit.length - 3) / 15; i++) {
                int i2 = i * 5;
                int i3 = i2 + 3;
                if (Integer.parseInt(strArrSplit[i3].trim()) == 0) {
                    return;
                }
                ArrayList arrayList = new ArrayList();
                SelfStudyControllerBase.CellRouteInfo cellRouteInfo = new SelfStudyControllerBase.CellRouteInfo();
                cellRouteInfo.cellRat = Integer.parseInt(strArrSplit[i3].trim());
                cellRouteInfo.cellPci = Integer.parseInt(strArrSplit[i2 + 33].trim());
                cellRouteInfo.cellFreq = Integer.parseInt(strArrSplit[i2 + 63].trim());
                arrayList.add(cellRouteInfo);
                SelfStudyControllerBase.CellRouteInfo cellRouteInfo2 = new SelfStudyControllerBase.CellRouteInfo();
                cellRouteInfo2.cellRat = Integer.parseInt(strArrSplit[i2 + 4].trim());
                cellRouteInfo2.cellPci = Integer.parseInt(strArrSplit[i2 + 34].trim());
                cellRouteInfo2.cellFreq = Integer.parseInt(strArrSplit[i2 + 64].trim());
                arrayList.add(cellRouteInfo2);
                SelfStudyControllerBase.CellRouteInfo cellRouteInfo3 = new SelfStudyControllerBase.CellRouteInfo();
                cellRouteInfo3.cellRat = Integer.parseInt(strArrSplit[i2 + 5].trim());
                cellRouteInfo3.cellPci = Integer.parseInt(strArrSplit[i2 + 35].trim());
                cellRouteInfo3.cellFreq = Integer.parseInt(strArrSplit[i2 + 65].trim());
                arrayList.add(cellRouteInfo3);
                SelfStudyControllerBase.CellRouteInfo cellRouteInfo4 = new SelfStudyControllerBase.CellRouteInfo();
                cellRouteInfo4.cellRat = Integer.parseInt(strArrSplit[i2 + 6].trim());
                cellRouteInfo4.cellPci = Integer.parseInt(strArrSplit[i2 + 36].trim());
                cellRouteInfo4.cellFreq = Integer.parseInt(strArrSplit[i2 + 66].trim());
                arrayList.add(cellRouteInfo4);
                SelfStudyControllerBase.CellRouteInfo cellRouteInfo5 = new SelfStudyControllerBase.CellRouteInfo();
                cellRouteInfo5.cellRat = Integer.parseInt(strArrSplit[i2 + 7].trim());
                cellRouteInfo5.cellPci = Integer.parseInt(strArrSplit[i2 + 37].trim());
                cellRouteInfo5.cellFreq = Integer.parseInt(strArrSplit[i2 + 67].trim());
                arrayList.add(cellRouteInfo5);
                this.allCellRouteList.add(arrayList);
            }
        }
    }

    private List parserListCellInfos(String str) {
        String[] strArrSplit = str.split(":")[1].split(",");
        log("dataArray's length is " + strArrSplit.length);
        LinkedList linkedList = new LinkedList();
        if ((strArrSplit.length - 2) % 10 == 0) {
            for (int i = 0; i < (strArrSplit.length - 2) / 10; i++) {
                int i2 = i * 10;
                int i3 = i2 + 2;
                if (Integer.parseInt(strArrSplit[i3]) == 0) {
                    break;
                }
                CellInfo cellInfo = new CellInfo();
                cellInfo.setRat(Integer.parseInt(strArrSplit[i3]));
                cellInfo.setFreq(Integer.parseInt(strArrSplit[i2 + 3]));
                cellInfo.setPci(Integer.parseInt(strArrSplit[i2 + 4]));
                cellInfo.setRrcExceptionCount(Integer.parseInt(strArrSplit[i2 + 5]));
                cellInfo.setHighBlerCount(Integer.parseInt(strArrSplit[i2 + 6]));
                cellInfo.setLowUlGrantCount(Integer.parseInt(strArrSplit[i2 + 7]));
                cellInfo.setRlfCount(Integer.parseInt(strArrSplit[i2 + 8]));
                cellInfo.setDuration(Integer.parseInt(strArrSplit[i2 + 9]));
                cellInfo.setBadRsrpDuration(Integer.parseInt(strArrSplit[i2 + 10]));
                cellInfo.setLowDlGrantCount(Integer.parseInt(strArrSplit[i2 + 11]));
                cellInfo.setDataType(this.dataType);
                cellInfo.setIsBadCell(calculateBadCellPoints(cellInfo));
                linkedList.add(cellInfo);
            }
        }
        return linkedList;
    }

    private void analysisAndRestoreCellInfoList(String str) {
        restoreOrUpdate(parserListCellInfos(str));
    }

    private void updateBadCells(String str) {
        log("updateBadCells");
        List list = parserListCellInfos(str);
        for (int i = 0; i < list.size(); i++) {
            int i2 = 0;
            while (i2 < this.mBadCellListInfos.size()) {
                if (isSameCell((CellInfo) this.mBadCellListInfos.get(i2), (CellInfo) list.get(i))) {
                    ((CellInfo) this.mBadCellListInfos.get(i2)).rrcExceptionCount += ((CellInfo) list.get(i)).rrcExceptionCount;
                    ((CellInfo) this.mBadCellListInfos.get(i2)).highBlerCount += ((CellInfo) list.get(i)).highBlerCount;
                    ((CellInfo) this.mBadCellListInfos.get(i2)).lowDlGrantCount += ((CellInfo) list.get(i)).lowDlGrantCount;
                    ((CellInfo) this.mBadCellListInfos.get(i2)).lowUlGrantCount += ((CellInfo) list.get(i)).lowUlGrantCount;
                    ((CellInfo) this.mBadCellListInfos.get(i2)).rlfCount += ((CellInfo) list.get(i)).rlfCount;
                    ((CellInfo) this.mBadCellListInfos.get(i2)).duration += ((CellInfo) list.get(i)).duration;
                    ((CellInfo) this.mBadCellListInfos.get(i2)).badRsrpDuration += ((CellInfo) list.get(i)).badRsrpDuration;
                    ((CellInfo) this.mBadCellListInfos.get(i2)).setIsBadCell(calculateBadCellPoints((CellInfo) this.mBadCellListInfos.get(i2)));
                    break;
                }
                i2++;
            }
            if (i2 >= this.mBadCellListInfos.size()) {
                this.mBadCellListInfos.add((CellInfo) list.get(i));
            }
        }
    }

    private void analysisWechatStallInfo(String[] strArr) throws Throwable {
        int i = Integer.parseInt(strArr[1]);
        if (i != 317) {
            if (i == 32) {
                int i2 = Integer.parseInt(strArr[2]);
                int i3 = Integer.parseInt(strArr[3]);
                log("receive action A3 skips cell freq = " + i2 + " pci = " + i3);
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (this.dbHelper.isWechatOptActionA3SkipCell(i2, i3)) {
                    return;
                }
                this.dbHelper.updateWechatOpActionA3SkipCell(i2, i3, jCurrentTimeMillis);
                return;
            }
            return;
        }
        int i4 = Integer.parseInt(strArr[2]);
        long j = Long.parseLong(strArr[3]);
        int i5 = Integer.parseInt(strArr[4]);
        int i6 = Integer.parseInt(strArr[5]);
        int i7 = Integer.parseInt(strArr[6]);
        int i8 = Integer.parseInt(strArr[7]);
        int i9 = Integer.parseInt(strArr[8]);
        log("DATA_STALL_RESULT_INFO sub: evt:" + i + " source:" + i4 + " rat:" + j + " freq:" + i5 + " pci:" + i6 + " action:" + i7 + " offset:" + i8 + " b1Thr:" + i9);
        processWechatStallEvent(j, i5, i6, i7, i9, i8);
        addOrUpdateWechatStallCount(i5, i6, j);
    }

    private void handlePingpongCellInfo(String[] strArr, int i) {
        String str;
        String[] strArr2 = strArr;
        int i2 = 0;
        if (strArr2 == null || strArr2.length < 16) {
            StringBuilder sb = new StringBuilder("handlePingpongCellInfo: invalid data length=");
            sb.append(strArr != null ? strArr.length : 0);
            log(sb.toString());
            return;
        }
        try {
            int i3 = Integer.parseInt(strArr2[3].trim());
            while (i2 < 2) {
                int i4 = Integer.parseInt(strArr2[i2 + 4].trim());
                long j = Long.parseLong(strArr2[i2 + 6].trim());
                long j2 = Long.parseLong(strArr2[i2 + 8].trim());
                long j3 = Long.parseLong(strArr2[i2 + 10].trim());
                String strTrim = strArr2[i2 + 12].trim();
                int i5 = Integer.parseInt(strArr2[i2 + 14].trim());
                List arrayList = (List) this.mPingPongRecords.get(i);
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    this.mPingPongRecords.put(i, arrayList);
                }
                PingPongRecord pingPongRecord = new PingPongRecord();
                pingPongRecord.rat = i4;
                pingPongRecord.band = i5;
                pingPongRecord.arfcn = (int) j;
                pingPongRecord.pci = (int) j2;
                pingPongRecord.cellId = j3;
                pingPongRecord.plmn = strTrim;
                arrayList.add(pingPongRecord);
                if (i4 == 4) {
                    str = "LTE";
                } else {
                    str = i4 == 128 ? "NR" : "Unknown";
                }
                Log.d("MtkSelfStudyController", "PingpongCellInfo[subId=" + i + "][" + i2 + "]: version=" + i3 + ", rat=" + str + ", arfcn=" + j + ", pci=" + j2 + ", cellId=" + j3 + ", plmn=" + strTrim + ", band=" + i5);
                i2++;
                strArr2 = strArr;
            }
        } catch (NumberFormatException e) {
            Log.e("MtkSelfStudyController", "handlePingpongCellInfo: parse error", e);
        }
    }

    private boolean isPingpongCell(int i, String str) {
        List<PingPongRecord> list = (List) this.mPingPongRecords.get(i);
        if (list != null && !list.isEmpty()) {
            String[] strArrSplit = str.split("_");
            if (strArrSplit.length <= 3) {
                return false;
            }
            String str2 = strArrSplit[2];
            if (str2.length() < 3) {
                return false;
            }
            try {
                long j = Long.parseLong(strArrSplit[3]);
                for (PingPongRecord pingPongRecord : list) {
                    if (str2.equals(pingPongRecord.plmn) && pingPongRecord.cellId == j) {
                        return true;
                    }
                }
            } catch (NumberFormatException e) {
                Log.e("MtkSelfStudyController", "isPingpongCell parse error: ".concat(str), e);
            }
        }
        return false;
    }

    private void analysisModemBaseInfo(String[] strArr, int i) {
        if (!this.mInSleepMode || strArr == null || strArr.length < 3 || i < 0 || i >= MiuiPhoneUtils.PHONE_COUNT) {
            return;
        }
        try {
            if (Integer.parseInt(strArr[1]) == 112) {
                int i2 = Integer.parseInt(strArr[2]);
                int i3 = this.lowerPowerOpt[i];
                LowPowerState lowPowerState = LowPowerState.LOW_POWER_STATE_START;
                if (i3 == lowPowerState.value) {
                    LowPowerState lowPowerState2 = LowPowerState.LOW_POWER_STATE_SUSPEND;
                    if (i2 == lowPowerState2.value) {
                        if (resetLowPowerOption(i)) {
                            sendStateToModem(lowPowerState2.value, i);
                            this.lowerPowerOpt[i] = lowPowerState2.value;
                            return;
                        }
                        return;
                    }
                }
                if (this.lowerPowerOpt[i] == LowPowerState.LOW_POWER_STATE_SUSPEND.value && i2 == lowPowerState.value) {
                    if (takeLowPowerOption(i)) {
                        this.lowerPowerOpt[i] = lowPowerState.value;
                        sendStateToModem(lowPowerState.value, i);
                        return;
                    }
                    return;
                }
                LowPowerState lowPowerState3 = LowPowerState.LOW_POWER_STATE_STOP;
                if (i2 == lowPowerState3.value) {
                    if (resetLowPowerOption(i)) {
                        sendStateToModem(lowPowerState3.value, i);
                        this.lowerPowerOpt[i] = lowPowerState3.value;
                        return;
                    }
                    return;
                }
                if (i2 == LowPowerState.LOW_POWER_STATE_ERROR.value) {
                    Log.w("MtkSelfStudyController", "analysisModemBaseInfo received ERROR state for phoneId: " + i);
                }
            }
        } catch (NumberFormatException e) {
            Log.e("MtkSelfStudyController", "analysisModemBaseInfo parse error", e);
        }
    }

    private void processWechatStallEvent(long j, int i, int i2, int i3, int i4, int i5) {
        ConcurrentHashMap concurrentHashMap;
        int i6;
        if (shouldDoActionForWechatOpt() && this.isVideoFront && this.enterWechatVideoDays > this.studyBadCellDays && this.mWechatOpt != null && this.mIsScreenOn) {
            recordWechatAction(i3);
            Pair pair = new Pair(Integer.valueOf(i), Integer.valueOf(i2));
            log("processWechatStallEvent action = " + i3);
            if (i3 == 2 && j == 2) {
                WechatDdsSaOpt wechatDdsSaOpt = this.mWechatOpt;
                Objects.requireNonNull(wechatDdsSaOpt);
                wechatDdsSaOpt.updateActionPhase(2);
            } else if (i3 == 1) {
                if (j != 1 && j != 2) {
                    log("set offset but not 4 / 5 G , return");
                    return;
                }
                if (j == 1) {
                    concurrentHashMap = this.mLteCellStats;
                } else {
                    concurrentHashMap = this.mNrCellStats;
                }
                ConcurrentHashMap concurrentHashMap2 = concurrentHashMap;
                if (concurrentHashMap2 != null) {
                    if (concurrentHashMap2.containsKey(pair)) {
                        int iIntValue = ((Integer) concurrentHashMap2.get(pair)).intValue();
                        if (iIntValue == -6) {
                            log("processWechatStallEvent: total offset is -6, skip");
                            return;
                        } else {
                            int i7 = iIntValue + i5;
                            i6 = i7 < -6 ? -6 : i7;
                        }
                    } else {
                        i6 = i5;
                    }
                    log("processWechatStallEvent: newOffset = " + i6);
                    doCellActionForWechat(i2, i, j, i6);
                    concurrentHashMap2.put(pair, Integer.valueOf(i6));
                }
            } else if (i3 == 3) {
                String networkOperatorForPhone = this.tmManager.getNetworkOperatorForPhone(MiuiPhoneUtils.getActiveDataPhoneId());
                if ((networkOperatorForPhone != null && ArrayUtils.contains(this.mChinaCMCC, networkOperatorForPhone)) || this.dbHelper.isWechatOptActionA3SkipCell(i, i2)) {
                    log("processWechatStallEvent action A3 skip, plmn = " + networkOperatorForPhone + " isWechatOptimizationSkipCell = " + this.dbHelper.isWechatOptActionA3SkipCell(i, i2));
                } else {
                    WechatDdsSaOpt wechatDdsSaOpt2 = this.mWechatOpt;
                    Objects.requireNonNull(wechatDdsSaOpt2);
                    wechatDdsSaOpt2.updateActionPhase(3);
                }
            }
            if (!this.isLte || i4 <= -424 || this.wechatOptAction == -1) {
                return;
            }
            WechatDdsSaOpt wechatDdsSaOpt3 = this.mWechatOpt;
            Objects.requireNonNull(wechatDdsSaOpt3);
            wechatDdsSaOpt3.updateActionPhase(4);
        }
    }

    private void addOrUpdateWechatStallCount(int i, int i2, long j) {
        if ((j == 1 || j == 2) && shouldDoActionForWechatOpt() && this.isVideoFront) {
            Pair pair = new Pair(Integer.valueOf(i), Integer.valueOf(i2));
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mCurrentCellStats.get(pair);
            log("addOrUpdateWechatStallCount freq = " + i + " pci = " + i2 + " rat = " + j + " pair" + pair + "stats = " + concurrentHashMap);
            if (concurrentHashMap == null) {
                ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                concurrentHashMap2.put("count", 1L);
                concurrentHashMap2.put("duration", 0L);
                concurrentHashMap2.put("rat", Long.valueOf(j));
                this.mCurrentCellStats.put(pair, concurrentHashMap2);
                return;
            }
            if (((Long) concurrentHashMap.get("rat")).longValue() == j) {
                long jLongValue = ((Long) concurrentHashMap.get("count")).longValue() + 1;
                concurrentHashMap.put("count", Long.valueOf(jLongValue));
                log("addOrUpdateWechatStallCount  CurCount = " + jLongValue);
            }
        }
    }

    private void addOrUpdateWechatStallDur(int i, int i2, long j, long j2) {
        if (j == 1 || j == 2) {
            Pair pair = new Pair(Integer.valueOf(i), Integer.valueOf(i2));
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mCurrentCellStats.get(pair);
            log("addOrUpdateWechatStallDur freq = " + i + " pci = " + i2 + " dur = " + j2 + " stats = " + concurrentHashMap);
            if (concurrentHashMap == null) {
                ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                concurrentHashMap2.put("count", 0L);
                concurrentHashMap2.put("duration", Long.valueOf(j2));
                concurrentHashMap2.put("rat", Long.valueOf(j));
                this.mCurrentCellStats.put(pair, concurrentHashMap2);
                return;
            }
            if (((Long) concurrentHashMap.get("rat")).longValue() == j) {
                long jLongValue = ((Long) concurrentHashMap.get("duration")).longValue();
                concurrentHashMap.put("duration", Long.valueOf(j2 + jLongValue));
                log("addOrUpdateWechatStallDur  oldDur = " + jLongValue);
            }
        }
    }

    class WechatDdsSaOpt {
        final int PHASE_ENABLE_SBP_B1 = 1;
        final int PHASE_ENABLE_SBP_A2 = 2;
        final int PHASE_ENABLE_SBP_A3 = 3;
        final int PHASE_EXIT_OPT = 4;

        WechatDdsSaOpt() {
        }

        public void updateActionPhase(int i) {
            MtkSelfStudyController mtkSelfStudyController = MtkSelfStudyController.this;
            if (mtkSelfStudyController.mIsInWeChatVoip && mtkSelfStudyController.isVideoFront && mtkSelfStudyController.enterWechatVideoDays > mtkSelfStudyController.studyBadCellDays && mtkSelfStudyController.mIsScreenOn) {
                mtkSelfStudyController.log("WechatDdsSaOpt updateActionPhase phase = " + i);
                int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
                if (i == 1) {
                    MtkSelfStudyController.this.wechatOptAction = 1;
                    MtkSelfStudyController.this.recordWechatAction(4);
                    return;
                }
                if (i == 2) {
                    MtkSelfStudyController.this.log("EVENT_WECHAT_VIDEO_OPTIMIZATION_MTK");
                    ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).enableSaForWechat(false);
                    MtkSelfStudyController.this.mWechatVideoOptimizationRst = false;
                    MtkSelfStudyController.this.wechatOptAction = 2;
                    ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.sendMessageDelayed(((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.obtainMessage(3003, Integer.valueOf(MtkSelfStudyController.this.wechatOptAction)), 3000L);
                    return;
                }
                if (i != 3) {
                    if (i != 4) {
                        return;
                    }
                    MtkSelfStudyController.this.wechatOptAction = -1;
                } else {
                    MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.SET_SA_STATUS, (Message) null);
                    MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.SET_A2_TIMER, (Message) null);
                    MtkSelfStudyController.this.wechatOptAction = 3;
                    MtkSelfStudyController.this.mWechatA3ActionOptRst = true;
                    ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.sendMessageDelayed(((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.obtainMessage(3003, Integer.valueOf(MtkSelfStudyController.this.wechatOptAction)), 3000L);
                }
            }
        }

        public void tryRecovery() {
            MtkSelfStudyController mtkSelfStudyController;
            sendInitialWechatRecoveryCommands();
            MtkSelfStudyController mtkSelfStudyController2 = MtkSelfStudyController.this;
            if (mtkSelfStudyController2.enterWechatVideoDays <= mtkSelfStudyController2.studyBadCellDays) {
                return;
            }
            mtkSelfStudyController2.log("WechatDdsSaOpt tryRecovery() ");
            int i = 0;
            while (true) {
                int i2 = MiuiPhoneUtils.PHONE_COUNT;
                mtkSelfStudyController = MtkSelfStudyController.this;
                if (i >= i2) {
                    break;
                }
                mtkSelfStudyController.sendAtCmd(i, MtkAtCmd.ENABLE_AFR_TO_SA, (Message) null);
                if (MtkSelfStudyController.this.mWechatA3ActionOptRst) {
                    MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.ENABLE_SA, (Message) null);
                }
                i++;
            }
            mtkSelfStudyController.enableSaForWechat(true);
            MtkSelfStudyController.this.wechatOptAction = -1;
            if (((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.hasMessages(1030)) {
                ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.removeMessages(1030);
            }
            if (((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.hasMessages(3003)) {
                ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.removeMessages(3003);
            }
            MtkSelfStudyController mtkSelfStudyController3 = MtkSelfStudyController.this;
            if (mtkSelfStudyController3.enterWechatVideoDays <= mtkSelfStudyController3.studyBadCellDays || !mtkSelfStudyController3.mIsProcessedWechatEnter) {
                return;
            }
            if (mtkSelfStudyController3.mNrCellStats.size() > 0) {
                MtkSelfStudyController mtkSelfStudyController4 = MtkSelfStudyController.this;
                mtkSelfStudyController4.setOrResetWechatOffset(mtkSelfStudyController4.mNrCellStats, 2, true);
            }
            if (MtkSelfStudyController.this.mLteCellStats.size() > 0) {
                MtkSelfStudyController mtkSelfStudyController5 = MtkSelfStudyController.this;
                mtkSelfStudyController5.setOrResetWechatOffset(mtkSelfStudyController5.mLteCellStats, 1, true);
            }
        }

        private void sendInitialWechatRecoveryCommands() {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.ENABLE_54G_INDOOR_MODE, (Message) null);
                MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.ENABLE_ELEVATOR_MODE, (Message) null);
                MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.RESTORE_DISCARD_TIMER, (Message) null);
                MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.RESTORE_A2_TIMER, (Message) null);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onSimStateChanged(int i) {
            MtkSelfStudyController.this.log("wechat opt onSimStateChanged " + i);
            if (i == 1) {
                tryRecovery();
            }
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void wechatOptTryRecovery() {
        WechatDdsSaOpt wechatDdsSaOpt = this.mWechatOpt;
        if (wechatDdsSaOpt != null) {
            wechatDdsSaOpt.tryRecovery();
        }
    }

    private List parserStreamMediaCellInfos(String str) {
        LinkedList linkedList = new LinkedList();
        String[] strArrSplit = str.split(":", 2);
        if (strArrSplit.length < 2) {
            log("Invalid input format: no colon found.");
            return linkedList;
        }
        String[] strArrSplit2 = strArrSplit[1].trim().split(",");
        log("Stream Data array length: " + strArrSplit2.length);
        if ((strArrSplit2.length - 2) % 8 != 0) {
            log("Data length after prefix is not a multiple of interval.");
            return linkedList;
        }
        for (int i = 0; i < 8; i++) {
            int i2 = i + 2;
            int i3 = i + 10;
            int i4 = i + 18;
            int i5 = i + 26;
            int i6 = i + 34;
            int i7 = i + 42;
            if (i7 >= strArrSplit2.length) {
                log("Data array out of bounds for cell " + i);
                return linkedList;
            }
            String strTrim = strArrSplit2[i2].trim();
            if (strTrim.isEmpty() || "0".equals(strTrim)) {
                break;
            }
            try {
                CellInfo cellInfo = new CellInfo();
                cellInfo.setCid(Long.parseLong(strTrim));
                cellInfo.setGlobalCellId(getPlmn() + Long.toString(Long.parseLong(strTrim), 16));
                cellInfo.setRsrp(Integer.parseInt(strArrSplit2[i3].trim()));
                cellInfo.setAction(Integer.parseInt(strArrSplit2[i4].trim()));
                cellInfo.setActionValue(Integer.parseInt(strArrSplit2[i5].trim()));
                cellInfo.setBitmask(Integer.parseInt(strArrSplit2[i6].trim()));
                cellInfo.setDataType(Integer.parseInt(strArrSplit2[i7].trim()));
                linkedList.add(cellInfo);
            } catch (NumberFormatException e) {
                log("Number format error parsing cell " + i + ": " + e.getMessage());
            }
        }
        return linkedList;
    }

    /* JADX WARN: Code duplicated, block: B:126:0x02d8  */
    private void processStreamMediaDataInfo(String str, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        String str2;
        if (!this.cloudStreamMediaOn || !MiuiPhoneUtils.isSupportMtkDataOptimization() || !this.mIsMobileDataOn || MiuiPhoneUtils.isDuringCall() || str == null || str.isEmpty()) {
            return;
        }
        String[] strArrSplit = str.split(",");
        log("processStreamMediaDataInfo data = " + Arrays.toString(strArrSplit) + " , eventType = " + i);
        if (i == 401) {
            if (strArrSplit.length <= 5 || (str2 = strArrSplit[5]) == null) {
                return;
            }
            try {
                int i8 = Integer.parseInt(str2.trim());
                if (i8 == 1) {
                    enableSaForStreamMedia(true);
                } else if (i8 == 2) {
                    enableSaForStreamMedia(false);
                }
                return;
            } catch (NumberFormatException e) {
                Log.e("MtkSelfStudyController", "parse 401 err :", e);
                return;
            }
        }
        if (i != 402) {
            if (i == 403) {
                if (strArrSplit.length < 5) {
                    log("parse 403 data length < 5");
                    return;
                }
                try {
                    int i9 = Integer.parseInt(strArrSplit[3].trim());
                    int i10 = Integer.parseInt(strArrSplit[4].trim());
                    CellInfo currentCellInfoV3 = getCurrentCellInfoV3();
                    if (currentCellInfoV3 != null && i9 == currentCellInfoV3.freq && i10 == currentCellInfoV3.pci) {
                        int[] iArr = this.mActionForStream;
                        doCellActionForStreamMedia(iArr[0], iArr[1], i9, i10);
                        return;
                    }
                    return;
                } catch (Exception e2) {
                    Log.e("MtkSelfStudyController", "parse 403 err :", e2);
                    return;
                }
            }
            if (i == 404) {
                List<CellInfo> list = parserStreamMediaCellInfos(str);
                if (list.isEmpty()) {
                    log("parserStreamMediaCellInfos is null");
                    return;
                }
                new ArrayList();
                new ArrayList();
                for (CellInfo cellInfo : list) {
                    cellInfo.isPeak = cellInfo.getBitmask() & 1;
                    cellInfo.isWorkDay = (cellInfo.getBitmask() >> 1) & 1;
                    cellInfo.updateTimeMills = System.currentTimeMillis();
                    this.mIsResidentCell = isResidentCellScene();
                    boolean zIsResidentCellByGci = isResidentCellByGci(getPlmn() + cellInfo.cid);
                    this.mIsSubway = isSubwayScene();
                    int i11 = this.mIsSubway ? 1 : zIsResidentCellByGci ? 2 : 0;
                    cellInfo.setCellType(zIsResidentCellByGci ? 1 : 0);
                    cellInfo.setSceneDataType(i11);
                    insertOrUpdateSamplingDatabase(cellInfo, zIsResidentCellByGci, cellInfo.cellType);
                }
                return;
            }
            return;
        }
        if (strArrSplit.length < 8) {
            log("parse 402 data length < 8");
            return;
        }
        try {
            int i12 = Integer.parseInt(strArrSplit[2].trim());
            int i13 = Integer.parseInt(strArrSplit[3].trim());
            int i14 = Integer.parseInt(strArrSplit[4].trim());
            int i15 = Integer.parseInt(strArrSplit[5].trim());
            int i16 = Integer.parseInt(strArrSplit[6].trim());
            int i17 = Integer.parseInt(strArrSplit[7].trim());
            if (i12 != 2 && i12 != 4) {
                i2 = 1;
                i13 = i13;
            } else if (i12 == this.dataType) {
                if (i13 != 1 || i14 == 0) {
                    i13 = i13;
                    if (i13 == 0) {
                        if (this.mWorkHandler.hasMessages(1053)) {
                            logV("remove long stall msg for modemStall.");
                            this.mWorkHandler.removeMessages(1053);
                        }
                        this.mIsStreamStall = false;
                        if (SelfStudyControllerBase.isBeginCollectCellData) {
                            if (this.mWorkHandler.hasMessages(1041)) {
                                this.mWorkHandler.removeMessages(1041);
                            }
                            stopToCollectCellData(this.mIsWorkDay, 5);
                        }
                        processDataStallAppStatistic(false, 5);
                    }
                } else {
                    CellInfo currentCellInfoV4 = getCurrentCellInfoV3();
                    logV("receiver modemStall cellInfo:" + currentCellInfoV4);
                    if (currentCellInfoV4 != null && i15 == currentCellInfoV4.freq && i16 == currentCellInfoV4.pci) {
                        this.mIsStreamStall = true;
                        beginStreamDataStall(i12, 0);
                        updateRecordStateForHome();
                        if (!SelfStudyControllerBase.isBeginCollectCellData && (this.mIsResidentCell || this.mIsSubway)) {
                            beginToCollectCellData(this.mIsWorkDay, 5);
                        }
                        processDataStallAppStatistic(true, 5);
                        if (!this.mWorkHandler.hasMessages(1053)) {
                            logV("send long stall msg for modemStall.");
                            Handler handler = this.mWorkHandler;
                            handler.sendMessageDelayed(handler.obtainMessage(1053), 10000L);
                        }
                        recordDfsEvent(0, Integer.valueOf(i12), Integer.valueOf(i14), Integer.valueOf(this.dataType), 5);
                    } else {
                        i13 = i13;
                    }
                }
                i2 = 1;
            } else {
                i13 = i13;
                i2 = 1;
            }
            if (i12 == i2) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (isHasDelayApiGame() && this.mEnterGameBattleState && SelfStudyControllerBase.mEnterGameOptimizer) {
                    int i18 = i13;
                    if (i18 == 1) {
                        this.isGameLagFromModem = true;
                        this.startGameLagFromModem = jCurrentTimeMillis;
                        this.mGameLagGpfrModem = this.oldGameGci + "-" + i16 + "-" + i15 + "-" + i17;
                        if (!this.mTop2Cell.isEmpty() && this.mTop2Cell.containsKey(this.mGameLagGpfrModem)) {
                            String str3 = this.oldGameGci + "-" + i16 + "-" + i15;
                            if (i17 == 1 && !this.mLteGameCellStats.isEmpty() && this.mLteGameCellStats.containsKey(this.mGameLagGpfrModem)) {
                                doCellActionForGame(str3, i17, ((Integer) this.mLteGameCellStats.get(str3)).intValue(), false);
                                this.mTop2Cell.remove(this.mGameLagGpfrModem);
                            } else if (i17 == 2 && !this.mNrGameCellStats.isEmpty() && this.mNrGameCellStats.containsKey(this.mGameLagGpfrModem)) {
                                doCellActionForGame(str3, i17, ((Integer) this.mNrGameCellStats.get(str3)).intValue(), false);
                                this.mTop2Cell.remove(this.mGameLagGpfrModem);
                            }
                        }
                        i3 = i18;
                        i4 = i12;
                        i5 = i14;
                        i7 = 2;
                        i6 = 4;
                    } else {
                        if (i18 == 0) {
                            i4 = i12;
                            long j = this.startGameLagFromModem;
                            if (j == 0 || !this.isGameLagFromModem) {
                                i4 = i4;
                                i3 = i18;
                                i5 = i14;
                                i6 = 4;
                            } else {
                                long gameLagOrDur = getGameLagOrDur(jCurrentTimeMillis, j);
                                if (this.mGameLagGpfrModem.equals(this.oldGameGci + "-" + i16 + "-" + i15 + "-" + i17)) {
                                    i3 = i18;
                                    i5 = i14;
                                    i6 = 4;
                                    addOrUpdateGameLagDur(this.oldGameGci, i15, i16, i17, gameLagOrDur, jCurrentTimeMillis, this.mCurrentGameCellStats, "MODEM");
                                    this.startGameLagFromModem = jCurrentTimeMillis;
                                } else {
                                    i3 = i18;
                                    i5 = i14;
                                    i6 = 4;
                                }
                                this.isGameLagFromModem = false;
                            }
                        } else {
                            i3 = i18;
                        }
                        i7 = 2;
                    }
                } else {
                    i3 = i13;
                }
                i4 = i12;
                i5 = i14;
                i6 = 4;
                i7 = 2;
            } else {
                i3 = i13;
                i4 = i12;
                i5 = i14;
                i6 = 4;
                i7 = 2;
            }
            if (i4 == i7 || i4 == i6 || i4 == 1) {
                StringBuilder sb = new StringBuilder("freeze:");
                int i19 = i3;
                sb.append(i19);
                sb.append(",reason:");
                int i20 = i5;
                sb.append(i20);
                sb.append(",scene:");
                sb.append(i4);
                SelfStudyControllerBase.localLog("MtkSelfStudyController.ModemStallReason", sb.toString(), 50);
                recordStallDetailsStatistic(i19 == 1, i20, i4, 5, this.mStallDetailsList);
            }
        } catch (Exception e3) {
            Log.e("MtkSelfStudyController", "parse 402 err :", e3);
        }
    }

    public void doCellActionForStreamMedia(int i, int i2, int i3, int i4) {
        if (shouldDoActionForStreamMediaOpt()) {
            log("doCellActionForStreamMedia action = " + i + " , actionValue = " + i2);
            if (i != 0) {
                if (i == 1) {
                    enableSaForStreamMedia(true);
                    return;
                } else {
                    if (i == 2) {
                        enableSaForStreamMedia(false);
                        return;
                    }
                    return;
                }
            }
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone == null) {
                return;
            }
            OffsetCell currentCellInfo = getCurrentCellInfo(activeDataPhone);
            if (currentCellInfo != null && currentCellInfo.pci == i4 && currentCellInfo.freq == i3) {
                currentCellInfo.offset = i2 * 4;
                handleLiveStreamingOffsetInd(currentCellInfo, activeDataPhone.getPhoneId());
            }
            log("doCellActionForStreamMedia Receiver offset.");
        }
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0053 A[Catch: all -> 0x0032, TRY_LEAVE, TryCatch #0 {all -> 0x0032, blocks: (B:8:0x0018, B:10:0x001e, B:16:0x0036, B:17:0x0053), top: B:30:0x0018, outer: #1 }] */
    private void insertOrUpdateSamplingDatabase(CellInfo cellInfo, boolean z, int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = z ? this.maxSavedResidentCells : this.maxSavedDynamicCells;
        try {
            Cursor streamMediaByGci = this.dbHelper.getStreamMediaByGci(cellInfo.globalCellId);
            if (streamMediaByGci != null) {
                try {
                    if (streamMediaByGci.moveToFirst()) {
                        int i3 = streamMediaByGci.getInt(0);
                        this.dbHelper.updateStreamMedia(cellInfo, i3);
                        StringBuilder sb = new StringBuilder("insertOrUpdateSamplingDatabase");
                        sb.append(z ? "ForResident" : "ForDynamic");
                        sb.append(" id = ");
                        sb.append(i3);
                        sb.append(" , globalCellId = ");
                        sb.append(cellInfo.globalCellId);
                        log(sb.toString());
                    } else {
                        arrayList.add(cellInfo);
                        this.dbHelper.insertStreamMediaData(arrayList, i2, i);
                    }
                } catch (Throwable th) {
                    if (streamMediaByGci != null) {
                        try {
                            streamMediaByGci.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } else {
                arrayList.add(cellInfo);
                this.dbHelper.insertStreamMediaData(arrayList, i2, i);
            }
            if (streamMediaByGci != null) {
                streamMediaByGci.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOrUpdateStreamMediaCellInfo(int i, int i2, long j, long j2) {
        if (j == 1 || j == 2) {
            Pair pair = new Pair(Integer.valueOf(i), Integer.valueOf(i2));
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mCurrentStreamMediaCellStats.get(pair);
            log("addOrUpdateStreamMediaCellInfo freq = " + i + " pci = " + i2 + " dur = " + j2 + " stats = " + concurrentHashMap);
            if (concurrentHashMap == null) {
                ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                concurrentHashMap2.put("totalCnt", 0L);
                concurrentHashMap2.put("duration", Long.valueOf(j2));
                concurrentHashMap2.put("curRat", Long.valueOf(j));
                this.mCurrentStreamMediaCellStats.put(pair, concurrentHashMap2);
                return;
            }
            if (((Long) concurrentHashMap.get("curRat")).longValue() == j) {
                long jLongValue = ((Long) concurrentHashMap.get("duration")).longValue();
                concurrentHashMap.put("duration", Long.valueOf(j2 + jLongValue));
                log("addOrUpdateStreamMediaCellInfo  oldDur = " + jLongValue);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:10:0x0051 A[Catch: all -> 0x004f, TRY_LEAVE, TryCatch #2 {all -> 0x004f, blocks: (B:5:0x0025, B:7:0x002b, B:10:0x0051), top: B:26:0x0025, outer: #1 }] */
    public void insertOrUpdateOffsetCellDatabase(OffsetCell offsetCell, int i) {
        log("insertOrUpdateOffsetCellDatabase cellInfo = " + offsetCell + " , phoneId = " + i);
        try {
            Cursor offsetCellByGci = this.dbHelper.getOffsetCellByGci(offsetCell.globalCellId);
            if (offsetCellByGci != null) {
                try {
                    if (offsetCellByGci.moveToFirst()) {
                        int i2 = offsetCellByGci.getInt(0);
                        this.dbHelper.updateOffsetCell(offsetCell, i2, i);
                        log("insertOrUpdateOffsetCellDatabase id = " + i2 + " , globalCellId = " + offsetCell.globalCellId);
                    } else {
                        this.dbHelper.insertOffsetCellData(offsetCell, i);
                    }
                } catch (Throwable th) {
                    if (offsetCellByGci != null) {
                        try {
                            offsetCellByGci.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } else {
                this.dbHelper.insertOffsetCellData(offsetCell, i);
            }
            if (offsetCellByGci != null) {
                offsetCellByGci.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0074 A[Catch: all -> 0x0071, TRY_LEAVE, TryCatch #0 {all -> 0x0071, blocks: (B:5:0x003f, B:7:0x0045, B:10:0x0074), top: B:24:0x003f, outer: #2 }] */
    private void insertOrUpdateHomeStreamBadCellDatabase(String str, int i, int i2, int i3, int i4) {
        logV("insertOrUpdateHomeStreamBadCellDatabase globalCellId:" + TelephonyUtils.pii(str) + " , pci-freq:" + i + "-" + i2 + " , action = " + i3 + " , phoneId = " + i4);
        try {
            Cursor homeStreamBadCellByGci = this.dbHelper.getHomeStreamBadCellByGci(str);
            if (homeStreamBadCellByGci != null) {
                try {
                    if (homeStreamBadCellByGci.moveToFirst()) {
                        int i5 = homeStreamBadCellByGci.getInt(0);
                        this.dbHelper.updateHomeStreamBadCell(str, i, i2, i3, i5, i4);
                        logV("insertOrUpdateHomeStreamBadCellDatabase id:" + i5 + ",globalCellId:" + TelephonyUtils.pii(str));
                    } else {
                        this.dbHelper.insertHomeStreamBadCellData(str, i, i2, i3, i4);
                    }
                } catch (Throwable th) {
                    if (homeStreamBadCellByGci == null) {
                        throw th;
                    }
                    try {
                        homeStreamBadCellByGci.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                }
            } else {
                this.dbHelper.insertHomeStreamBadCellData(str, i, i2, i3, i4);
            }
            if (homeStreamBadCellByGci != null) {
                homeStreamBadCellByGci.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMatchedHomeStreamMediaBadCell(String str) {
        logV("isMatchedHomeStreamMediaBadCell globalCellId:" + TelephonyUtils.pii(str));
        try {
            Cursor homeStreamBadCellByGci = this.dbHelper.getHomeStreamBadCellByGci(str);
            if (homeStreamBadCellByGci != null) {
                try {
                    if (homeStreamBadCellByGci.moveToFirst()) {
                        logV("isMatchedHomeStreamMediaBadCell isMatched");
                        homeStreamBadCellByGci.close();
                        return true;
                    }
                } catch (Throwable th) {
                    try {
                        homeStreamBadCellByGci.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            if (homeStreamBadCellByGci == null) {
                return false;
            }
            homeStreamBadCellByGci.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void deleteHomeStreamMediaBadCell(String str) {
        logV("deleteHomeStreamMediaBadCell globalCellId:" + TelephonyUtils.pii(str));
        try {
            Cursor homeStreamBadCellByGci = this.dbHelper.getHomeStreamBadCellByGci(str);
            if (homeStreamBadCellByGci != null) {
                try {
                    if (homeStreamBadCellByGci.moveToFirst()) {
                        int i = homeStreamBadCellByGci.getInt(0);
                        logV("deleteHomeStreamMediaBadCell id = " + i);
                        this.dbHelper.deleteHomeStreamBadCell(i);
                    }
                } catch (Throwable th) {
                    try {
                        homeStreamBadCellByGci.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            if (homeStreamBadCellByGci != null) {
                homeStreamBadCellByGci.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void insertStreamDataForTest(String str, boolean z) {
        log("insertStreamDataForTest urcAtString = " + str);
        if (str == null || str.isEmpty() || !str.startsWith("+EDMFAPP:")) {
            return;
        }
        List<CellInfo> list = parserStreamMediaCellInfos(str);
        if (list.isEmpty()) {
            log("insertStreamDataForTest parserStreamMediaCellInfos is null");
            return;
        }
        for (CellInfo cellInfo : list) {
            int i = 1;
            cellInfo.isPeak = cellInfo.getBitmask() & 1;
            cellInfo.isWorkDay = (cellInfo.getBitmask() >> 1) & 1;
            cellInfo.updateTimeMills = System.currentTimeMillis();
            this.mIsResidentCell = isResidentCellScene();
            this.mIsSubway = isSubwayScene();
            if (!this.mIsSubway) {
                i = this.mIsResidentCell ? 2 : 0;
            }
            cellInfo.setCellType(z ? 1 : 0);
            cellInfo.setSceneDataType(i);
            insertOrUpdateSamplingDatabase(cellInfo, z, cellInfo.cellType);
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void sendStreamCellInfoToModem() {
        CellInfo currentCellInfoV3;
        log("sendStreamCellInfoToModem");
        if (isNeedSkipOperation() || (currentCellInfoV3 = getCurrentCellInfoV3()) == null) {
            return;
        }
        String str = currentCellInfoV3.globalCellId;
        int rsrpFormStreamDataTableByGciAndType = this.dbHelper.getRsrpFormStreamDataTableByGciAndType(str, this.dataType, this.mIsWorkDay ? 1 : 0, !currentIsNonPeak() ? 1 : 0, this.mIsResidentCell);
        this.mActionForStream = this.dbHelper.getActionFromStreamMediaByGciAndType(str, this.dataType);
        if (rsrpFormStreamDataTableByGciAndType != Integer.MAX_VALUE) {
            log("sendStreamCellInfoToModem freq = " + currentCellInfoV3.freq + " , pci = " + currentCellInfoV3.pci + " , rsrp = " + rsrpFormStreamDataTableByGciAndType);
            sendSelfStudyRequest(403, 3, new int[]{currentCellInfoV3.freq, currentCellInfoV3.pci, rsrpFormStreamDataTableByGciAndType});
            recordCount("streamMediaUseDataCnt", "streamMedia");
        }
    }

    private void resetStreamOptForPhoneCrash() {
        SelfStudyDatabaseHelper selfStudyDatabaseHelper;
        log("resetStreamOptForPhoneCrash");
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null || (selfStudyDatabaseHelper = this.dbHelper) == null) {
            return;
        }
        List<OffsetCell> offsetCellsForTable = selfStudyDatabaseHelper.getOffsetCellsForTable();
        if (offsetCellsForTable.isEmpty()) {
            return;
        }
        for (OffsetCell offsetCell : offsetCellsForTable) {
            doCellOffsetAction(true, offsetCell.pci, offsetCell.freq, offsetCell.rat, offsetCell.offset, offsetCell.plmn, activeDataPhone.getPhoneId());
        }
        this.dbHelper.clearOffsetCellTable();
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void sendSelfStudyRequest(int i, int i2, byte[] bArr) {
        if (i == 15) {
            if (Build.IS_INTERNATIONAL_BUILD) {
                return;
            }
            sendAtCmd(constructionAtCmd(String.valueOf(15), 0, i2, bArr[0]), (Message) null, 0);
        } else if (i == 26) {
            sendAtCmd(constructionAtCmd(String.valueOf(26), 0, i2, bArr[0]), this.mWorkHandler.obtainMessage(1019), 0);
        } else {
            sendSelfStudyRequestByActiveDataPhone(i, i2, bArr);
        }
    }

    protected void sendSelfStudyRequestByActiveDataPhone(int i, int i2, byte[] bArr) {
        if (MiuiPhoneUtils.getActiveDataPhone() == null) {
            return;
        }
        Message messageObtainMessage = null;
        if (i == 6) {
            sendAtCmd(constructionAtCmd(String.valueOf(6), 0, i2, bArr[0]), bArr[0] == 0 ? this.mWorkHandler.obtainMessage(1009) : null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            return;
        }
        if (i == 10) {
            sendAtCmd(constructionAtCmd(String.valueOf(10), 0, i2, bArr[0]), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            byte b = bArr[0];
            if (b == 0) {
                resetUseStaus(1);
                return;
            } else {
                sendAtCmd(constructionAtCmd("SBP_HIGH_PRIORITY_CELL", 2, i2, b), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
                return;
            }
        }
        if (i == 12) {
            sendAtCmd(constructionAtCmd(String.valueOf(12), 0, i2, bArr[0]), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            this.cellTempRouteInfoList.clear();
            if (bArr[0] == 0) {
                resetUseStaus(2);
                return;
            }
            return;
        }
        if (i == 18) {
            sendAtCmd(constructionAtCmd(Integer.toString(18), 0, 1, bArr[0]), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            if (bArr[0] == 0) {
                Handler handler = this.mWorkHandler;
                handler.sendMessageDelayed(handler.obtainMessage(1013), 1000L);
                return;
            }
            return;
        }
        if (i == 19) {
            sendAtCmd(constructionAtCmd(Integer.toString(19), 0, 1, bArr[0]), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            return;
        }
        if (i != 23) {
            if (i != 24) {
                return;
            }
            sendAtCmd(constructionAtCmd(String.valueOf(24), 0, i2, bArr[0]), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            return;
        }
        String[] strArrConstructionAtCmd = constructionAtCmd(Integer.toString(23), 0, 1, bArr[0]);
        byte b2 = bArr[0];
        if (b2 == 0) {
            messageObtainMessage = this.mWorkHandler.obtainMessage(1016);
        } else if (b2 == 1) {
            messageObtainMessage = this.mWorkHandler.obtainMessage(1018);
        }
        sendAtCmd(strArrConstructionAtCmd, messageObtainMessage, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
    }

    private void performActionOnCurrentCell(int i, int i2, boolean z) {
        try {
            String[] strArr = {String.format("AT+EDMFAPP=%d,%d,%d,%d,%d,%d,0,%d,%d,%d,%d,%d", 20109, Integer.valueOf(i), Integer.valueOf(i2), 0, 0, 0, 0, 0, 0, 0, Integer.valueOf(z ? 0 : 3600)), "+EDMFAPP"};
            log("performActionOnCurrentCell atStudyCmd: " + Arrays.toString(strArr));
            FiveGManagerBase.getInstance().sendAtCommand(0, strArr, (Message) null);
        } catch (Exception e) {
            log("performActionOnCurrentCell error " + e.getMessage());
        }
    }

    /* JADX WARN: Code duplicated, block: B:30:0x00bb A[RETURN] */
    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void reSetBaredCells() {
        try {
            Cursor cursorQuery = this.dbHelper.getWritableDatabase().query("resident_cells_table", new String[]{"global_cell_id", "pci", "freq", "rat", "subId"}, "bar_cell_flag = ?", new String[]{"1"}, null, null, null);
            if (cursorQuery != null) {
                try {
                    if (!cursorQuery.moveToFirst()) {
                        log("No barred cells found");
                        if (cursorQuery == null) {
                            return;
                        }
                    } else {
                        int columnIndex = cursorQuery.getColumnIndex("global_cell_id");
                        int columnIndex2 = cursorQuery.getColumnIndex("pci");
                        int columnIndex3 = cursorQuery.getColumnIndex("freq");
                        int columnIndex4 = cursorQuery.getColumnIndex("rat");
                        int columnIndex5 = cursorQuery.getColumnIndex("subId");
                        if (columnIndex == -1 || columnIndex2 == -1 || columnIndex3 == -1 || columnIndex4 == -1 || columnIndex5 == -1) {
                            log("Error: Missing global_cell_id column");
                        } else {
                            do {
                                String string = cursorQuery.getString(columnIndex);
                                cursorQuery.getInt(columnIndex2);
                                cursorQuery.getInt(columnIndex3);
                                cursorQuery.getInt(columnIndex4);
                                int i = cursorQuery.getInt(columnIndex5);
                                int phoneId = android.telephony.SubscriptionManager.getPhoneId(i);
                                log("Found barred cell: " + string + " slotId: " + phoneId + " subId: " + i);
                                if (phoneId != -1) {
                                    SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
                                    selfStudyDatabaseHelper.updateBarCellOrVonrLimitFlagForResidentCells(selfStudyDatabaseHelper.getWritableDatabase(), string, 0, 0L, "barCell", false);
                                }
                            } while (cursorQuery.moveToNext());
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
                log("No barred cells found");
                if (cursorQuery == null) {
                    return;
                }
            }
            cursorQuery.close();
        } catch (Exception e) {
            log("reSetBaredCells error: " + e.getMessage());
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public boolean performActionForVonrOpt(String str, int i) {
        int columnIndex;
        String[] strArr = {str};
        boolean z = false;
        try {
            Cursor cursorQuery = this.dbHelper.query("resident_cells_table", new String[]{"global_cell_id", "vonr_limit_flag"}, "global_cell_id = ?", strArr, null, null, null);
            try {
                if (cursorQuery.moveToFirst() && (columnIndex = cursorQuery.getColumnIndex("vonr_limit_flag")) != -1) {
                    int i2 = cursorQuery.getInt(columnIndex);
                    if (i == 101 ? i2 == 0 : !(i != 102 || i2 != 1)) {
                        z = true;
                    }
                }
                cursorQuery.close();
                return z;
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
            Log.e("VonrAction", "Error querying database: " + e.getMessage());
            return false;
        }
    }

    private void sendAtCmd(String[] strArr, Message message, int i) {
        if (MiuiPhoneUtils.getPhone(i) == null) {
            log("get phone failed and error");
        } else {
            FiveGManagerBase.getInstance().sendAtCommand(i, strArr, message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAtCmd(int i, MtkAtCmd mtkAtCmd, Message message) {
        if (i < 0) {
            return;
        }
        log("sendAtCmd " + mtkAtCmd);
        if (PhoneFactory.getPhone(i) != null) {
            FiveGManagerBase.getInstance().sendAtCommand(i, mtkAtCmd.cmd(), message);
        }
    }

    public String[] constructionAtCmd(String str, int i, int i2, int i3) {
        String[] strArr = {"", ""};
        if (i == 0) {
            strArr[0] = "AT+EDMFAPP=20002," + str + "," + i2 + "," + i3;
            strArr[1] = "+EDMFAPP";
        } else if (i == 1) {
            strArr[0] = "AT+ESBP=7,\"" + str + "\"";
            strArr[1] = "+ESBP";
        } else if (i == 2) {
            strArr[0] = "AT+ESBP=5,\"" + str + "\"," + i3;
        } else if (i == 3) {
            strArr[0] = "AT+ESBP=8,\"" + str + "\"";
            strArr[1] = "+ESBP";
        } else if (i == 4) {
            strArr[0] = "AT+ESBP=6,\"" + str + "\"," + i3;
        } else if (i == 6) {
            strArr[0] = "AT+EDMFAPP=20003," + str + "," + i2 + "," + i3;
            strArr[1] = "+EDMFAPP";
        } else if (i == 7) {
            strArr[0] = "AT+EDMFAPP=20006," + str + "," + i2 + "," + i3;
            strArr[1] = "+EDMFAPP";
        }
        Log.d("MtkSelfStudyController", "constructionAtCmd atCmd is " + strArr[0] + ", " + strArr[1]);
        return strArr;
    }

    public void resetCellAction() {
        List<CellInfo> actionCells = this.dbHelper.getActionCells();
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            log("resetCellAction dataPhone is null");
            return;
        }
        int phoneId = activeDataPhone.getPhoneId();
        String networkOperatorForPhone = this.tmManager.getNetworkOperatorForPhone(phoneId);
        for (CellInfo cellInfo : actionCells) {
            int i = 1;
            if (cellInfo.getRat() != 1) {
                i = 0;
            }
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).revertTeleAction(2, TelephonyActionBase.ActionReason.SELF_STUDY.value(), Integer.valueOf(phoneId), "", Integer.valueOf(i), networkOperatorForPhone, Integer.valueOf(cellInfo.getFreq()), Integer.valueOf(cellInfo.getPci()));
        }
    }

    public String[] constructAtCmd(String str, int i, int i2, int[] iArr) {
        StringBuilder sb = new StringBuilder("AT+EDMFAPP=");
        if (i == 0) {
            sb.append(PreferenceMarkLevel.LEVEL_LARGE_ONLY_SUMMARY);
        } else if (i == 6) {
            sb.append(20003);
        } else if (i == 7) {
            sb.append(20006);
        }
        sb.append(",");
        sb.append(str);
        if (i2 > 0) {
            sb.append(",");
            sb.append(i2);
            for (int i3 : iArr) {
                sb.append(",");
                sb.append(i3);
            }
        }
        String[] strArr = {sb.toString(), "+EDMFAPP"};
        log("constructAtCmd atCmd is" + strArr[0] + ", " + strArr[1]);
        return strArr;
    }

    public String[] constructAtCmd(int i, int i2) {
        String[] strArr = {"AT+EDMFAPP=" + i + "," + i2, "+EDMFAPP"};
        log("constructAtCmd atCmd is" + strArr[0] + ", " + strArr[1]);
        return strArr;
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void resetSmartNetworkBadCellAction() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            return;
        }
        int phoneId = activeDataPhone.getPhoneId();
        String networkOperatorForPhone = this.tmManager.getNetworkOperatorForPhone(phoneId);
        List<Pair> miSmartNetworkActionCells = this.dbHelper.getMiSmartNetworkActionCells(1);
        List<Pair> miSmartNetworkActionCells2 = this.dbHelper.getMiSmartNetworkActionCells(2);
        this.dbHelper.resetMiSmartNetworkActionCells();
        for (Pair pair : miSmartNetworkActionCells) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).revertTeleAction(2, TelephonyActionBase.ActionReason.SELF_STUDY.value(), Integer.valueOf(phoneId), "", 1, networkOperatorForPhone, pair.first, pair.second);
        }
        for (Pair pair2 : miSmartNetworkActionCells2) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).revertTeleAction(2, TelephonyActionBase.ActionReason.SELF_STUDY.value(), Integer.valueOf(phoneId), "", 0, networkOperatorForPhone, pair2.first, pair2.second);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void doCellAction(long j, int i, int i2, int i3, int i4) {
        int iMax;
        Log.d("MtkSelfStudyController", "doCellAction,pci = " + i + "freq = " + i2 + " rat = " + i3 + " action = " + i4);
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            log("doCellAction dataPhone is null");
            return;
        }
        int phoneId = activeDataPhone.getPhoneId();
        int i5 = 1;
        if (i3 == 1) {
            iMax = Math.max(i4 * this.cellOffset * 4, this.max_lte_offset * 4);
        } else {
            if (i3 != 2) {
                return;
            }
            iMax = Math.max(i4 * this.cellOffset * 4, this.max_nr_offset * 4);
            i5 = 0;
        }
        String networkOperatorForPhone = this.tmManager.getNetworkOperatorForPhone(phoneId);
        log("doCellAction sim plmn = " + networkOperatorForPhone);
        if (TextUtils.isEmpty(networkOperatorForPhone)) {
            return;
        }
        this.dbHelper.updateActionCells(i2, i);
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).teleAction(2, TelephonyActionBase.ActionReason.SELF_STUDY.value(), Integer.valueOf(phoneId), String.valueOf(iMax), Integer.valueOf(i5), networkOperatorForPhone, Integer.valueOf(i2), Integer.valueOf(i));
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void processMobileDataStateChange(boolean z) {
        if (!z) {
            int i = this.dataType;
            if (i != 2 && i != 4) {
                log("processMobileDataStateChange not in the stream scene");
                return;
            }
            stopToCollectCellData(this.mIsWorkDay, this.mCollectActionType);
            if (!this.mStreamMediaOptimizationRst) {
                enableSaForStreamMedia(true);
            }
            resetBenefitStatisticList();
            this.mLastDataType = this.dataType;
            processStopDataStall();
            removeMsgForExitSceneBreak();
            return;
        }
        int sceneType = getSceneType();
        if (this.mLastToModemDataType != sceneType) {
            processDelayDataAppTypeChange(sceneType, "DATA_ON");
            processUidAndActivityDataTypeChange(this.mLastToModemDataType, sceneType);
            this.dataType = sceneType;
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void processDefaultDataSlotChange() {
        super.processDefaultDataSlotChange();
        log("processDefaultDataChanged cur ddsPhoneId=" + this.mDdsPhoneId);
        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            enableSaForStreamMedia(true);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void processCallStateChange(int i, int i2) {
        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            processCallStateForStream(i, i2);
            processCallStateForGameOpt(i, i2);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void sendDdsToModem(int i) {
        sendAtCmd(constructionAtCmd(String.valueOf(14), 0, 1, i), (Message) null, i);
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void sendSelfStudyRequest(int i, int i2, int[] iArr) {
        if (MiuiPhoneUtils.getActiveDataPhone() == null) {
            if (i == 405 && MiuiPhoneUtils.isSupportMtkDataOptimization() && iArr.length == 4 && iArr[0] == 0) {
                String[] strArrConstructAtCmd = constructAtCmd(String.valueOf(405), 7, i2, iArr);
                sendAtCmd(strArrConstructAtCmd, (Message) null, 0);
                sendAtCmd(strArrConstructAtCmd, (Message) null, 1);
                return;
            }
            return;
        }
        String[] strArr = {"", ""};
        if (i == 15) {
            if (Build.IS_INTERNATIONAL_BUILD || MiuiPhoneUtils.getActiveDataPhone() == null) {
                return;
            }
            sendAtCmd(constructionAtCmd(String.valueOf(15), 7, i2, iArr[0]), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            return;
        }
        if (i == 16) {
            sendAtCmd(constructionAtCmd(String.valueOf(16), 6, 1, iArr[0]), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            return;
        }
        if (i == 25) {
            StringBuilder sb = new StringBuilder("AT+EDMFAPP=20003,25,");
            sb.append(i2);
            for (int i3 = 0; i3 < i2; i3++) {
                sb.append(",");
                sb.append(iArr[i3]);
            }
            strArr[0] = sb.toString();
            strArr[1] = "+EDMFAPP";
            log("EVENT_UPDATE_MODEM_THRESHOLDS atCmd = " + strArr[0]);
            sendAtCmd(strArr, (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            return;
        }
        if (i == 27) {
            sendAtCmd(constructAtCmd(String.valueOf(27), 6, i2, iArr), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            return;
        }
        if (i != 103) {
            if (i != 403) {
                if (i == 100) {
                    if (this.mRealTimeOptimizationIsInit) {
                        return;
                    }
                    this.mRealTimeOptimizationIsInit = true;
                    sendAtCmd(constructAtCmd(String.valueOf(100), 6, i2, iArr), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
                    return;
                }
                if (i == 101) {
                    if (iArr != null && iArr.length > 1) {
                        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                            sendAtCmd(constructAtCmd(String.valueOf(101), 7, i2, iArr), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
                            return;
                        }
                        return;
                    } else {
                        if (hasTriggeredOffsetOptimizationForCurrentCell() || this.mGameOffsetCells.size() > 50) {
                            return;
                        }
                        sendAtCmd(constructAtCmd(String.valueOf(101), 0, i2, iArr), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
                        return;
                    }
                }
                if (i != 400) {
                    if (i == 401) {
                        sendAtCmd(constructAtCmd(String.valueOf(com.ot.pubsub.i.a.b), 7, i2, iArr), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
                        return;
                    } else if (i == 405) {
                        sendAtCmd(constructAtCmd(String.valueOf(405), 7, i2, iArr), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
                        return;
                    } else {
                        if (i != 406) {
                            return;
                        }
                        sendAtCmd(constructAtCmd(String.valueOf(406), 7, i2, iArr), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
                        return;
                    }
                }
            }
            if (MiuiPhoneUtils.getActiveDataPhone() == null) {
                return;
            }
            StringBuilder sb2 = new StringBuilder("AT+EDMFAPP=20006,");
            sb2.append(String.valueOf(i));
            sb2.append(",");
            sb2.append(i2);
            for (int i4 = 0; i4 < i2; i4++) {
                sb2.append(",");
                sb2.append(iArr[i4]);
            }
            strArr[0] = sb2.toString();
            strArr[1] = "+EDMFAPP";
            if (400 == i) {
                if (this.mBaseModelIsInit) {
                    return;
                } else {
                    this.mBaseModelIsInit = true;
                }
            }
            log("EVENT_INIT_MODEM_BASE_MODELS atCmd = " + strArr[0]);
            sendAtCmd(strArr, (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
            return;
        }
        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            sendAtCmd(constructAtCmd(String.valueOf(103), 7, i2, iArr), (Message) null, MiuiPhoneUtils.getActiveDataPhone().getPhoneId());
        }
    }

    public void doCellOffsetAction(boolean z, int i, int i2, int i3, int i4, String str, int i5) {
        Phone activeDataPhone;
        Log.d("MtkSelfStudyController", "doCellAction,pci = " + i + "freq = " + i2 + " rat = " + i3 + " offset = " + i4 + " isReset = " + z);
        int i6 = i3 == SelfStudyControllerBase.Rat.Lte.value ? 1 : 0;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (z) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).revertTeleAction(2, TelephonyActionBase.ActionReason.SELF_STUDY.value(), Integer.valueOf(i5), "", Integer.valueOf(i6), str, Integer.valueOf(i2), Integer.valueOf(i));
            return;
        }
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).teleAction(2, TelephonyActionBase.ActionReason.SELF_STUDY.value(), Integer.valueOf(i5), String.valueOf(i4), Integer.valueOf(i6), str, Integer.valueOf(i2), Integer.valueOf(i));
        if (!MiuiPhoneUtils.isSupportMtkDataOptimization() || (activeDataPhone = MiuiPhoneUtils.getActiveDataPhone()) == null || getCurrentCellInfo(activeDataPhone) == null) {
            return;
        }
        insertOrUpdateOffsetCellDatabase(new OffsetCell(i2, i, 0, str, i3, getCurrentCellInfo(activeDataPhone).globalCellId), i5);
    }

    private void doResidentCellAction(SQLiteDatabase sQLiteDatabase) {
        log("doResidentCellAction");
        resetResidentCellAction(sQLiteDatabase);
        if (this.mFeatureEnable) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                int subId = MiuiPhoneUtils.getPhone(i).getSubId();
                if (SubscriptionManager.isValidSubscriptionId(subId)) {
                    if (this.mIsWorkDay) {
                        queryResidentCellsAndDoCellAction(sQLiteDatabase, SelfStudyControllerBase.Scene.Company.value, subId, i);
                    }
                    queryResidentCellsAndDoCellAction(sQLiteDatabase, SelfStudyControllerBase.Scene.Home.value, subId, i);
                }
            }
        }
    }

    private void resetResidentCellAction(SQLiteDatabase sQLiteDatabase) {
        boolean z;
        Cursor cursorQuery = sQLiteDatabase.query("resident_cells_table", new String[]{"freq", "pci", "rat", "plmn", "subId", "global_cell_id"}, "have_been_optimized != ?", new String[]{"0"}, null, null, null);
        if (cursorQuery != null) {
            cursorQuery.moveToFirst();
            while (!cursorQuery.isAfterLast()) {
                boolean z2 = false;
                int i = cursorQuery.getInt(0);
                int i2 = cursorQuery.getInt(1);
                int i3 = cursorQuery.getInt(2);
                String string = cursorQuery.getString(3);
                int i4 = cursorQuery.getInt(4);
                String string2 = cursorQuery.getString(5);
                int i5 = i3 != 1 ? 0 : 1;
                int i6 = 0;
                while (i6 < MiuiPhoneUtils.PHONE_COUNT) {
                    if (i4 == MiuiPhoneUtils.getPhone(i6).getSubId()) {
                        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).revertTeleAction(2, TelephonyActionBase.ActionReason.SELF_STUDY.value(), Integer.valueOf(i6), "", Integer.valueOf(i5), string, Integer.valueOf(i), Integer.valueOf(i2));
                        z = false;
                        this.dbHelper.updateActionResidentCells(sQLiteDatabase, string2, 0);
                    } else {
                        z = z2;
                    }
                    i6++;
                    z2 = z;
                }
                cursorQuery.moveToNext();
            }
            cursorQuery.close();
        }
    }

    private void queryResidentCellsAndDoCellAction(SQLiteDatabase sQLiteDatabase, int i, int i2, int i3) {
        String[] strArr = {String.valueOf(i), String.valueOf(i2)};
        String[] strArr2 = {"pci", "freq", "rat", "plmn", "global_cell_id", "data_score", "rsrp_avg", "sinr_avg"};
        Cursor cursorQuery = sQLiteDatabase.query("resident_cells_table", strArr2, " scene = ? and subId = ? ", strArr, null, null, " store_time DESC");
        if (cursorQuery.getCount() < 2) {
            cursorQuery.close();
            return;
        }
        int count = cursorQuery.getCount();
        Cursor cursorQuery2 = sQLiteDatabase.query("resident_cells_table", strArr2, " scene = ? and subId = ? ", strArr, null, null, " data_score ASC");
        if (cursorQuery.moveToFirst() && cursorQuery2.moveToFirst()) {
            int i4 = 0;
            boolean z = true;
            while (!cursorQuery2.isAfterLast()) {
                int i5 = cursorQuery2.getInt(5);
                int i6 = cursorQuery2.getInt(6);
                int i7 = cursorQuery2.getInt(7);
                if (i5 != 0) {
                    z = false;
                }
                if (i6 < -105 || i7 < 0) {
                    i4++;
                }
                cursorQuery2.moveToNext();
            }
            log(" rsrpOrSinrCount: " + i4 + " count: " + count + " shouldIgnoreOptimization: " + z);
            if (z) {
                return;
            }
            cursorQuery2.moveToFirst();
            String string = cursorQuery.getString(4);
            if (string.equals(cursorQuery2.getString(4))) {
                doCellOffsetAction(false, cursorQuery.getInt(0), cursorQuery.getInt(1), cursorQuery.getInt(2), 12, cursorQuery.getString(3), i3);
                this.dbHelper.updateActionResidentCells(sQLiteDatabase, string, 1);
                cursorQuery.moveToNext();
                while (!cursorQuery.isAfterLast()) {
                    int i8 = cursorQuery.getInt(0);
                    int i9 = cursorQuery.getInt(1);
                    int i10 = cursorQuery.getInt(2);
                    String string2 = cursorQuery.getString(3);
                    String string3 = cursorQuery.getString(4);
                    doCellOffsetAction(false, i8, i9, i10, -12, string2, i3);
                    this.dbHelper.updateActionResidentCells(sQLiteDatabase, string3, 2);
                    cursorQuery.moveToNext();
                }
            } else {
                boolean z2 = HyperOSCustFeatureResolve.getBoolean("special_device_for_cell_info_bool", false);
                if (z2 || (!z2 && i4 < count)) {
                    doCellOffsetAction(false, cursorQuery.getInt(0), cursorQuery.getInt(1), cursorQuery.getInt(2), -12, cursorQuery.getString(3), i3);
                    this.dbHelper.updateActionResidentCells(sQLiteDatabase, string, 2);
                    String string4 = cursorQuery2.getString(4);
                    doCellOffsetAction(false, cursorQuery2.getInt(0), cursorQuery2.getInt(1), cursorQuery2.getInt(2), 12, cursorQuery2.getString(3), i3);
                    this.dbHelper.updateActionResidentCells(sQLiteDatabase, string4, 1);
                }
            }
        }
        cursorQuery.close();
        cursorQuery2.close();
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void startStudyHomeResidentCells() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (this.mIsStudyingHomeResidentCells || activeDataPhone == null || !this.mFeatureEnable) {
            return;
        }
        this.mIsStudyingHomeResidentCells = true;
        sendAtCmd(constructAtCmd(20103, 200), (Message) null, activeDataPhone.getPhoneId());
        if (MiuiPhoneUtils.isSupportCallMessageOptimization() && this.cloudCallMessageOn) {
            performActionOnCurrentCell(0, 1, true);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void startStudyLowPowerModeCells() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (this.mIsStudyingLowPowerModeCells || activeDataPhone == null) {
            return;
        }
        this.mIsStudyingLowPowerModeCells = true;
        sendAtCmd(new String[]{"AT+EDMFAPP=20110,0,0,0,0", "+EDMFAPP"}, (Message) null, activeDataPhone.getPhoneId());
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void stopStudyLowPowerModeCells() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (!this.mIsStudyingLowPowerModeCells || activeDataPhone == null) {
            return;
        }
        this.mIsStudyingLowPowerModeCells = false;
        sendAtCmd(new String[]{"AT+EDMFAPP=20110,1,0,0,0", "+EDMFAPP"}, (Message) null, activeDataPhone.getPhoneId());
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void startPingpongStudy() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (this.mIsStudyingPingpongCells || activeDataPhone == null) {
            return;
        }
        this.mIsStudyingPingpongCells = true;
        this.mPingPongRecords.clear();
        sendAtCmd(new String[]{"AT+EDMFAPP=20105,335", "+EDMFAPP"}, (Message) null, activeDataPhone.getPhoneId());
        Log.d("MtkSelfStudyController", "startPingpongStudy phoneId=" + activeDataPhone.getPhoneId());
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void stopPingpongStudy() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (!this.mIsStudyingPingpongCells || activeDataPhone == null) {
            return;
        }
        this.mIsStudyingPingpongCells = false;
        sendAtCmd(new String[]{"AT+EDMFAPP=20105,336", "+EDMFAPP"}, (Message) null, activeDataPhone.getPhoneId());
        Log.d("MtkSelfStudyController", "stopPingpongStudy subId=" + activeDataPhone.getSubId() + ", records count=" + ((List) this.mPingPongRecords.get(activeDataPhone.getSubId(), new ArrayList())).size());
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void startStudyCompanyResidentCells() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (this.mIsStudyingCompanyResidentCells || !this.mIsWorkDay || activeDataPhone == null || !this.mFeatureEnable) {
            return;
        }
        this.mIsStudyingCompanyResidentCells = true;
        sendAtCmd(constructAtCmd(20103, 100), (Message) null, activeDataPhone.getPhoneId());
        if (MiuiPhoneUtils.isSupportCallMessageOptimization() && this.cloudCallMessageOn) {
            performActionOnCurrentCell(0, 1, true);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void stopStudyResidentCells(int i) {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if ((this.mIsStudyingHomeResidentCells || this.mIsStudyingCompanyResidentCells) && activeDataPhone != null) {
            this.mIsStudyingHomeResidentCells = false;
            this.mIsStudyingCompanyResidentCells = false;
            sendAtCmd(constructAtCmd(20103, 101), this.mWorkHandler.obtainMessage(1021), activeDataPhone.getPhoneId());
            if (MiuiPhoneUtils.isSupportCallMessageOptimization() && this.cloudCallMessageOn) {
                performActionOnCurrentCell(0, 2, true);
            }
        }
    }

    private void updateLowPowerInfo(SQLiteDatabase sQLiteDatabase) {
        try {
            this.dbHelper.resetTable(sQLiteDatabase, "lower_power_cell_table");
            constructLowerInfo(sQLiteDatabase);
            log("updateLowPowerInfo reset table done");
        } catch (Exception e) {
            Log.e("MtkSelfStudyController", "updateLowPowerInfo error", e);
        }
    }

    private void constructLowerInfo(SQLiteDatabase sQLiteDatabase) {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null) {
                int subId = phone.getSubId();
                if (SubscriptionManager.isValidSubscriptionId(subId)) {
                    try {
                        List residentCellsInfoBySceneAndRat = this.dbHelper.getResidentCellsInfoBySceneAndRat(sQLiteDatabase, SelfStudyControllerBase.Scene.Home.value, SelfStudyControllerBase.Rat.Nr.value, subId);
                        if (residentCellsInfoBySceneAndRat != null && residentCellsInfoBySceneAndRat.size() != 0) {
                            processResidentCells(phone, sQLiteDatabase, residentCellsInfoBySceneAndRat);
                        }
                    } catch (Exception e) {
                        Log.e("MtkSelfStudyController", "constructLowerInfo error for phoneId: " + i, e);
                    }
                }
            }
        }
    }

    private void processResidentCells(Phone phone, SQLiteDatabase sQLiteDatabase, List list) {
        String str = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        int subId = phone.getSubId();
        List list2 = (List) this.mPingPongRecords.get(subId);
        StringBuilder sb = new StringBuilder("processResidentCells enter, subId=");
        sb.append(subId);
        sb.append(", residentCellsInfo size=");
        sb.append(list.size());
        sb.append(", pingpongRecords=");
        sb.append(list2 == null ? "null" : Integer.valueOf(list2.size()));
        log(sb.toString());
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HashMap map = (HashMap) it.next();
            if (map != null) {
                try {
                    String string = map.get("global_cell_id").toString();
                    if (isPingpongCell(subId, string)) {
                        log("processResidentCells skip pingpong cell: " + string);
                    } else {
                        Object[] objArrCollectCellModemData = collectCellModemData(phone, map);
                        if (objArrCollectCellModemData != null) {
                            arrayList.add(objArrCollectCellModemData);
                        }
                    }
                } catch (Exception e) {
                    Log.e("MtkSelfStudyController", "collectCellModemData error", e);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        sQLiteDatabase.beginTransaction();
        try {
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                try {
                    writeCellDataToDb(sQLiteDatabase, (Object[]) obj, str);
                } catch (Exception e2) {
                    Log.e("MtkSelfStudyController", "writeCellDataToDb error", e2);
                }
            }
            sQLiteDatabase.setTransactionSuccessful();
            sQLiteDatabase.endTransaction();
        } catch (Throwable th) {
            sQLiteDatabase.endTransaction();
            throw th;
        }
    }

    private Object[] collectCellModemData(Phone phone, HashMap map) {
        String string = map.get("global_cell_id").toString();
        log("collectCellModemData globalCellId: " + string);
        String[] strArrSplit = string.split("_");
        if (strArrSplit.length <= 3) {
            return null;
        }
        String str = strArrSplit[2];
        if (str.length() < 3) {
            return null;
        }
        try {
            int[] iArrQueryModemCellStats = queryModemCellStats(phone.getPhoneId(), Integer.parseInt(str.substring(0, 3)), Integer.parseInt(str.substring(3)), strArrSplit[3]);
            if (iArrQueryModemCellStats == null) {
                return null;
            }
            return new Object[]{string, map, iArrQueryModemCellStats};
        } catch (NumberFormatException e) {
            Log.e("MtkSelfStudyController", "collectCellModemData plmn parse error: ".concat(str), e);
            return null;
        }
    }

    private void writeCellDataToDb(SQLiteDatabase sQLiteDatabase, Object[] objArr, String str) {
        String str2 = (String) objArr[0];
        HashMap map = (HashMap) objArr[1];
        int[] iArr = (int[]) objArr[2];
        int i = iArr[9];
        int i2 = iArr[10];
        int i3 = iArr[11];
        int i4 = iArr[12];
        double d = (((double) i) * 0.2d) + (((double) i2) * 0.3d) + (((double) i3) * 0.3d) + (((double) i4) * 0.2d);
        this.dbHelper.insertOrUpdatePowerCells(sQLiteDatabase, str2, ((Integer) map.get("freq")).intValue(), ((Integer) map.get("rat")).intValue(), ((Integer) map.get("scene")).intValue(), ((Integer) map.get("subId")).intValue(), ((Integer) map.get("rsrp_avg")).intValue(), ((Integer) map.get("sinr_avg")).intValue(), iArr[1], iArr[2], iArr[3], iArr[4], iArr[5], iArr[6], iArr[7], iArr[8], iArr[9], iArr[10], iArr[11], iArr[12], d, (((double) i) * 0.5d) + (((double) i2) * 0.1d) + (((double) i3) * 0.3d) + (((double) i4) * 0.1d), str);
    }

    private int[] queryModemCellStats(int i, int i2, int i3, String str) {
        Object obj;
        String[] strArr = {String.format("AT+EDMFAPP=20110,2,%d,%d,%s", Integer.valueOf(i2), Integer.valueOf(i3), str), "+EDMFAPP"};
        Object[] objArr = new Object[1];
        if (!FiveGManagerBase.getInstance().sendAtCmdSync(i, strArr, objArr) || (obj = objArr[0]) == null) {
            log("queryModemCellStats failed or null");
            return null;
        }
        try {
            String[] strArrSplit = ((String[]) obj)[0].split(",");
            if (strArrSplit.length < 14) {
                return null;
            }
            int[] array = Arrays.stream((String[]) Arrays.copyOfRange(strArrSplit, 1, strArrSplit.length)).mapToInt(new MtkSelfStudyController$$ExternalSyntheticLambda12()).toArray();
            if (array.length < 13) {
                return null;
            }
            return array;
        } catch (NumberFormatException e) {
            Log.e("MtkSelfStudyController", "queryModemCellStats parse error", e);
            return null;
        }
    }

    private void statResidentCellOptimizationBenefit(SQLiteDatabase sQLiteDatabase) {
        if (this.mPreferenceUtils.getBoolean("stat_benefit_ratio", false)) {
            log("have been statistic before");
            return;
        }
        float residentCellAverageDataScore = this.dbHelper.getResidentCellAverageDataScore(sQLiteDatabase);
        float f = this.mPreferenceUtils.getFloat("average_data_score", 0.0f);
        float residentCellAverageCount = this.dbHelper.getResidentCellAverageCount(sQLiteDatabase, "data_count");
        float f2 = this.mPreferenceUtils.getFloat("average_data_count", 0.0f);
        float residentCellAverageCount2 = this.dbHelper.getResidentCellAverageCount(sQLiteDatabase, "call_count");
        float f3 = this.mPreferenceUtils.getFloat("average_call_count", 0.0f);
        float fRecordResidentCellScore = recordResidentCellScore(f, residentCellAverageDataScore, "average_data_score", 0.0f);
        float fRecordResidentCellScore2 = recordResidentCellScore(f2, residentCellAverageCount, "average_data_count", 0.0f);
        float fRecordResidentCellScore3 = recordResidentCellScore(f3, residentCellAverageCount2, "average_call_count", 0.0f);
        log("recordResidentCellOptimizationBenefit dataScoreBenefit = " + fRecordResidentCellScore + " dataCountBenefit = " + fRecordResidentCellScore2 + " callCountBenefit = " + fRecordResidentCellScore3 + "averageDataScore = " + residentCellAverageDataScore + " averageDataCount = " + residentCellAverageCount + " averageCallCount = " + residentCellAverageCount2 + " averageDataScoreBefore = " + f + " averageDataCountBefore = " + f2 + " averageCallCountBefore = " + f3);
        recordResidentCellOptimizationBenefit(fRecordResidentCellScore, fRecordResidentCellScore2, fRecordResidentCellScore3, sQLiteDatabase);
    }

    private float recordResidentCellScore(float f, float f2, String str, float f3) {
        if (f != 0.0f) {
            return (f - f2) / f;
        }
        this.mPreferenceUtils.putFloat(str, f2);
        return f3;
    }

    private void recordResidentCellOptimizationBenefit(float f, float f2, float f3, SQLiteDatabase sQLiteDatabase) {
        if (f != 0.0f) {
            recordResidentCellOptimizationBenefit(f, f2, f3, this.dbHelper.getResidentCellsStatParams(sQLiteDatabase));
            this.mPreferenceUtils.putBoolean("stat_benefit_ratio", true);
        }
    }

    private long getCellPingPongCounts() {
        return ((IModemModule) ModuleDelegate.get(IModemModule.class)).getCellPingPongCounts();
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void sendBadCellToModem(List list) {
        String[] strArr = {"", ""};
        StringBuilder sb = new StringBuilder("AT+EDMFAPP=20003,21,");
        sb.append(list.size() * 2);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            SelfStudyControllerBase.CellUnit cellUnit = (SelfStudyControllerBase.CellUnit) it.next();
            sb.append(",");
            sb.append(cellUnit.cellFreq);
            sb.append(",");
            sb.append(cellUnit.cellPci);
        }
        if (list.size() == 0) {
            sb.append(",0");
        }
        strArr[0] = sb.toString();
        strArr[1] = "+EDMFAPP";
        log("sendBadCellToModem atCmd = " + strArr[0]);
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            log("sendBadCellToModem dataPhone is null");
        } else {
            sendAtCmd(strArr, (Message) null, activeDataPhone.getPhoneId());
        }
    }

    public void resetUseStaus(int i) {
        log("stop Use type is " + i);
        if (i == 1) {
            resetCellAction();
            return;
        }
        if (i != 2) {
            return;
        }
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            log("resetUseStaus dataPhone is null");
        } else {
            sendAtCmd(constructionAtCmd(String.valueOf(12), 0, 1, 0), (Message) null, activeDataPhone.getPhoneId());
        }
    }

    private void clearSafData() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone != null) {
            FiveGManagerBase.getInstance().sendAtCommand(activeDataPhone, new String[]{"AT+EDMFAPP=20100,601", "+EDMFAPP"}, (Message) null);
        }
    }

    private void getModemSafData(int i, ArrayList arrayList) {
        Object[] objArr = new Object[1];
        if (FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EDMFAPP=20100,600", "+EDMFAPP"}, objArr)) {
            parseSafResponse(objArr[0], arrayList);
        }
    }

    private void parseSafResponse(Object obj, ArrayList arrayList) {
        String[] strArr = (String[]) obj;
        if (strArr == null || strArr.length <= 0) {
            return;
        }
        String[] strArrSplit = strArr[0].split(",");
        if (strArrSplit.length < 40) {
            log("parseSafResponse Invalid data format");
        } else if (!isValidSafData(strArrSplit)) {
            log("parseSafResponse Invalid data");
        } else {
            arrayList.add(createMapWithData(strArrSplit));
        }
    }

    private boolean isValidSafData(String[] strArr) {
        for (int i = 2; i < strArr.length; i++) {
            String str = strArr[i];
            if (str != null && !str.trim().equals("0")) {
                return true;
            }
        }
        return false;
    }

    private ConcurrentHashMap createMapWithData(String[] strArr) {
        ConcurrentHashMap concurrentHashMap = null;
        try {
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            try {
                concurrentHashMap2.put("slotId", strArr[1]);
                concurrentHashMap2.put("dataType", Integer.valueOf(this.mLastDataType));
                concurrentHashMap2.put("ulDataStall", TextUtils.join(",", Arrays.copyOfRange(strArr, 2, 8)));
                concurrentHashMap2.put("dlDataStall", TextUtils.join(",", Arrays.copyOfRange(strArr, 8, 14)));
                concurrentHashMap2.put("highBler", TextUtils.join(",", Arrays.copyOfRange(strArr, 14, 20)));
                concurrentHashMap2.put("highInterfer", TextUtils.join(",", Arrays.copyOfRange(strArr, 20, 26)));
                concurrentHashMap2.put("nwAbnormal", TextUtils.join(",", Arrays.copyOfRange(strArr, 26, strArr.length)));
                return concurrentHashMap2;
            } catch (Exception e) {
                e = e;
                concurrentHashMap = concurrentHashMap2;
                e.printStackTrace();
                return concurrentHashMap;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    /* JADX WARN: Code duplicated, block: B:25:0x0077 A[LOOP:0: B:24:0x0075->B:25:0x0077, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:28:0x0088 A[LOOP:1: B:27:0x0086->B:28:0x0088, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:31:0x009c  */
    /* JADX WARN: Code duplicated, block: B:37:? A[RETURN, SYNTHETIC] */
    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void processStartDataStall(int i) {
        int[] iArr;
        int[] iArr2;
        int i2;
        if (isVideoDataStallOptimizationSupported() && this.cloudVideoOn) {
            if ((i == 4 || i == 2) && !shouldDoActionForStreamMediaOpt()) {
                return;
            }
            if (i == 4 || i == 2) {
                this.hasSendDataStallAction = true;
                this.mLastStartDataStallTime = SystemClock.elapsedRealtime();
                iArr = new int[]{0, 4, 2, 3};
                beginDataStall(i);
            } else {
                if (i == 3) {
                    iArr = new int[]{0, 2, 3};
                    iArr2 = new int[]{0};
                    beginDataStall(i);
                } else {
                    if (i != 1) {
                        return;
                    }
                    iArr = new int[]{1, 0, 4, 2, 3};
                    beginDataStall(i);
                }
                for (int i3 : iArr) {
                    this.lastLteModes.add(Integer.valueOf(i3));
                }
                for (int i4 : iArr2) {
                    this.lastNrModes.add(Integer.valueOf(i4));
                }
                if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                    clearSafData();
                }
            }
            iArr2 = iArr;
            while (i2 < r8) {
                this.lastLteModes.add(Integer.valueOf(i3));
            }
            while (i < r8) {
                this.lastNrModes.add(Integer.valueOf(i4));
            }
            if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                clearSafData();
            }
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void processStopDataStall() {
        if (this.hasSendDataStallAction) {
            this.mLastStopDataStallTime = SystemClock.elapsedRealtime();
            this.hasSendDataStallAction = false;
        }
        HashSet hashSet = this.lastLteModes;
        if (hashSet != null && hashSet.size() > 0) {
            Iterator it = this.lastLteModes.iterator();
            while (it.hasNext()) {
                stopDataStall(((Integer) it.next()).intValue(), 0);
            }
            this.lastLteModes.clear();
        }
        HashSet hashSet2 = this.lastNrModes;
        if (hashSet2 != null && hashSet2.size() > 0) {
            Iterator it2 = this.lastNrModes.iterator();
            while (it2.hasNext()) {
                stopDataStall(((Integer) it2.next()).intValue(), 1);
            }
            this.lastNrModes.clear();
        }
        stopDataStallOnNddsPhoneId(MiuiPhoneUtils.getActiveDataPhoneId() == 0 ? 1 : 0);
        if (!MiuiPhoneUtils.isSupportMtkDataOptimization() || this.mLastDataType == 0) {
            return;
        }
        getModemSafData(MiuiPhoneUtils.getActiveDataPhoneId(), this.mSafDataList);
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void processDataTypeChanged(int i) {
        if (i == 2 && this.dataType != 2) {
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1025), 30000L);
        }
        if (i == 1 && this.dataType != 1) {
            this.mLastDataType = 1;
            Handler handler2 = this.mWorkHandler;
            handler2.sendMessage(handler2.obtainMessage(1023));
        } else if (i != 1 && this.dataType == 1) {
            if (this.mWorkHandler.hasMessages(1023)) {
                this.mWorkHandler.removeMessages(1023);
                this.mLastDataType = 1;
                processStopDataStall();
            }
            Handler handler3 = this.mWorkHandler;
            handler3.sendMessage(handler3.obtainMessage(SmartNetworkRelayScore.TRAFFIC_UNIT, 1));
        }
        int i2 = this.dataType;
        if (i2 != 4 && i2 != 8 && i2 != 2 && this.hasSendDataStallAction) {
            this.mWorkHandler.removeMessages(1023);
            if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                this.mLastDataType = i;
                Handler handler4 = this.mWorkHandler;
                handler4.sendMessage(handler4.obtainMessage(1023));
            } else {
                Handler handler5 = this.mWorkHandler;
                handler5.sendMessageDelayed(handler5.obtainMessage(1023), 30000L);
            }
        }
        int i3 = this.dataType;
        if (i3 == 4 || (i3 == 2 && !currentIsMorning() && !currentIsEveningPeak())) {
            boolean z = this.hasSendDataStallAction;
            Handler handler6 = this.mWorkHandler;
            if (!z) {
                handler6.sendMessage(handler6.obtainMessage(SmartNetworkRelayScore.TRAFFIC_UNIT, Integer.valueOf(this.dataType)));
            } else {
                handler6.removeMessages(1023);
            }
        }
        if (this.dataType == 8) {
            this.mWorkHandler.removeMessages(1023);
        }
        if (this.mIsSupportMtkVideoSbpOptimization) {
            int i4 = this.dataType;
            if (i4 == 1 || i4 == 2 || i4 == 4 || i4 == 8 || i4 == 3) {
                if (!this.hasSendSbpAction) {
                    if (!shouldDoActionForStreamMediaOpt()) {
                        return;
                    }
                    setSbpParam(false);
                    this.hasSendSbpAction = true;
                } else {
                    this.mWorkHandler.removeMessages(1033);
                }
            } else if (this.hasSendSbpAction) {
                this.mWorkHandler.removeMessages(1033);
                Handler handler7 = this.mWorkHandler;
                handler7.sendMessageDelayed(handler7.obtainMessage(1033), 30000L);
            }
            if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                if (shouldExitStreamMedia(i)) {
                    handleStreamMediaExit();
                } else {
                    updateStreamMediaCellStats();
                }
                handleShortVideoOptimization(i);
            }
        }
    }

    private boolean shouldExitStreamMedia(int i) {
        int i2;
        return (((i != 2 && i != 4) || (i2 = this.dataType) == 2 || i2 == 4) && this.mIsScreenOn) ? false : true;
    }

    private void handleStreamMediaExit() {
        if (!this.mWorkHandler.hasMessages(1025)) {
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1025), 30000L);
        }
        if (this.mWorkHandler.hasMessages(1041)) {
            this.mWorkHandler.removeMessages(1041);
        }
        stopStreamDataStall(0);
        stopToCollectCellData(this.mIsWorkDay, this.mCollectActionType);
        this.mStallDurTime = 0L;
        this.mCurrentStreamMediaCellStats.clear();
    }

    private void updateStreamMediaCellStats() {
        CellInfo currentCellInfoV3 = getCurrentCellInfoV3();
        if (currentCellInfoV3 != null) {
            this.oldStreamFreq = currentCellInfoV3.freq;
            this.oldStreamPci = currentCellInfoV3.pci;
            this.oldStreamRat = currentCellInfoV3.rat;
            Pair pair = new Pair(Integer.valueOf(this.oldStreamFreq), Integer.valueOf(this.oldStreamPci));
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("totalCnt", 0L);
            concurrentHashMap.put("duration", 0L);
            concurrentHashMap.put("curRat", Long.valueOf(this.oldStreamRat));
            boolean zIsCurrentCellResident = isCurrentCellResident();
            this.isResidentCell = zIsCurrentCellResident;
            if (zIsCurrentCellResident) {
                this.mLastStreamMediaCellTime = System.currentTimeMillis();
            }
            this.mCurrentStreamMediaCellStats.clear();
            this.mCurrentStreamMediaCellStats.put(pair, concurrentHashMap);
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone == null || this.oldStreamOffSetCell != null) {
                return;
            }
            this.oldStreamOffSetCell = getCurrentCellInfo(activeDataPhone);
        }
    }

    private void handleShortVideoOptimization(int i) {
        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
            return;
        }
        logV("handleShortVideoOptimization tempDataType:" + i + " ,dataType:" + this.dataType);
        if (shouldEnterShortVideoOptimization(i)) {
            enterShortVideoOptimization();
        } else {
            shouldExitShortVideoOptimization(i);
        }
    }

    private boolean shouldEnterShortVideoOptimization(int i) {
        return i != 4 && this.dataType == 4;
    }

    private void enterShortVideoOptimization() {
        String topAppName = getTopAppName();
        if (isDouYinApp(topAppName) && isColdStart(topAppName)) {
            beginStreamDataStall(4, 1);
            Handler handler = this.mWorkHandler;
            handler.sendMessage(handler.obtainMessage(1052));
        }
    }

    private boolean shouldExitShortVideoOptimization(int i) {
        return i == 4 && this.dataType != 4;
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void processVideoQualityChanged(int i) {
        int i2;
        logV("processVideoQualityChanged data type is " + this.dataType + " lastQuality is " + i + " isVideoFront is " + this.isVideoFront + " hasSendDataStallAction" + this.hasSendDataStallAction + " isLteOrNr " + isLteOrNr() + " mIsMobileDataOn " + this.mIsMobileDataOn + " mIsScreenOn is " + this.mIsScreenOn + " mIsResidentCell is " + this.mIsResidentCell + " mIsSubway is " + this.mIsSubway + "mLastStartDataStallTime is " + this.mLastStartDataStallTime + "isBeginCollectCellData is " + SelfStudyControllerBase.isBeginCollectCellData + " mLastStopDataStallTime is " + this.mLastStopDataStallTime + " current time is " + SystemClock.elapsedRealtime());
        if (MiuiPhoneUtils.isSupportMtkDataOptimization() && isLteOrNr() && this.mIsMobileDataOn && this.mIsScreenOn && (((i2 = this.dataType) == 4 || i2 == 2) && !MiuiPhoneUtils.isDuringCall())) {
            if (i == 0) {
                boolean zHasMessages = this.mWorkHandler.hasMessages(1045);
                Handler handler = this.mWorkHandler;
                if (zHasMessages) {
                    handler.removeMessages(1045);
                    return;
                } else {
                    handler.sendMessageDelayed(handler.obtainMessage(1045), 3000L);
                    this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$processVideoQualityChanged$8();
                        }
                    }, 30000L);
                }
            } else if (i == 1) {
                processDataStallAppStatistic(false, 3);
                this.mIsStreamStall = false;
                if (this.mWorkHandler.hasMessages(1041)) {
                    this.mWorkHandler.removeMessages(1041);
                }
                if (this.mWorkHandler.hasMessages(1045)) {
                    this.mWorkHandler.removeMessages(1045);
                }
                if (this.mWorkHandler.hasMessages(1053)) {
                    logV("remove EVENT_LONG_TIME_STALL_TYPE For lastQuality == 1.");
                    this.mWorkHandler.removeMessages(1053);
                }
                stopToCollectCellData(this.mIsWorkDay, 3);
            }
        }
        if (this.isVideoFront && this.dataType == 8 && i != 1 && i == 0 && !this.hasSendDataStallAction && isLteOrNr() && this.mIsMobileDataOn && this.mIsScreenOn) {
            if (this.mLastStartDataStallTime == this.mLastStopDataStallTime || Math.abs(SystemClock.elapsedRealtime() - this.mLastStopDataStallTime) >= RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
                Handler handler2 = this.mWorkHandler;
                handler2.sendMessage(handler2.obtainMessage(SmartNetworkRelayScore.TRAFFIC_UNIT, 4));
                recordCount("webPageShortVideoOptCnt", "video");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processVideoQualityChanged$8() {
        if (SelfStudyControllerBase.isBeginCollectCellData && 3 == this.mCollectActionType) {
            logV("stop for video stall 30s");
            processDataStallAppStatistic(false, 3);
            stopToCollectCellData(this.mIsWorkDay, 3);
        }
    }

    public void beginDataStall(int i) {
        if (isVideoDataStallOptimizationSupported()) {
            String[] dataStallCmds = getDataStallCmds(i);
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (dataStallCmds == null || activeDataPhone == null) {
                return;
            }
            for (String str : dataStallCmds) {
                if (!TextUtils.isEmpty(str)) {
                    String str2 = "MIPC_SET_DATA_STALL_OPT," + str;
                    log("start result is sb ".concat(str2));
                    FiveGManagerBase.getInstance().sendAtCommand(activeDataPhone, str2.split(","), (Message) null);
                }
            }
            stopDataStallOnNddsPhoneId(activeDataPhone.getPhoneId() == 0 ? 1 : 0);
        }
    }

    public void stopDataStall(int i, int i2) {
        if (isVideoDataStallOptimizationSupported()) {
            log("stopDataStall mode = " + i);
            String str = "MIPC_SET_DATA_STALL_OPT," + String.valueOf(i) + ",0,0," + String.valueOf(i2) + ",0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
            log("stop result is sb ".concat(str));
            String[] strArrSplit = str.split(",");
            if (MiuiPhoneUtils.getActiveDataPhone() != null) {
                FiveGManagerBase.getInstance().sendAtCommand(MiuiPhoneUtils.getActiveDataPhoneId(), strArrSplit, (Message) null);
            }
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void beginStreamDataStall(int i, int i2) {
        if (shouldProcessDataStall(i, i2)) {
            logDataStallInfo(i, i2);
            String[] defaultCommands = getDefaultCommands();
            if (shouldHandleSpecialCases(i, i2, defaultCommands)) {
                recordAndExecuteCommands(i2, defaultCommands);
            }
        }
    }

    private boolean shouldProcessDataStall(int i, int i2) {
        if (MiuiPhoneUtils.isSupportMtkDataOptimization() && isVideoDataStallOptimizationSupported() && this.cloudStreamMediaOn) {
            return (i == 2 || i == 4) && !isNeedSkipOperation();
        }
        return false;
    }

    private void logDataStallInfo(int i, int i2) {
        logV("beginStreamDataStall sceneType=" + i + " ,reason=" + i2 + " ,hasSendDlDataStallAction = " + this.hasSendDlDataStallAction + " ,mIsNeedResetDl = " + this.mIsNeedResetDl + " ,hasSendColdDlDataStallAction = " + this.hasSendColdDlDataStallAction + " ,hasSendDlDataStallActionForLongTime = " + this.hasSendDlDataStallActionForLongTime + " ,mIsNeedResetDlForLongTime = " + this.mIsNeedResetDlForLongTime);
    }

    private String[] getDefaultCommands() {
        return new String[]{"1,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,50,500,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,50,500,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    }

    private boolean shouldHandleSpecialCases(int i, int i2, String[] strArr) {
        String topAppName = getTopAppName();
        boolean zIsDouYinApp = isDouYinApp(topAppName);
        logV("shouldHandleSpecialCases topAppName:" + topAppName + " ,isDouYin:" + zIsDouYinApp);
        if (i2 == 0) {
            return handleStallReason(i, zIsDouYinApp, strArr);
        }
        if (i2 == 1) {
            return handleColdStartReason(strArr);
        }
        if (i2 != 2) {
            return false;
        }
        return handleLongTimeReason(i, zIsDouYinApp, strArr);
    }

    private boolean isDouYinApp(String str) {
        return (str == null || str.isEmpty() || !str.equals(HyperOSCustFeatureResolve.getString("config_data_stall_short_video_app_packages_v1", ""))) ? false : true;
    }

    private boolean handleStallReason(int i, boolean z, String[] strArr) {
        if ((this.hasSendDlDataStallAction && !this.mIsNeedResetDl) || this.hasSendColdDlDataStallAction) {
            return false;
        }
        this.mIsModForDouYin = false;
        if (currentIsNonPeak() && z && !this.mIsNeedResetDl) {
            setDouYinNonPeakCommands(strArr);
            this.mIsModForDouYin = true;
        }
        if (this.mIsNeedResetDl) {
            this.mIsModForDouYin = false;
        }
        if (2 == i) {
            setLiveStreamingCommands(strArr);
        }
        return true;
    }

    private void setDouYinNonPeakCommands(String[] strArr) {
        strArr[0] = "1,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,50,500,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
        strArr[1] = "1,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,50,500,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
    }

    private void setLiveStreamingCommands(String[] strArr) {
        strArr[0] = "1,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,10,128,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
        strArr[1] = "1,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,10,128,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
    }

    private boolean handleColdStartReason(String[] strArr) {
        if (this.hasSendColdDlDataStallAction) {
            return false;
        }
        strArr[0] = "1,0,0,0,8000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,5000,0,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
        strArr[1] = "1,0,0,1,8000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,5000,0,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
        return true;
    }

    private boolean handleLongTimeReason(int i, boolean z, String[] strArr) {
        if (i != 4 || (this.hasSendDlDataStallActionForLongTime && !this.mIsNeedResetDlForLongTime)) {
            return false;
        }
        this.mIsModForDouYinForLongTime = false;
        if (this.mIsNeedResetDlForLongTime) {
            return true;
        }
        if (!currentIsNonPeak() && z) {
            setDouYinNonPeakCommands(strArr);
            this.mIsModForDouYinForLongTime = true;
        }
        return this.mIsModForDouYinForLongTime;
    }

    private void recordAndExecuteCommands(int i, String[] strArr) {
        recordStreamModes(i);
        executeCommand(strArr, i);
    }

    public void stopStreamDataStall(int i) {
        if (MiuiPhoneUtils.isSupportMtkDataOptimization() && isVideoDataStallOptimizationSupported() && this.cloudStreamMediaOn) {
            logV("stopStreamDataStall reason:" + i);
            resetHasSendDlDataStallActionForReason(i);
            handleStopCommandsForReason(i);
        }
    }

    private void recordStreamModes(int i) {
        log("recordStreamModes reason:" + i);
        int[] iArr = {1};
        if (i == 1) {
            addModesToSets(this.lastColdLteStreamModes, this.lastColdNrStreamModes, iArr);
        } else if (i == 2) {
            addModesToSets(this.lastLongLteStreamModes, this.lastLongNrStreamModes, iArr);
        } else {
            addModesToSets(this.lastLteStreamModes, this.lastNrStreamModes, iArr);
        }
    }

    private void addModesToSets(ArrayList arrayList, ArrayList arrayList2, int[] iArr) {
        if (arrayList == null || arrayList2 == null || iArr == null) {
            return;
        }
        int[] iArr2 = (int[]) iArr.clone();
        arrayList.add(iArr2);
        arrayList2.add((int[]) iArr2.clone());
    }

    private void executeCommand(String[] strArr, int i) {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        logV("executeCommand ddsPhone = " + activeDataPhone + ", reason = " + i + ", command = " + Arrays.toString(strArr));
        if (activeDataPhone == null) {
            return;
        }
        handleReasonSpecificLogic(i);
        executeAtCommands(strArr, i, activeDataPhone);
        stopDataStallOnNddsPhone(activeDataPhone.getPhoneId());
    }

    private void handleReasonSpecificLogic(int i) {
        if (i == 0) {
            handleStallReason();
        } else if (i == 1) {
            handleColdStartReason();
        } else {
            if (i != 2) {
                return;
            }
            handleLongTimeReason();
        }
    }

    private void handleStallReason() {
        if (this.mIsNeedResetDl) {
            this.mIsNeedResetDl = false;
            this.hasSendDlDataStallAction = false;
        } else {
            this.hasSendDlDataStallAction = true;
        }
    }

    private void handleColdStartReason() {
        this.hasSendColdDlDataStallAction = true;
        if (this.mAutoStopColdDataStallTask == null) {
            Runnable runnable = new Runnable() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$handleColdStartReason$9();
                }
            };
            this.mAutoStopColdDataStallTask = runnable;
            this.mWorkHandler.postDelayed(runnable, 3000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleColdStartReason$9() {
        log("force stop DataStall for ColdStart.");
        stopStreamDataStall(1);
        this.mAutoStopColdDataStallTask = null;
    }

    private void handleLongTimeReason() {
        if (this.mIsNeedResetDlForLongTime) {
            this.mIsNeedResetDlForLongTime = false;
            this.hasSendDlDataStallActionForLongTime = false;
        } else {
            this.hasSendDlDataStallActionForLongTime = true;
        }
    }

    private void executeAtCommands(String[] strArr, int i, Phone phone) {
        for (String str : strArr) {
            if (!TextUtils.isEmpty(str)) {
                String str2 = "MIPC_SET_DATA_STALL_OPT," + str;
                log("executeCommand:" + str2 + " ,reason = " + i);
                FiveGManagerBase.getInstance().sendAtCommand(phone, str2.split(","), (Message) null);
            }
        }
    }

    private void stopDataStallOnNddsPhone(int i) {
        stopDataStallOnNddsPhoneId(i == 0 ? 1 : 0);
    }

    private void resetHasSendDlDataStallActionForReason(int i) {
        logV("resetHasSendDlDataStallActionForReason reason=" + i + " ,hasSendDlDataStallAction = " + this.hasSendDlDataStallAction + " ,mIsModForDouYin = " + this.mIsModForDouYin + " ,hasSendColdDlDataStallAction = " + this.hasSendColdDlDataStallAction + " ,hasSendDlDataStallActionForLongTime = " + this.hasSendDlDataStallActionForLongTime + " ,mIsModForDouYinForLongTime = " + this.mIsModForDouYinForLongTime);
        if (i == 0) {
            if (this.hasSendDlDataStallAction) {
                this.hasSendDlDataStallAction = false;
                this.mIsModForDouYin = false;
                return;
            }
            return;
        }
        if (1 == i) {
            if (this.hasSendColdDlDataStallAction) {
                this.hasSendColdDlDataStallAction = false;
            }
        } else if (2 == i && this.hasSendDlDataStallActionForLongTime) {
            this.hasSendDlDataStallActionForLongTime = false;
            this.mIsModForDouYinForLongTime = false;
        }
    }

    private void handleStopCommandsForReason(int i) {
        if (shouldSendExitCommands(i)) {
            log("handleStopCommandsForReason sendExitCommands reason=" + i);
            sendExitCommands();
            return;
        }
        log("handleStopCommandsForReason sendGeneratedStopCommands reason=" + i);
        sendGeneratedStopCommands(i);
    }

    private boolean shouldSendExitCommands(int i) {
        if (i == 0) {
            return checkStallConditions();
        }
        if (i == 1) {
            return checkColdStartConditions();
        }
        if (i != 2) {
            return false;
        }
        return checkLongTimeConditions();
    }

    private boolean checkStallConditions() {
        ArrayList arrayList = this.lastLteStreamModes;
        if (arrayList != null && !arrayList.isEmpty()) {
            return false;
        }
        ArrayList arrayList2 = this.lastNrStreamModes;
        return arrayList2 == null || arrayList2.isEmpty();
    }

    private boolean checkColdStartConditions() {
        ArrayList arrayList = this.lastColdLteStreamModes;
        if (arrayList != null && !arrayList.isEmpty()) {
            return false;
        }
        ArrayList arrayList2 = this.lastColdNrStreamModes;
        return arrayList2 == null || arrayList2.isEmpty();
    }

    private boolean checkLongTimeConditions() {
        ArrayList arrayList = this.lastLongLteStreamModes;
        if (arrayList != null && !arrayList.isEmpty()) {
            return false;
        }
        ArrayList arrayList2 = this.lastLongNrStreamModes;
        return arrayList2 == null || arrayList2.isEmpty();
    }

    private void sendGeneratedStopCommands(int i) {
        Iterator it = generateCommands(i).iterator();
        while (it.hasNext()) {
            sendCommand((String) it.next());
        }
    }

    private void sendExitCommands() {
        String[] strArr = {"1,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
        for (int i = 0; i < 2; i++) {
            String str = strArr[i];
            if (!TextUtils.isEmpty(str)) {
                sendCommand("MIPC_SET_DATA_STALL_OPT," + str);
            }
        }
    }

    private void sendCommand(String str) {
        log("sendCommand=" + str);
        if (str == null || TextUtils.isEmpty(str) || MiuiPhoneUtils.getActiveDataPhone() == null) {
            return;
        }
        log("sendCommand stop result is sb ".concat(str));
        FiveGManagerBase.getInstance().sendAtCommand(MiuiPhoneUtils.getActiveDataPhoneId(), str.split(","), (Message) null);
    }

    private void processCellChangeInHomeScene(int i) {
        OffsetCell offsetCell;
        MtkSelfStudyController mtkSelfStudyController;
        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
            return;
        }
        logV("processCellChangeInHomeScene mNotifySceneType:" + this.mNotifySceneType + " , mStreamMediaOptimizationRst:" + this.mStreamMediaOptimizationRst + " , mIsNeedRecordForInHome:" + this.mIsNeedRecordForInHome + " , oldStreamOffSetCell:" + this.oldStreamOffSetCell);
        if (this.mNotifySceneType != 3 || (offsetCell = this.oldStreamOffSetCell) == null) {
            return;
        }
        if (this.mStreamMediaOptimizationRst || !this.mIsNeedRecordForInHome) {
            mtkSelfStudyController = this;
        } else {
            mtkSelfStudyController = this;
            mtkSelfStudyController.insertOrUpdateHomeStreamBadCellDatabase(offsetCell.globalCellId, this.oldStreamPci, this.oldStreamFreq, 1, i);
        }
        if (mtkSelfStudyController.isMatchedHomeStreamMediaBadCell(mtkSelfStudyController.oldStreamOffSetCell.globalCellId) && mtkSelfStudyController.mStreamMediaOptimizationRst) {
            if (mtkSelfStudyController.mLastDownRatTimeForHome > 0 && System.currentTimeMillis() - mtkSelfStudyController.mLastDownRatTimeForHome < 600000) {
                mtkSelfStudyController.log("processCellLocationChanged ignore For DOWN_RAT_TIME_INTERVAL");
            } else {
                mtkSelfStudyController.enableSaForStreamMedia(false);
                mtkSelfStudyController.mLastDownRatTimeForHome = System.currentTimeMillis();
            }
        }
    }

    public void doActionForManualCellMap() {
        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
            return;
        }
        ManualSubCellInfo manualCellInfoByGci = getManualCellInfoByGci();
        logV("doActionForManualCellMap info = " + manualCellInfoByGci + " ,dataType = " + this.dataType + " , mManualSubCellInfoByGci1 = " + this.mManualSubCellInfoByGci1);
        if (manualCellInfoByGci != null && isMatchPeakFromManualCellMap(manualCellInfoByGci.time_extra) && manualCellInfoByGci.getType() == 0) {
            int service2 = manualCellInfoByGci.getService2();
            int i = this.dataType;
            if (service2 == i) {
                if (this.mManualSubCellInfoByGci1) {
                    sendSelfStudyRequest(103, 5, new int[]{i, 0, manualCellInfoByGci.getFreq2(), manualCellInfoByGci.getPci2(), manualCellInfoByGci.getRat2()});
                } else {
                    Handler handler = this.mWorkHandler;
                    handler.sendMessage(handler.obtainMessage(1055));
                }
            }
        }
    }

    public void doActionForCellMap() {
        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
            return;
        }
        boolean zIsCityFoundInCellMap = isCityFoundInCellMap();
        SubCellInfo subCellInfo = getSubCellInfo();
        logV("doActionForCellMap isCityMatch = " + zIsCityFoundInCellMap + " , currentSubCellInfo = " + subCellInfo);
        if (subCellInfo != null && zIsCityFoundInCellMap && isNeedDoActionForCellMap(subCellInfo)) {
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone == null) {
                log("return doActionForCellMap for dataPhone is null.");
                return;
            }
            OffsetCell currentCellInfo = getCurrentCellInfo(activeDataPhone);
            logV("doActionForCellMap offsetCell = " + currentCellInfo);
            if (currentCellInfo != null && currentCellInfo.pci == subCellInfo.pci && currentCellInfo.freq == subCellInfo.freq) {
                currentCellInfo.offset = -12;
                handleLiveStreamingOffsetInd(currentCellInfo, activeDataPhone.getPhoneId());
                log("doActionForCellMap offset.");
            }
        }
    }

    private boolean isMatchPeakFromManualCellMap(int i) {
        logV("isMatchPeakFromManualCellMap state = " + i + " ,mIsWorkDay = " + this.mIsWorkDay + ",mIsSubway = " + this.mIsSubway);
        return i == ((!this.mIsWorkDay || !this.mIsSubway || currentIsNonPeak()) ? 0 : 1);
    }

    private boolean isNeedDoActionForCellMap(SubCellInfo subCellInfo) {
        logV("isNeedDoActionForCellMap subCellInfo = " + subCellInfo);
        boolean z = false;
        if (subCellInfo != null) {
            logV("isNeedDoActionForCellMap isSubwayScene = " + isSubwayScene());
            if (String.valueOf(this.dataType).equals(subCellInfo.service) && isSubwayScene() && "0".equals(subCellInfo.scene) && isTimeInCellMap(subCellInfo) && isProjectTypeMatched(subCellInfo) && isUpdateTimeValid(subCellInfo)) {
                z = true;
            }
            log("isNeedDoActionForCellMap isNeed = " + z);
        }
        return z;
    }

    private List generateCommands(int i) {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3 = new ArrayList();
        if (i == 0) {
            arrayList = this.lastLteStreamModes;
            arrayList2 = this.lastNrStreamModes;
            arrayList.clear();
            this.lastNrStreamModes.clear();
        } else if (1 == i) {
            arrayList = this.lastColdLteStreamModes;
            arrayList2 = this.lastColdNrStreamModes;
            arrayList.clear();
            this.lastColdNrStreamModes.clear();
        } else {
            arrayList = this.lastLongLteStreamModes;
            arrayList2 = this.lastLongNrStreamModes;
            arrayList.clear();
            this.lastLongNrStreamModes.clear();
        }
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            for (int i3 : (int[]) obj) {
                arrayList3.add("MIPC_SET_DATA_STALL_OPT," + i3 + ",0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF");
            }
        }
        int size2 = arrayList2.size();
        int i4 = 0;
        while (i4 < size2) {
            Object obj2 = arrayList2.get(i4);
            i4++;
            for (int i5 : (int[]) obj2) {
                arrayList3.add("MIPC_SET_DATA_STALL_OPT," + i5 + ",0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF");
            }
        }
        return arrayList3;
    }

    public void recordDfsEvent(int i, Object... objArr) {
        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive() || objArr == null || objArr.length == 0) {
            return;
        }
        HashMap map = new HashMap();
        map.put("eventType", Integer.valueOf(i));
        if (i == 0 && objArr.length == 4) {
            map.put("source", objArr[0]);
            map.put("reason", objArr[1]);
            map.put("dataType", objArr[2]);
            map.put("txRate", Long.valueOf(getMobileTxRate(this.mContext)));
            map.put("stallType", objArr[3]);
        }
        map.put("time", getCurrentTimestamp());
        logV("recordDfsEvent params:" + map);
        MiuiTelephonyStatistic.getInstance().recordDFSEventStatistic(915700008, map);
    }

    public void stopDataStallOnNddsPhoneId(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        String[] dataStallCmds = getDataStallCmds(0);
        if (phone == null || dataStallCmds == null) {
            return;
        }
        for (String str : dataStallCmds) {
            String str2 = "MIPC_SET_DATA_STALL_OPT," + str;
            log("ndds phone stop result is sb ".concat(str2));
            FiveGManagerBase.getInstance().sendAtCommand(phone, str2.split(","), (Message) null);
        }
    }

    private void handleEnterGameStopSearchNw() {
        Phone phone;
        String topAppName = getTopAppName();
        if (!SelfStudyControllerBase.INVALID_SIM_OPT_WHITELIST_PACKAGES.contains(topAppName)) {
            log("handleEnterGameStopSearchNw, skip, topApp=" + topAppName + " not in whitelist");
            return;
        }
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null || !MiuiPhoneUtils.isValidatePhoneId(activeDataPhone.getPhoneId())) {
            log("handleEnterGameStopSearchNw, skip, invalid ddsPhone");
            return;
        }
        int phoneId = activeDataPhone.getPhoneId();
        int i = phoneId == 0 ? 1 : 0;
        boolean zIsInValidSim = SpecialSimManager.getInstance().isInValidSim(i);
        log("handleEnterGameStopSearchNw, ddsPhoneId=" + phoneId + ", nddsPhoneId=" + i + ", isInValidSim=" + zIsInValidSim + " topApp: " + topAppName);
        if (!zIsInValidSim || (phone = PhoneFactory.getPhone(i)) == null) {
            return;
        }
        log("handleEnterGameStopSearchNw, send stop search cmd on nddsPhoneId=" + i);
        FiveGManagerBase.getInstance().sendAtCommand(phone, new String[]{"MIPC_SET_NW_REQ", "0", "1", "1", "0"}, (Message) null);
        this.mWorkHandler.removeMessages(1057);
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1057), 1200000L);
    }

    private void handleGameStopSearchNwGuard() {
        String topAppName = getTopAppName();
        if (!SelfStudyControllerBase.INVALID_SIM_OPT_WHITELIST_PACKAGES.contains(topAppName)) {
            log("handleGameStopSearchNwGuard, topApp=" + topAppName + " not in whitelist, exit optimization");
            handleExitGameStartSearchNw();
            return;
        }
        log("handleGameStopSearchNwGuard, topApp=" + topAppName + " still in whitelist, reschedule");
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1057), 1200000L);
    }

    private void handleExitGameStartSearchNw() {
        this.mWorkHandler.removeMessages(1057);
        for (int i = 0; i < 2; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            log("handleExitGameStartSearchNw, phoneId=" + i + ", phone=" + phone);
            if (phone != null) {
                log("handleExitGameStartSearchNw, send start search cmd on phoneId=" + i);
                FiveGManagerBase.getInstance().sendAtCommand(phone, new String[]{"MIPC_SET_NW_REQ", "0", "1", "0", "0"}, (Message) null);
            }
        }
    }

    private String[] getDataStallCmds(int i) {
        String str = FiveGManagerBase.sIsSaModeCarrierDisabled ? null : "4,4,0,0,20000,300,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
        String str2 = FiveGManagerBase.sIsSaModeCarrierDisabled ? null : "4,4,0,1,20000,300,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
        String str3 = FiveGManagerBase.sIsSaModeCarrierDisabled ? null : "4,4,0,0,20000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
        String str4 = FiveGManagerBase.sIsSaModeCarrierDisabled ? null : "4,4,0,1,20000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF";
        if (i == 0) {
            return new String[]{"0,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "3,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "4,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "0,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "3,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "4,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
        }
        if (i == 1) {
            return new String[]{"0,1,0,0,5000,300,300,800,0xFF,90,0x7F,-110,0,-110,0,0xFF,0xFF,64,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,2000", "1,0,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-118,-5,0xFF,0xFF,150,6,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-110,-5,50,50,0xFFFFFFFF,0xFFFFFFFF,40,40,0xFFFFFFFF", "3,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,26,-100,0,-110,-5,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", str, "0,1,0,1,5000,300,300,800,0xFF,90,0x7F,-110,0,-110,0,0xFF,0xFF,64,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,2000", "1,0,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-118,-5,0xFF,0xFF,150,6,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-110,-5,50,50,0xFFFFFFFF,0xFFFFFFFF,40,40,0xFFFFFFFF", "3,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,26,-100,0,-110,-5,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", str2};
        }
        if (i == 2) {
            return new String[]{"0,1,0,0,5000,300,300,10000,0xFF,0xFF,0x7F,-105,0,-105,0,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,10000", "2,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,60,60,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "3,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-105,0,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", MiuiPhoneUtils.isSupportMtkDataOptimization() ? str3 : str, "0,1,0,1,5000,300,300,10000,0xFF,0xFF,0x7F,-105,0,-105,0,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,10000", "2,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,60,60,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "3,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-105,0,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", MiuiPhoneUtils.isSupportMtkDataOptimization() ? str4 : str2};
        }
        if (i != 3) {
            if (i != 4) {
                return null;
            }
            return new String[]{"0,1,0,0,5000,300,300,10000,0xFF,0xFF,0x7F,-110,-3,-105,0,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,10000", "2,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,50,50,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "3,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-105,0,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", MiuiPhoneUtils.isSupportMtkDataOptimization() ? str3 : str, "0,1,0,1,5000,300,300,10000,0xFF,0xFF,0x7F,-110,-3,-105,0,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,10000", "2,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,50,50,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "3,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-105,0,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", MiuiPhoneUtils.isSupportMtkDataOptimization() ? str4 : str2};
        }
        return new String[]{"0,3,0,0,3000,300,3000,11000,49,33,0x7F,-106,0,-106,0,0xFF,0xFF," + this.ulStallUlTput + "," + this.ulStallDlTput + "," + this.ulAllocRate + ",0xFFFFFFFF," + this.ulStallUlPendData, "0,3,0,1,3000,300,3000,11000,24,33,0x7F,-106,0,-106,0,0xFF,0xFF," + this.ulStallUlTput + "," + this.ulStallDlTput + "," + this.ulAllocRate + ",0xFFFFFFFF," + this.ulStallUlPendData, "2,3,0,0,4000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,-106,-10,-106,0,50,50,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "3,3,0,0,4000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-106,0,25,25,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    }

    public void setSbpParam(boolean z) {
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        log("setSbpParam  isReset = " + z);
        if (!z) {
            ArrayList arrayList = new ArrayList(Arrays.asList("SBP_NR_POSTPONE_A2_FOR_B1B2", "SBP_ENDC_PREFER_SEARCH", "SBP_ERRC_ENDC_CELL_OFFSET_OPTIMIZATION", "SBP_ERRC_SKIP_EVT_OFFSET_ENH_FOR_NBR_ENDC_CELL", "SBP_OOS_HIGH_RAT_OPT", "SBP_MODEM_54G_INDOOR_MODE"));
            if (this.dataType == 4 && this.mIsSubway && !SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                logV("add OPT-SBP for shortVideo and subway.");
                arrayList.add("SBP_ENABLE_PINGPONG_BAR_NR");
                arrayList.add("SBP_NR_FAKE_RI_ENABLED");
            }
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                String str = (String) obj;
                Object[] objArr = new Object[1];
                if (FiveGManagerBase.getInstance().atCmdRunSync(activeDataPhoneId, constructionAtCmd(str, 1, 0, 0), objArr)) {
                    parseSbpResponse(objArr[0], str, activeDataPhoneId);
                }
            }
            return;
        }
        ConcurrentHashMap concurrentHashMap = this.mMtkSbpHistoryStats;
        if (concurrentHashMap == null || concurrentHashMap.size() <= 0) {
            return;
        }
        for (String str2 : this.mMtkSbpHistoryStats.keySet()) {
            sendAtCmd(constructionAtCmd(str2, 2, 0, ((Integer) this.mMtkSbpHistoryStats.get(str2)).intValue()), (Message) null, activeDataPhoneId);
        }
        this.mMtkSbpHistoryStats.clear();
    }

    private void parseSbpResponse(Object obj, String str, int i) {
        try {
            String[] strArr = (String[]) obj;
            if (strArr == null || strArr.length <= 0) {
                return;
            }
            int i2 = Integer.parseInt(strArr[0].split(":")[1].trim());
            log("parseSbpResponse " + str + ": val = " + i2);
            if (!"SBP_NR_POSTPONE_A2_FOR_B1B2".equals(str)) {
                if (i2 == 1) {
                    this.mMtkSbpHistoryStats.put(str, Integer.valueOf(i2));
                    sendAtCmd(constructionAtCmd(str, 2, 0, 0), (Message) null, i);
                    return;
                } else {
                    log(str + " : current status is same,  does not need to be changed.");
                    return;
                }
            }
            if (i2 == 0 && this.dataType != 3) {
                this.mMtkSbpHistoryStats.put(str, Integer.valueOf(i2));
                sendAtCmd(constructionAtCmd(str, 2, 0, 1), (Message) null, i);
            } else {
                log(str + " : current status is same,  does not need to be changed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMobileDataSettingName() {
        return MiuiPhoneUtils.PHONE_COUNT > 1 ? "mobile_data0" : "mobile_data";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMobileDataSettingOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), getMobileDataSettingName(), 0) != 0;
    }

    private void handleLiveStreamingOffsetInd(OffsetCell offsetCell, int i) {
        if (offsetCell == null) {
            return;
        }
        int residentCellSize = getResidentCellSize();
        if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
            if (residentCellSize > 5 && this.isResidentCell) {
                log("live stream optimization cells more than MAX_STREAM_OFFSET_CELL_NUM, ignore");
                return;
            }
            offsetCell.setResidentCell(this.isResidentCell);
        }
        if (!this.mLiveStreamingOffsetCells.contains(offsetCell)) {
            handleNewOffsetCell(offsetCell, i, residentCellSize);
        } else {
            handleExistingOffsetCell(offsetCell, i);
        }
    }

    private void handleNewOffsetCell(OffsetCell offsetCell, int i, int i2) {
        try {
            if (MiuiPhoneUtils.isSupportMtkDataOptimization()) {
                logV("handleNewOffsetCell offsetCell:" + offsetCell);
                if (i2 >= 5 && offsetCell.isResidentCell) {
                    if (i2 == 5) {
                        updateLiveStreamingOffsetCells(i, offsetCell);
                        return;
                    }
                    return;
                }
                addOffsetCellAndAction(offsetCell, i);
                return;
            }
            addOffsetCellAndAction(offsetCell, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOffsetCellAndAction(OffsetCell offsetCell, int i) {
        try {
            this.mLiveStreamingOffsetCells.add(offsetCell);
            doCellOffsetAction(false, offsetCell.pci, offsetCell.freq, offsetCell.rat, offsetCell.offset, offsetCell.plmn, i);
            recordCount("liveStreamingOptCnt", "video");
            this.isVideoOpt = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleExistingOffsetCell(OffsetCell offsetCell, int i) {
        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
            return;
        }
        logV("handleExistingOffsetCell offsetCell:" + offsetCell);
        try {
            for (OffsetCell offsetCell2 : this.mLiveStreamingOffsetCells) {
                if (Objects.equals(offsetCell.globalCellId, offsetCell2.globalCellId) && offsetCell.offset * offsetCell2.offset <= 0) {
                    this.logV("handleExistingOffsetCell tempOffsetCell:" + offsetCell2);
                    this.mLiveStreamingOffsetCells.remove(offsetCell2);
                    this.mLiveStreamingOffsetCells.add(offsetCell);
                    this.doCellOffsetAction(false, offsetCell.pci, offsetCell.freq, offsetCell.rat, offsetCell.offset, offsetCell.plmn, i);
                    return;
                }
                this = this;
                i = i;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized int getResidentCellSize() {
        int i;
        List list = this.mLiveStreamingOffsetCells;
        i = 0;
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (((OffsetCell) it.next()).isResidentCell()) {
                    i++;
                }
            }
        }
        return i;
    }

    public void updateLiveStreamingOffsetCellScore(OffsetCell offsetCell, long j) {
        if (this.mLiveStreamingOffsetCells.contains(offsetCell)) {
            offsetCell.setScore((int) j);
            this.mLiveStreamingOffsetCells.set(this.mLiveStreamingOffsetCells.indexOf(offsetCell), offsetCell);
        }
    }

    public void updateLiveStreamingOffsetCells(int i, OffsetCell offsetCell) {
        MtkSelfStudyController mtkSelfStudyController;
        this.mLiveStreamingOffsetCells.sort(new Comparator(this) { // from class: com.android.phone.MtkSelfStudyController.3
            @Override // java.util.Comparator
            public int compare(OffsetCell offsetCell2, OffsetCell offsetCell3) {
                return offsetCell2.getScore() - offsetCell3.getScore();
            }
        });
        List list = this.mLiveStreamingOffsetCells;
        if (list == null || list.isEmpty()) {
            mtkSelfStudyController = this;
        } else {
            mtkSelfStudyController = this;
            mtkSelfStudyController.doCellOffsetAction(true, ((OffsetCell) this.mLiveStreamingOffsetCells.get(0)).pci, ((OffsetCell) this.mLiveStreamingOffsetCells.get(0)).freq, ((OffsetCell) this.mLiveStreamingOffsetCells.get(0)).rat, ((OffsetCell) this.mLiveStreamingOffsetCells.get(0)).offset, ((OffsetCell) this.mLiveStreamingOffsetCells.get(0)).plmn, i);
        }
        mtkSelfStudyController.mLiveStreamingOffsetCells.add(offsetCell);
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public boolean hasTriggeredOffsetOptimizationForCurrentCell() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            return true;
        }
        String globalCellId = activeDataPhone.getCurrentCellIdentity().getGlobalCellId();
        if (TextUtils.isEmpty(globalCellId)) {
            return true;
        }
        Iterator it = this.mGameOffsetCells.iterator();
        while (it.hasNext()) {
            if (globalCellId.equals(((OffsetCell) it.next()).globalCellId)) {
                return true;
            }
        }
        return false;
    }

    private void handleGameOffsetInd(OffsetCell offsetCell, int i, int i2) {
        if (this.mIsIgnoreGameStuck || this.mGameOffsetCells.contains(offsetCell)) {
            return;
        }
        this.mGameOffsetCells.add(offsetCell);
        doCellOffsetAction(false, offsetCell.pci, offsetCell.freq, offsetCell.rat, offsetCell.offset, offsetCell.plmn, i);
        this.mSmartNetworkGameController.notifyModemOffsetInd(i2, offsetCell.offset, 0, this.mGameDataStallTime);
    }

    private void handleAiGameOffsetInd(OffsetCell offsetCell, int i) {
        if (this.mAiGameOffsetCells.size() > 50) {
            log("ai game optimization cells more than 50, ignore");
        } else {
            if (this.mIsIgnoreGameStuck || this.mGameOffsetCells.contains(offsetCell) || this.mAiGameOffsetCells.contains(offsetCell)) {
                return;
            }
            this.mAiGameOffsetCells.add(offsetCell);
            doCellOffsetAction(false, offsetCell.pci, offsetCell.freq, offsetCell.rat, offsetCell.offset, offsetCell.plmn, i);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void resetGameOffsetOptimization() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            return;
        }
        for (OffsetCell offsetCell : this.mGameOffsetCells) {
            doCellOffsetAction(true, offsetCell.pci, offsetCell.freq, offsetCell.rat, offsetCell.offset, offsetCell.plmn, activeDataPhone.getPhoneId());
        }
        this.mGameOffsetCells.clear();
        for (OffsetCell offsetCell2 : this.mAiGameOffsetCells) {
            doCellOffsetAction(true, offsetCell2.pci, offsetCell2.freq, offsetCell2.rat, offsetCell2.offset, offsetCell2.plmn, activeDataPhone.getPhoneId());
        }
        this.mAiGameOffsetCells.clear();
    }

    protected void handleExitLiveStreamingApps() {
        List offsetCellsForTable;
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            return;
        }
        for (OffsetCell offsetCell : this.mLiveStreamingOffsetCells) {
            doCellOffsetAction(true, offsetCell.pci, offsetCell.freq, offsetCell.rat, offsetCell.offset, offsetCell.plmn, activeDataPhone.getPhoneId());
        }
        this.mLiveStreamingOffsetCells.clear();
        SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
        if (selfStudyDatabaseHelper == null || (offsetCellsForTable = selfStudyDatabaseHelper.getOffsetCellsForTable()) == null || offsetCellsForTable.isEmpty()) {
            return;
        }
        this.dbHelper.clearOffsetCellTable();
    }

    private void offsetForModemAi(Phone phone, int i, int i2, int i3, int i4) {
        Object[] objArr = {Integer.valueOf(phone.getPhoneId()), String.valueOf(i3), Integer.valueOf(i4 != 1 ? 0 : 1), phone.getOperatorNumeric(), Integer.valueOf(i), Integer.valueOf(i2)};
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).teleAction(2, TelephonyActionBase.ActionReason.SELF_STUDY.value(), objArr);
        this.mModemAiOffsetList.add(objArr);
    }

    private void revertForModemAi() {
        ArrayList arrayList = this.mModemAiOffsetList;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).revertTeleAction(2, TelephonyActionBase.ActionReason.SELF_STUDY.value(), (Object[]) obj);
        }
    }

    public void queryGameBadCellInfo(String str) {
        String[] strArr = {"freq", "pci", "rat", "gci", "update_time_mills"};
        long jQueryLatestGameCellTime = queryLatestGameCellTime(str) - 1200000;
        String[] strArr2 = {"1", String.valueOf(jQueryLatestGameCellTime)};
        String[] strArr3 = {"2", String.valueOf(jQueryLatestGameCellTime)};
        Cursor cursorQuery = this.dbHelper.query(str, strArr, "rat = ? AND update_time_mills > ?", strArr2, null, null, "average_game_data_lag_ratio ASC");
        Cursor cursorQuery2 = this.dbHelper.query(str, strArr, "rat = ? AND update_time_mills > ?", strArr3, null, null, "average_game_data_lag_ratio ASC");
        int count = cursorQuery != null ? cursorQuery.getCount() : 0;
        int count2 = cursorQuery2 != null ? cursorQuery2.getCount() : 0;
        if (count <= 0 && count2 <= 0) {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            if (cursorQuery2 != null) {
                cursorQuery2.close();
                return;
            }
            return;
        }
        log("queryGameBadCellInfo rat =  lteCount = " + count + " nrCount = " + count2);
        int stageCount = setStageCount(count2);
        getGameOffset(cursorQuery, setStageCount(count), count, true);
        getGameOffset(cursorQuery2, stageCount, count2, false);
    }

    private int setStageCount(int i) {
        if (i < 3) {
            return 1;
        }
        int i2 = i / 3;
        return i % 3 > 1 ? i2 + 1 : i2;
    }

    private void getGameOffset(Cursor cursor, int i, int i2, boolean z) {
        ConcurrentHashMap concurrentHashMap;
        log("getGameOffset StageCount = " + i + " count = " + i2 + " isLte = " + z);
        if (i2 <= 0) {
            if (cursor != null) {
                cursor.close();
                return;
            }
            return;
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int i3 = 0;
                while (!cursor.isAfterLast()) {
                    int i4 = cursor.getInt(0);
                    int i5 = cursor.getInt(1);
                    String str = cursor.getString(3) + "-" + i5 + "-" + i4;
                    new ConcurrentHashMap();
                    if (z) {
                        concurrentHashMap = this.mLteGameCellStats;
                    } else {
                        concurrentHashMap = this.mNrGameCellStats;
                    }
                    if (i3 < i) {
                        concurrentHashMap.put(str, Integer.valueOf(this.mstage1));
                    } else if (i3 < i * 2) {
                        concurrentHashMap.put(str, Integer.valueOf(this.mstage2));
                    } else {
                        concurrentHashMap.put(str, Integer.valueOf(this.mstage3));
                    }
                    i3++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
    }

    class GameCellDdsSaOpt {
        GameCellDdsSaOpt() {
        }

        public void updateActionPhase(int i) {
            if (MtkSelfStudyController.this.isEnterGame) {
                MtkSelfStudyController mtkSelfStudyController = MtkSelfStudyController.this;
                if (mtkSelfStudyController.mIsScreenOn) {
                    mtkSelfStudyController.log("GameCellDdsSaOpt updateActionPhase phase = " + i);
                    int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
                    if (i == 1) {
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_NR_SKIP_RPT, (Message) null);
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.SET_NR_ERRC_THR_FOR_GAME, (Message) null);
                        return;
                    }
                    if (i == 2) {
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_NR_SKIP_RPT, (Message) null);
                        return;
                    }
                    if (i == 3) {
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_HIGH_PRIORITY_CELL_NR_A2, (Message) null);
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_HIGH_PRIORITY_CELL_NR_B2, (Message) null);
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_HIGH_PRIORITY_CELL_NR_B1, (Message) null);
                        return;
                    }
                    if (i == 4) {
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_HIGH_PRIORITY_CELL_NR_A2, (Message) null);
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_HIGH_PRIORITY_CELL_NR_B2, (Message) null);
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_HIGH_PRIORITY_CELL_NR_B1, (Message) null);
                    } else {
                        if (i != 5) {
                            return;
                        }
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_54G_INDOOR_MODE, (Message) null);
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.DISABLE_ELEVATOR_MODE, (Message) null);
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE, (Message) null);
                        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                            return;
                        }
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_NRRC_INTRA_RAT_BLIND_REDIRECT_BY_A2_ENH, (Message) null);
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_NR_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH, (Message) null);
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_LTE_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH, (Message) null);
                        MtkSelfStudyController.this.sendAtCmd(activeDataPhoneId, MtkAtCmd.ENABLE_SBP_DATA_AM_ERLCDL_REORDER_TIMER_OPT, (Message) null);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void tryRecoveryGameCell() {
            MtkSelfStudyController mtkSelfStudyController;
            int i = 0;
            while (true) {
                int i2 = MiuiPhoneUtils.PHONE_COUNT;
                mtkSelfStudyController = MtkSelfStudyController.this;
                if (i >= i2) {
                    break;
                }
                mtkSelfStudyController.sendAtCmd(i, MtkAtCmd.ENABLE_54G_INDOOR_MODE, (Message) null);
                MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.ENABLE_ELEVATOR_MODE, (Message) null);
                MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.DISABLE_SBP_DATA_ENPDCP_T_REORD_CUSTOMIZE, (Message) null);
                if (!SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                    MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.DISABLE_NRRC_INTRA_RAT_BLIND_REDIRECT_BY_A2_ENH, (Message) null);
                    MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.DISABLE_NR_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH, (Message) null);
                    MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.DISABLE_LTE_VIRTUAL_PATTERN_FOR_DUAL_SIM_OOS_RECOVERY_SEARCH, (Message) null);
                    MtkSelfStudyController.this.sendAtCmd(i, MtkAtCmd.DISABLE_SBP_DATA_AM_ERLCDL_REORDER_TIMER_OPT, (Message) null);
                }
                i++;
            }
            if (mtkSelfStudyController.mNrGameCellStats.size() > 0) {
                MtkSelfStudyController mtkSelfStudyController2 = MtkSelfStudyController.this;
                mtkSelfStudyController2.setOrResetGameOffset(mtkSelfStudyController2.mNrGameCellStats, 2, true);
            }
            if (MtkSelfStudyController.this.mLteGameCellStats.size() > 0) {
                MtkSelfStudyController mtkSelfStudyController3 = MtkSelfStudyController.this;
                mtkSelfStudyController3.setOrResetGameOffset(mtkSelfStudyController3.mLteGameCellStats, 1, true);
            }
            MtkSelfStudyController mtkSelfStudyController4 = MtkSelfStudyController.this;
            mtkSelfStudyController4.recoverOffset(mtkSelfStudyController4.mTop2Cell);
            MtkSelfStudyController mtkSelfStudyController5 = MtkSelfStudyController.this;
            mtkSelfStudyController5.recoverOffset(mtkSelfStudyController5.mGameOffsetCell);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recoverOffset(ConcurrentHashMap concurrentHashMap) {
        MtkSelfStudyController mtkSelfStudyController;
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            return;
        }
        for (String str : concurrentHashMap.keySet()) {
            try {
                String[] strArrSplit = str.split("-");
                if (strArrSplit.length != 4) {
                    this.log("setOrResetGameOffset,Invalid key format: " + str);
                } else {
                    String str2 = strArrSplit[0] + "-" + strArrSplit[1] + "-" + strArrSplit[2];
                    long j = Long.parseLong(strArrSplit[3]);
                    int iIntValue = ((Integer) concurrentHashMap.get(str)).intValue();
                    this.log("setOrResetGameOffset: cellKey=" + str2 + ", rat=" + j + ", offset=" + iIntValue);
                    mtkSelfStudyController = this;
                    try {
                        mtkSelfStudyController.doCellActionForGame(str2, j, iIntValue, true);
                    } catch (Exception e) {
                        e = e;
                        mtkSelfStudyController.log("Invalid rat value in key: " + e.getMessage());
                    }
                    this = mtkSelfStudyController;
                }
            } catch (Exception e2) {
                e = e2;
                mtkSelfStudyController = this;
            }
        }
    }

    protected void sendGameOffset(String str) {
        if (this.mIsMobileDataOn) {
            log("sendGameOffset");
            if ("GAME".equalsIgnoreCase(str)) {
                queryGameBadCellInfo("game_bad_cell_table");
            } else if ("AI".equalsIgnoreCase(str)) {
                queryGameBadCellInfo("game_cell_from_ai_table");
            }
            if (this.mNrGameCellStats.size() > 0) {
                setOrResetGameOffset(this.mNrGameCellStats, 2, false);
            }
            if (this.mLteGameCellStats.size() > 0) {
                setOrResetGameOffset(this.mLteGameCellStats, 1, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOrResetGameOffset(ConcurrentHashMap concurrentHashMap, int i, boolean z) {
        log("setOrResetGameOffset rat = " + i);
        for (String str : concurrentHashMap.keySet()) {
            int iIntValue = ((Integer) concurrentHashMap.get(str)).intValue();
            log("setOrResetGameOffset offset = " + iIntValue);
            doCellActionForGame(str, (long) i, iIntValue, z);
        }
    }

    private boolean shouldDoGameAction(boolean z) {
        if (z) {
            return true;
        }
        if (this.enterGameResidentDays >= this.studyGameResidentCellDays) {
            return (isEnterGameOptimizer() || this.isStaticCell) && this.cloudGameResidentOn;
        }
        return false;
    }

    private boolean isNotActiveDataPhone() {
        return MiuiPhoneUtils.getActiveDataPhone() == null;
    }

    public void doCellActionForGame(String str, long j, int i, boolean z) {
        if ((!z && !shouldDoActionForGameOpt()) || (z && !MiuiPhoneUtils.isSupportMtkDataOptimization() && !MiuiPhoneUtils.isSupportGameResidentCellOptForOS4())) {
            log("doCellActionForGame shouldDoActionForGameOpt return");
            return;
        }
        if (!shouldDoGameAction(z)) {
            log("doCellActionForGame not enterGameOptimizer return");
            return;
        }
        if (isNotActiveDataPhone()) {
            return;
        }
        int phoneId = MiuiPhoneUtils.getActiveDataPhone().getPhoneId();
        String networkOperatorForPhone = this.tmManager.getNetworkOperatorForPhone(phoneId);
        if (TextUtils.isEmpty(networkOperatorForPhone)) {
            return;
        }
        int i2 = j == 1 ? 1 : 0;
        String[] strArrSplit = str.split("-");
        int stringToInt = parseStringToInt(strArrSplit[1]);
        int stringToInt2 = parseStringToInt(strArrSplit[2]);
        log("doCellActionForGame offset = " + i + " rat = " + j + " isReset = " + z);
        int i3 = i * 4;
        if (z) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).revertTeleAction(2, TelephonyActionBase.ActionReason.GAME_OPT.value(), Integer.valueOf(phoneId), String.valueOf(i3), Integer.valueOf(i2), networkOperatorForPhone, Integer.valueOf(stringToInt2), Integer.valueOf(stringToInt));
        } else {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).teleAction(2, TelephonyActionBase.ActionReason.GAME_OPT.value(), Integer.valueOf(phoneId), String.valueOf(i3), Integer.valueOf(i2), networkOperatorForPhone, Integer.valueOf(stringToInt2), Integer.valueOf(stringToInt));
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void handleGameStall() {
        log("handleGameStall");
        removeGameDelayCheckMessages();
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (!"46045".equals(MiuiPhoneUtils.getNetworkNumeric(defaultDataSlotId)) && MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) && shouldDoActionForGameOpt()) {
            if (this.mSetGameOptNr2Lte) {
                resetGameResidentNetworkOptimization(true);
            } else if (MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) {
                handleGameStallForOS4(defaultDataSlotId);
            } else {
                handleGameStallLegacy(defaultDataSlotId);
            }
        }
    }

    private void handleGameStallForOS4(int i) {
        if (MiuiPhoneUtils.is5GSaRegistered(i) && this.mNeighborCellManager.shouldDegrade() && ((this.oldGameRsrp <= -118 || this.oldGameRsrq < -13) && (!isLteLagMoreThanNr() || this.isStaticCell))) {
            log("handleGameStall, request neighbor cell info before optimization");
            triggerGameResidentNetworkOptimization(i);
            return;
        }
        if (isGameRsrpBetterThan(-118) && this.mNeighborCellManager.shouldTriggerDataStall()) {
            doCellActionForGame(this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq, this.oldGameRat, -5, false);
            this.mGameOffsetCell.put(this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq + "-" + this.oldGameRat, -5);
        }
    }

    private boolean isGameRsrpBetterThan(int i) {
        return isValidSignalValue(this.oldGameRsrp) && this.oldGameRsrp > i;
    }

    private void handleGameStallLegacy(int i) {
        if (MiuiPhoneUtils.is5GSaRegistered(i)) {
            log("handleGameStall, isValidatePhoneId oldGameRsrp: " + this.oldGameRsrp + " oldGameRsrq: " + this.oldGameRsrq + " isSetOffsetFromModem: " + this.isSetOffsetFromModem);
            if (isLteLagMoreThanNr()) {
                return;
            }
            log("handleGameStall, triggerGameResidentNetworkOptimization");
            triggerGameResidentNetworkOptimization(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void triggerGameResidentNetworkOptimization(int i) {
        log("triggerGameResidentNetworkOptimization phoneId: " + i);
        boolean[] saPowerEnable = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSaPowerEnable();
        if (!shouldDoActionForGameOpt() || this.mSetGameOptNr2Lte || !this.isEnterGame || saPowerEnable.length == 0 || !saPowerEnable[i]) {
            log("ignore optimization shouldDoActionForGameOpt: " + shouldDoActionForGameOpt() + " mSetGameOptNr2Lte: " + this.mSetGameOptNr2Lte + "isEnterGame: " + this.isEnterGame);
            return;
        }
        if (isNetworkOptimizationForSa()) {
            log("NetworkOptimization had disable sa ignore");
            return;
        }
        if (isWithinRecoveryCooldown()) {
            log("less ten minutes, ignore optimization");
            return;
        }
        log("GAME_RESIDENT_CELL_OPTIMIZATION");
        ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).triggerGameResidentNetworkOptimization(false);
        this.mSetGameOptNr2Lte = true;
        recordCount("gameResidentCellNr2LteAllCnt", "game");
        removeGameDelayCheckMessages();
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1, 5), MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    private boolean isWithinRecoveryCooldown() {
        log("triggerGameResidentNetworkOptimization mRecoveryTime: " + this.mRecoveryTime);
        return System.currentTimeMillis() - this.mRecoveryTime < 600000;
    }

    private void gameStatistic20Min() throws Throwable {
        boolean zIsStaticScene;
        MtkSelfStudyController mtkSelfStudyController;
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (isInHomeOrCompanyScene(this.curGameScene)) {
            updateDurOrLagDur(jCurrentTimeMillis);
            saveGameCell(this.maxSavedGameCells, jCurrentTimeMillis);
            this.minsertDbTime = jCurrentTimeMillis;
            updateHistoryGameData(this.mCurrentGameCellStats);
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(36), 1200000L);
            return;
        }
        if (isStaticCell()) {
            if (shouldDoActionForGameOptForOS4()) {
                zIsStaticScene = isStaticScene();
            } else {
                zIsStaticScene = ((double) this.staticCount) > ((double) this.sceneCount) * 0.8d;
            }
            this.isStaticCell = zIsStaticScene;
            this.mWorkHandler.removeMessages(46);
            if (this.isFirst20min && this.isStaticCell) {
                Handler handler2 = this.mWorkHandler;
                handler2.sendMessageDelayed(handler2.obtainMessage(37, this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq + "-" + this.oldGameRat), 20000L);
            }
            boolean z = this.isStaticCell;
            if (z) {
                if (this.mGameCellOpt != null) {
                    log("is static scene");
                    recordCount("gameStaticCellActualCnt", "game");
                    updateDurOrLagDur(jCurrentTimeMillis);
                    mtkSelfStudyController = this;
                    mtkSelfStudyController.saveTopCellForStaticGame(this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq, this.oldGameRat, this.mCurrentGameCellStats, this.mTopCellForStaticGame);
                    StringBuilder sb = new StringBuilder("mTopCellForStaticGame: ");
                    sb.append(mtkSelfStudyController.mTopCellForStaticGame);
                    mtkSelfStudyController.log(sb.toString());
                    mtkSelfStudyController.mCurrentGameCellStats.clear();
                    if (mtkSelfStudyController.isFirst20min) {
                        mtkSelfStudyController.mGameCellOpt.updateActionPhase(5);
                    }
                } else {
                    mtkSelfStudyController = this;
                }
                Handler handler3 = mtkSelfStudyController.mWorkHandler;
                handler3.sendMessageDelayed(handler3.obtainMessage(36), 1200000L);
            } else {
                mtkSelfStudyController = this;
                if (!z) {
                    mtkSelfStudyController.log("not static scene");
                    Handler handler4 = mtkSelfStudyController.mWorkHandler;
                    handler4.sendMessageDelayed(handler4.obtainMessage(38), 30000L);
                }
            }
            mtkSelfStudyController.isFirst20min = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setGoodLink(long j) {
        if (j == 1 || j == 2) {
            beginOrStopGoodlink(j == 1 ? 0 : 1);
        }
    }

    private void beginOrStopGoodlink(int i) {
        String str;
        log(" beginOrStopGoodlink:  type: " + i);
        if (shouldDoActionForGameOptForOS4()) {
            if (i == 0) {
                str = "5,7,0,0,2000,0,0,0,0xFF,50,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,1,0xFFFFFFFF,0xFFFFFFFF,800,100,10,50";
            } else if (i == 1) {
                str = "5,7,0,1,2000,0,0,0,0xFF,50,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,1,0xFFFFFFFF,0xFFFFFFFF,800,100,10,50";
            } else if (i == 2) {
                str = "5,0,0,0,2000,0,0,0,0xFF,50,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,1,0xFFFFFFFF,0xFFFFFFFF,800,100,10,50";
            } else {
                str = i != 3 ? null : "5,0,0,1,2000,0,0,0,0xFF,50,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,1,0xFFFFFFFF,0xFFFFFFFF,800,100,10,50";
            }
            try {
                Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
                if (!TextUtils.isEmpty(str) && activeDataPhone != null) {
                    String str2 = "MIPC_SET_DATA_STALL_OPT," + str;
                    log("start result is sb ".concat(str2));
                    FiveGManagerBase.getInstance().sendAtCommand(activeDataPhone, str2.split(","), (Message) null);
                }
            } catch (Exception e) {
                log("beginOrStopGoodlink error: " + e.getMessage());
            }
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void checkLevelOverTime(int i) {
        log("checkLevelOverTime delayLevelThreshold: " + i);
        if ((isEnterGameOptimizer() || this.isStaticCell) && isHasDelayApiGame() && this.mCurrentGameDelayLevel >= i) {
            boolean zShouldDoActionForGameOptForOS4 = shouldDoActionForGameOptForOS4();
            boolean z = this.mSetGameOptNr2Lte;
            if (zShouldDoActionForGameOptForOS4) {
                if (z) {
                    handleGameStall();
                } else {
                    requestNeighborCellInfoCommon();
                }
            } else if (z) {
                handleGameStall();
            } else if (!this.isSetGameAtCmd[1]) {
                sendOffsetOrNr2LteAtCmd("nr2lte");
                this.isSetGameAtCmd[1] = true;
            }
            removeGameDelayCheckMessages();
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void checkLteAfterDisableSa() {
        ServiceState serviceState;
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null || (serviceState = activeDataPhone.getServiceState()) == null) {
            return;
        }
        if (isEnterGameOptimizer() || this.isStaticCell) {
            if (serviceState.getDataRegState() == 1 || !(serviceState.getRilDataRadioTechnology() == 14 || serviceState.getRilDataRadioTechnology() == 19)) {
                log("reset optimization because failed to switch to LTE");
                resetGameResidentNetworkOptimization(this.mSetGameOptNr2Lte);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0085 A[Catch: Exception -> 0x00b6, TRY_ENTER, TryCatch #0 {Exception -> 0x00b6, blocks: (B:3:0x0003, B:5:0x005c, B:7:0x0062, B:9:0x0068, B:11:0x006c, B:13:0x0070, B:22:0x008d, B:20:0x0085), top: B:33:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:43:? A[RETURN, SYNTHETIC] */
    private void doOffsetFromMd(String[] strArr) {
        MtkSelfStudyController mtkSelfStudyController;
        Exception exc;
        try {
            int i = Integer.parseInt(strArr[2].trim());
            int i2 = Integer.parseInt(strArr[3].trim());
            int i3 = Integer.parseInt(strArr[4].trim());
            int i4 = Integer.parseInt(strArr[5].trim());
            int i5 = Integer.parseInt(strArr[6].trim());
            String str = this.oldGameGci + "-" + i3 + "-" + i2 + "-" + i5;
            if (i != 1 || !shouldDoActionForGameOpt() || !isHasDelayApiGame() || !this.mEnterGameBattleState || !SelfStudyControllerBase.mEnterGameOptimizer) {
                return;
            }
            if (!this.mTop2Cell.isEmpty()) {
                try {
                    if (this.mTop2Cell.containsKey(str)) {
                        if (!this.mTop2Cell.isEmpty()) {
                            return;
                        }
                    }
                } catch (Exception e) {
                    exc = e;
                    mtkSelfStudyController = this;
                }
            } else if (!this.mTop2Cell.isEmpty()) {
                return;
            }
            mtkSelfStudyController = this;
            try {
                mtkSelfStudyController.doCellActionForGame(this.oldGameGci + "-" + i3 + "-" + i2, i5, i4, false);
                mtkSelfStudyController.isSetOffsetFromModem = true;
                mtkSelfStudyController.isGameSetOffsetFromModem(true);
                return;
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
            mtkSelfStudyController = this;
        }
        exc = e;
        mtkSelfStudyController.log(" doOffsetFromMd error data: " + exc.getMessage());
    }

    private void getOffsetFromMdForGame(String[] strArr) {
        try {
            int i = Integer.parseInt(strArr[2].trim());
            int i2 = Integer.parseInt(strArr[3].trim());
            int i3 = Integer.parseInt(strArr[4].trim());
            int i4 = Integer.parseInt(strArr[5].trim());
            int i5 = Integer.parseInt(strArr[6].trim());
            int i6 = Integer.parseInt(strArr[7].trim());
            String str = this.oldGameGci + "-" + i3 + "-" + i2 + "-" + i6;
            if (i == 1 && shouldProcessGameAction()) {
                if (i4 == 3) {
                    if (shouldProcessModem3(str)) {
                        doCellActionForGame(this.oldGameGci + "-" + i3 + "-" + i2, i6, i5, false);
                        this.isSetOffsetFromModem = true;
                        isGameSetOffsetFromModem(true);
                        this.mGameOffsetCell.put(this.oldGameGci + "-" + i3 + "-" + i2 + "-" + i6, Integer.valueOf(i5));
                        return;
                    }
                    return;
                }
                if (i4 == 4 && this.isSetGameAtCmd[1] && isHasDelayApiGame()) {
                    handleGameStall();
                    this.isSetGameAtCmd[1] = false;
                }
            }
        } catch (Exception e) {
            log(" getOffsetFromMdForGame error data: " + e.getMessage());
        }
    }

    private boolean shouldProcessModem3(String str) {
        if (str == null) {
            return false;
        }
        if (this.mTop2Cell.isEmpty() || this.mTop2Cell.containsKey(str)) {
            return this.mTop2Cell.isEmpty() && this.isSetGameAtCmd[0] && !this.isSetOffsetFromModem;
        }
        return true;
    }

    private boolean shouldProcessGameAction() {
        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive() || !shouldDoActionForGameOpt() || !SelfStudyControllerBase.mEnterGameOptimizer) {
            return false;
        }
        if (isHasDelayApiGame() && this.mEnterGameBattleState) {
            return true;
        }
        return isNoDelayApiGame() && SelfStudyControllerBase.mEnterGameAiOptimizer;
    }

    private void doNextCellFromModemForGame(String[] strArr) {
        log("doNextCellFromModemForGame  mEnterGameBattleState: " + this.mEnterGameBattleState + " mEnterGameOptimizer： " + SelfStudyControllerBase.mEnterGameOptimizer);
        if (SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
            return;
        }
        try {
            int i = Integer.parseInt(strArr[2].trim());
            int i2 = Integer.parseInt(strArr[3].trim());
            int i3 = Integer.parseInt(strArr[4].trim());
            int i4 = Integer.parseInt(strArr[5].trim());
            int i5 = Integer.parseInt(strArr[6].trim());
            int i6 = Integer.parseInt(strArr[7].trim());
            log("source: " + i + " sub: " + i2 + " freq: " + i3 + " pci: " + i4 + " offset: " + i5 + " rat: " + i6);
            if (i == 1 && i2 == 3 && shouldDoActionForGameOpt() && isHasDelayApiGame() && !this.mEnterGameBattleState && SelfStudyControllerBase.mEnterGameOptimizer) {
                String str = this.oldGameGci + "_" + i4 + "_" + i3 + "_" + i6;
                Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
                if (activeDataPhone == null) {
                    log("doNextCellFromModemForGame dataPhone is null");
                    return;
                }
                int phoneId = activeDataPhone.getPhoneId();
                String networkOperatorForPhone = this.tmManager.getNetworkOperatorForPhone(phoneId);
                int i7 = i5 * 4;
                this.mDoNextCellFromMd.put(str, Integer.valueOf(i7));
                ITelephonyModule iTelephonyModule = (ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class);
                TelephonyActionBase.ActionReason actionReason = TelephonyActionBase.ActionReason.GAME_OPT;
                iTelephonyModule.teleAction(2, actionReason.value(), Integer.valueOf(phoneId), String.valueOf(-16), Integer.valueOf(i6 == 1 ? 1 : 0), networkOperatorForPhone, Integer.valueOf(this.oldGameFreq), Integer.valueOf(this.oldGamePci));
                this.mDoNextCellFromMd.put(this.oldGameGci + "_" + this.oldGamePci + "_" + this.oldGameRsrq + "_" + this.oldGameRat, -16);
                StringBuilder sb = new StringBuilder(" nextCellOffset: ");
                sb.append(i7);
                sb.append("mDoNextCellFromMd:");
                sb.append(this.mDoNextCellFromMd);
                log(sb.toString());
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).teleAction(2, actionReason.value(), Integer.valueOf(phoneId), String.valueOf(i7), Integer.valueOf(i6 == 1 ? 1 : 0), networkOperatorForPhone, Integer.valueOf(i3), Integer.valueOf(i4));
                sendAtCmd(MiuiPhoneUtils.getActiveDataPhoneId(), MtkAtCmd.ENABLE_HIGH_PRIORITY_CELL_NR_A2, (Message) null);
            }
        } catch (Exception e) {
            log("doNextCellFromModemForGame error data: " + e.getMessage());
        }
    }

    private void defaultResetFromModemForGame(String[] strArr) {
        if (!SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && shouldDoActionForGameOpt() && isHasDelayApiGame()) {
            log("defaultResetFromModemForGame");
            try {
                int i = Integer.parseInt(strArr[2].trim());
                int i2 = Integer.parseInt(strArr[3].trim());
                int i3 = Integer.parseInt(strArr[4].trim());
                log("source: " + i + " sub_source: " + i2 + " action: " + i3);
                if (i == 1 && i2 == 3 && i3 == 0) {
                    defaultResetFromModemForGame();
                }
            } catch (Exception e) {
                log("defaultResetFromModemForGame error data: " + e.getMessage());
            }
        }
    }

    private void defaultResetFromModemForGame() {
        log("defaultResetFromModemForGame");
        try {
            if (isNeedResetFromModemForGame()) {
                if (this.isSetBWEable) {
                    String[] strArrSplit = "SBP_NR_PREFER_LARGE_BW_IN_CONNECTED,0,0,100,-100,-13,0".split(",");
                    if (MiuiPhoneUtils.getActiveDataPhone() != null) {
                        FiveGManagerBase.getInstance().sendAtCommand(MiuiPhoneUtils.getActiveDataPhoneId(), strArrSplit, (Message) null);
                    }
                    this.isSetBWEable = false;
                }
                int phoneId = MiuiPhoneUtils.getActiveDataPhone().getPhoneId();
                String networkOperatorForPhone = this.tmManager.getNetworkOperatorForPhone(phoneId);
                log("mDoNextCellFromMd: " + this.mDoNextCellFromMd);
                if (this.mDoNextCellFromMd.isEmpty()) {
                    return;
                }
                for (String str : this.mDoNextCellFromMd.keySet()) {
                    String[] strArrSplit2 = str.split("_");
                    if (strArrSplit2.length == 4) {
                        int i = Integer.parseInt(strArrSplit2[1]);
                        int i2 = Integer.parseInt(strArrSplit2[2]);
                        int i3 = Integer.parseInt(strArrSplit2[3]);
                        log("defaultResetFromModemForGame Key: " + str);
                        ITelephonyModule iTelephonyModule = (ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class);
                        int iValue = TelephonyActionBase.ActionReason.GAME_OPT.value();
                        Integer numValueOf = Integer.valueOf(phoneId);
                        int i4 = 1;
                        String strValueOf = String.valueOf(0);
                        if (i3 != 1) {
                            i4 = 0;
                        }
                        iTelephonyModule.teleAction(2, iValue, numValueOf, strValueOf, Integer.valueOf(i4), networkOperatorForPhone, Integer.valueOf(i2), Integer.valueOf(i));
                    }
                }
                sendAtCmd(MiuiPhoneUtils.getActiveDataPhoneId(), MtkAtCmd.DISABLE_HIGH_PRIORITY_CELL_NR_A2, (Message) null);
                this.mDoNextCellFromMd.clear();
            }
        } catch (Exception e) {
            log("ResetFromModemForGame error data: " + e.getMessage());
        }
    }

    private boolean isNeedResetFromModemForGame() {
        return !SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && shouldDoActionForGameOpt() && SelfStudyControllerBase.mEnterGameOptimizer && MiuiPhoneUtils.getActiveDataPhone() != null;
    }

    private void getStaticSceneScore(String[] strArr, int i) {
        if (this.isEnterGame && this.isFirst20min && i == 20006) {
            try {
                int i2 = Integer.parseInt(strArr[2].trim());
                log("getStaticSceneScore score: " + i2);
                this.sceneCount = this.sceneCount + 1;
                if (i2 >= 70) {
                    this.staticCount++;
                }
            } catch (Exception e) {
                log("defaultResetFromModemForGame error data: " + e.getMessage());
            }
        }
    }

    private void setStaticCmdAndMessage() {
        if (!shouldDoActionForGameOpt() || SelfStudyControllerBase.isBelowMtkDataOptVersionFive() || this.isStaticCell || !this.isFirst20min) {
            return;
        }
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(46), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        if (shouldDoActionForGameOptForOS4()) {
            checkStaticState(true);
        }
    }

    private boolean isNetworkOptimizationForSa() {
        if (this.mRecovery == null) {
            return false;
        }
        return this.mRecovery.isSaOffInRecoveryAction(DefaultSimManager.getDefaultDataSlotId());
    }

    private void resetGameResidentNetworkOptimization(boolean z) {
        log("resetGameResidentNetworkOptimization isNeedRecovery: " + z);
        if (!z || SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
            return;
        }
        log("reset GAME_RESIDENT_CELL_OPTIMIZATION");
        ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).resetGameResidentNetworkOptimization(true);
        this.mSetGameOptNr2Lte = false;
        this.mRecoveryTime = System.currentTimeMillis();
        removeGameDelayCheckMessages();
    }

    private void processCallStateForGameOpt(int i, int i2) {
        if ((MiuiPhoneUtils.isSupportMtkDataOptimization() || MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) && this.isEnterGame) {
            if ((SelfStudyControllerBase.mEnterGameOptimizer || this.isStaticCell) && MiuiPhoneUtils.isDuringCall()) {
                log("processCallStateChange process inCall");
                resetResidentGameCellOptimization(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAirPlaneForGameOpt(boolean z) {
        if ((MiuiPhoneUtils.isSupportMtkDataOptimization() || MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) && this.isEnterGame) {
            if ((SelfStudyControllerBase.mEnterGameOptimizer || this.isStaticCell) && z) {
                log("processAirPlaneForGameOpt process isAirPlaneOn");
                resetResidentGameCellOptimization(true);
            }
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void resetResidentGameCellOptimization(boolean z) {
        if ((MiuiPhoneUtils.isSupportMtkDataOptimization() || MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) && this.mGameCellOpt != null) {
            if (z || this.isStaticCell) {
                if (this.mSetGameOptNr2Lte) {
                    resetGameResidentNetworkOptimization(true);
                }
                this.mGameCellOpt.tryRecoveryGameCell();
                SelfStudyControllerBase.mEnterGameOptimizer = false;
            }
            if (this.isSetNrTop && !SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                this.isSetNrTop = false;
                this.mGameCellOpt.updateActionPhase(4);
            }
            if (this.isSetGameTopCell) {
                beginOrStopGoodlink(2);
                beginOrStopGoodlink(3);
            }
        }
    }

    private void processGameExit() {
        if (this.isEnterGame) {
            log("processGameExit");
            defaultResetFromModemForGame();
            resetResidentGameCellOptimization(SelfStudyControllerBase.mEnterGameOptimizer);
            resetBenefitStatisticList();
            isGameSetOffsetFromModem(false);
            stopModemModel();
        }
        this.mEnterGameBoosterCount = 0;
        this.mEnterGameAiBoosterCount = 0;
        this.minsertDbTime = 0L;
        this.startGameLagTime = 0L;
        this.startGameLagFromModem = 0L;
        this.startGameLagFromGameAi = 0L;
        this.startGameLagFromTraModel = 0L;
        this.startGameTime = 0L;
        this.curGameScene = 2;
        this.isGameLag = false;
        this.isGameAiLag = false;
        this.isGameTraModelLag = false;
        this.isSetOffsetFromModem = false;
        SelfStudyControllerBase.mEnterGameAiOptimizer = false;
        this.isGameLagFromModem = false;
        this.mEnterGameBattleState = false;
        this.isSetGameTopCell = false;
        this.isLastAiAndTraModelLag = false;
        this.isSetBWEable = false;
        this.isStaticCell = false;
        this.isFirst20min = true;
        this.mlastGameLevelTime = 0L;
        resetGameAtCmdFlags();
        this.mTopGamePackage = "";
        this.mLastTopGamePackage = "";
        this.mEnterGameBattleCount = 0;
        this.mlastGameDelay = 0;
        this.mCurrentGameDelayLevel = 0;
        this.mRecoveryTime = 0L;
        this.mSetPenaltTime = 0L;
        this.mDoNextCellFromMd.clear();
        this.mTopCellForStaticGame.clear();
        this.mCurrentGameCellStats.clear();
        this.mLteGameCellStats.clear();
        this.mNrGameCellStats.clear();
        this.mTop2Cell.clear();
        this.mGameOffsetCell.clear();
        if (this.mSetGameOptNr2Lte) {
            resetGameResidentNetworkOptimization(true);
        }
        this.isGameDebug = false;
        this.isEnterGame = false;
        this.mSetGameOptNr2Lte = false;
        this.isSetNrTop = false;
        SelfStudyControllerBase.mEnterGameOptimizer = false;
        this.isNr2Lte = false;
        this.sceneCount = 0;
        this.staticCount = 0;
        removeGameDelayCheckMessages();
        resetStaticState();
        SystemProperties.set("gsm.game.scene", "0");
        this.mWorkHandler.removeMessages(1049);
        this.mWorkHandler.removeMessages(36);
        this.mWorkHandler.removeMessages(33);
        this.mWorkHandler.removeMessages(32);
        this.mWorkHandler.removeMessages(37);
        this.mWorkHandler.removeMessages(38);
        this.mWorkHandler.removeMessages(39);
        this.mWorkHandler.removeMessages(45);
        this.mWorkHandler.removeMessages(41);
        this.mWorkHandler.removeMessages(43);
        this.mWorkHandler.removeMessages(42);
        this.mWorkHandler.removeMessages(46);
        this.mWorkHandler.removeMessages(44);
    }

    public void resetGameAtCmdFlags() {
        int i = 0;
        while (true) {
            boolean[] zArr = this.isSetGameAtCmd;
            if (i >= zArr.length) {
                return;
            }
            zArr[i] = false;
            i++;
        }
    }

    /* JADX WARN: Code duplicated, block: B:109:0x022d  */
    /* JADX WARN: Code duplicated, block: B:140:0x02e1  */
    /* JADX WARN: Code duplicated, block: B:34:0x00d4  */
    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void processGameCellLocationChanged(CellLocation cellLocation, int i) {
        int i2;
        int i3;
        long j;
        int pci;
        int nrarfcn;
        long j2;
        int i4;
        int i5;
        int i6;
        int i7;
        boolean z;
        if (!MiuiPhoneUtils.isDefaultDataSlotId(i) || cellLocation == null || cellLocation.isEmpty()) {
            return;
        }
        Phone phone = MiuiPhoneUtils.getPhone(i);
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (phone == null || phone.getCurrentCellIdentity() == null || activeDataPhone == null || activeDataPhone.getCurrentCellIdentity() == null) {
            return;
        }
        String globalCellId = activeDataPhone.getCurrentCellIdentity().getGlobalCellId();
        if (TextUtils.isEmpty(globalCellId)) {
            return;
        }
        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j3 = 0;
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            pci = cellIdentityLte.getPci();
            nrarfcn = cellIdentityLte.getEarfcn();
            j2 = 1;
        } else {
            if (currentCellIdentity instanceof CellIdentityNr) {
                CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
                pci = cellIdentityNr.getPci();
                nrarfcn = cellIdentityNr.getNrarfcn();
                j2 = 2;
            } else {
                log("game cell type not supported, return");
                i2 = 0;
                i3 = 0;
                j = 0;
            }
            this.curGameRat = j;
            Log.d("MtkSelfStudyController", "processGameCellLocationChanged  new rat = " + j + " gci = " + TelephonyUtils.pii(globalCellId) + " mIsMobileDataOn = " + this.mIsMobileDataOn);
            StringBuilder sb = new StringBuilder(" isTmgp: ");
            sb.append(isHasDelayApiGame());
            sb.append(" isNoDelayApiGame： ");
            sb.append(isNoDelayApiGame());
            log(sb.toString());
            if (shouldDoActionForGameOpt() || !((isHasDelayApiGame() || isNoDelayApiGame()) && this.isEnterGame && this.mIsMobileDataOn && (i3 != this.oldGamePci || i2 != this.oldGameFreq || !this.oldGameGci.equals(globalCellId)))) {
                j = j;
                i4 = i2;
                i5 = i3;
            } else {
                if (this.isSetGameTopCell) {
                    beginOrStopGoodlink(2);
                    beginOrStopGoodlink(3);
                }
                if (this.mlastGameCellTime == 0 || this.oldGameRat == 0) {
                    j3 = 0;
                } else {
                    if (this.mWorkHandler.hasMessages(41)) {
                        this.mWorkHandler.removeMessages(41);
                    }
                    if (this.isSetOffsetFromModem && SelfStudyControllerBase.mEnterGameOptimizer && !this.mWorkHandler.hasMessages(39)) {
                        recordCount("gameResidentCellModemOffsetAllCnt", "game");
                        Handler handler = this.mWorkHandler;
                        handler.sendMessageDelayed(handler.obtainMessage(39), 10000L);
                        this.isSetOffsetFromModem = false;
                    }
                    if (this.isSetGameTopCell && SelfStudyControllerBase.mEnterGameOptimizer) {
                        this.mWorkHandler.removeMessages(43);
                        recordCount("gameResidentTop1NextCellAllCnt", "game");
                        Handler handler2 = this.mWorkHandler;
                        handler2.sendMessageDelayed(handler2.obtainMessage(43), 20000L);
                        this.isSetGameTopCell = false;
                    }
                    if (((this.mEnterGameBattleState || isStaticCell()) && isHasDelayApiGame()) || (isNoDelayApiGame() && (SelfStudyControllerBase.mEnterGameAiOptimizer || isStaticCell()))) {
                        addOrUpdateGameStallDur(this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, this.mlastGameCellTime), this.oldGameGci, jCurrentTimeMillis);
                    } else if (!this.mEnterGameBattleState && isHasDelayApiGame()) {
                        long j4 = this.endGameTime;
                        if (j4 != j3) {
                            long j5 = this.mlastGameCellTime;
                            if (j5 >= j4) {
                                log("lastGameCellTime > endGameTime no need save");
                            } else {
                                addOrUpdateGameStallDur(this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j4, j5), this.oldGameGci, jCurrentTimeMillis);
                            }
                        }
                    }
                    if (this.oldGameRat == j3 && this.isGameLag && isHasDelayApiGame()) {
                        long j6 = this.startGameLagTime;
                        if (j6 != j3) {
                            i6 = i2;
                            i7 = i3;
                            addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, j6) * ((long) getLastGameDelay(this.mlastGameDelay)), jCurrentTimeMillis, this.mCurrentGameCellStats, "AP");
                            this.startGameLagTime = jCurrentTimeMillis;
                        } else {
                            i6 = i2;
                            i7 = i3;
                        }
                        long j7 = this.startGameLagFromModem;
                        if (j7 != j3 && this.isGameLagFromModem) {
                            addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, j7), jCurrentTimeMillis, this.mCurrentGameCellStats, "MODEM");
                            this.startGameLagFromModem = jCurrentTimeMillis;
                        }
                    } else {
                        i6 = i2;
                        i7 = i3;
                    }
                    if (isNoDelayApiGame() && this.oldGameRat != j3 && ((SelfStudyControllerBase.mEnterGameAiOptimizer || isStaticCell()) && ((z = this.isGameAiLag) || this.isGameTraModelLag))) {
                        if (this.startGameLagFromGameAi != j3 && z) {
                            log("update game ai lag time  ai");
                            addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, this.startGameLagFromGameAi), jCurrentTimeMillis, this.mCurrentGameCellStats, "AI");
                            this.startGameLagFromGameAi = jCurrentTimeMillis;
                        }
                        if (this.startGameLagFromTraModel != j3 && this.isGameTraModelLag) {
                            log("update game ai lag time  tra");
                            addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, this.startGameLagFromTraModel), jCurrentTimeMillis, this.mCurrentGameCellStats, "TRA");
                            this.startGameLagFromTraModel = jCurrentTimeMillis;
                        }
                    }
                    if (this.mGameCellOpt != null && this.isSetNrTop && !SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                        this.isSetNrTop = false;
                        this.mGameCellOpt.updateActionPhase(4);
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(globalCellId);
                    sb2.append("-");
                    i5 = i7;
                    sb2.append(i5);
                    sb2.append("-");
                    i4 = i6;
                    sb2.append(i4);
                    sb2.append("-");
                    sb2.append(j);
                    this.curGameGpfr = sb2.toString();
                    if (this.mWorkHandler.hasMessages(37)) {
                        this.mWorkHandler.removeMessages(37);
                    }
                    if (this.isEnterGame && this.oldGameRat != j3 && ((this.mEnterGameBattleState && isHasDelayApiGame()) || (isNoDelayApiGame() && (SelfStudyControllerBase.mEnterGameAiOptimizer || isStaticCell())))) {
                        Handler handler3 = this.mWorkHandler;
                        handler3.sendMessageDelayed(handler3.obtainMessage(37, this.curGameGpfr), 20000L);
                    }
                }
                if (this.oldGameRat == j3) {
                    i6 = i2;
                    i7 = i3;
                } else {
                    i6 = i2;
                    i7 = i3;
                }
                if (isNoDelayApiGame()) {
                    if (this.startGameLagFromGameAi != j3) {
                        log("update game ai lag time  ai");
                        addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, this.startGameLagFromGameAi), jCurrentTimeMillis, this.mCurrentGameCellStats, "AI");
                        this.startGameLagFromGameAi = jCurrentTimeMillis;
                    }
                    if (this.startGameLagFromTraModel != j3) {
                        log("update game ai lag time  tra");
                        addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, this.startGameLagFromTraModel), jCurrentTimeMillis, this.mCurrentGameCellStats, "TRA");
                        this.startGameLagFromTraModel = jCurrentTimeMillis;
                    }
                }
                if (this.mGameCellOpt != null) {
                    this.isSetNrTop = false;
                    this.mGameCellOpt.updateActionPhase(4);
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append(globalCellId);
                sb3.append("-");
                i5 = i7;
                sb3.append(i5);
                sb3.append("-");
                i4 = i6;
                sb3.append(i4);
                sb3.append("-");
                sb3.append(j);
                this.curGameGpfr = sb3.toString();
                if (this.mWorkHandler.hasMessages(37)) {
                    this.mWorkHandler.removeMessages(37);
                }
                if (this.isEnterGame) {
                    Handler handler4 = this.mWorkHandler;
                    handler4.sendMessageDelayed(handler4.obtainMessage(37, this.curGameGpfr), 20000L);
                }
            }
            this.oldGameRat = j;
            this.oldGameFreq = i4;
            this.oldGamePci = i5;
            this.oldGameGci = globalCellId;
            this.mlastGameCellTime = jCurrentTimeMillis;
            resetGameAtCmdFlags();
        }
        i2 = nrarfcn;
        i3 = pci;
        j = j2;
        this.curGameRat = j;
        Log.d("MtkSelfStudyController", "processGameCellLocationChanged  new rat = " + j + " gci = " + TelephonyUtils.pii(globalCellId) + " mIsMobileDataOn = " + this.mIsMobileDataOn);
        StringBuilder sb4 = new StringBuilder(" isTmgp: ");
        sb4.append(isHasDelayApiGame());
        sb4.append(" isNoDelayApiGame： ");
        sb4.append(isNoDelayApiGame());
        log(sb4.toString());
        if (shouldDoActionForGameOpt()) {
            j = j;
            i4 = i2;
            i5 = i3;
        } else {
            j = j;
            i4 = i2;
            i5 = i3;
        }
        this.oldGameRat = j;
        this.oldGameFreq = i4;
        this.oldGamePci = i5;
        this.oldGameGci = globalCellId;
        this.mlastGameCellTime = jCurrentTimeMillis;
        resetGameAtCmdFlags();
    }

    /* JADX WARN: Code duplicated, block: B:52:0x009b  */
    /* JADX WARN: Code duplicated, block: B:53:0x00ae  */
    /* JADX WARN: Code duplicated, block: B:60:0x0118  */
    private void processServiceStateChangeForGame(int i, ServiceState serviceState) {
        Phone phone;
        SignalStrength signalStrength;
        long j;
        CellSignalStrengthNr nrSignalStrength;
        if (this.mIsMobileDataOn && i == MiuiPhoneUtils.getActiveDataPhoneId() && MiuiPhoneUtils.isValidatePhoneId(i)) {
            int dataNetworkType = serviceState == null ? 0 : serviceState.getDataNetworkType();
            ServiceState serviceState2 = this.mServiceState[i];
            int dataNetworkType2 = serviceState2 == null ? 0 : serviceState2.getDataNetworkType();
            boolean z = isNrNwType(dataNetworkType2) && isLteNwType(dataNetworkType);
            boolean zIsDataRegistered = MiuiPhoneUtils.isDataRegistered(i);
            boolean z2 = isNrNwType(dataNetworkType2) && !isNrNwType(dataNetworkType);
            boolean z3 = isLteNwType(dataNetworkType2) && !isLteNwType(dataNetworkType);
            this.isLte = isLteNwType(dataNetworkType);
            if (isLteNwType(dataNetworkType) && zIsDataRegistered) {
                this.curGameRat = 1L;
            } else {
                if (isNrNwType(dataNetworkType) && zIsDataRegistered) {
                    this.curGameRat = 2L;
                } else {
                    this.curGameRat = 0L;
                }
                phone = MiuiPhoneUtils.getPhone(i);
                if (phone != null && (signalStrength = phone.getSignalStrength()) != null) {
                    j = this.curGameRat;
                    if (j == 1) {
                        this.oldGameRsrp = signalStrength.getLteRsrp();
                        this.oldGameSnr = signalStrength.getLteRssnr();
                        this.oldGameRsrq = signalStrength.getLteRsrq();
                    } else if (j == 2 && (nrSignalStrength = MiuiPhoneUtils.getNrSignalStrength(signalStrength)) != null) {
                        this.oldGameRsrp = nrSignalStrength.getSsRsrp();
                        this.oldGameSnr = nrSignalStrength.getSsSinr();
                        this.oldGameRsrq = nrSignalStrength.getSsRsrq();
                    }
                }
                log("processServiceStateChangeForGame: rsrp = " + this.oldGameRsrp + " snr = " + this.oldGameSnr + " Rsrq: " + this.oldGameRsrq + " oldRat = " + dataNetworkType2 + " newRat = " + dataNetworkType + " curGameRat = " + this.curGameRat + " isEnterGame = " + this.isEnterGame);
                if (serviceState != null) {
                    this.mServiceState[i] = new ServiceState(serviceState);
                }
                if (this.isEnterGame || !shouldDoActionForGameOpt()) {
                }
                if ((z2 || z3) && this.mWorkHandler.hasMessages(37)) {
                    this.mWorkHandler.removeMessages(37);
                }
                if (this.curGameRat != 1) {
                    this.isNr2Lte = false;
                } else if (this.isNr2Lte || z) {
                    this.isNr2Lte = true;
                }
                log("isNr2Lte: " + this.isNr2Lte + " nr2Lte: " + z);
                if (!SelfStudyControllerBase.mEnterGameOptimizer || SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                    return;
                }
                if (this.mSetGameOptNr2Lte && this.isNr2Lte && this.curGameRat == 1 && !this.mWorkHandler.hasMessages(45)) {
                    Handler handler = this.mWorkHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(45), 10000L);
                }
                if (this.mWorkHandler.hasMessages(45) && this.curGameRat == r12) {
                    this.mWorkHandler.removeMessages(45);
                }
                if (this.curGameRat == r12 || !isHasDelayApiGame()) {
                    return;
                }
                defaultResetFromModemForGame();
                return;
            }
            phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null) {
                j = this.curGameRat;
                if (j == 1) {
                    this.oldGameRsrp = signalStrength.getLteRsrp();
                    this.oldGameSnr = signalStrength.getLteRssnr();
                    this.oldGameRsrq = signalStrength.getLteRsrq();
                } else if (j == 2) {
                    this.oldGameRsrp = nrSignalStrength.getSsRsrp();
                    this.oldGameSnr = nrSignalStrength.getSsSinr();
                    this.oldGameRsrq = nrSignalStrength.getSsRsrq();
                }
            }
            log("processServiceStateChangeForGame: rsrp = " + this.oldGameRsrp + " snr = " + this.oldGameSnr + " Rsrq: " + this.oldGameRsrq + " oldRat = " + dataNetworkType2 + " newRat = " + dataNetworkType + " curGameRat = " + this.curGameRat + " isEnterGame = " + this.isEnterGame);
            if (serviceState != null) {
                this.mServiceState[i] = new ServiceState(serviceState);
            }
            if (this.isEnterGame) {
            }
        }
    }

    private boolean isHasDelayApiGame() {
        return SelfStudyControllerBase.STATIC_GAME_CELL_WHITELIST_PACKAGES.contains(this.mLastTopGamePackage);
    }

    private boolean isNoDelayApiGame() {
        return SelfStudyControllerBase.STATIC_GAME_AI_CELL_NO_DELAY_WHITELIST_PACKAGES.contains(this.mLastTopGamePackage) && MiuiPhoneUtils.isSupportModemAiOptimization();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:35:0x013d  */
    public boolean isLteLagMoreThanNr() {
        long jLongValue;
        long jLongValue2;
        long jLongValue3;
        long jLongValue4;
        Cursor cursorQuery = this.dbHelper.query("game_bad_cell_table", new String[]{"rat", "data_stall_lag_duration", "data_stall_duration", "update_time_mills"}, "update_time_mills >= ?", new String[]{String.valueOf(queryLatestGameCellTime("game_bad_cell_table") - 1200000)}, null, null, null);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
        concurrentHashMap.put(1L, 0L);
        concurrentHashMap.put(2L, 0L);
        concurrentHashMap2.put(1L, 0L);
        concurrentHashMap2.put(2L, 0L);
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                try {
                    try {
                        long j = cursorQuery.getLong(cursorQuery.getColumnIndex("rat"));
                        long j2 = cursorQuery.getLong(cursorQuery.getColumnIndex("data_stall_lag_duration"));
                        long j3 = cursorQuery.getLong(cursorQuery.getColumnIndex("data_stall_duration"));
                        Long l = (Long) concurrentHashMap.get(Long.valueOf(j));
                        Long l2 = (Long) concurrentHashMap2.get(Long.valueOf(j));
                        if (l != null && l2 != null) {
                            concurrentHashMap.put(Long.valueOf(j), Long.valueOf(l.longValue() + j3));
                            concurrentHashMap2.put(Long.valueOf(j), Long.valueOf(l2.longValue() + j2));
                        }
                    } catch (Throwable th) {
                        cursorQuery.close();
                        throw th;
                    }
                } catch (Exception e) {
                    e = e;
                    jLongValue = 0;
                }
            }
            jLongValue = ((Long) concurrentHashMap.get(1L)).longValue();
            try {
                jLongValue2 = ((Long) concurrentHashMap.get(2L)).longValue();
                try {
                    jLongValue3 = ((Long) concurrentHashMap2.get(1L)).longValue();
                    try {
                        jLongValue4 = ((Long) concurrentHashMap2.get(2L)).longValue();
                        cursorQuery.close();
                    } catch (Exception e2) {
                        e = e2;
                        e.printStackTrace();
                        cursorQuery.close();
                        jLongValue4 = 0;
                    }
                } catch (Exception e3) {
                    e = e3;
                    jLongValue3 = 0;
                    e.printStackTrace();
                    cursorQuery.close();
                    jLongValue4 = 0;
                    log("isLteLagMoreThanNr: sumDurForLte = " + jLongValue + " sumDurForNR = " + jLongValue2 + " sumLagDurForLte = " + jLongValue3 + " sumLagDurForNR = " + jLongValue4);
                    double dCalculateRatio = calculateRatio(jLongValue3, jLongValue);
                    double dCalculateRatio2 = calculateRatio(jLongValue4, jLongValue2);
                    if (jLongValue >= 30000) {
                    }
                    log("isLteLagMoreThanNr false");
                    return false;
                }
            } catch (Exception e4) {
                e = e4;
                jLongValue2 = 0;
                jLongValue3 = 0;
                e.printStackTrace();
                cursorQuery.close();
                jLongValue4 = 0;
                log("isLteLagMoreThanNr: sumDurForLte = " + jLongValue + " sumDurForNR = " + jLongValue2 + " sumLagDurForLte = " + jLongValue3 + " sumLagDurForNR = " + jLongValue4);
                double dCalculateRatio3 = calculateRatio(jLongValue3, jLongValue);
                double dCalculateRatio4 = calculateRatio(jLongValue4, jLongValue2);
                if (jLongValue >= 30000) {
                }
                log("isLteLagMoreThanNr false");
                return false;
            }
        } else {
            jLongValue = 0;
            jLongValue4 = 0;
            jLongValue2 = 0;
            jLongValue3 = 0;
        }
        log("isLteLagMoreThanNr: sumDurForLte = " + jLongValue + " sumDurForNR = " + jLongValue2 + " sumLagDurForLte = " + jLongValue3 + " sumLagDurForNR = " + jLongValue4);
        double dCalculateRatio5 = calculateRatio(jLongValue3, jLongValue);
        double dCalculateRatio6 = calculateRatio(jLongValue4, jLongValue2);
        if (jLongValue >= 30000 || jLongValue2 < 30000) {
            log("isLteLagMoreThanNr false");
            return false;
        }
        if (dCalculateRatio5 <= dCalculateRatio6) {
            return false;
        }
        log("isLteLagMoreThanNr true");
        return true;
    }

    private void resetActionForGameCell(String str, boolean z) {
        if (str == null) {
            log("resetActionForGameCell: gpfr is null");
            return;
        }
        String[] strArrSplit = str.split("-");
        if (strArrSplit.length < 4) {
            log("resetActionForGameCell: Invalid gpfr format");
            return;
        }
        String str2 = strArrSplit[0];
        int stringToInt = parseStringToInt(strArrSplit[1]);
        int stringToInt2 = parseStringToInt(strArrSplit[2]);
        long stringToInt3 = parseStringToInt(strArrSplit[3]);
        if (MiuiPhoneUtils.getActiveDataPhone() == null) {
            return;
        }
        int phoneId = MiuiPhoneUtils.getActiveDataPhone().getPhoneId();
        String networkOperatorForPhone = this.tmManager.getNetworkOperatorForPhone(phoneId);
        if (TextUtils.isEmpty(networkOperatorForPhone)) {
            return;
        }
        log("resetActionForGameCell");
        if (z) {
            getGameCellRank(str2, stringToInt, stringToInt2, stringToInt3, phoneId, networkOperatorForPhone);
            return;
        }
        if (this.isStaticCell) {
            boolean zIsTopCellForStaticGame = isTopCellForStaticGame(str2 + "-" + stringToInt + "-" + stringToInt2, this.mTopCellForStaticGame);
            StringBuilder sb = new StringBuilder(" resetActionForGameCell isTop: ");
            sb.append(zIsTopCellForStaticGame);
            log(sb.toString());
            if (zIsTopCellForStaticGame) {
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).teleAction(2, TelephonyActionBase.ActionReason.GAME_OPT.value(), Integer.valueOf(phoneId), "16", Integer.valueOf(stringToInt3 == 1 ? 1 : 0), networkOperatorForPhone, Integer.valueOf(stringToInt2), Integer.valueOf(stringToInt));
                this.mTop2Cell.put(str2 + "-" + stringToInt + "-" + stringToInt2 + "-" + stringToInt3, 16);
                setGoodLink(stringToInt3);
                this.isSetGameTopCell = true;
            }
        }
    }

    private void getGameCellRank(String str, int i, int i2, long j, int i3, String str2) {
        String str3;
        int count;
        GameCellDdsSaOpt gameCellDdsSaOpt;
        if (isHasDelayApiGame() && this.mEnterGameBattleState && SelfStudyControllerBase.mEnterGameOptimizer) {
            log("set ap table");
            str3 = "game_bad_cell_table";
        } else if (isNoDelayApiGame() && SelfStudyControllerBase.mEnterGameAiOptimizer) {
            log("set ai table");
            str3 = "game_cell_from_ai_table";
        } else {
            log("no need set");
            return;
        }
        String str4 = str3;
        String[] strArr = {"gci", "freq", "pci", "update_time_mills", "average_game_data_lag_ratio"};
        long jQueryLatestGameCellTime = queryLatestGameCellTime(str4) - 1200000;
        try {
            Cursor cursorQuery = this.dbHelper.query(str4, strArr, "update_time_mills > ?", new String[]{String.valueOf(jQueryLatestGameCellTime)}, null, null, "average_game_data_lag_ratio ASC");
            if (cursorQuery != null) {
                try {
                    count = cursorQuery.getCount();
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
                count = 0;
            }
            if (count <= 0 || !shouldDoActionForGameOpt() || this.enterGameResidentDays <= this.studyGameResidentCellDays || !isEnterGameOptimizer()) {
                log(" no need set offset");
                if (cursorQuery == null) {
                    return;
                }
            } else if (cursorQuery.moveToFirst()) {
                if (cursorQuery.getString(0).equals(str) && cursorQuery.getInt(1) == i2 && cursorQuery.getInt(2) == i) {
                    log("getGameCellRank top1");
                    if (j == 2 && isNrBatterThanLte(str4, jQueryLatestGameCellTime) && !SelfStudyControllerBase.isBelowMtkDataOptVersionFive() && (gameCellDdsSaOpt = this.mGameCellOpt) != null && this.isEnterGame) {
                        this.isSetNrTop = true;
                        gameCellDdsSaOpt.updateActionPhase(3);
                    }
                    setGoodLink(j);
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).teleAction(2, TelephonyActionBase.ActionReason.GAME_OPT.value(), Integer.valueOf(i3), "16", Integer.valueOf(j == 1 ? 1 : 0), str2, Integer.valueOf(i2), Integer.valueOf(i));
                    this.mTop2Cell.put(str + "-" + i + "-" + i2 + "-" + j, 16);
                    this.mWorkHandler.removeMessages(41);
                    this.isSetGameTopCell = true;
                    Handler handler = this.mWorkHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(41), 20000L);
                    recordCount("gameResidentCellTop1AllCnt", "game");
                } else {
                    log("getGameCellRank no need set offset");
                }
                if (!this.mWorkHandler.hasMessages(44)) {
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    Handler handler2 = this.mWorkHandler;
                    handler2.sendMessageDelayed(handler2.obtainMessage(44, concurrentHashMap), 10000L);
                }
            }
            cursorQuery.close();
        } catch (Exception e) {
            log("Error querying game cell rank: " + e.getMessage());
        }
    }

    /* JADX WARN: Code duplicated, block: B:15:0x0053  */
    private boolean isNrBatterThanLte(String str, long j) {
        float f;
        int i;
        String[] strArr = {"average_game_data_lag_ratio"};
        String[] strArr2 = {String.valueOf(1), String.valueOf(j)};
        String[] strArr3 = {String.valueOf(2), String.valueOf(j)};
        try {
            Cursor cursorQuery = this.dbHelper.query(str, strArr, "rat = ? AND update_time_mills > ?", strArr2, null, null, null);
            try {
                Cursor cursorQuery2 = this.dbHelper.query(str, strArr, "rat = ? AND update_time_mills > ?", strArr3, null, null, null);
                float f2 = 0.0f;
                if (cursorQuery != null) {
                    try {
                        if (cursorQuery.moveToFirst()) {
                            f = 0.0f;
                            i = 0;
                            do {
                                f += cursorQuery.getFloat(0);
                                i++;
                            } while (cursorQuery.moveToNext());
                        } else {
                            f = 0.0f;
                            i = 0;
                        }
                    } catch (Throwable th) {
                        if (cursorQuery2 == null) {
                            throw th;
                        }
                        try {
                            cursorQuery2.close();
                            throw th;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            throw th;
                        }
                        Log.e("MtkSelfStudyController", "NR/LTE error: " + e.getMessage());
                        return false;
                    }
                } else {
                    f = 0.0f;
                    i = 0;
                }
                if (cursorQuery2 != null && cursorQuery2.moveToFirst()) {
                    do {
                        f2 += cursorQuery2.getFloat(0);
                    } while (cursorQuery2.moveToNext());
                }
                if (cursorQuery2 != null) {
                    cursorQuery2.close();
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                return i == 0 || f2 < f;
            } catch (Throwable th3) {
                if (cursorQuery == null) {
                    throw th3;
                }
                try {
                    cursorQuery.close();
                    throw th3;
                } catch (Throwable th4) {
                    th3.addSuppressed(th4);
                    throw th3;
                }
            }
        } catch (Exception e) {
            Log.e("MtkSelfStudyController", "NR/LTE error: " + e.getMessage());
            return false;
        }
    }

    private void setGameCellPenalt(String str) {
        String str2;
        if (this.enterGameResidentDays <= this.studyGameResidentCellDays || str == null) {
            return;
        }
        boolean z = isHasDelayApiGame() && this.isGameLag;
        boolean z2 = isNoDelayApiGame() && this.isGameAiLag && this.isGameTraModelLag;
        if (z || z2) {
            log("setGameCellPenalt mSetPenaltTime: " + this.mSetPenaltTime);
            if (System.currentTimeMillis() - this.mSetPenaltTime < 600000) {
                log("Less than 10 minutes since last penalty, skip");
                return;
            }
            log("setGameCellPenalt");
            String[] strArrSplit = str.split("-");
            if (strArrSplit.length < 4) {
                log("error format of gpfr");
                return;
            }
            String str3 = strArrSplit[0];
            int stringToInt = parseStringToInt(strArrSplit[1]);
            int stringToInt2 = parseStringToInt(strArrSplit[2]);
            int stringToInt3 = parseStringToInt(strArrSplit[3]);
            String str4 = str3 + "-" + stringToInt + "-" + stringToInt2;
            if (this.mTop2Cell.isEmpty() || !this.mTop2Cell.containsKey(str)) {
                return;
            }
            if (this.isSetGameTopCell) {
                beginOrStopGoodlink(2);
                beginOrStopGoodlink(3);
            }
            if (isInHomeOrCompanyScene(this.curGameScene)) {
                if (isHasDelayApiGame() || isNoDelayApiGame()) {
                    if (isHasDelayApiGame()) {
                        str2 = "game_bad_cell_table";
                    } else {
                        str2 = "game_cell_from_ai_table";
                    }
                    queryGameBadCellInfo(str2);
                    Log.d("MtkSelfStudyController", "setGameCellPenalt mLteGameCellStats = " + this.mLteGameCellStats + " mNrGameCellStats = " + this.mNrGameCellStats);
                    if (stringToInt3 == 1 && this.mLteGameCellStats.isEmpty()) {
                        return;
                    }
                    if (stringToInt3 == 2 && this.mNrGameCellStats.isEmpty()) {
                        return;
                    }
                    if (stringToInt3 == 1 && this.mLteGameCellStats.containsKey(str4)) {
                        doCellActionForGame(str4, stringToInt3, ((Integer) this.mLteGameCellStats.get(str4)).intValue(), false);
                        this.mSetPenaltTime = System.currentTimeMillis();
                        this.mTop2Cell.remove(str);
                        return;
                    } else {
                        if (stringToInt3 == 2 && this.mNrGameCellStats.containsKey(str4)) {
                            doCellActionForGame(str4, stringToInt3, ((Integer) this.mNrGameCellStats.get(str4)).intValue(), false);
                            this.mSetPenaltTime = System.currentTimeMillis();
                            this.mTop2Cell.remove(str);
                            return;
                        }
                        return;
                    }
                }
                return;
            }
            if (!this.isStaticCell || SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                return;
            }
            this.mSetPenaltTime = System.currentTimeMillis();
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone == null) {
                return;
            }
            int phoneId = activeDataPhone.getPhoneId();
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).revertTeleAction(2, TelephonyActionBase.ActionReason.GAME_OPT.value(), Integer.valueOf(phoneId), String.valueOf(0), Integer.valueOf(stringToInt3 == 1 ? 1 : 0), this.tmManager.getNetworkOperatorForPhone(phoneId), Integer.valueOf(stringToInt2), Integer.valueOf(stringToInt));
            this.mTop2Cell.remove(str);
        }
    }

    private void sameGameCell(long j) throws Throwable {
        log("minsertDbTime: " + this.minsertDbTime + " curtime: " + j);
        if (this.mEnterGameBattleCount >= 1) {
            long j2 = this.minsertDbTime;
            if ((j2 <= 0 || j - j2 < 900000) && (j2 != 0 || j - this.startGameTime < 900000)) {
                return;
            }
            addOrUpdateGameStallDur(this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, this.mlastGameCellTime), this.oldGameGci, j);
            this.mlastGameCellTime = j;
            if (this.isGameLag) {
                long j3 = this.startGameLagTime;
                if (j3 != 0) {
                    addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, j3) * ((long) this.mlastGameDelay), j, this.mCurrentGameCellStats, "AP");
                    this.startGameLagTime = j;
                }
            }
            saveGameCell(this.maxSavedGameCells, j);
            updateHistoryGameData(this.mCurrentGameCellStats);
        }
    }

    private void addOrUpdateGameStallDur(int i, int i2, long j, long j2, String str, long j3) {
        if (j == 1 || j == 2) {
            String str2 = str + "-" + i2 + "-" + i;
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mCurrentGameCellStats.get(str2);
            Log.d("MtkSelfStudyController", "addOrUpdateGameStallDur dur = " + j2 + " stats = " + concurrentHashMap + " curtime = " + j3);
            if (concurrentHashMap != null) {
                if (((Long) concurrentHashMap.get("rat")).longValue() == j) {
                    long jLongValue = ((Long) concurrentHashMap.get("dur")).longValue();
                    concurrentHashMap.put("dur", Long.valueOf(j2 + jLongValue));
                    concurrentHashMap.put("saveTime", Long.valueOf(j3));
                    log("addOrUpdateGameStallDur  oldDur = " + jLongValue);
                    return;
                }
                return;
            }
            if (this.mCurrentGameCellStats.size() >= 20) {
                return;
            }
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.put("count", 0L);
            concurrentHashMap2.put("dur", Long.valueOf(j2));
            concurrentHashMap2.put("rat", Long.valueOf(j));
            concurrentHashMap2.put("saveTime", Long.valueOf(j3));
            concurrentHashMap2.put("lagdur", 0L);
            concurrentHashMap2.put("lagdur_modem", 0L);
            concurrentHashMap2.put("lagdur_ai", 0L);
            concurrentHashMap2.put("lagdur_tra", 0L);
            concurrentHashMap2.put("lagdur_normal", 0L);
            this.mCurrentGameCellStats.put(str2, concurrentHashMap2);
        }
    }

    private void saveGameCell(int i, final long j) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (this.mCurrentGameCellStats.isEmpty()) {
            return;
        }
        for (Map.Entry entry : this.mCurrentGameCellStats.entrySet()) {
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) entry.getValue();
            if (((Long) concurrentHashMap.get("rat")).longValue() == 1) {
                arrayList.add(entry);
            } else if (((Long) concurrentHashMap.get("rat")).longValue() == 2) {
                arrayList2.add(entry);
            }
        }
        log("saveGameCell1 rat1Entries = " + arrayList + " rat2Entries = " + arrayList2);
        arrayList.removeIf(new Predicate() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MtkSelfStudyController.$r8$lambda$BFNtCTryOv1IC8K4oCwSdcasdes(j, (Map.Entry) obj);
            }
        });
        arrayList2.removeIf(new Predicate() { // from class: com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MtkSelfStudyController.$r8$lambda$hCzkXITIV3n_Sb08pxvRFmo6PBk(j, (Map.Entry) obj);
            }
        });
        log("saveGameCell rat1Entries = " + arrayList + " rat2Entries = " + arrayList2);
        Collections.sort(arrayList, new Comparator(this) { // from class: com.android.phone.MtkSelfStudyController.4
            @Override // java.util.Comparator
            public int compare(Map.Entry entry2, Map.Entry entry3) {
                return ((Long) ((ConcurrentHashMap) entry3.getValue()).get("saveTime")).compareTo((Long) ((ConcurrentHashMap) entry2.getValue()).get("saveTime"));
            }
        });
        Collections.sort(arrayList2, new Comparator(this) { // from class: com.android.phone.MtkSelfStudyController.5
            @Override // java.util.Comparator
            public int compare(Map.Entry entry2, Map.Entry entry3) {
                return ((Long) ((ConcurrentHashMap) entry3.getValue()).get("saveTime")).compareTo((Long) ((ConcurrentHashMap) entry2.getValue()).get("saveTime"));
            }
        });
        while (arrayList.size() > i) {
            this.mCurrentGameCellStats.remove(((Map.Entry) arrayList.remove(arrayList.size() - 1)).getKey());
        }
        while (true) {
            int size = arrayList2.size();
            ConcurrentHashMap concurrentHashMap2 = this.mCurrentGameCellStats;
            if (size <= i) {
                log("saveGameCell mCurrentGameCellStats = " + concurrentHashMap2);
                return;
            }
            concurrentHashMap2.remove(((Map.Entry) arrayList2.remove(arrayList2.size() - 1)).getKey());
        }
    }

    public static /* synthetic */ boolean $r8$lambda$BFNtCTryOv1IC8K4oCwSdcasdes(long j, Map.Entry entry) {
        return j - ((Long) ((ConcurrentHashMap) entry.getValue()).get("saveTime")).longValue() > 1200000;
    }

    public static /* synthetic */ boolean $r8$lambda$hCzkXITIV3n_Sb08pxvRFmo6PBk(long j, Map.Entry entry) {
        return j - ((Long) ((ConcurrentHashMap) entry.getValue()).get("saveTime")).longValue() > 1200000;
    }

    /* JADX WARN: Failed to calculate best type for var: r12v4 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r12v4 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r12v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r12v5 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r12v6 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r12v6 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r12v7 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r12v7 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r12v8 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r12v8 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r1v10 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r1v10 ??, new type: java.lang.StringBuilder
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r20v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r20v1 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r20v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r20v2 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r28v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r28v0 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v11 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v11 ??, new type: java.lang.Long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v14 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v14 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v16 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v16 ??, new type: java.lang.Long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v18 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v18 ??, new type: java.lang.Long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v21 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v21 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v23 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v23 ??, new type: java.lang.Long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v24 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v24 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v25 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v25 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v29 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v29 ??, new type: java.lang.Long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v30 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v30 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v9 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v9 ??, new type: java.lang.Long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /* JADX WARN: Failed to calculate best type for var: r2v9 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v9 ??, new type: java.lang.Long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v1 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v1 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v11 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v11 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v12 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v12 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v2 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v3 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v3 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v4 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v4 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r4v5 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v5 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r5v2 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r5v2 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to calculate best type for var: r6v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r6v1 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to set immutable type for var: r28v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r28v0 ??, new type: long
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:73)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setImmutableType(TypeInferenceVisitor.java:111)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:102)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:102)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r4v1 ??, new type: long
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
        	... 5 more
        */
    private void addOrUpdateGameLagDur(java.lang.String r23, int r24, int r25, long r26, long r28, long r30, java.util.concurrent.ConcurrentHashMap r32, java.lang.String r33) {
        /*
            Method dump skipped, instruction units count: 546
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.phone.MtkSelfStudyController.addOrUpdateGameLagDur(java.lang.String, int, int, long, long, long, java.util.concurrent.ConcurrentHashMap, java.lang.String):void");
    }

    private void getCurrentCellInfoForGame() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null || activeDataPhone.getCurrentCellIdentity() == null) {
            return;
        }
        CellIdentity currentCellIdentity = activeDataPhone.getCurrentCellIdentity();
        String globalCellId = currentCellIdentity.getGlobalCellId();
        if (TextUtils.isEmpty(globalCellId)) {
            return;
        }
        this.oldGameGci = globalCellId;
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            this.oldGamePci = cellIdentityLte.getPci();
            this.oldGameFreq = cellIdentityLte.getEarfcn();
            this.oldGameRat = 1L;
        } else if (currentCellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
            this.oldGamePci = cellIdentityNr.getPci();
            this.oldGameFreq = cellIdentityNr.getNrarfcn();
            this.oldGameRat = 2L;
        } else {
            log("cell type not supported, return");
            return;
        }
        String str = this.oldGameGci + "-" + Integer.toString(this.oldGamePci) + "-" + Integer.toString(this.oldGameFreq);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("count", 0L);
        concurrentHashMap.put("dur", 0L);
        concurrentHashMap.put("lagdur", 0L);
        concurrentHashMap.put("lagdur_modem", 0L);
        concurrentHashMap.put("lagdur_ai", 0L);
        concurrentHashMap.put("lagdur_tra", 0L);
        concurrentHashMap.put("lagdur_normal", 0L);
        concurrentHashMap.put("rat", Long.valueOf(this.oldGameRat));
        concurrentHashMap.put("saveTime", Long.valueOf(System.currentTimeMillis()));
        this.mlastGameCellTime = System.currentTimeMillis();
        this.mCurrentGameCellStats.clear();
        this.mCurrentGameCellStats.put(str, concurrentHashMap);
    }

    private void updateHistoryGameData(ConcurrentHashMap concurrentHashMap) throws Throwable {
        Long l;
        MtkSelfStudyController mtkSelfStudyController = this;
        long j = 0;
        Long l2 = 0L;
        Log.d("MtkSelfStudyController", "updateHistoryGameData map  " + concurrentHashMap + " size = " + concurrentHashMap.size());
        if (concurrentHashMap.size() > 0) {
            for (Map.Entry entry : concurrentHashMap.entrySet()) {
                String[] strArrSplit = ((String) entry.getKey()).split("-");
                if (strArrSplit.length == 3) {
                    String str = strArrSplit[0];
                    int stringToInt = mtkSelfStudyController.parseStringToInt(strArrSplit[1]);
                    int stringToInt2 = mtkSelfStudyController.parseStringToInt(strArrSplit[2]);
                    ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) entry.getValue();
                    if (concurrentHashMap2 != null) {
                        long jLongValue = ((Long) concurrentHashMap2.get("saveTime")).longValue();
                        long jLongValue2 = ((Long) concurrentHashMap2.getOrDefault("dur", l2)).longValue();
                        long jLongValue3 = ((Long) concurrentHashMap2.getOrDefault("lagdur", l2)).longValue();
                        long jLongValue4 = ((Long) concurrentHashMap2.getOrDefault("lagdur_modem", l2)).longValue();
                        long jLongValue5 = ((Long) concurrentHashMap2.getOrDefault("lagdur_ai", l2)).longValue();
                        long jLongValue6 = ((Long) concurrentHashMap2.getOrDefault("lagdur_tra", l2)).longValue();
                        l = l2;
                        long jLongValue7 = ((Long) concurrentHashMap2.getOrDefault("lagdur_normal", l2)).longValue();
                        long jLongValue8 = ((Long) concurrentHashMap2.get("rat")).longValue();
                        long jLongValue9 = ((Long) concurrentHashMap2.get("count")).longValue();
                        if (jLongValue2 <= j) {
                            mtkSelfStudyController = this;
                            l2 = l;
                        } else {
                            log("updateHistoryGameData gci = " + TelephonyUtils.pii(str) + " pci= " + stringToInt + " freq = " + stringToInt2 + " count = " + jLongValue9 + " rat = " + jLongValue8 + " dur = " + jLongValue2 + " lagdur = " + jLongValue3 + " lagdur_modem = " + jLongValue4 + " lagdur_ai = " + jLongValue5 + " lagdur_tra: " + jLongValue6 + " lagdur_normal: " + jLongValue7 + " scene = " + this.curGameScene);
                            if (isNoDelayApiGame()) {
                                insertOrUpdateGameDatabase(jLongValue8, stringToInt2, stringToInt, jLongValue9, jLongValue2, jLongValue7, jLongValue, str, "AI");
                            } else {
                                insertOrUpdateGameDatabase(jLongValue8, stringToInt2, stringToInt, jLongValue9, jLongValue2, jLongValue3, jLongValue, str, "AP");
                                insertOrUpdateGameDatabase(jLongValue8, stringToInt2, stringToInt, jLongValue9, jLongValue2, jLongValue4, jLongValue, str, "MODEM");
                            }
                        }
                    }
                } else {
                    l = l2;
                }
                mtkSelfStudyController = this;
                l2 = l;
                j = 0;
            }
        }
        concurrentHashMap.clear();
    }

    /* JADX WARN: Code duplicated, block: B:29:0x0151 A[Catch: all -> 0x0110, TryCatch #0 {all -> 0x0110, blocks: (B:17:0x0089, B:27:0x014b, B:29:0x0151, B:31:0x016a, B:33:0x0170, B:23:0x0118, B:25:0x013e, B:26:0x0144), top: B:45:0x006f }] */
    /* JADX WARN: Code duplicated, block: B:30:0x0168  */
    /* JADX WARN: Code duplicated, block: B:33:0x0170 A[Catch: all -> 0x0110, TRY_LEAVE, TryCatch #0 {all -> 0x0110, blocks: (B:17:0x0089, B:27:0x014b, B:29:0x0151, B:31:0x016a, B:33:0x0170, B:23:0x0118, B:25:0x013e, B:26:0x0144), top: B:45:0x006f }] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v3, types: [com.android.phone.performance.SelfStudyDatabaseHelper] */
    /* JADX WARN: Type inference failed for: r16v0, types: [int] */
    /* JADX WARN: Type inference failed for: r16v1 */
    /* JADX WARN: Type inference failed for: r16v10 */
    /* JADX WARN: Type inference failed for: r16v2, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r16v3 */
    /* JADX WARN: Type inference failed for: r16v5, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r16v9 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void insertOrUpdateGameDatabase(long j, int i, int i2, long j2, long j3, long j4, long j5, String str, String str2) throws Throwable {
        ArrayList arrayList;
        ArrayList arrayList2;
        int i3;
        Cursor cursor;
        if (!shouldDoActionForGameOpt()) {
            log("shouldDoActionForGameOpt return");
            return;
        }
        if (!SelfStudyControllerBase.mEnterGameOptimizer) {
            log("not EnterGameOptimizer return");
            return;
        }
        int i4 = this.curGameScene;
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        try {
            ?? r16 = i2;
            Cursor gameBadCellByPciAndFreq = this.dbHelper.getGameBadCellByPciAndFreq(j, i, r16, str, i4, str2);
            try {
                log("getGameBadCellByPciAndFreq count = " + gameBadCellByPciAndFreq.getCount() + " table = " + str2);
                try {
                    if (gameBadCellByPciAndFreq.moveToFirst()) {
                        int i5 = gameBadCellByPciAndFreq.getInt(0);
                        float f = gameBadCellByPciAndFreq.getFloat(1);
                        float f2 = gameBadCellByPciAndFreq.getFloat(2);
                        float f3 = gameBadCellByPciAndFreq.getFloat(3);
                        cursor = gameBadCellByPciAndFreq;
                        log("getGameBadCellByPciAndFreq oldCnt = " + f + " olDur = " + f2 + " oldLagDur = " + f3 + " lagdur = " + j4 + " dur = " + j3);
                        float f4 = (float) j2;
                        float f5 = (float) j3;
                        float f6 = f4 / f5;
                        float f7 = (float) j4;
                        float f8 = f7 / f5;
                        this.dbHelper.updateGameBadCell(f4, f5, f7, j5, f6, f8, i5, str2);
                        log("insertOrUpdateGameBadCell averageCnt = " + i4 + "scene = " + f6 + " averageLagTime = " + f8 + "oldCnt = " + f + " olDur = " + f2 + " oldLagDur = " + f3);
                        arrayList = arrayList3;
                    } else {
                        Cursor cursor2 = gameBadCellByPciAndFreq;
                        CellInfo cellInfo = new CellInfo();
                        cellInfo.freq = i;
                        cellInfo.pci = i2;
                        cellInfo.gameDataStallDur = j3;
                        cellInfo.gameDataStallLagDur = j4;
                        cellInfo.gameDataStallCount = j2;
                        cellInfo.updateTimeMills = j5;
                        cellInfo.globalCellId = str;
                        if (j == 1) {
                            arrayList = arrayList3;
                            arrayList.add(cellInfo);
                            cursor = cursor2;
                        } else {
                            arrayList = arrayList3;
                            arrayList2 = arrayList4;
                            arrayList2.add(cellInfo);
                            r16 = cursor2;
                        }
                        if (arrayList2.isEmpty()) {
                            i3 = i4;
                        } else {
                            this.dbHelper.insertGameBadCell(arrayList2, 2, i4, this.maxSavedGameCells, str2);
                            i3 = i4;
                        }
                        if (!arrayList.isEmpty()) {
                            this.dbHelper.insertGameBadCell(arrayList, 1, i3, this.maxSavedGameCells, str2);
                        }
                        r16.close();
                    }
                    arrayList2 = arrayList4;
                    r16 = cursor;
                    if (arrayList2.isEmpty()) {
                        this.dbHelper.insertGameBadCell(arrayList2, 2, i4, this.maxSavedGameCells, str2);
                        i3 = i4;
                    } else {
                        i3 = i4;
                    }
                    if (!arrayList.isEmpty()) {
                        this.dbHelper.insertGameBadCell(arrayList, 1, i3, this.maxSavedGameCells, str2);
                    }
                    r16.close();
                } catch (Throwable th) {
                    th = th;
                    Throwable th2 = th;
                    if (r16 == 0) {
                        throw th2;
                    }
                    try {
                        r16.close();
                        throw th2;
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                        throw th2;
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                r16 = gameBadCellByPciAndFreq;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getGameLagOrDur(long j, long j2) {
        long j3 = j - j2;
        long j4 = this.startGameTime;
        return j3 > j - j4 ? j - j4 : j3;
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public String getGameCellResidenceAndLagTime() {
        String str;
        long j = this.curGameRat;
        String str2 = "";
        if ((j == 1 && (this.oldGameRsrp < -114 || this.oldGameSnr < 0)) || (j == 2 && (this.oldGameRsrp < -110 || this.oldGameSnr < 0))) {
            return "";
        }
        try {
            Cursor cursorQuery = this.dbHelper.query("game_bad_cell_table", new String[]{"data_stall_duration", "data_stall_lag_duration", "freq", "pci", "rat", "gci", "update_time_mills"}, null, null, null, null, null);
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.getCount() > 0) {
                        cursorQuery.moveToFirst();
                        while (!cursorQuery.isAfterLast()) {
                            long j2 = cursorQuery.getLong(0);
                            long j3 = cursorQuery.getLong(1);
                            int i = cursorQuery.getInt(2);
                            int i2 = cursorQuery.getInt(3);
                            long j4 = cursorQuery.getLong(4);
                            String string = cursorQuery.getString(5);
                            long j5 = cursorQuery.getLong(6);
                            if (string != null && (str = this.oldGameGci) != null && j4 == this.curGameRat && i == this.oldGameFreq && i2 == this.oldGamePci && string.equals(str)) {
                                str2 = string + "-" + i2 + "-" + i + "-" + j4 + "-" + j2 + "-" + j3 + "-" + j5;
                            }
                            cursorQuery.moveToNext();
                        }
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
            return str2;
        } catch (Exception e) {
            Log.e("MtkSelfStudyController", "getGameCellResidenceAndLagTime error", e);
            return str2;
        }
    }

    private void updateDurOrLagDur(long j) {
        if (this.oldGameRat != 0 && ((this.mEnterGameBattleState && isHasDelayApiGame()) || (isNoDelayApiGame() && (SelfStudyControllerBase.mEnterGameAiOptimizer || isStaticCell())))) {
            addOrUpdateGameStallDur(this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, this.mlastGameCellTime), this.oldGameGci, j);
            this.mlastGameCellTime = j;
        }
        if (isHasDelayApiGame() && this.oldGameRat != 0 && this.isGameLag && this.mEnterGameBattleState) {
            long j2 = this.startGameLagTime;
            if (j2 != 0) {
                addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, j2) * ((long) getLastGameDelay(this.mlastGameDelay)), j, this.mCurrentGameCellStats, "AP");
                this.startGameLagTime = j;
            }
            long j3 = this.startGameLagFromModem;
            if (j3 != 0 && this.isGameLagFromModem) {
                addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, j3), j, this.mCurrentGameCellStats, "MODEM");
                this.startGameLagFromModem = j;
            }
        }
        if (this.oldGameRat == 0 || !isNoDelayApiGame()) {
            return;
        }
        if (SelfStudyControllerBase.mEnterGameAiOptimizer || isStaticCell()) {
            if (this.isGameAiLag) {
                long j4 = this.startGameLagFromGameAi;
                if (j4 != 0) {
                    addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, j4) * ((long) getLastGameDelay(2)), j, this.mCurrentGameCellStats, "AI");
                    this.startGameLagFromGameAi = j;
                }
            }
            if (this.isGameTraModelLag) {
                long j5 = this.startGameLagFromTraModel;
                if (j5 != 0) {
                    addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, j5) * ((long) getLastGameDelay(2)), j, this.mCurrentGameCellStats, "TRA");
                    this.startGameLagFromTraModel = j;
                }
            }
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void getGameAiLagTime(String str, String str2) {
        String str3;
        StringBuilder sb = new StringBuilder("getGameAiLagTime ret: ");
        sb.append(str);
        sb.append(" model: ");
        sb.append(str2);
        String str4 = " isGameAiLag: ";
        sb.append(" isGameAiLag: ");
        sb.append(this.isGameAiLag);
        sb.append(" isGameTraModelLag: ");
        sb.append(this.isGameTraModelLag);
        sb.append("startGameLagFromGameAi: ");
        sb.append(this.startGameLagFromGameAi);
        sb.append(" startGameLagFromTraModel: ");
        sb.append(this.startGameLagFromTraModel);
        log(sb.toString());
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (shouldDoActionForGameOpt() && isNoDelayApiGame() && this.isEnterGame) {
            if (isInHomeOrCompanyScene(this.curGameScene) || isStaticCell()) {
                if ("AI".equals(str2)) {
                    if (!"HIGH".equals(str) || this.isGameAiLag || this.oldGameRat == 0) {
                        if (this.startGameLagFromGameAi != 0 && "LOW".equals(str) && this.isGameAiLag && this.oldGameRat != 0) {
                            long gameLagOrDur = getGameLagOrDur(jCurrentTimeMillis, this.startGameLagFromGameAi) * ((long) getLastGameDelay(2));
                            log("ai lag  stop");
                            addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, gameLagOrDur, jCurrentTimeMillis, this.mCurrentGameCellStats, "AI");
                            this.startGameLagFromGameAi = 0L;
                        }
                        str3 = str;
                    } else {
                        this.startGameLagFromGameAi = jCurrentTimeMillis;
                        log(" set ai lag  time: " + this.startGameLagFromGameAi);
                        str3 = str;
                        str4 = " isGameAiLag: ";
                    }
                    this.isGameAiLag = "HIGH".equals(str3);
                } else {
                    str4 = " isGameAiLag: ";
                    if ("TRADITIONAL".equals(str2)) {
                        if (this.mWorkHandler.hasMessages(41) && ("HIGH".equals(str) || jCurrentTimeMillis - this.startGameLagFromTraModel < 20000)) {
                            recordCount("gameResidentCellTop1LagCnt", "game");
                            this.mWorkHandler.removeMessages(41);
                        }
                        if (this.mWorkHandler.hasMessages(43) && ("HIGH".equals(str) || jCurrentTimeMillis - this.startGameLagFromTraModel < 20000)) {
                            recordCount("gameResidentTop1NextCellLagCnt", "game");
                            this.mWorkHandler.removeMessages(43);
                        }
                        if ("HIGH".equals(str) && !this.isGameTraModelLag && this.oldGameRat != 0) {
                            this.startGameLagFromTraModel = jCurrentTimeMillis;
                            log("set tra lag time: " + this.startGameLagFromTraModel);
                        } else if (this.startGameLagFromTraModel != 0 && "LOW".equals(str) && this.isGameTraModelLag && this.oldGameRat != 0) {
                            addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, this.startGameLagFromTraModel) * ((long) getLastGameDelay(2)), jCurrentTimeMillis, this.mCurrentGameCellStats, "TRA");
                            this.startGameLagFromTraModel = 0L;
                        }
                        this.isGameTraModelLag = "HIGH".equals(str);
                    }
                }
                log("isGameTraModelLag: " + this.isGameTraModelLag + str4 + this.isGameAiLag);
                if (this.isGameTraModelLag && this.isGameAiLag && !SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                    if (isInHomeOrCompanyScene(this.curGameScene) && !this.isSetGameAtCmd[0]) {
                        sendOffsetOrNr2LteAtCmd("offset");
                        this.isSetGameAtCmd[0] = true;
                    }
                    String str5 = this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq + "-" + this.oldGameRat;
                    if (!this.mTop2Cell.isEmpty() && this.mTop2Cell.containsKey(str5) && !this.mWorkHandler.hasMessages(33)) {
                        Handler handler = this.mWorkHandler;
                        handler.sendMessageDelayed(handler.obtainMessage(33, str5), 2000L);
                    }
                    if (this.isLastAiAndTraModelLag) {
                        return;
                    }
                    this.isLastAiAndTraModelLag = true;
                    return;
                }
                this.isLastAiAndTraModelLag = false;
                this.mWorkHandler.removeMessages(33);
            }
        }
    }

    private void setGameDelayLevel(int i) {
        if (shouldSetGameLevel()) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        handleGameBattleStart(jCurrentTimeMillis);
        this.mWorkHandler.removeMessages(1049);
        this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(1049, Boolean.FALSE), 30000L);
        if (isInHomeOrCompanyScene(this.curGameScene) || this.isStaticCell) {
            recordDelayLevelChange(i, jCurrentTimeMillis);
            if (!SelfStudyControllerBase.isBelowMtkDataOptVersionFive()) {
                if (i > 2) {
                    boolean[] zArr = this.isSetGameAtCmd;
                    if (!zArr[0]) {
                        zArr[0] = true;
                        sendOffsetOrNr2LteAtCmd("offset");
                    }
                }
                if (isEnterGameOptimizer() || this.isStaticCell) {
                    this.mCurrentGameDelayLevel = i;
                    processgameDelayChangedForHasDelayGame(i);
                    gameLastDelayLevel(i);
                }
            }
        }
        if (i >= 1) {
            handleGameLagState(jCurrentTimeMillis, i);
        } else {
            handleGameNotLagState(jCurrentTimeMillis);
        }
    }

    private boolean shouldSetGameLevel() {
        return (shouldDoActionForGameOpt() && this.isEnterGame && isHasDelayApiGame() && this.mIsMobileDataOn) ? false : true;
    }

    private void recordDelayLevelChange(int i, long j) {
        int i2;
        long j2 = this.mlastGameLevelTime;
        if (j2 != 0 && (i2 = this.mlastGameDelay) != i) {
            recordGameDelayLevelDuration(j - j2, i2, this.isStaticCell);
        }
        this.mlastGameLevelTime = j;
    }

    private void handleGameBattleStart(long j) {
        int i = this.mEnterGameBattleCount + 1;
        this.mEnterGameBattleCount = i;
        if (i == 1) {
            log("send EVENT_GAME_STATISTIC_20MIN");
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(36), 1200000L);
        }
        if (this.mEnterGameBattleState) {
            return;
        }
        log("start game with level");
        this.startGameTime = j;
        String str = this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq + "-" + this.oldGameRat;
        if (!this.mWorkHandler.hasMessages(37)) {
            Handler handler2 = this.mWorkHandler;
            handler2.sendMessageDelayed(handler2.obtainMessage(37, str), 20000L);
        }
        this.mEnterGameBattleState = true;
        defaultResetFromModemForGame();
        saveGameCellStudyDays();
    }

    private void handleGameLagState(long j, int i) {
        boolean z = this.isGameLag;
        if (z && this.mlastGameDelay != i) {
            addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, this.startGameLagTime) * ((long) getLastGameDelay(this.mlastGameDelay)), j, this.mCurrentGameCellStats, "AP");
            log("mlastGameDelay != gameDelayLevel   mlastGameDelay: " + this.mlastGameDelay + " gameDelayLevel: " + i);
            this.mlastGameDelay = i;
            this.startGameLagTime = j;
        } else if (!z) {
            this.mlastGameDelay = i;
            this.startGameLagTime = j;
            log("setGameDelayLevel startGameLagTime = " + this.startGameLagTime);
        }
        this.isGameLag = true;
        recordCountForGameLevel();
        String str = this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq + "-" + this.oldGameRat;
        if (this.mTop2Cell.isEmpty() || !this.mTop2Cell.containsKey(str) || this.mWorkHandler.hasMessages(33)) {
            return;
        }
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(33, str), 2000L);
    }

    private void handleGameNotLagState(long j) {
        if (this.isGameLag) {
            addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, this.startGameLagTime) * ((long) getLastGameDelay(this.mlastGameDelay)), j, this.mCurrentGameCellStats, "AP");
            log("EVENT_GAME_BOOSTER_GAME_DELAY_CHANGED lag end");
            this.mlastGameDelay = 0;
        }
        this.mWorkHandler.removeMessages(33);
        this.isGameLag = false;
        this.startGameLagTime = 0L;
    }

    private void recordCountForGameLevel() {
        this.mWorkHandler.removeMessages(37);
        if (this.mWorkHandler.hasMessages(39)) {
            recordCount("gameResidentCellModemOffsetLagCnt", "game");
            this.mWorkHandler.removeMessages(39);
        }
        if (this.mWorkHandler.hasMessages(41)) {
            recordCount("gameResidentCellTop1LagCnt", "game");
            this.mWorkHandler.removeMessages(41);
        }
        if (this.mWorkHandler.hasMessages(43)) {
            recordCount("gameResidentTop1NextCellLagCnt", "game");
            this.mWorkHandler.removeMessages(43);
        }
    }

    private void sendOffsetOrNr2LteAtCmd(String str) {
        int i;
        if (MiuiPhoneUtils.isSupportGameResidentCellOptForOS4() || SelfStudyControllerBase.isBelowMtkDataOptVersionFive() || !isInHomeOrCompanyScene(this.curGameScene)) {
            return;
        }
        log("sendOffsetOrNr2LteAtCmd  type: " + str);
        if ("offset".equalsIgnoreCase(str)) {
            i = 3;
        } else {
            i = "nr2lte".equalsIgnoreCase(str) ? 4 : -1;
        }
        if (i == -1) {
            return;
        }
        sendAtCmd(new String[]{String.format("AT+EDMFAPP=20006,106,1,%d", Integer.valueOf(i)), "+EDMFAPP"}, (Message) null, MiuiPhoneUtils.getActiveDataPhoneId());
    }

    private boolean isStaticCell() {
        return (this.isStaticCell || this.isFirst20min) && !SelfStudyControllerBase.isBelowMtkDataOptVersionFive();
    }

    /* JADX WARN: Code duplicated, block: B:72:0x0164 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:7:0x002e  */
    /* JADX WARN: Code duplicated, block: B:92:0x015d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.android.phone.performance.SelfStudyControllerBase
    public int queryResidentCellSenceHomeOrCompany() throws Throwable {
        Throwable th;
        int i;
        String str;
        int i2 = 2;
        try {
            Cursor cursorQuery = this.dbHelper.query("resident_cells_table", new String[]{"global_cell_id", "scene", "pci", "freq", "rat"}, null, null, null, null, null);
            if (cursorQuery == null) {
                if (cursorQuery != null) {
                    return 2;
                }
                cursorQuery.close();
                return 2;
            }
            try {
                if (cursorQuery.getCount() != 0) {
                    int i3 = 2;
                    while (cursorQuery.moveToNext()) {
                        try {
                            String string = cursorQuery.getString(0);
                            int i4 = cursorQuery.getInt(1);
                            int i5 = cursorQuery.getInt(i2);
                            int i6 = cursorQuery.getInt(3);
                            if (string == null || (str = this.oldGameGci) == null || str.length() == 0 || string.length() == 0) {
                                i = i2;
                            } else {
                                int i7 = cursorQuery.getInt(4);
                                if (string.contains("_")) {
                                    String[] strArrSplit = string.split("_");
                                    if (strArrSplit.length >= i2) {
                                        String strTrim = strArrSplit[strArrSplit.length - 1].trim();
                                        String strTrim2 = strArrSplit[strArrSplit.length - i2].trim();
                                        if (this.oldGameGci.length() > strTrim2.length()) {
                                            String strSubstring = this.oldGameGci.substring(strTrim2.length());
                                            if (strSubstring.length() != 0 && strTrim != null) {
                                                try {
                                                    String str2 = strTrim2 + strTrim;
                                                    String str3 = strTrim2 + Long.valueOf(Long.parseLong(strSubstring, 16));
                                                    long j = i7;
                                                    i = i2;
                                                    try {
                                                        if (j == this.curGameRat && i6 == this.oldGameFreq && i5 == this.oldGamePci && str3.equals(str2)) {
                                                            Log.d("MtkSelfStudyController", "queryResidentCellSenceHomeOrCompany gciFromDb= " + TelephonyUtils.pii(str2) + " gci = " + TelephonyUtils.pii(str3) + " mccmnc = " + strTrim2 + " oldGameGci = " + this.oldGameGci + " scene = " + i4);
                                                            i3 = i4;
                                                        }
                                                    } catch (NumberFormatException e) {
                                                        e = e;
                                                        Log.e("MtkSelfStudyController", "Invalid hex format: " + strSubstring, e);
                                                    }
                                                } catch (NumberFormatException e2) {
                                                    e = e2;
                                                    i = i2;
                                                }
                                            }
                                        }
                                    }
                                }
                                i = i2;
                            }
                            i2 = i;
                        } catch (Throwable th2) {
                            th = th2;
                            i2 = i3;
                            try {
                                try {
                                    cursorQuery.close();
                                    throw th;
                                } catch (Throwable th3) {
                                    th.addSuppressed(th3);
                                    throw th;
                                }
                            } catch (Exception e3) {
                                e = e3;
                            }
                        }
                    }
                    try {
                        cursorQuery.close();
                        return i3;
                    } catch (Exception e4) {
                        e = e4;
                        i2 = i3;
                    }
                } else {
                    if (cursorQuery != null) {
                        return 2;
                    }
                    try {
                        cursorQuery.close();
                        return 2;
                    } catch (Exception e5) {
                        e = e5;
                        i2 = 2;
                    }
                }
            } catch (Throwable th4) {
                th = th4;
            }
        } catch (Exception e6) {
            e = e6;
        }
        Log.e("MtkSelfStudyController", "queryResidentCellSenceHomeOrCompany error", e);
        return i2;
    }

    private void saveGameCellStudyDays() {
        this.enterGameResidentDays = this.mPreferenceUtils.getInt("game_resident_enter_days", 0);
        long j = this.mPreferenceUtils.getLong("last_enter_game_resident_times", 0L);
        log("lastEnterGameResidentTimes = " + j + " enterGameResidentDays: " + this.enterGameResidentDays + " getCurrentDay: " + getCurrentDay(System.currentTimeMillis()) + " lastEnterGameResidentTimes: " + getCurrentDay(j));
        if (this.enterGameResidentDays == 0 || (j > 0 && getCurrentDay(System.currentTimeMillis()) - getCurrentDay(j) >= 1)) {
            this.enterGameResidentDays++;
            this.mPreferenceUtils.putLong("last_enter_game_resident_times", System.currentTimeMillis());
            this.mPreferenceUtils.putInt("game_resident_enter_days", this.enterGameResidentDays);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public boolean isGameResidentLag() {
        return this.isGameLag;
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public boolean enableNr(boolean z) {
        ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).triggerSmartNetworkOptimization(z);
        return true;
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void registerGameBoosterGameDelayListener() {
        GameBoosterGameDelayListener gameBoosterGameDelayListener;
        NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
        if (networkMetricsController == null || (gameBoosterGameDelayListener = this.mGameBoosterGameDelayListener) == null) {
            return;
        }
        networkMetricsController.registerGameDelayListener(gameBoosterGameDelayListener);
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void unRegisterGameBoosterGameDelayListener() {
        GameBoosterGameDelayListener gameBoosterGameDelayListener;
        NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
        if (networkMetricsController == null || (gameBoosterGameDelayListener = this.mGameBoosterGameDelayListener) == null) {
            return;
        }
        networkMetricsController.unregisterGameDelayListener(gameBoosterGameDelayListener);
    }

    public class GameBoosterGameDelayListener implements NetworkMetricsController.GameDelayListener {
        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameUpdateStatusChanged(boolean z) {
        }

        public GameBoosterGameDelayListener() {
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameDelayChanged(int i) {
            ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.obtainMessage(1047, Integer.valueOf(i)).sendToTarget();
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameStatusChanged(int i, String str) {
            Message messageObtainMessage = ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.obtainMessage(1048, str);
            messageObtainMessage.arg1 = i;
            ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.sendMessage(messageObtainMessage);
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameBattleStatusChanged(boolean z) {
            ((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.sendMessage(((SelfStudyControllerBase) MtkSelfStudyController.this).mWorkHandler.obtainMessage(1049, Boolean.valueOf(z)));
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected boolean isCellularNetworkSwitchOn() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "gamebooster_cellular_network", 1) == 1;
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void notifyCloudDataChanged(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        SmartNetworkGameController smartNetworkGameController = SmartNetworkGameController.getInstance();
        if (smartNetworkGameController != null) {
            smartNetworkGameController.notifyCloudDataChanged(cloudData);
        }
    }
}
