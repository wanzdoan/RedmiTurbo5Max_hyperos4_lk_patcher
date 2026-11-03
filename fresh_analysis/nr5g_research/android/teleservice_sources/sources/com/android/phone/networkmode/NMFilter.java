package com.android.phone.networkmode;

import com.android.internal.util.ArrayUtils;
import com.android.phone.NetworkModeManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class NMFilter {
    public abstract int[][] filter(int[][] iArr);

    public static int[] removeSelectedMode(int[] iArr, int i) {
        int[] supportNetworkMode = NetworkMode.getSupportNetworkMode(i);
        int i2 = 0;
        for (int i3 : iArr) {
            if (!ArrayUtils.contains(supportNetworkMode, i3)) {
                i2++;
            }
        }
        if (i2 > 0) {
            int[] iArr2 = new int[i2];
            int i4 = 0;
            for (int i5 = 0; i5 < iArr.length; i5++) {
                if (!ArrayUtils.contains(supportNetworkMode, iArr[i5])) {
                    iArr2[i4] = iArr[i5];
                    i4++;
                }
            }
            return iArr2;
        }
        NetworkModeManager.log("removeSelectedMode error args=" + iArr + ", removeType: " + i);
        return new int[]{1};
    }

    public static int[] removeLte(int[] iArr) {
        return removeSelectedMode(iArr, 7);
    }

    public static int[] removeNr(int[] iArr) {
        return removeSelectedMode(iArr, 9);
    }

    public static int[] removeGteLte(int[] iArr) {
        return removeLte(removeNr(iArr));
    }
}
