package com.xiaomi.mirror;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMirrorAdvConnectionCallback extends IInterface {
    public static final String DESCRIPTOR = "com.xiaomi.mirror.IMirrorAdvConnectionCallback";

    public class Default implements IMirrorAdvConnectionCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.xiaomi.mirror.IMirrorAdvConnectionCallback
        public void onLost() throws RemoteException {
        }
    }

    public abstract class Stub extends Binder implements IMirrorAdvConnectionCallback {
        static final int TRANSACTION_onLost = 1;

        class Proxy implements IMirrorAdvConnectionCallback {
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IMirrorAdvConnectionCallback.DESCRIPTOR;
            }

            @Override // com.xiaomi.mirror.IMirrorAdvConnectionCallback
            public void onLost() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMirrorAdvConnectionCallback.DESCRIPTOR);
                    this.mRemote.transact(1, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, IMirrorAdvConnectionCallback.DESCRIPTOR);
        }

        public static IMirrorAdvConnectionCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(IMirrorAdvConnectionCallback.DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof IMirrorAdvConnectionCallback)) ? new Proxy(iBinder) : (IMirrorAdvConnectionCallback) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IMirrorAdvConnectionCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IMirrorAdvConnectionCallback.DESCRIPTOR);
                return true;
            }
            if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            onLost();
            return true;
        }
    }

    void onLost() throws RemoteException;
}
