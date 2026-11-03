package com.android.phone;

import com.android.phone.statistics.utils.Utils;
import com.xiaomi.mirilhook.MiRilHook;
import miui.os.Build;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class FiveGServiceInfo {
    private boolean IS_MTK;
    private boolean IS_QCOM;
    private int mBearerAllocationStatus;
    private int mDcnr;
    private boolean mEndcStatus;
    private int mNrConfigType;
    private int mNrIconType;
    private int mPhoneId;
    private int mPlmn;
    private int mRsrp;
    private int mRxCount = -1;
    private int mSnr;
    private boolean mStatus;
    private int mUpperLayerInd;
    private static final int[] QC_SNR_THRSH = {-200, -30, 10, 45, 130, MiRilHook.HOOK_ID_START_URC_BY_AT};
    private static final int[] MTK_SNR_THRSH = {-23, 40};

    public FiveGServiceInfo() {
        this.mRsrp = Integer.MAX_VALUE;
        this.mSnr = Integer.MAX_VALUE;
        this.mPhoneId = -1;
        boolean z = ModemProvider.IS_QCOM;
        this.IS_QCOM = z;
        this.IS_MTK = ModemProvider.IS_MTK || ModemProvider.IS_XRING;
        this.mStatus = false;
        this.mBearerAllocationStatus = -1;
        this.mPlmn = z ? 0 : -1;
        this.mUpperLayerInd = z ? 0 : -1;
        this.mDcnr = z ? 0 : -1;
        this.mNrConfigType = -1;
        this.mNrIconType = -1;
        this.mEndcStatus = false;
        this.mRsrp = Integer.MAX_VALUE;
        this.mSnr = Integer.MAX_VALUE;
        this.mPhoneId = -1;
    }

    public void setPhoneId(int i) {
        this.mPhoneId = i;
    }

    public int getAllocated() {
        return this.mBearerAllocationStatus;
    }

    public int getPlmn() {
        return this.mPlmn;
    }

    public int getUpperLayerInd() {
        return this.mUpperLayerInd;
    }

    public int getDcnr() {
        return this.mDcnr;
    }

    public int getNrConfigType() {
        return this.mNrConfigType;
    }

    public int getNrIconType() {
        return this.mNrIconType;
    }

    public int getRsrp() {
        return this.mRsrp;
    }

    public int getSnr() {
        return this.mSnr;
    }

    public int getSnrLevel() {
        return getLevel(this.mSnr, this.IS_QCOM ? QC_SNR_THRSH : MTK_SNR_THRSH);
    }

    public int getRsrpLevel() {
        return Utils.getSsrsrpBin(this.mRsrp);
    }

    public int getRxCount() {
        return this.mRxCount;
    }

    public void setStatus(boolean z) {
        this.mStatus = z;
    }

    public void setBearAllocated(int i) {
        this.mBearerAllocationStatus = i;
    }

    public void setUpperLayerInd(int i) {
        this.mUpperLayerInd = i;
    }

    public void setDcnr(int i) {
        this.mDcnr = i;
    }

    public void setNrConfigType(int i) {
        this.mNrConfigType = i;
    }

    public void setNrIconType(int i) {
        this.mNrIconType = i;
    }

    public void setEndcStatus(boolean z) {
        this.mEndcStatus = z;
    }

    public void setSnr(int i) {
        this.mSnr = i;
    }

    public void setRsrp(int i) {
        this.mRsrp = i;
    }

    public void setRxCount(int i) {
        this.mRxCount = i;
    }

    public boolean isConnectedOnSaMode() {
        if (this.IS_QCOM && this.mNrConfigType == 1 && this.mBearerAllocationStatus > 0) {
            return true;
        }
        return this.IS_MTK && this.mNrConfigType == 1;
    }

    public boolean isConnectedOnNsaMode() {
        if (this.IS_QCOM && this.mNrConfigType == 0) {
            return true;
        }
        return this.IS_MTK && this.mNrConfigType == 0;
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0035  */
    public boolean isShow5GIcon() {
        boolean configState;
        if (this.IS_QCOM && this.mStatus) {
            int i = this.mNrConfigType;
            if (i == 1) {
                if (this.mBearerAllocationStatus > 0) {
                    configState = true;
                } else {
                    configState = false;
                }
            } else if (i != 0) {
                configState = false;
            } else if (!Build.IS_INTERNATIONAL_BUILD) {
                if (Build.IS_CM_CUSTOMIZATION && isCmccSimCard(this.mPhoneId)) {
                    configState = getConfigState();
                } else {
                    configState = getConfigDState();
                }
            } else {
                configState = getConfigState();
            }
        } else {
            configState = false;
        }
        if (this.IS_QCOM) {
            return configState;
        }
        return this.IS_MTK && this.mStatus && this.mNrIconType > 0;
    }

    public boolean is5GConnected() {
        return isConnectedOnNsaMode() || isConnectedOnSaMode();
    }

    private boolean getConfigDState() {
        return this.mNrIconType > 0 || this.mBearerAllocationStatus > 0 || (this.mUpperLayerInd == 1 && this.mPlmn == 1);
    }

    private boolean getConfigState() {
        return this.mNrIconType > 0;
    }

    private boolean isCmccSimCard(int i) {
        return TelephonyManager.getDefault().isSameOperator("46000", TelephonyManager.getDefault().getSimOperatorForSlot(i));
    }

    private synchronized int getLevel(int i, int[] iArr) {
        int i2;
        i2 = 1;
        if (iArr[iArr.length - 1] < i || i < iArr[0]) {
            i2 = 0;
        } else {
            for (int i3 = 0; i3 < iArr.length - 1; i3++) {
                if (iArr[i3] < i) {
                    int i4 = i3 + 1;
                    if (i <= iArr[i4]) {
                        i2 = i4;
                        break;
                    }
                }
            }
        }
        return i2;
    }

    public void copyFrom(FiveGServiceInfo fiveGServiceInfo) {
        this.mStatus = fiveGServiceInfo.mStatus;
        this.mBearerAllocationStatus = fiveGServiceInfo.mBearerAllocationStatus;
        this.mPlmn = fiveGServiceInfo.mPlmn;
        this.mUpperLayerInd = fiveGServiceInfo.mUpperLayerInd;
        this.mDcnr = fiveGServiceInfo.mDcnr;
        this.mNrConfigType = fiveGServiceInfo.mNrConfigType;
        this.mNrIconType = fiveGServiceInfo.mNrIconType;
        this.mEndcStatus = fiveGServiceInfo.mEndcStatus;
        this.mSnr = fiveGServiceInfo.mSnr;
        this.mRsrp = fiveGServiceInfo.mRsrp;
        this.mRxCount = fiveGServiceInfo.mRxCount;
        this.mPhoneId = fiveGServiceInfo.mPhoneId;
    }

    public boolean equals(FiveGServiceInfo fiveGServiceInfo) {
        return this.mStatus == fiveGServiceInfo.mStatus && this.mBearerAllocationStatus == fiveGServiceInfo.mBearerAllocationStatus && this.mPlmn == fiveGServiceInfo.mPlmn && this.mUpperLayerInd == fiveGServiceInfo.mUpperLayerInd && this.mDcnr == fiveGServiceInfo.mDcnr && this.mNrConfigType == fiveGServiceInfo.mNrConfigType && this.mNrIconType == fiveGServiceInfo.mNrIconType && this.mEndcStatus == fiveGServiceInfo.mEndcStatus && this.mSnr == fiveGServiceInfo.mSnr && this.mRsrp == fiveGServiceInfo.mRsrp && this.mRxCount == fiveGServiceInfo.mRxCount && this.mPhoneId == fiveGServiceInfo.mPhoneId;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("phoneId=");
        sb.append(this.mPhoneId);
        sb.append(",Status=");
        sb.append(this.mStatus);
        sb.append(",BearerAlloc=");
        sb.append(this.mBearerAllocationStatus);
        sb.append(",Plmn=");
        sb.append(this.mPlmn);
        sb.append(",UpLayerInd=");
        sb.append(this.mUpperLayerInd);
        sb.append(",");
        sb.append("Dcnr=" + this.mDcnr);
        sb.append(",NrConfigType=");
        sb.append(this.mNrConfigType);
        sb.append(",NrIconType=");
        sb.append(this.mNrIconType);
        sb.append(",EndcStatus=");
        sb.append(this.mEndcStatus);
        sb.append(",Snr=");
        sb.append(this.mSnr);
        sb.append(",Rsrp=");
        sb.append(this.mRsrp);
        sb.append("RxCount=");
        sb.append(this.mRxCount);
        return sb.toString();
    }
}
