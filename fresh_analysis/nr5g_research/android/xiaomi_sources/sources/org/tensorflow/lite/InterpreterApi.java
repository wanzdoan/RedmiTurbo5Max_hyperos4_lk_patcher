package org.tensorflow.lite;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface InterpreterApi extends AutoCloseable {
    void allocateTensors();

    @Override // java.lang.AutoCloseable
    void close();

    int getInputIndex(String str);

    Tensor getInputTensor(int i);

    int getInputTensorCount();

    Long getLastNativeInferenceDurationNanoseconds();

    int getOutputIndex(String str);

    Tensor getOutputTensor(int i);

    int getOutputTensorCount();

    void resizeInput(int i, int[] iArr);

    void resizeInput(int i, int[] iArr, boolean z);

    void run(Object obj, Object obj2);

    void runForMultipleInputsOutputs(Object[] objArr, Map<Integer, Object> map);

    public static class Options {
        Boolean allowCancellation;
        private final List<DelegateFactory> delegateFactories;
        final List<Delegate> delegates;
        int numThreads;
        TfLiteRuntime runtime;
        Boolean useNNAPI;

        public enum TfLiteRuntime {
            FROM_APPLICATION_ONLY,
            FROM_SYSTEM_ONLY,
            PREFER_SYSTEM_OVER_APPLICATION
        }

        public Options() {
            this.runtime = TfLiteRuntime.FROM_APPLICATION_ONLY;
            this.numThreads = -1;
            this.delegates = new ArrayList();
            this.delegateFactories = new ArrayList();
        }

        public Options(Options other) {
            this.runtime = TfLiteRuntime.FROM_APPLICATION_ONLY;
            this.numThreads = -1;
            this.numThreads = other.numThreads;
            this.useNNAPI = other.useNNAPI;
            this.allowCancellation = other.allowCancellation;
            this.delegates = new ArrayList(other.delegates);
            this.delegateFactories = new ArrayList(other.delegateFactories);
            this.runtime = other.runtime;
        }

        public Options setNumThreads(int numThreads) {
            this.numThreads = numThreads;
            return this;
        }

        public int getNumThreads() {
            return this.numThreads;
        }

        public Options setUseNNAPI(boolean useNNAPI) {
            this.useNNAPI = Boolean.valueOf(useNNAPI);
            return this;
        }

        public boolean getUseNNAPI() {
            return this.useNNAPI != null && this.useNNAPI.booleanValue();
        }

        public Options setCancellable(boolean allow) {
            this.allowCancellation = Boolean.valueOf(allow);
            return this;
        }

        public boolean isCancellable() {
            return this.allowCancellation != null && this.allowCancellation.booleanValue();
        }

        public Options addDelegate(Delegate delegate) {
            this.delegates.add(delegate);
            return this;
        }

        public List<Delegate> getDelegates() {
            return Collections.unmodifiableList(this.delegates);
        }

        public Options addDelegateFactory(DelegateFactory delegateFactory) {
            this.delegateFactories.add(delegateFactory);
            return this;
        }

        public List<DelegateFactory> getDelegateFactories() {
            return Collections.unmodifiableList(this.delegateFactories);
        }

        public Options setRuntime(TfLiteRuntime runtime) {
            this.runtime = runtime;
            return this;
        }

        public TfLiteRuntime getRuntime() {
            return this.runtime;
        }
    }

    static InterpreterApi create(File modelFile, Options options) {
        Options.TfLiteRuntime runtime = options == null ? null : options.getRuntime();
        InterpreterFactoryApi factory = TensorFlowLite.getFactory(runtime);
        return factory.create(modelFile, options);
    }

    static InterpreterApi create(ByteBuffer byteBuffer, Options options) {
        Options.TfLiteRuntime runtime = options == null ? null : options.getRuntime();
        InterpreterFactoryApi factory = TensorFlowLite.getFactory(runtime);
        return factory.create(byteBuffer, options);
    }
}
