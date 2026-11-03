package ai.onnxruntime;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public final class OrtTrainingSession implements AutoCloseable {
    private final OrtAllocator allocator;
    private final OrtCheckpointState checkpoint;
    private boolean closed;
    private final Set evalInputNames;
    private final Set evalOutputNames;
    private final String evalPath;
    private final long nativeHandle;
    private final String optimizerPath;
    private final Set trainInputNames;
    private final Set trainOutputNames;
    private final String trainPath;

    private native void closeSession(long j, long j2);

    private static native long createTrainingSession(long j, long j2, long j3, long j4, long j5, String str, String str2, String str3);

    private native OnnxValue[] evalStep(long j, long j2, long j3, long j4, String[] strArr, long[] jArr, long j5, String[] strArr2, long j6, long j7) throws OrtException;

    private native void exportModelForInference(long j, long j2, long j3, String str, long j4, String[] strArr) throws OrtException;

    private native String[] getEvalInputNames(long j, long j2, long j3, long j4) throws OrtException;

    private native String[] getEvalOutputNames(long j, long j2, long j3, long j4) throws OrtException;

    private native float getLearningRate(long j, long j2, long j3);

    private native String[] getTrainInputNames(long j, long j2, long j3, long j4) throws OrtException;

    private native String[] getTrainOutputNames(long j, long j2, long j3, long j4) throws OrtException;

    private native void lazyResetGrad(long j, long j2, long j3) throws OrtException;

    private native void optimizerStep(long j, long j2, long j3, long j4) throws OrtException;

    private native void registerLinearLRScheduler(long j, long j2, long j3, long j4, long j5, float f) throws OrtException;

    private native void schedulerStep(long j, long j2, long j3) throws OrtException;

    private native void setLearningRate(long j, long j2, long j3, float f) throws OrtException;

    private static native void setSeed(long j, long j2, long j3) throws OrtException;

    private native OnnxValue[] trainStep(long j, long j2, long j3, long j4, String[] strArr, long[] jArr, long j5, String[] strArr2, long j6, long j7);

    static {
        try {
            OnnxRuntime.init();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load onnx-runtime library", e);
        }
    }

    OrtTrainingSession(OrtEnvironment ortEnvironment, OrtAllocator ortAllocator, OrtSession.SessionOptions sessionOptions, OrtCheckpointState ortCheckpointState, String str, String str2, String str3) throws OrtException {
        this(createTrainingSession(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, ortEnvironment.getNativeHandle(), sessionOptions.getNativeHandle(), ortCheckpointState.nativeHandle, str, str2, str3), ortAllocator, ortCheckpointState, str, str2, str3);
    }

    private OrtTrainingSession(long j, OrtAllocator ortAllocator, OrtCheckpointState ortCheckpointState, String str, String str2, String str3) throws OrtException {
        this.closed = false;
        this.nativeHandle = j;
        this.allocator = ortAllocator;
        this.checkpoint = ortCheckpointState;
        this.trainPath = str;
        this.evalPath = str2;
        this.optimizerPath = str3;
        this.trainInputNames = Collections.unmodifiableSet(new LinkedHashSet(Arrays.asList(getTrainInputNames(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, j, ortAllocator.handle))));
        this.trainOutputNames = Collections.unmodifiableSet(new LinkedHashSet(Arrays.asList(getTrainOutputNames(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, j, ortAllocator.handle))));
        this.evalInputNames = Collections.unmodifiableSet(new LinkedHashSet(Arrays.asList(getEvalInputNames(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, j, ortAllocator.handle))));
        this.evalOutputNames = Collections.unmodifiableSet(new LinkedHashSet(Arrays.asList(getEvalOutputNames(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, j, ortAllocator.handle))));
    }

    public Set getTrainInputNames() {
        return this.trainInputNames;
    }

    public Set getTrainOutputNames() {
        return this.trainOutputNames;
    }

    public Set getEvalInputNames() {
        return this.evalInputNames;
    }

    public Set getEvalOutputNames() {
        return this.evalOutputNames;
    }

    public void addProperty(String str, float f) throws OrtException {
        this.checkpoint.addProperty(str, f);
    }

    public void addProperty(String str, int i) throws OrtException {
        this.checkpoint.addProperty(str, i);
    }

    public void addProperty(String str, String str2) throws OrtException {
        this.checkpoint.addProperty(str, str2);
    }

    public float getFloatProperty(String str) throws OrtException {
        return this.checkpoint.getFloatProperty(this.allocator, str);
    }

    public int getIntProperty(String str) throws OrtException {
        return this.checkpoint.getIntProperty(this.allocator, str);
    }

    public String getStringProperty(String str) throws OrtException {
        return this.checkpoint.getStringProperty(this.allocator, str);
    }

    private void checkClosed() {
        if (this.closed) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to use a closed OrtTrainingSession");
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        if (!this.closed) {
            closeSession(OnnxRuntime.ortTrainingApiHandle, this.nativeHandle);
            this.checkpoint.close();
            this.closed = true;
            return;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to close an already closed OrtSession.");
    }

    public void saveCheckpoint(Path path, boolean z) throws OrtException {
        checkClosed();
        this.checkpoint.saveCheckpoint(path, z);
    }

    public void lazyResetGrad() throws OrtException {
        checkClosed();
        lazyResetGrad(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle);
    }

    public static void setSeed(long j) throws OrtException {
        setSeed(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, j);
    }

    public OrtSession.Result trainStep(Map map) throws OrtException {
        return trainStep(map, this.trainOutputNames, null);
    }

    public OrtSession.Result trainStep(Map map, OrtSession.RunOptions runOptions) throws OrtException {
        return trainStep(map, this.trainOutputNames, runOptions);
    }

    public OrtSession.Result trainStep(Map map, Set set) throws OrtException {
        return trainStep(map, set, null);
    }

    public OrtSession.Result trainStep(Map map, Set set, OrtSession.RunOptions runOptions) throws OrtException {
        checkClosed();
        if ((map.isEmpty() && this.trainInputNames.size() != 0) || map.size() > this.trainInputNames.size()) {
            throw new OrtException("Unexpected number of inputs, expected [1," + this.trainInputNames.size() + ") found " + map.size());
        }
        if (set.isEmpty() || set.size() > this.trainOutputNames.size()) {
            throw new OrtException("Unexpected number of requestedOutputs, expected [1," + this.trainOutputNames.size() + ") found " + set.size());
        }
        int size = map.size();
        String[] strArr = new String[size];
        long[] jArr = new long[map.size()];
        int i = 0;
        int i2 = 0;
        for (Map.Entry entry : map.entrySet()) {
            if (this.trainInputNames.contains(entry.getKey())) {
                strArr[i2] = (String) entry.getKey();
                jArr[i2] = ((OnnxTensorLike) entry.getValue()).getNativeHandle();
                i2++;
            } else {
                throw new OrtException("Unknown input name " + ((String) entry.getKey()) + ", expected one of " + this.trainInputNames);
            }
        }
        int size2 = set.size();
        String[] strArr2 = new String[size2];
        Iterator it = set.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (this.trainOutputNames.contains(str)) {
                strArr2[i] = str;
                i++;
            } else {
                throw new OrtException("Unknown output name " + str + ", expected one of " + this.trainOutputNames.toString());
            }
        }
        return new OrtSession.Result(strArr2, trainStep(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, this.allocator.handle, strArr, jArr, size, strArr2, size2, runOptions == null ? 0L : runOptions.getNativeHandle()));
    }

    public OrtSession.Result evalStep(Map map) throws OrtException {
        return evalStep(map, this.evalOutputNames, null);
    }

    public OrtSession.Result evalStep(Map map, OrtSession.RunOptions runOptions) throws OrtException {
        return evalStep(map, this.evalOutputNames, runOptions);
    }

    public OrtSession.Result evalStep(Map map, Set set) throws OrtException {
        return evalStep(map, set, null);
    }

    public OrtSession.Result evalStep(Map map, Set set, OrtSession.RunOptions runOptions) throws OrtException {
        checkClosed();
        if ((map.isEmpty() && this.evalInputNames.size() != 0) || map.size() > this.evalInputNames.size()) {
            throw new OrtException("Unexpected number of inputs, expected [1," + this.evalInputNames.size() + ") found " + map.size());
        }
        if (set.isEmpty() || set.size() > this.evalOutputNames.size()) {
            throw new OrtException("Unexpected number of requestedOutputs, expected [1," + this.evalOutputNames.size() + ") found " + set.size());
        }
        int size = map.size();
        String[] strArr = new String[size];
        long[] jArr = new long[map.size()];
        int i = 0;
        int i2 = 0;
        for (Map.Entry entry : map.entrySet()) {
            if (this.evalInputNames.contains(entry.getKey())) {
                strArr[i2] = (String) entry.getKey();
                jArr[i2] = ((OnnxTensorLike) entry.getValue()).getNativeHandle();
                i2++;
            } else {
                throw new OrtException("Unknown input name " + ((String) entry.getKey()) + ", expected one of " + this.evalInputNames.toString());
            }
        }
        int size2 = set.size();
        String[] strArr2 = new String[size2];
        Iterator it = set.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (this.evalOutputNames.contains(str)) {
                strArr2[i] = str;
                i++;
            } else {
                throw new OrtException("Unknown output name " + str + ", expected one of " + this.evalOutputNames.toString());
            }
        }
        return new OrtSession.Result(strArr2, evalStep(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, this.allocator.handle, strArr, jArr, size, strArr2, size2, runOptions == null ? 0L : runOptions.getNativeHandle()));
    }

    public void setLearningRate(float f) throws OrtException {
        checkClosed();
        setLearningRate(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, f);
    }

    public float getLearningRate() throws OrtException {
        checkClosed();
        return getLearningRate(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle);
    }

    public void optimizerStep() throws OrtException {
        optimizerStep(null);
    }

    public void optimizerStep(OrtSession.RunOptions runOptions) throws OrtException {
        checkClosed();
        optimizerStep(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, runOptions == null ? 0L : runOptions.getNativeHandle());
    }

    public void registerLinearLRScheduler(long j, long j2, float f) throws OrtException {
        registerLinearLRScheduler(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, j, j2, f);
    }

    public void schedulerStep() throws OrtException {
        checkClosed();
        schedulerStep(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle);
    }

    public void exportModelForInference(Path path, String[] strArr) throws OrtException {
        checkClosed();
        if (strArr.length == 0) {
            Folme$$ExternalSyntheticBUOutline0.m("Requires at least one output name");
        } else {
            exportModelForInference(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, path.toString(), strArr.length, strArr);
        }
    }

    final class OrtCheckpointState implements AutoCloseable {
        final long nativeHandle;

        private native void addProperty(long j, long j2, long j3, String str, float f) throws OrtException;

        private native void addProperty(long j, long j2, long j3, String str, int i) throws OrtException;

        private native void addProperty(long j, long j2, long j3, String str, String str2) throws OrtException;

        private native void close(long j, long j2);

        private native float getFloatProperty(long j, long j2, long j3, long j4, String str) throws OrtException;

        private native int getIntProperty(long j, long j2, long j3, long j4, String str) throws OrtException;

        private native String getStringProperty(long j, long j2, long j3, long j4, String str) throws OrtException;

        private static native long loadCheckpoint(long j, long j2, String str) throws OrtException;

        private native void saveCheckpoint(long j, long j2, long j3, String str, boolean z) throws OrtException;

        OrtCheckpointState(long j) {
            this.nativeHandle = j;
        }

        static OrtCheckpointState loadCheckpoint(Path path) throws OrtException {
            return loadCheckpoint(path.toString());
        }

        static OrtCheckpointState loadCheckpoint(String str) throws OrtException {
            if (OnnxRuntime.trainingEnabled) {
                Objects.requireNonNull(str, "checkpoint path must not be null");
                return new OrtCheckpointState(loadCheckpoint(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, str));
            }
            OrtUtil$$ExternalSyntheticBUOutline0.m("Training is not enabled in this build of ONNX Runtime.");
            return null;
        }

        public void saveCheckpoint(Path path, boolean z) throws OrtException {
            Objects.requireNonNull(path, "checkpoint path must not be null");
            saveCheckpoint(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, path.toString(), z);
        }

        public void addProperty(String str, float f) throws OrtException {
            addProperty(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, str, f);
        }

        public void addProperty(String str, int i) throws OrtException {
            addProperty(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, str, i);
        }

        public void addProperty(String str, String str2) throws OrtException {
            addProperty(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, str, str2);
        }

        public float getFloatProperty(OrtAllocator ortAllocator, String str) throws OrtException {
            return getFloatProperty(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, ortAllocator.handle, str);
        }

        public int getIntProperty(OrtAllocator ortAllocator, String str) throws OrtException {
            return getIntProperty(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, ortAllocator.handle, str);
        }

        public String getStringProperty(OrtAllocator ortAllocator, String str) throws OrtException {
            return getStringProperty(OnnxRuntime.ortApiHandle, OnnxRuntime.ortTrainingApiHandle, this.nativeHandle, ortAllocator.handle, str);
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            close(OnnxRuntime.ortTrainingApiHandle, this.nativeHandle);
        }
    }
}
