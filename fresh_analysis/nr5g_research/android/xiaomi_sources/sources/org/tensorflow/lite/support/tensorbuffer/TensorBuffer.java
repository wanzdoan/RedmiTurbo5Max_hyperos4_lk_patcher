package org.tensorflow.lite.support.tensorbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;

/* JADX INFO: loaded from: classes.dex */
public abstract class TensorBuffer {
    protected ByteBuffer buffer;
    protected int flatSize = -1;
    protected final boolean isDynamic = true;
    protected int[] shape;

    public abstract DataType getDataType();

    public abstract float[] getFloatArray();

    public abstract float getFloatValue(int i);

    public abstract int[] getIntArray();

    public abstract int getIntValue(int i);

    public abstract int getTypeSize();

    public abstract void loadArray(float[] fArr, int[] iArr);

    public abstract void loadArray(int[] iArr, int[] iArr2);

    public static TensorBuffer createFixedSize(int[] shape, DataType dataType) {
        switch (dataType) {
            case FLOAT32:
                return new TensorBufferFloat(shape);
            case UINT8:
                return new TensorBufferUint8(shape);
            default:
                throw new AssertionError("TensorBuffer does not support data type: " + dataType);
        }
    }

    public static TensorBuffer createDynamic(DataType dataType) {
        switch (dataType) {
            case FLOAT32:
                return new TensorBufferFloat();
            case UINT8:
                return new TensorBufferUint8();
            default:
                throw new AssertionError("TensorBuffer does not support data type: " + dataType);
        }
    }

    public static TensorBuffer createFrom(TensorBuffer buffer, DataType dataType) {
        TensorBuffer result;
        SupportPreconditions.checkNotNull(buffer, "Cannot create a buffer from null");
        if (buffer.isDynamic()) {
            result = createDynamic(dataType);
        } else {
            result = createFixedSize(buffer.shape, dataType);
        }
        if (buffer.getDataType() == DataType.FLOAT32 && dataType == DataType.FLOAT32) {
            float[] data = buffer.getFloatArray();
            result.loadArray(data, buffer.shape);
        } else {
            int[] data2 = buffer.getIntArray();
            result.loadArray(data2, buffer.shape);
        }
        return result;
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    public int getFlatSize() {
        assertShapeIsCorect();
        return this.flatSize;
    }

    public int[] getShape() {
        assertShapeIsCorect();
        return Arrays.copyOf(this.shape, this.shape.length);
    }

    public boolean isDynamic() {
        return this.isDynamic;
    }

    public void loadArray(int[] src) {
        loadArray(src, this.shape);
    }

    public void loadArray(float[] src) {
        loadArray(src, this.shape);
    }

    public void loadBuffer(ByteBuffer buffer, int[] shape) {
        SupportPreconditions.checkNotNull(buffer, "Byte buffer cannot be null.");
        int flatSize = computeFlatSize(shape);
        SupportPreconditions.checkArgument(buffer.limit() == getTypeSize() * flatSize, "The size of byte buffer and the shape do not match.");
        resize(shape);
        buffer.rewind();
        this.buffer = buffer;
    }

    public void loadBuffer(ByteBuffer buffer) {
        loadBuffer(buffer, this.shape);
    }

    protected TensorBuffer(int[] shape) {
        allocateMemory(shape);
    }

    protected TensorBuffer() {
        allocateMemory(new int[]{0});
    }

    protected static int computeFlatSize(int[] shape) {
        SupportPreconditions.checkNotNull(shape, "Shape cannot be null.");
        int prod = 1;
        for (int s : shape) {
            prod *= s;
        }
        return prod;
    }

    protected void resize(int[] shape) {
        if (this.isDynamic) {
            allocateMemory(shape);
        } else {
            SupportPreconditions.checkArgument(Arrays.equals(shape, this.shape));
            this.shape = (int[]) shape.clone();
        }
    }

    private void allocateMemory(int[] shape) {
        SupportPreconditions.checkNotNull(shape, "TensorBuffer shape cannot be null.");
        SupportPreconditions.checkArgument(isShapeValid(shape), "Values in TensorBuffer shape should be non-negative.");
        int newFlatSize = computeFlatSize(shape);
        this.shape = (int[]) shape.clone();
        if (this.flatSize == newFlatSize) {
            return;
        }
        this.flatSize = newFlatSize;
        this.buffer = ByteBuffer.allocateDirect(this.flatSize * getTypeSize());
        this.buffer.order(ByteOrder.nativeOrder());
    }

    private void assertShapeIsCorect() {
        int flatSize = computeFlatSize(this.shape);
        SupportPreconditions.checkState(this.buffer.limit() == getTypeSize() * flatSize, String.format("The size of underlying ByteBuffer (%d) and the shape (%s) do not match. The ByteBuffer may have been changed.", Integer.valueOf(this.buffer.limit()), Arrays.toString(this.shape)));
    }

    private static boolean isShapeValid(int[] shape) {
        if (shape.length == 0) {
            return true;
        }
        for (int s : shape) {
            if (s < 0) {
                return false;
            }
        }
        return true;
    }
}
