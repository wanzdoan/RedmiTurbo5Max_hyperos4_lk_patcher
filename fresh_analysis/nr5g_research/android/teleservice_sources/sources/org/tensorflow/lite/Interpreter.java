package org.tensorflow.lite;

import java.nio.ByteBuffer;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public final class Interpreter extends InterpreterImpl implements InterpreterApi {
    private final String[] signatureKeyList;
    private final NativeInterpreterWrapperExperimental wrapperExperimental;

    public abstract class Options extends InterpreterImpl.Options {
    }

    @Override // org.tensorflow.lite.InterpreterImpl, java.lang.AutoCloseable
    public /* bridge */ /* synthetic */ void close() {
        super.close();
    }

    @Override // org.tensorflow.lite.InterpreterImpl
    public /* bridge */ /* synthetic */ Tensor getInputTensor(int i) {
        return super.getInputTensor(i);
    }

    @Override // org.tensorflow.lite.InterpreterImpl
    public /* bridge */ /* synthetic */ Tensor getOutputTensor(int i) {
        return super.getOutputTensor(i);
    }

    @Override // org.tensorflow.lite.InterpreterImpl
    public /* bridge */ /* synthetic */ void run(Object obj, Object obj2) {
        super.run(obj, obj2);
    }

    @Override // org.tensorflow.lite.InterpreterImpl
    public /* bridge */ /* synthetic */ void runForMultipleInputsOutputs(Object[] objArr, Map map) {
        super.runForMultipleInputsOutputs(objArr, map);
    }

    public Interpreter(ByteBuffer byteBuffer) {
        this(byteBuffer, null);
    }

    public Interpreter(ByteBuffer byteBuffer, Options options) {
        this(new NativeInterpreterWrapperExperimental(byteBuffer, options));
    }

    private Interpreter(NativeInterpreterWrapperExperimental nativeInterpreterWrapperExperimental) {
        super(nativeInterpreterWrapperExperimental);
        this.wrapperExperimental = nativeInterpreterWrapperExperimental;
        this.signatureKeyList = getSignatureKeys();
    }

    public String[] getSignatureKeys() {
        checkNotClosed();
        return this.wrapper.getSignatureKeys();
    }
}
