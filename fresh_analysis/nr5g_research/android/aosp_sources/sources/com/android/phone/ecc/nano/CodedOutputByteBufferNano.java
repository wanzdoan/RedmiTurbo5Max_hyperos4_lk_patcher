package com.android.phone.ecc.nano;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
public final class CodedOutputByteBufferNano {
    private final ByteBuffer buffer;

    public static int computeRawVarint32Size(int i) {
        if ((i & (-128)) == 0) {
            return 1;
        }
        if ((i & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & i) == 0) {
            return 3;
        }
        return (i & (-268435456)) == 0 ? 4 : 5;
    }

    private CodedOutputByteBufferNano(byte[] bArr, int i, int i2) {
        this(ByteBuffer.wrap(bArr, i, i2));
    }

    private CodedOutputByteBufferNano(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public static CodedOutputByteBufferNano newInstance(byte[] bArr) {
        return newInstance(bArr, 0, bArr.length);
    }

    public static CodedOutputByteBufferNano newInstance(byte[] bArr, int i, int i2) {
        return new CodedOutputByteBufferNano(bArr, i, i2);
    }

    public static class OutOfSpaceException extends IOException {
        private static final long serialVersionUID = -6947486886997889499L;

        OutOfSpaceException(int i, int i2) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + i + " limit " + i2 + ").");
        }
    }

    public void writeRawByte(byte b) throws OutOfSpaceException {
        boolean zHasRemaining = this.buffer.hasRemaining();
        ByteBuffer byteBuffer = this.buffer;
        if (!zHasRemaining) {
            throw new OutOfSpaceException(byteBuffer.position(), this.buffer.limit());
        }
        byteBuffer.put(b);
    }

    public void writeRawByte(int i) throws OutOfSpaceException {
        writeRawByte((byte) i);
    }

    public void writeRawBytes(byte[] bArr) throws OutOfSpaceException {
        writeRawBytes(bArr, 0, bArr.length);
    }

    public void writeRawBytes(byte[] bArr, int i, int i2) throws OutOfSpaceException {
        int iRemaining = this.buffer.remaining();
        ByteBuffer byteBuffer = this.buffer;
        if (iRemaining >= i2) {
            byteBuffer.put(bArr, i, i2);
            return;
        }
        throw new OutOfSpaceException(byteBuffer.position(), this.buffer.limit());
    }

    public void writeRawVarint32(int i) throws OutOfSpaceException {
        while ((i & (-128)) != 0) {
            writeRawByte((i & 127) | 128);
            i >>>= 7;
        }
        writeRawByte(i);
    }
}
