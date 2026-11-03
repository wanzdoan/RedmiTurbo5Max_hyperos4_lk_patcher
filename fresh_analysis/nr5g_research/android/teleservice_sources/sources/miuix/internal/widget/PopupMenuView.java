package miuix.internal.widget;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import miuix.popupwidget.widget.PopupView;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PopupMenuView implements IPopupMenuWidget {
    private PopupMenuAdapter mAdapter;
    private final Context mContext;
    private View mDecorView;
    private PopupView mPopupView;

    protected abstract void onDismiss();

    protected abstract void onMenuItemClick(MenuItem menuItem);

    protected abstract void onPreDismiss();

    public PopupMenuView(Context context) {
        this.mContext = context;
        this.mPopupView = new PopupView(context);
        initAdapter(context);
        setupListener();
    }

    @Override // miuix.internal.widget.IPopupMenuWidget
    public void update(Menu menu) {
        this.mAdapter.update(menu);
    }

    @Override // miuix.internal.widget.IPopupMenuWidget
    public void showAsDropDown(View view) {
        this.mPopupView.setAnchorView(view).setDecorView(this.mDecorView);
        this.mPopupView.showWithAnchor();
    }

    @Override // miuix.internal.widget.IPopupMenuWidget
    public void dismiss() throws Exception {
        this.mPopupView.dismiss();
    }

    public void setOnDismissListener(PopupView.OnDismissListener onDismissListener) {
        this.mPopupView.setOnDismissListener(onDismissListener);
    }

    private void initAdapter(Context context) {
        PopupMenuAdapter popupMenuAdapter = new PopupMenuAdapter(context);
        this.mAdapter = popupMenuAdapter;
        this.mPopupView.setAdapter(popupMenuAdapter);
    }

    private void setupListener() {
        this.mPopupView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: miuix.internal.widget.PopupMenuView.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView adapterView, View view, int i, long j) throws Exception {
                PopupMenuView.this.onMenuItemClick(PopupMenuView.this.mAdapter.getItem(i));
                PopupMenuView.this.dismiss();
            }
        });
        this.mPopupView.setOnPreDismissListener(new PopupView.OnPreDismissListener() { // from class: miuix.internal.widget.PopupMenuView$$ExternalSyntheticLambda0
            @Override // miuix.popupwidget.widget.PopupView.OnPreDismissListener
            public final void onPreDismiss() {
                this.f$0.onPreDismiss();
            }
        });
        setOnDismissListener(new PopupView.OnDismissListener() { // from class: miuix.internal.widget.PopupMenuView$$ExternalSyntheticLambda1
            @Override // miuix.popupwidget.widget.PopupView.OnDismissListener
            public final void onDismiss() {
                this.f$0.onDismiss();
            }
        });
    }
}
