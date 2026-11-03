package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class SendSmsResult implements Parcelable {
    public static final Parcelable.Creator<SendSmsResult> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.SendSmsResult.1
        @Override // android.os.Parcelable.Creator
        public SendSmsResult createFromParcel(Parcel parcel) {
            SendSmsResult sendSmsResult = new SendSmsResult();
            sendSmsResult.readFromParcel(parcel);
            return sendSmsResult;
        }

        @Override // android.os.Parcelable.Creator
        public SendSmsResult[] newArray(int i) {
            return new SendSmsResult[i];
        }
    };
    public String ackPDU;
    public int messageRef = 0;
    public int errorCode = 0;

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
        parcel.writeInt(this.messageRef);
        parcel.writeString(this.ackPDU);
        parcel.writeInt(this.errorCode);
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
                this.messageRef = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.ackPDU = parcel.readString();
                    if (parcel.dataPosition() - iDataPosition < i) {
                        this.errorCode = parcel.readInt();
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
