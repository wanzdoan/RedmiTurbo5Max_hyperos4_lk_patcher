package com.android.phone;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.statistics.HighSpeedTrainStats;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.NrSceneConstants$Scene;
import com.ot.pubsub.util.v;
import com.xiaomi.hyperradio.HyperRadioManager;
import com.xiaomi.internal.hyperradio.xms.CellBarringInfoSInfo;
import com.xiaomi.internal.hyperradio.xms.CellInfoSInfo;
import com.xiaomi.internal.hyperradio.xms.DataStallParamSInfo;
import com.xiaomi.internal.hyperradio.xms.DubiousCellInfoSInfo;
import com.xiaomi.internal.hyperradio.xms.LteDubiousCellInfoSInfo;
import com.xiaomi.internal.hyperradio.xms.NrDubiousCellInfoSInfo;
import com.xiaomi.internal.hyperradio.xms.PlmnIdSInfo;
import com.xiaomi.internal.hyperradio.xms.PreferredCellInfoSInfo;
import com.xiaomi.internal.hyperradio.xms.PreferredCellListSInfo;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import miui.telephony.qms.CellBarringInfoS;
import miui.telephony.qms.CellInfoS;
import miui.telephony.qms.CellPropertyCategoryCommonS;
import miui.telephony.qms.CellPropertyCategoryHdrRatTypeS;
import miui.telephony.qms.CellPropertyCategoryHdrS;
import miui.telephony.qms.CellPropertyCategoryHstTypeS;
import miui.telephony.qms.CellPropertyCategoryRsS;
import miui.telephony.qms.CellPropertyCategoryTypeV2S;
import miui.telephony.qms.DataStallParamS;
import miui.telephony.qms.DubiousCellInfoS;
import miui.telephony.qms.LocationS;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.NrDubiousCellInfoS;
import miui.telephony.qms.PlmnIdS;
import miui.telephony.qms.PreferredCellInfoS;
import miui.telephony.qms.PreferredCellListS;
import miui.telephony.qms.SetCellListInfoTypeV2S;
import miui.telephony.qms.SetCellListInfoV2S;
import miui.telephony.qms.UniqueCellInfoS;
import xiaomi.hypercomm.HyperCommManager;

/* JADX INFO: loaded from: classes.dex */
public final class NetworkRecoveryStrategyImpl extends NetworkRecoveryStrategyBase {
    private Handler mHandler;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Map HST_FREQUENCY_MAP = new ConcurrentHashMap();
    private static final String TRIGGER_REASON_CELL_CHANGED = NetworkMetricsController.Scene.CELLCHANGED.name();
    private static final String[] MTK_DATA_STALL_ENABLE = {"1,3,0,0,3000,8,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-112,-3,0xFF,0xFF,0,2000,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,3,0,1,3000,8,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-112,-3,0xFF,0xFF,0,2000,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    private static final String[] MTK_DATA_STALL_ENABLE_NORMAL = {"1,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,500,1,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,500,1,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    private static final String[] MTK_DATA_STALL_DISABLE = {"1,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    private final Map nrCaCallRecords = new ConcurrentHashMap();
    private boolean mIsForceDisableNr2LteForLabTest = false;
    private LRUCache mCommonLteDubiousCellCache = new LRUCache(16, 0.75f, true);
    private LRUCache mCommonNrDubiousCellCache = new LRUCache(16, 0.75f, true);

    enum MtkAtCmd {
        ENABLE_WEAK_OPT("AT+EGCMD=315,1,\"02\"", ""),
        DISABLE_WEAK_OPT("AT+EGCMD=315,1,\"00\"", ""),
        ENABLE_HSR("AT+EGCMD=343,1,\"01\"", ""),
        DISABLE_HSR("AT+EGCMD=343,1,\"00\"", ""),
        ENABLE_CONGESTION_1("AT+EGCMD=450,1,\"01\"", ""),
        ENABLE_CONGESTION_2("AT+EGCMD=470,1,\"01\"", ""),
        DISABLE_CONGESTION_1("AT+EGCMD=450,1,\"00\"", ""),
        DISABLE_CONGESTION_2("AT+EGCMD=470,1,\"00\"", "");

        String atCmd0;
        String atCmd1;

        MtkAtCmd(String str, String str2) {
            this.atCmd0 = str;
            this.atCmd1 = str2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String[] cmd() {
            String str = this.atCmd1;
            String str2 = this.atCmd0;
            if (str != null) {
                return new String[]{str2, str};
            }
            return new String[]{str2};
        }
    }

    public class HstFrequencyData {
        public final List lteData;
        public final List nrData;

        public HstFrequencyData(List list, List list2) {
            this.lteData = list;
            this.nrData = list2;
        }
    }

    public static void clearHstFrequencyCache() {
        HST_FREQUENCY_MAP.clear();
    }

    public static HstFrequencyData getHstFrequencyData(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        String strConcat = str.concat(str2);
        Map map = HST_FREQUENCY_MAP;
        HstFrequencyData hstFrequencyData = (HstFrequencyData) map.get(strConcat);
        if (hstFrequencyData != null) {
            return hstFrequencyData;
        }
        List frequencyData = HstFrequencyConfig.getFrequencyData(str, str2, true);
        List frequencyData2 = HstFrequencyConfig.getFrequencyData(str, str2, false);
        if (frequencyData == null && frequencyData2 == null) {
            return null;
        }
        HstFrequencyData hstFrequencyData2 = new HstFrequencyData(frequencyData, frequencyData2);
        map.put(strConcat, hstFrequencyData2);
        return hstFrequencyData2;
    }

    public static NetworkRecoveryStrategyBase make() {
        if (NetworkRecoveryStrategyBase.mInstance instanceof NetworkRecoveryStrategyImpl) {
            return NetworkRecoveryStrategyBase.mInstance;
        }
        NetworkRecoveryStrategyBase.mInstance = new NetworkRecoveryStrategyImpl();
        return NetworkRecoveryStrategyBase.mInstance;
    }

    private NetworkRecoveryStrategyImpl() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mRilHook = RilHook.init(phoneGlobals);
        HandlerThread handlerThread = new HandlerThread("NetworkRecoveryStrategy");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public boolean setNrCaStatus(int i, boolean z, String str) {
        if (this.mRilHook == null) {
            log("setNrCaStatus mRilHook is null");
            return false;
        }
        if (getNrCaCallCount() >= 100) {
            log("setNrCaStatusMsg[" + i + "] 超过限制: 12 小时内最多调用100次, triggerReason:" + str);
            return false;
        }
        List list = (List) this.nrCaCallRecords.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.phone.NetworkRecoveryStrategyImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return NetworkRecoveryStrategyImpl.$r8$lambda$NQcKzGqcBGDNoVohlALNq8nnFFU((Integer) obj);
            }
        });
        synchronized (list) {
            list.add(Long.valueOf(System.currentTimeMillis()));
        }
        boolean nrCaStatusMsg = this.mRilHook.setNrCaStatusMsg(i, z, str);
        if (nrCaStatusMsg && !z) {
            HighSpeedTrainStats.beginCaDynamicOpt();
        }
        return nrCaStatusMsg;
    }

