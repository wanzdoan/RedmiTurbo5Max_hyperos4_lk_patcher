package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import com.android.internal.telephony.utils.XmPhoneUtils;
import com.android.telephony.Rlog;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class XmRILProxy implements XmPhoneConstants {
    private static final int EVENT_COMMON_MESSAGE_RESPONSE = 0;
    private static final int EVENT_DEPRIORITIZE_NR5G_RESPONSE = 6;
    private static final int EVENT_ENABLE_ENDC_RESPONSE = 8;
    private static final int EVENT_GET_DDS_ASSISTANT_INFO_RESPONSE = 15;
    private static final int EVENT_GET_MODEM_REASON_RESPONSE = 17;
    private static final int EVENT_GET_MODEM_SCENE_RESPONSE = 16;
    private static final int EVENT_GET_SAR_DSI_GROUP_RESPONSE = 5;
    private static final int EVENT_GET_SAR_DSI_RESPONSE = 4;
    private static final int EVENT_PROMOTE_BSR_RESPONSE = 14;
    private static final int EVENT_SET_5G_NETWORK_MODE_RESPONSE = 7;
    private static final int EVENT_SET_DATA_STALL_MODE_RESPONSE = 11;
    private static final int EVENT_SET_FIVE_TUPLE_BLACK_LIST_RESPONSE = 23;
    private static final int EVENT_SET_MOBILITY_OPT_PARAMS_RESPONSE = 12;
    private static final int EVENT_SET_MODEM_CALLBACK_SWITCH_RESPONSE = 19;
    private static final int EVENT_SET_MODEM_EVENT_IND_SWITCH_RESPONSE = 20;
    private static final int EVENT_SET_MODEM_MODE_CONFIG_RESPONSE = 18;
    private static final int EVENT_SET_PREFER_BAND_PARAMS_RESPONSE = 13;
    private static final int EVENT_SET_SAR_DSI_GROUP_RESPONSE = 3;
    private static final int EVENT_SET_SAR_DSI_RESPONSE = 2;
    private static final int EVENT_UNSOL_COMMON_MESSAGE = 1;
    private static final int EVENT_UNSOL_HIGH_SPEED_TRAIN_STATE = 9;
    private static final int EVENT_UNSOL_MODEM_EXCEPTION = 21;
    private static final int EVENT_UNSOL_MODEM_SCENE = 22;
    private static final int EVENT_UNSOL_QOS_STATE = 10;
    private static final String LOG_TAG = "XmRILProxy";
    private XmPhone mPhone;
    private int mPhoneId;
    private Handler mXmPhoneHandler = new Handler(XmPhoneUtils.getInstance().getLooper()) { // from class: com.android.internal.telephony.XmRILProxy.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int[] sarDsi;
            XmRILProxy.this.logi("handleMessage: msgId = " + msg.what);
            AsyncResult ar = (AsyncResult) msg.obj;
            Object resultReceiver = ar.userObj;
            int resultCode = ar.exception != null ? -1 : 0;
            switch (msg.what) {
                case 0:
                    Bundle bundle = (Bundle) ar.result;
                    XmRILProxy.this.logd("resultReceiver = " + resultReceiver + ", bundle = " + bundle);
                    if ((resultReceiver instanceof ResultReceiver) && bundle != null) {
                        int commMsgId = bundle.getInt("message_id", -1);
                        if (commMsgId == 4) {
                            ((ResultReceiver) resultReceiver).send(resultCode, XmRILUtils.parseIntArrayBundle(resultCode, bundle, XmPhoneConstants.DDS_ASSISTANT_INFO));
                        } else if (commMsgId == 5) {
                            ((ResultReceiver) resultReceiver).send(resultCode, XmRILUtils.parseIntArrayBundle(resultCode, bundle, XmPhoneConstants.MODEM_SCENE_INFO));
                        } else if (commMsgId == 6) {
                            ((ResultReceiver) resultReceiver).send(resultCode, XmRILUtils.parseIntArrayBundle(resultCode, bundle, XmPhoneConstants.MODEM_REASON_INFO));
                        } else if (commMsgId == 7 || commMsgId == 8 || commMsgId == 9) {
                            Bundle resultBundle = new Bundle();
                            resultBundle.putInt("phone_id", XmRILProxy.this.mPhoneId);
                            ((ResultReceiver) resultReceiver).send(resultCode, resultBundle);
                        } else {
                            XmRILProxy xmRILProxy = XmRILProxy.this;
                            if (commMsgId == 13) {
                                xmRILProxy.handleFiveTupleBlackListResponse(ar, resultCode, (ResultReceiver) resultReceiver);
                            } else {
                                bundle.putInt("phone_id", xmRILProxy.mPhoneId);
                                ((ResultReceiver) resultReceiver).send(resultCode, bundle);
                            }
                        }
                        break;
                    }
                    break;
                case 1:
                    if (ar.exception == null) {
                        Bundle bundle2 = (Bundle) ar.result;
                        int msgId = bundle2.getInt("message_id", -1);
                        byte[] data = bundle2.getByteArray("byte_array");
                        XmRILProxy.this.logd("msgId = " + msgId + ", dataLen = " + data.length);
                        XmRILProxy.this.mPhone.notifyUsolCommonMsg(msgId, data);
                    }
                    break;
                case 2:
                case 3:
                    if (resultReceiver instanceof ResultReceiver) {
                        ((ResultReceiver) resultReceiver).send(resultCode, null);
                    }
                    break;
                case 4:
                    Bundle bundle3 = new Bundle();
                    if (resultReceiver instanceof ResultReceiver) {
                        if (resultCode == 0 && (ar.result instanceof int[]) && (sarDsi = (int[]) ar.result) != null && sarDsi.length > 0) {
                            bundle3.putInt(XmPhoneConstants.SAR_DSI, sarDsi[0]);
                        }
                        ((ResultReceiver) resultReceiver).send(resultCode, bundle3);
                    }
                    break;
                case 5:
                    Bundle bundle4 = new Bundle();
                    if (resultReceiver instanceof ResultReceiver) {
                        if (resultCode == 0 && (ar.result instanceof int[])) {
                            bundle4.putIntArray(XmPhoneConstants.SAR_DSI, (int[]) ar.result);
                        }
                        ((ResultReceiver) resultReceiver).send(resultCode, bundle4);
                    }
                    break;
                case 6:
                case 7:
                case 8:
                    Bundle bundle5 = msg.getData();
                    if (resultReceiver instanceof ResultReceiver) {
                        bundle5.putInt("phone_id", XmRILProxy.this.mPhoneId);
                        ((ResultReceiver) resultReceiver).send(resultCode, bundle5);
                    }
                    break;
                case 9:
                    if (ar.exception == null) {
                        Bundle bundle6 = (Bundle) ar.result;
                        boolean hstState = bundle6.getBoolean("hst_state", false);
                        int hstRat = bundle6.getInt("hst_sat", -1);
                        XmRILProxy.this.logd("hstState = " + hstState + ", hstRat = " + hstRat);
                        XmRILProxy.this.mPhone.notifyHstState(hstState, hstRat);
                    }
                    break;
                case 10:
                    if (ar.exception == null) {
                        Bundle bundle7 = (Bundle) ar.result;
                        int qosId = bundle7.getInt("qos_id", -1);
                        XmRILProxy.this.logd("qosState = " + qosId);
                        XmRILProxy.this.mPhone.notifyQosState(qosId, bundle7);
                    }
                    break;
                case 11:
                case 12:
                case 13:
                case 14:
                    Bundle bundle8 = new Bundle();
                    if (resultReceiver instanceof ResultReceiver) {
                        bundle8.putInt("phone_id", XmRILProxy.this.mPhoneId);
                        ((ResultReceiver) resultReceiver).send(resultCode, bundle8);
                    }
                    break;
                case 15:
                    Bundle bundle9 = new Bundle();
                    if (resultReceiver instanceof ResultReceiver) {
                        if (resultCode == 0 && (ar.result instanceof int[])) {
                            int[] ddsInfo = (int[]) ar.result;
                            bundle9.putIntArray(XmPhoneConstants.DDS_ASSISTANT_INFO, ddsInfo);
                        }
                        ((ResultReceiver) resultReceiver).send(resultCode, bundle9);
                    }
                    break;
                case 16:
                    Bundle bundle10 = new Bundle();
                    if (resultReceiver instanceof ResultReceiver) {
                        if (resultCode == 0 && (ar.result instanceof int[])) {
                            int[] modemScene = (int[]) ar.result;
                            bundle10.putIntArray(XmPhoneConstants.MODEM_SCENE_INFO, modemScene);
                        }
                        ((ResultReceiver) resultReceiver).send(resultCode, bundle10);
                    }
                    break;
                case 17:
                    Bundle bundle11 = new Bundle();
                    if (resultReceiver instanceof ResultReceiver) {
                        if (resultCode == 0 && (ar.result instanceof int[])) {
                            int[] modemReason = (int[]) ar.result;
                            bundle11.putIntArray(XmPhoneConstants.MODEM_REASON_INFO, modemReason);
                        }
                        ((ResultReceiver) resultReceiver).send(resultCode, bundle11);
                    }
                    break;
                case 18:
                case 19:
                case 20:
                    Bundle bundle12 = new Bundle();
                    if (resultReceiver instanceof ResultReceiver) {
                        bundle12.putInt("phone_id", XmRILProxy.this.mPhoneId);
                        ((ResultReceiver) resultReceiver).send(resultCode, bundle12);
                    }
                    break;
                case 21:
                    if (ar.exception == null) {
                        XmRILProxy.this.mPhone.notifyModemExceptionChanged((int[]) ar.result);
                    }
                    break;
                case 22:
                    if (ar.exception == null) {
                        XmRILProxy.this.mPhone.notifyModemSceneChanged((int[]) ar.result);
                    }
                    break;
                case 23:
                    if (resultReceiver instanceof ResultReceiver) {
                        XmRILProxy.this.handleFiveTupleBlackListResponse(ar, resultCode, (ResultReceiver) resultReceiver);
                    }
                    break;
            }
        }
    };
    private XmRIL mXmRil;

    public XmRILProxy(XmPhone phone) {
        this.mPhone = phone;
        this.mPhoneId = this.mPhone.getPhoneId();
        this.mXmRil = new XmRIL(this.mPhone.getContext(), Integer.valueOf(this.mPhoneId));
        this.mXmRil.registerForUnsolCommMsg(this.mXmPhoneHandler, 1, null);
        this.mXmRil.registerHstStateCallback(this.mXmPhoneHandler, 9, null);
        this.mXmRil.registerQosStateCallback(this.mXmPhoneHandler, 10, null);
        this.mXmRil.registerModemExceptionCallback(this.mXmPhoneHandler, 21, null);
        this.mXmRil.registerModemSceneCallback(this.mXmPhoneHandler, 22, null);
    }

    public boolean sendCommMsg(int msgId, byte[] data, ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 0);
        resultMessage.obj = resultReceiver;
        this.mXmRil.sendCommMsg(msgId, data, resultMessage);
        return true;
    }

    public boolean setSarDsi(int dsi, ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 2);
        resultMessage.obj = resultReceiver;
        this.mXmRil.setSarDsi(dsi, resultMessage);
        return true;
    }

    public boolean setSarDsiGroup(int[] dsi, ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 3);
        resultMessage.obj = resultReceiver;
        this.mXmRil.setSarDsiGroup(dsi, resultMessage);
        return true;
    }

    public boolean getSarDsi(ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 4);
        resultMessage.obj = resultReceiver;
        this.mXmRil.getSarDsi(resultMessage);
        return true;
    }

    public boolean getSarDsiGroup(ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 5);
        resultMessage.obj = resultReceiver;
        this.mXmRil.getSarDsiGroup(resultMessage);
        return true;
    }

    public boolean setDeprioritizeSA(boolean enable, ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 6);
        Bundle dataBundle = new Bundle();
        dataBundle.putBoolean(XmPhoneConstants.SET_STATE, enable);
        resultMessage.setData(dataBundle);
        resultMessage.obj = resultReceiver;
        this.mXmRil.setDeprioritizeSA(enable, resultMessage);
        return true;
    }

    public boolean setNrMode(int nrStatus, ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 7);
        Bundle dataBundle = new Bundle();
        dataBundle.putInt(XmPhoneConstants.SET_STATE, nrStatus);
        resultMessage.setData(dataBundle);
        resultMessage.obj = resultReceiver;
        this.mXmRil.setNrMode(nrStatus, resultMessage);
        return true;
    }

    public boolean enableEndc(boolean enable, ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 8);
        Bundle dataBundle = new Bundle();
        dataBundle.putBoolean(XmPhoneConstants.SET_STATE, enable);
        resultMessage.setData(dataBundle);
        resultMessage.obj = resultReceiver;
        this.mXmRil.enableEndc(enable, resultMessage);
        return true;
    }

    public boolean setDataStallMode(int mode, int operation, int bearerType, int scene, ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 11);
        resultMessage.obj = resultReceiver;
        this.mXmRil.setDataStallMode(mode, operation, bearerType, scene, resultMessage);
        return true;
    }

    public boolean setMobilityOptParams(boolean enable, int rat, int featureId, ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 12);
        resultMessage.obj = resultReceiver;
        this.mXmRil.setMobilityOptParams(enable, rat, featureId, resultMessage);
        return true;
    }

    public boolean setPreferBandParams(int rat, int[] bands, int[] arfcns, int[] rsrpOffsets, int preferType, ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 13);
        resultMessage.obj = resultReceiver;
        this.mXmRil.setPreferBandParams(rat, bands, arfcns, rsrpOffsets, preferType, resultMessage);
        return true;
    }

    public boolean promoteBSR(int level, ResultReceiver resultReceiver) {
        Message resultMessage = Message.obtain(this.mXmPhoneHandler, 14);
        resultMessage.obj = resultReceiver;
        this.mXmRil.promoteBSR(level, resultMessage);
        return true;
    }

    public boolean getDdsAssistantInfo(ResultReceiver resultReceiver) {
        boolean zLess = this.mXmRil.getHalVersion().less(XmRIL.XM_RADIO_HAL_VERSION_2);
        Handler handler = this.mXmPhoneHandler;
        if (zLess) {
            Message resultMessage = Message.obtain(handler, 0);
            resultMessage.obj = resultReceiver;
            this.mXmRil.sendCommMsg(4, new byte[0], resultMessage);
            return true;
        }
        Message resultMessage2 = Message.obtain(handler, 15);
        resultMessage2.obj = resultReceiver;
        this.mXmRil.getDdsAssistantInfo(resultMessage2);
        return true;
    }

    public boolean getModemScene(ResultReceiver resultReceiver) {
        boolean zLess = this.mXmRil.getHalVersion().less(XmRIL.XM_RADIO_HAL_VERSION_2);
        Handler handler = this.mXmPhoneHandler;
        if (zLess) {
            Message resultMessage = Message.obtain(handler, 0);
            resultMessage.obj = resultReceiver;
            this.mXmRil.sendCommMsg(5, new byte[0], resultMessage);
            return true;
        }
        Message resultMessage2 = Message.obtain(handler, 16);
        resultMessage2.obj = resultReceiver;
        this.mXmRil.getModemScene(resultMessage2);
        return true;
    }

    public boolean getModemReason(ResultReceiver resultReceiver) {
        boolean zLess = this.mXmRil.getHalVersion().less(XmRIL.XM_RADIO_HAL_VERSION_2);
        Handler handler = this.mXmPhoneHandler;
        if (zLess) {
            Message resultMessage = Message.obtain(handler, 0);
            resultMessage.obj = resultReceiver;
            this.mXmRil.sendCommMsg(6, new byte[0], resultMessage);
            return true;
        }
        Message resultMessage2 = Message.obtain(handler, 17);
        resultMessage2.obj = resultReceiver;
        this.mXmRil.getModemReason(resultMessage2);
        return true;
    }

    public boolean setModemModelCfg(int[] modelCfg, ResultReceiver resultReceiver) {
        boolean zLess = this.mXmRil.getHalVersion().less(XmRIL.XM_RADIO_HAL_VERSION_2);
        Handler handler = this.mXmPhoneHandler;
        if (zLess) {
            Message resultMessage = Message.obtain(handler, 0);
            resultMessage.obj = resultReceiver;
            this.mXmRil.sendCommMsg(7, XmRILUtils.intArrayToByteArray(modelCfg), resultMessage);
            return true;
        }
        Message resultMessage2 = Message.obtain(handler, 18);
        resultMessage2.obj = resultReceiver;
        this.mXmRil.setModemModelCfg(modelCfg, resultMessage2);
        return true;
    }

    public boolean setModemCallbackSwitch(boolean z, ResultReceiver resultReceiver) {
        boolean zLess = this.mXmRil.getHalVersion().less(XmRIL.XM_RADIO_HAL_VERSION_2);
        Handler handler = this.mXmPhoneHandler;
        if (zLess) {
            Message messageObtain = Message.obtain(handler, 0);
            messageObtain.obj = resultReceiver;
            this.mXmRil.sendCommMsg(8, new byte[]{z ? (byte) 1 : (byte) 0}, messageObtain);
        } else {
            Message messageObtain2 = Message.obtain(handler, 19);
            messageObtain2.obj = resultReceiver;
            this.mXmRil.setModemCallbackSwitch(z, messageObtain2);
        }
        return true;
    }

    public boolean setModemEventIndSwitch(int i, boolean z, ResultReceiver resultReceiver) {
        boolean zLess = this.mXmRil.getHalVersion().less(XmRIL.XM_RADIO_HAL_VERSION_2);
        Handler handler = this.mXmPhoneHandler;
        if (zLess) {
            Message messageObtain = Message.obtain(handler, 0);
            messageObtain.obj = resultReceiver;
            ByteBuffer byteBufferOrder = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder.putInt(i);
            byteBufferOrder.put(z ? (byte) 1 : (byte) 0);
            this.mXmRil.sendCommMsg(9, byteBufferOrder.array(), messageObtain);
            return true;
        }
        Message messageObtain2 = Message.obtain(handler, 20);
        messageObtain2.obj = resultReceiver;
        this.mXmRil.setModemEventIndSwitch(i, z, messageObtain2);
        return true;
    }

    public HalVersion getHalVersion() {
        return this.mXmRil.getHalVersion();
    }

    public boolean setFiveTupleBlackList(String rmnetData, List<String> ipList, int mode, ResultReceiver resultReceiver) {
        boolean zLess = this.mXmRil.getHalVersion().less(XmRIL.XM_RADIO_HAL_VERSION_2);
        Handler handler = this.mXmPhoneHandler;
        if (zLess) {
            Message resultMessage = Message.obtain(handler, 0);
            resultMessage.obj = resultReceiver;
            this.mXmRil.sendCommMsg(13, packFiveTupleBlackList(mode, rmnetData, ipList), resultMessage);
            return true;
        }
        Message resultMessage2 = Message.obtain(handler, 23);
        resultMessage2.obj = resultReceiver;
        this.mXmRil.setFiveTupleBlackList(rmnetData, ipList, mode, resultMessage2);
        return true;
    }

    private byte[] packFiveTupleBlackList(int mode, String rmnetData, List<String> ipList) {
        byte[] rmnetBytes = rmnetData != null ? rmnetData.getBytes(StandardCharsets.UTF_8) : new byte[0];
        int ipCount = ipList != null ? ipList.size() : 0;
        int totalLen = rmnetBytes.length + 8 + 4;
        byte[][] ipBytesArray = new byte[ipCount][];
        for (int i = 0; i < ipCount; i++) {
            String ip = ipList.get(i);
            ipBytesArray[i] = ip != null ? ip.getBytes(StandardCharsets.UTF_8) : new byte[0];
            totalLen += ipBytesArray[i].length + 4;
        }
        ByteBuffer buf = ByteBuffer.allocate(totalLen).order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(mode);
        buf.putInt(rmnetBytes.length);
        buf.put(rmnetBytes);
        buf.putInt(ipCount);
        for (int i2 = 0; i2 < ipCount; i2++) {
            buf.putInt(ipBytesArray[i2].length);
            buf.put(ipBytesArray[i2]);
        }
        return buf.array();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFiveTupleBlackListResponse(AsyncResult ar, int resultCode, ResultReceiver resultReceiver) {
        if (resultCode == 0) {
            resultReceiver.send(resultCode, null);
            return;
        }
        Bundle errorBundle = new Bundle();
        if (ar.exception instanceof CommandException) {
            CommandException.Error error = ar.exception.getCommandError();
            if (error == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                errorBundle.putInt(XmPhoneConstants.ERROR_CODE, 0);
            } else if (error == CommandException.Error.OEM_ERROR_1) {
                errorBundle.putInt(XmPhoneConstants.ERROR_CODE, 1);
            } else if (error == CommandException.Error.OEM_ERROR_2) {
                errorBundle.putInt(XmPhoneConstants.ERROR_CODE, 2);
            } else if (error == CommandException.Error.OEM_ERROR_3) {
                errorBundle.putInt(XmPhoneConstants.ERROR_CODE, 3);
            } else if (error == CommandException.Error.OEM_ERROR_4) {
                errorBundle.putInt(XmPhoneConstants.ERROR_CODE, 4);
            } else if (error == CommandException.Error.OEM_ERROR_5) {
                errorBundle.putInt(XmPhoneConstants.ERROR_CODE, 5);
            } else {
                errorBundle.putInt(XmPhoneConstants.ERROR_CODE, -1);
            }
        } else {
            errorBundle.putInt(XmPhoneConstants.ERROR_CODE, -1);
        }
        resultReceiver.send(resultCode, errorBundle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String s) {
        Rlog.d(LOG_TAG, "[" + this.mPhoneId + "] " + s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String s) {
        Rlog.i(LOG_TAG, "[" + this.mPhoneId + "] " + s);
    }

    private void loge(String s) {
        Rlog.e(LOG_TAG, "[" + this.mPhoneId + "] " + s);
    }
}
