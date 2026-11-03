package com.android.storage.block.read;

import com.android.storage.s2.S2Support$$ExternalSyntheticBUOutline0;
import com.android.storage.util.BitwiseUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public final class BlockData implements TypedData {
    private final ByteBuffer mDataBytes;

    public BlockData(ByteBuffer byteBuffer) {
        if (!byteBuffer.isReadOnly()) {
            Folme$$ExternalSyntheticBUOutline0.m("dataBytes must be readonly");
            throw null;
        }
        if (byteBuffer.order() != ByteOrder.BIG_ENDIAN) {
            Folme$$ExternalSyntheticBUOutline0.m("dataBytes must be big-endian");
            throw null;
        }
        this.mDataBytes = byteBuffer;
    }

    public TypedData slice(int i, int i2) {
        BlockData blockData;
        int i3 = i + i2;
        synchronized (this.mDataBytes) {
            try {
                int iPosition = this.mDataBytes.position();
                int iLimit = this.mDataBytes.limit();
                if (i3 > iLimit) {
                    throw new IllegalArgumentException("startPos(" + i + ") + length(" + i2 + ") > size()");
                }
                this.mDataBytes.position(i);
                this.mDataBytes.limit(i3);
                ByteBuffer byteBufferSlice = this.mDataBytes.slice();
                this.mDataBytes.limit(iLimit);
                this.mDataBytes.position(iPosition);
                blockData = new BlockData(byteBufferSlice);
            } catch (Throwable th) {
                throw th;
            }
        }
        return blockData;
    }

    public byte getByte(int i) {
        return this.mDataBytes.get(i);
    }

    @Override // com.android.storage.block.read.TypedData
    public int getUnsignedByte(int i) {
        return this.mDataBytes.get(i) & TransitionInfo.INIT;
    }

    @Override // com.android.storage.block.read.TypedData
    public int getInt(int i) {
        return this.mDataBytes.getInt(i);
    }

    public long getValueAsLong(int i, int i2, boolean z) {
        if (i < 0 || i > 8) {
            Folme$$ExternalSyntheticBUOutline0.m("valueSizeBytes must be <= 8 bytes");
            return 0L;
        }
        return getValueInternal(i, i2, z);
    }

    @Override // com.android.storage.block.read.TypedData
    public int getSize() {
        return this.mDataBytes.limit();
    }

    private long getValueInternal(int i, int i2, boolean z) {
        if (i2 < 0) {
            S2Support$$ExternalSyntheticBUOutline0.m("byteOffset=", i2, " must not be negative");
            return 0L;
        }
        long j = 0;
        int i3 = 0;
        while (true) {
            int i4 = i3 + 1;
            if (i3 >= i) {
                break;
            }
            j = (j << 8) | ((long) (this.mDataBytes.get(i2) & TransitionInfo.INIT));
            i3 = i4;
            i2++;
        }
        return (i >= 8 || !z) ? j : BitwiseUtils.signExtendToLong(i * 8, j);
    }
}
