package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class NtnSignalStrength implements Parcelable {
    public static final Parcelable.Creator<NtnSignalStrength> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.NtnSignalStrength.1
        @Override // android.os.Parcelable.Creator
        public NtnSignalStrength createFromParcel(Parcel parcel) {
            NtnSignalStrength ntnSignalStrength = new NtnSignalStrength();
            ntnSignalStrength.readFromParcel(parcel);
            return ntnSignalStrength;
        }

        @Override // android.os.Parcelable.Creator
        public NtnSignalStrength[] newArray(int i) {
            return new NtnSignalStrength[i];
        }
    };
    public int signalStrength = 0;
    public int bitErrorRate = 0;
    public int timingAdvance = 0;

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
        parcel.writeInt(this.signalStrength);
        parcel.writeInt(this.bitErrorRate);
        parcel.writeInt(this.timingAdvance);
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
                this.signalStrength = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.bitErrorRate = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.timingAdvance = parcel.readInt();
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
