package miuix.appcompat.app;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import androidx.activity.OnBackPressedCallback;
import androidx.core.view.ScrollingView;
import androidx.lifecycle.LifecycleOwner;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.List;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$id;
import miuix.appcompat.R$integer;
import miuix.appcompat.internal.app.widget.ActionBarContainer;
import miuix.appcompat.internal.app.widget.ActionBarContextView;
import miuix.appcompat.internal.app.widget.ActionBarImpl;
import miuix.appcompat.internal.app.widget.ActionBarOverlayLayout;
import miuix.appcompat.internal.app.widget.ActionBarView;
import miuix.appcompat.internal.view.menu.ImmersionMenuPopupWindow;
import miuix.appcompat.internal.view.menu.ImmersionMenuPopupWindowImpl;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuPresenter;
import miuix.container.ExtraPaddingObserver;
import miuix.container.ExtraPaddingPolicy;
import miuix.core.util.MiuixUIUtils;
import miuix.core.util.variable.WindowWrapper;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.ViewUtils;
import miuix.os.DeviceHelper;
import miuix.theme.token.ContainerToken;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ActionBarDelegateImpl implements ActionBarDelegate, ExtraPaddingObserver, MenuPresenter.Callback, MenuBuilder.Callback {
    protected ActionBar mActionBar;
    protected ActionBarView mActionBarView;
    protected ActionMode mActionMode;
    final AppCompatActivity mActivity;
    protected Rect mContentInset;
    protected int mDeviceType;
    private boolean mEndActionMenuEnabled;
    protected boolean mExtraPaddingApplyToContentEnable;
    protected boolean mExtraPaddingEnable;
    protected boolean mExtraPaddingInitEnable;
    protected ExtraPaddingPolicy mExtraPaddingPolicy;
    protected boolean mFeatureIndeterminateProgress;
    protected boolean mFeatureProgress;
    protected boolean mHasActionBar;
    private boolean mHyperActionMenuEnabled;
    private boolean mHyperSplitMenuEnabled;
    private MenuBuilder mImmersionMenu;
    private boolean mImmersionMenuEnabled;
    protected MenuBuilder mMenu;
    private MenuInflater mMenuInflater;
    private ImmersionMenuPopupWindow mMenuPopupWindow;
    private OnBackPressedCallback mOnBackPressedCallback;
    protected MenuBuilder mOriginMenu;
    protected boolean mOverlayActionBar;
    protected boolean mSubDecorInstalled;
    protected boolean mUserExtraPaddingPolicy;
    protected View mViewWithContentInset;
    protected ViewUtils.RelativePadding mViewWithContentInsetInitPadding;
    private int mTranslucentStatus = 0;
    protected boolean mHasAddSplitActionBar = false;
    protected int mExtraHorizontalPadding = 0;
    protected List mExtraPaddingObserver = null;
    protected boolean mIsDelegateAnimRunning = false;
    protected boolean mIsFloatingPanelMode = false;
    protected int mFloatingPanelMarginStart = -1;
    protected int mFloatingPanelMarginTop = -1;
    protected int mFloatingPanelMarginEnd = -1;
    protected int mFloatingPanelMarginBottom = -1;
    protected int mFloatingPanelCornerSide = 0;
    protected boolean mFloatingPanelDynamicCornerEnabled = true;

    public abstract LifecycleOwner getLifecycleOwner();

    public abstract Context getThemedContext();

    public abstract View getView();

    protected abstract boolean onCreateImmersionMenu(MenuBuilder menuBuilder);

    public abstract /* synthetic */ boolean onMenuItemSelected(int i, MenuItem menuItem);

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter.Callback
    public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
        return false;
    }

    protected abstract boolean onPrepareImmersionMenu(MenuBuilder menuBuilder);

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return null;
    }

    ActionBarDelegateImpl(AppCompatActivity appCompatActivity) {
        this.mActivity = appCompatActivity;
        this.mDeviceType = DeviceHelper.detectType(appCompatActivity);
    }

    public boolean hasActionBar() {
        return this.mHasActionBar || this.mOverlayActionBar;
    }

    public ActionBar getActionBar() {
        if (hasActionBar()) {
            if (this.mActionBar == null) {
                this.mActionBar = createActionBar();
            }
        } else {
            this.mActionBar = null;
        }
        return this.mActionBar;
    }

    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                this.mMenuInflater = new MenuInflater(actionBar.getThemedContext());
            } else {
                this.mMenuInflater = new MenuInflater(this.mActivity);
            }
        }
        return this.mMenuInflater;
    }

    protected final String getUiOptionsFromMetadata() {
        try {
            Bundle bundle = this.mActivity.getPackageManager().getActivityInfo(this.mActivity.getComponentName(), DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY).metaData;
            if (bundle != null) {
                return bundle.getString("android.support.UI_OPTIONS");
            }
            return null;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("ActionBarDelegate", "getUiOptionsFromMetadata: Activity '" + this.mActivity.getClass().getSimpleName() + "' not in manifest");
            return null;
        }
    }

    protected final Context getActionBarThemedContext() {
        AppCompatActivity appCompatActivity = this.mActivity;
        ActionBar actionBar = getActionBar();
        return actionBar != null ? actionBar.getThemedContext() : appCompatActivity;
    }

    public AppCompatActivity getActivity() {
        return this.mActivity;
    }

    protected void updateActionBarOverlayLayoutParams(ActionBarOverlayLayout actionBarOverlayLayout, boolean z, boolean z2) {
        actionBarOverlayLayout.setFloatingPanelMode(z);
        if (z && !actionBarOverlayLayout.isFloatingWindowMode()) {
            Context context = actionBarOverlayLayout.getContext();
            int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_floating_panel_default_margin);
            int floatingPanelMarginFromAttr = this.mFloatingPanelMarginStart;
            if (floatingPanelMarginFromAttr < 0) {
                floatingPanelMarginFromAttr = getFloatingPanelMarginFromAttr(context, R$attr.floatingPanelMarginStart, dimensionPixelOffset);
            }
            int floatingPanelMarginFromAttr2 = this.mFloatingPanelMarginTop;
            if (floatingPanelMarginFromAttr2 < 0) {
                floatingPanelMarginFromAttr2 = getFloatingPanelMarginFromAttr(context, R$attr.floatingPanelMarginTop, dimensionPixelOffset);
            }
            int floatingPanelMarginFromAttr3 = this.mFloatingPanelMarginEnd;
            if (floatingPanelMarginFromAttr3 < 0) {
                floatingPanelMarginFromAttr3 = getFloatingPanelMarginFromAttr(context, R$attr.floatingPanelMarginEnd, dimensionPixelOffset);
            }
            int floatingPanelMarginFromAttr4 = this.mFloatingPanelMarginBottom;
            if (floatingPanelMarginFromAttr4 < 0) {
                floatingPanelMarginFromAttr4 = getFloatingPanelMarginFromAttr(context, R$attr.floatingPanelMarginBottom, dimensionPixelOffset);
            }
            float dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_floating_panel_default_radius);
            float fDp2px = MiuixUIUtils.dp2px(context, 32.0f);
            int cornerInsetMargin = getCornerInsetMargin(floatingPanelMarginFromAttr, floatingPanelMarginFromAttr2, floatingPanelMarginFromAttr3, floatingPanelMarginFromAttr4);
            if (this.mFloatingPanelDynamicCornerEnabled) {
                dimensionPixelSize = MiuixUIUtils.getWidgetCornerRadius(context, cornerInsetMargin, 3, dimensionPixelSize, fDp2px);
            }
            actionBarOverlayLayout.setFloatingPanelUserMarginTop(floatingPanelMarginFromAttr2);
            actionBarOverlayLayout.setFloatingPanelUserMarginBottom(floatingPanelMarginFromAttr4);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) actionBarOverlayLayout.getLayoutParams();
            marginLayoutParams.setMarginStart(floatingPanelMarginFromAttr);
            marginLayoutParams.setMarginEnd(floatingPanelMarginFromAttr3);
            marginLayoutParams.bottomMargin = floatingPanelMarginFromAttr4;
            actionBarOverlayLayout.setLayoutParams(marginLayoutParams);
            actionBarOverlayLayout.setRadius(dimensionPixelSize);
            actionBarOverlayLayout.requestApplyInsets();
            return;
        }
        actionBarOverlayLayout.setFloatingPanelUserMarginTop(-1);
        actionBarOverlayLayout.setFloatingPanelUserMarginBottom(-1);
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) actionBarOverlayLayout.getLayoutParams();
        marginLayoutParams2.setMarginStart(0);
        marginLayoutParams2.topMargin = 0;
        marginLayoutParams2.setMarginEnd(0);
        marginLayoutParams2.bottomMargin = 0;
        actionBarOverlayLayout.setLayoutParams(marginLayoutParams2);
        actionBarOverlayLayout.setRadius(0.0f);
    }

    private int getCornerInsetMargin(int i, int i2, int i3, int i4) {
        int i5 = this.mFloatingPanelCornerSide;
        if (i5 == 1) {
            return i2;
        }
        if (i5 != 2) {
            return i5 != 3 ? i : i4;
        }
        return i3;
    }

    private int getFloatingPanelMarginFromAttr(Context context, int i, int i2) {
        try {
            TypedValue typedValueResolveTypedValue = AttributeResolver.resolveTypedValue(context, i);
            if (typedValueResolveTypedValue != null && typedValueResolveTypedValue.type == 5) {
                if (typedValueResolveTypedValue.resourceId > 0) {
                    return context.getResources().getDimensionPixelSize(typedValueResolveTypedValue.resourceId);
                }
                return TypedValue.complexToDimensionPixelSize(typedValueResolveTypedValue.data, context.getResources().getDisplayMetrics());
            }
            return i2;
        } catch (Exception e) {
            Log.w("ActionBarDelegate", "Failed to resolve floating panel margin attr", e);
            return i2;
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        ActionBarImpl actionBarImpl;
        if (this.mHasActionBar && this.mSubDecorInstalled && (actionBarImpl = (ActionBarImpl) getActionBar()) != null) {
            actionBarImpl.onConfigurationChanged(configuration);
        }
    }

    public void onDestroy() {
        ActionBarImpl actionBarImpl;
        ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        if (this.mHasActionBar && this.mSubDecorInstalled && (actionBarImpl = (ActionBarImpl) getActionBar()) != null) {
            actionBarImpl.onDestroy();
        }
    }

    public boolean requestWindowFeature(int i) {
        if (i == 2) {
            this.mFeatureProgress = true;
            return true;
        }
        if (i == 5) {
            this.mFeatureIndeterminateProgress = true;
            return true;
        }
        if (i == 8) {
            this.mHasActionBar = true;
            return true;
        }
        if (i == 9) {
            this.mOverlayActionBar = true;
            return true;
        }
        return this.mActivity.requestWindowFeature(i);
    }

    @Override // miuix.appcompat.app.IContentInsetState
    public void onContentInsetChanged(Rect rect) {
        this.mContentInset = rect;
    }

    public void onProcessBindViewWithContentInset(Rect rect) {
        if (this.mViewWithContentInset == null) {
            return;
        }
        ViewUtils.RelativePadding relativePadding = new ViewUtils.RelativePadding(this.mViewWithContentInsetInitPadding);
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this.mViewWithContentInset);
        relativePadding.start += zIsLayoutRtl ? rect.right : rect.left;
        relativePadding.top += rect.top;
        relativePadding.end += zIsLayoutRtl ? rect.left : rect.right;
        View view = this.mViewWithContentInset;
        if ((view instanceof ViewGroup) && (view instanceof ScrollingView)) {
            relativePadding.applyToScrollingView((ViewGroup) view);
        } else {
            relativePadding.applyToView(view);
        }
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
        if (i == 0) {
            return onWindowStartingActionMode(callback);
        }
        return null;
    }

    public void onActionModeStarted(ActionMode actionMode) {
        this.mActionMode = actionMode;
        updateOnBackPressedCallbackState(true);
    }

    public void onActionModeFinished(ActionMode actionMode) {
        this.mActionMode = null;
        updateOnBackPressedCallbackState(false);
    }

    private void updateOnBackPressedCallbackState(boolean z) {
        OnBackPressedCallback onBackPressedCallback = this.mOnBackPressedCallback;
        if (onBackPressedCallback == null) {
            this.mOnBackPressedCallback = new OnBackPressedCallback(z) { // from class: miuix.appcompat.app.ActionBarDelegateImpl.1
                @Override // androidx.activity.OnBackPressedCallback
                public void handleOnBackPressed() {
                    ActionMode actionMode;
                    ActionBarDelegateImpl actionBarDelegateImpl = ActionBarDelegateImpl.this;
                    if (actionBarDelegateImpl.mIsDelegateAnimRunning || (actionMode = actionBarDelegateImpl.mActionMode) == null) {
                        return;
                    }
                    actionMode.finish();
                }
            };
            this.mActivity.getOnBackPressedDispatcher().addCallback(getLifecycleOwner(), this.mOnBackPressedCallback);
        } else {
            onBackPressedCallback.setEnabled(z);
        }
    }

    public void setTranslucentStatus(int i) {
        int integer = this.mActivity.getResources().getInteger(R$integer.window_translucent_status);
        if (integer >= 0 && integer <= 2) {
            i = integer;
        }
        if (this.mTranslucentStatus == i || !WindowWrapper.setTranslucentStatus(this.mActivity.getWindow(), i)) {
            return;
        }
        this.mTranslucentStatus = i;
    }

    public int getTranslucentStatus() {
        return this.mTranslucentStatus;
    }

    protected void reopenMenu(MenuBuilder menuBuilder, boolean z) {
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null && actionBarView.isOverflowReserved()) {
            if (!this.mActionBarView.isOverflowMenuShowing() || !z) {
                if (this.mActionBarView.getVisibility() == 0) {
                    this.mActionBarView.showOverflowMenu();
                    return;
                }
                return;
            }
            this.mActionBarView.hideOverflowMenu();
            return;
        }
        menuBuilder.close();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setMenu(MenuBuilder menuBuilder) {
        if (menuBuilder == this.mMenu) {
            return;
        }
        this.mMenu = menuBuilder;
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.setMenu(menuBuilder, this);
            if (this.mActionBarView.isEndActionMenuEnable()) {
                onPanelViewAdded(0, null, this.mMenu, this.mActionBarView.getEndMenu());
            }
        }
    }

    protected MenuBuilder createMenu() {
        MenuBuilder menuBuilder = new MenuBuilder(getActionBarThemedContext());
        menuBuilder.setCallback(this);
        this.mOriginMenu = menuBuilder;
        return menuBuilder;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter.Callback
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        this.mActivity.closeOptionsMenu();
    }

    @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
    public void onMenuModeChange(MenuBuilder menuBuilder) {
        reopenMenu(menuBuilder, true);
    }

    public boolean isEndActionMenuEnabled() {
        return this.mEndActionMenuEnabled;
    }

    public void setHyperSplitMenuEnabled(boolean z) {
        this.mHyperSplitMenuEnabled = z;
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.setHyperSplitMenuEnabled(z);
        }
    }

    public void setEndActionMenuEnabled(boolean z, boolean z2, boolean z3) {
        this.mEndActionMenuEnabled = z;
        this.mHyperActionMenuEnabled = z2;
        if (this.mSubDecorInstalled && this.mHasActionBar) {
            this.mActionBarView.setEndActionMenuEnable(z);
            this.mActionBarView.setHyperActionMenuEnable(z2);
            this.mActionBarView.setHyperSplitMenuEnabled(this.mHyperSplitMenuEnabled);
            if (z3) {
                invalidateOptionsMenu();
            } else {
                this.mActivity.getWindow().getDecorView().post(new Runnable() { // from class: miuix.appcompat.app.ActionBarDelegateImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.invalidateOptionsMenu();
                    }
                });
            }
        }
    }

    public boolean isImmersionMenuShowing() {
        ImmersionMenuPopupWindow immersionMenuPopupWindow = this.mMenuPopupWindow;
        if (immersionMenuPopupWindow instanceof ImmersionMenuPopupWindowImpl) {
            return immersionMenuPopupWindow.isShowing();
        }
        return false;
    }

    public void showImmersionMenu() {
        View viewFindViewById;
        ImmersionMenuPopupWindow immersionMenuPopupWindow = this.mMenuPopupWindow;
        if (immersionMenuPopupWindow instanceof ImmersionMenuPopupWindowImpl) {
            View lastAnchor = ((ImmersionMenuPopupWindowImpl) immersionMenuPopupWindow).getLastAnchor();
            ViewGroup lastParent = ((ImmersionMenuPopupWindowImpl) this.mMenuPopupWindow).getLastParent();
            if (lastAnchor != null) {
                showImmersionMenu(lastAnchor, lastParent);
                return;
            }
        }
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null && (viewFindViewById = actionBarView.findViewById(R$id.more)) != null) {
            showImmersionMenu(viewFindViewById, this.mActionBarView);
        } else {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Can't find anchor view in actionbar or any other anchor view used before. Do you use default actionbar and immersion menu is enabled?");
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void showImmersionMenu(View view, ViewGroup viewGroup) {
        if (!this.mImmersionMenuEnabled) {
            Log.w("ActionBarDelegate", "Try to show immersion menu when immersion menu disabled");
            return;
        }
        if (view == null) {
            Folme$$ExternalSyntheticBUOutline0.m("You must specify a valid anchor view");
            return;
        }
        if (this.mImmersionMenu == null) {
            MenuBuilder menuBuilderCreateMenu = createMenu();
            this.mImmersionMenu = menuBuilderCreateMenu;
            onCreateImmersionMenu(menuBuilderCreateMenu);
        }
        if (onPrepareImmersionMenu(this.mImmersionMenu) && this.mImmersionMenu.hasVisibleItems()) {
            ImmersionMenuPopupWindow immersionMenuPopupWindow = this.mMenuPopupWindow;
            if (immersionMenuPopupWindow == null) {
                ImmersionMenuPopupWindowImpl immersionMenuPopupWindowImpl = new ImmersionMenuPopupWindowImpl(this, this.mImmersionMenu, getView());
                immersionMenuPopupWindowImpl.setDropDownGravity(49);
                immersionMenuPopupWindowImpl.setHorizontalOffset(0);
                immersionMenuPopupWindowImpl.setVerticalOffset(0);
                this.mMenuPopupWindow = immersionMenuPopupWindowImpl;
            } else {
                immersionMenuPopupWindow.update(this.mImmersionMenu);
            }
            if (this.mMenuPopupWindow.isShowing()) {
                return;
            }
            this.mMenuPopupWindow.show(view, null);
        }
    }

    public void dismissImmersionMenu(boolean z) {
        ImmersionMenuPopupWindow immersionMenuPopupWindow = this.mMenuPopupWindow;
        if (immersionMenuPopupWindow != null) {
            immersionMenuPopupWindow.dismiss(z);
        }
    }

    public void addSplitActionBar(boolean z, boolean z2, ActionBarOverlayLayout actionBarOverlayLayout) {
        ActionBarContainer actionBarContainer;
        ActionBarContextView actionBarContextView;
        if (this.mHasAddSplitActionBar) {
            return;
        }
        this.mHasAddSplitActionBar = true;
        ViewStub viewStub = (ViewStub) actionBarOverlayLayout.findViewById(R$id.split_action_bar_vs);
        if (viewStub != null) {
            actionBarContainer = (ActionBarContainer) viewStub.inflate();
        } else {
            actionBarContainer = (ActionBarContainer) actionBarOverlayLayout.findViewById(R$id.split_action_bar);
        }
        if (actionBarContainer != null) {
            this.mActionBarView.setSplitView(actionBarContainer);
            this.mActionBarView.setSplitActionBar(z);
            this.mActionBarView.setSplitWhenNarrow(z2);
            actionBarOverlayLayout.setSplitActionBarView(actionBarContainer);
            addContentMask(actionBarOverlayLayout);
        }
        ActionBarContainer actionBarContainer2 = (ActionBarContainer) actionBarOverlayLayout.findViewById(R$id.action_bar_container);
        ViewStub viewStub2 = (ViewStub) actionBarOverlayLayout.findViewById(R$id.action_context_bar_vs);
        if (viewStub2 != null) {
            actionBarContextView = (ActionBarContextView) viewStub2.inflate();
        } else {
            actionBarContextView = (ActionBarContextView) actionBarOverlayLayout.findViewById(R$id.action_context_bar);
        }
        if (actionBarContextView != null) {
            actionBarContainer2.setActionBarContextView(actionBarContextView);
            actionBarOverlayLayout.setActionBarContextView(actionBarContextView);
            if (actionBarContainer != null) {
                actionBarContextView.setSplitView(actionBarContainer);
                actionBarContextView.setSplitActionBar(z);
                actionBarContextView.setSplitWhenNarrow(z2);
            }
        }
    }

    public void addContentMask(ActionBarOverlayLayout actionBarOverlayLayout) {
        View viewFindViewById;
        if (actionBarOverlayLayout != null) {
            ViewStub viewStub = (ViewStub) actionBarOverlayLayout.findViewById(R$id.content_mask_vs);
            if (viewStub != null) {
                viewFindViewById = viewStub.inflate();
            } else {
                viewFindViewById = actionBarOverlayLayout.findViewById(R$id.content_mask);
            }
            actionBarOverlayLayout.setContentMask(viewFindViewById);
        }
    }

    public void registerCoordinateScrollView(View view) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.registerCoordinateScrollView(view);
        }
    }

    public void unregisterCoordinateScrollView(View view) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.unregisterCoordinateScrollView(view);
        }
    }

    @Override // miuix.container.ExtraPaddingObserver
    public boolean setExtraHorizontalPadding(int i) {
        if (this.mExtraHorizontalPadding == i) {
            return false;
        }
        this.mExtraHorizontalPadding = i;
        return true;
    }

    public int getExtraHorizontalPadding() {
        return this.mExtraHorizontalPadding;
    }

    public ExtraPaddingPolicy getExtraPaddingPolicy() {
        return this.mExtraPaddingPolicy;
    }

    public void setExtraHorizontalPaddingEnable(boolean z) {
        this.mExtraPaddingEnable = z;
        ExtraPaddingPolicy extraPaddingPolicy = this.mExtraPaddingPolicy;
        if (extraPaddingPolicy != null) {
            extraPaddingPolicy.setEnable(z);
        }
    }

    public void setExtraHorizontalPaddingInitEnable(boolean z) {
        this.mExtraPaddingInitEnable = z;
    }

    public void setExtraPaddingApplyToContentEnable(boolean z) {
        this.mExtraPaddingApplyToContentEnable = z;
    }

    protected void initExtraPaddingPolicy() {
        ExtraPaddingPolicy extraPaddingPolicyCreateDefault = ExtraPaddingPolicy.Builder.createDefault(this.mDeviceType, ContainerToken.PADDING_BASE_DP, ContainerToken.PADDING_HORIZONTAL_COMMON);
        this.mExtraPaddingPolicy = extraPaddingPolicyCreateDefault;
        if (extraPaddingPolicyCreateDefault != null) {
            extraPaddingPolicyCreateDefault.setEnable(this.mExtraPaddingEnable);
        }
    }
}
