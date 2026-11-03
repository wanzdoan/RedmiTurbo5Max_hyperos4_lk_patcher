package miuix.appcompat.app;

import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes2.dex */
public class SearchViewMaterialImpl$BackgroundAlphaTarget {
    private float mAlpha;
    private final Drawable mDrawable;

    SearchViewMaterialImpl$BackgroundAlphaTarget(Drawable drawable) {
        this.mDrawable = drawable;
        this.mAlpha = drawable.getAlpha() / 255.0f;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public void setAlpha(float f) {
        this.mAlpha = f;
        this.mDrawable.setAlpha((int) ((f * 255.0f) + 0.5f));
    }
}
