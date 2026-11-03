package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.AccessNetworkUtils;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.NetworkSpeedTracker;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.ot.pubsub.util.v;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyStatAdapter;
import miui.telephony.qms.CellListInfoS;
import miui.telephony.qms.CellListS;
import miui.telephony.qms.CellPropertyCategoryCommonS;
import miui.telephony.qms.CellPropertyCategoryHdrS;
import miui.telephony.qms.CellPropertyCategoryRsS;
import miui.telephony.qms.CellPropertyCategoryS;
import miui.telephony.qms.LocationS;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.NrDubiousCellInfoS;
import miui.telephony.qms.PlmnIdS;
import miui.telephony.qms.UniqueCellInfoS;

/* JADX INFO: loaded from: classes.dex */
public class CellPreferManager {
    private static Context mContext;
    private static CellPreferManager mInstance;
    private final Map mCellHdrStats;
    private CellScoringEngine mCellScoringEngine;
    private int mCurrentGameDelay;
    private GameInfoCallBack mGameInfoCallBack;
    private CellPreferHandler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    private boolean mIsInGameBackgroundMonitor;
    private boolean mIsInGameBattle;
    private boolean mIsInHSTStation;
    private boolean mIsInRealTimeGame;
    private boolean mIsMobileConnected;
    private boolean mIsWifiConnected;
    private long mLastNwDeniedTime;
    private long mLastSetCellId;
    private int mLastSetLteNrPhoneId;
    private long mLastSetLteNrTime;
    private long mLastTriggerOptCgi;
    private long mLastTriggerOptCgiTime;
    private ArrayList mLteDubiousCellCgiList;
    private ArrayList mNrDubiousCellCgiList;
    List mScoringResultList;
    ContentObserver mSubwayStateObserver;
    private String mTopGamePkg;
    private HandlerThread mWorkThread;
    private static final Map mActiveBand = new HashMap();
    private static Map mBandMappingScs = new HashMap();
    private static final List LIST_SUPPORTED_REAL_TIME_GAMES = Arrays.asList("com.tencent.tmgp.sgame", "com.tencent.lolm", "com.tencent.jkchess");
    private static final boolean IS_INDIA = "in".equalsIgnoreCase(SystemProperties.get("ro.miui.build.region"));
    private static final boolean IS_QCOM = MiuiTelephony.IS_QCOM;
    private boolean VDBG = false;
    private int INVALID_VALUE = -1;
    private TelephonyCallBack mTelephonyCallBack = null;
    private CellPreferDBHelper mCellPreferDBHelper = null;
    private NetworkModeRecognition mNetworkModeRecognition = null;
    private ElevatorModeController mElevatorModeController = null;
    private int mDdsPhoneId = -1;
    private boolean mIsDdsChanging = false;
    private ServiceState mDdsServiceState = null;
    private boolean mIsInSubway = false;
    private Set mFddSet = new HashSet();
    private Set mTddSet = new HashSet();
    private Map mFdd2TddMap = new HashMap();
    private boolean mLastIsFddCell = false;
    private long mLastFddGlobalCellId = Long.MAX_VALUE;
    private int mCurCoverageStatus = this.INVALID_VALUE;
    private Map mLastSetCoverageInfo = new HashMap();
    private int mDemandMinCount = 3;
    private final Map mSubwayFdd2TddStats = new ConcurrentHashMap();
    private long mLastNwDeniedCgi = Long.MAX_VALUE;
    private int mNwDeniedCount = 0;

    private boolean isNwDenied(int i) {
        return i == 9 || i == 10;
    }

