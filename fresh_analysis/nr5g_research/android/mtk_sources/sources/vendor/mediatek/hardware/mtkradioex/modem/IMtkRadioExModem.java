package vendor.mediatek.hardware.mtkradioex.modem;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.assist.IAssistModemIndication;
import vendor.mediatek.hardware.mtkradioex.assist.IAssistModemResponse;
import vendor.mediatek.hardware.mtkradioex.atci.IAtciIndication;
import vendor.mediatek.hardware.mtkradioex.atci.IAtciResponse;
import vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse;
import vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication;
import vendor.mediatek.hardware.mtkradioex.em.IEmRadioResponse;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExModem extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$modem$IMtkRadioExModem".replace('$', '.');
    public static final String HASH = "343450221c89bcee3b0e260bb10c69390c3cb226";
    public static final int VERSION = 5;

    void abortCertificate(int i, int i2, int i3) throws RemoteException;

    void enableCapability(int i, String str, int i2, int i3, int i4) throws RemoteException;

    void getEngineeringModeInfo(int i, int i2, int i3) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void modifyModemType(int i, int i2, int i3, int i4) throws RemoteException;

    void registerCellQltyReport(int i, String str, String str2, String str3, String str4, int i2) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void restartRILD(int i, int i2) throws RemoteException;

    void routeAuthMessage(int i, int i2, byte[] bArr, int i3) throws RemoteException;

    void routeCertificate(int i, int i2, byte[] bArr, byte[] bArr2, int i3) throws RemoteException;

    void runGbaAuthentication(int i, String str, String str2, boolean z, int i2, int i3) throws RemoteException;

    void sendAtciRequest(int i, byte[] bArr, int i2) throws RemoteException;

    void sendEmbmsAtCommand(int i, String str, int i2) throws RemoteException;

    void sendRequestRaw(int i, byte[] bArr, int i2) throws RemoteException;

    void sendRequestStrings(int i, String[] strArr, int i2) throws RemoteException;

    void sendSarIndicator(int i, int i2, String str, int i3) throws RemoteException;

    void sendWifiAssociated(int i, String str, int i2, String str2, String str3, int i3, String str4, int i4) throws RemoteException;

    void sendWifiEnabled(int i, String str, int i2, int i3) throws RemoteException;

    void sendWifiIpAddress(int i, String str, String str2, String str3, int i2, int i3, String str4, String str5, int i4, String str6, int i5) throws RemoteException;

    void setGnssMcc(int i, String str, byte b, byte b2, int i2) throws RemoteException;

    void setIndicationFunctionsAssist(IAssistModemIndication iAssistModemIndication) throws RemoteException;

    void setMaxUlSpeed(int i, int i2, int i3) throws RemoteException;

    void setModemPower(int i, boolean z, int i2) throws RemoteException;

    void setResponseFunctionsAssist(IAssistModemResponse iAssistModemResponse) throws RemoteException;

    void setResponseFunctionsCap(IMtkRadioExCapRadioResponse iMtkRadioExCapRadioResponse) throws RemoteException;

    void setResponseFunctionsEm(IEmRadioResponse iEmRadioResponse, IEmRadioIndication iEmRadioIndication) throws RemoteException;

    void setResponseFunctionsForAtci(IAtciResponse iAtciResponse, IAtciIndication iAtciIndication) throws RemoteException;

    void setResponseFunctionsGba(IMtkRadioExModemResponse iMtkRadioExModemResponse) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExModemResponse iMtkRadioExModemResponse, IMtkRadioExModemIndication iMtkRadioExModemIndication) throws RemoteException;

    void setResponseFunctionsMtkIms(IMtkRadioExModemResponse iMtkRadioExModemResponse, IMtkRadioExModemIndication iMtkRadioExModemIndication) throws RemoteException;

    void setTrm(int i, int i2, int i3) throws RemoteException;

    void setTxPower(int i, int i2, int i3) throws RemoteException;

    void setTxPowerStatus(int i, int i2, int i3) throws RemoteException;

    void setVendorSetting(int i, int i2, String str, int i3) throws RemoteException;

    void syncAppEventStatus(int i, int i2, int i3, int i4) throws RemoteException;

    void syncLocationInfo(int i, int i2, byte b, byte b2, byte b3, long j, double d, double d2, double d3, double d4, float f, float f2, float f3, float f4, float f5, float f6, float f7, byte b4, int i3) throws RemoteException;

    void triggerModeSwitchByEcc(int i, int i2, int i3) throws RemoteException;

    public static class Default implements IMtkRadioExModem {
        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void abortCertificate(int serial, int uid, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void enableCapability(int serial, String id, int uid, int toActive, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void getEngineeringModeInfo(int serial, int index, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void modifyModemType(int serial, int applyType, int modemType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void restartRILD(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void routeAuthMessage(int serial, int uid, byte[] msg, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void routeCertificate(int serial, int uid, byte[] cert, byte[] msg, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendAtciRequest(int serial, byte[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendEmbmsAtCommand(int serial, String data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendRequestRaw(int serial, byte[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendRequestStrings(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendSarIndicator(int serial, int sar_cmd_type, String sar_parameter, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setMaxUlSpeed(int serial, int ulSpeed, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setModemPower(int serial, boolean isOn, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setTrm(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setTxPower(int serial, int limitpower, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setTxPowerStatus(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setVendorSetting(int serial, int setting, String value, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void triggerModeSwitchByEcc(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void runGbaAuthentication(int serial, String nafFqdn, String nafSecureProtocolId, boolean forceRun, int netId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendWifiAssociated(int serial, String ifName, int associated, String ssid, String apMac, int mtuSize, String ueMac, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendWifiEnabled(int serial, String ifName, int isWifiEnabled, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendWifiIpAddress(int serial, String ifName, String ipv4Addr, String ipv6Addr, int ipv4PrefixLen, int ipv6PrefixLen, String ipv4Gateway, String ipv6Gateway, int dnsCount, String dnsServers, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void registerCellQltyReport(int serial, String registerQuality, String type, String thresholdValues, String triggerTime, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsMtk(IMtkRadioExModemResponse radioResponse, IMtkRadioExModemIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsMtkIms(IMtkRadioExModemResponse radioResponse, IMtkRadioExModemIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsCap(IMtkRadioExCapRadioResponse capRadioResponse) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsForAtci(IAtciResponse atciResponseParam, IAtciIndication atciIndicationParam) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsEm(IEmRadioResponse radioResponse, IEmRadioIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsGba(IMtkRadioExModemResponse radioResponse) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsAssist(IAssistModemResponse radioResponse) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void syncAppEventStatus(int serial, int event, int status, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setIndicationFunctionsAssist(IAssistModemIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void syncLocationInfo(int serial, int srv_id, byte ntn_enable, byte fixed_loc_info, byte provider, long utc, double latitude, double longitude, double altitude, double msl_altitude, float speed, float bearing, float horizontal_acc, float vertical_acc, float msl_alt_acc, float speed_acc, float bearing_acc, byte unavailable_cause, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setGnssMcc(int serial, String mcc, byte mcc_cfg_idx, byte fbo_en, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExModem {
        static final int TRANSACTION_abortCertificate = 1;
        static final int TRANSACTION_enableCapability = 2;
        static final int TRANSACTION_getEngineeringModeInfo = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_modifyModemType = 4;
        static final int TRANSACTION_registerCellQltyReport = 24;
        static final int TRANSACTION_responseAcknowledgementMtk = 25;
        static final int TRANSACTION_restartRILD = 5;
        static final int TRANSACTION_routeAuthMessage = 6;
        static final int TRANSACTION_routeCertificate = 7;
        static final int TRANSACTION_runGbaAuthentication = 20;
        static final int TRANSACTION_sendAtciRequest = 8;
        static final int TRANSACTION_sendEmbmsAtCommand = 9;
        static final int TRANSACTION_sendRequestRaw = 10;
        static final int TRANSACTION_sendRequestStrings = 11;
        static final int TRANSACTION_sendSarIndicator = 12;
        static final int TRANSACTION_sendWifiAssociated = 21;
        static final int TRANSACTION_sendWifiEnabled = 22;
        static final int TRANSACTION_sendWifiIpAddress = 23;
        static final int TRANSACTION_setGnssMcc = 36;
        static final int TRANSACTION_setIndicationFunctionsAssist = 34;
        static final int TRANSACTION_setMaxUlSpeed = 13;
        static final int TRANSACTION_setModemPower = 14;
        static final int TRANSACTION_setResponseFunctionsAssist = 32;
        static final int TRANSACTION_setResponseFunctionsCap = 28;
        static final int TRANSACTION_setResponseFunctionsEm = 30;
        static final int TRANSACTION_setResponseFunctionsForAtci = 29;
        static final int TRANSACTION_setResponseFunctionsGba = 31;
        static final int TRANSACTION_setResponseFunctionsMtk = 26;
        static final int TRANSACTION_setResponseFunctionsMtkIms = 27;
        static final int TRANSACTION_setTrm = 15;
        static final int TRANSACTION_setTxPower = 16;
        static final int TRANSACTION_setTxPowerStatus = 17;
        static final int TRANSACTION_setVendorSetting = 18;
        static final int TRANSACTION_syncAppEventStatus = 33;
        static final int TRANSACTION_syncLocationInfo = 35;
        static final int TRANSACTION_triggerModeSwitchByEcc = 19;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExModem asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExModem)) {
                return (IMtkRadioExModem) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Parcel parcel;
            if (code < 1 || code > TRANSACTION_getInterfaceVersion) {
                parcel = data;
            } else {
                parcel = data;
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (code != TRANSACTION_getInterfaceVersion) {
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
                        data.enforceNoDataAvail();
                        abortCertificate(_arg0, _arg1, _arg2);
                        return true;
                    case 2:
                        int _arg3 = data.readInt();
                        String _arg4 = data.readString();
                        int _arg5 = data.readInt();
                        int _arg6 = data.readInt();
                        int _arg7 = data.readInt();
                        data.enforceNoDataAvail();
                        enableCapability(_arg3, _arg4, _arg5, _arg6, _arg7);
                        return true;
                    case 3:
                        int _arg8 = data.readInt();
                        int _arg9 = data.readInt();
                        int _arg10 = data.readInt();
                        data.enforceNoDataAvail();
                        getEngineeringModeInfo(_arg8, _arg9, _arg10);
                        return true;
                    case 4:
                        int _arg11 = data.readInt();
                        int _arg12 = data.readInt();
                        int _arg13 = data.readInt();
                        int _arg14 = data.readInt();
                        data.enforceNoDataAvail();
                        modifyModemType(_arg11, _arg12, _arg13, _arg14);
                        return true;
                    case 5:
                        int _arg15 = data.readInt();
                        int _arg16 = data.readInt();
                        data.enforceNoDataAvail();
                        restartRILD(_arg15, _arg16);
                        return true;
                    case 6:
                        int _arg17 = data.readInt();
                        int _arg18 = data.readInt();
                        byte[] _arg19 = data.createByteArray();
                        int _arg20 = data.readInt();
                        data.enforceNoDataAvail();
                        routeAuthMessage(_arg17, _arg18, _arg19, _arg20);
                        return true;
                    case 7:
                        int _arg21 = data.readInt();
                        int _arg22 = data.readInt();
                        byte[] _arg23 = data.createByteArray();
                        byte[] _arg24 = data.createByteArray();
                        int _arg25 = data.readInt();
                        data.enforceNoDataAvail();
                        routeCertificate(_arg21, _arg22, _arg23, _arg24, _arg25);
                        return true;
                    case 8:
                        int _arg26 = data.readInt();
                        byte[] _arg27 = data.createByteArray();
                        int _arg28 = data.readInt();
                        data.enforceNoDataAvail();
                        sendAtciRequest(_arg26, _arg27, _arg28);
                        return true;
                    case 9:
                        int _arg29 = data.readInt();
                        String _arg30 = data.readString();
                        int _arg31 = data.readInt();
                        data.enforceNoDataAvail();
                        sendEmbmsAtCommand(_arg29, _arg30, _arg31);
                        return true;
                    case 10:
                        int _arg32 = data.readInt();
                        byte[] _arg33 = data.createByteArray();
                        int _arg34 = data.readInt();
                        data.enforceNoDataAvail();
                        sendRequestRaw(_arg32, _arg33, _arg34);
                        return true;
                    case 11:
                        int _arg35 = data.readInt();
                        String[] _arg36 = data.createStringArray();
                        int _arg37 = data.readInt();
                        data.enforceNoDataAvail();
                        sendRequestStrings(_arg35, _arg36, _arg37);
                        return true;
                    case 12:
                        int _arg38 = data.readInt();
                        int _arg39 = data.readInt();
                        String _arg40 = data.readString();
                        int _arg41 = data.readInt();
                        data.enforceNoDataAvail();
                        sendSarIndicator(_arg38, _arg39, _arg40, _arg41);
                        return true;
                    case 13:
                        int _arg42 = data.readInt();
                        int _arg43 = data.readInt();
                        int _arg44 = data.readInt();
                        data.enforceNoDataAvail();
                        setMaxUlSpeed(_arg42, _arg43, _arg44);
                        return true;
                    case 14:
                        int _arg45 = data.readInt();
                        boolean _arg46 = data.readBoolean();
                        int _arg47 = data.readInt();
                        data.enforceNoDataAvail();
                        setModemPower(_arg45, _arg46, _arg47);
                        return true;
                    case 15:
                        int _arg48 = data.readInt();
                        int _arg49 = data.readInt();
                        int _arg50 = data.readInt();
                        data.enforceNoDataAvail();
                        setTrm(_arg48, _arg49, _arg50);
                        return true;
                    case 16:
                        int _arg51 = data.readInt();
                        int _arg52 = data.readInt();
                        int _arg53 = data.readInt();
                        data.enforceNoDataAvail();
                        setTxPower(_arg51, _arg52, _arg53);
                        return true;
                    case 17:
                        int _arg54 = data.readInt();
                        int _arg55 = data.readInt();
                        int _arg56 = data.readInt();
                        data.enforceNoDataAvail();
                        setTxPowerStatus(_arg54, _arg55, _arg56);
                        return true;
                    case 18:
                        int _arg57 = data.readInt();
                        int _arg58 = data.readInt();
                        String _arg59 = data.readString();
                        int _arg60 = data.readInt();
                        data.enforceNoDataAvail();
                        setVendorSetting(_arg57, _arg58, _arg59, _arg60);
                        return true;
                    case TRANSACTION_triggerModeSwitchByEcc /* 19 */:
                        int _arg61 = data.readInt();
                        int _arg62 = data.readInt();
                        int _arg63 = data.readInt();
                        data.enforceNoDataAvail();
                        triggerModeSwitchByEcc(_arg61, _arg62, _arg63);
                        return true;
                    case 20:
                        int _arg64 = data.readInt();
                        String _arg65 = data.readString();
                        String _arg66 = data.readString();
                        boolean _arg67 = data.readBoolean();
                        int _arg68 = data.readInt();
                        int _arg69 = data.readInt();
                        data.enforceNoDataAvail();
                        runGbaAuthentication(_arg64, _arg65, _arg66, _arg67, _arg68, _arg69);
                        return true;
                    case TRANSACTION_sendWifiAssociated /* 21 */:
                        int _arg70 = data.readInt();
                        String _arg71 = data.readString();
                        int _arg72 = data.readInt();
                        String _arg73 = data.readString();
                        String _arg74 = data.readString();
                        int _arg75 = data.readInt();
                        String _arg76 = data.readString();
                        int _arg77 = data.readInt();
                        data.enforceNoDataAvail();
                        sendWifiAssociated(_arg70, _arg71, _arg72, _arg73, _arg74, _arg75, _arg76, _arg77);
                        return true;
                    case TRANSACTION_sendWifiEnabled /* 22 */:
                        int _arg78 = data.readInt();
                        String _arg79 = data.readString();
                        int _arg80 = data.readInt();
                        int _arg81 = data.readInt();
                        data.enforceNoDataAvail();
                        sendWifiEnabled(_arg78, _arg79, _arg80, _arg81);
                        return true;
                    case TRANSACTION_sendWifiIpAddress /* 23 */:
                        int _arg82 = data.readInt();
                        String _arg83 = data.readString();
                        String _arg84 = data.readString();
                        String _arg85 = data.readString();
                        int _arg86 = data.readInt();
                        int _arg87 = data.readInt();
                        String _arg88 = data.readString();
                        String _arg89 = data.readString();
                        int _arg90 = data.readInt();
                        String _arg91 = data.readString();
                        int _arg110 = data.readInt();
                        data.enforceNoDataAvail();
                        sendWifiIpAddress(_arg82, _arg83, _arg84, _arg85, _arg86, _arg87, _arg88, _arg89, _arg90, _arg91, _arg110);
                        return true;
                    case 24:
                        int _arg92 = data.readInt();
                        String _arg93 = data.readString();
                        String _arg94 = data.readString();
                        String _arg95 = data.readString();
                        String _arg96 = data.readString();
                        int _arg97 = data.readInt();
                        data.enforceNoDataAvail();
                        registerCellQltyReport(_arg92, _arg93, _arg94, _arg95, _arg96, _arg97);
                        return true;
                    case TRANSACTION_responseAcknowledgementMtk /* 25 */:
                        responseAcknowledgementMtk();
                        return true;
                    case TRANSACTION_setResponseFunctionsMtk /* 26 */:
                        IMtkRadioExModemResponse _arg98 = IMtkRadioExModemResponse.Stub.asInterface(data.readStrongBinder());
                        IMtkRadioExModemIndication _arg99 = IMtkRadioExModemIndication.Stub.asInterface(data.readStrongBinder());
                        data.enforceNoDataAvail();
                        setResponseFunctionsMtk(_arg98, _arg99);
                        return true;
                    case TRANSACTION_setResponseFunctionsMtkIms /* 27 */:
                        IMtkRadioExModemResponse _arg100 = IMtkRadioExModemResponse.Stub.asInterface(data.readStrongBinder());
                        IMtkRadioExModemIndication _arg101 = IMtkRadioExModemIndication.Stub.asInterface(data.readStrongBinder());
                        data.enforceNoDataAvail();
                        setResponseFunctionsMtkIms(_arg100, _arg101);
                        return true;
                    case TRANSACTION_setResponseFunctionsCap /* 28 */:
                        IMtkRadioExCapRadioResponse _arg102 = IMtkRadioExCapRadioResponse.Stub.asInterface(data.readStrongBinder());
                        data.enforceNoDataAvail();
                        setResponseFunctionsCap(_arg102);
                        return true;
                    case TRANSACTION_setResponseFunctionsForAtci /* 29 */:
                        IAtciResponse _arg103 = IAtciResponse.Stub.asInterface(data.readStrongBinder());
                        IAtciIndication _arg104 = IAtciIndication.Stub.asInterface(data.readStrongBinder());
                        data.enforceNoDataAvail();
                        setResponseFunctionsForAtci(_arg103, _arg104);
                        return true;
                    case TRANSACTION_setResponseFunctionsEm /* 30 */:
                        IEmRadioResponse _arg105 = IEmRadioResponse.Stub.asInterface(data.readStrongBinder());
                        IEmRadioIndication _arg106 = IEmRadioIndication.Stub.asInterface(data.readStrongBinder());
                        data.enforceNoDataAvail();
                        setResponseFunctionsEm(_arg105, _arg106);
                        return true;
                    case TRANSACTION_setResponseFunctionsGba /* 31 */:
                        IMtkRadioExModemResponse _arg107 = IMtkRadioExModemResponse.Stub.asInterface(data.readStrongBinder());
                        data.enforceNoDataAvail();
                        setResponseFunctionsGba(_arg107);
                        return true;
                    case 32:
                        IAssistModemResponse _arg108 = IAssistModemResponse.Stub.asInterface(data.readStrongBinder());
                        data.enforceNoDataAvail();
                        setResponseFunctionsAssist(_arg108);
                        return true;
                    case 33:
                        int _arg109 = data.readInt();
                        int _arg111 = data.readInt();
                        int _arg112 = data.readInt();
                        int _arg113 = data.readInt();
                        data.enforceNoDataAvail();
                        syncAppEventStatus(_arg109, _arg111, _arg112, _arg113);
                        return true;
                    case TRANSACTION_setIndicationFunctionsAssist /* 34 */:
                        IAssistModemIndication _arg114 = IAssistModemIndication.Stub.asInterface(data.readStrongBinder());
                        data.enforceNoDataAvail();
                        setIndicationFunctionsAssist(_arg114);
                        return true;
                    case TRANSACTION_syncLocationInfo /* 35 */:
                        int _arg115 = parcel.readInt();
                        int _arg116 = parcel.readInt();
                        byte _arg117 = parcel.readByte();
                        byte _arg118 = parcel.readByte();
                        byte _arg119 = parcel.readByte();
                        long _arg120 = parcel.readLong();
                        double _arg121 = data.readDouble();
                        double _arg122 = data.readDouble();
                        double _arg123 = data.readDouble();
                        double _arg124 = data.readDouble();
                        float _arg125 = data.readFloat();
                        float _arg126 = data.readFloat();
                        float _arg127 = data.readFloat();
                        float _arg128 = data.readFloat();
                        float _arg129 = data.readFloat();
                        float _arg130 = data.readFloat();
                        float _arg131 = data.readFloat();
                        byte _arg132 = data.readByte();
                        int _arg133 = data.readInt();
                        data.enforceNoDataAvail();
                        syncLocationInfo(_arg115, _arg116, _arg117, _arg118, _arg119, _arg120, _arg121, _arg122, _arg123, _arg124, _arg125, _arg126, _arg127, _arg128, _arg129, _arg130, _arg131, _arg132, _arg133);
                        return true;
                    case 36:
                        int _arg134 = parcel.readInt();
                        String _arg135 = parcel.readString();
                        byte _arg136 = parcel.readByte();
                        byte _arg137 = parcel.readByte();
                        int _arg138 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        setGnssMcc(_arg134, _arg135, _arg136, _arg137, _arg138);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeNoException();
            reply.writeInt(getInterfaceVersion());
            return true;
        }

        private static final class Proxy implements IMtkRadioExModem {
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void abortCertificate(int serial, int uid, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(uid);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void enableCapability(int serial, String id, int uid, int toActive, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(id);
                    _data.writeInt(uid);
                    _data.writeInt(toActive);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void getEngineeringModeInfo(int serial, int index, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(index);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void modifyModemType(int serial, int applyType, int modemType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(applyType);
                    _data.writeInt(modemType);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void restartRILD(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void routeAuthMessage(int serial, int uid, byte[] msg, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(uid);
                    _data.writeByteArray(msg);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void routeCertificate(int serial, int uid, byte[] cert, byte[] msg, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(uid);
                    _data.writeByteArray(cert);
                    _data.writeByteArray(msg);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendAtciRequest(int serial, byte[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeByteArray(data);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendEmbmsAtCommand(int serial, String data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(data);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendRequestRaw(int serial, byte[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeByteArray(data);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendRequestStrings(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendSarIndicator(int serial, int sar_cmd_type, String sar_parameter, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(sar_cmd_type);
                    _data.writeString(sar_parameter);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setMaxUlSpeed(int serial, int ulSpeed, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(ulSpeed);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setModemPower(int serial, boolean isOn, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(isOn);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setTrm(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setTxPower(int serial, int limitpower, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(limitpower);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setTxPowerStatus(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setVendorSetting(int serial, int setting, String value, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(setting);
                    _data.writeString(value);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void triggerModeSwitchByEcc(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_triggerModeSwitchByEcc, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void runGbaAuthentication(int serial, String nafFqdn, String nafSecureProtocolId, boolean forceRun, int netId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(nafFqdn);
                    _data.writeString(nafSecureProtocolId);
                    _data.writeBoolean(forceRun);
                    _data.writeInt(netId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendWifiAssociated(int serial, String ifName, int associated, String ssid, String apMac, int mtuSize, String ueMac, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ifName);
                    _data.writeInt(associated);
                    _data.writeString(ssid);
                    _data.writeString(apMac);
                    _data.writeInt(mtuSize);
                    _data.writeString(ueMac);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_sendWifiAssociated, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendWifiEnabled(int serial, String ifName, int isWifiEnabled, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ifName);
                    _data.writeInt(isWifiEnabled);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_sendWifiEnabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendWifiIpAddress(int serial, String ifName, String ipv4Addr, String ipv6Addr, int ipv4PrefixLen, int ipv6PrefixLen, String ipv4Gateway, String ipv6Gateway, int dnsCount, String dnsServers, int clientId) throws Throwable {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    try {
                        _data.writeString(ifName);
                        try {
                            _data.writeString(ipv4Addr);
                            try {
                                _data.writeString(ipv6Addr);
                                try {
                                    _data.writeInt(ipv4PrefixLen);
                                    try {
                                        _data.writeInt(ipv6PrefixLen);
                                        try {
                                            _data.writeString(ipv4Gateway);
                                            try {
                                                _data.writeString(ipv6Gateway);
                                                try {
                                                    _data.writeInt(dnsCount);
                                                    try {
                                                        _data.writeString(dnsServers);
                                                        try {
                                                            _data.writeInt(clientId);
                                                            try {
                                                                boolean _status = this.mRemote.transact(Stub.TRANSACTION_sendWifiIpAddress, _data, null, 1);
                                                                if (!_status) {
                                                                    throw new RemoteException("Unimplemented");
                                                                }
                                                                _data.recycle();
                                                            } catch (Throwable th) {
                                                                th = th;
                                                                _data.recycle();
                                                                throw th;
                                                            }
                                                        } catch (Throwable th2) {
                                                            th = th2;
                                                        }
                                                    } catch (Throwable th3) {
                                                        th = th3;
                                                        _data.recycle();
                                                        throw th;
                                                    }
                                                } catch (Throwable th4) {
                                                    th = th4;
                                                    _data.recycle();
                                                    throw th;
                                                }
                                            } catch (Throwable th5) {
                                                th = th5;
                                                _data.recycle();
                                                throw th;
                                            }
                                        } catch (Throwable th6) {
                                            th = th6;
                                            _data.recycle();
                                            throw th;
                                        }
                                    } catch (Throwable th7) {
                                        th = th7;
                                        _data.recycle();
                                        throw th;
                                    }
                                } catch (Throwable th8) {
                                    th = th8;
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th9) {
                                th = th9;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th10) {
                            th = th10;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th11) {
                        th = th11;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th12) {
                    th = th12;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void registerCellQltyReport(int serial, String registerQuality, String type, String thresholdValues, String triggerTime, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(registerQuality);
                    _data.writeString(type);
                    _data.writeString(thresholdValues);
                    _data.writeString(triggerTime);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsMtk(IMtkRadioExModemResponse radioResponse, IMtkRadioExModemIndication radioIndication) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsMtkIms(IMtkRadioExModemResponse radioResponse, IMtkRadioExModemIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsMtkIms, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsCap(IMtkRadioExCapRadioResponse capRadioResponse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(capRadioResponse);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsCap, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsForAtci(IAtciResponse atciResponseParam, IAtciIndication atciIndicationParam) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(atciResponseParam);
                    _data.writeStrongInterface(atciIndicationParam);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsForAtci, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsEm(IEmRadioResponse radioResponse, IEmRadioIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsEm, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsGba(IMtkRadioExModemResponse radioResponse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsGba, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsAssist(IAssistModemResponse radioResponse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void syncAppEventStatus(int serial, int event, int status, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(event);
                    _data.writeInt(status);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setIndicationFunctionsAssist(IAssistModemIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setIndicationFunctionsAssist, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void syncLocationInfo(int serial, int srv_id, byte ntn_enable, byte fixed_loc_info, byte provider, long utc, double latitude, double longitude, double altitude, double msl_altitude, float speed, float bearing, float horizontal_acc, float vertical_acc, float msl_alt_acc, float speed_acc, float bearing_acc, byte unavailable_cause, int clientId) throws Throwable {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(srv_id);
                    _data.writeByte(ntn_enable);
                    try {
                        _data.writeByte(fixed_loc_info);
                        try {
                            _data.writeByte(provider);
                            try {
                                _data.writeLong(utc);
                                try {
                                    _data.writeDouble(latitude);
                                    try {
                                        _data.writeDouble(longitude);
                                        try {
                                            _data.writeDouble(altitude);
                                            _data.writeDouble(msl_altitude);
                                            try {
                                                _data.writeFloat(speed);
                                                _data.writeFloat(bearing);
                                                _data.writeFloat(horizontal_acc);
                                                _data.writeFloat(vertical_acc);
                                                _data.writeFloat(msl_alt_acc);
                                                _data.writeFloat(speed_acc);
                                                _data.writeFloat(bearing_acc);
                                                _data.writeByte(unavailable_cause);
                                                _data.writeInt(clientId);
                                                boolean _status = this.mRemote.transact(Stub.TRANSACTION_syncLocationInfo, _data, null, 1);
                                                if (!_status) {
                                                    throw new RemoteException("Unimplemented");
                                                }
                                                _data.recycle();
                                            } catch (Throwable th) {
                                                th = th;
                                                _data.recycle();
                                                throw th;
                                            }
                                        } catch (Throwable th2) {
                                            th = th2;
                                            _data.recycle();
                                            throw th;
                                        }
                                    } catch (Throwable th3) {
                                        th = th3;
                                        _data.recycle();
                                        throw th;
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setGnssMcc(int serial, String mcc, byte mcc_cfg_idx, byte fbo_en, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(mcc);
                    _data.writeByte(mcc_cfg_idx);
                    _data.writeByte(fbo_en);
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
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
