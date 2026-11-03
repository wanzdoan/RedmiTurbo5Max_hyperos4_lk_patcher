package org.tensorflow.lite.support.tensorbuffer;

import miuix.animation.internal.TransitionInfo;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;

/* JADX INFO: loaded from: classes2.dex */
public final class TensorBufferUint8 extends TensorBuffer {
    private static final DataType DATA_TYPE = DataType.UINT8;

    TensorBufferUint8(int[] iArr) {
        super(iArr);
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public float[] getFloatArray() {
        this.buffer.rewind();
        byte[] bArr = new byte[this.flatSize];
        this.buffer.get(bArr);
        float[] fArr = new float[this.flatSize];
        for (int i = 0; i < this.flatSize; i++) {
            fArr[i] = bArr[i] & TransitionInfo.INIT;
        }
        return fArr;
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public int getTypeSize() {
        return DATA_TYPE.byteSize();
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public void loadArray(float[] fArr, int[] iArr) {
        SupportPreconditions.checkNotNull(fArr, "The array to be loaded cannot be null.");
        int i = 0;
        SupportPreconditions.checkArgument(fArr.length == TensorBuffer.computeFlatSize(iArr), "The size of the array to be loaded does not match the specified shape.");
        resize(iArr);
        this.buffer.rewind();
        byte[] bArr = new byte[fArr.length];
        int length = fArr.length;
        int i2 = 0;
        while (i < length) {
            bArr[i2] = (byte) Math.max(Math.min(fArr[i], 255.0d), 0.0d);
            i++;
            i2++;
        }
        this.buffer.put(bArr);
    }
}
