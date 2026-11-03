package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class NtnPosInfo implements Parcelable {
    public static final Parcelable.Creator<NtnPosInfo> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.NtnPosInfo.1
        @Override // android.os.Parcelable.Creator
        public NtnPosInfo createFromParcel(Parcel parcel) {
            NtnPosInfo ntnPosInfo = new NtnPosInfo();
            ntnPosInfo.readFromParcel(parcel);
            return ntnPosInfo;
        }

        @Override // android.os.Parcelable.Creator
        public NtnPosInfo[] newArray(int i) {
            return new NtnPosInfo[i];
        }
    };
    public int beam_id = 0;
    public String lat_beam;
    public String long_beam;

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
        parcel.writeString(this.long_beam);
        parcel.writeString(this.lat_beam);
        parcel.writeInt(this.beam_id);
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
                this.long_beam = parcel.readString();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.lat_beam = parcel.readString();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.beam_id = parcel.readInt();
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
