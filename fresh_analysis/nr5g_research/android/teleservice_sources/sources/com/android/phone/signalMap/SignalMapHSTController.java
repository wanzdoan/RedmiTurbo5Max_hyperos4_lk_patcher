package com.android.phone.signalMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.util.ArrayUtils;
import com.android.phone.CloudCellDbHelper;
import com.android.phone.ElevatorModeController;
import com.android.phone.FiveGManagerBase;
import com.android.phone.HighPriorityCellSelection;
import com.android.phone.HstFrequencyConfig;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemDataCollectorBase;
import com.android.phone.NetworkMetricsController;
import com.android.phone.NetworkRecoveryStrategyBase;
import com.android.phone.PhoneGlobals;
import com.android.phone.statistics.FileUploadStats;
import com.android.phone.statistics.HighSpeedTrainStats;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.VideoPreloadScene;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.google.common.hash.Hashing;
import com.ot.pubsub.a.a;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.TelephonyStatAdapter;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class SignalMapHSTController {
    public static boolean DEBUG = false;
    private static volatile SignalMapHSTController mSignalMapHSTController;
    private volatile Map HST_LTE_FREQ_MAP;
    private volatile Map HST_NR_FREQ_MAP;
    private volatile BloomFilter mBloomFilter;
    private Context mContext;
    private int mCurCoverageStatus;
    private int[] mCurrentFreq;
    private volatile boolean mCurrentHstState;
    private String mCurrentPlmn;
    private ElevatorModeController mElevatorModeController;
    private Set mEnterTunnelForSmartlinkCell;
    private Set mExitTunnelSmartlinkCell;
    public SignalMapHSTHandler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    private final List mInvalidReferFreqDurations;
    private boolean[] mIsCurrentCellLte;
    private boolean mIsEnableSmartlink;
    private volatile boolean mIsHstPreferFreqCloudOn;
    private boolean mIsHstTunnelSmartlinkCloudOn;
    private boolean[] mIsOos;
    private volatile boolean mIsScreenOn;
    private boolean[] mIsUserOos;
    private boolean mIsWechatUlEnhanceCallbackStarted;
    private volatile long mLastDisableSmartlinkTime;
    private long mLastDnsPrefetchTime;
    private volatile long mLastEnableSmartlinkTime;
    private long mLastPreloadTime;
    private int mLastSceneTypeFileUpload;
    private volatile int mLastSmartLinkScene;
    private int mOosCount;
    private int mOosFreqInConfigCount;
    private long[] mOosStartTime;
    private long mOosTotalDuration;
    private PowerManager mPowerManager;
    private Set mPreferFreqLines;
    private Map mRouteCidsMap;
    private ServiceState[] mServiceState;
    private SignalMapHSTCloudController mSignalMapHSTCloudController;
    private final Map mStats;
    private int mUserOosCount;
    private final List mUserOosDurations;
    private final List mValidReferFreqDurations;
    private HandlerThread mWorkThread;
    private static final HashMap sLocalLogs = new HashMap();
    private static final String[] SMARTLINK_CELL_LINE = {"G21", "G79"};
    private static final boolean IS_QCOM = MiuiTelephony.IS_QCOM;
    private static final String[] HST_OPERATORS = {"CM", "CU", "CT", "GM_DT", "SP_VDF", "SP_MAS", "FR_MAS"};
    private Map mLineDirections = new HashMap();
    private String mCurrentLines = "";
    private String mDdsOperator = "";
    private boolean mHstTunnelState = false;
    private Set mEnterTunnelCell = new HashSet();
    private Set mExitTunnelCell = new HashSet();
    private HashMap mDubiousCell = new HashMap();
    private Map mPreferCell = new HashMap();
    private Set mFdd2TddCells = new HashSet();
    private long mLastCid = -1;

    private SignalMapHSTController() {
        this.mPowerManager = null;
        int i = MiuiTelephony.PHONE_COUNT;
        this.mServiceState = new ServiceState[i];
        this.mIsOos = new boolean[i];
        this.mCurrentFreq = new int[i];
        this.mOosCount = 0;
        this.mOosTotalDuration = 0L;
        this.mOosStartTime = new long[i];
        this.mOosFreqInConfigCount = 0;
        this.mIsCurrentCellLte = new boolean[i];
        this.mValidReferFreqDurations = new ArrayList();
        this.mInvalidReferFreqDurations = new ArrayList();
        this.mUserOosDurations = new ArrayList();
        this.mUserOosCount = 0;
        this.mIsUserOos = new boolean[i];
        this.mCurrentPlmn = "";
        this.mCurrentHstState = false;
        this.mRouteCidsMap = new ConcurrentHashMap();
        this.mElevatorModeController = null;
        this.mCurCoverageStatus = -1;
        this.mLastPreloadTime = 0L;
        this.mLastDnsPrefetchTime = 0L;
        this.mStats = new ConcurrentHashMap();
        this.mIsHstTunnelSmartlinkCloudOn = false;
        this.mIsEnableSmartlink = false;
        this.mEnterTunnelForSmartlinkCell = new HashSet();
        this.mExitTunnelSmartlinkCell = new HashSet();
        this.mLastDisableSmartlinkTime = 0L;
        this.mLastEnableSmartlinkTime = 0L;
        this.mLastSceneTypeFileUpload = -1;
        this.mIsWechatUlEnhanceCallbackStarted = false;
        this.mLastSmartLinkScene = -1;
        this.mPreferFreqLines = null;
        this.HST_LTE_FREQ_MAP = new HashMap();
        this.HST_NR_FREQ_MAP = new HashMap();
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.signalMap.SignalMapHSTController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                SignalMapHSTController.this.log("receive action".concat(action));
                if ("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED".equals(action)) {
                    SignalMapHSTController.this.mHandler.obtainMessage(4, intent).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED")) {
                    SignalMapHSTController.this.mHandler.obtainMessage(5, intent).sendToTarget();
                    return;
                }
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    SignalMapHSTController.this.mHandler.obtainMessage(12).sendToTarget();
                    return;
                }
                if ("android.intent.action.SCREEN_ON".equals(action)) {
                    SignalMapHSTController.this.mIsScreenOn = true;
                    SignalMapHSTController.this.mHandler.obtainMessage(7).sendToTarget();
                    return;
                }
                if ("android.intent.action.SCREEN_OFF".equals(action)) {
                    SignalMapHSTController.this.mIsScreenOn = false;
                    SignalMapHSTController.this.mHandler.obtainMessage(7).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.FILE_UPLOAD_SCENE")) {
                    SignalMapHSTController.this.mHandler.obtainMessage(13, intent).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.HST_SIGNAL_MAP_ACTION_DEBUG_ALL")) {
                    SignalMapHSTController.this.mHandler.obtainMessage(100, intent).sendToTarget();
                    return;
                }
                if (action.equals("com.android.phone.intent.action.CLOUD_HST_PREFER_FREQ_SWITCH")) {
                    SignalMapHSTController signalMapHSTController = SignalMapHSTController.this;
                    signalMapHSTController.mIsHstPreferFreqCloudOn = signalMapHSTController.getHstPreferFreqSwitch();
                    SignalMapHSTController.this.logD("mIsHstPreferFreqCloudOn = " + SignalMapHSTController.this.mIsHstPreferFreqCloudOn);
                    return;
                }
                if (action.equals("com.android.phone.intent.action.ACTION_CLOUD_HST_TUNNEL_SMARTLINK_DONE")) {
                    SignalMapHSTController.this.mHandler.sendEmptyMessage(14);
                }
            }
        };
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("SignalMapHSTController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        SignalMapHSTHandler signalMapHSTHandler = new SignalMapHSTHandler(this.mWorkThread.getLooper());
        this.mHandler = signalMapHSTHandler;
        signalMapHSTHandler.sendEmptyMessageDelayed(1, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mElevatorModeController = ElevatorModeController.init(this.mContext);
        }
        this.mPowerManager = (PowerManager) this.mContext.getSystemService("power");
        this.mCurrentHstState = isCurrentHstState();
        this.mIsScreenOn = isScreenOn();
        this.mIsHstTunnelSmartlinkCloudOn = getHstTunnelSmartlinkCloud();
        this.mIsHstPreferFreqCloudOn = getHstPreferFreqSwitch();
        this.mSignalMapHSTCloudController = SignalMapHSTCloudController.getInstance();
        registerBroadcastReceiver();
        registerTelephonyCallBack();
        this.mServiceState = new ServiceState[i];
        Arrays.fill(this.mCurrentFreq, -1);
    }

    public static SignalMapHSTController getInstance() {
        synchronized (SignalMapHSTController.class) {
            try {
                if (mSignalMapHSTController == null) {
                    mSignalMapHSTController = new SignalMapHSTController();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return mSignalMapHSTController;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        this.mDdsOperator = operatorForDds();
        rebuildFreqMaps();
    }

    private void rebuildFreqMaps() {
        try {
            Set preferFreqLines = getPreferFreqLines();
            if (preferFreqLines.isEmpty()) {
                return;
            }
            String[] strArr = HST_OPERATORS;
            this.HST_LTE_FREQ_MAP = buildFreqMap(preferFreqLines, strArr, true);
            this.HST_NR_FREQ_MAP = buildFreqMap(preferFreqLines, strArr, false);
        } catch (Exception e) {
            logD("rebuildFreqMaps error " + e);
        }
    }

    private synchronized Set getPreferFreqLines() {
        Set set = this.mPreferFreqLines;
        if (set == null || set.isEmpty()) {
            try {
                CloudCellDbHelper cloudCellDbHelper = CloudCellDbHelper.getInstance(this.mContext);
                if (cloudCellDbHelper != null) {
                    this.mPreferFreqLines = cloudCellDbHelper.getAllHstFrequencyLines();
                }
            } catch (Exception e) {
                logD("getPreferFreqLines error: " + e.getMessage());
            }
        }
        Set set2 = this.mPreferFreqLines;
        if (set2 == null) {
            return Collections.EMPTY_SET;
        }
        return Collections.unmodifiableSet(set2);
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_HST_PREFER_FREQ_SWITCH");
        intentFilter.addAction("com.android.phone.intent.action.ACTION_CLOUD_HST_TUNNEL_SMARTLINK_DONE");
        if (MiuiPhoneUtils.isFileUploadMonitorSupported() && MiuiPhoneUtils.isHstSmartlinkOptSupported()) {
            intentFilter.addAction("com.android.phone.intent.action.FILE_UPLOAD_SCENE");
        }
        intentFilter.addAction("com.android.phone.intent.action.HST_SIGNAL_MAP_ACTION_DEBUG_ALL");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    public class SignalMapHSTHandler extends Handler {
        SignalMapHSTHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SignalMapHSTController.this.log("msg.what = " + message.what);
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 1:
                        SignalMapHSTController.this.init();
                        break;
                    case 2:
                        SignalMapHSTController.this.processCellLocationChanged(message.arg1);
                        break;
                    case 3:
                        if (SignalMapHSTController.this.mCurCoverageStatus == 1) {
                            SignalMapHSTController.this.setCoverageState(message.arg1, 2, "fdd2tddHst");
                            if (SignalMapHSTController.this.mElevatorModeController != null) {
                                SignalMapHSTController.this.mElevatorModeController.recordOutCoverageStateV3(message.arg1, "fdd2tddHst");
                            }
                            SignalMapHSTController.this.recordCount("fdd2tdd");
                        }
                        break;
                    case 4:
                        SignalMapHSTController.this.processHstStateChangeEvent((Intent) message.obj);
                        break;
                    case 5:
                        Map map = (Map) ((Intent) message.obj).getSerializableExtra("mHitHstLineStatus");
                        if (map != null) {
                            SignalMapHSTController.this.mLineDirections.clear();
                            SignalMapHSTController.this.mLineDirections.putAll(map);
                            SignalMapHSTController signalMapHSTController = SignalMapHSTController.this;
                            signalMapHSTController.mCurrentLines = signalMapHSTController.getCurrentHitLine();
                            SignalMapHSTController.this.log("EVENT_HIT_HST_LINE_STATUS_CHANGED: lineDirections = " + map.toString());
                            SignalMapHSTController signalMapHSTController2 = SignalMapHSTController.this;
                            signalMapHSTController2.loadHstTunnelCells(signalMapHSTController2.mLineDirections);
                            SignalMapHSTController signalMapHSTController3 = SignalMapHSTController.this;
                            signalMapHSTController3.loadSmartlinkCells(signalMapHSTController3.mLineDirections);
                            if (SignalMapHSTController.this.mLineDirections.size() > 0) {
                                SignalMapHSTController signalMapHSTController4 = SignalMapHSTController.this;
                                signalMapHSTController4.processLineHit(signalMapHSTController4.mCurrentLines);
                            }
                            SignalMapHSTController signalMapHSTController5 = SignalMapHSTController.this;
                            signalMapHSTController5.loadPreferCells(signalMapHSTController5.mLineDirections);
                            SignalMapHSTController signalMapHSTController6 = SignalMapHSTController.this;
                            signalMapHSTController6.setHstPreferredCell(signalMapHSTController6.mLineDirections);
                        }
                        break;
                    case 6:
                        SignalMapHSTController signalMapHSTController7 = SignalMapHSTController.this;
                        signalMapHSTController7.setHstPreferredCell(signalMapHSTController7.mLineDirections);
                        break;
                    case 7:
                        boolean z = SignalMapHSTController.this.mIsScreenOn;
                        SignalMapHSTController signalMapHSTController8 = SignalMapHSTController.this;
                        if (!z) {
                            if (signalMapHSTController8.mHandler.hasMessages(15)) {
                                SignalMapHSTController.this.mHandler.removeMessages(15);
                            }
                            if (SignalMapHSTController.this.mIsEnableSmartlink) {
                                SignalMapHSTController.this.notifyDisableSmartlink();
                            }
                            break;
                        } else if (signalMapHSTController8.mLastSceneTypeFileUpload == 1 && SignalMapHSTController.this.mIsWechatUlEnhanceCallbackStarted) {
                            SignalMapHSTController.this.scheduleWechatUlEnhanceStatusCheckForFileUpload();
                            break;
                        }
                        break;
                    case 8:
                        SignalMapHSTController.this.handleAutoExitTunnel();
                        break;
                    case 9:
                        SignalMapHSTController.this.processServiceStateChanged(message.arg1, (ServiceState) message.obj);
                        break;
                    case 10:
                        if (SignalMapHSTController.this.mIsEnableSmartlink) {
                            SignalMapHSTController.this.notifyDisableSmartlink();
                        }
                        break;
                    case 11:
                        if (!SignalMapHSTController.this.mIsEnableSmartlink && SignalMapHSTController.this.isSatifySmartlinkOptScene()) {
                            SignalMapHSTController.this.mLastSmartLinkScene = 0;
                            SignalMapHSTController.this.notifyEnableSmartlink();
                            SignalMapHSTController.this.mHandler.sendEmptyMessageDelayed(10, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                            break;
                        }
                        break;
                    case 12:
                        SignalMapHSTController.this.processDefaultDataSlotChanged();
                        break;
                    case 13:
                        SignalMapHSTController.this.processFileUploadStateChange((Intent) message.obj);
                        break;
                    case 14:
                        SignalMapHSTController signalMapHSTController9 = SignalMapHSTController.this;
                        signalMapHSTController9.mIsHstTunnelSmartlinkCloudOn = signalMapHSTController9.getHstTunnelSmartlinkCloud();
                        break;
                    case 15:
                        SignalMapHSTController.this.checkWechatUlEnhanceStatusForFileUpload();
                        break;
                    case 16:
                        SignalMapHSTController.this.stopWechatUlEnhanceCallbackForFileUpload();
                        break;
                }
                return;
            }
            Intent intent = (Intent) message.obj;
            String stringExtra = intent.getStringExtra("name");
            boolean booleanExtra = intent.getBooleanExtra(a.J, false);
            long longExtra = intent.getLongExtra(a.p, 0L);
            SignalMapHSTController.this.log("lineName = " + stringExtra + ", lineDirection = " + booleanExtra + ", preferCid = " + longExtra);
            if (stringExtra != null) {
                HashMap map2 = new HashMap();
                map2.put(stringExtra, Boolean.valueOf(booleanExtra));
                SignalMapHSTController.this.mLineDirections.clear();
                SignalMapHSTController.this.mLineDirections.putAll(map2);
                SignalMapHSTController signalMapHSTController10 = SignalMapHSTController.this;
                signalMapHSTController10.loadPreferCells(signalMapHSTController10.mLineDirections);
                SignalMapHSTController.this.setHstPreferredCell(map2);
            }
            if (longExtra != 0) {
                SignalMapHSTController signalMapHSTController11 = SignalMapHSTController.this;
                signalMapHSTController11.processPreferCellOptimization(longExtra, signalMapHSTController11.getDefaultDataSlotId());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getHstTunnelSmartlinkCloud() {
        boolean hstTunnelSmartlinkByCloud = FiveGManagerBase.getHstTunnelSmartlinkByCloud(this.mContext);
        log("getHstTunnelSmartlinkCloud rst = " + hstTunnelSmartlinkByCloud);
        return hstTunnelSmartlinkByCloud;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processFileUploadStateChange(Intent intent) {
        int intExtra = intent.getIntExtra("extra_file_upload_scene_type", -1);
        log("processFileUploadStateChange, type=" + intExtra);
        if (this.mLastSceneTypeFileUpload == intExtra) {
            return;
        }
        this.mLastSceneTypeFileUpload = intExtra;
        if (intExtra != 1) {
            if (intExtra == 0) {
                scheduleStopWechatUlEnhanceCallbackForFileUpload();
                if (this.mIsEnableSmartlink && isSmartlinkEnabledLongEnough()) {
                    notifyDisableSmartlink();
                    return;
                }
                return;
            }
            return;
        }
        startWechatUlEnhanceCallbackForFileUpload();
        if (!isSatifySmartlinkOptSceneForFileUpload() || this.mIsEnableSmartlink) {
            return;
        }
        this.mLastSmartLinkScene = this.mCurrentHstState ? 1 : 2;
        notifyEnableSmartlink();
        HighSpeedTrainStats.recordFileUpload();
        scheduleDisableSmartlinkForFileUpload();
    }

    private boolean isSmartlinkEnabledLongEnough() {
        return SystemClock.elapsedRealtime() - this.mLastEnableSmartlinkTime >= MirrorDesktopHelper.TIMEOUT_MILLIS;
    }

    private void startWechatUlEnhanceCallbackForFileUpload() {
        if (this.mHandler.hasMessages(16)) {
            this.mHandler.removeMessages(16);
        }
        ModemDataCollectorBase modemDataCollectorBase = ModemDataCollectorBase.getInstance();
        if (modemDataCollectorBase == null) {
            log("startWechatUlEnhanceCallbackForFileUpload collector is null");
            return;
        }
        if (!this.mIsWechatUlEnhanceCallbackStarted) {
            modemDataCollectorBase.startWechatUlEnhanceCallback();
            this.mIsWechatUlEnhanceCallbackStarted = true;
        }
        scheduleWechatUlEnhanceStatusCheckForFileUpload();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleWechatUlEnhanceStatusCheckForFileUpload() {
        if (this.mHandler.hasMessages(15)) {
            this.mHandler.removeMessages(15);
        }
        if (this.mLastSceneTypeFileUpload == 1 && this.mIsScreenOn) {
            this.mHandler.sendEmptyMessageDelayed(15, 3000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkWechatUlEnhanceStatusForFileUpload() {
        if (this.mLastSceneTypeFileUpload != 1) {
            return;
        }
        if (!this.mIsScreenOn) {
            scheduleWechatUlEnhanceStatusCheckForFileUpload();
            return;
        }
        if (!isSatifySmartlinkOptSceneForFileUpload() || this.mIsEnableSmartlink) {
            scheduleWechatUlEnhanceStatusCheckForFileUpload();
            return;
        }
        ModemDataCollectorBase modemDataCollectorBase = ModemDataCollectorBase.getInstance();
        if (modemDataCollectorBase == null) {
            log("checkWechatUlEnhanceStatusForFileUpload collector is null");
            return;
        }
        boolean zIsSupportWechatUlEnhance = modemDataCollectorBase.isSupportWechatUlEnhance();
        log("checkWechatUlEnhanceStatusForFileUpload isSupportWechatUlEnhance=" + zIsSupportWechatUlEnhance);
        if (zIsSupportWechatUlEnhance) {
            this.mLastSmartLinkScene = this.mCurrentHstState ? 1 : 2;
            notifyEnableSmartlink();
            scheduleDisableSmartlinkForFileUpload();
        }
        scheduleWechatUlEnhanceStatusCheckForFileUpload();
    }

    private void scheduleDisableSmartlinkForFileUpload() {
        int i = !this.mCurrentHstState ? 5000 : 60000;
        if (this.mHandler.hasMessages(10)) {
            this.mHandler.removeMessages(10);
        }
        this.mHandler.sendEmptyMessageDelayed(10, i);
    }

    private void scheduleStopWechatUlEnhanceCallbackForFileUpload() {
        if (this.mHandler.hasMessages(15)) {
            this.mHandler.removeMessages(15);
        }
        if (this.mHandler.hasMessages(16)) {
            this.mHandler.removeMessages(16);
        }
        this.mHandler.sendEmptyMessageDelayed(16, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopWechatUlEnhanceCallbackForFileUpload() {
        if (this.mLastSceneTypeFileUpload == 1 || !this.mIsWechatUlEnhanceCallbackStarted) {
            return;
        }
        ModemDataCollectorBase modemDataCollectorBase = ModemDataCollectorBase.getInstance();
        if (modemDataCollectorBase == null) {
            log("stopWechatUlEnhanceCallbackForFileUpload collector is null");
        } else {
            modemDataCollectorBase.stopWechatUlEnhanceCallback();
            this.mIsWechatUlEnhanceCallbackStarted = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChanged(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("Invalid PhoneId.");
            return;
        }
        initRouteData();
        long cid = getCid(i);
        updateCellFreqInfo(i);
        logD("processCellLocationChanged cid=" + cid + " freq=" + this.mCurrentFreq[i]);
        if (cid == -1 || this.mLastCid == cid) {
            log("Invalid or same CID: " + cid);
            return;
        }
        processHstTunnelState(cid);
        processPreloadOptimization(cid);
        if (MiuiPhoneUtils.isDnsPrefetchSupported()) {
            processDnsPrefetchOptimization(cid);
        }
        if (MiuiPhoneUtils.isHstSmartlinkOptSupported()) {
            processSmartlinkOptimization(cid);
        }
        this.mLastCid = cid;
        if (tryMatchRoute(cid)) {
            onRouteMatched(i, cid);
        }
        processPreferCellOptimization(cid, i);
    }

    private void processHstTunnelState(long j) {
        if (this.mEnterTunnelCell.size() == 0) {
            loadHstTunnelCells(this.mLineDirections);
        }
        if (this.mExitTunnelCell.size() == 0) {
            log("processHstTunnelState, mExitTunnelCell is empty, return");
            return;
        }
        String strValueOf = String.valueOf(j);
        if (!this.mHstTunnelState) {
            if (this.mEnterTunnelCell.contains(strValueOf)) {
                this.mHstTunnelState = true;
                localLog("hstTunnelStateChange", "enter tunnel");
                startAutoExitHstTunnelTimer();
                return;
            }
            return;
        }
        if (this.mExitTunnelCell.contains(strValueOf)) {
            this.mHstTunnelState = false;
            localLog("hstTunnelStateChange", "exit tunnel");
            stopAutoExitHstTunnelTimer();
        }
    }

    private void startAutoExitHstTunnelTimer() {
        stopAutoExitHstTunnelTimer();
        SignalMapHSTHandler signalMapHSTHandler = this.mHandler;
        signalMapHSTHandler.sendMessageDelayed(signalMapHSTHandler.obtainMessage(8), 300000L);
    }

    private void stopAutoExitHstTunnelTimer() {
        if (this.mHandler.hasMessages(8)) {
            this.mHandler.removeMessages(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAutoExitTunnel() {
        if (this.mHstTunnelState) {
            this.mHstTunnelState = false;
            localLog("hstTunnelStateChange", "auto exit tunnel");
        }
    }

    private void initRouteData() {
        Map map = this.mRouteCidsMap;
        if (map == null || !map.isEmpty()) {
            return;
        }
        synchronized (this) {
            if (this.mRouteCidsMap.isEmpty()) {
                try {
                    loadCidFromFile("hst_cid_default.bin");
                } catch (Exception e) {
                    log("Initialize failed: " + e.getMessage());
                }
            }
        }
    }

    public boolean tryMatchRoute(long j) {
        if (this.mBloomFilter != null && this.mBloomFilter.mightContain(j)) {
            for (Map.Entry entry : this.mRouteCidsMap.entrySet()) {
                if (((Set) entry.getValue()).contains(Long.valueOf(j))) {
                    String routeNameById = getRouteNameById(((Integer) entry.getKey()).intValue());
                    if (this.mLineDirections.containsKey(routeNameById)) {
                        return true;
                    }
                    this.mLineDirections.put(routeNameById, Boolean.TRUE);
                    this.mCurrentLines = getCurrentHitLine();
                    log("Matched route: " + this.mCurrentLines);
                    processLineHit(this.mCurrentLines);
                    return true;
                }
            }
        }
        return false;
    }

    private void onRouteMatched(int i, long j) {
        stageOptimization(j);
        if (isFDDtoTDDRequired(this.mLineDirections) && needFdd2Tdd(j)) {
            processFdd2TddOptimization(j);
        }
    }

    private void loadCidFromFile(String str) throws IOException {
        HashSet hashSet = new HashSet();
        try {
            InputStream inputStreamOpen = this.mContext.getResources().getAssets().open(str);
            try {
                DataInputStream dataInputStream = new DataInputStream(inputStreamOpen);
                while (true) {
                    try {
                        int i = dataInputStream.readByte() & TransitionInfo.INIT;
                        long j = dataInputStream.readLong();
                        ((Set) this.mRouteCidsMap.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.phone.signalMap.SignalMapHSTController$$ExternalSyntheticLambda0
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return ConcurrentHashMap.newKeySet();
                            }
                        })).add(Long.valueOf(j));
                        hashSet.add(Long.valueOf(j));
                    } catch (EOFException unused) {
                        dataInputStream.close();
                        if (inputStreamOpen != null) {
                            inputStreamOpen.close();
                        }
                        initBloomFilter(hashSet);
                        return;
                    } catch (Throwable th) {
                        try {
                            dataInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
            } catch (Throwable th3) {
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                }
                throw th3;
            }
        } catch (IOException e) {
            log("Failed to load CIDs from file: " + str + ", error: " + e.getMessage());
            throw e;
        }
    }

    private void initBloomFilter(Set set) {
        this.mBloomFilter = new BloomFilter(set.size(), 0.01d);
        Iterator it = set.iterator();
        while (it.hasNext()) {
            this.mBloomFilter.add(((Long) it.next()).longValue());
        }
    }

    class BloomFilter {
        private final int bitSize;
        private final BitSet bits;
        private final int hashCount;

        BloomFilter(int i, double d) {
            int iCalculateBitSize = calculateBitSize(i, d);
            this.bitSize = iCalculateBitSize;
            this.hashCount = calculateHashCount(iCalculateBitSize, i);
            this.bits = new BitSet(iCalculateBitSize);
        }

        void add(long j) {
            long jAsLong = Hashing.murmur3_128().hashLong(j).asLong();
            int i = (int) jAsLong;
            int i2 = (int) (jAsLong >>> 32);
            for (int i3 = 0; i3 < this.hashCount; i3++) {
                this.bits.set(Math.abs(((i3 * i2) + i) % this.bitSize));
            }
        }

        boolean mightContain(long j) {
            long jAsLong = Hashing.murmur3_128().hashLong(j).asLong();
            int i = (int) jAsLong;
            int i2 = (int) (jAsLong >>> 32);
            for (int i3 = 0; i3 < this.hashCount; i3++) {
                if (!this.bits.get(Math.abs(((i3 * i2) + i) % this.bitSize))) {
                    return false;
                }
            }
            return true;
        }

        void clear() {
            this.bits.clear();
        }

        static int calculateBitSize(int i, double d) {
            return (int) ((((double) (-i)) * Math.log(d)) / (Math.log(2.0d) * Math.log(2.0d)));
        }

        static int calculateHashCount(int i, int i2) {
            return Math.max(1, (int) Math.round((((double) i) / ((double) i2)) * Math.log(2.0d)));
        }
    }

    private String getRouteNameById(int i) {
        log("getRouteNameById, routeId = " + i);
        if (i == 1) {
            return "G21";
        }
        if (i == 2) {
            return "D6724";
        }
        return "Unknown";
    }

    private void stageOptimization(long j) {
        if (shouldOptimization() && needDubiousCell(j) && MiuiPhoneUtils.isSnsOptimizationSupported()) {
            String str = (String) this.mDubiousCell.get(String.valueOf(j));
            if (TextUtils.isEmpty(str)) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            SignalMapSnsControllerBase.getInstance().setLteNrDubiousCellIfNeed(arrayList, "signalMap");
            recordCount("dubiousCell");
        }
    }

    private boolean shouldOptimization() {
        return (MiuiPhoneUtils.isWifiConnected() || !MiuiPhoneUtils.isMobileDataEnabled(this.mContext) || MiuiPhoneUtils.isDuringCall() || !this.mIsScreenOn || MiuiPhoneUtils.getTopAppName().equals("com.miui.home") || (Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "airplane_mode_on", -1) == 1)) ? false : true;
    }

    private boolean isScreenOn() {
        PowerManager powerManager = this.mPowerManager;
        return powerManager != null && powerManager.isInteractive();
    }

    private boolean isCurrentHstState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_state_highspeedtrain", 0) == 1;
    }

    private boolean isPreloadVideoWhiteList() {
        String topAppName = MiuiPhoneUtils.getTopAppName();
        logD("isPreloadVideoWhiteList topAppName:" + topAppName);
        List stringArray = HyperOSCustFeatureResolve.getStringArray("config_hst_preload_packages");
        return stringArray != null && stringArray.contains(topAppName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        DefaultSimManager.getInstance();
        return DefaultSimManager.getDefaultDataSlotId();
    }

    private boolean isSatifyCommonScene(int i) {
        boolean z = (!MiuiPhoneUtils.isSimReady(i) || MiuiPhoneUtils.isAirPlaneMode() || MiuiPhoneUtils.isDuringCall() || !MiuiPhoneUtils.isMobileDataConnected(i) || MiuiPhoneUtils.isWifiConnected() || !MiuiPhoneUtils.isLteNrNwMode(i) || MiuiPhoneUtils.isManualSelection(i)) ? false : true;
        log("isSatifyCommonScene rst=" + z);
        return z;
    }

    public void processFdd2TddOptimization(long j) {
        int defaultDataSlotId = getDefaultDataSlotId();
        if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) && isSatifyCommonScene(defaultDataSlotId)) {
            log("processFdd2TddOptimization setCoverageState!");
            if (this.mHandler.hasMessages(3)) {
                this.mHandler.removeMessages(3);
            }
            setCoverageState(defaultDataSlotId, 1, "fdd2tddHst");
            SignalMapHSTHandler signalMapHSTHandler = this.mHandler;
            signalMapHSTHandler.sendMessageDelayed(signalMapHSTHandler.obtainMessage(3, defaultDataSlotId, 0), 2000L);
        }
    }

    private boolean isFDDtoTDDRequired(Map map) {
        SignalMapHSTCloudController signalMapHSTCloudController = this.mSignalMapHSTCloudController;
        if (signalMapHSTCloudController != null) {
            return signalMapHSTCloudController.getFDDToTDDStatus() && !map.isEmpty() && map.containsKey("G21");
        }
        logD("isFDDtoTDDRequired mSignalMapHSTCloudController is null");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCoverageState(int i, int i2, String str) {
        this.mCurCoverageStatus = i2;
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().setCoverageState(i, i2, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLineHit(String str) {
        recordCount("lineHit_" + str);
        loadDubiousCells(str);
        loadFdd2TddCells(str);
    }

    public void processDnsPrefetchOptimization(long j) {
        if (this.mEnterTunnelCell.size() == 0) {
            loadHstTunnelCells(this.mLineDirections);
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.mLastDnsPrefetchTime > 30000 && needPreloading(j)) {
            Intent intent = new Intent();
            intent.setAction("com.android.phone.intent.action.DNS_PREFETCH");
            this.mContext.sendBroadcast(intent);
            this.mLastDnsPrefetchTime = jCurrentTimeMillis;
            localLog("hstDnsPrefetch", "notify");
            return;
        }
        if (needPreloading(j)) {
            localLog("hstDnsPrefetch", "timeLimit");
        }
    }

    public void processPreloadOptimization(long j) {
        if (this.mEnterTunnelCell.size() == 0) {
            loadHstTunnelCells(this.mLineDirections);
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        boolean z = jCurrentTimeMillis - this.mLastPreloadTime > RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
        logD("processPreloadOptimization, needPreloading = " + needPreloading(j) + ", isPreloadVideoWhiteList = " + isPreloadVideoWhiteList());
        if (z && needPreloading(j) && isPreloadVideoWhiteList()) {
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().videoAppsPolicyNotify(1, -1, -1, VideoPreloadScene.HST_SIGNAL_MAP.getValue());
            }
            recordCount("preload");
            this.mLastPreloadTime = jCurrentTimeMillis;
            log("preload success.");
        }
    }

    public void processPreferCellOptimization(long j, int i) {
        if (needPreferCell(j)) {
            Map map = (Map) this.mPreferCell.get(String.valueOf(j));
            if (map == null || map.isEmpty()) {
                logD("cellInfo is Empty");
                return;
            }
            getNetworkNumeric(map, i);
            logD("processPreferCellOptimization " + map);
            HighSpeedTrainStats.recordPreferCell();
            if (MiuiPhoneUtils.isSNSV5SupportedByQcom()) {
                setCellListInfoV2ForHst(j, this.mLineDirections, map);
            } else if (MiuiPhoneUtils.isSNSV4SupportedByQcom()) {
                setPreferredCellForSNSV4(j, this.mLineDirections, map);
            } else if (MiuiPhoneUtils.isHighPriorityCellSelectionSupported()) {
                setHighPriorityCellForHst(map, i);
            }
        }
    }

    private void getNetworkNumeric(Map map, int i) {
        String networkNumeric = MiuiPhoneUtils.getNetworkNumeric(i);
        if (TextUtils.isEmpty(networkNumeric) || networkNumeric.length() < 5) {
            return;
        }
        String strSubstring = networkNumeric.substring(0, 3);
        String strSubstring2 = networkNumeric.substring(3);
        if (map.containsKey("mcc")) {
            map.put("mcc", strSubstring);
        }
        if (map.containsKey("mnc")) {
            map.put("mnc", strSubstring2);
        }
        if (map.containsKey("nextmcc")) {
            map.put("nextmcc", strSubstring);
        }
        if (map.containsKey("nextmnc")) {
            map.put("nextmnc", strSubstring2);
        }
    }

    private void setHighPriorityCellForHst(Map map, int i) {
        ArrayList arrayList = new ArrayList();
        String[][] strArr = {new String[]{"mcc", "mnc", "arfcn", "pci", "rat"}, new String[]{"nextmcc", "nextmnc", "nextarfcn", "nextpci", "nextrat"}};
        for (int i2 = 0; i2 < 2; i2++) {
            HighPriorityCellSelection.HighPriorityCellInfo highPriorityCellInfoCreateCellInfo = createCellInfo(map, strArr[i2]);
            if (highPriorityCellInfoCreateCellInfo != null) {
                arrayList.add(highPriorityCellInfoCreateCellInfo);
            }
        }
        logD("setCellListInfo " + arrayList);
        if (arrayList.isEmpty() || NetworkRecoveryStrategyBase.getInstance() == null) {
            return;
        }
        NetworkRecoveryStrategyBase.getInstance().setCellListInfo(i, arrayList, 60L);
    }

    private HighPriorityCellSelection.HighPriorityCellInfo createCellInfo(Map map, String[] strArr) {
        if (strArr == null || strArr.length < 5) {
            log("Invalid keys array: null or length < 5");
            return null;
        }
        String str = (String) map.get(strArr[0]);
        String str2 = (String) map.get(strArr[1]);
        String str3 = (String) map.get(strArr[2]);
        String str4 = (String) map.get(strArr[3]);
        String str5 = (String) map.get(strArr[4]);
        if (str == null || str2 == null || str3 == null || str4 == null) {
            log("processHighPriorityCellInfo invalid params");
            return null;
        }
        try {
            return new HighPriorityCellSelection.HighPriorityCellInfo(Short.parseShort(str), Short.parseShort(str2), str2.matches("\\d{3}"), Integer.parseInt(str3), Short.parseShort(str4), "1".equals(str5));
        } catch (NumberFormatException e) {
            log("Cell parsing failed: " + e.getMessage());
            return null;
        }
    }

    public void processSmartlinkOptimization(long j) {
        if (!this.mIsHstTunnelSmartlinkCloudOn) {
            log("processSmartlinkOptimization cloud off");
            return;
        }
        if (this.mEnterTunnelForSmartlinkCell.isEmpty()) {
            loadSmartlinkCells(this.mLineDirections);
        }
        if (this.mIsEnableSmartlink) {
            if (needDisableSmartlinkCell(j)) {
                notifyDisableSmartlink();
            }
        } else if (needEnableSmartlinkCell(j)) {
            startDelayEnableSmartlink();
        }
    }

    private void startDelayEnableSmartlink() {
        if (this.mHandler.hasMessages(11)) {
            return;
        }
        log("startDelayEnableSmartlink");
        this.mHandler.sendEmptyMessageDelayed(11, 30000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSatifySmartlinkOptScene() {
        boolean z = false;
        boolean z2 = this.mLastDisableSmartlinkTime != 0 && SystemClock.elapsedRealtime() - this.mLastDisableSmartlinkTime <= 180000;
        if (!z2 && this.mIsScreenOn && MiuiPhoneUtils.isMobileDataConnected() && !MiuiPhoneUtils.isWifiConnected()) {
            z = true;
        }
        if (!z) {
            log("isSatifySmartlinkOptScene timeLimit=" + z2 + ", mIsScreenOn=" + this.mIsScreenOn);
        }
        return z;
    }

    private boolean isSatifySmartlinkOptSceneForFileUpload() {
        boolean z = false;
        boolean z2 = this.mLastDisableSmartlinkTime != 0 && SystemClock.elapsedRealtime() - this.mLastDisableSmartlinkTime <= 180000;
        boolean z3 = MiuiPhoneUtils.isSpecialNetorkOptimizationSupported() && NetworkMetricsController.getInstance().getDdsLteNrRsrp() < -105;
        boolean z4 = NetworkMetricsController.getInstance() != null && NetworkMetricsController.getInstance().isInCall();
        if (!z2 && this.mIsScreenOn && !z4 && MiuiPhoneUtils.isMobileDataConnected() && !MiuiPhoneUtils.isWifiConnected() && z3) {
            z = true;
        }
        if (!z) {
            log("isSatifySmartlinkOptSceneForFileUpload, timeLimit=" + z2 + ", isInCall=" + z4 + ", mIsScreenOn=" + this.mIsScreenOn + ", isWeakRsrp=" + z3);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyEnableSmartlink() {
        localLog("hstSmartlink", "enable, scene=" + this.mLastSmartLinkScene);
        if (this.mLastSmartLinkScene == 1) {
            FileUploadStats.getInstance().recordTriggerTimes("mHstSmartLinkForFileUploadCount");
        } else if (this.mLastSmartLinkScene == 0) {
            recordCount("smartLink");
        } else if (this.mLastSmartLinkScene == 2) {
            FileUploadStats.getInstance().recordTriggerTimes("mCommonSmartLinkForFileUploadCount");
        }
        this.mIsEnableSmartlink = true;
        this.mLastEnableSmartlinkTime = SystemClock.elapsedRealtime();
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.HST_TUNNEL_SMARTLINK");
        intent.putExtra(a.p, 1);
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDisableSmartlink() {
        localLog("hstSmartlink", "disable, scene=" + this.mLastSmartLinkScene);
        if (this.mLastSmartLinkScene == 1) {
            FileUploadStats.getInstance().recordTriggerTimes("mHstSmartLinkForFileUploadExitCount");
        } else if (this.mLastSmartLinkScene == 0) {
            recordCount("smartLinkExit");
        } else if (this.mLastSmartLinkScene == 2) {
            FileUploadStats.getInstance().recordTriggerTimes("mCommonSmartLinkForFileUploadExitCount");
        }
        this.mIsEnableSmartlink = false;
        this.mLastDisableSmartlinkTime = SystemClock.elapsedRealtime();
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.HST_TUNNEL_SMARTLINK");
        intent.putExtra(a.p, 0);
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
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
    public void loadHstTunnelCells(Map map) {
        if (this.mSignalMapHSTCloudController == null) {
            logD("loadHstTunnelCells mSignalMapHSTCloudController is null");
            return;
        }
        this.mEnterTunnelCell.clear();
        this.mExitTunnelCell.clear();
        if (map == null || map.isEmpty()) {
            this.mEnterTunnelCell = this.mSignalMapHSTCloudController.getHSTPreloadCellFromDb();
        } else {
            for (Map.Entry entry : map.entrySet()) {
                String str = (String) entry.getKey();
                boolean zBooleanValue = ((Boolean) entry.getValue()).booleanValue();
                Set hSTTunnelCellFromDbByLineAndDirection = this.mSignalMapHSTCloudController.getHSTTunnelCellFromDbByLineAndDirection(str, !zBooleanValue ? 1 : 0);
                if (hSTTunnelCellFromDbByLineAndDirection != null && !hSTTunnelCellFromDbByLineAndDirection.isEmpty()) {
                    this.mEnterTunnelCell.addAll(hSTTunnelCellFromDbByLineAndDirection);
                }
                Set hSTTunnelCellFromDbByLineAndDirection2 = this.mSignalMapHSTCloudController.getHSTTunnelCellFromDbByLineAndDirection(str, zBooleanValue ? 1 : 0);
                if (hSTTunnelCellFromDbByLineAndDirection2 != null && !hSTTunnelCellFromDbByLineAndDirection2.isEmpty()) {
                    this.mExitTunnelCell.addAll(hSTTunnelCellFromDbByLineAndDirection2);
                }
            }
        }
        logD("mEnterTunnelCell = " + this.mEnterTunnelCell);
    }

    /* JADX INFO: Access modifiers changed from: private */
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
    public void loadSmartlinkCells(Map map) {
        if (this.mSignalMapHSTCloudController == null) {
            logD("loadSmartlinkCells mSignalMapHSTCloudController is null");
            return;
        }
        this.mEnterTunnelForSmartlinkCell.clear();
        this.mExitTunnelSmartlinkCell.clear();
        if (map == null || map.isEmpty()) {
            for (String str : SMARTLINK_CELL_LINE) {
                Set hSTSmartlinkCellFromDb = this.mSignalMapHSTCloudController.getHSTSmartlinkCellFromDb(str);
                if (hSTSmartlinkCellFromDb != null && !hSTSmartlinkCellFromDb.isEmpty()) {
                    this.mEnterTunnelForSmartlinkCell.addAll(hSTSmartlinkCellFromDb);
                }
            }
            this.mExitTunnelSmartlinkCell.clear();
        } else {
            for (Map.Entry entry : map.entrySet()) {
                String str2 = (String) entry.getKey();
                if (Arrays.asList(SMARTLINK_CELL_LINE).contains(str2)) {
                    boolean zBooleanValue = ((Boolean) entry.getValue()).booleanValue();
                    Set hSTTunnelCellFromDbByLineAndDirection = this.mSignalMapHSTCloudController.getHSTTunnelCellFromDbByLineAndDirection(str2, !zBooleanValue ? 1 : 0);
                    if (hSTTunnelCellFromDbByLineAndDirection != null && !hSTTunnelCellFromDbByLineAndDirection.isEmpty()) {
                        this.mEnterTunnelForSmartlinkCell.addAll(hSTTunnelCellFromDbByLineAndDirection);
                    }
                    Set hSTTunnelCellFromDbByLineAndDirection2 = this.mSignalMapHSTCloudController.getHSTTunnelCellFromDbByLineAndDirection(str2, zBooleanValue ? 1 : 0);
                    if (hSTTunnelCellFromDbByLineAndDirection2 != null && !hSTTunnelCellFromDbByLineAndDirection2.isEmpty()) {
                        this.mExitTunnelSmartlinkCell.addAll(hSTTunnelCellFromDbByLineAndDirection2);
                    }
                }
            }
        }
        logD("mEnterTunnelForSmartlinkCell = " + this.mEnterTunnelForSmartlinkCell);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadPreferCells(Map map) {
        Map hSTPreferCellFromDb;
        if (map == null || map.isEmpty()) {
            return;
        }
        if (this.mSignalMapHSTCloudController == null) {
            logD("loadPreferCells mSignalMapHSTCloudController is null");
            return;
        }
        this.mPreferCell.clear();
        for (String str : map.keySet()) {
            if (!TextUtils.isEmpty(str) && (hSTPreferCellFromDb = this.mSignalMapHSTCloudController.getHSTPreferCellFromDb(str)) != null && !hSTPreferCellFromDb.isEmpty()) {
                this.mPreferCell.putAll(hSTPreferCellFromDb);
            }
        }
    }

    private void loadDubiousCells(String str) {
        SignalMapHSTCloudController signalMapHSTCloudController = this.mSignalMapHSTCloudController;
        if (signalMapHSTCloudController == null) {
            logD("loadDubiousCells mSignalMapHSTCloudController is null");
            return;
        }
        HashMap hSTDubiousCellFromDb = signalMapHSTCloudController.getHSTDubiousCellFromDb(str);
        if (hSTDubiousCellFromDb == null || hSTDubiousCellFromDb.isEmpty()) {
            return;
        }
        this.mDubiousCell.putAll(hSTDubiousCellFromDb);
        logD("mDubiousCell = " + this.mDubiousCell);
    }

    private void loadFdd2TddCells(String str) {
        SignalMapHSTCloudController signalMapHSTCloudController = this.mSignalMapHSTCloudController;
        if (signalMapHSTCloudController == null) {
            logD("loadFdd2TddCells mSignalMapHSTCloudController is null");
            return;
        }
        Set hSTFDD2TDDCellFromDb = signalMapHSTCloudController.getHSTFDD2TDDCellFromDb(str);
        if (hSTFDD2TDDCellFromDb == null || hSTFDD2TDDCellFromDb.isEmpty()) {
            return;
        }
        this.mFdd2TddCells.addAll(hSTFDD2TDDCellFromDb);
        logD("mFdd2TddCells = " + this.mFdd2TddCells);
    }

    private boolean needPreloading(long j) {
        Set set = this.mEnterTunnelCell;
        if (set == null || set.size() == 0) {
            log("mEnterTunnelCell is empty");
            return false;
        }
        return this.mEnterTunnelCell.contains(String.valueOf(j));
    }

    private boolean needDubiousCell(long j) {
        HashMap map = this.mDubiousCell;
        if (map == null || map.size() == 0) {
            log("mDubiousCell is empty");
            return false;
        }
        return this.mDubiousCell.containsKey(String.valueOf(j));
    }

    private boolean needFdd2Tdd(long j) {
        Set set = this.mFdd2TddCells;
        if (set == null || set.size() == 0) {
            log("mFdd2TddCells is empty");
            return false;
        }
        return this.mFdd2TddCells.contains(String.valueOf(j));
    }

    private boolean needPreferCell(long j) {
        if (!checkPreferCellContainsCid(j)) {
            return false;
        }
        Map map = (Map) this.mPreferCell.get(String.valueOf(j));
        if (!isDirAndDirectionsValid(map)) {
            return false;
        }
        boolean zEquals = "1".equals(map.get("direction"));
        Boolean bool = (Boolean) this.mLineDirections.get(map.get("railtrip"));
        if (bool == null) {
            return false;
        }
        boolean z = zEquals == bool.booleanValue();
        logD("needPreferCell direction targetForwardDirection=" + zEquals + ", curHstDirection=" + bool + ", res=" + z);
        return z;
    }

    private boolean checkPreferCellContainsCid(long j) {
        if (ArrayUtils.isEmpty(this.mPreferCell)) {
            logD("mPreferCell is empty");
            return false;
        }
        String strValueOf = String.valueOf(j);
        if (this.mPreferCell.containsKey(strValueOf)) {
            return true;
        }
        logD("mPreferCell does not contain " + strValueOf);
        return false;
    }

    private boolean isDirAndDirectionsValid(Map map) {
        if (ArrayUtils.isEmpty(map)) {
            logD("dir is empty");
            return false;
        }
        if (map.get("direction") == null || map.get("railtrip") == null) {
            logD("dir missing required keys");
            return false;
        }
        if (ArrayUtils.isEmpty(this.mLineDirections)) {
            logD("mLineDirections is empty");
            return false;
        }
        String str = (String) map.get("railtrip");
        if (this.mLineDirections.containsKey(str) && this.mLineDirections.get(str) != null) {
            return true;
        }
        logD("mLineDirections missing railTrip: " + str);
        return false;
    }

    private boolean needEnableSmartlinkCell(long j) {
        Set set = this.mEnterTunnelForSmartlinkCell;
        if (set == null || set.isEmpty()) {
            log("mEnterTunnelForSmartlinkCell is empty");
            return false;
        }
        return this.mEnterTunnelForSmartlinkCell.contains(String.valueOf(j));
    }

    private boolean needDisableSmartlinkCell(long j) {
        Set set = this.mExitTunnelSmartlinkCell;
        if (set == null || set.isEmpty()) {
            log("mExitTunnelSmartlinkCell is empty");
            return false;
        }
        return this.mExitTunnelSmartlinkCell.contains(String.valueOf(j));
    }

    private long getCid(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null && phone.getCurrentCellIdentity() != null) {
            CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
            if (currentCellIdentity instanceof CellIdentityLte) {
                return ((CellIdentityLte) currentCellIdentity).getCi();
            }
            if (currentCellIdentity instanceof CellIdentityNr) {
                return ((CellIdentityNr) currentCellIdentity).getNci();
            }
        }
        return -1L;
    }

    private void updateCellFreqInfo(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return;
        }
        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
        if (currentCellIdentity instanceof CellIdentityLte) {
            this.mCurrentFreq[i] = ((CellIdentityLte) currentCellIdentity).getEarfcn();
            this.mIsCurrentCellLte[i] = true;
        } else if (currentCellIdentity instanceof CellIdentityNr) {
            this.mCurrentFreq[i] = ((CellIdentityNr) currentCellIdentity).getNrarfcn();
            this.mIsCurrentCellLte[i] = false;
        }
    }

    private boolean isFreqInHstConfig(int i, boolean z) {
        if (i != -1 && !this.mLineDirections.isEmpty() && !"INVALID".equals(this.mDdsOperator)) {
            Map map = z ? this.HST_LTE_FREQ_MAP : this.HST_NR_FREQ_MAP;
            if (map != null && !map.isEmpty()) {
                Iterator it = new HashSet(this.mLineDirections.keySet()).iterator();
                while (it.hasNext()) {
                    List<List> list = (List) map.get(((String) it.next()) + "|" + this.mDdsOperator);
                    if (list != null) {
                        for (List list2 : list) {
                            try {
                                if (Integer.parseInt((String) list2.get(3)) == i) {
                                    return true;
                                }
                            } catch (NumberFormatException unused) {
                                logD("isFreqInHstConfig parse error: " + ((String) list2.get(3)));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private String listToString(List list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            Long l = (Long) list.get(i);
            sb.append(l != null ? l.longValue() / 1000 : 0L);
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDefaultDataSlotChanged() {
        String strOperatorForDds = operatorForDds();
        if (TextUtils.equals(strOperatorForDds, this.mDdsOperator)) {
            return;
        }
        this.mDdsOperator = strOperatorForDds;
        setHstPreferredCell(this.mLineDirections);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServiceStateChanged(int i, ServiceState serviceState) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && serviceState != null && i == getDefaultDataSlotId()) {
            ServiceState serviceState2 = this.mServiceState[i];
            if (serviceState2 == null || !serviceState2.equals(serviceState)) {
                updateCellFreqInfo(i);
                this.mServiceState[i] = new ServiceState(serviceState);
                String operatorNumeric = serviceState.getOperatorNumeric();
                if (!TextUtils.isEmpty(operatorNumeric)) {
                    this.mCurrentPlmn = operatorNumeric;
                }
                boolean zIsOosState = isOosState(serviceState);
                if (this.mIsOos[i] == zIsOosState || (!this.mCurrentHstState && this.mIsOos[i] != zIsOosState)) {
                    this.mIsOos[i] = zIsOosState;
                    return;
                }
                this.mIsOos[i] = zIsOosState;
                logD("processServiceStateChanged mIsOos=" + zIsOosState + " phoneId=" + i);
                if (zIsOosState) {
                    handleOosEnter(i);
                } else {
                    handleOosExit(i);
                }
            }
        }
    }

    private void handleOosEnter(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            logD("handleOosEnter invalid phoneId=" + i);
            return;
        }
        this.mOosStartTime[i] = SystemClock.elapsedRealtime();
        this.mIsUserOos[i] = isUserOos(i);
        this.mOosCount++;
        logD("OOS started, phoneId=" + i + " mOosCount=" + this.mOosCount + " isUserOos=" + this.mIsUserOos[i]);
    }

    private void handleOosExit(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            logD("handleOosExit invalid phoneId=" + i);
            return;
        }
        long j = 0;
        if (this.mOosStartTime[i] > 0) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long[] jArr = this.mOosStartTime;
            long j2 = jElapsedRealtime - jArr[i];
            this.mOosTotalDuration += j2;
            jArr[i] = 0;
            logD("OOS ended, phoneId=" + i + " duration=" + j2 + "ms, mOosTotalDuration=" + this.mOosTotalDuration);
            j = j2;
        }
        if (this.mIsUserOos[i]) {
            this.mUserOosCount++;
            this.mUserOosDurations.add(Long.valueOf(j));
            logD("OOS exit userOos, phoneId=" + i);
        } else if (isFreqInHstConfig(this.mCurrentFreq[i], this.mIsCurrentCellLte[i])) {
            this.mOosFreqInConfigCount++;
            this.mValidReferFreqDurations.add(Long.valueOf(j));
            logD("OOS exit freq in config, freq=" + this.mCurrentFreq[i] + " isLte=" + this.mIsCurrentCellLte[i] + " mOosFreqInConfigCount=" + this.mOosFreqInConfigCount);
        } else {
            this.mInvalidReferFreqDurations.add(Long.valueOf(j));
        }
        this.mIsUserOos[i] = false;
    }

    private boolean isOosState(ServiceState serviceState) {
        int dataRegState = serviceState.getDataRegState();
        int voiceRegState = serviceState.getVoiceRegState();
        return (dataRegState == 1 || dataRegState == 2) && (voiceRegState == 1 || voiceRegState == 2);
    }

    private boolean isUserOos(int i) {
        return (Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1) || !(TelephonyManager.getDefault().getSimState(i) == 5);
    }

    private String operatorForDds() {
        int defaultDataSlotId = getDefaultDataSlotId();
        String str = "INVALID";
        if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId)) {
            String networkNumeric = MiuiPhoneUtils.getNetworkNumeric(defaultDataSlotId);
            if (!TextUtils.isEmpty(networkNumeric)) {
                if (ServiceProviderUtils.isChinaMobile(networkNumeric)) {
                    str = "CM";
                } else if (ServiceProviderUtils.isChinaUnicom(networkNumeric)) {
                    str = "CU";
                } else if (ServiceProviderUtils.isChinaTelecom(networkNumeric)) {
                    str = "CT";
                } else if (MiuiPhoneUtils.isGermanyDTByVPLMN(networkNumeric)) {
                    str = "GM_DT";
                } else if (MiuiPhoneUtils.isSpainVdfByVPLMN(networkNumeric)) {
                    str = "SP_VDF";
                } else if (MiuiPhoneUtils.isSpainMasByVPLMN(networkNumeric)) {
                    str = "SP_MAS";
                } else if (MiuiPhoneUtils.isFranceMasByVPLMN(networkNumeric)) {
                    str = "FR_MAS";
                }
                logD("operatorForDds numeric=" + networkNumeric + " operator=" + str);
                return str;
            }
        }
        return "INVALID";
    }

    public void setHstPreferredCell(Map map) {
        String str;
        log("setHstPreferredCell, lineDirections=" + map);
        int defaultDataSlotId = getDefaultDataSlotId();
        if (!this.mIsHstPreferFreqCloudOn || !MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) || !MiuiPhoneUtils.isHstPreferCellSupported() || (str = this.mDdsOperator) == null || "INVALID".equals(str) || map == null) {
            return;
        }
        HashMap map2 = new HashMap(map);
        map2.keySet().retainAll(getPreferFreqLines());
        if (map2.isEmpty()) {
            return;
        }
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().setPreferredCell(defaultDataSlotId, map2, this.mDdsOperator, "hst");
            HighSpeedTrainStats.recordPreferFreq();
        }
        recordCount("setHstPreferredCell");
    }

    private void setPreferredCellForSNSV4(long j, Map map, Map map2) {
        String str;
        log("setPreferredCellForSNSV4, lineDirections=" + map + ", cellInfo=" + map2);
        int defaultDataSlotId = getDefaultDataSlotId();
        if (!MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) || !MiuiPhoneUtils.isHstPreferCellSupported() || (str = this.mDdsOperator) == null || "INVALID".equals(str) || map == null || map.isEmpty()) {
            return;
        }
        Map map3 = (Map) this.mPreferCell.get(String.valueOf(j));
        if (map3 == null || map3.isEmpty() || map3.get("score") == null) {
            return;
        }
        try {
            int i = Integer.parseInt((String) map3.get("score")) == 0 ? 2 : 1;
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setPreferredCellForSNSV4(defaultDataSlotId, map, this.mDdsOperator, i, map2, "hst");
            }
            recordCount("setHstPreferredCellV4");
            if (this.mHandler.hasMessages(6)) {
                this.mHandler.removeMessages(6);
            }
            SignalMapHSTHandler signalMapHSTHandler = this.mHandler;
            signalMapHSTHandler.sendMessageDelayed(signalMapHSTHandler.obtainMessage(6, defaultDataSlotId, 0), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        } catch (NumberFormatException e) {
            logD("setPreferredCellForSNSV4 parse score error " + e);
        }
    }

    private boolean isSetCellListInfoV2ParamsValid(Map map) {
        String str;
        return (!MiuiPhoneUtils.isValidatePhoneId(getDefaultDataSlotId()) || !MiuiPhoneUtils.isSNSV5SupportedByQcom() || (str = this.mDdsOperator) == null || "INVALID".equals(str) || map == null || map.isEmpty()) ? false : true;
    }

    private void setCellListInfoV2ForHst(long j, Map map, Map map2) {
        log("setCellListInfoV2ForHst, lineDirections=" + map + ", cellInfo=" + map2);
        if (isSetCellListInfoV2ParamsValid(map)) {
            Map map3 = (Map) this.mPreferCell.get(String.valueOf(j));
            if (map3 == null || map3.isEmpty() || map3.get("score") == null) {
                return;
            }
            try {
                int i = Integer.parseInt((String) map3.get("score")) == 0 ? 2 : 1;
                if (NetworkRecoveryStrategyBase.getInstance() != null) {
                    NetworkRecoveryStrategyBase.getInstance().setCellListInfoV2(map2, i, 1L, "hst");
                }
                recordCount("setCellListHstV2");
            } catch (NumberFormatException e) {
                logD("setCellListInfoV2ForHst parse score error " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processHstStateChangeEvent(Intent intent) {
        try {
            this.mCurrentHstState = intent.getBooleanExtra("mIsHstMode", false);
            log("processHstStateChangeEvent, isHighSpeedTrain:" + this.mCurrentHstState);
            if (!this.mCurrentHstState) {
                clearHstStats();
            } else {
                int defaultDataSlotId = getDefaultDataSlotId();
                if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) && this.mIsOos[defaultDataSlotId]) {
                    long[] jArr = this.mOosStartTime;
                    if (jArr[defaultDataSlotId] <= 0) {
                        jArr[defaultDataSlotId] = SystemClock.elapsedRealtime();
                        logD("processHstStateChangeEvent: set mOosStartTime for OOS state, phoneId=" + defaultDataSlotId);
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void clearHstStats() {
        log("exit Hst State,clear Hst stats");
        int i = 0;
        while (true) {
            long[] jArr = this.mOosStartTime;
            if (i >= jArr.length) {
                break;
            }
            if (jArr[i] > 0) {
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                long[] jArr2 = this.mOosStartTime;
                this.mOosTotalDuration += jElapsedRealtime - jArr2[i];
                jArr2[i] = 0;
            }
            i++;
        }
        reportOosStats();
        this.mOosCount = 0;
        this.mOosTotalDuration = 0L;
        this.mOosFreqInConfigCount = 0;
        this.mUserOosCount = 0;
        this.mValidReferFreqDurations.clear();
        this.mInvalidReferFreqDurations.clear();
        this.mUserOosDurations.clear();
        Arrays.fill(this.mIsOos, false);
        Arrays.fill(this.mCurrentFreq, -1);
        Arrays.fill(this.mOosStartTime, 0L);
        Arrays.fill(this.mIsCurrentCellLte, false);
        Arrays.fill(this.mIsUserOos, false);
        Arrays.fill(this.mServiceState, (Object) null);
        this.mRouteCidsMap.clear();
        if (this.mBloomFilter != null) {
            this.mBloomFilter.clear();
            this.mBloomFilter = null;
        }
        this.mDubiousCell.clear();
        this.mFdd2TddCells.clear();
        this.mPreferCell.clear();
        clearHstLineStats();
    }

    private void reportOosStats() {
        if (this.mOosCount > 0) {
            HashMap map = new HashMap();
            map.put("hst_oos_duration", String.valueOf(this.mOosTotalDuration / 1000));
            map.put("hst_oos_count", String.valueOf(this.mOosCount));
            map.put("hst_preferfreq_valid_count", String.valueOf(this.mOosFreqInConfigCount));
            map.put("hst_user_oos_count", String.valueOf(this.mUserOosCount));
            String str = this.mCurrentLines;
            if (str == null) {
                str = "";
            }
            map.put("hst_line", str);
            map.put("valid_prefer_freq_statistics", listToString(this.mValidReferFreqDurations));
            map.put("invalid_prefer_freq_statistics", listToString(this.mInvalidReferFreqDurations));
            map.put("user_oos_statistics", listToString(this.mUserOosDurations));
            String str2 = this.mCurrentPlmn;
            map.put("NetworkPlmn", str2 != null ? str2 : "");
            MiStatInterfaceUtil.recordObjectEvent("signalMapHST", "event_hst_oos_statistics", map);
            logD("reportOosStats oosCount=" + this.mOosCount + " oosTotalDuration=" + (this.mOosTotalDuration / 1000) + "s oosFreqInConfig=" + this.mOosFreqInConfigCount + " userOosCount=" + this.mUserOosCount + " line=" + this.mCurrentLines + " validDurations=" + this.mValidReferFreqDurations + " invalidDurations=" + this.mInvalidReferFreqDurations + " userOosDurations=" + this.mUserOosDurations + " plmn=" + this.mCurrentPlmn);
        }
    }

    private void clearHstLineStats() {
        log("clear Hst line stats");
        this.mCurrentLines = "";
        this.mEnterTunnelCell.clear();
        this.mExitTunnelCell.clear();
        this.mEnterTunnelForSmartlinkCell.clear();
        this.mExitTunnelSmartlinkCell.clear();
        this.mLineDirections.clear();
        this.mHstTunnelState = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCurrentHitLine() {
        Map map = this.mLineDirections;
        if (map == null || map.isEmpty()) {
            return "";
        }
        return TextUtils.join(",", this.mLineDirections.keySet());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordCount(String str) {
        Integer num = (Integer) this.mStats.get(str);
        logD("recordCount key=" + str + ", value=" + num);
        Map map = this.mStats;
        if (num == null) {
            map.put(str, 1);
        } else {
            map.put(str, Integer.valueOf(num.intValue() + 1));
        }
    }

    public void reportStatistics() {
        Map map = this.mStats;
        if (map == null || map.isEmpty()) {
            return;
        }
        HashMap map2 = new HashMap(this.mStats);
        log("reportStatistics mStats=" + this.mStats);
        this.mStats.clear();
        MiStatInterfaceUtil.recordObjectEvent("signalMapHST", "event_hst_singalmap", map2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getHstPreferFreqSwitch() {
        boolean hstPreferFreqSwitchByCloud = FiveGManagerBase.getHstPreferFreqSwitchByCloud(this.mContext);
        log("getHstPreferFreqSwitch = " + hstPreferFreqSwitchByCloud);
        return hstPreferFreqSwitchByCloud;
    }

    private Map buildFreqMap(Set set, String[] strArr, boolean z) {
        HashMap map = new HashMap();
        if (set != null && strArr != null) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                for (String str2 : strArr) {
                    String str3 = str + "|" + str2;
                    List frequencyData = HstFrequencyConfig.getFrequencyData(str, str2, z);
                    if (frequencyData != null && !frequencyData.isEmpty()) {
                        map.put(str3, frequencyData);
                    }
                }
            }
        }
        return map;
    }

    public static void localLog(String str, String str2) {
        localLog("SignalMapHSTController", str, str2);
    }

    public static void localLog(String str, String str2, String str3) {
        String str4 = str + "." + str2;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(120));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d(str, str2 + " " + str3);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("SignalMapHSTController dump start------");
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
        printWriter.println("SignalMapHSTController dump end------");
        printWriter.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("SignalMapHSTController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String str) {
        if (DEBUG) {
            Log.d("SignalMapHSTController", str);
        }
    }

    private void registerTelephonyCallBack() {
        TelephonyStatAdapter.registerCallBack(new HstTelephonyCallBack());
    }

    class HstTelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
        }

        HstTelephonyCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            if (!MiuiPhoneUtils.isValidatePhoneId(i) || serviceState == null) {
                return;
            }
            SignalMapHSTController.this.mHandler.obtainMessage(9, i, 0, new ServiceState(serviceState)).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            if (SignalMapHSTController.this.mCurrentHstState && i == SignalMapHSTController.this.getDefaultDataSlotId()) {
                SignalMapHSTController.this.mHandler.obtainMessage(2, i, 0).sendToTarget();
            }
        }
    }
}
