package miuix.appcompat.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import miuix.appcompat.R$id;
import miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation;
import miuix.appcompat.app.floatingactivity.OnFloatingCallback;
import miuix.appcompat.app.floatingactivity.OnFloatingModeCallback;
import miuix.appcompat.internal.util.LayoutUIUtils;
import miuix.core.util.EnvStateManager;
import miuix.core.util.MiuixUIUtils;
import miuix.core.util.WindowBaseInfo;
import miuix.responsive.interfaces.IResponsive;
import miuix.responsive.map.ScreenSpec;

/* JADX INFO: loaded from: classes2.dex */
public class AppCompatActivity extends FragmentActivity implements IActivity, IActivitySwitcherAnimation, IResponsive {
    private AppDelegate mAppDelegate;
    private int mInputViewLimitTextSizeDp;
    private WindowBaseInfo mWindowInfo;

    public void checkThemeLegality() {
    }

    @Override // miuix.responsive.interfaces.IResponsive
    public Activity getResponsiveSubject() {
        return this;
    }

    protected boolean isResponsiveEnabled() {
        return false;
    }

    public void onFloatingWindowModeChanged(boolean z) {
    }

    public boolean onFloatingWindowModeChanging(boolean z) {
        return true;
    }

    public void onOptionsMenuViewAdded(Menu menu, Menu menu2) {
    }

    @Override // miuix.responsive.interfaces.IResponsive
    public void onResponsiveLayout(Configuration configuration, ScreenSpec screenSpec, boolean z) {
    }

