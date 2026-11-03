package vendor.mediatek.hardware.mtkradioex.data;

import android.hardware.radio.data.DataProfileInfo;
import android.hardware.radio.data.LinkAddress;
import android.hardware.radio.data.SliceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.assist.IAssistRadioResponse;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExData extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$data$IMtkRadioExData".replace('$', '.');
    public static final String HASH = "0f9b0198bf345f123790d9c279bde6bd25a2a227";
    public static final int VERSION = 3;

    void dataConnectionAttach(int i, int i2, int i3) throws RemoteException;

    void dataConnectionDetach(int i, int i2, int i3) throws RemoteException;

    void enableDsdaIndication(int i, boolean z, int i2) throws RemoteException;

    void getDsdaStatus(int i, int i2) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void resetAllConnections(int i, int i2) throws RemoteException;

    void resetMdDataRetryCount(int i, String str, int i2) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void setFdMode(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void setResponseFunctionsAssist(IAssistRadioResponse iAssistRadioResponse) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExDataResponse iMtkRadioExDataResponse, IMtkRadioExDataIndication iMtkRadioExDataIndication) throws RemoteException;

    void setupDataCallSlice(int i, int i2, DataProfileInfo dataProfileInfo, boolean z, int i3, LinkAddress[] linkAddressArr, String[] strArr, int i4, SliceInfo sliceInfo, boolean z2, TrafficDescriptor trafficDescriptor, String str, int i5, int i6) throws RemoteException;

    void syncDataSettingsToMd(int i, int[] iArr, int i2) throws RemoteException;

    public static class Default implements IMtkRadioExData {
        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void dataConnectionAttach(int serial, int type, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void dataConnectionDetach(int serial, int type, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void enableDsdaIndication(int serial, boolean enable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void getDsdaStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void resetAllConnections(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void resetMdDataRetryCount(int serial, String apn, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void setFdMode(int serial, int mode, int param1, int param2, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void syncDataSettingsToMd(int serial, int[] settings, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void setResponseFunctionsMtk(IMtkRadioExDataResponse radioResponse, IMtkRadioExDataIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void setResponseFunctionsAssist(IAssistRadioResponse radioResponse) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public void setupDataCallSlice(int serial, int accessNetwork, DataProfileInfo dataProfileInfo, boolean roamingAllowed, int reason, LinkAddress[] addresses, String[] dnses, int pduSessionId, SliceInfo sliceInfo, boolean matchAllRuleAllowed, TrafficDescriptor trafficDescriptor, String verifyDescriptor, int responseMode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExData {
        static final int TRANSACTION_dataConnectionAttach = 1;
        static final int TRANSACTION_dataConnectionDetach = 2;
        static final int TRANSACTION_enableDsdaIndication = 3;
        static final int TRANSACTION_getDsdaStatus = 4;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_resetAllConnections = 5;
        static final int TRANSACTION_resetMdDataRetryCount = 6;
        static final int TRANSACTION_responseAcknowledgementMtk = 9;
        static final int TRANSACTION_setFdMode = 7;
        static final int TRANSACTION_setResponseFunctionsAssist = 11;
        static final int TRANSACTION_setResponseFunctionsMtk = 10;
        static final int TRANSACTION_setupDataCallSlice = 12;
        static final int TRANSACTION_syncDataSettingsToMd = 8;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExData asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExData)) {
                return (IMtkRadioExData) iin;
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
                        dataConnectionAttach(_arg0, _arg1, _arg2);
                        return true;
                    case 2:
                        int _arg3 = data.readInt();
                        int _arg4 = data.readInt();
                        int _arg5 = data.readInt();
                        data.enforceNoDataAvail();
                        dataConnectionDetach(_arg3, _arg4, _arg5);
                        return true;
                    case 3:
                        int _arg6 = data.readInt();
                        boolean _arg7 = data.readBoolean();
                        int _arg8 = data.readInt();
                        data.enforceNoDataAvail();
                        enableDsdaIndication(_arg6, _arg7, _arg8);
                        return true;
                    case 4:
                        int _arg9 = data.readInt();
                        int _arg10 = data.readInt();
                        data.enforceNoDataAvail();
                        getDsdaStatus(_arg9, _arg10);
                        return true;
                    case 5:
                        int _arg11 = data.readInt();
                        int _arg12 = data.readInt();
                        data.enforceNoDataAvail();
                        resetAllConnections(_arg11, _arg12);
                        return true;
                    case 6:
                        int _arg13 = data.readInt();
                        String _arg14 = data.readString();
                        int _arg15 = data.readInt();
                        data.enforceNoDataAvail();
                        resetMdDataRetryCount(_arg13, _arg14, _arg15);
                        return true;
                    case 7:
                        int _arg16 = data.readInt();
                        int _arg17 = data.readInt();
                        int _arg18 = data.readInt();
                        int _arg19 = data.readInt();
                        int _arg20 = data.readInt();
                        data.enforceNoDataAvail();
                        setFdMode(_arg16, _arg17, _arg18, _arg19, _arg20);
                        return true;
                    case 8:
                        int _arg21 = data.readInt();
                        int[] _arg22 = data.createIntArray();
                        int _arg23 = data.readInt();
                        data.enforceNoDataAvail();
                        syncDataSettingsToMd(_arg21, _arg22, _arg23);
                        return true;
                    case 9:
                        responseAcknowledgementMtk();
                        return true;
                    case 10:
                        IMtkRadioExDataResponse _arg24 = IMtkRadioExDataResponse.Stub.asInterface(data.readStrongBinder());
                        IMtkRadioExDataIndication _arg25 = IMtkRadioExDataIndication.Stub.asInterface(data.readStrongBinder());
                        data.enforceNoDataAvail();
                        setResponseFunctionsMtk(_arg24, _arg25);
                        return true;
                    case 11:
                        IAssistRadioResponse _arg26 = IAssistRadioResponse.Stub.asInterface(data.readStrongBinder());
                        data.enforceNoDataAvail();
                        setResponseFunctionsAssist(_arg26);
                        return true;
                    case 12:
                        int _arg27 = data.readInt();
                        int _arg28 = data.readInt();
                        DataProfileInfo _arg29 = (DataProfileInfo) data.readTypedObject(DataProfileInfo.CREATOR);
                        boolean _arg30 = data.readBoolean();
                        int _arg31 = data.readInt();
                        LinkAddress[] _arg32 = (LinkAddress[]) data.createTypedArray(LinkAddress.CREATOR);
                        String[] _arg33 = data.createStringArray();
                        int _arg34 = data.readInt();
                        SliceInfo _arg35 = (SliceInfo) data.readTypedObject(SliceInfo.CREATOR);
                        boolean _arg36 = data.readBoolean();
                        TrafficDescriptor _arg110 = (TrafficDescriptor) data.readTypedObject(TrafficDescriptor.CREATOR);
                        String _arg111 = data.readString();
                        int _arg112 = data.readInt();
                        int _arg113 = data.readInt();
                        data.enforceNoDataAvail();
                        setupDataCallSlice(_arg27, _arg28, _arg29, _arg30, _arg31, _arg32, _arg33, _arg34, _arg35, _arg36, _arg110, _arg111, _arg112, _arg113);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeNoException();
            reply.writeInt(getInterfaceVersion());
            return true;
        }

        private static final class Proxy implements IMtkRadioExData {
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void dataConnectionAttach(int serial, int type, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(type);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void dataConnectionDetach(int serial, int type, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(type);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void enableDsdaIndication(int serial, boolean enable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(enable);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void getDsdaStatus(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void resetAllConnections(int serial, int clientId) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void resetMdDataRetryCount(int serial, String apn, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(apn);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void setFdMode(int serial, int mode, int param1, int param2, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(param1);
                    _data.writeInt(param2);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void syncDataSettingsToMd(int serial, int[] settings, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(settings);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void responseAcknowledgementMtk() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void setResponseFunctionsMtk(IMtkRadioExDataResponse radioResponse, IMtkRadioExDataIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void setResponseFunctionsAssist(IAssistRadioResponse radioResponse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
            public void setupDataCallSlice(int serial, int accessNetwork, DataProfileInfo dataProfileInfo, boolean roamingAllowed, int reason, LinkAddress[] addresses, String[] dnses, int pduSessionId, SliceInfo sliceInfo, boolean matchAllRuleAllowed, TrafficDescriptor trafficDescriptor, String verifyDescriptor, int responseMode, int clientId) throws Throwable {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(accessNetwork);
                    _data.writeTypedObject(dataProfileInfo, 0);
                    try {
                        _data.writeBoolean(roamingAllowed);
                        try {
                            _data.writeInt(reason);
                            try {
                                _data.writeTypedArray(addresses, 0);
                                try {
                                    _data.writeStringArray(dnses);
                                    try {
                                        _data.writeInt(pduSessionId);
                                        try {
                                            _data.writeTypedObject(sliceInfo, 0);
                                            try {
                                                _data.writeBoolean(matchAllRuleAllowed);
                                                try {
                                                    _data.writeTypedObject(trafficDescriptor, 0);
                                                    try {
                                                        _data.writeString(verifyDescriptor);
                                                        try {
                                                            _data.writeInt(responseMode);
                                                            try {
                                                                _data.writeInt(clientId);
                                                                boolean _status = this.mRemote.transact(12, _data, null, 1);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData
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
