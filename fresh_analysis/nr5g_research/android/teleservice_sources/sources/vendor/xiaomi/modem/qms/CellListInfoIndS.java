package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class CellListInfoIndS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.CellListInfoIndS.1
        @Override // android.os.Parcelable.Creator
        public CellListInfoIndS createFromParcel(Parcel parcel) {
            CellListInfoIndS cellListInfoIndS = new CellListInfoIndS();
            cellListInfoIndS.readFromParcel(parcel);
            return cellListInfoIndS;
        }

        @Override // android.os.Parcelable.Creator
        public CellListInfoIndS[] newArray(int i) {
            return new CellListInfoIndS[i];
        }
    };
    public FeedbackCellListInfoS[] lte_cell_list;
    public int mitigation_type;
    public FeedbackCellListInfoS[] nr_cell_list;
    public int lte_cell_list_len = 0;
    public int nr_cell_list_len = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.mitigation_type);
        parcel.writeInt(this.lte_cell_list_len);
        parcel.writeTypedArray(this.lte_cell_list, i);
        parcel.writeInt(this.nr_cell_list_len);
        parcel.writeTypedArray(this.nr_cell_list, i);
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
                this.mitigation_type = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.lte_cell_list_len = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        Parcelable.Creator creator = FeedbackCellListInfoS.CREATOR;
                        this.lte_cell_list = (FeedbackCellListInfoS[]) parcel.createTypedArray(creator);
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.nr_cell_list_len = parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.nr_cell_list = (FeedbackCellListInfoS[]) parcel.createTypedArray(creator);
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
        return describeContents(this.nr_cell_list) | describeContents(this.lte_cell_list);
    }

    private int describeContents(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Object[]) {
            int iDescribeContents = 0;
            for (Object obj2 : (Object[]) obj) {
                iDescribeContents |= describeContents(obj2);
            }
            return iDescribeContents;
        }
        if (obj instanceof Parcelable) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
