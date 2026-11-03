package com.android.phone.statistics;

import com.android.phone.statistics.utils.Constants;

/* JADX INFO: loaded from: classes.dex */
public class StatsSignalInfo {
    private long mLastTime;
    private int mLteRsrp;
    private int mLteRsrpBin;
    private int mLteRsrq;
    private int mLteRsrqBin;
    private int mLteSnr;
    private int mLteSnrBin;
    private String mNetworkClass;
    private long mNetworkConTime;
    private int mNetworkType;
    private int mNrRsrp;
    private int mNrRsrpBin;
    private int mNrSnr;
    private int mNrSnrBin;
    private String mPlmn;
    private boolean mSceneState;
    private int mSceneType;

    public StatsSignalInfo() {
        this.mLteRsrp = Integer.MAX_VALUE;
        this.mLteSnr = Integer.MAX_VALUE;
        this.mLteRsrq = Integer.MAX_VALUE;
        this.mNrRsrp = Integer.MAX_VALUE;
        this.mNrSnr = Integer.MAX_VALUE;
        this.mNetworkClass = "UNKNOW";
        this.mNetworkType = -1;
        this.mSceneState = false;
        this.mLastTime = -1L;
        this.mSceneType = -1;
        this.mPlmn = "";
        this.mLteRsrpBin = 0;
        this.mLteSnrBin = 0;
        this.mLteRsrqBin = 0;
        this.mNrRsrpBin = 0;
        this.mNrSnrBin = 0;
        this.mNetworkConTime = -1L;
        clear();
    }

    public StatsSignalInfo(int i, int i2, int i3, int i4, int i5, String str, int i6, int i7, boolean z, String str2) {
        this.mLastTime = -1L;
        this.mLteRsrpBin = 0;
        this.mLteSnrBin = 0;
        this.mLteRsrqBin = 0;
        this.mNrRsrpBin = 0;
        this.mNrSnrBin = 0;
        this.mNetworkConTime = -1L;
        this.mLteRsrp = i;
        this.mLteSnr = i2;
        this.mLteRsrq = i3;
        this.mNrRsrp = i4;
        this.mNrSnr = i5;
        this.mNetworkClass = str;
        this.mNetworkType = i6;
        this.mSceneType = i7;
        this.mSceneState = z;
        this.mPlmn = str2;
        int[] iArr = Constants.SCENE_RSRP_LENIENT;
        this.mLteRsrpBin = getSignalBin(iArr, i);
        int[] iArr2 = Constants.SCENE_SNR_LENIENT;
        this.mLteSnrBin = getSignalBin(iArr2, this.mLteSnr);
        this.mLteRsrqBin = getSignalBin(Constants.SCENE_RSRQ_LENIENT, this.mLteRsrq);
        this.mNrRsrpBin = getSignalBin(iArr, this.mNrRsrp);
        this.mNrSnrBin = getSignalBin(iArr2, this.mNrSnr);
    }

    public void clear() {
        this.mLteRsrp = Integer.MAX_VALUE;
        this.mLteSnr = Integer.MAX_VALUE;
        this.mLteRsrq = Integer.MAX_VALUE;
        this.mNrRsrp = Integer.MAX_VALUE;
        this.mNrSnr = Integer.MAX_VALUE;
        this.mNetworkClass = "UNKNOW";
        this.mNetworkType = -1;
        this.mSceneState = false;
        this.mSceneType = -1;
        this.mLastTime = -1L;
        this.mPlmn = "";
        this.mLteRsrpBin = 0;
        this.mLteSnrBin = 0;
        this.mLteRsrqBin = 0;
        this.mNrRsrpBin = 0;
        this.mNrSnrBin = 0;
        this.mNetworkConTime = -1L;
    }

    public int getLteRsrp() {
        return this.mLteRsrp;
    }

    public int getLteSnr() {
        return this.mLteSnr;
    }

    public int getLteRsrq() {
        return this.mLteRsrq;
    }

    public int getNrRsrp() {
        return this.mNrRsrp;
    }

    public int getNrSnr() {
        return this.mNrSnr;
    }

    public boolean getSceneState() {
        return this.mSceneState;
    }

    public long getLastTime() {
        return this.mLastTime;
    }

    public void setLastTime(Long l) {
        this.mLastTime = l.longValue();
    }

    public String getNetworkClass() {
        return this.mNetworkClass;
    }

    public int getNetworkType() {
        return this.mNetworkType;
    }

    public int getSceneType() {
        return this.mSceneType;
    }

    public String getPlmn() {
        return this.mPlmn;
    }

    public long getNetworkConTime() {
        return this.mNetworkConTime;
    }

    public void setNetworkConTime(long j) {
        this.mNetworkConTime = j;
    }

