package com.android.phone.networkmode;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterRemoveCdma extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        for (int i = 0; i < iArr.length; i++) {
            if (NetworkMode.isRemoveCdma(i)) {
                ArrayList arrayList = new ArrayList();
                int[] supportNetworkMode = NetworkMode.getSupportNetworkMode(3);
                for (int i2 : iArr[i]) {
                    int length = supportNetworkMode.length;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= length) {
                            arrayList.add(Integer.valueOf(i2));
                            break;
                        }
                        if (supportNetworkMode[i3] == i2) {
                            break;
                        }
                        i3++;
                    }
                }
                int[] iArr2 = new int[arrayList.size()];
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    iArr2[i4] = ((Integer) arrayList.get(i4)).intValue();
                }
                iArr[i] = iArr2;
            }
        }
        return iArr;
    }
}
