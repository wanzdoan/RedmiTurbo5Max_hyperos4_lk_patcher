package vendor.qti.latencyaidlservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes2.dex */
public interface ILinkLatencyIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$qti$latencyaidlservice$ILinkLatencyIndication".replace('$', '.');

    void filterStatus(FilterInfo filterInfo);

    String getInterfaceHash();

    int getInterfaceVersion();

    public abstract class Stub extends Binder implements ILinkLatencyIndication {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, ILinkLatencyIndication.DESCRIPTOR);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            String str = ILinkLatencyIndication.DESCRIPTOR;
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(str);
            }
            switch (i) {
                case 16777214:
                    parcel2.writeNoException();
                    parcel2.writeString(getInterfaceHash());
                    return true;
                case 16777215:
                    parcel2.writeNoException();
                    parcel2.writeInt(getInterfaceVersion());
                    return true;
                case 1598968902:
                    parcel2.writeString(str);
                    return true;
                default:
                    if (i == 1) {
                        FilterInfo filterInfo = (FilterInfo) parcel.readTypedObject(FilterInfo.CREATOR);
                        parcel.enforceNoDataAvail();
                        filterStatus(filterInfo);
                        return true;
                    }
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }
}
