package com.android.storage.s2;

/* JADX INFO: loaded from: classes.dex */
public abstract class S2CellOrdering {
    private static final long[] MIN_CELL_ID_NUMERIC = new long[31];
    private static final long[] MAX_CELL_ID_NUMERIC = new long[31];

    public static long asUnsignedNumeric(long j) {
        return j >>> 1;
    }

    static {
        int i = 0;
        while (true) {
            long[] jArr = MIN_CELL_ID_NUMERIC;
            if (i >= jArr.length) {
                return;
            }
            jArr[i] = asUnsignedNumeric(S2Support.getMinCellId(i));
            MAX_CELL_ID_NUMERIC[i] = asUnsignedNumeric(S2Support.getMaxCellId(i));
            i++;
        }
    }
}
