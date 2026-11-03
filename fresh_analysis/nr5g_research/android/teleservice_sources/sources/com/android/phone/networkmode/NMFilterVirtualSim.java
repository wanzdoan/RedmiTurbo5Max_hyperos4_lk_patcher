package com.android.phone.networkmode;

import android.provider.MiuiSettings;
import com.android.internal.util.ArrayUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.NetworkModeManager;
import com.android.phone.PhoneGlobals;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.Arrays;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterVirtualSim extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        int virtualSimSlotId;
        int ukelinkVirtualSimSlot = MiuiPhoneUtils.getUkelinkVirtualSimSlot();
        int i = SubscriptionManager.INVALID_SLOT_ID;
        if (ukelinkVirtualSimSlot != i) {
            NetworkModeManager.log("UkelinkVirtualSimSlot : " + ukelinkVirtualSimSlot);
            iArr[ukelinkVirtualSimSlot] = new int[]{MiuiPhoneUtils.getUkelinkVirtualSimNetworkType()};
        }
        int ukelinkCloudSimSlot = MiuiPhoneUtils.getUkelinkCloudSimSlot();
        if (ukelinkCloudSimSlot != i) {
            NetworkModeManager.log("ukeCloudSimSlot : " + ukelinkCloudSimSlot);
            iArr[ukelinkCloudSimSlot] = new int[]{MiuiPhoneUtils.getUkelinkCloudSimNetworkType()};
        }
        if (ukelinkVirtualSimSlot == i && ukelinkCloudSimSlot == i && i != (virtualSimSlotId = MiuiSettings.VirtualSim.getVirtualSimSlotId(PhoneGlobals.getInstance()))) {
            iArr[virtualSimSlotId] = getSupportedNetworkModesForVirtualSim(iArr[virtualSimSlotId]);
        }
        return iArr;
    }

    public static int[] getSupportedNetworkModesForVirtualSim(int[] iArr) {
        StringBuilder sb = new StringBuilder(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
        sb.append("getSupportedNetworkModesForVirtualSim");
        if (MiuiSettings.VirtualSim.isSupport4G(PhoneGlobals.getInstance())) {
            sb.append(" support4G");
            NetworkModeManager.log(sb.toString());
            return iArr;
        }
        int[] iArrRemoveGteLteAndTdd = removeGteLteAndTdd(iArr);
        sb.append(" networkModes from ");
        sb.append(Arrays.toString(iArr));
        sb.append(" to ");
        sb.append(Arrays.toString(iArrRemoveGteLteAndTdd));
        NetworkModeManager.log(sb.toString());
        return iArrRemoveGteLteAndTdd;
    }

    private static int[] removeGteLteAndTdd(int[] iArr) {
        return removeLteAndTdd(NMFilter.removeNr(iArr));
    }

    private static int[] removeLteAndTdd(int[] iArr) {
        int[] iArr2 = {10, 8, 20, 17, 9, 16};
        int i = 0;
        for (int i2 : iArr) {
            if (!ArrayUtils.contains(iArr2, i2)) {
                i++;
            }
        }
        if (i > 0) {
            int[] iArr3 = new int[i];
            int i3 = 0;
            for (int i4 = 0; i4 < iArr.length; i4++) {
                if (!ArrayUtils.contains(iArr2, iArr[i4])) {
                    iArr3[i3] = iArr[i4];
                    i3++;
                }
            }
            return iArr3;
        }
        NetworkModeManager.log("removeLteAndTdd error args=" + Arrays.toString(iArr));
        return new int[]{1};
    }
}
