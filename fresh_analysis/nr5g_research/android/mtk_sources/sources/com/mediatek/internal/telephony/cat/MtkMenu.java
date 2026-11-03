package com.mediatek.internal.telephony.cat;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.telephony.cat.Menu;

/* JADX INFO: loaded from: classes.dex */
public class MtkMenu extends Menu {
    public static final Parcelable.Creator<MtkMenu> CREATOR = new Parcelable.Creator<MtkMenu>() { // from class: com.mediatek.internal.telephony.cat.MtkMenu.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MtkMenu createFromParcel(Parcel in) {
            return new MtkMenu(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MtkMenu[] newArray(int size) {
            return new MtkMenu[size];
        }
    };
    public byte[] nextActionIndicator;

    public MtkMenu() {
        this.nextActionIndicator = null;
    }

    private MtkMenu(Parcel in) {
        super(in);
        int naiLen = in.readInt();
        if (naiLen <= 0) {
            this.nextActionIndicator = null;
        } else {
            this.nextActionIndicator = new byte[naiLen];
            in.readByteArray(this.nextActionIndicator);
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.nextActionIndicator == null ? -1 : this.nextActionIndicator.length);
        if (this.nextActionIndicator != null && this.nextActionIndicator.length > 0) {
            dest.writeByteArray(this.nextActionIndicator);
        }
    }
}
