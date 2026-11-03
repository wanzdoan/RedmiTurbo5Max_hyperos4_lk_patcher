package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class FeedbackCellPropertyCategoryS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.FeedbackCellPropertyCategoryS.1
        @Override // android.os.Parcelable.Creator
        public FeedbackCellPropertyCategoryS createFromParcel(Parcel parcel) {
            FeedbackCellPropertyCategoryS feedbackCellPropertyCategoryS = new FeedbackCellPropertyCategoryS();
            feedbackCellPropertyCategoryS.readFromParcel(parcel);
            return feedbackCellPropertyCategoryS;
        }

        @Override // android.os.Parcelable.Creator
        public FeedbackCellPropertyCategoryS[] newArray(int i) {
            return new FeedbackCellPropertyCategoryS[i];
        }
    };
    public CellPropertyCategoryRsS rs;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
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
                this.rs = (CellPropertyCategoryRsS) parcel.readTypedObject(CellPropertyCategoryRsS.CREATOR);
                if (iDataPosition > Integer.MAX_VALUE - i) {
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
        return describeContents(this.rs);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
