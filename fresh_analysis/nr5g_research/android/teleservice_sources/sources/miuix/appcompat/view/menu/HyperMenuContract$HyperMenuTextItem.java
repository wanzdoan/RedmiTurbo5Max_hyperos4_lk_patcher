package miuix.appcompat.view.menu;

import miuix.appcompat.internal.view.menu.MenuItemImpl;

/* JADX INFO: loaded from: classes2.dex */
public class HyperMenuContract$HyperMenuTextItem extends HyperMenuContract$HyperMenuItem {
    public HyperMenuContract$CheckableType checkStatus;
    public boolean isExpandable;
    public boolean isHeaderItem;
    public boolean isMultiSelect;
    public int itemForeignKey;
    public boolean showSubtitle;
    public CharSequence subtitle;

    public HyperMenuContract$HyperMenuTextItem(MenuItemImpl menuItemImpl) {
        super(menuItemImpl);
        this.checkStatus = HyperMenuContract$CheckableType.NON_SUPPORT;
        this.itemForeignKey = -1;
        this.isMultiSelect = false;
        this.showSubtitle = true;
    }

    public boolean isChecked() {
        return this.checkStatus == HyperMenuContract$CheckableType.CHECKED;
    }

    public void setSubtitle(CharSequence charSequence) {
        this.subtitle = charSequence;
    }

    public CharSequence getSubtitle() {
        return this.subtitle;
    }
}
