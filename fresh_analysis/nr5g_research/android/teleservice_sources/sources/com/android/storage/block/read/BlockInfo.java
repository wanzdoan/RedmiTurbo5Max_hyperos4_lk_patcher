package com.android.storage.block.read;

import java.util.Arrays;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class BlockInfo {
    private final long mBlockSizeBytes;
    private final long mBlockStartByteOffset;
    private final byte[] mExtraBytes;
    private final int mId;
    private final int mType;

    public BlockInfo(int i, int i2, long j, long j2, byte[] bArr) {
        this.mId = i;
        this.mType = i2;
        this.mBlockStartByteOffset = j;
        this.mBlockSizeBytes = j2;
        this.mExtraBytes = bArr;
    }

    public int getId() {
        return this.mId;
    }

    public int getType() {
        return this.mType;
    }

    public byte[] getExtraBytes() {
        return this.mExtraBytes;
    }

    public long getBlockStartByteOffset() {
        return this.mBlockStartByteOffset;
    }

    public long getBlockSizeBytes() {
        return this.mBlockSizeBytes;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && BlockInfo.class == obj.getClass()) {
            BlockInfo blockInfo = (BlockInfo) obj;
            if (this.mId == blockInfo.mId && this.mType == blockInfo.mType && this.mBlockStartByteOffset == blockInfo.mBlockStartByteOffset && this.mBlockSizeBytes == blockInfo.mBlockSizeBytes && Arrays.equals(this.mExtraBytes, blockInfo.mExtraBytes)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (Objects.hash(Integer.valueOf(this.mId), Integer.valueOf(this.mType), Long.valueOf(this.mBlockStartByteOffset), Long.valueOf(this.mBlockSizeBytes)) * 31) + Arrays.hashCode(this.mExtraBytes);
    }

    public String toString() {
        return "BlockInfo{mId=" + this.mId + "(" + Integer.toHexString(this.mId) + "), mType=" + this.mType + ", mBlockStartByteOffset=" + this.mBlockStartByteOffset + ", mBlockSizeBytes=" + this.mBlockSizeBytes + ", mExtraBytes=" + Arrays.toString(this.mExtraBytes) + '}';
    }
}
