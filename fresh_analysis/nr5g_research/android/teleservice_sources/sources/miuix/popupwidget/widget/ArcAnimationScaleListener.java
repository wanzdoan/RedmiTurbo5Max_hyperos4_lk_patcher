package miuix.popupwidget.widget;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import java.util.ArrayList;
import java.util.Collection;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.core.util.MiuiBlurUtils;
import miuix.smooth.SmoothContainerDrawable2;
import miuix.smooth.SmoothFrameLayout2;

/* JADX INFO: loaded from: classes2.dex */
class ArcAnimationScaleListener extends TransitionListener {
    private final ArcAnimation mArcAnimation;
    private final ArrayList mColorModes = new ArrayList();
    private final View mContentView;
    private int mEndBottom;
    private int mEndHeight;
    private float mEndKGB;
    private int mEndLeft;
    private final float mEndRadius;
    private int mEndRight;
    private int mEndTop;
    private int mEndWidth;
    private int mLayerBottom;
    private int mLayerLeft;
    private int mLayerRight;
    private int mLayerTop;
    private int mStartBottom;
    private final float mStartKGB;
    private int mStartLeft;
    private final float mStartRadius;
    private int mStartRight;
    private int mStartTop;

    ArcAnimationScaleListener(ArcAnimation arcAnimation, Rect rect, int i, int i2) {
        this.mArcAnimation = arcAnimation;
        this.mEndLeft = rect.left;
        this.mEndTop = rect.top;
        this.mEndRight = rect.right;
        this.mEndBottom = rect.bottom;
        Rect startBounds = getStartBounds(rect, i, i2);
        this.mStartLeft = startBounds.left;
        this.mStartTop = startBounds.top;
        this.mStartRight = startBounds.right;
        this.mStartBottom = startBounds.bottom;
        View contentView = arcAnimation.getContentView();
        this.mContentView = contentView;
        this.mEndWidth = contentView.getWidth();
        int height = contentView.getHeight();
        this.mEndHeight = height;
        this.mStartKGB = 0.2f;
        int i3 = this.mEndWidth;
        this.mEndKGB = i3 == 0 ? 0.0f : height / i3;
        float f = contentView.getRootView().getContext().getResources().getDisplayMetrics().density;
        this.mStartRadius = 4.0f * f;
        this.mEndRadius = f * 24.0f;
        View offsetLayer = arcAnimation.getOffsetLayer();
        if (offsetLayer != null) {
            this.mLayerLeft = offsetLayer.getLeft();
            this.mLayerTop = offsetLayer.getTop();
            this.mLayerRight = offsetLayer.getRight();
            this.mLayerBottom = offsetLayer.getBottom();
        }
    }

    void updateScaleBounds(Rect rect, int i, int i2) {
        Rect startBounds = getStartBounds(rect, i, i2);
        this.mEndLeft = rect.left;
        this.mEndTop = rect.top;
        this.mEndRight = rect.right;
        this.mEndBottom = rect.bottom;
        this.mStartLeft = startBounds.left;
        this.mStartTop = startBounds.top;
        this.mStartRight = startBounds.right;
        this.mStartBottom = startBounds.bottom;
    }

    private Rect getStartBounds(Rect rect, int i, int i2) {
        int iWidth = rect.width();
        int iHeight = rect.height();
        int i3 = (int) (iWidth * 0.69f);
        int i4 = i3 / 5;
        this.mEndWidth = iWidth;
        this.mEndHeight = iHeight;
        this.mEndKGB = iWidth == 0 ? 0.0f : iHeight / iWidth;
        int absoluteGravity = Gravity.getAbsoluteGravity(i, i2);
        int[] iArrCalcHorizontalStart = calcHorizontalStart(absoluteGravity, rect, i3);
        int[] iArrCalcVerticalStart = calcVerticalStart(absoluteGravity, rect, i4);
        return new Rect(iArrCalcHorizontalStart[0], iArrCalcVerticalStart[0], iArrCalcHorizontalStart[1], iArrCalcVerticalStart[1]);
    }

    private int[] calcHorizontalStart(int i, Rect rect, int i2) {
        int i3 = i & 7;
        int iWidth = rect.width();
        if (i3 == 3) {
            return new int[]{rect.left, rect.right - (iWidth - i2)};
        }
        if (i3 == 1) {
            int iCenterX = rect.centerX() - (i2 / 2);
            return new int[]{iCenterX, i2 + iCenterX};
        }
        return new int[]{rect.left + (iWidth - i2), rect.right};
    }

    private int[] calcVerticalStart(int i, Rect rect, int i2) {
        int i3 = i & 112;
        int iHeight = rect.height();
        if (i3 == 48) {
            return new int[]{rect.top, rect.bottom - (iHeight - i2)};
        }
        if (i3 == 16) {
            int iCenterY = rect.centerY() - (i2 / 2);
            return new int[]{iCenterY, i2 + iCenterY};
        }
        return new int[]{rect.top + (iHeight - i2), rect.bottom};
    }

    @Override // miuix.animation.listener.TransitionListener
    public void onBegin(Object obj) {
        super.onBegin(obj);
        Object parent = this.mArcAnimation.getAnimContainerView().getParent();
        if ((parent instanceof View) && this.mArcAnimation.getIsShowAnim()) {
            ((View) parent).setVisibility(4);
        }
    }

