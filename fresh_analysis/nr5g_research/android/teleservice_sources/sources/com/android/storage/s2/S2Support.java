package com.android.storage.s2;

import com.android.storage.util.BitwiseUtils;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class S2Support {
    private static final long[] MIN_CELL_ID = new long[31];
    private static final long[] MAX_CELL_ID = new long[31];

    static {
        int i = 0;
        while (true) {
            long[] jArr = MIN_CELL_ID;
            if (i >= jArr.length) {
                return;
            }
            jArr[i] = calcMinS2CellId(i);
            MAX_CELL_ID[i] = calcMaxS2CellId(i);
            i++;
        }
    }

    public static String cellIdToString(long j) {
        return Long.toUnsignedString(j) + " (level=" + getS2Level(j) + " faceId=" + getValidFaceId(j) + " index=" + getValidIndex(j) + ")";
    }

    private static int getValidFaceId(long j) {
        int faceIdValue = getFaceIdValue(j);
        checkValidFaceId(faceIdValue);
        return faceIdValue;
    }

    private static int getFaceIdValue(long j) {
        return (int) ((j & BitwiseUtils.getHighBitsMask(3)) >>> 61);
    }

    private static long getValidIndex(long j) {
        int iNumberOfTrailingZeros = Long.numberOfTrailingZeros(j) + 1;
        if (iNumberOfTrailingZeros > 61) {
            Folme$$ExternalSyntheticBUOutline0.m("There must be at least one bit set in every S2 cell ID");
            return 0L;
        }
        if (iNumberOfTrailingZeros % 2 != 0) {
            return (j & 2305843009213693951L) >>> iNumberOfTrailingZeros;
        }
        throw new IllegalArgumentException("Invalid number of index bits in " + j);
    }

    public static long cellId(int i, int i2, long j) {
        checkValidFaceId(i2);
        if (j < 0) {
            throw new IllegalArgumentException("index must be >= 0, index=" + j);
        }
        long maxIndex = getMaxIndex(i);
        if (j > maxIndex) {
            throw new IllegalArgumentException("index=" + j + " > the max index value at level=" + i + " (" + maxIndex + ")");
        }
        int iStorageBitCountForLevel = storageBitCountForLevel(i);
        return ((((long) i2) << (iStorageBitCountForLevel - 2)) | ((j << 1) | 1)) << (64 - (iStorageBitCountForLevel + 1));
    }

    public static long getMaxIndex(int i) {
        checkValidLevel(i);
        return BitwiseUtils.getLowBitsMask(i * 2);
    }

    public static int storageBitCountForLevel(int i) {
        checkValidLevel(i);
        return (i * 2) + 3;
    }

    public static void validateCellId(long j) {
        getValidFaceId(j);
        getValidIndex(j);
    }

    public static int getS2Level(long j) {
        int iNumberOfTrailingZeros = Long.numberOfTrailingZeros(j);
        if (iNumberOfTrailingZeros == 64) {
            throw new IllegalArgumentException("No trailing bit in " + j);
        }
        int i = 64 - iNumberOfTrailingZeros;
        if ((61 - iNumberOfTrailingZeros) % 2 != 1) {
            throw new IllegalArgumentException(j + " is not a valid cell Id, bitCount=" + i);
        }
        return (60 - iNumberOfTrailingZeros) / 2;
    }

    public static long getMinCellId(int i) {
        checkValidLevel(i);
        return MIN_CELL_ID[i];
    }

    public static long getMaxCellId(int i) {
        checkValidLevel(i);
        return MAX_CELL_ID[i];
    }

    private static void checkValidLevel(int i) {
        if (i < 0 || i > 30) {
            S2Support$$ExternalSyntheticBUOutline0.m("s2Level ", i, " is invalid");
        }
    }

    private static void checkValidFaceId(int i) {
        if (i < 0 || i > 5) {
            S2Support$$ExternalSyntheticBUOutline0.m("faceId=", i, " is invalid");
        }
    }

    private static long calcMinS2CellId(int i) {
        return cellId(i, 0, 0L);
    }

    private static long calcMaxS2CellId(int i) {
        return cellId(i, 5, getMaxIndex(i));
    }
}
