package com.android.phone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.hyperradio.HyperRadioManager;
import com.xiaomi.internal.hyperradio.config.ConfigSInfo;
import com.xiaomi.internal.hyperradio.config.SetLevelArgumentsSInfo;
import java.util.NoSuchElementException;
import java.util.Objects;
import miui.telephony.DefaultSimManager;
import vendor.qti.data.factoryservice.IFactory;
import vendor.qti.data.factoryservice.Result;
import vendor.qti.latencyaidlservice.Config;
import vendor.qti.latencyaidlservice.FilterInfo;
import vendor.qti.latencyaidlservice.IClientToken;
import vendor.qti.latencyaidlservice.ILinkLatencyIndication;
import vendor.qti.latencyaidlservice.ILinkLatencyService;
import vendor.qti.latencyaidlservice.SetLevelArguments;

/* JADX INFO: loaded from: classes.dex */
public class LowLatencyAidlService extends Service {
    public static Context mContext;
    private IBinder mBinder;
    private HandlerThread mEventThread;
    private DataLatencyHandler mLatencyHandler;
    private HandlerThread mServerMonitorThread;
    private Token mToken = null;
    private Handler mServerNotiHandler = null;
    private ServerNotiHandlerCallback mServerNotiHdlrCb = null;
    private Message mNotificationMsg = null;
    private ServiceFetcher mServiceFetcher = null;
    private ILinkLatencyService mILinkLatencyService = null;
    private IFactory mFactoryService = null;
    private LinkLatencyIndication mLinkLatencyIndication = new LinkLatencyIndication(this);
    private boolean mIsOptStatus = false;
    private int mLastDdsPhoneId = -1;
    private int mRetryCount = 0;
    BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.LowLatencyAidlService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                Log.d("LowLatencyAidlService", "received null intent");
                return;
            }
            String action = intent.getAction();
            if (action == null) {
                Log.d("LowLatencyAidlService", "received null intent action");
                return;
            }
            if (action.equals("com.android.phone.intent.action.CHANGE_LEVEL")) {
                Log.d("LowLatencyAidlService", "Low Latency Level Changed Event Received");
                LowLatencyAidlService.this.mLatencyHandler.obtainMessage(2, intent).sendToTarget();
            } else if (action.equals("com.android.phone.intent.action.CHANGE_PRIO")) {
                Log.d("LowLatencyAidlService", "Low Latency prioritize Changed Event Received");
                LowLatencyAidlService.this.mLatencyHandler.obtainMessage(3, intent).sendToTarget();
            } else if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                LowLatencyAidlService.this.mLatencyHandler.obtainMessage(4, intent).sendToTarget();
            } else if (action.equals("com.android.phone.intent.action.UPDATE_PDCP_DISCARD_TIMER")) {
                LowLatencyAidlService.this.mLatencyHandler.obtainMessage(5, intent).sendToTarget();
            }
        }
    };
    private ServiceNotification mServiceNotificationCallback = new ServiceNotification() { // from class: com.android.phone.LowLatencyAidlService.4
        @Override // com.android.phone.LowLatencyAidlService.ServiceNotification
        public void onRegistration() {
            Log.d("LowLatencyAidlService", "Cnd HAL onRegistration");
            try {
                LowLatencyAidlService lowLatencyAidlService = LowLatencyAidlService.this;
                lowLatencyAidlService.mNotificationMsg = lowLatencyAidlService.mServerNotiHandler.obtainMessage();
                LowLatencyAidlService.this.mNotificationMsg.what = 2;
                LowLatencyAidlService.this.mServerNotiHandler.sendMessage(LowLatencyAidlService.this.mNotificationMsg);
            } catch (NullPointerException e) {
                Log.d("LowLatencyAidlService", "HAL_SERVER_UP: Null Pointer Exception: " + e);
            } catch (Exception e2) {
                Log.d("LowLatencyAidlService", "HAL_SERVER_UP: Caught Unexpected Exception: " + e2);
                e2.printStackTrace();
            }
        }
    };
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.android.phone.LowLatencyAidlService.5
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d("LowLatencyAidlService", "Cnd HAL binderDied");
            try {
                LowLatencyAidlService.this.mILinkLatencyService.asBinder().unlinkToDeath(LowLatencyAidlService.this.mDeathRecipient, 0);
            } catch (Exception e) {
                Log.d("LowLatencyAidlService", "unlinkToDeath Caught Exception:", e);
            }
            try {
                LowLatencyAidlService lowLatencyAidlService = LowLatencyAidlService.this;
                lowLatencyAidlService.mNotificationMsg = lowLatencyAidlService.mServerNotiHandler.obtainMessage();
                LowLatencyAidlService.this.mNotificationMsg.what = 1;
                LowLatencyAidlService.this.mServerNotiHandler.sendMessage(LowLatencyAidlService.this.mNotificationMsg);
            } catch (NullPointerException e2) {
                Log.e("LowLatencyAidlService", "HAL_SERVER_DOWN: Null Pointer Exception: " + e2);
            } catch (Exception e3) {
                Log.e("LowLatencyAidlService", "HAL_SERVER_DOWN: Caught Unexpected Exception: " + e3);
                e3.printStackTrace();
            }
            LowLatencyAidlService.this.mILinkLatencyService = null;
        }
    };

    interface ServiceNotification {
        void onRegistration();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return false;
    }

    class Token extends IClientToken.Stub {
        @Override // vendor.qti.latencyaidlservice.IClientToken
        public int getInterfaceVersion() {
            return 1;
        }

        public Token(LowLatencyAidlService lowLatencyAidlService) {
            Log.d("LowLatencyAidlService", "new Token");
        }

        @Override // vendor.qti.latencyaidlservice.IClientToken
        public String getInterfaceHash() {
            return "7ead93de4c59bca175788844884846e7e32fe287";
        }
    }

    class LinkLatencyIndication extends ILinkLatencyIndication.Stub {
        @Override // vendor.qti.latencyaidlservice.ILinkLatencyIndication
        public int getInterfaceVersion() {
            return 1;
        }

        public LinkLatencyIndication(LowLatencyAidlService lowLatencyAidlService) {
            Log.d("LowLatencyAidlService", "new LinkLatencyIndication");
        }

        @Override // vendor.qti.latencyaidlservice.ILinkLatencyIndication
        public String getInterfaceHash() {
            return "7ead93de4c59bca175788844884846e7e32fe287";
        }

        @Override // vendor.qti.latencyaidlservice.ILinkLatencyIndication
        public void filterStatus(FilterInfo filterInfo) {
            Log.d("LowLatencyAidlService", "LinkLatencyIndication received filterStatus");
            Log.d("LowLatencyAidlService", "LinkLatencyIndication received filterId: " + filterInfo.filterId);
            Log.d("LowLatencyAidlService", "LinkLatencyIndication received status: " + filterInfo.status);
        }
    }

    class DataLatencyHandler extends Handler {
        public DataLatencyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d("LowLatencyAidlService", "handleMessage msg.what:" + message.what);
            int i = message.what;
            if (i == 1) {
                if (LowLatencyAidlService.this.waitForIFactoryavailable() || LowLatencyAidlService.this.mRetryCount >= 6) {
                    return;
                }
                LowLatencyAidlService.this.mRetryCount++;
                Log.d("LowLatencyAidlService", "mRetryCount=" + LowLatencyAidlService.this.mRetryCount);
                removeMessages(1);
                sendEmptyMessageDelayed(1, 10000L);
                return;
            }
            if (i == 2) {
                LowLatencyAidlService.this.onSetLevel((Intent) message.obj);
                return;
            }
            if (i != 3) {
                if (i == 4) {
                    LowLatencyAidlService.this.processDefaultDataChanged();
                    return;
                } else {
                    if (i != 5) {
                        return;
                    }
                    LowLatencyAidlService.this.updatePdcpDiscardTimerForDDS(((Intent) message.obj).getIntExtra("discard_timer", -1));
                    return;
                }
            }
            boolean booleanExtra = ((Intent) message.obj).getBooleanExtra("prio_default", true);
            Log.d("LowLatencyAidlService", "prio_default: " + Boolean.valueOf(booleanExtra));
            try {
                if (ModemProvider.IS_XRING) {
                    HyperRadioManager.getInstance(LowLatencyAidlService.mContext).prioritizeDefaultDataSubscription(booleanExtra, new LowLatencyAidlService$$ExternalSyntheticLambda0(this), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.LowLatencyAidlService.DataLatencyHandler.1
                        public void onResponse(int i2) {
                            Log.d("LowLatencyAidlService", "prioritizeDefaultDataSubscription status: " + i2);
                        }
                    });
                } else if (ModemProvider.IS_QCOM) {
                    Log.d("LowLatencyAidlService", "prioritizeDefaultDataSubscription status: " + LowLatencyAidlService.this.mILinkLatencyService.prioritizeDefaultDataSubscription(booleanExtra));
                }
            } catch (Exception e) {
                Log.d("LowLatencyAidlService", "prioritizeDefaultDataSubscription Caught Exception:", e);
            }
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Log.d("LowLatencyAidlService", "onCreate method");
        mContext = this;
        this.mToken = new Token(this);
        this.mServiceFetcher = new ServiceFetcher();
        this.mServerNotiHdlrCb = new ServerNotiHandlerCallback();
        HandlerThread handlerThread = new HandlerThread("ServiceMonitorThread");
        this.mServerMonitorThread = handlerThread;
        handlerThread.start();
        this.mServerNotiHandler = new Handler(this.mServerMonitorThread.getLooper(), this.mServerNotiHdlrCb);
        HandlerThread handlerThread2 = new HandlerThread("MainEventThread");
        this.mEventThread = handlerThread2;
        handlerThread2.start();
        this.mLatencyHandler = new DataLatencyHandler(this.mEventThread.getLooper());
        this.mLastDdsPhoneId = getDefaultDataSlotId();
        registerReceiver();
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int i) {
        Log.d("LowLatencyAidlService", "onStart: Attempt to start Service");
        super.onStart(intent, i);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d("LowLatencyAidlService", "onStartCommand- Call");
        this.mLatencyHandler.removeMessages(1);
        this.mLatencyHandler.sendMessageDelayed(this.mLatencyHandler.obtainMessage(1), 1000L);
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        Log.d("LowLatencyAidlService", "LowLatencyAidlService Destroyed Successfully...");
        super.onDestroy();
        ILinkLatencyService iLinkLatencyService = this.mILinkLatencyService;
        if (iLinkLatencyService != null) {
            try {
                iLinkLatencyService.close();
                this.mILinkLatencyService.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            } catch (Exception e) {
                Log.d("LowLatencyAidlService", "close Caught Exception:", e);
            }
            this.mILinkLatencyService = null;
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.CHANGE_LEVEL");
        intentFilter.addAction("com.android.phone.intent.action.CHANGE_PRIO");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.UPDATE_PDCP_DISCARD_TIMER");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, null, this.mLatencyHandler, 2);
    }

    public boolean registerServiceUpNotification() {
        Log.d("LowLatencyAidlService", "registerServiceUpNotification...");
        this.mServiceFetcher.registerForNotifications(this.mServiceNotificationCallback);
        return true;
    }

    private boolean createILinkLatencyService(IFactory iFactory) {
        try {
            Result result = new Result();
            ILinkLatencyService iLinkLatencyServiceCreateILinkLatencyService = iFactory.createILinkLatencyService(this.mToken, result);
            if (result.status == 0) {
                this.mILinkLatencyService = iLinkLatencyServiceCreateILinkLatencyService;
                iLinkLatencyServiceCreateILinkLatencyService.setCallback(this.mLinkLatencyIndication);
                return true;
            }
            Log.e("LowLatencyAidlService", "createILinkLatencyService failed with status " + result.status);
            return false;
        } catch (RemoteException e) {
            Log.e("LowLatencyAidlService", "Failed to createILinkLatencyService: " + e);
            return false;
        } catch (NullPointerException unused) {
            Log.e("LowLatencyAidlService", "Asked to createILinkLatencyService with null factory");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean connectToILinkLatencyService() {
        if (this.mILinkLatencyService == null) {
            try {
                Log.d("LowLatencyAidlService", "getService in connect");
                IFactory service = this.mServiceFetcher.getService();
                this.mFactoryService = service;
                if (!createILinkLatencyService(service)) {
                    this.mILinkLatencyService = null;
                }
                ILinkLatencyService iLinkLatencyService = this.mILinkLatencyService;
                if (iLinkLatencyService != null) {
                    iLinkLatencyService.asBinder().linkToDeath(this.mDeathRecipient, 0);
                }
                Log.d("LowLatencyAidlService", "Connected to ILinkLatencyService ".concat(this.mILinkLatencyService == null ? "FAILED" : "SUCC"));
            } catch (RemoteException e) {
                Log.e("LowLatencyAidlService", "Failed to connect to cnd: " + e);
                this.mILinkLatencyService = null;
            } catch (NoSuchElementException e2) {
                Log.e("LowLatencyAidlService", "Failed to connect to cnd: " + e2);
                this.mILinkLatencyService = null;
            }
        }
        return this.mILinkLatencyService != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean waitForIFactoryavailable() {
        this.mILinkLatencyService = null;
        Log.d("LowLatencyAidlService", "Disconnected from service");
        registerServiceUpNotification();
        return true;
    }

    private Config getNormalConfig(int i) {
        Config config = new Config();
        config.rat = 0L;
        config.slotId = i;
        config.uplink = 1L;
        config.downlink = 1L;
        config.enableConnectionExtension = false;
        return config;
    }

    private Config getDSCPOptimizeConfig(int i) {
        Config config = new Config();
        config.rat = 0L;
        config.slotId = i;
        config.uplink = 2L;
        config.downlink = 2L;
        config.enableConnectionExtension = false;
        return config;
    }

    private Config getHighOptimizeConfig(int i) {
        Config config = new Config();
        config.rat = 0L;
        config.slotId = i;
        config.uplink = 4L;
        config.downlink = 4L;
        config.enableConnectionExtension = true;
        return config;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSetLevel(Intent intent) {
        Config highOptimizeConfig;
        new Config();
        long longExtra = intent.getLongExtra("Rat_type", 0L);
        long longExtra2 = intent.getLongExtra("Slot_id", -1L);
        long longExtra3 = intent.getLongExtra("Level_UL", 1L);
        long longExtra4 = intent.getLongExtra("Level_DL", 1L);
        boolean booleanExtra = intent.getBooleanExtra("enableConnectionExtension", false);
        int intExtra = intent.getIntExtra("scene", 0);
        int i = longExtra2 == -1 ? this.mLastDdsPhoneId : (int) longExtra2;
        if (intExtra == 1 || intExtra == 5) {
            highOptimizeConfig = getHighOptimizeConfig(i);
        } else if (intExtra == -1) {
            highOptimizeConfig = getNormalConfig(i);
        } else if (intExtra == 3 || intExtra == 7 || intExtra == 9) {
            highOptimizeConfig = getDSCPOptimizeConfig(i);
        } else {
            Config normalConfig = getNormalConfig(i);
            normalConfig.rat = longExtra;
            normalConfig.slotId = i;
            if (longExtra3 == 2 && longExtra4 == 2) {
                longExtra3 = 4;
                longExtra4 = 4;
            }
            normalConfig.uplink = longExtra3;
            normalConfig.downlink = longExtra4;
            normalConfig.enableConnectionExtension = booleanExtra;
            onGameControlStateChanged(intent);
            highOptimizeConfig = normalConfig;
        }
        Log.d("LowLatencyAidlService", "setLevel: rat = " + highOptimizeConfig.rat + ", slot_id = " + highOptimizeConfig.slotId + ", uplink = " + highOptimizeConfig.uplink + ", downlink = " + highOptimizeConfig.downlink + ", enableConnectionExtension = " + highOptimizeConfig.enableConnectionExtension);
        setLevel(highOptimizeConfig);
    }

    private void setLevel(final Config config) {
        try {
            if (ModemProvider.IS_XRING) {
                SetLevelArgumentsSInfo setLevelArgumentsSInfo = new SetLevelArgumentsSInfo();
                ConfigSInfo configSInfo = new ConfigSInfo();
                configSInfo.rat = config.rat;
                configSInfo.slotId = config.slotId;
                configSInfo.uplink = config.uplink;
                configSInfo.downlink = config.downlink;
                configSInfo.enableConnectionExtension = config.enableConnectionExtension;
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(mContext);
                DataLatencyHandler dataLatencyHandler = this.mLatencyHandler;
                Objects.requireNonNull(dataLatencyHandler);
                hyperRadioManager.setLevel(configSInfo, setLevelArgumentsSInfo, new LowLatencyAidlService$$ExternalSyntheticLambda0(dataLatencyHandler), new HyperRadioManager.GenericResponse() { // from class: com.android.phone.LowLatencyAidlService.2
                    public void onResponse(int i) {
                        if (i == 0) {
                            LowLatencyAidlService.this.mIsOptStatus = config.downlink != 1;
                        }
                    }
                });
                return;
            }
            if (ModemProvider.IS_QCOM) {
                SetLevelArguments setLevelArguments = new SetLevelArguments();
                long level = this.mILinkLatencyService.setLevel(config, setLevelArguments);
                Log.d("LowLatencyAidlService", "setLevel status: " + level);
                if (level == 0) {
                    this.mIsOptStatus = config.downlink != 1;
                    Log.d("LowLatencyAidlService", "setLevel result.effectiveUplink: " + setLevelArguments.effectiveUplink);
                    Log.d("LowLatencyAidlService", "setLevel result.effectiveDownlink: " + setLevelArguments.effectiveDownlink);
                }
            }
        } catch (Exception e) {
            Log.d("LowLatencyAidlService", "setLevel Caught Exception:", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePdcpDiscardTimerForDDS(long j) {
        try {
            if (ModemProvider.IS_XRING) {
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(mContext);
                DataLatencyHandler dataLatencyHandler = this.mLatencyHandler;
                Objects.requireNonNull(dataLatencyHandler);
                hyperRadioManager.updatePdcpDiscardTimerForDDS(j, new LowLatencyAidlService$$ExternalSyntheticLambda0(dataLatencyHandler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.LowLatencyAidlService.3
                    public void onResponse(int i) {
                        Log.d("LowLatencyAidlService", "updatePdcpDiscardTimerForDDS status: " + i);
                    }
                });
                return;
            }
            if (ModemProvider.IS_QCOM) {
                Log.d("LowLatencyAidlService", "updatePdcpDiscardTimerForDDS status: " + this.mILinkLatencyService.updatePdcpDiscardTimerForDDS(j));
            }
        } catch (Exception e) {
            Log.d("LowLatencyAidlService", "updatePdcpDiscardTimerForDDS Caught Exception:", e);
        }
    }

    private void onGameControlStateChanged(Intent intent) {
        boolean z;
        long longExtra = intent.getLongExtra("Level_UL", 1L);
        long longExtra2 = intent.getLongExtra("Level_DL", 1L);
        if (longExtra == 2 && longExtra2 == 2) {
            SystemProperties.set("persist.radio.game.status", "true");
            z = true;
        } else {
            if (longExtra == 1 && longExtra2 == 1) {
                SystemProperties.set("persist.radio.game.status", "false");
            }
            z = false;
        }
        boolean zIs5GGameEnabledByCloud = FiveGCloudController.is5GGameEnabledByCloud();
        Log.d("LowLatencyAidlService", "onGameControlStateChanged status = " + z + ", cloud = " + zIs5GGameEnabledByCloud);
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

    class ServerNotiHandlerCallback implements Handler.Callback {
        private ServerNotiHandlerCallback() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                Log.d("LowLatencyAidlService", "handling HAL_SERVER_DOWN");
                LowLatencyAidlService.this.mILinkLatencyService = null;
                LowLatencyAidlService.this.waitForIFactoryavailable();
            } else if (i == 2) {
                Log.d("LowLatencyAidlService", "handling HAL_SERVER_UP");
                LowLatencyAidlService.this.connectToILinkLatencyService();
            } else {
                Log.d("LowLatencyAidlService", "Invalid message obtained");
            }
            return true;
        }
    }

    class ServiceFetcher {
        private Handler mServiceFetcherHandler;
        private HandlerThread mThread;
        private ServiceNotification mCb = null;
        private boolean pendingGetService = false;

        protected ServiceFetcher() {
            this.mThread = null;
            this.mServiceFetcherHandler = null;
            HandlerThread handlerThread = new HandlerThread("ServiceFetcher");
            this.mThread = handlerThread;
            handlerThread.start();
            this.mServiceFetcherHandler = new Handler(this.mThread.getLooper(), new Handler.Callback() { // from class: com.android.phone.LowLatencyAidlService.ServiceFetcher.1
                @Override // android.os.Handler.Callback
                public synchronized boolean handleMessage(Message message) {
                    try {
                        if (message.what == 1) {
                            Log.d("LowLatencyAidlService", "handling GET_SERVICE in ServiceFetcher");
                            if (ServiceFetcher.this.getService() == null) {
                                Log.e("LowLatencyAidlService", "Error in get IFactory Service");
                            } else {
                                ServiceFetcher.this.mCb.onRegistration();
                            }
                            ServiceFetcher.this.pendingGetService = false;
                        } else {
                            Log.d("LowLatencyAidlService", "Invalid message obtained");
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                    return true;
                }
            });
        }

        protected synchronized void registerForNotifications(ServiceNotification serviceNotification) {
            this.mCb = serviceNotification;
            if (!this.pendingGetService) {
                Message messageObtainMessage = this.mServiceFetcherHandler.obtainMessage();
                messageObtainMessage.what = 1;
                this.mServiceFetcherHandler.sendMessage(messageObtainMessage);
                this.pendingGetService = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IFactory getService() {
            LowLatencyAidlService.this.mFactoryService = null;
            try {
                Log.d("LowLatencyAidlService", "before waitForService");
                LowLatencyAidlService.this.mBinder = ServiceManager.waitForService("vendor.qti.data.factoryservice.IFactory/default");
                Log.d("LowLatencyAidlService", "after waitForService");
                LowLatencyAidlService lowLatencyAidlService = LowLatencyAidlService.this;
                lowLatencyAidlService.mFactoryService = IFactory.Stub.asInterface(lowLatencyAidlService.mBinder);
            } catch (NoSuchElementException e) {
                LowLatencyAidlService.this.mFactoryService = null;
                Log.e("LowLatencyAidlService", "getService error: " + e);
            }
            return LowLatencyAidlService.this.mFactoryService;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDefaultDataChanged() {
        int defaultDataSlotId = getDefaultDataSlotId();
        Log.d("LowLatencyAidlService", "processDefaultDataChanged dds=" + defaultDataSlotId + ",lastdds=" + this.mLastDdsPhoneId + ",isOpt=" + this.mIsOptStatus);
        int i = this.mLastDdsPhoneId;
        if (i != defaultDataSlotId && this.mIsOptStatus) {
            setLevel(getNormalConfig(i));
        }
        this.mLastDdsPhoneId = defaultDataSlotId;
    }

    private int getDefaultDataSlotId() {
        DefaultSimManager.getInstance();
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (isValidatePhoneId(defaultDataSlotId)) {
            return defaultDataSlotId;
        }
        Log.d("LowLatencyAidlService", "getDefaultDataSlotId invalid phoneid!");
        return -1;
    }

    private boolean isValidatePhoneId(int i) {
        return i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT;
    }
}
