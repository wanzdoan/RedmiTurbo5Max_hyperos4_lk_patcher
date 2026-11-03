package com.xiaomi.internal.telephony;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IQosStateCallback extends IInterface {
    public static final String DESCRIPTOR = "com.xiaomi.internal.telephony.IQosStateCallback";

    void onQosStateChange(int i, Bundle bundle) throws RemoteException;

    public static class Default implements IQosStateCallback {
        @Override // com.xiaomi.internal.telephony.IQosStateCallback
        public void onQosStateChange(int slotId, Bundle dataBundle) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IQosStateCallback {
        static final int TRANSACTION_onQosStateChange = 1;

        public Stub() {
            attachInterface(this, IQosStateCallback.DESCRIPTOR);
        }

        public static IQosStateCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IQosStateCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IQosStateCallback)) {
                return (IQosStateCallback) iin;
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
                data.enforceInterface(IQosStateCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    Bundle _arg1 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                    data.enforceNoDataAvail();
                    onQosStateChange(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IQosStateCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IQosStateCallback.DESCRIPTOR;
            }

            @Override // com.xiaomi.internal.telephony.IQosStateCallback
            public void onQosStateChange(int slotId, Bundle dataBundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IQosStateCallback.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeTypedObject(dataBundle, 0);
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
