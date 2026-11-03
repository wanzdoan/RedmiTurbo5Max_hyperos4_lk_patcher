package com.android.internal.telephony.ntnphone;

/* JADX INFO: loaded from: classes.dex */
public class AntIdentitySignalStrength {
    private int mAntId;
    private int mRssi;
    private float mSnr;

    AntIdentitySignalStrength(int i, int i2, float f) {
        this.mAntId = i;
        this.mRssi = i2;
        this.mSnr = f;
    }

    public int getAntId() {
        return this.mAntId;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public float getSnr() {
        return this.mSnr;
    }

    public String toString() {
        return "AntIdentitySignalStrength: antId=" + this.mAntId + " rssi=" + this.mRssi + " snr=" + this.mSnr;
    }
}
