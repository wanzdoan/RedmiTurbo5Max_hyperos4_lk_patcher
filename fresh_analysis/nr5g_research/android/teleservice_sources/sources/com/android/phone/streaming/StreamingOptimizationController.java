package com.android.phone.streaming;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.NetworkMetricsController;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.module.INetworkModule;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiuiCurrentStateMonitor;
import com.android.phone.utils.MiuiThreadUtil;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.telephony.DefaultSimManager;
import xiaomi.hypercomm.HyperCommManager;
import xiaomi.hypercomm.SceneAwareCallback;

/* JADX INFO: loaded from: classes.dex */
public abstract class StreamingOptimizationController {
    protected static volatile StreamingOptimizationController mInstance;
    private static final HashMap sLocalLogs = new HashMap();
    protected Context mContext;
    private final BaseEvent.IEventListener mForegroundInfoListener;
    private NetworkMetricsController mNetworkMetricsController;
    private StreamingOptimizationExecutor mOptExecutor;
    private StreamingQoEHandler mQoEHandler;
    private final BroadcastReceiver mReceiver;
    private SceneAwareCallback mSceneAwareCallback;
    protected final Handler mWorkHandler;
    private final List mStreamOptAppList = HyperOSCustFeatureResolve.getStringArray("config_stream_opt_packages");
    private final AtomicBoolean mRegistered = new AtomicBoolean(false);
    private final AtomicBoolean mIsStreamingSceneActive = new AtomicBoolean(false);
    private final AtomicBoolean mIsRoamingDisableFiveG = new AtomicBoolean(false);
    protected final StreamingOptimizationState mOptStateMgr = new StreamingOptimizationState();
    private final StreamingOptimizationStats mStats = StreamingOptimizationStats.getInstance();

    protected abstract void executeDataStallEscape(int i);

    protected void executeDataStallEscapeNormal(int i) {
    }

    protected void handlerStreamingSceneChanged(boolean z) {
    }

    protected abstract void processPlatformMessage(Message message);

    protected abstract void resetDataStallEscape(int i);

