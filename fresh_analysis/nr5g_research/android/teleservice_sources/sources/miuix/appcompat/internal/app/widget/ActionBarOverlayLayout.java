package miuix.appcompat.internal.app.widget;

import android.R;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.DisplayCutout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.DisplayCutoutCompat;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import miuix.animation.utils.CommonUtils;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$drawable;
import miuix.appcompat.R$id;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.ActionBarTransitionListener;
import miuix.appcompat.app.GroupButtonsPanel;
import miuix.appcompat.app.IContentInsetState;
import miuix.appcompat.app.floatingactivity.FloatingABOLayoutSpec;
import miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper;
import miuix.appcompat.internal.view.SearchActionModeImpl;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuDialogHelper;
import miuix.appcompat.internal.view.menu.MenuPresenter;
import miuix.appcompat.internal.view.menu.action.ActionMenuView;
import miuix.appcompat.internal.view.menu.context.ContextMenuBuilder;
import miuix.appcompat.internal.view.menu.context.ContextMenuPopupWindowHelper;
import miuix.autodensity.AutoDensityConfig;
import miuix.autodensity.DebugUtil;
import miuix.autodensity.DensityConfigManager;
import miuix.container.ExtraPaddingObserver;
import miuix.container.ExtraPaddingPolicy;
import miuix.core.util.EnvStateManager;
import miuix.core.util.HyperBloomStrokeUtils;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MaterialDayNightConfig;
import miuix.core.util.MiuixUIUtils;
import miuix.core.util.RomUtils;
import miuix.core.util.WindowBaseInfo;
import miuix.graphics.shadow.DropShadowConfig;
import miuix.graphics.shadow.DropShadowHelper;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.LiteUtils;
import miuix.os.DeviceHelper;
import miuix.smooth.SmoothCornerHelper;
import miuix.theme.token.BloomStrokeToken;
import miuix.theme.token.ColorBlendToken;
import miuix.theme.token.MaterialDayNightToken;
import miuix.theme.token.MaterialToken;
import miuix.theme.token.ShadowToken;
import miuix.theme.token.hypermaterial.Blur;
import miuix.view.BlurableWidget;
import miuix.view.Fence;
import miuix.view.MiuiBlurUiHelper;
import miuix.view.SearchActionMode;
import miuix.view.WindowInsetsController$InsetsConfig;

/* JADX INFO: loaded from: classes2.dex */
public class ActionBarOverlayLayout extends FrameLayout implements NestedScrollingParent3, Fence, BlurableWidget {
    private static final MaterialDayNightToken MATERIAL_FLOATING_PANEL;
    private static final MaterialToken MATERIAL_FLOATING_PANEL_DARK;
    private static final MaterialToken MATERIAL_FLOATING_PANEL_LIGHT;
    private static final boolean NAVIGATION_MATERIAL_ENABLED;
    private ActionBar mActionBar;
    private ActionBarContainer mActionBarBottom;
    private ActionBarContextView mActionBarContextView;
    protected ActionBarContainer mActionBarTop;
    protected ActionBarView mActionBarView;
    private ActionMode mActionMode;
    private Rect mAnimateContentMarginBottomInsets;
    private boolean mAnimating;
    private Rect mBaseContentInsets;
    private Rect mBaseInnerInsets;
    private MiuiBlurUiHelper mBlurUiHelper;
    private int mBottomExtraInset;
    private int mBottomMenuExtraInset;
    private int mBottomMenuMode;
    private int mBottomMenuModeConfig;
    private final int[] mBottomMenuVisibleHeight;
    private Window.Callback mCallback;
    private float mContentAlpha;
    private boolean mContentAutoFitSystemWindow;
    private ValueAnimator mContentFadeAnimator;
    private boolean mContentFadeEnabled;
    private float mContentFadeMaskAlpha;
    private Paint mContentFadePaint;
    private LinearGradient mContentFadeShader;
    private boolean mContentFadeShouldDraw;
    private Drawable mContentHeaderBackground;
    private IContentInsetState mContentInsetStateCallback;
    private Rect mContentInsets;
    private View mContentMask;
    private Rect mContentMaskInsets;
    protected View mContentView;
    private ContextMenuBuilder mContextMenu;
    private ContextMenuCallback mContextMenuCallback;
    private MenuDialogHelper mContextMenuHelper;
    private ContextMenuPopupWindowHelper mContextMenuPopupWindowHelper;
    protected final HashSet mCoordinatedScrollViewSet;
    private boolean mCorrectNestedScrollMotionEventEnabled;
    private final Rect mCurrentContentInset;
    private int mDefaultPanelMarginBottom;
    private int mDeviceType;
    private DropShadowHelper mDropShadowHelper;
    private boolean mEnableStickyViewContentInset;
    private boolean mEnableWindowStatusBarInsets;
    private int mExtraHorizontalPadding;
    private boolean mExtraPaddingApplyToContentEnable;
    private boolean mExtraPaddingEnable;
    private boolean mExtraPaddingInitEnable;
    private List mExtraPaddingObserver;
    private ExtraPaddingPolicy mExtraPaddingPolicy;
    private int mFloatingPanelUserMarginBottom;
    private int mFloatingPanelUserMarginTop;
    public final FloatingPanelHelper mFloatingRoundViewHelper;
    private final FloatingPanelHelper.ShapeOutlineProvider mFloatingRoundViewOutline;
    private FloatingABOLayoutSpec mFloatingWindowSize;
    private final Rect mGroupButtonInsetsRect;
    private GroupButtonsPanel mGroupButtonPanelView;
    private int mImeInsetBottom;
    private Rect mInnerInsets;
    private int mInsetTopInMiuixFloating;
    private WindowInsetsController$InsetsConfig mInsetsConfig;
    private WindowInsetsController$InsetsConfig mInternalInsetsConfig;
    private boolean mIsFloatingPanelMode;
    private boolean mIsFloatingTheme;
    private boolean mIsFloatingWindow;
    private boolean mIsInSearchMode;
    private boolean mIsInsideFloatingPanel;
    private boolean mIsMiuixFloating;
    private Rect mLastBaseContentInsets;
    private int mLastContentFadeHeight;
    private int mLastContentTop;
    private final Rect mLastDispatchContentInset;
    private Rect mLastInnerInsets;
    private boolean mLayoutStable;
    private LifecycleOwner mLifecycleOwner;
    private MaterialDayNightConfig mMaterial;
    private int mNavBarButtonThresholdPx;
    private int mNestedScrollMotionEventYAdjustment;
    private boolean mNestedScrollingParentEnabled;
    private final int[] mOffsetInWindow;
    private View.OnLayoutChangeListener mOnContainerViewLayoutChangeListener;
    private final Rect mOriginalInset;
    private boolean mOverlayMode;
    private Runnable mPostScroll;
    private View mPostScrollTarget;
    private final Scroller mPostScroller;
    private boolean mRequestFitSystemWindow;
    private boolean mRootSubDecor;
    private Drawable mSavedContentViewBackground;
    private Drawable mSavedSelfBackground;
    private int mShadowDisableClipDepth;
    private boolean mShouldExtraPaddingHorizontalNotifyChanged;
    private boolean mSqueezeContentByIme;
    private int mSystemBarsInsetBottom;
    private final Rect mThemeCompatSystemInset;
    private int mTranslucentStatus;
    private WindowInsetsController$InsetsConfig mUserInsetsConfig;

    static {
        boolean zEqualsIgnoreCase;
        try {
            zEqualsIgnoreCase = "true".equalsIgnoreCase(CommonUtils.readProp("miuix.material.navigation"));
        } catch (Exception unused) {
            zEqualsIgnoreCase = false;
        }
        NAVIGATION_MATERIAL_ENABLED = zEqualsIgnoreCase;
        MaterialToken materialTokenBuild = new MaterialToken.Builder(10, "internal-pured-thick-glass", "light").setColorBlend(ColorBlendToken.Pured_Thick_Glass_Light).setElementBlur(60).setBloomStroke(BloomStrokeToken.Glass_Stroke_Middle_Light).build();
        MATERIAL_FLOATING_PANEL_LIGHT = materialTokenBuild;
        MaterialToken materialTokenBuild2 = new MaterialToken.Builder(10, "internal-pured-thick-glass", "dark").setColorBlend(ColorBlendToken.Pured_Thick_Glass_Dark).setElementBlur(60).setBloomStroke(BloomStrokeToken.Glass_Stroke_Middle_Dark).build();
        MATERIAL_FLOATING_PANEL_DARK = materialTokenBuild2;
        MATERIAL_FLOATING_PANEL = new MaterialDayNightToken(materialTokenBuild, materialTokenBuild2);
    }

