package com.xiaomi.mirror;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface ICallRelayListener extends IInterface {
    public static final String DESCRIPTOR = "com.xiaomi.mirror.ICallRelayListener";

    public class Default implements ICallRelayListener {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.xiaomi.mirror.ICallRelayListener
        public void onAddressUpdate(String str) throws RemoteException {
        }

        @Override // com.xiaomi.mirror.ICallRelayListener
        public void onMessage(String str) throws RemoteException {
        }
    }

    public abstract class Stub extends Binder implements ICallRelayListener {
        static final int TRANSACTION_onAddressUpdate = 2;
        static final int TRANSACTION_onMessage = 1;

        class Proxy implements ICallRelayListener {
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICallRelayListener.DESCRIPTOR;
            }

            @Override // com.xiaomi.mirror.ICallRelayListener
            public void onAddressUpdate(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(ICallRelayListener.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(2, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.mirror.ICallRelayListener
            public void onMessage(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(ICallRelayListener.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(1, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, ICallRelayListener.DESCRIPTOR);
        }

        public static ICallRelayListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(ICallRelayListener.DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof ICallRelayListener)) ? new Proxy(iBinder) : (ICallRelayListener) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ICallRelayListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(ICallRelayListener.DESCRIPTOR);
                return true;
            }
            if (i == 1) {
                onMessage(parcel.readString());
            } else {
                if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                onAddressUpdate(parcel.readString());
            }
            return true;
        }
    }

    void onAddressUpdate(String str) throws RemoteException;

    void onMessage(String str) throws RemoteException;
}
