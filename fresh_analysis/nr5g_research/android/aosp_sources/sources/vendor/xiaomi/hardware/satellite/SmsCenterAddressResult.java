package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class SmsCenterAddressResult implements Parcelable {
    public static final Parcelable.Creator<SmsCenterAddressResult> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.SmsCenterAddressResult.1
        @Override // android.os.Parcelable.Creator
        public SmsCenterAddressResult createFromParcel(Parcel parcel) {
            SmsCenterAddressResult smsCenterAddressResult = new SmsCenterAddressResult();
            smsCenterAddressResult.readFromParcel(parcel);
            return smsCenterAddressResult;
        }

        @Override // android.os.Parcelable.Creator
        public SmsCenterAddressResult[] newArray(int i) {
            return new SmsCenterAddressResult[i];
        }
    };
    public String smsc;
    public int tosmsc = 0;

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
        parcel.writeString(this.smsc);
        parcel.writeInt(this.tosmsc);
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
                this.smsc = parcel.readString();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.tosmsc = parcel.readInt();
                    if (iDataPosition > Integer.MAX_VALUE - i) {
                        Call$$ExternalSyntheticBUOutline0.m();
                        return;
                    }
                } else if (iDataPosition > Integer.MAX_VALUE - i) {
                    Call$$ExternalSyntheticBUOutline0.m();
                    return;
                }
            } else if (iDataPosition > Integer.MAX_VALUE - i) {
                Call$$ExternalSyntheticBUOutline0.m();
                return;
            }
            parcel.setDataPosition(iDataPosition + i);
        } catch (Throwable th) {
            if (iDataPosition > Integer.MAX_VALUE - i) {
                Call$$ExternalSyntheticBUOutline0.m();
            } else {
                parcel.setDataPosition(iDataPosition + i);
                throw th;
            }
        }
    }
}
