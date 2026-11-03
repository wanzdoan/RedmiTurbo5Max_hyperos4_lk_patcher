package com.xiaomi.mirror;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMediaRelayCallback extends IInterface {
    public static final String DESCRIPTOR = "com.xiaomi.mirror.IMediaRelayCallback";

    public class Default implements IMediaRelayCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.xiaomi.mirror.IMediaRelayCallback
        public void onVolumeChanged(String str, int i) throws RemoteException {
        }
    }

    public abstract class Stub extends Binder implements IMediaRelayCallback {
        static final int TRANSACTION_onVolumeChanged = 1;

        class Proxy implements IMediaRelayCallback {
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IMediaRelayCallback.DESCRIPTOR;
            }

            @Override // com.xiaomi.mirror.IMediaRelayCallback
            public void onVolumeChanged(String str, int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IMediaRelayCallback.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(1, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, IMediaRelayCallback.DESCRIPTOR);
        }

        public static IMediaRelayCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(IMediaRelayCallback.DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof IMediaRelayCallback)) ? new Proxy(iBinder) : (IMediaRelayCallback) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IMediaRelayCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IMediaRelayCallback.DESCRIPTOR);
                return true;
            }
            if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            onVolumeChanged(parcel.readString(), parcel.readInt());
            return true;
        }
    }

    void onVolumeChanged(String str, int i) throws RemoteException;
}
