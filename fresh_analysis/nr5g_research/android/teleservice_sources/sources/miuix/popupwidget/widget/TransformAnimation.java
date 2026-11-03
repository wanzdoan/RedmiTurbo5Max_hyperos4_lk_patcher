package miuix.popupwidget.widget;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.FolmeEase;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ValueProperty;
import miuix.core.util.MiuiBlurUtils;
import miuix.view.GhostView;
import miuix.view.ITransformableView;

/* JADX INFO: loaded from: classes2.dex */
public class TransformAnimation implements IPopupAnim {
    private AnimState mEndState;
    private final AnimConfig mForwardAnimConfig;
    private final ITransformableView mFromTransformable;
    private final AnimConfig mReverseAnimConfig;
    private boolean mSimplified;
    private AnimState mStartState;
    private final ITransformableView mToTransformable;
    private final List mPopupListeners = new ArrayList();
    private final int[] loc = new int[2];
    private boolean mCrossWindow = false;
    private boolean mAnimationOptionsLocked = false;
    private final AnimationOptions mOptions = new AnimationOptions();

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void update(int i) {
    }

    public TransformAnimation(ITransformableView iTransformableView, ITransformableView iTransformableView2) {
        this.mSimplified = false;
        this.mFromTransformable = iTransformableView;
        this.mToTransformable = iTransformableView2;
        this.mSimplified = iTransformableView.isSimplified() || iTransformableView2.isSimplified();
        this.mForwardAnimConfig = new AnimConfig().setEase(FolmeEase.spring(0.8f, 0.4f)).setSpecial(AnimationOptions.CONTAINER_CENTER_X, FolmeEase.spring(0.8f, 0.25f), new float[0]).setSpecial(AnimationOptions.CONTAINER_CENTER_Y, FolmeEase.spring(0.8f, 0.25f), new float[0]).setSpecial(AnimationOptions.ICON_MATERIAL, FolmeEase.linear(80L), new float[0]).setSpecial(AnimationOptions.CONTENT_MATERIAL, FolmeEase.linear(80L), 50L, new float[0]);
        this.mReverseAnimConfig = new AnimConfig().setEase(FolmeEase.spring(0.8f, 0.28f)).setSpecial(AnimationOptions.CONTAINER_CENTER_X, FolmeEase.spring(0.8f, 0.4f), new float[0]).setSpecial(AnimationOptions.CONTAINER_CENTER_Y, FolmeEase.spring(0.8f, 0.4f), new float[0]).setSpecial(AnimationOptions.ICON_MATERIAL, FolmeEase.linear(80L), 50L, new float[0]).setSpecial(AnimationOptions.CONTENT_MATERIAL, FolmeEase.linear(80L), new float[0]);
    }

    public void updateAnimationOptions() {
        if (this.mAnimationOptionsLocked) {
            return;
        }
        View containerView = this.mToTransformable.getContainerView();
        View contentView = this.mFromTransformable.getContentView();
        View containerView2 = this.mFromTransformable.getContainerView();
        if (containerView == null || containerView2 == null || contentView == null) {
            return;
        }
        int width = containerView2.getWidth();
        int height = containerView2.getHeight();
        int width2 = containerView.getWidth();
        int height2 = containerView.getHeight();
        int[] iArrComputeCoordinates = computeCoordinates(containerView2, contentView, containerView);
        int i = iArrComputeCoordinates[0];
        int i2 = iArrComputeCoordinates[1];
        int i3 = iArrComputeCoordinates[2] - i;
        int i4 = iArrComputeCoordinates[3] - i2;
        int left = containerView.getLeft();
        int top = containerView.getTop();
        int right = containerView.getRight();
        int bottom = containerView.getBottom();
        int i5 = left - i3;
        int i6 = top - i4;
        float f = (right + left) / 2.0f;
        float f2 = (top + bottom) / 2.0f;
        int cornerRadius = (int) this.mFromTransformable.getCornerRadius();
        int cornerRadius2 = (int) this.mToTransformable.getCornerRadius();
        this.mOptions.startWidth = width;
        this.mOptions.startHeight = height;
        this.mOptions.endWidth = width2;
        this.mOptions.endHeight = height2;
        this.mOptions.startCenterX = ((i5 + width) + i5) / 2.0f;
        this.mOptions.startCenterY = (i6 + (i6 + height)) / 2.0f;
        this.mOptions.endCenterX = f;
        this.mOptions.endCenterY = f2;
        this.mOptions.startRadius = cornerRadius;
        this.mOptions.endRadius = cornerRadius2;
        this.mOptions.containerWidth = width2;
        this.mOptions.containerHeight = height2;
        this.mOptions.centerX = f;
        this.mOptions.centerY = f2;
        this.mOptions.left = left;
        this.mOptions.top = top;
        this.mOptions.right = right;
        this.mOptions.bottom = bottom;
        this.mOptions.radius = cornerRadius2;
        updateAnimState();
    }

