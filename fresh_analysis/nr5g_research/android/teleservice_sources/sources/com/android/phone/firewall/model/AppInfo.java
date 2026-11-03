package com.android.phone.firewall.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class AppInfo implements Cloneable, Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.phone.firewall.model.AppInfo.1
        @Override // android.os.Parcelable.Creator
        public AppInfo createFromParcel(Parcel parcel) {
            return new AppInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public AppInfo[] newArray(int i) {
            return new AppInfo[i];
        }
    };
    public boolean isSystemApp;
    public CharSequence packageName;
    public int tagId;
    public int uid;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public AppInfo() {
    }

    private AppInfo(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.uid = parcel.readInt();
        this.tagId = parcel.readInt();
        this.packageName = parcel.readString();
        this.isSystemApp = parcel.readInt() == 1;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.uid);
        parcel.writeInt(this.tagId);
        CharSequence charSequence = this.packageName;
        parcel.writeString(charSequence == null ? null : charSequence.toString());
        parcel.writeInt(this.isSystemApp ? 1 : 0);
    }

    public boolean equals(Object obj) {
        if (obj instanceof AppInfo) {
            AppInfo appInfo = (AppInfo) obj;
            if (this.uid == appInfo.uid && this.packageName.equals(appInfo.packageName)) {
                return true;
            }
        }
        return false;
    }
}
