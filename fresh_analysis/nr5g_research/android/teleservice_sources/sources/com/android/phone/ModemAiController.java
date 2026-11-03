package com.android.phone;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.MiuiMultiWindowAdapter;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.util.ArrayUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.NetworkSceneEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.INetworkModule;
import com.android.phone.statistics.utils.ModemHydraStatBase;
import com.android.phone.utils.CountableHandler;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiAppStateMonitor;
import com.android.phone.utils.VideoPreloadScene;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class ModemAiController implements MiuiAppStateMonitor.TopAppObserver, TelephonyStatAdapter.ICallBack {
    private static MiuiSettings.SettingsCloudData.CloudData sCloudData;
    private static ModemAiController sInstance;
    private ContentResolver mContentResolver;
    private final Context mContext;
    private boolean mFeatureEnabled;
    private ContentObserver mGameStatusObserver;
    private ModemAiHandler mHandler;
    private boolean mIsInSubway;
    private boolean mIsMobileDataConnected;
    private boolean mIsScreenOn;
    private boolean mIsSwitchOn;
    private int mLastDelay;
    private boolean mLastIsInSubway;
    private int mModelFlag;
    private Object mModemAiMgr;
    private BroadcastReceiver mReceiver;
    private int mSceneFlag;
    private boolean mSignalEnabled;
    private String mTopPackage;
    private static final List WHITE_APP_LIST = HyperOSCustFeatureResolve.getStringArray("config_modem_ai_tiktok_app_packages");
    private static final List GAME_AI_WHITELIST_PACKAGES = MiuiPhoneUtils.getFeatureConfig("config_modem_ai_game_app_packages");
    private static final List STATIC_GAME_AI_NO_DELAY_WHITELIST_PACKAGES = MiuiPhoneUtils.getFeatureConfig("config_modem_ai_no_delay_game_packages");
    private static final Set ALLOWED_RAT_TYPES = Set.of(20, 30, 14, 19);
    private final String[] MODEM_AI_METHODS = {"getInstance", "runSignalModel", "stopSignalModel", "runGameModel", "setGameDelay", "stopGameModel", "getSignalModelOneDayIndicator", "getGameModelOneDayIndicator", "getGameModelOneDayParam", "setGameRat"};
    private final ConcurrentHashMap mMethods = new ConcurrentHashMap();
    private boolean mIsNetworkMatched = true;
    private boolean mIsNetworkMatchedForGame = true;
    private String mTopGamePackage = "";
    private boolean isInGame = false;
    private boolean isGameModelRun = false;
    private int mCurRat = 0;
    private final BaseEvent.IEventListener mDelayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.ModemAiController.2
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public synchronized void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof NetworkSceneEvent.GameDelayEvent) {
                ModemAiController.this.mHandler.sendMessage(ModemAiController.this.mHandler.obtainMessage(108, (NetworkSceneEvent.GameDelayEvent) baseEvent));
            }
        }
    };
    private BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.ModemAiController$$ExternalSyntheticLambda1
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public final void onEvent(BaseEvent baseEvent) {
            this.f$0.lambda$new$2(baseEvent);
        }
    };
    private ModemHydraStatBase.SceneStateCallBack mSceneStateCallBack = new ModemHydraStatBase.SceneStateCallBack() { // from class: com.android.phone.ModemAiController.4
        @Override // com.android.phone.statistics.utils.ModemHydraStatBase.SceneStateCallBack
        public void onSceneStateChanged(int i, boolean z, String str) {
            ModemAiController.log("mSceneStateCallBack sceneType = " + i + " , isEnter = " + z + " , sceneName = " + str);
            if (i != 10000001) {
                return;
            }
            ModemAiController.this.mIsInSubway = z;
            if (ModemAiController.this.mLastIsInSubway != ModemAiController.this.mIsInSubway) {
                ModemAiController modemAiController = ModemAiController.this;
                modemAiController.mLastIsInSubway = modemAiController.mIsInSubway;
                ModemAiController.this.onSceneChanged();
            }
        }
    };

    class ModemAiHandler extends CountableHandler {
        ModemAiHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.phone.utils.CountableHandler, android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            boolean z = false;
            switch (message.what) {
                case 100:
                    ModemAiController.this.mIsScreenOn = message.arg1 == 1;
                    ModemAiController.logV("screen ".concat(ModemAiController.this.mIsScreenOn ? "ON" : "OFF"));
                    ModemAiController.this.onSceneChanged();
                    break;
                case 102:
                    boolean z2 = ModemAiController.this.mFeatureEnabled;
                    ModemAiController modemAiController = ModemAiController.this;
                    modemAiController.mIsSwitchOn = Settings.Secure.getInt(modemAiController.mContext.getContentResolver(), "gamebooster_cellular_network", 1) == 1;
                    ModemAiController.log("setting changed to:" + ModemAiController.this.mIsSwitchOn);
                    ModemAiController modemAiController2 = ModemAiController.this;
                    if (modemAiController2.mFeatureEnabled && ModemAiController.this.mIsSwitchOn) {
                        z = true;
                    }
                    modemAiController2.mFeatureEnabled = z;
                    if (z2 != ModemAiController.this.mFeatureEnabled) {
                        ModemAiController modemAiController3 = ModemAiController.this;
                        modemAiController3.regDeviceState(modemAiController3.mFeatureEnabled);
                        ModemAiController.this.onSceneChanged();
                    }
                    break;
                case 103:
                    if (ModemHydraStatBase.getInstance() != null) {
                        ModemAiController.log("registerSceneStateCallBack init");
                        ModemHydraStatBase.getInstance().registerSceneStateCallBack(ModemAiController.this.mSceneStateCallBack);
                    }
                    break;
                case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                    ModemAiController.log("EVENT_ENTER_GAME_5S mTopPackage: " + ModemAiController.this.mTopPackage + " mTopGamePackage: " + ModemAiController.this.mTopGamePackage);
                    ModemAiController modemAiController4 = ModemAiController.this;
                    modemAiController4.mTopGamePackage = modemAiController4.mTopPackage;
                    if (ModemAiController.STATIC_GAME_AI_NO_DELAY_WHITELIST_PACKAGES.contains(ModemAiController.this.mTopGamePackage)) {
                        ModemAiController.log("mTopGamePackage: " + ModemAiController.this.mTopGamePackage + " isInGame: " + ModemAiController.this.isInGame);
                        if (ModemAiController.this.isInGame && (ModemAiController.this.mSceneFlag & 2) == 0) {
                            ModemAiController.this.mSceneFlag |= 2;
                            ModemAiController.log("enter-mSceneFlag: " + ModemAiController.this.mSceneFlag + " MASK_GAME: 2");
                            ModemAiController.this.onSceneChanged();
                            break;
                        }
                    }
                    break;
                case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                    if (ModemAiController.this.mHandler.hasMessages(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT)) {
                        ModemAiController.this.mHandler.removeMessages(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT);
                    }
                    ModemAiController.this.mHandler.sendMessageDelayed(ModemAiController.this.mHandler.obtainMessage(107), 30000L);
                    break;
                case 106:
                    if (ModemAiController.this.mHandler.hasMessages(107)) {
                        ModemAiController.this.mHandler.removeMessages(107);
                    }
                    ModemAiController.this.mHandler.removeMessages(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT);
                    if (ModemAiController.this.mIsMobileDataConnected) {
                        ModemAiController.this.mHandler.sendMessageDelayed(ModemAiController.this.mHandler.obtainMessage(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT), MirrorDesktopHelper.TIMEOUT_MILLIS);
                    }
                    break;
                case 107:
                    ModemAiController.log("exit mSceneFlag: " + ModemAiController.this.mSceneFlag + " MASK_GAME: 2 mTopGamePackage: " + ModemAiController.this.mTopGamePackage);
                    if (ModemAiController.STATIC_GAME_AI_NO_DELAY_WHITELIST_PACKAGES.contains(ModemAiController.this.mTopGamePackage) && !ModemAiController.this.isInGame && (ModemAiController.this.mSceneFlag & 2) != 0) {
                        ModemAiController.this.mSceneFlag &= -3;
                        ModemAiController.this.onSceneChanged();
                    }
                    ModemAiController.this.mTopGamePackage = "";
                    ModemAiController.log("EVENT_EXIT_GAME_30S: " + ModemAiController.this.mTopGamePackage);
                    break;
                case 108:
                    NetworkSceneEvent.GameDelayEvent gameDelayEvent = (NetworkSceneEvent.GameDelayEvent) message.obj;
                    Bundle bundle = gameDelayEvent.getBundle();
                    if (bundle != null) {
                        if ("game_delay_level_changed".equals(gameDelayEvent.getSubType())) {
                            int i = bundle.getInt("game_delay_level", 0);
                            if (ModemAiController.this.mLastDelay != i && ModemAiController.GAME_AI_WHITELIST_PACKAGES.contains(ModemAiController.this.mTopPackage)) {
                                ModemAiController.this.mLastDelay = i;
                                ModemAiController.this.invokeMethod("setGameDelay", Integer.valueOf(i));
                                break;
                            }
                        } else if ("game_battle_status_changed".equals(gameDelayEvent.getSubType())) {
                            boolean z3 = bundle.getBoolean("game_delay_battle", false);
                            ModemAiController.log("battle:" + z3);
                            if (z3 && (ModemAiController.this.mSceneFlag & 2) == 0) {
                                ModemAiController.this.mSceneFlag |= 2;
                                ModemAiController.this.onSceneChanged();
                                break;
                            } else if (!z3 && (ModemAiController.this.mSceneFlag & 2) != 0) {
                                ModemAiController.this.mSceneFlag &= -3;
                                ModemAiController.this.onSceneChanged();
                                break;
                            }
                        }
                    }
                    break;
            }
        }
    }

    private static void initConfigFromCloud() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(PhoneGlobals.getInstance().getContentResolver(), "ModemAiCtrl");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("TelephonyCloudAiFeature data=" + cloudData.toString());
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2)) {
                        sCloudData = cloudData;
                        return;
                    }
                }
                return;
            }
            log("TelephonyModemFeature cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void observeGameStatus(boolean z) {
        ContentObserver contentObserver;
        if (z && this.mGameStatusObserver == null) {
            Uri uriFor = Settings.Secure.getUriFor("gb_boosting");
            ContentObserver contentObserver2 = new ContentObserver(this.mHandler) { // from class: com.android.phone.ModemAiController.1
                @Override // android.database.ContentObserver
                public void onChange(boolean z2) {
                    ModemAiController modemAiController = ModemAiController.this;
                    modemAiController.isInGame = Settings.Secure.getInt(modemAiController.mContentResolver, "gb_boosting", 0) == 1;
                    ModemAiController.log("game status mIsInGame = " + ModemAiController.this.isInGame);
                    boolean z3 = ModemAiController.this.isInGame;
                    ModemAiController modemAiController2 = ModemAiController.this;
                    if (!z3) {
                        modemAiController2.mHandler.sendMessage(ModemAiController.this.mHandler.obtainMessage(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT));
                        if (ModemAiController.this.mHandler.hasMessages(106)) {
                            ModemAiController.this.mHandler.removeMessages(106);
                            return;
                        }
                        return;
                    }
                    modemAiController2.mHandler.sendMessage(ModemAiController.this.mHandler.obtainMessage(106));
                    if (ModemAiController.this.mHandler.hasMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT)) {
                        ModemAiController.this.mHandler.removeMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
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

    private ModemAiController() {
        MiuiSettings.SettingsCloudData.CloudData cloudData;
        HandlerThread handlerThread = new HandlerThread("ModemAiController", 10);
        handlerThread.start();
        this.mHandler = new ModemAiHandler(handlerThread.getLooper());
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        initConfigFromCloud();
        boolean z = Settings.Secure.getInt(phoneGlobals.getContentResolver(), "gamebooster_cellular_network", 1) == 1;
        this.mIsSwitchOn = z;
        this.mFeatureEnabled = z && ((cloudData = sCloudData) == null || cloudData.getBoolean("pref_gameai_enable", true));
        MiuiSettings.SettingsCloudData.CloudData cloudData2 = sCloudData;
        this.mSignalEnabled = cloudData2 == null || cloudData2.getBoolean("pref_signalai_enable", true);
        new SettingsObserver(phoneGlobals, this.mHandler).observe(Settings.Secure.getUriFor("gamebooster_cellular_network"), 102);
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.ModemAiController$$ExternalSyntheticLambda2
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$1(baseEvent);
            }
        });
        this.mContentResolver = phoneGlobals.getContentResolver();
        if (!this.mFeatureEnabled) {
            log("telephony modem AI not enabled");
            observeGameStatus(false);
            return;
        }
        log("constructor");
        observeGameStatus(true);
        loadJar();
        if (this.mModemAiMgr == null) {
            log("Modem AI not supported");
            return;
        }
        regDeviceState(true);
        if (this.mSignalEnabled) {
            if (ModemHydraStatBase.getInstance() != null) {
                log("registerSceneStateCallBack");
                ModemHydraStatBase.getInstance().registerSceneStateCallBack(this.mSceneStateCallBack);
            } else {
                this.mHandler.sendEmptyMessageDelayed(103, 15000L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            this.mHandler.post(new Runnable() { // from class: com.android.phone.ModemAiController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void regDeviceState(boolean z) {
        MiuiAppStateMonitor miuiAppStateMonitorInit = MiuiAppStateMonitor.init();
        BroadcastReceiver broadcastReceiver = this.mReceiver;
        if (broadcastReceiver != null || !z) {
            if (broadcastReceiver == null || z) {
                return;
            }
            log("unregDeviceState");
            this.mContext.unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
            miuiAppStateMonitorInit.unregisterAppObserver(this);
            TelephonyStatAdapter.unregisterCallBack(this);
            EventHubManager.getInstance().unregisterListener(this.mDisplayListener);
            if (!this.mSignalEnabled && ModemHydraStatBase.getInstance() != null) {
                log("unregisterSceneStateCallBack");
                ModemHydraStatBase.getInstance().unregisterSceneStateCallBack(this.mSceneStateCallBack);
            }
            if (this.mFeatureEnabled) {
                return;
            }
            observeGameStatus(false);
            return;
        }
        log("regDeviceState");
        IntentFilter intentFilter = new IntentFilter("com.android.phone.intent.action.CHANGE_LEVEL");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.xiaomi.modemai.signal.FORECAST_RES");
        intentFilter.addAction("com.xiaomi.modemai.game.FORECAST_RES");
        AnonymousClass3 anonymousClass3 = new AnonymousClass3();
        this.mReceiver = anonymousClass3;
        this.mContext.registerReceiver(anonymousClass3, intentFilter, 2);
        miuiAppStateMonitorInit.registerAppObserver(this);
        TelephonyStatAdapter.registerCallBack(this);
        if (this.mSignalEnabled && ModemHydraStatBase.getInstance() != null) {
            log("registerSceneStateCallBack");
            ModemHydraStatBase.getInstance().registerSceneStateCallBack(this.mSceneStateCallBack);
        }
        if (this.mFeatureEnabled) {
            observeGameStatus(true);
        }
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
    }

    /* JADX INFO: renamed from: com.android.phone.ModemAiController$3, reason: invalid class name */
    class AnonymousClass3 extends BroadcastReceiver {
        AnonymousClass3() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                ModemAiController.this.mHandler.post(new Runnable() { // from class: com.android.phone.ModemAiController$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0();
                    }
                });
                return;
            }
            if ("com.android.phone.intent.action.CHANGE_LEVEL".equals(intent.getAction())) {
                ModemAiController.this.mHandler.post(new Runnable() { // from class: com.android.phone.ModemAiController$3$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$1(intent);
                    }
                });
                return;
            }
            if ("com.xiaomi.modemai.signal.FORECAST_RES".equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("FORECAST_RES");
                ModemAiController.logV("predict ret:" + stringExtra);
                if (TextUtils.equals("WEAK", stringExtra)) {
                    ModemAiController.this.mHandler.post(new Runnable() { // from class: com.android.phone.ModemAiController$3$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).videoAppsPolicyNotify(1, -1, -1, VideoPreloadScene.SUBWAY_WEAK_SIGNAL.getValue());
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            boolean zIsMobileDataConnected = MiuiPhoneUtils.isMobileDataConnected();
            if (ModemAiController.this.mIsMobileDataConnected != zIsMobileDataConnected) {
                ModemAiController.this.mIsMobileDataConnected = zIsMobileDataConnected;
                ModemAiController.this.onSceneChanged();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$1(Intent intent) {
            long longExtra = intent.getLongExtra("Level_UL", 1L);
            if ((intent.getLongExtra("Level_DL", 1L) != 1 || longExtra != 1) && !ModemAiController.this.isGameAIExempted()) {
                EventHubManager.getInstance().registerListener("network", new String[]{"game_delay_level_changed", "game_battle_status_changed"}, ModemAiController.this.mDelayListener);
            } else {
                EventHubManager.getInstance().unregisterListener(ModemAiController.this.mDelayListener);
            }
        }
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
    public /* synthetic */ void lambda$new$2(BaseEvent baseEvent) {
        if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
            DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
            if (displayEvent.isDefaultDisplay()) {
                boolean zIsScreenOn = displayEvent.isScreenOn();
                this.mHandler.removeMessageWithCount(100);
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(100, zIsScreenOn ? 1 : 0, 0), MirrorDesktopHelper.TIMEOUT_MILLIS);
            }
        }
    }

    private static boolean isModemAiSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_is_modem_ai_supported);
    }

    public static void make() {
        if (sInstance == null && isModemAiSupported()) {
            synchronized (ModemAiController.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new ModemAiController();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Rlog.d("ModemAiController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logV(String str) {
        if (PhoneDebug.VDBG) {
            Rlog.d("ModemAiController", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:36:0x00d3  */
    /* JADX WARN: Code duplicated, block: B:39:0x00d9  */
    /* JADX WARN: Code duplicated, block: B:41:0x00df  */
    public void onSceneChanged() {
        int i;
        logV("onSceneChanged:" + this.mSceneFlag + " m:" + this.mModelFlag);
        boolean z = this.mFeatureEnabled && this.mIsScreenOn && this.mIsMobileDataConnected && this.mIsNetworkMatchedForGame;
        boolean z2 = this.mSignalEnabled && this.mIsScreenOn && this.mIsMobileDataConnected && this.mIsNetworkMatched && this.mIsInSubway;
        INetworkModule iNetworkModule = (INetworkModule) ModuleDelegate.get(INetworkModule.class);
        log("MASK_GAME: 2 mFeatureEnabled: " + this.mFeatureEnabled + "mSignalEnabled: " + this.mSignalEnabled + " mIsNetworkMatchedForGame: " + this.mIsNetworkMatchedForGame + " mIsScreenOn: " + this.mIsScreenOn + " mIsMobileDataConnected: " + this.mIsMobileDataConnected + "mIsInSubway: " + this.mIsInSubway);
        int i2 = this.mSceneFlag;
        int i3 = 2;
        if ((i2 & 2) != 0) {
            int i4 = this.mModelFlag;
            if ((i4 & 2) == 0 && z) {
                this.mModelFlag = i4 | 2;
                if (iNetworkModule.isInHomeScence()) {
                    i3 = 1;
                } else if (iNetworkModule.isInCompanyScence()) {
                    i3 = 0;
                }
                invokeMethod("runGameModel", this.mTopPackage, Integer.valueOf(i3), Integer.valueOf(this.mCurRat));
                this.isGameModelRun = true;
            } else if ((i2 & 2) != 0 || !z) {
                i = this.mModelFlag;
                if ((i & 2) != 0) {
                    this.mModelFlag = i & (-3);
                    invokeMethod("stopGameModel", new Object[0]);
                    this.isGameModelRun = false;
                }
            }
        } else if ((i2 & 2) != 0) {
            i = this.mModelFlag;
            if ((i & 2) != 0) {
                this.mModelFlag = i & (-3);
                invokeMethod("stopGameModel", new Object[0]);
                this.isGameModelRun = false;
            }
        } else {
            i = this.mModelFlag;
            if ((i & 2) != 0) {
                this.mModelFlag = i & (-3);
                invokeMethod("stopGameModel", new Object[0]);
                this.isGameModelRun = false;
            }
        }
        int i5 = this.mSceneFlag;
        if ((i5 & 1) != 0) {
            int i6 = this.mModelFlag;
            if ((i6 & 1) == 0 && z2) {
                this.mModelFlag = i6 | 1;
                invokeMethod("runSignalModel", new Object[0]);
                return;
            }
        }
        if ((i5 & 1) == 0 || !z2) {
            int i7 = this.mModelFlag;
            if ((i7 & 1) != 0) {
                this.mModelFlag = i7 & (-2);
                invokeMethod("stopSignalModel", new Object[0]);
            }
        }
    }

    @Override // com.android.phone.utils.MiuiAppStateMonitor.TopAppObserver
    public void onTopAppChanged(final List list, final int i) {
        this.mHandler.post(new Runnable() { // from class: com.android.phone.ModemAiController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onTopAppChanged$4(list, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTopAppChanged$4(List list, int i) {
        ArrayList arrayList = (ArrayList) list;
        if (arrayList != null) {
            int i2 = this.mSceneFlag;
            boolean zAnyMatch = arrayList.stream().anyMatch(new Predicate() { // from class: com.android.phone.ModemAiController$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ModemAiController.$r8$lambda$AF44u6tTvClZoeMCDJ00XP1ntYw((ComponentName) obj);
                }
            });
            int i3 = this.mSceneFlag;
            if (zAnyMatch) {
                this.mSceneFlag = i3 | 1;
            } else {
                this.mSceneFlag = i3 & (-2);
            }
            if (i2 != this.mSceneFlag) {
                onSceneChanged();
            }
        }
        if (i == 0) {
            this.mTopPackage = MiuiPhoneUtils.getTopAppName();
        } else {
            this.mTopPackage = "";
        }
        logV(this.mSceneFlag + " onTopAppChanged:" + this.mTopPackage);
        if ((this.mSceneFlag & 2) == 0 || MiuiMultiWindowAdapter.isInTopGameList(this.mTopPackage)) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("game_delay_battle", false);
        EventHubManager.getInstance().postEvent(new NetworkSceneEvent.GameDelayEvent("game_battle_status_changed", bundle));
    }

    public static /* synthetic */ boolean $r8$lambda$AF44u6tTvClZoeMCDJ00XP1ntYw(ComponentName componentName) {
        return componentName != null && ArrayUtils.contains(WHITE_APP_LIST, componentName.getPackageName());
    }

    private void loadJar() {
        if (new File("/system_ext/framework/xiaomi-telephony-common.jar").exists()) {
            try {
                Method[] declaredMethods = new PathClassLoader("/system_ext/framework/xiaomi-telephony-common.jar", getClass().getClassLoader()).loadClass("com.xiaomi.internal.telephony.ai.ModemAiManager").getDeclaredMethods();
                logV("methods:" + Arrays.toString(declaredMethods));
                for (String str : this.MODEM_AI_METHODS) {
                    for (Method method : declaredMethods) {
                        if (str.equals(method.getName())) {
                            this.mMethods.put(str, method);
                        }
                    }
                }
                this.mModemAiMgr = ((Method) this.mMethods.get("getInstance")).invoke(null, this.mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeMethod(String str, Object... objArr) {
        Object obj;
        try {
            logV("invoke " + str);
            Method method = (Method) this.mMethods.get(str);
            if (method != null && (obj = this.mModemAiMgr) != null) {
                method.invoke(obj, objArr);
                return;
            }
            Rlog.w("ModemAiController", "mgr:" + this.mModemAiMgr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onServiceStateChanged(final int i, final ServiceState serviceState) {
        this.mHandler.post(new Runnable() { // from class: com.android.phone.ModemAiController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onServiceStateChanged$5(i, serviceState);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onServiceStateChanged$5(int i, ServiceState serviceState) {
        if (i == MiuiPhoneUtils.getActiveDataPhoneId()) {
            boolean z = this.mIsNetworkMatched;
            if (serviceState == null || (serviceState.getRilDataRadioTechnology() != 14 && serviceState.getRilDataRadioTechnology() != 19 && serviceState.getRilDataRadioTechnology() != 20 && serviceState.getRilDataRadioTechnology() != 30)) {
                this.mIsNetworkMatched = false;
            } else {
                this.mIsNetworkMatched = true;
            }
            if (z != this.mIsNetworkMatched) {
                logV("ServiceState changed to " + serviceState);
                onSceneChanged();
            }
            boolean z2 = this.mIsNetworkMatchedForGame;
            if (serviceState == null) {
                logV("mIsNetworkMatchedForGame = false (ss == null)");
                this.mIsNetworkMatchedForGame = false;
                this.mCurRat = 0;
            } else if (serviceState.getDataRegState() == 1) {
                logV("mIsNetworkMatchedForGame = true (STATE_OUT_OF_SERVICE)");
                this.mIsNetworkMatchedForGame = true;
                this.mCurRat = 0;
                if (this.isGameModelRun) {
                    invokeMethod("setGameRat", 0);
                }
            } else {
                int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
                if (ALLOWED_RAT_TYPES.contains(Integer.valueOf(rilDataRadioTechnology))) {
                    this.mIsNetworkMatchedForGame = true;
                    if (rilDataRadioTechnology == 20 || rilDataRadioTechnology == 30) {
                        this.mCurRat = 2;
                    } else {
                        this.mCurRat = 1;
                    }
                    if (this.isGameModelRun) {
                        invokeMethod("setGameRat", Integer.valueOf(this.mCurRat));
                    }
                } else {
                    logV("mIsNetworkMatchedForGame = false (unknown RAT)");
                    this.mIsNetworkMatchedForGame = false;
                    this.mCurRat = 0;
                }
            }
            if (z2 != this.mIsNetworkMatchedForGame) {
                onSceneChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: updateCloudFeatureStatus, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "ModemAiCtrl");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2)) {
                        if (cloudData.equals(sCloudData)) {
                            log("no change in cloud data");
                            return;
                        }
                        if (cloudData.json().has("pref_gameai_enable")) {
                            boolean z = cloudData.getBoolean("pref_gameai_enable", true);
                            log("updateCloudFeatureStatus enable=" + z);
                            MiuiSettings.SettingsCloudData.CloudData cloudData2 = sCloudData;
                            if (cloudData2 == null || z != cloudData2.getBoolean("pref_gameai_enable", true)) {
                                boolean z2 = z && this.mIsSwitchOn;
                                this.mFeatureEnabled = z2;
                                regDeviceState(z2);
                                onSceneChanged();
                            }
                        }
                        if (cloudData.json().has("pref_signalai_enable")) {
                            boolean z3 = cloudData.getBoolean("pref_signalai_enable", true);
                            log("updateCloudFeatureStatus cloudSignalEnable=" + z3);
                            MiuiSettings.SettingsCloudData.CloudData cloudData3 = sCloudData;
                            if (cloudData3 == null || z3 != cloudData3.getBoolean("pref_signalai_enable", true)) {
                                this.mSignalEnabled = z3;
                                regDeviceState(z3);
                                onSceneChanged();
                            }
                        }
                        sCloudData = cloudData;
                    }
                }
                return;
            }
            log("updateCloudFeatureStatus cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGameAIExempted() {
        return HyperOSCustFeatureResolve.getBoolean("config_is_game_AI_exempted", false);
    }

    private void reportModemAiStatsInner() {
        try {
            Method method = (Method) this.mMethods.get("getSignalModelOneDayIndicator");
            if (method != null) {
                List list = (List) method.invoke(this.mModemAiMgr, null);
                logV("sigRec" + list);
                if (list.size() > 0) {
                    HashMap map = new HashMap();
                    map.put("Params", list.toString());
                    MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "signal_model", map);
                }
            }
            Method method2 = (Method) this.mMethods.get("getGameModelOneDayIndicator");
            if (method2 != null) {
                List list2 = (List) method2.invoke(this.mModemAiMgr, null);
                logV("gameRec" + list2);
                if (list2.size() > 0) {
                    HashMap map2 = new HashMap();
                    map2.put("Params", list2.toString());
                    MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "game_model", map2);
                }
            }
            Method method3 = (Method) this.mMethods.get("getGameModelOneDayParam");
            if (method3 != null) {
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) method3.invoke(this.mModemAiMgr, null);
                logV("gameParamRec" + concurrentHashMap);
                if (concurrentHashMap.size() > 0) {
                    for (String str : concurrentHashMap.keySet()) {
                        ArrayList arrayList = (ArrayList) concurrentHashMap.get(str);
                        StringBuilder sb = new StringBuilder();
                        sb.append("gameParamRec key = ");
                        sb.append(str);
                        sb.append(" list = ");
                        sb.append(arrayList == null ? "null" : arrayList.toString());
                        logV(sb.toString());
                        HashMap map3 = new HashMap();
                        map3.put(str, arrayList);
                        logV("gameParamRec param = " + map3);
                        MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "game_model_param", map3);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reportModemAiStats() {
        ModemAiController modemAiController = sInstance;
        if (modemAiController != null) {
            modemAiController.reportModemAiStatsInner();
        }
    }
}
