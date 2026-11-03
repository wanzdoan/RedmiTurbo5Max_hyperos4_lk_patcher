package com.android.internal.telephony;

import android.hardware.radio.V1_0.ActivityStatsInfo;
import android.hardware.radio.V1_0.CardStatus;
import android.hardware.radio.V1_0.CarrierRestrictions;
import android.hardware.radio.V1_0.CdmaBroadcastSmsConfigInfo;
import android.hardware.radio.V1_0.CellInfo;
import android.hardware.radio.V1_0.DataRegStateResult;
import android.hardware.radio.V1_0.GsmBroadcastSmsConfigInfo;
import android.hardware.radio.V1_0.IccIoResult;
import android.hardware.radio.V1_0.LastCallFailCauseInfo;
import android.hardware.radio.V1_0.LceDataInfo;
import android.hardware.radio.V1_0.LceStatusInfo;
import android.hardware.radio.V1_0.NeighboringCell;
import android.hardware.radio.V1_0.OperatorInfo;
import android.hardware.radio.V1_0.RadioResponseInfo;
import android.hardware.radio.V1_0.SendSmsResult;
import android.hardware.radio.V1_0.SetupDataCallResult;
import android.hardware.radio.V1_0.SignalStrength;
import android.hardware.radio.V1_0.VoiceRegStateResult;
import android.hardware.radio.V1_1.KeepaliveStatus;
import android.hardware.radio.V1_4.CarrierRestrictionsWithPriority;
import android.hardware.radio.V1_5.BarringInfo;
import android.hardware.radio.V1_5.CellIdentity;
import android.hardware.radio.V1_5.RadioAccessSpecifier;
import android.hardware.radio.V1_5.RegStateResult;
import android.hardware.radio.V1_6.IRadioResponse;
import android.hardware.radio.V1_6.PhonebookCapacity;
import android.hardware.radio.V1_6.SlicingConfig;
import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierRestrictionRules;
import android.telephony.ModemActivityInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.data.DataCallResponse;
import android.telephony.data.NetworkSlicingConfig;
import android.text.TextUtils;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.uicc.AdnCapacity;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccSlotPortMapping;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class RadioResponse extends IRadioResponse.Stub {
    public RIL mRil;

    public RadioResponse(RIL ril) {
        this.mRil = ril;
    }

    public static void sendMessageResponse(Message message, Object obj) {
        if (message != null) {
            AsyncResult.forMessage(message, obj, (Throwable) null);
            message.sendToTarget();
        }
    }

    public void acknowledgeRequest(int i) {
        this.mRil.processRequestAck(i);
    }

    public void getIccCardStatusResponse(RadioResponseInfo radioResponseInfo, CardStatus cardStatus) {
        responseNotSupported(radioResponseInfo);
    }

    public void getIccCardStatusResponse_1_2(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_2.CardStatus cardStatus) {
        responseNotSupported(radioResponseInfo);
    }

    public void getIccCardStatusResponse_1_4(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_4.CardStatus cardStatus) {
        responseIccCardStatus(radioResponseInfo, cardStatus);
    }

    public void getIccCardStatusResponse_1_5(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_5.CardStatus cardStatus) {
        responseIccCardStatus_1_5(radioResponseInfo, cardStatus);
    }

    public void supplyIccPinForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void supplyIccPukForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void supplyIccPin2ForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void supplyIccPuk2ForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void changeIccPinForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void changeIccPin2ForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void supplyNetworkDepersonalizationResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void supplySimDepersonalizationResponse(RadioResponseInfo radioResponseInfo, int i, int i2) {
        responseInts(radioResponseInfo, i, i2);
    }

    public void getCurrentCallsResponse(RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_0.Call> arrayList) {
        responseNotSupported(radioResponseInfo);
    }

    public void getCurrentCallsResponse_1_2(RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_2.Call> arrayList) {
        responseCurrentCalls(radioResponseInfo, arrayList);
    }

    public void getCurrentCallsResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_6.Call> arrayList) {
        responseCurrentCalls_1_6(radioResponseInfo, arrayList);
    }

    public void dialResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getIMSIForAppResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    public void hangupConnectionResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void hangupWaitingOrBackgroundResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void hangupForegroundResumeBackgroundResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void switchWaitingOrHoldingAndActiveResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void conferenceResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void rejectCallResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getLastCallFailCauseResponse(RadioResponseInfo radioResponseInfo, LastCallFailCauseInfo lastCallFailCauseInfo) {
        responseLastCallFailCauseInfo(radioResponseInfo, lastCallFailCauseInfo);
    }

    public void getSignalStrengthResponse(RadioResponseInfo radioResponseInfo, SignalStrength signalStrength) {
        responseNotSupported(radioResponseInfo);
    }

    public void getSignalStrengthResponse_1_2(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_2.SignalStrength signalStrength) {
        responseNotSupported(radioResponseInfo);
    }

    public void getSignalStrengthResponse_1_4(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_4.SignalStrength signalStrength) {
        responseSignalStrength(radioResponseInfo, signalStrength);
    }

    public void getSignalStrengthResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_6.SignalStrength signalStrength) {
        responseSignalStrength_1_6(radioResponseInfo, signalStrength);
    }

    public void getVoiceRegistrationStateResponse(RadioResponseInfo radioResponseInfo, VoiceRegStateResult voiceRegStateResult) {
        responseNotSupported(radioResponseInfo);
    }

    public void getVoiceRegistrationStateResponse_1_2(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_2.VoiceRegStateResult voiceRegStateResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, voiceRegStateResult);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) voiceRegStateResult);
        }
    }

    public void getVoiceRegistrationStateResponse_1_5(RadioResponseInfo radioResponseInfo, RegStateResult regStateResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse == null) {
            return;
        }
        int i = radioResponseInfo.error;
        if (i == 6) {
            final int request = rILRequestProcessResponse.getRequest();
            final Message result = rILRequestProcessResponse.getResult();
            this.mRil.mRilHandler.post(new Runnable() { // from class: com.android.internal.telephony.RadioResponse$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getVoiceRegistrationStateResponse_1_5$0(request, result);
                }
            });
            this.mRil.processResponseFallback(rILRequestProcessResponse, radioResponseInfo, regStateResult);
            return;
        }
        if (i == 0) {
            sendMessageResponse(rILRequestProcessResponse.mResult, regStateResult);
        }
        this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) regStateResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getVoiceRegistrationStateResponse_1_5$0(int i, Message message) {
        this.mRil.setCompatVersion(i, RIL.RADIO_HAL_VERSION_1_4);
        this.mRil.getVoiceRegistrationState(message);
    }

    public void getVoiceRegistrationStateResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_6.RegStateResult regStateResult) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, regStateResult);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, regStateResult);
        }
    }

    public void getDataRegistrationStateResponse(RadioResponseInfo radioResponseInfo, DataRegStateResult dataRegStateResult) {
        responseNotSupported(radioResponseInfo);
    }

    public void getDataRegistrationStateResponse_1_2(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_2.DataRegStateResult dataRegStateResult) {
        responseNotSupported(radioResponseInfo);
    }

    public void getDataRegistrationStateResponse_1_4(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_4.DataRegStateResult dataRegStateResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, dataRegStateResult);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) dataRegStateResult);
        }
    }

    public void getDataRegistrationStateResponse_1_5(RadioResponseInfo radioResponseInfo, RegStateResult regStateResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse == null) {
            return;
        }
        int i = radioResponseInfo.error;
        if (i == 6) {
            final int request = rILRequestProcessResponse.getRequest();
            final Message result = rILRequestProcessResponse.getResult();
            this.mRil.mRilHandler.post(new Runnable() { // from class: com.android.internal.telephony.RadioResponse$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getDataRegistrationStateResponse_1_5$1(request, result);
                }
            });
            this.mRil.processResponseFallback(rILRequestProcessResponse, radioResponseInfo, regStateResult);
            return;
        }
        if (i == 0) {
            sendMessageResponse(rILRequestProcessResponse.mResult, regStateResult);
        }
        this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) regStateResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getDataRegistrationStateResponse_1_5$1(int i, Message message) {
        this.mRil.setCompatVersion(i, RIL.RADIO_HAL_VERSION_1_4);
        this.mRil.getDataRegistrationState(message);
    }

    public void getDataRegistrationStateResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_6.RegStateResult regStateResult) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, regStateResult);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, regStateResult);
        }
    }

    public void getOperatorResponse(RadioResponseInfo radioResponseInfo, String str, String str2, String str3) {
        responseStrings(radioResponseInfo, str, str2, str3);
    }

    public void setRadioPowerResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
        this.mRil.mLastRadioPowerResult = radioResponseInfo.error;
    }

    public void sendDtmfResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void sendSmsResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms(radioResponseInfo, sendSmsResult);
    }

    public void sendSmsResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms_1_6(radioResponseInfo, sendSmsResult);
    }

    public void sendSMSExpectMoreResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms(radioResponseInfo, sendSmsResult);
    }

    public void sendSmsExpectMoreResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms_1_6(radioResponseInfo, sendSmsResult);
    }

    public void setupDataCallResponse(RadioResponseInfo radioResponseInfo, SetupDataCallResult setupDataCallResult) {
        responseNotSupported(radioResponseInfo);
    }

    public void setupDataCallResponse_1_4(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_4.SetupDataCallResult setupDataCallResult) {
        responseSetupDataCall(radioResponseInfo, setupDataCallResult);
    }

    public void setupDataCallResponse_1_5(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_5.SetupDataCallResult setupDataCallResult) {
        responseSetupDataCall(radioResponseInfo, setupDataCallResult);
    }

    public void setupDataCallResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_6.SetupDataCallResult setupDataCallResult) {
        responseSetupDataCall_1_6(radioResponseInfo, setupDataCallResult);
    }

    public void getDataCallListResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_6.SetupDataCallResult> arrayList) {
        responseDataCallList(radioResponseInfo, arrayList);
    }

    public void setSimCardPowerResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        responseVoid_1_6(radioResponseInfo);
    }

    public void setAllowedNetworkTypesBitmapResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        responseVoid_1_6(radioResponseInfo);
    }

    public void getAllowedNetworkTypesBitmapResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, int i) {
        int iConvertHalNetworkTypeBitMask = RILUtils.convertHalNetworkTypeBitMask(i);
        this.mRil.mAllowedNetworkTypesBitmask = iConvertHalNetworkTypeBitMask;
        responseInts_1_6(radioResponseInfo, iConvertHalNetworkTypeBitMask);
    }

    public void iccIOForAppResponse(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        responseIccIo(radioResponseInfo, iccIoResult);
    }

    public void sendUssdResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void cancelPendingUssdResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getClirResponse(RadioResponseInfo radioResponseInfo, int i, int i2) {
        responseInts(radioResponseInfo, i, i2);
    }

    public void setClirResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getCallForwardStatusResponse(RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_0.CallForwardInfo> arrayList) {
        responseCallForwardInfo(radioResponseInfo, arrayList);
    }

    public void setCallForwardResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getCallWaitingResponse(RadioResponseInfo radioResponseInfo, boolean z, int i) {
        responseInts(radioResponseInfo, z ? 1 : 0, i);
    }

    public void setCallWaitingResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void acknowledgeLastIncomingGsmSmsResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void acceptCallResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void deactivateDataCallResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getFacilityLockForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void setFacilityLockForAppResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void setBarringPasswordResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getNetworkSelectionModeResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        responseInts(radioResponseInfo, z ? 1 : 0);
    }

    public void setNetworkSelectionModeAutomaticResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setNetworkSelectionModeManualResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setNetworkSelectionModeManualResponse_1_5(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getAvailableNetworksResponse(RadioResponseInfo radioResponseInfo, ArrayList<OperatorInfo> arrayList) {
        responseOperatorInfos(radioResponseInfo, arrayList);
    }

    public void startNetworkScanResponse(RadioResponseInfo radioResponseInfo) {
        responseNotSupported(radioResponseInfo);
    }

    public void startNetworkScanResponse_1_4(RadioResponseInfo radioResponseInfo) {
        responseScanStatus(radioResponseInfo, null);
    }

    public void startNetworkScanResponse_1_5(RadioResponseInfo radioResponseInfo) {
        responseScanStatus(radioResponseInfo, RIL.RADIO_HAL_VERSION_1_4);
    }

    public void stopNetworkScanResponse(RadioResponseInfo radioResponseInfo) {
        responseScanStatus(radioResponseInfo, null);
    }

    public void startDtmfResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void stopDtmfResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getBasebandVersionResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    public void separateConnectionResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setMuteResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getMuteResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        responseInts(radioResponseInfo, z ? 1 : 0);
    }

    public void getClipResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void getDataCallListResponse(RadioResponseInfo radioResponseInfo, ArrayList<SetupDataCallResult> arrayList) {
        responseNotSupported(radioResponseInfo);
    }

    public void getDataCallListResponse_1_4(RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_4.SetupDataCallResult> arrayList) {
        responseDataCallList(radioResponseInfo, arrayList);
    }

    public void getDataCallListResponse_1_5(RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_5.SetupDataCallResult> arrayList) {
        responseDataCallList(radioResponseInfo, arrayList);
    }

    public void setSuppServiceNotificationsResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void writeSmsToSimResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void deleteSmsOnSimResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setBandModeResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getAvailableBandModesResponse(RadioResponseInfo radioResponseInfo, ArrayList<Integer> arrayList) {
        responseIntArrayList(radioResponseInfo, arrayList);
    }

    public void sendEnvelopeResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    public void sendTerminalResponseToSimResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void handleStkCallSetupRequestFromSimResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void explicitCallTransferResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setPreferredNetworkTypeResponse(RadioResponseInfo radioResponseInfo) {
        responseNotSupported(radioResponseInfo);
    }

    public void setPreferredNetworkTypeBitmapResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getPreferredNetworkTypeResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseNotSupported(radioResponseInfo);
    }

    public void getPreferredNetworkTypeBitmapResponse(RadioResponseInfo radioResponseInfo, int i) {
        int iConvertHalNetworkTypeBitMask = RILUtils.convertHalNetworkTypeBitMask(i);
        this.mRil.mAllowedNetworkTypesBitmask = iConvertHalNetworkTypeBitMask;
        responseInts(radioResponseInfo, iConvertHalNetworkTypeBitMask);
    }

    public void getNeighboringCidsResponse(RadioResponseInfo radioResponseInfo, ArrayList<NeighboringCell> arrayList) {
        responseCellList(radioResponseInfo, arrayList);
    }

    public void setLocationUpdatesResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setCdmaSubscriptionSourceResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setCdmaRoamingPreferenceResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getCdmaRoamingPreferenceResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void setTTYModeResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getTTYModeResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void setPreferredVoicePrivacyResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getPreferredVoicePrivacyResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        responseInts(radioResponseInfo, z ? 1 : 0);
    }

    public void sendCDMAFeatureCodeResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void sendBurstDtmfResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void sendCdmaSmsResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms(radioResponseInfo, sendSmsResult);
    }

    public void sendCdmaSmsResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms_1_6(radioResponseInfo, sendSmsResult);
    }

    public void sendCdmaSmsExpectMoreResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms(radioResponseInfo, sendSmsResult);
    }

    public void sendCdmaSmsExpectMoreResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms_1_6(radioResponseInfo, sendSmsResult);
    }

    public void setDataThrottlingResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        responseVoid_1_6(radioResponseInfo);
    }

    public void acknowledgeLastIncomingCdmaSmsResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getGsmBroadcastConfigResponse(RadioResponseInfo radioResponseInfo, ArrayList<GsmBroadcastSmsConfigInfo> arrayList) {
        responseGmsBroadcastConfig(radioResponseInfo, arrayList);
    }

    public void setGsmBroadcastConfigResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setGsmBroadcastActivationResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getCdmaBroadcastConfigResponse(RadioResponseInfo radioResponseInfo, ArrayList<CdmaBroadcastSmsConfigInfo> arrayList) {
        responseCdmaBroadcastConfig(radioResponseInfo, arrayList);
    }

    public void setCdmaBroadcastConfigResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setCdmaBroadcastActivationResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getCDMASubscriptionResponse(RadioResponseInfo radioResponseInfo, String str, String str2, String str3, String str4, String str5) {
        responseStrings(radioResponseInfo, str, str2, str3, str4, str5);
    }

    public void writeSmsToRuimResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void deleteSmsOnRuimResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getDeviceIdentityResponse(RadioResponseInfo radioResponseInfo, String str, String str2, String str3, String str4) {
        responseStrings(radioResponseInfo, str, str2, str3, str4);
    }

    public void exitEmergencyCallbackModeResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getSmscAddressResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, MiuiSmsControllerStub.get(this.mRil.mPhoneId.intValue()).getSmscAddressResponse(this.mRil, str, new Object[0]));
    }

    public void setSmscAddressResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void reportSmsMemoryStatusResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void reportStkServiceIsRunningResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getCdmaSubscriptionSourceResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void requestIsimAuthenticationResponse(RadioResponseInfo radioResponseInfo, String str) {
        throw new RuntimeException("Inexplicable response received for requestIsimAuthentication");
    }

    public void acknowledgeIncomingGsmSmsWithPduResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void sendEnvelopeWithStatusResponse(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        responseIccIo(radioResponseInfo, iccIoResult);
    }

    public void getVoiceRadioTechnologyResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseInts(radioResponseInfo, i);
    }

    public void getCellInfoListResponse(RadioResponseInfo radioResponseInfo, ArrayList<CellInfo> arrayList) {
        responseNotSupported(radioResponseInfo);
    }

    public void getCellInfoListResponse_1_2(RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_2.CellInfo> arrayList) {
        responseNotSupported(radioResponseInfo);
    }

    public void getCellInfoListResponse_1_4(RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_4.CellInfo> arrayList) {
        responseCellInfoList(radioResponseInfo, arrayList);
    }

    public void getCellInfoListResponse_1_5(RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_5.CellInfo> arrayList) {
        responseCellInfoList(radioResponseInfo, arrayList);
    }

    public void getCellInfoListResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_6.CellInfo> arrayList) {
        responseCellInfoList_1_6(radioResponseInfo, arrayList);
    }

    public void setCellInfoListRateResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setInitialAttachApnResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setInitialAttachApnResponse_1_5(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getImsRegistrationStateResponse(RadioResponseInfo radioResponseInfo, boolean z, int i) {
        responseInts(radioResponseInfo, z ? 1 : 0, i == 0 ? 1 : 2);
    }

    public void sendImsSmsResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        responseSms(radioResponseInfo, sendSmsResult);
    }

    public void iccTransmitApduBasicChannelResponse(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        responseIccIo(radioResponseInfo, iccIoResult);
    }

    public void iccOpenLogicalChannelResponse(RadioResponseInfo radioResponseInfo, int i, ArrayList<Byte> arrayList) {
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        arrayList2.add(Integer.valueOf(i));
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(Integer.valueOf(arrayList.get(i2).byteValue()));
        }
        responseIntArrayList(radioResponseInfo, arrayList2);
    }

    public void iccCloseLogicalChannelResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void iccTransmitApduLogicalChannelResponse(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        responseIccIo(radioResponseInfo, iccIoResult);
    }

    public void nvReadItemResponse(RadioResponseInfo radioResponseInfo, String str) {
        responseString(radioResponseInfo, str);
    }

    public void nvWriteItemResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void nvWriteCdmaPrlResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void nvResetConfigResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setUiccSubscriptionResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setDataAllowedResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getHardwareConfigResponse(RadioResponseInfo radioResponseInfo, ArrayList<android.hardware.radio.V1_0.HardwareConfig> arrayList) {
        responseHardwareConfig(radioResponseInfo, arrayList);
    }

    public void requestIccSimAuthenticationResponse(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            com.android.internal.telephony.uicc.IccIoResult iccIoResult2 = new com.android.internal.telephony.uicc.IccIoResult(iccIoResult.sw1, iccIoResult.sw2, TextUtils.isEmpty(iccIoResult.simResponse) ? null : iccIoResult.simResponse.getBytes());
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, iccIoResult2);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) iccIoResult2);
        }
    }

    public void setDataProfileResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setDataProfileResponse_1_5(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void requestShutdownResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getRadioCapabilityResponse(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_0.RadioCapability radioCapability) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            RadioCapability radioCapabilityConvertHalRadioCapability = RILUtils.convertHalRadioCapability(radioCapability, this.mRil);
            int i = radioResponseInfo.error;
            if (i == 6 || i == 2) {
                radioCapabilityConvertHalRadioCapability = this.mRil.makeStaticRadioCapability();
                radioResponseInfo.error = 0;
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, radioCapabilityConvertHalRadioCapability);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) radioCapabilityConvertHalRadioCapability);
        }
    }

    public void setRadioCapabilityResponse(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_0.RadioCapability radioCapability) {
        responseRadioCapability(radioResponseInfo, radioCapability);
    }

    public void startLceServiceResponse(RadioResponseInfo radioResponseInfo, LceStatusInfo lceStatusInfo) {
        responseNotSupported(radioResponseInfo);
    }

    public void stopLceServiceResponse(RadioResponseInfo radioResponseInfo, LceStatusInfo lceStatusInfo) {
        responseNotSupported(radioResponseInfo);
    }

    public void pullLceDataResponse(RadioResponseInfo radioResponseInfo, LceDataInfo lceDataInfo) {
        responseNotSupported(radioResponseInfo);
    }

    public void getModemActivityInfoResponse(RadioResponseInfo radioResponseInfo, ActivityStatsInfo activityStatsInfo) {
        responseActivityData(radioResponseInfo, activityStatsInfo);
    }

    public void isNrDualConnectivityEnabledResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, boolean z) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, Boolean.valueOf(z));
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, Boolean.valueOf(z));
        }
    }

    public void setNrDualConnectivityStateResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        responseVoid_1_6(radioResponseInfo);
    }

    public void setAllowedCarriersResponse(RadioResponseInfo radioResponseInfo, int i) {
        responseNotSupported(radioResponseInfo);
    }

    public void setAllowedCarriersResponse_1_4(RadioResponseInfo radioResponseInfo) {
        int i;
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            this.mRil.riljLog("setAllowedCarriersResponse_1_4 - error = " + radioResponseInfo.error);
            if (radioResponseInfo.error == 0) {
                i = 0;
                sendMessageResponse(rILRequestProcessResponse.mResult, 0);
            } else {
                i = 2;
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) Integer.valueOf(i));
        }
    }

    public void getAllowedCarriersResponse(RadioResponseInfo radioResponseInfo, boolean z, CarrierRestrictions carrierRestrictions) {
        responseNotSupported(radioResponseInfo);
    }

    public void getAllowedCarriersResponse_1_4(RadioResponseInfo radioResponseInfo, CarrierRestrictionsWithPriority carrierRestrictionsWithPriority, int i) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse == null) {
            return;
        }
        CarrierRestrictionRules carrierRestrictionRulesBuild = CarrierRestrictionRules.newBuilder().setAllowedCarriers(RILUtils.convertHalCarrierList(carrierRestrictionsWithPriority.allowedCarriers)).setExcludedCarriers(RILUtils.convertHalCarrierList(carrierRestrictionsWithPriority.excludedCarriers)).setDefaultCarrierRestriction(1 ^ (carrierRestrictionsWithPriority.allowedCarriersPrioritized ? 1 : 0)).setMultiSimPolicy(i == 1 ? 1 : 0).build();
        if (radioResponseInfo.error == 0) {
            sendMessageResponse(rILRequestProcessResponse.mResult, carrierRestrictionRulesBuild);
        }
        this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) carrierRestrictionRulesBuild);
    }

    public void sendDeviceStateResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setCarrierInfoForImsiEncryptionResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setIndicationFilterResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setIndicationFilterResponse_1_5(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setSimCardPowerResponse(RadioResponseInfo radioResponseInfo) {
        responseNotSupported(radioResponseInfo);
    }

    public void setSignalStrengthReportingCriteriaResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setSignalStrengthReportingCriteriaResponse_1_5(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setLinkCapacityReportingCriteriaResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setLinkCapacityReportingCriteriaResponse_1_5(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void setSimCardPowerResponse_1_1(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void startKeepaliveResponse(RadioResponseInfo radioResponseInfo, KeepaliveStatus keepaliveStatus) {
        com.android.internal.telephony.data.KeepaliveStatus keepaliveStatus2;
        com.android.internal.telephony.data.KeepaliveStatus keepaliveStatus3;
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse == null) {
            return;
        }
        try {
            int i = radioResponseInfo.error;
            if (i == 0) {
                int iConvertHalKeepaliveStatusCode = RILUtils.convertHalKeepaliveStatusCode(keepaliveStatus.code);
                if (iConvertHalKeepaliveStatusCode < 0) {
                    keepaliveStatus2 = new com.android.internal.telephony.data.KeepaliveStatus(1);
                } else {
                    keepaliveStatus2 = new com.android.internal.telephony.data.KeepaliveStatus(keepaliveStatus.sessionHandle, iConvertHalKeepaliveStatusCode);
                }
                sendMessageResponse(rILRequestProcessResponse.mResult, keepaliveStatus2);
                keepaliveStatus3 = keepaliveStatus2;
            } else if (i == 6) {
                keepaliveStatus3 = new com.android.internal.telephony.data.KeepaliveStatus(1);
            } else if (i == 42) {
                keepaliveStatus3 = new com.android.internal.telephony.data.KeepaliveStatus(2);
            } else {
                keepaliveStatus3 = new com.android.internal.telephony.data.KeepaliveStatus(3);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) keepaliveStatus3);
        } catch (Throwable th) {
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) null);
            throw th;
        }
    }

    public void stopKeepaliveResponse(RadioResponseInfo radioResponseInfo) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse == null) {
            return;
        }
        try {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, null);
            }
        } finally {
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) null);
        }
    }

    public void getSimPhonebookRecordsResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        responseVoid_1_6(radioResponseInfo);
    }

    public void getSimPhonebookCapacityResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, PhonebookCapacity phonebookCapacity) {
        responseAdnCapacity(radioResponseInfo, RILUtils.convertHalPhonebookCapacity(phonebookCapacity));
    }

    public void updateSimPhonebookRecordsResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, int i) {
        responseInts_1_6(radioResponseInfo, i);
    }

    private void responseAdnCapacity(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, AdnCapacity adnCapacity) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, adnCapacity);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, adnCapacity);
        }
    }

    private void responseIccCardStatus(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_4.CardStatus cardStatus) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            IccCardStatus iccCardStatusConvertHalCardStatus = RILUtils.convertHalCardStatus(cardStatus.base.base);
            IccSlotPortMapping iccSlotPortMapping = new IccSlotPortMapping();
            android.hardware.radio.V1_2.CardStatus cardStatus2 = cardStatus.base;
            iccSlotPortMapping.mPhysicalSlotIndex = cardStatus2.physicalSlotId;
            iccCardStatusConvertHalCardStatus.mSlotPortMapping = iccSlotPortMapping;
            iccCardStatusConvertHalCardStatus.atr = cardStatus2.atr;
            iccCardStatusConvertHalCardStatus.iccid = cardStatus2.iccid;
            iccCardStatusConvertHalCardStatus.eid = cardStatus.eid;
            this.mRil.riljLog("responseIccCardStatus: from HIDL: " + iccCardStatusConvertHalCardStatus);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, iccCardStatusConvertHalCardStatus);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) iccCardStatusConvertHalCardStatus);
        }
    }

    private void responseIccCardStatus_1_5(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_5.CardStatus cardStatus) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            IccCardStatus iccCardStatusConvertHalCardStatus = RILUtils.convertHalCardStatus(cardStatus);
            this.mRil.riljLog("responseIccCardStatus: from HIDL: " + iccCardStatusConvertHalCardStatus);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, iccCardStatusConvertHalCardStatus);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) iccCardStatusConvertHalCardStatus);
        }
    }

    public void emergencyDialResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void responseInts(RadioResponseInfo radioResponseInfo, int... iArr) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        responseIntArrayList(radioResponseInfo, arrayList);
    }

    public void responseInts_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, int... iArr) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        responseIntArrayList_1_6(radioResponseInfo, arrayList);
    }

    public static void responseInts(int i, RIL ril, android.hardware.radio.RadioResponseInfo radioResponseInfo, int... iArr) {
        ArrayList arrayList = new ArrayList();
        for (int i2 : iArr) {
            arrayList.add(Integer.valueOf(i2));
        }
        responseIntArrayList(i, ril, radioResponseInfo, arrayList);
    }

    public void responseIntArrayList(RadioResponseInfo radioResponseInfo, ArrayList<Integer> arrayList) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int[] iArr = new int[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                iArr[i] = arrayList.get(i).intValue();
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, iArr);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) iArr);
        }
    }

    public void responseIntArrayList_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, ArrayList<Integer> arrayList) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            int[] iArr = new int[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                iArr[i] = arrayList.get(i).intValue();
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, iArr);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, iArr);
        }
    }

    public static void responseIntArrayList(int i, RIL ril, android.hardware.radio.RadioResponseInfo radioResponseInfo, ArrayList<Integer> arrayList) {
        RILRequest rILRequestProcessResponse = ril.processResponse(i, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int[] iArr = new int[arrayList.size()];
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                iArr[i2] = arrayList.get(i2).intValue();
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, iArr);
            }
            ril.processResponseDone(rILRequestProcessResponse, radioResponseInfo, iArr);
        }
    }

    private void responseCurrentCalls(RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int size = arrayList.size();
            ArrayList arrayList2 = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                DriverCall driverCallConvertToDriverCall = RILUtils.convertToDriverCall(arrayList.get(i));
                arrayList2.add(driverCallConvertToDriverCall);
                boolean z = driverCallConvertToDriverCall.isVoicePrivacy;
                RIL ril = this.mRil;
                if (z) {
                    ril.mVoicePrivacyOnRegistrants.notifyRegistrants();
                    this.mRil.riljLog("InCall VoicePrivacy is enabled");
                } else {
                    ril.mVoicePrivacyOffRegistrants.notifyRegistrants();
                    this.mRil.riljLog("InCall VoicePrivacy is disabled");
                }
            }
            Collections.sort(arrayList2);
            if (size == 0 && this.mRil.mTestingEmergencyCall.getAndSet(false)) {
                RIL ril2 = this.mRil;
                if (ril2.mEmergencyCallbackModeRegistrant != null) {
                    ril2.riljLog("responseCurrentCalls: call ended, testing emergency call, notify ECM Registrants");
                    this.mRil.mEmergencyCallbackModeRegistrant.notifyRegistrant();
                }
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, arrayList2);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) arrayList2);
        }
    }

    private void responseCurrentCalls_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            int size = arrayList.size();
            ArrayList arrayList2 = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                DriverCall driverCallConvertToDriverCall = RILUtils.convertToDriverCall(arrayList.get(i));
                arrayList2.add(driverCallConvertToDriverCall);
                boolean z = driverCallConvertToDriverCall.isVoicePrivacy;
                RIL ril = this.mRil;
                if (z) {
                    ril.mVoicePrivacyOnRegistrants.notifyRegistrants();
                    this.mRil.riljLog("InCall VoicePrivacy is enabled");
                } else {
                    ril.mVoicePrivacyOffRegistrants.notifyRegistrants();
                    this.mRil.riljLog("InCall VoicePrivacy is disabled");
                }
            }
            Collections.sort(arrayList2);
            if (size == 0 && this.mRil.mTestingEmergencyCall.getAndSet(false)) {
                RIL ril2 = this.mRil;
                if (ril2.mEmergencyCallbackModeRegistrant != null) {
                    ril2.riljLog("responseCurrentCalls: call ended, testing emergency call, notify ECM Registrants");
                    this.mRil.mEmergencyCallbackModeRegistrant.notifyRegistrant();
                }
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, arrayList2);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, arrayList2);
        }
    }

    public void responseNotSupported(RadioResponseInfo radioResponseInfo) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            this.mRil.riljLog(RILUtils.requestToString(rILRequestProcessResponse.mRequest) + "not supported on IRadio < 1.4");
            radioResponseInfo.error = 6;
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) null);
        }
    }

    public void responseVoid(RadioResponseInfo radioResponseInfo) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, null);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) null);
        }
    }

    public void responseVoid_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, null);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, null);
        }
    }

    public static void responseVoid(int i, RIL ril, android.hardware.radio.RadioResponseInfo radioResponseInfo) {
        RILRequest rILRequestProcessResponse = ril.processResponse(i, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, null);
            }
            ril.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) null);
        }
    }

    public void responseString(RadioResponseInfo radioResponseInfo, String str) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, str);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) str);
        }
    }

    public static void responseString(int i, RIL ril, android.hardware.radio.RadioResponseInfo radioResponseInfo, String str) {
        RILRequest rILRequestProcessResponse = ril.processResponse(i, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, str);
            }
            ril.processResponseDone(rILRequestProcessResponse, radioResponseInfo, str);
        }
    }

    public void responseStrings(RadioResponseInfo radioResponseInfo, String... strArr) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            arrayList.add(str);
        }
        responseStringArrayList(this.mRil, radioResponseInfo, arrayList);
    }

    public static void responseStrings(int i, RIL ril, android.hardware.radio.RadioResponseInfo radioResponseInfo, String... strArr) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            arrayList.add(str);
        }
        responseStringArrayList(i, ril, radioResponseInfo, arrayList);
    }

    public static void responseStringArrayList(RIL ril, RadioResponseInfo radioResponseInfo, ArrayList<String> arrayList) {
        RILRequest rILRequestProcessResponse = ril.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            String[] strArr = new String[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                strArr[i] = arrayList.get(i);
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, strArr);
            }
            ril.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) strArr);
        }
    }

    public static void responseStringArrayList(int i, RIL ril, android.hardware.radio.RadioResponseInfo radioResponseInfo, ArrayList<String> arrayList) {
        RILRequest rILRequestProcessResponse = ril.processResponse(i, radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            String[] strArr = new String[arrayList.size()];
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                strArr[i2] = arrayList.get(i2);
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, strArr);
            }
            ril.processResponseDone(rILRequestProcessResponse, radioResponseInfo, strArr);
        }
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
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) lastCallFailCause);
        }
    }

    private void responseSignalStrength(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_4.SignalStrength signalStrength) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            android.telephony.SignalStrength signalStrengthConvertHalSignalStrength = RILUtils.convertHalSignalStrength(signalStrength);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, signalStrengthConvertHalSignalStrength);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) signalStrengthConvertHalSignalStrength);
        }
    }

    private void responseSignalStrength_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_6.SignalStrength signalStrength) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            android.telephony.SignalStrength signalStrengthConvertHalSignalStrength = RILUtils.convertHalSignalStrength(signalStrength);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, signalStrengthConvertHalSignalStrength);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, signalStrengthConvertHalSignalStrength);
        }
    }

    private void responseSms(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            SmsResponse smsResponse = new SmsResponse(sendSmsResult.messageRef, sendSmsResult.ackPDU, sendSmsResult.errorCode, RIL.getOutgoingSmsMessageId(rILRequestProcessResponse.mResult));
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, smsResponse);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) smsResponse);
        }
    }

    private void responseSms_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            SmsResponse smsResponse = new SmsResponse(sendSmsResult.messageRef, sendSmsResult.ackPDU, sendSmsResult.errorCode, RIL.getOutgoingSmsMessageId(rILRequestProcessResponse_1_6.mResult));
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, smsResponse);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, smsResponse);
        }
    }

    private void responseSetupDataCall(RadioResponseInfo radioResponseInfo, Object obj) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            DataCallResponse dataCallResponseConvertHalDataCallResult = RILUtils.convertHalDataCallResult(obj);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, dataCallResponseConvertHalDataCallResult);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) dataCallResponseConvertHalDataCallResult);
        }
    }

    private void responseSetupDataCall_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, Object obj) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            DataCallResponse dataCallResponseConvertHalDataCallResult = RILUtils.convertHalDataCallResult(obj);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, dataCallResponseConvertHalDataCallResult);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, dataCallResponseConvertHalDataCallResult);
        }
    }

    private void responseIccIo(RadioResponseInfo radioResponseInfo, IccIoResult iccIoResult) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            com.android.internal.telephony.uicc.IccIoResult iccIoResult2 = new com.android.internal.telephony.uicc.IccIoResult(iccIoResult.sw1, iccIoResult.sw2, iccIoResult.simResponse);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, iccIoResult2);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) iccIoResult2);
        }
    }

    private void responseCallForwardInfo(RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            CallForwardInfo[] callForwardInfoArr = new CallForwardInfo[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                CallForwardInfo callForwardInfo = new CallForwardInfo();
                callForwardInfoArr[i] = callForwardInfo;
                callForwardInfo.status = ((android.hardware.radio.V1_0.CallForwardInfo) arrayList.get(i)).status;
                callForwardInfoArr[i].reason = ((android.hardware.radio.V1_0.CallForwardInfo) arrayList.get(i)).reason;
                callForwardInfoArr[i].serviceClass = ((android.hardware.radio.V1_0.CallForwardInfo) arrayList.get(i)).serviceClass;
                callForwardInfoArr[i].toa = ((android.hardware.radio.V1_0.CallForwardInfo) arrayList.get(i)).toa;
                callForwardInfoArr[i].number = ((android.hardware.radio.V1_0.CallForwardInfo) arrayList.get(i)).number;
                callForwardInfoArr[i].timeSeconds = ((android.hardware.radio.V1_0.CallForwardInfo) arrayList.get(i)).timeSeconds;
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, callForwardInfoArr);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) callForwardInfoArr);
        }
    }

    private void responseOperatorInfos(RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < arrayList.size(); i++) {
                arrayList2.add(new OperatorInfo(((OperatorInfo) arrayList.get(i)).alphaLong, ((OperatorInfo) arrayList.get(i)).alphaShort, ((OperatorInfo) arrayList.get(i)).operatorNumeric, RILUtils.convertHalOperatorStatus(((OperatorInfo) arrayList.get(i)).status)));
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, arrayList2);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) arrayList2);
        }
    }

    private void responseScanStatus(RadioResponseInfo radioResponseInfo, HalVersion halVersion) {
        Object[] objArr;
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse == null) {
            return;
        }
        int i = radioResponseInfo.error;
        NetworkScanResult networkScanResult = null;
        if (i == 6 && halVersion != null && (objArr = rILRequestProcessResponse.mArguments) != null && objArr.length > 0 && (objArr[0] instanceof NetworkScanRequest)) {
            final int request = rILRequestProcessResponse.getRequest();
            final Message result = rILRequestProcessResponse.getResult();
            final NetworkScanRequest networkScanRequest = (NetworkScanRequest) rILRequestProcessResponse.mArguments[0];
            this.mRil.mRilHandler.post(new Runnable() { // from class: com.android.internal.telephony.RadioResponse$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$responseScanStatus$2(request, networkScanRequest, result);
                }
            });
            this.mRil.processResponseFallback(rILRequestProcessResponse, radioResponseInfo, null);
            return;
        }
        if (i == 0) {
            NetworkScanResult networkScanResult2 = new NetworkScanResult(1, 0, (List) null);
            sendMessageResponse(rILRequestProcessResponse.mResult, networkScanResult2);
            networkScanResult = networkScanResult2;
        }
        this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) networkScanResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$responseScanStatus$2(int i, NetworkScanRequest networkScanRequest, Message message) {
        this.mRil.setCompatVersion(i, RIL.RADIO_HAL_VERSION_1_4);
        this.mRil.startNetworkScan(networkScanRequest, message);
    }

    private void responseDataCallList(RadioResponseInfo radioResponseInfo, List list) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList<DataCallResponse> arrayListConvertHalDataCallResultList = RILUtils.convertHalDataCallResultList((List<? extends Object>) list);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, arrayListConvertHalDataCallResultList);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) arrayListConvertHalDataCallResultList);
        }
    }

    private void responseDataCallList(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, List list) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            ArrayList<DataCallResponse> arrayListConvertHalDataCallResultList = RILUtils.convertHalDataCallResultList((List<? extends Object>) list);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, arrayListConvertHalDataCallResultList);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, arrayListConvertHalDataCallResultList);
        }
    }

    private void responseCellList(RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList arrayList2 = new ArrayList();
            int dataNetworkType = ((TelephonyManager) this.mRil.mContext.getSystemService("phone")).getDataNetworkType(SubscriptionManager.getSubscriptionId(this.mRil.mPhoneId.intValue()));
            if (dataNetworkType != 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    arrayList2.add(new NeighboringCellInfo(((NeighboringCell) arrayList.get(i)).rssi, ((NeighboringCell) arrayList.get(i)).cid, dataNetworkType));
                }
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, arrayList2);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) arrayList2);
        }
    }

    private void responseGmsBroadcastConfig(RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < arrayList.size(); i++) {
                arrayList2.add(new SmsBroadcastConfigInfo(((GsmBroadcastSmsConfigInfo) arrayList.get(i)).fromServiceId, ((GsmBroadcastSmsConfigInfo) arrayList.get(i)).toServiceId, ((GsmBroadcastSmsConfigInfo) arrayList.get(i)).fromCodeScheme, ((GsmBroadcastSmsConfigInfo) arrayList.get(i)).toCodeScheme, ((GsmBroadcastSmsConfigInfo) arrayList.get(i)).selected));
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, arrayList2);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) arrayList2);
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void responseCdmaBroadcastConfig(RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        int[] iArr;
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            int size = arrayList.size();
            int i = 0;
            int i2 = 1;
            if (size == 0) {
                iArr = new int[94];
                iArr[0] = 31;
                for (int i3 = 1; i3 < 94; i3 += 3) {
                    iArr[i3] = i3 / 3;
                    iArr[i3 + 1] = 1;
                    iArr[i3 + 2] = 0;
                }
            } else {
                int[] iArr2 = new int[(size * 3) + 1];
                iArr2[0] = size;
                while (i < arrayList.size()) {
                    iArr2[i2] = ((CdmaBroadcastSmsConfigInfo) arrayList.get(i)).serviceCategory;
                    iArr2[i2 + 1] = ((CdmaBroadcastSmsConfigInfo) arrayList.get(i)).language;
                    iArr2[i2 + 2] = ((CdmaBroadcastSmsConfigInfo) arrayList.get(i)).selected ? 1 : 0;
                    i++;
                    i2 += 3;
                }
                iArr = iArr2;
            }
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, iArr);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) iArr);
        }
    }

    private void responseCellInfoList(RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList<android.telephony.CellInfo> arrayListConvertHalCellInfoList = RILUtils.convertHalCellInfoList((ArrayList<Object>) arrayList);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, arrayListConvertHalCellInfoList);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) arrayListConvertHalCellInfoList);
        }
    }

    private void responseCellInfoList_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            ArrayList<android.telephony.CellInfo> arrayListConvertHalCellInfoList = RILUtils.convertHalCellInfoList((ArrayList<Object>) arrayList);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, arrayListConvertHalCellInfoList);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, arrayListConvertHalCellInfoList);
        }
    }

    private void responseActivityData(RadioResponseInfo radioResponseInfo, ActivityStatsInfo activityStatsInfo) {
        ModemActivityInfo modemActivityInfo;
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                int i = activityStatsInfo.sleepModeTimeMs;
                int i2 = activityStatsInfo.idleModeTimeMs;
                int[] iArr = new int[ModemActivityInfo.getNumTxPowerLevels()];
                for (int i3 = 0; i3 < ModemActivityInfo.getNumTxPowerLevels(); i3++) {
                    iArr[i3] = activityStatsInfo.txmModetimeMs[i3];
                }
                modemActivityInfo = new ModemActivityInfo(SystemClock.elapsedRealtime(), i, i2, iArr, activityStatsInfo.rxModeTimeMs);
            } else {
                ModemActivityInfo modemActivityInfo2 = new ModemActivityInfo(0L, 0, 0, new int[ModemActivityInfo.getNumTxPowerLevels()], 0);
                radioResponseInfo.error = 0;
                modemActivityInfo = modemActivityInfo2;
            }
            sendMessageResponse(rILRequestProcessResponse.mResult, modemActivityInfo);
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) modemActivityInfo);
        }
    }

    private void responseHardwareConfig(RadioResponseInfo radioResponseInfo, ArrayList arrayList) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            ArrayList<HardwareConfig> arrayListConvertHalHardwareConfigList = RILUtils.convertHalHardwareConfigList((ArrayList<android.hardware.radio.V1_0.HardwareConfig>) arrayList);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, arrayListConvertHalHardwareConfigList);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) arrayListConvertHalHardwareConfigList);
        }
    }

    private void responseRadioCapability(RadioResponseInfo radioResponseInfo, android.hardware.radio.V1_0.RadioCapability radioCapability) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            RadioCapability radioCapabilityConvertHalRadioCapability = RILUtils.convertHalRadioCapability(radioCapability, this.mRil);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, radioCapabilityConvertHalRadioCapability);
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) radioCapabilityConvertHalRadioCapability);
        }
    }

    public void enableModemResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getModemStackStatusResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, Boolean.valueOf(z));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) Boolean.valueOf(z));
        }
    }

    public void setSystemSelectionChannelsResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void enableUiccApplicationsResponse(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void areUiccApplicationsEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, Boolean.valueOf(z));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) Boolean.valueOf(z));
        }
    }

    public void setRadioPowerResponse_1_5(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
        RIL ril = this.mRil;
        int i = radioResponseInfo.error;
        ril.mLastRadioPowerResult = i;
        if (i == 1 || i == 0) {
            return;
        }
        AnomalyReporter.reportAnomaly(UUID.fromString(RILUtils.RADIO_POWER_FAILURE_BUGREPORT_UUID), "Radio power failure");
    }

    public void setRadioPowerResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        responseVoid_1_6(radioResponseInfo);
        RIL ril = this.mRil;
        int i = radioResponseInfo.error;
        ril.mLastRadioPowerResult = i;
        if (i == 70) {
            AnomalyReporter.reportAnomaly(UUID.fromString(RILUtils.RADIO_POWER_FAILURE_RF_HARDWARE_ISSUE_UUID), "RF HW damaged");
            return;
        }
        if (i == 71) {
            AnomalyReporter.reportAnomaly(UUID.fromString(RILUtils.RADIO_POWER_FAILURE_NO_RF_CALIBRATION_UUID), "No RF calibration data");
        } else {
            if (i == 1 || i == 0) {
                return;
            }
            AnomalyReporter.reportAnomaly(UUID.fromString(RILUtils.RADIO_POWER_FAILURE_BUGREPORT_UUID), "Radio power failure");
        }
    }

    public void setSystemSelectionChannelsResponse_1_5(RadioResponseInfo radioResponseInfo) {
        responseVoid(radioResponseInfo);
    }

    public void getSystemSelectionChannelsResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, ArrayList<RadioAccessSpecifier> arrayList) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            ArrayList arrayList2 = new ArrayList();
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                RadioAccessSpecifier radioAccessSpecifier = arrayList.get(i);
                i++;
                arrayList2.add(RILUtils.convertHalRadioAccessSpecifier(radioAccessSpecifier));
            }
            this.mRil.riljLog("getSystemSelectionChannelsResponse: from HIDL: " + arrayList2);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, arrayList2);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, arrayList2);
        }
    }

    public void getBarringInfoResponse(RadioResponseInfo radioResponseInfo, CellIdentity cellIdentity, ArrayList<BarringInfo> arrayList) {
        RILRequest rILRequestProcessResponse = this.mRil.processResponse(radioResponseInfo);
        if (rILRequestProcessResponse != null) {
            android.telephony.BarringInfo barringInfo = new android.telephony.BarringInfo(RILUtils.convertHalCellIdentity(cellIdentity), RILUtils.convertHalBarringInfoList(arrayList));
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse.mResult, barringInfo);
                this.mRil.mBarringInfoChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, barringInfo, (Throwable) null));
            }
            this.mRil.processResponseDone(rILRequestProcessResponse, radioResponseInfo, (Object) barringInfo);
        }
    }

    public void allocatePduSessionIdResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, int i) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, Integer.valueOf(i));
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, Integer.valueOf(i));
        }
    }

    public void releasePduSessionIdResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        responseVoid_1_6(radioResponseInfo);
    }

    public void startHandoverResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        responseVoid_1_6(radioResponseInfo);
    }

    public void cancelHandoverResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        responseVoid_1_6(radioResponseInfo);
    }

    public void getSlicingConfigResponse(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo, SlicingConfig slicingConfig) {
        RILRequest rILRequestProcessResponse_1_6 = this.mRil.processResponse_1_6(radioResponseInfo);
        if (rILRequestProcessResponse_1_6 != null) {
            NetworkSlicingConfig networkSlicingConfigConvertHalSlicingConfig = RILUtils.convertHalSlicingConfig(slicingConfig);
            if (radioResponseInfo.error == 0) {
                sendMessageResponse(rILRequestProcessResponse_1_6.mResult, networkSlicingConfigConvertHalSlicingConfig);
            }
            this.mRil.processResponseDone_1_6(rILRequestProcessResponse_1_6, radioResponseInfo, networkSlicingConfigConvertHalSlicingConfig);
        }
    }
}
