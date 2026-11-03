package com.aios.apptoolsdk.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface IAppToolProvider extends IInterface {
    String handleRequest(String str);

    public abstract class Stub extends Binder implements IAppToolProvider {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.aios.apptoolsdk.aidl.IAppToolProvider");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.aios.apptoolsdk.aidl.IAppToolProvider");
            }
            if (i == 1598968902) {
                parcel2.writeString("com.aios.apptoolsdk.aidl.IAppToolProvider");
                return true;
            }
            if (i == 1) {
                String strHandleRequest = handleRequest(parcel.readString());
                parcel2.writeNoException();
                parcel2.writeString(strHandleRequest);
                return true;
            }
            return super.onTransact(i, parcel, parcel2, i2);
        }
    }
}
