package com.android.services.telephony.relay.network;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRelayInfo {
    public String mTrafficName;
    public String mTrafficUnit;
    public String mTrafficValue;
    public int mSimSate = 0;
    public int mMethod = -1;
    public int mDataRegState = 1;
    public int mVoiceRegState = 1;
    public int mNetworkType = 0;
    public int mSignalLevel = 0;
    public int mSignalType = 5;
    public int mNetwork = 0;
    public int mOverrideNetwork = 0;
    public int mDataConectionState = 0;
    public int mDataConectionNetworkType = 0;
    public long mDurationTime = 0;
    public boolean mIsValid = false;
    public boolean mIsDataRoaming = false;
    public long mTotalTrafficLimit = 0;
    public long mUsedMonthTraffic = 0;
    public int mWiFiSignalLevel = 4;
    public String mGoBssid = "";
    public String mGoDeviceId = "";
    public int mVersion = 1;
    public boolean mAutoShareNetworkEnabled = false;

    public String toString() {
        return "NetworkRelayInfo:{mSimSate=" + this.mSimSate + ", mMethod=" + this.mMethod + ", mDataState=" + this.mDataRegState + ", mVoiceState=" + this.mVoiceRegState + ", mNetworkType=" + this.mNetworkType + ", mTrafficName=" + this.mTrafficName + ", mTrafficValue=" + this.mTrafficValue + ", mTrafficUnit=" + this.mTrafficUnit + ", mSignalType=" + this.mSignalType + ", mSignalLevel=" + this.mSignalLevel + ", mNetwork=" + this.mNetwork + ", mOverrideNetwork=" + this.mOverrideNetwork + ", mDataConectionState=" + this.mDataConectionState + ", mDataConectionNetworkType=" + this.mDataConectionNetworkType + ", mDurationTime=" + this.mDurationTime + ", mIsValid=" + this.mIsValid + ", mIsDataRoaming=" + this.mIsDataRoaming + ", mTotalTrafficLimit=" + this.mTotalTrafficLimit + ", mUsedMonthTraffic=" + this.mUsedMonthTraffic + ", mWiFiSignalLevel=" + this.mWiFiSignalLevel + ", mGoBssid=****, mGoDeviceId=" + this.mGoDeviceId + ", mVersion=" + this.mVersion + ", mAutoShareNetworkEnabled=" + this.mAutoShareNetworkEnabled + "}";
    }
}
