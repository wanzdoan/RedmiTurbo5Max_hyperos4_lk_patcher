package com.xiaomi.mirror;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMirrorDeviceStatusCallback extends IInterface {
    public static final String DESCRIPTOR = "com.xiaomi.mirror.IMirrorDeviceStatusCallback";

    public class Default implements IMirrorDeviceStatusCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.xiaomi.mirror.IMirrorDeviceStatusCallback
        public void onRemoteDeviceStatus(int i, boolean z) throws RemoteException {
        }
    }

    public abstract class Stub extends Binder implements IMirrorDeviceStatusCallback {
        static final int TRANSACTION_onRemoteDeviceStatus = 1;

        class Proxy implements IMirrorDeviceStatusCallback {
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IMirrorDeviceStatusCallback.DESCRIPTOR;
            }

            @Override // com.xiaomi.mirror.IMirrorDeviceStatusCallback
            public void onRemoteDeviceStatus(int i, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMirrorDeviceStatusCallback.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(1, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, IMirrorDeviceStatusCallback.DESCRIPTOR);
        }

        public static IMirrorDeviceStatusCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(IMirrorDeviceStatusCallback.DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof IMirrorDeviceStatusCallback)) ? new Proxy(iBinder) : (IMirrorDeviceStatusCallback) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IMirrorDeviceStatusCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IMirrorDeviceStatusCallback.DESCRIPTOR);
                return true;
            }
            if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            onRemoteDeviceStatus(parcel.readInt(), parcel.readInt() != 0);
            return true;
        }
    }

    void onRemoteDeviceStatus(int i, boolean z) throws RemoteException;
}
