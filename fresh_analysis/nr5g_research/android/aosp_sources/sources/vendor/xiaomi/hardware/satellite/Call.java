package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class Call implements Parcelable {
    public static final Parcelable.Creator<Call> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.Call.1
        @Override // android.os.Parcelable.Creator
        public Call createFromParcel(Parcel parcel) {
            Call call = new Call();
            call.readFromParcel(parcel);
            return call;
        }

        @Override // android.os.Parcelable.Creator
        public Call[] newArray(int i) {
            return new Call[i];
        }
    };
    public String name;
    public int namePresentation;
    public String number;
    public int numberPresentation;
    public int state;
    public UusInfo[] uusInfo;
    public int index = 0;
    public int toa = 0;
    public boolean isMpty = false;
    public boolean isMT = false;
    public byte als = 0;
    public boolean isVoice = false;
    public boolean isVoicePrivacy = false;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.state);
        parcel.writeInt(this.index);
        parcel.writeInt(this.toa);
        parcel.writeBoolean(this.isMpty);
        parcel.writeBoolean(this.isMT);
        parcel.writeByte(this.als);
        parcel.writeBoolean(this.isVoice);
        parcel.writeBoolean(this.isVoicePrivacy);
        parcel.writeString(this.number);
        parcel.writeInt(this.numberPresentation);
        parcel.writeString(this.name);
        parcel.writeInt(this.namePresentation);
        parcel.writeTypedArray(this.uusInfo, i);
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
                this.state = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.index = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.toa = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.isMpty = parcel.readBoolean();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.isMT = parcel.readBoolean();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.als = parcel.readByte();
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.isVoice = parcel.readBoolean();
                                        if (parcel.dataPosition() - iDataPosition < i) {
                                            this.isVoicePrivacy = parcel.readBoolean();
                                            if (parcel.dataPosition() - iDataPosition < i) {
                                                this.number = parcel.readString();
                                                if (parcel.dataPosition() - iDataPosition < i) {
                                                    this.numberPresentation = parcel.readInt();
                                                    if (parcel.dataPosition() - iDataPosition < i) {
                                                        this.name = parcel.readString();
                                                        if (parcel.dataPosition() - iDataPosition < i) {
                                                            this.namePresentation = parcel.readInt();
                                                            if (parcel.dataPosition() - iDataPosition < i) {
                                                                this.uusInfo = (UusInfo[]) parcel.createTypedArray(UusInfo.CREATOR);
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return describeContents(this.uusInfo);
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
