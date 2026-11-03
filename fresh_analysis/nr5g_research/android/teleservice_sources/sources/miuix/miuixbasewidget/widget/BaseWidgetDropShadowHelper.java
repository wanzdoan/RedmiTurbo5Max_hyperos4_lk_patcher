package miuix.miuixbasewidget.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MiShadowUtils;
import miuix.core.util.MiuixUIUtils;
import miuix.core.util.RomUtils;

/* JADX INFO: loaded from: classes2.dex */
class BaseWidgetDropShadowHelper {
    protected Context mContext;
    protected DropShadowConfig mDropShadowConfig;
    protected boolean mEnableMiShadow;
    protected boolean[] mOriginViewParentClipState;
    protected int mShadowColor;
    protected int mShadowColorAlpha;
    protected float mOffsetXPx = 0.0f;
    protected float mOffsetYPx = 0.0f;
    protected float mBlurRadiusPx = 0.0f;
    protected float mAlpha = 1.0f;
    protected float mLastDensity = 0.0f;
    protected RectF mShadowRect = new RectF();
    protected Paint mShadowPaint = new Paint();
    protected boolean mEnableShadow = false;

    public BaseWidgetDropShadowHelper(Context context, DropShadowConfig dropShadowConfig, boolean z) {
        boolean z2 = false;
        this.mContext = context;
        this.mDropShadowConfig = dropShadowConfig;
        if (RomUtils.getHyperOsVersion() >= 2 && MiShadowUtils.SUPPORT_MI_SHADOW) {
            z2 = true;
        }
        this.mEnableMiShadow = z2;
        updateShadowValues(z, context.getResources().getDisplayMetrics().density, dropShadowConfig);
    }

    public void setAlpha(float f) {
        if (this.mAlpha != f) {
            this.mAlpha = f;
            int i = (((int) (this.mShadowColorAlpha * f)) << 24) | (16777215 & this.mShadowColor);
            this.mShadowColor = i;
            this.mShadowPaint.setColor(i);
            this.mShadowPaint.setShadowLayer(this.mBlurRadiusPx, this.mOffsetXPx, this.mOffsetYPx, this.mShadowColor);
        }
    }

    public void invalidateShadow(View view, float f) {
        setAlpha(f);
        if (this.mEnableMiShadow) {
            refreshViewShadow(view);
        }
    }

    public void updateDropShadowConfig(MaterialConfig.ShadowConfig shadowConfig) {
        this.mDropShadowConfig = new DropShadowConfig.Builder(shadowConfig).create();
        updateShadowValues(true, this.mContext.getResources().getDisplayMetrics().density, this.mDropShadowConfig);
        this.mEnableShadow = this.mBlurRadiusPx > 0.0f;
    }

    public void refreshViewShadow(View view) {
        if (this.mEnableMiShadow) {
            if (this.mEnableShadow) {
                int i = this.mShadowColor;
                float f = this.mOffsetXPx;
                float f2 = this.mOffsetYPx;
                float f3 = this.mBlurRadiusPx;
                DropShadowConfig dropShadowConfig = this.mDropShadowConfig;
                MiShadowUtils.setMiShadow(view, i, f, f2, f3, dropShadowConfig.shadowDispersion, dropShadowConfig.clipShadowEnable);
                return;
            }
            MiShadowUtils.clearMiShadow(view);
            return;
        }
        view.invalidate();
    }

    public void updateViewShadow(View view, int i) {
        enableViewShadowInternal(view, this.mEnableShadow, i);
    }

    public void enableViewShadow(View view, boolean z, int i) {
        if (this.mEnableShadow == z) {
            return;
        }
        enableViewShadowInternal(view, z, i);
    }

    private void enableViewShadowInternal(View view, boolean z, int i) {
        this.mEnableShadow = z;
        refreshViewShadow(view);
        if (this.mEnableShadow) {
            this.mOriginViewParentClipState = new boolean[i];
            for (int i2 = 0; i2 < i; i2++) {
                Object parent = view.getParent();
                if (parent == null) {
                    return;
                }
                ViewGroup viewGroup = (ViewGroup) parent;
                this.mOriginViewParentClipState[i2] = viewGroup.getClipChildren();
                viewGroup.setClipChildren(false);
                view = (View) parent;
            }
            return;
        }
        boolean[] zArr = this.mOriginViewParentClipState;
        if (zArr != null && zArr.length >= i) {
            for (int i3 = 0; i3 < i; i3++) {
                Object parent2 = view.getParent();
                if (parent2 == null) {
                    break;
                }
                ((ViewGroup) parent2).setClipChildren(this.mOriginViewParentClipState[i3]);
                view = (View) parent2;
            }
        }
        this.mOriginViewParentClipState = null;
    }

    public void onConfigChanged(View view, Configuration configuration, boolean z) {
        updateShadowValues(z, (configuration.densityDpi * 1.0f) / 160.0f, this.mDropShadowConfig);
        this.mEnableShadow = this.mBlurRadiusPx > 0.0f;
        refreshViewShadow(view);
    }

    protected void updateShadowValues(boolean z, float f, DropShadowConfig dropShadowConfig) {
        int i = z ? dropShadowConfig.shadowColor : dropShadowConfig.shadowDarkColor;
        this.mShadowColor = i;
        this.mShadowColorAlpha = (i >> 24) & 255;
        this.mShadowPaint.setColor(i);
        if (this.mLastDensity != f) {
            this.mLastDensity = f;
            onDensityChanged(f, dropShadowConfig);
        }
        this.mShadowPaint.setShadowLayer(this.mBlurRadiusPx, this.mOffsetXPx, this.mOffsetYPx, this.mShadowColor);
    }

    protected void onDensityChanged(float f, DropShadowConfig dropShadowConfig) {
        this.mOffsetXPx = MiuixUIUtils.dp2px(f, dropShadowConfig.offsetXDp);
        this.mOffsetYPx = MiuixUIUtils.dp2px(f, dropShadowConfig.offsetYDp);
        this.mBlurRadiusPx = MiuixUIUtils.dp2px(f, dropShadowConfig.blurRadiusDp);
    }
}
