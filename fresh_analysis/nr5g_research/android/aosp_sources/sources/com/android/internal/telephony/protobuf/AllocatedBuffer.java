package com.android.internal.telephony.protobuf;

import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline0;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
abstract class AllocatedBuffer {
    public abstract byte[] array();

    public abstract int arrayOffset();

    public abstract boolean hasArray();

    public abstract boolean hasNioBuffer();

    public abstract int limit();

    public abstract ByteBuffer nioBuffer();

    public abstract int position();

    public abstract AllocatedBuffer position(int i);

    public abstract int remaining();

    AllocatedBuffer() {
    }

    public static AllocatedBuffer wrap(byte[] bArr) {
        return wrapNoCheck(bArr, 0, bArr.length);
    }

    public static AllocatedBuffer wrap(byte[] bArr, int i, int i2) {
        if (i < 0 || i2 < 0 || i + i2 > bArr.length) {
            throw new IndexOutOfBoundsException(String.format("bytes.length=%d, offset=%d, length=%d", Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)));
        }
        return wrapNoCheck(bArr, i, i2);
    }

    public static AllocatedBuffer wrap(final ByteBuffer byteBuffer) {
        Internal.checkNotNull(byteBuffer, "buffer");
        return new AllocatedBuffer() { // from class: com.android.internal.telephony.protobuf.AllocatedBuffer.1
            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public boolean hasNioBuffer() {
                return true;
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public ByteBuffer nioBuffer() {
                return byteBuffer;
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public boolean hasArray() {
                return byteBuffer.hasArray();
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public byte[] array() {
                return byteBuffer.array();
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public int arrayOffset() {
                return byteBuffer.arrayOffset();
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public int position() {
                return byteBuffer.position();
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public AllocatedBuffer position(int i) {
                Java8Compatibility.position(byteBuffer, i);
                return this;
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public int limit() {
                return byteBuffer.limit();
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public int remaining() {
                return byteBuffer.remaining();
            }
        };
    }

    private static AllocatedBuffer wrapNoCheck(final byte[] bArr, final int i, final int i2) {
        return new AllocatedBuffer() { // from class: com.android.internal.telephony.protobuf.AllocatedBuffer.2
            private int position;

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public boolean hasArray() {
                return true;
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public boolean hasNioBuffer() {
                return false;
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public ByteBuffer nioBuffer() {
                throw new UnsupportedOperationException();
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public byte[] array() {
                return bArr;
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public int arrayOffset() {
                return i;
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public int position() {
                return this.position;
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public AllocatedBuffer position(int i3) {
                if (i3 < 0 || i3 > i2) {
                    RILUtils$$ExternalSyntheticBUOutline0.m("Invalid position: ", i3);
                    return null;
                }
                this.position = i3;
                return this;
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public int limit() {
                return i2;
            }

            @Override // com.android.internal.telephony.protobuf.AllocatedBuffer
            public int remaining() {
                return i2 - this.position;
            }
        };
    }
}
