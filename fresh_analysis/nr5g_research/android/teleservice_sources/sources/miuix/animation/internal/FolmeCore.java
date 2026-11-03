package miuix.animation.internal;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.util.Log;
import miuix.animation.FolmeFactory;
import miuix.animation.IAnimTarget;
import miuix.animation.function.Differentiable;
import miuix.animation.motion.Motion;
import miuix.animation.physics.AccelerateOperator;
import miuix.animation.physics.EquilibriumChecker;
import miuix.animation.physics.FrictionOperator;
import miuix.animation.physics.PhysicsOperator;
import miuix.animation.physics.SpringOperator;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.EaseManager;
import miuix.animation.utils.LogUtils;

/* JADX INFO: loaded from: classes2.dex */
public class FolmeCore {
    private static final long LONGEST_DURATION_NANOS = 10000000000L;
    public static final long NANOS_TO_MS = 1000000;
    public static final long NANOS_TO_S = 1000000000;
    static final SpringOperator sSpring = new SpringOperator();
    static final AccelerateOperator sAccelerate = new AccelerateOperator();
    static final FrictionOperator sFriction = new FrictionOperator();
    static IntEvaluator sIntEvaluator = new IntEvaluator();
    static FloatEvaluator sFloatEvaluator = new FloatEvaluator();
    static final ThreadLocal mCheckerLocal = new ThreadLocal();

    private static float regulateProgress(float f) {
        if (f > 1.0f) {
            return 1.0f;
        }
        if (f < 0.0f) {
            return 0.0f;
        }
        return f;
    }

    public static void doAnimationFrame(IAnimTarget iAnimTarget, boolean z, AnimData animData, long j, double d, int i) {
        long j2 = j - animData.startTime;
        if (EaseManager.isPhysicsStyle(animData.ease.style)) {
            updatePhysicsAnim(iAnimTarget, animData, z, j2, d, i);
            return;
        }
        if (EaseManager.isPhysicsMotionStyle(animData.ease.style)) {
            updatePhysicsMotionAnim(animData, j2);
            return;
        }
        EaseManager.EaseStyle easeStyle = animData.ease;
        if (easeStyle instanceof EaseManager.InterpolateEaseStyle) {
            updateInterpolatorAnim(animData, z, j2 / NANOS_TO_MS);
        } else if (EaseManager.isDurationMotionStyle(easeStyle.style)) {
            updateDurationMotionAnim(animData, z, j2);
        }
    }

    @Deprecated
    private static void updateInterpolatorAnim(AnimData animData, boolean z, long j) {
        EaseManager.InterpolateEaseStyle interpolateEaseStyle = (EaseManager.InterpolateEaseStyle) animData.ease;
        TimeInterpolator interpolator = EaseManager.getInterpolator(interpolateEaseStyle);
        long j2 = interpolateEaseStyle.duration;
        if (j < j2) {
            animData.progress = interpolator.getInterpolation(j / j2);
        } else {
            animData.setOp((byte) 3);
            animData.progress = 1.0d;
        }
        if (z) {
            animData.value = ((Integer) CommonUtils.sArgbEvaluator.evaluate((float) animData.progress, Integer.valueOf((int) animData.startValue), Integer.valueOf((int) animData.targetValue))).doubleValue();
        } else {
            animData.value = evaluateValue(animData, (float) animData.progress);
        }
        if (Double.isInfinite(animData.value)) {
            Log.e(CommonUtils.TAG, "updateInterpolatorAnim data.value isInfinite!  data.ease " + interpolateEaseStyle + " totalTime_ms " + j + " interpolator " + interpolator + " data.progress " + animData.progress);
        }
    }

    private static double evaluateValue(AnimData animData, float f) {
        TypeEvaluator evaluator = getEvaluator(animData.property);
        if (evaluator instanceof IntEvaluator) {
            return ((IntEvaluator) evaluator).evaluate(f, Integer.valueOf((int) animData.startValue), Integer.valueOf((int) animData.targetValue)).doubleValue();
        }
        return ((FloatEvaluator) evaluator).evaluate(f, (Number) Float.valueOf((float) animData.startValue), (Number) Float.valueOf((float) animData.targetValue)).doubleValue();
    }

    private static TypeEvaluator getEvaluator(FloatProperty floatProperty) {
        if (floatProperty instanceof IIntValueProperty) {
            return sIntEvaluator;
        }
        return sFloatEvaluator;
    }

