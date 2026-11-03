package ai.onnxruntime;

import ai.onnxruntime.providers.CoreMLFlags;
import ai.onnxruntime.providers.NNAPIFlags;
import ai.onnxruntime.providers.OrtCUDAProviderOptions;
import ai.onnxruntime.providers.OrtFlags;
import ai.onnxruntime.providers.OrtTensorRTProviderOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
public class OrtSession implements AutoCloseable {
    private final OrtAllocator allocator;
    private boolean closed;
    private final Set inputNames;
    private OnnxModelMetadata metadata;
    private final long nativeHandle;
    private final long numInputs;
    private final long numOutputs;
    private final Set outputNames;

    private native void closeSession(long j, long j2) throws OrtException;

    private native OnnxModelMetadata constructMetadata(long j, long j2, long j3) throws OrtException;

    private static native long createSession(long j, long j2, String str, long j3) throws OrtException;

    private static native long createSession(long j, long j2, byte[] bArr, long j3) throws OrtException;

    private native String endProfiling(long j, long j2, long j3) throws OrtException;

    private native NodeInfo[] getInputInfo(long j, long j2, long j3) throws OrtException;

    private native String[] getInputNames(long j, long j2, long j3) throws OrtException;

    private native long getNumInputs(long j, long j2) throws OrtException;

    private native long getNumOutputs(long j, long j2) throws OrtException;

    private native NodeInfo[] getOutputInfo(long j, long j2, long j3) throws OrtException;

    private native String[] getOutputNames(long j, long j2, long j3) throws OrtException;

    private native long getProfilingStartTimeInNs(long j, long j2) throws OrtException;

    private native OnnxValue[] run(long j, long j2, long j3, String[] strArr, long[] jArr, long j4, String[] strArr2, long j5, long j6) throws OrtException;

    static {
        try {
            OnnxRuntime.init();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load onnx-runtime library", e);
        }
    }

    OrtSession(OrtEnvironment ortEnvironment, String str, OrtAllocator ortAllocator, SessionOptions sessionOptions) throws OrtException {
        this(createSession(OnnxRuntime.ortApiHandle, ortEnvironment.getNativeHandle(), str, sessionOptions.getNativeHandle()), ortAllocator);
    }

    OrtSession(OrtEnvironment ortEnvironment, byte[] bArr, OrtAllocator ortAllocator, SessionOptions sessionOptions) throws OrtException {
        this(createSession(OnnxRuntime.ortApiHandle, ortEnvironment.getNativeHandle(), bArr, sessionOptions.getNativeHandle()), ortAllocator);
    }

    private OrtSession(long j, OrtAllocator ortAllocator) throws OrtException {
        this.closed = false;
        this.nativeHandle = j;
        this.allocator = ortAllocator;
        this.numInputs = getNumInputs(OnnxRuntime.ortApiHandle, j);
        this.inputNames = new LinkedHashSet(Arrays.asList(getInputNames(OnnxRuntime.ortApiHandle, j, ortAllocator.handle)));
        this.numOutputs = getNumOutputs(OnnxRuntime.ortApiHandle, j);
        this.outputNames = new LinkedHashSet(Arrays.asList(getOutputNames(OnnxRuntime.ortApiHandle, j, ortAllocator.handle)));
    }

