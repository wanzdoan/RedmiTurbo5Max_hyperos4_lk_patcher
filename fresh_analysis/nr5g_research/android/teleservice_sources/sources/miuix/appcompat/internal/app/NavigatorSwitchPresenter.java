package miuix.appcompat.internal.app;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public class NavigatorSwitchPresenter {
    private float mAlpha;
    private final View mNavigatorSwitch;
    private boolean mSuppressAlpha;
    private boolean mSuppressVisibility;
    private int mVisibility;

    public NavigatorSwitchPresenter(View view) {
        this.mNavigatorSwitch = view;
        this.mVisibility = view.getVisibility();
        this.mAlpha = view.getAlpha();
    }

    public void suppressVisibility(boolean z, int i) {
        this.mSuppressVisibility = z;
        View view = this.mNavigatorSwitch;
        if (z) {
            view.setVisibility(i);
        } else {
            view.setVisibility(this.mVisibility);
        }
    }

    public void suppressAlpha(boolean z, float f) {
        this.mSuppressAlpha = z;
        View view = this.mNavigatorSwitch;
        if (z) {
            view.setAlpha(f);
        } else {
            view.setAlpha(this.mAlpha);
        }
    }
}
