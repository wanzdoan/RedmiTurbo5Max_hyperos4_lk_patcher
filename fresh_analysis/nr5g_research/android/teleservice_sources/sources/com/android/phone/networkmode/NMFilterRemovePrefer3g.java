package com.android.phone.networkmode;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterRemovePrefer3g extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        for (int i = 0; i < iArr.length; i++) {
            if (NetworkMode.isRemovePrefer3G(i) && !NetworkMode.isSupport3GAbilityHide3GSwitch(i)) {
                iArr[i] = NMFilter.removeSelectedMode(iArr[i], 11);
            }
        }
        return iArr;
    }
}
