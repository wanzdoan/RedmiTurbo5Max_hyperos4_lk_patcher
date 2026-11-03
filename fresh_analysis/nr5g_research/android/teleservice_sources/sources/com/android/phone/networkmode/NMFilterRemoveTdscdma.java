package com.android.phone.networkmode;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterRemoveTdscdma extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        for (int i = 0; i < iArr.length; i++) {
            if (NetworkMode.isRemoveTdScdma(i)) {
                iArr[i] = NMFilter.removeSelectedMode(iArr[i], 10);
            }
        }
        return iArr;
    }
}
