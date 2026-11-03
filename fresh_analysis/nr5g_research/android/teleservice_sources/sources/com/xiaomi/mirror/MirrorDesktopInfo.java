package com.xiaomi.mirror;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class MirrorDesktopInfo implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.xiaomi.mirror.MirrorDesktopInfo.1
        @Override // android.os.Parcelable.Creator
        public MirrorDesktopInfo createFromParcel(Parcel parcel) {
            return new MirrorDesktopInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public MirrorDesktopInfo[] newArray(int i) {
            return new MirrorDesktopInfo[i];
        }
    };
    static final String KEY_DESKTOP_HEIGHT = "desktop_height";
    static final String KEY_DESKTOP_IS_SHOW = "desktop_is_show";
    static final String KEY_DESKTOP_ORIENTATION = "desktop_orientation";
    static final String KEY_DESKTOP_SEAT = "desktop_seat";
    static final String KEY_DESKTOP_WIDTH = "desktop_width";
    private Bundle mBundle;

    public MirrorDesktopInfo() {
        this.mBundle = new Bundle();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public int getDesktopHeight() {
        return this.mBundle.getInt(KEY_DESKTOP_HEIGHT);
    }

    public int getDesktopOrientation() {
        return this.mBundle.getInt(KEY_DESKTOP_ORIENTATION);
    }

    public int getDesktopSeat() {
        return this.mBundle.getInt(KEY_DESKTOP_SEAT);
    }

    public int getDesktopWidth() {
        return this.mBundle.getInt(KEY_DESKTOP_WIDTH);
    }

    public boolean isShow() {
        return this.mBundle.getBoolean(KEY_DESKTOP_IS_SHOW);
    }

    public String toString() {
        return "MirrorDesktopInfo{Orientation =" + getDesktopOrientation() + "width =" + getDesktopWidth() + "height =" + getDesktopHeight() + "seat =" + getDesktopSeat() + "isShow =" + isShow() + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mBundle);
    }

    public MirrorDesktopInfo(Parcel parcel) {
        this.mBundle = new Bundle();
        this.mBundle = parcel.readBundle(getClass().getClassLoader());
    }
}
