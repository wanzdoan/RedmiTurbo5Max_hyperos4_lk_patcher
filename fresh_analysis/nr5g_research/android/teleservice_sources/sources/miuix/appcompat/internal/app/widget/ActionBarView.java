package miuix.appcompat.internal.app.widget;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.R;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.CollapsibleActionView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import miuix.animation.Folme;
import miuix.animation.FolmeEase;
import miuix.animation.IHoverStyle;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.ViewProperty;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$bool;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$string;
import miuix.appcompat.R$style;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.ActionBarTransitionListener;
import miuix.appcompat.internal.app.NavigatorSwitchPresenter;
import miuix.appcompat.internal.app.widget.actionbar.CollapseTitle;
import miuix.appcompat.internal.app.widget.actionbar.ExpandTitle;
import miuix.appcompat.internal.app.widget.actionbar.MediumTitle;
import miuix.appcompat.internal.util.ActionBarViewFactory;
import miuix.appcompat.internal.util.FloatingEffectHelper;
import miuix.appcompat.internal.view.ActionBarPolicy;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuItemImpl;
import miuix.appcompat.internal.view.menu.MenuPresenter;
import miuix.appcompat.internal.view.menu.SubMenuBuilder;
import miuix.appcompat.internal.view.menu.action.ActionMenuItem;
import miuix.appcompat.internal.view.menu.action.ActionMenuPresenter;
import miuix.appcompat.internal.view.menu.action.ActionMenuView;
import miuix.appcompat.internal.view.menu.action.EndActionMenuPresenter;
import miuix.appcompat.internal.view.menu.action.EndActionMenuView;
import miuix.appcompat.internal.view.menu.action.HyperActionMenuPresenter;
import miuix.appcompat.internal.view.menu.action.HyperSplitActionMenuPresenter;
import miuix.appcompat.internal.view.menu.action.ResponsiveActionMenuView;
import miuix.container.ExtraPaddingPolicy;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MiuiBlurUtils;
import miuix.core.util.MiuixUIUtils;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.ViewUtils;
import miuix.transition.R$id;
import miuix.view.ActionModeAnimationListener;

