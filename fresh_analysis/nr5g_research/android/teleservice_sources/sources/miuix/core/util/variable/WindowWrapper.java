package miuix.core.util.variable;

import android.view.View;
import android.view.Window;
import java.lang.reflect.Method;
import miuix.reflect.Reflects;

/* JADX INFO: loaded from: classes2.dex */
public abstract class WindowWrapper {
    protected static Method setExtraFlags;

    static {
        try {
            Class cls = Integer.TYPE;
            setExtraFlags = Reflects.getMethod(Window.class, "setExtraFlags", cls, cls);
        } catch (Exception unused) {
            setExtraFlags = null;
        }
    }

    public static boolean setTranslucentStatus(Window window, int i) {
        int i2 = 17;
        if (setExtraFlags == null) {
            return false;
        }
        if (i == 0) {
            window.clearFlags(Integer.MIN_VALUE);
        } else {
            window.addFlags(Integer.MIN_VALUE);
            View decorView = window.getDecorView();
            if (i == 1) {
                decorView.setSystemUiVisibility(8208);
            } else {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & (-8209));
            }
        }
        if (i == 0) {
            window.clearFlags(67108864);
        } else {
            window.setFlags(67108864, 67108864);
        }
        try {
            if (i == 0) {
                setExtraFlags.invoke(window, 0, 17);
                return true;
            }
            if (i != 1) {
                i2 = 1;
            }
            setExtraFlags.invoke(window, Integer.valueOf(i2), 17);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
