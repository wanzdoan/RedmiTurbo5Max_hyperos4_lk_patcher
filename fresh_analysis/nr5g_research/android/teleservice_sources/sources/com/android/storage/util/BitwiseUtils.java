package com.android.storage.util;

import com.android.storage.s2.S2Support$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class BitwiseUtils {
    private static final long[] HIGH_BITS_MASK;
    private static final long[] LOW_BITS_MASK;
    private static final long[] SINGLE_BIT_MASK = new long[64];

    static {
        long j = 1;
        for (int i = 0; i < 64; i++) {
            SINGLE_BIT_MASK[i] = j;
            j <<= 1;
        }
        LOW_BITS_MASK = new long[65];
        long j2 = 0;
        for (int i2 = 0; i2 < 65; i2++) {
            LOW_BITS_MASK[i2] = j2;
            j2 = (j2 << 1) | 1;
        }
        HIGH_BITS_MASK = new long[65];
        long j3 = -1;
        for (int i3 = 0; i3 < 65; i3++) {
            HIGH_BITS_MASK[64 - i3] = j3;
            j3 <<= 1;
        }
    }

    public static long getLowBitsMask(int i) {
        if (i < 0 || i > 64) {
            S2Support$$ExternalSyntheticBUOutline0.m("bitCount ", i, " out of range");
            return 0L;
        }
        return LOW_BITS_MASK[i];
    }

    public static long getMidBitsMask(int i, int i2) {
        if (i < 1 || i > 64) {
            S2Support$$ExternalSyntheticBUOutline0.m("totalBitCount ", i, " out of range");
            return 0L;
        }
        if (i2 < 1 || i2 > i) {
            S2Support$$ExternalSyntheticBUOutline0.m("highBitCount ", i2, " out of range");
            return 0L;
        }
        return LOW_BITS_MASK[i] & HIGH_BITS_MASK[(64 - i) + i2];
    }

    public static long getHighBitsMask(int i) {
        if (i < 1 || i > 64) {
            S2Support$$ExternalSyntheticBUOutline0.m("bitCount ", i, " out of range");
            return 0L;
        }
        return HIGH_BITS_MASK[i];
    }

    public static long extractLowBits(int i, long j) {
        if (i >= 1 && i <= 64) {
            return i == 64 ? j : getLowBitsMask(i) & j;
        }
        S2Support$$ExternalSyntheticBUOutline0.m("bitCount ", i, " out of range");
        return 0L;
    }

    public static long getSingleBitMask(int i) {
        if (i < 0 || i > 63) {
            S2Support$$ExternalSyntheticBUOutline0.m("bit ", i, " out of range");
            return 0L;
        }
        return SINGLE_BIT_MASK[i];
    }

    public static long signExtendToLong(int i, long j) {
        if (i > 64) {
            S2Support$$ExternalSyntheticBUOutline0.m("bitCount ", i, " out of range");
            return 0L;
        }
        if (i == 64) {
            return j;
        }
        long singleBitMask = getSingleBitMask(i - 1);
        if ((singleBitMask & j) == singleBitMask) {
            return j | getHighBitsMask(64 - i);
        }
        return j & getLowBitsMask(i);
    }

    public static long maxUnsignedValue(int i) {
        if (i < 1 || i > 63) {
            S2Support$$ExternalSyntheticBUOutline0.m("bitCount ", i, " out of range");
            return 0L;
        }
        return LOW_BITS_MASK[i];
    }

    public static String toUnsignedString(int i, long j) {
        String binaryString = Long.toBinaryString(j);
        if (binaryString.length() > i) {
            throw new IllegalArgumentException("value=" + j + " is not representable in " + i + " bits");
        }
        StringBuilder sb = new StringBuilder(i);
        for (int i2 = 0; i2 < i - binaryString.length(); i2++) {
            sb.append('0');
        }
        sb.append(binaryString);
        return sb.toString();
    }
}
