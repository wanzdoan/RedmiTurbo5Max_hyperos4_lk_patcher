package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Bundle;
import vendor.xiaomi.hardware.radio.IXmRadioIndication;
import vendor.xiaomi.hardware.radio.data.QosInfo;

/* JADX INFO: loaded from: classes.dex */
public class XmRadioIndication extends IXmRadioIndication.Stub {
    private static final String TAG = "XmRadioInd";
    XmRIL mXmRil;

    public XmRadioIndication(XmRIL ril) {
        this.mXmRil = ril;
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
    public String getInterfaceHash() {
        return "0a6fcf9e7119f7f2b3db206a650c509fcd647679";
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
    public int getInterfaceVersion() {
        return 2;
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
    public void unsolCommMsgInd(int indicationType, int msgId, byte[] data) {
        this.mXmRil.processIndication(indicationType);
        this.mXmRil.unsljLogRet(1000, Integer.valueOf(msgId));
        Bundle bundle = new Bundle();
        bundle.putInt("message_id", msgId);
        bundle.putByteArray("byte_array", data);
        if (this.mXmRil.mUnsolCommMsgRegistrants != null) {
            this.mXmRil.mUnsolCommMsgRegistrants.notifyRegistrants(new AsyncResult((Object) null, bundle, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
    public void hstInfoInd(int indicationType, boolean hstState, int hstRat) {
        this.mXmRil.processIndication(indicationType);
        this.mXmRil.unsljLogRet(1001, Boolean.valueOf(hstState));
        Bundle bundle = new Bundle();
        bundle.putBoolean("hst_state", hstState);
        bundle.putInt("hst_sat", hstRat);
        if (this.mXmRil.mHstInfoRegistrants != null) {
            this.mXmRil.mHstInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, bundle, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
    public void qosInfoInd(int indicationType, QosInfo qosInfo) {
        this.mXmRil.processIndication(indicationType);
        this.mXmRil.unsljLog(XmRILConstants.XM_RIL_UNSOL_QOS_INFO_INDICATION);
        Bundle bundle = new Bundle();
        bundle.putInt("qos_id", qosInfo.qosId);
        bundle.putInt("qos_new_flow", qosInfo.newFlow);
        bundle.putInt("qos_state", qosInfo.state);
        bundle.putInt("qos_qci", qosInfo.qci);
        if (this.mXmRil.mQosInfoRegistrants != null) {
            this.mXmRil.mQosInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, bundle, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
    public void modemExceptionInd(int indicationType, int[] modemException) {
        this.mXmRil.processIndication(indicationType);
        this.mXmRil.unsljLog(XmRILConstants.XM_RIL_UNSOL_MODEM_EXCEPTION_INDICATION);
        if (this.mXmRil.mModemExceptionRegistrants != null) {
            this.mXmRil.mModemExceptionRegistrants.notifyRegistrants(new AsyncResult((Object) null, modemException, (Throwable) null));
        }
    }

    @Override // vendor.xiaomi.hardware.radio.IXmRadioIndication
    public void modemSceneInd(int indicationType, int[] sceneInfo) {
        this.mXmRil.processIndication(indicationType);
        this.mXmRil.unsljLog(XmRILConstants.XM_RIL_UNSOL_MODEM_SCENE_INDICATION);
        if (this.mXmRil.mModemSceneRegistrants != null) {
            this.mXmRil.mModemSceneRegistrants.notifyRegistrants(new AsyncResult((Object) null, sceneInfo, (Throwable) null));
        }
    }
}
