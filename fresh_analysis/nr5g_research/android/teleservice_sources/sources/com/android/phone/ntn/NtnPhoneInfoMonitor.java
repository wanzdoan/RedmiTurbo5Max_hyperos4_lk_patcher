package com.android.phone.ntn;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.phone.BatteryStateManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.utils.FoldStateMonitor;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class NtnPhoneInfoMonitor {
    private static String LOG_TAG = "NtnPhoneInfoMonitor";
    private static NtnPhoneInfoMonitor sInstance;
    private ArrayList mCallProcessInformationList;
    private int mCallProcessRecordCount;
    private Context mContext;
    private long[] mEndCallTime;
    private int[] mForegroundCallState;
    private boolean[] mIsCallActive;
    private boolean[] mIsInCall;
    private Boolean[] mIsInService;
    private boolean mIsRecording;
    private long mLastTime;
    private NtnPhoneInfoHandler mNtnPhoneInfoHandler;
    private float[] mPercent;
    private int mPhoneId;
    private Map mSatelliteCallPowerMap;
    private Map mSatellitePowerMap;
    private ServiceState[] mServiceState;
    private SignalStrength[] mSignalStrength;
    private long[] mSignalStrengthLevelTime;
    private long[] mStartCallTime;
    private long mStartTime;
    private TelephonyManager mTelephonyMgr;
    public static final String[] ARRAY_KEY_SIGNAL_STRENGTH_TIME_LIST = {"level0", "level1", "level2", "level3", "level4", "levlel5"};
    public static final String[] ARRAY_KEY_CALL_PROCESS_INFORMATION_LIST = {"realTxPwr", "tarTxPwr", "rssi", "snr"};
    public static final String[] ARRAY_KEY_REGISTER_INFORMATION_LIST = {"registerusedtime", a.L, "calibrated", "realAzimuth", "realElevation", "desiredAzimuth", "desiredElevation", "altitude", "realTxPwr", "tarTxPwr", "rssi", "snr", "channelNumber", "beamId", "city", "speed", "ntn_vendor_info", "foldState", "rejectCause", "cityInfoVersionNumber", "saveBeamId", "cityInfoIndex", "locationType", "ntnPosInfo", "error_calss", "error_cause"};
    private static TelephonyCallBack mTelephonyCallBack = null;

    private boolean isCallActive(int i) {
        return (i == 0 || i == 7 || i == -1) ? false : true;
    }

    public static synchronized NtnPhoneInfoMonitor getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new NtnPhoneInfoMonitor();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private NtnPhoneInfoMonitor() {
        String[] strArr = ARRAY_KEY_SIGNAL_STRENGTH_TIME_LIST;
        this.mSignalStrengthLevelTime = new long[strArr.length];
        this.mCallProcessInformationList = new ArrayList();
        this.mPercent = new float[strArr.length];
        this.mTelephonyMgr = null;
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mServiceState = new ServiceState[i];
        this.mIsCallActive = new boolean[i];
        this.mIsInCall = new boolean[i];
        this.mIsInService = new Boolean[i];
        this.mIsRecording = false;
        this.mForegroundCallState = new int[i];
        this.mSignalStrength = new SignalStrength[i];
        this.mStartCallTime = new long[i];
        this.mEndCallTime = new long[i];
        this.mSatellitePowerMap = new HashMap();
        this.mSatelliteCallPowerMap = new HashMap();
        this.mPhoneId = 0;
        this.mCallProcessRecordCount = 60;
        this.mStartTime = 0L;
        this.mLastTime = 0L;
        this.mStartTime = System.currentTimeMillis();
        this.mNtnPhoneInfoHandler = new NtnPhoneInfoHandler(BackgroundThread.get().getLooper());
    }

    public void init() {
        log("NtnPhoneInfoMonitor init");
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        TelephonyManager telephonyManager = (TelephonyManager) phoneGlobals.getSystemService("phone");
        this.mTelephonyMgr = telephonyManager;
        if (telephonyManager == null) {
            log("mTelephonyMgr is null!");
            return;
        }
        FoldStateMonitor.getInstance();
        int i = 0;
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            this.mServiceState[i2] = new ServiceState();
            this.mIsCallActive[i2] = false;
            this.mForegroundCallState[i2] = 0;
            this.mIsInService[i2] = null;
            this.mSignalStrength[i2] = new SignalStrength();
        }
        while (true) {
            long[] jArr = this.mSignalStrengthLevelTime;
            if (i < jArr.length) {
                jArr[i] = 0;
                this.mPercent[i] = 0.0f;
                i++;
            } else {
                registerTelephonyCallBack();
                initPowerInfo(this.mSatellitePowerMap);
                log("NtnPhoneInfoMonitor init done");
                return;
            }
        }
    }

    class NtnPhoneInfoHandler extends Handler {
        NtnPhoneInfoHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            NtnPhoneInfoMonitor.log("handleMessage=" + message.what);
            int i = message.what;
            if (i == 1) {
                NtnPhoneInfoMonitor.this.processPreciseCallStateEvent(message.arg1, message.arg2);
                return;
            }
            if (i == 2) {
                NtnPhoneInfoMonitor.this.processSignalStrengthEvent(message.arg1, (SignalStrength) message.obj);
                return;
            }
            if (i == 3) {
                NtnPhoneInfoMonitor.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                return;
            }
            if (i == 4) {
                NtnPhoneInfoMonitor.this.reportCallPowerState();
                return;
            }
            if (i != 10) {
                if (i != 11) {
                    return;
                }
                NtnPhoneInfoMonitor.this.reportRegisterInformation(message.arg1, message.arg2);
                if (message.arg1 == 1) {
                    NtnPhoneInfoMonitor.this.dispose();
                    return;
                }
                return;
            }
            NtnPhoneInfoMonitor.this.addCallProcessInformation();
            if (NtnPhoneInfoMonitor.this.mCallProcessRecordCount > 0) {
                NtnPhoneInfoMonitor.this.mCallProcessRecordCount--;
                NtnPhoneInfoMonitor.this.mNtnPhoneInfoHandler.sendMessageDelayed(NtnPhoneInfoMonitor.this.mNtnPhoneInfoHandler.obtainMessage(10), MirrorDesktopHelper.TIMEOUT_MILLIS);
            }
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            NtnPhoneInfoMonitor.this.mNtnPhoneInfoHandler.obtainMessage(2, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            NtnPhoneInfoMonitor.this.mNtnPhoneInfoHandler.obtainMessage(3, i, 0, serviceState).sendToTarget();
            if (serviceState.getState() == 0) {
                TelephonyCommonUtils.querySpeechBitrate();
            }
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            NtnPhoneInfoMonitor.log("onPreciseCallStateChanged : callState = " + String.valueOf(i2));
            NtnPhoneInfoMonitor.this.mNtnPhoneInfoHandler.obtainMessage(1, i, i2).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        try {
            int rilVoiceRadioTechnology = serviceState.getRilVoiceRadioTechnology();
            log("serviceStateChangeEvent phoneId =" + i + ", serviceState = " + serviceState.getState() + ", rilVoiceRadioTechnology = " + rilVoiceRadioTechnology);
            if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                boolean z = serviceState.getState() == 0;
                if (rilVoiceRadioTechnology == 101) {
                    this.mPhoneId = i;
                }
                Boolean bool = this.mIsInService[i];
                if (bool != null && this.mStartTime != 0 && rilVoiceRadioTechnology == 101 && !bool.booleanValue() && z) {
                    this.mLastTime = System.currentTimeMillis();
                    this.mIsRecording = true;
                    log("start record signal strength");
                    this.mNtnPhoneInfoHandler.sendMessageDelayed(this.mNtnPhoneInfoHandler.obtainMessage(11, 0, serviceState.getChannelNumber()), 1000L);
                } else if (this.mIsRecording) {
                    Boolean bool2 = this.mIsInService[i];
                    if (bool2 != null && !bool2.booleanValue() && z) {
                        log("register service, restart record signal strength");
                        this.mLastTime = System.currentTimeMillis();
                    } else {
                        Boolean bool3 = this.mIsInService[i];
                        if (bool3 != null && bool3.booleanValue() && !z && this.mLastTime != 0) {
                            log("lost service, pause record signal strength");
                            int i2 = this.mPhoneId;
                            addSignalStrengthTime(i2, this.mSignalStrength[i2]);
                            this.mLastTime = 0L;
                        }
                    }
                }
                this.mServiceState[i] = serviceState;
                this.mIsInService[i] = Boolean.valueOf(z);
            } else {
                log("processServiceStateChangeEvent invalid params");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processPreciseCallStateEvent(int i, int i2) {
        try {
            if (MiuiPhoneUtils.isValidatePhoneId(i) && i2 != this.mForegroundCallState[i]) {
                boolean zIsCallActive = isCallActive(i2);
                this.mForegroundCallState[i] = i2;
                if (this.mIsCallActive[i] != zIsCallActive) {
                    NtnPhoneInfoHandler ntnPhoneInfoHandler = this.mNtnPhoneInfoHandler;
                    if (zIsCallActive) {
                        ntnPhoneInfoHandler.obtainMessage(10).sendToTarget();
                    } else {
                        ntnPhoneInfoHandler.removeMessages(10);
                        reportCallProcessInformation();
                    }
                    StringBuilder sb = new StringBuilder("callStateChanged, last = ");
                    sb.append(this.mIsCallActive);
                    sb.append(", now = ");
                    sb.append(zIsCallActive);
                    sb.append(zIsCallActive ? " start" : " stop");
                    sb.append(" record");
                    log(sb.toString());
                    this.mIsCallActive[i] = zIsCallActive;
                }
                if (i2 == 1 && !this.mIsInCall[i]) {
                    initPowerInfo(this.mSatelliteCallPowerMap);
                    this.mIsInCall[i] = true;
                    this.mStartCallTime[i] = System.currentTimeMillis();
                    log("init mSatelliteCallPowerMap");
                } else if (i2 == 7 || i2 == 0) {
                    if (this.mSatelliteCallPowerMap.size() > 0 && this.mIsInCall[i]) {
                        log("finsh mSatelliteCallPowerMap");
                        this.mEndCallTime[i] = System.currentTimeMillis();
                        finishCallInfo(i, this.mSatelliteCallPowerMap);
                        if (!this.mSatelliteCallPowerMap.containsKey("reason")) {
                            setHangUpReason("0");
                        }
                        Message messageObtainMessage = this.mNtnPhoneInfoHandler.obtainMessage(4);
                        this.mNtnPhoneInfoHandler.removeMessages(4);
                        this.mNtnPhoneInfoHandler.sendMessageDelayed(messageObtainMessage, 500L);
                    }
                    this.mIsInCall[i] = false;
                    this.mStartCallTime[i] = 0;
                    this.mEndCallTime[i] = 0;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addCallProcessInformation() {
        ArrayList arrayList = new ArrayList(ARRAY_KEY_CALL_PROCESS_INFORMATION_LIST.length);
        arrayList.add(String.valueOf(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getRealTxPwr()));
        arrayList.add(String.valueOf(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getTarTxPwr()));
        arrayList.add(String.valueOf(getCurrentDbm()));
        arrayList.add(String.valueOf(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getRetSnr()));
        this.mCallProcessInformationList.add(arrayList);
    }

    private int getCurrentDbm() {
        SignalStrength signalStrength = this.mSignalStrength[this.mPhoneId];
        if (signalStrength != null) {
            return signalStrength.getDbm();
        }
        return -140;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSignalStrengthEvent(int i, SignalStrength signalStrength) {
        try {
            if (MiuiPhoneUtils.isValidatePhoneId(i) && signalStrength != null) {
                if (this.mIsRecording && this.mLastTime != 0) {
                    addSignalStrengthTime(i, signalStrength);
                }
                this.mSignalStrength[i] = new SignalStrength(signalStrength);
            } else {
                loge("processSignalStrengthEvent-invalid phone id!");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void addSignalStrengthTime(int i, SignalStrength signalStrength) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || signalStrength == null) {
            loge("addSignalStrengthTime-invalid phone id!");
            return;
        }
        int miuiLevel = this.mSignalStrength[i].getMiuiLevel();
        if (miuiLevel == signalStrength.getMiuiLevel() && this.mIsRecording) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        int i2 = 0;
        while (true) {
            long[] jArr = this.mSignalStrengthLevelTime;
            if (i2 >= jArr.length) {
                return;
            }
            if (miuiLevel == i2) {
                jArr[i2] = jArr[i2] + (jCurrentTimeMillis - this.mLastTime);
                this.mLastTime = jCurrentTimeMillis;
                return;
            }
            i2++;
        }
    }

    public void reportSatellitePower() {
        finishPowerInfo(this.mSatellitePowerMap);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ntnphone_power", new HashMap(this.mSatellitePowerMap));
        log("report mSatellitePowerMap = " + mapToString(this.mSatellitePowerMap));
        this.mSatellitePowerMap.clear();
    }

    public void reportCallPowerState() {
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ntnphone_call_power", new HashMap(this.mSatelliteCallPowerMap));
        log("report mSatelliteCallPowerMap = " + mapToString(this.mSatelliteCallPowerMap));
        this.mSatelliteCallPowerMap.clear();
    }

    public void reportSignalStrengthTime() {
        if (!this.mIsRecording) {
            return;
        }
        int i = 0;
        this.mIsRecording = false;
        int i2 = this.mPhoneId;
        addSignalStrengthTime(i2, this.mSignalStrength[i2]);
        log("stop record signal strength");
        int i3 = 0;
        long j = 0;
        while (true) {
            long[] jArr = this.mSignalStrengthLevelTime;
            if (i3 >= jArr.length) {
                break;
            }
            j += jArr[i3];
            String str = "miuileve[" + i3 + "] =" + this.mSignalStrengthLevelTime[i3];
            if (i3 == this.mSignalStrengthLevelTime.length - 1) {
                str = str + "totalTime = " + j;
            }
            log(str);
            i3++;
        }
        int i4 = 0;
        while (true) {
            long[] jArr2 = this.mSignalStrengthLevelTime;
            if (i4 >= jArr2.length) {
                break;
            }
            long j2 = jArr2[i4];
            if (j2 != 0) {
                this.mPercent[i4] = (j2 * 100) / j;
            }
            i4++;
        }
        HashMap map = new HashMap();
        for (int i5 = 0; i5 < this.mSignalStrengthLevelTime.length; i5++) {
            map.put(ARRAY_KEY_SIGNAL_STRENGTH_TIME_LIST[i5], String.valueOf(this.mPercent[i5]));
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ntnphone_signal_strength_level_percent", map);
        while (true) {
            long[] jArr3 = this.mSignalStrengthLevelTime;
            if (i >= jArr3.length) {
                return;
            }
            jArr3[i] = 0;
            this.mPercent[i] = 0.0f;
            i++;
        }
    }

    public void reportRegisterInformation(int i, int i2) {
        ServiceState serviceState;
        NetworkRegistrationInfo networkRegistrationInfo;
        ArrayList arrayList = new ArrayList(ARRAY_KEY_REGISTER_INFORMATION_LIST.length);
        arrayList.add(String.valueOf((System.currentTimeMillis() - this.mStartTime) - 1000));
        String str = i == 0 ? "success" : "fail";
        this.mStartTime = 0L;
        log("reportRegisterInformation registe ".concat(str));
        arrayList.add(str);
        arrayList.add(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isCalibrated() ? "true" : "false");
        ArrayList calibrationInformation = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getCalibrationInformation();
        for (int i3 = 0; i3 < calibrationInformation.size(); i3++) {
            arrayList.add(((Float) calibrationInformation.get(i3)).toString());
        }
        arrayList.add(String.valueOf(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getRealTxPwr()));
        arrayList.add(String.valueOf(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getTarTxPwr()));
        int currentDbm = getCurrentDbm();
        arrayList.add(String.valueOf(currentDbm));
        int retSnr = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getRetSnr();
        arrayList.add(String.valueOf(retSnr));
        arrayList.add(String.valueOf(i2));
        arrayList.add(String.valueOf(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnBeamId()));
        Bundle ntnLocationInfo = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnLocationInfo();
        if (ntnLocationInfo != null) {
            arrayList.add(String.valueOf(ntnLocationInfo.getString("city")));
            arrayList.add(String.valueOf(ntnLocationInfo.getFloat("speed")));
        }
        arrayList.add(TelephonyCommonUtils.getNtnVendorInfo());
        String strValueOf = "N/A";
        arrayList.add(String.valueOf("N/A"));
        if (MiuiPhoneUtils.isValidatePhoneId(this.mPhoneId) && (serviceState = this.mServiceState[this.mPhoneId]) != null && (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(1, 1)) != null) {
            strValueOf = String.valueOf(networkRegistrationInfo.getRejectCause());
        }
        arrayList.add(strValueOf);
        String dataVersion = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getDataVersion();
        int saveBeamId = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getSaveBeamId();
        int usedCityInfoIndex = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getUsedCityInfoIndex();
        String locationType = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getLocationType();
        arrayList.add(dataVersion);
        arrayList.add(String.valueOf(saveBeamId));
        arrayList.add(String.valueOf(usedCityInfoIndex));
        arrayList.add(String.valueOf(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnPosInfo()));
        arrayList.add(String.valueOf(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getErrorClass()));
        arrayList.add(String.valueOf(((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getErrorCause()));
        arrayList.add(locationType);
        HashMap map = new HashMap();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            map.put(ARRAY_KEY_REGISTER_INFORMATION_LIST[i4], String.valueOf(arrayList.get(i4)));
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ntnphone_register_information", map);
        if (TextUtils.equals(str, "fail")) {
            MiuiTelephonyStatistic.getInstance();
            HashMap mapAddDfsSatelliteInfo = MiuiTelephonyStatistic.addDfsSatelliteInfo();
            int errorCause = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getErrorCause();
            int fer = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getFer();
            if (errorCause == 2 || errorCause == 5 || errorCause == 13) {
                MiuiTelephonyStatistic.getInstance().recordDFSEventStatistic(915900001, mapAddDfsSatelliteInfo);
            } else if (errorCause == 17 || errorCause == 20 || errorCause == 21) {
                MiuiTelephonyStatistic.getInstance().recordDFSEventStatistic(915900002, mapAddDfsSatelliteInfo);
            } else if ((currentDbm < -125 || retSnr < 2) && fer > 50) {
                MiuiTelephonyStatistic.getInstance().recordDFSEventStatistic(915900003, mapAddDfsSatelliteInfo);
            }
        }
        arrayList.clear();
    }

    private void reportCallProcessInformation() {
        if (!this.mCallProcessInformationList.isEmpty()) {
            HashMap map = new HashMap();
            ArrayList arrayList = this.mCallProcessInformationList;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                ArrayList arrayList2 = (ArrayList) obj;
                if (!arrayList2.isEmpty()) {
                    log("add callProcessInformationList strArray = " + arrayList2.toString());
                    for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                        map.put(ARRAY_KEY_CALL_PROCESS_INFORMATION_LIST[i2], (String) arrayList2.get(i2));
                    }
                }
            }
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ntnphone_callprocess_information", map);
            this.mCallProcessInformationList.clear();
        }
        this.mCallProcessRecordCount = 60;
    }

    public void finish() {
        reportSignalStrengthTime();
        reportSatellitePower();
        boolean z = MiuiPhoneUtils.getSupportSatelliteSlotId() != -1;
        if (this.mStartTime == 0 || !z) {
            if (!z) {
                log("don't have satellite SIM, don't record register information");
            }
            dispose();
            return;
        }
        this.mNtnPhoneInfoHandler.sendMessage(this.mNtnPhoneInfoHandler.obtainMessage(11, 1, 0));
    }

    private void registerTelephonyCallBack() {
        if (mTelephonyCallBack == null) {
            log("new  TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private void unregisterTelephonyCallBack() {
        TelephonyCallBack telephonyCallBack = mTelephonyCallBack;
        if (telephonyCallBack != null) {
            TelephonyStatAdapter.unregisterCallBack(telephonyCallBack);
            mTelephonyCallBack = null;
        }
    }

    private void initPowerInfo(Map map) {
        map.put("startThermal", MiuiPhoneUtils.getTemperature());
        map.put("startBattery", String.valueOf(BatteryStateManager.getInstance().getmBatteryLevel()));
    }

    private void finishPowerInfo(Map map) {
        if (map != null) {
            map.put("endThermal", MiuiPhoneUtils.getTemperature());
            map.put("endBattery", String.valueOf(BatteryStateManager.getInstance().getmBatteryLevel()));
        }
    }

    private void finishCallInfo(int i, Map map) {
        if (map != null) {
            finishPowerInfo(map);
            if (this.mSatelliteCallPowerMap.containsKey("reason")) {
                map.put("callDuration", String.valueOf((this.mEndCallTime[i] - this.mStartCallTime[i]) / 1000));
                Bundle ntnLocationInfo = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnLocationInfo();
                if (ntnLocationInfo != null) {
                    map.put("callCity", String.valueOf(ntnLocationInfo.getString("city")));
                }
            }
        }
    }

    public void setHangUpReason(String str) {
        Map map = this.mSatelliteCallPowerMap;
        if (map != null) {
            map.put("reason", str);
        }
    }

    public void dispose() {
        log("dispose");
        unregisterTelephonyCallBack();
        this.mIsCallActive = null;
        this.mForegroundCallState = null;
        this.mNtnPhoneInfoHandler.removeCallbacksAndMessages(null);
        this.mNtnPhoneInfoHandler = null;
        this.mTelephonyMgr = null;
        this.mIsInService = null;
        this.mSignalStrength = null;
        this.mCallProcessInformationList.clear();
        this.mCallProcessInformationList = null;
        sInstance = null;
        this.mSatellitePowerMap = null;
        this.mSatelliteCallPowerMap = null;
    }

    protected static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    protected static void loge(String str) {
        Log.e(LOG_TAG, str);
    }

    private String mapToString(Map map) {
        final StringBuilder sb = new StringBuilder();
        if (map == null) {
            return sb.toString();
        }
        map.forEach(new BiConsumer() { // from class: com.android.phone.ntn.NtnPhoneInfoMonitor$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                sb.append(obj + " : " + obj2 + ", ");
            }
        });
        return sb.toString();
    }
}
