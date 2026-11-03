package vendor.mediatek.hardware.mtkradioex.ims;

import android.hardware.radio.RadioResponseInfo;
import android.hardware.radio.messaging.SendSmsResult;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExImsResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$ims$IMtkRadioExImsResponse".replace('$', '.');
    public static final String HASH = "b6191c55369c4d5d70b8158a8e4c34dc461825de";
    public static final int VERSION = 3;

    void acknowledgeLastIncomingCdmaSmsExResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void acknowledgeLastIncomingGsmSmsExResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void cancelUssiResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void conferenceDialResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void controlCallResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void controlImsConferenceCallMemberResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void dialWithSipUriResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void eccRedialApproveResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void forceReleaseCallResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void getBarringCallsResponse(RadioResponseInfo radioResponseInfo, ImsBarringCall[] imsBarringCallArr) throws RemoteException;

    void getImsCfgFeatureValueResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getImsCfgProvisionValueResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    void getImsCfgResourceCapValueResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getVoiceDomainPreferenceResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getXcapStatusResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void imsBearerStateConfirmResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void imsEctCommandResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void pullCallResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void querySsacStatusResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void queryVopsStatusResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void rttModifyRequestResponseResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendImsSmsExResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) throws RemoteException;

    void sendRttModifyRequestResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendRttTextResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendUssiResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setBarringCallsResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setCallAdditionalInfoResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImsBearerNotificationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImsCallModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImsCfgFeatureValueResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImsCfgProvisionValueResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImsRegistrationReportResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImscfgResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setModemImsCfgResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    void setRttModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSipHeaderReportResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSipHeaderResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setVoiceDomainPreferenceResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setWfcProfileResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setupXcapUserAgentStringResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void toggleRttAudioIndicationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void videoCallAcceptResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void videoRingtoneEventResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void vtDialResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void vtDialWithSipUriResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void vtEmergencyDialResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    public static class Default implements IMtkRadioExImsResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void imsBearerStateConfirmResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImsBearerNotificationResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void acknowledgeLastIncomingGsmSmsExResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void acknowledgeLastIncomingCdmaSmsExResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void sendImsSmsExResponse(RadioResponseInfo info, SendSmsResult sms) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setRttModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void sendRttModifyRequestResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void sendRttTextResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void rttModifyRequestResponseResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void toggleRttAudioIndicationResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImsRegistrationReportResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void queryVopsStatusResponse(RadioResponseInfo info, int vops) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void sendUssiResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void cancelUssiResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getXcapStatusResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setupXcapUserAgentStringResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getBarringCallsResponse(RadioResponseInfo info, ImsBarringCall[] calls) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setBarringCallsResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void vtDialResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void vtDialWithSipUriResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void videoCallAcceptResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void videoRingtoneEventResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void dialWithSipUriResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void imsEctCommandResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void pullCallResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void eccRedialApproveResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void conferenceDialResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void forceReleaseCallResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void controlCallResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void controlImsConferenceCallMemberResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setSipHeaderResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setSipHeaderReportResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImsCallModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setCallAdditionalInfoResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImscfgResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setModemImsCfgResponse(RadioResponseInfo info, String results) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImsCfgFeatureValueResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getImsCfgFeatureValueResponse(RadioResponseInfo info, int value) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImsCfgProvisionValueResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getImsCfgProvisionValueResponse(RadioResponseInfo info, String value) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getImsCfgResourceCapValueResponse(RadioResponseInfo info, int value) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setWfcProfileResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setVoiceDomainPreferenceResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getVoiceDomainPreferenceResponse(RadioResponseInfo info, int vdp) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void querySsacStatusResponse(RadioResponseInfo info, int[] status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void vtEmergencyDialResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExImsResponse {
        static final int TRANSACTION_acknowledgeLastIncomingCdmaSmsExResponse = 4;
        static final int TRANSACTION_acknowledgeLastIncomingGsmSmsExResponse = 3;
        static final int TRANSACTION_cancelUssiResponse = 14;
        static final int TRANSACTION_conferenceDialResponse = 27;
        static final int TRANSACTION_controlCallResponse = 29;
        static final int TRANSACTION_controlImsConferenceCallMemberResponse = 30;
        static final int TRANSACTION_dialWithSipUriResponse = 23;
        static final int TRANSACTION_eccRedialApproveResponse = 26;
        static final int TRANSACTION_forceReleaseCallResponse = 28;
        static final int TRANSACTION_getBarringCallsResponse = 17;
        static final int TRANSACTION_getImsCfgFeatureValueResponse = 38;
        static final int TRANSACTION_getImsCfgProvisionValueResponse = 40;
        static final int TRANSACTION_getImsCfgResourceCapValueResponse = 41;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getVoiceDomainPreferenceResponse = 44;
        static final int TRANSACTION_getXcapStatusResponse = 15;
        static final int TRANSACTION_imsBearerStateConfirmResponse = 1;
        static final int TRANSACTION_imsEctCommandResponse = 24;
        static final int TRANSACTION_pullCallResponse = 25;
        static final int TRANSACTION_querySsacStatusResponse = 45;
        static final int TRANSACTION_queryVopsStatusResponse = 12;
        static final int TRANSACTION_rttModifyRequestResponseResponse = 9;
        static final int TRANSACTION_sendImsSmsExResponse = 5;
        static final int TRANSACTION_sendRttModifyRequestResponse = 7;
        static final int TRANSACTION_sendRttTextResponse = 8;
        static final int TRANSACTION_sendUssiResponse = 13;
        static final int TRANSACTION_setBarringCallsResponse = 18;
        static final int TRANSACTION_setCallAdditionalInfoResponse = 34;
        static final int TRANSACTION_setImsBearerNotificationResponse = 2;
        static final int TRANSACTION_setImsCallModeResponse = 33;
        static final int TRANSACTION_setImsCfgFeatureValueResponse = 37;
        static final int TRANSACTION_setImsCfgProvisionValueResponse = 39;
        static final int TRANSACTION_setImsRegistrationReportResponse = 11;
        static final int TRANSACTION_setImscfgResponse = 35;
        static final int TRANSACTION_setModemImsCfgResponse = 36;
        static final int TRANSACTION_setRttModeResponse = 6;
        static final int TRANSACTION_setSipHeaderReportResponse = 32;
        static final int TRANSACTION_setSipHeaderResponse = 31;
        static final int TRANSACTION_setVoiceDomainPreferenceResponse = 43;
        static final int TRANSACTION_setWfcProfileResponse = 42;
        static final int TRANSACTION_setupXcapUserAgentStringResponse = 16;
        static final int TRANSACTION_toggleRttAudioIndicationResponse = 10;
        static final int TRANSACTION_videoCallAcceptResponse = 21;
        static final int TRANSACTION_videoRingtoneEventResponse = 22;
        static final int TRANSACTION_vtDialResponse = 19;
        static final int TRANSACTION_vtDialWithSipUriResponse = 20;
        static final int TRANSACTION_vtEmergencyDialResponse = 46;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExImsResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExImsResponse)) {
                return (IMtkRadioExImsResponse) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= TRANSACTION_getInterfaceVersion) {
                data.enforceInterface(DESCRIPTOR);
            }
            if (code == TRANSACTION_getInterfaceVersion) {
                reply.writeNoException();
                reply.writeInt(getInterfaceVersion());
                return true;
            }
            if (code == TRANSACTION_getInterfaceHash) {
                reply.writeNoException();
                reply.writeString(getInterfaceHash());
                return true;
            }
            switch (code) {
                case 1:
                    RadioResponseInfo _arg0 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    imsBearerStateConfirmResponse(_arg0);
                    return true;
                case 2:
                    RadioResponseInfo _arg1 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setImsBearerNotificationResponse(_arg1);
                    return true;
                case 3:
                    RadioResponseInfo _arg2 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    acknowledgeLastIncomingGsmSmsExResponse(_arg2);
                    return true;
                case 4:
                    RadioResponseInfo _arg3 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    acknowledgeLastIncomingCdmaSmsExResponse(_arg3);
                    return true;
                case 5:
                    RadioResponseInfo _arg4 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    SendSmsResult _arg5 = (SendSmsResult) data.readTypedObject(SendSmsResult.CREATOR);
                    data.enforceNoDataAvail();
                    sendImsSmsExResponse(_arg4, _arg5);
                    return true;
                case 6:
                    RadioResponseInfo _arg6 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setRttModeResponse(_arg6);
                    return true;
                case 7:
                    RadioResponseInfo _arg7 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    sendRttModifyRequestResponse(_arg7);
                    return true;
                case 8:
                    RadioResponseInfo _arg8 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    sendRttTextResponse(_arg8);
                    return true;
                case 9:
                    RadioResponseInfo _arg9 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    rttModifyRequestResponseResponse(_arg9);
                    return true;
                case 10:
                    RadioResponseInfo _arg10 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    toggleRttAudioIndicationResponse(_arg10);
                    return true;
                case 11:
                    RadioResponseInfo _arg11 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setImsRegistrationReportResponse(_arg11);
                    return true;
                case 12:
                    RadioResponseInfo _arg12 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg13 = data.readInt();
                    data.enforceNoDataAvail();
                    queryVopsStatusResponse(_arg12, _arg13);
                    return true;
                case 13:
                    RadioResponseInfo _arg14 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    sendUssiResponse(_arg14);
                    return true;
                case 14:
                    RadioResponseInfo _arg15 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    cancelUssiResponse(_arg15);
                    return true;
                case 15:
                    RadioResponseInfo _arg16 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    getXcapStatusResponse(_arg16);
                    return true;
                case 16:
                    RadioResponseInfo _arg17 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setupXcapUserAgentStringResponse(_arg17);
                    return true;
                case 17:
                    RadioResponseInfo _arg18 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    ImsBarringCall[] _arg19 = (ImsBarringCall[]) data.createTypedArray(ImsBarringCall.CREATOR);
                    data.enforceNoDataAvail();
                    getBarringCallsResponse(_arg18, _arg19);
                    return true;
                case 18:
                    RadioResponseInfo _arg20 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setBarringCallsResponse(_arg20);
                    return true;
                case TRANSACTION_vtDialResponse /* 19 */:
                    RadioResponseInfo _arg21 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    vtDialResponse(_arg21);
                    return true;
                case 20:
                    RadioResponseInfo _arg22 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    vtDialWithSipUriResponse(_arg22);
                    return true;
                case TRANSACTION_videoCallAcceptResponse /* 21 */:
                    RadioResponseInfo _arg23 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    videoCallAcceptResponse(_arg23);
                    return true;
                case TRANSACTION_videoRingtoneEventResponse /* 22 */:
                    RadioResponseInfo _arg24 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    videoRingtoneEventResponse(_arg24);
                    return true;
                case TRANSACTION_dialWithSipUriResponse /* 23 */:
                    RadioResponseInfo _arg25 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    dialWithSipUriResponse(_arg25);
                    return true;
                case 24:
                    RadioResponseInfo _arg26 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    imsEctCommandResponse(_arg26);
                    return true;
                case TRANSACTION_pullCallResponse /* 25 */:
                    RadioResponseInfo _arg27 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    pullCallResponse(_arg27);
                    return true;
                case TRANSACTION_eccRedialApproveResponse /* 26 */:
                    RadioResponseInfo _arg28 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    eccRedialApproveResponse(_arg28);
                    return true;
                case TRANSACTION_conferenceDialResponse /* 27 */:
                    RadioResponseInfo _arg29 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    conferenceDialResponse(_arg29);
                    return true;
                case TRANSACTION_forceReleaseCallResponse /* 28 */:
                    RadioResponseInfo _arg30 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    forceReleaseCallResponse(_arg30);
                    return true;
                case TRANSACTION_controlCallResponse /* 29 */:
                    RadioResponseInfo _arg31 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    controlCallResponse(_arg31);
                    return true;
                case TRANSACTION_controlImsConferenceCallMemberResponse /* 30 */:
                    RadioResponseInfo _arg32 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    controlImsConferenceCallMemberResponse(_arg32);
                    return true;
                case TRANSACTION_setSipHeaderResponse /* 31 */:
                    RadioResponseInfo _arg33 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setSipHeaderResponse(_arg33);
                    return true;
                case 32:
                    RadioResponseInfo _arg34 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setSipHeaderReportResponse(_arg34);
                    return true;
                case 33:
                    RadioResponseInfo _arg35 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setImsCallModeResponse(_arg35);
                    return true;
                case TRANSACTION_setCallAdditionalInfoResponse /* 34 */:
                    RadioResponseInfo _arg36 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setCallAdditionalInfoResponse(_arg36);
                    return true;
                case TRANSACTION_setImscfgResponse /* 35 */:
                    RadioResponseInfo _arg37 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setImscfgResponse(_arg37);
                    return true;
                case 36:
                    RadioResponseInfo _arg38 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String _arg39 = data.readString();
                    data.enforceNoDataAvail();
                    setModemImsCfgResponse(_arg38, _arg39);
                    return true;
                case TRANSACTION_setImsCfgFeatureValueResponse /* 37 */:
                    RadioResponseInfo _arg40 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setImsCfgFeatureValueResponse(_arg40);
                    return true;
                case 38:
                    RadioResponseInfo _arg41 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg42 = data.readInt();
                    data.enforceNoDataAvail();
                    getImsCfgFeatureValueResponse(_arg41, _arg42);
                    return true;
                case TRANSACTION_setImsCfgProvisionValueResponse /* 39 */:
                    RadioResponseInfo _arg43 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setImsCfgProvisionValueResponse(_arg43);
                    return true;
                case 40:
                    RadioResponseInfo _arg44 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String _arg45 = data.readString();
                    data.enforceNoDataAvail();
                    getImsCfgProvisionValueResponse(_arg44, _arg45);
                    return true;
                case TRANSACTION_getImsCfgResourceCapValueResponse /* 41 */:
                    RadioResponseInfo _arg46 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg47 = data.readInt();
                    data.enforceNoDataAvail();
                    getImsCfgResourceCapValueResponse(_arg46, _arg47);
                    return true;
                case TRANSACTION_setWfcProfileResponse /* 42 */:
                    RadioResponseInfo _arg48 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setWfcProfileResponse(_arg48);
                    return true;
                case TRANSACTION_setVoiceDomainPreferenceResponse /* 43 */:
                    RadioResponseInfo _arg49 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setVoiceDomainPreferenceResponse(_arg49);
                    return true;
                case TRANSACTION_getVoiceDomainPreferenceResponse /* 44 */:
                    RadioResponseInfo _arg50 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg51 = data.readInt();
                    data.enforceNoDataAvail();
                    getVoiceDomainPreferenceResponse(_arg50, _arg51);
                    return true;
                case TRANSACTION_querySsacStatusResponse /* 45 */:
                    RadioResponseInfo _arg52 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg53 = data.createIntArray();
                    data.enforceNoDataAvail();
                    querySsacStatusResponse(_arg52, _arg53);
                    return true;
                case 46:
                    RadioResponseInfo _arg54 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    vtEmergencyDialResponse(_arg54);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExImsResponse {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void imsBearerStateConfirmResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImsBearerNotificationResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void acknowledgeLastIncomingGsmSmsExResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void acknowledgeLastIncomingCdmaSmsExResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void sendImsSmsExResponse(RadioResponseInfo info, SendSmsResult sms) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(sms, 0);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setRttModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void sendRttModifyRequestResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void sendRttTextResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void rttModifyRequestResponseResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void toggleRttAudioIndicationResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImsRegistrationReportResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void queryVopsStatusResponse(RadioResponseInfo info, int vops) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(vops);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void sendUssiResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void cancelUssiResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getXcapStatusResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setupXcapUserAgentStringResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getBarringCallsResponse(RadioResponseInfo info, ImsBarringCall[] calls) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedArray(calls, 0);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setBarringCallsResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void vtDialResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_vtDialResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void vtDialWithSipUriResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void videoCallAcceptResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_videoCallAcceptResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void videoRingtoneEventResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_videoRingtoneEventResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void dialWithSipUriResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_dialWithSipUriResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void imsEctCommandResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(24, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void pullCallResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_pullCallResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void eccRedialApproveResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_eccRedialApproveResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void conferenceDialResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_conferenceDialResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void forceReleaseCallResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_forceReleaseCallResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void controlCallResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_controlCallResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void controlImsConferenceCallMemberResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_controlImsConferenceCallMemberResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setSipHeaderResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSipHeaderResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setSipHeaderReportResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(32, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImsCallModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(33, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setCallAdditionalInfoResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setCallAdditionalInfoResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImscfgResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setImscfgResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setModemImsCfgResponse(RadioResponseInfo info, String results) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(results);
                    boolean _status = this.mRemote.transact(36, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImsCfgFeatureValueResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setImsCfgFeatureValueResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getImsCfgFeatureValueResponse(RadioResponseInfo info, int value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(38, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImsCfgProvisionValueResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setImsCfgProvisionValueResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getImsCfgProvisionValueResponse(RadioResponseInfo info, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(40, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getImsCfgResourceCapValueResponse(RadioResponseInfo info, int value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getImsCfgResourceCapValueResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setWfcProfileResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWfcProfileResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setVoiceDomainPreferenceResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setVoiceDomainPreferenceResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getVoiceDomainPreferenceResponse(RadioResponseInfo info, int vdp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(vdp);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getVoiceDomainPreferenceResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void querySsacStatusResponse(RadioResponseInfo info, int[] status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(status);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_querySsacStatusResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void vtEmergencyDialResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(46, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel data = Parcel.obtain(asBinder());
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceVersion, data, reply, 0);
                        reply.readException();
                        this.mCachedVersion = reply.readInt();
                    } finally {
                        reply.recycle();
                        data.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel data = Parcel.obtain(asBinder());
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
                        reply.readException();
                        this.mCachedHash = reply.readString();
                        reply.recycle();
                        data.recycle();
                    } catch (Throwable th) {
                        reply.recycle();
                        data.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }
    }
}
