package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Message;
import android.telephony.data.DataCallResponse;
import com.android.internal.telephony.ntnphone.NtnDriverCall;
import com.android.internal.telephony.ntnphone.NtnRILUtils;
import com.android.internal.telephony.ntnphone.SatelliteConfig;
import com.android.internal.telephony.uicc.IccCardStatus;
import java.util.ArrayList;
import java.util.Collections;
import vendor.xiaomi.hardware.satellite.CardStatus;
import vendor.xiaomi.hardware.satellite.EphemParam;
import vendor.xiaomi.hardware.satellite.INTNRadioResponse;
import vendor.xiaomi.hardware.satellite.LastCallFailCauseInfo;
import vendor.xiaomi.hardware.satellite.RadioResponseInfo;
import vendor.xiaomi.hardware.satellite.SatelliteCapability;
import vendor.xiaomi.hardware.satellite.SendSmsResult;
import vendor.xiaomi.hardware.satellite.SetupDataCallResult;
import vendor.xiaomi.hardware.satellite.SignalStrength;
import vendor.xiaomi.hardware.satellite.SmsCenterAddressResult;
import vendor.xiaomi.hardware.satellite.VoiceRegStateResult;

/* JADX INFO: loaded from: classes.dex */
public class NtnResponse extends INTNRadioResponse.Stub {
    NtnRIL mRil;
    ArrayList responseList;

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void changeIccPin2ForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getEphemResponse(RadioResponseInfo radioResponseInfo, EphemParam ephemParam) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public int getInterfaceVersion() {
        return 3;
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getMuteResponse(RadioResponseInfo radioResponseInfo, boolean z) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void sendPhyemDataResponse(RadioResponseInfo radioResponseInfo) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setAtCommandForStarNetTestResponse(RadioResponseInfo radioResponseInfo) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setEPHEMResponse(RadioResponseInfo radioResponseInfo) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setMuteResponse(RadioResponseInfo radioResponseInfo) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void supplyIccPin2ForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void supplyIccPuk2ForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
    }

    public NtnResponse(NtnRIL ntnRIL) {
        this.mRil = ntnRIL;
    }

