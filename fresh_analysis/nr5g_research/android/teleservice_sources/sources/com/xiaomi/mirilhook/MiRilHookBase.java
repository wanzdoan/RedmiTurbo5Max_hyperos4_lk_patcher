package com.xiaomi.mirilhook;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.android.internal.telephony.Registrant;
import com.xiaomi.modem.ModemUtils;
import java.nio.ByteBuffer;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

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
    protected static Context mContext;
    protected static Handler mHandlerClient;
    protected static int mHookAndroidVersion;

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
            Folme$$ExternalSyntheticBUOutline0.m("mContext is null");
            return;
        }
        if (mHandlerClient == null) {
            Folme$$ExternalSyntheticBUOutline0.m("mHandlerClient is null");
            return;
        }
        log("onInitByDefault, Calling onMiRilHookReady");
        Message messageObtain = Message.obtain();
        messageObtain.what = -1;
        mHandlerClient.sendMessage(messageObtain);
    }

    public void dispose() {
        log("dispose, mContext = " + mContext + ", mHandlerClient = " + mHandlerClient);
        if (mContext == null || mHandlerClient == null) {
            return;
        }
        Message messageObtain = Message.obtain();
        messageObtain.what = -2;
        mHandlerClient.sendMessage(messageObtain);
        mContext = null;
        mHandlerClient = null;
    }

    public int getMcfgRfsTimerTimeoutMaxNum() {
        log("getMcfgRfsTimerTimeoutMaxNum");
        return -1;
    }

    public ByteBuffer onHookPropSetSync(String str, String str2) {
        log("onHookPropSetSync, propName = " + str + ", value = " + str2);
        return null;
    }

    public boolean propSet(String str, String str2) {
        log("propSet, propName = " + str + ", value = " + str2);
        return false;
    }

    public String onHookPropGetSync(String str, String str2) {
        log("onHookPropGetSync, propName = " + str + ", defaultValue = " + str2);
        return str2;
    }

    public String propGet(String str, String str2) {
        log("propGet, propName = " + str + ", defaultValue = " + str2);
        return str2;
    }

    public String onSendAtCommandByMtkTelephonyManagerEx(String str) {
        log("onSendAtCommandByMtkTelephonyManagerEx: " + str);
        return null;
    }

    public void onMtkSendGroupSarDsiAtCommand(int[] iArr) {
        log("onMtkSendGroupSarDsiAtCommand: " + iArr);
    }

    public void onMtkSendTASarDsiAtCommand(int i) {
        log("onMtkSendTASarDsiAtCommand: " + i);
    }

    public int onMtkGetTASarDsiAtCommand() {
        log("onMtkGetTASarDsiAtCommand");
        return -1;
    }

    public void onXringSendTASarDsiAtCommand(int i) {
        log("onXringSendTASarDsiAtCommand: " + i);
    }

    public void onXringGetTASarDsiAtCommand(Message message) {
        log("onXringGetTASarDsiAtCommand: " + message);
    }

    public void onMtkSendUACAtCommand(int i) {
        log("onMtkSendUACAtCommand: " + i);
    }

    public void onMtkSendImbalanceAtCommand(int i) {
        log("onMtkSendImbalanceAtCommand: " + i);
    }

    public void onMtkSendAtCommand(String str, Handler handler) {
        log("onMtkSendAtCommand: " + str);
    }

    public boolean onMtkFeatureCtlCmn(int i, int i2, int i3) {
        log("onMtkFeatureCtlCmn, phoneId = " + i3 + ", ctlCode = " + i2 + ", featureType = " + i);
        return false;
    }

    public byte[] onMtkFeatureQueryCmn(int i, int i2, int i3) {
        log("onMtkFeatureQueryCmn, phoneId = " + i3 + ", featureType = " + i + ", queryCode = " + i2);
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

    public ByteBuffer onHookDiagSendWithResultSync(byte[] bArr, int i) {
        log("onHookDiagSendWithResultSync, cmdBytes = " + bArr + ", waitTime = " + i);
        return null;
    }

    public ByteBuffer onHookMbnReactivateSync(int i) {
        log("onHookMbnReactivateSync, subMask = " + i);
        return null;
    }

    public String onHookAtCmdSendSync(String str, int i, int i2) {
        log("onHookAtCmdSendSync, at_cmd = " + str + ", wait_ms = " + i + ", onceRead = " + i2);
        return null;
    }

    public String onHookAtCmdSendSync(String str, int i) {
        log("onHookAtCmdSendSync, at_cmd = " + str + ", wait_ms = " + i);
        return null;
    }

    public ByteBuffer onHookHydraDebugInfoForUserGetSync() {
        log("onHookHydraDebugInfoForUserGetSync");
        return null;
    }

    public ByteBuffer getNr5gB821MessageList(int i) {
        log("getNr5gB821MessageList");
        return null;
    }

    public ByteBuffer getAntAsdivConfigList(int i) {
        log("getAntAsdivConfigList");
        return null;
    }

    public ByteBuffer getNr5gRxRsrpList(int i) {
        log("getNr5gRxRsrpList");
        return null;
    }

    public ByteBuffer getLteRxRsrpList(int i) {
        log("getLteRxRsrpList");
        return null;
    }

    public ByteBuffer getCaasLicenseStat(int i) {
        log("getCaasLicenseStat");
        return null;
    }

    public ByteBuffer getB80xCombinedMsgList(int i) {
        log("getB80xCombinedMsgList");
        return null;
    }

    public ByteBuffer getB16ePuschPowerList(int i) {
        log("getB16ePuschPowerList");
        return null;
    }

    public ByteBuffer getB884PuschPowerList(int i) {
        log("getB884PuschPowerList");
        return null;
    }

    public ByteBuffer getB88aRachSuccessList(int i) {
        log("getB88aRachSuccessList");
        return null;
    }

    public ByteBuffer getB88aRachFailureList(int i) {
        log("getB88aRachFailureList");
        return null;
    }

    public ByteBuffer getB18aRlmSyncList(int i) {
        log("getB18aRlmSyncList");
        return null;
    }

    public ByteBuffer getB18aRlmOosList(int i) {
        log("getB18aRlmOosList");
        return null;
    }

    public ByteBuffer getLteB0c0MessageList(int i) {
        log("getLteB0c0MessageList");
        return null;
    }

    public ByteBuffer getLteNasB0e23List(int i) {
        log("getLteNasB0e23List");
        return null;
    }

    public ByteBuffer getLteMacRachB062List(int i) {
        log("getLteMacRachB062List");
        return null;
    }

    public ByteBuffer getImsReg1832List(int i) {
        log("getImsReg1832List");
        return null;
    }

    public int getMbnFeatureDefaultConfig() {
        log("getMbnFeatureDefaultConfig");
        return -1;
    }

    public ByteBuffer getMtConnFailInfo(int i) {
        log("getMtConnFailInfo");
        return null;
    }

    public ByteBuffer getMtConnFailInfoList(int i) {
        log("getMtConnFailInfoList");
        return null;
    }

    public ByteBuffer getVoiceQualityInfoList(int i) {
        log("getVoiceQualityInfoList");
        return null;
    }

    public ByteBuffer getLowMosScoreInfoList(int i) {
        log("getLowMosScoreInfoList");
        return null;
    }

    public int getSarDsi() {
        log("getSarDsi");
        return -1;
    }

    public boolean isModemFeatureSupported(int i) {
        log("isModemFeatureSupported, featureType = " + i);
        return false;
    }

    public boolean modemCrash(int i) {
        log("modemCrash, type = " + i);
        return false;
    }

    public boolean modemSSR(int i) {
        log("modemSSR, type = " + i);
        return false;
    }

    public boolean modemDump(int i) {
        log("modemDump, type = " + i);
        return false;
    }

    public boolean qcRilFeatureCtlCmn(int i, int i2, int i3) {
        log("qcRilFeatureCtlCmn, featureType = " + i + ", ctlCode = " + i2 + ", phoneId = " + i3);
        return false;
    }

    public boolean isCardSlotPresent() {
        log("isCardSlotPresent");
        return false;
    }

    public boolean registerOemHookCallbackForMiBroadcastDataRegistrant(String str, Registrant registrant, String str2, byte[] bArr, int i) {
        log("registerOemHookCallbackForMiBroadcastDataRegistrant, packageName = " + str + ", registrant = " + registrant + ", bcName = " + str2 + ", payload = " + bArr + ", phoneId = " + i);
        return false;
    }

    public boolean unRegisterOemHookCallbackForMiBroadcastDataRegistrant(String str, Registrant registrant) {
        log("unRegisterOemHookCallbackForMiBroadcastDataRegistrant, packageName = " + str + ", registrant = " + registrant);
        return false;
    }

    public int getNr5gBand(int i) {
        log("getNr5gBand, sub = " + i);
        return -1;
    }

    public int getLteBand(int i) {
        log("getLteBand, sub = " + i);
        return -1;
    }

    public ByteBuffer getLteMeasInfo(int i) {
        log("getLteMeasInfo, sub = " + i);
        return null;
    }

    public ByteBuffer getNr5gMeasInfo(int i) {
        log("getNr5gMeasInfo, sub = " + i);
        return null;
    }

    public ByteBuffer onHookCommonMsgSyncBySub(int i, byte[] bArr, int i2) {
        log("onHookCommonMsgSyncBySub, msg_id = " + i + ", bytes = " + bArr + ", phoneId = " + i2);
        return null;
    }

    public boolean onHookCommonMsgBySub(int i, byte[] bArr, int i2, Message message) {
        log("onHookCommonMsgBySub, msg_id = " + i + ", bytes = " + bArr + ", phoneId = " + i2 + ", msg = " + message);
        return false;
    }

    public boolean efsSync() {
        log("efsSync");
        return false;
    }

    public boolean efsWrite(int i, String str, byte[] bArr) {
        log("efsWrite, sub = " + i + ", path = " + str + ", data = " + bArr);
        return false;
    }

    public byte[] efsReadBytes(int i, String str) {
        log("efsReadBytes, sub = " + i + ", path = " + str);
        return null;
    }

    public ByteBuffer efsReadByteBuffer(int i, String str) {
        log("efsReadByteBuffer, sub = " + i + ", path = " + str);
        return null;
    }

    public ByteBuffer onHookEfsWriteSync(int i, String str, byte[] bArr) {
        log("onHookEfsWriteSync, sub = " + i + ", path = " + str + ", data = " + bArr);
        return null;
    }

    public byte[] nvReadBytes(int i, int i2) {
        log("nvReadBytes, sub = " + i + ", nvId = " + i2);
        return null;
    }

    public ByteBuffer onHookNvOptSync(int i, int i2, int i3) {
        log("onHookNvOptSync, sub = " + i + ", nvId = " + i2 + ", hook_id = " + i3);
        return null;
    }

    public ByteBuffer onHookNvWriteSync(int i, int i2, byte[] bArr) {
        log("onHookNvWriteSync, sub = " + i + ", nvId = " + i2 + ", data = " + bArr);
        return null;
    }

    public ByteBuffer onHookNvWriteSync(int i, int i2, byte[] bArr, int i3) {
        log("onHookNvWriteSync, sub = " + i + ", nvId = " + i2 + ", data = " + bArr + ", dataLen = " + i3);
        return null;
    }

    public ByteBuffer onHookBigEfsReadSync(int i, String str) {
        log("onHookBigEfsReadSync, sub = " + i + ", path = " + str);
        return null;
    }

    public ByteBuffer onHookDiagSendSync(byte[] bArr) {
        log("onHookDiagSendSync, cmdBytes = " + bArr);
        return null;
    }

    public ByteBuffer onHookModSwVerGetSync() {
        log("onHookModSwVerGetSync");
        return null;
    }

    public ByteBuffer onHookHydraStatsSetSync(String str, int i) {
        log("onHookHydraStatsSetSync, packageName = " + str + ", swState = " + i);
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

    public ByteBuffer onHookCommonMsgSync(int i, int i2) {
        log("onHookCommonMsgSync, msg_id = " + i + ", iArg1 = " + i2);
        return null;
    }

    public ByteBuffer onHookCommonMsgSync(int i) {
        log("onHookCommonMsgSync, msg_id = " + i);
        return null;
    }

    public ByteBuffer onHookCommonMsgSync(int i, byte[] bArr) {
        log("onHookCommonMsgSync, msg_id = " + i + ", bytes = " + bArr);
        return null;
    }

    public ByteBuffer onHookCommonMsgSync(int i, int i2, int i3) {
        log("onHookCommonMsgSync, msg_id = " + i + ", iArg1 = " + i2 + ", iArg2 = " + i3);
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

    public ByteBuffer onHookEfsOptSync(int i, String str, int i2) {
        log("onHookEfsOptSync, sub = " + i + ", path = " + str + ", hook_id = " + i2);
        return null;
    }

    public boolean onHookCommonMsg(int i) {
        log("onHookCommonMsg, msg_id = " + i);
        return false;
    }

    public boolean onHookCommonMsg(int i, int i2) {
        log("onHookCommonMsg, msg_id = " + i + ", iArg1 = " + i2);
        return false;
    }

    public boolean onHookCommonMsg(int i, int i2, int i3) {
        log("onHookCommonMsg, msg_id = " + i + ", iArg1 = " + i2 + ", iArg2 = " + i3);
        return false;
    }

    public boolean onHookCommonMsg(int i, int i2, int i3, int i4) {
        log("onHookCommonMsg, msg_id = " + i + ", iArg1 = " + i2 + ", iArg2 = " + i3 + ", iArg3 = " + i4);
        return false;
    }

    public boolean onHookPropSet(String str, String str2) {
        log("onHookPropSet, propName = " + str + ", value = " + str2);
        return false;
    }

    public boolean onHookCommonMsgBySub(int i, int i2) {
        log("onHookCommonMsgBySub, msg_id = " + i + ", phoneId = " + i2);
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

    public boolean setSarDsi(int i) {
        log("setSarDsi, dsi = " + i);
        return false;
    }

    public boolean registerOemHookRegistrant(Registrant registrant, String str) {
        log("registerOemHookRegistrant, registrant = " + registrant + ", bcName = " + str);
        return false;
    }

    public boolean unRegisterOemHookRegistrant(Registrant registrant) {
        log("unRegisterOemHookRegistrant, registrant = " + registrant);
        return false;
    }

    public boolean onHookNotifyPowerkeeperSleepStatus(int i) {
        log("unRegisterOemHookRegistrant, status = " + i);
        return false;
    }

    public int getModemSleepState() {
        log("getModemSleepState");
        return -1;
    }

    public void onRegisterAtUrcInd(int i, int i2, Handler handler) {
        log("onRegisterAtUrcInd, slotId = " + i + ", eventUrc = " + i2);
    }

    public ByteBuffer onHookMiBroadcastStateSetSync(int i, int i2) {
        log("onHookMiBroadcastStateSetSync, bc_type = " + i + ", bc_state = " + i2);
        return null;
    }

    public ByteBuffer onHookModemSceneRecognitionReqSync(byte[] bArr) {
        log("onHookModemSceneRecognitionReqSync, bytes = " + bArr);
        return null;
    }

    public boolean registerOemHookRegistrant(Registrant registrant, String str, byte[] bArr, int i) {
        log("registerOemHookRegistrant, registrant = " + registrant + ", bcName = " + str + ", payload = " + bArr + ", phoneId = " + i);
        return false;
    }

    public ByteBuffer getB193LteSrvMeas1sCollect(int i) {
        log("getB193LteSrvMeas1sCollect, sub = " + i);
        return null;
    }

    public ByteBuffer getB17fLteMl1SrvCellMeasHistory(int i) {
        log("getB17fLteMl1SrvCellMeasHistory, sub = " + i);
        return null;
    }

    public ByteBuffer getB176LteInitialAcqResult(int i) {
        log("getB176LteInitialAcqResult, sub = " + i);
        return null;
    }

    public ByteBuffer getB18eLteMl1SystemScanResults(int i) {
        log("getB18eLteMl1SystemScanResults, sub = " + i);
        return null;
    }

    public ByteBuffer getLteNasEmmPlainOtaOutMsg(int i) {
        log("getLteNasEmmPlainOtaOutMsg, sub = " + i);
        return null;
    }

    public ByteBuffer getLteNasEmmPlainOtaInMsg(int i) {
        log("getLteNasEmmPlainOtaInMsg, sub = " + i);
        return null;
    }

    public ByteBuffer getB80abCombinedMsgList(int i) {
        log("getB80abCombinedMsgList");
        return null;
    }

    public ByteBuffer getB970Nr5gMl1SuccessList(int i) {
        log("getB970Nr5gMl1SuccessList");
        return null;
    }

    public ByteBuffer getB970Nr5gMl1FailureList(int i) {
        log("getB970Nr5gMl1FailureList");
        return null;
    }

    public ByteBuffer getB97fNr5GMl1SearcherMsgList(int i) {
        log("getB97fNr5GMl1SearcherMsgList");
        return null;
    }

    public ByteBuffer getB822Nr5gRrcMibList(int i) {
        log("getB822Nr5gRrcMibList");
        return null;
    }

    public int getNr5gRrcState(int i) {
        log("getNr5gRrcState, sub = " + i);
        return -1;
    }

    public ByteBuffer getB96dNr5gMl1SuccessList(int i) {
        log("getB96dNr5gMl1SuccessList");
        return null;
    }

    public ByteBuffer getB96dNr5gMl1FailureList(int i) {
        log("getB96dNr5gMl1FailureList");
        return null;
    }

    private static void log(String str) {
        ModemUtils.logd(LOG_TAG, str);
    }
}
