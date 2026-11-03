package miuix.core.util;

import android.util.Log;
import android.view.View;
import miuix.reflect.ReflectionHelper;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MiShadowUtils {
    public static final boolean SUPPORT_MI_SHADOW;

    static {
        boolean z = Boolean.parseBoolean(SystemProperties.get("persist.sys.mi_shadow_supported", "false"));
        SUPPORT_MI_SHADOW = z;
        if (z) {
            return;
        }
        Log.d("MiShadowHelper", "This device does not support mi shadow!");
    }

    public static void setShadowConfig(View view, MaterialConfig.ShadowConfig shadowConfig) {
        if (view != null && SUPPORT_MI_SHADOW) {
            if (shadowConfig != null) {
                setMiShadow(view, shadowConfig.shadowColor, shadowConfig.shadowOffsetX, shadowConfig.shadowOffsetY, shadowConfig.shadowRadius, shadowConfig.shadowDispersion);
            } else {
                clearMiShadow(view);
            }
        }
    }

    public static void setMiShadow(View view, int i, float f, float f2, float f3) {
        setMiShadow(view, i, f, f2, f3, 1.0f);
    }

    public static void setMiShadow(View view, int i, float f, float f2, float f3, float f4) {
        if (SUPPORT_MI_SHADOW) {
            try {
                Class cls = Float.TYPE;
                ReflectionHelper.invoke(View.class, view, "setMiShadow", new Class[]{Integer.TYPE, cls, cls, cls, cls}, Integer.valueOf(i), Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4));
            } catch (Exception e) {
                Log.e("MiShadowHelper", "Failed to call setMiShadow", e);
            }
        }
    }

    public static void setMiShadow(View view, int i, float f, float f2, float f3, float f4, boolean z) {
        if (SUPPORT_MI_SHADOW) {
            try {
                Class cls = Float.TYPE;
                ReflectionHelper.invoke(View.class, view, "setMiShadow", new Class[]{Integer.TYPE, cls, cls, cls, cls, Boolean.TYPE}, Integer.valueOf(i), Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Boolean.valueOf(z));
            } catch (Exception e) {
                Log.e("MiShadowHelper", "Failed to call setMiShadow", e);
            }
        }
    }

    public static void clearMiShadow(View view) {
        setMiShadow(view, 0, 0.0f, 0.0f, 0.0f);
    }
}
