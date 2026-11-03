package com.android.phone.statistics;

/* JADX INFO: loaded from: classes.dex */
public class StatsFiveGInfo {
    private int mArfcn;
    private long mArfcnTime;
    private int mBand;
    private long mBandTime;
    private int mConfigType;
    private String mOperator;
    private int mRsrpLevel;
    private long mRsrpTime;
    private boolean mShowFiveGIcon;
    private long mShowIconTime;
    private int mSnrLevel;

    public StatsFiveGInfo() {
        this.mOperator = "";
        this.mSnrLevel = 0;
        this.mRsrpTime = -2L;
        this.mShowFiveGIcon = false;
        this.mShowIconTime = -2L;
        this.mRsrpLevel = 0;
        this.mConfigType = -2;
        this.mBand = -2;
        this.mBandTime = -2L;
        this.mArfcn = -2;
        this.mArfcnTime = -2L;
        clear();
    }

    public StatsFiveGInfo(String str, int i, int i2, int i3, boolean z, int i4, int i5) {
        this.mRsrpTime = -2L;
        this.mShowIconTime = -2L;
        this.mBandTime = -2L;
        this.mArfcnTime = -2L;
        this.mOperator = str;
        this.mConfigType = i;
        this.mRsrpLevel = i2;
        this.mSnrLevel = i3;
        this.mShowFiveGIcon = z;
        this.mBand = i4;
        this.mArfcn = i5;
    }

    private void clear() {
        this.mOperator = "";
        this.mSnrLevel = 0;
        this.mRsrpTime = -2L;
        this.mShowFiveGIcon = false;
        this.mShowIconTime = -2L;
        this.mRsrpLevel = 0;
        this.mConfigType = -2;
        this.mBand = -2;
        this.mArfcn = -2;
    }

    public String getOperator() {
        return this.mOperator;
    }

    public void setOperator(String str) {
        this.mOperator = str;
    }

    public int getSnrLevel() {
        return this.mSnrLevel;
    }

    public void setSnrLevel(int i) {
        this.mSnrLevel = i;
    }

    public long getRsrpTime() {
        return this.mRsrpTime;
    }

    public void setRsrpTime(long j) {
        this.mRsrpTime = j;
    }

    public boolean isShowFiveGIcon() {
        return this.mShowFiveGIcon;
    }

    public void setShowFiveGIcon(boolean z) {
        this.mShowFiveGIcon = z;
    }

    public long getShowIconTime() {
        return this.mShowIconTime;
    }

    public void setShowIconTime(long j) {
        this.mShowIconTime = j;
    }

    public int getRsrpLevel() {
        return this.mRsrpLevel;
    }

    public void setRsrpLevel(int i) {
        this.mRsrpLevel = i;
    }

    public int getConfigType() {
        return this.mConfigType;
    }

    public void setConfigType(int i) {
        this.mConfigType = i;
    }

    public int getBand() {
        return this.mBand;
    }

    public void setBand(int i) {
        this.mBand = i;
    }

    public long getBandTime() {
        return this.mBandTime;
    }

    public void setBandTime(long j) {
        this.mBandTime = j;
    }

    public int getArfcn() {
        return this.mArfcn;
    }

    public void setArfcn(int i) {
        this.mArfcn = i;
    }

    public long getArfcnTime() {
        return this.mArfcnTime;
    }

    public void setArfcnTime(long j) {
        this.mArfcnTime = j;
    }
}
