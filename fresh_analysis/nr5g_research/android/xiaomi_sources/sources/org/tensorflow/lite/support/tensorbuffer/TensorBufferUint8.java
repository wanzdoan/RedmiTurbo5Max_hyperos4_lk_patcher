package org.tensorflow.lite.support.tensorbuffer;

import com.xiaomi.modem.ModemUtils;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;

/* JADX INFO: loaded from: classes.dex */
public final class TensorBufferUint8 extends TensorBuffer {
    private static final DataType DATA_TYPE = DataType.UINT8;

    TensorBufferUint8(int[] shape) {
        super(shape);
    }

    TensorBufferUint8() {
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public DataType getDataType() {
        return DATA_TYPE;
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public float[] getFloatArray() {
        this.buffer.rewind();
        byte[] byteArr = new byte[this.flatSize];
        this.buffer.get(byteArr);
        float[] floatArr = new float[this.flatSize];
        for (int i = 0; i < this.flatSize; i++) {
            floatArr[i] = byteArr[i] & ModemUtils.EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
        }
        return floatArr;
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public float getFloatValue(int index) {
        return this.buffer.get(index) & ModemUtils.EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public int[] getIntArray() {
        this.buffer.rewind();
        byte[] byteArr = new byte[this.flatSize];
        this.buffer.get(byteArr);
        int[] intArr = new int[this.flatSize];
        for (int i = 0; i < this.flatSize; i++) {
            intArr[i] = byteArr[i] & ModemUtils.EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
        }
        return intArr;
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public int getIntValue(int index) {
        return this.buffer.get(index) & ModemUtils.EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH;
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public int getTypeSize() {
        return DATA_TYPE.byteSize();
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public void loadArray(float[] src, int[] shape) {
        SupportPreconditions.checkNotNull(src, "The array to be loaded cannot be null.");
        int i = 0;
        SupportPreconditions.checkArgument(src.length == computeFlatSize(shape), "The size of the array to be loaded does not match the specified shape.");
        resize(shape);
        this.buffer.rewind();
        byte[] byteArr = new byte[src.length];
        int cnt = 0;
        int length = src.length;
        while (i < length) {
            float a = src[i];
            byteArr[cnt] = (byte) Math.max(Math.min(a, 255.0d), 0.0d);
            i++;
            cnt++;
        }
        this.buffer.put(byteArr);
    }

    @Override // org.tensorflow.lite.support.tensorbuffer.TensorBuffer
    public void loadArray(int[] src, int[] shape) {
        SupportPreconditions.checkNotNull(src, "The array to be loaded cannot be null.");
        int i = 0;
        SupportPreconditions.checkArgument(src.length == computeFlatSize(shape), "The size of the array to be loaded does not match the specified shape.");
        resize(shape);
        this.buffer.rewind();
        byte[] byteArr = new byte[src.length];
        int cnt = 0;
        int length = src.length;
        while (i < length) {
            float a = src[i];
            byteArr[cnt] = (byte) Math.max(Math.min(a, 255.0f), 0.0f);
            i++;
            cnt++;
        }
        this.buffer.put(byteArr);
    }
}