    private static void updatePhysicsAnim(IAnimTarget iAnimTarget, AnimData animData, boolean z, long j, double d, int i) {
        FolmeEngine engine = FolmeFactory.getEngine();
        if (engine == null) {
            return;
        }
        EquilibriumChecker equilibriumChecker = (EquilibriumChecker) CommonUtils.getLocal(engine.getObjPool(), mCheckerLocal, EquilibriumChecker.class);
        if (animData.property.getMinVisibleChange() == -1.0f) {
            FloatProperty floatProperty = animData.property;
            floatProperty.setMinVisibleChange(iAnimTarget.getMinVisibleChange(floatProperty.getName()));
        }
        equilibriumChecker.init(animData.property, animData.targetValue);
        for (int i2 = 0; i2 < i; i2++) {
            if (z) {
                doArgbPhysicsCalculation(animData, d);
            } else {
                doPhysicsCalculation(animData, d);
            }
            if (!isAnimRunning(equilibriumChecker, animData.property, animData.ease.style, animData.value, animData.velocity, j)) {
                animData.setOp((byte) 3);
                if (LogUtils.isLogMoreEnable()) {
                    LogUtils.debug("----- updatePhysicsAnim data.setOp(AnimTask.OP_END)", new Object[0]);
                }
                setFinishValue(animData);
                return;
            }
        }
    }

    private static void updateDurationMotionAnim(AnimData animData, boolean z, long j) {
        EaseManager.DurationMotionEaseStyle durationMotionEaseStyle = (EaseManager.DurationMotionEaseStyle) animData.ease;
        double d = j / (durationMotionEaseStyle.factors[0] * 1000000.0d);
        Motion durationMotion = EaseManager.getDurationMotion(durationMotionEaseStyle);
        if (d > 1.0d) {
            animData.progress = durationMotion.stopPosition();
            animData.value = animData.targetValue;
            animData.velocity = durationMotion.stopSpeed();
            animData.setOp((byte) 3);
        } else {
            Differentiable differentiableSolve = durationMotion.solve();
            double dApply = differentiableSolve.apply(d);
            animData.progress = dApply;
            if (z) {
                animData.value = ((Integer) CommonUtils.sArgbEvaluator.evaluate((float) dApply, Integer.valueOf((int) animData.startValue), Integer.valueOf((int) animData.targetValue))).doubleValue();
            } else {
                double d2 = animData.startValue;
                animData.value = d2 + ((animData.targetValue - d2) * dApply);
            }
            animData.velocity = differentiableSolve.derivative().apply(d);
        }
        if (Double.isInfinite(animData.value)) {
            Log.e(CommonUtils.TAG, "updateDurationMotionAnim data.value isInfinite!  data.ease " + durationMotionEaseStyle + " totalTimeNanos " + j + " data.progress " + animData.progress);
        }
    }

    private static void updatePhysicsMotionAnim(AnimData animData, long j) {
        Motion motion = ((EaseManager.PhysicsMotionEaseStyle) animData.ease).motion;
        double dFinishTime = motion.finishTime();
        double d = animData.duration;
        if (Double.isFinite(dFinishTime) && d >= dFinishTime) {
            animData.value = ((animData.targetValue - animData.startValue) * motion.stopPosition()) + animData.startValue;
            animData.velocity = motion.stopSpeed();
            animData.setOp((byte) 3);
        } else {
            Differentiable differentiableSolve = motion.solve();
            animData.value = ((animData.targetValue - animData.startValue) * differentiableSolve.apply(d)) + animData.startValue;
            animData.velocity = differentiableSolve.derivative().apply(d);
        }
    }

    private static void setFinishValue(AnimData animData) {
        if (isUsingSpringPhy(animData)) {
            animData.value = animData.targetValue;
        }
    }

    private static void doPhysicsCalculation(AnimData animData, double d) {
        boolean z;
        String str;
        String str2;
        double d2 = animData.velocity;
        PhysicsOperator phyOperator = getPhyOperator(animData.ease.style);
        if (phyOperator == null || (((z = phyOperator instanceof SpringOperator)) && AnimValueUtils.isInvalid(animData.targetValue))) {
            animData.value = animData.targetValue;
            animData.velocity = 0.0d;
            return;
        }
        if (z) {
            if (animData.frameCount == 1) {
                double d3 = animData.value;
                double d4 = animData.ease.factors[0];
                double[] dArr = animData.ease.parameters;
                double d5 = dArr[1];
                double d6 = dArr[2];
                str2 = "doPhysicsCalculation data.value isInfinite! startVelocity ";
                str = CommonUtils.TAG;
                SpringOperator.updateValues(animData, d4, d5, d6, d, false);
                animData.value = d3;
            } else {
                str = CommonUtils.TAG;
                str2 = "doPhysicsCalculation data.value isInfinite! startVelocity ";
            }
            double d7 = animData.ease.factors[0];
            double[] dArr2 = animData.ease.parameters;
            SpringOperator.updateValues(animData, d7, dArr2[1], dArr2[2], d, false);
            if (Double.isInfinite(animData.value)) {
                Log.e(str, str2 + d2 + " data.ease.parameters " + animData.ease.parameters + " delta " + d + " data.targetValue " + animData.targetValue + " data.velocity " + animData.velocity);
                return;
            }
            return;
        }
        double[] dArr3 = animData.ease.parameters;
        double dUpdateVelocity = phyOperator.updateVelocity(d2, dArr3[0], dArr3[1], d, animData.targetValue, animData.value);
        double d8 = animData.value + ((animData.velocity + dUpdateVelocity) * 0.5d * d);
        animData.value = d8;
        if (Double.isInfinite(d8)) {
            Log.e(CommonUtils.TAG, "doPhysicsCalculation data.value isInfinite! startVelocity " + d2 + " velocity " + dUpdateVelocity + " data.ease.parameters " + animData.ease.parameters + " delta " + d + " data.targetValue " + animData.targetValue + " data.velocity " + animData.velocity);
        }
        animData.velocity = dUpdateVelocity;
    }

