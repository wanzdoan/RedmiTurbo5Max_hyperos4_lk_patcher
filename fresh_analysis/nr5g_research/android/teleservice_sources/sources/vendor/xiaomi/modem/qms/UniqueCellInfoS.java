package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class UniqueCellInfoS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.UniqueCellInfoS.1
        @Override // android.os.Parcelable.Creator
        public UniqueCellInfoS createFromParcel(Parcel parcel) {
            UniqueCellInfoS uniqueCellInfoS = new UniqueCellInfoS();
            uniqueCellInfoS.readFromParcel(parcel);
            return uniqueCellInfoS;
        }

        @Override // android.os.Parcelable.Creator
        public UniqueCellInfoS[] newArray(int i) {
            return new UniqueCellInfoS[i];
        }
    };
    public int band;
    public long cell_identity = 0;
    public int freq = 0;
    public int pci = 0;
    public PlmnIdS plmn;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedObject(this.plmn, i);
        parcel.writeLong(this.cell_identity);
        parcel.writeInt(this.band);
        parcel.writeInt(this.freq);
        parcel.writeInt(this.pci);
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
                this.plmn = (PlmnIdS) parcel.readTypedObject(PlmnIdS.CREATOR);
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.cell_identity = parcel.readLong();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.band = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.freq = parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.pci = parcel.readInt();
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
        return describeContents(this.plmn);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
