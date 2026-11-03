package com.android.phone.networkmode;

import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.NetworkModeManager;
import com.android.phone.module.ITelephonyModule;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterCmccViceSlot extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        if (SubscriptionManager.INVALID_SUBSCRIPTION_ID != ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getDataSlotForCmcc()) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                if (i != DefaultSimManager.getDefaultDataSlotId()) {
                    int phoneType = NetworkModeManager.getPhoneType(i);
                    if (phoneType == 1) {
                        iArr[i] = NMFilter.removeGteLte(iArr[i]);
                    } else if (phoneType == 2) {
                        iArr[i] = new int[]{7};
                    }
                }
            }
        }
        return iArr;
    }
}
