package com.android.phone.statistics.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.telephony.AccessNetworkUtils;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.view.OrientationEventListener;
import com.android.ims.ImsCall;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.FiveGCloudUtils;
import com.android.phone.FiveGManagerBase;
import com.android.phone.FiveGServiceInfo;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.badcells.BadCellStats;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.NetworkSceneEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.CountableHandler;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiAppStateMonitor;
import com.android.phone.utils.ModemDFS;
import com.android.phone.utils.ModemUtils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.b.a;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.process.ProcessManager;
import miui.telephony.TelephonyStatAdapter;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class QcModemHydraStat extends ModemHydraStatBase {
    private static final Object mLock = new Object();
    private boolean dataStallStarted;
    private boolean isLagging;
    private long lastLagTime;
    private String mCellGCI;
    private GameBoosterGameDelayListener mGameBoosterGameDelayListener;
    private int mGameLagFetchCnt;
    private int mHydraPollCount;
    private boolean mIsCallActive;
    private boolean mIsMobileDataOn;
    private boolean mModemMonitor;
    private final ModemUtils mModemUtil;
    private final OrientationEventListener mOrientationListener;
    private ModemHydraHandler mStatHandler;
    private final ConcurrentHashMap mModemRadioStatSceneMap = new ConcurrentHashMap();
    private final ConcurrentHashMap mSceneEnableMap = new ConcurrentHashMap();
    private final ArrayList mPingPongCellStats = new ArrayList();
    private final DecimalFormat mFloatDecimalFormat = new DecimalFormat("0.00");
    ArrayList mCellDataMapList = new ArrayList();
    ArrayList mDataStallMapList = new ArrayList();
    private final ConcurrentHashMap mSceneDataList = new ConcurrentHashMap();
    private ArrayList bandAndAntsList = new ArrayList();
    private int mCurrentOrientation = 1;
    private TelephonyCallBack mTelephonyCallBack = null;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.statistics.utils.QcModemHydraStat.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (networkInfo == null || networkInfo.getType() != 0) {
                    return;
                }
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    QcModemHydraStat.this.mStatHandler.obtainMessage(112, Boolean.TRUE).sendToTarget();
                    return;
                } else {
                    if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                        QcModemHydraStat.this.mStatHandler.obtainMessage(112, Boolean.FALSE).sendToTarget();
                        return;
                    }
                    return;
                }
            }
            if ("com.android.phone.modemhydra.test".equals(action)) {
                if (intent.getStringExtra(a.b) != null) {
                    QcModemHydraStat.this.handleGameStallChanged(intent.getBooleanExtra("isStall", true), intent.getExtras());
                    return;
                } else {
                    QcModemHydraStat.this.doNotifyModemReportStallInfo(new ModemHydraStatBase.StallRequestContext(MiuiPhoneUtils.getActiveDataPhoneId(), "com.tencent.tmgp.sgame", 0, new HashMap()), 915000007, "GameNetworkLag", "GameStall");
                    return;
                }
            }
            if ("miui.intent.action.VIDEO_INFO".equals(action)) {
                int intExtra = intent.getIntExtra("render-quality", -1);
                Utils.logD("QcModemHydraStat", "VIDEO_INFO videoQuality=" + intExtra);
                QcModemHydraStat.this.mLastVideoQuality = intExtra;
            }
        }
    };
    private boolean mGameDelayListenerRed = false;
    private final PingpongDetector[] mPingpongDetector = new PingpongDetector[MiuiPhoneUtils.PHONE_COUNT];
    private final ArrayList mSceneMdInfos = new ArrayList();

    class ModemHydraHandler extends CountableHandler {
        ModemHydraHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.phone.utils.CountableHandler, android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 100) {
                int i2 = message.arg1;
                boolean zIsPhoneInImsCall = MiuiPhoneUtils.isPhoneInImsCall(i2);
                if (QcModemHydraStat.this.mIsCallActive != zIsPhoneInImsCall) {
                    QcModemHydraStat.this.updateModemHydraStat("Volte", zIsPhoneInImsCall, i2);
                    QcModemHydraStat.this.mIsCallActive = zIsPhoneInImsCall;
                    return;
                }
                return;
            }
            if (i == 101) {
                int i3 = message.arg1;
                String str = (String) message.obj;
                removeMessageWithCount(101);
                if (("Volte".equals(str) || !MiuiPhoneUtils.isMobileDataConnected(i3)) && !MiuiPhoneUtils.isPhoneInImsCall(i3)) {
                    return;
                }
                QcModemHydraStat.this.mHydraPollCount++;
                if (QcModemHydraStat.this.mHydraPollCount < 60) {
                    QcModemHydraStat.this.recordModemRadioInfo(str, i3);
                    sendMessageDelayed(obtainMessage(101, i3, 0, str), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                    return;
                } else {
                    Utils.logD("QcModemHydraStat", "max poll count per round reached");
                    QcModemHydraStat.this.stopMonitor();
                    return;
                }
            }
            if (i == 103) {
                QcModemHydraStat.this.clearDataAfterReport(message);
                return;
            }
            if (i == 112) {
                boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                if (QcModemHydraStat.this.mIsMobileDataOn && !zBooleanValue && QcModemHydraStat.this.isLagging) {
                    QcModemHydraStat.this.isLagging = false;
                    QcModemHydraStat.this.mStatHandler.obtainMessage(108, Boolean.valueOf(QcModemHydraStat.this.isLagging)).sendToTarget();
                }
                QcModemHydraStat.this.mIsMobileDataOn = zBooleanValue;
                return;
            }
            if (i == 116) {
                QcModemHydraStat.this.processGameStatusChanged(((Boolean) message.obj).booleanValue());
                return;
            }
            if (i != 122) {
                switch (i) {
                    case 106:
                        QcModemHydraStat.this.updateCloudModemFeature();
                        break;
                    case 107:
                        QcModemHydraStat.this.processGameStall(((Integer) message.obj).intValue());
                        break;
                    case 108:
                        boolean zBooleanValue2 = ((Boolean) message.obj).booleanValue();
                        Utils.logD("QcModemHydraStat", "Game lagging:" + zBooleanValue2);
                        if (QcModemHydraStat.this.mGameLagFetchCnt < 60 && QcModemHydraStat.this.fetchModemDataByScene("GameLag", zBooleanValue2)) {
                            QcModemHydraStat.this.mGameLagFetchCnt++;
                            break;
                        }
                        break;
                    default:
                        Utils.logD("QcModemHydraStat", "unexpected msg:" + message);
                        break;
                }
                return;
            }
            if (message.obj instanceof Map) {
                try {
                    HashMap map = new HashMap((Map) message.obj);
                    QcModemHydraStat.this.recordModemCallReport(((Integer) map.get("cause")).intValue(), ((Integer) map.get("phoneId")).intValue(), ((Integer) map.get("callType")).intValue(), map);
                    return;
                } catch (ClassCastException | NullPointerException e) {
                    Utils.logE("QcModemHydraStat", "Invalid call report data format" + e);
                    return;
                }
            }
            Utils.logW("QcModemHydraStat", "msg.obj is not a Map type");
        }
    }

    private QcModemHydraStat() {
        HandlerThread handlerThread = new HandlerThread("QcModemHydraStat", 10);
        handlerThread.start();
        this.mStatHandler = new ModemHydraHandler(handlerThread.getLooper());
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mModemUtil = ModemUtils.init(phoneGlobals);
        this.mOrientationListener = new OrientationEventListener(this.mContext) { // from class: com.android.phone.statistics.utils.QcModemHydraStat.1
            @Override // android.view.OrientationEventListener
            public void onOrientationChanged(int i) {
                if (i > 45 && i <= 135) {
                    QcModemHydraStat.this.mCurrentOrientation = 8;
                    return;
                }
                if (i > 135 && i <= 225) {
                    QcModemHydraStat.this.mCurrentOrientation = 9;
                } else if (i > 225 && i <= 315) {
                    QcModemHydraStat.this.mCurrentOrientation = 0;
                } else {
                    QcModemHydraStat.this.mCurrentOrientation = 1;
                }
            }
        };
        this.mGameBoosterGameDelayListener = new GameBoosterGameDelayListener();
        registerAllEvents();
        this.mModemMonitor = PreferenceUtils.getInstance().getBooleanPreference("ModemMonitor", Boolean.TRUE).booleanValue();
        monitorAppSceneIfNeeded();
        super.initRegListener();
    }

    private void registerAllEvents() {
        registerTelephonyCallBack();
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.statistics.utils.QcModemHydraStat$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$registerAllEvents$0(baseEvent);
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.android.phone.modemhydra.test");
        intentFilter.addAction("miui.intent.action.VIDEO_INFO");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerAllEvents$0(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            ModemHydraHandler modemHydraHandler = this.mStatHandler;
            if (modemHydraHandler == null) {
                Utils.logW("QcModemHydraStat", "handler is null");
            } else {
                modemHydraHandler.sendEmptyMessage(106);
            }
        }
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            Utils.logD("QcModemHydraStat", "register TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                Utils.logD("QcModemHydraStat", "onPreciseCallStateChanged call state : " + i2);
                QcModemHydraStat.this.mStatHandler.removeMessages(100);
                QcModemHydraStat.this.mStatHandler.sendMessageDelayed(QcModemHydraStat.this.mStatHandler.obtainMessage(100, Integer.valueOf(i)), 500L);
            }
        }
    }

    private String SecondsToDateTime(long j) {
        return Instant.ofEpochSecond(j).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static ModemHydraStatBase make() {
        ModemUtils modemUtilsInit = ModemUtils.init(PhoneGlobals.getInstance());
        if (modemUtilsInit != null && modemUtilsInit.isModemFeatureSupported(2)) {
            ModemHydraStatBase.mInstance = new QcModemHydraStat();
            Utils.logW("QcModemHydraStat", "ModemHydraStat init " + ModemHydraStatBase.mInstance);
        }
        return ModemHydraStatBase.mInstance;
    }

    class GameBoosterGameDelayListener implements BaseEvent.IEventListener {
        private GameBoosterGameDelayListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            Bundle bundle;
            if ((baseEvent instanceof NetworkSceneEvent.GameDelayEvent) && (bundle = ((NetworkSceneEvent.GameDelayEvent) baseEvent).getBundle()) != null && "game_delay_level_changed".equals(baseEvent.getSubType())) {
                QcModemHydraStat.this.mStatHandler.obtainMessage(107, Integer.valueOf(bundle.getInt("game_delay_level", 0))).sendToTarget();
            }
        }
    }

    private void registerGameBoosterGameDelayListener() {
        EventHubManager.getInstance().registerListener("network", new String[]{"game_delay_level_changed"}, this.mGameBoosterGameDelayListener);
    }

    private void unRegisterGameBoosterGameDelayListener() {
        EventHubManager.getInstance().unregisterListener(this.mGameBoosterGameDelayListener);
    }

    private void startMonitor() {
        if (this.mModemUtil != null) {
            this.mOrientationListener.enable();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopMonitor() {
        if (this.mModemUtil != null) {
            this.mHydraPollCount = 0;
            this.mOrientationListener.disable();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordModemCallReport(int i, int i2, int i3, Map map) {
        Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getCallEndInfo", i2);
        if (objCallQcrilHookGetObject == null) {
            return;
        }
        ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
        Utils.logD("QcModemHydraStat", "recordModemCallReport buf remaining = " + byteBuffer.remaining());
        try {
            byteBuffer.get();
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("hplmn", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("roam", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("mcc", Short.valueOf(byteBuffer.getShort()));
            concurrentHashMap.put("mnc", Short.valueOf(byteBuffer.getShort()));
            concurrentHashMap.put("cid", Long.valueOf(byteBuffer.getLong()));
            concurrentHashMap.put("pci", Short.valueOf(byteBuffer.getShort()));
            concurrentHashMap.put("tac", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("rat", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("band", Short.valueOf(byteBuffer.getShort()));
            concurrentHashMap.put("freq", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("rsrp", Float.valueOf(byteBuffer.getFloat()));
            concurrentHashMap.put("sinr", Float.valueOf(byteBuffer.getFloat()));
            concurrentHashMap.put("ulGrant", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("ulBler", Float.valueOf(byteBuffer.getFloat()));
            concurrentHashMap.put("dlBler", Float.valueOf(byteBuffer.getFloat()));
            concurrentHashMap.put("averPuschPa", Float.valueOf(byteBuffer.getFloat()));
            concurrentHashMap.put("averMtpl", Float.valueOf(byteBuffer.getFloat()));
            concurrentHashMap.put("packetLossRate", Float.valueOf(byteBuffer.getFloat()));
            concurrentHashMap.put("sipErrorResponseCode", Short.valueOf(byteBuffer.getShort()));
            concurrentHashMap.put("nrNasRegErrorMsg", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("nrNasRegErrorCause", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("nrAuthErrorCause", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("nrAuthErrorMsg", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("nrRlfHappen", Short.valueOf(byteBuffer.getShort()));
            concurrentHashMap.put("rachReason", Short.valueOf(byteBuffer.getShort()));
            concurrentHashMap.put("rachResult", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("lteAuthErrorCause", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("lteAuthErrorMsg", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("lteNasRegErrorMsg", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("lteNasRegErrorCause", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("lteRlfCause", Short.valueOf(byteBuffer.getShort()));
            concurrentHashMap.put("lteRachReason", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("lteRachContention", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("lteRachResult", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("lteRachContentionProcedure", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("endReason", Short.valueOf(byteBuffer.getShort()));
            concurrentHashMap.put("dir", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("endUnderflowLossRate", Float.valueOf(byteBuffer.getFloat()));
            concurrentHashMap.put("beginBand", Short.valueOf(byteBuffer.getShort()));
            concurrentHashMap.put("bandSwitch", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("deactivateEpsBearerFlag", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("deactivateEpsBearerCause", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("pduSessionReleaseFlag", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("pduSessionReleaseCause", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("nr5gIRatLteFlag", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("fbrxErrorFlag", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("LteTimerExpireId", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("nr5gTimerExpireId", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("rtpSnNotUpdateFlag", Byte.valueOf(byteBuffer.get()));
            concurrentHashMap.put("teleCause", Integer.valueOf(i));
            concurrentHashMap.put("callType", Integer.valueOf(i3));
            ArrayList arrayList = new ArrayList();
            arrayList.add(concurrentHashMap);
            Utils.logD("QcModemHydraStat", "QcomCallReport " + arrayList);
            checkAndReport(arrayList, "QcomCallReport");
            int iIntValue = ((Integer) map.get("isEmergencyCall")).intValue();
            String str = (String) map.get("eccNumber");
            if (iIntValue != 1 || str == "null") {
                return;
            }
            concurrentHashMap.put("eccNumber", str);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(concurrentHashMap);
            Utils.logD("QcModemHydraStat", "QcomCallReportEcc " + arrayList2);
            checkAndReport(arrayList2, "QcomCallReportEcc");
        } catch (BufferUnderflowException unused) {
            Utils.logD("QcModemHydraStat", "Buffer underflow while reading call report");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameStall(int i) {
        Utils.logD("QcModemHydraStat", "processGameStall delay = " + i);
        if (this.mIsMobileDataOn) {
            if (i >= 460 && !this.isLagging) {
                this.isLagging = true;
                this.lastLagTime = SystemClock.elapsedRealtime();
                this.mStatHandler.obtainMessage(108, Boolean.valueOf(this.isLagging)).sendToTarget();
            } else {
                if (i >= 460 || !this.isLagging) {
                    return;
                }
                this.isLagging = false;
                if (SystemClock.elapsedRealtime() - this.lastLagTime < 2000) {
                    Utils.logD("QcModemHydraStat", "lag less than 2 secs, ignore this lag");
                } else {
                    this.mStatHandler.obtainMessage(108, Boolean.valueOf(this.isLagging)).sendToTarget();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameStatusChanged(boolean z) {
        if (z && "com.tencent.tmgp.sgame".equals(MiuiPhoneUtils.getTopAppName()) && !this.mGameDelayListenerRed) {
            registerGameBoosterGameDelayListener();
            this.mGameDelayListenerRed = true;
        } else {
            if (!this.mGameDelayListenerRed || z) {
                return;
            }
            unRegisterGameBoosterGameDelayListener();
            this.mGameDelayListenerRed = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean fetchModemDataByScene(String str, boolean z) {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone != null) {
            int phoneId = activeDataPhone.getPhoneId();
            Utils.logD("QcModemHydraStat", "fetchModemDataByScene:" + str);
            ServiceState serviceState = activeDataPhone.getServiceState();
            if (serviceState != null && MiuiPhoneUtils.isMobileDataConnected(phoneId)) {
                if (serviceState.getRilDataRadioTechnology() == 20) {
                    recordModemDataByScene(str, "getNr5gDataSceneInfo", z, phoneId);
                    recordAiModemByScene(str + "AiMd", "getNr5gAiDataInfo", z, phoneId);
                    return true;
                }
                if (serviceState.getRilDataRadioTechnology() == 14 || serviceState.getRilDataRadioTechnology() == 19) {
                    recordModemDataByScene(str, "getLteDataSceneInfo", z, phoneId);
                    recordAiModemByScene(str + "AiMd", "getLteAiDataInfo", z, phoneId);
                    return true;
                }
                Utils.logD("QcModemHydraStat", "not 4/5G, return");
                return false;
            }
        }
        return false;
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void getDataStallInfoFromModem(int i, HashMap map) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return;
        }
        try {
            ServiceState serviceState = phone.getServiceState();
            if (serviceState == null || !MiuiPhoneUtils.isMobileDataConnected(i)) {
                return;
            }
            if (serviceState.getRilDataRadioTechnology() == 20) {
                parseModemDataStallData("getNr5gDataSceneInfo", map, i);
            } else if (serviceState.getRilDataRadioTechnology() == 14 || serviceState.getRilDataRadioTechnology() == 19) {
                parseModemDataStallData("getLteDataSceneInfo", map, i);
            }
        } catch (Exception e) {
            Utils.logE("QcModemHydraStat", "getDataStallInfoFromModem failed: " + e.getMessage());
        }
    }

    private void parseModemDataStallData(String str, HashMap map, int i) {
        char c;
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        int i2 = 0;
        while (z && i2 < 50) {
            Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject(str, i);
            boolean z2 = str.charAt(3) == 'L';
            int i3 = i2 + 1;
            if (objCallQcrilHookGetObject != null) {
                ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
                byte b = byteBuffer.get();
                short s = byteBuffer.getShort();
                Utils.logD("QcModemHydraStat", " version: " + ((int) b) + " count: " + ((int) s));
                if (s < 6) {
                    z = false;
                }
                int i4 = 0;
                while (i4 < s) {
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    short s2 = byteBuffer.getShort();
                    short s3 = byteBuffer.getShort();
                    boolean z3 = z;
                    int i5 = i3;
                    ByteBuffer byteBuffer2 = byteBuffer;
                    boolean z4 = z2;
                    short s4 = s;
                    int i6 = i4;
                    arrayList = arrayList;
                    if (z4) {
                        concurrentHashMap.put("tac", Short.valueOf(byteBuffer2.getShort()));
                        concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s2), Short.valueOf(s3), String.valueOf(byteBuffer2.getInt())));
                        concurrentHashMap.put("arfcn", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("pci", Short.valueOf(byteBuffer2.getShort()));
                        concurrentHashMap.put("Band", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("ts", Long.valueOf(byteBuffer2.getLong()));
                        concurrentHashMap.put("dlBler", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("ulBler", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("grantBys", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("padding", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("lte2Lte", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("lte2Nr", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("dlMcs", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("ulMcs", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("tx", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("dlTbSz", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("pTotal", Long.valueOf(byteBuffer2.getLong()));
                        concurrentHashMap.put("cTotal", Long.valueOf(byteBuffer2.getLong()));
                        b = b;
                        if (b >= 2) {
                            concurrentHashMap.put("rsrp", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("snr", Float.valueOf(byteBuffer2.getFloat()));
                        }
                        if (b >= 3) {
                            concurrentHashMap.put("actTxPower", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("maxTxPower", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("expTxPower", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("sfnFullBuildSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("sfnGrantSum", Integer.valueOf(byteBuffer2.getInt()));
                        }
                        if (b >= 4) {
                            concurrentHashMap.put("ulSchCount", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("dlTbSizeSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("dlPadBytesSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("dlTbCount", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("dlPadZeroCount", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("rachFailFlag", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("pdcpDlMissPduSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pdcpDlDataPduRcvSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pdcpDlDataPduBytesSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pdcpDlDropPduBytesSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pdcpUlDiscardSduBytesSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pdcpUlBufBytesSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("numLayersRatio0", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("numLayersRatio1", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("numLayersRatio2", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("numLayersRatio3", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("ipaPdpDropBytesSum", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("ipaPdpDropExcepBytesSum", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("rsrpMin", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("sinrMin", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("antennaNumber", Byte.valueOf(byteBuffer2.get()));
                        }
                        if (b >= 5) {
                            concurrentHashMap.put("pdcpSduDiscardCount", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pdcpAvgDelay", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("RrcState", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("DsdaSubMode", Integer.valueOf(byteBuffer2.getInt()));
                        }
                        c = 3;
                    } else {
                        concurrentHashMap.put("tac", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s2), Short.valueOf(s3), String.valueOf(byteBuffer2.getLong())));
                        concurrentHashMap.put("arfcn", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("pci", Short.valueOf(byteBuffer2.getShort()));
                        concurrentHashMap.put("Band", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("scs", Byte.valueOf(byteBuffer2.get()));
                        concurrentHashMap.put("ts", Long.valueOf(byteBuffer2.getLong()));
                        concurrentHashMap.put("dlBler", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("ulBler", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("grantSz", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("bysBuilt", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("nr2Nr", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("nr2Lte", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("dlMcs", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("ulMcs", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("tx", Float.valueOf(byteBuffer2.getFloat()));
                        concurrentHashMap.put("dlTb", Integer.valueOf(byteBuffer2.getInt()));
                        concurrentHashMap.put("pTotal", Long.valueOf(byteBuffer2.getLong()));
                        concurrentHashMap.put("cTotal", Long.valueOf(byteBuffer2.getLong()));
                        concurrentHashMap.put("wm", Long.valueOf(byteBuffer2.getLong()));
                        concurrentHashMap.put("grantPdding", Long.valueOf(byteBuffer2.getLong()));
                        b = b;
                        if (b >= 2) {
                            concurrentHashMap.put("rsrp", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("snr", Float.valueOf(byteBuffer2.getFloat()));
                        }
                        c = 3;
                        if (b >= 3) {
                            concurrentHashMap.put("actTxPower", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("maxTxPower", Float.valueOf(byteBuffer2.getFloat()));
                        }
                        if (b > 4) {
                            concurrentHashMap.put("ulSchCount", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("ulFullSchCount", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("crcPassTbBytesSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("paddingBytesSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("rachFailFlag", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("pdcpDlMissPduSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pdcpDlDataPduRcvSum", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pdcpDlDataPduBytesSum", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("pdcpDlDropPduBytesSum", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("pdcpUlDiscardBytesSum", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("avgMetricsRi", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("avgMetricsCqi", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("numLayersRatio0", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("numLayersRatio1", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("numLayersRatio2", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("numLayersRatio3", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("txNextAck", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("txNext", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("taCommand", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("ipaPDropBytesSum", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("ipaPDropExcepBytesSum", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("rrsrMin", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("snrMin", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("antennaNumber", Integer.valueOf(byteBuffer2.getInt()));
                        }
                        if (b >= 5) {
                            concurrentHashMap.put("pdcpSduDiscardCount", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pdcpAvgDelay", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("RrcState", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("DsdaSubMode", Integer.valueOf(byteBuffer2.getInt()));
                        }
                    }
                    arrayList.add(concurrentHashMap);
                    i4 = i6 + 1;
                    z = z3;
                    i3 = i5;
                    byteBuffer = byteBuffer2;
                    z2 = z4;
                    s = s4;
                }
            }
            map.put("modemStallData", String.valueOf(arrayList));
            i2 = i3;
        }
    }

    private void recordModemDataByScene(String str, String str2, boolean z, int i) {
        char c;
        QcModemHydraStat qcModemHydraStat;
        QcModemHydraStat qcModemHydraStat2 = this;
        String str3 = str;
        ArrayList arrayList = (ArrayList) qcModemHydraStat2.mSceneDataList.get(str3);
        if (arrayList == null || arrayList.size() < 60) {
            boolean z2 = true;
            boolean z3 = true;
            int i2 = 0;
            while (z3 && i2 < 50) {
                Object objCallQcrilHookGetObject = qcModemHydraStat2.mModemUtil.callQcrilHookGetObject(str2, i);
                boolean z4 = str2.charAt(3) == 'L' ? z2 : false;
                int i3 = i2 + 1;
                if (z) {
                    if ("StallData".equals(str3)) {
                        qcModemHydraStat2.dataStallStarted = z2;
                        return;
                    }
                    return;
                }
                if (objCallQcrilHookGetObject != null) {
                    ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
                    byte b = byteBuffer.get();
                    short s = byteBuffer.getShort();
                    if (s < 6) {
                        z3 = false;
                    }
                    int i4 = 0;
                    while (i4 < s) {
                        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                        short s2 = byteBuffer.getShort();
                        short s3 = byteBuffer.getShort();
                        boolean z5 = z3;
                        int i5 = i3;
                        ByteBuffer byteBuffer2 = byteBuffer;
                        boolean z6 = z4;
                        short s4 = s;
                        int i6 = i4;
                        ArrayList arrayList2 = arrayList;
                        if (z6) {
                            concurrentHashMap.put("tac", Short.valueOf(byteBuffer2.getShort()));
                            concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s2), Short.valueOf(s3), String.valueOf(byteBuffer2.getInt())));
                            concurrentHashMap.put("arfcn", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pci", Short.valueOf(byteBuffer2.getShort()));
                            concurrentHashMap.put("Band", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("ts", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("dlBler", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("ulBler", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("grantBys", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("padding", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("lte2Lte", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("lte2Nr", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("dlMcs", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("ulMcs", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("tx", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("dlTbSz", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pTotal", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("cTotal", Long.valueOf(byteBuffer2.getLong()));
                            b = b;
                            if (b >= 2) {
                                concurrentHashMap.put("rsrp", Float.valueOf(byteBuffer2.getFloat()));
                                concurrentHashMap.put("snr", Float.valueOf(byteBuffer2.getFloat()));
                            }
                            c = 3;
                            if (b >= 3) {
                                concurrentHashMap.put("actTxPower", Float.valueOf(byteBuffer2.getFloat()));
                                concurrentHashMap.put("maxTxPower", Float.valueOf(byteBuffer2.getFloat()));
                                concurrentHashMap.put("expTxPower", Float.valueOf(byteBuffer2.getFloat()));
                                concurrentHashMap.put("sfnFullBuildSum", Integer.valueOf(byteBuffer2.getInt()));
                                concurrentHashMap.put("sfnGrantSum", Integer.valueOf(byteBuffer2.getInt()));
                                c = 3;
                            }
                        } else {
                            concurrentHashMap.put("tac", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s2), Short.valueOf(s3), String.valueOf(byteBuffer2.getLong())));
                            concurrentHashMap.put("arfcn", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pci", Short.valueOf(byteBuffer2.getShort()));
                            concurrentHashMap.put("Band", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("scs", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("ts", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("dlBler", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("ulBler", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("grantSz", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("bysBuilt", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("nr2Nr", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("nr2Lte", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("dlMcs", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("ulMcs", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("tx", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("dlTb", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("pTotal", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("cTotal", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("wm", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("grantPdding", Long.valueOf(byteBuffer2.getLong()));
                            b = b;
                            if (b >= 2) {
                                concurrentHashMap.put("rsrp", Float.valueOf(byteBuffer2.getFloat()));
                                concurrentHashMap.put("snr", Float.valueOf(byteBuffer2.getFloat()));
                            }
                            c = 3;
                            if (b >= 3) {
                                concurrentHashMap.put("actTxPower", Float.valueOf(byteBuffer2.getFloat()));
                                concurrentHashMap.put("maxTxPower", Float.valueOf(byteBuffer2.getFloat()));
                            }
                        }
                        Utils.logD("QcModemHydraStat", str + ":" + concurrentHashMap);
                        if (arrayList2 != null) {
                            arrayList = arrayList2;
                            arrayList.add(concurrentHashMap);
                            qcModemHydraStat = this;
                        } else {
                            ArrayList arrayList3 = new ArrayList();
                            arrayList3.add(concurrentHashMap);
                            qcModemHydraStat = this;
                            qcModemHydraStat.mSceneDataList.put(str, arrayList3);
                            arrayList = arrayList3;
                        }
                        i4 = i6 + 1;
                        str3 = str;
                        qcModemHydraStat2 = qcModemHydraStat;
                        z3 = z5;
                        i3 = i5;
                        byteBuffer = byteBuffer2;
                        z4 = z6;
                        s = s4;
                    }
                }
                str3 = str3;
                qcModemHydraStat2 = qcModemHydraStat2;
                i2 = i3;
                z2 = true;
            }
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.put("NA", "NA");
            if (arrayList != null) {
                arrayList.add(concurrentHashMap2);
            }
        }
    }

    private void recordAiModemByScene(String str, String str2, boolean z, int i) {
        ByteBuffer byteBuffer;
        String str3;
        QcModemHydraStat qcModemHydraStat = this;
        String str4 = str;
        ArrayList arrayList = (ArrayList) qcModemHydraStat.mSceneDataList.get(str4);
        if (arrayList == null || arrayList.size() < 60) {
            boolean z2 = true;
            int i2 = 0;
            while (z2 && i2 < 50) {
                Object objCallQcrilHookGetObject = qcModemHydraStat.mModemUtil.callQcrilHookGetObject(str2, i);
                boolean z3 = str2.charAt(3) == 'L';
                int i3 = i2 + 1;
                if (z) {
                    return;
                }
                if (objCallQcrilHookGetObject != null) {
                    ByteBuffer byteBuffer2 = (ByteBuffer) objCallQcrilHookGetObject;
                    byteBuffer2.get();
                    short s = byteBuffer2.getShort();
                    if (s < 4) {
                        z2 = false;
                    }
                    int i4 = 0;
                    while (i4 < s) {
                        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                        boolean z4 = z2;
                        int i5 = i3;
                        short s2 = s;
                        int i6 = i4;
                        ArrayList arrayList2 = arrayList;
                        if (z3) {
                            qcModemHydraStat.updateCommonDataFromBytes(byteBuffer2, z3, concurrentHashMap);
                            concurrentHashMap.put("ts", Long.valueOf(byteBuffer2.getLong()));
                            concurrentHashMap.put("ulTPdcp", Short.valueOf(byteBuffer2.getShort()));
                            concurrentHashMap.put("ulScsPL", Short.valueOf(byteBuffer2.getShort()));
                            concurrentHashMap.put("ulScsTxPwr", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("ulRbNum", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("ulPhr", Short.valueOf(byteBuffer2.getShort()));
                            concurrentHashMap.put("ulMaxPwr", Short.valueOf(byteBuffer2.getShort()));
                            concurrentHashMap.put("ulPaddingSz", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("ulBler", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("ulGrantSz", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("ulBand", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("ulBw", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("ulMcs", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("ulTbSz", Short.valueOf(byteBuffer2.getShort()));
                            concurrentHashMap.put("rsrp", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("snr", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("ulTxPwr", Short.valueOf(byteBuffer2.getShort()));
                            concurrentHashMap.put("dlRbNum", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("dlBler", Float.valueOf(byteBuffer2.getFloat()));
                            concurrentHashMap.put("dlMcs", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("dlCqi", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("dlri", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("dlNumLayer", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("dlCcCnt", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("ulCcCnt", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("dlPaddingBys", Integer.valueOf(byteBuffer2.getInt()));
                            concurrentHashMap.put("ulBsr", Integer.valueOf(byteBuffer2.getInt()));
                            qcModemHydraStat = this;
                            byteBuffer = byteBuffer2;
                        } else {
                            qcModemHydraStat.updateCommonDataFromBytes(byteBuffer2, z3, concurrentHashMap);
                            byteBuffer = byteBuffer2;
                            concurrentHashMap.put("scs", Byte.valueOf(byteBuffer2.get()));
                            concurrentHashMap.put("ts", Long.valueOf(byteBuffer.getLong()));
                            concurrentHashMap.put("dlBler", Float.valueOf(byteBuffer.getFloat()));
                            concurrentHashMap.put("dlCqi", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("dlMcs", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("dlNumLayer", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("dlPmi", Integer.valueOf(byteBuffer.getInt()));
                            concurrentHashMap.put("dlRbNum", Short.valueOf(byteBuffer.getShort()));
                            concurrentHashMap.put("dlRi", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("dlTbId", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("dlTput", Integer.valueOf(byteBuffer.getInt()));
                            concurrentHashMap.put("dlTxMode", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("dlTReorder", Short.valueOf(byteBuffer.getShort()));
                            concurrentHashMap.put("ulBand", Integer.valueOf(byteBuffer.getInt()));
                            concurrentHashMap.put("ulBler", Float.valueOf(byteBuffer.getFloat()));
                            concurrentHashMap.put("ulBw", Short.valueOf(byteBuffer.getShort()));
                            concurrentHashMap.put("ulGrantSz", Integer.valueOf(byteBuffer.getInt()));
                            concurrentHashMap.put("ulHarq", Float.valueOf(byteBuffer.getFloat()));
                            concurrentHashMap.put("ulMaxPwr", Float.valueOf(byteBuffer.getFloat()));
                            concurrentHashMap.put("ulMcs", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("ulPaddingSz", Long.valueOf(byteBuffer.getLong()));
                            concurrentHashMap.put("ulPhr", Float.valueOf(byteBuffer.getFloat()));
                            concurrentHashMap.put("ulRbNum", Short.valueOf(byteBuffer.getShort()));
                            concurrentHashMap.put("ulScs", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("ulSrsPL", Float.valueOf(byteBuffer.getFloat()));
                            concurrentHashMap.put("ulSrsTxPwr", Float.valueOf(byteBuffer.getFloat()));
                            concurrentHashMap.put("ulTbNum", Integer.valueOf(byteBuffer.getInt()));
                            concurrentHashMap.put("ulTbSz", Integer.valueOf(byteBuffer.getInt()));
                            concurrentHashMap.put("ulTput", Long.valueOf(byteBuffer.getLong()));
                            concurrentHashMap.put("ulTxMode", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("ulTxPwr", Float.valueOf(byteBuffer.getFloat()));
                            concurrentHashMap.put("ulTPdcp", Short.valueOf(byteBuffer.getShort()));
                            concurrentHashMap.put("CcCnt", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("rsrp", Float.valueOf(byteBuffer.getFloat()));
                            concurrentHashMap.put("snr", Float.valueOf(byteBuffer.getFloat()));
                            concurrentHashMap.put("dlPaddingBys", Long.valueOf(byteBuffer.getLong()));
                            concurrentHashMap.put("ulBsr", Short.valueOf(byteBuffer.getShort()));
                        }
                        if (arrayList2 != null) {
                            arrayList = arrayList2;
                            arrayList.add(concurrentHashMap);
                            str3 = str;
                        } else {
                            ArrayList arrayList3 = new ArrayList();
                            arrayList3.add(concurrentHashMap);
                            str3 = str;
                            qcModemHydraStat.mSceneDataList.put(str3, arrayList3);
                            arrayList = arrayList3;
                        }
                        i4 = i6 + 1;
                        str4 = str3;
                        z2 = z4;
                        i3 = i5;
                        s = s2;
                        z3 = z3;
                        byteBuffer2 = byteBuffer;
                    }
                }
                str4 = str4;
                i2 = i3;
            }
            Utils.logD("QcModemHydraStat", str4 + arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordModemRadioInfo(String str, int i) {
        CellIdentity currentCellIdentity;
        ServiceState serviceState;
        int operatingBandForEarfcn;
        ModemRadioInfo modemLteSignalInfo;
        if (!this.mModemMonitor) {
            Utils.logD("QcModemHydraStat", "modem monitor is off");
            return;
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || (currentCellIdentity = phone.getCurrentCellIdentity()) == null || (serviceState = phone.getServiceState()) == null) {
            return;
        }
        if (serviceState.getRilDataRadioTechnology() < 19 && serviceState.getRilDataRadioTechnology() != 14) {
            Utils.logD("QcModemHydraStat", "ignore non 4/5G rat");
            return;
        }
        if (isPhoneIn5G(i)) {
            if (currentCellIdentity instanceof CellIdentityNr) {
                operatingBandForEarfcn = Utils.getOperationBandForNrarfcn(((CellIdentityNr) currentCellIdentity).getNrarfcn());
            } else {
                operatingBandForEarfcn = this.mModemUtil.callQcrilHookGet("getNr5gBand", i);
            }
            modemLteSignalInfo = new ModemNrSignalInfo(i);
        } else {
            operatingBandForEarfcn = currentCellIdentity instanceof CellIdentityLte ? AccessNetworkUtils.getOperatingBandForEarfcn(((CellIdentityLte) currentCellIdentity).getEarfcn()) : 65535;
            modemLteSignalInfo = new ModemLteSignalInfo(i);
        }
        modemLteSignalInfo.band = operatingBandForEarfcn;
        modemLteSignalInfo.orientation = this.mCurrentOrientation;
        modemLteSignalInfo.addToRadioSceneMap(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearDataAfterReport(Message message) {
        try {
            ((List) message.obj).clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPhoneInVideoCall(int i) {
        if (!SubscriptionManager.isValidPhoneId(i)) {
            Utils.logD("QcModemHydraStat", "invalid phone id");
            return false;
        }
        Phone imsPhone = PhoneFactory.getPhone(i).getImsPhone();
        if (imsPhone != null && imsPhone.isVideoEnabled()) {
            ImsCall imsCall = imsPhone.getForegroundCall().getImsCall();
            ImsCall imsCall2 = imsPhone.getBackgroundCall().getImsCall();
            ImsCall imsCall3 = imsPhone.getRingingCall().getImsCall();
            if (imsCall != null && imsCall.isVideoCall()) {
                return true;
            }
            if (imsCall2 != null && imsCall2.isVideoCall()) {
                return true;
            }
            if (imsCall3 != null && imsCall3.isVideoCall()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataPhoneId() {
        int phoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getActiveDataSubscriptionId());
        if (phoneId != 1) {
            return 0;
        }
        return phoneId;
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void updateModemHydraStat(String str, boolean z, int i) {
        if ("Volte".equals(str) || MiuiPhoneUtils.isMobileDataConnected(i)) {
            if ("Game".equals(str)) {
                this.mStatHandler.obtainMessage(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET, Boolean.valueOf(z)).sendToTarget();
            }
            this.mSceneEnableMap.put(str, Boolean.valueOf(z));
            Utils.logD("QcModemHydraStat", z + " updateModemHydraStat " + str);
            this.mStatHandler.removeMessageWithCount(101);
            recordSceneModemInfo(str, z);
            if (!z || isMultiConcurrentScene()) {
                Utils.logD("QcModemHydraStat", "stopMonitor on:" + str);
                stopMonitor();
                pullRfBandAndAntnfo(z, str);
                return;
            }
            if (z) {
                Utils.logD("QcModemHydraStat", "startMonitor on:" + str);
                startMonitor();
                this.mStatHandler.obtainMessage(101, i, 0, str).sendToTarget();
                pullRfBandAndAntnfo(z, str);
            }
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void processTopAppChanged(String str) {
        String str2;
        if (ModemHydraStatBase.getShortVideoPackages().contains(str)) {
            updateModemHydraStat("shortVideo", true, getDefaultDataPhoneId());
            str2 = "shortVideo";
        } else if (ModemHydraStatBase.getLiveStreamingPkgs().contains(str)) {
            updateModemHydraStat("liveStreaming", true, getDefaultDataPhoneId());
            str2 = "liveStreaming";
        } else {
            str2 = "other";
        }
        if ("shortVideo".equals(this.lastScene) || ("liveStreaming".equals(this.lastScene) && "other".equals(str2))) {
            updateModemHydraStat(this.lastScene, false, getDefaultDataPhoneId());
        }
        this.lastScene = str2;
    }

    private boolean isMultiConcurrentScene() {
        Iterator it = this.mSceneEnableMap.values().iterator();
        int i = 0;
        while (it.hasNext()) {
            if (((Boolean) it.next()).booleanValue()) {
                i++;
            }
        }
        return i > 1;
    }

    class PingpongDetector {
        private long mCellChgTime;
        private String mLastHexCell;
        private long mlastPingpongCellTime;
        private final Long PING_PONG_SHORT_SPAN = 10000L;
        private final Long PING_PONG_LONG_SPAN = 1800000L;
        private final ArrayList mCells = new ArrayList();

        PingpongDetector() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void detect(int i) {
            CellIdentity currentCellIdentity;
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != activeDataPhone || phone == null || (currentCellIdentity = phone.getCurrentCellIdentity()) == null) {
                Utils.logD("QcModemHydraStat", "invalid phone info");
                return;
            }
            String globalCellId = currentCellIdentity.getGlobalCellId();
            if (TextUtils.isEmpty(globalCellId) || TextUtils.equals(this.mLastHexCell, globalCellId)) {
                return;
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            this.mLastHexCell = globalCellId;
            if (jElapsedRealtime - this.mCellChgTime > this.PING_PONG_SHORT_SPAN.longValue()) {
                this.mCells.clear();
            }
            this.mCellChgTime = jElapsedRealtime;
            long j = this.mlastPingpongCellTime;
            if (j > 0 && jElapsedRealtime - j < this.PING_PONG_LONG_SPAN.longValue()) {
                Utils.logD("QcModemHydraStat", "skip detecting in 30mins after pingpong detected");
                return;
            }
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            QcModemHydraStat.this.recordCommonCellIdentity(concurrentHashMap, currentCellIdentity);
            if (currentCellIdentity instanceof CellIdentityLte) {
                QcModemHydraStat.this.getLteSignalInfo(concurrentHashMap, phone);
            } else {
                if (!(currentCellIdentity instanceof CellIdentityNr)) {
                    Utils.logD("QcModemHydraStat", " ignore non 4/5G cell");
                    return;
                }
                QcModemHydraStat.this.getNrSignalInfo(concurrentHashMap, phone);
            }
            concurrentHashMap.put("time", String.valueOf(jElapsedRealtime / 1000));
            this.mCells.add(concurrentHashMap);
            Utils.logD("QcModemHydraStat", "current cell:" + concurrentHashMap);
            HashSet hashSet = new HashSet();
            HashMap map = new HashMap();
            ArrayList arrayList = this.mCells;
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                String str = (String) ((ConcurrentHashMap) obj).get("GCI");
                hashSet.add(str);
                if (hashSet.size() > 3) {
                    Utils.logD("QcModemHydraStat", "more than 3 cells found, NO pingpong");
                    this.mCells.clear();
                    return;
                } else if (map.containsKey(str)) {
                    int iIntValue = ((Integer) map.get(str)).intValue() + 1;
                    if (iIntValue > 4) {
                        onPingpongDetected();
                        return;
                    }
                    map.put(str, Integer.valueOf(iIntValue));
                } else {
                    map.put(str, 1);
                }
            }
        }

        private void onPingpongDetected() {
            String typeName;
            Utils.logW("QcModemHydraStat", "cells pingpong detected");
            this.mlastPingpongCellTime = SystemClock.elapsedRealtime();
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            ConnectivityManager connectivityManager = (ConnectivityManager) QcModemHydraStat.this.mContext.getSystemService("connectivity");
            String str = "NA";
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    typeName = "NA";
                } else {
                    typeName = activeNetworkInfo.getTypeName();
                }
                concurrentHashMap.put("networkType", typeName);
            }
            ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
            if (foregroundInfo != null) {
                str = foregroundInfo.mForegroundPackageName;
            }
            concurrentHashMap.put("pkg", str);
            this.mCells.add(concurrentHashMap);
            QcModemHydraStat.this.mPingPongCellStats.addAll(this.mCells);
            this.mCells.clear();
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void updatePingpongCellStat(int i) {
        PingpongDetector[] pingpongDetectorArr = this.mPingpongDetector;
        if (pingpongDetectorArr[i] == null) {
            pingpongDetectorArr[i] = new PingpongDetector();
        }
        this.mPingpongDetector[i].detect(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordCommonCellIdentity(Map map, CellIdentity cellIdentity) {
        if (cellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
            map.put("GCI", cellIdentityLte.getPlmn() + "-" + cellIdentityLte.getCi());
            map.put("tac", Integer.valueOf(cellIdentityLte.getTac()));
            map.put("pci", String.valueOf(cellIdentityLte.getPci()));
            map.put("arfcn", String.valueOf(cellIdentityLte.getEarfcn()));
            map.put("RAT", String.valueOf(14));
            return;
        }
        if (cellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            map.put("GCI", cellIdentityNr.getPlmn() + "-" + cellIdentityNr.getNci());
            map.put("tac", Integer.valueOf(cellIdentityNr.getTac()));
            map.put("pci", String.valueOf(cellIdentityNr.getPci()));
            map.put("arfcn", String.valueOf(cellIdentityNr.getNrarfcn()));
            map.put("RAT", String.valueOf(20));
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void checkAndReport(List list, String str) {
        if (list == null || list.size() <= 0) {
            return;
        }
        List listSubList = list.size() > 60 ? list.subList(0, 60) : list;
        HashMap map = new HashMap();
        map.put("Params", listSubList);
        MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", str, map);
        this.mStatHandler.sendMessageDelayed(this.mStatHandler.obtainMessage(103, list), MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void reportModemHydraStat() {
        if (this.mModemMonitor) {
            checkAndReport(this.mPingPongCellStats, "PingpongCell");
            checkAndReport(this.mSceneMdInfos, "MDSceneStats");
            for (String str : this.mModemRadioStatSceneMap.keySet()) {
                checkAndReport((List) this.mModemRadioStatSceneMap.get(str), str);
            }
            if (this.mSceneDataList.size() > 0) {
                for (String str2 : this.mSceneDataList.keySet()) {
                    checkAndReport((List) this.mSceneDataList.get(str2), str2);
                }
            }
            try {
                new ModemExceptionCollector().pullModemRecords();
                new MultiModemInfoCollector().pullModemInfoData();
                this.mCellGCI = null;
                pullNsaBandCombInfo();
                checkAndReport(this.bandAndAntsList, "RfBandAndAntInfo");
                pullImsSipMessageInfo();
                pullLteTimerExpiry();
                pullDLBoostInfo();
                pull6RXInfo();
                pull380Info();
                pullT1SInfo();
                pullVoiceQualityInfo();
                pullAirportModeInfo();
                pullBorderModeInfo();
                pullStAverageSarInfo();
                pullMbnSpaceSizeInfo();
                pullNrTxInfo();
                pullMtConnFailInfo();
                if (HyperOSCustFeatureResolve.getBoolean("config_modem_call_fail_info_support", false)) {
                    pullAbnormalTauCountInfo();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public ArrayList getRachFailRateInfo() {
        ArrayList arrayList = new ArrayList();
        ModemUtils modemUtils = this.mModemUtil;
        if (modemUtils == null) {
            Utils.logE("QcModemHydraStat", "getRachFailRateInfo: mModemUtil is null");
            return arrayList;
        }
        Object objCallQcrilHookGetObject = modemUtils.callQcrilHookGetObject("getLteRachFailNotDeleteInfo", -1);
        if (objCallQcrilHookGetObject != null) {
            parseRachFailRateData((ByteBuffer) objCallQcrilHookGetObject, arrayList);
        }
        Object objCallQcrilHookGetObject2 = this.mModemUtil.callQcrilHookGetObject("getNr5gRachFailNotDeleteInfo", -1);
        if (objCallQcrilHookGetObject2 != null) {
            parseRachFailRateData((ByteBuffer) objCallQcrilHookGetObject2, arrayList);
        }
        Utils.logD("QcModemHydraStat", "RACH Fail Rate Info: " + arrayList);
        return arrayList;
    }

    private void parseRachFailRateData(ByteBuffer byteBuffer, ArrayList arrayList) {
        if (byteBuffer == null) {
            return;
        }
        try {
            byteBuffer.get();
            short s = byteBuffer.getShort();
            for (int i = 0; i < s; i++) {
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                concurrentHashMap.put("mcc", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap.put("mnc", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap.put("rachTiggerNum", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap.put("rachOkNum", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap.put("rachFailNum", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap.put("rachUnknowNum", Short.valueOf(byteBuffer.getShort()));
                arrayList.add(concurrentHashMap);
            }
        } catch (Exception e) {
            Utils.logE("QcModemHydraStat", "Error parsing RACH fail rate data for : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void recordSceneModemInfo(String str, boolean z) {
        String[] strArr = {"getLteTxPowerInfo", "getNr5gTxPowerInfo"};
        String[] strArr2 = {"getNr5gUlBlerInfo", "getLteUlBlerInfo", "getNr5gMcsInfo", "getLteMcsInfo"};
        int i = 2;
        if (z) {
            this.mModemUtil.callQcrilHookGetObject("getMcsTrmAsdivInfo", -1);
            this.mModemUtil.callQcrilHookGetObject("getSwitchConfigAndAntennaInfo", -1);
            for (int i2 = 0; i2 < 2; i2++) {
                String str2 = strArr[i2];
                for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                    this.mModemUtil.callQcrilHookGetObject(str2, i3);
                }
            }
            for (int i4 = 0; i4 < 4; i4++) {
                this.mModemUtil.callHook(strArr2[i4], new Object[0]);
            }
            return;
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getMcsTrmAsdivInfo", -1);
        if (objCallQcrilHookGetObject != null) {
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byteBuffer.get();
            concurrentHashMap.put("daySwitch", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("3in5Switch", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("2in2Switch", Integer.valueOf(byteBuffer.getInt()));
        }
        Object objCallQcrilHookGetObject2 = this.mModemUtil.callQcrilHookGetObject("getSwitchConfigAndAntennaInfo", -1);
        if (objCallQcrilHookGetObject2 != null) {
            ByteBuffer byteBuffer2 = (ByteBuffer) objCallQcrilHookGetObject2;
            byteBuffer2.get();
            short s = byteBuffer2.get();
            short s2 = byteBuffer2.get();
            for (int i5 = 0; i5 < s; i5++) {
                concurrentHashMap.put("switch" + i5, Integer.valueOf(byteBuffer2.getInt()));
            }
            for (int i6 = 0; i6 < s2; i6++) {
                concurrentHashMap.put("ANT" + i6, Integer.valueOf(byteBuffer2.getInt()));
            }
        }
        String[] strArr3 = {"L1", "L2", "L3", "L4", "L5", "L6"};
        int i7 = 0;
        while (true) {
            char c = 'L';
            if (i7 >= i) {
                break;
            }
            String str3 = strArr[i7];
            int i8 = 0;
            while (i8 < MiuiPhoneUtils.PHONE_COUNT) {
                Object objCallQcrilHookGetObject3 = this.mModemUtil.callQcrilHookGetObject(str3, i8);
                if (objCallQcrilHookGetObject3 != null) {
                    ByteBuffer byteBuffer3 = (ByteBuffer) objCallQcrilHookGetObject3;
                    byteBuffer3.get();
                    boolean z2 = i8 == getDefaultDataPhoneId();
                    boolean z3 = str3.charAt(3) == c;
                    String strConcat = (z2 ? "_p" + i8 : "_s" + i8).concat(z3 ? "_lte" : "_nr");
                    for (int i9 = 0; i9 < 6; i9++) {
                        concurrentHashMap.put(strArr3[i9] + strConcat, Integer.valueOf(byteBuffer3.getInt()));
                    }
                }
                i8++;
                c = 'L';
            }
            i7++;
            i = 2;
        }
        for (int i10 = 0; i10 < 4; i10++) {
            String str4 = strArr2[i10];
            Object objCallHook = this.mModemUtil.callHook(str4, new Object[0]);
            if (objCallHook != null) {
                ByteBuffer byteBuffer4 = (ByteBuffer) objCallHook;
                boolean z4 = str4.charAt(3) == 'L';
                if (str4.contains("Mcs")) {
                    byteBuffer4.get();
                    byte b = byteBuffer4.get();
                    String str5 = z4 ? "mcs" : "nMcs";
                    for (int i11 = 0; i11 < b; i11++) {
                        concurrentHashMap.put(str5 + i11, Integer.valueOf(byteBuffer4.getInt()));
                    }
                } else {
                    byteBuffer4.get();
                    String str6 = z4 ? "bler" : "nBler";
                    for (int i12 = 1; i12 <= 5; i12++) {
                        concurrentHashMap.put(str6 + i12, Integer.valueOf(byteBuffer4.getInt()));
                    }
                }
            }
        }
        if (concurrentHashMap.size() > 0) {
            concurrentHashMap.put("Scene", str);
            Utils.logD("QcModemHydraStat", "recordSceneMdInfo:" + concurrentHashMap);
            if (this.mSceneMdInfos.size() >= 60) {
                return;
            }
            this.mSceneMdInfos.add(concurrentHashMap);
        }
    }

    private void pullStAverageSarInfo() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getStAverageSarInfo", i);
            Utils.logD("QcModemHydraStat", "St_Average_Sar_Info-sub=" + i);
            if (objCallQcrilHookGetObject == null) {
                break;
            }
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byte b = byteBuffer.get();
            short s = byteBuffer.getShort();
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("version", Byte.valueOf(b));
            if (s > 0) {
                arrayList.add(concurrentHashMap);
            }
            for (int i2 = 0; i2 < s; i2++) {
                ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                concurrentHashMap2.put("sub", Integer.valueOf(i));
                concurrentHashMap2.put("nr_dl_bler", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("pusch_tx_power", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("pusch_mtpl", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("pusch_pathloss", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("pucch_tx_power", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("pucch_mtpl", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("pucch_pathloss", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("nr_dl_rb", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap2.put("nr_ul_rb", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap2.put("nr_ul_tput", Long.valueOf(byteBuffer.getLong()));
                concurrentHashMap2.put("nr_dl_tput", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap2.put("nr_cell_qua_rsrp", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("lte_dl_bler", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("lte_ul_bler", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("lte_tx_power", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap2.put("lte_mtpl", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap2.put("lte_pathloss", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("lte_dl_rb", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("lte_ul_rb", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("lte_ul_tput", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap2.put("lte_dl_tput", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap2.put("lte_meas_rsrp", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("mcc", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap2.put("mnc", Short.valueOf(byteBuffer.getShort()));
                arrayList.add(concurrentHashMap2);
            }
        }
        Utils.logD("QcModemHydraStat", "St_Average_Sar_Info:" + arrayList);
        checkAndReport(arrayList, "St_Average_Sar_Info");
    }

    public List getMbnInfoList(String str) {
        if (str == null) {
            return new ArrayList();
        }
        List stringArray = HyperOSCustFeatureResolve.getStringArray(str);
        return stringArray != null ? stringArray : new ArrayList();
    }

    private void pullMbnSpaceSizeInfo() {
        ArrayList arrayList = new ArrayList();
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getMbnSpaceSizeInfo", -1);
        Utils.logD("QcModemHydraStat", "Mbn_Space_Size_Info Enter");
        if (objCallQcrilHookGetObject == null) {
            return;
        }
        ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
        int i = byteBuffer.getInt();
        Utils.logD("QcModemHydraStat", "r = " + i);
        if (i != 0) {
            Utils.logD("QcModemHydraStat", "Mbn_Space_Size_Info get failed");
            return;
        }
        int i2 = byteBuffer.getInt();
        long j = byteBuffer.getInt();
        long j2 = byteBuffer.getInt();
        long j3 = byteBuffer.getInt();
        Utils.logD("QcModemHydraStat", "modemEfsSize = " + i2 + ", fBsize = " + j + ", fBavail = " + j2 + ", fFavail = " + j3);
        long j4 = j3 * j;
        long j5 = j * j2;
        Utils.logD("QcModemHydraStat", "strSizeModem = " + ModemUtils.sizeByteToString((long) i2) + ", byteSizeAll = " + j4 + ", byteSizeAvail = " + j5 + ", strSizeAll = " + ModemUtils.sizeByteToString(j4) + ", strSizeAvail = " + ModemUtils.sizeByteToString(j5));
        concurrentHashMap.put("mbn_space_all", Integer.valueOf(i2));
        concurrentHashMap.put("mbn_space_left", Long.valueOf(j5));
        List mbnInfoList = getMbnInfoList("mbn_feature_info");
        for (int i3 = 0; i3 < mbnInfoList.size(); i3++) {
            concurrentHashMap.put((String) mbnInfoList.get(i3), SystemProperties.get((String) mbnInfoList.get(i3), "null"));
        }
        arrayList.add(concurrentHashMap);
        Utils.logD("QcModemHydraStat", "Mbn_Space_Size_Info:" + arrayList);
        checkAndReport(arrayList, "Mbn_Space_Size_Info");
    }

    private void pullNrTxInfo() {
        ArrayList arrayList = new ArrayList();
        String[] strArr = {"N1", "N28", "N41", "N78", "N5"};
        Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getNrTxPortInfo", 0);
        if (objCallQcrilHookGetObject == null) {
            return;
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byteBuffer.getInt();
            for (int i = 0; i < 5; i++) {
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                concurrentHashMap.put("band", strArr[i]);
                for (int i2 = 0; i2 < 3; i2++) {
                    concurrentHashMap.put("txPort" + i2, Long.valueOf(Integer.toUnsignedLong(byteBuffer.getInt())));
                }
                arrayList.add(concurrentHashMap);
            }
            Utils.logD("QcModemHydraStat", "NrTxInfo:" + arrayList);
            checkAndReport(arrayList, "NrTxInfo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pullMtConnFailInfo() {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            try {
                ByteBuffer mtConnFailInfo = this.mModemUtil.getMtConnFailInfo(i2);
                if (mtConnFailInfo == null) {
                    Utils.logD("QcModemHydraStat", "pullMtConnFailInfo, ret = null, subId: " + i2);
                } else {
                    byte b = mtConnFailInfo.get();
                    short s = mtConnFailInfo.getShort();
                    Utils.logD("QcModemHydraStat", "pullMtConnFailInfo, subId: " + i2 + ", version: " + ((int) b) + ", count: " + ((int) s));
                    if (b == 1) {
                        for (int i3 = 0; i3 < s; i3++) {
                            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                            long j = mtConnFailInfo.getLong();
                            concurrentHashMap.put("call_rat", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("nr5g_call_rlf_happen_num", Short.valueOf(mtConnFailInfo.getShort()));
                            concurrentHashMap.put("nr5g_call_rach_reason", Short.valueOf(mtConnFailInfo.getShort()));
                            concurrentHashMap.put("nr5g_call_rach_result", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("lte_call_rach_reason", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("lte_call_rach_contention", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("lte_call_rach_result", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("lte_call_rach_contention_procedure", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("call_is_roam", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("call_aver_pusch_pa", Float.valueOf(mtConnFailInfo.getFloat()));
                            concurrentHashMap.put("call_aver_mtpl", Float.valueOf(mtConnFailInfo.getFloat()));
                            concurrentHashMap.put("call_end_reason", Short.valueOf(mtConnFailInfo.getShort()));
                            concurrentHashMap.put("call_end_underflow_loss_rate", Float.valueOf(mtConnFailInfo.getFloat()));
                            concurrentHashMap.put("call_begin_band", Short.valueOf(mtConnFailInfo.getShort()));
                            concurrentHashMap.put("call_band_switch_flag", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("lte_call_deactivate_eps_bearer_flag", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("lte_call_deactivate_eps_bearer_cause", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("nr5g_call_pdu_session_release_flag", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("nr5g_call_pdu_session_release_cause", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("call_nr5g_irat_lte_flag", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("call_fbrx_error_flag", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("call_rtp_sn_not_update_flag", Byte.valueOf(mtConnFailInfo.get()));
                            concurrentHashMap.put("call_end_band", Short.valueOf(mtConnFailInfo.getShort()));
                            concurrentHashMap.put("call_cell_id", Long.valueOf(mtConnFailInfo.getLong()));
                            concurrentHashMap.put("call_pci", Short.valueOf(mtConnFailInfo.getShort()));
                            concurrentHashMap.put("call_ul_bler", Float.valueOf(mtConnFailInfo.getFloat()));
                            concurrentHashMap.put("call_dl_bler", Float.valueOf(mtConnFailInfo.getFloat()));
                            concurrentHashMap.put("scenario", Byte.valueOf(mtConnFailInfo.get()));
                            arrayList.add(concurrentHashMap);
                            concurrentHashMap2.put("time", SecondsToDateTime(j));
                            concurrentHashMap2.put("sub_id", Integer.valueOf(i2));
                            for (Map.Entry entry : concurrentHashMap.entrySet()) {
                                if (entry.getValue() instanceof Byte) {
                                    concurrentHashMap2.put((String) entry.getKey(), Short.valueOf(((Byte) entry.getValue()).byteValue()));
                                } else {
                                    concurrentHashMap2.put((String) entry.getKey(), entry.getValue());
                                }
                            }
                            ModemDFS.getInstance().recordDFSEventStatistic(915200015, concurrentHashMap2);
                        }
                    } else {
                        Utils.logE("QcModemHydraStat", "version is not support");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        while (i < arrayList.size()) {
            int i4 = i + 5;
            int iMin = Math.min(i4, arrayList.size());
            StringBuilder sb = new StringBuilder();
            sb.append("MtConnFailInfo[");
            sb.append(i);
            sb.append("-");
            sb.append(iMin - 1);
            sb.append("]:");
            sb.append(arrayList.subList(i, iMin));
            Utils.logD("QcModemHydraStat", sb.toString());
            i = i4;
        }
        if (arrayList.isEmpty()) {
            Utils.logD("QcModemHydraStat", "MtConnFailInfo: empty");
        }
        checkAndReport(arrayList, "mtConnFailInfo");
    }

    private void pullAbnormalTauCountInfo() {
        ArrayList arrayList = new ArrayList();
        Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getAbnormalTauCountInfo", -1);
        if (objCallQcrilHookGetObject == null) {
            Utils.logD("QcModemHydraStat", "abnormalTauInfo:" + arrayList);
            return;
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byte b = byteBuffer.get();
            byte b2 = byteBuffer.get();
            Utils.logD("QcModemHydraStat", "version: " + ((int) b) + ", count: " + ((int) b2));
            if (b == 1) {
                for (int i = 0; i < b2; i++) {
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    concurrentHashMap.put("hplmn", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("epsUpdateResult", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("imsVops", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("causeValue", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("addUpdateResult", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("sos", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("tauDisconnect", Integer.valueOf(byteBuffer.getInt()));
                    arrayList.add(concurrentHashMap);
                }
                Utils.logD("QcModemHydraStat", "abnormalTauInfo:" + arrayList);
                checkAndReport(arrayList, "abnormalTauInfo");
                return;
            }
            Utils.logE("QcModemHydraStat", "version is not support");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pull380Info() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("get380Info", i);
            Utils.logD("QcModemHydraStat", "alternative_Info-sub=" + i);
            if (objCallQcrilHookGetObject == null) {
                break;
            }
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byte b = byteBuffer.get();
            short s = byteBuffer.getShort();
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            StringBuilder sb = new StringBuilder();
            sb.setLength(0);
            concurrentHashMap.put("version", Byte.valueOf(b));
            if (s > 0) {
                arrayList.add(concurrentHashMap);
            }
            for (int i2 = 0; i2 < s; i2++) {
                ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                concurrentHashMap2.put("sub", Integer.valueOf(i));
                for (int i3 = 0; i3 < 8; i3++) {
                    byte b2 = byteBuffer.get();
                    if (b2 != 0) {
                        sb.append((char) b2);
                    }
                }
                concurrentHashMap2.put("call_number", sb.toString());
                sb.setLength(0);
                for (int i4 = 0; i4 < 16; i4++) {
                    byte b3 = byteBuffer.get();
                    if (b3 != 0) {
                        sb.append((char) b3);
                    }
                }
                concurrentHashMap2.put("emc_type", sb.toString());
                sb.setLength(0);
                for (int i5 = 0; i5 < 64; i5++) {
                    byte b4 = byteBuffer.get();
                    if (b4 != 0) {
                        sb.append((char) b4);
                    }
                }
                concurrentHashMap2.put("reason", sb.toString());
                sb.setLength(0);
                concurrentHashMap2.put("rat_type", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("ims_redial_sos", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("cs_redial_sos", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("call_end_reason", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap2.put("is_roam", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("hplmn", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap2.put("vplmn", Integer.valueOf(byteBuffer.getInt()));
                arrayList.add(concurrentHashMap2);
            }
        }
        Utils.logD("QcModemHydraStat", "alternative_Info:" + arrayList);
        checkAndReport(arrayList, "alternative_Info");
    }

    private void pull6RXInfo() {
        Object objCallQcrilHookGetObject;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT && (objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("get6RXInfo", i)) != null; i++) {
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byte b = byteBuffer.get();
            short s = byteBuffer.getShort();
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("version", Byte.valueOf(b));
            if (s > 0) {
                arrayList.add(concurrentHashMap);
            }
            for (int i2 = 0; i2 < s; i2++) {
                ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                concurrentHashMap2.put("sub", Integer.valueOf(i));
                if (b == 3) {
                    concurrentHashMap2.put("sixrx_time_n41", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap2.put("sixrx_time_n78", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap2.put("time_n41", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap2.put("time_n78", Integer.valueOf(byteBuffer.getInt()));
                } else {
                    concurrentHashMap2.put("sixrx_count", Integer.valueOf(byteBuffer.getInt()));
                }
                concurrentHashMap2.put("raster_arfcn", Integer.valueOf(byteBuffer.getInt()));
                for (int i3 = 0; i3 < 6; i3++) {
                    concurrentHashMap2.put("rsrp_6rx" + i3, Float.valueOf(byteBuffer.getFloat()));
                }
                concurrentHashMap2.put("cq_rsrp", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("cq_rsrq", Float.valueOf(byteBuffer.getFloat()));
                for (int i4 = 0; i4 < 6; i4++) {
                    concurrentHashMap2.put("snr_6rx" + i4, Float.valueOf(byteBuffer.getFloat()));
                }
                concurrentHashMap2.put("metrics_cqi", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("metrics_ri", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("tb_size", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap2.put("mcs", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("num_rbs", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap2.put("num_layers", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("mod_type", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("bler", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("channel_type", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("tx_power_pusch", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("mtpl_pusch", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("pathloss_pusch", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("sixrx_status", Integer.valueOf(byteBuffer.getInt()));
                if (b == 3) {
                    concurrentHashMap2.put("mcc", Short.valueOf(byteBuffer.getShort()));
                    concurrentHashMap2.put("mnc", Short.valueOf(byteBuffer.getShort()));
                    concurrentHashMap2.put("band", Short.valueOf(byteBuffer.getShort()));
                }
                arrayList.add(concurrentHashMap2);
            }
        }
        Utils.logD("QcModemHydraStat", "6RXInfo:" + arrayList);
        checkAndReport(arrayList, "sixrx_Info");
    }

    private void pullAirportModeInfo() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getAirportModeInfo", i);
            Utils.logD("QcModemHydraStat", "AirportModeInfo-sub=" + i);
            if (objCallQcrilHookGetObject == null) {
                break;
            }
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byte b = byteBuffer.get();
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("version", Byte.valueOf(b));
            concurrentHashMap.put("sub", Integer.valueOf(i));
            concurrentHashMap.put("TriggerCount", Integer.valueOf(byteBuffer.getInt()));
            arrayList.add(concurrentHashMap);
        }
        Utils.logD("QcModemHydraStat", "AirportModeInfo:" + arrayList);
        checkAndReport(arrayList, "AirportModeInfo");
    }

    private void pullBorderModeInfo() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getBorderModeInfo", i);
            Utils.logD("QcModemHydraStat", "BorderModeInfo-sub=" + i);
            if (objCallQcrilHookGetObject == null) {
                break;
            }
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byte b = byteBuffer.get();
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("version", Byte.valueOf(b));
            concurrentHashMap.put("sub", Integer.valueOf(i));
            concurrentHashMap.put("border_mode_total_count", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1000", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1001", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1002", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1003", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1004", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1005", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1006", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1101", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1102", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1103", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1104", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1201", Integer.valueOf(byteBuffer.getInt()));
            concurrentHashMap.put("boeder_id_1202", Integer.valueOf(byteBuffer.getInt()));
            arrayList.add(concurrentHashMap);
        }
        Utils.logD("QcModemHydraStat", "BorderModeInfo:" + arrayList);
        checkAndReport(arrayList, "BorderModeInfo");
    }

    private void pullT1SInfo() {
        Object objCallQcrilHookGetObject;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT && (objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getT1SInfo", i)) != null; i++) {
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byte b = byteBuffer.get();
            short s = byteBuffer.getShort();
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("version", Byte.valueOf(b));
            if (s > 0) {
                arrayList.add(concurrentHashMap);
            }
            for (int i2 = 0; i2 < s; i2++) {
                ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                concurrentHashMap2.put("sub", Integer.valueOf(i));
                concurrentHashMap2.put("raster_arfcn", Integer.valueOf(byteBuffer.getInt()));
                for (int i3 = 0; i3 < 6; i3++) {
                    concurrentHashMap2.put("rsrp_6rx" + i3, Float.valueOf(byteBuffer.getFloat()));
                }
                concurrentHashMap2.put("cq_rsrp", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("cq_rsrq", Float.valueOf(byteBuffer.getFloat()));
                for (int i4 = 0; i4 < 6; i4++) {
                    concurrentHashMap2.put("snr_6rx" + i4, Float.valueOf(byteBuffer.getFloat()));
                }
                concurrentHashMap2.put("metrics_cqi", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("metrics_ri", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("tb_size", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap2.put("mcs", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("num_rbs", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap2.put("num_layers", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("mod_type", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("bler", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("channel_type", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap2.put("tx_power_pusch", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("mtpl_pusch", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("pathloss_pusch", Float.valueOf(byteBuffer.getFloat()));
                concurrentHashMap2.put("t1s_status", Integer.valueOf(byteBuffer.getInt()));
                if (b == 3) {
                    concurrentHashMap2.put("mcc", Short.valueOf(byteBuffer.getShort()));
                    concurrentHashMap2.put("mnc", Short.valueOf(byteBuffer.getShort()));
                    concurrentHashMap2.put("band", Short.valueOf(byteBuffer.getShort()));
                }
                arrayList.add(concurrentHashMap2);
            }
        }
        Utils.logD("QcModemHydraStat", "T1SInfo:" + arrayList);
        checkAndReport(arrayList, "t1s_Info");
    }

    private float safeGetFloat(ByteBuffer byteBuffer) {
        float f = byteBuffer.getFloat();
        if (Float.isNaN(f) || Float.isInfinite(f)) {
            return 0.0f;
        }
        return f;
    }

    private void pullVoiceQualityInfo() {
        String str;
        String str2;
        int i;
        boolean z;
        short s;
        int i2;
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        while (i3 < MiuiPhoneUtils.PHONE_COUNT) {
            boolean z2 = true;
            while (z2) {
                Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getVoiceQualityInfo", i3);
                if (objCallQcrilHookGetObject == null) {
                    Utils.logD("QcModemHydraStat", "pullVoiceQualityInfo, ret = null, subId: " + i3);
                    break;
                }
                ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
                byte b = byteBuffer.get();
                short s2 = byteBuffer.getShort();
                if (s2 < 3) {
                    z2 = false;
                }
                Utils.logD("QcModemHydraStat", "pullVoiceQualityInfo, subId: " + i3 + ", version: " + ((int) b) + ", count: " + ((int) s2));
                new ConcurrentHashMap();
                int i4 = 0;
                while (i4 < s2) {
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                    concurrentHashMap.put("subId", Integer.valueOf(i3));
                    concurrentHashMap.put("dl_rtp_silent_times", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("ul_rtp_silent_times", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("dl_rtp_500_times", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("dl_rtp_packet_loss_count", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("dl_rtp_packet_total_count", Integer.valueOf(byteBuffer.getInt()));
                    for (int i5 = 0; i5 < 5; i5++) {
                        concurrentHashMap.put("dl_loss_packet_level_times[" + i5 + "]", Integer.valueOf(byteBuffer.getInt()));
                    }
                    int i6 = 0;
                    while (true) {
                        str = "dl_loss_packet_rate_statics[";
                        if (i6 >= 10) {
                            break;
                        }
                        concurrentHashMap.put("dl_loss_packet_rate_statics[" + i6 + "]", Float.valueOf(this.safeGetFloat(byteBuffer)));
                        i6++;
                    }
                    concurrentHashMap.put("cs_flag", Byte.valueOf(byteBuffer.get()));
                    concurrentHashMap.put("rat_type", Byte.valueOf(byteBuffer.get()));
                    int i7 = 0;
                    while (true) {
                        str2 = "rsrp[";
                        i = i3;
                        if (i7 >= 10) {
                            break;
                        }
                        concurrentHashMap.put("rsrp[" + i7 + "]", Float.valueOf(byteBuffer.getFloat()));
                        i7++;
                        i3 = i;
                    }
                    int i8 = 0;
                    while (true) {
                        z = z2;
                        if (i8 >= 10) {
                            break;
                        }
                        concurrentHashMap.put("snr[" + i8 + "]", Float.valueOf(byteBuffer.getFloat()));
                        i8++;
                        z2 = z;
                    }
                    concurrentHashMap.put("call_duration", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("band", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("is_roam", Byte.valueOf(byteBuffer.get()));
                    concurrentHashMap.put("hplmn", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("vplmn", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("dl_bler", Float.valueOf(byteBuffer.getFloat()));
                    concurrentHashMap.put("dl_under_flow_count", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("call_begin_time", this.SecondsToDateTime(byteBuffer.getLong()));
                    concurrentHashMap.put("call_end_time", this.SecondsToDateTime(byteBuffer.getLong()));
                    if (b == 2) {
                        concurrentHashMap.put("rtp_overall_loss_gt5pct", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("rtp_no_dl_packet", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("rtp_3s_loss_gt10pct_times", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("rtp_3s_no_packet_times", Integer.valueOf(byteBuffer.getInt()));
                    }
                    arrayList.add(concurrentHashMap);
                    Utils.logD("QcModemHydraStat", "VoiceQualityInfo size:" + arrayList.size());
                    if (b == 2) {
                        String[] strArr = {"dl_loss_packet_level_times", "dl_loss_packet_rate_statics", "rsrp", "snr"};
                        for (Map.Entry entry : concurrentHashMap.entrySet()) {
                            short s3 = s2;
                            String str3 = (String) entry.getKey();
                            int i9 = i4;
                            int i10 = 0;
                            while (true) {
                                if (i10 >= 4) {
                                    if (entry.getValue() instanceof Byte) {
                                        concurrentHashMap2.put(str3, Short.valueOf(((Byte) entry.getValue()).byteValue()));
                                        break;
                                    } else {
                                        concurrentHashMap2.put(str3, entry.getValue());
                                        break;
                                    }
                                }
                                int i11 = i10;
                                if (str3.startsWith(strArr[i10] + "[")) {
                                    break;
                                } else {
                                    i10 = i11 + 1;
                                }
                            }
                            i4 = i9;
                            s2 = s3;
                        }
                        s = s2;
                        i2 = i4;
                        int[] iArr = new int[5];
                        int i12 = 0;
                        for (int i13 = 5; i12 < i13; i13 = 5) {
                            Object obj = concurrentHashMap.get("dl_loss_packet_level_times[" + i12 + "]");
                            int i14 = i12;
                            iArr[i14] = obj instanceof Integer ? ((Integer) obj).intValue() : 0;
                            i12 = i14 + 1;
                        }
                        concurrentHashMap2.put("dl_loss_packet_level_times", iArr);
                        float[] fArr = new float[10];
                        float[] fArr2 = new float[10];
                        float[] fArr3 = new float[10];
                        int i15 = 0;
                        for (int i16 = 10; i15 < i16; i16 = 10) {
                            Object obj2 = concurrentHashMap.get(str + i15 + "]");
                            String str4 = str;
                            Object obj3 = concurrentHashMap.get(str2 + i15 + "]");
                            String str5 = str2;
                            Object obj4 = concurrentHashMap.get("snr[" + i15 + "]");
                            ConcurrentHashMap concurrentHashMap3 = concurrentHashMap;
                            float fFloatValue = 0.0f;
                            fArr[i15] = obj2 instanceof Float ? ((Float) obj2).floatValue() : 0.0f;
                            fArr2[i15] = obj3 instanceof Float ? ((Float) obj3).floatValue() : 0.0f;
                            if (obj4 instanceof Float) {
                                fFloatValue = ((Float) obj4).floatValue();
                            }
                            fArr3[i15] = fFloatValue;
                            i15++;
                            str = str4;
                            str2 = str5;
                            concurrentHashMap = concurrentHashMap3;
                        }
                        concurrentHashMap2.put("dl_loss_packet_rate_statics", fArr);
                        concurrentHashMap2.put("rsrp", fArr2);
                        concurrentHashMap2.put("snr", fArr3);
                        ModemDFS.getInstance().recordDFSEventStatistic(915200012, concurrentHashMap2);
                    } else {
                        s = s2;
                        i2 = i4;
                    }
                    i4 = i2 + 1;
                    this = this;
                    i3 = i;
                    z2 = z;
                    byteBuffer = byteBuffer;
                    b = b;
                    s2 = s;
                }
                this = this;
            }
            i3++;
        }
        int i17 = 0;
        while (i17 < arrayList.size()) {
            int i18 = i17 + 3;
            int iMin = Math.min(i18, arrayList.size());
            StringBuilder sb = new StringBuilder("VoiceQualityInfo[");
            sb.append(i17);
            sb.append("-");
            sb.append(iMin - 1);
            sb.append("]:");
            sb.append(arrayList.subList(i17, iMin));
            Utils.logD("QcModemHydraStat", sb.toString());
            i17 = i18;
        }
        if (arrayList.isEmpty()) {
            Utils.logD("QcModemHydraStat", "VoiceQualityInfo: empty");
        }
        checkAndReport(arrayList, "VoiceQuality_Info");
    }

    private void pullNsaBandCombInfo() {
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        int i = 0;
        while (z && i < 50) {
            Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getNsaBandCombInfo", -1);
            i++;
            if (objCallQcrilHookGetObject == null) {
                break;
            }
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byteBuffer.get();
            short s = byteBuffer.getShort();
            if (s < 41) {
                z = false;
            }
            for (int i2 = 0; i2 < s; i2++) {
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                concurrentHashMap.put("mcc", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap.put("mnc", Short.valueOf(byteBuffer.getShort()));
                for (int i3 = 0; i3 < 5; i3++) {
                    concurrentHashMap.put("lteBands" + i3, Short.valueOf(byteBuffer.getShort()));
                }
                for (int i4 = 0; i4 < 2; i4++) {
                    concurrentHashMap.put("nr5gBands" + i4, Short.valueOf(byteBuffer.getShort()));
                }
                arrayList.add(concurrentHashMap);
            }
        }
        Utils.logD("QcModemHydraStat", "NsaBandComb:" + arrayList);
        checkAndReport(arrayList, "NsaBandComb");
    }

    private void pullRfBandAndAntnfo(boolean z, String str) {
        String[] strArr;
        char c;
        String[] strArr2 = {"getLteRfBandAndAntennaNumberInfo", "getNr5gRfBandAndAntennaNumberInfo"};
        char c2 = '<';
        if (this.bandAndAntsList.size() > 60) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            String str2 = strArr2[i];
            int i2 = 0;
            while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                boolean z2 = true;
                int i3 = 0;
                while (z2 && i3 < 50) {
                    Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject(str2, i2);
                    if (z) {
                        return;
                    }
                    if (objCallQcrilHookGetObject == null) {
                        break;
                    }
                    ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
                    boolean z3 = str2.charAt(3) == 'L';
                    byteBuffer.get();
                    if (z3) {
                        byte b = byteBuffer.get();
                        byte b2 = byteBuffer.get();
                        int i4 = 0;
                        while (i4 < b) {
                            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                            concurrentHashMap.put("lteBand", Integer.valueOf(i4));
                            int i5 = 0;
                            while (i5 < b2) {
                                concurrentHashMap.put("lteAnt" + i5, Integer.valueOf(byteBuffer.getInt()));
                                i5++;
                                strArr2 = strArr2;
                            }
                            String[] strArr3 = strArr2;
                            concurrentHashMap.put("sub", Integer.valueOf(i2));
                            concurrentHashMap.put("scene", str);
                            this.bandAndAntsList.add(concurrentHashMap);
                            if (this.bandAndAntsList.size() > 60) {
                                return;
                            }
                            i4++;
                            strArr2 = strArr3;
                            c2 = '<';
                        }
                        strArr = strArr2;
                        c = c2;
                        z2 = false;
                    } else {
                        strArr = strArr2;
                        short s = byteBuffer.getShort();
                        i3++;
                        if (s < 16) {
                            z2 = false;
                        }
                        byte b3 = byteBuffer.get();
                        int i6 = 0;
                        while (i6 < s) {
                            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                            concurrentHashMap2.put("nrAnt", Short.valueOf(byteBuffer.getShort()));
                            int i7 = 0;
                            while (i7 < b3) {
                                concurrentHashMap2.put("nrBand" + i7, Integer.valueOf(byteBuffer.getInt()));
                                i7++;
                                s = s;
                            }
                            short s2 = s;
                            concurrentHashMap2.put("sub", Integer.valueOf(i2));
                            concurrentHashMap2.put("scene", str);
                            this.bandAndAntsList.add(concurrentHashMap2);
                            if (this.bandAndAntsList.size() > 60) {
                                return;
                            }
                            i6++;
                            s = s2;
                        }
                        c = '<';
                    }
                    c2 = c;
                    strArr2 = strArr;
                }
                i2++;
                c2 = c2;
                strArr2 = strArr2;
            }
        }
    }

    private void pullImsSipMessageInfo() {
        int unsignedInt;
        String strValueOf;
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        int i = 0;
        while (z && i < 50) {
            Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getImsSipMessageInfo", -1);
            if (objCallQcrilHookGetObject == null) {
                break;
            }
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byteBuffer.get();
            short s = byteBuffer.getShort();
            i++;
            if (s < 17) {
                z = false;
            }
            for (int i2 = 0; i2 < s; i2++) {
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                concurrentHashMap.put("ts", Long.valueOf(byteBuffer.getLong()));
                byte b = byteBuffer.get();
                short s2 = byteBuffer.getShort();
                short s3 = byteBuffer.getShort();
                if (b == 1) {
                    unsignedInt = Short.toUnsignedInt(byteBuffer.getShort());
                    strValueOf = String.valueOf(byteBuffer.getInt());
                } else {
                    unsignedInt = byteBuffer.getInt();
                    strValueOf = String.valueOf(byteBuffer.getLong());
                }
                concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s2), Short.valueOf(s3), strValueOf));
                concurrentHashMap.put("tac", Integer.valueOf(unsignedInt));
                concurrentHashMap.put("arfcn", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap.put("pci", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap.put("Band", Integer.valueOf(byteBuffer.getInt()));
                if (b != 1) {
                    concurrentHashMap.put("scs", Byte.valueOf(byteBuffer.get()));
                }
                concurrentHashMap.put("dir", Byte.valueOf(byteBuffer.get()));
                concurrentHashMap.put("msgId", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap.put("resCode", Short.valueOf(byteBuffer.getShort()));
                arrayList.add(concurrentHashMap);
            }
        }
        Utils.logD("QcModemHydraStat", "ImsSipMessage: " + arrayList);
        checkAndReport(arrayList, "ImsSipMessage");
    }

    private void pullLteTimerExpiry() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            boolean z = true;
            int i2 = 0;
            while (z && i2 < 50) {
                Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getLteTimerExpiryInfo", i);
                if (objCallQcrilHookGetObject == null) {
                    break;
                }
                ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
                byteBuffer.get();
                short s = byteBuffer.getShort();
                i2++;
                if (s < 77) {
                    z = false;
                }
                for (int i3 = 0; i3 < s; i3++) {
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    concurrentHashMap.put("ts", Long.valueOf(byteBuffer.getLong()));
                    concurrentHashMap.put("id", Byte.valueOf(byteBuffer.get()));
                    concurrentHashMap.put("sub", Integer.valueOf(i));
                    arrayList.add(concurrentHashMap);
                }
            }
        }
        Utils.logD("QcModemHydraStat", "LteTimerExpiry: " + arrayList);
        checkAndReport(arrayList, "LteTimerExpiry");
    }

    private void pullDLBoostInfo() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            Object objCallQcrilHookGetObject = this.mModemUtil.callQcrilHookGetObject("getNr5gSawAndSawlessTimeStatisticsInfo", i);
            if (objCallQcrilHookGetObject == null) {
                return;
            }
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            Utils.logD("QcModemHydraStat", "pullDLBoostInfo is not null");
            if (byteBuffer.get() == 0) {
                concurrentHashMap.put("nr5gSawTime", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap.put("nr5gSawLessTime", Integer.valueOf(byteBuffer.getInt()));
            } else {
                concurrentHashMap.put("n38SawTime", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap.put("n38SawLessTime", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap.put("n41SawTime", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap.put("n41SawLessTime", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap.put("n77SawTime", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap.put("n77SawLessTime", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap.put("n78SawTime", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap.put("n78SawLessTime", Integer.valueOf(byteBuffer.getInt()));
            }
            concurrentHashMap.put("sub", Integer.valueOf(i));
            arrayList.add(concurrentHashMap);
        }
        checkAndReport(arrayList, "event_dl_boost");
    }

    class ModemExceptionCollector {
        private boolean toContinue;
        private final String LTE_RACH_FAILURE = "LteRach";
        private final String LTE_RLF_FAILURE = "LteRlf";
        private final String NR_RACH_FAILURE = "NrRach";
        private final String NR_RLF_FAILURE = "NrRlf";
        private final String LTE_NAS_REJECT = "LteNas";
        private final String NR_NAS_REJECT = "NrNas";
        private final String LTE_FULL_SCH = "LteSch";
        private final String NR_FULL_SCH = "NrSch";
        private final String LTE_BLER_EXCEPTION = "LteBler";
        private final String NR_BLER_EXCEPTION = "NrBler";
        private final String[] recordKeys = {"LteRach", "LteRlf", "LteNas", "LteSch", "LteBler", "NrRach", "NrRlf", "NrNas", "NrSch", "NrBler"};
        private final String[] methods = {"getLteRachFailInfo", "getLteRlfInfo", "getLteNasRejectInfo", "getLteFullSchInfo", "getLteBlerAbnormalInfo", "getNr5gRachFailInfo", "getNr5gRlfInfo", "getNr5gNasRejectInfo", "getNr5gFullSchInfo", "getNr5gBlerAbnormalInfo"};

        ModemExceptionCollector() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void pullModemRecords() {
            Object objCallQcrilHookGetObject;
            BadCellStats badCellStats = BadCellStats.getInstance();
            int length = this.recordKeys.length;
            int i = 0;
            while (i < length) {
                this.toContinue = true;
                ArrayList arrayList = new ArrayList();
                int i2 = 0;
                while (this.toContinue && i2 < 50 && (objCallQcrilHookGetObject = QcModemHydraStat.this.mModemUtil.callQcrilHookGetObject(this.methods[i], -1)) != null) {
                    try {
                        ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
                        byte b = byteBuffer.get();
                        Utils.logD("QcModemHydraStat", this.recordKeys[i] + " version = " + ((int) b));
                        int i3 = i2 + 1;
                        if (b == 0) {
                            try {
                                this.decodeBufferV0(byteBuffer, i < length / 2, arrayList);
                            } catch (Exception e) {
                                e = e;
                                i2 = i3;
                                e.printStackTrace();
                            }
                        } else if (b == 1) {
                            this.decodeBufferV1(byteBuffer, i < length / 2, arrayList);
                        } else if (b == 2) {
                            this.decodeBufferV2(byteBuffer, i < length / 2, arrayList);
                        } else if (b == 3) {
                            this.decodeBufferV3(byteBuffer, i < length / 2, this.methods[i], arrayList);
                        } else {
                            ModemExceptionCollector modemExceptionCollector = this;
                            try {
                                modemExceptionCollector.decodeBufferV4(byteBuffer, i < length / 2, this.methods[i], arrayList, b);
                                this = modemExceptionCollector;
                            } catch (Exception e2) {
                                e = e2;
                                this = modemExceptionCollector;
                                i2 = i3;
                                e.printStackTrace();
                            }
                        }
                        i2 = i3;
                    } catch (Exception e3) {
                        e = e3;
                    }
                }
                if (arrayList.size() == 0) {
                    arrayList = null;
                }
                Utils.logD("QcModemHydraStat", this.recordKeys[i] + " " + arrayList);
                if (badCellStats != null) {
                    badCellStats.syncBadCellInfo(this.methods[i], arrayList);
                }
                QcModemHydraStat.this.checkAndReport(arrayList, this.recordKeys[i]);
                i++;
            }
            if (badCellStats != null) {
                badCellStats.notifySyncCellInfoCompleted();
            }
        }

        private void decodeBufferV0(ByteBuffer byteBuffer, boolean z, ArrayList arrayList) {
            String strValueOf;
            short s = byteBuffer.getShort();
            Utils.logD("QcModemHydraStat", "decodeBuffer count = " + ((int) s));
            if (s < 43) {
                this.toContinue = false;
            }
            for (int i = 0; i < s; i++) {
                short s2 = byteBuffer.getShort();
                short s3 = byteBuffer.getShort();
                if (z) {
                    strValueOf = String.valueOf(byteBuffer.getInt());
                } else {
                    strValueOf = String.valueOf(byteBuffer.getLong());
                }
                int i2 = byteBuffer.getInt();
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s2), Short.valueOf(s3), strValueOf));
                concurrentHashMap.put("Count", Integer.valueOf(i2));
                arrayList.add(concurrentHashMap);
            }
        }

        private void decodeBufferV1(ByteBuffer byteBuffer, boolean z, ArrayList arrayList) {
            int unsignedInt;
            String strValueOf;
            short s = byteBuffer.getShort();
            if (s < 34) {
                this.toContinue = false;
            }
            for (int i = 0; i < s; i++) {
                short s2 = byteBuffer.getShort();
                short s3 = byteBuffer.getShort();
                if (z) {
                    unsignedInt = Short.toUnsignedInt(byteBuffer.getShort());
                    strValueOf = String.valueOf(byteBuffer.getInt());
                } else {
                    unsignedInt = byteBuffer.getInt();
                    strValueOf = String.valueOf(byteBuffer.getLong());
                }
                int i2 = byteBuffer.getInt();
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s2), Short.valueOf(s3), strValueOf));
                concurrentHashMap.put("tac", Integer.valueOf(unsignedInt));
                concurrentHashMap.put("Count", Integer.valueOf(i2));
                arrayList.add(concurrentHashMap);
            }
        }

        private void decodeBufferV2(ByteBuffer byteBuffer, boolean z, ArrayList arrayList) {
            int unsignedInt;
            String strValueOf;
            short s = byteBuffer.getShort();
            if (s < 23) {
                this.toContinue = false;
            }
            for (int i = 0; i < s; i++) {
                short s2 = byteBuffer.getShort();
                short s3 = byteBuffer.getShort();
                if (z) {
                    unsignedInt = Short.toUnsignedInt(byteBuffer.getShort());
                    strValueOf = String.valueOf(byteBuffer.getInt());
                } else {
                    unsignedInt = byteBuffer.getInt();
                    strValueOf = String.valueOf(byteBuffer.getLong());
                }
                int i2 = byteBuffer.getInt();
                short s4 = byteBuffer.getShort();
                int i3 = byteBuffer.getInt();
                int i4 = byteBuffer.getInt();
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s2), Short.valueOf(s3), strValueOf));
                concurrentHashMap.put("tac", Integer.valueOf(unsignedInt));
                concurrentHashMap.put("arfcn", Integer.valueOf(i2));
                concurrentHashMap.put("pci", Short.valueOf(s4));
                concurrentHashMap.put("Band", Integer.valueOf(i3));
                concurrentHashMap.put("Count", Integer.valueOf(i4));
                arrayList.add(concurrentHashMap);
            }
        }

        private void decodeBufferV3(ByteBuffer byteBuffer, boolean z, String str, ArrayList arrayList) {
            int unsignedInt;
            String strValueOf;
            short s = byteBuffer.getShort();
            if (s < 15) {
                this.toContinue = false;
            }
            for (int i = 0; i < s; i++) {
                short s2 = byteBuffer.getShort();
                short s3 = byteBuffer.getShort();
                if (z) {
                    unsignedInt = Short.toUnsignedInt(byteBuffer.getShort());
                    strValueOf = String.valueOf(byteBuffer.getInt());
                } else {
                    unsignedInt = byteBuffer.getInt();
                    strValueOf = String.valueOf(byteBuffer.getLong());
                }
                int i2 = byteBuffer.getInt();
                short s4 = byteBuffer.getShort();
                int i3 = byteBuffer.getInt();
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s2), Short.valueOf(s3), strValueOf));
                concurrentHashMap.put("tac", Integer.valueOf(unsignedInt));
                concurrentHashMap.put("arfcn", Integer.valueOf(i2));
                concurrentHashMap.put("pci", Short.valueOf(s4));
                concurrentHashMap.put("Band", Integer.valueOf(i3));
                str.hashCode();
                switch (str) {
                    case "getNr5gNasRejectInfo":
                    case "getNr5gRlfInfo":
                    case "getNr5gBlerAbnormalInfo":
                    case "getNr5gRachFailInfo":
                    case "getNr5gFullSchInfo":
                        concurrentHashMap.put("scs", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("Count", Integer.valueOf(byteBuffer.getInt()));
                        break;
                    case "getLteRlfInfo":
                        concurrentHashMap.put("cause", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap.put("A1", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A2", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A3", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A4", Integer.valueOf(byteBuffer.getInt()));
                        break;
                    case "getLteFullSchInfo":
                    case "getLteBlerAbnormalInfo":
                        concurrentHashMap.put("4sCnt", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("6sCnt", Integer.valueOf(byteBuffer.getInt()));
                        break;
                    case "getLteRachFailInfo":
                        concurrentHashMap.put("cause", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("A1", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A2", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A3", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A4", Integer.valueOf(byteBuffer.getInt()));
                        break;
                    case "getLteNasRejectInfo":
                        concurrentHashMap.put("Count", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("cause", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("msgType", Integer.valueOf(Byte.toUnsignedInt(byteBuffer.get())));
                        break;
                }
                arrayList.add(concurrentHashMap);
            }
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r9v11 */
        /* JADX WARN: Type inference failed for: r9v12 */
        /* JADX WARN: Type inference failed for: r9v13 */
        /* JADX WARN: Type inference failed for: r9v14 */
        /* JADX WARN: Type inference failed for: r9v15 */
        /* JADX WARN: Type inference failed for: r9v16 */
        /* JADX WARN: Type inference failed for: r9v17 */
        /* JADX WARN: Type inference failed for: r9v2 */
        /* JADX WARN: Type inference failed for: r9v20 */
        /* JADX WARN: Type inference failed for: r9v21 */
        /* JADX WARN: Type inference failed for: r9v22 */
        /* JADX WARN: Type inference failed for: r9v23 */
        /* JADX WARN: Type inference failed for: r9v24 */
        /* JADX WARN: Type inference failed for: r9v25 */
        /* JADX WARN: Type inference failed for: r9v26 */
        /* JADX WARN: Type inference failed for: r9v3 */
        private void decodeBufferV4(ByteBuffer byteBuffer, boolean z, String str, ArrayList arrayList, int i) {
            int unsignedInt;
            String strValueOf;
            short s = byteBuffer.getShort();
            boolean z2 = false;
            if (s < 15) {
                this.toContinue = false;
            }
            int i2 = 0;
            while (i2 < s) {
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                if ("getLteNasRejectInfo".equals(str) || "getNr5gNasRejectInfo".equals(str)) {
                    concurrentHashMap.put("ts", String.valueOf(byteBuffer.getLong()));
                }
                short s2 = byteBuffer.getShort();
                short s3 = byteBuffer.getShort();
                if (z) {
                    unsignedInt = Short.toUnsignedInt(byteBuffer.getShort());
                    strValueOf = String.valueOf(byteBuffer.getInt());
                } else {
                    unsignedInt = byteBuffer.getInt();
                    strValueOf = String.valueOf(byteBuffer.getLong());
                }
                int i3 = byteBuffer.getInt();
                short s4 = byteBuffer.getShort();
                int i4 = byteBuffer.getInt();
                concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s2), Short.valueOf(s3), strValueOf));
                concurrentHashMap.put("tac", Integer.valueOf(unsignedInt));
                concurrentHashMap.put("arfcn", Integer.valueOf(i3));
                concurrentHashMap.put("pci", Short.valueOf(s4));
                concurrentHashMap.put("Band", Integer.valueOf(i4));
                str.hashCode();
                ?? r9 = -1;
                r9 = -1;
                r9 = -1;
                r9 = -1;
                r9 = -1;
                r9 = -1;
                r9 = -1;
                r9 = -1;
                switch (str.hashCode()) {
                    case -1294661247:
                        if (str.equals("getNr5gNasRejectInfo")) {
                            r9 = z2;
                        }
                        break;
                    case -899804530:
                        if (str.equals("getNr5gRlfInfo")) {
                            r9 = 1;
                        }
                        break;
                    case -673184365:
                        if (str.equals("getLteRlfInfo")) {
                            r9 = 2;
                        }
                        break;
                    case -40816295:
                        if (str.equals("getNr5gBlerAbnormalInfo")) {
                            r9 = 3;
                        }
                        break;
                    case 330320396:
                        if (str.equals("getNr5gRachFailInfo")) {
                            r9 = 4;
                        }
                        break;
                    case 544201643:
                        if (str.equals("getNr5gFullSchInfo")) {
                            r9 = 5;
                        }
                        break;
                    case 1850991814:
                        if (str.equals("getLteNasRejectInfo")) {
                            r9 = 6;
                        }
                        break;
                }
                switch (r9) {
                    case 0:
                        concurrentHashMap.put("scs", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("msgType", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("cause", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("flag", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("sub", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("scene", Integer.valueOf(byteBuffer.getInt()));
                        break;
                    case 1:
                        concurrentHashMap.put("scs", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("cause", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap.put("A1", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A2", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A3", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A4", Integer.valueOf(byteBuffer.getInt()));
                        break;
                    case 2:
                        concurrentHashMap.put("cause", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap.put("hplmn", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A1", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A2", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A3", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A4", Integer.valueOf(byteBuffer.getInt()));
                        break;
                    case 3:
                    case 5:
                        concurrentHashMap.put("scs", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("4sCnt", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("6sCnt", Integer.valueOf(byteBuffer.getInt()));
                        break;
                    case 4:
                        concurrentHashMap.put("scs", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("cause", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("A1", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A2", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A3", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("A4", Integer.valueOf(byteBuffer.getInt()));
                        break;
                    case 6:
                        concurrentHashMap.put("msgType", Integer.valueOf(Byte.toUnsignedInt(byteBuffer.get())));
                        concurrentHashMap.put("cause", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("flag", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap.put("sub", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap.put("scene", Integer.valueOf(byteBuffer.getInt()));
                        if (i == 5) {
                            concurrentHashMap.put("hplmn", Integer.valueOf(byteBuffer.getInt()));
                            concurrentHashMap.put("isEplmn", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap.put("plmnCat", Byte.valueOf(byteBuffer.get()));
                        }
                        break;
                    default:
                        continue;
                        arrayList.add(concurrentHashMap);
                        i2++;
                        z2 = false;
                        break;
                }
                arrayList.add(concurrentHashMap);
                i2++;
                z2 = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCommonDataFromBytes(ByteBuffer byteBuffer, boolean z, ConcurrentHashMap concurrentHashMap) {
        int unsignedInt;
        String strValueOf;
        short s = byteBuffer.getShort();
        short s2 = byteBuffer.getShort();
        if (z) {
            unsignedInt = Short.toUnsignedInt(byteBuffer.getShort());
            strValueOf = String.valueOf(byteBuffer.getInt());
        } else {
            unsignedInt = byteBuffer.getInt();
            strValueOf = String.valueOf(byteBuffer.getLong());
        }
        int i = byteBuffer.getInt();
        short s3 = byteBuffer.getShort();
        int i2 = byteBuffer.getInt();
        concurrentHashMap.put("GCI", String.format("%03d%02d-%s", Short.valueOf(s), Short.valueOf(s2), strValueOf));
        concurrentHashMap.put("tac", Integer.valueOf(unsignedInt));
        concurrentHashMap.put("arfcn", Integer.valueOf(i));
        concurrentHashMap.put("pci", Short.valueOf(s3));
        concurrentHashMap.put("Band", Integer.valueOf(i2));
    }

    class MultiModemInfoCollector {
        private final String LTE_RSSI_SIG_PATH = "LteRssiSigPath";
        private final String NR_RSSI_SIG_PATH = "NrRssiSigPath";
        private final String NR_CHANNEL = "NrChannel";
        private final String LTE_CHANNEL = "LteChannel";
        private final String LTE_NLIC_ENGINE = "LteNlicEngine";
        private final String LTE_EVENT_THR = "LteEventThr";
        private final String TX_BW = "TxBw";
        private final String CAFI_SNS_INFO = "CafiSnsInfo";
        private final String LTE_SNS_DATA_STALL = "LteSnsDataStall";
        private final String NR_SNS_DATA_STALL = "NrSnsDataStall";
        private final String LTE_SET_DUBIOUS_CELL = "LteSetDubiousCell";
        private final String NR_SET_DUBIOUS_CELL = "NrSetDubiousCell";
        private final String LTE_SNS_INFO = "LteSnsInfo";
        private final String NR_SNS_INFO = "NrSnsInfo";
        private final String ALL_SCENE_TXPOWER = "AllSceneTxpower";
        private final String ALL_SCENE_RSRP = "AllSceneRsrp";
        private final String ALL_SCENE_SNR = "AllSceneSnr";
        private final String NR_SNS_AI_INFO = "NrSnsAiInfo";
        private final String RSRP_STAGES_TXPOWER = "RsrpStagesTxpower";
        private final String RSRP_STAGES_SNR = "RsrpStagesSnr";
        private final String RX6_COUNT = "Rx6Count";
        private final String NR_PUSCH_TX_MODE_COUNT = "NrPuschTxModeCount";
        private final String LTE_RESEL_THR = "LteReselThr";
        private final String NR_RESEL_THR = "NrReselThr";
        private final String LTE_RESEL_BAND_RSRP = "LteReselBandRsrp";
        private final String NR_RESEL_BAND_RSRP = "NrReselBandRsrp";
        private final String LTE_HO_BAND_RSRP = "LteHoBandRsrp";
        private final String NR_HO_BAND_RSRP = "NrHoBandRsrp";
        private final String NR_TX_POWER_BY_CHANNEL = "NrTxPowerByChannel";
        private final String NR_MSG1_MSG3_FAIL = "NrMsg1Msg3Fail";
        private final String LTE_MSG1_MSG3_FAIL = "LteMsg1Msg3Fail";
        private final String NR_RACH_FAIL_RATE = "NrRachFailRate";
        private final String LTE_RACH_FAIL_RATE = "LteRachFailRate";
        private final String LTE_NR_FBRX_POWER = "LteNrFbrxPower";
        private final String NR_PDSCH_NUM_LAYERS_COUNT = "NrPdschNumLayersCount";
        private final String NR_SNS_DATA_STALL_V2 = "NrSnsDataStallV2";
        private final String NR_SNS_INFO_V2 = "NrSnsInfoV2";
        private final String GPS_ON_DURATION_AND_COUNT = "GpsOnDurationAndCount";
        private final String LTE_SNS_DATA_STALL_V2 = "LteSnsDataStallV2";
        private final String LTE_SNS_INFO_V2 = "LteSnsInfoV2";
        private final String NR_SNS_RRC_MITIGATION_INFO = "NrSnsRrcMitigationInfo";
        private final String LTE_SNS_RRC_MITIGATION_INFO = "LteSnsRrcMitigationInfo";
        private final String NR_TX_ANT_COUNT = "NrTxAntCount";
        private final String[] recordOnceKeys = {"LteRssiSigPath", "NrRssiSigPath", "NrChannel", "LteChannel", "LteEventThr", "TxBw", "AllSceneTxpower", "AllSceneRsrp", "AllSceneSnr", "LteReselThr", "LteReselBandRsrp", "LteHoBandRsrp", "NrSnsAiInfo", "RsrpStagesTxpower", "RsrpStagesSnr", "Rx6Count", "NrPuschTxModeCount", "NrReselThr", "NrReselBandRsrp", "NrHoBandRsrp", "NrTxPowerByChannel", "NrMsg1Msg3Fail", "LteMsg1Msg3Fail", "NrRachFailRate", "LteRachFailRate", "LteNrFbrxPower", "NrPdschNumLayersCount", "GpsOnDurationAndCount", "NrTxAntCount"};
        private final String[] multiRecordKeys = {"LteNlicEngine", "LteSnsDataStall", "CafiSnsInfo", "LteSetDubiousCell", "LteSnsInfo", "NrSnsDataStall", "NrSetDubiousCell", "NrSnsInfo", "NrSnsInfoV2", "NrSnsDataStallV2", "LteSnsInfoV2", "LteSnsDataStallV2", "NrSnsRrcMitigationInfo", "LteSnsRrcMitigationInfo"};
        private final String[] multiPollMethods = {"getLteNlicEngineActiveInfo", "getLteSnsDataStallInfo", "getCafiSnsInfo", "getLteSnsSetDubiousCellInfo", "getLteSnsInfo", "getNr5gSnsDataStallInfo", "getNr5gSnsSetDubiousCellInfo", "getNr5gSnsInfo", "getNr5gSnsInfoV2", "getNr5gSnsDataStallInfoV2", "getLteSnsInfoV2", "getLteSnsDataStallInfoV2", "getNr5gSnsRrcMitigationInfo", "getLteSnsRrcMitigationInfo"};
        private final String[] pollOnceMethods = {"getLteRssiAndSigPathInfo", "getNrRssiAndSigPathInfo", "getNrChannelInfo", "getLteChannelInfo", "getLteEventThresholdValueInfo", "getLteAndNr5gBandwidthInfo", "getLteNrBandAndTxPowerInfo", "getLteNrBandAndRsrpInfo", "getLteNrBandAndSinrInfo", "getLteReselThresholdInfo", "getLteReselBandAndRsrpInfo", "getLtehoBandAndRsrpInfo", "getNr5gSnsAiInfo", "getRsrpAndTxPowerInfo", "getRsrpAndSinrInfo", "get6RxCountInfo", "getNrPuschTxModeCountInfo", "getNr5gReselThresholdInfo", "getNr5gReselBandAndRsrpInfo", "getNr5ghoBandAndRsrpInfo", "getNrTxPowerByChannelInfo", "getNr5gMsg1AndMsg3SendFailInfo", "getLteMsg1AndMsg3SendFailInfo", "getNr5gRachFailRateInfo", "getLteRachFailRateInfo", "getLteNrBandAndFbrxPowerInfo", "getNrPdschNumLayersCountInfo", "getGpsOnDurationAndCountInfo", "getNr5gTxAntCountInfo"};
        private final String[] NrBand = {"N1", "N28", "N41", "N78", "N5"};
        private final String[] LteNrBand = {"B1", "B3", "B5", "B7", "B8", "B20", "B28", "B39", "B40", "B41", "N1", "N28", "N41", "N78", "N5"};
        private final String[] LteNrBandVersionO = {"B1", "B3", "B5", "B34", "B40", "B41", "N1", "N28", "N41", "N78"};
        int bandIdx = 0;
        int stageIdx = 0;

        MultiModemInfoCollector() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void pullModemInfoData() {
            for (int i = 0; i < this.recordOnceKeys.length; i++) {
                ArrayList arrayList = new ArrayList();
                if ("AllSceneTxpower".equals(this.recordOnceKeys[i]) || "AllSceneRsrp".equals(this.recordOnceKeys[i]) || "AllSceneSnr".equals(this.recordOnceKeys[i]) || "RsrpStagesTxpower".equals(this.recordOnceKeys[i]) || "RsrpStagesSnr".equals(this.recordOnceKeys[i])) {
                    for (int i2 = 0; i2 < this.LteNrBand.length * 4 && pullModemRecordsOnce(i, arrayList, i2); i2++) {
                    }
                } else if ("NrTxPowerByChannel".equals(this.recordOnceKeys[i])) {
                    for (int i3 = 0; i3 < this.NrBand.length && pullModemRecordsOnce(i, arrayList, i3); i3++) {
                    }
                } else {
                    for (int i4 = 0; i4 < 50 && pullModemRecordsOnce(i, arrayList, i4); i4++) {
                    }
                }
                Utils.logD("QcModemHydraStat", this.recordOnceKeys[i] + " " + arrayList);
                QcModemHydraStat.this.checkAndReport(arrayList, this.recordOnceKeys[i]);
            }
            for (int i5 = 0; i5 < this.multiRecordKeys.length; i5++) {
                ArrayList arrayList2 = new ArrayList();
                for (int i6 = 0; i6 < MiuiPhoneUtils.PHONE_COUNT; i6++) {
                    for (int i7 = 0; i7 < 50 && pullModemRecordsOnSub(i5, i6, arrayList2); i7++) {
                    }
                }
                Utils.logD("QcModemHydraStat", this.multiRecordKeys[i5] + " " + arrayList2);
                QcModemHydraStat.this.checkAndReport(arrayList2, this.multiRecordKeys[i5]);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code duplicated, block: B:12:0x003a  */
        private boolean pullModemRecordsOnce(int i, ArrayList arrayList, int i2) {
            String[] strArr;
            boolean z;
            int i3;
            Object objCallQcrilHookGetObject = QcModemHydraStat.this.mModemUtil.callQcrilHookGetObject(this.pollOnceMethods[i], -1);
            if (objCallQcrilHookGetObject == null) {
                return false;
            }
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byte b = byteBuffer.get();
            if (b == 0) {
                strArr = this.LteNrBandVersionO;
            } else {
                strArr = b == 1 ? this.LteNrBand : this.NrBand;
            }
            String str = this.pollOnceMethods[i];
            str.hashCode();
            switch (str) {
                case "getLteEventThresholdValueInfo":
                    short s = byteBuffer.getShort();
                    boolean z2 = s > 5;
                    for (int i4 = 0; i4 < s; i4++) {
                        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                        QcModemHydraStat.this.updateCommonDataFromBytes(byteBuffer, true, concurrentHashMap);
                        concurrentHashMap.put("ts", Long.valueOf(byteBuffer.getLong()));
                        arrayList.add(concurrentHashMap);
                        byte b2 = byteBuffer.get();
                        for (int i5 = 0; i5 < b2; i5++) {
                            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                            concurrentHashMap2.put("eventId", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap2.put("freq", Integer.valueOf(byteBuffer.getInt()));
                            concurrentHashMap2.put("thr_1", Short.valueOf(byteBuffer.getShort()));
                            concurrentHashMap2.put("thr_2", Short.valueOf(byteBuffer.getShort()));
                            arrayList.add(concurrentHashMap2);
                        }
                    }
                    return z2;
                case "getNr5ghoBandAndRsrpInfo":
                case "getNr5gReselBandAndRsrpInfo":
                    short s2 = byteBuffer.getShort();
                    boolean z3 = s2 < 43;
                    for (int i6 = 0; i6 < s2; i6++) {
                        ConcurrentHashMap concurrentHashMap3 = new ConcurrentHashMap();
                        concurrentHashMap3.put("cid", Long.valueOf(byteBuffer.getLong()));
                        concurrentHashMap3.put("band", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap3.put("rsrp", Float.valueOf(byteBuffer.getFloat()));
                        arrayList.add(concurrentHashMap3);
                    }
                    return z3;
                case "getNr5gTxAntCountInfo":
                    String[] strArr2 = {"N41", "N78", "N28", "N1", "N5"};
                    for (int i7 = 0; i7 < 5; i7++) {
                        ConcurrentHashMap concurrentHashMap4 = new ConcurrentHashMap();
                        for (int i8 = 0; i8 < 20; i8++) {
                            concurrentHashMap4.put("txAnt" + i8, Integer.valueOf(byteBuffer.getInt()));
                        }
                        concurrentHashMap4.put("band", strArr2[i7]);
                        arrayList.add(concurrentHashMap4);
                    }
                    return false;
                case "getNr5gSnsAiInfo":
                    ConcurrentHashMap concurrentHashMap5 = new ConcurrentHashMap();
                    concurrentHashMap5.put("nrRlfOptimizationSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("nrCellselectionEnhancementSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("nrBandScanEnhancementSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("nrRlfOptimizationNum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("nrCellselectionEnhancementNum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("nrBandScanEnhancementNum", Integer.valueOf(byteBuffer.getInt()));
                    arrayList.add(concurrentHashMap5);
                    return false;
                case "getLteMsg1AndMsg3SendFailInfo":
                case "getNr5gMsg1AndMsg3SendFailInfo":
                    short s3 = byteBuffer.getShort();
                    for (int i9 = 0; i9 < s3; i9++) {
                        ConcurrentHashMap concurrentHashMap6 = new ConcurrentHashMap();
                        concurrentHashMap6.put("mcc", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap6.put("mnc", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap6.put("msg1FailCnt", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap6.put("msg3FailCnt", Short.valueOf(byteBuffer.getShort()));
                        arrayList.add(concurrentHashMap6);
                    }
                    return false;
                case "getGpsOnDurationAndCountInfo":
                    if (byteBuffer.remaining() < 5) {
                        Utils.logW("QcModemHydraStat", "Not enough data for getGpsOnDurationAndCountInfo");
                        return false;
                    }
                    ConcurrentHashMap concurrentHashMap7 = new ConcurrentHashMap();
                    concurrentHashMap7.put("duration", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap7.put("count", Byte.valueOf(byteBuffer.get()));
                    arrayList.add(concurrentHashMap7);
                    return false;
                case "getRsrpAndSinrInfo":
                    int i10 = byteBuffer.getInt();
                    this.bandIdx = i10;
                    if (i10 >= 1 && i10 <= strArr.length) {
                        ConcurrentHashMap concurrentHashMap8 = new ConcurrentHashMap();
                        concurrentHashMap8.put("band", strArr[this.bandIdx - 1]);
                        this.stageIdx = i2 / strArr.length;
                        concurrentHashMap8.put("stage", "s_" + this.stageIdx);
                        for (int i11 = -20; i11 < 31; i11++) {
                            concurrentHashMap8.put("snr" + i11, Integer.valueOf(byteBuffer.getInt()));
                        }
                        arrayList.add(concurrentHashMap8);
                    }
                    return i2 < (strArr.length * 4) + (-1);
                case "getLteChannelInfo":
                    ConcurrentHashMap concurrentHashMap9 = new ConcurrentHashMap();
                    concurrentHashMap9.put("band", Short.valueOf(byteBuffer.getShort()));
                    concurrentHashMap9.put("lChannel", Short.valueOf(byteBuffer.getShort()));
                    concurrentHashMap9.put("mChannel", Short.valueOf(byteBuffer.getShort()));
                    concurrentHashMap9.put("hChannel", Short.valueOf(byteBuffer.getShort()));
                    arrayList.add(concurrentHashMap9);
                    return false;
                case "getNr5gRachFailRateInfo":
                case "getLteRachFailRateInfo":
                    short s4 = byteBuffer.getShort();
                    for (int i12 = 0; i12 < s4; i12++) {
                        ConcurrentHashMap concurrentHashMap10 = new ConcurrentHashMap();
                        concurrentHashMap10.put("mcc", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap10.put("mnc", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap10.put("rachTiggerNum", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap10.put("rachOkNum", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap10.put("rachFailNum", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap10.put("rachUnknowNum", Short.valueOf(byteBuffer.getShort()));
                        arrayList.add(concurrentHashMap10);
                    }
                    return false;
                case "getLteReselThresholdInfo":
                    short s5 = byteBuffer.getShort();
                    boolean z4 = s5 < 36;
                    for (int i13 = 0; i13 < s5; i13++) {
                        ConcurrentHashMap concurrentHashMap11 = new ConcurrentHashMap();
                        concurrentHashMap11.put("mcc", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap11.put("mnc", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap11.put("band", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap11.put("cid", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap11.put("relevmin", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap11.put("qQualmin", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap11.put("servCell", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap11.put("nonIntraSearch", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap11.put("thrServLow", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap11.put("sNonIntraSearchQ", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap11.put("thrServLowQ", Byte.valueOf(byteBuffer.get()));
                        arrayList.add(concurrentHashMap11);
                    }
                    return z4;
                case "getLteNrBandAndRsrpInfo":
                    byteBuffer.getInt();
                    int i14 = byteBuffer.getShort() & 65535;
                    this.bandIdx = i14;
                    if (i14 >= 1 && i14 <= strArr.length) {
                        String str2 = strArr[i14 - 1];
                        for (int i15 = 0; i15 < 3; i15++) {
                            ConcurrentHashMap concurrentHashMap12 = new ConcurrentHashMap();
                            if (i15 > 0) {
                                byteBuffer.getInt();
                                byteBuffer.getShort();
                            }
                            concurrentHashMap12.put("posture", Integer.valueOf(i15));
                            concurrentHashMap12.put("band", str2);
                            for (int i16 = -140; i16 < -59; i16++) {
                                concurrentHashMap12.put("rsrp" + i16, Integer.valueOf(byteBuffer.getShort() & 65535));
                            }
                            arrayList.add(concurrentHashMap12);
                        }
                    }
                    return i2 < strArr.length + (-1);
                case "get6RxCountInfo":
                    String[] strArr3 = {"N1", "N3", "N41", "N78", "N5"};
                    int i17 = b >= 1 ? 5 : 4;
                    for (int i18 = 0; i18 < i17; i18++) {
                        ConcurrentHashMap concurrentHashMap13 = new ConcurrentHashMap();
                        concurrentHashMap13.put("2rx", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap13.put("4rx", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap13.put("6rx", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap13.put("band", strArr3[i18]);
                        arrayList.add(concurrentHashMap13);
                    }
                    return false;
                case "getNrPdschNumLayersCountInfo":
                    String[] strArr4 = {"N28", "N41", "N78"};
                    for (int i19 = 0; i19 < 3; i19++) {
                        ConcurrentHashMap concurrentHashMap14 = new ConcurrentHashMap();
                        concurrentHashMap14.put("num_layers_1_and_2x2_mimo", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap14.put("num_layers_2_and_2x2_mimo", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap14.put("num_layers_4_and_2x2_mimo", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap14.put("num_layers_1_and_4x4_mimo", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap14.put("num_layers_2_and_4x4_mimo", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap14.put("num_layers_4_and_4x4_mimo", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap14.put("band", strArr4[i19]);
                        arrayList.add(concurrentHashMap14);
                    }
                    return false;
                case "getLteNrBandAndFbrxPowerInfo":
                    int i20 = byteBuffer.getInt();
                    this.bandIdx = i20;
                    if (i20 >= 1 && i20 <= strArr.length) {
                        ConcurrentHashMap concurrentHashMap15 = new ConcurrentHashMap();
                        concurrentHashMap15.put("band", strArr[this.bandIdx - 1]);
                        for (int i21 = -25; i21 < 30; i21++) {
                            concurrentHashMap15.put("tx" + i21, Integer.valueOf(byteBuffer.getInt()));
                        }
                        arrayList.add(concurrentHashMap15);
                    }
                    return i2 < strArr.length + (-1);
                case "getNrPuschTxModeCountInfo":
                    String[] strArr5 = {"N41", "N78", "N1"};
                    for (int i22 = 0; i22 < 3; i22++) {
                        ConcurrentHashMap concurrentHashMap16 = new ConcurrentHashMap();
                        concurrentHashMap16.put("SISO", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap16.put("SMSL", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap16.put("SMDL", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap16.put("band", strArr5[i22]);
                        arrayList.add(concurrentHashMap16);
                    }
                    return false;
                case "getNrChannelInfo":
                    int i23 = b == 0 ? 3 : 4;
                    for (int i24 = 0; i24 < i23; i24++) {
                        ConcurrentHashMap concurrentHashMap17 = new ConcurrentHashMap();
                        concurrentHashMap17.put("band", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap17.put("lChannel", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap17.put("mChannel", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap17.put("hChannel", Short.valueOf(byteBuffer.getShort()));
                        arrayList.add(concurrentHashMap17);
                    }
                    return false;
                case "getNrTxPowerByChannelInfo":
                    int i25 = 0;
                    int i26 = 4;
                    while (true) {
                        if (i25 < i26) {
                            byteBuffer.get();
                            int i27 = byteBuffer.getInt();
                            if (i27 >= 1 && i27 <= strArr.length) {
                                this.bandIdx = i27;
                                i3 = i25;
                                z = true;
                            } else {
                                byteBuffer.position(byteBuffer.position() + MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT);
                                i25++;
                                i26 = 4;
                            }
                        } else {
                            z = false;
                            i3 = -1;
                        }
                    }
                    if (z) {
                        byteBuffer.position(byteBuffer.position() - ((i3 * 109) + 5));
                        for (int i28 = 0; i28 < 4; i28++) {
                            ConcurrentHashMap concurrentHashMap18 = new ConcurrentHashMap();
                            concurrentHashMap18.put("channel", Integer.valueOf(byteBuffer.get() & TransitionInfo.INIT));
                            concurrentHashMap18.put("band", strArr[this.bandIdx - 1]);
                            byteBuffer.getInt();
                            for (int i29 = 1; i29 <= 26; i29++) {
                                concurrentHashMap18.put("power_stage_" + i29, Integer.valueOf(byteBuffer.getInt()));
                            }
                            arrayList.add(concurrentHashMap18);
                        }
                    }
                    return i2 < strArr.length + (-1);
                case "getLteRssiAndSigPathInfo":
                    for (int i30 = 0; i30 < 9; i30++) {
                        ConcurrentHashMap concurrentHashMap19 = new ConcurrentHashMap();
                        concurrentHashMap19.put("rssi0", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap19.put("rssi1", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap19.put("rssi2", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap19.put("rssi3", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap19.put("earfn", Integer.valueOf(byteBuffer.getInt()));
                        arrayList.add(concurrentHashMap19);
                    }
                    return false;
                case "getNrReselThresholdInfo":
                    short s6 = byteBuffer.getShort();
                    boolean z5 = s6 < 34;
                    for (int i31 = 0; i31 < s6; i31++) {
                        ConcurrentHashMap concurrentHashMap20 = new ConcurrentHashMap();
                        concurrentHashMap20.put("mcc", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap20.put("mnc", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap20.put("band", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap20.put("cid", Long.valueOf(byteBuffer.getLong()));
                        concurrentHashMap20.put("qRelevmin", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap20.put("qRelevminOffset", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap20.put("qQualmin", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap20.put("qQualminOffset", Byte.valueOf(byteBuffer.get()));
                        arrayList.add(concurrentHashMap20);
                    }
                    return z5;
                case "getLteNrBandAndSinrInfo":
                    int i32 = byteBuffer.getInt();
                    this.bandIdx = i32;
                    if (i32 >= 1 && i32 <= strArr.length) {
                        ConcurrentHashMap concurrentHashMap21 = new ConcurrentHashMap();
                        concurrentHashMap21.put("band", strArr[this.bandIdx - 1]);
                        for (int i33 = -20; i33 < 31; i33++) {
                            concurrentHashMap21.put("snr" + i33, Integer.valueOf(byteBuffer.getInt()));
                        }
                        arrayList.add(concurrentHashMap21);
                    }
                    return i2 < strArr.length + (-1);
                case "getLteAndNr5gBandwidthInfo":
                    short s7 = byteBuffer.getShort();
                    boolean z6 = s7 > 40;
                    for (int i34 = 0; i34 < s7; i34++) {
                        ConcurrentHashMap concurrentHashMap22 = new ConcurrentHashMap();
                        concurrentHashMap22.put("ts", Long.valueOf(byteBuffer.getLong()));
                        concurrentHashMap22.put("rat", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap22.put("band", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap22.put("bw", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap22.put("ulRbNum", Short.valueOf(byteBuffer.getShort()));
                        arrayList.add(concurrentHashMap22);
                    }
                    return z6;
                case "getLteNrBandAndTxPowerInfo":
                    int i35 = byteBuffer.getInt();
                    this.bandIdx = i35;
                    if (i35 >= 1 && i35 <= strArr.length) {
                        ConcurrentHashMap concurrentHashMap23 = new ConcurrentHashMap();
                        concurrentHashMap23.put("band", strArr[this.bandIdx - 1]);
                        for (int i36 = -25; i36 < 27; i36++) {
                            concurrentHashMap23.put("tx" + i36, Integer.valueOf(byteBuffer.getInt()));
                        }
                        arrayList.add(concurrentHashMap23);
                    }
                    return i2 < strArr.length + (-1);
                case "getLteReselBandAndRsrpInfo":
                case "getLtehoBandAndRsrpInfo":
                    short s8 = byteBuffer.getShort();
                    boolean z7 = s8 < 58;
                    for (int i37 = 0; i37 < s8; i37++) {
                        ConcurrentHashMap concurrentHashMap24 = new ConcurrentHashMap();
                        concurrentHashMap24.put("cid", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap24.put("band", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap24.put("rsrp", Float.valueOf(byteBuffer.getFloat()));
                        arrayList.add(concurrentHashMap24);
                    }
                    return z7;
                case "getNrRssiAndSigPathInfo":
                    for (int i38 = 0; i38 < 5; i38++) {
                        ConcurrentHashMap concurrentHashMap25 = new ConcurrentHashMap();
                        concurrentHashMap25.put("rssi0", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap25.put("rssi1", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap25.put("rssi2", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap25.put("rssi3", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap25.put("earfn", Integer.valueOf(byteBuffer.getInt()));
                        arrayList.add(concurrentHashMap25);
                    }
                    return false;
                case "getRsrpAndTxPowerInfo":
                    int i39 = byteBuffer.getInt();
                    this.bandIdx = i39;
                    if (i39 >= 1 && i39 <= strArr.length) {
                        ConcurrentHashMap concurrentHashMap26 = new ConcurrentHashMap();
                        concurrentHashMap26.put("band", strArr[this.bandIdx - 1]);
                        this.stageIdx = i2 / strArr.length;
                        concurrentHashMap26.put("stage", "s_" + this.stageIdx);
                        for (int i40 = -25; i40 < 27; i40++) {
                            concurrentHashMap26.put("tx" + i40, Integer.valueOf(byteBuffer.getInt()));
                        }
                        arrayList.add(concurrentHashMap26);
                    }
                    return i2 < (strArr.length * 4) + (-1);
                default:
                    return false;
            }
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        private boolean pullModemRecordsOnSub(int i, int i2, ArrayList arrayList) {
            Object objCallQcrilHookGetObject = QcModemHydraStat.this.mModemUtil.callQcrilHookGetObject(this.multiPollMethods[i], i2);
            if (objCallQcrilHookGetObject == null) {
                return false;
            }
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            byte b = byteBuffer.get();
            String str = this.multiPollMethods[i];
            str.hashCode();
            byte b2 = -1;
            switch (str.hashCode()) {
                case -1899800284:
                    if (str.equals("getNr5gSnsRrcMitigationInfo")) {
                        b2 = 0;
                    }
                    break;
                case -1785948113:
                    if (str.equals("getCafiSnsInfo")) {
                        b2 = 1;
                    }
                    break;
                case -1092911946:
                    if (str.equals("getNr5gSnsInfoV2")) {
                        b2 = 2;
                    }
                    break;
                case 56963226:
                    if (str.equals("getNr5gSnsInfo")) {
                        b2 = 3;
                    }
                    break;
                case 283583391:
                    if (str.equals("getLteSnsInfo")) {
                        b2 = 4;
                    }
                    break;
                case 575471732:
                    if (str.equals("getLteSnsSetDubiousCellInfo")) {
                        b2 = 5;
                    }
                    break;
                case 714683967:
                    if (str.equals("getLteSnsRrcMitigationInfo")) {
                        b2 = 6;
                    }
                    break;
                case 1130838575:
                    if (str.equals("getNr5gSnsSetDubiousCellInfo")) {
                        b2 = 7;
                    }
                    break;
                case 1280882549:
                    if (str.equals("getLteNlicEngineActiveInfo")) {
                        b2 = 8;
                    }
                    break;
                case 1633474936:
                    if (str.equals("getNr5gSnsDataStallInfo")) {
                        b2 = 9;
                    }
                    break;
                case 1809632751:
                    if (str.equals("getLteSnsDataStallInfoV2")) {
                        b2 = 10;
                    }
                    break;
                case 1892383763:
                    if (str.equals("getLteSnsDataStallInfo")) {
                        b2 = 11;
                    }
                    break;
                case 1940701819:
                    if (str.equals("getLteSnsInfoV2")) {
                        b2 = 12;
                    }
                    break;
                case 2106353172:
                    if (str.equals("getNr5gSnsDataStallInfoV2")) {
                        b2 = 13;
                    }
                    break;
            }
            Object obj = "getLteSnsSetDubiousCellInfo";
            switch (b2) {
                case 0:
                    short s = byteBuffer.getShort();
                    boolean z = s > 62;
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    concurrentHashMap.put("hstScanTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("hstScanActivation", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("hstScanOemActivation", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("preferCellRanSharingTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("preferCellSameEnbTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("preferCellQfsTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("badCellCefTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("badCellRlfTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("badCellHofTrigger", Integer.valueOf(byteBuffer.getInt()));
                    arrayList.add(concurrentHashMap);
                    for (int i3 = 0; i3 < s; i3++) {
                        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                        concurrentHashMap2.put("reestablishmentStatus", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap2.put("arfcn", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap2.put("pci", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap2.put("band", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap2.put("globalCellId", Long.valueOf(byteBuffer.getLong()));
                        arrayList.add(concurrentHashMap2);
                    }
                    return z;
                case 1:
                    ConcurrentHashMap concurrentHashMap3 = new ConcurrentHashMap();
                    if (byteBuffer.remaining() >= 4) {
                        concurrentHashMap3.put("cafi_subway_num", Integer.valueOf(byteBuffer.getInt()));
                    }
                    if (byteBuffer.remaining() >= 4) {
                        concurrentHashMap3.put("cafi_rach_enhancement_num", Integer.valueOf(byteBuffer.getInt()));
                    }
                    if (byteBuffer.remaining() >= 4) {
                        concurrentHashMap3.put("cafi_ferry_crossing_num", Integer.valueOf(byteBuffer.getInt()));
                    }
                    if (byteBuffer.remaining() >= 4) {
                        concurrentHashMap3.put("cafi_hsu_num", Integer.valueOf(byteBuffer.getInt()));
                    }
                    if (byteBuffer.remaining() >= 4) {
                        concurrentHashMap3.put("cafi_lte_garage_num", Integer.valueOf(byteBuffer.getInt()));
                    }
                    if (byteBuffer.remaining() >= 4) {
                        concurrentHashMap3.put("cafi_nr_garage_num", Integer.valueOf(byteBuffer.getInt()));
                    }
                    if (byteBuffer.remaining() >= 4) {
                        concurrentHashMap3.put("cafi_low_power_night_num", Integer.valueOf(byteBuffer.getInt()));
                    }
                    arrayList.add(concurrentHashMap3);
                    return false;
                case 2:
                    if (byteBuffer.remaining() <= 32) {
                        return false;
                    }
                    ConcurrentHashMap concurrentHashMap4 = new ConcurrentHashMap();
                    concurrentHashMap4.put("blocking_intra_rat_weak_cell", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap4.put("blocking_inter_rat_weak_cell_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap4.put("good_link_status_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap4.put("nw_policy_status_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap4.put("nw_policy_blocking_mr_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap4.put("upp_reduce_mobility_status_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap4.put("upp_blocking_pingpong_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap4.put("hdr_status_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap4.put("sub", Integer.valueOf(i2));
                    arrayList.add(concurrentHashMap4);
                    return false;
                case 3:
                    ConcurrentHashMap concurrentHashMap5 = new ConcurrentHashMap();
                    concurrentHashMap5.put("n2lNum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("n2lSum", Integer.valueOf(byteBuffer.getInt()));
                    if (b >= 1) {
                        concurrentHashMap5.put("garageNum", Integer.valueOf(byteBuffer.getInt()));
                    }
                    concurrentHashMap5.put("garageSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("weakCellSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("airportModeAccSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("airportModeTrigSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("oosAp2ModemSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("oosSlowSacnEffSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("subwayModeSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap5.put("rachEnhanceSum", Integer.valueOf(byteBuffer.getInt()));
                    if (b == 2) {
                        concurrentHashMap5.put("goodlink", Integer.valueOf(byteBuffer.getInt()));
                    }
                    concurrentHashMap5.put("sub", Integer.valueOf(i2));
                    arrayList.add(concurrentHashMap5);
                    return false;
                case 4:
                    ConcurrentHashMap concurrentHashMap6 = new ConcurrentHashMap();
                    concurrentHashMap6.put("l2nNum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap6.put("l2nSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap6.put("garageNum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap6.put("garageSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap6.put("rlfNum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap6.put("rlfSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap6.put("avoidBadHoSum", Integer.valueOf(byteBuffer.getInt()));
                    if (b == 1) {
                        concurrentHashMap6.put("goodlink", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap6.put("oemhstdb", Integer.valueOf(byteBuffer.getInt()));
                    }
                    concurrentHashMap6.put("sub", Integer.valueOf(i2));
                    arrayList.add(concurrentHashMap6);
                    return false;
                case 5:
                case 7:
                    short s2 = byteBuffer.getShort();
                    boolean z2 = s2 > 40;
                    ConcurrentHashMap concurrentHashMap7 = new ConcurrentHashMap();
                    concurrentHashMap7.put("cellNum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap7.put("cellSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap7.put("sub", Integer.valueOf(i2));
                    arrayList.add(concurrentHashMap7);
                    int i4 = 0;
                    while (i4 < s2) {
                        ConcurrentHashMap concurrentHashMap8 = new ConcurrentHashMap();
                        concurrentHashMap8.put("type", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap8.put("freq", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap8.put("pci", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap8.put("band", Short.valueOf(byteBuffer.getShort()));
                        Object obj2 = obj;
                        if (obj2.equals(this.multiPollMethods[i])) {
                            concurrentHashMap8.put("scs", Integer.valueOf(byteBuffer.getInt()));
                            concurrentHashMap8.put("rat", Integer.valueOf(byteBuffer.getInt()));
                        } else {
                            concurrentHashMap8.put("scs", Byte.valueOf(byteBuffer.get()));
                            concurrentHashMap8.put("rat", Byte.valueOf(byteBuffer.get()));
                        }
                        arrayList.add(concurrentHashMap8);
                        i4++;
                        obj = obj2;
                    }
                    return z2;
                case 6:
                    short s3 = byteBuffer.getShort();
                    boolean z3 = s3 > 62;
                    ConcurrentHashMap concurrentHashMap9 = new ConcurrentHashMap();
                    concurrentHashMap9.put("hstScanTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap9.put("hstScanActivation", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap9.put("hstScanOemActivation", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap9.put("weakCellTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap9.put("preferCellRanSharingTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap9.put("preferCellSameEnbTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap9.put("badCellCefTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap9.put("badCellRlfTrigger", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap9.put("badCellHofTrigger", Integer.valueOf(byteBuffer.getInt()));
                    arrayList.add(concurrentHashMap9);
                    for (int i5 = 0; i5 < s3; i5++) {
                        ConcurrentHashMap concurrentHashMap10 = new ConcurrentHashMap();
                        concurrentHashMap10.put("reestStatus", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap10.put("erfcn", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap10.put("pci", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap10.put("globalCellId", Long.valueOf(byteBuffer.getLong()));
                        arrayList.add(concurrentHashMap10);
                    }
                    return z3;
                case 8:
                    ConcurrentHashMap concurrentHashMap11 = new ConcurrentHashMap();
                    concurrentHashMap11.put("mcc", Short.valueOf(byteBuffer.getShort()));
                    concurrentHashMap11.put("mnc", Short.valueOf(byteBuffer.getShort()));
                    concurrentHashMap11.put("b3N78Cnt", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap11.put("eng0", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap11.put("eng1", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap11.put("eng2", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap11.put("eng3", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap11.put("sub", Integer.valueOf(i2));
                    arrayList.add(concurrentHashMap11);
                    return false;
                case 9:
                    short s4 = byteBuffer.getShort();
                    boolean z4 = s4 > 62;
                    ConcurrentHashMap concurrentHashMap12 = new ConcurrentHashMap();
                    concurrentHashMap12.put("dataStallNum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap12.put("false2True", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap12.put("true2True", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap12.put("sub", Integer.valueOf(i2));
                    arrayList.add(concurrentHashMap12);
                    for (int i6 = 0; i6 < s4; i6++) {
                        ConcurrentHashMap concurrentHashMap13 = new ConcurrentHashMap();
                        concurrentHashMap13.put("type", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap13.put("freq", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap13.put("pci", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap13.put("band", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap13.put("scs", Byte.valueOf(byteBuffer.get()));
                        arrayList.add(concurrentHashMap13);
                    }
                    return z4;
                case 10:
                    short s5 = byteBuffer.getShort();
                    boolean z5 = s5 > 62;
                    ConcurrentHashMap concurrentHashMap14 = new ConcurrentHashMap();
                    concurrentHashMap14.put("ulFalse2True", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap14.put("dlFalse2True", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap14.put("mobilityFalse2True", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap14.put("sub", Integer.valueOf(i2));
                    arrayList.add(concurrentHashMap14);
                    for (int i7 = 0; i7 < s5; i7++) {
                        ConcurrentHashMap concurrentHashMap15 = new ConcurrentHashMap();
                        concurrentHashMap15.put("type", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap15.put("freq", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap15.put("pci", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap15.put("band", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap15.put("scs", Byte.valueOf(byteBuffer.get()));
                        arrayList.add(concurrentHashMap15);
                    }
                    return z5;
                case 11:
                    short s6 = byteBuffer.getShort();
                    boolean z6 = s6 > 136;
                    ConcurrentHashMap concurrentHashMap16 = new ConcurrentHashMap();
                    concurrentHashMap16.put("dataStallNum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap16.put("kick2KickSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap16.put("na2KickSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap16.put("sub", Integer.valueOf(i2));
                    arrayList.add(concurrentHashMap16);
                    for (int i8 = 0; i8 < s6; i8++) {
                        ConcurrentHashMap concurrentHashMap17 = new ConcurrentHashMap();
                        concurrentHashMap17.put("type", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap17.put("freq", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap17.put("pci", Short.valueOf(byteBuffer.getShort()));
                        arrayList.add(concurrentHashMap17);
                    }
                    return z6;
                case 12:
                    if (byteBuffer.remaining() <= 40) {
                        return false;
                    }
                    ConcurrentHashMap concurrentHashMap18 = new ConcurrentHashMap();
                    concurrentHashMap18.put("l2nNum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap18.put("l2nSum", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap18.put("blocking_intra_rat_weak_cell", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap18.put("blocking_inter_rat_weak_cell_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap18.put("good_link_status_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap18.put("nw_policy_status_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap18.put("nw_policy_blocking_mr_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap18.put("upp_reduce_mobility_status_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap18.put("upp_blocking_pingpong_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap18.put("hdr_status_num", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap18.put("sub", Integer.valueOf(i2));
                    arrayList.add(concurrentHashMap18);
                    return false;
                case 13:
                    short s7 = byteBuffer.getShort();
                    boolean z7 = s7 > 62;
                    ConcurrentHashMap concurrentHashMap19 = new ConcurrentHashMap();
                    concurrentHashMap19.put("ulFalse2True", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap19.put("dlFalse2True", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap19.put("mobilityFalse2True", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap19.put("sub", Integer.valueOf(i2));
                    arrayList.add(concurrentHashMap19);
                    for (int i9 = 0; i9 < s7; i9++) {
                        ConcurrentHashMap concurrentHashMap20 = new ConcurrentHashMap();
                        concurrentHashMap20.put("type", Byte.valueOf(byteBuffer.get()));
                        concurrentHashMap20.put("freq", Integer.valueOf(byteBuffer.getInt()));
                        concurrentHashMap20.put("pci", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap20.put("band", Short.valueOf(byteBuffer.getShort()));
                        concurrentHashMap20.put("scs", Byte.valueOf(byteBuffer.get()));
                        arrayList.add(concurrentHashMap20);
                    }
                    return z7;
                default:
                    return false;
            }
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void modemHydraProcess(int i, Object obj) {
        this.mStatHandler.obtainMessage(i, obj).sendToTarget();
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void modemHydraSendDelayMessage(int i, Object obj, int i2) {
        this.mStatHandler.sendMessageDelayed(this.mStatHandler.obtainMessage(i, obj), i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getNrSignalInfo(Map map, Phone phone) {
        CellSignalStrengthNr nrSignalStrength;
        SignalStrength signalStrength = phone.getSignalStrength();
        if (signalStrength == null || (nrSignalStrength = MiuiPhoneUtils.getNrSignalStrength(signalStrength)) == null) {
            return;
        }
        int ssRsrp = nrSignalStrength.getSsRsrp();
        int ssSinr = nrSignalStrength.getSsSinr();
        map.put("rsrp", Integer.valueOf(ssRsrp));
        map.put("snr", Integer.valueOf(ssSinr));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getLteSignalInfo(Map map, Phone phone) {
        SignalStrength signalStrength = phone.getSignalStrength();
        if (signalStrength != null) {
            int lteRsrp = signalStrength.getLteRsrp();
            int lteRssnr = signalStrength.getLteRssnr();
            map.put("rsrp", Integer.valueOf(lteRsrp));
            map.put("snr", Integer.valueOf(lteRssnr));
        }
    }

    abstract class ModemRadioInfo {
        int band;
        int orientation;
        int phoneId;
        float rsrpGap;
        float rsrpRx0;
        float rsrpRx1;
        float rsrpRx2;
        float rsrpRx3;
        float snrRx0;
        float snrRx1;
        float snrRx2;
        float snrRx3;
        int txpower;

        protected abstract void addToRadioSceneMap(String str);

        ModemRadioInfo() {
        }

        void checkAndUpdate(Map map, String str, float f) {
            if (f < 65500.0f) {
                map.put(str, QcModemHydraStat.this.mFloatDecimalFormat.format(f));
            }
        }

        float max(float f, float f2, float f3, float f4) {
            if (f >= 65500.0f) {
                f = -65500.0f;
            }
            if (f2 < 65500.0f) {
                f = Math.max(f, f2);
            }
            if (f3 < 65500.0f) {
                f = Math.max(f, f3);
            }
            return f4 < 65500.0f ? Math.max(f, f4) : f;
        }

        float min(float f, float f2, float f3, float f4) {
            if (f >= 65500.0f) {
                f = 65500.0f;
            }
            if (f2 < 65500.0f) {
                f = Math.min(f, f2);
            }
            if (f3 < 65500.0f) {
                f = Math.min(f, f3);
            }
            return f4 < 65500.0f ? Math.min(f, f4) : f;
        }
    }

    class ModemLteSignalInfo extends ModemRadioInfo {
        float filterRsrp;
        float rssiGap;
        int rxCombo;
        float snr;

        ModemLteSignalInfo(int i) {
            super();
            this.phoneId = i;
            Object objCallQcrilHookGetObject = QcModemHydraStat.this.mModemUtil.callQcrilHookGetObject("getLteMeasInfo", i);
            if (objCallQcrilHookGetObject != null) {
                try {
                    ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
                    byteBuffer.position(1);
                    int i2 = byteBuffer.getInt();
                    this.rxCombo = i2;
                    if (i2 > 0) {
                        this.rsrpRx0 = byteBuffer.getFloat();
                        this.rsrpRx1 = byteBuffer.getFloat();
                        this.rsrpRx2 = byteBuffer.getFloat();
                        this.rsrpRx3 = byteBuffer.getFloat();
                        float fMax = -65500.0f;
                        float fMin = 65500.0f;
                        for (int i3 = 0; i3 < 4; i3++) {
                            float f = byteBuffer.getFloat();
                            if (f <= 65500.0f) {
                                fMax = Math.max(fMax, f);
                                fMin = Math.min(fMin, f);
                            }
                        }
                        byteBuffer.getFloat();
                        this.snrRx0 = byteBuffer.getFloat();
                        this.snrRx1 = byteBuffer.getFloat();
                        this.snrRx2 = byteBuffer.getFloat();
                        this.snrRx3 = byteBuffer.getFloat();
                        this.filterRsrp = byteBuffer.getFloat();
                        this.rsrpGap = max(this.rsrpRx0, this.rsrpRx1, this.rsrpRx2, this.rsrpRx3) - min(this.rsrpRx0, this.rsrpRx1, this.rsrpRx2, this.rsrpRx3);
                        this.rssiGap = fMax - fMin;
                        this.snr = max(this.snrRx0, this.snrRx1, this.snrRx2, this.snrRx3);
                    }
                } catch (Exception e) {
                    Utils.logW("QcModemHydraStat", "getLteMeasInfo ex:" + e.getMessage());
                }
                this.txpower = QcModemHydraStat.this.mModemUtil.callQcrilHookGet("getLtePuschTx", i);
            }
        }

        @Override // com.android.phone.statistics.utils.QcModemHydraStat.ModemRadioInfo
        protected void addToRadioSceneMap(String str) {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("rxcombo", Integer.valueOf(this.rxCombo));
            checkAndUpdate(concurrentHashMap, "rsrp0", this.rsrpRx0);
            checkAndUpdate(concurrentHashMap, "rsrp1", this.rsrpRx1);
            checkAndUpdate(concurrentHashMap, "rsrp2", this.rsrpRx2);
            checkAndUpdate(concurrentHashMap, "rsrp3", this.rsrpRx3);
            checkAndUpdate(concurrentHashMap, "snr0", this.snrRx0);
            checkAndUpdate(concurrentHashMap, "snr1", this.snrRx1);
            checkAndUpdate(concurrentHashMap, "snr2", this.snrRx2);
            checkAndUpdate(concurrentHashMap, "snr3", this.snrRx3);
            checkAndUpdate(concurrentHashMap, "rsrp", this.filterRsrp);
            checkAndUpdate(concurrentHashMap, "rsrpGap", this.rsrpGap);
            checkAndUpdate(concurrentHashMap, "rssiGap", this.rssiGap);
            int i = this.txpower;
            if (i != 65535) {
                concurrentHashMap.put("tx", Integer.valueOf(i));
            }
            concurrentHashMap.put("Band", Integer.valueOf(this.band));
            if ("Volte".equals(str) && QcModemHydraStat.this.isPhoneInVideoCall(this.phoneId)) {
                str = "Vilte";
            }
            concurrentHashMap.put("extra", Integer.valueOf(QcModemHydraStat.this.getDefaultDataPhoneId() == this.phoneId ? 1 : 0));
            String str2 = str + "TxRx";
            Utils.logD("QcModemHydraStat", str2 + " add:" + concurrentHashMap);
            if (QcModemHydraStat.this.mModemRadioStatSceneMap.get(str2) == null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(concurrentHashMap);
                QcModemHydraStat.this.mModemRadioStatSceneMap.put(str2, arrayList);
                return;
            }
            ((List) QcModemHydraStat.this.mModemRadioStatSceneMap.get(str2)).add(concurrentHashMap);
        }
    }

    class ModemNrSignalInfo extends ModemRadioInfo {
        float maxRsrp;
        float snr;

        ModemNrSignalInfo(int i) {
            super();
            Object objCallQcrilHookGetObject = QcModemHydraStat.this.mModemUtil.callQcrilHookGetObject("getNr5gMeasInfo", i);
            if (objCallQcrilHookGetObject != null) {
                try {
                    ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
                    byteBuffer.position(1);
                    this.rsrpRx0 = byteBuffer.getFloat();
                    this.rsrpRx1 = byteBuffer.getFloat();
                    this.rsrpRx2 = byteBuffer.getFloat();
                    this.rsrpRx3 = byteBuffer.getFloat();
                    byteBuffer.getFloat();
                    this.snrRx0 = byteBuffer.getFloat();
                    this.snrRx1 = byteBuffer.getFloat();
                    this.snrRx2 = byteBuffer.getFloat();
                    this.snrRx3 = byteBuffer.getFloat();
                    float fMax = max(this.rsrpRx0, this.rsrpRx1, this.rsrpRx2, this.rsrpRx3);
                    this.rsrpGap = fMax - min(this.rsrpRx0, this.rsrpRx1, this.rsrpRx2, this.rsrpRx3);
                    this.maxRsrp = fMax;
                    this.snr = max(this.snrRx0, this.snrRx1, this.snrRx2, this.snrRx3);
                } catch (Exception e) {
                    Utils.logW("QcModemHydraStat", "getNr5gMeasInfo ex:" + e.getMessage());
                }
            }
            this.txpower = QcModemHydraStat.this.mModemUtil.callQcrilHookGet("getNr5gPuschTx", i);
        }

        @Override // com.android.phone.statistics.utils.QcModemHydraStat.ModemRadioInfo
        protected void addToRadioSceneMap(String str) {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            checkAndUpdate(concurrentHashMap, "rsrp0", this.rsrpRx0);
            checkAndUpdate(concurrentHashMap, "rsrp1", this.rsrpRx1);
            checkAndUpdate(concurrentHashMap, "rsrp2", this.rsrpRx2);
            checkAndUpdate(concurrentHashMap, "rsrp3", this.rsrpRx3);
            checkAndUpdate(concurrentHashMap, "rsrpGap", this.rsrpGap);
            checkAndUpdate(concurrentHashMap, "snr0", this.snrRx0);
            checkAndUpdate(concurrentHashMap, "snr1", this.snrRx1);
            checkAndUpdate(concurrentHashMap, "snr2", this.snrRx2);
            checkAndUpdate(concurrentHashMap, "snr3", this.snrRx3);
            int i = this.txpower;
            if (i != 65535) {
                concurrentHashMap.put("tx", Integer.valueOf(i));
            }
            concurrentHashMap.put("Band", Integer.valueOf(this.band));
            if ("Volte".equals(str) && QcModemHydraStat.this.isPhoneInVideoCall(this.phoneId)) {
                str = "Vilte";
            }
            concurrentHashMap.put("extra", Integer.valueOf((QcModemHydraStat.this.getDefaultDataPhoneId() == this.phoneId ? 1 : 0) | 2));
            String str2 = str + "TxRx";
            Utils.logD("QcModemHydraStat", str2 + " add:" + concurrentHashMap);
            if (QcModemHydraStat.this.mModemRadioStatSceneMap.get(str2) == null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(concurrentHashMap);
                QcModemHydraStat.this.mModemRadioStatSceneMap.put(str2, arrayList);
                return;
            }
            ((List) QcModemHydraStat.this.mModemRadioStatSceneMap.get(str2)).add(concurrentHashMap);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCloudModemFeature() {
        boolean z;
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "TelephonyModemFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    Utils.logW("QcModemHydraStat", "TelephonyModemFeature data=" + cloudData.toString());
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2) && (z = cloudData.getBoolean("ModemMonitor", true)) != this.mModemMonitor) {
                        ModemUtils modemUtils = this.mModemUtil;
                        if (modemUtils != null) {
                            if (z) {
                                modemUtils.callQcrilHookSet("onHookPropSetSync", "persist.vendor.radio.stats_switch", "1");
                                this.mModemUtil.startModemStats();
                            } else {
                                modemUtils.callQcrilHookSet("onHookPropSetSync", "persist.vendor.radio.stats_switch", "0");
                                this.mModemUtil.stopModemStats();
                            }
                            Utils.logW("QcModemHydraStat", "change modem monitor to:" + z);
                            this.mModemMonitor = z;
                            PreferenceUtils.getInstance().setBooleanPreference("ModemMonitor", this.mModemMonitor);
                            return;
                        }
                        return;
                    }
                }
                return;
            }
            Utils.logW("QcModemHydraStat", "TelephonyModemFeature cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPhoneIn5G(int i) {
        ServiceState serviceState;
        FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
        if (fiveGServiceInfo != null) {
            return fiveGServiceInfo.is5GConnected();
        }
        Phone phone = PhoneFactory.getPhone(i);
        return (phone == null || (serviceState = phone.getServiceState()) == null || serviceState.getRilVoiceRadioTechnology() != 20) ? false : true;
    }

    private void monitorAppSceneIfNeeded() {
        final ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit != null) {
            new MiuiAppStateMonitor.AppScene(this, this.mStatHandler) { // from class: com.android.phone.statistics.utils.QcModemHydraStat.3
                private int getSceneModemParam(String str, boolean z) {
                    str.hashCode();
                    int i = 5;
                    switch (str) {
                        case "social":
                            i = 7;
                            break;
                        case "wechat":
                            i = 3;
                            break;
                        case "shopping":
                            i = 9;
                            break;
                        case "streaming":
                            break;
                        case "game":
                            i = 1;
                            break;
                        case "wifi":
                            i = 11;
                            break;
                        default:
                            Utils.logD("QcModemHydraStat", "invalid scene arg");
                            return -1;
                    }
                    return z ? i : i + 1;
                }

                @Override // com.android.phone.utils.MiuiAppStateMonitor.AppScene
                public void sceneEnter(String str) {
                    Utils.logD("QcModemHydraStat", "sceneEnter:" + str);
                    modemUtilsInit.onHookCommonMsgSync(117, 4, getSceneModemParam(str, true));
                }

                @Override // com.android.phone.utils.MiuiAppStateMonitor.AppScene
                public void sceneExit(String str) {
                    Utils.logD("QcModemHydraStat", "sceneExit:" + str);
                    modemUtilsInit.onHookCommonMsgSync(117, 4, getSceneModemParam(str, false));
                }
            };
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    protected Handler getStatHandler() {
        return this.mStatHandler;
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    protected void onModemReasonReceived(int i, Bundle bundle, ModemHydraStatBase.StallRequestContext stallRequestContext) {
        if (stallRequestContext == null) {
            return;
        }
        try {
            int i2 = stallRequestContext.stallType;
            if (i2 == 1) {
                doNotifyModemReportStallInfo(stallRequestContext, 915000008, "StreamNetworkLag", "StreamStall");
            } else if (i2 == 0) {
                doNotifyModemReportStallInfo(stallRequestContext, 915000007, "GameNetworkLag", "GameStall");
            }
        } catch (Exception e) {
            Utils.logE("QcModemHydraStat", "onModemReasonReceived exception: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doNotifyModemReportStallInfo(ModemHydraStatBase.StallRequestContext stallRequestContext, int i, String str, String str2) {
        List<String> stallDataList;
        int i2 = stallRequestContext.phoneId;
        String str3 = stallRequestContext.packageName;
        Utils.logD("QcModemHydraStat", "doNotifyModemReportStallInfo phoneId=" + i2 + " pkg=" + str3 + " eventCode=" + i);
        ArrayList arrayList = new ArrayList();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        addRsrpSnrToMap(linkedHashMap, i2);
        addStallExtraParams(linkedHashMap, i2, str3);
        boolean z = true;
        if (stallRequestContext.stallType == 1) {
            linkedHashMap.put("videoQuality", Integer.valueOf(this.mLastVideoQuality));
        }
        int[] modemReasonArray = getModemReasonArray();
        HashMap map = stallRequestContext.dataStallMap;
        if (map == null) {
            map = new HashMap();
        }
        String str4 = (String) map.get("modemStallData");
        if (str4 != null && (stallDataList = parseStallDataList(str4)) != null) {
            ArrayList arrayList2 = new ArrayList();
            for (String str5 : stallDataList) {
                if (str5 != null && str5.startsWith("{")) {
                    arrayList2.add(str5);
                }
            }
            linkedHashMap.put("isCellSwitch", Integer.valueOf(arrayList2.size() < 5 ? 1 : 0));
            arrayList.add(linkedHashMap);
            Utils.logD("QcModemHydraStat", "doNotifyModemReportStallInfo list=" + arrayList);
            int iMax = Math.max(0, arrayList2.size() - 5);
            int i3 = iMax;
            while (i3 < arrayList2.size()) {
                int i4 = i3 - iMax;
                LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                boolean z2 = z;
                linkedHashMap2.put("reasonBitMask_1", Integer.valueOf(modemReasonArray != null ? modemReasonArray[i4 * 2] : 0));
                linkedHashMap2.put("reasonBitMask_2", Integer.valueOf(modemReasonArray != null ? modemReasonArray[(i4 * 2) + 1] : 0));
                mergeStallEntry((String) arrayList2.get(i3), linkedHashMap2);
                arrayList.add(linkedHashMap2);
                i3++;
                z = z2;
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.add(linkedHashMap);
        }
        if (arrayList.size() > 10) {
            arrayList = new ArrayList(arrayList.subList(0, 10));
        }
        Map mapBuildStallDfsParams = buildStallDfsParams(linkedHashMap, arrayList, str3, stallRequestContext.stallType);
        Utils.logD("QcModemHydraStat", "doNotifyModemReportStallInfo dfsParams=" + mapBuildStallDfsParams);
        recordDfsEvent(i, 0, mapBuildStallDfsParams);
        ModemHydraStatBase.localLog(str2, arrayList.toString());
        checkAndReport(arrayList, str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r4v5, types: [int] */
    /* JADX WARN: Type inference failed for: r4v7 */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r4v9 */
    /* JADX WARN: Type inference failed for: r7v0, types: [com.android.phone.statistics.utils.QcModemHydraStat] */
    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public String processUeNoSaInfo(int i) {
        boolean[] endcPowerEnable;
        List ueNoSaInfo = UeNoFivegStats.getInstance().parseUeNoSaInfo(this.mModemUtil, i);
        if (ueNoSaInfo.isEmpty()) {
            return "";
        }
        ?? sb = new StringBuilder();
        sb.append("[");
        UeNoFivegStats.UeNoFivegInfo ueNoFivegInfo = (UeNoFivegStats.UeNoFivegInfo) ueNoSaInfo.get(0);
        sb.append(ueNoFivegInfo.getCommonInfo());
        sb.append(",cellId: ");
        sb.append(ueNoFivegInfo.cellId);
        sb.append(",tac: ");
        sb.append(ueNoFivegInfo.tac);
        sb.append(",");
        ?? r4 = 1;
        r4 = 1;
        r4 = 1;
        r4 = 1;
        for (int i2 = 1; i2 < ueNoSaInfo.size(); i2++) {
            appendInfoByCause(sb, (UeNoFivegStats.UeNoFivegInfo) ueNoSaInfo.get(i2));
        }
        int restrictDcnrStatus = FiveGManagerBase.getInstance().getRestrictDcnrStatus(i);
        boolean z = SystemProperties.getBoolean("ril.deprioritize.sa.slot" + i, false);
        IFiveGPowerModule iFiveGPowerModule = (IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class);
        if (iFiveGPowerModule != null && (endcPowerEnable = iFiveGPowerModule.getEndcPowerEnable()) != null && i >= 0 && i < endcPowerEnable.length) {
            r4 = endcPowerEnable[i];
        }
        sb.append("enable_endc: ");
        sb.append(r4);
        sb.append(",");
        sb.append("restrict_dcnr: ");
        sb.append(restrictDcnrStatus);
        sb.append(",");
        sb.append("deprioritize_nr5g: ");
        sb.append(z ? 1 : 0);
        sb.append(",");
        sb.append("]");
        return sb.toString();
    }

    private void appendInfoByCause(StringBuilder sb, UeNoFivegStats.UeNoFivegInfo ueNoFivegInfo) {
        switch (ueNoFivegInfo.flag) {
            case 0:
                appendField(sb, "Nr5gNoSaTime", ueNoFivegInfo.bpFaultTime);
                break;
            case 1:
                appendField(sb, "Nr5gRejectTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, "Nr5gRejectMsgType", Float.valueOf(ueNoFivegInfo.rejectMsgType));
                appendField(sb, "Nr5gRejectCause", Float.valueOf(ueNoFivegInfo.rejectCause));
                break;
            case 2:
            case 3:
                appendField(sb, "Nr5gTimerTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, "timerId", Float.valueOf(ueNoFivegInfo.timerId));
                break;
            case 4:
                appendField(sb, "Nr5gRedirTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, "Nr5gRedirCause", Float.valueOf(ueNoFivegInfo.timerId));
                appendField(sb, "Nr5gRedirRsrp", Float.valueOf(ueNoFivegInfo.timerId));
                break;
            case 5:
                appendField(sb, "Nr5gHoTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, "Nr5gHoCause", Float.valueOf(ueNoFivegInfo.cause));
                appendField(sb, "Nr5gHoRsrp", Float.valueOf(ueNoFivegInfo.rsrp));
                break;
            case 6:
                appendField(sb, "Nr5gReselTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, "Nr5gRelCause", Float.valueOf(ueNoFivegInfo.rat));
                appendField(sb, "Nr5gReselRsrp", Float.valueOf(ueNoFivegInfo.rsrp));
                break;
            case 7:
                appendField(sb, "Nr5gRachTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, "Nr5gRachRsrp", Float.valueOf(ueNoFivegInfo.rsrp));
                break;
            case 8:
                appendField(sb, "Nr5gS_CriteriaTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, com.ot.pubsub.a.a.L, Float.valueOf(ueNoFivegInfo.result));
                appendField(sb, "q_relevmin", Float.valueOf(ueNoFivegInfo.q_relevmin));
                appendField(sb, "f_cell_quality_rsrp", Float.valueOf(ueNoFivegInfo.f_cell_quality_rsrp));
                appendField(sb, "s_rx_level", Float.valueOf(ueNoFivegInfo.s_rx_level));
                appendField(sb, "s_qual", Float.valueOf(ueNoFivegInfo.s_qual));
                break;
            case 9:
                appendField(sb, "Nr5gCellBarredTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, "cellBarred", Float.valueOf(ueNoFivegInfo.rejectCause));
                break;
            case 10:
                appendField(sb, "Nr5gRedirectTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, "redirectFreq", Float.valueOf(ueNoFivegInfo.rejectCause));
                break;
            case 11:
                appendField(sb, "Nr5gMobilityTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, "targetRAT", Float.valueOf(ueNoFivegInfo.rejectCause));
                break;
            case 12:
                appendField(sb, "Nr5gRejectWaitTimeTime", ueNoFivegInfo.bpFaultTime);
                appendField(sb, "waitTime", Float.valueOf(ueNoFivegInfo.rejectCause));
                break;
        }
    }

    private void appendField(StringBuilder sb, String str, Object obj) {
        sb.append(str);
        sb.append(": ");
        sb.append(obj != null ? obj.toString() : "null");
        sb.append(",");
    }
}
