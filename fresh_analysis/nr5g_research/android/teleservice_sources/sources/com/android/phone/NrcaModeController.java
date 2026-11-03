package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.network.NrcaScheduleEvaluator;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;
import xiaomi.hypercomm.HyperCommManager;
import xiaomi.hypercomm.SceneAwareCallback;

/* JADX INFO: loaded from: classes.dex */
public class NrcaModeController {
    private static volatile NrcaModeController sInstance;
    private Context mContext;
    private Handler mHandler;
    private NetworkRecoveryStrategyBase mNetworkRecovery;
    private NrcaBroadcastReceiver mReceiver;
    private SceneAwareCallback mSceneAwareCallback;
    private NrcaTeleCallback mTeleCallback;
    private HandlerThread mWorkThread;
    private final Object mTeleCallbackLock = new Object();
    private boolean mDebug = false;
    private volatile int mDdsPhoneId = -1;
    private volatile boolean mIsHstState = false;
    private volatile boolean mIsAirPlaneMode = false;
    private volatile boolean mStallDetecting = false;
    private volatile String mDisableCellId = null;
    private volatile int mDisablePhoneId = -1;
    private volatile boolean mWaitingCellChangeRestore = false;
    private volatile boolean mWaitingInServiceRestore = false;
    private volatile int mDisableNetworkScore = -1;
    private volatile int mLatestNetworkScore = -1;
    private volatile String mCurrentHstLine = "NA";
    private int mAfterScoreSampleIndex = 0;
    private final int[] mAfterScoreSamples = new int[10];
    private String mNrcaStatusAtSampleEnd = "NA";

