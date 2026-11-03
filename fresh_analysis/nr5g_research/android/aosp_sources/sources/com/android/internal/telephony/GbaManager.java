package com.android.internal.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telephony.IBootstrapAuthenticationCallback;
import android.telephony.SubscriptionManager;
import android.telephony.gba.GbaAuthRequest;
import android.telephony.gba.IGbaService;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.metrics.RcsStats;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.internal.telephony.util.WorkerThread;
import com.android.telephony.Rlog;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

/* JADX INFO: loaded from: classes.dex */
public class GbaManager {
    public static final int MAX_RETRY = 5;
    public static final int REQUEST_TIMEOUT_MS = 5000;
    public static final int RETRY_TIME_MS = 3000;
    private final Context mContext;
    private GbaDeathRecipient mDeathRecipient;
    private final FeatureFlags mFeatureFlags;
    private Handler mHandler;
    private IGbaService mIGbaService;
    private final String mLogTag;
    private final RcsStats mRcsStats;
    private int mReleaseTime;
    private GbaServiceConnection mServiceConnection;
    private String mServicePackageName;
    private final int mSubId;
    private String mTargetBindingPackageName;
    private static final boolean DBG = Build.IS_DEBUGGABLE;
    private static final SparseArray sGbaManagers = new SparseArray();
    private int mUserId = 0;
    private int mRetryTimes = 0;
    private final ConcurrentLinkedQueue mRequestQueue = new ConcurrentLinkedQueue();
    private final SparseArray mCallbacks = new SparseArray();
    private final IBootstrapAuthenticationCallback mServiceCallback = new IBootstrapAuthenticationCallback.Stub() { // from class: com.android.internal.telephony.GbaManager.1
        public void onKeysAvailable(int i, byte[] bArr, String str) {
            IBootstrapAuthenticationCallback iBootstrapAuthenticationCallback;
            GbaManager.this.logv("onKeysAvailable: " + Integer.toHexString(i) + ", id: " + str);
            synchronized (GbaManager.this.mCallbacks) {
                iBootstrapAuthenticationCallback = (IBootstrapAuthenticationCallback) GbaManager.this.mCallbacks.get(i);
            }
            if (iBootstrapAuthenticationCallback != null) {
                try {
                    iBootstrapAuthenticationCallback.onKeysAvailable(i, bArr, str);
                    GbaManager.this.mRcsStats.onGbaSuccessEvent(GbaManager.this.mSubId);
                } catch (RemoteException e) {
                    GbaManager.this.logd("RemoteException " + e);
                }
                synchronized (GbaManager.this.mCallbacks) {
                    try {
                        GbaManager.this.mCallbacks.remove(i);
                        if (GbaManager.this.mCallbacks.size() == 0) {
                            GbaManager.this.releaseServiceAsNeeded(0);
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }

        public void onAuthenticationFailure(int i, int i2) {
            IBootstrapAuthenticationCallback iBootstrapAuthenticationCallback;
            GbaManager.this.logd("onAuthenticationFailure: " + Integer.toHexString(i) + " for: " + i2);
            synchronized (GbaManager.this.mCallbacks) {
                iBootstrapAuthenticationCallback = (IBootstrapAuthenticationCallback) GbaManager.this.mCallbacks.get(i);
            }
            if (iBootstrapAuthenticationCallback != null) {
                try {
                    iBootstrapAuthenticationCallback.onAuthenticationFailure(i, i2);
                    GbaManager.this.mRcsStats.onGbaFailureEvent(GbaManager.this.mSubId, i2);
                } catch (RemoteException e) {
                    GbaManager.this.logd("RemoteException " + e);
                }
                synchronized (GbaManager.this.mCallbacks) {
                    try {
                        GbaManager.this.mCallbacks.remove(i);
                        if (GbaManager.this.mCallbacks.size() == 0) {
                            GbaManager.this.releaseServiceAsNeeded(0);
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }
    };

    private final class GbaManagerHandler extends Handler {
        GbaManagerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            GbaManager.this.logv("handle msg:" + message.what);
            switch (message.what) {
                case 1:
                    GbaManager gbaManager = GbaManager.this;
                    int i = gbaManager.mRetryTimes;
                    gbaManager.mRetryTimes = i + 1;
                    GbaManager gbaManager2 = GbaManager.this;
                    if (i < 5) {
                        gbaManager2.rebindService(false);
                    } else {
                        gbaManager2.loge("Too many retries, stop now!");
                        sendEmptyMessage(3);
                    }
                    break;
                case 2:
                    if (GbaManager.this.mRequestQueue.isEmpty()) {
                        GbaManager.this.clearCallbacksAndNotifyFailure();
                        GbaManager.this.unbindService();
                    }
                    break;
                case 3:
                case 4:
                    GbaManager.this.mRetryTimes = 0;
                    GbaManager.this.processRequests();
                    break;
                case 5:
                    GbaManager.this.mRetryTimes = 0;
                    if (GbaManager.this.isServiceConnetable() || GbaManager.this.isServiceConnected()) {
                        GbaManager.this.rebindService(true);
                    }
                    break;
                case 6:
                    boolean zIsServiceConnected = GbaManager.this.isServiceConnected();
                    GbaManager gbaManager3 = GbaManager.this;
                    if (zIsServiceConnected) {
                        gbaManager3.processRequests();
                    } else if (!gbaManager3.mHandler.hasMessages(1)) {
                        GbaManager.this.mHandler.sendEmptyMessage(1);
                    }
                    break;
                default:
                    GbaManager.this.loge("Unhandled event " + message.what);
                    break;
            }
        }
    }

    private final class GbaDeathRecipient implements IBinder.DeathRecipient {
        private IBinder mBinder;
        private final ComponentName mComponentName;

        GbaDeathRecipient(ComponentName componentName) {
            this.mComponentName = componentName;
        }

        public void linkToDeath(IBinder iBinder) throws RemoteException {
            if (iBinder != null) {
                this.mBinder = iBinder;
                iBinder.linkToDeath(this, 0);
            }
        }

        public synchronized void unlinkToDeath() {
            IBinder iBinder = this.mBinder;
            if (iBinder != null) {
                try {
                    iBinder.unlinkToDeath(this, 0);
                } catch (NoSuchElementException unused) {
                }
                this.mBinder = null;
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            GbaManager.this.logd("GbaService(" + this.mComponentName + ") has died.");
            unlinkToDeath();
            GbaManager.this.retryBind();
        }
    }

    private final class GbaServiceConnection implements ServiceConnection {
        private GbaServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            GbaManager.this.logd("service " + componentName + " for Gba is connected.");
            GbaManager.this.mIGbaService = IGbaService.Stub.asInterface(iBinder);
            GbaManager gbaManager = GbaManager.this;
            gbaManager.mDeathRecipient = gbaManager.new GbaDeathRecipient(componentName);
            try {
                GbaManager.this.mDeathRecipient.linkToDeath(iBinder);
            } catch (RemoteException e) {
                GbaManager.this.mDeathRecipient.binderDied();
                GbaManager.this.logd("RemoteException " + e);
            }
            GbaManager.this.mHandler.sendEmptyMessage(4);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            GbaManager.this.logd("service " + componentName + " is now disconnected.");
            GbaManager.this.mTargetBindingPackageName = null;
        }
    }

    public GbaManager(Context context, int i, String str, int i2, RcsStats rcsStats, Looper looper, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mSubId = i;
        this.mLogTag = "GbaManager[" + i + "]";
        this.mServicePackageName = str;
        this.mReleaseTime = i2;
        this.mFeatureFlags = featureFlags;
        GbaManagerHandler gbaManagerHandler = new GbaManagerHandler(looper);
        this.mHandler = gbaManagerHandler;
        if (this.mReleaseTime < 0) {
            gbaManagerHandler.sendEmptyMessage(1);
        }
        this.mRcsStats = rcsStats;
    }

    public static GbaManager make(Context context, int i, String str, int i2, FeatureFlags featureFlags) {
        GbaManager gbaManager = new GbaManager(context, i, str, i2, RcsStats.getInstance(), WorkerThread.get().getLooper(), featureFlags);
        SparseArray sparseArray = sGbaManagers;
        synchronized (sparseArray) {
            sparseArray.put(i, gbaManager);
        }
        return gbaManager;
    }

    public static GbaManager getInstance(int i) {
        GbaManager gbaManager;
        SparseArray sparseArray = sGbaManagers;
        synchronized (sparseArray) {
            gbaManager = (GbaManager) sparseArray.get(i);
        }
        return gbaManager;
    }

    public void bootstrapAuthenticationRequest(GbaAuthRequest gbaAuthRequest) {
        logv("bootstrapAuthenticationRequest: " + gbaAuthRequest);
        if (TextUtils.isEmpty(getServicePackage())) {
            logd("do not support!");
            try {
                gbaAuthRequest.getCallback().onAuthenticationFailure(gbaAuthRequest.getToken(), 1);
                return;
            } catch (RemoteException e) {
                loge("exception to call service: " + e);
                return;
            }
        }
        this.mRequestQueue.offer(gbaAuthRequest);
        if (this.mHandler.hasMessages(6)) {
            return;
        }
        this.mHandler.sendEmptyMessage(6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processRequests() {
        if (isServiceConnected()) {
            while (!this.mRequestQueue.isEmpty()) {
                try {
                    GbaAuthRequest gbaAuthRequest = new GbaAuthRequest((GbaAuthRequest) this.mRequestQueue.peek());
                    synchronized (this.mCallbacks) {
                        this.mCallbacks.put(gbaAuthRequest.getToken(), gbaAuthRequest.getCallback());
                    }
                    gbaAuthRequest.setCallback(this.mServiceCallback);
                    this.mIGbaService.authenticationRequest(gbaAuthRequest);
                    this.mRequestQueue.poll();
                } catch (RemoteException e) {
                    this.mDeathRecipient.binderDied();
                    logd("RemoteException " + e);
                }
            }
        } else {
            while (!this.mRequestQueue.isEmpty()) {
                GbaAuthRequest gbaAuthRequest2 = (GbaAuthRequest) this.mRequestQueue.poll();
                try {
                    gbaAuthRequest2.getCallback().onAuthenticationFailure(gbaAuthRequest2.getToken(), 1);
                } catch (RemoteException e2) {
                    logd("RemoteException " + e2);
                }
            }
        }
        releaseServiceAsNeeded(REQUEST_TIMEOUT_MS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseServiceAsNeeded(int i) {
        int releaseTime = getReleaseTime();
        if (releaseTime < 0) {
            return;
        }
        if (releaseTime > i) {
            i = releaseTime;
        }
        if (this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
        }
        this.mHandler.sendEmptyMessageDelayed(2, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearCallbacksAndNotifyFailure() {
        synchronized (this.mCallbacks) {
            int i = 0;
            while (true) {
                int size = this.mCallbacks.size();
                SparseArray sparseArray = this.mCallbacks;
                if (i < size) {
                    try {
                        ((IBootstrapAuthenticationCallback) sparseArray.valueAt(i)).onAuthenticationFailure(this.mCallbacks.keyAt(i), 0);
                    } catch (RemoteException e) {
                        logd("RemoteException " + e);
                    }
                    i++;
                } else {
                    sparseArray.clear();
                }
                throw th;
            }
        }
    }

    public boolean isServiceConnected() {
        boolean z;
        synchronized (this) {
            try {
                IGbaService iGbaService = this.mIGbaService;
                z = iGbaService != null && iGbaService.asBinder().isBinderAlive() && TextUtils.equals(this.mServicePackageName, this.mTargetBindingPackageName);
            } catch (Throwable th) {
                throw th;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isServiceConnetable() {
        boolean z;
        synchronized (this) {
            try {
                z = this.mTargetBindingPackageName != null || (this.mReleaseTime < 0 && !TextUtils.isEmpty(this.mServicePackageName));
            } catch (Throwable th) {
                throw th;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindService() {
        GbaDeathRecipient gbaDeathRecipient = this.mDeathRecipient;
        if (gbaDeathRecipient != null) {
            gbaDeathRecipient.unlinkToDeath();
        }
        if (this.mServiceConnection != null) {
            logv("unbind service.");
            this.mContext.unbindService(this.mServiceConnection);
        }
        this.mDeathRecipient = null;
        this.mIGbaService = null;
        this.mServiceConnection = null;
        this.mTargetBindingPackageName = null;
    }

    private void bindService() {
        if (this.mContext == null || !SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            loge("Can't bind service with invalid sub Id.");
            return;
        }
        String servicePackage = getServicePackage();
        if (TextUtils.isEmpty(servicePackage)) {
            loge("Can't find the binding package");
            return;
        }
        Intent intent = new Intent("android.telephony.gba.GbaService");
        intent.setPackage(servicePackage);
        try {
            logv("Trying to bind " + servicePackage);
            GbaServiceConnection gbaServiceConnection = new GbaServiceConnection();
            this.mServiceConnection = gbaServiceConnection;
            if (!this.mContext.bindServiceAsUser(intent, gbaServiceConnection, 67108865, UserHandle.of(this.mUserId))) {
                logd("Cannot bind to the service.");
                retryBind();
            } else {
                this.mTargetBindingPackageName = servicePackage;
            }
        } catch (SecurityException e) {
            loge("bindService failed " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retryBind() {
        if (this.mHandler.hasMessages(1)) {
            logv("wait for pending retry.");
            return;
        }
        logv("starting retry:" + this.mRetryTimes);
        this.mHandler.sendEmptyMessageDelayed(1, SatelliteSessionController.DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rebindService(boolean z) {
        if (!z && isServiceConnected()) {
            logv("Service " + getServicePackage() + " already bound or being bound.");
            return;
        }
        unbindService();
        bindService();
    }

    public boolean overrideServicePackage(String str, int i) {
        synchronized (this) {
            try {
                if (TextUtils.equals(this.mServicePackageName, str) && i == this.mUserId) {
                    return false;
                }
                logv("Service package name is changed from " + this.mServicePackageName + " to " + str + ", user id from " + this.mUserId + " to " + i);
                this.mServicePackageName = str;
                this.mUserId = i;
                if (!this.mHandler.hasMessages(5)) {
                    this.mHandler.sendEmptyMessage(5);
                }
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public String getServicePackage() {
        String str;
        synchronized (this) {
            str = this.mServicePackageName;
        }
        return str;
    }

    public boolean overrideReleaseTime(int i) {
        synchronized (this) {
            try {
                if (this.mReleaseTime == i) {
                    return false;
                }
                logv("Service release time is changed from " + this.mReleaseTime + " to " + i);
                this.mReleaseTime = i;
                if (!this.mHandler.hasMessages(5)) {
                    this.mHandler.sendEmptyMessage(5);
                }
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int getReleaseTime() {
        int i;
        synchronized (this) {
            i = this.mReleaseTime;
        }
        return i;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void destroy() {
        this.mHandler.removeCallbacksAndMessages(null);
        this.mRequestQueue.clear();
        this.mCallbacks.clear();
        unbindService();
        SparseArray sparseArray = sGbaManagers;
        synchronized (sparseArray) {
            sparseArray.remove(this.mSubId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logv(String str) {
        if (DBG) {
            Rlog.d(this.mLogTag, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Rlog.d(this.mLogTag, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }
}
