package miuix.appcompat.app.floatingactivity;

import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes2.dex */
public class SingleAppFloatingLifecycleObserver extends FloatingLifecycleObserver {
    public SingleAppFloatingLifecycleObserver(AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
    }

    @Override // miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver
    public void onCreate() {
        AppCompatActivity activity;
        FloatingActivitySwitcher floatingActivitySwitcher = FloatingActivitySwitcher.getInstance();
        if (floatingActivitySwitcher == null || (activity = floatingActivitySwitcher.getActivity(getActivityIdentity(), getActivityTaskId())) == null) {
            return;
        }
        if (floatingActivitySwitcher.getPreviousActivity(activity) != null) {
            if (activity.isInFloatingWindowMode()) {
                if (floatingActivitySwitcher.isActivityOpenEnterAnimExecuted(activity)) {
                    return;
                }
                floatingActivitySwitcher.markActivityOpenEnterAnimExecutedInternal(activity);
                FloatingAnimHelper.singleAppFloatingActivityEnter(activity);
                return;
            }
            floatingActivitySwitcher.markActivityOpenEnterAnimExecutedInternal(activity);
            FloatingAnimHelper.preformFloatingExitAnimWithClip(activity, false);
            return;
        }
        execEnterNotInFloatingWindowMode(activity);
    }

    private void execEnterNotInFloatingWindowMode(AppCompatActivity appCompatActivity) {
        FloatingActivitySwitcher floatingActivitySwitcher = FloatingActivitySwitcher.getInstance();
        if (FloatingAnimHelper.obtainPageIndex(appCompatActivity) < 0 || appCompatActivity.isInFloatingWindowMode() || floatingActivitySwitcher == null) {
            return;
        }
        floatingActivitySwitcher.markActivityOpenEnterAnimExecutedInternal(appCompatActivity);
        FloatingAnimHelper.preformFloatingExitAnimWithClip(appCompatActivity, false);
    }

    @Override // miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver
    public void onResume() {
        AppCompatActivity activity;
        FloatingActivitySwitcher floatingActivitySwitcher = FloatingActivitySwitcher.getInstance();
        if (floatingActivitySwitcher == null || (activity = floatingActivitySwitcher.getActivity(getActivityIdentity(), getActivityTaskId())) == null || !activity.isInFloatingWindowMode()) {
            return;
        }
        if (floatingActivitySwitcher.getPreviousActivity(activity) != null) {
            activity.hideFloatingDimBackground();
        }
        reenterTransition(activity);
    }

    @Override // miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver
    public void onDestroy() {
        FloatingActivitySwitcher floatingActivitySwitcher = FloatingActivitySwitcher.getInstance();
        if (floatingActivitySwitcher != null) {
            floatingActivitySwitcher.remove(getActivityIdentity(), getActivityTaskId());
        }
    }

    private void reenterTransition(AppCompatActivity appCompatActivity) {
        ArrayList activityList;
        int activityIndex;
        AppCompatActivity appCompatActivity2;
        FloatingActivitySwitcher floatingActivitySwitcher = FloatingActivitySwitcher.getInstance();
        if (floatingActivitySwitcher == null || (activityList = floatingActivitySwitcher.getActivityList(appCompatActivity.getTaskId())) == null || (activityIndex = floatingActivitySwitcher.getActivityIndex(appCompatActivity) + 1) >= activityList.size() || (appCompatActivity2 = (AppCompatActivity) activityList.get(activityIndex)) == null || !appCompatActivity2.isFinishing()) {
            return;
        }
        executeCloseExit(appCompatActivity);
    }

    private void executeCloseExit(final AppCompatActivity appCompatActivity) {
        FloatingActivitySwitcher floatingActivitySwitcher;
        final View lastActivityPanel;
        if (FloatingAnimHelper.isSupportTransWithClipAnim() || (floatingActivitySwitcher = FloatingActivitySwitcher.getInstance()) == null || (lastActivityPanel = floatingActivitySwitcher.getLastActivityPanel()) == null) {
            return;
        }
        lastActivityPanel.post(new Runnable() { // from class: miuix.appcompat.app.floatingactivity.SingleAppFloatingLifecycleObserver$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SingleAppFloatingLifecycleObserver.m5449$r8$lambda$yNAz8mWfG53jkLX46y890Gpmqw(this.f$0, lastActivityPanel, appCompatActivity);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$yNAz8mWfG53jkLX46y890-Gpmqw, reason: not valid java name */
    public static /* synthetic */ void m5449$r8$lambda$yNAz8mWfG53jkLX46y890Gpmqw(SingleAppFloatingLifecycleObserver singleAppFloatingLifecycleObserver, View view, AppCompatActivity appCompatActivity) {
        singleAppFloatingLifecycleObserver.getClass();
        View childAt = ((ViewGroup) view).getChildAt(0);
        if (childAt != null) {
            AnimConfig animConfig = FloatingSwitcherAnimHelper.getAnimConfig(0, null);
            animConfig.addListeners(singleAppFloatingLifecycleObserver.new CloseExitListener(appCompatActivity));
            FloatingSwitcherAnimHelper.executeCloseExitAnimation(childAt, animConfig);
        }
    }

    class CloseExitListener extends TransitionListener {
        WeakReference mHostActivity;

        CloseExitListener(AppCompatActivity appCompatActivity) {
            this.mHostActivity = new WeakReference(appCompatActivity);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            FloatingActivitySwitcher floatingActivitySwitcher;
            View lastActivityPanel;
            super.onComplete(obj);
            AppCompatActivity appCompatActivity = (AppCompatActivity) this.mHostActivity.get();
            if (appCompatActivity == null || appCompatActivity.isDestroyed() || (floatingActivitySwitcher = FloatingActivitySwitcher.getInstance()) == null || (lastActivityPanel = floatingActivitySwitcher.getLastActivityPanel()) == null) {
                return;
            }
            ((ViewGroup) appCompatActivity.getFloatingBrightPanel().getParent()).getOverlay().remove(lastActivityPanel);
        }
    }
}
