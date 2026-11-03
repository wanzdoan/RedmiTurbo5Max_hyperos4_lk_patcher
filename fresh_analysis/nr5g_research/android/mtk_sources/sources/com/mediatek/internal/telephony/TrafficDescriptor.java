package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class TrafficDescriptor implements Parcelable {
    public static final Parcelable.Creator<TrafficDescriptor> CREATOR = new Parcelable.Creator<TrafficDescriptor>() { // from class: com.mediatek.internal.telephony.TrafficDescriptor.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TrafficDescriptor createFromParcel(Parcel in) {
            return new TrafficDescriptor(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TrafficDescriptor[] newArray(int size) {
            return new TrafficDescriptor[size];
        }
    };
    private String connectionCapabilities;
    private String dnn;
    private String domainDescriptors;
    private IpDescriptors ipDescriptors;
    private String osAppId;

    public void setOsAppId(String osAppId) {
        this.osAppId = osAppId;
    }

    public void setIpDescriptors(IpDescriptors ipDescriptors) {
        this.ipDescriptors = ipDescriptors;
    }

    public void setDomainDescriptors(String domainDescriptors) {
        this.domainDescriptors = domainDescriptors;
    }

    public void setDnn(String dnn) {
        this.dnn = dnn;
    }

    public void setConnectionCapabilities(String connectionCapabilities) {
        this.connectionCapabilities = connectionCapabilities;
    }

    public String getOsAppId() {
        return this.osAppId;
    }

    public IpDescriptors getIpDescriptors() {
        return this.ipDescriptors;
    }

    public String getDomainDescriptors() {
        return this.domainDescriptors;
    }

    public String getDnn() {
        return this.dnn;
    }

    public String getConnectionCapabilities() {
        return this.connectionCapabilities;
    }

    public TrafficDescriptor() {
    }

    public TrafficDescriptor(String osAppId, IpDescriptors ipDescriptors, String domainDescriptors, String dnn, String connectionCapabilities) {
        this.osAppId = osAppId;
        this.ipDescriptors = ipDescriptors;
        this.domainDescriptors = domainDescriptors;
        this.dnn = dnn;
        this.connectionCapabilities = connectionCapabilities;
    }

    private TrafficDescriptor(Parcel in) {
        this.osAppId = in.readString();
        this.ipDescriptors = (IpDescriptors) in.readParcelable(Thread.currentThread().getContextClassLoader());
        this.domainDescriptors = in.readString();
        this.dnn = in.readString();
        this.connectionCapabilities = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.osAppId);
        parcel.writeParcelable(this.ipDescriptors, 0);
        parcel.writeString(this.domainDescriptors);
        parcel.writeString(this.dnn);
        parcel.writeString(this.connectionCapabilities);
    }

    public String toString() {
        return "TrafficDescriptor{osAppId='" + this.osAppId + "', ipDescriptors=" + this.ipDescriptors + ", domainDescriptors='" + this.domainDescriptors + "', dnn='" + this.dnn + "', connectionCapabilities='" + this.connectionCapabilities + "'}";
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TrafficDescriptor)) {
            return false;
        }
        TrafficDescriptor trafficDescriptor = (TrafficDescriptor) obj;
        if (!trafficDescriptor.getOsAppId().equals(this.osAppId) || !trafficDescriptor.getConnectionCapabilities().equals(this.connectionCapabilities) || !trafficDescriptor.getIpDescriptors().equals(this.ipDescriptors) || !trafficDescriptor.getDnn().equals(this.dnn) || !trafficDescriptor.getDomainDescriptors().equals(this.domainDescriptors)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.osAppId, this.ipDescriptors, this.domainDescriptors, this.dnn, this.connectionCapabilities);
    }
}
