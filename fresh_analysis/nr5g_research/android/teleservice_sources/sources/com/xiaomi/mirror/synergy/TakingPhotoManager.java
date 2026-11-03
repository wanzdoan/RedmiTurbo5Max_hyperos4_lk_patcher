package com.xiaomi.mirror.synergy;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.xiaomi.mirror.IMirrorTakePhotoCallback;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes2.dex */
public class TakingPhotoManager implements IBinder.DeathRecipient {
    private CallbackWrapper mCallbackWrapper;

    class CallbackWrapper extends IMirrorTakePhotoCallback.Stub {
        private final WeakReference mCallBack;

        public CallbackWrapper(MirrorTakePhotoCallback mirrorTakePhotoCallback) {
            this.mCallBack = new WeakReference(mirrorTakePhotoCallback);
        }

        @Override // com.xiaomi.mirror.IMirrorTakePhotoCallback
        public void onTakePhotoChanged(String str, boolean z) throws RemoteException {
            if (this.mCallBack.get() != null) {
                ((MirrorTakePhotoCallback) this.mCallBack.get()).onTakePhotoChanged(str, z);
            }
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        CallbackWrapper callbackWrapper = this.mCallbackWrapper;
        if (callbackWrapper != null) {
            callbackWrapper.unlinkToDeath(this, 0);
            this.mCallbackWrapper = null;
        }
    }

    public int registerTakePhotoCallback(Context context, MirrorTakePhotoCallback mirrorTakePhotoCallback) {
        if (this.mCallbackWrapper != null) {
            return -1;
        }
        CallbackWrapper callbackWrapper = new CallbackWrapper(mirrorTakePhotoCallback);
        this.mCallbackWrapper = callbackWrapper;
        callbackWrapper.linkToDeath(this, 0);
        Bundle bundle = new Bundle();
        bundle.putBinder("callback", this.mCallbackWrapper.asBinder());
        try {
            return CallMethod.doCall(context.getContentResolver(), "registerTakingPhone", null, bundle) != null ? 0 : -1;
        } catch (Exception unused) {
        }
    }

    public int unregisterTakePhotoCallback(Context context, MirrorTakePhotoCallback mirrorTakePhotoCallback) {
        CallbackWrapper callbackWrapper = this.mCallbackWrapper;
        if (callbackWrapper == null || callbackWrapper.mCallBack.get() != mirrorTakePhotoCallback) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putBinder("callback", this.mCallbackWrapper.asBinder());
        try {
            CallMethod.doCall(context.getContentResolver(), "unregisterTakingPhone", null, bundle);
        } catch (Exception unused) {
        }
        this.mCallbackWrapper = null;
        return 0;
    }
}
