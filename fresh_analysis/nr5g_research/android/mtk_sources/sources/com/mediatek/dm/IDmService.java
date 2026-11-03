package com.mediatek.dm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IDmService extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.dm.IDmService";

    int getDmSupported() throws RemoteException;

    boolean getImcProvision(int i, int i2) throws RemoteException;

    boolean setImcProvision(int i, int i2, int i3) throws RemoteException;

    public static class Default implements IDmService {
        @Override // com.mediatek.dm.IDmService
        public int getDmSupported() throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.dm.IDmService
        public boolean getImcProvision(int phoneId, int feature) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.dm.IDmService
        public boolean setImcProvision(int phoneId, int feature, int pvs_en) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDmService {
        static final int TRANSACTION_getDmSupported = 1;
        static final int TRANSACTION_getImcProvision = 2;
        static final int TRANSACTION_setImcProvision = 3;

        public Stub() {
            attachInterface(this, IDmService.DESCRIPTOR);
        }

        public static IDmService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IDmService.DESCRIPTOR);
            if (iin != null && (iin instanceof IDmService)) {
                return (IDmService) iin;
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
                data.enforceInterface(IDmService.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _result = getDmSupported();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result2 = getImcProvision(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeBoolean(_result2);
                    return true;
                case 3:
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    int _arg4 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result3 = setImcProvision(_arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeBoolean(_result3);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IDmService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IDmService.DESCRIPTOR;
            }

            @Override // com.mediatek.dm.IDmService
            public int getDmSupported() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IDmService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.dm.IDmService
            public boolean getImcProvision(int phoneId, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IDmService.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(feature);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.dm.IDmService
            public boolean setImcProvision(int phoneId, int feature, int pvs_en) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IDmService.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(feature);
                    _data.writeInt(pvs_en);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
