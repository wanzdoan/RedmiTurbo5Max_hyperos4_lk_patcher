package miuix.appcompat.internal.util;

import android.content.Context;
import miuix.appcompat.R$attr;
import miuix.internal.util.AttributeResolver;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MenuBlurUtils {
    public static int getMenuBlurOptions(Context context) {
        if (context == null) {
            return 0;
        }
        return AttributeResolver.resolveInt(context, R$attr.menuBlurOptions, 0);
    }

    public static boolean isMenuBlurOptionEnabled(Context context, int i) {
        return (getMenuBlurOptions(context) & i) != 0;
    }
}
