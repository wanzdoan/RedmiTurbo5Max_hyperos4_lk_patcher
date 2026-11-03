package com.android.telephony.sats2range.read;

import ai.onnxruntime.OrtSession$Result$$ExternalSyntheticBUOutline0;
import androidx.recyclerview.widget.RecyclerView$$ExternalSyntheticBUOutline0;
import com.android.phone.settings.CommonOptions$$ExternalSyntheticBUOutline0;
import com.android.storage.block.read.Block;
import com.android.storage.block.read.BlockFileReader;
import com.android.storage.block.read.BlockInfo;
import com.android.storage.s2.S2Support;
import com.android.storage.util.Conditions;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import miuix.pickerwidget.date.Calendar$$ExternalSyntheticBUOutline1;

/* JADX INFO: loaded from: classes.dex */
public final class SatS2RangeFileReader implements AutoCloseable {
    private final BlockFileReader mBlockFileReader;
    private boolean mClosed;
    private SatS2RangeFileFormat mFileFormat;
    private HeaderBlock mHeaderBlock;
    private SuffixTableExtraInfo[] mSuffixTableExtraInfos;

    private SatS2RangeFileReader(BlockFileReader blockFileReader) {
        Objects.requireNonNull(blockFileReader);
        this.mBlockFileReader = blockFileReader;
    }

    public static SatS2RangeFileReader open(File file) {
        SatS2RangeFileReader satS2RangeFileReader = new SatS2RangeFileReader(BlockFileReader.open(false, file, (char) 53167, 1));
        satS2RangeFileReader.initialize();
        return satS2RangeFileReader;
    }

    private void initialize() {
        BlockInfo blockInfo = this.mBlockFileReader.getBlockInfo(0);
        if (blockInfo.getType() != 1) {
            RecyclerView$$ExternalSyntheticBUOutline0.m("headerBlockInfo.getType()=", blockInfo.getType(), " must be 1");
            return;
        }
        Block block = this.mBlockFileReader.getBlock(0);
        if (block.getType() != 1) {
            RecyclerView$$ExternalSyntheticBUOutline0.m("firstBlock.getType()=", block.getType(), " must be 1");
            return;
        }
        HeaderBlock headerBlockWrap = HeaderBlock.wrap(block.getData());
        this.mHeaderBlock = headerBlockWrap;
        SatS2RangeFileFormat fileFormat = headerBlockWrap.getFileFormat();
        this.mFileFormat = fileFormat;
        this.mSuffixTableExtraInfos = new SuffixTableExtraInfo[fileFormat.getMaxPrefixValue() + 1];
        for (int i = 0; i < this.mSuffixTableExtraInfos.length; i++) {
            BlockInfo blockInfo2 = this.mBlockFileReader.getBlockInfo(this.mFileFormat.getSuffixTableBlockIdOffset() + i);
            int type = blockInfo2.getType();
            if (type == 10) {
                this.mSuffixTableExtraInfos[i] = SuffixTableExtraInfo.create(this.mFileFormat, blockInfo2);
            } else {
                CommonOptions$$ExternalSyntheticBUOutline0.m("Unknown block type=", type);
                return;
            }
        }
    }

    public SuffixTableRange findEntryByCellId(long j) throws IOException {
        checkNotClosed();
        int s2Level = this.mFileFormat.getS2Level();
        int s2Level2 = S2Support.getS2Level(j);
        if (s2Level != s2Level2) {
            Calendar$$ExternalSyntheticBUOutline1.m("data S2 level=", s2Level, ", search S2 level=", s2Level2);
            return null;
        }
        SuffixTableBlock.Entry entryFindEntryByCellId = getSuffixTableBlockForPrefix(this.mFileFormat.extractPrefixValueFromCellId(j)).findEntryByCellId(j);
        if (entryFindEntryByCellId == null) {
            return null;
        }
        return entryFindEntryByCellId.getSuffixTableRange();
    }

    private SuffixTableExtraInfo getSuffixTableExtraInfoForPrefix(int i) {
        Conditions.checkArgInRange("prefixValue", i, "minPrefixValue", 0, "maxPrefixValue", this.mFileFormat.getMaxPrefixValue());
        return this.mSuffixTableExtraInfos[i];
    }

    private SuffixTableBlock getSuffixTableBlockForPrefix(int i) {
        if (getSuffixTableExtraInfoForPrefix(i).isEmpty()) {
            return SuffixTableBlock.createEmpty(this.mFileFormat, i);
        }
        SuffixTableBlock suffixTableBlockCreatePopulated = SuffixTableBlock.createPopulated(this.mFileFormat, this.mBlockFileReader.getBlock(this.mFileFormat.getSuffixTableBlockIdOffset() + i).getData());
        if (i == suffixTableBlockCreatePopulated.getPrefix()) {
            return suffixTableBlockCreatePopulated;
        }
        OrtSession$Result$$ExternalSyntheticBUOutline0.m("prefixValue=", i, " != suffixTableBlock.getPrefix()=", suffixTableBlockCreatePopulated.getPrefix());
        return null;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mClosed = true;
        this.mHeaderBlock = null;
        this.mBlockFileReader.close();
    }

    private void checkNotClosed() throws IOException {
        if (this.mClosed) {
            throw new IOException("Closed");
        }
    }

    public int getS2Level() throws IOException {
        checkNotClosed();
        return this.mHeaderBlock.getFileFormat().getS2Level();
    }

    public boolean isAllowedList() {
        return this.mFileFormat.isAllowedList();
    }
}
