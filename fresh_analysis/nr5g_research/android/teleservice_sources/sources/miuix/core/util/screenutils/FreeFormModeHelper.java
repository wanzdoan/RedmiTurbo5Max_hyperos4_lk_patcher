package miuix.core.util.screenutils;

import android.content.Context;
import android.graphics.Point;
import miuix.core.util.WindowBaseInfo;
import miuix.core.util.WindowUtils;

/* JADX INFO: loaded from: classes2.dex */
public abstract class FreeFormModeHelper {
    public static WindowBaseInfo detectFreeFormInfo(WindowBaseInfo windowBaseInfo, Context context, Point point) {
        return acquireFreeFormWindowRatioInternal(windowBaseInfo, context, point);
    }

    private static WindowBaseInfo acquireFreeFormWindowRatioInternal(WindowBaseInfo windowBaseInfo, Context context, Point point) {
        if (!WindowUtils.isFreeformMode(context.getResources().getConfiguration(), point, windowBaseInfo.windowSize)) {
            windowBaseInfo.windowMode &= -8193;
            return windowBaseInfo;
        }
        Point point2 = windowBaseInfo.windowSize;
        int i = point2.x;
        return freeFormModeRatioToCodeInternal(windowBaseInfo, i != 0 ? (point2.y * 1.0f) / i : 0.0f);
    }

    private static WindowBaseInfo freeFormModeRatioToCodeInternal(WindowBaseInfo windowBaseInfo, float f) {
        if (f <= 0.0f) {
            windowBaseInfo.windowMode = 0;
            return windowBaseInfo;
        }
        if (f >= 0.74f && f < 0.76f) {
            windowBaseInfo.windowMode = 8195;
            return windowBaseInfo;
        }
        if (f >= 1.32f && f < 1.34f) {
            windowBaseInfo.windowMode = 8194;
            return windowBaseInfo;
        }
        if (f >= 1.76f && f < 1.79f) {
            windowBaseInfo.windowMode = 8193;
            return windowBaseInfo;
        }
        windowBaseInfo.windowMode = 8196;
        return windowBaseInfo;
    }
}
