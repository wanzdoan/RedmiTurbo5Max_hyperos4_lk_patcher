package com.android.phone.statistics;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.FiveGCloudController;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.DeviceUtil;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.phone.utils.ModemUtils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.util.v;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.TelephonyManagerEx;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class MiuiNetworkInfoStats {
    private static MiuiNetworkInfoStats sInstance;
    private Context mContext;
    private boolean mIsAirplayMode;
    private boolean mIsScreenOn;
    private final long[] mLastQueryTime;
    private long mLastRecordOosInfoTime;
    private ModemUtils mModemUtils;
    private final long[] mNetworkInServiceDuration;
    private final long[] mNetworkInServiceTimeStamp;
    private NetworkInfoHandler mNetworkInfoHandler;
    private ConcurrentHashMap mNetworkInfoRecords;
    private final ConcurrentHashMap mNetworkInfos;
    private final int[] mQueryCount;
    private final boolean[] mRadioPowerOn;
    private int mRecordOosInfoCount;
    private final boolean[] mRoamingState;
    private final ServiceState[] mServiceState;
    private final SignalStrength[] mSignalStrength;
    private final String[] mSimState;
    private static final OosCauseHandler LTE_REJECT_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda0
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.m3803$r8$lambda$OJ7STXmRUvREgu4M_b7KhKxvJ8(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_REJECT_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda11
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$UVseIOfEpRzgRluWrYf5oxaIs0Q(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler LTE_TIMER_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda14
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.m3800$r8$lambda$1_815hKUisybNnWuUr6Ro6HAuI(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_TIMER_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda15
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$qC7_NtkWxNO0TCDaV06t8reJAtg(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler LTE_RRC_REJECT_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda16
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$4YGZMfo2QPnVgxq6OxWiDbhlxkQ(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler LTE_RLF_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda17
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.m3804$r8$lambda$TrI4fOLTiM5Z9NEJU9uoGFVCqI(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler LTE_RACH_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda18
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$FYpQb48IzbrgmbNvoK7QL7_z__M(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_RACH_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda19
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$RXYDsLMOfjLKqcza_b7uedNGs3E(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler LTE_CELL_SELECTION_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda20
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.m3801$r8$lambda$9Fxu80j2VCdji4IJzbIJmfX6Mw(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler LTE_S_CRITERIA_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda21
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$eDk1iDcOg9Xc9qg3VGH3n0NVNkk(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_S_CRITERIA_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda1
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$udhrwsJhYXEF6b07Ypu8KjLqZmk(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler LTE_ACQ_SUCCESS_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda2
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$F7AkeR4SCg4iuDAhVQFH5876VtU(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_ACQ_SUCCESS_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda3
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$J6pBw6ISC5264fsR8NtmN_MtDZw(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler LTE_SIB_READ_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda4
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$mcUuVUmhgqA4bkwxtQNCDrQJIuw(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler LTE_SRV_STATUS_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda5
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.m3802$r8$lambda$IayBSv1hdAbAQOTVsaN6s_ARM0(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_SRV_STATUS_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda6
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$NrbTbhAazjKcBaAWzsW1K6QIbV0(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler LTE_TIMER_START_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda7
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$D3ivZgGn066pfNtYmoQIywlq9BY(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_RRC_CELL_BARRED_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda8
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$_1SWXqK3P67iqV8JkFNBku3Vx9o(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_RRC_RELEASE_REDIRECT_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda9
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$GazVQ0Ccas2xLaJujGKlfINwvQQ(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_MOBILITY_FROM_NR_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda10
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$EuJ5M24VBHMYEfWbZmYKCKd4pcw(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_RRC_REJECT_WAITTIME_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda12
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$QA7eShGYjklfwXApBFvvySxFOyo(concurrentHashMap, concurrentHashMap2, i);
        }
    };
    private static final OosCauseHandler NR5G_RRC_RLF_HANDLER = new OosCauseHandler() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda13
        @Override // com.android.phone.statistics.MiuiNetworkInfoStats.OosCauseHandler
        public final void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
            MiuiNetworkInfoStats.$r8$lambda$AFUosMnncQnmO6dlowPNK7bympo(concurrentHashMap, concurrentHashMap2, i);
        }
    };

    interface OosCauseHandler {
        void handle(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i);
    }

    private boolean isInService(int i, int i2) {
        return i == 0 && i2 == 0;
    }

    private boolean isNetworkLost(int i, int i2) {
        return i == 1 && i2 == 1;
    }

    private MiuiNetworkInfoStats() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mQueryCount = new int[i];
        this.mLastQueryTime = new long[i];
        this.mSimState = new String[i];
        this.mRoamingState = new boolean[i];
        this.mRadioPowerOn = new boolean[i];
        this.mNetworkInServiceDuration = new long[i];
        this.mNetworkInServiceTimeStamp = new long[i];
        this.mServiceState = new ServiceState[i];
        this.mSignalStrength = new SignalStrength[i];
        this.mNetworkInfos = new ConcurrentHashMap();
        this.mRecordOosInfoCount = 0;
        this.mLastRecordOosInfoTime = 0L;
        this.mNetworkInfoRecords = new ConcurrentHashMap();
        this.mNetworkInfoHandler = new NetworkInfoHandler(MiuiThreadUtil.getWorkThreadLooper());
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mModemUtils = ModemUtils.init(phoneGlobals);
        loadNetworkInfoRecords();
        registerTelephony();
    }

    public static MiuiNetworkInfoStats getInstance() {
        MiuiNetworkInfoStats miuiNetworkInfoStats = sInstance;
        if (miuiNetworkInfoStats != null) {
            return miuiNetworkInfoStats;
        }
        MiuiNetworkInfoStats miuiNetworkInfoStats2 = new MiuiNetworkInfoStats();
        sInstance = miuiNetworkInfoStats2;
        return miuiNetworkInfoStats2;
    }

    private void loadNetworkInfoRecords() {
        String networkInfoStats = DefaultPreferenceHelper.getInstance().getNetworkInfoStats();
        try {
            if (TextUtils.isEmpty(networkInfoStats)) {
                return;
            }
            this.mNetworkInfoRecords = (ConcurrentHashMap) ObjectSerializer.deserialize(networkInfoStats);
        } catch (Exception e) {
            Utils.logE("MiuiNetworkInfoStats", "reloadNetStatsFromSP exception:" + e);
        }
    }

    private void registerTelephony() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null) {
                phone.mCi.registerForRadioStateChanged(this.mNetworkInfoHandler, 2, Integer.valueOf(i));
            }
        }
    }

    class NetworkInfoHandler extends Handler {
        public NetworkInfoHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0 || i == 1) {
                MiuiNetworkInfoStats.this.updateNetOosInfo(((Integer) message.obj).intValue());
            } else {
                if (i != 2) {
                    return;
                }
                int iIntValue = ((Integer) ((AsyncResult) message.obj).userObj).intValue();
                MiuiNetworkInfoStats.this.mRadioPowerOn[iIntValue] = MiuiPhoneUtils.isRadioPowerOn(iIntValue);
            }
        }
    }

    public void onDisplayChanged(Display display) {
        int state = display.getState();
        Utils.logD("MiuiNetworkInfoStats", "onDisplayChanged to state: " + Display.stateToString(state));
        this.mIsScreenOn = state == 2;
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            NetworkInfo networkInfo = (NetworkInfo) this.mNetworkInfos.get(Integer.valueOf(i));
            if (networkInfo != null) {
                boolean z = this.mIsScreenOn;
                if (z && networkInfo.screenOnTimeStamp == 0) {
                    networkInfo.screenOnTimeStamp = SystemClock.elapsedRealtime();
                } else if (!z && networkInfo.screenOnTimeStamp != 0) {
                    networkInfo.screenOnDuration += (SystemClock.elapsedRealtime() - networkInfo.screenOnTimeStamp) / 1000;
                    networkInfo.screenOnTimeStamp = 0L;
                }
            }
        }
    }

    public void onSimCardStateChanged(int i, String str) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            this.mSimState[i] = str;
            Utils.logD("MiuiNetworkInfoStats", "onSimCardStateChanged to state: " + str + " ,for sim: " + i);
            if ("ABSENT".equals(str)) {
                calculateNetworkLostDuration(i);
            }
        }
    }

    public void processServiceState(int i, ServiceState serviceState) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || serviceState == null) {
            return;
        }
        updateNetworkInfo(i, serviceState);
        this.mServiceState[i] = serviceState;
    }

    public void processSignalStrength(int i, SignalStrength signalStrength) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || signalStrength == null) {
            return;
        }
        this.mSignalStrength[i] = signalStrength;
    }

    public void onAirplaneModeChanged(boolean z) {
        this.mIsAirplayMode = z;
        if (z) {
            calculateNetworkLostDuration();
        }
    }

    private void onRoamingStateChanged(NetworkInfo networkInfo, ServiceState serviceState) {
        if (serviceState.getRoaming() && networkInfo.roamingTimeStamp == 0) {
            networkInfo.roamingTimeStamp = SystemClock.elapsedRealtime();
        } else {
            if (serviceState.getRoaming() || networkInfo.roamingTimeStamp == 0) {
                return;
            }
            networkInfo.roamingDuration += (SystemClock.elapsedRealtime() - networkInfo.roamingTimeStamp) / 1000;
            networkInfo.roamingTimeStamp = 0L;
        }
    }

    private void updateNetworkInfo() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            ServiceState serviceState = this.mServiceState[i];
            if (serviceState != null) {
                updateNetworkInfo(i, serviceState);
            }
        }
    }

    private void updateNetworkInfo(int i, ServiceState serviceState) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || serviceState == null) {
            Utils.logD("MiuiNetworkInfoStats", "No need to updateNetworkInfo since serviceState is null.");
            return;
        }
        NetworkInfo networkInfo = (NetworkInfo) this.mNetworkInfos.get(Integer.valueOf(i));
        int voiceRegState = serviceState.getVoiceRegState();
        int dataRegState = serviceState.getDataRegState();
        Utils.logD("MiuiNetworkInfoStats", "updateNetworkInfo voice state: " + ServiceState.rilServiceStateToString(voiceRegState) + " ,data State: " + ServiceState.rilServiceStateToString(dataRegState) + " ,for sim: " + i);
        updateRoamingState(i, serviceState);
        recordNetworkInServiceDuration(i, serviceState, voiceRegState, dataRegState);
        if (networkInfo == null) {
            recordLostNetworkInfo(i, voiceRegState, dataRegState, networkInfo, serviceState);
        } else if (isNetworkStateChanged(i, networkInfo, serviceState, voiceRegState, dataRegState)) {
            recordRecoveryNetworkInfo(i, voiceRegState, dataRegState, networkInfo, serviceState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetOosInfo(int i) {
        ConcurrentHashMap oosInfo;
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            try {
                NetworkInfo networkInfo = (NetworkInfo) this.mNetworkInfos.get(Integer.valueOf(i));
                if (networkInfo != null && (oosInfo = getOosInfo(i)) != null && !oosInfo.isEmpty()) {
                    networkInfo.oosInfo = new ConcurrentHashMap();
                    for (String str : oosInfo.keySet()) {
                        if ("Time".equals(str) && oosInfo.get(str) != null && (oosInfo.get(str) instanceof Long)) {
                            networkInfo.oosInfo.put(str, Utils.systemTimeToString(((Long) oosInfo.get(str)).longValue()));
                        } else {
                            networkInfo.oosInfo.put(str, String.valueOf(oosInfo.get(str)));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void recordLostNetworkInfo(int i, int i2, int i3, NetworkInfo networkInfo, ServiceState serviceState) {
        if (!shouldRecordNetworkLost(i, i2, i3, serviceState)) {
            Utils.logD("MiuiNetworkInfoStats", "Ignore the normal net lost.");
            return;
        }
        Phone phone = PhoneFactory.getPhone(i);
        CellIdentity currentCellIdentity = phone != null ? phone.getCurrentCellIdentity() : null;
        String sceneType = getSceneType();
        long jCurrentTimeMillis = System.currentTimeMillis();
        NetworkInfo networkInfo2 = new NetworkInfo(i, serviceState, currentCellIdentity, sceneType, jCurrentTimeMillis, this.mSignalStrength[i], Utils.getPropertyOperatingMode());
        Utils.logD("MiuiNetworkInfoStats", "recordLostNetworkInfo-voiceState: " + ServiceState.rilServiceStateToString(i2) + " ,dataState: " + ServiceState.rilServiceStateToString(i3) + " ,for sim: " + i + ", time: " + Utils.systemTimeToString(jCurrentTimeMillis));
        this.mNetworkInfos.put(Integer.valueOf(i), networkInfo2);
        networkInfo2.lostNetScreenState = this.mIsScreenOn ? 1 : 0;
        if (this.mRoamingState[i]) {
            networkInfo2.roamingTimeStamp = SystemClock.elapsedRealtime();
        }
        if (this.mIsScreenOn) {
            networkInfo2.screenOnTimeStamp = SystemClock.elapsedRealtime();
        }
        if (isNetworkLost(i2, i3)) {
            networkInfo2.lostNetTimeStamp = SystemClock.elapsedRealtime();
            delayToQueryOosCause(i);
        } else if (i2 == 1) {
            networkInfo2.lostVoiceTimeStamp = SystemClock.elapsedRealtime();
        } else if (i3 == 1) {
            networkInfo2.lostDataTimeStamp = SystemClock.elapsedRealtime();
        } else {
            this.mNetworkInfos.remove(Integer.valueOf(i));
            Utils.logD("MiuiNetworkInfoStats", "Remove the invalid net lost record.");
        }
    }

    private void recordRecoveryNetworkInfo(int i, int i2, int i3, NetworkInfo networkInfo, ServiceState serviceState) {
        Utils.logD("MiuiNetworkInfoStats", "recordReNetworkInfo voice net: " + ServiceState.rilServiceStateToString(i2) + " ,data net: " + ServiceState.rilServiceStateToString(i3) + " ,for sim: " + i);
        if (this.mServiceState[i] != null && serviceState.getRoaming() != this.mServiceState[i].getRoaming()) {
            onRoamingStateChanged(networkInfo, serviceState);
        }
        if (networkInfo.lostNetTimeStamp != 0 && !isNetworkLost(i2, i3)) {
            networkInfo.lostNetDuration = (SystemClock.elapsedRealtime() - networkInfo.lostNetTimeStamp) / 1000;
            networkInfo.lostNetTimeStamp = 0L;
            saveNetworkInfoStats(i, networkInfo, serviceState);
        } else if (networkInfo.lostVoiceTimeStamp != 0 && (i2 == 0 || isNetworkLost(i2, i3))) {
            networkInfo.lostVoiceDuration = (SystemClock.elapsedRealtime() - networkInfo.lostVoiceTimeStamp) / 1000;
            networkInfo.lostVoiceTimeStamp = 0L;
            saveNetworkInfoStats(i, networkInfo, serviceState);
        } else if (networkInfo.lostDataTimeStamp != 0 && (i3 == 0 || isNetworkLost(i2, i3))) {
            networkInfo.lostDataDuration = (SystemClock.elapsedRealtime() - networkInfo.lostDataTimeStamp) / 1000;
            networkInfo.lostDataTimeStamp = 0L;
            saveNetworkInfoStats(i, networkInfo, serviceState);
        }
        if (this.mNetworkInfos.get(Integer.valueOf(i)) != null || isInService(i2, i3)) {
            return;
        }
        updateNetworkInfo(i, serviceState);
    }

    private void updateRoamingState(int i, ServiceState serviceState) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || serviceState == null) {
            Utils.logD("MiuiNetworkInfoStats", "No need to updateRoamingState since serviceState is null or invalid phone id.");
            return;
        }
        if (isInService(serviceState)) {
            boolean roaming = serviceState.getRoaming();
            Utils.logD("MiuiNetworkInfoStats", "updateRoamingState - old state: " + this.mRoamingState[i] + ", new state: " + roaming);
            this.mRoamingState[i] = roaming;
        }
    }

    private void recordNetworkInServiceDuration(int i, ServiceState serviceState, int i2, int i3) {
        if (isInService(i2, i3)) {
            long[] jArr = this.mNetworkInServiceTimeStamp;
            if (jArr[i] == 0) {
                jArr[i] = SystemClock.elapsedRealtime();
                return;
            }
        }
        if (isInService(i2, i3) || this.mNetworkInServiceTimeStamp[i] == 0) {
            return;
        }
        long[] jArr2 = this.mNetworkInServiceDuration;
        long j = jArr2[i];
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long[] jArr3 = this.mNetworkInServiceTimeStamp;
        jArr2[i] = j + ((jElapsedRealtime - jArr3[i]) / 1000);
        jArr3[i] = 0;
    }

    private void delayToQueryOosCause(int i) {
        if (canQueryOosCause(i)) {
            NetworkInfoHandler networkInfoHandler = this.mNetworkInfoHandler;
            networkInfoHandler.sendMessageDelayed(networkInfoHandler.obtainMessage(i == 0 ? 0 : 1, Integer.valueOf(i)), 2000L);
        }
    }

    private void calculateNetworkLostDuration() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            calculateNetworkLostDuration(i);
        }
    }

    private void calculateNetworkLostDuration(int i) {
        NetworkInfo networkInfo = (NetworkInfo) this.mNetworkInfos.get(Integer.valueOf(i));
        if (networkInfo == null) {
            return;
        }
        if (networkInfo.lostNetTimeStamp != 0) {
            networkInfo.lostNetDuration = (SystemClock.elapsedRealtime() - networkInfo.lostNetTimeStamp) / 1000;
            networkInfo.lostNetTimeStamp = 0L;
            saveNetworkInfoStats(i, networkInfo, this.mServiceState[i]);
        } else if (networkInfo.lostVoiceTimeStamp != 0) {
            networkInfo.lostVoiceDuration = (SystemClock.elapsedRealtime() - networkInfo.lostVoiceTimeStamp) / 1000;
            networkInfo.lostVoiceTimeStamp = 0L;
            saveNetworkInfoStats(i, networkInfo, this.mServiceState[i]);
        } else if (networkInfo.lostDataTimeStamp != 0) {
            networkInfo.lostDataDuration = (SystemClock.elapsedRealtime() - networkInfo.lostDataTimeStamp) / 1000;
            networkInfo.lostDataTimeStamp = 0L;
            saveNetworkInfoStats(i, networkInfo, this.mServiceState[i]);
        }
    }

    private void saveNetworkInfoStats(int i, NetworkInfo networkInfo, ServiceState serviceState) {
        networkInfo.recoveryNetState = serviceState;
        networkInfo.reScreenState = this.mIsScreenOn ? 1 : 0;
        networkInfo.recoveryNetSignal = this.mSignalStrength[i];
        if (networkInfo.roamingTimeStamp != 0) {
            networkInfo.roamingDuration += (SystemClock.elapsedRealtime() - networkInfo.roamingTimeStamp) / 1000;
            networkInfo.roamingTimeStamp = 0L;
        }
        if (networkInfo.screenOnTimeStamp != 0) {
            networkInfo.screenOnDuration += (SystemClock.elapsedRealtime() - networkInfo.screenOnTimeStamp) / 1000;
            networkInfo.screenOnTimeStamp = 0L;
        }
        if (shouldRecordReNetworkInfo(networkInfo)) {
            this.mNetworkInfoRecords.put(Long.valueOf(networkInfo.currentTimeStamp), networkInfo.serialize());
            try {
                DefaultPreferenceHelper.getInstance().setNetworkInfoStats(ObjectSerializer.serialize(this.mNetworkInfoRecords));
            } catch (Exception e) {
                Utils.logE("MiuiNetworkInfoStats", "saveNetworkInfoStats exception: ", e);
            }
            Log.d("MiuiNetworkInfoStats", "saveNetworkInfoStats networkInfo: " + networkInfo.toString());
            this.mNetworkInServiceTimeStamp[i] = 0;
            this.mNetworkInServiceDuration[i] = 0;
            this.mRecordOosInfoCount++;
            this.mLastRecordOosInfoTime = SystemClock.elapsedRealtime();
            if (this.mNetworkInfoRecords.toString().getBytes().length > 50176) {
                reportNetworkInfoStats();
                clearNetworkInfoStats();
            }
        } else {
            this.mNetworkInfoHandler.removeMessages(i == 0 ? 0 : 1);
            Utils.logD("MiuiNetworkInfoStats", "Ignore the short duration of this network lost for sim: " + i);
        }
        this.mNetworkInfos.remove(Integer.valueOf(i));
        updateNetworkInfo(i, serviceState);
    }

    private boolean shouldRecordNetworkLost(int i, int i2, int i3, ServiceState serviceState) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && !this.mIsAirplayMode && !serviceState.getIsManualSelection() && "LOADED".equals(this.mSimState[i]) && this.mRadioPowerOn[i] && !isInService(i2, i3)) {
            if (SystemClock.elapsedRealtime() - this.mLastRecordOosInfoTime > v.b) {
                this.mRecordOosInfoCount = 0;
            }
            if (this.mRecordOosInfoCount < 150) {
                return true;
            }
            Utils.logD("MiuiNetworkInfoStats", "The daily limit has been reached and no longer recorded.");
        }
        return false;
    }

    private boolean shouldRecordReNetworkInfo(NetworkInfo networkInfo) {
        return networkInfo.lostNetDuration > 5 || networkInfo.lostVoiceDuration > 10 || networkInfo.lostDataDuration > 10;
    }

    private boolean canQueryOosCause(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            Utils.logD("MiuiNetworkInfoStats", "Invalid phoneId: " + i);
            return false;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long[] jArr = this.mLastQueryTime;
        if (jElapsedRealtime - jArr[i] > RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
            jArr[i] = jElapsedRealtime;
            this.mQueryCount[i] = 0;
        }
        int[] iArr = this.mQueryCount;
        int i2 = iArr[i];
        if (i2 <= 5) {
            iArr[i] = i2 + 1;
            return true;
        }
        Utils.logD("MiuiNetworkInfoStats", "Ignore the short duration of this network lost reason for sim: " + i);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInService(ServiceState serviceState) {
        if (serviceState == null) {
            return false;
        }
        return serviceState.getVoiceRegState() == 0 && serviceState.getDataRegState() == 0;
    }

    private boolean isNetworkStateChanged(int i, NetworkInfo networkInfo, ServiceState serviceState, int i2, int i3) {
        ServiceState serviceState2;
        if (networkInfo != null && (serviceState2 = networkInfo.lostNetState) != null) {
            boolean z = serviceState2.getVoiceRegState() != i2;
            boolean z2 = networkInfo.lostNetState.getDataRegState() != i3;
            ServiceState serviceState3 = this.mServiceState[i];
            boolean z3 = (serviceState3 == null || serviceState3.getRoaming() == serviceState.getRoaming()) ? false : true;
            if (!z && !z2 && !z3) {
                return false;
            }
        }
        return true;
    }

    public void reportNetworkInfoStats() {
        calculateNetworkLostDuration();
        ConcurrentHashMap concurrentHashMap = this.mNetworkInfoRecords;
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            Utils.logD("MiuiNetworkInfoStats", "There is no oos record, no need to report!");
            return;
        }
        HashMap map = new HashMap();
        map.put("Params", convertMapStatsToList(this.mNetworkInfoRecords));
        Utils.logD("MiuiNetworkInfoStats", "reportNetworkInfoStats: " + map);
        MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "event_lost_network_info", map);
    }

    public void clearNetworkInfoStats() {
        this.mNetworkInfos.clear();
        this.mNetworkInfoRecords.clear();
        this.mRecordOosInfoCount = 0;
        this.mLastRecordOosInfoTime = 0L;
        try {
            DefaultPreferenceHelper.getInstance().setNetworkInfoStats(ObjectSerializer.serialize(this.mNetworkInfoRecords));
        } catch (Exception e) {
            Utils.logE("MiuiNetworkInfoStats", "saveNetworkInfoStats exception: ", e);
        }
        Utils.logD("MiuiNetworkInfoStats", "clearNetworkInfoStats.");
        updateNetworkInfo();
    }

    private List convertMapStatsToList(ConcurrentHashMap concurrentHashMap) {
        ArrayList arrayList = new ArrayList();
        for (Long l : concurrentHashMap.keySet()) {
            l.longValue();
            Map map = (Map) concurrentHashMap.get(l);
            if (map != null) {
                Utils.logD("MiuiNetworkInfoStats", "convertMapStatsToList value:" + map);
                arrayList.add(map);
            }
        }
        return arrayList;
    }

    private ConcurrentHashMap getOosInfo(int i) {
        ArrayList oosInfo = parseOosInfo(i);
        if (oosInfo == null || oosInfo.isEmpty()) {
            return null;
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        for (int i2 = 0; i2 < oosInfo.size(); i2++) {
            ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) oosInfo.get(i2);
            int iIntValue = ((Integer) concurrentHashMap2.getOrDefault("OosCause", -1)).intValue();
            OosCauseHandler handler = getHandler(iIntValue);
            if (handler != null) {
                handler.handle(concurrentHashMap, concurrentHashMap2, i2 + 1);
            } else {
                Log.i("MiuiNetworkInfoStats", "Unknown oos cause: " + iIntValue);
            }
        }
        return concurrentHashMap;
    }

    private OosCauseHandler getHandler(int i) {
        switch (i) {
            case 1:
                return LTE_REJECT_HANDLER;
            case 2:
                return NR5G_REJECT_HANDLER;
            case 3:
                return LTE_TIMER_HANDLER;
            case 4:
                return NR5G_TIMER_HANDLER;
            case 5:
                return LTE_RRC_REJECT_HANDLER;
            case 6:
                return LTE_RLF_HANDLER;
            case 7:
                return LTE_RACH_HANDLER;
            case 8:
                return NR5G_RACH_HANDLER;
            case 9:
                return LTE_CELL_SELECTION_HANDLER;
            case 10:
                return LTE_S_CRITERIA_HANDLER;
            case 11:
                return NR5G_S_CRITERIA_HANDLER;
            case 12:
                return LTE_ACQ_SUCCESS_HANDLER;
            case 13:
                return NR5G_ACQ_SUCCESS_HANDLER;
            case 14:
                return LTE_SIB_READ_HANDLER;
            case 15:
                return LTE_SRV_STATUS_HANDLER;
            case 16:
                return NR5G_SRV_STATUS_HANDLER;
            case 17:
                return LTE_TIMER_START_HANDLER;
            case 18:
                return NR5G_RRC_CELL_BARRED_HANDLER;
            case 19:
                return NR5G_RRC_RELEASE_REDIRECT_HANDLER;
            case 20:
                return NR5G_MOBILITY_FROM_NR_HANDLER;
            case 21:
                return NR5G_RRC_REJECT_WAITTIME_HANDLER;
            case 22:
                return NR5G_RRC_RLF_HANDLER;
            default:
                return null;
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$OJ7STXmRUvREgu4M_b7Kh-KxvJ8, reason: not valid java name */
    public static /* synthetic */ void m3803$r8$lambda$OJ7STXmRUvREgu4M_b7KhKxvJ8(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteRejectTag", Integer.valueOf(i));
        concurrentHashMap.put("LteRejectMsgType", concurrentHashMap2.get("RejectMsgType"));
        concurrentHashMap.put("LteRejectCause", concurrentHashMap2.get("RejectCause"));
    }

    public static /* synthetic */ void $r8$lambda$UVseIOfEpRzgRluWrYf5oxaIs0Q(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gRejectTag", Integer.valueOf(i));
        concurrentHashMap.put("Nr5gRejectMsgType", concurrentHashMap2.get("RejectMsgType"));
        concurrentHashMap.put("Nr5gRejectCause", concurrentHashMap2.get("RejectCause"));
    }

    /* JADX INFO: renamed from: $r8$lambda$1_815hKUisybNnWuUr6Ro-6HAuI, reason: not valid java name */
    public static /* synthetic */ void m3800$r8$lambda$1_815hKUisybNnWuUr6Ro6HAuI(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteTimerTag", Integer.valueOf(i));
        concurrentHashMap.put("LteTimerId", concurrentHashMap2.get("TimerId"));
    }

    public static /* synthetic */ void $r8$lambda$qC7_NtkWxNO0TCDaV06t8reJAtg(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gTimerTag", Integer.valueOf(i));
        concurrentHashMap.put("Nr5gTimerId", concurrentHashMap2.get("TimerId"));
    }

    public static /* synthetic */ void $r8$lambda$4YGZMfo2QPnVgxq6OxWiDbhlxkQ(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteRrcRejectTag", Integer.valueOf(i));
        concurrentHashMap.put("RrcMsgType", concurrentHashMap2.get("RrcMsgType"));
    }

    /* JADX INFO: renamed from: $r8$lambda$TrI4fOLT-iM5Z9NEJU9uoGFVCqI, reason: not valid java name */
    public static /* synthetic */ void m3804$r8$lambda$TrI4fOLTiM5Z9NEJU9uoGFVCqI(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteRlfTag", Integer.valueOf(i));
        concurrentHashMap.put("RlfCause", concurrentHashMap2.get("RlfCause"));
        concurrentHashMap.put("Lte_Rsrp", concurrentHashMap2.get("Lte_Rsrp"));
    }

    public static /* synthetic */ void $r8$lambda$FYpQb48IzbrgmbNvoK7QL7_z__M(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteRachTag", Integer.valueOf(i));
        concurrentHashMap.put("Lte_Sa_Rsrp", concurrentHashMap2.get("Lte_Sa_Rsrp"));
    }

    public static /* synthetic */ void $r8$lambda$RXYDsLMOfjLKqcza_b7uedNGs3E(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gRachTag", Integer.valueOf(i));
        concurrentHashMap.put("Lte_Sa_Rsrp", concurrentHashMap2.get("Lte_Sa_Rsrp"));
    }

    /* JADX INFO: renamed from: $r8$lambda$9Fxu80j2VCdj-i4IJzbIJmfX6Mw, reason: not valid java name */
    public static /* synthetic */ void m3801$r8$lambda$9Fxu80j2VCdji4IJzbIJmfX6Mw(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteCellRelTag", Integer.valueOf(i));
        concurrentHashMap.put("CellSeletCause", concurrentHashMap2.get("CellSeletCause"));
    }

    public static /* synthetic */ void $r8$lambda$eDk1iDcOg9Xc9qg3VGH3n0NVNkk(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteSCritTag", Integer.valueOf(i));
        concurrentHashMap.put("LteRelevmin", concurrentHashMap2.get("LteRelevmin"));
        concurrentHashMap.put("MeasuredRsrp", concurrentHashMap2.get("MeasuredRsrp"));
        concurrentHashMap.put("Rxlev", concurrentHashMap2.get("Rxlev"));
        concurrentHashMap.put("LteQual", concurrentHashMap2.get("LteQual"));
        if (concurrentHashMap2.containsKey("LteSCritEarfcn")) {
            concurrentHashMap.put("LteSCritEarfcn", concurrentHashMap2.get("LteSCritEarfcn"));
            concurrentHashMap.put("LteSCritPci", concurrentHashMap2.get("LteSCritPci"));
        }
    }

    public static /* synthetic */ void $r8$lambda$udhrwsJhYXEF6b07Ypu8KjLqZmk(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gSCritTag", Integer.valueOf(i));
        concurrentHashMap.put("Result", concurrentHashMap2.get("Result"));
        concurrentHashMap.put("SaRelevmin", concurrentHashMap2.get("SaRelevmin"));
        concurrentHashMap.put("CellQRsrp", concurrentHashMap2.get("CellQRsrp"));
        concurrentHashMap.put("RxLevel", concurrentHashMap2.get("RxLevel"));
        concurrentHashMap.put("SaQual", concurrentHashMap2.get("SaQual"));
        if (concurrentHashMap2.containsKey("Nr5gSCritArfcn")) {
            concurrentHashMap.put("Nr5gSCritArfcn", concurrentHashMap2.get("Nr5gSCritArfcn"));
            concurrentHashMap.put("Nr5gSCritPci", concurrentHashMap2.get("Nr5gSCritPci"));
        }
    }

    public static /* synthetic */ void $r8$lambda$F7AkeR4SCg4iuDAhVQFH5876VtU(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteAcqSuccTag", Integer.valueOf(i));
        concurrentHashMap.put("Earfcn", concurrentHashMap2.get("Earfcn"));
        concurrentHashMap.put("AcqResult", concurrentHashMap2.get("AcqResult"));
    }

    public static /* synthetic */ void $r8$lambda$J6pBw6ISC5264fsR8NtmN_MtDZw(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gAcqSuccTag", Integer.valueOf(i));
        concurrentHashMap.put("Arfcn", concurrentHashMap2.get("Arfcn"));
        concurrentHashMap.put("Status", concurrentHashMap2.get("Status"));
        concurrentHashMap.put("MaxNbEnergy", concurrentHashMap2.get("MaxNbEnergy"));
    }

    public static /* synthetic */ void $r8$lambda$mcUuVUmhgqA4bkwxtQNCDrQJIuw(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteSibReadTag", Integer.valueOf(i));
        concurrentHashMap.put("SibFreq", concurrentHashMap2.get("SibFreq"));
        concurrentHashMap.put("SibCellId", concurrentHashMap2.get("SibCellId"));
        concurrentHashMap.put("RxedSibsMask", concurrentHashMap2.get("RxedSibsMask"));
    }

    /* JADX INFO: renamed from: $r8$lambda$IayBSv1hdAbAQOTVsa-N6s_ARM0, reason: not valid java name */
    public static /* synthetic */ void m3802$r8$lambda$IayBSv1hdAbAQOTVsaN6s_ARM0(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteSrvTag", Integer.valueOf(i));
        concurrentHashMap.put("Time", concurrentHashMap2.get("Time"));
        concurrentHashMap.put("Mcc", concurrentHashMap2.get("Mcc"));
        concurrentHashMap.put("Mnc", concurrentHashMap2.get("Mnc"));
        concurrentHashMap.put("Freq", concurrentHashMap2.get("Freq"));
        concurrentHashMap.put("CellId", concurrentHashMap2.get("CellId"));
        concurrentHashMap.put("Tac", concurrentHashMap2.get("Tac"));
        concurrentHashMap.put("hplmn", concurrentHashMap2.get("hplmn"));
        concurrentHashMap.put("vplmn", concurrentHashMap2.get("vplmn"));
        concurrentHashMap.put("is_roam", concurrentHashMap2.get("is_roam"));
    }

    public static /* synthetic */ void $r8$lambda$NrbTbhAazjKcBaAWzsW1K6QIbV0(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gSrvTag", Integer.valueOf(i));
        concurrentHashMap.put("Time", concurrentHashMap2.get("Time"));
        concurrentHashMap.put("Mcc", concurrentHashMap2.get("Mcc"));
        concurrentHashMap.put("Mnc", concurrentHashMap2.get("Mnc"));
        concurrentHashMap.put("Freq", concurrentHashMap2.get("Freq"));
        concurrentHashMap.put("CellId", concurrentHashMap2.get("CellId"));
        concurrentHashMap.put("Tac", concurrentHashMap2.get("Tac"));
        concurrentHashMap.put("hplmn", concurrentHashMap2.get("hplmn"));
        concurrentHashMap.put("vplmn", concurrentHashMap2.get("vplmn"));
        concurrentHashMap.put("is_roam", concurrentHashMap2.get("is_roam"));
    }

    public static /* synthetic */ void $r8$lambda$D3ivZgGn066pfNtYmoQIywlq9BY(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("LteTimerStartTag", Integer.valueOf(i));
        concurrentHashMap.put("TimerId", concurrentHashMap2.get("TimerId"));
    }

    public static /* synthetic */ void $r8$lambda$_1SWXqK3P67iqV8JkFNBku3Vx9o(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gRrcCellBarredTag", Integer.valueOf(i));
        concurrentHashMap.put("CellBarred", concurrentHashMap2.get("CellBarred"));
    }

    public static /* synthetic */ void $r8$lambda$GazVQ0Ccas2xLaJujGKlfINwvQQ(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gRrcReleaseRedirectTag", Integer.valueOf(i));
        concurrentHashMap.put("RedirectFreq", concurrentHashMap2.get("RedirectFreq"));
    }

    public static /* synthetic */ void $r8$lambda$EuJ5M24VBHMYEfWbZmYKCKd4pcw(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gMobilityFromNrTag", Integer.valueOf(i));
        concurrentHashMap.put("TargetRAT", concurrentHashMap2.get("TargetRAT"));
    }

    public static /* synthetic */ void $r8$lambda$QA7eShGYjklfwXApBFvvySxFOyo(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gRrcRejectWaitTimeTag", Integer.valueOf(i));
        concurrentHashMap.put("WaitTime", concurrentHashMap2.get("WaitTime"));
    }

    public static /* synthetic */ void $r8$lambda$AFUosMnncQnmO6dlowPNK7bympo(ConcurrentHashMap concurrentHashMap, ConcurrentHashMap concurrentHashMap2, int i) {
        concurrentHashMap.put("Nr5gRrcRlfTag", Integer.valueOf(i));
        concurrentHashMap.put("ScgFailureCause", concurrentHashMap2.get("ScgFailureCause"));
    }

    private ArrayList parseOosInfo(int i) {
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        while (z) {
            ModemUtils modemUtils = this.mModemUtils;
            if (modemUtils == null) {
                Utils.logD("MiuiNetworkInfoStats", "Function: parseOosInfo mModemUtils is null!");
                return null;
            }
            Object objCallQcrilHookGetObject = modemUtils.callQcrilHookGetObject("getUeOosInfo", i);
            if (objCallQcrilHookGetObject == null) {
                Utils.logD("MiuiNetworkInfoStats", "Function: getUeOosInfo is not support!");
                return null;
            }
            ByteBuffer byteBuffer = (ByteBuffer) objCallQcrilHookGetObject;
            int i2 = byteBuffer.get() & TransitionInfo.INIT;
            short s = byteBuffer.getShort();
            if (s <= 0) {
                break;
            }
            if (s < (i2 == 2 ? (short) 10 : i2 >= 1 ? (short) 12 : (short) 14)) {
                z = false;
            }
            for (int i3 = 0; i3 < s; i3++) {
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                concurrentHashMap.put("Time", Long.valueOf(byteBuffer.getLong()));
                concurrentHashMap.put("Mcc", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap.put("Mnc", Short.valueOf(byteBuffer.getShort()));
                concurrentHashMap.put("Freq", Integer.valueOf(byteBuffer.getInt()));
                concurrentHashMap.put("CellId", Long.valueOf(byteBuffer.getLong()));
                concurrentHashMap.put("Tac", Integer.valueOf(byteBuffer.getInt()));
                if (i2 == 2) {
                    concurrentHashMap.put("hplmn", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("vplmn", Integer.valueOf(byteBuffer.getInt()));
                    concurrentHashMap.put("is_roam", Byte.valueOf(byteBuffer.get()));
                }
                parseOosCause(byteBuffer, concurrentHashMap, i2);
                logParseOosInfo(concurrentHashMap);
                addToSortedListWithDeduplication(arrayList, concurrentHashMap);
            }
        }
        return arrayList;
    }

    private void addToSortedListWithDeduplication(ArrayList arrayList, ConcurrentHashMap concurrentHashMap) {
        int iIntValue = ((Integer) concurrentHashMap.get("OosCause")).intValue();
        long jLongValue = ((Long) concurrentHashMap.get("Time")).longValue();
        for (int i = 0; i < arrayList.size(); i++) {
            ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) arrayList.get(i);
            if (((Integer) concurrentHashMap2.get("OosCause")).intValue() == iIntValue) {
                long jLongValue2 = ((Long) concurrentHashMap2.get("Time")).longValue();
                if (jLongValue > jLongValue2) {
                    arrayList.remove(i);
                    Utils.logD("MiuiNetworkInfoStats", "Removed old record for cause: " + iIntValue + ", time: " + jLongValue2);
                    break;
                }
                Utils.logD("MiuiNetworkInfoStats", "Kept existing record for cause: " + iIntValue + ", existing time: " + jLongValue2 + ", new time: " + jLongValue + " (discarded)");
                return;
            }
        }
        arrayList.add(findInsertPosition(arrayList, concurrentHashMap), concurrentHashMap);
        Utils.logD("MiuiNetworkInfoStats", "Added record for cause: " + iIntValue + ", time: " + jLongValue);
    }

    private int findInsertPosition(ArrayList arrayList, ConcurrentHashMap concurrentHashMap) {
        if (arrayList.isEmpty()) {
            return 0;
        }
        int iBinarySearch = Collections.binarySearch(arrayList, concurrentHashMap, new Comparator() { // from class: com.android.phone.statistics.MiuiNetworkInfoStats$$ExternalSyntheticLambda22
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Long.compare(((Long) ((ConcurrentHashMap) obj).get("Time")).longValue(), ((Long) ((ConcurrentHashMap) obj2).get("Time")).longValue());
            }
        });
        return iBinarySearch < 0 ? (-iBinarySearch) - 1 : iBinarySearch;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void parseOosCause(ByteBuffer byteBuffer, ConcurrentHashMap concurrentHashMap, int i) {
        byte b = byteBuffer.get();
        concurrentHashMap.put("OosCause", Integer.valueOf(b));
        int i2 = 2;
        switch (b) {
            case 1:
            case 2:
                float f = byteBuffer.getFloat();
                float f2 = byteBuffer.getFloat();
                concurrentHashMap.put("RejectMsgType", Float.valueOf(f));
                concurrentHashMap.put("RejectCause", Float.valueOf(f2));
                i2 = 3;
                break;
            case 3:
            case 4:
            case 17:
                concurrentHashMap.put("TimerId", Float.valueOf(byteBuffer.getFloat()));
                i2 = 4;
                break;
            case 5:
                concurrentHashMap.put("RrcMsgType", Float.valueOf(byteBuffer.getFloat()));
                i2 = 4;
                break;
            case 6:
                float f3 = byteBuffer.getFloat();
                float f4 = byteBuffer.getFloat();
                concurrentHashMap.put("RlfCause", Float.valueOf(f3));
                concurrentHashMap.put("Lte_Rsrp", Float.valueOf(f4));
                i2 = 3;
                break;
            case 7:
            case 8:
                concurrentHashMap.put("Lte_Sa_Rsrp", Float.valueOf(byteBuffer.getFloat()));
                i2 = 4;
                break;
            case 9:
                concurrentHashMap.put("CellSeletCause", Float.valueOf(byteBuffer.getFloat()));
                i2 = 4;
                break;
            case 10:
                float f5 = byteBuffer.getFloat();
                float f6 = byteBuffer.getFloat();
                float f7 = byteBuffer.getFloat();
                float f8 = byteBuffer.getFloat();
                concurrentHashMap.put("LteRelevmin", Float.valueOf(f5));
                concurrentHashMap.put("MeasuredRsrp", Float.valueOf(f6));
                concurrentHashMap.put("Rxlev", Float.valueOf(f7));
                concurrentHashMap.put("LteQual", Float.valueOf(f8));
                if (i >= 1) {
                    float f9 = byteBuffer.getFloat();
                    float f10 = byteBuffer.getFloat();
                    concurrentHashMap.put("LteSCritEarfcn", Float.valueOf(f9));
                    concurrentHashMap.put("LteSCritPci", Float.valueOf(f10));
                }
                i2 = 1;
                break;
            case 11:
                float f11 = byteBuffer.getFloat();
                float f12 = byteBuffer.getFloat();
                float f13 = byteBuffer.getFloat();
                float f14 = byteBuffer.getFloat();
                float f15 = byteBuffer.getFloat();
                concurrentHashMap.put("Result", Float.valueOf(f11));
                concurrentHashMap.put("SaRelevmin", Float.valueOf(f12));
                concurrentHashMap.put("CellQRsrp", Float.valueOf(f13));
                concurrentHashMap.put("RxLevel", Float.valueOf(f14));
                concurrentHashMap.put("SaQual", Float.valueOf(f15));
                if (i >= 1) {
                    float f16 = byteBuffer.getFloat();
                    float f17 = byteBuffer.getFloat();
                    concurrentHashMap.put("Nr5gSCritArfcn", Float.valueOf(f16));
                    concurrentHashMap.put("Nr5gSCritPci", Float.valueOf(f17));
                }
                i2 = 0;
                break;
            case 12:
                float f18 = byteBuffer.getFloat();
                float f19 = byteBuffer.getFloat();
                concurrentHashMap.put("Earfcn", Float.valueOf(f18));
                concurrentHashMap.put("AcqResult", Float.valueOf(f19));
                i2 = 3;
                break;
            case 13:
                float f20 = byteBuffer.getFloat();
                float f21 = byteBuffer.getFloat();
                float f22 = byteBuffer.getFloat();
                concurrentHashMap.put("Arfcn", Float.valueOf(f20));
                concurrentHashMap.put("Status", Float.valueOf(f21));
                concurrentHashMap.put("MaxNbEnergy", Float.valueOf(f22));
                break;
            case 14:
                float f23 = byteBuffer.getFloat();
                float f24 = byteBuffer.getFloat();
                float f25 = byteBuffer.getFloat();
                concurrentHashMap.put("SibFreq", Float.valueOf(f23));
                concurrentHashMap.put("SibCellId", Float.valueOf(f24));
                concurrentHashMap.put("RxedSibsMask", Float.valueOf(f25));
                break;
            case 15:
            case 16:
                i2 = 5;
                break;
            case 18:
                byteBuffer.getFloat();
                concurrentHashMap.put("CellBarred", Float.valueOf(byteBuffer.getFloat()));
                i2 = 3;
                break;
            case 19:
                byteBuffer.getFloat();
                concurrentHashMap.put("RedirectFreq", Float.valueOf(byteBuffer.getFloat()));
                i2 = 3;
                break;
            case 20:
                byteBuffer.getFloat();
                concurrentHashMap.put("TargetRAT", Float.valueOf(byteBuffer.getFloat()));
                i2 = 3;
                break;
            case 21:
                byteBuffer.getFloat();
                concurrentHashMap.put("WaitTime", Float.valueOf(byteBuffer.getFloat()));
                i2 = 3;
                break;
            case 22:
                concurrentHashMap.put("ScgFailureCause", Float.valueOf(byteBuffer.getFloat()));
                i2 = 4;
                break;
            default:
                Log.i("MiuiNetworkInfoStats", "Unknown oos cause: " + ((int) b));
                i2 = 5;
                break;
        }
        if (i >= 1 && b != 10 && b != 11) {
            i2 += 2;
        }
        byteBuffer.position(byteBuffer.position() + (i2 * 4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getSceneType() {
        return MiuiTelephonyStatistic.getInstance() == null ? "NA" : MiuiTelephonyStatistic.getInstance().getSceneType();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRsrp(SignalStrength signalStrength) {
        if (signalStrength == null) {
            return 0;
        }
        return signalStrength.getDbm();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVoiceNetworkType(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getVoiceNetworkType();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDataNetworkType(ServiceState serviceState) {
        if (serviceState == null) {
            return 0;
        }
        return serviceState.getDataNetworkType();
    }

    private void logParseOosInfo(ConcurrentHashMap concurrentHashMap) {
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            return;
        }
        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap(concurrentHashMap);
        concurrentHashMap2.remove("CellId");
        concurrentHashMap2.remove("Tac");
        concurrentHashMap2.remove("SibCellId");
        Log.d("MiuiNetworkInfoStats", "parseOosInfo: " + concurrentHashMap2.toString());
    }

    class NetworkInfo {
        int OperatingMode;
        long currentTimeStamp;
        boolean enableEndc = true;
        IFiveGPowerModule fiveGPower;
        boolean isManualSel;
        long lostDataDuration;
        long lostDataTimeStamp;
        CellIdentity lostNetCellIdentity;
        long lostNetDuration;
        int lostNetMode;
        int lostNetScreenState;
        SignalStrength lostNetSignal;
        ServiceState lostNetState;
        long lostNetTimeStamp;
        long lostVoiceDuration;
        long lostVoiceTimeStamp;
        int oosCause;
        ConcurrentHashMap oosInfo;
        int phoneId;
        int reScreenState;
        SignalStrength recoveryNetSignal;
        ServiceState recoveryNetState;
        long roamingDuration;
        long roamingTimeStamp;
        String sceneType;
        long screenOnDuration;
        long screenOnTimeStamp;

        public NetworkInfo(int i, ServiceState serviceState, CellIdentity cellIdentity, String str, long j, SignalStrength signalStrength, int i2) {
            this.phoneId = i;
            this.sceneType = str;
            this.lostNetState = serviceState;
            this.lostNetCellIdentity = cellIdentity;
            this.currentTimeStamp = j;
            this.lostNetSignal = signalStrength;
            this.OperatingMode = i2;
            this.lostNetMode = Utils.getPreferredNwModeFromDb(i);
        }

        /* JADX WARN: Type inference failed for: r3v29 */
        /* JADX WARN: Type inference failed for: r3v30, types: [boolean, int] */
        /* JADX WARN: Type inference failed for: r3v38 */
        public Map serialize() {
            boolean[] endcPowerEnable;
            int i;
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentHashMap.put("SimId", Integer.toString(this.phoneId));
            concurrentHashMap.put("apTime", Utils.systemTimeToString(this.currentTimeStamp));
            concurrentHashMap.put("Scene", this.sceneType);
            concurrentHashMap.put("OpMode", String.valueOf(this.OperatingMode));
            concurrentHashMap.put("LNMode", Integer.toString(this.lostNetMode));
            concurrentHashMap.put("LNDur", Long.toString(this.lostNetDuration));
            concurrentHashMap.put("LVDur", Long.toString(this.lostVoiceDuration));
            concurrentHashMap.put("LDDur", Long.toString(this.lostDataDuration));
            concurrentHashMap.put("LNSS", Integer.toString(this.lostNetScreenState));
            concurrentHashMap.put("LNRsrp", Integer.toString(MiuiNetworkInfoStats.this.getRsrp(this.lostNetSignal)));
            concurrentHashMap.put("LNBand", Utils.getBand(this.phoneId, this.lostNetState));
            concurrentHashMap.put("LVNetType", Integer.toString(MiuiNetworkInfoStats.this.getVoiceNetworkType(this.lostNetState)));
            concurrentHashMap.put("LDNetType", Integer.toString(MiuiNetworkInfoStats.this.getDataNetworkType(this.lostNetState)));
            ServiceState serviceState = this.lostNetState;
            ServiceState[] serviceStateArr = MiuiNetworkInfoStats.this.mServiceState;
            int i2 = this.phoneId;
            concurrentHashMap.put("LNetNum", Utils.getAvailableOperatorNumeric(serviceState, serviceStateArr[i2], i2));
            HashMap cellParamInfo = Utils.getCellParamInfo(this.lostNetCellIdentity);
            if (!cellParamInfo.isEmpty()) {
                concurrentHashMap.putAll(cellParamInfo);
            }
            concurrentHashMap.put("ScreenOnDur", Long.toString(this.screenOnDuration));
            concurrentHashMap.put("ReSS", Integer.toString(this.reScreenState));
            concurrentHashMap.put("ReNetMode", Integer.toString(Utils.getPreferredNwModeFromDb(this.phoneId)));
            concurrentHashMap.put("ReBand", Utils.getBand(this.phoneId, this.recoveryNetState));
            concurrentHashMap.put("ReScene", MiuiNetworkInfoStats.getSceneType());
            concurrentHashMap.put("ReRsrp", Integer.toString(MiuiNetworkInfoStats.this.getRsrp(this.recoveryNetSignal)));
            ServiceState serviceState2 = this.recoveryNetState;
            ServiceState[] serviceStateArr2 = MiuiNetworkInfoStats.this.mServiceState;
            int i3 = this.phoneId;
            concurrentHashMap.put("ReNetNum", Utils.getAvailableOperatorNumeric(serviceState2, serviceStateArr2[i3], i3));
            MiuiNetworkInfoStats miuiNetworkInfoStats = MiuiNetworkInfoStats.this;
            concurrentHashMap.put("ReSState", String.valueOf(miuiNetworkInfoStats.isInService(miuiNetworkInfoStats.mServiceState[this.phoneId]) ? 1 : 0));
            concurrentHashMap.put("RDur", Long.toString(this.roamingDuration));
            concurrentHashMap.put("IDur", Long.toString(MiuiNetworkInfoStats.this.mNetworkInServiceDuration[this.phoneId]));
            String city = FiveGCloudController.getInstance() != null ? FiveGCloudController.getInstance().getCity() : "";
            String province = FiveGCloudController.getInstance() != null ? FiveGCloudController.getInstance().getProvince() : "";
            concurrentHashMap.put("operating_mode", System.getProperty("persist.radio.operating_mode", "null"));
            String string = Settings.Global.getString(MiuiNetworkInfoStats.this.mContext.getContentResolver(), "radio_stall");
            if (string == null) {
                string = "null";
            }
            concurrentHashMap.put("radio_stall", string);
            String string2 = Settings.Global.getString(MiuiNetworkInfoStats.this.mContext.getContentResolver(), "reattach");
            if (string2 == null) {
                string2 = "null";
            }
            concurrentHashMap.put("reattach", string2);
            String string3 = Settings.Global.getString(MiuiNetworkInfoStats.this.mContext.getContentResolver(), "mbn_refresh");
            concurrentHashMap.put("mbn_refresh", string3 != null ? string3 : "null");
            concurrentHashMap.put("radio_status", TelephonyManagerEx.RADIO_STATUS);
            IFiveGPowerModule iFiveGPowerModule = (IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class);
            this.fiveGPower = iFiveGPowerModule;
            if (iFiveGPowerModule != null && (endcPowerEnable = iFiveGPowerModule.getEndcPowerEnable()) != null && (i = this.phoneId) >= 0 && i < endcPowerEnable.length) {
                this.enableEndc = endcPowerEnable[i];
            }
            concurrentHashMap.put("enable_endc", String.valueOf(this.enableEndc ? 1 : 0));
            ?? r3 = (MiuiNetworkInfoStats.this.mServiceState[this.phoneId] == null || !MiuiNetworkInfoStats.this.mServiceState[this.phoneId].getIsManualSelection()) ? 0 : 1;
            this.isManualSel = r3;
            concurrentHashMap.put("net_sel_pref", String.valueOf((int) r3));
            if (!DeviceUtil.IS_INTERNATIONAL_BUILD) {
                concurrentHashMap.put("C", city);
                concurrentHashMap.put("P", province);
                concurrentHashMap.put("Iccid", Utils.getIccidSS(PhoneGlobals.getInstance(), this.phoneId));
            }
            ConcurrentHashMap concurrentHashMap2 = this.oosInfo;
            if (concurrentHashMap2 != null && !concurrentHashMap2.isEmpty()) {
                concurrentHashMap.putAll(this.oosInfo);
            }
            return concurrentHashMap;
        }

        public String toString() {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            ConcurrentHashMap concurrentHashMap2 = this.oosInfo;
            if (concurrentHashMap2 != null && !concurrentHashMap2.isEmpty()) {
                concurrentHashMap.putAll(this.oosInfo);
                concurrentHashMap.remove("CellId");
                concurrentHashMap.remove("Tac");
                concurrentHashMap.remove("SibCellId");
            }
            StringBuilder sb = new StringBuilder();
            sb.append(getCommonParamsInfo());
            sb.append(", Oos Info: ");
            sb.append(!concurrentHashMap.isEmpty() ? concurrentHashMap.toString() : "");
            return sb.toString();
        }

        public String getCommonParamsInfo() {
            return "SimId: " + this.phoneId + ", TimeStamp: " + Utils.systemTimeToString(this.currentTimeStamp) + ", Scene: " + this.sceneType + ", LostNetMode: " + this.lostNetMode + ", LostNetDuration: " + this.lostNetDuration + ", LostVoiceDuration: " + this.lostVoiceDuration + ", LostDataDuration: " + this.lostDataDuration + ", LostNetScreenState: " + this.lostNetScreenState + ", Band: " + Utils.getBand(this.phoneId, this.lostNetState) + ", ScreenOnDuration: " + this.screenOnDuration + ", ReScreenState: " + this.reScreenState + ", ReBand: " + Utils.getBand(this.phoneId, this.recoveryNetState) + ", ReScene: " + MiuiNetworkInfoStats.getSceneType() + ", OosCause: " + this.oosCause + ", RoamingDuration: " + this.roamingDuration + ", InServiceDuration: " + MiuiNetworkInfoStats.this.mNetworkInServiceDuration[this.phoneId] + ", SimIccidSS: " + Utils.getIccidSS(PhoneGlobals.getInstance(), this.phoneId);
        }
    }
}
