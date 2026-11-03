package com.android.phone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface IMiuiNetworkQueryService extends IInterface {
    void startNetworkQuery(IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallback, int i);

    void stopNetworkQuery(IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallback, int i);

    void unregisterCallback(IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallback);

    public abstract class Stub extends Binder implements IMiuiNetworkQueryService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.phone.IMiuiNetworkQueryService");
        }

        public static IMiuiNetworkQueryService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.android.phone.IMiuiNetworkQueryService");
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IMiuiNetworkQueryService)) {
                return (IMiuiNetworkQueryService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.phone.IMiuiNetworkQueryService");
            }
            if (i == 1) {
                IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallbackAsInterface = IMiuiNetworkQueryServiceCallback.Stub.asInterface(parcel.readStrongBinder());
                int i3 = parcel.readInt();
                parcel.enforceNoDataAvail();
                startNetworkQuery(iMiuiNetworkQueryServiceCallbackAsInterface, i3);
            } else if (i == 2) {
                IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallbackAsInterface2 = IMiuiNetworkQueryServiceCallback.Stub.asInterface(parcel.readStrongBinder());
                int i4 = parcel.readInt();
                parcel.enforceNoDataAvail();
                stopNetworkQuery(iMiuiNetworkQueryServiceCallbackAsInterface2, i4);
            } else if (i == 3) {
                IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallbackAsInterface3 = IMiuiNetworkQueryServiceCallback.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                unregisterCallback(iMiuiNetworkQueryServiceCallbackAsInterface3);
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            return true;
        }

        final class Proxy implements IMiuiNetworkQueryService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.phone.IMiuiNetworkQueryService
            public void startNetworkQuery(IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallback, int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken("com.android.phone.IMiuiNetworkQueryService");
                    parcelObtain.writeStrongInterface(iMiuiNetworkQueryServiceCallback);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(1, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.phone.IMiuiNetworkQueryService
            public void stopNetworkQuery(IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallback, int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken("com.android.phone.IMiuiNetworkQueryService");
                    parcelObtain.writeStrongInterface(iMiuiNetworkQueryServiceCallback);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(2, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }
    }
}
