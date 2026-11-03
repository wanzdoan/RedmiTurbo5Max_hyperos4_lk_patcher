package org.tensorflow.lite.nnapi;

import org.tensorflow.lite.Delegate;
import org.tensorflow.lite.InterpreterFactoryApi;
import org.tensorflow.lite.TensorFlowLite;

/* JADX INFO: loaded from: classes.dex */
public class NnApiDelegate implements Delegate, AutoCloseable {
    private PrivateInterface impl;
    private boolean initialized;
    private Options options;

    public interface PrivateInterface extends Delegate, AutoCloseable {
        @Override // org.tensorflow.lite.Delegate, java.io.Closeable, java.lang.AutoCloseable
        void close();

        int getNnapiErrno();
    }

    public static final class Options {
        public static final int EXECUTION_PREFERENCE_FAST_SINGLE_ANSWER = 1;
        public static final int EXECUTION_PREFERENCE_LOW_POWER = 0;
        public static final int EXECUTION_PREFERENCE_SUSTAINED_SPEED = 2;
        public static final int EXECUTION_PREFERENCE_UNDEFINED = -1;
        private int executionPreference = -1;
        private String acceleratorName = null;
        private String cacheDir = null;
        private String modelToken = null;
        private Integer maxDelegatedPartitions = null;
        private Boolean useNnapiCpu = null;
        private Boolean allowFp16 = null;
        private long nnApiSupportLibraryHandle = 0;

        public Options setExecutionPreference(int preference) {
            this.executionPreference = preference;
            return this;
        }

        public Options setAcceleratorName(String name) {
            this.acceleratorName = name;
            return this;
        }

        public Options setCacheDir(String cacheDir) {
            this.cacheDir = cacheDir;
            return this;
        }

        public Options setModelToken(String modelToken) {
            this.modelToken = modelToken;
            return this;
        }

        public Options setMaxNumberOfDelegatedPartitions(int limit) {
            this.maxDelegatedPartitions = Integer.valueOf(limit);
            return this;
        }

        public Options setUseNnapiCpu(boolean enable) {
            this.useNnapiCpu = Boolean.valueOf(enable);
            return this;
        }

        public Options setAllowFp16(boolean enable) {
            this.allowFp16 = Boolean.valueOf(enable);
            return this;
        }

        public Options setNnApiSupportLibraryHandle(long handle) {
            this.nnApiSupportLibraryHandle = handle;
            return this;
        }

        public int getExecutionPreference() {
            return this.executionPreference;
        }

        public String getAcceleratorName() {
            return this.acceleratorName;
        }

        public String getCacheDir() {
            return this.cacheDir;
        }

        public String getModelToken() {
            return this.modelToken;
        }

        public int getMaxNumberOfDelegatedPartitions() {
            if (this.maxDelegatedPartitions == null) {
                return -1;
            }
            return this.maxDelegatedPartitions.intValue();
        }

        public Boolean getUseNnapiCpu() {
            return this.useNnapiCpu;
        }

        public boolean getAllowFp16() {
            return this.allowFp16 != null && this.allowFp16.booleanValue();
        }

        public long getNnApiSupportLibraryHandle() {
            return this.nnApiSupportLibraryHandle;
        }
    }

    public NnApiDelegate(Options options) {
        TensorFlowLite.init();
        this.options = options;
    }

    public NnApiDelegate() {
        this(new Options());
    }

    public void initWithInterpreterFactoryApi(InterpreterFactoryApi interpreterFactoryApi) {
        this.impl = interpreterFactoryApi.createNnApiDelegateImpl(this.options);
        this.initialized = true;
    }

    @Override // org.tensorflow.lite.Delegate
    public long getNativeHandle() {
        checkNotClosed();
        return this.impl.getNativeHandle();
    }

    @Override // org.tensorflow.lite.Delegate, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.impl != null) {
            this.impl.close();
            this.impl = null;
        }
    }

    public int getNnapiErrno() {
        if (!this.initialized) {
            return 0;
        }
        checkNotClosed();
        return this.impl.getNnapiErrno();
    }

    public boolean hasErrors() {
        return getNnapiErrno() != 0;
    }

    private void checkNotClosed() {
        String str;
        if (this.impl == null) {
            if (this.initialized) {
                str = "Should not access delegate after delegate has been closed.";
            } else {
                str = "Should not access delegate before interpreter has been constructed.";
            }
            throw new IllegalStateException(str);
        }
    }
}
