package com.android.phone;

import android.content.Intent;
import android.net.IMiuiNetworkManager;
import android.net.IMiuiNetworkScoreCallback;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.INetworkManagementService;
import android.os.Looper;
import android.os.Message;
import android.os.ServiceManager;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class KernelScoreMonitor {
    private static boolean VDBG = false;
    private static KernelScoreMonitor mInstance;
    private KernelScoreCallback mCallback;
    private final IBinder.DeathRecipient mDeathRecipient;
    private final ScoreHandler mHandler;
    private IMiuiNetworkManager mMiuiNetworkManager;
    private final HandlerThread mWorkThread;
    private int mConfirmRetryCount = 0;
    private volatile boolean mCallbackConfirmed = false;
    private volatile boolean mDestroyed = false;
    private final IMiuiNetworkScoreCallback.Stub mNetworkScoreCallback = new IMiuiNetworkScoreCallback.Stub() { // from class: com.android.phone.KernelScoreMonitor.1
        public void onNetworkScoreChanged(Intent intent) {
            KernelScoreMonitor.logD("onNetworkScoreChanged");
            KernelScoreMonitor.this.mHandler.obtainMessage(4, intent).sendToTarget();
        }

        public void onKernelProtocolMetricsChanged(Intent intent) {
            KernelScoreMonitor.logD("onKernelProtocolMetricsChanged");
            if (!KernelScoreMonitor.this.mCallbackConfirmed) {
                KernelScoreMonitor.this.mCallbackConfirmed = true;
                KernelScoreMonitor.this.mHandler.removeMessages(2);
                KernelScoreMonitor.log("Callback confirmed by first data arrival");
            }
            KernelScoreMonitor.this.mHandler.obtainMessage(5, intent).sendToTarget();
        }
    };

    public interface KernelScoreCallback {
        void onKernelProtocolMetricsChanged(Intent intent);

        void onKernelScoreChanged(Intent intent);
    }

    public static synchronized KernelScoreMonitor getInstance() {
        try {
            if (mInstance == null) {
                mInstance = new KernelScoreMonitor();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private KernelScoreMonitor() {
        log("KernelScoreMonitor created");
        HandlerThread handlerThread = new HandlerThread("KernelScoreMonitor", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new ScoreHandler(handlerThread.getLooper());
        this.mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.android.phone.KernelScoreMonitor$$ExternalSyntheticLambda0
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                this.f$0.lambda$new$0();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        Log.w("KernelScoreMonitor", "MiuiNetworkManager died");
        this.mHandler.obtainMessage(3).sendToTarget();
    }

    public void setCallback(KernelScoreCallback kernelScoreCallback) {
        this.mCallback = kernelScoreCallback;
    }

    public void init() {
        log("init: first attempt in 10000ms");
        this.mDestroyed = false;
        this.mConfirmRetryCount = 0;
        this.mCallbackConfirmed = false;
        this.mHandler.sendEmptyMessageDelayed(1, 10000L);
    }

    class ScoreHandler extends Handler {
        ScoreHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (KernelScoreMonitor.this.mDestroyed) {
                return;
            }
            int i = message.what;
            if (i == 1) {
                KernelScoreMonitor.this.handleConnectAndRegister();
                return;
            }
            if (i == 2) {
                KernelScoreMonitor.this.handleConfirmTimeout();
                return;
            }
            if (i == 3) {
                KernelScoreMonitor.this.handleBinderDied();
                return;
            }
            if (i == 4) {
                KernelScoreMonitor.this.dispatchScoreChanged((Intent) message.obj);
            } else {
                if (i == 5) {
                    KernelScoreMonitor.this.dispatchProtocolMetricsChanged((Intent) message.obj);
                    return;
                }
                KernelScoreMonitor.log("Unknown message: " + message.what);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectAndRegister() {
        if (!connectToService() || !registerCallback()) {
            Log.w("KernelScoreMonitor", "connectAndRegister: connect or register failed");
        }
        this.mHandler.removeMessages(2);
        this.mHandler.sendEmptyMessageDelayed(2, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConfirmTimeout() {
        if (this.mCallbackConfirmed) {
            return;
        }
        int i = this.mConfirmRetryCount + 1;
        this.mConfirmRetryCount = i;
        if (i > 3) {
            Log.e("KernelScoreMonitor", "Callback not confirmed after 3 retries, giving up");
            return;
        }
        Log.w("KernelScoreMonitor", "Callback not confirmed, retry " + this.mConfirmRetryCount + "/3");
        handleConnectAndRegister();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBinderDied() {
        try {
            IMiuiNetworkManager iMiuiNetworkManager = this.mMiuiNetworkManager;
            if (iMiuiNetworkManager != null) {
                iMiuiNetworkManager.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            }
        } catch (Exception e) {
            Log.e("KernelScoreMonitor", "Error unlinking death recipient", e);
        }
        this.mMiuiNetworkManager = null;
        if (!this.mDestroyed) {
            Log.w("KernelScoreMonitor", "binderDied: reconnecting");
            this.mConfirmRetryCount = 0;
            this.mCallbackConfirmed = false;
            this.mHandler.sendEmptyMessage(1);
            return;
        }
        log("binderDied: already destroyed, skip reconnect");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchScoreChanged(Intent intent) {
        KernelScoreCallback kernelScoreCallback = this.mCallback;
        if (kernelScoreCallback != null) {
            kernelScoreCallback.onKernelScoreChanged(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchProtocolMetricsChanged(Intent intent) {
        KernelScoreCallback kernelScoreCallback = this.mCallback;
        if (kernelScoreCallback != null) {
            kernelScoreCallback.onKernelProtocolMetricsChanged(intent);
        }
    }

    private boolean connectToService() {
        try {
            IBinder service = ServiceManager.getService("network_management");
            if (service == null) {
                Log.w("KernelScoreMonitor", "network_management service not found");
                return false;
            }
            IBinder miuiNetworkManager = INetworkManagementService.Stub.asInterface(service).getMiuiNetworkManager();
            if (miuiNetworkManager == null) {
                Log.w("KernelScoreMonitor", "MiuiNetworkManager binder is null");
                return false;
            }
            this.mMiuiNetworkManager = IMiuiNetworkManager.Stub.asInterface(miuiNetworkManager);
            miuiNetworkManager.linkToDeath(this.mDeathRecipient, 0);
            log("Connected to MiuiNetworkManager");
            return true;
        } catch (Exception e) {
            Log.e("KernelScoreMonitor", "Failed to connect", e);
            return false;
        }
    }

    private boolean registerCallback() {
        try {
            IMiuiNetworkManager iMiuiNetworkManager = this.mMiuiNetworkManager;
            if (iMiuiNetworkManager == null) {
                return false;
            }
            iMiuiNetworkManager.registerNetworkScoreCallback(this.mNetworkScoreCallback);
            log("Registered network score callback");
            return true;
        } catch (Exception e) {
            Log.w("KernelScoreMonitor", "Failed to register callback", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("KernelScoreMonitor", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logD(String str) {
        if (VDBG) {
            Log.d("KernelScoreMonitor", str);
        }
    }
}