    static void localLog(String str, String str2) {
        String str3 = "StreamingOptimizationController." + str;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str3)) {
                    map.put(str3, new LocalLog(100));
                }
                ((LocalLog) map.get(str3)).log(str2);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d("StreamingOptimizationController", str + " " + str2);
    }

    public static StreamingOptimizationController getInstance() {
        return mInstance;
    }

    public static void setResidentCellProvider(Supplier supplier) {
        StreamingOptimizationController streamingOptimizationController = mInstance;
        if (streamingOptimizationController != null) {
            streamingOptimizationController.mOptStateMgr.getSceneContext().setResidentCellProvider(supplier);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(BaseEvent baseEvent) {
        final ForegroundInfo foregroundInfo;
        if (!(baseEvent instanceof DeviceStateEvent.AppStateEvent) || (foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo()) == null) {
            return;
        }
        this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.streaming.StreamingOptimizationController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0(foregroundInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ForegroundInfo foregroundInfo) {
        onForegroundAppChanged(foregroundInfo.mForegroundPackageName);
    }

    protected StreamingOptimizationController() {
        Handler handler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.streaming.StreamingOptimizationController.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 0) {
                    if (StreamingOptimizationController.this.mOptExecutor != null) {
                        StreamingOptimizationController.this.mOptExecutor.checkOptEffectiveness(message.arg1);
                        return;
                    }
                    return;
                }
                if (i == 1) {
                    Object obj = message.obj;
                    if (obj instanceof NetworkInfo) {
                        StreamingOptimizationController.this.onDataStateChange((NetworkInfo) obj);
                        return;
                    }
                    return;
                }
                if (i == 2) {
                    StreamingOptimizationController streamingOptimizationController = StreamingOptimizationController.this;
                    int ddsSlot = streamingOptimizationController.mOptStateMgr.getSceneContext().getDdsSlot();
                    DefaultSimManager.getInstance();
                    streamingOptimizationController.onDataSlotChanged(ddsSlot, DefaultSimManager.getDefaultDataSlotId());
                    return;
                }
                if (i == 4) {
                    StreamingOptimizationController.this.resetAllOptimizations();
                    return;
                }
                switch (i) {
                    case 9:
                        if (StreamingOptimizationController.this.mOptExecutor != null) {
                            StreamingOptimizationController.this.mOptExecutor.restore5GAfterOpt2(message.arg1);
                        }
                        break;
                    case 10:
                        StreamingOptimizationController.this.onHitHstLineStatusChanged(message.obj);
                        break;
                    case 11:
                        if (StreamingOptimizationController.this.mOptExecutor != null) {
                            StreamingOptimizationController.this.mOptExecutor.evaluateCollectedScores(message.arg1);
                        }
                        break;
                    default:
                        try {
                            StreamingOptimizationController.this.processPlatformMessage(message);
                        } catch (Exception e) {
                            Log.e("StreamingOptimizationController", "processPlatformMessage failed", e);
                            return;
                        }
                        break;
                }
            }
        };
        this.mWorkHandler = handler;
        this.mForegroundInfoListener = new BaseEvent.IEventListener() { // from class: com.android.phone.streaming.StreamingOptimizationController$$ExternalSyntheticLambda1
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$1(baseEvent);
            }
        };
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.phone.streaming.StreamingOptimizationController.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) {
                    return;
                }
                if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(intent.getAction())) {
                    StreamingOptimizationController.this.onActiveDataSlotIdChanged();
                    return;
                }
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                    Log.d("StreamingOptimizationController", "onDataConnectionStateChanged");
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    if (networkInfo == null || networkInfo.getType() != 0) {
                        return;
                    }
                    StreamingOptimizationController.this.mWorkHandler.removeMessages(1);
                    Handler handler2 = StreamingOptimizationController.this.mWorkHandler;
                    handler2.sendMessageDelayed(handler2.obtainMessage(1, networkInfo), MirrorDesktopHelper.TIMEOUT_MILLIS);
                    return;
                }
                if ("com.android.phone.intent.action.MOVELIKE_STATE_CHANGED".equals(intent.getAction())) {
                    Log.d("StreamingOptimizationController", "onMoveLikeStateChanged");
                    StreamingOptimizationController.this.mOptStateMgr.getSceneContext().setDynamicScene(intent.getBooleanExtra("isMovelikeState", false));
                } else if ("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED".equals(intent.getAction())) {
                    Log.d("StreamingOptimizationController", "onHitHstLineStateChanged");
                    StreamingOptimizationController.this.mWorkHandler.obtainMessage(10, intent.getSerializableExtra("mHitHstLineStatus")).sendToTarget();
                }
            }
        };
        this.mContext = PhoneGlobals.getInstance();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        handler.postDelayed(new Runnable() { // from class: com.android.phone.streaming.StreamingOptimizationController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.initCommState();
            }
        }, 3000L);
    }

    public static void init() {
        new StreamingCloudConfigHelper(new StreamingCloudConfigHelper.OnCloudConfigChangeListener() { // from class: com.android.phone.streaming.StreamingOptimizationController$$ExternalSyntheticLambda0
            @Override // com.android.phone.streaming.StreamingCloudConfigHelper.OnCloudConfigChangeListener
            public final void onCloudConfigChanged(boolean z, boolean z2, boolean z3) {
                StreamingOptimizationController.m4124$r8$lambda$vrlzdhZ5ZBXMtlcyWYUWmj4AdA(z, z2, z3);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$vrlzdhZ5ZBXMtlcyWYUW-mj4AdA, reason: not valid java name */
    public static /* synthetic */ void m4124$r8$lambda$vrlzdhZ5ZBXMtlcyWYUWmj4AdA(boolean z, boolean z2, boolean z3) {
        synchronized (StreamingOptimizationController.class) {
            try {
                if (mInstance == null && z) {
                    if (ModemProvider.IS_QCOM) {
                        QcStreamingOptimizationImpl.make();
                    }
                    if (ModemProvider.IS_MTK) {
                        MtkStreamingOptimizationImpl.make();
                    }
                } else if (mInstance != null && !z) {
                    mInstance.mWorkHandler.removeCallbacksAndMessages(null);
                    mInstance.mContext.unregisterReceiver(mInstance.mReceiver);
                    mInstance.mStats.unregisterStallTimeObserver();
                    mInstance.unregisterHyperCommCallback();
                    EventHubManager.getInstance().unregisterListener(mInstance.mForegroundInfoListener);
                    mInstance.resetAllOptimizations();
                    mInstance = null;
                    Log.i("StreamingOptimizationController", "feature is disabled");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void initCommState() {
        initCallback();
        registerCommonListener();
        this.mOptStateMgr.getSceneContext().setDdsSlot(MiuiPhoneUtils.getActiveDataPhoneId());
        int ddsSlot = this.mOptStateMgr.getSceneContext().getDdsSlot();
        if (ddsSlot >= 0) {
            this.mOptStateMgr.getSceneContext().setMobileDataConnected(MiuiPhoneUtils.isMobileDataConnected(ddsSlot));
        }
        this.mOptStateMgr.getSceneContext().setAppType(-1);
        Log.i("StreamingOptimizationController", "initCommState done, current DDS slot=" + this.mOptStateMgr.getSceneContext().getDdsSlot());
    }

    private void registerCommonListener() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.MOVELIKE_STATE_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        this.mStats.registerStallTimeObserver(this.mContext, this.mWorkHandler);
        registerHyperCommCallback();
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", this.mForegroundInfoListener);
        String foregroundPackageName = MiuiCurrentStateMonitor.getInstance().getForegroundPackageName();
        if (foregroundPackageName != null) {
            onForegroundAppChanged(foregroundPackageName);
        }
    }

    private void initCallback() {
        this.mOptExecutor = new StreamingOptimizationExecutor(this.mContext, this.mWorkHandler, this.mOptStateMgr, this.mStats, new StreamingOptimizationExecutor.ExecutorCallback() { // from class: com.android.phone.streaming.StreamingOptimizationController.2
            @Override // com.android.phone.streaming.StreamingOptimizationExecutor.ExecutorCallback
            public void executeDataStallEscape(int i) {
                StreamingOptimizationController.this.executeDataStallEscape(i);
            }

            @Override // com.android.phone.streaming.StreamingOptimizationExecutor.ExecutorCallback
            public void executeDataStallEscapeNormal(int i) {
                StreamingOptimizationController.this.executeDataStallEscapeNormal(i);
            }

            @Override // com.android.phone.streaming.StreamingOptimizationExecutor.ExecutorCallback
            public void resetDataStallEscape(int i) {
                StreamingOptimizationController.this.resetDataStallEscape(i);
            }

            @Override // com.android.phone.streaming.StreamingOptimizationExecutor.ExecutorCallback
            public void set5GNetworkCapability(int i, boolean z) {
                StreamingOptimizationController.this.set5GNetworkCapability(i, z);
            }
        });
        this.mQoEHandler = new StreamingQoEHandler(this.mOptStateMgr, new StreamingQoEHandler.QoECallback() { // from class: com.android.phone.streaming.StreamingOptimizationController.3
            @Override // com.android.phone.streaming.StreamingQoEHandler.QoECallback
            public void onCongestionReason(int i, int i2, int i3, int i4) {
                if (StreamingOptimizationController.this.mOptExecutor != null) {
                    StreamingOptimizationController.this.mOptExecutor.handleCongestionReason(i, i2, i3, i4);
                }
            }

            @Override // com.android.phone.streaming.StreamingQoEHandler.QoECallback
            public void onNonCongestionReason(int i, int i2, int i3, int i4) {
                if (StreamingOptimizationController.this.mOptExecutor != null) {
                    StreamingOptimizationController.this.mOptExecutor.handleNonCongestionReason(i, i2, i3, i4);
                }
            }

            @Override // com.android.phone.streaming.StreamingQoEHandler.QoECallback
            public void onScoreForStableCell(int i) {
                if (StreamingOptimizationController.this.mOptExecutor != null) {
                    StreamingOptimizationController.this.mOptExecutor.evaluateStableCellByScore(i);
                }
            }
        });
        Log.i("StreamingOptimizationController", "initCallback done, mOptExecutor and mQoEHandler initialized");
    }

    public boolean registerHyperCommCallback() {
        if (this.mRegistered.get()) {
            Log.w("StreamingOptimizationController", "Already registered");
            return false;
        }
        HyperCommManager hyperCommManager = HyperCommManager.getDefault();
        if (hyperCommManager == null) {
            Log.e("StreamingOptimizationController", "HyperCommManager is not available");
            return false;
        }
        HyperCommSceneAwareCallback hyperCommSceneAwareCallback = new HyperCommSceneAwareCallback(new HandlerExecutor(this.mWorkHandler));
        this.mSceneAwareCallback = hyperCommSceneAwareCallback;
        boolean zRegisterSceneAwareCallback = hyperCommManager.registerSceneAwareCallback(hyperCommSceneAwareCallback);
        if (!zRegisterSceneAwareCallback) {
            this.mSceneAwareCallback = null;
        }
        this.mRegistered.set(zRegisterSceneAwareCallback);
        Log.i("StreamingOptimizationController", "registered to xm.hypercomm result: " + zRegisterSceneAwareCallback);
        return zRegisterSceneAwareCallback;
    }

    public boolean unregisterHyperCommCallback() {
        if (!this.mRegistered.get() || this.mSceneAwareCallback == null) {
            Log.w("StreamingOptimizationController", "Not registered");
            return false;
        }
        boolean zUnregisterSceneAwareCallback = HyperCommManager.getDefault().unregisterSceneAwareCallback(this.mSceneAwareCallback);
        if (zUnregisterSceneAwareCallback) {
            this.mRegistered.set(false);
            this.mSceneAwareCallback = null;
        }
        Log.i("StreamingOptimizationController", "unregistered from xm.hypercomm result: " + zUnregisterSceneAwareCallback);
        return zUnregisterSceneAwareCallback;
    }

    private void onForegroundAppChanged(String str) {
        List list = this.mStreamOptAppList;
        boolean z = list != null && list.contains(str);
        int i = z ? 1 : -1;
        this.mOptStateMgr.getSceneContext().setAppType(i);
        this.mStats.onWhitelistForegroundChanged(z);
        Log.d("StreamingOptimizationController", "onForegroundAppChanged: pkg=" + str + ", isStreamApp=" + z + ", appType=" + i);
        checkStreamingScene();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onActiveDataSlotIdChanged() {
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        Log.d("StreamingOptimizationController", "onActiveDataSlotIdChanged: newDds=" + activeDataPhoneId);
        if (activeDataPhoneId != this.mOptStateMgr.getSceneContext().getDdsSlot()) {
            this.mWorkHandler.removeMessages(2);
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(2), MirrorDesktopHelper.TIMEOUT_MILLIS);
        }
    }

    class HyperCommSceneAwareCallback extends SceneAwareCallback implements SceneAwareCallback.NetworkQoeListener, SceneAwareCallback.HighSpeedTrainStateListener, SceneAwareCallback.SubwayStateListener {
        HyperCommSceneAwareCallback(Executor executor) {
            super(executor);
        }

        public void onNetworkQoeChanged(int i, int i2, int i3, int i4, int i5) {
            Log.i("StreamingOptimizationController", "onNetworkQoeChanged: slotId=" + i + ", pci=" + i2 + ", appType=" + i3 + ", reason=" + i4 + ", networkScore=" + i5);
            StreamingOptimizationController.this.mStats.onNetworkScoreChanged(i5);
            int i6 = i;
            int i7 = i3;
            int i8 = i5;
            StreamingOptimizationController.this.mQoEHandler.handleNetworkQoeChanged(i6, i2, i7, i4, i8);
            if (StreamingOptimizationController.this.mNetworkMetricsController == null || i4 != 0) {
                return;
            }
            if (i7 == 1) {
                StreamingOptimizationController.this.mNetworkMetricsController.onNetworkQoeChanged(i6, i2, i7, i4, i8);
                StreamingOptimizationController.this.mNetworkMetricsController.onBasementQoeChanged(i6, i8);
            } else if (i7 == 2) {
                StreamingOptimizationController.this.mNetworkMetricsController.onBasementQoeChanged(i6, i8);
            }
            if (i8 < 0 || i8 >= 80) {
                i6 = i6;
                i7 = i7;
                i8 = i8;
                i6 = i6;
                i7 = i7;
                i8 = i8;
                if (i7 != -1) {
                    return;
                }
            }
            i6 = i6;
            i7 = i7;
            i8 = i8;
            if (StreamingOptimizationController.this.mNetworkMetricsController.isSubwayRunning()) {
                StreamingOptimizationController.this.mNetworkMetricsController.onSubwayQoeChanged(i6, i7, i8);
            }
        }

        public void onSubwayStateChanged(boolean z, boolean z2) {
            Log.i("StreamingOptimizationController", "onSubwayStateChanged: subwayState=" + z + ", isDomestic=" + z2);
            StreamingOptimizationController.this.mOptStateMgr.getSceneContext().setSubway(z);
            StreamingOptimizationController.this.mOptStateMgr.getSceneContext().setOverSeaSubway(z && !z2);
        }

        public void onHighSpeedTrainStateChanged(boolean z) {
            Log.i("StreamingOptimizationController", "onHighSpeedTrainStateChanged: hstState=" + z);
            StreamingOptimizationController.this.mOptStateMgr.getSceneContext().setHighSpeedTrain(z);
        }
    }

    protected void set5GNetworkCapability(int i, boolean z) {
        if (MiuiPhoneUtils.isDuringCall()) {
            Log.i("StreamingOptimizationController", "set5GNetworkCapability: during call, skip");
            return;
        }
        Log.d("StreamingOptimizationController", "set5GNetworkCapability: phoneId=" + i + ", enable=" + z);
        INetworkModule iNetworkModule = (INetworkModule) ModuleDelegate.get(INetworkModule.class);
        if (z) {
            if (!MiuiPhoneUtils.isDataRoaming(i) && !this.mIsRoamingDisableFiveG.get()) {
                iNetworkModule.enableNetworkRecoverySa(true, i);
            }
            iNetworkModule.enableNetworkRecoveryNsa(true, i);
            this.mIsRoamingDisableFiveG.set(false);
            this.mOptStateMgr.getAuxState().set5GDisabled(false);
            return;
        }
        if (MiuiPhoneUtils.isDataRoaming(i)) {
            iNetworkModule.enableNetworkRecoveryNsa(false, i);
            this.mIsRoamingDisableFiveG.set(true);
        } else {
            iNetworkModule.enableNetworkRecoverySa(false, i);
            if (!FiveGUtils.isXmRadioSupported()) {
                iNetworkModule.enableNetworkRecoveryNsa(false, i);
            }
        }
        this.mOptStateMgr.getAuxState().set5GDisabled(true);
    }

    private void cleanupOptimization(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            Log.i("StreamingOptimizationController", "Cleaning up optimization for slot " + i);
            if (this.mOptStateMgr.getAuxState().is5GDisabled()) {
                set5GNetworkCapability(i, true);
            }
            if (this.mOptStateMgr.getOptState() == 1) {
                resetDataStallEscape(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetAllOptimizations() {
        Log.i("StreamingOptimizationController", "Resetting all optimizations");
        localLog("sceneExit", "ddsSlot=" + this.mOptStateMgr.getSceneContext().getDdsSlot() + ", optState=" + this.mOptStateMgr.getOptState());
        this.mIsStreamingSceneActive.compareAndSet(true, false);
        this.mWorkHandler.removeMessages(9);
        this.mWorkHandler.removeMessages(6);
        this.mWorkHandler.removeMessages(5);
        this.mWorkHandler.removeMessages(11);
        this.mWorkHandler.removeMessages(7);
        int ddsSlot = this.mOptStateMgr.getSceneContext().getDdsSlot();
        cleanupOptimization(ddsSlot);
        resetDataStallEscape(ddsSlot);
        this.mWorkHandler.sendEmptyMessage(8);
        this.mOptStateMgr.resetOptimizationState();
        handlerStreamingSceneChanged(false);
        this.mStats.onExitScene();
    }

    private void checkStreamingScene() {
        if (this.mWorkHandler.hasMessages(2)) {
            this.mWorkHandler.removeMessages(2);
            int ddsSlot = this.mOptStateMgr.getSceneContext().getDdsSlot();
            cleanupOptimization(ddsSlot);
            resetDataStallEscape(ddsSlot);
            this.mOptStateMgr.resetOptimizationState();
            StreamingSceneContext sceneContext = this.mOptStateMgr.getSceneContext();
            DefaultSimManager.getInstance();
            sceneContext.setDdsSlot(DefaultSimManager.getDefaultDataSlotId());
        }
        boolean z = this.mOptStateMgr.getSceneContext().getAppType() == 1 && this.mOptStateMgr.getSceneContext().isMobileDataConnected();
        if (z && !this.mIsStreamingSceneActive.get()) {
            processSceneEnter();
            return;
        }
        if (z && this.mIsStreamingSceneActive.get()) {
            this.mWorkHandler.removeMessages(4);
        } else {
            if (z || !this.mIsStreamingSceneActive.get() || this.mWorkHandler.hasMessages(4)) {
                return;
            }
            Handler handler = this.mWorkHandler;
            handler.sendMessageDelayed(handler.obtainMessage(4), 30000L);
        }
    }

    private void processSceneEnter() {
        this.mWorkHandler.removeMessages(4);
        handlerStreamingSceneChanged(true);
        this.mStats.onEnterScene();
        this.mIsStreamingSceneActive.compareAndSet(false, true);
        localLog("sceneEnter", "ddsSlot=" + this.mOptStateMgr.getSceneContext().getDdsSlot() + ", appType=" + this.mOptStateMgr.getSceneContext().getAppType());
    }

    public void onDataSlotChanged(int i, int i2) {
        Log.i("StreamingOptimizationController", "onDataSlotChanged: oldSlot=" + i + ", newSlot=" + i2);
        localLog("ddsSwitch", "oldSlot=" + i + ", newSlot=" + i2);
        cleanupOptimization(i);
        resetDataStallEscape(i);
        this.mOptStateMgr.resetOptimizationState();
        this.mOptStateMgr.getSceneContext().setDdsSlot(i2);
        checkStreamingScene();
    }

    public void onDataStateChange(NetworkInfo networkInfo) {
        Log.i("StreamingOptimizationController", "onDataStateChange: networkInfo=" + networkInfo);
        if (networkInfo != null && networkInfo.getType() == 0) {
            boolean zIsMobileDataConnected = this.mOptStateMgr.getSceneContext().isMobileDataConnected();
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED && !zIsMobileDataConnected) {
                this.mOptStateMgr.getSceneContext().setMobileDataConnected(true);
            } else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED && zIsMobileDataConnected) {
                this.mOptStateMgr.getSceneContext().setMobileDataConnected(false);
                cleanupOptimization(this.mOptStateMgr.getSceneContext().getDdsSlot());
                this.mOptStateMgr.resetOptimizationState();
            }
        }
        checkStreamingScene();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onHitHstLineStatusChanged(Object obj) {
        if (obj instanceof Map) {
            try {
                Map map = (Map) obj;
                if (map.isEmpty()) {
                    return;
                }
                this.mOptStateMgr.getSceneContext().setHstLineName(TextUtils.join(",", map.keySet()));
                Log.d("StreamingOptimizationController", "onHitHstLineStatusChanged: lineDirections = " + map);
            } catch (ClassCastException e) {
                Log.e("StreamingOptimizationController", "Invalid lineDirections type", e);
            }
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("StreamingOptimizationController dump start------");
        printWriter.println("[Controller]");
        printWriter.println("  registered=" + this.mRegistered.get() + ", sceneActive=" + this.mIsStreamingSceneActive.get() + ", roamingDisable5G=" + this.mIsRoamingDisableFiveG.get());
        this.mOptStateMgr.dump(printWriter);
        StreamingOptimizationExecutor streamingOptimizationExecutor = this.mOptExecutor;
        if (streamingOptimizationExecutor != null) {
            streamingOptimizationExecutor.dump(printWriter);
        }
        printWriter.println("[LocalLog]");
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    printWriter.println("  " + ((String) entry.getKey()));
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.println("StreamingOptimizationController dump end------");
        printWriter.flush();
    }
}
