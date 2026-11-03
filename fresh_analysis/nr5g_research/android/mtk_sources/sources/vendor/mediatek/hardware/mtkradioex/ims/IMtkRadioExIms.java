package vendor.mediatek.hardware.mtkradioex.ims;

import android.hardware.radio.messaging.CdmaSmsAck;
import android.hardware.radio.messaging.ImsSmsMessage;
import android.hardware.radio.voice.Dial;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication;
import vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse;
import vendor.mediatek.hardware.mtkradioex.voice.ConferenceDial;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExIms extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$ims$IMtkRadioExIms".replace('$', '.');
    public static final String HASH = "b6191c55369c4d5d70b8158a8e4c34dc461825de";
    public static final int VERSION = 3;

    void acknowledgeLastIncomingCdmaSmsEx(int i, CdmaSmsAck cdmaSmsAck, int i2) throws RemoteException;

    void acknowledgeLastIncomingGsmSmsEx(int i, boolean z, int i2, int i3) throws RemoteException;

    void cancelUssi(int i, int i2) throws RemoteException;

    void conferenceDial(int i, ConferenceDial conferenceDial, int i2) throws RemoteException;

    void controlCall(int i, int i2, int i3, int i4) throws RemoteException;

    void controlImsConferenceCallMember(int i, int i2, int i3, String str, int i4, int i5) throws RemoteException;

    void dialWithSipUri(int i, String str, int i2) throws RemoteException;

    void eccRedialApprove(int i, int i2, int i3, int i4) throws RemoteException;

    void forceReleaseCall(int i, int i2, int i3) throws RemoteException;

    void getBarringCalls(int i, int i2, int i3) throws RemoteException;

    void getImsCfgFeatureValue(int i, int i2, int i3, int i4) throws RemoteException;

    void getImsCfgProvisionValue(int i, int i2, int i3) throws RemoteException;

    void getImsCfgResourceCapValue(int i, int i2, int i3) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getVoiceDomainPreference(int i, int i2) throws RemoteException;

    void getWfcConfig(int i, int i2, int i3) throws RemoteException;

    void getXcapStatus(int i, int i2) throws RemoteException;

    void imsBearerStateConfirm(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void imsEctCommand(int i, String str, int i2, int i3) throws RemoteException;

    void notifyEPDGScreenState(int i, int i2, int i3) throws RemoteException;

    void pullCall(int i, String str, boolean z, int i2) throws RemoteException;

    void querySsacStatus(int i, int i2) throws RemoteException;

    void queryVopsStatus(int i, int i2) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void rttModifyRequestResponse(int i, int i2, int i3, int i4) throws RemoteException;

    void sendImsSmsEx(int i, ImsSmsMessage imsSmsMessage, int i2) throws RemoteException;

    void sendRttModifyRequest(int i, int i2, int i3, int i4) throws RemoteException;

    void sendRttText(int i, int i2, int i3, String str, int i4) throws RemoteException;

    void sendUssi(int i, String str, int i2) throws RemoteException;

    void setBarringCalls(int i, ImsBarringCall[] imsBarringCallArr, int i2) throws RemoteException;

    void setCallAdditionalInfo(int i, String[] strArr, int i2) throws RemoteException;

    void setEmergencyAddressId(int i, String str, int i2) throws RemoteException;

    void setImsBearerNotification(int i, int i2, int i3) throws RemoteException;

    void setImsCallMode(int i, int i2, int i3) throws RemoteException;

    void setImsCfgFeatureValue(int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException;

    void setImsCfgProvisionValue(int i, int i2, String str, int i3) throws RemoteException;

    void setImsRegistrationReport(int i, int i2) throws RemoteException;

    void setImscfg(int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, int i2) throws RemoteException;

    void setLocationInfo(int i, String[] strArr, int i2) throws RemoteException;

    void setModemImsCfg(int i, String str, String str2, int i2, int i3) throws RemoteException;

    void setNattKeepAliveStatus(int i, String str, boolean z, String str2, int i2, String str3, int i3, int i4) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExImsResponse iMtkRadioExImsResponse, IMtkRadioExImsIndication iMtkRadioExImsIndication) throws RemoteException;

    void setResponseFunctionsMwi(IMwiRadioResponse iMwiRadioResponse, IMwiRadioIndication iMwiRadioIndication) throws RemoteException;

    void setRttMode(int i, int i2, int i3) throws RemoteException;

    void setSipHeader(int i, String[] strArr, int i2) throws RemoteException;

    void setSipHeaderReport(int i, String[] strArr, int i2) throws RemoteException;

    void setVoiceDomainPreference(int i, int i2, int i3) throws RemoteException;

    void setWfcConfig(int i, int i2, String str, String str2, int i3) throws RemoteException;

    void setWfcProfile(int i, int i2, int i3) throws RemoteException;

    void setWifiAssociated(int i, String[] strArr, int i2) throws RemoteException;

    void setWifiEnabled(int i, String str, int i2, int i3, int i4) throws RemoteException;

    void setWifiIpAddress(int i, String[] strArr, int i2) throws RemoteException;

    void setWifiPingResult(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void setWifiSignalLevel(int i, int i2, int i3, int i4) throws RemoteException;

    void setupXcapUserAgentString(int i, String str, int i2) throws RemoteException;

    void toggleRttAudioIndication(int i, int i2, int i3, int i4) throws RemoteException;

    void videoCallAccept(int i, int i2, int i3, int i4) throws RemoteException;

    void videoRingtoneEventRequest(int i, String[] strArr, int i2) throws RemoteException;

    void vtDial(int i, Dial dial, int i2) throws RemoteException;

    void vtDialWithSipUri(int i, String str, int i2) throws RemoteException;

    void vtEmergencyDial(int i, Dial dial, int i2, String[] strArr, int i3, boolean z, boolean z2, int i4, int i5) throws RemoteException;

    public static class Default implements IMtkRadioExIms {
        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void imsBearerStateConfirm(int serial, int aid, int action, int status, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImsBearerNotification(int serial, int enable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void acknowledgeLastIncomingCdmaSmsEx(int serial, CdmaSmsAck smsAck, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void acknowledgeLastIncomingGsmSmsEx(int serial, boolean success, int cause, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void sendImsSmsEx(int serial, ImsSmsMessage message, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void rttModifyRequestResponse(int serial, int callId, int result, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void sendRttModifyRequest(int serial, int callId, int newMode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void sendRttText(int serial, int callId, int lenOfString, String text, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setRttMode(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void toggleRttAudioIndication(int serial, int callId, int audio, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void queryVopsStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImsRegistrationReport(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void cancelUssi(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getXcapStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void sendUssi(int serial, String ussiString, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setupXcapUserAgentString(int serial, String userAgent, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getBarringCalls(int serial, int serviceClass, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setBarringCalls(int serial, ImsBarringCall[] calls, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void videoCallAccept(int serial, int videoMode, int callId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void videoRingtoneEventRequest(int serial, String[] event, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void vtDial(int serial, Dial dialInfo, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void vtDialWithSipUri(int serial, String address, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void conferenceDial(int serial, ConferenceDial dailInfo, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void controlCall(int serial, int controlType, int callId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void controlImsConferenceCallMember(int serial, int controlType, int confCallId, String address, int callId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void dialWithSipUri(int serial, String address, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void eccRedialApprove(int serial, int approve, int callId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void forceReleaseCall(int serial, int callId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void imsEctCommand(int serial, String number, int type, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void pullCall(int serial, String target, boolean isVideoCall, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setCallAdditionalInfo(int serial, String[] info, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImsCallMode(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setSipHeader(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setSipHeaderReport(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getImsCfgFeatureValue(int serial, int featureId, int network, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getImsCfgProvisionValue(int serial, int configId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getImsCfgResourceCapValue(int serial, int featureId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getVoiceDomainPreference(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImsCfgFeatureValue(int serial, int featureId, int network, int value, int isLast, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImsCfgProvisionValue(int serial, int configId, String value, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImscfg(int serial, boolean volteEnable, boolean vilteEnable, boolean vowifiEnable, boolean viwifiEnable, boolean smsEnable, boolean eimsEnable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setVoiceDomainPreference(int serial, int vdp, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWfcProfile(int serial, int wfcPreference, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setModemImsCfg(int serial, String keys, String values, int type, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void querySsacStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getWfcConfig(int serial, int setting, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void notifyEPDGScreenState(int serial, int state, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setEmergencyAddressId(int serial, String aid, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setLocationInfo(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setNattKeepAliveStatus(int serial, String ifName, boolean enable, String srcIp, int srcPort, String dstIp, int dstPort, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWfcConfig(int serial, int setting, String ifName, String value, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWifiAssociated(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWifiEnabled(int serial, String ifName, int isWifiEnabled, int isFlightModeOn, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWifiIpAddress(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWifiPingResult(int serial, int rat, int latency, int pktloss, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWifiSignalLevel(int serial, int rssi, int snr, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setResponseFunctionsMtk(IMtkRadioExImsResponse radioResponse, IMtkRadioExImsIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setResponseFunctionsMwi(IMwiRadioResponse radioResponse, IMwiRadioIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void vtEmergencyDial(int serial, Dial dialInfo, int categories, String[] urns, int routing, boolean hasKnownUserIntentEmergency, boolean isTesting, int videoDirection, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExIms {
        static final int TRANSACTION_acknowledgeLastIncomingCdmaSmsEx = 3;
        static final int TRANSACTION_acknowledgeLastIncomingGsmSmsEx = 4;
        static final int TRANSACTION_cancelUssi = 13;
        static final int TRANSACTION_conferenceDial = 23;
        static final int TRANSACTION_controlCall = 24;
        static final int TRANSACTION_controlImsConferenceCallMember = 25;
        static final int TRANSACTION_dialWithSipUri = 26;
        static final int TRANSACTION_eccRedialApprove = 27;
        static final int TRANSACTION_forceReleaseCall = 28;
        static final int TRANSACTION_getBarringCalls = 17;
        static final int TRANSACTION_getImsCfgFeatureValue = 35;
        static final int TRANSACTION_getImsCfgProvisionValue = 36;
        static final int TRANSACTION_getImsCfgResourceCapValue = 37;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getVoiceDomainPreference = 38;
        static final int TRANSACTION_getWfcConfig = 46;
        static final int TRANSACTION_getXcapStatus = 14;
        static final int TRANSACTION_imsBearerStateConfirm = 1;
        static final int TRANSACTION_imsEctCommand = 29;
        static final int TRANSACTION_notifyEPDGScreenState = 47;
        static final int TRANSACTION_pullCall = 30;
        static final int TRANSACTION_querySsacStatus = 45;
        static final int TRANSACTION_queryVopsStatus = 11;
        static final int TRANSACTION_responseAcknowledgementMtk = 57;
        static final int TRANSACTION_rttModifyRequestResponse = 6;
        static final int TRANSACTION_sendImsSmsEx = 5;
        static final int TRANSACTION_sendRttModifyRequest = 7;
        static final int TRANSACTION_sendRttText = 8;
        static final int TRANSACTION_sendUssi = 15;
        static final int TRANSACTION_setBarringCalls = 18;
        static final int TRANSACTION_setCallAdditionalInfo = 31;
        static final int TRANSACTION_setEmergencyAddressId = 48;
        static final int TRANSACTION_setImsBearerNotification = 2;
        static final int TRANSACTION_setImsCallMode = 32;
        static final int TRANSACTION_setImsCfgFeatureValue = 39;
        static final int TRANSACTION_setImsCfgProvisionValue = 40;
        static final int TRANSACTION_setImsRegistrationReport = 12;
        static final int TRANSACTION_setImscfg = 41;
        static final int TRANSACTION_setLocationInfo = 49;
        static final int TRANSACTION_setModemImsCfg = 44;
        static final int TRANSACTION_setNattKeepAliveStatus = 50;
        static final int TRANSACTION_setResponseFunctionsMtk = 58;
        static final int TRANSACTION_setResponseFunctionsMwi = 59;
        static final int TRANSACTION_setRttMode = 9;
        static final int TRANSACTION_setSipHeader = 33;
        static final int TRANSACTION_setSipHeaderReport = 34;
        static final int TRANSACTION_setVoiceDomainPreference = 42;
        static final int TRANSACTION_setWfcConfig = 51;
        static final int TRANSACTION_setWfcProfile = 43;
        static final int TRANSACTION_setWifiAssociated = 52;
        static final int TRANSACTION_setWifiEnabled = 53;
        static final int TRANSACTION_setWifiIpAddress = 54;
        static final int TRANSACTION_setWifiPingResult = 55;
        static final int TRANSACTION_setWifiSignalLevel = 56;
        static final int TRANSACTION_setupXcapUserAgentString = 16;
        static final int TRANSACTION_toggleRttAudioIndication = 10;
        static final int TRANSACTION_videoCallAccept = 19;
        static final int TRANSACTION_videoRingtoneEventRequest = 20;
        static final int TRANSACTION_vtDial = 21;
        static final int TRANSACTION_vtDialWithSipUri = 22;
        static final int TRANSACTION_vtEmergencyDial = 60;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExIms asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExIms)) {
                return (IMtkRadioExIms) iin;
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
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    int _arg4 = data.readInt();
                    data.enforceNoDataAvail();
                    imsBearerStateConfirm(_arg0, _arg1, _arg2, _arg3, _arg4);
                    return true;
                case 2:
                    int _arg5 = data.readInt();
                    int _arg6 = data.readInt();
                    int _arg7 = data.readInt();
                    data.enforceNoDataAvail();
                    setImsBearerNotification(_arg5, _arg6, _arg7);
                    return true;
                case 3:
                    int _arg8 = data.readInt();
                    CdmaSmsAck _arg9 = (CdmaSmsAck) data.readTypedObject(CdmaSmsAck.CREATOR);
                    int _arg10 = data.readInt();
                    data.enforceNoDataAvail();
                    acknowledgeLastIncomingCdmaSmsEx(_arg8, _arg9, _arg10);
                    return true;
                case 4:
                    int _arg11 = data.readInt();
                    boolean _arg12 = data.readBoolean();
                    int _arg13 = data.readInt();
                    int _arg14 = data.readInt();
                    data.enforceNoDataAvail();
                    acknowledgeLastIncomingGsmSmsEx(_arg11, _arg12, _arg13, _arg14);
                    return true;
                case 5:
                    int _arg15 = data.readInt();
                    ImsSmsMessage _arg16 = (ImsSmsMessage) data.readTypedObject(ImsSmsMessage.CREATOR);
                    int _arg17 = data.readInt();
                    data.enforceNoDataAvail();
                    sendImsSmsEx(_arg15, _arg16, _arg17);
                    return true;
                case 6:
                    int _arg18 = data.readInt();
                    int _arg19 = data.readInt();
                    int _arg20 = data.readInt();
                    int _arg21 = data.readInt();
                    data.enforceNoDataAvail();
                    rttModifyRequestResponse(_arg18, _arg19, _arg20, _arg21);
                    return true;
                case 7:
                    int _arg22 = data.readInt();
                    int _arg23 = data.readInt();
                    int _arg24 = data.readInt();
                    int _arg25 = data.readInt();
                    data.enforceNoDataAvail();
                    sendRttModifyRequest(_arg22, _arg23, _arg24, _arg25);
                    return true;
                case 8:
                    int _arg26 = data.readInt();
                    int _arg27 = data.readInt();
                    int _arg28 = data.readInt();
                    String _arg29 = data.readString();
                    int _arg30 = data.readInt();
                    data.enforceNoDataAvail();
                    sendRttText(_arg26, _arg27, _arg28, _arg29, _arg30);
                    return true;
                case 9:
                    int _arg31 = data.readInt();
                    int _arg32 = data.readInt();
                    int _arg33 = data.readInt();
                    data.enforceNoDataAvail();
                    setRttMode(_arg31, _arg32, _arg33);
                    return true;
                case 10:
                    int _arg34 = data.readInt();
                    int _arg35 = data.readInt();
                    int _arg36 = data.readInt();
                    int _arg37 = data.readInt();
                    data.enforceNoDataAvail();
                    toggleRttAudioIndication(_arg34, _arg35, _arg36, _arg37);
                    return true;
                case 11:
                    int _arg38 = data.readInt();
                    int _arg39 = data.readInt();
                    data.enforceNoDataAvail();
                    queryVopsStatus(_arg38, _arg39);
                    return true;
                case 12:
                    int _arg40 = data.readInt();
                    int _arg41 = data.readInt();
                    data.enforceNoDataAvail();
                    setImsRegistrationReport(_arg40, _arg41);
                    return true;
                case 13:
                    int _arg42 = data.readInt();
                    int _arg43 = data.readInt();
                    data.enforceNoDataAvail();
                    cancelUssi(_arg42, _arg43);
                    return true;
                case 14:
                    int _arg44 = data.readInt();
                    int _arg45 = data.readInt();
                    data.enforceNoDataAvail();
                    getXcapStatus(_arg44, _arg45);
                    return true;
                case 15:
                    int _arg46 = data.readInt();
                    String _arg47 = data.readString();
                    int _arg48 = data.readInt();
                    data.enforceNoDataAvail();
                    sendUssi(_arg46, _arg47, _arg48);
                    return true;
                case 16:
                    int _arg49 = data.readInt();
                    String _arg50 = data.readString();
                    int _arg51 = data.readInt();
                    data.enforceNoDataAvail();
                    setupXcapUserAgentString(_arg49, _arg50, _arg51);
                    return true;
                case 17:
                    int _arg52 = data.readInt();
                    int _arg53 = data.readInt();
                    int _arg54 = data.readInt();
                    data.enforceNoDataAvail();
                    getBarringCalls(_arg52, _arg53, _arg54);
                    return true;
                case 18:
                    int _arg55 = data.readInt();
                    ImsBarringCall[] _arg56 = (ImsBarringCall[]) data.createTypedArray(ImsBarringCall.CREATOR);
                    int _arg57 = data.readInt();
                    data.enforceNoDataAvail();
                    setBarringCalls(_arg55, _arg56, _arg57);
                    return true;
                case TRANSACTION_videoCallAccept /* 19 */:
                    int _arg58 = data.readInt();
                    int _arg59 = data.readInt();
                    int _arg60 = data.readInt();
                    int _arg61 = data.readInt();
                    data.enforceNoDataAvail();
                    videoCallAccept(_arg58, _arg59, _arg60, _arg61);
                    return true;
                case 20:
                    int _arg62 = data.readInt();
                    String[] _arg63 = data.createStringArray();
                    int _arg64 = data.readInt();
                    data.enforceNoDataAvail();
                    videoRingtoneEventRequest(_arg62, _arg63, _arg64);
                    return true;
                case TRANSACTION_vtDial /* 21 */:
                    int _arg65 = data.readInt();
                    Dial _arg66 = (Dial) data.readTypedObject(Dial.CREATOR);
                    int _arg67 = data.readInt();
                    data.enforceNoDataAvail();
                    vtDial(_arg65, _arg66, _arg67);
                    return true;
                case TRANSACTION_vtDialWithSipUri /* 22 */:
                    int _arg68 = data.readInt();
                    String _arg69 = data.readString();
                    int _arg70 = data.readInt();
                    data.enforceNoDataAvail();
                    vtDialWithSipUri(_arg68, _arg69, _arg70);
                    return true;
                case TRANSACTION_conferenceDial /* 23 */:
                    int _arg71 = data.readInt();
                    ConferenceDial _arg72 = (ConferenceDial) data.readTypedObject(ConferenceDial.CREATOR);
                    int _arg73 = data.readInt();
                    data.enforceNoDataAvail();
                    conferenceDial(_arg71, _arg72, _arg73);
                    return true;
                case 24:
                    int _arg74 = data.readInt();
                    int _arg75 = data.readInt();
                    int _arg76 = data.readInt();
                    int _arg77 = data.readInt();
                    data.enforceNoDataAvail();
                    controlCall(_arg74, _arg75, _arg76, _arg77);
                    return true;
                case TRANSACTION_controlImsConferenceCallMember /* 25 */:
                    int _arg78 = data.readInt();
                    int _arg79 = data.readInt();
                    int _arg80 = data.readInt();
                    String _arg81 = data.readString();
                    int _arg82 = data.readInt();
                    int _arg83 = data.readInt();
                    data.enforceNoDataAvail();
                    controlImsConferenceCallMember(_arg78, _arg79, _arg80, _arg81, _arg82, _arg83);
                    return true;
                case TRANSACTION_dialWithSipUri /* 26 */:
                    int _arg84 = data.readInt();
                    String _arg85 = data.readString();
                    int _arg86 = data.readInt();
                    data.enforceNoDataAvail();
                    dialWithSipUri(_arg84, _arg85, _arg86);
                    return true;
                case TRANSACTION_eccRedialApprove /* 27 */:
                    int _arg87 = data.readInt();
                    int _arg88 = data.readInt();
                    int _arg89 = data.readInt();
                    int _arg90 = data.readInt();
                    data.enforceNoDataAvail();
                    eccRedialApprove(_arg87, _arg88, _arg89, _arg90);
                    return true;
                case TRANSACTION_forceReleaseCall /* 28 */:
                    int _arg91 = data.readInt();
                    int _arg92 = data.readInt();
                    int _arg93 = data.readInt();
                    data.enforceNoDataAvail();
                    forceReleaseCall(_arg91, _arg92, _arg93);
                    return true;
                case TRANSACTION_imsEctCommand /* 29 */:
                    int _arg94 = data.readInt();
                    String _arg95 = data.readString();
                    int _arg96 = data.readInt();
                    int _arg97 = data.readInt();
                    data.enforceNoDataAvail();
                    imsEctCommand(_arg94, _arg95, _arg96, _arg97);
                    return true;
                case TRANSACTION_pullCall /* 30 */:
                    int _arg98 = data.readInt();
                    String _arg99 = data.readString();
                    boolean _arg100 = data.readBoolean();
                    int _arg101 = data.readInt();
                    data.enforceNoDataAvail();
                    pullCall(_arg98, _arg99, _arg100, _arg101);
                    return true;
                case TRANSACTION_setCallAdditionalInfo /* 31 */:
                    int _arg102 = data.readInt();
                    String[] _arg103 = data.createStringArray();
                    int _arg104 = data.readInt();
                    data.enforceNoDataAvail();
                    setCallAdditionalInfo(_arg102, _arg103, _arg104);
                    return true;
                case 32:
                    int _arg105 = data.readInt();
                    int _arg106 = data.readInt();
                    int _arg107 = data.readInt();
                    data.enforceNoDataAvail();
                    setImsCallMode(_arg105, _arg106, _arg107);
                    return true;
                case 33:
                    int _arg108 = data.readInt();
                    String[] _arg109 = data.createStringArray();
                    int _arg110 = data.readInt();
                    data.enforceNoDataAvail();
                    setSipHeader(_arg108, _arg109, _arg110);
                    return true;
                case TRANSACTION_setSipHeaderReport /* 34 */:
                    int _arg111 = data.readInt();
                    String[] _arg112 = data.createStringArray();
                    int _arg113 = data.readInt();
                    data.enforceNoDataAvail();
                    setSipHeaderReport(_arg111, _arg112, _arg113);
                    return true;
                case TRANSACTION_getImsCfgFeatureValue /* 35 */:
                    int _arg114 = data.readInt();
                    int _arg115 = data.readInt();
                    int _arg116 = data.readInt();
                    int _arg117 = data.readInt();
                    data.enforceNoDataAvail();
                    getImsCfgFeatureValue(_arg114, _arg115, _arg116, _arg117);
                    return true;
                case 36:
                    int _arg118 = data.readInt();
                    int _arg119 = data.readInt();
                    int _arg120 = data.readInt();
                    data.enforceNoDataAvail();
                    getImsCfgProvisionValue(_arg118, _arg119, _arg120);
                    return true;
                case TRANSACTION_getImsCfgResourceCapValue /* 37 */:
                    int _arg121 = data.readInt();
                    int _arg122 = data.readInt();
                    int _arg123 = data.readInt();
                    data.enforceNoDataAvail();
                    getImsCfgResourceCapValue(_arg121, _arg122, _arg123);
                    return true;
                case 38:
                    int _arg124 = data.readInt();
                    int _arg125 = data.readInt();
                    data.enforceNoDataAvail();
                    getVoiceDomainPreference(_arg124, _arg125);
                    return true;
                case TRANSACTION_setImsCfgFeatureValue /* 39 */:
                    int _arg126 = data.readInt();
                    int _arg127 = data.readInt();
                    int _arg128 = data.readInt();
                    int _arg129 = data.readInt();
                    int _arg130 = data.readInt();
                    int _arg131 = data.readInt();
                    data.enforceNoDataAvail();
                    setImsCfgFeatureValue(_arg126, _arg127, _arg128, _arg129, _arg130, _arg131);
                    return true;
                case 40:
                    int _arg132 = data.readInt();
                    int _arg133 = data.readInt();
                    String _arg134 = data.readString();
                    int _arg135 = data.readInt();
                    data.enforceNoDataAvail();
                    setImsCfgProvisionValue(_arg132, _arg133, _arg134, _arg135);
                    return true;
                case TRANSACTION_setImscfg /* 41 */:
                    int _arg136 = data.readInt();
                    boolean _arg137 = data.readBoolean();
                    boolean _arg138 = data.readBoolean();
                    boolean _arg139 = data.readBoolean();
                    boolean _arg140 = data.readBoolean();
                    boolean _arg141 = data.readBoolean();
                    boolean _arg142 = data.readBoolean();
                    int _arg143 = data.readInt();
                    data.enforceNoDataAvail();
                    setImscfg(_arg136, _arg137, _arg138, _arg139, _arg140, _arg141, _arg142, _arg143);
                    return true;
                case TRANSACTION_setVoiceDomainPreference /* 42 */:
                    int _arg144 = data.readInt();
                    int _arg145 = data.readInt();
                    int _arg146 = data.readInt();
                    data.enforceNoDataAvail();
                    setVoiceDomainPreference(_arg144, _arg145, _arg146);
                    return true;
                case TRANSACTION_setWfcProfile /* 43 */:
                    int _arg147 = data.readInt();
                    int _arg148 = data.readInt();
                    int _arg149 = data.readInt();
                    data.enforceNoDataAvail();
                    setWfcProfile(_arg147, _arg148, _arg149);
                    return true;
                case TRANSACTION_setModemImsCfg /* 44 */:
                    int _arg150 = data.readInt();
                    String _arg151 = data.readString();
                    String _arg152 = data.readString();
                    int _arg153 = data.readInt();
                    int _arg154 = data.readInt();
                    data.enforceNoDataAvail();
                    setModemImsCfg(_arg150, _arg151, _arg152, _arg153, _arg154);
                    return true;
                case TRANSACTION_querySsacStatus /* 45 */:
                    int _arg155 = data.readInt();
                    int _arg156 = data.readInt();
                    data.enforceNoDataAvail();
                    querySsacStatus(_arg155, _arg156);
                    return true;
                case 46:
                    int _arg157 = data.readInt();
                    int _arg158 = data.readInt();
                    int _arg159 = data.readInt();
                    data.enforceNoDataAvail();
                    getWfcConfig(_arg157, _arg158, _arg159);
                    return true;
                case 47:
                    int _arg160 = data.readInt();
                    int _arg161 = data.readInt();
                    int _arg162 = data.readInt();
                    data.enforceNoDataAvail();
                    notifyEPDGScreenState(_arg160, _arg161, _arg162);
                    return true;
                case 48:
                    int _arg163 = data.readInt();
                    String _arg164 = data.readString();
                    int _arg165 = data.readInt();
                    data.enforceNoDataAvail();
                    setEmergencyAddressId(_arg163, _arg164, _arg165);
                    return true;
                case TRANSACTION_setLocationInfo /* 49 */:
                    int _arg166 = data.readInt();
                    String[] _arg167 = data.createStringArray();
                    int _arg168 = data.readInt();
                    data.enforceNoDataAvail();
                    setLocationInfo(_arg166, _arg167, _arg168);
                    return true;
                case 50:
                    int _arg169 = data.readInt();
                    String _arg170 = data.readString();
                    boolean _arg171 = data.readBoolean();
                    String _arg172 = data.readString();
                    int _arg173 = data.readInt();
                    String _arg174 = data.readString();
                    int _arg175 = data.readInt();
                    int _arg176 = data.readInt();
                    data.enforceNoDataAvail();
                    setNattKeepAliveStatus(_arg169, _arg170, _arg171, _arg172, _arg173, _arg174, _arg175, _arg176);
                    return true;
                case 51:
                    int _arg177 = data.readInt();
                    int _arg178 = data.readInt();
                    String _arg179 = data.readString();
                    String _arg180 = data.readString();
                    int _arg181 = data.readInt();
                    data.enforceNoDataAvail();
                    setWfcConfig(_arg177, _arg178, _arg179, _arg180, _arg181);
                    return true;
                case 52:
                    int _arg182 = data.readInt();
                    String[] _arg183 = data.createStringArray();
                    int _arg184 = data.readInt();
                    data.enforceNoDataAvail();
                    setWifiAssociated(_arg182, _arg183, _arg184);
                    return true;
                case TRANSACTION_setWifiEnabled /* 53 */:
                    int _arg185 = data.readInt();
                    String _arg186 = data.readString();
                    int _arg187 = data.readInt();
                    int _arg188 = data.readInt();
                    int _arg189 = data.readInt();
                    data.enforceNoDataAvail();
                    setWifiEnabled(_arg185, _arg186, _arg187, _arg188, _arg189);
                    return true;
                case 54:
                    int _arg190 = data.readInt();
                    String[] _arg191 = data.createStringArray();
                    int _arg192 = data.readInt();
                    data.enforceNoDataAvail();
                    setWifiIpAddress(_arg190, _arg191, _arg192);
                    return true;
                case TRANSACTION_setWifiPingResult /* 55 */:
                    int _arg193 = data.readInt();
                    int _arg194 = data.readInt();
                    int _arg195 = data.readInt();
                    int _arg196 = data.readInt();
                    int _arg197 = data.readInt();
                    data.enforceNoDataAvail();
                    setWifiPingResult(_arg193, _arg194, _arg195, _arg196, _arg197);
                    return true;
                case 56:
                    int _arg198 = data.readInt();
                    int _arg199 = data.readInt();
                    int _arg200 = data.readInt();
                    int _arg201 = data.readInt();
                    data.enforceNoDataAvail();
                    setWifiSignalLevel(_arg198, _arg199, _arg200, _arg201);
                    return true;
                case TRANSACTION_responseAcknowledgementMtk /* 57 */:
                    responseAcknowledgementMtk();
                    return true;
                case TRANSACTION_setResponseFunctionsMtk /* 58 */:
                    IMtkRadioExImsResponse _arg202 = IMtkRadioExImsResponse.Stub.asInterface(data.readStrongBinder());
                    IMtkRadioExImsIndication _arg203 = IMtkRadioExImsIndication.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setResponseFunctionsMtk(_arg202, _arg203);
                    return true;
                case TRANSACTION_setResponseFunctionsMwi /* 59 */:
                    IMwiRadioResponse _arg204 = IMwiRadioResponse.Stub.asInterface(data.readStrongBinder());
                    IMwiRadioIndication _arg205 = IMwiRadioIndication.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setResponseFunctionsMwi(_arg204, _arg205);
                    return true;
                case 60:
                    int _arg206 = data.readInt();
                    Dial _arg207 = (Dial) data.readTypedObject(Dial.CREATOR);
                    int _arg208 = data.readInt();
                    String[] _arg209 = data.createStringArray();
                    int _arg210 = data.readInt();
                    boolean _arg211 = data.readBoolean();
                    boolean _arg212 = data.readBoolean();
                    int _arg213 = data.readInt();
                    int _arg214 = data.readInt();
                    data.enforceNoDataAvail();
                    vtEmergencyDial(_arg206, _arg207, _arg208, _arg209, _arg210, _arg211, _arg212, _arg213, _arg214);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExIms {
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void imsBearerStateConfirm(int serial, int aid, int action, int status, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(aid);
                    _data.writeInt(action);
                    _data.writeInt(status);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImsBearerNotification(int serial, int enable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(enable);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void acknowledgeLastIncomingCdmaSmsEx(int serial, CdmaSmsAck smsAck, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(smsAck, 0);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void acknowledgeLastIncomingGsmSmsEx(int serial, boolean success, int cause, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(success);
                    _data.writeInt(cause);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void sendImsSmsEx(int serial, ImsSmsMessage message, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(message, 0);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void rttModifyRequestResponse(int serial, int callId, int result, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(result);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void sendRttModifyRequest(int serial, int callId, int newMode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(newMode);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void sendRttText(int serial, int callId, int lenOfString, String text, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(lenOfString);
                    _data.writeString(text);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setRttMode(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void toggleRttAudioIndication(int serial, int callId, int audio, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(audio);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void queryVopsStatus(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImsRegistrationReport(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void cancelUssi(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getXcapStatus(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void sendUssi(int serial, String ussiString, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ussiString);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setupXcapUserAgentString(int serial, String userAgent, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(userAgent);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getBarringCalls(int serial, int serviceClass, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(serviceClass);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setBarringCalls(int serial, ImsBarringCall[] calls, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedArray(calls, 0);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void videoCallAccept(int serial, int videoMode, int callId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(videoMode);
                    _data.writeInt(callId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_videoCallAccept, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void videoRingtoneEventRequest(int serial, String[] event, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(event);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void vtDial(int serial, Dial dialInfo, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(dialInfo, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_vtDial, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void vtDialWithSipUri(int serial, String address, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(address);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_vtDialWithSipUri, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void conferenceDial(int serial, ConferenceDial dailInfo, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(dailInfo, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_conferenceDial, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void controlCall(int serial, int controlType, int callId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(controlType);
                    _data.writeInt(callId);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void controlImsConferenceCallMember(int serial, int controlType, int confCallId, String address, int callId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(controlType);
                    _data.writeInt(confCallId);
                    _data.writeString(address);
                    _data.writeInt(callId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_controlImsConferenceCallMember, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void dialWithSipUri(int serial, String address, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(address);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_dialWithSipUri, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void eccRedialApprove(int serial, int approve, int callId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(approve);
                    _data.writeInt(callId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_eccRedialApprove, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void forceReleaseCall(int serial, int callId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_forceReleaseCall, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void imsEctCommand(int serial, String number, int type, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(number);
                    _data.writeInt(type);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsEctCommand, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void pullCall(int serial, String target, boolean isVideoCall, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(target);
                    _data.writeBoolean(isVideoCall);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_pullCall, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setCallAdditionalInfo(int serial, String[] info, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(info);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setCallAdditionalInfo, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImsCallMode(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setSipHeader(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setSipHeaderReport(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSipHeaderReport, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getImsCfgFeatureValue(int serial, int featureId, int network, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(featureId);
                    _data.writeInt(network);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getImsCfgFeatureValue, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getImsCfgProvisionValue(int serial, int configId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(configId);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getImsCfgResourceCapValue(int serial, int featureId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(featureId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getImsCfgResourceCapValue, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getVoiceDomainPreference(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImsCfgFeatureValue(int serial, int featureId, int network, int value, int isLast, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(featureId);
                    _data.writeInt(network);
                    _data.writeInt(value);
                    _data.writeInt(isLast);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setImsCfgFeatureValue, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImsCfgProvisionValue(int serial, int configId, String value, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(configId);
                    _data.writeString(value);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImscfg(int serial, boolean volteEnable, boolean vilteEnable, boolean vowifiEnable, boolean viwifiEnable, boolean smsEnable, boolean eimsEnable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(volteEnable);
                    _data.writeBoolean(vilteEnable);
                    _data.writeBoolean(vowifiEnable);
                    _data.writeBoolean(viwifiEnable);
                    _data.writeBoolean(smsEnable);
                    _data.writeBoolean(eimsEnable);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setImscfg, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setVoiceDomainPreference(int serial, int vdp, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(vdp);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setVoiceDomainPreference, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWfcProfile(int serial, int wfcPreference, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(wfcPreference);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWfcProfile, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setModemImsCfg(int serial, String keys, String values, int type, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(keys);
                    _data.writeString(values);
                    _data.writeInt(type);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setModemImsCfg, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void querySsacStatus(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_querySsacStatus, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getWfcConfig(int serial, int setting, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(setting);
                    _data.writeInt(clientId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void notifyEPDGScreenState(int serial, int state, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(state);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(47, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setEmergencyAddressId(int serial, String aid, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(aid);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(48, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setLocationInfo(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setLocationInfo, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setNattKeepAliveStatus(int serial, String ifName, boolean enable, String srcIp, int srcPort, String dstIp, int dstPort, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ifName);
                    _data.writeBoolean(enable);
                    _data.writeString(srcIp);
                    _data.writeInt(srcPort);
                    _data.writeString(dstIp);
                    _data.writeInt(dstPort);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(50, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWfcConfig(int serial, int setting, String ifName, String value, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(setting);
                    _data.writeString(ifName);
                    _data.writeString(value);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(51, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWifiAssociated(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(52, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWifiEnabled(int serial, String ifName, int isWifiEnabled, int isFlightModeOn, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ifName);
                    _data.writeInt(isWifiEnabled);
                    _data.writeInt(isFlightModeOn);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWifiEnabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWifiIpAddress(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(54, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWifiPingResult(int serial, int rat, int latency, int pktloss, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(rat);
                    _data.writeInt(latency);
                    _data.writeInt(pktloss);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWifiPingResult, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWifiSignalLevel(int serial, int rssi, int snr, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(rssi);
                    _data.writeInt(snr);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(56, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void responseAcknowledgementMtk() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_responseAcknowledgementMtk, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setResponseFunctionsMtk(IMtkRadioExImsResponse radioResponse, IMtkRadioExImsIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsMtk, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setResponseFunctionsMwi(IMwiRadioResponse radioResponse, IMwiRadioIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsMwi, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void vtEmergencyDial(int serial, Dial dialInfo, int categories, String[] urns, int routing, boolean hasKnownUserIntentEmergency, boolean isTesting, int videoDirection, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(dialInfo, 0);
                    _data.writeInt(categories);
                    _data.writeStringArray(urns);
                    _data.writeInt(routing);
                    _data.writeBoolean(hasKnownUserIntentEmergency);
                    _data.writeBoolean(isTesting);
                    _data.writeInt(videoDirection);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(60, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
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
