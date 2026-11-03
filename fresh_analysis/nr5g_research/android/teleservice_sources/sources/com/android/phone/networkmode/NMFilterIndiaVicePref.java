package com.android.phone.networkmode;

import com.android.phone.ModuleDelegate;
import com.android.phone.module.ITelephonyModule;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterIndiaVicePref extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        if (NetworkMode.hasCardByMcc(NetworkMode.INDIA_MCC_ARRAY)) {
            for (int i = 0; i < iArr.length; i++) {
                if (i != DefaultSimManager.getDefaultDataSlotId() && !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled()) {
                    iArr[i] = NMFilter.removeGteLte(iArr[i]);
                }
            }
        }
        return iArr;
    }
}
