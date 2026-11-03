package miuix.appcompat.internal.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.ActionMode;
import android.view.View;
import java.lang.ref.WeakReference;
import miuix.appcompat.internal.app.widget.SearchActionModeView;
import miuix.view.SearchActionMode;

/* JADX INFO: loaded from: classes2.dex */
public class SearchActionModeImpl extends ActionModeImpl implements SearchActionMode {
    public SearchActionModeImpl(Context context, ActionMode.Callback callback) {
        super(context, callback);
    }

    @Override // android.view.ActionMode
    public void setCustomView(View view) {
        ((SearchActionModeView) this.mActionModeView.get()).setCustomView(view);
    }

    @Override // miuix.appcompat.internal.view.ActionModeImpl, android.view.ActionMode
    public View getCustomView() {
        return ((SearchActionModeView) this.mActionModeView.get()).getCustomView();
    }

    @Override // miuix.view.SearchActionMode
    public void setAnimatedViewListener(SearchActionMode.AnimatedViewListener animatedViewListener) {
        ((SearchActionModeView) this.mActionModeView.get()).setAnimatedViewListener(animatedViewListener);
    }

    public void setPendingInsets(Rect rect) {
        WeakReference weakReference = this.mActionModeView;
        SearchActionModeView searchActionModeView = weakReference != null ? (SearchActionModeView) weakReference.get() : null;
        if (searchActionModeView != null) {
            searchActionModeView.rePaddingAndRelayout(rect);
        }
    }
}
