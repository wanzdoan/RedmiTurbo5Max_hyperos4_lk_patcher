package miuix.appcompat.internal.view.menu;

import android.content.Context;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import miuix.appcompat.R$layout;
import miuix.appcompat.internal.util.MenuBlurUtils;
import miuix.internal.util.AnimHelper;
import miuix.internal.util.TaggingDrawableUtil;
import miuix.popupwidget.internal.strategy.IPopupWindowStrategy;
import miuix.popupwidget.widget.PopupView;

/* JADX INFO: loaded from: classes2.dex */
public class MenuPopupHelper implements AdapterView.OnItemClickListener, View.OnKeyListener, PopupView.OnDismissListener, MenuPresenter {
    private static final int ITEM_LAYOUT = R$layout.miuix_appcompat_popup_menu_item_layout;
    private MenuAdapter mAdapter;
    private View mAnchorView;
    private Context mContext;
    private View mFenceDecor;
    boolean mForceShowIcon;
    private LayoutInflater mInflater;
    private MenuBuilder mMenu;
    private boolean mOverflowOnly;
    private PopupView mPopupView;
    private MenuPresenter.Callback mPresenterCallback;
    private IPopupWindowStrategy mStrategy;
    private int mMenuItemLayout = ITEM_LAYOUT;
    private int mGravity = 49;

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean flagActionItems() {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public int getId() {
        return 0;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void onRestoreInstanceState(Parcelable parcelable) {
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public Parcelable onSaveInstanceState() {
        return null;
    }

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder, View view, View view2, boolean z) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMenu = menuBuilder;
        this.mOverflowOnly = z;
        this.mAnchorView = view;
        this.mFenceDecor = view2;
        menuBuilder.addMenuPresenter(this);
    }

    public void setForceShowIcon(boolean z) {
        this.mForceShowIcon = z;
    }

    public void setDropDownGravity(int i) {
        this.mGravity = i;
    }

    public void setMenuItemLayout(int i) {
        this.mMenuItemLayout = i;
    }

    public boolean tryShow() {
        PopupView popupView = new PopupView(this.mContext);
        this.mPopupView = popupView;
        popupView.setDropDownGravity(this.mGravity);
        IPopupWindowStrategy iPopupWindowStrategy = this.mStrategy;
        if (iPopupWindowStrategy != null) {
            this.mPopupView.setStrategy(iPopupWindowStrategy);
        }
        this.mPopupView.setOnItemClickListener(this);
        this.mPopupView.setOnDismissListener(this);
        MenuAdapter menuAdapter = new MenuAdapter(this.mMenu);
        this.mAdapter = menuAdapter;
        this.mPopupView.setAdapter(menuAdapter);
        View view = this.mFenceDecor;
        if (view != null) {
            this.mPopupView.setDecorView(view);
        }
        this.mPopupView.setMaterialEnabled(MenuBlurUtils.isMenuBlurOptionEnabled(this.mContext, 1));
        if (!this.mPopupView.prepareShow(this.mAnchorView)) {
            return false;
        }
        this.mPopupView.showWithAnchor();
        return true;
    }

    public void dismiss(boolean z) {
        if (isShowing()) {
            PopupView popupView = this.mPopupView;
            if (z) {
                popupView.dismiss();
            } else {
                popupView.realDismiss();
            }
        }
    }

    @Override // miuix.popupwidget.widget.PopupView.OnDismissListener
    public void onDismiss() {
        this.mPopupView = null;
        this.mMenu.close();
    }

    public void setStrategy(IPopupWindowStrategy iPopupWindowStrategy) {
        this.mStrategy = iPopupWindowStrategy;
    }

    public boolean isShowing() {
        PopupView popupView = this.mPopupView;
        return popupView != null && popupView.isShowing();
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        MenuAdapter menuAdapter = this.mAdapter;
        menuAdapter.mAdapterMenu.performItemAction(menuAdapter.getItem(i), 0);
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || i != 82) {
            return false;
        }
        dismiss(false);
        return true;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void updateMenuView(boolean z) {
        MenuAdapter menuAdapter = this.mAdapter;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }

    public void setCallback(MenuPresenter.Callback callback) {
        this.mPresenterCallback = callback;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        boolean z;
        if (subMenuBuilder.hasVisibleItems()) {
            MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this.mContext, subMenuBuilder, this.mAnchorView, this.mFenceDecor, false);
            menuPopupHelper.setCallback(this.mPresenterCallback);
            int size = subMenuBuilder.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    z = false;
                    break;
                }
                MenuItem item = subMenuBuilder.getItem(i);
                if (item.isVisible() && item.getIcon() != null) {
                    z = true;
                    break;
                }
                i++;
            }
            menuPopupHelper.setForceShowIcon(z);
            if (menuPopupHelper.tryShow()) {
                MenuPresenter.Callback callback = this.mPresenterCallback;
                if (callback != null) {
                    callback.onOpenSubMenu(subMenuBuilder);
                }
                return true;
            }
        }
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        if (menuBuilder != this.mMenu) {
            return;
        }
        dismiss(true);
        MenuPresenter.Callback callback = this.mPresenterCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, z);
        }
    }

    class MenuAdapter extends BaseAdapter {
        private MenuBuilder mAdapterMenu;
        private int mExpandedIndex = -1;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public MenuAdapter(MenuBuilder menuBuilder) {
            this.mAdapterMenu = menuBuilder;
            findExpandedIndex();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            boolean z = MenuPopupHelper.this.mOverflowOnly;
            MenuBuilder menuBuilder = this.mAdapterMenu;
            ArrayList nonActionItems = z ? menuBuilder.getNonActionItems() : menuBuilder.getVisibleItems();
            if (this.mExpandedIndex < 0) {
                return nonActionItems.size();
            }
            return nonActionItems.size() - 1;
        }

        @Override // android.widget.Adapter
        public MenuItemImpl getItem(int i) {
            boolean z = MenuPopupHelper.this.mOverflowOnly;
            MenuBuilder menuBuilder = this.mAdapterMenu;
            ArrayList nonActionItems = z ? menuBuilder.getNonActionItems() : menuBuilder.getVisibleItems();
            int i2 = this.mExpandedIndex;
            if (i2 >= 0 && i >= i2) {
                i++;
            }
            return (MenuItemImpl) nonActionItems.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = MenuPopupHelper.this.mInflater.inflate(MenuPopupHelper.this.mMenuItemLayout, viewGroup, false);
                AnimHelper.addItemPressEffect(view, true);
            }
            TaggingDrawableUtil.updateItemPadding(view, i, getCount());
            MenuView.ItemView itemView = (MenuView.ItemView) view;
            if (MenuPopupHelper.this.mForceShowIcon) {
                ((ListMenuItemView) view).setForceShowIcon(true);
            }
            itemView.initialize(getItem(i), 0);
            return view;
        }

        void findExpandedIndex() {
            MenuItemImpl expandedItem = MenuPopupHelper.this.mMenu.getExpandedItem();
            if (expandedItem != null) {
                ArrayList nonActionItems = MenuPopupHelper.this.mMenu.getNonActionItems();
                int size = nonActionItems.size();
                for (int i = 0; i < size; i++) {
                    if (((MenuItemImpl) nonActionItems.get(i)) == expandedItem) {
                        this.mExpandedIndex = i;
                        return;
                    }
                }
            }
            this.mExpandedIndex = -1;
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            findExpandedIndex();
            super.notifyDataSetChanged();
        }
    }
}
