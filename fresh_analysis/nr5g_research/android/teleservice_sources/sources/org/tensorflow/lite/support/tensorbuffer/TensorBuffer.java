package org.tensorflow.lite.support.tensorbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.SupportPreconditions;

/* JADX INFO: loaded from: classes2.dex */
public abstract class TensorBuffer {
    protected ByteBuffer buffer;
    protected int flatSize = -1;
    protected final boolean isDynamic = false;
    protected int[] shape;

    public abstract float[] getFloatArray();

    public abstract int getTypeSize();

    public abstract void loadArray(float[] fArr, int[] iArr);

    /* JADX INFO: renamed from: org.tensorflow.lite.support.tensorbuffer.TensorBuffer$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$tensorflow$lite$DataType;

        static {
            int[] iArr = new int[DataType.values().length];
            $SwitchMap$org$tensorflow$lite$DataType = iArr;
            try {
                iArr[DataType.FLOAT32.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.UINT8.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static TensorBuffer createFixedSize(int[] iArr, DataType dataType) {
        int i = AnonymousClass1.$SwitchMap$org$tensorflow$lite$DataType[dataType.ordinal()];
        if (i == 1) {
            return new TensorBufferFloat(iArr);
        }
        if (i == 2) {
            return new TensorBufferUint8(iArr);
        }
        throw new AssertionError("TensorBuffer does not support data type: " + dataType);
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    public void loadArray(float[] fArr) {
        loadArray(fArr, this.shape);
    }

    protected TensorBuffer(int[] iArr) {
        allocateMemory(iArr);
    }

    protected static int computeFlatSize(int[] iArr) {
        SupportPreconditions.checkNotNull(iArr, "Shape cannot be null.");
        int i = 1;
        for (int i2 : iArr) {
            i *= i2;
        }
        return i;
    }

    protected void resize(int[] iArr) {
        if (this.isDynamic) {
            allocateMemory(iArr);
        } else {
            SupportPreconditions.checkArgument(Arrays.equals(iArr, this.shape));
            this.shape = (int[]) iArr.clone();
        }
    }

    private void allocateMemory(int[] iArr) {
        SupportPreconditions.checkNotNull(iArr, "TensorBuffer shape cannot be null.");
        SupportPreconditions.checkArgument(isShapeValid(iArr), "Values in TensorBuffer shape should be non-negative.");
        int iComputeFlatSize = computeFlatSize(iArr);
        this.shape = (int[]) iArr.clone();
        if (this.flatSize == iComputeFlatSize) {
            return;
        }
        this.flatSize = iComputeFlatSize;
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(iComputeFlatSize * getTypeSize());
        this.buffer = byteBufferAllocateDirect;
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
    }

    private static boolean isShapeValid(int[] iArr) {
        if (iArr.length == 0) {
            return true;
        }
        for (int i : iArr) {
            if (i < 0) {
                return false;
            }
        }
        return true;
    }
}