    @Override // miuix.animation.listener.TransitionListener
    public void onUpdate(Object obj, Collection collection) {
        UpdateInfo updateInfoFindBy;
        UpdateInfo updateInfoFindByName = UpdateInfo.findByName(collection, "fraction");
        UpdateInfo updateInfoFindByName2 = UpdateInfo.findByName(collection, "position");
        if (updateInfoFindByName != null) {
            float floatValue = updateInfoFindByName.getFloatValue();
            if (floatValue < 0.0f) {
                floatValue = 0.0f;
            }
            updateViewBounds(floatValue, updateInfoFindByName2 != null ? Math.max(0.0f, updateInfoFindByName2.getFloatValue()) : floatValue);
            updateRadiusAnimation(floatValue);
        }
        if (!this.mArcAnimation.isBlurEnabled() || (updateInfoFindBy = UpdateInfo.findBy(collection, this.mArcAnimation.getPopupBlur())) == null) {
            return;
        }
        updateBlurEffect(updateInfoFindBy.getFloatValue());
    }

    private void updateViewBounds(float f, float f2) {
        Object parent;
        int i = this.mStartRight;
        int i2 = this.mStartLeft;
        int i3 = i - i2;
        int i4 = this.mEndRight;
        int i5 = this.mEndLeft;
        int i6 = (int) (i3 + (((i4 - i5) - i3) * f));
        float f3 = this.mStartKGB;
        float f4 = i6;
        int i7 = (int) (((f3 + ((this.mEndKGB - f3) * f)) * f4) + 0.5f);
        int i8 = (i2 + i) / 2;
        int i9 = (int) (i8 + ((((i5 + i4) / 2) - i8) * f2));
        int i10 = (this.mStartTop + this.mStartBottom) / 2;
        int i11 = i9 - (i6 / 2);
        int i12 = i6 + i11;
        int i13 = ((int) (i10 + ((((this.mEndTop + this.mEndBottom) / 2) - i10) * f2))) - (i7 / 2);
        int i14 = i7 + i13;
        View animContainerView = this.mArcAnimation.getAnimContainerView();
        if (animContainerView == null || (parent = animContainerView.getParent()) == null) {
            return;
        }
        if (parent instanceof View) {
            View view = (View) parent;
            view.setLeftTopRightBottom(i11, i13, i12, i14);
            if (view.getVisibility() != 0 && f > 0.1f) {
                view.setVisibility(0);
            }
        }
        View offsetLayer = this.mArcAnimation.getOffsetLayer();
        if (offsetLayer != null) {
            offsetLayer.setLeftTopRightBottom(this.mLayerLeft, this.mLayerTop, this.mLayerRight, this.mLayerBottom);
        }
        int i15 = this.mEndWidth;
        if (i15 == 0) {
            return;
        }
        float fMin = Math.min(f4 / i15, 1.0f);
        animContainerView.setPivotX(0.0f);
        animContainerView.setPivotY(0.0f);
        animContainerView.setScaleX(fMin);
        animContainerView.setScaleY(fMin);
    }

    private void applyCornerRadius(int i) {
        float f = i;
        ((SmoothFrameLayout2) this.mContentView).setCornerRadius(f);
        Drawable background = this.mContentView.getBackground();
        if (background instanceof SmoothContainerDrawable2) {
            ((SmoothContainerDrawable2) background).setCornerRadius(f);
        }
    }

    void updateRadiusAnimation(float f) {
        if (this.mArcAnimation.getIsShowAnim() && (this.mContentView instanceof SmoothFrameLayout2)) {
            float f2 = this.mEndRadius;
            if (f2 != 0.0f) {
                float f3 = this.mStartRadius;
                applyCornerRadius((int) (f3 + ((f2 - f3) * f)));
            }
        }
    }

    public void resetRadius() {
        if (!(this.mContentView instanceof SmoothFrameLayout2) || this.mEndRadius == 0.0f) {
            return;
        }
        applyCornerRadius((int) this.mStartRadius);
    }

    private void updateBlurEffect(float f) {
        View blurView = this.mArcAnimation.getBlurView();
        if (blurView != null) {
            setSelfBlur(blurView, (int) f);
        }
    }

    private void setSelfBlur(View view, int i) {
        if (MiuiBlurUtils.setSelfBlur(view, i, this.mColorModes, i)) {
            return;
        }
        MiuiBlurUtils.setSelfBlur(view, i, this.mColorModes);
    }

    @Override // miuix.animation.listener.TransitionListener
    public void onComplete(Object obj) {
        View animContainerView = this.mArcAnimation.getAnimContainerView();
        if (animContainerView == null) {
            return;
        }
        animContainerView.setScaleX(1.0f);
        animContainerView.setScaleY(1.0f);
        if ("end".equals(obj)) {
            Object parent = animContainerView.getParent();
            if (parent instanceof View) {
                ((View) parent).setLeftTopRightBottom(this.mEndLeft, this.mEndTop, this.mEndRight, this.mEndBottom);
            }
        }
    }
}
