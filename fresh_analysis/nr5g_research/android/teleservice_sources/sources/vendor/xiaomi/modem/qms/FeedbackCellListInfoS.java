package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class FeedbackCellListInfoS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.FeedbackCellListInfoS.1
        @Override // android.os.Parcelable.Creator
        public FeedbackCellListInfoS createFromParcel(Parcel parcel) {
            FeedbackCellListInfoS feedbackCellListInfoS = new FeedbackCellListInfoS();
            feedbackCellListInfoS.readFromParcel(parcel);
            return feedbackCellListInfoS;
        }

        @Override // android.os.Parcelable.Creator
        public FeedbackCellListInfoS[] newArray(int i) {
            return new FeedbackCellListInfoS[i];
        }
    };
    public int cause_bitmask = 0;
    public UniqueCellInfoS cell_info;
    public int feedback_cause;
    public FeedbackCellPropertyCategoryS property;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedObject(this.cell_info, i);
        parcel.writeInt(this.cause_bitmask);
        parcel.writeInt(this.feedback_cause);
        parcel.writeTypedObject(this.property, i);
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
                this.cell_info = (UniqueCellInfoS) parcel.readTypedObject(UniqueCellInfoS.CREATOR);
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.cause_bitmask = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.feedback_cause = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.property = (FeedbackCellPropertyCategoryS) parcel.readTypedObject(FeedbackCellPropertyCategoryS.CREATOR);
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
        return describeContents(this.property) | describeContents(this.cell_info);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
