package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class NrDubiousCellInfoS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.NrDubiousCellInfoS.1
        @Override // android.os.Parcelable.Creator
        public NrDubiousCellInfoS createFromParcel(Parcel parcel) {
            NrDubiousCellInfoS nrDubiousCellInfoS = new NrDubiousCellInfoS();
            nrDubiousCellInfoS.readFromParcel(parcel);
            return nrDubiousCellInfoS;
        }

        @Override // android.os.Parcelable.Creator
        public NrDubiousCellInfoS[] newArray(int i) {
            return new NrDubiousCellInfoS[i];
        }
    };
    public DubiousCellInfoS cellInfo;
    public long cgi = 0;
    public int nrScsType;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedObject(this.cellInfo, i);
        parcel.writeLong(this.cgi);
        parcel.writeInt(this.nrScsType);
        int iDataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(iDataPosition);
        parcel.writeInt(iDataPosition2 - iDataPosition);
        parcel.setDataPosition(iDataPosition2);
    }

    public final void readFromParcel(Parcel parcel) {
        int iDataPosition = parcel.dataPosition();
        int i = parcel.readInt();
        try {
            if (i < 4) {
                throw new BadParcelableException("Parcelable too small");
            }
            if (parcel.dataPosition() - iDataPosition < i) {
                this.cellInfo = (DubiousCellInfoS) parcel.readTypedObject(DubiousCellInfoS.CREATOR);
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.cgi = parcel.readLong();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.nrScsType = parcel.readInt();
                        if (iDataPosition > Integer.MAX_VALUE - i) {
                            PlmnIdS$$ExternalSyntheticBUOutline0.m();
                            return;
                        }
                    } else if (iDataPosition > Integer.MAX_VALUE - i) {
                        PlmnIdS$$ExternalSyntheticBUOutline0.m();
                        return;
                    }
                } else if (iDataPosition > Integer.MAX_VALUE - i) {
                    PlmnIdS$$ExternalSyntheticBUOutline0.m();
                    return;
                }
            } else if (iDataPosition > Integer.MAX_VALUE - i) {
                PlmnIdS$$ExternalSyntheticBUOutline0.m();
                return;
            }
            parcel.setDataPosition(iDataPosition + i);
        } catch (Throwable th) {
            if (iDataPosition > Integer.MAX_VALUE - i) {
                PlmnIdS$$ExternalSyntheticBUOutline0.m();
            } else {
                parcel.setDataPosition(iDataPosition + i);
                throw th;
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return describeContents(this.cellInfo);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
