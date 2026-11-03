package com.android.phone.freqopt.util;

/* JADX INFO: loaded from: classes.dex */
public abstract class NrBandUtils {
    private static final int[][] BAND_TABLE = {new int[]{123400, 130400, 71, 1}, new int[]{151600, 160600, 28, 1}, new int[]{158200, 164200, 20, 1}, new int[]{173800, 178800, 5, 1}, new int[]{185000, 192000, 8, 1}, new int[]{285400, 286400, 51, 2}, new int[]{286400, 294999, 50, 2}, new int[]{361000, 375999, 3, 1}, new int[]{376000, 384000, 39, 2}, new int[]{386000, 398000, 2, 1}, new int[]{422000, 434000, 1, 1}, new int[]{434001, 440000, 66, 1}, new int[]{460000, 480000, 40, 2}, new int[]{499200, 523999, 41, 2}, new int[]{620000, 653333, 78, 2}, new int[]{653334, 680000, 77, 2}, new int[]{693334, 733333, 79, 2}};
    private static final int[][] DUPLEX_TABLE = {new int[]{123400, 130400, 1}, new int[]{151600, 164200, 1}, new int[]{173800, 178800, 1}, new int[]{185000, 192000, 1}, new int[]{285400, 294999, 2}, new int[]{361000, 375999, 1}, new int[]{376000, 384000, 2}, new int[]{386000, 398999, 1}, new int[]{422000, 440000, 1}, new int[]{460000, 480000, 2}, new int[]{499200, 523999, 2}, new int[]{620000, 680000, 2}, new int[]{693334, 733333, 2}};

    public static int getDuplexTypeFromNrarfcn(int i) {
        if (i <= 0) {
            return 0;
        }
        for (int[] iArr : DUPLEX_TABLE) {
            if (i >= iArr[0] && i <= iArr[1]) {
                return iArr[2];
            }
        }
        return 0;
    }

    public static int getBandFromNrarfcn(int i) {
        if (i <= 0) {
            return 0;
        }
        for (int[] iArr : BAND_TABLE) {
            if (i >= iArr[0] && i <= iArr[1]) {
                return iArr[2];
            }
        }
        return 0;
    }
}
