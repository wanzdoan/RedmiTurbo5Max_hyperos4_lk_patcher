package com.android.phone.networkmode;

import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.module.ITelephonyModule;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterDual4GDisabled extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        int[] iArr2;
        for (int i = 0; i < iArr.length; i++) {
            if (i != DefaultSimManager.getDefaultDataSlotId()) {
                if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled() && !MiuiPhoneUtils.cannotDisableDual4G()) {
                    iArr[i] = NMFilter.removeGteLte(iArr[i]);
                } else if (!NetworkMode.shouldShowViceNetworkTypePref() && (iArr2 = iArr[i]) != null && iArr2.length > 0) {
                    iArr[i] = new int[]{iArr2[0]};
                }
            }
        }
        return iArr;
    }
}
