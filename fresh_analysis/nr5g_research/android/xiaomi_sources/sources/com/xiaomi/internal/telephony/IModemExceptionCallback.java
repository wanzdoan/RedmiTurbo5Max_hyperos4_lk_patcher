package com.xiaomi.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IModemExceptionCallback extends IInterface {
    public static final String DESCRIPTOR = "com.xiaomi.internal.telephony.IModemExceptionCallback";

    void onModemExceptionChanged(int i, int[] iArr) throws RemoteException;

    public static class Default implements IModemExceptionCallback {
        @Override // com.xiaomi.internal.telephony.IModemExceptionCallback
        public void onModemExceptionChanged(int slotId, int[] modemException) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IModemExceptionCallback {
        static final int TRANSACTION_onModemExceptionChanged = 1;

        public Stub() {
            attachInterface(this, IModemExceptionCallback.DESCRIPTOR);
        }

        public static IModemExceptionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IModemExceptionCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IModemExceptionCallback)) {
                return (IModemExceptionCallback) iin;
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
                data.enforceInterface(IModemExceptionCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    int[] _arg1 = data.createIntArray();
                    data.enforceNoDataAvail();
                    onModemExceptionChanged(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IModemExceptionCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IModemExceptionCallback.DESCRIPTOR;
            }

            @Override // com.xiaomi.internal.telephony.IModemExceptionCallback
            public void onModemExceptionChanged(int slotId, int[] modemException) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IModemExceptionCallback.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeIntArray(modemException);
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
