package miuix.springback.view;

import miuix.view.animation.AnimationUtils;

/* JADX INFO: loaded from: classes2.dex */
public class SpringOverScroller {
    private double mCurrX;
    private double mCurrY;
    private long mCurrentTimeNanos;
    private boolean mFinished = true;
    private int mFirstStep;
    private boolean mLastStep;
    private int mOrientation;
    private SpringOperator mSpringOperator;
    private long mStartTimeNanos;
    private double mStartX;
    private double mStartY;
    private double mVelocity;

    public void startOverScroll(float f, float f2, float f3, int i) {
        this.mFinished = false;
        this.mLastStep = false;
        this.mStartX = f;
        double d = f2;
        this.mStartY = d;
        this.mCurrY = (int) d;
        double d2 = f3;
        this.mVelocity = d2;
        if (Math.abs(d2) <= 5000.0d) {
            this.mSpringOperator = new SpringOperator(1.0f, 0.4f);
        } else {
            this.mSpringOperator = new SpringOperator(1.0f, 0.55f);
        }
        this.mOrientation = i;
        this.mStartTimeNanos = AnimationUtils.currentAnimationTimeNanos();
    }

    public boolean computeScrollOffset() {
        if (this.mSpringOperator == null || this.mFinished) {
            return false;
        }
        if (this.mFirstStep != 0) {
            handleFirstStep();
            return true;
        }
        if (this.mLastStep) {
            handleLastStep();
            return true;
        }
        updateDeltaTime();
        if (this.mOrientation == 2) {
            return handleVerticalScroll();
        }
        return handleHorizontalScroll();
    }

    private void handleFirstStep() {
        int i = this.mOrientation;
        int i2 = this.mFirstStep;
        if (i == 1) {
            setHorizontalPosition(i2);
        } else {
            setVerticalPosition(i2);
        }
        resetFirstStep();
    }

    private void setHorizontalPosition(int i) {
        double d = i;
        this.mCurrX = d;
        this.mStartX = d;
    }

    private void setVerticalPosition(int i) {
        double d = i;
        this.mCurrY = d;
        this.mStartY = d;
    }

    private void resetFirstStep() {
        this.mFirstStep = 0;
    }

    private void handleLastStep() {
        this.mFinished = true;
    }

    private void updateDeltaTime() {
        this.mCurrentTimeNanos = AnimationUtils.currentAnimationTimeNanos();
    }

    private double calculateDelta() {
        double dMin = Math.min((this.mCurrentTimeNanos - this.mStartTimeNanos) / 1.0E9d, 0.01600000075995922d);
        double d = dMin != 0.0d ? dMin : 0.01600000075995922d;
        this.mStartTimeNanos = this.mCurrentTimeNanos;
        return d;
    }

    private boolean handleVerticalScroll() {
        double dCalculateDelta = calculateDelta();
        double dComputeCurrentVelocity = computeCurrentVelocity((float) this.mVelocity, (float) dCalculateDelta);
        double d = this.mStartY + (dCalculateDelta * dComputeCurrentVelocity);
        this.mCurrY = d;
        this.mCurrY = clampOverscrollDistance(d);
        this.mVelocity = dComputeCurrentVelocity;
        updateVerticalState();
        return true;
    }

    private boolean handleHorizontalScroll() {
        double dCalculateDelta = calculateDelta();
        double dComputeCurrentVelocity = computeCurrentVelocity((float) this.mVelocity, (float) dCalculateDelta);
        double d = this.mStartX + (dCalculateDelta * dComputeCurrentVelocity);
        this.mCurrX = d;
        this.mCurrX = clampOverscrollDistance(d);
        this.mVelocity = dComputeCurrentVelocity;
        updateHorizontalState();
        return true;
    }

    private double clampOverscrollDistance(double d) {
        if (Math.abs(d) <= 600.0d) {
            return d;
        }
        this.mLastStep = true;
        return Math.signum(d) * 600.0d;
    }

    private void updateVerticalState() {
        if (Math.abs(this.mVelocity) <= 2000.0d) {
            this.mLastStep = true;
        } else {
            this.mStartY = this.mCurrY;
        }
    }

    private void updateHorizontalState() {
        if (Math.abs(this.mVelocity) <= 2000.0d) {
            this.mLastStep = true;
        } else {
            this.mStartX = this.mCurrX;
        }
    }

    private float computeCurrentVelocity(float f, float f2) {
        return (f > 0.0f ? 1 : -1) * Math.abs(f) * Math.max(0.0f, Math.min(1.0f, 1.0f - (f2 * 18.0f)));
    }

    public final int getCurrX() {
        return (int) this.mCurrX;
    }

    public final int getCurrY() {
        return (int) this.mCurrY;
    }

    public final boolean isFinished() {
        return this.mFinished;
    }

    public final void forceStop() {
        this.mFinished = true;
        this.mFirstStep = 0;
    }
}
