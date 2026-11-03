package com.android.phone.utils;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.RilHook;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.mirilhook.MiRilHookCallback;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class ModemUtils {
    private static int[] array_0;
    private static volatile MiRilHookBase mMiRilHook;
    private static volatile boolean mQcrilHookReady;
    public static byte[] modem_power_disable;
    private static ModemUtils sInstance;
    private final String[] HOOK_METHODS;
    private Context mContext;
    ConcurrentHashMap mMethodMap = new ConcurrentHashMap();
    private Object mQcHook;
    private String mReadItem;
    private RilHook mRilHook;
    private Class qcrilHookCls;
    private static int[] array_1 = {1};
    public static byte[] modem_power_enable = onGetModemMsgBytes(array_1);
    private static Handler mHandler = new AnonymousClass1(Looper.getMainLooper());
    private static File file = new File("/proc/ccci_lp_mem");

    static {
        int[] iArr = {0};
        array_0 = iArr;
        modem_power_disable = onGetModemMsgBytes(iArr);
    }

    public enum featureOptType {
        SWITCH_XIAOMI_MODEM_POWER(0),
        SWITCH_XIAOMI_SCENARIO_OOS(10),
        SET_OOS_TYPE(11),
        GET_OOS_FIXNUM(21),
        SWITCH_XIAOMI_NIGHT_IRAT_PINGPONG(20),
        GET_IRAT_PINGPONG_FIXNUM(21),
        SWITCH_XIAOMI_HO_PINGPONG(30),
        SET_SCREEN_STATUS_FOR_HO_PINGPONG(31),
        GET_HO_PINGPONG_FIXNUM(32),
        SWITCH_XIAOMI_NIGHT_LIMIT_RIL(40),
        GET_NIGHT_LIMIT_RIL_FIXNUM(41),
        SWITCH_XIAOMI_NONDDS_RRC(50),
        GET_NONDDS_FIXNUM(51);

        private final int type;

        featureOptType(int i) {
            this.type = i;
        }

        public int getType() {
            return this.type;
        }
    }

    private ModemUtils(Context context) {
        String[] strArr = {"startModemStats", "closeModemStats", "getLtePuschTx", "getNr5gPuschTx", "getAntPath", "getNr5gBand", "getLteFullSchSfnRatio", "getNr5gFullSchSfnRatio", "getNr5gPdschMcs", "getLtePdschMcs", "isLteRrcActive", "isCardSlotPresent", "getLteRrcActiveTime", "getLteRrcSetupCount", "clearLteRrcStateData", "getLteRlfInfo", "getLteRachFailInfo", "getNr5gRlfInfo", "getNr5gRachFailInfo", "getLtePuschPathLoss", "getNr5gPuschPathLoss", "getLteMeasInfo", "getNr5gMeasInfo", "getLteLayersNumForCrnti", "getNr5gLayersNumForCrnti", "getLteTbBytes", "getNr5gTbBytes", "getNr5gBler", "getLteBler", "getNr5gRbNum", "getLtePdschRb", "getLtePuschMcs", "getNr5gUlMcs", "getLteNasRejectInfo", "getNr5gNasRejectInfo", "getLteFullSchInfo", "getLteBlerAbnormalInfo", "getNr5gFullSchInfo", "getNr5gBlerAbnormalInfo", "onHookPropSetSync", "onHookModemOosBaseTypeSet", "onHookNotifyScreenStatusSync", "onHookModemPowerAPCommand", "onSetEsimStatus", "onGetEsimStatus", "onHookUimPowerReqEx", "onHookEsimPowerReqEx", "onGetEsimGpioStatus", "startTcpMonitorRegister", "stopTcpMonitorRegister", "startTcpKeepAlive", "stopTcpKeepAlive", "onHookEfsOptSync", "onHookEfsWrite", "getLteTxPowerInfo", "getNr5gTxPowerInfo", "getLteNasRejectList", "getNr5gNasRejectList", "getNr5gHighTOosList", "getLteT3410ExpiryList", "getLteT3430ExpiryList", "getNr5gT3510ExpiryList", "getLteRlfOfWeakSignalList", "getLteImsSipMessageList", "getLteImsRtpPacketLossList", "getNr5gPdcpUlDataList", "getLtePdcpUlDataList", "getNr5gPdcpDlDataList", "getLtePdcpDlDataList", "getNr5gFullSchList", "getLteFullSchList", "getNr5gBlerAbnormalList", "getLteBlerAbnormalCList", "getLteBlerAbnormalPList", "getNr5gTxPowerList", "getLteTxPowerList", "getMcsTrmAsdivInfo", "getSwitchConfigAndAntennaInfo", "isModemFeatureSupported", "efsWrite", "efsDelete", "efsSync", "efsCopy", "efsReadBytes", "onHookMbnRefresh", "getLteUlBlerInfo", "getNr5gUlBlerInfo", "getLteMcsInfo", "getNr5gMcsInfo", "onHookNVCMD", "getNsaBandCombInfo", "onHookUimPurgeGutiReq", "getLteRfBandAndAntennaNumberInfo", "getNr5gRfBandAndAntennaNumberInfo", "getLteTimerExpiryInfo", "getImsSipMessageInfo", "getLteDataSceneInfo", "getNr5gDataSceneInfo", "getNr5gRlfList", "getLteTimerExpiryList", "getNr5gTimerExpiryList", "getLteNasEmmStateList", "getNasMm5gStateList", "getNr5gRrcIratRedirFromNrList", "getNr5gRrcIratReselFromNrList", "getNr5gRrcIratHoFromNrList", "getNr5gSawAndSawlessTimeStatisticsInfo", "getLteMl1IratMeasList", "qcRilValidateConfig", "onHookMbnReactivateSync", "getLteRssiAndSigPathInfo", "getNrRssiAndSigPathInfo", "getNrChannelInfo", "getLteChannelInfo", "getLteAiDataInfo", "getNr5gAiDataInfo", "getLteNasRejectForDfsList", "getNr5gNasRejectForDfsList", "getLteImsSipMessageForDfsList", "getLteNlicEngineActiveInfo", "getLteEventThresholdValueInfo", "getLteAndNr5gBandwidthInfo", "getLteSnsDataStallInfo", "getCafiSnsInfo", "getLteSnsSetDubiousCellInfo", "getLteSnsInfo", "getNr5gSnsDataStallInfo", "getNr5gSnsSetDubiousCellInfo", "getNr5gSnsInfo", "getHwId", "qcRilReadOemNV", "qcRilWriteOemNV", "qcRilFeatureQueryCmn", "qcRilFeatureCtlCmn", "qcRilRefreshMbn", "execSetUserFiveGNetworkMode", "execSetUserVoNREnabled", "onHookCommonMsgSyncBySub", "getLteNrBandAndTxPowerInfo", "getLteNrBandAndRsrpInfo", "getLteNrBandAndSinrInfo", "getNr5gSnsAiInfo", "get6RXInfo", "getT1SInfo", "get380Info", "getRsrpAndTxPowerInfo", "getRsrpAndSinrInfo", "get6RxCountInfo", "getUeNoSaInfo", "getUeOosInfo", "getVoiceQualityInfo", "getNrPuschTxModeCountInfo", "getLteReselThresholdInfo", "getLteReselBandAndRsrpInfo", "getLtehoBandAndRsrpInfo", "getNr5gReselThresholdInfo", "getNr5gReselBandAndRsrpInfo", "getNr5ghoBandAndRsrpInfo", "getNrTxPowerByChannelInfo", "getNr5gMsg1AndMsg3SendFailInfo", "getLteMsg1AndMsg3SendFailInfo", "getNr5gRachFailRateInfo", "getLteRachFailRateInfo", "getCallEndInfo", "getLteNrBandAndFbrxPowerInfo", "getNrPdschNumLayersCountInfo", "getPreferCellInfo", "getGpsOnDurationAndCountInfo", "getNr5gTxAntCountInfo", "getNr5gSnsInfoV2", "getNr5gSnsDataStallInfoV2", "getAirportModeInfo", "getBorderModeInfo", "getFbrxForPowerErrCodeList", "getStAverageSarInfo", "getLteSnsInfoV2", "getLteSnsDataStallInfoV2", "getNr5gSnsRrcMitigationInfo", "getLteSnsRrcMitigationInfo", "getMbnSpaceSizeInfo", "getCallEndInfoList", "onHookModemFeatureSet", "onHookStartCollectData", "onHookStartCollectDataSync", "sendSetPropertyCmd", "getNr5gRachFailNotDeleteInfo", "getLteRachFailNotDeleteInfo", "getNrTxPortInfo", "setSrvDomain", "getModemEid", "onHookUimPowerReqEx", "getAbnormalTauCountInfo"};
        this.HOOK_METHODS = strArr;
        this.mContext = context;
        this.mRilHook = RilHook.init(context);
        if (new File("/system_ext/framework/xiaomi-modem-common.jar").exists()) {
            try {
                Class clsLoadClass = new PathClassLoader("/system_ext/framework/xiaomi-modem-common.jar", getClass().getClassLoader()).loadClass("com.xiaomi.mirilhook.MiRilHook");
                this.qcrilHookCls = clsLoadClass;
                if (clsLoadClass == null) {
                    log("hook loading failed");
                    return;
                }
                Method[] methods = clsLoadClass.getMethods();
                for (String str : strArr) {
                    for (Method method : methods) {
                        if (str.equals(method.getName())) {
                            this.mMethodMap.put(str, method);
                            break;
                        }
                    }
                }
                Object objNewProxyInstance = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{MiRilHookCallback.class}, new RilHookCbHandler());
                Constructor constructor = this.qcrilHookCls.getConstructor(Context.class, MiRilHookCallback.class, String.class);
                if (constructor != null) {
                    this.mQcHook = constructor.newInstance(context, objNewProxyInstance, context.getPackageName());
                    return;
                }
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
                return;
            } catch (InstantiationException e3) {
                e3.printStackTrace();
                return;
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
                return;
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
                return;
            }
        }
        log("Hook jar doesn't exist");
        mQcrilHookReady = false;
    }

    public static synchronized ModemUtils init(Context context) {
        try {
            if (sInstance == null && MiuiPhoneUtils.isModemUtilsSupport()) {
                sInstance = new ModemUtils(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public static ModemUtils getInstance() {
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("ModemHydraUtils", str);
    }

    public int callQcrilHookGet(String str, int i) {
        if (!mQcrilHookReady || str == null || this.mMethodMap.get(str) == null) {
            return 65535;
        }
        try {
            return ((Integer) ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, Integer.valueOf(i))).intValue();
        } catch (ClassCastException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return 65535;
        }
    }

    public float callQcrilHookGetFloat(String str, int i) {
        if (!mQcrilHookReady || str == null || this.mMethodMap.get(str) == null) {
            return -1.0f;
        }
        try {
            return ((Float) ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, Integer.valueOf(i))).floatValue();
        } catch (ClassCastException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return -1.0f;
        }
    }

    public Object callQcrilHookGetObject(String str, int i) {
        if (mQcrilHookReady && str != null && this.mMethodMap.get(str) != null) {
            ConcurrentHashMap concurrentHashMap = this.mMethodMap;
            try {
                if (i < 0) {
                    return ((Method) concurrentHashMap.get(str)).invoke(this.mQcHook, null);
                }
                return ((Method) concurrentHashMap.get(str)).invoke(this.mQcHook, Integer.valueOf(i));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean callQcrilHookAction(String str, int i) {
        if (!mQcrilHookReady || str == null || this.mMethodMap.get(str) == null) {
            return false;
        }
        try {
            return ((Boolean) ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, Integer.valueOf(i))).booleanValue();
        } catch (ClassCastException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int callQcrilHookGet(String str) {
        if (!mQcrilHookReady || str == null || this.mMethodMap.get(str) == null) {
            return 65535;
        }
        try {
            return ((Integer) ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, null)).intValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return 65535;
        }
    }

    public boolean callQcrilHookSet(String str, Object... objArr) {
        if (mQcrilHookReady && str != null && this.mMethodMap.get(str) != null) {
            try {
                if (objArr.length == 1) {
                    ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, objArr[0]);
                } else if (objArr.length == 2) {
                    ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, objArr[0], objArr[1]);
                }
                return true;
            } catch (IllegalAccessException | NullPointerException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Object callHook(String str, Object... objArr) {
        if (mQcrilHookReady && str != null && this.mMethodMap.get(str) != null) {
            try {
                if (objArr.length == 0) {
                    return ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, null);
                }
                if (objArr.length == 1) {
                    return ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, objArr[0]);
                }
                if (objArr.length == 2) {
                    return ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, objArr[0], objArr[1]);
                }
                if (objArr.length == 3) {
                    return ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, objArr[0], objArr[1], objArr[2]);
                }
                if (objArr.length == 4) {
                    return ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, objArr[0], objArr[1], objArr[2], objArr[3]);
                }
                if (objArr.length == 5) {
                    return ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, objArr[0], objArr[1], objArr[2], objArr[3], objArr[4]);
                }
                if (objArr.length == 6) {
                    return ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5]);
                }
                if (objArr.length == 7) {
                    return ((Method) this.mMethodMap.get(str)).invoke(this.mQcHook, objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6]);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean isModemFeatureSupported(int i) {
        Object obj;
        Method method = (Method) this.mMethodMap.get("isModemFeatureSupported");
        if (!mQcrilHookReady || (obj = this.mQcHook) == null || method == null) {
            return false;
        }
        try {
            Object objInvoke = method.invoke(obj, Integer.valueOf(i));
            if (objInvoke != null) {
                return ((Boolean) objInvoke).booleanValue();
            }
            return false;
        } catch (Exception e) {
            Log.e("ModemHydraUtils", "isModemFeatureSupported: Exception: " + e);
            return false;
        }
    }

    public static String sizeByteToString(long j) {
        String str;
        double d = j;
        if (d >= 1000.0d) {
            double d2 = d / 1024.0d;
            if (d2 >= 1000.0d) {
                d2 = d / 1048576.0d;
                if (d2 >= 1000.0d) {
                    d2 = d / 1.073741824E9d;
                    if (d2 < 1000.0d) {
                        str = " G";
                    } else {
                        d /= 1.099511627776E12d;
                        str = " T";
                    }
                } else {
                    str = " M";
                }
            } else {
                str = " K";
            }
            d = d2;
        } else {
            str = " B";
        }
        return "" + new BigDecimal(d).setScale(2, 4).doubleValue() + str;
    }

    public void startModemStats() {
        if (!mQcrilHookReady || this.mQcHook == null) {
            return;
        }
        log("startMonitor");
        try {
            if (this.mMethodMap.get("startModemStats") != null) {
                ((Method) this.mMethodMap.get("startModemStats")).invoke(this.mQcHook, this.mContext.getPackageName());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
    }

    public void stopModemStats() {
        if (!mQcrilHookReady || this.mQcHook == null) {
            return;
        }
        log("stopMonitor");
        try {
            if (this.mMethodMap.get("closeModemStats") != null) {
                ((Method) this.mMethodMap.get("closeModemStats")).invoke(this.mQcHook, this.mContext.getPackageName());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
    }

    public void setModemLimitRilMsgFlag(boolean z) {
        int i;
        int iIndexOf;
        try {
            this.mReadItem = this.mRilHook.readOemNvItem(6859);
        } catch (Exception e) {
            log("readOemNvItem error: " + e);
        }
        log("mReadItem: " + this.mReadItem);
        if (this.mReadItem != null) {
            int i2 = 0;
            i = -1;
            while (true) {
                iIndexOf = this.mReadItem.indexOf(59, i + 1);
                i2++;
                if (iIndexOf == -1 || i2 >= 8) {
                    break;
                } else {
                    i = iIndexOf;
                }
            }
        } else {
            i = -1;
            iIndexOf = -1;
        }
        String str = this.mReadItem;
        if (str != null && iIndexOf != -1 && i != -1 && iIndexOf > i) {
            String strSubstring = str.substring(0, i + 1);
            String strSubstring2 = this.mReadItem.substring(iIndexOf);
            String str2 = strSubstring + (z ? "2" : "1") + strSubstring2;
            log("writeItem:".concat(str2));
            if (this.mReadItem.equals(str2)) {
                log("same data will not be updated, return");
                return;
            }
            try {
                this.mRilHook.writeOemNvItem(6859, str2);
                return;
            } catch (Exception e2) {
                log("writeOemNvItem error: " + e2);
                return;
            }
        }
        log("mReadItem is NULL, cannot find the right index");
    }

    public static byte[] onGetModemMsgBytes(int[] iArr) {
        int length = iArr.length;
        int i = length * 4;
        log("msg_num = " + length + ", msg_len = " + i);
        byte[] bArr = new byte[i];
        ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArr).order(ByteOrder.nativeOrder());
        for (int i2 = 0; i2 < length; i2++) {
            try {
                byteBufferOrder.putInt(iArr[i2]);
                log("int_array[" + i2 + "] = " + iArr[i2]);
            } catch (Exception e) {
                log("onGetModemMsgBytes reqBuffer.putInt exception" + e);
            }
        }
        return bArr;
    }

    class RilHookCbHandler implements InvocationHandler {
        private RilHookCbHandler() {
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) {
            if ("onQcRilHookReady".equals(method.getName())) {
                ModemUtils.log("QcRilHook is Ready");
                ModemUtils.mHandler.sendEmptyMessage(0);
                return null;
            }
            if (!"onQcRilHookDisconnected".equals(method.getName())) {
                return null;
            }
            ModemUtils.log("QcRilHook is disconnected");
            ModemUtils.mQcrilHookReady = false;
            return null;
        }
    }

    /* JADX INFO: renamed from: com.android.phone.utils.ModemUtils$1, reason: invalid class name */
    class AnonymousClass1 extends Handler {
        AnonymousClass1(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ModemUtils.log("handleMessage msg id: " + message.what);
            if (message.what == 0) {
                ModemUtils.log("EVENT_QCRIL_HOOK_READY");
                ModemUtils.mQcrilHookReady = true;
                new Thread(new Runnable() { // from class: com.android.phone.utils.ModemUtils$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        EventHubManager.getInstance().postEvent(new TelephonyEvent.RilEvent("ril_ready", true));
                    }
                }, "hookReady").start();
                return;
            }
            ModemUtils.log("Not support this msg");
        }
    }

    public boolean copyEfs(int i, String str, int i2, String str2) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || !MiuiPhoneUtils.isValidatePhoneId(i2) || str == null || str2 == null) {
            log("parameter exception");
            return false;
        }
        if (this.mQcHook == null) {
            log("mQcHook is null, return");
            return false;
        }
        try {
            Method method = (Method) this.mMethodMap.get("efsCopy");
            if (method == null) {
                log("no copyEfsMethod");
                return false;
            }
            method.setAccessible(true);
            boolean zBooleanValue = ((Boolean) method.invoke(this.mQcHook, Integer.valueOf(i), str, Integer.valueOf(i2), str2)).booleanValue();
            log("copyEfsMethod get result, result:" + zBooleanValue);
            return zBooleanValue;
        } catch (Exception e) {
            log("copyEfsMethod Exception: " + e);
            return false;
        }
    }

    public boolean syncEfs() {
        if (this.mQcHook == null) {
            log("mQcHook is null, return");
            return false;
        }
        try {
            Method method = (Method) this.mMethodMap.get("efsSync");
            if (method == null) {
                log("no syncEfsMethod");
                return false;
            }
            method.setAccessible(true);
            boolean zBooleanValue = ((Boolean) method.invoke(this.mQcHook, null)).booleanValue();
            log("syncEfsMethod get result, result:" + zBooleanValue);
            return zBooleanValue;
        } catch (Exception e) {
            log("syncEfsMethod Exception: " + e);
            return false;
        }
    }

    public boolean deleteEfs(int i, String str) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || str == null) {
            log("parameter exception");
            return false;
        }
        if (this.mQcHook == null) {
            log("mQcHook is null, return");
            return false;
        }
        try {
            Method method = (Method) this.mMethodMap.get("efsDelete");
            if (method == null) {
                log("no deleteEfsMethod");
                return false;
            }
            method.setAccessible(true);
            boolean zBooleanValue = ((Boolean) method.invoke(this.mQcHook, Integer.valueOf(i), str)).booleanValue();
            log("deleteEfsMethod get result, result:" + zBooleanValue);
            return zBooleanValue;
        } catch (Exception e) {
            log("deleteEfsMethod Exception: " + e);
            return false;
        }
    }

    public boolean writeEfs(int i, String str, byte[] bArr) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || str == null || bArr == null) {
            log("parameter exception");
            return false;
        }
        if (this.mQcHook == null) {
            log("mQcHook is null, return");
            return false;
        }
        try {
            Method method = (Method) this.mMethodMap.get("efsWrite");
            if (method == null) {
                log("no writeEfsMethod");
                return false;
            }
            method.setAccessible(true);
            boolean zBooleanValue = ((Boolean) method.invoke(this.mQcHook, Integer.valueOf(i), str, bArr)).booleanValue();
            log("writeEfs get result, result:" + zBooleanValue);
            return zBooleanValue;
        } catch (Exception e) {
            log("writeEfs Exception: " + e);
            return false;
        }
    }

    public byte[] readEfs(int i, String str) {
        byte[] bArr = null;
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || str == null) {
            log("parameter exception");
            return null;
        }
        if (this.mQcHook == null) {
            log("mQcHook is null, return");
            return null;
        }
        try {
            Method method = (Method) this.mMethodMap.get("efsReadBytes");
            if (method == null) {
                log("no efsReadBytes");
                return null;
            }
            method.setAccessible(true);
            byte[] bArr2 = (byte[]) method.invoke(this.mQcHook, Integer.valueOf(i), str);
            try {
                log("readEfs get result, result:" + Arrays.toString(bArr2));
                return bArr2;
            } catch (Exception e) {
                bArr = bArr2;
                e = e;
                log("readEfs Exception: " + e);
                return bArr;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public boolean onHookMbnRefresh(int i) {
        if (this.mQcHook == null) {
            log("mQcHook is null, return");
            return false;
        }
        try {
            Method method = (Method) this.mMethodMap.get("onHookMbnRefresh");
            if (method == null) {
                log("no onHookMbnRefresh");
                return false;
            }
            method.setAccessible(true);
            boolean zBooleanValue = ((Boolean) method.invoke(this.mQcHook, Integer.valueOf(i))).booleanValue();
            log("onHookMbnRefresh result, result:" + zBooleanValue);
            return zBooleanValue;
        } catch (Exception e) {
            log("onHookMbnRefresh Exception: " + e);
            return false;
        }
    }

    public ByteBuffer getMtConnFailInfo(int i) {
        return getMiRilHook(this.mContext).getMtConnFailInfo(i);
    }

    public ByteBuffer getMtConnFailInfoList(int i) {
        return getMiRilHook(this.mContext).getMtConnFailInfoList(i);
    }

    public ByteBuffer getVoiceQualityInfoList(int i) {
        return getMiRilHook(this.mContext).getVoiceQualityInfoList(i);
    }

    public ByteBuffer getLowMosScoreInfoList(int i) {
        return getMiRilHook(this.mContext).getLowMosScoreInfoList(i);
    }

    public ByteBuffer onHookCommonMsgSyncBySub(int i, byte[] bArr, int i2) {
        Class cls;
        if (this.mQcHook == null || (cls = this.qcrilHookCls) == null) {
            return null;
        }
        try {
            Class<?> cls2 = Integer.TYPE;
            return (ByteBuffer) cls.getMethod("onHookCommonMsgSyncBySub", cls2, byte[].class, cls2).invoke(this.mQcHook, Integer.valueOf(i), bArr, Integer.valueOf(i2));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ByteBuffer onHookCommonMsgSync(int i, int i2, int i3) {
        return getMiRilHook(this.mContext).onHookCommonMsgSync(i, i2, i3);
    }

    public int setSrvDomain(int i, int i2) {
        Class cls;
        int iIntValue = 0;
        if (this.mQcHook != null && (cls = this.qcrilHookCls) != null) {
            try {
                Class<?> cls2 = Integer.TYPE;
                iIntValue = ((Integer) cls.getMethod("setSrvDomain", cls2, cls2).invoke(this.mQcHook, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
                log("setSrvDomain, slotId:" + i + ",srvDomain:" + i2 + ",ret:" + iIntValue);
                return iIntValue;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return iIntValue;
    }

    public String getModemEid(int i) {
        Class cls;
        String str = "";
        if (this.mQcHook != null && (cls = this.qcrilHookCls) != null) {
            try {
                String str2 = (String) cls.getMethod("getModemEid", Integer.TYPE).invoke(this.mQcHook, Integer.valueOf(i));
                try {
                    log("getModemEid, slotId:" + i + ",eid:" + str2);
                    return str2;
                } catch (InvocationTargetException e) {
                    e = e;
                    str = str2;
                    e.printStackTrace();
                    return str;
                } catch (Exception e2) {
                    e = e2;
                    str = str2;
                    e.printStackTrace();
                    return str;
                }
            } catch (InvocationTargetException e3) {
                e = e3;
            } catch (Exception e4) {
                e = e4;
            }
        }
        return str;
    }

    public boolean onHookUimPowerReqEx(int i, int i2, int i3) {
        Class cls;
        if (this.mQcHook != null && (cls = this.qcrilHookCls) != null) {
            try {
                Class<?> cls2 = Integer.TYPE;
                return ((Boolean) cls.getMethod("onHookUimPowerReqEx", cls2, cls2, cls2).invoke(this.mQcHook, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3))).booleanValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public ByteBuffer getNr5gB821MessageList(int i) {
        return getMiRilHook(this.mContext).getNr5gB821MessageList(i);
    }

    public ByteBuffer getAntAsdivConfigList(int i) {
        return getMiRilHook(this.mContext).getAntAsdivConfigList(i);
    }

    public ByteBuffer getNr5gRxRsrpList(int i) {
        return getMiRilHook(this.mContext).getNr5gRxRsrpList(i);
    }

    public ByteBuffer getLteRxRsrpList(int i) {
        return getMiRilHook(this.mContext).getLteRxRsrpList(i);
    }

    public ByteBuffer getCaasLicenseStat(int i) {
        return getMiRilHook(this.mContext).getCaasLicenseStat(i);
    }

    public ByteBuffer getB80xCombinedMsgList(int i) {
        return getMiRilHook(this.mContext).getB80xCombinedMsgList(i);
    }

    public ByteBuffer getB16ePuschPowerList(int i) {
        return getMiRilHook(this.mContext).getB16ePuschPowerList(i);
    }

    public ByteBuffer getB884PuschPowerList(int i) {
        return getMiRilHook(this.mContext).getB884PuschPowerList(i);
    }

    public ByteBuffer getB88aRachSuccessList(int i) {
        return getMiRilHook(this.mContext).getB88aRachSuccessList(i);
    }

    public ByteBuffer getB88aRachFailureList(int i) {
        return getMiRilHook(this.mContext).getB88aRachFailureList(i);
    }

    public ByteBuffer getB18aRlmSyncList(int i) {
        return getMiRilHook(this.mContext).getB18aRlmSyncList(i);
    }

    public ByteBuffer getB18aRlmOosList(int i) {
        return getMiRilHook(this.mContext).getB18aRlmOosList(i);
    }

    public ByteBuffer getLteB0c0MessageList(int i) {
        return getMiRilHook(this.mContext).getLteB0c0MessageList(i);
    }

    public ByteBuffer getLteNasB0e23List(int i) {
        return getMiRilHook(this.mContext).getLteNasB0e23List(i);
    }

    public ByteBuffer getLteMacRachB062List(int i) {
        return getMiRilHook(this.mContext).getLteMacRachB062List(i);
    }

    public ByteBuffer getImsReg1832List(int i) {
        return getMiRilHook(this.mContext).getImsReg1832List(i);
    }

    public ByteBuffer getB193LteSrvMeas1sCollect(int i) {
        return getMiRilHook(this.mContext).getB193LteSrvMeas1sCollect(i);
    }

    public ByteBuffer getB17fLteMl1SrvCellMeasHistory(int i) {
        return getMiRilHook(this.mContext).getB17fLteMl1SrvCellMeasHistory(i);
    }

    public ByteBuffer getB176LteInitialAcqResult(int i) {
        return getMiRilHook(this.mContext).getB176LteInitialAcqResult(i);
    }

    public ByteBuffer getB18eLteMl1SystemScanResults(int i) {
        return getMiRilHook(this.mContext).getB18eLteMl1SystemScanResults(i);
    }

    public ByteBuffer getLteNasEmmPlainOtaOutMsg(int i) {
        return getMiRilHook(this.mContext).getLteNasEmmPlainOtaOutMsg(i);
    }

    public ByteBuffer getLteNasEmmPlainOtaInMsg(int i) {
        return getMiRilHook(this.mContext).getLteNasEmmPlainOtaInMsg(i);
    }

    public ByteBuffer getB822Nr5gRrcMibList(int i) {
        return getMiRilHook(this.mContext).getB822Nr5gRrcMibList(i);
    }

    public int getNr5gRrcState(int i) {
        return getMiRilHook(this.mContext).getNr5gRrcState(i);
    }

    public ByteBuffer getB80abCombinedMsgList(int i) {
        return getMiRilHook(this.mContext).getB80abCombinedMsgList(i);
    }

    public ByteBuffer getB970Nr5gMl1SuccessList(int i) {
        return getMiRilHook(this.mContext).getB970Nr5gMl1SuccessList(i);
    }

    public ByteBuffer getB970Nr5gMl1FailureList(int i) {
        return getMiRilHook(this.mContext).getB970Nr5gMl1FailureList(i);
    }

    public ByteBuffer getB97fNr5GMl1SearcherMsgList(int i) {
        return getMiRilHook(this.mContext).getB97fNr5GMl1SearcherMsgList(i);
    }

    public ByteBuffer getB96dNr5gMl1SuccessList(int i) {
        return getMiRilHook(this.mContext).getB96dNr5gMl1SuccessList(i);
    }

    public ByteBuffer getB96dNr5gMl1FailureList(int i) {
        return getMiRilHook(this.mContext).getB96dNr5gMl1FailureList(i);
    }

    public boolean onHookCommonMsg(int i, int i2, int i3, int i4) {
        return getMiRilHook(this.mContext).onHookCommonMsg(i, i2, i3, i4);
    }

    public void regPoorNetworkFastRecovery(int i, int i2, Handler handler, int i3, Object obj) {
        Class cls;
        log("FastRecovery regPoorNetworkFastRecovery qcrilHookcls = " + this.qcrilHookCls);
        if (this.mQcHook == null || (cls = this.qcrilHookCls) == null) {
            return;
        }
        try {
            Class<?> cls2 = Integer.TYPE;
            Method method = cls.getMethod("regPoorNetworkFastRecovery", cls2, cls2, Handler.class, cls2, Object.class);
            log("FastRecovery regPoorNetworkFastRecovery");
            method.invoke(this.mQcHook, Integer.valueOf(i), Integer.valueOf(i2), handler, Integer.valueOf(i3), obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ByteBuffer getLteStaticFeatureCellInfo(int i) {
        Class cls;
        if (this.mQcHook == null || (cls = this.qcrilHookCls) == null) {
            return null;
        }
        try {
            Method method = cls.getMethod("getLteStaticFeatureCellInfo", Integer.TYPE);
            log("getLteStaticFeatureCellInfo");
            return (ByteBuffer) method.invoke(this.mQcHook, Integer.valueOf(i));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ByteBuffer getNr5gStaticFeatureCellInfo(int i) {
        Class cls;
        if (this.mQcHook == null || (cls = this.qcrilHookCls) == null) {
            return null;
        }
        try {
            Method method = cls.getMethod("getNr5gStaticFeatureCellInfo", Integer.TYPE);
            log("getNr5gStaticFeatureCellInfo");
            return (ByteBuffer) method.invoke(this.mQcHook, Integer.valueOf(i));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean registerOemHookRegistrant(Handler handler, int i, Object obj, String str) {
        Class cls;
        if (this.mQcHook != null && (cls = this.qcrilHookCls) != null) {
            try {
                cls.getMethod("registerOemHookRegistrant", Handler.class, Integer.TYPE, Object.class, String.class).invoke(this.mQcHook, handler, Integer.valueOf(i), obj, str);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean registerOemHookRegistrant(Handler handler, int i, Object obj, String str, int i2) {
        Class cls;
        if (this.mQcHook != null && (cls = this.qcrilHookCls) != null) {
            try {
                Class<?> cls2 = Integer.TYPE;
                cls.getMethod("registerOemHookRegistrant", Handler.class, cls2, Object.class, String.class, byte[].class, cls2).invoke(this.mQcHook, handler, Integer.valueOf(i), obj, str, null, Integer.valueOf(i2));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void unregisterOemHookRegistrant(Handler handler, int i, Object obj) {
        Class cls;
        if (this.mQcHook == null || (cls = this.qcrilHookCls) == null) {
            return;
        }
        try {
            cls.getMethod("unRegisterOemHookRegistrant", Handler.class, Integer.TYPE, Object.class).invoke(this.mQcHook, handler, Integer.valueOf(i), obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readOemNvItem(int i) {
        Object obj;
        Method method = (Method) this.mMethodMap.get("qcRilReadOemNV");
        if (mQcrilHookReady && (obj = this.mQcHook) != null && method != null) {
            try {
                return new String((byte[]) method.invoke(obj, Integer.valueOf(i)));
            } catch (Exception e) {
                Log.e("ModemHydraUtils", "readOemNvItem: Exception: " + e);
                return "";
            }
        }
        return "";
    }

    public boolean writeOemNvItem(int i, String str) {
        Method method = (Method) this.mMethodMap.get("qcRilWriteOemNV");
        if (mQcrilHookReady && this.mQcHook != null && method != null) {
            try {
                return ((Boolean) method.invoke(this.mQcHook, Integer.valueOf(i), str.substring(0, str.lastIndexOf(59) + 1).getBytes())).booleanValue();
            } catch (Exception e) {
                Log.e("ModemHydraUtils", "writeOemNvItem: Exception: " + e);
            }
        }
        return false;
    }

    public byte[] featureQueryCmn(int i, int i2, int i3) {
        Object obj;
        if (!MiuiPhoneUtils.isSupportFakeCellV2() && !MiuiPhoneUtils.isSupportFakeCellV1() && !MiuiPhoneUtils.isSupportFeatureQueryCmn()) {
            return null;
        }
        Method method = (Method) this.mMethodMap.get("qcRilFeatureQueryCmn");
        if (mQcrilHookReady && (obj = this.mQcHook) != null && method != null) {
            try {
                return (byte[]) method.invoke(obj, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
            } catch (Exception e) {
                Log.e("ModemHydraUtils", "featureQueryCmn: Exception: " + e);
            }
        }
        return null;
    }

    public boolean featureSetCmn(int i, int i2, int i3) {
        Object obj;
        if (!MiuiPhoneUtils.isSupportFakeCellV2() && !MiuiPhoneUtils.isSupportFeatureQueryCmn()) {
            return false;
        }
        Method method = (Method) this.mMethodMap.get("qcRilFeatureCtlCmn");
        if (mQcrilHookReady && (obj = this.mQcHook) != null && method != null) {
            try {
                return ((Boolean) method.invoke(obj, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3))).booleanValue();
            } catch (Exception e) {
                Log.e("ModemHydraUtils", "featureSetCmn: Exception: " + e);
            }
        }
        return false;
    }

    public void regSmartNetworkSchedule(int i, Handler handler, int i2, Object obj) {
        Class cls;
        log("regSmartNetworkSchedule qcrilHookcls = " + this.qcrilHookCls);
        if (this.mQcHook == null || (cls = this.qcrilHookCls) == null) {
            return;
        }
        try {
            Class<?> cls2 = Integer.TYPE;
            Method method = cls.getMethod("regSmartGrant", cls2, Handler.class, cls2, Object.class);
            log("regSmartNetworkSchedule");
            method.invoke(this.mQcHook, Integer.valueOf(i), handler, Integer.valueOf(i2), obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegSmartNetworkSchedule() {
        Class cls;
        log("unRegSmartNetworkSchedule");
        if (this.mQcHook == null || (cls = this.qcrilHookCls) == null) {
            return;
        }
        try {
            Class[] clsArr = new Class[0];
            cls.getMethod("deregSmartGrant", null).invoke(this.mQcHook, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void regSubwayAndHsrRecognition(int i, int i2, Handler handler, int i3, Object obj) {
        Class cls;
        log("regSubwayAndHsrRecognition qcrilHookcls = " + this.qcrilHookCls);
        if (this.mQcHook == null || (cls = this.qcrilHookCls) == null) {
            return;
        }
        try {
            Class<?> cls2 = Integer.TYPE;
            Method method = cls.getMethod("regSubwayAndHsrRecognition", cls2, cls2, Handler.class, cls2, Object.class);
            log("regSubwayAndHsrRecognition");
            method.invoke(this.mQcHook, Integer.valueOf(i), Integer.valueOf(i2), handler, Integer.valueOf(i3), obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegSubwayAndHsrRecognition(int i) {
        Class cls;
        log("unRegSubwayAndHsrRecognition");
        if (this.mQcHook == null || (cls = this.qcrilHookCls) == null) {
            return;
        }
        try {
            cls.getMethod("deregSubwayAndHsrRecognition", Integer.TYPE).invoke(this.mQcHook, Integer.valueOf(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void regsiterDsdaSubModeIndication() {
        try {
            if (ModemProvider.IS_QCOM) {
                if (!featureSetCmn(14, 1, 0)) {
                    log("regsiterDsdaSubModeIndication failed");
                } else {
                    log("regsiterDsdaSubModeIndication SUCCESS");
                }
            }
        } catch (Exception e) {
            log("regsiterDsdaSubModeIndication Exception : " + e);
        }
    }

    public void registerSpecialSimRecognition(int i, Handler handler, int i2, Object obj) {
        Class cls;
        log("registerSpecialSimRecognition mQcHook= " + this.mQcHook + " qcrilHookcls=" + this.qcrilHookCls);
        if (this.mQcHook == null || (cls = this.qcrilHookCls) == null) {
            return;
        }
        try {
            Class<?> cls2 = Integer.TYPE;
            Method method = cls.getMethod("regArrearsCardRecognition", cls2, Handler.class, cls2, Object.class);
            log("regSpecialSimRecognition");
            method.invoke(this.mQcHook, Integer.valueOf(i), handler, Integer.valueOf(i2), obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean regLteEmmTimerStart(int i, Handler handler, int i2, Object obj) {
        Class cls;
        log("regLteEmmTimerStart qcrilHookcls= " + this.mQcHook + " qcrilHookcls=" + this.qcrilHookCls);
        if (this.mQcHook != null && (cls = this.qcrilHookCls) != null) {
            try {
                Class<?> cls2 = Integer.TYPE;
                return ((Boolean) cls.getMethod("getLteEmmTimerStart", cls2, Handler.class, cls2, Object.class).invoke(this.mQcHook, Integer.valueOf(i), handler, Integer.valueOf(i2), obj)).booleanValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean unRegLteEmmTimerStart() {
        Class cls;
        log("unRegLteEmmTimerStart");
        if (this.mQcHook != null && (cls = this.qcrilHookCls) != null) {
            try {
                Class[] clsArr = new Class[0];
                return ((Boolean) cls.getMethod("degetLteEmmTimerStart", null).invoke(this.mQcHook, null)).booleanValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static MiRilHookBase loadHookFromJar(Context context) {
        try {
            PathClassLoader pathClassLoader = new PathClassLoader("/system_ext/framework/xiaomi-modem-common.jar", context.getClassLoader());
            Class clsLoadClass = pathClassLoader.loadClass("com.xiaomi.mirilhook.MiRilHook");
            Class<?> clsLoadClass2 = pathClassLoader.loadClass("com.xiaomi.mirilhook.MiRilHookCallback");
            return (MiRilHookBase) clsLoadClass.getConstructor(Context.class, clsLoadClass2, String.class).newInstance(context, Proxy.newProxyInstance(pathClassLoader, new Class[]{clsLoadClass2}, new RilHookCbHandler()), context.getPackageName());
        } catch (Exception unused) {
            log("hook loading failed");
            return null;
        }
    }

    public static MiRilHookBase getMiRilHook(Context context) {
        if (context == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("invalid modem context!");
            return null;
        }
        if (mMiRilHook != null) {
            return mMiRilHook;
        }
        Context applicationContext = context.getApplicationContext();
        if (applicationContext == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("application context is null");
            return null;
        }
        synchronized (MiRilHookBase.class) {
            try {
                if (mMiRilHook == null) {
                    MiRilHookBase miRilHookBaseLoadHookFromJar = loadHookFromJar(applicationContext);
                    log("loadHookFromJar hook = " + miRilHookBaseLoadHookFromJar);
                    if (miRilHookBaseLoadHookFromJar == null) {
                        miRilHookBaseLoadHookFromJar = new MiRilHookBase(applicationContext);
                        log("hook loading failed, use base hook");
                    }
                    mMiRilHook = miRilHookBaseLoadHookFromJar;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return mMiRilHook;
    }
}
