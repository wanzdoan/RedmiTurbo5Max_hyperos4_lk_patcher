package vendor.xiaomi.sensor.citsensorservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes2.dex */
public interface ICitSensorService extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$sensor$citsensorservice$ICitSensorService".replace('$', '.');

    int setArrayConfig(int i, float[] fArr, int i2);

    public abstract class Stub extends Binder implements ICitSensorService {
        public static ICitSensorService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(ICitSensorService.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof ICitSensorService)) {
                return (ICitSensorService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        class Proxy implements ICitSensorService {
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

            @Override // vendor.xiaomi.sensor.citsensorservice.ICitSensorService
            public int setArrayConfig(int i, float[] fArr, int i2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(ICitSensorService.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeFloatArray(fArr);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(13, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method setArrayConfig is unimplemented.");
                    }
                    parcelObtain2.readException();
                    int i3 = parcelObtain2.readInt();
                    parcelObtain2.readFloatArray(fArr);
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return i3;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }
        }
    }
}
