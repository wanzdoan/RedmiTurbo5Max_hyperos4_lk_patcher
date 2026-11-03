package com.android.phone.networkmode;

import com.android.phone.MiuiPhoneUtils;
import com.android.phone.NetworkModeManager;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterCmccPreferred extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        int[] iArr2;
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (NetworkModeManager.isRemoveNetworkModeSettings(i) && MiuiPhoneUtils.isCmccSim(MiuiPhoneUtils.getPhone(i)) && (iArr2 = iArr[i]) != null && iArr2.length > 0) {
                iArr[i] = new int[]{iArr2[0]};
            }
        }
        return iArr;
    }
}
