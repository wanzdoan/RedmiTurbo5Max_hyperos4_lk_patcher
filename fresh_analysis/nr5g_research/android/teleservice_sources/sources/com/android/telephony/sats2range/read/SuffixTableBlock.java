package com.android.telephony.sats2range.read;

import com.android.storage.block.read.BlockData;
import com.android.storage.s2.S2Support;
import com.android.storage.table.packed.read.IntValueTypedPackedTable;
import com.android.storage.util.BitwiseUtils;
import java.util.Objects;
import org.tensorflow.lite.DataType$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public final class SuffixTableBlock {
    private final SuffixTableBlockDelegate mDelegate;
    private final SatS2RangeFileFormat mFileFormat;
    private final int mPrefix;

    public abstract class Entry {
        public abstract SuffixTableRange getSuffixTableRange();
    }

    interface SuffixTableBlockDelegate {
        Entry findEntryByCellId(long j);

        int getPrefix();
    }

    private SuffixTableBlock(SatS2RangeFileFormat satS2RangeFileFormat, SuffixTableBlockDelegate suffixTableBlockDelegate) {
        Objects.requireNonNull(satS2RangeFileFormat);
        this.mFileFormat = satS2RangeFileFormat;
        Objects.requireNonNull(suffixTableBlockDelegate);
        this.mDelegate = suffixTableBlockDelegate;
        this.mPrefix = suffixTableBlockDelegate.getPrefix();
    }

    public static SuffixTableBlock createPopulated(SatS2RangeFileFormat satS2RangeFileFormat, BlockData blockData) {
        if (blockData.getSize() == 0) {
            DataType$$ExternalSyntheticBUOutline0.m("blockData=", blockData, ", is zero length");
            return null;
        }
        return new SuffixTableBlock(satS2RangeFileFormat, new PopulatedSuffixTableBlock(satS2RangeFileFormat, new IntValueTypedPackedTable(blockData, true)));
    }

    public static SuffixTableBlock createEmpty(SatS2RangeFileFormat satS2RangeFileFormat, int i) {
        return new SuffixTableBlock(satS2RangeFileFormat, new UnpopulatedSuffixTableBlock(i));
    }

    public int getPrefix() {
        return this.mDelegate.getPrefix();
    }

    public Entry findEntryByCellId(long j) {
        if (S2Support.getS2Level(j) != this.mFileFormat.getS2Level()) {
            throw new IllegalArgumentException(S2Support.cellIdToString(j) + " s2 level is not " + this.mFileFormat.getS2Level());
        }
        if (this.mFileFormat.extractPrefixValueFromCellId(j) != this.mPrefix) {
            String unsignedString = BitwiseUtils.toUnsignedString(this.mFileFormat.getPrefixBitCount(), this.mPrefix);
            throw new IllegalArgumentException(j + "(" + this.mFileFormat.cellIdToString(j) + ") does not have prefix bits " + this.mPrefix + " (" + unsignedString + ")");
        }
        return this.mDelegate.findEntryByCellId(j);
    }
}
