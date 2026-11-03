package com.xiaomi.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IHstStateCallback extends IInterface {
    public static final String DESCRIPTOR = "com.xiaomi.internal.telephony.IHstStateCallback";

    void onHstStateChange(int i, boolean z, int i2) throws RemoteException;

    public static class Default implements IHstStateCallback {
        @Override // com.xiaomi.internal.telephony.IHstStateCallback
        public void onHstStateChange(int slotId, boolean hstState, int hstRat) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IHstStateCallback {
        static final int TRANSACTION_onHstStateChange = 1;

        public Stub() {
            attachInterface(this, IHstStateCallback.DESCRIPTOR);
        }

        public static IHstStateCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IHstStateCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IHstStateCallback)) {
                return (IHstStateCallback) iin;
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
                data.enforceInterface(IHstStateCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    boolean _arg1 = data.readBoolean();
                    int _arg2 = data.readInt();
                    data.enforceNoDataAvail();
                    onHstStateChange(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IHstStateCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IHstStateCallback.DESCRIPTOR;
            }

            @Override // com.xiaomi.internal.telephony.IHstStateCallback
            public void onHstStateChange(int slotId, boolean hstState, int hstRat) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHstStateCallback.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeBoolean(hstState);
                    _data.writeInt(hstRat);
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
