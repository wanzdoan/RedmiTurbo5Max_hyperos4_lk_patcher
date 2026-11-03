package com.android.services.telephony.relay;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ISynergy extends IInterface {
    public static final String DESCRIPTOR = "com.android.services.telephony.relay.ISynergy";

    public class Default implements ISynergy {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.services.telephony.relay.ISynergy
        public List getSynergyDeviceList() throws RemoteException {
            return null;
        }

        @Override // com.android.services.telephony.relay.ISynergy
        public void registerSynergyDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
        }

        @Override // com.android.services.telephony.relay.ISynergy
        public void unregisterSynergyDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
        }
    }

    List getSynergyDeviceList() throws RemoteException;

    void registerSynergyDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException;

    void unregisterSynergyDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException;

    public abstract class Stub extends Binder implements ISynergy {
        static final int TRANSACTION_getSynergyDeviceList = 1;
        static final int TRANSACTION_registerSynergyDeviceChangeCallback = 2;
        static final int TRANSACTION_unregisterSynergyDeviceChangeCallback = 3;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ISynergy.DESCRIPTOR);
        }

        public static ISynergy asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(ISynergy.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof ISynergy)) {
                return (ISynergy) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ISynergy.DESCRIPTOR);
            }
            if (i == 1) {
                List<String> synergyDeviceList = getSynergyDeviceList();
                parcel2.writeNoException();
                parcel2.writeStringList(synergyDeviceList);
            } else if (i == 2) {
                IDeviceChangeCallback iDeviceChangeCallbackAsInterface = IDeviceChangeCallback.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                registerSynergyDeviceChangeCallback(iDeviceChangeCallbackAsInterface);
                parcel2.writeNoException();
            } else if (i == 3) {
                IDeviceChangeCallback iDeviceChangeCallbackAsInterface2 = IDeviceChangeCallback.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                unregisterSynergyDeviceChangeCallback(iDeviceChangeCallbackAsInterface2);
                parcel2.writeNoException();
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            return true;
        }

        final class Proxy implements ISynergy {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return ISynergy.DESCRIPTOR;
            }

            @Override // com.android.services.telephony.relay.ISynergy
            public List getSynergyDeviceList() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(ISynergy.DESCRIPTOR);
                    this.mRemote.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.createStringArrayList();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.services.telephony.relay.ISynergy
            public void registerSynergyDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(ISynergy.DESCRIPTOR);
                    parcelObtain.writeStrongInterface(iDeviceChangeCallback);
                    this.mRemote.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.services.telephony.relay.ISynergy
            public void unregisterSynergyDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(ISynergy.DESCRIPTOR);
                    parcelObtain.writeStrongInterface(iDeviceChangeCallback);
                    this.mRemote.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
