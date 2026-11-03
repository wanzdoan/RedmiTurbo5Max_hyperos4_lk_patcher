package com.android.phone.networkmode;

import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterAddNr extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        for (int i = 0; i < iArr.length; i++) {
            if (!NetworkMode.shouldAddNrNetworkModes(i) || !MiuiPhoneUtils.hideNetmodeMenuAndUseDefaultRat(i)) {
                FiveGManagerBase fiveGManagerBase = FiveGManagerBase.getInstance();
                if (NetworkMode.shouldAddNrNetworkModes(i) && iArr[i].length > 1 && fiveGManagerBase != null && !fiveGManagerBase.isUserFiveGEnabled(i)) {
                    iArr[i] = NMFilter.removeNr(iArr[i]);
                }
            }
        }
        return iArr;
    }
}
