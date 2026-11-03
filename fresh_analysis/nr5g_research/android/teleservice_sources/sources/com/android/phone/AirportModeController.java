package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SettingsObserver;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.ModemUtils;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import miui.os.Build;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class AirportModeController {
    private static final boolean IS_INTERNATIONAL_BUILD = Build.IS_INTERNATIONAL_BUILD;
    private static AirportModeController sInstance;
    private boolean mAirportModeCloudSwitch;
    public AirportModeStats mAirportModeStats;
    private boolean mAsyncRecoveryFreqFile;
    private Context mContext;
    private final BaseEvent.IEventListener mDisplayListener;
    private boolean mDisplayRegisterState;
    private AirportModeHandler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    private boolean[] mIsPresetFreq;
    private boolean mIsScreenOn;
    private int mLastState;
    private final int[] mOosType;
    private ContentResolver mResolver;
    private SettingsObserver mSettingsObserver;
    private TelephonyCallBack mTelephonyCallBack;
    private HandlerThread mWorkThread;

    enum EfsOperateResponse {
        UNKNOWN,
        SUCCESS,
        COPY_FAILED,
        DELETE_FAILED,
        WRITE_FAILED
    }

    private AirportModeController() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mOosType = new int[i];
        this.mIsPresetFreq = new boolean[i];
        this.mTelephonyCallBack = null;
        this.mAirportModeCloudSwitch = true;
        this.mAsyncRecoveryFreqFile = false;
        this.mIsScreenOn = false;
        this.mDisplayRegisterState = false;
        this.mAirportModeStats = new AirportModeStats();
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.AirportModeController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                AirportModeController.this.log("receive action".concat(action));
                if ("com.android.phone.intent.action.CLOUD_AIRPORT_MODE_SWITCH".equals(action)) {
                    AirportModeController.this.processCloudController();
                }
            }
        };
        this.mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.AirportModeController.2
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                    if (!displayEvent.isDefaultDisplay() || AirportModeController.this.mHandler == null) {
                        return;
                    }
                    AirportModeController.this.mHandler.sendMessage(AirportModeController.this.mHandler.obtainMessage(7, displayEvent));
                }
            }
        };
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("AirportModeController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mResolver = this.mContext.getContentResolver();
        this.mHandler = new AirportModeHandler(this.mWorkThread.getLooper());
        this.mSettingsObserver = new SettingsObserver(this.mContext, this.mHandler);
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    public static synchronized AirportModeController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new AirportModeController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        this.mLastState = Settings.Global.getInt(this.mContext.getContentResolver(), "network_mode_state", 0);
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mOosType[i] = 1;
            this.mIsPresetFreq[i] = false;
        }
        recoveryPresetFreqFromDb();
        recoveryAirportOosFromDb();
        processCloudController();
        registerSettingsCallbacks();
        registerBroadcastReceiver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCloudController() {
        this.mAirportModeCloudSwitch = FiveGManagerBase.getAirportModeSwitchByCloud(this.mContext);
        log("processCloudController switch = " + this.mAirportModeCloudSwitch);
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_AIRPORT_MODE_SWITCH");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    class AirportModeHandler extends Handler {
        AirportModeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AirportModeController.this.log("msg.what = " + message.what);
            switch (message.what) {
                case 1:
                    AirportModeController.this.init();
                    break;
                case 2:
                    if (AirportModeController.this.mAirportModeCloudSwitch) {
                        AirportModeController.this.processAirplaneStateChanged();
                    }
                    break;
                case 3:
                    AirportModeController.this.updateOosType(message.arg1, message.arg2);
                    break;
                case 4:
                    AirportModeController.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                    break;
                case 5:
                    AirportModeController.this.log("10min time out, ready detect service state");
                    if (((int) SystemClock.elapsedRealtime()) - message.arg1 < 1800000) {
                        AirportModeController.this.detectServiceState();
                    }
                    break;
                case 6:
                    if (AirportModeController.this.mAsyncRecoveryFreqFile && !MiuiPhoneUtils.isDuringCall()) {
                        AirportModeController.this.exitPresetFreq();
                        AirportModeController.this.shouldUnregisterTelephonyCallBack();
                        AirportModeController.this.calcPrefFreqStateToDb();
                        break;
                    }
                    break;
                case 7:
                    boolean zIsScreenOn = ((DeviceStateEvent.DisplayEvent) message.obj).isScreenOn();
                    AirportModeController.this.log("onDisplayChanged isScreenOn=" + zIsScreenOn + ",mIsScreenOn=" + AirportModeController.this.mIsScreenOn);
                    if (zIsScreenOn != AirportModeController.this.mIsScreenOn) {
                        AirportModeController.this.mIsScreenOn = zIsScreenOn;
                        if (!AirportModeController.this.mIsScreenOn) {
                            AirportModeController.this.mHandler.obtainMessage(6).sendToTarget();
                        }
                        break;
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        if (serviceState == null || !MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("parameter exception, ServiceState is null or phoneId is not Validate, phoneId = " + i);
            return;
        }
        int voiceNetworkType = serviceState.getVoiceNetworkType();
        log("service state change, dataRat = " + voiceNetworkType);
        if (Utils.isLte(voiceNetworkType) || Utils.isNr(voiceNetworkType)) {
            this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(this.mContext);
            boolean zIsDuringCall = MiuiPhoneUtils.isDuringCall();
            if (!this.mIsScreenOn && !zIsDuringCall) {
                exitPresetFreq();
                calcPrefFreqStateToDb();
            }
            if (this.mIsScreenOn || recoveryFailed() || zIsDuringCall) {
                this.mAsyncRecoveryFreqFile = true;
                if (!this.mDisplayRegisterState) {
                    EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
                    this.mDisplayRegisterState = true;
                }
            }
            exitPowerOptimization();
            shouldUnregisterTelephonyCallBack();
            calcAirportOosStateToDb();
        }
    }

    private boolean recoveryFailed() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (this.mIsPresetFreq[i]) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAirplaneStateChanged() {
        int airplaneState = getAirplaneState();
        if (airplaneState == 1) {
            startMonitorTimerForServiceState();
            recordAirportModeStats("airplane_take_off");
        } else {
            if (airplaneState != 2) {
                return;
            }
            exitPowerOptimization();
            shouldUnregisterTelephonyCallBack();
            recordAirportModeStats("airplane_landing");
        }
    }

    private void startMonitorTimerForServiceState() {
        log("startMonitorTimerForServiceState");
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        Message message = new Message();
        message.what = 5;
        message.arg1 = (int) jElapsedRealtime;
        this.mHandler.sendMessageDelayed(message, 600000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void detectServiceState() {
        log("detectServiceState");
        if (isAirplaneModeOn() && !IS_INTERNATIONAL_BUILD) {
            enterPresetFreq();
            calcPrefFreqStateToDb();
            registerTelephonyCallBack();
        } else if (shouldEnterPowerOptimization()) {
            enterPowerOptimization();
            registerTelephonyCallBack();
        }
    }

    private boolean shouldEnterPowerOptimization() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            ServiceState serviceState = getServiceState(i);
            if (serviceState == null) {
                return false;
            }
            if (serviceState.getState() != 1) {
                log("does not meet power optimization requirements, phoneId " + i);
                return false;
            }
        }
        return true;
    }

    private int getAirplaneState() {
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "network_mode_state", 0);
        int i2 = this.mLastState;
        int i3 = ((i2 & 16) == 0 && (i & 16) == 16) ? 1 : -1;
        if ((i2 & 32) == 0 && (i & 32) == 32) {
            i3 = 2;
        }
        this.mLastState = i;
        return i3;
    }

    private synchronized void enterPresetFreq() {
        byte[] prefFreq = getPrefFreq();
        if (prefFreq == null) {
            log("pref freq is null");
            return;
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (!this.mIsPresetFreq[i]) {
                log("ready enter preset frequency phoneId = " + i);
                EfsOperateResponse efsOperateResponseWritePrefFreqFile = writePrefFreqFile(i, prefFreq);
                this.mIsPresetFreq[i] = efsOperateResponseWritePrefFreqFile == EfsOperateResponse.SUCCESS;
                if (this.mAirportModeStats != null) {
                    recordAirportModeStats(efsOperateResponseWritePrefFreqFile);
                }
                log("enter preset frequency result = " + this.mIsPresetFreq[i] + " phoneId = " + i);
            }
        }
    }

    private EfsOperateResponse writePrefFreqFile(int i, byte[] bArr) {
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit == null || !MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("write pref_freq file fail, parameter exception, " + modemUtilsInit + "phoneId=" + i);
            return EfsOperateResponse.UNKNOWN;
        }
        if (!modemUtilsInit.copyEfs(i, "/nv/item_files/modem/nr5g/RRC/pref_freq_list", i, "/nv/item_files/modem/nr5g/RRC/pref_freq_list_copy")) {
            log("backup error, failed to write pref_freq file fail");
            return EfsOperateResponse.COPY_FAILED;
        }
        if (!modemUtilsInit.deleteEfs(i, "/nv/item_files/modem/nr5g/RRC/pref_freq_list")) {
            modemUtilsInit.copyEfs(i, "/nv/item_files/modem/nr5g/RRC/pref_freq_list_copy", i, "/nv/item_files/modem/nr5g/RRC/pref_freq_list");
            log("delete error, failed to write pref_freq file fail");
            return EfsOperateResponse.DELETE_FAILED;
        }
        if (modemUtilsInit.writeEfs(i, "/nv/item_files/modem/nr5g/RRC/pref_freq_list", bArr)) {
            modemMbnRefresh();
            return EfsOperateResponse.SUCCESS;
        }
        modemUtilsInit.copyEfs(i, "/nv/item_files/modem/nr5g/RRC/pref_freq_list_copy", i, "/nv/item_files/modem/nr5g/RRC/pref_freq_list");
        return EfsOperateResponse.WRITE_FAILED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void exitPresetFreq() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (this.mIsPresetFreq[i]) {
                log("ready exit preset frequency phonedId = " + i);
                if (recoveryFreqFile(i)) {
                    this.mIsPresetFreq[i] = false;
                    recordAirportModeStats("exit_airport_mode");
                    log("exit preset frequency success phoneId = " + i);
                } else {
                    continue;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shouldUnregisterTelephonyCallBack() {
        boolean z;
        int[] iArr = this.mOosType;
        int length = iArr.length;
        boolean z2 = false;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = true;
                break;
            } else {
                if (iArr[i] == 5) {
                    log("shouldUnregisterTelephonyCallBack : oos opt scene skip");
                    z = false;
                    break;
                }
                i++;
            }
        }
        int i2 = 0;
        while (true) {
            if (i2 >= MiuiPhoneUtils.PHONE_COUNT) {
                z2 = z;
                break;
            } else {
                if (this.mIsPresetFreq[i2]) {
                    log("shouldUnregisterTelephonyCallBack : freq opt scene skip");
                    break;
                }
                i2++;
            }
        }
        if (z2) {
            unregisterTelephonyCallBack();
        }
    }

    private boolean recoveryFreqFile(int i) {
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit == null || !MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("recovery frequency file fail fail, parameter exception, modemUtils = " + modemUtilsInit + " phoneId = " + i);
            return false;
        }
        if (!modemUtilsInit.copyEfs(i, "/nv/item_files/modem/nr5g/RRC/pref_freq_list_copy", i, "/nv/item_files/modem/nr5g/RRC/pref_freq_list")) {
            return false;
        }
        modemMbnRefresh();
        return true;
    }

    private void modemMbnRefresh() {
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit != null) {
            modemUtilsInit.syncEfs();
            modemUtilsInit.onHookMbnRefresh(3);
        }
    }

    /* JADX WARN: Code duplicated, block: B:32:0x003e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private byte[] getPrefFreq() throws Throwable {
        InputStream inputStreamOpen;
        Throwable th;
        try {
            inputStreamOpen = this.mContext.getResources().getAssets().open("pref_freq_list");
            try {
                try {
                    byte[] bArr = new byte[253];
                    inputStreamOpen.read(bArr, 0, 253);
                    try {
                        inputStreamOpen.close();
                        return bArr;
                    } catch (IOException unused) {
                        log("getPrefFreq not found");
                        return bArr;
                    }
                } catch (Exception unused2) {
                    log("pref_freq_list not found");
                    if (inputStreamOpen != null) {
                        try {
                            inputStreamOpen.close();
                        } catch (IOException unused3) {
                            log("getPrefFreq not found");
                        }
                    }
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (IOException unused4) {
                        log("getPrefFreq not found");
                    }
                }
                throw th;
            }
        } catch (Exception unused5) {
            inputStreamOpen = null;
        } catch (Throwable th3) {
            inputStreamOpen = null;
            th = th3;
            if (inputStreamOpen != null) {
                inputStreamOpen.close();
            }
            throw th;
        }
    }

    private void enterPowerOptimization() {
        updateOosTypeIfNeed(5);
    }

    private void exitPowerOptimization() {
        updateOosTypeIfNeed(1);
    }

    private void updateOosTypeIfNeed(int i) {
        int i2 = 0;
        while (true) {
            int[] iArr = this.mOosType;
            if (i2 >= iArr.length) {
                return;
            }
            if (iArr[i2] != i) {
                log("ready update oosType = " + i + ", phoneId = " + i2);
                this.mHandler.obtainMessage(3, i, i2).sendToTarget();
            }
            i2++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOosType(int i, int i2) {
        if ((i != 1 && i != 5) || !MiuiPhoneUtils.isValidatePhoneId(i2)) {
            log("updateOosType parameter exception, return; oosType = " + i + ", phoneId = " + i2);
            return;
        }
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit == null) {
            log("updateOosType failed due to modemUtils null");
            return;
        }
        if (this.mOosType[i2] == i) {
            return;
        }
        boolean zCallQcrilHookSet = modemUtilsInit.callQcrilHookSet("onHookModemOosBaseTypeSet", Integer.valueOf(i), Integer.valueOf(i2));
        if (zCallQcrilHookSet) {
            this.mOosType[i2] = i;
        }
        log("updateOosType result = " + zCallQcrilHookSet + " new oosType = " + i + " phoneId = " + i2);
        if (this.mAirportModeStats != null && i == 5) {
            recordAirportModeStats("enter_oos_night_total");
            if (!zCallQcrilHookSet) {
                recordAirportModeStats("enter_oos_night_failed");
            }
        }
        calcAirportOosStateToDb();
    }

    private boolean isAirplaneModeOn() {
        return Settings.Global.getInt(this.mResolver, "airplane_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calcPrefFreqStateToDb() {
        int i = 0;
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            if (this.mIsPresetFreq[i2]) {
                if (i2 == 0) {
                    i++;
                } else if (i2 == 1) {
                    i += 2;
                }
            }
        }
        log("pref freq state = " + i);
        setPrefFreqState(i);
        if (this.mAsyncRecoveryFreqFile && i == 0) {
            this.mAsyncRecoveryFreqFile = false;
            if (this.mDisplayRegisterState) {
                EventHubManager.getInstance().unregisterListener(this.mDisplayListener);
                this.mDisplayRegisterState = false;
            }
        }
    }

    private void calcAirportOosStateToDb() {
        int i = 0;
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            if (this.mOosType[i2] == 5) {
                if (i2 == 0) {
                    i++;
                } else if (i2 == 1) {
                    i += 2;
                }
            }
        }
        log("airport oos state = " + i);
        setAirportOosState(i);
    }

    private void recoveryPresetFreqFromDb() {
        int prefFreqState = getPrefFreqState();
        log("recoveryPresetFreqFromDb prefFreqState = " + prefFreqState);
        if (prefFreqState <= 0 || prefFreqState > 3) {
            return;
        }
        if (prefFreqState == 1) {
            this.mIsPresetFreq[0] = true;
        } else {
            boolean[] zArr = this.mIsPresetFreq;
            if (prefFreqState == 2) {
                zArr[1] = true;
            } else {
                zArr[0] = true;
                zArr[1] = true;
            }
        }
        registerTelephonyCallBack();
    }

    private void recoveryAirportOosFromDb() {
        int airportOosState = getAirportOosState();
        log("recoveryAirportOosFromDb airportOosState = " + airportOosState);
        if (airportOosState <= 0 || airportOosState > 3) {
            return;
        }
        if (airportOosState == 1) {
            this.mOosType[0] = 5;
        } else {
            int[] iArr = this.mOosType;
            if (airportOosState == 2) {
                if (iArr.length >= 2) {
                    iArr[1] = 5;
                }
            } else {
                iArr[0] = 5;
                if (iArr.length >= 2) {
                    iArr[1] = 5;
                }
            }
        }
        registerTelephonyCallBack();
    }

    private void setPrefFreqState(int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "pref_freq_mode", i);
    }

    private int getPrefFreqState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "pref_freq_mode", 0);
    }

    private void setAirportOosState(int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "pref_airport_oos_mode", i);
    }

    private int getAirportOosState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "pref_airport_oos_mode", 0);
    }

    private ServiceState getServiceState(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone != null) {
            return phone.getServiceState();
        }
        return null;
    }

    private void registerSettingsCallbacks() {
        this.mSettingsObserver.observe(Settings.Global.getUriFor("network_mode_state"), 2);
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            log("enter airport mode");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private void unregisterTelephonyCallBack() {
        if (this.mTelephonyCallBack != null) {
            log("exit airport mode");
            TelephonyStatAdapter.unregisterCallBack(this.mTelephonyCallBack);
            this.mTelephonyCallBack = null;
        }
    }

    private void recordAirportModeStats(String str) {
        AirportModeStats airportModeStats = this.mAirportModeStats;
        if (airportModeStats == null) {
            return;
        }
        airportModeStats.record(str);
    }

    private void recordAirportModeStats(EfsOperateResponse efsOperateResponse) {
        int iOrdinal = efsOperateResponse.ordinal();
        if (iOrdinal == 1) {
            recordAirportModeStats("enter_airport_mode");
            return;
        }
        if (iOrdinal == 2) {
            recordAirportModeStats("copy_efs_failed");
        } else if (iOrdinal == 3) {
            recordAirportModeStats("delete_efs_failed");
        } else {
            if (iOrdinal != 4) {
                return;
            }
            recordAirportModeStats("write_efs_failed");
        }
    }

    public void reportStatistics() {
        AirportModeStats airportModeStats = this.mAirportModeStats;
        if (airportModeStats == null) {
            return;
        }
        airportModeStats.reportStatistics();
        this.mAirportModeStats.clear();
    }

    public void reloadAirportModeStatsFromSP() {
        AirportModeStats airportModeStats = this.mAirportModeStats;
        if (airportModeStats == null) {
            return;
        }
        airportModeStats.reloadStatsFromSP();
    }

    class AirportModeStats {
        private ConcurrentHashMap mStats;

        private AirportModeStats() {
            this.mStats = new ConcurrentHashMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void record(String str) {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null) {
                return;
            }
            concurrentHashMap.put(str, Integer.valueOf(((Integer) concurrentHashMap.getOrDefault(str, 0)).intValue() + 1));
            try {
                DefaultPreferenceHelper.getInstance().setAirportModeStats(ObjectSerializer.serialize(this.mStats));
            } catch (Exception e) {
                AirportModeController.this.log("record exception:" + e);
            }
        }

        public void reportStatistics() {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
                return;
            }
            HashMap map = new HashMap(this.mStats);
            AirportModeController.this.log("reportStatistics mStats=" + this.mStats);
            MiStatInterfaceUtil.recordObjectEvent("airport", "event_airport_mode_statistic", map);
        }

        public void reloadStatsFromSP() {
            try {
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) ObjectSerializer.deserialize(DefaultPreferenceHelper.getInstance().getAirportModeStats());
                if (concurrentHashMap != null) {
                    this.mStats = concurrentHashMap;
                }
            } catch (Exception e) {
                AirportModeController.this.log("reloadStatsFromSP exception:" + e);
            }
        }

        public void clear() {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null) {
                return;
            }
            concurrentHashMap.clear();
            try {
                DefaultPreferenceHelper.getInstance().setAirportModeStats(ObjectSerializer.serialize(this.mStats));
            } catch (Exception e) {
                AirportModeController.this.log("clear exception:" + e);
            }
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            AirportModeController.this.mHandler.obtainMessage(4, i, 0, serviceState).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("AirportModeController", str);
    }
}
