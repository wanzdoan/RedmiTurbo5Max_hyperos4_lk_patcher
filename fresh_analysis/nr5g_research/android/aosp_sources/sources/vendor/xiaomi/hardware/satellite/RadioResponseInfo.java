package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class RadioResponseInfo implements Parcelable {
    public static final Parcelable.Creator<RadioResponseInfo> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.RadioResponseInfo.1
        @Override // android.os.Parcelable.Creator
        public RadioResponseInfo createFromParcel(Parcel parcel) {
            RadioResponseInfo radioResponseInfo = new RadioResponseInfo();
            radioResponseInfo.readFromParcel(parcel);
            return radioResponseInfo;
        }

        @Override // android.os.Parcelable.Creator
        public RadioResponseInfo[] newArray(int i) {
            return new RadioResponseInfo[i];
        }
    };
    public int error;
    public int serial = 0;
    public int type;

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
        parcel.writeInt(this.type);
        parcel.writeInt(this.serial);
        parcel.writeInt(this.error);
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
                this.type = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.serial = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.error = parcel.readInt();
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
