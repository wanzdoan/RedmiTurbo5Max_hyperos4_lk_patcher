package com.xiaomi.telephony;

import android.os.Binder;
import com.android.internal.util.FunctionalUtils;
import com.xiaomi.internal.telephony.IHstStateCallback;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class HstStateCallback {
    private static final String LOG_TAG = "HstStateCallback";
    private Executor executor;
    private final IHstStateCallbackStub mCallback;

    public HstStateCallback() {
        this(new CommMsgCallback$$ExternalSyntheticLambda0());
    }

    public HstStateCallback(Executor executor) {
        this.executor = executor;
        this.mCallback = new IHstStateCallbackStub(this, executor);
    }

    public IHstStateCallbackStub getBinder() {
        return this.mCallback;
    }

    public void onHstStateChange(int slotId, boolean hstState, int hstRat) {
    }

    protected static class IHstStateCallbackStub extends IHstStateCallback.Stub {
        private WeakReference<HstStateCallback> mCallbackWeakRef;
        private Executor mExecutor;

        IHstStateCallbackStub(HstStateCallback callback, Executor executor) {
            this.mCallbackWeakRef = new WeakReference<>(callback);
            this.mExecutor = executor;
        }

        @Override // com.xiaomi.internal.telephony.IHstStateCallback
        public void onHstStateChange(final int slotId, final boolean hstState, final int hstRat) {
            final HstStateCallback callback = this.mCallbackWeakRef.get();
            if (callback == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.xiaomi.telephony.HstStateCallback$IHstStateCallbackStub$$ExternalSyntheticLambda0
                public final void runOrThrow() throws Exception {
                    this.f$0.lambda$onHstStateChange$1(callback, slotId, hstState, hstRat);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHstStateChange$1(final HstStateCallback callback, final int slotId, final boolean hstState, final int hstRat) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.telephony.HstStateCallback$IHstStateCallbackStub$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    callback.onHstStateChange(slotId, hstState, hstRat);
                }
            });
        }
    }
}
