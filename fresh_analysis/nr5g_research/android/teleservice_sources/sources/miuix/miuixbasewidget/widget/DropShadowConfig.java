package miuix.miuixbasewidget.widget;

import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import miuix.core.util.MaterialConfig;

/* JADX INFO: loaded from: classes2.dex */
class DropShadowConfig {
    float blurRadiusDp;
    BlurMaskFilter.Blur blurStyle;
    boolean clipShadowEnable;
    float offsetXDp;
    float offsetYDp;
    int shadowColor;
    int shadowDarkColor;
    float shadowDispersion;

    DropShadowConfig(float f) {
        this(f, BlurMaskFilter.Blur.NORMAL);
    }

    DropShadowConfig(float f, BlurMaskFilter.Blur blur) {
        this(Color.parseColor("#0D000000"), Color.parseColor("#0DFFFFFF"), 0.0f, 0.0f, f, 1.0f, true, blur);
    }

    DropShadowConfig(int i, int i2, float f, float f2, float f3, float f4, boolean z, BlurMaskFilter.Blur blur) {
        this.shadowColor = i;
        this.shadowDarkColor = i2;
        this.offsetXDp = f;
        this.offsetYDp = f2;
        this.blurRadiusDp = f3;
        this.shadowDispersion = f4;
        this.blurStyle = blur;
        this.clipShadowEnable = z;
    }

    public class Builder {
        private final DropShadowConfig dropShadowConfig;

        public Builder(MaterialConfig.ShadowConfig shadowConfig) {
            if (shadowConfig == null) {
                this.dropShadowConfig = new DropShadowConfig(0.0f);
            } else {
                int i = shadowConfig.shadowColor;
                this.dropShadowConfig = new DropShadowConfig(i, i, shadowConfig.shadowOffsetX, shadowConfig.shadowOffsetY, shadowConfig.shadowRadius, shadowConfig.shadowDispersion, false, BlurMaskFilter.Blur.NORMAL);
            }
        }

        public DropShadowConfig create() {
            return this.dropShadowConfig;
        }
    }
}
