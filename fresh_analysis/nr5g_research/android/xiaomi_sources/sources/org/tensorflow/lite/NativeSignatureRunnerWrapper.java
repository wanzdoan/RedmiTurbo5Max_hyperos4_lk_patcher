package org.tensorflow.lite;

/* JADX INFO: loaded from: classes.dex */
final class NativeSignatureRunnerWrapper {
    private final long errorHandle;
    private boolean isMemoryAllocated = false;
    private final long signatureRunnerHandle;

    private static native void nativeAllocateTensors(long j, long j2);

    private static native int nativeGetInputIndex(long j, String str);

    private static native int nativeGetOutputIndex(long j, String str);

    private static native long nativeGetSignatureRunner(long j, String str);

    private static native int nativeGetSubgraphIndex(long j);

    private static native String[] nativeInputNames(long j);

    private static native void nativeInvoke(long j, long j2);

    private static native String[] nativeOutputNames(long j);

    private static native boolean nativeResizeInput(long j, long j2, String str, int[] iArr);

    NativeSignatureRunnerWrapper(long interpreterHandle, long errorHandle, String signatureKey) {
        this.errorHandle = errorHandle;
        this.signatureRunnerHandle = nativeGetSignatureRunner(interpreterHandle, signatureKey);
        if (this.signatureRunnerHandle == -1) {
            throw new IllegalArgumentException("Input error: Signature " + signatureKey + " not found.");
        }
    }

    public int getSubgraphIndex() {
        return nativeGetSubgraphIndex(this.signatureRunnerHandle);
    }

    public String[] inputNames() {
        return nativeInputNames(this.signatureRunnerHandle);
    }

    public String[] outputNames() {
        return nativeOutputNames(this.signatureRunnerHandle);
    }

    public TensorImpl getInputTensor(String inputName) {
        return TensorImpl.fromSignatureInput(this.signatureRunnerHandle, inputName);
    }

    public TensorImpl getOutputTensor(String outputName) {
        return TensorImpl.fromSignatureOutput(this.signatureRunnerHandle, outputName);
    }

    public int getInputIndex(String inputName) {
        int inputIndex = nativeGetInputIndex(this.signatureRunnerHandle, inputName);
        if (inputIndex == -1) {
            throw new IllegalArgumentException("Input error: input " + inputName + " not found.");
        }
        return inputIndex;
    }

    public int getOutputIndex(String outputName) {
        int outputIndex = nativeGetOutputIndex(this.signatureRunnerHandle, outputName);
        if (outputIndex == -1) {
            throw new IllegalArgumentException("Input error: output " + outputName + " not found.");
        }
        return outputIndex;
    }

    public boolean resizeInput(String inputName, int[] dims) {
        this.isMemoryAllocated = false;
        return nativeResizeInput(this.signatureRunnerHandle, this.errorHandle, inputName, dims);
    }

    public void allocateTensorsIfNeeded() {
        if (this.isMemoryAllocated) {
            return;
        }
        nativeAllocateTensors(this.signatureRunnerHandle, this.errorHandle);
        this.isMemoryAllocated = true;
    }

    public void invoke() {
        nativeInvoke(this.signatureRunnerHandle, this.errorHandle);
    }
}
