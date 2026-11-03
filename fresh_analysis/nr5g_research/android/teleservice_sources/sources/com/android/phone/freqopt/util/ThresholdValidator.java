package com.android.phone.freqopt.util;

/* JADX INFO: loaded from: classes.dex */
public class ThresholdValidator {
    private int mRsrpThreshold = -105;
    private int mSinrThreshold = 0;
    private long mRateThresholdKbps = 1000;

    public void setRsrpThreshold(int i) {
        this.mRsrpThreshold = i;
    }

    public void setSinrThreshold(int i) {
        this.mSinrThreshold = i;
    }

    public void setRateThresholdKbps(long j) {
        this.mRateThresholdKbps = j;
    }

    public boolean validateSignal(int i, int i2) {
        boolean z = i > this.mRsrpThreshold && i2 > this.mSinrThreshold;
        LogUtils.d("Freq_Opt_ThresholdValidator", "validateSignal rsrp=" + i + ", sinr=" + i2 + ", result=" + z);
        return z;
    }

    public boolean validateRate(long j) {
        boolean z = j > this.mRateThresholdKbps;
        LogUtils.d("Freq_Opt_ThresholdValidator", "validateRate rate=" + j + "kbps, result=" + z);
        return z;
    }

    public int getRsrpThreshold() {
        return this.mRsrpThreshold;
    }

    public int getSinrThreshold() {
        return this.mSinrThreshold;
    }

    public long getRateThresholdKbps() {
        return this.mRateThresholdKbps;
    }
}
