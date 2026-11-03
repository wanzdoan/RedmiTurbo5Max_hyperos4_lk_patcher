package com.android.telephony.sats2range.read;

import com.android.storage.block.read.BlockData;

/* JADX INFO: loaded from: classes.dex */
public final class HeaderBlock {
    private final SatS2RangeFileFormat mFileFormat;

    private HeaderBlock(BlockData blockData) {
        int unsignedByte = blockData.getUnsignedByte(0);
        int unsignedByte2 = blockData.getUnsignedByte(1);
        int unsignedByte3 = blockData.getUnsignedByte(2);
        int unsignedByte4 = blockData.getUnsignedByte(3);
        int unsignedByte5 = blockData.getUnsignedByte(4);
        boolean z = blockData.getUnsignedByte(5) == 1;
        if (6 < blockData.getSize()) {
            this.mFileFormat = new SatS2RangeFileFormat(unsignedByte, unsignedByte2, unsignedByte3, unsignedByte5, unsignedByte4, z, blockData.getUnsignedByte(6), blockData.getInt(7));
        } else {
            this.mFileFormat = new SatS2RangeFileFormat(unsignedByte, unsignedByte2, unsignedByte3, unsignedByte5, unsignedByte4, z);
        }
    }

    public static HeaderBlock wrap(BlockData blockData) {
        return new HeaderBlock(blockData);
    }

    public SatS2RangeFileFormat getFileFormat() {
        return this.mFileFormat;
    }
}
