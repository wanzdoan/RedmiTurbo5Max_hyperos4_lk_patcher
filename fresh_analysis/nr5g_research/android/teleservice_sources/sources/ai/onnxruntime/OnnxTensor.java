package ai.onnxruntime;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/* JADX INFO: loaded from: classes.dex */
public class OnnxTensor extends OnnxTensorLike {
    private final Buffer buffer;

    private native void close(long j, long j2);

    private static native long createString(long j, long j2, String str) throws OrtException;

    private static native long createStringTensor(long j, long j2, Object[] objArr, long[] jArr) throws OrtException;

    private static native long createTensor(long j, long j2, Object obj, long[] jArr, int i) throws OrtException;

    private static native long createTensorFromBuffer(long j, long j2, Buffer buffer, int i, long j3, long[] jArr, int i2) throws OrtException;

    private native void getArray(long j, long j2, Object obj) throws OrtException;

    private native boolean getBool(long j, long j2) throws OrtException;

    private native ByteBuffer getBuffer(long j, long j2);

    private native byte getByte(long j, long j2, int i) throws OrtException;

    private native double getDouble(long j, long j2) throws OrtException;

    private native float getFloat(long j, long j2, int i) throws OrtException;

    private native int getInt(long j, long j2, int i) throws OrtException;

    private native long getLong(long j, long j2, int i) throws OrtException;

    private native short getShort(long j, long j2, int i) throws OrtException;

    private native String getString(long j, long j2) throws OrtException;

    OnnxTensor(long j, long j2, TensorInfo tensorInfo) {
        this(j, j2, tensorInfo, null);
    }

    OnnxTensor(long j, long j2, TensorInfo tensorInfo, Buffer buffer) {
        super(j, j2, tensorInfo);
        this.buffer = buffer;
    }

    @Override // ai.onnxruntime.OnnxValue
    public OnnxValue.OnnxValueType getType() {
        return OnnxValue.OnnxValueType.ONNX_TYPE_TENSOR;
    }

