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
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

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
    protected static OemHookAgent mHookAgent;
    private static volatile MEModuleSimAbnormalMonitor mInstance;
    private static final Object mLock = new Object();
    private String[] mAtrRequestState;
    private Context mContext;
    private String[] mCurrentSimState;
    private boolean[] mFaultReported;
    private String[] mLastSimState;
    private final int mPhoneCount;
    private long[] mPresentTimestamp;
    private boolean mIsBootPhase = true;
    private boolean mIsTestMode = false;
    private final Object stateLock = new Object();
    private Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                MEModuleSimAbnormalMonitor.this.routeMessage(message.what, message);
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
                int intExtra = intent.getIntExtra("phone", -1);
                String stringExtra = intent.getStringExtra("ss");
                MELog.d(MEModuleSimAbnormalMonitor.TAG, "phoneId = " + intExtra + ", sim state = " + stringExtra);
                if (intExtra < 0 || intExtra >= MEModuleSimAbnormalMonitor.this.mPhoneCount) {
                    return;
                }
                MEModuleSimAbnormalMonitor.this.handleSimStateChanged(intExtra, stringExtra);
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
                int intExtra = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_TEST_TYPE, -1);
                int intExtra2 = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, 0);
                String stringExtra = intent.getStringExtra(MEModuleSimAbnormalMonitor.EXTRA_ATR_DATA);
                int intExtra3 = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_EVENT_TYPE, 1);
                boolean booleanExtra = intent.getBooleanExtra(MEModuleSimAbnormalMonitor.EXTRA_IS_BOOT_PHASE, false);
                MELog.d(MEModuleSimAbnormalMonitor.TAG, "Test: testType=" + intExtra + ", slotId=" + intExtra2 + ", atrData=" + stringExtra + ", eventType=" + intExtra3 + ", bootPhase=" + booleanExtra);
                MEModuleSimAbnormalMonitor.this.handleTestCommand(intExtra, intExtra2, stringExtra, intExtra3, booleanExtra);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void routeMessage(int i, Message message) {
        int iResolveSlotId = resolveSlotId(i, 100);
        if (iResolveSlotId >= 0) {
            lambda$executeTestNoReady$2(iResolveSlotId);
            return;
        }
        int iResolveSlotId2 = resolveSlotId(i, 200);
        if (iResolveSlotId2 >= 0) {
            handleAtrResponse(iResolveSlotId2, message);
            return;
        }
        int iResolveSlotId3 = resolveSlotId(i, 300);
        if (iResolveSlotId3 >= 0) {
            handleAtrTimeout(iResolveSlotId3);
        }
    }

    private int resolveSlotId(int i, int i2) {
        int i3 = i - i2;
        if (i3 < 0 || i3 >= this.mPhoneCount) {
            return -1;
        }
        return i3;
    }

    private MEModuleSimAbnormalMonitor(Context context) {
        MELog.d(TAG, "MEModuleSimAbnormalMonitor init...");
        this.mContext = context.getApplicationContext();
        int iMax = Math.max(TelephonyManager.getDefault().getPhoneCount(), 1);
        this.mPhoneCount = iMax;
        MELog.d(TAG, "phoneCount=" + iMax);
        this.mLastSimState = new String[iMax];
        this.mCurrentSimState = new String[iMax];
        this.mPresentTimestamp = new long[iMax];
        this.mFaultReported = new boolean[iMax];
        this.mAtrRequestState = new String[iMax];
        for (int i = 0; i < this.mPhoneCount; i++) {
            this.mLastSimState[i] = null;
            this.mCurrentSimState[i] = null;
            this.mPresentTimestamp[i] = 0;
            this.mFaultReported[i] = false;
            this.mAtrRequestState[i] = null;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(ACTION_SIM_MONITOR_TEST);
        this.mContext.registerReceiver(this.mTestReceiver, intentFilter2, 2);
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
        MEModuleSimAbnormalMonitor mEModuleSimAbnormalMonitor;
        if (context == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Context must not be null");
            return null;
        }
        MEModuleSimAbnormalMonitor mEModuleSimAbnormalMonitor2 = mInstance;
        if (mEModuleSimAbnormalMonitor2 != null) {
            return mEModuleSimAbnormalMonitor2;
        }
        synchronized (mLock) {
            try {
                mEModuleSimAbnormalMonitor = mInstance;
                if (mEModuleSimAbnormalMonitor == null) {
                    mHookAgent = XiaomiTelephonyComponentFactory.getHookAgent();
                    mEModuleSimAbnormalMonitor = new MEModuleSimAbnormalMonitor(context.getApplicationContext());
                    mInstance = mEModuleSimAbnormalMonitor;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return mEModuleSimAbnormalMonitor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTestCommand(int i, int i2, String str, int i3, boolean z) {
        if (i2 < 0 || i2 >= this.mPhoneCount) {
            MELog.e(TAG, "Invalid slotId: " + i2);
            return;
        }
        switch (i) {
            case 1:
            case 3:
                executeTestNoPresent(i2, str, i == 3);
                break;
            case 2:
            case 4:
                executeTestNoReady(i2, i == 4);
                break;
            case 5:
                executeTestCompleteStateFlow(i2);
                break;
            case 6:
                executeTestDirectFaultReport(i2, i3, z);
                break;
            case 7:
                executeTestResetState(i2);
                break;
            case 8:
                executeTestSetBootPhase(z);
                break;
            default:
                MELog.e(TAG, "Unknown test type: " + i);
                printTestUsage();
                break;
        }
    }

    private void executeTestNoPresent(final int i, final String str, boolean z) {
        MELog.d(TAG, "Test: Simulating NoPresent for slot " + i + ", bootPhase=" + z);
        synchronized (this.stateLock) {
            this.mIsBootPhase = z;
            this.mFaultReported[i] = false;
        }
        handleSimStateChanged(i, ICC_ABSENT);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestNoPresent$1(i, str);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestNoPresent$1(final int i, final String str) {
        handleSimStateChanged(i, ICC_NOT_READY);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestNoPresent$0(i, str);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestNoPresent$0(int i, String str) {
        if (str == null) {
            str = "3B9F96C00A...";
        }
        simulateAtrResponse(i, str);
    }

    private void executeTestNoReady(final int i, boolean z) {
        MELog.d(TAG, "Test: Simulating NoReady for slot " + i + ", bootPhase=" + z);
        synchronized (this.stateLock) {
            this.mIsBootPhase = z;
            this.mFaultReported[i] = false;
        }
        handleSimStateChanged(i, ICC_ABSENT);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestNoReady$4(i);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestNoReady$4(final int i) {
        handleSimStateChanged(i, ICC_NOT_READY);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestNoReady$3(i);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestNoReady$3(final int i) {
        handleSimStateChanged(i, ICC_PRESENT);
        synchronized (this.stateLock) {
            this.mPresentTimestamp[i] = System.currentTimeMillis() - 11000;
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestNoReady$2(i);
            }
        }, 500L);
    }

    private void executeTestCompleteStateFlow(final int i) {
        MELog.d(TAG, "Test: Complete state flow test for slot " + i);
        synchronized (this.stateLock) {
            this.mFaultReported[i] = false;
        }
        handleSimStateChanged(i, ICC_ABSENT);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestCompleteStateFlow$5(i);
            }
        }, 1000L);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestCompleteStateFlow$6(i);
            }
        }, 2000L);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestCompleteStateFlow$7(i);
            }
        }, 3000L);
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$executeTestCompleteStateFlow$8(i);
            }
        }, 4000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestCompleteStateFlow$5(int i) {
        handleSimStateChanged(i, ICC_NOT_READY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestCompleteStateFlow$6(int i) {
        handleSimStateChanged(i, ICC_PRESENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestCompleteStateFlow$7(int i) {
        handleSimStateChanged(i, ICC_READY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeTestCompleteStateFlow$8(int i) {
        handleSimStateChanged(i, ICC_LOADED);
    }

    private void executeTestDirectFaultReport(int i, int i2, boolean z) {
        MELog.d(TAG, "Test: Direct fault report for slot " + i + ", eventType=" + i2 + ", bootPhase=" + z);
        classifyAndReport(i, i2, z);
    }

    private void executeTestResetState(int i) {
        MELog.d(TAG, "Test: Reset state for slot " + i);
        synchronized (this.stateLock) {
            this.mFaultReported[i] = false;
            this.mPresentTimestamp[i] = 0;
            this.mCurrentSimState[i] = null;
            this.mLastSimState[i] = null;
            this.mAtrRequestState[i] = null;
        }
        this.mHandler.removeMessages(i + 100);
        this.mHandler.removeMessages(i + 300);
        MELog.d(TAG, "Test: State reset for slot " + i);
    }

    private void executeTestSetBootPhase(boolean z) {
        synchronized (this.stateLock) {
            this.mIsBootPhase = z;
        }
        MELog.d(TAG, "Test: Boot phase set to " + z);
    }

    private void simulateAtrResponse(int i, String str) {
        MELog.d(TAG, "simulateAtrResponse for slot " + i + ", atrData=" + str);
        synchronized (this.stateLock) {
            this.mAtrRequestState[i] = this.mCurrentSimState[i];
        }
        Message messageObtainMessage = this.mHandler.obtainMessage(i + 200);
        messageObtainMessage.obj = new AsyncResult((Object) null, new String[]{"+ESIMINFO: " + str}, (Throwable) null);
        this.mHandler.sendMessage(messageObtainMessage);
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
    public void handleSimStateChanged(int i, String str) {
        if (str == null) {
            return;
        }
        synchronized (this.stateLock) {
            try {
                String[] strArr = this.mLastSimState;
                String[] strArr2 = this.mCurrentSimState;
                strArr[i] = strArr2[i];
                strArr2[i] = str;
                MELog.d(TAG, "Slot " + i + " state transition: " + this.mLastSimState[i] + " -> " + str + ", bootPhase=" + this.mIsBootPhase);
                if (ICC_ABSENT.equals(str)) {
                    this.mFaultReported[i] = false;
                    this.mPresentTimestamp[i] = 0;
                    this.mAtrRequestState[i] = null;
                    this.mHandler.removeMessages(i + 100);
                    this.mHandler.removeMessages(i + 300);
                    return;
                }
                if (ICC_NOT_READY.equals(str)) {
                    int i2 = i + 100;
                    this.mHandler.removeMessages(i2);
                    Handler handler = this.mHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(i2), SIM_STATE_CHECK_DELAY);
                }
                if (ICC_PRESENT.equals(str)) {
                    this.mPresentTimestamp[i] = System.currentTimeMillis();
                    int i3 = i + 100;
                    this.mHandler.removeMessages(i3);
                    Handler handler2 = this.mHandler;
                    handler2.sendMessageDelayed(handler2.obtainMessage(i3), SIM_STATE_CHECK_DELAY);
                }
                if (ICC_READY.equals(str) || ICC_LOADED.equals(str)) {
                    this.mHandler.removeMessages(i + 100);
                    this.mHandler.removeMessages(i + 300);
                    this.mFaultReported[i] = false;
                    this.mPresentTimestamp[i] = 0;
                    this.mAtrRequestState[i] = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: checkAndClassify, reason: merged with bridge method [inline-methods] */
    public void lambda$executeTestNoReady$2(int i) {
        String str;
        if (i < 0 || i >= this.mPhoneCount) {
            MELog.e(TAG, "checkAndClassify invalid slotId: " + i);
            return;
        }
        synchronized (this.stateLock) {
            try {
                if (this.mFaultReported[i]) {
                    MELog.d(TAG, "Slot " + i + " fault already reported, skip");
                    return;
                }
                String str2 = this.mCurrentSimState[i];
                MELog.d(TAG, "checkAndClassify slotId=" + i + ", currentState=" + str2 + ", isBootPhase=" + this.mIsBootPhase);
                if (ICC_NOT_READY.equals(str2)) {
                    str = ICC_NOT_READY;
                } else {
                    str = (!ICC_PRESENT.equals(str2) || this.mPresentTimestamp[i] <= 0 || System.currentTimeMillis() - this.mPresentTimestamp[i] < SIM_STATE_CHECK_DELAY) ? null : ICC_PRESENT;
                }
                if (str != null) {
                    sendAtrRequest(i, str);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void sendAtrRequest(int i, String str) {
        if (mHookAgent == null) {
            MELog.e(TAG, "mHookAgent is null, cannot send ATR request");
            return;
        }
        this.mAtrRequestState[i] = str;
        MELog.d(TAG, "sendAtrRequest for slot " + i + ", state=" + str);
        int i2 = i + 300;
        this.mHandler.removeMessages(i2);
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(i2), ATR_REQUEST_TIMEOUT);
        mHookAgent.onMtkSendEsimsInfoAtCommand(i, this.mHandler, i + 200);
    }

    private void handleAtrTimeout(int i) {
        MELog.e(TAG, "ATR request timeout for slot " + i);
        synchronized (this.stateLock) {
            this.mAtrRequestState[i] = null;
        }
    }

    private void handleAtrResponse(int i, Message message) {
        if (i < 0 || i >= this.mPhoneCount) {
            MELog.e(TAG, "handleAtrResponse invalid slotId: " + i);
            return;
        }
        this.mHandler.removeMessages(i + 300);
        boolean atrFromMessage = parseAtrFromMessage(message, i);
        synchronized (this.stateLock) {
            try {
                if (this.mFaultReported[i]) {
                    MELog.d(TAG, "Slot " + i + " fault already reported, skip");
                    return;
                }
                String str = this.mAtrRequestState[i];
                String str2 = this.mCurrentSimState[i];
                boolean z = this.mIsBootPhase;
                MELog.d(TAG, "handleAtrResponse slotId=" + i + ", atrReceived=" + atrFromMessage + ", requestState=" + str + ", currentState=" + str2);
                if (str != null && str.equals(str2)) {
                    if (!atrFromMessage) {
                        MELog.d(TAG, "No valid ATR received, skip fault report");
                        this.mAtrRequestState[i] = null;
                        return;
                    } else {
                        int iResolveFaultEventType = resolveFaultEventType(str, z);
                        this.mAtrRequestState[i] = null;
                        if (iResolveFaultEventType > 0) {
                            classifyAndReport(i, iResolveFaultEventType, z);
                        }
                        return;
                    }
                }
                MELog.d(TAG, "State changed since ATR request (" + str + " -> " + str2 + "), skip fault report");
                this.mAtrRequestState[i] = null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private boolean parseAtrFromMessage(Message message, int i) {
        Object obj;
        try {
            Object obj2 = message.obj;
            if (!(obj2 instanceof AsyncResult)) {
                MELog.e(TAG, "handleAtrResponse msg.obj is not AsyncResult");
                return false;
            }
            AsyncResult asyncResult = (AsyncResult) obj2;
            if (asyncResult != null && (obj = asyncResult.result) != null && (obj instanceof String[])) {
                StringBuilder sb = new StringBuilder();
                for (String str : (String[]) obj) {
                    if (str != null) {
                        sb.append(str);
                        sb.append(" ");
                    }
                }
                String strTrim = sb.toString().trim();
                MELog.d(TAG, "ATR response for slot " + i + ": " + strTrim);
                if (!strTrim.contains("+ESIMINFO")) {
                    return false;
                }
                String[] strArrSplit = strTrim.split(":");
                if (strArrSplit.length <= 1 || strArrSplit[1].trim().length() < 2) {
                    return false;
                }
                MELog.d(TAG, "Valid ATR received for slot " + i);
                return true;
            }
            MELog.d(TAG, "ATR response is null or not String[] for slot " + i);
            return false;
        } catch (Exception e) {
            MELog.e(TAG, "Error parsing ATR response for slot " + i + ": " + e.getMessage());
            return false;
        }
    }

    private int resolveFaultEventType(String str, boolean z) {
        if (ICC_NOT_READY.equals(str)) {
            return z ? 3 : 1;
        }
        if (ICC_PRESENT.equals(str)) {
            return z ? 4 : 2;
        }
        return -1;
    }

    private void classifyAndReport(int i, int i2, boolean z) {
        MELog.d(TAG, "classifyAndReport slotId=" + i + ", eventType=" + i2 + ", isBootPhase=" + z);
        if (i < 0 || i >= this.mPhoneCount) {
            MELog.e(TAG, "classifyAndReport invalid slotId: " + i);
            return;
        }
        if (this.mContext == null) {
            MELog.e(TAG, "classifyAndReport mContext is null");
            return;
        }
        try {
            Intent intent = new Intent(ACTION_SIM_FAULT_DETECTED);
            intent.putExtra(EXTRA_SLOT_ID, i);
            intent.putExtra(EXTRA_EVENT_TYPE, i2);
            intent.putExtra(EXTRA_TIMESTAMP, System.currentTimeMillis());
            intent.putExtra(EXTRA_IS_BOOT_PHASE, z);
            this.mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
            this.mFaultReported[i] = true;
            MELog.d(TAG, "Broadcast SIM fault detected: slot=" + i + ", eventType=" + i2 + ", isBootPhase=" + z);
        } catch (Exception e) {
            MELog.e(TAG, "classifyAndReport broadcast error: " + e.getMessage());
        }
    }
}
