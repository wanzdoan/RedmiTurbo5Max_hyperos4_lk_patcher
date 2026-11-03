package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.network.ModemNetworkPerception;
import com.android.phone.statistics.HighSpeedTrainStats;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;
import miui.os.Build;
import miui.process.ForegroundInfo;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class HighSpeedTrainModeController implements HighSpeedTrainStats.CollectSwitchListener {
    private static HighSpeedTrainModeController sInstance;
    private final List mCollectedData;
    private Context mContext;
    private final BaseEvent.IEventListener mEventListener;
    private FiveGServiceInfo[] mFiveGInfo;
    private HighSpeedTrainModeHandler mHandler;
    private long mHstFlagTime;
    private int mHstStateEnhanceCount;
    private long mHstStateEnhanceFlagTime;
    private final BroadcastReceiver mIntentReceiver;
    private boolean mIsHstStateEnhanceCollecting;
    private boolean mIsScreenOn;
    private int mLastModemHstState;
    private int mModemHstStateChangeCount;
    private long mModemHstStateTime;
    private ModemNetworkPerception mModemNetworkPerception;
    private long mModemNonHstStateTime;
    private long mNonHstFlagTime;
    private QmsClientBase.IHstStateListener mQmsHstStateListener;
    private ServiceState[] mServiceState;
    private SignalStrength[] mSignalStrength;
    private HashMap mSummaryData;
    private long mTempHstFlagTime;
    private long mTempHstStateEnhanceFlagTime;
    private long mTempHstStateTime;
    private int mValidCount;
    private HandlerThread mWorkThread;
    private static final HashMap sLocalLogs = new HashMap();
    private static boolean mIsAiInfoSupport = false;
    private boolean VDBG = false;
    private TeleCallBack mTeleCallBack = null;
    private volatile int mDdsPhoneId = -1;
    private QmsClientBase mQmsClient = null;
    private boolean mIsLastHst5G = false;
    private boolean mIsLastHst4G = false;
    private boolean mIsHstN4GY5G = false;
    private boolean mIsHstY4GY5G = false;
    private boolean mIsHstN5GY4G = false;
    private boolean mIsHstY5GY4G = false;
    private byte[] mLastResult = null;
    private long mLastCheckTime = 0;
    private boolean mCurrentHstState = false;
    private long mHstEnterTime = 0;
    private long mHstDurationTime = 0;
    private boolean mHstWifiFlag = false;
    private boolean mHstModemFlag = false;
    private long mLastHstWifiCheckTime = 0;
    private CollectionState mCollectionState = CollectionState.IDLE;
    private Map mLineDirections = new HashMap();
    private String mCurrentLines = "";
    private boolean mIsInHstStation = false;
    private String mLastStationName = "";
    private String mCurrentStationName = "";
    private String mCurrentForegroundPackageName = "";
    private String mLastForegroundPackageName = "";
    private long mLastForegroundChangeTime = 0;
    private Map foregroundPackageDurationsInTunnel = new HashMap();
    private String mLongestDurationPackageNameInTunnel = "";
    private long mLongestDurationInTunnel = 0;
    private final Map mStats = new ConcurrentHashMap();
    private boolean mSupportFiveG = false;

    enum CollectionState {
        IDLE,
        IN_STATION,
        BETWEEN_STATIONS
    }

    private HighSpeedTrainModeController() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mSignalStrength = new SignalStrength[i];
        this.mServiceState = new ServiceState[i];
        this.mFiveGInfo = new FiveGServiceInfo[i];
        this.mCollectedData = new CopyOnWriteArrayList();
        this.mSummaryData = new HashMap();
        this.mTempHstFlagTime = 0L;
        this.mTempHstStateTime = 0L;
        this.mModemHstStateTime = 0L;
        this.mModemNonHstStateTime = 0L;
        this.mHstFlagTime = 0L;
        this.mNonHstFlagTime = 0L;
        this.mLastModemHstState = 2;
        this.mModemHstStateChangeCount = 0;
        this.mValidCount = 0;
        this.mIsHstStateEnhanceCollecting = false;
        this.mHstStateEnhanceCount = 0;
        this.mTempHstStateEnhanceFlagTime = 0L;
        this.mHstStateEnhanceFlagTime = 0L;
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.HighSpeedTrainModeController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                HighSpeedTrainModeController.this.log("receive action".concat(action));
                if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    HighSpeedTrainModeController.this.mHandler.obtainMessage(4).sendToTarget();
                    return;
                }
                if (action.equalsIgnoreCase("qualcomm.intent.action.ACTION_HST_INFO_IND")) {
                    byte[] byteArrayExtra = intent.getByteArrayExtra("BROADCAST_CONTEXT");
                    if (byteArrayExtra == null || byteArrayExtra.length == 0) {
                        HighSpeedTrainModeController.this.log("error ind data error");
                        return;
                    } else {
                        HighSpeedTrainModeController.this.mHandler.obtainMessage(5, byteArrayExtra).sendToTarget();
                        return;
                    }
                }
                if (action.equals("highSpeedTrainModeController.intent.debugall")) {
                    HighSpeedTrainModeController.this.mHandler.obtainMessage(100, intent).sendToTarget();
                    return;
                }
                if (action.equals("highSpeedTrainModeController.intent.debugHstStateReport")) {
                    HighSpeedTrainModeController.this.mHandler.obtainMessage(101, Boolean.valueOf(intent.getBooleanExtra("hstStateReport", false))).sendToTarget();
                } else if ("com.android.phone.intent.action.HST_STATION_STATUS_CHANGED".equals(action)) {
                    HighSpeedTrainModeController.this.mHandler.obtainMessage(13, intent).sendToTarget();
                } else if (action.equals("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED")) {
                    HighSpeedTrainModeController.this.mHandler.obtainMessage(14, intent).sendToTarget();
                } else if ("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED".equals(action)) {
                    HighSpeedTrainModeController.this.mHandler.obtainMessage(3, intent).sendToTarget();
                }
            }
        };
        this.mEventListener = new BaseEvent.IEventListener() { // from class: com.android.phone.HighSpeedTrainModeController.2
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                ForegroundInfo foregroundInfo;
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    if (((DeviceStateEvent.DisplayEvent) baseEvent).isDefaultDisplay()) {
                        HighSpeedTrainModeController.this.mHandler.obtainMessage(6).sendToTarget();
                    }
                } else {
                    if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null) {
                        return;
                    }
                    HighSpeedTrainModeController.this.mHandler.sendMessageDelayed(HighSpeedTrainModeController.this.mHandler.obtainMessage(7, foregroundInfo), 1000L);
                }
            }
        };
        this.mQmsHstStateListener = new QmsClientBase.IHstStateListener() { // from class: com.android.phone.HighSpeedTrainModeController.3
            @Override // com.android.phone.QmsClientBase.IHstStateListener
            public void onHstStateReportChanged(int i2) {
                boolean z = i2 == 1;
                Intent intent = new Intent();
                intent.setAction("com.android.phone.intent.action.ENHANCE_HST_MODE_STATUS_CHANGED");
                intent.putExtra("mIsHstMode", z);
                HighSpeedTrainModeController.this.mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
                HighSpeedTrainModeController.this.log("onHstStateReportChanged modemHstState=" + z + " mCurrentHstState=" + HighSpeedTrainModeController.this.mCurrentHstState);
                if ((i2 == 0 || i2 == 2) && HighSpeedTrainModeController.this.mCurrentHstState) {
                    HighSpeedTrainModeController.this.mModemHstStateChangeCount++;
                    HighSpeedTrainModeController.this.setHstState(1);
                    if (!HighSpeedTrainModeController.this.mHandler.hasMessages(8)) {
                        HighSpeedTrainModeController.this.mHandler.sendMessageDelayed(HighSpeedTrainModeController.this.mHandler.obtainMessage(8), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                    }
                }
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                if ((i2 == 0 || i2 == 2) && HighSpeedTrainModeController.this.mLastModemHstState == 1) {
                    HighSpeedTrainModeController.this.mModemHstStateTime += jElapsedRealtime - HighSpeedTrainModeController.this.mTempHstStateTime;
                } else if (i2 == 1) {
                    HighSpeedTrainModeController.this.mModemNonHstStateTime += jElapsedRealtime - HighSpeedTrainModeController.this.mTempHstStateTime;
                }
                HighSpeedTrainModeController.this.mTempHstStateTime = jElapsedRealtime;
                HighSpeedTrainModeController.this.mLastModemHstState = i2;
            }

            @Override // com.android.phone.QmsClientBase.IHstStateListener
            public void onGetHstStateChanged(int i2, int i3) {
                HighSpeedTrainModeController.this.log("onGetHstStateChanged = " + i3);
                if (i3 == 1) {
                    HighSpeedTrainModeController.this.mValidCount++;
                }
            }
        };
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("HighSpeedTrainModeController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        HighSpeedTrainModeHandler highSpeedTrainModeHandler = new HighSpeedTrainModeHandler(this.mWorkThread.getLooper());
        this.mHandler = highSpeedTrainModeHandler;
        highSpeedTrainModeHandler.obtainMessage(1).sendToTarget();
    }

    public static synchronized HighSpeedTrainModeController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new HighSpeedTrainModeController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        this.mDdsPhoneId = getDefaultDataSlotId();
        this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
        mIsAiInfoSupport = MiuiPhoneUtils.isAiInfoSupported();
        if (MiuiPhoneUtils.isModemNetworkPerceptionSupported()) {
            this.mModemNetworkPerception = ModemNetworkPerception.getInstance();
        }
        registerReceiver();
        HighSpeedTrainStats.registerCollectSwitchListener(this);
        this.mSupportFiveG = MiuiPhoneUtils.isFiveGCapable();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mSignalStrength[i] = new SignalStrength();
            this.mServiceState[i] = new ServiceState();
            if (this.mSupportFiveG) {
                this.mFiveGInfo[i] = new FiveGServiceInfo();
            }
        }
        HighSpeedTrainModeHandler highSpeedTrainModeHandler = this.mHandler;
        highSpeedTrainModeHandler.sendMessageDelayed(highSpeedTrainModeHandler.obtainMessage(2), 90000L);
    }

    class HighSpeedTrainModeHandler extends Handler {
        HighSpeedTrainModeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            HighSpeedTrainModeController.this.log("msg.what = " + message.what);
            int i = message.what;
            if (i == 100) {
                HighSpeedTrainModeController.this.VDBG = true;
                return;
            }
            if (i != 101) {
                switch (i) {
                    case 1:
                        HighSpeedTrainModeController.this.init();
                        break;
                    case 2:
                        HighSpeedTrainModeController.this.processInitQms();
                        break;
                    case 3:
                        HighSpeedTrainModeController.this.mCurrentHstState = ((Intent) message.obj).getBooleanExtra("mIsHstMode", false);
                        HighSpeedTrainModeController.localLog("HstStateChange", "Hststate: " + HighSpeedTrainModeController.this.mCurrentHstState);
                        HighSpeedTrainModeController.this.processHstStateChange();
                        break;
                    case 4:
                        HighSpeedTrainModeController highSpeedTrainModeController = HighSpeedTrainModeController.this;
                        highSpeedTrainModeController.mDdsPhoneId = highSpeedTrainModeController.getDefaultDataSlotId();
                        break;
                    case 5:
                        byte[] bArr = (byte[]) message.obj;
                        if (!Arrays.equals(bArr, HighSpeedTrainModeController.this.mLastResult)) {
                            HighSpeedTrainModeController.this.processHstFlagState(bArr);
                        }
                        HighSpeedTrainModeController.this.mLastResult = bArr;
                        break;
                    case 6:
                        HighSpeedTrainModeController highSpeedTrainModeController2 = HighSpeedTrainModeController.this;
                        highSpeedTrainModeController2.mIsScreenOn = MiuiPhoneUtils.isScreenOn(highSpeedTrainModeController2.mContext);
                        break;
                    case 7:
                        HighSpeedTrainModeController.this.processForegroundInfoEvent((ForegroundInfo) message.obj);
                        HighSpeedTrainModeController.this.processForegroundInHstTunnel(false);
                        break;
                    case 8:
                        HighSpeedTrainModeController.this.getHstState();
                        break;
                    case 9:
                        if (HighSpeedTrainModeController.this.mCollectionState != CollectionState.IDLE) {
                            HighSpeedTrainModeController.this.captureNetworkData();
                            HighSpeedTrainModeController.this.scheduleNextDataCollection();
                        }
                        break;
                    case 10:
                        HighSpeedTrainModeController.this.processSignalStrengthChange(message.arg1, (SignalStrength) message.obj);
                        break;
                    case 11:
                        HighSpeedTrainModeController.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                        break;
                    case 12:
                        HighSpeedTrainModeController.this.handleDelayedBetweenStationsStart();
                        break;
                    case 13:
                        HighSpeedTrainModeController.this.processHstStationStatusChanged((Intent) message.obj);
                        break;
                    case 14:
                        Map map = (Map) ((Intent) message.obj).getSerializableExtra("mHitHstLineStatus");
                        if (map != null) {
                            HighSpeedTrainModeController.this.mLineDirections.clear();
                            HighSpeedTrainModeController.this.mLineDirections.putAll(map);
                            HighSpeedTrainModeController highSpeedTrainModeController3 = HighSpeedTrainModeController.this;
                            highSpeedTrainModeController3.mCurrentLines = highSpeedTrainModeController3.getCurrentHitLine();
                            HighSpeedTrainModeController.this.log("EVENT_HIT_HST_LINE_STATUS_CHANGED: lineDirections = " + map.toString());
                        }
                        break;
                }
                return;
            }
            boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
            HighSpeedTrainModeController.this.log("tempUpload = " + zBooleanValue);
            HighSpeedTrainModeController highSpeedTrainModeController4 = HighSpeedTrainModeController.this;
            if (zBooleanValue) {
                highSpeedTrainModeController4.mQmsHstStateListener.onHstStateReportChanged(1);
            } else {
                highSpeedTrainModeController4.mQmsHstStateListener.onHstStateReportChanged(2);
            }
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("qualcomm.intent.action.ACTION_HST_INFO_IND");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.HST_STATION_STATUS_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED");
        intentFilter.addAction("highSpeedTrainModeController.intent.debugall");
        intentFilter.addAction("highSpeedTrainModeController.intent.debugHstStateReport");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
        registerForegroundInfoListener();
        registerTelephonyInfoCallBack();
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mEventListener);
        registerHstStateCallback();
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
        public void onCellLocationChanged(int i, CellLocation cellLocation) {
        }

        TeleCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            HighSpeedTrainModeController.this.mHandler.obtainMessage(10, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            HighSpeedTrainModeController.this.mHandler.obtainMessage(11, i, 0, serviceState).sendToTarget();
        }
    }

    private void registerForegroundInfoListener() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mEventListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processHstFlagState(byte[] bArr) {
        try {
            try {
                localLog("processHstFlagState", Arrays.toString(bArr) + "," + bArr.length);
                ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
                byteBufferWrap.order(ByteOrder.nativeOrder());
                boolean z = false;
                int i = 0;
                boolean z2 = false;
                while (byteBufferWrap.hasRemaining()) {
                    byte b = byteBufferWrap.get();
                    if (i == 1 && b == 1) {
                        z = true;
                    } else if (i == 3 && b == 1) {
                        z2 = true;
                    }
                    boolean z3 = this.mIsLastHst4G;
                    if (!z3 && z2) {
                        this.mIsHstN4GY5G = true;
                    } else if (z3 && z2) {
                        this.mIsHstY4GY5G = true;
                    } else {
                        boolean z4 = this.mIsLastHst5G;
                        if (!z4 && z) {
                            this.mIsHstN5GY4G = true;
                        } else if (z4 && z) {
                            this.mIsHstY5GY4G = true;
                        } else {
                            this.mIsHstN4GY5G = false;
                            this.mIsHstY4GY5G = false;
                            this.mIsHstN5GY4G = false;
                            this.mIsHstY5GY4G = false;
                        }
                    }
                    i++;
                }
                if (this.mIsLastHst4G != z || this.mIsLastHst5G != z2) {
                    long jElapsedRealtime = SystemClock.elapsedRealtime();
                    updateHstStateEnhanceDuration(jElapsedRealtime);
                    if (this.mIsLastHst4G || this.mIsLastHst5G) {
                        this.mHstFlagTime += jElapsedRealtime - this.mTempHstFlagTime;
                    } else {
                        this.mNonHstFlagTime += jElapsedRealtime - this.mTempHstFlagTime;
                    }
                    this.mIsLastHst4G = z;
                    this.mIsLastHst5G = z2;
                    this.mTempHstFlagTime = jElapsedRealtime;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:13:0x001c  */
    /* JADX WARN: Code duplicated, block: B:20:0x002b  */
    public int getOperatorScore() {
        int iOperatorForDds = operatorForDds();
        int i = 0;
        if (this.mCurrentHstState && iOperatorForDds != 0) {
            if (this.mIsHstN4GY5G || isOn5GHstLine() || this.mIsHstN5GY4G) {
                i = 1;
            } else if (this.mIsHstY4GY5G) {
                if (1 == iOperatorForDds) {
                    i = 1;
                } else if (2 == iOperatorForDds || 3 == iOperatorForDds) {
                    i = 2;
                }
            } else if (this.mIsHstY5GY4G) {
                if (1 == iOperatorForDds) {
                    i = 2;
                } else if (2 == iOperatorForDds || 3 == iOperatorForDds) {
                    i = 1;
                }
            }
            log("getOperatorScore operatorScoreLevel = " + i);
        }
        return i;
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

    private boolean isOn5GHstLine() {
        if (this.mLineDirections.isEmpty() || !this.mLineDirections.containsKey("D6724")) {
            return false;
        }
        log("isOn5GHstLine jingzhang line");
        recordCount("on5GHstLine");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processInitQms() {
        registerHstStateCallback();
        logD("processInitQms");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHstStateChange() {
        if (this.mCurrentHstState) {
            processEnterHst();
        } else {
            processExitHst();
        }
    }

    private synchronized void processEnterHst() {
        this.mHstEnterTime = System.currentTimeMillis();
        this.mHstDurationTime = 0L;
        setHstState(1);
        updateHstNwInfo(this.mCurrentHstState);
        this.mTempHstFlagTime = SystemClock.elapsedRealtime();
        this.mTempHstStateTime = SystemClock.elapsedRealtime();
        clearHstStateStatistics();
        getHstState();
    }

    private void processExitHst() {
        this.mHstDurationTime = (System.currentTimeMillis() - this.mHstEnterTime) / 1000;
        setHstState(2);
        updateHstNwInfo(this.mCurrentHstState);
        if (MiuiPhoneUtils.isSNSV4SupportedByQcom()) {
            reportHstStateStatistics();
        } else {
            updateHstStateDuration();
            clearHstStateStatistics();
        }
        recordHSTStatus();
    }

    private String getDdsLteNrCid() {
        Phone phone;
        long nci;
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || (phone = MiuiPhoneUtils.getPhone(this.mDdsPhoneId)) == null || phone.getCurrentCellIdentity() == null) {
            return "";
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
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    private void updateHstNwInfo(boolean z) {
        if (MiuiTelephonyStatistic.getInstance() == null) {
            return;
        }
        if (!z) {
            MiuiTelephonyStatistic.getInstance().updateNwBandInfo(this.mDdsPhoneId, null, true);
            MiuiTelephonyStatistic.getInstance().updateHstArfcnInfo(this.mDdsPhoneId, null, true);
        }
        if (MiuiTelephonyStatistic.getInstance().mHstModemStats != null) {
            MiuiTelephonyStatistic.getInstance().mHstModemStats.updateHstState(z);
        }
    }

    public String getLongestDurationPackageNameInTunnel() {
        return TextUtils.isEmpty(this.mLongestDurationPackageNameInTunnel) ? this.mCurrentForegroundPackageName : this.mLongestDurationPackageNameInTunnel;
    }

    public long getLongestDurationInTunnel() {
        return this.mLongestDurationInTunnel;
    }

    public void clearHstForegroundPackageData() {
        this.foregroundPackageDurationsInTunnel.clear();
        this.mLongestDurationPackageNameInTunnel = "";
        this.mLongestDurationInTunnel = 0L;
        this.mLastForegroundPackageName = "";
        this.mLastForegroundChangeTime = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processForegroundInfoEvent(ForegroundInfo foregroundInfo) {
        this.mCurrentForegroundPackageName = foregroundInfo.mForegroundPackageName;
    }

    public void processForegroundInHstTunnel(boolean z) {
        ModemNetworkPerception modemNetworkPerception;
        if (z || ((modemNetworkPerception = this.mModemNetworkPerception) != null && modemNetworkPerception.isHstTunnel())) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (!this.mLastForegroundPackageName.isEmpty()) {
                final long j = (jCurrentTimeMillis - this.mLastForegroundChangeTime) / 1000;
                this.foregroundPackageDurationsInTunnel.compute(this.mLastForegroundPackageName, new BiFunction() { // from class: com.android.phone.HighSpeedTrainModeController$$ExternalSyntheticLambda0
                    @Override // java.util.function.BiFunction
                    public final Object apply(Object obj, Object obj2) {
                        return HighSpeedTrainModeController.m813$r8$lambda$TQh1PFf5AqyYSJvhVWWmx871M4(j, (String) obj, (Long) obj2);
                    }
                });
                if (((Long) this.foregroundPackageDurationsInTunnel.getOrDefault(this.mLastForegroundPackageName, 0L)).longValue() > this.mLongestDurationInTunnel) {
                    this.mLongestDurationInTunnel = ((Long) this.foregroundPackageDurationsInTunnel.get(this.mLastForegroundPackageName)).longValue();
                    this.mLongestDurationPackageNameInTunnel = this.mLastForegroundPackageName;
                }
            }
            this.mLastForegroundChangeTime = jCurrentTimeMillis;
            this.mLastForegroundPackageName = this.mCurrentForegroundPackageName;
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$TQh1PFf5AqyYSJvhVWWmx87-1M4, reason: not valid java name */
    public static /* synthetic */ Long m813$r8$lambda$TQh1PFf5AqyYSJvhVWWmx871M4(long j, String str, Long l) {
        if (l != null) {
            j += l.longValue();
        }
        return Long.valueOf(j);
    }

    private void recordHSTStatus() {
        HashMap map = new HashMap();
        map.put("hstState", Boolean.valueOf(this.mCurrentHstState));
        map.put("hstDuration", Long.valueOf(this.mHstDurationTime));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_hst", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHstStationStatusChanged(Intent intent) {
        String stringExtra = intent.getStringExtra("hst_station_name");
        boolean booleanExtra = intent.getBooleanExtra("hst_station_status", false);
        localLog("HstStationChange", stringExtra + "," + booleanExtra);
        if (booleanExtra) {
            handleStationEntry(stringExtra);
        } else {
            handleStationExit(stringExtra);
        }
    }

    private void handleStationEntry(String str) {
        this.mHandler.removeMessages(12);
        if (!TextUtils.isEmpty(this.mCurrentStationName)) {
            this.mLastStationName = this.mCurrentStationName;
        }
        this.mCurrentStationName = str;
        if (this.mCollectionState == CollectionState.BETWEEN_STATIONS) {
            processExitHstCollector();
        }
        this.mIsInHstStation = true;
        safeStartCollection(CollectionState.IN_STATION);
    }

    private void handleStationExit(String str) {
        if (TextUtils.equals(str, this.mCurrentStationName)) {
            this.mIsInHstStation = false;
            if (this.mCollectionState == CollectionState.IN_STATION) {
                processExitHstCollector();
            }
            if (!this.mLineDirections.isEmpty()) {
                scheduleDelayedEvent(12, 2000L);
            }
            this.mLastStationName = this.mCurrentStationName;
            this.mCurrentStationName = "";
        }
    }

    private void scheduleDelayedEvent(int i, long j) {
        this.mHandler.removeMessages(i);
        HighSpeedTrainModeHandler highSpeedTrainModeHandler = this.mHandler;
        highSpeedTrainModeHandler.sendMessageDelayed(highSpeedTrainModeHandler.obtainMessage(i), j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDelayedBetweenStationsStart() {
        if (this.mLineDirections.isEmpty() || this.mIsInHstStation) {
            return;
        }
        CollectionState collectionState = this.mCollectionState;
        CollectionState collectionState2 = CollectionState.BETWEEN_STATIONS;
        if (collectionState != collectionState2) {
            safeStartCollection(collectionState2);
        }
    }

    private void processEnterHstCollector() {
        this.mCollectedData.clear();
        scheduleInitialDataCollection();
    }

    private void processExitHstCollector() {
        CollectionState collectionState = this.mCollectionState;
        CollectionState collectionState2 = CollectionState.IDLE;
        try {
            if (collectionState == collectionState2) {
                log("processExitHstCollector, Not collecting, skip");
                return;
            }
            try {
                cancelDataCollection();
                if (!this.mCollectedData.isEmpty()) {
                    generateReport();
                    reportHSTSummary();
                } else {
                    log("No data collected, skipping report");
                }
                resetCollectionState();
                this.mCollectionState = collectionState2;
            } catch (Exception e) {
                log("processExitHstCollector: Exception during exit collection" + e);
                resetCollectionState();
                this.mCollectionState = CollectionState.IDLE;
            }
        } catch (Throwable th) {
            resetCollectionState();
            this.mCollectionState = CollectionState.IDLE;
            throw th;
        }
    }

    private void safeStartCollection(CollectionState collectionState) {
        if (this.mCollectionState != CollectionState.IDLE) {
            processExitHstCollector();
        }
        this.mCollectionState = collectionState;
        processEnterHstCollector();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void captureNetworkData() {
        CollectionState collectionState = this.mCollectionState;
        if (collectionState == CollectionState.IDLE) {
            return;
        }
        if (this.mCollectedData.size() < (collectionState == CollectionState.BETWEEN_STATIONS ? 1800 : 600) && MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && this.mDdsPhoneId < this.mServiceState.length && this.mDdsPhoneId < this.mSignalStrength.length) {
            ServiceState serviceState = this.mServiceState[this.mDdsPhoneId];
            SignalStrength signalStrength = this.mSignalStrength[this.mDdsPhoneId];
            this.mCollectedData.add(new NetworkSnapshot(System.currentTimeMillis(), fetchCellID(), fetchRSRP(signalStrength), fetchSNR(serviceState, signalStrength), fetchRAT(serviceState), fetchBand(serviceState), fetchARFCN(serviceState), fetchPLMN(serviceState), fetchforegroundPackage()));
        }
    }

    private void scheduleInitialDataCollection() {
        HighSpeedTrainModeHandler highSpeedTrainModeHandler = this.mHandler;
        highSpeedTrainModeHandler.sendMessage(highSpeedTrainModeHandler.obtainMessage(9));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleNextDataCollection() {
        HighSpeedTrainModeHandler highSpeedTrainModeHandler = this.mHandler;
        highSpeedTrainModeHandler.sendMessageDelayed(highSpeedTrainModeHandler.obtainMessage(9), MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    private void cancelDataCollection() {
        this.mHandler.removeMessages(9);
    }

    private void resetCollectionState() {
        this.mCollectedData.clear();
        this.mSummaryData.clear();
    }

    private boolean isInStationCollection() {
        return this.mCollectionState == CollectionState.IN_STATION;
    }

    private void generateReport() {
        if (this.mCollectedData.isEmpty()) {
            log("generateReport, mCollectedData is empty.");
            return;
        }
        String str = isInStationCollection() ? "IN_STATION" : "BETWEEN_STATIONS";
        String str2 = this.mCurrentStationName;
        String str3 = str2 != null ? str2 : "";
        String str4 = this.mCollectionState == CollectionState.IN_STATION ? str3 : this.mLastStationName;
        String str5 = TextUtils.isEmpty(this.mCurrentLines) ? "" : this.mCurrentLines;
        ReportPayload reportPayload = new ReportPayload(Utils.systemTimeToString(System.currentTimeMillis()), str5, str3, str4, str, new ArrayList(this.mCollectedData));
        this.mSummaryData.clear();
        int size = this.mCollectedData.size();
        long j = size > 0 ? ((NetworkSnapshot) this.mCollectedData.get(size - 1)).timestamp - ((NetworkSnapshot) this.mCollectedData.get(0)).timestamp : 0L;
        this.mSummaryData.put("reportTime", String.valueOf(reportPayload.reportTime));
        this.mSummaryData.put("durationMs", String.valueOf(j));
        this.mSummaryData.put("dataCount", String.valueOf(size));
        this.mSummaryData.put("hitLineStatus", str5);
        this.mSummaryData.put("stationInName", str3);
        this.mSummaryData.put("stationOutName", str4);
        this.mSummaryData.put("collectionType", str);
        addStatistics("rsrp", new Function() { // from class: com.android.phone.HighSpeedTrainModeController$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return String.valueOf(((HighSpeedTrainModeController.NetworkSnapshot) obj).getRsrp());
            }
        }, true);
        addStatistics("snr", new Function() { // from class: com.android.phone.HighSpeedTrainModeController$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return String.valueOf(((HighSpeedTrainModeController.NetworkSnapshot) obj).getSnr());
            }
        }, true);
        addStatistics("arfcn", new Function() { // from class: com.android.phone.HighSpeedTrainModeController$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return String.valueOf(((HighSpeedTrainModeController.NetworkSnapshot) obj).getArfcn());
            }
        }, true);
        addStatistics("rat", new Function() { // from class: com.android.phone.HighSpeedTrainModeController$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((HighSpeedTrainModeController.NetworkSnapshot) obj).getRat();
            }
        }, false);
        addStatistics("band", new Function() { // from class: com.android.phone.HighSpeedTrainModeController$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((HighSpeedTrainModeController.NetworkSnapshot) obj).getBand();
            }
        }, false);
        addStatistics("plmn", new Function() { // from class: com.android.phone.HighSpeedTrainModeController$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((HighSpeedTrainModeController.NetworkSnapshot) obj).getPlmn();
            }
        }, false);
        addStatistics("cellid", new Function() { // from class: com.android.phone.HighSpeedTrainModeController$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((HighSpeedTrainModeController.NetworkSnapshot) obj).getCellId();
            }
        }, false);
        addStatistics("foregroundPackage", new Function() { // from class: com.android.phone.HighSpeedTrainModeController$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((HighSpeedTrainModeController.NetworkSnapshot) obj).getForegroundPackage();
            }
        }, false);
    }

    private void addStatistics(String str, Function function, boolean z) {
        String string;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator it = this.mCollectedData.iterator();
        while (it.hasNext()) {
            String str2 = (String) function.apply((NetworkSnapshot) it.next());
            linkedHashMap.put(str2, Integer.valueOf(((Integer) linkedHashMap.getOrDefault(str2, 0)).intValue() + 1));
        }
        if (z) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry entry : linkedHashMap.entrySet()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((String) entry.getKey());
                sb.append(":");
                sb.append(entry.getValue());
            }
            string = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder("{");
            for (Map.Entry entry2 : linkedHashMap.entrySet()) {
                sb2.append((String) entry2.getKey());
                sb2.append(":");
                sb2.append(entry2.getValue());
                sb2.append(",");
            }
            if (sb2.length() > 1) {
                sb2.deleteCharAt(sb2.length() - 1);
            }
            sb2.append("}");
            string = sb2.toString();
        }
        this.mSummaryData.put(str, string);
    }

    private String fetchCellID() {
        if (MiuiPhoneUtils.isMPDevice() || Build.IS_INTERNATIONAL_BUILD) {
            return "";
        }
        return getDdsLteNrCid();
    }

    private int fetchRSRP(SignalStrength signalStrength) {
        return getDbm(signalStrength);
    }

    private int getDbm(SignalStrength signalStrength) {
        if (signalStrength != null) {
            return signalStrength.getDbm();
        }
        return Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSignalStrengthChange(int i, SignalStrength signalStrength) {
        if (signalStrength == null || !MiuiPhoneUtils.isValidatePhoneId(i)) {
            return;
        }
        SignalStrength[] signalStrengthArr = this.mSignalStrength;
        if (signalStrengthArr[i] == signalStrength) {
            return;
        }
        signalStrengthArr[i] = new SignalStrength(signalStrength);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        log("processServiceStateChangeEvent phoneId=" + i);
        if (serviceState != null && MiuiPhoneUtils.isValidatePhoneId(i)) {
            this.mServiceState[i] = new ServiceState(serviceState);
        }
    }

    private int fetchSNR(ServiceState serviceState, SignalStrength signalStrength) {
        if (serviceState != null && signalStrength != null) {
            int voiceNetworkType = serviceState.getVoiceNetworkType();
            if (Utils.isLte(voiceNetworkType)) {
                return signalStrength.getLteRssnr();
            }
            if (Utils.isNr(voiceNetworkType) && this.mFiveGInfo[this.mDdsPhoneId] != null) {
                return this.mFiveGInfo[this.mDdsPhoneId].getSnr();
            }
        }
        return -1;
    }

    private String fetchRAT(ServiceState serviceState) {
        if (serviceState == null) {
            return "";
        }
        return ServiceState.rilRadioTechnologyToString(getDataRat(serviceState));
    }

    private int getDataRat(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getRilDataRadioTechnology();
    }

    private String fetchBand(ServiceState serviceState) {
        if (serviceState == null) {
            return "";
        }
        return Utils.getBand(this.mDdsPhoneId, serviceState);
    }

    private int fetchARFCN(ServiceState serviceState) {
        if (serviceState == null) {
            return -1;
        }
        return serviceState.getChannelNumber();
    }

    private String fetchPLMN(ServiceState serviceState) {
        String operatorNumeric = serviceState != null ? serviceState.getOperatorNumeric() : "";
        if (TextUtils.isEmpty(operatorNumeric)) {
            operatorNumeric = this.mServiceState[this.mDdsPhoneId].getOperatorNumeric();
        }
        if (TextUtils.isEmpty(operatorNumeric)) {
            operatorNumeric = TelephonyManager.getDefault().getSimOperatorForSlot(this.mDdsPhoneId);
        }
        return TextUtils.isEmpty(operatorNumeric) ? "NoSim" : operatorNumeric;
    }

    private String fetchforegroundPackage() {
        return this.mCurrentForegroundPackageName;
    }

    public class NetworkSnapshot {
        public final int arfcn;
        public final String band;
        public final String cellId;
        public final String foregroundPackage;
        public final String plmn;
        public final String rat;
        public final int rsrp;
        public final double snr;
        public final long timestamp;

        public NetworkSnapshot(long j, String str, int i, double d, String str2, String str3, int i2, String str4, String str5) {
            this.timestamp = j;
            this.cellId = str;
            this.rsrp = i;
            this.snr = d;
            this.rat = str2;
            this.band = str3;
            this.arfcn = i2;
            this.plmn = str4;
            this.foregroundPackage = str5;
        }

        public String getCellId() {
            return this.cellId;
        }

        public int getRsrp() {
            return this.rsrp;
        }

        public double getSnr() {
            return this.snr;
        }

        public String getRat() {
            return this.rat;
        }

        public String getBand() {
            return this.band;
        }

        public int getArfcn() {
            return this.arfcn;
        }

        public String getPlmn() {
            return this.plmn;
        }

        public String getForegroundPackage() {
            return this.foregroundPackage;
        }
    }

    public class ReportPayload {
        public final String collectionType;
        public final String hitLineStatus;
        public final String reportTime;
        public final List snapshots;
        public final String stationInName;
        public final String stationOutName;

        public ReportPayload(String str, String str2, String str3, String str4, String str5, List list) {
            this.reportTime = str;
            this.hitLineStatus = TextUtils.isEmpty(str2) ? "UNKNOWN" : str2;
            this.stationInName = TextUtils.isEmpty(str3) ? "UNKNOWN" : str3;
            this.stationOutName = TextUtils.isEmpty(str4) ? "UNKNOWN" : str4;
            this.collectionType = TextUtils.isEmpty(str5) ? "UNKNOWN" : str5;
            this.snapshots = list;
        }
    }

    private void reportHSTSummary() {
        HashMap map = this.mSummaryData;
        if (map == null || map.isEmpty()) {
            log("reportHSTSummary, mSummaryData is empty.");
        } else {
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_hst_collect", new HashMap(this.mSummaryData));
        }
    }

    private void recordCount(String str) {
        Integer num = (Integer) this.mStats.get(str);
        log("recordCount key=" + str + ", value=" + num);
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
        MiStatInterfaceUtil.recordObjectEvent("HST", "event_hst_singalmap", map2);
    }

    private synchronized void updateHstStateDuration() {
        try {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            int i = this.mLastModemHstState;
            if (i == 0 || i == 2) {
                this.mModemNonHstStateTime += jElapsedRealtime - this.mTempHstStateTime;
            } else if (i == 1) {
                this.mModemHstStateTime += jElapsedRealtime - this.mTempHstStateTime;
            }
            if (this.mIsLastHst4G || this.mIsLastHst5G) {
                this.mHstFlagTime += jElapsedRealtime - this.mTempHstFlagTime;
            } else {
                this.mNonHstFlagTime += jElapsedRealtime - this.mTempHstFlagTime;
            }
            this.mTempHstStateTime = jElapsedRealtime;
            this.mTempHstFlagTime = jElapsedRealtime;
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.android.phone.statistics.HighSpeedTrainStats.CollectSwitchListener
    public synchronized void onCollectSwitchChanged(boolean z) {
        try {
            if (z) {
                startHstStateEnhanceStatistics();
            } else {
                reportHstStateEnhanceStatistics();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void startHstStateEnhanceStatistics() {
        this.mHstStateEnhanceFlagTime = 0L;
        this.mTempHstStateEnhanceFlagTime = SystemClock.elapsedRealtime();
        this.mHstStateEnhanceCount = 0;
        this.mIsHstStateEnhanceCollecting = true;
    }

    private void reportHstStateEnhanceStatistics() {
        if (this.mIsHstStateEnhanceCollecting) {
            updateHstStateEnhanceDuration(SystemClock.elapsedRealtime());
            HighSpeedTrainStats.recordHstStateEnhance(this.mHstStateEnhanceFlagTime, this.mHstStateEnhanceCount);
            this.mHstStateEnhanceFlagTime = 0L;
            this.mTempHstStateEnhanceFlagTime = 0L;
            this.mHstStateEnhanceCount = 0;
            this.mIsHstStateEnhanceCollecting = false;
        }
    }

    private synchronized void updateHstStateEnhanceDuration(long j) {
        try {
            if (this.mIsHstStateEnhanceCollecting) {
                long j2 = this.mTempHstStateEnhanceFlagTime;
                if (j2 > 0) {
                    if (this.mIsLastHst4G || this.mIsLastHst5G) {
                        this.mHstStateEnhanceFlagTime += j - j2;
                    }
                    this.mTempHstStateEnhanceFlagTime = j;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void reportHstStateStatistics() {
        updateHstStateDuration();
        HashMap map = new HashMap();
        map.put("modem_hst_state", String.valueOf(this.mModemHstStateTime / 1000));
        map.put("modem_non_hst_state", String.valueOf(this.mModemNonHstStateTime / 1000));
        map.put("hst_flag", String.valueOf(this.mHstFlagTime / 1000));
        map.put("non_hst_flag", String.valueOf(this.mNonHstFlagTime / 1000));
        map.put("hst_line", this.mCurrentLines);
        map.put("hst_count", Integer.valueOf(this.mValidCount));
        map.put("modem_hst_state_count", Integer.valueOf(this.mModemHstStateChangeCount));
        MiStatInterfaceUtil.recordObjectEvent("HST", "event_hst_state_statistics", map);
        HashMap map2 = new HashMap();
        map2.putAll(map);
        localLog("event_hst_state_statistics", map2.toString());
        clearHstStateStatistics();
    }

    private void clearHstStateStatistics() {
        logD("mDurModemHstState " + this.mModemHstStateTime + " mDurModemNonHstState " + this.mModemNonHstStateTime + "mDurHstFlag " + this.mHstFlagTime + " mDurNonHstFlag " + this.mNonHstFlagTime + " mValidCount " + this.mValidCount + " mModemHstStateChangeCount " + this.mModemHstStateChangeCount + " mHstDurationTime " + this.mHstDurationTime);
        this.mModemHstStateTime = 0L;
        this.mModemNonHstStateTime = 0L;
        this.mHstFlagTime = 0L;
        this.mNonHstFlagTime = 0L;
        this.mValidCount = 0;
        this.mModemHstStateChangeCount = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getHstState() {
        logD("getHstState");
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().getHstState("hst");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setHstState(int i) {
        try {
            logD("setHstState " + i);
            if (this.mIsHstStateEnhanceCollecting) {
                this.mHstStateEnhanceCount++;
            }
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setHstState(i, "hst");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void registerHstStateCallback() {
        try {
            if (this.mQmsHstStateListener != null) {
                QmsClientBase qmsClientBase = QmsClientBase.getInstance();
                this.mQmsClient = qmsClientBase;
                if (qmsClientBase != null) {
                    qmsClientBase.registerHstStateListener(this.mQmsHstStateListener);
                    logD("registerHstStateCallback");
                }
            }
        } catch (Exception e) {
            log("Exception:" + e);
        }
    }

    public String getCurrentHitLine() {
        Map map = this.mLineDirections;
        if (map == null || map.isEmpty()) {
            return "";
        }
        return TextUtils.join(",", this.mLineDirections.keySet());
    }

    public static void localLog(String str, String str2) {
        localLog("HighSpeedTrainModeController", str, str2);
    }

    public static void localLog(String str, String str2, String str3) {
        String str4 = str + "." + str2;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(60));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d(str, str2 + " " + str3);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("HighSpeedTrainModeController dump start------");
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
        printWriter.println("HighSpeedTrainModeController dump end------");
        printWriter.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("HighSpeedTrainModeController", str);
    }

    private void logD(String str) {
        if (this.VDBG) {
            Log.d("HighSpeedTrainModeController", str);
        }
    }
}