/* JADX INFO: loaded from: classes2.dex */
public class ActionBarView extends AbsActionBarView implements ActionModeAnimationListener {
    public static final ViewProperty SELF_BLUR = new ViewProperty("self_blur", 1.0f) { // from class: miuix.appcompat.internal.app.widget.ActionBarView.17
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setTag(R$id.miuix_animation_tag_view_self_blur, Float.valueOf(f));
            int i = (int) f;
            if (MiuiBlurUtils.setSelfBlur(view, i, null, DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY)) {
                return;
            }
            MiuiBlurUtils.setSelfBlur(view, i, null);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            Object tag = view.getTag(R$id.miuix_animation_tag_view_self_blur);
            if (tag == null) {
                return 0.0f;
            }
            return ((Float) tag).floatValue();
        }
    };
    AnimConfig enterAnimConfig;
    AnimConfig exitAnimConfig;
    private boolean mActionBarButtonMaterial;
    private AnimState mActionBarHideState;
    private AnimState mActionBarShowState;
    private boolean mAnimateStart;
    private boolean mApplyBgBlur;
    private OnBackInvokedCallback mBackInvokedCallback;
    private OnBackInvokedDispatcher mBackInvokedDispatcher;
    private TransitionListener mBottomMenuTransitionListener;
    private View mCenterView;
    protected TransitionListener mCollapseAnimHideConfigListener;
    protected TransitionListener mCollapseAnimShowConfigListener;
    private final AbsActionBarView.CollapseView mCollapseController;
    private FrameLayout mCollapseCustomContainer;
    private int mCollapseSecondaryTabHeight;
    private final int mCollapseSubtitleStyleRes;
    private FrameLayout mCollapseTabContainer;
    private CollapseTitle mCollapseTitle;
    private boolean mCollapseTitleShowable;
    private int mCollapseTitleStyleRes;
    int mCollapseTotalHeight;
    private boolean mCollapsedTitleVisible;
    private Context mContext;
    private View mCustomNavView;
    private final TextWatcher mCustomTitleWatcher;
    private float mDensity;
    private int mDisplayOptions;
    private boolean mDoContainerShowAnimInFinishActionMode;
    private int mEndActionMenuItemLimit;
    private ActionMenuPresenter mEndActionMenuPresenter;
    private MenuBuilder mEndMenu;
    private ActionMenuView mEndMenuView;
    private View mEndView;
    private int mExpandSubtitlePaddingBottom;
    private int mExpandTabTopPadding;
    private ExpandTitle mExpandTitle;
    private int mExpandTitlePaddingBottom;
    private boolean mExpandTitleVisible;
    int mExpandTotalHeight;
    View mExpandedActionView;
    private final View.OnClickListener mExpandedActionViewUpListener;
    private HomeView mExpandedHomeLayout;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mExtraPadding;
    private ExtraPaddingPolicy mExtraPaddingPolicy;
    private int mFloatingBgColor;
    public Boolean mFloatingMode;
    private final Handler mHandler;
    private boolean mHasNavigatorSwitchView;
    private boolean mHasStartView;
    private Drawable mHomeAsUpIndicatorDrawable;
    private int mHomeAsUpIndicatorResId;
    private HomeView mHomeLayout;
    private final int mHomeResId;
    private Drawable mIcon;
    private int mIconLogoInitIndicator;
    private boolean mInActionMode;
    private boolean mInActionModeAnimating;
    private boolean mInSearchMode;
    private ProgressBar mIndeterminateProgressView;
    private CharSequence mInitCustomTitle;
    private boolean mIsBottomMenuVisible;
    private boolean mIsCollapseTitleShowingOnResizing;
    private boolean mIsCollapsed;
    private int mItemPadding;
    private float mLastResizingProcess;
    private LifecycleOwner mLifecycleOwner;
    private LinearLayout mListNavLayout;
    private Drawable mLogo;
    private ActionMenuItem mLogoNavItem;
    private FrameLayout mMainContainer;
    private FrameLayout mMediumContainer;
    private final AbsActionBarView.CollapseView mMediumController;
    private int mMediumSubtitleStyleRes;
    private MediumTitle mMediumTitle;
    private int mMediumTitlePaddingStartNoUp;
    private int mMediumTitleStyleRes;
    private AnimConfig mMenuAnimConfig;
    protected TransitionListener mMovableAlphaShowListener;
    protected TransitionListener mMovableAnimAlphaListener;
    private final AbsActionBarView.CollapseView mMovableController;
    private FrameLayout mMovableMainContainer;
    private int mMovableSecondaryTabHeight;
    private FrameLayout mMovableTabContainer;
    private final AdapterView.OnItemSelectedListener mNavItemSelectedListener;
    private int mNavigationMode;
    private View mNavigatorSwitch;
    private final int mNavigatorSwitchResId;
    private boolean mNeedRequestLayoutOnExpandTitleShowing;
    private boolean mNonTouchScrolling;
    private boolean mOptionalIconsVisible;
    private MenuBuilder mOptionsMenu;
    private boolean mPendingCreated;
    private int mPendingHeight;
    private final Runnable mPostAnimationRunnable;
    private Runnable mPostScroll;
    private final Scroller mPostScroller;
    private int mProgressBarPadding;
    private ProgressBar mProgressView;
    private Runnable mScheduleBottomMenuRunnable;
    private View mSearchView;
    private int mSearchViewMargin;
    private int mSearchViewMaxWidth;
    private int mSecondaryTabVerticalPadding;
    private Spinner mSpinner;
    private View mStartView;
    private IStateStyle mStateChangeAnimStateStyle;
    private final View.OnClickListener mSubTitleClickListener;
    private CharSequence mSubtitle;
    private boolean mTabsExit;
    private CharSequence mTitle;
    private boolean mTitleCenter;
    private final View.OnClickListener mTitleClickListener;
    private int mTitleGapPaddingStart;
    private int mTitleMinWidth;
    private ActionMenuItem mTitleNavItem;
    private View mTitleUpView;
    private int mTitleUpViewMarginEnd;
    private int mTitleUpViewMarginStart;
    private boolean mTouchScrolling;
    private int mTransitionTarget;
    private int mUncollapsePaddingH;
    private int mUncollapseTabPaddingH;
    private final View.OnClickListener mUpClickListener;
    private boolean mUserSetEndActionMenuItemLimit;
    private boolean mUserTitle;
    Window.Callback mWindowCallback;
    private View.OnLayoutChangeListener mWindowWidthWatcher;

    private void addSecondaryTabsToCollapseTabContainers() {
    }

    private void addSecondaryTabsToExpandTabContainers() {
    }

    private void ensureMainContainerVisibleForMedium() {
    }

    private int normalizeHorizontalGravity(int i, boolean z) {
        int i2 = 8388615 & i;
        if ((i & 8388608) == 0) {
            if (i2 == 3) {
                return z ? 8388613 : 8388611;
            }
            if (i2 == 5) {
                return z ? 8388611 : 8388613;
            }
        }
        return i2;
    }

    private void updateTabsLayoutParams() {
    }

    public void setCollapsable(boolean z) {
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    static /* synthetic */ ScrollingTabContainerView access$2400(ActionBarView actionBarView) {
        actionBarView.getClass();
        return null;
    }

    static /* synthetic */ ScrollingTabContainerView access$2500(ActionBarView actionBarView) {
        actionBarView.getClass();
        return null;
    }

    static /* synthetic */ SecondaryTabBar access$2600(ActionBarView actionBarView) {
        actionBarView.getClass();
        return null;
    }

    static /* synthetic */ SecondaryTabBar access$2700(ActionBarView actionBarView) {
        actionBarView.getClass();
        return null;
    }

    static /* synthetic */ ActionBar.OnNavigationListener access$500(ActionBarView actionBarView) {
        actionBarView.getClass();
        return null;
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ ActionMenuView getActionMenuView() {
        return super.getActionMenuView();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ int getExpandState() {
        return super.getExpandState();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ boolean hideOverflowMenu() {
        return super.hideOverflowMenu();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ boolean isOverflowMenuShowing() {
        return super.isOverflowMenuShowing();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ boolean isOverflowReserved() {
        return super.isOverflowReserved();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ boolean isResizable() {
        return super.isResizable();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ boolean isUserSetExpandState() {
        return super.isUserSetExpandState();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void postShowOverflowMenu() {
        super.postShowOverflowMenu();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setBottomMenuMode(int i) {
        super.setBottomMenuMode(i);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setExpandState(int i) {
        super.setExpandState(i);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setPendingInset(Rect rect) {
        super.setPendingInset(rect);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setResizable(boolean z) {
        super.setResizable(z);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setSplitView(ActionBarContainer actionBarContainer) {
        super.setSplitView(actionBarContainer);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setSplitWhenNarrow(boolean z) {
        super.setSplitWhenNarrow(z);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView, android.view.View
    public /* bridge */ /* synthetic */ void setVisibility(int i) {
        super.setVisibility(i);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ boolean showOverflowMenu() {
        return super.showOverflowMenu();
    }

    public ActionBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDisplayOptions = -1;
        this.mLifecycleOwner = null;
        this.mDoContainerShowAnimInFinishActionMode = false;
        this.mIsBottomMenuVisible = true;
        this.mPendingCreated = false;
        this.mCollapsedTitleVisible = true;
        this.mExpandTitleVisible = true;
        this.mHasStartView = false;
        this.mHasNavigatorSwitchView = false;
        this.mApplyBgBlur = false;
        this.mActionBarButtonMaterial = false;
        this.mCollapseTitleShowable = true;
        this.mLastResizingProcess = 0.0f;
        this.mNeedRequestLayoutOnExpandTitleShowing = false;
        this.mHandler = new Handler(Looper.myLooper());
        this.mPostAnimationRunnable = new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.1
            @Override // java.lang.Runnable
            public void run() {
                if (ActionBarView.this.mPostScroll != null) {
                    ActionBarView actionBarView = ActionBarView.this;
                    actionBarView.postOnAnimation(actionBarView.mPostScroll);
                }
            }
        };
        this.mCollapseAnimShowConfigListener = new TransitionListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.2
            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                super.onBegin(obj);
                if (ActionBarView.this.mCollapseController != null) {
                    ActionBarView.this.mCollapseController.onShow();
                }
            }
        };
        this.mCollapseAnimHideConfigListener = new TransitionListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.3
            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                if (ActionBarView.this.mCollapseController != null) {
                    ActionBarView.this.mCollapseController.onHide();
                }
            }
        };
        this.mMovableAlphaShowListener = new TransitionListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.4
            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj, Collection collection) {
                super.onBegin(obj, collection);
                if (ActionBarView.this.mMovableMainContainer == null || ActionBarView.this.mMovableMainContainer.getVisibility() == 0) {
                    return;
                }
                ActionBarView.this.mMovableController.setVisibility(0);
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection collection) {
                super.onUpdate(obj, collection);
                if (ActionBarView.this.mNeedRequestLayoutOnExpandTitleShowing) {
                    ActionBarView.this.requestLayout();
                }
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                if (ActionBarView.this.mNeedRequestLayoutOnExpandTitleShowing) {
                    ActionBarView.this.requestLayout();
                }
                ActionBarView.this.mNeedRequestLayoutOnExpandTitleShowing = false;
            }
        };
        this.mMovableAnimAlphaListener = new TransitionListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.5
            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj, Collection collection) {
                super.onBegin(obj, collection);
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                float alpha = ActionBarView.this.mMovableMainContainer.getAlpha();
                ActionBarView actionBarView = ActionBarView.this;
                if (alpha != 0.0f) {
                    if (actionBarView.mMovableMainContainer.getVisibility() != 0) {
                        ActionBarView.this.mMovableController.setVisibility(0);
                        return;
                    }
                    return;
                }
                int i = actionBarView.mInnerExpandState;
                if (i == 0) {
                    if (actionBarView.mMovableMainContainer.getVisibility() != 8) {
                        ActionBarView.this.mMovableController.setVisibility(8);
                    }
                } else {
                    if (i != 1) {
                        if (i == 3 && actionBarView.mMovableMainContainer.getVisibility() != 4) {
                            ActionBarView.this.mMovableController.setVisibility(4);
                            return;
                        }
                        return;
                    }
                    if (actionBarView.mMovableMainContainer.getVisibility() != 8) {
                        ActionBarView.this.mMovableController.setVisibility(8);
                    }
                }
            }
        };
        this.mNavItemSelectedListener = new AdapterView.OnItemSelectedListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.6
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
                ActionBarView.access$500(ActionBarView.this);
            }
        };
        this.mExpandedActionViewUpListener = new View.OnClickListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MenuItemImpl menuItemImpl = ActionBarView.this.mExpandedMenuPresenter.mCurrentExpandedItem;
                if (menuItemImpl != null) {
                    menuItemImpl.collapseActionView();
                }
            }
        };
        this.mUpClickListener = new View.OnClickListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ActionBarView actionBarView = ActionBarView.this;
                actionBarView.mWindowCallback.onMenuItemSelected(0, actionBarView.mLogoNavItem);
            }
        };
        this.mTitleClickListener = new View.OnClickListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ActionBarView actionBarView = ActionBarView.this;
                actionBarView.mWindowCallback.onMenuItemSelected(0, actionBarView.mTitleNavItem);
            }
        };
        this.mSubTitleClickListener = new View.OnClickListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                View.OnClickListener onClickListener = ActionBarView.this.mUserSubTitleClickListener;
                if (onClickListener != null) {
                    onClickListener.onClick(view);
                }
            }
        };
        this.mCustomTitleWatcher = new TextWatcher() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.11
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!charSequence.equals(ActionBarView.this.mTitle)) {
                    ActionBarView.this.mInitCustomTitle = charSequence;
                }
                if (ActionBarView.this.mExpandTitle != null) {
                    ActionBarView.this.mExpandTitle.setTitle(charSequence);
                }
            }
        };
        this.mIsCollapseTitleShowingOnResizing = false;
        this.mTransitionTarget = 0;
        AbsActionBarView.CollapseView collapseView = new AbsActionBarView.CollapseView();
        this.mCollapseController = collapseView;
        AbsActionBarView.CollapseView collapseView2 = new AbsActionBarView.CollapseView();
        this.mMovableController = collapseView2;
        AbsActionBarView.CollapseView collapseView3 = new AbsActionBarView.CollapseView();
        this.mMediumController = collapseView3;
        this.mTouchScrolling = false;
        this.mNonTouchScrolling = false;
        this.mInActionMode = false;
        this.mInSearchMode = false;
        this.mInActionModeAnimating = false;
        this.mStateChangeAnimStateStyle = null;
        this.mPostScroll = new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.16
            @Override // java.lang.Runnable
            public void run() {
                if (ActionBarView.this.mPostScroller.computeScrollOffset()) {
                    ActionBarView actionBarView = ActionBarView.this;
                    int currY = actionBarView.mPostScroller.getCurrY();
                    ActionBarView actionBarView2 = ActionBarView.this;
                    actionBarView.mPendingHeight = (currY - actionBarView2.mCollapseTotalHeight) + actionBarView2.mCollapseSecondaryTabHeight;
                    ActionBarView.this.requestLayout();
                    boolean zIsFinished = ActionBarView.this.mPostScroller.isFinished();
                    ActionBarView actionBarView3 = ActionBarView.this;
                    if (zIsFinished) {
                        int currY2 = actionBarView3.mPostScroller.getCurrY();
                        ActionBarView actionBarView4 = ActionBarView.this;
                        if (currY2 != actionBarView4.mCollapseTotalHeight) {
                            int currY3 = actionBarView4.mPostScroller.getCurrY();
                            ActionBarView actionBarView5 = ActionBarView.this;
                            if (currY3 == actionBarView5.mCollapseTotalHeight + actionBarView5.mMovableMainContainer.getMeasuredHeight()) {
                                ActionBarView.this.setExpandState(2);
                                return;
                            }
                            return;
                        }
                        actionBarView4.setExpandState(0);
                        return;
                    }
                    actionBarView3.postOnAnimation(this);
                }
            }
        };
        AnimConfig ease = new AnimConfig().setEase(FolmeEase.springLegacy(400.0f, 0.98f, 0.75f));
        ViewProperty viewProperty = SELF_BLUR;
        AnimConfig special = ease.setSpecial(viewProperty, FolmeEase.linear(150L), new float[0]);
        ViewProperty viewProperty2 = ViewProperty.ALPHA;
        this.enterAnimConfig = special.setSpecial(viewProperty2, FolmeEase.bezier(isSelfBlurEnabled() ? 150L : 80L, 0.0f, 0.0f, 0.67f, 1.0f), new float[0]);
        this.exitAnimConfig = new AnimConfig().setEase(FolmeEase.springLegacy(400.0f, 0.98f, 0.75f)).setSpecial(viewProperty, FolmeEase.linear(120L), new float[0]).setSpecial(viewProperty2, FolmeEase.bezier(isSelfBlurEnabled() ? 150L : 80L, 0.26f, 0.0f, 0.6f, 0.2f), new float[0]);
        this.mFloatingMode = null;
        this.mFloatingBgColor = -1;
        this.mContext = context;
        this.mPostScroller = new Scroller(context);
        this.mInActionMode = false;
        this.mInSearchMode = false;
        Resources resources = this.mContext.getResources();
        this.mDensity = resources.getDisplayMetrics().density;
        this.mUncollapsePaddingH = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_horizontal_padding);
        this.mUncollapseTabPaddingH = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_tab_horizontal_padding);
        this.mExpandTabTopPadding = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_top_padding);
        this.mExpandTitlePaddingBottom = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_bottom_padding);
        this.mExpandSubtitlePaddingBottom = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_subtitle_bottom_padding);
        this.mSecondaryTabVerticalPadding = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_secondary_tab_vertical_padding);
        this.mTitleUpViewMarginStart = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_up_view_margin_start);
        this.mTitleUpViewMarginEnd = 0;
        this.mTitleGapPaddingStart = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_padding_gap);
        this.mMediumTitlePaddingStartNoUp = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_medium_title_padding_start_no_up);
        this.mSearchViewMaxWidth = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_search_view_max_width);
        this.mSearchViewMargin = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_search_view_margin);
        this.mTitleMinWidth = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_min_width);
        this.mMovableAnimShowConfig.addListeners(this.mMovableAlphaShowListener);
        this.mMovableAnimNormalConfig.addListeners(this.mMovableAnimAlphaListener);
        this.mCollapseAnimShowConfig.addListeners(this.mCollapseAnimShowConfigListener);
        this.mCollapseAnimHideConfig.addListeners(this.mCollapseAnimHideConfigListener);
        FrameLayout frameLayout = new FrameLayout(context);
        this.mMainContainer = frameLayout;
        frameLayout.setId(miuix.appcompat.R$id.action_bar_collapse_container);
        this.mMainContainer.setForegroundGravity(17);
        this.mMainContainer.setVisibility(0);
        this.mMainContainer.setAlpha(this.mInnerExpandState == 0 ? 1.0f : 0.0f);
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.mMediumContainer = frameLayout2;
        frameLayout2.setId(miuix.appcompat.R$id.action_bar_medium_container);
        this.mMediumContainer.setForegroundGravity(8388611);
        this.mMediumContainer.setVisibility(0);
        this.mMediumContainer.setAlpha(this.mInnerExpandState == 1 ? 1.0f : 0.0f);
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.mMovableMainContainer = frameLayout3;
        frameLayout3.setId(miuix.appcompat.R$id.action_bar_movable_container);
        FrameLayout frameLayout4 = this.mMovableMainContainer;
        int i = this.mUncollapsePaddingH;
        frameLayout4.setPaddingRelative(i, this.mExpandTabTopPadding, i, this.mExpandTitlePaddingBottom);
        this.mMovableMainContainer.setVisibility(0);
        FrameLayout frameLayout5 = this.mMovableMainContainer;
        int i2 = this.mInnerExpandState;
        frameLayout5.setAlpha((i2 == 0 || i2 == 1) ? 0.0f : 1.0f);
        collapseView.attachViews(this.mMainContainer);
        collapseView3.attachViews(this.mMediumContainer);
        collapseView2.attachViews(this.mMovableMainContainer);
        setBackgroundResource(0);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ActionBar, R.attr.actionBarStyle, 0);
        this.mNavigationMode = typedArrayObtainStyledAttributes.getInt(R$styleable.ActionBar_android_navigationMode, 0);
        this.mTitle = typedArrayObtainStyledAttributes.getText(R$styleable.ActionBar_android_title);
        this.mSubtitle = typedArrayObtainStyledAttributes.getText(R$styleable.ActionBar_android_subtitle);
        this.mTitleCenter = typedArrayObtainStyledAttributes.getBoolean(R$styleable.ActionBar_titleCenter, false);
        this.mLogo = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ActionBar_android_logo);
        this.mIcon = typedArrayObtainStyledAttributes.getDrawable(R$styleable.ActionBar_android_icon);
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(context);
        this.mNavigatorSwitchResId = typedArrayObtainStyledAttributes.getResourceId(R$styleable.ActionBar_navigatorSwitchLayout, R$layout.miuix_appcompat_action_bar_navigator_switch);
        this.mHomeResId = typedArrayObtainStyledAttributes.getResourceId(R$styleable.ActionBar_android_homeLayout, R$layout.miuix_appcompat_action_bar_home);
        this.mCollapseTitleStyleRes = typedArrayObtainStyledAttributes.getResourceId(R$styleable.ActionBar_android_titleTextStyle, 0);
        this.mCollapseSubtitleStyleRes = typedArrayObtainStyledAttributes.getResourceId(R$styleable.ActionBar_android_subtitleTextStyle, 0);
        this.mMediumTitleStyleRes = typedArrayObtainStyledAttributes.getResourceId(R$styleable.ActionBar_mediumTitleTextStyle, R$style.MediumTitleStyle);
        this.mMediumSubtitleStyleRes = typedArrayObtainStyledAttributes.getResourceId(R$styleable.ActionBar_mediumSubtitleTextStyle, R$style.MediumSubtitleStyle);
        this.mProgressBarPadding = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R$styleable.ActionBar_android_progressBarPadding, 0);
        this.mItemPadding = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R$styleable.ActionBar_android_itemPadding, 0);
        setDisplayOptions(typedArrayObtainStyledAttributes.getInt(R$styleable.ActionBar_android_displayOptions, 0));
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(R$styleable.ActionBar_android_customNavigationLayout, 0);
        if (resourceId != 0) {
            View viewInflate = layoutInflaterFrom.inflate(resourceId, (ViewGroup) this, false);
            this.mCustomNavView = viewInflate;
            viewInflate.setLayoutParams(new ActionBar.LayoutParams(-1, -2, 8388627));
            this.mNavigationMode = 0;
        }
        this.mTitleMinHeight = typedArrayObtainStyledAttributes.getLayoutDimension(R$styleable.ActionBar_android_minHeight, 0);
        this.mTitleMaxHeight = typedArrayObtainStyledAttributes.getLayoutDimension(R$styleable.ActionBar_android_maxHeight, 0);
        this.mTitleMaxHeight = (AttributeResolver.resolveBoolean(this.mContext, R$attr.actionBarTitleAdaptLargeFont, true) && (MiuixUIUtils.getFontLevel(this.mContext) == 2)) ? typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.ActionBar_actionBarMaxSizeInLargeFont, this.mContext.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_action_bar_large_font_max_height)) : this.mTitleMaxHeight;
        this.mOptionalIconsVisible = typedArrayObtainStyledAttributes.getBoolean(R$styleable.ActionBar_showOptionIcons, false);
        typedArrayObtainStyledAttributes.recycle();
        this.mActionBarButtonMaterial = AttributeResolver.resolveBoolean(context, R$attr.actionBarButtonMaterial, false);
        this.mLogoNavItem = new ActionMenuItem(context, 0, R.id.home, 0, 0, this.mTitle);
        this.mTitleNavItem = new ActionMenuItem(context, 0, R.id.title, 0, 0, this.mTitle);
        postRefreshTitleControllerStatus();
    }

    private void postRefreshTitleControllerStatus() {
        post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarView$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ActionBarView.$r8$lambda$04kpj5IDuE0qXTIj9b55WOW81uI(this.f$0);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$04kpj5IDuE0qXTIj9b55WOW81uI(ActionBarView actionBarView) {
        int i = actionBarView.mInnerExpandState;
        if (i == 0) {
            actionBarView.mCollapseController.setAnimFrom(1.0f, 0, 0, true);
            actionBarView.mMediumController.setAnimFrom(0.0f, 0, 0, true);
            actionBarView.mMovableController.setAnimFrom(0.0f, 0, 0, true);
            return;
        }
        if (i != 1) {
            if (i == 2) {
                actionBarView.mCollapseController.setAnimFrom(0.0f, 0, 20, true);
                actionBarView.mMediumController.setAnimFrom(0.0f, 0, 0, true);
                actionBarView.mMovableController.setAnimFrom(1.0f, 0, 0, true);
                return;
            }
            return;
        }
        boolean zHasTabsInContainer = actionBarView.hasTabsInContainer(actionBarView.mMainContainer);
        AbsActionBarView.CollapseView collapseView = actionBarView.mCollapseController;
        if (zHasTabsInContainer) {
            collapseView.setAnimFrom(1.0f, 0, 0, true);
            actionBarView.mMediumController.setAnimFrom(0.0f, 0, 0, true);
        } else {
            collapseView.setAnimFrom(0.0f, 0, 0, true);
            actionBarView.mMediumController.setAnimFrom(1.0f, 0, 0, true);
            actionBarView.ensureCollapseTabContainerVisibleForMedium();
        }
        actionBarView.mMovableController.setAnimFrom(0.0f, 0, 0, true);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView, android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(null, R$styleable.ActionBar, getActionBarStyle(), 0);
        this.mTitleMinHeight = typedArrayObtainStyledAttributes.getLayoutDimension(R$styleable.ActionBar_android_minHeight, 0);
        this.mTitleMaxHeight = typedArrayObtainStyledAttributes.getLayoutDimension(R$styleable.ActionBar_android_maxHeight, 0);
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.ActionBar_actionBarMaxSizeInLargeFont, this.mContext.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_action_bar_large_font_max_height));
        boolean zResolveBoolean = AttributeResolver.resolveBoolean(this.mContext, R$attr.actionBarTitleAdaptLargeFont, true);
        boolean z = MiuixUIUtils.getFontLevel(this.mContext) == 2;
        if (!zResolveBoolean || !z) {
            dimensionPixelSize = this.mTitleMaxHeight;
        }
        this.mTitleMaxHeight = dimensionPixelSize;
        typedArrayObtainStyledAttributes.recycle();
        Configuration configuration2 = getResources().getConfiguration();
        this.mCollapseTitleShowable = true;
        updateTightTitle();
        if ((getDisplayOptions() & 8) != 0) {
            CollapseTitle collapseTitle = this.mCollapseTitle;
            if (collapseTitle != null) {
                collapseTitle.onConfigurationChanged(configuration2);
            }
            ExpandTitle expandTitle = this.mExpandTitle;
            if (expandTitle != null) {
                expandTitle.onConfigurationChanged(configuration2);
            }
            MediumTitle mediumTitle = this.mMediumTitle;
            if (mediumTitle != null) {
                mediumTitle.onConfigurationChanged(configuration2);
            }
        }
        Resources resources = this.mContext.getResources();
        float f = resources.getDisplayMetrics().density;
        if (f != this.mDensity) {
            this.mDensity = f;
            this.mUncollapseTabPaddingH = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_tab_horizontal_padding);
            this.mExpandTabTopPadding = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_top_padding);
            this.mExpandTitlePaddingBottom = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_bottom_padding);
            this.mExpandSubtitlePaddingBottom = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_subtitle_bottom_padding);
            this.mTitleUpViewMarginStart = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_up_view_margin_start);
            this.mTitleUpViewMarginEnd = 0;
            this.mTitleGapPaddingStart = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_padding_gap);
            this.mMediumTitlePaddingStartNoUp = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_medium_title_padding_start_no_up);
        }
        this.mUncollapsePaddingH = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_horizontal_padding);
        this.mMovableMainContainer.setPaddingRelative(this.mUncollapsePaddingH, getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_top_padding), this.mUncollapsePaddingH, TextUtils.isEmpty(this.mSubtitle) ? this.mExpandTitlePaddingBottom : this.mExpandSubtitlePaddingBottom);
        this.mSecondaryTabVerticalPadding = resources.getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_secondary_tab_vertical_padding);
        FrameLayout frameLayout = this.mCollapseTabContainer;
        if (frameLayout != null) {
            frameLayout.setPaddingRelative(frameLayout.getPaddingStart(), this.mCollapseTabContainer.getPaddingTop(), this.mCollapseTabContainer.getPaddingEnd(), this.mSecondaryTabVerticalPadding);
        }
        FrameLayout frameLayout2 = this.mMovableTabContainer;
        if (frameLayout2 != null) {
            frameLayout2.setPaddingRelative(frameLayout2.getPaddingStart(), this.mMovableTabContainer.getPaddingTop(), this.mMovableTabContainer.getPaddingEnd(), this.mSecondaryTabVerticalPadding);
        }
        setPaddingRelative(AttributeResolver.resolveDimensionPixelSize(getContext(), R$attr.actionBarPaddingStart), getPaddingTop(), AttributeResolver.resolveDimensionPixelSize(getContext(), R$attr.actionBarPaddingEnd), getPaddingBottom());
        if (this.mTabsExit) {
            updateTabsLayoutParams();
        }
        post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ActionBarView.$r8$lambda$ADuzlzSkaxUsMj_gi3Z3NBj_ofk(this.f$0);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$ADuzlzSkaxUsMj_gi3Z3NBj_ofk(ActionBarView actionBarView) {
        ActionMenuPresenter actionMenuPresenter = actionBarView.mEndActionMenuPresenter;
        if (actionMenuPresenter == null || !actionMenuPresenter.isOverflowMenuShowing()) {
            return;
        }
        LifecycleOwner lifecycleOwner = actionBarView.mLifecycleOwner;
        if (lifecycleOwner != null ? lifecycleOwner.getLifecycle().getCurrentState().equals(Lifecycle.State.RESUMED) : true) {
            return;
        }
        actionBarView.mEndActionMenuPresenter.hideOverflowMenu(false);
    }

    public void setWindowCallback(Window.Callback callback) {
        this.mWindowCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBackInvokedCallbackState() {
        OnBackInvokedDispatcher onBackInvokedDispatcher;
        OnBackInvokedDispatcher onBackInvokedDispatcherFindOnBackInvokedDispatcher = findOnBackInvokedDispatcher();
        boolean z = hasExpandedActionView() && onBackInvokedDispatcherFindOnBackInvokedDispatcher != null && ViewCompat.isAttachedToWindow(this);
        if (z && this.mBackInvokedDispatcher == null) {
            if (this.mBackInvokedCallback == null) {
                this.mBackInvokedCallback = new OnBackInvokedCallback() { // from class: miuix.appcompat.internal.app.widget.ActionBarView$$ExternalSyntheticLambda2
                    @Override // android.window.OnBackInvokedCallback
                    public final void onBackInvoked() {
                        this.f$0.collapseActionView();
                    }
                };
            }
            onBackInvokedDispatcherFindOnBackInvokedDispatcher.registerOnBackInvokedCallback(AnimState.VIEW_SIZE, this.mBackInvokedCallback);
            this.mBackInvokedDispatcher = onBackInvokedDispatcherFindOnBackInvokedDispatcher;
            return;
        }
        if (z || (onBackInvokedDispatcher = this.mBackInvokedDispatcher) == null) {
            return;
        }
        onBackInvokedDispatcher.unregisterOnBackInvokedCallback(this.mBackInvokedCallback);
        this.mBackInvokedDispatcher = null;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mCollapseController.onAttachedToWindow();
        this.mMediumController.onAttachedToWindow();
        this.mMovableController.onAttachedToWindow();
        updateBackInvokedCallbackState();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.hideOverflowMenu(false);
            this.mActionMenuPresenter.hideSubMenus();
        }
        ActionMenuPresenter actionMenuPresenter2 = this.mEndActionMenuPresenter;
        if (actionMenuPresenter2 != null) {
            actionMenuPresenter2.hideOverflowMenu(false);
            this.mEndActionMenuPresenter.hideSubMenus();
        }
        View.OnLayoutChangeListener onLayoutChangeListener = this.mWindowWidthWatcher;
        if (onLayoutChangeListener != null) {
            removeOnLayoutChangeListener(onLayoutChangeListener);
            this.mWindowWidthWatcher = null;
        }
        this.mCollapseController.onDetachedFromWindow();
        this.mMediumController.onDetachedFromWindow();
        this.mMovableController.onDetachedFromWindow();
        updateBackInvokedCallbackState();
    }

    public void initIndeterminateProgress() {
        ProgressBar progressBar = new ProgressBar(this.mContext, null, R$attr.actionBarIndeterminateProgressStyle);
        this.mIndeterminateProgressView = progressBar;
        progressBar.setId(miuix.appcompat.R$id.progress_circular);
        this.mIndeterminateProgressView.setVisibility(8);
        this.mIndeterminateProgressView.setIndeterminate(true);
        addView(this.mIndeterminateProgressView);
    }

    public void setEndActionMenuEnable(boolean z) {
        this.mEndActionMenuEnable = z;
    }

    public void setHyperActionMenuEnable(boolean z) {
        this.mHyperActionMenuEnable = z;
    }

    public void setHyperSplitMenuEnabled(boolean z) {
        this.mHyperSplitMenuEnabled = z;
    }

    public boolean isEndActionMenuEnable() {
        return this.mEndActionMenuEnable;
    }

    boolean updateExpandStateOnLayout() {
        if (this.mInnerExpandState != 3) {
            return false;
        }
        int i = this.mExpandStateOnLayout;
        int i2 = this.mPendingHeight;
        if (i2 == 0) {
            i = 0;
        } else if (i2 == this.mMovableMainContainer.getMeasuredHeight() + this.mMovableSecondaryTabHeight) {
            i = 2;
        }
        if (this.mExpandStateOnLayout == i) {
            return false;
        }
        this.mExpandStateOnLayout = i;
        this.mExpandStateBeforeResizing = i;
        return true;
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public void setSplitActionBar(boolean z) {
        if (this.mSplitActionBarEnable != z) {
            ActionMenuView actionMenuView = this.mMenuView;
            if (actionMenuView != null) {
                removeMenuViewFromOldParent(actionMenuView);
                if (z) {
                    ActionBarContainer actionBarContainer = this.mSplitView;
                    if (actionBarContainer != null) {
                        actionBarContainer.addView(this.mMenuView);
                    }
                    this.mMenuView.getLayoutParams().width = -1;
                } else {
                    addView(this.mMenuView);
                    this.mMenuView.getLayoutParams().width = -2;
                }
                this.mMenuView.requestLayout();
            }
            ActionBarContainer actionBarContainer2 = this.mSplitView;
            if (actionBarContainer2 != null) {
                actionBarContainer2.setVisibility(z ? 0 : 8);
            }
            ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
            if (actionMenuPresenter != null) {
                if (!z) {
                    actionMenuPresenter.setExpandedActionViewsExclusive(getResources().getBoolean(R$bool.abc_action_bar_expanded_action_views_exclusive));
                } else {
                    actionMenuPresenter.setExpandedActionViewsExclusive(false);
                    this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
                }
            }
            super.setSplitActionBar(z);
        }
    }

    public boolean isSplitActionBar() {
        return this.mSplitActionBarEnable;
    }

    public boolean isTightTitleWithEmbeddedTabs() {
        return this.mTabsExit && ActionBarPolicy.get(this.mContext).isTightTitle();
    }

    public int getCollapsedHeight() {
        return this.mCollapseTotalHeight;
    }

    public int getExpandedHeight() {
        return this.mExpandTotalHeight;
    }

    public void setEmbeddedTabView(ScrollingTabContainerView scrollingTabContainerView, ScrollingTabContainerView scrollingTabContainerView2, SecondaryTabBar secondaryTabBar, SecondaryTabBar secondaryTabBar2) {
        this.mTabsExit = false;
    }

    private void addTabsToMainContainers() {
        FrameLayout frameLayout = this.mCollapseTabContainer;
        if (frameLayout != null) {
            if (frameLayout.getParent() == this) {
                removeView(this.mCollapseTabContainer);
                this.mCollapseController.detachView(this.mCollapseTabContainer);
            }
            this.mCollapseTabContainer.removeAllViews();
            this.mCollapseTabContainer = null;
        }
        FrameLayout frameLayout2 = this.mMovableTabContainer;
        if (frameLayout2 != null) {
            if (frameLayout2.getParent() == this) {
                removeView(this.mMovableTabContainer);
                this.mMovableController.detachView(this.mMovableTabContainer);
            }
            this.mMovableTabContainer.removeAllViews();
            this.mMovableTabContainer = null;
        }
        this.mMainContainer.removeAllViews();
        this.mMovableMainContainer.removeAllViews();
        if (this.mInnerExpandState == 3) {
            setExpandState(this.mExpandState, false, false);
        }
        ensureMainContainerVisibleForMedium();
    }

    public void setMenu(Menu menu, MenuPresenter.Callback callback) {
        MenuBuilder menuBuilder;
        MenuBuilder menuBuilder2 = this.mOptionsMenu;
        if (menuBuilder2 != null) {
            menuBuilder2.removeMenuPresenter(this.mActionMenuPresenter);
            this.mOptionsMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
        }
        MenuBuilder menuBuilder3 = this.mEndMenu;
        if (menuBuilder3 != null) {
            menuBuilder3.removeMenuPresenter(this.mEndActionMenuPresenter);
        }
        removeMenuViewFromOldParent(this.mMenuView);
        removeMenuViewFromOldParent(this.mEndMenuView);
        if (menu == null || (!this.mSplitActionBarEnable && !this.mEndActionMenuEnable)) {
            this.mActionMenuPresenter = null;
            this.mEndActionMenuPresenter = null;
            this.mExpandedMenuPresenter = null;
            return;
        }
        if (this.mEndActionMenuEnable && this.mHyperActionMenuEnable) {
            Pair pairDivideHyperMenuAndSplitMenu = divideHyperMenuAndSplitMenu(menu);
            this.mOptionsMenu = (MenuBuilder) pairDivideHyperMenuAndSplitMenu.first;
            this.mEndMenu = (MenuBuilder) pairDivideHyperMenuAndSplitMenu.second;
        } else {
            Pair pairDivideMenuByGroup = divideMenuByGroup(menu);
            this.mOptionsMenu = (MenuBuilder) pairDivideMenuByGroup.first;
            this.mEndMenu = (MenuBuilder) pairDivideMenuByGroup.second;
        }
        if (this.mSplitActionBarEnable) {
            if (this.mActionMenuPresenter == null) {
                this.mActionMenuPresenter = createActionMenuPresenter(callback, this.mHyperSplitMenuEnabled);
                this.mExpandedMenuPresenter = createExpandedActionViewMenuPresenter();
            }
            MenuBuilder menuBuilder4 = this.mOptionsMenu;
            ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
            if (menuBuilder4 != null) {
                menuBuilder4.addMenuPresenter(actionMenuPresenter);
                this.mOptionsMenu.addMenuPresenter(this.mExpandedMenuPresenter);
                this.mOptionsMenu.setForceShowOptionalIcon(this.mOptionalIconsVisible);
            } else {
                actionMenuPresenter.initForMenu(this.mContext, null);
                this.mExpandedMenuPresenter.initForMenu(this.mContext, null);
            }
            this.mActionMenuPresenter.updateMenuView(true);
            this.mExpandedMenuPresenter.updateMenuView(true);
            addSplitMenuView();
        }
        if (this.mEndActionMenuEnable && (menuBuilder = this.mEndMenu) != null && menuBuilder.size() > 0) {
            if (this.mEndActionMenuPresenter == null) {
                this.mEndActionMenuPresenter = createEndActionMenuPresenter(callback, this.mHyperActionMenuEnable);
            }
            this.mEndMenu.addMenuPresenter(this.mEndActionMenuPresenter);
            this.mEndMenu.setForceShowOptionalIcon(this.mOptionalIconsVisible);
            this.mEndActionMenuPresenter.updateMenuView(true);
            addEndMenuView();
        }
        updateBadgeOnMenuItemViews();
        updateBackInvokedCallbackState();
    }

    public MenuBuilder getEndMenu() {
        return this.mEndMenu;
    }

    private void updateBadgeOnMenuItemViews() {
        ActionMenuPresenter actionMenuPresenter = this.mEndActionMenuPresenter;
        if (actionMenuPresenter == null) {
            return;
        }
        actionMenuPresenter.updateBadgeOnItemViews();
        ActionMenuPresenter actionMenuPresenter2 = this.mEndActionMenuPresenter;
        if (actionMenuPresenter2 instanceof EndActionMenuPresenter) {
            ((EndActionMenuPresenter) actionMenuPresenter2).updateBadgeOnMoreButton();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Pair divideHyperMenuAndSplitMenu(Menu menu) {
        SubMenu subMenu;
        MenuBuilder menuBuilder = (MenuBuilder) menu;
        MenuBuilder menuBuilder2 = new MenuBuilder(this.mContext);
        menuBuilder2.setCallback(menuBuilder.getCallback());
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int size = menu.size() - 1; size >= 0; size--) {
            MenuItemImpl menuItemImpl = (MenuItemImpl) menu.getItem(size);
            if (menuItemImpl.getGroupId() == miuix.appcompat.R$id.miuix_split_action_menu_group) {
                menuBuilder.removeItemAt(size);
                SubMenu subMenu2 = menuItemImpl.getSubMenu();
                if (subMenu2 instanceof SubMenuBuilder) {
                    ((SubMenuBuilder) subMenu2).setParentMenu(menuBuilder2);
                }
                menuItemImpl.setMenu(menuBuilder2);
                arrayList.add(menuItemImpl);
            }
            if ((menuItemImpl.getGroupId() != miuix.appcompat.R$id.miuix_split_action_menu_group || menuItemImpl.getItemId() != miuix.appcompat.R$id.miuix_hyper_split_parent_item) && this.mHyperActionMenuEnable) {
                checkItemsVisibleIsExpected(menuItemImpl, arrayList2);
            }
        }
        if (this.mHyperActionMenuEnable) {
            correctItemsVisible(arrayList2);
        }
        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
            menuBuilder2.add((MenuItemImpl) arrayList.get(size2));
        }
        int size3 = menuBuilder.size() - 1;
        while (true) {
            if (size3 < 0) {
                subMenu = null;
                break;
            }
            MenuItem item = menuBuilder.getItem(size3);
            if (item.getItemId() == miuix.appcompat.R$id.miuix_hyper_split_parent_item) {
                menuBuilder.removeItemAt(size3);
                subMenu = item.getSubMenu();
                break;
            }
            size3--;
        }
        if (this.mHyperSplitMenuEnabled && subMenu != null) {
            ArrayList arrayList3 = new ArrayList();
            for (int i = 0; i < subMenu.size(); i++) {
                MenuItem item2 = subMenu.getItem(i);
                if (item2 instanceof MenuItemImpl) {
                    menuBuilder2.add((MenuItemImpl) item2);
                }
                checkItemsVisibleIsExpected(item2, arrayList3);
            }
            subMenu.clear();
            correctItemsVisible(arrayList3);
        }
        return new Pair(menuBuilder2, menuBuilder);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Pair divideMenuByGroup(Menu menu) {
        SubMenu subMenu;
        MenuBuilder menuBuilder = (MenuBuilder) menu;
        MenuBuilder menuBuilder2 = new MenuBuilder(this.mContext);
        menuBuilder2.setCallback(menuBuilder.getCallback());
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int size = menu.size() - 1; size >= 0; size--) {
            MenuItemImpl menuItemImpl = (MenuItemImpl) menu.getItem(size);
            if (menuItemImpl.getGroupId() == miuix.appcompat.R$id.miuix_action_end_menu_group) {
                menuBuilder.removeItemAt(size);
                SubMenu subMenu2 = menuItemImpl.getSubMenu();
                if (subMenu2 instanceof SubMenuBuilder) {
                    ((SubMenuBuilder) subMenu2).setParentMenu(menuBuilder2);
                }
                menuItemImpl.setMenu(menuBuilder2);
                arrayList.add(menuItemImpl);
            }
            if ((menuItemImpl.getGroupId() != miuix.appcompat.R$id.miuix_split_action_menu_group || menuItemImpl.getItemId() != miuix.appcompat.R$id.miuix_hyper_split_parent_item) && this.mHyperActionMenuEnable) {
                checkItemsVisibleIsExpected(menuItemImpl, arrayList2);
            }
        }
        if (this.mHyperActionMenuEnable) {
            correctItemsVisible(arrayList2);
        }
        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
            menuBuilder2.add((MenuItemImpl) arrayList.get(size2));
        }
        int i = 0;
        while (true) {
            if (i >= menuBuilder.size()) {
                subMenu = null;
                break;
            }
            MenuItem item = menuBuilder.getItem(i);
            if (item.getItemId() == miuix.appcompat.R$id.miuix_hyper_split_parent_item) {
                menuBuilder.removeItemAt(i);
                subMenu = item.getSubMenu();
                break;
            }
            i++;
        }
        if (this.mHyperSplitMenuEnabled && subMenu != null) {
            ArrayList arrayList3 = new ArrayList();
            for (int i2 = 0; i2 < subMenu.size(); i2++) {
                MenuItem item2 = subMenu.getItem(i2);
                if (item2 instanceof MenuItemImpl) {
                    menuBuilder.add((MenuItemImpl) item2);
                }
                checkItemsVisibleIsExpected(item2, arrayList3);
            }
            subMenu.clear();
            correctItemsVisible(arrayList3);
        }
        return new Pair(menuBuilder, menuBuilder2);
    }

    private void checkItemsVisibleIsExpected(MenuItem menuItem, ArrayList arrayList) {
        if (menuItem == null || !menuItem.hasSubMenu()) {
            return;
        }
        if (menuItem.isVisible() != hasAnySubMenuVisibleItem(menuItem.getSubMenu())) {
            arrayList.add(menuItem);
        }
    }

    private boolean hasAnySubMenuVisibleItem(SubMenu subMenu) {
        if (subMenu != null && subMenu.size() != 0) {
            for (int i = 0; i < subMenu.size(); i++) {
                if (subMenu.getItem(i).isVisible()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void correctItemsVisible(ArrayList arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            MenuItem menuItem = (MenuItem) arrayList.get(i);
            if (menuItem != null) {
                menuItem.setVisible(!menuItem.isVisible());
            }
        }
    }

    private void addEndMenuView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -1);
        this.mEndActionMenuPresenter.setExpandedActionViewsExclusive(getResources().getBoolean(R$bool.abc_action_bar_expanded_action_views_exclusive));
        this.mEndActionMenuPresenter.setItemLimit(this.mEndActionMenuItemLimit);
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.gravity = 8388613;
        ActionMenuView actionMenuView = (ActionMenuView) this.mEndActionMenuPresenter.getMenuView(this);
        ViewGroup viewGroup = (ViewGroup) actionMenuView.getParent();
        if (viewGroup != null && viewGroup != this) {
            viewGroup.removeView(actionMenuView);
        }
        addView(actionMenuView, layoutParams);
        this.mEndMenuView = actionMenuView;
        Boolean bool = this.mFloatingMode;
        if (bool == null || !bool.booleanValue()) {
            return;
        }
        ((EndActionMenuView) actionMenuView).setFloatingMode(true, MiuixUIUtils.isLightTheme(getContext()), this.mActionBarButtonMaterial, this.mFloatingBgColor);
    }

    protected void makeMenuViewShowHideWithAnimation(final boolean z) {
        if (z == this.mIsBottomMenuVisible) {
            return;
        }
        if (this.mMenuView == null) {
            scheduleBottomMenuAnimation(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.12
                @Override // java.lang.Runnable
                public void run() {
                    ActionBarView.this.makeMenuViewShowHide(z);
                    ActionMenuView actionMenuView = ActionBarView.this.mMenuView;
                    if (actionMenuView != null) {
                        actionMenuView.setVisibility(0);
                    }
                }
            });
            return;
        }
        this.mIsBottomMenuVisible = z;
        this.mAnimateStart = false;
        if (this.mSplitActionBarEnable) {
            animateMenuViewShowHide(z);
        }
    }

    private void animateMenuViewShowHide(boolean z) {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView == null) {
            return;
        }
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) getParent().getParent();
        int collapsedHeight = actionMenuView.getCollapsedHeight();
        float fApplyDimension = TypedValue.applyDimension(1, 15.0f, getContext().getResources().getDisplayMetrics());
        float f = z ? fApplyDimension : 0.0f;
        if (z) {
            fApplyDimension = 0.0f;
        }
        ensureMenuAnimConfig(actionBarOverlayLayout, collapsedHeight);
        actionMenuView.setTranslationY(f);
        float f2 = z ? 1.0f : 0.0f;
        float f3 = z ? 0.0f : 50.0f;
        int i = z ? 100 : 0;
        actionMenuView.setAlpha(z ? 0.0f : 1.0f);
        this.mMenuAnimConfig.setDelay(i);
        Folme.useAt(actionMenuView).state().to(new AnimState("menu_end_state").add(ViewProperty.TRANSLATION_Y, fApplyDimension).add(ViewProperty.ALPHA, f2).add(SELF_BLUR, f3), this.mMenuAnimConfig);
        if (actionMenuView instanceof ResponsiveActionMenuView) {
            ((ResponsiveActionMenuView) actionMenuView).setHidden(!this.mIsBottomMenuVisible);
        }
    }

    private void ensureMenuAnimConfig(final ActionBarOverlayLayout actionBarOverlayLayout, final int i) {
        if (this.mMenuAnimConfig == null) {
            this.mMenuAnimConfig = new AnimConfig().setEase(-2, 0.82f, 0.3f).setSpecial(ViewProperty.ALPHA, FolmeEase.linear(150L), new float[0]).setSpecial(SELF_BLUR, FolmeEase.linear(150L), new float[0]);
        }
        TransitionListener transitionListener = this.mBottomMenuTransitionListener;
        if (transitionListener != null) {
            this.mMenuAnimConfig.removeListeners(transitionListener);
        }
        AnimConfig animConfig = this.mMenuAnimConfig;
        TransitionListener transitionListener2 = new TransitionListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.13
            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                if (ActionBarView.this.mAnimateStart) {
                    return;
                }
                ActionBarView.this.mAnimateStart = true;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection collection) {
                UpdateInfo updateInfoFindByName = UpdateInfo.findByName(collection, View.TRANSLATION_Y.getName());
                if (updateInfoFindByName == null) {
                    return;
                }
                actionBarOverlayLayout.onMenuStateChanged((int) (i - updateInfoFindByName.getFloatValue()), 0);
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                ActionBarView.this.mAnimateStart = false;
            }
        };
        this.mBottomMenuTransitionListener = transitionListener2;
        animConfig.addListeners(transitionListener2);
    }

    protected void makeMenuViewShowHide(final boolean z) {
        if (this.mSplitActionBarEnable && z != this.mIsBottomMenuVisible) {
            if (this.mMenuView == null) {
                scheduleBottomMenuAnimation(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.14
                    @Override // java.lang.Runnable
                    public void run() {
                        ActionBarView.this.makeMenuViewShowHide(z);
                        ActionMenuView actionMenuView = ActionBarView.this.mMenuView;
                        if (actionMenuView != null) {
                            actionMenuView.setVisibility(0);
                        }
                    }
                });
                return;
            }
            ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) this.mSplitView.getParent();
            int collapsedHeight = this.mMenuView.getCollapsedHeight();
            this.mMenuView.setTranslationY(z ? 0.0f : collapsedHeight);
            if (!z) {
                collapsedHeight = 0;
            }
            actionBarOverlayLayout.animateContentMarginBottomByBottomMenu(collapsedHeight);
            this.mIsBottomMenuVisible = z;
            ActionMenuView actionMenuView = this.mMenuView;
            if (actionMenuView instanceof ResponsiveActionMenuView) {
                ((ResponsiveActionMenuView) actionMenuView).setHidden(!z);
            }
        }
    }

    private void scheduleBottomMenuAnimation(Runnable runnable) {
        this.mScheduleBottomMenuRunnable = runnable;
    }

    private void addSplitMenuView() {
        ActionMenuView actionMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
        this.mMenuView = actionMenuView;
        if (actionMenuView != null && this.mScheduleBottomMenuRunnable != null) {
            actionMenuView.setVisibility(4);
            this.mMenuView.post(this.mScheduleBottomMenuRunnable);
            this.mScheduleBottomMenuRunnable = null;
        }
        boolean z = this.mBottomMenuMode == 3;
        this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
        this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
        FrameLayout.LayoutParams layoutParamsBuildSplitMenuLayoutParams = buildSplitMenuLayoutParams(z);
        ActionBarContainer actionBarContainer = this.mSplitView;
        ActionMenuView actionMenuView2 = this.mMenuView;
        if (actionBarContainer != null) {
            removeMenuViewFromOldParent(actionMenuView2);
            this.mSplitView.onResidentActionMenuViewRemoved(this.mMenuView);
            ActionMenuView actionMenuView3 = this.mMenuView;
            if (actionMenuView3 instanceof ResponsiveActionMenuView) {
                ResponsiveActionMenuView responsiveActionMenuView = (ResponsiveActionMenuView) actionMenuView3;
                responsiveActionMenuView.setSuspendEnabled(z);
                responsiveActionMenuView.setHidden(!this.mIsBottomMenuVisible);
            }
            this.mSplitView.addView(this.mMenuView, 0, layoutParamsBuildSplitMenuLayoutParams);
            this.mSplitView.onResidentActionMenuViewAdded(this.mMenuView);
            View viewFindViewById = this.mMenuView.findViewById(miuix.appcompat.R$id.expanded_menu);
            if (viewFindViewById != null) {
                viewFindViewById.requestLayout();
            }
            requestLayout();
            return;
        }
        actionMenuView2.setLayoutParams(layoutParamsBuildSplitMenuLayoutParams);
    }

    private FrameLayout.LayoutParams buildSplitMenuLayoutParams(boolean z) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        layoutParams.gravity = 1;
        if (z) {
            layoutParams.topMargin = MiuixUIUtils.dp2px(getContext(), 20.0f);
            layoutParams.bottomMargin = MiuixUIUtils.dp2px(getContext(), 24.0f);
        }
        if (this.mPendingInset != null) {
            applySplitMenuInsets(layoutParams, z);
        }
        return layoutParams;
    }

    private void applySplitMenuInsets(FrameLayout.LayoutParams layoutParams, boolean z) {
        if (z) {
            int iDp2px = MiuixUIUtils.dp2px(getContext(), 24.0f);
            int i = this.mPendingInset.bottom;
            if (i < iDp2px) {
                layoutParams.bottomMargin = iDp2px;
            } else {
                layoutParams.bottomMargin = i + MiuixUIUtils.dp2px(getContext(), 8.0f);
            }
            ViewUtils.resetPaddingBottom(this.mMenuView, 0);
            return;
        }
        ViewUtils.resetPaddingBottom(this.mMenuView, this.mPendingInset.bottom);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public void refreshBottomMenu() {
        if (!this.mSplitActionBarEnable || this.mActionMenuPresenter == null) {
            return;
        }
        addSplitMenuView();
    }

    private void removeMenuViewFromOldParent(View view) {
        ViewGroup viewGroup;
        if (view == null || (viewGroup = (ViewGroup) view.getParent()) == null) {
            return;
        }
        viewGroup.removeView(view);
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.mLifecycleOwner = lifecycleOwner;
    }

    public boolean showEndOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter;
        LifecycleOwner lifecycleOwner = this.mLifecycleOwner;
        return (lifecycleOwner != null ? lifecycleOwner.getLifecycle().getCurrentState().equals(Lifecycle.State.RESUMED) : true) && (actionMenuPresenter = this.mEndActionMenuPresenter) != null && this.mEndActionMenuEnable && actionMenuPresenter.showOverflowMenu();
    }

    public void postShowEndOverflowMenu() {
        post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarView$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.showEndOverflowMenu();
            }
        });
    }

    public boolean isEndOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mEndActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
    }

    private boolean shouldMeasureCollapseTabContainer() {
        FrameLayout frameLayout = this.mCollapseTabContainer;
        return (frameLayout == null || frameLayout.getParent() != this || this.mCollapseTabContainer.getChildCount() == 0) ? false : true;
    }

    private boolean shouldMeasureMovableTabContainer() {
        FrameLayout frameLayout = this.mMovableTabContainer;
        return (frameLayout == null || frameLayout.getParent() != this || this.mMovableTabContainer.getChildCount() == 0) ? false : true;
    }

    public boolean hasExpandedActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        return (expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null) ? false : true;
    }

    public void setExtraPaddingPolicy(ExtraPaddingPolicy extraPaddingPolicy) {
        this.mExtraPaddingPolicy = extraPaddingPolicy;
    }

    public void collapseActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        MenuItemImpl menuItemImpl = expandedActionViewMenuPresenter == null ? null : expandedActionViewMenuPresenter.mCurrentExpandedItem;
        if (menuItemImpl != null) {
            menuItemImpl.collapseActionView();
        }
    }

    private TextView getCustomTitleView(View view) {
        if (view != null) {
            return (TextView) view.findViewById(R.id.title);
        }
        return null;
    }

    private boolean hasTabsInContainer(ViewGroup viewGroup) {
        if (viewGroup != null && viewGroup.getChildCount() == 1) {
            viewGroup.getChildAt(0);
        }
        return false;
    }

    private boolean freeMainContainerChildren() {
        if (hasTabsInContainer(this.mMainContainer)) {
            addSecondaryTabsToCollapseTabContainers();
        }
        if (hasTabsInContainer(this.mMovableMainContainer)) {
            addSecondaryTabsToExpandTabContainers();
        }
        this.mMainContainer.removeAllViews();
        this.mMovableMainContainer.removeAllViews();
        return true;
    }

    public void setTitle(CharSequence charSequence) {
        this.mUserTitle = true;
        setTitleImpl(charSequence);
    }

    public void setWindowTitle(CharSequence charSequence) {
        if (this.mUserTitle) {
            return;
        }
        setTitleImpl(charSequence);
    }

    private boolean isAllTitlesEmpty() {
        return TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle);
    }

    private boolean shouldTitleVisible() {
        return (this.mExpandedActionView != null || (this.mDisplayOptions & 8) == 0 || isAllTitlesEmpty()) ? false : true;
    }

    private void setTitleImpl(CharSequence charSequence) {
        boolean zShouldTitleVisible = shouldTitleVisible();
        this.mTitle = charSequence;
        boolean z = false;
        if (((this.mDisplayOptions & 16) == 0 || this.mCustomNavView == null) ? false : updateExpandTitleOnShowCustom()) {
            return;
        }
        updateCollapseTitle();
        updateExpandTitle();
        updateMediumTitle();
        boolean zShouldTitleVisible2 = shouldTitleVisible();
        setTitleVisibility(zShouldTitleVisible2);
        ActionMenuItem actionMenuItem = this.mLogoNavItem;
        if (actionMenuItem != null) {
            actionMenuItem.setTitle(charSequence);
        }
        ActionMenuItem actionMenuItem2 = this.mTitleNavItem;
        if (actionMenuItem2 != null) {
            actionMenuItem2.setTitle(charSequence);
        }
        if (zShouldTitleVisible && !zShouldTitleVisible2) {
            if (getNavigationMode() == 2 || isTightTitleWithEmbeddedTabs()) {
                addTabsToMainContainers();
                return;
            }
            return;
        }
        if (zShouldTitleVisible || !zShouldTitleVisible2) {
            return;
        }
        if (getNavigationMode() == 2 && isTightTitleWithEmbeddedTabs()) {
            return;
        }
        CollapseTitle collapseTitle = this.mCollapseTitle;
        if (collapseTitle != null && collapseTitle.getLayout().getParent() == null) {
            z = true;
        }
        ExpandTitle expandTitle = this.mExpandTitle;
        if ((expandTitle == null || z || expandTitle.getLayout().getParent() != null) ? z : true) {
            freeMainContainerChildren();
            CollapseTitle collapseTitle2 = this.mCollapseTitle;
            if (collapseTitle2 != null) {
                safeAddView(this.mMainContainer, collapseTitle2.getLayout());
            }
            ExpandTitle expandTitle2 = this.mExpandTitle;
            if (expandTitle2 != null) {
                safeAddView(this.mMovableMainContainer, expandTitle2.getLayout());
            }
        }
    }

    private void updateCollapseTitle() {
        CollapseTitle collapseTitle = this.mCollapseTitle;
        if (collapseTitle != null) {
            if (collapseTitle.getTitleVisibility() != 0) {
                this.mCollapseTitle.setTitleVisibility(0);
            }
            this.mCollapseTitle.setTitle(this.mTitle);
            this.mCollapseTitle.setSubTitle(this.mSubtitle);
            post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarView$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ActionBarView.$r8$lambda$NeV4ZctygqB325yNYUBGP1cCtRw(this.f$0);
                }
            });
        }
    }

    public static /* synthetic */ void $r8$lambda$NeV4ZctygqB325yNYUBGP1cCtRw(ActionBarView actionBarView) {
        CollapseTitle collapseTitle = actionBarView.mCollapseTitle;
        if (collapseTitle != null) {
            collapseTitle.setSubTitleTextSize(collapseTitle.getSubtitleAdjustSize());
        }
    }

    private void updateExpandTitle() {
        if (this.mExpandTitle != null) {
            boolean zUpdateExpandTitleOnShowCustom = (!((this.mDisplayOptions & 16) != 0) || this.mCustomNavView == null) ? false : updateExpandTitleOnShowCustom();
            this.mExpandTitle.setTitleVisibility(0);
            if (!zUpdateExpandTitleOnShowCustom) {
                this.mExpandTitle.setTitle(this.mTitle);
            }
            this.mExpandTitle.setSubTitle(this.mSubtitle);
        }
    }

    private void updateMediumTitle() {
        MediumTitle mediumTitle = this.mMediumTitle;
        if (mediumTitle != null) {
            mediumTitle.setTitleVisibility(0);
            this.mMediumTitle.setTitle(this.mTitle);
            this.mMediumTitle.setSubTitle(this.mSubtitle);
            this.mMediumTitle.setVisibility(0);
        }
    }

    private void updateTitleVisibilityByState(int i) {
        boolean zShouldTitleVisible = shouldTitleVisible();
        boolean z = false;
        boolean z2 = i == 0 || i == 3;
        boolean z3 = i == 2 || i == 3;
        boolean z4 = i == 1;
        updateCollapseTitleVisibility(zShouldTitleVisible && z2);
        updateMediumTitleVisibility(zShouldTitleVisible && z4);
        if (zShouldTitleVisible && z3) {
            z = true;
        }
        updateExpandTitleVisibility(z);
    }

    private void updateCollapseTitleVisibility(boolean z) {
        CollapseTitle collapseTitle = this.mCollapseTitle;
        if (collapseTitle != null) {
            collapseTitle.setVisibility(z ? 0 : 4);
        }
    }

    private void updateMediumTitleVisibility(boolean z) {
        MediumTitle mediumTitle = this.mMediumTitle;
        if (mediumTitle != null) {
            mediumTitle.setVisibility(z ? 0 : 8);
        }
    }

    private void updateExpandTitleVisibility(boolean z) {
        ExpandTitle expandTitle = this.mExpandTitle;
        if (expandTitle != null) {
            expandTitle.setVisibility(z ? 0 : 4);
        }
    }

    private boolean updateExpandTitleOnShowCustom() {
        TextView customTitleView = getCustomTitleView((FrameLayout) this.mCustomNavView.findViewById(miuix.appcompat.R$id.action_bar_expand_container));
        if (customTitleView == null) {
            return false;
        }
        if (this.mExpandTitle == null) {
            return true;
        }
        if (!TextUtils.isEmpty(this.mInitCustomTitle)) {
            if (!this.mInitCustomTitle.equals(customTitleView.getText())) {
                customTitleView.removeTextChangedListener(this.mCustomTitleWatcher);
                customTitleView.setText(this.mInitCustomTitle);
                customTitleView.addTextChangedListener(this.mCustomTitleWatcher);
            }
            this.mExpandTitle.setTitle(this.mInitCustomTitle);
        } else {
            this.mExpandTitle.setTitle(this.mTitle);
            customTitleView.removeTextChangedListener(this.mCustomTitleWatcher);
            customTitleView.setText(this.mTitle);
            customTitleView.addTextChangedListener(this.mCustomTitleWatcher);
        }
        if (this.mExpandTitle.getVisibility() != 0) {
            this.mExpandTitle.setVisibility(0);
        }
        this.mExpandTitle.setSubTitleVisibility(8);
        return true;
    }

    public void setHomeButtonEnabled(boolean z) {
        HomeView homeView = this.mHomeLayout;
        if (homeView != null) {
            homeView.setEnabled(z);
            this.mHomeLayout.setFocusable(z);
            if (!z) {
                this.mHomeLayout.setContentDescription(null);
                return;
            }
            int i = this.mDisplayOptions & 4;
            HomeView homeView2 = this.mHomeLayout;
            if (i != 0) {
                homeView2.setContentDescription(this.mContext.getResources().getText(R$string.abc_action_bar_up_description));
            } else {
                homeView2.setContentDescription(this.mContext.getResources().getText(R$string.abc_action_bar_home_description));
            }
        }
    }

    public void setDisplayOptions(int i) {
        View view;
        int i2 = this.mDisplayOptions;
        int i3 = i2 != -1 ? i ^ i2 : -1;
        this.mDisplayOptions = i;
        if ((i3 & 8223) != 0) {
            boolean z = (i & 2) != 0;
            int i4 = 8;
            if (z) {
                initHomeLayout();
                this.mHomeLayout.setVisibility(this.mExpandedActionView == null ? 0 : 8);
                if ((i3 & 4) != 0) {
                    boolean z2 = (i & 4) != 0;
                    this.mHomeLayout.setUp(z2);
                    if (z2) {
                        setHomeButtonEnabled(true);
                    }
                }
                if ((i3 & 1) != 0) {
                    Drawable logo = getLogo();
                    boolean z3 = (logo == null || (i & 1) == 0) ? false : true;
                    HomeView homeView = this.mHomeLayout;
                    if (!z3) {
                        logo = getIcon();
                    }
                    homeView.setIcon(logo);
                }
            } else {
                HomeView homeView2 = this.mHomeLayout;
                if (homeView2 != null) {
                    removeView(homeView2);
                }
            }
            if ((i3 & 8) != 0) {
                if ((i & 8) != 0) {
                    if (getNavigationMode() == 2) {
                        freeMainContainerChildren();
                    }
                    initTitle();
                } else {
                    CollapseTitle collapseTitle = this.mCollapseTitle;
                    if (collapseTitle != null) {
                        this.mMainContainer.removeView(collapseTitle.getLayout());
                    }
                    ExpandTitle expandTitle = this.mExpandTitle;
                    if (expandTitle != null) {
                        this.mMovableMainContainer.removeView(expandTitle.getLayout());
                    }
                    this.mCollapseTitle = null;
                    this.mExpandTitle = null;
                    if ((getDisplayOptions() & 32) == 0) {
                        removeView(this.mTitleUpView);
                        this.mTitleUpView = null;
                    }
                    if (getNavigationMode() == 2) {
                        addTabsToMainContainers();
                    }
                }
            }
            if ((i3 & 6) != 0) {
                boolean z4 = (this.mDisplayOptions & 4) != 0;
                CollapseTitle collapseTitle2 = this.mCollapseTitle;
                boolean z5 = collapseTitle2 != null && collapseTitle2.getVisibility() == 0;
                ExpandTitle expandTitle2 = this.mExpandTitle;
                if (expandTitle2 != null && expandTitle2.getVisibility() == 0) {
                    z5 = true;
                }
                if (this.mTitleUpView != null && (z5 || (getDisplayOptions() & 32) != 0)) {
                    View view2 = this.mTitleUpView;
                    if (!z) {
                        i4 = z4 ? 0 : 4;
                    }
                    view2.setVisibility(i4);
                }
            }
            if ((i3 & 16) != 0 && (view = this.mCustomNavView) != null) {
                if ((i & 16) != 0) {
                    safeAddView(this, view);
                    addCustomView();
                } else {
                    removeView(view);
                }
            }
            if ((i3 & 8192) != 0) {
                if ((i & 8192) != 0) {
                    View viewInflate = LayoutInflater.from(this.mContext).inflate(this.mNavigatorSwitchResId, (ViewGroup) this, false);
                    this.mNavigatorSwitch = viewInflate;
                    viewInflate.setTag(miuix.appcompat.R$id.miuix_appcompat_navigator_switch_presenter, new NavigatorSwitchPresenter(viewInflate));
                    Folme.useAt(this.mNavigatorSwitch).hover().setFeedbackRadius(60.0f);
                    Folme.useAt(this.mNavigatorSwitch).hover().setEffect(IHoverStyle.HoverEffect.FLOATED_WRAPPED).handleHoverOf(this.mNavigatorSwitch, new AnimConfig[0]);
                    addView(this.mNavigatorSwitch);
                    Boolean bool = this.mFloatingMode;
                    if (bool != null && bool.booleanValue()) {
                        FloatingEffectHelper.updateViewFloatingState(this.mNavigatorSwitch, true, MiuixUIUtils.isLightTheme(getContext()), this.mActionBarButtonMaterial, this.mFloatingBgColor);
                    }
                } else {
                    removeView(this.mNavigatorSwitch);
                    this.mNavigatorSwitch = null;
                }
            }
            requestLayout();
        } else {
            invalidate();
        }
        HomeView homeView3 = this.mHomeLayout;
        if (homeView3 != null) {
            if (!homeView3.isEnabled()) {
                this.mHomeLayout.setContentDescription(null);
                return;
            }
            int i5 = i & 4;
            HomeView homeView4 = this.mHomeLayout;
            if (i5 != 0) {
                homeView4.setContentDescription(this.mContext.getResources().getText(R$string.abc_action_bar_up_description));
            } else {
                homeView4.setContentDescription(this.mContext.getResources().getText(R$string.abc_action_bar_home_description));
            }
        }
    }

    private void addCustomView() {
        FrameLayout frameLayout = (FrameLayout) this.mCustomNavView.findViewById(miuix.appcompat.R$id.action_bar_expand_container);
        TextView customTitleView = getCustomTitleView(frameLayout);
        if (customTitleView != null) {
            this.mInitCustomTitle = customTitleView.getText();
            freeMainContainerChildren();
            this.mCollapseCustomContainer = frameLayout;
            this.mCollapseController.attachViews(frameLayout);
            ExpandTitle expandTitle = this.mExpandTitle;
            if (expandTitle != null) {
                expandTitle.setTitle(this.mInitCustomTitle);
                this.mExpandTitle.setTitleVisibility(0);
                this.mExpandTitle.setVisibility(0);
                this.mExpandTitle.setSubTitleVisibility(8);
                if (this.mMovableMainContainer != this.mExpandTitle.getLayout().getParent()) {
                    safeAddView(this.mMovableMainContainer, this.mExpandTitle.getLayout());
                }
            }
            customTitleView.addTextChangedListener(this.mCustomTitleWatcher);
        }
    }

    public View getCustomNavigationView() {
        return this.mCustomNavView;
    }

    public int getNavigationMode() {
        return this.mNavigationMode;
    }

    public int getDisplayOptions() {
        return this.mDisplayOptions;
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ActionBar.LayoutParams(8388627);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initTitle() {
        this.mPendingCreated = false;
        initTitleUpView();
        if (this.mNavigationMode == 2) {
            freeMainContainerChildren();
        }
        createTitleForCurrentState();
        updateTightTitle();
        post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarView$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ActionBarView.m5454$r8$lambda$TcAr6NBal3zTzwtlD3x4mPL6Uw(this.f$0);
            }
        });
        if (this.mExpandedActionView != null || isAllTitlesEmpty()) {
            setTitleVisibility(false);
        }
        safeAddView(this, this.mMainContainer);
        safeAddView(this, this.mMediumContainer);
        safeAddView(this, this.mMovableMainContainer, 0);
    }

    /* JADX INFO: renamed from: $r8$lambda$TcA-r6NBal3zTzwtlD3x4mPL6Uw, reason: not valid java name */
    public static /* synthetic */ void m5454$r8$lambda$TcAr6NBal3zTzwtlD3x4mPL6Uw(ActionBarView actionBarView) {
        actionBarView.pendingCreateTitle();
        actionBarView.setTitleVisibility(actionBarView.shouldTitleVisible());
        actionBarView.updateTightTitle();
    }

    private void createTitleForCurrentState() {
        int i = this.mInnerExpandState;
        if (i == 2) {
            if (this.mExpandTitle == null) {
                createExpandTitle(false);
            }
            AbsActionBarView.CollapseView collapseView = this.mCollapseController;
            if (collapseView != null) {
                collapseView.onHide();
                return;
            }
            return;
        }
        if (i == 1) {
            if (this.mMediumTitle == null) {
                createMediumTitle(false);
            }
        } else if (i == 0 && this.mCollapseTitle == null) {
            createCollapseTitle(false);
        }
    }

    private void initTitleUpView() {
        int i;
        if (this.mTitleUpView == null) {
            View viewGenerateTitleUpView = ActionBarViewFactory.generateTitleUpView(getContext(), null);
            this.mTitleUpView = viewGenerateTitleUpView;
            viewGenerateTitleUpView.setOnClickListener(this.mUpClickListener);
        }
        int i2 = this.mDisplayOptions;
        boolean z = (i2 & 4) != 0;
        boolean z2 = (i2 & 2) != 0;
        View view = this.mTitleUpView;
        if (z2) {
            i = 8;
        } else {
            i = z ? 0 : 4;
        }
        view.setVisibility(i);
        this.mTitleUpView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        safeAddView(this, this.mTitleUpView);
        Boolean bool = this.mFloatingMode;
        if (bool == null || !bool.booleanValue()) {
            return;
        }
        View view2 = this.mTitleUpView;
        if (!(view2 instanceof ViewGroup) || ((ViewGroup) view2).getChildCount() <= 0) {
            return;
        }
        FloatingEffectHelper.updateViewFloatingState(((ViewGroup) this.mTitleUpView).getChildAt(0), true, MiuixUIUtils.isLightTheme(getContext()), this.mActionBarButtonMaterial, this.mFloatingBgColor);
    }

    private void pendingCreateTitle() {
        if (this.mPendingCreated) {
            return;
        }
        boolean z = true;
        this.mPendingCreated = true;
        boolean z2 = (this.mDisplayOptions & 8) != 0;
        boolean z3 = z2 || (isUserSetExpandState() && this.mUserExpandState == 1);
        if (!z2 && (!isResizable() || !z3)) {
            z = false;
        }
        pendingCreateAllTitles(z);
        pendingCreateMediumTitle(z3);
        updateCollapseTitleTouchDelegate();
    }

    private void pendingCreateAllTitles(boolean z) {
        if (z) {
            if (this.mExpandTitle == null) {
                createExpandTitle(true);
                updateExpandTitle();
            }
            if (this.mCollapseTitle == null) {
                createCollapseTitle(true);
            }
            updateCollapseTitle();
        }
    }

    private void pendingCreateMediumTitle(boolean z) {
        if (z && this.mMediumTitle == null) {
            createMediumTitle(true);
            updateMediumTitle();
        }
    }

    private void updateCollapseTitleTouchDelegate() {
        CollapseTitle collapseTitle = this.mCollapseTitle;
        if (collapseTitle != null) {
            Rect hitRect = collapseTitle.getHitRect();
            hitRect.left -= AttributeResolver.resolveDimensionPixelSize(getContext(), R$attr.actionBarPaddingStart);
            setTouchDelegate(new TouchDelegate(hitRect, this.mCollapseTitle.getLayout()));
        }
    }

    private void createCollapseTitle(boolean z) {
        if (this.mCollapseTitle == null) {
            CollapseTitle collapseTitleGenerateCollapseTitle = ActionBarViewFactory.generateCollapseTitle(getContext(), this.mCollapseTitleStyleRes, this.mCollapseSubtitleStyleRes);
            this.mCollapseTitle = collapseTitleGenerateCollapseTitle;
            collapseTitleGenerateCollapseTitle.setVisible(this.mCollapsedTitleVisible);
            this.mCollapseTitle.setTextColorTransitEnable(this.mCollapseTitleColorTransitEnable, this.mInnerExpandState);
            this.mCollapseTitle.setAllTitlesClickable(this.mTitleClickable);
            this.mCollapseTitle.setTitle(this.mTitle);
            this.mCollapseTitle.setOnClickListener(this.mTitleClickListener, this.mTitleClickable);
            this.mCollapseTitle.setSubTitleOnClickListener(this.mSubTitleClickListener, this.mUserSubTitleClickListener != null);
            this.mCollapseTitle.setSubTitle(this.mSubtitle);
            if (!z) {
                safeAddView(this.mMainContainer, this.mCollapseTitle.getLayout());
                return;
            }
            if ((this.mDisplayOptions & 8) != 0) {
                if (getNavigationMode() == 2 && isTightTitleWithEmbeddedTabs()) {
                    return;
                }
                if (hasTabsInContainer(this.mMainContainer)) {
                    addSecondaryTabsToCollapseTabContainers();
                }
                this.mMainContainer.removeAllViews();
                safeAddView(this.mMainContainer, this.mCollapseTitle.getLayout());
            }
        }
    }

    private void createExpandTitle(boolean z) {
        boolean z2;
        View view;
        if (this.mExpandTitle == null) {
            ExpandTitle expandTitleGenerateExpandTitle = ActionBarViewFactory.generateExpandTitle(getContext());
            this.mExpandTitle = expandTitleGenerateExpandTitle;
            expandTitleGenerateExpandTitle.setVisible(this.mExpandTitleVisible);
            this.mExpandTitle.setTextColorTransitEnable(this.mExpandTitleColorTransitEnable, this.mInnerExpandState);
            this.mExpandTitle.setAllTitlesClickable(this.mTitleClickable);
            CharSequence charSequence = this.mTitle;
            if (!z || (this.mDisplayOptions & 16) == 0 || (view = this.mCustomNavView) == null || getCustomTitleView((FrameLayout) view.findViewById(miuix.appcompat.R$id.action_bar_expand_container)) == null || TextUtils.isEmpty(this.mInitCustomTitle)) {
                z2 = false;
            } else {
                charSequence = this.mInitCustomTitle;
                z2 = true;
            }
            this.mExpandTitle.setTitle(charSequence);
            this.mExpandTitle.setOnClickListener(this.mTitleClickListener, this.mTitleClickable);
            this.mExpandTitle.setSubTitleOnClickListener(this.mSubTitleClickListener, this.mUserSubTitleClickListener != null);
            ExpandTitle expandTitle = this.mExpandTitle;
            if (!z2) {
                expandTitle.setSubTitle(this.mSubtitle);
            } else {
                expandTitle.setSubTitle(null);
            }
            if (!z) {
                safeAddView(this.mMovableMainContainer, this.mExpandTitle.getLayout());
                return;
            }
            if ((this.mDisplayOptions & 8) != 0) {
                if (getNavigationMode() == 2 && isTightTitleWithEmbeddedTabs()) {
                    return;
                }
                if (hasTabsInContainer(this.mMovableMainContainer)) {
                    addSecondaryTabsToExpandTabContainers();
                }
                this.mMovableMainContainer.removeAllViews();
                safeAddView(this.mMovableMainContainer, this.mExpandTitle.getLayout());
            }
        }
    }

    private void createMediumTitle(boolean z) {
        if (this.mMediumTitle == null) {
            MediumTitle mediumTitleGenerateMediumTitle = ActionBarViewFactory.generateMediumTitle(getContext(), this.mMediumTitleStyleRes, this.mMediumSubtitleStyleRes);
            this.mMediumTitle = mediumTitleGenerateMediumTitle;
            mediumTitleGenerateMediumTitle.setVisible(this.mCollapsedTitleVisible);
            this.mMediumTitle.setTextColorTransitEnable(this.mCollapseTitleColorTransitEnable, this.mInnerExpandState);
            this.mMediumTitle.setTitleClickable(this.mTitleClickable);
            this.mMediumTitle.setTitle(this.mTitle);
            this.mMediumTitle.setOnClickListener(this.mTitleClickListener, this.mTitleClickable);
            this.mMediumTitle.setSubTitleOnClickListener(this.mSubTitleClickListener, this.mUserSubTitleClickListener != null);
            this.mMediumTitle.setSubTitle(this.mSubtitle);
            safeAddView(this.mMediumContainer, this.mMediumTitle.getLayout());
        }
    }

    private void safeAddView(ViewGroup viewGroup, View view) {
        safeAddView(viewGroup, view, -1);
    }

    private void safeAddView(ViewGroup viewGroup, View view, int i) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        if (viewGroup != null) {
            viewGroup.addView(view, i);
        }
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public void setExpandState(int i, boolean z, boolean z2) {
        if (!z) {
            pendingCreateTitle();
        }
        if (i == 1 && this.mMediumTitle == null) {
            createMediumTitle(false);
            updateMediumTitle();
        }
        super.setExpandState(i, z, z2);
    }

    public boolean isCollapsed() {
        return this.mIsCollapsed;
    }

    public void setEndActionMenuItemLimit(int i) {
        this.mEndActionMenuItemLimit = i;
        ActionMenuPresenter actionMenuPresenter = this.mEndActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.setItemLimit(i);
        }
    }

    public int getEndActionMenuItemLimit() {
        return this.mEndActionMenuItemLimit;
    }

    public boolean isUserSetEndActionMenuItemLimit() {
        return this.mUserSetEndActionMenuItemLimit;
    }

    private void updateTightTitle() {
        if (isTightTitleWithEmbeddedTabs()) {
            TextUtils.isEmpty(this.mTitle);
        }
        TextUtils.isEmpty(this.mSubtitle);
    }

    private boolean isSimpleCustomNavView() {
        View view = this.mCustomNavView;
        if (view == null || view.getVisibility() != 0) {
            return true;
        }
        ViewGroup.LayoutParams layoutParams = this.mCustomNavView.getLayoutParams();
        ActionBar.LayoutParams layoutParams2 = layoutParams instanceof ActionBar.LayoutParams ? (ActionBar.LayoutParams) layoutParams : null;
        return layoutParams2 != null && normalizeHorizontalGravity(layoutParams2.gravity, ViewUtils.isLayoutRtl(this)) == 8388613;
    }

    private boolean isTitleCenter() {
        if (!this.mTitleCenter || !isSimpleCustomNavView()) {
            return false;
        }
        HomeView homeView = this.mHomeLayout;
        return homeView == null || homeView.getVisibility() == 8;
    }

    private boolean isShowTitle() {
        if (this.mMainContainer.getChildCount() <= 0) {
            return (this.mCustomNavView == null || this.mCollapseCustomContainer == null) ? false : true;
        }
        return true;
    }

    private void updateTitleCenter() {
        CollapseTitle collapseTitle = this.mCollapseTitle;
        if (collapseTitle != null) {
            collapseTitle.updateTitleCenter(isTitleCenter());
        }
    }

    /* JADX WARN: Code duplicated, block: B:167:0x0328 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:168:0x032a  */
    /* JADX WARN: Code duplicated, block: B:169:0x032f A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:170:0x0331  */
    /* JADX WARN: Code duplicated, block: B:171:0x0336  */
    /* JADX WARN: Code duplicated, block: B:175:0x033f  */
    /* JADX WARN: Code duplicated, block: B:176:0x0342  */
    /* JADX WARN: Code duplicated, block: B:178:0x0346  */
    /* JADX WARN: Code duplicated, block: B:179:0x034f  */
    /* JADX WARN: Code duplicated, block: B:182:0x035c  */
    /* JADX WARN: Code duplicated, block: B:183:0x035f  */
    /* JADX WARN: Code duplicated, block: B:205:0x03de  */
    /* JADX WARN: Code duplicated, block: B:212:0x03f2 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:213:0x03f4  */
    /* JADX WARN: Code duplicated, block: B:215:0x03fa  */
    /* JADX WARN: Code duplicated, block: B:218:0x0415 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:221:0x0429  */
    /* JADX WARN: Code duplicated, block: B:222:0x0430  */
    /* JADX WARN: Code duplicated, block: B:227:0x044b  */
    /* JADX WARN: Code duplicated, block: B:233:0x046c  */
    /* JADX WARN: Code duplicated, block: B:237:0x047f A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:238:0x0481  */
    /* JADX WARN: Code duplicated, block: B:239:0x0483  */
    /* JADX WARN: Code duplicated, block: B:241:0x0496  */
    /* JADX WARN: Code duplicated, block: B:247:0x04c6  */
    /* JADX WARN: Code duplicated, block: B:250:0x04e1  */
    /* JADX WARN: Code duplicated, block: B:251:0x04f9  */
    /* JADX WARN: Code duplicated, block: B:254:0x0500  */
    /* JADX WARN: Code duplicated, block: B:255:0x0517  */
    /* JADX WARN: Code duplicated, block: B:265:0x052a  */
    /* JADX WARN: Code duplicated, block: B:268:0x053f  */
    /* JADX WARN: Code duplicated, block: B:269:0x0546  */
    /* JADX WARN: Code duplicated, block: B:272:0x0561  */
    /* JADX WARN: Code duplicated, block: B:274:0x0569  */
    /* JADX WARN: Code duplicated, block: B:276:0x056d  */
    /* JADX WARN: Code duplicated, block: B:277:0x057c  */
    /* JADX WARN: Code duplicated, block: B:279:0x0580  */
    /* JADX WARN: Code duplicated, block: B:280:0x0584  */
    /* JADX WARN: Code duplicated, block: B:282:0x0589  */
    /* JADX WARN: Code duplicated, block: B:283:0x058d  */
    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int iMax;
        int i3;
        int iMax2;
        int i4;
        int iMax3;
        int measuredWidth;
        int i5;
        boolean z;
        boolean z2;
        int iMax4;
        int measuredWidth2;
        boolean z3;
        int i6;
        boolean zShouldMeasureCollapseTabContainer;
        boolean zShouldMeasureMovableTabContainer;
        ExtraPaddingPolicy extraPaddingPolicy;
        int i7;
        int iMax5;
        int i8;
        int i9;
        int measuredHeight;
        int i10;
        int measuredHeight2;
        int iMax6;
        int i11;
        int i12;
        ProgressBar progressBar;
        int i13;
        int i14;
        int iMeasureChildView;
        int i15;
        int measuredWidth3;
        int i16;
        int measuredWidth4;
        int i17;
        int i18;
        int i19;
        int i20;
        int iMin;
        int iMax7;
        int i21;
        int i22;
        int iMin2;
        int iMax8;
        int i23;
        View view;
        View view2;
        int i24;
        int iMakeMeasureSpec;
        ActionMenuView actionMenuView;
        int childCount = getChildCount();
        int i25 = 0;
        for (int i26 = 0; i26 < childCount; i26++) {
            View childAt = getChildAt(i26);
            if (childAt.getVisibility() != 8 && (childAt != (actionMenuView = this.mMenuView) || actionMenuView.getChildCount() != 0)) {
                i25++;
            }
        }
        if (i25 == 0) {
            setMeasuredDimension(0, 0);
            this.mIsCollapsed = true;
            return;
        }
        this.mIsCollapsed = false;
        int size = View.MeasureSpec.getSize(i);
        int i27 = this.mTitleMaxHeight;
        int i28 = this.mTitleMinHeight;
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int size2 = (i27 > 0 ? i27 : View.MeasureSpec.getSize(i2)) - paddingTop;
        int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE);
        int iMeasureChildView2 = (size - paddingStart) - paddingEnd;
        int iMax9 = iMeasureChildView2 / 2;
        boolean zHasTitle = hasTitle();
        View view3 = this.mStartView;
        if (view3 == null || view3.getVisibility() == 8) {
            iMax = 0;
        } else {
            iMeasureChildView2 = measureChildView(this.mStartView, iMeasureChildView2, iMakeMeasureSpec2, 0);
            paddingStart += this.mStartView.getMeasuredWidth();
            iMax = this.mStartView.getMeasuredHeight() + paddingTop;
        }
        View view4 = this.mNavigatorSwitch;
        if (view4 != null) {
            ((NavigatorSwitchPresenter) view4.getTag(miuix.appcompat.R$id.miuix_appcompat_navigator_switch_presenter)).suppressVisibility(false, 0);
            if (this.mNavigatorSwitch.getVisibility() != 8) {
                iMeasureChildView2 = measureChildView(this.mNavigatorSwitch, iMeasureChildView2, iMakeMeasureSpec2, 0);
                paddingStart += this.mNavigatorSwitch.getMeasuredWidth();
                iMax = Math.max(iMax, this.mNavigatorSwitch.getMeasuredHeight() + paddingTop);
            }
        }
        View view5 = this.mStartView;
        if (view5 != null && view5.getVisibility() != 0) {
            this.mHasStartView = false;
        }
        View view6 = this.mNavigatorSwitch;
        if (view6 != null && view6.getVisibility() != 0) {
            this.mHasNavigatorSwitchView = false;
        }
        HomeView homeView = this.mExpandedActionView != null ? this.mExpandedHomeLayout : this.mHomeLayout;
        if (this.mStartView == null || homeView == null) {
            i3 = 8;
        } else {
            i3 = 8;
            homeView.setVisibility(8);
        }
        if (homeView == null || homeView.getVisibility() == i3) {
            iMax2 = iMax9;
        } else {
            int i29 = homeView.getLayoutParams().width;
            if (i29 < 0) {
                iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(iMeasureChildView2, Integer.MIN_VALUE);
            } else {
                iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i29, 1073741824);
            }
            homeView.measure(iMakeMeasureSpec, iMakeMeasureSpec2);
            int measuredWidth5 = homeView.getMeasuredWidth() + homeView.getStartOffset();
            iMeasureChildView2 = Math.max(0, iMeasureChildView2 - measuredWidth5);
            iMax2 = Math.max(0, iMeasureChildView2 - measuredWidth5);
            paddingStart += measuredWidth5;
            iMax = Math.max(iMax, homeView.getMeasuredHeight() + paddingTop);
        }
        ActionMenuView actionMenuView2 = this.mEndMenuView;
        if (actionMenuView2 != null && actionMenuView2.getParent() == this && this.mEndMenuView.getVisibility() != 8) {
            iMeasureChildView2 = measureChildView(this.mEndMenuView, iMeasureChildView2, iMakeMeasureSpec2, 0);
            iMax9 = Math.max(0, iMax9 - this.mEndMenuView.getMeasuredWidth());
            paddingEnd += this.mEndMenuView.getMeasuredWidth();
            iMax = Math.max(iMax, this.mEndMenuView.getMeasuredHeight() + paddingTop);
        }
        View view7 = this.mEndView;
        if (view7 != null && view7.getVisibility() != 8) {
            iMeasureChildView2 = measureChildView(this.mEndView, iMeasureChildView2, iMakeMeasureSpec2, 0);
            paddingEnd += this.mEndView.getMeasuredWidth();
            iMax = Math.max(iMax, this.mEndView.getMeasuredHeight() + paddingTop);
        }
        View view8 = this.mSearchView;
        if (view8 != null) {
            i4 = 2;
            if (view8.getVisibility() != 8) {
                int iMin3 = iMeasureChildView2 - (this.mSearchViewMargin * 2);
                ViewGroup.LayoutParams layoutParams = this.mSearchView.getLayoutParams();
                if (layoutParams != null && (i24 = layoutParams.width) > 0) {
                    iMin3 = Math.min(i24, iMin3);
                }
                this.mSearchView.measure(View.MeasureSpec.makeMeasureSpec(iMin3, 1073741824), iMakeMeasureSpec2);
                int measuredWidth6 = this.mSearchView.getMeasuredWidth() + (this.mSearchViewMargin * 2);
                iMeasureChildView2 = Math.max(0, iMeasureChildView2 - measuredWidth6);
                iMax9 = Math.max(0, iMax9 - measuredWidth6);
                paddingEnd += measuredWidth6;
                iMax = Math.max(iMax, this.mSearchView.getMeasuredHeight() + paddingTop);
            }
        } else {
            i4 = 2;
        }
        ProgressBar progressBar2 = this.mIndeterminateProgressView;
        if (progressBar2 != null && progressBar2.getVisibility() != 8) {
            iMeasureChildView2 = measureChildView(this.mIndeterminateProgressView, iMeasureChildView2, iMakeMeasureSpec2, this.mProgressBarPadding * 2);
            iMax9 = Math.max(0, (iMax9 - this.mIndeterminateProgressView.getMeasuredWidth()) - (this.mProgressBarPadding * 2));
            paddingEnd += this.mIndeterminateProgressView.getMeasuredWidth();
            iMax = Math.max(iMax, this.mIndeterminateProgressView.getMeasuredHeight() + paddingTop);
        }
        View view9 = this.mCenterView;
        if (view9 == null || view9.getVisibility() == 8) {
            iMax3 = i28;
        } else {
            measureChildView(this.mCenterView, iMeasureChildView2, iMakeMeasureSpec2, 0);
            int measuredHeight3 = this.mCenterView.getMeasuredHeight() + paddingTop;
            iMax = Math.max(iMax, measuredHeight3);
            iMax3 = Math.max(i28, measuredHeight3);
        }
        boolean zIsShowTitle = isShowTitle();
        if (zIsShowTitle) {
            updateTitleCenter();
        }
        if (this.mExpandedActionView != null || ((!zIsShowTitle && (getDisplayOptions() & 32) == 0) || (view2 = this.mTitleUpView) == null || view2.getVisibility() != 0)) {
            measuredWidth = paddingStart;
        } else {
            int i30 = paddingStart;
            iMeasureChildView2 = measureChildView(this.mTitleUpView, iMeasureChildView2, iMakeMeasureSpec2, (this.mHasStartView || this.mHasNavigatorSwitchView) ? this.mTitleUpViewMarginStart + this.mTitleUpViewMarginEnd : 0);
            measuredWidth = i30 + this.mTitleUpView.getMeasuredWidth() + ((this.mHasStartView || this.mHasNavigatorSwitchView) ? this.mTitleUpViewMarginEnd + this.mTitleUpViewMarginStart : 0);
            Math.max(iMax, this.mTitleUpView.getMeasuredHeight() + paddingTop);
        }
        View view10 = this.mExpandedActionView;
        if (view10 == null && ((this.mDisplayOptions & 16) == 0 || (view10 = this.mCustomNavView) == null)) {
            view10 = null;
        }
        if ((this.mStartView == null && this.mEndView == null) || view10 == null) {
            i5 = 8;
        } else {
            i5 = 8;
            view10.setVisibility(8);
        }
        if (view10 != null && view10.getVisibility() != i5) {
            View view11 = this.mTitleUpView;
            int measuredWidth7 = (view11 == null || view11.getVisibility() != 4) ? 0 : this.mTitleUpView.getMeasuredWidth();
            ViewGroup.LayoutParams layoutParamsGenerateLayoutParams = generateLayoutParams(view10.getLayoutParams());
            int i31 = measuredWidth7;
            ActionBar.LayoutParams layoutParams2 = layoutParamsGenerateLayoutParams instanceof ActionBar.LayoutParams ? (ActionBar.LayoutParams) layoutParamsGenerateLayoutParams : null;
            z = zIsShowTitle;
            if (layoutParams2 != null) {
                i18 = ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin;
                i17 = ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin;
            } else {
                i17 = 0;
                i18 = 0;
            }
            if (i27 <= 0) {
                i19 = i17;
            } else {
                i19 = i17;
                int i32 = layoutParamsGenerateLayoutParams.height >= 0 ? 1073741824 : Integer.MIN_VALUE;
                i20 = iMeasureChildView2;
                iMin = layoutParamsGenerateLayoutParams.height;
                z2 = zHasTitle;
                if (iMin >= 0) {
                    if (iMin == -1) {
                        iMax7 = Math.max(i28, i27);
                    } else {
                        iMax7 = size2;
                    }
                    iMin = iMax7 - i19;
                } else if (i27 > 0) {
                    iMin = Math.min(iMin, i27);
                }
                i21 = layoutParamsGenerateLayoutParams.width;
                if (i21 != -2) {
                    i22 = 1073741824;
                } else {
                    i22 = Integer.MIN_VALUE;
                }
                if (i21 >= 0) {
                    iMin2 = Math.min(i21, i20 + i31);
                } else {
                    iMin2 = i20 + i31;
                }
                iMax8 = Math.max(0, iMin2 - i18);
                if (layoutParams2 != null) {
                    i23 = layoutParams2.gravity;
                } else {
                    i23 = 8388627;
                }
                if ((i23 & 8388615) == 1 && layoutParamsGenerateLayoutParams.width == -1) {
                    iMax8 = Math.min(iMax2, iMax9) * 2;
                }
                view = this.mNavigatorSwitch;
                if (view != null && view.getVisibility() == 0) {
                    iMax8 = (int) (iMax8 + ((this.mNavigatorSwitch.getMeasuredWidth() * (1.0f - this.mNavigatorSwitch.getAlpha())) - (this.mTitleUpViewMarginStart * this.mNavigatorSwitch.getAlpha())));
                }
                view10.measure(View.MeasureSpec.makeMeasureSpec(iMax8, i22), View.MeasureSpec.makeMeasureSpec(iMin, i32));
                measuredWidth2 = i20 - ((i18 + view10.getMeasuredWidth()) - i31);
                iMax4 = Math.max(iMax3, view10.getMeasuredHeight());
            }
            i20 = iMeasureChildView2;
            iMin = layoutParamsGenerateLayoutParams.height;
            z2 = zHasTitle;
            if (iMin >= 0) {
                if (iMin == -1) {
                    iMax7 = Math.max(i28, i27);
                } else {
                    iMax7 = size2;
                }
                iMin = iMax7 - i19;
            } else if (i27 > 0) {
                iMin = Math.min(iMin, i27);
            }
            i21 = layoutParamsGenerateLayoutParams.width;
            if (i21 != -2) {
                i22 = 1073741824;
            } else {
                i22 = Integer.MIN_VALUE;
            }
            if (i21 >= 0) {
                iMin2 = Math.min(i21, i20 + i31);
            } else {
                iMin2 = i20 + i31;
            }
            iMax8 = Math.max(0, iMin2 - i18);
            if (layoutParams2 != null) {
                i23 = layoutParams2.gravity;
            } else {
                i23 = 8388627;
            }
            if ((i23 & 8388615) == 1) {
                iMax8 = Math.min(iMax2, iMax9) * 2;
            }
            view = this.mNavigatorSwitch;
            if (view != null) {
                iMax8 = (int) (iMax8 + ((this.mNavigatorSwitch.getMeasuredWidth() * (1.0f - this.mNavigatorSwitch.getAlpha())) - (this.mTitleUpViewMarginStart * this.mNavigatorSwitch.getAlpha())));
            }
            view10.measure(View.MeasureSpec.makeMeasureSpec(iMax8, i22), View.MeasureSpec.makeMeasureSpec(iMin, i32));
            measuredWidth2 = i20 - ((i18 + view10.getMeasuredWidth()) - i31);
            iMax4 = Math.max(iMax3, view10.getMeasuredHeight());
        } else {
            i28 = i28;
            z = zIsShowTitle;
            int i33 = iMeasureChildView2;
            z2 = zHasTitle;
            if (view10 == null || view10.getVisibility() != 8 || z) {
                iMax4 = iMax3;
                measuredWidth2 = i33;
            } else {
                iMax4 = iMax3;
                measuredWidth2 = i33;
                z3 = true;
            }
            if (this.mExpandedActionView == null) {
                if ((!z || this.mInnerExpandState == 3) && this.mMainContainer.getChildCount() > 0) {
                    if (isTitleCenter()) {
                        if (measuredWidth > paddingEnd) {
                            i15 = size - (measuredWidth * 2);
                        } else {
                            i15 = measuredWidth2;
                        }
                        int iMax10 = Math.max(0, i15);
                        this.mMainContainer.measure(View.MeasureSpec.makeMeasureSpec(iMax10, Integer.MIN_VALUE), iMakeMeasureSpec2);
                        measuredWidth3 = this.mMainContainer.getMeasuredWidth();
                        i16 = this.mTitleMinWidth;
                        if (measuredWidth3 >= i16 && iMax10 < i16) {
                            this.mMainContainer.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth2, Integer.MIN_VALUE), iMakeMeasureSpec2);
                            measuredWidth4 = this.mMainContainer.getMeasuredWidth();
                        } else {
                            measuredWidth4 = this.mMainContainer.getMeasuredWidth();
                        }
                        iMeasureChildView = measuredWidth2 - measuredWidth4;
                    } else {
                        iMeasureChildView = measureChildView(this.mMainContainer, measuredWidth2, iMakeMeasureSpec2, 0);
                    }
                    iMax3 = Math.max(iMax3, this.mMainContainer.getMeasuredHeight());
                } else {
                    iMeasureChildView = measuredWidth2;
                }
                if (this.mMediumContainer.getChildCount() != 0) {
                    if (isTitleCenter() && measuredWidth > paddingEnd) {
                        measuredWidth2 = size - (measuredWidth * 2);
                    }
                    this.mMediumContainer.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, measuredWidth2), Integer.MIN_VALUE), iMakeMeasureSpec2);
                    if (this.mInnerExpandState == 1) {
                        iMax3 = Math.max(iMax3, this.mMediumContainer.getMeasuredHeight());
                    }
                }
                measuredWidth2 = iMeasureChildView;
            }
            if (this.mMovableMainContainer.getChildCount() != 0) {
                if (z2) {
                    i14 = 0;
                } else {
                    i14 = 1073741824;
                }
                i6 = 0;
                this.mMovableMainContainer.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, i14));
            } else {
                i6 = 0;
            }
            zShouldMeasureCollapseTabContainer = shouldMeasureCollapseTabContainer();
            zShouldMeasureMovableTabContainer = shouldMeasureMovableTabContainer();
            this.mMovableSecondaryTabHeight = i6;
            float f = getResources().getConfiguration().densityDpi / 160.0f;
            extraPaddingPolicy = this.mExtraPaddingPolicy;
            if (extraPaddingPolicy == null && extraPaddingPolicy.isEnable()) {
                this.mExtraPadding = (int) (this.mExtraPaddingPolicy.getExtraPaddingDp() * f);
                i7 = 0;
            } else {
                i7 = 0;
                this.mExtraPadding = 0;
            }
            iMax5 = Math.max(i7, size - ((this.mUncollapseTabPaddingH + this.mExtraPadding) * 2));
            i8 = size - ((this.mUncollapseTabPaddingH + this.mExtraPadding) * 2);
            if (zShouldMeasureMovableTabContainer) {
                i9 = 1073741824;
                this.mMovableTabContainer.measure(View.MeasureSpec.makeMeasureSpec(iMax5, 1073741824), View.MeasureSpec.makeMeasureSpec(i7, i7));
                measuredHeight = this.mMovableTabContainer.getMeasuredHeight();
                this.mMovableSecondaryTabHeight = measuredHeight;
            } else {
                i9 = 1073741824;
                measuredHeight = i7;
            }
            this.mCollapseSecondaryTabHeight = i7;
            if (zShouldMeasureCollapseTabContainer) {
                this.mCollapseTabContainer.measure(View.MeasureSpec.makeMeasureSpec(i8, i9), View.MeasureSpec.makeMeasureSpec(i7, i7));
                int measuredHeight4 = this.mCollapseTabContainer.getMeasuredHeight();
                this.mCollapseSecondaryTabHeight = measuredHeight4;
                i10 = measuredHeight4;
            } else {
                i10 = 0;
            }
            if (this.mExpandedActionView == null && this.mNavigationMode == 1 && this.mListNavLayout != null) {
                i13 = this.mItemPadding;
                if (z) {
                    i13 *= 2;
                }
                this.mListNavLayout.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, measuredWidth2 - i13), Integer.MIN_VALUE), iMakeMeasureSpec2);
            }
            if (z2) {
                measuredHeight2 = this.mMovableMainContainer.getMeasuredHeight();
            } else {
                measuredHeight2 = 0;
            }
            int iMax11 = Math.max(iMax4, iMax3 + i10);
            this.mCollapseTotalHeight = iMax11;
            this.mCollapseTotalHeight = Math.max(i28, iMax11);
            iMax6 = Math.max(iMax4, iMax3) + measuredHeight2 + measuredHeight;
            this.mExpandTotalHeight = iMax6;
            if (z3) {
                setMeasuredDimension(size, 0);
                this.mIsCollapsed = true;
                return;
            }
            i11 = this.mInnerExpandState;
            if (i11 == 3) {
                int i34 = this.mCollapseTotalHeight;
                setMeasuredDimension(size, Math.max((i34 - i10) + this.mPendingHeight, i34));
            } else if (i11 == i4) {
                setMeasuredDimension(size, iMax6);
            } else {
                i12 = this.mCollapseTotalHeight;
                if (i11 == 1) {
                    setMeasuredDimension(size, i12);
                } else {
                    setMeasuredDimension(size, i12);
                }
            }
            progressBar = this.mProgressView;
            if (progressBar != null || progressBar.getVisibility() == 8) {
            }
            this.mProgressView.measure(View.MeasureSpec.makeMeasureSpec(size - (this.mProgressBarPadding * 2), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE));
            return;
        }
        z3 = false;
        if (this.mExpandedActionView == null) {
            if (z) {
                if (isTitleCenter()) {
                    if (measuredWidth > paddingEnd) {
                        i15 = size - (measuredWidth * 2);
                    } else {
                        i15 = measuredWidth2;
                    }
                    int iMax12 = Math.max(0, i15);
                    this.mMainContainer.measure(View.MeasureSpec.makeMeasureSpec(iMax12, Integer.MIN_VALUE), iMakeMeasureSpec2);
                    measuredWidth3 = this.mMainContainer.getMeasuredWidth();
                    i16 = this.mTitleMinWidth;
                    if (measuredWidth3 >= i16) {
                        measuredWidth4 = this.mMainContainer.getMeasuredWidth();
                    } else {
                        measuredWidth4 = this.mMainContainer.getMeasuredWidth();
                    }
                    iMeasureChildView = measuredWidth2 - measuredWidth4;
                } else {
                    iMeasureChildView = measureChildView(this.mMainContainer, measuredWidth2, iMakeMeasureSpec2, 0);
                }
                iMax3 = Math.max(iMax3, this.mMainContainer.getMeasuredHeight());
            } else {
                if (isTitleCenter()) {
                    if (measuredWidth > paddingEnd) {
                        i15 = size - (measuredWidth * 2);
                    } else {
                        i15 = measuredWidth2;
                    }
                    int iMax13 = Math.max(0, i15);
                    this.mMainContainer.measure(View.MeasureSpec.makeMeasureSpec(iMax13, Integer.MIN_VALUE), iMakeMeasureSpec2);
                    measuredWidth3 = this.mMainContainer.getMeasuredWidth();
                    i16 = this.mTitleMinWidth;
                    if (measuredWidth3 >= i16) {
                        measuredWidth4 = this.mMainContainer.getMeasuredWidth();
                    } else {
                        measuredWidth4 = this.mMainContainer.getMeasuredWidth();
                    }
                    iMeasureChildView = measuredWidth2 - measuredWidth4;
                } else {
                    iMeasureChildView = measureChildView(this.mMainContainer, measuredWidth2, iMakeMeasureSpec2, 0);
                }
                iMax3 = Math.max(iMax3, this.mMainContainer.getMeasuredHeight());
            }
            if (this.mMediumContainer.getChildCount() != 0) {
                if (isTitleCenter()) {
                    measuredWidth2 = size - (measuredWidth * 2);
                }
                this.mMediumContainer.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, measuredWidth2), Integer.MIN_VALUE), iMakeMeasureSpec2);
                if (this.mInnerExpandState == 1) {
                    iMax3 = Math.max(iMax3, this.mMediumContainer.getMeasuredHeight());
                }
            }
            measuredWidth2 = iMeasureChildView;
        }
        if (this.mMovableMainContainer.getChildCount() != 0) {
            if (z2) {
                i14 = 0;
            } else {
                i14 = 1073741824;
            }
            i6 = 0;
            this.mMovableMainContainer.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, i14));
        } else {
            i6 = 0;
        }
        zShouldMeasureCollapseTabContainer = shouldMeasureCollapseTabContainer();
        zShouldMeasureMovableTabContainer = shouldMeasureMovableTabContainer();
        this.mMovableSecondaryTabHeight = i6;
        float f2 = getResources().getConfiguration().densityDpi / 160.0f;
        extraPaddingPolicy = this.mExtraPaddingPolicy;
        if (extraPaddingPolicy == null) {
            i7 = 0;
            this.mExtraPadding = 0;
        } else {
            i7 = 0;
            this.mExtraPadding = 0;
        }
        iMax5 = Math.max(i7, size - ((this.mUncollapseTabPaddingH + this.mExtraPadding) * 2));
        i8 = size - ((this.mUncollapseTabPaddingH + this.mExtraPadding) * 2);
        if (zShouldMeasureMovableTabContainer) {
            i9 = 1073741824;
            this.mMovableTabContainer.measure(View.MeasureSpec.makeMeasureSpec(iMax5, 1073741824), View.MeasureSpec.makeMeasureSpec(i7, i7));
            measuredHeight = this.mMovableTabContainer.getMeasuredHeight();
            this.mMovableSecondaryTabHeight = measuredHeight;
        } else {
            i9 = 1073741824;
            measuredHeight = i7;
        }
        this.mCollapseSecondaryTabHeight = i7;
        if (zShouldMeasureCollapseTabContainer) {
            this.mCollapseTabContainer.measure(View.MeasureSpec.makeMeasureSpec(i8, i9), View.MeasureSpec.makeMeasureSpec(i7, i7));
            int measuredHeight5 = this.mCollapseTabContainer.getMeasuredHeight();
            this.mCollapseSecondaryTabHeight = measuredHeight5;
            i10 = measuredHeight5;
        } else {
            i10 = 0;
        }
        if (this.mExpandedActionView == null) {
            i13 = this.mItemPadding;
            if (z) {
                i13 *= 2;
            }
            this.mListNavLayout.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, measuredWidth2 - i13), Integer.MIN_VALUE), iMakeMeasureSpec2);
        }
        if (z2) {
            measuredHeight2 = this.mMovableMainContainer.getMeasuredHeight();
        } else {
            measuredHeight2 = 0;
        }
        int iMax14 = Math.max(iMax4, iMax3 + i10);
        this.mCollapseTotalHeight = iMax14;
        this.mCollapseTotalHeight = Math.max(i28, iMax14);
        iMax6 = Math.max(iMax4, iMax3) + measuredHeight2 + measuredHeight;
        this.mExpandTotalHeight = iMax6;
        if (z3) {
            setMeasuredDimension(size, 0);
            this.mIsCollapsed = true;
            return;
        }
        i11 = this.mInnerExpandState;
        if (i11 == 3) {
            int i35 = this.mCollapseTotalHeight;
            setMeasuredDimension(size, Math.max((i35 - i10) + this.mPendingHeight, i35));
        } else if (i11 == i4) {
            setMeasuredDimension(size, iMax6);
        } else {
            i12 = this.mCollapseTotalHeight;
            if (i11 == 1) {
                setMeasuredDimension(size, i12);
            } else {
                setMeasuredDimension(size, i12);
            }
        }
        progressBar = this.mProgressView;
        if (progressBar != null) {
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int iComputeCollapseContentHeight = computeCollapseContentHeight();
        int i5 = this.mCollapseSecondaryTabHeight;
        int measuredHeight = this.mMovableMainContainer.getMeasuredHeight();
        int i6 = this.mMovableSecondaryTabHeight;
        int iComputeExtraHeight = computeExtraHeight(measuredHeight, i6);
        int i7 = (i4 - i2) - i6;
        float fComputeLayoutProcess = computeLayoutProcess(measuredHeight, i6, iComputeExtraHeight);
        onLayoutCollapseViews(z, i, 0, i3, iComputeCollapseContentHeight, i5);
        onLayoutExpandViews(z, i, i7 - iComputeExtraHeight, i3, i7, i6, fComputeLayoutProcess);
        notifyMenuStateChange();
        if (!this.mInActionMode && !this.mInActionModeAnimating) {
            animateLayoutWithProcess(fComputeLayoutProcess);
        }
        this.mLastProcess = fComputeLayoutProcess;
        updateBadgeOnMenuItemViews();
    }

    private int computeCollapseContentHeight() {
        int iMax = Math.max(this.mTitleMinHeight, this.mMainContainer.getMeasuredHeight());
        View view = this.mCustomNavView;
        if (view != null && view.getParent() == this) {
            iMax = Math.max(iMax, this.mCustomNavView.getMeasuredHeight());
        }
        View view2 = this.mCenterView;
        return (view2 == null || view2.getVisibility() == 8) ? iMax : Math.max(iMax, this.mCenterView.getMeasuredHeight());
    }

    private int computeExtraHeight(int i, int i2) {
        int i3 = this.mInnerExpandState;
        if (i3 == 3) {
            return this.mPendingHeight;
        }
        if (i3 == 2) {
            return i + i2;
        }
        return 0;
    }

    private float computeLayoutProcess(int i, int i2, int i3) {
        if (hasTitle() || i != 0) {
            return Math.min(1.0f, ((i2 + i) - i3) / i);
        }
        return 1.0f;
    }

    private void animateLayoutWithProcess(float f) {
        float fMin = 1.0f - Math.min(1.0f, 3.0f * f);
        int i = this.mInnerExpandState;
        if (i == 3) {
            if (this.mLastProcess == f) {
                this.mLastResizingProcess = fMin;
                return;
            }
            boolean z = this.mIsCollapseTitleShowingOnResizing;
            if (fMin > 0.0f) {
                if (z) {
                    this.mIsCollapseTitleShowingOnResizing = false;
                    this.mCollapseController.animTo(0.0f, 0, 20, this.mCollapseAnimHideConfig);
                    if (this.mActionBarTransitionListeners.size() > 0) {
                        Folme.useValue("target", 0).setFlags(1L).setup(2).setTo("expand", Integer.valueOf(this.mTransitionTarget)).to("expand", 20, this.mHideProcessConfig);
                    }
                    this.mMovableController.setVisibility(0);
                }
            } else if (!z) {
                this.mIsCollapseTitleShowingOnResizing = true;
                this.mCollapseController.animTo(1.0f, 0, 0, this.mCollapseAnimShowConfig);
                if (this.mActionBarTransitionListeners.size() > 0) {
                    Folme.useValue("target", 0).setFlags(1L).setup(0).setTo("collapse", Integer.valueOf(this.mTransitionTarget)).to("collapse", 0, this.mShowProcessConfig);
                }
                this.mCollapseController.setVisibility(0);
            }
            if (this.mLastResizingProcess != fMin) {
                this.mMovableController.setAnimFrom(fMin, 0, 0, true);
                this.mLastResizingProcess = fMin;
                return;
            }
            return;
        }
        if (i == 2) {
            this.mNeedRequestLayoutOnExpandTitleShowing = this.mLastResizingProcess == 0.0f;
            this.mTransitionTarget = 20;
            this.mLastResizingProcess = 1.0f;
            this.mIsCollapseTitleShowingOnResizing = false;
            if (this.mLastProcess == f) {
                return;
            }
            this.mCollapseController.animTo(0.0f, 0, 20, this.mCollapseAnimHideConfig);
            this.mMovableController.animTo(1.0f, 0, 0, this.mMovableAnimShowConfig);
            return;
        }
        if (i == 0) {
            this.mNeedRequestLayoutOnExpandTitleShowing = false;
            this.mTransitionTarget = 0;
            this.mLastResizingProcess = 0.0f;
            this.mIsCollapseTitleShowingOnResizing = true;
            if (this.mLastProcess == f) {
                return;
            }
            this.mCollapseController.animTo(1.0f, 0, 0, this.mCollapseAnimShowConfig);
            this.mMovableController.animTo(0.0f, 0, 0, this.mMovableAnimNormalConfig);
            return;
        }
        if (i == 1) {
            this.mNeedRequestLayoutOnExpandTitleShowing = false;
            this.mTransitionTarget = 0;
            this.mLastResizingProcess = 0.0f;
            this.mIsCollapseTitleShowingOnResizing = false;
            if (hasTabsInContainer(this.mMainContainer)) {
                this.mMainContainer.setVisibility(0);
                this.mMainContainer.setAlpha(1.0f);
                this.mMainContainer.setTranslationX(0.0f);
                this.mMainContainer.setTranslationY(0.0f);
                AbsActionBarView.CollapseView collapseView = this.mMediumController;
                if (collapseView != null) {
                    collapseView.setVisibility(8);
                }
            } else {
                AbsActionBarView.CollapseView collapseView2 = this.mMediumController;
                if (collapseView2 != null) {
                    collapseView2.setVisibility(0);
                    this.mMediumController.setAnimFrom(1.0f, 0, 0, true);
                }
                this.mMainContainer.setVisibility(8);
                this.mMainContainer.clearFocus();
                this.mMainContainer.setAlpha(0.0f);
                this.mMainContainer.setTranslationX(0.0f);
                this.mMainContainer.setTranslationY(0.0f);
            }
            AbsActionBarView.CollapseView collapseView3 = this.mMovableController;
            if (collapseView3 != null) {
                collapseView3.setVisibility(8);
                this.mMovableController.setAnimFrom(0.0f, 0, 0, true);
            }
            ensureCollapseTabContainerVisibleForMedium();
        }
    }

    private boolean hasTitle() {
        return !((this.mDisplayOptions & 8) == 0 || isAllTitlesEmpty()) || getNavigationMode() == 2;
    }

    private boolean canCollapseTitleBeShown() {
        if (this.mCollapseTitle == null || TextUtils.isEmpty(this.mTitle)) {
            return false;
        }
        boolean zCanTitleBeShown = this.mCollapseTitle.canTitleBeShown(this.mTitle.toString());
        if (!ActionBarPolicy.get(this.mContext).isTitleEnableEllipsis() || zCanTitleBeShown) {
            return zCanTitleBeShown;
        }
        return true;
    }

    /* JADX WARN: Code duplicated, block: B:111:0x018f  */
    /* JADX WARN: Code duplicated, block: B:113:0x0195  */
    /* JADX WARN: Code duplicated, block: B:115:0x019d  */
    /* JADX WARN: Code duplicated, block: B:116:0x01a2  */
    /* JADX WARN: Code duplicated, block: B:120:0x01b9 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:121:0x01bb  */
    /* JADX WARN: Code duplicated, block: B:123:0x01c3  */
    /* JADX WARN: Code duplicated, block: B:125:0x01c7  */
    /* JADX WARN: Code duplicated, block: B:126:0x01c9 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:127:0x01cb  */
    /* JADX WARN: Code duplicated, block: B:128:0x01d5  */
    /* JADX WARN: Code duplicated, block: B:130:0x01f3  */
    /* JADX WARN: Code duplicated, block: B:131:0x0202  */
    /* JADX WARN: Code duplicated, block: B:138:0x021f  */
    /* JADX WARN: Code duplicated, block: B:139:0x0221  */
    /* JADX WARN: Code duplicated, block: B:147:0x0235  */
    /* JADX WARN: Code duplicated, block: B:149:0x0242  */
    /* JADX WARN: Code duplicated, block: B:183:0x02aa  */
    private void onLayoutCollapseViews(boolean z, int i, int i2, int i3, int i4, int i5) {
        HomeView homeView;
        int iPositionChild;
        int i6;
        int i7;
        int i8;
        int paddingBottom;
        View view;
        int iPositionChildWithOffset;
        boolean z2;
        int i9;
        int iPositionChild2;
        View view2;
        int measuredWidth;
        int i10;
        int iComputeTitleCenterLayoutStart;
        int measuredWidth2;
        int i11;
        int measuredWidth3;
        int measuredWidth4;
        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
        FrameLayout frameLayout = this.mMainContainer;
        FrameLayout frameLayout2 = this.mMediumContainer;
        FrameLayout frameLayout3 = this.mCollapseTabContainer;
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop2 = (i4 - getPaddingTop()) - getPaddingBottom();
        if (paddingTop2 <= 0) {
            return;
        }
        int i12 = i3 - i;
        int paddingEnd = i12 - getPaddingEnd();
        ActionMenuView actionMenuView = this.mEndMenuView;
        if (actionMenuView != null && actionMenuView.getParent() == this && this.mEndMenuView.getVisibility() != 8) {
            positionChildInverse(this.mEndMenuView, paddingEnd, paddingTop, paddingTop2);
            paddingEnd -= this.mEndMenuView.getMeasuredWidth();
        }
        View view3 = this.mEndView;
        if (view3 != null && view3.getVisibility() != 8) {
            positionChildInverse(this.mEndView, paddingEnd, paddingTop, paddingTop2);
            paddingEnd -= this.mEndView.getMeasuredWidth();
        }
        View view4 = this.mSearchView;
        if (view4 != null && view4.getVisibility() != 8) {
            int i13 = paddingEnd - this.mSearchViewMargin;
            positionChildInverse(this.mSearchView, i13, paddingTop, paddingTop2);
            paddingEnd = i13 - (this.mSearchView.getMeasuredWidth() + this.mSearchViewMargin);
        }
        ProgressBar progressBar = this.mIndeterminateProgressView;
        if (progressBar != null && progressBar.getVisibility() != 8) {
            positionChildInverse(this.mIndeterminateProgressView, paddingEnd - this.mProgressBarPadding, paddingTop, paddingTop2);
            paddingEnd -= this.mIndeterminateProgressView.getMeasuredWidth() - (this.mProgressBarPadding * 2);
        }
        int marginEnd = paddingEnd;
        boolean z3 = paddingTop2 > this.mTitleMinHeight;
        View view5 = this.mNavigatorSwitch;
        int iPositionChild3 = (view5 == null || view5.getVisibility() == 8) ? 0 : positionChild(this.mNavigatorSwitch, paddingStart, paddingTop, paddingTop2, z3);
        HomeView homeView2 = this.mExpandedActionView != null ? this.mExpandedHomeLayout : this.mHomeLayout;
        if (homeView2 != null && homeView2.getVisibility() == 0) {
            int startOffset = homeView2.getStartOffset();
            homeView = homeView2;
            iPositionChild = positionChild(homeView2, paddingStart + startOffset, paddingTop, paddingTop2, z3) + startOffset + paddingStart;
        } else {
            homeView = homeView2;
            View view6 = this.mStartView;
            iPositionChild = (view6 == null || view6.getVisibility() == 8) ? paddingStart : paddingStart + positionChild(this.mStartView, paddingStart, paddingTop, paddingTop2, z3);
        }
        if (this.mExpandedActionView == null) {
            boolean zIsShowTitle = isShowTitle();
            if ((zIsShowTitle || (getDisplayOptions() & 32) != 0) && (view = this.mTitleUpView) != null && view.getVisibility() == 0) {
                if (this.mHasStartView || this.mHasNavigatorSwitchView) {
                    iPositionChildWithOffset = positionChildWithOffset(this.mTitleUpView, iPositionChild, paddingTop, paddingTop2, z3, this.mTitleUpViewMarginStart);
                } else {
                    iPositionChildWithOffset = positionChild(this.mTitleUpView, iPositionChild, paddingTop, paddingTop2, z3);
                }
                iPositionChild += iPositionChildWithOffset;
            }
            boolean z4 = (zIsShowTitle || this.mInnerExpandState == 3) && frameLayout != null && frameLayout.getChildCount() > 0;
            int i14 = iPositionChild + iPositionChild3;
            int iMax = this.mTitleGapPaddingStart + i14;
            View view7 = this.mTitleUpView;
            boolean z5 = view7 != null && view7.getVisibility() == 0;
            boolean z6 = homeView != null && homeView.getVisibility() == 0;
            View view8 = this.mStartView;
            boolean z7 = (view8 == null || view8.getVisibility() == 8) ? false : true;
            View view9 = this.mNavigatorSwitch;
            if (view9 != null) {
                z2 = z4;
                boolean z8 = view9.getVisibility() != 8;
                if (!z5 && !z6 && !z7 && !z8) {
                    iMax = Math.max(this.mMediumTitlePaddingStartNoUp, iMax);
                }
                i9 = iMax;
                if (z2) {
                    i6 = paddingTop2;
                    iPositionChild2 = iPositionChild;
                } else if (isTitleCenter()) {
                    if (frameLayout.getVisibility() != 8) {
                        iComputeTitleCenterLayoutStart = computeTitleCenterLayoutStart(frameLayout);
                    } else {
                        iComputeTitleCenterLayoutStart = i14;
                    }
                    this.mCollapseTitleShowable = canCollapseTitleBeShown();
                    updateTightTitle();
                    measuredWidth2 = frameLayout.getMeasuredWidth() + iComputeTitleCenterLayoutStart;
                    if (frameLayout.getMeasuredWidth() + i14 <= marginEnd) {
                        i11 = i14;
                        measuredWidth3 = marginEnd;
                    } else if (measuredWidth2 > marginEnd) {
                        measuredWidth4 = marginEnd - frameLayout.getMeasuredWidth();
                        if (measuredWidth4 < i14) {
                            i11 = i14;
                        } else {
                            i11 = measuredWidth4;
                        }
                        measuredWidth3 = marginEnd;
                    } else if (iComputeTitleCenterLayoutStart < i14) {
                        measuredWidth3 = i14 + frameLayout.getMeasuredWidth();
                        i11 = i14;
                    } else {
                        i11 = iComputeTitleCenterLayoutStart;
                        measuredWidth3 = measuredWidth2;
                    }
                    int measuredHeight = frameLayout.getMeasuredHeight();
                    int i15 = ((paddingTop2 - measuredHeight) / 2) + paddingTop;
                    int i16 = measuredHeight + i15;
                    i6 = paddingTop2;
                    iPositionChild2 = i14;
                    ViewUtils.layoutChildView(this, frameLayout, i11, i15, measuredWidth3, i16);
                } else {
                    i6 = paddingTop2;
                    iPositionChild2 = i14 + positionChild(frameLayout, i14 + this.mTitleGapPaddingStart, paddingTop, i6);
                }
                if (zIsShowTitle && frameLayout2 != null && frameLayout2.getChildCount() > 0) {
                    int measuredHeight2 = frameLayout2.getMeasuredHeight();
                    int i17 = paddingTop + ((i6 - measuredHeight2) / 2);
                    measuredWidth = frameLayout2.getMeasuredWidth() + i9;
                    if (measuredWidth > marginEnd) {
                        i10 = marginEnd;
                    } else {
                        i10 = measuredWidth;
                    }
                    ViewUtils.layoutChildView(this, frameLayout2, i9, i17, i10, i17 + measuredHeight2);
                }
                if (this.mNavigationMode == 1 || (view2 = this.mListNavLayout) == null) {
                    iPositionChild = iPositionChild2;
                } else {
                    if (zIsShowTitle) {
                        iPositionChild2 += this.mItemPadding;
                    }
                    iPositionChild = iPositionChild2 + positionChild(view2, iPositionChild2, paddingTop, i6) + this.mItemPadding;
                }
            } else {
                z2 = z4;
            }
            if (!z5) {
                iMax = Math.max(this.mMediumTitlePaddingStartNoUp, iMax);
            }
            i9 = iMax;
            if (z2) {
                i6 = paddingTop2;
                iPositionChild2 = iPositionChild;
            } else if (isTitleCenter()) {
                if (frameLayout.getVisibility() != 8) {
                    iComputeTitleCenterLayoutStart = computeTitleCenterLayoutStart(frameLayout);
                } else {
                    iComputeTitleCenterLayoutStart = i14;
                }
                this.mCollapseTitleShowable = canCollapseTitleBeShown();
                updateTightTitle();
                measuredWidth2 = frameLayout.getMeasuredWidth() + iComputeTitleCenterLayoutStart;
                if (frameLayout.getMeasuredWidth() + i14 <= marginEnd) {
                    i11 = i14;
                    measuredWidth3 = marginEnd;
                } else if (measuredWidth2 > marginEnd) {
                    measuredWidth4 = marginEnd - frameLayout.getMeasuredWidth();
                    if (measuredWidth4 < i14) {
                        i11 = i14;
                    } else {
                        i11 = measuredWidth4;
                    }
                    measuredWidth3 = marginEnd;
                } else if (iComputeTitleCenterLayoutStart < i14) {
                    measuredWidth3 = i14 + frameLayout.getMeasuredWidth();
                    i11 = i14;
                } else {
                    i11 = iComputeTitleCenterLayoutStart;
                    measuredWidth3 = measuredWidth2;
                }
                int measuredHeight3 = frameLayout.getMeasuredHeight();
                int i18 = ((paddingTop2 - measuredHeight3) / 2) + paddingTop;
                int i19 = measuredHeight3 + i18;
                i6 = paddingTop2;
                iPositionChild2 = i14;
                ViewUtils.layoutChildView(this, frameLayout, i11, i18, measuredWidth3, i19);
            } else {
                i6 = paddingTop2;
                iPositionChild2 = i14 + positionChild(frameLayout, i14 + this.mTitleGapPaddingStart, paddingTop, i6);
            }
            if (zIsShowTitle) {
                int measuredHeight4 = frameLayout2.getMeasuredHeight();
                int i110 = paddingTop + ((i6 - measuredHeight4) / 2);
                measuredWidth = frameLayout2.getMeasuredWidth() + i9;
                if (measuredWidth > marginEnd) {
                    i10 = marginEnd;
                } else {
                    i10 = measuredWidth;
                }
                ViewUtils.layoutChildView(this, frameLayout2, i9, i110, i10, i110 + measuredHeight4);
            }
            if (this.mNavigationMode == 1) {
                iPositionChild = iPositionChild2;
            } else {
                if (zIsShowTitle) {
                    iPositionChild2 += this.mItemPadding;
                }
                iPositionChild = iPositionChild2 + positionChild(view2, iPositionChild2, paddingTop, i6) + this.mItemPadding;
            }
        } else {
            paddingTop = paddingTop;
            i6 = paddingTop2;
        }
        View view10 = this.mExpandedActionView;
        if (view10 == null && ((this.mDisplayOptions & 16) == 0 || (view10 = this.mCustomNavView) == null)) {
            view10 = null;
        }
        if (view10 != null && view10.getVisibility() != 8) {
            ViewGroup.LayoutParams layoutParams = view10.getLayoutParams();
            ActionBar.LayoutParams layoutParams2 = layoutParams instanceof ActionBar.LayoutParams ? (ActionBar.LayoutParams) layoutParams : null;
            int i20 = layoutParams2 != null ? layoutParams2.gravity : 8388627;
            int measuredWidth5 = view10.getMeasuredWidth();
            if (layoutParams2 != null) {
                iPositionChild += layoutParams2.getMarginStart();
                marginEnd -= layoutParams2.getMarginEnd();
                i8 = ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin;
                i7 = ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin;
            } else {
                i7 = 0;
                i8 = 0;
            }
            int i21 = 8388615 & i20;
            if (i21 == 1) {
                int width = (getWidth() - measuredWidth5) / 2;
                if (width < iPositionChild) {
                    i21 = 8388611;
                } else if (width + measuredWidth5 > marginEnd) {
                    i21 = 8388613;
                }
            } else if (i20 == -1 || i21 == 0) {
                i21 = 8388611;
            }
            int paddingStart2 = getPaddingStart();
            int iNormalizeHorizontalGravity = normalizeHorizontalGravity(i21, zIsLayoutRtl);
            if (iNormalizeHorizontalGravity == 1) {
                paddingStart2 = (getWidth() - measuredWidth5) / 2;
            } else if (iNormalizeHorizontalGravity == 8388611) {
                paddingStart2 = iPositionChild;
            } else if (iNormalizeHorizontalGravity == 8388613) {
                paddingStart2 = marginEnd - measuredWidth5;
            }
            int i22 = i20 & 112;
            if (i20 == -1 || i22 == 0) {
                i22 = 16;
            }
            if (i22 == 16) {
                paddingBottom = ((((this.mCollapseTotalHeight - i5) - getPaddingBottom()) - getPaddingTop()) - view10.getMeasuredHeight()) / 2;
            } else if (i22 == 48) {
                paddingBottom = getPaddingTop() + i8;
            } else {
                paddingBottom = i22 != 80 ? 0 : (((this.mCollapseTotalHeight - i5) - getPaddingBottom()) - view10.getMeasuredHeight()) - i7;
            }
            int measuredWidth6 = view10.getMeasuredWidth();
            view10.layout(zIsLayoutRtl ? (getWidth() - paddingStart2) - measuredWidth6 : paddingStart2, paddingBottom, zIsLayoutRtl ? getWidth() - paddingStart2 : measuredWidth6 + paddingStart2, view10.getMeasuredHeight() + paddingBottom);
        }
        int i23 = iPositionChild;
        ProgressBar progressBar2 = this.mProgressView;
        if (progressBar2 != null) {
            progressBar2.bringToFront();
            int measuredHeight5 = this.mProgressView.getMeasuredHeight() / 2;
            ProgressBar progressBar3 = this.mProgressView;
            int i24 = this.mProgressBarPadding;
            progressBar3.layout(i24, -measuredHeight5, progressBar3.getMeasuredWidth() + i24, measuredHeight5);
        }
        if (i5 > 0) {
            int i25 = this.mExtraPadding + this.mUncollapseTabPaddingH;
            ViewUtils.layoutChildView(this, frameLayout3, i25, i4, i25 + frameLayout3.getMeasuredWidth(), i4 + i5);
        }
        View view11 = this.mCenterView;
        if (view11 == null || view11.getVisibility() == 8) {
            return;
        }
        int measuredWidth7 = this.mCenterView.getMeasuredWidth();
        int measuredHeight6 = this.mCenterView.getMeasuredHeight();
        int i26 = (i12 - measuredWidth7) / 2;
        int i27 = i26 + measuredWidth7;
        if (i27 > marginEnd) {
            i26 = marginEnd - measuredWidth7;
        } else {
            marginEnd = i27;
        }
        if (i26 < i23) {
            marginEnd = i23 + measuredWidth7;
        } else {
            i23 = i26;
        }
        int i28 = paddingTop + ((i6 - measuredHeight6) / 2);
        ViewUtils.layoutChildView(this, this.mCenterView, i23, i28, marginEnd, measuredHeight6 + i28);
    }

    private void notifyMenuStateChange() {
        if (!this.mSplitActionBarEnable || this.mMenuView == null) {
            return;
        }
        ((ActionBarOverlayLayout) this.mSplitView.getParent()).onMenuStateChanged((int) (this.mMenuView.getCollapsedHeight() - this.mMenuView.getTranslationY()), 0);
    }

    protected void onLayoutExpandViews(boolean z, int i, int i2, int i3, int i4, int i5, float f) {
        if (hasTitle()) {
            FrameLayout frameLayout = this.mMovableMainContainer;
            FrameLayout frameLayout2 = this.mMovableTabContainer;
            int measuredHeight = (frameLayout == null || frameLayout.getVisibility() != 0) ? 0 : frameLayout.getMeasuredHeight();
            int i6 = this.mMovableSecondaryTabHeight;
            if (frameLayout != null && frameLayout.getVisibility() == 0 && this.mInnerExpandState != 0) {
                frameLayout.layout(i, i4 - measuredHeight, i3, i4);
                if (hasTabsInContainer(this.mMovableMainContainer)) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.mMovableMainContainer.getChildAt(0));
                }
            }
            if (i6 <= 0 || this.mInnerExpandState == 0) {
                return;
            }
            int i7 = i + this.mUncollapseTabPaddingH + this.mExtraPadding;
            int i8 = i4 + i5;
            ViewUtils.layoutChildView(this, frameLayout2, i7, i8 - i6, i7 + frameLayout2.getMeasuredWidth(), i8);
            if (hasTabsInContainer(frameLayout2)) {
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(frameLayout2.getChildAt(0));
            }
        }
    }

    private int computeTitleCenterLayoutStart(View view) {
        int width = (getWidth() - view.getMeasuredWidth()) / 2;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        return width - (layoutParams instanceof LinearLayout.LayoutParams ? ((LinearLayout.LayoutParams) layoutParams).getMarginStart() : 0);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ActionBar.LayoutParams(getContext(), attributeSet);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams == null ? generateDefaultLayoutParams() : layoutParams;
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        MenuItemImpl menuItemImpl;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (expandedActionViewMenuPresenter != null && (menuItemImpl = expandedActionViewMenuPresenter.mCurrentExpandedItem) != null) {
            savedState.expandedMenuItemId = menuItemImpl.getItemId();
        } else {
            savedState.expandedMenuItemId = 0;
        }
        savedState.isOverflowOpen = isOverflowMenuShowing();
        savedState.isEndOverflowOpen = isEndOverflowMenuShowing();
        int i = this.mInnerExpandState;
        if (i == 3) {
            savedState.expandState = 0;
        } else {
            savedState.expandState = i;
        }
        savedState.userSetExpandState = this.mUserSetExpandState;
        savedState.userExpandState = this.mUserExpandState;
        savedState.applyBlur = this.mApplyBgBlur;
        return savedState;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        MenuBuilder menuBuilder;
        MenuItem menuItemFindItem;
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            int i = savedState.expandedMenuItemId;
            if (i != 0 && this.mExpandedMenuPresenter != null && (menuBuilder = this.mOptionsMenu) != null && (menuItemFindItem = menuBuilder.findItem(i)) != null) {
                menuItemFindItem.expandActionView();
            }
            if (savedState.isOverflowOpen) {
                postShowOverflowMenu();
            }
            if (savedState.isEndOverflowOpen) {
                postShowEndOverflowMenu();
            }
            if (this.mUserExpandState == -1) {
                this.mUserSetExpandState = savedState.userSetExpandState;
                this.mUserExpandState = savedState.userExpandState;
                setExpandState(isUserSetExpandState() ? this.mUserExpandState : savedState.expandState, false, false);
            }
            if (savedState.applyBlur) {
                setApplyBgBlur(this.mApplyBgBlur);
                return;
            }
            return;
        }
        Log.w("ActionBarView", "Wrong state class, expecting SavedState! This usually happens when two views of different type have the same id in the same hierarchy.");
        super.onRestoreInstanceState(parcelable);
    }

    class SavedState extends View.BaseSavedState {
        public static final Parcelable.ClassLoaderCreator CREATOR = new Parcelable.ClassLoaderCreator() { // from class: miuix.appcompat.internal.app.widget.ActionBarView.SavedState.1
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
        boolean applyBlur;
        int expandState;
        int expandedMenuItemId;
        boolean isEndOverflowOpen;
        boolean isOverflowOpen;
        int userExpandState;
        boolean userSetExpandState;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        SavedState(Parcel parcel) {
            super(parcel);
            this.expandedMenuItemId = parcel.readInt();
            this.isOverflowOpen = parcel.readInt() != 0;
            this.isEndOverflowOpen = parcel.readInt() != 0;
            this.expandState = parcel.readInt();
            this.userSetExpandState = parcel.readInt() != 0;
            this.userExpandState = parcel.readInt();
            this.applyBlur = parcel.readInt() != 0;
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.expandedMenuItemId = parcel.readInt();
            this.isOverflowOpen = parcel.readInt() != 0;
            this.isEndOverflowOpen = parcel.readInt() != 0;
            this.expandState = parcel.readInt();
            this.userSetExpandState = parcel.readInt() != 0;
            this.userExpandState = parcel.readInt();
            this.applyBlur = parcel.readInt() != 0;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.expandedMenuItemId);
            parcel.writeInt(this.isOverflowOpen ? 1 : 0);
            parcel.writeInt(this.isEndOverflowOpen ? 1 : 0);
            parcel.writeInt(this.expandState);
            parcel.writeInt(this.userSetExpandState ? 1 : 0);
            parcel.writeInt(this.userExpandState);
            parcel.writeInt(this.applyBlur ? 1 : 0);
        }
    }

    class HomeView extends FrameLayout {
        private Drawable mDefaultUpIndicator;
        private int mHorizontalPadding;
        private ImageView mIconView;
        private int mUpIndicatorRes;
        private ImageView mUpView;

        public int getStartOffset() {
            return 0;
        }

        public HomeView(Context context) {
            this(context, null);
        }

        public HomeView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mHorizontalPadding = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_view_padding_horizontal);
        }

        public void setUp(boolean z) {
            this.mUpView.setVisibility(z ? 0 : 8);
        }

        public void setIcon(Drawable drawable) {
            this.mIconView.setImageDrawable(drawable);
        }

        public void setUpIndicator(Drawable drawable) {
            ImageView imageView = this.mUpView;
            if (drawable == null) {
                drawable = this.mDefaultUpIndicator;
            }
            imageView.setImageDrawable(drawable);
            this.mUpIndicatorRes = 0;
        }

        public void setUpIndicator(int i) {
            this.mUpIndicatorRes = i;
            this.mUpView.setImageDrawable(i != 0 ? getResources().getDrawable(i) : null);
        }

        public View getUp() {
            return this.mUpView;
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            int i = this.mUpIndicatorRes;
            if (i != 0) {
                setUpIndicator(i);
            }
        }

        @Override // android.view.View
        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            CharSequence contentDescription = getContentDescription();
            if (TextUtils.isEmpty(contentDescription)) {
                return true;
            }
            accessibilityEvent.getText().add(contentDescription);
            return true;
        }

        @Override // android.view.View
        protected void onFinishInflate() {
            super.onFinishInflate();
            this.mUpView = (ImageView) findViewById(miuix.appcompat.R$id.up);
            this.mIconView = (ImageView) findViewById(miuix.appcompat.R$id.home);
            ImageView imageView = this.mUpView;
            if (imageView != null) {
                this.mDefaultUpIndicator = imageView.getDrawable();
                Folme.useAt(this.mUpView).hover().setFeedbackRadius(60.0f);
                Folme.useAt(this.mUpView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED_WRAPPED).handleHoverOf(this.mUpView, new AnimConfig[0]);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            measureChildWithMargins(this.mUpView, i, 0, i2, 0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mUpView.getLayoutParams();
            int measuredWidth = this.mUpView.getVisibility() == 8 ? 0 : layoutParams.leftMargin + this.mUpView.getMeasuredWidth() + layoutParams.rightMargin;
            int measuredHeight = layoutParams.topMargin + this.mUpView.getMeasuredHeight() + layoutParams.bottomMargin;
            measureChildWithMargins(this.mIconView, i, measuredWidth, i2, 0);
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mIconView.getLayoutParams();
            int measuredWidth2 = measuredWidth + (this.mIconView.getVisibility() != 8 ? layoutParams2.leftMargin + this.mIconView.getMeasuredWidth() + layoutParams2.rightMargin : 0);
            if (measuredWidth2 > 0) {
                measuredWidth2 += this.mHorizontalPadding;
            }
            int iMax = Math.max(measuredHeight, layoutParams2.topMargin + this.mIconView.getMeasuredHeight() + layoutParams2.bottomMargin);
            int mode = View.MeasureSpec.getMode(i);
            int mode2 = View.MeasureSpec.getMode(i2);
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            if (mode == Integer.MIN_VALUE) {
                measuredWidth2 = Math.min(measuredWidth2, size);
            } else if (mode == 1073741824) {
                measuredWidth2 = size;
            }
            if (mode2 == Integer.MIN_VALUE) {
                iMax = Math.min(iMax, size2);
            } else if (mode2 == 1073741824) {
                iMax = size2;
            }
            setMeasuredDimension(measuredWidth2, iMax);
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            int i9 = (i4 - i2) / 2;
            boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
            if (this.mUpView.getVisibility() != 8) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mUpView.getLayoutParams();
                int measuredHeight = this.mUpView.getMeasuredHeight();
                int measuredWidth = this.mUpView.getMeasuredWidth();
                int i10 = i9 - (measuredHeight / 2);
                ViewUtils.layoutChildView(this, this.mUpView, 0, i10, measuredWidth, measuredHeight + i10);
                i5 = layoutParams.leftMargin + measuredWidth + layoutParams.rightMargin;
                if (zIsLayoutRtl) {
                    i7 = i5;
                    i8 = i3 - i5;
                    i6 = i;
                } else {
                    i6 = i + i5;
                }
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mIconView.getLayoutParams();
                int measuredHeight2 = this.mIconView.getMeasuredHeight();
                int measuredWidth2 = this.mIconView.getMeasuredWidth();
                int iMax = i7 + Math.max(layoutParams2.getMarginStart(), (((i8 - i6) - this.mHorizontalPadding) / 2) - (measuredWidth2 / 2));
                int iMax2 = Math.max(layoutParams2.topMargin, i9 - (measuredHeight2 / 2));
                ViewUtils.layoutChildView(this, this.mIconView, iMax, iMax2, iMax + measuredWidth2, measuredHeight2 + iMax2);
            }
            i5 = 0;
            i6 = i;
            i7 = i5;
            i8 = i3;
            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.mIconView.getLayoutParams();
            int measuredHeight3 = this.mIconView.getMeasuredHeight();
            int measuredWidth3 = this.mIconView.getMeasuredWidth();
            int iMax3 = i7 + Math.max(layoutParams3.getMarginStart(), (((i8 - i6) - this.mHorizontalPadding) / 2) - (measuredWidth3 / 2));
            int iMax4 = Math.max(layoutParams3.topMargin, i9 - (measuredHeight3 / 2));
            ViewUtils.layoutChildView(this, this.mIconView, iMax3, iMax4, iMax3 + measuredWidth3, measuredHeight3 + iMax4);
        }
    }

    class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter
        public boolean flagActionItems() {
            return false;
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter
        public int getId() {
            return 0;
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter
        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter
        public Parcelable onSaveInstanceState() {
            return null;
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter
        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            return false;
        }

        private ExpandedActionViewMenuPresenter() {
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter
        public void initForMenu(Context context, MenuBuilder menuBuilder) {
            MenuItemImpl menuItemImpl;
            MenuBuilder menuBuilder2 = this.mMenu;
            if (menuBuilder2 != null && (menuItemImpl = this.mCurrentExpandedItem) != null) {
                menuBuilder2.collapseItemActionView(menuItemImpl);
            }
            this.mMenu = menuBuilder;
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter
        public void updateMenuView(boolean z) {
            if (this.mCurrentExpandedItem != null) {
                MenuBuilder menuBuilder = this.mMenu;
                if (menuBuilder != null) {
                    int size = menuBuilder.size();
                    for (int i = 0; i < size; i++) {
                        if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                            return;
                        }
                    }
                }
                collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
            }
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter
        public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            ActionBarView.this.mExpandedActionView = menuItemImpl.getActionView();
            ActionBarView.this.initExpandedHomeLayout();
            ActionBarView.this.mExpandedHomeLayout.setIcon(ActionBarView.this.getIcon().getConstantState().newDrawable(ActionBarView.this.getResources()));
            this.mCurrentExpandedItem = menuItemImpl;
            ViewParent parent = ActionBarView.this.mExpandedActionView.getParent();
            ActionBarView actionBarView = ActionBarView.this;
            if (parent != actionBarView) {
                actionBarView.addView(actionBarView.mExpandedActionView);
            }
            ViewParent parent2 = ActionBarView.this.mExpandedHomeLayout.getParent();
            ActionBarView actionBarView2 = ActionBarView.this;
            if (parent2 != actionBarView2) {
                actionBarView2.addView(actionBarView2.mExpandedHomeLayout);
            }
            if (ActionBarView.this.mHomeLayout != null) {
                ActionBarView.this.mHomeLayout.setVisibility(8);
            }
            if (ActionBarView.this.mCollapseTitle != null) {
                ActionBarView.this.setTitleVisibility(false);
            }
            ActionBarView.access$2400(ActionBarView.this);
            ActionBarView.access$2500(ActionBarView.this);
            ActionBarView.access$2600(ActionBarView.this);
            ActionBarView.access$2700(ActionBarView.this);
            if (ActionBarView.this.mSpinner != null) {
                ActionBarView.this.mSpinner.setVisibility(8);
            }
            if (ActionBarView.this.mCustomNavView != null) {
                ActionBarView.this.mCustomNavView.setVisibility(8);
            }
            ActionBarView.this.requestLayout();
            menuItemImpl.setActionViewExpanded(true);
            KeyEvent.Callback callback = ActionBarView.this.mExpandedActionView;
            if (callback instanceof CollapsibleActionView) {
                ((CollapsibleActionView) callback).onActionViewExpanded();
            }
            ActionBarView.this.updateBackInvokedCallbackState();
            return true;
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter
        public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            KeyEvent.Callback callback = ActionBarView.this.mExpandedActionView;
            if (callback instanceof CollapsibleActionView) {
                ((CollapsibleActionView) callback).onActionViewCollapsed();
            }
            ActionBarView actionBarView = ActionBarView.this;
            actionBarView.removeView(actionBarView.mExpandedActionView);
            ActionBarView actionBarView2 = ActionBarView.this;
            actionBarView2.removeView(actionBarView2.mExpandedHomeLayout);
            ActionBarView actionBarView3 = ActionBarView.this;
            actionBarView3.mExpandedActionView = null;
            if ((actionBarView3.mDisplayOptions & 2) != 0) {
                ActionBarView.this.mHomeLayout.setVisibility(0);
            }
            if ((ActionBarView.this.mDisplayOptions & 8) != 0) {
                CollapseTitle collapseTitle = ActionBarView.this.mCollapseTitle;
                ActionBarView actionBarView4 = ActionBarView.this;
                if (collapseTitle == null) {
                    actionBarView4.initTitle();
                } else {
                    actionBarView4.setTitleVisibility(true);
                }
            }
            ActionBarView.access$2400(ActionBarView.this);
            ActionBarView.access$2500(ActionBarView.this);
            ActionBarView.access$2600(ActionBarView.this);
            ActionBarView.access$2700(ActionBarView.this);
            if (ActionBarView.this.mSpinner != null && ActionBarView.this.mNavigationMode == 1) {
                ActionBarView.this.mSpinner.setVisibility(0);
            }
            if (ActionBarView.this.mCustomNavView != null && (ActionBarView.this.mDisplayOptions & 16) != 0) {
                ActionBarView.this.mCustomNavView.setVisibility(0);
            }
            ActionBarView.this.mExpandedHomeLayout.setIcon(null);
            this.mCurrentExpandedItem = null;
            ActionBarView.this.requestLayout();
            menuItemImpl.setActionViewExpanded(false);
            ActionBarView.this.updateBackInvokedCallbackState();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTitleVisibility(boolean z) {
        CollapseTitle collapseTitle = this.mCollapseTitle;
        if (collapseTitle != null) {
            collapseTitle.setVisibility(z ? 0 : 8);
        }
        MediumTitle mediumTitle = this.mMediumTitle;
        if (mediumTitle != null) {
            mediumTitle.setVisibility(z ? 0 : 8);
        }
        ExpandTitle expandTitle = this.mExpandTitle;
        if (expandTitle != null) {
            expandTitle.setVisibility(z ? 0 : 4);
        }
        updateMovableContainerVisibility(z);
        updateTitleUpViewVisibility();
        int i = TextUtils.isEmpty(this.mSubtitle) ? this.mExpandTitlePaddingBottom : this.mExpandSubtitlePaddingBottom;
        FrameLayout frameLayout = this.mMovableMainContainer;
        frameLayout.setPaddingRelative(frameLayout.getPaddingStart(), this.mMovableMainContainer.getPaddingTop(), this.mMovableMainContainer.getPaddingEnd(), i);
    }

    private void updateMovableContainerVisibility(boolean z) {
        if (this.mMovableController == null) {
            return;
        }
        if (isAllTitlesEmpty()) {
            this.mMovableController.setVisibility(8);
            return;
        }
        if (z) {
            int i = this.mInnerExpandState;
            if (i == 0 || i == 1) {
                this.mMovableController.setVisibility(8);
                this.mMovableController.setAnimFrom(0.0f, 0, 0, true);
            } else {
                this.mMovableController.setVisibility(0);
            }
        }
    }

    private void ensureCollapseTabContainerVisibleForMedium() {
        FrameLayout frameLayout = this.mCollapseTabContainer;
        if (frameLayout == null || frameLayout.getParent() != this) {
            return;
        }
        this.mCollapseTabContainer.setVisibility(0);
        this.mCollapseTabContainer.setAlpha(1.0f);
        MiuiBlurUtils.setSelfBlur(this.mCollapseTabContainer, 0, null);
        if (this.mCollapseTabContainer.isAttachedToWindow()) {
            Folme.useAt(this.mCollapseTabContainer).state().setTo(new AnimState("collapseTabVisible").add(ViewProperty.ALPHA, 1.0d).add(SELF_BLUR, 0.0d));
        }
    }

    private void updateTitleUpViewVisibility() {
        if (this.mTitleUpView == null || (getDisplayOptions() & 32) != 0) {
            return;
        }
        int i = this.mDisplayOptions;
        boolean z = (i & 4) != 0;
        int i2 = i & 2;
        View view = this.mTitleUpView;
        if (i2 != 0) {
            view.setVisibility(8);
        } else {
            view.setVisibility(z ? 0 : 4);
        }
    }

    protected ActionMenuPresenter createActionMenuPresenter(MenuPresenter.Callback callback, boolean z) {
        ActionMenuPresenter actionMenuPresenter;
        ActionBarOverlayLayout actionBarOverlayLayoutFindActionBarOverlayLayout = findActionBarOverlayLayout();
        Context context = this.mContext;
        if (z) {
            actionMenuPresenter = new HyperSplitActionMenuPresenter(context, actionBarOverlayLayoutFindActionBarOverlayLayout, R$layout.miuix_appcompat_responsive_action_menu_layout, R$layout.miuix_appcompat_action_menu_item_layout);
        } else {
            actionMenuPresenter = new ActionMenuPresenter(context, actionBarOverlayLayoutFindActionBarOverlayLayout, R$layout.miuix_appcompat_responsive_action_menu_layout, R$layout.miuix_appcompat_action_menu_item_layout);
        }
        actionMenuPresenter.setCallback(callback);
        actionMenuPresenter.setId(miuix.appcompat.R$id.action_menu_presenter);
        return actionMenuPresenter;
    }

    protected EndActionMenuPresenter createEndActionMenuPresenter(MenuPresenter.Callback callback, boolean z) {
        EndActionMenuPresenter endActionMenuPresenter;
        ActionBarOverlayLayout actionBarOverlayLayoutFindActionBarOverlayLayout = findActionBarOverlayLayout();
        Context context = this.mContext;
        if (z) {
            endActionMenuPresenter = new HyperActionMenuPresenter(context, actionBarOverlayLayoutFindActionBarOverlayLayout, R$layout.miuix_appcompat_action_end_menu_layout, R$layout.miuix_appcompat_action_end_menu_item_layout, R$layout.miuix_appcompat_action_bar_expanded_menu_layout, R$layout.miuix_appcompat_action_bar_list_menu_item_layout);
        } else {
            endActionMenuPresenter = new EndActionMenuPresenter(context, actionBarOverlayLayoutFindActionBarOverlayLayout, R$layout.miuix_appcompat_action_end_menu_layout, R$layout.miuix_appcompat_action_end_menu_item_layout, R$layout.miuix_appcompat_action_bar_expanded_menu_layout, R$layout.miuix_appcompat_action_bar_list_menu_item_layout);
        }
        endActionMenuPresenter.setCallback(callback);
        endActionMenuPresenter.setId(miuix.appcompat.R$id.miuix_action_end_menu_presenter);
        return endActionMenuPresenter;
    }

    private ActionBarOverlayLayout findActionBarOverlayLayout() {
        Object parent = getParent();
        while (true) {
            View view = (View) parent;
            if (!(view instanceof ActionBarOverlayLayout)) {
                if (view.getParent() instanceof View) {
                    parent = view.getParent();
                } else {
                    OrtUtil$$ExternalSyntheticBUOutline0.m("ActionBarOverlayLayout not found");
                    return null;
                }
            } else {
                return (ActionBarOverlayLayout) view;
            }
        }
    }

    protected ExpandedActionViewMenuPresenter createExpandedActionViewMenuPresenter() {
        return new ExpandedActionViewMenuPresenter();
    }

    public void setProgressBarIndeterminateVisibility(boolean z) {
        updateProgressBars(z ? -1 : -2);
    }

    private void updateProgressBars(int i) {
        ProgressBar circularProgressBar = getCircularProgressBar();
        ProgressBar horizontalProgressBar = getHorizontalProgressBar();
        if (i == -1) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setVisibility((horizontalProgressBar.isIndeterminate() || horizontalProgressBar.getProgress() < 10000) ? 0 : 4);
            }
            if (circularProgressBar != null) {
                circularProgressBar.setVisibility(0);
                return;
            }
            return;
        }
        if (i == -2) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setVisibility(8);
            }
            if (circularProgressBar != null) {
                circularProgressBar.setVisibility(8);
                return;
            }
            return;
        }
        if (i == -3) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setIndeterminate(true);
            }
        } else if (i == -4) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setIndeterminate(false);
            }
        } else {
            if (i < 0 || i > 10000 || horizontalProgressBar == null) {
                return;
            }
            horizontalProgressBar.setProgress(i);
            if (i < 10000) {
                showProgressBars(horizontalProgressBar, circularProgressBar);
            } else {
                hideProgressBars(horizontalProgressBar, circularProgressBar);
            }
        }
    }

    private void showProgressBars(ProgressBar progressBar, ProgressBar progressBar2) {
        if (progressBar2 != null && progressBar2.getVisibility() == 4) {
            progressBar2.setVisibility(0);
        }
        if (progressBar == null || progressBar.getProgress() >= 10000) {
            return;
        }
        progressBar.setVisibility(0);
    }

    private void hideProgressBars(ProgressBar progressBar, ProgressBar progressBar2) {
        if (progressBar2 != null && progressBar2.getVisibility() == 0) {
            progressBar2.setVisibility(4);
        }
        if (progressBar == null || progressBar.getVisibility() != 0) {
            return;
        }
        progressBar.setVisibility(4);
    }

    private ProgressBar getCircularProgressBar() {
        ProgressBar progressBar = this.mIndeterminateProgressView;
        if (progressBar != null) {
            progressBar.setVisibility(4);
        }
        return progressBar;
    }

    private ProgressBar getHorizontalProgressBar() {
        ProgressBar progressBar = this.mProgressView;
        if (progressBar != null) {
            progressBar.setVisibility(4);
        }
        return progressBar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable getIcon() {
        if ((this.mIconLogoInitIndicator & 1) != 1) {
            Context context = this.mContext;
            if (context instanceof Activity) {
                try {
                    this.mIcon = context.getPackageManager().getActivityIcon(((Activity) this.mContext).getComponentName());
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e("ActionBarView", "Activity component name not found!", e);
                }
            }
            if (this.mIcon == null) {
                this.mIcon = this.mContext.getApplicationInfo().loadIcon(this.mContext.getPackageManager());
            }
            this.mIconLogoInitIndicator |= 1;
        }
        return this.mIcon;
    }

    private Drawable getLogo() {
        if ((this.mIconLogoInitIndicator & 2) != 2) {
            Context context = this.mContext;
            if (context instanceof Activity) {
                try {
                    this.mLogo = context.getPackageManager().getActivityLogo(((Activity) this.mContext).getComponentName());
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e("ActionBarView", "Activity component name not found!", e);
                }
            }
            if (this.mLogo == null) {
                this.mLogo = this.mContext.getApplicationInfo().loadLogo(this.mContext.getPackageManager());
            }
            this.mIconLogoInitIndicator |= 2;
        }
        return this.mLogo;
    }

    private void initHomeLayout() {
        if (this.mHomeLayout == null) {
            HomeView homeView = (HomeView) LayoutInflater.from(this.mContext).inflate(this.mHomeResId, (ViewGroup) this, false);
            this.mHomeLayout = homeView;
            homeView.setOnClickListener(this.mUpClickListener);
            this.mHomeLayout.setClickable(true);
            this.mHomeLayout.setFocusable(true);
            int i = this.mHomeAsUpIndicatorResId;
            if (i != 0) {
                this.mHomeLayout.setUpIndicator(i);
                this.mHomeAsUpIndicatorResId = 0;
            }
            Drawable drawable = this.mHomeAsUpIndicatorDrawable;
            if (drawable != null) {
                this.mHomeLayout.setUpIndicator(drawable);
                this.mHomeAsUpIndicatorDrawable = null;
            }
            Boolean bool = this.mFloatingMode;
            if (bool != null && bool.booleanValue()) {
                FloatingEffectHelper.updateViewFloatingState(this.mHomeLayout.getUp(), true, MiuixUIUtils.isLightTheme(getContext()), this.mActionBarButtonMaterial, this.mFloatingBgColor);
            }
            addView(this.mHomeLayout);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initExpandedHomeLayout() {
        if (this.mExpandedHomeLayout == null) {
            HomeView homeView = (HomeView) LayoutInflater.from(this.mContext).inflate(this.mHomeResId, (ViewGroup) this, false);
            this.mExpandedHomeLayout = homeView;
            homeView.setUp(true);
            this.mExpandedHomeLayout.setOnClickListener(this.mExpandedActionViewUpListener);
        }
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    protected void onExpandStateChanged(int i, int i2) {
        AbsActionBarView.CollapseView collapseView;
        if (i == 3) {
            this.mPendingHeight = 0;
            if (!this.mPostScroller.isFinished()) {
                this.mPostScroller.forceFinished(true);
            }
        }
        if (i2 == 3 && (collapseView = this.mMovableController) != null) {
            collapseView.setVisibility(0);
            AbsActionBarView.CollapseView collapseView2 = this.mMediumController;
            if (collapseView2 != null) {
                collapseView2.setVisibility(8);
            }
        }
        if (i2 == 2) {
            if (this.mMovableMainContainer.getAlpha() > 0.0f) {
                AbsActionBarView.CollapseView collapseView3 = this.mCollapseController;
                if (collapseView3 != null) {
                    collapseView3.setAnimFrom(0.0f, 0, 20, true);
                }
                AbsActionBarView.CollapseView collapseView4 = this.mMediumController;
                if (collapseView4 != null) {
                    collapseView4.setAnimFrom(0.0f, 0, 0, true);
                }
                AbsActionBarView.CollapseView collapseView5 = this.mMovableController;
                if (collapseView5 != null) {
                    collapseView5.setAnimFrom(1.0f, 0, 0, true);
                }
            }
            AbsActionBarView.CollapseView collapseView6 = this.mCollapseController;
            if (collapseView6 != null) {
                collapseView6.setVisibility(4);
            }
            AbsActionBarView.CollapseView collapseView7 = this.mMediumController;
            if (collapseView7 != null) {
                collapseView7.setVisibility(8);
            }
            AbsActionBarView.CollapseView collapseView8 = this.mMovableController;
            if (collapseView8 != null) {
                collapseView8.setVisibility(0);
            }
            if (getParent() instanceof ActionBarContainer) {
                ((ActionBarContainer) getParent()).onActionBarExpandStateToExpand();
            }
        }
        if (i2 == 1) {
            boolean zHasTabsInContainer = hasTabsInContainer(this.mMainContainer);
            FrameLayout frameLayout = this.mMainContainer;
            if (zHasTabsInContainer) {
                frameLayout.setVisibility(0);
                this.mMainContainer.setAlpha(1.0f);
                AbsActionBarView.CollapseView collapseView9 = this.mMediumController;
                if (collapseView9 != null) {
                    collapseView9.setVisibility(8);
                }
            } else {
                frameLayout.setVisibility(8);
                this.mMainContainer.clearFocus();
                this.mMainContainer.setAlpha(0.0f);
                AbsActionBarView.CollapseView collapseView10 = this.mMediumController;
                if (collapseView10 != null) {
                    collapseView10.setAnimFrom(1.0f, 0, 0, true);
                    this.mMediumController.setVisibility(0);
                    this.mMediumController.onShow();
                }
                this.mMediumContainer.setVisibility(0);
                this.mMediumContainer.setAlpha(1.0f);
            }
            AbsActionBarView.CollapseView collapseView11 = this.mMovableController;
            if (collapseView11 != null) {
                collapseView11.setVisibility(8);
            }
            this.mMovableMainContainer.setAlpha(0.0f);
            ensureCollapseTabContainerVisibleForMedium();
        }
        if (i2 == 0) {
            AbsActionBarView.CollapseView collapseView12 = this.mCollapseController;
            if (collapseView12 != null && !this.mInActionMode) {
                collapseView12.setAnimFrom(1.0f, 0, 0, true);
                this.mCollapseController.setVisibility(0);
                this.mCollapseController.onShow();
            }
            AbsActionBarView.CollapseView collapseView13 = this.mMediumController;
            if (collapseView13 != null) {
                collapseView13.setVisibility(8);
            }
            AbsActionBarView.CollapseView collapseView14 = this.mMovableController;
            if (collapseView14 != null) {
                collapseView14.setVisibility(8);
            }
        } else {
            this.mPendingHeight = (getHeight() - this.mCollapseTotalHeight) + this.mCollapseSecondaryTabHeight;
        }
        updateTitleVisibilityByState(i2);
        if (this.mActionBarTransitionListeners.size() > 0) {
            if (this.mExpandStateBeforeResizing == i2 && this.mExpandStateOnLayout == i2) {
                return;
            }
            for (ActionBarTransitionListener actionBarTransitionListener : this.mActionBarTransitionListeners) {
                if (i2 == 2) {
                    actionBarTransitionListener.onExpandStateChanged(2);
                } else if (i2 == 1) {
                    actionBarTransitionListener.onExpandStateChanged(1);
                } else if (i2 == 0) {
                    actionBarTransitionListener.onExpandStateChanged(0);
                }
            }
        }
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    protected void onAnimatedExpandStateChanged(int i, int i2) {
        IStateStyle iStateStyle = this.mStateChangeAnimStateStyle;
        if (iStateStyle != null) {
            iStateStyle.cancel();
        }
        this.mPendingHeight = computePendingHeightForOldState(i);
        AnimConfig animConfigAddListeners = new AnimConfig().addListeners(new InnerTransitionListener(this));
        int iComputeTargetHeightForNewState = computeTargetHeightForNewState(i2);
        applyNewStateVisibility(i2);
        updateTitleVisibilityByState(i2);
        this.mStateChangeAnimStateStyle = Folme.useValue(new Object[0]).setFlags(1L).setTo("actionbar_state_change", Integer.valueOf(this.mPendingHeight)).to("actionbar_state_change", Integer.valueOf(iComputeTargetHeightForNewState), animConfigAddListeners);
    }

    private int computePendingHeightForOldState(int i) {
        if (i == 2) {
            return this.mMovableMainContainer.getMeasuredHeight() + this.mMovableSecondaryTabHeight;
        }
        if (i == 1) {
            return this.mMainContainer.getMeasuredHeight();
        }
        return 0;
    }

    private int computeTargetHeightForNewState(int i) {
        if (i == 2) {
            return this.mMovableMainContainer.getMeasuredHeight() + this.mMovableSecondaryTabHeight;
        }
        if (i == 1) {
            return this.mMainContainer.getMeasuredHeight();
        }
        return 0;
    }

    private void applyNewStateVisibility(int i) {
        if (i == 2) {
            this.mCollapseController.setVisibility(4);
            return;
        }
        if (i != 1) {
            if (i == 0) {
                this.mCollapseController.setVisibility(0);
                return;
            }
            return;
        }
        boolean zHasTabsInContainer = hasTabsInContainer(this.mMainContainer);
        FrameLayout frameLayout = this.mMainContainer;
        if (zHasTabsInContainer) {
            frameLayout.setVisibility(0);
            this.mMainContainer.setAlpha(1.0f);
        } else {
            frameLayout.setVisibility(8);
            this.mMainContainer.clearFocus();
        }
        ensureCollapseTabContainerVisibleForMedium();
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr, int[] iArr2) {
        if (canConsumeScroll()) {
            int measuredHeight = this.mMovableMainContainer.getMeasuredHeight() + this.mMovableSecondaryTabHeight;
            if (!hasTitle() && (this.mDisplayOptions & 16) != 0 && this.mCustomNavView != null) {
                measuredHeight = 0;
            }
            int i6 = (this.mCollapseTotalHeight - this.mCollapseSecondaryTabHeight) + measuredHeight;
            int height = getHeight();
            if (i4 >= 0 || height >= i6) {
                return;
            }
            int i7 = this.mPendingHeight;
            if (height - i4 <= i6) {
                this.mPendingHeight = i7 - i4;
                iArr[1] = iArr[1] + i4;
            } else {
                this.mPendingHeight = measuredHeight;
                iArr[1] = iArr[1] + (-(i6 - height));
            }
            if (this.mPendingHeight != i7) {
                if (this.mInnerExpandState != 3) {
                    if (!this.mPostScroller.isFinished()) {
                        this.mPostScroller.forceFinished(true);
                    }
                    setExpandState(3);
                }
                iArr2[1] = i4;
                requestLayout();
            }
        }
    }

    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        return this.mExpandedActionView == null || this.mCustomNavView != null;
    }

    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        if (canConsumeScroll()) {
            if (isUserSetExpandState() && this.mUserExpandState == 1) {
                return;
            }
            if (i2 == 0) {
                this.mTouchScrolling = true;
            } else {
                this.mNonTouchScrolling = true;
            }
            if (!this.mPostScroller.isFinished()) {
                this.mPostScroller.forceFinished(true);
                Runnable runnable = this.mPostScroll;
                if (runnable != null) {
                    removeCallbacks(runnable);
                }
            }
            setExpandState(3);
        }
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0014  */
    public void onStopNestedScroll(View view, int i) {
        boolean z = true;
        if (this.mTouchScrolling) {
            this.mTouchScrolling = false;
            if (this.mNonTouchScrolling) {
                z = false;
            }
        } else if (this.mNonTouchScrolling) {
            this.mNonTouchScrolling = false;
        } else {
            z = false;
        }
        if (canConsumeScroll() && z) {
            snapToFinalExpandState();
        }
    }

    private void snapToFinalExpandState() {
        int measuredHeight = this.mMovableMainContainer.getMeasuredHeight();
        int height = getHeight();
        if (isUserSetExpandState() && this.mUserExpandState == 1) {
            return;
        }
        int i = this.mPendingHeight;
        if (i == 0) {
            setExpandState(0);
            return;
        }
        int i2 = this.mMovableSecondaryTabHeight;
        if (i == measuredHeight + i2) {
            setExpandState(2);
            return;
        }
        int i3 = this.mCollapseTotalHeight;
        int i4 = ((i2 + measuredHeight) / 2) + i3;
        Scroller scroller = this.mPostScroller;
        if (height > i4) {
            scroller.startScroll(0, height, 0, (i3 + measuredHeight) - height);
        } else {
            scroller.startScroll(0, height, 0, i3 - height);
        }
        this.mHandler.postDelayed(this.mPostAnimationRunnable, 20L);
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3, int[] iArr2) {
        int i4;
        if (canConsumeScroll()) {
            if (isUserSetExpandState() && this.mUserExpandState == 1) {
                return;
            }
            int height = getHeight();
            if (i2 <= 0 || height <= (i4 = this.mCollapseTotalHeight)) {
                return;
            }
            int i5 = height - i2;
            int i6 = this.mPendingHeight;
            if (i5 >= i4) {
                this.mPendingHeight = i6 - i2;
            } else {
                this.mPendingHeight = 0;
            }
            iArr[1] = iArr[1] + i2;
            if (this.mPendingHeight != i6) {
                if (this.mInnerExpandState != 3) {
                    if (!this.mPostScroller.isFinished()) {
                        this.mPostScroller.forceFinished(true);
                    }
                    setExpandState(3);
                }
                iArr2[1] = i2;
                requestLayout();
            }
        }
    }

    private boolean canConsumeScroll() {
        return (isShowTitle() || this.mCustomNavView != null) && isResizable() && this.mInnerExpandState != 1;
    }

    public void onActionModeStart(boolean z, boolean z2) {
        this.mInActionMode = true;
        this.mInSearchMode = z;
        if (!z) {
            this.mCollapseController.setVisibility(8);
            this.mMediumController.setVisibility(8);
            this.mMovableController.setVisibility(8);
            setVisibility(8);
        }
        View view = this.mStartView;
        if (view != null) {
            view.setAlpha(0.0f);
        }
        View view2 = this.mEndView;
        if (view2 != null) {
            view2.setAlpha(0.0f);
        }
        View view3 = this.mCenterView;
        if (view3 != null) {
            view3.setAlpha(0.0f);
        }
        View view4 = this.mTitleUpView;
        if (view4 != null) {
            view4.setAlpha(0.0f);
        }
        View view5 = this.mNavigatorSwitch;
        if (view5 != null) {
            NavigatorSwitchPresenter navigatorSwitchPresenter = (NavigatorSwitchPresenter) view5.getTag(miuix.appcompat.R$id.miuix_appcompat_navigator_switch_presenter);
            if (navigatorSwitchPresenter != null) {
                navigatorSwitchPresenter.suppressAlpha(true, 0.0f);
            } else {
                this.mNavigatorSwitch.setAlpha(0.0f);
            }
        }
        if (z2) {
            this.mMovableController.setAcceptAlphaChange(false);
            this.mMediumController.setAcceptAlphaChange(false);
            this.mCollapseController.setAcceptAlphaChange(false);
        }
    }

    public void onActionModeEnd(boolean z) {
        this.mInActionMode = false;
        if (!this.mInSearchMode) {
            setAlpha(0.0f);
            setVisibility(0);
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f);
            objectAnimatorOfFloat.setInterpolator(new AccelerateInterpolator());
            objectAnimatorOfFloat.setDuration(300L);
            objectAnimatorOfFloat.start();
        }
        this.mInSearchMode = false;
        restoreContainerVisibilityForState(getExpandState());
        restoreActionBarChildrenAlpha();
        if (z) {
            this.mMovableController.setAcceptAlphaChange(true);
            this.mCollapseController.setAcceptAlphaChange(true);
            this.mMediumController.setAcceptAlphaChange(true);
            postRefreshTitleControllerStatus();
        }
    }

    private void restoreContainerVisibilityForState(int i) {
        if (i == 0) {
            this.mCollapseController.setVisibility(0);
            this.mMediumController.setVisibility(8);
            this.mMovableController.setVisibility(8);
            return;
        }
        if (i != 1) {
            if (i == 2) {
                this.mCollapseController.setVisibility(4);
                this.mMediumController.setVisibility(8);
                this.mMovableController.setVisibility(0);
                return;
            }
            return;
        }
        boolean zHasTabsInContainer = hasTabsInContainer(this.mMainContainer);
        FrameLayout frameLayout = this.mMainContainer;
        if (zHasTabsInContainer) {
            frameLayout.setVisibility(0);
            this.mMainContainer.setAlpha(1.0f);
            this.mMediumController.setVisibility(8);
        } else {
            frameLayout.setVisibility(8);
            this.mMainContainer.clearFocus();
            this.mMediumController.setVisibility(0);
        }
        this.mMovableController.setVisibility(8);
        ensureCollapseTabContainerVisibleForMedium();
    }

    private void restoreActionBarChildrenAlpha() {
        View view = this.mStartView;
        if (view != null) {
            view.setAlpha(1.0f);
        }
        View view2 = this.mEndView;
        if (view2 != null) {
            view2.setAlpha(1.0f);
        }
        View view3 = this.mCenterView;
        if (view3 != null) {
            view3.setAlpha(1.0f);
        }
        View view4 = this.mTitleUpView;
        if (view4 != null) {
            view4.setAlpha(1.0f);
        }
        View view5 = this.mNavigatorSwitch;
        if (view5 != null) {
            NavigatorSwitchPresenter navigatorSwitchPresenter = (NavigatorSwitchPresenter) view5.getTag(miuix.appcompat.R$id.miuix_appcompat_navigator_switch_presenter);
            if (navigatorSwitchPresenter != null) {
                navigatorSwitchPresenter.suppressAlpha(false, 0.0f);
            } else {
                this.mNavigatorSwitch.setAlpha(1.0f);
            }
        }
    }

    @Override // miuix.view.ActionModeAnimationListener
    public void onStart(boolean z) {
        this.mInActionModeAnimating = true;
        if (this.mActionBarHideState == null) {
            AnimState animState = new AnimState();
            this.mActionBarHideState = animState;
            animState.add(ViewProperty.SCALE_X, 0.8999999761581421d);
            this.mActionBarHideState.add(ViewProperty.SCALE_Y, 0.8999999761581421d);
            this.mActionBarHideState.add(ViewProperty.ALPHA, 0.0d);
            if (isSelfBlurEnabled()) {
                this.mActionBarHideState.add(SELF_BLUR, 50.0d);
            }
        }
        if (this.mActionBarShowState == null) {
            AnimState animState2 = new AnimState();
            this.mActionBarShowState = animState2;
            animState2.add(ViewProperty.SCALE_X, 1.0d);
            this.mActionBarShowState.add(ViewProperty.SCALE_Y, 1.0d);
            this.mActionBarShowState.add(ViewProperty.ALPHA, 1.0d);
            if (isSelfBlurEnabled()) {
                this.mActionBarShowState.add(SELF_BLUR, 0.0d);
            }
        }
        if (z) {
            this.mDoContainerShowAnimInFinishActionMode = false;
            Folme.use((View) this).setTo(this.mActionBarShowState).to(this.mActionBarHideState, this.enterAnimConfig);
            return;
        }
        if (getExpandState() == 0) {
            this.mCollapseController.setVisibility(0);
            this.mCollapseController.setAlpha(0.0f);
            this.mMovableController.setVisibility(8);
        } else if (getExpandState() == 1) {
            boolean zHasTabsInContainer = hasTabsInContainer(this.mMainContainer);
            FrameLayout frameLayout = this.mMainContainer;
            if (zHasTabsInContainer) {
                frameLayout.setVisibility(0);
                this.mMainContainer.setAlpha(0.0f);
                this.mMediumController.setVisibility(8);
            } else {
                frameLayout.setVisibility(8);
                this.mMainContainer.clearFocus();
                this.mMediumController.setVisibility(0);
                this.mMediumController.setAlpha(0.0f);
            }
            this.mMovableController.setVisibility(8);
            ensureCollapseTabContainerVisibleForMedium();
        } else if (getExpandState() == 2) {
            this.mCollapseController.setVisibility(4);
            this.mMovableController.setVisibility(0);
            this.mMovableController.setAlpha(0.0f);
        }
        Folme.use((View) this).setTo(this.mActionBarHideState).to(this.mActionBarShowState, this.exitAnimConfig);
    }

    private boolean isSelfBlurEnabled() {
        return HyperMaterialUtils.isEnable();
    }

    @Override // miuix.view.ActionModeAnimationListener
    public void onUpdate(boolean z, float f) {
        if (this.mDoContainerShowAnimInFinishActionMode || z || f <= 0.8f) {
            return;
        }
        this.mDoContainerShowAnimInFinishActionMode = true;
        showContainerInFinishActionMode();
    }

    @Override // miuix.view.ActionModeAnimationListener
    public void onStop(boolean z) {
        this.mInActionModeAnimating = false;
        if (z) {
            this.mCollapseController.setVisibility(4);
            this.mMovableController.setVisibility(4);
        } else {
            if (!this.mDoContainerShowAnimInFinishActionMode) {
                showContainerInFinishActionMode();
            }
            this.mDoContainerShowAnimInFinishActionMode = false;
        }
    }

    private void showContainerInFinishActionMode() {
        if (getExpandState() == 0) {
            this.mCollapseController.animTo(1.0f, 0, 0, this.mMovableAnimNormalConfig);
            return;
        }
        if (getExpandState() == 1) {
            if (hasTabsInContainer(this.mMainContainer)) {
                this.mCollapseController.animTo(1.0f, 0, 0, this.mMovableAnimNormalConfig);
            } else {
                this.mMediumController.animTo(1.0f, 0, 0, this.mMovableAnimNormalConfig);
            }
            ensureCollapseTabContainerVisibleForMedium();
            return;
        }
        if (getExpandState() == 2) {
            this.mCollapseController.setAlpha(0.0f);
            this.mCollapseController.setVisibility(0);
            this.mMovableController.animTo(1.0f, 0, 0, this.mMovableAnimShowConfig);
        }
    }

    class InnerTransitionListener extends TransitionListener {
        private WeakReference mRef;

        public InnerTransitionListener(ActionBarView actionBarView) {
            this.mRef = new WeakReference(actionBarView);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onBegin(Object obj) {
            super.onBegin(obj);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection collection) {
            ActionBarView actionBarView;
            super.onUpdate(obj, collection);
            UpdateInfo updateInfoFindByName = UpdateInfo.findByName(collection, "actionbar_state_change");
            if (updateInfoFindByName == null || (actionBarView = (ActionBarView) this.mRef.get()) == null) {
                return;
            }
            actionBarView.mPendingHeight = updateInfoFindByName.getIntValue();
            actionBarView.requestLayout();
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            this.mRef.clear();
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onCancel(Object obj) {
            super.onCancel(obj);
            this.mRef.clear();
        }
    }

    public void setApplyBgBlur(boolean z) {
        if (this.mApplyBgBlur != z) {
            this.mApplyBgBlur = z;
        }
    }

    public void updateActionButtonFloatingState(boolean z) {
        Boolean bool = this.mFloatingMode;
        if (bool == null || bool.booleanValue() != z) {
            this.mFloatingMode = Boolean.valueOf(z);
            applyFloatingStateToButtons();
        }
    }

    public void setFloatingBgColor(int i) {
        if (this.mFloatingBgColor != i) {
            this.mFloatingBgColor = i;
            if (this.mFloatingMode != null) {
                applyFloatingStateToButtons();
            }
        }
    }

    public void refreshFloatingState() {
        if (this.mFloatingMode != null) {
            applyFloatingStateToButtons();
        }
    }

    private void applyFloatingStateToButtons() {
        boolean zIsLightTheme = MiuixUIUtils.isLightTheme(getContext());
        boolean zBooleanValue = this.mFloatingMode.booleanValue();
        View view = this.mTitleUpView;
        if ((view instanceof ViewGroup) && ((ViewGroup) view).getChildCount() > 0) {
            FloatingEffectHelper.updateViewFloatingState(((ViewGroup) this.mTitleUpView).getChildAt(0), zBooleanValue, zIsLightTheme, this.mActionBarButtonMaterial, this.mFloatingBgColor);
        }
        HomeView homeView = this.mHomeLayout;
        if (homeView != null) {
            FloatingEffectHelper.updateViewFloatingState(homeView.getUp(), zBooleanValue, zIsLightTheme, this.mActionBarButtonMaterial, this.mFloatingBgColor);
        }
        View view2 = this.mNavigatorSwitch;
        if (view2 != null) {
            FloatingEffectHelper.updateViewFloatingState(view2, zBooleanValue, zIsLightTheme, this.mActionBarButtonMaterial, this.mFloatingBgColor);
        }
        ActionMenuView actionMenuView = this.mEndMenuView;
        if (actionMenuView instanceof EndActionMenuView) {
            ((EndActionMenuView) actionMenuView).setFloatingMode(zBooleanValue, zIsLightTheme, this.mActionBarButtonMaterial, this.mFloatingBgColor);
        }
    }
}
