package miuix.appcompat.internal.app.widget;

import android.R;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import miuix.animation.Folme;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.FolmeEase;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$color;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$id;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.ActionBarTransitionListener;
import miuix.appcompat.internal.view.menu.action.ActionMenuView;
import miuix.appcompat.internal.view.menu.action.ResponsiveActionMenuView;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MaterialDayNightConfig;
import miuix.core.util.MiuiBlurUtils;
import miuix.core.util.MiuixUIUtils;
import miuix.core.util.RomUtils;
import miuix.core.util.WindowUtils;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.DeviceHelper;
import miuix.theme.token.hypermaterial.Blur;
import miuix.theme.token.hypermaterial.Mask;
import miuix.view.BlurApplier;
import miuix.view.BlurableWidget;
import miuix.view.MiuiBlurUiHelper;

/* JADX INFO: loaded from: classes2.dex */
public class ActionBarContainer extends FrameLayout implements BlurableWidget {
    private ActionBarContextView mActionBarContextView;
    private int mActionBarHeightGap;
    private int mActionBarHeightTotalGap;
    List mActionBarTransitionListeners;
    private ActionBarView mActionBarView;
    private int mActionButtonFloatingBgColor;
    private int mActionButtonFloatingState;
    private ActionMenuView mActionModeMenuView;
    private Drawable mBackground;
    private Drawable[] mBackgroundArray;
    private final MiuiBlurUiHelper mBlurHelper;
    private boolean mContentFadeModeActive;
    protected ActionBarCoordinateListener mCoordinateListener;
    private int mCoordinatedOffsetYInSearchModeAnimation;
    private int mCurBarExpandState;
    private boolean mCurBarResizable;
    private int mCurContextBarExpandState;
    private boolean mCurContextBarResizable;
    private boolean mCustomBackground;
    private boolean mCustomViewAutoFitSystemWindow;
    private int mDefaultMaskColor;
    private boolean mDrawBackground;
    private final BlurApplier mGradientBlurApplier;
    private boolean mGradientNeedsUpdate;
    private Paint mGradientPaint;
    private Path mGradientPath;
    private LinearGradient mGradientShader;
    private int mHeightMaxMeasureSpec;
    private boolean mInternalApplyBgBlur;
    private boolean mInternalApplySpiltBgBlur;
    private boolean mIsDarkThemeOverlay;
    private boolean mIsInActionMode;
    private boolean mIsInWideMode;
    private boolean mIsMiuixFloating;
    private boolean mIsSplit;
    private boolean mIsStacked;
    private boolean mIsTemporaryOverlayMode;
    private boolean mIsTransitioning;
    private float mLastActionBarResizingProcess;
    private int mLastGradientHeight;
    private int mLastGradientWidth;
    private int mLastToState;
    private float mMaskAlpha;
    private ValueAnimator mMaskAnimator;
    private int mMaskColor;
    private float mMaskExtraHeight;
    private MaterialDayNightConfig mMaterial;
    private boolean mNowShowing;
    private boolean mOverlayMode;
    private ViewTreeObserver.OnGlobalLayoutListener mPendingGradientLayoutListener;
    private Rect mPendingInsets;
    private boolean mRequestAnimation;
    private ActionMenuView mResidentActionMenuView;
    private boolean mShouldDrawOverlayMask;
    private AnimConfig mShowAnimConfig;
    private Drawable mSplitBackground;
    private boolean mSplitEnableBlur;
    private Drawable mStackedBackground;
    private View mStickyView;
    private boolean mStickyViewCollapsible;
    private View mTabContainer;
    private int mTabContainerPaddingTop;
    private Boolean mUserApplyBgBlur;
    private Boolean mUserApplySplitActionBarBgBlur;
    private Boolean mUserBgViewApplyBlur;
    private BlurApplier mUserBlurApplier;