    public static synchronized NrcaModeController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new NrcaModeController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private NrcaModeController() {
        logD("NrcaModeController Instantiation");
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("NrcaModeController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        NrcaModeControllerHandler nrcaModeControllerHandler = new NrcaModeControllerHandler(this.mWorkThread.getLooper());
        this.mHandler = nrcaModeControllerHandler;
        nrcaModeControllerHandler.obtainMessage(1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        logD("init");
        this.mNetworkRecovery = NetworkRecoveryStrategyBase.getInstance();
        this.mDdsPhoneId = DefaultSimManager.getDefaultDataSlotId();
        this.mIsAirPlaneMode = MiuiPhoneUtils.isAirPlaneMode();
        registerReceiver();
        registerTelephonyInfoCallBack();
        registerSceneAwareCallback();
    }

    class NrcaModeControllerHandler extends Handler {
        NrcaModeControllerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                processEvent(message);
            } catch (Exception e) {
                Log.e("NrcaModeController", "handleMessage exception: " + e.toString());
            }
        }

        private void processEvent(Message message) {
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 1:
                        NrcaModeController.this.init();
                        break;
                    case 2:
                        NrcaModeController.this.processStallDetected();
                        break;
                    case 3:
                        NrcaModeController.this.processHstStateChanged(message);
                        break;
                    case 4:
                        NrcaModeController.this.processAirplaneModeChanged();
                        break;
                    case 5:
                        NrcaModeController.this.processDataSlotChanged();
                        break;
                    case 6:
                        NrcaModeController.this.processCellLocationChanged(message.arg1);
                        break;
                    case 7:
                        NrcaModeController.this.processDelayNrcaRestore();
                        break;
                    case 8:
                        NrcaModeController.this.processSimStateChanged(message);
                        break;
                    case 9:
                        NrcaModeController.this.processServiceStateChanged(message.arg1, (ServiceState) message.obj);
                        break;
                    case 10:
                        NrcaModeController.this.processCollectAfterScore();
                        break;
                    case 11:
                        NrcaModeController.this.processHstLineChanged(message);
                        break;
                }
                return;
            }
            NrcaModeController.this.processDebug(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNetworkQoeChanged(int i, int i2) {
        this.mLatestNetworkScore = i;
        if (i == -1) {
            logD("processNetworkQoeChanged: invalid networkScore, skip");
            return;
        }
        if (i2 == -1) {
            if (this.mStallDetecting) {
                logD("processNetworkQoeChanged: app exit, cancel stall detect");
                this.mHandler.removeMessages(2);
                this.mStallDetecting = false;
                return;
            }
            return;
        }
        if (isDualDataEnabled()) {
            logD("processNetworkQoeChanged: dual data enabled, skip");
            return;
        }
        if (!isNrOrNrCaNetwork()) {
            logD("processNetworkQoeChanged: not NR/NR_CA network, skip");
            return;
        }
        if (isDisableCountAllowed()) {
            boolean z = this.mStallDetecting;
            if (i >= 80) {
                if (z) {
                    logD("processNetworkQoeChanged: score recovered, cancel stall detect");
                    this.mHandler.removeMessages(2);
                    this.mStallDetecting = false;
                    return;
                }
                return;
            }
            if (z) {
                return;
            }
            log("processNetworkQoeChanged: score=" + i + ", start stall detect");
            this.mStallDetecting = true;
            this.mHandler.removeMessages(2);
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(2), 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processStallDetected() {
        this.mStallDetecting = false;
        if (!isDisableCountAllowed()) {
            logD("processStallDetected: disable count exceeded");
        } else if (getNrcaScheduleStatus()) {
            logD("processStallDetected: SCC schedule normal, skip");
        } else {
            disableNrCa();
        }
    }

    private synchronized void disableNrCa() {
        String currentCellId = getCurrentCellId();
        if (currentCellId == null) {
            logD("disableNrCa: getCurrentCellId is null, skip");
            return;
        }
        if (!setNrcaStatus(false, this.mDdsPhoneId, "SCC_SCHEDULE_ABNORMAL")) {
            logD("disableNrCa: setNrcaStatus failed, abort");
            return;
        }
        log("disableNrCa: cellId=" + currentCellId + ", phoneId=" + this.mDdsPhoneId + ", isHst=" + this.mIsHstState);
        this.mDisableCellId = currentCellId;
        this.mDisablePhoneId = this.mDdsPhoneId;
        this.mDisableNetworkScore = this.mLatestNetworkScore;
        startAfterScoreSampling();
        this.mHandler.removeMessages(7);
        boolean z = this.mIsHstState;
        Handler handler = this.mHandler;
        if (z) {
            handler.sendMessageDelayed(handler.obtainMessage(7), 15000L);
        } else {
            handler.sendMessageDelayed(handler.obtainMessage(7), 1200000L);
        }
    }

    private synchronized void restoreNrCa(String str) {
        if (!isNrcaDisabled()) {
            logD("restoreNrCa: already enabled, reset local state");
            resetDisableState();
            return;
        }
        if (!isInService()) {
            logD("restoreNrCa: OOS, wait for in service");
            this.mWaitingInServiceRestore = true;
            return;
        }
        if (MiuiPhoneUtils.isDuringCall()) {
            logD("restoreNrCa: during call, wait for cell change");
            this.mWaitingCellChangeRestore = true;
            return;
        }
        if (this.mAfterScoreSampleIndex < 10) {
            this.mNrcaStatusAtSampleEnd = "disabled";
        }
        if (!setNrcaStatus(true, this.mDisablePhoneId, "SCC_SCHEDULE_ABNORMAL")) {
            logD("restoreNrCa: enable failed");
            return;
        }
        log("restoreNrCa reason=" + str);
        recordSccDisableStatistics(str);
        resetDisableState();
    }

    private void resetDisableState() {
        this.mDisableCellId = null;
        this.mDisablePhoneId = -1;
        this.mWaitingCellChangeRestore = false;
        this.mWaitingInServiceRestore = false;
        this.mDisableNetworkScore = -1;
        this.mAfterScoreSampleIndex = 0;
        this.mNrcaStatusAtSampleEnd = "NA";
        this.mHandler.removeMessages(7);
        this.mHandler.removeMessages(10);
    }

    private void startAfterScoreSampling() {
        this.mAfterScoreSampleIndex = 0;
        Arrays.fill(this.mAfterScoreSamples, -1);
        this.mHandler.removeMessages(10);
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(10), 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCollectAfterScore() {
        int i = this.mAfterScoreSampleIndex;
        if (i >= 10) {
            return;
        }
        this.mAfterScoreSamples[i] = this.mLatestNetworkScore;
        this.mAfterScoreSampleIndex++;
        StringBuilder sb = new StringBuilder("processCollectAfterScore: index=");
        sb.append(this.mAfterScoreSampleIndex - 1);
        sb.append(" score=");
        sb.append(this.mLatestNetworkScore);
        logD(sb.toString());
        if (this.mAfterScoreSampleIndex < 10) {
            this.mHandler.removeMessages(10);
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(10), 2000L);
        } else {
            this.mNrcaStatusAtSampleEnd = isNrcaDisabled() ? "disabled" : "enabled";
            logD("processCollectAfterScore: sampling done, nrcaStatus=" + this.mNrcaStatusAtSampleEnd);
        }
    }

    private String buildAfterScoresString() {
        if (this.mAfterScoreSampleIndex <= 0) {
            return "NA";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.mAfterScoreSampleIndex; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(this.mAfterScoreSamples[i]);
        }
        return sb.toString();
    }

    private void recordSccDisableStatistics(String str) {
        if (this.mDisableNetworkScore == -1) {
            logD("recordSccDisableStatistics: disableScore invalid, skip");
            return;
        }
        try {
            HashMap map = new HashMap();
            map.put("nrca_scc_disable_score", String.valueOf(this.mDisableNetworkScore));
            map.put("nrca_scc_after_scores", buildAfterScoresString());
            map.put("nrca_scc_restore_reason", str);
            map.put("nrca_scc_scene_type", this.mIsHstState ? "HST" : "COMMON");
            map.put("nrca_scc_hst_line", this.mCurrentHstLine);
            map.put("nrca_scc_nrca_status", this.mNrcaStatusAtSampleEnd);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_nrca_scc_disable_statistics", map);
            logD("recordSccDisableStatistics: " + map);
        } catch (Exception e) {
            logD("recordSccDisableStatistics exception: " + e.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:10:0x0028  */
    public void processHstLineChanged(Message message) {
        Object obj = message.obj;
        if (obj instanceof Intent) {
            Serializable serializableExtra = ((Intent) obj).getSerializableExtra("mHitHstLineStatus");
            if (serializableExtra instanceof Map) {
                Map map = (Map) serializableExtra;
                if (!map.isEmpty()) {
                    this.mCurrentHstLine = TextUtils.join(",", map.keySet());
                } else {
                    this.mCurrentHstLine = "NA";
                }
            } else {
                this.mCurrentHstLine = "NA";
            }
            logD("processHstLineChanged: " + this.mCurrentHstLine);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processHstStateChanged(Message message) {
        boolean booleanExtra;
        Object obj = message.obj;
        if ((obj instanceof Intent) && (booleanExtra = ((Intent) obj).getBooleanExtra("mIsHstMode", false)) != this.mIsHstState) {
            logD("processHstStateChanged: " + this.mIsHstState + " -> " + booleanExtra);
            boolean z = this.mIsHstState;
            this.mIsHstState = booleanExtra;
            if (!z && booleanExtra && this.mDisablePhoneId != -1 && isNrcaDisabled()) {
                logD("processHstStateChanged: enter HST, switch restore timer");
                this.mWaitingCellChangeRestore = false;
                this.mHandler.removeMessages(7);
                Handler handler = this.mHandler;
                handler.sendMessageDelayed(handler.obtainMessage(7), 15000L);
                return;
            }
            if (!z || booleanExtra || this.mDisablePhoneId == -1) {
                return;
            }
            restoreNrCa("EXIT_HST");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAirplaneModeChanged() {
        boolean zIsAirPlaneMode = MiuiPhoneUtils.isAirPlaneMode();
        if (zIsAirPlaneMode == this.mIsAirPlaneMode) {
            return;
        }
        logD("processAirplaneModeChanged: " + this.mIsAirPlaneMode + " -> " + zIsAirPlaneMode);
        boolean z = this.mIsAirPlaneMode;
        this.mIsAirPlaneMode = zIsAirPlaneMode;
        if (!z || zIsAirPlaneMode || this.mDisablePhoneId == -1) {
            return;
        }
        restoreNrCa("AIRPLANE");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDataSlotChanged() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (!MiuiPhoneUtils.isValidatePhoneId(defaultDataSlotId) || defaultDataSlotId == this.mDdsPhoneId) {
            return;
        }
        logD("processDataSlotChanged: " + this.mDdsPhoneId + " -> " + defaultDataSlotId);
        this.mDdsPhoneId = defaultDataSlotId;
        if (this.mDisablePhoneId != -1) {
            restoreNrCa("SLOT_CHANGED");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChanged(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && i == this.mDdsPhoneId && this.mWaitingCellChangeRestore && isCellChanged()) {
            String str = this.mIsHstState ? "HST_TIMER_CELL_CHANGED" : "TIMER_CELL_CHANGED";
            log("processCellLocationChanged: cell changed after timer, restore");
            restoreNrCa(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDelayNrcaRestore() {
        if (this.mDisablePhoneId == -1) {
            return;
        }
        String str = this.mIsHstState ? "HST_TIMER_CELL_CHANGED" : "TIMER_CELL_CHANGED";
        if (isCellChanged()) {
            restoreNrCa(str);
        } else {
            logD("processDelayNrcaRestore: cell not changed, wait for cell change");
            this.mWaitingCellChangeRestore = true;
        }
    }

    private boolean isCellChanged() {
        String currentCellId = getCurrentCellId();
        return (TextUtils.isEmpty(this.mDisableCellId) || TextUtils.isEmpty(currentCellId) || TextUtils.equals(currentCellId, this.mDisableCellId)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServiceStateChanged(int i, ServiceState serviceState) {
        if (serviceState != null && MiuiPhoneUtils.isValidatePhoneId(i) && i == this.mDisablePhoneId && this.mWaitingInServiceRestore && serviceState.getDataRegState() == 0) {
            logD("processServiceStateChanged: in service, restore");
            restoreNrCa("SERVICE_RESTORED");
        }
    }

    private boolean isInService() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDisablePhoneId)) {
            return false;
        }
        try {
            Phone phone = MiuiPhoneUtils.getPhone(this.mDisablePhoneId);
            return (phone == null || phone.getServiceState() == null || phone.getServiceState().getDataRegState() != 0) ? false : true;
        } catch (Exception e) {
            logD("isInService exception: " + e.toString());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSimStateChanged(Message message) {
        Object obj = message.obj;
        if (obj instanceof Intent) {
            Intent intent = (Intent) obj;
            String stringExtra = intent.getStringExtra("ss");
            int intExtra = intent.getIntExtra("phone", -1);
            if ("LOADED".equals(stringExtra)) {
                if (MiuiPhoneUtils.isValidatePhoneId(intExtra)) {
                    if (intExtra == this.mDisablePhoneId) {
                        logD("processSimStateChanged: SIM loaded on disabled phoneId=" + intExtra);
                        restoreNrCa("SIM_LOADED");
                    }
                }
            }
        }
    }

    private boolean getNrcaScheduleStatus() {
        try {
            return NrcaScheduleEvaluator.getInstance().getNrcaScheduleStatus();
        } catch (Exception e) {
            logD("getNrcaScheduleStatus exception: " + e.toString());
            return true;
        }
    }

    private boolean isNrcaDisabled() {
        if (this.mNetworkRecovery == null || !MiuiPhoneUtils.isValidatePhoneId(this.mDisablePhoneId)) {
            return false;
        }
        boolean nrCaStatus = this.mNetworkRecovery.getNrCaStatus(this.mDisablePhoneId);
        logD("isNrcaDisabled: getNrCaStatus=" + nrCaStatus + ", phoneId=" + this.mDisablePhoneId);
        return !nrCaStatus;
    }

    private boolean setNrcaStatus(boolean z, int i, String str) {
        if (this.mNetworkRecovery == null) {
            logD("setNrcaStatus: mNetworkRecovery is null");
            return false;
        }
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            logD("setNrcaStatus: invalid phoneId=" + i);
            return false;
        }
        if (this.mNetworkRecovery.getNrCaStatus(i) == z) {
            logD("setNrcaStatus: already " + z + ", skip");
            return true;
        }
        try {
            boolean nrCaStatus = this.mNetworkRecovery.setNrCaStatus(i, z, str);
            log("setNrcaStatus: enabled=" + z + ", phoneId=" + i + ", reason=" + str + ", result=" + nrCaStatus);
            return nrCaStatus;
        } catch (Exception e) {
            logD("setNrcaStatus exception: " + e.toString());
            return false;
        }
    }

    private boolean isNrOrNrCaNetwork() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            return false;
        }
        try {
            Phone phone = MiuiPhoneUtils.getPhone(this.mDdsPhoneId);
            if (phone != null && phone.getServiceState() != null) {
                return MiuiPhoneUtils.isNrNwType(phone.getServiceState().getDataNetworkType());
            }
            return false;
        } catch (Exception e) {
            logD("isNrOrNrCaNetwork exception: " + e.toString());
            return false;
        }
    }

    private boolean isDisableCountAllowed() {
        NetworkRecoveryStrategyBase networkRecoveryStrategyBase = this.mNetworkRecovery;
        if (networkRecoveryStrategyBase == null) {
            return false;
        }
        try {
            int nrCaCallCount = networkRecoveryStrategyBase.getNrCaCallCount();
            logD("isDisableCountAllowed: callCount=" + nrCaCallCount);
            return nrCaCallCount < 80;
        } catch (Exception e) {
            logD("isDisableCountAllowed exception: " + e.toString());
            return false;
        }
    }

    private boolean isDualDataEnabled() {
        try {
            return Settings.Global.getInt(this.mContext.getContentResolver(), "dual_mobile_data", 0) != 0;
        } catch (Exception e) {
            logD("isDualDataEnabled exception: " + e.toString());
            return false;
        }
    }

    private String getCurrentCellId() {
        if (!MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
            return null;
        }
        try {
            Phone phone = MiuiPhoneUtils.getPhone(this.mDdsPhoneId);
            if (phone == null) {
                return null;
            }
            CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
            if (currentCellIdentity instanceof CellIdentityLte) {
                return String.valueOf(((CellIdentityLte) currentCellIdentity).getCi());
            }
            if (currentCellIdentity instanceof CellIdentityNr) {
                return String.valueOf(((CellIdentityNr) currentCellIdentity).getNci());
            }
            return null;
        } catch (Exception e) {
            logD("getCurrentCellId exception: " + e.toString());
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.NRCA_MODE_CONTROLLER_DEBUG");
        NrcaBroadcastReceiver nrcaBroadcastReceiver = new NrcaBroadcastReceiver();
        this.mReceiver = nrcaBroadcastReceiver;
        this.mContext.registerReceiver(nrcaBroadcastReceiver, intentFilter, 2);
    }

    private void registerTelephonyInfoCallBack() {
        synchronized (this.mTeleCallbackLock) {
            try {
                if (this.mTeleCallback == null) {
                    NrcaTeleCallback nrcaTeleCallback = new NrcaTeleCallback();
                    this.mTeleCallback = nrcaTeleCallback;
                    TelephonyStatAdapter.registerCallBack(nrcaTeleCallback);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void registerSceneAwareCallback() {
        try {
            HyperCommManager hyperCommManager = HyperCommManager.getDefault();
            if (hyperCommManager == null) {
                Log.e("NrcaModeController", "HyperCommManager is not available");
                return;
            }
            NrcaSceneAwareCallback nrcaSceneAwareCallback = new NrcaSceneAwareCallback(new HandlerExecutor(this.mHandler));
            boolean zRegisterSceneAwareCallback = hyperCommManager.registerSceneAwareCallback(nrcaSceneAwareCallback);
            log("registerSceneAwareCallback result=" + zRegisterSceneAwareCallback);
            if (zRegisterSceneAwareCallback) {
                this.mSceneAwareCallback = nrcaSceneAwareCallback;
            }
        } catch (Exception e) {
            Log.e("NrcaModeController", "registerSceneAwareCallback exception: " + e.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDebug(Message message) {
        this.mDebug = true;
        Object obj = message.obj;
        if (obj instanceof Intent) {
            Intent intent = (Intent) obj;
            this.mStallDetecting = false;
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(7);
            logD("processDebug: isNrcaDisabled=" + isNrcaDisabled() + ", mIsHstState=" + this.mIsHstState);
            if (intent.hasExtra("nrca_enabled")) {
                boolean booleanExtra = intent.getBooleanExtra("nrca_enabled", true);
                setNrcaStatus(booleanExtra, this.mDdsPhoneId, "DEBUG");
                if (booleanExtra) {
                    resetDisableState();
                } else {
                    this.mDisablePhoneId = this.mDdsPhoneId;
                }
            }
            if (intent.hasExtra("schedule_result")) {
                if (!intent.getBooleanExtra("schedule_result", true)) {
                    disableNrCa();
                } else {
                    processNetworkQoeChanged(intent.getIntExtra("network_score", 0), 0);
                }
            }
            if (intent.getBooleanExtra("trigger_restore", false)) {
                processDelayNrcaRestore();
            }
        }
    }

    class NrcaBroadcastReceiver extends BroadcastReceiver {
        private NrcaBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            String action = intent.getAction();
            NrcaModeController.this.logD("onReceive action=" + action);
            handleAction(action, intent);
        }

        private void handleAction(String str, Intent intent) {
            if (NrcaModeController.this.mHandler == null) {
                return;
            }
            if ("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED".equals(str)) {
                NrcaModeController.this.mHandler.obtainMessage(3, intent).sendToTarget();
                return;
            }
            if ("com.android.phone.intent.action.HIT_HST_LINE_STATUS_CHANGED".equals(str)) {
                NrcaModeController.this.mHandler.obtainMessage(11, intent).sendToTarget();
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(str)) {
                NrcaModeController.this.mHandler.obtainMessage(4).sendToTarget();
                return;
            }
            if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(str)) {
                NrcaModeController.this.mHandler.obtainMessage(5).sendToTarget();
            } else if ("android.intent.action.SIM_STATE_CHANGED".equals(str)) {
                NrcaModeController.this.mHandler.obtainMessage(8, intent).sendToTarget();
            } else if ("com.android.phone.intent.action.NRCA_MODE_CONTROLLER_DEBUG".equals(str)) {
                NrcaModeController.this.mHandler.obtainMessage(100, intent).sendToTarget();
            }
        }
    }

    class NrcaTeleCallback implements TelephonyStatAdapter.ICallBack {
        public void onDataConnectionChanged(int i, int i2) {
        }

        public void onDataStatisticChanged(TelephonyStatAdapter.DataStatType dataStatType, int i, Map map) {
        }

        public void onPreciseCallStateChanged(int i, int i2) {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
        }

        private NrcaTeleCallback() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            if (NrcaModeController.this.mHandler == null) {
                return;
            }
            NrcaModeController.this.mHandler.obtainMessage(9, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            if (NrcaModeController.this.mHandler == null || cellLocation == null || !MiuiPhoneUtils.isValidatePhoneId(i)) {
                return;
            }
            NrcaModeController.this.mHandler.obtainMessage(6, i, 0).sendToTarget();
        }
    }

    class NrcaSceneAwareCallback extends SceneAwareCallback implements SceneAwareCallback.NetworkQoeListener {
        NrcaSceneAwareCallback(HandlerExecutor handlerExecutor) {
            super(handlerExecutor);
        }

        public void onNetworkQoeChanged(int i, int i2, int i3, int i4, int i5) {
            NrcaModeController.this.logD("onNetworkQoeChanged: slotId=" + i + ", pci=" + i2 + ", appType=" + i3 + ", reason=" + i4 + ", networkScore=" + i5);
            NrcaModeController.this.processNetworkQoeChanged(i5, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String str) {
        if (this.mDebug) {
            Log.d("NrcaModeController", str);
        }
    }

    private static void log(String str) {
        Log.d("NrcaModeController", str);
    }
}
