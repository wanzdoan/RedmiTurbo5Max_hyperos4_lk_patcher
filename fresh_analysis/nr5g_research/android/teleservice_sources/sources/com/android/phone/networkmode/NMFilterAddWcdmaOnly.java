package com.android.phone.networkmode;

/* JADX INFO: loaded from: classes.dex */
public class NMFilterAddWcdmaOnly extends NMFilter {
    @Override // com.android.phone.networkmode.NMFilter
    public int[][] filter(int[][] iArr) {
        int[] iArr2;
        for (int i = 0; i < iArr.length; i++) {
            if (NetworkMode.showWcdmaOnlyNetworkModes(i)) {
                int[] iArr3 = new int[iArr[i].length + 1];
                int i2 = 0;
                int i3 = 0;
                while (true) {
                    iArr2 = iArr[i];
                    if (i2 >= iArr2.length) {
                        break;
                    }
                    int i4 = iArr2[i2];
                    if (i4 == 1) {
                        iArr3[i3] = 2;
                        i3++;
                        iArr3[i3] = 1;
                    } else {
                        iArr3[i3] = i4;
                    }
                    i2++;
                    i3++;
                }
                if (i2 == i3) {
                    iArr3[iArr2.length] = 2;
                }
                iArr[i] = iArr3;
            }
        }
        return iArr;
    }
}
