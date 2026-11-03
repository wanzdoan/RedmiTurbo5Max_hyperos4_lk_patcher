package miuix.core.util;

import android.content.Context;
import android.graphics.Point;
import android.util.Pair;
import android.view.View;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MiuiBlurUtils {
    public static Method METHOD_ADD_BG_BLEND_COLOR;
    public static Method METHOD_CLEAR_BG_BLEND_COLOR;
    public static Method METHOD_GET_PASS_WINDOW_BLUR_MODE;
    public static volatile Method METHOD_IS_PASS_WINDOW_BLUR_WHITELISTED;
    public static Method METHOD_SET_BG_BLEND_COLORS;
    public static Method METHOD_SET_BG_BLUR_MODE;
    public static Method METHOD_SET_BG_BLUR_RADIUS;
    public static Method METHOD_SET_BG_BLUR_TYPE;
    public static Method METHOD_SET_BG_GRADIENT_BLUR_PARAMS;
    public static Method METHOD_SET_BG_LIGHT_BLEND_MODE;
    public static Method METHOD_SET_COLOR_ADJUST;
    public static Method METHOD_SET_GLASS;
    public static Method METHOD_SET_MIX_EFFECT_ENABLED;
    public static Method METHOD_SET_MI_BLUR_WIN_TYPE;
    public static volatile Method METHOD_SET_MI_GLASS_BLUR_RADIUS;
    public static volatile Method METHOD_SET_MI_VIEW_MATERIAL_TYPE;
    public static Method METHOD_SET_PASS_WINDOW_BLUR_MODE;
    public static Method METHOD_SET_SELF_BLUR;
    public static Method METHOD_SET_SELF_BLUR_ENHANCE_FLAG;
    public static Method METHOD_SET_SELF_BLUR_EXTEND;
    public static Method METHOD_SET_VIEW_BLUR_MODE;

    public static boolean isEnable() {
        return HyperMaterialUtils.isSupport();
    }

    public static synchronized boolean isEffectEnable(Context context) {
        return HyperMaterialUtils.isSupport() && HyperMaterialUtils.isFeatureEnable(context);
    }

    public static void setBlurConfig(View view, float f, MaterialConfig.BlurConfig blurConfig) {
        float[] fArr;
        if (blurConfig == null) {
            clearBlurConfig(view);
            return;
        }
        int i = (int) ((blurConfig.blurRadius * f) + 0.5f);
        applyBackgroundBlurMode(view, blurConfig, i);
        applyContentBlurMode(view, blurConfig, i);
        applyColorBlendConfig(view, blurConfig);
        setBackgroundBlurType(view, blurConfig.blurType);
        if (blurConfig.blurType != 2 || (fArr = blurConfig.blurExtraParams) == null) {
            return;
        }
        setBackgroundGradientBlurParams(view, fArr, blurConfig.blurSubType);
    }

    private static void applyBackgroundBlurMode(View view, MaterialConfig.BlurConfig blurConfig, int i) {
        int i2 = blurConfig.blurBgMode;
        if (i2 > 0) {
            setBackgroundBlur(view, i, i2);
        }
    }

    private static void applyContentBlurMode(View view, MaterialConfig.BlurConfig blurConfig, int i) {
        int i2 = blurConfig.blurContentMode;
        if (i2 <= 0) {
            return;
        }
        if (i2 == 1 || i2 == 2 || i2 == 3) {
            setViewBlurMode(view, i2);
            setBackgroundBlurRadius(view, i);
        } else if (i2 == 4 && blurConfig.colorBlendConfig != null) {
            setBackgroundLightBlendMode(view, 1);
        }
    }

    private static void applyColorBlendConfig(View view, MaterialConfig.BlurConfig blurConfig) {
        int[] iArr;
        int[] iArr2;
        ArrayList colorAdjustParams;
        MaterialConfig.ColorBlendConfig colorBlendConfig = blurConfig.colorBlendConfig;
        if (colorBlendConfig == null || (iArr = colorBlendConfig.blendColors) == null || (iArr2 = colorBlendConfig.blendModes) == null) {
            return;
        }
        ArrayList arrayListWrapBlendConfig = wrapBlendConfig(iArr, iArr2);
        if (arrayListWrapBlendConfig != null) {
            setBackgroundBlendConfig(view, arrayListWrapBlendConfig);
        }
        float[] fArr = colorBlendConfig.blendExtraParams;
        if (fArr == null || (colorAdjustParams = parseColorAdjustParams(fArr)) == null || colorAdjustParams.isEmpty()) {
            return;
        }
        setColorAdjust(view, colorAdjustParams);
    }

    private static ArrayList parseColorAdjustParams(float[] fArr) {
        int length = fArr.length / 7;
        if (length == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < length) {
            int i2 = i * 7;
            int i3 = i2 + 7;
            if (i3 > fArr.length) {
                break;
            }
            i++;
            arrayList.add(new Pair(Integer.valueOf(i), Arrays.copyOfRange(fArr, i2, i3)));
        }
        return arrayList;
    }

    private static void clearBlurConfig(View view) {
        setViewBlurMode(view, 0);
        setGlass(view, null);
        setBackgroundBlurType(view, 0);
        clearBackgroundBlendConfig(view);
    }

    public static boolean setBackgroundBlur(View view, int i) {
        return setBackgroundBlur(view, i, false);
    }

    public static boolean setBackgroundBlur(View view, int i, boolean z) {
        if (z) {
            return setViewBlurMode(view, 2) | setBackgroundBlur(view, i, 1);
        }
        return setBackgroundBlur(view, i, 1);
    }

    public static boolean setBackgroundOnlyBlur(View view, int i) {
        if (view != null && HyperMaterialUtils.isSupport() && HyperMaterialUtils.isFeatureEnable(view.getContext())) {
            return setBackgroundBlurMode(view, 1) && setBackgroundBlurRadius(view, Math.max(0, Math.min(i, 400)));
        }
        return false;
    }

    public static boolean setBackgroundBlur(View view, int i, int i2) {
        if (view != null && HyperMaterialUtils.isSupport() && HyperMaterialUtils.isFeatureEnable(view.getContext())) {
            return setBackgroundBlurMode(view, 1) && setBackgroundBlurRadius(view, Math.max(0, Math.min(i, 400))) && setViewBlurMode(view, i2);
        }
        return false;
    }

    public static boolean setBackgroundBlurRadius(View view, int i) {
        if (HyperMaterialUtils.isSupport() && i >= 0 && i <= 400) {
            try {
                if (METHOD_SET_BG_BLUR_RADIUS == null) {
                    METHOD_SET_BG_BLUR_RADIUS = View.class.getMethod("setMiBackgroundBlurRadius", Integer.TYPE);
                }
                METHOD_SET_BG_BLUR_RADIUS.invoke(view, Integer.valueOf(i));
                return true;
            } catch (Exception unused) {
                METHOD_SET_BG_BLUR_RADIUS = null;
            }
        }
        return false;
    }

    public static boolean setBackgroundBlurMode(View view, int i) {
        if (!HyperMaterialUtils.isSupport()) {
            return false;
        }
        try {
            if (METHOD_SET_BG_BLUR_MODE == null) {
                METHOD_SET_BG_BLUR_MODE = View.class.getMethod("setMiBackgroundBlurMode", Integer.TYPE);
            }
            METHOD_SET_BG_BLUR_MODE.invoke(view, Integer.valueOf(i));
            return true;
        } catch (Exception unused) {
            METHOD_SET_BG_BLUR_MODE = null;
            return false;
        }
    }

    public static boolean setBackgroundLightBlendMode(View view, int i) {
        if (!HyperMaterialUtils.isSupport()) {
            return false;
        }
        try {
            if (METHOD_SET_BG_LIGHT_BLEND_MODE == null) {
                METHOD_SET_BG_LIGHT_BLEND_MODE = View.class.getMethod("setMiBackgroundLightBlendMode", Integer.TYPE);
            }
            METHOD_SET_BG_LIGHT_BLEND_MODE.invoke(view, Integer.valueOf(i));
            return true;
        } catch (Exception unused) {
            METHOD_SET_BG_LIGHT_BLEND_MODE = null;
            return false;
        }
    }

    public static boolean setBackgroundBlurType(View view, int i) {
        if (!HyperMaterialUtils.isSupport()) {
            return false;
        }
        try {
            if (METHOD_SET_BG_BLUR_TYPE == null) {
                METHOD_SET_BG_BLUR_TYPE = View.class.getMethod("setMiBackgroundBlurType", Integer.TYPE);
            }
            METHOD_SET_BG_BLUR_TYPE.invoke(view, Integer.valueOf(i));
            return true;
        } catch (Exception unused) {
            METHOD_SET_BG_BLUR_TYPE = null;
            return false;
        }
    }

    public static boolean clearBackgroundBlur(View view) {
        if (setBackgroundBlurMode(view, 0)) {
            return setViewBlurMode(view, 0);
        }
        return false;
    }

    public static boolean setViewBlurMode(View view, int i) {
        if (view != null && HyperMaterialUtils.isSupport()) {
            try {
                if (METHOD_SET_VIEW_BLUR_MODE == null) {
                    METHOD_SET_VIEW_BLUR_MODE = View.class.getMethod("setMiViewBlurMode", Integer.TYPE);
                }
                METHOD_SET_VIEW_BLUR_MODE.invoke(view, Integer.valueOf(i));
                return true;
            } catch (Exception unused) {
                METHOD_SET_VIEW_BLUR_MODE = null;
            }
        }
        return false;
    }

    public static boolean addBackgroundBlenderColor(View view, int i, int i2) {
        if (view != null && HyperMaterialUtils.isSupport()) {
            try {
                if (METHOD_ADD_BG_BLEND_COLOR == null) {
                    Class cls = Integer.TYPE;
                    METHOD_ADD_BG_BLEND_COLOR = View.class.getMethod("addMiBackgroundBlendColor", cls, cls);
                }
                METHOD_ADD_BG_BLEND_COLOR.invoke(view, Integer.valueOf(i), Integer.valueOf(i2));
                return true;
            } catch (Exception unused) {
                METHOD_ADD_BG_BLEND_COLOR = null;
            }
        }
        return false;
    }

    public static boolean setBackgroundBlendConfig(View view, int[] iArr, int[] iArr2) {
        if (view == null || iArr == null || iArr2 == null || !HyperMaterialUtils.isSupport()) {
            return false;
        }
        if (RomUtils.getHyperOsVersion() > 1) {
            ArrayList arrayListWrapBlendConfig = wrapBlendConfig(iArr, iArr2);
            if (arrayListWrapBlendConfig == null) {
                return false;
            }
            setBackgroundBlendConfig(view, arrayListWrapBlendConfig);
            return true;
        }
        clearBackgroundBlendConfig(view);
        int iMin = Math.min(iArr.length, iArr2.length);
        for (int i = 0; i < iMin; i++) {
            if (!addBackgroundBlenderColor(view, iArr[i], iArr2[i])) {
                clearBackgroundBlendConfig(view);
                return false;
            }
        }
        return true;
    }

    public static boolean setBackgroundBlendConfig(View view, ArrayList arrayList) {
        if (view != null && HyperMaterialUtils.isSupport()) {
            try {
                if (METHOD_SET_BG_BLEND_COLORS == null) {
                    METHOD_SET_BG_BLEND_COLORS = View.class.getMethod("setMiBackgroundBlendColors", ArrayList.class);
                }
                METHOD_SET_BG_BLEND_COLORS.invoke(view, arrayList);
                return true;
            } catch (Exception unused) {
                METHOD_SET_BG_BLEND_COLORS = null;
            }
        }
        return false;
    }

    public static boolean setColorAdjust(View view, ArrayList arrayList) {
        if (view != null && HyperMaterialUtils.isSupport()) {
            try {
                if (METHOD_SET_COLOR_ADJUST == null) {
                    METHOD_SET_COLOR_ADJUST = View.class.getMethod("setMiColorAdjust", ArrayList.class);
                }
                METHOD_SET_COLOR_ADJUST.invoke(view, arrayList);
                return true;
            } catch (Exception unused) {
                METHOD_SET_COLOR_ADJUST = null;
            }
        }
        return false;
    }

    public static boolean setBackgroundLinearGradientBlur(View view, float[] fArr) {
        return setBackgroundGradientBlur(view, fArr, 1);
    }

    public static boolean setBackgroundGradientBlur(View view, float[] fArr, int i) {
        return HyperMaterialUtils.isSupport() && HyperMaterialUtils.isDefaultFeatureEnable() && setBackgroundBlurType(view, 2) && setBackgroundGradientBlurParams(view, fArr, i);
    }

    public static boolean setBackgroundGradientBlurParams(View view, float[] fArr, int i) {
        if (!HyperMaterialUtils.isSupport() || !HyperMaterialUtils.isDefaultFeatureEnable()) {
            return false;
        }
        try {
            if (METHOD_SET_BG_GRADIENT_BLUR_PARAMS == null) {
                METHOD_SET_BG_GRADIENT_BLUR_PARAMS = View.class.getMethod("setBackgroundGradientBlurParams", float[].class, Integer.TYPE);
            }
            METHOD_SET_BG_GRADIENT_BLUR_PARAMS.invoke(view, fArr, Integer.valueOf(i));
            return true;
        } catch (Exception unused) {
            METHOD_SET_BG_GRADIENT_BLUR_PARAMS = null;
            return false;
        }
    }

    public static ArrayList wrapBlendConfig(int[] iArr, int[] iArr2) {
        if (iArr == null || iArr2 == null) {
            return null;
        }
        int iMin = Math.min(iArr.length, iArr2.length);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < iMin; i++) {
            arrayList.add(new Point(iArr[i], iArr2[i]));
        }
        return arrayList;
    }

    public static boolean clearBackgroundBlendConfig(View view) {
        if (!HyperMaterialUtils.isSupport()) {
            return false;
        }
        try {
            if (METHOD_CLEAR_BG_BLEND_COLOR == null) {
                Class[] clsArr = new Class[0];
                METHOD_CLEAR_BG_BLEND_COLOR = View.class.getMethod("clearMiBackgroundBlendColor", null);
            }
            METHOD_CLEAR_BG_BLEND_COLOR.invoke(view, null);
            return true;
        } catch (Exception unused) {
            METHOD_CLEAR_BG_BLEND_COLOR = null;
            return false;
        }
    }

    public static boolean setMiSelfBlurEnhanceFlag(View view, int i, int i2) {
        if (view != null && HyperMaterialUtils.isSupport()) {
            try {
                if (METHOD_SET_SELF_BLUR_ENHANCE_FLAG == null) {
                    Class cls = Integer.TYPE;
                    METHOD_SET_SELF_BLUR_ENHANCE_FLAG = View.class.getMethod("setMiSelfBlurEnhanceFlag", cls, cls);
                }
                METHOD_SET_SELF_BLUR_ENHANCE_FLAG.invoke(view, Integer.valueOf(i), Integer.valueOf(i2));
                return true;
            } catch (Exception unused) {
                METHOD_SET_SELF_BLUR_ENHANCE_FLAG = null;
            }
        }
        return false;
    }

    public static boolean setMixEffectEnabled(View view, boolean z) {
        if (!HyperMaterialUtils.isSupport()) {
            return false;
        }
        try {
            if (METHOD_SET_MIX_EFFECT_ENABLED == null) {
                METHOD_SET_MIX_EFFECT_ENABLED = View.class.getMethod("setMixEffectEnabled", Boolean.TYPE);
            }
            METHOD_SET_MIX_EFFECT_ENABLED.invoke(view, Boolean.valueOf(z));
            return true;
        } catch (Exception unused) {
            METHOD_SET_MIX_EFFECT_ENABLED = null;
            return false;
        }
    }

    public static boolean setSelfBlur(View view, int i, ArrayList arrayList) {
        if (!HyperMaterialUtils.isSupport()) {
            return false;
        }
        try {
            if (METHOD_SET_SELF_BLUR == null) {
                METHOD_SET_SELF_BLUR = View.class.getMethod("setMiSelfBlur", Integer.TYPE, ArrayList.class);
            }
            METHOD_SET_SELF_BLUR.invoke(view, Integer.valueOf(i), arrayList);
            return true;
        } catch (Exception unused) {
            METHOD_SET_SELF_BLUR = null;
            return false;
        }
    }

    public static boolean setSelfBlur(View view, int i, ArrayList arrayList, int i2) {
        if (!HyperMaterialUtils.isSupport()) {
            return false;
        }
        try {
            if (METHOD_SET_SELF_BLUR_EXTEND == null) {
                Class cls = Integer.TYPE;
                METHOD_SET_SELF_BLUR_EXTEND = View.class.getMethod("setMiSelfBlur", cls, ArrayList.class, cls);
            }
            METHOD_SET_SELF_BLUR_EXTEND.invoke(view, Integer.valueOf(i), arrayList, Integer.valueOf(i2));
            return true;
        } catch (Exception unused) {
            METHOD_SET_SELF_BLUR_EXTEND = null;
            return false;
        }
    }

    public static boolean setPassWindowBlurEnabled(View view, boolean z) {
        if (!HyperMaterialUtils.isSupport()) {
            return false;
        }
        try {
            if (METHOD_SET_PASS_WINDOW_BLUR_MODE == null) {
                METHOD_SET_PASS_WINDOW_BLUR_MODE = View.class.getMethod("setPassWindowBlurEnabled", Boolean.TYPE);
            }
            METHOD_SET_PASS_WINDOW_BLUR_MODE.invoke(view, Boolean.valueOf(z));
            return true;
        } catch (Exception unused) {
            METHOD_SET_PASS_WINDOW_BLUR_MODE = null;
            return false;
        }
    }

    public static boolean getPassWindowBlurEnabled(View view) {
        if (!HyperMaterialUtils.isSupport()) {
            return false;
        }
        try {
            if (METHOD_GET_PASS_WINDOW_BLUR_MODE == null) {
                Class[] clsArr = new Class[0];
                METHOD_GET_PASS_WINDOW_BLUR_MODE = View.class.getMethod("getPassWindowBlurEnabled", null);
            }
            Object objInvoke = METHOD_GET_PASS_WINDOW_BLUR_MODE.invoke(view, null);
            if (objInvoke instanceof Boolean) {
                return ((Boolean) objInvoke).booleanValue();
            }
        } catch (Exception unused) {
            METHOD_GET_PASS_WINDOW_BLUR_MODE = null;
        }
        return false;
    }

    public static boolean setGlass(View view, float[] fArr) {
        if (!HyperMaterialUtils.isSupportGlass()) {
            return false;
        }
        if (fArr == null) {
            fArr = new float[0];
        }
        try {
            if (METHOD_SET_GLASS == null) {
                METHOD_SET_GLASS = View.class.getMethod("setMiGlass", float[].class);
            }
            METHOD_SET_GLASS.invoke(view, fArr);
            return true;
        } catch (Exception unused) {
            METHOD_SET_GLASS = null;
            return false;
        }
    }

    public static void setMiViewMaterialType(View view, int i) {
        if (HyperMaterialUtils.isSupportGlass()) {
            try {
                Method method = METHOD_SET_MI_VIEW_MATERIAL_TYPE;
                if (method == null) {
                    method = View.class.getMethod("setMiViewMaterialType", Integer.TYPE);
                    METHOD_SET_MI_VIEW_MATERIAL_TYPE = method;
                }
                method.invoke(view, Integer.valueOf(i));
            } catch (NoSuchMethodException unused) {
                METHOD_SET_MI_VIEW_MATERIAL_TYPE = null;
            } catch (ReflectiveOperationException unused2) {
                METHOD_SET_MI_VIEW_MATERIAL_TYPE = null;
            }
        }
    }

    public static boolean setMiGlassBlurRadius(View view, int i, int i2) {
        if (!HyperMaterialUtils.isSupportGlass()) {
            return false;
        }
        try {
            Method method = METHOD_SET_MI_GLASS_BLUR_RADIUS;
            if (method == null) {
                Class cls = Integer.TYPE;
                method = View.class.getMethod("setMiGlassBlurRadius", cls, cls);
                METHOD_SET_MI_GLASS_BLUR_RADIUS = method;
            }
            method.invoke(view, Integer.valueOf(i), Integer.valueOf(i2));
            return true;
        } catch (NoSuchMethodException unused) {
            METHOD_SET_MI_GLASS_BLUR_RADIUS = null;
            return false;
        } catch (ReflectiveOperationException unused2) {
            METHOD_SET_MI_GLASS_BLUR_RADIUS = null;
            return false;
        }
    }

    public static boolean setMiBlurWinType(View view, int i) {
        if (!HyperMaterialUtils.isSupport()) {
            return false;
        }
        try {
            if (METHOD_SET_MI_BLUR_WIN_TYPE == null) {
                METHOD_SET_MI_BLUR_WIN_TYPE = View.class.getMethod("setMiBlurWinType", Integer.TYPE);
            }
            METHOD_SET_MI_BLUR_WIN_TYPE.invoke(view, Integer.valueOf(i));
            return true;
        } catch (Exception unused) {
            METHOD_SET_MI_BLUR_WIN_TYPE = null;
            return false;
        }
    }

    public static boolean isPassWindowBlurWhitelisted(View view, String str) {
        Method method = METHOD_IS_PASS_WINDOW_BLUR_WHITELISTED;
        if (method == null) {
            try {
                method = View.class.getMethod("isPassWindowBlurWhitelisted", String.class);
                METHOD_IS_PASS_WINDOW_BLUR_WHITELISTED = method;
            } catch (Exception unused) {
                METHOD_IS_PASS_WINDOW_BLUR_WHITELISTED = null;
            }
        }
        Object objInvoke = method.invoke(view, str);
        if (objInvoke instanceof Boolean) {
            return ((Boolean) objInvoke).booleanValue();
        }
        return false;
    }
}