    private void drawSplitMode(Canvas canvas) {
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return null;
    }

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    public ActionBarContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mNowShowing = true;
        this.mGradientBlurApplier = new BlurApplier() { // from class: miuix.appcompat.internal.app.widget.ActionBarContainer.1
            @Override // miuix.view.BlurApplier
            public void applyBlur(View view, int i, int i2) {
                ActionBarContainer.this.applyVerticalGradientBlur(view, i2);
            }
        };
        this.mInternalApplyBgBlur = false;
        this.mInternalApplySpiltBgBlur = false;
        this.mUserApplyBgBlur = null;
        this.mUserApplySplitActionBarBgBlur = null;
        this.mUserBgViewApplyBlur = null;
        this.mSplitEnableBlur = false;
        this.mResidentActionMenuView = null;
        this.mActionModeMenuView = null;
        this.mCustomBackground = false;
        this.mHeightMaxMeasureSpec = -1;
        this.mLastActionBarResizingProcess = 0.0f;
        this.mLastToState = 0;
        this.mActionBarHeightTotalGap = 0;
        this.mActionBarHeightGap = 0;
        this.mIsDarkThemeOverlay = false;
        this.mLastGradientHeight = -1;
        this.mLastGradientWidth = -1;
        this.mGradientNeedsUpdate = true;
        this.mShouldDrawOverlayMask = false;
        this.mMaskAlpha = 0.0f;
        this.mActionButtonFloatingState = -1;
        this.mActionButtonFloatingBgColor = -1;
        this.mContentFadeModeActive = false;
        this.mIsTemporaryOverlayMode = false;
        this.mCoordinateListener = null;
        this.mActionBarTransitionListeners = new CopyOnWriteArrayList();
        setBackground(null);
        this.mGradientPath = new Path();
        this.mGradientPaint = new Paint(1);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ActionBar);
        try {
            this.mIsDarkThemeOverlay = MiuixUIUtils.isDarkThemeOverlay(getContext(), R$color.miuix_default_color_on_surface_light);
            Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ActionBar_android_background);
            this.mBackground = drawable;
            this.mBackgroundArray = new Drawable[]{drawable, typedArrayObtainStyledAttributes.getDrawable(R$styleable.ActionBar_actionBarEmbededTabsBackground), typedArrayObtainStyledAttributes.getDrawable(R$styleable.ActionBar_actionBarStackedBackground)};
            this.mCustomViewAutoFitSystemWindow = typedArrayObtainStyledAttributes.getBoolean(R$styleable.ActionBar_customViewAutoFitSystemWindow, false);
            float dimension = typedArrayObtainStyledAttributes.getDimension(R$styleable.ActionBar_maskExtraHeight, context.getResources().getDimension(R$dimen.miuix_appcompat_action_bar_mask_extra_height));
            this.mMaskExtraHeight = dimension;
            this.mMaskExtraHeight = dimension / context.getResources().getDisplayMetrics().density;
            int color = typedArrayObtainStyledAttributes.getColor(R$styleable.ActionBar_maskColor, !AttributeResolver.resolveBoolean(getContext(), R.attr.isLightTheme, true) ? -16777216 : -789517);
            this.mMaskColor = color;
            this.mDefaultMaskColor = color;
            if (getId() == R$id.split_action_bar) {
                this.mIsSplit = true;
                this.mSplitBackground = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ActionBar_android_backgroundSplit);
            }
            typedArrayObtainStyledAttributes.recycle();
            if (!this.mIsSplit) {
                setPadding(0, 0, 0, 0);
            }
            resizeSplitMaxHeight();
            updateWillNotDraw();
            this.mDrawBackground = true;
            if (HyperMaterialUtils.isEnable() && !this.mIsSplit) {
                this.mMaterial = MaterialDayNightConfig.create(RomUtils.getHyperOsVersion() > 2 ? Mask.Pured_Regular : Blur.ExtraHeavy);
                this.mBlurHelper = new MiuiBlurUiHelper(context, this, false, false, new MiuiBlurUiHelper.BlurStateCallback() { // from class: miuix.appcompat.internal.app.widget.ActionBarContainer.2
                    final boolean isDarkThemeOverlay;

                    {
                        this.isDarkThemeOverlay = MiuixUIUtils.isDarkThemeOverlay(ActionBarContainer.this.getContext(), R$color.miuix_default_color_on_surface_light);
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public boolean isLightTheme() {
                        Integer colorFromDrawable;
                        if (ActionBarContainer.this.mBackground == null || (colorFromDrawable = MiuixUIUtils.getColorFromDrawable(ActionBarContainer.this.mBackground)) == null) {
                            return !this.isDarkThemeOverlay && AttributeResolver.resolveBoolean(ActionBarContainer.this.getContext(), R$attr.isLightTheme, true);
                        }
                        return MiuixUIUtils.isLightColor(colorFromDrawable.intValue()) && !this.isDarkThemeOverlay;
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public Drawable getBackground() {
                        return ActionBarContainer.this.mBackground;
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public int getBackgroundColor() {
                        if (this.isDarkThemeOverlay) {
                            return AttributeResolver.resolveColor(ActionBarContainer.this.getContext(), miuix.theme.R$attr.colorSurface, 0);
                        }
                        return 0;
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public MaterialConfig.BlurConfig getBlurConfig(boolean z) {
                        MaterialDayNightConfig materialDayNightConfig = ActionBarContainer.this.mMaterial;
                        if (materialDayNightConfig != null) {
                            return materialDayNightConfig.getBlurConfig(z);
                        }
                        return null;
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public void onBlurEnableStateChanged(boolean z) {
                        if (ActionBarContainer.this.mIsSplit) {
                            ActionBarContainer.this.mInternalApplySpiltBgBlur = z;
                            if (ActionBarContainer.this.mResidentActionMenuView != null) {
                                Boolean bool = ActionBarContainer.this.mUserApplySplitActionBarBgBlur;
                                ActionBarContainer actionBarContainer = ActionBarContainer.this;
                                boolean zBooleanValue = bool != null ? actionBarContainer.mUserApplySplitActionBarBgBlur.booleanValue() : actionBarContainer.mInternalApplySpiltBgBlur;
                                if (z) {
                                    ActionBarContainer.this.mResidentActionMenuView.setSupportBlur(true);
                                    ActionBarContainer.this.mResidentActionMenuView.setEnableBlur(true);
                                }
                                ActionBarContainer.this.mResidentActionMenuView.applyBlur(zBooleanValue);
                            }
                        }
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public void onBlurApplyStateChanged(boolean z) {
                        ActionBarContainer.this.mDrawBackground = !z;
                        if (ActionBarContainer.this.mActionBarView != null) {
                            ActionBarContainer.this.mActionBarView.setApplyBgBlur(z);
                        }
                        if (ActionBarContainer.this.mActionBarContextView != null) {
                            ActionBarContainer.this.mActionBarContextView.updateBackground(z);
                        }
                        ActionBarContainer.this.invalidate();
                    }

                    @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                    public BlurApplier getBlurApplier() {
                        BlurApplier blurApplier = ActionBarContainer.this.mUserBlurApplier;
                        ActionBarContainer actionBarContainer = ActionBarContainer.this;
                        return blurApplier != null ? actionBarContainer.mUserBlurApplier : actionBarContainer.mGradientBlurApplier;
                    }
                });
            } else {
                this.mBlurHelper = null;
            }
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = (ActionBarView) findViewById(R$id.action_bar);
        this.mActionBarContextView = (ActionBarContextView) findViewById(R$id.action_context_bar);
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.bindActionBarTransitionListeners(this.mActionBarTransitionListeners);
            this.mCurBarExpandState = this.mActionBarView.getExpandState();
            this.mCurBarResizable = this.mActionBarView.isResizable();
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            this.mCurContextBarExpandState = actionBarContextView.getExpandState();
            this.mCurContextBarResizable = this.mActionBarContextView.isResizable();
            this.mActionBarContextView.setActionBarView(this.mActionBarView);
        }
    }

    public void setMiuixFloatingOnInit(boolean z) {
        this.mIsMiuixFloating = z;
        propagateFloatingBgColor();
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null && z) {
            this.mCurBarResizable = actionBarView.isResizable();
            this.mActionBarView.setExpandState(0);
            this.mActionBarView.setResizable(false);
            this.mCurBarExpandState = this.mActionBarView.getExpandState();
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView == null || !z) {
            return;
        }
        this.mCurContextBarResizable = actionBarContextView.isResizable();
        this.mActionBarContextView.setExpandState(0);
        this.mActionBarContextView.setResizable(false);
        this.mCurContextBarExpandState = this.mActionBarContextView.getExpandState();
    }

    public void setIsMiuixFloating(boolean z) {
        this.mIsMiuixFloating = z;
        propagateFloatingBgColor();
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            if (z) {
                this.mCurBarExpandState = actionBarView.getExpandState();
                this.mCurBarResizable = this.mActionBarView.isResizable();
                this.mActionBarView.setExpandState(0);
                this.mActionBarView.setResizable(false);
            } else {
                actionBarView.setResizable(this.mCurBarResizable);
                this.mActionBarView.setExpandState(0);
            }
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            if (z) {
                this.mCurContextBarExpandState = actionBarContextView.getExpandState();
                this.mCurContextBarResizable = this.mActionBarContextView.isResizable();
                this.mActionBarContextView.setExpandState(0);
                this.mActionBarContextView.setResizable(false);
                return;
            }
            actionBarContextView.setResizable(this.mCurContextBarResizable);
            this.mActionBarContextView.setExpandState(0);
        }
    }

    public void setOverlayMode(boolean z) {
        this.mOverlayMode = z;
        updateParentClipChildren();
        updateWillNotDraw();
    }

    public void setTemporaryOverlayMode(boolean z) {
        this.mIsTemporaryOverlayMode = z;
    }

    private void updateParentClipChildren() {
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).setClipChildren(!this.mOverlayMode);
        }
    }

    private void updateWillNotDraw() {
        boolean z = false;
        if (this.mIsSplit) {
            setWillNotDraw(this.mSplitBackground == null);
            return;
        }
        if (this.mBackground == null && this.mStackedBackground == null && !this.mOverlayMode) {
            z = true;
        }
        setWillNotDraw(z);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mOverlayMode) {
            updateParentClipChildren();
        }
    }

    public void setCoordinatedOffsetYInSearchModeAnimation(int i) {
        this.mCoordinatedOffsetYInSearchModeAnimation = i;
        ActionBarCoordinateListener actionBarCoordinateListener = this.mCoordinateListener;
        if (actionBarCoordinateListener != null) {
            actionBarCoordinateListener.onActionBarResizing(this.mLastToState, this.mLastActionBarResizingProcess, this.mActionBarHeightGap + i, this.mActionBarHeightTotalGap);
        }
    }

    public void setActionBarContextView(ActionBarContextView actionBarContextView) {
        this.mActionBarContextView = actionBarContextView;
        if (actionBarContextView != null) {
            actionBarContextView.setActionBarView(this.mActionBarView);
            this.mCurContextBarExpandState = this.mActionBarContextView.getExpandState();
            this.mCurContextBarResizable = this.mActionBarContextView.isResizable();
        }
    }

    public void setPendingInsets(Rect rect) {
        if (this.mIsSplit) {
            return;
        }
        if (this.mPendingInsets == null) {
            this.mPendingInsets = new Rect();
        }
        if (Objects.equals(this.mPendingInsets, rect)) {
            return;
        }
        this.mPendingInsets.set(rect);
        applyInsetsTopByMargin(this.mActionBarView);
        applyInsetsTopByMargin(this.mActionBarContextView);
    }

    void setActionBarCoordinateListener(ActionBarCoordinateListener actionBarCoordinateListener) {
        this.mCoordinateListener = actionBarCoordinateListener;
    }

    ActionBarCoordinateListener getActionBarCoordinateListener() {
        return this.mCoordinateListener;
    }

    void addActionBarTransitionListener(ActionBarTransitionListener actionBarTransitionListener) {
        if (actionBarTransitionListener != null) {
            this.mActionBarTransitionListeners.add(actionBarTransitionListener);
        }
    }

    @Override // miuix.view.BlurableWidget
    public void setSupportBlur(boolean z) {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurHelper;
        if (miuiBlurUiHelper != null) {
            miuiBlurUiHelper.setSupportBlur(z);
        }
    }

    public boolean isSupportBlur() {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurHelper;
        if (miuiBlurUiHelper == null) {
            return false;
        }
        return miuiBlurUiHelper.isSupportBlur();
    }

    @Override // miuix.view.BlurableWidget
    public void setEnableBlur(boolean z) {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurHelper;
        if (miuiBlurUiHelper == null) {
            if (this.mIsSplit) {
                this.mSplitEnableBlur = z;
                ActionMenuView actionMenuView = this.mResidentActionMenuView;
                if (actionMenuView != null) {
                    actionMenuView.setSupportBlur(true);
                    this.mResidentActionMenuView.setEnableBlur(z);
                    this.mResidentActionMenuView.applyBlur(z);
                    return;
                }
                return;
            }
            return;
        }
        miuiBlurUiHelper.setEnableBlur(z);
    }

    public boolean isEnableBlur() {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurHelper;
        if (miuiBlurUiHelper == null) {
            return this.mIsSplit && this.mSplitEnableBlur && MiuiBlurUtils.isEffectEnable(getContext());
        }
        return miuiBlurUiHelper.isEnableBlur();
    }

    @Override // miuix.view.BlurableWidget
    public void applyBlur(boolean z) {
        if (this.mBlurHelper == null || this.mIsSplit) {
            return;
        }
        if (z && getVisibility() == 0) {
            this.mBlurHelper.applyBlur(true);
        } else {
            this.mBlurHelper.applyBlur(false);
        }
    }

    public boolean isApplyBlur() {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurHelper;
        if (miuiBlurUiHelper == null) {
            return false;
        }
        return miuiBlurUiHelper.isApplyBlur();
    }

    void setActionBarBlurByNestedScrolled(boolean z) {
        this.mInternalApplyBgBlur = z;
        if (this.mUserApplyBgBlur != null) {
            return;
        }
        applyBlur(z);
    }

    void resetActionBarBlurConfigOnReshow() {
        Boolean bool = this.mUserApplyBgBlur;
        if (bool != null) {
            applyBlur(bool.booleanValue());
        } else {
            applyBlur(this.mInternalApplyBgBlur);
        }
    }

    int getInsetHeight() {
        if (this.mIsSplit) {
            return Math.max(Math.max(0, getActionMenuViewInsetHeight(this.mActionModeMenuView)), getActionMenuViewInsetHeight(this.mResidentActionMenuView));
        }
        return 0;
    }

    private int getActionMenuViewInsetHeight(ActionMenuView actionMenuView) {
        if (actionMenuView == null || actionMenuView.getVisibility() != 0 || actionMenuView.getAlpha() == 0.0f || actionMenuView.getCollapsedHeight() <= 0) {
            return 0;
        }
        return Math.max(0, (int) (actionMenuView.getCollapsedHeight() - actionMenuView.getTranslationY()));
    }

    public Drawable getPrimaryBackground() {
        return this.mBackground;
    }

    public float getMaskExtraHeight() {
        return this.mMaskExtraHeight;
    }

    int resolveFloatingBgColor() {
        int i = this.mActionButtonFloatingBgColor;
        if (i != -1) {
            return i;
        }
        if (this.mIsMiuixFloating) {
            return AttributeResolver.resolveColor(getContext(), miuix.theme.R$attr.colorSurfacePopWindow, -1);
        }
        return resolveAutoFloatingBgColor();
    }

    private int resolveAutoFloatingBgColor() {
        int iResolveWindowBackgroundColor = resolveWindowBackgroundColor();
        Integer numResolveDrawableColor = resolveDrawableColor(this.mBackground);
        if (numResolveDrawableColor == null) {
            return iResolveWindowBackgroundColor;
        }
        if (iResolveWindowBackgroundColor == -1) {
            if (((numResolveDrawableColor.intValue() >>> 24) & 255) == 255) {
                return numResolveDrawableColor.intValue();
            }
            return -1;
        }
        return compositeSrcOver(numResolveDrawableColor.intValue(), iResolveWindowBackgroundColor);
    }

    private int resolveWindowBackgroundColor() {
        TypedValue typedValue = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(R.attr.windowBackground, typedValue, true)) {
            return -1;
        }
        int i = typedValue.type;
        if (i >= 28 && i <= 31) {
            int i2 = typedValue.data;
            if (((i2 >>> 24) & 255) == 255) {
                return i2;
            }
            return -1;
        }
        if (typedValue.resourceId != 0) {
            try {
                Integer numResolveDrawableColor = resolveDrawableColor(ContextCompat.getDrawable(getContext(), typedValue.resourceId));
                if (numResolveDrawableColor != null && ((numResolveDrawableColor.intValue() >>> 24) & 255) == 255) {
                    return numResolveDrawableColor.intValue();
                }
            } catch (Exception unused) {
            }
        }
        return -1;
    }

    private static Integer resolveDrawableColor(Drawable drawable) {
        if (drawable instanceof ColorDrawable) {
            return Integer.valueOf(((ColorDrawable) drawable).getColor());
        }
        return null;
    }

    private static int compositeSrcOver(int i, int i2) {
        int i3 = (i >>> 24) & 255;
        if (i3 == 0) {
            return i2 | (-16777216);
        }
        if (i3 == 255) {
            return i;
        }
        int i4 = 255 - i3;
        return ((((i & 255) * i3) + ((i2 & 255) * i4)) / 255) | ((((((i >> 16) & 255) * i3) + (((i2 >> 16) & 255) * i4)) / 255) << 16) | (-16777216) | ((((((i >> 8) & 255) * i3) + (((i2 >> 8) & 255) * i4)) / 255) << 8);
    }

    private void propagateFloatingBgColor() {
        int iResolveFloatingBgColor = resolveFloatingBgColor();
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.setFloatingBgColor(iResolveFloatingBgColor);
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            actionBarContextView.setFloatingBgColor(iResolveFloatingBgColor);
        }
    }

    void onActionBarExpandStateToExpand() {
        if (this.mShouldDrawOverlayMask) {
            this.mShouldDrawOverlayMask = false;
            animateMaskAlpha(false);
            Iterator it = this.mActionBarTransitionListeners.iterator();
            while (it.hasNext()) {
                ((ActionBarTransitionListener) it.next()).onOverlayMaskStateChanged(true);
            }
            if (this.mActionButtonFloatingState == -1) {
                ActionBarContextView actionBarContextView = this.mActionBarContextView;
                if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
                    this.mActionBarContextView.updateActionButtonFloatingState(false);
                    return;
                }
                ActionBarView actionBarView = this.mActionBarView;
                if (actionBarView != null) {
                    actionBarView.updateActionButtonFloatingState(false);
                }
            }
        }
    }

    public void updateBackground(boolean z) {
        this.mUserBgViewApplyBlur = Boolean.valueOf(z);
        updateBackgroundInternal(z);
    }

    void updateBackgroundInternal(boolean z) {
        if (z) {
            this.mDrawBackground = false;
        } else {
            this.mDrawBackground = true;
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            actionBarContextView.updateBackground(z);
        }
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.setApplyBgBlur(z);
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyVerticalGradientBlur(View view, final int i) {
        if (view != this) {
            Folme$$ExternalSyntheticBUOutline0.m("applyVerticalGradientBlur expects ActionBarContainer itself");
            return;
        }
        if (this.mPendingGradientLayoutListener != null) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this.mPendingGradientLayoutListener);
            this.mPendingGradientLayoutListener = null;
        }
        if (getHeight() > 0) {
            applyVerticalGradientBlurInternal(i);
        } else {
            this.mPendingGradientLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarContainer.3
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public void onGlobalLayout() {
                    ActionBarContainer.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if (ActionBarContainer.this.mPendingGradientLayoutListener == this) {
                        ActionBarContainer.this.mPendingGradientLayoutListener = null;
                    }
                    ActionBarContainer.this.applyVerticalGradientBlurInternal(i);
                }
            };
            getViewTreeObserver().addOnGlobalLayoutListener(this.mPendingGradientLayoutListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyVerticalGradientBlurInternal(int i) {
        float fRound = Math.round(getResources().getDisplayMetrics().density * 40.0f);
        float height = getHeight();
        MiuiBlurUtils.setBackgroundBlurMode(this, 1);
        MiuiBlurUtils.setViewBlurMode(this, i);
        if (MiuiBlurUtils.setBackgroundLinearGradientBlur(this, new float[]{0.0f, 0.0f, fRound, 0.0f, height, 0.0f})) {
            return;
        }
        Log.w("miuix-appcompat", "Gradient blur unavailable (isDefaultFeatureEnable=" + HyperMaterialUtils.isDefaultFeatureEnable() + "), fallback to no blur");
        MiuiBlurUtils.clearBackgroundBlur(this);
        this.mDrawBackground = true;
        invalidate();
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        boolean z = i == 0;
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setVisible(z, false);
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.setVisible(z, false);
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null) {
            drawable3.setVisible(z, false);
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        if (drawable == this.mBackground && !this.mIsSplit) {
            return true;
        }
        if (drawable == this.mStackedBackground && this.mIsStacked) {
            return true;
        }
        return (drawable == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(drawable);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mBackground;
        if (drawable != null && drawable.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null && drawable2.isStateful()) {
            this.mStackedBackground.setState(getDrawableState());
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 == null || !drawable3.isStateful()) {
            return;
        }
        this.mSplitBackground.setState(getDrawableState());
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return !this.mIsSplit && super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        return !this.mIsSplit;
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    public void setTabContainer(ScrollingTabContainerView scrollingTabContainerView) {
        View view = this.mTabContainer;
        if (view != null) {
            removeView(view);
        }
        View view2 = this.mTabContainer;
        if (view2 != null) {
            view2.setBackground(null);
        }
        this.mTabContainer = scrollingTabContainerView;
    }

    public int getStickyAreaHeight() {
        View view = this.mStickyView;
        if (view == null || view.getVisibility() == 8 || this.mStickyView.getMeasuredHeight() <= 0) {
            return 0;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mStickyView.getLayoutParams();
        return this.mStickyView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    private void applyStickyCollapseRatio(float f) {
        View view = this.mStickyView;
        if (view == null) {
            return;
        }
        if (!this.mStickyViewCollapsible) {
            view.setAlpha(1.0f);
            this.mStickyView.setTranslationY(0.0f);
        } else {
            float fMax = Math.max(0.0f, Math.min(1.0f, f));
            this.mStickyView.setAlpha(fMax);
            this.mStickyView.setTranslationY((-(1.0f - fMax)) * this.mStickyView.getMeasuredHeight());
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        if (this.mIsSplit) {
            drawSplitMode(canvas);
        } else {
            drawNormalMode(canvas);
        }
    }

    private void drawNormalMode(Canvas canvas) {
        drawMainBackground(canvas);
        drawOverlayMaskIfNeeded(canvas);
    }

    private void drawMainBackground(Canvas canvas) {
        Drawable drawable = this.mBackground;
        if (drawable == null || !this.mDrawBackground) {
            return;
        }
        drawable.draw(canvas);
    }

    private void drawOverlayMaskIfNeeded(Canvas canvas) {
        if (this.mContentFadeModeActive || !this.mOverlayMode || this.mMaskAlpha <= 0.0f || Color.alpha(this.mMaskColor) == 0 || this.mCustomBackground || hasNonTransparentBackground()) {
            return;
        }
        drawOverlayMask(canvas);
    }

    private boolean hasNonTransparentBackground() {
        Integer colorFromDrawable;
        Drawable drawable = this.mBackground;
        return (drawable == null || (colorFromDrawable = MiuixUIUtils.getColorFromDrawable(drawable)) == null || Color.alpha(colorFromDrawable.intValue()) == 0) ? false : true;
    }

    private void drawOverlayMask(Canvas canvas) {
        int height = (int) (getHeight() + MiuixUIUtils.dp2px(getContext(), this.mMaskExtraHeight));
        int width = getWidth();
        if (this.mLastGradientHeight != height || this.mLastGradientWidth != width || this.mGradientNeedsUpdate) {
            this.mLastGradientHeight = height;
            this.mLastGradientWidth = width;
            this.mGradientNeedsUpdate = false;
            int iAlpha = Color.alpha(this.mMaskColor);
            int i = this.mMaskColor & 16777215;
            float[] fArr = {1.0f, 0.98f, 0.8f, 0.5f, 0.0f};
            float[] fArr2 = {0.0f, 0.52f, 0.74f, 0.85f, 1.0f};
            int[] iArr = new int[fArr.length];
            for (int i2 = 0; i2 < fArr.length; i2++) {
                iArr[i2] = (Math.round(iAlpha * fArr[i2]) << 24) | i;
            }
            float f = height;
            LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, f, iArr, fArr2, Shader.TileMode.CLAMP);
            this.mGradientShader = linearGradient;
            this.mGradientPaint.setShader(linearGradient);
            this.mGradientPath.reset();
            this.mGradientPath.addRect(0.0f, 0.0f, width, f, Path.Direction.CW);
        }
        this.mGradientPaint.setAlpha(Math.round(this.mMaskAlpha * 255.0f));
        canvas.drawPath(this.mGradientPath, this.mGradientPaint);
    }

    private void animateMaskAlpha(boolean z) {
        ValueAnimator valueAnimator = this.mMaskAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mMaskAnimator = null;
        }
        if (z) {
            this.mMaskAlpha = 1.0f;
            invalidate();
            return;
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.mMaskAlpha, 0.0f);
        this.mMaskAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(100L);
        this.mMaskAnimator.setInterpolator(new LinearInterpolator());
        this.mMaskAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarContainer$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ActionBarContainer.m5451$r8$lambda$5W3671uGuUMWatWoZ3SJLwcris(this.f$0, valueAnimator2);
            }
        });
        this.mMaskAnimator.start();
    }

    /* JADX INFO: renamed from: $r8$lambda$5W3671uGuUMWat-WoZ3SJLwcris, reason: not valid java name */
    public static /* synthetic */ void m5451$r8$lambda$5W3671uGuUMWatWoZ3SJLwcris(ActionBarContainer actionBarContainer, ValueAnimator valueAnimator) {
        actionBarContainer.getClass();
        actionBarContainer.mMaskAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        actionBarContainer.invalidate();
    }

    private void applyInsetsTopByMargin(View view) {
        if (view == null || view.getVisibility() != 0) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        Rect rect = this.mPendingInsets;
        marginLayoutParams.topMargin = rect != null ? rect.top : 0;
        view.setLayoutParams(marginLayoutParams);
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int measuredHeight;
        int measuredHeight2;
        int measuredHeight3;
        Rect rect;
        if (this.mIsSplit) {
            onMeasureSplit(i, i2);
            return;
        }
        View view = this.mTabContainer;
        if (view != null) {
            view.setPadding(view.getPaddingLeft(), this.mTabContainerPaddingTop, this.mTabContainer.getPaddingRight(), this.mTabContainer.getPaddingBottom());
        }
        applyInsetsTopByMargin(this.mActionBarView);
        applyInsetsTopByMargin(this.mActionBarContextView);
        super.onMeasure(i, i2);
        ActionBarView actionBarView = this.mActionBarView;
        boolean z = (actionBarView == null || actionBarView.getVisibility() == 8 || this.mActionBarView.getMeasuredHeight() <= 0) ? false : true;
        if (z) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mActionBarView.getLayoutParams();
            measuredHeight = this.mActionBarView.isCollapsed() ? layoutParams.topMargin : layoutParams.bottomMargin + this.mActionBarView.getMeasuredHeight() + layoutParams.topMargin;
        } else {
            measuredHeight = 0;
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if ((actionBarContextView == null || actionBarContextView.getVisibility() == 8 || this.mActionBarContextView.getMeasuredHeight() <= 0) ? false : true) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mActionBarContextView.getLayoutParams();
            measuredHeight2 = this.mActionBarContextView.getMeasuredHeight() + layoutParams2.topMargin + layoutParams2.bottomMargin;
        } else {
            measuredHeight2 = 0;
        }
        if (measuredHeight > 0 || measuredHeight2 > 0) {
            setMeasuredDimension(getMeasuredWidth(), Math.max(measuredHeight, measuredHeight2));
        }
        View view2 = this.mStickyView;
        if (view2 == null || view2.getVisibility() == 8 || this.mStickyView.getMeasuredHeight() <= 0) {
            measuredHeight3 = 0;
        } else {
            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.mStickyView.getLayoutParams();
            measuredHeight3 = this.mStickyView.getMeasuredHeight() + layoutParams3.topMargin + layoutParams3.bottomMargin;
        }
        View view3 = this.mTabContainer;
        if (view3 == null || view3.getVisibility() == 8) {
            if (measuredHeight3 > 0 && (measuredHeight > 0 || measuredHeight2 > 0)) {
                setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + measuredHeight3);
            }
        } else if (View.MeasureSpec.getMode(i2) == Integer.MIN_VALUE) {
            setMeasuredDimension(getMeasuredWidth(), Math.min(measuredHeight + this.mTabContainer.getMeasuredHeight() + measuredHeight3, View.MeasureSpec.getSize(i2)) + ((z || (rect = this.mPendingInsets) == null) ? 0 : rect.top));
        }
        int i3 = 0;
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() == 0 && childAt.getMeasuredHeight() > 0 && childAt.getMeasuredWidth() > 0) {
                i3++;
            }
        }
        if (i3 == 0) {
            setMeasuredDimension(0, 0);
        }
    }

    private void onMeasureSplit(int i, int i2) {
        if (View.MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            i = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824);
        }
        int i3 = this.mHeightMaxMeasureSpec;
        if (i3 != -1) {
            i2 = i3;
        }
        super.onMeasure(i, i2);
        int childCount = getChildCount();
        int iMax = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            iMax = Math.max(iMax, getChildAt(i4).getMeasuredHeight());
        }
        if (iMax == 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        ActionMenuView actionMenuView = this.mResidentActionMenuView;
        if (actionMenuView == null || !actionMenuView.hasOnlyCustomView()) {
            return;
        }
        ActionMenuView actionMenuView2 = this.mResidentActionMenuView;
        if (!(actionMenuView2 instanceof ResponsiveActionMenuView) || ((ResponsiveActionMenuView) actionMenuView2).isSuspend()) {
            return;
        }
        setMeasuredDimension(getMeasuredWidth(), iMax);
    }

    /* JADX WARN: Code duplicated, block: B:40:0x00e6  */
    /* JADX WARN: Code duplicated, block: B:57:0x013d  */
    /* JADX WARN: Code duplicated, block: B:58:0x013f  */
    /* JADX WARN: Code duplicated, block: B:61:0x0154  */
    /* JADX WARN: Code duplicated, block: B:64:0x015c  */
    /* JADX WARN: Code duplicated, block: B:65:0x015e  */
    /* JADX WARN: Code duplicated, block: B:73:0x017a  */
    /* JADX WARN: Code duplicated, block: B:77:0x018a  */
    /* JADX WARN: Code duplicated, block: B:82:0x01a8  */
    /* JADX WARN: Code duplicated, block: B:90:0x019c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:92:0x0184 A[SYNTHETIC] */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        boolean z2;
        int i6;
        int expandedHeight;
        int collapsedHeight;
        int iMax;
        float f;
        float fMin;
        float f2;
        int i7;
        boolean zUpdateExpandStateOnLayout;
        ActionMenuView actionMenuView;
        ActionBarCoordinateListener actionBarCoordinateListener;
        super.onLayout(z, i, i2, i3, i4);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = (int) (getMeasuredWidth() / getContext().getResources().getDisplayMetrics().density);
        View view = this.mStickyView;
        if (view == null || view.getVisibility() == 8 || this.mStickyView.getMeasuredHeight() <= 0) {
            i5 = 0;
        } else {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mStickyView.getLayoutParams();
            int measuredHeight2 = this.mStickyView.getMeasuredHeight();
            int i8 = layoutParams.topMargin + measuredHeight2;
            int i9 = layoutParams.bottomMargin;
            i5 = i8 + i9;
            int i10 = measuredHeight - i9;
            this.mStickyView.layout(layoutParams.leftMargin + i, i10 - measuredHeight2, i3 - layoutParams.rightMargin, i10);
        }
        View view2 = this.mTabContainer;
        if (view2 != null && view2.getVisibility() != 8) {
            int measuredHeight3 = this.mTabContainer.getMeasuredHeight();
            ActionBarView actionBarView = this.mActionBarView;
            if (actionBarView != null && actionBarView.getVisibility() == 0 && this.mActionBarView.getMeasuredHeight() > 0) {
                View view3 = this.mTabContainer;
                view3.setPadding(view3.getPaddingLeft(), this.mTabContainerPaddingTop, this.mTabContainer.getPaddingRight(), this.mTabContainer.getPaddingBottom());
            } else {
                Rect rect = this.mPendingInsets;
                measuredHeight3 += rect != null ? rect.top : 0;
                View view4 = this.mTabContainer;
                int paddingLeft = view4.getPaddingLeft();
                Rect rect2 = this.mPendingInsets;
                view4.setPadding(paddingLeft, rect2 != null ? rect2.top + this.mTabContainerPaddingTop : this.mTabContainerPaddingTop, this.mTabContainer.getPaddingRight(), this.mTabContainer.getPaddingBottom());
            }
            int i11 = measuredHeight - i5;
            this.mTabContainer.layout(i, i11 - measuredHeight3, i3, i11);
        }
        if (this.mIsSplit) {
            Drawable drawable = this.mSplitBackground;
            if (drawable != null) {
                drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                z2 = true;
            } else {
                z2 = false;
            }
        } else {
            selectDrawable();
            Drawable drawable2 = this.mBackground;
            if (drawable2 != null) {
                drawable2.setBounds(0, 0, i3 - i, measuredHeight);
                z2 = true;
            } else {
                z2 = false;
            }
        }
        this.mIsInWideMode = measuredWidth > 600;
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && this.mIsInActionMode) {
            this.mActionBarHeightGap = actionBarContextView.getMeasuredHeight() - this.mActionBarContextView.getCollapsedHeight();
            expandedHeight = this.mActionBarContextView.getExpandedHeight();
            collapsedHeight = this.mActionBarContextView.getCollapsedHeight();
        } else {
            ActionBarView actionBarView2 = this.mActionBarView;
            if (actionBarView2 != null) {
                this.mActionBarHeightGap = actionBarView2.getMeasuredHeight() - this.mActionBarView.getCollapsedHeight();
                expandedHeight = this.mActionBarView.getExpandedHeight();
                collapsedHeight = this.mActionBarView.getCollapsedHeight();
            } else {
                i6 = 0;
            }
            iMax = Math.max(0, this.mActionBarHeightGap);
            this.mActionBarHeightGap = iMax;
            if (i6 == 0) {
                f = 1.0f;
            } else {
                f = ((i6 - iMax) * 1.0f) / i6;
            }
            this.mActionBarHeightTotalGap = i6;
            fMin = Math.min(1.0f, f);
            if (getTranslationY() < 0.0f) {
                fMin = 0.0f;
            }
            f2 = this.mLastActionBarResizingProcess - fMin;
            if (f2 > 0.0f) {
                i7 = 2;
            } else {
                i7 = 0;
            }
            this.mLastToState = i7;
            if (this.mOverlayMode && (actionBarCoordinateListener = this.mCoordinateListener) != null) {
                actionBarCoordinateListener.onActionBarResizing(i7, fMin, this.mActionBarHeightGap + this.mCoordinatedOffsetYInSearchModeAnimation, this.mActionBarHeightTotalGap);
            }
            applyStickyCollapseRatio(fMin);
            ActionBarView actionBarView3 = this.mActionBarView;
            zUpdateExpandStateOnLayout = actionBarView3 != null ? actionBarView3.updateExpandStateOnLayout() : false;
            for (ActionBarTransitionListener actionBarTransitionListener : this.mActionBarTransitionListeners) {
                actionBarTransitionListener.onActionBarMove(f2, fMin);
                actionBarTransitionListener.onActionBarResizing(this.mLastToState, fMin, this.mActionBarHeightGap);
                if (zUpdateExpandStateOnLayout) {
                    actionBarTransitionListener.onExpandStateChanged(this.mActionBarView.mExpandStateOnLayout);
                }
            }
            this.mLastActionBarResizingProcess = fMin;
            if (z2) {
                invalidate();
            }
            if (this.mIsSplit || (actionMenuView = this.mResidentActionMenuView) == null) {
            }
            actionMenuView.applyBlur(isEnableBlur());
            return;
        }
        i6 = expandedHeight - collapsedHeight;
        iMax = Math.max(0, this.mActionBarHeightGap);
        this.mActionBarHeightGap = iMax;
        if (i6 == 0) {
            f = 1.0f;
        } else {
            f = ((i6 - iMax) * 1.0f) / i6;
        }
        this.mActionBarHeightTotalGap = i6;
        fMin = Math.min(1.0f, f);
        if (getTranslationY() < 0.0f) {
            fMin = 0.0f;
        }
        f2 = this.mLastActionBarResizingProcess - fMin;
        if (f2 > 0.0f) {
            i7 = 2;
        } else {
            i7 = 0;
        }
        this.mLastToState = i7;
        if (this.mOverlayMode) {
            actionBarCoordinateListener.onActionBarResizing(i7, fMin, this.mActionBarHeightGap + this.mCoordinatedOffsetYInSearchModeAnimation, this.mActionBarHeightTotalGap);
        }
        applyStickyCollapseRatio(fMin);
        ActionBarView actionBarView4 = this.mActionBarView;
        if (actionBarView4 != null) {
        }
        while (r11.hasNext()) {
            actionBarTransitionListener.onActionBarMove(f2, fMin);
            actionBarTransitionListener.onActionBarResizing(this.mLastToState, fMin, this.mActionBarHeightGap);
            if (zUpdateExpandStateOnLayout) {
                actionBarTransitionListener.onExpandStateChanged(this.mActionBarView.mExpandStateOnLayout);
            }
        }
        this.mLastActionBarResizingProcess = fMin;
        if (z2) {
            invalidate();
        }
        if (this.mIsSplit) {
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ValueAnimator valueAnimator = this.mMaskAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mMaskAnimator = null;
        }
        setActionBarCoordinateListener(null);
        this.mActionBarTransitionListeners.clear();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        ActionMenuView actionMenuView;
        super.onConfigurationChanged(configuration);
        resizeSplitMaxHeight();
        this.mGradientNeedsUpdate = true;
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurHelper;
        if (miuiBlurUiHelper != null) {
            miuiBlurUiHelper.onConfigChanged();
            if (!this.mBlurHelper.isEnableBlur() && this.mUserBgViewApplyBlur == null) {
                updateBackgroundInternal(false);
            }
        }
        if (this.mIsSplit && (actionMenuView = this.mResidentActionMenuView) != null) {
            actionMenuView.applyBlur(isEnableBlur());
        }
        propagateFloatingBgColor();
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.refreshFloatingState();
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            actionBarContextView.refreshFloatingState();
        }
    }

    private void resizeSplitMaxHeight() {
        TypedValue typedValueResolveTypedValue;
        if (this.mIsSplit && (typedValueResolveTypedValue = AttributeResolver.resolveTypedValue(getContext(), R$attr.actionBarSplitMaxPercentageHeight)) != null && typedValueResolveTypedValue.type == 6) {
            float windowHeight = WindowUtils.getWindowHeight(getContext());
            this.mHeightMaxMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) typedValueResolveTypedValue.getFraction(windowHeight, windowHeight), Integer.MIN_VALUE);
        }
    }

    @Override // android.view.View
    public void setAlpha(float f) {
        super.setAlpha(f);
    }

    public void startActionMode() {
        this.mIsInActionMode = true;
    }

    public void finishActionMode() {
        ActionBarView actionBarView;
        this.mIsInActionMode = false;
        if (this.mOverlayMode && !this.mIsTemporaryOverlayMode && this.mShouldDrawOverlayMask && this.mActionButtonFloatingState == -1 && (actionBarView = this.mActionBarView) != null) {
            actionBarView.updateActionButtonFloatingState(true);
        }
    }

    public void recheckOverlayMaskState(boolean z) {
        if (!this.mOverlayMode || this.mIsTemporaryOverlayMode || z == this.mShouldDrawOverlayMask) {
            return;
        }
        this.mShouldDrawOverlayMask = z;
        ValueAnimator valueAnimator = this.mMaskAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mMaskAlpha = z ? 1.0f : 0.0f;
        invalidate();
        dispatchOverlayMaskAndFloatingState(z);
    }

    private void dispatchOverlayMaskAndFloatingState(boolean z) {
        ActionBarView actionBarView;
        Iterator it = this.mActionBarTransitionListeners.iterator();
        while (it.hasNext()) {
            ((ActionBarTransitionListener) it.next()).onOverlayMaskStateChanged(!z);
        }
        if (this.mActionButtonFloatingState != -1 || (actionBarView = this.mActionBarView) == null) {
            return;
        }
        actionBarView.updateActionButtonFloatingState(z);
    }

    public void show(boolean z) {
        if (this.mNowShowing) {
            return;
        }
        this.mNowShowing = true;
        Folme.useAt(this).state().cancel();
        setVisibility(0);
        if (z) {
            if (this.mIsSplit) {
                setTranslationY(TypedValue.applyDimension(1, 15.0f, getContext().getResources().getDisplayMetrics()));
                setAlpha(0.0f);
                if (this.mShowAnimConfig == null) {
                    this.mShowAnimConfig = new AnimConfig().setEase(-2, 0.82f, 0.3f).setSpecial(ViewProperty.ALPHA, FolmeEase.linear(150L), new float[0]).setSpecial(AbsActionBarView.SELF_BLUR, FolmeEase.linear(150L), new float[0]);
                    this.mShowAnimConfig.addListeners(new TransitionListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarContainer.5
                        @Override // miuix.animation.listener.TransitionListener
                        public void onComplete(Object obj) {
                            super.onComplete(obj);
                        }
                    });
                }
                if (!DeviceHelper.isFeatureWholeAnim()) {
                    this.mShowAnimConfig.setEase(-2, 0.82f, 0.3f).setSpecial(ViewProperty.ALPHA, FolmeEase.linear(0L), new float[0]).setSpecial(AbsActionBarView.SELF_BLUR, FolmeEase.linear(0L), new float[0]);
                }
                Folme.useAt(this).state().to(new AnimState("show_end").add(ViewProperty.TRANSLATION_Y, 0.0d).add(ViewProperty.ALPHA, 1.0d).add(AbsActionBarView.SELF_BLUR, 0.0d), this.mShowAnimConfig);
                ActionMenuView actionMenuView = this.mResidentActionMenuView;
                if (actionMenuView != null) {
                    actionMenuView.startLayoutAnimation();
                    return;
                }
                return;
            }
            return;
        }
        setTranslationY(0.0f);
        setAlpha(1.0f);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mRequestAnimation) {
            post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarContainer.6
                @Override // java.lang.Runnable
                public void run() {
                    ActionBarContainer.this.show(true);
                }
            });
            this.mRequestAnimation = false;
        }
    }

    private void selectDrawable() {
        ActionBarView actionBarView;
        Drawable[] drawableArr;
        char c;
        if (this.mCustomBackground || this.mIsSplit || (actionBarView = this.mActionBarView) == null || this.mBackground == null || (drawableArr = this.mBackgroundArray) == null || drawableArr.length < 3) {
            return;
        }
        if (actionBarView.isTightTitleWithEmbeddedTabs()) {
            int displayOptions = this.mActionBarView.getDisplayOptions();
            c = ((displayOptions & 2) == 0 && (displayOptions & 4) == 0 && (displayOptions & 16) == 0) ? (char) 1 : (char) 2;
        } else {
            c = 0;
        }
        Drawable drawable = this.mBackgroundArray[c];
        if (drawable != null) {
            this.mBackground = drawable;
        }
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr, int[] iArr2) {
        int i6 = iArr[1];
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onNestedScroll(view, i, i2, i3, i4, i5, iArr, iArr2);
        } else if (!this.mIsSplit && getVisibility() != 8) {
            this.mActionBarView.onNestedScroll(view, i, i2, i3, i4, i5, iArr, iArr2);
        }
        int i7 = iArr[1] - i6;
        if (!this.mOverlayMode || this.mIsTemporaryOverlayMode || i4 >= 0 || i7 > 0) {
            return;
        }
        setActionBarBlurByNestedScrolled(false);
        if (this.mShouldDrawOverlayMask) {
            this.mShouldDrawOverlayMask = false;
            animateMaskAlpha(false);
            Iterator it = this.mActionBarTransitionListeners.iterator();
            while (it.hasNext()) {
                ((ActionBarTransitionListener) it.next()).onOverlayMaskStateChanged(true);
            }
            if (this.mActionButtonFloatingState == -1) {
                ActionBarContextView actionBarContextView2 = this.mActionBarContextView;
                if (actionBarContextView2 != null && actionBarContextView2.getVisibility() == 0) {
                    this.mActionBarContextView.updateActionButtonFloatingState(false);
                } else {
                    ActionBarView actionBarView = this.mActionBarView;
                    if (actionBarView != null) {
                        actionBarView.updateActionButtonFloatingState(false);
                    }
                }
            }
        }
        if (this.mIsSplit || getVisibility() != 8) {
            return;
        }
        this.mActionBarView.setExpandState(2);
        ActionBarCoordinateListener actionBarCoordinateListener = this.mCoordinateListener;
        if (actionBarCoordinateListener != null) {
            int i8 = this.mActionBarHeightTotalGap;
            actionBarCoordinateListener.onActionBarResizing(2, 0.0f, i8, i8);
        }
    }

    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            return this.mActionBarContextView.onStartNestedScroll(view, view2, i, i2);
        }
        return this.mActionBarView.onStartNestedScroll(view, view2, i, i2);
    }

    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onNestedScrollAccepted(view, view2, i, i2);
        } else {
            if (this.mIsSplit || getVisibility() == 8) {
                return;
            }
            this.mActionBarView.onNestedScrollAccepted(view, view2, i, i2);
        }
    }

    public void onStopNestedScroll(View view, int i) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onStopNestedScroll(view, i);
        } else {
            if (this.mIsSplit || getVisibility() == 8) {
                return;
            }
            this.mActionBarView.onStopNestedScroll(view, i);
        }
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3, int[] iArr2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onNestedPreScroll(view, i, i2, iArr, i3, iArr2);
        } else if (!this.mIsSplit && getVisibility() != 8) {
            this.mActionBarView.onNestedPreScroll(view, i, i2, iArr, i3, iArr2);
        }
        if (!this.mOverlayMode || this.mIsTemporaryOverlayMode || i2 <= 0 || i2 - iArr[1] <= 0) {
            return;
        }
        if (!this.mShouldDrawOverlayMask) {
            this.mShouldDrawOverlayMask = true;
            animateMaskAlpha(true);
            Iterator it = this.mActionBarTransitionListeners.iterator();
            while (it.hasNext()) {
                ((ActionBarTransitionListener) it.next()).onOverlayMaskStateChanged(false);
            }
            if (this.mActionButtonFloatingState == -1) {
                ActionBarContextView actionBarContextView2 = this.mActionBarContextView;
                if (actionBarContextView2 != null && actionBarContextView2.getVisibility() == 0) {
                    this.mActionBarContextView.updateActionButtonFloatingState(true);
                } else {
                    ActionBarView actionBarView = this.mActionBarView;
                    if (actionBarView != null) {
                        actionBarView.updateActionButtonFloatingState(true);
                    }
                }
            }
        }
        if (!this.mIsSplit && getVisibility() == 8) {
            setActionBarBlurByNestedScrolled(true);
            this.mActionBarView.setExpandState(0);
            ActionBarCoordinateListener actionBarCoordinateListener = this.mCoordinateListener;
            if (actionBarCoordinateListener != null) {
                actionBarCoordinateListener.onActionBarResizing(0, 1.0f, 0, this.mActionBarHeightTotalGap);
            }
        }
        if (isLayoutRequested()) {
            return;
        }
        setActionBarBlurByNestedScrolled(true);
    }

    void onResidentActionMenuViewAdded(ActionMenuView actionMenuView) {
        this.mResidentActionMenuView = actionMenuView;
        if (actionMenuView != null && this.mIsSplit) {
            boolean zIsEnableBlur = isEnableBlur();
            actionMenuView.setSupportBlur(true);
            actionMenuView.setEnableBlur(zIsEnableBlur);
            actionMenuView.applyBlur(zIsEnableBlur);
            return;
        }
        if (actionMenuView == null || !isSupportBlur()) {
            return;
        }
        actionMenuView.setSupportBlur(isSupportBlur());
        actionMenuView.setEnableBlur(isEnableBlur());
        Boolean bool = this.mUserApplySplitActionBarBgBlur;
        actionMenuView.applyBlur((bool != null ? bool.booleanValue() : isEnableBlur()) && getMeasuredWidth() > 0 && getMeasuredHeight() > 0);
    }

    void onResidentActionMenuViewRemoved(ActionMenuView actionMenuView) {
        if (this.mResidentActionMenuView == actionMenuView) {
            this.mResidentActionMenuView = null;
        }
    }

    void onActionModeMenuViewAdded(ActionMenuView actionMenuView) {
        this.mActionModeMenuView = actionMenuView;
        if (actionMenuView != null && this.mIsSplit) {
            boolean zIsEnableBlur = isEnableBlur();
            actionMenuView.setSupportBlur(true);
            actionMenuView.setEnableBlur(zIsEnableBlur);
            if (actionMenuView.getMeasuredWidth() <= 0 || actionMenuView.getMeasuredHeight() <= 0) {
                return;
            }
            actionMenuView.applyBlur(zIsEnableBlur);
            return;
        }
        if (actionMenuView == null || !isSupportBlur()) {
            return;
        }
        Boolean bool = this.mUserApplySplitActionBarBgBlur;
        actionMenuView.applyBlur((bool != null ? bool.booleanValue() : isEnableBlur()) && actionMenuView.getMeasuredWidth() > 0 && actionMenuView.getMeasuredHeight() > 0);
    }

    void onActionModeMenuViewRemoved(ActionMenuView actionMenuView) {
        if (this.mActionModeMenuView == actionMenuView) {
            this.mActionModeMenuView = null;
        }
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        int i;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        Boolean bool = this.mUserApplyBgBlur;
        int i2 = 0;
        if (bool == null) {
            i = -1;
        } else {
            i = bool.booleanValue() ? 1 : 0;
        }
        savedState.userActionBarApplyBlur = i;
        Boolean bool2 = this.mUserApplySplitActionBarBgBlur;
        if (bool2 == null) {
            i2 = -1;
        } else if (bool2.booleanValue()) {
            i2 = 1;
        }
        savedState.userSplitActionBarApplyBlur = i2;
        savedState.actionBarSupportBlur = isSupportBlur();
        savedState.actionBarEnableBlur = isEnableBlur();
        savedState.actionBarApplyBlur = isApplyBlur();
        return savedState;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        int i = savedState.userActionBarApplyBlur;
        if (i == -1) {
            this.mUserApplyBgBlur = null;
        } else if (i == 0) {
            this.mUserApplyBgBlur = Boolean.FALSE;
        } else if (i == 1) {
            this.mUserApplyBgBlur = Boolean.TRUE;
        }
        int i2 = savedState.userSplitActionBarApplyBlur;
        if (i2 == -1) {
            this.mUserApplySplitActionBarBgBlur = null;
        } else if (i2 == 0) {
            this.mUserApplySplitActionBarBgBlur = Boolean.FALSE;
        } else if (i2 == 1) {
            this.mUserApplySplitActionBarBgBlur = Boolean.TRUE;
        }
        if (savedState.actionBarSupportBlur) {
            setSupportBlur(true);
        }
        if (savedState.actionBarEnableBlur) {
            setEnableBlur(true);
        }
        if (savedState.actionBarApplyBlur && isEnableBlur()) {
            applyBlur(true);
        }
    }

    class SavedState extends View.BaseSavedState {
        public static final Parcelable.ClassLoaderCreator CREATOR = new Parcelable.ClassLoaderCreator() { // from class: miuix.appcompat.internal.app.widget.ActionBarContainer.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }
        };
        boolean actionBarApplyBlur;
        boolean actionBarEnableBlur;
        boolean actionBarSupportBlur;
        int userActionBarApplyBlur;
        int userSplitActionBarApplyBlur;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        SavedState(Parcel parcel) {
            super(parcel);
            this.userActionBarApplyBlur = parcel.readInt();
            this.userSplitActionBarApplyBlur = parcel.readInt();
            this.actionBarSupportBlur = parcel.readInt() != 0;
            this.actionBarEnableBlur = parcel.readInt() != 0;
            this.actionBarApplyBlur = parcel.readInt() != 0;
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.userActionBarApplyBlur = parcel.readInt();
            this.userSplitActionBarApplyBlur = parcel.readInt();
            this.actionBarSupportBlur = parcel.readInt() != 0;
            this.actionBarEnableBlur = parcel.readInt() != 0;
            this.actionBarApplyBlur = parcel.readInt() != 0;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.userActionBarApplyBlur);
            parcel.writeInt(this.userSplitActionBarApplyBlur);
            parcel.writeInt(this.actionBarSupportBlur ? 1 : 0);
            parcel.writeInt(this.actionBarEnableBlur ? 1 : 0);
            parcel.writeInt(this.actionBarApplyBlur ? 1 : 0);
        }
    }
}
