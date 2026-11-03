package com.android.telephony.sats2range.read;

import com.android.storage.s2.S2Support;
import com.android.storage.table.packed.read.IntValueTypedPackedTable;
import com.android.storage.table.reader.IntValueTable$IntValueEntryMatcher;
import com.android.storage.table.reader.IntValueTable$TableEntry;
import com.android.storage.util.Conditions;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
final class PopulatedSuffixTableBlock implements SuffixTableBlock.SuffixTableBlockDelegate {
    private final SatS2RangeFileFormat mFileFormat;
    private final IntValueTypedPackedTable mPackedTable;
    private final int mPrefix;
    private final SuffixTableSharedData mSuffixTableSharedData;

    PopulatedSuffixTableBlock(SatS2RangeFileFormat satS2RangeFileFormat, IntValueTypedPackedTable intValueTypedPackedTable) {
        Objects.requireNonNull(satS2RangeFileFormat);
        this.mFileFormat = satS2RangeFileFormat;
        Objects.requireNonNull(intValueTypedPackedTable);
        this.mPackedTable = intValueTypedPackedTable;
        SuffixTableSharedData suffixTableSharedDataFromTypedData = SuffixTableSharedData.fromTypedData(intValueTypedPackedTable.getSharedDataAsTyped(), satS2RangeFileFormat);
        this.mSuffixTableSharedData = suffixTableSharedDataFromTypedData;
        this.mPrefix = suffixTableSharedDataFromTypedData.getTablePrefix();
    }

    @Override // com.android.telephony.sats2range.read.SuffixTableBlock.SuffixTableBlockDelegate
    public int getPrefix() {
        return this.mPrefix;
    }

    @Override // com.android.telephony.sats2range.read.SuffixTableBlock.SuffixTableBlockDelegate
    public SuffixTableBlock.Entry findEntryByCellId(long j) {
        return findEntryWithMatcher(new S2CellMatcher(this.mFileFormat, this.mFileFormat.extractSuffixValueFromCellId(j)));
    }

    private SuffixTableBlock.Entry findEntryWithMatcher(IntValueTable$IntValueEntryMatcher intValueTable$IntValueEntryMatcher) {
        IntValueTable$TableEntry intValueTable$TableEntryFindEntry = this.mPackedTable.findEntry(intValueTable$IntValueEntryMatcher);
        if (intValueTable$TableEntryFindEntry == null) {
            return null;
        }
        return new Entry(intValueTable$TableEntryFindEntry);
    }

    final class S2CellMatcher implements IntValueTable$IntValueEntryMatcher {
        private final SatS2RangeFileFormat mFileFormat;
        private final int mSuffixSearchValue;

        S2CellMatcher(SatS2RangeFileFormat satS2RangeFileFormat, int i) {
            Objects.requireNonNull(satS2RangeFileFormat);
            this.mFileFormat = satS2RangeFileFormat;
            this.mSuffixSearchValue = i;
        }

        @Override // com.android.storage.table.reader.IntValueTable$IntValueEntryMatcher
        public int compare(int i, int i2) {
            if (this.mSuffixSearchValue < i) {
                return -1;
            }
            return this.mSuffixSearchValue >= i + this.mFileFormat.extractRangeLengthFromTableEntryValue(i2) ? 1 : 0;
        }
    }

    public final class Entry extends SuffixTableBlock.Entry {
        private final IntValueTable$TableEntry mSuffixTableEntry;
        private SuffixTableRange mSuffixTableRange;

        Entry(IntValueTable$TableEntry intValueTable$TableEntry) {
            Objects.requireNonNull(intValueTable$TableEntry);
            this.mSuffixTableEntry = intValueTable$TableEntry;
        }

        @Override // com.android.telephony.sats2range.read.SuffixTableBlock.Entry
        public SuffixTableRange getSuffixTableRange() {
            if (this.mSuffixTableRange == null) {
                int key = this.mSuffixTableEntry.getKey();
                Conditions.checkStateInRange("startCellIdSuffixBits", key, "minSuffixValue", 0, "maxSuffixValue", PopulatedSuffixTableBlock.this.mFileFormat.getMaxSuffixValue());
                long jCreateCellId = PopulatedSuffixTableBlock.this.mFileFormat.createCellId(PopulatedSuffixTableBlock.this.mPrefix, key);
                int iExtractRangeLengthFromTableEntryValue = PopulatedSuffixTableBlock.this.mFileFormat.extractRangeLengthFromTableEntryValue(this.mSuffixTableEntry.getValue());
                Conditions.checkStateInRange("rangeLength", iExtractRangeLengthFromTableEntryValue, "minRangeLength", 0, "maxRangeLength", PopulatedSuffixTableBlock.this.mFileFormat.getTableEntryMaxRangeLengthValue());
                int i = key + iExtractRangeLengthFromTableEntryValue;
                int i2 = PopulatedSuffixTableBlock.this.mPrefix;
                if (i > PopulatedSuffixTableBlock.this.mFileFormat.getMaxSuffixValue()) {
                    if (i != PopulatedSuffixTableBlock.this.mFileFormat.getMaxSuffixValue() + 1) {
                        throw new IllegalStateException("Range exceeds allowable cell IDs: startCellId=" + S2Support.cellIdToString(jCreateCellId) + ", rangeLength=" + iExtractRangeLengthFromTableEntryValue);
                    }
                    i2++;
                    if (PopulatedSuffixTableBlock.this.mFileFormat.extractFaceIdFromPrefix(i2) > 5) {
                        i2 = 0;
                    }
                    i = 0;
                }
                this.mSuffixTableRange = new SuffixTableRange(jCreateCellId, PopulatedSuffixTableBlock.this.mFileFormat.createCellId(i2, i), getEntryValue());
            }
            return this.mSuffixTableRange;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || Entry.class != obj.getClass()) {
                return false;
            }
            return this.mSuffixTableEntry.equals(((Entry) obj).mSuffixTableEntry);
        }

        public int hashCode() {
            return Objects.hash(this.mSuffixTableEntry);
        }

        public String toString() {
            return "Entry{mSuffixTableEntry=" + this.mSuffixTableEntry + '}';
        }

        private int getEntryValue() {
            return PopulatedSuffixTableBlock.this.mSuffixTableSharedData.getEntryValue(this.mSuffixTableEntry.getIndex());
        }
    }
}
