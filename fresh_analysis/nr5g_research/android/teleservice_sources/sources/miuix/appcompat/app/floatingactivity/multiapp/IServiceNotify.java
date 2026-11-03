package miuix.appcompat.app.floatingactivity.multiapp;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes2.dex */
public interface IServiceNotify extends IInterface {
    Bundle notifyFromService(int i, Bundle bundle);

    public abstract class Stub extends Binder implements IServiceNotify {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify");
        }

        public static IServiceNotify asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify");
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IServiceNotify)) {
                return (IServiceNotify) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i != 1) {
                if (i == 1598968902) {
                    parcel2.writeString("miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify");
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel.enforceInterface("miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify");
            parcel2.writeBundle(notifyFromService(parcel.readInt(), parcel.readBundle()));
            parcel2.writeNoException();
            return true;
        }

        class Proxy implements IServiceNotify {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify
            public Bundle notifyFromService(int i, Bundle bundle) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify");
                    parcelObtain.writeInt(i);
                    parcelObtain.writeBundle(bundle);
                    this.mRemote.transact(1, parcelObtain, parcelObtain2, 0);
                    Bundle bundle2 = parcelObtain2.readBundle();
                    parcelObtain2.readException();
                    return bundle2;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
