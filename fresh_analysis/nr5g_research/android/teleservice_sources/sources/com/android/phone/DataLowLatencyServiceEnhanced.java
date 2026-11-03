package com.android.phone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.internal.hidl.manager.V1_0.IServiceManager;
import android.internal.hidl.manager.V1_0.IServiceNotification;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IHwBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.telephony.SettingsObserver;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import java.util.NoSuchElementException;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManagerEx;
import vendor.qti.data.factory.V2_0.IFactory;
import vendor.qti.latency.V2_0.Config;
import vendor.qti.latency.V2_0.IClientToken;
import vendor.qti.latency.V2_0.ILinkLatencyService;

/* JADX INFO: loaded from: classes.dex */
public class DataLowLatencyServiceEnhanced extends Service {
    private static Context mContext;
    private static IFactory mHidlService;
    private static ILinkLatencyService mILinkLatencyService;
    private HandlerThread mEventThread;
    private boolean mIsScreenOn;
    private DataLatencyHandler mLatencyHandler;
    private SettingsObserver mSettingsObserver;
    private boolean mIsServiceStarted = false;
    private boolean mIsHIDLDead = true;
    private final long mDeathBinderCookie = 1011011;
    private final Object mLock = new Object();
    private IServiceManager mIServiceManager = null;
    private int mRetryCount = 0;
    private long mLastUplinkLevel = 1;
    private long mLastDownlinkLevel = 1;
    private long mLastConnectionEx = -1;
    private Config mSetLevelConfig = null;
    private boolean mIsMobileDataOn = false;
    private int mLastScene = -2;
    private boolean mIsGeoTypeEnabled = false;
    private boolean mIsOptStatus = false;
    private long mTimeLastExitGeoMs = 0;
    private int mLastDdsPhoneId = -1;
    private final IServiceNotification mServiceNotificationCallback = new IServiceNotification.Stub() { // from class: com.android.phone.DataLowLatencyServiceEnhanced.1
        @Override // android.internal.hidl.manager.V1_0.IServiceNotification
        public void onRegistration(String str, String str2, boolean z) {
            synchronized (DataLowLatencyServiceEnhanced.this.mLock) {
                Log.d("DataLowLatencyServiceEnhanced", "IServiceNotification.onRegistration");
                DataLowLatencyServiceEnhanced.this.startService();
            }
        }
    };
    private final IClientToken mClientToken = new IClientToken.Stub(this) { // from class: com.android.phone.DataLowLatencyServiceEnhanced.2
    };
    private IHwBinder.DeathRecipient mDeathRecipient = new IHwBinder.DeathRecipient() { // from class: com.android.phone.DataLowLatencyServiceEnhanced.3
        public void serviceDied(long j) {
            Log.d("DataLowLatencyServiceEnhanced", "HIDL SErvice Died");
            if (j != 1011011) {
                return;
            }
            try {
                DataLowLatencyServiceEnhanced.mHidlService.unlinkToDeath(DataLowLatencyServiceEnhanced.this.mDeathRecipient);
            } catch (RemoteException unused) {
                Log.e("DataLowLatencyServiceEnhanced", "RemoteException : unable to unlink DeathRecipient");
            }
            DataLowLatencyServiceEnhanced.this.mIsHIDLDead = true;
            DataLowLatencyServiceEnhanced.this.mIsServiceStarted = false;
            DataLowLatencyServiceEnhanced.this.mIServiceManager = null;
            DataLowLatencyServiceEnhanced.this.registerForNotification();
        }
    };
    BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.DataLowLatencyServiceEnhanced.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                Log.e("DataLowLatencyServiceEnhanced", "received null intent or action");
                return;
            }
            String action = intent.getAction();
            if (DataLowLatencyServiceEnhanced.mILinkLatencyService == null) {
                Log.e("DataLowLatencyServiceEnhanced", "mILinkLatencyService is null !!!");
                return;
            }
            if (action.equals("com.android.phone.intent.action.CHANGE_LEVEL")) {
                Log.d("DataLowLatencyServiceEnhanced", "Low Latency Level Changed Event Received");
                DataLowLatencyServiceEnhanced.this.mLatencyHandler.obtainMessage(2, intent).sendToTarget();
                return;
            }
            if (action.equals("com.android.phone.intent.action.CHANGE_PRIO")) {
                Log.d("DataLowLatencyServiceEnhanced", "Low Latency prioritize Changed Event Received");
                DataLowLatencyServiceEnhanced.this.mLatencyHandler.obtainMessage(3, intent).sendToTarget();
                return;
            }
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (networkInfo == null || networkInfo.getType() != 0) {
                    return;
                }
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED && !DataLowLatencyServiceEnhanced.this.mIsMobileDataOn) {
                    DataLowLatencyServiceEnhanced.this.mLatencyHandler.obtainMessage(5, Boolean.TRUE).sendToTarget();
                    return;
                } else {
                    if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED && DataLowLatencyServiceEnhanced.this.mIsMobileDataOn) {
                        DataLowLatencyServiceEnhanced.this.mLatencyHandler.obtainMessage(5, Boolean.FALSE).sendToTarget();
                        return;
                    }
                    return;
                }
            }
            if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                DataLowLatencyServiceEnhanced.this.mLatencyHandler.obtainMessage(8, intent).sendToTarget();
            }
        }
    };
    private final BaseEvent.IEventListener mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.DataLowLatencyServiceEnhanced.6
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                DataLowLatencyServiceEnhanced.this.mLatencyHandler.obtainMessage(4).sendToTarget();
            }
        }
    };

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Log.d("DataLowLatencyServiceEnhanced", "onCreate method");
        mContext = this;
        HandlerThread handlerThread = new HandlerThread("EnhancedLowLatencyThread");
        this.mEventThread = handlerThread;
        handlerThread.start();
        this.mLatencyHandler = new DataLatencyHandler(this.mEventThread.getLooper());
        this.mLastDdsPhoneId = getDefaultDataSlotId();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(mContext);
            this.mSettingsObserver = new SettingsObserver(mContext, this.mLatencyHandler);
            regSettingsObserver();
            EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
        }
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.CHANGE_LEVEL");
        intentFilter.addAction("com.android.phone.intent.action.CHANGE_PRIO");
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        }
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, null, this.mLatencyHandler, 2);
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int i) {
        Log.d("DataLowLatencyServiceEnhanced", "onStart: Attempt to start Service");
        super.onStart(intent, i);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d("DataLowLatencyServiceEnhanced", "onStartCommand- Call");
        this.mLatencyHandler.removeMessages(1);
        this.mLatencyHandler.sendMessageDelayed(this.mLatencyHandler.obtainMessage(1), 1000L);
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        Log.d("DataLowLatencyServiceEnhanced", "LowLatencyServiceEnhanced Destroyed Successfully...");
        super.onDestroy();
        IFactory iFactory = mHidlService;
        if (iFactory != null) {
            try {
                iFactory.unlinkToDeath(this.mDeathRecipient);
            } catch (RemoteException e) {
                Log.e("DataLowLatencyServiceEnhanced", "onDestroy Caught Exception:", e);
            }
            mHidlService = null;
        }
        BroadcastReceiver broadcastReceiver = this.mIntentReceiver;
        if (broadcastReceiver != null) {
            mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startService() {
        if (this.mIsServiceStarted) {
            return;
        }
        Log.d("DataLowLatencyServiceEnhanced", "Trying to get HIDL-IFactory");
        try {
            mHidlService = IFactory.getService();
        } catch (RemoteException e) {
            Log.e("DataLowLatencyServiceEnhanced", "startService Caught Exception:", e);
        } catch (NoSuchElementException e2) {
            Log.e("DataLowLatencyServiceEnhanced", "startService Caught Exception: Service not Registered yet", e2);
        } catch (Exception e3) {
            Log.e("DataLowLatencyServiceEnhanced", "startService Caught Exception:", e3);
        }
        if (mHidlService != null) {
            Log.d("DataLowLatencyServiceEnhanced", "Received HIDL-IFactory");
            try {
                mHidlService.createILinkLatencyService(this.mClientToken, new IFactory.createILinkLatencyServiceCallback() { // from class: com.android.phone.DataLowLatencyServiceEnhanced$$ExternalSyntheticLambda1
                    @Override // vendor.qti.data.factory.V2_0.IFactory.createILinkLatencyServiceCallback
                    public final void onValues(int i, ILinkLatencyService iLinkLatencyService) {
                        this.f$0.lambda$startService$0(i, iLinkLatencyService);
                    }
                });
                this.mIsHIDLDead = !mHidlService.linkToDeath(this.mDeathRecipient, 1011011L);
                Log.d("DataLowLatencyServiceEnhanced", "mIsHIDLDead " + this.mIsHIDLDead);
            } catch (Exception e4) {
                Log.e("DataLowLatencyServiceEnhanced", "startService Caught Exception:", e4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startService$0(int i, ILinkLatencyService iLinkLatencyService) {
        if (i == 0) {
            Log.d("DataLowLatencyServiceEnhanced", "createILinkLatencyService success " + i);
            mILinkLatencyService = iLinkLatencyService;
            this.mIsServiceStarted = true;
            new Thread() { // from class: com.android.phone.DataLowLatencyServiceEnhanced.5
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        DataLowLatencyServiceEnhanced dataLowLatencyServiceEnhanced = DataLowLatencyServiceEnhanced.this;
                        dataLowLatencyServiceEnhanced.setLevel(dataLowLatencyServiceEnhanced.getDefaultConfig(dataLowLatencyServiceEnhanced.mLastDdsPhoneId));
                    } catch (Exception e) {
                        Log.e("DataLowLatencyServiceEnhanced", "setdefaultLevel Caught Exception:", e);
                    }
                }
            }.start();
            return;
        }
        Log.e("DataLowLatencyServiceEnhanced", "createILinkLatencyService failed with status " + i);
        this.mIsServiceStarted = false;
    }

    class DataLatencyHandler extends Handler {
        public DataLatencyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d("DataLowLatencyServiceEnhanced", "handleMessage msg.what:" + message.what);
            switch (message.what) {
                case 1:
                    if (!DataLowLatencyServiceEnhanced.this.registerForNotification() && DataLowLatencyServiceEnhanced.this.mRetryCount < 6) {
                        DataLowLatencyServiceEnhanced.this.mRetryCount++;
                        Log.d("DataLowLatencyServiceEnhanced", "mRetryCount=" + DataLowLatencyServiceEnhanced.this.mRetryCount);
                        removeMessages(1);
                        sendEmptyMessageDelayed(1, 10000L);
                        break;
                    }
                    break;
                case 2:
                    if (DataLowLatencyServiceEnhanced.this.mLastScene == 2) {
                        Log.d("DataLowLatencyServiceEnhanced", "EVENT_SET_LEVEL ignore during special nw!");
                    } else {
                        DataLowLatencyServiceEnhanced.this.onSetLevel((Intent) message.obj);
                    }
                    break;
                case 3:
                    boolean booleanExtra = ((Intent) message.obj).getBooleanExtra("prio_default", false);
                    Log.d("DataLowLatencyServiceEnhanced", "prio_default: " + booleanExtra);
                    try {
                        DataLowLatencyServiceEnhanced.mILinkLatencyService.prioritizeDefaultDataSubscription(booleanExtra);
                    } catch (Exception e) {
                        Log.e("DataLowLatencyServiceEnhanced", "prioritizeDefaultDataSubscription Caught Exception:", e);
                        return;
                    }
                    break;
                case 4:
                    DataLowLatencyServiceEnhanced.this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(DataLowLatencyServiceEnhanced.mContext);
                    int optStatus = DataLowLatencyServiceEnhanced.this.getOptStatus();
                    if (optStatus != -1) {
                        DataLowLatencyServiceEnhanced.this.onUpdateLlmStatus(optStatus, false);
                    }
                    break;
                case 5:
                    DataLowLatencyServiceEnhanced.this.mIsMobileDataOn = ((Boolean) message.obj).booleanValue();
                    int optStatus2 = DataLowLatencyServiceEnhanced.this.getOptStatus();
                    if (optStatus2 != -1) {
                        DataLowLatencyServiceEnhanced.this.onUpdateLlmStatus(optStatus2, false);
                    }
                    break;
                case 6:
                    boolean zIsGeoTypeEnabled = DataLowLatencyServiceEnhanced.isGeoTypeEnabled(DataLowLatencyServiceEnhanced.mContext);
                    if (zIsGeoTypeEnabled != DataLowLatencyServiceEnhanced.this.mIsGeoTypeEnabled) {
                        DataLowLatencyServiceEnhanced dataLowLatencyServiceEnhanced = DataLowLatencyServiceEnhanced.this;
                        if (zIsGeoTypeEnabled) {
                            dataLowLatencyServiceEnhanced.mLastScene = 2;
                        } else {
                            dataLowLatencyServiceEnhanced.mLastScene = -2;
                            DataLowLatencyServiceEnhanced.this.mTimeLastExitGeoMs = SystemClock.elapsedRealtime();
                        }
                        if (!zIsGeoTypeEnabled || DataLowLatencyServiceEnhanced.this.getElapsedTimeSinceExitGeoMs() > 300000) {
                            DataLowLatencyServiceEnhanced dataLowLatencyServiceEnhanced2 = DataLowLatencyServiceEnhanced.this;
                            dataLowLatencyServiceEnhanced2.onUpdateLlmStatus(dataLowLatencyServiceEnhanced2.getOptStatus(), true);
                        } else {
                            Log.d("DataLowLatencyServiceEnhanced", "skip back to back geo enter!");
                        }
                    }
                    DataLowLatencyServiceEnhanced.this.mIsGeoTypeEnabled = zIsGeoTypeEnabled;
                    break;
                case 7:
                    boolean zIsSpecialNetworkModeEnabled = DataLowLatencyServiceEnhanced.isSpecialNetworkModeEnabled(DataLowLatencyServiceEnhanced.mContext);
                    Log.d("DataLowLatencyServiceEnhanced", "isNwOptEnabled=" + zIsSpecialNetworkModeEnabled + ",mIsGeoTypeEnabled=" + DataLowLatencyServiceEnhanced.this.mIsGeoTypeEnabled);
                    if (DataLowLatencyServiceEnhanced.this.mIsGeoTypeEnabled && !zIsSpecialNetworkModeEnabled) {
                        DataLowLatencyServiceEnhanced.this.mLastScene = -2;
                        DataLowLatencyServiceEnhanced dataLowLatencyServiceEnhanced3 = DataLowLatencyServiceEnhanced.this;
                        dataLowLatencyServiceEnhanced3.onUpdateLlmStatus(dataLowLatencyServiceEnhanced3.getOptStatus(), true);
                        break;
                    }
                    break;
                case 8:
                    DataLowLatencyServiceEnhanced.this.processDefaultDataChanged();
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean registerForNotification() {
        try {
            if (!this.mIsServiceStarted) {
                if (this.mIServiceManager != null) {
                    Log.d("DataLowLatencyServiceEnhanced", "Already have a Service Manager running");
                } else {
                    IServiceManager service = IServiceManager.getService();
                    this.mIServiceManager = service;
                    if (service == null) {
                        Log.e("DataLowLatencyServiceEnhanced", "Failed to obtain Service Manager");
                        return false;
                    }
                }
                if (!this.mIServiceManager.registerForNotifications("vendor.qti.data.factory@2.0::IFactory", "", this.mServiceNotificationCallback)) {
                    Log.e("DataLowLatencyServiceEnhanced", "Failed to register for notifications to ILinkLatencyService");
                    return false;
                }
                Log.d("DataLowLatencyServiceEnhanced", "register for notifications to ILinkLatencyService");
                return true;
            }
            Log.d("DataLowLatencyServiceEnhanced", "Already HIDL-ILinkLatencyService is Started ");
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Config getDefaultConfig(int i) {
        Log.d("DataLowLatencyServiceEnhanced", "getDefaultConfig slotId=" + i);
        Config config = new Config();
        config.rat = 0L;
        config.slotId = i;
        config.uplink = 1L;
        config.downlink = 1L;
        config.enableConnectionExtension = false;
        return config;
    }

    private Config getWechatScanConfig(int i) {
        Log.d("DataLowLatencyServiceEnhanced", "getWechatScanConfig slotId=" + i);
        Config config = new Config();
        config.rat = 0L;
        config.slotId = i;
        config.uplink = 2L;
        config.downlink = 2L;
        config.enableConnectionExtension = true;
        return config;
    }

    private Config getOptimizedConfig(int i) {
        Log.d("DataLowLatencyServiceEnhanced", "getOptimizedConfig slotId=" + i);
        Config config = new Config();
        config.rat = 0L;
        config.slotId = i;
        config.uplink = 2L;
        config.downlink = 2L;
        config.enableConnectionExtension = false;
        return config;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLevel(final Config config) {
        if (mILinkLatencyService == null || config == null) {
            return;
        }
        Log.d("DataLowLatencyServiceEnhanced", "setLevel enter dl=" + config.downlink);
        try {
            mILinkLatencyService.setLevel(config, new ILinkLatencyService.setLevelCallback() { // from class: com.android.phone.DataLowLatencyServiceEnhanced$$ExternalSyntheticLambda0
                @Override // vendor.qti.latency.V2_0.ILinkLatencyService.setLevelCallback
                public final void onValues(long j, long j2, long j3) {
                    this.f$0.lambda$setLevel$1(config, j, j2, j3);
                }
            });
        } catch (Exception e) {
            Log.e("DataLowLatencyServiceEnhanced", "init setLevel Caught Exception:", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLevel$1(Config config, long j, long j2, long j3) {
        if (j == 0) {
            Log.d("DataLowLatencyServiceEnhanced", "setLevel success " + j + ",UL=" + j2 + ",DL=" + j3);
            this.mIsOptStatus = config.downlink == 2;
            return;
        }
        Log.e("DataLowLatencyServiceEnhanced", "setLevel failed with status " + j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSetLevel(Intent intent) {
        boolean z;
        long longExtra = intent.getLongExtra("Rat_type", 0L);
        long longExtra2 = intent.getLongExtra("Slot_id", -1L);
        long longExtra3 = intent.getLongExtra("Level_UL", 1L);
        long longExtra4 = intent.getLongExtra("Level_DL", 1L);
        int intExtra = intent.getIntExtra("enableConnectionExtension", -1);
        int intExtra2 = intent.getIntExtra("scene", 0);
        int i = longExtra2 == -1 ? this.mLastDdsPhoneId : (int) longExtra2;
        if (intExtra == -1) {
            z = this.mLastConnectionEx == 1;
            this.mLastUplinkLevel = longExtra3;
            this.mLastDownlinkLevel = longExtra4;
        } else {
            z = intExtra == 1;
            this.mLastConnectionEx = intExtra;
            longExtra3 = this.mLastUplinkLevel;
            longExtra4 = this.mLastDownlinkLevel;
        }
        Log.d("DataLowLatencyServiceEnhanced", "setLevel: rat[" + longExtra + "],level_ul[" + longExtra3 + "],level_dl[" + longExtra4 + "],slotid[" + i + "],enableConnectionEx[" + z + "],scene[" + intExtra2 + "]");
        if (intExtra2 == 1) {
            this.mSetLevelConfig = getWechatScanConfig(i);
        } else if (intExtra2 == -1) {
            this.mSetLevelConfig = getDefaultConfig(i);
        } else if (intExtra2 == 3) {
            this.mSetLevelConfig = getOptimizedConfig(i);
        } else {
            Config defaultConfig = getDefaultConfig(i);
            this.mSetLevelConfig = defaultConfig;
            defaultConfig.rat = longExtra;
            defaultConfig.slotId = i;
            if (longExtra3 == 2 && longExtra4 == 2) {
                Log.d("DataLowLatencyServiceEnhanced", "Enter GameScene");
                if (TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform8650()) {
                    Log.d("DataLowLatencyServiceEnhanced", "set Level4 version");
                    longExtra3 = 4;
                    longExtra4 = 4;
                }
            }
            Config config = this.mSetLevelConfig;
            config.uplink = longExtra3;
            config.downlink = longExtra4;
            config.enableConnectionExtension = z;
            onGameControlStateChanged(intent);
        }
        setLevel(this.mSetLevelConfig);
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0067  */
    /* JADX WARN: Code duplicated, block: B:23:? A[RETURN, SYNTHETIC] */
    private void onGameControlStateChanged(Intent intent) {
        boolean zIs5GGameEnabledByCloud;
        long longExtra = intent.getLongExtra("Level_UL", 1L);
        long longExtra2 = intent.getLongExtra("Level_DL", 1L);
        boolean z = false;
        try {
            if (longExtra == 2 && longExtra2 == 2) {
                SystemProperties.set("persist.radio.game.status", "true");
                z = true;
            } else {
                if (longExtra == 1 && longExtra2 == 1) {
                    SystemProperties.set("persist.radio.game.status", "false");
                }
                zIs5GGameEnabledByCloud = FiveGCloudController.is5GGameEnabledByCloud();
                Log.d("DataLowLatencyServiceEnhanced", "onGameControlStateChanged status = " + z + ", cloud = " + zIs5GGameEnabledByCloud);
                if (zIs5GGameEnabledByCloud) {
                    sendGameStatusBroadcast(z);
                }
            }
        } catch (Exception e) {
            Log.d("DataLowLatencyServiceEnhanced", "fail to set persist.radio.game.status, exception: " + e);
        }
        zIs5GGameEnabledByCloud = FiveGCloudController.is5GGameEnabledByCloud();
        Log.d("DataLowLatencyServiceEnhanced", "onGameControlStateChanged status = " + z + ", cloud = " + zIs5GGameEnabledByCloud);
        if (zIs5GGameEnabledByCloud) {
            sendGameStatusBroadcast(z);
        }
    }

    private void sendGameStatusBroadcast(boolean z) {
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.GAME_STATUS_SCENE");
        intent.setPackage(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
        intent.putExtra("game_status", z);
        mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onUpdateLlmStatus(int i, boolean z) {
        if (i != -1 || z) {
            int i2 = this.mLastDdsPhoneId;
            try {
                this.mSetLevelConfig = i == 1 ? getOptimizedConfig(i2) : getDefaultConfig(i2);
                Log.d("DataLowLatencyServiceEnhanced", "onUpdateLlmStatus setlevel");
                setLevel(this.mSetLevelConfig);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void regSettingsObserver() {
        this.mSettingsObserver.unobserve();
        this.mSettingsObserver.observe(Settings.Global.getUriFor("geo_type"), 6);
        this.mSettingsObserver.observe(Settings.Global.getUriFor("special_network_optimization"), 7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isGeoTypeEnabled(Context context) {
        int i = Settings.Global.getInt(context.getContentResolver(), "geo_type", 0);
        Log.d("DataLowLatencyServiceEnhanced", "isGeoTypeEnabled rst=" + i);
        return i != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSpecialNetworkModeEnabled(Context context) {
        int i = Settings.Global.getInt(context.getContentResolver(), "special_network_optimization", 1);
        Log.d("DataLowLatencyServiceEnhanced", "isSpecialNetworkModeEnabled rst=" + i);
        return i != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOptStatus() {
        int i = this.mLastScene;
        if (i == 2 && this.mIsScreenOn && this.mIsMobileDataOn && !this.mIsOptStatus) {
            return 1;
        }
        if (i == 2 && this.mIsOptStatus) {
            return (this.mIsScreenOn && this.mIsMobileDataOn) ? -1 : 0;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getElapsedTimeSinceExitGeoMs() {
        return SystemClock.elapsedRealtime() - this.mTimeLastExitGeoMs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDefaultDataChanged() {
        int defaultDataSlotId = getDefaultDataSlotId();
        Log.d("DataLowLatencyServiceEnhanced", "processDefaultDataChanged dds=" + defaultDataSlotId + ",lastdds=" + this.mLastDdsPhoneId + ",isOpt=" + this.mIsOptStatus);
        int i = this.mLastDdsPhoneId;
        if (i != defaultDataSlotId && this.mIsOptStatus) {
            setLevel(getDefaultConfig(i));
        }
        this.mLastDdsPhoneId = defaultDataSlotId;
    }

    private int getDefaultDataSlotId() {
        DefaultSimManager.getInstance();
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (isValidatePhoneId(defaultDataSlotId)) {
            return defaultDataSlotId;
        }
        Log.d("DataLowLatencyServiceEnhanced", "getDefaultDataSlotId invalid phoneid!");
        return -1;
    }

    private boolean isValidatePhoneId(int i) {
        return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
    }
}
