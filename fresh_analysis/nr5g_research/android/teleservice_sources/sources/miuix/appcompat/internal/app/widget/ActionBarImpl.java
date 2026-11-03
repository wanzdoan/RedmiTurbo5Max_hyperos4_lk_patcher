package miuix.appcompat.internal.app.widget;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.fragment.app.FragmentManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.ActionBarTransitionListener;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.strategy.ActionBarConfig;
import miuix.appcompat.app.strategy.ActionBarSpec;
import miuix.appcompat.app.strategy.CommonActionBarStrategy;
import miuix.appcompat.app.strategy.IActionBarStrategy;
import miuix.appcompat.internal.view.ActionBarPolicy;
import miuix.appcompat.internal.view.ActionModeImpl;
import miuix.appcompat.internal.view.EditActionModeImpl;
import miuix.appcompat.internal.view.SearchActionModeImpl;
import miuix.appcompat.internal.view.menu.action.PhoneActionMenuView;
import miuix.container.ExtraPaddingPolicy;
import miuix.core.util.EnvStateManager;
import miuix.core.util.MiuiBlurUtils;
import miuix.core.util.MiuixUIUtils;
import miuix.core.view.NestedContentInsetObserver;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.LiteUtils;
import miuix.view.SearchActionMode;

/* JADX INFO: loaded from: classes2.dex */
public class ActionBarImpl extends ActionBar {
    private IActionBarStrategy mActionBarStrategy;
    private ActionMode mActionMode;
    private ActionModeView mActionModeView;
    private ActionBarView mActionView;
    private boolean mAdsorptionToNoOverlay;
    private IStateStyle mContainerAnim;
    private ActionBarContainer mContainerView;
    private Rect mContentInset;
    private View mContentMask;
    private View.OnClickListener mContentMaskOnClickListener;
    private Context mContext;
    private int mContextDisplayMode;
    private ActionBarContextView mContextView;
    private int mCurrentAccessibilityImportant;
    private int mCurrentExpandState;
    private boolean mCurrentResizable;
    private boolean mDisplayHomeAsUpSet;
    private ExtraPaddingPolicy mExtraPaddingPolicy;
    private FragmentManager mFragmentManager;
    private boolean mHiddenByApp;
    private boolean mHiddenBySystem;
    private boolean mIsWindowInfoChanged;
    private ActionBarOverlayLayout mOverlayLayout;
    private ActionBarCoordinateListener mOwnCoordinateListener;
    private SearchActionModeView mSearchActionModeView;
    private boolean mShowHideAnimationEnabled;
    private boolean mShowingForMode;
    private PhoneActionMenuView mSplitMenuView;
    private ActionBarContainer mSplitView;
    private View.OnLayoutChangeListener mStrategyLayoutChangeListener;
    private ViewTreeObserver.OnPreDrawListener mStrategyPreDrawListener;
    private Context mThemedContext;
    private int mWindowMode;
    private static androidx.appcompat.app.ActionBar.TabListener sTabListenerWrapper = new androidx.appcompat.app.ActionBar.TabListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.1
    };
    private static final Integer UNINITIALIZED_OFFSET = -1;
    private final HashMap mCoordinateOffsetViewSet = new HashMap();
    private final HashSet mNestedContentInsetObserverSet = new HashSet();
    private ArrayList mTabs = new ArrayList();
    private boolean isSelectingTab = false;
    private int mSavedTabPosition = -1;
    private ArrayList mMenuVisibilityListeners = new ArrayList();
    private int mCurWindowVisibility = 0;
    private boolean mNowShowing = true;
    private ActionModeImpl.ActionModeCallback mActionModeCallback = new ActionModeImpl.ActionModeCallback() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.2
        @Override // miuix.appcompat.internal.view.ActionModeImpl.ActionModeCallback
        public void onActionModeFinish(ActionMode actionMode) {
            ActionBarImpl.this.animateToMode(false);
            ActionBarImpl.this.mActionMode = null;
        }
    };
    private boolean mIsContainerAnimationRunning = false;
    private int mMaxActionMenuItemCount = -1;
    private int mContentInsetTop = 0;
    private int mCurrentActionBarHeightGap = 0;
    private int mActionBarHeightTotalGap = 0;
    private int mCurrentActionBarHeightGapOnShow = 0;
    private int mActionBarHeightTotalGapOnShow = 0;
    private float mTargetTranslationY = 0.0f;
    private final TransitionListener mContainerViewAnimationListener = new TransitionListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.8
        @Override // miuix.animation.listener.TransitionListener
        public void onBegin(Object obj) {
            super.onBegin(obj);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection collection) {
            super.onUpdate(obj, collection);
            float translationY = (ActionBarImpl.this.mTargetTranslationY - ActionBarImpl.this.mContainerView.getTranslationY()) / ActionBarImpl.this.mTargetTranslationY;
            ActionBarImpl actionBarImpl = ActionBarImpl.this;
            actionBarImpl.mActionBarHeightTotalGap = (int) Math.max(0.0f, actionBarImpl.mActionBarHeightTotalGapOnShow * translationY);
            ActionBarImpl actionBarImpl2 = ActionBarImpl.this;
            actionBarImpl2.mCurrentActionBarHeightGap = (int) Math.max(0.0f, actionBarImpl2.mCurrentActionBarHeightGapOnShow * translationY);
            ActionBarImpl.this.mOverlayLayout.updateCurrentContentInsetInOverlayMode();
            ActionBarImpl.this.updateCoordinateOffsetView();
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onCancel(Object obj) {
            super.onCancel(obj);
            ActionBarImpl.this.mIsContainerAnimationRunning = false;
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            ActionBarImpl.this.mIsContainerAnimationRunning = false;
        }
    };

    private static boolean checkShowingFlags(boolean z, boolean z2, boolean z3) {
        if (z3) {
            return true;
        }
        return (z || z2) ? false : true;
    }

    public ActionBarImpl(AppCompatActivity appCompatActivity, ViewGroup viewGroup) {
        this.mContext = appCompatActivity;
        this.mFragmentManager = appCompatActivity.getSupportFragmentManager();
        init(viewGroup);
        this.mActionView.setWindowTitle(appCompatActivity.getTitle());
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void init(ViewGroup viewGroup) {
        int iResolveInt;
        ExtraPaddingPolicy extraPaddingPolicy;
        if (viewGroup == null) {
            return;
        }
        TypedValue typedValueResolveTypedValue = AttributeResolver.resolveTypedValue(this.mContext, R$attr.actionBarStrategy);
        if (typedValueResolveTypedValue != null) {
            try {
                Class[] clsArr = new Class[0];
                this.mActionBarStrategy = (IActionBarStrategy) Class.forName(typedValueResolveTypedValue.string.toString()).getDeclaredConstructor(null).newInstance(null);
            } catch (Exception unused) {
            }
        }
        this.mWindowMode = EnvStateManager.getWindowInfo(this.mContext).windowMode;
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) viewGroup;
        this.mOverlayLayout = actionBarOverlayLayout;
        actionBarOverlayLayout.setActionBar(this);
        ActionBarView actionBarView = (ActionBarView) viewGroup.findViewById(R$id.action_bar);
        this.mActionView = actionBarView;
        if (actionBarView != null && (extraPaddingPolicy = this.mExtraPaddingPolicy) != null) {
            actionBarView.setExtraPaddingPolicy(extraPaddingPolicy);
        }
        this.mContextView = (ActionBarContextView) viewGroup.findViewById(R$id.action_context_bar);
        this.mContainerView = (ActionBarContainer) viewGroup.findViewById(R$id.action_bar_container);
        this.mSplitView = (ActionBarContainer) viewGroup.findViewById(R$id.split_action_bar);
        View viewFindViewById = viewGroup.findViewById(R$id.content_mask);
        this.mContentMask = viewFindViewById;
        if (viewFindViewById != null) {
            this.mContentMaskOnClickListener = new View.OnClickListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ActionBarImpl.this.mSplitMenuView == null || !ActionBarImpl.this.mSplitMenuView.isOverflowMenuShowing()) {
                        return;
                    }
                    ActionBarImpl.this.mSplitMenuView.getPresenter().hideOverflowMenu(true);
                }
            };
        }
        ActionBarView actionBarView2 = this.mActionView;
        if (actionBarView2 == null && this.mContextView == null && this.mContainerView == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m(getClass().getSimpleName().concat(" can only be used with a compatible window decor layout"));
            return;
        }
        this.mContextDisplayMode = actionBarView2.isSplitActionBar() ? 1 : 0;
        Object[] objArr = (this.mActionView.getDisplayOptions() & 4) != 0;
        if (objArr != false) {
            this.mDisplayHomeAsUpSet = true;
        }
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(this.mContext);
        setHomeButtonEnabled(actionBarPolicy.enableHomeButtonByDefault() || objArr == true);
        setHasEmbeddedTabs(actionBarPolicy.hasEmbeddedTabs());
        boolean z = MiuiBlurUtils.isEnable() && !LiteUtils.isCommonLiteStrategy();
        ActionBarContainer actionBarContainer = this.mContainerView;
        if (actionBarContainer != null) {
            actionBarContainer.setSupportBlur(z);
        }
        ActionBarContainer actionBarContainer2 = this.mSplitView;
        if (actionBarContainer2 != null) {
            actionBarContainer2.setSupportBlur(z);
        }
        if (z && (iResolveInt = AttributeResolver.resolveInt(this.mContext, R$attr.bgBlurOptions, 0)) != 0) {
            int displayOptions = getDisplayOptions();
            if ((iResolveInt & 1) != 0) {
                displayOptions |= 32768;
            }
            if ((iResolveInt & 2) != 0) {
                displayOptions |= 16384;
            }
            setDisplayOptions(displayOptions);
        }
        if (this.mActionBarStrategy == null) {
            this.mActionBarStrategy = new CommonActionBarStrategy();
        }
        this.mStrategyPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.4
            int lastWidth = 0;

            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                int measuredWidth = ActionBarImpl.this.mOverlayLayout.getMeasuredWidth();
                if (this.lastWidth == measuredWidth && !ActionBarImpl.this.mIsWindowInfoChanged) {
                    return true;
                }
                ActionBarImpl.this.mIsWindowInfoChanged = false;
                this.lastWidth = measuredWidth;
                int expandState = ActionBarImpl.this.getExpandState();
                ActionBarImpl actionBarImpl = ActionBarImpl.this;
                actionBarImpl.applyActionBarStrategy(actionBarImpl.mActionView, ActionBarImpl.this.mContextView);
                int expandState2 = ActionBarImpl.this.getExpandState();
                ActionBarImpl.this.mOverlayLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                ActionBarImpl.this.mStrategyPreDrawListener = null;
                ActionBarImpl.this.mOverlayLayout.requestLayout();
                ActionBarImpl.this.postRecheckOverlayMaskState();
                return expandState == expandState2;
            }
        };
        this.mOverlayLayout.getViewTreeObserver().addOnPreDrawListener(this.mStrategyPreDrawListener);
        AnonymousClass5 anonymousClass5 = new AnonymousClass5();
        this.mStrategyLayoutChangeListener = anonymousClass5;
        this.mOverlayLayout.addOnLayoutChangeListener(anonymousClass5);
        if (this.mOverlayLayout.findViewById(R$id.search_mode_stub) != null) {
            final WeakReference weakReference = new WeakReference(this);
            this.mOverlayLayout.post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl$$ExternalSyntheticLambda3
                        @Override // android.os.MessageQueue.IdleHandler
                        public final boolean queueIdle() {
                            return ActionBarImpl.$r8$lambda$I0xKt96dlj64UHqSakKhd66xoJ8(weakReference);
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: renamed from: miuix.appcompat.internal.app.widget.ActionBarImpl$5, reason: invalid class name */
    class AnonymousClass5 implements View.OnLayoutChangeListener {
        int lastWidth = 0;

        AnonymousClass5() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            int i9 = i3 - i;
            if (this.lastWidth != i9 || ActionBarImpl.this.mIsWindowInfoChanged) {
                ActionBarImpl.this.mIsWindowInfoChanged = false;
                this.lastWidth = i9;
                ActionBarImpl.this.mActionView.post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl$5$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActionBarImpl.AnonymousClass5.m5453$r8$lambda$VBjL__c9DLk1QV2vfeVIDz7FE4(this.f$0);
                    }
                });
            }
        }

        /* JADX INFO: renamed from: $r8$lambda$VBjL__c9DLk1QV2vfeVIDz7FE-4, reason: not valid java name */
        public static /* synthetic */ void m5453$r8$lambda$VBjL__c9DLk1QV2vfeVIDz7FE4(AnonymousClass5 anonymousClass5) {
            ActionBarImpl actionBarImpl = ActionBarImpl.this;
            actionBarImpl.applyActionBarStrategy(actionBarImpl.mActionView, ActionBarImpl.this.mContextView);
        }
    }

    public static /* synthetic */ boolean $r8$lambda$I0xKt96dlj64UHqSakKhd66xoJ8(WeakReference weakReference) {
        ActionBarImpl actionBarImpl = (ActionBarImpl) weakReference.get();
        if (actionBarImpl != null) {
            Context context = actionBarImpl.mContext;
            if (!(context instanceof Activity) || !((Activity) context).isDestroyed()) {
                actionBarImpl.preCreateSearchActionModeView();
                return false;
            }
        }
        return false;
    }

    private ActionBarSpec getActionBarSpec(ActionBarContainer actionBarContainer, ActionBarView actionBarView) {
        ActionBarSpec actionBarSpec = new ActionBarSpec();
        actionBarSpec.deviceType = this.mOverlayLayout.getDeviceType();
        actionBarSpec.windowMode = this.mWindowMode;
        if (actionBarContainer != null && actionBarView != null) {
            float f = actionBarView.getContext().getResources().getDisplayMetrics().density;
            Point windowSize = EnvStateManager.getWindowSize(actionBarView.getContext());
            int i = windowSize.x;
            actionBarSpec.windowWidth = i;
            actionBarSpec.windowHeight = windowSize.y;
            actionBarSpec.windowWidthDp = MiuixUIUtils.px2dp(f, i);
            actionBarSpec.windowHeightDp = MiuixUIUtils.px2dp(f, actionBarSpec.windowHeight);
            int measuredWidth = actionBarContainer.getMeasuredWidth();
            actionBarSpec.actionBarWidth = measuredWidth;
            if (measuredWidth == 0) {
                actionBarSpec.actionBarWidth = this.mOverlayLayout.getMeasuredWidth();
            }
            actionBarSpec.actionBarWidthDp = MiuixUIUtils.px2dp(f, actionBarSpec.actionBarWidth);
            int measuredHeight = actionBarView.getMeasuredHeight();
            actionBarSpec.actionBarHeight = measuredHeight;
            actionBarSpec.actionBarHeightDp = MiuixUIUtils.px2dp(f, measuredHeight);
            actionBarSpec.isUserSetExpandState = actionBarView.isUserSetExpandState();
            actionBarSpec.expandState = actionBarView.getExpandState();
            actionBarSpec.resizable = actionBarView.isResizable();
            actionBarSpec.isUserSetEndActionMenuItemLimit = actionBarView.isUserSetEndActionMenuItemLimit();
            actionBarSpec.endActionMenuItemLimit = actionBarView.getEndActionMenuItemLimit();
        }
        Context context = this.mContext;
        if (context instanceof AppCompatActivity) {
            actionBarSpec.isInFloatingWindowMode = ((AppCompatActivity) context).isInFloatingWindowMode();
        }
        return actionBarSpec;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyActionBarStrategy(ActionBarView actionBarView, ActionBarContextView actionBarContextView) {
        if (this.mActionBarStrategy == null) {
            return;
        }
        int expandState = getExpandState();
        ActionBarConfig actionBarConfigConfig = this.mActionBarStrategy.config(this, getActionBarSpec(this.mContainerView, this.mActionView));
        if (actionBarView != null && actionBarConfigConfig != null) {
            if (!actionBarView.isUserSetExpandState() || actionBarConfigConfig.overrideUserExpandStateConfig) {
                if (!actionBarView.isResizable() || !actionBarConfigConfig.resizable) {
                    actionBarView.setExpandState(actionBarConfigConfig.expandState, false, true);
                }
                actionBarView.setResizable(actionBarConfigConfig.resizable);
            }
            if (!actionBarView.isUserSetEndActionMenuItemLimit() || actionBarConfigConfig.overrideUserEndMenuConfig) {
                actionBarView.setEndActionMenuItemLimit(actionBarConfigConfig.endMenuMaxItemCount);
            }
        }
        if (actionBarContextView != null && actionBarConfigConfig != null && (!actionBarContextView.isUserSetExpandState() || actionBarConfigConfig.overrideUserExpandStateConfig)) {
            if (!actionBarContextView.isResizable() || !actionBarConfigConfig.resizable) {
                actionBarContextView.setExpandState(actionBarConfigConfig.expandState, false, true);
            }
            actionBarContextView.setResizable(actionBarConfigConfig.resizable);
        }
        this.mCurrentExpandState = getExpandState();
        this.mCurrentResizable = isResizable();
        int i = this.mCurrentExpandState;
        if (i != 2 || expandState == i || this.mContentInset == null) {
            return;
        }
        Iterator it = this.mCoordinateOffsetViewSet.keySet().iterator();
        while (it.hasNext()) {
            this.mCoordinateOffsetViewSet.put((View) it.next(), Integer.valueOf(this.mContentInset.top));
        }
        Iterator it2 = this.mNestedContentInsetObserverSet.iterator();
        while (it2.hasNext()) {
            ((NestedContentInsetObserver) it2.next()).onContentInsetChanged(this.mContentInset);
        }
        ActionBarContainer actionBarContainer = this.mContainerView;
        if (actionBarContainer != null) {
            actionBarContainer.setActionBarBlurByNestedScrolled(false);
        }
    }

    @Override // androidx.appcompat.app.ActionBar
    public void onConfigurationChanged(Configuration configuration) {
        this.mIsWindowInfoChanged = true;
        this.mWindowMode = EnvStateManager.getWindowInfo(this.mContext, configuration).windowMode;
        setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
        SearchActionModeView searchActionModeView = this.mSearchActionModeView;
        if (searchActionModeView == null || searchActionModeView.isAttachedToWindow()) {
            return;
        }
        this.mSearchActionModeView.onConfigurationChanged(configuration);
    }

    public void onDestroy() {
        ActionBarOverlayLayout actionBarOverlayLayout = this.mOverlayLayout;
        if (actionBarOverlayLayout == null) {
            return;
        }
        if (this.mStrategyPreDrawListener != null) {
            actionBarOverlayLayout.getViewTreeObserver().removeOnPreDrawListener(this.mStrategyPreDrawListener);
            this.mStrategyPreDrawListener = null;
        }
        View.OnLayoutChangeListener onLayoutChangeListener = this.mStrategyLayoutChangeListener;
        if (onLayoutChangeListener != null) {
            this.mOverlayLayout.removeOnLayoutChangeListener(onLayoutChangeListener);
            this.mStrategyLayoutChangeListener = null;
        }
    }

    public void onFloatingModeChanged(boolean z) {
        this.mContainerView.setIsMiuixFloating(z);
        SearchActionModeView searchActionModeView = this.mSearchActionModeView;
        if (searchActionModeView != null) {
            searchActionModeView.onFloatingModeChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postRecheckOverlayMaskState() {
        ActionBarOverlayLayout actionBarOverlayLayout = this.mOverlayLayout;
        if (actionBarOverlayLayout == null) {
            return;
        }
        actionBarOverlayLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.6
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                boolean z;
                ActionBarImpl.this.mOverlayLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                if (ActionBarImpl.this.mContainerView != null && ActionBarImpl.this.mOverlayLayout.isAttachedToWindow()) {
                    Iterator it = ActionBarImpl.this.mNestedContentInsetObserverSet.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            z = false;
                            break;
                        }
                        Object obj = (NestedContentInsetObserver) it.next();
                        if ((obj instanceof View) && ((View) obj).canScrollVertically(-1)) {
                            z = true;
                            break;
                        }
                    }
                    if (!z) {
                        Iterator it2 = ActionBarImpl.this.mCoordinateOffsetViewSet.keySet().iterator();
                        while (it2.hasNext()) {
                            if (((View) it2.next()).canScrollVertically(-1)) {
                                z = true;
                                break;
                            }
                        }
                    }
                    ActionBarImpl.this.mContainerView.recheckOverlayMaskState(z);
                }
                return true;
            }
        });
    }

    private void setHasEmbeddedTabs(boolean z) {
        ActionBarContainer actionBarContainer = this.mContainerView;
        if (actionBarContainer != null) {
            actionBarContainer.setTabContainer(null);
        }
        this.mActionView.setEmbeddedTabView(null, null, null, null);
        getNavigationMode();
        this.mActionView.setCollapsable(false);
    }

    @Override // miuix.appcompat.app.ActionBar
    public boolean isAdsorptionToNoOverlay() {
        return this.mAdsorptionToNoOverlay;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setTitle(CharSequence charSequence) {
        this.mActionView.setTitle(charSequence);
    }

    public void setDisplayOptions(int i, int i2) {
        int displayOptions = this.mActionView.getDisplayOptions();
        if ((i2 & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mActionView.setDisplayOptions((i & i2) | ((~i2) & displayOptions));
        int displayOptions2 = this.mActionView.getDisplayOptions();
        ActionBarContainer actionBarContainer = this.mContainerView;
        if (actionBarContainer != null) {
            actionBarContainer.setEnableBlur((32768 & displayOptions2) != 0);
        }
        ActionBarContainer actionBarContainer2 = this.mSplitView;
        if (actionBarContainer2 != null) {
            actionBarContainer2.setEnableBlur((displayOptions2 & 16384) != 0);
        }
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayHomeAsUpEnabled(boolean z) {
        int blurOptions = getBlurOptions();
        setDisplayOptions((z ? 4 : 0) | blurOptions, blurOptions | 4);
    }

    private int getBlurOptions() {
        return ((getDisplayOptions() & 32768) != 0 ? 32768 : 0) | ((getDisplayOptions() & 16384) != 0 ? 16384 : 0);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setHomeButtonEnabled(boolean z) {
        this.mActionView.setHomeButtonEnabled(z);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setTitle(int i) {
        setTitle(this.mContext.getString(i));
    }

    public int getNavigationMode() {
        return this.mActionView.getNavigationMode();
    }

    @Override // androidx.appcompat.app.ActionBar
    public int getDisplayOptions() {
        return this.mActionView.getDisplayOptions();
    }

    public void setDisplayOptions(int i) {
        if ((i & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mActionView.setDisplayOptions(i);
        int displayOptions = this.mActionView.getDisplayOptions();
        ActionBarContainer actionBarContainer = this.mContainerView;
        if (actionBarContainer != null) {
            actionBarContainer.setEnableBlur((displayOptions & 32768) != 0);
        }
        ActionBarContainer actionBarContainer2 = this.mSplitView;
        if (actionBarContainer2 != null) {
            actionBarContainer2.setEnableBlur((i & 16384) != 0);
        }
    }

    @Override // androidx.appcompat.app.ActionBar
    public Context getThemedContext() {
        if (this.mThemedContext == null) {
            TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
            int i = typedValue.resourceId;
            if (i != 0) {
                this.mThemedContext = new ContextThemeWrapper(this.mContext, i);
            } else {
                this.mThemedContext = this.mContext;
            }
        }
        return this.mThemedContext;
    }

    @Override // androidx.appcompat.app.ActionBar
    public int getHeight() {
        return this.mContainerView.getHeight();
    }

    void showForActionMode() {
        if (this.mShowingForMode) {
            return;
        }
        this.mShowingForMode = true;
        updateVisibility(false);
        if (this.mActionModeView instanceof SearchActionModeView) {
            ActionBarView actionBarView = this.mActionView;
            if (actionBarView.mInnerExpandState == 3) {
                int height = actionBarView.getHeight();
                int collapsedHeight = this.mActionView.getCollapsedHeight();
                int expandedHeight = collapsedHeight + ((this.mActionView.getExpandedHeight() - collapsedHeight) / 2);
                ActionBarView actionBarView2 = this.mActionView;
                if (height <= expandedHeight) {
                    actionBarView2.setExpandState(0);
                } else {
                    actionBarView2.setExpandState(2);
                }
            }
            this.mCurrentExpandState = getExpandState();
            this.mCurrentResizable = isResizable();
            setResizable(false);
        } else {
            this.mCurrentExpandState = getExpandState();
            this.mCurrentResizable = isResizable();
            this.mContainerView.startActionMode();
            ((ActionBarContextView) this.mActionModeView).setExpandState(this.mCurrentExpandState);
            ((ActionBarContextView) this.mActionModeView).setResizable(this.mCurrentResizable);
        }
        this.mCurrentAccessibilityImportant = this.mActionView.getImportantForAccessibility();
        this.mActionView.setImportantForAccessibility(4);
        this.mActionView.onActionModeStart(this.mActionModeView instanceof SearchActionModeView, (getDisplayOptions() & 32768) != 0);
    }

    @Override // miuix.appcompat.app.ActionBar
    public View getActionBarView() {
        return this.mActionView;
    }

    void hideForActionMode() {
        if (this.mShowingForMode) {
            this.mShowingForMode = false;
            this.mActionView.onActionModeEnd((getDisplayOptions() & 32768) != 0);
            updateVisibility(false);
            if (this.mActionModeView instanceof SearchActionModeView) {
                setResizable(this.mCurrentResizable);
            } else {
                this.mContainerView.finishActionMode();
                this.mCurrentResizable = ((ActionBarContextView) this.mActionModeView).isResizable();
                this.mCurrentExpandState = ((ActionBarContextView) this.mActionModeView).getExpandState();
                setResizable(this.mCurrentResizable);
                this.mActionView.setExpandState(this.mCurrentExpandState);
            }
            this.mActionView.setImportantForAccessibility(this.mCurrentAccessibilityImportant);
        }
    }

    public boolean isShowing() {
        return this.mNowShowing;
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        Rect baseInnerInsets;
        SearchActionModeView searchActionModeView;
        EditText searchInput;
        ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        if ((callback instanceof SearchActionMode.Callback) && (searchActionModeView = this.mSearchActionModeView) != null && (searchInput = searchActionModeView.getSearchInput()) != null && searchInput.isAttachedToWindow()) {
            this.mSearchActionModeView.setAlpha(0.0f);
            this.mSearchActionModeView.setVisibility(0);
            searchInput.setFocusable(true);
            searchInput.setFocusableInTouchMode(true);
            searchInput.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
            inputMethodManager.viewClicked(searchInput);
            inputMethodManager.showSoftInput(searchInput, 0);
        }
        ActionMode actionModeCreateActionMode = createActionMode(callback);
        ActionModeView actionModeView = this.mActionModeView;
        if (((actionModeView instanceof SearchActionModeView) && (actionModeCreateActionMode instanceof SearchActionModeImpl)) || ((actionModeView instanceof ActionBarContextView) && (actionModeCreateActionMode instanceof EditActionModeImpl))) {
            actionModeView.closeMode();
            this.mActionModeView.killMode();
        }
        ActionModeView actionModeViewCreateActionModeView = createActionModeView(callback);
        this.mActionModeView = actionModeViewCreateActionModeView;
        if (actionModeViewCreateActionModeView == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("not set windowSplitActionBar true in activity style!");
            return null;
        }
        if (actionModeCreateActionMode instanceof ActionModeImpl) {
            ActionModeImpl actionModeImpl = (ActionModeImpl) actionModeCreateActionMode;
            actionModeImpl.setActionModeView(actionModeViewCreateActionModeView);
            if ((actionModeImpl instanceof SearchActionModeImpl) && (baseInnerInsets = this.mOverlayLayout.getBaseInnerInsets()) != null) {
                ((SearchActionModeImpl) actionModeImpl).setPendingInsets(baseInnerInsets);
            }
            actionModeImpl.setActionModeCallback(this.mActionModeCallback);
            if (actionModeImpl.dispatchOnCreate()) {
                actionModeCreateActionMode.invalidate();
                this.mActionModeView.initForMode(actionModeCreateActionMode);
                animateToMode(true);
                ActionBarContainer actionBarContainer = this.mSplitView;
                if (actionBarContainer != null && this.mContextDisplayMode == 1 && actionBarContainer.getVisibility() != 0) {
                    this.mSplitView.setVisibility(0);
                }
                ActionModeView actionModeView2 = this.mActionModeView;
                if (actionModeView2 instanceof ActionBarContextView) {
                    ((ActionBarContextView) actionModeView2).sendAccessibilityEvent(32);
                }
                this.mActionMode = actionModeCreateActionMode;
                return actionModeCreateActionMode;
            }
        }
        return null;
    }

    void animateToMode(boolean z) {
        if (z) {
            showForActionMode();
        } else {
            hideForActionMode();
        }
        this.mActionModeView.animateToVisibility(z);
    }

    private ActionMode createActionMode(ActionMode.Callback callback) {
        if (callback instanceof SearchActionMode.Callback) {
            return new SearchActionModeImpl(this.mContext, callback);
        }
        return new EditActionModeImpl(this.mContext, callback);
    }

    public ActionModeView createActionModeView(ActionMode.Callback callback) {
        ActionModeView actionModeView;
        int i;
        if (callback instanceof SearchActionMode.Callback) {
            if (this.mSearchActionModeView == null) {
                SearchActionModeView searchActionModeViewCreateSearchActionModeView = createSearchActionModeView();
                this.mSearchActionModeView = searchActionModeViewCreateSearchActionModeView;
                searchActionModeViewCreateSearchActionModeView.setExtraPaddingPolicy(this.mExtraPaddingPolicy);
            }
            if (this.mOverlayLayout != this.mSearchActionModeView.getParent()) {
                this.mOverlayLayout.addView(this.mSearchActionModeView);
            }
            measureSearchActionModeView();
            this.mSearchActionModeView.addAnimationListener(this.mActionView);
            actionModeView = this.mSearchActionModeView;
        } else {
            actionModeView = this.mContextView;
            if (actionModeView == null) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("not set windowSplitActionBar true in activity style!");
                return null;
            }
        }
        if ((actionModeView instanceof ActionBarContextView) && (i = this.mMaxActionMenuItemCount) != -1) {
            ((ActionBarContextView) actionModeView).setActionMenuItemLimit(i);
        }
        return actionModeView;
    }

    public SearchActionModeView createSearchActionModeView() {
        SearchActionModeView searchActionModeView = (SearchActionModeView) LayoutInflater.from(getThemedContext()).inflate(R$layout.miuix_appcompat_search_action_mode_view, (ViewGroup) this.mOverlayLayout, false);
        searchActionModeView.setOverlayModeView(this.mOverlayLayout);
        searchActionModeView.setOnBackClickListener(new View.OnClickListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ActionBarImpl.this.mActionMode != null) {
                    ActionBarImpl.this.mActionMode.finish();
                }
            }
        });
        return searchActionModeView;
    }

    private void measureSearchActionModeView() {
        this.mSearchActionModeView.measure(ViewGroup.getChildMeasureSpec(this.mOverlayLayout.getMeasuredWidth(), 0, this.mSearchActionModeView.getLayoutParams().width), ViewGroup.getChildMeasureSpec(this.mOverlayLayout.getMeasuredHeight(), 0, this.mSearchActionModeView.getLayoutParams().height));
    }

    public void preCreateSearchActionModeView() {
        if (this.mSearchActionModeView == null) {
            SearchActionModeView searchActionModeViewCreateSearchActionModeView = createSearchActionModeView();
            this.mSearchActionModeView = searchActionModeViewCreateSearchActionModeView;
            searchActionModeViewCreateSearchActionModeView.setExtraPaddingPolicy(this.mExtraPaddingPolicy);
        }
        if (this.mOverlayLayout != this.mSearchActionModeView.getParent()) {
            this.mSearchActionModeView.setVisibility(8);
            this.mOverlayLayout.addView(this.mSearchActionModeView);
        }
    }

    void setExtraPaddingPolicy(ExtraPaddingPolicy extraPaddingPolicy) {
        if (this.mExtraPaddingPolicy != extraPaddingPolicy) {
            this.mExtraPaddingPolicy = extraPaddingPolicy;
            ActionBarView actionBarView = this.mActionView;
            if (actionBarView != null) {
                actionBarView.setExtraPaddingPolicy(extraPaddingPolicy);
            }
            SearchActionModeView searchActionModeView = this.mSearchActionModeView;
            if (searchActionModeView != null) {
                searchActionModeView.setExtraPaddingPolicy(this.mExtraPaddingPolicy);
            }
        }
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setProgressBarIndeterminateVisibility(boolean z) {
        this.mActionView.setProgressBarIndeterminateVisibility(z);
    }

    public void setResizable(boolean z) {
        this.mActionView.setResizable(z);
    }

    public boolean isResizable() {
        return this.mActionView.isResizable();
    }

    public int getExpandState() {
        return this.mActionView.getExpandState();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // miuix.appcompat.app.ActionBar
    public void registerCoordinateScrollView(View view) {
        if (view == 0) {
            Log.w("miuix-appcompat", "warning!! the view is null on registerCoordinateScrollView!!");
            return;
        }
        if (view instanceof NestedContentInsetObserver) {
            NestedContentInsetObserver nestedContentInsetObserver = (NestedContentInsetObserver) view;
            this.mNestedContentInsetObserverSet.add(nestedContentInsetObserver);
            Rect rect = this.mContentInset;
            if (rect != null) {
                nestedContentInsetObserver.onContentInsetChanged(rect);
            }
        } else {
            HashMap map = this.mCoordinateOffsetViewSet;
            Rect rect2 = this.mContentInset;
            map.put(view, Integer.valueOf(rect2 != null ? rect2.top : UNINITIALIZED_OFFSET.intValue()));
            Rect rect3 = this.mContentInset;
            if (rect3 != null) {
                this.mCoordinateOffsetViewSet.put(view, Integer.valueOf(rect3.top));
                doUpdateTopOffsetForCoordinateView(view, this.mContentInset.top);
            }
        }
        if (this.mContainerView.getActionBarCoordinateListener() == null) {
            ActionBarCoordinateListener actionBarCoordinateListenerCreateActionBarCoordinateListener = createActionBarCoordinateListener();
            this.mOwnCoordinateListener = actionBarCoordinateListenerCreateActionBarCoordinateListener;
            this.mContainerView.setActionBarCoordinateListener(actionBarCoordinateListenerCreateActionBarCoordinateListener);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // miuix.appcompat.app.ActionBar
    public void unregisterCoordinateScrollView(View view) {
        if (view instanceof NestedContentInsetObserver) {
            this.mNestedContentInsetObserverSet.remove((NestedContentInsetObserver) view);
        } else {
            this.mCoordinateOffsetViewSet.remove(view);
        }
        if (this.mCoordinateOffsetViewSet.size() == 0 && this.mNestedContentInsetObserverSet.size() == 0 && this.mOwnCoordinateListener != null && this.mContainerView.getActionBarCoordinateListener() == this.mOwnCoordinateListener) {
            this.mContainerView.setActionBarCoordinateListener(null);
            this.mOwnCoordinateListener = null;
        }
    }

    @Override // miuix.appcompat.app.ActionBar
    public void addActionBarTransitionListener(ActionBarTransitionListener actionBarTransitionListener) {
        this.mContainerView.addActionBarTransitionListener(actionBarTransitionListener);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setActionBarStrategy(IActionBarStrategy iActionBarStrategy) {
        this.mActionBarStrategy = iActionBarStrategy;
        this.mOverlayLayout.post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ActionBarImpl actionBarImpl = this.f$0;
                actionBarImpl.applyActionBarStrategy(actionBarImpl.mActionView, actionBarImpl.mContextView);
            }
        });
    }

    public View getContentView() {
        ActionBarOverlayLayout actionBarOverlayLayout = this.mOverlayLayout;
        if (actionBarOverlayLayout != null) {
            return actionBarOverlayLayout.findViewById(R.id.content);
        }
        return null;
    }

    private void updateVisibility(boolean z) {
        updateVisibility(z, true, null);
    }

    private void updateVisibility(boolean z, boolean z2, AnimState animState) {
        boolean zCheckShowingFlags = checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode);
        boolean z3 = this.mNowShowing;
        if (zCheckShowingFlags) {
            if (z3) {
                return;
            }
            this.mNowShowing = true;
            doShow(z, z2, animState);
            return;
        }
        if (z3) {
            this.mNowShowing = false;
            doHide(z, z2, animState);
        }
    }

    private IStateStyle startContainerViewAnimation(boolean z, String str, AnimState animState, AnimState animState2) {
        AnimState animStateAdd;
        int height = this.mContainerView.getHeight();
        if (height == 0) {
            int childMeasureSpec = ViewGroup.getChildMeasureSpec(this.mOverlayLayout.getMeasuredWidth(), 0, this.mOverlayLayout.getLayoutParams().width);
            int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(this.mOverlayLayout.getMeasuredHeight(), 0, this.mOverlayLayout.getLayoutParams().height);
            this.mContainerView.measure(childMeasureSpec, childMeasureSpec2);
            applyActionBarStrategy(this.mActionView, this.mContextView);
            this.mContainerView.measure(childMeasureSpec, childMeasureSpec2);
            height = this.mContainerView.getMeasuredHeight();
        }
        int i = -height;
        this.mTargetTranslationY = i;
        AnimConfig animConfig = new AnimConfig();
        animConfig.addListeners(this.mContainerViewAnimationListener);
        if (z) {
            animConfig.setEase(EaseManager.getStyle(-2, 0.9f, 0.25f));
            animConfig.addListeners(new ViewShowTransitionListener(this.mContainerView, this));
            animStateAdd = animState2 == null ? new AnimState(str).add(ViewProperty.TRANSLATION_Y, 0.0d).add(ViewProperty.ALPHA, 1.0d) : animState2;
            if (animState == null && animState2 == null) {
                animState = new AnimState(str).add(ViewProperty.TRANSLATION_Y, i).add(ViewProperty.ALPHA, 0.0d);
            }
        } else {
            animConfig.setEase(EaseManager.getStyle(-2, 1.0f, 0.35f));
            animConfig.addListeners(new ViewHideTransitionListener(this.mContainerView, this));
            animStateAdd = animState2 == null ? new AnimState(str).add(ViewProperty.TRANSLATION_Y, i).add(ViewProperty.ALPHA, 0.0d) : animState2;
            if (animState == null && animState2 == null) {
                animState = new AnimState(str).add(ViewProperty.TRANSLATION_Y, 0.0d).add(ViewProperty.ALPHA, 1.0d);
            }
        }
        IStateStyle iStateStyleState = Folme.useAt(this.mContainerView).state();
        if (animState != null) {
            animState.setTag(str);
            iStateStyleState = iStateStyleState.setTo(animState);
        }
        iStateStyleState.to(animStateAdd, animConfig);
        this.mIsContainerAnimationRunning = true;
        return iStateStyleState;
    }

    private void doShow(boolean z) {
        doShow(z, true, null);
    }

    private void doShow(boolean z, boolean z2, AnimState animState) {
        AnimState currentState;
        IStateStyle iStateStyle = this.mContainerAnim;
        if (iStateStyle == null || !this.mIsContainerAnimationRunning) {
            currentState = null;
        } else {
            currentState = iStateStyle.getCurrentState();
            this.mContainerAnim.cancel();
        }
        boolean z3 = (isShowHideAnimationEnabled() || z) && z2;
        boolean z4 = this.mActionMode instanceof SearchActionMode;
        ActionBarContainer actionBarContainer = this.mContainerView;
        if (z4) {
            actionBarContainer.setVisibility(this.mOverlayLayout.isInOverlayMode() ? 4 : 8);
        } else {
            actionBarContainer.setVisibility(0);
        }
        this.mContainerView.resetActionBarBlurConfigOnReshow();
        if (z3) {
            this.mContainerAnim = startContainerViewAnimation(true, "ShowActionBar", currentState, animState);
        } else {
            this.mContainerView.setTranslationY(0.0f);
            this.mContainerView.setAlpha(1.0f);
        }
    }

    private void doHide(boolean z) {
        doHide(z, true, null);
    }

    private void doHide(boolean z, boolean z2, AnimState animState) {
        AnimState currentState;
        IStateStyle iStateStyle = this.mContainerAnim;
        if (iStateStyle == null || !this.mIsContainerAnimationRunning) {
            currentState = null;
        } else {
            currentState = iStateStyle.getCurrentState();
            this.mContainerAnim.cancel();
        }
        if ((isShowHideAnimationEnabled() || z) && z2) {
            this.mContainerAnim = startContainerViewAnimation(false, "HideActionBar", currentState, animState);
            return;
        }
        ActionBarContainer actionBarContainer = this.mContainerView;
        actionBarContainer.setTranslationY(-actionBarContainer.getHeight());
        this.mContainerView.setAlpha(0.0f);
        this.mActionBarHeightTotalGap = 0;
        this.mCurrentActionBarHeightGap = 0;
        this.mContainerView.setVisibility(8);
    }

    boolean isShowHideAnimationEnabled() {
        return this.mShowHideAnimationEnabled;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setShowHideAnimationEnabled(boolean z) {
        this.mShowHideAnimationEnabled = z;
        if (z) {
            return;
        }
        if (isShowing()) {
            doShow(false);
        } else {
            doHide(false);
        }
    }

    class ViewShowTransitionListener extends TransitionListener {
        private WeakReference mActionBarRef;
        private WeakReference mRef;

        public ViewShowTransitionListener(View view, ActionBarImpl actionBarImpl) {
            this.mRef = new WeakReference(view);
            this.mActionBarRef = new WeakReference(actionBarImpl);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            ActionBarImpl actionBarImpl = (ActionBarImpl) this.mActionBarRef.get();
            View view = (View) this.mRef.get();
            if (view == null || actionBarImpl == null || !actionBarImpl.mNowShowing || view.getId() != R$id.action_bar_container) {
                return;
            }
            view.setTranslationY(0.0f);
        }
    }

    class ViewHideTransitionListener extends TransitionListener {
        private WeakReference mActionBarRef;
        private WeakReference mRef;

        public ViewHideTransitionListener(View view, ActionBarImpl actionBarImpl) {
            this.mRef = new WeakReference(view);
            this.mActionBarRef = new WeakReference(actionBarImpl);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            ActionBarImpl actionBarImpl = (ActionBarImpl) this.mActionBarRef.get();
            View view = (View) this.mRef.get();
            if (view == null || actionBarImpl == null || actionBarImpl.mNowShowing) {
                return;
            }
            view.setVisibility(8);
            if (view.getId() == R$id.action_bar_container) {
                view.setTranslationY(-view.getHeight());
            }
        }
    }

    int getTopViewHeight() {
        ActionModeView actionModeView;
        if (this.mActionMode != null && (actionModeView = this.mActionModeView) != null) {
            return actionModeView.getViewHeight();
        }
        if (this.mActionView.isCollapsed()) {
            return 0;
        }
        return this.mActionView.getCollapsedHeight();
    }

    int updateTopOffsetOnNestedPreScroll(View view, int i) {
        if (!this.mCoordinateOffsetViewSet.containsKey(view)) {
            return 0;
        }
        Integer coordinateOffsetViewTopOffsetOrDefault = getCoordinateOffsetViewTopOffsetOrDefault(view);
        if (coordinateOffsetViewTopOffsetOrDefault.intValue() <= i) {
            return 0;
        }
        this.mCoordinateOffsetViewSet.put(view, Integer.valueOf(i));
        doUpdateTopOffsetForCoordinateView(view, i);
        return coordinateOffsetViewTopOffsetOrDefault.intValue() - i;
    }

    private Integer getCoordinateOffsetViewTopOffsetOrDefault(View view) {
        Integer num = (Integer) this.mCoordinateOffsetViewSet.get(view);
        return Integer.valueOf(Objects.equals(num, UNINITIALIZED_OFFSET) ? 0 : num.intValue());
    }

    int updateTopOffsetOnNestedScroll(View view, int i) {
        int i2 = 0;
        for (View view2 : this.mCoordinateOffsetViewSet.keySet()) {
            int iIntValue = getCoordinateOffsetViewTopOffsetOrDefault(view2).intValue();
            int i3 = iIntValue - i;
            Rect rect = this.mContentInset;
            int iMin = Math.min(i3, rect == null ? 0 : rect.top);
            if (iIntValue < iMin) {
                this.mCoordinateOffsetViewSet.put(view2, Integer.valueOf(iMin));
                doUpdateTopOffsetForCoordinateView(view2, iMin);
                if (view == view2) {
                    i2 = iIntValue - iMin;
                }
            }
        }
        return i2;
    }

    void updateTopOffsetOnPostScroll(View view, int i) {
        for (View view2 : this.mCoordinateOffsetViewSet.keySet()) {
            if (view == view2) {
                int iIntValue = getCoordinateOffsetViewTopOffsetOrDefault(view2).intValue();
                Rect rect = this.mContentInset;
                int iMin = Math.min(i, rect == null ? 0 : rect.top);
                if (iIntValue != iMin) {
                    this.mCoordinateOffsetViewSet.put(view2, Integer.valueOf(iMin));
                    doUpdateTopOffsetForCoordinateView(view2, iMin);
                }
            }
        }
    }

    int getTopOffsetForCoordinateView(View view) {
        if (this.mCoordinateOffsetViewSet.containsKey(view)) {
            return getCoordinateOffsetViewTopOffsetOrDefault(view).intValue();
        }
        return -1;
    }

    void updateContentInsetForNestedObserver(Rect rect) {
        this.mContentInset = rect;
        int i = rect.top;
        int i2 = i - this.mContentInsetTop;
        this.mContentInsetTop = i;
        Iterator it = this.mNestedContentInsetObserverSet.iterator();
        while (it.hasNext()) {
            ((NestedContentInsetObserver) it.next()).onContentInsetChanged(rect);
        }
        for (View view : this.mCoordinateOffsetViewSet.keySet()) {
            Integer num = (Integer) this.mCoordinateOffsetViewSet.get(view);
            if (num != null && i2 != 0) {
                if (num.equals(UNINITIALIZED_OFFSET)) {
                    num = 0;
                } else if (num.intValue() == 0) {
                }
                int iMax = Math.max(0, num.intValue() + i2);
                this.mCoordinateOffsetViewSet.put(view, Integer.valueOf(iMax));
                doUpdateTopOffsetForCoordinateView(view, iMax);
            }
        }
    }

    void updateCoordinateOffsetView() {
        if (this.mCoordinateOffsetViewSet.size() == 0 && this.mNestedContentInsetObserverSet.size() == 0) {
            if (this.mOwnCoordinateListener == null || this.mContainerView.getActionBarCoordinateListener() != this.mOwnCoordinateListener) {
                return;
            }
            this.mContainerView.setActionBarCoordinateListener(null);
            this.mOwnCoordinateListener = null;
            return;
        }
        for (View view : this.mCoordinateOffsetViewSet.keySet()) {
            doUpdateTopOffsetForCoordinateView(view, getCoordinateOffsetViewTopOffsetOrDefault(view).intValue());
        }
        Iterator it = this.mNestedContentInsetObserverSet.iterator();
        while (it.hasNext()) {
            doUpdateTopOffsetForCoordinateView((View) ((NestedContentInsetObserver) it.next()), 0);
        }
    }

    private void doUpdateTopOffsetForCoordinateView(View view, int i) {
        int top = view.getTop();
        int i2 = this.mCurrentActionBarHeightGap;
        if (top != i2 + i) {
            view.offsetTopAndBottom((Math.max(0, i2) + i) - top);
        }
    }

    protected ActionBarCoordinateListener createActionBarCoordinateListener() {
        return new ActionBarCoordinateListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl$$ExternalSyntheticLambda0
            @Override // miuix.appcompat.internal.app.widget.ActionBarCoordinateListener
            public final void onActionBarResizing(int i, float f, int i2, int i3) {
                ActionBarImpl.$r8$lambda$cXU2ox_wcuV6zykg8U20_3QSuww(this.f$0, i, f, i2, i3);
            }
        };
    }

    public static /* synthetic */ void $r8$lambda$cXU2ox_wcuV6zykg8U20_3QSuww(ActionBarImpl actionBarImpl, int i, float f, int i2, int i3) {
        actionBarImpl.mCurrentActionBarHeightGapOnShow = i2;
        actionBarImpl.mActionBarHeightTotalGapOnShow = i3;
        float height = (actionBarImpl.mContainerView.getHeight() + actionBarImpl.mContainerView.getTranslationY()) / actionBarImpl.mContainerView.getHeight();
        float f2 = actionBarImpl.mTargetTranslationY;
        if (f2 != 0.0f) {
            height = (f2 - actionBarImpl.mContainerView.getTranslationY()) / actionBarImpl.mTargetTranslationY;
        }
        if (actionBarImpl.mContainerView.getHeight() == 0) {
            height = 1.0f;
        }
        actionBarImpl.mCurrentActionBarHeightGap = (int) (actionBarImpl.mCurrentActionBarHeightGapOnShow * height);
        actionBarImpl.mActionBarHeightTotalGap = (int) (actionBarImpl.mActionBarHeightTotalGapOnShow * height);
    }
}
