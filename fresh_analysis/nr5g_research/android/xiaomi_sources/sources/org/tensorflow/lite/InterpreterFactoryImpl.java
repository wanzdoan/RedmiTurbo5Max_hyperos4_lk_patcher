package org.tensorflow.lite;

import java.io.File;
import java.nio.ByteBuffer;
import org.tensorflow.lite.nnapi.NnApiDelegate;
import org.tensorflow.lite.nnapi.NnApiDelegateImpl;

/* JADX INFO: loaded from: classes.dex */
class InterpreterFactoryImpl implements InterpreterFactoryApi {
    private static native String nativeRuntimeVersion();

    private static native String nativeSchemaVersion();

    @Override // org.tensorflow.lite.InterpreterFactoryApi
    public InterpreterApi create(File modelFile, InterpreterApi.Options options) {
        return new InterpreterImpl(modelFile, options == null ? null : new InterpreterImpl.Options(options));
    }

    @Override // org.tensorflow.lite.InterpreterFactoryApi
    public InterpreterApi create(ByteBuffer byteBuffer, InterpreterApi.Options options) {
        return new InterpreterImpl(byteBuffer, options == null ? null : new InterpreterImpl.Options(options));
    }

    @Override // org.tensorflow.lite.InterpreterFactoryApi
    public String runtimeVersion() {
        TensorFlowLite.init();
        return nativeRuntimeVersion();
    }

    @Override // org.tensorflow.lite.InterpreterFactoryApi
    public String schemaVersion() {
        TensorFlowLite.init();
        return nativeSchemaVersion();
    }

    @Override // org.tensorflow.lite.InterpreterFactoryApi
    public NnApiDelegate.PrivateInterface createNnApiDelegateImpl(NnApiDelegate.Options options) {
        return new NnApiDelegateImpl(options);
    }
}