    static void sendMessageResponse(Message message, Object obj) {
        if (message != null) {
            AsyncResult.forMessage(message, obj, (Throwable) null);
            message.sendToTarget();
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setRadioPowerResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
        this.mRil.mLastRadioPowerResult = radioResponseInfo.error;
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getCurrentCallsResponse(RadioResponseInfo radioResponseInfo, vendor.xiaomi.hardware.satellite.Call[] callArr) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList arrayList = new ArrayList(callArr.length);
            for (vendor.xiaomi.hardware.satellite.Call call : callArr) {
                NtnDriverCall ntnDriverCallConvertToNtnDriverCall = NtnRILUtils.convertToNtnDriverCall(call);
                arrayList.add(ntnDriverCallConvertToNtnDriverCall);
                boolean z = ntnDriverCallConvertToNtnDriverCall.isVoicePrivacy;
                NtnRIL ntnRIL = this.mRil;
                if (z) {
                    ntnRIL.mVoicePrivacyOnRegistrants.notifyRegistrants();
                    this.mRil.riljLog("InCall VoicePrivacy is enabled");
                } else {
                    ntnRIL.mVoicePrivacyOffRegistrants.notifyRegistrants();
                    this.mRil.riljLog("InCall VoicePrivacy is disabled");
                }
            }
            Collections.sort(arrayList);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, arrayList);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, arrayList);
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void dialResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void hangupConnectionResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void rejectCallResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getLastCallFailCauseResponse(RadioResponseInfo radioResponseInfo, LastCallFailCauseInfo lastCallFailCauseInfo) {
        responseLastCallFailCauseInfo(radioResponseInfo, lastCallFailCauseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getSignalStrengthResponse(RadioResponseInfo radioResponseInfo, SignalStrength signalStrength) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            android.telephony.SignalStrength signalStrengthConvertHalSignalStrength = NtnRILUtils.convertHalSignalStrength(signalStrength);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, signalStrengthConvertHalSignalStrength);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, signalStrengthConvertHalSignalStrength);
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setSignalStrengthReportingCriteriaResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getVoiceRegistrationStateResponse(RadioResponseInfo radioResponseInfo, VoiceRegStateResult voiceRegStateResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, voiceRegStateResult);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, voiceRegStateResult);
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void sendDtmfResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void sendSmsResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            SmsResponse smsResponse = new SmsResponse(sendSmsResult.messageRef, sendSmsResult.ackPDU, sendSmsResult.errorCode, RIL.getOutgoingSmsMessageId(rILRequestProcessResponse.mResult));
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, smsResponse);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, smsResponse);
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void acceptCallResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void startDtmfResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void stopDtmfResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getBasebandVersionResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void sendBurstDtmfResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void requestShutdownResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void supplyImsiForAppResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void supplyIccAuthRspResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void acknowledgeRequest(int i) {
        this.mRil.processRequestAck(i);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getSARModeResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInt(radioResponseInfo, i);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setSARModeResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void supplyIccPinForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void supplyIccPukForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void changeIccPinForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getIMSIForAppResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getIccidForAppResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    private void responseLastCallFailCauseInfo(RadioResponseInfo radioResponseInfo, LastCallFailCauseInfo lastCallFailCauseInfo) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            LastCallFailCause lastCallFailCause = new LastCallFailCause();
            lastCallFailCause.causeCode = lastCallFailCauseInfo.causeCode;
            lastCallFailCause.vendorCause = lastCallFailCauseInfo.vendorCause;
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, lastCallFailCause);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, lastCallFailCause);
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void supplyGpsForAppResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public String getInterfaceHash() {
        return "ffb2075644123aea70558b66153b0c951d994dcc";
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void sendTempResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void sendcustFactAtcmdresponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setScreenStateResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setSpeechBitrateResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getSpeechBitrateResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    private void responseInt(RadioResponseInfo radioResponseInfo, int i) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, Integer.valueOf(i));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, Integer.valueOf(i));
        }
    }

    private void responseVoid(RadioResponseInfo radioResponseInfo) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, null);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) null);
        }
    }

    private void responseString(RadioResponseInfo radioResponseInfo, String str) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, str);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, str);
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void acknowledgeIncomingGsmSmsWithPduResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setSmscAddressResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getSmscAddressResponse(RadioResponseInfo radioResponseInfo, SmsCenterAddressResult smsCenterAddressResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, smsCenterAddressResult);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, smsCenterAddressResult);
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setStackErrorEventNotifyResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void supplyImeiForAppResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getCPIdResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getImeiForAppResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getIccCardStatusResponse(RadioResponseInfo radioResponseInfo, CardStatus cardStatus) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            IccCardStatus iccCardStatusConvertHalCardStatus = NtnRILUtils.convertHalCardStatus(cardStatus);
            this.mRil.riljLog("responseIccCardStatus: from AIDL: " + iccCardStatusConvertHalCardStatus);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, iccCardStatusConvertHalCardStatus);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, iccCardStatusConvertHalCardStatus);
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setRFModeResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setupDataCallResponse(RadioResponseInfo radioResponseInfo, SetupDataCallResult setupDataCallResult) {
        responseSetupDataCall(radioResponseInfo, setupDataCallResult);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void deactivateDataCallResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getDataCallListResponse(RadioResponseInfo radioResponseInfo, SetupDataCallResult setupDataCallResult) {
        responseDataCallList(radioResponseInfo, setupDataCallResult);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void sendVoicectrlVersionResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    private void responseInts(RadioResponseInfo radioResponseInfo, int... iArr) {
        ArrayList arrayList = new ArrayList();
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        responseIntArrayList(radioResponseInfo, arrayList);
    }

    private void responseIntArrayList(RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int[] iArr = new int[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                iArr[i] = ((Integer) arrayList.get(i)).intValue();
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, iArr);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, iArr);
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setPriorityFrequencySearchResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setSatBeamResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setPowerSwitchResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    private void responseSetupDataCall(RadioResponseInfo radioResponseInfo, Object obj) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            DataCallResponse dataCallResponseConvertHalDataCallResult = NtnRILUtils.convertHalDataCallResult(obj);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, dataCallResponseConvertHalDataCallResult);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, dataCallResponseConvertHalDataCallResult);
        }
    }

    private void responseDataCallList(RadioResponseInfo radioResponseInfo, Object obj) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            DataCallResponse dataCallResponseConvertHalDataCallResult = NtnRILUtils.convertHalDataCallResult(obj);
            ArrayList arrayList = new ArrayList();
            this.responseList = arrayList;
            arrayList.add(dataCallResponseConvertHalDataCallResult);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, this.responseList);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, dataCallResponseConvertHalDataCallResult);
        }
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setAntennaReportModeResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void getAntennaStateResponse(RadioResponseInfo radioResponseInfo, int i, int i2) {
        responseInts(radioResponseInfo, NtnRILUtils.convertHalTxAntEnableType(i).getType(), NtnRILUtils.convertHalRxAntEnableType(i2).getType());
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setAntennaStateResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void sendEphemCalcResultResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void queryCurrentSatellteCapabilityResponse(RadioResponseInfo radioResponseInfo, SatelliteCapability[] satelliteCapabilityArr) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        ArrayList arrayList = new ArrayList();
        new SatelliteConfig();
        if (rILRequestProcessResponse != null && satelliteCapabilityArr != null) {
            for (SatelliteCapability satelliteCapability : satelliteCapabilityArr) {
                arrayList.add(NtnRILUtils.convertToNtnSatelliteCap(satelliteCapability));
            }
        }
        Collections.sort(arrayList);
        if (radioResponseInfo.error == 0) {
            sendMessageResponse(rILRequestProcessResponse.mResult, arrayList);
        }
        this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, arrayList);
    }

    @Override // vendor.xiaomi.hardware.satellite.INTNRadioResponse
    public void setSimCardPowerResponse(RadioResponseInfo radioResponseInfo) {
        this.mRil.riljLog("setSimCardPowerResponse receive response!");
        responseVoid(radioResponseInfo);
    }
}
