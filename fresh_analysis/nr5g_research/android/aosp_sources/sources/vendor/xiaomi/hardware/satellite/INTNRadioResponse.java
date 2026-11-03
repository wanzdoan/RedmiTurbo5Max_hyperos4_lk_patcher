package vendor.xiaomi.hardware.satellite;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.telephony.AsyncService;

/* JADX INFO: loaded from: classes.dex */
public interface INTNRadioResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$hardware$satellite$INTNRadioResponse".replace('$', '.');

    void acceptCallResponse(RadioResponseInfo radioResponseInfo);

    void acknowledgeIncomingGsmSmsWithPduResponse(RadioResponseInfo radioResponseInfo);

    void acknowledgeRequest(int i);

    void changeIccPin2ForAppResponse(RadioResponseInfo radioResponseInfo, int i);

    void changeIccPinForAppResponse(RadioResponseInfo radioResponseInfo, int i);

    void deactivateDataCallResponse(RadioResponseInfo radioResponseInfo);

    void dialResponse(RadioResponseInfo radioResponseInfo);

    void getAntennaStateResponse(RadioResponseInfo radioResponseInfo, int i, int i2);

    void getBasebandVersionResponse(RadioResponseInfo radioResponseInfo, String str);

    void getCPIdResponse(RadioResponseInfo radioResponseInfo, String str);

    void getCurrentCallsResponse(RadioResponseInfo radioResponseInfo, Call[] callArr);

    void getDataCallListResponse(RadioResponseInfo radioResponseInfo, SetupDataCallResult setupDataCallResult);

    void getEphemResponse(RadioResponseInfo radioResponseInfo, EphemParam ephemParam);

    void getIMSIForAppResponse(RadioResponseInfo radioResponseInfo, String str);

    void getIccCardStatusResponse(RadioResponseInfo radioResponseInfo, CardStatus cardStatus);

    void getIccidForAppResponse(RadioResponseInfo radioResponseInfo, String str);

    void getImeiForAppResponse(RadioResponseInfo radioResponseInfo, String str);

    String getInterfaceHash();

    int getInterfaceVersion();

    void getLastCallFailCauseResponse(RadioResponseInfo radioResponseInfo, LastCallFailCauseInfo lastCallFailCauseInfo);

    void getMuteResponse(RadioResponseInfo radioResponseInfo, boolean z);

    void getSARModeResponse(RadioResponseInfo radioResponseInfo, int i);

    void getSignalStrengthResponse(RadioResponseInfo radioResponseInfo, SignalStrength signalStrength);

    void getSmscAddressResponse(RadioResponseInfo radioResponseInfo, SmsCenterAddressResult smsCenterAddressResult);

    void getSpeechBitrateResponse(RadioResponseInfo radioResponseInfo, String str);

    void getVoiceRegistrationStateResponse(RadioResponseInfo radioResponseInfo, VoiceRegStateResult voiceRegStateResult);

    void hangupConnectionResponse(RadioResponseInfo radioResponseInfo);

    void queryCurrentSatellteCapabilityResponse(RadioResponseInfo radioResponseInfo, SatelliteCapability[] satelliteCapabilityArr);

    void rejectCallResponse(RadioResponseInfo radioResponseInfo);

    void requestShutdownResponse(RadioResponseInfo radioResponseInfo);

    void sendBurstDtmfResponse(RadioResponseInfo radioResponseInfo);

    void sendDtmfResponse(RadioResponseInfo radioResponseInfo);

    void sendEphemCalcResultResponse(RadioResponseInfo radioResponseInfo);

    void sendPhyemDataResponse(RadioResponseInfo radioResponseInfo);

    void sendSmsResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult);

    void sendTempResponse(RadioResponseInfo radioResponseInfo);

    void sendVoicectrlVersionResponse(RadioResponseInfo radioResponseInfo);

    void sendcustFactAtcmdresponse(RadioResponseInfo radioResponseInfo, String str);

    void setAntennaReportModeResponse(RadioResponseInfo radioResponseInfo);

    void setAntennaStateResponse(RadioResponseInfo radioResponseInfo);

    void setAtCommandForStarNetTestResponse(RadioResponseInfo radioResponseInfo);

    void setEPHEMResponse(RadioResponseInfo radioResponseInfo);

    void setMuteResponse(RadioResponseInfo radioResponseInfo);

    void setPowerSwitchResponse(RadioResponseInfo radioResponseInfo);

    void setPriorityFrequencySearchResponse(RadioResponseInfo radioResponseInfo);

    void setRFModeResponse(RadioResponseInfo radioResponseInfo);

    void setRadioPowerResponse(RadioResponseInfo radioResponseInfo);

    void setSARModeResponse(RadioResponseInfo radioResponseInfo);

    void setSatBeamResponse(RadioResponseInfo radioResponseInfo);

    void setScreenStateResponse(RadioResponseInfo radioResponseInfo);

    void setSignalStrengthReportingCriteriaResponse(RadioResponseInfo radioResponseInfo);

    void setSimCardPowerResponse(RadioResponseInfo radioResponseInfo);

    void setSmscAddressResponse(RadioResponseInfo radioResponseInfo);

    void setSpeechBitrateResponse(RadioResponseInfo radioResponseInfo);

    void setStackErrorEventNotifyResponse(RadioResponseInfo radioResponseInfo);

    void setupDataCallResponse(RadioResponseInfo radioResponseInfo, SetupDataCallResult setupDataCallResult);

    void startDtmfResponse(RadioResponseInfo radioResponseInfo);

    void stopDtmfResponse(RadioResponseInfo radioResponseInfo);

    void supplyGpsForAppResponse(RadioResponseInfo radioResponseInfo);

    void supplyIccAuthRspResponse(RadioResponseInfo radioResponseInfo);

    void supplyIccPin2ForAppResponse(RadioResponseInfo radioResponseInfo, int i);

    void supplyIccPinForAppResponse(RadioResponseInfo radioResponseInfo, int i);

    void supplyIccPuk2ForAppResponse(RadioResponseInfo radioResponseInfo, int i);

    void supplyIccPukForAppResponse(RadioResponseInfo radioResponseInfo, int i);

    void supplyImeiForAppResponse(RadioResponseInfo radioResponseInfo);

    void supplyImsiForAppResponse(RadioResponseInfo radioResponseInfo);

    public static abstract class Stub extends Binder implements INTNRadioResponse {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, INTNRadioResponse.DESCRIPTOR);
        }

        public static INTNRadioResponse asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(INTNRadioResponse.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof INTNRadioResponse)) {
                return (INTNRadioResponse) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "acceptCallResponse";
                case 2:
                    return "acknowledgeRequest";
                case 3:
                    return "changeIccPin2ForAppResponse";
                case 4:
                    return "changeIccPinForAppResponse";
                case 5:
                    return "dialResponse";
                case 6:
                    return "getBasebandVersionResponse";
                case 7:
                    return "getCurrentCallsResponse";
                case 8:
                    return "getIMSIForAppResponse";
                case 9:
                    return "getIccCardStatusResponse";
                case 10:
                    return "getIccidForAppResponse";
                case 11:
                    return "getLastCallFailCauseResponse";
                case 12:
                    return "getMuteResponse";
                case 13:
                    return "getSARModeResponse";
                case 14:
                    return "getSignalStrengthResponse";
                case 15:
                    return "getVoiceRegistrationStateResponse";
                case 16:
                    return "hangupConnectionResponse";
                case 17:
                    return "rejectCallResponse";
                case 18:
                    return "requestShutdownResponse";
                case 19:
                    return "sendBurstDtmfResponse";
                case 20:
                    return "sendDtmfResponse";
                case 21:
                    return "sendSmsResponse";
                case 22:
                    return "setMuteResponse";
                case 23:
                    return "setRadioPowerResponse";
                case 24:
                    return "setSARModeResponse";
                case 25:
                    return "startDtmfResponse";
                case 26:
                    return "stopDtmfResponse";
                case 27:
                    return "supplyIccAuthRspResponse";
                case 28:
                    return "supplyIccPin2ForAppResponse";
                case 29:
                    return "supplyIccPinForAppResponse";
                case 30:
                    return "supplyIccPuk2ForAppResponse";
                case 31:
                    return "supplyIccPukForAppResponse";
                case 32:
                    return "supplyImsiForAppResponse";
                case 33:
                    return "setSignalStrengthReportingCriteriaResponse";
                case 34:
                    return "supplyGpsForAppResponse";
                case 35:
                    return "sendTempResponse";
                case 36:
                    return "setScreenStateResponse";
                case 37:
                    return "sendcustFactAtcmdresponse";
                case 38:
                    return "sendVoicectrlVersionResponse";
                case 39:
                    return "getSmscAddressResponse";
                case 40:
                    return "setSmscAddressResponse";
                case 41:
                    return "acknowledgeIncomingGsmSmsWithPduResponse";
                case 42:
                    return "supplyImeiForAppResponse";
                case 43:
                    return "setStackErrorEventNotifyResponse";
                case 44:
                    return "getImeiForAppResponse";
                case 45:
                    return "getCPIdResponse";
                case 46:
                    return "setSatBeamResponse";
                case 47:
                    return "getSpeechBitrateResponse";
                case 48:
                    return "setSpeechBitrateResponse";
                case 49:
                    return "setRFModeResponse";
                case 50:
                    return "setEPHEMResponse";
                case 51:
                    return "sendPhyemDataResponse";
                case 52:
                    return "setAtCommandForStarNetTestResponse";
                case 53:
                    return "setPriorityFrequencySearchResponse";
                case 54:
                    return "setPowerSwitchResponse";
                case 55:
                    return "setupDataCallResponse";
                case 56:
                    return "deactivateDataCallResponse";
                case 57:
                    return "getDataCallListResponse";
                case 58:
                    return "sendEphemCalcResultResponse";
                case 59:
                    return "getEphemResponse";
                case 60:
                    return "setAntennaStateResponse";
                case 61:
                    return "getAntennaStateResponse";
                case 62:
                    return "setAntennaReportModeResponse";
                case 63:
                    return "queryCurrentSatellteCapabilityResponse";
                case 64:
                    return "setSimCardPowerResponse";
                default:
                    switch (i) {
                        case 16777214:
                            return "getInterfaceHash";
                        case AsyncService.CMD_ASYNC_SERVICE_ON_START_INTENT /* 16777215 */:
                            return "getInterfaceVersion";
                        default:
                            return null;
                    }
            }
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INTNRadioResponse.DESCRIPTOR);
            }
            if (i == 16777215) {
                parcel2.writeNoException();
                parcel2.writeInt(getInterfaceVersion());
                return true;
            }
            if (i == 16777214) {
                parcel2.writeNoException();
                parcel2.writeString(getInterfaceHash());
                return true;
            }
            switch (i) {
                case 1:
                    RadioResponseInfo radioResponseInfo = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    acceptCallResponse(radioResponseInfo);
                    return true;
                case 2:
                    int i3 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    acknowledgeRequest(i3);
                    return true;
                case 3:
                    RadioResponseInfo radioResponseInfo2 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    int i4 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    changeIccPin2ForAppResponse(radioResponseInfo2, i4);
                    return true;
                case 4:
                    RadioResponseInfo radioResponseInfo3 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    int i5 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    changeIccPinForAppResponse(radioResponseInfo3, i5);
                    return true;
                case 5:
                    RadioResponseInfo radioResponseInfo4 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    dialResponse(radioResponseInfo4);
                    return true;
                case 6:
                    RadioResponseInfo radioResponseInfo5 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    String string = parcel.readString();
                    parcel.enforceNoDataAvail();
                    getBasebandVersionResponse(radioResponseInfo5, string);
                    return true;
                case 7:
                    RadioResponseInfo radioResponseInfo6 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    Call[] callArr = (Call[]) parcel.createTypedArray(Call.CREATOR);
                    parcel.enforceNoDataAvail();
                    getCurrentCallsResponse(radioResponseInfo6, callArr);
                    return true;
                case 8:
                    RadioResponseInfo radioResponseInfo7 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    String string2 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    getIMSIForAppResponse(radioResponseInfo7, string2);
                    return true;
                case 9:
                    RadioResponseInfo radioResponseInfo8 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    CardStatus cardStatus = (CardStatus) parcel.readTypedObject(CardStatus.CREATOR);
                    parcel.enforceNoDataAvail();
                    getIccCardStatusResponse(radioResponseInfo8, cardStatus);
                    return true;
                case 10:
                    RadioResponseInfo radioResponseInfo9 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    String string3 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    getIccidForAppResponse(radioResponseInfo9, string3);
                    return true;
                case 11:
                    RadioResponseInfo radioResponseInfo10 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    LastCallFailCauseInfo lastCallFailCauseInfo = (LastCallFailCauseInfo) parcel.readTypedObject(LastCallFailCauseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    getLastCallFailCauseResponse(radioResponseInfo10, lastCallFailCauseInfo);
                    return true;
                case 12:
                    RadioResponseInfo radioResponseInfo11 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    boolean z = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    getMuteResponse(radioResponseInfo11, z);
                    return true;
                case 13:
                    RadioResponseInfo radioResponseInfo12 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    int i6 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    getSARModeResponse(radioResponseInfo12, i6);
                    return true;
                case 14:
                    RadioResponseInfo radioResponseInfo13 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    SignalStrength signalStrength = (SignalStrength) parcel.readTypedObject(SignalStrength.CREATOR);
                    parcel.enforceNoDataAvail();
                    getSignalStrengthResponse(radioResponseInfo13, signalStrength);
                    return true;
                case 15:
                    RadioResponseInfo radioResponseInfo14 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    VoiceRegStateResult voiceRegStateResult = (VoiceRegStateResult) parcel.readTypedObject(VoiceRegStateResult.CREATOR);
                    parcel.enforceNoDataAvail();
                    getVoiceRegistrationStateResponse(radioResponseInfo14, voiceRegStateResult);
                    return true;
                case 16:
                    RadioResponseInfo radioResponseInfo15 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    hangupConnectionResponse(radioResponseInfo15);
                    return true;
                case 17:
                    RadioResponseInfo radioResponseInfo16 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    rejectCallResponse(radioResponseInfo16);
                    return true;
                case 18:
                    RadioResponseInfo radioResponseInfo17 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    requestShutdownResponse(radioResponseInfo17);
                    return true;
                case 19:
                    RadioResponseInfo radioResponseInfo18 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    sendBurstDtmfResponse(radioResponseInfo18);
                    return true;
                case 20:
                    RadioResponseInfo radioResponseInfo19 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    sendDtmfResponse(radioResponseInfo19);
                    return true;
                case 21:
                    RadioResponseInfo radioResponseInfo20 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    SendSmsResult sendSmsResult = (SendSmsResult) parcel.readTypedObject(SendSmsResult.CREATOR);
                    parcel.enforceNoDataAvail();
                    sendSmsResponse(radioResponseInfo20, sendSmsResult);
                    return true;
                case 22:
                    RadioResponseInfo radioResponseInfo21 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setMuteResponse(radioResponseInfo21);
                    return true;
                case 23:
                    RadioResponseInfo radioResponseInfo22 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setRadioPowerResponse(radioResponseInfo22);
                    return true;
                case 24:
                    RadioResponseInfo radioResponseInfo23 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setSARModeResponse(radioResponseInfo23);
                    return true;
                case 25:
                    RadioResponseInfo radioResponseInfo24 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    startDtmfResponse(radioResponseInfo24);
                    return true;
                case 26:
                    RadioResponseInfo radioResponseInfo25 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    stopDtmfResponse(radioResponseInfo25);
                    return true;
                case 27:
                    RadioResponseInfo radioResponseInfo26 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    supplyIccAuthRspResponse(radioResponseInfo26);
                    return true;
                case 28:
                    RadioResponseInfo radioResponseInfo27 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    int i7 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    supplyIccPin2ForAppResponse(radioResponseInfo27, i7);
                    return true;
                case 29:
                    RadioResponseInfo radioResponseInfo28 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    int i8 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    supplyIccPinForAppResponse(radioResponseInfo28, i8);
                    return true;
                case 30:
                    RadioResponseInfo radioResponseInfo29 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    int i9 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    supplyIccPuk2ForAppResponse(radioResponseInfo29, i9);
                    return true;
                case 31:
                    RadioResponseInfo radioResponseInfo30 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    int i10 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    supplyIccPukForAppResponse(radioResponseInfo30, i10);
                    return true;
                case 32:
                    RadioResponseInfo radioResponseInfo31 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    supplyImsiForAppResponse(radioResponseInfo31);
                    return true;
                case 33:
                    RadioResponseInfo radioResponseInfo32 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setSignalStrengthReportingCriteriaResponse(radioResponseInfo32);
                    return true;
                case 34:
                    RadioResponseInfo radioResponseInfo33 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    supplyGpsForAppResponse(radioResponseInfo33);
                    return true;
                case 35:
                    RadioResponseInfo radioResponseInfo34 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    sendTempResponse(radioResponseInfo34);
                    return true;
                case 36:
                    RadioResponseInfo radioResponseInfo35 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setScreenStateResponse(radioResponseInfo35);
                    return true;
                case 37:
                    RadioResponseInfo radioResponseInfo36 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    String string4 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    sendcustFactAtcmdresponse(radioResponseInfo36, string4);
                    return true;
                case 38:
                    RadioResponseInfo radioResponseInfo37 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    sendVoicectrlVersionResponse(radioResponseInfo37);
                    return true;
                case 39:
                    RadioResponseInfo radioResponseInfo38 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    SmsCenterAddressResult smsCenterAddressResult = (SmsCenterAddressResult) parcel.readTypedObject(SmsCenterAddressResult.CREATOR);
                    parcel.enforceNoDataAvail();
                    getSmscAddressResponse(radioResponseInfo38, smsCenterAddressResult);
                    return true;
                case 40:
                    RadioResponseInfo radioResponseInfo39 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setSmscAddressResponse(radioResponseInfo39);
                    return true;
                case 41:
                    RadioResponseInfo radioResponseInfo40 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    acknowledgeIncomingGsmSmsWithPduResponse(radioResponseInfo40);
                    return true;
                case 42:
                    RadioResponseInfo radioResponseInfo41 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    supplyImeiForAppResponse(radioResponseInfo41);
                    return true;
                case 43:
                    RadioResponseInfo radioResponseInfo42 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setStackErrorEventNotifyResponse(radioResponseInfo42);
                    return true;
                case 44:
                    RadioResponseInfo radioResponseInfo43 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    String string5 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    getImeiForAppResponse(radioResponseInfo43, string5);
                    return true;
                case 45:
                    RadioResponseInfo radioResponseInfo44 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    String string6 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    getCPIdResponse(radioResponseInfo44, string6);
                    return true;
                case 46:
                    RadioResponseInfo radioResponseInfo45 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setSatBeamResponse(radioResponseInfo45);
                    return true;
                case 47:
                    RadioResponseInfo radioResponseInfo46 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    String string7 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    getSpeechBitrateResponse(radioResponseInfo46, string7);
                    return true;
                case 48:
                    RadioResponseInfo radioResponseInfo47 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setSpeechBitrateResponse(radioResponseInfo47);
                    return true;
                case 49:
                    RadioResponseInfo radioResponseInfo48 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setRFModeResponse(radioResponseInfo48);
                    return true;
                case 50:
                    RadioResponseInfo radioResponseInfo49 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setEPHEMResponse(radioResponseInfo49);
                    return true;
                case 51:
                    RadioResponseInfo radioResponseInfo50 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    sendPhyemDataResponse(radioResponseInfo50);
                    return true;
                case 52:
                    RadioResponseInfo radioResponseInfo51 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setAtCommandForStarNetTestResponse(radioResponseInfo51);
                    return true;
                case 53:
                    RadioResponseInfo radioResponseInfo52 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setPriorityFrequencySearchResponse(radioResponseInfo52);
                    return true;
                case 54:
                    RadioResponseInfo radioResponseInfo53 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setPowerSwitchResponse(radioResponseInfo53);
                    return true;
                case 55:
                    RadioResponseInfo radioResponseInfo54 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    SetupDataCallResult setupDataCallResult = (SetupDataCallResult) parcel.readTypedObject(SetupDataCallResult.CREATOR);
                    parcel.enforceNoDataAvail();
                    setupDataCallResponse(radioResponseInfo54, setupDataCallResult);
                    return true;
                case 56:
                    RadioResponseInfo radioResponseInfo55 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    deactivateDataCallResponse(radioResponseInfo55);
                    return true;
                case 57:
                    RadioResponseInfo radioResponseInfo56 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    SetupDataCallResult setupDataCallResult2 = (SetupDataCallResult) parcel.readTypedObject(SetupDataCallResult.CREATOR);
                    parcel.enforceNoDataAvail();
                    getDataCallListResponse(radioResponseInfo56, setupDataCallResult2);
                    return true;
                case 58:
                    RadioResponseInfo radioResponseInfo57 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    sendEphemCalcResultResponse(radioResponseInfo57);
                    return true;
                case 59:
                    RadioResponseInfo radioResponseInfo58 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    EphemParam ephemParam = (EphemParam) parcel.readTypedObject(EphemParam.CREATOR);
                    parcel.enforceNoDataAvail();
                    getEphemResponse(radioResponseInfo58, ephemParam);
                    return true;
                case 60:
                    RadioResponseInfo radioResponseInfo59 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setAntennaStateResponse(radioResponseInfo59);
                    return true;
                case 61:
                    RadioResponseInfo radioResponseInfo60 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    int i11 = parcel.readInt();
                    int i12 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    getAntennaStateResponse(radioResponseInfo60, i11, i12);
                    return true;
                case 62:
                    RadioResponseInfo radioResponseInfo61 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setAntennaReportModeResponse(radioResponseInfo61);
                    return true;
                case 63:
                    RadioResponseInfo radioResponseInfo62 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    SatelliteCapability[] satelliteCapabilityArr = (SatelliteCapability[]) parcel.createTypedArray(SatelliteCapability.CREATOR);
                    parcel.enforceNoDataAvail();
                    queryCurrentSatellteCapabilityResponse(radioResponseInfo62, satelliteCapabilityArr);
                    return true;
                case 64:
                    RadioResponseInfo radioResponseInfo63 = (RadioResponseInfo) parcel.readTypedObject(RadioResponseInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    setSimCardPowerResponse(radioResponseInfo63);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static final class Proxy implements INTNRadioResponse {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }
        }
    }
}
