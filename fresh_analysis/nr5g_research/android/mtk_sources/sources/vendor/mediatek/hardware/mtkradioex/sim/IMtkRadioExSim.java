package vendor.mediatek.hardware.mtkradioex.sim;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.rsu.IRsuRadioIndication;
import vendor.mediatek.hardware.mtkradioex.rsu.IRsuRadioResponse;
import vendor.mediatek.hardware.mtkradioex.rsu.RsuRequestInfo;
import vendor.mediatek.hardware.mtkradioex.se.ISERadioIndication;
import vendor.mediatek.hardware.mtkradioex.se.ISERadioResponse;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExSim extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$sim$IMtkRadioExSim".replace('$', '.');
    public static final String HASH = "dd10c80aa3478fddc0a46cab8597191943a29052";
    public static final int VERSION = 2;

    void activateUiccCard(int i, int i2) throws RemoteException;

    void deactivateUiccCard(int i, int i2) throws RemoteException;

    void deleteUPBEntry(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void doGeneralSimAuthentication(int i, SimAuthStructure simAuthStructure, int i2) throws RemoteException;

    void editUPBEntry(int i, String[] strArr, int i2) throws RemoteException;

    void getATR(int i, int i2) throws RemoteException;

    void getCurrentUiccCardProvisioningStatus(int i, int i2) throws RemoteException;

    void getIccid(int i, int i2) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getPhoneBookMemStorage(int i, int i2) throws RemoteException;

    void getPhoneBookStringsLength(int i, int i2) throws RemoteException;

    void getSimTypeInfo(int i, int i2) throws RemoteException;

    void handleStkCallSetupRequestFromSimWithResCode(int i, int i2, int i3) throws RemoteException;

    void queryNetworkLock(int i, int i2, int i3) throws RemoteException;

    void queryPhbStorageInfo(int i, int i2, int i3) throws RemoteException;

    void queryUPBAvailable(int i, int i2, int i3, int i4) throws RemoteException;

    void queryUPBCapability(int i, int i2) throws RemoteException;

    void readPhbEntry(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void readPhoneBookEntryExt(int i, int i2, int i3, int i4) throws RemoteException;

    void readUPBAasList(int i, int i2, int i3, int i4) throws RemoteException;

    void readUPBAnrEntry(int i, int i2, int i3, int i4) throws RemoteException;

    void readUPBEmailEntry(int i, int i2, int i3, int i4) throws RemoteException;

    void readUPBGasList(int i, int i2, int i3, int i4) throws RemoteException;

    void readUPBGrpEntry(int i, int i2, int i3) throws RemoteException;

    void readUPBSneEntry(int i, int i2, int i3, int i4) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void sendRsuRequest(int i, RsuRequestInfo rsuRequestInfo, int i2) throws RemoteException;

    void sendVsimNotification(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void sendVsimOperation(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6) throws RemoteException;

    void setNetworkLock(int i, int i2, int i3, String str, String str2, String str3, String str4, int i4) throws RemoteException;

    void setPhoneBookMemStorage(int i, String str, String str2, int i2) throws RemoteException;

    void setPhonebookReady(int i, int i2, int i3) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExSimResponse iMtkRadioExSimResponse, IMtkRadioExSimIndication iMtkRadioExSimIndication) throws RemoteException;

    void setResponseFunctionsRsu(IRsuRadioResponse iRsuRadioResponse, IRsuRadioIndication iRsuRadioIndication) throws RemoteException;

    void setResponseFunctionsSE(ISERadioResponse iSERadioResponse, ISERadioIndication iSERadioIndication) throws RemoteException;

    void setSimPower(int i, int i2, int i3) throws RemoteException;

    void supplyDepersonalization(int i, String str, int i2, int i3) throws RemoteException;

    void supplyDeviceNetworkDepersonalization(int i, String str, int i2) throws RemoteException;

    void switchSimType(int i, int i2, int i3) throws RemoteException;

    void writePhbEntry(int i, PhbEntryStructure phbEntryStructure, int i2) throws RemoteException;

    void writePhoneBookEntryExt(int i, PhbEntryExt phbEntryExt, int i2) throws RemoteException;

    void writeUPBGrpEntry(int i, int i2, int[] iArr, int i3) throws RemoteException;

    public static class Default implements IMtkRadioExSim {
        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getIccid(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void activateUiccCard(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void deactivateUiccCard(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void deleteUPBEntry(int serial, int entryType, int adnIndex, int entryIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void doGeneralSimAuthentication(int serial, SimAuthStructure simAuth, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void editUPBEntry(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getATR(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getCurrentUiccCardProvisioningStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getPhoneBookMemStorage(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getPhoneBookStringsLength(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void handleStkCallSetupRequestFromSimWithResCode(int serial, int resultCode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void queryNetworkLock(int serial, int category, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void queryPhbStorageInfo(int serial, int type, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void queryUPBAvailable(int serial, int eftype, int fileIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readPhbEntry(int serial, int type, int bIndex, int eIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readPhoneBookEntryExt(int serial, int index1, int index2, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBAasList(int serial, int startIndex, int endIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBAnrEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBEmailEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBGasList(int serial, int startIndex, int endIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBGrpEntry(int serial, int adnIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBSneEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void sendRsuRequest(int serial, RsuRequestInfo rri, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void sendVsimNotification(int serial, int transactionId, int eventId, int simType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void sendVsimOperation(int serial, int transactionId, int eventId, int result, int dataLength, byte[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setNetworkLock(int serial, int category, int lockop, String password, String data_imsi, String gid1, String gid2, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setPhoneBookMemStorage(int serial, String storage, String password, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setPhonebookReady(int serial, int ready, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setSimPower(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void writePhbEntry(int serial, PhbEntryStructure phbEntry, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void writePhoneBookEntryExt(int serial, PhbEntryExt phbEntryExt, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void writeUPBGrpEntry(int serial, int adnIndex, int[] grpIds, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void queryUPBCapability(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void supplyDepersonalization(int serial, String netPin, int type, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void supplyDeviceNetworkDepersonalization(int serial, String pwd, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setResponseFunctionsMtk(IMtkRadioExSimResponse radioResponse, IMtkRadioExSimIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setResponseFunctionsRsu(IRsuRadioResponse radioResponse, IRsuRadioIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setResponseFunctionsSE(ISERadioResponse radioResponse, ISERadioIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getSimTypeInfo(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void switchSimType(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExSim {
        static final int TRANSACTION_activateUiccCard = 2;
        static final int TRANSACTION_deactivateUiccCard = 3;
        static final int TRANSACTION_deleteUPBEntry = 4;
        static final int TRANSACTION_doGeneralSimAuthentication = 5;
        static final int TRANSACTION_editUPBEntry = 6;
        static final int TRANSACTION_getATR = 7;
        static final int TRANSACTION_getCurrentUiccCardProvisioningStatus = 8;
        static final int TRANSACTION_getIccid = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getPhoneBookMemStorage = 9;
        static final int TRANSACTION_getPhoneBookStringsLength = 10;
        static final int TRANSACTION_getSimTypeInfo = 40;
        static final int TRANSACTION_handleStkCallSetupRequestFromSimWithResCode = 11;
        static final int TRANSACTION_queryNetworkLock = 12;
        static final int TRANSACTION_queryPhbStorageInfo = 13;
        static final int TRANSACTION_queryUPBAvailable = 14;
        static final int TRANSACTION_queryUPBCapability = 33;
        static final int TRANSACTION_readPhbEntry = 15;
        static final int TRANSACTION_readPhoneBookEntryExt = 16;
        static final int TRANSACTION_readUPBAasList = 17;
        static final int TRANSACTION_readUPBAnrEntry = 18;
        static final int TRANSACTION_readUPBEmailEntry = 19;
        static final int TRANSACTION_readUPBGasList = 20;
        static final int TRANSACTION_readUPBGrpEntry = 21;
        static final int TRANSACTION_readUPBSneEntry = 22;
        static final int TRANSACTION_responseAcknowledgementMtk = 36;
        static final int TRANSACTION_sendRsuRequest = 23;
        static final int TRANSACTION_sendVsimNotification = 24;
        static final int TRANSACTION_sendVsimOperation = 25;
        static final int TRANSACTION_setNetworkLock = 26;
        static final int TRANSACTION_setPhoneBookMemStorage = 27;
        static final int TRANSACTION_setPhonebookReady = 28;
        static final int TRANSACTION_setResponseFunctionsMtk = 37;
        static final int TRANSACTION_setResponseFunctionsRsu = 38;
        static final int TRANSACTION_setResponseFunctionsSE = 39;
        static final int TRANSACTION_setSimPower = 29;
        static final int TRANSACTION_supplyDepersonalization = 34;
        static final int TRANSACTION_supplyDeviceNetworkDepersonalization = 35;
        static final int TRANSACTION_switchSimType = 41;
        static final int TRANSACTION_writePhbEntry = 30;
        static final int TRANSACTION_writePhoneBookEntryExt = 31;
        static final int TRANSACTION_writeUPBGrpEntry = 32;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExSim asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExSim)) {
                return (IMtkRadioExSim) iin;
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
                    data.enforceNoDataAvail();
                    getIccid(_arg0, _arg1);
                    return true;
                case 2:
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    data.enforceNoDataAvail();
                    activateUiccCard(_arg2, _arg3);
                    return true;
                case 3:
                    int _arg4 = data.readInt();
                    int _arg5 = data.readInt();
                    data.enforceNoDataAvail();
                    deactivateUiccCard(_arg4, _arg5);
                    return true;
                case 4:
                    int _arg6 = data.readInt();
                    int _arg7 = data.readInt();
                    int _arg8 = data.readInt();
                    int _arg9 = data.readInt();
                    int _arg10 = data.readInt();
                    data.enforceNoDataAvail();
                    deleteUPBEntry(_arg6, _arg7, _arg8, _arg9, _arg10);
                    return true;
                case 5:
                    int _arg11 = data.readInt();
                    SimAuthStructure _arg12 = (SimAuthStructure) data.readTypedObject(SimAuthStructure.CREATOR);
                    int _arg13 = data.readInt();
                    data.enforceNoDataAvail();
                    doGeneralSimAuthentication(_arg11, _arg12, _arg13);
                    return true;
                case 6:
                    int _arg14 = data.readInt();
                    String[] _arg15 = data.createStringArray();
                    int _arg16 = data.readInt();
                    data.enforceNoDataAvail();
                    editUPBEntry(_arg14, _arg15, _arg16);
                    return true;
                case 7:
                    int _arg17 = data.readInt();
                    int _arg18 = data.readInt();
                    data.enforceNoDataAvail();
                    getATR(_arg17, _arg18);
                    return true;
                case 8:
                    int _arg19 = data.readInt();
                    int _arg20 = data.readInt();
                    data.enforceNoDataAvail();
                    getCurrentUiccCardProvisioningStatus(_arg19, _arg20);
                    return true;
                case 9:
                    int _arg21 = data.readInt();
                    int _arg22 = data.readInt();
                    data.enforceNoDataAvail();
                    getPhoneBookMemStorage(_arg21, _arg22);
                    return true;
                case 10:
                    int _arg23 = data.readInt();
                    int _arg24 = data.readInt();
                    data.enforceNoDataAvail();
                    getPhoneBookStringsLength(_arg23, _arg24);
                    return true;
                case 11:
                    int _arg25 = data.readInt();
                    int _arg26 = data.readInt();
                    int _arg27 = data.readInt();
                    data.enforceNoDataAvail();
                    handleStkCallSetupRequestFromSimWithResCode(_arg25, _arg26, _arg27);
                    return true;
                case 12:
                    int _arg28 = data.readInt();
                    int _arg29 = data.readInt();
                    int _arg30 = data.readInt();
                    data.enforceNoDataAvail();
                    queryNetworkLock(_arg28, _arg29, _arg30);
                    return true;
                case 13:
                    int _arg31 = data.readInt();
                    int _arg32 = data.readInt();
                    int _arg33 = data.readInt();
                    data.enforceNoDataAvail();
                    queryPhbStorageInfo(_arg31, _arg32, _arg33);
                    return true;
                case 14:
                    int _arg34 = data.readInt();
                    int _arg35 = data.readInt();
                    int _arg36 = data.readInt();
                    int _arg37 = data.readInt();
                    data.enforceNoDataAvail();
                    queryUPBAvailable(_arg34, _arg35, _arg36, _arg37);
                    return true;
                case 15:
                    int _arg38 = data.readInt();
                    int _arg39 = data.readInt();
                    int _arg40 = data.readInt();
                    int _arg41 = data.readInt();
                    int _arg42 = data.readInt();
                    data.enforceNoDataAvail();
                    readPhbEntry(_arg38, _arg39, _arg40, _arg41, _arg42);
                    return true;
                case 16:
                    int _arg43 = data.readInt();
                    int _arg44 = data.readInt();
                    int _arg45 = data.readInt();
                    int _arg46 = data.readInt();
                    data.enforceNoDataAvail();
                    readPhoneBookEntryExt(_arg43, _arg44, _arg45, _arg46);
                    return true;
                case 17:
                    int _arg47 = data.readInt();
                    int _arg48 = data.readInt();
                    int _arg49 = data.readInt();
                    int _arg50 = data.readInt();
                    data.enforceNoDataAvail();
                    readUPBAasList(_arg47, _arg48, _arg49, _arg50);
                    return true;
                case 18:
                    int _arg51 = data.readInt();
                    int _arg52 = data.readInt();
                    int _arg53 = data.readInt();
                    int _arg54 = data.readInt();
                    data.enforceNoDataAvail();
                    readUPBAnrEntry(_arg51, _arg52, _arg53, _arg54);
                    return true;
                case TRANSACTION_readUPBEmailEntry /* 19 */:
                    int _arg55 = data.readInt();
                    int _arg56 = data.readInt();
                    int _arg57 = data.readInt();
                    int _arg58 = data.readInt();
                    data.enforceNoDataAvail();
                    readUPBEmailEntry(_arg55, _arg56, _arg57, _arg58);
                    return true;
                case 20:
                    int _arg59 = data.readInt();
                    int _arg60 = data.readInt();
                    int _arg61 = data.readInt();
                    int _arg62 = data.readInt();
                    data.enforceNoDataAvail();
                    readUPBGasList(_arg59, _arg60, _arg61, _arg62);
                    return true;
                case TRANSACTION_readUPBGrpEntry /* 21 */:
                    int _arg63 = data.readInt();
                    int _arg64 = data.readInt();
                    int _arg65 = data.readInt();
                    data.enforceNoDataAvail();
                    readUPBGrpEntry(_arg63, _arg64, _arg65);
                    return true;
                case TRANSACTION_readUPBSneEntry /* 22 */:
                    int _arg66 = data.readInt();
                    int _arg67 = data.readInt();
                    int _arg68 = data.readInt();
                    int _arg69 = data.readInt();
                    data.enforceNoDataAvail();
                    readUPBSneEntry(_arg66, _arg67, _arg68, _arg69);
                    return true;
                case TRANSACTION_sendRsuRequest /* 23 */:
                    int _arg70 = data.readInt();
                    RsuRequestInfo _arg71 = (RsuRequestInfo) data.readTypedObject(RsuRequestInfo.CREATOR);
                    int _arg72 = data.readInt();
                    data.enforceNoDataAvail();
                    sendRsuRequest(_arg70, _arg71, _arg72);
                    return true;
                case 24:
                    int _arg73 = data.readInt();
                    int _arg74 = data.readInt();
                    int _arg75 = data.readInt();
                    int _arg76 = data.readInt();
                    int _arg77 = data.readInt();
                    data.enforceNoDataAvail();
                    sendVsimNotification(_arg73, _arg74, _arg75, _arg76, _arg77);
                    return true;
                case TRANSACTION_sendVsimOperation /* 25 */:
                    int _arg78 = data.readInt();
                    int _arg79 = data.readInt();
                    int _arg80 = data.readInt();
                    int _arg81 = data.readInt();
                    int _arg82 = data.readInt();
                    byte[] _arg83 = data.createByteArray();
                    int _arg84 = data.readInt();
                    data.enforceNoDataAvail();
                    sendVsimOperation(_arg78, _arg79, _arg80, _arg81, _arg82, _arg83, _arg84);
                    return true;
                case TRANSACTION_setNetworkLock /* 26 */:
                    int _arg85 = data.readInt();
                    int _arg86 = data.readInt();
                    int _arg87 = data.readInt();
                    String _arg88 = data.readString();
                    String _arg89 = data.readString();
                    String _arg90 = data.readString();
                    String _arg91 = data.readString();
                    int _arg92 = data.readInt();
                    data.enforceNoDataAvail();
                    setNetworkLock(_arg85, _arg86, _arg87, _arg88, _arg89, _arg90, _arg91, _arg92);
                    return true;
                case TRANSACTION_setPhoneBookMemStorage /* 27 */:
                    int _arg93 = data.readInt();
                    String _arg94 = data.readString();
                    String _arg95 = data.readString();
                    int _arg96 = data.readInt();
                    data.enforceNoDataAvail();
                    setPhoneBookMemStorage(_arg93, _arg94, _arg95, _arg96);
                    return true;
                case TRANSACTION_setPhonebookReady /* 28 */:
                    int _arg97 = data.readInt();
                    int _arg98 = data.readInt();
                    int _arg99 = data.readInt();
                    data.enforceNoDataAvail();
                    setPhonebookReady(_arg97, _arg98, _arg99);
                    return true;
                case TRANSACTION_setSimPower /* 29 */:
                    int _arg100 = data.readInt();
                    int _arg101 = data.readInt();
                    int _arg102 = data.readInt();
                    data.enforceNoDataAvail();
                    setSimPower(_arg100, _arg101, _arg102);
                    return true;
                case TRANSACTION_writePhbEntry /* 30 */:
                    int _arg103 = data.readInt();
                    PhbEntryStructure _arg104 = (PhbEntryStructure) data.readTypedObject(PhbEntryStructure.CREATOR);
                    int _arg105 = data.readInt();
                    data.enforceNoDataAvail();
                    writePhbEntry(_arg103, _arg104, _arg105);
                    return true;
                case TRANSACTION_writePhoneBookEntryExt /* 31 */:
                    int _arg106 = data.readInt();
                    PhbEntryExt _arg107 = (PhbEntryExt) data.readTypedObject(PhbEntryExt.CREATOR);
                    int _arg108 = data.readInt();
                    data.enforceNoDataAvail();
                    writePhoneBookEntryExt(_arg106, _arg107, _arg108);
                    return true;
                case 32:
                    int _arg109 = data.readInt();
                    int _arg110 = data.readInt();
                    int[] _arg111 = data.createIntArray();
                    int _arg112 = data.readInt();
                    data.enforceNoDataAvail();
                    writeUPBGrpEntry(_arg109, _arg110, _arg111, _arg112);
                    return true;
                case 33:
                    int _arg113 = data.readInt();
                    int _arg114 = data.readInt();
                    data.enforceNoDataAvail();
                    queryUPBCapability(_arg113, _arg114);
                    return true;
                case TRANSACTION_supplyDepersonalization /* 34 */:
                    int _arg115 = data.readInt();
                    String _arg116 = data.readString();
                    int _arg117 = data.readInt();
                    int _arg118 = data.readInt();
                    data.enforceNoDataAvail();
                    supplyDepersonalization(_arg115, _arg116, _arg117, _arg118);
                    return true;
                case TRANSACTION_supplyDeviceNetworkDepersonalization /* 35 */:
                    int _arg119 = data.readInt();
                    String _arg120 = data.readString();
                    int _arg121 = data.readInt();
                    data.enforceNoDataAvail();
                    supplyDeviceNetworkDepersonalization(_arg119, _arg120, _arg121);
                    return true;
                case 36:
                    responseAcknowledgementMtk();
                    return true;
                case TRANSACTION_setResponseFunctionsMtk /* 37 */:
                    IMtkRadioExSimResponse _arg122 = IMtkRadioExSimResponse.Stub.asInterface(data.readStrongBinder());
                    IMtkRadioExSimIndication _arg123 = IMtkRadioExSimIndication.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setResponseFunctionsMtk(_arg122, _arg123);
                    return true;
                case 38:
                    IRsuRadioResponse _arg124 = IRsuRadioResponse.Stub.asInterface(data.readStrongBinder());
                    IRsuRadioIndication _arg125 = IRsuRadioIndication.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setResponseFunctionsRsu(_arg124, _arg125);
                    return true;
                case TRANSACTION_setResponseFunctionsSE /* 39 */:
                    ISERadioResponse _arg126 = ISERadioResponse.Stub.asInterface(data.readStrongBinder());
                    ISERadioIndication _arg127 = ISERadioIndication.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setResponseFunctionsSE(_arg126, _arg127);
                    return true;
                case 40:
                    int _arg128 = data.readInt();
                    int _arg129 = data.readInt();
                    data.enforceNoDataAvail();
                    getSimTypeInfo(_arg128, _arg129);
                    return true;
                case TRANSACTION_switchSimType /* 41 */:
                    int _arg130 = data.readInt();
                    int _arg131 = data.readInt();
                    int _arg132 = data.readInt();
                    data.enforceNoDataAvail();
                    switchSimType(_arg130, _arg131, _arg132);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExSim {
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getIccid(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void activateUiccCard(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void deactivateUiccCard(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void deleteUPBEntry(int serial, int entryType, int adnIndex, int entryIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(entryType);
                    _data.writeInt(adnIndex);
                    _data.writeInt(entryIndex);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void doGeneralSimAuthentication(int serial, SimAuthStructure simAuth, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(simAuth, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void editUPBEntry(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getATR(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getCurrentUiccCardProvisioningStatus(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getPhoneBookMemStorage(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getPhoneBookStringsLength(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void handleStkCallSetupRequestFromSimWithResCode(int serial, int resultCode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(resultCode);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void queryNetworkLock(int serial, int category, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(category);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void queryPhbStorageInfo(int serial, int type, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(type);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void queryUPBAvailable(int serial, int eftype, int fileIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(eftype);
                    _data.writeInt(fileIndex);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readPhbEntry(int serial, int type, int bIndex, int eIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(type);
                    _data.writeInt(bIndex);
                    _data.writeInt(eIndex);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readPhoneBookEntryExt(int serial, int index1, int index2, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(index1);
                    _data.writeInt(index2);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBAasList(int serial, int startIndex, int endIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(startIndex);
                    _data.writeInt(endIndex);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBAnrEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(adnIndex);
                    _data.writeInt(fileIndex);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBEmailEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(adnIndex);
                    _data.writeInt(fileIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_readUPBEmailEntry, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBGasList(int serial, int startIndex, int endIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(startIndex);
                    _data.writeInt(endIndex);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBGrpEntry(int serial, int adnIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(adnIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_readUPBGrpEntry, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBSneEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(adnIndex);
                    _data.writeInt(fileIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_readUPBSneEntry, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void sendRsuRequest(int serial, RsuRequestInfo rri, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(rri, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_sendRsuRequest, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void sendVsimNotification(int serial, int transactionId, int eventId, int simType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(transactionId);
                    _data.writeInt(eventId);
                    _data.writeInt(simType);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void sendVsimOperation(int serial, int transactionId, int eventId, int result, int dataLength, byte[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(transactionId);
                    _data.writeInt(eventId);
                    _data.writeInt(result);
                    _data.writeInt(dataLength);
                    _data.writeByteArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_sendVsimOperation, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setNetworkLock(int serial, int category, int lockop, String password, String data_imsi, String gid1, String gid2, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(category);
                    _data.writeInt(lockop);
                    _data.writeString(password);
                    _data.writeString(data_imsi);
                    _data.writeString(gid1);
                    _data.writeString(gid2);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNetworkLock, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setPhoneBookMemStorage(int serial, String storage, String password, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(storage);
                    _data.writeString(password);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setPhoneBookMemStorage, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setPhonebookReady(int serial, int ready, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(ready);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setPhonebookReady, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setSimPower(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSimPower, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void writePhbEntry(int serial, PhbEntryStructure phbEntry, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(phbEntry, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_writePhbEntry, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void writePhoneBookEntryExt(int serial, PhbEntryExt phbEntryExt, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(phbEntryExt, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_writePhoneBookEntryExt, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void writeUPBGrpEntry(int serial, int adnIndex, int[] grpIds, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(adnIndex);
                    _data.writeIntArray(grpIds);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void queryUPBCapability(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void supplyDepersonalization(int serial, String netPin, int type, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(netPin);
                    _data.writeInt(type);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_supplyDepersonalization, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void supplyDeviceNetworkDepersonalization(int serial, String pwd, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(pwd);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_supplyDeviceNetworkDepersonalization, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void responseAcknowledgementMtk() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setResponseFunctionsMtk(IMtkRadioExSimResponse radioResponse, IMtkRadioExSimIndication radioIndication) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setResponseFunctionsRsu(IRsuRadioResponse radioResponse, IRsuRadioIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setResponseFunctionsSE(ISERadioResponse radioResponse, ISERadioIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsSE, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getSimTypeInfo(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void switchSimType(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_switchSimType, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
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
