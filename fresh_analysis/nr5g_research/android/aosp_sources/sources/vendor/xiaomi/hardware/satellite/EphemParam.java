package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class EphemParam implements Parcelable {
    public static final Parcelable.Creator<EphemParam> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.EphemParam.1
        @Override // android.os.Parcelable.Creator
        public EphemParam createFromParcel(Parcel parcel) {
            EphemParam ephemParam = new EphemParam();
            ephemParam.readFromParcel(parcel);
            return ephemParam;
        }

        @Override // android.os.Parcelable.Creator
        public EphemParam[] newArray(int i) {
            return new EphemParam[i];
        }
    };
    public int toe = 0;
    public int fitAreLength = 0;
    public int semiMajorAxisOffset = 0;
    public int eccentricity = 0;
    public int inclinationOffset = 0;
    public int rateOfRightAicendingNode = 0;
    public int argumentOfPeriapsis = 0;
    public int longitudeOfAscendingNode = 0;
    public int meanAnomaly = 0;

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
        parcel.writeInt(this.toe);
        parcel.writeInt(this.fitAreLength);
        parcel.writeInt(this.semiMajorAxisOffset);
        parcel.writeInt(this.eccentricity);
        parcel.writeInt(this.inclinationOffset);
        parcel.writeInt(this.rateOfRightAicendingNode);
        parcel.writeInt(this.argumentOfPeriapsis);
        parcel.writeInt(this.longitudeOfAscendingNode);
        parcel.writeInt(this.meanAnomaly);
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
                this.toe = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.fitAreLength = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.semiMajorAxisOffset = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.eccentricity = parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.inclinationOffset = parcel.readInt();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.rateOfRightAicendingNode = parcel.readInt();
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.argumentOfPeriapsis = parcel.readInt();
                                        if (parcel.dataPosition() - iDataPosition < i) {
                                            this.longitudeOfAscendingNode = parcel.readInt();
                                            if (parcel.dataPosition() - iDataPosition < i) {
                                                this.meanAnomaly = parcel.readInt();
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
