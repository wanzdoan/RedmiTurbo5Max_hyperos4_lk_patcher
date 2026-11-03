package com.xiaomi.telephony;

import android.os.Binder;
import com.android.internal.util.FunctionalUtils;
import com.xiaomi.internal.telephony.ICommMsgCallback;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class CommMsgCallback {
    private static final String LOG_TAG = "CommMsgCallback";
    private Executor executor;
    private final ICommMsgCallbackStub mCallback;

    public CommMsgCallback() {
        this(new CommMsgCallback$$ExternalSyntheticLambda0());
    }

    public CommMsgCallback(Executor executor) {
        this.executor = executor;
        this.mCallback = new ICommMsgCallbackStub(this, executor);
    }

    public ICommMsgCallbackStub getBinder() {
        return this.mCallback;
    }

    public void onUnsolCommMsg(int phoneId, int msgId, byte[] data) {
    }

    protected static class ICommMsgCallbackStub extends ICommMsgCallback.Stub {
        private WeakReference<CommMsgCallback> mCallbackWeakRef;
        private Executor mExecutor;

        ICommMsgCallbackStub(CommMsgCallback callback, Executor executor) {
            this.mCallbackWeakRef = new WeakReference<>(callback);
            this.mExecutor = executor;
        }

        @Override // com.xiaomi.internal.telephony.ICommMsgCallback
        public void onUnsolCommMsg(final int phoneId, final int msgId, final byte[] data) {
            final CommMsgCallback callback = this.mCallbackWeakRef.get();
            if (callback == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.xiaomi.telephony.CommMsgCallback$ICommMsgCallbackStub$$ExternalSyntheticLambda0
                public final void runOrThrow() throws Exception {
                    this.f$0.lambda$onUnsolCommMsg$1(callback, phoneId, msgId, data);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUnsolCommMsg$1(final CommMsgCallback callback, final int phoneId, final int msgId, final byte[] data) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.telephony.CommMsgCallback$ICommMsgCallbackStub$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    callback.onUnsolCommMsg(phoneId, msgId, data);
                }
            });
        }
    }
}
