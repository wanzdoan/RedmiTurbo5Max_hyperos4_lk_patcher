package com.android.phone.networkmode;

import com.android.internal.util.ArrayUtils;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterRemove2gOnly extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        for (int i = 0; i < iArr.length; i++) {
            if (NetworkMode.isRemove2GOnly(i)) {
                int[] iArr2 = iArr[i];
                if (iArr2.length >= 2 && ArrayUtils.contains(iArr2, 1)) {
                    int[] iArr3 = new int[iArr[i].length - 1];
                    int i2 = 0;
                    int i3 = 0;
                    while (true) {
                        int[] iArr4 = iArr[i];
                        if (i2 >= iArr4.length) {
                            break;
                        }
                        int i4 = iArr4[i2];
                        if (i4 != 1) {
                            iArr3[i3] = i4;
                            i3++;
                        }
                        i2++;
                    }
                    iArr[i] = iArr3;
                }
            }
        }
        return iArr;
    }
}
