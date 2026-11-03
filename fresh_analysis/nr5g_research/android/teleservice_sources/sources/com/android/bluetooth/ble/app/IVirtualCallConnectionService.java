package com.android.bluetooth.ble.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.android.bluetooth.ble.app.virtualCall.VirtualCallDevice;

/* JADX INFO: loaded from: classes.dex */
public interface IVirtualCallConnectionService extends IInterface {
    boolean answerTheCall();

    boolean connect(VirtualCallDevice virtualCallDevice);

    boolean disConnect();

    void registerEventListener(IVirtualCallEventListener iVirtualCallEventListener);

    boolean rejectTheCall(byte[] bArr);

    boolean requestTheCall(byte[] bArr, byte[] bArr2);

    boolean sendRecordData(byte[] bArr);

    boolean setScanMode(int i);

    boolean startScan();

    boolean stopScan();

    public abstract class Stub extends Binder implements IVirtualCallConnectionService {
        public static IVirtualCallConnectionService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IVirtualCallConnectionService)) {
                return (IVirtualCallConnectionService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        final class Proxy implements IVirtualCallConnectionService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.bluetooth.ble.app.IVirtualCallConnectionService
            public void registerEventListener(IVirtualCallEventListener iVirtualCallEventListener) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
                    parcelObtain.writeStrongInterface(iVirtualCallEventListener);
                    this.mRemote.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.bluetooth.ble.app.IVirtualCallConnectionService
            public boolean setScanMode(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(6, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.bluetooth.ble.app.IVirtualCallConnectionService
            public boolean startScan() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
                    this.mRemote.transact(7, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.bluetooth.ble.app.IVirtualCallConnectionService
            public boolean stopScan() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
                    this.mRemote.transact(8, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.bluetooth.ble.app.IVirtualCallConnectionService
            public boolean connect(VirtualCallDevice virtualCallDevice) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
                    parcelObtain.writeTypedObject(virtualCallDevice, 0);
                    this.mRemote.transact(11, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.bluetooth.ble.app.IVirtualCallConnectionService
            public boolean disConnect() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
                    this.mRemote.transact(12, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.bluetooth.ble.app.IVirtualCallConnectionService
            public boolean answerTheCall() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
                    this.mRemote.transact(13, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.bluetooth.ble.app.IVirtualCallConnectionService
            public boolean rejectTheCall(byte[] bArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
                    parcelObtain.writeByteArray(bArr);
                    this.mRemote.transact(14, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.bluetooth.ble.app.IVirtualCallConnectionService
            public boolean sendRecordData(byte[] bArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
                    parcelObtain.writeByteArray(bArr);
                    this.mRemote.transact(15, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.bluetooth.ble.app.IVirtualCallConnectionService
            public boolean requestTheCall(byte[] bArr, byte[] bArr2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.android.bluetooth.ble.app.IVirtualCallConnectionService");
                    parcelObtain.writeByteArray(bArr);
                    parcelObtain.writeByteArray(bArr2);
                    this.mRemote.transact(16, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
