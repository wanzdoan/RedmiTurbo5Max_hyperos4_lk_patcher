package com.android.internal.telephony.ims;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.UserHandle;
import android.telephony.ims.aidl.IImsServiceController;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import android.util.Log;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ImsServiceFeatureQueryManager {
    private final Context mContext;
    private final Listener mListener;
    private final Map mActiveQueries = new HashMap();
    private final Object mLock = new Object();

    public interface Listener {
        void onComplete(ComponentName componentName, Set<ImsFeatureConfiguration.FeatureSlotPair> set);

        void onError(ComponentName componentName);

        void onPermanentError(ComponentName componentName, UserHandle userHandle);
    }

    private final class ImsServiceFeatureQuery implements ServiceConnection {
        private final String mIntentFilter;
        private boolean mIsServiceConnectionDead = false;
        private final ComponentName mName;
        private final UserHandle mUser;

        ImsServiceFeatureQuery(ComponentName componentName, UserHandle userHandle, String str) {
            this.mName = componentName;
            this.mUser = userHandle;
            this.mIntentFilter = str;
        }

        public boolean start() {
            Log.d("ImsServiceFeatureQuery", "start: intent filter=" + this.mIntentFilter + ", name=" + this.mName);
            boolean zBindServiceAsUser = ImsServiceFeatureQueryManager.this.mContext.bindServiceAsUser(new Intent(this.mIntentFilter).setComponent(this.mName), this, 67108929, this.mUser);
            if (!zBindServiceAsUser) {
                cleanup();
            }
            return zBindServiceAsUser;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("ImsServiceFeatureQuery", "onServiceConnected for component: " + componentName);
            if (iBinder != null) {
                queryImsFeatures(IImsServiceController.Stub.asInterface(iBinder));
                return;
            }
            Log.w("ImsServiceFeatureQuery", "onServiceConnected: " + componentName + " binder null.");
            cleanup();
            ImsServiceFeatureQueryManager.this.mListener.onPermanentError(componentName, this.mUser);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.w("ImsServiceFeatureQuery", "onServiceDisconnected for component: " + componentName);
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            this.mIsServiceConnectionDead = true;
            Log.w("ImsServiceFeatureQuery", "onBindingDied: " + componentName);
            cleanup();
            ImsServiceFeatureQueryManager.this.mListener.onError(componentName);
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(ComponentName componentName) {
            Log.w("ImsServiceFeatureQuery", "onNullBinding: " + componentName);
            if (this.mIsServiceConnectionDead) {
                return;
            }
            cleanup();
            ImsServiceFeatureQueryManager.this.mListener.onPermanentError(componentName, this.mUser);
        }

        private void queryImsFeatures(IImsServiceController iImsServiceController) {
            Set<ImsFeatureConfiguration.FeatureSlotPair> serviceFeatures;
            try {
                ImsFeatureConfiguration imsFeatureConfigurationQuerySupportedImsFeatures = iImsServiceController.querySupportedImsFeatures();
                if (imsFeatureConfigurationQuerySupportedImsFeatures == null) {
                    serviceFeatures = Collections.EMPTY_SET;
                } else {
                    serviceFeatures = imsFeatureConfigurationQuerySupportedImsFeatures.getServiceFeatures();
                }
                cleanup();
                ImsServiceFeatureQueryManager.this.mListener.onComplete(this.mName, serviceFeatures);
            } catch (Exception e) {
                Log.w("ImsServiceFeatureQuery", "queryImsFeatures - error: " + e);
                cleanup();
                ImsServiceFeatureQueryManager.this.mListener.onError(this.mName);
            }
        }

        private void cleanup() {
            ImsServiceFeatureQueryManager.this.mContext.unbindService(this);
            synchronized (ImsServiceFeatureQueryManager.this.mLock) {
                ImsServiceFeatureQueryManager.this.mActiveQueries.remove(this.mName);
            }
        }
    }

    public ImsServiceFeatureQueryManager(Context context, Listener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public boolean startQuery(ComponentName componentName, UserHandle userHandle, String str) {
        synchronized (this.mLock) {
            try {
                if (this.mActiveQueries.containsKey(componentName)) {
                    return true;
                }
                ImsServiceFeatureQuery imsServiceFeatureQuery = new ImsServiceFeatureQuery(componentName, userHandle, str);
                this.mActiveQueries.put(componentName, imsServiceFeatureQuery);
                return imsServiceFeatureQuery.start();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean isQueryInProgress() {
        boolean z;
        synchronized (this.mLock) {
            z = !this.mActiveQueries.isEmpty();
        }
        return z;
    }
}
