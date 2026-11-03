package ai.onnxruntime;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
class OrtAllocator implements AutoCloseable {
    private boolean closed = false;
    final long handle;
    private final boolean isDefault;

    private native void closeAllocator(long j, long j2) throws OrtException;

    static {
        try {
            OnnxRuntime.init();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load onnx-runtime library", e);
        }
    }

    OrtAllocator(long j, boolean z) {
        this.handle = j;
        this.isDefault = z;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    @Override // java.lang.AutoCloseable
    public void close() throws OrtException {
        if (!this.closed) {
            if (this.isDefault) {
                return;
            }
            closeAllocator(OnnxRuntime.ortApiHandle, this.handle);
            this.closed = true;
            return;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to close an already closed OrtAllocator.");
    }
}
