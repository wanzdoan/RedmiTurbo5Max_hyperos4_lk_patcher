package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class AppStatus implements Parcelable {
    public static final Parcelable.Creator<AppStatus> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.AppStatus.1
        @Override // android.os.Parcelable.Creator
        public AppStatus createFromParcel(Parcel parcel) {
            AppStatus appStatus = new AppStatus();
            appStatus.readFromParcel(parcel);
            return appStatus;
        }

        @Override // android.os.Parcelable.Creator
        public AppStatus[] newArray(int i) {
            return new AppStatus[i];
        }
    };
    public String aidPtr;
    public String appLabelPtr;
    public int appState;
    public int appType;
    public int persoSubstate;
    public int pin1;
    public int pin1Replaced = 0;
    public int pin2;

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
        parcel.writeInt(this.appType);
        parcel.writeInt(this.appState);
        parcel.writeInt(this.persoSubstate);
        parcel.writeString(this.aidPtr);
        parcel.writeString(this.appLabelPtr);
        parcel.writeInt(this.pin1Replaced);
        parcel.writeInt(this.pin1);
        parcel.writeInt(this.pin2);
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
                this.appType = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.appState = parcel.readInt();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.persoSubstate = parcel.readInt();
                        if (parcel.dataPosition() - iDataPosition < i) {
                            this.aidPtr = parcel.readString();
                            if (parcel.dataPosition() - iDataPosition < i) {
                                this.appLabelPtr = parcel.readString();
                                if (parcel.dataPosition() - iDataPosition < i) {
                                    this.pin1Replaced = parcel.readInt();
                                    if (parcel.dataPosition() - iDataPosition < i) {
                                        this.pin1 = parcel.readInt();
                                        if (parcel.dataPosition() - iDataPosition < i) {
                                            this.pin2 = parcel.readInt();
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
}
