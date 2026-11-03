package miuix.view;

import android.view.ActionMode;

/* JADX INFO: loaded from: classes2.dex */
public interface SearchActionMode {

    public interface AnimatedViewListener {
        void onInSearchMode(boolean z);

        void onUpdateOffsetY(int i);
    }

    public interface Callback extends ActionMode.Callback {
    }

    void setAnimatedViewListener(AnimatedViewListener animatedViewListener);
}
