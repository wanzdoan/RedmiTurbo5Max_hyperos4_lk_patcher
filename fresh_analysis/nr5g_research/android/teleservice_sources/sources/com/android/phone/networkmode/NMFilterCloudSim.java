package com.android.phone.networkmode;

import android.provider.MiuiSettings;
import com.android.phone.PhoneGlobals;
import miui.telephony.SubscriptionManager;
import miui.telephony.VirtualSimUtils;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterCloudSim extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        if (VirtualSimUtils.isCloudSimEnabled()) {
            int virtualSimSlotId = MiuiSettings.VirtualSim.getVirtualSimSlotId(PhoneGlobals.getInstance());
            if (SubscriptionManager.isRealSlotId(virtualSimSlotId)) {
                iArr[virtualSimSlotId] = new int[]{9};
            }
        }
        return iArr;
    }
}
