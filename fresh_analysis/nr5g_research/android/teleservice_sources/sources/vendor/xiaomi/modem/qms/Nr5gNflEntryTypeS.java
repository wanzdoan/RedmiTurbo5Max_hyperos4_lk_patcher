package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class Nr5gNflEntryTypeS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.Nr5gNflEntryTypeS.1
        @Override // android.os.Parcelable.Creator
        public Nr5gNflEntryTypeS createFromParcel(Parcel parcel) {
            Nr5gNflEntryTypeS nr5gNflEntryTypeS = new Nr5gNflEntryTypeS();
            nr5gNflEntryTypeS.readFromParcel(parcel);
            return nr5gNflEntryTypeS;
        }

        @Override // android.os.Parcelable.Creator
        public Nr5gNflEntryTypeS[] newArray(int i) {
            return new Nr5gNflEntryTypeS[i];
        }
    };
    public int activeBand;
    public PlmnIdS[] plmnIdArray;
    public int scsType;
    public int arfcn = 0;
    public char plmnIdArrayLength = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.arfcn);
        parcel.writeInt(this.activeBand);
        parcel.writeInt(this.scsType);
        parcel.writeInt(this.plmnIdArrayLength);
        parcel.writeTypedArray(this.plmnIdArray, i);
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
                this.arfcn = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.activeBand = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.scsType = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.plmnIdArrayLength = (char) parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.plmnIdArray = (PlmnIdS[]) parcel.createTypedArray(PlmnIdS.CREATOR);
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
        return describeContents(this.plmnIdArray);
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
