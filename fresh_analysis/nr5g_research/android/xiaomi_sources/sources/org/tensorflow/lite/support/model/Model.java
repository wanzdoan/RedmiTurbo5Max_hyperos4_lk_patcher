package org.tensorflow.lite.support.model;

import android.content.Context;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.Map;
import org.tensorflow.lite.Delegate;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.SupportPreconditions;

/* JADX INFO: loaded from: classes.dex */
public class Model {
    private final MappedByteBuffer byteModel;
    private final GpuDelegateProxy gpuDelegateProxy;
    private final Interpreter interpreter;
    private final String modelPath;

    public enum Device {
        CPU,
        NNAPI,
        GPU
    }

    public static class Options {
        private final Device device;
        private final int numThreads;

        public static class Builder {
            private Device device = Device.CPU;
            private int numThreads = 1;

            public Builder setDevice(Device device) {
                this.device = device;
                return this;
            }

            public Builder setNumThreads(int numThreads) {
                this.numThreads = numThreads;
                return this;
            }

            public Options build() {
                return new Options(this);
            }
        }

        private Options(Builder builder) {
            this.device = builder.device;
            this.numThreads = builder.numThreads;
        }
    }

    @Deprecated
    public static class Builder {
        private final MappedByteBuffer byteModel;
        private final String modelPath;
        private Device device = Device.CPU;
        private int numThreads = 1;

        public Builder(Context context, String modelPath) throws IOException {
            this.modelPath = modelPath;
            this.byteModel = FileUtil.loadMappedFile(context, modelPath);
        }

        public Builder setDevice(Device device) {
            this.device = device;
            return this;
        }

        public Builder setNumThreads(int numThreads) {
            this.numThreads = numThreads;
            return this;
        }

        public Model build() {
            Options options = new Options.Builder().setNumThreads(this.numThreads).setDevice(this.device).build();
            return Model.createModel(this.byteModel, this.modelPath, options);
        }
    }

    public static Model createModel(Context context, String modelPath) throws IOException {
        return createModel(context, modelPath, new Options.Builder().build());
    }

    public static Model createModel(Context context, String modelPath, Options options) throws IOException {
        SupportPreconditions.checkNotEmpty(modelPath, "Model path in the asset folder cannot be empty.");
        MappedByteBuffer byteModel = FileUtil.loadMappedFile(context, modelPath);
        return createModel(byteModel, modelPath, options);
    }

    public static Model createModel(MappedByteBuffer byteModel, String modelPath, Options options) {
        Interpreter.Options interpreterOptions = new Interpreter.Options();
        GpuDelegateProxy gpuDelegateProxy = null;
        switch (options.device.ordinal()) {
            case 1:
                interpreterOptions.setUseNNAPI(true);
                break;
            case 2:
                gpuDelegateProxy = GpuDelegateProxy.maybeNewInstance();
                SupportPreconditions.checkArgument(gpuDelegateProxy != null, "Cannot inference with GPU. Did you add \"tensorflow-lite-gpu\" as dependency?");
                interpreterOptions.addDelegate((Delegate) gpuDelegateProxy);
                break;
        }
        interpreterOptions.setNumThreads(options.numThreads);
        Interpreter interpreter = new Interpreter(byteModel, interpreterOptions);
        return new Model(modelPath, byteModel, interpreter, gpuDelegateProxy);
    }

    public MappedByteBuffer getData() {
        return this.byteModel;
    }

    public String getPath() {
        return this.modelPath;
    }

    public Tensor getInputTensor(int inputIndex) {
        return this.interpreter.getInputTensor(inputIndex);
    }

    public Tensor getOutputTensor(int outputIndex) {
        return this.interpreter.getOutputTensor(outputIndex);
    }

    public int[] getOutputTensorShape(int outputIndex) {
        return this.interpreter.getOutputTensor(outputIndex).shape();
    }

    public void run(Object[] inputs, Map<Integer, Object> outputs) {
        this.interpreter.runForMultipleInputsOutputs(inputs, outputs);
    }

    public void close() {
        if (this.interpreter != null) {
            this.interpreter.close();
        }
        if (this.gpuDelegateProxy != null) {
            this.gpuDelegateProxy.close();
        }
    }

    private Model(String modelPath, MappedByteBuffer byteModel, Interpreter interpreter, GpuDelegateProxy gpuDelegateProxy) {
        this.modelPath = modelPath;
        this.byteModel = byteModel;
        this.interpreter = interpreter;
        this.gpuDelegateProxy = gpuDelegateProxy;
    }
}
