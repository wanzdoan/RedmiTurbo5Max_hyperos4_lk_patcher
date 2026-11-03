package miuix.animation.physics;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public final class SpringStepForce extends SpringForce {
    @Override // miuix.animation.physics.SpringForce
    protected void init() {
        if (this.mInitialized) {
            return;
        }
        if (this.mFinalPosition == Double.MAX_VALUE) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Error: Final position of the spring must be set before the miuix.animation starts");
        } else {
            this.mDampedFreq = Math.min(this.mNaturalFreq * 2.0d * this.mDampingRatio, 60.0d);
            this.mInitialized = true;
        }
    }

    @Override // miuix.animation.physics.SpringForce
    DynamicAnimation.MassState updateValues(double d, double d2, long j) {
        init();
        double d3 = j / 1.0E9d;
        double stiffness = ((1.0d - (this.mDampedFreq * d3)) * d2) + (((double) getStiffness()) * (this.mFinalPosition - d) * d3);
        DynamicAnimation.MassState massState = this.mMassState;
        massState.mValue = (float) (d + ((d2 + stiffness) * 0.5d * d3));
        massState.mVelocity = (float) stiffness;
        return massState;
    }
}
