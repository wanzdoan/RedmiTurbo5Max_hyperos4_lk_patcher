package miuix.appcompat.app;

import android.view.View;
import miuix.appcompat.app.strategy.IActionBarStrategy;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ActionBar extends androidx.appcompat.app.ActionBar {
    public abstract void addActionBarTransitionListener(ActionBarTransitionListener actionBarTransitionListener);

    public abstract View getActionBarView();

    public abstract boolean isAdsorptionToNoOverlay();

    public abstract void registerCoordinateScrollView(View view);

    public abstract void setActionBarStrategy(IActionBarStrategy iActionBarStrategy);

    public abstract void setProgressBarIndeterminateVisibility(boolean z);

    public abstract void unregisterCoordinateScrollView(View view);
}
