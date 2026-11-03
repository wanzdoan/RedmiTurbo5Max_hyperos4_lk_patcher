package com.xiaomi.mirror;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class ScreenInfo implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.xiaomi.mirror.ScreenInfo.1
        @Override // android.os.Parcelable.Creator
        public ScreenInfo createFromParcel(Parcel parcel) {
            return new ScreenInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public ScreenInfo[] newArray(int i) {
            return new ScreenInfo[0];
        }
    };
    public static String KEY_IS_FOREGROUND = "is_foreground";
    public static String KEY_PKG_NAME = "pkg_name";
    public static String KEY_SCREEN_ID = "screen_id";
    private Bundle bundle;

    public ScreenInfo() {
        this.bundle = new Bundle();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public String getPkgName() {
        return this.bundle.getString(KEY_PKG_NAME);
    }

    public int getScreenId() {
        return this.bundle.getInt(KEY_SCREEN_ID);
    }

    public boolean isForeground() {
        return this.bundle.getBoolean(KEY_IS_FOREGROUND);
    }

    public String toString() {
        return "ScreenInfo{bundle=" + this.bundle + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.bundle);
    }

    public ScreenInfo(Parcel parcel) {
        this.bundle = new Bundle();
        this.bundle = parcel.readBundle(getClass().getClassLoader());
    }
}
