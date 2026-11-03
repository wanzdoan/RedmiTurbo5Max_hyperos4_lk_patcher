package vendor.xiaomi.modem.qms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes2.dex */
public interface IMiQmsIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$modem$qms$IMiQmsIndication".replace('$', '.');

    void cellListInfoV2Ind(CellListInfoIndS cellListInfoIndS);

    void cell_list_info_ind(CellListInfoIndS cellListInfoIndS);

    String getInterfaceHash();

    int getInterfaceVersion();

    void serviceStatusInd(int i);

    void serviceStatusV2Ind(int i);

    public abstract class Stub extends Binder implements IMiQmsIndication {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IMiQmsIndication.DESCRIPTOR);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IMiQmsIndication.DESCRIPTOR);
            }
            if (i == 16777215) {
                parcel2.writeNoException();
                parcel2.writeInt(getInterfaceVersion());
                return true;
            }
            if (i == 16777214) {
                parcel2.writeNoException();
                parcel2.writeString(getInterfaceHash());
                return true;
            }
            if (i == 1) {
                CellListInfoIndS cellListInfoIndS = (CellListInfoIndS) parcel.readTypedObject(CellListInfoIndS.CREATOR);
                parcel.enforceNoDataAvail();
                cell_list_info_ind(cellListInfoIndS);
            } else if (i == 2) {
                int i3 = parcel.readInt();
                parcel.enforceNoDataAvail();
                serviceStatusInd(i3);
            } else if (i == 3) {
                int i4 = parcel.readInt();
                parcel.enforceNoDataAvail();
                serviceStatusV2Ind(i4);
            } else if (i == 4) {
                CellListInfoIndS cellListInfoIndS2 = (CellListInfoIndS) parcel.readTypedObject(CellListInfoIndS.CREATOR);
                parcel.enforceNoDataAvail();
                cellListInfoV2Ind(cellListInfoIndS2);
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            return true;
        }
    }
}
