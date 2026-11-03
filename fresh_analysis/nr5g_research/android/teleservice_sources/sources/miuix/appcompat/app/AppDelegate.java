package miuix.appcompat.app;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import java.util.Arrays;
import java.util.List;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$bool;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.floatingactivity.FloatingActivitySwitcher;
import miuix.appcompat.app.floatingactivity.OnFloatingCallback;
import miuix.appcompat.app.floatingactivity.OnFloatingModeCallback;
import miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper;
import miuix.appcompat.app.floatingactivity.helper.FloatingHelperFactory;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;
import miuix.appcompat.internal.app.widget.ActionBarContainer;
import miuix.appcompat.internal.app.widget.ActionBarImpl;
import miuix.appcompat.internal.app.widget.ActionBarOverlayLayout;
import miuix.appcompat.internal.app.widget.ActionBarView;
import miuix.appcompat.internal.util.AppcompatClassPreLoader;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.core.util.EnvStateManager;
import miuix.core.util.variable.WindowWrapper;
import miuix.internal.util.AttributeResolver;
import miuix.os.Build;
import miuix.os.DeviceHelper;
import miuix.responsive.interfaces.IResponsive;
import miuix.responsive.map.ScreenSpec;
import miuix.responsive.page.manager.BaseResponseStateManager;
import miuix.view.SearchActionMode;

/* JADX INFO: loaded from: classes2.dex */
class AppDelegate extends ActionBarDelegateImpl implements IResponsive {
    private ActionBarContainer mActionBarContainer;
    private ActivityCallback mActivityCallback;
    private final String mActivityIdentity;
    private AppCompatWindowCallback mAppCompatWindowCallback;
    private ViewGroup mContentParent;
    private boolean mDelegateFinished;
    private OnFloatingModeCallback mFloatingModeCallback;
    private ViewGroup mFloatingRoot;
    private BaseFloatingActivityHelper mFloatingWindowHelper;
    private final Runnable mInvalidateMenuRunnable;
    private boolean mIsFloatingTheme;
    private boolean mIsFloatingWindow;
    private boolean mIsUserResponsiveEnabled;
    private LayoutInflater mLayoutInflater;
    private BaseResponseStateManager mResponsiveStateManager;
    private boolean mSplitActionBarEnable;
    private ActionBarOverlayLayout mSubDecor;
    private CharSequence mTitle;
    private int mUIMode;
    private Runnable mUpdateWindowInfoRunnable;
    private Boolean mUserIsFloatingWindow;
    Window mWindow;

