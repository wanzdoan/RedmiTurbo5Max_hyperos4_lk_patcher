package com.xiaomi.telephony;

import android.os.Binder;
import android.os.Bundle;
import com.android.internal.util.FunctionalUtils;
import com.xiaomi.internal.telephony.IQosStateCallback;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class QosStateCallback {
    private static final String LOG_TAG = "QosStateCallback";
    private Executor executor;
    private final IQosStateCallbackStub mCallback;

    public QosStateCallback() {
        this(new CommMsgCallback$$ExternalSyntheticLambda0());
    }

    public QosStateCallback(Executor executor) {
        this.executor = executor;
        this.mCallback = new IQosStateCallbackStub(this, executor);
    }

    public IQosStateCallbackStub getBinder() {
        return this.mCallback;
    }

    public void onQosStateChange(int slotId, Bundle dataBundle) {
    }

    protected static class IQosStateCallbackStub extends IQosStateCallback.Stub {
        private WeakReference<QosStateCallback> mCallbackWeakRef;
        private Executor mExecutor;

        IQosStateCallbackStub(QosStateCallback callback, Executor executor) {
            this.mCallbackWeakRef = new WeakReference<>(callback);
            this.mExecutor = executor;
        }

        @Override // com.xiaomi.internal.telephony.IQosStateCallback
        public void onQosStateChange(final int slotId, final Bundle dataBundle) {
            final QosStateCallback callback = this.mCallbackWeakRef.get();
            if (callback == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.xiaomi.telephony.QosStateCallback$IQosStateCallbackStub$$ExternalSyntheticLambda0
                public final void runOrThrow() throws Exception {
                    this.f$0.lambda$onQosStateChange$1(callback, slotId, dataBundle);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onQosStateChange$1(final QosStateCallback callback, final int slotId, final Bundle dataBundle) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.telephony.QosStateCallback$IQosStateCallbackStub$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    callback.onQosStateChange(slotId, dataBundle);
                }
            });
        }
    }
}
