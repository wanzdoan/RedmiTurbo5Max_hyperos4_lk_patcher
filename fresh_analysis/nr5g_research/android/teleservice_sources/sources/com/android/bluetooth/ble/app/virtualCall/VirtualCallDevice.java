package com.android.bluetooth.ble.app.virtualCall;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class VirtualCallDevice implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.android.bluetooth.ble.app.virtualCall.VirtualCallDevice.1
        @Override // android.os.Parcelable.Creator
        public VirtualCallDevice createFromParcel(Parcel parcel) {
            return new VirtualCallDevice(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public VirtualCallDevice[] newArray(int i) {
            return new VirtualCallDevice[i];
        }
    };
    private String mBleAddr;
    private int mEncryption;
    private int mFeature;
    private String mPhoneNum1;
    private String mPhoneNum2;
    private int mRssi;
    private int mVersion;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected VirtualCallDevice(Parcel parcel) {
        this.mPhoneNum1 = parcel.readString();
        this.mPhoneNum2 = parcel.readString();
        this.mBleAddr = parcel.readString();
        this.mRssi = parcel.readInt();
        this.mVersion = parcel.readInt();
        this.mFeature = parcel.readInt();
        this.mEncryption = parcel.readInt();
    }

    public VirtualCallDevice(String str, String str2, String str3, int i, int i2, int i3, int i4) {
        this.mPhoneNum1 = str;
        this.mPhoneNum2 = str2;
        this.mBleAddr = str3;
        this.mRssi = i;
        this.mVersion = i2;
        this.mFeature = i3;
        this.mEncryption = i4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mPhoneNum1);
        parcel.writeString(this.mPhoneNum2);
        parcel.writeString(this.mBleAddr);
        parcel.writeInt(this.mRssi);
        parcel.writeInt(this.mVersion);
        parcel.writeInt(this.mFeature);
        parcel.writeInt(this.mEncryption);
    }

    public String getBleAddr() {
        return this.mBleAddr;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public String getPhoneNum1() {
        return this.mPhoneNum1;
    }

    public String getPhoneNum2() {
        return this.mPhoneNum2;
    }
}
