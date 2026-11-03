package miuix.view;

import android.content.res.Configuration;
import android.util.DisplayMetrics;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayConfig {
    public int defaultBitmapDensity;
    public float density;
    public int densityDpi;
    public float fontScale;
    public float scaledDensity;
    public int windowHeightDp;
    public int windowWidthDp;

    public DisplayConfig(DisplayMetrics displayMetrics) {
        int i = displayMetrics.densityDpi;
        this.defaultBitmapDensity = i;
        this.densityDpi = i;
        float f = displayMetrics.density;
        this.density = f;
        float f2 = displayMetrics.scaledDensity;
        this.scaledDensity = f2;
        this.fontScale = f2 / f;
        this.windowWidthDp = (int) ((displayMetrics.widthPixels / f) + 0.5f);
        this.windowHeightDp = (int) ((displayMetrics.heightPixels / f) + 0.5f);
    }

    public DisplayConfig(Configuration configuration) {
        this.windowWidthDp = configuration.screenWidthDp;
        this.windowHeightDp = configuration.screenHeightDp;
        int i = configuration.densityDpi;
        this.defaultBitmapDensity = i;
        this.densityDpi = i;
        float f = i * 0.00625f;
        this.density = f;
        float f2 = configuration.fontScale;
        this.fontScale = f2;
        this.scaledDensity = f * (f2 == 0.0f ? 1.0f : f2);
    }

    public boolean equals(Object obj) {
        if (obj instanceof DisplayConfig) {
            DisplayConfig displayConfig = (DisplayConfig) obj;
            if (Float.compare(this.density, displayConfig.density) == 0 && Float.compare(this.scaledDensity, displayConfig.scaledDensity) == 0 && Float.compare(this.fontScale, displayConfig.fontScale) == 0 && this.densityDpi == displayConfig.densityDpi && this.defaultBitmapDensity == displayConfig.defaultBitmapDensity) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return "{ densityDpi:" + this.densityDpi + ", density:" + this.density + ", windowWidthDp:" + this.windowWidthDp + ", windowHeightDp: " + this.windowHeightDp + ", scaledDensity:" + this.scaledDensity + ", fontScale: " + this.fontScale + ", defaultBitmapDensity:" + this.defaultBitmapDensity + "}";
    }
}
