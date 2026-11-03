package com.xiaomi.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import com.xiaomi.modem.OemHookAgent;

/* JADX INFO: loaded from: classes.dex */
public class MEModuleSimAbnormalMonitor {
    public static final String ACTION_SIM_FAULT_DETECTED = "miui.intent.action.SIM_FAULT_DETECTED";
    private static final String ACTION_SIM_MONITOR_TEST = "miui.intent.action.SIM_MONITOR_TEST";
    private static final long ATR_REQUEST_TIMEOUT = 15000;
    private static final long BOOT_PHASE_TIMEOUT = 120000;
    public static final int EVENT_BOOT_SIM_NO_PRESENT = 3;
    public static final int EVENT_BOOT_SIM_NO_READY = 4;
    public static final int EVENT_SIM_INSERT_NO_PRESENT = 1;
    public static final int EVENT_SIM_INSERT_NO_READY = 2;
    private static final String EXTRA_ATR_DATA = "atr_data";
    public static final String EXTRA_EVENT_TYPE = "event_type";
    public static final String EXTRA_IS_BOOT_PHASE = "is_boot_phase";
    public static final String EXTRA_SLOT_ID = "slot_id";
    private static final String EXTRA_TEST_TYPE = "test_type";
    public static final String EXTRA_TIMESTAMP = "time_stamp";
    private static final String ICC_ABSENT = "ABSENT";
    private static final String ICC_LOADED = "LOADED";
    private static final String ICC_NOT_READY = "NOT_READY";
    private static final String ICC_PRESENT = "PRESENT";
    private static final String ICC_READY = "READY";
    private static final int MSG_ATR_RESPONSE_BASE = 200;
    private static final int MSG_ATR_TIMEOUT_BASE = 300;
    private static final int MSG_CHECK_SIM_STATE_BASE = 100;
    private static final String PROP_TEST_MODE = "persist.vendor.radio.sim_monitor_test";
    private static final long SIM_STATE_CHECK_DELAY = 10000;
    private static final String TAG = "MEModuleSimAbnormalMonitor";
    private static volatile MEModuleSimAbnormalMonitor mInstance;
    private String[] mAtrRequestState;
    private Context mContext;
    private String[] mCurrentSimState;
    private boolean[] mFaultReported;
    private String[] mLastSimState;
    private final int mPhoneCount;
    private long[] mPresentTimestamp;
    private static final Object mLock = new Object();
    protected static OemHookAgent mHookAgent = null;
    private boolean mIsBootPhase = true;
    private boolean mIsTestMode = false;
    private final Object stateLock = new Object();
    private Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            try {
                MEModuleSimAbnormalMonitor.this.routeMessage(msg.what, msg);
            } catch (Exception e) {
                MELog.e(MEModuleSimAbnormalMonitor.TAG, "handleMessage error: " + e.getMessage());
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                MELog.d(MEModuleSimAbnormalMonitor.TAG, "mBroadcastReceiver, intent is null");
                return;
            }
            String action = intent.getAction();
            MELog.d(MEModuleSimAbnormalMonitor.TAG, "BroadcastReceiver action: " + action);
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                int phoneId = intent.getIntExtra("phone", -1);
                String simState = intent.getStringExtra("ss");
                MELog.d(MEModuleSimAbnormalMonitor.TAG, "phoneId = " + phoneId + ", sim state = " + simState);
                if (phoneId >= 0 && phoneId < MEModuleSimAbnormalMonitor.this.mPhoneCount) {
                    MEModuleSimAbnormalMonitor.this.handleSimStateChanged(phoneId, simState);
                }
            }
        }
    };
    private BroadcastReceiver mTestReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                MELog.d(MEModuleSimAbnormalMonitor.TAG, "mTestReceiver, intent is null");
                return;
            }
            MEModuleSimAbnormalMonitor.this.checkTestMode();
            if (!MEModuleSimAbnormalMonitor.this.mIsTestMode) {
                MELog.d(MEModuleSimAbnormalMonitor.TAG, "Test mode is disabled, enable with: setprop persist.vendor.radio.sim_monitor_test 1");
                return;
            }
            String action = intent.getAction();
            MELog.d(MEModuleSimAbnormalMonitor.TAG, "mTestReceiver action: " + action);
            if (MEModuleSimAbnormalMonitor.ACTION_SIM_MONITOR_TEST.equals(action)) {
                int testType = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_TEST_TYPE, -1);
                int slotId = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, 0);
                String atrData = intent.getStringExtra(MEModuleSimAbnormalMonitor.EXTRA_ATR_DATA);
                int eventType = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_EVENT_TYPE, 1);
                boolean isBootPhase = intent.getBooleanExtra(MEModuleSimAbnormalMonitor.EXTRA_IS_BOOT_PHASE, false);
                MELog.d(MEModuleSimAbnormalMonitor.TAG, "Test: testType=" + testType + ", slotId=" + slotId + ", atrData=" + atrData + ", eventType=" + eventType + ", bootPhase=" + isBootPhase);
                MEModuleSimAbnormalMonitor.this.handleTestCommand(testType, slotId, atrData, eventType, isBootPhase);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void routeMessage(int what, Message msg) {
        int slotId = resolveSlotId(what, 100);
        if (slotId >= 0) {
            lambda$executeTestNoReady$2(slotId);
            return;
        }
        int slotId2 = resolveSlotId(what, 200);
        if (slotId2 >= 0) {
            handleAtrResponse(slotId2, msg);
            return;
        }
        int slotId3 = resolveSlotId(what, 300);
        if (slotId3 >= 0) {
            handleAtrTimeout(slotId3);
        }
    }

    private int resolveSlotId(int what, int base) {
        int slotId = what - base;
        if (slotId >= 0 && slotId < this.mPhoneCount) {
            return slotId;
        }
        return -1;
    }

    private MEModuleSimAbnormalMonitor(Context context) {
        MELog.d(TAG, "MEModuleSimAbnormalMonitor init...");
        this.mContext = context.getApplicationContext();
        int count = TelephonyManager.getDefault().getPhoneCount();
        this.mPhoneCount = Math.max(count, 1);
        MELog.d(TAG, "phoneCount=" + this.mPhoneCount);
        this.mLastSimState = new String[this.mPhoneCount];
        this.mCurrentSimState = new String[this.mPhoneCount];
        this.mPresentTimestamp = new long[this.mPhoneCount];
        this.mFaultReported = new boolean[this.mPhoneCount];
        this.mAtrRequestState = new String[this.mPhoneCount];
        for (int i = 0; i < this.mPhoneCount; i++) {
            this.mLastSimState[i] = null;
            this.mCurrentSimState[i] = null;
            this.mPresentTimestamp[i] = 0;
            this.mFaultReported[i] = false;
            this.mAtrRequestState[i] = null;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
        IntentFilter testFilter = new IntentFilter();
        testFilter.addAction(ACTION_SIM_MONITOR_TEST);
        this.mContext.registerReceiver(this.mTestReceiver, testFilter, 2);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (MEModuleSimAbnormalMonitor.this.stateLock) {
                    MEModuleSimAbnormalMonitor.this.mIsBootPhase = false;
                    MELog.d(MEModuleSimAbnormalMonitor.TAG, "Boot phase ended");
                }
            }
        }, BOOT_PHASE_TIMEOUT);
        checkTestMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkTestMode() {
        this.mIsTestMode = SystemProperties.getBoolean(PROP_TEST_MODE, false);
        MELog.d(TAG, "Test mode: " + this.mIsTestMode);
    }

    public static MEModuleSimAbnormalMonitor getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        MEModuleSimAbnormalMonitor instance = mInstance;
        if (instance == null) {
            synchronized (mLock) {
                instance = mInstance;
                if (instance == null) {
                    mHookAgent = XiaomiTelephonyComponentFactory.getHookAgent();
                    instance = new MEModuleSimAbnormalMonitor(context.getApplicationContext());
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTestCommand(int testType, int slotId, String atrData, int eventType, boolean isBootPhase) {
        if (slotId < 0 || slotId >= this.mPhoneCount) {
            MELog.e(TAG, "Invalid slotId: " + slotId);
            return;
        }
        switch (testType) {
            case 1:
            case 3:
                executeTestNoPresent(slotId, atrData, testType == 3);
                break;
            case 2:
            case 4:
                executeTestNoReady(slotId, testType == 4);
                break;
            case 5:
                executeTestCompleteStateFlow(slotId);
                break;
            case 6:
                executeTestDirectFaultReport(slotId, eventType, isBootPhase);
                break;
            case 7:
                executeTestResetState(slotId);
                break;
            case 8:
                executeTestSetBootPhase(isBootPhase);
                break;
            default:
                MELog.e(TAG, "Unknown test type: " + testType);
                printTestUsage();
                break;
        }
    }

    private void executeTestNoPresent(final int slotId, final String atrData, boolean bootPhase) {
        MELog.d(TAG, "Test: Simulating NoPresent for slot " + slotId + ", bootPhase=" + bootPhase);
        synchronized (this.stateLock) {
            this.mIsBootPhase = bootPhase;
            this.mFaultReported[slotId] = false;
        }
        handleSimStateChanged(slotId, ICC_ABSENT);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestNoPresent$1(slotId, atrData);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestNoPresent$1(final int slotId, final String atrData) {
        handleSimStateChanged(slotId, ICC_NOT_READY);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestNoPresent$0(slotId, atrData);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestNoPresent$0(int slotId, String atrData) {
        simulateAtrResponse(slotId, atrData != null ? atrData : "3B9F96C00A...");
    }

    private void executeTestNoReady(final int slotId, boolean bootPhase) {
        MELog.d(TAG, "Test: Simulating NoReady for slot " + slotId + ", bootPhase=" + bootPhase);
        synchronized (this.stateLock) {
            this.mIsBootPhase = bootPhase;
            this.mFaultReported[slotId] = false;
        }
        handleSimStateChanged(slotId, ICC_ABSENT);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestNoReady$4(slotId);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestNoReady$4(final int slotId) {
        handleSimStateChanged(slotId, ICC_NOT_READY);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestNoReady$3(slotId);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestNoReady$3(final int slotId) {
        handleSimStateChanged(slotId, ICC_PRESENT);
        synchronized (this.stateLock) {
            this.mPresentTimestamp[slotId] = (System.currentTimeMillis() - SIM_STATE_CHECK_DELAY) - 1000;
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestNoReady$2(slotId);
            }
        }, 500L);
    }

    private void executeTestCompleteStateFlow(final int slotId) {
        MELog.d(TAG, "Test: Complete state flow test for slot " + slotId);
        synchronized (this.stateLock) {
            this.mFaultReported[slotId] = false;
        }
        handleSimStateChanged(slotId, ICC_ABSENT);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestCompleteStateFlow$5(slotId);
            }
        }, 1000L);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestCompleteStateFlow$6(slotId);
            }
        }, 2000L);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestCompleteStateFlow$7(slotId);
            }
        }, 3000L);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestCompleteStateFlow$8(slotId);
            }
        }, 4000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestCompleteStateFlow$5(int slotId) {
        handleSimStateChanged(slotId, ICC_NOT_READY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestCompleteStateFlow$6(int slotId) {
        handleSimStateChanged(slotId, ICC_PRESENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestCompleteStateFlow$7(int slotId) {
        handleSimStateChanged(slotId, ICC_READY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestCompleteStateFlow$8(int slotId) {
        handleSimStateChanged(slotId, ICC_LOADED);
    }

    private void executeTestDirectFaultReport(int slotId, int eventType, boolean isBootPhase) {
        MELog.d(TAG, "Test: Direct fault report for slot " + slotId + ", eventType=" + eventType + ", bootPhase=" + isBootPhase);
        classifyAndReport(slotId, eventType, isBootPhase);
    }

    private void executeTestResetState(int slotId) {
        MELog.d(TAG, "Test: Reset state for slot " + slotId);
        synchronized (this.stateLock) {
            this.mFaultReported[slotId] = false;
            this.mPresentTimestamp[slotId] = 0;
            this.mCurrentSimState[slotId] = null;
            this.mLastSimState[slotId] = null;
            this.mAtrRequestState[slotId] = null;
        }
        this.mHandler.removeMessages(slotId + 100);
        this.mHandler.removeMessages(slotId + 300);
        MELog.d(TAG, "Test: State reset for slot " + slotId);
    }

    private void executeTestSetBootPhase(boolean isBootPhase) {
        synchronized (this.stateLock) {
            this.mIsBootPhase = isBootPhase;
        }
        MELog.d(TAG, "Test: Boot phase set to " + isBootPhase);
    }

    private void simulateAtrResponse(int slotId, String atrData) {
        MELog.d(TAG, "simulateAtrResponse for slot " + slotId + ", atrData=" + atrData);
        synchronized (this.stateLock) {
            this.mAtrRequestState[slotId] = this.mCurrentSimState[slotId];
        }
        String[] result = {"+ESIMINFO: " + atrData};
        Message msg = this.mHandler.obtainMessage(slotId + 200);
        msg.obj = new AsyncResult((Object) null, result, (Throwable) null);
        this.mHandler.sendMessage(msg);
    }

    private void printTestUsage() {
        MELog.d(TAG, "=== SIM Monitor Test Usage ===");
        MELog.d(TAG, "Enable test mode: setprop persist.vendor.radio.sim_monitor_test 1");
        MELog.d(TAG, "Test commands:");
        MELog.d(TAG, "  1: EVENT_SIM_INSERT_NO_PRESENT (Hot swap)");
        MELog.d(TAG, "  2: EVENT_SIM_INSERT_NO_READY (Hot swap)");
        MELog.d(TAG, "  3: EVENT_BOOT_SIM_NO_PRESENT (Boot)");
        MELog.d(TAG, "  4: EVENT_BOOT_SIM_NO_READY (Boot)");
        MELog.d(TAG, "  5: Complete state flow test");
        MELog.d(TAG, "  6: Direct fault report");
        MELog.d(TAG, "  7: Reset state");
        MELog.d(TAG, "  8: Set boot phase");
        MELog.d(TAG, "Example: adb shell am broadcast -a miui.intent.action.SIM_MONITOR_TEST --ei test_type 1 --ei slot_id 0");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimStateChanged(int slotId, String simState) {
        if (simState == null) {
            return;
        }
        synchronized (this.stateLock) {
            this.mLastSimState[slotId] = this.mCurrentSimState[slotId];
            this.mCurrentSimState[slotId] = simState;
            MELog.d(TAG, "Slot " + slotId + " state transition: " + this.mLastSimState[slotId] + " -> " + simState + ", bootPhase=" + this.mIsBootPhase);
            if (ICC_ABSENT.equals(simState)) {
                this.mFaultReported[slotId] = false;
                this.mPresentTimestamp[slotId] = 0;
                this.mAtrRequestState[slotId] = null;
                this.mHandler.removeMessages(slotId + 100);
                this.mHandler.removeMessages(slotId + 300);
                return;
            }
            if (ICC_NOT_READY.equals(simState)) {
                this.mHandler.removeMessages(slotId + 100);
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(slotId + 100), SIM_STATE_CHECK_DELAY);
            }
            if (ICC_PRESENT.equals(simState)) {
                this.mPresentTimestamp[slotId] = System.currentTimeMillis();
                this.mHandler.removeMessages(slotId + 100);
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(slotId + 100), SIM_STATE_CHECK_DELAY);
            }
            if (ICC_READY.equals(simState) || ICC_LOADED.equals(simState)) {
                this.mHandler.removeMessages(slotId + 100);
                this.mHandler.removeMessages(slotId + 300);
                this.mFaultReported[slotId] = false;
                this.mPresentTimestamp[slotId] = 0;
                this.mAtrRequestState[slotId] = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: checkAndClassify, reason: merged with bridge method [inline-methods] */
    public void lambda$executeTestNoReady$2(int slotId) {
        if (slotId < 0 || slotId >= this.mPhoneCount) {
            MELog.e(TAG, "checkAndClassify invalid slotId: " + slotId);
            return;
        }
        String stateToRequest = null;
        synchronized (this.stateLock) {
            if (this.mFaultReported[slotId]) {
                MELog.d(TAG, "Slot " + slotId + " fault already reported, skip");
                return;
            }
            String currentState = this.mCurrentSimState[slotId];
            MELog.d(TAG, "checkAndClassify slotId=" + slotId + ", currentState=" + currentState + ", isBootPhase=" + this.mIsBootPhase);
            if (ICC_NOT_READY.equals(currentState)) {
                stateToRequest = ICC_NOT_READY;
            } else if (ICC_PRESENT.equals(currentState) && this.mPresentTimestamp[slotId] > 0) {
                long elapsed = System.currentTimeMillis() - this.mPresentTimestamp[slotId];
                if (elapsed >= SIM_STATE_CHECK_DELAY) {
                    stateToRequest = ICC_PRESENT;
                }
            }
            if (stateToRequest != null) {
                sendAtrRequest(slotId, stateToRequest);
            }
        }
    }

    private void sendAtrRequest(int slotId, String requestState) {
        if (mHookAgent == null) {
            MELog.e(TAG, "mHookAgent is null, cannot send ATR request");
            return;
        }
        this.mAtrRequestState[slotId] = requestState;
        MELog.d(TAG, "sendAtrRequest for slot " + slotId + ", state=" + requestState);
        this.mHandler.removeMessages(slotId + 300);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(slotId + 300), ATR_REQUEST_TIMEOUT);
        mHookAgent.onMtkSendEsimsInfoAtCommand(slotId, this.mHandler, slotId + 200);
    }

    private void handleAtrTimeout(int slotId) {
        MELog.e(TAG, "ATR request timeout for slot " + slotId);
        synchronized (this.stateLock) {
            this.mAtrRequestState[slotId] = null;
        }
    }

    private void handleAtrResponse(int slotId, Message msg) {
        if (slotId < 0 || slotId >= this.mPhoneCount) {
            MELog.e(TAG, "handleAtrResponse invalid slotId: " + slotId);
            return;
        }
        this.mHandler.removeMessages(slotId + 300);
        boolean atrReceived = parseAtrFromMessage(msg, slotId);
        synchronized (this.stateLock) {
            if (this.mFaultReported[slotId]) {
                MELog.d(TAG, "Slot " + slotId + " fault already reported, skip");
                return;
            }
            String requestState = this.mAtrRequestState[slotId];
            String currentState = this.mCurrentSimState[slotId];
            boolean isBootPhase = this.mIsBootPhase;
            MELog.d(TAG, "handleAtrResponse slotId=" + slotId + ", atrReceived=" + atrReceived + ", requestState=" + requestState + ", currentState=" + currentState);
            if (requestState != null && requestState.equals(currentState)) {
                if (!atrReceived) {
                    MELog.d(TAG, "No valid ATR received, skip fault report");
                    this.mAtrRequestState[slotId] = null;
                    return;
                } else {
                    int eventType = resolveFaultEventType(requestState, isBootPhase);
                    this.mAtrRequestState[slotId] = null;
                    if (eventType > 0) {
                        classifyAndReport(slotId, eventType, isBootPhase);
                    }
                    return;
                }
            }
            MELog.d(TAG, "State changed since ATR request (" + requestState + " -> " + currentState + "), skip fault report");
            this.mAtrRequestState[slotId] = null;
        }
    }

    private boolean parseAtrFromMessage(Message msg, int slotId) {
        try {
            if (!(msg.obj instanceof AsyncResult)) {
                MELog.e(TAG, "handleAtrResponse msg.obj is not AsyncResult");
                return false;
            }
            AsyncResult ar = (AsyncResult) msg.obj;
            if (ar != null && ar.result != null && (ar.result instanceof String[])) {
                String[] result = (String[]) ar.result;
                StringBuilder sb = new StringBuilder();
                for (String s : result) {
                    if (s != null) {
                        sb.append(s).append(" ");
                    }
                }
                String response = sb.toString().trim();
                MELog.d(TAG, "ATR response for slot " + slotId + ": " + response);
                if (!response.contains("+ESIMINFO")) {
                    return false;
                }
                String[] parts = response.split(":");
                if (parts.length <= 1) {
                    return false;
                }
                String atrData = parts[1].trim();
                if (atrData.length() < 2) {
                    return false;
                }
                MELog.d(TAG, "Valid ATR received for slot " + slotId);
                return true;
            }
            MELog.d(TAG, "ATR response is null or not String[] for slot " + slotId);
            return false;
        } catch (Exception e) {
            MELog.e(TAG, "Error parsing ATR response for slot " + slotId + ": " + e.getMessage());
            return false;
        }
    }

    private int resolveFaultEventType(String requestState, boolean isBootPhase) {
        if (ICC_NOT_READY.equals(requestState)) {
            return isBootPhase ? 3 : 1;
        }
        if (ICC_PRESENT.equals(requestState)) {
            return isBootPhase ? 4 : 2;
        }
        return -1;
    }

    private void classifyAndReport(int slotId, int eventType, boolean isBootPhase) {
        MELog.d(TAG, "classifyAndReport slotId=" + slotId + ", eventType=" + eventType + ", isBootPhase=" + isBootPhase);
        if (slotId < 0 || slotId >= this.mPhoneCount) {
            MELog.e(TAG, "classifyAndReport invalid slotId: " + slotId);
            return;
        }
        if (this.mContext == null) {
            MELog.e(TAG, "classifyAndReport mContext is null");
            return;
        }
        try {
            Intent intent = new Intent(ACTION_SIM_FAULT_DETECTED);
            intent.putExtra(EXTRA_SLOT_ID, slotId);
            intent.putExtra(EXTRA_EVENT_TYPE, eventType);
            intent.putExtra(EXTRA_TIMESTAMP, System.currentTimeMillis());
            intent.putExtra(EXTRA_IS_BOOT_PHASE, isBootPhase);
            this.mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
            this.mFaultReported[slotId] = true;
            MELog.d(TAG, "Broadcast SIM fault detected: slot=" + slotId + ", eventType=" + eventType + ", isBootPhase=" + isBootPhase);
        } catch (Exception e) {
            MELog.e(TAG, "classifyAndReport broadcast error: " + e.getMessage());
        }
    }
}
