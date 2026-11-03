package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class UusInfo implements Parcelable {
    public static final Parcelable.Creator<UusInfo> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.UusInfo.1
        @Override // android.os.Parcelable.Creator
        public UusInfo createFromParcel(Parcel parcel) {
            UusInfo uusInfo = new UusInfo();
            uusInfo.readFromParcel(parcel);
            return uusInfo;
        }

        @Override // android.os.Parcelable.Creator
        public UusInfo[] newArray(int i) {
            return new UusInfo[i];
        }
    };
    public String uusData;
    public int uusDcs;
    public int uusType;

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
        parcel.writeInt(this.uusType);
        parcel.writeInt(this.uusDcs);
        parcel.writeString(this.uusData);
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
                this.uusType = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.uusDcs = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.uusData = parcel.readString();
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
