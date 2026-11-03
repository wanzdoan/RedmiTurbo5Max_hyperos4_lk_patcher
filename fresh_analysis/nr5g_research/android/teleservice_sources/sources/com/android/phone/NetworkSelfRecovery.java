package com.android.phone;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.Log;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.NetworkSceneEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class NetworkSelfRecovery extends Handler {
    private static NetworkSelfRecovery sInstance;
    private final Context mContext;
    private NetworkMetricsController mNetworkMetricsController;
    private ContentResolver mResolver;
    private HashSet mCGISet = new HashSet();
    private TeleCallBack mTeleCallBack = null;
    private ServiceState[] mServiceState = new ServiceState[MiuiPhoneUtils.PHONE_COUNT];
    private long[] mLastDepriSaTime = {0, 0};
    private int[] mScenePrev = {-1, -1};
    private int[] mCausePrev = {-1, -1};
    private String mCGI = "";
    private String mFivegCGI = "";
    private ArrayList mActionListener = new ArrayList();
    private boolean mIsSubway = false;
    private boolean mIsHst = false;
    private boolean mIsHighSpeedScene = false;
    private SensorStateCallBack mSensorStateCallBack = null;
    private RecoveryStrategyHandler mDsRecoveryHandler = new RecoveryStrategyHandler();

    public static synchronized NetworkSelfRecovery make(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new NetworkSelfRecovery(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public static synchronized NetworkSelfRecovery getInstance() {
        return sInstance;
    }

    public NetworkSelfRecovery(Context context) {
        this.mContext = context;
        this.mResolver = context.getContentResolver();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        registerTelephonyInfoCallBack();
        if (MiuiPhoneUtils.isSupportMtkHighSpeedSbpOpt()) {
            regSensorStateCallBack();
            delayToResetSbpOosHighRatOpt();
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

    class TeleCallBack implements TelephonyStatAdapter.ICallBack {
        TeleCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) throws Throwable {
            NetworkSelfRecovery.this.processServiceStateChanged(i, serviceState);
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            this.mDsRecoveryHandler.doRecovery(message.arg1, message.arg2, (NetworkMetricsController.Scene) message.obj);
            return;
        }
        if (i == 2) {
            localLog("ESA", "s[" + NetworkMetricsController.Scene.TIMEOUT.getValue() + "]");
            this.mDsRecoveryHandler.doRecovery(message.arg1, message.arg2, (NetworkMetricsController.Scene) message.obj);
            return;
        }
        if (i == 3) {
            localLog("ENSA", "s[" + NetworkMetricsController.Scene.TIMEOUT.getValue() + "]");
            this.mDsRecoveryHandler.doRecovery(message.arg1, message.arg2, (NetworkMetricsController.Scene) message.obj);
            return;
        }
        if (i == 4) {
            if (this.mIsSubway || this.mIsHst) {
                return;
            }
            setSbpOosHighRatOptState(true);
            return;
        }
        log("Unhandled message with number:" + message.what);
    }

    class RecoveryStrategyHandler {
        private long mTimeLastNsaOffTimeOutMs;
        private long[] mTimeLastRecoveryStartMs = new long[MiuiPhoneUtils.PHONE_COUNT];
        private long mTimeLastSaOffTimeOutMs;

        public RecoveryStrategyHandler() {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                this.mTimeLastRecoveryStartMs[i] = 0;
            }
            reset();
        }

        /* JADX WARN: Code duplicated, block: B:23:0x0027 A[RETURN] */
        private int getRecoveryByCause(int i, int i2) {
            if (i == 3) {
                return getRecoveryForLinkPoor(getRecoveryAction(i2));
            }
            if (i == 4) {
                return 1;
            }
            if (i == 9) {
                return 10;
            }
            switch (i) {
                case 53:
                    return 5;
                case 54:
                case 55:
                    if (getRecoveryAction(i2) == 5) {
                        return 4;
                    }
                    return 0;
                case 56:
                    return 8;
                case 57:
                    return 9;
                default:
                    switch (i) {
                        case 103:
                            return 3;
                        case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                        case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                            if (getRecoveryAction(i2) == 3) {
                                return 2;
                            }
                            return 0;
                        default:
                            return 0;
                    }
            }
        }

        private int getRecoveryForLinkPoor(int i) {
            if (i == 0) {
                NetworkSelfRecovery.this.localLog("REA", "s[" + NetworkMetricsController.Scene.LINK_QOS.getValue() + "] OR s[" + NetworkMetricsController.Scene.CELLCHANGED.getValue() + "]");
                return 6;
            }
            if (i == 3) {
                NetworkSelfRecovery.this.localLog("ENSA", " s[" + NetworkMetricsController.Scene.CELLCHANGED.getValue() + "]");
                return 2;
            }
            if (i != 5) {
                return 6;
            }
            NetworkSelfRecovery.this.localLog("ESA", " s[" + NetworkMetricsController.Scene.CELLCHANGED.getValue() + "]");
            return 4;
        }

        private void saveCellGlobalId(String str) {
            NetworkSelfRecovery.this.mFivegCGI = str;
        }

        public void reset() {
            this.mTimeLastSaOffTimeOutMs = 0L;
            this.mTimeLastNsaOffTimeOutMs = 0L;
            saveCellGlobalId("");
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                putRecoveryAction(0, i);
                this.mTimeLastRecoveryStartMs[i] = 0;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isInCellLocLingeringTime() {
            if (this.mTimeLastSaOffTimeOutMs != 0 && SystemClock.elapsedRealtime() - this.mTimeLastSaOffTimeOutMs < 900000) {
                NetworkSelfRecovery.log("skip cell location changed while SA lingering time");
                return true;
            }
            if (this.mTimeLastNsaOffTimeOutMs == 0 || SystemClock.elapsedRealtime() - this.mTimeLastNsaOffTimeOutMs >= 900000) {
                return false;
            }
            NetworkSelfRecovery.log("skip cell location changed while Nsa lingering time");
            return true;
        }

        private long getElapsedTimeSinceRecoveryMs(int i) {
            return SystemClock.elapsedRealtime() - this.mTimeLastRecoveryStartMs[i];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getRecoveryAction(int i) {
            int i2 = Settings.System.getInt(NetworkSelfRecovery.this.mResolver, "radio.recovery.strategy" + i, 0);
            NetworkSelfRecovery.log("getRecoveryAction: " + i2 + ", phoneId " + i);
            return i2;
        }

        private void putRecoveryAction(int i, int i2) {
            Settings.System.putInt(NetworkSelfRecovery.this.mResolver, "radio.recovery.strategy" + i2, i);
            NetworkSelfRecovery.log("putRecoveryAction: " + i);
        }

        private boolean checkRecovery(boolean z, int i) {
            if (!z && getElapsedTimeSinceRecoveryMs(i) < 15000) {
                NetworkSelfRecovery.this.localLog("checkRecovery", "skip back to back data stall recovery");
                return false;
            }
            if (!MiuiPhoneUtils.isDuringCall()) {
                return true;
            }
            NetworkSelfRecovery.this.localLog("checkRecovery", "skip data stall recovery as there is an active call");
            return false;
        }

        private void triggerRecovery(int i, int i2, NetworkMetricsController.Scene scene) {
            this.mTimeLastRecoveryStartMs[i2] = SystemClock.elapsedRealtime();
            NetworkSelfRecovery networkSelfRecovery = NetworkSelfRecovery.this;
            networkSelfRecovery.sendMessage(networkSelfRecovery.obtainMessage(1, i, i2, scene));
        }

        public void doRecovery(int i, int i2, NetworkMetricsController.Scene scene) {
            int recoveryByCause = getRecoveryByCause(i, i2);
            NetworkSelfRecovery.log("doRecovery recoveryAction:" + recoveryByCause);
            switch (recoveryByCause) {
                case 1:
                    if (NetworkRecoveryStrategyBase.getInstance() != null) {
                        NetworkRecoveryStrategyBase.getInstance().forceLte(i2);
                    }
                    break;
                case 2:
                    if (!NetworkSelfRecovery.this.isFiveGOffByThermal() && NetworkRecoveryStrategyBase.getInstance() != null) {
                        NetworkRecoveryStrategyBase.getInstance().enableNsa(true, i2, getNsaOnCallback());
                    }
                    break;
                case 3:
                    if (NetworkRecoveryStrategyBase.getInstance() != null) {
                        NetworkRecoveryStrategyBase.getInstance().enableNsa(false, i2, getNsaOffCallback(i2));
                    }
                    break;
                case 4:
                    if (!isAvoidRestreNrforNightMode(i2) && !NetworkSelfRecovery.this.isFiveGOffByThermal() && NetworkRecoveryStrategyBase.getInstance() != null) {
                        if (!FiveGUtils.isXmRadioSupported()) {
                            NetworkRecoveryStrategyBase.getInstance().enableNsa(true, i2);
                        }
                        NetworkRecoveryStrategyBase.getInstance().enableSa(true, i2, getSaOnCallback(i, i2, scene));
                    }
                    break;
                case 5:
                    int value = scene != null ? scene.getValue() : -2;
                    if (shouldSkipDisalbeSa(scene)) {
                        NetworkSelfRecovery.log("skip disable sa for scene:" + value);
                    } else if (NetworkRecoveryStrategyBase.getInstance() != null) {
                        NetworkRecoveryStrategyBase.getInstance().enableSa(false, i2, getSaOffCallback(i, i2, scene, value));
                        if (!FiveGUtils.isXmRadioSupported()) {
                            NetworkRecoveryStrategyBase.getInstance().enableNsa(false, i2);
                        }
                    }
                    break;
                case 6:
                    if (NetworkRecoveryStrategyBase.getInstance() != null) {
                        NetworkRecoveryStrategyBase.getInstance().reAttach(i2);
                    }
                    NetworkSelfRecovery.this.sendBroadcastToUI("Lte");
                    break;
                case 7:
                    if (!MiuiPhoneUtils.isDuringCall()) {
                        if (NetworkRecoveryStrategyBase.getInstance() != null) {
                            NetworkRecoveryStrategyBase.getInstance().restartRadio(i2);
                        }
                    } else {
                        NetworkSelfRecovery.log("skip restart radio as phone is in call");
                    }
                    reset();
                    break;
                case 8:
                    String reasonByScene = NetworkSelfRecovery.this.getReasonByScene(scene);
                    if (NetworkRecoveryStrategyBase.getInstance() != null) {
                        NetworkRecoveryStrategyBase.getInstance().alleviateDataStall(i2, NetworkSelfRecovery.this.getAppType().getValue(), 0, true, reasonByScene);
                    }
                    break;
                case 9:
                    String reasonByScene2 = NetworkSelfRecovery.this.getReasonByScene(scene);
                    if (NetworkRecoveryStrategyBase.getInstance() != null) {
                        NetworkRecoveryStrategyBase.getInstance().alleviateDataStall(i2, NetworkSelfRecovery.this.getAppType().getValue(), 1, true, reasonByScene2);
                    }
                    break;
                case 10:
                    if (NetworkRecoveryStrategyBase.getInstance() != null) {
                        NetworkRecoveryStrategyBase.getInstance().forceLte2Nr(i2);
                    }
                    break;
                default:
                    NetworkSelfRecovery.log("doRecovery recoveryAction=" + recoveryByCause);
                    break;
            }
            NetworkSelfRecovery.this.notityRecoveryActionListener(recoveryByCause);
            if (scene != null) {
                NetworkSelfRecovery.this.reportRecoveryAction(recoveryByCause, scene.getValue());
            }
        }

        private boolean isAvoidRestreNrforNightMode(int i) {
            int i2;
            Calendar calendar = Calendar.getInstance();
            if (calendar == null || !MiuiPhoneUtils.isValidatePhoneId(i) || ((((i2 = calendar.get(11)) < 23 || i2 >= 24) && (i2 < 0 || i2 >= 7)) || NetworkSelfRecovery.this.mNetworkMetricsController == null || !NetworkSelfRecovery.this.mNetworkMetricsController.isNightMode())) {
                return false;
            }
            NetworkSelfRecovery.this.localLog("checkRecovery", "skip data stall recovery as there is sleep mode");
            return true;
        }

        private boolean shouldSkipDisalbeSa(NetworkMetricsController.Scene scene) {
            return MiuiPhoneUtils.shouldSkipDeprioSaExceptThermal() && scene != NetworkMetricsController.Scene.THERMAL;
        }

        private void dataLinkTimeOutForSA(int i, NetworkMetricsController.Scene scene) {
            int i2 = (scene == null || AnonymousClass1.$SwitchMap$com$android$phone$NetworkMetricsController$Scene[scene.ordinal()] != 1) ? 30 : 20;
            NetworkSelfRecovery networkSelfRecovery = NetworkSelfRecovery.this;
            networkSelfRecovery.sendMessageDelayed(networkSelfRecovery.obtainMessage(2, 54, i, NetworkMetricsController.Scene.TIMEOUT), TimeUnit.MINUTES.toMillis(i2));
        }

        private INetworkRecoveryStrategy$RecoveryResultCallback getNsaOnCallback() {
            return new INetworkRecoveryStrategy$RecoveryResultCallback() { // from class: com.android.phone.NetworkSelfRecovery.RecoveryStrategyHandler.1
                @Override // com.android.phone.INetworkRecoveryStrategy$RecoveryResultCallback
                public void onComplete(final boolean z) {
                    NetworkSelfRecovery.this.post(new Runnable() { // from class: com.android.phone.NetworkSelfRecovery.RecoveryStrategyHandler.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            RecoveryStrategyHandler.this.handleNsaOnResult(z);
                        }
                    });
                }
            };
        }

        private INetworkRecoveryStrategy$RecoveryResultCallback getNsaOffCallback(final int i) {
            return new INetworkRecoveryStrategy$RecoveryResultCallback() { // from class: com.android.phone.NetworkSelfRecovery.RecoveryStrategyHandler.2
                @Override // com.android.phone.INetworkRecoveryStrategy$RecoveryResultCallback
                public void onComplete(final boolean z) {
                    NetworkSelfRecovery.this.post(new Runnable() { // from class: com.android.phone.NetworkSelfRecovery.RecoveryStrategyHandler.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                            RecoveryStrategyHandler.this.handleNsaOffResult(z, i);
                        }
                    });
                }
            };
        }

        private INetworkRecoveryStrategy$RecoveryResultCallback getSaOnCallback(final int i, final int i2, final NetworkMetricsController.Scene scene) {
            return new INetworkRecoveryStrategy$RecoveryResultCallback() { // from class: com.android.phone.NetworkSelfRecovery.RecoveryStrategyHandler.3
                @Override // com.android.phone.INetworkRecoveryStrategy$RecoveryResultCallback
                public void onComplete(final boolean z) {
                    NetworkSelfRecovery.this.post(new Runnable() { // from class: com.android.phone.NetworkSelfRecovery.RecoveryStrategyHandler.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AnonymousClass3 anonymousClass3 = AnonymousClass3.this;
                            RecoveryStrategyHandler.this.handleSaOnResult(z, i, i2, scene);
                        }
                    });
                }
            };
        }

        private INetworkRecoveryStrategy$RecoveryResultCallback getSaOffCallback(final int i, final int i2, final NetworkMetricsController.Scene scene, final int i3) {
            return new INetworkRecoveryStrategy$RecoveryResultCallback() { // from class: com.android.phone.NetworkSelfRecovery.RecoveryStrategyHandler.4
                @Override // com.android.phone.INetworkRecoveryStrategy$RecoveryResultCallback
                public void onComplete(final boolean z) {
                    NetworkSelfRecovery.this.post(new Runnable() { // from class: com.android.phone.NetworkSelfRecovery.RecoveryStrategyHandler.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AnonymousClass4 anonymousClass4 = AnonymousClass4.this;
                            RecoveryStrategyHandler.this.handleSaOffResult(z, i, i2, scene, i3);
                        }
                    });
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleNsaOnResult(boolean z) {
            if (!z) {
                NetworkSelfRecovery.log("skip update recovery action for nsa on failure");
                return;
            }
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                putRecoveryAction(2, i);
            }
            saveCellGlobalId("");
            if (NetworkSelfRecovery.this.hasMessages(3)) {
                NetworkSelfRecovery.this.removeMessages(3);
            }
            this.mTimeLastNsaOffTimeOutMs = 0L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleNsaOffResult(boolean z, int i) {
            if (!z) {
                NetworkSelfRecovery.log("skip update recovery action for nsa off failure");
                return;
            }
            putRecoveryAction(3, i);
            saveCellGlobalId(NetworkSelfRecovery.this.mCGI);
            NetworkSelfRecovery.this.mCGISet.clear();
            NetworkSelfRecovery.this.removeMessages(3);
            this.mTimeLastNsaOffTimeOutMs = SystemClock.elapsedRealtime();
            NetworkSelfRecovery networkSelfRecovery = NetworkSelfRecovery.this;
            networkSelfRecovery.sendMessageDelayed(networkSelfRecovery.obtainMessage(3, MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, i, NetworkMetricsController.Scene.TIMEOUT), 2700000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleSaOnResult(boolean z, int i, int i2, NetworkMetricsController.Scene scene) {
            if (!z) {
                NetworkSelfRecovery.log("skip update recovery action for sa on failure");
                return;
            }
            NetworkSelfRecovery networkSelfRecovery = NetworkSelfRecovery.this;
            networkSelfRecovery.recordDeprioritizeOrRestoreSa(2, i, networkSelfRecovery.mCausePrev[i2], scene != null ? scene.getValue() : -2, NetworkSelfRecovery.this.mScenePrev[i2], i2, SystemClock.elapsedRealtime() - NetworkSelfRecovery.this.mLastDepriSaTime[i2]);
            NetworkSelfRecovery.this.mCausePrev[i2] = -1;
            NetworkSelfRecovery.this.mScenePrev[i2] = -1;
            for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                putRecoveryAction(4, i3);
            }
            saveCellGlobalId("");
            if (NetworkSelfRecovery.this.hasMessages(2)) {
                NetworkSelfRecovery.this.removeMessages(2);
            }
            this.mTimeLastSaOffTimeOutMs = 0L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleSaOffResult(boolean z, int i, int i2, NetworkMetricsController.Scene scene, int i3) {
            if (!z) {
                NetworkSelfRecovery.log("skip update recovery action for sa off failure");
                return;
            }
            NetworkSelfRecovery.this.mLastDepriSaTime[i2] = System.currentTimeMillis();
            NetworkSelfRecovery.this.mScenePrev[i2] = i3;
            NetworkSelfRecovery.this.mCausePrev[i2] = i;
            putRecoveryAction(5, i2);
            saveCellGlobalId(NetworkSelfRecovery.this.mCGI);
            NetworkSelfRecovery.this.mCGISet.clear();
            NetworkSelfRecovery.this.removeMessages(2);
            this.mTimeLastSaOffTimeOutMs = SystemClock.elapsedRealtime();
            dataLinkTimeOutForSA(i2, scene);
        }

        public void processDoRecovery(int i, int i2, boolean z, NetworkMetricsController.Scene scene) {
            NetworkSelfRecovery.log("processDoRecovery cause=" + i + ",force=" + z);
            if (checkRecovery(z, i2)) {
                triggerRecovery(i, i2, scene);
            }
        }
    }

    /* JADX INFO: renamed from: com.android.phone.NetworkSelfRecovery$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$phone$NetworkMetricsController$Scene;

        static {
            int[] iArr = new int[NetworkMetricsController.Scene.values().length];
            $SwitchMap$com$android$phone$NetworkMetricsController$Scene = iArr;
            try {
                iArr[NetworkMetricsController.Scene.SIGNAL_MAP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$phone$NetworkMetricsController$Scene[NetworkMetricsController.Scene.VOIP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$phone$NetworkMetricsController$Scene[NetworkMetricsController.Scene.LINK_QOS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$phone$NetworkMetricsController$Scene[NetworkMetricsController.Scene.LIVE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$phone$NetworkMetricsController$Scene[NetworkMetricsController.Scene.DATA_STALL_SELFSTUDY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$phone$NetworkMetricsController$Scene[NetworkMetricsController.Scene.GAME.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$phone$NetworkMetricsController$Scene[NetworkMetricsController.Scene.GAME_RESIDENT_CELL.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$phone$NetworkMetricsController$Scene[NetworkMetricsController.Scene.DATA_STALL_RESET.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$phone$NetworkMetricsController$Scene[NetworkMetricsController.Scene.CELLCHANGED.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$phone$NetworkMetricsController$Scene[NetworkMetricsController.Scene.SUBWAY_QOE_DATA_STALL.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:22:0x003f  */
    public synchronized void processServiceStateChanged(int i, ServiceState serviceState) throws Throwable {
        Throwable th;
        NetworkSelfRecovery networkSelfRecovery;
        int i2;
        int dataNetworkType;
        try {
            try {
                log("processServiceStateChangeEvent phoneId=" + i);
                if (serviceState != null && MiuiPhoneUtils.isValidatePhoneId(i)) {
                    int dataNetworkType2 = serviceState.getDataNetworkType();
                    ServiceState serviceState2 = this.mServiceState[i];
                    if (serviceState2 != null) {
                        try {
                            dataNetworkType = serviceState2.getDataNetworkType();
                        } catch (Throwable th2) {
                            th = th2;
                            throw th;
                        }
                    } else {
                        dataNetworkType = dataNetworkType2;
                    }
                    int i3 = this.mScenePrev[i];
                    if (i3 == -1 || !(dataNetworkType == 13 || dataNetworkType == 19)) {
                        networkSelfRecovery = this;
                        i2 = i;
                    } else if (dataNetworkType2 == 20 || dataNetworkType2 == 30) {
                        networkSelfRecovery = this;
                        i2 = i;
                        networkSelfRecovery.recordDeprioritizeOrRestoreSa(2, -2, this.mCausePrev[i], -2, i3, i2, SystemClock.elapsedRealtime() - this.mLastDepriSaTime[i]);
                        networkSelfRecovery.mCausePrev[i2] = -1;
                        networkSelfRecovery.mScenePrev[i2] = -1;
                    } else {
                        networkSelfRecovery = this;
                        i2 = i;
                    }
                } else {
                    networkSelfRecovery = this;
                    i2 = i;
                    log("processServiceStateChangeEvent invalid params");
                }
                networkSelfRecovery.mServiceState[i2] = serviceState;
                return;
            } catch (Throwable th3) {
                th = th3;
                th = th;
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            th = th;
            throw th;
        }
        throw th;
    }

    public void doRecoveryIfNeed(int i, int i2, boolean z, NetworkMetricsController.Scene scene) {
        RecoveryStrategyHandler recoveryStrategyHandler;
        if (!MiuiPhoneUtils.isValidatePhoneId(i2) || (recoveryStrategyHandler = this.mDsRecoveryHandler) == null) {
            return;
        }
        recoveryStrategyHandler.processDoRecovery(i, i2, z, scene);
    }

    public void doRecoveryInHst(int i) {
        if (MiuiPhoneUtils.isDuringCall() || isFiveGOffByThermal()) {
            log("In the high-speed train, if you are on call or Thermal scene, ignore the upgrade to 5G.");
            return;
        }
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            if (!FiveGUtils.isXmRadioSupported()) {
                NetworkRecoveryStrategyBase.getInstance().enableNsa(true, i);
            }
            NetworkRecoveryStrategyBase.getInstance().enableSa(true, i);
        }
        recordDeprioritizeOrRestoreSa(2, 55, this.mCausePrev[i], NetworkMetricsController.Scene.HIGH_SPEED_TRAIN.getValue(), this.mScenePrev[i], i, SystemClock.elapsedRealtime() - this.mLastDepriSaTime[i]);
        this.mCausePrev[i] = -1;
        this.mScenePrev[i] = -1;
    }

    public synchronized void updateCellGobalId(String str, int i) {
        try {
            this.mCGI = str;
            if (isCellLocChanged()) {
                this.mCGISet.add(this.mCGI);
                this.mCGISet.add(this.mFivegCGI);
                log("updateCellGobalId cellLocChanged,mCGI= " + this.mCGI + ",mFivegCGI= " + this.mFivegCGI + ",mCGISet.size=" + this.mCGISet.size());
                boolean zIsInCellLocLingeringTime = this.mDsRecoveryHandler.isInCellLocLingeringTime();
                HashSet hashSet = this.mCGISet;
                if (!zIsInCellLocLingeringTime) {
                    hashSet.clear();
                    NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
                    if (networkMetricsController != null) {
                        networkMetricsController.restoreNrIfIndeed(NetworkMetricsController.Scene.CELLCHANGED);
                    }
                } else if (hashSet.size() > 5) {
                    this.mCGISet.clear();
                    NetworkMetricsController networkMetricsController2 = this.mNetworkMetricsController;
                    if (networkMetricsController2 != null) {
                        networkMetricsController2.restoreNrIfIndeed(NetworkMetricsController.Scene.CELLCHANGED);
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public boolean isNsaOffInRecoveryAction(int i) {
        return MiuiPhoneUtils.isValidatePhoneId(i) && this.mDsRecoveryHandler.getRecoveryAction(i) == 3;
    }

    public boolean isSaOffInRecoveryAction(int i) {
        return MiuiPhoneUtils.isValidatePhoneId(i) && this.mDsRecoveryHandler.getRecoveryAction(i) == 5;
    }

    public void resetDataStallOptimization(int i, int i2, NetworkMetricsController.Scene scene) {
        String reasonByScene = getReasonByScene(scene);
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().alleviateDataStall(i, getAppType().getValue(), i2, false, reasonByScene);
        }
    }

    private boolean isCellLocChanged() {
        return (TextUtils.isEmpty(this.mFivegCGI) || TextUtils.isEmpty(this.mCGI) || TextUtils.equals(this.mCGI, this.mFivegCGI)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBroadcastToUI(String str) {
        Intent intent = new Intent("miui.intent.action.ACTIVE_RE_REGISTRATION_NETWORK");
        intent.putExtra("TECHNOLOGY", str);
        intent.putExtra("className", NetworkSelfRecovery.class.getSimpleName());
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notityRecoveryActionListener(int i) {
        Iterator it = this.mActionListener.iterator();
        if (it.hasNext()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            throw null;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("recovery_action", i);
        EventHubManager.getInstance().postEvent(new NetworkSceneEvent.SelfRecoveryEvent("self_recovery_done", bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportRecoveryAction(int i, int i2) {
        log("reportRecoveryAction");
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.ACTION_RECOVER_POLICY");
        intent.putExtra("action", i);
        intent.putExtra("scene", i2);
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFiveGOffByThermal() {
        boolean saThermalFlag = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSaThermalFlag();
        log("isFiveGOffByThermal = " + saThermalFlag);
        return saThermalFlag;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getReasonByScene(NetworkMetricsController.Scene scene) {
        if (scene == null) {
            return "";
        }
        switch (AnonymousClass1.$SwitchMap$com$android$phone$NetworkMetricsController$Scene[scene.ordinal()]) {
            case 2:
                return "weChat";
            case 3:
                return "linkqos";
            case 4:
                return "live";
            case 5:
                return "dataStallSelfStudy";
            case 6:
                return "game";
            case 7:
            case 8:
                return "dataStallReset";
            case 9:
            case 10:
                return scene.name();
            default:
                return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NetworkMetricsController.AppType getAppType() {
        NetworkMetricsController.AppType appType;
        NetworkMetricsController.AppType appType2 = NetworkMetricsController.AppType.TYPE_GAMING;
        NetworkMetricsController networkMetricsController = this.mNetworkMetricsController;
        return (networkMetricsController == null || (appType = networkMetricsController.mCurApptype) == NetworkMetricsController.AppType.NONE) ? appType2 : appType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordDeprioritizeOrRestoreSa(int i, int i2, int i3, int i4, int i5, int i6, long j) {
        log("recordDepriSa,updateType= " + i + ",causeCurr= " + i2 + ",causePre= " + i3 + ",sceneTypeCurr= " + i4 + ",sceneTypePre= " + i5 + ",phoneId=" + i6 + ",depriTimeMs=" + j);
        HashMap map = new HashMap();
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(i6);
        map.put("UpdateType", Integer.valueOf(i));
        map.put("CauseCurr", Integer.valueOf(i2));
        map.put("CausePre", Integer.valueOf(i3));
        map.put("SceneCurr", Integer.valueOf(i4));
        map.put("ScenePre", Integer.valueOf(i5));
        map.put("DdsPhoneId", Integer.valueOf(i6));
        map.put("DepriTimeMs", Long.valueOf(j));
        map.put("Network", simOperatorNumeric);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_deprioritize_or_restore_sa", map);
    }

    class SensorStateCallBack implements TelephonySensorManager.SensorStateListener {
        SensorStateCallBack() {
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onSubwayStateChanged(boolean z) {
            if (NetworkSelfRecovery.this.mIsSubway == z) {
                return;
            }
            NetworkSelfRecovery.this.mIsSubway = z;
            NetworkSelfRecovery.this.processSensorStateChanged();
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onHstStateChanged(boolean z) {
            if (NetworkSelfRecovery.this.mIsHst == z) {
                return;
            }
            NetworkSelfRecovery.this.mIsHst = z;
            NetworkSelfRecovery.this.processSensorStateChanged();
        }
    }

    private void regSensorStateCallBack() {
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            TelephonySensorManager telephonySensorManagerInit = TelephonySensorManager.init(this.mContext);
            if (this.mSensorStateCallBack != null || telephonySensorManagerInit == null) {
                return;
            }
            log("regSensorStateCallBack");
            SensorStateCallBack sensorStateCallBack = new SensorStateCallBack();
            this.mSensorStateCallBack = sensorStateCallBack;
            telephonySensorManagerInit.registerSensorStateListener(sensorStateCallBack);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSensorStateChanged() {
        log("processSensorStateChanged mIsHighSpeedScene=" + this.mIsHighSpeedScene + ", S=" + this.mIsSubway + ", H=" + this.mIsHst);
        boolean z = this.mIsSubway || this.mIsHst;
        if (this.mIsHighSpeedScene == z) {
            return;
        }
        this.mIsHighSpeedScene = z;
        if (z) {
            if (hasMessages(4)) {
                removeMessages(4);
                return;
            } else {
                setSbpOosHighRatOptState(false);
                return;
            }
        }
        delayToResetSbpOosHighRatOpt();
    }

    private void delayToResetSbpOosHighRatOpt() {
        log("delayToResetSbpOosHighRatOpt");
        sendMessageDelayed(obtainMessage(4), 30000L);
    }

    private void setSbpOosHighRatOptState(boolean z) {
        if (MiuiPhoneUtils.isSupportMtkHighSpeedSbpOpt()) {
            localLog("setSbpOosHighRatOptState", "enable:" + z + " S:" + this.mIsSubway + " H:" + this.mIsHst);
            if (NetworkRecoveryStrategyBase.getInstance() != null) {
                NetworkRecoveryStrategyBase.getInstance().setSbpOosHighRatOptState(z);
            }
        }
    }

    public void enableWeakOpt(int i, boolean z) {
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().enableWeakOpt(i, z);
        }
    }

    public void enableCongestionOpt(int i, boolean z) {
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().enableCongestionOpt(i, z);
        }
    }

    public void enableHsrOpt(int i, boolean z) {
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().enableHsrOpt(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void localLog(String str, String str2) {
        NetworkMetricsController.localLog("NetworkSelfRecovery", str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("NetworkSelfRecovery", str);
    }
}
