package com.android.phone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.android.internal.telephony.OperatorInfo;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IMiuiNetworkQueryServiceCallback extends IInterface {
    void onIncrementalManualScanResult(String[] strArr, int i);

    void onQueryComplete(List list, int i);

    public abstract class Stub extends Binder implements IMiuiNetworkQueryServiceCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.phone.IMiuiNetworkQueryServiceCallback");
        }

        public static IMiuiNetworkQueryServiceCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.android.phone.IMiuiNetworkQueryServiceCallback");
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IMiuiNetworkQueryServiceCallback)) {
                return (IMiuiNetworkQueryServiceCallback) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.phone.IMiuiNetworkQueryServiceCallback");
            }
            if (i == 1) {
                ArrayList arrayListCreateTypedArrayList = parcel.createTypedArrayList(OperatorInfo.CREATOR);
                int i3 = parcel.readInt();
                parcel.enforceNoDataAvail();
                onQueryComplete(arrayListCreateTypedArrayList, i3);
            } else if (i == 2) {
                String[] strArrCreateStringArray = parcel.createStringArray();
                int i4 = parcel.readInt();
                parcel.enforceNoDataAvail();
                onIncrementalManualScanResult(strArrCreateStringArray, i4);
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            return true;
        }

        final class Proxy implements IMiuiNetworkQueryServiceCallback {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.phone.IMiuiNetworkQueryServiceCallback
            public void onQueryComplete(List list, int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken("com.android.phone.IMiuiNetworkQueryServiceCallback");
                    parcelObtain.writeTypedList(list, 0);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(1, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.phone.IMiuiNetworkQueryServiceCallback
            public void onIncrementalManualScanResult(String[] strArr, int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken("com.android.phone.IMiuiNetworkQueryServiceCallback");
                    parcelObtain.writeStringArray(strArr);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(2, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }
    }
}
