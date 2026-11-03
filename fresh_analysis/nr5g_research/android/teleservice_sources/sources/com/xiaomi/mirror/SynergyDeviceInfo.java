package com.xiaomi.mirror;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class SynergyDeviceInfo implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.xiaomi.mirror.SynergyDeviceInfo.1
        @Override // android.os.Parcelable.Creator
        public SynergyDeviceInfo createFromParcel(Parcel parcel) {
            return new SynergyDeviceInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SynergyDeviceInfo[] newArray(int i) {
            return new SynergyDeviceInfo[i];
        }
    };
    static final String KEY_DISPLAY_NAME = "display_name";
    static final String KEY_ID = "id";
    private Bundle mBundle;

    public SynergyDeviceInfo() {
        this.mBundle = new Bundle();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Bundle getBundle() {
        return new Bundle(this.mBundle);
    }

    public String getDeviceId() {
        return this.mBundle.getString(KEY_ID);
    }

    public String getDisplayName() {
        return this.mBundle.getString(KEY_DISPLAY_NAME);
    }

    public String toString() {
        return "SynergyDeviceInfo{mBundle=" + this.mBundle + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mBundle);
    }

    public SynergyDeviceInfo(Parcel parcel) {
        this.mBundle = new Bundle();
        this.mBundle = parcel.readBundle(getClass().getClassLoader());
    }
}
