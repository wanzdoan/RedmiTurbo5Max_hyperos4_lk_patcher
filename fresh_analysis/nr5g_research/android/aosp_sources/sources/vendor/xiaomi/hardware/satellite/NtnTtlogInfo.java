package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class NtnTtlogInfo implements Parcelable {
    public static final Parcelable.Creator<NtnTtlogInfo> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.NtnTtlogInfo.1
        @Override // android.os.Parcelable.Creator
        public NtnTtlogInfo createFromParcel(Parcel parcel) {
            NtnTtlogInfo ntnTtlogInfo = new NtnTtlogInfo();
            ntnTtlogInfo.readFromParcel(parcel);
            return ntnTtlogInfo;
        }

        @Override // android.os.Parcelable.Creator
        public NtnTtlogInfo[] newArray(int i) {
            return new NtnTtlogInfo[i];
        }
    };
    public int sat_id = 0;
    public int rTotal = 0;
    public int rError = 0;
    public int foffset = 0;
    public int tx_valid = 0;
    public int txp_setted = 0;
    public int txp_real = 0;
    public int tempeture = 0;

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
        parcel.writeInt(this.sat_id);
        parcel.writeInt(this.rTotal);
        parcel.writeInt(this.rError);
        parcel.writeInt(this.foffset);
        parcel.writeInt(this.tx_valid);
        parcel.writeInt(this.txp_setted);
        parcel.writeInt(this.txp_real);
        parcel.writeInt(this.tempeture);
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
                this.sat_id = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.rTotal = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.rError = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.foffset = parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.tx_valid = parcel.readInt();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.txp_setted = parcel.readInt();
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.txp_real = parcel.readInt();
                                        if (parcel.dataPosition() - iDataPosition < i) {
                                            this.tempeture = parcel.readInt();
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
