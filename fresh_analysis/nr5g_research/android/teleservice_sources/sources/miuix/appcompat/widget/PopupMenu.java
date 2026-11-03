package miuix.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$styleable;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.internal.widget.IPopupMenuWidget;
import miuix.internal.widget.PopupMenuView;
import miuix.internal.widget.PopupMenuWindow;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PopupMenu {
    private final View mAnchor;
    private final Context mContext;
    private boolean mIsEnableImmersive;
    private final MenuBuilder mMenu;
    private OnMenuItemClickListener mMenuItemClickListener;
    private IPopupMenuWidget mPopupMenu;

    public interface OnDismissListener {
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public interface OnPreDismissListener {
    }

    static /* synthetic */ OnPreDismissListener access$100(PopupMenu popupMenu) {
        popupMenu.getClass();
        return null;
    }

    static /* synthetic */ OnDismissListener access$200(PopupMenu popupMenu) {
        popupMenu.getClass();
        return null;
    }

    public PopupMenu(Context context, View view) {
        this(context, view, 0);
    }

    public PopupMenu(Context context, View view, int i) {
        this.mIsEnableImmersive = true;
        if (i == 0) {
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(null, R$styleable.miuiPopupMenu, R$attr.miuiPopupMenuStyle, 0);
            try {
                int resourceId = typedArrayObtainStyledAttributes.getResourceId(R$styleable.miuiPopupMenu_miuiPopupTheme, 0);
                typedArrayObtainStyledAttributes.recycle();
                i = resourceId;
            } catch (Throwable th) {
                typedArrayObtainStyledAttributes.recycle();
                throw th;
            }
        }
        if (i != 0) {
            this.mContext = new ContextThemeWrapper(context, i);
        } else {
            this.mContext = context;
        }
        this.mAnchor = view;
        this.mMenu = new MenuBuilder(this.mContext);
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public void show() {
        IPopupMenuWidget popupMenu = getPopupMenu();
        popupMenu.update(this.mMenu);
        popupMenu.showAsDropDown(this.mAnchor);
    }

    public void dismiss() {
        getPopupMenu().dismiss();
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mMenuItemClickListener = onMenuItemClickListener;
    }

    private IPopupMenuWidget createPopupMenu(boolean z) {
        if (z) {
            return new PopupMenuWindow(this.mContext) { // from class: miuix.appcompat.widget.PopupMenu.1
                @Override // miuix.internal.widget.PopupMenuWindow
                protected void onMenuItemClick(MenuItem menuItem) {
                    if (PopupMenu.this.mMenuItemClickListener != null) {
                        PopupMenu.this.mMenuItemClickListener.onMenuItemClick(menuItem);
                    }
                }

                @Override // miuix.internal.widget.PopupMenuWindow
                protected void onPreDismiss() {
                    PopupMenu.access$100(PopupMenu.this);
                }

                @Override // miuix.internal.widget.PopupMenuWindow
                protected void onDismiss() {
                    PopupMenu.access$200(PopupMenu.this);
                }
            };
        }
        return new PopupMenuView(this.mContext) { // from class: miuix.appcompat.widget.PopupMenu.2
            @Override // miuix.internal.widget.PopupMenuView
            protected void onMenuItemClick(MenuItem menuItem) {
                if (PopupMenu.this.mMenuItemClickListener != null) {
                    PopupMenu.this.mMenuItemClickListener.onMenuItemClick(menuItem);
                }
            }

            @Override // miuix.internal.widget.PopupMenuView
            protected void onPreDismiss() {
                PopupMenu.access$100(PopupMenu.this);
            }

            @Override // miuix.internal.widget.PopupMenuView
            protected void onDismiss() {
                PopupMenu.access$200(PopupMenu.this);
            }
        };
    }

    private IPopupMenuWidget getPopupMenu() {
        if (this.mPopupMenu == null) {
            this.mPopupMenu = createPopupMenu(this.mIsEnableImmersive);
        }
        return this.mPopupMenu;
    }
}
