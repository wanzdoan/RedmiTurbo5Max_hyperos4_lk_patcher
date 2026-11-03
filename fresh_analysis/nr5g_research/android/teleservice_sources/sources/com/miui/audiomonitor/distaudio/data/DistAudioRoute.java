package com.miui.audiomonitor.distaudio.data;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class DistAudioRoute implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.miui.audiomonitor.distaudio.data.DistAudioRoute.1
        @Override // android.os.Parcelable.Creator
        public DistAudioRoute createFromParcel(Parcel parcel) {
            return new DistAudioRoute(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public DistAudioRoute[] newArray(int i) {
            return new DistAudioRoute[i];
        }
    };
    private final AudioAttributes mAttributes;
    private final DistAudioDeviceInfo mDeviceInfo;
    private final AudioFormat mPlayerFormat;
    private final AudioFormat mRecorderFormat;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private DistAudioRoute(Builder builder) {
        this.mDeviceInfo = builder.deviceInfo;
        this.mAttributes = builder.attributes;
        this.mPlayerFormat = builder.playerFormat;
        this.mRecorderFormat = builder.recorderFormat;
    }

    protected DistAudioRoute(Parcel parcel) {
        this.mDeviceInfo = (DistAudioDeviceInfo) parcel.readParcelable(DistAudioDeviceInfo.class.getClassLoader());
        this.mAttributes = (AudioAttributes) parcel.readParcelable(AudioAttributes.class.getClassLoader());
        this.mPlayerFormat = (AudioFormat) parcel.readParcelable(AudioFormat.class.getClassLoader());
        this.mRecorderFormat = (AudioFormat) parcel.readParcelable(AudioFormat.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mDeviceInfo, i);
        parcel.writeParcelable(this.mAttributes, i);
        parcel.writeParcelable(this.mPlayerFormat, i);
        parcel.writeParcelable(this.mRecorderFormat, i);
    }

    public DistAudioDeviceInfo getDeviceInfo() {
        return this.mDeviceInfo;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DistAudioRoute) {
            return this.mDeviceInfo.equals(((DistAudioRoute) obj).mDeviceInfo);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mDeviceInfo);
    }

    public class Builder {
        private AudioAttributes attributes;
        private DistAudioDeviceInfo deviceInfo;
        private AudioFormat playerFormat;
        private AudioFormat recorderFormat;

        public Builder setDistAudioDeviceInfo(DistAudioDeviceInfo distAudioDeviceInfo) {
            this.deviceInfo = distAudioDeviceInfo;
            return this;
        }

        public Builder setAudioAttributes(AudioAttributes audioAttributes) {
            this.attributes = audioAttributes;
            return this;
        }

        public Builder setPlayerFormat(AudioFormat audioFormat) {
            this.playerFormat = audioFormat;
            return this;
        }

        public Builder setRecorderFormat(AudioFormat audioFormat) {
            this.recorderFormat = audioFormat;
            return this;
        }

        public DistAudioRoute build() {
            return new DistAudioRoute(this);
        }
    }
}
