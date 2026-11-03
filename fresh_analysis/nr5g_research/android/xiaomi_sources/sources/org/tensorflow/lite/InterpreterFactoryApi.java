package org.tensorflow.lite;

import java.io.File;
import java.nio.ByteBuffer;
import org.tensorflow.lite.nnapi.NnApiDelegate;

/* JADX INFO: loaded from: classes.dex */
public interface InterpreterFactoryApi {
    InterpreterApi create(File file, InterpreterApi.Options options);

    InterpreterApi create(ByteBuffer byteBuffer, InterpreterApi.Options options);

    NnApiDelegate.PrivateInterface createNnApiDelegateImpl(NnApiDelegate.Options options);

    String runtimeVersion();

    String schemaVersion();
}
