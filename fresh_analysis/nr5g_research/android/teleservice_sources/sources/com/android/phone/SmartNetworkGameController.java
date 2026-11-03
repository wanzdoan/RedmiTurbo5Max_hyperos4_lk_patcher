package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.performance.SelfStudyControllerBase;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.services.telephony.relay.offlinecall.OfflineUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.ot.pubsub.g.f;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class SmartNetworkGameController {
    private static SmartNetworkGameController sInstance;
    private final List GAME_WHITELIST_PACKAGES;
    private boolean isGameReallyLag;
    private boolean isTencentGame;
    private long mApGameLagStartTime;
    private long mApGameLagStopTime;
    private ContentObserver mCellularNetworkStatusObserver;
    public volatile boolean mCellularNetworkSwitchOn;
    private ContentResolver mContentResolver;
    private Context mContext;
    private int mCurrentGameDelay;
    private int mCurrentGameDelayLevel;
    private String mCurrentGlobalCellId;
    private boolean mCurrentIsGameDelay;
    private long mDisableSaTime;
    private GameBoosterGameDelayListener mGameBoosterGameDelayListener;
    public int mGameBoosterGameType;
    private final ConcurrentHashMap mGameStatsList;
    private ContentObserver mGameStatusObserver;
    private Handler mHandler;
    private boolean mIsIgnoreStuck;
    public volatile boolean mIsInGame;
    private boolean mIsMobileDataOn;
    private boolean mIsSupportSnsOptimization;
    private int mLastDelayLevel;
    private long mLastDelayLevelStartTimes;
    private long mLastEnterGameTime;
    private String mLastGlobalCellId;
    private long mMdGameLagStartTime;
    private long mMdGameLagStopTime;
    private NetworkMetricsController mNetworkMetricsController;
    private String mOptimizationSaGlobalCellId;
    private ConcurrentHashMap mOtherGameStats;
    private final BroadcastReceiver mReceiver;
    private long mRecoveryTime;
    private SelfStudyControllerBase mSelfStudyController;
    private SmartNetworkGameDelayListener mSmartNetworkGameDelayListener;
    private int mTempDelayLevel;
    private String mTempTopGamePackage;
    private ConcurrentHashMap mTencentGameStats;
    private String mTopGamePackage;
    private long otherGameLagStartTime;
    private static final List COMMON_GAME_DELAY_THRESHOLDS_PACKAGES = Arrays.asList("com.tencent.tmgp.sgame");
    private static final int[] sCommonGameDelayThresholds = {100, 200, MiRilHook.HOOK_ID_START_URC_BY_AT};
    private static final int[] sLolmGameDelayThresholds = {80, 200, MiRilHook.HOOK_ID_START_URC_BY_AT};
    private static final int[] sJkchessGameDelayThresholds = {100, MiRilHook.HOOK_ID_START_URC_BY_AT, ResponseObject.RELAY_FAILED_CODE};
    private int mCurrentNrRsrp = Integer.MAX_VALUE;
    private int mCurrentLteOrNrRsrp = Integer.MAX_VALUE;
    private long mTriggeredDataStallTime = 0;
    private long mDeleteOutOfDateSmartNetworkGameCellTime = 0;
    private TeleCallBack mTeleCallBack = null;
    private int mDelayLeve1Count = 0;
    private int mDelayLeve2Count = 0;
    private boolean mSmartNetworkOptimizationRst = true;
    private boolean isGameBattle = false;
    public boolean isGameOpt = false;
    private int mNrRsrpWeakThreshold = -100;
    private int mModemGameDelayTimeThreshold = OfflineUtils.Offline.SAMPLE_RATE_VALUE_8KHZ;
    private int mGameDelayCountsCheckTime = 30000;
    public int gameType = -1;
    public int[] gameStatistic = new int[10];
    private List mFixedCycleGameDelays = new ArrayList(4);
    private int[] mCloudDelayCheckTime = {8, 30, 4, 3, 2, 3, 2, 10, 5, 1};
    private long mDataStallTime = 0;
    private boolean mGameNetworkSwitchStatus = true;

    class TeleCallBack implements TelephonyStatAdapter.ICallBack {
        TeleCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            SmartNetworkGameController.this.mHandler.obtainMessage(3, i, 0, signalStrength).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            SmartNetworkGameController.this.mHandler.obtainMessage(5, i, 0, cellLocation).sendToTarget();
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

    class SmartNetworkGameDelayListener implements SelfStudyControllerBase.GameDelayListener {
        SmartNetworkGameDelayListener() {
        }

        @Override // com.android.phone.performance.SelfStudyControllerBase.GameDelayListener
        public void onGameDelayChanged(boolean z) {
            SmartNetworkGameController.this.mHandler.obtainMessage(2, Boolean.valueOf(z)).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerGameDelayListener() {
        SelfStudyControllerBase selfStudyControllerBase = this.mSelfStudyController;
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.registerGameDelayListener(this.mSmartNetworkGameDelayListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterGameDelayListener() {
        SelfStudyControllerBase selfStudyControllerBase = this.mSelfStudyController;
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.unregisterGameDelayListener(this.mSmartNetworkGameDelayListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSelfStudyGameBoosterGameDelayListener() {
        SelfStudyControllerBase selfStudyControllerBase = this.mSelfStudyController;
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.registerGameBoosterGameDelayListener();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterSelfStudyBoosterGameDelayListener() {
        SelfStudyControllerBase selfStudyControllerBase = this.mSelfStudyController;
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.unRegisterGameBoosterGameDelayListener();
        }
    }

    class SmartNetworkGameHandler extends Handler {
        public SmartNetworkGameHandler() {
            super(MiuiThreadUtil.getSmartNetworkWorkThread());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Phone activeDataPhone;
            ServiceState serviceState;
            SmartNetworkGameController.log("handle message msg.what = " + message.what);
            switch (message.what) {
                case 1:
                    int iIntValue = ((Integer) message.obj).intValue();
                    if (iIntValue != 0) {
                        if (iIntValue != 1) {
                            if (iIntValue == 3) {
                                if (SmartNetworkGameController.this.mIsInGame && (SmartNetworkGameController.this.mDelayLeve1Count >= 2 || SmartNetworkGameController.this.mDelayLeve2Count >= 2)) {
                                    SmartNetworkGameController.this.handleGameStall();
                                }
                                SmartNetworkGameController.this.mDelayLeve1Count = 0;
                                SmartNetworkGameController.this.mDelayLeve2Count = 0;
                                break;
                            } else if (iIntValue != 4) {
                                if (iIntValue != 5) {
                                    if (iIntValue != 6) {
                                        if (iIntValue != 7) {
                                            if (iIntValue == 9 && (activeDataPhone = MiuiPhoneUtils.getActiveDataPhone()) != null && (serviceState = activeDataPhone.getServiceState()) != null) {
                                                if (serviceState.getDataRegState() == 1 || (serviceState.getRilDataRadioTechnology() != 14 && serviceState.getRilDataRadioTechnology() != 19)) {
                                                    SmartNetworkGameController.log("reset optimization because failed to switch to LTE");
                                                    SmartNetworkGameController.this.resetSmartNetworkGameOptimization(false);
                                                }
                                            }
                                            break;
                                        } else if (SmartNetworkGameController.this.mIsInGame && SmartNetworkGameController.this.mCurrentGameDelayLevel >= 2) {
                                            SmartNetworkGameController.this.handleGameStall();
                                            break;
                                        }
                                    } else if (SmartNetworkGameController.this.mIsInGame && SmartNetworkGameController.this.mCurrentGameDelayLevel >= 1) {
                                        SmartNetworkGameController.this.handleGameStall();
                                        break;
                                    }
                                } else if (SmartNetworkGameController.this.mIsInGame) {
                                    if ((SmartNetworkGameController.this.mCurrentIsGameDelay && SmartNetworkGameController.this.mGameBoosterGameType == 0) || SmartNetworkGameController.this.mCurrentGameDelay >= 460) {
                                        SmartNetworkGameController.this.handleGameStall();
                                        break;
                                    }
                                }
                            } else if (SmartNetworkGameController.this.mIsInGame) {
                                if ((SmartNetworkGameController.this.mCurrentIsGameDelay && SmartNetworkGameController.this.mGameBoosterGameType == 0) || SmartNetworkGameController.this.mCurrentGameDelayLevel == 3) {
                                    SmartNetworkGameController.this.handleGameStall();
                                    break;
                                }
                            }
                        } else if ((SmartNetworkGameController.this.mCurrentIsGameDelay && SmartNetworkGameController.this.mGameBoosterGameType == 0) || SmartNetworkGameController.this.mCurrentGameDelayLevel == 3) {
                            SmartNetworkGameController.this.mDelayLeve1Count++;
                            SmartNetworkGameController.log("mDelayLeve1Count = " + SmartNetworkGameController.this.mDelayLeve1Count);
                            break;
                        }
                    } else if ((SmartNetworkGameController.this.mCurrentIsGameDelay && SmartNetworkGameController.this.mGameBoosterGameType == 0) || SmartNetworkGameController.this.mCurrentGameDelay >= 460) {
                        SmartNetworkGameController.this.mDelayLeve2Count++;
                        SmartNetworkGameController.log("mDelayLeve2Count = " + SmartNetworkGameController.this.mDelayLeve2Count);
                        break;
                    }
                    break;
                case 2:
                    SmartNetworkGameController.this.processGameDelayChanged(((Boolean) message.obj).booleanValue());
                    break;
                case 3:
                    SmartNetworkGameController.this.processSignalStrengthEvent(message.arg1, (SignalStrength) message.obj);
                    break;
                case 4:
                    SmartNetworkGameController.this.mIsMobileDataOn = ((Boolean) message.obj).booleanValue();
                    break;
                case 5:
                    SmartNetworkGameController.this.processCellLocationChanged((CellLocation) message.obj, message.arg1);
                    break;
                case 6:
                    SmartNetworkGameController.this.resetSmartNetworkGameOptimization(true);
                    if (SmartNetworkGameController.this.mSelfStudyController != null) {
                        SmartNetworkGameController.this.mSelfStudyController.resetGameOffsetOptimization();
                    }
                    SmartNetworkGameController.this.mFixedCycleGameDelays.clear();
                    break;
                case 7:
                    SmartNetworkGameController.this.mCurrentGameDelay = 0;
                    SmartNetworkGameController smartNetworkGameController = SmartNetworkGameController.this;
                    smartNetworkGameController.mGameBoosterGameType = message.arg1;
                    smartNetworkGameController.mTopGamePackage = (String) message.obj;
                    SmartNetworkGameController smartNetworkGameController2 = SmartNetworkGameController.this;
                    if (smartNetworkGameController2.mGameBoosterGameType != 0) {
                        smartNetworkGameController2.isTencentGame = true;
                    }
                    break;
                case 8:
                    SmartNetworkGameController.this.mCurrentGameDelay = ((Integer) message.obj).intValue();
                    SmartNetworkGameController smartNetworkGameController3 = SmartNetworkGameController.this;
                    int gameDelayLevel = smartNetworkGameController3.getGameDelayLevel(smartNetworkGameController3.mCurrentGameDelay);
                    if (SmartNetworkGameController.this.mLastDelayLevel != gameDelayLevel) {
                        SmartNetworkGameController.this.calcGameDelayLevelDuration(gameDelayLevel);
                    }
                    if (SmartNetworkGameController.this.mSelfStudyController != null && SmartNetworkGameController.this.mSelfStudyController.GAME_WHITELIST_PACKAGES.contains(SmartNetworkGameController.this.mTopGamePackage)) {
                        SmartNetworkGameController.this.mLastDelayLevel = gameDelayLevel;
                        SmartNetworkGameController smartNetworkGameController4 = SmartNetworkGameController.this;
                        int i = smartNetworkGameController4.mGameBoosterGameType;
                        if (i == 2) {
                            smartNetworkGameController4.processGameBoosterGameDelayChangedForRealTimeDelayGame(gameDelayLevel);
                        } else if (i == 1) {
                            smartNetworkGameController4.processGameBoosterGameDelayChangedForFixedCycleGame(gameDelayLevel);
                        }
                        break;
                    }
                    break;
                case 9:
                    MiuiSettings.SettingsCloudData.CloudData cloudData = (MiuiSettings.SettingsCloudData.CloudData) message.obj;
                    SmartNetworkGameController.this.mNrRsrpWeakThreshold = cloudData.getInt("nr_rsrp_weak_threshold", -100);
                    String string = cloudData.getString("delay_check_time", (String) null);
                    if (string != null) {
                        String[] strArrSplit = string.trim().split(",");
                        if (strArrSplit.length == SmartNetworkGameController.this.mCloudDelayCheckTime.length) {
                            for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                                SmartNetworkGameController.this.mCloudDelayCheckTime[i2] = Integer.parseInt(strArrSplit[i2]);
                            }
                        }
                    }
                    break;
                case 10:
                    boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                    long jElapsedRealtime = SystemClock.elapsedRealtime();
                    if (SmartNetworkGameController.this.isGameBattle != zBooleanValue) {
                        SmartNetworkGameController.this.isGameBattle = zBooleanValue;
                        SmartNetworkGameController smartNetworkGameController5 = SmartNetworkGameController.this;
                        if (zBooleanValue) {
                            smartNetworkGameController5.mLastDelayLevelStartTimes = jElapsedRealtime;
                            if ("com.tencent.tmgp.sgame".equals(SmartNetworkGameController.this.mTempTopGamePackage)) {
                                SmartNetworkGameController.this.mHandler.sendMessage(SmartNetworkGameController.this.mHandler.obtainMessage(12, 1, 0));
                            }
                        } else {
                            smartNetworkGameController5.calcGameDelayLevelDuration(smartNetworkGameController5.mLastDelayLevel);
                            SmartNetworkGameController.this.mLastDelayLevelStartTimes = 0L;
                            SmartNetworkGameController.this.clearGameBefiteStatistic();
                        }
                    }
                    break;
                case 11:
                    SmartNetworkGameController.this.isGameBattle = false;
                    break;
                case 12:
                    if (SmartNetworkGameController.this.mIsInGame) {
                        SmartNetworkGameController smartNetworkGameController6 = SmartNetworkGameController.this;
                        int i3 = message.arg1;
                        smartNetworkGameController6.gameType = i3;
                        int i4 = message.arg2;
                        int[] iArr = smartNetworkGameController6.gameStatistic;
                        iArr[i4] = 0;
                        if (i3 == 1) {
                            iArr[i4] = smartNetworkGameController6.mLastDelayLevel;
                        } else if (i3 == 2 && smartNetworkGameController6.mCurrentIsGameDelay) {
                            SmartNetworkGameController.this.gameStatistic[i4] = 1;
                        }
                        SmartNetworkGameController.this.mHandler.sendMessageDelayed(SmartNetworkGameController.this.mHandler.obtainMessage(12, SmartNetworkGameController.this.gameType, (i4 + 1) % 10), 1000L);
                        break;
                    }
                    break;
                case 13:
                    if (SmartNetworkGameController.this.mIsInGame) {
                        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) message.obj;
                        SmartNetworkGameController smartNetworkGameController7 = SmartNetworkGameController.this;
                        smartNetworkGameController7.getGameDelayDur(smartNetworkGameController7.gameType, "After", concurrentHashMap);
                        SmartNetworkGameController smartNetworkGameController8 = SmartNetworkGameController.this;
                        int i5 = smartNetworkGameController8.gameType;
                        if (i5 == 1) {
                            smartNetworkGameController8.updateGameStats("hok_game_benefit", concurrentHashMap);
                        } else if (i5 == 2) {
                            smartNetworkGameController8.updateGameStats("modem_game_benefit", concurrentHashMap);
                        }
                        break;
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearGameBefiteStatistic() {
        this.gameType = -1;
        this.isGameOpt = false;
        int i = 0;
        while (true) {
            int[] iArr = this.gameStatistic;
            if (i < iArr.length) {
                iArr[i] = 0;
                i++;
            } else {
                this.mHandler.removeMessages(12);
                this.mHandler.removeMessages(13);
                return;
            }
        }
    }

    int getGameDelayLevel(int i) {
        int[] iArr;
        String str = this.mTopGamePackage;
        if (str == null) {
            return 0;
        }
        if (COMMON_GAME_DELAY_THRESHOLDS_PACKAGES.contains(str)) {
            iArr = sCommonGameDelayThresholds;
        } else if (this.mTopGamePackage.equals("com.tencent.lolm")) {
            iArr = sLolmGameDelayThresholds;
        } else {
            iArr = this.mTopGamePackage.equals("com.tencent.jkchess") ? sJkchessGameDelayThresholds : null;
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

    public static SmartNetworkGameController init(SelfStudyControllerBase selfStudyControllerBase) {
        if (sInstance == null) {
            sInstance = new SmartNetworkGameController(selfStudyControllerBase);
        }
        return sInstance;
    }

    public static SmartNetworkGameController getInstance() {
        return sInstance;
    }

    public SmartNetworkGameController(SelfStudyControllerBase selfStudyControllerBase) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.SmartNetworkGameController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    if (networkInfo == null || networkInfo.getType() != 0) {
                        return;
                    }
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        SmartNetworkGameController.this.mHandler.obtainMessage(4, Boolean.TRUE).sendToTarget();
                        return;
                    } else {
                        if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                            SmartNetworkGameController.this.mHandler.obtainMessage(4, Boolean.FALSE).sendToTarget();
                            return;
                        }
                        return;
                    }
                }
                if (action.equals("smart_network_debug_all")) {
                    SmartNetworkGameController.this.unRegisterGameBoosterGameDelayListener();
                    SmartNetworkGameController.this.mHandler.obtainMessage(8, Integer.valueOf(intent.getIntExtra("gameDelay", 0))).sendToTarget();
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        this.isGameReallyLag = false;
        this.isTencentGame = false;
        this.mLastDelayLevel = -1;
        this.mTempDelayLevel = -1;
        this.otherGameLagStartTime = 0L;
        this.mMdGameLagStartTime = 0L;
        this.mMdGameLagStopTime = 0L;
        this.mLastDelayLevelStartTimes = 0L;
        this.mLastEnterGameTime = 0L;
        this.mApGameLagStartTime = 0L;
        this.mApGameLagStopTime = 0L;
        this.mTencentGameStats = new ConcurrentHashMap();
        this.mOtherGameStats = new ConcurrentHashMap();
        this.mGameStatsList = new ConcurrentHashMap();
        this.GAME_WHITELIST_PACKAGES = Arrays.asList("com.miHoYo.Yuanshen", "com.miHoYo.ys.mi", "com.netease.party", "com.netease.party.mi", "com.netease.dwrg.mi", "com.netease.dwrg", "com.tencent.tmgp.sgame", "com.tencent.lolm", "com.tencent.jkchess");
        this.mContext = PhoneGlobals.getInstance();
        this.mHandler = new SmartNetworkGameHandler();
        this.mContentResolver = this.mContext.getContentResolver();
        this.mCellularNetworkSwitchOn = isCellularNetworkSwitchOn();
        registerTelephonyInfoCallBack();
        observeGameStatus(true);
        observeCellularNetworkSwitchStatus(true);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("smart_network_debug_all");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        this.mSelfStudyController = selfStudyControllerBase;
        this.mGameBoosterGameDelayListener = new GameBoosterGameDelayListener();
        this.mSmartNetworkGameDelayListener = new SmartNetworkGameDelayListener();
        if (this.mCellularNetworkSwitchOn) {
            registerGameDelayListener();
            registerGameBoosterGameDelayListener();
            registerSelfStudyGameBoosterGameDelayListener();
        }
        this.mHandler.post(new Runnable() { // from class: com.android.phone.SmartNetworkGameController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mIsSupportSnsOptimization = MiuiPhoneUtils.isSnsOptimizationSupported();
    }

    private void observeGameStatus(boolean z) {
        ContentObserver contentObserver;
        if (z && this.mGameStatusObserver == null) {
            Uri uriFor = Settings.Secure.getUriFor("gb_boosting");
            ContentObserver contentObserver2 = new ContentObserver(this.mHandler) { // from class: com.android.phone.SmartNetworkGameController.2
                @Override // android.database.ContentObserver
                public void onChange(boolean z2) {
                    boolean z3 = SmartNetworkGameController.this.mIsInGame;
                    SmartNetworkGameController smartNetworkGameController = SmartNetworkGameController.this;
                    smartNetworkGameController.mIsInGame = Settings.Secure.getInt(smartNetworkGameController.mContentResolver, "gb_boosting", 0) == 1;
                    SmartNetworkGameController.log("game status mIsInGame = " + SmartNetworkGameController.this.mIsInGame + "lastGameStatus = " + z3);
                    if (z3 && !SmartNetworkGameController.this.mIsInGame) {
                        SmartNetworkGameController.this.mHandler.sendMessageDelayed(SmartNetworkGameController.this.mHandler.obtainMessage(6), 20000L);
                        SmartNetworkGameController.this.mCurrentIsGameDelay = false;
                        SmartNetworkGameController.this.mCurrentGameDelay = 0;
                        SmartNetworkGameController smartNetworkGameController2 = SmartNetworkGameController.this;
                        smartNetworkGameController2.processGameStatistic(smartNetworkGameController2.mIsInGame, SmartNetworkGameController.this.mTempTopGamePackage);
                        return;
                    }
                    if (SmartNetworkGameController.this.mIsInGame) {
                        SmartNetworkGameController.this.mHandler.removeMessages(6);
                        if (SmartNetworkGameController.this.mNetworkMetricsController != null) {
                            SmartNetworkGameController smartNetworkGameController3 = SmartNetworkGameController.this;
                            smartNetworkGameController3.processGameStatistic(smartNetworkGameController3.mIsInGame, SmartNetworkGameController.this.mNetworkMetricsController.getTopAppName());
                        }
                    }
                }
            };
            this.mGameStatusObserver = contentObserver2;
            this.mContentResolver.registerContentObserver(uriFor, false, contentObserver2);
            return;
        }
        if (z || (contentObserver = this.mGameStatusObserver) == null) {
            return;
        }
        this.mContentResolver.unregisterContentObserver(contentObserver);
        this.mGameStatusObserver = null;
    }

    private void observeCellularNetworkSwitchStatus(boolean z) {
        ContentObserver contentObserver;
        if (z && this.mCellularNetworkStatusObserver == null) {
            Uri uriFor = Settings.Secure.getUriFor("gamebooster_cellular_network");
            ContentObserver contentObserver2 = new ContentObserver(this.mHandler) { // from class: com.android.phone.SmartNetworkGameController.3
                @Override // android.database.ContentObserver
                public void onChange(boolean z2) {
                    boolean z3 = SmartNetworkGameController.this.mCellularNetworkSwitchOn;
                    SmartNetworkGameController smartNetworkGameController = SmartNetworkGameController.this;
                    smartNetworkGameController.mCellularNetworkSwitchOn = smartNetworkGameController.isCellularNetworkSwitchOn();
                    if (z3 && !SmartNetworkGameController.this.mCellularNetworkSwitchOn) {
                        SmartNetworkGameController.this.unRegisterGameBoosterGameDelayListener();
                        SmartNetworkGameController.this.unRegisterGameDelayListener();
                        SmartNetworkGameController.this.unRegisterSelfStudyBoosterGameDelayListener();
                        SmartNetworkGameController.this.resetSmartNetworkGameOptimization(true);
                        SmartNetworkGameController.this.mGameNetworkSwitchStatus = false;
                        if (SmartNetworkGameController.this.mSelfStudyController != null) {
                            SmartNetworkGameController.this.mSelfStudyController.resetGameOffsetOptimization();
                            SmartNetworkGameController.this.mSelfStudyController.resetResidentGameCellOptimization(true);
                        }
                        SmartNetworkGameController.this.mFixedCycleGameDelays.clear();
                        if (SmartNetworkGameController.this.mSelfStudyController == null || !ModemProvider.IS_QCOM) {
                            return;
                        }
                        SmartNetworkGameController.this.mSelfStudyController.changeModemGameFeatureStatus(false);
                        return;
                    }
                    if (!SmartNetworkGameController.this.mCellularNetworkSwitchOn || z3) {
                        return;
                    }
                    SmartNetworkGameController.this.registerGameBoosterGameDelayListener();
                    SmartNetworkGameController.this.registerGameDelayListener();
                    SmartNetworkGameController.this.registerSelfStudyGameBoosterGameDelayListener();
                    SmartNetworkGameController.this.mGameNetworkSwitchStatus = true;
                    if (SmartNetworkGameController.this.mSelfStudyController == null || !ModemProvider.IS_QCOM) {
                        return;
                    }
                    SmartNetworkGameController.this.mSelfStudyController.changeModemGameFeatureStatus(true);
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

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCellularNetworkSwitchOn() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "gamebooster_cellular_network", 1) == 1;
    }

    private void triggerSmartNetworkGameOptimization(int i) {
        log("feature disabled by cloud : " + TelephonyCloudController.CLOUD_SMART_NETWORK_GAME_OPT_DISABLED);
        boolean[] saPowerEnable = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSaPowerEnable();
        if (!this.mSmartNetworkOptimizationRst || !this.mIsInGame || saPowerEnable.length == 0 || !saPowerEnable[i] || TelephonyCloudController.CLOUD_SMART_NETWORK_GAME_OPT_DISABLED || this.mIsIgnoreStuck) {
            log("ignore optimization");
            return;
        }
        this.isGameOpt = true;
        if (System.currentTimeMillis() - this.mRecoveryTime < 600000) {
            log("less ten minutes, ignore optimization");
            return;
        }
        log("SMART_NETWORK_OPTIMIZATION");
        ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).triggerSmartNetworkOptimization(false);
        this.mSmartNetworkOptimizationRst = false;
        recordCount(this.mTencentGameStats, "ratSwitch");
        removeDelayCheckMessages();
        this.mOptimizationSaGlobalCellId = this.mCurrentGlobalCellId;
        this.mDisableSaTime = System.currentTimeMillis();
        this.mDataStallTime = SystemClock.elapsedRealtime();
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1, 9), MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    private void removeDelayCheckMessages() {
        this.mHandler.removeMessages(1, 3);
        this.mHandler.removeMessages(1, 1);
        this.mHandler.removeMessages(1, 0);
        this.mHandler.removeMessages(1, 4);
        this.mHandler.removeMessages(1, 5);
        this.mHandler.removeMessages(1, 6);
        this.mHandler.removeMessages(1, 7);
        this.mDelayLeve1Count = 0;
        this.mDelayLeve2Count = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetSmartNetworkGameOptimization(boolean z) {
        if (this.mSmartNetworkOptimizationRst) {
            return;
        }
        recordCount(this.mTencentGameStats, "ratSwitch");
        log("reset SMART_NETWORK_OPTIMIZATION");
        ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).triggerSmartNetworkOptimization(true);
        this.mSmartNetworkOptimizationRst = true;
        this.mRecoveryTime = System.currentTimeMillis();
        this.mDataStallTime = SystemClock.elapsedRealtime();
        removeDelayCheckMessages();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChanged(CellLocation cellLocation, int i) {
        Phone phone;
        CellIdentity currentCellIdentity;
        String globalCellId;
        if (!MiuiPhoneUtils.isDefaultDataSlotId(i) || cellLocation == null || cellLocation.isEmpty() || (phone = MiuiPhoneUtils.getPhone(i)) == null || (currentCellIdentity = phone.getCurrentCellIdentity()) == null || (globalCellId = currentCellIdentity.getGlobalCellId()) == null) {
            return;
        }
        this.mLastGlobalCellId = this.mCurrentGlobalCellId;
        this.mCurrentGlobalCellId = globalCellId;
        log("process cellLocationChanged = " + this.mCurrentGlobalCellId + "mIsInGame = " + this.mIsInGame);
        int currentDataSlotRat = getCurrentDataSlotRat();
        if (this.isGameOpt && this.mIsInGame) {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            getGameDelayDur(this.gameType, "Before", concurrentHashMap);
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(13, concurrentHashMap), 10000L);
            this.isGameOpt = false;
        }
        if (currentDataSlotRat == 0 || !this.mIsInGame) {
            log("error rat or not in game, ignore cell location changed");
        }
    }

    public void getGameDelayDur(int i, String str, ConcurrentHashMap concurrentHashMap) {
        if (concurrentHashMap == null) {
            return;
        }
        int i2 = 0;
        if (i != 1) {
            if (i == 2) {
                int i3 = 0;
                int i4 = 0;
                while (i2 < 10) {
                    if (this.gameStatistic[i2] == 0) {
                        i3++;
                    } else {
                        i4++;
                    }
                    i2++;
                }
                concurrentHashMap.put("normal" + str, Integer.valueOf(i3));
                concurrentHashMap.put("abnormal" + str, Integer.valueOf(i4));
                return;
            }
            return;
        }
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        while (i2 < 10) {
            int i9 = this.gameStatistic[i2];
            if (i9 == 0) {
                i5++;
            } else if (i9 == 1) {
                i6++;
            } else if (i9 == 2) {
                i7++;
            } else if (i9 == 3) {
                i8++;
            }
            i2++;
        }
        concurrentHashMap.put("level0" + str, Integer.valueOf(i5));
        concurrentHashMap.put("level1" + str, Integer.valueOf(i6));
        concurrentHashMap.put("level2" + str, Integer.valueOf(i7));
        concurrentHashMap.put("level3" + str, Integer.valueOf(i8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSignalStrengthEvent(int i, SignalStrength signalStrength) {
        int i2;
        if (!MiuiPhoneUtils.isDefaultDataSlotId(i) || signalStrength == null) {
            return;
        }
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            log("cellSignal = " + cellSignalStrength);
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                int rsrp = ((CellSignalStrengthLte) cellSignalStrength).getRsrp();
                this.mCurrentLteOrNrRsrp = rsrp;
                boolean[] saPowerEnable = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSaPowerEnable();
                if (this.mIsInGame && (i2 = this.mCurrentNrRsrp) != Integer.MAX_VALUE && i2 <= this.mNrRsrpWeakThreshold && rsrp >= -110 && saPowerEnable.length > 0 && saPowerEnable[i]) {
                    this.mCurrentNrRsrp = Integer.MAX_VALUE;
                }
            } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                int ssRsrp = ((CellSignalStrengthNr) cellSignalStrength).getSsRsrp();
                this.mCurrentNrRsrp = ssRsrp;
                this.mCurrentLteOrNrRsrp = ssRsrp;
            } else {
                this.mCurrentLteOrNrRsrp = Integer.MAX_VALUE;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameDelayChanged(boolean z) {
        this.mCurrentIsGameDelay = z;
        log("game delay changed gameDelay = " + z);
        gameMdDelayStatistic(z);
        if (this.mIsMobileDataOn && this.mGameBoosterGameType == 0) {
            boolean z2 = this.mCurrentIsGameDelay;
            Handler handler = this.mHandler;
            if (z2) {
                if (handler.hasMessages(1, 4)) {
                    return;
                }
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, 4), this.mCloudDelayCheckTime[2] * 1000);
                return;
            }
            handler.removeMessages(1, 4);
        }
    }

    public void recordDuration(ConcurrentHashMap concurrentHashMap, String str, long j) {
        if (concurrentHashMap == null) {
            return;
        }
        concurrentHashMap.put(str, Long.valueOf((concurrentHashMap.get(str) != null ? ((Long) concurrentHashMap.get(str)).longValue() : 0L) + j));
    }

    public void recordCount(ConcurrentHashMap concurrentHashMap, String str) {
        if (concurrentHashMap == null) {
            return;
        }
        concurrentHashMap.put(str, Integer.valueOf((concurrentHashMap.get(str) != null ? ((Integer) concurrentHashMap.get(str)).intValue() : 0) + 1));
    }

    public void reportGameStatistic() {
        ConcurrentHashMap concurrentHashMap = this.mGameStatsList;
        if (concurrentHashMap != null && concurrentHashMap.size() > 0) {
            for (String str : this.mGameStatsList.keySet()) {
                HashMap map = new HashMap();
                map.put("Params", this.mGameStatsList.get(str));
                MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, str, map);
            }
        }
        this.mGameStatsList.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateGameStats(String str, ConcurrentHashMap concurrentHashMap) {
        String str2;
        if (concurrentHashMap == null || (str2 = this.mTempTopGamePackage) == null) {
            return;
        }
        concurrentHashMap.put(f.a.e, str2);
        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap(concurrentHashMap);
        ArrayList arrayList = (ArrayList) this.mGameStatsList.get(str);
        if (arrayList == null) {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(concurrentHashMap2);
            this.mGameStatsList.put(str, arrayList2);
        } else {
            arrayList.add(concurrentHashMap2);
        }
        concurrentHashMap.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameStatistic(boolean z, String str) {
        if (str == null || !this.GAME_WHITELIST_PACKAGES.contains(str)) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (z) {
            this.mLastEnterGameTime = jElapsedRealtime;
            this.mTempTopGamePackage = str;
            if ("com.tencent.tmgp.sgame".equals(str) || "com.tencent.lolm".equals(str) || "com.tencent.jkchess".equals(str)) {
                return;
            }
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(12, 2, 0));
            return;
        }
        long j = this.mLastEnterGameTime;
        if (j != 0) {
            long j2 = (jElapsedRealtime - j) / 1000;
            if (this.isTencentGame) {
                calcGameDelayLevelDuration(this.mLastDelayLevel);
                recordDuration(this.mTencentGameStats, "enterGameDur", j2);
                updateGameStats("event_tencent_games_statistic", this.mTencentGameStats);
            } else {
                recordDuration(this.mOtherGameStats, "enterGameDur", j2);
                updateGameStats("event_other_games_statistic", this.mOtherGameStats);
            }
        }
        clearGameBefiteStatistic();
        this.mLastDelayLevelStartTimes = 0L;
        this.mLastDelayLevel = 0;
        this.mLastEnterGameTime = 0L;
        this.mApGameLagStartTime = 0L;
        this.mApGameLagStopTime = 0L;
        this.mMdGameLagStartTime = 0L;
        this.mMdGameLagStopTime = 0L;
        this.isTencentGame = false;
        this.mTempTopGamePackage = "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calcGameDelayLevelDuration(int i) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (i > 2) {
            this.mApGameLagStartTime = jElapsedRealtime;
        } else if (i <= 2 && this.mLastDelayLevel > 2) {
            this.mApGameLagStopTime = jElapsedRealtime;
        }
        long j = this.mLastDelayLevelStartTimes;
        if (j == 0) {
            this.mLastDelayLevelStartTimes = jElapsedRealtime;
            return;
        }
        long j2 = (jElapsedRealtime - j) / 1000;
        int i2 = this.mLastDelayLevel;
        if (i2 == -1 || i2 == 0) {
            recordDuration(this.mTencentGameStats, "level_0", j2);
        } else if (i2 == 1) {
            recordDuration(this.mTencentGameStats, "level_1", j2);
        } else if (i2 == 2) {
            recordDuration(this.mTencentGameStats, "level_2", j2);
        } else if (i2 == 3) {
            recordDuration(this.mTencentGameStats, "level_3", j2);
        }
        this.mLastDelayLevelStartTimes = jElapsedRealtime;
    }

    /* JADX WARN: Code duplicated, block: B:56:0x00cb  */
    /* JADX WARN: Code duplicated, block: B:64:0x00dd  */
    private void gameMdDelayStatistic(boolean z) {
        long j;
        long j2;
        long j3;
        int i;
        if (this.mIsMobileDataOn) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (this.mGameBoosterGameType == 0) {
                if (z) {
                    this.otherGameLagStartTime = jElapsedRealtime;
                    return;
                }
                long j4 = this.otherGameLagStartTime;
                if (j4 != 0 && jElapsedRealtime - j4 < 30000) {
                    recordDuration(this.mOtherGameStats, "mdGameLagDur", (jElapsedRealtime - j4) / 1000);
                }
                this.otherGameLagStartTime = 0L;
                return;
            }
            if (z && !this.isGameReallyLag && (i = this.mCurrentLteOrNrRsrp) != Integer.MAX_VALUE && i < -120) {
                this.isGameReallyLag = true;
                this.mMdGameLagStartTime = jElapsedRealtime;
                this.mTempDelayLevel = this.mLastDelayLevel;
                return;
            }
            if (z) {
                return;
            }
            long j5 = this.mMdGameLagStartTime;
            if (j5 == 0 || !this.isGameReallyLag) {
                return;
            }
            this.isGameReallyLag = false;
            if (jElapsedRealtime - j5 > 2000 && jElapsedRealtime - j5 <= 30000) {
                recordCount(this.mTencentGameStats, "mdGameLagCnt");
                this.mMdGameLagStopTime = jElapsedRealtime;
                recordDuration(this.mTencentGameStats, "mdGameLagDur", (jElapsedRealtime - this.mMdGameLagStartTime) / 1000);
                if (this.mTempDelayLevel <= 2) {
                    recordCount(this.mTencentGameStats, "judgeLagFailCnt");
                }
                if (this.mLastDelayLevel > 2) {
                    recordCount(this.mTencentGameStats, "judgeNotLagFailCnt");
                }
                long j6 = this.mApGameLagStopTime;
                long j7 = this.mApGameLagStartTime;
                long j8 = this.mMdGameLagStartTime;
                if (j6 < j7) {
                    if (j8 > j7) {
                        j2 = this.mMdGameLagStopTime - j8;
                        j = 0;
                    } else {
                        long j9 = this.mMdGameLagStopTime;
                        if (j6 > j8) {
                            j3 = ((j6 - j8) + j9) - j7;
                            j = j7 - j6;
                            j2 = j3;
                        } else {
                            j2 = j9 - j7;
                            j = j7 - j8;
                        }
                    }
                } else if (j8 <= j7) {
                    long j10 = this.mMdGameLagStopTime;
                    if (j10 >= j6) {
                        long j11 = j6 - j7;
                        j = ((j7 - j8) + j10) - j6;
                        j2 = j11;
                    } else {
                        if (j7 != 0 && j6 != 0 && j8 < j6) {
                            j7 = this.mMdGameLagStopTime;
                            if (j7 > j6) {
                                j3 = j6 - j8;
                                j = j7 - j6;
                                j2 = j3;
                            }
                        }
                        j = this.mMdGameLagStopTime - j8;
                        j2 = 0;
                    }
                } else {
                    if (j7 != 0) {
                        j7 = this.mMdGameLagStopTime;
                        if (j7 > j6) {
                            j3 = j6 - j8;
                            j = j7 - j6;
                            j2 = j3;
                        }
                    }
                    j = this.mMdGameLagStopTime - j8;
                    j2 = 0;
                }
                recordDuration(this.mTencentGameStats, "judgeOkDur", j2 / 1000);
                recordDuration(this.mTencentGameStats, "judgeFailDur", j / 1000);
            }
            this.mMdGameLagStartTime = 0L;
            this.mMdGameLagStopTime = 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerGameBoosterGameDelayListener() {
        GameBoosterGameDelayListener gameBoosterGameDelayListener;
        NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
        if (networkMetricsController == null || (gameBoosterGameDelayListener = this.mGameBoosterGameDelayListener) == null) {
            return;
        }
        networkMetricsController.registerGameDelayListener(gameBoosterGameDelayListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterGameBoosterGameDelayListener() {
        GameBoosterGameDelayListener gameBoosterGameDelayListener;
        NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
        if (networkMetricsController == null || (gameBoosterGameDelayListener = this.mGameBoosterGameDelayListener) == null) {
            return;
        }
        networkMetricsController.unregisterGameDelayListener(gameBoosterGameDelayListener);
    }

    class GameBoosterGameDelayListener implements NetworkMetricsController.GameDelayListener {
        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameUpdateStatusChanged(boolean z) {
        }

        GameBoosterGameDelayListener() {
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameDelayChanged(int i) {
            SmartNetworkGameController.this.mHandler.obtainMessage(8, Integer.valueOf(i)).sendToTarget();
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameStatusChanged(int i, String str) {
            Message messageObtainMessage = SmartNetworkGameController.this.mHandler.obtainMessage(7, str);
            messageObtainMessage.arg1 = i;
            SmartNetworkGameController.this.mHandler.sendMessage(messageObtainMessage);
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameBattleStatusChanged(boolean z) {
            SmartNetworkGameController.this.mHandler.sendMessage(SmartNetworkGameController.this.mHandler.obtainMessage(10, Boolean.valueOf(z)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameBoosterGameDelayChangedForFixedCycleGame(int i) {
        if (this.mIsMobileDataOn) {
            log("game delay changed gameDelayLevel = " + i);
            this.mFixedCycleGameDelays.add(Integer.valueOf(i));
            if (this.mFixedCycleGameDelays.size() == 4) {
                if (i != 0 && i != 1) {
                    if (i == 2 || i == 3) {
                        Iterator it = this.mFixedCycleGameDelays.iterator();
                        int i2 = 0;
                        int i3 = 0;
                        while (it.hasNext()) {
                            int iIntValue = ((Integer) it.next()).intValue();
                            if (iIntValue == 2) {
                                i3++;
                            } else if (iIntValue == 3) {
                                i2++;
                            }
                        }
                        int i4 = i2 + i3;
                        if (i4 == 4) {
                            i = 3;
                        } else {
                            i = i4 == 3 ? 460 : MiRilHook.HOOK_ID_START_URC_BY_AT;
                        }
                    } else {
                        i = -1;
                    }
                }
                if (!this.mSmartNetworkOptimizationRst && i != 0) {
                    resetSmartNetworkGameOptimization(false);
                } else if (i == 3 || (i == 2 && this.mCurrentGameDelayLevel == 2)) {
                    handleGameStall();
                }
                this.mCurrentGameDelayLevel = i;
                this.mFixedCycleGameDelays.remove(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameBoosterGameDelayChangedForRealTimeDelayGame(int i) {
        log("game delay changed gameDelayLevel = " + i);
        this.mCurrentGameDelayLevel = i;
        if (this.mIsMobileDataOn) {
            if (i == 3) {
                this.mHandler.removeMessages(1, 0);
                this.mHandler.removeMessages(1, 5);
                if (!this.mHandler.hasMessages(1, 3)) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, 3), this.mCloudDelayCheckTime[1] * 1000);
                }
                if (!this.mHandler.hasMessages(1, 1)) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, 1), this.mCloudDelayCheckTime[4] * 1000);
                }
                if (this.mHandler.hasMessages(1, 4)) {
                    return;
                }
                Message messageObtainMessage = this.mHandler.obtainMessage(1, 4);
                boolean z = SystemClock.elapsedRealtime() - this.mDataStallTime > 3000;
                log("isDataStallMore2s is " + z);
                this.mHandler.sendMessageDelayed(messageObtainMessage, (this.mIsSupportSnsOptimization && z) ? (this.mCloudDelayCheckTime[9] * 1000) + ResponseObject.RELAY_FAILED_CODE : this.mCloudDelayCheckTime[3] * 1000);
                return;
            }
            if (i == 2) {
                this.mHandler.removeMessages(1, 0);
                this.mHandler.removeMessages(1, 5);
                this.mHandler.removeMessages(1, 1);
                this.mHandler.removeMessages(1, 4);
                if (this.mHandler.hasMessages(1, 7)) {
                    return;
                }
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, 7), this.mCloudDelayCheckTime[8] * 1000);
                return;
            }
            Handler handler = this.mHandler;
            if (i == 1) {
                handler.removeMessages(1, 0);
                this.mHandler.removeMessages(1, 5);
                this.mHandler.removeMessages(1, 1);
                this.mHandler.removeMessages(1, 4);
                this.mHandler.removeMessages(1, 7);
                if (this.mHandler.hasMessages(1, 6)) {
                    return;
                }
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, 6), this.mCloudDelayCheckTime[7] * 1000);
                return;
            }
            handler.removeMessages(1, 0);
            this.mHandler.removeMessages(1, 5);
            this.mHandler.removeMessages(1, 1);
            this.mHandler.removeMessages(1, 4);
            this.mHandler.removeMessages(1, 7);
            this.mHandler.removeMessages(1, 6);
        }
    }

    private int getCurrentDataSlotRat() {
        ServiceState serviceState;
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null || (serviceState = activeDataPhone.getServiceState()) == null) {
            log("null phone/ServiceState");
            return 0;
        }
        if (serviceState.getRilDataRadioTechnology() == 20) {
            return 2;
        }
        if (serviceState.getRilDataRadioTechnology() == 14) {
            return 1;
        }
        log("unavailable HST RAT " + serviceState.getRilDataRadioTechnology());
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGameStall() {
        SelfStudyControllerBase selfStudyControllerBase;
        removeDelayCheckMessages();
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if ("46045".equals(MiuiPhoneUtils.getNetworkNumeric(defaultDataSlotId))) {
            return;
        }
        this.isGameOpt = true;
        if (!this.mSmartNetworkOptimizationRst) {
            resetSmartNetworkGameOptimization(false);
            return;
        }
        if (!MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) || (selfStudyControllerBase = this.mSelfStudyController) == null) {
            return;
        }
        if ((ModemProvider.IS_QCOM && !this.mIsSupportSnsOptimization) || selfStudyControllerBase.hasTriggeredOffsetOptimizationForCurrentCell()) {
            if (!MiuiPhoneUtils.is5GSaRegistered(defaultDataSlotId) || this.mGameBoosterGameType == 0) {
                return;
            }
            triggerSmartNetworkGameOptimization(defaultDataSlotId);
            return;
        }
        if (TelephonyCloudController.CLOUD_SMART_NETWORK_GAME_OPT_DISABLED) {
            return;
        }
        this.mSelfStudyController.sendSelfStudyRequest(101, 1, new int[]{0});
    }

    public void notifyCloudDataChanged(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        this.mHandler.obtainMessage(9, cloudData).sendToTarget();
    }

    public void notifyModemOffsetInd(final int i, final int i2, final int i3, final long j) {
        this.mHandler.post(new Runnable() { // from class: com.android.phone.SmartNetworkGameController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyModemOffsetInd$1(i2, i, i3, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyModemOffsetInd$1(int i, int i2, int i3, long j) {
        if (this.mGameBoosterGameType != 0) {
            recordCount(this.mTencentGameStats, "offsetOpt");
        } else {
            recordCount(this.mOtherGameStats, "offsetOpt");
        }
        if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && i == 0 && i2 != 10) {
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) && MiuiPhoneUtils.is5GSaRegistered(defaultDataSlotId) && this.mGameBoosterGameType != 0) {
                triggerSmartNetworkGameOptimization(defaultDataSlotId);
                return;
            }
            return;
        }
        if (ModemProvider.IS_QCOM) {
            if (i3 != 2) {
                if (i3 == 3) {
                    resetSmartNetworkGameOptimization(false);
                    return;
                } else {
                    this.mDataStallTime = j;
                    return;
                }
            }
            int defaultDataSlotId2 = DefaultSimManager.getDefaultDataSlotId();
            if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId2) && MiuiPhoneUtils.is5GSaRegistered(defaultDataSlotId2) && this.mGameBoosterGameType != 0) {
                triggerSmartNetworkGameOptimization(defaultDataSlotId2);
            }
        }
    }

    public void notifyIgnoreStuckSceneInd(final boolean z) {
        this.mHandler.post(new Runnable() { // from class: com.android.phone.SmartNetworkGameController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyIgnoreStuckSceneInd$2(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyIgnoreStuckSceneInd$2(boolean z) {
        this.mIsIgnoreStuck = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("SmartNetworkGameController", str);
    }
}
