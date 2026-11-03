package miuix.animation.internal;

import android.animation.TimeInterpolator;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.physics.PhysicsOperator;
import miuix.animation.property.FloatProperty;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.EaseManager;

/* JADX INFO: loaded from: classes2.dex */
public class PredictTask {
    static final TransitionInfo.IUpdateInfoCreator sCreator = new TransitionInfo.IUpdateInfoCreator() { // from class: miuix.animation.internal.PredictTask.1
        @Override // miuix.animation.internal.TransitionInfo.IUpdateInfoCreator
        public UpdateInfo getUpdateInfo(FloatProperty floatProperty) {
            return new UpdateInfo(floatProperty);
        }
    };

    public static long predictDuration(IAnimTarget iAnimTarget, AnimState animState, AnimState animState2, AnimConfigLink animConfigLink) {
        TransitionInfo transitionInfo = new TransitionInfo(iAnimTarget, animState, animState2, animConfigLink);
        transitionInfo.initUpdateList(sCreator);
        if (transitionInfo.animTasks.isEmpty()) {
            Log.w(CommonUtils.TAG, "warning!! predictDuration failed! info " + transitionInfo + " trace:" + Log.getStackTraceString(new Throwable()));
            return 0L;
        }
        long j = 16666666;
        while (true) {
            Iterator it = transitionInfo.animTasks.iterator();
            while (it.hasNext()) {
                AnimTaskStackRunner.doAnimationFrame((AnimTask) it.next(), j, FolmeEngine.MAX_DELTA, 1, 0.016666666d, false);
            }
            if (!transitionInfo.getInfoAnimStats().isRunning()) {
                AnimTaskStackRunner.animDataLocal.remove();
                return (long) (j / 1000000.0d);
            }
            j += FolmeEngine.MAX_DELTA;
        }
    }

    public static double predictNextValue(IAnimTarget iAnimTarget, AnimState animState, FloatProperty floatProperty, AnimConfig animConfig) {
        AndroidEngine inst;
        EaseManager.EaseStyle ease = AnimConfigUtils.getEase(animState.getConfig(), animConfig.getSpecialConfig(floatProperty));
        float value = iAnimTarget.getValue(floatProperty);
        double velocity = iAnimTarget.getVelocity(floatProperty);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        iAnimTarget.animManager.addToTransitionInfoList(arrayList);
        int size = arrayList.size();
        UpdateInfo updateInfoFindBy = null;
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            TransitionInfo transitionInfo = (TransitionInfo) obj;
            arrayList2.clear();
            arrayList2.addAll(transitionInfo.updateList);
            if (transitionInfo.containsPropertyInUpdateList(floatProperty)) {
                updateInfoFindBy = UpdateInfo.findBy(transitionInfo.updateList, floatProperty);
            }
        }
        if (updateInfoFindBy == null || (inst = AndroidEngine.getInst()) == null) {
            return Double.MAX_VALUE;
        }
        double averageDeltaNanos = inst.getAverageDeltaNanos() / 1.0E9d;
        if (EaseManager.isPhysicsStyle(ease.style) && ease.parameters != null) {
            PhysicsOperator phyOperator = FolmeCore.getPhyOperator(ease.style);
            if (AnimValueUtils.isInvalid(updateInfoFindBy.animInfo.targetValue)) {
                return Double.MAX_VALUE;
            }
            double[] dArr = ease.parameters;
            double d = dArr[0];
            double d2 = dArr[1];
            double d3 = value;
            return d3 + ((velocity + phyOperator.updateVelocity(velocity, d, d2, averageDeltaNanos, updateInfoFindBy.animInfo.targetValue, d3)) * 0.5d * averageDeltaNanos);
        }
        EaseManager.InterpolateEaseStyle interpolateEaseStyle = (EaseManager.InterpolateEaseStyle) ease;
        TimeInterpolator interpolator = EaseManager.getInterpolator(interpolateEaseStyle);
        long jCurrentTimeMillis = System.currentTimeMillis();
        AnimInfo animInfo = updateInfoFindBy.animInfo;
        long j = (jCurrentTimeMillis - animInfo.startTime) + ((long) averageDeltaNanos);
        if (interpolator != null) {
            long j2 = interpolateEaseStyle.duration;
            if (j < j2) {
                return interpolator.getInterpolation(j / j2);
            }
        }
        return animInfo.targetValue;
    }

    public static double predictNextVelocity(IAnimTarget iAnimTarget, AnimState animState, FloatProperty floatProperty, AnimConfig animConfig) {
        AndroidEngine inst;
        EaseManager.EaseStyle ease = AnimConfigUtils.getEase(animState.getConfig(), animConfig.getSpecialConfig(floatProperty));
        float value = iAnimTarget.getValue(floatProperty);
        double velocity = iAnimTarget.getVelocity(floatProperty);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        iAnimTarget.animManager.addToTransitionInfoList(arrayList);
        int size = arrayList.size();
        UpdateInfo updateInfoFindBy = null;
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            TransitionInfo transitionInfo = (TransitionInfo) obj;
            arrayList2.clear();
            arrayList2.addAll(transitionInfo.updateList);
            if (transitionInfo.containsPropertyInUpdateList(floatProperty)) {
                updateInfoFindBy = UpdateInfo.findBy(arrayList2, floatProperty);
            }
        }
        if (updateInfoFindBy == null || !EaseManager.isPhysicsStyle(ease.style) || ease.parameters == null || (inst = AndroidEngine.getInst()) == null) {
            return Double.MAX_VALUE;
        }
        double averageDeltaNanos = inst.getAverageDeltaNanos() / 1.0E9d;
        PhysicsOperator phyOperator = FolmeCore.getPhyOperator(ease.style);
        if (AnimValueUtils.isInvalid(updateInfoFindBy.animInfo.targetValue)) {
            return Double.MAX_VALUE;
        }
        double[] dArr = ease.parameters;
        return phyOperator.updateVelocity(velocity, dArr[0], dArr[1], averageDeltaNanos, updateInfoFindBy.animInfo.targetValue, value);
    }
}
