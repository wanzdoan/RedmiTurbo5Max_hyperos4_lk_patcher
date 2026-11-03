package org.tensorflow.lite;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.nxp.id.tp.common.HexString$$ExternalSyntheticBUOutline1;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import org.tensorflow.lite.nnapi.NnApiDelegate;

/* JADX INFO: loaded from: classes2.dex */
abstract class NativeInterpreterWrapper implements AutoCloseable {
    private static final RuntimeFlavor RUNTIME_FLAVOR = RuntimeFlavor.APPLICATION;
    long errorHandle;
    private TensorImpl[] inputTensors;
    private Map inputsIndexes;
    long interpreterHandle;
    private ByteBuffer modelByteBuffer;
    private long modelHandle;
    private TensorImpl[] outputTensors;
    private Map outputsIndexes;
    private long cancellationFlagHandle = 0;
    private long inferenceDurationNanoseconds = -1;
    private boolean isMemoryAllocated = false;
    private boolean originalGraphHasUnresolvedFlexOp = false;
    private final List delegates = new ArrayList();
    private final List ownedDelegates = new ArrayList();

    private static native long allocateTensors(long j, long j2);

    private static native void allowBufferHandleOutput(long j, boolean z);

    private static native void allowFp16PrecisionForFp32(long j, boolean z);

    private static native long createCancellationFlag(long j);

    private static native long createErrorReporter(int i);

    private static native long createInterpreter(long j, long j2, int i, boolean z, List list);

    private static native long createModelWithBuffer(ByteBuffer byteBuffer, long j);

    private static native void delete(long j, long j2, long j3);

    private static native long deleteCancellationFlag(long j);

    private static native int getInputCount(long j);

    private static native int getInputTensorIndex(long j, int i);

    private static native int getOutputCount(long j);

    private static native int getOutputTensorIndex(long j, int i);

    private static native String[] getSignatureKeys(long j);

    private static native boolean hasUnresolvedFlexOp(long j);

    private static native boolean resizeInput(long j, long j2, int i, int[] iArr, boolean z);

    private static native void run(long j, long j2);

    NativeInterpreterWrapper(ByteBuffer byteBuffer, InterpreterImpl.Options options) {
        TensorFlowLite.init();
        if (byteBuffer == null || (!(byteBuffer instanceof MappedByteBuffer) && (!byteBuffer.isDirect() || byteBuffer.order() != ByteOrder.nativeOrder()))) {
            Folme$$ExternalSyntheticBUOutline0.m("Model ByteBuffer should be either a MappedByteBuffer of the model file, or a direct ByteBuffer using ByteOrder.nativeOrder() which contains bytes of model content.");
            throw null;
        }
        this.modelByteBuffer = byteBuffer;
        long jCreateErrorReporter = createErrorReporter(512);
        init(jCreateErrorReporter, createModelWithBuffer(this.modelByteBuffer, jCreateErrorReporter), options);
    }

