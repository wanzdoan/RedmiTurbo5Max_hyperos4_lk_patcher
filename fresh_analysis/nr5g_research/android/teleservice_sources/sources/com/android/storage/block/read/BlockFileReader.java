package com.android.storage.block.read;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import com.android.storage.io.read.TypedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;

/* JADX INFO: loaded from: classes.dex */
public final class BlockFileReader implements AutoCloseable {
    private static final BlockData EMPTY_BLOCK_DATA;
    private static final ByteBuffer EMPTY_BYTE_BUFFER;
    private BlockInfo[] mBlockInfos;
    private FileChannel mFileChannel;
    private char mMagic;
    private final boolean mMemoryMapBlocks;
    private Character mRequiredMagic;
    private Integer mRequiredMinVersion;
    private int mVersion;

    static {
        ByteBuffer byteBufferAsReadOnlyBuffer = ByteBuffer.allocate(0).asReadOnlyBuffer();
        EMPTY_BYTE_BUFFER = byteBufferAsReadOnlyBuffer;
        EMPTY_BLOCK_DATA = new BlockData(byteBufferAsReadOnlyBuffer);
    }

    private BlockFileReader(boolean z) {
        this.mMemoryMapBlocks = z;
    }

    private void setRequiredMagic(char c) {
        this.mRequiredMagic = Character.valueOf(c);
    }

    private void setRequiredMinVersion(int i) {
        this.mRequiredMinVersion = Integer.valueOf(i);
    }

    public static BlockFileReader open(boolean z, File file, char c, int i) {
        BlockFileReader blockFileReader = new BlockFileReader(z);
        blockFileReader.setRequiredMagic(c);
        blockFileReader.setRequiredMinVersion(i);
        blockFileReader.open(file);
        return blockFileReader;
    }

    private void open(File file) {
        TypedInputStream typedInputStream = new TypedInputStream(new FileInputStream(file));
        try {
            char c = typedInputStream.readChar();
            this.mMagic = c;
            Character ch = this.mRequiredMagic;
            if (ch != null && c != ch.charValue()) {
                throw new IOException("Bad magic: expected " + Integer.toHexString(this.mMagic) + " but was " + Integer.toHexString(this.mMagic));
            }
            int i = typedInputStream.readInt();
            this.mVersion = i;
            Integer num = this.mRequiredMinVersion;
            if (num != null && i < num.intValue()) {
                throw new IOException("Bad version: " + this.mVersion + ", reader requires at least " + this.mRequiredMinVersion);
            }
            typedInputStream.skipBytes(10);
            int i2 = typedInputStream.readInt();
            this.mBlockInfos = new BlockInfo[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                if (typedInputStream.readInt() < 25) {
                    throw new IOException("Minimum block info size:25");
                }
                this.mBlockInfos[i3] = new BlockInfo(i3, typedInputStream.readInt(), typedInputStream.readLong(), typedInputStream.readLong(), typedInputStream.readTinyVarByteArray());
            }
            typedInputStream.close();
            this.mFileChannel = FileChannel.open(file.toPath(), new OpenOption[0]);
        } catch (Throwable th) {
            try {
                typedInputStream.close();
                throw th;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                throw th;
            }
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mFileChannel.close();
    }

    public BlockInfo getBlockInfo(int i) {
        checkFileOpen();
        return this.mBlockInfos[i];
    }

    public Block getBlock(int i) {
        ByteBuffer byteBufferAsReadOnlyBuffer;
        checkFileOpen();
        BlockInfo blockInfo = this.mBlockInfos[i];
        if (blockInfo.getBlockSizeBytes() == 0) {
            return new Block(i, blockInfo.getType(), EMPTY_BLOCK_DATA);
        }
        if (this.mMemoryMapBlocks) {
            byteBufferAsReadOnlyBuffer = this.mFileChannel.map(FileChannel.MapMode.READ_ONLY, blockInfo.getBlockStartByteOffset(), blockInfo.getBlockSizeBytes());
            if (byteBufferAsReadOnlyBuffer.order() != ByteOrder.BIG_ENDIAN) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Byte order must be BIG_ENDIAN");
                return null;
            }
        } else {
            long blockSizeBytes = blockInfo.getBlockSizeBytes();
            if (blockSizeBytes > 2147483647L) {
                throw new IOException("Block too large to read into memory. Try mapping instead.");
            }
            int i2 = (int) blockSizeBytes;
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(i2);
            this.mFileChannel.position(blockInfo.getBlockStartByteOffset());
            int i3 = this.mFileChannel.read(byteBufferAllocate);
            if (i3 != i2) {
                throw new IllegalStateException("Unable to read " + i2 + ", only read" + i3);
            }
            byteBufferAllocate.rewind();
            byteBufferAsReadOnlyBuffer = byteBufferAllocate.asReadOnlyBuffer();
        }
        int i4 = byteBufferAsReadOnlyBuffer.getInt();
        if (i4 != i) {
            throw new IllegalStateException("Expected id=" + i + ", but was " + i4);
        }
        int i5 = byteBufferAsReadOnlyBuffer.getInt();
        if (i5 != blockInfo.getType()) {
            throw new IllegalStateException("Expected type=" + blockInfo.getType() + ", but was " + i5);
        }
        return new Block(i4, i5, new BlockData(byteBufferAsReadOnlyBuffer.slice()));
    }

    private void checkFileOpen() {
        if (this.mFileChannel.isOpen()) {
            return;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("BlockFile is closed.");
    }
}
