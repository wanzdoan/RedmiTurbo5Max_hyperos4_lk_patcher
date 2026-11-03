package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class HLS_ERROR_INFO implements Parcelable {
    public static final Parcelable.Creator<HLS_ERROR_INFO> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.HLS_ERROR_INFO.1
        @Override // android.os.Parcelable.Creator
        public HLS_ERROR_INFO createFromParcel(Parcel parcel) {
            HLS_ERROR_INFO hls_error_info = new HLS_ERROR_INFO();
            hls_error_info.readFromParcel(parcel);
            return hls_error_info;
        }

        @Override // android.os.Parcelable.Creator
        public HLS_ERROR_INFO[] newArray(int i) {
            return new HLS_ERROR_INFO[i];
        }
    };
    public int class_info = 0;
    public int error_cause = 0;

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
        parcel.writeInt(this.class_info);
        parcel.writeInt(this.error_cause);
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
                this.class_info = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.error_cause = parcel.readInt();
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
