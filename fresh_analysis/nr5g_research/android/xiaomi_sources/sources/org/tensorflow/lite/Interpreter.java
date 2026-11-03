package org.tensorflow.lite;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class Interpreter extends InterpreterImpl implements InterpreterApi {
    private final String[] signatureKeyList;
    private final NativeInterpreterWrapperExperimental wrapperExperimental;

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ void allocateTensors() {
        super.allocateTensors();
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi, java.lang.AutoCloseable
    public /* bridge */ /* synthetic */ void close() {
        super.close();
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ int getInputIndex(String str) {
        return super.getInputIndex(str);
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ Tensor getInputTensor(int i) {
        return super.getInputTensor(i);
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ int getInputTensorCount() {
        return super.getInputTensorCount();
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ Long getLastNativeInferenceDurationNanoseconds() {
        return super.getLastNativeInferenceDurationNanoseconds();
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ int getOutputIndex(String str) {
        return super.getOutputIndex(str);
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ Tensor getOutputTensor(int i) {
        return super.getOutputTensor(i);
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ int getOutputTensorCount() {
        return super.getOutputTensorCount();
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ void resizeInput(int i, int[] iArr) {
        super.resizeInput(i, iArr);
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ void resizeInput(int i, int[] iArr, boolean z) {
        super.resizeInput(i, iArr, z);
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ void run(Object obj, Object obj2) {
        super.run(obj, obj2);
    }

    @Override // org.tensorflow.lite.InterpreterImpl, org.tensorflow.lite.InterpreterApi
    public /* bridge */ /* synthetic */ void runForMultipleInputsOutputs(Object[] objArr, Map map) {
        super.runForMultipleInputsOutputs(objArr, map);
    }

    public static class Options extends InterpreterImpl.Options {
        public Options() {
        }

        public Options(InterpreterApi.Options options) {
            super(options);
        }

        Options(InterpreterImpl.Options options) {
            super(options);
        }

        @Override // org.tensorflow.lite.InterpreterApi.Options
        public Options setNumThreads(int numThreads) {
            super.setNumThreads(numThreads);
            return this;
        }

        @Override // org.tensorflow.lite.InterpreterApi.Options
        public Options setUseNNAPI(boolean useNNAPI) {
            super.setUseNNAPI(useNNAPI);
            return this;
        }

        @Deprecated
        public Options setAllowFp16PrecisionForFp32(boolean allow) {
            this.allowFp16PrecisionForFp32 = Boolean.valueOf(allow);
            return this;
        }

        @Override // org.tensorflow.lite.InterpreterApi.Options
        public Options addDelegate(Delegate delegate) {
            super.addDelegate(delegate);
            return this;
        }

        @Override // org.tensorflow.lite.InterpreterApi.Options
        public Options addDelegateFactory(DelegateFactory delegateFactory) {
            super.addDelegateFactory(delegateFactory);
            return this;
        }

        public Options setAllowBufferHandleOutput(boolean allow) {
            this.allowBufferHandleOutput = Boolean.valueOf(allow);
            return this;
        }

        @Override // org.tensorflow.lite.InterpreterApi.Options
        public Options setCancellable(boolean allow) {
            super.setCancellable(allow);
            return this;
        }

        public Options setUseXNNPACK(boolean useXNNPACK) {
            this.useXNNPACK = Boolean.valueOf(useXNNPACK);
            return this;
        }

        @Override // org.tensorflow.lite.InterpreterApi.Options
        public Options setRuntime(InterpreterApi.Options.TfLiteRuntime runtime) {
            super.setRuntime(runtime);
            return this;
        }
    }

    public Interpreter(File modelFile) {
        this(modelFile, (Options) null);
    }

    public Interpreter(File modelFile, Options options) {
        this(new NativeInterpreterWrapperExperimental(modelFile.getAbsolutePath(), options));
    }

    public Interpreter(ByteBuffer byteBuffer) {
        this(byteBuffer, (Options) null);
    }

    public Interpreter(ByteBuffer byteBuffer, Options options) {
        this(new NativeInterpreterWrapperExperimental(byteBuffer, options));
    }

    private Interpreter(NativeInterpreterWrapperExperimental wrapper) {
        super(wrapper);
        this.wrapperExperimental = wrapper;
        this.signatureKeyList = getSignatureKeys();
    }

    public void runSignature(Map<String, Object> inputs, Map<String, Object> outputs, String signatureKey) {
        checkNotClosed();
        if (signatureKey == null && this.signatureKeyList.length == 1) {
            signatureKey = this.signatureKeyList[0];
        }
        if (signatureKey == null) {
            throw new IllegalArgumentException("Input error: SignatureDef signatureKey should not be null. null is only allowed if the model has a single Signature. Available Signatures: " + Arrays.toString(this.signatureKeyList));
        }
        this.wrapper.runSignature(inputs, outputs, signatureKey);
    }

    public void runSignature(Map<String, Object> inputs, Map<String, Object> outputs) {
        checkNotClosed();
        runSignature(inputs, outputs, null);
    }

    public Tensor getInputTensorFromSignature(String inputName, String signatureKey) {
        checkNotClosed();
        if (signatureKey == null && this.signatureKeyList.length == 1) {
            signatureKey = this.signatureKeyList[0];
        }
        if (signatureKey == null) {
            throw new IllegalArgumentException("Input error: SignatureDef signatureKey should not be null. null is only allowed if the model has a single Signature. Available Signatures: " + Arrays.toString(this.signatureKeyList));
        }
        return this.wrapper.getInputTensor(inputName, signatureKey);
    }

    public String[] getSignatureKeys() {
        checkNotClosed();
        return this.wrapper.getSignatureKeys();
    }

    public String[] getSignatureInputs(String signatureKey) {
        checkNotClosed();
        return this.wrapper.getSignatureInputs(signatureKey);
    }

    public String[] getSignatureOutputs(String signatureKey) {
        checkNotClosed();
        return this.wrapper.getSignatureOutputs(signatureKey);
    }

    public Tensor getOutputTensorFromSignature(String outputName, String signatureKey) {
        checkNotClosed();
        if (signatureKey == null && this.signatureKeyList.length == 1) {
            signatureKey = this.signatureKeyList[0];
        }
        if (signatureKey == null) {
            throw new IllegalArgumentException("Input error: SignatureDef signatureKey should not be null. null is only allowed if the model has a single Signature. Available Signatures: " + Arrays.toString(this.signatureKeyList));
        }
        return this.wrapper.getOutputTensor(outputName, signatureKey);
    }

    public void resetVariableTensors() {
        checkNotClosed();
        this.wrapperExperimental.resetVariableTensors();
    }

    public void setCancelled(boolean cancelled) {
        this.wrapper.setCancelled(cancelled);
    }
}
