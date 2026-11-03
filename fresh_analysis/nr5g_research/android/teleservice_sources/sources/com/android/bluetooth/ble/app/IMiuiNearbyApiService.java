package com.android.bluetooth.ble.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface IMiuiNearbyApiService extends IInterface {
    void connectHotspotForEasyTether(String str, int i);

    void disconnectCelluarTether(String str);

    public abstract class Stub extends Binder implements IMiuiNearbyApiService {
        public static IMiuiNearbyApiService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.android.bluetooth.ble.app.IMiuiNearbyApiService");
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IMiuiNearbyApiService)) {
                return (IMiuiNearbyApiService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        class Proxy implements IMiuiNearbyApiService {
            public static IMiuiNearbyApiService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.bluetooth.ble.app.IMiuiNearbyApiService
            public void connectHotspotForEasyTether(String str, int i) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IMiuiNearbyApiService");
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(21, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().connectHotspotForEasyTether(str, i);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.bluetooth.ble.app.IMiuiNearbyApiService
            public void disconnectCelluarTether(String str) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IMiuiNearbyApiService");
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(25, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disconnectCelluarTether(str);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static IMiuiNearbyApiService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
