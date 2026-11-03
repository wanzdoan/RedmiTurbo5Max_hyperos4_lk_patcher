package miuix.overscroller.internal.dynamicanimation.animation;

import android.os.Looper;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Choreographer;
import java.lang.reflect.Method;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class AnimationHandler {
    private static final boolean SUPPORT_MI_MOTION = Boolean.parseBoolean(LiteSystemProperties.get("ro.display.mimotion", "false"));
    public static final ThreadLocal sAnimatorHandler = new ThreadLocal();
    private AnimationFrameCallbackProvider mProvider;
    private final ArrayMap mDelayedCallbackStartTime = new ArrayMap();
    private final ArrayList mAnimationCallbacks = new ArrayList();
    private final AnimationCallbackDispatcher mCallbackDispatcher = new AnimationCallbackDispatcher();
    private long mCurrentFrameTime = 0;
    private boolean mListDirty = false;

    public interface AnimationFrameCallback {
        boolean doAnimationFrame(long j);
    }

    class AnimationCallbackDispatcher {
        AnimationCallbackDispatcher() {
        }

        void dispatchAnimationFrame(long j) {
            AnimationHandler.this.doAnimationFrame(j);
            if (AnimationHandler.this.mAnimationCallbacks.size() > 0) {
                AnimationHandler.this.getProvider().postFrameCallback();
            }
        }
    }

    public static AnimationHandler getInstance() {
        ThreadLocal threadLocal = sAnimatorHandler;
        if (threadLocal.get() == null) {
            threadLocal.set(new AnimationHandler());
        }
        return (AnimationHandler) threadLocal.get();
    }

    public AnimationFrameCallbackProvider getProvider() {
        if (this.mProvider == null) {
            this.mProvider = new FrameCallbackProvider33(this.mCallbackDispatcher);
        }
        return this.mProvider;
    }

    public void postVsyncCallback() {
        getProvider().postVsyncCallback();
    }

    public void addAnimationFrameCallback(AnimationFrameCallback animationFrameCallback, long j) {
        if (this.mAnimationCallbacks.size() == 0) {
            getProvider().postFrameCallback();
        }
        if (!this.mAnimationCallbacks.contains(animationFrameCallback)) {
            this.mAnimationCallbacks.add(animationFrameCallback);
        }
        if (j > 0) {
            this.mDelayedCallbackStartTime.put(animationFrameCallback, Long.valueOf(SystemClock.uptimeMillis() + j));
        }
    }

    public void removeCallback(AnimationFrameCallback animationFrameCallback) {
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        int iIndexOf = this.mAnimationCallbacks.indexOf(animationFrameCallback);
        if (iIndexOf >= 0) {
            this.mAnimationCallbacks.set(iIndexOf, null);
            this.mListDirty = true;
        }
    }

    boolean isCurrentThread() {
        return getProvider().isCurrentThread();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doAnimationFrame(long j) {
        long jUptimeMillis = SystemClock.uptimeMillis();
        for (int i = 0; i < this.mAnimationCallbacks.size(); i++) {
            AnimationFrameCallback animationFrameCallback = (AnimationFrameCallback) this.mAnimationCallbacks.get(i);
            if (animationFrameCallback != null && isCallbackDue(animationFrameCallback, jUptimeMillis)) {
                animationFrameCallback.doAnimationFrame(j);
            }
        }
        cleanUpList();
    }

    private boolean isCallbackDue(AnimationFrameCallback animationFrameCallback, long j) {
        Long l = (Long) this.mDelayedCallbackStartTime.get(animationFrameCallback);
        if (l == null) {
            return true;
        }
        if (l.longValue() >= j) {
            return false;
        }
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        return true;
    }

    private void cleanUpList() {
        if (this.mListDirty) {
            for (int size = this.mAnimationCallbacks.size() - 1; size >= 0; size--) {
                if (this.mAnimationCallbacks.get(size) == null) {
                    this.mAnimationCallbacks.remove(size);
                }
            }
            this.mListDirty = false;
        }
    }

    public long getFrameDeltaNanos() {
        return getProvider().getFrameDeltaNanos();
    }

    public boolean getFromFramePeriodNsecs() {
        return getProvider().getFromFramePeriodNsecs();
    }

    class FrameCallbackProvider33 extends AnimationFrameCallbackProvider {
        private final Choreographer mChoreographer;
        private final Choreographer.FrameCallback mChoreographerCallback;
        private long mFrameDeltaNanos;
        private boolean mFromFramePeriodNsecs;
        private Method mGetFramePeriodNsecs;
        private final Looper mLooper;
        private final Choreographer.VsyncCallback mVsyncCallback;

        FrameCallbackProvider33(AnimationCallbackDispatcher animationCallbackDispatcher) {
            super(animationCallbackDispatcher);
            this.mChoreographer = Choreographer.getInstance();
            this.mGetFramePeriodNsecs = null;
            this.mLooper = Looper.myLooper();
            this.mFrameDeltaNanos = 0L;
            this.mFromFramePeriodNsecs = false;
            if (AnimationHandler.SUPPORT_MI_MOTION && this.mGetFramePeriodNsecs == null) {
                try {
                    Class[] clsArr = new Class[0];
                    Method declaredMethod = Choreographer.class.getDeclaredMethod("getFramePeriodNsecs", null);
                    this.mGetFramePeriodNsecs = declaredMethod;
                    declaredMethod.setAccessible(true);
                } catch (Exception unused) {
                    Log.w("OverScroller Animation", "get getFramePeriodNSec failed ");
                }
            }
            this.mVsyncCallback = new Choreographer.VsyncCallback() { // from class: miuix.overscroller.internal.dynamicanimation.animation.AnimationHandler.FrameCallbackProvider33.1
                /* JADX WARN: Code duplicated, block: B:12:0x003e  */
                @Override // android.view.Choreographer.VsyncCallback
                public void onVsync(Choreographer.FrameData frameData) {
                    boolean z;
                    Choreographer.FrameTimeline[] frameTimelines;
                    int length;
                    if (FrameCallbackProvider33.this.mGetFramePeriodNsecs != null) {
                        try {
                            long jLongValue = ((Long) FrameCallbackProvider33.this.mGetFramePeriodNsecs.invoke(FrameCallbackProvider33.this.mChoreographer, null)).longValue();
                            if (jLongValue != -1) {
                                FrameCallbackProvider33.this.mFrameDeltaNanos = jLongValue;
                                try {
                                    FrameCallbackProvider33.this.mFromFramePeriodNsecs = true;
                                    z = true;
                                } catch (Exception unused2) {
                                    z = true;
                                    Log.w("OverScroller Animation", "onVsync getFramePeriodNSec failed");
                                }
                            } else {
                                z = false;
                            }
                        } catch (Exception unused3) {
                            z = false;
                        }
                    } else {
                        z = false;
                    }
                    if (z || (length = (frameTimelines = frameData.getFrameTimelines()).length) <= 1) {
                        return;
                    }
                    int i = length - 1;
                    FrameCallbackProvider33.this.mFrameDeltaNanos = Math.round(((frameTimelines[i].getExpectedPresentationTimeNanos() - frameTimelines[0].getExpectedPresentationTimeNanos()) * 1.0d) / ((double) i));
                }
            };
            this.mChoreographerCallback = new Choreographer.FrameCallback() { // from class: miuix.overscroller.internal.dynamicanimation.animation.AnimationHandler.FrameCallbackProvider33.2
                @Override // android.view.Choreographer.FrameCallback
                public void doFrame(long j) {
                    FrameCallbackProvider33.this.mDispatcher.dispatchAnimationFrame(j);
                }
            };
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.AnimationHandler.AnimationFrameCallbackProvider
        void postFrameCallback() {
            this.mChoreographer.postVsyncCallback(this.mVsyncCallback);
            this.mChoreographer.postFrameCallback(this.mChoreographerCallback);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.AnimationHandler.AnimationFrameCallbackProvider
        public void postVsyncCallback() {
            this.mChoreographer.postVsyncCallback(this.mVsyncCallback);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.AnimationHandler.AnimationFrameCallbackProvider
        boolean isCurrentThread() {
            return Thread.currentThread() == this.mLooper.getThread();
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.AnimationHandler.AnimationFrameCallbackProvider
        long getFrameDeltaNanos() {
            return this.mFrameDeltaNanos;
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.AnimationHandler.AnimationFrameCallbackProvider
        boolean getFromFramePeriodNsecs() {
            return this.mFromFramePeriodNsecs;
        }
    }

    public abstract class AnimationFrameCallbackProvider {
        final AnimationCallbackDispatcher mDispatcher;

        abstract long getFrameDeltaNanos();

        abstract boolean getFromFramePeriodNsecs();

        abstract boolean isCurrentThread();

        abstract void postFrameCallback();

        abstract void postVsyncCallback();

        AnimationFrameCallbackProvider(AnimationCallbackDispatcher animationCallbackDispatcher) {
            this.mDispatcher = animationCallbackDispatcher;
        }
    }
}
