package com.xiaomi.telephony;

import android.os.Binder;
import com.android.internal.util.FunctionalUtils;
import com.xiaomi.internal.telephony.IModemSceneCallback;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class ModemSceneCallback {
    private Executor executor;
    private final IModemSceneCallbackStub mCallback;

    public ModemSceneCallback(Executor executor) {
        Objects.requireNonNull(executor);
        this.executor = executor;
        this.mCallback = new IModemSceneCallbackStub(this, executor);
    }

    public IModemSceneCallbackStub getBinder() {
        return this.mCallback;
    }

    public void onModemSceneChanged(int slotId, int[] sceneInfo) {
    }

    protected static class IModemSceneCallbackStub extends IModemSceneCallback.Stub {
        private WeakReference<ModemSceneCallback> mCallbackWeakRef;
        private Executor mExecutor;

        IModemSceneCallbackStub(ModemSceneCallback callback, Executor executor) {
            this.mCallbackWeakRef = new WeakReference<>(callback);
            this.mExecutor = executor;
        }

        @Override // com.xiaomi.internal.telephony.IModemSceneCallback
        public void onModemSceneChanged(final int slotId, final int[] sceneInfo) {
            final ModemSceneCallback callback = this.mCallbackWeakRef.get();
            if (callback == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.xiaomi.telephony.ModemSceneCallback$IModemSceneCallbackStub$$ExternalSyntheticLambda0
                public final void runOrThrow() throws Exception {
                    this.f$0.lambda$onModemSceneChanged$1(callback, slotId, sceneInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onModemSceneChanged$1(final ModemSceneCallback callback, final int slotId, final int[] sceneInfo) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.telephony.ModemSceneCallback$IModemSceneCallbackStub$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    callback.onModemSceneChanged(slotId, sceneInfo);
                }
            });
        }
    }
}
