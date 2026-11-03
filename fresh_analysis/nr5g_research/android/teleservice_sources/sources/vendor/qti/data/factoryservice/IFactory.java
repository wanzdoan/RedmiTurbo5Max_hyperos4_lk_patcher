package vendor.qti.data.factoryservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.qti.latencyaidlservice.IClientToken;
import vendor.qti.latencyaidlservice.ILinkLatencyService;

/* JADX INFO: loaded from: classes2.dex */
public interface IFactory extends IInterface {
    public static final String DESCRIPTOR = "vendor$qti$data$factoryservice$IFactory".replace('$', '.');

    ILinkLatencyService createILinkLatencyService(IClientToken iClientToken, Result result);

    public abstract class Stub extends Binder implements IFactory {
        public static IFactory asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(IFactory.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IFactory)) {
                return (IFactory) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        class Proxy implements IFactory {
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

            @Override // vendor.qti.data.factoryservice.IFactory
            public ILinkLatencyService createILinkLatencyService(IClientToken iClientToken, Result result) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IFactory.DESCRIPTOR);
                    parcelObtain.writeStrongInterface(iClientToken);
                    parcelObtain.writeTypedObject(result, 0);
                    if (!this.mRemote.transact(5, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method createILinkLatencyService is unimplemented.");
                    }
                    parcelObtain2.readException();
                    ILinkLatencyService iLinkLatencyServiceAsInterface = ILinkLatencyService.Stub.asInterface(parcelObtain2.readStrongBinder());
                    if (parcelObtain2.readInt() != 0) {
                        result.readFromParcel(parcelObtain2);
                    }
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return iLinkLatencyServiceAsInterface;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }
        }
    }
}
