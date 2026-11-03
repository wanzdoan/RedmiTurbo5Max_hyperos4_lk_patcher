package vendor.mediatek.hardware.mtkradioex.ims;

import android.hardware.radio.messaging.CdmaSmsMessage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.voice.Dialog;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExImsIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$ims$IMtkRadioExImsIndication".replace('$', '.');
    public static final String HASH = "b6191c55369c4d5d70b8158a8e4c34dc461825de";
    public static final int VERSION = 3;

    void audioIndication(int i, int i2, int i3) throws RemoteException;

    void callInfoIndication(int i, String[] strArr) throws RemoteException;

    void callRatIndication(int i, int i2, int i3) throws RemoteException;

    void callmodChangeIndicator(int i, String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void cdmaNewSmsEx(int i, CdmaSmsMessage cdmaSmsMessage) throws RemoteException;

    void econfResultIndication(int i, String str, String str2, String str3, String str4, String str5, String str6) throws RemoteException;

    void ectIndication(int i, int i2, int i3, int i4) throws RemoteException;

    void eregrtInfoInd(int i, int[] iArr) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void imsBearerInit(int i) throws RemoteException;

    void imsBearerStateNotify(int i, int i2, int i3, String str) throws RemoteException;

    void imsCfgConfigChanged(int i, int i2, String str, String str2) throws RemoteException;

    void imsCfgConfigLoaded(int i) throws RemoteException;

    void imsCfgDynamicImsSwitchComplete(int i) throws RemoteException;

    void imsCfgFeatureChanged(int i, int i2, int i3, int i4) throws RemoteException;

    void imsConferenceInfoIndication(int i, ImsConfParticipant[] imsConfParticipantArr) throws RemoteException;

    void imsDataInfoNotify(int i, String str, String str2, String str3) throws RemoteException;

    void imsDialogIndication(int i, Dialog[] dialogArr) throws RemoteException;

    void imsEventPackageIndication(int i, String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void imsRedialEmergencyIndication(int i, String str) throws RemoteException;

    void imsRegFlagInd(int i, int i2) throws RemoteException;

    void imsRegInfoInd(int i, int[] iArr) throws RemoteException;

    void imsRegStatusReport(int i, ImsRegStatusInfo imsRegStatusInfo) throws RemoteException;

    void imsRegistrationInfo(int i, int i2, int i3) throws RemoteException;

    void imsRtpInfo(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7) throws RemoteException;

    void imsSupportEcc(int i, int i2) throws RemoteException;

    void incomingCallSipInviteIndication(int i, String str, String str2) throws RemoteException;

    void lteMessageWaitingIndication(int i, String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void newSmsEx(int i, byte[] bArr) throws RemoteException;

    void newSmsStatusReportEx(int i, byte[] bArr) throws RemoteException;

    void noEmergencyCallbackMode(int i) throws RemoteException;

    void onMDInternetUsageInd(int i, int[] iArr) throws RemoteException;

    void onSsacStatus(int i, int[] iArr) throws RemoteException;

    void onUssi(int i, int i2, String str) throws RemoteException;

    void onVolteSubscription(int i, int i2) throws RemoteException;

    void onXui(int i, String str, String str2, String str3) throws RemoteException;

    void rttCapabilityIndication(int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException;

    void rttModifyRequestReceive(int i, int i2, int i3) throws RemoteException;

    void rttModifyResponse(int i, int i2, int i3) throws RemoteException;

    void rttTextReceive(int i, int i2, int i3, String str) throws RemoteException;

    void sendVopsIndication(int i, int i2) throws RemoteException;

    void sipCallProgressIndicator(int i, String str, String str2, String str3, String str4, String str5, String str6) throws RemoteException;

    void sipHeaderReport(int i, String[] strArr) throws RemoteException;

    void sipRegInfoInd(int i, int i2, int i3, String[] strArr) throws RemoteException;

    void speechCodecInfoIndication(int i, int i2) throws RemoteException;

    void videoCapabilityIndicator(int i, String str, String str2, String str3) throws RemoteException;

    void videoRingtoneEventInd(int i, String[] strArr) throws RemoteException;

    void volteSetting(int i, boolean z) throws RemoteException;

    public static class Default implements IMtkRadioExImsIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsBearerStateNotify(int type, int aid, int action, String capability) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsBearerInit(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsDataInfoNotify(int type, String capability, String event, String extra) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void cdmaNewSmsEx(int type, CdmaSmsMessage msg) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void newSmsStatusReportEx(int type, byte[] pdu) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void newSmsEx(int type, byte[] pdu) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void rttModifyResponse(int type, int callId, int result) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void rttTextReceive(int type, int callId, int lenOfString, String text) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void rttCapabilityIndication(int type, int callId, int localCap, int remoteCap, int localStatus, int remoteStatus) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void rttModifyRequestReceive(int type, int callId, int rttType) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void audioIndication(int type, int callId, int audio) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void sendVopsIndication(int type, int vops) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void volteSetting(int type, boolean isEnable) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRegistrationInfo(int type, int registerState, int capability) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsSupportEcc(int type, int supportLteEcc) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void sipRegInfoInd(int type, int account_id, int response_code, String[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRegStatusReport(int type, ImsRegStatusInfo report) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRegInfoInd(int type, int[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void eregrtInfoInd(int type, int[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRegFlagInd(int type, int flag) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRtpInfo(int type, String pdnId, String networkId, String timer, String sendPktLost, String recvPktLost, String jitter, String delay) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void onXui(int type, String accountId, String broadcastFlag, String xuiInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void onUssi(int type, int modeType, String msg) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void onVolteSubscription(int type, int status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void videoCapabilityIndicator(int type, String callId, String localVideoCap, String remoteVideoCap) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void videoRingtoneEventInd(int type, String[] event) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void onMDInternetUsageInd(int type, int[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void ectIndication(int type, int call_id, int ectResult, int cause) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void callInfoIndication(int type, String[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void callmodChangeIndicator(int type, String callId, String callMode, String videoState, String audioDirection, String pau) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void econfResultIndication(int type, String confCallId, String op, String num, String result, String cause, String joinedCallId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void sipCallProgressIndicator(int type, String callId, String dir, String sipMsgType, String method, String responseCode, String reasonText) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsConferenceInfoIndication(int type, ImsConfParticipant[] participants) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void lteMessageWaitingIndication(int type, String callId, String ptype, String urcIdx, String totalUrcCount, String rawData) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsDialogIndication(int type, Dialog[] dialogList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void noEmergencyCallbackMode(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRedialEmergencyIndication(int type, String callId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void speechCodecInfoIndication(int type, int info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsEventPackageIndication(int type, String callId, String ptype, String urcIdx, String totalUrcCount, String rawData) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void sipHeaderReport(int type, String[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void callRatIndication(int type, int domain, int rat) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsCfgDynamicImsSwitchComplete(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsCfgConfigChanged(int type, int phoneId, String configId, String value) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsCfgFeatureChanged(int type, int phoneId, int featureId, int value) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsCfgConfigLoaded(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void onSsacStatus(int type, int[] status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void incomingCallSipInviteIndication(int type, String number, String text) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExImsIndication {
        static final int TRANSACTION_audioIndication = 11;
        static final int TRANSACTION_callInfoIndication = 29;
        static final int TRANSACTION_callRatIndication = 41;
        static final int TRANSACTION_callmodChangeIndicator = 30;
        static final int TRANSACTION_cdmaNewSmsEx = 4;
        static final int TRANSACTION_econfResultIndication = 31;
        static final int TRANSACTION_ectIndication = 28;
        static final int TRANSACTION_eregrtInfoInd = 19;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_imsBearerInit = 2;
        static final int TRANSACTION_imsBearerStateNotify = 1;
        static final int TRANSACTION_imsCfgConfigChanged = 43;
        static final int TRANSACTION_imsCfgConfigLoaded = 45;
        static final int TRANSACTION_imsCfgDynamicImsSwitchComplete = 42;
        static final int TRANSACTION_imsCfgFeatureChanged = 44;
        static final int TRANSACTION_imsConferenceInfoIndication = 33;
        static final int TRANSACTION_imsDataInfoNotify = 3;
        static final int TRANSACTION_imsDialogIndication = 35;
        static final int TRANSACTION_imsEventPackageIndication = 39;
        static final int TRANSACTION_imsRedialEmergencyIndication = 37;
        static final int TRANSACTION_imsRegFlagInd = 20;
        static final int TRANSACTION_imsRegInfoInd = 18;
        static final int TRANSACTION_imsRegStatusReport = 17;
        static final int TRANSACTION_imsRegistrationInfo = 14;
        static final int TRANSACTION_imsRtpInfo = 21;
        static final int TRANSACTION_imsSupportEcc = 15;
        static final int TRANSACTION_incomingCallSipInviteIndication = 47;
        static final int TRANSACTION_lteMessageWaitingIndication = 34;
        static final int TRANSACTION_newSmsEx = 6;
        static final int TRANSACTION_newSmsStatusReportEx = 5;
        static final int TRANSACTION_noEmergencyCallbackMode = 36;
        static final int TRANSACTION_onMDInternetUsageInd = 27;
        static final int TRANSACTION_onSsacStatus = 46;
        static final int TRANSACTION_onUssi = 23;
        static final int TRANSACTION_onVolteSubscription = 24;
        static final int TRANSACTION_onXui = 22;
        static final int TRANSACTION_rttCapabilityIndication = 9;
        static final int TRANSACTION_rttModifyRequestReceive = 10;
        static final int TRANSACTION_rttModifyResponse = 7;
        static final int TRANSACTION_rttTextReceive = 8;
        static final int TRANSACTION_sendVopsIndication = 12;
        static final int TRANSACTION_sipCallProgressIndicator = 32;
        static final int TRANSACTION_sipHeaderReport = 40;
        static final int TRANSACTION_sipRegInfoInd = 16;
        static final int TRANSACTION_speechCodecInfoIndication = 38;
        static final int TRANSACTION_videoCapabilityIndicator = 25;
        static final int TRANSACTION_videoRingtoneEventInd = 26;
        static final int TRANSACTION_volteSetting = 13;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExImsIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExImsIndication)) {
                return (IMtkRadioExImsIndication) iin;
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
                    String _arg3 = data.readString();
                    data.enforceNoDataAvail();
                    imsBearerStateNotify(_arg0, _arg1, _arg2, _arg3);
                    return true;
                case 2:
                    int _arg4 = data.readInt();
                    data.enforceNoDataAvail();
                    imsBearerInit(_arg4);
                    return true;
                case 3:
                    int _arg5 = data.readInt();
                    String _arg6 = data.readString();
                    String _arg7 = data.readString();
                    String _arg8 = data.readString();
                    data.enforceNoDataAvail();
                    imsDataInfoNotify(_arg5, _arg6, _arg7, _arg8);
                    return true;
                case 4:
                    int _arg9 = data.readInt();
                    CdmaSmsMessage _arg10 = (CdmaSmsMessage) data.readTypedObject(CdmaSmsMessage.CREATOR);
                    data.enforceNoDataAvail();
                    cdmaNewSmsEx(_arg9, _arg10);
                    return true;
                case 5:
                    int _arg11 = data.readInt();
                    byte[] _arg12 = data.createByteArray();
                    data.enforceNoDataAvail();
                    newSmsStatusReportEx(_arg11, _arg12);
                    return true;
                case 6:
                    int _arg13 = data.readInt();
                    byte[] _arg14 = data.createByteArray();
                    data.enforceNoDataAvail();
                    newSmsEx(_arg13, _arg14);
                    return true;
                case 7:
                    int _arg15 = data.readInt();
                    int _arg16 = data.readInt();
                    int _arg17 = data.readInt();
                    data.enforceNoDataAvail();
                    rttModifyResponse(_arg15, _arg16, _arg17);
                    return true;
                case 8:
                    int _arg18 = data.readInt();
                    int _arg19 = data.readInt();
                    int _arg20 = data.readInt();
                    String _arg21 = data.readString();
                    data.enforceNoDataAvail();
                    rttTextReceive(_arg18, _arg19, _arg20, _arg21);
                    return true;
                case 9:
                    int _arg22 = data.readInt();
                    int _arg23 = data.readInt();
                    int _arg24 = data.readInt();
                    int _arg25 = data.readInt();
                    int _arg26 = data.readInt();
                    int _arg27 = data.readInt();
                    data.enforceNoDataAvail();
                    rttCapabilityIndication(_arg22, _arg23, _arg24, _arg25, _arg26, _arg27);
                    return true;
                case 10:
                    int _arg28 = data.readInt();
                    int _arg29 = data.readInt();
                    int _arg30 = data.readInt();
                    data.enforceNoDataAvail();
                    rttModifyRequestReceive(_arg28, _arg29, _arg30);
                    return true;
                case 11:
                    int _arg31 = data.readInt();
                    int _arg32 = data.readInt();
                    int _arg33 = data.readInt();
                    data.enforceNoDataAvail();
                    audioIndication(_arg31, _arg32, _arg33);
                    return true;
                case 12:
                    int _arg34 = data.readInt();
                    int _arg35 = data.readInt();
                    data.enforceNoDataAvail();
                    sendVopsIndication(_arg34, _arg35);
                    return true;
                case 13:
                    int _arg36 = data.readInt();
                    boolean _arg37 = data.readBoolean();
                    data.enforceNoDataAvail();
                    volteSetting(_arg36, _arg37);
                    return true;
                case 14:
                    int _arg38 = data.readInt();
                    int _arg39 = data.readInt();
                    int _arg40 = data.readInt();
                    data.enforceNoDataAvail();
                    imsRegistrationInfo(_arg38, _arg39, _arg40);
                    return true;
                case 15:
                    int _arg41 = data.readInt();
                    int _arg42 = data.readInt();
                    data.enforceNoDataAvail();
                    imsSupportEcc(_arg41, _arg42);
                    return true;
                case 16:
                    int _arg43 = data.readInt();
                    int _arg44 = data.readInt();
                    int _arg45 = data.readInt();
                    String[] _arg46 = data.createStringArray();
                    data.enforceNoDataAvail();
                    sipRegInfoInd(_arg43, _arg44, _arg45, _arg46);
                    return true;
                case 17:
                    int _arg47 = data.readInt();
                    ImsRegStatusInfo _arg48 = (ImsRegStatusInfo) data.readTypedObject(ImsRegStatusInfo.CREATOR);
                    data.enforceNoDataAvail();
                    imsRegStatusReport(_arg47, _arg48);
                    return true;
                case 18:
                    int _arg49 = data.readInt();
                    int[] _arg50 = data.createIntArray();
                    data.enforceNoDataAvail();
                    imsRegInfoInd(_arg49, _arg50);
                    return true;
                case TRANSACTION_eregrtInfoInd /* 19 */:
                    int _arg51 = data.readInt();
                    int[] _arg52 = data.createIntArray();
                    data.enforceNoDataAvail();
                    eregrtInfoInd(_arg51, _arg52);
                    return true;
                case 20:
                    int _arg53 = data.readInt();
                    int _arg54 = data.readInt();
                    data.enforceNoDataAvail();
                    imsRegFlagInd(_arg53, _arg54);
                    return true;
                case TRANSACTION_imsRtpInfo /* 21 */:
                    int _arg55 = data.readInt();
                    String _arg56 = data.readString();
                    String _arg57 = data.readString();
                    String _arg58 = data.readString();
                    String _arg59 = data.readString();
                    String _arg60 = data.readString();
                    String _arg61 = data.readString();
                    String _arg62 = data.readString();
                    data.enforceNoDataAvail();
                    imsRtpInfo(_arg55, _arg56, _arg57, _arg58, _arg59, _arg60, _arg61, _arg62);
                    return true;
                case TRANSACTION_onXui /* 22 */:
                    int _arg63 = data.readInt();
                    String _arg64 = data.readString();
                    String _arg65 = data.readString();
                    String _arg66 = data.readString();
                    data.enforceNoDataAvail();
                    onXui(_arg63, _arg64, _arg65, _arg66);
                    return true;
                case TRANSACTION_onUssi /* 23 */:
                    int _arg67 = data.readInt();
                    int _arg68 = data.readInt();
                    String _arg69 = data.readString();
                    data.enforceNoDataAvail();
                    onUssi(_arg67, _arg68, _arg69);
                    return true;
                case 24:
                    int _arg70 = data.readInt();
                    int _arg71 = data.readInt();
                    data.enforceNoDataAvail();
                    onVolteSubscription(_arg70, _arg71);
                    return true;
                case TRANSACTION_videoCapabilityIndicator /* 25 */:
                    int _arg72 = data.readInt();
                    String _arg73 = data.readString();
                    String _arg74 = data.readString();
                    String _arg75 = data.readString();
                    data.enforceNoDataAvail();
                    videoCapabilityIndicator(_arg72, _arg73, _arg74, _arg75);
                    return true;
                case TRANSACTION_videoRingtoneEventInd /* 26 */:
                    int _arg76 = data.readInt();
                    String[] _arg77 = data.createStringArray();
                    data.enforceNoDataAvail();
                    videoRingtoneEventInd(_arg76, _arg77);
                    return true;
                case TRANSACTION_onMDInternetUsageInd /* 27 */:
                    int _arg78 = data.readInt();
                    int[] _arg79 = data.createIntArray();
                    data.enforceNoDataAvail();
                    onMDInternetUsageInd(_arg78, _arg79);
                    return true;
                case TRANSACTION_ectIndication /* 28 */:
                    int _arg80 = data.readInt();
                    int _arg81 = data.readInt();
                    int _arg82 = data.readInt();
                    int _arg83 = data.readInt();
                    data.enforceNoDataAvail();
                    ectIndication(_arg80, _arg81, _arg82, _arg83);
                    return true;
                case TRANSACTION_callInfoIndication /* 29 */:
                    int _arg84 = data.readInt();
                    String[] _arg85 = data.createStringArray();
                    data.enforceNoDataAvail();
                    callInfoIndication(_arg84, _arg85);
                    return true;
                case TRANSACTION_callmodChangeIndicator /* 30 */:
                    int _arg86 = data.readInt();
                    String _arg87 = data.readString();
                    String _arg88 = data.readString();
                    String _arg89 = data.readString();
                    String _arg90 = data.readString();
                    String _arg91 = data.readString();
                    data.enforceNoDataAvail();
                    callmodChangeIndicator(_arg86, _arg87, _arg88, _arg89, _arg90, _arg91);
                    return true;
                case TRANSACTION_econfResultIndication /* 31 */:
                    int _arg92 = data.readInt();
                    String _arg93 = data.readString();
                    String _arg94 = data.readString();
                    String _arg95 = data.readString();
                    String _arg96 = data.readString();
                    String _arg97 = data.readString();
                    String _arg98 = data.readString();
                    data.enforceNoDataAvail();
                    econfResultIndication(_arg92, _arg93, _arg94, _arg95, _arg96, _arg97, _arg98);
                    return true;
                case 32:
                    int _arg99 = data.readInt();
                    String _arg100 = data.readString();
                    String _arg101 = data.readString();
                    String _arg102 = data.readString();
                    String _arg103 = data.readString();
                    String _arg104 = data.readString();
                    String _arg105 = data.readString();
                    data.enforceNoDataAvail();
                    sipCallProgressIndicator(_arg99, _arg100, _arg101, _arg102, _arg103, _arg104, _arg105);
                    return true;
                case 33:
                    int _arg106 = data.readInt();
                    ImsConfParticipant[] _arg107 = (ImsConfParticipant[]) data.createTypedArray(ImsConfParticipant.CREATOR);
                    data.enforceNoDataAvail();
                    imsConferenceInfoIndication(_arg106, _arg107);
                    return true;
                case TRANSACTION_lteMessageWaitingIndication /* 34 */:
                    int _arg108 = data.readInt();
                    String _arg109 = data.readString();
                    String _arg110 = data.readString();
                    String _arg111 = data.readString();
                    String _arg112 = data.readString();
                    String _arg113 = data.readString();
                    data.enforceNoDataAvail();
                    lteMessageWaitingIndication(_arg108, _arg109, _arg110, _arg111, _arg112, _arg113);
                    return true;
                case TRANSACTION_imsDialogIndication /* 35 */:
                    int _arg114 = data.readInt();
                    Dialog[] _arg115 = (Dialog[]) data.createTypedArray(Dialog.CREATOR);
                    data.enforceNoDataAvail();
                    imsDialogIndication(_arg114, _arg115);
                    return true;
                case 36:
                    int _arg116 = data.readInt();
                    data.enforceNoDataAvail();
                    noEmergencyCallbackMode(_arg116);
                    return true;
                case TRANSACTION_imsRedialEmergencyIndication /* 37 */:
                    int _arg117 = data.readInt();
                    String _arg118 = data.readString();
                    data.enforceNoDataAvail();
                    imsRedialEmergencyIndication(_arg117, _arg118);
                    return true;
                case 38:
                    int _arg119 = data.readInt();
                    int _arg120 = data.readInt();
                    data.enforceNoDataAvail();
                    speechCodecInfoIndication(_arg119, _arg120);
                    return true;
                case TRANSACTION_imsEventPackageIndication /* 39 */:
                    int _arg121 = data.readInt();
                    String _arg122 = data.readString();
                    String _arg123 = data.readString();
                    String _arg124 = data.readString();
                    String _arg125 = data.readString();
                    String _arg126 = data.readString();
                    data.enforceNoDataAvail();
                    imsEventPackageIndication(_arg121, _arg122, _arg123, _arg124, _arg125, _arg126);
                    return true;
                case 40:
                    int _arg127 = data.readInt();
                    String[] _arg128 = data.createStringArray();
                    data.enforceNoDataAvail();
                    sipHeaderReport(_arg127, _arg128);
                    return true;
                case TRANSACTION_callRatIndication /* 41 */:
                    int _arg129 = data.readInt();
                    int _arg130 = data.readInt();
                    int _arg131 = data.readInt();
                    data.enforceNoDataAvail();
                    callRatIndication(_arg129, _arg130, _arg131);
                    return true;
                case TRANSACTION_imsCfgDynamicImsSwitchComplete /* 42 */:
                    int _arg132 = data.readInt();
                    data.enforceNoDataAvail();
                    imsCfgDynamicImsSwitchComplete(_arg132);
                    return true;
                case TRANSACTION_imsCfgConfigChanged /* 43 */:
                    int _arg133 = data.readInt();
                    int _arg134 = data.readInt();
                    String _arg135 = data.readString();
                    String _arg136 = data.readString();
                    data.enforceNoDataAvail();
                    imsCfgConfigChanged(_arg133, _arg134, _arg135, _arg136);
                    return true;
                case TRANSACTION_imsCfgFeatureChanged /* 44 */:
                    int _arg137 = data.readInt();
                    int _arg138 = data.readInt();
                    int _arg139 = data.readInt();
                    int _arg140 = data.readInt();
                    data.enforceNoDataAvail();
                    imsCfgFeatureChanged(_arg137, _arg138, _arg139, _arg140);
                    return true;
                case TRANSACTION_imsCfgConfigLoaded /* 45 */:
                    int _arg141 = data.readInt();
                    data.enforceNoDataAvail();
                    imsCfgConfigLoaded(_arg141);
                    return true;
                case 46:
                    int _arg142 = data.readInt();
                    int[] _arg143 = data.createIntArray();
                    data.enforceNoDataAvail();
                    onSsacStatus(_arg142, _arg143);
                    return true;
                case 47:
                    int _arg144 = data.readInt();
                    String _arg145 = data.readString();
                    String _arg146 = data.readString();
                    data.enforceNoDataAvail();
                    incomingCallSipInviteIndication(_arg144, _arg145, _arg146);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExImsIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsBearerStateNotify(int type, int aid, int action, String capability) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(aid);
                    _data.writeInt(action);
                    _data.writeString(capability);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsBearerInit(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsDataInfoNotify(int type, String capability, String event, String extra) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(capability);
                    _data.writeString(event);
                    _data.writeString(extra);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void cdmaNewSmsEx(int type, CdmaSmsMessage msg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(msg, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void newSmsStatusReportEx(int type, byte[] pdu) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeByteArray(pdu);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void newSmsEx(int type, byte[] pdu) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeByteArray(pdu);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void rttModifyResponse(int type, int callId, int result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callId);
                    _data.writeInt(result);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void rttTextReceive(int type, int callId, int lenOfString, String text) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callId);
                    _data.writeInt(lenOfString);
                    _data.writeString(text);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void rttCapabilityIndication(int type, int callId, int localCap, int remoteCap, int localStatus, int remoteStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callId);
                    _data.writeInt(localCap);
                    _data.writeInt(remoteCap);
                    _data.writeInt(localStatus);
                    _data.writeInt(remoteStatus);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void rttModifyRequestReceive(int type, int callId, int rttType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callId);
                    _data.writeInt(rttType);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void audioIndication(int type, int callId, int audio) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callId);
                    _data.writeInt(audio);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void sendVopsIndication(int type, int vops) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void volteSetting(int type, boolean isEnable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeBoolean(isEnable);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRegistrationInfo(int type, int registerState, int capability) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(registerState);
                    _data.writeInt(capability);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsSupportEcc(int type, int supportLteEcc) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(supportLteEcc);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void sipRegInfoInd(int type, int account_id, int response_code, String[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(account_id);
                    _data.writeInt(response_code);
                    _data.writeStringArray(info);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRegStatusReport(int type, ImsRegStatusInfo report) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(report, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRegInfoInd(int type, int[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(info);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void eregrtInfoInd(int type, int[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(info);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_eregrtInfoInd, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRegFlagInd(int type, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(flag);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRtpInfo(int type, String pdnId, String networkId, String timer, String sendPktLost, String recvPktLost, String jitter, String delay) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(pdnId);
                    _data.writeString(networkId);
                    _data.writeString(timer);
                    _data.writeString(sendPktLost);
                    _data.writeString(recvPktLost);
                    _data.writeString(jitter);
                    _data.writeString(delay);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsRtpInfo, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void onXui(int type, String accountId, String broadcastFlag, String xuiInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(accountId);
                    _data.writeString(broadcastFlag);
                    _data.writeString(xuiInfo);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_onXui, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void onUssi(int type, int modeType, String msg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(modeType);
                    _data.writeString(msg);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_onUssi, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void onVolteSubscription(int type, int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(status);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void videoCapabilityIndicator(int type, String callId, String localVideoCap, String remoteVideoCap) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    _data.writeString(localVideoCap);
                    _data.writeString(remoteVideoCap);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_videoCapabilityIndicator, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void videoRingtoneEventInd(int type, String[] event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(event);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_videoRingtoneEventInd, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void onMDInternetUsageInd(int type, int[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(info);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_onMDInternetUsageInd, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void ectIndication(int type, int call_id, int ectResult, int cause) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(call_id);
                    _data.writeInt(ectResult);
                    _data.writeInt(cause);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_ectIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void callInfoIndication(int type, String[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(data);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_callInfoIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void callmodChangeIndicator(int type, String callId, String callMode, String videoState, String audioDirection, String pau) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    _data.writeString(callMode);
                    _data.writeString(videoState);
                    _data.writeString(audioDirection);
                    _data.writeString(pau);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_callmodChangeIndicator, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void econfResultIndication(int type, String confCallId, String op, String num, String result, String cause, String joinedCallId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(confCallId);
                    _data.writeString(op);
                    _data.writeString(num);
                    _data.writeString(result);
                    _data.writeString(cause);
                    _data.writeString(joinedCallId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_econfResultIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void sipCallProgressIndicator(int type, String callId, String dir, String sipMsgType, String method, String responseCode, String reasonText) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    _data.writeString(dir);
                    _data.writeString(sipMsgType);
                    _data.writeString(method);
                    _data.writeString(responseCode);
                    _data.writeString(reasonText);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsConferenceInfoIndication(int type, ImsConfParticipant[] participants) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedArray(participants, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void lteMessageWaitingIndication(int type, String callId, String ptype, String urcIdx, String totalUrcCount, String rawData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    _data.writeString(ptype);
                    _data.writeString(urcIdx);
                    _data.writeString(totalUrcCount);
                    _data.writeString(rawData);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_lteMessageWaitingIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsDialogIndication(int type, Dialog[] dialogList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedArray(dialogList, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsDialogIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void noEmergencyCallbackMode(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRedialEmergencyIndication(int type, String callId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsRedialEmergencyIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void speechCodecInfoIndication(int type, int info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(info);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsEventPackageIndication(int type, String callId, String ptype, String urcIdx, String totalUrcCount, String rawData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    _data.writeString(ptype);
                    _data.writeString(urcIdx);
                    _data.writeString(totalUrcCount);
                    _data.writeString(rawData);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsEventPackageIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void sipHeaderReport(int type, String[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(data);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void callRatIndication(int type, int domain, int rat) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(domain);
                    _data.writeInt(rat);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_callRatIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsCfgDynamicImsSwitchComplete(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsCfgDynamicImsSwitchComplete, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsCfgConfigChanged(int type, int phoneId, String configId, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(phoneId);
                    _data.writeString(configId);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsCfgConfigChanged, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsCfgFeatureChanged(int type, int phoneId, int featureId, int value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(phoneId);
                    _data.writeInt(featureId);
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsCfgFeatureChanged, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsCfgConfigLoaded(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsCfgConfigLoaded, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void onSsacStatus(int type, int[] status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(status);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void incomingCallSipInviteIndication(int type, String number, String text) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(number);
                    _data.writeString(text);
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
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
