package com.xiaomi.mirilhook;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import com.xiaomi.modem.ModemUtils;
import java.nio.ByteBuffer;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public class MiRilHookBase {
    public static final int DEFAULT_PHONE = 0;
    public static final int EVENT_OEMHOOK_CARD_SLOT_STATUS_QUERY = 39;
    public static final int EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET = 115;
    public static final int EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET = 116;
    public static final int EVENT_OEMHOOK_DSDA_HW_MBN_CFG = 134;
    public static final int EVENT_OEMHOOK_HVOLTE_MBN_CFG = 44;
    public static final int EVENT_OEMHOOK_MODEM_ENHANCE_COMMON = 100;
    public static final int EVENT_OEMHOOK_MODEM_STATS_DATA_GET = 62;
    public static final int EVENT_OEMHOOK_MTB_CLASS_MASK_GET = 81;
    public static final int EVENT_OEMHOOK_NOTIFY_SHUTDOWN = 14;
    public static final int EVENT_OEMHOOK_NV_EFS_SYNC = 35;
    public static final int EVENT_OEMHOOK_RILD_RESET = 103;
    public static final int EVENT_OEMHOOK_SSR_BY_ERROR_FATAL = 31;
    public static final int EVENT_OEMHOOK_XIAOMI_EFS_DELTE = 6;
    public static final int EVENT_OEMHOOK_XIAOMI_EFS_READ = 4;
    public static final int EVENT_OEMHOOK_XIAOMI_MODEM_HAL_CHIP_ID_GET = 82;
    public static final int EVENT_OEMHOOK_XIAOMI_NV_READ = 1;
    public static final int EVENT_RIL_HOOK_DISCONNECTED = -2;
    public static final int EVENT_RIL_HOOK_READY = -1;
    public static final int HOOK_ANDROID_VERSION_T = 13;
    public static final int HOOK_ANDROID_VERSION_U = 14;
    public static final int HOOK_ANDROID_VERSION_UNKNOW = 0;
    public static final int HOOK_ANDROID_VERSION_V = 15;
    public static final int HOOK_TYPE_DEF = 0;
    public static final int HOOK_TYPE_MTK = 1;
    public static final int HOOK_TYPE_QCOM = 2;
    public static final int HOOK_TYPE_SPRD = 4;
    public static final int HOOK_TYPE_XRING = 3;
    public static final int INT_SIZE = 4;
    private static final String LOG_TAG = "MiRilHook-BASE";
    public static final int LONG_SIZE = 8;
    public static final String PROP_RADIO_XIAOMI_HAL_CHIP_ID = "ro.vendor.radio.hal_chip_id";
    public static final int SUB_0 = 0;
    public static final int SUB_1 = 1;
    public static final int SUB_ALL = 2;
    public static final int SUB_MASK_0 = 1;
    public static final int SUB_MASK_1 = 2;
    public static final int SUB_MASK_ALL = 3;
    public static final int XIAOMI_HAL_CHIP_ID_INVALID = -1;
    public static final int XIAOMI_MANUFACTURER_ID_HUAQIN = 4;
    public static final int XIAOMI_MANUFACTURER_ID_LONGQI = 3;
    public static final int XIAOMI_MANUFACTURER_ID_NULL = 0;
    public static final int XIAOMI_MANUFACTURER_ID_WENTAI = 2;
    public static final int XIAOMI_MANUFACTURER_ID_XIAOMI = 1;
    public static final int XIAOMI_MANUFACTURER_ID_ZHONGKE = 5;
    public static final int XIAOMI_MODEM_BP_CHIP_ID_INVALID = 0;
    public static final int XIAOMI_MODEM_HAL_CHIP_ID_NULL = 0;
    protected static Context mContext = null;
    protected static Handler mHandlerClient = null;
    protected static int mHookAndroidVersion = 0;

    public MiRilHookBase(Context context) {
        mContext = context;
        log("MiRilHookBase, context = " + context);
    }

    public MiRilHookBase(Context context, Handler handler) {
        mContext = context;
        log("MiRilHookBase, context = " + context + ", handler = " + handler);
        mHandlerClient = handler;
        onInitByDefault();
    }

    private void onInitByDefault() {
        log("onInitByDefault, mContext = " + mContext + ", mHandlerClient = " + mHandlerClient);
        if (mContext == null) {
            throw new IllegalArgumentException("mContext is null");
        }
        if (mHandlerClient == null) {
            throw new IllegalArgumentException("mHandlerClient is null");
        }
        log("onInitByDefault, Calling onMiRilHookReady");
        Message msg = Message.obtain();
        msg.what = -1;
        mHandlerClient.sendMessage(msg);
    }

    public void dispose() {
        log("dispose, mContext = " + mContext + ", mHandlerClient = " + mHandlerClient);
        if (mContext != null && mHandlerClient != null) {
            Message msg = Message.obtain();
            msg.what = -2;
            mHandlerClient.sendMessage(msg);
            mContext = null;
            mHandlerClient = null;
        }
    }

    public int getMcfgRfsTimerTimeoutMaxNum() {
        log("getMcfgRfsTimerTimeoutMaxNum");
        return -1;
    }

    public ByteBuffer onHookPropSetSync(String propName, String value) {
        log("onHookPropSetSync, propName = " + propName + ", value = " + value);
        return null;
    }

    public boolean propSet(String propName, String value) {
        log("propSet, propName = " + propName + ", value = " + value);
        return false;
    }

    public String onHookPropGetSync(String propName, String defaultValue) {
        log("onHookPropGetSync, propName = " + propName + ", defaultValue = " + defaultValue);
        return defaultValue;
    }

    public String propGet(String propName, String defaultValue) {
        log("propGet, propName = " + propName + ", defaultValue = " + defaultValue);
        return defaultValue;
    }

    public String onSendAtCommandByMtkTelephonyManagerEx(String atCmd) {
        log("onSendAtCommandByMtkTelephonyManagerEx: " + atCmd);
        return null;
    }

    public void onMtkSendGroupSarDsiAtCommand(int[] dsiGroup) {
        log("onMtkSendGroupSarDsiAtCommand: " + dsiGroup);
    }

    public void onMtkSendTASarDsiAtCommand(int dsi) {
        log("onMtkSendTASarDsiAtCommand: " + dsi);
    }

    public int onMtkGetTASarDsiAtCommand() {
        log("onMtkGetTASarDsiAtCommand");
        return -1;
    }

    public void onXringSendTASarDsiAtCommand(int dsi) {
        log("onXringSendTASarDsiAtCommand: " + dsi);
    }

    public void onXringGetTASarDsiAtCommand(Message result) {
        log("onXringGetTASarDsiAtCommand: " + result);
    }

    public void onMtkSendUACAtCommand(int scene) {
        log("onMtkSendUACAtCommand: " + scene);
    }

    public void onMtkSendImbalanceAtCommand(int scene) {
        log("onMtkSendImbalanceAtCommand: " + scene);
    }

    public void onMtkSendAtCommand(String atCmd, Handler handler) {
        log("onMtkSendAtCommand: " + atCmd);
    }

    public boolean onMtkFeatureCtlCmn(int featureType, int ctlCode, int phoneId) {
        log("onMtkFeatureCtlCmn, phoneId = " + phoneId + ", ctlCode = " + ctlCode + ", featureType = " + featureType);
        return false;
    }

    public byte[] onMtkFeatureQueryCmn(int featureType, int queryCode, int phoneId) {
        log("onMtkFeatureQueryCmn, phoneId = " + phoneId + ", featureType = " + featureType + ", queryCode = " + queryCode);
        return null;
    }

    public boolean onHookDsdaCurrentStatusGetSync() {
        log("onHookDsdaCurrentStatusGetSync");
        return false;
    }

    public boolean onHookCdmaIsSupportedSync() {
        log("onHookCdmaIsSupportedSync");
        return false;
    }

    public boolean onHookDsdaIsSupportedSync() {
        log("onHookDsdaIsSupportedSync");
        return false;
    }

    public ByteBuffer onHookDiagSendWithResultSync(byte[] cmdBytes, int waitTime) {
        log("onHookDiagSendWithResultSync, cmdBytes = " + cmdBytes + ", waitTime = " + waitTime);
        return null;
    }

    public ByteBuffer onHookMbnReactivateSync(int subMask) {
        log("onHookMbnReactivateSync, subMask = " + subMask);
        return null;
    }

    public String onHookAtCmdSendSync(String at_cmd, int wait_ms, int onceRead) {
        log("onHookAtCmdSendSync, at_cmd = " + at_cmd + ", wait_ms = " + wait_ms + ", onceRead = " + onceRead);
        return null;
    }

    public String onHookAtCmdSendSync(String at_cmd, int wait_ms) {
        log("onHookAtCmdSendSync, at_cmd = " + at_cmd + ", wait_ms = " + wait_ms);
        return null;
    }

    public ByteBuffer onHookHydraDebugInfoForUserGetSync() {
        log("onHookHydraDebugInfoForUserGetSync");
        return null;
    }

    public ByteBuffer getNr5gB821MessageList(int sub) {
        log("getNr5gB821MessageList");
        return null;
    }

    public ByteBuffer getAntAsdivConfigList(int sub) {
        log("getAntAsdivConfigList");
        return null;
    }

    public ByteBuffer getNr5gRxRsrpList(int sub) {
        log("getNr5gRxRsrpList");
        return null;
    }

    public ByteBuffer getLteRxRsrpList(int sub) {
        log("getLteRxRsrpList");
        return null;
    }

    public ByteBuffer getCaasLicenseStat(int sub) {
        log("getCaasLicenseStat");
        return null;
    }

    public ByteBuffer getB80xCombinedMsgList(int sub) {
        log("getB80xCombinedMsgList");
        return null;
    }

    public ByteBuffer getB16ePuschPowerList(int sub) {
        log("getB16ePuschPowerList");
        return null;
    }

    public ByteBuffer getB884PuschPowerList(int sub) {
        log("getB884PuschPowerList");
        return null;
    }

    public ByteBuffer getB88aRachSuccessList(int sub) {
        log("getB88aRachSuccessList");
        return null;
    }

    public ByteBuffer getB88aRachFailureList(int sub) {
        log("getB88aRachFailureList");
        return null;
    }

    public ByteBuffer getB18aRlmSyncList(int sub) {
        log("getB18aRlmSyncList");
        return null;
    }

    public ByteBuffer getB18aRlmOosList(int sub) {
        log("getB18aRlmOosList");
        return null;
    }

    public ByteBuffer getLteB0c0MessageList(int sub) {
        log("getLteB0c0MessageList");
        return null;
    }

    public ByteBuffer getLteNasB0e23List(int sub) {
        log("getLteNasB0e23List");
        return null;
    }

    public ByteBuffer getLteMacRachB062List(int sub) {
        log("getLteMacRachB062List");
        return null;
    }

    public ByteBuffer getImsReg1832List(int sub) {
        log("getImsReg1832List");
        return null;
    }

    public int getMbnFeatureDefaultConfig() {
        log("getMbnFeatureDefaultConfig");
        return -1;
    }

    public ByteBuffer getMtConnFailInfo(int sub) {
        log("getMtConnFailInfo");
        return null;
    }

    public ByteBuffer getMtConnFailInfoList(int sub) {
        log("getMtConnFailInfoList");
        return null;
    }

    public ByteBuffer getVoiceQualityInfoList(int sub) {
        log("getVoiceQualityInfoList");
        return null;
    }

    public ByteBuffer getLowMosScoreInfoList(int sub) {
        log("getLowMosScoreInfoList");
        return null;
    }

    public int getSarDsi() {
        log("getSarDsi");
        return -1;
    }

    public boolean isModemFeatureSupported(int featureType) {
        log("isModemFeatureSupported, featureType = " + featureType);
        return false;
    }

    public boolean modemCrash(int type) {
        log("modemCrash, type = " + type);
        return false;
    }

    public boolean modemSSR(int type) {
        log("modemSSR, type = " + type);
        return false;
    }

    public boolean modemDump(int type) {
        log("modemDump, type = " + type);
        return false;
    }

    public boolean qcRilFeatureCtlCmn(int featureType, int ctlCode, int phoneId) {
        log("qcRilFeatureCtlCmn, featureType = " + featureType + ", ctlCode = " + ctlCode + ", phoneId = " + phoneId);
        return false;
    }

    public boolean isCardSlotPresent() {
        log("isCardSlotPresent");
        return false;
    }

    public boolean registerOemHookCallbackForMiBroadcastDataRegistrant(String packageName, Registrant registrant, String bcName, byte[] payload, int phoneId) {
        log("registerOemHookCallbackForMiBroadcastDataRegistrant, packageName = " + packageName + ", registrant = " + registrant + ", bcName = " + bcName + ", payload = " + payload + ", phoneId = " + phoneId);
        return false;
    }

    public boolean unRegisterOemHookCallbackForMiBroadcastDataRegistrant(String packageName, Registrant registrant) {
        log("unRegisterOemHookCallbackForMiBroadcastDataRegistrant, packageName = " + packageName + ", registrant = " + registrant);
        return false;
    }

    public int getNr5gBand(int sub) {
        log("getNr5gBand, sub = " + sub);
        return -1;
    }

    public int getLteBand(int sub) {
        log("getLteBand, sub = " + sub);
        return -1;
    }

    public ByteBuffer getLteMeasInfo(int sub) {
        log("getLteMeasInfo, sub = " + sub);
        return null;
    }

    public ByteBuffer getNr5gMeasInfo(int sub) {
        log("getNr5gMeasInfo, sub = " + sub);
        return null;
    }

    public ByteBuffer onHookCommonMsgSyncBySub(int msg_id, byte[] bytes, int phoneId) {
        log("onHookCommonMsgSyncBySub, msg_id = " + msg_id + ", bytes = " + bytes + ", phoneId = " + phoneId);
        return null;
    }

    public boolean onHookCommonMsgBySub(int msg_id, byte[] bytes, int phoneId, Message msg) {
        log("onHookCommonMsgBySub, msg_id = " + msg_id + ", bytes = " + bytes + ", phoneId = " + phoneId + ", msg = " + msg);
        return false;
    }

    public boolean efsSync() {
        log("efsSync");
        return false;
    }

    public boolean efsWrite(int sub, String path, byte[] data) {
        log("efsWrite, sub = " + sub + ", path = " + path + ", data = " + data);
        return false;
    }

    public byte[] efsReadBytes(int sub, String path) {
        log("efsReadBytes, sub = " + sub + ", path = " + path);
        return null;
    }

    public ByteBuffer efsReadByteBuffer(int sub, String path) {
        log("efsReadByteBuffer, sub = " + sub + ", path = " + path);
        return null;
    }

    public ByteBuffer onHookEfsWriteSync(int sub, String path, byte[] data) {
        log("onHookEfsWriteSync, sub = " + sub + ", path = " + path + ", data = " + data);
        return null;
    }

    public byte[] nvReadBytes(int sub, int nvId) {
        log("nvReadBytes, sub = " + sub + ", nvId = " + nvId);
        return null;
    }

    public ByteBuffer onHookNvOptSync(int sub, int nvId, int hook_id) {
        log("onHookNvOptSync, sub = " + sub + ", nvId = " + nvId + ", hook_id = " + hook_id);
        return null;
    }

    public ByteBuffer onHookNvWriteSync(int sub, int nvId, byte[] data) {
        log("onHookNvWriteSync, sub = " + sub + ", nvId = " + nvId + ", data = " + data);
        return null;
    }

    public ByteBuffer onHookNvWriteSync(int sub, int nvId, byte[] data, int dataLen) {
        log("onHookNvWriteSync, sub = " + sub + ", nvId = " + nvId + ", data = " + data + ", dataLen = " + dataLen);
        return null;
    }

    public ByteBuffer onHookBigEfsReadSync(int sub, String path) {
        log("onHookBigEfsReadSync, sub = " + sub + ", path = " + path);
        return null;
    }

    public ByteBuffer onHookDiagSendSync(byte[] cmdBytes) {
        log("onHookDiagSendSync, cmdBytes = " + cmdBytes);
        return null;
    }

    public ByteBuffer onHookModSwVerGetSync() {
        log("onHookModSwVerGetSync");
        return null;
    }

    public ByteBuffer onHookHydraStatsSetSync(String packageName, int swState) {
        log("onHookHydraStatsSetSync, packageName = " + packageName + ", swState = " + swState);
        return null;
    }

    public boolean isOemProduct() {
        log("isOemProduct");
        return false;
    }

    public boolean isHalDecoupled() {
        log("isHalDecoupled");
        return false;
    }

    public int getXiaomiManufacturerId() {
        log("getXiaomiManufacturerId");
        return 0;
    }

    public int getXiaomiModemBpChipId() {
        log("getXiaomiModemBpChipId");
        return 0;
    }

    public ByteBuffer onHookCommonMsgSync(int msg_id, int iArg1) {
        log("onHookCommonMsgSync, msg_id = " + msg_id + ", iArg1 = " + iArg1);
        return null;
    }

    public ByteBuffer onHookCommonMsgSync(int msg_id) {
        log("onHookCommonMsgSync, msg_id = " + msg_id);
        return null;
    }

    public ByteBuffer onHookCommonMsgSync(int msg_id, byte[] bytes) {
        log("onHookCommonMsgSync, msg_id = " + msg_id + ", bytes = " + bytes);
        return null;
    }

    public ByteBuffer onHookCommonMsgSync(int msg_id, int iArg1, int iArg2) {
        log("onHookCommonMsgSync, msg_id = " + msg_id + ", iArg1 = " + iArg1 + ", iArg2 = " + iArg2);
        return null;
    }

    public boolean resetModemStats() {
        log("resetModemStats");
        return false;
    }

    public boolean startModemStats() {
        log("startModemStats");
        return false;
    }

    public boolean closeModemStats() {
        log("closeModemStats");
        return false;
    }

    public ByteBuffer onHookEfsOptSync(int sub, String path, int hook_id) {
        log("onHookEfsOptSync, sub = " + sub + ", path = " + path + ", hook_id = " + hook_id);
        return null;
    }

    public boolean onHookCommonMsg(int msg_id) {
        log("onHookCommonMsg, msg_id = " + msg_id);
        return false;
    }

    public boolean onHookCommonMsg(int msg_id, int iArg1) {
        log("onHookCommonMsg, msg_id = " + msg_id + ", iArg1 = " + iArg1);
        return false;
    }

    public boolean onHookCommonMsg(int msg_id, int iArg1, int iArg2) {
        log("onHookCommonMsg, msg_id = " + msg_id + ", iArg1 = " + iArg1 + ", iArg2 = " + iArg2);
        return false;
    }

    public boolean onHookCommonMsg(int msg_id, int iArg1, int iArg2, int iArg3) {
        log("onHookCommonMsg, msg_id = " + msg_id + ", iArg1 = " + iArg1 + ", iArg2 = " + iArg2 + ", iArg3 = " + iArg3);
        return false;
    }

    public boolean onHookPropSet(String propName, String value) {
        log("onHookPropSet, propName = " + propName + ", value = " + value);
        return false;
    }

    public boolean onHookCommonMsgBySub(int msg_id, int phoneId) {
        log("onHookCommonMsgBySub, msg_id = " + msg_id + ", phoneId = " + phoneId);
        return false;
    }

    public ByteBuffer getGameAiInfo() {
        log("getGameAiInfo");
        return null;
    }

    public ByteBuffer getSignalAiInfo() {
        log("getSignalAiInfo");
        return null;
    }

    public boolean setSarDsi(int dsi) {
        log("setSarDsi, dsi = " + dsi);
        return false;
    }

    public boolean registerOemHookRegistrant(Registrant registrant, String bcName) {
        log("registerOemHookRegistrant, registrant = " + registrant + ", bcName = " + bcName);
        return false;
    }

    public boolean unRegisterOemHookRegistrant(Registrant registrant) {
        log("unRegisterOemHookRegistrant, registrant = " + registrant);
        return false;
    }

    public boolean onHookNotifyPowerkeeperSleepStatus(int status) {
        log("unRegisterOemHookRegistrant, status = " + status);
        return false;
    }

    public int getModemSleepState() {
        log("getModemSleepState");
        return -1;
    }

    public void onRegisterAtUrcInd(int slotId, int eventUrc, Handler handler) {
        log("onRegisterAtUrcInd, slotId = " + slotId + ", eventUrc = " + eventUrc);
    }

    public ByteBuffer onHookMiBroadcastStateSetSync(int bc_type, int bc_state) {
        log("onHookMiBroadcastStateSetSync, bc_type = " + bc_type + ", bc_state = " + bc_state);
        return null;
    }

    public ByteBuffer onHookModemSceneRecognitionReqSync(byte[] bytes) {
        log("onHookModemSceneRecognitionReqSync, bytes = " + bytes);
        return null;
    }

    public boolean registerOemHookRegistrant(Registrant registrant, String bcName, byte[] payload, int phoneId) {
        log("registerOemHookRegistrant, registrant = " + registrant + ", bcName = " + bcName + ", payload = " + payload + ", phoneId = " + phoneId);
        return false;
    }

    public ByteBuffer getB193LteSrvMeas1sCollect(int sub) {
        log("getB193LteSrvMeas1sCollect, sub = " + sub);
        return null;
    }

    public ByteBuffer getB17fLteMl1SrvCellMeasHistory(int sub) {
        log("getB17fLteMl1SrvCellMeasHistory, sub = " + sub);
        return null;
    }

    public ByteBuffer getB176LteInitialAcqResult(int sub) {
        log("getB176LteInitialAcqResult, sub = " + sub);
        return null;
    }

    public ByteBuffer getB18eLteMl1SystemScanResults(int sub) {
        log("getB18eLteMl1SystemScanResults, sub = " + sub);
        return null;
    }

    public ByteBuffer getLteNasEmmPlainOtaOutMsg(int sub) {
        log("getLteNasEmmPlainOtaOutMsg, sub = " + sub);
        return null;
    }

    public ByteBuffer getLteNasEmmPlainOtaInMsg(int sub) {
        log("getLteNasEmmPlainOtaInMsg, sub = " + sub);
        return null;
    }

    public ByteBuffer getB80abCombinedMsgList(int sub) {
        log("getB80abCombinedMsgList");
        return null;
    }

    public ByteBuffer getB970Nr5gMl1SuccessList(int sub) {
        log("getB970Nr5gMl1SuccessList");
        return null;
    }

    public ByteBuffer getB970Nr5gMl1FailureList(int sub) {
        log("getB970Nr5gMl1FailureList");
        return null;
    }

    public ByteBuffer getB97fNr5GMl1SearcherMsgList(int sub) {
        log("getB97fNr5GMl1SearcherMsgList");
        return null;
    }

    public ByteBuffer getB822Nr5gRrcMibList(int sub) {
        log("getB822Nr5gRrcMibList");
        return null;
    }

    public int getNr5gRrcState(int sub) {
        log("getNr5gRrcState, sub = " + sub);
        return -1;
    }

    public ByteBuffer getB96dNr5gMl1SuccessList(int sub) {
        log("getB96dNr5gMl1SuccessList");
        return null;
    }

    public ByteBuffer getB96dNr5gMl1FailureList(int sub) {
        log("getB96dNr5gMl1FailureList");
        return null;
    }

    private static void log(String msg) {
        ModemUtils.logd(LOG_TAG, msg);
    }
}
