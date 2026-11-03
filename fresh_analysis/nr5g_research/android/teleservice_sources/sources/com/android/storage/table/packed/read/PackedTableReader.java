package com.android.storage.table.packed.read;

import com.android.phone.settings.CommonOptions$$ExternalSyntheticBUOutline0;
import com.android.storage.block.read.BlockData;
import com.android.storage.block.read.TypedData;
import com.android.storage.table.reader.IntValueTable$IntValueEntryMatcher;
import com.android.storage.table.reader.LongValueTable$LongValueEntryMatcher;
import com.android.storage.util.BitwiseUtils;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class PackedTableReader {
    private final BlockData mBlockData;
    private final int mEntryCount;
    private final int mEntrySizeBits;
    private final int mEntrySizeBytes;
    private final int mHeaderLengthBytes;
    private final boolean mIntValueSupported;
    private final int mKeySizeBits;
    private final TypedData mSharedData;
    private final boolean mSignedValue;
    private final int mValueSizeBits;

    public PackedTableReader(BlockData blockData, boolean z) {
        int i;
        int i2;
        Objects.requireNonNull(blockData);
        this.mBlockData = blockData;
        if (z) {
            i = blockData.getInt(0);
            i2 = 4;
        } else {
            i = blockData.getByte(0);
            i2 = 1;
        }
        this.mSharedData = blockData.slice(i2, i);
        int i3 = i2 + i;
        int i4 = i3 + 1;
        boolean z2 = (blockData.getUnsignedByte(i3) & 1) > 0;
        this.mSignedValue = z2;
        int unsignedByte = blockData.getUnsignedByte(i4);
        this.mEntrySizeBytes = unsignedByte;
        int i5 = i3 + 2;
        if (unsignedByte > 8) {
            CommonOptions$$ExternalSyntheticBUOutline0.m("Entries cannot exceed 8 bytes in width, entrySizeBytes=", unsignedByte);
            throw null;
        }
        int i6 = unsignedByte * 8;
        this.mEntrySizeBits = i6;
        int unsignedByte2 = blockData.getUnsignedByte(i5);
        this.mKeySizeBits = unsignedByte2;
        int i7 = i3 + 3;
        int iMin = Math.min(31, i6 - 1);
        if (unsignedByte2 < 1 || unsignedByte2 > iMin) {
            CommonOptions$$ExternalSyntheticBUOutline0.m("keySizeBits must be >= 1 and <= ", iMin);
            throw null;
        }
        int i8 = i6 - unsignedByte2;
        this.mValueSizeBits = i8;
        if (z2) {
            this.mIntValueSupported = i8 <= 32;
        } else {
            this.mIntValueSupported = i8 <= 31;
        }
        this.mHeaderLengthBytes = i7;
        this.mEntryCount = (blockData.getSize() - i7) / unsignedByte;
    }

    public boolean isValueSigned() {
        return this.mSignedValue;
    }

    public int getValueSizeBits() {
        return this.mValueSizeBits;
    }

    public TypedData getSharedDataAsTyped() {
        return this.mSharedData;
    }

    public Entry findIntValueEntry(IntValueTable$IntValueEntryMatcher intValueTable$IntValueEntryMatcher) {
        checkIntValueSupported();
        return findLongValueEntry(convertToLongValueEntryMatcher(intValueTable$IntValueEntryMatcher));
    }

    public Entry findLongValueEntry(LongValueTable$LongValueEntryMatcher longValueTable$LongValueEntryMatcher) {
        return binarySearch(0, this.mEntryCount - 1, longValueTable$LongValueEntryMatcher);
    }

    private Entry binarySearch(int i, int i2, LongValueTable$LongValueEntryMatcher longValueTable$LongValueEntryMatcher) {
        while (i <= i2) {
            int i3 = (i + i2) / 2;
            long entryBytesForIndex = getEntryBytesForIndex(i3);
            int iCompare = longValueTable$LongValueEntryMatcher.compare(getKeyFromEntryBytes(entryBytesForIndex), getLongValueFromEntryBytes(entryBytesForIndex));
            if (iCompare > 0) {
                i = i3 + 1;
            } else {
                if (iCompare >= 0) {
                    return new Entry(i3, entryBytesForIndex);
                }
                i2 = i3 - 1;
            }
        }
        return null;
    }

    private long getEntryBytesForIndex(int i) {
        if (i < 0 || i >= this.mEntryCount) {
            throw new IndexOutOfBoundsException();
        }
        int i2 = this.mHeaderLengthBytes;
        int i3 = this.mEntrySizeBytes;
        return this.mBlockData.getValueAsLong(i3, i2 + (i * i3), this.mSignedValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getKeyFromEntryBytes(long j) {
        return getKeyFromEntryBytes(this.mEntrySizeBits, this.mKeySizeBits, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getIntValueFromEntryBytes(long j) {
        checkIntValueSupported();
        return (int) getLongValueFromEntryBytes(j);
    }

    private long getLongValueFromEntryBytes(long j) {
        long jExtractLowBits = BitwiseUtils.extractLowBits(this.mValueSizeBits, j);
        return this.mSignedValue ? BitwiseUtils.signExtendToLong(this.mValueSizeBits, jExtractLowBits) : jExtractLowBits;
    }

    private void checkIntValueSupported() {
        if (this.mIntValueSupported) {
            return;
        }
        throw new IllegalStateException("value size is too big to support int, mSignedValue=" + this.mSignedValue + ", mValueSizeBits=" + this.mValueSizeBits);
    }

    public final class Entry {
        private final long mEntryBytes;
        final int mIndex;
        private int mKey = -1;

        Entry(int i, long j) {
            this.mIndex = i;
            this.mEntryBytes = j;
        }

        public int getKey() {
            if (this.mKey == -1) {
                this.mKey = PackedTableReader.this.getKeyFromEntryBytes(this.mEntryBytes);
            }
            return this.mKey;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public int getIntValue() {
            return PackedTableReader.this.getIntValueFromEntryBytes(this.mEntryBytes);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && Entry.class == obj.getClass()) {
                Entry entry = (Entry) obj;
                if (this.mIndex == entry.mIndex && this.mEntryBytes == entry.mEntryBytes) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mIndex));
        }
    }

    private static int getKeyFromEntryBytes(int i, int i2, long j) {
        return (int) ((j & BitwiseUtils.getMidBitsMask(i, i2)) >>> (i - i2));
    }

    private static LongValueTable$LongValueEntryMatcher convertToLongValueEntryMatcher(final IntValueTable$IntValueEntryMatcher intValueTable$IntValueEntryMatcher) {
        return new LongValueTable$LongValueEntryMatcher() { // from class: com.android.storage.table.packed.read.PackedTableReader$$ExternalSyntheticLambda0
            @Override // com.android.storage.table.reader.LongValueTable$LongValueEntryMatcher
            public final int compare(int i, long j) {
                return intValueTable$IntValueEntryMatcher.compare(i, (int) j);
            }
        };
    }
}
