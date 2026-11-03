package miuix.overscroller.internal.dynamicanimation.animation;

import android.util.AndroidRuntimeException;
import android.view.View;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.ArrayList;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.internal.FolmeCore;
import miuix.animation.internal.FolmeEngine;
import miuix.animation.motion.MergeMotion$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DynamicAnimation implements AnimationHandler.AnimationFrameCallback {
    final FloatPropertyCompat mProperty;
    public static final ViewProperty TRANSLATION_X = new ViewProperty("translationX") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.1
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationX(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationX();
        }
    };
    public static final ViewProperty TRANSLATION_Y = new ViewProperty("translationY") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.2
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationY(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationY();
        }
    };
    public static final ViewProperty TRANSLATION_Z = new ViewProperty("translationZ") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.3
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationZ(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationZ();
        }
    };
    public static final ViewProperty SCALE_X = new ViewProperty("scaleX") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.4
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScaleX(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScaleX();
        }
    };
    public static final ViewProperty SCALE_Y = new ViewProperty("scaleY") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.5
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScaleY(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScaleY();
        }
    };
    public static final ViewProperty ROTATION = new ViewProperty("rotation") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.6
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotation(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotation();
        }
    };
    public static final ViewProperty ROTATION_X = new ViewProperty("rotationX") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.7
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotationX(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotationX();
        }
    };
    public static final ViewProperty ROTATION_Y = new ViewProperty("rotationY") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.8
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotationY(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotationY();
        }
    };
    public static final ViewProperty X = new ViewProperty("x") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.9
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setX(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getX();
        }
    };
    public static final ViewProperty Y = new ViewProperty("y") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.10
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setY(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getY();
        }
    };
    public static final ViewProperty Z = new ViewProperty("z") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.11
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setZ(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getZ();
        }
    };
    public static final ViewProperty ALPHA = new ViewProperty("alpha") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.12
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setAlpha(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getAlpha();
        }
    };
    public static final ViewProperty SCROLL_X = new ViewProperty("scrollX") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.13
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScrollX((int) f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScrollX();
        }
    };
    public static final ViewProperty SCROLL_Y = new ViewProperty("scrollY") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.14
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScrollY((int) f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScrollY();
        }
    };
    float mVelocity = 0.0f;
    float mValue = Float.MAX_VALUE;
    boolean mStartValueIsSet = false;
    boolean mRunning = false;
    float mMaxValue = Float.MAX_VALUE;
    float mMinValue = -Float.MAX_VALUE;
    private long mLastFrameTimeNanos = 0;
    private long mStartDelay = 0;
    private final ArrayList mEndListeners = new ArrayList();
    private final ArrayList mUpdateListeners = new ArrayList();
    final Object mTarget = null;
    private float mMinVisibleChange = 1.0f;

    public interface OnAnimationUpdateListener {
        void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2);
    }

    abstract void setValueThreshold(float f);

    abstract boolean updateValueAndVelocity(long j);

    public abstract class ViewProperty extends FloatPropertyCompat {
        private ViewProperty(String str) {
            super(str);
        }
    }

    class MassState {
        float mValue;
        float mVelocity;

        MassState() {
        }
    }

    DynamicAnimation(final FloatValueHolder floatValueHolder) {
        this.mProperty = new FloatPropertyCompat("FloatValueHolder") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.15
            @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
            public float getValue(Object obj) {
                return floatValueHolder.getValue();
            }

            @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
            public void setValue(Object obj, float f) {
                floatValueHolder.setValue(f);
            }
        };
    }

    public DynamicAnimation setStartValue(float f) {
        this.mValue = f;
        this.mStartValueIsSet = true;
        return this;
    }

    public DynamicAnimation setStartVelocity(float f) {
        this.mVelocity = f;
        return this;
    }

    public DynamicAnimation setMaxValue(float f) {
        this.mMaxValue = f;
        return this;
    }

    public DynamicAnimation setMinValue(float f) {
        this.mMinValue = f;
        return this;
    }

    public DynamicAnimation addUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        if (isRunning()) {
            MergeMotion$$ExternalSyntheticBUOutline0.m("Error: Update listeners must be added beforethe miuix.animation.");
            return null;
        }
        if (!this.mUpdateListeners.contains(onAnimationUpdateListener)) {
            this.mUpdateListeners.add(onAnimationUpdateListener);
        }
        return this;
    }

    public void removeUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        removeEntry(this.mUpdateListeners, onAnimationUpdateListener);
    }

    public DynamicAnimation setMinimumVisibleChange(float f) {
        if (f <= 0.0f) {
            Folme$$ExternalSyntheticBUOutline0.m("Minimum visible change must be positive.");
            return null;
        }
        this.mMinVisibleChange = f;
        setValueThreshold(f * 0.75f);
        return this;
    }

    private static void removeNullEntries(ArrayList arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size) == null) {
                arrayList.remove(size);
            }
        }
    }

    private static void removeEntry(ArrayList arrayList, Object obj) {
        int iIndexOf = arrayList.indexOf(obj);
        if (iIndexOf >= 0) {
            arrayList.set(iIndexOf, null);
        }
    }

    public void start(boolean z) {
        if (!getAnimationHandler().isCurrentThread()) {
            throw new AndroidRuntimeException("Animations may only be started on the main thread");
        }
        if (this.mRunning) {
            return;
        }
        startAnimationInternal(z);
    }

    public void cancel() {
        if (!getAnimationHandler().isCurrentThread()) {
            throw new AndroidRuntimeException("Animations may only be canceled from the same thread as the animation handler");
        }
        if (this.mRunning) {
            endAnimationInternal(true);
        }
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    private void startAnimationInternal(boolean z) {
        if (this.mRunning) {
            return;
        }
        this.mRunning = true;
        if (!this.mStartValueIsSet) {
            this.mValue = getPropertyValue();
        }
        float f = this.mValue;
        if (f > this.mMaxValue || f < this.mMinValue) {
            Folme$$ExternalSyntheticBUOutline0.m("Starting value need to be in between min value and max value");
        } else {
            if (z) {
                return;
            }
            AnimationHandler.getInstance().addAnimationFrameCallback(this, this.mStartDelay);
        }
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.AnimationHandler.AnimationFrameCallback
    public boolean doAnimationFrame(long j) {
        long jMin;
        long frameDeltaNanos = AnimationHandler.getInstance().getFrameDeltaNanos();
        boolean fromFramePeriodNsecs = AnimationHandler.getInstance().getFromFramePeriodNsecs();
        long j2 = this.mLastFrameTimeNanos;
        if (j2 == 0) {
            this.mLastFrameTimeNanos = j;
            setPropertyValue(this.mValue);
            return false;
        }
        if (frameDeltaNanos == 0) {
            frameDeltaNanos = j - j2;
        }
        if (!fromFramePeriodNsecs || frameDeltaNanos == 0) {
            jMin = Math.min(frameDeltaNanos, FolmeEngine.MAX_DELTA);
        } else {
            jMin = Math.min(frameDeltaNanos, FolmeCore.NANOS_TO_S);
        }
        this.mLastFrameTimeNanos = j;
        boolean zUpdateValueAndVelocity = updateValueAndVelocity(jMin);
        float fMin = Math.min(this.mValue, this.mMaxValue);
        this.mValue = fMin;
        float fMax = Math.max(fMin, this.mMinValue);
        this.mValue = fMax;
        setPropertyValue(fMax);
        if (zUpdateValueAndVelocity) {
            endAnimationInternal(false);
        }
        return zUpdateValueAndVelocity;
    }

    private void endAnimationInternal(boolean z) {
        int i = 0;
        this.mRunning = false;
        AnimationHandler.getInstance().removeCallback(this);
        this.mLastFrameTimeNanos = 0L;
        this.mStartValueIsSet = false;
        while (true) {
            int size = this.mEndListeners.size();
            ArrayList arrayList = this.mEndListeners;
            if (i < size) {
                if (arrayList.get(i) != null) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.mEndListeners.get(i));
                    throw null;
                }
                i++;
            } else {
                removeNullEntries(arrayList);
                return;
            }
        }
    }

    void setPropertyValue(float f) {
        this.mProperty.setValue(this.mTarget, f);
        int i = 0;
        while (true) {
            int size = this.mUpdateListeners.size();
            ArrayList arrayList = this.mUpdateListeners;
            if (i < size) {
                if (arrayList.get(i) != null) {
                    ((OnAnimationUpdateListener) this.mUpdateListeners.get(i)).onAnimationUpdate(this, this.mValue, this.mVelocity);
                }
                i++;
            } else {
                removeNullEntries(arrayList);
                return;
            }
        }
    }

    float getValueThreshold() {
        return this.mMinVisibleChange * 0.75f;
    }

    private float getPropertyValue() {
        return this.mProperty.getValue(this.mTarget);
    }

    public AnimationHandler getAnimationHandler() {
        return AnimationHandler.getInstance();
    }
}