    private void init(long j, long j2, InterpreterImpl.Options options) {
        long j3;
        InterpreterImpl.Options options2 = options == null ? new InterpreterImpl.Options() : options;
        this.errorHandle = j;
        this.modelHandle = j2;
        ArrayList arrayList = new ArrayList();
        Boolean bool = options2.useXNNPACK;
        boolean zBooleanValue = bool != null ? bool.booleanValue() : true;
        long jCreateInterpreter = createInterpreter(j2, j, options2.getNumThreads(), zBooleanValue, arrayList);
        this.interpreterHandle = jCreateInterpreter;
        this.originalGraphHasUnresolvedFlexOp = hasUnresolvedFlexOp(jCreateInterpreter);
        addDelegates(options2);
        initDelegatesWithInterpreterFactory();
        arrayList.ensureCapacity(this.delegates.size());
        Iterator it = this.delegates.iterator();
        while (it.hasNext()) {
            arrayList.add(Long.valueOf(((Delegate) it.next()).getNativeHandle()));
        }
        if (arrayList.isEmpty()) {
            j3 = j;
        } else {
            delete(0L, 0L, this.interpreterHandle);
            j3 = j;
            this.interpreterHandle = createInterpreter(j2, j3, options2.getNumThreads(), zBooleanValue, arrayList);
        }
        Boolean bool2 = options2.allowFp16PrecisionForFp32;
        if (bool2 != null) {
            allowFp16PrecisionForFp32(this.interpreterHandle, bool2.booleanValue());
        }
        Boolean bool3 = options2.allowBufferHandleOutput;
        if (bool3 != null) {
            allowBufferHandleOutput(this.interpreterHandle, bool3.booleanValue());
        }
        if (options2.isCancellable()) {
            this.cancellationFlagHandle = createCancellationFlag(this.interpreterHandle);
        }
        this.inputTensors = new TensorImpl[getInputCount(this.interpreterHandle)];
        this.outputTensors = new TensorImpl[getOutputCount(this.interpreterHandle)];
        Boolean bool4 = options2.allowFp16PrecisionForFp32;
        if (bool4 != null) {
            allowFp16PrecisionForFp32(this.interpreterHandle, bool4.booleanValue());
        }
        Boolean bool5 = options2.allowBufferHandleOutput;
        if (bool5 != null) {
            allowBufferHandleOutput(this.interpreterHandle, bool5.booleanValue());
        }
        allocateTensors(this.interpreterHandle, j3);
        this.isMemoryAllocated = true;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        int i = 0;
        while (true) {
            TensorImpl[] tensorImplArr = this.inputTensors;
            if (i >= tensorImplArr.length) {
                break;
            }
            TensorImpl tensorImpl = tensorImplArr[i];
            if (tensorImpl != null) {
                tensorImpl.close();
                this.inputTensors[i] = null;
            }
            i++;
        }
        int i2 = 0;
        while (true) {
            TensorImpl[] tensorImplArr2 = this.outputTensors;
            if (i2 >= tensorImplArr2.length) {
                break;
            }
            TensorImpl tensorImpl2 = tensorImplArr2[i2];
            if (tensorImpl2 != null) {
                tensorImpl2.close();
                this.outputTensors[i2] = null;
            }
            i2++;
        }
        delete(this.errorHandle, this.modelHandle, this.interpreterHandle);
        deleteCancellationFlag(this.cancellationFlagHandle);
        this.errorHandle = 0L;
        this.modelHandle = 0L;
        this.interpreterHandle = 0L;
        this.cancellationFlagHandle = 0L;
        this.modelByteBuffer = null;
        this.inputsIndexes = null;
        this.outputsIndexes = null;
        this.isMemoryAllocated = false;
        this.delegates.clear();
        Iterator it = this.ownedDelegates.iterator();
        while (it.hasNext()) {
            ((Delegate) it.next()).close();
        }
        this.ownedDelegates.clear();
    }

