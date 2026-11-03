package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class PreferredCellInfoS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.PreferredCellInfoS.1
        @Override // android.os.Parcelable.Creator
        public PreferredCellInfoS createFromParcel(Parcel parcel) {
            PreferredCellInfoS preferredCellInfoS = new PreferredCellInfoS();
            preferredCellInfoS.readFromParcel(parcel);
            return preferredCellInfoS;
        }

        @Override // android.os.Parcelable.Creator
        public PreferredCellInfoS[] newArray(int i) {
            return new PreferredCellInfoS[i];
        }
    };
    public LocationS Location;
    public CellInfoS cell_info;
    public int cause_bitmask = 0;
    public boolean priority_valid = false;
    public byte priority = 0;
    public boolean location_valid = false;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeTypedObject(this.cell_info, i);
        parcel.writeInt(this.cause_bitmask);
        parcel.writeBoolean(this.priority_valid);
        parcel.writeByte(this.priority);
        parcel.writeBoolean(this.location_valid);
        parcel.writeTypedObject(this.Location, i);
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
                this.cell_info = (CellInfoS) parcel.readTypedObject(CellInfoS.CREATOR);
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.cause_bitmask = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.priority_valid = parcel.readBoolean();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.priority = parcel.readByte();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.location_valid = parcel.readBoolean();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.Location = (LocationS) parcel.readTypedObject(LocationS.CREATOR);
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
        return describeContents(this.Location) | describeContents(this.cell_info);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
