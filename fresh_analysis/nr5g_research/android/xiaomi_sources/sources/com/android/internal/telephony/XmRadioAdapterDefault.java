package com.android.internal.telephony;

import android.os.ResultReceiver;
import com.android.telephony.Rlog;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class XmRadioAdapterDefault implements IXmRadioAdapter {
    private static final String TAG = "XmRadioAdapterDefault";
    public XmPhone mPhone;
    public int mPhoneId;
    public XmRILProxy mXmRILProxy;

    public XmRadioAdapterDefault(XmPhone phone) {
        this.mXmRILProxy = new XmRILProxy(phone);
        this.mPhone = phone;
        this.mPhoneId = phone.getPhoneId();
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean sendCommMsg(int msgId, byte[] data, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.sendCommMsg(msgId, data, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setSarDsi(int dsi, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setSarDsi(dsi, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setSarDsiGroup(int[] dsi, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setSarDsiGroup(dsi, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean getSarDsi(ResultReceiver resultReceiver) {
        return this.mXmRILProxy.getSarDsi(resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean getSarDsiGroup(ResultReceiver resultReceiver) {
        return this.mXmRILProxy.getSarDsiGroup(resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setDeprioritizeSA(boolean enable, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setDeprioritizeSA(enable, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setNrMode(int nrStatus, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setNrMode(nrStatus, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean enableEndc(boolean enable, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.enableEndc(enable, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean restartRILD(ResultReceiver resultReceiver) {
        if (resultReceiver != null) {
            resultReceiver.send(-1, null);
            return true;
        }
        return true;
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setDataStallMode(int mode, int operation, int bearerType, int scene, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setDataStallMode(mode, operation, bearerType, scene, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setMobilityOptParams(boolean enable, int rat, int featureId, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setMobilityOptParams(enable, rat, featureId, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setPreferBandParams(int rat, int[] bands, int[] arfcns, int[] rsrpOffsets, int preferType, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setPreferBandParams(rat, bands, arfcns, rsrpOffsets, preferType, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean promoteBSR(int level, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.promoteBSR(level, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setSensorState(int sensorType, int sensorState, ResultReceiver resultReceiver) {
        if (resultReceiver != null) {
            resultReceiver.send(-1, null);
            return true;
        }
        return true;
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean getDdsAssistantInfo(ResultReceiver resultReceiver) {
        return this.mXmRILProxy.getDdsAssistantInfo(resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean getModemScene(ResultReceiver resultReceiver) {
        return this.mXmRILProxy.getModemScene(resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean getModemReason(ResultReceiver resultReceiver) {
        return this.mXmRILProxy.getModemReason(resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setModemModelCfg(int[] modelCfg, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setModemModelCfg(modelCfg, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setModemCallbackSwitch(boolean enable, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setModemCallbackSwitch(enable, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setModemEventIndSwitch(int event, boolean isRegister, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setModemEventIndSwitch(event, isRegister, resultReceiver);
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public int getHalVersion() {
        HalVersion version = this.mXmRILProxy.getHalVersion();
        return version.major;
    }

    @Override // com.android.internal.telephony.IXmRadioAdapter
    public boolean setFiveTupleBlackList(String rmnetData, List<String> ipList, int mode, ResultReceiver resultReceiver) {
        return this.mXmRILProxy.setFiveTupleBlackList(rmnetData, ipList, mode, resultReceiver);
    }

    public void logd(String s) {
        Rlog.d(TAG, "[" + this.mPhoneId + "] " + s);
    }

    public void logi(String s) {
        Rlog.i(TAG, "[" + this.mPhoneId + "] " + s);
    }

    public void loge(String s) {
        Rlog.e(TAG, "[" + this.mPhoneId + "] " + s);
    }
}