    public long getNumInputs() {
        if (!this.closed) {
            return this.numInputs;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Asking for inputs from a closed OrtSession.");
        return 0L;
    }

    public long getNumOutputs() {
        if (!this.closed) {
            return this.numOutputs;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Asking for outputs from a closed OrtSession.");
        return 0L;
    }

    public Set getInputNames() {
        if (!this.closed) {
            return this.inputNames;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Asking for inputs from a closed OrtSession.");
        return null;
    }

    public Set getOutputNames() {
        if (!this.closed) {
            return this.outputNames;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Asking for outputs from a closed OrtSession.");
        return null;
    }

    public Map getInputInfo() throws OrtException {
        if (!this.closed) {
            return wrapInMap(getInputInfo(OnnxRuntime.ortApiHandle, this.nativeHandle, this.allocator.handle));
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Asking for inputs from a closed OrtSession.");
        return null;
    }

    public Map getOutputInfo() throws OrtException {
        if (!this.closed) {
            return wrapInMap(getOutputInfo(OnnxRuntime.ortApiHandle, this.nativeHandle, this.allocator.handle));
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Asking for outputs from a closed OrtSession.");
        return null;
    }

    public Result run(Map map) throws OrtException {
        return run(map, this.outputNames);
    }

    public Result run(Map map, RunOptions runOptions) throws OrtException {
        return run(map, this.outputNames, runOptions);
    }

    public Result run(Map map, Set set) throws OrtException {
        return run(map, set, null);
    }

    public Result run(Map map, Set set, RunOptions runOptions) throws OrtException {
        if (!this.closed) {
            if ((map.isEmpty() && this.numInputs != 0) || map.size() > this.numInputs) {
                throw new OrtException("Unexpected number of inputs, expected [1," + this.numInputs + ") found " + map.size());
            }
            if (set.isEmpty() || set.size() > this.numOutputs) {
                throw new OrtException("Unexpected number of requestedOutputs, expected [1," + this.numOutputs + ") found " + set.size());
            }
            int size = map.size();
            String[] strArr = new String[size];
            long[] jArr = new long[map.size()];
            int i = 0;
            int i2 = 0;
            for (Map.Entry entry : map.entrySet()) {
                if (this.inputNames.contains(entry.getKey())) {
                    strArr[i2] = (String) entry.getKey();
                    jArr[i2] = ((OnnxTensorLike) entry.getValue()).getNativeHandle();
                    i2++;
                } else {
                    throw new OrtException("Unknown input name " + ((String) entry.getKey()) + ", expected one of " + this.inputNames.toString());
                }
            }
            int size2 = set.size();
            String[] strArr2 = new String[size2];
            Iterator it = set.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (this.outputNames.contains(str)) {
                    strArr2[i] = str;
                    i++;
                } else {
                    throw new OrtException("Unknown output name " + str + ", expected one of " + this.outputNames.toString());
                }
            }
            return new Result(strArr2, run(OnnxRuntime.ortApiHandle, this.nativeHandle, this.allocator.handle, strArr, jArr, size, strArr2, size2, runOptions != null ? runOptions.getNativeHandle() : 0L));
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to score a closed OrtSession.");
        return null;
    }

    public OnnxModelMetadata getMetadata() throws OrtException {
        OrtSession ortSession;
        if (this.metadata == null) {
            ortSession = this;
            ortSession.metadata = ortSession.constructMetadata(OnnxRuntime.ortApiHandle, this.nativeHandle, this.allocator.handle);
        } else {
            ortSession = this;
        }
        return ortSession.metadata;
    }

    public long getProfilingStartTimeInNs() throws OrtException {
        return getProfilingStartTimeInNs(OnnxRuntime.ortApiHandle, this.nativeHandle);
    }

    public String endProfiling() throws OrtException {
        return endProfiling(OnnxRuntime.ortApiHandle, this.nativeHandle, this.allocator.handle);
    }

    public String toString() {
        return "OrtSession(numInputs=" + this.numInputs + ",numOutputs=" + this.numOutputs + ")";
    }

    @Override // java.lang.AutoCloseable
    public void close() throws OrtException {
        if (!this.closed) {
            closeSession(OnnxRuntime.ortApiHandle, this.nativeHandle);
            this.closed = true;
        } else {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to close an already closed OrtSession.");
        }
    }

    private static Map wrapInMap(NodeInfo[] nodeInfoArr) {
        LinkedHashMap linkedHashMap = new LinkedHashMap(OrtUtil.capacityFromSize(nodeInfoArr.length));
        for (NodeInfo nodeInfo : nodeInfoArr) {
            linkedHashMap.put(nodeInfo.getName(), nodeInfo);
        }
        return linkedHashMap;
    }

    public class SessionOptions implements AutoCloseable {
        private boolean closed = false;
        private final long nativeHandle = createOptions(OnnxRuntime.ortApiHandle);
        private final List customLibraryHandles = new ArrayList();
        private final Map configEntries = new LinkedHashMap();

        private native void addACL(long j, long j2, int i) throws OrtException;

        private native void addArmNN(long j, long j2, int i) throws OrtException;

        private native void addCPU(long j, long j2, int i) throws OrtException;

        private native void addCUDA(long j, long j2, int i) throws OrtException;

        private native void addCUDAV2(long j, long j2, long j3) throws OrtException;

        private native void addConfigEntry(long j, long j2, String str, String str2) throws OrtException;

        private native void addCoreML(long j, long j2, int i) throws OrtException;

        private native void addDirectML(long j, long j2, int i) throws OrtException;

        private native void addDnnl(long j, long j2, int i) throws OrtException;

        private native void addExecutionProvider(long j, long j2, String str, String[] strArr, String[] strArr2) throws OrtException;

        private native void addFreeDimensionOverrideByName(long j, long j2, String str, long j3) throws OrtException;

        private native void addNnapi(long j, long j2, int i) throws OrtException;

        private native void addOpenVINO(long j, long j2, String str) throws OrtException;

        private native void addROCM(long j, long j2, int i) throws OrtException;

        private native void addTensorrt(long j, long j2, int i) throws OrtException;

        private native void addTensorrtV2(long j, long j2, long j3) throws OrtException;

        private native void addTvm(long j, long j2, String str) throws OrtException;

        private native void closeCustomLibraries(long[] jArr);

        private native void closeOptions(long j, long j2);

        private native long createOptions(long j);

        private native void disablePerSessionThreads(long j, long j2) throws OrtException;

        private native void disableProfiling(long j, long j2) throws OrtException;

        private native void enableProfiling(long j, long j2, String str) throws OrtException;

        private native long registerCustomOpLibrary(long j, long j2, String str) throws OrtException;

        private native void registerCustomOpsUsingFunction(long j, long j2, String str) throws OrtException;

        private native void setCPUArenaAllocator(long j, long j2, boolean z) throws OrtException;

        private native void setExecutionMode(long j, long j2, int i) throws OrtException;

        private native void setInterOpNumThreads(long j, long j2, int i) throws OrtException;

        private native void setIntraOpNumThreads(long j, long j2, int i) throws OrtException;

        private native void setLoggerId(long j, long j2, String str) throws OrtException;

        private native void setMemoryPatternOptimization(long j, long j2, boolean z) throws OrtException;

        private native void setOptimizationLevel(long j, long j2, int i) throws OrtException;

        private native void setOptimizationModelFilePath(long j, long j2, String str) throws OrtException;

        private native void setSessionLogLevel(long j, long j2, int i) throws OrtException;

        private native void setSessionLogVerbosityLevel(long j, long j2, int i) throws OrtException;

        public enum OptLevel {
            NO_OPT(0),
            BASIC_OPT(1),
            EXTENDED_OPT(2),
            ALL_OPT(99);

            private final int id;

            OptLevel(int i) {
                this.id = i;
            }

            public int getID() {
                return this.id;
            }
        }

        public enum ExecutionMode {
            SEQUENTIAL(0),
            PARALLEL(1);

            private final int id;

            ExecutionMode(int i) {
                this.id = i;
            }

            public int getID() {
                return this.id;
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            if (!this.closed) {
                if (this.customLibraryHandles.size() > 0) {
                    long[] jArr = new long[this.customLibraryHandles.size()];
                    for (int i = 0; i < this.customLibraryHandles.size(); i++) {
                        jArr[i] = ((Long) this.customLibraryHandles.get(i)).longValue();
                    }
                    closeCustomLibraries(jArr);
                }
                closeOptions(OnnxRuntime.ortApiHandle, this.nativeHandle);
                this.closed = true;
                return;
            }
            OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to close a closed SessionOptions.");
        }

        private void checkClosed() {
            if (this.closed) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to use a closed SessionOptions");
            }
        }

        long getNativeHandle() {
            return this.nativeHandle;
        }

        public void setExecutionMode(ExecutionMode executionMode) throws OrtException {
            checkClosed();
            setExecutionMode(OnnxRuntime.ortApiHandle, this.nativeHandle, executionMode.getID());
        }

        public void setOptimizationLevel(OptLevel optLevel) throws OrtException {
            checkClosed();
            setOptimizationLevel(OnnxRuntime.ortApiHandle, this.nativeHandle, optLevel.getID());
        }

        public void setInterOpNumThreads(int i) throws OrtException {
            checkClosed();
            setInterOpNumThreads(OnnxRuntime.ortApiHandle, this.nativeHandle, i);
        }

        public void setIntraOpNumThreads(int i) throws OrtException {
            checkClosed();
            setIntraOpNumThreads(OnnxRuntime.ortApiHandle, this.nativeHandle, i);
        }

        public void setOptimizedModelFilePath(String str) throws OrtException {
            checkClosed();
            setOptimizationModelFilePath(OnnxRuntime.ortApiHandle, this.nativeHandle, str);
        }

        public void setLoggerId(String str) throws OrtException {
            checkClosed();
            setLoggerId(OnnxRuntime.ortApiHandle, this.nativeHandle, str);
        }

        public void enableProfiling(String str) throws OrtException {
            checkClosed();
            enableProfiling(OnnxRuntime.ortApiHandle, this.nativeHandle, str);
        }

        public void disableProfiling() throws OrtException {
            checkClosed();
            disableProfiling(OnnxRuntime.ortApiHandle, this.nativeHandle);
        }

        public void setMemoryPatternOptimization(boolean z) throws OrtException {
            checkClosed();
            setMemoryPatternOptimization(OnnxRuntime.ortApiHandle, this.nativeHandle, z);
        }

        public void setCPUArenaAllocator(boolean z) throws OrtException {
            checkClosed();
            setCPUArenaAllocator(OnnxRuntime.ortApiHandle, this.nativeHandle, z);
        }

        public void setSessionLogLevel(OrtLoggingLevel ortLoggingLevel) throws OrtException {
            checkClosed();
            setSessionLogLevel(OnnxRuntime.ortApiHandle, this.nativeHandle, ortLoggingLevel.getValue());
        }

        public void setSessionLogVerbosityLevel(int i) throws OrtException {
            checkClosed();
            setSessionLogVerbosityLevel(OnnxRuntime.ortApiHandle, this.nativeHandle, i);
        }

        public void registerCustomOpLibrary(String str) throws OrtException {
            checkClosed();
            Objects.requireNonNull(str, "path must not be null");
            this.customLibraryHandles.add(Long.valueOf(registerCustomOpLibrary(OnnxRuntime.ortApiHandle, this.nativeHandle, str)));
        }

        public void registerCustomOpsUsingFunction(String str) throws OrtException {
            checkClosed();
            registerCustomOpsUsingFunction(OnnxRuntime.ortApiHandle, this.nativeHandle, str);
        }

        public void setSymbolicDimensionValue(String str, long j) throws OrtException {
            checkClosed();
            addFreeDimensionOverrideByName(OnnxRuntime.ortApiHandle, this.nativeHandle, str, j);
        }

        public void disablePerSessionThreads() throws OrtException {
            checkClosed();
            disablePerSessionThreads(OnnxRuntime.ortApiHandle, this.nativeHandle);
        }

        public void addConfigEntry(String str, String str2) throws OrtException {
            checkClosed();
            addConfigEntry(OnnxRuntime.ortApiHandle, this.nativeHandle, str, str2);
            this.configEntries.put(str, str2);
        }

        public Map getConfigEntries() {
            checkClosed();
            return Collections.unmodifiableMap(this.configEntries);
        }

        public void addCUDA() throws OrtException {
            addCUDA(0);
        }

        public void addCUDA(int i) throws OrtException {
            checkClosed();
            if (OnnxRuntime.extractCUDA()) {
                addCUDA(OnnxRuntime.ortApiHandle, this.nativeHandle, i);
                return;
            }
            throw new OrtException(OrtException.OrtErrorCode.ORT_EP_FAIL, "Failed to find CUDA shared provider");
        }

        public void addCUDA(OrtCUDAProviderOptions ortCUDAProviderOptions) throws OrtException {
            checkClosed();
            if (OnnxRuntime.extractCUDA()) {
                addCUDAV2(OnnxRuntime.ortApiHandle, this.nativeHandle, ortCUDAProviderOptions.nativeHandle);
                return;
            }
            throw new OrtException(OrtException.OrtErrorCode.ORT_EP_FAIL, "Failed to find CUDA shared provider");
        }

        public void addROCM() throws OrtException {
            addROCM(0);
        }

        public void addROCM(int i) throws OrtException {
            checkClosed();
            if (OnnxRuntime.extractROCM()) {
                addROCM(OnnxRuntime.ortApiHandle, this.nativeHandle, i);
                return;
            }
            throw new OrtException(OrtException.OrtErrorCode.ORT_EP_FAIL, "Failed to find ROCM shared provider");
        }

        public void addCPU(boolean z) throws OrtException {
            checkClosed();
            addCPU(OnnxRuntime.ortApiHandle, this.nativeHandle, z ? 1 : 0);
        }

        public void addDnnl(boolean z) throws OrtException {
            checkClosed();
            if (OnnxRuntime.extractDNNL()) {
                addDnnl(OnnxRuntime.ortApiHandle, this.nativeHandle, z ? 1 : 0);
                return;
            }
            throw new OrtException(OrtException.OrtErrorCode.ORT_EP_FAIL, "Failed to find DNNL shared provider");
        }

        public void addOpenVINO(String str) throws OrtException {
            checkClosed();
            if (OnnxRuntime.extractOpenVINO()) {
                addOpenVINO(OnnxRuntime.ortApiHandle, this.nativeHandle, str);
                return;
            }
            throw new OrtException(OrtException.OrtErrorCode.ORT_EP_FAIL, "Failed to find OpenVINO shared provider");
        }

        public void addTensorrt(int i) throws OrtException {
            checkClosed();
            if (OnnxRuntime.extractTensorRT()) {
                addTensorrt(OnnxRuntime.ortApiHandle, this.nativeHandle, i);
                return;
            }
            throw new OrtException(OrtException.OrtErrorCode.ORT_EP_FAIL, "Failed to find TensorRT shared provider");
        }

        public void addTensorrt(OrtTensorRTProviderOptions ortTensorRTProviderOptions) throws OrtException {
            checkClosed();
            if (OnnxRuntime.extractTensorRT()) {
                addTensorrtV2(OnnxRuntime.ortApiHandle, this.nativeHandle, ortTensorRTProviderOptions.nativeHandle);
                return;
            }
            throw new OrtException(OrtException.OrtErrorCode.ORT_EP_FAIL, "Failed to find TensorRT shared provider");
        }

        public void addNnapi() throws OrtException {
            addNnapi(EnumSet.noneOf(NNAPIFlags.class));
        }

        public void addNnapi(EnumSet enumSet) throws OrtException {
            checkClosed();
            addNnapi(OnnxRuntime.ortApiHandle, this.nativeHandle, OrtFlags.aggregateToInt(enumSet));
        }

        public void addTvm(String str) throws OrtException {
            checkClosed();
            addTvm(OnnxRuntime.ortApiHandle, this.nativeHandle, str);
        }

        public void addDirectML(int i) throws OrtException {
            checkClosed();
            addDirectML(OnnxRuntime.ortApiHandle, this.nativeHandle, i);
        }

        public void addACL(boolean z) throws OrtException {
            checkClosed();
            addACL(OnnxRuntime.ortApiHandle, this.nativeHandle, z ? 1 : 0);
        }

        public void addArmNN(boolean z) throws OrtException {
            checkClosed();
            addArmNN(OnnxRuntime.ortApiHandle, this.nativeHandle, z ? 1 : 0);
        }

        public void addCoreML() throws OrtException {
            addCoreML(EnumSet.noneOf(CoreMLFlags.class));
        }

        public void addCoreML(EnumSet enumSet) throws OrtException {
            checkClosed();
            addCoreML(OnnxRuntime.ortApiHandle, this.nativeHandle, OrtFlags.aggregateToInt(enumSet));
        }

        public void addXnnpack(Map map) throws OrtException {
            checkClosed();
            String[] strArr = new String[map.size()];
            String[] strArr2 = new String[map.size()];
            int i = 0;
            for (Map.Entry entry : map.entrySet()) {
                strArr[i] = (String) entry.getKey();
                strArr2[i] = (String) entry.getValue();
                i++;
            }
            addExecutionProvider(OnnxRuntime.ortApiHandle, this.nativeHandle, "XNNPACK", strArr, strArr2);
        }
    }

    public class RunOptions implements AutoCloseable {
        private boolean closed = false;
        private final long nativeHandle = createRunOptions(OnnxRuntime.ortApiHandle);

        private native void addRunConfigEntry(long j, long j2, String str, String str2) throws OrtException;

        private static native void close(long j, long j2);

        private static native long createRunOptions(long j) throws OrtException;

        private native int getLogLevel(long j, long j2) throws OrtException;

        private native int getLogVerbosityLevel(long j, long j2) throws OrtException;

        private native String getRunTag(long j, long j2) throws OrtException;

        private native void setLogLevel(long j, long j2, int i) throws OrtException;

        private native void setLogVerbosityLevel(long j, long j2, int i) throws OrtException;

        private native void setRunTag(long j, long j2, String str) throws OrtException;

        private native void setTerminate(long j, long j2, boolean z) throws OrtException;

        long getNativeHandle() {
            return this.nativeHandle;
        }

        public void setLogLevel(OrtLoggingLevel ortLoggingLevel) throws OrtException {
            checkClosed();
            setLogLevel(OnnxRuntime.ortApiHandle, this.nativeHandle, ortLoggingLevel.getValue());
        }

        public OrtLoggingLevel getLogLevel() throws OrtException {
            checkClosed();
            return OrtLoggingLevel.mapFromInt(getLogLevel(OnnxRuntime.ortApiHandle, this.nativeHandle));
        }

        public void setLogVerbosityLevel(int i) throws OrtException {
            checkClosed();
            setLogVerbosityLevel(OnnxRuntime.ortApiHandle, this.nativeHandle, i);
        }

        public int getLogVerbosityLevel() throws OrtException {
            checkClosed();
            return getLogVerbosityLevel(OnnxRuntime.ortApiHandle, this.nativeHandle);
        }

        public void setRunTag(String str) throws OrtException {
            checkClosed();
            setRunTag(OnnxRuntime.ortApiHandle, this.nativeHandle, str);
        }

        public String getRunTag() throws OrtException {
            checkClosed();
            return getRunTag(OnnxRuntime.ortApiHandle, this.nativeHandle);
        }

        public void setTerminate(boolean z) throws OrtException {
            checkClosed();
            setTerminate(OnnxRuntime.ortApiHandle, this.nativeHandle, z);
        }

        public void addRunConfigEntry(String str, String str2) throws OrtException {
            checkClosed();
            addRunConfigEntry(OnnxRuntime.ortApiHandle, this.nativeHandle, str, str2);
        }

        private void checkClosed() {
            if (this.closed) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to use a closed RunOptions");
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            if (!this.closed) {
                close(OnnxRuntime.ortApiHandle, this.nativeHandle);
                this.closed = true;
            } else {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to close an already closed RunOptions");
            }
        }
    }

    public class Result implements AutoCloseable, Iterable {
        private static final Logger logger = Logger.getLogger(Result.class.getName());
        private boolean closed;
        private final List list;
        private final Map map;

        Result(String[] strArr, OnnxValue[] onnxValueArr) {
            if (strArr.length != onnxValueArr.length) {
                OrtSession$Result$$ExternalSyntheticBUOutline0.m("Expected same number of names and values, found names.length = ", strArr.length, ", values.length = ", onnxValueArr.length);
                throw null;
            }
            this.map = new LinkedHashMap(OrtUtil.capacityFromSize(strArr.length));
            this.list = new ArrayList(strArr.length);
            for (int i = 0; i < strArr.length; i++) {
                this.map.put(strArr[i], onnxValueArr[i]);
                this.list.add(onnxValueArr[i]);
            }
            this.closed = false;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            if (!this.closed) {
                this.closed = true;
                Iterator it = this.map.values().iterator();
                while (it.hasNext()) {
                    ((OnnxValue) it.next()).close();
                }
                return;
            }
            logger.warning("Closing an already closed Result");
        }

        @Override // java.lang.Iterable
        public Iterator iterator() {
            if (!this.closed) {
                return this.map.entrySet().iterator();
            }
            OrtUtil$$ExternalSyntheticBUOutline0.m("Result is closed");
            return null;
        }

        public OnnxValue get(int i) {
            if (!this.closed) {
                return (OnnxValue) this.list.get(i);
            }
            OrtUtil$$ExternalSyntheticBUOutline0.m("Result is closed");
            return null;
        }

        public int size() {
            return this.map.size();
        }

        public Optional get(String str) {
            if (!this.closed) {
                OnnxValue onnxValue = (OnnxValue) this.map.get(str);
                if (onnxValue != null) {
                    return Optional.of(onnxValue);
                }
                return Optional.empty();
            }
            OrtUtil$$ExternalSyntheticBUOutline0.m("Result is closed");
            return null;
        }
    }
}