    public static synchronized CellPreferManager getInstance() {
        try {
            if (mInstance == null) {
                mInstance = new CellPreferManager();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private CellPreferManager() {
        int i = this.INVALID_VALUE;
        this.mLastNwDeniedTime = i;
        this.mLastTriggerOptCgi = Long.MAX_VALUE;
        this.mLastTriggerOptCgiTime = i;
        this.mLastSetLteNrTime = i;
        this.mLastSetLteNrPhoneId = i;
        this.mNrDubiousCellCgiList = new ArrayList();
        this.mLteDubiousCellCgiList = new ArrayList();
        this.mIsInHSTStation = false;
        this.mCurrentGameDelay = this.INVALID_VALUE;
        this.mIsInRealTimeGame = false;
        this.mTopGamePkg = null;
        this.mIsInGameBattle = false;
        this.mGameInfoCallBack = null;
        this.mCellScoringEngine = null;
        this.mScoringResultList = null;
        this.mIsInGameBackgroundMonitor = false;
        this.mCellHdrStats = new ConcurrentHashMap();
        this.mLastSetCellId = this.INVALID_VALUE;
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.CellPreferManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) {
                    return;
                }
                String action = intent.getAction();
                CellPreferManager.this.log("receive action " + action);
                if ("CellPreferManager.intent.debug".equals(action)) {
                    CellPreferManager.this.mHandler.obtainMessage(100, intent).sendToTarget();
                    return;
                }
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED") || action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY")) {
                    CellPreferManager.this.mHandler.obtainMessage(2).sendToTarget();
                    return;
                }
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    CellPreferManager.this.mHandler.obtainMessage(8, intent).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.SUBWAY_SIGNAL_MAP_TUNNEL_TO_PLATFORM".equals(action)) {
                    CellPreferManager.this.mHandler.obtainMessage(14, intent).sendToTarget();
                } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                    CellPreferManager.this.mHandler.obtainMessage(15, intent).sendToTarget();
                } else if ("com.android.phone.intent.action.HST_STATION_STATUS_CHANGED".equals(action)) {
                    CellPreferManager.this.mHandler.obtainMessage(16, intent).sendToTarget();
                }
            }
        };
        this.mSubwayStateObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.CellPreferManager.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean zIsInSubway = CellPreferManager.this.isInSubway();
                if (CellPreferManager.this.mIsInSubway != zIsInSubway) {
                    CellPreferManager.this.mIsInSubway = zIsInSubway;
                    CellPreferManager.this.mHandler.obtainMessage(5).sendToTarget();
                }
            }
        };
        log("CellPreferManager Init");
        mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("CellPreferManager", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        CellPreferHandler cellPreferHandler = new CellPreferHandler(this.mWorkThread.getLooper());
        this.mHandler = cellPreferHandler;
        cellPreferHandler.sendEmptyMessage(1);
    }

    class CellPreferHandler extends Handler {
        CellPreferHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws Throwable {
            CellPreferManager.this.log("handleMessage=" + message.what);
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 1:
                        CellPreferManager.this.initAction();
                        break;
                    case 2:
                        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                        if (CellPreferManager.this.mDdsPhoneId != defaultDataSlotId) {
                            CellPreferManager.this.enterDdsChangedMonitorStatus();
                            CellPreferManager cellPreferManager = CellPreferManager.this;
                            cellPreferManager.enableCellBarringFeature(cellPreferManager.mDdsPhoneId, false);
                            CellPreferManager.this.enableCellBarringFeature(defaultDataSlotId, true);
                            CellPreferManager.this.mDdsPhoneId = defaultDataSlotId;
                            CellPreferManager.this.resetLastFddCellInfo();
                            CellPreferManager.this.setLteNrCellBarringInfo(Long.MAX_VALUE, false);
                        }
                        CellPreferManager.this.log("EVENT_DATA_SLOT_READY_OR_CHANGED mDdsPhoneId=" + CellPreferManager.this.mDdsPhoneId);
                        break;
                    case 3:
                        if (CellPreferManager.this.mIsInSubway) {
                            CellPreferManager.this.processCellLocationChanged(message.arg1);
                        }
                        if (CellPreferManager.this.mIsInHSTStation) {
                            CellPreferManager.this.processCellLocationChangedForHSTStation(message.arg1);
                        }
                        break;
                    case 4:
                        if (CellPreferManager.this.mCurCoverageStatus == 1) {
                            CellPreferManager.this.setCoverageState(message.arg1, 2, (String) message.obj);
                            if (CellPreferManager.this.mElevatorModeController != null) {
                                CellPreferManager.this.mElevatorModeController.recordOutCoverageStateV3(message.arg1, (String) message.obj);
                            }
                        }
                        break;
                    case 5:
                        CellPreferManager.this.processSubwayStateChanged();
                        break;
                    case 6:
                        CellPreferManager.this.processCheckNetworkSpeed(((Integer) message.obj).intValue());
                        break;
                    case 7:
                        CellPreferManager.this.exitDdsChangedMonitorStatus();
                        break;
                    case 8:
                        CellPreferManager.this.processSimStateChanged();
                        break;
                    case 9:
                    case 13:
                        CellPreferManager.this.processServiceStateChanged(message.arg1, (ServiceState) message.obj);
                        break;
                    case 10:
                        HashMap map = (HashMap) message.obj;
                        if (map != null && map.size() > 0) {
                            Map.Entry entry = (Map.Entry) new ArrayList(map.entrySet()).get(0);
                            CellPreferManager.this.setLteNrCellBarringInfo(((Long) entry.getKey()).longValue(), ((Boolean) entry.getValue()).booleanValue());
                            break;
                        }
                        break;
                    case 11:
                    case 12:
                        CellPreferManager.this.enableCellBarringFeature(message.arg1, ((Boolean) message.obj).booleanValue());
                        break;
                    case 14:
                        CellPreferManager.this.processReceiveTunnelToPlateformCell((Intent) message.obj);
                        break;
                    case 15:
                        CellPreferManager.this.processNetworkStateChanged((Intent) message.obj);
                        break;
                    case 16:
                        CellPreferManager.this.processHstStationStatusChanged((Intent) message.obj);
                        break;
                    case 17:
                        if (CellPreferManager.this.mIsInRealTimeGame) {
                            CellPreferManager.this.mCurrentGameDelay = ((Integer) message.obj).intValue();
                            CellPreferManager.this.log("gameDelay=" + CellPreferManager.this.mCurrentGameDelay);
                        }
                        break;
                    case 18:
                        CellPreferManager.this.processGameStatusChanged(message.arg1, (String) message.obj);
                        break;
                    case 19:
                        if (CellPreferManager.this.mIsInRealTimeGame) {
                            CellPreferManager.this.processGameBattleStatusChanged(((Boolean) message.obj).booleanValue(), false);
                        }
                        break;
                    case 20:
                        CellPreferManager.this.log("EVENT_GAME_BACKGROUND_LINGER");
                        CellPreferManager.this.mIsInGameBackgroundMonitor = false;
                        CellPreferManager.this.mIsInRealTimeGame = false;
                        CellPreferManager.this.processGameBattleStatusChanged(false, true);
                        break;
                }
                return;
            }
            CellPreferManager.this.VDBG = true;
            CellPreferManager.this.mDemandMinCount = 1;
            if (MiuiPhoneUtils.isHstStationCellPreferSupported()) {
                CellPreferManager.this.processSetHdrCellInfo(((Intent) message.obj).getStringExtra("cellinfo"));
            }
        }
    }

    public void processSetHdrCellInfo(String str) {
        log("processSetHdrCellInfo cellInfo=" + str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String[] strArrSplit = str.split(",");
        if (strArrSplit == null || strArrSplit.length != 7) {
            log("cellInfoArr not satify");
            return;
        }
        try {
            setCellListInfo(new CellScoringEngine.SnsCellIdentity(Short.parseShort(strArrSplit[1]), Short.parseShort(strArrSplit[2]), strArrSplit[2].length() == 3, Long.parseLong(strArrSplit[3]), ((Integer) mActiveBand.get(strArrSplit[4])).intValue(), Integer.parseInt(strArrSplit[5]), Short.parseShort(strArrSplit[6]), "nr".equals(strArrSplit[0])));
        } catch (Exception e) {
            log("parseCommonCellInfo exception:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameStatusChanged(int i, String str) {
        this.mTopGamePkg = str;
        boolean z = i == 2 && LIST_SUPPORTED_REAL_TIME_GAMES.contains(str);
        log("processGameStatusChanged gameType=" + i + ", topPkg=" + this.mTopGamePkg + ", lastInGame=" + this.mIsInRealTimeGame + ", curInGame=" + z + ", isInMonitor=" + this.mIsInGameBackgroundMonitor);
        if (this.mIsInRealTimeGame != z || this.mIsInGameBackgroundMonitor) {
            if (z) {
                this.mIsInRealTimeGame = true;
                stopTimerForGameBackground();
                this.mCellScoringEngine.setAllowSample(true);
            } else if (this.mIsInGameBattle) {
                startTimerForGameBackground();
                this.mCellScoringEngine.setAllowSample(false);
            } else {
                this.mIsInRealTimeGame = false;
            }
        }
    }

    private void startTimerForGameBackground() {
        log("startTimerForGameBackground");
        stopTimerForGameBackground();
        CellPreferHandler cellPreferHandler = this.mHandler;
        cellPreferHandler.sendMessageDelayed(cellPreferHandler.obtainMessage(20), 30000L);
        this.mIsInGameBackgroundMonitor = true;
    }

    private void stopTimerForGameBackground() {
        if (this.mHandler.hasMessages(20)) {
            log("stopTimerForGameBackground");
            this.mHandler.removeMessages(20);
            this.mIsInGameBackgroundMonitor = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHstStationStatusChanged(Intent intent) {
        String stringExtra = intent.getStringExtra("hst_station_name");
        boolean booleanExtra = intent.getBooleanExtra("hst_station_status", false);
        localLog("hstStationStatus", "stationName=" + stringExtra + ", status=" + booleanExtra);
        if (this.mIsInHSTStation == booleanExtra) {
            return;
        }
        this.mIsInHSTStation = booleanExtra;
        if (booleanExtra) {
            registerGameInfoCallBack();
            recordCellHdrCount("enterHstStation");
        } else {
            unregisterGameInfoCallBack();
            this.mScoringResultList = this.mCellScoringEngine.stopGame(CellScoringEngine.GameStopReason.EXIT_HST_STATION);
            resetGameState();
        }
    }

    private void resetGameState() {
        log("resetGameState");
        this.mIsInRealTimeGame = false;
        this.mIsInGameBattle = false;
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
                return;
            }
            if (type != 1 || this.mIsWifiConnected == z) {
                return;
            }
            this.mIsWifiConnected = z;
            if (z) {
                this.mScoringResultList = this.mCellScoringEngine.stopGame(CellScoringEngine.GameStopReason.WIFI_CONNECTED);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameBattleStatusChanged(boolean z, boolean z2) {
        List list;
        log("processGameBattleStatusChanged, old=" + this.mIsInGameBattle + ", new=" + z);
        if (this.mIsInGameBattle == z) {
            return;
        }
        this.mIsInGameBattle = z;
        if (z) {
            if (this.mIsWifiConnected) {
                return;
            }
            List list2 = this.mScoringResultList;
            if (list2 != null && list2.size() > 0) {
                setCellListInfo(((CellScoringEngine.ScoringResult) this.mScoringResultList.get(0)).getCellIdentity());
                clearScoringResultList();
            }
            this.mCellScoringEngine.startGame(this.mTopGamePkg);
            this.mCellScoringEngine.switchCell(getCellIdentityByPhoneId(this.mDdsPhoneId));
            return;
        }
        CellScoringEngine cellScoringEngine = this.mCellScoringEngine;
        if (z2) {
            this.mScoringResultList = cellScoringEngine.stopGame(CellScoringEngine.GameStopReason.EXIT_GAME);
        } else {
            this.mScoringResultList = cellScoringEngine.stopGame(CellScoringEngine.GameStopReason.GAME_BATTLE_END);
        }
        if (!this.VDBG || (list = this.mScoringResultList) == null || list.size() <= 0) {
            return;
        }
        Iterator it = this.mScoringResultList.iterator();
        while (it.hasNext()) {
            log("ScoringResult: " + ((CellScoringEngine.ScoringResult) it.next()).toString());
        }
    }

    private void clearScoringResultList() {
        log("clearScoringResultList");
        List list = this.mScoringResultList;
        if (list == null || list.size() <= 0) {
            return;
        }
        this.mScoringResultList.clear();
    }

    private void setCellListInfo(CellScoringEngine.SnsCellIdentity snsCellIdentity) {
        if (IS_INDIA) {
            log("setCellListInfo not support in india");
            return;
        }
        log("setCellListInfo, ci=" + snsCellIdentity);
        if (IS_QCOM) {
            boolean z = snsCellIdentity.isNrCell;
            ArrayList arrayList = new ArrayList();
            CellListS cellListS = new CellListS();
            cellListS.mitigation_type = 1;
            cellListS.reset_flag = true;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            CellListInfoS cellListInfoS = new CellListInfoS();
            cellListInfoS.neighbor_cluster_id = -1;
            cellListInfoS.anchor_cell = 0;
            cellListInfoS.cause_bitmask = 1;
            cellListInfoS.expiry_timer = 30;
            UniqueCellInfoS uniqueCellInfoS = new UniqueCellInfoS();
            PlmnIdS plmnIdS = new PlmnIdS();
            plmnIdS.mcc = snsCellIdentity.mcc;
            plmnIdS.mnc = snsCellIdentity.mnc;
            plmnIdS.mncIncludesPcsDigit = snsCellIdentity.mncIncludesPcsDigit;
            uniqueCellInfoS.plmn = plmnIdS;
            uniqueCellInfoS.cell_identity = snsCellIdentity.ci;
            uniqueCellInfoS.band = snsCellIdentity.band;
            uniqueCellInfoS.freq = snsCellIdentity.freq;
            uniqueCellInfoS.pci = snsCellIdentity.pci;
            cellListInfoS.cell_info = uniqueCellInfoS;
            CellPropertyCategoryS cellPropertyCategoryS = new CellPropertyCategoryS();
            cellPropertyCategoryS.common_valid = false;
            cellPropertyCategoryS.hdr_valid = true;
            cellPropertyCategoryS.rs_valid = false;
            CellPropertyCategoryCommonS cellPropertyCategoryCommonS = new CellPropertyCategoryCommonS();
            cellPropertyCategoryCommonS.priority = -1;
            cellPropertyCategoryCommonS.location_valid = false;
            LocationS locationS = new LocationS();
            locationS.longitude = 0;
            locationS.latitude = 0;
            cellPropertyCategoryCommonS.location = locationS;
            CellPropertyCategoryHdrS cellPropertyCategoryHdrS = new CellPropertyCategoryHdrS();
            cellPropertyCategoryHdrS.service_type = -1;
            cellPropertyCategoryHdrS.load = -1;
            cellPropertyCategoryHdrS.expiry_timer = 30;
            CellPropertyCategoryRsS cellPropertyCategoryRsS = new CellPropertyCategoryRsS();
            cellPropertyCategoryRsS.shared_plmn_len = 0;
            cellPropertyCategoryRsS.shared_plmn = new ArrayList();
            cellPropertyCategoryS.common = cellPropertyCategoryCommonS;
            cellPropertyCategoryS.hdr = cellPropertyCategoryHdrS;
            cellPropertyCategoryS.rs = cellPropertyCategoryRsS;
            cellListInfoS.property = cellPropertyCategoryS;
            if (z) {
                cellListS.lte_cell_list_len = (byte) 0;
                cellListS.nr_cell_list_len = (byte) 1;
                arrayList3.add(cellListInfoS);
            } else {
                cellListS.lte_cell_list_len = (byte) 1;
                cellListS.nr_cell_list_len = (byte) 0;
                arrayList2.add(cellListInfoS);
            }
            cellListS.lte_cell_list = arrayList2;
            cellListS.nr_cell_list = arrayList3;
            arrayList.add(cellListS);
            this.mLastSetCellId = snsCellIdentity.ci;
            recordCellHdrCount("setCellCount");
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setCellListInfo(arrayList, "hst");
                return;
            }
            return;
        }
        if (MiuiPhoneUtils.isHighPriorityCellSelectionSupported()) {
            ArrayList arrayList4 = new ArrayList();
            arrayList4.add(new HighPriorityCellSelection.HighPriorityCellInfo(snsCellIdentity.mcc, snsCellIdentity.mnc, snsCellIdentity.mncIncludesPcsDigit, snsCellIdentity.freq, snsCellIdentity.pci, snsCellIdentity.isNrCell));
            this.mLastSetCellId = snsCellIdentity.ci;
            recordCellHdrCount("setCellCount");
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setCellListInfo(this.mDdsPhoneId, arrayList4, 30L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServiceStateChanged(int i, ServiceState serviceState) throws Throwable {
        boolean z;
        long nci;
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || serviceState == null || serviceState.getIsManualSelection() || this.mCellPreferDBHelper == null) {
            return;
        }
        this.mDdsServiceState = serviceState;
        stopMonitorTimerForCheckServiceState();
        int dataRegistrationState = this.mDdsServiceState.getDataRegistrationState();
        log("dataState=" + dataRegistrationState + ", phoneId=" + i);
        int psRegRejectCause = getPsRegRejectCause(this.mDdsServiceState);
        int dataNetworkType = this.mDdsServiceState.getDataNetworkType();
        log("rejectPsCause=" + psRegRejectCause + ", rat=" + dataNetworkType);
        if (dataRegistrationState != 1 || !isNwDenied(psRegRejectCause) || !isLteNrNwType(dataNetworkType)) {
            if (dataRegistrationState != 1 || (dataRegistrationState == 1 && psRegRejectCause != 0)) {
                resetNwDeniedCountInfo();
                return;
            }
            return;
        }
        CellIdentity currentCellIdentity = PhoneFactory.getPhone(i).getCurrentCellIdentity();
        if (currentCellIdentity instanceof CellIdentityLte) {
            nci = ((CellIdentityLte) currentCellIdentity).getCi();
            z = false;
        } else if (currentCellIdentity instanceof CellIdentityNr) {
            nci = ((CellIdentityNr) currentCellIdentity).getNci();
            z = true;
        } else {
            z = false;
            nci = Long.MAX_VALUE;
        }
        logv("curCgi=" + nci + ", isNrType=" + z);
        if (nci == Long.MAX_VALUE || nci == 2147483647L) {
            return;
        }
        if (nci != this.mLastNwDeniedCgi) {
            resetNwDeniedCountInfo();
            this.mLastNwDeniedCgi = nci;
        }
        if (isInNwDeniedTimeLimit()) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        int i2 = this.mNwDeniedCount + 1;
        this.mNwDeniedCount = i2;
        if (i2 == 1) {
            startMonitorTimerCheckServiceState();
        }
        if (this.mNwDeniedCount >= 2 && !isConsecutiveTrigerOpt(nci, jCurrentTimeMillis)) {
            if (this.mCellPreferDBHelper.getCellBarringCountByCgi(nci, z) > 10) {
                setLteNrCellBarringInfo(Long.MAX_VALUE, false);
            } else {
                setLteNrCellBarringInfo(nci, z);
            }
        }
        if (this.mCellPreferDBHelper.isExist(nci, z)) {
            if (!this.mCellPreferDBHelper.updateCellInfoAndFeedbackNeedOpt(nci, z) || isConsecutiveTrigerOpt(nci, jCurrentTimeMillis)) {
                return;
            }
            setLteNrCellBarringInfo(Long.MAX_VALUE, false);
            return;
        }
        recordCellInfoToDb(currentCellIdentity, jCurrentTimeMillis);
    }

    /* JADX WARN: Code duplicated, block: B:9:0x001a  */
    private boolean isConsecutiveTrigerOpt(long j, long j2) {
        boolean z;
        if (this.mLastTriggerOptCgi == j) {
            long j3 = this.mLastTriggerOptCgiTime;
            if (j3 == this.INVALID_VALUE || j2 - j3 > 259200000) {
                z = false;
            } else {
                z = true;
            }
        } else {
            z = false;
        }
        if (!z) {
            this.mLastTriggerOptCgi = j;
            this.mLastTriggerOptCgiTime = j2;
        }
        log("isConsecutiveTrigerOpt rst=" + z);
        return z;
    }

    private void initSnsBandData() {
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

    private void recordCellInfoToDb(CellIdentity cellIdentity, long j) {
        if (cellIdentity == null || this.mCellPreferDBHelper == null) {
            return;
        }
        if (cellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            short s = Short.parseShort(cellIdentityNr.getMccString());
            short s2 = Short.parseShort(cellIdentityNr.getMncString());
            boolean z = cellIdentityNr.getMncString().length() == 3;
            long nci = cellIdentityNr.getNci();
            int nrarfcn = cellIdentityNr.getNrarfcn();
            int operationBandForNrarfcn = Utils.getOperationBandForNrarfcn(nrarfcn);
            logv("band=" + operationBandForNrarfcn);
            Map map = mActiveBand;
            if (map.get("NR5G_BAND_" + operationBandForNrarfcn) == null) {
                log("nr band is null");
                return;
            }
            int iIntValue = ((Integer) map.get("NR5G_BAND_" + operationBandForNrarfcn)).intValue();
            short pci = (short) cellIdentityNr.getPci();
            Integer num = (Integer) mBandMappingScs.get(Integer.valueOf(operationBandForNrarfcn));
            if (num == null) {
                num = 30;
            }
            log("recordCellInfoToDb: nr mcc=" + ((int) s) + ", mnc=" + ((int) s2) + ", mncIncludesPcsDigit=" + z + ", activeBand=" + iIntValue + ", arfcn=" + nrarfcn + ", pci=" + ((int) pci) + ", scs=" + num);
            this.mCellPreferDBHelper.insertCellBarringRecord(new CellPreferDBHelper.CellInfo(s, s2, z, nci, iIntValue, nrarfcn, pci, num.intValue()), true, j);
            return;
        }
        if (cellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
            short s3 = Short.parseShort(cellIdentityLte.getMccString());
            short s4 = Short.parseShort(cellIdentityLte.getMncString());
            boolean z2 = cellIdentityLte.getMncString().length() == 3;
            long ci = cellIdentityLte.getCi();
            int earfcn = cellIdentityLte.getEarfcn();
            int operatingBandForEarfcn = AccessNetworkUtils.getOperatingBandForEarfcn(earfcn);
            logv("band=" + operatingBandForEarfcn);
            Map map2 = mActiveBand;
            if (map2.get("E_UTRA_OPERATING_BAND_" + operatingBandForEarfcn) == null) {
                log("lte band is null");
                return;
            }
            int iIntValue2 = ((Integer) map2.get("E_UTRA_OPERATING_BAND_" + operatingBandForEarfcn)).intValue();
            short pci2 = (short) cellIdentityLte.getPci();
            log("recordCellInfoToDb: lte mcc=" + ((int) s3) + ", mnc=" + ((int) s4) + ", mncIncludesPcsDigit=" + z2 + ", activeBand=" + iIntValue2 + ", arfcn=" + earfcn + ", pci=" + ((int) pci2));
            this.mCellPreferDBHelper.insertCellBarringRecord(new CellPreferDBHelper.CellInfo(s3, s4, z2, ci, iIntValue2, earfcn, pci2), false, j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableCellBarringFeature(int i, boolean z) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("enableCellBarringFeature exception");
            return;
        }
        log("enableCellBarringFeature phonId=" + i + ", isEnable=" + z);
        int i2 = z ? 11 : 12;
        if (SystemClock.elapsedRealtime() <= RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
            if (this.mHandler.hasMessages(i2)) {
                return;
            }
            CellPreferHandler cellPreferHandler = this.mHandler;
            cellPreferHandler.sendMessageDelayed(cellPreferHandler.obtainMessage(i2, i, 0, Boolean.valueOf(z)), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
            log("need delay to enableCellBarringFeature");
            return;
        }
        if (this.mIsDdsChanging) {
            if (this.mHandler.hasMessages(i2)) {
                this.mHandler.removeMessages(i2);
            }
            CellPreferHandler cellPreferHandler2 = this.mHandler;
            cellPreferHandler2.sendMessageDelayed(cellPreferHandler2.obtainMessage(i2, i, 0, Boolean.valueOf(z)), 10000L);
            log("dds is changing, delay to enableCellBarringFeature");
            return;
        }
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().enableCellBarringFeature(i, z, "nwDenied");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLteNrCellBarringInfo(long j, boolean z) throws Throwable {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || this.mCellPreferDBHelper == null) {
            log("setLteNrCellBarringInfo exception");
            return;
        }
        log("setLteNrCellBarringInfo");
        HashMap map = new HashMap();
        map.put(Long.valueOf(j), Boolean.valueOf(z));
        if (SystemClock.elapsedRealtime() <= 90000) {
            if (this.mHandler.hasMessages(10)) {
                return;
            }
            CellPreferHandler cellPreferHandler = this.mHandler;
            cellPreferHandler.sendMessageDelayed(cellPreferHandler.obtainMessage(10, map), 90000L);
            log("need delay to setLteNrCellBarringInfo");
            return;
        }
        if (this.mIsDdsChanging) {
            if (this.mHandler.hasMessages(10)) {
                this.mHandler.removeMessages(10);
            }
            CellPreferHandler cellPreferHandler2 = this.mHandler;
            cellPreferHandler2.sendMessageDelayed(cellPreferHandler2.obtainMessage(10, map), 10000L);
            log("dds is changing, delay to setLteNrCellBarringInfo");
            return;
        }
        ArrayList nrDubiousCellInfoSList = this.mCellPreferDBHelper.getNrDubiousCellInfoSList();
        ArrayList lteDubiousCellInfoSList = this.mCellPreferDBHelper.getLteDubiousCellInfoSList();
        if (j != Long.MAX_VALUE && j != 2147483647L) {
            CellPreferDBHelper cellPreferDBHelper = this.mCellPreferDBHelper;
            if (z) {
                NrDubiousCellInfoS nrDubiousCellInfoS = cellPreferDBHelper.getNrDubiousCellInfoS(j);
                if (nrDubiousCellInfoS != null) {
                    nrDubiousCellInfoSList.add(nrDubiousCellInfoS);
                }
            } else {
                LteDubiousCellInfoS lteDubiousCellInfoS = cellPreferDBHelper.getLteDubiousCellInfoS(j);
                if (lteDubiousCellInfoS != null) {
                    lteDubiousCellInfoSList.add(lteDubiousCellInfoS);
                }
            }
        }
        log("nrSize=" + nrDubiousCellInfoSList.size() + ", lteSize=" + lteDubiousCellInfoSList.size());
        if (lteDubiousCellInfoSList.isEmpty() && nrDubiousCellInfoSList.isEmpty()) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j2 = this.mLastSetLteNrTime;
        int i = 0;
        boolean z2 = true;
        if ((j2 == this.INVALID_VALUE || jCurrentTimeMillis - j2 <= 259200000) && this.mLastSetLteNrPhoneId == this.mDdsPhoneId && this.mNrDubiousCellCgiList.size() == nrDubiousCellInfoSList.size() && this.mLteDubiousCellCgiList.size() == lteDubiousCellInfoSList.size()) {
            int size = nrDubiousCellInfoSList.size();
            boolean z3 = false;
            int i2 = 0;
            while (i2 < size) {
                Object obj = nrDubiousCellInfoSList.get(i2);
                i2++;
                if (!this.mNrDubiousCellCgiList.contains(Long.valueOf(((NrDubiousCellInfoS) obj).cgi))) {
                    z3 = true;
                }
            }
            if (!z3) {
                int size2 = lteDubiousCellInfoSList.size();
                int i3 = 0;
                while (i3 < size2) {
                    Object obj2 = lteDubiousCellInfoSList.get(i3);
                    i3++;
                    if (!this.mLteDubiousCellCgiList.contains(Integer.valueOf(((LteDubiousCellInfoS) obj2).cgi))) {
                        z3 = true;
                    }
                }
            }
            z2 = z3;
        }
        if (!z2) {
            log("not need set");
            return;
        }
        this.mNrDubiousCellCgiList.clear();
        this.mLteDubiousCellCgiList.clear();
        int size3 = nrDubiousCellInfoSList.size();
        int i4 = 0;
        while (i4 < size3) {
            Object obj3 = nrDubiousCellInfoSList.get(i4);
            i4++;
            this.mNrDubiousCellCgiList.add(Long.valueOf(((NrDubiousCellInfoS) obj3).cgi));
        }
        int size4 = lteDubiousCellInfoSList.size();
        while (i < size4) {
            Object obj4 = lteDubiousCellInfoSList.get(i);
            i++;
            this.mLteDubiousCellCgiList.add(Integer.valueOf(((LteDubiousCellInfoS) obj4).cgi));
        }
        this.mLastSetLteNrTime = jCurrentTimeMillis;
        this.mLastSetLteNrPhoneId = this.mDdsPhoneId;
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().setLteNrCellBarringInfo(this.mDdsPhoneId, lteDubiousCellInfoSList, nrDubiousCellInfoSList, "nwDenied");
        }
    }

    private void resetNwDeniedCountInfo() {
        logv("resetNwDeniedCountInfo");
        this.mNwDeniedCount = 0;
        this.mLastNwDeniedTime = this.INVALID_VALUE;
        this.mLastNwDeniedCgi = Long.MAX_VALUE;
    }

    private boolean isInNwDeniedTimeLimit() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mLastNwDeniedTime;
        if (j != this.INVALID_VALUE && jCurrentTimeMillis - j <= MirrorDesktopHelper.TIMEOUT_MILLIS) {
            log("curTime in nw denied time limit");
            return true;
        }
        this.mLastNwDeniedTime = jCurrentTimeMillis;
        return false;
    }

    private boolean isLteNrNwType(int i) {
        boolean z = i == 13 || i == 19 || i == 20 || i == 30;
        if (!z) {
            log("rat=" + i);
        }
        return z;
    }

    private int getPsRegRejectCause(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo;
        if (serviceState == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1)) == null) {
            return -1;
        }
        return networkRegistrationInfo.getRejectCause();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        this.mCellPreferDBHelper = CellPreferDBHelper.getInstance(mContext);
        this.mNetworkModeRecognition = NetworkModeRecognition.getInstance();
        this.mCellScoringEngine = new CellScoringEngine();
        initBandData();
        initSnsBandData();
        initBandMappingScs();
        registerReceiver();
        if (!IS_INDIA) {
            mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("network_mode_state"), false, this.mSubwayStateObserver);
        }
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mElevatorModeController = ElevatorModeController.init(mContext);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSimStateChanged() {
        int insertedSimCount = MiuiPhoneUtils.getInsertedSimCount();
        log("processSimStateChanged cur=" + insertedSimCount);
        if (insertedSimCount > 0) {
            registerTelephonyCallBack();
        } else {
            unregisterTelephonyCallBack();
        }
    }

    private void syncDbToFddMap() {
        if (this.mCellPreferDBHelper == null) {
            return;
        }
        new ArrayList();
        ArrayList allFdd2TddCellInfo = this.mCellPreferDBHelper.getAllFdd2TddCellInfo();
        if (allFdd2TddCellInfo == null || allFdd2TddCellInfo.isEmpty()) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        int size = allFdd2TddCellInfo.size();
        int i = 0;
        while (i < size) {
            Object obj = allFdd2TddCellInfo.get(i);
            i++;
            CellPreferDBHelper.Fdd2TddCellInfo fdd2TddCellInfo = (CellPreferDBHelper.Fdd2TddCellInfo) obj;
            if (fdd2TddCellInfo != null && jCurrentTimeMillis - fdd2TddCellInfo.getLastTime() <= v.a) {
                this.mFdd2TddMap.put(Long.valueOf(fdd2TddCellInfo.getCgi()), fdd2TddCellInfo);
            }
        }
        log("syncDbToFddMap size=" + this.mFdd2TddMap.size());
        if (!this.VDBG || this.mFdd2TddMap.isEmpty()) {
            return;
        }
        Iterator it = this.mFdd2TddMap.entrySet().iterator();
        while (it.hasNext()) {
            log(((CellPreferDBHelper.Fdd2TddCellInfo) ((Map.Entry) it.next()).getValue()).toString());
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.SUBWAY_SIGNAL_MAP_TUNNEL_TO_PLATFORM");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        if (MiuiPhoneUtils.isHstStationCellPreferSupported()) {
            intentFilter.addAction("com.android.phone.intent.action.HST_STATION_STATUS_CHANGED");
        }
        intentFilter.addAction("CellPreferManager.intent.debug");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processReceiveTunnelToPlateformCell(Intent intent) {
        String stringExtra = intent.getStringExtra("cid");
        if (TextUtils.isEmpty(stringExtra) || !this.mIsInSubway) {
            return;
        }
        long j = Long.parseLong(stringExtra);
        log("processReceiveTunnelToPlateformCell cid=" + j);
        recordSubwayFdd2TddCount("tunnelfdd2TddCount");
        if (this.mFdd2TddMap.containsKey(Long.valueOf(j))) {
            recordSubwayFdd2TddCount("tunnelfdd2TddStudyCount");
            CellPreferDBHelper.Fdd2TddCellInfo fdd2TddCellInfo = (CellPreferDBHelper.Fdd2TddCellInfo) this.mFdd2TddMap.get(Long.valueOf(j));
            if (fdd2TddCellInfo != null) {
                int count = fdd2TddCellInfo.getCount();
                int i = this.mDemandMinCount;
                if (count <= i) {
                    fdd2TddCellInfo.setCount(i + 1);
                    fdd2TddCellInfo.setTunnelFlag(1);
                    this.mFdd2TddMap.put(Long.valueOf(j), fdd2TddCellInfo);
                    logv(((CellPreferDBHelper.Fdd2TddCellInfo) this.mFdd2TddMap.get(Long.valueOf(j))).toString());
                }
            }
        }
    }

    private void startMonitorTimerCheckServiceState() {
        log("startMonitorTimerCheckServiceState");
        stopMonitorTimerForCheckServiceState();
        CellPreferHandler cellPreferHandler = this.mHandler;
        cellPreferHandler.sendMessageDelayed(cellPreferHandler.obtainMessage(13, this.mDdsPhoneId, 0, this.mDdsServiceState), 12000L);
    }

    private void stopMonitorTimerForCheckServiceState() {
        if (this.mHandler.hasMessages(13)) {
            this.mHandler.removeMessages(13);
        }
    }

    private void startMonitorTimerForDdsChanged() {
        log("startMonitorTimerForDdsChanged");
        stopMonitorTimerForDdsChanged();
        CellPreferHandler cellPreferHandler = this.mHandler;
        cellPreferHandler.sendMessageDelayed(cellPreferHandler.obtainMessage(7), 10000L);
    }

    private void stopMonitorTimerForDdsChanged() {
        this.mHandler.removeMessages(7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enterDdsChangedMonitorStatus() {
        this.mIsDdsChanging = true;
        startMonitorTimerForDdsChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitDdsChangedMonitorStatus() {
        stopMonitorTimerForDdsChanged();
        this.mIsDdsChanging = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInSubway() {
        if (this.mNetworkModeRecognition == null) {
            return false;
        }
        return this.mNetworkModeRecognition.isSubwayStartStopStateUpdate(Settings.Global.getInt(mContext.getContentResolver(), "network_mode_state", 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSubwayStateChanged() {
        log("processSubwayStateChanged isInSubway=" + this.mIsInSubway);
        if (this.mIsInSubway) {
            recordSubwayFdd2TddCount("enterSubway");
            syncDbToFddMap();
            this.mHandler.obtainMessage(3, this.mDdsPhoneId, 0).sendToTarget();
        } else {
            resetLastFddCellInfo();
            syncFddMapToDb();
            this.mLastSetCoverageInfo.clear();
        }
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            log("registerTelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private void unregisterTelephonyCallBack() {
        if (this.mTelephonyCallBack != null) {
            log("unregisterTelephonyCallBack");
            TelephonyStatAdapter.unregisterCallBack(this.mTelephonyCallBack);
            this.mTelephonyCallBack = null;
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            if (i == CellPreferManager.this.mDdsPhoneId) {
                CellPreferManager.this.mHandler.obtainMessage(9, i, 0, serviceState).sendToTarget();
            }
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            if ((!CellPreferManager.this.mIsInSubway && !CellPreferManager.this.mIsInHSTStation) || i != CellPreferManager.this.mDdsPhoneId || CellPreferManager.this.mIsDdsChanging || cellLocation == null || cellLocation.isEmpty()) {
                return;
            }
            CellPreferManager.this.mHandler.obtainMessage(3, i, 0).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChanged(int i) {
        logv("processCellLocationChanged, phoneId=" + i);
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("invalid phone info");
            return;
        }
        CellIdentity currentCellIdentity = PhoneFactory.getPhone(i).getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            return;
        }
        if (!(currentCellIdentity instanceof CellIdentityNr)) {
            resetLastFddCellInfo();
            return;
        }
        if (isFddCellByPhoneId(i)) {
            long nci = ((CellIdentityNr) currentCellIdentity).getNci();
            if (nci != Long.MAX_VALUE) {
                triggerFdd2TddOptIfNeed(i, nci);
                this.mLastFddGlobalCellId = nci;
                this.mLastIsFddCell = true;
                logv("mLastFddGlobalCellId=" + nci);
                return;
            }
            return;
        }
        if (isTddCellByPhoneId(i)) {
            if (this.mLastIsFddCell) {
                recordSubwayFdd2TddCount("fdd2TddCount");
                startMonitorTimerForCheckSpeed(i);
                return;
            }
            return;
        }
        resetLastFddCellInfo();
    }

    private void startMonitorTimerForCheckSpeed(int i) {
        log("startMonitorTimerForCheckSpeed");
        stopMonitorTimerForCheckSpeed();
        CellPreferHandler cellPreferHandler = this.mHandler;
        cellPreferHandler.sendMessageDelayed(cellPreferHandler.obtainMessage(6, Integer.valueOf(i)), MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    private void stopMonitorTimerForCheckSpeed() {
        if (this.mHandler.hasMessages(6)) {
            logv("stopMonitorTimerForCheckSpeed");
            this.mHandler.removeMessages(6);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCheckNetworkSpeed(int i) {
        NetworkSpeedTracker networkSpeedTracker;
        log("processCheckNetworkSpeed");
        if (isTddCellByPhoneId(i) && this.mLastIsFddCell && this.mIsInSubway && (networkSpeedTracker = NetworkSpeedTracker.getInstance()) != null) {
            if (networkSpeedTracker.isDataHighTputByNetworkSpeed()) {
                long jLongValue = Long.MAX_VALUE;
                if (this.mLastFddGlobalCellId == Long.MAX_VALUE) {
                    log("mLastFddGlobalCellId is invalid");
                    return;
                }
                recordSubwayFdd2TddCount("fdd2TddStudyCount");
                long jCurrentTimeMillis = System.currentTimeMillis();
                boolean zContainsKey = this.mFdd2TddMap.containsKey(Long.valueOf(this.mLastFddGlobalCellId));
                Map map = this.mFdd2TddMap;
                if (zContainsKey) {
                    CellPreferDBHelper.Fdd2TddCellInfo fdd2TddCellInfo = (CellPreferDBHelper.Fdd2TddCellInfo) map.get(Long.valueOf(this.mLastFddGlobalCellId));
                    fdd2TddCellInfo.setCount(fdd2TddCellInfo.getCount() + 1);
                    fdd2TddCellInfo.setLastTime(jCurrentTimeMillis);
                    this.mFdd2TddMap.put(Long.valueOf(this.mLastFddGlobalCellId), fdd2TddCellInfo);
                    log("update mFdd2TddMap size=" + this.mFdd2TddMap.size());
                    logv(((CellPreferDBHelper.Fdd2TddCellInfo) this.mFdd2TddMap.get(Long.valueOf(this.mLastFddGlobalCellId))).toString());
                } else {
                    if (map.size() >= 50) {
                        long lastTime = Long.MAX_VALUE;
                        for (Map.Entry entry : this.mFdd2TddMap.entrySet()) {
                            if (((CellPreferDBHelper.Fdd2TddCellInfo) entry.getValue()).getLastTime() < lastTime) {
                                lastTime = ((CellPreferDBHelper.Fdd2TddCellInfo) entry.getValue()).getLastTime();
                                jLongValue = ((Long) entry.getKey()).longValue();
                            }
                        }
                        this.mFdd2TddMap.remove(Long.valueOf(jLongValue));
                        logv("remove cgi=" + jLongValue);
                    }
                    this.mFdd2TddMap.put(Long.valueOf(this.mLastFddGlobalCellId), new CellPreferDBHelper.Fdd2TddCellInfo(this.mLastFddGlobalCellId, 1, jCurrentTimeMillis));
                    log("insert mFdd2TddMap size=" + this.mFdd2TddMap.size());
                    logv(((CellPreferDBHelper.Fdd2TddCellInfo) this.mFdd2TddMap.get(Long.valueOf(this.mLastFddGlobalCellId))).toString());
                }
            }
            resetLastFddCellInfo();
        }
    }

    private void syncFddMapToDb() {
        if (this.mCellPreferDBHelper == null || this.mFdd2TddMap.isEmpty()) {
            return;
        }
        this.mCellPreferDBHelper.saveFddInfoToDb(this.mFdd2TddMap);
        log("syncFddMapToDb size=" + this.mFdd2TddMap.size());
        if (this.VDBG) {
            Iterator it = this.mFdd2TddMap.entrySet().iterator();
            while (it.hasNext()) {
                log(((CellPreferDBHelper.Fdd2TddCellInfo) ((Map.Entry) it.next()).getValue()).toString());
            }
        }
        this.mFdd2TddMap.clear();
    }

    private void triggerFdd2TddOptIfNeed(int i, long j) {
        String str;
        if (this.mFdd2TddMap.containsKey(Long.valueOf(j))) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            boolean zIsInSetCoverageTimeLimit = isInSetCoverageTimeLimit(jCurrentTimeMillis, j);
            boolean z = ((CellPreferDBHelper.Fdd2TddCellInfo) this.mFdd2TddMap.get(Long.valueOf(j))).getLastTime() >= jCurrentTimeMillis - v.a;
            boolean z2 = ((CellPreferDBHelper.Fdd2TddCellInfo) this.mFdd2TddMap.get(Long.valueOf(j))).getCount() > this.mDemandMinCount;
            log("triggerFdd2TddOptIfNeed isInTimeLimit=" + zIsInSetCoverageTimeLimit + ", isCellTimeSatify=" + z + ", isCountSatify=" + z2);
            if (!zIsInSetCoverageTimeLimit && z && z2 && isSatifyCommonScene(i)) {
                if (this.mHandler.hasMessages(4)) {
                    this.mHandler.removeMessages(4);
                }
                if (((CellPreferDBHelper.Fdd2TddCellInfo) this.mFdd2TddMap.get(Long.valueOf(j))).getTunnelFlag() == 1) {
                    str = "fdd2tddSubwayTunnel";
                } else {
                    str = "fdd2tddSubway";
                }
                setCoverageState(i, 1, str);
                CellPreferHandler cellPreferHandler = this.mHandler;
                cellPreferHandler.sendMessageDelayed(cellPreferHandler.obtainMessage(4, i, 0, str), 2000L);
                this.mLastSetCoverageInfo.put(Long.valueOf(j), Long.valueOf(jCurrentTimeMillis));
            }
        }
    }

    private boolean isInSetCoverageTimeLimit(long j, long j2) {
        return this.mLastSetCoverageInfo.containsKey(Long.valueOf(j2)) && j - ((Long) this.mLastSetCoverageInfo.get(Long.valueOf(j2))).longValue() < 1800000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCoverageState(int i, int i2, String str) {
        if (IS_INDIA) {
            log("setCoverageState not support india");
        } else if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            this.mCurCoverageStatus = i2;
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setCoverageState(i, i2, str);
            }
        }
    }

    private boolean isSatifyCommonScene(int i) {
        boolean z = (!MiuiPhoneUtils.isSimReady(i) || MiuiPhoneUtils.isAirPlaneMode() || MiuiPhoneUtils.isDuringCall() || !MiuiPhoneUtils.isMobileDataConnected(i) || MiuiPhoneUtils.isWifiConnected() || !MiuiPhoneUtils.isLteNrNwMode(i) || MiuiPhoneUtils.isManualSelection(i)) ? false : true;
        log("isSatifyCommonScene rst=" + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetLastFddCellInfo() {
        logv("resetLastFddCellInfo");
        this.mLastIsFddCell = false;
        this.mLastFddGlobalCellId = Long.MAX_VALUE;
    }

    private void initBandData() {
        String[] stringArray = mContext.getResources().getStringArray(R.array.band_fdd);
        if (stringArray != null && stringArray.length > 0) {
            for (String str : stringArray) {
                this.mFddSet.add(str);
            }
        }
        String[] stringArray2 = mContext.getResources().getStringArray(R.array.band_tdd);
        if (stringArray2 != null && stringArray2.length > 0) {
            for (String str2 : stringArray2) {
                this.mTddSet.add(str2);
            }
        }
        log("initBandData");
    }

    private boolean isFddCellByPhoneId(int i) {
        return !this.mFddSet.isEmpty() && this.mFddSet.contains(getBandByPhoneId(i));
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

    private CellScoringEngine.SnsCellIdentity getCellIdentityByPhoneId(int i) {
        boolean z;
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            return null;
        }
        CellIdentity currentCellIdentity = PhoneFactory.getPhone(i).getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            log("invalid phone info");
            return null;
        }
        if (currentCellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
            String mccString = cellIdentityNr.getMccString();
            String mncString = cellIdentityNr.getMncString();
            if (TextUtils.isEmpty(mccString) || TextUtils.isEmpty(mncString)) {
                log("invalid nr ci");
                return null;
            }
            short s = Short.parseShort(mccString);
            short s2 = Short.parseShort(mncString);
            z = mncString.length() == 3;
            long nci = cellIdentityNr.getNci();
            int nrarfcn = cellIdentityNr.getNrarfcn();
            int operationBandForNrarfcn = Utils.getOperationBandForNrarfcn(nrarfcn);
            logv("band=" + operationBandForNrarfcn);
            Map map = mActiveBand;
            if (map.get("NR5G_BAND_" + operationBandForNrarfcn) == null) {
                log("nr band is null");
                return null;
            }
            int iIntValue = ((Integer) map.get("NR5G_BAND_" + operationBandForNrarfcn)).intValue();
            short pci = (short) cellIdentityNr.getPci();
            log("getCellIdentityByPhoneId: nr mcc=" + ((int) s) + ", mnc=" + ((int) s2) + ", mncIncludesPcsDigit=" + z + ", activeBand=" + iIntValue + ", arfcn=" + nrarfcn + ", pci=" + ((int) pci));
            return new CellScoringEngine.SnsCellIdentity(s, s2, z, nci, iIntValue, nrarfcn, pci, true);
        }
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            String mccString2 = cellIdentityLte.getMccString();
            String mncString2 = cellIdentityLte.getMncString();
            if (TextUtils.isEmpty(mccString2) || TextUtils.isEmpty(mncString2)) {
                log("invalid lte ci");
                return null;
            }
            short s3 = Short.parseShort(mccString2);
            short s4 = Short.parseShort(mncString2);
            z = mncString2.length() == 3;
            long ci = cellIdentityLte.getCi();
            int earfcn = cellIdentityLte.getEarfcn();
            int operatingBandForEarfcn = AccessNetworkUtils.getOperatingBandForEarfcn(earfcn);
            logv("band=" + operatingBandForEarfcn);
            Map map2 = mActiveBand;
            if (map2.get("E_UTRA_OPERATING_BAND_" + operatingBandForEarfcn) == null) {
                log("lte band is null");
                return null;
            }
            int iIntValue2 = ((Integer) map2.get("E_UTRA_OPERATING_BAND_" + operatingBandForEarfcn)).intValue();
            short pci2 = (short) cellIdentityLte.getPci();
            log("getCellIdentityByPhoneId: lte mcc=" + ((int) s3) + ", mnc=" + ((int) s4) + ", mncIncludesPcsDigit=" + z + ", activeBand=" + iIntValue2 + ", arfcn=" + earfcn + ", pci=" + ((int) pci2));
            return new CellScoringEngine.SnsCellIdentity(s3, s4, z, ci, iIntValue2, earfcn, pci2, false);
        }
        log("ingore non 4/5G cell");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChangedForHSTStation(int i) {
        logv("processCellLocationChangedForHSTStation, phoneId=" + i);
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || !this.mIsInRealTimeGame) {
            log("invalid phone info or not in game");
            return;
        }
        CellScoringEngine.SnsCellIdentity cellIdentityByPhoneId = getCellIdentityByPhoneId(i);
        if (cellIdentityByPhoneId == null) {
            log("snsCellIdentity is null");
        }
        this.mCellScoringEngine.switchCell(cellIdentityByPhoneId);
    }

    private void registerGameInfoCallBack() {
        NetworkMetricsController networkMetricsController = NetworkMetricsController.getInstance();
        if (this.mGameInfoCallBack != null || networkMetricsController == null) {
            return;
        }
        log("registerGameInfoCallBack");
        GameInfoCallBack gameInfoCallBack = new GameInfoCallBack();
        this.mGameInfoCallBack = gameInfoCallBack;
        networkMetricsController.registerGameDelayListener(gameInfoCallBack);
    }

    private void unregisterGameInfoCallBack() {
        NetworkMetricsController networkMetricsController = NetworkMetricsController.getInstance();
        if (this.mGameInfoCallBack == null || networkMetricsController == null) {
            return;
        }
        log("unregisterGameInfoCallBack");
        networkMetricsController.unregisterGameDelayListener(this.mGameInfoCallBack);
        this.mGameInfoCallBack = null;
    }

    class GameInfoCallBack implements NetworkMetricsController.GameDelayListener {
        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameUpdateStatusChanged(boolean z) {
        }

        GameInfoCallBack() {
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameDelayChanged(int i) {
            CellPreferManager.this.mHandler.obtainMessage(17, Integer.valueOf(i)).sendToTarget();
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameStatusChanged(int i, String str) {
            CellPreferManager.this.mHandler.obtainMessage(18, i, 0, str).sendToTarget();
        }

        @Override // com.android.phone.NetworkMetricsController.GameDelayListener
        public void onGameBattleStatusChanged(boolean z) {
            CellPreferManager.this.mHandler.obtainMessage(19, Boolean.valueOf(z)).sendToTarget();
        }
    }

    class CellScoringEngine {
        private static final Map LATENCY_RANGES = Map.of("com.tencent.tmgp.sgame", List.of(new LatencyRange(0, 100, 100), new LatencyRange(100, 200, 50), new LatencyRange(200, MiRilHook.HOOK_ID_START_URC_BY_AT, -50), new LatencyRange(MiRilHook.HOOK_ID_START_URC_BY_AT, 460, -300), new LatencyRange(460, Integer.MAX_VALUE, -1000)), "com.tencent.lolm", List.of(new LatencyRange(0, 80, 100), new LatencyRange(80, 200, 50), new LatencyRange(200, MiRilHook.HOOK_ID_START_URC_BY_AT, -50), new LatencyRange(MiRilHook.HOOK_ID_START_URC_BY_AT, 999, -300), new LatencyRange(999, Integer.MAX_VALUE, -1000)), "com.tencent.jkchess", List.of(new LatencyRange(0, 100, 100), new LatencyRange(100, MiRilHook.HOOK_ID_START_URC_BY_AT, 50), new LatencyRange(MiRilHook.HOOK_ID_START_URC_BY_AT, ResponseObject.RELAY_FAILED_CODE, -50), new LatencyRange(ResponseObject.RELAY_FAILED_CODE, 999, -300), new LatencyRange(999, Integer.MAX_VALUE, -1000)));
        private final Map mCellDataMap = new ConcurrentHashMap();
        private SnsCellIdentity mCurrentCell;
        private String mCurrentGameName;
        private long mGameStartTime;
        private boolean mIsAllowSample;
        private boolean mIsRunning;
        private ScheduledExecutorService mScheduler;

        CellScoringEngine() {
        }

        public enum GameStopReason {
            DUMMY(0),
            GAME_BATTLE_END(1),
            EXIT_GAME(2),
            EXIT_HST_STATION(3),
            WIFI_CONNECTED(4);

            private int value;

            GameStopReason(int i) {
                this.value = i;
            }
        }

        public class SnsCellIdentity {
            public final int band;
            public final long ci;
            public final int freq;
            public final boolean isNrCell;
            public final short mcc;
            public final short mnc;
            public final boolean mncIncludesPcsDigit;
            public final short pci;

            public SnsCellIdentity(short s, short s2, boolean z, long j, int i, int i2, short s3, boolean z2) {
                this.mcc = s;
                this.mnc = s2;
                this.mncIncludesPcsDigit = z;
                this.ci = j;
                this.band = i;
                this.freq = i2;
                this.pci = s3;
                this.isNrCell = z2;
            }

            public boolean equals(Object obj) {
                return (obj instanceof SnsCellIdentity) && this.ci == ((SnsCellIdentity) obj).ci;
            }

            public int hashCode() {
                return Objects.hash(Long.valueOf(this.ci));
            }

            public String toString() {
                return "SnsCellIdentity{mcc=" + ((int) this.mcc) + ", mnc=" + ((int) this.mnc) + ", mncIncludesPcsDigit=" + this.mncIncludesPcsDigit + ", ci=" + this.ci + ", band=" + this.band + ", freq=" + this.freq + ", pci=" + ((int) this.pci) + ", isNrCell=" + this.isNrCell + "}";
            }
        }

        public void setAllowSample(boolean z) {
            CellPreferManager.this.log("setAllowSample isAllow=" + z);
            this.mIsAllowSample = z;
        }

        class CellData {
            long activeStartTime;
            long durationMs;
            List latencyScores;
            List latencys;

            private CellData() {
                this.latencys = new CopyOnWriteArrayList();
                this.latencyScores = new CopyOnWriteArrayList();
                this.activeStartTime = -1L;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public boolean meetsMinDuration() {
                return this.durationMs >= 10000;
            }

            public String toString() {
                return "CellData{latencys=" + this.latencys + ", latencyScores=" + this.latencyScores + ", durationMs=" + this.durationMs + ", activeStartTime=" + this.activeStartTime + "}";
            }
        }

        class LatencyRange {
            final int max;
            final int min;
            final int score;

            LatencyRange(int i, int i2, int i3) {
                this.min = i;
                this.max = i2;
                this.score = i3;
            }
        }

        public synchronized void startGame(String str) {
            CellPreferManager.this.log("startGame gameName=" + str);
            if (this.mIsRunning) {
                return;
            }
            reset();
            this.mCurrentGameName = str;
            this.mIsRunning = true;
            this.mGameStartTime = System.currentTimeMillis();
            ScheduledExecutorService scheduledExecutorServiceNewScheduledThreadPool = Executors.newScheduledThreadPool(1);
            this.mScheduler = scheduledExecutorServiceNewScheduledThreadPool;
            scheduledExecutorServiceNewScheduledThreadPool.scheduleAtFixedRate(new Runnable() { // from class: com.android.phone.CellPreferManager$CellScoringEngine$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.sampleLatency();
                }
            }, 0L, 2000L, TimeUnit.MILLISECONDS);
        }

        public synchronized List stopGame(GameStopReason gameStopReason) {
            if (!this.mIsRunning) {
                return null;
            }
            CellPreferManager.this.log("stopGame reason=" + gameStopReason);
            forceStop();
            if (gameStopReason != GameStopReason.GAME_BATTLE_END) {
                return null;
            }
            return calculateResults();
        }

        public synchronized void switchCell(SnsCellIdentity snsCellIdentity) {
            try {
                CellPreferManager.this.log("switchCell newCell=" + snsCellIdentity);
                if (this.mIsRunning && !Objects.equals(this.mCurrentCell, snsCellIdentity)) {
                    SnsCellIdentity snsCellIdentity2 = this.mCurrentCell;
                    if (snsCellIdentity2 != null) {
                        updateCellDuration(snsCellIdentity2);
                    }
                    this.mCurrentCell = snsCellIdentity;
                    if (snsCellIdentity != null) {
                        ((CellData) this.mCellDataMap.computeIfAbsent(snsCellIdentity, new Function() { // from class: com.android.phone.CellPreferManager$CellScoringEngine$$ExternalSyntheticLambda0
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return CellPreferManager.CellScoringEngine.m311$r8$lambda$fPfzetMBwZnHY5UWkN_LlCYobA((CellPreferManager.CellScoringEngine.SnsCellIdentity) obj);
                            }
                        })).activeStartTime = System.currentTimeMillis();
                        if (CellPreferManager.this.mLastSetCellId != CellPreferManager.this.INVALID_VALUE && CellPreferManager.this.mLastSetCellId == snsCellIdentity.ci) {
                            CellPreferManager.this.recordCellHdrCount("setCellEffectCount");
                            CellPreferManager cellPreferManager = CellPreferManager.this;
                            cellPreferManager.mLastSetCellId = cellPreferManager.INVALID_VALUE;
                        }
                    }
                    return;
                }
                CellPreferManager.this.log("game is not running");
            } catch (Throwable th) {
                throw th;
            }
        }

        /* JADX INFO: renamed from: $r8$lambda$fPfzetMBwZnHY5UWkN_LlCYo-bA, reason: not valid java name */
        public static /* synthetic */ CellData m311$r8$lambda$fPfzetMBwZnHY5UWkN_LlCYobA(SnsCellIdentity snsCellIdentity) {
            return new CellData();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sampleLatency() {
            CellPreferManager.this.log("sampleLatency");
            if (this.mCurrentCell == null || !this.mIsAllowSample) {
                CellPreferManager.this.log("mIsAllowSample=" + this.mIsAllowSample);
                return;
            }
            final int i = CellPreferManager.this.mCurrentGameDelay;
            final int iCalculateLatencyScore = calculateLatencyScore(i, this.mCurrentGameName);
            this.mCellDataMap.compute(this.mCurrentCell, new BiFunction() { // from class: com.android.phone.CellPreferManager$CellScoringEngine$$ExternalSyntheticLambda5
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return CellPreferManager.CellScoringEngine.$r8$lambda$5Ysur0S31NqLNi3wkPRZ1tscLe4(i, iCalculateLatencyScore, (CellPreferManager.CellScoringEngine.SnsCellIdentity) obj, (CellPreferManager.CellScoringEngine.CellData) obj2);
                }
            });
        }

        public static /* synthetic */ CellData $r8$lambda$5Ysur0S31NqLNi3wkPRZ1tscLe4(int i, int i2, SnsCellIdentity snsCellIdentity, CellData cellData) {
            cellData.latencys.add(Integer.valueOf(i));
            cellData.latencyScores.add(Integer.valueOf(i2));
            return cellData;
        }

        private int calculateLatencyScore(int i, String str) {
            for (LatencyRange latencyRange : (List) LATENCY_RANGES.get(str)) {
                if (i >= latencyRange.min && i < latencyRange.max) {
                    return latencyRange.score;
                }
            }
            return 0;
        }

        private void updateCellDuration(SnsCellIdentity snsCellIdentity) {
            CellData cellData = (CellData) this.mCellDataMap.get(snsCellIdentity);
            if (cellData == null || cellData.activeStartTime <= 0) {
                return;
            }
            long jCurrentTimeMillis = System.currentTimeMillis() - cellData.activeStartTime;
            cellData.durationMs += jCurrentTimeMillis;
            CellPreferManager.this.log("updateCellDuration ci=" + snsCellIdentity.ci + ", duration=" + jCurrentTimeMillis + ", total=" + cellData.durationMs);
            cellData.activeStartTime = -1L;
        }

        private List calculateResults() {
            final long jCurrentTimeMillis = System.currentTimeMillis() - this.mGameStartTime;
            CellPreferManager.this.log("calculateResults totalDuration=" + jCurrentTimeMillis + "ms");
            if (CellPreferManager.this.VDBG) {
                for (Map.Entry entry : this.mCellDataMap.entrySet()) {
                    CellPreferManager.this.log("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }
            }
            return (List) this.mCellDataMap.entrySet().stream().filter(new Predicate() { // from class: com.android.phone.CellPreferManager$CellScoringEngine$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((CellPreferManager.CellScoringEngine.CellData) ((Map.Entry) obj).getValue()).meetsMinDuration();
                }
            }).map(new Function() { // from class: com.android.phone.CellPreferManager$CellScoringEngine$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return CellPreferManager.CellScoringEngine.m310$r8$lambda$XfWSSp_mqByTxDh_4vMwa7Z4wA(jCurrentTimeMillis, (Map.Entry) obj);
                }
            }).sorted(Comparator.comparingDouble(new ToDoubleFunction() { // from class: com.android.phone.CellPreferManager$CellScoringEngine$$ExternalSyntheticLambda4
                @Override // java.util.function.ToDoubleFunction
                public final double applyAsDouble(Object obj) {
                    return ((CellPreferManager.CellScoringEngine.ScoringResult) obj).getScore();
                }
            }).reversed()).collect(Collectors.toList());
        }

        /* JADX INFO: renamed from: $r8$lambda$XfWSSp_mqByTxD-h_4vMwa7Z4wA, reason: not valid java name */
        public static /* synthetic */ ScoringResult m310$r8$lambda$XfWSSp_mqByTxDh_4vMwa7Z4wA(long j, Map.Entry entry) {
            SnsCellIdentity snsCellIdentity = (SnsCellIdentity) entry.getKey();
            CellData cellData = (CellData) entry.getValue();
            return new ScoringResult(snsCellIdentity, (cellData.latencyScores.stream().mapToDouble(new ToDoubleFunction() { // from class: com.android.phone.CellPreferManager$CellScoringEngine$$ExternalSyntheticLambda6
                @Override // java.util.function.ToDoubleFunction
                public final double applyAsDouble(Object obj) {
                    return ((Integer) obj).intValue();
                }
            }).average().orElse(0.0d) * 0.8d) + ((cellData.durationMs / j) * 100.0d * 0.2d));
        }

        private void forceStop() {
            CellPreferManager.this.log("forceStop");
            ScheduledExecutorService scheduledExecutorService = this.mScheduler;
            if (scheduledExecutorService != null) {
                scheduledExecutorService.shutdownNow();
            }
            SnsCellIdentity snsCellIdentity = this.mCurrentCell;
            if (snsCellIdentity != null) {
                updateCellDuration(snsCellIdentity);
            }
            this.mIsRunning = false;
            CellPreferManager cellPreferManager = CellPreferManager.this;
            cellPreferManager.mLastSetCellId = cellPreferManager.INVALID_VALUE;
        }

        private void reset() {
            CellPreferManager.this.log("reset");
            this.mCellDataMap.clear();
            this.mCurrentCell = null;
            this.mGameStartTime = 0L;
        }

        public class ScoringResult {
            private final SnsCellIdentity cellIdentity;
            private final double score;

            public ScoringResult(SnsCellIdentity snsCellIdentity, double d) {
                this.cellIdentity = snsCellIdentity;
                this.score = Math.min(100.0d, Math.max(0.0d, d));
            }

            public double getScore() {
                return this.score;
            }

            public SnsCellIdentity getCellIdentity() {
                return this.cellIdentity;
            }

            public String toString() {
                return "ScoringResult{cellIdentity=" + this.cellIdentity + ", score=" + String.format("%.4f", Double.valueOf(this.score)) + "}";
            }
        }
    }

    private void recordSubwayFdd2TddCount(String str) {
        Map map = this.mSubwayFdd2TddStats;
        map.put(str, Integer.valueOf(((Integer) map.getOrDefault(str, 0)).intValue() + 1));
    }

    public void reportSubwayFdd2TddStatistic() {
        if (this.mSubwayFdd2TddStats.isEmpty()) {
            return;
        }
        HashMap map = new HashMap(this.mSubwayFdd2TddStats);
        logv("reportSubwayFdd2TddStatistic data=" + this.mSubwayFdd2TddStats);
        this.mSubwayFdd2TddStats.clear();
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_subway_fdd2tdd_statistic", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordCellHdrCount(String str) {
        Map map = this.mCellHdrStats;
        map.put(str, Integer.valueOf(((Integer) map.getOrDefault(str, 0)).intValue() + 1));
    }

    public void reportCellHdrStatistic() {
        if (this.mCellHdrStats.isEmpty()) {
            return;
        }
        HashMap map = new HashMap(this.mCellHdrStats);
        logv("reportCellHdrStatistic data=" + this.mCellHdrStats);
        this.mCellHdrStats.clear();
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_hst_station_cellhdr_statistic", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("CellPreferManager", str);
    }

    private void logv(String str) {
        if (this.VDBG) {
            Log.d("CellPreferManager", str);
        }
    }

    private void localLog(String str, String str2) {
        NetworkMetricsController.localLog("CellPreferManager", str, str2);
    }
}
