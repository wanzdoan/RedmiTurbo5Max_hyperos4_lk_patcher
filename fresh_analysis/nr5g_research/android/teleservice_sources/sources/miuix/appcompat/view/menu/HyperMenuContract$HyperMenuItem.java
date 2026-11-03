package miuix.appcompat.view.menu;

import miuix.appcompat.internal.view.menu.MenuItemImpl;

/* JADX INFO: loaded from: classes2.dex */
public abstract class HyperMenuContract$HyperMenuItem {
    private final int itemId;
    private final MenuItemImpl menuItem;

    public HyperMenuContract$HyperMenuItem(MenuItemImpl menuItemImpl, int i) {
        this.menuItem = menuItemImpl;
        this.itemId = i;
    }

    public HyperMenuContract$HyperMenuItem(MenuItemImpl menuItemImpl) {
        this.menuItem = menuItemImpl;
        if (menuItemImpl != null) {
            this.itemId = menuItemImpl.getItemId();
        } else {
            this.itemId = -200;
        }
    }

    public MenuItemImpl getMenuItem() {
        return this.menuItem;
    }

    public int getItemId() {
        return this.itemId;
    }
}
