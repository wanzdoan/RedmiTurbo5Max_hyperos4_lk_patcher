package com.android.phone.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncResult;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
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
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.phone.FiveGCloudController;
import com.android.phone.HighSpeedTrainModeController;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.NetworkModeRecognition;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.CountableHandler;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.ModemUtils;
import com.android.phone.utils.NetworkSpeedTracker;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.ot.pubsub.b.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class ModemNetworkPerception {
    private static boolean VDBG = false;
    private static ModemNetworkPerception mInstance;
    private Context mContext;
    private final BaseEvent.IEventListener mEventListener;
    public CountableHandler mHandler;
    private HighSpeedTrainModeController mHstModeController;
    private boolean mIsOperaterQosAccelerateSupported;
    private boolean mIsScreenOn;
    private List mModemPercepListenerList;
    private HashMap mModemTypeConfigMap;
    private final ModemUtils mModemUtils;
    private NetworkModeRecognition mNetworkModeRecognition;
    private NetworkSpeedTracker mSpeedTracker;
    ContentObserver mSubwayStateObserver;
    private HandlerThread mWorkThread;
    private static List mSmartNetworkWhiteListForVideo = new ArrayList();
    private static List mQosWhiteList = new ArrayList();
    private boolean mLastSubwayState = false;
    private boolean mLastQosSwitch = false;
    private boolean mIsSmartNetworkWhiteListForground = false;
    private boolean mIsQosWhiteListForground = false;
    private boolean mIsRegSmartNetworkSchedule = false;
    private boolean mIsRegSubwayRecognition = false;
    private boolean mIsRegHsrRecognition = false;
    private int mLastQosAbnormalPci = -1;
    private int mCurSubwayPlatformCount = 0;
    private int mCurHstTunnelCount = 0;
    private ModemRule mCurModemRule = new ModemRule();
    private boolean mLastCellHstTunnel = false;
    private boolean mLastCellSubwayPlatform = false;
    BooleanFixedSizeQueue mHstTunnelStateQueue = new BooleanFixedSizeQueue(this, 3);
    private TeleCallBack mTeleCallBack = null;
    private String mEnterHstTunnelCid = "";
    private long mEnterHstTunnelTime = 0;
    private boolean mIsHstTunnel = false;
    private int mLastRsrpValue = -1;
    private int mLastRsrpLevel = -1;
    private long mLastRsrpLevelChangeTime = 0;
    private Map rsrpLevelDurationsInTunnel = new HashMap();
    private int mLongestDurationRsrpLevelInTunnel = -1;
    private long mLongestDurationInTunnel = 0;
    private boolean mIsLteEmmTimerStart = false;
    private int mLteEmmTimerSlotId = -1;
    private long mLastAirPlaneModeTime = 0;
    private ServiceState[] mServiceState = new ServiceState[MiuiPhoneUtils.PHONE_COUNT];
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.network.ModemNetworkPerception.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            if (intent.getAction().equals("com.android.phone.CELLULAR_MODEM_LINK_STATUS")) {
                ModemNetworkPerception.VDBG = true;
                CountableHandler countableHandler = ModemNetworkPerception.this.mHandler;
                countableHandler.sendMessage(countableHandler.obtainMessage(1, intent));
                return;
            }
            if (intent.getAction().equals("com.android.phone.ACTION_SCENE_SCENE_PERCEPTION")) {
                ModemNetworkPerception.VDBG = true;
                CountableHandler countableHandler2 = ModemNetworkPerception.this.mHandler;
                countableHandler2.sendMessage(countableHandler2.obtainMessage(3, intent));
            } else if (intent.getAction().equals("com.android.phone.LTE_EMM_TIMER_START_TEST")) {
                ModemNetworkPerception.VDBG = true;
                CountableHandler countableHandler3 = ModemNetworkPerception.this.mHandler;
                countableHandler3.sendMessage(countableHandler3.obtainMessage(11, intent));
            } else if (intent.getAction().equals("com.xiaomi.hypercomm.intent.action.SWITCH_QOS_CONGESTION_DETECT")) {
                ModemNetworkPerception.VDBG = true;
                CountableHandler countableHandler4 = ModemNetworkPerception.this.mHandler;
                countableHandler4.sendMessage(countableHandler4.obtainMessage(15, intent));
            } else if (intent.getAction().equals("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED")) {
                CountableHandler countableHandler5 = ModemNetworkPerception.this.mHandler;
                countableHandler5.sendMessage(countableHandler5.obtainMessage(8, intent));
            }
        }
    };

    public interface IModemNetworkPerception {
        default void onDlCongestionAbnormal(int i, int i2) {
        }

        default void onDlHighBlerAbnormal(int i, int i2) {
        }

        default void onLteHstTunnelRecognize(int i, int i2) {
        }

        default void onSaHstTunnelRecognize(int i, int i2) {
        }

        void onSaSubwayPlatformRecognize(int i, int i2);

        default void onUlCongestionAbnormal(int i, int i2) {
        }

        default void onUlHighBlerAbnormal(int i, int i2) {
        }
    }

    private boolean isLteNwType(int i) {
        return i == 13 || i == 19;
    }

    private boolean isNrNwType(int i) {
        return i == 20 || i == 30;
    }

    public static synchronized ModemNetworkPerception getInstance() {
        try {
            if (mInstance == null) {
                mInstance = new ModemNetworkPerception();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private ModemNetworkPerception() {
        this.mNetworkModeRecognition = null;
        this.mModemTypeConfigMap = new HashMap();
        this.mIsOperaterQosAccelerateSupported = false;
        this.mSubwayStateObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.network.ModemNetworkPerception.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                int subwayStateChange = ModemNetworkPerception.this.getSubwayStateChange();
                if (subwayStateChange != -1) {
                    ModemNetworkPerception.this.mHandler.obtainMessage(7, Integer.valueOf(subwayStateChange)).sendToTarget();
                }
            }
        };
        BaseEvent.IEventListener iEventListener = new BaseEvent.IEventListener() { // from class: com.android.phone.network.ModemNetworkPerception.3
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                ForegroundInfo foregroundInfo;
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    if (((DeviceStateEvent.DisplayEvent) baseEvent).isDefaultDisplay()) {
                        ModemNetworkPerception.this.mHandler.obtainMessage(14).sendToTarget();
                    }
                } else {
                    if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null) {
                        return;
                    }
                    CountableHandler countableHandler = ModemNetworkPerception.this.mHandler;
                    countableHandler.sendMessageDelayed(countableHandler.obtainMessage(9, foregroundInfo), 1000L);
                }
            }
        };
        this.mEventListener = iEventListener;
        log("ModemNetworkPerception init");
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("ModemNetworkPerception", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new PerceptionHandler(this.mWorkThread.getLooper());
        this.mModemUtils = ModemUtils.init(this.mContext);
        this.mModemPercepListenerList = new ArrayList();
        registerAllEvents();
        this.mNetworkModeRecognition = NetworkModeRecognition.getInstance();
        initSmartNetworkWhiteList();
        initQosHstStationWhiteList();
        HashMap networkRuleConfig = ModemRuleParser.parseNetworkRuleConfig();
        this.mModemTypeConfigMap = networkRuleConfig;
        logD(networkRuleConfig.toString());
        initSceneRecognitionState();
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", iEventListener);
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mHstModeController = HighSpeedTrainModeController.getInstance();
        }
        this.mSpeedTracker = NetworkSpeedTracker.getInstance();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mServiceState[i] = new ServiceState();
        }
        this.mIsOperaterQosAccelerateSupported = MiuiPhoneUtils.isOperatorQosAccelerateSupported();
        this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
    }

    private void registerAllEvents() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.CELLULAR_MODEM_LINK_STATUS");
        intentFilter.addAction("com.android.phone.ACTION_SCENE_SCENE_PERCEPTION");
        intentFilter.addAction("com.android.phone.LTE_EMM_TIMER_START_TEST");
        intentFilter.addAction("com.xiaomi.hypercomm.intent.action.SWITCH_QOS_CONGESTION_DETECT");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("network_mode_state"), false, this.mSubwayStateObserver);
        registerForegroundInfoListener();
    }

    private void initSmartNetworkWhiteList() {
        mSmartNetworkWhiteListForVideo.addAll(getOptWhiteList("modem_perception_optimization_white_list_default"));
        log("initSmartNetworkWhiteList:" + mSmartNetworkWhiteListForVideo.toString());
    }

    private void initQosHstStationWhiteList() {
        mQosWhiteList.addAll(getOptWhiteList("key_qos_optimization_white_list_default"));
        log("initQosHstStationWhiteList:" + mQosWhiteList.toString());
    }

    public List getOptWhiteList(String str) {
        if (str == null) {
            return new ArrayList();
        }
        List stringArray = HyperOSCustFeatureResolve.getStringArray(str);
        return stringArray != null ? stringArray : new ArrayList();
    }

    class TeleCallBack implements TelephonyStatAdapter.ICallBack {
        TeleCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            if (i == DefaultSimManager.getDefaultDataSlotId() && ModemNetworkPerception.this.mIsHstTunnel) {
                ModemNetworkPerception.this.processSignalStrengthEvent(i, signalStrength, false);
            }
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            ModemNetworkPerception.this.mHandler.obtainMessage(13, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            if (i == DefaultSimManager.getDefaultDataSlotId()) {
                ModemNetworkPerception.this.mHandler.obtainMessage(10, i, 0, cellLocation).sendToTarget();
            }
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

    private void unregisterTelephonyInfoCallBack() {
        TeleCallBack teleCallBack = this.mTeleCallBack;
        if (teleCallBack != null) {
            TelephonyStatAdapter.unregisterCallBack(teleCallBack);
            this.mTeleCallBack = null;
        }
    }

    private void registerForegroundInfoListener() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mEventListener);
    }

    public void registerModemNetworkPerception(IModemNetworkPerception iModemNetworkPerception) {
        List list;
        if (iModemNetworkPerception == null || (list = this.mModemPercepListenerList) == null) {
            return;
        }
        synchronized (list) {
            this.mModemPercepListenerList.add(iModemNetworkPerception);
        }
    }

    public void unRegisterModemNetworkPerception(IModemNetworkPerception iModemNetworkPerception) {
        List list;
        if (iModemNetworkPerception == null || (list = this.mModemPercepListenerList) == null) {
            return;
        }
        synchronized (list) {
            this.mModemPercepListenerList.remove(iModemNetworkPerception);
        }
    }

    private void notifyModemNetworkPerception(int i, int i2, int i3) {
        if (this.mModemPercepListenerList == null) {
            return;
        }
        log("notifyModemNetworkPerception event:" + i + " subId:" + i2 + " pci:" + i3 + " mLastQosSwitch:" + this.mLastQosSwitch + " mLastQosAbnormalPci:" + this.mLastQosAbnormalPci);
        if (this.mIsOperaterQosAccelerateSupported && this.mLastQosSwitch && this.mIsQosWhiteListForground) {
            if (6 != getQciState()) {
                log("notifyModemNetworkPerception send broadcast to hyperComm");
                sendQosSwitchBroadcast(i, i2, i3);
                this.mLastQosAbnormalPci = i3;
                return;
            }
            return;
        }
        synchronized (this.mModemPercepListenerList) {
            try {
                for (IModemNetworkPerception iModemNetworkPerception : this.mModemPercepListenerList) {
                    if (i == 15) {
                        iModemNetworkPerception.onUlHighBlerAbnormal(i2, i3);
                        iModemNetworkPerception.onDlHighBlerAbnormal(i2, i3);
                        iModemNetworkPerception.onUlCongestionAbnormal(i2, i3);
                        iModemNetworkPerception.onDlCongestionAbnormal(i2, i3);
                    } else if (i == 12) {
                        iModemNetworkPerception.onUlHighBlerAbnormal(i2, i3);
                        iModemNetworkPerception.onDlHighBlerAbnormal(i2, i3);
                    } else if (i == 8) {
                        iModemNetworkPerception.onDlHighBlerAbnormal(i2, i3);
                    } else if (i == 4) {
                        iModemNetworkPerception.onUlHighBlerAbnormal(i2, i3);
                    } else if (i == 3) {
                        iModemNetworkPerception.onUlCongestionAbnormal(i2, i3);
                        iModemNetworkPerception.onDlCongestionAbnormal(i2, i3);
                    } else if (i == 2) {
                        iModemNetworkPerception.onDlCongestionAbnormal(i2, i3);
                    } else if (i == 1) {
                        iModemNetworkPerception.onUlCongestionAbnormal(i2, i3);
                    } else {
                        log("notifyModemNetworkPerception not match event");
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void sendQosSwitchBroadcast(int i, int i2, int i3) {
        Intent intent = new Intent("com.android.phone.intent.action.QOS_CONGESTION_ABNOMAL");
        intent.putExtra(a.b, i);
        intent.putExtra("subId", i2);
        intent.putExtra("pci", i3);
        intent.setPackage("com.xiaomi.hypercomm");
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyModemScenePerception(int i, int i2) {
        if (this.mModemPercepListenerList == null) {
            return;
        }
        log("notifyModemScenePerception" + i + i2);
        processModemSceneSafely(i2);
        synchronized (this.mModemPercepListenerList) {
            try {
                for (IModemNetworkPerception iModemNetworkPerception : this.mModemPercepListenerList) {
                    if (i2 == 0) {
                        iModemNetworkPerception.onSaSubwayPlatformRecognize(i, i2);
                    } else if (i2 == 1) {
                        iModemNetworkPerception.onSaHstTunnelRecognize(i, i2);
                    } else if (i2 == 2) {
                        iModemNetworkPerception.onLteHstTunnelRecognize(i, i2);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    class PerceptionHandler extends CountableHandler {
        PerceptionHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.phone.utils.CountableHandler, android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            ModemNetworkPerception.logD("handleMessage event:" + message.what);
            switch (message.what) {
                case 1:
                    Intent intent = (Intent) message.obj;
                    if (intent != null) {
                        ModemRule modemRule = new ModemRule();
                        int intExtra = intent.getIntExtra("switchOn", -1);
                        modemRule.setEvent(intent.getIntExtra(a.b, -1));
                        modemRule.setTimer(intent.getIntExtra("timer", -1));
                        modemRule.setUlGrantRatio(intent.getIntExtra("ul_grant_ratio", -1));
                        modemRule.setDlGrantRatio(intent.getIntExtra("dl_grant_ratio", -1));
                        modemRule.setUlSpeed(intent.getIntExtra("ul_speed", -1));
                        modemRule.setDlSpeed(intent.getIntExtra("dl_speed", -1));
                        modemRule.setUlBler(intent.getIntExtra("ul_bler", -1));
                        modemRule.setDlBler(intent.getIntExtra("dl_bler", -1));
                        int intExtra2 = intent.getIntExtra("slotId", -1);
                        ModemNetworkPerception.log("EVENT_SMART_NETWORK_SCHEDULE_TEST : " + intExtra + " slotId:" + intExtra2 + " modemRule:" + modemRule);
                        ModemNetworkPerception.this.setSmartNetworkSchedule(intExtra, intExtra2, modemRule);
                    }
                    break;
                case 2:
                    if (ModemNetworkPerception.this.mIsRegSmartNetworkSchedule) {
                        ModemNetworkPerception.this.parseSmartNetworkScheduleInfo((AsyncResult) message.obj);
                    }
                    break;
                case 3:
                    Intent intent2 = (Intent) message.obj;
                    if (intent2 != null) {
                        int intExtra3 = intent2.getIntExtra("type", -1);
                        int intExtra4 = intent2.getIntExtra("test_notify", -1);
                        ModemNetworkPerception.log("EVENT_MODEM_SCENE_PERCEPTION_TEST V1 type: " + intExtra3 + " test_notify:" + intExtra4);
                        if (intExtra3 != -1) {
                            ModemNetworkPerception.this.setModemSceneReception(intExtra3);
                        }
                        if (intExtra4 != -1) {
                            ModemNetworkPerception.this.notifyModemScenePerception(0, intExtra4);
                            ModemNetworkPerception.logD("EVENT_MODEM_SCENE_PERCEPTION_TEST mHstTunnelStateQueue=" + ModemNetworkPerception.this.mHstTunnelStateQueue.toString());
                        }
                    }
                    break;
                case 4:
                    if (ModemNetworkPerception.this.mIsRegSubwayRecognition) {
                        ModemNetworkPerception.this.parseModemSaSubwayOrHstInfo((AsyncResult) message.obj, 0);
                    }
                    break;
                case 5:
                    if (ModemNetworkPerception.this.mIsRegHsrRecognition) {
                        ModemNetworkPerception.this.parseModemSaSubwayOrHstInfo((AsyncResult) message.obj, 1);
                    }
                    break;
                case 6:
                    if (ModemNetworkPerception.this.mIsRegHsrRecognition) {
                        ModemNetworkPerception.this.parseModemLteSubwayOrHstInfo((AsyncResult) message.obj, 2);
                    }
                    break;
                case 7:
                    int iIntValue = ((Integer) message.obj).intValue();
                    ModemNetworkPerception.log("EVENT_SUBWAY_STATE_CHANGED state:" + iIntValue);
                    if (iIntValue == 1) {
                        ModemNetworkPerception.this.setModemSceneReception(1);
                    } else if (iIntValue == 0) {
                        ModemNetworkPerception.this.setModemSceneReception(0);
                    }
                    break;
                case 8:
                    Intent intent3 = (Intent) message.obj;
                    if (intent3 != null) {
                        boolean booleanExtra = intent3.getBooleanExtra("mIsHstMode", false);
                        ModemNetworkPerception.log("EVENT_HST_STATE_CHANGED state:" + booleanExtra);
                        ModemNetworkPerception modemNetworkPerception = ModemNetworkPerception.this;
                        if (booleanExtra) {
                            modemNetworkPerception.setModemSceneReception(2);
                        } else {
                            modemNetworkPerception.setModemSceneReception(0);
                        }
                    }
                    break;
                case 9:
                    ModemNetworkPerception.this.processForegroundInfoEvent((ForegroundInfo) message.obj);
                    break;
                case 10:
                    ModemNetworkPerception.this.processCellLocationChangeEvent(message.arg1, (CellLocation) message.obj);
                    break;
                case 11:
                    if (ModemNetworkPerception.this.mIsLteEmmTimerStart) {
                        if (MiuiTelephonyStatistic.getInstance() != null) {
                            MiuiTelephonyStatistic.getInstance().recordLteEmmTimerStartStatistic();
                        }
                        ModemNetworkPerception.this.processLteEmmTimerStart();
                    }
                    break;
                case 12:
                    ModemNetworkPerception.this.processLteEmmTimerStartDelay();
                    break;
                case 13:
                    ModemNetworkPerception.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                    break;
                case 14:
                    boolean zIsScreenOn = MiuiPhoneUtils.isScreenOn(ModemNetworkPerception.this.mContext);
                    if (ModemNetworkPerception.this.mIsScreenOn != zIsScreenOn) {
                        ModemNetworkPerception.this.mIsScreenOn = zIsScreenOn;
                        ModemNetworkPerception.this.processScreenStateChanged();
                    }
                    break;
                case 15:
                    boolean booleanExtra2 = ((Intent) message.obj).getBooleanExtra("isOpen", false);
                    ModemNetworkPerception.logD("EVENT_SWITCH_QOS_CONGESTION_DETECT isQosDetectOpen=" + booleanExtra2 + " mLastQosSwitch:" + ModemNetworkPerception.this.mLastQosSwitch);
                    if (booleanExtra2 != ModemNetworkPerception.this.mLastQosSwitch) {
                        if (!booleanExtra2) {
                            ModemNetworkPerception.this.mIsQosWhiteListForground = false;
                        }
                        ModemNetworkPerception.this.mLastQosSwitch = booleanExtra2;
                        break;
                    }
                    break;
            }
        }
    }

    private void processModemSceneSafely(int i) {
        log("processModemSceneSafely type: " + i + " mCurSubwayPlatformCount: " + this.mCurSubwayPlatformCount + " mCurHstTunnelCount: " + this.mCurHstTunnelCount);
        if (i == 0) {
            this.mLastCellSubwayPlatform = true;
            int i2 = this.mCurSubwayPlatformCount;
            if (i2 >= 3) {
                setModemSceneReception(0);
                this.mCurSubwayPlatformCount = 0;
                return;
            } else {
                this.mCurSubwayPlatformCount = i2 + 1;
                return;
            }
        }
        if (i == 1 || i == 2) {
            this.mLastCellHstTunnel = true;
            int i3 = this.mCurHstTunnelCount;
            if (i3 >= 50) {
                setModemSceneReception(0);
                this.mCurHstTunnelCount = 0;
                exitHstTunnel();
            } else {
                this.mCurHstTunnelCount = i3 + 1;
                enterHstTunnel();
            }
        }
    }

    private void enterHstTunnel() {
        if (getHstState() == 4) {
            log("enterHstTunnel");
            this.mIsHstTunnel = true;
            Settings.Global.putInt(this.mContext.getContentResolver(), "modem_scene_recognition_state", 3);
            processEnterHstTunnel();
        }
    }

    private void exitHstTunnel() {
        if (getHstState() == 3) {
            log("exitHstTunnel");
            this.mIsHstTunnel = false;
            Settings.Global.putInt(this.mContext.getContentResolver(), "modem_scene_recognition_state", 4);
            processExitHstTunnel();
        }
    }

    private void processEnterHstTunnel() {
        log("processEnterHstTunnel");
        this.mEnterHstTunnelTime = SystemClock.elapsedRealtime();
        updateHstTunnelRat(true);
        updateEnterHstTunnelCellID();
        clearRsrpLevelData();
        processSignalStrengthInHstTunnel(false);
        NetworkSpeedTracker networkSpeedTracker = this.mSpeedTracker;
        if (networkSpeedTracker != null) {
            networkSpeedTracker.clearHstTunnelPeakRateBps();
        }
        HighSpeedTrainModeController highSpeedTrainModeController = this.mHstModeController;
        if (highSpeedTrainModeController != null) {
            highSpeedTrainModeController.clearHstForegroundPackageData();
            this.mHstModeController.processForegroundInHstTunnel(false);
        }
    }

    private void processExitHstTunnel() {
        log("processExitHstTunnel");
        updateHstTunnelRat(false);
        recordExitHstTunnelStatistic();
    }

    private void updateEnterHstTunnelCellID() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId)) {
            this.mEnterHstTunnelCid = getDdsLteNrCid(defaultDataSlotId);
        }
    }

    private String getDdsLteNrCid(int i) {
        Phone phone;
        long nci;
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || (phone = MiuiPhoneUtils.getPhone(i)) == null || phone.getCurrentCellIdentity() == null) {
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

    private void recordExitHstTunnelStatistic() {
        long jElapsedRealtime = (SystemClock.elapsedRealtime() - this.mEnterHstTunnelTime) / 1000;
        HashMap map = new HashMap();
        map.put("duration", String.valueOf(jElapsedRealtime));
        map.put("CellIdentity", this.mEnterHstTunnelCid);
        FiveGCloudController fiveGCloudController = FiveGCloudController.getInstance();
        String province = "";
        String city = fiveGCloudController != null ? fiveGCloudController.getCity() : "";
        if (fiveGCloudController != null) {
            province = fiveGCloudController.getProvince();
        }
        map.put("c", city);
        map.put("p", province);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_exit_hst_tunnel", map);
        clearEnterHstTunnelData();
    }

    private void clearEnterHstTunnelData() {
        this.mEnterHstTunnelTime = 0L;
        this.mEnterHstTunnelCid = "";
    }

    private void updateHstTunnelRat(boolean z) {
        if (MiuiTelephonyStatistic.getInstance() == null || MiuiTelephonyStatistic.getInstance().mHstTunnelModemStats == null) {
            return;
        }
        MiuiTelephonyStatistic.getInstance().mHstTunnelModemStats.updateHstTunnelState(z);
    }

    private void initSceneRecognitionState() {
        if (getHstState() != 4) {
            Settings.Global.putInt(this.mContext.getContentResolver(), "modem_scene_recognition_state", 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processCellLocationChangeEvent(int i, CellLocation cellLocation) {
        try {
            if (MiuiPhoneUtils.isValidatePhoneId(i) && cellLocation != null && !cellLocation.isEmpty() && (this.mIsRegHsrRecognition || this.mIsRegSubwayRecognition)) {
                logD("processCellLocationChangeEvent phoneId=" + i + " mLastCellHstTunnel=" + this.mLastCellHstTunnel + " mCurHstTunnelCount=" + this.mCurHstTunnelCount + " mLastCellSubwayPlatform=" + this.mLastCellSubwayPlatform + " mCurSubwayPlatformCount=" + this.mCurSubwayPlatformCount);
                if (this.mIsRegSubwayRecognition) {
                    if (!this.mLastCellSubwayPlatform) {
                        this.mCurSubwayPlatformCount = 0;
                    }
                    this.mLastCellSubwayPlatform = false;
                } else if (this.mIsRegHsrRecognition) {
                    this.mHstTunnelStateQueue.offer(this.mLastCellHstTunnel);
                    this.mLastCellHstTunnel = false;
                    boolean allAnd = this.mHstTunnelStateQueue.getAllAnd();
                    if (allAnd) {
                        logD("processCellLocationChangeEvent phoneId=" + i + " isExitTunnel=" + allAnd + "mHstTunnelStateQueue" + this.mHstTunnelStateQueue.toString());
                        exitHstTunnel();
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private int getHstState() {
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "modem_scene_recognition_state", 4);
        logD("getHstState state" + i);
        return i;
    }

    public boolean isHstTunnel() {
        return this.mIsHstTunnel;
    }

    private void sendSmartNetworkScheduleRequest(ByteBuffer byteBuffer) {
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            return;
        }
        ByteBuffer byteBufferOnHookCommonMsgSyncBySub = modemUtils.onHookCommonMsgSyncBySub(146, byteBuffer.array(), 0);
        if (byteBufferOnHookCommonMsgSyncBySub == null) {
            log("sendSmartNetworkScheduleRequest: onHookCommonMsgSyncBySub return null");
            return;
        }
        log("sendSmartNetworkScheduleRequest : " + byteBufferOnHookCommonMsgSyncBySub.getInt());
    }

    private void sendModemSceneReceptionRequest(ByteBuffer byteBuffer) {
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            return;
        }
        ByteBuffer byteBufferOnHookCommonMsgSyncBySub = modemUtils.onHookCommonMsgSyncBySub(147, byteBuffer.array(), 0);
        if (byteBufferOnHookCommonMsgSyncBySub == null) {
            log("sendModemSceneReceptionRequest: onHookCommonMsgSyncBySub return null");
            return;
        }
        log("sendModemSceneReceptionRequest : " + byteBufferOnHookCommonMsgSyncBySub.getInt());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSmartNetworkSchedule(int i, int i2, ModemRule modemRule) {
        if (this.mModemUtils == null || modemRule == null) {
            return;
        }
        log("setSmartNetworkSchedule : " + i + " slotId:" + i2 + " modemRule:" + modemRule.toString());
        if (i == 1 && this.mIsRegSmartNetworkSchedule) {
            log("setSmartNetworkSchedule type: already regSmartNetworkSchedule");
            return;
        }
        if (i == 0 && !this.mIsRegSmartNetworkSchedule) {
            log("setSmartNetworkSchedule type: already unRegSmartNetworkSchedule");
            return;
        }
        try {
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[20]);
            byteBufferWrap.order(ByteOrder.nativeOrder());
            byteBufferWrap.putInt(20);
            byteBufferWrap.put((byte) i);
            byteBufferWrap.put((byte) i2);
            byteBufferWrap.put((byte) modemRule.getEvent());
            byteBufferWrap.put((byte) modemRule.getTimer());
            byteBufferWrap.put((byte) modemRule.getUlGrantRatio());
            byteBufferWrap.put((byte) modemRule.getDlGrantRatio());
            byteBufferWrap.putInt(modemRule.getUlSpeed());
            byteBufferWrap.putInt(modemRule.getDlSpeed());
            byteBufferWrap.put((byte) modemRule.getUlBler());
            byteBufferWrap.put((byte) modemRule.getDlBler());
            sendSmartNetworkScheduleRequest(byteBufferWrap);
            if (i == 1) {
                this.mIsRegSmartNetworkSchedule = true;
                this.mModemUtils.regSmartNetworkSchedule(i2, this.mHandler, 2, null);
            } else if (i == 0) {
                this.mIsRegSmartNetworkSchedule = false;
                this.mModemUtils.unRegSmartNetworkSchedule();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setModemSceneReception(int i) {
        if (this.mModemUtils == null) {
            return;
        }
        if ((i == 1 && this.mIsRegSubwayRecognition) || (i == 2 && this.mIsRegHsrRecognition)) {
            log("setModemSceneReception type: already regSubwayAndHsrRecognition");
            return;
        }
        if (i == 0 && !this.mIsRegSubwayRecognition && !this.mIsRegHsrRecognition) {
            log("setModemSceneReception type: already unRegSubwayAndHsrRecognition");
            return;
        }
        int defaultDataSlotId = getDefaultDataSlotId();
        if (!MiuiPhoneUtils.isMobileDataConnected(defaultDataSlotId) && i != 0) {
            log("setModemSceneReception skip: data disconnected");
            return;
        }
        log("setModemSceneReception type: " + i + " dataSlot" + defaultDataSlotId);
        try {
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[5]);
            byteBufferWrap.order(ByteOrder.nativeOrder());
            byteBufferWrap.putInt(5);
            byteBufferWrap.put((byte) i);
            sendModemSceneReceptionRequest(byteBufferWrap);
            if (i == 1) {
                this.mIsRegSubwayRecognition = true;
                this.mModemUtils.regSubwayAndHsrRecognition(0, defaultDataSlotId, this.mHandler, 4, null);
                registerTelephonyInfoCallBack();
                return;
            }
            if (i == 2) {
                this.mIsRegHsrRecognition = true;
                this.mModemUtils.regSubwayAndHsrRecognition(1, defaultDataSlotId, this.mHandler, 5, null);
                this.mModemUtils.regSubwayAndHsrRecognition(2, defaultDataSlotId, this.mHandler, 6, null);
                registerTelephonyInfoCallBack();
                return;
            }
            if (i == 0) {
                if (this.mIsRegSubwayRecognition) {
                    this.mIsRegSubwayRecognition = false;
                    this.mModemUtils.unRegSubwayAndHsrRecognition(0);
                    this.mCurSubwayPlatformCount = 0;
                    this.mLastCellSubwayPlatform = false;
                }
                if (this.mIsRegHsrRecognition) {
                    this.mIsRegHsrRecognition = false;
                    this.mModemUtils.unRegSubwayAndHsrRecognition(1);
                    this.mModemUtils.unRegSubwayAndHsrRecognition(2);
                    this.mHstTunnelStateQueue.clear();
                    this.mCurHstTunnelCount = 0;
                    this.mLastCellHstTunnel = false;
                }
                unregisterTelephonyInfoCallBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLteEmmTimerStart(int i) {
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            return;
        }
        if (i == 0 && !this.mIsLteEmmTimerStart) {
            int defaultDataSlotId = getDefaultDataSlotId();
            this.mLteEmmTimerSlotId = defaultDataSlotId;
            if (MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) && this.mModemUtils.regLteEmmTimerStart(this.mLteEmmTimerSlotId, this.mHandler, 11, null)) {
                this.mIsLteEmmTimerStart = true;
                return;
            }
            return;
        }
        if (i == 1 && this.mIsLteEmmTimerStart && modemUtils.unRegLteEmmTimerStart()) {
            this.mIsLteEmmTimerStart = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseSmartNetworkScheduleInfo(AsyncResult asyncResult) {
        try {
            log("parseSmartNetworkScheduleInfo");
            if (asyncResult == null) {
                log("parseSmartNetworkScheduleInfo: onHookCommonMsgSyncBySub return null");
                return;
            }
            ByteBuffer byteBufferWrap = ByteBuffer.wrap((byte[]) asyncResult.result);
            byteBufferWrap.order(ByteOrder.nativeOrder());
            int i = byteBufferWrap.getInt();
            logD("version:" + i + " remaining:" + byteBufferWrap.remaining());
            byte b = byteBufferWrap.get();
            logD("event:" + ((int) b) + " remaining:" + byteBufferWrap.remaining());
            Short shValueOf = Short.valueOf(byteBufferWrap.getShort());
            logD("pci:" + shValueOf + " remaining:" + byteBufferWrap.remaining());
            int i2 = byteBufferWrap.getInt();
            logD("freq_band:" + i2 + " remaining:" + byteBufferWrap.remaining());
            int i3 = byteBufferWrap.getInt();
            logD("subId:" + i3 + " remaining:" + byteBufferWrap.remaining());
            log("version:" + i + "event:" + ((int) b) + "pci:" + shValueOf + "freq_band:" + i2 + " remaining:" + byteBufferWrap.remaining());
            notifyModemNetworkPerception(b, i3, shValueOf.shortValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseModemSaSubwayOrHstInfo(AsyncResult asyncResult, int i) {
        try {
            log("parseModemSaSubwayOrHstInfo type:" + i);
            if (asyncResult == null) {
                log("parseModemSaSubwayOrHstInfo: onHookCommonMsgSyncBySub return null");
                return;
            }
            ByteBuffer byteBufferWrap = ByteBuffer.wrap((byte[]) asyncResult.result);
            byteBufferWrap.order(ByteOrder.nativeOrder());
            int i2 = byteBufferWrap.getInt();
            logD("version:" + i2 + " remaining:" + byteBufferWrap.remaining());
            short s = byteBufferWrap.getShort();
            logD("mcc:" + ((int) s) + " remaining:" + byteBufferWrap.remaining());
            short s2 = byteBufferWrap.getShort();
            logD("mcc:" + ((int) s) + " remaining:" + byteBufferWrap.remaining());
            int i3 = byteBufferWrap.getInt();
            logD("tac:" + i3 + " remaining:" + byteBufferWrap.remaining());
            long j = byteBufferWrap.getLong();
            logD("cellId:" + j + " remaining:" + byteBufferWrap.remaining());
            int i4 = byteBufferWrap.getInt();
            logD("dl_earfcn:" + i4 + " remaining:" + byteBufferWrap.remaining());
            short s3 = byteBufferWrap.getShort();
            logD("pci:" + ((int) s3) + " remaining:" + byteBufferWrap.remaining());
            int i5 = byteBufferWrap.getInt();
            logD("band:" + i5 + " remaining:" + byteBufferWrap.remaining());
            byte b = byteBufferWrap.get();
            logD("scs:" + ((int) b) + " remaining:" + byteBufferWrap.remaining());
            int i6 = byteBufferWrap.getInt();
            logD("subId:" + i6 + " remaining:" + byteBufferWrap.remaining());
            log("parseModemSaSubwayOrHstInfo version:" + i2 + " mcc:" + ((int) s) + " mnc:" + ((int) s2) + " tac:" + i3 + " cellId:" + j + " dl_earfcn:" + i4 + " pci:" + ((int) s3) + " band:" + i5 + " scs:" + ((int) b) + " subId:" + i6);
            notifyModemScenePerception(i6, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseModemLteSubwayOrHstInfo(AsyncResult asyncResult, int i) {
        try {
            log("parseModemLteSubwayOrHstInfo type:" + i);
            if (asyncResult == null) {
                log("parseModemLteSubwayOrHstInfo: onHookCommonMsgSyncBySub return null");
                return;
            }
            ByteBuffer byteBufferWrap = ByteBuffer.wrap((byte[]) asyncResult.result);
            byteBufferWrap.order(ByteOrder.nativeOrder());
            byte b = byteBufferWrap.get();
            logD("version:" + ((int) b) + " remaining:" + byteBufferWrap.remaining());
            short s = byteBufferWrap.getShort();
            logD("mcc:" + ((int) s) + " remaining:" + byteBufferWrap.remaining());
            short s2 = byteBufferWrap.getShort();
            logD("mcc:" + ((int) s) + " remaining:" + byteBufferWrap.remaining());
            short s3 = byteBufferWrap.getShort();
            logD("tac:" + ((int) s3) + " remaining:" + byteBufferWrap.remaining());
            int i2 = byteBufferWrap.getInt();
            logD("cellId:" + i2 + " remaining:" + byteBufferWrap.remaining());
            int i3 = byteBufferWrap.getInt();
            logD("dl_earfcn:" + i3 + " remaining:" + byteBufferWrap.remaining());
            short s4 = byteBufferWrap.getShort();
            logD("pci:" + ((int) s4) + " remaining:" + byteBufferWrap.remaining());
            int i4 = byteBufferWrap.getInt();
            logD("band:" + i4 + " remaining:" + byteBufferWrap.remaining());
            int i5 = byteBufferWrap.getInt();
            logD("subId:" + i5 + " remaining:" + byteBufferWrap.remaining());
            log("parseModemLteSubwayOrHstInfo version:" + ((int) b) + " mcc:" + ((int) s) + " mnc:" + ((int) s2) + " tac:" + ((int) s3) + " cellId:" + i2 + " dl_earfcn:" + i3 + " pci:" + ((int) s4) + " band:" + i4 + " subId:" + i5);
            notifyModemScenePerception(i5, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processForegroundInfoEvent(ForegroundInfo foregroundInfo) {
        String str = foregroundInfo.mForegroundPackageName;
        logD("processForegroundInfoEvent qosSupport:" + this.mIsOperaterQosAccelerateSupported + " mLastQosSwitch:" + this.mLastQosSwitch + " qosWhiteList:" + this.mIsQosWhiteListForground);
        if (this.mIsOperaterQosAccelerateSupported && this.mLastQosSwitch) {
            if (isQosWhiteList(str) && !this.mIsQosWhiteListForground) {
                log("processForegroundInfoEvent enter qosApp");
                this.mIsQosWhiteListForground = true;
                this.mCurModemRule = setSmartNetworkScheduleByApp("qosAccelerateApp");
                return;
            } else {
                if (isQosWhiteList(str) || !this.mIsQosWhiteListForground) {
                    return;
                }
                this.mIsQosWhiteListForground = false;
                log("processForegroundInfoEvent exit qosApp");
                ModemRule modemRule = this.mCurModemRule;
                if (modemRule != null) {
                    setSmartNetworkSchedule(0, 0, modemRule);
                    this.mCurModemRule = null;
                    return;
                }
                return;
            }
        }
        if (isSmartNetworkWhiteListForVideo(str) && !this.mIsSmartNetworkWhiteListForground) {
            log("processForegroundInfoEvent enter smartNetwork app");
            this.mIsSmartNetworkWhiteListForground = true;
            this.mCurModemRule = setSmartNetworkScheduleByApp("shortVideo");
        } else {
            if (isSmartNetworkWhiteListForVideo(str) || !this.mIsSmartNetworkWhiteListForground) {
                return;
            }
            this.mIsSmartNetworkWhiteListForground = false;
            log("processForegroundInfoEvent exit smartNetwork app");
            ModemRule modemRule2 = this.mCurModemRule;
            if (modemRule2 != null) {
                setSmartNetworkSchedule(0, 0, modemRule2);
                this.mCurModemRule = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processScreenStateChanged() {
        if (this.mIsScreenOn) {
            logD("processScreenStateChanged on");
            setLteEmmTimerStart(0);
        } else {
            logD("processScreenStateChanged off");
            setLteEmmTimerStart(1);
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
    public void processLteEmmTimerStart() {
        if (this.mHandler.hasMessages(12)) {
            this.mHandler.removeMessageWithCount(12);
        }
        CountableHandler countableHandler = this.mHandler;
        countableHandler.sendMessageDelayed(countableHandler.obtainMessage(12), 30000L);
    }

    public int getQciState() {
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "key_qos_qci_state", 1);
        log("getQciState:" + i);
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLteEmmTimerStartDelay() {
        ServiceState serviceState;
        int defaultDataSlotId = getDefaultDataSlotId();
        if (!MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) || (serviceState = this.mServiceState[defaultDataSlotId]) == null) {
            return;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        long jCurrentTimeMillis = System.currentTimeMillis();
        Phone phone = MiuiPhoneUtils.getPhone(defaultDataSlotId);
        log("processLteEmmTimerStartDelay " + dataNetworkType + " " + this.mLastAirPlaneModeTime);
        if (isNeedToAirPlaneMode(dataNetworkType, jCurrentTimeMillis, defaultDataSlotId, phone)) {
            MiuiDataControllerImpl.Processor.recoveryDataByLpm(phone);
            this.mLastAirPlaneModeTime = jCurrentTimeMillis;
            this.mLteEmmTimerSlotId = -1;
            if (MiuiTelephonyStatistic.getInstance() != null) {
                MiuiTelephonyStatistic.getInstance().recordLteEmmTimerAirPlaneOnStatistic();
            }
        }
    }

    private boolean isNeedToAirPlaneMode(int i, long j, int i2, Phone phone) {
        if (this.mServiceState[i2] == null || isWifiConnect(phone) || i2 != this.mLteEmmTimerSlotId || isLteNrNwType(i) || !isLteNrNwMode(i2) || MiuiPhoneUtils.isPhoneInCall() || PhoneConstants.State.IDLE != phone.getState() || this.mServiceState[i2].getRoaming()) {
            return false;
        }
        long j2 = this.mLastAirPlaneModeTime;
        return j - j2 >= 21600000 || j2 == 0;
    }

    private boolean isLteNrNwMode(int i) {
        int preferredNwModeFromDb = getPreferredNwModeFromDb(i);
        return (preferredNwModeFromDb == 0 || preferredNwModeFromDb == 1 || preferredNwModeFromDb == 2 || preferredNwModeFromDb == 3 || preferredNwModeFromDb == 5 || preferredNwModeFromDb == 6 || preferredNwModeFromDb == 7 || preferredNwModeFromDb == 13 || preferredNwModeFromDb == 14 || preferredNwModeFromDb == 16 || preferredNwModeFromDb == 18 || preferredNwModeFromDb == 21) ? false : true;
    }

    private boolean isLteNrNwType(int i) {
        return isLteNwType(i) || isNrNwType(i);
    }

    private int getPreferredNwModeFromDb(int i) {
        return ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
    }

    private boolean isWifiConnect(Phone phone) {
        NetworkInfo networkInfo = ((ConnectivityManager) phone.getContext().getSystemService("connectivity")).getNetworkInfo(1);
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        logD("isWifiConnect.");
        return true;
    }

    private ModemRule setSmartNetworkScheduleByApp(String str) {
        int defaultDataSlotId = getDefaultDataSlotId();
        if (!MiuiPhoneUtils.isMobileDataConnected(defaultDataSlotId)) {
            log("setSmartNetworkScheduleByApp skip: data disconnected");
            return null;
        }
        logD("getModemRuleByApplication type:" + str);
        List<ModemRule> list = (List) this.mModemTypeConfigMap.get(str);
        if (list == null) {
            log("getModemRuleByApplication: modemRulesList is null");
            return null;
        }
        ModemRule modemRule = new ModemRule();
        int i = 0;
        for (ModemRule modemRule2 : list) {
            logD("getModemRuleByApplication rule:" + modemRule2);
            if (modemRule2.isSwitchOn()) {
                if (isValidProperty(Integer.valueOf(modemRule2.getUlGrantRatio())) && isValidProperty(Integer.valueOf(modemRule2.getDlGrantRatio()))) {
                    i += 3;
                    modemRule.setDlGrantRatio(modemRule2.getDlGrantRatio());
                    modemRule.setUlGrantRatio(modemRule2.getUlGrantRatio());
                    modemRule.setDlSpeed(modemRule2.getDlSpeed());
                    modemRule.setUlSpeed(modemRule2.getUlSpeed());
                } else if (isValidProperty(Integer.valueOf(modemRule2.getDlGrantRatio()))) {
                    i += 2;
                    modemRule.setDlGrantRatio(modemRule2.getDlGrantRatio());
                    modemRule.setDlSpeed(modemRule2.getDlSpeed());
                } else if (isValidProperty(Integer.valueOf(modemRule2.getUlGrantRatio()))) {
                    i++;
                    modemRule.setUlGrantRatio(modemRule2.getUlGrantRatio());
                    modemRule.setUlSpeed(modemRule2.getUlSpeed());
                }
                if (isValidProperty(Integer.valueOf(modemRule2.getUlBler())) && isValidProperty(Integer.valueOf(modemRule2.getDlBler()))) {
                    i += 12;
                    modemRule.setDlBler(modemRule2.getDlBler());
                    modemRule.setUlBler(modemRule2.getUlBler());
                } else if (isValidProperty(Integer.valueOf(modemRule2.getUlBler()))) {
                    i += 4;
                    modemRule.setUlBler(modemRule2.getUlBler());
                } else if (isValidProperty(Integer.valueOf(modemRule2.getDlBler()))) {
                    i += 8;
                    modemRule.setDlBler(modemRule2.getDlBler());
                }
                modemRule.setEvent(i);
                logD(modemRule.toString());
                if (isValidProperty(modemRule2.getWeakRsrp()) || isValidProperty(modemRule2.getWeakSnr())) {
                    modemRule.setWeakRsrp(modemRule2.getWeakRsrp());
                    modemRule.setWeakSnr(modemRule2.getWeakSnr());
                }
            }
        }
        setSmartNetworkSchedule(1, defaultDataSlotId, modemRule);
        return modemRule;
    }

    private boolean isValidProperty(Object obj) {
        if (obj instanceof Boolean) {
            return true;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue() != -1;
        }
        if (obj instanceof int[]) {
            for (int i : (int[]) obj) {
                if (i != Integer.MIN_VALUE) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSmartNetworkWhiteListForVideo(String str) {
        if (str == null || mSmartNetworkWhiteListForVideo.size() == 0) {
            return false;
        }
        Iterator it = mSmartNetworkWhiteListForVideo.iterator();
        while (it.hasNext()) {
            if (str.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    public boolean isQosWhiteList(String str) {
        if (str == null || mQosWhiteList.size() == 0) {
            return false;
        }
        Iterator it = mQosWhiteList.iterator();
        while (it.hasNext()) {
            if (str.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSubwayStateChange() {
        if (this.mNetworkModeRecognition == null) {
            return -1;
        }
        boolean zIsSubwayStartStopStateUpdate = this.mNetworkModeRecognition.isSubwayStartStopStateUpdate(Settings.Global.getInt(this.mContext.getContentResolver(), "network_mode_state", 0) & 3);
        if (this.mLastSubwayState == zIsSubwayStartStopStateUpdate) {
            return -1;
        }
        log("getSubwayState mLastSubwayState:" + this.mLastSubwayState + ", subwayState:" + zIsSubwayStartStopStateUpdate);
        this.mLastSubwayState = zIsSubwayStartStopStateUpdate;
        return zIsSubwayStartStopStateUpdate ? 1 : 0;
    }

    private int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    public void processSignalStrengthInHstTunnel(boolean z) {
        int defaultDataSlotId;
        Phone phone;
        if ((z || this.mIsHstTunnel) && (phone = MiuiPhoneUtils.getPhone((defaultDataSlotId = getDefaultDataSlotId()))) != null && MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId)) {
            processSignalStrengthEvent(defaultDataSlotId, phone.getSignalStrength(), z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSignalStrengthEvent(int i, SignalStrength signalStrength, boolean z) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && signalStrength != null) {
            int ssrsrpBin = -1;
            for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
                if (cellSignalStrength instanceof CellSignalStrengthLte) {
                    int rsrp = ((CellSignalStrengthLte) cellSignalStrength).getRsrp();
                    if (rsrp != Integer.MAX_VALUE) {
                        this.mLastRsrpValue = rsrp;
                    }
                    ssrsrpBin = Utils.getRsrpBin(this.mLastRsrpValue);
                } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                    int ssRsrp = ((CellSignalStrengthNr) cellSignalStrength).getSsRsrp();
                    if (ssRsrp != Integer.MAX_VALUE) {
                        this.mLastRsrpValue = ssRsrp;
                    }
                    ssrsrpBin = Utils.getSsrsrpBin(this.mLastRsrpValue);
                }
                processRsrpLevelInHstTunnel(ssrsrpBin, z);
            }
            return;
        }
        log("processSignalStrengthEvent-invalid phone id!");
    }

    private void processRsrpLevelInHstTunnel(int i, boolean z) {
        if (z || (this.mIsHstTunnel && this.mLastRsrpLevel != i)) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            int i2 = this.mLastRsrpLevel;
            if (i2 != -1) {
                final long j = (jCurrentTimeMillis - this.mLastRsrpLevelChangeTime) / 1000;
                this.rsrpLevelDurationsInTunnel.compute(Integer.valueOf(i2), new BiFunction() { // from class: com.android.phone.network.ModemNetworkPerception$$ExternalSyntheticLambda0
                    @Override // java.util.function.BiFunction
                    public final Object apply(Object obj, Object obj2) {
                        return ModemNetworkPerception.$r8$lambda$1cEJBQrhQ60pp5xnCMM5Pq5_a1Q(j, (Integer) obj, (Long) obj2);
                    }
                });
                if (((Long) this.rsrpLevelDurationsInTunnel.getOrDefault(Integer.valueOf(this.mLastRsrpLevel), 0L)).longValue() > this.mLongestDurationInTunnel) {
                    int i3 = this.mLastRsrpLevel;
                    this.mLongestDurationRsrpLevelInTunnel = i3;
                    this.mLongestDurationInTunnel = ((Long) this.rsrpLevelDurationsInTunnel.get(Integer.valueOf(i3))).longValue();
                }
            }
            this.mLastRsrpLevelChangeTime = jCurrentTimeMillis;
            this.mLastRsrpLevel = i;
        }
    }

    public static /* synthetic */ Long $r8$lambda$1cEJBQrhQ60pp5xnCMM5Pq5_a1Q(long j, Integer num, Long l) {
        if (l != null) {
            j += l.longValue();
        }
        return Long.valueOf(j);
    }

    private void clearRsrpLevelData() {
        this.rsrpLevelDurationsInTunnel.clear();
        this.mLongestDurationRsrpLevelInTunnel = -1;
        this.mLongestDurationInTunnel = 0L;
        this.mLastRsrpLevel = -1;
        this.mLastRsrpLevelChangeTime = 0L;
    }

    public void recordHstTunnel(String str, String str2, long j) {
        if (j == 0) {
            clearHstTunnelData();
            return;
        }
        HashMap map = new HashMap();
        int defaultDataSlotId = getDefaultDataSlotId();
        if (MiuiPhoneUtils.getPhone(defaultDataSlotId) == null || !MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId)) {
            return;
        }
        map.put("Network", str);
        String strBeforeUnderscore = beforeUnderscore(str2);
        map.put("Rat", strBeforeUnderscore);
        map.put("duration", String.valueOf(j));
        if (!"0".equals(strBeforeUnderscore)) {
            map.put("rsrpLevel", String.valueOf(this.mLongestDurationRsrpLevelInTunnel));
            map.put("rsrpLevelDuration", String.valueOf(this.mLongestDurationInTunnel));
            HighSpeedTrainModeController highSpeedTrainModeController = this.mHstModeController;
            if (highSpeedTrainModeController != null) {
                map.put("appPackageName", highSpeedTrainModeController.getLongestDurationPackageNameInTunnel());
                map.put("appPackageNameDuration", Long.valueOf(this.mHstModeController.getLongestDurationInTunnel()));
            }
            NetworkSpeedTracker networkSpeedTracker = this.mSpeedTracker;
            if (networkSpeedTracker != null) {
                map.put("ULPeakRate", String.valueOf(networkSpeedTracker.getHstTunnelPeakUlRateBps()));
                map.put("DLPeakRate", String.valueOf(this.mSpeedTracker.getHstTunnelPeakDlRateBps()));
            }
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_hst_tunnel", map);
        clearHstTunnelData();
    }

    private String beforeUnderscore(String str) {
        if (str == null) {
            return null;
        }
        int iIndexOf = str.indexOf("_");
        return iIndexOf == -1 ? str : str.substring(0, iIndexOf);
    }

    private void clearHstTunnelData() {
        clearRsrpLevelData();
        processSignalStrengthInHstTunnel(false);
        HighSpeedTrainModeController highSpeedTrainModeController = this.mHstModeController;
        if (highSpeedTrainModeController != null) {
            highSpeedTrainModeController.clearHstForegroundPackageData();
            this.mHstModeController.processForegroundInHstTunnel(false);
        }
        NetworkSpeedTracker networkSpeedTracker = this.mSpeedTracker;
        if (networkSpeedTracker != null) {
            networkSpeedTracker.clearHstTunnelPeakRateBps();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("ModemNetworkPerception", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logD(String str) {
        if (VDBG) {
            Log.d("ModemNetworkPerception", str);
        }
    }

    public class BooleanFixedSizeQueue {
        private final int maxSize;
        private final ArrayDeque queue;

        public BooleanFixedSizeQueue(ModemNetworkPerception modemNetworkPerception, int i) {
            this.maxSize = i;
            this.queue = new ArrayDeque(i);
        }

        public boolean offer(boolean z) {
            if (this.queue.size() >= this.maxSize) {
                this.queue.pollFirst();
            }
            this.queue.offer(Boolean.valueOf(z));
            return true;
        }

        public static /* synthetic */ boolean $r8$lambda$9z67tRMPIfSjmCp2sENM6bz8L8M(Boolean bool) {
            return !bool.booleanValue();
        }

        public boolean getAllAnd() {
            return this.queue.stream().allMatch(new Predicate() { // from class: com.android.phone.network.ModemNetworkPerception$BooleanFixedSizeQueue$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ModemNetworkPerception.BooleanFixedSizeQueue.$r8$lambda$9z67tRMPIfSjmCp2sENM6bz8L8M((Boolean) obj);
                }
            });
        }

        public void clear() {
            this.queue.clear();
        }

        public String toString() {
            return this.queue.toString();
        }
    }
}
