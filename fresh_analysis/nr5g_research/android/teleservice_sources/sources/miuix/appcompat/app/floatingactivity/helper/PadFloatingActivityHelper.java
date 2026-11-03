package miuix.appcompat.app.floatingactivity.helper;

import android.graphics.Point;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.FloatingAnimHelper;
import miuix.core.util.EnvStateManager;
import miuix.core.util.WindowBaseInfo;

/* JADX INFO: loaded from: classes2.dex */
public class PadFloatingActivityHelper extends TabletFloatingActivityHelper {
    public PadFloatingActivityHelper(AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public boolean isFloatingModeSupport() {
        WindowBaseInfo windowInfo = EnvStateManager.getWindowInfo(this.mActivity);
        int i = windowInfo.windowMode;
        if (i == 0 || i == 8195 || i == 4099) {
            return true;
        }
        Point point = windowInfo.windowSizeDp;
        return point.y >= 747 && point.x > 670;
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.TabletFloatingActivityHelper
    public void execExitAnim() {
        if (FloatingAnimHelper.isSupportTransWithClipAnim()) {
            return;
        }
        boolean zIsFloatingWindow = isFloatingWindow();
        AppCompatActivity appCompatActivity = this.mActivity;
        if (zIsFloatingWindow) {
            FloatingAnimHelper.clearFloatingWindowAnim(appCompatActivity);
        } else if (FloatingAnimHelper.obtainPageIndex(appCompatActivity) >= 0) {
            FloatingAnimHelper.execFloatingWindowExitAnimRomNormal(this.mActivity);
        }
    }
}
