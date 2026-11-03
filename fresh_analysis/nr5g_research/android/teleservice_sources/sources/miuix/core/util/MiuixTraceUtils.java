package miuix.core.util;

import android.os.Trace;
import android.util.Log;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MiuixTraceUtils {
    private static final AtomicInteger mTraceCookie = new AtomicInteger(0);
    public static boolean mAnimationTraceEnabled = isAnimationTraceEnabled();

    private static boolean isAnimationTraceEnabled() {
        try {
            return SystemProperties.getBoolean("persist.miuix.animation.trace.enable", false) || SystemProperties.getBoolean("log.tag.miuix.animation.trace.enable", false);
        } catch (Exception e) {
            Log.i("MiuixTraceUtils", "can not access property log.tag.miuix.animation.trace.enable | persist.sys.miuix.animation.trace.enable, debug mode disabled", e);
            return false;
        }
    }

    public static int generateUniqueCookie() {
        return mTraceCookie.getAndIncrement();
    }

    public static void beginAsyncTrace(String str, int i) {
        if (mAnimationTraceEnabled) {
            Trace.beginAsyncSection(str, i);
        }
    }

    public static void endAsyncTrace(String str, int i) {
        if (mAnimationTraceEnabled) {
            Trace.endAsyncSection(str, i);
        }
    }
}
