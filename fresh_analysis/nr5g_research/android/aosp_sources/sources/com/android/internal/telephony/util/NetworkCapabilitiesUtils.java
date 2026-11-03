package com.android.internal.telephony.util;

import android.net.NetworkCapabilities;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public final class NetworkCapabilitiesUtils {
    private static final int[] DISPLAY_TRANSPORT_PRIORITIES = {4, 0, 5, 2, 1, 3, 8, 10};
    public static final long RESTRICTED_CAPABILITIES = 12490639292L;
    public static final int TYPE_TEST = 18;
    public static final long UNRESTRICTED_CAPABILITIES = 4163;

    public static int deduceTransportTypeForLegacyNetworkType(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 1;
        }
        if (i == 7) {
            return 2;
        }
        if (i == 9) {
            return 3;
        }
        switch (i) {
            case 16:
                return 2;
            case 17:
                return 4;
            case 18:
                return 7;
            default:
                return -1;
        }
    }

    public static boolean inferRestrictedCapability(long j) {
        if ((608174080 & j) != 0) {
            return true;
        }
        return (UNRESTRICTED_CAPABILITIES & j) == 0 && (j & RESTRICTED_CAPABILITIES) != 0;
    }

    public static int getDisplayTransport(int[] iArr) {
        for (int i : DISPLAY_TRANSPORT_PRIORITIES) {
            if (CollectionUtils.contains(iArr, i)) {
                return i;
            }
        }
        if (iArr.length < 1) {
            Phone$$ExternalSyntheticBUOutline0.m("No transport in the provided array");
            return 0;
        }
        return iArr[0];
    }

    public static boolean inferRestrictedCapability(NetworkCapabilities networkCapabilities) {
        return inferRestrictedCapability(BitUtils.packBits(networkCapabilities.getCapabilities()));
    }
}
