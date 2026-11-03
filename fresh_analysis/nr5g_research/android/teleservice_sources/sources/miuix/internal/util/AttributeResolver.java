package miuix.internal.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AttributeResolver {
    private static final TypedValue TYPED_VALUE = new TypedValue();
    private static final ThreadLocal TYPED_VALUE_THREAD_LOCAL = new ThreadLocal();

    private static TypedValue getTypedValue(Context context) {
        if (context.getMainLooper().getThread() == Thread.currentThread()) {
            return TYPED_VALUE;
        }
        ThreadLocal threadLocal = TYPED_VALUE_THREAD_LOCAL;
        TypedValue typedValue = (TypedValue) threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }

    public static int resolve(Context context, int i) {
        TypedValue typedValue = getTypedValue(context);
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            return typedValue.resourceId;
        }
        return -1;
    }

    public static Drawable resolveDrawable(Context context, int i) {
        TypedValue typedValue = getTypedValue(context);
        if (!context.getTheme().resolveAttribute(i, typedValue, true)) {
            return null;
        }
        if (typedValue.resourceId > 0) {
            return context.getResources().getDrawable(typedValue.resourceId, context.getTheme());
        }
        int i2 = typedValue.type;
        if (i2 < 28 || i2 > 31) {
            return null;
        }
        return new ColorDrawable(typedValue.data);
    }

    public static int resolveColor(Context context, int i) {
        Integer numInnerResolveColor = innerResolveColor(context, i);
        if (numInnerResolveColor != null) {
            return numInnerResolveColor.intValue();
        }
        return context.getResources().getColor(-1);
    }

    public static int resolveColor(Context context, int i, int i2) {
        Integer numInnerResolveColor = innerResolveColor(context, i);
        return numInnerResolveColor != null ? numInnerResolveColor.intValue() : i2;
    }

    private static Integer innerResolveColor(Context context, int i) {
        TypedValue typedValue = getTypedValue(context);
        if (!context.getTheme().resolveAttribute(i, typedValue, true)) {
            return null;
        }
        if (typedValue.resourceId > 0) {
            return Integer.valueOf(context.getResources().getColor(typedValue.resourceId));
        }
        int i2 = typedValue.type;
        if (i2 < 28 || i2 > 31) {
            return null;
        }
        return Integer.valueOf(typedValue.data);
    }

    public static boolean resolveBoolean(Context context, int i, boolean z) {
        TypedValue typedValue = getTypedValue(context);
        if (!context.getTheme().resolveAttribute(i, typedValue, true) || typedValue.type != 18) {
            return z;
        }
        if (typedValue.resourceId > 0) {
            return context.getResources().getBoolean(typedValue.resourceId);
        }
        return typedValue.data != 0;
    }

    public static int resolveDimensionPixelSize(Context context, int i) {
        TypedValue typedValueResolveTypedValue = resolveTypedValue(context, i);
        if (typedValueResolveTypedValue == null || typedValueResolveTypedValue.type != 5) {
            return 0;
        }
        if (typedValueResolveTypedValue.resourceId > 0) {
            return context.getResources().getDimensionPixelSize(typedValueResolveTypedValue.resourceId);
        }
        return TypedValue.complexToDimensionPixelSize(typedValueResolveTypedValue.data, context.getResources().getDisplayMetrics());
    }

    public static int resolveInt(Context context, int i, int i2) {
        TypedValue typedValue = getTypedValue(context);
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            if (typedValue.resourceId > 0) {
                return context.getResources().getInteger(typedValue.resourceId);
            }
            int i3 = typedValue.type;
            if (i3 >= 16 && i3 <= 31) {
                return typedValue.data;
            }
        }
        return i2;
    }

    public static float resolveFloat(Context context, int i, float f) {
        TypedValue typedValue = getTypedValue(context);
        if (!context.getTheme().resolveAttribute(i, typedValue, true) || typedValue.type != 4) {
            return f;
        }
        if (typedValue.resourceId > 0) {
            return context.getResources().getFloat(typedValue.resourceId);
        }
        return typedValue.data;
    }

    public static TypedValue resolveTypedValue(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            return typedValue;
        }
        return null;
    }
}
