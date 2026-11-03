package androidx.core.view;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class VelocityTrackerCompat {
    private static Map sFallbackTrackers = Collections.synchronizedMap(new WeakHashMap());

    public static float getAxisVelocity(VelocityTracker velocityTracker, int i) {
        return Api34Impl.getAxisVelocity(velocityTracker, i);
    }

    public static void computeCurrentVelocity(VelocityTracker velocityTracker, int i, float f) {
        velocityTracker.computeCurrentVelocity(i, f);
        getFallbackTrackerOrNull(velocityTracker);
    }

    public static void computeCurrentVelocity(VelocityTracker velocityTracker, int i) {
        computeCurrentVelocity(velocityTracker, i, Float.MAX_VALUE);
    }

    public static void addMovement(VelocityTracker velocityTracker, MotionEvent motionEvent) {
        velocityTracker.addMovement(motionEvent);
    }

    private static VelocityTrackerFallback getFallbackTrackerOrNull(VelocityTracker velocityTracker) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(sFallbackTrackers.get(velocityTracker));
        return null;
    }

    abstract class Api34Impl {
        static float getAxisVelocity(VelocityTracker velocityTracker, int i) {
            return velocityTracker.getAxisVelocity(i);
        }
    }
}
