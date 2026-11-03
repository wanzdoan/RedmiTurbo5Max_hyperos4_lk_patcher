package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.performance.CellInfo;
import com.android.phone.performance.SelfStudyControllerBase;
import com.android.phone.performance.SelfStudyDatabaseHelper;
import com.android.phone.signalMap.SignalMapSnsController;
import com.android.phone.signalMap.SignalMapSnsControllerBase;
import com.android.phone.utils.ModemUtils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class QcSelfStudyController extends SelfStudyControllerBase {
    private int mCurrentGameDelay;
    private int mCurrentGameDelayLevel;
    private long mDisableSaTime;
    protected GameBoosterGameDelayListener mGameBoosterGameDelayListener;
    private boolean mIsSupportSnsOptimization;
    private NetworkMetricsController mNetworkMetricsController;
    private BroadcastReceiver mQcomBroadcastReceiver;
    private NetworkSelfRecovery mRecovery;
    private long mRecoveryTime;
    private SignalMapSnsController mSignalMapSnsController;
    protected SmartNetworkGameController mSmartNetworkGameController;
    private int mlastGameDelay;
    private static ArrayList mCacheNrDubiousCellInfoList = new ArrayList();
    private static ArrayList mCacheLteDubiousCellInfoList = new ArrayList();
    private static final Map mActiveBand = new HashMap();
    private boolean isAddSample = false;
    private long mTempCellId = 0;
    private long mTriggedDataStallTime = 0;
    private ServiceState[] mServiceState = new ServiceState[MiuiPhoneUtils.PHONE_COUNT];
    private String mTopGamePackage = "";
    private int maxSavedGameCells = 6;
    private int oldGameFreq = 0;
    private int oldGamePci = 0;
    private long oldGameRat = 0;
    private long curGameRat = 0;
    private int oldGameRsrp = 0;
    private int oldGameSnr = 0;
    private int oldGameRsrq = 0;
    private long startGameLagTime = 0;
    private long startGameTime = 0;
    private long endGameTime = 0;
    private long mDataStallTime = 0;
    private String oldGameGci = "";
    private long mlastGameCellTime = 0;
    private long minsertDbTime = 0;
    private int mEnterGameBattleCount = 0;
    private int mEnterGameBoosterCount = 0;
    private int curGameScene = 2;
    private int enterGameResidentDays = 0;
    private int studyGameResidentCellDays = 3;
    private boolean mEnterGameBattleState = false;
    private String mLastTopGamePackage = "";
    private long mlastGameLevelTime = 0;
    private long mSetPenaltTime = 0;
    private boolean isEnterGame = false;
    private boolean isGameLag = false;
    private boolean isBattle = false;
    private boolean isStaticCell = false;
    private boolean isFirst20min = true;
    private boolean isSetGameTopCell = false;
    private boolean mIsAirPlane = false;
    private boolean mSetGameOptNr2Lte = false;
    private int[] mCloudDelayCheckTime = {8, 30, 4, 3, 2, 3, 2, 10, 5, 1};
    private ConcurrentHashMap mCurrentGameCellStats = new ConcurrentHashMap();
    private ConcurrentHashMap mTop2Cell = new ConcurrentHashMap();
    private List mTopCellForStaticGame = new ArrayList();
    private final ModemUtils mModemUtils = ModemUtils.init(this.mContext);

    public static void make() {
        if (SelfStudyControllerBase.mInstance instanceof QcSelfStudyController) {
            return;
        }
        SelfStudyControllerBase.mInstance = new QcSelfStudyController();
    }

    private QcSelfStudyController() {
        this.mNetworkMetricsController = null;
        this.mRecovery = null;
        super.initCommon();
        this.isSupportPredictLoad = true;
        this.mGameBoosterGameDelayListener = new GameBoosterGameDelayListener();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
            this.mRecovery = NetworkSelfRecovery.make(this.mContext);
        }
        if (isCellularNetworkSwitchStillOn()) {
            registerGameBoosterGameDelayListener();
        }
        this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.QcSelfStudyController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (MiuiPhoneUtils.isSupportSmartNetworkGameOptimization()) {
            this.mSmartNetworkGameController = SmartNetworkGameController.init(this);
        }
        this.mSignalMapSnsController = (SignalMapSnsController) SignalMapSnsControllerBase.getInstance();
        this.mIsSupportSnsOptimization = MiuiPhoneUtils.isSnsOptimizationSupported();
        initBandData();
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void handlePlatformDependentMessage(Message message) {
        log("handlePlatformDependentMessage msg.what is " + message.what);
        int i = message.what;
        if (i == 1) {
            int iIntValue = ((Integer) message.obj).intValue();
            log("EVENT_GAME_DELAY_CHECK type: " + iIntValue);
            delayCheck(iIntValue);
            return;
        }
        if (i == 40) {
            this.mWorkHandler.removeMessages(40);
            return;
        }
        if (i == 46) {
            if (shouldDoActionForGameOptForOS4() && this.isEnterGame && this.isFirst20min) {
                setStaticMessage();
                return;
            }
            return;
        }
        if (i == 1023) {
            handleExitStreamingApps();
            return;
        }
        if (i == 1027) {
            processServiceStateChange(message.arg1, (ServiceState) message.obj);
            return;
        }
        if (i == 1036) {
            handleAiGameOffsetInd();
            return;
        }
        switch (i) {
            case DynamicSarService.WIFI_DATA_STATE_TCP_STATE /* 32 */:
                this.enterGameResidentDays = this.mPreferenceUtils.getInt("game_resident_enter_days", 0);
                log("EVENT_GAME_STATISTIC_5S isEnterGame = " + this.isEnterGame + " enterGameResidentDays: " + this.enterGameResidentDays);
                SelfStudyControllerBase.mEnterGameOptimizer = true;
                recordCount("gameResidentCellOptCnt", "game");
                if (isInHomeOrCompanyScene(this.curGameScene)) {
                    recordCount("ResidentCellCnt", "game");
                }
                if (isInHomeOrCompanyFromXiaoAiScene()) {
                    recordCount("XiaoAiResidentCellCnt", "game");
                }
                this.mWorkHandler.removeMessages(32);
                break;
            case 33:
                setGameCellPenalt((String) message.obj);
                this.mWorkHandler.removeMessages(33);
                break;
            case 34:
                if (MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) {
                    Handler handler = this.mWorkHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(38), 30000L);
                }
                break;
            case MiRilHookBase.EVENT_OEMHOOK_NV_EFS_SYNC /* 35 */:
                if (this.mIsMobileDataOn && shouldDoActionForGameQcOpt()) {
                    if (isInHomeScence()) {
                        this.curGameScene = 1;
                    } else if (isInCompanyScence()) {
                        this.curGameScene = 0;
                    }
                    if (isInHomeOrCompanyScene(this.curGameScene) || isMandatoryInHomeOrCompany()) {
                        if (this.mWorkHandler.hasMessages(38)) {
                            this.mWorkHandler.removeMessages(38);
                        }
                        this.isEnterGame = true;
                        setGameSceneProperty(true);
                        enterOptimizationForModemGameResidentCell();
                    } else if ((this.isStaticCell || this.isFirst20min) && shouldDoActionForGameOptForOS4()) {
                        this.isEnterGame = true;
                        log("enter static game scene");
                        if (this.mWorkHandler.hasMessages(38)) {
                            this.mWorkHandler.removeMessages(38);
                        }
                    }
                    if (this.isEnterGame) {
                        startModemModel();
                    }
                    break;
                }
                break;
            case 36:
                gameStatistic20Min();
                break;
            case 37:
                if (shouldDoActionForGameOptForOS4() && isHasDelayApiGame() && !this.isGameLag && this.mEnterGameBattleState) {
                    if (isInHomeOrCompanyScene(this.curGameScene)) {
                        resetActionForGameCell((String) message.obj, true);
                    } else if (this.isStaticCell) {
                        resetActionForGameCell((String) message.obj, false);
                    }
                }
                this.mWorkHandler.removeMessages(37);
                break;
            case 38:
                if (SelfStudyControllerBase.STATIC_GAME_CELL_WHITELIST_PACKAGES.contains(this.mLastTopGamePackage)) {
                    if (this.isEnterGame && isEnterGameOptimizer()) {
                        sameGameCell(System.currentTimeMillis());
                    }
                    log("EVENT_EXIT_GAME_30S : isEnterGame = " + this.isEnterGame + " isHasDelayApiGame = " + isHasDelayApiGame());
                    processGameExit();
                }
                break;
            default:
                switch (i) {
                    case 1047:
                        int iIntValue2 = ((Integer) message.obj).intValue();
                        this.mCurrentGameDelay = iIntValue2;
                        int gameDelayLevel = getGameDelayLevel(iIntValue2);
                        log("mCurrentGameDelay: " + this.mCurrentGameDelay + " gameDelayLevel: " + gameDelayLevel);
                        if (shouldDoActionForGameQcOpt() && this.isEnterGame && isHasDelayApiGame() && this.mIsMobileDataOn) {
                            setGameDelayLevel(gameDelayLevel);
                            break;
                        }
                        break;
                    case 1048:
                        if (shouldDoActionForGameQcOpt()) {
                            this.mCurrentGameDelay = 0;
                            List list = SelfStudyControllerBase.STATIC_GAME_CELL_WHITELIST_PACKAGES;
                            if (list.contains((String) message.obj)) {
                                this.mEnterGameBoosterCount++;
                            }
                            if (!list.contains(this.mTopGamePackage) && list.contains((String) message.obj)) {
                                this.mLastTopGamePackage = (String) message.obj;
                            }
                            this.mTopGamePackage = (String) message.obj;
                            log("mTopGamePackage = " + this.mTopGamePackage + " isHasDelayApiGame(): " + isHasDelayApiGame() + " mLastTopGamePackage: " + this.mLastTopGamePackage + " mEnterGameBoosterCount: " + this.mEnterGameBoosterCount);
                            if (isHasDelayApiGame() && this.mEnterGameBoosterCount == 1) {
                                recordCount("gameResidentCellEnterAllCnt", "game");
                                if (isMandatoryInHomeOrCompany() || isInHomeOrCompanyScene(this.curGameScene)) {
                                    getCurrentCellInfoForGame();
                                    Handler handler2 = this.mWorkHandler;
                                    handler2.sendMessageDelayed(handler2.obtainMessage(32), MirrorDesktopHelper.TIMEOUT_MILLIS);
                                    this.mEnterGameBoosterCount++;
                                    break;
                                } else if (shouldDoActionForGameOptForOS4() && !this.isStaticCell && this.isFirst20min) {
                                    log("enter delay api game for static scene");
                                    recordCount("gameStaticCellEnterAllCnt", "game");
                                    getCurrentCellInfoForGame();
                                    this.mEnterGameBoosterCount++;
                                    setStaticMessage();
                                    break;
                                }
                            }
                        }
                        break;
                    case 1049:
                        this.isBattle = ((Boolean) message.obj).booleanValue();
                        if (shouldDoActionForGameQcOpt() && this.isEnterGame && isHasDelayApiGame()) {
                            if (isInHomeOrCompanyScene(this.curGameScene) || isStaticCell()) {
                                boolean z = this.isBattle;
                                boolean z2 = this.mEnterGameBattleState;
                                if (!z) {
                                    if (!z2) {
                                        log("EVENT_GAME_BATTLE_STATUS_CHANGED false ignored, not in battle");
                                    } else {
                                        this.mWorkHandler.removeMessages(1049, Boolean.FALSE);
                                        long jCurrentTimeMillis = System.currentTimeMillis();
                                        this.endGameTime = jCurrentTimeMillis;
                                        addOrUpdateGameStallDur(this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, this.mlastGameCellTime), this.oldGameGci, this.endGameTime);
                                        this.mlastGameCellTime = this.endGameTime;
                                        if (this.isGameLag) {
                                            log("no battle -is lag");
                                            addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(this.endGameTime, this.startGameLagTime) * ((long) getLastGameDelay(this.mlastGameDelay)), this.endGameTime, this.mCurrentGameCellStats, "AP");
                                            this.startGameLagTime = this.endGameTime;
                                        }
                                        this.mEnterGameBattleState = false;
                                        this.mWorkHandler.removeMessages(37);
                                    }
                                } else if (!z2) {
                                    log("start game with battle");
                                    this.startGameTime = System.currentTimeMillis();
                                    String str = this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq + "-" + this.oldGameRat;
                                    if (!this.mWorkHandler.hasMessages(37)) {
                                        Handler handler3 = this.mWorkHandler;
                                        handler3.sendMessageDelayed(handler3.obtainMessage(37, str), 20000L);
                                    }
                                    this.mEnterGameBattleState = true;
                                    saveGameCellStudyDays();
                                }
                            }
                            break;
                        }
                        break;
                }
                break;
        }
    }

    class RilHookListener implements BaseEvent.IEventListener {
        private RilHookListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            QcSelfStudyController.this.sendSelfStudyRequest(26, 1, new byte[]{0});
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseSelfStudyData(byte[] bArr) {
        int i;
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        int i2 = byteBufferWrap.getInt();
        Log.d("QcSelfStudyController", "parseSelfStudyData eventType is " + i2);
        if (i2 == 7) {
            doRestoreOrUpdate(byteBufferWrap);
            return;
        }
        if (i2 == 8) {
            notifyUser();
            return;
        }
        if (i2 == 11) {
            doRestoreOrUpdateCellRoute_V2(byteBufferWrap);
            return;
        }
        if (i2 == 13) {
            updateRatChangedCount(byteBufferWrap.getInt());
            return;
        }
        if (i2 == 20) {
            updateBadCells(byteBufferWrap);
            return;
        }
        if (i2 == 26) {
            try {
                SelfStudyControllerBase.encryptedKey = byteBufferWrap.getInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (this.mLock) {
                try {
                    this.mLock.notifyAll();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return;
        }
        int i3 = 2;
        boolean z = true;
        if (i2 == 28) {
            try {
                long j = byteBufferWrap.getLong();
                String str = this.tmManager.getNetworkOperatorForPhone(MiuiPhoneUtils.getActiveDataPhone().getPhoneId()) + "_";
                if (this.mIsSubwayStateIn) {
                    i = 1;
                } else if (this.mIsDriveStateIn) {
                    i = i3;
                } else if (this.mIsHstStateIn) {
                    i3 = 3;
                    i = i3;
                } else {
                    i = 0;
                }
                log("EVENT_NOTIFY_MODEM_COLLECT_MULTI_ROUTE unit.cellid is " + j + "lastCellIndex = " + this.mCurrentRouteCellIds[0]);
                String str2 = this.mCurrentRouteCellIds[0];
                if (str2 != null) {
                    this.dbHelper.insertMultiRouteBadCell(str2, str + String.valueOf(j), currentIsEveningPeak(), System.currentTimeMillis(), i);
                    return;
                }
                return;
            } catch (Exception e3) {
                e3.printStackTrace();
                return;
            }
        }
        if (i2 == 101) {
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone == null) {
                return;
            }
            OffsetCell currentCellInfo = getCurrentCellInfo(activeDataPhone);
            int i4 = byteBufferWrap.getInt();
            int i5 = byteBufferWrap.getInt();
            int i6 = byteBufferWrap.getInt();
            int i7 = byteBufferWrap.getInt();
            log("modem offset ind source = " + i4 + " freq = " + i5 + " pci = " + i6 + " action = " + i7);
            if (currentCellInfo != null && currentCellInfo.pci == i6 && currentCellInfo.freq == i5) {
                if (i4 == 2) {
                    handleLiveStreamingOffsetInd(currentCellInfo, activeDataPhone.getPhoneId(), i7);
                    return;
                } else {
                    if (this.mSmartNetworkGameController != null) {
                        handleGameOffsetInd(currentCellInfo, activeDataPhone.getPhoneId(), i4, i7);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if (i2 == 23) {
            try {
                SelfStudyControllerBase.CellUnit cellUnit = new SelfStudyControllerBase.CellUnit();
                cellUnit.cellId = byteBufferWrap.getLong();
                byteBufferWrap.getInt();
                cellUnit.cellPci = byteBufferWrap.getInt();
                cellUnit.cellFreq = byteBufferWrap.getInt();
                log("cellId = " + cellUnit.cellId + " cellFreq " + cellUnit.cellFreq + "  cellPci " + cellUnit.cellPci);
                if (this.isAddSample) {
                    this.mSampingCellInfoList.add(cellUnit);
                } else {
                    this.mGlobalCurrentCellId = String.valueOf(cellUnit.cellFreq) + cellUnit.cellPci;
                    this.mGlobalCurrentCellId = getCombineString(this.mGlobalCurrentCellId, Long.toString(cellUnit.cellId, 16));
                }
                this.isAddSample = false;
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            synchronized (this.mLock) {
                try {
                    this.mLock.notifyAll();
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
            }
            return;
        }
        if (i2 != 24) {
            if (i2 == 303) {
                try {
                    if (byteBufferWrap.getInt() != 1) {
                        z = false;
                    }
                    notifyGameDelay(z);
                    return;
                } catch (Exception e6) {
                    e6.printStackTrace();
                    return;
                }
            }
            if (i2 != 304) {
                return;
            }
            z = byteBufferWrap.getInt() == 1;
            this.mIsIgnoreGameStuck = z;
            SmartNetworkGameController smartNetworkGameController = this.mSmartNetworkGameController;
            if (smartNetworkGameController != null) {
                smartNetworkGameController.notifyIgnoreStuckSceneInd(z);
                return;
            }
            return;
        }
        try {
            SelfStudyControllerBase.CellUnit cellUnit2 = new SelfStudyControllerBase.CellUnit();
            cellUnit2.cellId = byteBufferWrap.getLong();
            byteBufferWrap.getInt();
            cellUnit2.cellPci = byteBufferWrap.getInt();
            cellUnit2.cellFreq = byteBufferWrap.getInt();
            log("unit.cellid is " + cellUnit2.cellId + "  mTempCellId is " + this.mTempCellId);
            long j2 = cellUnit2.cellId;
            if (j2 == this.mTempCellId || cellUnit2.cellFreq <= 0) {
                return;
            }
            this.mTempCellId = j2;
            Iterator it = SelfStudyControllerBase.cbs.iterator();
            while (it.hasNext()) {
                ((SelfStudyControllerBase.CellCallBack) it.next()).onCellLocationChanged(MiuiPhoneUtils.getActiveDataPhone().getPhoneId(), cellUnit2);
            }
        } catch (Exception e7) {
            e7.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseWechatData(byte[] bArr) {
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        if (byteBufferWrap.getInt() != 1) {
            return;
        }
        int i = byteBufferWrap.getInt();
        long j = byteBufferWrap.getLong();
        int i2 = byteBufferWrap.getInt();
        long ddsLteNrCid = getDdsLteNrCid(MiuiPhoneUtils.getActiveDataPhoneId());
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        CellIdentity currentCellIdentity = activeDataPhone.getCurrentCellIdentity();
        OffsetCell currentCellInfo = getCurrentCellInfo(activeDataPhone);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        log("DATA_STALL_RESULT_INFO  rat = " + i + " mdCellId = " + j + " cause = " + i2 + "cellId =" + ddsLteNrCid + " is resident cell = " + isCurrentCellResident());
        if (!isCurrentCellResident() && canTriggerDataStallOptimization(this.mWechatVideoOffsetCells, currentCellInfo, jElapsedRealtime) && shouldDoActionForWechatOpt() && this.isVideoFront) {
            setLteNrDubiousCell(currentCellIdentity, true);
            setDataStallOptimization(1);
            this.isWechatOpt = true;
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void delayProcessWechatEnter() {
        if (shouldDoActionForWechatOpt()) {
            log("delayProcessWechatEnter");
            sendWechatVideoRequest(0, 1, new byte[]{1});
            Intent intent = new Intent("com.android.phone.intent.action.UPDATE_PDCP_DISCARD_TIMER");
            intent.putExtra("discard_timer", 5000);
            this.mContext.sendBroadcast(intent);
            setWechatHistoryDubiousCell();
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void processWechatExit() {
        log("processWechatExit");
        this.mLiveStreamingOffsetCells.clear();
        resetDataStallOptimization();
        resetCacheDubiousCellOpt();
        sendWechatVideoRequest(0, 1, new byte[]{0});
        Intent intent = new Intent("com.android.phone.intent.action.UPDATE_PDCP_DISCARD_TIMER");
        intent.putExtra("discard_timer", -1);
        this.mContext.sendBroadcast(intent);
    }

    private void setWechatHistoryDubiousCell() {
        Cursor cursorQuery = this.dbHelper.query("wechat_video_dubious_cell", new String[]{"rat", "cgi", "mcc", "mnc", "arfcn", "pci", "band"}, null, null, null, null, null);
        if (cursorQuery != null && cursorQuery.moveToFirst()) {
            while (!cursorQuery.isAfterLast()) {
                int i = cursorQuery.getInt(0);
                long j = cursorQuery.getLong(1);
                short s = cursorQuery.getShort(2);
                short s2 = cursorQuery.getShort(3);
                int i2 = cursorQuery.getInt(4);
                short s3 = cursorQuery.getShort(5);
                int i3 = cursorQuery.getInt(6);
                log("set wechat history dubious cell cgi = " + j + " band = " + i3 + " rat = " + i);
                if (i == SelfStudyControllerBase.Rat.Lte.value) {
                    mCacheLteDubiousCellInfoList.add(this.mSignalMapSnsController.constructLteDubiousCellInfo((int) j, s, s2, i2, s3, i3, "CEF:RLF"));
                } else if (i == SelfStudyControllerBase.Rat.Nr.value) {
                    mCacheNrDubiousCellInfoList.add(this.mSignalMapSnsController.constructNrDubiousCellInfo(j, s, s2, i2, s3, i3, "CEF:RLF"));
                }
                cursorQuery.moveToNext();
            }
        }
        cursorQuery.close();
        this.mSignalMapSnsController.setOrResetLteDubiousCellIfNeed(mCacheLteDubiousCellInfoList, true, "dataStallSelfStudy");
        this.mSignalMapSnsController.setOrReserNrDubiousCellIfNeed(mCacheNrDubiousCellInfoList, true, "dataStallSelfStudy");
    }

    protected void sendWechatVideoRequest(int i, int i2, byte[] bArr) {
        if (this.mModemUtils == null) {
            Log.d("QcSelfStudyController", "ModemUtils init fail");
            return;
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[i2 + 8]);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        byteBufferWrap.putInt(i);
        byteBufferWrap.putInt(i2);
        byteBufferWrap.put(bArr);
        if (this.mModemUtils.onHookCommonMsgSyncBySub(176, byteBufferWrap.array(), 0) == null) {
            Log.i("QcSelfStudyController", "onHookCommonMsgSyncBySub return null");
        }
    }

    public long getDdsLteNrCid(int i) {
        if (!SubscriptionManager.isValidPhoneId(i)) {
            return 0L;
        }
        String cellId = TelephonyManagerEx.getDefault().getCellId(i);
        if (TextUtils.isEmpty(cellId)) {
            log("getDdsLteNrCid cellId is empty");
            return 0L;
        }
        try {
            return Long.parseLong(cellId);
        } catch (NumberFormatException unused) {
            log("getDdsLteNrCid invalid cellId = " + cellId);
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLiveStreamingOffsetInd(OffsetCell offsetCell, int i, int i2) {
        if (isVideoDataStallOptimizationSupported() && this.cloudVideoOn) {
            log("handleLiveStreamingOffsetInd data type = " + this.dataType);
            if (MiuiPhoneUtils.isDuringCall()) {
                return;
            }
            int i3 = this.dataType;
            if (i3 == 2 || i3 == 4) {
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                if ((i2 == 0 || i2 == 1) && canTriggerDataStallOptimization(this.mLiveStreamingOffsetCells, offsetCell, jElapsedRealtime)) {
                    int i4 = i2 == 0 ? 0 : 1;
                    setLteNrDubiousCell(MiuiPhoneUtils.getActiveDataPhone().getCurrentCellIdentity(), false);
                    setDataStallOptimization(i4);
                    recordCount("liveStreamingOptCnt", "video");
                    this.hasSendDataStallAction = true;
                    this.isVideoOpt = true;
                }
            }
        }
    }

    private void handleGameOffsetInd(OffsetCell offsetCell, int i, int i2, int i3) {
        if (this.mIsIgnoreGameStuck) {
            return;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if ((i3 == 0 || i3 == 1) && canTriggerDataStallOptimization(this.mGameOffsetCells, offsetCell, jElapsedRealtime)) {
            int i4 = this.mSmartNetworkGameController.mGameBoosterGameType != 0 ? i3 == 1 ? 0 : 1 : 1;
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            setLteNrDubiousCell(activeDataPhone != null ? activeDataPhone.getCurrentCellIdentity() : null, false);
            setDataStallOptimization(i4);
            this.mGameDataStallTime = jElapsedRealtime;
        }
        this.mSmartNetworkGameController.notifyModemOffsetInd(i2, offsetCell.offset, i3, this.mGameDataStallTime);
    }

    private boolean canTriggerDataStallOptimization(List list, OffsetCell offsetCell, long j) {
        if (offsetCell == null) {
            log("canTriggerDataStallOptimization currentCell is null");
            return false;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            OffsetCell offsetCell2 = (OffsetCell) it.next();
            if (offsetCell2.equals(offsetCell)) {
                long j2 = offsetCell2.optimizationTime;
                if (j2 != 0 && j - j2 <= 600000) {
                    return false;
                }
                offsetCell2.optimizationTime = j;
                return true;
            }
        }
        offsetCell.optimizationTime = j;
        list.add(offsetCell);
        return true;
    }

    private void handleAiGameOffsetInd() {
        if (this.mAiGameOffsetCells.size() > 50) {
            log("ai game optimization cells more than 50, ignore");
            return;
        }
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (TelephonyCloudController.CLOUD_SMART_NETWORK_GAME_OPT_DISABLED || activeDataPhone == null) {
            return;
        }
        OffsetCell currentCellInfo = getCurrentCellInfo(activeDataPhone);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mIsIgnoreGameStuck || !canTriggerDataStallOptimization(this.mAiGameOffsetCells, currentCellInfo, jElapsedRealtime)) {
            return;
        }
        setDataStallOptimization(1);
        this.isGameAiOpt = true;
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void resetGameOffsetOptimization() {
        this.mGameOffsetCells.clear();
        this.mAiGameOffsetCells.clear();
        resetDataStallOptimization();
        resetCacheDubiousCellOpt();
    }

    protected void handleExitStreamingApps() {
        log(" handleExitStreamingApps hasSendDataStallAction = " + this.hasSendDataStallAction);
        if (this.hasSendDataStallAction) {
            this.mLiveStreamingOffsetCells.clear();
            resetDataStallOptimization();
            resetCacheDubiousCellOpt();
            this.hasSendDataStallAction = false;
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void processDataTypeChanged(int i) {
        int i2 = this.dataType;
        if (i2 != 4 && i2 != 2 && this.hasSendDataStallAction) {
            this.mWorkHandler.removeMessages(1023);
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1023), 30000L);
        }
        int i3 = this.dataType;
        if (i3 == 4 || i3 == 2) {
            this.mWorkHandler.removeMessages(1023);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void processStopDataStall() {
        handleExitStreamingApps();
    }

    private void resetDataStallOptimization() {
        NetworkMetricsController networkMetricsController;
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (this.mTriggedDataStallTime == 0 || activeDataPhone == null || (networkMetricsController = this.mNetworkMetricsController) == null) {
            return;
        }
        networkMetricsController.resetDataStallOptimization(activeDataPhone.getPhoneId());
        if (isEnterGameOptimizer() || this.isStaticCell) {
            isSetDataStallForGameOpt(false);
        }
    }

    private void resetCacheDubiousCellOpt() {
        this.mSignalMapSnsController.setOrReserNrDubiousCellIfNeed(mCacheNrDubiousCellInfoList, false, "dataStallReset");
        this.mSignalMapSnsController.setOrResetLteDubiousCellIfNeed(mCacheLteDubiousCellInfoList, false, "dataStallReset");
        mCacheNrDubiousCellInfoList.clear();
        mCacheLteDubiousCellInfoList.clear();
    }

    private void updateBadCells(ByteBuffer byteBuffer) {
        List list = parserListCellInfos(byteBuffer);
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
            log("updateBadCells j = " + i2);
            if (i2 >= this.mBadCellListInfos.size()) {
                this.mBadCellListInfos.add((CellInfo) list.get(i));
            }
        }
    }

    private List parserListCellInfos(ByteBuffer byteBuffer) {
        LinkedList linkedList = new LinkedList();
        int i = byteBuffer.getInt();
        Log.d("QcSelfStudyController", "parserListCellInfos length is " + i);
        for (int i2 = 0; i2 < i; i2++) {
            CellInfo cellInfo = new CellInfo();
            cellInfo.setCid(byteBuffer.getLong());
            cellInfo.setRat(byteBuffer.getInt());
            cellInfo.setPci(byteBuffer.getInt());
            cellInfo.setFreq(byteBuffer.getInt());
            cellInfo.setScs(byteBuffer.get());
            cellInfo.setRrcExceptionCount(byteBuffer.getInt());
            cellInfo.setHighBlerCount(byteBuffer.getInt());
            cellInfo.setLowUlGrantCount(byteBuffer.getInt());
            cellInfo.setRlfCount(byteBuffer.getInt());
            cellInfo.setDuration(byteBuffer.getInt());
            cellInfo.setBadRsrpDuration(byteBuffer.getInt());
            cellInfo.setLowDlGrantCount(byteBuffer.getInt());
            cellInfo.setDataType(this.dataType);
            cellInfo.setIsBadCell(calculateBadCellPoints(cellInfo));
            linkedList.add(cellInfo);
        }
        Log.d("QcSelfStudyController", "parserListCellInfos list size is " + linkedList.size());
        return linkedList;
    }

    private void doRestoreOrUpdate(ByteBuffer byteBuffer) {
        restoreOrUpdate(parserListCellInfos(byteBuffer));
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void sendSelfStudyRequest(int i, int i2, byte[] bArr) {
        if (this.mModemUtils == null) {
            Log.d("QcSelfStudyController", "ModemUtils init fail");
            return;
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[i2 + 9]);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        byteBufferWrap.putInt(i);
        byteBufferWrap.putInt(i2 + 1);
        byteBufferWrap.put(bArr);
        if (this.isEndcOrNr5G) {
            byteBufferWrap.put((byte) 1);
        } else {
            byteBufferWrap.put((byte) 0);
        }
        ByteBuffer byteBufferOnHookCommonMsgSyncBySub = this.mModemUtils.onHookCommonMsgSyncBySub(98, byteBufferWrap.array(), 0);
        if (byteBufferOnHookCommonMsgSyncBySub == null) {
            Log.i("QcSelfStudyController", "onHookCommonMsgSyncBySub return null");
            return;
        }
        int i3 = byteBufferOnHookCommonMsgSyncBySub.getInt();
        if (i3 == 6 && bArr[0] == 0) {
            Handler handler = this.mWorkHandler;
            handler.sendMessage(handler.obtainMessage(1009));
        }
        if (i3 == 18 && bArr[0] == 0) {
            Handler handler2 = this.mWorkHandler;
            handler2.sendMessageDelayed(handler2.obtainMessage(1013), 500L);
        } else if (i3 == 1018) {
            this.isAddSample = false;
        } else if (i3 == 1016) {
            this.isAddSample = true;
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void sendSelfStudyRequest(int i, int i2, int[] iArr) {
        if (i == 16) {
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[4]);
            byteBufferWrap.order(ByteOrder.nativeOrder());
            byteBufferWrap.putInt(iArr[0]);
            sendSelfStudyRequest(16, 4, byteBufferWrap.array());
            return;
        }
        ByteBuffer byteBufferWrap2 = ByteBuffer.wrap(new byte[iArr.length * 4]);
        byteBufferWrap2.order(ByteOrder.nativeOrder());
        for (int i3 : iArr) {
            byteBufferWrap2.putInt(i3);
        }
        byte[] bArrArray = byteBufferWrap2.array();
        sendSelfStudyRequest(i, bArrArray.length, bArrArray);
    }

    class QcomSelfStudyReceiver extends BroadcastReceiver {
        public String TAG;

        private QcomSelfStudyReceiver() {
            this.TAG = "QcomSelfStudyReceiver";
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) throws Throwable {
            OffsetCell currentCellInfo;
            if (intent == null) {
                Log.d(this.TAG, "Null action");
                return;
            }
            String action = intent.getAction();
            Log.d(this.TAG, "Receive action is  " + action);
            if ("qualcomm.intent.action.XIAOMI_SELF_STUDY_IND".equalsIgnoreCase(intent.getAction())) {
                final byte[] byteArrayExtra = intent.getByteArrayExtra("BROADCAST_CONTEXT");
                if (byteArrayExtra != null) {
                    try {
                        ((SelfStudyControllerBase) QcSelfStudyController.this).mWorkHandler.post(new Runnable() { // from class: com.android.phone.QcSelfStudyController$QcomSelfStudyReceiver$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onReceive$0(byteArrayExtra);
                            }
                        });
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                Log.d(this.TAG, "error ind data error");
                return;
            }
            if ("qualcomm.intent.action.XIAOMI_WECHAT_IND".equals(intent.getAction())) {
                final byte[] byteArrayExtra2 = intent.getByteArrayExtra("BROADCAST_CONTEXT");
                if (byteArrayExtra2 != null) {
                    try {
                        ((SelfStudyControllerBase) QcSelfStudyController.this).mWorkHandler.post(new Runnable() { // from class: com.android.phone.QcSelfStudyController$QcomSelfStudyReceiver$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onReceive$1(byteArrayExtra2);
                            }
                        });
                        return;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                Log.d(this.TAG, "modem wechat video error ind data error");
                return;
            }
            if ("com.android.phone.intent.action.STREAMING_APPS_OPTIMIZATION_DEBUG".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("action", 2);
                Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
                if (activeDataPhone == null || (currentCellInfo = QcSelfStudyController.this.getCurrentCellInfo(activeDataPhone)) == null) {
                    return;
                }
                QcSelfStudyController.this.handleLiveStreamingOffsetInd(currentCellInfo, activeDataPhone.getPhoneId(), intExtra);
                return;
            }
            if ("selfStudyController.game.intent.debugall".equals(action)) {
                String stringExtra = intent.getStringExtra("todo");
                int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                if ("handleGameStall".equalsIgnoreCase(stringExtra)) {
                    QcSelfStudyController.this.log("debug handleGameStall ");
                    QcSelfStudyController.this.handleGameStall();
                    if (MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) {
                        QcSelfStudyController.this.log("handleGameStall, set data stall");
                        QcSelfStudyController.this.tryDataStallOptimization(true, defaultDataSlotId, 1, "gameResidentCell");
                        return;
                    }
                    return;
                }
                if ("nr2lte".equalsIgnoreCase(stringExtra)) {
                    QcSelfStudyController.this.log("handleGameStall, request neighbor cell info before optimization");
                    QcSelfStudyController.this.triggerGameResidentNetworkOptimization(defaultDataSlotId);
                    return;
                }
                if ("datastall".equalsIgnoreCase(stringExtra)) {
                    QcSelfStudyController.this.log("debug datastall tryDataStallOptimization data_stall_self_study");
                    QcSelfStudyController.this.mNetworkMetricsController.setDataStallOptimization(1, true, NetworkMetricsController.Scene.GAME_RESIDENT_CELL);
                    return;
                }
                if ("recover".equalsIgnoreCase(stringExtra)) {
                    QcSelfStudyController.this.log("debug recover ");
                    QcSelfStudyController.this.resetGameResidentNetworkOptimization(true);
                    return;
                }
                if ("isLteCellMoreThanNr".equalsIgnoreCase(stringExtra)) {
                    QcSelfStudyController.this.isLteCellMoreThanNr();
                    QcSelfStudyController.this.log("debug queryResidentCellSenceHomeOrCompany");
                    QcSelfStudyController.this.queryResidentCellSenceHomeOrCompany();
                    QcSelfStudyController.this.log("debug isTopGameCell");
                    QcSelfStudyController qcSelfStudyController = QcSelfStudyController.this;
                    qcSelfStudyController.isTopGameCell(qcSelfStudyController.oldGameGci, QcSelfStudyController.this.oldGamePci, QcSelfStudyController.this.oldGameFreq, QcSelfStudyController.this.oldGameRat);
                    return;
                }
                if ("isStaticCell".equalsIgnoreCase(stringExtra)) {
                    QcSelfStudyController.this.isStaticCell = true;
                    QcSelfStudyController.this.mTopCellForStaticGame.add(QcSelfStudyController.this.oldGameGci + "-" + QcSelfStudyController.this.oldGamePci + "-" + QcSelfStudyController.this.oldGameFreq);
                    QcSelfStudyController qcSelfStudyController2 = QcSelfStudyController.this;
                    StringBuilder sb = new StringBuilder("mTopCellForStaticGame: ");
                    sb.append(QcSelfStudyController.this.mTopCellForStaticGame);
                    qcSelfStudyController2.log(sb.toString());
                    ((SelfStudyControllerBase) QcSelfStudyController.this).isGameDebug = true;
                    QcSelfStudyController.this.log("checkStaticState");
                    QcSelfStudyController.this.checkStaticState(true);
                    QcSelfStudyController.this.log("requestNeighborCellInfo");
                    QcSelfStudyController.this.requestNeighborCellInfoCommon();
                    return;
                }
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                boolean z = Settings.Global.getInt(((SelfStudyControllerBase) QcSelfStudyController.this).mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
                if (MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) {
                    new AtomicBoolean(false);
                    if (!QcSelfStudyController.this.mIsAirPlane && z) {
                        QcSelfStudyController.this.mIsAirPlane = true;
                        QcSelfStudyController qcSelfStudyController3 = QcSelfStudyController.this;
                        qcSelfStudyController3.processAirPlaneForGameOpt(qcSelfStudyController3.mIsAirPlane);
                    } else {
                        if (!QcSelfStudyController.this.mIsAirPlane || z) {
                            return;
                        }
                        QcSelfStudyController.this.mIsAirPlane = false;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(byte[] bArr) {
            QcSelfStudyController.this.parseSelfStudyData(bArr);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$1(byte[] bArr) {
            QcSelfStudyController.this.parseWechatData(bArr);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void regUnregOtherReceiver(boolean z) {
        BroadcastReceiver broadcastReceiver;
        if (!z || this.mQcomBroadcastReceiver != null) {
            if (z || (broadcastReceiver = this.mQcomBroadcastReceiver) == null) {
                return;
            }
            this.mContext.unregisterReceiver(broadcastReceiver);
            this.mQcomBroadcastReceiver = null;
            return;
        }
        this.mQcomBroadcastReceiver = new QcomSelfStudyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("qualcomm.intent.action.XIAOMI_SELF_STUDY_IND");
        intentFilter.addAction("qualcomm.intent.action.XIAOMI_WECHAT_IND");
        intentFilter.addAction("com.android.phone.intent.action.STREAMING_APPS_OPTIMIZATION_DEBUG");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("selfStudyController.game.intent.debugall");
        this.mContext.registerReceiver(this.mQcomBroadcastReceiver, intentFilter, 2);
        EventHubManager.getInstance().registerListener("telephony", "ril_ready", new RilHookListener(), this.mWorkHandler);
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void sendBadCellToModem(List list) {
        if (this.mModemUtils == null) {
            log("sendBadCellToModem failed, mModemUtils is null");
            return;
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[(list.size() * 16) + 8]);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        byteBufferWrap.putInt(21);
        byteBufferWrap.putInt(list.size());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            SelfStudyControllerBase.CellUnit cellUnit = (SelfStudyControllerBase.CellUnit) it.next();
            byteBufferWrap.putLong(cellUnit.cellId);
            byteBufferWrap.putInt(cellUnit.cellPci);
            byteBufferWrap.putInt(cellUnit.cellFreq);
        }
        this.mModemUtils.onHookCommonMsgSyncBySub(106, byteBufferWrap.array(), 0);
    }

    public void doRestoreOrUpdateCellRoute_V2(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        Log.d("QcSelfStudyController", ",length is " + i);
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = byteBuffer.getInt();
            Log.d("QcSelfStudyController", " unitLength is " + i3);
            ArrayList arrayList = new ArrayList();
            for (int i4 = 0; i4 < i3; i4++) {
                SelfStudyControllerBase.CellRouteInfo cellRouteInfo = new SelfStudyControllerBase.CellRouteInfo();
                cellRouteInfo.cellId = byteBuffer.getLong();
                cellRouteInfo.cellRat = byteBuffer.getInt();
                cellRouteInfo.cellPci = byteBuffer.getInt();
                cellRouteInfo.cellFreq = byteBuffer.getInt();
                arrayList.add(cellRouteInfo);
            }
            this.allCellRouteList.add(arrayList);
            log("all cell size is " + this.allCellRouteList.size());
        }
    }

    private void setDataStallOptimization(int i) {
        if (!MiuiPhoneUtils.isSnsOptimizationSupported() || this.mNetworkMetricsController == null) {
            log("not support sns");
        } else {
            log("setDataStallOptimization data_stall_self_study");
            this.mNetworkMetricsController.setDataStallOptimization(i, true, NetworkMetricsController.Scene.DATA_STALL_SELFSTUDY);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void tryDataStallOptimization(boolean z, int i, int i2, String str) {
        if (!MiuiPhoneUtils.isSnsOptimizationSupported() || this.mNetworkMetricsController == null) {
            log("not support sns");
            return;
        }
        if (this.mTriggedDataStallTime > 0 && SystemClock.elapsedRealtime() - this.mTriggedDataStallTime >= 120000) {
            this.mTriggedDataStallTime = 0L;
            if (!z) {
                log("resetDataStallOptimization data_stall_self_study");
                this.mNetworkMetricsController.resetDataStallOptimization(i);
            }
        }
        if (z && this.mTriggedDataStallTime == 0) {
            this.mTriggedDataStallTime = SystemClock.elapsedRealtime();
            recordSelfStudyFor3rdOrQms(3);
            log("tryDataStallOptimization data_stall_self_study");
            if ("gameResidentCell".equalsIgnoreCase(str)) {
                this.mNetworkMetricsController.setDataStallOptimization(i2, false, NetworkMetricsController.Scene.GAME_RESIDENT_CELL);
                return;
            } else {
                if (this.dataType == 2) {
                    this.mNetworkMetricsController.setDataStallOptimization(i2, true, NetworkMetricsController.Scene.DATA_STALL_SELFSTUDY);
                    return;
                }
                return;
            }
        }
        log("tryDataStallOptimization not trigger data stall isBadCell = " + z);
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void startCollectMultiRouteAndBeginPredict() {
        if (this.mIsMobileDataOn) {
            if (!this.haveStartedCollectBadCellOnMorningPeak && currentIsMorning() && this.mIsWorkDay) {
                this.haveStartedCollectBadCellOnMorningPeak = true;
                initMultiRouteStudyState();
                log("notifyModemReportBadCellAndBeginPredict on morning peak");
                sendSelfStudyRequest(28, 1, new byte[]{1});
                return;
            }
            if (!this.haveStartedCollectBadCellOnEveningPeak && currentIsEveningPeak() && this.mIsWorkDay) {
                this.haveStartedCollectBadCellOnEveningPeak = true;
                initMultiRouteStudyState();
                log("notifyModemReportBadCellAndBeginPredict on evening peak");
                sendSelfStudyRequest(28, 1, new byte[]{1});
            }
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void stopCollectMultiRouteAndPredict(boolean z) {
        if (z && (this.haveStartedCollectBadCellOnEveningPeak || this.haveStartedCollectBadCellOnMorningPeak)) {
            this.haveStartedCollectBadCellOnMorningPeak = false;
            this.haveStartedCollectBadCellOnEveningPeak = false;
            sendSelfStudyRequest(28, 1, new byte[]{0});
        } else if (this.haveStartedCollectBadCellOnMorningPeak && currentIsNonPeak()) {
            this.haveStartedCollectBadCellOnMorningPeak = false;
            sendSelfStudyRequest(28, 1, new byte[]{0});
        } else if (this.haveStartedCollectBadCellOnEveningPeak && currentIsNonPeak()) {
            this.haveStartedCollectBadCellOnEveningPeak = false;
            sendSelfStudyRequest(28, 1, new byte[]{0});
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void startStudyHomeResidentCells() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (this.mIsStudyingHomeResidentCells || activeDataPhone == null || !this.mFeatureEnable) {
            return;
        }
        if (this.mModemUtils == null) {
            log("startStudyHomeResidentCells failed, mModemUtils is null");
            return;
        }
        this.mIsStudyingHomeResidentCells = true;
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mModemUtils.onHookCommonMsg(175, 8, 200, i);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void startStudyCompanyResidentCells() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (this.mIsStudyingCompanyResidentCells || !this.mIsWorkDay || activeDataPhone == null || !this.mFeatureEnable) {
            return;
        }
        if (this.mModemUtils == null) {
            log("startStudyCompanyResidentCells failed, mModemUtils is null");
            return;
        }
        this.mIsStudyingCompanyResidentCells = true;
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mModemUtils.onHookCommonMsg(175, 8, 100, i);
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void stopStudyResidentCells(int i) {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if ((this.mIsStudyingHomeResidentCells || this.mIsStudyingCompanyResidentCells) && activeDataPhone != null) {
            this.mIsStudyingHomeResidentCells = false;
            this.mIsStudyingCompanyResidentCells = false;
            if (this.mModemUtils == null) {
                log("stopStudyResidentCells failed, mModemUtils is null");
                return;
            }
            SQLiteDatabase writableDatabase = this.dbHelper.getWritableDatabase();
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                this.mModemUtils.onHookCommonMsg(175, 8, 101, i2);
                Phone phone = MiuiPhoneUtils.getPhone(i2);
                if (phone != null && phone.getIccCard() != null && phone.getIccCard().hasIccCard()) {
                    try {
                        getResidentCellsInfoFromModemAndStore(i2, i, writableDatabase);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (i == SelfStudyControllerBase.Scene.Home.value) {
                int i3 = this.mPreferenceUtils.getInt("study_resident_cells_days", 0);
                log("studyResidentCellsDays : " + i3);
                if (i3 < 2) {
                    this.mPreferenceUtils.putInt("study_resident_cells_days", i3 + 1);
                } else {
                    for (int i4 = 0; i4 < MiuiPhoneUtils.PHONE_COUNT; i4++) {
                        Phone phone2 = MiuiPhoneUtils.getPhone(i4);
                        if (phone2 != null) {
                            int subId = phone2.getSubId();
                            SelfStudyDatabaseHelper selfStudyDatabaseHelper = this.dbHelper;
                            SelfStudyControllerBase.Scene scene = SelfStudyControllerBase.Scene.Home;
                            int i5 = scene.value;
                            SelfStudyControllerBase.Rat rat = SelfStudyControllerBase.Rat.Lte;
                            selfStudyDatabaseHelper.statisticsResidentCellsInfo(writableDatabase, i5, rat.value, subId);
                            SelfStudyDatabaseHelper selfStudyDatabaseHelper2 = this.dbHelper;
                            int i6 = scene.value;
                            SelfStudyControllerBase.Rat rat2 = SelfStudyControllerBase.Rat.Nr;
                            selfStudyDatabaseHelper2.statisticsResidentCellsInfo(writableDatabase, i6, rat2.value, subId);
                            SelfStudyDatabaseHelper selfStudyDatabaseHelper3 = this.dbHelper;
                            SelfStudyControllerBase.Scene scene2 = SelfStudyControllerBase.Scene.Company;
                            selfStudyDatabaseHelper3.statisticsResidentCellsInfo(writableDatabase, scene2.value, rat.value, subId);
                            this.dbHelper.statisticsResidentCellsInfo(writableDatabase, scene2.value, rat2.value, subId);
                        }
                    }
                    try {
                        loadResidentCellIds();
                        if (!MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) {
                            setLteNrResidentDubiousCell(writableDatabase);
                        }
                    } catch (Exception e2) {
                        log("loadResidentCellIds and setLteNrResidentDubiousCell error");
                        e2.printStackTrace();
                    }
                }
            }
            writableDatabase.close();
        }
    }

    private void setLteNrResidentDubiousCell(SQLiteDatabase sQLiteDatabase) {
        String strSubstring;
        String strSubstring2;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        if (this.mSignalMapSnsController == null) {
            return;
        }
        Cursor cursorQuery = sQLiteDatabase.query("resident_cells_table", new String[]{"global_cell_id", "pci", "freq", "rat", "plmn", "scene"}, null, null, null, null, null);
        int i = 1;
        if (cursorQuery.moveToFirst()) {
            while (!cursorQuery.isAfterLast()) {
                String string = cursorQuery.getString(0);
                int i2 = cursorQuery.getInt(i);
                int i3 = cursorQuery.getInt(2);
                int i4 = cursorQuery.getInt(3);
                String string2 = cursorQuery.getString(4);
                int i5 = cursorQuery.getInt(5);
                SelfStudyControllerBase.Rat rat = SelfStudyControllerBase.Rat.Lte;
                int i6 = rat.value;
                if (String.valueOf(string2).length() != 5) {
                    strSubstring = "";
                    strSubstring2 = "";
                } else {
                    strSubstring = String.valueOf(string2).substring(0, 3);
                    strSubstring2 = String.valueOf(string2).substring(3, 5);
                }
                String[] strArrSplit = string.split("_");
                int i7 = Integer.parseInt(strArrSplit[4]);
                Cursor cursor = cursorQuery;
                log("query resident cell  band = " + i7 + " rat = " + i4);
                if (i4 == rat.value) {
                    int i8 = Integer.parseInt(strArrSplit[3]);
                    int i9 = SelfStudyControllerBase.Scene.Home.value;
                    SignalMapSnsController signalMapSnsController = this.mSignalMapSnsController;
                    if (i5 == i9) {
                        arrayList.add(signalMapSnsController.constructLteDubiousCellInfo(i8, Short.parseShort(strSubstring), Short.parseShort(strSubstring2), i3, (short) i2, i7, "CEF:RLF"));
                    } else {
                        arrayList3.add(signalMapSnsController.constructLteDubiousCellInfo(i8, Short.parseShort(strSubstring), Short.parseShort(strSubstring2), i3, (short) i2, i7, "CEF:RLF"));
                    }
                } else {
                    String str = strSubstring2;
                    if (i4 == SelfStudyControllerBase.Rat.Nr.value) {
                        long j = Long.parseLong(strArrSplit[3]);
                        int i10 = SelfStudyControllerBase.Scene.Home.value;
                        SignalMapSnsController signalMapSnsController2 = this.mSignalMapSnsController;
                        if (i5 == i10) {
                            arrayList2.add(signalMapSnsController2.constructNrDubiousCellInfo(j, Short.parseShort(strSubstring), Short.parseShort(str), i3, (short) i2, i7, "CEF:RLF"));
                        } else {
                            arrayList4.add(signalMapSnsController2.constructNrDubiousCellInfo(j, Short.parseShort(strSubstring), Short.parseShort(str), i3, (short) i2, i7, "CEF:RLF"));
                        }
                    }
                }
                cursor.moveToNext();
                cursorQuery = cursor;
                i = 1;
            }
        }
        cursorQuery.close();
        if (arrayList.size() > 1) {
            this.mSignalMapSnsController.setOrResetLteDubiousCellIfNeed(arrayList, true, "dataStallSelfStudy");
        }
        if (arrayList2.size() > 1) {
            this.mSignalMapSnsController.setOrReserNrDubiousCellIfNeed(arrayList2, true, "dataStallSelfStudy");
        }
        if (arrayList3.size() > 1) {
            this.mSignalMapSnsController.setOrResetLteDubiousCellIfNeed(arrayList3, true, "dataStallSelfStudy");
        }
        if (arrayList4.size() > 1) {
            this.mSignalMapSnsController.setOrReserNrDubiousCellIfNeed(arrayList4, true, "dataStallSelfStudy");
        }
    }

    private void setLteNrDubiousCell(CellIdentity cellIdentity, boolean z) {
        boolean z2;
        if (cellIdentity != null) {
            try {
                if (this.mSignalMapSnsController == null) {
                    return;
                }
                short s = Short.parseShort(cellIdentity.getMccString());
                short s2 = Short.parseShort(cellIdentity.getMncString());
                if (cellIdentity instanceof CellIdentityNr) {
                    CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
                    long nci = cellIdentityNr.getNci();
                    int[] bands = cellIdentityNr.getBands();
                    if (validateBands(bands, "NR")) {
                        int i = bands[0];
                        int nrarfcn = cellIdentityNr.getNrarfcn();
                        short pci = (short) cellIdentityNr.getPci();
                        log("setLteNrDubiousCell nr cell cgi = " + nci + " band = " + i);
                        mCacheNrDubiousCellInfoList.add(this.mSignalMapSnsController.constructNrDubiousCellInfo(nci, s, s2, nrarfcn, pci, i, "CEF:RLF"));
                        if (z) {
                            z2 = true;
                            this.dbHelper.insertWechatVideoDubiousCells(SelfStudyControllerBase.Rat.Nr.value, nci, s, s2, nrarfcn, pci, i, System.currentTimeMillis());
                            log("insertWechatDubiousCells cgi = " + nci + " mcc: " + ((int) s) + " mnc: " + ((int) s2) + " arfcn: " + nrarfcn + " pci: " + ((int) pci) + " band: " + i);
                        } else {
                            z2 = true;
                        }
                        this.mSignalMapSnsController.setOrReserNrDubiousCellIfNeed(mCacheNrDubiousCellInfoList, z2, "dataStallSelfStudy");
                        return;
                    }
                    return;
                }
                if (cellIdentity instanceof CellIdentityLte) {
                    CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
                    int ci = cellIdentityLte.getCi();
                    int[] bands2 = cellIdentityLte.getBands();
                    if (validateBands(bands2, "LTE")) {
                        int i2 = bands2[0];
                        int earfcn = cellIdentityLte.getEarfcn();
                        short pci2 = (short) cellIdentityLte.getPci();
                        log(" setLteNrDubiousCell lte cell cgi = " + ci + " band = " + i2);
                        mCacheLteDubiousCellInfoList.add(this.mSignalMapSnsController.constructLteDubiousCellInfo(ci, s, s2, earfcn, pci2, i2, "CEF:RLF"));
                        if (z) {
                            this.dbHelper.insertWechatVideoDubiousCells(SelfStudyControllerBase.Rat.Lte.value, ci, s, s2, earfcn, pci2, i2, System.currentTimeMillis());
                            log("insertWechatDubiousCells cgi = " + ci + " mcc: " + ((int) s) + " mnc: " + ((int) s2) + " arfcn: " + earfcn + " pci: " + ((int) pci2) + " band: " + i2);
                        }
                        this.mSignalMapSnsController.setOrResetLteDubiousCellIfNeed(mCacheLteDubiousCellInfoList, true, "dataStallSelfStudy");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateBands(int[] iArr, String str) {
        if (iArr != null && iArr.length != 0) {
            return true;
        }
        log(str + " bands is null or empty");
        return false;
    }

    private void getResidentCellsInfoFromModemAndStore(int i, int i2, SQLiteDatabase sQLiteDatabase) {
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            log("getResidentCellsInfoFromModemAndStore failed, mModemUtils is null");
            return;
        }
        ByteBuffer lteStaticFeatureCellInfo = modemUtils.getLteStaticFeatureCellInfo(i);
        ByteBuffer nr5gStaticFeatureCellInfo = this.mModemUtils.getNr5gStaticFeatureCellInfo(i);
        ArrayList arrayList = new ArrayList();
        boolean zAnalyzeAndStoreResidentCellsInfo = lteStaticFeatureCellInfo != null ? analyzeAndStoreResidentCellsInfo(lteStaticFeatureCellInfo, SelfStudyControllerBase.Rat.Lte.value, sQLiteDatabase, arrayList) : true;
        boolean zAnalyzeAndStoreResidentCellsInfo2 = nr5gStaticFeatureCellInfo != null ? analyzeAndStoreResidentCellsInfo(nr5gStaticFeatureCellInfo, SelfStudyControllerBase.Rat.Nr.value, sQLiteDatabase, arrayList) : true;
        if (zAnalyzeAndStoreResidentCellsInfo && zAnalyzeAndStoreResidentCellsInfo2 && DefaultSimManager.getDefaultDataSlotId() == i) {
            for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                this.dbHelper.deleteOldResidentCells(sQLiteDatabase, arrayList, i2, MiuiPhoneUtils.getPhone(i3).getSubId());
            }
            this.mPreferenceUtils.putInt("study_resident_cells_days", 0);
        }
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0134  */
    private boolean analyzeAndStoreResidentCellsInfo(ByteBuffer byteBuffer, int i, SQLiteDatabase sQLiteDatabase, List list) {
        if (byteBuffer == null) {
            return false;
        }
        log("version = " + ((int) byteBuffer.get()) + " count = " + ((int) byteBuffer.getShort()));
        boolean z = true;
        for (int i2 = 0; i2 < 4; i2++) {
            StringBuilder sb = new StringBuilder();
            int i3 = byteBuffer.getInt();
            Phone phone = MiuiPhoneUtils.getPhone(i3);
            int subId = phone == null ? -1 : phone.getSubId();
            byte b = byteBuffer.get();
            short s = byteBuffer.getShort();
            short s2 = byteBuffer.getShort();
            int i4 = byteBuffer.getInt();
            StringBuilder sb2 = new StringBuilder();
            if (s2 < 10) {
                sb2.append('0');
            }
            sb2.append((int) s2);
            sb.append((int) b);
            sb.append("_");
            sb.append(subId);
            sb.append("_");
            sb.append((int) s);
            sb.append((CharSequence) sb2);
            sb.append("_");
            long j = byteBuffer.getLong();
            sb.append(j);
            sb.append("_");
            sb.append(i4);
            short s3 = byteBuffer.getShort();
            int i5 = byteBuffer.getInt();
            int i6 = byteBuffer.getInt();
            short s4 = byteBuffer.getShort();
            short s5 = byteBuffer.getShort();
            boolean z2 = z;
            log("insert : plmn = " + ((int) s) + " " + ((int) s2) + " cellId = " + j + " pci = " + ((int) s3) + " freq = " + i5 + " rat = " + i + " scene = " + ((int) b) + " storeTime = " + i6 + "phoneId = " + i3);
            list.add(sb.toString());
            if (i5 == 0 || i6 < 30) {
                z = z2;
            } else {
                if (this.dbHelper.insertOrUpdateResidentCells(sQLiteDatabase, ((int) s) + sb2.toString(), sb.toString(), s3, i5, i, b, subId, i6, s4, s5, 0, 0, 0, 0)) {
                    z = false;
                } else {
                    z = z2;
                }
            }
        }
        return z;
    }

    /* JADX WARN: Code duplicated, block: B:43:0x00d5  */
    /* JADX WARN: Code duplicated, block: B:71:0x016c  */
    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void processCellLocationChanged(CellLocation cellLocation, int i) {
        int i2;
        int i3;
        long j;
        int pci;
        int nrarfcn;
        long j2;
        long j3;
        int i4;
        long j4;
        long j5;
        if (!MiuiPhoneUtils.isDefaultDataSlotId(i) || cellLocation == null || cellLocation.isEmpty() || !MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) {
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
                log("game cell type not supported");
                i2 = 0;
                i3 = 0;
                j = 0;
            }
            this.curGameRat = j;
            Log.d("QcSelfStudyController", "processCellLocationChanged new pci = " + i2 + " new freq =  " + i3 + " new rat = " + j + " gci = " + TelephonyUtils.pii(globalCellId) + " mIsMobileDataOn = " + this.mIsMobileDataOn);
            if (!shouldDoActionForGameQcOpt() && isHasDelayApiGame() && this.isEnterGame && this.mIsMobileDataOn && ((SelfStudyControllerBase.mEnterGameOptimizer || this.isStaticCell) && (i2 != this.oldGamePci || i3 != this.oldGameFreq || !this.oldGameGci.equals(globalCellId)))) {
                long j6 = this.mlastGameCellTime;
                if (j6 == 0 || this.oldGameRat == 0) {
                    j4 = 0;
                    j5 = j;
                } else if (this.mEnterGameBattleState) {
                    j4 = 0;
                    j5 = j;
                    addOrUpdateGameStallDur(this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, j6), this.oldGameGci, jCurrentTimeMillis);
                } else {
                    j4 = 0;
                    j5 = j;
                    long j7 = this.endGameTime;
                    if (j7 != 0) {
                        if (j6 >= j7) {
                            log("lastGameCellTime > endGameTime no need save");
                        } else {
                            addOrUpdateGameStallDur(this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j7, j6), this.oldGameGci, jCurrentTimeMillis);
                        }
                    }
                }
                if (this.oldGameRat == j4 || !this.isGameLag) {
                    i4 = i2;
                } else {
                    long j8 = this.startGameLagTime;
                    if (j8 != j4) {
                        i4 = i2;
                        addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(jCurrentTimeMillis, j8) * ((long) getLastGameDelay(this.mlastGameDelay)), jCurrentTimeMillis, this.mCurrentGameCellStats, "AP");
                        this.startGameLagTime = jCurrentTimeMillis;
                    } else {
                        i4 = i2;
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append(globalCellId);
                sb.append("-");
                sb.append(i4);
                sb.append("-");
                sb.append(i3);
                sb.append("-");
                j3 = j5;
                sb.append(j3);
                String string = sb.toString();
                this.mWorkHandler.removeMessages(37);
                if (this.isEnterGame && this.oldGameRat != j4 && this.mEnterGameBattleState) {
                    Handler handler = this.mWorkHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(37, string), 20000L);
                }
            } else {
                j3 = j;
                i4 = i2;
            }
            this.oldGameRat = j3;
            this.oldGameFreq = i3;
            this.oldGamePci = i4;
            this.oldGameGci = globalCellId;
            this.mlastGameCellTime = jCurrentTimeMillis;
        }
        i3 = nrarfcn;
        i2 = pci;
        j = j2;
        this.curGameRat = j;
        Log.d("QcSelfStudyController", "processCellLocationChanged new pci = " + i2 + " new freq =  " + i3 + " new rat = " + j + " gci = " + TelephonyUtils.pii(globalCellId) + " mIsMobileDataOn = " + this.mIsMobileDataOn);
        if (!shouldDoActionForGameQcOpt()) {
            j3 = j;
            i4 = i2;
        } else {
            j3 = j;
            i4 = i2;
        }
        this.oldGameRat = j3;
        this.oldGameFreq = i3;
        this.oldGamePci = i4;
        this.oldGameGci = globalCellId;
        this.mlastGameCellTime = jCurrentTimeMillis;
    }

    private void processServiceStateChange(int i, ServiceState serviceState) {
        SignalStrength signalStrength;
        CellSignalStrengthNr nrSignalStrength;
        if (i == MiuiPhoneUtils.getActiveDataPhoneId() && MiuiPhoneUtils.isValidatePhoneId(i)) {
            int dataNetworkType = serviceState == null ? 0 : serviceState.getDataNetworkType();
            ServiceState serviceState2 = this.mServiceState[i];
            int dataNetworkType2 = serviceState2 != null ? serviceState2.getDataNetworkType() : 0;
            if (isNrNwType(dataNetworkType2)) {
                isLteNwType(dataNetworkType);
            }
            boolean zIsDataRegistered = MiuiPhoneUtils.isDataRegistered(i);
            if (isLteNwType(dataNetworkType) && zIsDataRegistered) {
                this.curGameRat = 1L;
            } else if (isNrNwType(dataNetworkType) && zIsDataRegistered) {
                this.curGameRat = 2L;
            } else {
                this.curGameRat = 0L;
            }
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null && (signalStrength = phone.getSignalStrength()) != null) {
                long j = this.curGameRat;
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
            Log.d("QcSelfStudyController", "processServiceStateChangeForGame: oldRat = " + dataNetworkType2 + " newRat = " + dataNetworkType + " curGameRat = " + this.curGameRat + " oldGameRsrp = " + this.oldGameRsrp + " oldGameSnr = " + this.oldGameSnr + " oldGameRsrq = " + this.oldGameRsrq + " isEnterGame = " + this.isEnterGame);
            if (serviceState != null) {
                this.mServiceState[i] = new ServiceState(serviceState);
            }
        }
    }

    private boolean isHasDelayApiGame() {
        return SelfStudyControllerBase.STATIC_GAME_CELL_WHITELIST_PACKAGES.contains(this.mLastTopGamePackage);
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
        arrayList.removeIf(new Predicate() { // from class: com.android.phone.QcSelfStudyController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return QcSelfStudyController.$r8$lambda$F9dCAppO72iPRoRRb56vkSJCgKE(j, (Map.Entry) obj);
            }
        });
        arrayList2.removeIf(new Predicate() { // from class: com.android.phone.QcSelfStudyController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return QcSelfStudyController.m1930$r8$lambda$LfyHdl37lL23okZQVL1aZH9dA(j, (Map.Entry) obj);
            }
        });
        log("saveGameCell rat1Entries = " + arrayList + " rat2Entries = " + arrayList2);
        Collections.sort(arrayList, new Comparator(this) { // from class: com.android.phone.QcSelfStudyController.1
            @Override // java.util.Comparator
            public int compare(Map.Entry entry2, Map.Entry entry3) {
                return ((Long) ((ConcurrentHashMap) entry3.getValue()).get("saveTime")).compareTo((Long) ((ConcurrentHashMap) entry2.getValue()).get("saveTime"));
            }
        });
        Collections.sort(arrayList2, new Comparator(this) { // from class: com.android.phone.QcSelfStudyController.2
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

    public static /* synthetic */ boolean $r8$lambda$F9dCAppO72iPRoRRb56vkSJCgKE(long j, Map.Entry entry) {
        return j - ((Long) ((ConcurrentHashMap) entry.getValue()).get("saveTime")).longValue() > 1200000;
    }

    /* JADX INFO: renamed from: $r8$lambda$LfyHdl37lL23okZQVL--1aZH9dA, reason: not valid java name */
    public static /* synthetic */ boolean m1930$r8$lambda$LfyHdl37lL23okZQVL1aZH9dA(long j, Map.Entry entry) {
        return j - ((Long) ((ConcurrentHashMap) entry.getValue()).get("saveTime")).longValue() > 1200000;
    }

    private void updateHistoryGameData(ConcurrentHashMap concurrentHashMap) {
        QcSelfStudyController qcSelfStudyController = this;
        qcSelfStudyController.log("updateHistoryGameData map  " + concurrentHashMap + " size = " + concurrentHashMap.size());
        if (concurrentHashMap.size() > 0) {
            for (Map.Entry entry : concurrentHashMap.entrySet()) {
                String[] strArrSplit = ((String) entry.getKey()).split("-");
                if (strArrSplit.length == 3) {
                    String str = strArrSplit[0];
                    int stringToInt = qcSelfStudyController.parseStringToInt(strArrSplit[1]);
                    int stringToInt2 = qcSelfStudyController.parseStringToInt(strArrSplit[2]);
                    ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) entry.getValue();
                    if (concurrentHashMap2 != null) {
                        long jLongValue = ((Long) concurrentHashMap2.get("saveTime")).longValue();
                        long jLongValue2 = ((Long) concurrentHashMap2.get("dur")).longValue();
                        long jLongValue3 = ((Long) concurrentHashMap2.get("lagdur")).longValue();
                        long jLongValue4 = ((Long) concurrentHashMap2.get("lagdur_modem")).longValue();
                        long jLongValue5 = ((Long) concurrentHashMap2.get("lagdur_ai")).longValue();
                        long jLongValue6 = ((Long) concurrentHashMap2.get("rat")).longValue();
                        long jLongValue7 = ((Long) concurrentHashMap2.get("count")).longValue();
                        if (jLongValue2 > 0) {
                            Log.d("QcSelfStudyController", "updateHistoryGameData gci = " + TelephonyUtils.pii(str) + " pci= " + stringToInt + " freq = " + stringToInt2 + " count = " + jLongValue7 + " rat = " + jLongValue6 + " dur = " + jLongValue2 + " lagdur = " + jLongValue3 + " lagdur_modem = " + jLongValue4 + " lagdur_ai = " + jLongValue5 + " scene = " + this.curGameScene);
                            insertOrUpdateGameDatabase(jLongValue6, stringToInt2, stringToInt, jLongValue7, jLongValue2, jLongValue3, jLongValue, str, "AP");
                            insertOrUpdateGameDatabase(jLongValue6, stringToInt2, stringToInt, jLongValue7, jLongValue2, jLongValue4, jLongValue, str, "MODEM");
                            insertOrUpdateGameDatabase(jLongValue6, stringToInt2, stringToInt, jLongValue7, jLongValue2, jLongValue5, jLongValue, str, "AI");
                        }
                    }
                }
                qcSelfStudyController = this;
            }
        }
        concurrentHashMap.clear();
    }

    /* JADX WARN: Code duplicated, block: B:24:0x015a A[Catch: all -> 0x0121, TryCatch #0 {all -> 0x0121, blocks: (B:12:0x004b, B:14:0x006e, B:22:0x0154, B:24:0x015a, B:25:0x016e, B:27:0x0174, B:18:0x0125, B:20:0x014b, B:21:0x014f), top: B:39:0x004b, outer: #2 }] */
    /* JADX WARN: Code duplicated, block: B:27:0x0174 A[Catch: all -> 0x0121, TRY_LEAVE, TryCatch #0 {all -> 0x0121, blocks: (B:12:0x004b, B:14:0x006e, B:22:0x0154, B:24:0x015a, B:25:0x016e, B:27:0x0174, B:18:0x0125, B:20:0x014b, B:21:0x014f), top: B:39:0x004b, outer: #2 }] */
    private void insertOrUpdateGameDatabase(long j, int i, int i2, long j2, long j3, long j4, long j5, String str, String str2) {
        ArrayList arrayList;
        if (!shouldDoActionForGameQcOpt()) {
            log("shouldDoActionForGameQcOpt return");
            return;
        }
        if (!SelfStudyControllerBase.mEnterGameOptimizer) {
            log("not enterGameOptimizer return");
            return;
        }
        int i3 = this.curGameScene;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        try {
            Cursor gameBadCellByPciAndFreq = this.dbHelper.getGameBadCellByPciAndFreq(j, i, i2, str, i3, str2);
            try {
                log("getGameBadCellByPciAndFreq count = " + gameBadCellByPciAndFreq.getCount() + " table = " + str2);
                if (gameBadCellByPciAndFreq.moveToFirst()) {
                    int i4 = gameBadCellByPciAndFreq.getInt(0);
                    float f = gameBadCellByPciAndFreq.getFloat(1);
                    float f2 = gameBadCellByPciAndFreq.getFloat(2);
                    float f3 = gameBadCellByPciAndFreq.getFloat(3);
                    log("getGameBadCellByPciAndFreq oldCnt = " + f + " olDur = " + f2 + " oldLagDur = " + f3 + " lagdur = " + j4 + " dur = " + j3);
                    float f4 = (float) j2;
                    float f5 = (float) j3;
                    float f6 = f4 / f5;
                    float f7 = (float) j4;
                    float f8 = f7 / f5;
                    this.dbHelper.updateGameBadCell(f4, f5, f7, j5, f6, f8, i4, str2);
                    Log.d("QcSelfStudyController", "insertOrUpdateGameBadCell pci= " + TelephonyUtils.pii(str) + " freq = " + i + " averageCnt = " + f6 + "scene = " + i3 + " averageLagTime = " + f8 + "oldCnt = " + f + " olDur = " + f2 + " oldLagDur = " + f3);
                } else {
                    CellInfo cellInfo = new CellInfo();
                    cellInfo.freq = i;
                    cellInfo.pci = i2;
                    cellInfo.gameDataStallDur = j3;
                    cellInfo.gameDataStallLagDur = j4;
                    cellInfo.gameDataStallCount = j2;
                    cellInfo.updateTimeMills = j5;
                    cellInfo.globalCellId = str;
                    if (j == 1) {
                        arrayList2.add(cellInfo);
                    } else {
                        arrayList = arrayList3;
                        arrayList.add(cellInfo);
                    }
                    if (!arrayList.isEmpty()) {
                        this.dbHelper.insertGameBadCell(arrayList, 2, i3, this.maxSavedGameCells, str2);
                    }
                    if (!arrayList2.isEmpty()) {
                        this.dbHelper.insertGameBadCell(arrayList2, 1, i3, this.maxSavedGameCells, str2);
                    }
                    gameBadCellByPciAndFreq.close();
                }
                arrayList = arrayList3;
                if (!arrayList.isEmpty()) {
                    this.dbHelper.insertGameBadCell(arrayList, 2, i3, this.maxSavedGameCells, str2);
                }
                if (!arrayList2.isEmpty()) {
                    this.dbHelper.insertGameBadCell(arrayList2, 1, i3, this.maxSavedGameCells, str2);
                }
                gameBadCellByPciAndFreq.close();
            } catch (Throwable th) {
                if (gameBadCellByPciAndFreq == null) {
                    throw th;
                }
                try {
                    gameBadCellByPciAndFreq.close();
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

    private long getGameLagOrDur(long j, long j2) {
        long j3 = j - j2;
        long j4 = this.startGameTime;
        return j3 > j - j4 ? j - j4 : j3;
    }

    private void sameGameCell(long j) {
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
                    addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, j3) * ((long) getLastGameDelay(this.mlastGameDelay)), j, this.mCurrentGameCellStats, "AP");
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
            Log.d("QcSelfStudyController", "addOrUpdateGameStallDur freq = " + i + " pci = " + i2 + " dur = " + j2 + " gci = " + TelephonyUtils.pii(str) + " gpf = " + str2 + " stats = " + concurrentHashMap + " curtime = " + j3);
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
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.put("count", 0L);
            concurrentHashMap2.put("dur", Long.valueOf(j2));
            concurrentHashMap2.put("rat", Long.valueOf(j));
            concurrentHashMap2.put("saveTime", Long.valueOf(j3));
            concurrentHashMap2.put("lagdur", 0L);
            concurrentHashMap2.put("lagdur_modem", 0L);
            concurrentHashMap2.put("lagdur_ai", 0L);
            this.mCurrentGameCellStats.put(str2, concurrentHashMap2);
        }
    }

    private void addOrUpdateGameLagDur(String str, int i, int i2, long j, long j2, long j3, ConcurrentHashMap concurrentHashMap, String str2) {
        long jLongValue;
        if (j == 1 || j == 2) {
            String str3 = str + "-" + i2 + "-" + i;
            ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) concurrentHashMap.get(str3);
            Log.d("QcSelfStudyController", "updateGameLagDur freq = " + i + " pci = " + i2 + " gci = " + TelephonyUtils.pii(str) + " stats = " + concurrentHashMap2 + " rat = " + j + " lagdur = " + j2 + " table = " + str2);
            if (concurrentHashMap2 == null) {
                concurrentHashMap2 = new ConcurrentHashMap();
                if ("AP".equalsIgnoreCase(str2)) {
                    concurrentHashMap2.put("lagdur", Long.valueOf(j2));
                    concurrentHashMap2.put("lagdur_modem", 0L);
                    concurrentHashMap2.put("lagdur_ai", 0L);
                } else if ("MODEM".equals(str2)) {
                    concurrentHashMap2.put("lagdur_modem", Long.valueOf(j2));
                    concurrentHashMap2.put("lagdur", 0L);
                    concurrentHashMap2.put("lagdur_ai", 0L);
                } else {
                    concurrentHashMap2.put("lagdur_ai", Long.valueOf(j2));
                    concurrentHashMap2.put("lagdur", 0L);
                    concurrentHashMap2.put("lagdur_modem", 0L);
                }
                concurrentHashMap2.put("dur", 0L);
                concurrentHashMap2.put("count", 1L);
                concurrentHashMap2.put("rat", Long.valueOf(j));
                concurrentHashMap2.put("saveTime", Long.valueOf(j3));
            } else if (((Long) concurrentHashMap2.get("rat")).longValue() == j) {
                if ("AP".equalsIgnoreCase(str2)) {
                    jLongValue = ((Long) concurrentHashMap2.get("lagdur")).longValue();
                    concurrentHashMap2.put("lagdur", Long.valueOf(jLongValue + j2));
                } else if ("MODEM".equalsIgnoreCase(str2)) {
                    jLongValue = ((Long) concurrentHashMap2.get("lagdur_modem")).longValue();
                    concurrentHashMap2.put("lagdur_modem", Long.valueOf(jLongValue + j2));
                } else {
                    long jLongValue2 = ((Long) concurrentHashMap2.get("lagdur_ai")).longValue();
                    concurrentHashMap2.put("lagdur_ai", Long.valueOf(jLongValue2 + j2));
                    jLongValue = jLongValue2;
                }
                Long l = (Long) concurrentHashMap2.get("count");
                long jLongValue3 = l.longValue() + 1;
                concurrentHashMap2.put("count", l);
                concurrentHashMap2.put("saveTime", Long.valueOf(j3));
                log("updateGameLagDur  oldLagDur = " + jLongValue + " oldCount = " + jLongValue3 + " table: " + str2);
            }
            concurrentHashMap.put(str3, concurrentHashMap2);
        }
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
        Log.d("QcSelfStudyController", "getCurrentCellInfoForGame old pci = " + this.oldGamePci + " old freq = " + this.oldGameFreq + " old rat = " + this.oldGameRat);
        String str = this.oldGameGci + "-" + Integer.toString(this.oldGamePci) + "-" + Integer.toString(this.oldGameFreq);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("count", 0L);
        concurrentHashMap.put("dur", 0L);
        concurrentHashMap.put("lagdur", 0L);
        concurrentHashMap.put("lagdur_modem", 0L);
        concurrentHashMap.put("lagdur_ai", 0L);
        concurrentHashMap.put("rat", Long.valueOf(this.oldGameRat));
        concurrentHashMap.put("saveTime", Long.valueOf(System.currentTimeMillis()));
        this.mlastGameCellTime = System.currentTimeMillis();
        ConcurrentHashMap concurrentHashMap2 = this.mCurrentGameCellStats;
        if (concurrentHashMap2 != null) {
            concurrentHashMap2.clear();
        }
        this.mCurrentGameCellStats.put(str, concurrentHashMap);
    }

    private void updateDurOrLagDur(long j) {
        if (this.oldGameRat != 0 && this.mEnterGameBattleState) {
            addOrUpdateGameStallDur(this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, this.mlastGameCellTime), this.oldGameGci, j);
            this.mlastGameCellTime = j;
        }
        if (this.oldGameRat != 0 && this.isGameLag && this.mEnterGameBattleState) {
            long j2 = this.startGameLagTime;
            if (j2 != 0) {
                addOrUpdateGameLagDur(this.oldGameGci, this.oldGameFreq, this.oldGamePci, this.oldGameRat, getGameLagOrDur(j, j2) * ((long) getLastGameDelay(this.mlastGameDelay)), j, this.mCurrentGameCellStats, "AP");
                this.startGameLagTime = j;
            }
        }
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
                                                            Log.d("QcSelfStudyController", "queryResidentCellSenceHomeOrCompany gciFromDb= " + TelephonyUtils.pii(str2) + " gci = " + TelephonyUtils.pii(str3) + " mccmnc = " + strTrim2 + " oldGameGci = " + this.oldGameGci + " scene = " + i4);
                                                            i3 = i4;
                                                        }
                                                    } catch (NumberFormatException e) {
                                                        e = e;
                                                        Log.e("QcSelfStudyController", "Invalid hex format: " + strSubstring, e);
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
        Log.e("QcSelfStudyController", "queryResidentCellSenceHomeOrCompany error", e);
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

    private int getGameDelayLevel(int i) {
        int[] iArr;
        String str = this.mTopGamePackage;
        if (str == null) {
            return 0;
        }
        if (SelfStudyControllerBase.COMMON_GAME_DELAY_THRESHOLDS_PACKAGES.contains(str)) {
            iArr = SelfStudyControllerBase.sCommonGameDelayThresholds;
        } else if (this.mTopGamePackage.equals("com.tencent.lolm")) {
            iArr = SelfStudyControllerBase.sLolmGameDelayThresholds;
        } else if (this.mTopGamePackage.equals("com.tencent.jkchess")) {
            iArr = SelfStudyControllerBase.sJkchessGameDelayThresholds;
        } else {
            iArr = this.mTopGamePackage.equals("com.tencent.tmgp.dfm") ? SelfStudyControllerBase.sDfmGameDelayThresholds : null;
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

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void handleGameStall() {
        log("handleGameStall");
        removeGameDelayCheckMessages();
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if ("46045".equals(MiuiPhoneUtils.getNetworkNumeric(defaultDataSlotId))) {
            return;
        }
        if (this.mSetGameOptNr2Lte) {
            resetGameResidentNetworkOptimization(true);
            return;
        }
        if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) && isHasDelayApiGame() && shouldDoActionForGameQcOpt()) {
            log("handleGameStall, isValidatePhoneId mIsSupportSnsOptimization: " + this.mIsSupportSnsOptimization);
            if (MiuiPhoneUtils.isSupportGameResidentCellOptForOS4()) {
                handleGameStallForOS4(defaultDataSlotId);
            } else {
                handleGameStallLegacy(defaultDataSlotId);
            }
        }
    }

    private void handleGameStallForOS4(int i) {
        if (MiuiPhoneUtils.is5GSaRegistered(i) && this.mNeighborCellManager.shouldDegrade() && isWeakGameSignal() && (!isLteCellMoreThanNr() || this.isStaticCell)) {
            log("handleGameStall, request neighbor cell info before optimization");
            triggerGameResidentNetworkOptimization(i);
        } else if (this.mIsSupportSnsOptimization && isGameRsrpBetterThan(-118) && this.mNeighborCellManager.shouldTriggerDataStall()) {
            log("handleGameStall, set data stall");
            tryDataStallOptimization(true, i, 1, "gameResidentCell");
        }
    }

    private boolean isWeakGameSignal() {
        if (!isValidSignalValue(this.oldGameRsrp) || this.oldGameRsrp > -118) {
            return isValidSignalValue(this.oldGameRsrq) && this.oldGameRsrq < -13;
        }
        return true;
    }

    private boolean isGameRsrpBetterThan(int i) {
        return isValidSignalValue(this.oldGameRsrp) && this.oldGameRsrp > i;
    }

    private void handleGameStallLegacy(int i) {
        if (MiuiTelephony.IS_QCOM && !this.mIsSupportSnsOptimization) {
            if (MiuiPhoneUtils.is5GSaRegistered(i)) {
                if ((this.oldGameRsrp <= -118 || this.oldGameRsrq < -13) && !isLteCellMoreThanNr()) {
                    log("handleGameStall, request neighbor cell info before optimization");
                    triggerGameResidentNetworkOptimization(i);
                    return;
                }
                return;
            }
            return;
        }
        log("handleGameStall, set data stall");
        int i2 = this.oldGameRsrp;
        if (i2 <= -118 || i2 >= -110) {
            return;
        }
        tryDataStallOptimization(true, i, 1, "gameResidentCell");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void triggerGameResidentNetworkOptimization(int i) {
        log("triggerGameResidentNetworkOptimization phoneId: " + i);
        boolean[] saPowerEnable = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSaPowerEnable();
        if (!shouldDoActionForGameQcOpt() || this.mSetGameOptNr2Lte || !this.isEnterGame || !saPowerEnable[i]) {
            log("ignore optimization shouldDoActionForGameQcOpt: " + shouldDoActionForGameQcOpt() + " mSetGameOptNr2Lte: " + this.mSetGameOptNr2Lte + "isEnterGame: " + this.isEnterGame);
            return;
        }
        if (isNetworkOptimizationForSa()) {
            log("NetworkOptimization had disable sa ignore");
            return;
        }
        log("triggerGameResidentNetworkOptimization mRecoveryTime: " + this.mRecoveryTime);
        if (System.currentTimeMillis() - this.mRecoveryTime < 600000) {
            log("less ten minutes, ignore optimization");
            return;
        }
        log("GAME_RESIDENT_CELL_OPTIMIZATION");
        ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).sendMessage(60, Boolean.FALSE);
        this.mSetGameOptNr2Lte = true;
        removeDelayCheckMessages();
        this.mDisableSaTime = System.currentTimeMillis();
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1, 5), MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetGameResidentNetworkOptimization(boolean z) {
        log("resetGameResidentNetworkOptimization isNeedRecovery: " + z);
        if (z) {
            log("reset GAME_RESIDENT_CELL_OPTIMIZATION");
            ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).sendMessage(60, Boolean.TRUE);
            this.mSetGameOptNr2Lte = false;
            this.mRecoveryTime = System.currentTimeMillis();
            removeDelayCheckMessages();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLteCellMoreThanNr() {
        long jLongValue;
        long jLongValue2;
        long jLongValue3;
        long j;
        long jLongValue4;
        long j2;
        long jLongValue5;
        long j3 = 0;
        long jQueryLatestGameCellTime = queryLatestGameCellTime("game_bad_cell_table");
        ConcurrentHashMap concurrentHashMap = this.mCurrentGameCellStats;
        if (concurrentHashMap == null || concurrentHashMap.size() == 0) {
            log("isLteCellMoreThanNr: mCurrentGameCellStats == null");
            String str = "data_stall_duration";
            Cursor cursorQuery = this.dbHelper.query("game_bad_cell_table", new String[]{"rat", "data_stall_lag_duration", "data_stall_duration", "update_time_mills"}, "update_time_mills >= ?", new String[]{String.valueOf(jQueryLatestGameCellTime - 1200000)}, null, null, null);
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            ConcurrentHashMap concurrentHashMap3 = new ConcurrentHashMap();
            concurrentHashMap2.put(1L, 0L);
            concurrentHashMap2.put(2L, 0L);
            concurrentHashMap3.put(1L, 0L);
            concurrentHashMap3.put(2L, 0L);
            if (cursorQuery != null) {
                while (cursorQuery.moveToNext()) {
                    try {
                        try {
                            long j4 = cursorQuery.getLong(cursorQuery.getColumnIndex("rat"));
                            long j5 = cursorQuery.getLong(cursorQuery.getColumnIndex("data_stall_lag_duration"));
                            long j6 = cursorQuery.getLong(cursorQuery.getColumnIndex(str));
                            Long l = (Long) concurrentHashMap2.get(Long.valueOf(j4));
                            String str2 = str;
                            Long l2 = (Long) concurrentHashMap3.get(Long.valueOf(j4));
                            if (l != null && l2 != null) {
                                concurrentHashMap2.put(Long.valueOf(j4), Long.valueOf(l.longValue() + j6));
                                concurrentHashMap3.put(Long.valueOf(j4), Long.valueOf(l2.longValue() + j5));
                            }
                            str = str2;
                        } catch (Exception e) {
                            e = e;
                            jLongValue = 0;
                            jLongValue2 = 0;
                            jLongValue3 = 0;
                        }
                    } catch (Throwable th) {
                        cursorQuery.close();
                        throw th;
                    }
                }
                jLongValue3 = ((Long) concurrentHashMap2.get(1L)).longValue();
                try {
                    jLongValue2 = ((Long) concurrentHashMap2.get(2L)).longValue();
                    try {
                        jLongValue = ((Long) concurrentHashMap3.get(1L)).longValue();
                        try {
                            long jLongValue6 = ((Long) concurrentHashMap3.get(2L)).longValue();
                            cursorQuery.close();
                            j3 = jLongValue;
                            j = jLongValue6;
                        } catch (Exception e2) {
                            e = e2;
                            e.printStackTrace();
                            cursorQuery.close();
                            j3 = jLongValue;
                            j = 0;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        jLongValue = 0;
                    }
                } catch (Exception e4) {
                    e = e4;
                    jLongValue = 0;
                    jLongValue2 = 0;
                }
                jLongValue4 = jLongValue3;
                j2 = jLongValue2;
                jLongValue5 = j;
            } else {
                j3 = 0;
                jLongValue2 = 0;
                jLongValue3 = 0;
            }
            j = 0;
            jLongValue4 = jLongValue3;
            j2 = jLongValue2;
            jLongValue5 = j;
        } else {
            log("isLteCellMoreThanNr: mCurrentGameCellStats != null");
            jLongValue5 = 0;
            jLongValue4 = 0;
            long jLongValue7 = 0;
            for (ConcurrentHashMap concurrentHashMap4 : this.mCurrentGameCellStats.values()) {
                Long l3 = (Long) concurrentHashMap4.get("rat");
                long j7 = j3;
                Long l4 = (Long) concurrentHashMap4.get("saveTime");
                Long l5 = (Long) concurrentHashMap4.get("lagdur");
                Long l6 = (Long) concurrentHashMap4.get("dur");
                if (l4 != null && l3 != null && jQueryLatestGameCellTime - l4.longValue() < 1200000) {
                    if (l3.equals(1L)) {
                        long jLongValue8 = j7 + l5.longValue();
                        jLongValue4 += l6.longValue();
                        j3 = jLongValue8;
                    } else if (l3.equals(2L)) {
                        jLongValue5 += l5.longValue();
                        jLongValue7 += l6.longValue();
                    }
                }
                j3 = j7;
            }
            j2 = jLongValue7;
        }
        log("isLteCellMoreThanNr: sumDurForLte = " + jLongValue4 + " sumDurForNR = " + j2 + " sumLagDurForLte = " + j3 + " sumLagDurForNR = " + jLongValue5);
        if (jLongValue4 < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS || j2 < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
            log("isLteCellMoreThanNr false");
            return false;
        }
        double d = j3 / jLongValue4;
        double d2 = jLongValue5 / j2;
        if (d <= d2 || d <= d2 * 1.2d) {
            return false;
        }
        log("isLteCellMoreThanNr true");
        return true;
    }

    private void removeDelayCheckMessages() {
        this.mWorkHandler.removeMessages(1, 2);
        this.mWorkHandler.removeMessages(1, 3);
        this.mWorkHandler.removeMessages(1, 4);
        this.mWorkHandler.removeMessages(1, 5);
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void resetResidentGameCellOptimization(boolean z) {
        if (MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) {
            if ((isEnterGameOptimizer() || this.isStaticCell) && z) {
                resetGameResidentNetworkOptimization(this.mSetGameOptNr2Lte);
                resetDataStallOptimization();
                SelfStudyControllerBase.mEnterGameOptimizer = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAirPlaneForGameOpt(boolean z) {
        if (MiuiPhoneUtils.isSupportQcGameResidentCellOptimization() && this.isEnterGame) {
            if ((SelfStudyControllerBase.mEnterGameOptimizer || this.isStaticCell) && z) {
                log("processAirPlaneForGameOpt process isAirPlaneOn");
                resetResidentGameCellOptimization(true);
            }
        }
    }

    private void processCallStateForGameOpt(int i, int i2) {
        if (MiuiPhoneUtils.isSupportQcGameResidentCellOptimization() && this.isEnterGame) {
            if ((SelfStudyControllerBase.mEnterGameOptimizer || this.isStaticCell) && MiuiPhoneUtils.isDuringCall()) {
                log("processCallStateChange process inCall");
                resetResidentGameCellOptimization(true);
            }
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    protected void processCallStateChange(int i, int i2) {
        if (MiuiPhoneUtils.isSupportQcGameResidentCellOptimization()) {
            processCallStateForGameOpt(i, i2);
        }
    }

    private void gameStatistic20Min() {
        QcSelfStudyController qcSelfStudyController;
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
            this.isStaticCell = isStaticScene();
            this.mWorkHandler.removeMessages(46);
            if (this.isFirst20min && this.isStaticCell) {
                Handler handler2 = this.mWorkHandler;
                handler2.sendMessageDelayed(handler2.obtainMessage(37, this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq + "-" + this.oldGameRat), 20000L);
            }
            boolean z = this.isStaticCell;
            if (z) {
                log("is static scene");
                recordCount("gameStaticCellActualCnt", "game");
                updateDurOrLagDur(jCurrentTimeMillis);
                qcSelfStudyController = this;
                qcSelfStudyController.saveTopCellForStaticGame(this.oldGameGci + "-" + this.oldGamePci + "-" + this.oldGameFreq, this.oldGameRat, this.mCurrentGameCellStats, this.mTopCellForStaticGame);
                StringBuilder sb = new StringBuilder("mTopCellForStaticGame: ");
                sb.append(qcSelfStudyController.mTopCellForStaticGame);
                qcSelfStudyController.log(sb.toString());
                qcSelfStudyController.mCurrentGameCellStats.clear();
                Handler handler3 = qcSelfStudyController.mWorkHandler;
                handler3.sendMessageDelayed(handler3.obtainMessage(36), 1200000L);
            } else {
                qcSelfStudyController = this;
                if (!z) {
                    qcSelfStudyController.log("not static scene");
                    Handler handler4 = qcSelfStudyController.mWorkHandler;
                    handler4.sendMessageDelayed(handler4.obtainMessage(38), 30000L);
                }
            }
            qcSelfStudyController.isFirst20min = false;
        }
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
        if (TextUtils.isEmpty(this.tmManager.getNetworkOperatorForPhone(MiuiPhoneUtils.getActiveDataPhone().getPhoneId()))) {
            return;
        }
        log("resetActionForGameCell");
        if (z && isTopGameCell(str2, stringToInt, stringToInt2, stringToInt3)) {
            setOrResetHdr(false);
            this.isSetGameTopCell = true;
            this.mTop2Cell.put(str2 + "-" + stringToInt + "-" + stringToInt2 + "-" + stringToInt3, 0);
            return;
        }
        if (this.isStaticCell) {
            boolean zIsTopCellForStaticGame = isTopCellForStaticGame(str2 + "-" + stringToInt + "-" + stringToInt2, this.mTopCellForStaticGame);
            StringBuilder sb = new StringBuilder(" resetActionForGameCell isTop: ");
            sb.append(zIsTopCellForStaticGame);
            log(sb.toString());
            if (zIsTopCellForStaticGame) {
                setOrResetHdr(false);
                this.isSetGameTopCell = true;
                this.mTop2Cell.put(str2 + "-" + stringToInt + "-" + stringToInt2 + "-" + stringToInt3, 0);
            }
        }
    }

    private void setGameCellPenalt(String str) {
        if (shouldDoActionForGameOptForOS4() && str != null && !this.mTop2Cell.isEmpty() && this.mTop2Cell.containsKey(str)) {
            if (SelfStudyControllerBase.mEnterGameOptimizer || this.isStaticCell) {
                log("setGameCellPenalt mSetPenaltTime: " + this.mSetPenaltTime);
                if (System.currentTimeMillis() - this.mSetPenaltTime < 600000) {
                    log("Less than 10 minutes since last penalty, skip");
                    return;
                }
                setOrResetHdr(true);
                this.mSetPenaltTime = System.currentTimeMillis();
                this.mTop2Cell.remove(str);
            }
        }
    }

    private void setOrResetHdr(boolean z) {
        CellIdentity currentCellIdentity;
        CellInfoData cellInfoDataProcessLteCell;
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null || (currentCellIdentity = activeDataPhone.getCurrentCellIdentity()) == null) {
            return;
        }
        try {
            String mccString = currentCellIdentity.getMccString();
            String mncString = currentCellIdentity.getMncString();
            if (currentCellIdentity instanceof CellIdentityNr) {
                cellInfoDataProcessLteCell = processNrCell((CellIdentityNr) currentCellIdentity, z);
            } else if (currentCellIdentity instanceof CellIdentityLte) {
                cellInfoDataProcessLteCell = processLteCell((CellIdentityLte) currentCellIdentity, z);
            } else {
                log("Unsupported cell identity type");
                return;
            }
            if (cellInfoDataProcessLteCell == null) {
                return;
            }
            String strJoin = String.join(",", cellInfoDataProcessLteCell.networkType, mccString, mncString, String.valueOf(cellInfoDataProcessLteCell.cgi), cellInfoDataProcessLteCell.activeBandKey, String.valueOf(cellInfoDataProcessLteCell.arfcn), String.valueOf(cellInfoDataProcessLteCell.pci));
            if (MiuiPhoneUtils.isCellPreferOptSupported() && MiuiPhoneUtils.isSNSV4SupportedByQcom()) {
                CellPreferManager.getInstance().processSetHdrCellInfo(strJoin);
            }
        } catch (Exception e) {
            log("setHdr exception: " + e);
        }
    }

    class CellInfoData {
        String activeBandKey;
        int arfcn;
        long cgi;
        String networkType;
        int pci;

        private CellInfoData() {
        }
    }

    private CellInfoData processNrCell(CellIdentityNr cellIdentityNr, boolean z) {
        int[] bands = cellIdentityNr.getBands();
        if (!validateBands(bands, "NR")) {
            return null;
        }
        String str = "NR5G_BAND_" + bands[0];
        if (mActiveBand.get(str) == null) {
            log("nr band is null");
            return null;
        }
        CellInfoData cellInfoData = new CellInfoData();
        cellInfoData.networkType = "nr";
        cellInfoData.activeBandKey = str;
        if (z) {
            cellInfoData.cgi = 0L;
            cellInfoData.arfcn = 0;
            cellInfoData.pci = 2000;
            return cellInfoData;
        }
        cellInfoData.cgi = cellIdentityNr.getNci();
        cellInfoData.arfcn = cellIdentityNr.getNrarfcn();
        cellInfoData.pci = cellIdentityNr.getPci();
        return cellInfoData;
    }

    private CellInfoData processLteCell(CellIdentityLte cellIdentityLte, boolean z) {
        int[] bands = cellIdentityLte.getBands();
        if (!validateBands(bands, "LTE")) {
            return null;
        }
        String str = "E_UTRA_OPERATING_BAND_" + bands[0];
        if (mActiveBand.get(str) == null) {
            log("lte band is null");
            return null;
        }
        CellInfoData cellInfoData = new CellInfoData();
        cellInfoData.networkType = "lte";
        cellInfoData.activeBandKey = str;
        if (z) {
            cellInfoData.cgi = 0L;
            cellInfoData.arfcn = 0;
            cellInfoData.pci = 2000;
            return cellInfoData;
        }
        cellInfoData.cgi = cellIdentityLte.getCi();
        cellInfoData.arfcn = cellIdentityLte.getEarfcn();
        cellInfoData.pci = cellIdentityLte.getPci();
        return cellInfoData;
    }

    private void initBandData() {
        for (String str : "BC_0 = 0,BC_1 = 1,BC_3 = 3,BC_4 = 4,BC_5 = 5,BC_6 = 6,BC_7 = 7,BC_8 = 8,BC_9 = 9,BC_10 = 10,BC_11 = 11,BC_12 = 12,BC_13 = 13,BC_14 = 14,BC_15 = 15,BC_16 = 16,BC_17 = 17,BC_18 = 18,BC_19 = 19,GSM_450 = 40,GSM_480 = 41,GSM_750 = 42,GSM_850 = 43,GSM_900_EXTENDED = 44,GSM_900_PRIMARY = 45,GSM_900_RAILWAYS = 46,GSM_1800 = 47,GSM_1900 = 48,WCDMA_2100 = 80,WCDMA_PCS_1900 = 81,WCDMA_DCS_1800 = 82,WCDMA_1700_US = 83,WCDMA_850 = 84,WCDMA_800 = 85,WCDMA_2600 = 86,WCDMA_900 = 87,WCDMA_1700_JAPAN = 88,WCDMA_1500_JAPAN = 90,WCDMA_850_JAPAN = 91,E_UTRA_OPERATING_BAND_1 = 120,E_UTRA_OPERATING_BAND_2 = 121,E_UTRA_OPERATING_BAND_3 = 122,E_UTRA_OPERATING_BAND_4 = 123,E_UTRA_OPERATING_BAND_5 = 124,E_UTRA_OPERATING_BAND_6 = 125,E_UTRA_OPERATING_BAND_7 = 126,E_UTRA_OPERATING_BAND_8 = 127,E_UTRA_OPERATING_BAND_9 = 128,E_UTRA_OPERATING_BAND_10 = 129,E_UTRA_OPERATING_BAND_11 = 130,E_UTRA_OPERATING_BAND_12 = 131,E_UTRA_OPERATING_BAND_13 = 132,E_UTRA_OPERATING_BAND_14 = 133,E_UTRA_OPERATING_BAND_17 = 134,E_UTRA_OPERATING_BAND_33 = 135,E_UTRA_OPERATING_BAND_34 = 136,E_UTRA_OPERATING_BAND_35 = 137,E_UTRA_OPERATING_BAND_36 = 138,E_UTRA_OPERATING_BAND_37 = 139,E_UTRA_OPERATING_BAND_38 = 140,E_UTRA_OPERATING_BAND_39 = 141,E_UTRA_OPERATING_BAND_40 = 142,E_UTRA_OPERATING_BAND_18 = 143,E_UTRA_OPERATING_BAND_19 = 144,E_UTRA_OPERATING_BAND_20 = 145,E_UTRA_OPERATING_BAND_21 = 146,E_UTRA_OPERATING_BAND_24 = 147,E_UTRA_OPERATING_BAND_25 = 148,E_UTRA_OPERATING_BAND_41 = 149,E_UTRA_OPERATING_BAND_42 = 150,E_UTRA_OPERATING_BAND_43 = 151,E_UTRA_OPERATING_BAND_23 = 152,E_UTRA_OPERATING_BAND_26 = 153,E_UTRA_OPERATING_BAND_32 = 154,E_UTRA_OPERATING_BAND_125 = 155,E_UTRA_OPERATING_BAND_126 = 156,E_UTRA_OPERATING_BAND_127 = 157,E_UTRA_OPERATING_BAND_28 = 158,E_UTRA_OPERATING_BAND_29 = 159,E_UTRA_OPERATING_BAND_30 = 160,E_UTRA_OPERATING_BAND_66 = 161,E_UTRA_OPERATING_BAND_250 = 162,E_UTRA_OPERATING_BAND_46 = 163,E_UTRA_OPERATING_BAND_27 = 164,E_UTRA_OPERATING_BAND_31 = 165,E_UTRA_OPERATING_BAND_71 = 166,E_UTRA_OPERATING_BAND_47 = 167,E_UTRA_OPERATING_BAND_48 = 168,E_UTRA_OPERATING_BAND_67 = 169,E_UTRA_OPERATING_BAND_68 = 170,E_UTRA_OPERATING_BAND_49 = 171,E_UTRA_OPERATING_BAND_85 = 172,E_UTRA_OPERATING_BAND_72 = 173,E_UTRA_OPERATING_BAND_73 = 174,E_UTRA_OPERATING_BAND_86 = 175,E_UTRA_OPERATING_BAND_53 = 176,E_UTRA_OPERATING_BAND_87 = 177,E_UTRA_OPERATING_BAND_88 = 178,E_UTRA_OPERATING_BAND_70 = 179,TDSCDMA_BAND_A = 200,TDSCDMA_BAND_B = 201,TDSCDMA_BAND_C = 202,TDSCDMA_BAND_D = 203,TDSCDMA_BAND_E = 204,TDSCDMA_BAND_F = 205,NR5G_BAND_1 = 250,NR5G_BAND_2 = 251,NR5G_BAND_3 = 252,NR5G_BAND_5 = 253,NR5G_BAND_7 = 254,NR5G_BAND_8 = 255,NR5G_BAND_20 = 256,NR5G_BAND_28 = 257,NR5G_BAND_38 = 258,NR5G_BAND_41 = 259,NR5G_BAND_50 = 260,NR5G_BAND_51 = 261,NR5G_BAND_66 = 262,NR5G_BAND_70 = 263,NR5G_BAND_71 = 264,NR5G_BAND_74 = 265,NR5G_BAND_75 = 266,NR5G_BAND_76 = 267,NR5G_BAND_77 = 268,NR5G_BAND_78 = 269,NR5G_BAND_79 = 270,NR5G_BAND_80 = 271,NR5G_BAND_81 = 272,NR5G_BAND_82 = 273,NR5G_BAND_83 = 274,NR5G_BAND_84 = 275,NR5G_BAND_85 = 276,NR5G_BAND_257 = 277,NR5G_BAND_258 = 278,NR5G_BAND_259 = 279,NR5G_BAND_260 = 280,NR5G_BAND_261 = 281,NR5G_BAND_12 = 282,NR5G_BAND_25 = 283,NR5G_BAND_34 = 284,NR5G_BAND_39 = 285,NR5G_BAND_40 = 286,NR5G_BAND_65 = 287,NR5G_BAND_86 = 288,NR5G_BAND_48 = 289,NR5G_BAND_14 = 290,NR5G_BAND_13 = 291,NR5G_BAND_18 = 292,NR5G_BAND_26 = 293,NR5G_BAND_30 = 294,NR5G_BAND_29 = 295,NR5G_BAND_53 = 296,NR5G_BAND_46 = 297,NR5G_BAND_91 = 298,NR5G_BAND_92 = 299,NR5G_BAND_93 = 300,NR5G_BAND_94 = 301".split(",")) {
            try {
                String[] strArrSplit = str.split("=");
                if (strArrSplit.length >= 2) {
                    mActiveBand.put(strArrSplit[0].trim(), Integer.valueOf(Integer.parseInt(strArrSplit[1].trim())));
                }
            } catch (Exception e) {
                Log.e("QcSelfStudyController", "initBandData error: " + str, e);
            }
        }
    }

    private boolean isStaticCell() {
        return (this.isStaticCell || this.isFirst20min) && MiuiPhoneUtils.isSupportGameResidentCellOptForOS4();
    }

    private void setStaticMessage() {
        if (shouldDoActionForGameOptForOS4() && !this.isStaticCell && this.isFirst20min) {
            checkStaticState(true);
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(46), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        }
    }

    private void setGameDelayLevel(int i) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        handleGameBattleStart(jCurrentTimeMillis);
        this.mWorkHandler.removeMessages(1049);
        this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(1049, Boolean.FALSE), 30000L);
        log("gameDelayLevel: " + i);
        if (isInHomeOrCompanyScene(this.curGameScene) || this.isStaticCell) {
            recordDelayLevelChange(i, jCurrentTimeMillis);
            if ((isEnterGameOptimizer() || this.isStaticCell) && this.mIsMobileDataOn) {
                this.mCurrentGameDelayLevel = i;
                processgameDelayChangedForHasDelayGame(i);
                gameLastDelayLevel(i);
            }
        }
        if (i >= 1) {
            handleGameLagState(jCurrentTimeMillis, i);
        } else {
            handleGameNotLagState(jCurrentTimeMillis);
        }
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
        this.mWorkHandler.removeMessages(37);
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
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public void checkLevelOverTime(int i) {
        if ((isEnterGameOptimizer() || this.isStaticCell) && isHasDelayApiGame() && this.mCurrentGameDelayLevel >= i) {
            if (!MiuiPhoneUtils.isSupportGameResidentCellOptForOS4() || this.mSetGameOptNr2Lte) {
                handleGameStall();
            } else {
                requestNeighborCellInfoCommon();
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

    private void recordDelayLevelChange(int i, long j) {
        int i2;
        long j2 = this.mlastGameLevelTime;
        if (j2 != 0 && (i2 = this.mlastGameDelay) != i) {
            recordGameDelayLevelDuration(j - j2, i2, this.isStaticCell);
        }
        this.mlastGameLevelTime = j;
    }

    private boolean isNetworkOptimizationForSa() {
        if (this.mRecovery == null) {
            return false;
        }
        return this.mRecovery.isSaOffInRecoveryAction(DefaultSimManager.getDefaultDataSlotId());
    }

    private void processGameExit() {
        if (this.isEnterGame) {
            log("processGameExit");
            if (this.mSetGameOptNr2Lte) {
                resetGameResidentNetworkOptimization(true);
            }
            if (SelfStudyControllerBase.mEnterGameOptimizer || this.isStaticCell) {
                resetDataStallOptimization();
            }
            if (this.isSetGameTopCell) {
                setOrResetHdr(true);
            }
            resetBenefitStatisticList();
            isSetDataStallForGameOpt(false);
            removeDelayCheckMessages();
            stopModemModel();
        }
        this.mlastGameDelay = 0;
        this.mlastGameLevelTime = 0L;
        this.mSetPenaltTime = 0L;
        this.minsertDbTime = 0L;
        this.startGameTime = 0L;
        this.curGameScene = 2;
        this.mEnterGameBattleCount = 0;
        this.isGameDebug = false;
        SelfStudyControllerBase.mEnterGameOptimizer = false;
        this.isEnterGame = false;
        this.isGameLag = false;
        this.mEnterGameBattleState = false;
        this.mSetGameOptNr2Lte = false;
        this.isFirst20min = true;
        this.isSetGameTopCell = false;
        this.isStaticCell = false;
        resetStaticState();
        SystemProperties.set("gsm.game.scene", "0");
        this.mTopGamePackage = "";
        this.mLastTopGamePackage = "";
        this.mEnterGameBoosterCount = 0;
        this.mCurrentGameCellStats.clear();
        this.mTopCellForStaticGame.clear();
        this.mTop2Cell.clear();
        this.mWorkHandler.removeMessages(1049);
        this.mWorkHandler.removeMessages(37);
        this.mWorkHandler.removeMessages(33);
        this.mWorkHandler.removeMessages(46);
        if (this.mWorkHandler.hasMessages(36)) {
            this.mWorkHandler.removeMessages(36);
        }
        if (this.mWorkHandler.hasMessages(32)) {
            this.mWorkHandler.removeMessages(32);
        }
        if (this.mWorkHandler.hasMessages(38)) {
            this.mWorkHandler.removeMessages(38);
        }
        if (this.mWorkHandler.hasMessages(40)) {
            this.mWorkHandler.removeMessages(40);
        }
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
            ((SelfStudyControllerBase) QcSelfStudyController.this).mWorkHandler.obtainMessage(1047, Integer.valueOf(i)).sendToTarget();
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameStatusChanged(int i, String str) {
            Message messageObtainMessage = ((SelfStudyControllerBase) QcSelfStudyController.this).mWorkHandler.obtainMessage(1048, str);
            messageObtainMessage.arg1 = i;
            ((SelfStudyControllerBase) QcSelfStudyController.this).mWorkHandler.sendMessage(messageObtainMessage);
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameBattleStatusChanged(boolean z) {
            ((SelfStudyControllerBase) QcSelfStudyController.this).mWorkHandler.sendMessage(((SelfStudyControllerBase) QcSelfStudyController.this).mWorkHandler.obtainMessage(1049, Boolean.valueOf(z)));
        }
    }

    @Override // com.android.phone.performance.SelfStudyControllerBase
    public boolean isGameResidentLag() {
        return this.isGameLag;
    }
}
