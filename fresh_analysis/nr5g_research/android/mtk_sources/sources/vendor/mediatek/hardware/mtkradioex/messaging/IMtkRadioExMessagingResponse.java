package vendor.mediatek.hardware.mtkradioex.messaging;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExMessagingResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$messaging$IMtkRadioExMessagingResponse".replace('$', '.');
    public static final String HASH = "93a2d5af4d82e246ed35f8afe17083da08ba1147";
    public static final int VERSION = 1;

    void exitSCBMResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void getGsmBroadcastActivationRsp(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getGsmBroadcastLangsResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getSmsMemStatusResponse(RadioResponseInfo radioResponseInfo, SmsMemStatus smsMemStatus) throws RemoteException;

    void getSmsParametersResponse(RadioResponseInfo radioResponseInfo, SmsParams smsParams) throws RemoteException;

    void getSmsRuimMemoryStatusResponse(RadioResponseInfo radioResponseInfo, SmsMemStatus smsMemStatus) throws RemoteException;

    void removeCbMsgResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setEtwsResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setGsmBroadcastLangsResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSmsParametersResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    public static class Default implements IMtkRadioExMessagingResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public void getGsmBroadcastActivationRsp(RadioResponseInfo info, int active) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public void getGsmBroadcastLangsResponse(RadioResponseInfo info, String langs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public void getSmsMemStatusResponse(RadioResponseInfo info, SmsMemStatus status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public void getSmsParametersResponse(RadioResponseInfo info, SmsParams param) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public void getSmsRuimMemoryStatusResponse(RadioResponseInfo info, SmsMemStatus memStatus) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public void removeCbMsgResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public void setEtwsResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public void setGsmBroadcastLangsResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public void setSmsParametersResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public void exitSCBMResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExMessagingResponse {
        static final int TRANSACTION_exitSCBMResponse = 10;
        static final int TRANSACTION_getGsmBroadcastActivationRsp = 1;
        static final int TRANSACTION_getGsmBroadcastLangsResponse = 2;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getSmsMemStatusResponse = 3;
        static final int TRANSACTION_getSmsParametersResponse = 4;
        static final int TRANSACTION_getSmsRuimMemoryStatusResponse = 5;
        static final int TRANSACTION_removeCbMsgResponse = 6;
        static final int TRANSACTION_setEtwsResponse = 7;
        static final int TRANSACTION_setGsmBroadcastLangsResponse = 8;
        static final int TRANSACTION_setSmsParametersResponse = 9;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExMessagingResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExMessagingResponse)) {
                return (IMtkRadioExMessagingResponse) iin;
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
                    int _arg1 = data.readInt();
                    data.enforceNoDataAvail();
                    getGsmBroadcastActivationRsp(_arg0, _arg1);
                    return true;
                case 2:
                    RadioResponseInfo _arg2 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    String _arg3 = data.readString();
                    data.enforceNoDataAvail();
                    getGsmBroadcastLangsResponse(_arg2, _arg3);
                    return true;
                case 3:
                    RadioResponseInfo _arg4 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    SmsMemStatus _arg5 = (SmsMemStatus) data.readTypedObject(SmsMemStatus.CREATOR);
                    data.enforceNoDataAvail();
                    getSmsMemStatusResponse(_arg4, _arg5);
                    return true;
                case 4:
                    RadioResponseInfo _arg6 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    SmsParams _arg7 = (SmsParams) data.readTypedObject(SmsParams.CREATOR);
                    data.enforceNoDataAvail();
                    getSmsParametersResponse(_arg6, _arg7);
                    return true;
                case 5:
                    RadioResponseInfo _arg8 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    SmsMemStatus _arg9 = (SmsMemStatus) data.readTypedObject(SmsMemStatus.CREATOR);
                    data.enforceNoDataAvail();
                    getSmsRuimMemoryStatusResponse(_arg8, _arg9);
                    return true;
                case 6:
                    RadioResponseInfo _arg10 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    removeCbMsgResponse(_arg10);
                    return true;
                case 7:
                    RadioResponseInfo _arg11 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setEtwsResponse(_arg11);
                    return true;
                case 8:
                    RadioResponseInfo _arg12 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setGsmBroadcastLangsResponse(_arg12);
                    return true;
                case 9:
                    RadioResponseInfo _arg13 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setSmsParametersResponse(_arg13);
                    return true;
                case 10:
                    RadioResponseInfo _arg14 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                    data.enforceNoDataAvail();
                    exitSCBMResponse(_arg14);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExMessagingResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
            public void getGsmBroadcastActivationRsp(RadioResponseInfo info, int active) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(active);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
            public void getGsmBroadcastLangsResponse(RadioResponseInfo info, String langs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(langs);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
            public void getSmsMemStatusResponse(RadioResponseInfo info, SmsMemStatus status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(status, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
            public void getSmsParametersResponse(RadioResponseInfo info, SmsParams param) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(param, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
            public void getSmsRuimMemoryStatusResponse(RadioResponseInfo info, SmsMemStatus memStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(memStatus, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
            public void removeCbMsgResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
            public void setEtwsResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
            public void setGsmBroadcastLangsResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
            public void setSmsParametersResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
            public void exitSCBMResponse(RadioResponseInfo info) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
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
