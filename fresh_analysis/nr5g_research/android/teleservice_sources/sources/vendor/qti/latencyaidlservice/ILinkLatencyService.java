package vendor.qti.latencyaidlservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes2.dex */
public interface ILinkLatencyService extends IInterface {
    public static final String DESCRIPTOR = "vendor$qti$latencyaidlservice$ILinkLatencyService".replace('$', '.');

    long close();

    long prioritizeDefaultDataSubscription(boolean z);

    void setCallback(ILinkLatencyIndication iLinkLatencyIndication);

    long setLevel(Config config, SetLevelArguments setLevelArguments);

    long updatePdcpDiscardTimerForDDS(long j);

    public abstract class Stub extends Binder implements ILinkLatencyService {
        public static ILinkLatencyService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(ILinkLatencyService.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof ILinkLatencyService)) {
                return (ILinkLatencyService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        class Proxy implements ILinkLatencyService {
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

            @Override // vendor.qti.latencyaidlservice.ILinkLatencyService
            public long close() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(ILinkLatencyService.DESCRIPTOR);
                    if (!this.mRemote.transact(2, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method close is unimplemented.");
                    }
                    parcelObtain2.readException();
                    long j = parcelObtain2.readLong();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return j;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.qti.latencyaidlservice.ILinkLatencyService
            public long prioritizeDefaultDataSubscription(boolean z) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(ILinkLatencyService.DESCRIPTOR);
                    parcelObtain.writeBoolean(z);
                    if (!this.mRemote.transact(7, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method prioritizeDefaultDataSubscription is unimplemented.");
                    }
                    parcelObtain2.readException();
                    long j = parcelObtain2.readLong();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return j;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.qti.latencyaidlservice.ILinkLatencyService
            public void setCallback(ILinkLatencyIndication iLinkLatencyIndication) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(ILinkLatencyService.DESCRIPTOR);
                    parcelObtain.writeStrongInterface(iLinkLatencyIndication);
                    if (!this.mRemote.transact(8, parcelObtain, null, 1)) {
                        throw new RemoteException("Method setCallback is unimplemented.");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.qti.latencyaidlservice.ILinkLatencyService
            public long setLevel(Config config, SetLevelArguments setLevelArguments) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(ILinkLatencyService.DESCRIPTOR);
                    parcelObtain.writeTypedObject(config, 0);
                    if (!this.mRemote.transact(9, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method setLevel is unimplemented.");
                    }
                    parcelObtain2.readException();
                    long j = parcelObtain2.readLong();
                    if (parcelObtain2.readInt() != 0) {
                        setLevelArguments.readFromParcel(parcelObtain2);
                    }
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return j;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.qti.latencyaidlservice.ILinkLatencyService
            public long updatePdcpDiscardTimerForDDS(long j) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(ILinkLatencyService.DESCRIPTOR);
                    parcelObtain.writeLong(j);
                    if (!this.mRemote.transact(13, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Method updatePdcpDiscardTimerForDDS is unimplemented.");
                    }
                    parcelObtain2.readException();
                    long j2 = parcelObtain2.readLong();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    return j2;
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }
        }
    }
}