    private int[] computeCoordinates(View view, View view2, View view3) {
        int i;
        int i2;
        int i3;
        int i4;
        boolean z = this.mCrossWindow;
        int[] iArr = this.loc;
        if (z) {
            view.getLocationOnScreen(iArr);
            int[] iArr2 = this.loc;
            i = iArr2[0];
            i2 = iArr2[1];
            view2.getLocationOnScreen(iArr2);
            int[] iArr3 = this.loc;
            int i5 = iArr3[0];
            int i6 = iArr3[1];
            view3.getLocationOnScreen(iArr3);
            int[] iArr4 = this.loc;
            i3 = iArr4[0];
            i4 = iArr4[1];
            view3.getLocationInWindow(iArr4);
            int[] iArr5 = this.loc;
            int i7 = i3 - iArr5[0];
            int i8 = i4 - iArr5[1];
            this.mOptions.startFromContentLeft = i5 - i7;
            this.mOptions.startFromContentTop = i6 - i8;
            AnimationOptions animationOptions = this.mOptions;
            animationOptions.startFromContentRight = animationOptions.startFromContentLeft + view2.getWidth();
            AnimationOptions animationOptions2 = this.mOptions;
            animationOptions2.startFromContentBottom = animationOptions2.startFromContentTop + view2.getHeight();
        } else {
            view.getLocationInWindow(iArr);
            int[] iArr6 = this.loc;
            i = iArr6[0];
            i2 = iArr6[1];
            view2.getLocationInWindow(iArr6);
            this.mOptions.startFromContentLeft = this.loc[0];
            this.mOptions.startFromContentTop = this.loc[1];
            this.mOptions.startFromContentRight = this.loc[0] + view2.getWidth();
            this.mOptions.startFromContentBottom = this.loc[1] + view2.getHeight();
            view3.getLocationInWindow(this.loc);
            int[] iArr7 = this.loc;
            i3 = iArr7[0];
            i4 = iArr7[1];
        }
        return new int[]{i, i2, i3, i4};
    }

    private void updateAnimState() {
        this.mStartState = new AnimState().add(AnimationOptions.POPUP_FRACTION, 0.0d).add(AnimationOptions.CONTAINER_CENTER_X, this.mOptions.startCenterX).add(AnimationOptions.CONTAINER_CENTER_Y, this.mOptions.startCenterY).add(AnimationOptions.CONTENT_MATERIAL, 0.0d).add(AnimationOptions.ICON_MATERIAL, 0.0d);
        this.mEndState = new AnimState().add(AnimationOptions.POPUP_FRACTION, 1.0d).add(AnimationOptions.CONTAINER_CENTER_X, this.mOptions.endCenterX).add(AnimationOptions.CONTAINER_CENTER_Y, this.mOptions.endCenterY).add(AnimationOptions.CONTENT_MATERIAL, 1.0d).add(AnimationOptions.ICON_MATERIAL, 1.0d);
    }

    public void setCrossWindow(boolean z) {
        this.mCrossWindow = z;
    }

