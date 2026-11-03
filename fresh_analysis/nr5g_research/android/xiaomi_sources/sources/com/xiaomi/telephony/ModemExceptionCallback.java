package com.xiaomi.telephony;

import android.os.Binder;
import com.android.internal.util.FunctionalUtils;
import com.xiaomi.internal.telephony.IModemExceptionCallback;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class ModemExceptionCallback {
    private Executor executor;
    private final IModemExceptionCallbackStub mCallback;

    public ModemExceptionCallback(Executor executor) {
        Objects.requireNonNull(executor);
        this.executor = executor;
        this.mCallback = new IModemExceptionCallbackStub(this, executor);
    }

    public IModemExceptionCallbackStub getBinder() {
        return this.mCallback;
    }

    public void onModemExceptionChanged(int slotId, int[] modemException) {
    }

    protected static class IModemExceptionCallbackStub extends IModemExceptionCallback.Stub {
        private WeakReference<ModemExceptionCallback> mCallbackWeakRef;
        private Executor mExecutor;

        IModemExceptionCallbackStub(ModemExceptionCallback callback, Executor executor) {
            this.mCallbackWeakRef = new WeakReference<>(callback);
            this.mExecutor = executor;
        }

        @Override // com.xiaomi.internal.telephony.IModemExceptionCallback
        public void onModemExceptionChanged(final int slotId, final int[] modemException) {
            final ModemExceptionCallback callback = this.mCallbackWeakRef.get();
            if (callback == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.xiaomi.telephony.ModemExceptionCallback$IModemExceptionCallbackStub$$ExternalSyntheticLambda0
                public final void runOrThrow() throws Exception {
                    this.f$0.lambda$onModemExceptionChanged$1(callback, slotId, modemException);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onModemExceptionChanged$1(final ModemExceptionCallback callback, final int slotId, final int[] modemException) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.telephony.ModemExceptionCallback$IModemExceptionCallbackStub$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    callback.onModemExceptionChanged(slotId, modemException);
                }
            });
        }
    }
}
