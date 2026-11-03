package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class DubiousCellInfoS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.DubiousCellInfoS.1
        @Override // android.os.Parcelable.Creator
        public DubiousCellInfoS createFromParcel(Parcel parcel) {
            DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
            dubiousCellInfoS.readFromParcel(parcel);
            return dubiousCellInfoS;
        }

        @Override // android.os.Parcelable.Creator
        public DubiousCellInfoS[] newArray(int i) {
            return new DubiousCellInfoS[i];
        }
    };
    public int activeBand;
    public PlmnIdS plmnId;
    public int arfcn = 0;
    public char pci = 0;
    public int causeCodeBitMask = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedObject(this.plmnId, i);
        parcel.writeInt(this.activeBand);
        parcel.writeInt(this.arfcn);
        parcel.writeInt(this.pci);
        parcel.writeInt(this.causeCodeBitMask);
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
                this.plmnId = (PlmnIdS) parcel.readTypedObject(PlmnIdS.CREATOR);
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.activeBand = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.arfcn = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.pci = (char) parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.causeCodeBitMask = parcel.readInt();
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
        return describeContents(this.plmnId);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
