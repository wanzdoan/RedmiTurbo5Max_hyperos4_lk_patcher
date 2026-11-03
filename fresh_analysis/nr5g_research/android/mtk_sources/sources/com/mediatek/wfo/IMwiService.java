package com.mediatek.wfo;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMwiService extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.wfo.IMwiService";

    IWifiOffloadService getWfcHandlerInterface() throws RemoteException;

    int getWfcState(int i) throws RemoteException;

    void requestSmsLocation(int i, String str) throws RemoteException;

    public static class Default implements IMwiService {
        @Override // com.mediatek.wfo.IMwiService
        public IWifiOffloadService getWfcHandlerInterface() throws RemoteException {
            return null;
        }

        @Override // com.mediatek.wfo.IMwiService
        public int getWfcState(int simIdx) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.wfo.IMwiService
        public void requestSmsLocation(int phoneId, String destAddress) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMwiService {
        static final int TRANSACTION_getWfcHandlerInterface = 1;
        static final int TRANSACTION_getWfcState = 2;
        static final int TRANSACTION_requestSmsLocation = 3;

        public Stub() {
            attachInterface(this, IMwiService.DESCRIPTOR);
        }

        public static IMwiService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IMwiService.DESCRIPTOR);
            if (iin != null && (iin instanceof IMwiService)) {
                return (IMwiService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IMwiService.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    IWifiOffloadService _result = getWfcHandlerInterface();
                    reply.writeNoException();
                    reply.writeStrongInterface(_result);
                    return true;
                case 2:
                    int _arg0 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result2 = getWfcState(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    int _arg1 = data.readInt();
                    String _arg2 = data.readString();
                    data.enforceNoDataAvail();
                    requestSmsLocation(_arg1, _arg2);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMwiService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IMwiService.DESCRIPTOR;
            }

            @Override // com.mediatek.wfo.IMwiService
            public IWifiOffloadService getWfcHandlerInterface() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMwiService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    IWifiOffloadService _result = IWifiOffloadService.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IMwiService
            public int getWfcState(int simIdx) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMwiService.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IMwiService
            public void requestSmsLocation(int phoneId, String destAddress) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMwiService.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(destAddress);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
