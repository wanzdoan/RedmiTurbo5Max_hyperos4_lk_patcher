package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class VoiceRegStateResult implements Parcelable {
    public static final Parcelable.Creator<VoiceRegStateResult> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.VoiceRegStateResult.1
        @Override // android.os.Parcelable.Creator
        public VoiceRegStateResult createFromParcel(Parcel parcel) {
            VoiceRegStateResult voiceRegStateResult = new VoiceRegStateResult();
            voiceRegStateResult.readFromParcel(parcel);
            return voiceRegStateResult;
        }

        @Override // android.os.Parcelable.Creator
        public VoiceRegStateResult[] newArray(int i) {
            return new VoiceRegStateResult[i];
        }
    };
    public CellIdentity cellIdentity;
    public int regState;
    public int rat = 0;
    public boolean cssSupported = false;
    public int roamingIndicator = 0;
    public int systemIsInPrl = 0;
    public int defaultRoamingIndicator = 0;
    public int reasonForDenial = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.regState);
        parcel.writeInt(this.rat);
        parcel.writeBoolean(this.cssSupported);
        parcel.writeInt(this.roamingIndicator);
        parcel.writeInt(this.systemIsInPrl);
        parcel.writeInt(this.defaultRoamingIndicator);
        parcel.writeInt(this.reasonForDenial);
        parcel.writeTypedObject(this.cellIdentity, i);
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
                this.regState = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.rat = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.cssSupported = parcel.readBoolean();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.roamingIndicator = parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.systemIsInPrl = parcel.readInt();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.defaultRoamingIndicator = parcel.readInt();
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.reasonForDenial = parcel.readInt();
                                        if (parcel.dataPosition() - iDataPosition < i) {
                                            this.cellIdentity = (CellIdentity) parcel.readTypedObject(CellIdentity.CREATOR);
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return describeContents(this.cellIdentity);
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
