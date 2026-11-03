package miuix.appcompat.app.strategy;

import miuix.appcompat.app.ActionBar;
import miuix.core.util.ScreenModeHelper;
import miuix.device.DeviceUtils;

/* JADX INFO: loaded from: classes2.dex */
public class CommonActionBarStrategy implements IActionBarStrategy {
    @Override // miuix.appcompat.app.strategy.IActionBarStrategy
    public ActionBarConfig config(ActionBar actionBar, ActionBarSpec actionBarSpec) {
        if (actionBar == null || actionBarSpec == null) {
            return null;
        }
        ActionBarConfig actionBarConfig = new ActionBarConfig();
        int i = actionBarSpec.actionBarWidthDp;
        if (actionBarSpec.isInFloatingWindowMode) {
            actionBarConfig.expandState = 0;
            actionBarConfig.resizable = false;
            actionBarConfig.endMenuMaxItemCount = 3;
            return actionBarConfig;
        }
        if (DeviceUtils.isLhasaDevice()) {
            actionBarConfig.expandState = 1;
            actionBarConfig.resizable = false;
            actionBarConfig.endMenuMaxItemCount = 3;
            return actionBarConfig;
        }
        if (i >= 960) {
            actionBarConfig.expandState = 0;
            actionBarConfig.resizable = false;
            actionBarConfig.endMenuMaxItemCount = 3;
            return actionBarConfig;
        }
        float f = i;
        int i2 = actionBarSpec.windowWidthDp;
        float f2 = i2 * 0.8f;
        int i3 = actionBarSpec.deviceType;
        if (f < f2) {
            if ((i3 == 2 && i2 > 600) || i > 410) {
                actionBarConfig.expandState = 0;
                actionBarConfig.resizable = false;
                if (i < 410) {
                    actionBarConfig.endMenuMaxItemCount = 2;
                    return actionBarConfig;
                }
                actionBarConfig.endMenuMaxItemCount = 3;
                return actionBarConfig;
            }
            actionBarConfig.resizable = true;
            actionBarConfig.endMenuMaxItemCount = 2;
            return actionBarConfig;
        }
        if ((i3 == 2 && i2 > 600) || ((i3 == 1 && i2 > actionBarSpec.windowHeightDp) || (((i3 == 3 || i3 == 4) && Math.min(i2, actionBarSpec.windowHeightDp) <= 550 && actionBarSpec.windowWidthDp > actionBarSpec.windowHeightDp) || (actionBarSpec.deviceType == 4 && Math.min(actionBarSpec.windowWidthDp, actionBarSpec.windowHeightDp) <= 330)))) {
            actionBarConfig.expandState = 0;
            actionBarConfig.resizable = false;
        } else if (!ScreenModeHelper.isInSplitScreenMode(actionBarSpec.windowMode) || actionBarSpec.deviceType == 2) {
            actionBarConfig.resizable = true;
        } else if (actionBarSpec.windowHeightDp / actionBarSpec.windowWidthDp < 1.7f) {
            actionBarConfig.expandState = 0;
            actionBarConfig.resizable = false;
        }
        actionBarConfig.endMenuMaxItemCount = 3;
        return actionBarConfig;
    }
}
