package miuix.animation.physics;

import android.util.AndroidRuntimeException;
import java.util.ArrayList;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.motion.MergeMotion$$ExternalSyntheticBUOutline0;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.FloatValueHolder;
import miuix.animation.property.ViewProperty;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DynamicAnimation implements AnimationHandler.AnimationFrameCallback {
    public static final float MIN_VISIBLE_CHANGE_ALPHA = 0.00390625f;
    public static final float MIN_VISIBLE_CHANGE_PIXELS = 1.0f;
    public static final float MIN_VISIBLE_CHANGE_ROTATION_DEGREES = 0.1f;
    public static final float MIN_VISIBLE_CHANGE_SCALE = 0.002f;
    private static final float THRESHOLD_MULTIPLIER = 0.75f;
    private static final float UNSET = Float.MAX_VALUE;
    private final ArrayList mEndListeners;
    private long mLastFrameTimeNanos;
    float mMaxValue;
    float mMinValue;
    private float mMinVisibleChange;
    final FloatProperty mProperty;
    boolean mRunning;
    private long mStartDelay;
    private boolean mStartImmediately;
    private final ArrayList mStartListeners;
    boolean mStartValueIsSet;
    final Object mTarget;
    private final ArrayList mUpdateListeners;
    float mValue;
    float mVelocity;

    public interface OnAnimationEndListener {
        void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2);
    }

    public interface OnAnimationStartListener {
        void onAnimationStart(DynamicAnimation dynamicAnimation, float f, float f2);
    }

    public interface OnAnimationUpdateListener {
        void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2);
    }

    abstract float getAcceleration(float f, float f2);

    abstract boolean isAtEquilibrium(float f, float f2);

    abstract void setValueThreshold(float f);

    abstract boolean updateValueAndVelocity(long j);

    class MassState {
        float mValue;
        float mVelocity;

        MassState() {
        }
    }

    DynamicAnimation(final FloatValueHolder floatValueHolder) {
        this.mVelocity = 0.0f;
        this.mValue = UNSET;
        this.mStartValueIsSet = false;
        this.mRunning = false;
        this.mMaxValue = UNSET;
        this.mMinValue = -UNSET;
        this.mLastFrameTimeNanos = 0L;
        this.mStartDelay = 0L;
        this.mStartImmediately = false;
        this.mStartListeners = new ArrayList();
        this.mEndListeners = new ArrayList();
        this.mUpdateListeners = new ArrayList();
        this.mTarget = null;
        this.mProperty = new FloatProperty("FloatValueHolder") { // from class: miuix.animation.physics.DynamicAnimation.1
            @Override // miuix.animation.property.FloatProperty
            public float getValue(Object obj) {
                return floatValueHolder.getValue();
            }

            @Override // miuix.animation.property.FloatProperty
            public void setValue(Object obj, float f) {
                floatValueHolder.setValue(f);
            }
        };
        this.mMinVisibleChange = 1.0f;
    }

    DynamicAnimation(Object obj, FloatProperty floatProperty) {
        this.mVelocity = 0.0f;
        this.mValue = UNSET;
        this.mStartValueIsSet = false;
        this.mRunning = false;
        this.mMaxValue = UNSET;
        this.mMinValue = -UNSET;
        this.mLastFrameTimeNanos = 0L;
        this.mStartDelay = 0L;
        this.mStartImmediately = false;
        this.mStartListeners = new ArrayList();
        this.mEndListeners = new ArrayList();
        this.mUpdateListeners = new ArrayList();
        this.mTarget = obj;
        this.mProperty = floatProperty;
        if (floatProperty == ViewProperty.ROTATION || floatProperty == ViewProperty.ROTATION_X || floatProperty == ViewProperty.ROTATION_Y) {
            this.mMinVisibleChange = 0.1f;
            return;
        }
        if (floatProperty == ViewProperty.ALPHA) {
            this.mMinVisibleChange = 0.00390625f;
        } else if (floatProperty == ViewProperty.SCALE_X || floatProperty == ViewProperty.SCALE_Y) {
            this.mMinVisibleChange = 0.002f;
        } else {
            this.mMinVisibleChange = 1.0f;
        }
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

    public void setStartDelay(long j) {
        if (j < 0) {
            j = 0;
        }
        this.mStartDelay = j;
    }

    public DynamicAnimation addStartListener(OnAnimationStartListener onAnimationStartListener) {
        if (!this.mStartListeners.contains(onAnimationStartListener)) {
            this.mStartListeners.add(onAnimationStartListener);
        }
        return this;
    }

    public void removeStartListener(OnAnimationStartListener onAnimationStartListener) {
        removeEntry(this.mStartListeners, onAnimationStartListener);
    }

    public DynamicAnimation addEndListener(OnAnimationEndListener onAnimationEndListener) {
        if (!this.mEndListeners.contains(onAnimationEndListener)) {
            this.mEndListeners.add(onAnimationEndListener);
        }
        return this;
    }

    public void removeEndListener(OnAnimationEndListener onAnimationEndListener) {
        removeEntry(this.mEndListeners, onAnimationEndListener);
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

    public float getMinimumVisibleChange() {
        return this.mMinVisibleChange;
    }

    public void enableStartImmediately(boolean z) {
        this.mStartImmediately = z;
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

    public void start() {
        if (!getAnimationHandler().isCurrentThread()) {
            throw new AndroidRuntimeException("Animations may only be started on the same thread as the animation handler");
        }
        if (this.mRunning) {
            return;
        }
        startAnimationInternal();
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

    private void startAnimationInternal() {
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
            AnimationHandler.getInstance().addAnimationFrameCallback(this, this.mStartDelay);
        }
    }

    @Override // miuix.animation.physics.AnimationHandler.AnimationFrameCallback
    public boolean doAnimationFrame(long j) {
        long frameDeltaNanos = AnimationHandler.getInstance().getFrameDeltaNanos();
        long j2 = this.mLastFrameTimeNanos;
        if (j2 != 0 || this.mStartImmediately) {
            if (frameDeltaNanos == 0) {
                frameDeltaNanos = j - j2;
            }
            this.mLastFrameTimeNanos = j;
            boolean zUpdateValueAndVelocity = updateValueAndVelocity(frameDeltaNanos);
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
        int i = 0;
        while (true) {
            int size = this.mStartListeners.size();
            ArrayList arrayList = this.mStartListeners;
            if (i < size) {
                if (arrayList.get(i) != null) {
                    ((OnAnimationStartListener) this.mStartListeners.get(i)).onAnimationStart(this, this.mValue, this.mVelocity);
                }
                i++;
            } else {
                removeNullEntries(arrayList);
                this.mLastFrameTimeNanos = j;
                setPropertyValue(this.mValue);
                return false;
            }
        }
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
                    ((OnAnimationEndListener) this.mEndListeners.get(i)).onAnimationEnd(this, z, this.mValue, this.mVelocity);
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
