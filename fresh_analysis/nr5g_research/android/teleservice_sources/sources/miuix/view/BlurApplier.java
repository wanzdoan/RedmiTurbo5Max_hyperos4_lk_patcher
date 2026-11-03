package miuix.view;

import android.view.View;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MiuiBlurUtils;

/* JADX INFO: loaded from: classes2.dex */
public interface BlurApplier {
    public static final BlurApplier UNIFORM = new BlurApplier() { // from class: miuix.view.BlurApplier.1
    };

    default boolean applyMaterial(View view, int i, int i2, MaterialConfig.BlurConfig blurConfig) {
        applyBlur(view, i, i2);
        return false;
    }

    default void applyBlur(View view, int i, int i2) {
        MiuiBlurUtils.setBackgroundBlurType(view, 0);
        MiuiBlurUtils.setBackgroundBlur(view, i, i2);
    }

    default void clearBlur(View view) {
        MiuiBlurUtils.clearBackgroundBlur(view);
    }
}
