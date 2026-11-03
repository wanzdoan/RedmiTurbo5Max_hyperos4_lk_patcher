package miuix.autodensity;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import miuix.core.util.EnvStateManager;
import miuix.core.util.WindowBaseInfo;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DensityProcessor {
    protected HashMap mModifier;
    protected DisplayManager mDisplayManager = null;
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    protected void onDensityChangedOnActivityDisplayChanged(int i, Activity activity) {
    }

    protected void onDensityChangedOnAppConfigChanged(Application application) {
    }

    protected abstract void processBeforeActivityConfigChanged(Activity activity, Configuration configuration);

    protected abstract void processOnActivityCreated(Activity activity);

    protected abstract void processOnActivityDestroyed(Activity activity);

    protected abstract void processOnActivityDisplayChanged(int i, Activity activity);

    protected void registerCallback(Activity activity) {
        if (this.mDisplayManager == null) {
            this.mDisplayManager = (DisplayManager) activity.getApplication().getSystemService("display");
        }
        if (this.mModifier == null) {
            this.mModifier = new HashMap();
        }
        int iHashCode = activity.hashCode();
        if (((DensityCallback) this.mModifier.get(Integer.valueOf(iHashCode))) == null) {
            DensityCallback densityCallback = new DensityCallback(activity, this);
            this.mModifier.put(Integer.valueOf(iHashCode), densityCallback);
            this.mDisplayManager.registerDisplayListener(densityCallback, this.mUiHandler);
            activity.getApplication().registerComponentCallbacks(densityCallback);
            activity.registerComponentCallbacks(densityCallback);
        }
    }

    protected void unregisterCallback(Activity activity) {
        if (this.mModifier != null) {
            int iHashCode = activity.hashCode();
            DensityCallback densityCallback = (DensityCallback) this.mModifier.get(Integer.valueOf(iHashCode));
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("unregisterCallback obj: " + densityCallback);
            }
            if (densityCallback != null) {
                unregisterDisplayListener(densityCallback);
                activity.getApplication().unregisterComponentCallbacks(densityCallback);
                activity.unregisterComponentCallbacks(densityCallback);
                densityCallback.removeOnAttachStateChangeListener(activity);
                densityCallback.clear();
            }
            this.mModifier.remove(Integer.valueOf(iHashCode));
        }
    }

    protected void unregisterDisplayListener(DensityCallback densityCallback) {
        DisplayManager displayManager = this.mDisplayManager;
        if (displayManager != null) {
            displayManager.unregisterDisplayListener(densityCallback);
        }
    }

    protected void onDensityChangedOnActivityCreated(Activity activity) {
        registerCallback(activity);
    }

    protected void onDensityChangedBeforeActivityConfigChanged(Activity activity, Configuration configuration, WindowBaseInfo windowBaseInfo) {
        EnvStateManager.updateWindowSizeByConfig(configuration, windowBaseInfo);
        EnvStateManager.updateWindowMode(activity, windowBaseInfo);
        EnvStateManager.markEnvStateDirty(activity);
        EnvStateManager.markWindowInfoDirty(windowBaseInfo);
    }

    class DensityProcessorLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        private final DensityProcessor mDensityProcessor;

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
        }

        public DensityProcessorLifecycleCallbacks(DensityProcessor densityProcessor) {
            this.mDensityProcessor = densityProcessor;
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
            this.mDensityProcessor.processOnActivityCreated(activity);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
            this.mDensityProcessor.processOnActivityDestroyed(activity);
        }
    }

    class DensityCallback implements DisplayManager.DisplayListener, ComponentCallbacks {
        private WeakReference mActivityRefs;
        private WeakReference mDecorViewListener = null;
        private final DensityProcessor mDensityProcessor;

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
        }

        public DensityCallback(Activity activity, DensityProcessor densityProcessor) {
            this.mActivityRefs = null;
            this.mActivityRefs = new WeakReference(activity);
            this.mDensityProcessor = densityProcessor;
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            WeakReference weakReference = this.mActivityRefs;
            Activity activity = weakReference == null ? null : (Activity) weakReference.get();
            if (activity != null) {
                this.mDensityProcessor.processOnActivityDisplayChanged(i, activity);
            } else {
                DensityProcessor.this.unregisterDisplayListener(this);
            }
        }

        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
            WeakReference weakReference = this.mActivityRefs;
            Activity activity = weakReference != null ? (Activity) weakReference.get() : null;
            if (activity != null) {
                this.mDensityProcessor.processBeforeActivityConfigChanged(activity, configuration);
            } else {
                DensityProcessor.this.unregisterDisplayListener(this);
            }
        }

        void clear() {
            WeakReference weakReference = this.mActivityRefs;
            if (weakReference != null) {
                weakReference.clear();
            }
            WeakReference weakReference2 = this.mDecorViewListener;
            if (weakReference2 != null) {
                weakReference2.clear();
            }
        }

        void addOnAttachStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
            this.mDecorViewListener = new WeakReference(onAttachStateChangeListener);
        }

        void removeOnAttachStateChangeListener(Activity activity) {
            View.OnAttachStateChangeListener onAttachStateChangeListener;
            WeakReference weakReference = this.mDecorViewListener;
            if (weakReference == null || (onAttachStateChangeListener = (View.OnAttachStateChangeListener) weakReference.get()) == null) {
                return;
            }
            activity.getWindow().getDecorView().removeOnAttachStateChangeListener(onAttachStateChangeListener);
        }
    }
}
