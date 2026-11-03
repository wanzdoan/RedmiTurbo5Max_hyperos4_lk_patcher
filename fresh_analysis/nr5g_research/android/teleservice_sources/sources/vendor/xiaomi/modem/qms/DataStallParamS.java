package vendor.xiaomi.modem.qms;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes2.dex */
public class DataStallParamS implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.modem.qms.DataStallParamS.1
        @Override // android.os.Parcelable.Creator
        public DataStallParamS createFromParcel(Parcel parcel) {
            DataStallParamS dataStallParamS = new DataStallParamS();
            dataStallParamS.readFromParcel(parcel);
            return dataStallParamS;
        }

        @Override // android.os.Parcelable.Creator
        public DataStallParamS[] newArray(int i) {
            return new DataStallParamS[i];
        }
    };
    public int appType;
    public int trafficDirection;
    public boolean dataStallStatus = false;
    public int expectedIpPacketCount = 0;
    public int expectedDataRate = 0;
    public int lostIpPacketCount = 0;
    public int measurementInterval = 0;

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
        parcel.writeInt(this.trafficDirection);
        parcel.writeBoolean(this.dataStallStatus);
        parcel.writeInt(this.appType);
        parcel.writeInt(this.expectedIpPacketCount);
        parcel.writeInt(this.expectedDataRate);
        parcel.writeInt(this.lostIpPacketCount);
        parcel.writeInt(this.measurementInterval);
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
                this.trafficDirection = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.dataStallStatus = parcel.readBoolean();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.appType = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.expectedIpPacketCount = parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.expectedDataRate = parcel.readInt();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.lostIpPacketCount = parcel.readInt();
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.measurementInterval = parcel.readInt();
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
