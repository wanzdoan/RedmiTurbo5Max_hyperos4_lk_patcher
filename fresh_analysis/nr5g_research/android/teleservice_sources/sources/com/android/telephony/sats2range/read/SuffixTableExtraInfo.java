package com.android.telephony.sats2range.read;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import androidx.recyclerview.widget.RecyclerView$$ExternalSyntheticBUOutline0;
import com.android.storage.block.read.BlockInfo;
import com.android.storage.io.read.TypedInputStream;
import com.android.storage.s2.S2Support$$ExternalSyntheticBUOutline0;
import com.android.storage.util.Conditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class SuffixTableExtraInfo {
    private final int mEntryCount;
    private final int mPrefix;

    public SuffixTableExtraInfo(int i, int i2) {
        if (i < 0) {
            S2Support$$ExternalSyntheticBUOutline0.m("prefix=", i, " must be >= 0");
            throw null;
        }
        this.mPrefix = i;
        if (i2 < 0) {
            S2Support$$ExternalSyntheticBUOutline0.m("entryCount=", i2, " must be >= 0");
            throw null;
        }
        this.mEntryCount = i2;
    }

    public static SuffixTableExtraInfo create(SatS2RangeFileFormat satS2RangeFileFormat, BlockInfo blockInfo) {
        if (blockInfo.getType() != 10) {
            RecyclerView$$ExternalSyntheticBUOutline0.m("blockType=", blockInfo.getType(), " is not of expected type=10");
            return null;
        }
        int id = blockInfo.getId() - satS2RangeFileFormat.getSuffixTableBlockIdOffset();
        if (blockInfo.getBlockSizeBytes() == 0) {
            return new SuffixTableExtraInfo(id, 0);
        }
        byte[] extraBytes = blockInfo.getExtraBytes();
        if (extraBytes == null || extraBytes.length == 0) {
            OrtProvider$$ExternalSyntheticBUOutline0.m("Extra bytes null or empty in blockInfo=", blockInfo);
            return null;
        }
        try {
            TypedInputStream typedInputStream = new TypedInputStream(new ByteArrayInputStream(extraBytes));
            try {
                int i = typedInputStream.readInt();
                Conditions.checkStateInRange("entryCount", i, "minSuffixValue", 0, "maxSuffixValue", satS2RangeFileFormat.getMaxSuffixValue());
                SuffixTableExtraInfo suffixTableExtraInfo = new SuffixTableExtraInfo(id, i);
                typedInputStream.close();
                return suffixTableExtraInfo;
            } catch (Throwable th) {
                try {
                    typedInputStream.close();
                    throw th;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                    throw th;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected exception while reading a byte[]", e);
        }
    }

    public boolean isEmpty() {
        return this.mEntryCount == 0;
    }
}
