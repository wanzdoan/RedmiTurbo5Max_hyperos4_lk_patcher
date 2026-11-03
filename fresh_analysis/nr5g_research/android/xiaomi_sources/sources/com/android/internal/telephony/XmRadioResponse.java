package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Message;
import vendor.xiaomi.hardware.radio.IXmRadioResponse;
import vendor.xiaomi.hardware.radio.RadioResponseInfo;

/* JADX INFO: loaded from: classes.dex */
public class XmRadioResponse extends IXmRadioResponse.Stub {
    private static final String TAG = "XmRadioResp";
    XmRIL mXmRil;

    public XmRadioResponse(XmRIL ril) {
        this.mXmRil = ril;
    }

    static void sendMessageResponse(Message msg, Object ret) {
        if (msg != null) {
            AsyncResult.forMessage(msg, ret, (Throwable) null);
            msg.sendToTarget();
        }
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public String getInterfaceHash() {
        return "0a6fcf9e7119f7f2b3db206a650c509fcd647679";
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public int getInterfaceVersion() {
        return 2;
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void sendCommMsgResponse(RadioResponseInfo responseInfo, byte[] data) {
        XmRILRequest rr = this.mXmRil.processResponse(responseInfo);
        if (rr != null && rr.mArguments != null && rr.mArguments.length > 0) {
            Bundle bundle = new Bundle();
            bundle.putInt("message_id", ((Integer) rr.mArguments[0]).intValue());
            bundle.putByteArray("byte_array", data);
            if (responseInfo.error == 0) {
                sendMessageResponse(rr.mResult, bundle);
            }
            this.mXmRil.processResponseDone(rr, responseInfo, (Object) bundle);
        }
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setSarDsiResponse(RadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setSarDsiGroupResponse(RadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void getSarDsiResponse(RadioResponseInfo responseInfo, int dsi) {
        responseInts(responseInfo, dsi);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void getSarDsiGroupResponse(RadioResponseInfo responseInfo, int[] dsiGroup) {
        responseIntList(responseInfo, dsiGroup);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setDeprioritizeSAResponse(RadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setNrModeResponse(RadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void enableEndcResponse(RadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    private void responseVoid(RadioResponseInfo responseInfo) {
        XmRILRequest rr = this.mXmRil.processResponse(responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                sendMessageResponse(rr.mResult, null);
            }
            this.mXmRil.processResponseDone(rr, responseInfo, (Object) null);
        }
    }

    private void responseInts(RadioResponseInfo responseInfo, int... var) {
        int[] ints = new int[var.length];
        for (int i = 0; i < var.length; i++) {
            ints[i] = var[i];
        }
        responseIntList(responseInfo, ints);
    }

    private void responseIntList(RadioResponseInfo responseInfo, int[] ret) {
        XmRILRequest rr = this.mXmRil.processResponse(responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                sendMessageResponse(rr.mResult, ret);
            }
            this.mXmRil.processResponseDone(rr, responseInfo, (Object) ret);
        }
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setDataStallModeResponse(RadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setMobilityOptParamsResponse(RadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setPreferBandParamsResponse(RadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void promoteBSRResponse(RadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void getDdsAssistantInfoResponse(RadioResponseInfo responseInfo, int[] assistantInfo) {
        responseIntList(responseInfo, assistantInfo);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void getModemSceneResponse(RadioResponseInfo info, int[] scene) {
        responseIntList(info, scene);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void getModemReasonResponse(RadioResponseInfo info, int[] reason) {
        responseIntList(info, reason);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setModemModelCfgResponse(RadioResponseInfo info) {
        responseVoid(info);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setModemCallbackSwitchResponse(RadioResponseInfo info) {
        responseVoid(info);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setModemEventIndSwitchResponse(RadioResponseInfo info) {
        responseVoid(info);
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioResponse
    public void setFiveTupleBlackListResponse(RadioResponseInfo info) {
        responseVoid(info);
    }
}
