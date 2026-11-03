package com.android.phone.networkmode;

import com.android.phone.MiuiPhoneUtils;
import com.android.phone.NetworkModeManager;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterLteOnly extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (NetworkModeManager.isNetworkModeLteOnly(i)) {
                int[] iArrRemoveSelectedMode = NMFilter.removeSelectedMode(iArr[i], 11);
                iArr[i] = iArrRemoveSelectedMode;
                iArr[i] = NMFilter.removeSelectedMode(iArrRemoveSelectedMode, 0);
                return iArr;
            }
        }
        return iArr;
    }
}
