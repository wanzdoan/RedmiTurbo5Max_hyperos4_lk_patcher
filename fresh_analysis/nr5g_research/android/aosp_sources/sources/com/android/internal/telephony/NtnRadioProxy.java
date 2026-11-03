package com.android.internal.telephony;

import android.os.RemoteException;
import android.telephony.data.DataProfile;
import com.android.internal.telephony.ntnphone.NtnAntConstants;
import com.android.internal.telephony.ntnphone.NtnRILUtils;
import com.android.telephony.Rlog;
import vendor.xiaomi.hardware.satellite.Dial;
import vendor.xiaomi.hardware.satellite.GsmSmsMessage;
import vendor.xiaomi.hardware.satellite.INTNRadio;

/* JADX INFO: loaded from: classes.dex */
public class NtnRadioProxy extends RadioServiceProxy {
    private volatile INTNRadio mNtnRadioProxy = null;

    public HalVersion setAidl(INTNRadio iNTNRadio) {
        this.mHalVersion = NtnRIL.NTN_RADIO_HAL_VERSION_UNKNOWN;
        try {
            this.mHalVersion = NtnRIL.getNtnServiceHalVersion(iNTNRadio.getInterfaceVersion());
        } catch (RemoteException e) {
            Rlog.e("NtnRadioProxy", "setAidl : ", e);
        }
        this.mNtnRadioProxy = iNTNRadio;
        this.mIsAidl = true;
        Rlog.d("NtnRadioProxy", "AIDL initialized mIsAidl = " + this.mIsAidl + " mHalVersion = " + this.mHalVersion);
        return this.mHalVersion;
    }

    public INTNRadio getAidl() {
        return this.mNtnRadioProxy;
    }

    @Override // com.android.internal.telephony.RadioServiceProxy
    public void clear() {
        super.clear();
        this.mNtnRadioProxy = null;
    }

    @Override // com.android.internal.telephony.RadioServiceProxy
    public boolean isEmpty() {
        return this.mRadioProxy == null && this.mNtnRadioProxy == null;
    }

