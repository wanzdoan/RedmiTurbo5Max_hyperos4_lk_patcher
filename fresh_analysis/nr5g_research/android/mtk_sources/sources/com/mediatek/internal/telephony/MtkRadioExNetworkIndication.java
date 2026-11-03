package com.mediatek.internal.telephony;

import android.content.Intent;
import android.os.AsyncResult;
import android.os.Build;
import android.os.SystemProperties;
import android.telephony.SignalStrength;
import vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication;
import vendor.mediatek.hardware.mtkradioex.network.SignalStrengthWithWcdmaEcio;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExNetworkIndication extends IMtkRadioExNetworkIndication.Stub {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    MtkRIL mMtkRil;

    public MtkRadioExNetworkIndication(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void currentSignalStrengthWithWcdmaEcioInd(int type, SignalStrengthWithWcdmaEcio signalStrength) {
        this.mMtkRil.processIndication(4, type);
        SignalStrength ss = new SignalStrength();
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_SIGNAL_STRENGTH_WITH_WCDMA_ECIO, ss);
        this.mMtkRil.riljLog("currentSignalStrengthWithWcdmaEcioInd SignalStrength=" + ss);
        if (this.mMtkRil.mSignalStrengthWithWcdmaEcioRegistrants.size() != 0) {
            this.mMtkRil.mSignalStrengthWithWcdmaEcioRegistrants.notifyRegistrants(new AsyncResult((Object) null, ss, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void networkBandInfoInd(int type, int[] state) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_NETWORK_BAND_INFO, state);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void networkInfoInd(int type, String[] networkinfo) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.unsljLogMore(MtkRILConstants.RIL_UNSOL_NETWORK_INFO, "networkInfo: " + networkinfo);
        if (this.mMtkRil.mNetworkInfoRegistrant.size() != 0) {
            this.mMtkRil.mNetworkInfoRegistrant.notifyRegistrants(new AsyncResult((Object) null, networkinfo, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void nrCaBandChangeInd(int type, int[] bands) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_NR_CA_BAND_IND, bands);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void nrSysInfoInd(int type, int[] nrSysInfos) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_NR_SYS_INFO, nrSysInfos);
        if (this.mMtkRil.mNrSysInfoRegistrants.size() != 0) {
            this.mMtkRil.mNrSysInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, nrSysInfos, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void on5GUWInfoInd(int type, int[] data) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_5GUW_INFO_IND, data);
        if (data != null && data.length > 0) {
            int networkType = data[0];
            this.mMtkRil.getDisplayNetworkTypeChangedRegistrants().notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(networkType), (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void onMccMncChanged(int type, String mccmnc) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_MCCMNC_CHANGED, mccmnc);
        if (this.mMtkRil.mMccMncRegistrants.size() != 0) {
            this.mMtkRil.mMccMncRegistrants.notifyRegistrants(new AsyncResult((Object) null, mccmnc, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void onNwCfgInfoInd(int type, boolean mimo, boolean qam_256, boolean qam_ul64) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.riljLog("onNwCfgInfoInd mimo =" + mimo + " qam_256 = " + qam_256);
        Boolean[] response = {Boolean.valueOf(mimo), Boolean.valueOf(qam_256), Boolean.valueOf(qam_ul64)};
        if (this.mMtkRil.mNwCfgInfoRegistrants != null) {
            this.mMtkRil.mNwCfgInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void onNwRrcStateInd(int type, int rat, int state) {
        this.mMtkRil.processIndication(4, type);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void onPseudoCellInfoInd(int type, int[] info) {
        PseudoCellInfo cellInfo;
        this.mMtkRil.processIndication(4, type);
        if (ENG) {
            this.mMtkRil.unsljLog(MtkRILConstants.RIL_UNSOL_PSEUDO_CELL_INFO);
        }
        String property = String.format("persist.vendor.radio.apc.mode%d", this.mMtkRil.mInstanceId);
        String propStr = SystemProperties.get(property, "0");
        int index = propStr.indexOf("=");
        if (index != -1) {
            String subStr = propStr.substring(index + 1);
            String[] settings = subStr.split(",");
            int mode = Integer.parseInt(settings[0]);
            int report = Integer.parseInt(settings[1]);
            boolean enable = report == 1;
            int interval = Integer.parseInt(settings[2]);
            cellInfo = new PseudoCellInfo(mode, enable, interval, info);
        } else {
            cellInfo = new PseudoCellInfo(0, false, 0, info);
        }
        if (this.mMtkRil.mPseudoCellInfoRegistrants != null) {
            this.mMtkRil.mPseudoCellInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, cellInfo, (Throwable) null));
        }
        Intent intent = new Intent("com.mediatek.phone.ACTION_APC_INFO_NOTIFY");
        intent.putExtra("phoneId", this.mMtkRil.mInstanceId);
        intent.putExtra("info", cellInfo);
        this.mMtkRil.mMtkContext.sendBroadcast(intent);
        this.mMtkRil.riljLog("Broadcast for APC info:cellInfo=" + cellInfo.toString());
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void responseCsNetworkStateChangeInd(int type, String[] state) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.riljLog("[UNSL]< UNSOL_RESPONSE_CS_NETWORK_STATE_CHANGED");
        if (this.mMtkRil.mCsNetworkStateRegistrants.size() != 0) {
            this.mMtkRil.mCsNetworkStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, state, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void responseFemtocellInfo(int type, String[] info) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_FEMTOCELL_INFO, info);
        if (this.mMtkRil.mFemtoCellInfoRegistrants.size() != 0) {
            this.mMtkRil.mFemtoCellInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, info, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void responseLteNetworkInfo(int type, int info) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.riljLog("[UNSL]< RIL_UNSOL_LTE_NETWORK_INFO " + info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void responseModulationInfoInd(int type, int[] modulation) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_MODULATION_INFO, modulation);
        if (this.mMtkRil.mModulationRegistrants.size() != 0) {
            this.mMtkRil.mModulationRegistrants.notifyRegistrants(new AsyncResult((Object) null, modulation, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void responseNetworkEventInd(int type, int[] event) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_NETWORK_EVENT, event);
        if (this.mMtkRil.mNetworkEventRegistrants.size() != 0) {
            this.mMtkRil.mNetworkEventRegistrants.notifyRegistrants(new AsyncResult((Object) null, event, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void responsePsNetworkStateChangeInd(int type, int[] state) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.riljLog("[UNSL]< UNSOL_RESPONSE_PS_NETWORK_STATE_CHANGED");
        if (this.mMtkRil.mPsNetworkStateRegistrants.size() != 0) {
            this.mMtkRil.mPsNetworkStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, state, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void sib16TimeInfoInd(int type, String sib16Time, long receivedTime) {
        this.mMtkRil.processIndication(4, type);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void toeInfoInd(int type, String longName, String shortName, String numeric) {
        this.mMtkRil.processIndication(4, type);
        String[] response = {longName, shortName, numeric};
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_TOE_INFO_IND, response);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public void iwlanRegistrationStateInd(int type, int state) {
        this.mMtkRil.processIndication(4, type);
        this.mMtkRil.unsljLogRet(MtkRILConstants.RIL_UNSOL_IWLAN_REGISTRATION_STATE_IND, Integer.valueOf(state));
        int[] response = {state};
        this.mMtkRil.mIWlanStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public String getInterfaceHash() {
        return "583aefd4764eb70d99325928242c9ced29a9c0ee";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication
    public int getInterfaceVersion() {
        return 1;
    }
}
