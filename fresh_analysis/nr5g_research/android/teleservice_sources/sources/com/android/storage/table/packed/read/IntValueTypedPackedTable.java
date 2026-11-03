package com.android.storage.table.packed.read;

import com.android.storage.block.read.BlockData;
import com.android.storage.block.read.TypedData;
import com.android.storage.table.reader.IntValueTable$IntValueEntryMatcher;
import com.android.storage.table.reader.IntValueTable$TableEntry;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public final class IntValueTypedPackedTable extends BaseTypedPackedTable {
    @Override // com.android.storage.table.packed.read.BaseTypedPackedTable
    public /* bridge */ /* synthetic */ TypedData getSharedDataAsTyped() {
        return super.getSharedDataAsTyped();
    }

    public IntValueTypedPackedTable(BlockData blockData, boolean z) {
        super(blockData, 32, z);
        boolean zIsValueSigned = this.mTableReader.isValueSigned();
        if (this.mTableReader.getValueSizeBits() != 32 || zIsValueSigned) {
            return;
        }
        Folme$$ExternalSyntheticBUOutline0.m("Cannot use unsigned with 32 bits");
        throw null;
    }

    public IntValueTable$TableEntry findEntry(IntValueTable$IntValueEntryMatcher intValueTable$IntValueEntryMatcher) {
        PackedTableReader.Entry entryFindIntValueEntry = this.mTableReader.findIntValueEntry(intValueTable$IntValueEntryMatcher);
        if (entryFindIntValueEntry == null) {
            return null;
        }
        return createEntry(entryFindIntValueEntry);
    }

    protected IntValueTable$TableEntry createEntry(PackedTableReader.Entry entry) {
        return new IntValuePackedTableEntry(entry);
    }

    class IntValuePackedTableEntry extends BaseTypedPackedTable.BasePackedTableEntry implements IntValueTable$TableEntry {
        IntValuePackedTableEntry(PackedTableReader.Entry entry) {
            super(entry);
        }

        @Override // com.android.storage.table.reader.IntValueTable$TableEntry
        public int getValue() {
            return this.mEntry.getIntValue();
        }

        public boolean equals(Object obj) {
            return IntValueTable$TableEntry.equal(this, obj);
        }

        public String toString() {
            return IntValueTable$TableEntry.toString(this);
        }
    }
}
