package miuix.popupwidget.widget;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.FolmeEase;
import miuix.animation.FolmeObject;
import miuix.animation.IFolme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.ValueProperty;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MiuiBlurUtils;
import miuix.popupwidget.R$id;
import miuix.smooth.SmoothContainerDrawable2;
import miuix.smooth.SmoothFrameLayout2;

/* JADX INFO: loaded from: classes2.dex */
public class DirectionAnimation implements FolmeObject, IPopupAnim {
    private final AnimConfig mAnimConfig;
    private final View mAnimContainerView;
    private float mBlur;
    private boolean mBlurEnabled;
    private View mBlurView;
    private final View mContentView;
    private final AnimConfig mEnterAlphaConfig;
    private Folme.ObjectFolmeImpl mFolmeAnimator;
    private float mFraction;
    private final View mOffsetLayer;
    private int mOffsetX;
    private int mOffsetY;
    private ScaleListener mScaleListener;
    private static final ValueProperty POPUP_FRACTION = new ValueProperty("fraction") { // from class: miuix.popupwidget.widget.DirectionAnimation.1
        @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
        public float getValue(DirectionAnimation directionAnimation) {
            return directionAnimation.mFraction;
        }

        @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
        public void setValue(DirectionAnimation directionAnimation, float f) {
            directionAnimation.mFraction = f;
        }
    };
    private static final ValueProperty POPUP_BLUR = new ValueProperty("popupBlur", 0.1f) { // from class: miuix.popupwidget.widget.DirectionAnimation.2
        @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
        public float getValue(DirectionAnimation directionAnimation) {
            return directionAnimation.mBlur;
        }

        @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
        public void setValue(DirectionAnimation directionAnimation, float f) {
            directionAnimation.mBlur = f;
        }
    };
    private boolean mInAnimation = false;
    private int mAnimGravity = 0;
    private final List mPopupListeners = new ArrayList();

    public DirectionAnimation(final View view) {
        View rootView = view.getRootView();
        this.mAnimContainerView = rootView.findViewById(R$id.miuix_popupwidget_popup_anim_container);
        this.mOffsetLayer = rootView.findViewById(R$id.miuix_popupwidget_popup_offset_anim_container);
        this.mEnterAlphaConfig = new AnimConfig().setEase(EaseManager.getStyle(1, 200.0f));
        this.mAnimConfig = new AnimConfig().setEase(EaseManager.getStyle(-2, 0.82f, 0.33f)).addListeners(new TransitionListener() { // from class: miuix.popupwidget.widget.DirectionAnimation.3
            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                if (DirectionAnimation.this.mAnimContainerView instanceof ViewGroup) {
                    ((ViewGroup) DirectionAnimation.this.mAnimContainerView).suppressLayout(true);
                }
                view.setLayerType(2, null);
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                if (DirectionAnimation.this.mAnimContainerView instanceof ViewGroup) {
                    ((ViewGroup) DirectionAnimation.this.mAnimContainerView).suppressLayout(false);
                }
                view.setLayerType(0, null);
                DirectionAnimation.this.mInAnimation = false;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onCancel(Object obj) {
                view.setLayerType(0, null);
                DirectionAnimation.this.mInAnimation = false;
            }
        });
        this.mContentView = view;
    }

    @Override // miuix.animation.FolmeObject
    public void setFolmeImpl(Folme.ObjectFolmeImpl objectFolmeImpl) {
        this.mFolmeAnimator = objectFolmeImpl;
    }

