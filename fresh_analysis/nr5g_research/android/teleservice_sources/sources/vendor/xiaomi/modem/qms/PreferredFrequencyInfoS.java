package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class PreferredFrequencyInfoS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.PreferredFrequencyInfoS.1
        @Override // android.os.Parcelable.Creator
        public PreferredFrequencyInfoS createFromParcel(Parcel parcel) {
            PreferredFrequencyInfoS preferredFrequencyInfoS = new PreferredFrequencyInfoS();
            preferredFrequencyInfoS.readFromParcel(parcel);
            return preferredFrequencyInfoS;
        }

        @Override // android.os.Parcelable.Creator
        public PreferredFrequencyInfoS[] newArray(int i) {
            return new PreferredFrequencyInfoS[i];
        }
    };
    public int band;
    public int bandwidth;
    public PlmnIdS[] plmn;
    public int freq = 0;
    public byte plmn_list_len = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.freq);
        parcel.writeInt(this.band);
        parcel.writeInt(this.bandwidth);
        parcel.writeByte(this.plmn_list_len);
        parcel.writeTypedArray(this.plmn, i);
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
                this.freq = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.band = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.bandwidth = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.plmn_list_len = parcel.readByte();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.plmn = (PlmnIdS[]) parcel.createTypedArray(PlmnIdS.CREATOR);
                                if (iDataPosition > Integer.MAX_VALUE - i) {
                                    PlmnIdS$$ExternalSyntheticBUOutline0.m();
                                    return;
                                }
                            } else if (iDataPosition > Integer.MAX_VALUE - i) {
                                PlmnIdS$$ExternalSyntheticBUOutline0.m();
                                return;
                            }
                        } else if (iDataPosition > Integer.MAX_VALUE - i) {
                            PlmnIdS$$ExternalSyntheticBUOutline0.m();
                            return;
                        }
                    } else if (iDataPosition > Integer.MAX_VALUE - i) {
                        PlmnIdS$$ExternalSyntheticBUOutline0.m();
                        return;
                    }
                } else if (iDataPosition > Integer.MAX_VALUE - i) {
                    PlmnIdS$$ExternalSyntheticBUOutline0.m();
                    return;
                }
            } else if (iDataPosition > Integer.MAX_VALUE - i) {
                PlmnIdS$$ExternalSyntheticBUOutline0.m();
                return;
            }
            parcel.setDataPosition(iDataPosition + i);
        } catch (Throwable th) {
            if (iDataPosition > Integer.MAX_VALUE - i) {
                PlmnIdS$$ExternalSyntheticBUOutline0.m();
            } else {
                parcel.setDataPosition(iDataPosition + i);
                throw th;
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return describeContents(this.plmn);
    }

    private int describeContents(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Object[]) {
            int iDescribeContents = 0;
            for (Object obj2 : (Object[]) obj) {
                iDescribeContents |= describeContents(obj2);
            }
            return iDescribeContents;
        }
        if (obj instanceof Parcelable) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
