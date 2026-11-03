package com.android.phone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellSignalStrengthNr;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.utils.ModemUtils;
import com.ot.pubsub.util.v;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import miui.os.Build;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class NetworkFastRecovery {
    private static NetworkFastRecovery sInstance;
    private final String[] ALARM_OVER_TIME_NOTIFY_INTENT;
    private AlarmManager mAlarmManager;
    private Context mContext;
    private FastRecovery mFastRecovery;
    private ModemUtils mModemUtils;
    private final BroadcastReceiver mReceiver;
    private Handler mWorkHandler;
    private boolean VDBG = true;
    private boolean mIsFeatureEnabled = true;
    private int[] mCurrentUpdateOosType = {-1, -1};
    private long[] indoorModeOrB1EventIndTime = {0, 0};
    private int[] indoorModeOrB1EventIndCount = {0, 0};
    private PendingIntent[] mAlarmOverTimePendingIntent = new PendingIntent[2];
    private long[] mPhoneEventTime = new long[4];

    private static boolean isSupportPoorNetworkFastRecovery() {
        return true;
    }

    public void notifyModemReset() {
    }

    class FastRecoveryHandler extends Handler {
        FastRecoveryHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            NetworkFastRecovery.this.log("handleMessage msg = " + message.what);
            switch (message.what) {
                case 100:
                    int intExtra = ((Intent) message.obj).getIntExtra("android.telephony.extra.SIM_STATE", 1);
                    if (intExtra == 1 || intExtra == 11) {
                        NetworkFastRecovery.this.mPhoneEventTime[3] = SystemClock.elapsedRealtime();
                    }
                    if (NetworkFastRecovery.this.mFastRecovery != null) {
                        NetworkFastRecovery.this.mFastRecovery.onSimStateChanged(intExtra);
                    }
                    break;
                case 101:
                    int iIntValue = ((Integer) message.obj).intValue();
                    if (NetworkFastRecovery.this.mFastRecovery != null) {
                        NetworkFastRecovery.this.mFastRecovery.recoverOssType(iIntValue);
                    }
                    break;
                case 102:
                case 103:
                    NetworkFastRecovery networkFastRecovery = NetworkFastRecovery.this;
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    FastRecovery unused = networkFastRecovery.mFastRecovery;
                    networkFastRecovery.parseIndoorModeOrB1EventInd(asyncResult, 1);
                    break;
                case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                    NetworkFastRecovery networkFastRecovery2 = NetworkFastRecovery.this;
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    FastRecovery unused2 = networkFastRecovery2.mFastRecovery;
                    networkFastRecovery2.parseIndoorModeOrB1EventInd(asyncResult2, 0);
                    break;
                case 106:
                    NetworkFastRecovery.this.log("HYDRA_READY_IND , regPoorNetworkFastRecovery");
                    ModemUtils modemUtils = NetworkFastRecovery.this.mModemUtils;
                    NetworkFastRecovery networkFastRecovery3 = NetworkFastRecovery.this;
                    if (modemUtils == null) {
                        networkFastRecovery3.log("EVENT_HYDRA_READY_IND failed, mModemUtils is null");
                    } else {
                        networkFastRecovery3.mModemUtils.regPoorNetworkFastRecovery(0, 2, NetworkFastRecovery.this.mWorkHandler, 102, null);
                        NetworkFastRecovery.this.mModemUtils.regPoorNetworkFastRecovery(1, 2, NetworkFastRecovery.this.mWorkHandler, 103, null);
                        NetworkFastRecovery.this.mModemUtils.regPoorNetworkFastRecovery(2, 2, NetworkFastRecovery.this.mWorkHandler, MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, null);
                        NetworkFastRecovery.this.mModemUtils.regPoorNetworkFastRecovery(3, 2, NetworkFastRecovery.this.mWorkHandler, MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, null);
                    }
                    break;
                case 107:
                    int i = message.arg1;
                    int i2 = message.arg2;
                    if (NetworkFastRecovery.this.mFastRecovery != null) {
                        NetworkFastRecovery.this.mFastRecovery.updateOosType(i, i2);
                    }
                    break;
                case 108:
                    boolean z = TelephonyCloudController.CLOUD_NETWORK_FAST_RECOVERY_OPT_DISABLED;
                    boolean z2 = !z;
                    if (NetworkFastRecovery.this.mIsFeatureEnabled && z) {
                        for (int i3 = 0; i3 < NetworkFastRecovery.this.mCurrentUpdateOosType.length; i3++) {
                            if (NetworkFastRecovery.this.mCurrentUpdateOosType[i3] == 6) {
                                NetworkFastRecovery.this.mFastRecovery.recoverOssType(i3);
                            }
                        }
                    }
                    NetworkFastRecovery.this.mIsFeatureEnabled = z2;
                    break;
            }
        }
    }

    /* JADX INFO: renamed from: com.android.phone.NetworkFastRecovery$1, reason: invalid class name */
    class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            NetworkFastRecovery.this.log("receive action = " + action);
            if (action == null) {
                NetworkFastRecovery.this.log("null action");
            }
            boolean zEquals = "android.telephony.action.SIM_CARD_STATE_CHANGED".equals(action);
            NetworkFastRecovery networkFastRecovery = NetworkFastRecovery.this;
            if (zEquals) {
                networkFastRecovery.mWorkHandler.sendMessage(NetworkFastRecovery.this.mWorkHandler.obtainMessage(100, intent));
                return;
            }
            boolean zEquals2 = networkFastRecovery.ALARM_OVER_TIME_NOTIFY_INTENT[0].equals(action);
            NetworkFastRecovery networkFastRecovery2 = NetworkFastRecovery.this;
            if (zEquals2) {
                networkFastRecovery2.mWorkHandler.sendMessage(NetworkFastRecovery.this.mWorkHandler.obtainMessage(101, 0));
                return;
            }
            if (networkFastRecovery2.ALARM_OVER_TIME_NOTIFY_INTENT[1].equals(action)) {
                NetworkFastRecovery.this.mWorkHandler.sendMessage(NetworkFastRecovery.this.mWorkHandler.obtainMessage(101, 1));
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                if (Settings.Global.getInt(NetworkFastRecovery.this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1) {
                    return;
                }
                NetworkFastRecovery.this.mPhoneEventTime[2] = SystemClock.elapsedRealtime();
                NetworkFastRecovery.this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.NetworkFastRecovery$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0();
                    }
                }, 90000L);
                return;
            }
            if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                NetworkFastRecovery.this.mWorkHandler.postDelayed(new Runnable() { // from class: com.android.phone.NetworkFastRecovery$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$1();
                    }
                }, 90000L);
                return;
            }
            if ("PoorNetworkFastRecovery.intent.debugall".equals(action)) {
                int intExtra = intent.getIntExtra("type", 0);
                int intExtra2 = intent.getIntExtra("phoneId", 0);
                if (intExtra == 0) {
                    NetworkFastRecovery.this.mFastRecovery.forceScan(0, intExtra2);
                    return;
                }
                if (intExtra == 1) {
                    NetworkFastRecovery.this.mFastRecovery.updateOosType(6, intExtra2);
                    return;
                } else {
                    if (intExtra == 2) {
                        NetworkFastRecovery.this.mFastRecovery.setRsrpThres(intent.getIntExtra("rsrp", -110));
                        return;
                    }
                    return;
                }
            }
            if ("com.android.phone.intent.action.CLOUD_TELE_FEATURE_INFO_CHANGED".equals(action)) {
                NetworkFastRecovery.this.mWorkHandler.sendMessageDelayed(NetworkFastRecovery.this.mWorkHandler.obtainMessage(108), MirrorDesktopHelper.TIMEOUT_MILLIS);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            NetworkFastRecovery.this.mFastRecovery.checkServiceStateAfterBootOrAirplaneMode();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$1() {
            NetworkFastRecovery.this.mFastRecovery.checkServiceStateAfterBootOrAirplaneMode();
        }
    }

    public static NetworkFastRecovery init() {
        if (!Build.IS_INTERNATIONAL_BUILD && isSupportPoorNetworkFastRecovery() && sInstance == null) {
            sInstance = new NetworkFastRecovery();
        }
        return sInstance;
    }

    public NetworkFastRecovery() {
        String[] strArr = {"alarm_over_time_notify_intent_phone0", "alarm_over_time_notify_intent_phone1"};
        this.ALARM_OVER_TIME_NOTIFY_INTENT = strArr;
        AnonymousClass1 anonymousClass1 = new AnonymousClass1();
        this.mReceiver = anonymousClass1;
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("NetworkFastRecovery", 10);
        handlerThread.start();
        this.mWorkHandler = new FastRecoveryHandler(handlerThread.getLooper());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        intentFilter.addAction(strArr[0]);
        intentFilter.addAction(strArr[1]);
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("PoorNetworkFastRecovery.intent.debugall");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_TELE_FEATURE_INFO_CHANGED");
        this.mContext.registerReceiver(anonymousClass1, intentFilter, 2);
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        this.mFastRecovery = new FastRecovery("FastRecovery", handlerThread.getLooper());
        this.mModemUtils = ModemUtils.init(this.mContext);
        EventHubManager.getInstance().registerListener("telephony", "ril_ready", new FastRecoveryQcrilHookStateCallBack(), this.mWorkHandler);
    }

    class FastRecoveryQcrilHookStateCallBack implements BaseEvent.IEventListener {
        private FastRecoveryQcrilHookStateCallBack() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if ((baseEvent instanceof TelephonyEvent.RilEvent) && ((TelephonyEvent.RilEvent) baseEvent).isRilReady()) {
                NetworkFastRecovery.this.log("qcrilhookReady");
                ModemUtils modemUtils = NetworkFastRecovery.this.mModemUtils;
                NetworkFastRecovery networkFastRecovery = NetworkFastRecovery.this;
                if (modemUtils == null) {
                    networkFastRecovery.log("FastRecoveryQcrilHookStateCallBack failed, mModemUtils is null");
                    return;
                }
                networkFastRecovery.mModemUtils.regPoorNetworkFastRecovery(0, 2, NetworkFastRecovery.this.mWorkHandler, 102, null);
                NetworkFastRecovery.this.mModemUtils.regPoorNetworkFastRecovery(1, 2, NetworkFastRecovery.this.mWorkHandler, 103, null);
                NetworkFastRecovery.this.mModemUtils.regPoorNetworkFastRecovery(2, 2, NetworkFastRecovery.this.mWorkHandler, MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, null);
                NetworkFastRecovery.this.mModemUtils.regPoorNetworkFastRecovery(3, 2, NetworkFastRecovery.this.mWorkHandler, MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, null);
                NetworkFastRecovery.this.mModemUtils.registerOemHookRegistrant(NetworkFastRecovery.this.mWorkHandler, 106, null, "qualcomm.intent.action.ACTION_HYDRA_READY_IND");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseIndoorModeOrB1EventInd(AsyncResult asyncResult, int i) {
        try {
            log("parseIndoorModeOrB1EventInd");
            ByteBuffer byteBufferWrap = ByteBuffer.wrap((byte[]) asyncResult.result);
            byteBufferWrap.order(ByteOrder.nativeOrder());
            byteBufferWrap.get();
            int i2 = byteBufferWrap.getInt();
            if (i2 >= 0 && i2 <= 1) {
                handleIndoorModeOrB1EventInd(i2, i);
                return;
            }
            log("error phone id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleIndoorModeOrB1EventInd(int i, int i2) {
        if (ignoreHandleIndoorMode(i)) {
            log("ignore IndoorModeOrB1EventInd");
            return;
        }
        long[] jArr = this.indoorModeOrB1EventIndTime;
        if (jArr[i] == 0) {
            jArr[i] = SystemClock.elapsedRealtime();
            int[] iArr = this.indoorModeOrB1EventIndCount;
            iArr[i] = iArr[i] + 1;
        } else {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long[] jArr2 = this.indoorModeOrB1EventIndTime;
            if (jElapsedRealtime - jArr2[i] <= v.b) {
                int[] iArr2 = this.indoorModeOrB1EventIndCount;
                int i3 = iArr2[i];
                if (i3 > 5) {
                    log("ignore IndoorModeOrB1EventInd, reported times reached the upper limit int one day");
                    return;
                }
                iArr2[i] = i3 + 1;
            } else {
                jArr2[i] = SystemClock.elapsedRealtime();
                this.indoorModeOrB1EventIndCount[i] = 1;
            }
        }
        this.mFastRecovery.forceScan(i2, i);
    }

    private boolean ignoreHandleIndoorMode(int i) {
        if (MiuiPhoneUtils.is5GSaRegistered(i)) {
            return true;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        int i2 = 0;
        while (true) {
            long[] jArr = this.mPhoneEventTime;
            if (i2 >= jArr.length) {
                return false;
            }
            if (jElapsedRealtime - jArr[i2] < 15000) {
                return true;
            }
            i2++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d("FastRecovery", str);
    }

    class FastRecovery extends StateMachine implements TelephonyStatAdapter.ICallBack {
        private int RSRP_THRES;
        private int T1;
        private int T2;
        private int T3;
        private final State mDefaultState;
        private boolean mHasRegisteredTelephonyCallBack;
        private final State mInvalidForceScanState;
        private final long[] mLast45GTime;
        private int mLastRSCFGSlot;
        private final State mOosState;
        private final int[] mPhoneRegState;
        private String[] mRegOperatorNumeric;

        private FastRecovery(String str, Looper looper) {
            super(str, looper);
            this.T1 = 10;
            this.T2 = 3;
            this.T3 = 180;
            this.RSRP_THRES = -110;
            this.mLastRSCFGSlot = -1;
            this.mPhoneRegState = new int[]{3, 3};
            DefaultState defaultState = new DefaultState();
            this.mDefaultState = defaultState;
            OosState oosState = new OosState();
            this.mOosState = oosState;
            InvalidForceScanState invalidForceScanState = new InvalidForceScanState();
            this.mInvalidForceScanState = invalidForceScanState;
            int i = MiuiPhoneUtils.PHONE_COUNT;
            this.mLast45GTime = new long[i * 2];
            this.mRegOperatorNumeric = new String[i];
            this.mHasRegisteredTelephonyCallBack = false;
            addState(defaultState);
            addState(oosState);
            addState(invalidForceScanState);
            setInitialState(invalidForceScanState);
            start();
        }

        public void setRsrpThres(int i) {
            this.RSRP_THRES = i;
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            log("MQSTele: onServiceStateChanged begin, phoneId = " + i);
            if (serviceState == null || !SubscriptionManager.isValidPhoneId(i)) {
                return;
            }
            if (!TextUtils.isEmpty(serviceState.getOperatorNumeric())) {
                this.mRegOperatorNumeric[i] = serviceState.getOperatorNumeric();
            }
            sendMessage(100, i, 0, serviceState);
            log("MQSTele: onServiceStateChanged end, phoneId = " + i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void forceScan(int i, int i2) {
            if (!NetworkFastRecovery.this.mIsFeatureEnabled || !isUserFiveGSaEnabled(i2) || !hasBeenRegister5gSaInTwoDays(i2) || isInHighSpeedTrainMode()) {
                log("feature disabled by cloud : " + TelephonyCloudController.CLOUD_NETWORK_FAST_RECOVERY_OPT_DISABLED);
            } else {
                if (NetworkFastRecovery.this.mModemUtils == null) {
                    log("forceScan failed, mModemUtils is null");
                    return;
                }
                log("force scan searchType = " + i + " searchSlot = " + i2);
                NetworkFastRecovery.this.mModemUtils.onHookCommonMsg(120, 8, i, i2);
            }
        }

        private boolean isInHighSpeedTrainMode() {
            NetworkMetricsController networkMetricsController = MiuiPhoneUtils.isSpecialNetorkOptimizationSupported() ? NetworkMetricsController.getInstance() : null;
            if (networkMetricsController != null) {
                return networkMetricsController.isHighSpeedTrain();
            }
            return false;
        }

        private boolean hasBeenRegister5gSaInTwoDays(int i) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long j = this.mLast45GTime[(i * 2) + 1];
            return j > 0 && jElapsedRealtime - j < 172800000;
        }

        private boolean isUserFiveGSaEnabled(int i) {
            FiveGManagerBase fiveGManagerBase = FiveGManagerBase.getInstance();
            if (fiveGManagerBase != null) {
                return fiveGManagerBase.isUserFiveGSaEnabled(i);
            }
            return false;
        }

        private void setAlarm(int i, long j) {
            NetworkFastRecovery.this.mAlarmOverTimePendingIntent[i] = PendingIntent.getBroadcast(NetworkFastRecovery.this.mContext, 0, new Intent(NetworkFastRecovery.this.ALARM_OVER_TIME_NOTIFY_INTENT[i]), 201326592);
            NetworkFastRecovery.this.mAlarmManager.setExactAndAllowWhileIdle(0, j, NetworkFastRecovery.this.mAlarmOverTimePendingIntent[i]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onSimStateChanged(int i) {
            int insertedSimCount = MiuiPhoneUtils.getInsertedSimCount();
            log("onSimStateChanged " + i + "simCount " + insertedSimCount);
            if (i == 1 && insertedSimCount == 0) {
                this.mRegOperatorNumeric = new String[]{"", ""};
                Arrays.fill(this.mLast45GTime, -1L);
                log("SIM absent, disable SS monitoring");
                TelephonyStatAdapter.unregisterCallBack(this);
                this.mHasRegisteredTelephonyCallBack = false;
            } else {
                log("enable SS monitoring");
                if (!this.mHasRegisteredTelephonyCallBack) {
                    TelephonyStatAdapter.registerCallBack(this);
                    this.mHasRegisteredTelephonyCallBack = true;
                }
                if (insertedSimCount > 1 && isSameOperatorSimCard()) {
                    transitionTo(this.mDefaultState);
                    return;
                }
            }
            transitionTo(this.mInvalidForceScanState);
        }

        private boolean isSameOperatorSimCard() {
            String simOperatorName = MiuiPhoneUtils.getSimOperatorName(0);
            String simOperatorName2 = MiuiPhoneUtils.getSimOperatorName(1);
            log("operatorNameSim1 = " + simOperatorName + " operatorNameSim2" + simOperatorName2);
            return (simOperatorName == null || simOperatorName2 == null || !simOperatorName.equals(simOperatorName2)) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateServiceStateInfo(int i, ServiceState serviceState) {
            if (!SubscriptionManager.isValidPhoneId(i)) {
                log("MQSTeleError: phoneId is not valid, phoneId =" + i);
                return;
            }
            if (serviceState.getRilDataRadioTechnology() == 14) {
                this.mLast45GTime[i * 2] = SystemClock.elapsedRealtime();
            } else if (serviceState.getRilDataRadioTechnology() == 20) {
                this.mLast45GTime[(i * 2) + 1] = SystemClock.elapsedRealtime();
            }
            int serviceState2 = getServiceState(serviceState);
            int i2 = this.mPhoneRegState[i];
            if ((i2 == 0 || i2 == 3) && serviceState2 == 1) {
                NetworkFastRecovery.this.mPhoneEventTime[i] = SystemClock.elapsedRealtime();
                setAlarm(i, System.currentTimeMillis() + 600000);
                NetworkFastRecovery.this.mWorkHandler.sendMessageDelayed(NetworkFastRecovery.this.mWorkHandler.obtainMessage(107, 6, i), 500L);
            } else if (i2 == 1 && serviceState2 == 0) {
                try {
                    NetworkFastRecovery.this.mAlarmManager.cancel(NetworkFastRecovery.this.mAlarmOverTimePendingIntent[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                recoverOssType(i);
            }
            this.mPhoneRegState[i] = serviceState2;
            log("updateServiceStateInfo:" + this.mPhoneRegState[0] + " " + this.mPhoneRegState[1]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getServiceState(ServiceState serviceState) {
            if (serviceState.getDataRegState() == 1 && serviceState.getVoiceRegState() == 1) {
                return 1;
            }
            if (serviceState.getDataRegState() == 0 || serviceState.getVoiceRegState() == 0) {
                return 0;
            }
            return serviceState.getDataRegState();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkServiceStateAfterBootOrAirplaneMode() {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                if (this.mPhoneRegState[i] == 1 && MiuiPhoneUtils.isIccCardActivated(i)) {
                    setAlarm(i, System.currentTimeMillis() + 600000);
                    updateOosType(6, i);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recoverOssType(int i) {
            updateOosType(getCurrentOosType(i), i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateOosType(int i, int i2) {
            if (!MiuiPhoneUtils.isIccCardActivated(i2) || !NetworkFastRecovery.this.mIsFeatureEnabled || isInHighSpeedTrainMode()) {
                log("feature disabled by cloud or no active sim on phone " + i2);
                return;
            }
            ModemUtils unused = NetworkFastRecovery.this.mModemUtils;
            byte[] bArrOnGetModemMsgBytes = ModemUtils.onGetModemMsgBytes(new int[]{i, i2});
            if ("Asia/Shanghai".equals(ZoneId.systemDefault().getId())) {
                NetworkFastRecovery.this.mCurrentUpdateOosType[i2] = i;
                log("updateOosType:" + i + " on phone:" + i2 + ". [1:day,2:repeatOOS,3:night]");
                if (NetworkFastRecovery.this.mModemUtils != null && NetworkFastRecovery.this.mModemUtils.callQcrilHookSet("onHookModemOosBaseTypeSet", Integer.valueOf(i), Integer.valueOf(i2))) {
                    log("updateOosType success");
                } else if (NetworkFastRecovery.this.mModemUtils == null) {
                    log("updateOosType failed due to mHydra null");
                } else if (!NetworkFastRecovery.this.mModemUtils.callQcrilHookSet("onHookModemOosBaseTypeSet", Integer.valueOf(i), Integer.valueOf(i2))) {
                    log("updateOosType failed due to callQcrilHookSet onHookModemOosBaseTypeSet return false");
                }
                if (NetworkFastRecovery.this.mModemUtils != null && NetworkFastRecovery.this.mModemUtils.callQcrilHookSet("onHookModemPowerAPCommand", Integer.valueOf(ModemUtils.featureOptType.SET_OOS_TYPE.getType()), bArrOnGetModemMsgBytes)) {
                    log("onHookModemPowerAPCommand success");
                } else if (NetworkFastRecovery.this.mModemUtils == null) {
                    log("updateOosType failed due to mHydra null");
                } else {
                    if (NetworkFastRecovery.this.mModemUtils.callQcrilHookSet("onHookModemPowerAPCommand", Integer.valueOf(ModemUtils.featureOptType.SET_OOS_TYPE.getType()), bArrOnGetModemMsgBytes)) {
                        return;
                    }
                    log("updateOosType failed due to callQcrilHookSet onHookModemPowerAPCommand return false");
                }
            }
        }

        private int getCurrentOosType(int i) {
            return ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getCurrentOosType(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getCurrentServiceState(int i) {
            Phone phone;
            if (MiuiPhoneUtils.isIccCardActivated(i) && (phone = MiuiPhoneUtils.getPhone(i)) != null) {
                return getServiceState(phone.getServiceState());
            }
            return 3;
        }

        class InvalidForceScanState extends State {
            InvalidForceScanState() {
            }

            public void enter() {
                FastRecovery.this.mPhoneRegState[0] = FastRecovery.this.getCurrentServiceState(0);
                FastRecovery.this.mPhoneRegState[1] = FastRecovery.this.getCurrentServiceState(1);
                FastRecovery.this.log("enter InvalidForceScanState : " + FastRecovery.this.mPhoneRegState[0] + FastRecovery.this.mPhoneRegState[1]);
            }

            public boolean processMessage(Message message) {
                if (NetworkFastRecovery.this.VDBG) {
                    FastRecovery.this.log("InvalidForceScanState processMessage " + message.what);
                }
                if (message.what != 100) {
                    return true;
                }
                ServiceState serviceState = (ServiceState) message.obj;
                int i = message.arg1;
                FastRecovery.this.log("MQSTele: get phoneId from msg, phoneId =" + i);
                FastRecovery.this.updateServiceStateInfo(i, serviceState);
                return true;
            }
        }

        class DefaultState extends State {
            DefaultState() {
            }

            public void enter() {
                FastRecovery.this.mPhoneRegState[0] = FastRecovery.this.getCurrentServiceState(0);
                FastRecovery.this.mPhoneRegState[1] = FastRecovery.this.getCurrentServiceState(1);
                FastRecovery.this.log("enter DefaultState : " + FastRecovery.this.mPhoneRegState[0] + FastRecovery.this.mPhoneRegState[1]);
            }

            public boolean processMessage(Message message) {
                if (NetworkFastRecovery.this.VDBG) {
                    FastRecovery.this.log("DefaultState processMessage " + message.what);
                }
                int i = message.what;
                if (i == 100) {
                    ServiceState serviceState = (ServiceState) message.obj;
                    int i2 = message.arg1;
                    char c = i2 == 0 ? (char) 1 : (char) 0;
                    if (serviceState.getDataRegState() == 3) {
                        enter();
                        return true;
                    }
                    int serviceState2 = FastRecovery.this.getServiceState(serviceState);
                    if (FastRecovery.this.mPhoneRegState[0] != 0 || FastRecovery.this.mPhoneRegState[1] != 0 || serviceState2 != 1) {
                        if (FastRecovery.this.hasMessages(101)) {
                            if (serviceState2 == 1 && FastRecovery.this.mPhoneRegState[c] == 1) {
                                FastRecovery.this.removeMessages(101);
                                if (MiuiPhoneUtils.getInsertedSimCount() == MiuiPhoneUtils.PHONE_COUNT) {
                                    FastRecovery fastRecovery = FastRecovery.this;
                                    fastRecovery.transitionTo(fastRecovery.mOosState);
                                }
                            }
                            if (serviceState2 == 0 && FastRecovery.this.mPhoneRegState[i2] == 1) {
                                FastRecovery.this.removeMessages(101);
                            }
                        }
                        FastRecovery.this.updateServiceStateInfo(i2, serviceState);
                    } else {
                        FastRecovery.this.removeMessages(101);
                        FastRecovery fastRecovery2 = FastRecovery.this;
                        fastRecovery2.sendMessageDelayed(101, TimeUnit.SECONDS.toMillis(fastRecovery2.T1));
                        FastRecovery.this.updateServiceStateInfo(i2, serviceState);
                        return true;
                    }
                } else if (i == 101) {
                    FastRecovery.this.log("2 subs OOS checking expired");
                    FastRecovery.this.removeMessages(101);
                }
                return true;
            }
        }

        class OosState extends State {
            OosState() {
            }

            /* JADX WARN: Code duplicated, block: B:22:0x0066  */
            private boolean isNrOrLteSignalAbovePoor(int i) {
                List cellSignalStrengths;
                CellSignalStrengthNr cellSignalStrengthNr;
                Phone phone = PhoneFactory.getPhone(i);
                boolean z = false;
                if (phone != null) {
                    ServiceState serviceState = phone.getServiceState();
                    SignalStrength signalStrength = phone.getSignalStrength();
                    if (serviceState != null && signalStrength != null) {
                        if (serviceState.getRilDataRadioTechnology() == 14 || serviceState.getRilDataRadioTechnology() == 19) {
                            FastRecovery.this.log("lte rsrp = " + signalStrength.getLteRsrp());
                            if (signalStrength.getLteRsrp() > FastRecovery.this.RSRP_THRES) {
                                z = true;
                            }
                        } else if (serviceState.getRilDataRadioTechnology() == 20 && (cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class)) != null && cellSignalStrengths.size() > 0 && (cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrengths.get(0)) != null) {
                            FastRecovery.this.log("SA rsrp = " + cellSignalStrengthNr.getSsRsrp());
                            if (cellSignalStrengthNr.getSsRsrp() > FastRecovery.this.RSRP_THRES) {
                                z = true;
                            }
                        }
                    }
                }
                FastRecovery.this.log("isSignalGood:" + z);
                return z;
            }

            public void enter() {
                FastRecovery.this.log("enter OosState");
            }

            public boolean processMessage(Message message) {
                int i;
                if (NetworkFastRecovery.this.VDBG) {
                    FastRecovery.this.log("OosState processMessage " + message.what);
                }
                int i2 = message.what;
                if (i2 == 100) {
                    ServiceState serviceState = (ServiceState) message.obj;
                    int i3 = message.arg1;
                    i = i3 == 0 ? 1 : 0;
                    int dataRegState = serviceState.getDataRegState();
                    FastRecovery fastRecovery = FastRecovery.this;
                    if (dataRegState == 3) {
                        fastRecovery.transitionTo(fastRecovery.mDefaultState);
                        return true;
                    }
                    int serviceState2 = fastRecovery.getServiceState(serviceState);
                    if (FastRecovery.this.mPhoneRegState[i3] == 1 && serviceState2 == 0) {
                        FastRecovery.this.removeMessages(102);
                        FastRecovery fastRecovery2 = FastRecovery.this;
                        long j = fastRecovery2.T2;
                        TimeUnit timeUnit = TimeUnit.SECONDS;
                        fastRecovery2.sendMessageDelayed(102, i, timeUnit.toMillis(j));
                        FastRecovery.this.removeMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
                        FastRecovery fastRecovery3 = FastRecovery.this;
                        fastRecovery3.sendMessageDelayed(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, i, timeUnit.toMillis(fastRecovery3.T3));
                        FastRecovery.this.updateServiceStateInfo(i3, serviceState);
                        return true;
                    }
                    FastRecovery.this.log("ss change:" + FastRecovery.this.mPhoneRegState[i] + serviceState2);
                    if (FastRecovery.this.mPhoneRegState[i] == 0 && serviceState2 == 0) {
                        FastRecovery.this.removeMessages(102);
                        FastRecovery.this.removeMessages(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT);
                        FastRecovery fastRecovery4 = FastRecovery.this;
                        fastRecovery4.transitionTo(fastRecovery4.mDefaultState);
                    }
                    FastRecovery.this.updateServiceStateInfo(i3, serviceState);
                } else if (i2 == 102) {
                    FastRecovery.this.removeMessages(102);
                    int i4 = message.arg1;
                    i = i4 == 0 ? 1 : 0;
                    if (FastRecovery.this.mPhoneRegState[i4] == 1 && isNrOrLteSignalAbovePoor(i) && MiuiPhoneUtils.getInsertedSimCount() > 1) {
                        FastRecovery.this.forceScan(1, i4);
                        FastRecovery fastRecovery5 = FastRecovery.this;
                        fastRecovery5.transitionTo(fastRecovery5.mDefaultState);
                    }
                } else if (i2 == 104) {
                    FastRecovery.this.log("EVENT_EOPS_LTE_RESP");
                } else if (i2 == 105) {
                    FastRecovery fastRecovery6 = FastRecovery.this;
                    fastRecovery6.transitionTo(fastRecovery6.mDefaultState);
                }
                return true;
            }
        }
    }
}