    public boolean isLteSignalBinChanged(StatsSignalInfo statsSignalInfo) {
        return (getLteRsrpBin() == statsSignalInfo.getLteRsrpBin() && getLteSnrBin() == statsSignalInfo.getLteSnrBin()) ? false : true;
    }

    public boolean isNrSignalBinChanged(StatsSignalInfo statsSignalInfo) {
        return (getNrRsrpBin() == statsSignalInfo.getNrRsrpBin() && getNrSnrBin() == statsSignalInfo.getNrSnrBin()) ? false : true;
    }

    public boolean isNetworkTypeChanged(StatsSignalInfo statsSignalInfo) {
        return this.mNetworkType != statsSignalInfo.getNetworkType();
    }

    public boolean isNetworkClassChanged(StatsSignalInfo statsSignalInfo) {
        return !this.mNetworkClass.equals(statsSignalInfo.getNetworkClass());
    }

    public boolean isSceneSignalChanged(StatsSignalInfo statsSignalInfo) {
        return isLteSignalBinChanged(statsSignalInfo) || isNrSignalBinChanged(statsSignalInfo) || isSceneStatusChanged(statsSignalInfo);
    }

    public boolean isSceneNetworkChanged(StatsSignalInfo statsSignalInfo) {
        return isNetworkClassChanged(statsSignalInfo) || isNetworkTypeChanged(statsSignalInfo) || isSceneStatusChanged(statsSignalInfo);
    }

    public boolean isSceneStatusChanged(StatsSignalInfo statsSignalInfo) {
        return this.mSceneState != statsSignalInfo.getSceneState();
    }

    public boolean isValidNetworkType() {
        return this.mNetworkType != -1;
    }

    public int getLteRsrpBin() {
        return this.mLteRsrpBin;
    }

    public int getLteSnrBin() {
        return this.mLteSnrBin;
    }

    public int getLteRsrqBin() {
        return this.mLteRsrqBin;
    }

    public int getNrRsrpBin() {
        return this.mNrRsrpBin;
    }

    public int getNrSnrBin() {
        return this.mNrSnrBin;
    }

    private static int getSignalBin(int[] iArr, int i) {
        if (iArr == null || iArr.length == 0 || iArr[iArr.length - 1] < i || i < iArr[0]) {
            return 0;
        }
        for (int i2 = 0; i2 < iArr.length - 1; i2++) {
            if (iArr[i2] < i) {
                int i3 = i2 + 1;
                if (i <= iArr[i3]) {
                    return i3;
                }
            }
        }
        return 1;
    }

    public void copy(StatsSignalInfo statsSignalInfo) {
        this.mLteRsrp = statsSignalInfo.getLteRsrp();
        this.mLteSnr = statsSignalInfo.getLteSnr();
        this.mLteRsrq = statsSignalInfo.getLteRsrq();
        this.mNrRsrp = statsSignalInfo.getNrRsrp();
        this.mNrSnr = statsSignalInfo.getNrSnr();
        this.mNetworkClass = statsSignalInfo.getNetworkClass();
        this.mNetworkType = statsSignalInfo.getNetworkType();
        this.mSceneType = statsSignalInfo.getSceneType();
        this.mSceneState = statsSignalInfo.getSceneState();
        this.mPlmn = statsSignalInfo.getPlmn();
        this.mLteRsrpBin = statsSignalInfo.getLteRsrpBin();
        this.mLteSnrBin = statsSignalInfo.getLteSnrBin();
        this.mLteRsrqBin = statsSignalInfo.getLteRsrqBin();
        this.mNrRsrpBin = statsSignalInfo.getNrRsrpBin();
        this.mNrSnrBin = statsSignalInfo.getNrSnrBin();
    }

    public String toString() {
        return "StatsSignalInfo [mLastTime=" + this.mLastTime + ", mLteRsrp=" + this.mLteRsrp + ", mLteRsrpBin=" + this.mLteRsrpBin + ", mLteRsrq=" + this.mLteRsrq + ", mLteRsrqBin=" + this.mLteRsrqBin + ", mLteSnr=" + this.mLteSnr + ", mLteSnrBin=" + this.mLteSnrBin + ", mNetworkClass=" + this.mNetworkClass + ", mNetworkConTime=" + this.mNetworkConTime + ", mNetworkType=" + this.mNetworkType + ", mNrRsrp=" + this.mNrRsrp + ", mNrRsrpBin=" + this.mNrRsrpBin + ", mNrSnr=" + this.mNrSnr + ", mNrSnrBin=" + this.mNrSnrBin + ", mPlmn=" + this.mPlmn + ", mSceneState=" + this.mSceneState + ", mSceneType=" + this.mSceneType + "]";
    }
}
