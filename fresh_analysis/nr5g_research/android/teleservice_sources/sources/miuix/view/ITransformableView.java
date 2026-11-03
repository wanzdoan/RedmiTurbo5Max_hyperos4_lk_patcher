package miuix.view;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public interface ITransformableView {
    default void applyTransformElevation(View view) {
    }

    default void captureAppearance() {
    }

    default void clearAppearance() {
    }

    View getContainerView();

    View getContentView();

    float getCornerRadius();

    default View getElevationView() {
        return null;
    }

    default boolean isFloating() {
        return false;
    }

    default boolean isSimplified() {
        return false;
    }

    default void restoreAppearance() {
    }

    default void setCornerRadius(float f) {
    }
}