    public static /* synthetic */ List $r8$lambda$NQcKzGqcBGDNoVohlALNq8nnFFU(Integer num) {
        return new ArrayList(100);
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public boolean getNrCaStatus(int i) {
        RilHook rilHook = this.mRilHook;
        if (rilHook == null) {
            log("getNrCaStatus mRilHook is null");
            return false;
        }
        return rilHook.getNrCaStatusMsg(i);
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public int getNrCaCallCount() {
        int size = 0;
        for (List list : this.nrCaCallRecords.values()) {
            synchronized (list) {
                cleanExpiredRecords(list);
                size += list.size();
            }
        }
        return size;
    }

    private void cleanExpiredRecords(List list) {
        final long jCurrentTimeMillis = System.currentTimeMillis() - v.c;
        list.removeIf(new Predicate() { // from class: com.android.phone.NetworkRecoveryStrategyImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return NetworkRecoveryStrategyImpl.m1758$r8$lambda$wYLI8VhTlMeTTg1Y7SAW9BY6yE(jCurrentTimeMillis, (Long) obj);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$wYLI8VhTlMeTTg1Y7SAW-9BY6yE, reason: not valid java name */
    public static /* synthetic */ boolean m1758$r8$lambda$wYLI8VhTlMeTTg1Y7SAW9BY6yE(long j, Long l) {
        return l.longValue() < j;
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void forceLte(int i) {
        if (this.mRilHook != null) {
            log("forceLte");
            this.mRilHook.forceScanLte(i);
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void forceLte2Nr(int i) {
        if (this.mRilHook != null) {
            log("forceNr");
            this.mRilHook.forceLte2Nr(i);
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void enableNsa(boolean z, int i) {
        enableNsa(z, i, null);
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void enableNsa(boolean z, final int i, final INetworkRecoveryStrategy$RecoveryResultCallback iNetworkRecoveryStrategy$RecoveryResultCallback) {
        if (FiveGUtils.isXmRadioSupported()) {
            NrSceneConstants$Scene nrSceneConstants$Scene = NrSceneConstants$Scene.NSA_NETWORK_RECOVERY_STRATEGY;
            int typeByScene = FiveGUtils.getTypeByScene(nrSceneConstants$Scene);
            final String strValueOf = String.valueOf(nrSceneConstants$Scene.getValue());
            HyperCommManager.getDefault().requestNrOperation(i, strValueOf, typeByScene, !z, new ResultReceiver(this, this.mHandler) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.1
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i2, Bundle bundle) {
                    boolean z2 = i2 == 0;
                    NetworkRecoveryStrategyImpl.log("requestNrOperation result=" + i2 + "; featureId=" + strValueOf + "; slotId=" + i);
                    INetworkRecoveryStrategy$RecoveryResultCallback iNetworkRecoveryStrategy$RecoveryResultCallback2 = iNetworkRecoveryStrategy$RecoveryResultCallback;
                    if (iNetworkRecoveryStrategy$RecoveryResultCallback2 != null) {
                        iNetworkRecoveryStrategy$RecoveryResultCallback2.onComplete(z2);
                    }
                }
            });
            return;
        }
        try {
            log("enableNsa [" + i + "],enabled[" + z + "]");
            if (ignoreDisableFiveG() && !z) {
                log(" skip disable endc in misence scenario..");
                if (iNetworkRecoveryStrategy$RecoveryResultCallback != null) {
                    iNetworkRecoveryStrategy$RecoveryResultCallback.onComplete(false);
                    return;
                }
                return;
            }
            ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).bindServiceAndEnableEndc(z, i);
            if (iNetworkRecoveryStrategy$RecoveryResultCallback != null) {
                iNetworkRecoveryStrategy$RecoveryResultCallback.onComplete(true);
            }
        } catch (Exception e) {
            log("enableNsa: Exception = " + e);
            if (iNetworkRecoveryStrategy$RecoveryResultCallback != null) {
                iNetworkRecoveryStrategy$RecoveryResultCallback.onComplete(false);
            }
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void enableSa(boolean z, int i) {
        enableSa(z, i, null);
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void enableSa(boolean z, final int i, final INetworkRecoveryStrategy$RecoveryResultCallback iNetworkRecoveryStrategy$RecoveryResultCallback) {
        if (!z && (ignoreDisableFiveG() || this.mIsForceDisableNr2LteForLabTest)) {
            log("skip disable sa");
            if (iNetworkRecoveryStrategy$RecoveryResultCallback != null) {
                iNetworkRecoveryStrategy$RecoveryResultCallback.onComplete(false);
                return;
            }
            return;
        }
        if (FiveGUtils.isXmRadioSupported()) {
            NrSceneConstants$Scene nrSceneConstants$Scene = NrSceneConstants$Scene.NETWORK_RECOVERY_STRATEGY;
            int typeByScene = FiveGUtils.getTypeByScene(nrSceneConstants$Scene);
            final String strValueOf = String.valueOf(nrSceneConstants$Scene.getValue());
            HyperCommManager.getDefault().requestNrOperation(i, strValueOf, typeByScene, true ^ z, new ResultReceiver(this, this.mHandler) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.2
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i2, Bundle bundle) {
                    boolean z2 = i2 == 0;
                    NetworkRecoveryStrategyImpl.log("requestNrOperation result=" + i2 + "; featureId=" + strValueOf + "; slotId=" + i);
                    INetworkRecoveryStrategy$RecoveryResultCallback iNetworkRecoveryStrategy$RecoveryResultCallback2 = iNetworkRecoveryStrategy$RecoveryResultCallback;
                    if (iNetworkRecoveryStrategy$RecoveryResultCallback2 != null) {
                        iNetworkRecoveryStrategy$RecoveryResultCallback2.onComplete(z2);
                    }
                }
            });
            return;
        }
        try {
            log("enableSa [" + i + "],enabled[" + z + "]");
            ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).enableSaForNetRecovery(z, i);
            if (iNetworkRecoveryStrategy$RecoveryResultCallback != null) {
                iNetworkRecoveryStrategy$RecoveryResultCallback.onComplete(true);
            }
        } catch (Exception e) {
            log("enableSa: Exception = " + e);
            if (iNetworkRecoveryStrategy$RecoveryResultCallback != null) {
                iNetworkRecoveryStrategy$RecoveryResultCallback.onComplete(false);
            }
        }
    }

    public void setForceDisableNr2LteForLabTest(boolean z) {
        this.mIsForceDisableNr2LteForLabTest = z;
        log("setForceDisableNr2LteForLabTest: " + this.mIsForceDisableNr2LteForLabTest);
    }

    private boolean ignoreDisableFiveG() {
        SmartNetworkGameController smartNetworkGameController = SmartNetworkGameController.getInstance();
        return (smartNetworkGameController == null || !smartNetworkGameController.mIsInGame || smartNetworkGameController.mCellularNetworkSwitchOn) ? false : true;
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void reAttach(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("reAttach[" + i + "]");
            String str = sdf.format(new Date());
            Settings.Global.putString(this.mContext.getContentResolver(), "reattach", str + "_reAttach[" + i + "]");
            this.mRilHook.lteReAttach(i);
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void restartRadio(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("restartRadio[" + i + "]");
            MiuiPhoneUtils.getPhone(i).getServiceStateTracker().powerOffRadioSafely();
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void setSbpOosHighRatOptState(boolean z) {
        String[] strArr;
        if (!ModemProvider.IS_MTK) {
            super.setSbpOosHighRatOptState(z);
            return;
        }
        log("setSbpOosHighRatOptState enable=" + z);
        Object[] objArr = new Object[1];
        if (z) {
            strArr = new String[]{"AT+ESBP=5,\"SBP_OOS_HIGH_RAT_OPT\",1", "+ESBP"};
        } else {
            strArr = new String[]{"AT+ESBP=5,\"SBP_OOS_HIGH_RAT_OPT\",0", "+ESBP"};
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            FiveGManagerBase.getInstance().atCmdRunSync(i, strArr, objArr);
        }
    }

    private void executeMtkDataStallCommand(int i, boolean z) {
        executeMtkDataStallCommand(i, z ? MTK_DATA_STALL_ENABLE : MTK_DATA_STALL_DISABLE);
    }

    private void executeMtkDataStallCommand(int i, String[] strArr) {
        for (String str : strArr) {
            if (str != null && !str.isEmpty()) {
                String strConcat = "MIPC_SET_DATA_STALL_OPT,".concat(str);
                log("executeMtkDataStallCommand: ".concat(strConcat));
                log("executeMtkDataStallCommand result: ".concat(FiveGManagerBase.getInstance().atCmdRunSync(i, strConcat.split(","), new Object[1]) ? "succeeded" : "failed"));
            }
        }
    }

    private boolean shouldUseMtkDataStallNormalCommand(boolean z, String str) {
        return !z && TRIGGER_REASON_CELL_CHANGED.equals(str);
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void alleviateDataStall(int i, int i2, int i3, boolean z, String str) {
        if (ModemProvider.IS_MTK) {
            if (MiuiPhoneUtils.isNetworkQoeSupported()) {
                if (shouldUseMtkDataStallNormalCommand(z, str)) {
                    executeMtkDataStallCommand(i, MTK_DATA_STALL_ENABLE_NORMAL);
                    return;
                } else {
                    executeMtkDataStallCommand(i, z);
                    return;
                }
            }
            super.alleviateDataStall(i, i2, i3, z, str);
            return;
        }
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("processDataStall appType: " + i2 + " trafficDirection :" + i3 + " dataStallStatus :" + z + " triggerReason :" + str);
            DataStallParamS dataStallParamS = new DataStallParamS();
            dataStallParamS.trafficDirection = i3;
            dataStallParamS.dataStallStatus = z;
            dataStallParamS.appType = i2;
            dataStallParamS.expectedIpPacketCount = -1;
            dataStallParamS.expectedDataRate = -1;
            dataStallParamS.lostIpPacketCount = -1;
            dataStallParamS.measurementInterval = -1;
            if (z) {
                HashMap map = new HashMap();
                Phone phone = PhoneFactory.getPhone(i);
                if (phone == null || phone.getCurrentCellIdentity() == null || phone.getCurrentCellIdentity().getGlobalCellId() == null) {
                    return;
                }
                map.put("DataStallBadCell", phone.getCurrentCellIdentity().getGlobalCellId());
                map.put("DataStallReason", str);
                MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_data_stall_statistic", map);
            }
            if (ModemProvider.IS_XRING) {
                DataStallParamSInfo dataStallParamSInfoConvertToDataStallParamSInfo = convertToDataStallParamSInfo(dataStallParamS);
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
                Handler handler = this.mHandler;
                Objects.requireNonNull(handler);
                hyperRadioManager.setDataStallParameters(i, dataStallParamSInfoConvertToDataStallParamSInfo, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.3
                    public void onResponse(int i4) {
                        NetworkRecoveryStrategyImpl.log("XRING: setDataStallParameters done with errorCode " + i4);
                    }
                });
                return;
            }
            if (ModemProvider.IS_QCOM) {
                QmsMsgTunnelControllerBase.sendQmsRequestSafe(1001, i, dataStallParamS, str);
            }
        }
    }

    public DataStallParamSInfo convertToDataStallParamSInfo(DataStallParamS dataStallParamS) {
        DataStallParamSInfo dataStallParamSInfo = new DataStallParamSInfo();
        dataStallParamSInfo.trafficDirection = dataStallParamS.trafficDirection;
        dataStallParamSInfo.dataStallStatus = dataStallParamS.dataStallStatus;
        dataStallParamSInfo.appType = dataStallParamS.appType;
        dataStallParamSInfo.expectedIpPacketCount = dataStallParamS.expectedIpPacketCount;
        dataStallParamSInfo.expectedDataRate = dataStallParamS.expectedDataRate;
        dataStallParamSInfo.lostIpPacketCount = dataStallParamS.lostIpPacketCount;
        dataStallParamSInfo.measurementInterval = dataStallParamS.measurementInterval;
        return dataStallParamSInfo;
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public synchronized void setLteDubiousCell(int i, ArrayList arrayList, int i2, String str) {
        if (ModemProvider.IS_MTK) {
            super.setLteDubiousCell(i, arrayList, i2, str);
            return;
        }
        if (MiuiPhoneUtils.isValidatePhoneId(i) && arrayList != null && i2 >= 0 && i2 <= 1) {
            if (!updateLteCache(arrayList, i2)) {
                log("mCommonLteDubiousCellCache not change, return");
                return;
            }
            if (ModemProvider.IS_XRING) {
                ArrayList arrayList2 = new ArrayList(this.mCommonLteDubiousCellCache.values());
                LteDubiousCellInfoSInfo[] lteDubiousCellInfoSInfoArr = new LteDubiousCellInfoSInfo[arrayList2.size()];
                for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                    lteDubiousCellInfoSInfoArr[i3] = convertToLteSInfo((LteDubiousCellInfoS) arrayList2.get(i3));
                }
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
                Handler handler = this.mHandler;
                Objects.requireNonNull(handler);
                hyperRadioManager.setLteDubiousCellArray(i, lteDubiousCellInfoSInfoArr, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.4
                    public void onResponse(int i4) {
                        NetworkRecoveryStrategyImpl.log("XRING: setLteDubiousCellArray done with errorCode " + i4);
                    }
                });
                log("XRING: setLteDubiousCell, slotId=" + i + ", cacheSize=" + arrayList2.size() + ", triggerReason=" + str);
                return;
            }
            if (ModemProvider.IS_QCOM) {
                ArrayList arrayList3 = new ArrayList(this.mCommonLteDubiousCellCache.values());
                QmsMsgTunnelControllerBase.sendQmsRequestSafe(1004, i, arrayList3, str);
                log("setLteDubiousCell: slotId=" + i + ", size=" + arrayList.size() + ", triggerReason=" + str + ", sendsize=" + arrayList3.size());
            }
        }
    }

    private boolean updateLteCache(ArrayList arrayList, int i) {
        int size = arrayList.size();
        boolean z = false;
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            if (obj instanceof LteDubiousCellInfoS) {
                LteDubiousCellInfoS lteDubiousCellInfoS = (LteDubiousCellInfoS) obj;
                int i3 = lteDubiousCellInfoS.cgi;
                LRUCache lRUCache = this.mCommonLteDubiousCellCache;
                if (i == 0) {
                    if (!lRUCache.containsKey(Integer.valueOf(i3))) {
                        this.mCommonLteDubiousCellCache.put(Integer.valueOf(i3), lteDubiousCellInfoS);
                        z = true;
                    }
                } else if (lRUCache.containsKey(Integer.valueOf(i3))) {
                    this.mCommonLteDubiousCellCache.remove(Integer.valueOf(i3));
                    z = true;
                }
            } else {
                log("Invalid object in list: not LteDubiousCellInfoS, skip");
            }
        }
        return z;
    }

    private LteDubiousCellInfoSInfo convertToLteSInfo(LteDubiousCellInfoS lteDubiousCellInfoS) {
        DubiousCellInfoS dubiousCellInfoS = lteDubiousCellInfoS.cellInfo;
        return new LteDubiousCellInfoSInfo(createDubiousCellInfoSInfo(dubiousCellInfoS.plmnId, dubiousCellInfoS), lteDubiousCellInfoS.cgi);
    }

    private NrDubiousCellInfoSInfo convertToNrSInfo(NrDubiousCellInfoS nrDubiousCellInfoS) {
        DubiousCellInfoS dubiousCellInfoS = nrDubiousCellInfoS.cellInfo;
        return new NrDubiousCellInfoSInfo(createDubiousCellInfoSInfo(dubiousCellInfoS.plmnId, dubiousCellInfoS), nrDubiousCellInfoS.cgi, nrDubiousCellInfoS.nrScsType);
    }

    private DubiousCellInfoSInfo createDubiousCellInfoSInfo(PlmnIdS plmnIdS, DubiousCellInfoS dubiousCellInfoS) {
        return new DubiousCellInfoSInfo(new PlmnIdSInfo((char) plmnIdS.mcc, (char) plmnIdS.mnc, plmnIdS.mncIncludesPcsDigit), dubiousCellInfoS.activeBand, dubiousCellInfoS.arfcn, (char) dubiousCellInfoS.pci, dubiousCellInfoS.causeCodeBitMask);
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public synchronized void setNrDubiousCell(int i, ArrayList arrayList, int i2, String str) {
        if (ModemProvider.IS_MTK) {
            super.setLteDubiousCell(i, arrayList, i2, str);
            return;
        }
        if (MiuiPhoneUtils.isValidatePhoneId(i) && arrayList != null && i2 >= 0 && i2 <= 1) {
            if (!updateNrCache(arrayList, i2)) {
                log("mCommonNrDubiousCellCache not change, return");
                return;
            }
            if (ModemProvider.IS_XRING) {
                ArrayList arrayList2 = new ArrayList(this.mCommonNrDubiousCellCache.values());
                NrDubiousCellInfoSInfo[] nrDubiousCellInfoSInfoArr = new NrDubiousCellInfoSInfo[arrayList2.size()];
                int size = arrayList2.size();
                int i3 = 0;
                int i4 = 0;
                while (i4 < size) {
                    Object obj = arrayList2.get(i4);
                    i4++;
                    nrDubiousCellInfoSInfoArr[i3] = convertToNrSInfo((NrDubiousCellInfoS) obj);
                    i3++;
                }
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
                Handler handler = this.mHandler;
                Objects.requireNonNull(handler);
                hyperRadioManager.setNrDubiousCellArray(i, nrDubiousCellInfoSInfoArr, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.5
                    public void onResponse(int i5) {
                        NetworkRecoveryStrategyImpl.log("XRING: setNrDubiousCellArray done with errorCode " + i5);
                    }
                });
                log("XRING: setNrDubiousCell, slotId=" + i + ", cacheSize=" + arrayList2.size() + ", triggerReason=" + str);
                return;
            }
            if (ModemProvider.IS_QCOM) {
                ArrayList arrayList3 = new ArrayList(this.mCommonNrDubiousCellCache.values());
                QmsMsgTunnelControllerBase.sendQmsRequestSafe(1005, i, arrayList3, str);
                log("QCOM: setNrDubiousCell, slotId=" + i + ", listSize=" + arrayList.size() + ", triggerReason=" + str + ", sendSize=" + arrayList3.size());
            }
        }
    }

    private boolean updateNrCache(ArrayList arrayList, int i) {
        int size = arrayList.size();
        boolean z = false;
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            if (obj instanceof NrDubiousCellInfoS) {
                NrDubiousCellInfoS nrDubiousCellInfoS = (NrDubiousCellInfoS) obj;
                long j = nrDubiousCellInfoS.cgi;
                LRUCache lRUCache = this.mCommonNrDubiousCellCache;
                if (i == 0) {
                    if (!lRUCache.containsKey(Long.valueOf(j))) {
                        this.mCommonNrDubiousCellCache.put(Long.valueOf(j), nrDubiousCellInfoS);
                        z = true;
                    }
                } else if (lRUCache.containsKey(Long.valueOf(j))) {
                    this.mCommonNrDubiousCellCache.remove(Long.valueOf(j));
                    z = true;
                }
            } else {
                log("Invalid object in list: not NrDubiousCellInfoS, skip");
            }
        }
        return z;
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void setOutOfServiceState(int i, int i2, String str) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && i2 >= 0 && i2 <= 1) {
            log("setOutOfServiceState slotId=" + i + ", scanState=" + i2 + ", triggerReason=" + str);
            if (ModemProvider.IS_XRING) {
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
                Handler handler = this.mHandler;
                Objects.requireNonNull(handler);
                hyperRadioManager.setOutOfServiceState(i, i2, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.6
                    public void onResponse(int i3) {
                        NetworkRecoveryStrategyImpl.log("XRING: setOutOfServiceState done with errorCode " + i3);
                    }
                });
                return;
            }
            if (ModemProvider.IS_QCOM) {
                QmsMsgTunnelControllerBase.sendQmsRequestSafe(1010, i, Integer.valueOf(i2), str);
            }
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void setHstState(int i, String str) {
        if (!ModemProvider.IS_QCOM) {
            super.setHstState(i, str);
            return;
        }
        log("setHstState hstState=" + i + ",triggerReason=" + str);
        QmsMsgTunnelControllerBase.sendQmsRequestSafe(1012, 0, Integer.valueOf(i), str);
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void getHstState(String str) {
        if (ModemProvider.IS_MTK) {
            super.getHstState(str);
            return;
        }
        log("getHstState triggerReason=" + str);
        if (ModemProvider.IS_XRING) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            hyperRadioManager.getHstState(0, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.7
                public void onResponse(int i) {
                    NetworkRecoveryStrategyImpl.log("XRING: getHstState done with errorCode " + i);
                }
            });
            return;
        }
        if (ModemProvider.IS_QCOM) {
            QmsMsgTunnelControllerBase.sendQmsRequestSafe(1013, 0, null, str);
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void setCoverageState(int i, int i2, String str) {
        if (ModemProvider.IS_MTK) {
            super.setCoverageState(i, i2, str);
            return;
        }
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("setCoverageState slotId=" + i + ", coverageState=" + i2 + ", triggerReason=" + str);
            if (ModemProvider.IS_XRING) {
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
                Handler handler = this.mHandler;
                Objects.requireNonNull(handler);
                hyperRadioManager.setCoverageState(i, i2, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.8
                    public void onResponse(int i3) {
                        NetworkRecoveryStrategyImpl.log("XRING: setCoverageState done with errorCode " + i3);
                    }
                });
                return;
            }
            if (ModemProvider.IS_QCOM) {
                QmsMsgTunnelControllerBase.sendQmsRequestSafe(1015, i, Integer.valueOf(i2), str);
            }
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void setPreferredCell(int i, Map map, String str, String str2) {
        byte b;
        if (ModemProvider.IS_MTK) {
            super.setPreferredCell(i, map, str, str2);
            return;
        }
        PreferredCellListS preferredCellListS = new PreferredCellListS();
        if (map == null || map.isEmpty()) {
            return;
        }
        log("setPreferredCell" + map.toString() + ", plmn=" + str);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            initHstPreferredCellInfo(preferredCellListS, (String) ((Map.Entry) it.next()).getKey(), str, i);
        }
        byte b2 = preferredCellListS.lte_cell_list_len;
        if (b2 > 64 || b2 < 0 || (b = preferredCellListS.nr_cell_list_len) > 64 || b < 0) {
            log("setPreferredCell data is null or length is illegal");
            return;
        }
        log("setPreferredCell slotId=" + i + " , triggerReason=" + str2);
        if (ModemProvider.IS_XRING) {
            PreferredCellListSInfo preferredCellListSInfoConvertToPreferredCellListSInfo = convertToPreferredCellListSInfo(preferredCellListS);
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            hyperRadioManager.setPreferredCellList(i, preferredCellListSInfoConvertToPreferredCellListSInfo, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.9
                public void onResponse(int i2) {
                    NetworkRecoveryStrategyImpl.log("XRING: setPreferredCellList done with errorCode " + i2);
                }
            });
            return;
        }
        if (ModemProvider.IS_QCOM) {
            QmsMsgTunnelControllerBase.sendQmsRequestSafe(1016, i, preferredCellListS, str2);
        }
    }

    public PreferredCellListSInfo convertToPreferredCellListSInfo(PreferredCellListS preferredCellListS) {
        PreferredCellListSInfo preferredCellListSInfo = new PreferredCellListSInfo();
        preferredCellListSInfo.expiry_timer = preferredCellListS.expiry_timer;
        preferredCellListSInfo.lte_cell_list_len = preferredCellListS.lte_cell_list_len;
        preferredCellListSInfo.nr_cell_list_len = preferredCellListS.nr_cell_list_len;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = preferredCellListS.lte_cell_list;
        int size = arrayList3.size();
        int i = 0;
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList3.get(i2);
            i2++;
            arrayList.add(convertToPreferredCellInfoSInfo((PreferredCellInfoS) obj));
        }
        ArrayList arrayList4 = preferredCellListS.nr_cell_list;
        int size2 = arrayList4.size();
        while (i < size2) {
            Object obj2 = arrayList4.get(i);
            i++;
            arrayList2.add(convertToPreferredCellInfoSInfo((PreferredCellInfoS) obj2));
        }
        preferredCellListSInfo.lte_cell_list = arrayList;
        preferredCellListSInfo.nr_cell_list = arrayList2;
        return preferredCellListSInfo;
    }

    public PreferredCellInfoSInfo convertToPreferredCellInfoSInfo(PreferredCellInfoS preferredCellInfoS) {
        PreferredCellInfoSInfo preferredCellInfoSInfo = new PreferredCellInfoSInfo();
        CellInfoS cellInfoS = preferredCellInfoS.cell_info;
        preferredCellInfoSInfo.cell_info = createCellInfoSInfo(cellInfoS.plmn, cellInfoS);
        preferredCellInfoSInfo.cause_bitmask = preferredCellInfoS.cause_bitmask;
        return preferredCellInfoSInfo;
    }

    public CellInfoSInfo createCellInfoSInfo(PlmnIdS plmnIdS, CellInfoS cellInfoS) {
        return new CellInfoSInfo(new PlmnIdSInfo((char) plmnIdS.mcc, (char) plmnIdS.mnc, plmnIdS.mncIncludesPcsDigit), cellInfoS.cgi, cellInfoS.band, cellInfoS.freq, (char) cellInfoS.pci);
    }

    private void initHstPreferredCellInfo(PreferredCellListS preferredCellListS, String str, String str2, int i) {
        int size;
        int size2;
        HstFrequencyData hstFrequencyData = getHstFrequencyData(str, str2);
        if (hstFrequencyData == null) {
            log("NO Hst Line match: " + str + str2);
            return;
        }
        List list = hstFrequencyData.lteData;
        List list2 = hstFrequencyData.nrData;
        preferredCellListS.expiry_timer = 28800;
        if (list != null && (size2 = preferredCellListS.lte_cell_list_len + list.size()) <= 64 && size2 <= 127) {
            new ArrayList();
            ArrayList preferredCellInfo = setPreferredCellInfo(list, i);
            preferredCellListS.lte_cell_list_len = (byte) size2;
            preferredCellListS.lte_cell_list.addAll(preferredCellInfo);
        }
        if (list2 != null && (size = preferredCellListS.nr_cell_list_len + list2.size()) <= 64 && size <= 127) {
            new ArrayList();
            ArrayList preferredCellInfo2 = setPreferredCellInfo(list2, i);
            preferredCellListS.nr_cell_list_len = (byte) size;
            preferredCellListS.nr_cell_list.addAll(preferredCellInfo2);
        }
        log("setPreferredCellInfo res=" + preferredCellListS + ", hstLine=" + str + str2);
    }

    private ArrayList setPreferredCellInfo(List list, int i) {
        short s;
        short s2;
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            String networkNumeric = MiuiPhoneUtils.getNetworkNumeric(i);
            if (networkNumeric == null || networkNumeric.length() < 5) {
                s = 0;
                s2 = 0;
            } else {
                try {
                    s2 = Short.parseShort(networkNumeric.substring(0, 3));
                    try {
                        s = Short.parseShort(networkNumeric.substring(3));
                    } catch (NumberFormatException unused) {
                        log("setPreferredCellInfo parse network plmn error");
                        s = 0;
                    }
                } catch (NumberFormatException unused2) {
                    s2 = 0;
                }
            }
            Iterator it = list.iterator();
            while (it.hasNext()) {
                List list2 = (List) it.next();
                if (list2 == null || list2.size() != 4) {
                    log("Entry has incorrectly formatted parameters");
                } else {
                    if (arrayList.size() >= 64) {
                        log("Cell num has reached the upper limit");
                        break;
                    }
                    try {
                        PreferredCellInfoS preferredCellInfoS = new PreferredCellInfoS();
                        CellInfoS cellInfoS = new CellInfoS();
                        PlmnIdS plmnIdS = new PlmnIdS();
                        if (s2 > 0) {
                            plmnIdS.mcc = s2;
                            plmnIdS.mnc = s;
                        } else {
                            plmnIdS.mcc = Short.parseShort((String) list2.get(0));
                            plmnIdS.mnc = Short.parseShort(((String) list2.get(1)).replaceFirst("^0+(?!$)", ""));
                        }
                        cellInfoS.band = Integer.parseInt((String) list2.get(2));
                        cellInfoS.freq = Integer.parseInt((String) list2.get(3));
                        cellInfoS.plmn = plmnIdS;
                        preferredCellInfoS.cell_info = cellInfoS;
                        preferredCellInfoS.cause_bitmask = 1;
                        arrayList.add(preferredCellInfoS);
                    } catch (NumberFormatException unused3) {
                        log("Error parsing preferred cell data (invalid number format): " + list2);
                    } catch (Exception e) {
                        log("Unexpected error processing entry: " + list2 + ", error: " + e.getMessage());
                    }
                }
            }
        }
        return arrayList;
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void setPreferredCellForSNSV4(int i, Map map, String str, int i2, Map map2, String str2) {
        if (!ModemProvider.IS_QCOM) {
            super.setPreferredCellForSNSV4(i, map, str, i2, map2, str2);
            return;
        }
        if (map2 == null || map2.isEmpty()) {
            log("setPreferredCellForSNSV4 param is invaild!");
            return;
        }
        PreferredCellListS preferredCellListS = new PreferredCellListS();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            initHstPreferredCellInfo(preferredCellListS, (String) ((Map.Entry) it.next()).getKey(), str, i);
        }
        PreferredCellListS preferredCellListS2 = new PreferredCellListS();
        if (i2 == 1) {
            preferredCellListS2 = processCellInfo(map2, "nextmcc", "nextmnc", "nextcid", "nextband", "nextarfcn", "nextpci", "nextrat", processCellInfo(map2, "mcc", "mnc", "cid", "band", "arfcn", "pci", "rat", preferredCellListS));
        } else if (i2 == 2) {
            preferredCellListS2 = processCellInfo(map2, "nextmcc", "nextmnc", "nextcid", "nextband", "nextarfcn", "nextpci", "nextrat", preferredCellListS);
        }
        preferredCellListS2.expiry_timer = 60;
        log("setPreferredCellForSNSV4 " + preferredCellListS2);
        QmsMsgTunnelControllerBase.sendQmsRequestSafe(1016, i, preferredCellListS2, str2);
    }

    private PreferredCellListS processCellInfo(Map map, String str, String str2, String str3, String str4, String str5, String str6, String str7, PreferredCellListS preferredCellListS) {
        PreferredCellInfoS preferredCellInfoS = new PreferredCellInfoS();
        CellInfoS cellInfoS = new CellInfoS();
        PlmnIdS plmnIdS = new PlmnIdS();
        try {
            plmnIdS.mcc = Short.parseShort((String) map.get(str));
            plmnIdS.mnc = Short.parseShort((String) map.get(str2));
            cellInfoS.cgi = Long.parseLong((String) map.get(str3));
            cellInfoS.band = Integer.parseInt((String) map.get(str4));
            cellInfoS.freq = Integer.parseInt((String) map.get(str5));
            cellInfoS.pci = Short.parseShort((String) map.get(str6));
            cellInfoS.plmn = plmnIdS;
            preferredCellInfoS.cell_info = cellInfoS;
            preferredCellInfoS.cause_bitmask = 1;
            String str8 = (String) map.get(str7);
            if ("0".equals(str8)) {
                preferredCellListS.lte_cell_list.add(preferredCellInfoS);
                preferredCellListS.lte_cell_list_len = (byte) (preferredCellListS.lte_cell_list_len + 1);
                return preferredCellListS;
            }
            if ("1".equals(str8)) {
                preferredCellListS.nr_cell_list.add(preferredCellInfoS);
                preferredCellListS.nr_cell_list_len = (byte) (preferredCellListS.nr_cell_list_len + 1);
            }
            return preferredCellListS;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return preferredCellListS;
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void enableCellBarringFeature(int i, boolean z, String str) {
        if (ModemProvider.IS_MTK) {
            super.enableCellBarringFeature(i, z, str);
            return;
        }
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("enableCellBarringFeature slotId=" + i + ", enabled=" + z + ", triggerReason=" + str);
            if (ModemProvider.IS_XRING) {
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
                Handler handler = this.mHandler;
                Objects.requireNonNull(handler);
                hyperRadioManager.enableCellBarringFeature(i, z, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.10
                    public void onResponse(int i2) {
                        NetworkRecoveryStrategyImpl.log("XRING: enableCellBarringFeature done with errorCode " + i2);
                    }
                });
                return;
            }
            if (ModemProvider.IS_QCOM) {
                QmsMsgTunnelControllerBase.sendQmsRequestSafe(1017, i, Boolean.valueOf(z), str);
            }
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public synchronized void setLteNrCellBarringInfo(int i, List list, List list2, String str) {
        if (ModemProvider.IS_MTK) {
            super.setLteNrCellBarringInfo(i, list, list2, str);
            return;
        }
        if (MiuiPhoneUtils.isValidatePhoneId(i) && list != null && list.size() <= 16 && list2 != null && list2.size() <= 16) {
            log("setLteNrCellBarringInfo slotId=" + i + ", lteSize=" + list.size() + ", nrSize=" + list2.size() + ", triggerReason=" + str);
            CellBarringInfoS cellBarringInfoS = new CellBarringInfoS(4320, (byte) list.size(), (ArrayList) list, (byte) list2.size(), (ArrayList) list2);
            ArrayList arrayList = new ArrayList();
            arrayList.add(cellBarringInfoS);
            if (ModemProvider.IS_XRING) {
                CellBarringInfoSInfo cellBarringInfoSInfoConvertToCellBarringInfoSInfo = convertToCellBarringInfoSInfo(cellBarringInfoS);
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
                Handler handler = this.mHandler;
                Objects.requireNonNull(handler);
                hyperRadioManager.setCellBarringInfo(i, cellBarringInfoSInfoConvertToCellBarringInfoSInfo, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse(this) { // from class: com.android.phone.NetworkRecoveryStrategyImpl.11
                    public void onResponse(int i2) {
                        NetworkRecoveryStrategyImpl.log("XRING: setCellBarringInfo done with errorCode" + i2);
                    }
                });
            } else if (ModemProvider.IS_QCOM) {
                QmsMsgTunnelControllerBase.sendQmsRequestSafe(1018, i, arrayList, str);
            }
        }
    }

    public CellBarringInfoSInfo convertToCellBarringInfoSInfo(CellBarringInfoS cellBarringInfoS) {
        CellBarringInfoSInfo cellBarringInfoSInfo = new CellBarringInfoSInfo();
        cellBarringInfoSInfo.expiry_timer = cellBarringInfoS.expiry_timer;
        cellBarringInfoSInfo.lte_cell_list_len = cellBarringInfoS.lte_cell_list_len;
        cellBarringInfoSInfo.nr_cell_list_len = cellBarringInfoS.nr_cell_list_len;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = cellBarringInfoS.lte_cell_list;
        int size = arrayList3.size();
        int i = 0;
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList3.get(i2);
            i2++;
            arrayList.add(convertToLteSInfo((LteDubiousCellInfoS) obj));
        }
        ArrayList arrayList4 = cellBarringInfoS.nr_cell_list;
        int size2 = arrayList4.size();
        while (i < size2) {
            Object obj2 = arrayList4.get(i);
            i++;
            arrayList2.add(convertToNrSInfo((NrDubiousCellInfoS) obj2));
        }
        cellBarringInfoSInfo.lte_cell_list = arrayList;
        cellBarringInfoSInfo.nr_cell_list = arrayList2;
        return cellBarringInfoSInfo;
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public synchronized void setCellListInfo(List list, String str) {
        if (!ModemProvider.IS_QCOM) {
            super.setCellListInfo(list, str);
            return;
        }
        if (list == null) {
            return;
        }
        log("setCellListInfo cellInfo=" + list + ", triggerReason=" + str);
        QmsMsgTunnelControllerBase.sendQmsRequestSafe(1021, 0, list, str);
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public synchronized void setCellListInfo(int i, List list, long j) {
        if (!MiuiPhoneUtils.isHighPriorityCellSelectionSupported()) {
            super.setCellListInfo(i, list, j);
        } else {
            if (list == null) {
                return;
            }
            log("setHighPriorityCell");
            HighPriorityCellSelection.getInstance().setHighPriorityCell(i, list, j);
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public synchronized void setCellListInfoV2(Map map, int i, long j, String str) {
        if (!ModemProvider.IS_QCOM) {
            super.setCellListInfoV2(map, i, j, str);
            return;
        }
        if (map == null) {
            return;
        }
        log("setCellListInfoV2 cellInfo=" + map + ", type=" + i + ", triggerReason=" + str + ", mode= hst");
        SetCellListInfoV2S setCellListInfoV2S = new SetCellListInfoV2S();
        setCellListInfoV2S.mitigation_type = 1;
        setCellListInfoV2S.reset_flag = true;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        try {
            if (i == 1) {
                if (!buildCellListInfoV2S(map, "arfcn", "band", "cid", "mcc", "mnc", "pci", "rat", j, arrayList, arrayList2)) {
                    return;
                }
                if (!buildCellListInfoV2S(map, "nextarfcn", "nextband", "nextcid", "nextmcc", "nextmnc", "nextpci", "nextrat", j, arrayList, arrayList2)) {
                    return;
                }
            } else {
                if (i != 2) {
                    setCellListInfoV2S = setCellListInfoV2S;
                } else if (!buildCellListInfoV2S(map, "nextarfcn", "nextband", "nextcid", "nextmcc", "nextmnc", "nextpci", "nextrat", j, arrayList, arrayList2)) {
                    return;
                }
                setCellListInfoV2S.lte_cell_list_len = (byte) arrayList.size();
                byte size = (byte) arrayList2.size();
                setCellListInfoV2S.nr_cell_list_len = size;
                setCellListInfoV2S.lte_cell_list = arrayList;
                setCellListInfoV2S.nr_cell_list = arrayList2;
                if (setCellListInfoV2S.lte_cell_list_len != 0 && size == 0) {
                    log("setCellListInfoV2 no cell built");
                    return;
                }
                ArrayList arrayList3 = new ArrayList();
                arrayList3.add(setCellListInfoV2S);
                log("setCellListInfoV2");
                QmsMsgTunnelControllerBase.sendQmsRequestSafe(1022, 0, arrayList3, str);
            }
            setCellListInfoV2S.lte_cell_list_len = (byte) arrayList.size();
            byte size2 = (byte) arrayList2.size();
            setCellListInfoV2S.nr_cell_list_len = size2;
            setCellListInfoV2S.lte_cell_list = arrayList;
            setCellListInfoV2S.nr_cell_list = arrayList2;
            if (setCellListInfoV2S.lte_cell_list_len != 0) {
            }
            ArrayList arrayList4 = new ArrayList();
            arrayList4.add(setCellListInfoV2S);
            log("setCellListInfoV2");
            QmsMsgTunnelControllerBase.sendQmsRequestSafe(1022, 0, arrayList4, str);
        } catch (NumberFormatException e) {
            log("setCellListInfoV2 parse error: " + e.getMessage());
        }
    }

    private boolean isBuildCellListInfoParamsValid(String str, String str2, String str3, String str4) {
        if (str != null && str2 != null && str3 != null && str4 != null) {
            int length = str.length() + str2.length();
            if (length < 5 || length > 6) {
                log("buildCellListInfoV2S invalid plmn length: " + length);
            } else {
                int i = Integer.parseInt(str3);
                int i2 = Integer.parseInt(str4);
                if (i > 0 && i2 >= 0) {
                    return true;
                }
                log("buildCellListInfoV2S invalid freq=" + i + " or pci=" + i2);
                return false;
            }
        }
        return false;
    }

    private boolean buildCellListInfoV2S(Map map, String str, String str2, String str3, String str4, String str5, String str6, String str7, long j, ArrayList arrayList, ArrayList arrayList2) {
        String str8 = (String) map.get(str4);
        String str9 = (String) map.get(str5);
        String str10 = (String) map.get(str);
        String str11 = (String) map.get(str6);
        String str12 = (String) map.get(str7);
        if (!isBuildCellListInfoParamsValid(str8, str9, str10, str11)) {
            return false;
        }
        if (str12 == null || (!"0".equals(str12) && !"1".equals(str12))) {
            log("buildCellListInfoV2S invalid rat=" + str12);
            return false;
        }
        SetCellListInfoTypeV2S setCellListInfoTypeV2SCreateCellListInfoV2S = createCellListInfoV2S(map, str8, str9, str2, str3, Integer.parseInt(str10), Integer.parseInt(str11), j);
        if ("0".equals(str12)) {
            arrayList.add(setCellListInfoTypeV2SCreateCellListInfoV2S);
            return true;
        }
        if (!"1".equals(str12)) {
            return true;
        }
        arrayList2.add(setCellListInfoTypeV2SCreateCellListInfoV2S);
        return true;
    }

    private SetCellListInfoTypeV2S createCellListInfoV2S(Map map, String str, String str2, String str3, String str4, int i, int i2, long j) {
        SetCellListInfoTypeV2S setCellListInfoTypeV2S = new SetCellListInfoTypeV2S();
        setCellListInfoTypeV2S.neighbor_cluster_id = -1;
        setCellListInfoTypeV2S.anchor_cell = 0;
        setCellListInfoTypeV2S.cause_bitmask = 2;
        int i3 = (int) j;
        setCellListInfoTypeV2S.expiry_timer = i3;
        UniqueCellInfoS uniqueCellInfoS = new UniqueCellInfoS();
        PlmnIdS plmnIdS = new PlmnIdS();
        plmnIdS.mcc = Short.parseShort(str);
        plmnIdS.mnc = Short.parseShort(str2);
        plmnIdS.mncIncludesPcsDigit = str2.length() == 3;
        uniqueCellInfoS.plmn = plmnIdS;
        String str5 = (String) map.get(str4);
        uniqueCellInfoS.cell_identity = str5 != null ? Long.parseLong(str5) : 0L;
        String str6 = (String) map.get(str3);
        uniqueCellInfoS.band = str6 != null ? Integer.parseInt(str6) : 0;
        uniqueCellInfoS.freq = i;
        uniqueCellInfoS.pci = (short) i2;
        setCellListInfoTypeV2S.cell_info = uniqueCellInfoS;
        CellPropertyCategoryTypeV2S cellPropertyCategoryTypeV2S = new CellPropertyCategoryTypeV2S();
        cellPropertyCategoryTypeV2S.common_valid = false;
        cellPropertyCategoryTypeV2S.hdr_valid = false;
        cellPropertyCategoryTypeV2S.rs_valid = false;
        cellPropertyCategoryTypeV2S.hst_valid = true;
        cellPropertyCategoryTypeV2S.hdr_rat_valid = false;
        CellPropertyCategoryCommonS cellPropertyCategoryCommonS = new CellPropertyCategoryCommonS();
        cellPropertyCategoryTypeV2S.common = cellPropertyCategoryCommonS;
        cellPropertyCategoryCommonS.priority = -1;
        cellPropertyCategoryCommonS.location_valid = false;
        cellPropertyCategoryCommonS.location = new LocationS();
        LocationS locationS = cellPropertyCategoryTypeV2S.common.location;
        locationS.longitude = 0;
        locationS.latitude = 0;
        CellPropertyCategoryHdrS cellPropertyCategoryHdrS = new CellPropertyCategoryHdrS();
        cellPropertyCategoryTypeV2S.hdr = cellPropertyCategoryHdrS;
        cellPropertyCategoryHdrS.service_type = -1;
        cellPropertyCategoryHdrS.load = -1;
        cellPropertyCategoryHdrS.expiry_timer = i3;
        CellPropertyCategoryRsS cellPropertyCategoryRsS = new CellPropertyCategoryRsS();
        cellPropertyCategoryTypeV2S.rs = cellPropertyCategoryRsS;
        cellPropertyCategoryRsS.shared_plmn_len = 0;
        cellPropertyCategoryRsS.shared_plmn = new ArrayList();
        CellPropertyCategoryHstTypeS cellPropertyCategoryHstTypeS = new CellPropertyCategoryHstTypeS();
        cellPropertyCategoryTypeV2S.hst = cellPropertyCategoryHstTypeS;
        cellPropertyCategoryHstTypeS.priority = 1L;
        cellPropertyCategoryHstTypeS.expiry_timer = i3;
        CellPropertyCategoryHdrRatTypeS cellPropertyCategoryHdrRatTypeS = new CellPropertyCategoryHdrRatTypeS();
        cellPropertyCategoryTypeV2S.hdr_rat = cellPropertyCategoryHdrRatTypeS;
        cellPropertyCategoryHdrRatTypeS.sub_id_mask = (byte) 0;
        cellPropertyCategoryHdrRatTypeS.expiry_timer = 0;
        setCellListInfoTypeV2S.property = cellPropertyCategoryTypeV2S;
        return setCellListInfoTypeV2S;
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void setSubwayState(int i, String str) {
        if (!ModemProvider.IS_QCOM) {
            super.setSubwayState(i, str);
            return;
        }
        log("setSubwayState subwayState=" + i + ",triggerReason=" + str);
        QmsMsgTunnelControllerBase.sendQmsRequestSafe(1020, 0, Integer.valueOf(i), str);
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void enableWeakOpt(int i, boolean z) {
        if (!ModemProvider.IS_MTK) {
            super.enableWeakOpt(i, z);
        } else if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            if (z) {
                sendAtCmd(i, MtkAtCmd.ENABLE_WEAK_OPT, null);
            } else {
                sendAtCmd(i, MtkAtCmd.DISABLE_WEAK_OPT, null);
            }
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void enableCongestionOpt(int i, boolean z) {
        if (!ModemProvider.IS_MTK) {
            super.enableCongestionOpt(i, z);
            return;
        }
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            if (z) {
                sendAtCmd(i, MtkAtCmd.ENABLE_CONGESTION_1, null);
                sendAtCmd(i, MtkAtCmd.ENABLE_CONGESTION_2, null);
            } else {
                sendAtCmd(i, MtkAtCmd.DISABLE_CONGESTION_1, null);
                sendAtCmd(i, MtkAtCmd.DISABLE_CONGESTION_2, null);
            }
        }
    }

    @Override // com.android.phone.NetworkRecoveryStrategyBase
    public void enableHsrOpt(int i, boolean z) {
        if (!ModemProvider.IS_MTK) {
            super.enableHsrOpt(i, z);
        } else if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            if (z) {
                sendAtCmd(i, MtkAtCmd.ENABLE_HSR, null);
            } else {
                sendAtCmd(i, MtkAtCmd.DISABLE_HSR, null);
            }
        }
    }

    private void sendAtCmd(int i, MtkAtCmd mtkAtCmd, Message message) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || mtkAtCmd == null) {
            return;
        }
        log("enableHyperEngine [" + i + "], sendAtCmd[" + mtkAtCmd + "]");
        FiveGManagerBase.getInstance().sendAtCommand(i, mtkAtCmd.cmd(), message);
    }

    class LRUCache extends LinkedHashMap {
        private int capacity;

        public LRUCache(int i, float f, boolean z) {
            super(i, f, z);
            this.capacity = i;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry entry) {
            return size() > this.capacity;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("NetworkRecoveryStrategyImpl", str);
    }
}
