package com.android.storage.block.read;

import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class Block {
    private final BlockData mBlockData;
    private final int mId;
    private final int mType;

    public Block(int i, int i2, BlockData blockData) {
        this.mId = i;
        this.mType = i2;
        Objects.requireNonNull(blockData);
        this.mBlockData = blockData;
    }

    public int getType() {
        return this.mType;
    }

    public BlockData getData() {
        return this.mBlockData;
    }
}
