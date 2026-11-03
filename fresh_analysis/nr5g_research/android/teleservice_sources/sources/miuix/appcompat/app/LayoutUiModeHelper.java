package miuix.appcompat.app;

import android.app.Activity;
import android.view.Window;
import miuix.core.util.EnvStateManager;

/* JADX INFO: loaded from: classes2.dex */
public abstract class LayoutUiModeHelper {
    public static void autoSetLayoutUiMode(Activity activity) {
        Window window;
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            if (appCompatActivity.isFloatingWindowTheme() && appCompatActivity.isInFloatingWindowMode()) {
                Window window2 = activity.getWindow();
                if (window2 != null) {
                    window2.addFlags(134217728);
                    return;
                }
                return;
            }
        }
        if (activity == null || (window = activity.getWindow()) == null) {
            return;
        }
        autoSetLayoutUiMode(activity, window);
    }

    public static void autoSetLayoutUiMode(Activity activity, Window window) {
        String string = activity.getResources().getConfiguration().toString();
        boolean zContains = string.contains("mWindowingMode=freeform");
        boolean zContains2 = string.contains("miui-magic-windows");
        if (!EnvStateManager.isFullScreenGestureMode(activity) && (zContains2 || !zContains)) {
            window.clearFlags(134217728);
        } else {
            window.addFlags(134217728);
        }
        window.addFlags(67108864);
    }
}
