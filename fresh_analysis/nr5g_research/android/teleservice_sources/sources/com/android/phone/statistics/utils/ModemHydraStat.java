package com.android.phone.statistics.utils;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.util.ArrayUtils;
import com.android.phone.FiveGCloudController;
import com.android.phone.FiveGCloudUtils;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.MtkSelfStudyController$$ExternalSyntheticLambda12;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.TelephonyXiaoAiManager;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.ModemEvent;
import com.android.phone.eventhub.events.NetworkSceneEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IModemModule;
import com.android.phone.module.INetworkModule;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.utils.CountableHandler;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.ot.pubsub.a.b;
import com.ot.pubsub.b.a;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import com.xiaomi.internal.telephony.ModemSarUtils;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import com.xiaomi.settingsdk.backup.SettingsBackupConsts;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.process.ProcessManager;
import miuix.animation.internal.AnimTask;
import miuix.reflect.Reflects$$ExternalSyntheticBUOutline0;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ModemHydraStat extends ModemHydraStatBase implements LocationListener {
    private static MiuiSettings.SettingsCloudData.CloudData sCloudData;
    private boolean dataStallStarted;
    private boolean isGameBattle;
    private boolean isLagging;
    private long lastLagTime;
    private JSONObject mAftersaleInfo;
    private long mCellPingPongCounts;
    private GameBoosterGameDelayListener mGameBoosterGameDelayListener;
    private boolean mGameDelayListenerRed;
    private int mGameLagFetchCnt;
    private boolean mIsMobileDataOn;
    private boolean mIsScreenOn;
    private long mLastDataStallTime;
    private int mPeriodicFetchCnt;
    private final BroadcastReceiver mReceiver;
    private boolean mSceneEnable;
    private long mStartDataStallTime;
    private long mStartGameLogTime;
    private ModemHydraHandler mStatHandler;
    private boolean mStatsEnable;
    protected final Handler mWorkHandler;
    private static final int[] sGameDelayThresholds = {0, 100, 200, MiRilHook.HOOK_ID_START_URC_BY_AT, 400};
    private static final String[] UE_NO_SA_KEY_NAMES = {"slotId", "wifiStatus", "scrnStatus", "callStatus", "appMode", "mdTime", "vgNasSt", "vgNasCas", "vgNasTime", "nrRachSinr", "nrRachRsrp", "nrRachCnt", "nrRachRea", "nrRachCas", "nrRachTime", "ratTime", "ratRsrp", "oriRat", "destRat", "ratAction", "ratFailCas", "rrcTimerId", "rrcTimerTm", "nrrcCause", "nrrcCauseTime", "lclRelTm", "lclRelCas", "cellJudgeCas", "cellJudgeTm", "imsRegTm", "imsRat", "serRegRsttac", "imsFailRea", "n1DisRea", "n1CapSt", "n1Events", "n1DisTm", "cellTm", "cellRat", "cellSt", "epsfbSt", "epsfbTm"};
    private final ArrayList mPingPongCellStats = new ArrayList();
    private final ModemExceptionCollector mMdExceptCollector = new ModemExceptionCollector();
    private final RfCollector mRfCollector = new RfCollector();
    private final PingpongDetector[] mPingpongDetector = new PingpongDetector[MiuiPhoneUtils.PHONE_COUNT];
    private final ConcurrentHashMap mSceneDataList = new ConcurrentHashMap();
    private final String DMF_CMD_HEADER = "AT+EDMFAPP=20100,";
    private final String DMF_CMD_GAME_DELAY = "AT+EDMFAPP=20102,4,";
    private int mLastGameDelay = -1;
    private double mLatitude = 0.0d;
    private double mLongitude = 0.0d;
    private HashSet callBacks = new HashSet();
    private XiaoAiScenceListener xiaoAiScenceListener = new XiaoAiScenceListener();

    /* JADX INFO: Access modifiers changed from: private */
    public int getXiaoAiSceneMask(int i) {
        if (i == 3) {
            return 1;
        }
        if (i == 4) {
            return 2;
        }
        switch (i) {
            case 10000001:
                return 4;
            case 10000002:
                return 8;
            case 10000003:
                return 16;
            case 10000004:
                return 32;
            case 10000005:
                return 64;
            case 10000006:
                return DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY;
            case 10000007:
                return DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER;
            default:
                return 0;
        }
    }

    class ModemHydraHandler extends CountableHandler {
        ModemHydraHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.phone.utils.CountableHandler, android.os.Handler
        public void handleMessage(final Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 101:
                    String str = (String) message.obj;
                    removeMessageWithCount(101);
                    if (ModemHydraStat.this.mStatsEnable) {
                        if (ModemHydraStat.this.mPeriodicFetchCnt < 60) {
                            if (ModemHydraStat.this.fetchModemDataByScene(str, true, MiuiPhoneUtils.getTopAppName(), -2, 0)) {
                                ModemHydraStat.this.mPeriodicFetchCnt++;
                            }
                            sendMessageDelayed(obtainMessage(101, message.arg1, 0, str), 30000L);
                        } else {
                            Utils.logD("ModemHydraStat", "max poll count per round reached");
                        }
                        break;
                    }
                    break;
                case 102:
                case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                case 109:
                case 110:
                case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET /* 115 */:
                case 117:
                case 118:
                case 119:
                case 121:
                default:
                    Utils.logD("ModemHydraStat", "unexpected msg:" + message);
                    break;
                case 103:
                    ModemHydraStat.this.clearDataAfterReport();
                    break;
                case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                    ModemPowerStats.check();
                    break;
                case 106:
                    ModemHydraStat.this.updateCloudModemFeature();
                    break;
                case 107:
                    int iIntValue = ((Integer) message.obj).intValue();
                    ModemHydraStat modemHydraStat = ModemHydraStat.this;
                    int gameDelayLevel = modemHydraStat.getGameDelayLevel(modemHydraStat.mLastGameDelay, ModemHydraStat.sGameDelayThresholds);
                    int gameDelayLevel2 = ModemHydraStat.this.getGameDelayLevel(iIntValue, ModemHydraStat.sGameDelayThresholds);
                    if (gameDelayLevel != gameDelayLevel2) {
                        ModemHydraStat.this.notifyModemGameDelayChanged(gameDelayLevel2);
                    }
                    ModemHydraStat.this.mLastGameDelay = iIntValue;
                    ModemHydraStat.this.processGameStall(iIntValue);
                    break;
                case 108:
                    boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                    Utils.logD("ModemHydraStat", "Game lagging:" + zBooleanValue);
                    if (zBooleanValue && ModemHydraStat.this.mGameLagFetchCnt < 60 && ModemHydraStat.this.fetchModemDataByScene("GameLag", zBooleanValue, MiuiPhoneUtils.getTopAppName(), -2, 0)) {
                        ModemHydraStat.this.mGameLagFetchCnt++;
                        break;
                    }
                    break;
                case 111:
                    int i = message.arg1;
                    if (i != 460 && i != 461) {
                        switch (i) {
                            case 420:
                            case 421:
                                if (message.arg2 > 0) {
                                    Utils.logD("ModemHydraStat", "game lagging begin");
                                }
                        }
                    }
                    FiveGManagerBase.safeRun(new Runnable() { // from class: com.android.phone.statistics.utils.ModemHydraStat$ModemHydraHandler$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$handleMessage$0(message);
                        }
                    });
                    break;
                case 112:
                    ModemHydraStat.this.mIsMobileDataOn = ((Boolean) message.obj).booleanValue();
                    ModemPowerStats.check();
                    break;
                case ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE /* 113 */:
                    ModemHydraStat.this.recordImsCallFailInfo("ImsCallFail", ((Integer) message.obj).intValue());
                    break;
                case ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX /* 114 */:
                    ModemHydraStat.this.processGameBattle(((Boolean) message.obj).booleanValue());
                    break;
                case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET /* 116 */:
                    ModemHydraStat.this.processGameStatusChanged(((Boolean) message.obj).booleanValue());
                    break;
                case 120:
                    ModemHydraStat.this.recordOoSForAbnormalSim(((Integer) message.obj).intValue());
                    break;
                case 122:
                    ModemHydraStat.this.recordModemCallReport(((Integer) new HashMap((Map) message.obj).get("cause")).intValue());
                    break;
                case 123:
                    if (ModemHydraStat.this.mStatsEnable) {
                        HashMap map = new HashMap((Map) message.obj);
                        boolean zBooleanValue2 = ((Boolean) map.get("isDataStallApp")).booleanValue();
                        String str2 = (String) map.get("pkg");
                        int iIntValue2 = ((Integer) map.get("sourceType")).intValue();
                        int iIntValue3 = ((Integer) map.get("sceneType")).intValue();
                        long jElapsedRealtime = SystemClock.elapsedRealtime();
                        ModemHydraStat modemHydraStat2 = ModemHydraStat.this;
                        if (zBooleanValue2) {
                            modemHydraStat2.updateAftersaleCount("StallDataApp", 1);
                            if (ModemHydraStat.this.mLastDataStallTime > 0 && jElapsedRealtime - ModemHydraStat.this.mLastDataStallTime < 30000) {
                                Utils.logW("ModemHydraStat", "Stall data in 30 seconds,skip");
                            } else if (ModemHydraStat.this.dataStallStarted) {
                                Utils.logW("ModemHydraStat", "dataStallStarted,skip");
                            } else {
                                ModemHydraStat.this.mStartDataStallTime = System.currentTimeMillis() / 1000;
                                ModemHydraStat.this.fetchModemDataByScene("StallDataApp", zBooleanValue2, str2, iIntValue2, iIntValue3);
                            }
                        } else {
                            modemHydraStat2.mLastDataStallTime = jElapsedRealtime;
                            ModemHydraStat.this.dataStallStarted = false;
                        }
                        break;
                    }
                    break;
                case 124:
                    ModemHydraStat.this.notifyModemReportDropNrInfo(((Integer) message.obj).intValue());
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$0(Message message) {
            ModemHydraStat.this.recordModemDataByScene(message);
        }
    }

    private ModemHydraStat() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.statistics.utils.ModemHydraStat.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    if (networkInfo == null || networkInfo.getType() != 0) {
                        return;
                    }
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        ModemHydraStat.this.mStatHandler.obtainMessage(112, Boolean.TRUE).sendToTarget();
                        return;
                    } else {
                        if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                            ModemHydraStat.this.mStatHandler.obtainMessage(112, Boolean.FALSE).sendToTarget();
                            return;
                        }
                        return;
                    }
                }
                if ("com.android.phone.modemhydra.test".equals(action)) {
                    String stringExtra = intent.getStringExtra(a.b);
                    if ("game_stall".equals(stringExtra)) {
                        ModemHydraStat.this.handleGameStallChanged(intent.getBooleanExtra("isStall", true), intent.getExtras());
                        return;
                    } else if (stringExtra != null) {
                        ModemHydraStat.this.doNotifyModemReportStallInfo(new ModemHydraStatBase.StallRequestContext(MiuiPhoneUtils.getActiveDataPhoneId(), "com.tencent.tmgp.sgame", 0, new HashMap()), 915000007, "GameNetworkLag", "GameStall");
                        return;
                    } else {
                        if (ModemPowerStats.sPwrStatsIns != null) {
                            ModemPowerStats.sPwrStatsIns.setDebugParams(intent);
                            return;
                        }
                        return;
                    }
                }
                if ("miui.intent.action.VIDEO_INFO".equals(action)) {
                    int intExtra = intent.getIntExtra("render-quality", -1);
                    Utils.logD("ModemHydraStat", "VIDEO_INFO videoQuality=" + intExtra);
                    ModemHydraStat.this.mLastVideoQuality = intExtra;
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        this.mWorkHandler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.statistics.utils.ModemHydraStat.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 121) {
                    return;
                }
                try {
                    Bundle data = message.getData();
                    if (data == null) {
                        return;
                    }
                    int i = data.getInt("sceneType");
                    boolean z = true;
                    if (data.getInt("isEnter") != 1) {
                        z = false;
                    }
                    String string = data.getString("sceneName");
                    Utils.logI("ModemHydraStat", "handleMessage msg.what stateType = " + i + " , isEnter = " + z + " , sceneName = " + string);
                    if (!ModemHydraStat.this.callBacks.isEmpty()) {
                        Iterator it = ModemHydraStat.this.callBacks.iterator();
                        while (it.hasNext()) {
                            ((ModemHydraStatBase.SceneStateCallBack) it.next()).onSceneStateChanged(i, z, string);
                        }
                    }
                    EventHubManager.getInstance().postEvent(new ModemEvent.HydraEvent("hydra_scene_state_changed", data));
                } catch (Exception e) {
                    Reflects$$ExternalSyntheticBUOutline0.m(e);
                }
            }
        };
        this.isGameBattle = false;
        this.mGameDelayListenerRed = false;
        this.mAftersaleInfo = new JSONObject();
        HandlerThread handlerThread = new HandlerThread("ModemHydraStat", 10);
        handlerThread.start();
        this.mStatHandler = new ModemHydraHandler(handlerThread.getLooper());
        this.mContext = PhoneGlobals.getInstance();
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.statistics.utils.ModemHydraStat$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$0(baseEvent);
            }
        });
        MiuiSettings.SettingsCloudData.CloudData cloudData = sCloudData;
        boolean z = cloudData == null || cloudData.getBoolean("modem_stats", true);
        this.mStatsEnable = z;
        enableModemMonitor(z, 16);
        this.mGameBoosterGameDelayListener = new GameBoosterGameDelayListener();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.android.phone.modemhydra.test");
        intentFilter.addAction("miui.intent.action.VIDEO_INFO");
        this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
        ModemPowerStats.init();
        initRegListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            this.mStatHandler.sendEmptyMessage(106);
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    protected Handler getStatHandler() {
        return this.mStatHandler;
    }

    private static void initConfigFromCloud() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(PhoneGlobals.getInstance().getContentResolver(), "TelephonyModemFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    Utils.logW("ModemHydraStat", "TelephonyModemFeature data=" + cloudData.toString());
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2)) {
                        sCloudData = cloudData;
                        return;
                    }
                }
                return;
            }
            Utils.logW("ModemHydraStat", "TelephonyModemFeature cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized ModemHydraStatBase make() {
        if (ModemProvider.IS_QCOM) {
            return QcModemHydraStat.make();
        }
        boolean zIsModemDumpFeatureSupported = ((IModemModule) ModuleDelegate.get(IModemModule.class)).isModemDumpFeatureSupported(16);
        boolean z = true;
        boolean zIsModemDumpFeatureSupported2 = ((IModemModule) ModuleDelegate.get(IModemModule.class)).isModemDumpFeatureSupported(1);
        initConfigFromCloud();
        MiuiSettings.SettingsCloudData.CloudData cloudData = sCloudData;
        boolean z2 = cloudData == null || cloudData.getBoolean("modem_dump", true);
        if (zIsModemDumpFeatureSupported2 && z2) {
            enableModemMonitor(true, 1);
        }
        MiuiSettings.SettingsCloudData.CloudData cloudData2 = sCloudData;
        if (cloudData2 != null && !cloudData2.getBoolean("modem_stats", true)) {
            z = false;
        }
        if (zIsModemDumpFeatureSupported && z) {
            ModemHydraStatBase.mInstance = new ModemHydraStat();
            return ModemHydraStatBase.mInstance;
        }
        Utils.logW("ModemHydraStat", "stats is not supported");
        return ModemHydraStatBase.mInstance;
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    protected void initRegListener() {
        super.initRegListener();
        if (TelephonyXiaoAiManager.getInstance() == null) {
            return;
        }
        TelephonyXiaoAiManager.getInstance().registerXiaoAiListener(this.xiaoAiScenceListener);
    }

    class XiaoAiScenceListener implements TelephonyXiaoAiManager.XiaoAiListener {
        XiaoAiScenceListener() {
        }

        @Override // com.android.phone.TelephonyXiaoAiManager.XiaoAiListener
        public void onScenceStateChanged(Bundle bundle) {
            int i = bundle.getInt("StateType");
            int i2 = -1;
            String string = "default";
            if (i == 3) {
                int i3 = bundle.getInt("State");
                if (i3 == 10) {
                    ModemHydraStatBase.mXiaoAiScene |= 1;
                    Utils.logI("ModemHydraStat", "enter the scenestateType = " + i + " xiaoAiScene = " + Integer.toBinaryString(ModemHydraStatBase.mXiaoAiScene));
                    i2 = 1;
                } else if (i3 == 0 || i3 == 5) {
                    ModemHydraStatBase.mXiaoAiScene &= -2;
                    Utils.logI("ModemHydraStat", "exit the scenestateType = " + i + " xiaoAiScene = " + Integer.toBinaryString(ModemHydraStatBase.mXiaoAiScene));
                    i2 = 0;
                }
                if (ModemHydraStat.this.callBacks.isEmpty()) {
                    return;
                }
                if (ModemHydraStat.this.mWorkHandler.hasMessages(121)) {
                    if (i2 == 1) {
                        ModemHydraStat.this.mWorkHandler.removeMessages(121);
                        return;
                    } else if (i2 == 0) {
                        return;
                    }
                }
                ModemHydraStat.this.sendSceneStateChangeMessage(i, i2, "default");
                return;
            }
            if (i != 4) {
                switch (i) {
                    case 10000001:
                    case 10000002:
                    case 10000003:
                    case 10000004:
                    case 10000005:
                        try {
                            JSONArray jSONArray = new JSONArray(bundle.getString("poiList"));
                            if (jSONArray.length() > 0) {
                                string = ((JSONObject) jSONArray.get(0)).get("name").toString();
                                Utils.logD("ModemHydraStat", "name = " + string);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 10000006:
                    case 10000007:
                        break;
                    default:
                        return;
                }
                String string2 = bundle.getString("type");
                int xiaoAiSceneMask = ModemHydraStat.this.getXiaoAiSceneMask(i);
                if ("IN".equals(string2)) {
                    ModemHydraStatBase.mXiaoAiScene |= xiaoAiSceneMask;
                    Utils.logI("ModemHydraStat", "enter the scenestateType = " + i + " xiaoAiScene = " + Integer.toBinaryString(ModemHydraStatBase.mXiaoAiScene));
                    i2 = 1;
                } else if ("OUT".equals(string2)) {
                    ModemHydraStatBase.mXiaoAiScene &= ~xiaoAiSceneMask;
                    Utils.logI("ModemHydraStat", "exit the scenestateType = " + i + " xiaoAiScene = " + Integer.toBinaryString(ModemHydraStatBase.mXiaoAiScene));
                    i2 = 0;
                }
                if (ModemHydraStat.this.callBacks.isEmpty()) {
                    return;
                }
                if (ModemHydraStat.this.mWorkHandler.hasMessages(121)) {
                    if (i2 == 1) {
                        ModemHydraStat.this.mWorkHandler.removeMessages(121);
                        return;
                    } else if (i2 == 0) {
                        return;
                    }
                }
                ModemHydraStat.this.sendSceneStateChangeMessage(i, i2, string);
                return;
            }
            int i4 = bundle.getInt("State");
            if (i4 == 2) {
                ModemHydraStatBase.mXiaoAiScene |= 2;
                Utils.logI("ModemHydraStat", "enter the scenestateType = " + i + " xiaoAiScene = " + Integer.toBinaryString(ModemHydraStatBase.mXiaoAiScene));
                i2 = 1;
            } else if (i4 == 1) {
                ModemHydraStatBase.mXiaoAiScene &= -3;
                Utils.logI("ModemHydraStat", "exit the scenestateType = " + i + " xiaoAiScene = " + Integer.toBinaryString(ModemHydraStatBase.mXiaoAiScene));
                i2 = 0;
            }
            if (ModemHydraStat.this.callBacks.isEmpty()) {
                return;
            }
            if (ModemHydraStat.this.mWorkHandler.hasMessages(121)) {
                if (i2 == 1) {
                    ModemHydraStat.this.mWorkHandler.removeMessages(121);
                    return;
                } else if (i2 == 0) {
                    return;
                }
            }
            ModemHydraStat.this.sendSceneStateChangeMessage(i, i2, "default");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSceneStateChangeMessage(int i, int i2, String str) {
        Message messageObtainMessage = this.mWorkHandler.obtainMessage(121);
        Bundle bundle = new Bundle();
        bundle.putInt("sceneType", i);
        bundle.putInt("isEnter", i2);
        bundle.putString("sceneName", str);
        messageObtainMessage.setData(bundle);
        this.mWorkHandler.sendMessageDelayed(messageObtainMessage, i2 == 1 ? 0L : RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
    }

    private void registerGameBoosterGameDelayListener() {
        EventHubManager.getInstance().registerListener("network", this.mGameBoosterGameDelayListener);
    }

    private void unRegisterGameBoosterGameDelayListener() {
        EventHubManager.getInstance().unregisterListener(this.mGameBoosterGameDelayListener);
    }

    class GameBoosterGameDelayListener implements BaseEvent.IEventListener {
        private GameBoosterGameDelayListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            Bundle bundle;
            if ((baseEvent instanceof NetworkSceneEvent.GameDelayEvent) && (bundle = ((NetworkSceneEvent.GameDelayEvent) baseEvent).getBundle()) != null) {
                if ("game_delay_level_changed".equals(baseEvent.getSubType())) {
                    ModemHydraStat.this.mStatHandler.obtainMessage(107, Integer.valueOf(bundle.getInt("game_delay_level", 0))).sendToTarget();
                } else if ("game_battle_status_changed".equals(baseEvent.getSubType())) {
                    ModemHydraStat.this.mStatHandler.obtainMessage(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX, Boolean.valueOf(bundle.getBoolean("game_delay_battle", false))).sendToTarget();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGameStall(int i) {
        Utils.logD("ModemHydraStat", "processGameStall delay = " + i);
        if (this.mIsMobileDataOn) {
            if (i >= 100 && !this.isLagging) {
                this.isLagging = true;
                this.lastLagTime = SystemClock.elapsedRealtime();
                this.mStartGameLogTime = System.currentTimeMillis() / 1000;
                this.mStatHandler.obtainMessage(108, Boolean.valueOf(this.isLagging)).sendToTarget();
                return;
            }
            if (i >= 100 || !this.isLagging) {
                return;
            }
            this.isLagging = false;
            if (SystemClock.elapsedRealtime() - this.lastLagTime < 2000) {
                Utils.logD("ModemHydraStat", "lag less than 2 secs, ignore this lag");
            } else {
                this.mStatHandler.obtainMessage(108, Boolean.valueOf(this.isLagging)).sendToTarget();
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
    public void processGameBattle(boolean z) {
        Utils.logD("ModemHydraStat", "processGameBattle isBattle = " + z + " isGameBattle = " + this.isGameBattle);
        if (this.isGameBattle != z) {
            this.isGameBattle = z;
            if (z) {
                return;
            }
            notifyModemGameDelayChanged(255);
            this.mLastGameDelay = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getGameDelayLevel(int i, int[] iArr) {
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
        return i >= iArr[0] ? 0 : 255;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearDataAfterReport() {
        this.mPingPongCellStats.clear();
        if (this.mSceneDataList.size() > 0) {
            for (String str : this.mSceneDataList.keySet()) {
                if (this.mSceneDataList.get(str) != null) {
                    ((ArrayList) this.mSceneDataList.get(str)).clear();
                }
            }
        }
        clearCellPingPongCounts();
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void updateModemHydraStat(String str, boolean z, int i) {
        if (!this.mSceneEnable && !this.mStatsEnable) {
            Utils.logD("ModemHydraStat", "stats disable by cloud");
            return;
        }
        if (!MiuiPhoneUtils.isMobileDataConnected(i) || MiuiPhoneUtils.getInsertedSimCount() == 0) {
            return;
        }
        if ("Game".equals(str)) {
            this.mStatHandler.obtainMessage(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET, Boolean.valueOf(z)).sendToTarget();
        }
        Utils.logD("ModemHydraStat", z + " updateModemHydraStat " + str);
        if (this.mSceneEnable) {
            this.mSceneEnable = false;
            z = false;
        } else {
            this.mSceneEnable = z;
        }
        this.mStatHandler.removeMessageWithCount(101);
        if (z) {
            String str2 = str + "Periodic";
            this.mStatHandler.obtainMessage(101, i, 0, str2).sendToTarget();
            Utils.logD("ModemHydraStat", "start perioidc featching for:".concat(str2));
            return;
        }
        this.mPeriodicFetchCnt = 0;
        Utils.logD("ModemHydraStat", "stop periodic fetching");
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void updatePingpongCellStat(int i) {
        PingpongDetector[] pingpongDetectorArr = this.mPingpongDetector;
        if (pingpongDetectorArr[i] == null) {
            pingpongDetectorArr[i] = new PingpongDetector();
        }
        this.mPingpongDetector[i].detect(i);
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void checkAndReport(List list, String str) {
        if (list == null || list.size() <= 0) {
            return;
        }
        if (list.size() > 100) {
            list = list.subList(0, 100);
        }
        HashMap map = new HashMap();
        map.put("Params", list);
        if ("PingpongCell".equals(str)) {
            map.put("cellPingPongCounts", Long.valueOf(this.mCellPingPongCounts));
        }
        MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", str, map);
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void reportModemHydraStat() {
        if (this.mStatsEnable) {
            checkAndReport(this.mPingPongCellStats, "PingpongCell");
            if (this.mSceneDataList.size() > 0) {
                for (String str : this.mSceneDataList.keySet()) {
                    checkAndReport((List) this.mSceneDataList.get(str), str);
                }
            }
            this.mStatHandler.sendEmptyMessageDelayed(103, MirrorDesktopHelper.TIMEOUT_MILLIS);
            FiveGManagerBase.safeRun(new Runnable() { // from class: com.android.phone.statistics.utils.ModemHydraStat$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$reportModemHydraStat$1();
                }
            });
            this.mPeriodicFetchCnt = 0;
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).reportCommonDataOptStatistics();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportModemHydraStat$1() {
        enableModemMonitor(false, 16);
        this.mMdExceptCollector.pullModemRecords();
        this.mRfCollector.pullDataFromMd();
        pullUserPortrait();
        pullMeasThrInfo();
        enableModemMonitor(true, 16);
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void getModemSimAbnormalInfo(int i, ArrayList arrayList) {
        if (arrayList == null) {
            return;
        }
        Object[] objArr = new Object[1];
        if (FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EDMFAPP=20100,487", "+EDMFAPP"}, objArr)) {
            parseSimResponse(objArr[0], arrayList);
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void getDataStallInfoFromModem(int i, HashMap map) {
        ModemHydraStat modemHydraStat;
        String versionStallData;
        if (MiuiPhoneUtils.getPhone(i) == null) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
        String[] strArr = {"AT+EDMFAPP=20100,461", "+EDMFAPP"};
        Object[] objArr = new Object[1];
        try {
            if (FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"AT+EDMFAPP=20100,460", "+EDMFAPP"}, objArr)) {
                modemHydraStat = this;
                String versionStallData2 = modemHydraStat.parseVersionStallData((String[]) objArr[0], null, 460, jCurrentTimeMillis);
                if (versionStallData2 != null) {
                    map.put("modemStallData", versionStallData2);
                }
            } else {
                modemHydraStat = this;
            }
            Object[] objArr2 = new Object[1];
            if (!FiveGManagerBase.getInstance().atCmdRunSync(i, strArr, objArr2) || (versionStallData = modemHydraStat.parseVersionStallData((String[]) objArr2[0], null, 461, jCurrentTimeMillis)) == null) {
                return;
            }
            map.put("modemStallData", versionStallData);
        } catch (Exception unused) {
            Utils.logW("ModemHydraStat", "getDataStallInfoFromModem failed");
        }
    }

    private ConcurrentHashMap buildMap(String[] strArr, int i, String[] strArr2, String[] strArr3, long j, String[] strArr4) {
        String[] strArr5 = {"pdcp_dl_tput", "pdcp_dl_zero_cnt", "pdcp_dl_rx_oow_num", "pdcp_dl_rx_data_pdu_num", "pdcp_dl_miss_sdu_from_ll", "pdcp_dl_rx_data_pdu_num_reest", "pdcp_dl_rx_oow_num_reest", "pdcp_dl_miss_sdu_from_ll_reest", "sdu_discard_num", "tx_data_pdu_num", "tx_data_pdu_num_reest", "sdu_discard_num_reest", "rlc_total_delay", "rlc_transmission_delay", "rlc_retransmission_delay", "rlc_ack_delay"};
        if (i >= strArr.length || Long.parseLong(strArr[i]) < j - 5) {
            return null;
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        for (int i2 = 0; i2 < strArr2.length; i2++) {
            concurrentHashMap.put(strArr2[i2], strArr[i2 + 2]);
        }
        int length = strArr2.length + 2;
        int i3 = 1;
        int i4 = Integer.parseInt(strArr[1]);
        if (i4 >= 2) {
            for (String str : strArr3) {
                if (length < strArr.length) {
                    concurrentHashMap.put(str, strArr[length]);
                    length++;
                }
            }
        }
        if (i4 == 3) {
            for (int i5 = 0; i5 < 16; i5++) {
                if (i3 < strArr4.length) {
                    concurrentHashMap.put(strArr5[i5], strArr4[i3]);
                    i3++;
                }
            }
        }
        return concurrentHashMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyModemGameDelayChanged(int i) {
        Utils.logD("ModemHydraStat", "notifyModemGameDelayChanged:" + i);
        String[] strArr = {"AT+EDMFAPP=20102,4," + i, "+EDMFAPP"};
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            return;
        }
        FiveGManagerBase.getInstance().atCmdRunSync(activeDataPhone.getPhoneId(), strArr, null);
    }

    private void pullMeasThrInfo() {
        SparseArray sparseArray = new SparseArray();
        sparseArray.put(441, "LteMeasThr");
        sparseArray.put(442, "NrMeasThr");
        for (int i = 0; i < sparseArray.size(); i++) {
            int iKeyAt = sparseArray.keyAt(i);
            String[] strArr = {"AT+EDMFAPP=20100," + iKeyAt, "+EDMFAPP"};
            Object[] objArr = new Object[1];
            ArrayList arrayList = new ArrayList();
            int i2 = 0;
            boolean z = true;
            while (i2 < 5 && z && FiveGManagerBase.getInstance().atCmdRunSync(0, strArr, objArr)) {
                Object obj = objArr[0];
                String[] strArr2 = (String[]) obj;
                boolean z2 = strArr2 != null && strArr2.length > 10;
                parseMeasThrResponse(obj, iKeyAt, arrayList);
                i2++;
                z = z2;
            }
            Utils.logD("ModemHydraStat", ((String) sparseArray.get(iKeyAt)) + " " + arrayList);
            checkAndReport(arrayList, (String) sparseArray.get(iKeyAt));
        }
    }

    private void parseMeasThrResponse(Object obj, int i, ArrayList arrayList) {
        String[] strArr = (String[]) obj;
        if (strArr.length > 1) {
            for (int i2 = 1; i2 < strArr.length; i2++) {
                Utils.logD("ModemHydraStat", "MeasThr resp:" + strArr[i2]);
                String[] strArrSplit = strArr[i2].split(",");
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                updateCommonDataFromLine(i, concurrentHashMap, strArrSplit);
                concurrentHashMap.put("curTime", strArrSplit[8]);
                arrayList.add(concurrentHashMap);
                int i3 = 9;
                for (int i4 = 0; i4 < 12; i4++) {
                    ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                    String str = strArrSplit[i3];
                    String str2 = strArrSplit[i3 + 1];
                    int i5 = i3 + 3;
                    String str3 = strArrSplit[i3 + 2];
                    i3 += 4;
                    String str4 = strArrSplit[i5];
                    int i6 = Integer.parseInt(str.trim());
                    int i7 = Integer.parseInt(str2.trim());
                    int i8 = Integer.parseInt(str3.trim());
                    int i9 = Integer.parseInt(str4.trim());
                    if (i6 == 0 && i7 == 0 && i8 == 0 && i9 == 0) {
                        break;
                    }
                    concurrentHashMap2.put("eventType", str);
                    concurrentHashMap2.put("arfcn", str2);
                    concurrentHashMap2.put("rsrp1", str3);
                    concurrentHashMap2.put("rsrp2", str4);
                    arrayList.add(concurrentHashMap2);
                }
            }
        }
    }

    private void pullUserPortrait() {
        SparseArray sparseArray = new SparseArray();
        sparseArray.put(450, "userPortrait");
        sparseArray.put(458, "CallUserProtrait");
        for (int i = 0; i < sparseArray.size(); i++) {
            int iKeyAt = sparseArray.keyAt(i);
            String[] strArr = {"AT+EDMFAPP=20100," + iKeyAt, "+EDMFAPP"};
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                Object[] objArr = new Object[1];
                ArrayList arrayList = new ArrayList();
                if (FiveGManagerBase.getInstance().atCmdRunSync(i2, strArr, objArr)) {
                    parsePortraitResponse(objArr[0], iKeyAt, arrayList, i2);
                }
                Utils.logD("ModemHydraStat", ((String) sparseArray.get(iKeyAt)) + " " + arrayList);
                checkAndReport(arrayList, (String) sparseArray.get(iKeyAt));
            }
        }
    }

    private void parsePortraitResponse(Object obj, int i, ArrayList arrayList, int i2) {
        boolean z;
        String[] strArr = (String[]) obj;
        String simOperatorNumericForPhone = TelephonyManager.from(this.mContext).getSimOperatorNumericForPhone(i2);
        if (TextUtils.isEmpty(simOperatorNumericForPhone) || strArr == null) {
            return;
        }
        try {
            if (strArr.length > 0) {
                String str = null;
                String[] strArr2 = null;
                for (int i3 = 0; i3 < strArr.length; i3++) {
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    Utils.logD("ModemHydraStat", "UserPortrait rsrp:" + strArr[i3]);
                    String[] strArrSplit = strArr[i3].split(",");
                    int i4 = 2;
                    if (i != 450) {
                        if (i == 458) {
                            str = strArrSplit[1];
                            if (Integer.parseInt(str) >= 6) {
                                strArr2 = new String[]{"mcc", "mnc", "moVolteOk", "moVolteCnt", "mtVolteOk", "mtVolteCnt", "moVonrOk", "moVonrCnt", "mtVonrOk", "mtVonrCnt", "moVowifiOk", "moVowifiCnt", "mtVowifiOk", "mtVoeifiCnt", "moVtOk", "moVtCnt", "mtVtOk", "mtVtCnt", "moCsOk", "moCsCnt", "mtCsOk", "mtCsCnt", "moCsfbOk", "moCsfbCnt", "atmpFailVonr", "atmpFailVolte", "atmpFailVowifi", "eccOk", "eccCnt", "intlCallOk", "intlCallCnt", "amr", "amrwb", "evs", "epsfbOk", "epsfbCnt", "csfbOk", "csfbCnt", "moSmsOk", "moSmsCnt", "mtSmsOk", "mtSmsCnt", "imsRegOk", "imsRegCnt", "imsCallQualityAbnml", "imsCallConnCnt", "bearerSetCnt1", "bearerSetCnt2", "bearerSetCnt3"};
                            } else if (Integer.parseInt(str) >= 5) {
                                strArr2 = new String[]{"mcc", "mnc", "moVolteOk", "moVolteCnt", "mtVolteOk", "mtVolteCnt", "moVonrOk", "moVonrCnt", "mtVonrOk", "mtVonrCnt", "moVowifiOk", "moVowifiCnt", "mtVowifiOk", "mtVoeifiCnt", "moVtOk", "moVtCnt", "mtVtOk", "mtVtCnt", "moCsOk", "moCsCnt", "mtCsOk", "mtCsCnt", "moCsfbOk", "moCsfbCnt", "atmpFailVonr", "atmpFailVolte", "atmpFailVowifi", "eccOk", "eccCnt", "intlCallOk", "intlCallCnt", "amr", "amrwb", "evs", "epsfbOk", "epsfbCnt", "csfbOk", "csfbCnt", "moSmsOk", "moSmsCnt", "mtSmsOk", "mtSmsCnt", "imsRegOk", "imsRegCnt", "imsCallQualityAbnml", "imsCallConnCnt"};
                            } else {
                                strArr2 = new String[]{"mcc", "mnc", "moVolteOk", "moVolteCnt", "mtVolteOk", "mtVolteCnt", "moVonrOk", "moVonrCnt", "mtVonrOk", "mtVonrCnt", "moVowifiOk", "moVowifiCnt", "mtVowifiOk", "mtVoeifiCnt", "moVtOk", "moVtCnt", "mtVtOk", "mtVtCnt", "moCsOk", "moCsCnt", "mtCsOk", "mtCsCnt", "moCsfbOk", "moCsfbCnt", "atmpFailVonr", "atmpFailVolte", "atmpFailVowifi", "eccOk", "eccCnt", "intlCallOk", "intlCallCnt", "amr", "amrwb", "evs"};
                            }
                        }
                        z = false;
                    } else {
                        Utils.logD("ModemHydraStat", "UserPortrait length:" + strArrSplit.length);
                        if (strArrSplit.length > 63) {
                            str = strArrSplit[1];
                            z = true;
                        } else {
                            z = false;
                        }
                        strArr2 = new String[]{"mcc", "mnc", "gsmOosCnt", "gsmOosDur", "wcdmaOosCnt", "wcdmaOosdur", "lteOosCnt", "lteOosDur", "nrOosCnt", "nrOosDur", "lteConnCnt", "lteConnDur", "nrConnCnt", "nrConnDur", "reselOk", "reselFail", "redirOk", "redirFail", "handOk", "handFail", "lteHandFail", "lteHandOk", "nrHandFail", "nrHandOk", "gsmSrchCnt", "gsmSrchDur", "wcdmaSrchCnt", "wcdmaSrchDur", "lteSrchCnt", "lteSrchDur", "nrSrchCnt", "nrSrchdur", "lteAttCnt", "lteAttOk", "lteTauCnt", "lteTauOk", "lteAuthCnt", "lteAuthOk", "nrRegCnt", "nrRegOk", "nrAuthCnt", "nrAuthOk", "lteDataCnt", "lteDataOk", "lteImsCnt", "LteImsOk", "nrPduCnt", "nrPduOk", "moCallCnt", "moCallDur", "moCallRtp", "moCallSilent", "moCallOk", "moCallFail", "mtCallCnt", "mtCalldur", "mtCallRtp", "mtCallSilent", "mtCallOk", "mtCallFail", "csfbFail", "csfbOk", "esfbOk", "esfbFail", "volteReleaseCnt", "srvccOkCnt", "srvccFailCnt", "csPaging", "csNotify"};
                        if (z && str != null) {
                            if (Integer.parseInt(str) >= 6) {
                                strArr2 = new String[]{"mcc", "mnc", "gsmOosCnt", "gsmOosDur", "wcdmaOosCnt", "wcdmaOosdur", "lteOosCnt", "lteOosDur", "nrOosCnt", "nrOosDur", "lteConnCnt", "lteConnDur", "nrConnCnt", "nrConnDur", "reselOk", "reselFail", "redirOk", "redirFail", "handOk", "handFail", "lteHandFail", "lteHandOk", "nrHandFail", "nrHandOk", "gsmSrchCnt", "gsmSrchDur", "wcdmaSrchCnt", "wcdmaSrchDur", "lteSrchCnt", "lteSrchDur", "nrSrchCnt", "nrSrchdur", "lteAttCnt", "lteAttOk", "lteTauCnt", "lteTauOk", "lteAuthCnt", "lteAuthOk", "nrRegCnt", "nrRegOk", "nrAuthCnt", "nrAuthOk", "lteDataCnt", "lteDataOk", "lteImsCnt", "LteImsOk", "nrPduCnt", "nrPduOk", "moCallCnt", "moCallDur", "moCallRtp", "moCallSilent", "moCallOk", "moCallFail", "mtCallCnt", "mtCalldur", "mtCallRtp", "mtCallSilent", "mtCallOk", "mtCallFail", "csfbFail", "csfbOk", "esfbOk", "esfbFail", "volteReleaseCnt", "srvccOkCnt", "srvccFailCnt", "csPaging", "csNotify", "scgAddCnt", "scgFailCnt", "scgDropCnt", "lteRachSuccessCnt", "lteRachAbortCnt", "lteRachFailCnt", "nrRachSuccessCnt", "nrRachAbortCnt", "nrRachFailCnt"};
                            } else if (Integer.parseInt(str) > 4) {
                                strArr2 = new String[]{"mcc", "mnc", "gsmOosCnt", "gsmOosDur", "wcdmaOosCnt", "wcdmaOosdur", "lteOosCnt", "lteOosDur", "nrOosCnt", "nrOosDur", "lteConnCnt", "lteConnDur", "nrConnCnt", "nrConnDur", "reselOk", "reselFail", "redirOk", "redirFail", "handOk", "handFail", "lteHandFail", "lteHandOk", "nrHandFail", "nrHandOk", "gsmSrchCnt", "gsmSrchDur", "wcdmaSrchCnt", "wcdmaSrchDur", "lteSrchCnt", "lteSrchDur", "nrSrchCnt", "nrSrchdur", "lteAttCnt", "lteAttOk", "lteTauCnt", "lteTauOk", "lteAuthCnt", "lteAuthOk", "nrRegCnt", "nrRegOk", "nrAuthCnt", "nrAuthOk", "lteDataCnt", "lteDataOk", "lteImsCnt", "LteImsOk", "nrPduCnt", "nrPduOk", "moCallCnt", "moCallDur", "moCallRtp", "moCallSilent", "moCallOk", "moCallFail", "mtCallCnt", "mtCalldur", "mtCallRtp", "mtCallSilent", "mtCallOk", "mtCallFail", "csfbFail", "csfbOk", "esfbOk", "esfbFail", "volteReleaseCnt", "srvccOkCnt", "srvccFailCnt", "csPaging", "csNotify", "scgAddCnt", "scgFailCnt", "scgDropCnt"};
                            }
                        } else {
                            strArr2 = (String[]) Arrays.copyOfRange(strArr2, 2, 64);
                        }
                    }
                    if (i == 450 && !z) {
                        i4 = 1;
                    }
                    if (strArr2 == null) {
                        return;
                    }
                    if (strArr2.length > strArrSplit.length - i4) {
                        Utils.logD("ModemHydraStat", "mismatched response");
                    } else {
                        for (int i5 = 0; i5 < strArr2.length; i5++) {
                            if (!"moCallDur".equals(strArr2[i5]) && !"mtCalldur".equals(strArr2[i5])) {
                                if (i == 450 && !z) {
                                    concurrentHashMap.put(strArr2[i5], strArrSplit[i5 + 1]);
                                } else {
                                    concurrentHashMap.put(strArr2[i5], strArrSplit[i5 + 2]);
                                }
                            }
                        }
                        if (i == 458) {
                            updateAftersaleMap(458, concurrentHashMap);
                        } else if (i == 450 && !z) {
                            concurrentHashMap.put("mccMnc", simOperatorNumericForPhone);
                        }
                        concurrentHashMap.put("sub", Integer.valueOf(i2));
                        arrayList.add(concurrentHashMap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseSimResponse(Object obj, ArrayList arrayList) {
        String[] strArr = (String[]) obj;
        if (strArr.length <= 1 || arrayList == null) {
            return;
        }
        for (int i = 1; i < strArr.length; i++) {
            Utils.logD("ModemHydraStat", "SIMERROR  resp:" + strArr[i]);
            String[] strArrSplit = strArr[i].split(",");
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            String str = strArrSplit[1];
            concurrentHashMap.put("slotId", strArrSplit[2]);
            concurrentHashMap.put("appMode", strArrSplit[3]);
            concurrentHashMap.put("ts", strArrSplit[4]);
            concurrentHashMap.put(MEModuleSimAbnormalMonitor.EXTRA_EVENT_TYPE, strArrSplit[5]);
            arrayList.add(concurrentHashMap);
        }
        Utils.logD("ModemHydraStat", "simErrorInfo" + arrayList);
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
    public static void getNrSignalInfo(Map map, Phone phone) {
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
    public static void getLteSignalInfo(Map map, Phone phone) {
        SignalStrength signalStrength = phone.getSignalStrength();
        if (signalStrength != null) {
            int lteRsrp = signalStrength.getLteRsrp();
            int lteRssnr = signalStrength.getLteRssnr();
            map.put("rsrp", Integer.valueOf(lteRsrp));
            map.put("snr", Integer.valueOf(lteRssnr));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCloudModemFeature() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "TelephonyModemFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    Utils.logW("ModemHydraStat", "TelephonyModemFeature data=" + cloudData.toString());
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2)) {
                        if (cloudData.equals(sCloudData)) {
                            Utils.logW("ModemHydraStat", "no change in cloud data");
                            return;
                        }
                        boolean z = true;
                        if (cloudData.json().has("modem_stats")) {
                            boolean z2 = cloudData.getBoolean("modem_stats", true);
                            MiuiSettings.SettingsCloudData.CloudData cloudData2 = sCloudData;
                            if (cloudData2 == null || z2 != cloudData2.getBoolean("modem_stats", true)) {
                                Utils.logW("ModemHydraStat", "modem_stats changes to:" + z2);
                                enableModemMonitor(z2, 16);
                                this.mStatsEnable = z2;
                            }
                        }
                        if (cloudData.json().has("modem_dump")) {
                            boolean z3 = cloudData.getBoolean("modem_dump", true);
                            MiuiSettings.SettingsCloudData.CloudData cloudData3 = sCloudData;
                            if (cloudData3 == null || z3 != cloudData3.getBoolean("modem_dump", true)) {
                                Utils.logW("ModemHydraStat", "modem_dump changes to:" + z3);
                                enableModemMonitor(z3, 1);
                            }
                        }
                        if (cloudData.json().has("ModemLogStatus")) {
                            int i = cloudData.getInt("ModemLogStatus", 0);
                            MiuiSettings.SettingsCloudData.CloudData cloudData4 = sCloudData;
                            if (cloudData4 == null || i != cloudData4.getInt("ModemLogStatus", 0)) {
                                Utils.logW("ModemHydraStat", "ModemLogStatus changes to:" + i);
                                if (i != 1) {
                                    z = false;
                                }
                                enableDataModemLog(z);
                            }
                        }
                        sCloudData = cloudData;
                        return;
                    }
                }
                return;
            }
            Utils.logW("ModemHydraStat", "TelephonyModemFeature cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enableModemMonitor(boolean z, int i) {
        String[] strArr = {z ? "AT+EDMFAPP=20100,200" : "AT+EDMFAPP=20100,201", "+EDMFAPP"};
        String[] strArr2 = {z ? "AT+EDMFAPP=20100,400" : "AT+EDMFAPP=20100,401", "+EDMFAPP"};
        if (i == 1) {
            FiveGManagerBase.getInstance().sendAtCommand(0, strArr, (Message) null);
            return;
        }
        if (i == 16) {
            FiveGManagerBase.getInstance().sendAtCommand(0, strArr2, (Message) null);
            if (z) {
                int phoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getActiveDataSubscriptionId());
                if (SubscriptionManager.isValidPhoneId(phoneId)) {
                    FiveGManagerBase.getInstance().sendAtCommand(0, new String[]{"AT+EDMFAPP=20102,2," + phoneId, "+EDMFAPP"}, (Message) null);
                }
            }
        }
    }

    public static void enableDataModemLog(boolean z) {
        FiveGManagerBase.getInstance().sendAtCommand(0, new String[]{z ? "AT+EDMFAPP=20100,0" : "AT+EDMFAPP=20100,1", "+EDMFAPP"}, (Message) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordOoSForAbnormalSim(int i) {
        String[] strArr;
        String[] strArr2 = {"AT+EDMFAPP=20100,456", "+EDMFAPP"};
        int i2 = 1;
        Object[] objArr = new Object[1];
        long jCurrentTimeMillis = System.currentTimeMillis();
        int i3 = 0;
        int i4 = 1;
        int i5 = 0;
        while (true) {
            int i6 = 5;
            if (i5 >= 5 || i4 == 0 || !FiveGManagerBase.getInstance().atCmdRunSync(i, strArr2, objArr)) {
                return;
            }
            String[] strArr3 = (String[]) objArr[i3];
            char c = '\n';
            int i7 = (strArr3 == null || strArr3.length <= 10) ? i3 : i2;
            if (strArr3 != null && strArr3.length > 0) {
                ArrayList arrayList = new ArrayList();
                int i8 = i3;
                while (i8 < strArr3.length) {
                    int i9 = i2;
                    Utils.logD("ModemHydraStat", "OOS  resp:" + strArr3[i8]);
                    int i10 = i3;
                    String[] strArrSplit = strArr3[i8].split(",");
                    char c2 = c;
                    if (Integer.valueOf(strArrSplit[i9]).intValue() < i6) {
                        strArr = new String[68];
                        strArr[i10] = "slotId";
                        strArr[i9] = "wifiStatus";
                        strArr[2] = "scrnStatus";
                        strArr[3] = "oosTime";
                        strArr[4] = "oosMdTime";
                        strArr[i6] = "callStatus";
                        strArr[6] = "appMode";
                        strArr[7] = "lteNasStatus";
                        strArr[8] = "nrNasStatus";
                        strArr[9] = "nasTag";
                        strArr[c2] = "rachRat";
                        strArr[11] = "rachSinr";
                        strArr[12] = "rachRsrp";
                        strArr[13] = "rachCnt";
                        strArr[14] = "rachReason";
                        strArr[15] = "rachCause";
                        strArr[16] = "rachTime";
                        strArr[17] = "rachTag";
                        strArr[18] = "oriRat";
                        strArr[19] = "desRat";
                        strArr[20] = "action";
                        strArr[21] = "failCause";
                        strArr[22] = "ratRsrp";
                        strArr[23] = "ratTime";
                        strArr[24] = "ratTag";
                        strArr[25] = "rat";
                        strArr[26] = "roamStatus";
                        strArr[27] = "oosTag";
                        strArr[28] = "serTime";
                        strArr[29] = "rlfRat";
                        strArr[30] = "rlfCause";
                        strArr[31] = "rlfCasOther";
                        strArr[32] = "rlfSinr";
                        strArr[33] = "rlfRsrp";
                        strArr[34] = "rlfTime";
                        strArr[35] = "rlfTag";
                        strArr[36] = "cellStatus";
                        strArr[37] = "cellFoundTag";
                        strArr[38] = "ueDetach";
                        strArr[39] = "ueDetachTag";
                        strArr[40] = "nwDetach";
                        strArr[41] = "nwDetach_tag";
                        strArr[42] = "scenario";
                        strArr[43] = "recoveryType";
                        strArr[44] = "mdExpTag";
                        strArr[45] = "cellRat";
                        strArr[46] = "mcc";
                        strArr[47] = "mnc";
                        strArr[48] = "tac";
                        strArr[49] = "cid";
                        strArr[50] = "servTac";
                        strArr[51] = "servCid";
                        strArr[52] = "servMcc";
                        strArr[53] = "servMnc";
                        strArr[54] = "servTag";
                        strArr[55] = "servRat";
                        strArr[56] = "servRsrp";
                        strArr[57] = "servSinr";
                        strArr[58] = "rrcTimerId";
                        strArr[59] = "rrcTimerRat";
                        strArr[60] = "rrcTimerTag";
                        strArr[61] = "rfState";
                        strArr[62] = "rfStateTag";
                        strArr[63] = "simState";
                        strArr[64] = "simStateTag";
                        strArr[65] = "rrcCause";
                        strArr[66] = "rrcCauseRat";
                        strArr[67] = "rrcCauseTag";
                    } else {
                        strArr = new String[76];
                        strArr[i10] = "slotId";
                        strArr[i9] = "wifiStatus";
                        strArr[2] = "scrnStatus";
                        strArr[3] = "oosTime";
                        strArr[4] = "oosMdTime";
                        strArr[i6] = "callStatus";
                        strArr[6] = "appMode";
                        strArr[7] = "lteNasStatus";
                        strArr[8] = "nrNasStatus";
                        strArr[9] = "nasTag";
                        strArr[c2] = "rachRat";
                        strArr[11] = "rachSinr";
                        strArr[12] = "rachRsrp";
                        strArr[13] = "rachCnt";
                        strArr[14] = "rachReason";
                        strArr[15] = "rachCause";
                        strArr[16] = "rachTime";
                        strArr[17] = "rachTag";
                        strArr[18] = "oriRat";
                        strArr[19] = "desRat";
                        strArr[20] = "action";
                        strArr[21] = "failCause";
                        strArr[22] = "ratRsrp";
                        strArr[23] = "ratTime";
                        strArr[24] = "ratTag";
                        strArr[25] = "rat";
                        strArr[26] = "roamStatus";
                        strArr[27] = "oosTag";
                        strArr[28] = "serTime";
                        strArr[29] = "rlfRat";
                        strArr[30] = "rlfCause";
                        strArr[31] = "rlfCasOther";
                        strArr[32] = "rlfSinr";
                        strArr[33] = "rlfRsrp";
                        strArr[34] = "rlfTime";
                        strArr[35] = "rlfTag";
                        strArr[36] = "cellStatus";
                        strArr[37] = "cellFoundTag";
                        strArr[38] = "ueDetach";
                        strArr[39] = "ueDetachTag";
                        strArr[40] = "nwDetach";
                        strArr[41] = "nwDetach_tag";
                        strArr[42] = "scenario";
                        strArr[43] = "recoveryType";
                        strArr[44] = "mdExpTag";
                        strArr[45] = "cellRat";
                        strArr[46] = "mcc";
                        strArr[47] = "mnc";
                        strArr[48] = "tac";
                        strArr[49] = "cid";
                        strArr[50] = "servTac";
                        strArr[51] = "servCid";
                        strArr[52] = "servMcc";
                        strArr[53] = "servMnc";
                        strArr[54] = "servTag";
                        strArr[55] = "servRat";
                        strArr[56] = "servRsrp";
                        strArr[57] = "servSinr";
                        strArr[58] = "rrcTimerId";
                        strArr[59] = "rrcTimerRat";
                        strArr[60] = "rrcTimerTag";
                        strArr[61] = "rfState";
                        strArr[62] = "rfStCause";
                        strArr[63] = "rfStateTag";
                        strArr[64] = "simState";
                        strArr[65] = "simStCause";
                        strArr[66] = "simStateTag";
                        strArr[67] = "rrcCause";
                        strArr[68] = "rrcCauseRat";
                        strArr[69] = "rrcCauseTag";
                        strArr[70] = "cellJuCas";
                        strArr[71] = "cellJuRat";
                        strArr[72] = "cellJuTag";
                        strArr[73] = "lcrelCause";
                        strArr[74] = "lcrelRat";
                        strArr[75] = "lcrelTag";
                    }
                    if (strArr.length > strArrSplit.length - 2) {
                        Utils.logW("ModemHydraStat", "mismatched response");
                        return;
                    }
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    String[] strArr4 = strArr2;
                    int i11 = i10;
                    while (i11 < strArr.length) {
                        String[] strArr5 = strArrSplit;
                        linkedHashMap.put(strArr[i11], strArr5[i11 + 2]);
                        i11++;
                        strArrSplit = strArr5;
                    }
                    if (needRecordLocation()) {
                        getLocation();
                        linkedHashMap.put("latitude", String.valueOf(this.mLatitude));
                        linkedHashMap.put("longitude", String.valueOf(this.mLongitude));
                    }
                    linkedHashMap.put("apOosTime", Long.valueOf(jCurrentTimeMillis));
                    linkedHashMap.put("xiaoAiScene", Integer.toBinaryString(ModemHydraStatBase.mXiaoAiScene));
                    arrayList.add(linkedHashMap);
                    Utils.logD("ModemHydraStat", "OOS " + arrayList);
                    checkAndReport(arrayList, "Oos");
                    i8++;
                    i2 = i9;
                    i3 = i10;
                    strArr2 = strArr4;
                    c = c2;
                    i6 = 5;
                }
            }
            i5++;
            i4 = i7;
            i2 = i2;
            i3 = i3;
            strArr2 = strArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyModemReportDropNrInfo(int i) {
        String[] strArr;
        if (!SubscriptionManager.isValidPhoneId(i)) {
            return;
        }
        String[] strArr2 = {"AT+EDMFAPP=20100,457", "+EDMFAPP"};
        int i2 = 1;
        Object[] objArr = new Object[1];
        long jCurrentTimeMillis = System.currentTimeMillis();
        int i3 = 0;
        int i4 = 1;
        int i5 = 0;
        while (true) {
            int i6 = 5;
            if (i5 >= 5 || i4 == 0 || !FiveGManagerBase.getInstance().atCmdRunSync(i, strArr2, objArr)) {
                return;
            }
            String[] strArr3 = (String[]) objArr[i3];
            char c = '\n';
            int i7 = (strArr3 == null || strArr3.length <= 10) ? i3 : i2;
            if (strArr3 != null && strArr3.length > 0) {
                ArrayList arrayList = new ArrayList();
                int i8 = i3;
                while (i8 < strArr3.length) {
                    int i9 = i2;
                    Utils.logD("ModemHydraStat", "DropNr resp:" + strArr3[i8]);
                    int i10 = i3;
                    String[] strArrSplit = strArr3[i8].split(",");
                    int iIntValue = Integer.valueOf(strArrSplit[i9]).intValue();
                    char c2 = c;
                    if (iIntValue < i6) {
                        strArr = new String[31];
                        strArr[i10] = "slotId";
                        strArr[i9] = "wifiStatus";
                        strArr[2] = "scrnStatus";
                        strArr[3] = "callStatus";
                        strArr[4] = "appMode";
                        strArr[i6] = "action";
                        strArr[6] = "mrStatus";
                        strArr[7] = "rsrp";
                        strArr[8] = "dropTime";
                        strArr[9] = "actionTime";
                        strArr[c2] = "nroosFlag";
                        strArr[11] = "nrNofound";
                        strArr[12] = "nroosTime";
                        strArr[13] = "nrNofoundTime";
                        strArr[14] = "imsRegTime";
                        strArr[15] = "regRes";
                        strArr[16] = "failRea";
                        strArr[17] = "vgnasStatus";
                        strArr[18] = "vgnasTime";
                        strArr[19] = "n1DisTime";
                        strArr[20] = "n1Events";
                        strArr[21] = "n1DisRea";
                        strArr[22] = "n1CapStatus";
                        strArr[23] = "nrrcCause";
                        strArr[24] = "nrrcCauseTime";
                        strArr[25] = "servRsrp";
                        strArr[26] = "servSinr";
                        strArr[27] = "servMcc";
                        strArr[28] = "servMnc";
                        strArr[29] = "servTac";
                        strArr[30] = "servCid";
                    } else if (iIntValue == i6) {
                        strArr = new String[33];
                        strArr[i10] = "slotId";
                        strArr[i9] = "wifiStatus";
                        strArr[2] = "scrnStatus";
                        strArr[3] = "callStatus";
                        strArr[4] = "appMode";
                        strArr[i6] = "action";
                        strArr[6] = "mrStatus";
                        strArr[7] = "rsrp";
                        strArr[8] = "dropTime";
                        strArr[9] = "actionTime";
                        strArr[c2] = "nroosFlag";
                        strArr[11] = "nrNofound";
                        strArr[12] = "nroosTime";
                        strArr[13] = "nrNofoundTime";
                        strArr[14] = "imsRegTime";
                        strArr[15] = "regRes";
                        strArr[16] = "failRea";
                        strArr[17] = "vgnasStatus";
                        strArr[18] = "vgnasTime";
                        strArr[19] = "n1DisTime";
                        strArr[20] = "n1Events";
                        strArr[21] = "n1DisRea";
                        strArr[22] = "n1CapStatus";
                        strArr[23] = "nrrcCause";
                        strArr[24] = "nrrcCauseTime";
                        strArr[25] = "locrelTime";
                        strArr[26] = "locrelCause";
                        strArr[27] = "servRsrp";
                        strArr[28] = "servSinr";
                        strArr[29] = "servMcc";
                        strArr[30] = "servMnc";
                        strArr[31] = "servTac";
                        strArr[32] = "servCid";
                    } else {
                        strArr = new String[37];
                        strArr[i10] = "slotId";
                        strArr[i9] = "wifiStatus";
                        strArr[2] = "scrnStatus";
                        strArr[3] = "callStatus";
                        strArr[4] = "appMode";
                        strArr[i6] = "action";
                        strArr[6] = "mrStatus";
                        strArr[7] = "rsrp";
                        strArr[8] = "dropTime";
                        strArr[9] = "actionTime";
                        strArr[c2] = "nroosFlag";
                        strArr[11] = "nrNofound";
                        strArr[12] = "nroosTime";
                        strArr[13] = "nrNofoundTime";
                        strArr[14] = "imsRegTime";
                        strArr[15] = "regRes";
                        strArr[16] = "failRea";
                        strArr[17] = "vgnasStatus";
                        strArr[18] = "vgnasTime";
                        strArr[19] = "n1DisTime";
                        strArr[20] = "n1Events";
                        strArr[21] = "n1DisRea";
                        strArr[22] = "n1CapStatus";
                        strArr[23] = "nrrcCause";
                        strArr[24] = "nrrcCauseTime";
                        strArr[25] = "locrelTime";
                        strArr[26] = "locrelCause";
                        strArr[27] = "epsfbSt";
                        strArr[28] = "epsfbTime";
                        strArr[29] = "cellJugeTime";
                        strArr[30] = "cellJugCau";
                        strArr[31] = "servRsrp";
                        strArr[32] = "servSinr";
                        strArr[33] = "servMcc";
                        strArr[34] = "servMnc";
                        strArr[35] = "servTac";
                        strArr[36] = "servCid";
                    }
                    if (strArr.length > strArrSplit.length - 2) {
                        Utils.logW("ModemHydraStat", "mismatched response");
                        return;
                    }
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    String[] strArr4 = strArr2;
                    int i11 = i10;
                    while (i11 < strArr.length) {
                        String[] strArr5 = strArrSplit;
                        concurrentHashMap.put(strArr[i11], strArr5[i11 + 2]);
                        i11++;
                        strArrSplit = strArr5;
                    }
                    putCommonInfo(concurrentHashMap, i);
                    if (needRecordLocation()) {
                        getLocation();
                        concurrentHashMap.put("latitude", String.valueOf(this.mLatitude));
                        concurrentHashMap.put("longitude", String.valueOf(this.mLongitude));
                    }
                    concurrentHashMap.put("xiaoAiScene", Integer.toBinaryString(ModemHydraStatBase.mXiaoAiScene));
                    concurrentHashMap.put("apDropNrTime", Long.valueOf(jCurrentTimeMillis));
                    arrayList.add(concurrentHashMap);
                    Utils.logD("ModemHydraStat", "DropNr " + arrayList);
                    checkAndReport(arrayList, "DropNr");
                    i8++;
                    i2 = i9;
                    i3 = i10;
                    strArr2 = strArr4;
                    c = c2;
                    i6 = 5;
                }
            }
            i5++;
            i4 = i7;
            i2 = i2;
            i3 = i3;
            strArr2 = strArr2;
        }
    }

    private ConcurrentHashMap putCommonInfo(ConcurrentHashMap concurrentHashMap, int i) {
        Bundle phoneStatus = MiuiPhoneUtils.getPhoneStatus(i, true);
        if (phoneStatus == null) {
            return concurrentHashMap;
        }
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
        }
        Phone phone = MiuiPhoneUtils.getPhone(i);
        concurrentHashMap.put("fivegEnable", getStringFromBundle(phoneStatus, "fivegEnable"));
        concurrentHashMap.put("saEnable", getStringFromBundle(phoneStatus, "saEnable"));
        concurrentHashMap.put("volteEnable", getStringFromBundle(phoneStatus, "volteEnable"));
        concurrentHashMap.put("prefNetMode", getStringFromBundle(phoneStatus, "prefNetMode"));
        concurrentHashMap.put("fivegOptimizeScene", getStringFromBundle(phoneStatus, "fivegOptimizeScene"));
        concurrentHashMap.put("ueNetworkScene", getStringFromBundle(phoneStatus, "ueNetworkScene"));
        concurrentHashMap.put("fivegOptimizeSceneSwitchCount", getStringFromBundle(phoneStatus, "fivegOptimizeSceneSwitchCount"));
        concurrentHashMap.put("p", getStringFromBundle(phoneStatus, "p"));
        concurrentHashMap.put("c", getStringFromBundle(phoneStatus, "c"));
        concurrentHashMap.put("imsReg", getStringFromBundle(phoneStatus, "imsReg"));
        concurrentHashMap.put("registeredCellInfo", getStringFromBundle(phoneStatus, "registeredCellInfo"));
        concurrentHashMap.put("phoneId", String.valueOf(i));
        concurrentHashMap.put("foregroundActivity", getForegroundActivity());
        String operatorNumeric = MiuiPhoneUtils.getOperatorNumeric(phone);
        if (operatorNumeric == null) {
            operatorNumeric = "NA";
        }
        concurrentHashMap.put("numeric", operatorNumeric);
        concurrentHashMap.put("simState", Integer.valueOf(miui.telephony.TelephonyManager.getDefault().getSimStateForSlot(i)));
        concurrentHashMap.put("airplaneMode", Boolean.valueOf(MiuiPhoneUtils.isAirPlaneMode()));
        concurrentHashMap.put("ScreenStatus", Boolean.valueOf(MiuiPhoneUtils.isScreenOn(this.mContext)));
        return concurrentHashMap;
    }

    private String getStringFromBundle(Bundle bundle, String str) {
        return bundle.getString(str, "NA");
    }

    private String getForegroundActivity() {
        ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
        if (foregroundInfo != null) {
            return foregroundInfo.mForegroundPackageName;
        }
        return "";
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
            Utils.logE("ModemHydraStat", "onModemReasonReceived exception: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doNotifyModemReportStallInfo(ModemHydraStatBase.StallRequestContext stallRequestContext, int i, String str, String str2) {
        List<String> stallDataList;
        int i2 = stallRequestContext.phoneId;
        String str3 = stallRequestContext.packageName;
        Utils.logD("ModemHydraStat", "doNotifyModemReportStallInfo phoneId=" + i2 + " packageName=" + str3 + " eventCode=" + i);
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
        int i3 = 0;
        if (str4 != null && (stallDataList = parseStallDataList(str4)) != null) {
            ArrayList arrayList2 = new ArrayList();
            for (String str5 : stallDataList) {
                if (str5 != null && str5.startsWith("{")) {
                    arrayList2.add(str5);
                }
            }
            linkedHashMap.put("isCellSwitch", Integer.valueOf(arrayList2.size() < 5 ? 1 : 0));
            arrayList.add(linkedHashMap);
            Utils.logD("ModemHydraStat", "doNotifyModemReportStallInfo list=" + arrayList);
            int iMax = Math.max(0, arrayList2.size() - 5);
            int i4 = iMax;
            while (i4 < arrayList2.size()) {
                int i5 = i4 - iMax;
                LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                boolean z2 = z;
                linkedHashMap2.put("reasonBitMask_1", Integer.valueOf(modemReasonArray != null ? modemReasonArray[i5 * 2] : i3));
                linkedHashMap2.put("reasonBitMask_2", Integer.valueOf(modemReasonArray != null ? modemReasonArray[(i5 * 2) + 1] : 0));
                mergeStallEntry((String) arrayList2.get(i4), linkedHashMap2);
                arrayList.add(linkedHashMap2);
                i4++;
                z = z2;
                i3 = 0;
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.add(linkedHashMap);
        }
        if (arrayList.size() > 10) {
            arrayList = new ArrayList(arrayList.subList(0, 10));
        }
        Utils.logD("ModemHydraStat", "doNotifyModemReportStallInfo list=" + arrayList);
        Map mapBuildStallDfsParams = buildStallDfsParams(linkedHashMap, arrayList, str3, stallRequestContext.stallType);
        Utils.logD("ModemHydraStat", "doNotifyModemReportStallInfo dfsParams=" + mapBuildStallDfsParams);
        recordDfsEvent(i, 0, mapBuildStallDfsParams);
        ModemHydraStatBase.localLog(str2, arrayList.toString());
        checkAndReport(arrayList, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordImsCallFailInfo(String str, int i) {
        String[] strArr = {"AT+EDMFAPP=20100,452", "+EDMFAPP"};
        int i2 = 1;
        Object[] objArr = new Object[1];
        int i3 = 0;
        int i4 = 1;
        int i5 = 0;
        while (i5 < 5 && i4 != 0 && FiveGManagerBase.getInstance().atCmdRunSync(i3, strArr, objArr)) {
            String[] strArr2 = (String[]) objArr[i3];
            int i6 = (strArr2 == null || strArr2.length <= 10) ? i3 : i2;
            if (strArr2 != null && strArr2.length > 0) {
                ArrayList arrayList = (ArrayList) this.mSceneDataList.get(str);
                if (arrayList != null && arrayList.size() >= 60) {
                    return;
                }
                int i7 = i3;
                while (i7 < strArr2.length) {
                    Utils.logD("ModemHydraStat", "ImsCallFail  resp:" + strArr2[i7]);
                    String[] strArrSplit = strArr2[i7].split(",");
                    String str2 = strArrSplit[i2];
                    String[] strArr3 = {"slot", "mcc", "mnc", "roaming", "MdCallEndTime", "callType", "callEndType", "rat", "dire", "endSip", "sipCode", "reason", "reasonFlag", "worningFlag", "rtpLossRate", "dlRtpCnt", "callDur", "endStatus"};
                    if (18 > strArrSplit.length - 2) {
                        Utils.logW("ModemHydraStat", "mismatched response");
                        return;
                    }
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    for (int i8 = i3; i8 < 18; i8++) {
                        concurrentHashMap.put(strArr3[i8], strArrSplit[i8 + 2]);
                    }
                    concurrentHashMap.put("teleCause", String.valueOf(i));
                    if (arrayList != null) {
                        arrayList.add(concurrentHashMap);
                    } else {
                        ArrayList arrayList2 = new ArrayList();
                        arrayList2.add(concurrentHashMap);
                        this.mSceneDataList.put(str, arrayList2);
                        arrayList = arrayList2;
                    }
                    i7++;
                    i2 = 1;
                    i3 = 0;
                }
                Utils.logD("ModemHydraStat", "ImsCallFail: " + arrayList);
            }
            i5++;
            i4 = i6;
            i2 = 1;
            i3 = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordModemCallReport(int i) {
        String[] strArr = {"AT+EDMFAPP=20100,485", "+EDMFAPP"};
        Object[] objArr = new Object[1];
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        int i2 = 0;
        while (i2 < 5 && z && FiveGManagerBase.getInstance().atCmdRunSync(0, strArr, objArr)) {
            String[] strArr2 = (String[]) objArr[0];
            boolean z2 = strArr2 != null && strArr2.length > 10;
            if (strArr2 != null && strArr2.length > 0) {
                for (int i3 = 0; i3 < strArr2.length; i3++) {
                    Utils.logD("ModemHydraStat", "DMFAPP resp:" + strArr2[i3]);
                    String[] strArrSplit = strArr2[i3].split(",");
                    Map versionedResponse = this.mMdExceptCollector.parseVersionedResponse(459, strArrSplit[1], strArrSplit);
                    if (versionedResponse != null && !versionedResponse.isEmpty()) {
                        versionedResponse.put("teleCause", Integer.valueOf(i));
                        if (needRecordLocation()) {
                            getLocation();
                            versionedResponse.put("latitude", String.valueOf(this.mLatitude));
                            versionedResponse.put("longitude", String.valueOf(this.mLongitude));
                        }
                        versionedResponse.put("xiaoAiScene", Integer.toBinaryString(ModemHydraStatBase.mXiaoAiScene));
                        arrayList.add(versionedResponse);
                    }
                }
            }
            Utils.logD("ModemHydraStat", "MDCallReport " + arrayList);
            checkAndReport(arrayList, "MDCallReport");
            i2++;
            z = z2;
        }
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

    private String[] getExtraMdRest(int i) {
        String[] strArr = null;
        if (MiuiPhoneUtils.getActiveDataPhone() == null) {
            return null;
        }
        if (i == 460) {
            strArr = new String[]{"AT+EDMFAPP=20100,4600", "+EDMFAPP"};
        } else if (i == 461) {
            strArr = new String[]{"AT+EDMFAPP=20100,4601", "+EDMFAPP"};
        }
        Object[] objArr = new Object[1];
        FiveGManagerBase.getInstance().atCmdRunSync(0, strArr, objArr);
        String[] strArr2 = (String[]) objArr[0];
        Utils.logD("ModemHydraStat", "getExtraMdRest:" + strArr + ", result:" + strArr2);
        return strArr2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordModemDataByScene(Message message) {
        AsyncResult asyncResult = (AsyncResult) message.obj;
        long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
        int i = message.arg1;
        boolean z = i == 460 || i == 461;
        if (asyncResult != null) {
            try {
                if (asyncResult.exception == null) {
                    String str = (String) asyncResult.userObj;
                    if (message.arg2 == 1 && z && !"GamePeriodic".equals(str) && !"WechatPeriodic".equals(str)) {
                        str = "Before" + str;
                    }
                    String str2 = str;
                    String[] strArr = (String[]) asyncResult.result;
                    int i2 = message.arg1;
                    if (z) {
                        parseVersionStallData(strArr, str2, i2, jCurrentTimeMillis);
                    } else {
                        parseCommonData(strArr, str2, i2);
                    }
                }
            } catch (Exception unused) {
                Utils.logW("ModemHydraStat", "recordModemDataByScene exception");
            }
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    protected void addCityProvinceToMap(LinkedHashMap linkedHashMap) {
        FiveGCloudController fiveGCloudController = FiveGCloudController.getInstance();
        if (fiveGCloudController != null) {
            String city = fiveGCloudController.getCity();
            String province = fiveGCloudController.getProvince();
            if (!TextUtils.isEmpty(city)) {
                linkedHashMap.put("c", city);
            }
            if (TextUtils.isEmpty(province)) {
                return;
            }
            linkedHashMap.put("p", province);
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public String getSceneType() {
        MiuiTelephonyStatistic miuiTelephonyStatistic = MiuiTelephonyStatistic.getInstance();
        if (miuiTelephonyStatistic != null) {
            return miuiTelephonyStatistic.getSceneType();
        }
        return "NA";
    }

    /* JADX WARN: Code duplicated, block: B:46:0x04e7 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:47:0x04e9  */
    /* JADX WARN: Code duplicated, block: B:48:0x04ee  */
    /* JADX WARN: Code duplicated, block: B:66:0x04f6 A[SYNTHETIC] */
    private String parseVersionStallData(String[] strArr, String str, int i, long j) {
        int i2;
        int i3;
        ConcurrentHashMap concurrentHashMapBuildMap;
        ModemHydraStat modemHydraStat = this;
        ArrayList arrayList = new ArrayList();
        if (strArr != null && strArr.length > 1) {
            if (str != null) {
                arrayList = (ArrayList) modemHydraStat.mSceneDataList.get(str);
                Utils.logD("ModemHydraStat", "parse_scene:" + str + ", list length:" + (arrayList != null ? arrayList.size() : 0));
                if (arrayList != null && arrayList.size() >= 60) {
                    return null;
                }
            }
            ArrayList arrayList2 = arrayList;
            boolean z = false;
            String[] extraMdRest = null;
            int i4 = 1;
            while (i4 < strArr.length) {
                Utils.logD("ModemHydraStat", "Data stall resp:" + strArr[i4]);
                String[] strArrSplit = strArr[i4].split(",");
                if (strArrSplit.length < 2) {
                    Utils.logD("ModemHydraStat", "Invalid lineData length: " + strArrSplit.length);
                    i2 = i4;
                } else {
                    int i5 = Integer.parseInt(strArrSplit[1]);
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    if (i5 <= 2) {
                        if (i % 2 == 0) {
                            i2 = i4;
                            concurrentHashMapBuildMap = modemHydraStat.buildMap(strArrSplit, 23, new String[]{"mcc", "mnc", "tac", "cId", "dlFreq", "freqBandInd", "pcid", "rsrp", "sinr", "ulBler", "dlBler", "ulGrant", "mcs", "rsrpGap", "dlTput", "ulTput", "L1DlTput", "L1UlTput", "phr", "inPdcpSz", "paddingSz", "curTime", "gameApState", "wechatApState", "streamingApState", "socialApState", "shoppingApState", "wifiApState", "dsda", "geminiMode", "nddsRrc", "full15SubframeCnt", "pathLoss"}, new String[]{"ddsRrc", "dnnName0", "dnnName1", "dnnName2", "dnnName3", "dnnName4", "bandwidth", "rsrq", "ssbRsrp", "ssbSnr", "trsRsrp", "trsSnr", "nbrNrRsrp", "nbrNrSinr", "nbrNrRsrq", "nbrLteRsrp", "nbrLteSinr", "nbrLteRsrq", "ulPending", "pdcpDiscardRatio", "numCa", "sccDlTput0", "sccDlTput1", "sccDlTput2", "sccDlTput3", "sccDlTput4", "sccDlTput5", "sccDltput6", "sccDlBler0", "sccDlBler1", "sccDlBler2", "sccDlBler3", "sccDlBler4", "sccDlBler5", "sccDlBler6", "sccDlBler7", "dlPadding", "dlPdcpLostRatio", "rlfCause", "rachReason", "rachResult", "nasEvent", "nasCause", "redirSuccessCnt", "redirFailCnt", "redirCellMatchCnt", "redirCellNomatchCnt", "hoSuccessCnt", "hoFailCnt", "rrcReleaseCnt", "sessionAmbrUnitDl", "sessionAmbrUnitUl", "sessionAmbrValueDl", "sessionAmbrValueUl", "avgDlLayers10", "pcmax", "targetPower"}, j, null);
                            modemHydraStat = this;
                        } else {
                            i2 = i4;
                            modemHydraStat = this;
                            concurrentHashMapBuildMap = modemHydraStat.buildMap(strArrSplit, 22, new String[]{"mcc", "mnc", "tac", "cId", "dlFreq", "freqBandInd", "pcid", "rsrp", "sinr", "ulBler", "dlBler", "ulGrant", "mcs", "rsrpGap", "dlTput", "ulTput", "L1DlTput", "L1UlTput", "inPdcpSz", "paddingSz", "curTime", "gameApState", "wechatApState", "streamingApState", "socialApState", "shoppingApState", "wifiApState", "dsda", "geminiMode", "nddsRrc", "full15SubframeCnt", "pathLoss"}, new String[]{"ddsRrc", "apnName0", "apnName1", "apnName2", "apnName3", "apnName4", "bandwidth", "rsrq", "nbrNrRsrp", "nbrNrSinr", "nbrNrRsrq", "nbrLteRsrp", "nbrLteSinr", "nbrLteRsrq", "ulPending", "pdcpDiscardRatio", "numCa", "sccDlTput0", "sccDlTput1", "sccDlTput2", "sccDlTput3", "sccDlTput4", "sccDlTput5", "sccDlTput6", "sccDlTput7", "sccDlBler0", "sccDlBler1", "sccDlBler2", "sccDlBler3", "sccDlBler4", "sccDlBler5", "sccDlBler6", "sccDlBler7", "dlPadding", "dlPdcpLostRatio", "rlfCause", "rachReason", "rachResult", "nasEvent", "nasCause", "redirSuccessCnt", "redirFailCnt", "redirCellMatchCnt", "redirCellNomatchCnt", "hoSuccessCnt", "hoFailCnt", "rrcReleaseCnt", "apnAmbrDl", "apnAmbrUl", "avgDlLayers10", "pcmax", "targetPower"}, j, null);
                        }
                        concurrentHashMap = concurrentHashMapBuildMap;
                        modemHydraStat.addCommonDataToMap(concurrentHashMap);
                        Utils.logD("ModemHydraStat", str + ":" + concurrentHashMap);
                        if (concurrentHashMap != null) {
                            if (arrayList2 != null) {
                                arrayList2.add(concurrentHashMap);
                            } else {
                                arrayList2 = new ArrayList();
                                arrayList2.add(concurrentHashMap);
                            }
                        }
                    } else {
                        i2 = i4;
                        if (i5 > 2) {
                            if (!z) {
                                extraMdRest = modemHydraStat.getExtraMdRest(i);
                                z = true;
                            }
                            String[] strArr2 = extraMdRest;
                            if (strArr2 == null || (i3 = i2 - 1) >= strArr2.length) {
                                extraMdRest = strArr2;
                            } else {
                                if (i % 2 == 0) {
                                    extraMdRest = strArr2;
                                    concurrentHashMapBuildMap = modemHydraStat.buildMap(strArrSplit, 23, new String[]{"mcc", "mnc", "tac", "cid", "arfcn", "band", "pci", "rsrp", "snr", "ulBler", "dlBler", "ulGrant", "mcs", "rsrpGap", "dlTput", "ulTput", "L1DlTput", "L1UlTput", "phr", "inPdcpSz", "paddingSz", "curTime", "dsda", "geminiMode", "nddsRrc", "full15SubframeCnt", "pathLoss"}, new String[]{"ddsRrc", "isDnn", "bandwidth", "rsrq", "trsRsrp", "trsSnr", "nbrNrRsrp", "nbrNrSinr", "nbrNrRsrq", "nbrLteRsrp", "nbrLteSinr", "nbrLteRsrq", "ulPending", "pdcpDiscardRatio", "numCa", "sccDlTput0", "sccDlTput1", "sccDlTput2", "sccDlBler0", "sccDlBler1", "sccDlBler2", "dlPadding", "dlPdcpLostRatio", "rlfCause", "rachReason", "rachResult", "nasEvent", "nasCause", "redirSuccessCnt", "redirFailCnt", "redirCellMatchCnt", "redirCellNomatchCnt", "hoSuccessCnt", "hoFailCnt", "rrcReleaseCnt", "sessionAmbrUnitDl", "sessionAmbrUnitUl", "sessionAmbrValueDl", "sessionAmbrValueUl", "avgDlLayers10", "pcmax", "targetPower", "count_high_ul_bler", "count_high_dl_bler", "total_ul_assignment_num", "total_dl_assignment_num", "mac_throughput_ul", "mac_padding_throughput_ul", "dds_rrc_state_change_cause", "resel_success_cnt", "resel_fail_cnt", "irat_handover_success_cnt", "irat_handover_fail_cnt", "irat_reselection_success_cnt", "irat_reselection_fail_cnt", "irat_redirection_success_cnt", "irat_redirection_fail_cnt", "is_oos", "nr_tdd_ul_symbol_count", "mpr", "ampr", "ddpc_report", "current_target_power", "tx_idx_0", "subway_cell_type", "configure_power"}, j, strArr2[i3].split(","));
                                    modemHydraStat = this;
                                } else {
                                    extraMdRest = strArr2;
                                    modemHydraStat = this;
                                    concurrentHashMapBuildMap = modemHydraStat.buildMap(strArrSplit, 22, new String[]{"mcc", "mnc", "tac", "cid", "arfcn", "band", "pci", "rsrp", "snr", "ulBler", "dlBler", "ulGrant", "mcs", "rsrpGap", "dlTput", "ulTput", "L1DlTput", "L1UlTput", "inPdcpSz", "paddingSz", "curTime", "dsda", "geminiMode", "nddsRrc", "full15SubframeCnt", "pathLoss"}, new String[]{"ddsRrc", "isApn", "bandwidth", "rsrq", "nbrNrRsrp", "nbrNrSinr", "nbrNrRsrq", "nbrLteRsrp", "nbrLteSinr", "nbrLteRsrq", "ulPending", "pdcpDiscardRatio", "numCa", "sccDlTput0", "sccDlTput1", "sccDlTput2", "sccDlBler0", "sccDlBler1", "sccDlBler2", "dlPadding", "dlPdcpLostRatio", "rlfCause", "rachReason", "rachResult", "nasEvent", "nasCause", "redirSuccessCnt", "redirFailCnt", "redirCellMatchCnt", "redirCellNomatchCnt", "hoSuccessCnt", "hoFailCnt", "rrcReleaseCnt", "apnAmbrDl", "apnAmbrUl", "avgDlLayers10", "pcmax", "targetPower", "mac_throughput_ul", "mac_padding_throughput_ul", "dds_rrc_state_change_cause", "lte_ul_slot_count", "resel_success_cnt", "resel_fail_cnt", "irat_handover_success_cnt", "irat_handover_fail_cnt", "irat_reselection_success_cnt", "irat_reselection_fail_cnt", "irat_redirection_success_cnt", "irat_redirection_fail_cnt", "is_oos", "mpr", "ampr", "pd_pwr", "target_pwr", "tx_antenna", "subway_cell_type", "configure_power"}, j, extraMdRest[i3].split(","));
                                }
                                concurrentHashMap = concurrentHashMapBuildMap;
                                modemHydraStat.addCommonDataToMap(concurrentHashMap);
                                Utils.logD("ModemHydraStat", str + ":" + concurrentHashMap);
                                if (concurrentHashMap != null) {
                                    if (arrayList2 != null) {
                                        arrayList2.add(concurrentHashMap);
                                    } else {
                                        arrayList2 = new ArrayList();
                                        arrayList2.add(concurrentHashMap);
                                    }
                                }
                            }
                        } else {
                            modemHydraStat.addCommonDataToMap(concurrentHashMap);
                            Utils.logD("ModemHydraStat", str + ":" + concurrentHashMap);
                            if (concurrentHashMap != null) {
                                if (arrayList2 != null) {
                                    arrayList2.add(concurrentHashMap);
                                } else {
                                    arrayList2 = new ArrayList();
                                    arrayList2.add(concurrentHashMap);
                                }
                            }
                        }
                    }
                }
                i4 = i2 + 1;
            }
            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.put("NA", "NA");
            if (arrayList2 != null && str != null) {
                arrayList2.add(concurrentHashMap2);
                modemHydraStat.mSceneDataList.put(str, arrayList2);
            }
            arrayList = arrayList2;
        }
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        return String.valueOf(arrayList);
    }

    private void parseCommonData(String[] strArr, String str, int i) {
        String[] strArr2;
        int i2;
        if (strArr == null || strArr.length <= 1) {
            return;
        }
        ArrayList arrayList = (ArrayList) this.mSceneDataList.get(str);
        if (arrayList == null || arrayList.size() < 60) {
            for (int i3 = 1; i3 < strArr.length; i3++) {
                String[] strArrSplit = strArr[i3].split(",");
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                int i4 = 8;
                if (i % 2 == 0) {
                    strArr2 = new String[]{"rsrp", "snr", "ulBler", "dlBler", "ulGrant", "mcs", "rsrpGap", "dlTput", "ulTput", "L1DlTput", "L1UlTput", "phr", "inPdcpSz", "paddingSz", "curTime"};
                } else {
                    strArr2 = new String[]{"rsrp", "snr", "ulBler", "dlBler", "ulGrant", "mcs", "rsrpGap", "dlTput", "ulTput", "L1DlTput", "L1UlTput", "inPdcpSz", "paddingSz", "curTime"};
                }
                updateCommonDataFromLine(i, concurrentHashMap, strArrSplit);
                int length = strArr2.length;
                int i5 = 0;
                while (true) {
                    i2 = i4;
                    if (i5 >= length) {
                        break;
                    }
                    i4 = i2 + 1;
                    concurrentHashMap.put(strArr2[i5], strArrSplit[i2]);
                    i5++;
                }
                extractApState(concurrentHashMap, strArrSplit, i2);
                addCommonDataToMap(concurrentHashMap);
                Utils.logD("ModemHydraStat", str + ":" + concurrentHashMap);
                if (arrayList != null) {
                    arrayList.add(concurrentHashMap);
                } else {
                    arrayList = new ArrayList();
                    arrayList.add(concurrentHashMap);
                    this.mSceneDataList.put(str, arrayList);
                }
            }
        }
    }

    private void addCommonDataToMap(ConcurrentHashMap concurrentHashMap) {
        if (concurrentHashMap == null) {
            return;
        }
        concurrentHashMap.put("xiaoAiScene", Integer.toBinaryString(ModemHydraStatBase.mXiaoAiScene));
        concurrentHashMap.put("residentCell", ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isCurrentCellResident() ? "1" : "0");
        if (needRecordLocation()) {
            getLocation();
            concurrentHashMap.put("latitude", String.valueOf(this.mLatitude));
            concurrentHashMap.put("longitude", String.valueOf(this.mLongitude));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCommonDataFromLine(int i, Map map, String[] strArr) {
        String str;
        if (strArr[2].length() > 1) {
            str = strArr[2];
        } else {
            str = "0" + strArr[2];
        }
        String str2 = strArr[4];
        map.put("arfcn", strArr[5]);
        map.put("band", strArr[6]);
        if ((i == 426 || i == 427 || i == 441 || i == 442) && Build.IS_INTERNATIONAL_BUILD) {
            map.put("GCI", strArr[1] + str + "-" + processCellId(str2));
            return;
        }
        map.put("GCI", strArr[1] + str + "-" + str2);
        map.put("tac", strArr[3]);
        map.put("pci", strArr[7]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVersionedCommonData(Map map, String[] strArr) {
        String str;
        if (strArr[3].length() > 1) {
            str = strArr[3];
        } else {
            str = "0" + strArr[3];
        }
        String str2 = strArr[5];
        map.put("arfcn", strArr[6]);
        map.put("band", strArr[7]);
        if (Build.IS_INTERNATIONAL_BUILD) {
            map.put("GCI", strArr[2] + str + "-" + processCellId(str2));
            return;
        }
        map.put("GCI", strArr[2] + str + "-" + str2);
        map.put("tac", strArr[4]);
        map.put("pci", strArr[8]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String processCellId(String str) {
        return (str == null || str.length() <= 5) ? str : str.substring(str.length() - 5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void extractApState(Map map, String[] strArr, int i) {
        map.put("gameApState", strArr[i]);
        map.put("wechatApState", strArr[i + 1]);
        map.put("streamingApState", strArr[i + 2]);
        map.put("socialApState", strArr[i + 3]);
        map.put("shoppingApState", strArr[i + 4]);
        map.put("wifiApState", strArr[i + 5]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean fetchModemDataByScene(String str, boolean z, String str2, int i, int i2) {
        String str3;
        int i3;
        String str4;
        int i4;
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone != null && MiuiPhoneUtils.getInsertedSimCount() != 0) {
            int phoneId = activeDataPhone.getPhoneId();
            Utils.logD("ModemHydraStat", "fetchModemDataByScene:" + str);
            ServiceState serviceState = activeDataPhone.getServiceState();
            if (serviceState != null && MiuiPhoneUtils.isMobileDataConnected(phoneId)) {
                boolean z2 = "GameLag".equals(str) || "StallDataApp".equals(str);
                if (serviceState.getRilDataRadioTechnology() == 20) {
                    if (z2) {
                        str3 = "AT+EDMFAPP=20100,420";
                        i3 = 420;
                    } else {
                        str3 = "AT+EDMFAPP=20100,422";
                        i3 = 422;
                    }
                    str4 = "AT+EDMFAPP=20100,460";
                    i4 = 460;
                } else if (serviceState.getRilDataRadioTechnology() == 14 || serviceState.getRilDataRadioTechnology() == 19) {
                    if (z2) {
                        str3 = "AT+EDMFAPP=20100,421";
                        i3 = 421;
                    } else {
                        str3 = "AT+EDMFAPP=20100,423";
                        i3 = 423;
                    }
                    str4 = "AT+EDMFAPP=20100,461";
                    i4 = 461;
                } else {
                    Utils.logD("ModemHydraStat", "not 4/5G, return");
                    return false;
                }
                Bundle bundle = new Bundle();
                bundle.putString(SettingsBackupConsts.EXTRA_PACKAGE_NAME, str2);
                bundle.putInt("stallSourceType", i);
                bundle.putInt("sceneType", i2);
                Message messageObtainMessage = this.mStatHandler.obtainMessage(111, i4, z ? 1 : 0, str);
                messageObtainMessage.setData(bundle);
                FiveGManagerBase.getInstance().sendAtCommand(phoneId, new String[]{str4, "+EDMFAPP"}, messageObtainMessage);
                Message messageObtainMessage2 = this.mStatHandler.obtainMessage(111, i3, z ? 1 : 0, str);
                messageObtainMessage2.setData(bundle);
                FiveGManagerBase.getInstance().sendAtCommand(phoneId, new String[]{str3, "+EDMFAPP"}, messageObtainMessage2);
                fetchModemDataForRatChange(phoneId, serviceState.getRilDataRadioTechnology(), str, z, str2, i, i2);
                if (!z || !"StallDataApp".equals(str)) {
                    return true;
                }
                this.dataStallStarted = true;
                return true;
            }
        }
        return false;
    }

    private void fetchModemDataForRatChange(int i, int i2, String str, boolean z, String str2, int i3, int i4) {
        String str3;
        int i5;
        String str4;
        int i6;
        boolean z2 = "GameLag".equals(str) || "StallDataApp".equals(str);
        if (i2 == 20) {
            if (z2) {
                str3 = "AT+EDMFAPP=20100,421";
                i5 = 421;
            } else {
                str3 = "AT+EDMFAPP=20100,423";
                i5 = 423;
            }
            str4 = "AT+EDMFAPP=20100,461";
            i6 = 461;
        } else {
            if (i2 != 14 && i2 != 19) {
                Utils.logD("ModemHydraStat", "not 4/5G, return");
                return;
            }
            if (z2) {
                str3 = "AT+EDMFAPP=20100,420";
                i5 = 420;
            } else {
                str3 = "AT+EDMFAPP=20100,422";
                i5 = 422;
            }
            str4 = "AT+EDMFAPP=20100,460";
            i6 = 460;
        }
        Bundle bundle = new Bundle();
        bundle.putString(SettingsBackupConsts.EXTRA_PACKAGE_NAME, str2);
        bundle.putInt("stallSourceType", i3);
        bundle.putInt("sceneType", i4);
        Message messageObtainMessage = this.mStatHandler.obtainMessage(111, i6, z ? 1 : 0, str);
        messageObtainMessage.setData(bundle);
        try {
            FiveGManagerBase.getInstance().sendAtCommand(i, new String[]{str4, "+EDMFAPP"}, messageObtainMessage);
        } catch (Exception e) {
            Utils.logD("ModemHydraStat", "Failed to send versioned command: " + e.getMessage());
        }
        Message messageObtainMessage2 = this.mStatHandler.obtainMessage(111, i5, z ? 1 : 0, str);
        messageObtainMessage2.setData(bundle);
        try {
            FiveGManagerBase.getInstance().sendAtCommand(i, new String[]{str3, "+EDMFAPP"}, messageObtainMessage2);
        } catch (Exception e2) {
            Utils.logD("ModemHydraStat", "Failed to send command: " + e2.getMessage());
        }
    }

    class PingpongDetector {
        private long mCellChgTime;
        private String mLastHexCell;
        private long mLastPingpongCellTime;
        private final Long PING_PONG_SHORT_SPAN = 10000L;
        private final Long PING_PONG_LONG_SPAN = 1800000L;
        private final ArrayList mCells = new ArrayList();

        PingpongDetector() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void detect(int i) {
            CellIdentity currentCellIdentity;
            if (ModemHydraStat.this.mStatsEnable) {
                Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
                Phone phone = PhoneFactory.getPhone(i);
                if (phone != activeDataPhone || phone == null || (currentCellIdentity = phone.getCurrentCellIdentity()) == null) {
                    Utils.logD("ModemHydraStat", "invalid phone info");
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
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                ModemHydraStat.this.recordCommonCellIdentity(concurrentHashMap, currentCellIdentity);
                if (currentCellIdentity instanceof CellIdentityLte) {
                    ModemHydraStat.getLteSignalInfo(concurrentHashMap, phone);
                } else {
                    if (!(currentCellIdentity instanceof CellIdentityNr)) {
                        Utils.logD("ModemHydraStat", " ignore non 4/5G cell");
                        return;
                    }
                    ModemHydraStat.getNrSignalInfo(concurrentHashMap, phone);
                }
                concurrentHashMap.put("ts", String.valueOf(jElapsedRealtime / 1000));
                this.mCells.add(concurrentHashMap);
                Utils.logD("ModemHydraStat", "current cell:" + concurrentHashMap);
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
                        Utils.logD("ModemHydraStat", "more than 3 cells found, NO pingpong");
                        this.mCells.clear();
                        return;
                    } else if (map.containsKey(str)) {
                        int iIntValue = ((Integer) map.get(str)).intValue() + 1;
                        if (iIntValue > 2) {
                            onPingpongDetected();
                            return;
                        }
                        map.put(str, Integer.valueOf(iIntValue));
                    } else {
                        map.put(str, 1);
                    }
                }
            }
        }

        private void onPingpongDetected() {
            String typeName;
            Utils.logW("ModemHydraStat", "cells pingpong detected");
            ModemHydraStat.this.mCellPingPongCounts++;
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long j = this.mLastPingpongCellTime;
            if (j > 0 && jElapsedRealtime - j < this.PING_PONG_LONG_SPAN.longValue()) {
                this.mCells.clear();
                Utils.logD("ModemHydraStat", "skip detecting in 30mins after pingpong detected");
                return;
            }
            this.mLastPingpongCellTime = jElapsedRealtime;
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            ConnectivityManager connectivityManager = (ConnectivityManager) ModemHydraStat.this.mContext.getSystemService("connectivity");
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
            if (ModemHydraStat.this.needRecordLocation()) {
                ModemHydraStat.this.getLocation();
                concurrentHashMap.put("latitude", String.valueOf(ModemHydraStat.this.mLatitude));
                concurrentHashMap.put("longitude", String.valueOf(ModemHydraStat.this.mLongitude));
            }
            this.mCells.add(concurrentHashMap);
            ModemHydraStat.this.mPingPongCellStats.addAll(this.mCells);
            this.mCells.clear();
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public long getCellPingPongCounts() {
        return this.mCellPingPongCounts;
    }

    public void clearCellPingPongCounts() {
        this.mCellPingPongCounts = 0L;
    }

    class ModemExceptionCollector {
        private boolean isVersioned426;
        private final SparseArray mDmfIdArray;
        private final int[] mVersionedDmfId;

        ModemExceptionCollector() {
            SparseArray sparseArray = new SparseArray();
            this.mDmfIdArray = sparseArray;
            this.mVersionedDmfId = new int[]{445, 446, 447, 448, 449, 453, 454, 455, 459, 472, 473, 490, 4001, 4002, 4006, 4007};
            this.isVersioned426 = false;
            sparseArray.put(402, "LteRlf");
            sparseArray.put(403, "NrRlf");
            sparseArray.put(com.ot.pubsub.i.a.c, "LteNas");
            sparseArray.put(405, "NrNas");
            sparseArray.put(406, "LteSch");
            sparseArray.put(407, "NrSch");
            sparseArray.put(ResponseObject.RELAY_TIMEOUT_CODE, "LteBler");
            sparseArray.put(409, "NrBler");
            sparseArray.put(410, "NrImsSip");
            sparseArray.put(411, "NrScg");
            sparseArray.put(412, "LteRach");
            sparseArray.put(413, "NrRach");
            sparseArray.put(414, "LteTas");
            sparseArray.put(415, "NrTas");
            sparseArray.put(426, "CallDrop");
            sparseArray.put(427, "N1Disable");
            sparseArray.put(445, "EpdgDns");
            sparseArray.put(446, "IkeSetup");
            sparseArray.put(447, "IkeSetup1");
            sparseArray.put(448, "NrOos");
            sparseArray.put(449, "LteOos");
            sparseArray.put(459, "CallFailRpt");
            sparseArray.put(472, "LteCapEnquiry");
            sparseArray.put(473, "NrCapEnquiry");
            sparseArray.put(453, "CallQuality");
            sparseArray.put(454, "SMSInfo");
            sparseArray.put(455, "ImsRegException");
            sparseArray.put(490, "CustomEnhFeature");
            sparseArray.put(4001, "LtePagingError");
            sparseArray.put(4002, "NrPagingError");
            sparseArray.put(4006, "abnormalTauInfo");
            sparseArray.put(4007, "MtAccessFail");
        }

        private static boolean isModemCallFailInfoSupported() {
            return HyperOSCustFeatureResolve.getBoolean("config_modem_call_fail_info_support", false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void pullModemRecords() {
            if (ModemHydraStat.this.mStatsEnable) {
                for (int i = 0; i < this.mDmfIdArray.size(); i++) {
                    int iKeyAt = this.mDmfIdArray.keyAt(i);
                    if (iKeyAt != 4006 || isModemCallFailInfoSupported()) {
                        String[] strArr = {"AT+EDMFAPP=20100," + iKeyAt, "+EDMFAPP"};
                        Object[] objArr = new Object[1];
                        ArrayList arrayList = new ArrayList();
                        boolean z = true;
                        for (int i2 = 0; i2 < 5 && z && FiveGManagerBase.getInstance().atCmdRunSync(0, strArr, objArr); i2++) {
                            String[] strArr2 = (String[]) objArr[0];
                            z = strArr2 != null && strArr2.length > 10;
                            ModemHydraStat.this.mMdExceptCollector.parseDmfResponse(objArr[0], iKeyAt, arrayList);
                        }
                        Utils.logD("ModemHydraStat", ((String) this.mDmfIdArray.get(iKeyAt)) + " " + arrayList);
                        ModemHydraStat.this.checkAndReport(arrayList, (String) this.mDmfIdArray.get(iKeyAt));
                    }
                }
            }
        }

        private void parseRemainArgs(int i, Map map, String[] strArr) {
            String str;
            if (i == 426) {
                int i2 = this.isVersioned426 ? 9 : 8;
                map.put("scs", strArr[i2]);
                map.put("rsrp", strArr[i2 + 1]);
                map.put("sinr", strArr[i2 + 2]);
                map.put("callType", strArr[i2 + 3]);
                map.put("ratInfo", strArr[i2 + 4]);
                map.put("direction", strArr[i2 + 5]);
                map.put("cause", strArr[i2 + 6]);
                map.put("reason", strArr[i2 + 7]);
                int i3 = i2 + 9;
                map.put("reasonFlag", strArr[i2 + 8]);
                if (this.isVersioned426) {
                    map.put("roamingStatus", strArr[i3]);
                    i3 = i2 + 10;
                }
                map.put("slot", strArr[i3]);
                return;
            }
            if (i != 427) {
                switch (i) {
                    case 402:
                    case 403:
                    case 412:
                        if (i == 402) {
                            map.put("cause", strArr[8]);
                            map.put("causeOther", strArr[9]);
                        } else if (i == 403) {
                            map.put("scs", strArr[8]);
                            map.put("cause", strArr[9]);
                        } else {
                            map.put("reason", strArr[8]);
                            map.put(com.ot.pubsub.a.a.L, strArr[9]);
                        }
                        map.put("A1", strArr[10]);
                        map.put("A2", strArr[11]);
                        map.put("A3", strArr[12]);
                        map.put("A4", strArr[13]);
                        break;
                    case com.ot.pubsub.i.a.c /* 404 */:
                        map.put("ts", strArr[8]);
                        map.put("cause", strArr[9]);
                        map.put("evt", strArr[10]);
                        map.put("histate0", strArr[11]);
                        map.put("histate1", strArr[12]);
                        map.put("slot", strArr[13]);
                        ModemHydraStat.this.extractApState(map, strArr, 14);
                        ModemHydraStat.this.updateAftersaleCount("LteNas_evt" + map.get("evt") + "_cause" + map.get("cause"), 1);
                        break;
                    case 405:
                        map.put("scs", strArr[8]);
                        map.put("ts", strArr[9]);
                        map.put("cause", strArr[10]);
                        map.put("evt", strArr[11]);
                        map.put("histate0", strArr[12]);
                        map.put("histate1", strArr[13]);
                        map.put("slot", strArr[14]);
                        ModemHydraStat.this.extractApState(map, strArr, 15);
                        ModemHydraStat.this.updateAftersaleCount("NrNas_evt" + map.get("evt") + "_cause" + map.get("cause"), 1);
                        break;
                    case 406:
                    case ResponseObject.RELAY_TIMEOUT_CODE /* 408 */:
                        map.put("Count", strArr[8]);
                        break;
                    case 407:
                    case 409:
                        map.put("scs", strArr[8]);
                        map.put("Count", strArr[9]);
                        break;
                    case 410:
                        map.put("scs", strArr[8]);
                        map.put("ts", strArr[9]);
                        map.put("direction", strArr[10]);
                        map.put("RAT", strArr[11]);
                        map.put("sipMethod", strArr[12]);
                        map.put("respCode", strArr[13]);
                        map.put("reason", strArr[14]);
                        map.put("warnFlag", strArr[15]);
                        ModemHydraStat.this.updateAftersaleCount("NrImsSip_respCode" + map.get("respCode"), 1);
                        break;
                    case 411:
                        if (strArr[9].length() > 1) {
                            str = strArr[9];
                        } else {
                            str = "0" + strArr[9];
                        }
                        map.put("nr_gci", strArr[8] + str + strArr[11]);
                        map.put("nr_tac", strArr[10]);
                        map.put("narfcn", strArr[12]);
                        map.put("NRband", strArr[13]);
                        map.put("NRpci", strArr[14]);
                        map.put("scs", strArr[15]);
                        map.put("Count", strArr[16]);
                        break;
                    case 413:
                        map.put("scs", strArr[8]);
                        map.put("reason", strArr[9]);
                        map.put(com.ot.pubsub.a.a.L, strArr[10]);
                        map.put("A1", strArr[11]);
                        map.put("A2", strArr[12]);
                        map.put("A3", strArr[13]);
                        map.put("A4", strArr[14]);
                        break;
                    case 414:
                    case 415:
                        map.put("N", strArr[1]);
                        map.put("N1", strArr[2]);
                        map.put("N2", strArr[3]);
                        break;
                    default:
                        Utils.logW("ModemHydraStat", "invalid response what:" + i);
                        break;
                }
                return;
            }
            map.put("scs", strArr[8]);
            map.put("n1_status", strArr[9]);
            map.put("n1_reason", strArr[10]);
            map.put("s1_status", strArr[11]);
            map.put("s1_reason", strArr[12]);
            map.put("ts", strArr[13]);
            map.put("slot", strArr[14]);
            ModemHydraStat.this.extractApState(map, strArr, 15);
            ModemHydraStat.this.updateAftersaleCount("N1Disable_n1reason" + map.get("n1_reason") + "_s1reason" + map.get("s1_reason"), 1);
        }

        private void parseDmfResponse(Object obj, int i, ArrayList arrayList) {
            String[] strArr = (String[]) obj;
            if (strArr.length > 1) {
                for (int i2 = 1; i2 < strArr.length; i2++) {
                    Utils.logD("ModemHydraStat", "DMFAPP resp:" + strArr[i2]);
                    String[] strArrSplit = strArr[i2].split(",");
                    if (i == 426 && strArrSplit.length - 2 > 17) {
                        this.isVersioned426 = true;
                        String str = strArrSplit[1];
                    }
                    if (ArrayUtils.contains(this.mVersionedDmfId, i)) {
                        Map versionedResponse = parseVersionedResponse(i, strArrSplit[1], strArrSplit);
                        if (versionedResponse != null && versionedResponse.size() > 0) {
                            arrayList.add(versionedResponse);
                            if (i == 459) {
                                ModemHydraStat.this.updateAftersaleMap(459, versionedResponse);
                            }
                        }
                    } else if (strArrSplit.length > 7) {
                        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                        if (i == 426 && this.isVersioned426) {
                            ModemHydraStat.this.updateVersionedCommonData(concurrentHashMap, strArrSplit);
                        } else {
                            ModemHydraStat.this.updateCommonDataFromLine(i, concurrentHashMap, strArrSplit);
                        }
                        parseRemainArgs(i, concurrentHashMap, strArrSplit);
                        arrayList.add(concurrentHashMap);
                    } else if (strArrSplit.length > 2) {
                        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                        parseRemainArgs(i, concurrentHashMap2, strArrSplit);
                        arrayList.add(concurrentHashMap2);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Map parseVersionedResponse(int i, String str, String[] strArr) {
            String[] strArr2;
            String str2;
            int i2 = 20;
            if (i != 459) {
                if (i == 490) {
                    strArr2 = new String[]{"slotId", "enhType", "enhSubType", "serRsrp", "serSinr", "nbrRsrp", "nbrSinr", "serFreq", "serPci", "nbrFreq", "nbrPci"};
                } else if (i == 472) {
                    strArr2 = new String[]{"mcc", "mnc", "rrcSegAllowed", "reducedFormat", "maxccsdl", "maxccsul", "reducedContComb", " diffFallBackComb", "eutraNrOnly", "srcRats", "freqBandsNum", "eutra", "mrdc", "nr", "utra", "geranCs", "geranPs", "c2k1xrtt"};
                } else if (i == 473) {
                    strArr2 = new String[]{"mcc", "mnc", "rrcSegAllowed", "omitEndc", "nrdc", "nedc", "sglePanel", "multiPanel", "r16", "portSelect", "ulTxSwitchReq", "enqSrcRat", "eutra", "mrdc", "nr", "utraFdd", "freqBandsNum", "careBandsNum"};
                } else if (i == 4001 || i == 4002) {
                    strArr2 = new String[]{"mcc", "mnc", "tac", "cellId", "dlFreq", "freqBand", "pci", "A1", "A2", "A3", "A4"};
                } else if (i == 4006) {
                    strArr2 = new String[]{"hplmn", "epsUpdateResult", "imsVops", "causeValue", "addUpdateResult", "sos", "tauDisconnect"};
                } else if (i != 4007) {
                    switch (i) {
                        case 445:
                            strArr2 = new String[]{"ret", "fqdn", "dnsErr", "ipv4Cnt", "ipv6Cnt"};
                            break;
                        case 446:
                            strArr2 = new String[]{"slot", "errType", "errInternal", "support", "ip"};
                            break;
                        case 447:
                            strArr2 = new String[]{"ts", "slot", "ret", "support"};
                            break;
                        case 448:
                        case 449:
                            strArr2 = new String[]{"ts", "slot", "cause", "roamingStatus"};
                            break;
                        default:
                            switch (i) {
                                case 453:
                                    strArr2 = new String[]{"subId", "roamingStatus", "callDir", "callRat", "voiceRtpLostReason", "noVoiceRbConn", "dsda", "mcc", "mnc", "voiceRtpLossRat", "noRtpTimes", "rtpHlossRateTimes", "lteVoiceUlHlossRateTimes", "lteVideoUlHlossRateTimes", "nrVoiceUlHlossRateTimes", "nrVideoUlHlossRateTimes", "lteVoiceDlHlossRateTimes", "lteVideoDlHlossRateTimes", "nrVoiceDlHlossRateTimes", "nrVideoDlHlossRateTimes", "redirTimes", "callHoTimes", "rlfTimes", "lteVoiceUlLossRate", "lteVideoUlLossRate", "nrVoiceUlLossRate", "nrVideoUlLossRate", "lteVoiceDlLossRate", "lteVideoDlLossRate", "nrVoiceDlLossRate", "nrVideoDlLossRate", "frqBandInd", "phyCid", "callEndTime", "callStartTime", "rtpRePacs", "voiceUlTxSduCnt", "videoUlTxSduCnt", "dlFrq", "tac", "cid", "rsrp", "sinr", "ulGrant", "ulBler", "dlBler"};
                                    break;
                                case 454:
                                    strArr2 = new String[]{"subId", "roamingStatus", "dir", "rat", "domain", "isRetry", "mcc", "mnc", "frqBandInd", "phyCid", "failCause", "sipErrCause", "modemTime", "dlFrq", "tac", "cid"};
                                    break;
                                case 455:
                                    strArr2 = new String[]{"subId", "roamingStatus", "regType", "Rat", "regServ", "sipUriType", "mcc", "mnc", "frqBandInd", "phyCid", "modemTime", "dLfreq", "tac", "cid", "failReason"};
                                    break;
                                default:
                                    strArr2 = null;
                                    break;
                            }
                            break;
                    }
                } else {
                    strArr2 = new String[]{"mcc", "mnc", "failReason", "stuckAtStep", "rat", "rsrp", "sinr", "rachTriggered", "rachResult", "rrcConnected", "nasTriggered", "nasRejected", "nasRejectCause", "pagingTime"};
                }
            } else if (Integer.parseInt(str) > 6) {
                strArr2 = new String[]{"hMcc", "hMnc", "rMcc", "rMnc", "roamStats", "rat", "callDomian", "dir", "band", "freq", "cid", "pci", "tac", "rsrp", "sinr", "ulGrant", "ulBler", "dlBler", "LossRate", "txPower", "mtpl", "codec", "rptType", "discCuz", "errFlag", "nasEmmEvt", "nasEmmCuz", "nasEsmEvt", "nasEsmCuz", "rlfCuz", "srcRat", "desRat", "hoErrCuz", "epsfbRedirCuz", "epsfbHoCuz", "srvccCuz", "csfbCuz", "sipErrCode", "reasonTxtFlag", "rlfRat", "ts", "nasEmmRat", "nasEsmRat", "callType", "callEndState", "sipFlow", "callDur", "qciSta", "vMmFlow", "eMmFlow", "nrrcSta", "nrrcChgCuz", "errcSta", "errcChgCuz", "regType", "regResult", "failDomain0", "failCuz0", "failDomain1", "failCuz1", "reasonTxt", "warningTxt", "issueCode", "callChange", "callReason", "regReason"};
            } else if (Integer.parseInt(str) > 5) {
                strArr2 = new String[]{"hMcc", "hMnc", "rMcc", "rMnc", "roamStats", "rat", "callDomian", "dir", "band", "freq", "cid", "pci", "tac", "rsrp", "sinr", "ulGrant", "ulBler", "dlBler", "LossRate", "txPower", "mtpl", "codec", "rptType", "discCuz", "errFlag", "nasEmmEvt", "nasEmmCuz", "nasEsmEvt", "nasEsmCuz", "rlfCuz", "srcRat", "desRat", "hoErrCuz", "epsfbRedirCuz", "epsfbHoCuz", "srvccCuz", "csfbCuz", "sipErrCode", "reasonTxtFlag", "rlfRat", "ts", "nasEmmRat", "nasEsmRat", "callType", "callEndState"};
            } else if (Integer.parseInt(str) > 4) {
                strArr2 = new String[]{"hMcc", "hMnc", "rMcc", "rMnc", "roamStats", "rat", "callDomian", "dir", "band", "freq", "cid", "pci", "tac", "rsrp", "sinr", "ulGrant", "ulBler", "dlBler", "LossRate", "txPower", "mtpl", "codec", "rptType", "discCuz", "errFlag", "nasEmmEvt", "nasEmmCuz", "nasEsmEvt", "nasEsmCuz", "rlfCuz", "srcRat", "desRat", "hoErrCuz", "epsfbRedirCuz", "epsfbHoCuz", "srvccCuz", "csfbCuz", "sipErrCode", "reasonTxtFlag", "rlfRat", "ts"};
            } else if (Integer.parseInt(str) == 3) {
                strArr2 = new String[]{"hMcc", "hMnc", "rMcc", "rMnc", "roamStats", "rat", "band", "freq", "cid", "pci", "tac", "rsrp", "sinr", "ulGrant", "ulBler", "txPower", "nasEmmEvt", "nasEmmCuz", "nasEsmEvt", "nasEsmCuz", "rlfCuz", "srcRat", "desRat", "hoErrCuz", "srvccCuz", "csfbCuz", "sipErrCode", "reasonTxtFlag", "sipFlow", "reasonTxt", "warningTxt", "rlfRat", "ts", "nasEmmRat", "nasEsmRat", "callType", "qciSta", "eMmFlow", "errcSta", "errcChgCuz"};
            } else {
                strArr2 = new String[]{"hMcc", "hMnc", "rMcc", "rMnc", "roamStats", "rat", "callDomian", "dir", "band", "freq", "cid", "pci", "tac", "rsrp", "sinr", "ulGrant", "ulBler", "dlBler", "LossRate", "txPower", "mtpl", "codec", "rptType", "discCuz", "errFlag", "nasEmmEvt", "nasEmmCuz", "nasEsmEvt", "nasEsmCuz", "rlfCuz", "srcRat", "desRat", "hoErrCuz", "epsfbRedirCuz", "epsfbHoCuz", "srvccCuz", "csfbCuz", "sipErrCode", "reasonTxtFlag"};
            }
            ConcurrentHashMap concurrentHashMap = null;
            if (strArr2 != null) {
                if (strArr2.length > strArr.length - 2) {
                    Utils.logW("ModemHydraStat", "mismatched response");
                    return null;
                }
                concurrentHashMap = new ConcurrentHashMap();
                int i3 = 0;
                while (i3 < strArr2.length) {
                    if (i == 459 && Build.IS_INTERNATIONAL_BUILD) {
                        if (!"pci".equals(strArr2[i3]) && !"tac".equals(strArr2[i3])) {
                            if ("cid".equals(strArr2[i3])) {
                                concurrentHashMap.put(strArr2[i3], ModemHydraStat.this.processCellId(strArr[i3 + 2]));
                            } else {
                                concurrentHashMap.put(strArr2[i3], strArr[i3 + 2]);
                            }
                        }
                    } else {
                        concurrentHashMap.put(strArr2[i3], strArr[i3 + 2]);
                    }
                    i3++;
                }
                if (i == 472) {
                    for (int i4 = 0; i4 < 16; i4++) {
                        concurrentHashMap.put("freqBand_" + i4, strArr[i3 + 2]);
                        i3++;
                    }
                } else if (i == 473) {
                    int i5 = 0;
                    while (i5 < 80) {
                        int i6 = i2;
                        if (i5 < i6) {
                            str2 = "freqSelect";
                        } else if (i5 < 40) {
                            str2 = "careSelect";
                        } else if (i5 < 60) {
                            str2 = "freqBand";
                        } else {
                            str2 = "careBand";
                        }
                        concurrentHashMap.put(str2 + "_" + (i5 % 20), strArr[i3 + 2]);
                        i3++;
                        i5++;
                        i2 = i6;
                    }
                }
            }
            return concurrentHashMap;
        }
    }

    class RfCollector {
        private final String[] bandNames;
        private final SparseArray mDmfIdArray;
        private final int[] mScenceDmfId;
        private final int[] mVersionedDmfId;

        RfCollector() {
            SparseArray sparseArray = new SparseArray();
            this.mDmfIdArray = sparseArray;
            this.mVersionedDmfId = new int[]{466, 467, 468, 469, 470, 471, 474, 475, 476, 486, 488, 489, 499, 4003, 4004, 4005, 700, 4008};
            this.mScenceDmfId = new int[]{491, 492, 493, 494, 495, 496, 497, 498};
            this.bandNames = new String[]{"B1", "B3", "B5", "B7", "B8", "B20", "B28", "B39", "B40", "B41", "N1", "N5", "N28", "N41", "N78"};
            sparseArray.put(416, "LteMeas");
            sparseArray.put(417, "NrMeas");
            sparseArray.put(418, "LteTxPwr");
            sparseArray.put(419, "NrTxPwr");
            sparseArray.put(424, "EndcBand");
            sparseArray.put(428, "El1Tas");
            sparseArray.put(429, "Nl1Tas");
            sparseArray.put(430, "El1Pdcch");
            sparseArray.put(431, "EScellBw");
            sparseArray.put(432, "El1Pucch");
            sparseArray.put(433, "LteMeas2");
            sparseArray.put(434, "NrMeas2");
            sparseArray.put(435, "Nl1Dci");
            sparseArray.put(436, "Nl1PhyConfig");
            sparseArray.put(437, "Nl1DciStats");
            sparseArray.put(438, "Nl1Bw");
            sparseArray.put(439, "Nl1Fdd");
            sparseArray.put(440, "El1TFdd");
            sparseArray.put(443, "NsaBandComb");
            sparseArray.put(444, "MipiErr");
            sparseArray.put(466, "SceneLteTxPwr");
            sparseArray.put(467, "SceneNrTxPwr");
            sparseArray.put(468, "PuschPucchSrsTxPwr");
            sparseArray.put(469, "RsrpPuschTxPwr");
            sparseArray.put(470, "LteCaBand");
            sparseArray.put(471, "NrCaBand");
            sparseArray.put(474, "LteUlModulation");
            sparseArray.put(475, "LteRbCount");
            sparseArray.put(476, "NrRbCount");
            sparseArray.put(486, "PaDetect");
            sparseArray.put(488, "DoubHandPuschPucchSrsTxPwr");
            sparseArray.put(489, "SingleHandPuschPucchSrsTxPwr");
            sparseArray.put(498, "SingleDoubLteTxPwr");
            sparseArray.put(491, "SingleDoubNrTxPwr");
            sparseArray.put(492, "SingleDoubLteRsrp");
            sparseArray.put(493, "SingleDoubNrRsrp");
            sparseArray.put(494, "SingleDoubLteSinr");
            sparseArray.put(495, "SingleDoubNrSinr");
            sparseArray.put(496, "SingleDoubLteAntNum");
            sparseArray.put(497, "SingleDoubNrAntNum");
            sparseArray.put(499, "LteNrBlerCount");
            sparseArray.put(AnimTask.MAX_MAIN_THREAD_TASK_SIZE, "PaDetectV3");
            sparseArray.put(4003, "RxSAWlessCount");
            sparseArray.put(4004, "RxSAWlessBreakOutCauseCnt");
            sparseArray.put(4005, "RxSAWlessDeactivationCauseCnt");
            sparseArray.put(700, "CameraAntNum");
            sparseArray.put(4008, "TxPwrPcmax");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void pullDataFromMd() {
            if (ModemHydraStat.this.mStatsEnable) {
                for (int i = 0; i < this.mDmfIdArray.size(); i++) {
                    int iKeyAt = this.mDmfIdArray.keyAt(i);
                    String[] strArr = {"AT+EDMFAPP=20100," + iKeyAt, "+EDMFAPP"};
                    Object[] objArr = new Object[1];
                    ArrayList arrayList = new ArrayList();
                    int i2 = iKeyAt == 469 ? 6 : 5;
                    int i3 = 0;
                    boolean z = true;
                    while (i3 < i2 && z && FiveGManagerBase.getInstance().atCmdRunSync(0, strArr, objArr)) {
                        Object obj = objArr[0];
                        String[] strArr2 = (String[]) obj;
                        boolean z2 = strArr2 != null && strArr2.length > 10;
                        try {
                            parseRfResponse(obj, iKeyAt, arrayList, i3);
                        } catch (Exception unused) {
                        }
                        i3++;
                        z = z2;
                    }
                    Utils.logD("ModemHydraStat", ((String) this.mDmfIdArray.get(iKeyAt)) + " " + arrayList);
                    ModemHydraStat.this.checkAndReport(arrayList, (String) this.mDmfIdArray.get(iKeyAt));
                }
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code duplicated, block: B:140:0x036a A[LOOP:3: B:138:0x0366->B:140:0x036a, LOOP_END] */
        /* JADX WARN: Code duplicated, block: B:161:0x0408 A[LOOP:4: B:159:0x0404->B:161:0x0408, LOOP_END] */
        /* JADX WARN: Code duplicated, block: B:166:0x0429 A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:167:0x042b  */
        /* JADX WARN: Code duplicated, block: B:168:0x042f  */
        /* JADX WARN: Code duplicated, block: B:170:0x0433  */
        /* JADX WARN: Code duplicated, block: B:171:0x0442  */
        /* JADX WARN: Code duplicated, block: B:177:0x0466 A[LOOP:6: B:175:0x0462->B:177:0x0466, LOOP_END] */
        /* JADX WARN: Code duplicated, block: B:187:0x04d1 A[PHI: r12
  0x04d1: PHI (r12v12 int) = (r12v4 int), (r12v29 int), (r12v4 int) binds: [B:67:0x0177, B:186:0x04c6, B:80:0x0191] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Code duplicated, block: B:188:0x04d5  */
        /* JADX WARN: Code duplicated, block: B:190:0x04e3  */
        /* JADX WARN: Code duplicated, block: B:193:0x04e8 A[LOOP:26: B:191:0x04e4->B:193:0x04e8, LOOP_END] */
        /* JADX WARN: Code duplicated, block: B:196:0x0519  */
        /* JADX WARN: Code duplicated, block: B:197:0x0520  */
        /* JADX WARN: Code duplicated, block: B:199:0x052b  */
        /* JADX WARN: Code duplicated, block: B:200:0x052e  */
        /* JADX WARN: Code duplicated, block: B:204:0x0559 A[LOOP:27: B:202:0x0556->B:204:0x0559, LOOP_END] */
        /* JADX WARN: Code duplicated, block: B:207:0x0577 A[LOOP:28: B:206:0x0575->B:207:0x0577, LOOP_END] */
        /* JADX WARN: Code duplicated, block: B:212:0x059a A[LOOP:9: B:210:0x0596->B:212:0x059a, LOOP_END] */
        /* JADX WARN: Code duplicated, block: B:213:0x05b1  */
        /* JADX WARN: Code duplicated, block: B:215:0x05c9  */
        /* JADX WARN: Code duplicated, block: B:223:0x05ee  */
        /* JADX WARN: Code duplicated, block: B:225:0x05f3 A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:226:0x05f5  */
        /* JADX WARN: Code duplicated, block: B:229:0x05f9  */
        /* JADX WARN: Code duplicated, block: B:235:0x061a A[LOOP:29: B:233:0x0616->B:235:0x061a, LOOP_END] */
        /* JADX WARN: Code duplicated, block: B:238:0x0638 A[LOOP:30: B:237:0x0636->B:238:0x0638, LOOP_END] */
        /* JADX WARN: Code duplicated, block: B:239:0x0653  */
        /* JADX WARN: Code duplicated, block: B:248:0x0603 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:249:0x0603 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:64:0x0167  */
        /* JADX WARN: Code duplicated, block: B:66:0x016b  */
        /* JADX WARN: Code duplicated, block: B:68:0x0179  */
        /* JADX WARN: Code duplicated, block: B:70:0x017d  */
        /* JADX WARN: Code duplicated, block: B:72:0x0181  */
        /* JADX WARN: Code duplicated, block: B:98:0x0213  */
        /* JADX WARN: Code duplicated, block: B:99:0x0217  */
        /* JADX WARN: Failed to find 'out' block for switch in B:80:0x0191. Please report as an issue. */
        /* JADX WARN: Failed to find 'out' block for switch in B:82:0x0195. Please report as an issue. */
        /* JADX WARN: Failed to find 'out' block for switch in B:83:0x0198. Please report as an issue. */
        /* JADX WARN: Failed to find 'out' block for switch in B:84:0x019b. Please report as an issue. */
        /* JADX WARN: Failed to find 'out' block for switch in B:85:0x019e. Please report as an issue. */
        /* JADX WARN: Instruction removed from duplicated block: B:140:0x036a, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:161:0x0408, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:177:0x0466, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:193:0x04e8, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:200:0x052e, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:204:0x0559, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:207:0x0577, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:212:0x059a, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:235:0x061a, please report this as an issue */
        /* JADX WARN: Instruction removed from duplicated block: B:238:0x0638, please report this as an issue */
        private void parseRfResponse(Object obj, int i, ArrayList arrayList, int i2) {
            int i3;
            String[] strArr;
            char c;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            String str;
            int i9;
            int i10;
            int i11;
            String str2;
            int i12;
            String str3;
            int i13;
            int i14;
            String string;
            int i15;
            int i16;
            String[] strArr2 = (String[]) obj;
            if (strArr2 == null || arrayList.size() >= 60) {
                return;
            }
            int i17 = 0;
            int i18 = 1;
            if (strArr2.length == 1) {
                if (i == 486 || i == 4000) {
                    String[] strArrSplit = strArr2[0].split(",");
                    if (strArrSplit.length < 7) {
                        return;
                    }
                    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                    concurrentHashMap.put("modemVersion", strArrSplit[1]);
                    String[] strArr3 = {"N1", "N5", "N28", "N41", "N78"};
                    int i19 = 2;
                    while (i17 < 5) {
                        concurrentHashMap.put(strArr3[i17], Integer.valueOf(Integer.parseInt(strArrSplit[i19].trim())));
                        i17++;
                        i19++;
                    }
                    arrayList.add(concurrentHashMap);
                    return;
                }
                if (i == 4003) {
                    String[] strArrSplit2 = strArr2[0].split(",");
                    if (strArrSplit2.length < 18) {
                        return;
                    }
                    String[] strArr4 = {"n34", "n38", "n39", "n40", "n41", "n77", "n78", "n79"};
                    int i20 = 10;
                    int i21 = 2;
                    while (i17 < 8) {
                        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
                        concurrentHashMap2.put("band", strArr4[i17]);
                        concurrentHashMap2.put("saw", strArrSplit2[i21]);
                        concurrentHashMap2.put("sawless", strArrSplit2[i20]);
                        arrayList.add(concurrentHashMap2);
                        i17++;
                        i21++;
                        i20++;
                    }
                    return;
                }
                return;
            }
            if (strArr2.length > 1) {
                int i22 = 1;
                while (i22 < strArr2.length) {
                    Utils.logD("ModemHydraStat", "RF DMFAPP resp:" + strArr2[i22]);
                    String[] strArrSplit3 = strArr2[i22].split(",");
                    ConcurrentHashMap concurrentHashMap3 = new ConcurrentHashMap();
                    if (ArrayUtils.contains(this.mVersionedDmfId, i)) {
                        String str4 = strArrSplit3[i18];
                        i3 = 2;
                    } else {
                        i3 = i18;
                    }
                    if (i != 443 && i != 444 && i != 470 && i != 471 && i != 499 && i != 700) {
                        int i23 = i3 + 1;
                        String string2 = strArrSplit3[i3];
                        int i24 = Integer.parseInt(string2.trim());
                        if (i24 < 1) {
                            strArr = strArr2;
                            c = 5;
                        } else {
                            if (i == 475 || i == 476) {
                                concurrentHashMap3.put("modemVersion", strArrSplit3[1]);
                                StringBuilder sb = i == 475 ? new StringBuilder(b.a) : new StringBuilder("N");
                                sb.append(i24);
                                string2 = sb.toString();
                            }
                            concurrentHashMap3.put("band", string2);
                            i3 = i23;
                            processSceneStatus(i, i2, i22, concurrentHashMap3);
                            if (i != 424) {
                                if (i != 700) {
                                    strArr = strArr2;
                                    if (i == 4008) {
                                        concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                        i8 = -25;
                                        while (i8 < 27) {
                                            concurrentHashMap3.put("tx" + i8, strArrSplit3[i3]);
                                            i8++;
                                            i3++;
                                        }
                                    } else if (i != 443) {
                                        String str5 = strArrSplit3[i3];
                                        if (strArrSplit3[2].length() > 1) {
                                            str = strArrSplit3[2];
                                        } else {
                                            str = "0" + strArrSplit3[2];
                                        }
                                        i9 = i3 + 2;
                                        concurrentHashMap3.put("mccMnc", str5 + str);
                                        i10 = 0;
                                        while (i10 < 5) {
                                            concurrentHashMap3.put("lteBands" + i10, strArrSplit3[i9]);
                                            i10++;
                                            i9++;
                                        }
                                        i11 = 0;
                                        while (i11 < 2) {
                                            concurrentHashMap3.put("nr5gBands" + i11, strArrSplit3[i9]);
                                            i11++;
                                            i9++;
                                        }
                                    } else if (i != 444) {
                                        if (i != 488) {
                                            concurrentHashMap3.put(com.ot.pubsub.a.a.J, strArrSplit3[i3]);
                                            i3++;
                                            concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                        } else {
                                            concurrentHashMap3.put(com.ot.pubsub.a.a.J, strArrSplit3[i3]);
                                            i3++;
                                            concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                        }
                                        i8 = -25;
                                        while (i8 < 27) {
                                            concurrentHashMap3.put("tx" + i8, strArrSplit3[i3]);
                                            i8++;
                                            i3++;
                                        }
                                    } else {
                                        str2 = strArrSplit3[42];
                                        if (Integer.parseInt(str2.trim()) > 0) {
                                            i12 = 0;
                                            while (i12 < 41) {
                                                concurrentHashMap3.put("id" + i12, strArrSplit3[i3].trim());
                                                i12++;
                                                i3++;
                                            }
                                            concurrentHashMap3.put("devNum", str2);
                                            str3 = SystemProperties.get(ModemSarUtils.PROPERTY_HWID_VER, "");
                                            if (!TextUtils.isEmpty(str3)) {
                                                concurrentHashMap3.put("hwversion", str3);
                                            }
                                        }
                                    }
                                    c = 5;
                                } else {
                                    strArr = strArr2;
                                    int i25 = i3 + 1;
                                    concurrentHashMap3.put("cameraTotalDuration", strArrSplit3[i3]);
                                    i4 = i3 + 2;
                                    concurrentHashMap3.put("cameraOpenCount", strArrSplit3[i25]);
                                    if (i2 != 0) {
                                        if (i22 > 0) {
                                        }
                                        Utils.logW("ModemHydraStat", "Invalid lte band index: " + i22);
                                        return;
                                    }
                                    if (i2 == 1) {
                                        if (i22 > 0) {
                                            c = 5;
                                            if (i22 > 5) {
                                                concurrentHashMap3.put("band", this.bandNames[i22 + 9]);
                                            }
                                        }
                                        Utils.logW("ModemHydraStat", "Invalid nr band index: " + i22);
                                        return;
                                    }
                                    i5 = 0;
                                    while (i5 < 12) {
                                        concurrentHashMap3.put("ant0_" + i5, strArrSplit3[i4]);
                                        i5++;
                                        i4++;
                                    }
                                    i7 = 0;
                                    for (i6 = 12; i7 < i6; i6 = 12) {
                                        concurrentHashMap3.put("ant1_" + i7, strArrSplit3[i4]);
                                        i7++;
                                        i4++;
                                    }
                                    c = 5;
                                    i5 = 0;
                                    while (i5 < 12) {
                                        concurrentHashMap3.put("ant0_" + i5, strArrSplit3[i4]);
                                        i5++;
                                        i4++;
                                    }
                                    i7 = 0;
                                    while (i7 < i6) {
                                        concurrentHashMap3.put("ant1_" + i7, strArrSplit3[i4]);
                                        i7++;
                                        i4++;
                                    }
                                }
                                arrayList.add(concurrentHashMap3);
                            } else {
                                strArr = strArr2;
                            }
                            c = 5;
                            arrayList.add(concurrentHashMap3);
                        }
                    } else {
                        processSceneStatus(i, i2, i22, concurrentHashMap3);
                        if (i != 424) {
                            if (i != 700) {
                                strArr = strArr2;
                                if (i == 4008) {
                                    concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                    i8 = -25;
                                    while (i8 < 27) {
                                        concurrentHashMap3.put("tx" + i8, strArrSplit3[i3]);
                                        i8++;
                                        i3++;
                                    }
                                } else if (i != 443) {
                                    String str6 = strArrSplit3[i3];
                                    if (strArrSplit3[2].length() > 1) {
                                        str = strArrSplit3[2];
                                    } else {
                                        str = "0" + strArrSplit3[2];
                                    }
                                    i9 = i3 + 2;
                                    concurrentHashMap3.put("mccMnc", str6 + str);
                                    i10 = 0;
                                    while (i10 < 5) {
                                        concurrentHashMap3.put("lteBands" + i10, strArrSplit3[i9]);
                                        i10++;
                                        i9++;
                                    }
                                    i11 = 0;
                                    while (i11 < 2) {
                                        concurrentHashMap3.put("nr5gBands" + i11, strArrSplit3[i9]);
                                        i11++;
                                        i9++;
                                    }
                                } else if (i != 444) {
                                    if (i != 488 || i == 489) {
                                        concurrentHashMap3.put(com.ot.pubsub.a.a.J, strArrSplit3[i3]);
                                        i3++;
                                        concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                    } else if (i == 4004) {
                                        int i26 = 1;
                                        while (i26 < 5) {
                                            concurrentHashMap3.put("deactivationCause_" + i26, strArrSplit3[i3]);
                                            i26++;
                                            i3++;
                                        }
                                        concurrentHashMap3.put("deactivationCause_7", strArrSplit3[i3]);
                                    } else if (i != 4005) {
                                        switch (i) {
                                            case 416:
                                            case 417:
                                                concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                                i13 = -140;
                                                while (i13 < -59) {
                                                    concurrentHashMap3.put("rsrp" + i13, strArrSplit3[i3]);
                                                    i13++;
                                                    i3++;
                                                }
                                                break;
                                            case 418:
                                                concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                                break;
                                            case 419:
                                                concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                                i14 = 0;
                                                while (i14 < 105) {
                                                    if (i14 == 0) {
                                                        string = "txErr";
                                                    } else if (i14 < 53) {
                                                        StringBuilder sb2 = new StringBuilder("tx");
                                                        sb2.append(i14 - 26);
                                                        string = sb2.toString();
                                                    } else {
                                                        StringBuilder sb3 = new StringBuilder("eTx");
                                                        sb3.append(i14 - 78);
                                                        string = sb3.toString();
                                                    }
                                                    concurrentHashMap3.put(string, strArrSplit3[i3]);
                                                    i14++;
                                                    i3++;
                                                }
                                                break;
                                            default:
                                                switch (i) {
                                                    case 428:
                                                    case 429:
                                                        concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                                        i15 = 0;
                                                        while (i15 < 12) {
                                                            concurrentHashMap3.put("ant" + i15, strArrSplit3[i3]);
                                                            i15++;
                                                            i3++;
                                                        }
                                                        break;
                                                    case 430:
                                                        int i27 = 0;
                                                        while (i27 < 8) {
                                                            concurrentHashMap3.put("udc" + i27, strArrSplit3[i3]);
                                                            i27++;
                                                            i3++;
                                                        }
                                                        break;
                                                    case 431:
                                                        String[] strArr5 = {"1.4M", "3M", "5M", "10M", "15M", "20M"};
                                                        int i28 = 0;
                                                        while (i28 < 6) {
                                                            concurrentHashMap3.put(strArr5[i28], strArrSplit3[i3]);
                                                            i28++;
                                                            i3++;
                                                        }
                                                        break;
                                                    case 432:
                                                        int i29 = 1;
                                                        while (i29 < 5) {
                                                            concurrentHashMap3.put("rank" + i29, strArrSplit3[i3]);
                                                            i29++;
                                                            i3++;
                                                        }
                                                        int i30 = 0;
                                                        while (i30 < 16) {
                                                            concurrentHashMap3.put("cw" + i30, strArrSplit3[i3]);
                                                            i30++;
                                                            i3++;
                                                        }
                                                        break;
                                                    case 433:
                                                    case 434:
                                                        concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                                        i16 = -20;
                                                        while (i16 < 31) {
                                                            concurrentHashMap3.put("snr" + i16, strArrSplit3[i3]);
                                                            i16++;
                                                            i3++;
                                                        }
                                                        break;
                                                    case 435:
                                                        int i31 = 0;
                                                        while (i31 < 21) {
                                                            concurrentHashMap3.put("tdd" + i31, strArrSplit3[i3]);
                                                            i31++;
                                                            i3++;
                                                        }
                                                        break;
                                                    case 436:
                                                        concurrentHashMap3.put("narfcnL", strArrSplit3[i3]);
                                                        concurrentHashMap3.put("narfcnM", strArrSplit3[i3 + 1]);
                                                        concurrentHashMap3.put("narfcnH", strArrSplit3[i3 + 2]);
                                                        break;
                                                    case 437:
                                                        int i32 = 0;
                                                        while (i32 < 5) {
                                                            concurrentHashMap3.put("ulModOrder" + i32, strArrSplit3[i3]);
                                                            i32++;
                                                            i3++;
                                                        }
                                                        int i33 = 0;
                                                        while (i33 < 4) {
                                                            concurrentHashMap3.put("dlModOrder" + i33, strArrSplit3[i3]);
                                                            i33++;
                                                            i3++;
                                                        }
                                                        int i34 = 0;
                                                        while (i34 < 32) {
                                                            concurrentHashMap3.put("ulMcsIdx" + i34, strArrSplit3[i3]);
                                                            i34++;
                                                            i3++;
                                                        }
                                                        int i35 = 0;
                                                        while (i35 < 32) {
                                                            concurrentHashMap3.put("dlMcsIdx" + i35, strArrSplit3[i3]);
                                                            i35++;
                                                            i3++;
                                                        }
                                                        int i36 = 0;
                                                        while (i36 < 4) {
                                                            concurrentHashMap3.put("ulLayer" + i36, strArrSplit3[i3]);
                                                            i36++;
                                                            i3++;
                                                        }
                                                        int i37 = 0;
                                                        while (i37 < 4) {
                                                            concurrentHashMap3.put("dlLayer" + i37, strArrSplit3[i3]);
                                                            i37++;
                                                            i3++;
                                                        }
                                                        break;
                                                    case 438:
                                                        int i38 = 0;
                                                        while (i38 < 15) {
                                                            concurrentHashMap3.put("bw" + i38, strArrSplit3[i3]);
                                                            i38++;
                                                            i3++;
                                                        }
                                                        break;
                                                    case 439:
                                                        int i39 = 0;
                                                        while (i39 < 11) {
                                                            concurrentHashMap3.put("fdd" + i39, strArrSplit3[i3]);
                                                            i39++;
                                                            i3++;
                                                        }
                                                        break;
                                                    case 440:
                                                        int i40 = 0;
                                                        while (i40 < 11) {
                                                            concurrentHashMap3.put("tfdd" + i40, strArrSplit3[i3]);
                                                            i40++;
                                                            i3++;
                                                        }
                                                        break;
                                                    default:
                                                        switch (i) {
                                                            case 466:
                                                            case 467:
                                                                break;
                                                            case 468:
                                                            case 469:
                                                                concurrentHashMap3.put(com.ot.pubsub.a.a.J, strArrSplit3[i3]);
                                                                i3++;
                                                                concurrentHashMap3.put("sceneStatus", "ScreenOn");
                                                                break;
                                                            case 470:
                                                            case 471:
                                                                concurrentHashMap3.put("mcc", strArrSplit3[i3]);
                                                                concurrentHashMap3.put("mnc", strArrSplit3[i3 + 1]);
                                                                concurrentHashMap3.put("caBand", strArrSplit3[i3 + 2]);
                                                                break;
                                                            default:
                                                                switch (i) {
                                                                    case 474:
                                                                        int i41 = 0;
                                                                        while (i41 < 4) {
                                                                            concurrentHashMap3.put("modulation" + i41, strArrSplit3[i3]);
                                                                            i41++;
                                                                            i3++;
                                                                        }
                                                                        break;
                                                                    case 475:
                                                                    case 476:
                                                                        int i42 = 0;
                                                                        while (i42 < 14) {
                                                                            concurrentHashMap3.put("rbCnt" + i42, strArrSplit3[i3]);
                                                                            i42++;
                                                                            i3++;
                                                                        }
                                                                        break;
                                                                    default:
                                                                        switch (i) {
                                                                            case 491:
                                                                                i14 = 0;
                                                                                while (i14 < 105) {
                                                                                    if (i14 == 0) {
                                                                                        string = "txErr";
                                                                                    } else if (i14 < 53) {
                                                                                        StringBuilder sb4 = new StringBuilder("tx");
                                                                                        sb4.append(i14 - 26);
                                                                                        string = sb4.toString();
                                                                                    } else {
                                                                                        StringBuilder sb5 = new StringBuilder("eTx");
                                                                                        sb5.append(i14 - 78);
                                                                                        string = sb5.toString();
                                                                                    }
                                                                                    concurrentHashMap3.put(string, strArrSplit3[i3]);
                                                                                    i14++;
                                                                                    i3++;
                                                                                }
                                                                                break;
                                                                            case 492:
                                                                            case 493:
                                                                                i13 = -140;
                                                                                while (i13 < -59) {
                                                                                    concurrentHashMap3.put("rsrp" + i13, strArrSplit3[i3]);
                                                                                    i13++;
                                                                                    i3++;
                                                                                }
                                                                                break;
                                                                            case 494:
                                                                            case 495:
                                                                                i16 = -20;
                                                                                while (i16 < 31) {
                                                                                    concurrentHashMap3.put("snr" + i16, strArrSplit3[i3]);
                                                                                    i16++;
                                                                                    i3++;
                                                                                }
                                                                                break;
                                                                            case 496:
                                                                            case 497:
                                                                                i15 = 0;
                                                                                while (i15 < 12) {
                                                                                    concurrentHashMap3.put("ant" + i15, strArrSplit3[i3]);
                                                                                    i15++;
                                                                                    i3++;
                                                                                }
                                                                                break;
                                                                            case 498:
                                                                                break;
                                                                            case 499:
                                                                                processBlerCount(i2, i22, concurrentHashMap3, strArrSplit3);
                                                                                break;
                                                                            default:
                                                                                Utils.logW("ModemHydraStat", "invalid DMFAPP ID " + i);
                                                                                break;
                                                                        }
                                                                        break;
                                                                }
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                    } else {
                                        int i43 = 0;
                                        while (i43 < 8) {
                                            concurrentHashMap3.put("breakOutCause_" + i43, strArrSplit3[i3]);
                                            i43++;
                                            i3++;
                                        }
                                    }
                                    i8 = -25;
                                    while (i8 < 27) {
                                        concurrentHashMap3.put("tx" + i8, strArrSplit3[i3]);
                                        i8++;
                                        i3++;
                                    }
                                } else {
                                    str2 = strArrSplit3[42];
                                    if (Integer.parseInt(str2.trim()) > 0) {
                                        i12 = 0;
                                        while (i12 < 41) {
                                            concurrentHashMap3.put("id" + i12, strArrSplit3[i3].trim());
                                            i12++;
                                            i3++;
                                        }
                                        concurrentHashMap3.put("devNum", str2);
                                        str3 = SystemProperties.get(ModemSarUtils.PROPERTY_HWID_VER, "");
                                        if (!TextUtils.isEmpty(str3)) {
                                            concurrentHashMap3.put("hwversion", str3);
                                        }
                                    }
                                }
                                c = 5;
                            } else {
                                strArr = strArr2;
                                int i210 = i3 + 1;
                                concurrentHashMap3.put("cameraTotalDuration", strArrSplit3[i3]);
                                i4 = i3 + 2;
                                concurrentHashMap3.put("cameraOpenCount", strArrSplit3[i210]);
                                if (i2 != 0) {
                                    if (i2 == 1) {
                                        if (i22 > 0) {
                                            c = 5;
                                            if (i22 > 5) {
                                                concurrentHashMap3.put("band", this.bandNames[i22 + 9]);
                                            }
                                        }
                                        Utils.logW("ModemHydraStat", "Invalid nr band index: " + i22);
                                        return;
                                    }
                                    i5 = 0;
                                    while (i5 < 12) {
                                        concurrentHashMap3.put("ant0_" + i5, strArrSplit3[i4]);
                                        i5++;
                                        i4++;
                                    }
                                    i7 = 0;
                                    while (i7 < i6) {
                                        concurrentHashMap3.put("ant1_" + i7, strArrSplit3[i4]);
                                        i7++;
                                        i4++;
                                    }
                                } else {
                                    if (i22 > 0 || i22 > 10) {
                                        Utils.logW("ModemHydraStat", "Invalid lte band index: " + i22);
                                        return;
                                    }
                                    concurrentHashMap3.put("band", this.bandNames[i22 - 1]);
                                }
                                c = 5;
                                i5 = 0;
                                while (i5 < 12) {
                                    concurrentHashMap3.put("ant0_" + i5, strArrSplit3[i4]);
                                    i5++;
                                    i4++;
                                }
                                i7 = 0;
                                while (i7 < i6) {
                                    concurrentHashMap3.put("ant1_" + i7, strArrSplit3[i4]);
                                    i7++;
                                    i4++;
                                }
                            }
                            arrayList.add(concurrentHashMap3);
                        } else {
                            strArr = strArr2;
                        }
                        c = 5;
                        arrayList.add(concurrentHashMap3);
                    }
                    i22++;
                    strArr2 = strArr;
                    i18 = 1;
                }
            }
        }

        private void processSceneStatus(int i, int i2, int i3, ConcurrentHashMap concurrentHashMap) {
            if (ArrayUtils.contains(this.mScenceDmfId, i)) {
                switch (i) {
                    case 491:
                    case 493:
                    case 495:
                    case 497:
                        if (i3 <= 5) {
                            concurrentHashMap.put("sceneStatus", "SingleHandApp");
                            break;
                        } else if (i3 > 5 && i3 <= 10) {
                            concurrentHashMap.put("sceneStatus", "DobuHandApp");
                            break;
                        }
                        break;
                    case 492:
                    case 494:
                    case 496:
                    case 498:
                        if (i2 == 0) {
                            concurrentHashMap.put("sceneStatus", "SingleHandApp");
                        } else if (i2 == 1) {
                            concurrentHashMap.put("sceneStatus", "DobuHandApp");
                        }
                        break;
                }
            }
        }

        private void processBlerCount(int i, int i2, ConcurrentHashMap concurrentHashMap, String[] strArr) {
            if (i == 0) {
                processFirstBlerCount(i2, concurrentHashMap, strArr);
            } else if (i == 1) {
                processSecondBlerCount(i2, concurrentHashMap, strArr);
            }
        }

        private void processFirstBlerCount(int i, ConcurrentHashMap concurrentHashMap, String[] strArr) {
            if (i > 0) {
                String[] strArr2 = this.bandNames;
                if (i <= strArr2.length) {
                    concurrentHashMap.put("band", strArr2[i - 1]);
                    setStagesToMap(concurrentHashMap, strArr, 2);
                    return;
                }
            }
            Utils.logW("ModemHydraStat", "Invalid band index: " + i);
        }

        private void processSecondBlerCount(int i, ConcurrentHashMap concurrentHashMap, String[] strArr) {
            if (i >= 0) {
                int i2 = i + 9;
                String[] strArr2 = this.bandNames;
                if (i2 < strArr2.length) {
                    concurrentHashMap.put("band", strArr2[i2]);
                    setStagesToMap(concurrentHashMap, strArr, 2);
                    return;
                }
            }
            Utils.logW("ModemHydraStat", "Invalid nr band index: " + i);
        }

        private void setStagesToMap(ConcurrentHashMap concurrentHashMap, String[] strArr, int i) {
            if (strArr.length < i + 10) {
                Utils.logW("ModemHydraStat", "Insufficient lineData length: " + strArr.length);
            } else {
                for (int i2 = 0; i2 < 10; i2++) {
                    concurrentHashMap.put("stage" + i2, strArr[i + i2]);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long readSysNode(String str) {
        long j = -1;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str));
            try {
                String strTrim = new String(fileInputStream.readAllBytes()).trim();
                Utils.logD("ModemHydraStat", str + " val:" + strTrim);
                j = Long.parseLong(strTrim);
                fileInputStream.close();
                return j;
            } catch (Throwable th) {
                try {
                    fileInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return j;
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void updateScreenState(boolean z) {
        ModemHydraStat hydraInstance = ModemPowerStats.getHydraInstance();
        if (hydraInstance != null) {
            hydraInstance.mIsScreenOn = z;
            ModemPowerStats.check();
        }
    }

    public static void updateAppScene(String str, boolean z) {
        if (ModemPowerStats.sPwrStatsIns != null) {
            ModemPowerStats.sPwrStatsIns.updateAppSceneInner(str, z);
        }
    }

    public static boolean isModemPowerStatsSupported() {
        return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_is_modem_power_stats_supported);
    }

    class DmfItem {
        int mDmfId;
        String mName;
        Map mVersionParamMap;

        public DmfItem(int i, String str, Map map) {
            this.mDmfId = i;
            this.mName = str;
            this.mVersionParamMap = map;
        }
    }

    class ModemPowerStats {
        private static ModemPowerStats sPwrStatsIns;
        private boolean VDBG;
        private long mCurrentSum;
        private static final String[] mPowerStatsScenes = {"social", "streaming", "game", "wechat", "shopping"};
        private static long MD_PULL_GUARD_TIMER = 7200000;
        private static long MD_PULL_INTERVAL = 180000;
        private static int CURRENT_POLL_NUM = 6;
        private static int RSRP_THRESHOLD = -85;
        private static int CURRENT_THRESHOLD = 50000;
        private static int CURRENT_THRESHOLD_GAME = 80000;
        private static int CURRENT_THRESHOLD_ALL = 100000;
        private static int TEMPERATURE_THRESHOLD = 33000;
        private static int MD_PULL_MAX_COUNT = 2;
        private static long CURRENT_POLL_INTERVAL = MirrorDesktopHelper.TIMEOUT_MILLIS;
        private final HashSet mPowerStatsSceneSet = new HashSet();
        private final SimpleDateFormat mDumpTimestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        private final List mDmfItemList = Arrays.asList(new DmfItem(477, "NR_CDRX_PARA", Map.of(4, Arrays.asList("MD_ICD_VER", "timestamp", "slot", "rat", "pci", "arfcn", "pagingCycle", "longCycle", "durTimer", "inactTimer", "shortCycle"))), new DmfItem(478, "LTE_CDRX_PARA", Map.of(4, Arrays.asList("MD_ICD_VER", "timestamp", "slot", "rat", "pci", "freq", "pagingCycle", "longCycle", "duraTimer", "inactTimer", "shortCycle"))), new DmfItem(479, "NR_POWER_PARA", Map.of(4, Arrays.asList("MD_ICD_VER", "timestamp", "slot", "timeInter", "cellGroup", "carrier", "rat", "dlband", "pci", "arfcn", "rsrp", "sinr", "puscHTx", "pucchTx", "srsTx", "mtpl", "dlModRatio0", "dlModRatio1", "dlModRatio2", "dlModRatio3", "dlLayRatio0", "dlLayRatio1", "dlLayRatio2", "dlLayRatio3", "dlSymbRadio", "ulSymbRadio", "rankRiRatio0", "rankRiRatio1", "rankRiRatio2", "rankRiRatio3", "conFi", "ddpcReport", "bwpSize", "ulSlotNum0", "ulSlotNum1", "inactTimer", "pathLoss", "dlRbNum", "dlTb0Size", "dlTput", "dlBler", "ulTput", "ulBler", "dci1", "dci0"))), new DmfItem(480, "LTE_POWER_PARA", Map.of(4, Arrays.asList("MD_ICD_VER", "timestamp", "slot", "timeInter", "carrier", "rat", "dlband", "pci", "dlfreq", "rsrp", "sinr", "pucchTx", "dlModRatio0", "dlModRatio1", "dlModRatio2", "dlModRatio3", "dlLayRatio0", "dlLayRatio1", "dlLayRatio2", "dlLayRatio3", "rankRiRatio0", "rankRiRatio1", "rankRiRatio2", "rankRiRatio3", "puschTx", "srsTx", "pdpwr", "confi", "mtpl", "ulslotNum", "inactTimer", "pathLoss", "dlRbNum", "dlTb0Size", "dlTput", "dlBler", "ulTput", "ulBler"))), new DmfItem(481, "NR_SERVCELL_PARA", Map.of(4, Arrays.asList("MD_ICD_VER", "timestamp", "slot", "roaming", "cellgroup", "carrier", "rat", "dlband", "pci", "dlarfcn", "rplmcc", "rplmnc", "tac", "cellId", "scs", "rsrp", "sinr", "pucchtx", "srsTx", "puschTx", "mtpl", "dlLayRatio0", "dlLayRatio1", "dlLayRatio2", "dlLayRatio3", "dlModRatio0", "dlModRatio1", "dlModRatio2", "dlModRatio3", "rankRiRatio0", "rankRiRatio1", "rankRiRatio2", "rankRiRatio3", "conFi", "ddpcReport", "tddPatern10", "tddPatern11", "tddPatern12", "tddPatern13", "tddPatern14", "tddPatern20", "tddPatern21", "tddPatern22", "tddPatern23", "tddPatern24", "pathLoss", "dlTput", "dlBler", "ulTput", "ulBler", "moRrcTimer", "mtRrcTimer"))), new DmfItem(482, "LTE_SERVCELL_PARA", Map.of(4, Arrays.asList("MD_ICD_VER", "timestamp", "slot", "roaming", "carrier", "rat", "dlband", "pci", "dlfreq", "rplmcc", "rplmnc", "tac", "cellId", "rsrp", "sinr", "pucchTx", "udc", "dlLayRatio0", "dlLayRatio1", "dlLayRatio2", "dlLayRatio3", "dlModRatio0", "dlModRatio1", "dlModRatio2", "dlModRatio3", "rankRiRatio0", "rankRiRatio1", "rankRiRatio2", "rankRiRatio3", "srsTx", "puschTx", "conFi", "mtpl", "pdpwr", "pathLoss", "dlTput", "dlBler", "ulTput", "ulBler", "moRrcTimer", "mtRrcTimer", "inactTimer"))), new DmfItem(483, "NR_LONG_POWER_PARA", Map.of(4, Arrays.asList("MD_ICD_VER", "timestamp", "slot", "pciCnt", "ratCnt", "scsCnt", "moSetupCnt", "mtSetupCnt", "rrcConnTime", "cdrxInactTime", "oosSeaTime"))), new DmfItem(484, "LTE_LONG_POWER_PARA", Map.of(4, Arrays.asList("MD_ICD_VER", "timestamp", "slot", "pciCnt", "ratCnt", "ftddCnt", "moSetupCnt", "mtSetupCnt", "rrcConnTime", "cdrxInactTime", "oosSeaTime"))));
        private final Runnable mPullMdPower = new Runnable() { // from class: com.android.phone.statistics.utils.ModemHydraStat.ModemPowerStats.1
            private int count = ModemPowerStats.MD_PULL_MAX_COUNT;

            @Override // java.lang.Runnable
            public void run() {
                try {
                    ModemHydraStat hydraInstance = ModemPowerStats.getHydraInstance();
                    if (hydraInstance == null) {
                        return;
                    }
                    ModemPowerStats.this.recordPowerModemStats();
                    this.count--;
                    Utils.logW("ModemHydraStat", "mPullMdPower:" + this.count);
                    if (this.count > 0) {
                        hydraInstance.mStatHandler.postDelayed(this, ModemPowerStats.MD_PULL_INTERVAL);
                    } else {
                        this.count = ModemPowerStats.MD_PULL_MAX_COUNT;
                        hydraInstance.mStatHandler.removeCallbacks(this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        private final Runnable mCurrentPoll = new Runnable() { // from class: com.android.phone.statistics.utils.ModemHydraStat.ModemPowerStats.2
            @Override // java.lang.Runnable
            public void run() {
                ModemHydraStat hydraInstance = ModemPowerStats.getHydraInstance();
                if (hydraInstance == null) {
                    return;
                }
                ModemPowerStats.this.mCurrentSum += ModemHydraStat.readSysNode("/sys/class/power_supply/battery/current_now");
                ModemPowerStats.this.mCurrentPollCnt--;
                Utils.logD("ModemHydraStat", "mCurrentPoll:" + ModemPowerStats.this.mCurrentPollCnt);
                if (ModemPowerStats.this.mCurrentPollCnt < 1) {
                    ModemPowerStats modemPowerStats = ModemPowerStats.this;
                    if (modemPowerStats.isCurrentHighPerApps(modemPowerStats.mCurrentSum / ((long) ModemPowerStats.CURRENT_POLL_NUM))) {
                        ModemPowerStats.this.mLastGetMdPwrTime = SystemClock.elapsedRealtime();
                        hydraInstance.mStatHandler.removeCallbacks(this);
                        hydraInstance.mStatHandler.post(ModemPowerStats.this.mPullMdPower);
                    }
                    ModemPowerStats.this.mCurrentPollCnt = ModemPowerStats.CURRENT_POLL_NUM;
                    ModemPowerStats.this.mCurrentSum = 0L;
                    return;
                }
                hydraInstance.mStatHandler.postDelayed(this, ModemPowerStats.CURRENT_POLL_INTERVAL);
            }
        };
        private int mCurrentPollCnt = CURRENT_POLL_NUM;
        private long mLastGetMdPwrTime = -MD_PULL_GUARD_TIMER;

        /* JADX INFO: Access modifiers changed from: private */
        public static ModemHydraStat getHydraInstance() {
            if (ModemHydraStatBase.mInstance instanceof ModemHydraStat) {
                return (ModemHydraStat) ModemHydraStatBase.mInstance;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isCurrentHighPerApps(long j) {
            if (j > CURRENT_THRESHOLD_ALL) {
                return true;
            }
            if (this.mPowerStatsSceneSet.contains("game")) {
                return j > 80000;
            }
            return j > ((long) CURRENT_THRESHOLD);
        }

        private ModemPowerStats() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void init() {
            if (sPwrStatsIns == null && ModemHydraStat.isModemPowerStatsSupported()) {
                sPwrStatsIns = new ModemPowerStats();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void check() {
            ModemPowerStats modemPowerStats = sPwrStatsIns;
            if (modemPowerStats != null) {
                modemPowerStats.checkInner();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateAppSceneInner(String str, boolean z) {
            if (ArrayUtils.contains(mPowerStatsScenes, str)) {
                if (!(z && this.mPowerStatsSceneSet.add(str)) && (z || !this.mPowerStatsSceneSet.remove(str))) {
                    return;
                }
                check();
            }
        }

        private void checkInner() {
            ModemHydraStat hydraInstance = getHydraInstance();
            if (hydraInstance == null) {
                return;
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            boolean zIsPollPreconditionSatisfied = isPollPreconditionSatisfied();
            Utils.logD("ModemHydraStat", "checkInnerSatisfied:" + zIsPollPreconditionSatisfied + " current sum:" + this.mCurrentSum);
            if (zIsPollPreconditionSatisfied) {
                if (jElapsedRealtime < this.mLastGetMdPwrTime + MD_PULL_GUARD_TIMER) {
                    return;
                }
                this.mCurrentSum = 0L;
                hydraInstance.mStatHandler.post(this.mCurrentPoll);
                return;
            }
            this.mCurrentSum = 0L;
            this.mLastGetMdPwrTime = -MD_PULL_GUARD_TIMER;
            hydraInstance.mStatHandler.removeCallbacks(this.mCurrentPoll);
            hydraInstance.mStatHandler.removeCallbacks(this.mPullMdPower);
        }

        private boolean isPollPreconditionSatisfied() {
            ServiceState serviceState;
            if (this.VDBG) {
                return true;
            }
            ModemHydraStat hydraInstance = getHydraInstance();
            if (hydraInstance != null && hydraInstance.mStatsEnable && hydraInstance.mIsScreenOn && hydraInstance.mIsMobileDataOn && this.mPowerStatsSceneSet.size() >= 1) {
                Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
                if (activeDataPhone != null && (serviceState = activeDataPhone.getServiceState()) != null) {
                    HashMap map = new HashMap();
                    if (serviceState.getRilDataRadioTechnology() == 14) {
                        ModemHydraStat.getLteSignalInfo(map, activeDataPhone);
                    } else if (serviceState.getRilDataRadioTechnology() == 20) {
                        ModemHydraStat.getNrSignalInfo(map, activeDataPhone);
                    }
                    Object obj = map.get("rsrp");
                    if (obj == null || ((Integer) obj).intValue() > RSRP_THRESHOLD) {
                        return false;
                    }
                }
                if (ModemHydraStat.readSysNode("/sys/class/thermal/thermal_message/board_sensor_temp") > TEMPERATURE_THRESHOLD) {
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordPowerModemStats() {
            String[] strArr;
            ModemPowerStats modemPowerStats = this;
            ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
            String str = foregroundInfo != null ? foregroundInfo.mForegroundPackageName : "NA";
            long sysNode = ModemHydraStat.readSysNode("/sys/class/power_supply/battery/current_now");
            for (DmfItem dmfItem : modemPowerStats.mDmfItemList) {
                String str2 = "AT+EDMFAPP=20100," + dmfItem.mDmfId;
                int i = 1;
                Object[] objArr = new Object[1];
                ArrayList arrayList = new ArrayList();
                int i2 = 1;
                int i3 = 0;
                for (int i4 = 0; i3 < 8 && i2 != 0 && FiveGManagerBase.getInstance().atCmdRunSync(i4, new String[]{str2, "+EDMFAPP"}, objArr) && (strArr = (String[]) objArr[i4]) != null; i4 = 0) {
                    int i5 = strArr.length > 10 ? i : i4;
                    if (strArr.length > i) {
                        int i6 = i;
                        while (i6 < strArr.length) {
                            int i7 = i;
                            String[] strArrSplit = strArr[i6].split(",");
                            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                            Integer.parseInt(strArrSplit[i7]);
                            long j = sysNode;
                            Iterator it = ((List) dmfItem.mVersionParamMap.get(4)).iterator();
                            int i8 = i7;
                            while (it.hasNext()) {
                                String str3 = (String) it.next();
                                Iterator it2 = it;
                                if (str3 != null && str3.equals("timestamp")) {
                                    concurrentHashMap.put(str3, modemPowerStats.humanReadableTime(strArrSplit[i8]).trim());
                                    i8++;
                                } else if (dmfItem.mDmfId == 479 && ("dlSymbRadio".equals(str3) || "ulSymbRadio".equals(str3))) {
                                    for (int i9 = 0; i9 < 14; i9++) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(str3);
                                        int i10 = i8;
                                        sb.append("");
                                        sb.append(i9);
                                        i8 = i10 + 1;
                                        concurrentHashMap.put(sb.toString(), Integer.valueOf(Integer.parseInt(strArrSplit[i10].trim())));
                                    }
                                } else {
                                    concurrentHashMap.put(str3, strArrSplit[i8].trim());
                                    i8++;
                                }
                                modemPowerStats = this;
                                it = it2;
                            }
                            concurrentHashMap.put("pkg", str);
                            concurrentHashMap.put("current", Long.valueOf(j));
                            arrayList.add(concurrentHashMap);
                            i6++;
                            modemPowerStats = this;
                            i = i7;
                            sysNode = j;
                        }
                    }
                    i3++;
                    modemPowerStats = this;
                    i2 = i5;
                    i = i;
                    sysNode = sysNode;
                }
                long j2 = sysNode;
                Utils.logD("ModemHydraStat", dmfItem.mName + " " + arrayList);
                ModemHydraStatBase modemHydraStatBase = ModemHydraStatBase.getInstance();
                if (modemHydraStatBase != null) {
                    modemHydraStatBase.checkAndReport(arrayList, dmfItem.mName);
                }
                modemPowerStats = this;
                sysNode = j2;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDebugParams(Intent intent) {
            this.VDBG = intent.getBooleanExtra("vdbg", false);
            CURRENT_POLL_INTERVAL = intent.getIntExtra("thermal poll_interval", (int) CURRENT_POLL_INTERVAL);
            MD_PULL_GUARD_TIMER = intent.getIntExtra("pull_timer", (int) MD_PULL_GUARD_TIMER);
            CURRENT_POLL_NUM = intent.getIntExtra("poll_num", CURRENT_POLL_NUM);
            RSRP_THRESHOLD = intent.getIntExtra("rsrp_thres", RSRP_THRESHOLD);
            TEMPERATURE_THRESHOLD = intent.getIntExtra("temp_thres", TEMPERATURE_THRESHOLD);
            CURRENT_THRESHOLD = intent.getIntExtra("current_thres", CURRENT_THRESHOLD);
        }

        private String humanReadableTime(String str) {
            return this.mDumpTimestampFormat.format(new Date(Long.parseLong(str.trim() + "000")));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAftersaleCount(String str, int i) {
        try {
            boolean zHas = this.mAftersaleInfo.has(str);
            JSONObject jSONObject = this.mAftersaleInfo;
            if (zHas) {
                this.mAftersaleInfo.put(str, i + jSONObject.getInt(str));
            } else {
                jSONObject.put(str, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAftersaleMap(int i, Map map) {
        try {
            if (i == 458) {
                int iCalcFromMap = calcFromMap(map, new String[]{"moVolteCnt", "mtVolteCnt", "atmpFailVolte"}, new String[]{"moVolteOk", "mtVolteOk"});
                int iCalcFromMap2 = calcFromMap(map, new String[]{"moVonrCnt", "mtVonrCnt", "atmpFailVonr"}, new String[]{"moVonrOk", "mtVonrOk"});
                int iCalcFromMap3 = calcFromMap(map, new String[]{"moVowifiCnt", "mtVoeifiCnt", "atmpFailVowifi"}, new String[]{"moVowifiOk", "mtVowifiOk"});
                int iCalcFromMap4 = calcFromMap(map, new String[]{"moVtCnt", "mtVtCnt"}, new String[]{"moVtOk", "mtVtOk"});
                int iCalcFromMap5 = calcFromMap(map, new String[]{"moCsCnt", "mtCsCnt"}, new String[]{"moCsOk", "mtCsOk"});
                int iCalcFromMap6 = calcFromMap(map, new String[]{"moCsfbCnt", "csfbCnt"}, new String[]{"moCsfbOk", "csfbOk"});
                updateAftersaleCount("CallUserPortrait_volteFail", iCalcFromMap);
                updateAftersaleCount("CallUserPortrait_vonrFail", iCalcFromMap2);
                updateAftersaleCount("CallUserPortrait_vowifiFail", iCalcFromMap3);
                updateAftersaleCount("CallUserPortrait_vtFail", iCalcFromMap4);
                updateAftersaleCount("CallUserPortrait_csFail", iCalcFromMap5);
                updateAftersaleCount("CallUserPortrait_csfbFail", iCalcFromMap6);
                return;
            }
            if (i != 459) {
                return;
            }
            updateAftersaleCount("CallFail_disc" + map.get("discCuz"), 1);
            updateAftersaleCount("CallFail_emm" + map.get("nasEmmCuz") + "_esm" + map.get("nasEsmCuz"), 1);
            StringBuilder sb = new StringBuilder("CallFail_rlf");
            sb.append(map.get("rlfCuz"));
            updateAftersaleCount(sb.toString(), 1);
            updateAftersaleCount("CallFail_ho" + map.get("hoErrCuz"), 1);
            updateAftersaleCount("CallFail_epsfbRe" + map.get("epsfbRedirCuz") + "_Ho" + map.get("epsfbHoCuz"), 1);
            StringBuilder sb2 = new StringBuilder("CallFail_srvcc");
            sb2.append(map.get("srvccCuz"));
            updateAftersaleCount(sb2.toString(), 1);
            updateAftersaleCount("CallFail_csfb" + map.get("csfbCuz"), 1);
            updateAftersaleCount("CallFail_sip" + map.get("sipErrCode") + "_reason" + map.get("reasonTxtFlag"), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int calcFromMap(final Map map, String[] strArr, String[] strArr2) {
        int iSum;
        int iSum2 = 0;
        if (strArr != null) {
            Stream stream = Arrays.stream(strArr);
            Objects.requireNonNull(map);
            iSum = stream.map(new Function() { // from class: com.android.phone.statistics.utils.ModemHydraStat$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return map.get((String) obj);
                }
            }).filter(new Predicate() { // from class: com.android.phone.statistics.utils.ModemHydraStat$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Objects.nonNull(obj);
                }
            }).map(new Function() { // from class: com.android.phone.statistics.utils.ModemHydraStat$$ExternalSyntheticLambda5
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return obj.toString();
                }
            }).mapToInt(new MtkSelfStudyController$$ExternalSyntheticLambda12()).sum();
        } else {
            iSum = 0;
        }
        if (strArr2 != null) {
            Stream stream2 = Arrays.stream(strArr2);
            Objects.requireNonNull(map);
            iSum2 = stream2.map(new Function() { // from class: com.android.phone.statistics.utils.ModemHydraStat$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return map.get((String) obj);
                }
            }).filter(new Predicate() { // from class: com.android.phone.statistics.utils.ModemHydraStat$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Objects.nonNull(obj);
                }
            }).map(new Function() { // from class: com.android.phone.statistics.utils.ModemHydraStat$$ExternalSyntheticLambda5
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return obj.toString();
                }
            }).mapToInt(new MtkSelfStudyController$$ExternalSyntheticLambda12()).sum();
        }
        return iSum - iSum2;
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public JSONObject getAftersaleFromHydra() {
        JSONObject jSONObject = this.mAftersaleInfo;
        this.mAftersaleInfo = new JSONObject();
        return jSONObject;
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        if (location != null) {
            this.mLatitude = location.getLatitude();
            this.mLongitude = location.getLongitude();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needRecordLocation() {
        return MiuiPhoneUtils.isSupportLocationStats() && !MiuiPhoneUtils.isMPDevice();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getLocation() {
        LocationManager locationManager;
        if (MiuiPhoneUtils.isSupportLocationStats() && (locationManager = (LocationManager) this.mContext.getSystemService("location")) != null) {
            String str = "network";
            if (!locationManager.isProviderEnabled("network")) {
                str = "passive";
                if (!locationManager.isProviderEnabled("passive")) {
                    str = "gps";
                    if (!locationManager.isProviderEnabled("gps")) {
                        str = null;
                    }
                }
            }
            Utils.logD("ModemHydraStat", "provider : " + str);
            if (TextUtils.isEmpty(str)) {
                return;
            }
            locationManager.getCurrentLocation(str, null, this.mContext.getMainExecutor(), new Consumer() { // from class: com.android.phone.statistics.utils.ModemHydraStat$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.onLocationChanged((Location) obj);
                }
            });
        }
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void registerSceneStateCallBack(ModemHydraStatBase.SceneStateCallBack sceneStateCallBack) {
        this.callBacks.add(sceneStateCallBack);
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public void unregisterSceneStateCallBack(ModemHydraStatBase.SceneStateCallBack sceneStateCallBack) {
        this.callBacks.remove(sceneStateCallBack);
    }

    @Override // com.android.phone.statistics.utils.ModemHydraStatBase
    public String processUeNoSaInfo(int i) {
        String[] strArr = {"AT+EDMFAPP=20100,451", "+EDMFAPP"};
        Object[] objArr = new Object[1];
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        int i2 = 0;
        while (i2 < 5 && z && FiveGManagerBase.getInstance().atCmdRunSync(i, strArr, objArr)) {
            String[] strArr2 = (String[]) objArr[0];
            boolean z2 = strArr2 != null && strArr2.length > 10;
            if (strArr2 != null && strArr2.length > 0 && !parseUeNoSaResult(strArr2, arrayList)) {
                return "";
            }
            i2++;
            z = z2;
        }
        return arrayList.toString();
    }

    private boolean parseUeNoSaResult(String[] strArr, ArrayList arrayList) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i] == null) {
                Utils.logD("ModemHydraStat", "result[" + i + "] is null, skip");
            } else {
                Utils.logD("ModemHydraStat", "No FiveG:" + strArr[i]);
                String[] strArrSplit = strArr[i].split(",");
                if (strArrSplit.length < 3) {
                    Utils.logD("ModemHydraStat", "invalid lineData length: " + strArrSplit.length);
                } else {
                    if (UE_NO_SA_KEY_NAMES.length > strArrSplit.length - 2) {
                        Utils.logD("ModemHydraStat", "mismatched response");
                        return false;
                    }
                    HashMap map = new HashMap();
                    int i2 = 0;
                    while (true) {
                        String[] strArr2 = UE_NO_SA_KEY_NAMES;
                        if (i2 >= strArr2.length) {
                            break;
                        }
                        map.put(strArr2[i2], strArrSplit[i2 + 2]);
                        i2++;
                    }
                    arrayList.add(map);
                }
            }
        }
        return true;
    }
}
