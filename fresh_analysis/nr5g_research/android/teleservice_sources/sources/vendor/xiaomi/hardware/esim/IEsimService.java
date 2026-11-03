package vendor.xiaomi.hardware.esim;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes2.dex */
public interface IEsimService extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$hardware$esim$IEsimService".replace('$', '.');

    byte[] esim_bind_step_1(byte[] bArr);

    int esim_bind_step_2(byte[] bArr);

    boolean esim_get_bind_status();

    byte[] esim_pair_step_1(byte[] bArr);

    int esim_pair_step_2(byte[] bArr);

    byte[] esim_read_eid();

    public abstract class Stub extends Binder implements IEsimService {
        public static IEsimService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(IEsimService.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IEsimService)) {
                return (IEsimService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        class Proxy implements IEsimService {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // vendor.xiaomi.hardware.esim.IEsimService
            public byte[] esim_bind_step_1(byte[] bArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IEsimService.DESCRIPTOR);
                    parcelObtain.writeByteArray(bArr);
                    if (!this.mRemote.transact(1, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method esim_bind_step_1 is unimplemented.");
                    }
                    parcelObtain2.readException();
                    byte[] bArrCreateByteArray = parcelObtain2.createByteArray();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return bArrCreateByteArray;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.esim.IEsimService
            public int esim_bind_step_2(byte[] bArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IEsimService.DESCRIPTOR);
                    parcelObtain.writeByteArray(bArr);
                    if (!this.mRemote.transact(2, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method esim_bind_step_2 is unimplemented.");
                    }
                    parcelObtain2.readException();
                    int i = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.esim.IEsimService
            public byte[] esim_pair_step_1(byte[] bArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IEsimService.DESCRIPTOR);
                    parcelObtain.writeByteArray(bArr);
                    if (!this.mRemote.transact(3, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method esim_pair_step_1 is unimplemented.");
                    }
                    parcelObtain2.readException();
                    byte[] bArrCreateByteArray = parcelObtain2.createByteArray();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return bArrCreateByteArray;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.esim.IEsimService
            public int esim_pair_step_2(byte[] bArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IEsimService.DESCRIPTOR);
                    parcelObtain.writeByteArray(bArr);
                    if (!this.mRemote.transact(4, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method esim_pair_step_2 is unimplemented.");
                    }
                    parcelObtain2.readException();
                    int i = parcelObtain2.readInt();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.esim.IEsimService
            public byte[] esim_read_eid() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IEsimService.DESCRIPTOR);
                    if (!this.mRemote.transact(5, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method esim_read_eid is unimplemented.");
                    }
                    parcelObtain2.readException();
                    byte[] bArrCreateByteArray = parcelObtain2.createByteArray();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return bArrCreateByteArray;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.esim.IEsimService
            public boolean esim_get_bind_status() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IEsimService.DESCRIPTOR);
                    if (!this.mRemote.transact(6, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method esim_get_bind_status is unimplemented.");
                    }
                    parcelObtain2.readException();
                    boolean z = parcelObtain2.readBoolean();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return z;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }
        }
    }
}
