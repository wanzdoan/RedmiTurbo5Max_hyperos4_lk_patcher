package com.newcalllib.datachannel.V1_0;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface IImsDataChannelServiceController extends IInterface {
    void createImsDataChannel(String[] strArr, String str, int i, String str2, String str3);

    void setImsDataChannelCallback(IImsDataChannelCallback iImsDataChannelCallback, int i, String str);

    public abstract class Stub extends Binder implements IImsDataChannelServiceController {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.newcalllib.datachannel.V1_0.IImsDataChannelServiceController");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.newcalllib.datachannel.V1_0.IImsDataChannelServiceController");
            }
            if (i == 1) {
                String[] strArrCreateStringArray = parcel.createStringArray();
                String string = parcel.readString();
                int i3 = parcel.readInt();
                String string2 = parcel.readString();
                String string3 = parcel.readString();
                parcel.enforceNoDataAvail();
                createImsDataChannel(strArrCreateStringArray, string, i3, string2, string3);
                parcel2.writeNoException();
            } else if (i == 2) {
                IImsDataChannelCallback iImsDataChannelCallbackAsInterface = IImsDataChannelCallback.Stub.asInterface(parcel.readStrongBinder());
                int i4 = parcel.readInt();
                String string4 = parcel.readString();
                parcel.enforceNoDataAvail();
                setImsDataChannelCallback(iImsDataChannelCallbackAsInterface, i4, string4);
                parcel2.writeNoException();
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            return true;
        }
    }
}
