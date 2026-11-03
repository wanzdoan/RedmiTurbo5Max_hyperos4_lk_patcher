package com.android.phone.statistics.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellSignalStrengthNr;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.phone.KernelScoreMonitor;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.TelephonyHyperCommSceneMonitor;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.ot.pubsub.a.b;
import com.ot.pubsub.b.a;
import com.xiaomi.telephony.XmTelephonyManagerInner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import xiaomi.hypercomm.HyperCommManager;
import xiaomi.hypercomm.SceneAwareCallback;

/* JADX INFO: loaded from: classes.dex */
public abstract class ModemHydraStatBase {
    protected static volatile ModemHydraStatBase mInstance;
    private static volatile List sLiveStreamingPkgs;
    private static volatile List sShortVideoPackages;
    protected Context mContext;
    protected volatile ResultReceiver mDdsResultReceiver;
    protected StallSceneCB mStallSceneCB;
    protected static final HashMap sLocalLogs = new HashMap();
    public static int mXiaoAiScene = 0;
    public final String SHORT_VIDEO_SCENE = "shortVideo";
    public final String LIVE_STREAMING_SCENE = "liveStreaming";
    public final String OTHER_SCENE = "other";
    public String lastScene = "other";
    private volatile int mLastKernelAvgScore = 0;
    private volatile int mRrcState = 0;
    private volatile int mNddsRrcState = 0;
    private volatile ProtocolMetricsSnapshot mProtocolMetrics = new ProtocolMetricsSnapshot(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    protected final ConcurrentLinkedQueue mPendingStallQueue = new ConcurrentLinkedQueue();
    protected volatile long mLastGameStallReportTime = 0;
    protected volatile long mLastStreamStallReportTime = 0;
    protected volatile int mLastVideoQuality = -1;
    protected Handler mWorkHandler = null;
    protected volatile int[] sModemReasonResult = null;
    protected final ResultReceiver mResultReceiver = new AnonymousClass3(null);

    public interface SceneStateCallBack {
        void onSceneStateChanged(int i, boolean z, String str);
    }

    protected void addCityProvinceToMap(LinkedHashMap linkedHashMap) {
    }

    public abstract void checkAndReport(List list, String str);

    public long getCellPingPongCounts() {
        return 0L;
    }

    public abstract void getDataStallInfoFromModem(int i, HashMap map);

    public void getModemSimAbnormalInfo(int i, ArrayList arrayList) {
    }

    protected abstract Handler getStatHandler();

    public abstract void modemHydraProcess(int i, Object obj);

    public abstract void modemHydraSendDelayMessage(int i, Object obj, int i2);

    protected abstract void onModemReasonReceived(int i, Bundle bundle, StallRequestContext stallRequestContext);

    public void processTopAppChanged(String str) {
    }

    public abstract String processUeNoSaInfo(int i);

    public void registerSceneStateCallBack(SceneStateCallBack sceneStateCallBack) {
    }

    public abstract void reportModemHydraStat();

    public void unregisterSceneStateCallBack(SceneStateCallBack sceneStateCallBack) {
    }

    public abstract void updateModemHydraStat(String str, boolean z, int i);

    public abstract void updatePingpongCellStat(int i);

    public void updateScreenState(boolean z) {
    }

    public class StallRequestContext {
        final HashMap dataStallMap;
        final String packageName;
        final int phoneId;
        final int stallType;

        StallRequestContext(int i, String str, int i2, HashMap map) {
            this.phoneId = i;
            this.packageName = str;
            this.stallType = i2;
            this.dataStallMap = map;
        }
    }

    public class ProtocolMetricsSnapshot {
        final int bw;
        final int dlLoss;
        final int dlRetrans;
        final long dnsLat;
        final int dnsSuccess;
        final int oooB;
        final int oooN;
        final int rtt;
        final int udp;
        final int ulRetrans;

        ProtocolMetricsSnapshot(int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, int i8, int i9) {
            this.rtt = i;
            this.bw = i2;
            this.dlLoss = i3;
            this.dlRetrans = i4;
            this.ulRetrans = i5;
            this.dnsSuccess = i6;
            this.dnsLat = j;
            this.udp = i7;
            this.oooN = i8;
            this.oooB = i9;
        }
    }

    public static List getLiveStreamingPkgs() {
        if (sLiveStreamingPkgs == null) {
            synchronized (ModemHydraStatBase.class) {
                try {
                    if (sLiveStreamingPkgs == null) {
                        sLiveStreamingPkgs = MiuiPhoneUtils.getFeatureConfig("config_live_streaming_packages");
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sLiveStreamingPkgs;
    }

    public static List getShortVideoPackages() {
        if (sShortVideoPackages == null) {
            synchronized (ModemHydraStatBase.class) {
                try {
                    if (sShortVideoPackages == null) {
                        sShortVideoPackages = MiuiPhoneUtils.getFeatureConfig("config_shortvideo_packages");
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sShortVideoPackages;
    }

    public static ModemHydraStatBase getInstance() {
        return mInstance;
    }

    protected void initRegListener() {
        if (MiuiPhoneUtils.isSupportDfxDataStallStat()) {
            registerStallListener();
            registerHyperCommSceneMonitor();
            registerKernelScoreListener();
        }
    }

    class StallSceneCB extends SceneAwareCallback implements SceneAwareCallback.GameStallListener, SceneAwareCallback.NetworkQoeListener {
        public StallSceneCB(Executor executor) {
            super(executor);
        }

        public void onGameStallChanged(boolean z, Bundle bundle) {
            Utils.logD("ModemHydraStatBase", "onGameStallChanged " + z + " " + bundle);
            ModemHydraStatBase.this.handleGameStallChanged(z, bundle);
        }

        public void onNetworkQoeChanged(int i, int i2, int i3, int i4, int i5) {
            Utils.logD("ModemHydraStatBase", "onNetworkQoeChanged slotId=" + i + " pci=" + i2 + " appType=" + i3 + " reason=" + i4 + " networkScore=" + i5);
            ModemHydraStatBase.this.handleStreamingStallChanged(i5);
        }
    }

    protected void registerStallListener() {
        Handler statHandler = getStatHandler();
        if (statHandler == null) {
            Utils.logW("ModemHydraStatBase", "registerStallListener: handler is null, skip");
            return;
        }
        this.mDdsResultReceiver = new ResultReceiver(statHandler) { // from class: com.android.phone.statistics.utils.ModemHydraStatBase.1
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                int[] intArray;
                if (i != 0 || bundle == null) {
                    return;
                }
                try {
                    if (bundle.containsKey("dds_assistant_info") && (intArray = bundle.getIntArray("dds_assistant_info")) != null && intArray.length >= 17) {
                        ModemHydraStatBase.this.mRrcState = intArray[15];
                        ModemHydraStatBase.this.mNddsRrcState = intArray[16];
                        Utils.logD("ModemHydraStatBase", "mDdsResultReceiver: rrcState=" + ModemHydraStatBase.this.mRrcState + ", nddsRrcState=" + ModemHydraStatBase.this.mNddsRrcState);
                    }
                } catch (Exception e) {
                    Utils.logE("ModemHydraStatBase", "mDdsResultReceiver onReceiveResult exception: " + e);
                }
            }
        };
        try {
            if (this.mStallSceneCB != null) {
                Utils.logW("ModemHydraStatBase", "registerStallListener: already registered, skip");
            } else {
                this.mStallSceneCB = new StallSceneCB(new HandlerExecutor(statHandler));
                HyperCommManager.getDefault().registerSceneAwareCallback(this.mStallSceneCB);
            }
        } catch (Exception e) {
            Utils.logW("ModemHydraStatBase", "registerStallListener failed: " + e);
        }
    }

    protected void registerHyperCommSceneMonitor() {
        Handler statHandler = getStatHandler();
        if (statHandler == null) {
            Utils.logW("ModemHydraStatBase", "registerHyperCommSceneMonitor: handler is null, skip");
        } else {
            TelephonyHyperCommSceneMonitor.getInstance().register(statHandler);
        }
    }

    protected void handleGameStallChanged(boolean z, Bundle bundle) {
        if (z) {
            try {
                if (!MiuiPhoneUtils.isMobileDataConnected()) {
                    Utils.logD("ModemHydraStatBase", "handleGameStallChanged: not mobile data, skip");
                    return;
                }
                synchronized (this) {
                    try {
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        if (jCurrentTimeMillis - this.mLastGameStallReportTime < 30000) {
                            Utils.logD("ModemHydraStatBase", "handleGameStallChanged throttled, skip (interval=" + (jCurrentTimeMillis - this.mLastGameStallReportTime) + "ms)");
                            return;
                        }
                        this.mLastGameStallReportTime = jCurrentTimeMillis;
                        Utils.logD("ModemHydraStatBase", "Game stall detected, fetching modem data");
                        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
                        XmTelephonyManagerInner.getDefault().getDdsAssistantInfo(this.mDdsResultReceiver);
                        HashMap map = new HashMap();
                        getDataStallInfoFromModem(activeDataPhoneId, map);
                        notifyModemReportGameStallInfo(activeDataPhoneId, MiuiPhoneUtils.getTopAppName(), map);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            } catch (Exception e) {
                Utils.logW("ModemHydraStatBase", "handleGameStallChanged failed: " + e);
            }
        }
    }

    protected void handleStreamingStallChanged(int i) {
        if (i < 0 || i >= 80) {
            return;
        }
        try {
            if (!MiuiPhoneUtils.isMobileDataConnected()) {
                Utils.logD("ModemHydraStatBase", "handleStreamingStallChanged: not mobile data, skip");
                return;
            }
            synchronized (this) {
                try {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    if (jCurrentTimeMillis - this.mLastStreamStallReportTime < 30000) {
                        Utils.logD("ModemHydraStatBase", "handleStreamingStallChanged throttled, skip (interval=" + (jCurrentTimeMillis - this.mLastStreamStallReportTime) + "ms)");
                        return;
                    }
                    this.mLastStreamStallReportTime = jCurrentTimeMillis;
                    Utils.logD("ModemHydraStatBase", "Streaming stall detected, networkScore=" + i);
                    int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
                    XmTelephonyManagerInner.getDefault().getDdsAssistantInfo(this.mDdsResultReceiver);
                    HashMap map = new HashMap();
                    getDataStallInfoFromModem(activeDataPhoneId, map);
                    notifyModemReportStreamStallInfo(activeDataPhoneId, MiuiPhoneUtils.getTopAppName(), map);
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Exception e) {
            Utils.logW("ModemHydraStatBase", "handleStreamingStallChanged failed: " + e);
        }
    }

    protected void notifyModemReportGameStallInfo(int i, String str, HashMap map) {
        Utils.logD("ModemHydraStatBase", "notifyModemReportGameStallInfo");
        this.mPendingStallQueue.offer(new StallRequestContext(i, str, 0, map));
        XmTelephonyManagerInner.getDefault().getModemReason(this.mResultReceiver);
    }

    protected void notifyModemReportStreamStallInfo(int i, String str, HashMap map) {
        Utils.logD("ModemHydraStatBase", "notifyModemReportStreamStallInfo");
        this.mPendingStallQueue.offer(new StallRequestContext(i, str, 1, map));
        XmTelephonyManagerInner.getDefault().getModemReason(this.mResultReceiver);
    }

    protected void registerKernelScoreListener() {
        KernelScoreMonitor.getInstance().init();
        KernelScoreMonitor.getInstance().setCallback(new KernelScoreMonitor.KernelScoreCallback() { // from class: com.android.phone.statistics.utils.ModemHydraStatBase.2
            @Override // com.android.phone.KernelScoreMonitor.KernelScoreCallback
            public void onKernelScoreChanged(Intent intent) {
                if (intent == null) {
                    return;
                }
                ModemHydraStatBase.this.handleKernelScoreChanged(intent.getIntExtra("avgScore", 0));
            }

            @Override // com.android.phone.KernelScoreMonitor.KernelScoreCallback
            public void onKernelProtocolMetricsChanged(Intent intent) {
                if (intent == null) {
                    return;
                }
                ModemHydraStatBase.this.handleProtocolMetricsChanged(intent);
            }
        });
    }

    public void handleKernelScoreChanged(int i) {
        Utils.logD("ModemHydraStatBase", "handleKernelScoreChanged avgScore: " + i);
        this.mLastKernelAvgScore = i;
    }

    protected void handleProtocolMetricsChanged(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        this.mProtocolMetrics = new ProtocolMetricsSnapshot(extras.getInt("kernel_avg_tcp_rtt_ms", 0), extras.getInt("kernel_dl_bandwidth_kbps", 0), extras.getInt("kernel_dl_tcp_loss_rate", 0), extras.getInt("kernel_dl_tcp_retrans_rate", 0), extras.getInt("kernel_ul_tcp_retrans_rate", 0), extras.getInt("kernel_dns_success_rate", 0), extras.getLong("kernel_dns_avg_latency", 0L), extras.getInt("kernel_udp_ratio", 0), extras.getInt("kernel_dl_tcp_ooo_normal_rate", 0), extras.getInt("kernel_dl_tcp_ooo_bad_rate", 0));
        Utils.logD("ModemHydraStatBase", "handleProtocolMetricsChanged rtt=" + this.mProtocolMetrics.rtt + " bw=" + this.mProtocolMetrics.bw);
    }

    /* JADX INFO: renamed from: com.android.phone.statistics.utils.ModemHydraStatBase$3, reason: invalid class name */
    class AnonymousClass3 extends ResultReceiver {
        AnonymousClass3(Handler handler) {
            super(handler);
        }

        @Override // android.os.ResultReceiver
        protected void onReceiveResult(final int i, final Bundle bundle) {
            int size = ModemHydraStatBase.this.mPendingStallQueue.size();
            ModemHydraStatBase modemHydraStatBase = ModemHydraStatBase.this;
            if (size >= 2) {
                Utils.logW("ModemHydraStatBase", "mPendingStallQueue overflow (size=" + modemHydraStatBase.mPendingStallQueue.size() + "), clear and skip");
                ModemHydraStatBase.this.mPendingStallQueue.clear();
                return;
            }
            final StallRequestContext stallRequestContext = (StallRequestContext) modemHydraStatBase.mPendingStallQueue.poll();
            Runnable runnable = new Runnable() { // from class: com.android.phone.statistics.utils.ModemHydraStatBase$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceiveResult$0(i, bundle, stallRequestContext);
                }
            };
            Handler handler = ModemHydraStatBase.this.mWorkHandler;
            if (handler != null) {
                handler.post(runnable);
            } else {
                runnable.run();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceiveResult$0(int i, Bundle bundle, StallRequestContext stallRequestContext) {
            int[] intArray;
            Utils.logD("ModemHydraStatBase", "result: resultCode=" + i + ", resultData=" + bundle);
            if (i != 0 || (intArray = bundle.getIntArray("modem_reason_info")) == null) {
                return;
            }
            ModemHydraStatBase.this.sModemReasonResult = intArray;
            Utils.logD("ModemHydraStatBase", "ModemReason received: " + Arrays.toString(intArray));
            if (MiuiPhoneUtils.isSupportDfxDataStallStat()) {
                ModemHydraStatBase.this.onModemReasonReceived(i, bundle, stallRequestContext);
            }
        }
    }

    protected int[] getModemReasonArray() {
        int[] iArr = this.sModemReasonResult;
        if (iArr == null || iArr.length < 24) {
            return null;
        }
        int[] iArr2 = new int[10];
        for (int i = 1; i <= 5; i++) {
            int i2 = (i - 1) * 2;
            iArr2[i2] = iArr[i + 12];
            iArr2[i2 + 1] = iArr[i + 18];
        }
        this.sModemReasonResult = null;
        return iArr2;
    }

    public ArrayList getRachFailRateInfo() {
        return new ArrayList();
    }

    public JSONObject getAftersaleFromHydra() {
        return new JSONObject();
    }

    public void addStallExtraParams(LinkedHashMap linkedHashMap, int i, String str) {
        Phone phone;
        if (linkedHashMap == null || (phone = MiuiPhoneUtils.getPhone(i)) == null) {
            return;
        }
        String simOperatorNumericForPhone = TelephonyManager.from(this.mContext).getSimOperatorNumericForPhone(i);
        if (!TextUtils.isEmpty(simOperatorNumericForPhone)) {
            linkedHashMap.put("Network", simOperatorNumericForPhone);
        }
        ServiceState serviceState = phone.getServiceState();
        if (serviceState != null) {
            linkedHashMap.put("RAT", ServiceState.rilRadioTechnologyToString(serviceState.getRilDataRadioTechnology()));
        }
        addCityProvinceToMap(linkedHashMap);
        linkedHashMap.put("sceneType", getSceneType());
        linkedHashMap.put("networkType", getNetworkTypeName(i));
        if (!TextUtils.isEmpty(str)) {
            linkedHashMap.put("packages", str);
        }
        linkedHashMap.put("apGameStallTime", Long.valueOf(System.currentTimeMillis()));
        linkedHashMap.put("SceneBitmask", Integer.valueOf(TelephonyHyperCommSceneMonitor.getInstance().getActiveSceneBitmask()));
        linkedHashMap.put("dsdaState", Integer.valueOf("1".equals(SystemProperties.get("persist.vendor.radio.dsda_cap", "0")) ? 1 : 0));
        linkedHashMap.put("simCount", Integer.valueOf(MiuiPhoneUtils.getInsertedSimCount()));
        linkedHashMap.put("band", getBandFromPhone(i));
        linkedHashMap.put("rrcState", Integer.valueOf(this.mRrcState));
        linkedHashMap.put("nddsRrcState", Integer.valueOf(this.mNddsRrcState));
        linkedHashMap.put("stallSource", 0);
        linkedHashMap.put("kernelAvgScore", Integer.valueOf(this.mLastKernelAvgScore));
        ProtocolMetricsSnapshot protocolMetricsSnapshot = this.mProtocolMetrics;
        if (protocolMetricsSnapshot == null) {
            return;
        }
        linkedHashMap.put("rtt", Integer.valueOf(protocolMetricsSnapshot.rtt));
        linkedHashMap.put("bw", Integer.valueOf(protocolMetricsSnapshot.bw));
        linkedHashMap.put("dlLoss", Integer.valueOf(protocolMetricsSnapshot.dlLoss));
        linkedHashMap.put("dlRetrans", Integer.valueOf(protocolMetricsSnapshot.dlRetrans));
        linkedHashMap.put("ulRetrans", Integer.valueOf(protocolMetricsSnapshot.ulRetrans));
        linkedHashMap.put("dnsSuccess", Integer.valueOf(protocolMetricsSnapshot.dnsSuccess));
        linkedHashMap.put("dnsLat", Long.valueOf(protocolMetricsSnapshot.dnsLat));
        linkedHashMap.put("udp", Integer.valueOf(protocolMetricsSnapshot.udp));
        linkedHashMap.put("oooN", Integer.valueOf(protocolMetricsSnapshot.oooN));
        linkedHashMap.put("oooB", Integer.valueOf(protocolMetricsSnapshot.oooB));
    }

    public void addRsrpSnrToMap(Map map, int i) {
        SignalStrength signalStrength;
        Utils.logD("ModemHydraStatBase", "addRsrpSnrToMap phoneId: " + i);
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || (signalStrength = phone.getSignalStrength()) == null) {
            return;
        }
        CellSignalStrengthNr nrSignalStrength = MiuiPhoneUtils.getNrSignalStrength(signalStrength);
        if (nrSignalStrength != null) {
            map.put("rsrp", Integer.valueOf(nrSignalStrength.getSsRsrp()));
            map.put("snr", Integer.valueOf(nrSignalStrength.getSsSinr()));
        } else {
            map.put("rsrp", Integer.valueOf(signalStrength.getLteRsrp()));
            map.put("snr", Integer.valueOf(signalStrength.getLteRssnr()));
        }
    }

    protected Map buildStallDfsParams(Map map, List list, String str, int i) {
        String str2;
        long jLongValue;
        Object obj;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(a.d, map.get("kernelAvgScore"));
        linkedHashMap.put("source", map.get("stallSource"));
        long jLongValue2 = 0;
        if (list.size() > 1) {
            Map map2 = (Map) list.get(list.size() - 1);
            Object obj2 = map2.get("reasonBitMask_1");
            str2 = "dnsLat";
            Object obj3 = map2.get("reasonBitMask_2");
            jLongValue = obj2 instanceof Number ? ((Number) obj2).longValue() : 0L;
            if (obj3 instanceof Number) {
                jLongValue2 = ((Number) obj3).longValue();
            }
        } else {
            str2 = "dnsLat";
            jLongValue = 0;
        }
        linkedHashMap.put("reason", Long.valueOf((jLongValue2 << 32) | (jLongValue & 4294967295L)));
        linkedHashMap.put("package_name", str);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("SceneBitmask", map.get("SceneBitmask"));
            jSONObject.put("rsrp", map.get("rsrp"));
            jSONObject.put("band", map.get("band"));
            jSONObject.put("dsdaState", map.get("dsdaState"));
            jSONObject.put("simCount", map.get("simCount"));
            jSONObject.put("rtt", map.get("rtt"));
            jSONObject.put("bw", map.get("bw"));
            jSONObject.put("dlLoss", map.get("dlLoss"));
            jSONObject.put("dlRetrans", map.get("dlRetrans"));
            jSONObject.put("ulRetrans", map.get("ulRetrans"));
            jSONObject.put("dnsSuccess", map.get("dnsSuccess"));
            String str3 = str2;
            jSONObject.put(str3, map.get(str3));
            jSONObject.put("udp", map.get("udp"));
            jSONObject.put("oooN", map.get("oooN"));
            jSONObject.put("oooB", map.get("oooB"));
            if (i == 1) {
                jSONObject.put("videoQuality", map.get("videoQuality"));
            }
            obj = "modem_param";
            try {
                linkedHashMap.put(obj, jSONObject.toString());
                return linkedHashMap;
            } catch (JSONException e) {
                e = e;
                Utils.logE("ModemHydraStatBase", "buildStallDfsParams build modem_param failed", e);
                linkedHashMap.put(obj, "0");
                return linkedHashMap;
            }
        } catch (JSONException e2) {
            e = e2;
            obj = "modem_param";
        }
    }

    public void recordDfsEvent(int i, int i2, Map map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.putAll(map);
        Utils.logD("ModemHydraStatBase", "recordDfsEvent params:" + linkedHashMap);
        MiuiTelephonyStatistic.getInstance().recordDFSEventStatistic(i, linkedHashMap);
    }

    public String getNetworkTypeName(int i) {
        ServiceState serviceState;
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || (serviceState = phone.getServiceState()) == null) {
            return "";
        }
        int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
        if (rilDataRadioTechnology == 1) {
            return "GPRS";
        }
        if (rilDataRadioTechnology == 2) {
            return "EDGE";
        }
        if (rilDataRadioTechnology == 3) {
            return "UMTS";
        }
        if (rilDataRadioTechnology == 14) {
            return "LTE";
        }
        if (rilDataRadioTechnology == 15) {
            return "HSPA+";
        }
        if (rilDataRadioTechnology == 19) {
            return "LTE_CA";
        }
        if (rilDataRadioTechnology != 20) {
            switch (rilDataRadioTechnology) {
                case 9:
                    return "HSDPA";
                case 10:
                    return "HSUPA";
                case 11:
                    return "HSPA";
                default:
                    return "UNKNOWN";
            }
        }
        return "NR";
    }

    protected String getBandFromPhone(int i) {
        ServiceState serviceState;
        NetworkRegistrationInfo networkRegistrationInfo;
        int[] bands;
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || (serviceState = phone.getServiceState()) == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1)) == null) {
            return "NA";
        }
        CellIdentity cellIdentity = networkRegistrationInfo.getCellIdentity();
        if (cellIdentity instanceof CellIdentityNr) {
            int[] bands2 = ((CellIdentityNr) cellIdentity).getBands();
            if (bands2 != null && bands2.length > 0) {
                return "N" + bands2[0];
            }
        } else if ((cellIdentity instanceof CellIdentityLte) && (bands = ((CellIdentityLte) cellIdentity).getBands()) != null && bands.length > 0) {
            return b.a + bands[0];
        }
        return "NA";
    }

    public String getSceneType() {
        return "NA";
    }

    protected List parseStallDataList(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        try {
            Matcher matcher = Pattern.compile("\\{[^{}]*\\}").matcher(str);
            while (matcher.find()) {
                arrayList.add(matcher.group());
            }
        } catch (Exception e) {
            Utils.logW("ModemHydraStatBase", "parseStallDataList exception: " + e);
        }
        if (arrayList.isEmpty()) {
            Utils.logD("ModemHydraStatBase", "parseStallDataList result null");
        } else {
            Utils.logD("ModemHydraStatBase", "parseStallDataList result : " + arrayList);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList;
    }

    protected void mergeStallEntry(String str, Map map) {
        if (str == null || str.isEmpty()) {
            return;
        }
        String strTrim = str.trim();
        if (strTrim.startsWith("{")) {
            strTrim = strTrim.substring(1);
        }
        if (strTrim.endsWith("}")) {
            strTrim = strTrim.substring(0, strTrim.length() - 1);
        }
        for (String str2 : strTrim.split(",")) {
            String strTrim2 = str2.trim();
            int iIndexOf = strTrim2.indexOf(61);
            if (iIndexOf > 0) {
                map.put(strTrim2.substring(0, iIndexOf).trim(), strTrim2.substring(iIndexOf + 1).trim());
            }
        }
    }

    protected static void localLog(String str, String str2) {
        String str3 = "ModemHydraStatBase." + str;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str3)) {
                    map.put(str3, new LocalLog(20));
                }
                ((LocalLog) map.get(str3)).log(str2);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("ModemHydraStatBase dump start------");
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    printWriter.println((String) entry.getKey());
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.println("ModemHydraStatBase dump end------");
        printWriter.flush();
    }
}
