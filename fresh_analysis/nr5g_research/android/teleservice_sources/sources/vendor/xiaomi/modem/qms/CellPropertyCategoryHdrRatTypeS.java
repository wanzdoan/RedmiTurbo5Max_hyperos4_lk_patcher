package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class CellPropertyCategoryHdrRatTypeS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.CellPropertyCategoryHdrRatTypeS.1
        @Override // android.os.Parcelable.Creator
        public CellPropertyCategoryHdrRatTypeS createFromParcel(Parcel parcel) {
            CellPropertyCategoryHdrRatTypeS cellPropertyCategoryHdrRatTypeS = new CellPropertyCategoryHdrRatTypeS();
            cellPropertyCategoryHdrRatTypeS.readFromParcel(parcel);
            return cellPropertyCategoryHdrRatTypeS;
        }

        @Override // android.os.Parcelable.Creator
        public CellPropertyCategoryHdrRatTypeS[] newArray(int i) {
            return new CellPropertyCategoryHdrRatTypeS[i];
        }
    };
    public byte sub_id_mask = 0;
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
        parcel.writeByte(this.sub_id_mask);
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
                this.sub_id_mask = parcel.readByte();
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
