package miuix.popupwidget.widget;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public interface IPopupAnim {
    void addPopupAnimationListener(IPopupAnimationListener iPopupAnimationListener);

    void dismissWithAnim();

    void removePopupAnimationListener(IPopupAnimationListener iPopupAnimationListener);

    default void setBlurEnabled(boolean z) {
    }

    default void setBlurView(View view) {
    }

    default void setGravity(int i) {
    }

    void showWithAnim();

    void update(int i);
}
