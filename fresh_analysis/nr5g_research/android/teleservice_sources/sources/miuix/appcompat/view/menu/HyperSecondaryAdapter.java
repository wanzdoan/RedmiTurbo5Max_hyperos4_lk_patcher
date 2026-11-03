package miuix.appcompat.view.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.Map;
import miuix.appcompat.R$id;
import miuix.appcompat.internal.view.menu.MenuItemImpl;

/* JADX INFO: loaded from: classes2.dex */
public class HyperSecondaryAdapter extends HyperBaseAdapter {
    private View mHeaderView;
    private Map mSecondaryItemCheckedMap;

    protected HyperSecondaryAdapter(LayoutInflater layoutInflater, List list, Map map) {
        super(layoutInflater, list);
        this.mSecondaryItemCheckedMap = map;
    }

    @Override // miuix.appcompat.view.menu.HyperBaseAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = super.getView(i, view, viewGroup);
        if (i == 0) {
            view2.setId(R$id.tag_secondary_popup_menu_item_head);
            this.mHeaderView = view2;
        }
        return view2;
    }

    public void resumeSecondaryItemClickStatus(int i) {
        List list = this.mMenuItemList;
        if (list == null || list.size() <= 2) {
            return;
        }
        int itemId = ((HyperMenuContract$HyperMenuItem) this.mMenuItemList.get(0)).getItemId();
        Boolean[] boolArr = (Boolean[]) this.mSecondaryItemCheckedMap.get(Integer.valueOf(itemId));
        if (boolArr == null) {
            boolArr = new Boolean[this.mMenuItemList.size() - 2];
        }
        HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem = this.mMenuItemList.get(0) instanceof HyperMenuContract$HyperMenuTextItem ? (HyperMenuContract$HyperMenuTextItem) this.mMenuItemList.get(0) : null;
        if (this.mMultiSelectEnabled) {
            handleMultiSelectClick(i, itemId, boolArr, hyperMenuContract$HyperMenuTextItem);
        } else {
            handleSingleSelectClick(i, itemId, boolArr, hyperMenuContract$HyperMenuTextItem);
        }
    }

    private void handleMultiSelectClick(int i, int i2, Boolean[] boolArr, HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem) {
        HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem2;
        MenuItemImpl menuItem;
        int i3;
        for (int i4 = 2; i4 < this.mMenuItemList.size(); i4++) {
            HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) this.mMenuItemList.get(i4);
            if ((hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem) && (menuItem = (hyperMenuContract$HyperMenuTextItem2 = (HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem).getMenuItem()) != null && menuItem.isCheckable() && !hyperMenuContract$HyperMenuTextItem2.isHeaderItem && hyperMenuContract$HyperMenuTextItem2.getItemId() == i && (i3 = i4 - 2) < boolArr.length) {
                boolean zEquals = Boolean.TRUE.equals(boolArr[i3]);
                boolean z = !zEquals;
                boolArr[i3] = Boolean.valueOf(z);
                hyperMenuContract$HyperMenuTextItem2.checkStatus = !zEquals ? HyperMenuContract$CheckableType.CHECKED : HyperMenuContract$CheckableType.NOT_CHECKED;
                menuItem.setChecked(z);
                break;
            }
        }
        if (hyperMenuContract$HyperMenuTextItem != null && hyperMenuContract$HyperMenuTextItem.showSubtitle && this.mMenuItemList.size() >= 2) {
            List list = this.mMenuItemList;
            updateHeaderSubtitle(hyperMenuContract$HyperMenuTextItem, HyperMenuUtils.joinCheckedItemTitles(list.subList(2, list.size()), boolArr));
        }
        this.mSecondaryItemCheckedMap.put(Integer.valueOf(i2), boolArr);
    }

    private void handleSingleSelectClick(int i, int i2, Boolean[] boolArr, HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem) {
        HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem2;
        MenuItemImpl menuItem;
        for (int i3 = 0; i3 < this.mMenuItemList.size(); i3++) {
            HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) this.mMenuItemList.get(i3);
            if ((hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem) && (menuItem = (hyperMenuContract$HyperMenuTextItem2 = (HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem).getMenuItem()) != null && menuItem.isCheckable() && !hyperMenuContract$HyperMenuTextItem2.isHeaderItem && i3 >= 2) {
                boolean z = hyperMenuContract$HyperMenuTextItem2.getItemId() == i;
                boolArr[i3 - 2] = Boolean.valueOf(z);
                hyperMenuContract$HyperMenuTextItem2.checkStatus = z ? HyperMenuContract$CheckableType.CHECKED : HyperMenuContract$CheckableType.NOT_CHECKED;
                menuItem.setChecked(hyperMenuContract$HyperMenuTextItem2.isChecked());
                if (z && hyperMenuContract$HyperMenuTextItem != null && hyperMenuContract$HyperMenuTextItem.showSubtitle) {
                    updateHeaderSubtitle(hyperMenuContract$HyperMenuTextItem, menuItem.getTitle());
                }
            }
        }
        this.mSecondaryItemCheckedMap.put(Integer.valueOf(i2), boolArr);
        notifyDataSetChanged();
    }

    private void updateHeaderSubtitle(HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem, CharSequence charSequence) {
        if (hyperMenuContract$HyperMenuTextItem == null) {
            return;
        }
        hyperMenuContract$HyperMenuTextItem.setSubtitle(charSequence);
        MenuItemImpl menuItem = hyperMenuContract$HyperMenuTextItem.getMenuItem();
        if (menuItem != null) {
            menuItem.setSubtitle(charSequence);
        }
    }

    public View getHeaderView() {
        return this.mHeaderView;
    }
}
