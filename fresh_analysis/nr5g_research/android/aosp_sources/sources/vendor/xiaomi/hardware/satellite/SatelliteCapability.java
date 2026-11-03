package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteCapability implements Parcelable {
    public static final Parcelable.Creator<SatelliteCapability> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.SatelliteCapability.1
        @Override // android.os.Parcelable.Creator
        public SatelliteCapability createFromParcel(Parcel parcel) {
            SatelliteCapability satelliteCapability = new SatelliteCapability();
            satelliteCapability.readFromParcel(parcel);
            return satelliteCapability;
        }

        @Override // android.os.Parcelable.Creator
        public SatelliteCapability[] newArray(int i) {
            return new SatelliteCapability[i];
        }
    };
    public int satelliteType = 0;
    public int chipType = 0;
    public int capability = 0;

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
        parcel.writeInt(this.satelliteType);
        parcel.writeInt(this.chipType);
        parcel.writeInt(this.capability);
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
                this.satelliteType = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.chipType = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.capability = parcel.readInt();
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
