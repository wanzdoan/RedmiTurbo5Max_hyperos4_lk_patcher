package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class CellPropertyCategoryTypeV2S implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.CellPropertyCategoryTypeV2S.1
        @Override // android.os.Parcelable.Creator
        public CellPropertyCategoryTypeV2S createFromParcel(Parcel parcel) {
            CellPropertyCategoryTypeV2S cellPropertyCategoryTypeV2S = new CellPropertyCategoryTypeV2S();
            cellPropertyCategoryTypeV2S.readFromParcel(parcel);
            return cellPropertyCategoryTypeV2S;
        }

        @Override // android.os.Parcelable.Creator
        public CellPropertyCategoryTypeV2S[] newArray(int i) {
            return new CellPropertyCategoryTypeV2S[i];
        }
    };
    public CellPropertyCategoryCommonS common;
    public CellPropertyCategoryHdrS hdr;
    public CellPropertyCategoryHdrRatTypeS hdr_rat;
    public CellPropertyCategoryHstTypeS hst;
    public CellPropertyCategoryRsS rs;
    public boolean common_valid = false;
    public boolean hdr_valid = false;
    public boolean rs_valid = false;
    public boolean hst_valid = false;
    public boolean hdr_rat_valid = false;

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
        parcel.writeBoolean(this.hst_valid);
        parcel.writeTypedObject(this.hst, i);
        parcel.writeBoolean(this.hdr_rat_valid);
        parcel.writeTypedObject(this.hdr_rat, i);
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
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.hst_valid = parcel.readBoolean();
                                        if (parcel.dataPosition() - iDataPosition < i) {
                                            this.hst = (CellPropertyCategoryHstTypeS) parcel.readTypedObject(CellPropertyCategoryHstTypeS.CREATOR);
                                            if (parcel.dataPosition() - iDataPosition < i) {
                                                this.hdr_rat_valid = parcel.readBoolean();
                                                if (parcel.dataPosition() - iDataPosition < i) {
                                                    this.hdr_rat = (CellPropertyCategoryHdrRatTypeS) parcel.readTypedObject(CellPropertyCategoryHdrRatTypeS.CREATOR);
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
        return describeContents(this.hdr_rat) | describeContents(this.common) | describeContents(this.hdr) | describeContents(this.rs) | describeContents(this.hst);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
