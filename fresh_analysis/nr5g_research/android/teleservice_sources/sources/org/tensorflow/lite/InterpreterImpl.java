package org.tensorflow.lite;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
abstract class InterpreterImpl implements InterpreterApi {
    NativeInterpreterWrapper wrapper;

    class Options extends InterpreterApi.Options {
        Boolean allowBufferHandleOutput;
        Boolean allowFp16PrecisionForFp32;
        Boolean useXNNPACK;
    }

    InterpreterImpl(NativeInterpreterWrapper nativeInterpreterWrapper) {
        this.wrapper = nativeInterpreterWrapper;
    }

    public void run(Object obj, Object obj2) {
        HashMap map = new HashMap();
        map.put(0, obj2);
        runForMultipleInputsOutputs(new Object[]{obj}, map);
    }

    public void runForMultipleInputsOutputs(Object[] objArr, Map map) {
        checkNotClosed();
        this.wrapper.run(objArr, map);
    }

    public Tensor getInputTensor(int i) {
        checkNotClosed();
        return this.wrapper.getInputTensor(i);
    }

    public Tensor getOutputTensor(int i) {
        checkNotClosed();
        return this.wrapper.getOutputTensor(i);
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        NativeInterpreterWrapper nativeInterpreterWrapper = this.wrapper;
        if (nativeInterpreterWrapper != null) {
            nativeInterpreterWrapper.close();
            this.wrapper = null;
        }
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    void checkNotClosed() {
        if (this.wrapper != null) {
            return;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Internal error: The Interpreter has already been closed.");
    }
}
