package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class CellPropertyCategoryS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.CellPropertyCategoryS.1
        @Override // android.os.Parcelable.Creator
        public CellPropertyCategoryS createFromParcel(Parcel parcel) {
            CellPropertyCategoryS cellPropertyCategoryS = new CellPropertyCategoryS();
            cellPropertyCategoryS.readFromParcel(parcel);
            return cellPropertyCategoryS;
        }

        @Override // android.os.Parcelable.Creator
        public CellPropertyCategoryS[] newArray(int i) {
            return new CellPropertyCategoryS[i];
        }
    };
    public CellPropertyCategoryCommonS common;
    public CellPropertyCategoryHdrS hdr;
    public CellPropertyCategoryRsS rs;
    public boolean common_valid = false;
    public boolean hdr_valid = false;
    public boolean rs_valid = false;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeBoolean(this.common_valid);
        parcel.writeTypedObject(this.common, i);
        parcel.writeBoolean(this.hdr_valid);
        parcel.writeTypedObject(this.hdr, i);
        parcel.writeBoolean(this.rs_valid);
        parcel.writeTypedObject(this.rs, i);
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
                this.common_valid = parcel.readBoolean();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.common = (CellPropertyCategoryCommonS) parcel.readTypedObject(CellPropertyCategoryCommonS.CREATOR);
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.hdr_valid = parcel.readBoolean();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.hdr = (CellPropertyCategoryHdrS) parcel.readTypedObject(CellPropertyCategoryHdrS.CREATOR);
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.rs_valid = parcel.readBoolean();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.rs = (CellPropertyCategoryRsS) parcel.readTypedObject(CellPropertyCategoryRsS.CREATOR);
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
        return describeContents(this.rs) | describeContents(this.common) | describeContents(this.hdr);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
