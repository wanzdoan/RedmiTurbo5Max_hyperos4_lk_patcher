package vendor.mediatek.hardware.mtkradioex.messaging;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExMessaging extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$messaging$IMtkRadioExMessaging".replace('$', '.');
    public static final String HASH = "93a2d5af4d82e246ed35f8afe17083da08ba1147";
    public static final int VERSION = 1;

    void exitSCBM(int i, int i2) throws RemoteException;

    void getGsmBroadcastActivation(int i, int i2) throws RemoteException;

    void getGsmBroadcastLangs(int i, int i2) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getSmsMemStatus(int i, int i2) throws RemoteException;

    void getSmsParameters(int i, int i2) throws RemoteException;

    void getSmsRuimMemoryStatus(int i, int i2) throws RemoteException;

    void removeCbMsg(int i, int i2, int i3, int i4) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void setEtws(int i, int i2, int i3) throws RemoteException;

    void setGsmBroadcastLangs(int i, String str, int i2) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExMessagingResponse iMtkRadioExMessagingResponse, IMtkRadioExMessagingIndication iMtkRadioExMessagingIndication) throws RemoteException;

    void setSmsParameters(int i, SmsParams smsParams, int i2) throws RemoteException;

    public static class Default implements IMtkRadioExMessaging {
        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void getGsmBroadcastActivation(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void getGsmBroadcastLangs(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void getSmsMemStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void getSmsParameters(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void getSmsRuimMemoryStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void removeCbMsg(int serial, int channelId, int serialId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void setEtws(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void setGsmBroadcastLangs(int serial, String langs, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void setSmsParameters(int serial, SmsParams message, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void exitSCBM(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public void setResponseFunctionsMtk(IMtkRadioExMessagingResponse radioResponse, IMtkRadioExMessagingIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExMessaging {
        static final int TRANSACTION_exitSCBM = 10;
        static final int TRANSACTION_getGsmBroadcastActivation = 1;
        static final int TRANSACTION_getGsmBroadcastLangs = 2;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getSmsMemStatus = 3;
        static final int TRANSACTION_getSmsParameters = 4;
        static final int TRANSACTION_getSmsRuimMemoryStatus = 5;
        static final int TRANSACTION_removeCbMsg = 6;
        static final int TRANSACTION_responseAcknowledgementMtk = 11;
        static final int TRANSACTION_setEtws = 7;
        static final int TRANSACTION_setGsmBroadcastLangs = 8;
        static final int TRANSACTION_setResponseFunctionsMtk = 12;
        static final int TRANSACTION_setSmsParameters = 9;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExMessaging asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExMessaging)) {
                return (IMtkRadioExMessaging) iin;
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
                    getGsmBroadcastActivation(_arg0, _arg1);
                    return true;
                case 2:
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    data.enforceNoDataAvail();
                    getGsmBroadcastLangs(_arg2, _arg3);
                    return true;
                case 3:
                    int _arg4 = data.readInt();
                    int _arg5 = data.readInt();
                    data.enforceNoDataAvail();
                    getSmsMemStatus(_arg4, _arg5);
                    return true;
                case 4:
                    int _arg6 = data.readInt();
                    int _arg7 = data.readInt();
                    data.enforceNoDataAvail();
                    getSmsParameters(_arg6, _arg7);
                    return true;
                case 5:
                    int _arg8 = data.readInt();
                    int _arg9 = data.readInt();
                    data.enforceNoDataAvail();
                    getSmsRuimMemoryStatus(_arg8, _arg9);
                    return true;
                case 6:
                    int _arg10 = data.readInt();
                    int _arg11 = data.readInt();
                    int _arg12 = data.readInt();
                    int _arg13 = data.readInt();
                    data.enforceNoDataAvail();
                    removeCbMsg(_arg10, _arg11, _arg12, _arg13);
                    return true;
                case 7:
                    int _arg14 = data.readInt();
                    int _arg15 = data.readInt();
                    int _arg16 = data.readInt();
                    data.enforceNoDataAvail();
                    setEtws(_arg14, _arg15, _arg16);
                    return true;
                case 8:
                    int _arg17 = data.readInt();
                    String _arg18 = data.readString();
                    int _arg19 = data.readInt();
                    data.enforceNoDataAvail();
                    setGsmBroadcastLangs(_arg17, _arg18, _arg19);
                    return true;
                case 9:
                    int _arg20 = data.readInt();
                    SmsParams _arg21 = (SmsParams) data.readTypedObject(SmsParams.CREATOR);
                    int _arg22 = data.readInt();
                    data.enforceNoDataAvail();
                    setSmsParameters(_arg20, _arg21, _arg22);
                    return true;
                case 10:
                    int _arg23 = data.readInt();
                    int _arg24 = data.readInt();
                    data.enforceNoDataAvail();
                    exitSCBM(_arg23, _arg24);
                    return true;
                case 11:
                    responseAcknowledgementMtk();
                    return true;
                case 12:
                    IMtkRadioExMessagingResponse _arg25 = IMtkRadioExMessagingResponse.Stub.asInterface(data.readStrongBinder());
                    IMtkRadioExMessagingIndication _arg26 = IMtkRadioExMessagingIndication.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setResponseFunctionsMtk(_arg25, _arg26);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkRadioExMessaging {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void getGsmBroadcastActivation(int serial, int clientId) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void getGsmBroadcastLangs(int serial, int clientId) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void getSmsMemStatus(int serial, int clientId) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void getSmsParameters(int serial, int clientId) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void getSmsRuimMemoryStatus(int serial, int clientId) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void removeCbMsg(int serial, int channelId, int serialId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(channelId);
                    _data.writeInt(serialId);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void setEtws(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void setGsmBroadcastLangs(int serial, String langs, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(langs);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void setSmsParameters(int serial, SmsParams message, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(message, 0);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void exitSCBM(int serial, int clientId) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void responseAcknowledgementMtk() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
            public void setResponseFunctionsMtk(IMtkRadioExMessagingResponse radioResponse, IMtkRadioExMessagingIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging
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
