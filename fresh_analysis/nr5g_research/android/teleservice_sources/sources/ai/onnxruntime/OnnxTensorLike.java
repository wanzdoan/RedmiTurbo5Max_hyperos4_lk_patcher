package ai.onnxruntime;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public abstract class OnnxTensorLike implements OnnxValue {
    protected final long allocatorHandle;
    protected final TensorInfo info;
    protected final long nativeHandle;

    static {
        try {
            OnnxRuntime.init();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load onnx-runtime library", e);
        }
    }

    OnnxTensorLike(long j, long j2, TensorInfo tensorInfo) {
        this.nativeHandle = j;
        this.allocatorHandle = j2;
        this.info = tensorInfo;
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    @Override // ai.onnxruntime.OnnxValue
    public TensorInfo getInfo() {
        return this.info;
    }
}
