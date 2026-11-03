package com.android.phone.network;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemDataCollectorBase;
import com.android.phone.NetworkMetricsController;
import com.android.phone.PhoneGlobals;
import com.android.phone.utils.NetworkSpeedTracker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.IActivityChangeListener;
import miui.process.ProcessManager;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;
import xiaomi.hypercomm.HyperCommManager;
import xiaomi.hypercomm.SceneAwareCallback;

/* JADX INFO: loaded from: classes.dex */
public abstract class ModemAiQoeController {
    protected static String TAG = "ModemAiQoeController";
    protected static boolean VDBG = false;
    private static volatile boolean mIsUpdate = false;
    private static int mOnnxRuntimeLoadRetryCount = 0;
    protected static boolean mOnnxRuntimeLoaded = false;
    protected Context mContext;
    protected int mDdsPhoneId;
    protected boolean mIsRegsiterLteNr;
    protected ModemQoeMetrics mModemQoeMetrics;
    protected NetworkMetricsController mNetworkMetricsController;
    protected NetworkSpeedTracker mNetworkSpeedTracker;
    protected OrtEnvironment mOnnxEnv;
    protected String mOnnxModelPath;
    protected OrtSession mOnnxSession;
    protected TelephonyManager mTelephonyManager;
    protected Handler mWorkHandler;
    protected HandlerThread mWorkThread;
    private static List mMeetingActivityWhiteList = new ArrayList();
    private static List mLiveActivityWhiteList = new ArrayList();
    private static List mDefaultPackageWhiteList = new ArrayList();
    protected TeleCallBack mTeleCallBack = null;
    protected boolean mIsExistOnnxRuntimeEnv = false;
    private boolean mIsSubwayMode = false;
    private boolean mIsHstMode = false;
    private SceneAwareCallback mSceneAwareCallback = null;
    protected SignalStrength mSignalStrength = new SignalStrength();
    protected ServiceState[] mServiceState = new ServiceState[MiuiPhoneUtils.PHONE_COUNT];
    protected int mCollectGap = 2;
    protected float[] mAppScene = {-1.0f, -1.0f};
    public boolean mIsWhiteListForground = false;
    protected boolean mIsMobileDataOn = false;
    protected boolean mIsWifiDataOn = false;
    protected boolean mTestDataUpdate = false;
    protected float mTestOutput = -1.0f;
    protected String mPlmn = "";
    Random mRandom = new Random();
    private final Queue mMcsQueue = new LinkedList();
    private long mLastMcsUpdateTime = 0;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.network.ModemAiQoeController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            String action = intent.getAction();
            ModemAiQoeController.log("receive action" + action);
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (networkInfo != null && networkInfo.getType() == 0) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        ModemAiQoeController modemAiQoeController = ModemAiQoeController.this;
                        if (!modemAiQoeController.mIsMobileDataOn) {
                            modemAiQoeController.mWorkHandler.obtainMessage(10, Boolean.TRUE).sendToTarget();
                            return;
                        }
                    }
                    if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                        ModemAiQoeController modemAiQoeController2 = ModemAiQoeController.this;
                        if (modemAiQoeController2.mIsMobileDataOn) {
                            modemAiQoeController2.mWorkHandler.obtainMessage(10, Boolean.FALSE).sendToTarget();
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (networkInfo == null || networkInfo.getType() != 1) {
                    return;
                }
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    ModemAiQoeController modemAiQoeController3 = ModemAiQoeController.this;
                    if (!modemAiQoeController3.mIsWifiDataOn) {
                        modemAiQoeController3.mWorkHandler.obtainMessage(11, Boolean.TRUE).sendToTarget();
                        return;
                    }
                }
                if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                    ModemAiQoeController modemAiQoeController4 = ModemAiQoeController.this;
                    if (modemAiQoeController4.mIsWifiDataOn) {
                        modemAiQoeController4.mWorkHandler.obtainMessage(11, Boolean.FALSE).sendToTarget();
                        return;
                    }
                    return;
                }
                return;
            }
            if (action.equals("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED")) {
                ModemAiQoeController.this.updateHstMode(intent.getBooleanExtra("mIsHstMode", false));
                return;
            }
            if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                ModemAiQoeController.this.mWorkHandler.obtainMessage(5).sendToTarget();
                return;
            }
            if (action.equals("miui.intent.action.VIDEO_INFO")) {
                ModemAiQoeController modemAiQoeController5 = ModemAiQoeController.this;
                if (modemAiQoeController5.mWorkHandler == null || !modemAiQoeController5.mIsWhiteListForground) {
                    return;
                }
                int intExtra = intent.getIntExtra("render-quality", -1);
                int intExtra2 = intent.getIntExtra("render-start", -1);
                ModemAiQoeController.logD("ACTION_MIUI_VIDEO_INFO video quality:" + intExtra + " isVideoInFront:" + intExtra2);
                ModemAiQoeController.this.mWorkHandler.obtainMessage(12, intExtra, intExtra2).sendToTarget();
                return;
            }
            if (action.equals("com.android.phone.intent.action.ACTION_RECOVER_POLICY")) {
                if (ModemAiQoeController.this.mWorkHandler != null) {
                    int intExtra3 = intent.getIntExtra("action", -1);
                    int intExtra4 = intent.getIntExtra("scene", -1);
                    ModemAiQoeController.logD("ACTION_RECOVER_POLICY policy:" + intExtra3 + " scene:" + intExtra4);
                    ModemAiQoeController.this.mWorkHandler.obtainMessage(13, intExtra3, intExtra4).sendToTarget();
                    return;
                }
                return;
            }
            if (action.equals("com.android.phone.ACTION_MODEM_QOE_MODEL_VDBG_TEST")) {
                ModemAiQoeController.VDBG = true;
                ModemAiQoeController.this.mTestDataUpdate = intent.getBooleanExtra("testDataUpdate", false);
                ModemAiQoeController.this.mTestOutput = intent.getFloatExtra("testOutput", -1.0f);
            }
        }
    };
    private IActivityChangeListener.Stub mActivityChangeListener = new IActivityChangeListener.Stub() { // from class: com.android.phone.network.ModemAiQoeController.2
        public void onActivityChanged(ComponentName componentName, ComponentName componentName2) {
            boolean z;
            boolean z2;
            if (componentName == null || componentName2 == null) {
                return;
            }
            String string = componentName2.toString();
            String string2 = componentName.toString();
            ModemAiQoeController.logD("onActivityChanged cur= " + string + " pre" + string2);
            int i = 0;
            while (true) {
                z = true;
                if (i >= ModemAiQoeController.mLiveActivityWhiteList.size()) {
                    z2 = false;
                    break;
                }
                if (!TextUtils.isEmpty(string) && string.contains((CharSequence) ModemAiQoeController.mLiveActivityWhiteList.get(i))) {
                    ModemAiQoeController.logD("onActivityChanged enter live stream scenario");
                    ModemAiQoeController.this.mAppScene[0] = 1.0f;
                    z2 = true;
                    break;
                } else {
                    if (!TextUtils.isEmpty(string2) && string2.contains((CharSequence) ModemAiQoeController.mLiveActivityWhiteList.get(i))) {
                        ModemAiQoeController.logD("onActivityChanged exit live stream scenario");
                    }
                    i++;
                }
            }
            int i2 = 0;
            while (true) {
                if (i2 >= ModemAiQoeController.mMeetingActivityWhiteList.size()) {
                    z = z2;
                    break;
                }
                if (!TextUtils.isEmpty(string) && string.contains((CharSequence) ModemAiQoeController.mMeetingActivityWhiteList.get(i2))) {
                    ModemAiQoeController.logD("onActivityChanged enter meeting scenario");
                    ModemAiQoeController.this.mAppScene[0] = 2.0f;
                    break;
                } else {
                    if (!TextUtils.isEmpty(string2) && string2.contains((CharSequence) ModemAiQoeController.mMeetingActivityWhiteList.get(i2))) {
                        ModemAiQoeController.logD("onActivityChanged exit meeting stream scenario");
                        z2 = false;
                    }
                    i2++;
                }
            }
            if (ModemAiQoeController.this.mIsWhiteListForground != z) {
                ModemAiQoeController.logD("onActivityChanged pre:" + componentName + ",cur:" + componentName2 + ",mIsWhiteListForground:" + ModemAiQoeController.this.mIsWhiteListForground + ",mAppScene:" + Arrays.toString(ModemAiQoeController.this.mAppScene));
                ModemAiQoeController modemAiQoeController = ModemAiQoeController.this;
                modemAiQoeController.mIsWhiteListForground = z;
                modemAiQoeController.mWorkHandler.obtainMessage(9).sendToTarget();
            }
        }
    };

    protected abstract void handleSubModelMessage(Message message);

    protected void processCellLocationChangeEvent(int i, CellLocation cellLocation) {
    }

    protected void processMobileDataChangeEvent() {
    }

    protected void processSignalStrengthEvent(SignalStrength signalStrength) {
    }

    protected void processWifiDataChangeEvent() {
    }

    protected ModemAiQoeController(String str) {
        this.mOnnxModelPath = "";
        this.mModemQoeMetrics = null;
        this.mDdsPhoneId = -1;
        log("ModemAiQoeController init");
        this.mContext = PhoneGlobals.getInstance();
        this.mOnnxModelPath = str;
        HandlerThread handlerThread = new HandlerThread(TAG, 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mWorkHandler = new NetworkQoeHandler(this.mWorkThread.getLooper());
        initModemAiQoeWhiteList();
        registerAllEvents();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        this.mDdsPhoneId = getDefaultDataSlotId();
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mModemQoeMetrics = ModemQoeMetrics.getInstance();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mServiceState[i] = new ServiceState();
        }
        this.mNetworkSpeedTracker = NetworkSpeedTracker.getInstance();
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

    private void registerAllEvents() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.ACTION_MODEM_QOE_MODEL_VDBG_TEST");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("miui.intent.action.VIDEO_INFO");
        intentFilter.addAction("com.android.phone.intent.action.ACTION_RECOVER_POLICY");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        registerTelephonyInfoCallBack();
        registerSceneAwareCallback();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHstMode(boolean z) {
        if (this.mWorkHandler == null || this.mIsHstMode == z) {
            return;
        }
        this.mIsHstMode = z;
        updateMovementAppScene();
        log("updateHstMode mIsHstMode:" + this.mIsHstMode);
        this.mWorkHandler.obtainMessage(3).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSubwayMode(boolean z) {
        if (this.mWorkHandler == null || this.mIsSubwayMode == z) {
            return;
        }
        this.mIsSubwayMode = z;
        updateMovementAppScene();
        log("updateSubwayMode mIsSubwayMode:" + this.mIsSubwayMode);
        this.mWorkHandler.obtainMessage(3).sendToTarget();
    }

    private void updateMovementAppScene() {
        if (this.mIsHstMode) {
            this.mAppScene[1] = 2.0f;
            return;
        }
        boolean z = this.mIsSubwayMode;
        float[] fArr = this.mAppScene;
        if (z) {
            fArr[1] = 1.0f;
        } else {
            fArr[1] = -1.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSceneAwareCallback() {
        if (this.mSceneAwareCallback != null) {
            return;
        }
        try {
            HyperCommManager hyperCommManager = HyperCommManager.getDefault();
            if (hyperCommManager == null) {
                log("HyperCommManager is not available");
                retryRegisterSceneAwareCallback();
                return;
            }
            ModemAiQoeSceneAwareCallback modemAiQoeSceneAwareCallback = new ModemAiQoeSceneAwareCallback(new HandlerExecutor(this.mWorkHandler));
            boolean zRegisterSceneAwareCallback = hyperCommManager.registerSceneAwareCallback(modemAiQoeSceneAwareCallback);
            if (zRegisterSceneAwareCallback) {
                this.mSceneAwareCallback = modemAiQoeSceneAwareCallback;
            } else {
                retryRegisterSceneAwareCallback();
            }
            log("registerSceneAwareCallback result:" + zRegisterSceneAwareCallback);
        } catch (Exception e) {
            log("registerSceneAwareCallback Exception:" + e);
            retryRegisterSceneAwareCallback();
        }
    }

    private void retryRegisterSceneAwareCallback() {
        Handler handler = this.mWorkHandler;
        if (handler == null || this.mSceneAwareCallback != null) {
            return;
        }
        handler.removeMessages(14);
        this.mWorkHandler.sendEmptyMessageDelayed(14, 30000L);
    }

    class ModemAiQoeSceneAwareCallback extends SceneAwareCallback implements SceneAwareCallback.SubwayStateListener {
        ModemAiQoeSceneAwareCallback(HandlerExecutor handlerExecutor) {
            super(handlerExecutor);
        }

        public void onSubwayStateChanged(boolean z, boolean z2) {
            ModemAiQoeController.log("onSubwayStateChanged subwayState:" + z + ", isDomestic:" + z2);
            if (z2) {
                ModemAiQoeController.this.updateSubwayMode(z);
            }
        }
    }

    class TeleCallBack implements TelephonyStatAdapter.ICallBack {
        TeleCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            ModemAiQoeController modemAiQoeController = ModemAiQoeController.this;
            if (i == modemAiQoeController.mDdsPhoneId) {
                modemAiQoeController.mWorkHandler.obtainMessage(1, i, 0, signalStrength).sendToTarget();
            }
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            ModemAiQoeController.this.mWorkHandler.obtainMessage(6, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            ModemAiQoeController modemAiQoeController = ModemAiQoeController.this;
            if (i == modemAiQoeController.mDdsPhoneId) {
                modemAiQoeController.mWorkHandler.obtainMessage(2, i, 0, cellLocation).sendToTarget();
            }
        }
    }

    public void registerActivityChangeListener() {
        if (this.mActivityChangeListener != null) {
            logD("registerActivityChangeListener");
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            arrayList2.addAll(mDefaultPackageWhiteList);
            arrayList.addAll(mLiveActivityWhiteList);
            arrayList.addAll(mMeetingActivityWhiteList);
            ProcessManager.registerActivityChangeListener(arrayList2, arrayList, this.mActivityChangeListener);
        }
    }

    private void initModemAiQoeWhiteList() {
        mDefaultPackageWhiteList.addAll(getOptWhiteList("modemAi_Qoe_default_package_white_list"));
        mLiveActivityWhiteList.addAll(getOptWhiteList("modemAi_Qoe_live_activity_white_list"));
        mMeetingActivityWhiteList.addAll(getOptWhiteList("modemAi_Qoe_meeting_activity_white_list"));
        log("initModemAiQoeWhiteList:" + mDefaultPackageWhiteList.toString() + ",mLiveActivityWhiteList:" + mLiveActivityWhiteList.toString() + ",mMeetingActivityWhiteList:" + mMeetingActivityWhiteList.toString());
    }

    public List getOptWhiteList(String str) {
        if (str == null) {
            return new ArrayList();
        }
        List stringArray = HyperOSCustFeatureResolve.getStringArray(str);
        return stringArray != null ? stringArray : new ArrayList();
    }

    private void unRegisterActivityChangeListener() {
        logD("unRegisterActivityChangeListener");
        this.mIsWhiteListForground = false;
        ProcessManager.unregisterActivityChanageListener(this.mActivityChangeListener);
    }

    class NetworkQoeHandler extends Handler {
        NetworkQoeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ModemAiQoeController.logD("handleMessage event:" + message.what);
            switch (message.what) {
                case 1:
                    SignalStrength signalStrength = (SignalStrength) message.obj;
                    if (signalStrength != null) {
                        ModemAiQoeController.logD("EVENT_SIGNAL_STRENGTH_CHANGED");
                        ModemAiQoeController.this.mSignalStrength = new SignalStrength(signalStrength);
                        ModemAiQoeController modemAiQoeController = ModemAiQoeController.this;
                        modemAiQoeController.processSignalStrengthEvent(modemAiQoeController.mSignalStrength);
                    }
                    break;
                case 2:
                    ModemAiQoeController.this.processCellLocationChangeEvent(message.arg1, (CellLocation) message.obj);
                    break;
                case 3:
                    boolean z = ModemAiQoeController.this.mIsSubwayMode || ModemAiQoeController.this.mIsHstMode;
                    if (z) {
                        ModemAiQoeController modemAiQoeController2 = ModemAiQoeController.this;
                        if (!modemAiQoeController2.mIsExistOnnxRuntimeEnv) {
                            modemAiQoeController2.mIsExistOnnxRuntimeEnv = modemAiQoeController2.initModemAiEnv();
                            ModemAiQoeController modemAiQoeController3 = ModemAiQoeController.this;
                            if (modemAiQoeController3.mIsExistOnnxRuntimeEnv) {
                                modemAiQoeController3.processMovementEnterChange();
                            }
                        }
                    }
                    if (!z) {
                        ModemAiQoeController modemAiQoeController4 = ModemAiQoeController.this;
                        if (modemAiQoeController4.mIsExistOnnxRuntimeEnv) {
                            modemAiQoeController4.closeModemAiEnv();
                            ModemAiQoeController.this.processMovementExitChange();
                        }
                    }
                    break;
                case 4:
                case 7:
                case 12:
                case 13:
                default:
                    ModemAiQoeController.this.handleSubModelMessage(message);
                    break;
                case 5:
                    int defaultDataSlotId = ModemAiQoeController.this.getDefaultDataSlotId();
                    ModemAiQoeController modemAiQoeController5 = ModemAiQoeController.this;
                    if (defaultDataSlotId != modemAiQoeController5.mDdsPhoneId) {
                        modemAiQoeController5.processDefaultDataSlotChange(defaultDataSlotId);
                    }
                    ModemAiQoeController.log("EVENT_DATA_SLOT_CHANGED: mDdsPhoneId=" + ModemAiQoeController.this.mDdsPhoneId + " newDdsPhoneId=" + defaultDataSlotId);
                    ModemAiQoeController.this.mDdsPhoneId = defaultDataSlotId;
                    break;
                case 6:
                    int i = message.arg1;
                    ServiceState serviceState = (ServiceState) message.obj;
                    ModemAiQoeController.logD("EVENT_SERVICE_STATE_CHANGED phoneId=" + i);
                    if (serviceState != null && MiuiPhoneUtils.isValidatePhoneId(i)) {
                        ModemAiQoeController.this.mServiceState[i] = new ServiceState(serviceState);
                        ModemAiQoeController.this.processServiceStateChangeEvent(i, serviceState);
                    } else {
                        ModemAiQoeController.log("processServiceStateChangeEvent invalid params!!!");
                    }
                    break;
                case 8:
                    if (ModemAiQoeController.this.isNecessaryCase()) {
                        ModemAiQoeController.this.processModemQoeMetricsForPeriod();
                    }
                    ModemAiQoeController.this.periodicGetModemQoeMetrics();
                    break;
                case 9:
                    ModemAiQoeController.this.processForegroundInfoEvent();
                    break;
                case 10:
                    boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                    ModemAiQoeController modemAiQoeController6 = ModemAiQoeController.this;
                    if (zBooleanValue != modemAiQoeController6.mIsMobileDataOn) {
                        modemAiQoeController6.mIsMobileDataOn = zBooleanValue;
                        ModemAiQoeController.log("EVENT_MOBILE_STATE_CHANGED: mIsMobileDataOn:" + ModemAiQoeController.this.mIsMobileDataOn);
                        ModemAiQoeController.this.processMobileDataChangeEvent();
                    }
                    break;
                case 11:
                    boolean zBooleanValue2 = ((Boolean) message.obj).booleanValue();
                    ModemAiQoeController modemAiQoeController7 = ModemAiQoeController.this;
                    if (zBooleanValue2 != modemAiQoeController7.mIsWifiDataOn) {
                        modemAiQoeController7.mIsWifiDataOn = zBooleanValue2;
                        ModemAiQoeController.log("EVENT_WIFI_STATE_CHANGED: mIsWifiDataOn:" + ModemAiQoeController.this.mIsWifiDataOn);
                        ModemAiQoeController.this.processWifiDataChangeEvent();
                    }
                    break;
                case 14:
                    ModemAiQoeController.this.registerSceneAwareCallback();
                    break;
            }
        }
    }

    protected synchronized void processForegroundInfoEvent() {
    }

    public void periodicGetModemQoeMetrics() {
        if (this.mWorkHandler.hasMessages(8)) {
            this.mWorkHandler.removeMessages(8);
        }
        Handler handler = this.mWorkHandler;
        handler.sendMessageDelayed(handler.obtainMessage(8), this.mCollectGap * 1000);
    }

    protected void processModemQoeMetricsForPeriod() {
        logD("ModemAiQoeController: processModemQoeMetricsForPeriod");
        if (mIsUpdate) {
            return;
        }
        mIsUpdate = true;
        ModemDataCollectorBase modemDataCollectorBase = ModemDataCollectorBase.getInstance();
        if (modemDataCollectorBase != null) {
            modemDataCollectorBase.updateModemAiData();
        }
        this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.network.ModemAiQoeController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ModemAiQoeController.mIsUpdate = false;
            }
        }, 1000L);
    }

    protected void processMovementEnterChange() {
        log("ModemAiQoeController: processMovementEnterChange");
        registerActivityChangeListener();
        ModemDataCollectorBase modemDataCollectorBase = ModemDataCollectorBase.getInstance();
        if (modemDataCollectorBase != null) {
            modemDataCollectorBase.startModemCallback();
        }
        this.mWorkHandler.obtainMessage(8).sendToTarget();
    }

    protected void processMovementExitChange() {
        log("ModemAiQoeController: processMovementExitChange");
        unRegisterActivityChangeListener();
        ModemDataCollectorBase modemDataCollectorBase = ModemDataCollectorBase.getInstance();
        if (modemDataCollectorBase != null) {
            modemDataCollectorBase.stopModemCallback();
        }
        this.mWorkHandler.removeMessages(8);
    }

    protected void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        int i2;
        if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) && i == (i2 = this.mDdsPhoneId) && this.mServiceState[i2] != null) {
            updateServiceState(serviceState);
        }
    }

    /* JADX WARN: Code duplicated, block: B:8:0x0016  */
    private void updateServiceState(ServiceState serviceState) {
        boolean z;
        if (serviceState == null) {
            return;
        }
        int dataRegistrationState = serviceState.getDataRegistrationState();
        if (this.mNetworkMetricsController.isLteNrNwType(serviceState.getDataNetworkType())) {
            z = true;
            if (dataRegistrationState == 1) {
                log("updateServiceState: not reg lte/nr!");
                z = false;
            }
        } else {
            log("updateServiceState: not reg lte/nr!");
            z = false;
        }
        this.mIsRegsiterLteNr = z;
        this.mPlmn = serviceState.getOperatorNumeric();
    }

    protected void processDefaultDataSlotChange(int i) {
        ServiceState serviceState;
        log("processDefaultDataSlotChange: ddsPhoneId=" + i);
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || (serviceState = this.mServiceState[i]) == null) {
            return;
        }
        updateServiceState(serviceState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNecessaryCase() {
        logD("isNecessaryCase mIsExistOnnxRuntimeEnv=" + this.mIsExistOnnxRuntimeEnv + "mIsMobileDataOn=" + this.mIsMobileDataOn + "mIsWifiDataOn=" + this.mIsWifiDataOn + "mIsRegsiterLteNr=" + this.mIsRegsiterLteNr);
        return this.mIsExistOnnxRuntimeEnv && this.mIsMobileDataOn && !this.mIsWifiDataOn && this.mIsRegsiterLteNr && !isModemDataCollectorInErrorState();
    }

    private boolean isModemDataCollectorInErrorState() {
        ModemDataCollectorBase modemDataCollectorBase = ModemDataCollectorBase.getInstance();
        if (modemDataCollectorBase == null) {
            return false;
        }
        return modemDataCollectorBase.isInErrorState();
    }

    private static synchronized boolean loadOnnxRuntime() {
        if (mOnnxRuntimeLoaded) {
            return true;
        }
        if (mOnnxRuntimeLoadRetryCount >= 1) {
            return false;
        }
        try {
            log("loadOnnxRuntime");
            System.loadLibrary("onnxruntime4j_jni");
            mOnnxRuntimeLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            log("loadOnnxRuntime failed, retryCount=" + mOnnxRuntimeLoadRetryCount + " " + e);
            mOnnxRuntimeLoadRetryCount = mOnnxRuntimeLoadRetryCount + 1;
        }
        return mOnnxRuntimeLoaded;
    }

    protected boolean initModemAiEnv() {
        try {
            if (this.mOnnxModelPath.isEmpty()) {
                log("initModemAiEnv onnxModelPath is empty");
                return false;
            }
            if (!loadOnnxRuntime()) {
                log("initModemAiEnv: onnxRuntime load failed");
                return false;
            }
            OrtEnvironment environment = OrtEnvironment.getEnvironment();
            this.mOnnxEnv = environment;
            this.mOnnxSession = environment.createSession(this.mOnnxModelPath, new OrtSession.SessionOptions());
            log("initModemAiEnv: init model success");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void closeModemAiEnv() {
        try {
            OrtSession ortSession = this.mOnnxSession;
            if (ortSession != null) {
                ortSession.close();
                this.mOnnxSession = null;
            }
            OrtEnvironment ortEnvironment = this.mOnnxEnv;
            if (ortEnvironment != null) {
                ortEnvironment.close();
                this.mOnnxEnv = null;
            }
            this.mIsExistOnnxRuntimeEnv = false;
            log("closeModemAiEnv: close model success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isScreenOn() {
        NetworkSpeedTracker networkSpeedTracker = this.mNetworkSpeedTracker;
        if (networkSpeedTracker != null) {
            return networkSpeedTracker.getScreenOnState();
        }
        return false;
    }

    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    public enum ModemAiScene {
        DUMMY(0),
        WEAK_SIGNAL_PREDICTION(1),
        MOVEMENT_TCPIP_QOE_PREDICTION(2),
        MOVEMENT_MODEM_QOE_PREDICTION(3);

        private int value;

        ModemAiScene(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }

        public static boolean isEqual(ModemAiScene modemAiScene, int i) {
            return modemAiScene != null && modemAiScene.value == i;
        }
    }

    public boolean shouldRandomTriggerStatsData(double d) {
        double dNextDouble = this.mRandom.nextDouble();
        logD("shouldRandomTriggerStatsData random: " + dNextDouble);
        return dNextDouble < d;
    }

    public class EvaluationCriteria {
        private int truePositives = 0;
        private int falsePositives = 0;
        private int falseNegatives = 0;
        private int trueNegatives = 0;
        private int falsePositivesCase1 = 0;
        private int falsePositivesCase8 = 0;
        private int falseNegativesCase2 = 0;
        private int falseNegativesCase7 = 0;
        private int truePositivesCase4 = 0;
        private int truePositivesCase5 = 0;
        private int trueNegativesCase3 = 0;
        private int trueNegativesCase6 = 0;
        private int inferenceCount = 0;

        public EvaluationCriteria(ModemAiQoeController modemAiQoeController) {
        }

        public void update(boolean z, boolean z2, boolean z3) {
            if (z2 && z3) {
                this.truePositives++;
                if (z) {
                    this.truePositivesCase5++;
                } else {
                    this.truePositivesCase4++;
                }
            } else if (z2 && !z3) {
                this.falsePositives++;
                if (z) {
                    this.falsePositivesCase1++;
                } else {
                    this.falsePositivesCase8++;
                }
            } else if (!z2 && z3) {
                this.falseNegatives++;
                if (z) {
                    this.falseNegativesCase7++;
                } else {
                    this.falseNegativesCase2++;
                }
            } else if (!z2 && !z3) {
                this.trueNegatives++;
                if (z) {
                    this.trueNegativesCase3++;
                } else {
                    this.trueNegativesCase6++;
                }
            }
            this.inferenceCount++;
        }

        public double accuracy() {
            int i = this.inferenceCount;
            if (i == 0) {
                return 0.0d;
            }
            return ((double) ((i - this.falsePositives) - this.falseNegatives)) / ((double) i);
        }

        public double precision() {
            int i = this.truePositives;
            int i2 = this.falsePositives;
            if (i + i2 == 0) {
                return 0.0d;
            }
            return ((double) i) / ((double) (i + i2));
        }

        public double recall() {
            int i = this.truePositives;
            int i2 = this.falseNegatives;
            if (i + i2 == 0) {
                return 0.0d;
            }
            return ((double) i) / ((double) (i + i2));
        }

        public double f1Score() {
            double dPrecision = precision();
            double dRecall = recall();
            double d = dPrecision + dRecall;
            if (d == 0.0d) {
                return 0.0d;
            }
            return ((dPrecision * dRecall) * 2.0d) / d;
        }

        public void reset() {
            this.truePositives = 0;
            this.falsePositives = 0;
            this.falseNegatives = 0;
            this.trueNegatives = 0;
            this.falsePositivesCase1 = 0;
            this.falsePositivesCase8 = 0;
            this.falseNegativesCase2 = 0;
            this.falseNegativesCase7 = 0;
            this.truePositivesCase4 = 0;
            this.truePositivesCase5 = 0;
            this.trueNegativesCase3 = 0;
            this.trueNegativesCase6 = 0;
            this.inferenceCount = 0;
        }

        public String toString() {
            return "EvaluationCriteria{True Positives=" + this.truePositives + ", False Positives=" + this.falsePositives + ", False Negatives=" + this.falseNegatives + ", True Negatives=" + this.trueNegatives + ", falsePositivesCase1=" + this.falsePositivesCase1 + ", falsePositivesCase8 =" + this.falsePositivesCase8 + ", falseNegativesCase2 =" + this.falseNegativesCase2 + ", falseNegativesCase7 =" + this.falseNegativesCase7 + ", truePositivesCase4=" + this.truePositivesCase4 + ", truePositivesCase5=" + this.truePositivesCase5 + ", trueNegativesCase3=" + this.trueNegativesCase3 + ", trueNegativesCase6=" + this.trueNegativesCase6 + ", Accuracy=" + accuracy() + ", Precision=" + precision() + ", Recall=" + recall() + ", F1 Score=" + f1Score() + ", inferenceCount=" + this.inferenceCount + '}';
        }
    }

    public class RecoveryPolicyInfo {
        private int recoveryPolicy = -1;
        private int recoveryScene = -1;
        private long recoveryTime = -1;

        public RecoveryPolicyInfo(ModemAiQoeController modemAiQoeController) {
        }

        public void updateRecoveryPolicyInfo(int i, int i2, long j) {
            this.recoveryPolicy = i;
            this.recoveryScene = i2;
            this.recoveryTime = j;
        }

        public void clear() {
            this.recoveryPolicy = -1;
            this.recoveryScene = -1;
            this.recoveryTime = -1L;
        }

        public String toString() {
            return "RecoveryPolicyInfo{recoveryPolicy=" + this.recoveryPolicy + ", recoveryScene=" + this.recoveryScene + ", recoveryTime=" + this.recoveryTime + '}';
        }
    }

    protected static void log(String str) {
        Log.d(TAG, str);
    }

    protected static void logD(String str) {
        if (VDBG) {
            Log.d(TAG, str);
        }
    }
}
