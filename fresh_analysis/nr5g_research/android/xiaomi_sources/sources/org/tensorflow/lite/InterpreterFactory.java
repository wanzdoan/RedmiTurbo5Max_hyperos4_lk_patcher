package org.tensorflow.lite;

import java.io.File;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class InterpreterFactory {
    public InterpreterApi create(File modelFile, InterpreterApi.Options options) {
        return InterpreterApi.create(modelFile, options);
    }

    public InterpreterApi create(ByteBuffer byteBuffer, InterpreterApi.Options options) {
        return InterpreterApi.create(byteBuffer, options);
    }
}