    AppDelegate(AppCompatActivity appCompatActivity, ActivityCallback activityCallback, OnFloatingModeCallback onFloatingModeCallback) {
        super(appCompatActivity);
        this.mIsUserResponsiveEnabled = false;
        this.mIsFloatingTheme = false;
        this.mIsFloatingWindow = false;
        this.mUserIsFloatingWindow = null;
        this.mFloatingRoot = null;
        this.mDelegateFinished = false;
        this.mInvalidateMenuRunnable = new Runnable() { // from class: miuix.appcompat.app.AppDelegate.3
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v1, types: [android.view.Menu, miuix.appcompat.internal.view.menu.MenuBuilder] */
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
            @Override // java.lang.Runnable
            public void run() {
                ?? CreateMenu = AppDelegate.this.createMenu();
                if ((AppDelegate.this.isEndActionMenuEnabled() || AppDelegate.this.mSplitActionBarEnable) && AppDelegate.this.mActivityCallback.onCreatePanelMenu(0, CreateMenu) && AppDelegate.this.mActivityCallback.onPreparePanel(0, null, CreateMenu)) {
                    AppDelegate.this.setMenu(CreateMenu);
                } else {
                    AppDelegate.this.setMenu(null);
                }
            }
        };
        this.mActivityIdentity = String.valueOf(SystemClock.elapsedRealtimeNanos());
        this.mActivityCallback = activityCallback;
        this.mFloatingModeCallback = onFloatingModeCallback;
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public LifecycleOwner getLifecycleOwner() {
        return this.mActivity;
    }

    public void setResponsiveEnabled(boolean z) {
        this.mIsUserResponsiveEnabled = z;
    }

    public void onCreate(Bundle bundle) {
        this.mActivity.checkThemeLegality();
        if (!AppcompatClassPreLoader.sIsActionBarClassInit) {
            AppcompatClassPreLoader.sIsActionBarClassInit = true;
            AppcompatClassPreLoader.preloadClass(getThemedContext().getApplicationContext());
        }
        boolean zResolveBoolean = AttributeResolver.resolveBoolean(this.mActivity, R$attr.windowExtraPaddingHorizontalEnable, AttributeResolver.resolveInt(this.mActivity, R$attr.windowExtraPaddingHorizontal, 0) != 0);
        if (this.mExtraPaddingEnable) {
            zResolveBoolean = true;
        }
        boolean zResolveBoolean2 = AttributeResolver.resolveBoolean(this.mActivity, R$attr.windowExtraPaddingHorizontalInitEnable, this.mExtraPaddingInitEnable);
        if (this.mExtraPaddingInitEnable) {
            zResolveBoolean2 = true;
        }
        boolean zResolveBoolean3 = this.mExtraPaddingApplyToContentEnable ? true : AttributeResolver.resolveBoolean(this.mActivity, R$attr.windowExtraPaddingApplyToContentEnable, this.mExtraPaddingApplyToContentEnable);
        setExtraHorizontalPaddingEnable(zResolveBoolean);
        setExtraHorizontalPaddingInitEnable(zResolveBoolean2);
        setExtraPaddingApplyToContentEnable(zResolveBoolean3);
        this.mActivityCallback.onCreate(bundle);
        installSubDecor();
        installFloatingSwitcher(this.mIsFloatingTheme, bundle);
    }

    public void installFloatingSwitcher(boolean z, Bundle bundle) {
        if (z) {
            Intent intent = this.mActivity.getIntent();
            if (intent != null && MultiAppFloatingActivitySwitcher.isFromMultiApp(intent)) {
                MultiAppFloatingActivitySwitcher.install(this.mActivity, intent, bundle);
            } else {
                FloatingActivitySwitcher.install(this.mActivity, bundle);
            }
        }
    }

    private void ensureWindow() {
        AppCompatActivity appCompatActivity;
        Window window = this.mWindow;
        if (window != null) {
            return;
        }
        if (window == null && (appCompatActivity = this.mActivity) != null) {
            attachToWindow(appCompatActivity.getWindow());
        }
        if (this.mWindow != null) {
            return;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("We have not been given a Window");
    }

    private void attachToWindow(Window window) {
        if (this.mWindow != null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("AppCompat has already installed itself into the Window");
            return;
        }
        Window.Callback callback = window.getCallback();
        if (callback instanceof AppCompatWindowCallback) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("AppCompat has already installed itself into the Window");
            return;
        }
        AppCompatWindowCallback appCompatWindowCallback = new AppCompatWindowCallback(callback);
        this.mAppCompatWindowCallback = appCompatWindowCallback;
        window.setCallback(appCompatWindowCallback);
        this.mWindow = window;
    }

    @Override // miuix.appcompat.app.ActionBarDelegate
    public ActionBar createActionBar() {
        if (!this.mSubDecorInstalled) {
            installSubDecor();
        }
        if (this.mSubDecor == null) {
            return null;
        }
        return new ActionBarImpl(this.mActivity, this.mSubDecor);
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public Context getThemedContext() {
        return this.mActivity;
    }

    public void onStop() {
        this.mActivityCallback.onStop();
        dismissImmersionMenu(false);
        ActionBarImpl actionBarImpl = (ActionBarImpl) getActionBar();
        if (actionBarImpl != null) {
            actionBarImpl.setShowHideAnimationEnabled(false);
        }
    }

    public void onPostResume() {
        this.mActivityCallback.onPostResume();
        ActionBarImpl actionBarImpl = (ActionBarImpl) getActionBar();
        if (actionBarImpl != null) {
            actionBarImpl.setShowHideAnimationEnabled(true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [miuix.appcompat.app.ActivityCallback] */
    /* JADX WARN: Type inference failed for: r1v2, types: [miuix.appcompat.app.ActivityCallback] */
    /* JADX WARN: Type inference failed for: r4v0, types: [miuix.appcompat.app.ActionBarDelegateImpl, miuix.appcompat.app.AppDelegate] */
    /* JADX WARN: Type inference failed for: r5v2, types: [miuix.appcompat.internal.view.menu.MenuBuilder] */
    /* JADX WARN: Type inference failed for: r5v3, types: [miuix.appcompat.internal.view.menu.MenuBuilder] */
    /* JADX WARN: Type inference failed for: r5v4, types: [android.view.Menu, miuix.appcompat.internal.view.menu.MenuBuilder] */
    /* JADX WARN: Type inference failed for: r5v5, types: [android.view.Menu, miuix.appcompat.internal.view.menu.MenuBuilder] */
    /* JADX WARN: Type inference failed for: r5v7 */
    /* JADX WARN: Type inference failed for: r5v8 */
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
    public View onCreatePanelView(int i) {
        ?? CreateMenu;
        if (i != 0) {
            return this.mActivityCallback.onCreatePanelView(i);
        }
        if (isEndActionMenuEnabled() || this.mSplitActionBarEnable) {
            ?? r5 = this.mMenu;
            boolean zOnPreparePanel = true;
            r5 = r5;
            if (this.mActionMode == null) {
                if (r5 == 0) {
                    CreateMenu = createMenu();
                    setMenu(CreateMenu);
                    CreateMenu.stopDispatchingItemsChanged();
                    zOnPreparePanel = this.mActivityCallback.onCreatePanelMenu(0, CreateMenu);
                }
                if (zOnPreparePanel) {
                    r5 = CreateMenu;
                    r5.stopDispatchingItemsChanged();
                    zOnPreparePanel = this.mActivityCallback.onPreparePanel(0, null, r5);
                }
            } else if (r5 == 0) {
                zOnPreparePanel = false;
            }
            if (zOnPreparePanel) {
                r5.startDispatchingItemsChanged();
            } else {
                setMenu(null);
            }
        }
        return null;
    }

    public boolean onCreatePanelMenu(int i, Menu menu) {
        return i != 0 && this.mActivityCallback.onCreatePanelMenu(i, menu);
    }

    public boolean onPreparePanel(int i, View view, Menu menu) {
        return i != 0 && this.mActivityCallback.onPreparePanel(i, view, menu);
    }

    @Override // miuix.appcompat.app.ActionBarDelegate
    public void onPanelViewAdded(int i, View view, Menu menu, Menu menu2) {
        this.mActivityCallback.onPanelViewAdded(i, view, menu, menu2);
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        boolean zOnNavigateUpFromChild;
        if (this.mActivityCallback.onMenuItemSelected(i, menuItem)) {
            return true;
        }
        if (i == 0 && menuItem.getItemId() == 16908332 && getActionBar() != null && (getActionBar().getDisplayOptions() & 4) != 0) {
            Activity parent = this.mActivity.getParent();
            AppCompatActivity appCompatActivity = this.mActivity;
            if (parent == null) {
                zOnNavigateUpFromChild = appCompatActivity.onNavigateUp();
            } else {
                zOnNavigateUpFromChild = appCompatActivity.getParent().onNavigateUpFromChild(this.mActivity);
            }
            if (!zOnNavigateUpFromChild) {
                this.mActivity.finish();
            }
        }
        return false;
    }

    public void onPanelClosed(int i, Menu menu) {
        this.mActivityCallback.onPanelClosed(i, menu);
    }

    private void installSubDecor() {
        ActionBarOverlayLayout actionBarOverlayLayout;
        if (this.mSubDecorInstalled) {
            return;
        }
        ensureWindow();
        this.mSubDecorInstalled = true;
        Window window = this.mActivity.getWindow();
        this.mLayoutInflater = window.getLayoutInflater();
        AppCompatActivity appCompatActivity = this.mActivity;
        int[] iArr = R$styleable.Window;
        TypedArray typedArrayObtainStyledAttributes = appCompatActivity.obtainStyledAttributes(iArr);
        if (typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_responsiveEnabled, this.mIsUserResponsiveEnabled)) {
            this.mResponsiveStateManager = new BaseResponseStateManager(this) { // from class: miuix.appcompat.app.AppDelegate.1
                @Override // miuix.responsive.page.manager.BaseStateManager
                protected Context getContext() {
                    return AppDelegate.this.mActivity;
                }
            };
        }
        if (typedArrayObtainStyledAttributes.getInt(R$styleable.Window_windowLayoutMode, 0) == 1) {
            this.mActivity.getWindow().setGravity(80);
        }
        if (!typedArrayObtainStyledAttributes.hasValue(R$styleable.Window_windowActionBar)) {
            Log.i("MiuixException", "miuix theme parse error!! TypedArray:" + typedArrayObtainStyledAttributes);
            typedArrayObtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a miuix theme (or descendant) such as Theme.DayNight with this activity.\n\tR.styleable.Window:" + Arrays.toString(iArr) + "\n\tR.styleable.Window_windowActionBar:" + R$styleable.Window_windowActionBar + "\n\t0x" + Integer.toHexString(R$styleable.Window_windowActionBar) + "\n\t theme:" + this.mActivity.getTheme());
        }
        if (typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_windowActionBar, false)) {
            requestWindowFeature(8);
        }
        if (typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_windowActionBarOverlay, false)) {
            requestWindowFeature(9);
        }
        this.mIsFloatingTheme = typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_isMiuixFloatingTheme, false);
        this.mIsFloatingWindow = typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_windowFloating, false);
        this.mIsFloatingPanelMode = typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_floatingPanelMode, false);
        this.mFloatingPanelDynamicCornerEnabled = typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_floatingPanelDynamicCornerEnabled, true);
        this.mFloatingPanelCornerSide = typedArrayObtainStyledAttributes.getInt(R$styleable.Window_floatingPanelCornerSide, 0);
        setTranslucentStatus(typedArrayObtainStyledAttributes.getInt(R$styleable.Window_windowTranslucentStatus, 0));
        this.mUIMode = this.mActivity.getResources().getConfiguration().uiMode;
        installToDecor(window);
        ActionBarOverlayLayout actionBarOverlayLayout2 = this.mSubDecor;
        if (actionBarOverlayLayout2 != null) {
            actionBarOverlayLayout2.setCallback(this.mActivity);
            this.mSubDecor.setContentInsetStateCallback(this.mActivity);
            this.mSubDecor.addExtraPaddingObserver(this.mActivity);
            this.mSubDecor.setTranslucentStatus(getTranslucentStatus());
        }
        if (this.mHasActionBar && (actionBarOverlayLayout = this.mSubDecor) != null) {
            this.mActionBarContainer = (ActionBarContainer) actionBarOverlayLayout.findViewById(R$id.action_bar_container);
            this.mSubDecor.setOverlayMode(this.mOverlayActionBar);
            ActionBarView actionBarView = (ActionBarView) this.mSubDecor.findViewById(R$id.action_bar);
            this.mActionBarView = actionBarView;
            actionBarView.setLifecycleOwner(getLifecycleOwner());
            this.mActionBarView.setWindowCallback(this.mActivity);
            if (this.mFeatureIndeterminateProgress) {
                this.mActionBarView.initIndeterminateProgress();
            }
            if (isEndActionMenuEnabled()) {
                this.mActionBarView.setEndActionMenuEnable(true);
            }
            if (this.mActionBarView.getCustomNavigationView() != null) {
                ActionBarView actionBarView2 = this.mActionBarView;
                actionBarView2.setDisplayOptions(actionBarView2.getDisplayOptions() | 16);
            }
            boolean zEquals = "splitActionBarWhenNarrow".equals(getUiOptionsFromMetadata());
            if (zEquals) {
                this.mSplitActionBarEnable = this.mActivity.getResources().getBoolean(R$bool.abc_split_action_bar_is_narrow);
            } else {
                this.mSplitActionBarEnable = typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_windowSplitActionBar, false);
            }
            if (this.mSplitActionBarEnable) {
                addSplitActionBar(true, zEquals, this.mSubDecor);
                setHyperSplitMenuEnabled(typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_hyperSplitMenuEnabled, false));
            }
            if (typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_endActionMenuEnabled, false)) {
                setEndActionMenuEnabled(true, typedArrayObtainStyledAttributes.getBoolean(R$styleable.Window_hyperActionMenuEnabled, false), false);
            } else if (this.mActivity.getWindow() != null) {
                this.mActivity.getWindow().getDecorView().post(this.mInvalidateMenuRunnable);
            }
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    public boolean isFloatingTheme() {
        return this.mIsFloatingTheme;
    }

    public boolean isInFloatingWindowMode() {
        Boolean bool = this.mUserIsFloatingWindow;
        return bool == null ? shouldShowFloatingActivityTabletMode() : bool.booleanValue();
    }

    public View getFloatingBrightPanel() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            return baseFloatingActivityHelper.getFloatingBrightPanel();
        }
        return null;
    }

    private void updateSystemUiStateInFloatingTheme(boolean z) {
        Window window = this.mActivity.getWindow();
        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        boolean z2 = ((systemUiVisibility & SmartNetworkRelayScore.TRAFFIC_UNIT) != 0) || (getTranslucentStatus() != 0);
        if (z) {
            window.addFlags(201326592);
            window.setDecorFitsSystemWindows(false);
        } else {
            systemUiVisibility = z2 ? systemUiVisibility | SmartNetworkRelayScore.TRAFFIC_UNIT : systemUiVisibility & (-1025);
            if (z2) {
                window.setDecorFitsSystemWindows(false);
            } else {
                window.setDecorFitsSystemWindows(true);
            }
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    private void setFloatingWindowMode(boolean z, int i, boolean z2, boolean z3) {
        if (this.mIsFloatingTheme) {
            if (z3 || Build.IS_TABLET) {
                if (this.mIsFloatingWindow != z && this.mFloatingModeCallback.onFloatingWindowModeChanging(z)) {
                    this.mIsFloatingWindow = z;
                    this.mFloatingWindowHelper.setFloatingWindowMode(z);
                    updateSystemUiStateInFloatingTheme(this.mIsFloatingWindow);
                    ViewGroup.LayoutParams floatingLayoutParam = this.mFloatingWindowHelper.getFloatingLayoutParam();
                    if (floatingLayoutParam != null) {
                        if (z) {
                            floatingLayoutParam.height = -2;
                            floatingLayoutParam.width = -2;
                        } else {
                            floatingLayoutParam.height = -1;
                            floatingLayoutParam.width = -1;
                        }
                    }
                    ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
                    if (actionBarOverlayLayout != null) {
                        actionBarOverlayLayout.requestLayout();
                        this.mSubDecor.onFloatingModeChanged(z);
                    }
                    if (z2) {
                        notifyFloatWindowModeChanged(z);
                        return;
                    }
                    return;
                }
                if (i != this.mUIMode) {
                    this.mUIMode = i;
                    this.mFloatingWindowHelper.setFloatingWindowMode(z);
                }
            }
        }
    }

    private void notifyFloatWindowModeChanged(boolean z) {
        this.mFloatingModeCallback.onFloatingWindowModeChanged(z);
    }

    public void setContentView(int i) {
        if (!this.mSubDecorInstalled) {
            installSubDecor();
        }
        ViewGroup viewGroup = this.mContentParent;
        if (viewGroup != null) {
            viewGroup.removeAllViews();
            this.mLayoutInflater.inflate(i, this.mContentParent);
        }
        this.mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        if (!this.mSubDecorInstalled) {
            installSubDecor();
        }
        ViewGroup viewGroup = this.mContentParent;
        if (viewGroup != null) {
            viewGroup.removeAllViews();
            this.mContentParent.addView(view, layoutParams);
        }
        this.mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        if (!this.mSubDecorInstalled) {
            installSubDecor();
        }
        ViewGroup viewGroup = this.mContentParent;
        if (viewGroup != null) {
            viewGroup.addView(view, layoutParams);
        }
        this.mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    private boolean shouldShowFloatingActivityTabletMode() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        return baseFloatingActivityHelper != null && baseFloatingActivityHelper.isFloatingModeSupport();
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
    private void installToDecor(Window window) {
        ViewGroup viewGroupReplaceSubDecor;
        this.mFloatingWindowHelper = this.mIsFloatingTheme ? FloatingHelperFactory.get(this.mActivity) : null;
        this.mFloatingRoot = null;
        View viewInflate = View.inflate(this.mActivity, getDecorViewLayoutRes(window), null);
        boolean z = true;
        View view = viewInflate;
        if (this.mFloatingWindowHelper != null) {
            boolean zShouldShowFloatingActivityTabletMode = shouldShowFloatingActivityTabletMode();
            this.mIsFloatingWindow = zShouldShowFloatingActivityTabletMode;
            this.mFloatingWindowHelper.setFloatingWindowMode(zShouldShowFloatingActivityTabletMode);
            viewGroupReplaceSubDecor = this.mFloatingWindowHelper.replaceSubDecor(viewInflate, this.mIsFloatingWindow);
            this.mFloatingRoot = viewGroupReplaceSubDecor;
            updateSystemUiStateInFloatingTheme(this.mIsFloatingWindow);
            if (this.mFloatingWindowHelper.shouldInterceptBack()) {
                view = viewGroupReplaceSubDecor;
                this.mActivity.getOnBackPressedDispatcher().addCallback(this.mActivity, new OnBackPressedCallback(z) { // from class: miuix.appcompat.app.AppDelegate.2
                    @Override // androidx.activity.OnBackPressedCallback
                    public void handleOnBackPressed() {
                        AppDelegate.this.mFloatingWindowHelper.onBackPressed();
                    }
                });
                view = viewGroupReplaceSubDecor;
            }
        }
        view = viewGroupReplaceSubDecor;
        if (!this.mUserExtraPaddingPolicy) {
            initExtraPaddingPolicy();
        }
        View viewFindViewById = view.findViewById(R$id.action_bar_overlay_layout);
        if (viewFindViewById instanceof ActionBarOverlayLayout) {
            ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) viewFindViewById;
            this.mSubDecor = actionBarOverlayLayout;
            actionBarOverlayLayout.setLifecycleOwner(getLifecycleOwner());
            this.mSubDecor.setExtraHorizontalPaddingEnable(this.mExtraPaddingEnable);
            this.mSubDecor.setExtraHorizontalPaddingInitEnable(this.mExtraPaddingInitEnable);
            this.mSubDecor.setExtraPaddingApplyToContentEnable(this.mExtraPaddingApplyToContentEnable);
            this.mSubDecor.setExtraPaddingPolicy(getExtraPaddingPolicy());
            ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(R.id.content);
            ViewGroup viewGroup2 = (ViewGroup) window.findViewById(R.id.content);
            if (viewGroup2 != null) {
                while (viewGroup2.getChildCount() > 0) {
                    View childAt = viewGroup2.getChildAt(0);
                    viewGroup2.removeViewAt(0);
                    viewGroup.addView(childAt);
                }
                viewGroup2.setId(-1);
                viewGroup.setId(R.id.content);
                if (viewGroup2 instanceof FrameLayout) {
                    ((FrameLayout) viewGroup2).setForeground(null);
                }
            }
        }
        window.setContentView(view);
        if (this.mIsFloatingPanelMode) {
            updateActionBarOverlayLayoutParams(this.mSubDecor, true, true);
        }
        ActionBarOverlayLayout actionBarOverlayLayout2 = this.mSubDecor;
        if (actionBarOverlayLayout2 != null) {
            this.mContentParent = (ViewGroup) actionBarOverlayLayout2.findViewById(R.id.content);
        }
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.init(this.mFloatingRoot, shouldShowFloatingActivityTabletMode());
        }
    }

    void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.setWindowTitle(charSequence);
        }
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public View getView() {
        return this.mSubDecor;
    }

    private int getDecorViewLayoutRes(Window window) {
        int i;
        Context context = window.getContext();
        if (AttributeResolver.resolveBoolean(context, R$attr.windowActionBar, false)) {
            if (AttributeResolver.resolveBoolean(context, R$attr.windowActionBarMovable, false)) {
                i = R$layout.miuix_appcompat_screen_action_bar_movable;
            } else {
                i = R$layout.miuix_appcompat_screen_action_bar;
            }
        } else {
            i = R$layout.miuix_appcompat_screen_simple;
        }
        int iResolve = AttributeResolver.resolve(context, R$attr.startingWindowOverlay);
        if (iResolve > 0 && isSystemProcess() && isWindowActionBarEnabled(context)) {
            i = iResolve;
        }
        if (!window.isFloating() && (window.getCallback() instanceof Dialog)) {
            WindowWrapper.setTranslucentStatus(window, AttributeResolver.resolveInt(context, R$attr.windowTranslucentStatus, 0));
        }
        return i;
    }

    private boolean isSystemProcess() {
        return "android".equals(getActivity().getApplicationContext().getApplicationInfo().packageName);
    }

    private static boolean isWindowActionBarEnabled(Context context) {
        return AttributeResolver.resolveBoolean(context, R$attr.windowActionBar, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    protected boolean onCreateImmersionMenu(MenuBuilder menuBuilder) {
        return this.mActivity.onCreateOptionsMenu(menuBuilder);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    protected boolean onPrepareImmersionMenu(MenuBuilder menuBuilder) {
        return this.mActivity.onPrepareOptionsMenu(menuBuilder);
    }

    public void setOnFloatingCallback(OnFloatingCallback onFloatingCallback) {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.setOnFloatingCallback(onFloatingCallback);
        }
    }

    public void setEnableSwipToDismiss(boolean z) {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.setEnableSwipToDismiss(z);
        }
    }

    public void hideFloatingDimBackground() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.hideFloatingDimBackground();
        }
    }

    public void hideFloatingBrightPanel() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.hideFloatingBrightPanel();
        }
    }

    public void showFloatingBrightPanel() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.showFloatingBrightPanel();
        }
    }

    public void executeOpenEnterAnimation() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.executeOpenEnterAnimation();
        }
    }

    public void executeOpenExitAnimation() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.executeOpenExitAnimation();
        }
    }

    public void executeCloseEnterAnimation() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.executeCloseEnterAnimation();
        }
    }

    public boolean shouldDelegateActivityFinish() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper == null) {
            return false;
        }
        boolean zDelegateFinishFloatingActivityInternal = baseFloatingActivityHelper.delegateFinishFloatingActivityInternal();
        if (zDelegateFinishFloatingActivityInternal) {
            this.mDelegateFinished = true;
        }
        return zDelegateFinishFloatingActivityInternal;
    }

    public boolean isDelegateFinishing() {
        return this.mDelegateFinished;
    }

    public void beforeConfigurationChanged(Configuration configuration) {
        BaseResponseStateManager baseResponseStateManager = this.mResponsiveStateManager;
        if (baseResponseStateManager != null) {
            baseResponseStateManager.beforeConfigurationChanged(configuration);
        }
    }

    public void afterConfigurationChanged(Configuration configuration) {
        BaseResponseStateManager baseResponseStateManager = this.mResponsiveStateManager;
        if (baseResponseStateManager != null) {
            baseResponseStateManager.afterConfigurationChanged(configuration);
        }
    }

    @Override // miuix.responsive.interfaces.IResponsive
    public Activity getResponsiveSubject() {
        return this.mActivity;
    }

    public void testNotifyResponseChange(int i) {
        BaseResponseStateManager baseResponseStateManager = this.mResponsiveStateManager;
        if (baseResponseStateManager != null) {
            baseResponseStateManager.testNotifyResponseChange(i);
        }
    }

    @Override // miuix.responsive.interfaces.IResponsive
    public void dispatchResponsiveLayout(Configuration configuration, ScreenSpec screenSpec, boolean z) {
        onResponsiveLayout(configuration, screenSpec, z);
    }

    @Override // miuix.responsive.interfaces.IResponsive
    public void onResponsiveLayout(Configuration configuration, ScreenSpec screenSpec, boolean z) {
        if (this.mActivity != null) {
            this.mActivity.onResponsiveLayout(configuration, screenSpec, z);
        }
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public void onConfigurationChanged(final Configuration configuration) {
        int iDetectType;
        AppCompatActivity appCompatActivity = this.mActivity;
        EnvStateManager.updateWindowInfo(appCompatActivity, appCompatActivity.getWindowInfo(), configuration, false);
        this.mUpdateWindowInfoRunnable = new Runnable() { // from class: miuix.appcompat.app.AppDelegate$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AppDelegate.$r8$lambda$3Sr00TMfPe9u6Ram_CsX3uemma0(this.f$0, configuration);
            }
        };
        if (this.mActivity.getWindow() != null) {
            this.mActivity.getWindow().getDecorView().post(this.mUpdateWindowInfoRunnable);
        }
        super.onConfigurationChanged(configuration);
        if (!this.mUserExtraPaddingPolicy && this.mDeviceType != (iDetectType = DeviceHelper.detectType(this.mActivity))) {
            this.mDeviceType = iDetectType;
            initExtraPaddingPolicy();
            ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
            if (actionBarOverlayLayout != null) {
                actionBarOverlayLayout.setExtraPaddingPolicy(this.mExtraPaddingPolicy);
            }
        }
        this.mActivityCallback.onConfigurationChanged(configuration);
        if (isImmersionMenuShowing()) {
            showImmersionMenu();
        }
    }

    public static /* synthetic */ void $r8$lambda$3Sr00TMfPe9u6Ram_CsX3uemma0(AppDelegate appDelegate, Configuration configuration) {
        AppCompatActivity appCompatActivity = appDelegate.mActivity;
        EnvStateManager.updateWindowInfo(appCompatActivity, appCompatActivity.getWindowInfo(), null, true);
        appDelegate.setFloatingWindowMode(appDelegate.isInFloatingWindowMode(), configuration.uiMode, true, Build.IS_FOLDABLE);
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        this.mActivityCallback.onSaveInstanceState(bundle);
        if (this.mFloatingWindowHelper != null) {
            FloatingActivitySwitcher.onSaveInstanceState(this.mActivity, bundle);
            MultiAppFloatingActivitySwitcher.onSaveInstanceState(this.mActivity.getTaskId(), this.mActivity.getActivityIdentity(), bundle);
        }
        if (this.mActionBarContainer != null) {
            SparseArray<? extends Parcelable> sparseArray = new SparseArray<>();
            this.mActionBarContainer.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("miuix:ActionBar", sparseArray);
        }
    }

    public void onRestoreInstanceState(Bundle bundle) {
        SparseArray sparseParcelableArray;
        this.mActivityCallback.onRestoreInstanceState(bundle);
        if (this.mActionBarContainer == null || (sparseParcelableArray = bundle.getSparseParcelableArray("miuix:ActionBar")) == null) {
            return;
        }
        this.mActionBarContainer.restoreHierarchyState(sparseParcelableArray);
    }

    @Override // miuix.appcompat.app.ActionBarDelegate
    public void invalidateOptionsMenu() {
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity == null || appCompatActivity.isFinishing() || this.mActivity.isDestroyed()) {
            return;
        }
        this.mInvalidateMenuRunnable.run();
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public void onDestroy() {
        super.onDestroy();
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity == null || appCompatActivity.getWindow() == null) {
            return;
        }
        View decorView = this.mActivity.getWindow().getDecorView();
        decorView.removeCallbacks(this.mInvalidateMenuRunnable);
        Runnable runnable = this.mUpdateWindowInfoRunnable;
        if (runnable != null) {
            decorView.removeCallbacks(runnable);
        }
    }

    public boolean requestDispatchContentInset() {
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout == null) {
            return false;
        }
        actionBarOverlayLayout.requestDispatchContentInset();
        return true;
    }

    @Override // miuix.appcompat.app.IContentInsetState
    public Rect getContentInset() {
        return this.mContentInset;
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl, miuix.appcompat.app.IContentInsetState
    public void onContentInsetChanged(Rect rect) {
        super.onContentInsetChanged(rect);
        List fragments = this.mActivity.getSupportFragmentManager().getFragments();
        int size = fragments.size();
        for (int i = 0; i < size; i++) {
            LifecycleOwner lifecycleOwner = (Fragment) fragments.get(i);
            if (lifecycleOwner instanceof IFragment) {
                IFragment iFragment = (IFragment) lifecycleOwner;
                if (!iFragment.hasActionBar()) {
                    iFragment.onContentInsetChanged(rect);
                }
            }
        }
    }

    @Override // miuix.container.ExtraPaddingObserver
    public void onExtraPaddingChanged(int i) {
        this.mExtraHorizontalPadding = i;
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        if (callback instanceof SearchActionMode.Callback) {
            addContentMask(this.mSubDecor);
        }
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            return actionBarOverlayLayout.startActionMode(callback);
        }
        return null;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        return this.mActivity.onMenuItemSelected(0, menuItem);
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        if (getActionBar() != null) {
            return ((ActionBarImpl) getActionBar()).startActionMode(callback);
        }
        return super.onWindowStartingActionMode(callback);
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public void setExtraHorizontalPaddingEnable(boolean z) {
        super.setExtraHorizontalPaddingEnable(z);
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setExtraHorizontalPaddingEnable(z);
        }
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public void setExtraHorizontalPaddingInitEnable(boolean z) {
        super.setExtraHorizontalPaddingInitEnable(z);
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setExtraHorizontalPaddingInitEnable(z);
        }
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public void setExtraPaddingApplyToContentEnable(boolean z) {
        super.setExtraPaddingApplyToContentEnable(z);
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setExtraPaddingApplyToContentEnable(z);
        }
    }

    public String getActivityIdentity() {
        return this.mActivityIdentity;
    }

    class AppCompatWindowCallback extends WindowCallbackWrapper {
        @Override // android.view.Window.Callback
        public void onContentChanged() {
        }

        public AppCompatWindowCallback(Window.Callback callback) {
            super(callback);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (ShortcutsCallback.dispatchKeyEvent(AppDelegate.this.mActivity.getSupportFragmentManager(), keyEvent)) {
                return true;
            }
            return super.dispatchKeyEvent(keyEvent);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            if (ShortcutsCallback.dispatchKeyShortcutEvent(AppDelegate.this.mActivity.getSupportFragmentManager(), keyEvent)) {
                return true;
            }
            return super.dispatchKeyShortcutEvent(keyEvent);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (ShortcutsCallback.dispatchTouchEvent(AppDelegate.this.mActivity.getSupportFragmentManager(), motionEvent)) {
                return true;
            }
            return super.dispatchTouchEvent(motionEvent);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
            if (ShortcutsCallback.dispatchTrackballEvent(AppDelegate.this.mActivity.getSupportFragmentManager(), motionEvent)) {
                return true;
            }
            return super.dispatchTrackballEvent(motionEvent);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
            if (ShortcutsCallback.dispatchGenericMotionEvent(AppDelegate.this.mActivity.getSupportFragmentManager(), motionEvent)) {
                return true;
            }
            return super.dispatchGenericMotionEvent(motionEvent);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public void onProvideKeyboardShortcuts(List list, Menu menu, int i) {
            ShortcutsCallback.dispatchProvideKeyboardShortcuts(AppDelegate.this.mActivity.getSupportFragmentManager(), list, menu, i);
            super.onProvideKeyboardShortcuts(list, menu, i);
        }
    }
}
