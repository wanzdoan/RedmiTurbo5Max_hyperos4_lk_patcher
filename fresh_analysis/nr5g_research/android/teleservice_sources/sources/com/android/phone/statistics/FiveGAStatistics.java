package com.android.phone.statistics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityNr;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthNr;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.FiveGServiceInfo;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.INetworkModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.utils.CountableHandler;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class FiveGAStatistics {
    public static final String TAG = "FiveGAStatistics";
    protected static volatile FiveGAStatistics sInstance;
    private Map mBandMap;
    private Context mContext;
    private FiveGAInfo[] mCurrentFiveGAInfo;
    private ServiceState[] mCurrentServiceState;
    private final BaseEvent.IEventListener mDisplayListener;
    private int[] mFiveG2ccCount;
    private long[] mFiveG2ccTime;
    private int[] mFiveG3ccCount;
    private long[] mFiveG3ccTime;
    private boolean[] mFiveGARegistration;
    private FiveGServiceListener mFiveGServiceListener;
    private FiveGAStatisticsHandler mHandler;
    private Deque[] mInfoQueues;
    private FiveGAInfo[] mLastFiveGAInfo;
    private long[] mNrcaReportTime;
    private String[] mOperatorName;
    private ServiceState[] mPreServiceState;
    private TelephonyCallBack mTelephonyCallBack;
    public static final long STATISTICS_TIME_INTERVAL = RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
    public static final long REGISTRATION_REPORT_TIME_INTERVAL = 21600000;
    private int INVALID = -1;
    private volatile long mDisplayStateOffTime = -1;

    private FiveGAStatistics() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mFiveGARegistration = new boolean[i];
        this.mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.statistics.FiveGAStatistics.1
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                    if (displayEvent.isDefaultDisplay()) {
                        boolean zIsScreenOn = displayEvent.isScreenOn();
                        FiveGAStatistics fiveGAStatistics = FiveGAStatistics.this;
                        if (zIsScreenOn) {
                            fiveGAStatistics.mDisplayStateOffTime = fiveGAStatistics.INVALID;
                        } else {
                            fiveGAStatistics.mDisplayStateOffTime = SystemClock.elapsedRealtime();
                        }
                    }
                }
            }
        };
        this.mCurrentServiceState = new ServiceState[i];
        this.mPreServiceState = new ServiceState[i];
        this.mCurrentFiveGAInfo = new FiveGAInfo[i];
        this.mLastFiveGAInfo = new FiveGAInfo[i];
        this.mFiveG2ccCount = new int[i];
        this.mFiveG3ccCount = new int[i];
        this.mFiveG2ccTime = new long[i];
        this.mFiveG3ccTime = new long[i];
        this.mOperatorName = new String[i];
        this.mInfoQueues = new LinkedList[i];
        this.mBandMap = new HashMap();
        this.mNrcaReportTime = new long[i];
        this.mContext = PhoneGlobals.getInstance();
        int i2 = 0;
        while (true) {
            Deque[] dequeArr = this.mInfoQueues;
            if (i2 < dequeArr.length) {
                dequeArr[i2] = new LinkedList();
                i2++;
            } else {
                this.mHandler = new FiveGAStatisticsHandler(MiuiThreadUtil.getWorkThreadLooper());
                this.mFiveGServiceListener = new FiveGServiceListener();
                this.mTelephonyCallBack = new TelephonyCallBack();
                Arrays.fill(this.mNrcaReportTime, -REGISTRATION_REPORT_TIME_INTERVAL);
                TelephonyStatAdapter.registerCallBack(this.mTelephonyCallBack);
                updateOperatorName();
                EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener, this.mHandler);
                registerFiveGListener();
                return;
            }
        }
    }

    public static FiveGAStatistics getInstance() {
        if (sInstance == null) {
            synchronized (FiveGAStatistics.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new FiveGAStatistics();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public static FiveGAStatistics make() {
        if (sInstance == null) {
            sInstance = new FiveGAStatistics();
        }
        return sInstance;
    }

    public String getFiveG2ccTime(int i) {
        long[] jArr = this.mFiveG2ccTime;
        double d = jArr[i] / 8.64E7d;
        jArr[i] = 0;
        return String.format("%.4f", Double.valueOf(d));
    }

    public String getFiveG3ccTime(int i) {
        long[] jArr = this.mFiveG3ccTime;
        double d = jArr[i] / 8.64E7d;
        jArr[i] = 0;
        return String.format("%.4f", Double.valueOf(d));
    }

    public String getFiveG2ccCount(int i) {
        int[] iArr = this.mFiveG2ccCount;
        int i2 = iArr[i];
        iArr[i] = 0;
        return String.valueOf(i2);
    }

    public String getFiveG3ccCount(int i) {
        int[] iArr = this.mFiveG3ccCount;
        int i2 = iArr[i];
        iArr[i] = 0;
        return String.valueOf(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceState(int i, ServiceState serviceState) {
        ServiceState serviceState2 = this.mCurrentServiceState[i];
        if (serviceState2 != null && getDataNetworkType(serviceState2) == getDataNetworkType(serviceState) && getVoiceNetworkType(this.mCurrentServiceState[i]) == getVoiceNetworkType(serviceState) && this.mCurrentServiceState[i].isUsingCarrierAggregation() == serviceState.isUsingCarrierAggregation()) {
            return;
        }
        StringBuilder sb = new StringBuilder("phoneId = ");
        sb.append(i);
        sb.append(", ServiceState from ");
        ServiceState serviceState3 = this.mCurrentServiceState[i];
        sb.append(serviceState3 == null ? "" : serviceState3.toString());
        log(sb.toString());
        log("ServiceState change to" + serviceState.toString());
        ServiceState[] serviceStateArr = this.mPreServiceState;
        ServiceState[] serviceStateArr2 = this.mCurrentServiceState;
        serviceStateArr[i] = serviceStateArr2[i];
        serviceStateArr2[i] = serviceState;
        updateFiveGAType(i);
    }

    public void notifySimStateChanged(int i, Intent intent) {
        String stringExtra = intent.getStringExtra("ss");
        log("notifySimStateChanged : iccState = " + stringExtra);
        if ("LOADED".equals(stringExtra)) {
            updateOperatorName();
        } else if ("ABSENT".equals(stringExtra)) {
            clearStatisticsBuffer(i);
        }
    }

    private void updateOperatorName() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mOperatorName[i] = MiuiPhoneUtils.getSimOperatorName(i);
            log("phoneId = " + i + ", operatorName = " + this.mOperatorName[i]);
        }
    }

    private void clearStatisticsBuffer(int i) {
        if (i != -1) {
            this.mOperatorName[i] = "";
            this.mFiveG2ccTime[i] = 0;
            this.mFiveG3ccTime[i] = 0;
            this.mFiveG2ccCount[i] = 0;
            this.mFiveG3ccCount[i] = 0;
        }
    }

    public void reportStatistics() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (MiuiPhoneUtils.isIccCardActivated(i)) {
                HashMap map = new HashMap();
                map.put("nrca_operator", this.mOperatorName[i]);
                map.put("nrca_2cc_time", getFiveG2ccTime(i));
                map.put("nrca_2cc_max_count", getFiveG2ccCount(i));
                map.put("nrca_3cc_time", getFiveG3ccTime(i));
                map.put("nrca_3cc_max_count", getFiveG3ccCount(i));
                MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "nrca_statistics", map);
            }
            clearStatisticsBuffer(i);
        }
        if (!this.mBandMap.isEmpty()) {
            HashMap map2 = new HashMap();
            for (Map.Entry entry : this.mBandMap.entrySet()) {
                if (!TextUtils.isEmpty((CharSequence) entry.getKey())) {
                    map2.put((String) entry.getKey(), String.format("%.4f", Double.valueOf(((Long) entry.getValue()).longValue() / 8.64E7d)));
                }
            }
            this.mBandMap.clear();
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "nrca_band_statistics", map2);
        }
        updateOperatorName();
    }

    public void reportRegistrationStatistics(int i, ServiceState serviceState) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mNrcaReportTime[i];
        if (j > 0 && jElapsedRealtime - j < REGISTRATION_REPORT_TIME_INTERVAL) {
            log("Report interval not reached, cancel report");
            return;
        }
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null) {
            CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) getSignalStrengthNr(phone);
            HashMap map = new HashMap();
            Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
            if (cloudData != null) {
                map.put("nrca_p", cloudData.getString("province"));
                map.put("nrca_c", cloudData.getString("city"));
            }
            map.put("nrca_plmn", MiuiPhoneUtils.getOperatorNumeric(phone));
            map.put("nrca_band", getBandString(serviceState));
            if (cellSignalStrengthNr != null) {
                map.put("nrca_rsrp", Integer.valueOf(cellSignalStrengthNr.getSsRsrp()));
                map.put("nrca_snr", Integer.valueOf(cellSignalStrengthNr.getSsSinr()));
            }
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "nrca_registration_statistics", map);
            this.mNrcaReportTime[i] = jElapsedRealtime;
        }
    }

    public void reportFallbackFrom5GAStatistics(int i, ServiceState serviceState) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return;
        }
        long currentDlRateBps = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getCurrentDlRateBps() / 1024;
        long currentUlRateBps = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getCurrentUlRateBps() / 1024;
        HashMap map = new HashMap();
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mDisplayStateOffTime;
        int i2 = this.INVALID;
        long j2 = j == ((long) i2) ? i2 : (jElapsedRealtime - this.mDisplayStateOffTime) / 1000;
        int dataRat = getDataRat(serviceState);
        map.put("fallback_from_nrca_plmn", MiuiPhoneUtils.getOperatorNumeric(phone));
        map.put("fallback_from_nrca_band", getBandString(serviceState));
        map.put("fallback_from_nrca_rat", ServiceState.rilRadioTechnologyToString(dataRat));
        map.put("fallback_from_nrca_screen_off_time", Long.valueOf(j2));
        map.put("fallback_from_nrca_dl_rate", Long.valueOf(currentDlRateBps));
        map.put("fallback_from_nrca_ul_rate", Long.valueOf(currentUlRateBps));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "fallback_from_nrca_statistics", map);
    }

    private int getDataRat(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getRilDataRadioTechnology();
    }

    private boolean isInNrcaState(ServiceState serviceState) {
        return ((getDataNetworkType(serviceState) == 20 || getVoiceNetworkType(serviceState) == 20) && serviceState.isUsingCarrierAggregation()) || getDataNetworkType(serviceState) == 30 || getVoiceNetworkType(serviceState) == 30;
    }

    private int getDataNetworkType(ServiceState serviceState) {
        if (serviceState != null) {
            return serviceState.getDataNetworkType();
        }
        return 0;
    }

    private int getVoiceNetworkType(ServiceState serviceState) {
        if (serviceState != null) {
            return serviceState.getVoiceNetworkType();
        }
        return 0;
    }

    private String getBandString(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo;
        if (serviceState == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1)) == null) {
            return "null";
        }
        CellIdentity cellIdentity = networkRegistrationInfo.getCellIdentity();
        return Arrays.toString(cellIdentity instanceof CellIdentityNr ? ((CellIdentityNr) cellIdentity).getBands() : null);
    }

    private CellSignalStrength getSignalStrengthNr(Phone phone) {
        CellSignalStrengthNr cellSignalStrengthNr = null;
        for (CellSignalStrength cellSignalStrength : phone.getSignalStrengthController().getSignalStrength().getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthNr) {
                cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrength;
            }
        }
        return cellSignalStrengthNr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateFiveGAType(int i) {
        try {
            if (this.mCurrentServiceState[i] == null) {
                return;
            }
            FiveGAInfo fiveGAInfo = new FiveGAInfo();
            boolean zIsInNrcaState = isInNrcaState(this.mCurrentServiceState[i]);
            log("updateFiveGAType[" + i + "]: phoneId = " + i + ", isInNrca = " + zIsInNrcaState + ", fiveGARegistration = " + this.mFiveGARegistration[i]);
            if (zIsInNrcaState && this.mFiveGARegistration[i]) {
                fiveGAInfo.setType(2);
                FiveGAStatisticsHandler fiveGAStatisticsHandler = this.mHandler;
                fiveGAStatisticsHandler.sendMessage(fiveGAStatisticsHandler.obtainMessage(2, i, 0, this.mCurrentServiceState[i]));
            } else if (zIsInNrcaState) {
                fiveGAInfo.setType(1);
            } else {
                fiveGAInfo.setType(0);
                FiveGAInfo fiveGAInfo2 = this.mCurrentFiveGAInfo[i];
                if (fiveGAInfo2 != null && fiveGAInfo2.getType() != 0) {
                    FiveGAStatisticsHandler fiveGAStatisticsHandler2 = this.mHandler;
                    fiveGAStatisticsHandler2.sendMessage(fiveGAStatisticsHandler2.obtainMessage(4, i, 0, this.mCurrentServiceState[i]));
                }
            }
            fiveGAInfo.setTime(SystemClock.elapsedRealtime());
            FiveGAInfo[] fiveGAInfoArr = this.mLastFiveGAInfo;
            FiveGAInfo[] fiveGAInfoArr2 = this.mCurrentFiveGAInfo;
            fiveGAInfoArr[i] = fiveGAInfoArr2[i];
            fiveGAInfoArr2[i] = fiveGAInfo;
            statisticsNrcaTime(i);
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void statisticsNrcaTime(int i) {
        try {
            FiveGAInfo fiveGAInfo = this.mCurrentFiveGAInfo[i];
            FiveGAInfo fiveGAInfo2 = this.mLastFiveGAInfo[i];
            if (fiveGAInfo != null && fiveGAInfo2 != null) {
                if (fiveGAInfo2.getType() != fiveGAInfo.getType()) {
                    if (fiveGAInfo2.getType() == 1) {
                        long[] jArr = this.mFiveG2ccTime;
                        jArr[i] = jArr[i] + (fiveGAInfo.getTime() - fiveGAInfo2.getTime());
                        String bandString = getBandString(this.mPreServiceState[i]);
                        Map map = this.mBandMap;
                        map.put(bandString, Long.valueOf((((Long) map.getOrDefault(bandString, 0L)).longValue() + fiveGAInfo.getTime()) - fiveGAInfo2.getTime()));
                    } else if (fiveGAInfo2.getType() == 2) {
                        long[] jArr2 = this.mFiveG3ccTime;
                        jArr2[i] = jArr2[i] + (fiveGAInfo.getTime() - fiveGAInfo2.getTime());
                        String bandString2 = getBandString(this.mPreServiceState[i]);
                        Map map2 = this.mBandMap;
                        map2.put(bandString2, Long.valueOf((((Long) map2.getOrDefault(bandString2, 0L)).longValue() + fiveGAInfo.getTime()) - fiveGAInfo2.getTime()));
                    }
                    log("FiveGAType[" + i + "]: from  " + fiveGAInfo2.getType() + " change to " + fiveGAInfo.getType());
                    log("statisticsNrcaTime[" + i + "]: FiveG2ccTime = " + this.mFiveG2ccTime[i] + ", mFiveG3ccTime = " + this.mFiveG3ccTime[i]);
                }
                if (fiveGAInfo2.getType() != fiveGAInfo.getType()) {
                    Deque deque = this.mInfoQueues[i];
                    while (!deque.isEmpty() && fiveGAInfo.getTime() - ((FiveGAInfo) deque.peekFirst()).getTime() > STATISTICS_TIME_INTERVAL) {
                        deque.pollFirst();
                    }
                    deque.offerLast(fiveGAInfo);
                    StringBuilder sb = new StringBuilder();
                    int i2 = 0;
                    for (FiveGAInfo fiveGAInfo3 : new LinkedList(deque)) {
                        if (fiveGAInfo.getType() == fiveGAInfo3.getType()) {
                            sb.append("type = " + fiveGAInfo3.getType() + ", time = " + convertToDate(fiveGAInfo3.getTime()) + "|");
                            i2++;
                        }
                    }
                    log(sb.toString());
                    if (fiveGAInfo.getType() == 1) {
                        log("FiveG2ccCount = " + this.mFiveG2ccCount[i] + ", count = " + i2);
                        int[] iArr = this.mFiveG2ccCount;
                        iArr[i] = Math.max(iArr[i], i2);
                    } else if (fiveGAInfo.getType() == 2) {
                        log("FiveG3ccCount = " + this.mFiveG3ccCount[i] + ", count = " + i2);
                        int[] iArr2 = this.mFiveG3ccCount;
                        iArr2[i] = Math.max(iArr2[i], i2);
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void registerFiveGListener() {
        EventHubManager.getInstance().registerListener("telephony", "fiveg_service_info_changed", this.mFiveGServiceListener, this.mHandler);
        log("registerFiveGListener successfully");
    }

    private String convertToDate(long j) {
        return String.valueOf(j / 1000);
    }

    public boolean isFiveGARegistration(int i) {
        FiveGAInfo fiveGAInfo;
        return MiuiPhoneUtils.isValidatePhoneId(i) && (fiveGAInfo = this.mCurrentFiveGAInfo[i]) != null && fiveGAInfo.getType() == 2;
    }

    class FiveGAStatisticsHandler extends CountableHandler {
        public FiveGAStatisticsHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.phone.utils.CountableHandler, android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            FiveGAStatistics.log("Handling messages, msg = " + message.what);
            int i = message.what;
            if (i == 0) {
                FiveGAStatistics.this.updateServiceState(message.arg1, (ServiceState) message.obj);
                return;
            }
            if (i == 1) {
                FiveGAStatistics.this.reportStatistics();
                return;
            }
            if (i == 2) {
                FiveGAStatistics.this.reportRegistrationStatistics(message.arg1, (ServiceState) message.obj);
            } else if (i == 3) {
                FiveGAStatistics.this.updateFiveGAType(message.arg1);
            } else {
                if (i != 4) {
                    return;
                }
                FiveGAStatistics.this.reportFallbackFrom5GAStatistics(message.arg1, (ServiceState) message.obj);
            }
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        private TelephonyCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            if (FiveGAStatistics.this.mHandler != null) {
                FiveGAStatistics.this.mHandler.obtainMessage(0, i, 0, serviceState).sendToTarget();
            }
        }
    }

    class FiveGServiceListener implements BaseEvent.IEventListener {
        private FiveGServiceListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            TelephonyEvent.FiveGEvent fiveGEvent;
            Bundle bundle;
            if (!(baseEvent instanceof TelephonyEvent.FiveGEvent) || (bundle = (fiveGEvent = (TelephonyEvent.FiveGEvent) baseEvent).getBundle()) == null) {
                return;
            }
            int i = bundle.getInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID);
            FiveGServiceInfo fiveGServiceInfo = fiveGEvent.getFiveGServiceInfo();
            if (fiveGServiceInfo == null || !MiuiPhoneUtils.isValidatePhoneId(i)) {
                FiveGAStatistics.log("FiveGServiceListener info is null or invalid phone id");
                return;
            }
            boolean z = fiveGServiceInfo.getNrIconType() == 2;
            if (FiveGAStatistics.this.mFiveGARegistration[i] != z) {
                FiveGAStatistics.log("FiveGARegistration[" + i + "]: from  " + FiveGAStatistics.this.mFiveGARegistration[i] + " change to " + z);
                FiveGAStatistics.this.mFiveGARegistration[i] = z;
                Message messageObtain = Message.obtain(FiveGAStatistics.this.mHandler, 3);
                messageObtain.arg1 = i;
                messageObtain.sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d(TAG, str);
    }
}
