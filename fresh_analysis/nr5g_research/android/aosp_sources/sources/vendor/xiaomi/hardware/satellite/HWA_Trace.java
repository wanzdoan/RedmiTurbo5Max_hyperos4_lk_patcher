package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class HWA_Trace implements Parcelable {
    public static final Parcelable.Creator<HWA_Trace> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.HWA_Trace.1
        @Override // android.os.Parcelable.Creator
        public HWA_Trace createFromParcel(Parcel parcel) {
            HWA_Trace hWA_Trace = new HWA_Trace();
            hWA_Trace.readFromParcel(parcel);
            return hWA_Trace;
        }

        @Override // android.os.Parcelable.Creator
        public HWA_Trace[] newArray(int i) {
            return new HWA_Trace[i];
        }
    };
    public int evtId = 0;
    public int evtType = 0;
    public String hexstr;
    public String time;

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
        parcel.writeInt(this.evtId);
        parcel.writeInt(this.evtType);
        parcel.writeString(this.time);
        parcel.writeString(this.hexstr);
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
                this.evtId = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.evtType = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.time = parcel.readString();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.hexstr = parcel.readString();
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
