package com.android.telephony.sats2range.read;

import com.android.storage.s2.S2Support;
import com.android.storage.s2.S2Support$$ExternalSyntheticBUOutline0;
import com.android.storage.util.BitwiseUtils;
import com.android.storage.util.Conditions;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class SatS2RangeFileFormat {
    private final int mDataS2Level;
    private final int mEntryValueSizeInBytes;
    private final boolean mIsAllowedList;
    private final int mMaxPrefixValue;
    private final int mMaxSuffixValue;
    private final int mPrefixBitCount;
    private final int mSuffixBitCount;
    private final int mSuffixTableBlockIdOffset;
    private final int mTableEntryBitCount;
    private final int mTableEntryByteCount;
    private final int mTableEntryMaxRangeLengthValue;
    private final int mTableEntryRangeLengthBitCount;
    private final int mUnusedCellIdBitCount;
    private final int mVersionNumber;

    public SatS2RangeFileFormat(int i, int i2, int i3, int i4, int i5, boolean z) {
        this(i, i2, i3, i4, i5, z, 0, 0);
    }

    public SatS2RangeFileFormat(int i, int i2, int i3, int i4, int i5, boolean z, int i6, int i7) {
        Conditions.checkArgInRange("s2Level", i, 0, 30);
        Conditions.checkArgInRange("prefixBitCount", i2, 4, 31);
        Conditions.checkArgInRange("tableEntryKeyBitCount", i3, 1, 31);
        int i8 = i5 - i3;
        Conditions.checkArgInRange("tableEntryValueBitCount", i8, 1, 32);
        int i9 = i2 + i3;
        if (S2Support.storageBitCountForLevel(i) != i9) {
            throw new IllegalArgumentException("prefixBitCount=" + i2 + " + suffixBitCount=" + i3 + " must be correct for the s2Level (" + S2Support.storageBitCountForLevel(i) + ")");
        }
        if (i4 < 1) {
            S2Support$$ExternalSyntheticBUOutline0.m("suffixTableBlockIdOffset=", i4, " must be >= 1");
            throw null;
        }
        if (i5 < 0 || i5 % 8 != 0 || i5 > 64) {
            S2Support$$ExternalSyntheticBUOutline0.m("suffixTableEntryBitCount=", i5, " must be >= 0, be divisible by 8, and be no more than 64 bits");
            throw null;
        }
        Conditions.checkArgInRange("entryRangeLengthBitCount", i8, 2, 31);
        this.mDataS2Level = i;
        this.mPrefixBitCount = i2;
        int i10 = i2 - 3;
        this.mMaxPrefixValue = (int) (BitwiseUtils.maxUnsignedValue(i10) | (5 << i10));
        this.mSuffixBitCount = i3;
        this.mMaxSuffixValue = (int) BitwiseUtils.maxUnsignedValue(i3);
        this.mUnusedCellIdBitCount = 64 - i9;
        this.mTableEntryBitCount = i5;
        this.mTableEntryByteCount = i5 / 8;
        this.mTableEntryRangeLengthBitCount = i8;
        this.mTableEntryMaxRangeLengthValue = (int) BitwiseUtils.maxUnsignedValue(i8);
        this.mSuffixTableBlockIdOffset = i4;
        this.mIsAllowedList = z;
        Conditions.checkArgInRange("entryValueSizeInBytes", i6, 0, 4);
        this.mEntryValueSizeInBytes = i6;
        this.mVersionNumber = i7;
    }

    public int getS2Level() {
        return this.mDataS2Level;
    }

    public int getPrefixBitCount() {
        return this.mPrefixBitCount;
    }

    public int getMaxPrefixValue() {
        return this.mMaxPrefixValue;
    }

    public int getMaxSuffixValue() {
        return this.mMaxSuffixValue;
    }

    public int getTableEntryMaxRangeLengthValue() {
        return this.mTableEntryMaxRangeLengthValue;
    }

    public int getSuffixTableBlockIdOffset() {
        return this.mSuffixTableBlockIdOffset;
    }

    public boolean isAllowedList() {
        return this.mIsAllowedList;
    }

    public int extractPrefixValueFromCellId(long j) {
        checkS2Level("cellId", j);
        return (int) (j >>> (this.mSuffixBitCount + this.mUnusedCellIdBitCount));
    }

    public int extractSuffixValueFromCellId(long j) {
        checkS2Level("cellId", j);
        return this.mMaxSuffixValue & ((int) (j >>> this.mUnusedCellIdBitCount));
    }

    public int extractRangeLengthFromTableEntryValue(int i) {
        return (int) (((long) i) & BitwiseUtils.getLowBitsMask(this.mTableEntryRangeLengthBitCount));
    }

    public long createCellId(int i, int i2) {
        Conditions.checkArgInRange("prefixValue", i, 0, this.mMaxPrefixValue);
        Conditions.checkArgInRange("suffixValue", i2, 0, this.mMaxSuffixValue);
        long j = (((((long) i2) | (((long) i) << this.mSuffixBitCount)) << 1) | 1) << (this.mUnusedCellIdBitCount - 1);
        checkS2Level("cellId", j);
        return j;
    }

    public int extractFaceIdFromPrefix(int i) {
        return i >>> (this.mPrefixBitCount - 3);
    }

    public String cellIdToString(long j) {
        int iExtractPrefixValueFromCellId = extractPrefixValueFromCellId(j);
        int iExtractSuffixValueFromCellId = extractSuffixValueFromCellId(j);
        return "cellId{prefix=" + iExtractPrefixValueFromCellId + " (" + BitwiseUtils.toUnsignedString(this.mPrefixBitCount, iExtractPrefixValueFromCellId) + "), suffix=" + iExtractSuffixValueFromCellId + " (" + BitwiseUtils.toUnsignedString(this.mSuffixBitCount, iExtractSuffixValueFromCellId) + ")}";
    }

    public int getEntryValueSizeInBytes() {
        return this.mEntryValueSizeInBytes;
    }

    public String toString() {
        return "SatS2RangeFileFormat{mDataS2Level=" + this.mDataS2Level + ", mPrefixBitCount=" + this.mPrefixBitCount + ", mMaxPrefixValue=" + this.mMaxPrefixValue + ", mSuffixBitCount=" + this.mSuffixBitCount + ", mMaxSuffixValue=" + this.mMaxSuffixValue + ", mTableEntryBitCount=" + this.mTableEntryBitCount + ", mTableEntryRangeLengthBitCount=" + this.mTableEntryRangeLengthBitCount + ", mTableEntryMaxRangeLengthValue=" + this.mTableEntryMaxRangeLengthValue + ", mSuffixTableBlockIdOffset=" + this.mSuffixTableBlockIdOffset + ", mUnusedCellIdBitCount=" + this.mUnusedCellIdBitCount + ", mIsAllowedList=" + this.mIsAllowedList + ", mEntryValueSizeInBytes=" + this.mEntryValueSizeInBytes + ", mVersionNumber=" + this.mVersionNumber + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && SatS2RangeFileFormat.class == obj.getClass()) {
            SatS2RangeFileFormat satS2RangeFileFormat = (SatS2RangeFileFormat) obj;
            if (this.mDataS2Level == satS2RangeFileFormat.mDataS2Level && this.mPrefixBitCount == satS2RangeFileFormat.mPrefixBitCount && this.mMaxPrefixValue == satS2RangeFileFormat.mMaxPrefixValue && this.mSuffixBitCount == satS2RangeFileFormat.mSuffixBitCount && this.mMaxSuffixValue == satS2RangeFileFormat.mMaxSuffixValue && this.mTableEntryBitCount == satS2RangeFileFormat.mTableEntryBitCount && this.mTableEntryRangeLengthBitCount == satS2RangeFileFormat.mTableEntryRangeLengthBitCount && this.mTableEntryMaxRangeLengthValue == satS2RangeFileFormat.mTableEntryMaxRangeLengthValue && this.mSuffixTableBlockIdOffset == satS2RangeFileFormat.mSuffixTableBlockIdOffset && this.mIsAllowedList == satS2RangeFileFormat.mIsAllowedList && this.mUnusedCellIdBitCount == satS2RangeFileFormat.mUnusedCellIdBitCount && this.mEntryValueSizeInBytes == satS2RangeFileFormat.mEntryValueSizeInBytes && this.mVersionNumber == satS2RangeFileFormat.mVersionNumber) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mDataS2Level), Integer.valueOf(this.mPrefixBitCount), Integer.valueOf(this.mMaxPrefixValue), Integer.valueOf(this.mSuffixBitCount), Integer.valueOf(this.mMaxSuffixValue), Integer.valueOf(this.mTableEntryBitCount), Integer.valueOf(this.mTableEntryRangeLengthBitCount), Integer.valueOf(this.mTableEntryMaxRangeLengthValue), Integer.valueOf(this.mSuffixTableBlockIdOffset), Boolean.valueOf(this.mIsAllowedList), Integer.valueOf(this.mUnusedCellIdBitCount), Integer.valueOf(this.mEntryValueSizeInBytes), Integer.valueOf(this.mVersionNumber));
    }

    private void checkS2Level(String str, long j) {
        if (S2Support.getS2Level(j) == this.mDataS2Level) {
            return;
        }
        throw new IllegalArgumentException(str + "=" + S2Support.cellIdToString(j) + " is at the wrong level");
    }
}
