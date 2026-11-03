package miuix.appcompat.app;

import java.util.Collection;

/* JADX INFO: loaded from: classes2.dex */
public interface ActionBarTransitionListener {
    @Deprecated
    default void onActionBarMove(float f, float f2) {
    }

    void onActionBarResizing(int i, float f, int i2);

    void onExpandStateChanged(int i);

    default void onOverlayMaskStateChanged(boolean z) {
    }

    @Deprecated
    default void onTransitionBegin(Object obj) {
    }

    @Deprecated
    default void onTransitionComplete(Object obj) {
    }

    @Deprecated
    default void onTransitionUpdate(Object obj, Collection collection) {
    }
}
