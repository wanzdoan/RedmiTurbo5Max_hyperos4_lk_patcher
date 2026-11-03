package miuix.overscroller.internal.dynamicanimation.animation;

import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public final class FlingAnimation extends DynamicAnimation {
    private FinalValueListener mFinalValueListener;
    private final DragForce mFlingForce;

    public interface FinalValueListener {
        void onFinalValueArrived(int i);
    }

    public FlingAnimation(FloatValueHolder floatValueHolder, FinalValueListener finalValueListener) {
        super(floatValueHolder);
        DragForce dragForce = new DragForce();
        this.mFlingForce = dragForce;
        dragForce.setValueThreshold(getValueThreshold());
        this.mFinalValueListener = finalValueListener;
    }

    public FlingAnimation setFriction(float f) {
        if (f <= 0.0f) {
            Folme$$ExternalSyntheticBUOutline0.m("Friction must be positive");
            return null;
        }
        this.mFlingForce.setFrictionScalar(f);
        return this;
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation
    public FlingAnimation setMinValue(float f) {
        super.setMinValue(f);
        return this;
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation
    public FlingAnimation setMaxValue(float f) {
        super.setMaxValue(f);
        return this;
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation
    public FlingAnimation setStartVelocity(float f) {
        super.setStartVelocity(f);
        return this;
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation
    boolean updateValueAndVelocity(long j) {
        DynamicAnimation.MassState massStateUpdateValueAndVelocity = this.mFlingForce.updateValueAndVelocity(this.mValue, this.mVelocity, j);
        float f = massStateUpdateValueAndVelocity.mValue;
        this.mValue = f;
        float f2 = massStateUpdateValueAndVelocity.mVelocity;
        this.mVelocity = f2;
        float f3 = this.mMinValue;
        if (f < f3) {
            this.mValue = f3;
            return true;
        }
        float f4 = this.mMaxValue;
        if (f > f4) {
            this.mValue = f4;
            return true;
        }
        if (!isAtEquilibrium(f, f2)) {
            return false;
        }
        this.mFinalValueListener.onFinalValueArrived((int) this.mValue);
        return true;
    }

    boolean isAtEquilibrium(float f, float f2) {
        return f >= this.mMaxValue || f <= this.mMinValue || this.mFlingForce.isAtEquilibrium(f, f2);
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation
    void setValueThreshold(float f) {
        this.mFlingForce.setValueThreshold(f);
    }

    public float predictNaturalDest() {
        return (this.mValue - (this.mVelocity / this.mFlingForce.mFriction)) + ((Math.signum(this.mVelocity) * this.mFlingForce.mVelocityThreshold) / this.mFlingForce.mFriction);
    }

    public float predictTimeTo(float f) {
        return predictTimeWithVelocity(((f - this.mValue) + (this.mVelocity / this.mFlingForce.mFriction)) * this.mFlingForce.mFriction);
    }

    public float predictDuration() {
        return predictTimeWithVelocity(Math.signum(this.mVelocity) * this.mFlingForce.mVelocityThreshold);
    }

    private float predictTimeWithVelocity(float f) {
        return (float) ((Math.log(f / this.mVelocity) * 1000.0d) / ((double) this.mFlingForce.mFriction));
    }

    final class DragForce {
        private double mDragRate;
        private float mVelocityThreshold;
        private float mFriction = -4.2f;
        private final DynamicAnimation.MassState mMassState = new DynamicAnimation.MassState();
        private final double NANOSECONDS_PER_SECOND = 1.0E9d;

        DragForce() {
        }

        void setFrictionScalar(float f) {
            float f2 = f * (-4.2f);
            this.mFriction = f2;
            this.mDragRate = 1.0d - Math.pow(2.718281828459045d, f2);
        }

        DynamicAnimation.MassState updateValueAndVelocity(float f, float f2, long j) {
            double d = j / 1.0E9d;
            double dPow = Math.pow(1.0d - this.mDragRate, d);
            DynamicAnimation.MassState massState = this.mMassState;
            float f3 = (float) (((double) f2) * dPow);
            massState.mVelocity = f3;
            float f4 = (float) (((double) f) + (((double) f3) * d));
            massState.mValue = f4;
            if (isAtEquilibrium(f4, f3)) {
                this.mMassState.mVelocity = 0.0f;
            }
            return this.mMassState;
        }

        public boolean isAtEquilibrium(float f, float f2) {
            return Math.abs(f2) < this.mVelocityThreshold;
        }

        void setValueThreshold(float f) {
            this.mVelocityThreshold = f * 62.5f;
        }
    }
}
