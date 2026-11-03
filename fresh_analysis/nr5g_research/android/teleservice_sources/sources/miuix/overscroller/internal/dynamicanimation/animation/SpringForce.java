package miuix.overscroller.internal.dynamicanimation.animation;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public final class SpringForce {
    private double mDampedFreq;
    private double mGammaMinus;
    private double mGammaPlus;
    private double mValueThreshold;
    private double mVelocityThreshold;
    double mNaturalFreq = Math.sqrt(1500.0d);
    double mDampingRatio = 0.5d;
    double mTimeRatio = 1000.0d;
    private boolean mInitialized = false;
    private double mFinalPosition = Double.MAX_VALUE;
    private final DynamicAnimation.MassState mMassState = new DynamicAnimation.MassState();

    public SpringForce setStiffness(float f) {
        if (f <= 0.0f) {
            Folme$$ExternalSyntheticBUOutline0.m("Spring stiffness constant must be positive.");
            return null;
        }
        this.mNaturalFreq = Math.sqrt(f);
        this.mInitialized = false;
        return this;
    }

    public SpringForce setTimeRatio(double d) {
        this.mTimeRatio = d;
        return this;
    }

    public SpringForce setDampingRatio(float f) {
        if (f < 0.0f) {
            Folme$$ExternalSyntheticBUOutline0.m("Damping ratio must be non-negative");
            return null;
        }
        this.mDampingRatio = f;
        this.mInitialized = false;
        return this;
    }

    public SpringForce setFinalPosition(float f) {
        this.mFinalPosition = f;
        return this;
    }

    public float getFinalPosition() {
        return (float) this.mFinalPosition;
    }

    public boolean isAtEquilibrium(float f, float f2) {
        return ((double) Math.abs(f2)) < this.mVelocityThreshold && ((double) Math.abs(f - getFinalPosition())) < this.mValueThreshold;
    }

    private void init() {
        if (this.mInitialized) {
            return;
        }
        if (this.mFinalPosition == Double.MAX_VALUE) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Error: Final position of the spring must be set before the miuix.animation starts");
            return;
        }
        double d = this.mDampingRatio;
        if (d > 1.0d) {
            double d2 = this.mNaturalFreq;
            this.mGammaPlus = ((-d) * d2) + (d2 * Math.sqrt((d * d) - 1.0d));
            double d3 = this.mDampingRatio;
            double d4 = this.mNaturalFreq;
            this.mGammaMinus = ((-d3) * d4) - (d4 * Math.sqrt((d3 * d3) - 1.0d));
        } else if (d >= 0.0d && d < 1.0d) {
            this.mDampedFreq = this.mNaturalFreq * Math.sqrt(1.0d - (d * d));
        }
        this.mInitialized = true;
    }

    DynamicAnimation.MassState updateValues(double d, double d2, long j) {
        double dPow;
        double dCos;
        init();
        double d3 = j / 1.0E9d;
        double d4 = d - this.mFinalPosition;
        double d5 = this.mDampingRatio;
        if (d5 > 1.0d) {
            double d6 = this.mGammaMinus;
            double d7 = this.mGammaPlus;
            double d8 = d4 - (((d6 * d4) - d2) / (d6 - d7));
            double d9 = ((d4 * d6) - d2) / (d6 - d7);
            dPow = (Math.pow(2.718281828459045d, d6 * d3) * d8) + (Math.pow(2.718281828459045d, this.mGammaPlus * d3) * d9);
            double d10 = this.mGammaMinus;
            double dPow2 = d8 * d10 * Math.pow(2.718281828459045d, d10 * d3);
            double d11 = this.mGammaPlus;
            dCos = dPow2 + (d9 * d11 * Math.pow(2.718281828459045d, d11 * d3));
        } else if (d5 == 1.0d) {
            double d12 = this.mNaturalFreq;
            double d13 = d2 + (d12 * d4);
            double d14 = d4 + (d13 * d3);
            dPow = Math.pow(2.718281828459045d, (-d12) * d3) * d14;
            double dPow3 = d14 * Math.pow(2.718281828459045d, (-this.mNaturalFreq) * d3);
            double d15 = this.mNaturalFreq;
            dCos = (d13 * Math.pow(2.718281828459045d, (-d15) * d3)) + (dPow3 * (-d15));
        } else {
            double d16 = 1.0d / this.mDampedFreq;
            double d17 = this.mNaturalFreq;
            double d18 = d16 * ((d5 * d17 * d4) + d2);
            dPow = Math.pow(2.718281828459045d, (-d5) * d17 * d3) * ((Math.cos(this.mDampedFreq * d3) * d4) + (Math.sin(this.mDampedFreq * d3) * d18));
            double d19 = this.mNaturalFreq;
            double d20 = this.mDampingRatio;
            double dPow4 = Math.pow(2.718281828459045d, (-d20) * d19 * d3);
            double d21 = this.mDampedFreq;
            double dSin = (-d21) * d4 * Math.sin(d21 * d3);
            double d22 = this.mDampedFreq;
            dCos = ((-d19) * dPow * d20) + (dPow4 * (dSin + (d18 * d22 * Math.cos(d22 * d3))));
        }
        if (Math.abs(dPow) < 0.6000000238418579d) {
            dPow = 0.0d;
            dCos = 0.0d;
        }
        DynamicAnimation.MassState massState = this.mMassState;
        massState.mValue = (float) (dPow + this.mFinalPosition);
        massState.mVelocity = (float) dCos;
        return massState;
    }
}
