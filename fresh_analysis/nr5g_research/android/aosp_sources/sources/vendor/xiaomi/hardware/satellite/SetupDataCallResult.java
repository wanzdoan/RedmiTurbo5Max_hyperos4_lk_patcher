package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class SetupDataCallResult implements Parcelable {
    public static final Parcelable.Creator<SetupDataCallResult> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.SetupDataCallResult.1
        @Override // android.os.Parcelable.Creator
        public SetupDataCallResult createFromParcel(Parcel parcel) {
            SetupDataCallResult setupDataCallResult = new SetupDataCallResult();
            setupDataCallResult.readFromParcel(parcel);
            return setupDataCallResult;
        }

        @Override // android.os.Parcelable.Creator
        public SetupDataCallResult[] newArray(int i) {
            return new SetupDataCallResult[i];
        }
    };
    public String addresses;
    public String dnses;
    public String gateways;
    public String ifname;
    public String pcscf;
    public int status;
    public String type;
    public int suggestedRetryTime = 0;
    public int cid = 0;
    public int active = 0;
    public int mtu = 0;

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
        parcel.writeInt(this.status);
        parcel.writeInt(this.suggestedRetryTime);
        parcel.writeInt(this.cid);
        parcel.writeInt(this.active);
        parcel.writeString(this.type);
        parcel.writeString(this.ifname);
        parcel.writeString(this.addresses);
        parcel.writeString(this.dnses);
        parcel.writeString(this.gateways);
        parcel.writeString(this.pcscf);
        parcel.writeInt(this.mtu);
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
                this.status = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.suggestedRetryTime = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.cid = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.active = parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.type = parcel.readString();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.ifname = parcel.readString();
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.addresses = parcel.readString();
                                        if (parcel.dataPosition() - iDataPosition < i) {
                                            this.dnses = parcel.readString();
                                            if (parcel.dataPosition() - iDataPosition < i) {
                                                this.gateways = parcel.readString();
                                                if (parcel.dataPosition() - iDataPosition < i) {
                                                    this.pcscf = parcel.readString();
                                                    if (parcel.dataPosition() - iDataPosition < i) {
                                                        this.mtu = parcel.readInt();
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
