package com.xiaomi.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface ICommMsgCallback extends IInterface {
    public static final String DESCRIPTOR = "com.xiaomi.internal.telephony.ICommMsgCallback";

    void onUnsolCommMsg(int i, int i2, byte[] bArr) throws RemoteException;

    public static class Default implements ICommMsgCallback {
        @Override // com.xiaomi.internal.telephony.ICommMsgCallback
        public void onUnsolCommMsg(int phoneId, int msgId, byte[] data) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ICommMsgCallback {
        static final int TRANSACTION_onUnsolCommMsg = 1;

        public Stub() {
            attachInterface(this, ICommMsgCallback.DESCRIPTOR);
        }

        public static ICommMsgCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICommMsgCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof ICommMsgCallback)) {
                return (ICommMsgCallback) iin;
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
                data.enforceInterface(ICommMsgCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    byte[] _arg2 = data.createByteArray();
                    data.enforceNoDataAvail();
                    onUnsolCommMsg(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements ICommMsgCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return ICommMsgCallback.DESCRIPTOR;
            }

            @Override // com.xiaomi.internal.telephony.ICommMsgCallback
            public void onUnsolCommMsg(int phoneId, int msgId, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICommMsgCallback.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(msgId);
                    _data.writeByteArray(data);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
