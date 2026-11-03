package ai.onnxruntime;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class OnnxSequence implements OnnxValue {
    private final long allocatorHandle;
    private final SequenceInfo info;
    final long nativeHandle;

    private native void close(long j, long j2);

    private native OnnxMap[] getMaps(long j, long j2, long j3) throws OrtException;

    private native OnnxTensor[] getTensors(long j, long j2, long j3) throws OrtException;

    static {
        try {
            OnnxRuntime.init();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load onnx-runtime library", e);
        }
    }

    OnnxSequence(long j, long j2, SequenceInfo sequenceInfo) {
        this.nativeHandle = j;
        this.allocatorHandle = j2;
        this.info = sequenceInfo;
    }

    @Override // ai.onnxruntime.OnnxValue
    public OnnxValue.OnnxValueType getType() {
        return OnnxValue.OnnxValueType.ONNX_TYPE_SEQUENCE;
    }

    @Override // ai.onnxruntime.OnnxValue
    public List getValue() throws OrtException {
        SequenceInfo sequenceInfo = this.info;
        if (sequenceInfo.sequenceOfMaps) {
            return Collections.unmodifiableList(Arrays.asList(getMaps(OnnxRuntime.ortApiHandle, this.nativeHandle, this.allocatorHandle)));
        }
        int i = AnonymousClass1.$SwitchMap$ai$onnxruntime$OnnxJavaType[sequenceInfo.sequenceType.ordinal()];
        if (i == 1 || i == 2 || i == 3 || i == 4) {
            return Collections.unmodifiableList(Arrays.asList(getTensors(OnnxRuntime.ortApiHandle, this.nativeHandle, this.allocatorHandle)));
        }
        throw new OrtException("Unsupported type in a sequence, found " + this.info.sequenceType);
    }

    /* JADX INFO: renamed from: ai.onnxruntime.OnnxSequence$1, reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ai$onnxruntime$OnnxJavaType;

        static {
            int[] iArr = new int[OnnxJavaType.values().length];
            $SwitchMap$ai$onnxruntime$OnnxJavaType = iArr;
            try {
                iArr[OnnxJavaType.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.INT64.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.DOUBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.BOOL.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.UINT8.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.INT8.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.INT16.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.INT32.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$ai$onnxruntime$OnnxJavaType[OnnxJavaType.UNKNOWN.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    @Override // ai.onnxruntime.OnnxValue
    public SequenceInfo getInfo() {
        return this.info;
    }

    public String toString() {
        return "OnnxSequence(info=" + this.info.toString() + ")";
    }

    @Override // ai.onnxruntime.OnnxValue, java.lang.AutoCloseable
    public void close() {
        close(OnnxRuntime.ortApiHandle, this.nativeHandle);
    }
}
