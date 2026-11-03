package com.android.telephony.sats2range.read;

import com.android.storage.block.read.TypedData;
import com.android.storage.s2.S2Support$$ExternalSyntheticBUOutline0;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class SuffixTableSharedData {
    private final int mEntryValueSizeInBytes;
    private List mEntryValuesToWrite = Collections.EMPTY_LIST;
    private final int mHeaderByteOffsetToRead;
    private final int mNumberOfEntryValues;
    private final TypedData mSharedDataToRead;
    private final int mTablePrefix;

    public SuffixTableSharedData(TypedData typedData, SatS2RangeFileFormat satS2RangeFileFormat) {
        Objects.requireNonNull(typedData);
        TypedData typedData2 = typedData;
        this.mSharedDataToRead = typedData2;
        this.mTablePrefix = typedData2.getInt(0);
        if (8 < typedData2.getSize()) {
            this.mNumberOfEntryValues = typedData2.getInt(4);
            this.mHeaderByteOffsetToRead = 8;
            this.mEntryValueSizeInBytes = satS2RangeFileFormat.getEntryValueSizeInBytes();
        } else {
            this.mNumberOfEntryValues = 0;
            this.mHeaderByteOffsetToRead = 4;
            this.mEntryValueSizeInBytes = 0;
        }
    }

    public static SuffixTableSharedData fromTypedData(TypedData typedData, SatS2RangeFileFormat satS2RangeFileFormat) {
        return new SuffixTableSharedData(typedData, satS2RangeFileFormat);
    }

    public int getEntryValue(int i) {
        int i2;
        TypedData typedData = this.mSharedDataToRead;
        if (typedData != null && i >= 0 && (i2 = this.mNumberOfEntryValues) != 0) {
            if (i2 == 1) {
                i = 0;
            }
            if (i < i2) {
                int i3 = this.mHeaderByteOffsetToRead;
                int i4 = this.mEntryValueSizeInBytes;
                return getValueInternal(typedData, i4, i3 + (i * i4));
            }
        }
        return -1;
    }

    public int getTablePrefix() {
        return this.mTablePrefix;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && SuffixTableSharedData.class == obj.getClass()) {
            SuffixTableSharedData suffixTableSharedData = (SuffixTableSharedData) obj;
            if (this.mTablePrefix == suffixTableSharedData.mTablePrefix && this.mNumberOfEntryValues == suffixTableSharedData.mNumberOfEntryValues && this.mEntryValuesToWrite.equals(suffixTableSharedData.mEntryValuesToWrite)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mTablePrefix), Integer.valueOf(this.mNumberOfEntryValues), this.mEntryValuesToWrite);
    }

    public String toString() {
        return "SuffixTableSharedData{mTablePrefix=" + this.mTablePrefix + "mNumberOfEntries=" + this.mNumberOfEntryValues + "mEntryValuesToWrite=" + this.mEntryValuesToWrite + '}';
    }

    private int getValueInternal(TypedData typedData, int i, int i2) {
        if (i2 < 0) {
            S2Support$$ExternalSyntheticBUOutline0.m("byteOffset=", i2, " must not be negative");
            return 0;
        }
        int i3 = 0;
        int i4 = i2;
        int i5 = 0;
        while (true) {
            int i6 = i3 + 1;
            if (i3 >= i) {
                return i5;
            }
            int unsignedByte = (i5 << 8) | typedData.getUnsignedByte(i4);
            i4++;
            i5 = unsignedByte;
            i3 = i6;
        }
    }
}