    @Override // miuix.animation.FolmeObject
    public Folme.ObjectFolmeImpl folme() {
        return this.mFolmeAnimator;
    }

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void setGravity(int i) {
        if (this.mInAnimation) {
            return;
        }
        this.mAnimGravity = i;
    }

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void showWithAnim() {
        View view = this.mContentView;
        if (view == null || view.getParent() == null) {
            return;
        }
        this.mInAnimation = true;
        final int layoutDirection = this.mContentView.getLayoutDirection();
        this.mContentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: miuix.popupwidget.widget.DirectionAnimation.4
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                DirectionAnimation.this.mContentView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (!DirectionAnimation.this.mContentView.isAttachedToWindow() || DirectionAnimation.this.mContentView.getWidth() == 0 || DirectionAnimation.this.mContentView.getHeight() == 0) {
                    return true;
                }
                int left = DirectionAnimation.this.mContentView.getLeft();
                int top = DirectionAnimation.this.mContentView.getTop();
                int right = DirectionAnimation.this.mContentView.getRight();
                int bottom = DirectionAnimation.this.mContentView.getBottom();
                if (DirectionAnimation.this.mScaleListener != null) {
                    DirectionAnimation.this.mAnimConfig.removeListeners(DirectionAnimation.this.mScaleListener);
                }
                DirectionAnimation directionAnimation = DirectionAnimation.this;
                directionAnimation.mScaleListener = directionAnimation.new ScaleListener(new Rect(left, top, right, bottom), DirectionAnimation.this.mAnimGravity, layoutDirection);
                DirectionAnimation.this.mAnimConfig.addListeners(DirectionAnimation.this.mScaleListener);
                DirectionAnimation.this.mAnimConfig.addListeners(new TransitionListener() { // from class: miuix.popupwidget.widget.DirectionAnimation.4.1
                    @Override // miuix.animation.listener.TransitionListener
                    public void onBegin(Object obj) {
                        DirectionAnimation.this.notifyForwardStart();
                    }

                    @Override // miuix.animation.listener.TransitionListener
                    public void onComplete(Object obj) {
                        DirectionAnimation.this.notifyForwardEnd();
                        DirectionAnimation.this.mAnimConfig.removeListeners(this);
                    }

                    @Override // miuix.animation.listener.TransitionListener
                    public void onCancel(Object obj) {
                        DirectionAnimation.this.notifyForwardCancel();
                        DirectionAnimation.this.mAnimConfig.removeListeners(this);
                    }
                });
                IFolme iFolmeUse = Folme.use(DirectionAnimation.this.mContentView);
                ViewProperty viewProperty = ViewProperty.ALPHA;
                iFolmeUse.resetTo(viewProperty, Float.valueOf(0.0f)).to(viewProperty, Float.valueOf(1.0f), DirectionAnimation.this.mEnterAlphaConfig);
                AnimState animStateAdd = new AnimState().add(DirectionAnimation.POPUP_FRACTION, 0.0d);
                AnimState animStateAdd2 = new AnimState("end").add(DirectionAnimation.POPUP_FRACTION, 1.0d);
                if (DirectionAnimation.this.isBlurEnabled()) {
                    if (HyperMaterialUtils.getVersion() < 5) {
                        MiuiBlurUtils.setMiSelfBlurEnhanceFlag(DirectionAnimation.this.getBlurView(), 4096, 4096);
                    }
                    MiuiBlurUtils.setMiSelfBlurEnhanceFlag(DirectionAnimation.this.getBlurView(), 512, 3840);
                    animStateAdd.add(DirectionAnimation.POPUP_BLUR, 40.0d);
                    animStateAdd2.add(DirectionAnimation.POPUP_BLUR, 0.0d);
                    DirectionAnimation.this.mAnimConfig.setSpecial(DirectionAnimation.POPUP_BLUR, FolmeEase.linear(200L), new float[0]);
                }
                Folme.use((FolmeObject) DirectionAnimation.this);
                DirectionAnimation.this.folme().resetTo(animStateAdd).to(animStateAdd2, DirectionAnimation.this.mAnimConfig);
                return false;
            }
        });
    }

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void dismissWithAnim() {
        View view = this.mContentView;
        if (view == null || !view.isAttachedToWindow() || this.mContentView.getParent() == null) {
            notifyReverseEnd();
            return;
        }
        this.mInAnimation = true;
        if (this.mContentView instanceof ViewGroup) {
            Folme.use(this.mContentView).to(ViewProperty.ALPHA, Float.valueOf(0.0f), new AnimConfig().setEase(EaseManager.getStyle(1, 150.0f)).addListeners(new TransitionListener() { // from class: miuix.popupwidget.widget.DirectionAnimation.5
                @Override // miuix.animation.listener.TransitionListener
                public void onComplete(Object obj) {
                    super.onComplete(obj);
                    DirectionAnimation.this.folme().end();
                }
            }));
            AnimState animStateAdd = new AnimState().add(POPUP_FRACTION, 0.0d);
            if (isBlurEnabled()) {
                animStateAdd.add(POPUP_BLUR, 40.0d);
            }
            this.mAnimConfig.addListeners(new TransitionListener() { // from class: miuix.popupwidget.widget.DirectionAnimation.6
                @Override // miuix.animation.listener.TransitionListener
                public void onBegin(Object obj) {
                    DirectionAnimation.this.notifyReverseStart();
                }

                @Override // miuix.animation.listener.TransitionListener
                public void onComplete(Object obj) {
                    DirectionAnimation.this.notifyReverseEnd();
                    DirectionAnimation.this.mAnimConfig.removeListeners(this);
                }
            });
            Folme.useValue(this).to(animStateAdd, this.mAnimConfig);
        }
    }

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void update(int i) {
        if (this.mInAnimation) {
            return;
        }
        this.mAnimGravity = i;
        Rect rect = new Rect(this.mContentView.getLeft(), this.mContentView.getTop(), this.mContentView.getRight(), this.mContentView.getBottom());
        ScaleListener scaleListener = this.mScaleListener;
        if (scaleListener != null) {
            scaleListener.updateScaleBounds(rect, i, this.mContentView.getLayoutDirection());
        }
    }

    public int getAnimGravity() {
        return this.mAnimGravity;
    }

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void setBlurView(View view) {
        this.mBlurView = view;
    }

    public View getBlurView() {
        View view = this.mBlurView;
        return view != null ? view : this.mContentView;
    }

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void setBlurEnabled(boolean z) {
        this.mBlurEnabled = z;
    }

    public boolean isBlurEnabled() {
        return HyperMaterialUtils.isEnable() && this.mBlurEnabled;
    }

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void addPopupAnimationListener(IPopupAnimationListener iPopupAnimationListener) {
        this.mPopupListeners.add(iPopupAnimationListener);
    }

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void removePopupAnimationListener(IPopupAnimationListener iPopupAnimationListener) {
        this.mPopupListeners.remove(iPopupAnimationListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyForwardStart() {
        ArrayList arrayList = new ArrayList(this.mPopupListeners);
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            IPopupAnimationListener iPopupAnimationListener = (IPopupAnimationListener) obj;
            if (iPopupAnimationListener != null) {
                iPopupAnimationListener.onForwardAnimationStart();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyForwardEnd() {
        ArrayList arrayList = new ArrayList(this.mPopupListeners);
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            IPopupAnimationListener iPopupAnimationListener = (IPopupAnimationListener) obj;
            if (iPopupAnimationListener != null) {
                iPopupAnimationListener.onForwardAnimationEnd();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyForwardCancel() {
        ArrayList arrayList = new ArrayList(this.mPopupListeners);
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            IPopupAnimationListener iPopupAnimationListener = (IPopupAnimationListener) obj;
            if (iPopupAnimationListener != null) {
                iPopupAnimationListener.onForwardAnimationCancel();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyReverseStart() {
        ArrayList arrayList = new ArrayList(this.mPopupListeners);
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            IPopupAnimationListener iPopupAnimationListener = (IPopupAnimationListener) obj;
            if (iPopupAnimationListener != null) {
                iPopupAnimationListener.onReverseAnimationStart();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyReverseEnd() {
        ArrayList arrayList = new ArrayList(this.mPopupListeners);
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            IPopupAnimationListener iPopupAnimationListener = (IPopupAnimationListener) obj;
            if (iPopupAnimationListener != null) {
                iPopupAnimationListener.onReverseAnimationEnd();
            }
        }
    }

    class ScaleListener extends TransitionListener {
        private final ArrayList mColorModes = new ArrayList();
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
        private float mVGrav;

        public ScaleListener(Rect rect, int i, int i2) {
            this.mEndLeft = rect.left;
            this.mEndTop = rect.top;
            this.mEndRight = rect.right;
            this.mEndBottom = rect.bottom;
            Rect startBounds = getStartBounds(rect, i, i2);
            this.mStartLeft = startBounds.left;
            this.mStartTop = startBounds.top;
            this.mStartRight = startBounds.right;
            this.mStartBottom = startBounds.bottom;
            this.mEndWidth = DirectionAnimation.this.mContentView.getWidth();
            int height = DirectionAnimation.this.mContentView.getHeight();
            this.mEndHeight = height;
            this.mStartKGB = 0.2f;
            int i3 = this.mEndWidth;
            this.mEndKGB = i3 == 0 ? 0.0f : height / i3;
            float f = DirectionAnimation.this.mContentView.getRootView().getContext().getResources().getDisplayMetrics().density;
            this.mStartRadius = 4.0f * f;
            this.mEndRadius = f * 24.0f;
            if (DirectionAnimation.this.mOffsetLayer != null) {
                this.mLayerLeft = DirectionAnimation.this.mOffsetLayer.getLeft();
                this.mLayerTop = DirectionAnimation.this.mOffsetLayer.getTop();
                this.mLayerRight = DirectionAnimation.this.mOffsetLayer.getRight();
                this.mLayerBottom = DirectionAnimation.this.mOffsetLayer.getBottom();
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
            int iCenterX;
            int i3;
            int iCenterY;
            int i4;
            int iWidth = rect.width();
            int iHeight = rect.height();
            int i5 = (int) (iWidth * 0.15f);
            int i6 = i5 / 5;
            int absoluteGravity = Gravity.getAbsoluteGravity(i, i2) & 7;
            this.mEndWidth = iWidth;
            this.mEndHeight = iHeight;
            this.mEndKGB = iWidth == 0 ? 0.0f : iHeight / iWidth;
            if (absoluteGravity == 3) {
                iCenterX = rect.left;
                i3 = rect.right - (iWidth - i5);
            } else if (absoluteGravity == 1) {
                iCenterX = rect.centerX() - (i5 / 2);
                i3 = iCenterX + i5;
            } else {
                iCenterX = rect.left + (iWidth - i5);
                i3 = rect.right;
            }
            int absoluteGravity2 = Gravity.getAbsoluteGravity(i, i2) & 112;
            this.mVGrav = absoluteGravity2;
            if (absoluteGravity2 == 48) {
                iCenterY = rect.top;
                i4 = rect.bottom - (iHeight - i6);
            } else if (absoluteGravity2 == 16) {
                iCenterY = rect.centerY() - (i6 / 2);
                i4 = iCenterY + i6;
            } else {
                iCenterY = rect.top + (iHeight - i6);
                i4 = rect.bottom;
            }
            return new Rect(iCenterX, iCenterY, i3, i4);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection collection) {
            UpdateInfo updateInfoFindBy;
            UpdateInfo updateInfoFindByName = UpdateInfo.findByName(collection, "fraction");
            if (updateInfoFindByName != null) {
                float floatValue = updateInfoFindByName.getFloatValue();
                if (floatValue < 0.0f) {
                    floatValue = 0.0f;
                }
                updateViewBounds(floatValue);
                updateRadiusAnimation(floatValue);
            }
            if (!DirectionAnimation.this.isBlurEnabled() || (updateInfoFindBy = UpdateInfo.findBy(collection, DirectionAnimation.POPUP_BLUR)) == null) {
                return;
            }
            updateBlurEffect(updateInfoFindBy.getFloatValue());
        }

        /* JADX WARN: Code duplicated, block: B:18:0x006e  */
        /* JADX WARN: Code duplicated, block: B:21:0x007b  */
        /* JADX WARN: Code duplicated, block: B:24:0x00ac A[ORIG_RETURN, RETURN] */
        /* JADX WARN: Code duplicated, block: B:25:0x00ad  */
        private void updateViewBounds(float f) {
            int i;
            int i2;
            Object parent;
            int i3;
            int i4 = this.mStartLeft;
            int i5 = (int) (i4 + ((this.mEndLeft - i4) * f));
            int i6 = this.mStartRight;
            int i7 = (int) (i6 + ((this.mEndRight - i6) * f));
            float f2 = this.mStartKGB;
            float f3 = i7 - i5;
            int i8 = (int) (((f2 + ((this.mEndKGB - f2) * f)) * f3) + 0.5f);
            float f4 = this.mVGrav;
            if (f4 != 48.0f) {
                if (f4 == 16.0f) {
                    int i9 = (this.mStartTop + this.mStartBottom) / 2;
                    i2 = ((int) (i9 + ((((this.mEndTop + this.mEndBottom) / 2) - i9) * f))) - (i8 / 2);
                } else {
                    int i10 = this.mStartBottom;
                    int i11 = i10 - i8;
                    i = i10;
                    i2 = i11;
                }
                if (DirectionAnimation.this.mAnimContainerView == null && (parent = DirectionAnimation.this.mAnimContainerView.getParent()) != null) {
                    if (parent instanceof View) {
                        ((View) parent).setLeftTopRightBottom(i5, i2, i7, i);
                    }
                    if (DirectionAnimation.this.mOffsetLayer != null) {
                        DirectionAnimation.this.mOffsetLayer.setLeftTopRightBottom(this.mLayerLeft + DirectionAnimation.this.mOffsetX, this.mLayerTop + DirectionAnimation.this.mOffsetY, this.mLayerRight + DirectionAnimation.this.mOffsetX, this.mLayerBottom + DirectionAnimation.this.mOffsetY);
                    }
                    i3 = this.mEndWidth;
                    if (i3 == 0) {
                        return;
                    }
                    float f5 = f3 / i3;
                    DirectionAnimation.this.mAnimContainerView.setPivotX(0.0f);
                    DirectionAnimation.this.mAnimContainerView.setPivotY(0.0f);
                    DirectionAnimation.this.mAnimContainerView.setScaleX(f5);
                    DirectionAnimation.this.mAnimContainerView.setScaleY(f5);
                }
                return;
            }
            i2 = this.mStartTop;
            i = i8 + i2;
            if (DirectionAnimation.this.mAnimContainerView == null) {
                return;
            }
            if (parent instanceof View) {
                ((View) parent).setLeftTopRightBottom(i5, i2, i7, i);
            }
            if (DirectionAnimation.this.mOffsetLayer != null) {
                DirectionAnimation.this.mOffsetLayer.setLeftTopRightBottom(this.mLayerLeft + DirectionAnimation.this.mOffsetX, this.mLayerTop + DirectionAnimation.this.mOffsetY, this.mLayerRight + DirectionAnimation.this.mOffsetX, this.mLayerBottom + DirectionAnimation.this.mOffsetY);
            }
            i3 = this.mEndWidth;
            if (i3 == 0) {
                return;
            }
            float f6 = f3 / i3;
            DirectionAnimation.this.mAnimContainerView.setPivotX(0.0f);
            DirectionAnimation.this.mAnimContainerView.setPivotY(0.0f);
            DirectionAnimation.this.mAnimContainerView.setScaleX(f6);
            DirectionAnimation.this.mAnimContainerView.setScaleY(f6);
        }

        private void updateRadiusAnimation(float f) {
            if (DirectionAnimation.this.mContentView instanceof SmoothFrameLayout2) {
                float f2 = this.mEndRadius;
                if (f2 != 0.0f) {
                    float f3 = this.mStartRadius;
                    float f4 = (int) (f3 + ((f2 - f3) * f));
                    ((SmoothFrameLayout2) DirectionAnimation.this.mContentView).setCornerRadius(f4);
                    Drawable background = DirectionAnimation.this.mContentView.getBackground();
                    if (background instanceof SmoothContainerDrawable2) {
                        ((SmoothContainerDrawable2) background).setCornerRadius(f4);
                    }
                }
            }
        }

        private void updateBlurEffect(float f) {
            View blurView = DirectionAnimation.this.getBlurView();
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
            if (DirectionAnimation.this.mAnimContainerView != null && "end".equals(obj)) {
                Object parent = DirectionAnimation.this.mAnimContainerView.getParent();
                if (parent instanceof View) {
                    ((View) parent).setLeftTopRightBottom(this.mEndLeft, this.mEndTop, this.mEndRight, this.mEndBottom);
                }
            }
        }
    }
}
