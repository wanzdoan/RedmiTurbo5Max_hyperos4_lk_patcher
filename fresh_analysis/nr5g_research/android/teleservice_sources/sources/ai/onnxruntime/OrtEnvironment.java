package ai.onnxruntime;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Objects;
import java.util.logging.Logger;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public final class OrtEnvironment implements AutoCloseable {
    public static final String DEFAULT_NAME = "ort-java";
    private static volatile OrtEnvironment INSTANCE;
    private static volatile OrtLoggingLevel curLogLevel;
    private static volatile String curLoggingName;
    private static final Logger logger = Logger.getLogger(OrtEnvironment.class.getName());
    final OrtAllocator defaultAllocator;
    final long nativeHandle;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void close(long j, long j2) throws OrtException;

    private static native long createHandle(long j, int i, String str) throws OrtException;

    private static native long createHandle(long j, int i, String str, long j2) throws OrtException;

    private static native long getDefaultAllocator(long j) throws OrtException;

    private static native void setTelemetry(long j, long j2, boolean z) throws OrtException;

    @Override // java.lang.AutoCloseable
    public void close() {
    }

    static {
        try {
            OnnxRuntime.init();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load onnx-runtime library", e);
        }
    }

    public static synchronized OrtEnvironment getEnvironment() {
        if (INSTANCE == null) {
            return getEnvironment(OrtLoggingLevel.ORT_LOGGING_LEVEL_WARNING, DEFAULT_NAME);
        }
        return INSTANCE;
    }

    public static OrtEnvironment getEnvironment(String str) {
        return getEnvironment(OrtLoggingLevel.ORT_LOGGING_LEVEL_WARNING, str);
    }

    public static OrtEnvironment getEnvironment(OrtLoggingLevel ortLoggingLevel) {
        return getEnvironment(ortLoggingLevel, DEFAULT_NAME);
    }

    public static synchronized OrtEnvironment getEnvironment(OrtLoggingLevel ortLoggingLevel, String str) {
        if (INSTANCE == null) {
            try {
                INSTANCE = new OrtEnvironment(ortLoggingLevel, str);
                curLogLevel = ortLoggingLevel;
                curLoggingName = str;
            } catch (OrtException e) {
                throw new IllegalStateException("Failed to create OrtEnvironment", e);
            }
        } else if (ortLoggingLevel.getValue() != curLogLevel.getValue() || !str.equals(curLoggingName)) {
            logger.warning("Tried to change OrtEnvironment's logging level or name while a reference exists.");
        }
        return INSTANCE;
    }

    public static synchronized OrtEnvironment getEnvironment(OrtLoggingLevel ortLoggingLevel, String str, ThreadingOptions threadingOptions) {
        if (INSTANCE == null) {
            try {
                INSTANCE = new OrtEnvironment(ortLoggingLevel, str, threadingOptions);
                curLogLevel = ortLoggingLevel;
                curLoggingName = str;
            } catch (OrtException e) {
                throw new IllegalStateException("Failed to create OrtEnvironment", e);
            }
        } else {
            throw new IllegalStateException("Tried to specify the thread pool when creating an OrtEnvironment, but one already exists.");
        }
        return INSTANCE;
    }

    private OrtEnvironment() throws OrtException {
        this(OrtLoggingLevel.ORT_LOGGING_LEVEL_WARNING, "java-default");
    }

    private OrtEnvironment(OrtLoggingLevel ortLoggingLevel, String str) throws OrtException {
        long jCreateHandle = createHandle(OnnxRuntime.ortApiHandle, ortLoggingLevel.getValue(), str);
        this.nativeHandle = jCreateHandle;
        this.defaultAllocator = new OrtAllocator(getDefaultAllocator(OnnxRuntime.ortApiHandle), true);
        Runtime.getRuntime().addShutdownHook(new Thread(new OrtEnvCloser(OnnxRuntime.ortApiHandle, jCreateHandle)));
    }

    private OrtEnvironment(OrtLoggingLevel ortLoggingLevel, String str, ThreadingOptions threadingOptions) throws OrtException {
        long jCreateHandle = createHandle(OnnxRuntime.ortApiHandle, ortLoggingLevel.getValue(), str, threadingOptions.nativeHandle);
        this.nativeHandle = jCreateHandle;
        this.defaultAllocator = new OrtAllocator(getDefaultAllocator(OnnxRuntime.ortApiHandle), true);
        Runtime.getRuntime().addShutdownHook(new Thread(new OrtEnvCloser(OnnxRuntime.ortApiHandle, jCreateHandle)));
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    public OrtSession createSession(String str) throws OrtException {
        return createSession(str, new OrtSession.SessionOptions());
    }

    public OrtSession createSession(String str, OrtSession.SessionOptions sessionOptions) throws OrtException {
        return createSession(str, this.defaultAllocator, sessionOptions);
    }

    OrtSession createSession(String str, OrtAllocator ortAllocator, OrtSession.SessionOptions sessionOptions) throws OrtException {
        Objects.requireNonNull(str, "model path must not be null");
        return new OrtSession(this, str, ortAllocator, sessionOptions);
    }

    public OrtSession createSession(byte[] bArr, OrtSession.SessionOptions sessionOptions) throws OrtException {
        return createSession(bArr, this.defaultAllocator, sessionOptions);
    }

    public OrtSession createSession(byte[] bArr) throws OrtException {
        return createSession(bArr, new OrtSession.SessionOptions());
    }

    OrtSession createSession(byte[] bArr, OrtAllocator ortAllocator, OrtSession.SessionOptions sessionOptions) throws OrtException {
        Objects.requireNonNull(bArr, "model array must not be null");
        return new OrtSession(this, bArr, ortAllocator, sessionOptions);
    }

    public OrtTrainingSession createTrainingSession(String str, String str2, String str3, String str4) throws OrtException {
        return createTrainingSession(str, str2, str3, str4, new OrtSession.SessionOptions());
    }

    public OrtTrainingSession createTrainingSession(String str, String str2, String str3, String str4, OrtSession.SessionOptions sessionOptions) throws OrtException {
        return createTrainingSession(str, str2, str3, str4, this.defaultAllocator, sessionOptions);
    }

    OrtTrainingSession createTrainingSession(String str, String str2, String str3, String str4, OrtAllocator ortAllocator, OrtSession.SessionOptions sessionOptions) throws OrtException {
        if (OnnxRuntime.trainingEnabled) {
            Objects.requireNonNull(str2, "train path must not be null");
            return new OrtTrainingSession(this, ortAllocator, sessionOptions, OrtTrainingSession.OrtCheckpointState.loadCheckpoint(str), str2, str3, str4);
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("Training is not enabled in this build of ONNX Runtime.");
        return null;
    }

    public boolean isTrainingEnabled() {
        return OnnxRuntime.trainingEnabled;
    }

    public void setTelemetry(boolean z) throws OrtException {
        setTelemetry(OnnxRuntime.ortApiHandle, this.nativeHandle, z);
    }

    public String getVersion() {
        return OnnxRuntime.version();
    }

    public String toString() {
        return "OrtEnvironment(name=" + curLoggingName + ",logLevel=" + curLogLevel + ",version=" + getVersion() + ")";
    }

    public static EnumSet getAvailableProviders() {
        return OnnxRuntime.providers.clone();
    }

    public final class ThreadingOptions implements AutoCloseable {
        private boolean closed = false;
        private final long nativeHandle = createThreadingOptions(OnnxRuntime.ortApiHandle);

        private native void closeThreadingOptions(long j, long j2);

        private static native long createThreadingOptions(long j);

        private native void setGlobalDenormalAsZero(long j, long j2) throws OrtException;

        private native void setGlobalInterOpNumThreads(long j, long j2, int i) throws OrtException;

        private native void setGlobalIntraOpNumThreads(long j, long j2, int i) throws OrtException;

        private native void setGlobalSpinControl(long j, long j2, int i) throws OrtException;

        static {
            try {
                OnnxRuntime.init();
            } catch (IOException e) {
                throw new RuntimeException("Failed to load onnx-runtime library", e);
            }
        }

        private void checkClosed() {
            if (this.closed) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to use a closed ThreadingOptions");
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            if (!this.closed) {
                closeThreadingOptions(OnnxRuntime.ortApiHandle, this.nativeHandle);
                this.closed = true;
            } else {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Trying to close a closed ThreadingOptions.");
            }
        }

        public void setGlobalInterOpNumThreads(int i) throws OrtException {
            checkClosed();
            if (i < 0) {
                Folme$$ExternalSyntheticBUOutline0.m("Number of threads must be non-negative.");
            } else {
                setGlobalInterOpNumThreads(OnnxRuntime.ortApiHandle, this.nativeHandle, i);
            }
        }

        public void setGlobalIntraOpNumThreads(int i) throws OrtException {
            checkClosed();
            if (i < 0) {
                Folme$$ExternalSyntheticBUOutline0.m("Number of threads must be non-negative.");
            } else {
                setGlobalIntraOpNumThreads(OnnxRuntime.ortApiHandle, this.nativeHandle, i);
            }
        }

        public void setGlobalSpinControl(boolean z) throws OrtException {
            checkClosed();
            setGlobalSpinControl(OnnxRuntime.ortApiHandle, this.nativeHandle, z ? 1 : 0);
        }

        public void setGlobalDenormalAsZero() throws OrtException {
            checkClosed();
            setGlobalDenormalAsZero(OnnxRuntime.ortApiHandle, this.nativeHandle);
        }
    }

    final class OrtEnvCloser implements Runnable {
        private final long apiHandle;
        private final long nativeHandle;

        OrtEnvCloser(long j, long j2) {
            this.apiHandle = j;
            this.nativeHandle = j2;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                OrtEnvironment.close(this.apiHandle, this.nativeHandle);
            } catch (OrtException e) {
                System.err.println("Error closing OrtEnvironment, " + e);
            }
        }
    }
}
