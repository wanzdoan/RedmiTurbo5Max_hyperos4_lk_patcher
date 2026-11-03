package com.android.phone.beidou;

/* JADX INFO: loaded from: classes.dex */
public class BDSatelliteDynInfo {
    int mAltitude;
    int mAzimuthDelta;
    int mCity;
    public int mCountdown;
    int mElevationDelta;
    int mLatitude;
    int mLongitude;
    public BDSmsRecInfo mRecInfo;
    public int mSateId;
    int mSearchCause;
    public BDSmsSendInfo mSendInfo;
    int mSensorAccuracy;
    int mServiceState;
    int mSignalStrength;
    int mSlotId;
    float mTargetAzimuth;
    float mTargetElevation;
    float mTrueAzimuth;
    float mTrueElevation;
    int mScene = 1;
    boolean mInOpenSpace = false;
    BDSysStatus mSysStatus = BDSysStatus.SYS_STATUS_DISABLE;

    public boolean isAlignedAzimuth(int i) {
        return Math.abs(this.mAzimuthDelta) >= 0 && Math.abs(this.mAzimuthDelta) <= i;
    }

    public boolean isAlignedElevation() {
        return Math.abs(this.mElevationDelta) <= 20;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String str = "sat info{, sateId=" + this.mSateId + ", slotId=" + this.mSlotId + ", accuracy=" + this.mSensorAccuracy + ", sysStatus=" + this.mSysStatus + ", trueAzimuth=" + this.mTrueAzimuth + ", trueElevation=" + this.mTrueElevation + ", targetAzimuth=" + this.mTargetAzimuth + ", targetElevation=" + this.mTargetElevation + ", azimuthDelta=" + this.mAzimuthDelta + ", elevationDelta=" + this.mElevationDelta + ", searchCause=" + this.mSearchCause + ", city=" + this.mCity + ", longitude=" + this.mLongitude + ", latitude=" + this.mLatitude + ", altitude=" + this.mAltitude + ", serviceState=" + this.mServiceState + ", signalStrength=" + this.mSignalStrength + '}';
        BDSmsRecInfo bDSmsRecInfo = this.mRecInfo;
        if (bDSmsRecInfo != null) {
            sb.append(bDSmsRecInfo.toString());
            sb.append("\n");
        }
        BDSmsSendInfo bDSmsSendInfo = this.mSendInfo;
        if (bDSmsSendInfo != null) {
            sb.append(bDSmsSendInfo.toString());
            sb.append("\n");
        }
        sb.append(str);
        return sb.toString();
    }

    public class BDSmsSendInfo {
        BDSmsSendFailedCause mFailedCause;
        BDSmsSendStatus mSmsSendStatus;

        public BDSmsSendInfo(BDSmsSendStatus bDSmsSendStatus, BDSmsSendFailedCause bDSmsSendFailedCause) {
            this.mSmsSendStatus = BDSmsSendStatus.SEND_STATUS_UNKNOWN;
            BDSmsSendFailedCause bDSmsSendFailedCause2 = BDSmsSendFailedCause.UNKNOWN;
            this.mSmsSendStatus = bDSmsSendStatus;
            this.mFailedCause = bDSmsSendFailedCause;
        }

        public String toString() {
            return "BDSmsSendInfo = [mSmsSendStatus = " + this.mSmsSendStatus + ", mFailedCause = " + this.mFailedCause + "]";
        }
    }

    public class BDSmsRecInfo {
        BDSmsRecFailedCause mFailedCause;
        boolean mHasContent;
        boolean mRecSuccess;

        BDSmsRecInfo(boolean z, BDSmsRecFailedCause bDSmsRecFailedCause, boolean z2) {
            this.mRecSuccess = z;
            this.mFailedCause = bDSmsRecFailedCause;
            this.mHasContent = z2;
        }

        public String toString() {
            return "BDSmsRecInfo = [mRecSuccess = " + this.mRecSuccess + ", mFailedCause=" + this.mFailedCause + ", mHasContent=" + this.mHasContent + "]";
        }
    }
}
