package com.android.storage.table.packed.read;

import com.android.storage.block.read.BlockData;
import com.android.storage.block.read.TypedData;
import com.android.storage.s2.S2Support$$ExternalSyntheticBUOutline0;
import com.android.storage.table.reader.Table$TableEntry;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
abstract class BaseTypedPackedTable {
    protected final PackedTableReader mTableReader;

    BaseTypedPackedTable(BlockData blockData, int i, boolean z) {
        PackedTableReader packedTableReader = new PackedTableReader(blockData, z);
        this.mTableReader = packedTableReader;
        if (packedTableReader.getValueSizeBits() <= i) {
            return;
        }
        S2Support$$ExternalSyntheticBUOutline0.m("The supplied packed table block can hold values greater than ", i, " bits. Use a wider value type.");
        throw null;
    }

    public TypedData getSharedDataAsTyped() {
        return this.mTableReader.getSharedDataAsTyped();
    }

    abstract class BasePackedTableEntry implements Table$TableEntry {
        protected final PackedTableReader.Entry mEntry;

        BasePackedTableEntry(PackedTableReader.Entry entry) {
            Objects.requireNonNull(entry);
            this.mEntry = entry;
        }

        @Override // com.android.storage.table.reader.Table$TableEntry
        public int getKey() {
            return this.mEntry.getKey();
        }

        @Override // com.android.storage.table.reader.Table$TableEntry
        public int getIndex() {
            return this.mEntry.getIndex();
        }

        public int hashCode() {
            return getKey();
        }
    }
}
