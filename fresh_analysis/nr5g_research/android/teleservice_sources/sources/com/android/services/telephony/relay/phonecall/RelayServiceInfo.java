package com.android.services.telephony.relay.phonecall;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class RelayServiceInfo {
    public String mDeviceId;
    public String mDeviceName;
    public int mDeviceType;
    public String mPackageName;
    public byte[] mServiceData;
    public String mServiceName;
    public int mVersion;

    public RelayServiceInfo(String str, String str2, byte[] bArr, String str3, String str4, int i, int i2) {
        this.mPackageName = str;
        this.mServiceName = str2;
        this.mServiceData = bArr;
        this.mDeviceId = str3;
        this.mDeviceName = str4;
        this.mDeviceType = i;
        this.mVersion = i2;
    }

    public String toString() {
        return "RelayServiceInfo{PackageName='" + this.mPackageName + "', ServiceName='" + this.mServiceName + "', ServiceData=" + Arrays.toString(this.mServiceData) + ", DeviceId='" + this.mDeviceId + "', DeviceName='" + this.mDeviceName + "', DeviceType=" + this.mDeviceType + "', Version=" + this.mVersion + '}';
    }
}
