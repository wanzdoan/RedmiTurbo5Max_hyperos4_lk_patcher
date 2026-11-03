package miuix.internal.widget;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import miuix.popupwidget.widget.PopupWindow;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PopupMenuWindow extends PopupWindow implements IPopupMenuWidget {
    private PopupMenuAdapter mAdapter;
    private View mLastAnchor;

    protected abstract void onDismiss();

    protected abstract void onMenuItemClick(MenuItem menuItem);

    protected abstract void onPreDismiss();

    public PopupMenuWindow(Context context) {
        super(context);
        PopupMenuAdapter popupMenuAdapter = new PopupMenuAdapter(context);
        this.mAdapter = popupMenuAdapter;
        setAdapter(popupMenuAdapter);
        setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: miuix.internal.widget.PopupMenuWindow$$ExternalSyntheticLambda0
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                PopupMenuWindow.m5469$r8$lambda$jkk4f2vTEBfjQxUHA94vX4i0RI(this.f$0, adapterView, view, i, j);
            }
        });
        setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() { // from class: miuix.internal.widget.PopupMenuWindow$$ExternalSyntheticLambda1
            @Override // android.widget.PopupWindow.OnDismissListener
            public final void onDismiss() {
                this.f$0.onDismiss();
            }
        });
        setOnPreDismissListener(new PopupWindow.OnPreDismissListener() { // from class: miuix.internal.widget.PopupMenuWindow$$ExternalSyntheticLambda2
            @Override // miuix.popupwidget.widget.PopupWindow.OnPreDismissListener
            public final void onPreDismiss() {
                this.f$0.onPreDismiss();
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$jkk4f2vTEB-fjQxUHA94vX4i0RI, reason: not valid java name */
    public static /* synthetic */ void m5469$r8$lambda$jkk4f2vTEBfjQxUHA94vX4i0RI(final PopupMenuWindow popupMenuWindow, AdapterView adapterView, View view, int i, long j) {
        MenuItem item = popupMenuWindow.mAdapter.getItem(i);
        if (item.hasSubMenu()) {
            final SubMenu subMenu = item.getSubMenu();
            popupMenuWindow.setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() { // from class: miuix.internal.widget.PopupMenuWindow$$ExternalSyntheticLambda3
                @Override // android.widget.PopupWindow.OnDismissListener
                public final void onDismiss() {
                    PopupMenuWindow.m5468$r8$lambda$FfEC2Yf9zeBX4e93LYiXr5kDHU(this.f$0, subMenu);
                }
            });
        } else {
            popupMenuWindow.onMenuItemClick(item);
        }
        popupMenuWindow.dismiss();
    }

    /* JADX INFO: renamed from: $r8$lambda$FfEC2Yf9zeBX4e93LYiX-r5kDHU, reason: not valid java name */
    public static /* synthetic */ void m5468$r8$lambda$FfEC2Yf9zeBX4e93LYiXr5kDHU(PopupMenuWindow popupMenuWindow, SubMenu subMenu) {
        popupMenuWindow.setOnDismissListener(null);
        popupMenuWindow.update(subMenu);
        popupMenuWindow.showAsDropDown(popupMenuWindow.mLastAnchor);
    }

    @Override // miuix.internal.widget.IPopupMenuWidget
    public void update(Menu menu) {
        this.mAdapter.update(menu);
    }

    @Override // miuix.popupwidget.widget.PopupWindow, android.widget.PopupWindow, miuix.internal.widget.IPopupMenuWidget
    public void showAsDropDown(View view) {
        this.mLastAnchor = view;
        if (prepareShow(view)) {
            super.showAsDropDown(view);
        }
    }

    @Override // miuix.popupwidget.widget.PopupWindow, android.widget.PopupWindow
    public void showAtLocation(View view, int i, int i2, int i3) {
        if (prepareShow(view)) {
            super.showAtLocation(view.getRootView(), i, i2, i3);
        }
    }
}
