package ai.onnxruntime;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public final class OrtUtil {
    static int capacityFromSize(int i) {
        return (int) ((((double) i) / 0.75d) + 1.0d);
    }

    private OrtUtil() {
    }

    public static int[] transformShape(long[] jArr) {
        if (jArr.length == 0 || jArr.length > 8) {
            Folme$$ExternalSyntheticBUOutline0.m("Arrays with less than 1 and greater than 8 dimensions are not supported.");
            return null;
        }
        int[] iArr = new int[jArr.length];
        for (int i = 0; i < jArr.length; i++) {
            long j = jArr[i];
            if (j < 0 || j > 2147483647L) {
                OrtUtil$$ExternalSyntheticBUOutline4.m("Invalid shape for a Java array, expected non-negative entries smaller than Integer.MAX_VALUE. Found ", Arrays.toString(jArr));
                return null;
            }
            iArr[i] = (int) j;
        }
        return iArr;
    }

    public static long[] transformShape(int[] iArr) {
        if (iArr.length == 0 || iArr.length > 8) {
            Folme$$ExternalSyntheticBUOutline0.m("Arrays with less than 1 and greater than 8 dimensions are not supported.");
            return null;
        }
        long[] jArr = new long[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            long j = iArr[i];
            if (j < 1) {
                OrtUtil$$ExternalSyntheticBUOutline4.m("Invalid shape for a Java array, expected positive entries smaller than Integer.MAX_VALUE. Found ", Arrays.toString(iArr));
                return null;
            }
            jArr[i] = j;
        }
        return jArr;
    }

    public static Object newBooleanArray(long[] jArr) {
        return Array.newInstance((Class<?>) Boolean.TYPE, transformShape(jArr));
    }

    public static Object newByteArray(long[] jArr) {
        return Array.newInstance((Class<?>) Byte.TYPE, transformShape(jArr));
    }

    public static Object newShortArray(long[] jArr) {
        return Array.newInstance((Class<?>) Short.TYPE, transformShape(jArr));
    }

    public static Object newIntArray(long[] jArr) {
        return Array.newInstance((Class<?>) Integer.TYPE, transformShape(jArr));
    }

    public static Object newLongArray(long[] jArr) {
        return Array.newInstance((Class<?>) Long.TYPE, transformShape(jArr));
    }

    public static Object newFloatArray(long[] jArr) {
        return Array.newInstance((Class<?>) Float.TYPE, transformShape(jArr));
    }

    public static Object newDoubleArray(long[] jArr) {
        return Array.newInstance((Class<?>) Double.TYPE, transformShape(jArr));
    }

    public static Object newStringArray(long[] jArr) {
        return Array.newInstance((Class<?>) String.class, transformShape(jArr));
    }

    public static Object reshape(boolean[] zArr, long[] jArr) {
        Object objNewBooleanArray = newBooleanArray(jArr);
        reshape(zArr, objNewBooleanArray, 0);
        return objNewBooleanArray;
    }

    public static Object reshape(byte[] bArr, long[] jArr) {
        Object objNewByteArray = newByteArray(jArr);
        reshape(bArr, objNewByteArray, 0);
        return objNewByteArray;
    }

    public static Object reshape(short[] sArr, long[] jArr) {
        Object objNewShortArray = newShortArray(jArr);
        reshape(sArr, objNewShortArray, 0);
        return objNewShortArray;
    }

    public static Object reshape(int[] iArr, long[] jArr) {
        Object objNewIntArray = newIntArray(jArr);
        reshape(iArr, objNewIntArray, 0);
        return objNewIntArray;
    }

    public static Object reshape(long[] jArr, long[] jArr2) {
        Object objNewLongArray = newLongArray(jArr2);
        reshape(jArr, objNewLongArray, 0);
        return objNewLongArray;
    }

    public static Object reshape(float[] fArr, long[] jArr) {
        Object objNewFloatArray = newFloatArray(jArr);
        reshape(fArr, objNewFloatArray, 0);
        return objNewFloatArray;
    }

    public static Object reshape(double[] dArr, long[] jArr) {
        Object objNewDoubleArray = newDoubleArray(jArr);
        reshape(dArr, objNewDoubleArray, 0);
        return objNewDoubleArray;
    }

    public static Object reshape(String[] strArr, long[] jArr) {
        Object objNewStringArray = newStringArray(jArr);
        reshape(strArr, objNewStringArray, 0);
        return objNewStringArray;
    }

    private static int reshape(Object obj, Object obj2, int i) {
        if (obj2.getClass().isArray()) {
            for (Object obj3 : (Object[]) obj2) {
                Class<?> cls = obj3.getClass();
                if (cls.isArray()) {
                    Class<?> componentType = cls.getComponentType();
                    if (componentType.isPrimitive() || componentType == String.class) {
                        int length = Array.getLength(obj3);
                        System.arraycopy(obj, i, obj3, 0, length);
                        i += length;
                    } else {
                        i = reshape(obj, obj3, i);
                    }
                } else {
                    OrtUtil$$ExternalSyntheticBUOutline2.m("Found element type when expecting an array. Class ", cls);
                    return 0;
                }
            }
            return i;
        }
        OrtUtil$$ExternalSyntheticBUOutline1.m("Found element type when expecting an array. Class ", obj2.getClass());
        return 0;
    }

    public static long elementCount(long[] jArr) {
        long j = 1;
        for (long j2 : jArr) {
            if (j2 < 0) {
                OrtUtil$$ExternalSyntheticBUOutline3.m("Received negative value in shape ", Arrays.toString(jArr), " .");
                return 0L;
            }
            j *= j2;
        }
        return j;
    }

    public static boolean validateShape(long[] jArr) {
        boolean z = true;
        for (int i = 0; i < jArr.length; i++) {
            long j = jArr[i];
            z = z & (j > 0) & (((long) ((int) j)) == j);
        }
        return z && jArr.length <= 8;
    }

    public static String[] flattenString(Object obj) {
        if (obj instanceof String[]) {
            return (String[]) obj;
        }
        ArrayList arrayList = new ArrayList();
        flattenString((Object[]) obj, arrayList);
        return (String[]) arrayList.toArray(new String[0]);
    }

    private static void flattenString(Object[] objArr, ArrayList arrayList) {
        for (Object obj : objArr) {
            Class<?> cls = obj.getClass();
            if (cls.isArray()) {
                if (cls.getComponentType().isArray()) {
                    flattenString((Object[]) obj, arrayList);
                } else if (cls.getComponentType().equals(String.class)) {
                    arrayList.addAll(Arrays.asList((String[]) obj));
                } else {
                    OrtUtil$$ExternalSyntheticBUOutline2.m("Found a non-String, non-array element type, ", cls);
                    return;
                }
            } else {
                OrtUtil$$ExternalSyntheticBUOutline2.m("Found an element type where there should have been an array. Class = ", cls);
                return;
            }
        }
    }

    /* JADX INFO: renamed from: ai.onnxruntime.OrtUtil$1, reason: invalid class name */
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

    static Object convertBoxedPrimitiveToArray(OnnxJavaType onnxJavaType, Object obj) {
        switch (AnonymousClass1.$SwitchMap$ai$onnxruntime$OnnxJavaType[onnxJavaType.ordinal()]) {
            case 1:
                return new float[]{((Float) obj).floatValue()};
            case 2:
                return new double[]{((Double) obj).doubleValue()};
            case 3:
            case 4:
                return new byte[]{((Byte) obj).byteValue()};
            case 5:
                return new short[]{((Short) obj).shortValue()};
            case 6:
                return new int[]{((Integer) obj).intValue()};
            case 7:
                return new long[]{((Long) obj).longValue()};
            case 8:
                return new boolean[]{((Boolean) obj).booleanValue()};
            default:
                return null;
        }
    }

    static BufferTuple prepareBuffer(Buffer buffer, OnnxJavaType onnxJavaType) {
        Buffer bufferPut;
        Buffer buffer2;
        int iPosition;
        long jRemaining = buffer.remaining();
        int i = onnxJavaType.size;
        if (jRemaining * ((long) i) > Integer.MAX_VALUE - (i * 8)) {
            throw new IllegalStateException("Cannot allocate a direct buffer of the requested size and type, size " + buffer.remaining() + ", type = " + onnxJavaType);
        }
        int iRemaining = buffer.remaining() * onnxJavaType.size;
        if (buffer.isDirect()) {
            buffer2 = buffer;
            iPosition = buffer.position() * onnxJavaType.size;
        } else {
            int iPosition2 = buffer.position();
            ByteBuffer byteBufferOrder = ByteBuffer.allocateDirect(iRemaining).order(ByteOrder.nativeOrder());
            switch (AnonymousClass1.$SwitchMap$ai$onnxruntime$OnnxJavaType[onnxJavaType.ordinal()]) {
                case 1:
                    bufferPut = byteBufferOrder.asFloatBuffer().put((FloatBuffer) buffer);
                    break;
                case 2:
                    bufferPut = byteBufferOrder.asDoubleBuffer().put((DoubleBuffer) buffer);
                    break;
                case 3:
                case 4:
                    bufferPut = byteBufferOrder.put((ByteBuffer) buffer);
                    break;
                case 5:
                    bufferPut = byteBufferOrder.asShortBuffer().put((ShortBuffer) buffer);
                    break;
                case 6:
                    bufferPut = byteBufferOrder.asIntBuffer().put((IntBuffer) buffer);
                    break;
                case 7:
                    bufferPut = byteBufferOrder.asLongBuffer().put((LongBuffer) buffer);
                    break;
                default:
                    OrtUtil$$ExternalSyntheticBUOutline0.m("Impossible to reach here, managed to cast a buffer as an incorrect type");
                    return null;
            }
            buffer.position(iPosition2);
            bufferPut.rewind();
            buffer2 = bufferPut;
            iPosition = 0;
        }
        return new BufferTuple(buffer2, iPosition, iRemaining, buffer.remaining(), buffer2 != buffer);
    }

    final class BufferTuple {
        final long byteSize;
        final Buffer data;
        final boolean isCopy;
        final int pos;
        final long size;

        BufferTuple(Buffer buffer, int i, long j, long j2, boolean z) {
            this.data = buffer;
            this.pos = i;
            this.byteSize = j;
            this.size = j2;
            this.isCopy = z;
        }
    }
}
