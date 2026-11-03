package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class DataProfileInfo implements Parcelable {
    public static final Parcelable.Creator<DataProfileInfo> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.DataProfileInfo.1
        @Override // android.os.Parcelable.Creator
        public DataProfileInfo createFromParcel(Parcel parcel) {
            DataProfileInfo dataProfileInfo = new DataProfileInfo();
            dataProfileInfo.readFromParcel(parcel);
            return dataProfileInfo;
        }

        @Override // android.os.Parcelable.Creator
        public DataProfileInfo[] newArray(int i) {
            return new DataProfileInfo[i];
        }
    };
    public String apn;
    public String mvnoMatchData;
    public int mvnoType;
    public String password;
    public String protocol;
    public String roamingProtocol;
    public String user;
    public int profileId = 0;
    public int authType = 0;
    public int type = 0;
    public int maxConnsTime = 0;
    public int maxConns = 0;
    public int waitTime = 0;
    public int enabled = 0;
    public int supportedTypesBitmask = 0;
    public int bearerBitmask = 0;
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
        parcel.writeInt(this.profileId);
        parcel.writeString(this.apn);
        parcel.writeString(this.protocol);
        parcel.writeString(this.roamingProtocol);
        parcel.writeInt(this.authType);
        parcel.writeString(this.user);
        parcel.writeString(this.password);
        parcel.writeInt(this.type);
        parcel.writeInt(this.maxConnsTime);
        parcel.writeInt(this.maxConns);
        parcel.writeInt(this.waitTime);
        parcel.writeInt(this.enabled);
        parcel.writeInt(this.supportedTypesBitmask);
        parcel.writeInt(this.bearerBitmask);
        parcel.writeInt(this.mtu);
        parcel.writeInt(this.mvnoType);
        parcel.writeString(this.mvnoMatchData);
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
                this.profileId = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.apn = parcel.readString();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.protocol = parcel.readString();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.roamingProtocol = parcel.readString();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.authType = parcel.readInt();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.user = parcel.readString();
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.password = parcel.readString();
                                        if (parcel.dataPosition() - iDataPosition < i) {
                                            this.type = parcel.readInt();
                                            if (parcel.dataPosition() - iDataPosition < i) {
                                                this.maxConnsTime = parcel.readInt();
                                                if (parcel.dataPosition() - iDataPosition < i) {
                                                    this.maxConns = parcel.readInt();
                                                    if (parcel.dataPosition() - iDataPosition < i) {
                                                        this.waitTime = parcel.readInt();
                                                        if (parcel.dataPosition() - iDataPosition < i) {
                                                            this.enabled = parcel.readInt();
                                                            if (parcel.dataPosition() - iDataPosition < i) {
                                                                this.supportedTypesBitmask = parcel.readInt();
                                                                if (parcel.dataPosition() - iDataPosition < i) {
                                                                    this.bearerBitmask = parcel.readInt();
                                                                    if (parcel.dataPosition() - iDataPosition < i) {
                                                                        this.mtu = parcel.readInt();
                                                                        if (parcel.dataPosition() - iDataPosition < i) {
                                                                            this.mvnoType = parcel.readInt();
                                                                            if (parcel.dataPosition() - iDataPosition < i) {
                                                                                this.mvnoMatchData = parcel.readString();
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
