package vendor.xiaomi.hardware.satellite;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class JS_TXPWR implements Parcelable {
    public static final Parcelable.Creator<JS_TXPWR> CREATOR = new Parcelable.Creator() { // from class: vendor.xiaomi.hardware.satellite.JS_TXPWR.1
        @Override // android.os.Parcelable.Creator
        public JS_TXPWR createFromParcel(Parcel parcel) {
            JS_TXPWR js_txpwr = new JS_TXPWR();
            js_txpwr.readFromParcel(parcel);
            return js_txpwr;
        }

        @Override // android.os.Parcelable.Creator
        public JS_TXPWR[] newArray(int i) {
            return new JS_TXPWR[i];
        }
    };
    public int tarTxPwr = 0;
    public int realTxPwr = 0;

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
        parcel.writeInt(this.tarTxPwr);
        parcel.writeInt(this.realTxPwr);
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
                this.tarTxPwr = parcel.readInt();
                if (parcel.dataPosition() - iDataPosition < i) {
                    this.realTxPwr = parcel.readInt();
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
