package org.tensorflow.lite.nnapi;

import org.tensorflow.lite.Delegate;
import org.tensorflow.lite.TensorFlowLite;

/* JADX INFO: loaded from: classes.dex */
public class NnApiDelegateImpl implements NnApiDelegate.PrivateInterface, Delegate, AutoCloseable {
    private static final long INVALID_DELEGATE_HANDLE = 0;
    private long delegateHandle;

    private static native long createDelegate(int i, String str, String str2, String str3, int i2, boolean z, boolean z2, boolean z3, long j);

    private static native void deleteDelegate(long j);

    private static native int getNnapiErrno(long j);

    public NnApiDelegateImpl(NnApiDelegate.Options options) {
        TensorFlowLite.init();
        int executionPreference = options.getExecutionPreference();
        String acceleratorName = options.getAcceleratorName();
        String cacheDir = options.getCacheDir();
        String modelToken = options.getModelToken();
        int maxNumberOfDelegatedPartitions = options.getMaxNumberOfDelegatedPartitions();
        boolean z = false;
        boolean z2 = options.getUseNnapiCpu() != null;
        if (options.getUseNnapiCpu() == null || !options.getUseNnapiCpu().booleanValue()) {
            z = true;
        }
        this.delegateHandle = createDelegate(executionPreference, acceleratorName, cacheDir, modelToken, maxNumberOfDelegatedPartitions, z2, z, options.getAllowFp16(), options.getNnApiSupportLibraryHandle());
    }

    @Override // org.tensorflow.lite.Delegate
    public long getNativeHandle() {
        return this.delegateHandle;
    }

    @Override // org.tensorflow.lite.nnapi.NnApiDelegate.PrivateInterface, org.tensorflow.lite.Delegate, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.delegateHandle != 0) {
            deleteDelegate(this.delegateHandle);
            this.delegateHandle = 0L;
        }
    }

    @Override // org.tensorflow.lite.nnapi.NnApiDelegate.PrivateInterface
    public int getNnapiErrno() {
        checkNotClosed();
        return getNnapiErrno(this.delegateHandle);
    }

    private void checkNotClosed() {
        if (this.delegateHandle == 0) {
            throw new IllegalStateException("Should not access delegate after it has been closed.");
        }
    }
}
