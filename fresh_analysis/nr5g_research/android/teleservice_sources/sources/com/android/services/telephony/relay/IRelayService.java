package com.android.services.telephony.relay;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IRelayService extends IInterface {
    public static final String DESCRIPTOR = "com.android.services.telephony.relay.IRelayService";

    public class Default implements IRelayService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.services.telephony.relay.IRelayService
        public List getRelayDeviceIds() throws RemoteException {
            return null;
        }

        @Override // com.android.services.telephony.relay.IRelayService
        public void registerRelayDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
        }

        @Override // com.android.services.telephony.relay.IRelayService
        public void unregisterRelayDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
        }
    }

    List getRelayDeviceIds() throws RemoteException;

    void registerRelayDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException;

    void unregisterRelayDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException;

    public abstract class Stub extends Binder implements IRelayService {
        static final int TRANSACTION_getRelayDeviceIds = 1;
        static final int TRANSACTION_registerRelayDeviceChangeCallback = 2;
        static final int TRANSACTION_unregisterRelayDeviceChangeCallback = 3;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IRelayService.DESCRIPTOR);
        }

        public static IRelayService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(IRelayService.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IRelayService)) {
                return (IRelayService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IRelayService.DESCRIPTOR);
            }
            if (i == 1) {
                List<String> relayDeviceIds = getRelayDeviceIds();
                parcel2.writeNoException();
                parcel2.writeStringList(relayDeviceIds);
            } else if (i == 2) {
                IDeviceChangeCallback iDeviceChangeCallbackAsInterface = IDeviceChangeCallback.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                registerRelayDeviceChangeCallback(iDeviceChangeCallbackAsInterface);
                parcel2.writeNoException();
            } else if (i == 3) {
                IDeviceChangeCallback iDeviceChangeCallbackAsInterface2 = IDeviceChangeCallback.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                unregisterRelayDeviceChangeCallback(iDeviceChangeCallbackAsInterface2);
                parcel2.writeNoException();
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            return true;
        }

        final class Proxy implements IRelayService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IRelayService.DESCRIPTOR;
            }

            @Override // com.android.services.telephony.relay.IRelayService
            public List getRelayDeviceIds() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IRelayService.DESCRIPTOR);
                    this.mRemote.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.createStringArrayList();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.services.telephony.relay.IRelayService
            public void registerRelayDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IRelayService.DESCRIPTOR);
                    parcelObtain.writeStrongInterface(iDeviceChangeCallback);
                    this.mRemote.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.services.telephony.relay.IRelayService
            public void unregisterRelayDeviceChangeCallback(IDeviceChangeCallback iDeviceChangeCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IRelayService.DESCRIPTOR);
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