    /* JADX INFO: renamed from: ai.onnxruntime.OnnxTensor$1, reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ai$onnxruntime$OnnxJavaType;

        static {
            int[] iArr = new int[OnnxJavaType.values().length];
            $SwitchMap$ai$onnxruntime$OnnxJavaType = iArr;
            try {
                iArr[OnnxJavaType.FLOAT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.DOUBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.UINT8.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.INT8.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.INT16.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.INT32.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.INT64.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.BOOL.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.STRING.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.UNKNOWN.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    @Override // ai.onnxruntime.OnnxValue
    public Object getValue() throws OrtException {
        if (this.info.isScalar()) {
            switch (AnonymousClass1.$SwitchMap$ai$onnxruntime$OnnxJavaType[this.info.type.ordinal()]) {
                case 1:
                    return Float.valueOf(getFloat(OnnxRuntime.ortApiHandle, this.nativeHandle, this.info.onnxType.value));
                case 2:
                    return Double.valueOf(getDouble(OnnxRuntime.ortApiHandle, this.nativeHandle));
                case 3:
                case 4:
                    return Byte.valueOf(getByte(OnnxRuntime.ortApiHandle, this.nativeHandle, this.info.onnxType.value));
                case 5:
                    return Short.valueOf(getShort(OnnxRuntime.ortApiHandle, this.nativeHandle, this.info.onnxType.value));
                case 6:
                    return Integer.valueOf(getInt(OnnxRuntime.ortApiHandle, this.nativeHandle, this.info.onnxType.value));
                case 7:
                    return Long.valueOf(getLong(OnnxRuntime.ortApiHandle, this.nativeHandle, this.info.onnxType.value));
                case 8:
                    return Boolean.valueOf(getBool(OnnxRuntime.ortApiHandle, this.nativeHandle));
                case 9:
                    return getString(OnnxRuntime.ortApiHandle, this.nativeHandle);
                default:
                    throw new OrtException("Extracting the value of an invalid Tensor.");
            }
        }
        Object objMakeCarrier = this.info.makeCarrier();
        if (this.info.getNumElements() > 0) {
            getArray(OnnxRuntime.ortApiHandle, this.nativeHandle, objMakeCarrier);
        }
        TensorInfo tensorInfo = this.info;
        if (tensorInfo.type == OnnxJavaType.STRING) {
            long[] jArr = tensorInfo.shape;
            if (jArr.length != 1) {
                return OrtUtil.reshape((String[]) objMakeCarrier, jArr);
            }
        }
        return objMakeCarrier;
    }

    public String toString() {
        return "OnnxTensor(info=" + this.info.toString() + ")";
    }

    @Override // ai.onnxruntime.OnnxValue, java.lang.AutoCloseable
    public void close() {
        close(OnnxRuntime.ortApiHandle, this.nativeHandle);
    }

    public ByteBuffer getByteBuffer() {
        if (this.info.type == OnnxJavaType.STRING) {
            return null;
        }
        ByteBuffer buffer = getBuffer(OnnxRuntime.ortApiHandle, this.nativeHandle);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(buffer.capacity());
        byteBufferAllocate.put(buffer);
        byteBufferAllocate.rewind();
        return byteBufferAllocate;
    }

    public FloatBuffer getFloatBuffer() {
        TensorInfo tensorInfo = this.info;
        if (tensorInfo.type != OnnxJavaType.FLOAT) {
            return null;
        }
        if (tensorInfo.onnxType == TensorInfo.OnnxTensorType.ONNX_TENSOR_ELEMENT_DATA_TYPE_FLOAT16) {
            ShortBuffer shortBufferAsShortBuffer = getBuffer().asShortBuffer();
            int iCapacity = shortBufferAsShortBuffer.capacity();
            FloatBuffer floatBufferAllocate = FloatBuffer.allocate(iCapacity);
            for (int i = 0; i < iCapacity; i++) {
                floatBufferAllocate.put(fp16ToFloat(shortBufferAsShortBuffer.get(i)));
            }
            floatBufferAllocate.rewind();
            return floatBufferAllocate;
        }
        FloatBuffer floatBufferAsFloatBuffer = getBuffer().asFloatBuffer();
        FloatBuffer floatBufferAllocate2 = FloatBuffer.allocate(floatBufferAsFloatBuffer.capacity());
        floatBufferAllocate2.put(floatBufferAsFloatBuffer);
        floatBufferAllocate2.rewind();
        return floatBufferAllocate2;
    }

    public DoubleBuffer getDoubleBuffer() {
        if (this.info.type != OnnxJavaType.DOUBLE) {
            return null;
        }
        DoubleBuffer doubleBufferAsDoubleBuffer = getBuffer().asDoubleBuffer();
        DoubleBuffer doubleBufferAllocate = DoubleBuffer.allocate(doubleBufferAsDoubleBuffer.capacity());
        doubleBufferAllocate.put(doubleBufferAsDoubleBuffer);
        doubleBufferAllocate.rewind();
        return doubleBufferAllocate;
    }

    public ShortBuffer getShortBuffer() {
        if (this.info.type != OnnxJavaType.INT16) {
            return null;
        }
        ShortBuffer shortBufferAsShortBuffer = getBuffer().asShortBuffer();
        ShortBuffer shortBufferAllocate = ShortBuffer.allocate(shortBufferAsShortBuffer.capacity());
        shortBufferAllocate.put(shortBufferAsShortBuffer);
        shortBufferAllocate.rewind();
        return shortBufferAllocate;
    }

    public IntBuffer getIntBuffer() {
        if (this.info.type != OnnxJavaType.INT32) {
            return null;
        }
        IntBuffer intBufferAsIntBuffer = getBuffer().asIntBuffer();
        IntBuffer intBufferAllocate = IntBuffer.allocate(intBufferAsIntBuffer.capacity());
        intBufferAllocate.put(intBufferAsIntBuffer);
        intBufferAllocate.rewind();
        return intBufferAllocate;
    }

    public LongBuffer getLongBuffer() {
        if (this.info.type != OnnxJavaType.INT64) {
            return null;
        }
        LongBuffer longBufferAsLongBuffer = getBuffer().asLongBuffer();
        LongBuffer longBufferAllocate = LongBuffer.allocate(longBufferAsLongBuffer.capacity());
        longBufferAllocate.put(longBufferAsLongBuffer);
        longBufferAllocate.rewind();
        return longBufferAllocate;
    }

    private ByteBuffer getBuffer() {
        return getBuffer(OnnxRuntime.ortApiHandle, this.nativeHandle).order(ByteOrder.nativeOrder());
    }

    static float fp16ToFloat(short s) {
        return Float.intBitsToFloat(((s & 1023) << 13) | ((Short.MIN_VALUE & s) << 16) | (((s & 31744) + 114688) << 13));
    }

    public static OnnxTensor createTensor(OrtEnvironment ortEnvironment, Object obj) throws OrtException {
        return createTensor(ortEnvironment, ortEnvironment.defaultAllocator, obj);
    }

    static OnnxTensor createTensor(OrtEnvironment ortEnvironment, OrtAllocator ortAllocator, Object obj) throws OrtException {
        if (!ortAllocator.isClosed()) {
            TensorInfo tensorInfoConstructFromJavaArray = TensorInfo.constructFromJavaArray(obj);
            OnnxJavaType onnxJavaType = tensorInfoConstructFromJavaArray.type;
            OnnxJavaType onnxJavaType2 = OnnxJavaType.STRING;
            long[] jArr = tensorInfoConstructFromJavaArray.shape;
            if (onnxJavaType == onnxJavaType2) {
                if (jArr.length == 0) {
                    return new OnnxTensor(createString(OnnxRuntime.ortApiHandle, ortAllocator.handle, (String) obj), ortAllocator.handle, tensorInfoConstructFromJavaArray);
                }
                return new OnnxTensor(createStringTensor(OnnxRuntime.ortApiHandle, ortAllocator.handle, OrtUtil.flattenString(obj), tensorInfoConstructFromJavaArray.shape), ortAllocator.handle, tensorInfoConstructFromJavaArray);
            }
            if (jArr.length == 0 && (obj = OrtUtil.convertBoxedPrimitiveToArray(onnxJavaType, obj)) == null) {
                throw new OrtException("Failed to convert a boxed primitive to an array, this is an error with the ORT Java API, please report this message & stack trace. JavaType = " + tensorInfoConstructFromJavaArray.type + ", object = " + obj);
            }
            return new OnnxTensor(createTensor(OnnxRuntime.ortApiHandle, ortAllocator.handle, obj, tensorInfoConstructFromJavaArray.shape, tensorInfoConstructFromJavaArray.onnxType.value), ortAllocator.handle, tensorInfoConstructFromJavaArray);
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to create an OnnxTensor with a closed OrtAllocator.");
        return null;
    }

    public static OnnxTensor createTensor(OrtEnvironment ortEnvironment, String[] strArr, long[] jArr) throws OrtException {
        return createTensor(ortEnvironment, ortEnvironment.defaultAllocator, strArr, jArr);
    }

    static OnnxTensor createTensor(OrtEnvironment ortEnvironment, OrtAllocator ortAllocator, String[] strArr, long[] jArr) throws OrtException {
        if (!ortAllocator.isClosed()) {
            return new OnnxTensor(createStringTensor(OnnxRuntime.ortApiHandle, ortAllocator.handle, strArr, jArr), ortAllocator.handle, new TensorInfo(jArr, OnnxJavaType.STRING, TensorInfo.OnnxTensorType.ONNX_TENSOR_ELEMENT_DATA_TYPE_STRING));
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to create an OnnxTensor on a closed OrtAllocator.");
        return null;
    }

    public static OnnxTensor createTensor(OrtEnvironment ortEnvironment, FloatBuffer floatBuffer, long[] jArr) throws OrtException {
        return createTensor(ortEnvironment, ortEnvironment.defaultAllocator, floatBuffer, jArr);
    }

    static OnnxTensor createTensor(OrtEnvironment ortEnvironment, OrtAllocator ortAllocator, FloatBuffer floatBuffer, long[] jArr) throws OrtException {
        if (!ortAllocator.isClosed()) {
            return createTensor(OnnxJavaType.FLOAT, ortAllocator, floatBuffer, jArr);
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to create an OnnxTensor on a closed OrtAllocator.");
        return null;
    }

    public static OnnxTensor createTensor(OrtEnvironment ortEnvironment, DoubleBuffer doubleBuffer, long[] jArr) throws OrtException {
        return createTensor(ortEnvironment, ortEnvironment.defaultAllocator, doubleBuffer, jArr);
    }

    static OnnxTensor createTensor(OrtEnvironment ortEnvironment, OrtAllocator ortAllocator, DoubleBuffer doubleBuffer, long[] jArr) throws OrtException {
        if (!ortAllocator.isClosed()) {
            return createTensor(OnnxJavaType.DOUBLE, ortAllocator, doubleBuffer, jArr);
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to create an OnnxTensor on a closed OrtAllocator.");
        return null;
    }

    public static OnnxTensor createTensor(OrtEnvironment ortEnvironment, ByteBuffer byteBuffer, long[] jArr) throws OrtException {
        return createTensor(ortEnvironment, ortEnvironment.defaultAllocator, byteBuffer, jArr);
    }

    static OnnxTensor createTensor(OrtEnvironment ortEnvironment, OrtAllocator ortAllocator, ByteBuffer byteBuffer, long[] jArr) throws OrtException {
        return createTensor(ortEnvironment, ortAllocator, byteBuffer, jArr, OnnxJavaType.INT8);
    }

    public static OnnxTensor createTensor(OrtEnvironment ortEnvironment, ByteBuffer byteBuffer, long[] jArr, OnnxJavaType onnxJavaType) throws OrtException {
        return createTensor(ortEnvironment, ortEnvironment.defaultAllocator, byteBuffer, jArr, onnxJavaType);
    }

    static OnnxTensor createTensor(OrtEnvironment ortEnvironment, OrtAllocator ortAllocator, ByteBuffer byteBuffer, long[] jArr, OnnxJavaType onnxJavaType) throws OrtException {
        if (!ortAllocator.isClosed()) {
            return createTensor(onnxJavaType, ortAllocator, byteBuffer, jArr);
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to create an OnnxTensor on a closed OrtAllocator.");
        return null;
    }

    public static OnnxTensor createTensor(OrtEnvironment ortEnvironment, ShortBuffer shortBuffer, long[] jArr) throws OrtException {
        return createTensor(ortEnvironment, ortEnvironment.defaultAllocator, shortBuffer, jArr);
    }

    static OnnxTensor createTensor(OrtEnvironment ortEnvironment, OrtAllocator ortAllocator, ShortBuffer shortBuffer, long[] jArr) throws OrtException {
        if (!ortAllocator.isClosed()) {
            return createTensor(OnnxJavaType.INT16, ortAllocator, shortBuffer, jArr);
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to create an OnnxTensor on a closed OrtAllocator.");
        return null;
    }

    public static OnnxTensor createTensor(OrtEnvironment ortEnvironment, IntBuffer intBuffer, long[] jArr) throws OrtException {
        return createTensor(ortEnvironment, ortEnvironment.defaultAllocator, intBuffer, jArr);
    }

    static OnnxTensor createTensor(OrtEnvironment ortEnvironment, OrtAllocator ortAllocator, IntBuffer intBuffer, long[] jArr) throws OrtException {
        if (!ortAllocator.isClosed()) {
            return createTensor(OnnxJavaType.INT32, ortAllocator, intBuffer, jArr);
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to create an OnnxTensor on a closed OrtAllocator.");
        return null;
    }

    public static OnnxTensor createTensor(OrtEnvironment ortEnvironment, LongBuffer longBuffer, long[] jArr) throws OrtException {
        return createTensor(ortEnvironment, ortEnvironment.defaultAllocator, longBuffer, jArr);
    }

    static OnnxTensor createTensor(OrtEnvironment ortEnvironment, OrtAllocator ortAllocator, LongBuffer longBuffer, long[] jArr) throws OrtException {
        if (!ortAllocator.isClosed()) {
            return createTensor(OnnxJavaType.INT64, ortAllocator, longBuffer, jArr);
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to create an OnnxTensor on a closed OrtAllocator.");
        return null;
    }

    private static OnnxTensor createTensor(OnnxJavaType onnxJavaType, OrtAllocator ortAllocator, Buffer buffer, long[] jArr) throws OrtException {
        OrtUtil.BufferTuple bufferTuplePrepareBuffer = OrtUtil.prepareBuffer(buffer, onnxJavaType);
        TensorInfo tensorInfoConstructFromBuffer = TensorInfo.constructFromBuffer(bufferTuplePrepareBuffer.data, jArr, onnxJavaType);
        return new OnnxTensor(createTensorFromBuffer(OnnxRuntime.ortApiHandle, ortAllocator.handle, bufferTuplePrepareBuffer.data, bufferTuplePrepareBuffer.pos, bufferTuplePrepareBuffer.byteSize, jArr, tensorInfoConstructFromBuffer.onnxType.value), ortAllocator.handle, tensorInfoConstructFromBuffer, bufferTuplePrepareBuffer.data);
    }
}
