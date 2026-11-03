package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class EphemCalcInfo implements Parcelable {
    public static final Parcelable.Creator<EphemCalcInfo> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.EphemCalcInfo.1
        @Override // android.os.Parcelable.Creator
        public EphemCalcInfo createFromParcel(Parcel parcel) {
            EphemCalcInfo ephemCalcInfo = new EphemCalcInfo();
            ephemCalcInfo.readFromParcel(parcel);
            return ephemCalcInfo;
        }

        @Override // android.os.Parcelable.Creator
        public EphemCalcInfo[] newArray(int i) {
            return new EphemCalcInfo[i];
        }
    };
    public byte protocolType = 0;
    public byte mode = 0;
    public byte tableLength = 0;
    public byte timeStart = 0;
    public int timeStep = 0;
    public int carrierFreq = 0;

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
        parcel.writeByte(this.protocolType);
        parcel.writeByte(this.mode);
        parcel.writeByte(this.tableLength);
        parcel.writeByte(this.timeStart);
        parcel.writeInt(this.timeStep);
        parcel.writeInt(this.carrierFreq);
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
                this.protocolType = parcel.readByte();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.mode = parcel.readByte();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.tableLength = parcel.readByte();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.timeStart = parcel.readByte();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.timeStep = parcel.readInt();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.carrierFreq = parcel.readInt();
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
