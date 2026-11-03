package com.android.bluetooth.ble.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.bluetooth.ble.app.virtualCall.VirtualCallDevice;

/* JADX INFO: loaded from: classes.dex */
public interface IVirtualCallEventListener extends IInterface {
    void onAuidioPlayerData(byte[] bArr);

    void onCallAnswer(byte[] bArr);

    void onCallReject(byte[] bArr);

    void onCallRequest(byte[] bArr, byte[] bArr2);

    void onConnectStateChanged(String str, int i, int i2);

    void onDeviceFound(VirtualCallDevice virtualCallDevice);

    public abstract class Stub extends Binder implements IVirtualCallEventListener {
        static final int TRANSACTION_onAuidioPlayerData = 6;
        static final int TRANSACTION_onCallAnswer = 4;
        static final int TRANSACTION_onCallReject = 5;
        static final int TRANSACTION_onCallRequest = 3;
        static final int TRANSACTION_onConnectStateChanged = 2;
        static final int TRANSACTION_onDeviceFound = 1;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.bluetooth.ble.app.IVirtualCallEventListener");
        }

        public static IVirtualCallEventListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.android.bluetooth.ble.app.IVirtualCallEventListener");
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IVirtualCallEventListener)) {
                return (IVirtualCallEventListener) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.bluetooth.ble.app.IVirtualCallEventListener");
            }
            switch (i) {
                case 1:
                    VirtualCallDevice virtualCallDevice = (VirtualCallDevice) parcel.readTypedObject(VirtualCallDevice.CREATOR);
                    parcel.enforceNoDataAvail();
                    onDeviceFound(virtualCallDevice);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    String string = parcel.readString();
                    int i3 = parcel.readInt();
                    int i4 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    onConnectStateChanged(string, i3, i4);
                    parcel2.writeNoException();
                    return true;
                case 3:
                    byte[] bArrCreateByteArray = parcel.createByteArray();
                    byte[] bArrCreateByteArray2 = parcel.createByteArray();
                    parcel.enforceNoDataAvail();
                    onCallRequest(bArrCreateByteArray, bArrCreateByteArray2);
                    parcel2.writeNoException();
                    return true;
                case 4:
                    byte[] bArrCreateByteArray3 = parcel.createByteArray();
                    parcel.enforceNoDataAvail();
                    onCallAnswer(bArrCreateByteArray3);
                    parcel2.writeNoException();
                    return true;
                case 5:
                    byte[] bArrCreateByteArray4 = parcel.createByteArray();
                    parcel.enforceNoDataAvail();
                    onCallReject(bArrCreateByteArray4);
                    parcel2.writeNoException();
                    return true;
                case 6:
                    byte[] bArrCreateByteArray5 = parcel.createByteArray();
                    parcel.enforceNoDataAvail();
                    onAuidioPlayerData(bArrCreateByteArray5);
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        final class Proxy implements IVirtualCallEventListener {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }
        }
    }
}
