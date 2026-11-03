package miuix.recyclerview.card;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/* JADX INFO: loaded from: classes2.dex */
public abstract class LiteUtils {
    public static Drawable getThemeDrawable(Context context, int i) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(0);
        typedArrayObtainStyledAttributes.recycle();
        return drawable;
    }

    public static int getThemeDimens(Resources.Theme theme, Resources resources, int i) {
        int i2;
        TypedValue typedValue = new TypedValue();
        if (!theme.resolveAttribute(i, typedValue, true) || (i2 = typedValue.resourceId) <= 0) {
            return 0;
        }
        return resources.getDimensionPixelSize(i2);
    }
}