    private static void doArgbPhysicsCalculation(AnimData animData, double d) {
        AnimData animData2;
        boolean z;
        double d2 = animData.velocity;
        PhysicsOperator phyOperator = getPhyOperator(animData.ease.style);
        if (phyOperator == null || (((z = phyOperator instanceof SpringOperator)) && AnimValueUtils.isInvalid(animData.targetValue))) {
            animData2 = animData;
            animData2.value = 1.0d;
            animData2.velocity = 0.0d;
        } else if (z) {
            if (animData.frameCount == 1) {
                double d3 = animData.progress;
                double d4 = animData.ease.factors[0];
                double[] dArr = animData.ease.parameters;
                SpringOperator.updateValues(animData, d4, dArr[1], dArr[2], d, true);
                animData.progress = d3;
            }
            double d5 = animData.ease.factors[0];
            double[] dArr2 = animData.ease.parameters;
            SpringOperator.updateValues(animData, d5, dArr2[1], dArr2[2], d, true);
            animData2 = animData;
        } else {
            double[] dArr3 = animData.ease.parameters;
            animData2 = animData;
            double dUpdateVelocity = phyOperator.updateVelocity(d2, dArr3[0], dArr3[1], d, 1.0d, animData.progress);
            double d6 = animData2.progress + ((animData2.velocity + dUpdateVelocity) * 0.5d * d);
            animData2.progress = d6;
            if (d6 > 1.0d) {
                animData2.progress = 1.0d;
            } else if (d6 < 0.0d) {
                animData2.progress = 0.0d;
            }
            animData2.velocity = dUpdateVelocity;
        }
        Integer num = (Integer) CommonUtils.sArgbEvaluator.evaluate((float) animData2.progress, Integer.valueOf((int) animData2.startValue), Integer.valueOf((int) animData2.targetValue));
        if (LogUtils.isLogFrameEnable() || LogUtils.isLogDetailEnable()) {
            LogUtils.debug("doArgbPhysics p='" + animData2.property.getName() + "' color=" + Integer.toHexString(num.intValue()) + " fraction=" + animData2.progress, new Object[0]);
        }
        animData2.value = num.doubleValue();
    }

    static boolean isAnimRunning(EquilibriumChecker equilibriumChecker, FloatProperty floatProperty, int i, double d, double d2, long j) {
        boolean zIsAtEquilibrium = equilibriumChecker.isAtEquilibrium(i, d, d2);
        boolean z = !zIsAtEquilibrium;
        if (!zIsAtEquilibrium && j > LONGEST_DURATION_NANOS) {
            z = false;
            if (LogUtils.isLogMainEnabled()) {
                LogUtils.debug("animation for " + floatProperty.getName() + " stopped for running time too long, totalTime_nanos = " + j, new Object[0]);
            }
        }
        return z;
    }

    public static PhysicsOperator getPhyOperator(int i) {
        if (i == -4) {
            return sFriction;
        }
        if (i == -3) {
            return sAccelerate;
        }
        if (i != -2) {
            return null;
        }
        return sSpring;
    }

    @Deprecated
    public static float getVelocityThreshold() {
        EquilibriumChecker equilibriumChecker;
        FolmeEngine engine = FolmeFactory.getEngine();
        if (engine == null || (equilibriumChecker = (EquilibriumChecker) CommonUtils.getLocal(engine.getObjPool(), mCheckerLocal, EquilibriumChecker.class)) == null) {
            return 0.0f;
        }
        return equilibriumChecker.getVelocityThreshold();
    }

    private static boolean isUsingSpringPhy(AnimData animData) {
        return animData.ease.style == -2;
    }
}
