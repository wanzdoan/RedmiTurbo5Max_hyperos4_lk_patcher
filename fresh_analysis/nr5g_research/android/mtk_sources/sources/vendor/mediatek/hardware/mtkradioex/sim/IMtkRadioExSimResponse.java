package vendor.mediatek.hardware.mtkradioex.sim;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.rsu.RsuResponseInfo;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExSimResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$sim$IMtkRadioExSimResponse".replace('$', '.');
    public static final String HASH = "dd10c80aa3478fddc0a46cab8597191943a29052";
    public static final int VERSION = 2;

    void activateUiccCardRsp(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void deactivateUiccCardRsp(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void deleteUPBEntryResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void editUPBEntryResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void getATRResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    void getCurrentUiccCardProvisioningStatusRsp(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getIccidResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getPhoneBookMemStorageResponse(RadioResponseInfo radioResponseInfo, PhbMemStorageResponse phbMemStorageResponse) throws RemoteException;

    void getPhoneBookStringsLengthResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getSimTypeInfoResponse(RadioResponseInfo radioResponseInfo, SimTypeInfo simTypeInfo) throws RemoteException;

    void handleStkCallSetupRequestFromSimWithResCodeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void queryNetworkLockResponse(RadioResponseInfo radioResponseInfo, int i, int i2, int i3, int i4, int i5, int i6, int i7) throws RemoteException;

    void queryPhbStorageInfoResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void queryUPBAvailableResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void queryUPBCapabilityResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void readPhbEntryResponse(RadioResponseInfo radioResponseInfo, PhbEntryStructure[] phbEntryStructureArr) throws RemoteException;

    void readPhoneBookEntryExtResponse(RadioResponseInfo radioResponseInfo, PhbEntryExt[] phbEntryExtArr) throws RemoteException;

    void readUPBAasListResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void readUPBAnrEntryResponse(RadioResponseInfo radioResponseInfo, PhbEntryStructure[] phbEntryStructureArr) throws RemoteException;

    void readUPBEmailEntryResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    void readUPBGasListResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void readUPBGrpEntryResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void readUPBSneEntryResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    void sendRsuRequestResponse(RadioResponseInfo radioResponseInfo, RsuResponseInfo rsuResponseInfo) throws RemoteException;

    void setNetworkLockResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setPhoneBookMemStorageResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setPhonebookReadyResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSimPowerResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void supplyDepersonalizationResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void supplyDeviceNetworkDepersonalizationResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void switchSimTypeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void vsimNotificationResponse(RadioResponseInfo radioResponseInfo, VsimEvent vsimEvent) throws RemoteException;

    void vsimOperationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void writePhbEntryResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void writePhoneBookEntryExtResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void writeUPBGrpEntryResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    public static class Default implements IMtkRadioExSimResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void getIccidResponse(RadioResponseInfo info, String response) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void activateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void deactivateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void deleteUPBEntryResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void editUPBEntryResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void getATRResponse(RadioResponseInfo info, String response) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void getCurrentUiccCardProvisioningStatusRsp(RadioResponseInfo info, int simPowerOnOffStatus) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void getPhoneBookMemStorageResponse(RadioResponseInfo info, PhbMemStorageResponse phbMemStorage) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void getPhoneBookStringsLengthResponse(RadioResponseInfo info, int[] stringLengthInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void handleStkCallSetupRequestFromSimWithResCodeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void queryNetworkLockResponse(RadioResponseInfo info, int catagory, int state, int retry_cnt, int autolock_cnt, int num_set, int total_set, int key_state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void setNetworkLockResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void queryPhbStorageInfoResponse(RadioResponseInfo info, int[] storageInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void queryUPBAvailableResponse(RadioResponseInfo info, int[] upbAvailable) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void queryUPBCapabilityResponse(RadioResponseInfo info, int[] upbCapability) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void readPhbEntryResponse(RadioResponseInfo info, PhbEntryStructure[] phbEntries) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void readPhoneBookEntryExtResponse(RadioResponseInfo info, PhbEntryExt[] phbEntryExts) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void readUPBAasListResponse(RadioResponseInfo info, String[] aasList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void readUPBAnrEntryResponse(RadioResponseInfo info, PhbEntryStructure[] anrs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void readUPBEmailEntryResponse(RadioResponseInfo info, String email) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void readUPBGasListResponse(RadioResponseInfo info, String[] gasList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void readUPBGrpEntryResponse(RadioResponseInfo info, int[] grpEntries) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void readUPBSneEntryResponse(RadioResponseInfo info, String sne) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void sendRsuRequestResponse(RadioResponseInfo info, RsuResponseInfo rri) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void vsimNotificationResponse(RadioResponseInfo info, VsimEvent event) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void vsimOperationResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void setPhoneBookMemStorageResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void setPhonebookReadyResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void setSimPowerResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void writePhbEntryResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void writePhoneBookEntryExtResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void writeUPBGrpEntryResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void supplyDepersonalizationResponse(RadioResponseInfo info, int remainingRetries) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void supplyDeviceNetworkDepersonalizationResponse(RadioResponseInfo info, int remainingRetries) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void getSimTypeInfoResponse(RadioResponseInfo info, SimTypeInfo typeInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public void switchSimTypeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExSimResponse {
        static final int TRANSACTION_activateUiccCardRsp = 2;
        static final int TRANSACTION_deactivateUiccCardRsp = 3;
        static final int TRANSACTION_deleteUPBEntryResponse = 4;
        static final int TRANSACTION_editUPBEntryResponse = 5;
        static final int TRANSACTION_getATRResponse = 6;
        static final int TRANSACTION_getCurrentUiccCardProvisioningStatusRsp = 7;
        static final int TRANSACTION_getIccidResponse = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getPhoneBookMemStorageResponse = 8;
        static final int TRANSACTION_getPhoneBookStringsLengthResponse = 9;
        static final int TRANSACTION_getSimTypeInfoResponse = 35;
        static final int TRANSACTION_handleStkCallSetupRequestFromSimWithResCodeResponse = 10;
        static final int TRANSACTION_queryNetworkLockResponse = 11;
        static final int TRANSACTION_queryPhbStorageInfoResponse = 13;
        static final int TRANSACTION_queryUPBAvailableResponse = 14;
        static final int TRANSACTION_queryUPBCapabilityResponse = 15;
        static final int TRANSACTION_readPhbEntryResponse = 16;
        static final int TRANSACTION_readPhoneBookEntryExtResponse = 17;
        static final int TRANSACTION_readUPBAasListResponse = 18;
        static final int TRANSACTION_readUPBAnrEntryResponse = 19;
        static final int TRANSACTION_readUPBEmailEntryResponse = 20;
        static final int TRANSACTION_readUPBGasListResponse = 21;
        static final int TRANSACTION_readUPBGrpEntryResponse = 22;
        static final int TRANSACTION_readUPBSneEntryResponse = 23;
        static final int TRANSACTION_sendRsuRequestResponse = 24;
        static final int TRANSACTION_setNetworkLockResponse = 12;
        static final int TRANSACTION_setPhoneBookMemStorageResponse = 27;
        static final int TRANSACTION_setPhonebookReadyResponse = 28;
        static final int TRANSACTION_setSimPowerResponse = 29;
        static final int TRANSACTION_supplyDepersonalizationResponse = 33;
        static final int TRANSACTION_supplyDeviceNetworkDepersonalizationResponse = 34;
        static final int TRANSACTION_switchSimTypeResponse = 36;
        static final int TRANSACTION_vsimNotificationResponse = 25;
        static final int TRANSACTION_vsimOperationResponse = 26;
        static final int TRANSACTION_writePhbEntryResponse = 30;
        static final int TRANSACTION_writePhoneBookEntryExtResponse = 31;
        static final int TRANSACTION_writeUPBGrpEntryResponse = 32;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExSimResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExSimResponse)) {
                return (IMtkRadioExSimResponse) iin;
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
                    String _arg1 = data.readString();
                    data.enforceNoDataAvail();
                    getIccidResponse(_arg0, _arg1);
                    return true;
                case 2:
                    RadioResponseInfo _arg2 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg3 = data.readInt();
                    data.enforceNoDataAvail();
                    activateUiccCardRsp(_arg2, _arg3);
                    return true;
                case 3:
                    RadioResponseInfo _arg4 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg5 = data.readInt();
                    data.enforceNoDataAvail();
                    deactivateUiccCardRsp(_arg4, _arg5);
                    return true;
                case 4:
                    RadioResponseInfo _arg6 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    deleteUPBEntryResponse(_arg6);
                    return true;
                case 5:
                    RadioResponseInfo _arg7 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    editUPBEntryResponse(_arg7);
                    return true;
                case 6:
                    RadioResponseInfo _arg8 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String _arg9 = data.readString();
                    data.enforceNoDataAvail();
                    getATRResponse(_arg8, _arg9);
                    return true;
                case 7:
                    RadioResponseInfo _arg10 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg11 = data.readInt();
                    data.enforceNoDataAvail();
                    getCurrentUiccCardProvisioningStatusRsp(_arg10, _arg11);
                    return true;
                case 8:
                    RadioResponseInfo _arg12 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    PhbMemStorageResponse _arg13 = (PhbMemStorageResponse) data.readTypedObject(PhbMemStorageResponse.CREATOR);
                    data.enforceNoDataAvail();
                    getPhoneBookMemStorageResponse(_arg12, _arg13);
                    return true;
                case 9:
                    RadioResponseInfo _arg14 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg15 = data.createIntArray();
                    data.enforceNoDataAvail();
                    getPhoneBookStringsLengthResponse(_arg14, _arg15);
                    return true;
                case 10:
                    RadioResponseInfo _arg16 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    handleStkCallSetupRequestFromSimWithResCodeResponse(_arg16);
                    return true;
                case 11:
                    RadioResponseInfo _arg17 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg18 = data.readInt();
                    int _arg19 = data.readInt();
                    int _arg20 = data.readInt();
                    int _arg21 = data.readInt();
                    int _arg22 = data.readInt();
                    int _arg23 = data.readInt();
                    int _arg24 = data.readInt();
                    data.enforceNoDataAvail();
                    queryNetworkLockResponse(_arg17, _arg18, _arg19, _arg20, _arg21, _arg22, _arg23, _arg24);
                    return true;
                case 12:
                    RadioResponseInfo _arg25 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setNetworkLockResponse(_arg25);
                    return true;
                case 13:
                    RadioResponseInfo _arg26 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg27 = data.createIntArray();
                    data.enforceNoDataAvail();
                    queryPhbStorageInfoResponse(_arg26, _arg27);
                    return true;
                case 14:
                    RadioResponseInfo _arg28 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg29 = data.createIntArray();
                    data.enforceNoDataAvail();
                    queryUPBAvailableResponse(_arg28, _arg29);
                    return true;
                case 15:
                    RadioResponseInfo _arg30 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg31 = data.createIntArray();
                    data.enforceNoDataAvail();
                    queryUPBCapabilityResponse(_arg30, _arg31);
                    return true;
                case 16:
                    RadioResponseInfo _arg32 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    PhbEntryStructure[] _arg33 = (PhbEntryStructure[]) data.createTypedArray(PhbEntryStructure.CREATOR);
                    data.enforceNoDataAvail();
                    readPhbEntryResponse(_arg32, _arg33);
                    return true;
                case 17:
                    RadioResponseInfo _arg34 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    PhbEntryExt[] _arg35 = (PhbEntryExt[]) data.createTypedArray(PhbEntryExt.CREATOR);
                    data.enforceNoDataAvail();
                    readPhoneBookEntryExtResponse(_arg34, _arg35);
                    return true;
                case 18:
                    RadioResponseInfo _arg36 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String[] _arg37 = data.createStringArray();
                    data.enforceNoDataAvail();
                    readUPBAasListResponse(_arg36, _arg37);
                    return true;
                case TRANSACTION_readUPBAnrEntryResponse /* 19 */:
                    RadioResponseInfo _arg38 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    PhbEntryStructure[] _arg39 = (PhbEntryStructure[]) data.createTypedArray(PhbEntryStructure.CREATOR);
                    data.enforceNoDataAvail();
                    readUPBAnrEntryResponse(_arg38, _arg39);
                    return true;
                case 20:
                    RadioResponseInfo _arg40 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String _arg41 = data.readString();
                    data.enforceNoDataAvail();
                    readUPBEmailEntryResponse(_arg40, _arg41);
                    return true;
                case TRANSACTION_readUPBGasListResponse /* 21 */:
                    RadioResponseInfo _arg42 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String[] _arg43 = data.createStringArray();
                    data.enforceNoDataAvail();
                    readUPBGasListResponse(_arg42, _arg43);
                    return true;
                case TRANSACTION_readUPBGrpEntryResponse /* 22 */:
                    RadioResponseInfo _arg44 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int[] _arg45 = data.createIntArray();
                    data.enforceNoDataAvail();
                    readUPBGrpEntryResponse(_arg44, _arg45);
                    return true;
                case TRANSACTION_readUPBSneEntryResponse /* 23 */:
                    RadioResponseInfo _arg46 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String _arg47 = data.readString();
                    data.enforceNoDataAvail();
                    readUPBSneEntryResponse(_arg46, _arg47);
                    return true;
                case 24:
                    RadioResponseInfo _arg48 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    RsuResponseInfo _arg49 = (RsuResponseInfo) data.readTypedObject(RsuResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    sendRsuRequestResponse(_arg48, _arg49);
                    return true;
                case TRANSACTION_vsimNotificationResponse /* 25 */:
                    RadioResponseInfo _arg50 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    VsimEvent _arg51 = (VsimEvent) data.readTypedObject(VsimEvent.CREATOR);
                    data.enforceNoDataAvail();
                    vsimNotificationResponse(_arg50, _arg51);
                    return true;
                case TRANSACTION_vsimOperationResponse /* 26 */:
                    RadioResponseInfo _arg52 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    vsimOperationResponse(_arg52);
                    return true;
                case TRANSACTION_setPhoneBookMemStorageResponse /* 27 */:
                    RadioResponseInfo _arg53 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setPhoneBookMemStorageResponse(_arg53);
                    return true;
                case TRANSACTION_setPhonebookReadyResponse /* 28 */:
                    RadioResponseInfo _arg54 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setPhonebookReadyResponse(_arg54);
                    return true;
                case TRANSACTION_setSimPowerResponse /* 29 */:
                    RadioResponseInfo _arg55 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setSimPowerResponse(_arg55);
                    return true;
                case TRANSACTION_writePhbEntryResponse /* 30 */:
                    RadioResponseInfo _arg56 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    writePhbEntryResponse(_arg56);
                    return true;
                case TRANSACTION_writePhoneBookEntryExtResponse /* 31 */:
                    RadioResponseInfo _arg57 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    writePhoneBookEntryExtResponse(_arg57);
                    return true;
                case 32:
                    RadioResponseInfo _arg58 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    writeUPBGrpEntryResponse(_arg58);
                    return true;
                case 33:
                    RadioResponseInfo _arg59 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg60 = data.readInt();
                    data.enforceNoDataAvail();
                    supplyDepersonalizationResponse(_arg59, _arg60);
                    return true;
                case TRANSACTION_supplyDeviceNetworkDepersonalizationResponse /* 34 */:
                    RadioResponseInfo _arg61 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg62 = data.readInt();
                    data.enforceNoDataAvail();
                    supplyDeviceNetworkDepersonalizationResponse(_arg61, _arg62);
                    return true;
                case TRANSACTION_getSimTypeInfoResponse /* 35 */:
                    RadioResponseInfo _arg63 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    SimTypeInfo _arg64 = (SimTypeInfo) data.readTypedObject(SimTypeInfo.CREATOR);
                    data.enforceNoDataAvail();
                    getSimTypeInfoResponse(_arg63, _arg64);
                    return true;
                case 36:
                    RadioResponseInfo _arg65 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    switchSimTypeResponse(_arg65);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExSimResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void getIccidResponse(RadioResponseInfo info, String response) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(response);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void activateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(simPowerOnOffResponse);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void deactivateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(simPowerOnOffResponse);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void deleteUPBEntryResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void editUPBEntryResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void getATRResponse(RadioResponseInfo info, String response) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(response);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void getCurrentUiccCardProvisioningStatusRsp(RadioResponseInfo info, int simPowerOnOffStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(simPowerOnOffStatus);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void getPhoneBookMemStorageResponse(RadioResponseInfo info, PhbMemStorageResponse phbMemStorage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(phbMemStorage, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void getPhoneBookStringsLengthResponse(RadioResponseInfo info, int[] stringLengthInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(stringLengthInfo);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void handleStkCallSetupRequestFromSimWithResCodeResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void queryNetworkLockResponse(RadioResponseInfo info, int catagory, int state, int retry_cnt, int autolock_cnt, int num_set, int total_set, int key_state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(catagory);
                    _data.writeInt(state);
                    _data.writeInt(retry_cnt);
                    _data.writeInt(autolock_cnt);
                    _data.writeInt(num_set);
                    _data.writeInt(total_set);
                    _data.writeInt(key_state);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void setNetworkLockResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void queryPhbStorageInfoResponse(RadioResponseInfo info, int[] storageInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(storageInfo);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void queryUPBAvailableResponse(RadioResponseInfo info, int[] upbAvailable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(upbAvailable);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void queryUPBCapabilityResponse(RadioResponseInfo info, int[] upbCapability) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(upbCapability);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void readPhbEntryResponse(RadioResponseInfo info, PhbEntryStructure[] phbEntries) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedArray(phbEntries, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void readPhoneBookEntryExtResponse(RadioResponseInfo info, PhbEntryExt[] phbEntryExts) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedArray(phbEntryExts, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void readUPBAasListResponse(RadioResponseInfo info, String[] aasList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeStringArray(aasList);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void readUPBAnrEntryResponse(RadioResponseInfo info, PhbEntryStructure[] anrs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedArray(anrs, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_readUPBAnrEntryResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void readUPBEmailEntryResponse(RadioResponseInfo info, String email) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(email);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void readUPBGasListResponse(RadioResponseInfo info, String[] gasList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeStringArray(gasList);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_readUPBGasListResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void readUPBGrpEntryResponse(RadioResponseInfo info, int[] grpEntries) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(grpEntries);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_readUPBGrpEntryResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void readUPBSneEntryResponse(RadioResponseInfo info, String sne) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(sne);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_readUPBSneEntryResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void sendRsuRequestResponse(RadioResponseInfo info, RsuResponseInfo rri) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(rri, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void vsimNotificationResponse(RadioResponseInfo info, VsimEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(event, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_vsimNotificationResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void vsimOperationResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_vsimOperationResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void setPhoneBookMemStorageResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setPhoneBookMemStorageResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void setPhonebookReadyResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setPhonebookReadyResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void setSimPowerResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSimPowerResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void writePhbEntryResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_writePhbEntryResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void writePhoneBookEntryExtResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_writePhoneBookEntryExtResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void writeUPBGrpEntryResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void supplyDepersonalizationResponse(RadioResponseInfo info, int remainingRetries) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(remainingRetries);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void supplyDeviceNetworkDepersonalizationResponse(RadioResponseInfo info, int remainingRetries) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(remainingRetries);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_supplyDeviceNetworkDepersonalizationResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void getSimTypeInfoResponse(RadioResponseInfo info, SimTypeInfo typeInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(typeInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getSimTypeInfoResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Unimplemented");
                    }
                    _data.recycle();
                } catch (Throwable th) {
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
            public void switchSimTypeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
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
