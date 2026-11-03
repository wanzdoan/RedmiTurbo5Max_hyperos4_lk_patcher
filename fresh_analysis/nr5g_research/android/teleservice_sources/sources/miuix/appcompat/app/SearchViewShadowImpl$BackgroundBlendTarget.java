package miuix.appcompat.app;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes2.dex */
public class SearchViewShadowImpl$BackgroundBlendTarget {
    private float mBlend = 0.0f;
    private final Drawable mDrawable;

    SearchViewShadowImpl$BackgroundBlendTarget(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public float getBlend() {
        return this.mBlend;
    }

    public void setBlend(float f) {
        this.mBlend = f;
        if (f <= 0.0f) {
            this.mDrawable.setColorFilter(null);
        } else {
            this.mDrawable.setColorFilter(new PorterDuffColorFilter(Color.argb((int) ((Math.min(f, 1.0f) * 255.0f) + 0.5f), 255, 255, 255), PorterDuff.Mode.SRC_ATOP));
        }
    }
}
