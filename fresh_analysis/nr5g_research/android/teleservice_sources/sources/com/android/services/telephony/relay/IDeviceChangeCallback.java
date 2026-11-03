package com.android.services.telephony.relay;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IDeviceChangeCallback extends IInterface {
    public static final String DESCRIPTOR = "com.android.services.telephony.relay.IDeviceChangeCallback";

    public class Default implements IDeviceChangeCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.services.telephony.relay.IDeviceChangeCallback
        public void onDeviceChange() throws RemoteException {
        }
    }

    void onDeviceChange() throws RemoteException;

    public abstract class Stub extends Binder implements IDeviceChangeCallback {
        static final int TRANSACTION_onDeviceChange = 1;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IDeviceChangeCallback.DESCRIPTOR);
        }

        public static IDeviceChangeCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(IDeviceChangeCallback.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IDeviceChangeCallback)) {
                return (IDeviceChangeCallback) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IDeviceChangeCallback.DESCRIPTOR);
            }
            if (i == 1) {
                onDeviceChange();
                parcel2.writeNoException();
                return true;
            }
            return super.onTransact(i, parcel, parcel2, i2);
        }

        final class Proxy implements IDeviceChangeCallback {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IDeviceChangeCallback.DESCRIPTOR;
            }

            @Override // com.android.services.telephony.relay.IDeviceChangeCallback
            public void onDeviceChange() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IDeviceChangeCallback.DESCRIPTOR);
                    this.mRemote.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
