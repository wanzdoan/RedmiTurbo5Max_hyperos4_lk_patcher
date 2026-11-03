package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class CellIdentityNtn implements Parcelable {
    public static final Parcelable.Creator<CellIdentityNtn> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.CellIdentityNtn.1
        @Override // android.os.Parcelable.Creator
        public CellIdentityNtn createFromParcel(Parcel parcel) {
            CellIdentityNtn cellIdentityNtn = new CellIdentityNtn();
            cellIdentityNtn.readFromParcel(parcel);
            return cellIdentityNtn;
        }

        @Override // android.os.Parcelable.Creator
        public CellIdentityNtn[] newArray(int i) {
            return new CellIdentityNtn[i];
        }
    };
    public String mcc;
    public String mnc;
    public int lac = 0;
    public int cid = 0;
    public int arfcn = 0;
    public int beamid = 0;
    public int bmlong = 0;
    public int bmlat = 0;
    public byte bsic = 0;

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
        parcel.writeString(this.mcc);
        parcel.writeString(this.mnc);
        parcel.writeInt(this.lac);
        parcel.writeInt(this.cid);
        parcel.writeInt(this.arfcn);
        parcel.writeInt(this.beamid);
        parcel.writeInt(this.bmlong);
        parcel.writeInt(this.bmlat);
        parcel.writeByte(this.bsic);
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
                this.mcc = parcel.readString();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.mnc = parcel.readString();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.lac = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.cid = parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.arfcn = parcel.readInt();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.beamid = parcel.readInt();
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.bmlong = parcel.readInt();
                                        if (parcel.dataPosition() - iDataPosition < i) {
                                            this.bmlat = parcel.readInt();
                                            if (parcel.dataPosition() - iDataPosition < i) {
                                                this.bsic = parcel.readByte();
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
