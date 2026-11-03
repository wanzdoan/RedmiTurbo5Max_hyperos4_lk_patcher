package com.android.phone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.module.IUtilityModule;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.NetworkSpeedTracker;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.miui.misight.MiEvent;
import com.miui.misight.MiSight;
import com.ot.pubsub.a.a;
import com.ot.pubsub.util.v;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import miui.os.Build;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class SpecialSimManager {
    private static final boolean IS_INTERNATIONAL_BUILD = Build.IS_INTERNATIONAL_BUILD;
    private static long PENALTY_TIMER = 259200000;
    private static Context mContext;
    private static SpecialSimManager sInstance;
    HashMap mArrearSims;
    private int mAutoCaptureLogMask;
    private int mDelayValidTimer;
    private final BaseEvent.IEventListener mDisplayListener;
    private int mFakeConnectionCount;
    private SmartSimHandler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    private volatile boolean mIsAutoLogPropVerified;
    private boolean[] mIsCorrectionSuccess;
    private boolean[] mIsInValidSim;
    private boolean mIsNoBillSim;
    private boolean mIsNwDeniedModeCloudOn;
    private boolean[] mIsOosTimerWorking;
    private boolean mIsScreenOn;
    private boolean[] mIsSendBillQuery;
    private long[] mLastRegLteNrTime;
    private long[] mLastTime;
    private long[] mLastTimeBillQuery;
    private boolean[] mNwDenied;
    private LinkedList[] mNwDeniedTimeQueue;
    private RilHook mRilHook;
    private ServiceState[] mServiceState;
    private long[] mStartTimeForOos;
    private TelephonyInfoCallBack mTelephonyInfoCallBack;
    private HandlerThread mWorkThread;
    private boolean VDBG = false;
    private long mDelayForIdentityInvalidSim = -1;

    private boolean isNwDenied(int i) {
        return i == 7 || i == 15;
    }

    private boolean isOtherNwDenied(int i) {
        return (i == -1 || i == 0 || i == 7 || i == 15) ? false : true;
    }

    public enum State {
        NW_DENIED(0),
        NW_DETACHED(1),
        MOBILE_DATA_DISABLED(2),
        MOBILE_DATA_DISCONNECTED(3),
        DATA_STALL(4),
        USER_BUSY(5),
        INVALID_PARAM(6),
        VALID(7);

        private int value;

        State(int i) {
            this.value = i;
        }
    }

    public static synchronized SpecialSimManager getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new SpecialSimManager();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private SpecialSimManager() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mStartTimeForOos = new long[i];
        this.mServiceState = new ServiceState[i];
        this.mIsOosTimerWorking = new boolean[i];
        this.mIsInValidSim = new boolean[i];
        this.mIsSendBillQuery = new boolean[i];
        this.mLastTime = new long[i];
        this.mLastTimeBillQuery = new long[i];
        this.mIsAutoLogPropVerified = false;
        this.mAutoCaptureLogMask = -1;
        this.mIsNoBillSim = false;
        this.mTelephonyInfoCallBack = null;
        this.mFakeConnectionCount = 0;
        this.mArrearSims = new HashMap();
        this.mNwDenied = new boolean[i];
        this.mDelayValidTimer = 60000;
        this.mNwDeniedTimeQueue = new LinkedList[i];
        this.mIsCorrectionSuccess = new boolean[i];
        this.mLastRegLteNrTime = new long[i];
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.SpecialSimManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                SpecialSimManager.this.log("receive action".concat(action));
                if ("com.android.phone.intent.action.ARREARS_SIM_QUERY".equals(action)) {
                    SpecialSimManager.this.mHandler.obtainMessage(3, Integer.valueOf(intent.getIntExtra("slotId", -1))).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.ARREARS_SIM_NOTIFICATION".equals(action)) {
                    SpecialSimManager.this.mHandler.obtainMessage(5, Integer.valueOf(intent.getIntExtra("slotId", -1))).sendToTarget();
                    return;
                }
                if ("com.android.phone.intent.action.NA_SIM_BILL_QUERY".equals(action)) {
                    int intExtra = intent.getIntExtra("slotId", -1);
                    SpecialSimManager.this.log("phoneId=" + intExtra);
                    if (MiuiPhoneUtils.isValidatePhoneId(intExtra) && SpecialSimManager.this.mIsSendBillQuery[intExtra]) {
                        SpecialSimManager.this.mIsSendBillQuery[intExtra] = false;
                        SpecialSimManager.this.mHandler.obtainMessage(8, intent).sendToTarget();
                        return;
                    }
                    return;
                }
                if ("com.android.phone.intent.action.ACTION_CLOUD_ARREAR_SIM_NW_DENIED_DONE".equals(action)) {
                    SpecialSimManager.this.mHandler.sendEmptyMessage(11);
                } else if ("SpecialSimManager.intent.debug".equals(action)) {
                    SpecialSimManager.this.mHandler.obtainMessage(100, intent).sendToTarget();
                }
            }
        };
        this.mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.SpecialSimManager.2
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                    if (displayEvent.isDefaultDisplay()) {
                        SpecialSimManager.this.mHandler.obtainMessage(6, displayEvent).sendToTarget();
                    }
                }
            }
        };
        log("SpecialSimManager init");
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        mContext = phoneGlobals;
        this.mRilHook = RilHook.init(phoneGlobals);
        HandlerThread handlerThread = new HandlerThread("SpecialSimManager", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        SmartSimHandler smartSimHandler = new SmartSimHandler(this.mWorkThread.getLooper());
        this.mHandler = smartSimHandler;
        smartSimHandler.sendEmptyMessage(1);
    }

    class SmartSimHandler extends Handler {
        SmartSimHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            int i2 = 0;
            if (i != 100) {
                if (i != 1000) {
                    switch (i) {
                        case 1:
                            SpecialSimManager.this.init();
                            break;
                        case 2:
                            SpecialSimManager.this.processDataStatisticChange(message.arg1, message.arg2, (HashMap) message.obj);
                            break;
                        case 3:
                            SpecialSimManager.this.onArrearsSimChanged(((Integer) message.obj).intValue());
                            break;
                        case 4:
                            SpecialSimManager.this.validNetowrkOnArrearsSim(((Integer) message.obj).intValue());
                            break;
                        case 5:
                            SpecialSimManager.this.recordUrlArrearSimNotification(((Integer) message.obj).intValue());
                            if (!SpecialSimManager.this.isLingerTimeBillQuery(((Integer) message.obj).intValue())) {
                                SpecialSimManager.this.sendSimBillQuery(((Integer) message.obj).intValue(), false);
                                SpecialSimManager specialSimManager = SpecialSimManager.this;
                                specialSimManager.notifyDfsArrearSimStatus(specialSimManager.getActiveDataPhoneId());
                            }
                            SpecialSimManager.this.mIsNoBillSim = true;
                            break;
                        case 6:
                            boolean zIsScreenOn = ((DeviceStateEvent.DisplayEvent) message.obj).isScreenOn();
                            if (zIsScreenOn != SpecialSimManager.this.mIsScreenOn) {
                                SpecialSimManager.this.mIsScreenOn = zIsScreenOn;
                                SpecialSimManager.this.onScreenStateChanged();
                                break;
                            }
                            break;
                        case 7:
                            SpecialSimManager.this.onServiceStateChanged(message.arg1, (ServiceState) message.obj);
                            break;
                        case 8:
                            SpecialSimManager.this.processSimBillResultNotification((Intent) message.obj);
                            break;
                        case 9:
                            SpecialSimManager.this.log("EVENT_SIM_BILL_QUERY_LINGER, phoneid=" + ((Integer) message.obj).intValue());
                            SpecialSimManager.this.mIsSendBillQuery[((Integer) message.obj).intValue()] = false;
                            break;
                        case 10:
                            SpecialSimManager.this.processNwDeniedLinger(((Integer) message.obj).intValue());
                            break;
                        case 11:
                            SpecialSimManager specialSimManager2 = SpecialSimManager.this;
                            specialSimManager2.mIsNwDeniedModeCloudOn = specialSimManager2.getArrearSimNwDeniedCloud();
                            break;
                    }
                    return;
                }
                int iIntValue = ((Integer) message.obj).intValue();
                SpecialSimManager.this.log("receive event EVENT_STATISTIC_INVALID_SIM: phone id =" + iIntValue + " is invalid sim card ");
                SpecialSimManager.this.mIsInValidSim[iIntValue] = true;
                SpecialSimManager.this.mIsOosTimerWorking[iIntValue] = false;
                SpecialSimManager.this.mStartTimeForOos[iIntValue] = -1;
                SpecialSimManager specialSimManager3 = SpecialSimManager.this;
                specialSimManager3.sendInvalidSimStatusIfNeed(iIntValue, specialSimManager3.mIsInValidSim[iIntValue]);
                return;
            }
            SpecialSimManager.this.VDBG = true;
            Intent intent = (Intent) message.obj;
            if (intent == null) {
                return;
            }
            SpecialSimManager.this.mDelayValidTimer = intent.getIntExtra("delay_valid", 60000);
            long longExtra = intent.getLongExtra("delay_invalid_sim", -1L);
            if (longExtra != -1) {
                SpecialSimManager.this.mDelayForIdentityInvalidSim = longExtra;
                SpecialSimManager.this.log("set mDelayForIdentityInvalidSim=" + SpecialSimManager.this.mDelayForIdentityInvalidSim);
            }
            while (true) {
                int i3 = MiuiPhoneUtils.PHONE_COUNT;
                SpecialSimManager specialSimManager4 = SpecialSimManager.this;
                if (i2 < i3) {
                    specialSimManager4.mIsSendBillQuery[i2] = true;
                    i2++;
                } else {
                    specialSimManager4.log("debug action mDelayValidTimer: " + SpecialSimManager.this.mDelayValidTimer + ",mIsSendBillQuery: " + Arrays.toString(SpecialSimManager.this.mIsSendBillQuery));
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNwDeniedLinger(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("processNwDeniedLinger");
            if (isLteNrNwType(this.mServiceState[i])) {
                clearNwDeniedTimeQueue(i);
                return;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.mNwDeniedTimeQueue[i].offerLast(Long.valueOf(jCurrentTimeMillis));
            log("mNwDeniedTimeQueue phoneId=" + i + ", size=" + this.mNwDeniedTimeQueue[i].size());
            while (this.mNwDeniedTimeQueue[i].size() >= 3) {
                if (jCurrentTimeMillis - ((Long) this.mNwDeniedTimeQueue[i].peekFirst()).longValue() <= 3000000) {
                    if (!this.mIsCorrectionSuccess[i] && isPreviousRegLteNr(i) && !isLingerTime(i)) {
                        if (this.mIsNwDeniedModeCloudOn) {
                            showArrearSimNotification(i);
                        }
                        boolean zIsDefaultDataSlotId = MiuiPhoneUtils.isDefaultDataSlotId(i);
                        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(i);
                        localLog("ArrearSimNwDenied", "phoneId=" + i + ", isDds=" + zIsDefaultDataSlotId + ", plmn=" + simOperatorNumeric + ", cloudOn=" + this.mIsNwDeniedModeCloudOn);
                        recordNetworkDeniedNotification(i, zIsDefaultDataSlotId, simOperatorNumeric);
                        notifyDfsArrearSimStatus(i);
                    }
                    this.mNwDeniedTimeQueue[i].clear();
                } else {
                    this.mNwDeniedTimeQueue[i].pollFirst();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mNwDenied[i] = false;
            this.mServiceState[i] = new ServiceState();
            this.mIsInValidSim[i] = false;
            this.mIsSendBillQuery[i] = false;
            this.mIsCorrectionSuccess[i] = false;
            this.mNwDeniedTimeQueue[i] = new LinkedList();
            this.mLastTime[i] = -1;
            this.mLastTimeBillQuery[i] = -1;
            this.mLastRegLteNrTime[i] = -1;
            this.mStartTimeForOos[i] = -1;
        }
        this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(mContext);
        this.mDelayForIdentityInvalidSim = mContext.getResources().getInteger(R.integer.config_identity_invalid_sim_delay);
        log("mDelayForIdentityInvalidSim=" + this.mDelayForIdentityInvalidSim);
        this.mIsNwDeniedModeCloudOn = getArrearSimNwDeniedCloud();
        registerReceiver();
        registerTelephonyInfoCallBack();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        if (!IS_INTERNATIONAL_BUILD) {
            intentFilter.addAction("com.android.phone.intent.action.ARREARS_SIM_QUERY");
            intentFilter.addAction("com.android.phone.intent.action.ARREARS_SIM_NOTIFICATION");
            intentFilter.addAction("com.android.phone.intent.action.NA_SIM_BILL_QUERY");
            intentFilter.addAction("com.android.phone.intent.action.ACTION_CLOUD_ARREAR_SIM_NW_DENIED_DONE");
        }
        intentFilter.addAction("SpecialSimManager.intent.debug");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    private synchronized void registerScreenState() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
    }

    private synchronized void unregisterScreenState() {
        EventHubManager.getInstance().unregisterListener(this.mDisplayListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onServiceStateChanged(int i, ServiceState serviceState) {
        if (serviceState == null || !MiuiPhoneUtils.isValidatePhoneId(i) || serviceState.getIsManualSelection()) {
            return;
        }
        int dataRegistrationState = serviceState.getDataRegistrationState();
        int voiceRegState = serviceState.getVoiceRegState();
        log("[dataState][" + i + "]=" + dataRegistrationState + " [voiceState][" + i + "]=" + voiceRegState + " [mIsInValidSim][" + i + "]=" + this.mIsInValidSim[i] + " [mIsOosTimerWorking][" + i + "]=" + this.mIsOosTimerWorking[i]);
        if ((dataRegistrationState == 0 && voiceRegState == 0) || dataRegistrationState == 3 || voiceRegState == 3) {
            boolean[] zArr = this.mNwDenied;
            if (zArr[i]) {
                zArr[i] = false;
            }
            if (isLteNrNwType(serviceState)) {
                stopNwDeniedTimerById(i);
                clearNwDeniedTimeQueue(i);
                this.mLastRegLteNrTime[i] = System.currentTimeMillis();
            }
        } else if (dataRegistrationState == 1 || voiceRegState == 1) {
            int psRegRejectCause = getPsRegRejectCause(serviceState);
            int csRegRejectCause = getCsRegRejectCause(serviceState);
            if (isNwDenied(psRegRejectCause) || isNwDenied(csRegRejectCause)) {
                if (!isLingerTimeBillQuery(i)) {
                    sendSimBillQuery(i, true);
                }
                log("detach rejectPsCause:" + psRegRejectCause + ", rejectCsCause:" + csRegRejectCause + ", phoneId:" + i);
                startNwDeniedTimerById(i);
                this.mNwDenied[i] = true;
            } else if (isOtherNwDenied(psRegRejectCause) || isOtherNwDenied(csRegRejectCause)) {
                clearNwDeniedTimeQueue(i);
            }
        }
        if (voiceRegState == 0) {
            if (this.mIsInValidSim[i] && SpecialSimBase.getInstance() != null) {
                SpecialSimBase.getInstance().sendInvalidSimState(i, 0);
            }
            this.mIsInValidSim[i] = false;
            stopNwOOSTimerById(i);
            this.mStartTimeForOos[i] = -1;
        } else if (voiceRegState == 1) {
            if (!isNormalCaseForVoiceDetach(i) && !this.mIsOosTimerWorking[i] && !this.mIsInValidSim[i]) {
                this.mStartTimeForOos[i] = SystemClock.elapsedRealtime();
                startNwOOSTimerById(i);
            } else if (this.mIsOosTimerWorking[i] && this.mStartTimeForOos[i] != -1) {
                long jElapsedRealtime = SystemClock.elapsedRealtime() - this.mStartTimeForOos[i];
                log("oosDuration[" + i + "]=" + jElapsedRealtime);
                if (jElapsedRealtime >= this.mDelayForIdentityInvalidSim) {
                    this.mIsInValidSim[i] = true;
                    stopNwOOSTimerById(i);
                    this.mStartTimeForOos[i] = -1;
                    sendInvalidSimStatusIfNeed(i, this.mIsInValidSim[i]);
                }
            }
        }
        this.mServiceState[i] = serviceState;
    }

    private boolean isLteNrNwType(ServiceState serviceState) {
        boolean z = false;
        if (serviceState != null) {
            int dataRegistrationState = serviceState.getDataRegistrationState();
            int voiceRegState = serviceState.getVoiceRegState();
            if (dataRegistrationState == 0 && voiceRegState == 0) {
                int dataNetworkType = serviceState.getDataNetworkType();
                int voiceNetworkType = serviceState.getVoiceNetworkType();
                if (dataNetworkType == 13 || dataNetworkType == 19 || dataNetworkType == 20 || dataNetworkType == 30 || voiceNetworkType == 13 || voiceNetworkType == 19 || voiceNetworkType == 20 || voiceNetworkType == 30) {
                    z = true;
                }
            }
        }
        log("isLteNrNwType rst=" + z);
        return z;
    }

    private boolean isNormalCaseForVoiceDetach(int i) {
        boolean z = TelephonyManager.getDefault().getSimState(i) == 5;
        boolean z2 = Settings.Global.getInt(mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
        log("isNormalCaseForVoiceDetach simReady= " + z + " airplane= " + z2);
        return z2 || !z;
    }

    private void startNwOOSTimerById(int i) {
        log("startNwOOSTimerById: phoneid = " + i);
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            stopNwOOSTimerById(i);
            if (this.mIsOosTimerWorking[i]) {
                return;
            }
            SmartSimHandler smartSimHandler = this.mHandler;
            smartSimHandler.sendMessageDelayed(smartSimHandler.obtainMessage(1000, Integer.valueOf(i)), this.mDelayForIdentityInvalidSim);
            this.mIsOosTimerWorking[i] = true;
        }
    }

    private void stopNwOOSTimerById(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && this.mIsOosTimerWorking[i]) {
            if (this.mHandler.hasMessages(1000, Integer.valueOf(i))) {
                this.mHandler.removeMessages(1000, Integer.valueOf(i));
                log("stopNwOOSTimerById: phoneid = " + i);
            }
            this.mIsOosTimerWorking[i] = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSimBillQuery(int i, boolean z) {
        if (IS_INTERNATIONAL_BUILD || !MiuiPhoneUtils.isValidatePhoneId(i)) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.SIM_BILL_QUERY");
        intent.putExtra("slotId", i);
        intent.putExtra("flag", z);
        mContext.sendBroadcast(intent);
        this.mIsCorrectionSuccess[i] = false;
        this.mIsSendBillQuery[i] = true;
        startSimBillQueryTimer(i);
        localLog("sendSimBillQuery", "broadcast for sim bill query,phoneId=" + i + ",flag=" + z);
    }

    private void startSimBillQueryTimer(int i) {
        log("startSimBillQueryTimer: phoneId=" + i);
        stopSimBillQueryTimer();
        SmartSimHandler smartSimHandler = this.mHandler;
        smartSimHandler.sendMessageDelayed(smartSimHandler.obtainMessage(9, Integer.valueOf(i)), 600000L);
    }

    private void stopSimBillQueryTimer() {
        if (this.mHandler.hasMessages(9)) {
            log("stopSimBillQueryTimer");
            this.mHandler.removeMessages(9);
        }
    }

    private synchronized void dfsEventForMtb(int i) {
        try {
            if (mContext == null) {
                return;
            }
            if (!this.mIsAutoLogPropVerified) {
                this.mAutoCaptureLogMask = SystemProperties.getInt(ModemServiceMoniter.PROPERTY_AUTO_CAPTURE_LOG_MASK, -1);
                this.mIsAutoLogPropVerified = true;
            }
            if (1 == this.mAutoCaptureLogMask) {
                Intent intent = new Intent("android.intent.action.DFS_MODEM_EVENT_FOR_MTB");
                intent.setFlags(16777216);
                intent.putExtra("eventId", i);
                intent.setPackage("com.xiaomi.mtb");
                mContext.sendBroadcast(intent);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendInvalidSimStatusIfNeed(int i, boolean z) {
        int dataRegistrationState = this.mServiceState[i].getDataRegistrationState();
        int voiceRegState = this.mServiceState[i].getVoiceRegState();
        int i2 = z ? 1 : 0;
        log("value=" + i2 + ", dataState=" + dataRegistrationState + ", voiceState" + voiceRegState);
        if (!isNormalCaseForVoiceDetach(i) && dataRegistrationState == 1 && voiceRegState == 1) {
            if (!isOtherPhoneInService(i)) {
                this.mIsInValidSim[i] = false;
                localLog("sendInvalidSim", "phoneid=" + i + ", skip");
                return;
            }
            this.mRilHook.setInvalidSimState(i, i2);
            if (SpecialSimBase.getInstance() != null) {
                SpecialSimBase.getInstance().sendInvalidSimState(i, i2);
            }
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).trackerMiEvent(915300006, i);
            dfsEventForMtb(915300006);
            recordInvalidSimDetected(i, i2);
            localLog("sendInvalidSim", "phoneid=" + i + ", status=" + z);
        }
    }

    private boolean isOtherPhoneInService(int i) {
        if (MiuiPhoneUtils.getInsertedSimCount() < 2 || !MiuiPhoneUtils.isValidatePhoneId(i)) {
            return false;
        }
        ServiceState serviceState = this.mServiceState[i == 0 ? (char) 1 : (char) 0];
        return serviceState.getDataRegistrationState() == 0 || serviceState.getVoiceRegState() == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onArrearsSimChanged(int i) {
        registerScreenState();
        clearValidCount(i);
        validNetowrkOnArrearsSim(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void validNetowrkOnArrearsSim(int i) {
        try {
            log("valid network on arrears sim: " + i);
            State stateValidNetwork = validNetwork(i);
            switch (stateValidNetwork) {
                case NW_DENIED:
                    onValidArrearSimDone(i, 1);
                    break;
                case NW_DETACHED:
                case MOBILE_DATA_DISCONNECTED:
                case DATA_STALL:
                    processValidState(i, stateValidNetwork);
                    break;
                case MOBILE_DATA_DISABLED:
                case USER_BUSY:
                case INVALID_PARAM:
                    onValidArrearSimDone(i, -1);
                    break;
                case VALID:
                    onValidArrearSimDone(i, 0);
                    break;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void retryValidNetwork(int i, int i2) {
        this.mHandler.removeMessages(4);
        SmartSimHandler smartSimHandler = this.mHandler;
        smartSimHandler.sendMessageDelayed(smartSimHandler.obtainMessage(4, Integer.valueOf(i)), i2 * this.mDelayValidTimer);
    }

    private void processValidState(int i, State state) {
        int validCount = getValidCount(i);
        log("valid state:" + state + ", phoneId: " + i + ", count: " + validCount);
        if (validCount < 3) {
            int i2 = validCount + 1;
            updateValidCount(i, i2);
            if (this.mIsScreenOn) {
                retryValidNetwork(i, i2);
                return;
            }
            return;
        }
        onValidArrearSimDone(i, 1);
    }

    private void onValidArrearSimDone(int i, int i2) {
        sendValidResult(i, i2);
        clearValidCount(i);
        unregisterScreenState();
    }

    private synchronized void sendValidResult(int i, int i2) {
        try {
            Intent intent = new Intent();
            intent.setAction("com.android.phone.intent.action.ARREARS_SIM_RESULT");
            intent.putExtra(a.L, i2);
            intent.putExtra("slotId", i);
            mContext.sendBroadcast(intent);
            recordArrearSimValidResult(i, i2);
            if (i2 == 1) {
                notifyDfsArrearSimStatus(i);
            }
            localLog("sendValidResult", "broadcast send: " + i2 + ", phoneId:" + i);
        } catch (Throwable th) {
            throw th;
        }
    }

    private void clearValidCount(int i) {
        this.mArrearSims.remove(Integer.valueOf(i));
    }

    private int getValidCount(int i) {
        Integer num = (Integer) this.mArrearSims.get(Integer.valueOf(i));
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    private void updateValidCount(int i, int i2) {
        this.mArrearSims.put(Integer.valueOf(i), Integer.valueOf(i2));
    }

    private State validNetwork(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("invalid phoneid !");
            return State.INVALID_PARAM;
        }
        if (!isNormalCase(i)) {
            log("user busy");
            return State.USER_BUSY;
        }
        if (!isCsAttached(i) || !isPsAttached(i)) {
            int psRegRejectCause = getPsRegRejectCause(i);
            int csRegRejectCause = getCsRegRejectCause(i);
            log("detached rejectPsCause:" + psRegRejectCause + ", rejectCsCause:" + csRegRejectCause + ", denied:" + this.mNwDenied[i]);
            if (isNwDenied(psRegRejectCause) || isNwDenied(csRegRejectCause) || this.mNwDenied[i]) {
                return State.NW_DENIED;
            }
            return State.NW_DETACHED;
        }
        if (!isMobileDataEnabled() || isWifiConnected() || !isActiveDataSlotId(i)) {
            log("mobile data disabled");
            return State.MOBILE_DATA_DISABLED;
        }
        if (!isMobileDataConnected(i)) {
            log("mobile data disconnected");
            return State.MOBILE_DATA_DISCONNECTED;
        }
        if (isDataConnectionStall()) {
            log("mobile data stall");
            return State.DATA_STALL;
        }
        log("mobile data is valid");
        return State.VALID;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordUrlArrearSimNotification(int i) {
        HashMap map = new HashMap();
        map.put("Type", 0);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_arrear_sim", map);
    }

    private void recordSimBillResultNotification(int i, long j, boolean z) {
        HashMap map = new HashMap();
        map.put("Type", 2);
        map.put("Network", getSimOperator(i));
        map.put("Value", Long.valueOf(j));
        map.put("isAutoCorrectionEnable", Boolean.valueOf(z));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_arrear_sim", map);
    }

    private void recordArrearSimValidResult(int i, int i2) {
        HashMap map = new HashMap();
        map.put("Type", 1);
        map.put("Network", getSimOperator(i));
        map.put("Value", Integer.valueOf(i2));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_arrear_sim", map);
    }

    private void recordNetworkDeniedNotification(int i, boolean z, String str) {
        HashMap map = new HashMap();
        map.put("Type", 3);
        map.put("isDds", Boolean.valueOf(z));
        map.put("SimOperator", str);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_arrear_sim", map);
    }

    public void recordApnDetectNotification(int i) {
        HashMap map = new HashMap();
        map.put("Type", 4);
        map.put("Network", getSimOperator(i));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_arrear_sim", map);
    }

    private void recordInvalidSimDetected(int i, int i2) {
        HashMap map = new HashMap();
        boolean zIsDefaultDataSlotId = MiuiPhoneUtils.isDefaultDataSlotId(i);
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(i);
        map.put("Value", Integer.valueOf(i2));
        map.put("isDds", Boolean.valueOf(zIsDefaultDataSlotId));
        map.put("SimOperator", simOperatorNumeric);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_invalid_sim", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScreenStateChanged() {
        if (this.mIsScreenOn) {
            for (Integer num : this.mArrearSims.keySet()) {
                int iIntValue = num.intValue();
                int iIntValue2 = ((Integer) this.mArrearSims.get(num)).intValue();
                log("screen on, count:" + iIntValue2);
                if (iIntValue2 > 0 && iIntValue2 < 3) {
                    retryValidNetwork(iIntValue, 1);
                }
            }
        }
    }

    private String getSimOperator(int i) {
        return MiuiPhoneUtils.isValidatePhoneId(i) ? TelephonyManager.getTelephonyProperty(i, "gsm.sim.operator.numeric", "") : "";
    }

    private boolean isPsAttached(int i) {
        return MiuiPhoneUtils.isDataRegistered(i);
    }

    private boolean isCsAttached(int i) {
        Phone phone;
        ServiceState serviceState;
        return MiuiPhoneUtils.isValidatePhoneId(i) && (phone = PhoneFactory.getPhone(i)) != null && (serviceState = phone.getServiceState()) != null && serviceState.getVoiceRegState() == 0;
    }

    private int getPsRegRejectCause(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return -1;
        }
        return getPsRegRejectCause(phone.getServiceState());
    }

    private int getPsRegRejectCause(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo;
        if (serviceState == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1)) == null) {
            return -1;
        }
        return networkRegistrationInfo.getRejectCause();
    }

    private int getCsRegRejectCause(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return -1;
        }
        return getCsRegRejectCause(phone.getServiceState());
    }

    private int getCsRegRejectCause(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo;
        if (serviceState == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(1, 1)) == null) {
            return -1;
        }
        return networkRegistrationInfo.getRejectCause();
    }

    private boolean isMobileDataEnabled() {
        return MiuiPhoneUtils.isMobileDataEnabled(mContext);
    }

    private boolean isMobileDataConnected(int i) {
        return MiuiPhoneUtils.isMobileDataConnected(i);
    }

    private boolean isWifiConnected() {
        return MiuiPhoneUtils.isWifiConnected();
    }

    private boolean isDataConnectionStall() {
        NetworkSpeedTracker networkSpeedTracker = NetworkSpeedTracker.getInstance();
        if (networkSpeedTracker == null) {
            return false;
        }
        int dataStallCount = networkSpeedTracker.getDataStallCount();
        log("connection count: " + this.mFakeConnectionCount + ", dataStallCount: " + dataStallCount);
        return this.mFakeConnectionCount >= 5 || dataStallCount >= 2;
    }

    private void registerTelephonyInfoCallBack() {
        log("registerTelephonyInfoCallBack");
        if (this.mTelephonyInfoCallBack == null) {
            TelephonyInfoCallBack telephonyInfoCallBack = new TelephonyInfoCallBack();
            this.mTelephonyInfoCallBack = telephonyInfoCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyInfoCallBack);
        }
    }

    class TelephonyInfoCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyInfoCallBack() {
        }

        public void onDataStatisticChanged(TelephonyStatAdapter.DataStatType dataStatType, int i, Map map) {
            SpecialSimManager.this.mHandler.obtainMessage(2, dataStatType.ordinal(), i, map).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            SpecialSimManager.this.mHandler.obtainMessage(7, i, 0, serviceState).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDataStatisticChange(int i, int i2, HashMap map) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i2) || !isActiveDataSlotId(i2)) {
            log("invalid phoneId: " + i2);
            return;
        }
        int i3 = AnonymousClass3.$SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.values()[i].ordinal()];
        if (i3 == 1) {
            this.mFakeConnectionCount++;
        } else {
            if (i3 != 2) {
                return;
            }
            this.mFakeConnectionCount = 0;
        }
    }

    /* JADX INFO: renamed from: com.android.phone.SpecialSimManager$3, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType;

        static {
            int[] iArr = new int[TelephonyStatAdapter.DataStatType.values().length];
            $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType = iArr;
            try {
                iArr[TelephonyStatAdapter.DataStatType.FAKE_CONNECTION_CN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$miui$telephony$TelephonyStatAdapter$DataStatType[TelephonyStatAdapter.DataStatType.DATA_CONNECTION_OK_CN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSimBillResultNotification(Intent intent) {
        long longExtra;
        stopSimBillQueryTimer();
        int intExtra = intent.getIntExtra("slotId", -1);
        if (MiuiPhoneUtils.isValidatePhoneId(intExtra)) {
            try {
                this.mIsCorrectionSuccess[intExtra] = intent.getBooleanExtra("correctionSuccess", false);
                boolean z = true;
                if (this.mIsCorrectionSuccess[intExtra]) {
                    longExtra = intent.getLongExtra("billRemained", Long.MIN_VALUE);
                    if (longExtra < 0 && longExtra != Long.MIN_VALUE) {
                        notifyDfsArrearSimStatus(intExtra);
                    }
                } else {
                    longExtra = intent.getLongExtra("lastEfficientBill", Long.MIN_VALUE);
                    boolean booleanExtra = intent.getBooleanExtra("isAutoCorrectionEnable", false);
                    if (longExtra < 2 && longExtra != Long.MIN_VALUE) {
                        notifyDfsArrearSimStatus(intExtra);
                        this.mIsCorrectionSuccess[intExtra] = true;
                    }
                    z = booleanExtra;
                }
                localLog("sendSimBillQuery", "bill=" + longExtra + ", isAutoCorrectionEnable=" + z);
                recordSimBillResultNotification(intExtra, longExtra, z);
            } catch (Exception e) {
                e.printStackTrace();
                log("processSimBillResultNotification exception");
            }
        }
    }

    private void startNwDeniedTimerById(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && !this.mHandler.hasMessages(10, Integer.valueOf(i))) {
            log("startNwDeniedTimerById");
            SmartSimHandler smartSimHandler = this.mHandler;
            smartSimHandler.sendMessageDelayed(smartSimHandler.obtainMessage(10, Integer.valueOf(i)), 120000L);
        }
    }

    private void stopNwDeniedTimerById(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && this.mHandler.hasMessages(10, Integer.valueOf(i))) {
            log("stopNwDeniedTimerById");
            this.mHandler.removeMessages(10, Integer.valueOf(i));
        }
    }

    private void clearNwDeniedTimeQueue(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || this.mNwDeniedTimeQueue[i].isEmpty()) {
            return;
        }
        log("clearNwDeniedTimeQueue");
        this.mNwDeniedTimeQueue[i].clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getArrearSimNwDeniedCloud() {
        boolean arrearSimNwDeniedModeByCloud = FiveGManagerBase.getArrearSimNwDeniedModeByCloud(mContext);
        log("getArrearSimNwDeniedCloud rst = " + arrearSimNwDeniedModeByCloud);
        return arrearSimNwDeniedModeByCloud;
    }

    private boolean isNormalCase(int i) {
        return !isAirPlaneModeOn() && MiuiPhoneUtils.isSimReady(i) && MiuiPhoneUtils.isLteNrNwMode(i) && isPhoneStateIdle() && !isManualSelection(i);
    }

    private boolean isPhoneStateIdle() {
        return !MiuiPhoneUtils.isDuringCall();
    }

    private boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    private boolean isManualSelection(int i) {
        ServiceState serviceState;
        Phone phone = PhoneFactory.getPhone(i);
        return (phone == null || (serviceState = phone.getServiceState()) == null || !serviceState.getIsManualSelection()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getActiveDataPhoneId() {
        int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
        if (SubscriptionManager.isValidSubscriptionId(activeDataSubscriptionId)) {
            return SubscriptionManager.getPhoneId(activeDataSubscriptionId);
        }
        return -1;
    }

    private boolean isActiveDataSlotId(int i) {
        return i == getActiveDataPhoneId();
    }

    public boolean isInValidSim(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            return this.mIsInValidSim[i];
        }
        return false;
    }

    boolean isNoPhoneBalance(int i) {
        return this.mIsNoBillSim;
    }

    private boolean isPreviousRegLteNr(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mLastRegLteNrTime[i];
        if (j != -1 && jCurrentTimeMillis - j < v.b) {
            return true;
        }
        log("previous not reg lte nr");
        return false;
    }

    private boolean isLingerTime(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        long[] jArr = this.mLastTime;
        long j = jArr[i];
        if (j != -1 && jCurrentTimeMillis < j + PENALTY_TIMER) {
            log("do NOT prompted in the notification bar during lingering time");
            return true;
        }
        jArr[i] = jCurrentTimeMillis;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLingerTimeBillQuery(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        long[] jArr = this.mLastTimeBillQuery;
        long j = jArr[i];
        if (j != -1 && jCurrentTimeMillis < j + PENALTY_TIMER) {
            log("do NOT send sim bill query during lingering time");
            return true;
        }
        jArr[i] = jCurrentTimeMillis;
        return false;
    }

    private static void sendEvent(MiEvent miEvent) {
        MiSight.sendEvent(miEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDfsArrearSimStatus(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            sendEvent(MiSight.constructEvent(915300007, new Object[]{"PhoneId", Integer.valueOf(i)}));
            dfsEventForMtb(915300007);
            log("notifyDfsArrearSimStatus: " + i);
        }
    }

    private void showArrearSimNotification(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService("notification");
            notificationManager.createNotificationChannel(new NotificationChannel("arrears_sim_rminders", mContext.getString(R.string.arrears_sim_v2_notification), 4));
            int i2 = i + 1;
            Notification notificationBuild = new Notification.Builder(mContext, "arrears_sim_rminders").setOngoing(true).setSmallIcon(android.R.drawable.stat_sys_warning).setContentText(String.format(mContext.getString(R.string.arrears_sim_v2_summary), Integer.valueOf(i2), Integer.valueOf(i2))).setContentTitle(String.format(mContext.getString(R.string.arrears_sim_v2_title), Integer.valueOf(i2))).setAutoCancel(true).setChannelId("arrears_sim_rminders").build();
            log(" start notification");
            notificationManager.notify(1, notificationBuild);
        }
    }

    private void localLog(String str, String str2) {
        NetworkMetricsController.localLog("SpecialSimManager", str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("SpecialSimManager", str);
    }
}