    public ActionBarOverlayLayout(Context context) {
        this(context, null);
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.view.View
    public void setAlpha(float f) {
        if (this.mIsFloatingPanelMode && this.mDropShadowHelper != null) {
            this.mContentAlpha = f;
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).setAlpha(f);
            }
            return;
        }
        super.setAlpha(f);
    }

    @Override // android.view.View
    public float getAlpha() {
        if (this.mIsFloatingPanelMode && this.mDropShadowHelper != null) {
            return this.mContentAlpha;
        }
        return super.getAlpha();
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCoordinatedScrollViewSet = new HashSet();
        this.mLifecycleOwner = null;
        this.mIsInSearchMode = false;
        this.mRootSubDecor = true;
        this.mBaseContentInsets = new Rect();
        this.mLastBaseContentInsets = new Rect();
        this.mContentInsets = new Rect();
        this.mBaseInnerInsets = new Rect();
        this.mLastInnerInsets = new Rect();
        this.mInnerInsets = new Rect();
        this.mContentMaskInsets = new Rect();
        this.mCurrentContentInset = new Rect();
        this.mLastDispatchContentInset = new Rect();
        this.mThemeCompatSystemInset = new Rect();
        this.mOriginalInset = new Rect();
        this.mBottomMenuVisibleHeight = new int[2];
        this.mIsFloatingPanelMode = false;
        this.mFloatingPanelUserMarginTop = -1;
        this.mFloatingPanelUserMarginBottom = -1;
        this.mAnimateContentMarginBottomInsets = null;
        this.mContextMenuCallback = new ContextMenuCallback();
        this.mIsFloatingTheme = false;
        this.mIsFloatingWindow = false;
        this.mImeInsetBottom = 0;
        this.mSystemBarsInsetBottom = 0;
        this.mCorrectNestedScrollMotionEventEnabled = true;
        this.mNestedScrollMotionEventYAdjustment = 0;
        this.mGroupButtonInsetsRect = new Rect();
        this.mSqueezeContentByIme = false;
        this.mLayoutStable = false;
        this.mNestedScrollingParentEnabled = true;
        this.mEnableWindowStatusBarInsets = true;
        this.mIsInsideFloatingPanel = false;
        this.mEnableStickyViewContentInset = false;
        this.mContentFadeEnabled = false;
        this.mContentFadeShouldDraw = false;
        this.mLastContentFadeHeight = 0;
        this.mLastContentTop = 0;
        this.mContentFadeMaskAlpha = 0.0f;
        this.mShadowDisableClipDepth = 2;
        this.mContentAlpha = 1.0f;
        this.mOffsetInWindow = new int[2];
        this.mPostScroll = new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarOverlayLayout.5
            @Override // java.lang.Runnable
            public void run() {
                if (ActionBarOverlayLayout.this.mPostScroller.computeScrollOffset()) {
                    if (ActionBarOverlayLayout.this.mActionBar != null && ActionBarOverlayLayout.this.mPostScrollTarget != null) {
                        ((ActionBarImpl) ActionBarOverlayLayout.this.mActionBar).updateTopOffsetOnPostScroll(ActionBarOverlayLayout.this.mPostScrollTarget, ActionBarOverlayLayout.this.mPostScroller.getCurrY());
                    }
                    if (ActionBarOverlayLayout.this.mPostScroller.isFinished()) {
                        return;
                    }
                    ActionBarOverlayLayout.this.postOnAnimation(this);
                }
            }
        };
        SmoothCornerHelper.init(context);
        FloatingPanelHelper floatingPanelHelper = new FloatingPanelHelper(this);
        this.mFloatingRoundViewHelper = floatingPanelHelper;
        FloatingPanelHelper.ShapeOutlineProvider shapeOutlineProvider = new FloatingPanelHelper.ShapeOutlineProvider(floatingPanelHelper);
        this.mFloatingRoundViewOutline = shapeOutlineProvider;
        setOutlineProvider(shapeOutlineProvider);
        setClipToOutline(true);
        this.mPostScroller = new Scroller(context);
        this.mFloatingWindowSize = new FloatingABOLayoutSpec(context, attributeSet);
        this.mDeviceType = DeviceHelper.detectType(context);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Window, i, 0);
        this.mIsFloatingTheme = typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_isMiuixFloatingTheme, false);
        this.mIsFloatingWindow = BaseFloatingActivityHelper.isFloatingWindow(context);
        boolean z = typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_contentAutoFitSystemWindow, false);
        this.mContentAutoFitSystemWindow = z;
        if (z) {
            this.mContentHeaderBackground = typedArrayObtainStyledAttributes.getDrawable(R$styleable.Window_contentHeaderBackground);
        }
        this.mEnableWindowStatusBarInsets = typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_enableWindowStatusBarInsets, true);
        this.mIsInsideFloatingPanel = typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_isInsideFloatingPanel, false);
        this.mEnableStickyViewContentInset = typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_enableStickyViewContentInset, false);
        typedArrayObtainStyledAttributes.recycle();
        this.mBottomMenuModeConfig = AttributeResolver.resolveInt(context, R$attr.bottomMenuMode, 3);
        this.mShadowDisableClipDepth = AttributeResolver.resolveInt(context, R$attr.floatingPanelShadowDisableClipDepth, 2);
        this.mSqueezeContentByIme = AttributeResolver.resolveBoolean(context, R$attr.squeezeContentByIme, false);
        this.mLayoutStable = AttributeResolver.resolveBoolean(context, R$attr.layoutStable, false);
        this.mInsetTopInMiuixFloating = getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_floating_window_top_offset);
        this.mNavBarButtonThresholdPx = MiuixUIUtils.dp2px(context, 24.0f);
        this.mDefaultPanelMarginBottom = getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_floating_panel_default_margin);
        Paint paint = new Paint(1);
        this.mContentFadePaint = paint;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    int getDeviceType() {
        return this.mDeviceType;
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.mLifecycleOwner = lifecycleOwner;
    }

    public void setContentInsetStateCallback(IContentInsetState iContentInsetState) {
        this.mContentInsetStateCallback = iContentInsetState;
    }

    public void onFloatingModeChanged(boolean z) {
        if (this.mIsMiuixFloating != (this.mIsFloatingTheme && z)) {
            this.mIsFloatingWindow = z;
            this.mIsMiuixFloating = z;
            if (z) {
                this.mInsetTopInMiuixFloating = getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_floating_window_top_offset);
            }
            this.mFloatingWindowSize.onFloatingModeChanged(this.mIsMiuixFloating);
            ActionBar actionBar = this.mActionBar;
            if (actionBar != null) {
                ((ActionBarImpl) actionBar).onFloatingModeChanged(this.mIsMiuixFloating);
            }
            requestFitSystemWindows();
            requestLayout();
        }
    }

    public boolean isFloatingWindowMode() {
        return this.mIsMiuixFloating;
    }

    public void setRadius(float f) {
        this.mFloatingRoundViewHelper.setRadius(f);
    }

    public void setFloatingPanelMode(boolean z) {
        if (this.mIsFloatingPanelMode != z) {
            this.mIsFloatingPanelMode = z;
            this.mFloatingRoundViewHelper.setEnable(z && !this.mIsFloatingWindow);
            if (z) {
                setEnableBlur(true);
                applyBlur(true);
            } else {
                applyBlur(false);
                setEnableBlur(false);
            }
            updateFloatingPanelShadow(z);
            requestApplyInsets();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyFloatingPanelBlurBackground(boolean z) {
        Drawable background = getBackground();
        Drawable drawable = this.mSavedSelfBackground;
        if (z) {
            if (drawable == null) {
                this.mSavedSelfBackground = background;
            }
            setBackground(null);
        } else {
            setBackground(drawable);
            this.mSavedSelfBackground = null;
        }
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer != null) {
            actionBarContainer.updateBackground(z);
        }
        View view = this.mContentView;
        if (view != null) {
            if (z) {
                this.mSavedContentViewBackground = view.getBackground();
                this.mContentView.setBackground(null);
            } else {
                view.setBackground(this.mSavedContentViewBackground);
                this.mSavedContentViewBackground = null;
            }
        }
    }

    private void updateFloatingPanelShadow(boolean z) {
        if (z) {
            if (this.mDropShadowHelper == null) {
                boolean zResolveBoolean = AttributeResolver.resolveBoolean(getContext(), R$attr.isLightTheme, true);
                DropShadowConfig.Builder builder = new DropShadowConfig.Builder(ShadowToken.Regular.radius);
                int i = ShadowToken.Regular.color;
                DropShadowHelper dropShadowHelper = new DropShadowHelper(getContext(), builder.setColor(i, i).setOffsetXDp(ShadowToken.Regular.offsetX).setOffsetYDp(ShadowToken.Regular.offsetY).setClipShadowEnable(true).create(), zResolveBoolean);
                this.mDropShadowHelper = dropShadowHelper;
                dropShadowHelper.setClipShadow(true);
            }
            if (this.mDropShadowHelper.isEnableMiShadow()) {
                setElevation(0.0f);
                this.mDropShadowHelper.enableViewShadow(this, true, this.mShadowDisableClipDepth);
                return;
            } else {
                setElevation(MiuixUIUtils.dp2px(getContext(), 12.0f));
                return;
            }
        }
        setElevation(0.0f);
        DropShadowHelper dropShadowHelper2 = this.mDropShadowHelper;
        if (dropShadowHelper2 != null) {
            dropShadowHelper2.enableViewShadow(this, false, this.mShadowDisableClipDepth);
        }
    }

    @Override // miuix.view.Fence
    public boolean isFenceEnabled() {
        return this.mIsFloatingPanelMode;
    }

    public void setFloatingPanelUserMarginTop(int i) {
        this.mFloatingPanelUserMarginTop = i;
    }

    public void setFloatingPanelUserMarginBottom(int i) {
        this.mFloatingPanelUserMarginBottom = i;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Context context = getContext();
        DebugUtil.printDensityLog("->processActionBarOverlayLayout ConfigurationChanged newConfig.densityDpi " + configuration.densityDpi);
        DensityConfigManager.getInstance().tryUpdateConfig(context, configuration);
        AutoDensityConfig.updateDensity(context);
        this.mFloatingWindowSize.onConfigurationChanged();
        handleBlurConfigChanged();
        handleDropShadowConfigChanged(configuration);
        postRefreshMenus();
        handleContextMenuRefresh();
    }

    private void handleBlurConfigChanged() {
        if (this.mBlurUiHelper == null) {
            return;
        }
        boolean zIsApplyBlur = isApplyBlur();
        boolean zIsEnableBlur = isEnableBlur();
        if (zIsApplyBlur && zIsEnableBlur) {
            applyBlur(false);
        }
        this.mBlurUiHelper.onConfigChanged();
        if (isEnableBlur() && isSupportBlur() && !isApplyBlur()) {
            applyBlur(true);
        }
    }

    private void handleDropShadowConfigChanged(Configuration configuration) {
        if (this.mDropShadowHelper == null || !this.mIsFloatingPanelMode) {
            return;
        }
        this.mDropShadowHelper.onConfigChanged(this, configuration, AttributeResolver.resolveBoolean(getContext(), R$attr.isLightTheme, true));
    }

    private void postRefreshMenus() {
        post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarOverlayLayout$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ActionBarOverlayLayout.$r8$lambda$6ZK9FjoI1Q5kzHxNFV1og731Mmk(this.f$0);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$6ZK9FjoI1Q5kzHxNFV1og731Mmk(ActionBarOverlayLayout actionBarOverlayLayout) {
        if (actionBarOverlayLayout.isAttachedToWindow()) {
            ActionBarContextView actionBarContextView = actionBarOverlayLayout.mActionBarContextView;
            if (actionBarContextView != null) {
                actionBarContextView.refreshBottomMenu();
            }
            ActionBarView actionBarView = actionBarOverlayLayout.mActionBarView;
            if (actionBarView != null) {
                actionBarView.refreshBottomMenu();
            }
            if (actionBarOverlayLayout.mContextMenu != null) {
                LifecycleOwner lifecycleOwner = actionBarOverlayLayout.mLifecycleOwner;
                if (lifecycleOwner != null ? lifecycleOwner.getLifecycle().getCurrentState().equals(Lifecycle.State.RESUMED) : true) {
                    return;
                }
                actionBarOverlayLayout.mContextMenu.close();
            }
        }
    }

    private void handleContextMenuRefresh() {
        ContextMenuBuilder contextMenuBuilder = this.mContextMenu;
        if (contextMenuBuilder == null || !contextMenuBuilder.isContextMenuPopupWindowShowing()) {
            return;
        }
        this.mContextMenu.refreshContextMenuPopupWindow();
    }

    public void setExtraPaddingPolicy(ExtraPaddingPolicy extraPaddingPolicy) {
        if (this.mExtraPaddingPolicy == null && extraPaddingPolicy != null) {
            this.mExtraPaddingPolicy = extraPaddingPolicy;
            extraPaddingPolicy.setEnable(this.mExtraPaddingEnable);
            if (this.mExtraPaddingInitEnable) {
                updateExtraPaddingHorizontal(getContext(), this.mExtraPaddingPolicy, -1, -1);
                this.mShouldExtraPaddingHorizontalNotifyChanged = false;
                if (this.mExtraPaddingObserver != null) {
                    for (int i = 0; i < this.mExtraPaddingObserver.size(); i++) {
                        ((ExtraPaddingObserver) this.mExtraPaddingObserver.get(i)).setExtraHorizontalPadding(this.mExtraHorizontalPadding);
                    }
                }
            }
        } else {
            this.mExtraPaddingPolicy = extraPaddingPolicy;
            if (extraPaddingPolicy != null) {
                extraPaddingPolicy.setEnable(this.mExtraPaddingEnable);
            }
        }
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            ((ActionBarImpl) actionBar).setExtraPaddingPolicy(this.mExtraPaddingPolicy);
        }
        requestLayout();
    }

    public void addExtraPaddingObserver(ExtraPaddingObserver extraPaddingObserver) {
        if (this.mExtraPaddingObserver == null) {
            this.mExtraPaddingObserver = new CopyOnWriteArrayList();
        }
        if (this.mExtraPaddingObserver.contains(extraPaddingObserver)) {
            return;
        }
        this.mExtraPaddingObserver.add(extraPaddingObserver);
        extraPaddingObserver.setExtraHorizontalPadding(this.mExtraHorizontalPadding);
    }

    public void setExtraHorizontalPaddingEnable(boolean z) {
        if (this.mExtraPaddingEnable != z) {
            this.mExtraPaddingEnable = z;
            ExtraPaddingPolicy extraPaddingPolicy = this.mExtraPaddingPolicy;
            if (extraPaddingPolicy != null) {
                extraPaddingPolicy.setEnable(z);
                requestLayout();
            }
        }
    }

    public void setExtraHorizontalPaddingInitEnable(boolean z) {
        if (this.mExtraPaddingInitEnable != z) {
            this.mExtraPaddingInitEnable = z;
        }
    }

    public void setExtraPaddingApplyToContentEnable(boolean z) {
        if (this.mExtraPaddingApplyToContentEnable != z) {
            this.mExtraPaddingApplyToContentEnable = z;
            requestLayout();
        }
    }

    public boolean isExtraPaddingApplyToContentEnable() {
        return this.mExtraPaddingApplyToContentEnable;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        View.OnLayoutChangeListener onLayoutChangeListener;
        super.onDetachedFromWindow();
        setContentInsetStateCallback(null);
        List list = this.mExtraPaddingObserver;
        if (list != null) {
            list.clear();
        }
        GroupButtonsPanel groupButtonsPanel = this.mGroupButtonPanelView;
        if (groupButtonsPanel == null || (onLayoutChangeListener = this.mOnContainerViewLayoutChangeListener) == null) {
            return;
        }
        groupButtonsPanel.removeOnLayoutChangeListener(onLayoutChangeListener);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean showContextMenuForChild(View view, float f, float f2) {
        if (internalShowContextMenu(view, f, f2)) {
            return true;
        }
        return getParent() != null && getParent().showContextMenuForChild(view, f, f2);
    }

    private boolean internalShowContextMenu(View view, float f, float f2) {
        ContextMenuBuilder contextMenuBuilder = this.mContextMenu;
        if (contextMenuBuilder == null) {
            ContextMenuBuilder contextMenuBuilder2 = new ContextMenuBuilder(getContext());
            this.mContextMenu = contextMenuBuilder2;
            contextMenuBuilder2.setCallback(this.mContextMenuCallback);
        } else {
            contextMenuBuilder.clear();
        }
        if (view != null) {
            this.mContextMenuPopupWindowHelper = this.mContextMenu.show(view, view.getWindowToken(), f, f2);
        }
        ContextMenuPopupWindowHelper contextMenuPopupWindowHelper = this.mContextMenuPopupWindowHelper;
        if (contextMenuPopupWindowHelper != null) {
            contextMenuPopupWindowHelper.setPresenterCallback(this.mContextMenuCallback);
            return true;
        }
        return super.showContextMenuForChild(view);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean showContextMenuForChild(View view) {
        ContextMenuBuilder contextMenuBuilder = this.mContextMenu;
        if (contextMenuBuilder == null) {
            ContextMenuBuilder contextMenuBuilder2 = new ContextMenuBuilder(getContext());
            this.mContextMenu = contextMenuBuilder2;
            contextMenuBuilder2.setCallback(this.mContextMenuCallback);
        } else {
            contextMenuBuilder.clear();
        }
        MenuDialogHelper menuDialogHelperShow = this.mContextMenu.show(view, view.getWindowToken());
        this.mContextMenuHelper = menuDialogHelperShow;
        if (menuDialogHelperShow != null) {
            menuDialogHelperShow.setPresenterCallback(this.mContextMenuCallback);
            return true;
        }
        return super.showContextMenuForChild(view);
    }

    @Override // android.view.View
    protected boolean fitSystemWindows(Rect rect) {
        boolean z;
        boolean zApplyInsetsByMargin;
        boolean zApplyInsetsByMargin2;
        WindowInsetsController$InsetsConfig windowInsetsController$InsetsConfig;
        Insets insets;
        Insets insets2;
        dispatchInsetsIgnoreVisibility(this, this.mLayoutStable);
        boolean zIsLayoutHideNavigation = isLayoutHideNavigation();
        boolean zIsTranslucentStatus = isTranslucentStatus();
        this.mSystemBarsInsetBottom = 0;
        this.mOriginalInset.set(rect);
        this.mThemeCompatSystemInset.set(rect);
        this.mBaseInnerInsets.set(rect);
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(this);
        if (rootWindowInsets != null) {
            if (this.mLayoutStable) {
                insets = rootWindowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars());
                insets2 = rootWindowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            } else {
                insets = rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                insets2 = rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            }
            this.mSystemBarsInsetBottom = insets.bottom;
            int i = rootWindowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            this.mImeInsetBottom = i;
            Rect rect2 = this.mThemeCompatSystemInset;
            rect2.left = insets2.left;
            rect2.right = insets2.right;
            rect2.bottom = this.mSystemBarsInsetBottom;
            if (this.mSqueezeContentByIme && i > 0) {
                rect2.bottom = 0;
            }
        }
        if (!zIsLayoutHideNavigation) {
            this.mThemeCompatSystemInset.bottom = 0;
            if (isNavigationBarToLeftEdge(rootWindowInsets, this.mLayoutStable)) {
                this.mThemeCompatSystemInset.left = 0;
            }
            if (isNavigationBarToRightEdge(rootWindowInsets, this.mLayoutStable)) {
                this.mThemeCompatSystemInset.right = 0;
            }
        }
        boolean z2 = true;
        if (isInFloatingMode()) {
            Rect rect3 = this.mBaseInnerInsets;
            rect3.top = this.mInsetTopInMiuixFloating;
            rect3.left = 0;
            rect3.right = 0;
            Rect rect4 = this.mThemeCompatSystemInset;
            rect4.top = this.mBaseContentInsets.top;
            rect4.bottom = 0;
            rect4.left = 0;
            rect4.right = 0;
        } else if (this.mIsFloatingPanelMode) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                int i2 = this.mFloatingPanelUserMarginTop;
                if (i2 < 0) {
                    i2 = 0;
                }
                int i3 = i2 + rect.top;
                int i4 = this.mFloatingPanelUserMarginBottom;
                if (i4 < 0) {
                    i4 = this.mDefaultPanelMarginBottom;
                }
                int i5 = this.mSystemBarsInsetBottom;
                if (i5 >= this.mNavBarButtonThresholdPx) {
                    i4 += i5;
                }
                if (marginLayoutParams.topMargin != i3) {
                    marginLayoutParams.topMargin = i3;
                    z = true;
                } else {
                    z = false;
                }
                if (marginLayoutParams.bottomMargin != i4) {
                    marginLayoutParams.bottomMargin = i4;
                    z = true;
                }
                if (z) {
                    setLayoutParams(marginLayoutParams);
                }
            }
            Rect rect5 = this.mBaseInnerInsets;
            rect5.top = 0;
            rect5.left = 0;
            rect5.right = 0;
            Rect rect6 = this.mThemeCompatSystemInset;
            rect6.top = 0;
            rect6.bottom = 0;
            rect6.left = 0;
            rect6.right = 0;
        }
        if (this.mIsInsideFloatingPanel) {
            this.mBaseInnerInsets.top = 0;
            this.mThemeCompatSystemInset.top = 0;
        }
        if (MiuixUIUtils.renderContentInCutoutArea(getContext())) {
            Rect rect7 = this.mBaseInnerInsets;
            rect7.left = 0;
            rect7.right = 0;
            Insets displayCoutInsets = getDisplayCoutInsets();
            if (isCutoutToLeftEdge(displayCoutInsets)) {
                this.mThemeCompatSystemInset.left = 0;
            }
            if (isCutoutToRightEdge(displayCoutInsets)) {
                this.mThemeCompatSystemInset.right = 0;
            }
        }
        if (this.mOverlayMode) {
            updateCurrentContentInsetInOverlayMode();
        } else {
            updateCurrentContentInset();
        }
        if (!isRootSubDecor() && (!zIsLayoutHideNavigation || this.mBaseInnerInsets.bottom != this.mSystemBarsInsetBottom)) {
            this.mBaseInnerInsets.bottom = 0;
        }
        WindowInsetsController$InsetsConfig windowInsetsController$InsetsConfig2 = this.mInsetsConfig;
        if (windowInsetsController$InsetsConfig2 != null && !windowInsetsController$InsetsConfig2.isFloatingMode) {
            if (windowInsetsController$InsetsConfig2.ignoreLeftInset) {
                this.mBaseInnerInsets.left = 0;
                this.mThemeCompatSystemInset.left = 0;
            }
            if (windowInsetsController$InsetsConfig2.ignoreTopInset) {
                this.mBaseInnerInsets.top = 0;
                this.mThemeCompatSystemInset.top = 0;
            }
            if (windowInsetsController$InsetsConfig2.ignoreRightInset) {
                this.mBaseInnerInsets.right = 0;
                this.mThemeCompatSystemInset.right = 0;
            }
            if (windowInsetsController$InsetsConfig2.ignoreBottomInset) {
                this.mBaseInnerInsets.bottom = 0;
                this.mThemeCompatSystemInset.bottom = 0;
            }
        }
        computeFitSystemInsets(zIsTranslucentStatus, zIsLayoutHideNavigation, this.mImeInsetBottom, this.mBaseInnerInsets, this.mBaseContentInsets);
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer != null) {
            if (zIsTranslucentStatus) {
                actionBarContainer.setPendingInsets(this.mBaseInnerInsets);
            }
            ActionMode actionMode = this.mActionMode;
            if (actionMode instanceof SearchActionModeImpl) {
                ((SearchActionModeImpl) actionMode).setPendingInsets(this.mBaseInnerInsets);
            }
            zApplyInsetsByMargin = applyInsetsByMargin(this.mActionBarTop, this.mThemeCompatSystemInset, true, isRootSubDecor() && !zIsTranslucentStatus, true, false);
        } else {
            zApplyInsetsByMargin = false;
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            actionBarContextView.setPendingInset(this.mThemeCompatSystemInset);
        }
        ActionBarContainer actionBarContainer2 = this.mActionBarBottom;
        if (actionBarContainer2 != null) {
            actionBarContainer2.setPendingInsets(this.mThemeCompatSystemInset);
            this.mContentMaskInsets.set(this.mBaseInnerInsets);
            Rect rect8 = new Rect();
            rect8.set(this.mBaseContentInsets);
            if (this.mIsFloatingWindow || ((windowInsetsController$InsetsConfig = this.mInsetsConfig) != null && windowInsetsController$InsetsConfig.isFloatingMode)) {
                rect8.bottom = 0;
            }
            if (this.mSqueezeContentByIme) {
                Rect rect9 = new Rect(this.mThemeCompatSystemInset);
                rect9.bottom = this.mBaseContentInsets.bottom;
                zApplyInsetsByMargin2 = applyInsetsByMargin(this.mActionBarBottom, rect9, true, false, true, true);
            } else {
                zApplyInsetsByMargin2 = applyInsetsByMargin(this.mActionBarBottom, this.mThemeCompatSystemInset, true, false, true, false);
            }
            zApplyInsetsByMargin |= zApplyInsetsByMargin2;
        }
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.setPendingInset(this.mThemeCompatSystemInset);
        }
        if (this.mLastBaseContentInsets.equals(this.mBaseContentInsets)) {
            z2 = zApplyInsetsByMargin;
        } else {
            this.mLastBaseContentInsets.set(this.mBaseContentInsets);
        }
        if (z2) {
            requestLayout();
        }
        return super.fitSystemWindows(rect);
    }

    private void dispatchInsetsIgnoreVisibility(ViewGroup viewGroup, boolean z) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                dispatchInsetsIgnoreVisibility((ViewGroup) childAt, z);
            }
        }
    }

    void updateCurrentContentInsetInOverlayMode() {
        int i;
        int i2;
        ActionBarContainer actionBarContainer;
        this.mCurrentContentInset.set(this.mThemeCompatSystemInset);
        int iMax = 0;
        if (this.mActionBar != null && (actionBarContainer = this.mActionBarTop) != null && actionBarContainer.getVisibility() != 8 && this.mActionBarTop.getMeasuredHeight() > 0) {
            int topViewHeight = ((ActionBarImpl) this.mActionBar).getTopViewHeight() + (this.mEnableWindowStatusBarInsets ? this.mThemeCompatSystemInset.top : 0) + (isInFloatingMode() ? this.mInsetTopInMiuixFloating : 0);
            if (this.mEnableStickyViewContentInset) {
                topViewHeight += this.mActionBarTop.getStickyAreaHeight();
            }
            iMax = Math.max(0, (int) (topViewHeight + this.mActionBarTop.getTranslationY()));
        }
        int iMax2 = Math.max(Math.max(Math.max(getBottomInset(), this.mBottomExtraInset), this.mBottomMenuExtraInset), this.mGroupButtonInsetsRect.bottom);
        if (isTranslucentStatus() && iMax < (i2 = this.mThemeCompatSystemInset.top)) {
            this.mCurrentContentInset.top = i2;
        } else {
            this.mCurrentContentInset.top = iMax;
        }
        if (isLayoutHideNavigation() && iMax2 < (i = this.mThemeCompatSystemInset.bottom)) {
            this.mCurrentContentInset.bottom = i;
        } else {
            this.mCurrentContentInset.bottom = iMax2;
        }
        Rect rect = this.mCurrentContentInset;
        int i3 = rect.left;
        Rect rect2 = this.mThemeCompatSystemInset;
        int i4 = rect2.left;
        if (i3 < i4) {
            rect.left = i4;
        }
        int i5 = rect.right;
        int i6 = rect2.right;
        if (i5 < i6) {
            rect.right = i6;
        }
        dispatchContentInset(rect);
    }

    private void updateCurrentContentInset() {
        int i;
        this.mCurrentContentInset.set(0, 0, 0, 0);
        int iMax = Math.max(Math.max(getBottomInset(), this.mBottomExtraInset), this.mBottomMenuExtraInset);
        if (isLayoutHideNavigation() && iMax < (i = this.mThemeCompatSystemInset.bottom)) {
            this.mCurrentContentInset.bottom = i;
        } else {
            this.mCurrentContentInset.bottom = iMax;
        }
        Rect rect = this.mCurrentContentInset;
        int i2 = rect.left;
        Rect rect2 = this.mThemeCompatSystemInset;
        int i3 = rect2.left;
        if (i2 < i3) {
            rect.left = i3;
        }
        int i4 = rect.right;
        int i5 = rect2.right;
        if (i4 < i5) {
            rect.right = i5;
        }
    }

    private boolean isCutoutToLeftEdge(Insets insets) {
        return insets != null && insets.left > 0;
    }

    private boolean isCutoutToRightEdge(Insets insets) {
        return insets != null && insets.right > 0;
    }

    private Insets getDisplayCoutInsets() {
        DisplayCutout cutout;
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(this);
        if (rootWindowInsets == null) {
            return null;
        }
        DisplayCutoutCompat displayCutout = rootWindowInsets.getDisplayCutout();
        if (displayCutout == null) {
            Activity activityContextFromView = getActivityContextFromView(this);
            if (activityContextFromView == null || (cutout = activityContextFromView.getWindowManager().getDefaultDisplay().getCutout()) == null) {
                return null;
            }
            return Insets.of(cutout.getSafeInsetLeft(), cutout.getSafeInsetTop(), cutout.getSafeInsetRight(), cutout.getSafeInsetBottom());
        }
        return Insets.of(displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(), displayCutout.getSafeInsetBottom());
    }

    private boolean isNavigationBarToLeftEdge(WindowInsetsCompat windowInsetsCompat, boolean z) {
        Insets insets;
        if (windowInsetsCompat == null) {
            return false;
        }
        if (z) {
            insets = windowInsetsCompat.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.captionBar() | WindowInsetsCompat.Type.navigationBars());
        } else {
            insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.captionBar() | WindowInsetsCompat.Type.navigationBars());
        }
        return insets.left > 0;
    }

    private boolean isNavigationBarToRightEdge(WindowInsetsCompat windowInsetsCompat, boolean z) {
        Insets insets;
        if (windowInsetsCompat == null) {
            return false;
        }
        if (z) {
            insets = windowInsetsCompat.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.captionBar() | WindowInsetsCompat.Type.navigationBars());
        } else {
            insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.captionBar() | WindowInsetsCompat.Type.navigationBars());
        }
        return insets.right > 0;
    }

    public void requestDispatchContentInset() {
        notifyContentInset(false);
    }

    private void dispatchContentInset(Rect rect) {
        dispatchContentInset(rect, false);
    }

    private void dispatchContentInset(Rect rect, boolean z) {
        if (this.mLastDispatchContentInset.equals(rect)) {
            return;
        }
        this.mLastDispatchContentInset.set(rect);
        notifyContentInset(z);
    }

    private void notifyContentInset(boolean z) {
        boolean z2 = this.mOverlayMode;
        if (z2 || z) {
            ActionBar actionBar = this.mActionBar;
            if (actionBar != null && z2) {
                ((ActionBarImpl) actionBar).updateContentInsetForNestedObserver(this.mLastDispatchContentInset);
            }
            IContentInsetState iContentInsetState = this.mContentInsetStateCallback;
            if (iContentInsetState != null) {
                iContentInsetState.onContentInsetChanged(this.mLastDispatchContentInset);
            }
        }
    }

    private Activity getActivityContextFromView(View view) {
        Context context = ((ViewGroup) view.getRootView()).getChildAt(0).getContext();
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    public Rect getBaseInnerInsets() {
        return this.mBaseInnerInsets;
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        WindowInsets windowInsetsOnApplyWindowInsets = super.onApplyWindowInsets(windowInsets);
        return (windowInsetsOnApplyWindowInsets.isConsumed() || !isRootSubDecor()) ? windowInsetsOnApplyWindowInsets : windowInsets.consumeDisplayCutout();
    }

    private void computeFitSystemInsets(boolean z, boolean z2, int i, Rect rect, Rect rect2) {
        boolean zIsRootSubDecor = isRootSubDecor();
        rect2.set(rect);
        if ((!zIsRootSubDecor || z) && !this.mContentAutoFitSystemWindow) {
            rect2.top = 0;
        }
        if (this.mIsFloatingWindow || z2) {
            rect2.bottom = 0;
        } else {
            int i2 = rect2.bottom;
            if (i2 != 0) {
                int i3 = i2 - i;
                rect2.bottom = i3;
                if (i3 < 0) {
                    rect2.bottom = 0;
                }
            }
        }
        if (!this.mSqueezeContentByIme || i <= 0) {
            return;
        }
        rect2.bottom = this.mOriginalInset.bottom;
    }

    private boolean applyInsetsByMargin(View view, Rect rect, boolean z, boolean z2, boolean z3, boolean z4) {
        boolean z5 = false;
        if (view == null) {
            return false;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (z) {
            int i = layoutParams.leftMargin;
            int i2 = rect.left;
            if (i != i2) {
                layoutParams.leftMargin = i2;
                z5 = true;
            }
        }
        if (z2) {
            int i3 = layoutParams.topMargin;
            int i4 = rect.top;
            if (i3 != i4) {
                layoutParams.topMargin = i4;
                z5 = true;
            }
        }
        if (z3) {
            int i5 = layoutParams.rightMargin;
            int i6 = rect.right;
            if (i5 != i6) {
                layoutParams.rightMargin = i6;
                z5 = true;
            }
        }
        if (z4) {
            int i7 = layoutParams.bottomMargin;
            int i8 = rect.bottom;
            if (i7 != i8) {
                layoutParams.bottomMargin = i8;
                return true;
            }
        }
        return z5;
    }

    public void animateContentMarginBottomByBottomMenu(int i) {
        if (this.mAnimateContentMarginBottomInsets == null) {
            this.mAnimateContentMarginBottomInsets = new Rect();
        }
        Rect rect = this.mAnimateContentMarginBottomInsets;
        Rect rect2 = this.mContentInsets;
        rect.top = rect2.top;
        rect.bottom = i;
        rect.right = rect2.right;
        rect.left = rect2.left;
        applyInsetsByMargin(this.mContentView, rect, true, true, true, true);
        this.mContentView.requestLayout();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestFitSystemWindows();
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestFitSystemWindows() {
        super.requestFitSystemWindows();
        this.mRequestFitSystemWindow = true;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        pullChildren();
    }

    /* JADX WARN: Code duplicated, block: B:82:0x01af  */
    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        View view;
        int iMakeMeasureSpec;
        int i3;
        int i4;
        int widthMeasureSpec = this.mFloatingWindowSize.getWidthMeasureSpec(i);
        int heightMeasureSpec = this.mFloatingWindowSize.getHeightMeasureSpec(i2);
        View view2 = this.mContentView;
        View view3 = this.mContentMask;
        int i5 = 0;
        int iMax = 0;
        int iMax2 = 0;
        int iCombineMeasuredStates = 0;
        while (i5 < getChildCount()) {
            View childAt = getChildAt(i5);
            if (childAt != view2 && childAt != view3 && childAt.getVisibility() != 8) {
                measureChildWithMargins(childAt, widthMeasureSpec, 0, heightMeasureSpec, 0);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                iMax = Math.max(iMax, childAt.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                iMax2 = Math.max(iMax2, childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                iCombineMeasuredStates = FrameLayout.combineMeasuredStates(iCombineMeasuredStates, childAt.getMeasuredState());
            }
            i5++;
            widthMeasureSpec = widthMeasureSpec;
            heightMeasureSpec = heightMeasureSpec;
        }
        int i6 = widthMeasureSpec;
        int i7 = heightMeasureSpec;
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        int measuredHeight = (actionBarContainer == null || actionBarContainer.getVisibility() == 8) ? 0 : this.mActionBarTop.getMeasuredHeight();
        int bottomInset = getBottomInset();
        ActionBarView actionBarView = this.mActionBarView;
        int i8 = (actionBarView == null || !actionBarView.isSplitActionBar()) ? 0 : bottomInset;
        this.mInnerInsets.set(this.mBaseInnerInsets);
        this.mContentInsets.set(this.mBaseContentInsets);
        boolean zIsLayoutHideNavigation = isLayoutHideNavigation();
        boolean zIsTranslucentStatus = isTranslucentStatus();
        if (zIsTranslucentStatus && measuredHeight > 0) {
            this.mContentInsets.top = 0;
        }
        if (!this.mOverlayMode) {
            Rect rect = this.mContentInsets;
            rect.top += measuredHeight;
            rect.bottom += i8;
        } else {
            if (!zIsTranslucentStatus) {
                this.mInnerInsets.top += measuredHeight;
            } else if (measuredHeight > 0) {
                this.mInnerInsets.top = measuredHeight;
            }
            this.mInnerInsets.bottom += i8;
        }
        if ((!this.mIsFloatingTheme || !this.mIsFloatingWindow) && zIsLayoutHideNavigation) {
            if (getResources().getConfiguration().orientation == 2) {
                Rect rect2 = this.mContentInsets;
                rect2.right = 0;
                rect2.left = 0;
            }
            if (bottomInset == 0 && (!this.mSqueezeContentByIme || this.mImeInsetBottom <= 0)) {
                this.mContentInsets.bottom = 0;
            }
        }
        if (isBottomAnimating()) {
            view = view2;
        } else {
            view = view2;
            applyInsetsByMargin(view, this.mContentInsets, true, true, true, true);
            this.mAnimateContentMarginBottomInsets = null;
        }
        if (!this.mOverlayMode) {
            view.setPadding(view.getPaddingLeft(), 0, view.getPaddingRight(), view.getPaddingBottom());
        }
        if (!this.mLastInnerInsets.equals(this.mInnerInsets) || this.mRequestFitSystemWindow) {
            this.mLastInnerInsets.set(this.mInnerInsets);
            this.mRequestFitSystemWindow = false;
        }
        if (isTranslucentStatus() && this.mContentAutoFitSystemWindow) {
            Drawable drawable = this.mContentHeaderBackground;
            if (drawable != null) {
                drawable.setBounds(0, 0, getRight() - getLeft(), this.mBaseContentInsets.top);
            } else {
                ViewGroup viewGroup = (ViewGroup) findViewById(R.id.content);
                if (viewGroup != null && viewGroup.getChildCount() == 1) {
                    View childAt2 = viewGroup.getChildAt(0);
                    childAt2.setPadding(childAt2.getPaddingLeft(), 0, childAt2.getPaddingRight(), childAt2.getPaddingBottom());
                }
            }
        }
        ExtraPaddingPolicy extraPaddingPolicy = this.mExtraPaddingPolicy;
        if (extraPaddingPolicy == null || !extraPaddingPolicy.isEnable()) {
            iMakeMeasureSpec = i6;
        } else {
            int size = View.MeasureSpec.getSize(i6);
            updateExtraPaddingHorizontal(getContext(), this.mExtraPaddingPolicy, size, View.MeasureSpec.getSize(i7));
            if (this.mExtraPaddingApplyToContentEnable) {
                iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size - (this.mExtraHorizontalPadding * 2), View.MeasureSpec.getMode(i6));
            } else {
                iMakeMeasureSpec = i6;
            }
        }
        measureChildWithMargins(view, iMakeMeasureSpec, 0, i7, 0);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) view.getLayoutParams();
        int iMax3 = Math.max(iMax, view.getMeasuredWidth() + layoutParams2.leftMargin + layoutParams2.rightMargin);
        int iMax4 = Math.max(iMax2, view.getMeasuredHeight() + layoutParams2.topMargin + layoutParams2.bottomMargin);
        int iCombineMeasuredStates2 = FrameLayout.combineMeasuredStates(iCombineMeasuredStates, view.getMeasuredState());
        if (view3 == null || view3.getVisibility() != 0) {
            i3 = i6;
            i4 = i7;
        } else {
            applyInsetsByMargin(view3, this.mContentMaskInsets, true, false, true, true);
            i3 = i6;
            i4 = i7;
            measureChildWithMargins(view3, i3, 0, i4, 0);
        }
        setMeasuredDimension(FrameLayout.resolveSizeAndState(Math.max(iMax3 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i3, iCombineMeasuredStates2), FrameLayout.resolveSizeAndState(Math.max(iMax4 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i4, iCombineMeasuredStates2 << 16));
        post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarOverlayLayout$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.updateBottomMenuMode();
            }
        });
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        WindowInsetsCompat rootWindowInsets;
        Insets insets;
        super.onLayout(z, i, i2, i3, i4);
        if (this.mOverlayMode) {
            updateCurrentContentInsetInOverlayMode();
        }
        ExtraPaddingPolicy extraPaddingPolicy = this.mExtraPaddingPolicy;
        if (extraPaddingPolicy != null && extraPaddingPolicy.isEnable()) {
            if (this.mShouldExtraPaddingHorizontalNotifyChanged && this.mExtraPaddingObserver != null) {
                this.mShouldExtraPaddingHorizontalNotifyChanged = false;
                for (int i5 = 0; i5 < this.mExtraPaddingObserver.size(); i5++) {
                    ((ExtraPaddingObserver) this.mExtraPaddingObserver.get(i5)).onExtraPaddingChanged(this.mExtraHorizontalPadding);
                }
            }
            if (this.mExtraPaddingApplyToContentEnable) {
                this.mExtraPaddingPolicy.applyExtraPadding(this.mContentView);
            }
        }
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null && !this.mIsInSearchMode) {
            ((ActionBarImpl) actionBar).updateCoordinateOffsetView();
        }
        Context context = getContext();
        if (this.mUserInsetsConfig != null || (rootWindowInsets = ViewCompat.getRootWindowInsets(this)) == null) {
            return;
        }
        if (this.mLayoutStable) {
            insets = rootWindowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
        } else {
            insets = rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
        }
        int i6 = EnvStateManager.getScreenSize(context).x;
        if (i6 != -1) {
            int[] iArr = new int[2];
            getLocationOnScreen(iArr);
            int i7 = iArr[0];
            applyInternalWindowInsets(false, MiuixUIUtils.isLayoutHideNavigation(this), i7 >= insets.left, false, i6 - (getWidth() + i7) >= insets.right, false);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mFloatingRoundViewHelper.onSizeChanged(i, i2, i3, i4);
        if (this.mBlurUiHelper == null || !isApplyBlur() || i3 != 0 || i4 != 0 || i <= 0 || i2 <= 0) {
            return;
        }
        this.mBlurUiHelper.refreshBlur();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        Canvas canvas2;
        Drawable drawable;
        if (this.mContentAutoFitSystemWindow && (drawable = this.mContentHeaderBackground) != null) {
            drawable.setBounds(0, 0, getRight() - getLeft(), this.mBaseContentInsets.top);
            this.mContentHeaderBackground.draw(canvas);
        }
        if (shouldDrawContentFade()) {
            int contentFadeZoneHeight = getContentFadeZoneHeight();
            int top = this.mContentView.getTop();
            float f = top;
            float width = getWidth();
            canvas2 = canvas;
            int iSaveLayer = canvas2.saveLayer(0.0f, f, width, getHeight(), null);
            drawChild(canvas2, this.mContentView, getDrawingTime());
            updateContentFadeShader(contentFadeZoneHeight);
            this.mContentFadePaint.setShader(this.mContentFadeShader);
            this.mContentFadePaint.setAlpha(Math.round(this.mContentFadeMaskAlpha * 255.0f));
            canvas2.drawRect(0.0f, f, width, top + contentFadeZoneHeight, this.mContentFadePaint);
            canvas2.restoreToCount(iSaveLayer);
            ActionBarContainer actionBarContainer = this.mActionBarTop;
            if (actionBarContainer != null && actionBarContainer.getVisibility() == 0) {
                drawChild(canvas2, this.mActionBarTop, getDrawingTime());
            }
        } else {
            canvas2 = canvas;
            super.dispatchDraw(canvas2);
        }
        this.mFloatingRoundViewHelper.onBorderDraw(canvas2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateContentFadeAlpha(boolean z) {
        ValueAnimator valueAnimator = this.mContentFadeAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.mContentFadeMaskAlpha, z ? 1.0f : 0.0f);
        this.mContentFadeAnimator = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(100L);
        this.mContentFadeAnimator.setInterpolator(new LinearInterpolator());
        this.mContentFadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarOverlayLayout$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ActionBarOverlayLayout.$r8$lambda$BQXI2EdhGCNxSK2zs8J76PQEIG4(this.f$0, valueAnimator2);
            }
        });
        this.mContentFadeAnimator.start();
    }

    public static /* synthetic */ void $r8$lambda$BQXI2EdhGCNxSK2zs8J76PQEIG4(ActionBarOverlayLayout actionBarOverlayLayout, ValueAnimator valueAnimator) {
        actionBarOverlayLayout.getClass();
        actionBarOverlayLayout.mContentFadeMaskAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        actionBarOverlayLayout.invalidate();
    }

    private void updateContentFadeShader(int i) {
        View view = this.mContentView;
        int top = view != null ? view.getTop() : 0;
        if (i == this.mLastContentFadeHeight && top == this.mLastContentTop && this.mContentFadeShader != null) {
            return;
        }
        float[] fArr = {0.0f, 0.02f, 0.2f, 0.5f, 1.0f};
        int[] iArr = new int[5];
        for (int i2 = 0; i2 < 5; i2++) {
            iArr[i2] = Math.round(fArr[i2] * 255.0f) << 24;
        }
        this.mContentFadeShader = new LinearGradient(0.0f, top, 0.0f, top + i, iArr, new float[]{0.0f, 0.52f, 0.74f, 0.85f, 1.0f}, Shader.TileMode.CLAMP);
        this.mLastContentFadeHeight = i;
        this.mLastContentTop = top;
    }

    private int getContentFadeZoneHeight() {
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        int height = actionBarContainer != null ? actionBarContainer.getHeight() : 0;
        ActionBarContainer actionBarContainer2 = this.mActionBarTop;
        return height + MiuixUIUtils.dp2px(getContext(), actionBarContainer2 != null ? actionBarContainer2.getMaskExtraHeight() : 20.0f);
    }

    private boolean shouldDrawContentFade() {
        return this.mContentFadeEnabled && this.mContentFadeShouldDraw && this.mContentFadeMaskAlpha > 0.0f && this.mContentView != null;
    }

    protected int getBottomInset() {
        ActionBarContainer actionBarContainer = this.mActionBarBottom;
        if (actionBarContainer != null) {
            return actionBarContainer.getInsetHeight();
        }
        return 0;
    }

    boolean isBottomAnimating() {
        return this.mAnimating;
    }

    public void setActionBar(ActionBar actionBar) {
        this.mActionBar = actionBar;
        if (actionBar != null) {
            ((ActionBarImpl) actionBar).setExtraPaddingPolicy(this.mExtraPaddingPolicy);
        }
    }

    public void setOverlayMode(boolean z) {
        this.mOverlayMode = z;
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer != null) {
            actionBarContainer.setOverlayMode(z);
        }
    }

    public boolean isInOverlayMode() {
        return this.mOverlayMode;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public ContentMaskAnimator getContentMaskAnimator(View.OnClickListener onClickListener) {
        return new ContentMaskAnimator(onClickListener);
    }

    public Window.Callback getCallback() {
        return this.mCallback;
    }

    public void setCallback(Window.Callback callback) {
        this.mCallback = callback;
    }

    public void setTranslucentStatus(int i) {
        if (this.mTranslucentStatus != i) {
            this.mTranslucentStatus = i;
            requestFitSystemWindows();
        }
    }

    private void pullChildren() {
        if (this.mContentView == null) {
            this.mContentView = findViewById(R.id.content);
            ActionBarContainer actionBarContainer = (ActionBarContainer) findViewById(R$id.action_bar_container);
            this.mActionBarTop = actionBarContainer;
            boolean z = false;
            if (this.mIsFloatingTheme && this.mIsFloatingWindow && actionBarContainer != null && !AttributeResolver.resolveBoolean(getContext(), R$attr.windowActionBar, false)) {
                this.mActionBarTop.setVisibility(8);
                this.mActionBarTop = null;
            }
            ActionBarContainer actionBarContainer2 = this.mActionBarTop;
            if (actionBarContainer2 != null) {
                actionBarContainer2.setOverlayMode(this.mOverlayMode);
                ActionBarView actionBarView = (ActionBarView) this.mActionBarTop.findViewById(R$id.action_bar);
                this.mActionBarView = actionBarView;
                actionBarView.setBottomMenuMode(this.mBottomMenuMode);
                if (this.mIsFloatingTheme && this.mIsFloatingWindow) {
                    z = true;
                }
                this.mIsMiuixFloating = z;
                if (z) {
                    this.mInsetTopInMiuixFloating = getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_floating_window_top_offset);
                }
                this.mActionBarTop.setMiuixFloatingOnInit(this.mIsMiuixFloating);
            }
            ActionBarContainer actionBarContainer3 = this.mActionBarTop;
            if (actionBarContainer3 != null) {
                actionBarContainer3.addActionBarTransitionListener(new ActionBarTransitionListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarOverlayLayout.1
                    @Override // miuix.appcompat.app.ActionBarTransitionListener
                    public void onActionBarResizing(int i, float f, int i2) {
                    }

                    @Override // miuix.appcompat.app.ActionBarTransitionListener
                    public void onExpandStateChanged(int i) {
                    }

                    @Override // miuix.appcompat.app.ActionBarTransitionListener
                    public void onOverlayMaskStateChanged(boolean z2) {
                        if (ActionBarOverlayLayout.this.mContentFadeEnabled) {
                            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
                            if (z2) {
                                actionBarOverlayLayout.mContentFadeShouldDraw = false;
                                ActionBarOverlayLayout.this.animateContentFadeAlpha(false);
                            } else {
                                actionBarOverlayLayout.mContentFadeShouldDraw = true;
                                ActionBarOverlayLayout.this.animateContentFadeAlpha(true);
                            }
                        }
                    }
                });
            }
            initBlurUiHelper();
        }
    }

    private void initBlurUiHelper() {
        MaterialDayNightConfig materialDayNightConfigCreate;
        int iResolveInt = AttributeResolver.resolveInt(getContext(), R$attr.bgBlurOptions, 0);
        boolean z = NAVIGATION_MATERIAL_ENABLED;
        if (((iResolveInt & 4) != 0 || z) && HyperMaterialUtils.isEnable()) {
            if (RomUtils.getHyperOsVersion() > 2) {
                materialDayNightConfigCreate = MaterialDayNightConfig.create(MATERIAL_FLOATING_PANEL);
            } else {
                materialDayNightConfigCreate = MaterialDayNightConfig.create(Blur.ExtraHeavy);
            }
            this.mMaterial = materialDayNightConfigCreate;
            MiuiBlurUiHelper miuiBlurUiHelper = new MiuiBlurUiHelper(getContext(), this, false, false, new MiuiBlurUiHelper.BlurStateCallback() { // from class: miuix.appcompat.internal.app.widget.ActionBarOverlayLayout.2
                @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                public int getBackgroundColor() {
                    return 0;
                }

                @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                public void onBlurEnableStateChanged(boolean z2) {
                }

                @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                public boolean isLightTheme() {
                    return AttributeResolver.resolveBoolean(ActionBarOverlayLayout.this.getContext(), R$attr.isLightTheme, true);
                }

                @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                public Drawable getBackground() {
                    ActionBarContainer actionBarContainer = ActionBarOverlayLayout.this.mActionBarTop;
                    if (actionBarContainer != null) {
                        return actionBarContainer.getPrimaryBackground();
                    }
                    return null;
                }

                @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                public MaterialConfig.BlurConfig getBlurConfig(boolean z2) {
                    MaterialDayNightConfig materialDayNightConfig = ActionBarOverlayLayout.this.mMaterial;
                    if (materialDayNightConfig != null) {
                        return materialDayNightConfig.getBlurConfig(z2);
                    }
                    return null;
                }

                @Override // miuix.view.MiuiBlurUiHelper.BlurStateCallback
                public void onBlurApplyStateChanged(boolean z2) {
                    ActionBarOverlayLayout.this.applyFloatingPanelBlurBackground(z2);
                    if (z2 && ActionBarOverlayLayout.this.mMaterial != null) {
                        MaterialConfig.BloomStrokeConfig bloomStrokeConfig = ActionBarOverlayLayout.this.mMaterial.getBloomStrokeConfig(AttributeResolver.resolveBoolean(ActionBarOverlayLayout.this.getContext(), R$attr.isLightTheme, true));
                        if (bloomStrokeConfig != null) {
                            HyperBloomStrokeUtils.setBloomStrokeConfig(ActionBarOverlayLayout.this, bloomStrokeConfig);
                            return;
                        }
                        return;
                    }
                    HyperBloomStrokeUtils.clearBloomStroke(ActionBarOverlayLayout.this);
                }
            });
            this.mBlurUiHelper = miuiBlurUiHelper;
            miuiBlurUiHelper.setSupportBlur(!LiteUtils.isCommonLiteStrategy());
            this.mBlurUiHelper.setEnableBlur(HyperMaterialUtils.isFeatureEnable(getContext()));
        }
    }

    public void setSplitActionBarView(ActionBarContainer actionBarContainer) {
        this.mActionBarBottom = actionBarContainer;
        actionBarContainer.setPendingInsets(this.mThemeCompatSystemInset);
    }

    public void setContentMask(View view) {
        this.mContentMask = view;
        if (!miuix.internal.util.DeviceHelper.isOled() || this.mContentMask == null) {
            return;
        }
        ResourcesCompat.getDrawable(getContext().getResources(), R$drawable.miuix_appcompat_window_content_mask_oled, getContext().getTheme());
    }

    public View getContentMask() {
        return this.mContentMask;
    }

    public void setActionBarContextView(ActionBarContextView actionBarContextView) {
        this.mActionBarContextView = actionBarContextView;
        if (actionBarContextView != null) {
            actionBarContextView.setActionBarView(this.mActionBarView);
            this.mActionBarContextView.setBottomMenuMode(this.mBottomMenuMode);
            this.mActionBarContextView.setPendingInset(this.mThemeCompatSystemInset);
        }
    }

    public boolean isRootSubDecor() {
        return this.mRootSubDecor;
    }

    public boolean isTranslucentStatus() {
        if (MiuixUIUtils.isTargetSdkVersionAboveV(getContext())) {
            return true;
        }
        int windowSystemUiVisibility = getWindowSystemUiVisibility();
        boolean z = (windowSystemUiVisibility & DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER) != 0;
        boolean z2 = (windowSystemUiVisibility & SmartNetworkRelayScore.TRAFFIC_UNIT) != 0;
        boolean z3 = this.mTranslucentStatus != 0;
        if (this.mIsFloatingTheme) {
            return z2 || z3;
        }
        return (z && z2) || z3;
    }

    private boolean isLayoutHideNavigation() {
        if (MiuixUIUtils.isLayoutHideNavigation(this)) {
            return true;
        }
        WindowInsetsController$InsetsConfig windowInsetsController$InsetsConfig = this.mInsetsConfig;
        return windowInsetsController$InsetsConfig != null && windowInsetsController$InsetsConfig.renderUnderBottomDecorations;
    }

    public void setRootSubDecor(boolean z) {
        this.mRootSubDecor = z;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0 || actionMasked == 3 || actionMasked == 1) {
            this.mNestedScrollMotionEventYAdjustment = 0;
        }
        int i = this.mNestedScrollMotionEventYAdjustment;
        if (i != 0) {
            motionEvent.offsetLocation(0.0f, i);
            boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
            motionEvent.offsetLocation(0.0f, -this.mNestedScrollMotionEventYAdjustment);
            return zDispatchTouchEvent;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (super.onTouchEvent(motionEvent)) {
            return true;
        }
        return this.mIsFloatingTheme;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (super.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        if (isBackPressed(keyEvent)) {
            if (this.mActionMode != null) {
                ActionBarContextView actionBarContextView = this.mActionBarContextView;
                if (actionBarContextView == null || !actionBarContextView.hideOverflowMenu()) {
                    this.mActionMode.finish();
                    this.mActionMode = null;
                }
                return true;
            }
            ActionBarView actionBarView = this.mActionBarView;
            if (actionBarView != null && actionBarView.hideOverflowMenu()) {
                return true;
            }
        }
        return false;
    }

    private boolean isBackPressed(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return startActionMode(view, callback);
    }

    @Override // android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        ActionMode actionModeOnWindowStartingActionMode = null;
        if (actionBarContextView != null && actionBarContextView.isAnimating()) {
            return null;
        }
        ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        this.mActionMode = null;
        if (getCallback() != null) {
            actionModeOnWindowStartingActionMode = getCallback().onWindowStartingActionMode(createActionModeCallbackWrapper(callback));
        }
        if (actionModeOnWindowStartingActionMode != null) {
            this.mActionMode = actionModeOnWindowStartingActionMode;
        }
        if (this.mActionMode != null && getCallback() != null) {
            getCallback().onActionModeStarted(this.mActionMode);
        }
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null && actionBarView.isSplitActionBar()) {
            ActionMenuView actionMenuView = this.mActionBarView.getActionMenuView();
            if (actionMenuView != null) {
                setBottomMenuExtraInset(actionMenuView.getCollapsedHeight());
            }
            this.mActionBarView.makeMenuViewShowHideWithAnimation(false);
        }
        if ((this.mActionMode instanceof SearchActionMode) && this.mOverlayMode) {
            updateCurrentContentInsetInOverlayMode();
        }
        return this.mActionMode;
    }

    public ActionMode startActionMode(View view, ActionMode.Callback callback) {
        if (view instanceof ActionBarOverlayLayout) {
            ActionMode actionMode = this.mActionMode;
            if (actionMode != null) {
                actionMode.finish();
            }
            ActionMode actionModeStartActionMode = view.startActionMode(createActionModeCallbackWrapper(callback));
            this.mActionMode = actionModeStartActionMode;
            return actionModeStartActionMode;
        }
        return startActionMode(callback);
    }

    private ActionModeCallbackWrapper createActionModeCallbackWrapper(ActionMode.Callback callback) {
        if (callback instanceof SearchActionMode.Callback) {
            return new SearchActionModeCallbackWrapper(callback);
        }
        return new ActionModeCallbackWrapper(callback);
    }

    /* JADX WARN: Code duplicated, block: B:10:0x003a  */
    /* JADX WARN: Code duplicated, block: B:9:0x0038  */
    protected void updateBottomMenuMode() {
        int i = this.mBottomMenuModeConfig;
        float f = getContext().getResources().getDisplayMetrics().density;
        if (i == 0) {
            int measuredWidth = (int) ((getMeasuredWidth() * 1.0f) / f);
            int i2 = (int) ((EnvStateManager.getWindowSize(getContext()).x * 1.0f) / f);
            if (this.mDeviceType != 2 || measuredWidth <= 410 || i2 <= 600) {
                i = 2;
            } else {
                i = 3;
            }
        } else if (i == 1) {
            int i3 = (int) ((EnvStateManager.getWindowSize(getContext()).x * 1.0f) / f);
            if (this.mDeviceType != 2 || i3 <= 600) {
                i = 2;
            } else {
                i = 3;
            }
        }
        if (i != this.mBottomMenuMode) {
            this.mBottomMenuMode = i;
            ActionBarContextView actionBarContextView = this.mActionBarContextView;
            if (actionBarContextView != null) {
                actionBarContextView.setBottomMenuMode(i);
                this.mActionBarContextView.refreshBottomMenu();
            }
            ActionBarView actionBarView = this.mActionBarView;
            if (actionBarView != null) {
                actionBarView.setBottomMenuMode(this.mBottomMenuMode);
                this.mActionBarView.refreshBottomMenu();
            }
        }
    }

    public void onMenuStateChanged(int i, int i2) {
        int i3;
        int[] iArr = this.mBottomMenuVisibleHeight;
        iArr[i2] = i;
        int iMax = Math.max(iArr[0], iArr[1]);
        if (this.mOverlayMode) {
            if (isLayoutHideNavigation() && iMax <= (i3 = this.mThemeCompatSystemInset.bottom)) {
                iMax = i3;
            }
            this.mCurrentContentInset.bottom = Math.max(Math.max(iMax, this.mBottomMenuExtraInset), this.mBottomExtraInset);
            dispatchContentInset(this.mCurrentContentInset);
            return;
        }
        animateContentMarginBottomByBottomMenu(iMax);
    }

    public void setBottomMenuExtraInset(int i) {
        this.mBottomMenuExtraInset = i;
    }

    class ActionModeCallbackWrapper implements ActionMode.Callback {
        private ActionMode.Callback mWrapped;

        public ActionModeCallbackWrapper(ActionMode.Callback callback) {
            this.mWrapped = callback;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            ActionBarView actionBarView = ActionBarOverlayLayout.this.mActionBarView;
            if (actionBarView != null && actionBarView.isSplitActionBar()) {
                ActionBarOverlayLayout.this.mActionBarView.makeMenuViewShowHideWithAnimation(true);
            }
            if (ActionBarOverlayLayout.this.getCallback() != null) {
                ActionBarOverlayLayout.this.getCallback().onActionModeFinished(actionMode);
            }
            ActionBarOverlayLayout.this.mActionMode = null;
        }
    }

    class SearchActionModeCallbackWrapper extends ActionModeCallbackWrapper implements SearchActionMode.Callback {
        public SearchActionModeCallbackWrapper(ActionMode.Callback callback) {
            super(callback);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // miuix.appcompat.internal.app.widget.ActionBarOverlayLayout.ActionModeCallbackWrapper, android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            ((SearchActionMode) actionMode).setAnimatedViewListener(new SearchActionMode.AnimatedViewListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarOverlayLayout.SearchActionModeCallbackWrapper.1
                @Override // miuix.view.SearchActionMode.AnimatedViewListener
                public void onUpdateOffsetY(int i) {
                    ActionBarContainer actionBarContainer = ActionBarOverlayLayout.this.mActionBarTop;
                    if (actionBarContainer != null) {
                        actionBarContainer.setCoordinatedOffsetYInSearchModeAnimation(i);
                        ActionBarOverlayLayout.this.mActionBarTop.requestLayout();
                    }
                }

                @Override // miuix.view.SearchActionMode.AnimatedViewListener
                public void onInSearchMode(boolean z) {
                    if (ActionBarOverlayLayout.this.mIsInSearchMode != z) {
                        ActionBarOverlayLayout.this.mIsInSearchMode = z;
                        if (ActionBarOverlayLayout.this.mActionBar != null) {
                            ((ActionBarImpl) ActionBarOverlayLayout.this.mActionBar).updateCoordinateOffsetView();
                        }
                    }
                }
            });
            return super.onCreateActionMode(actionMode, menu);
        }
    }

    public class ContentMaskAnimator implements Animator.AnimatorListener {
        private ObjectAnimator mHideAnimator;
        private View.OnClickListener mOnClickListener;
        private ObjectAnimator mShowAnimator;

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        private ContentMaskAnimator(View.OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(ActionBarOverlayLayout.this.mContentMask, "alpha", 0.0f, 1.0f);
            this.mShowAnimator = objectAnimatorOfFloat;
            objectAnimatorOfFloat.addListener(this);
            ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(ActionBarOverlayLayout.this.mContentMask, "alpha", 1.0f, 0.0f);
            this.mHideAnimator = objectAnimatorOfFloat2;
            objectAnimatorOfFloat2.addListener(this);
            if (miuix.internal.util.DeviceHelper.isFeatureWholeAnim()) {
                return;
            }
            this.mShowAnimator.setDuration(0L);
            this.mHideAnimator.setDuration(0L);
        }

        public Animator show() {
            return this.mShowAnimator;
        }

        public Animator hide() {
            return this.mHideAnimator;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (ActionBarOverlayLayout.this.mContentMask == null || ActionBarOverlayLayout.this.mActionBarBottom == null || animator != this.mShowAnimator) {
                return;
            }
            ActionBarOverlayLayout.this.mContentMask.setVisibility(0);
            ActionBarOverlayLayout.this.mContentMask.bringToFront();
            ActionBarOverlayLayout.this.mActionBarBottom.bringToFront();
            ActionBarOverlayLayout.this.mContentMask.setOnClickListener(this.mOnClickListener);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ActionBarOverlayLayout.this.mContentMask == null || ActionBarOverlayLayout.this.mActionBarBottom == null || ActionBarOverlayLayout.this.mContentMask.getAlpha() != 0.0f) {
                return;
            }
            ActionBarOverlayLayout.this.mActionBarBottom.bringToFront();
            ActionBarOverlayLayout.this.mContentMask.setOnClickListener(null);
            ActionBarOverlayLayout.this.mContentMask.setVisibility(8);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (ActionBarOverlayLayout.this.mContentMask == null || ActionBarOverlayLayout.this.mActionBarBottom == null || animator != this.mHideAnimator) {
                return;
            }
            ActionBarOverlayLayout.this.mActionBarBottom.bringToFront();
            ActionBarOverlayLayout.this.mContentMask.setOnClickListener(null);
        }
    }

    class ContextMenuCallback implements MenuBuilder.Callback, MenuPresenter.Callback {
        private MenuDialogHelper mSubMenuHelper;

        @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
        public void onMenuModeChange(MenuBuilder menuBuilder) {
        }

        private ContextMenuCallback() {
        }

        public void onCloseSubMenu(MenuBuilder menuBuilder) {
            if (ActionBarOverlayLayout.this.mCallback != null) {
                ActionBarOverlayLayout.this.mCallback.onPanelClosed(6, menuBuilder.getRootMenu());
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // miuix.appcompat.internal.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            if (menuBuilder.getRootMenu() != menuBuilder) {
                onCloseSubMenu(menuBuilder);
            }
            if (z) {
                if (ActionBarOverlayLayout.this.mCallback != null) {
                    ActionBarOverlayLayout.this.mCallback.onPanelClosed(6, menuBuilder);
                }
                ActionBarOverlayLayout.this.dismissContextMenu();
                MenuDialogHelper menuDialogHelper = this.mSubMenuHelper;
                if (menuDialogHelper != null) {
                    menuDialogHelper.dismiss();
                    this.mSubMenuHelper = null;
                }
            }
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            if (menuBuilder == null) {
                return false;
            }
            menuBuilder.setCallback(this);
            MenuDialogHelper menuDialogHelper = new MenuDialogHelper(menuBuilder);
            this.mSubMenuHelper = menuDialogHelper;
            menuDialogHelper.show(null);
            return true;
        }

        @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            if (ActionBarOverlayLayout.this.mCallback != null) {
                return ActionBarOverlayLayout.this.mCallback.onMenuItemSelected(6, menuItem);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissContextMenu() {
        MenuDialogHelper menuDialogHelper = this.mContextMenuHelper;
        if (menuDialogHelper != null) {
            menuDialogHelper.dismiss();
            this.mContextMenuHelper = null;
            this.mContextMenu = null;
        }
    }

    public void setAnimating(boolean z) {
        this.mAnimating = z;
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0029  */
    @Override // androidx.core.view.NestedScrollingParent3
    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
        int i6;
        ActionBar actionBar;
        if (getAdjustView(view) == null) {
            return;
        }
        if (i4 < 0) {
            int i7 = iArr[1];
            if (i4 - i7 > 0 || (actionBar = this.mActionBar) == null || !(actionBar instanceof ActionBarImpl)) {
                i6 = i4;
            } else {
                int iUpdateTopOffsetOnNestedScroll = ((ActionBarImpl) actionBar).updateTopOffsetOnNestedScroll(view, i4 - i7);
                iArr[1] = iArr[1] + iUpdateTopOffsetOnNestedScroll;
                i6 = i4 - iUpdateTopOffsetOnNestedScroll;
            }
        } else {
            i6 = i4;
        }
        int[] iArr2 = this.mOffsetInWindow;
        iArr2[1] = 0;
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer != null && !this.mIsInSearchMode) {
            actionBarContainer.onNestedScroll(view, i, i2, i3, i6, i5, iArr, iArr2);
        }
        applyNestedScrollMotionEventAdjustment();
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        ActionBarContainer actionBarContainer;
        if (!this.mNestedScrollingParentEnabled) {
            return false;
        }
        if (!this.mPostScroller.isFinished()) {
            this.mPostScroller.forceFinished(true);
        }
        return (getAdjustView(view2) == null || (actionBarContainer = this.mActionBarTop) == null || !actionBarContainer.onStartNestedScroll(view, view2, i, i2)) ? false : true;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        ActionBarContainer actionBarContainer;
        if (!this.mPostScroller.isFinished()) {
            this.mPostScroller.forceFinished(true);
        }
        if (getAdjustView(view2) == null || (actionBarContainer = this.mActionBarTop) == null) {
            return;
        }
        actionBarContainer.onNestedScrollAccepted(view, view2, i, i2);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onStopNestedScroll(View view, int i) {
        ActionBarContainer actionBarContainer;
        ActionBar actionBar;
        if (getAdjustView(view) == null || (actionBarContainer = this.mActionBarTop) == null) {
            return;
        }
        actionBarContainer.onStopNestedScroll(view, i);
        if (isInOverlayMode() && this.mActionBarView != null && (actionBar = this.mActionBar) != null && actionBar.isAdsorptionToNoOverlay() && this.mActionBarView.getExpandState() == 0) {
            this.mPostScrollTarget = view;
            if (!this.mPostScroller.isFinished()) {
                this.mPostScroller.forceFinished(true);
            }
            int topOffsetForCoordinateView = ((ActionBarImpl) this.mActionBar).getTopOffsetForCoordinateView(this.mPostScrollTarget);
            int collapsedHeight = this.mActionBarView.getCollapsedHeight() + this.mActionBarView.getTop();
            if (topOffsetForCoordinateView != collapsedHeight) {
                this.mPostScroller.startScroll(0, topOffsetForCoordinateView, 0, collapsedHeight - topOffsetForCoordinateView);
                postOnAnimation(this.mPostScroll);
            }
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
        if (!this.mPostScroller.isFinished()) {
            this.mPostScroller.forceFinished(true);
        }
        removeCallbacks(this.mPostScroll);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
        View view2;
        int i4;
        int[] iArr2;
        ActionBar actionBar;
        if (!this.mPostScroller.isFinished()) {
            this.mPostScroller.forceFinished(true);
        }
        removeCallbacks(this.mPostScroll);
        if (getAdjustView(view) == null) {
            return;
        }
        int[] iArr3 = this.mOffsetInWindow;
        iArr3[1] = 0;
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer == null || this.mIsInSearchMode) {
            view2 = view;
            i4 = i2;
            iArr2 = iArr;
        } else {
            view2 = view;
            i4 = i2;
            iArr2 = iArr;
            actionBarContainer.onNestedPreScroll(view2, i, i4, iArr2, i3, iArr3);
        }
        if (i4 > 0) {
            int i5 = iArr2[1];
            if (i4 - i5 > 0 && (actionBar = this.mActionBar) != null && (actionBar instanceof ActionBarImpl)) {
                int i6 = i4 - i5;
                int topOffsetForCoordinateView = ((ActionBarImpl) actionBar).getTopOffsetForCoordinateView(view2);
                if (topOffsetForCoordinateView != -1) {
                    iArr2[1] = iArr2[1] + ((ActionBarImpl) this.mActionBar).updateTopOffsetOnNestedPreScroll(view2, Math.max(0, topOffsetForCoordinateView - i6));
                }
            }
        }
        applyNestedScrollMotionEventAdjustment();
    }

    private View getAdjustView(View view) {
        if (this.mCoordinatedScrollViewSet.isEmpty()) {
            return this.mContentView;
        }
        return this.mCoordinatedScrollViewSet.contains(view) ? view : this.mContentView;
    }

    private void applyNestedScrollMotionEventAdjustment() {
        int i = this.mOffsetInWindow[1];
        if (i != 0) {
            this.mNestedScrollMotionEventYAdjustment += -i;
        }
    }

    private void updateExtraPaddingHorizontal(Context context, ExtraPaddingPolicy extraPaddingPolicy, int i, int i2) {
        Resources resources = context.getResources();
        WindowBaseInfo windowInfo = EnvStateManager.getWindowInfo(context, resources.getConfiguration());
        if (i == -1) {
            i = windowInfo.windowSize.x;
        }
        int i3 = i;
        if (i2 == -1) {
            i2 = windowInfo.windowSize.y;
        }
        float f = resources.getDisplayMetrics().density;
        Point point = windowInfo.windowSizeDp;
        extraPaddingPolicy.onContainerSizeChanged(point.x, point.y, i3, i2, f, isInFloatingMode());
        int extraPaddingDp = extraPaddingPolicy.isEnable() ? (int) (extraPaddingPolicy.getExtraPaddingDp() * f) : 0;
        if (extraPaddingDp != this.mExtraHorizontalPadding) {
            this.mExtraHorizontalPadding = extraPaddingDp;
            this.mShouldExtraPaddingHorizontalNotifyChanged = true;
        }
    }

    public boolean isInFloatingMode() {
        if (this.mIsMiuixFloating) {
            return true;
        }
        WindowInsetsController$InsetsConfig windowInsetsController$InsetsConfig = this.mInsetsConfig;
        return windowInsetsController$InsetsConfig != null && windowInsetsController$InsetsConfig.isFloatingMode;
    }

    private void applyInternalWindowInsets(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
        if (this.mUserInsetsConfig != null) {
            return;
        }
        if (this.mInternalInsetsConfig == null) {
            this.mInternalInsetsConfig = new WindowInsetsController$InsetsConfig();
        }
        this.mInternalInsetsConfig.update(z, z2, z3, z4, z5, z6);
        applyInsetsConfig(this.mInternalInsetsConfig);
    }

    private void applyInsetsConfig(WindowInsetsController$InsetsConfig windowInsetsController$InsetsConfig) {
        if (this.mInsetsConfig == null) {
            this.mInsetsConfig = new WindowInsetsController$InsetsConfig();
        }
        if (this.mInsetsConfig.update(windowInsetsController$InsetsConfig)) {
            requestApplyInsets();
        }
    }

    public boolean isSupportBlur() {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurUiHelper;
        if (miuiBlurUiHelper == null) {
            return false;
        }
        return miuiBlurUiHelper.isSupportBlur();
    }

    @Override // miuix.view.BlurableWidget
    public void setEnableBlur(boolean z) {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurUiHelper;
        if (miuiBlurUiHelper != null) {
            miuiBlurUiHelper.setEnableBlur(z);
        }
    }

    public boolean isEnableBlur() {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurUiHelper;
        if (miuiBlurUiHelper == null) {
            return false;
        }
        return miuiBlurUiHelper.isEnableBlur();
    }

    @Override // miuix.view.BlurableWidget
    public void applyBlur(boolean z) {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurUiHelper;
        if (miuiBlurUiHelper != null) {
            miuiBlurUiHelper.applyBlur(z);
        }
    }

    public boolean isApplyBlur() {
        MiuiBlurUiHelper miuiBlurUiHelper = this.mBlurUiHelper;
        if (miuiBlurUiHelper == null) {
            return false;
        }
        return miuiBlurUiHelper.isApplyBlur();
    }
}
