package miuix.core.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/* JADX INFO: loaded from: classes2.dex */
public abstract class WindowUtils {
    public static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService("window");
    }

    public static Display getDisplay(Context context) {
        try {
            return context.getDisplay();
        } catch (UnsupportedOperationException unused) {
            Log.w("WindowUtils", "This context is not associated with a display. You should use createDisplayContext() to create a display context to work with windows.");
            return getWindowManager(context).getDefaultDisplay();
        }
    }

    public static void getWindowSize(Context context, Point point) {
        getWindowSize(getWindowManager(context), context, point);
    }

    public static void getWindowSize(WindowManager windowManager, Context context, Point point) {
        Rect bounds = windowManager.getCurrentWindowMetrics().getBounds();
        point.x = bounds.width();
        point.y = bounds.height();
    }

    public static Point getScreenSize(Context context) {
        Point point = new Point();
        getScreenSize(context, point);
        return point;
    }

    public static void getScreenSize(Context context, Point point) {
        getScreenSize(getWindowManager(context), context, point);
    }

    public static void getScreenSize(WindowManager windowManager, Context context, Point point) {
        Rect bounds = windowManager.getMaximumWindowMetrics().getBounds();
        point.x = bounds.width();
        point.y = bounds.height();
    }

    public static Point getWindowSize(Context context) {
        Point point = new Point();
        getWindowSize(context, point);
        return point;
    }

    public static int getWindowHeight(Context context) {
        return getWindowSize(context).y;
    }

    public static boolean isFreeformMode(Configuration configuration, Point point, Point point2) {
        float f = (point2.x + 0.0f) / point.x;
        float f2 = (point2.y + 0.0f) / point.y;
        if (configuration.toString().contains("mWindowingMode=freeform")) {
            return f <= 0.9f || f2 <= 0.9f;
        }
        return false;
    }

    public static int getScreenType(Configuration configuration) {
        String string = configuration.toString();
        if (string.contains("screenType=0")) {
            return 0;
        }
        if (string.contains("screenType=1")) {
            return 1;
        }
        if (string.contains("screenType=2")) {
            return 2;
        }
        if (string.contains("screenType=3")) {
            return 3;
        }
        if (string.contains("screenType=4")) {
            return 4;
        }
        return string.contains("screenType=5") ? 5 : 0;
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }
}
