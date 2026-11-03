package miuix.core.util.screenutils;

import android.graphics.Point;
import miuix.core.util.WindowBaseInfo;

/* JADX INFO: loaded from: classes2.dex */
public abstract class SplitScreenModeHelper {
    private static boolean isInRegion(float f, float f2, float f3) {
        return f >= f2 && f < f3;
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0033  */
    /* JADX WARN: Code duplicated, block: B:14:0x0038  */
    /* JADX WARN: Code duplicated, block: B:16:0x0041  */
    /* JADX WARN: Code duplicated, block: B:18:0x0046  */
    /* JADX WARN: Code duplicated, block: B:20:0x004f  */
    /* JADX WARN: Code duplicated, block: B:22:0x0054  */
    public static void detectSplitScreenInfo(WindowBaseInfo windowBaseInfo, Point point) {
        float f;
        float f2;
        int i;
        if (isScreenLandscape(point)) {
            f2 = windowBaseInfo.windowSize.x;
            i = point.x;
        } else {
            Point point2 = windowBaseInfo.windowSize;
            float f3 = point2.x / (point.x + 0.0f);
            if (f3 >= 0.95f) {
                f2 = point2.y;
                i = point.y;
            } else {
                f = f3;
            }
            if (isInRegion(f, 0.0f, 0.4f)) {
                windowBaseInfo.windowMode = 4097;
                return;
            }
            if (isInRegion(f, 0.4f, 0.6f)) {
                windowBaseInfo.windowMode = 4098;
            } else if (isInRegion(f, 0.6f, 0.8f)) {
                windowBaseInfo.windowMode = 4099;
            } else {
                windowBaseInfo.windowMode = 0;
            }
        }
        f = f2 / (i + 0.0f);
        if (isInRegion(f, 0.0f, 0.4f)) {
            windowBaseInfo.windowMode = 4097;
            return;
        }
        if (isInRegion(f, 0.4f, 0.6f)) {
            windowBaseInfo.windowMode = 4098;
        } else if (isInRegion(f, 0.6f, 0.8f)) {
            windowBaseInfo.windowMode = 4099;
        } else {
            windowBaseInfo.windowMode = 0;
        }
    }

    private static boolean isScreenLandscape(Point point) {
        return point.x > point.y;
    }
}
