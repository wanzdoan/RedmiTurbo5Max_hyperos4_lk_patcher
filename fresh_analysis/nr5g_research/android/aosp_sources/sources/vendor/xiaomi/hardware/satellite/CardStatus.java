package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class CardStatus implements Parcelable {
    public static final Parcelable.Creator<CardStatus> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.CardStatus.1
        @Override // android.os.Parcelable.Creator
        public CardStatus createFromParcel(Parcel parcel) {
            CardStatus cardStatus = new CardStatus();
            cardStatus.readFromParcel(parcel);
            return cardStatus;
        }

        @Override // android.os.Parcelable.Creator
        public CardStatus[] newArray(int i) {
            return new CardStatus[i];
        }
    };
    public AppStatus[] applications;
    public String atr;
    public int cardState;
    public String iccid;
    public int universalPinState;
    public int gsmUmtsSubscriptionAppIndex = 0;
    public int cdmaSubscriptionAppIndex = 0;
    public int imsSubscriptionAppIndex = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.cardState);
        parcel.writeInt(this.universalPinState);
        parcel.writeInt(this.gsmUmtsSubscriptionAppIndex);
        parcel.writeInt(this.cdmaSubscriptionAppIndex);
        parcel.writeInt(this.imsSubscriptionAppIndex);
        parcel.writeTypedArray(this.applications, i);
        parcel.writeString(this.iccid);
        parcel.writeString(this.atr);
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
                this.cardState = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.universalPinState = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.gsmUmtsSubscriptionAppIndex = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.cdmaSubscriptionAppIndex = parcel.readInt();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.imsSubscriptionAppIndex = parcel.readInt();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.applications = (AppStatus[]) parcel.createTypedArray(AppStatus.CREATOR);
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.iccid = parcel.readString();
                                        if (parcel.dataPosition() - iDataPosition < i) {
                                            this.atr = parcel.readString();
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
        return describeContents(this.applications);
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
