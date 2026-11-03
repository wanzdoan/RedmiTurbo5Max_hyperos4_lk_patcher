package vendor.mediatek.hardware.mtkradioex.data;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExDataResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$data$IMtkRadioExDataResponse".replace('$', '.');
    public static final String HASH = "0f9b0198bf345f123790d9c279bde6bd25a2a227";
    public static final int VERSION = 3;

    void dataConnectionAttachResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void dataConnectionDetachResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void enableDsdaIndicationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void getDsdaStatusResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void resetAllConnectionsResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void resetMdDataRetryCountResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setupDataCallResponseSlice(RadioResponseInfo radioResponseInfo, SetupDataCallResultSlice setupDataCallResultSlice) throws RemoteException;

    public static class Default implements IMtkRadioExDataResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void dataConnectionAttachResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void dataConnectionDetachResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void enableDsdaIndicationResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void getDsdaStatusResponse(RadioResponseInfo info, int mode) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void resetAllConnectionsResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void resetMdDataRetryCountResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void setupDataCallResponseSlice(RadioResponseInfo info, SetupDataCallResultSlice dcResponse) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExDataResponse {
        static final int TRANSACTION_dataConnectionAttachResponse = 1;
        static final int TRANSACTION_dataConnectionDetachResponse = 2;
        static final int TRANSACTION_enableDsdaIndicationResponse = 3;
        static final int TRANSACTION_getDsdaStatusResponse = 4;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_resetAllConnectionsResponse = 5;
        static final int TRANSACTION_resetMdDataRetryCountResponse = 6;
        static final int TRANSACTION_setupDataCallResponseSlice = 7;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExDataResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExDataResponse)) {
                return (IMtkRadioExDataResponse) iin;
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
                    dataConnectionAttachResponse(_arg0);
                    return true;
                case 2:
                    RadioResponseInfo _arg1 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    dataConnectionDetachResponse(_arg1);
                    return true;
                case 3:
                    RadioResponseInfo _arg2 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    enableDsdaIndicationResponse(_arg2);
                    return true;
                case 4:
                    RadioResponseInfo _arg3 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    int _arg4 = data.readInt();
                    data.enforceNoDataAvail();
                    getDsdaStatusResponse(_arg3, _arg4);
                    return true;
                case 5:
                    RadioResponseInfo _arg5 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    resetAllConnectionsResponse(_arg5);
                    return true;
                case 6:
                    RadioResponseInfo _arg6 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    resetMdDataRetryCountResponse(_arg6);
                    return true;
                case 7:
                    RadioResponseInfo _arg7 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    SetupDataCallResultSlice _arg8 = (SetupDataCallResultSlice) data.readTypedObject(SetupDataCallResultSlice.CREATOR);
                    data.enforceNoDataAvail();
                    setupDataCallResponseSlice(_arg7, _arg8);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExDataResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void dataConnectionAttachResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void dataConnectionDetachResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void enableDsdaIndicationResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void getDsdaStatusResponse(RadioResponseInfo info, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(mode);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void resetAllConnectionsResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void resetMdDataRetryCountResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void setupDataCallResponseSlice(RadioResponseInfo info, SetupDataCallResultSlice dcResponse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(dcResponse, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
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
