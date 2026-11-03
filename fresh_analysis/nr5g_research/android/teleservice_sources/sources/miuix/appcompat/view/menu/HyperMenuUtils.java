package miuix.appcompat.view.menu;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuItemImpl;

/* JADX INFO: loaded from: classes2.dex */
public abstract class HyperMenuUtils {
    public static void updatePrimaryGroupStatusByPosition(Map map, int i, int i2, int i3) {
        int i4 = i - i2;
        int i5 = ((i + i3) - i2) - 1;
        int i6 = i4;
        while (i6 >= i4 && i6 <= i5) {
            map.put(Integer.valueOf(i6), Boolean.valueOf(i6 == i4 + i2));
            i6++;
        }
    }

    public static int getGroupIndexAndUpdateStatus(ArrayList arrayList, Map map, int i, boolean z) {
        int i2 = -1;
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) arrayList.get(i3);
            if (hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem) {
                HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem = (HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem;
                MenuItemImpl menuItem = hyperMenuContract$HyperMenuTextItem.getMenuItem();
                if (hyperMenuContract$HyperMenuTextItem.getItemId() == i) {
                    i2 = i3;
                }
                if (menuItem != null) {
                    if (menuItem.isCheckable() && !hyperMenuContract$HyperMenuTextItem.isExpandable) {
                        hyperMenuContract$HyperMenuTextItem.checkStatus = hyperMenuContract$HyperMenuTextItem.getItemId() == i ? HyperMenuContract$CheckableType.CHECKED : HyperMenuContract$CheckableType.NOT_CHECKED;
                        menuItem.setChecked(hyperMenuContract$HyperMenuTextItem.isChecked());
                    }
                    if (z) {
                        map.put(Integer.valueOf(menuItem.getItemId()), Boolean.valueOf(menuItem.getItemId() == i));
                    }
                }
            }
        }
        return i2;
    }

    public static boolean hasAnyVisibleSubMenuItem(List list) {
        if (list != null && !list.isEmpty()) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                MenuItemImpl menuItem = ((HyperMenuContract$HyperMenuItem) it.next()).getMenuItem();
                if (menuItem != null && menuItem.isVisible()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean subMenuHasAnyVisibleItem(SubMenu subMenu) {
        if (subMenu != null && subMenu.size() != 0) {
            for (int i = 0; i < subMenu.size(); i++) {
                MenuItem item = subMenu.getItem(i);
                if (item != null && item.isVisible()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setMenuItemChecked(MenuItemImpl menuItemImpl, boolean z) {
        if (menuItemImpl == null || menuItemImpl.isChecked() == z) {
            return;
        }
        menuItemImpl.setChecked(z);
    }

    public static boolean visibleItemListIsChanged(MenuBuilder menuBuilder) {
        if (menuBuilder == null || menuBuilder.size() == 0) {
            return false;
        }
        ArrayList visibleItems = menuBuilder.getVisibleItems();
        ArrayList arrayList = new ArrayList();
        getRecentVisibleItems(menuBuilder, arrayList);
        if (visibleItems == null || visibleItems.isEmpty()) {
            return true ^ arrayList.isEmpty();
        }
        if (visibleItems.size() != arrayList.size()) {
            return true;
        }
        return adjustListItemIsDiff(arrayList, visibleItems);
    }

    private static boolean adjustListItemIsDiff(ArrayList arrayList, ArrayList arrayList2) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (!arrayList2.contains((MenuItemImpl) arrayList.get(i))) {
                return true;
            }
        }
        return false;
    }

    private static void getRecentVisibleItems(MenuBuilder menuBuilder, ArrayList arrayList) {
        for (int i = 0; i < menuBuilder.size(); i++) {
            MenuItem item = menuBuilder.getItem(i);
            if (item.isVisible() && (item instanceof MenuItemImpl)) {
                arrayList.add((MenuItemImpl) item);
            }
        }
    }

    public static void checkPrimaryItemVisibility(MenuBuilder menuBuilder) {
        boolean zSubMenuHasAnyVisibleItem;
        if (menuBuilder == null || menuBuilder.size() == 0) {
            return;
        }
        for (int i = 0; i < menuBuilder.size(); i++) {
            MenuItem item = menuBuilder.getItem(i);
            boolean zIsVisible = item.isVisible();
            if (item.hasSubMenu() && zIsVisible != (zSubMenuHasAnyVisibleItem = subMenuHasAnyVisibleItem(item.getSubMenu()))) {
                item.setVisible(zSubMenuHasAnyVisibleItem);
            }
        }
        if (visibleItemListIsChanged(menuBuilder)) {
            menuBuilder.updateVisibleItemCountLimit();
        }
    }

    public static boolean isGroupTitleItem(MenuItemImpl menuItemImpl) {
        if (menuItemImpl == null || menuItemImpl.getTitle() == null) {
            return false;
        }
        return TextUtils.equals(menuItemImpl.getTitleCondensed(), "#" + ((Object) menuItemImpl.getTitle()) + "!");
    }

    public static CharSequence joinCheckedItemTitles(List list, Boolean[] boolArr) {
        if (list != null && boolArr != null) {
            StringBuilder sb = new StringBuilder();
            int iMin = Math.min(list.size(), boolArr.length);
            for (int i = 0; i < iMin; i++) {
                if (Boolean.TRUE.equals(boolArr[i])) {
                    appendItemTitle(sb, (HyperMenuContract$HyperMenuItem) list.get(i));
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }
        return null;
    }

    public static CharSequence findFirstCheckedItemTitle(List list, Boolean[] boolArr) {
        HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem;
        if (list != null && boolArr != null) {
            int iMin = Math.min(list.size(), boolArr.length);
            for (int i = 0; i < iMin; i++) {
                if (Boolean.TRUE.equals(boolArr[i]) && (hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) list.get(i)) != null && hyperMenuContract$HyperMenuItem.getMenuItem() != null) {
                    return hyperMenuContract$HyperMenuItem.getMenuItem().getTitle();
                }
            }
        }
        return null;
    }

    private static void appendItemTitle(StringBuilder sb, HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem) {
        if (hyperMenuContract$HyperMenuItem == null || hyperMenuContract$HyperMenuItem.getMenuItem() == null || hyperMenuContract$HyperMenuItem.getMenuItem().getTitle() == null) {
            return;
        }
        if (sb.length() > 0) {
            sb.append("、");
        }
        sb.append(hyperMenuContract$HyperMenuItem.getMenuItem().getTitle());
    }
}
