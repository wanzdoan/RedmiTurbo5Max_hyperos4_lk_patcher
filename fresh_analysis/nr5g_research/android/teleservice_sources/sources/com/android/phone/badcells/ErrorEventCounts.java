package com.android.phone.badcells;

/* JADX INFO: compiled from: BadCellDynamicWeight.java */
/* JADX INFO: loaded from: classes.dex */
class ErrorEventCounts {
    public int mBlerCounts;
    public int mNasCounts;
    public int mRachCounts;
    public int mRat;
    public int mRlfCounts;
    public int mSchCounts;

    public ErrorEventCounts(int i) {
        this.mRat = i;
    }

    public ErrorEventCounts() {
    }

    public int total() {
        return this.mRlfCounts + this.mNasCounts + this.mSchCounts + this.mBlerCounts + this.mRachCounts;
    }
}
