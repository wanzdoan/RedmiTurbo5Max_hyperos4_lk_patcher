package vendor.qti.latencyaidlservice;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import vendor.xiaomi.modem.qms.PlmnIdS$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public class Config implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.qti.latencyaidlservice.Config.1
        @Override // android.os.Parcelable.Creator
        public Config createFromParcel(Parcel parcel) {
            Config config = new Config();
            config.readFromParcel(parcel);
            return config;
        }

        @Override // android.os.Parcelable.Creator
        public Config[] newArray(int i) {
            return new Config[i];
        }
    };
    public long downlink;
    public boolean enableConnectionExtension = false;
    public long rat;
    public long slotId;
    public long uplink;

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
        parcel.writeLong(this.rat);
        parcel.writeLong(this.slotId);
        parcel.writeLong(this.uplink);
        parcel.writeLong(this.downlink);
        parcel.writeBoolean(this.enableConnectionExtension);
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
                this.rat = parcel.readLong();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.slotId = parcel.readLong();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.uplink = parcel.readLong();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.downlink = parcel.readLong();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.enableConnectionExtension = parcel.readBoolean();
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
}