    public AppCompatActivity() {
        this.mAppDelegate = new AppDelegate(this, new Callback(), new FloatingModeCallback());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        EnvStateManager.markWindowInfoDirty(this);
        this.mAppDelegate.setResponsiveEnabled(isResponsiveEnabled());
        this.mAppDelegate.onCreate(bundle);
        this.mWindowInfo = EnvStateManager.getWindowInfo(this, null, true);
        this.mInputViewLimitTextSizeDp = MiuixUIUtils.isTallFontLang(this) ? 16 : 27;
        getWindow().getDecorView().post(new Runnable() { // from class: miuix.appcompat.app.AppCompatActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AppCompatActivity appCompatActivity = this.f$0;
                LayoutUIUtils.resetSearchModeStubInputTextSize(appCompatActivity.getResources(), appCompatActivity.findViewById(R$id.search_mode_stub), appCompatActivity.mInputViewLimitTextSizeDp);
            }
        });
    }

    public ActionBar getAppCompatActionBar() {
        return this.mAppDelegate.getActionBar();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(int i) {
        this.mAppDelegate.setContentView(i);
    }

    @Override // android.app.Activity
    public void setContentView(View view) {
        this.mAppDelegate.setContentView(view);
    }

    @Override // android.app.Activity
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mAppDelegate.setContentView(view, layoutParams);
    }

    @Override // android.app.Activity
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mAppDelegate.addContentView(view, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPostResume() {
        this.mAppDelegate.onPostResume();
    }

    @Override // android.app.Activity
    protected void onTitleChanged(CharSequence charSequence, int i) {
        super.onTitleChanged(charSequence, i);
        this.mAppDelegate.setTitle(charSequence);
    }

    @Override // android.app.Activity
    public void invalidateOptionsMenu() {
        this.mAppDelegate.invalidateOptionsMenu();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public View onCreatePanelView(int i) {
        return this.mAppDelegate.onCreatePanelView(i);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        return this.mAppDelegate.onMenuItemSelected(i, menuItem);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public void onPanelClosed(int i, Menu menu) {
        this.mAppDelegate.onPanelClosed(i, menu);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode actionMode) {
        this.mAppDelegate.onActionModeStarted(actionMode);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode actionMode) {
        this.mAppDelegate.onActionModeFinished(actionMode);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
        return this.mAppDelegate.onWindowStartingActionMode(callback, i);
    }

    @Override // android.app.Activity
    public MenuInflater getMenuInflater() {
        return this.mAppDelegate.getMenuInflater();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        beforeConfigurationChanged(getResources().getConfiguration());
        if (!this.mWindowInfo.isDirty()) {
            EnvStateManager.markWindowInfoDirty(this.mWindowInfo);
        }
        this.mAppDelegate.onConfigurationChanged(configuration);
        afterConfigurationChanged(configuration);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        this.mAppDelegate.onSaveInstanceState(bundle);
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle bundle) {
        this.mAppDelegate.onRestoreInstanceState(bundle);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        this.mAppDelegate.onStop();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        this.mAppDelegate.onDestroy();
        EnvStateManager.removeInfoOfContext(this);
        this.mWindowInfo = null;
        super.onDestroy();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (ShortcutsCallback.dispatchKeyDown(getSupportFragmentManager(), i, keyEvent)) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        if (ShortcutsCallback.dispatchKeyLongPress(getSupportFragmentManager(), i, keyEvent)) {
            return true;
        }
        return super.onKeyLongPress(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (ShortcutsCallback.dispatchKeyUp(getSupportFragmentManager(), i, keyEvent)) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyMultiple(int i, int i2, KeyEvent keyEvent) {
        if (ShortcutsCallback.dispatchKeyMultiple(getSupportFragmentManager(), i, i2, keyEvent)) {
            return true;
        }
        return super.onKeyMultiple(i, i2, keyEvent);
    }

    protected void beforeConfigurationChanged(Configuration configuration) {
        this.mAppDelegate.beforeConfigurationChanged(configuration);
    }

    protected void afterConfigurationChanged(Configuration configuration) {
        this.mAppDelegate.afterConfigurationChanged(configuration);
    }

    public WindowBaseInfo getWindowInfo() {
        return this.mWindowInfo;
    }

    @Override // miuix.responsive.interfaces.IResponsive
    public void dispatchResponsiveLayout(Configuration configuration, ScreenSpec screenSpec, boolean z) {
        this.mAppDelegate.dispatchResponsiveLayout(configuration, screenSpec, z);
    }

    public void testNotifyResponseChange(int i) {
        this.mAppDelegate.testNotifyResponseChange(i);
    }

    @Override // android.app.Activity
    public void finish() {
        if (this.mAppDelegate.shouldDelegateActivityFinish()) {
            return;
        }
        realFinish();
    }

    @Override // android.app.Activity
    public boolean isFinishing() {
        return this.mAppDelegate.isDelegateFinishing() || super.isFinishing();
    }

    public void realFinish() {
        super.finish();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onCreatePanelMenu(int i, Menu menu) {
        return this.mAppDelegate.onCreatePanelMenu(i, menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onPreparePanel(int i, View view, Menu menu) {
        return this.mAppDelegate.onPreparePanel(i, view, menu);
    }

    @Override // android.app.Activity
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return this.mAppDelegate.startActionMode(callback);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return this.mAppDelegate.onWindowStartingActionMode(callback);
    }

    public boolean isFloatingWindowTheme() {
        return this.mAppDelegate.isFloatingTheme();
    }

    @Override // miuix.appcompat.app.IActivity
    public boolean isInFloatingWindowMode() {
        return this.mAppDelegate.isInFloatingWindowMode();
    }

    public View getFloatingBrightPanel() {
        return this.mAppDelegate.getFloatingBrightPanel();
    }

    public void setEnableSwipToDismiss(boolean z) {
        this.mAppDelegate.setEnableSwipToDismiss(z);
    }

    public void setOnFloatingCallback(OnFloatingCallback onFloatingCallback) {
        this.mAppDelegate.setOnFloatingCallback(onFloatingCallback);
    }

    public void hideFloatingDimBackground() {
        this.mAppDelegate.hideFloatingDimBackground();
    }

    public void hideFloatingBrightPanel() {
        this.mAppDelegate.hideFloatingBrightPanel();
    }

    public void showFloatingBrightPanel() {
        this.mAppDelegate.showFloatingBrightPanel();
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeOpenEnterAnimation() {
        this.mAppDelegate.executeOpenEnterAnimation();
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeOpenExitAnimation() {
        this.mAppDelegate.executeOpenExitAnimation();
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeCloseEnterAnimation() {
        this.mAppDelegate.executeCloseEnterAnimation();
    }

    public void setExtraHorizontalPaddingEnable(boolean z) {
        this.mAppDelegate.setExtraHorizontalPaddingEnable(z);
    }

    @Override // miuix.container.ExtraPaddingObserver
    public boolean setExtraHorizontalPadding(int i) {
        return this.mAppDelegate.setExtraHorizontalPadding(i);
    }

    public int getExtraHorizontalPadding() {
        return this.mAppDelegate.getExtraHorizontalPadding();
    }

    public void onExtraPaddingChanged(int i) {
        this.mAppDelegate.onExtraPaddingChanged(i);
    }

    public String getActivityIdentity() {
        return this.mAppDelegate.getActivityIdentity();
    }

    public boolean requestDispatchContentInset() {
        return this.mAppDelegate.requestDispatchContentInset();
    }

    public void registerCoordinateScrollView(View view) {
        this.mAppDelegate.registerCoordinateScrollView(view);
    }

    public void unregisterCoordinateScrollView(View view) {
        this.mAppDelegate.unregisterCoordinateScrollView(view);
    }

    @Override // miuix.appcompat.app.IContentInsetState
    public Rect getContentInset() {
        return this.mAppDelegate.getContentInset();
    }

    @Override // miuix.appcompat.app.IContentInsetState
    public void onContentInsetChanged(Rect rect) {
        this.mAppDelegate.onContentInsetChanged(rect);
        onProcessBindViewWithContentInset(rect);
    }

    public void onProcessBindViewWithContentInset(Rect rect) {
        this.mAppDelegate.onProcessBindViewWithContentInset(rect);
    }

    class Callback implements ActivityCallback {
        private Callback() {
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onCreate(Bundle bundle) {
            AppCompatActivity.super.onCreate(bundle);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onPostResume() {
            AppCompatActivity.super.onPostResume();
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onStop() {
            AppCompatActivity.super.onStop();
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public boolean onMenuItemSelected(int i, MenuItem menuItem) {
            return AppCompatActivity.super.onMenuItemSelected(i, menuItem);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onPanelClosed(int i, Menu menu) {
            AppCompatActivity.super.onPanelClosed(i, menu);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public View onCreatePanelView(int i) {
            return AppCompatActivity.super.onCreatePanelView(i);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public boolean onCreatePanelMenu(int i, Menu menu) {
            return AppCompatActivity.super.onCreatePanelMenu(i, menu);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public boolean onPreparePanel(int i, View view, Menu menu) {
            return AppCompatActivity.super.onPreparePanel(i, view, menu);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onPanelViewAdded(int i, View view, Menu menu, Menu menu2) {
            AppCompatActivity.this.onOptionsMenuViewAdded(menu, menu2);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onConfigurationChanged(Configuration configuration) {
            AppCompatActivity.super.onConfigurationChanged(configuration);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onSaveInstanceState(Bundle bundle) {
            AppCompatActivity.super.onSaveInstanceState(bundle);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onRestoreInstanceState(Bundle bundle) {
            AppCompatActivity.super.onRestoreInstanceState(bundle);
        }
    }

    class FloatingModeCallback implements OnFloatingModeCallback {
        private FloatingModeCallback() {
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingModeCallback
        public boolean onFloatingWindowModeChanging(boolean z) {
            return AppCompatActivity.this.onFloatingWindowModeChanging(z);
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingModeCallback
        public void onFloatingWindowModeChanged(boolean z) {
            AppCompatActivity.this.onFloatingWindowModeChanged(z);
        }
    }
}
