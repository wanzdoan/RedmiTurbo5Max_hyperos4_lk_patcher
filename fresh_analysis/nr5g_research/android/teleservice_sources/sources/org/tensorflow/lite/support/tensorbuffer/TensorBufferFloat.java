package org.tensorflow.lite.support.tensorbuffer;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;

/* JADX INFO: loaded from: classes2.dex */
public final class TensorBufferFloat extends TensorBuffer {
    private static final DataType DATA_TYPE = DataType.FLOAT32;

    TensorBufferFloat(int[] iArr) {
        super(iArr);
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public float[] getFloatArray() {
        this.buffer.rewind();
        float[] fArr = new float[this.flatSize];
        this.buffer.asFloatBuffer().get(fArr);
        return fArr;
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public int getTypeSize() {
        return DATA_TYPE.byteSize();
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public void loadArray(float[] fArr, int[] iArr) {
        SupportPreconditions.checkNotNull(fArr, "The array to be loaded cannot be null.");
        SupportPreconditions.checkArgument(fArr.length == TensorBuffer.computeFlatSize(iArr), "The size of the array to be loaded does not match the specified shape.");
        resize(iArr);
        this.buffer.rewind();
        this.buffer.asFloatBuffer().put(fArr);
    }
}