    void run(Object[] objArr, Map map) {
        this.inferenceDurationNanoseconds = -1L;
        if (objArr == null || objArr.length == 0) {
            Folme$$ExternalSyntheticBUOutline0.m("Input error: Inputs should not be null or empty.");
            return;
        }
        if (map == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Input error: Outputs should not be null.");
            return;
        }
        for (int i = 0; i < objArr.length; i++) {
            int[] inputShapeIfDifferent = getInputTensor(i).getInputShapeIfDifferent(objArr[i]);
            if (inputShapeIfDifferent != null) {
                resizeInput(i, inputShapeIfDifferent);
            }
        }
        boolean zAllocateTensorsIfNeeded = allocateTensorsIfNeeded();
        for (int i2 = 0; i2 < objArr.length; i2++) {
            getInputTensor(i2).setTo(objArr[i2]);
        }
        long jNanoTime = System.nanoTime();
        run(this.interpreterHandle, this.errorHandle);
        long jNanoTime2 = System.nanoTime() - jNanoTime;
        if (zAllocateTensorsIfNeeded) {
            for (TensorImpl tensorImpl : this.outputTensors) {
                if (tensorImpl != null) {
                    tensorImpl.refreshShape();
                }
            }
        }
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() != null) {
                getOutputTensor(((Integer) entry.getKey()).intValue()).copyTo(entry.getValue());
            }
        }
        this.inferenceDurationNanoseconds = jNanoTime2;
    }

    void resizeInput(int i, int[] iArr) {
        resizeInput(i, iArr, false);
    }

    void resizeInput(int i, int[] iArr, boolean z) {
        if (resizeInput(this.interpreterHandle, this.errorHandle, i, iArr, z)) {
            this.isMemoryAllocated = false;
            TensorImpl tensorImpl = this.inputTensors[i];
            if (tensorImpl != null) {
                tensorImpl.refreshShape();
            }
        }
    }

    private boolean allocateTensorsIfNeeded() {
        if (this.isMemoryAllocated) {
            return false;
        }
        this.isMemoryAllocated = true;
        allocateTensors(this.interpreterHandle, this.errorHandle);
        for (TensorImpl tensorImpl : this.outputTensors) {
            if (tensorImpl != null) {
                tensorImpl.refreshShape();
            }
        }
        return true;
    }

    TensorImpl getInputTensor(int i) {
        if (i >= 0) {
            TensorImpl[] tensorImplArr = this.inputTensors;
            if (i < tensorImplArr.length) {
                TensorImpl tensorImpl = tensorImplArr[i];
                if (tensorImpl != null) {
                    return tensorImpl;
                }
                long j = this.interpreterHandle;
                TensorImpl tensorImplFromIndex = TensorImpl.fromIndex(j, getInputTensorIndex(j, i));
                tensorImplArr[i] = tensorImplFromIndex;
                return tensorImplFromIndex;
            }
        }
        HexString$$ExternalSyntheticBUOutline1.m("Invalid input Tensor index: ", i);
        return null;
    }

    public String[] getSignatureKeys() {
        return getSignatureKeys(this.interpreterHandle);
    }

    TensorImpl getOutputTensor(int i) {
        if (i >= 0) {
            TensorImpl[] tensorImplArr = this.outputTensors;
            if (i < tensorImplArr.length) {
                TensorImpl tensorImpl = tensorImplArr[i];
                if (tensorImpl != null) {
                    return tensorImpl;
                }
                long j = this.interpreterHandle;
                TensorImpl tensorImplFromIndex = TensorImpl.fromIndex(j, getOutputTensorIndex(j, i));
                tensorImplArr[i] = tensorImplFromIndex;
                return tensorImplFromIndex;
            }
        }
        HexString$$ExternalSyntheticBUOutline1.m("Invalid output Tensor index: ", i);
        return null;
    }

    private void addDelegates(InterpreterImpl.Options options) {
        Delegate delegateMaybeCreateFlexDelegate;
        if (this.originalGraphHasUnresolvedFlexOp && (delegateMaybeCreateFlexDelegate = maybeCreateFlexDelegate(options.getDelegates())) != null) {
            this.ownedDelegates.add(delegateMaybeCreateFlexDelegate);
            this.delegates.add(delegateMaybeCreateFlexDelegate);
        }
        addUserProvidedDelegates(options);
        Iterator it = options.getDelegateFactories().iterator();
        if (it.hasNext()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            throw null;
        }
        if (options.getUseNNAPI()) {
            NnApiDelegate nnApiDelegate = new NnApiDelegate();
            this.ownedDelegates.add(nnApiDelegate);
            this.delegates.add(nnApiDelegate);
        }
    }

    private void addUserProvidedDelegates(InterpreterImpl.Options options) {
        for (Delegate delegate : options.getDelegates()) {
            if (options.getRuntime() != InterpreterApi.Options.TfLiteRuntime.FROM_APPLICATION_ONLY && !(delegate instanceof NnApiDelegate)) {
                Folme$$ExternalSyntheticBUOutline0.m("Instantiated delegates (other than NnApiDelegate) are not allowed when using TF Lite from Google Play Services. Please use InterpreterApi.Options.addDelegateFactory() with an appropriate DelegateFactory instead.");
                return;
            }
            this.delegates.add(delegate);
        }
    }

    private void initDelegatesWithInterpreterFactory() {
        InterpreterFactoryImpl interpreterFactoryImpl = new InterpreterFactoryImpl();
        for (Delegate delegate : this.delegates) {
            if (delegate instanceof NnApiDelegate) {
                ((NnApiDelegate) delegate).initWithInterpreterFactoryApi(interpreterFactoryImpl);
            }
        }
    }

    private static Delegate maybeCreateFlexDelegate(List list) {
        try {
            Class<?> cls = Class.forName("org.tensorflow.lite.flex.FlexDelegate");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (cls.isInstance((Delegate) it.next())) {
                    return null;
                }
            }
            Class[] clsArr = new Class[0];
            return (Delegate) cls.getConstructor(null).newInstance(null);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException unused) {
            return null;
        }
    }
}