    public void acceptCall(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.acceptCall(i);
        }
    }

    public void changeIccPin2ForApp(int i, String str, String str2, String str3) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.changeIccPin2ForApp(i, str, str2, str3);
        }
    }

    public void changeIccPinForApp(int i, String str, String str2, String str3) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.changeIccPin2ForApp(i, str, str2, str3);
        }
    }

    public void dial(int i, Dial dial) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.dial(i, dial);
        }
    }

    public void getBasebandVersion(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getBasebandVersion(i);
        }
    }

    public void getCurrentCalls(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getCurrentCalls(i);
        }
    }

    public void getIccCardStatus(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getIccCardStatus(i);
        }
    }

    public void getIccidForApp(int i, String str) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getIccidForApp(i, str);
        }
    }

    public void getMute(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getMute(i);
        }
    }

    public void getSARMode(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getSARMode(i);
        }
    }

    public void getSignalStrength(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getSignalStrength(i);
        }
    }

    public void setSignalStrengthReportingCriteria(int i, int i2, int[] iArr) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setSignalStrengthReportingCriteria(i, i2, iArr);
        }
    }

    public void getVoiceRegistrationState(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getVoiceRegistrationState(i);
        }
    }

    public void hangup(int i, int i2) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.hangup(i, i2);
        }
    }

    public void rejectCall(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.rejectCall(i);
        }
    }

    public void requestShutdown(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.requestShutdown(i);
        }
    }

    @Override // com.android.internal.telephony.RadioServiceProxy
    public void responseAcknowledgement() throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.responseAcknowledgement();
        }
    }

    public void sendBurstDtmf(int i, String str, int i2, int i3) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.sendBurstDtmf(i, str, i2, i3);
        }
    }

    public void sendDtmf(int i, String str) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.sendDtmf(i, str);
        }
    }

    public void sendSms(int i, GsmSmsMessage gsmSmsMessage) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.sendSms(i, gsmSmsMessage);
        }
    }

    public void setMute(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setMute(i, z);
        }
    }

    public void setRadioPower(int i, boolean z, boolean z2, boolean z3) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setRadioPower(i, z, z2, z3);
        }
    }

    public void setSARMode(int i, int i2) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setSARMode(i, i2);
        }
    }

    public void startDtmf(int i, String str) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.startDtmf(i, str);
        }
    }

    public void stopDtmf(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.stopDtmf(i);
        }
    }

    public void supplyIccAuthRsp(int i, String str) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.supplyIccAuthRsp(i, str);
        }
    }

    public void supplyIccPin2ForApp(int i, String str, String str2) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.supplyIccPin2ForApp(i, str, str2);
        }
    }

    public void supplyIccPinForApp(int i, String str, String str2) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.supplyIccPinForApp(i, str, str2);
        }
    }

    public void supplyIccPuk2ForApp(int i, String str, String str2, String str3) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.supplyIccPuk2ForApp(i, str, str2, str3);
        }
    }

    public void supplyIccPukForApp(int i, String str, String str2, String str3) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.supplyIccPukForApp(i, str, str2, str3);
        }
    }

    public void supplyImsiForApp(int i, String str) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.supplyImsiForApp(i, str);
        }
    }

    public void getLastCallFailCause(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getLastCallFailCause(i);
        }
    }

    public void supplyGpsForApp(int i, String str) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.supplyGpsForApp(i, str);
        }
    }

    public void sendTemp(int i, int i2) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.sendTemp(i, i2);
        }
    }

    public void sendcustFactAtcmd(int i, int i2, String str) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.sendcustFactAtcmd(i, i2, str);
        }
    }

    public void setScreenState(int i, int i2) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setScreenState(i, i2);
        }
    }

    public void getSpeechBitrate(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getSpeechBitrate(i);
        }
    }

    public void setSpeechBitrate(int i, String str) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setSpeechBitrate(i, str);
        }
    }

    public void getIMSIForApp(int i, String str) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getImsiForApp(i, str);
        }
    }

    public void supplyImeiForApp(int i, String str) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.supplyImeiForApp(i, str);
        }
    }

    public void setRFMode(int i, int i2) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setRFMode(i, i2);
        }
    }

    public void setPriorityFrequencySearch(int i, int[] iArr) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setPriorityFrequencySearch(i, iArr);
        }
    }

    public void setSatBeam(int i, int i2, int[] iArr) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setSatBeam(i, i2, iArr);
        }
    }

    public void setPowerSwitch(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setPowerSwitch(i, z);
        }
    }

    public void setupDataCall(int i, int i2, DataProfile dataProfile, boolean z, boolean z2, boolean z3) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setupDataCall(i, i2, NtnRILUtils.convertToHalDataProfile(new DataProfile.Builder().setType(dataProfile.getType()).setPreferred(dataProfile.isPreferred()).setApnSetting(dataProfile.getApnSetting()).build()), z, z2, z3);
        }
    }

    public void deactivateDataCall(int i, int i2, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.deactivateDataCall(i, i2, z);
        }
    }

    public void queryCurrentSatellteCapability(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.queryCurrentSatellteCapability(i);
        }
    }

    public void setSimCardPower(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setSimCardPower(i, z ? 1 : 0);
        }
    }

    public void setAntennaState(int i, NtnAntConstants.TxAntEnableType txAntEnableType, NtnAntConstants.RxAntEnableType rxAntEnableType) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setAntennaState(i, NtnRILUtils.convertToHalTxAntEnableType(txAntEnableType), NtnRILUtils.convertToHalRxAntEnableType(rxAntEnableType));
        }
    }

    public void getAntennaState(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.getAntennaState(i);
        }
    }

    public void setAntennaReportMode(int i, NtnAntConstants.RxAntReportMode rxAntReportMode) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNtnRadioProxy.setAntennaReportMode(i, NtnRILUtils.convertToHalRxAntReportMode(rxAntReportMode));
        }
    }
}
