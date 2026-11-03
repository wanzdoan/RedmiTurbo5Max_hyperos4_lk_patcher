package miuix.appcompat.internal.app.widget;

import android.view.ActionMode;
import miuix.view.ActionModeAnimationListener;

/* JADX INFO: loaded from: classes2.dex */
public interface ActionModeView {
    void addAnimationListener(ActionModeAnimationListener actionModeAnimationListener);

    void animateToVisibility(boolean z);

    void closeMode();

    int getViewHeight();

    void initForMode(ActionMode actionMode);

    void killMode();
}
