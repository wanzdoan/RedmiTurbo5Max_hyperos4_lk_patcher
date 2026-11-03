package com.android.phone.networkmode;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterDisableLte extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        if (NetworkMode.isDisableLte()) {
            for (int i = 0; i < iArr.length; i++) {
                iArr[i] = NMFilter.removeGteLte(iArr[i]);
            }
        }
        return iArr;
    }
}