    public void resetFromContentView() {
        View contentView = this.mFromTransformable.getContentView();
        if (contentView != null) {
            contentView.setVisibility(0);
        }
        this.mFromTransformable.restoreAppearance();
    }

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void showWithAnim() {
        final GhostView ghostViewAddGhost;
        final View containerView = this.mToTransformable.getContainerView();
        final View contentView = this.mToTransformable.getContentView();
        View containerView2 = this.mFromTransformable.getContainerView();
        View contentView2 = this.mFromTransformable.getContentView();
        if (containerView == null || contentView == null || containerView2 == null || contentView2 == null) {
            return;
        }
        this.mFromTransformable.captureAppearance();
        if (this.mSimplified) {
            ghostViewAddGhost = null;
        } else {
            ghostViewAddGhost = GhostView.addGhost(contentView2, (ViewGroup) containerView.getRootView());
            contentView2.setVisibility(4);
        }
        containerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: miuix.popupwidget.widget.TransformAnimation.1
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                containerView.getViewTreeObserver().removeOnPreDrawListener(this);
                TransformAnimation.this.mFromTransformable.clearAppearance();
                TransformAnimation.this.updateAnimationOptions();
                if (TransformAnimation.this.mOptions.startWidth != 0 && TransformAnimation.this.mOptions.endWidth != 0 && TransformAnimation.this.mOptions.startHeight != 0 && TransformAnimation.this.mOptions.endHeight != 0) {
                    TransformAnimation.this.mAnimationOptionsLocked = true;
                    TransformAnimation.this.mForwardAnimConfig.addListeners(new TransitionListener() { // from class: miuix.popupwidget.widget.TransformAnimation.1.1
                        @Override // miuix.animation.listener.TransitionListener
                        public void onBegin(Object obj) {
                            TransformAnimation.this.notifyForwardStart();
                        }

                        @Override // miuix.animation.listener.TransitionListener
                        public void onUpdate(Object obj, Collection collection) {
                            TransformAnimation.this.mOptions.updateToContainer(TransformAnimation.this.mToTransformable);
                            TransformAnimation.this.mOptions.updateToContent(contentView);
                            TransformAnimation.this.mOptions.updateFromContent(ghostViewAddGhost);
                            TransformAnimation.this.mOptions.updateFloatingAlpha(TransformAnimation.this.mFromTransformable, TransformAnimation.this.mToTransformable);
                        }

                        @Override // miuix.animation.listener.TransitionListener
                        public void onComplete(Object obj) {
                            TransformAnimation.this.mForwardAnimConfig.removeListeners(this);
                            GhostView ghostView = ghostViewAddGhost;
                            if (ghostView != null) {
                                ghostView.removeGhost();
                            }
                            TransformAnimation.this.mAnimationOptionsLocked = false;
                            TransformAnimation.this.notifyForwardEnd();
                        }

                        @Override // miuix.animation.listener.TransitionListener
                        public void onCancel(Object obj) {
                            TransformAnimation.this.mForwardAnimConfig.removeListeners(this);
                            TransformAnimation.this.mAnimationOptionsLocked = false;
                            TransformAnimation.this.notifyForwardCancel();
                        }
                    });
                    Folme.use(TransformAnimation.this.mOptions).setTo(TransformAnimation.this.mStartState).to(TransformAnimation.this.mEndState, TransformAnimation.this.mForwardAnimConfig);
                }
                return false;
            }
        });
    }

    @Override // miuix.popupwidget.widget.IPopupAnim
    public void dismissWithAnim() {
        GhostView ghostViewAddGhost;
        View containerView = this.mToTransformable.getContainerView();
        View contentView = this.mToTransformable.getContentView();
        View containerView2 = this.mFromTransformable.getContainerView();
        View contentView2 = this.mFromTransformable.getContentView();
        if (containerView == null || contentView == null || containerView2 == null || contentView2 == null) {
            notifyReverseEnd();
            return;
        }
        this.mAnimationOptionsLocked = true;
        this.mFromTransformable.clearAppearance();
        View elevationView = this.mToTransformable.getElevationView();
        if (elevationView != null) {
            this.mFromTransformable.applyTransformElevation(elevationView);
        }
        if (this.mSimplified) {
            ghostViewAddGhost = null;
        } else {
            ghostViewAddGhost = GhostView.addGhost(contentView2, (ViewGroup) containerView.getRootView());
            contentView2.setVisibility(4);
        }
        this.mReverseAnimConfig.addListeners(new AnonymousClass2(contentView, ghostViewAddGhost, contentView2, containerView));
        if (contentView instanceof ViewGroup) {
            ((ViewGroup) contentView).suppressLayout(true);
        }
        Folme.use(this.mOptions).to(this.mStartState, this.mReverseAnimConfig);
    }

    /* JADX INFO: renamed from: miuix.popupwidget.widget.TransformAnimation$2, reason: invalid class name */
    class AnonymousClass2 extends TransitionListener {
        final /* synthetic */ View val$fromContentView;
        final /* synthetic */ GhostView val$ghostView;
        final /* synthetic */ View val$toContainerView;
        final /* synthetic */ View val$toContentView;

        AnonymousClass2(View view, GhostView ghostView, View view2, View view3) {
            this.val$toContentView = view;
            this.val$ghostView = ghostView;
            this.val$fromContentView = view2;
            this.val$toContainerView = view3;
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onBegin(Object obj) {
            TransformAnimation.this.notifyReverseStart();
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection collection) {
            TransformAnimation.this.mOptions.updateToContainer(TransformAnimation.this.mToTransformable);
            TransformAnimation.this.mOptions.updateToContent(this.val$toContentView);
            TransformAnimation.this.mOptions.updateFromContent(this.val$ghostView);
            TransformAnimation.this.mOptions.updateFloatingAlpha(TransformAnimation.this.mFromTransformable, TransformAnimation.this.mToTransformable);
            if (!TransformAnimation.this.mSimplified || TransformAnimation.this.mOptions.fraction > 0.0f) {
                return;
            }
            Folme.use(TransformAnimation.this.mOptions).end();
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            View view = this.val$toContentView;
            if (view instanceof ViewGroup) {
                ((ViewGroup) view).suppressLayout(false);
            }
            this.val$fromContentView.setVisibility(0);
            TransformAnimation.this.mReverseAnimConfig.removeListeners(this);
            TransformAnimation.this.mFromTransformable.restoreAppearance();
            TransformAnimation.this.mAnimationOptionsLocked = false;
            GhostView ghostView = this.val$ghostView;
            if (ghostView != null) {
                ghostView.removeGhost();
            }
            this.val$toContainerView.setAlpha(0.0f);
            this.val$toContainerView.post(new Runnable() { // from class: miuix.popupwidget.widget.TransformAnimation$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TransformAnimation.this.notifyReverseEnd();
                }
            });
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onCancel(Object obj) {
            View view = this.val$toContentView;
            if (view instanceof ViewGroup) {
                ((ViewGroup) view).suppressLayout(false);
            }
            TransformAnimation.this.mReverseAnimConfig.removeListeners(this);
            TransformAnimation.this.mAnimationOptionsLocked = false;
        }
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

    class AnimationOptions {
        private static final ValueProperty CONTAINER_CENTER_X;
        private static final ValueProperty CONTAINER_CENTER_Y;
        private int bottom;
        private float centerX;
        private float centerY;
        private float containerHeight;
        private float containerWidth;
        private float contentAlpha;
        private int contentBlur;
        private float contentMaterialFraction;
        private float contentScaleX;
        private float contentScaleY;
        private float endCenterX;
        private float endCenterY;
        private int endHeight;
        private int endRadius;
        private int endWidth;
        private float fraction;
        private float fromContentAlpha;
        private int fromContentBlur;
        private float fromContentScaleX;
        private float iconMaterialFraction;
        private int left;
        private int radius;
        private int right;
        private float startCenterX;
        private float startCenterY;
        private int startFromContentBottom;
        private int startFromContentLeft;
        private int startFromContentRight;
        private int startFromContentTop;
        private int startHeight;
        private int startRadius;
        private int startWidth;
        private int top;
        private static final ValueProperty POPUP_FRACTION = new ValueProperty("fraction") { // from class: miuix.popupwidget.widget.TransformAnimation.AnimationOptions.1
            @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
            public float getValue(AnimationOptions animationOptions) {
                return animationOptions.fraction;
            }

            @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
            public void setValue(AnimationOptions animationOptions, float f) {
                animationOptions.fraction = f;
                animationOptions.containerWidth = ((animationOptions.endWidth - animationOptions.startWidth) * f) + animationOptions.startWidth;
                animationOptions.containerHeight = ((animationOptions.endHeight - animationOptions.startHeight) * f) + animationOptions.startHeight;
                animationOptions.radius = (int) (((animationOptions.endRadius - animationOptions.startRadius) * f) + animationOptions.startRadius);
                animationOptions.contentScaleX = animationOptions.containerWidth / animationOptions.endWidth;
                animationOptions.contentScaleY = animationOptions.contentScaleX;
                animationOptions.fromContentScaleX = animationOptions.containerWidth / animationOptions.startWidth;
            }
        };
        private static final ValueProperty ICON_MATERIAL = new ValueProperty("icon_material") { // from class: miuix.popupwidget.widget.TransformAnimation.AnimationOptions.2
            @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
            public float getValue(AnimationOptions animationOptions) {
                return animationOptions.iconMaterialFraction;
            }

            @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
            public void setValue(AnimationOptions animationOptions, float f) {
                animationOptions.iconMaterialFraction = f;
                animationOptions.fromContentAlpha = ((-1.0f) * f) + 1.0f;
                animationOptions.fromContentBlur = (int) ((50.0f * f) + 0.0f);
            }
        };
        private static final ValueProperty CONTENT_MATERIAL = new ValueProperty("content_material") { // from class: miuix.popupwidget.widget.TransformAnimation.AnimationOptions.3
            @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
            public float getValue(AnimationOptions animationOptions) {
                return animationOptions.contentMaterialFraction;
            }

            @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
            public void setValue(AnimationOptions animationOptions, float f) {
                animationOptions.contentMaterialFraction = f;
                animationOptions.contentAlpha = (1.0f * f) + 0.0f;
                animationOptions.contentBlur = (int) (((-50.0f) * f) + 50.0f);
            }
        };
        private final float startContentAlpha = 0.0f;
        private final float endContentAlpha = 1.0f;
        private final float startFromContentAlpha = 1.0f;
        private final float endFromContentAlpha = 0.0f;
        private final int startContentBlur = 50;
        private final int endContentBlur = 0;
        private final int startFromContentBlur = 0;
        private final int endFromContentBlur = 50;

        AnimationOptions() {
        }

        static {
            float f = 1.0f;
            CONTAINER_CENTER_X = new ValueProperty("container_center_x", f) { // from class: miuix.popupwidget.widget.TransformAnimation.AnimationOptions.4
                @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
                public float getValue(AnimationOptions animationOptions) {
                    return animationOptions.centerX;
                }

                @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
                public void setValue(AnimationOptions animationOptions, float f2) {
                    animationOptions.centerX = f2;
                }
            };
            CONTAINER_CENTER_Y = new ValueProperty("container_center_y", f) { // from class: miuix.popupwidget.widget.TransformAnimation.AnimationOptions.5
                @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
                public float getValue(AnimationOptions animationOptions) {
                    return animationOptions.centerY;
                }

                @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
                public void setValue(AnimationOptions animationOptions, float f2) {
                    animationOptions.centerY = f2;
                }
            };
        }

        public void updateToContainer(ITransformableView iTransformableView) {
            this.left = Math.round(this.centerX - (this.containerWidth / 2.0f));
            this.top = Math.round(this.centerY - (this.containerHeight / 2.0f));
            this.right = Math.round(this.left + this.containerWidth);
            this.bottom = Math.round(this.top + this.containerHeight);
            iTransformableView.getContainerView().setLeftTopRightBottom(this.left, this.top, this.right, this.bottom);
            iTransformableView.setCornerRadius(this.radius);
        }

        public void updateFromContent(GhostView ghostView) {
            if (ghostView == null) {
                return;
            }
            int iRound = Math.round(this.centerX - this.startCenterX);
            int iRound2 = Math.round(this.centerY - this.startCenterY);
            int i = this.startFromContentLeft + iRound;
            int i2 = this.startFromContentTop + iRound2;
            int i3 = this.startFromContentRight + iRound;
            int i4 = this.startFromContentBottom + iRound2;
            ghostView.setTranslationX(i - ghostView.getLeft());
            ghostView.setTranslationY(i2 - ghostView.getTop());
            ghostView.setAlpha(this.fromContentAlpha);
            ghostView.setScaleX(this.fromContentScaleX);
            ghostView.setPivotX((i3 - i) / 2.0f);
            ghostView.setPivotY((i4 - i2) / 2.0f);
            MiuiBlurUtils.setSelfBlur(ghostView, this.fromContentBlur, null);
        }

        public void updateToContent(View view) {
            view.setPivotX(0.0f);
            view.setPivotY(0.0f);
            view.setScaleX(this.contentScaleX);
            view.setScaleY(this.contentScaleY);
            view.setAlpha(this.contentAlpha);
            MiuiBlurUtils.setSelfBlur(view, this.contentBlur, null);
        }

        public void updateFloatingAlpha(ITransformableView iTransformableView, ITransformableView iTransformableView2) {
            View containerView = iTransformableView2.getContainerView();
            if (!iTransformableView.isFloating()) {
                containerView.setAlpha(this.iconMaterialFraction);
            } else {
                containerView.setAlpha(1.0f);
            }
        }
    }
}
