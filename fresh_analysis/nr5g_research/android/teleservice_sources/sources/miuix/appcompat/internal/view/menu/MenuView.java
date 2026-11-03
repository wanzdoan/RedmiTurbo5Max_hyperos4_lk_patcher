package miuix.appcompat.internal.view.menu;

/* JADX INFO: loaded from: classes2.dex */
public interface MenuView {

    public interface ItemView {
        MenuItemImpl getItemData();

        void initialize(MenuItemImpl menuItemImpl, int i);

        boolean prefersCondensedTitle();

        void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker);
    }

    boolean filterLeftoverView(int i);

    boolean hasBackgroundView();

    void initialize(MenuBuilder menuBuilder);
}
