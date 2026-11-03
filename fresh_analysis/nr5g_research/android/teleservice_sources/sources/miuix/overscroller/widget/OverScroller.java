package miuix.overscroller.widget;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import miuix.animation.internal.AnimData;
import miuix.animation.physics.SpringOperator;
import miuix.view.animation.AnimationUtils;

/* JADX INFO: loaded from: classes2.dex */
public class OverScroller {
    private final boolean mFlywheel;
    private Interpolator mInterpolator;
    private int mMode;
    private final SplineOverScroller mScrollerX;
    private final SplineOverScroller mScrollerY;

    public OverScroller(Context context) {
        this(context, null);
    }

    public OverScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    public OverScroller(Context context, Interpolator interpolator, boolean z) {
        if (interpolator == null) {
            this.mInterpolator = new ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
        this.mFlywheel = z;
        this.mScrollerX = new DynamicScroller(context);
        this.mScrollerY = new DynamicScroller(context);
    }

    public int getMode() {
        return this.mMode;
    }

    public final boolean isFinished() {
        return this.mScrollerX.mFinished && this.mScrollerY.mFinished;
    }

    public final void forceFinished(boolean z) {
        this.mScrollerX.mFinished = this.mScrollerY.mFinished = z;
    }

    public final void resetPosition() {
        resetScrollerPosition(this.mScrollerX);
        resetScrollerPosition(this.mScrollerY);
    }

    private void resetScrollerPosition(SplineOverScroller splineOverScroller) {
        splineOverScroller.setStart(0);
        splineOverScroller.setFinal(0);
        splineOverScroller.setCurrentPosition(0);
    }

    public final int getCurrX() {
        return (int) this.mScrollerX.mCurrentPosition;
    }

    public final int getCurrY() {
        return (int) this.mScrollerY.mCurrentPosition;
    }

    public float getCurrVelocity() {
        return (float) Math.hypot(this.mScrollerX.mCurrVelocity, this.mScrollerY.mCurrVelocity);
    }

    public float getCurrVelocityX() {
        return (float) this.mScrollerX.mCurrVelocity;
    }

    public float getCurrVelocityY() {
        return (float) this.mScrollerY.mCurrVelocity;
    }

    public final int getStartX() {
        return (int) this.mScrollerX.mStart;
    }

    public final int getStartY() {
        return (int) this.mScrollerY.mStart;
    }

    public final int getFinalX() {
        return (int) this.mScrollerX.mFinal;
    }

    public final int getFinalY() {
        return (int) this.mScrollerY.mFinal;
    }

    public boolean computeScrollOffset() {
        if (isFinished()) {
            return false;
        }
        int i = this.mMode;
        if (i == 0) {
            long jCurrentAnimationTimeNanos = AnimationUtils.currentAnimationTimeNanos() - this.mScrollerX.mStartTime;
            int i2 = this.mScrollerX.mDuration;
            if (jCurrentAnimationTimeNanos < i2) {
                float interpolation = this.mInterpolator.getInterpolation(jCurrentAnimationTimeNanos / i2);
                this.mScrollerX.updateScroll(interpolation);
                this.mScrollerY.updateScroll(interpolation);
            } else {
                abortAnimation();
            }
        } else if (i == 1) {
            if (!this.mScrollerX.mFinished && !this.mScrollerX.update() && !this.mScrollerX.continueWhenFinished()) {
                this.mScrollerX.finish();
            }
            if (!this.mScrollerY.mFinished && !this.mScrollerY.update() && !this.mScrollerY.continueWhenFinished()) {
                this.mScrollerY.finish();
            }
        } else if (i == 2) {
            return this.mScrollerY.computeScrollOffset() || this.mScrollerX.computeScrollOffset();
        }
        return true;
    }

    public void startScrollByFling(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mMode = 2;
        this.mScrollerX.startScrollByFling(i, i3, i5);
        this.mScrollerY.startScrollByFling(i2, i4, i6);
    }

    public void startScroll(int i, int i2, int i3, int i4) {
        startScroll(i, i2, i3, i4, 250);
    }

    public void startScroll(int i, int i2, int i3, int i4, int i5) {
        this.mMode = 0;
        this.mScrollerX.startScroll(i, i3, i5);
        this.mScrollerY.startScroll(i2, i4, i5);
    }

    public boolean springBack(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mMode = 1;
        return this.mScrollerX.springback(i, i3, i4) || this.mScrollerY.springback(i2, i5, i6);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        fling(i, i2, i3, i4, i5, i6, i7, i8, 0, 0);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        if (this.mFlywheel && !isFinished()) {
            float f = (float) this.mScrollerX.mCurrVelocity;
            float f2 = (float) this.mScrollerY.mCurrVelocity;
            float f3 = i3;
            if (Math.signum(f3) == Math.signum(f)) {
                float f4 = i4;
                if (Math.signum(f4) == Math.signum(f2)) {
                    i3 = (int) (f3 + f);
                    i4 = (int) (f4 + f2);
                }
            }
        }
        this.mMode = 1;
        this.mScrollerX.fling(i, i3, i5, i6, i9);
        this.mScrollerY.fling(i2, i4, i7, i8, i10);
    }

    public void notifyHorizontalEdgeReached(int i, int i2, int i3) {
        this.mScrollerX.notifyEdgeReached(i, i2, i3);
    }

    public void notifyVerticalEdgeReached(int i, int i2, int i3) {
        this.mScrollerY.notifyEdgeReached(i, i2, i3);
    }

    public void abortAnimation() {
        this.mScrollerX.finish();
        this.mScrollerY.finish();
    }

    abstract class SplineOverScroller {
        private static float DECELERATION_RATE = (float) (Math.log(0.78d) / Math.log(0.9d));
        private static final float[] SPLINE_POSITION = new float[101];
        private static final float[] SPLINE_TIME = new float[101];
        private Context mContext;
        private double mCurrVelocity;
        private double mCurrentPosition;
        private float mDeceleration;
        private int mDuration;
        private double mFinal;
        private boolean mLastStep;
        private double mOriginStart;
        private float mPhysicalCoeff;
        private AnimData mSpringAnimData;
        private double[] mSpringFactor;
        private SpringOperator mSpringOperator;
        private double[] mSpringParams;
        private double mStart;
        private long mStartTime;
        private double mVelocity;
        private float mFlingFriction = ViewConfiguration.getScrollFriction();
        private int mState = 0;
        private boolean mFinished = true;

        abstract boolean continueWhenFinished();

        abstract void finish();

        abstract void fling(int i, int i2, int i3, int i4, int i5);

        abstract void notifyEdgeReached(int i, int i2, int i3);

        abstract boolean springback(int i, int i2, int i3);

        abstract boolean update();

        static {
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            float f8;
            float f9;
            float f10;
            float f11 = 0.0f;
            int i = 0;
            float f12 = 0.0f;
            while (true) {
                float f13 = 1.0f;
                if (i < 100) {
                    float f14 = i / 100.0f;
                    float f15 = 1.0f;
                    while (true) {
                        f = ((f15 - f11) / 2.0f) + f11;
                        f2 = f13 - f;
                        f3 = f * 3.0f * f2;
                        f4 = f * f * f;
                        float f16 = (((f2 * 0.175f) + (f * 0.35000002f)) * f3) + f4;
                        f5 = f13;
                        f6 = f14;
                        if (Math.abs(f16 - f14) < 1.0E-5d) {
                            break;
                        }
                        if (f16 > f6) {
                            f15 = f;
                        } else {
                            f11 = f;
                        }
                        f13 = f5;
                        f14 = f6;
                    }
                    SPLINE_POSITION[i] = (f3 * ((f2 * 0.5f) + f)) + f4;
                    float f17 = f5;
                    while (true) {
                        f7 = ((f17 - f12) / 2.0f) + f12;
                        f8 = f5 - f7;
                        f9 = f7 * 3.0f * f8;
                        f10 = f7 * f7 * f7;
                        float f18 = (((f8 * 0.5f) + f7) * f9) + f10;
                        if (Math.abs(f18 - f6) < 1.0E-5d) {
                            break;
                        } else if (f18 > f6) {
                            f17 = f7;
                        } else {
                            f12 = f7;
                        }
                    }
                    SPLINE_TIME[i] = (f9 * ((f8 * 0.175f) + (f7 * 0.35000002f))) + f10;
                    i++;
                } else {
                    float[] fArr = SPLINE_POSITION;
                    SPLINE_TIME[100] = 1.0f;
                    fArr[100] = 1.0f;
                    return;
                }
            }
        }

        SplineOverScroller(Context context) {
            this.mContext = context;
            this.mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
        }

        void updateScroll(float f) {
            double d = this.mStart;
            this.mCurrentPosition = d + Math.round(((double) f) * (this.mFinal - d));
        }

        void startScroll(int i, int i2, int i3) {
            this.mFinished = false;
            double d = i;
            this.mStart = d;
            this.mCurrentPosition = d;
            this.mFinal = i + i2;
            this.mStartTime = AnimationUtils.currentAnimationTimeNanos();
            this.mDuration = i3;
            this.mDeceleration = 0.0f;
            this.mVelocity = 0.0d;
        }

        void startScrollByFling(float f, int i, int i2) {
            this.mFinished = false;
            this.mLastStep = false;
            setState(0);
            double d = f;
            this.mOriginStart = d;
            this.mStart = d;
            this.mCurrentPosition = d;
            this.mFinal = f + i;
            this.mStartTime = AnimationUtils.currentAnimationTimeNanos();
            double d2 = i2;
            this.mVelocity = d2;
            this.mCurrVelocity = d2;
            this.mSpringOperator = new SpringOperator();
            this.mSpringFactor = new double[]{0.99d, 0.4d};
            AnimData animData = new AnimData();
            this.mSpringAnimData = animData;
            animData.startValue = this.mStart;
            animData.targetValue = this.mFinal;
            animData.value = this.mCurrentPosition;
            animData.velocity = this.mCurrVelocity;
            double[] dArr = new double[3];
            this.mSpringParams = dArr;
            this.mSpringOperator.getParameters(this.mSpringFactor, dArr);
        }

        boolean computeScrollOffset() {
            if (this.mSpringOperator == null || this.mFinished) {
                return false;
            }
            if (this.mLastStep) {
                this.mFinished = true;
                this.mCurrentPosition = this.mFinal;
                return true;
            }
            long jCurrentAnimationTimeNanos = AnimationUtils.currentAnimationTimeNanos();
            double dMin = Math.min((jCurrentAnimationTimeNanos - this.mStartTime) / 1.0E9d, 0.01600000075995922d);
            double d = dMin == 0.0d ? 0.01600000075995922d : dMin;
            this.mStartTime = jCurrentAnimationTimeNanos;
            AnimData animData = this.mSpringAnimData;
            animData.startValue = this.mStart;
            animData.targetValue = this.mFinal;
            animData.value = this.mCurrentPosition;
            double d2 = this.mSpringFactor[0];
            double[] dArr = this.mSpringParams;
            SpringOperator.updateValues(animData, d2, dArr[1], dArr[2], d, false);
            AnimData animData2 = this.mSpringAnimData;
            double d3 = animData2.value;
            this.mCurrentPosition = d3;
            this.mCurrVelocity = animData2.velocity;
            if (isAtEquilibrium(d3, this.mFinal)) {
                this.mLastStep = true;
            } else {
                this.mStart = this.mCurrentPosition;
            }
            return true;
        }

        boolean isAtEquilibrium(double d, double d2) {
            return Math.abs(d - d2) < 1.0d;
        }

        void setFinalPosition(int i) {
            this.mFinal = i;
            this.mFinished = false;
        }

        final boolean isFinished() {
            return this.mFinished;
        }

        final void setFinished(boolean z) {
            this.mFinished = z;
        }

        final int getCurrentPosition() {
            return (int) this.mCurrentPosition;
        }

        final void setCurrentPosition(int i) {
            this.mCurrentPosition = i;
        }

        final float getCurrVelocity() {
            return (float) this.mCurrVelocity;
        }

        final void setCurrVelocity(float f) {
            this.mCurrVelocity = f;
        }

        final int getStart() {
            return (int) this.mStart;
        }

        final void setStart(int i) {
            this.mStart = i;
        }

        final int getFinal() {
            return (int) this.mFinal;
        }

        final void setFinal(int i) {
            this.mFinal = i;
        }

        final void setDuration(int i) {
            this.mDuration = i;
        }

        final int getState() {
            return this.mState;
        }

        final void setState(int i) {
            this.mState = i;
        }

        final void setStartTime(long j) {
            this.mStartTime = j;
        }
    }

    class ViscousFluidInterpolator implements Interpolator {
        private static final float VISCOUS_FLUID_NORMALIZE;
        private static final float VISCOUS_FLUID_OFFSET;

        ViscousFluidInterpolator() {
        }

        static {
            float fViscousFluid = 1.0f / viscousFluid(1.0f);
            VISCOUS_FLUID_NORMALIZE = fViscousFluid;
            VISCOUS_FLUID_OFFSET = 1.0f - (fViscousFluid * viscousFluid(1.0f));
        }

        private static float viscousFluid(float f) {
            float f2 = f * 8.0f;
            if (f2 < 1.0f) {
                return f2 - (1.0f - ((float) Math.exp(-f2)));
            }
            return ((1.0f - ((float) Math.exp(1.0f - f2))) * 0.63212055f) + 0.36787945f;
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            float fViscousFluid = VISCOUS_FLUID_NORMALIZE * viscousFluid(f);
            return fViscousFluid > 0.0f ? fViscousFluid + VISCOUS_FLUID_OFFSET : fViscousFluid;
        }
    }
}
