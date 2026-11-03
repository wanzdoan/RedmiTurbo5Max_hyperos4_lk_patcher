package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class CellPropertyCategoryHdrS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.CellPropertyCategoryHdrS.1
        @Override // android.os.Parcelable.Creator
        public CellPropertyCategoryHdrS createFromParcel(Parcel parcel) {
            CellPropertyCategoryHdrS cellPropertyCategoryHdrS = new CellPropertyCategoryHdrS();
            cellPropertyCategoryHdrS.readFromParcel(parcel);
            return cellPropertyCategoryHdrS;
        }

        @Override // android.os.Parcelable.Creator
        public CellPropertyCategoryHdrS[] newArray(int i) {
            return new CellPropertyCategoryHdrS[i];
        }
    };
    public int service_type = 0;
    public int load = 0;
    public int expiry_timer = 0;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.service_type);
        parcel.writeInt(this.load);
        parcel.writeInt(this.expiry_timer);
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
                this.service_type = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.load = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.expiry_timer = parcel.readInt();
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
}
