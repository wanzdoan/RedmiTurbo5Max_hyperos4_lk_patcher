package org.tensorflow.lite.support.tensorbuffer;

import java.nio.FloatBuffer;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;

/* JADX INFO: loaded from: classes.dex */
public final class TensorBufferFloat extends TensorBuffer {
    private static final DataType DATA_TYPE = DataType.FLOAT32;

    TensorBufferFloat(int[] shape) {
        super(shape);
    }

    TensorBufferFloat() {
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public DataType getDataType() {
        return DATA_TYPE;
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public float[] getFloatArray() {
        this.buffer.rewind();
        float[] arr = new float[this.flatSize];
        FloatBuffer floatBuffer = this.buffer.asFloatBuffer();
        floatBuffer.get(arr);
        return arr;
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public float getFloatValue(int absIndex) {
        return this.buffer.getFloat(absIndex << 2);
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public int[] getIntArray() {
        this.buffer.rewind();
        float[] floatArr = new float[this.flatSize];
        this.buffer.asFloatBuffer().get(floatArr);
        int[] intArr = new int[this.flatSize];
        for (int i = 0; i < this.flatSize; i++) {
            intArr[i] = (int) floatArr[i];
        }
        return intArr;
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public int getIntValue(int absIndex) {
        return (int) this.buffer.getFloat(absIndex << 2);
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public int getTypeSize() {
        return DATA_TYPE.byteSize();
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public void loadArray(float[] src, int[] shape) {
        SupportPreconditions.checkNotNull(src, "The array to be loaded cannot be null.");
        SupportPreconditions.checkArgument(src.length == computeFlatSize(shape), "The size of the array to be loaded does not match the specified shape.");
        resize(shape);
        this.buffer.rewind();
        FloatBuffer floatBuffer = this.buffer.asFloatBuffer();
        floatBuffer.put(src);
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public void loadArray(int[] src, int[] shape) {
        SupportPreconditions.checkNotNull(src, "The array to be loaded cannot be null.");
        int i = 0;
        SupportPreconditions.checkArgument(src.length == computeFlatSize(shape), "The size of the array to be loaded does not match the specified shape.");
        resize(shape);
        this.buffer.rewind();
        float[] floatArray = new float[src.length];
        int cnt = 0;
        int length = src.length;
        while (i < length) {
            int a = src[i];
            floatArray[cnt] = a;
            i++;
            cnt++;
        }
        this.buffer.asFloatBuffer().put(floatArray);
    }
}
