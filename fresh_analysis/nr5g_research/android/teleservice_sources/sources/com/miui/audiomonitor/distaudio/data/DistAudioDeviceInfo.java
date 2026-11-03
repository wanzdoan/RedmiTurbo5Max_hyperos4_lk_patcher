package com.miui.audiomonitor.distaudio.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class DistAudioDeviceInfo implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.miui.audiomonitor.distaudio.data.DistAudioDeviceInfo.1
        @Override // android.os.Parcelable.Creator
        public DistAudioDeviceInfo createFromParcel(Parcel parcel) {
            return new DistAudioDeviceInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public DistAudioDeviceInfo[] newArray(int i) {
            return new DistAudioDeviceInfo[i];
        }
    };
    private final String mDeviceId;
    private String mDeviceName;
    private final int mDeviceType;
    public List mHwAttrs = new ArrayList();

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected DistAudioDeviceInfo(Parcel parcel) {
        this.mDeviceId = parcel.readString();
        this.mDeviceName = parcel.readString();
        this.mDeviceType = parcel.readInt();
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mDeviceId);
        parcel.writeString(this.mDeviceName);
        parcel.writeInt(this.mDeviceType);
    }

    public String toString() {
        return "DeviceInfo{deviceId='" + this.mDeviceId + "', deviceName='" + this.mDeviceName + "', deviceType=" + this.mDeviceType + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DistAudioDeviceInfo)) {
            return false;
        }
        DistAudioDeviceInfo distAudioDeviceInfo = (DistAudioDeviceInfo) obj;
        return this.mDeviceType == distAudioDeviceInfo.mDeviceType && this.mDeviceId.equals(distAudioDeviceInfo.mDeviceId) && this.mDeviceName.equals(distAudioDeviceInfo.mDeviceName);
    }

    public int hashCode() {
        return Objects.hash(this.mDeviceId, this.mDeviceName, Integer.valueOf(this.mDeviceType));
    }
}
