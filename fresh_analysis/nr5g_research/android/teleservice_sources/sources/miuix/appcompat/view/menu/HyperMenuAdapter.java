package miuix.appcompat.view.menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuItemImpl;
import miuix.appcompat.internal.view.menu.SubMenuBuilder;

/* JADX INFO: loaded from: classes2.dex */
public class HyperMenuAdapter extends HyperBaseAdapter {
    private final Map mGroupIdOverrideMap;
    private Map mGroupedMap;
    private boolean mOverflowOnly;
    private boolean mPrimaryCheckedMapFirstModified;
    private Map mPrimaryItemCheckedMap;
    private List mPrimaryMenuItems;
    private boolean mSavePrimaryStatusByIdEnabled;
    private boolean mSecondaryCheckedMapFirstModified;
    private Map mSecondaryItemCheckedMap;
    private Map mSecondaryMenuMap;
    private Map mUserPreCheckedPrimaryMap;
    private Map mUserPreCheckedSecondaryMap;

    public HyperMenuAdapter(Context context) {
        this(context, null, false);
    }

    public HyperMenuAdapter(Context context, MenuBuilder menuBuilder, boolean z) {
        this.mPrimaryMenuItems = new ArrayList();
        this.mSecondaryMenuMap = new HashMap();
        this.mGroupedMap = new HashMap();
        this.mGroupIdOverrideMap = new HashMap();
        this.mPrimaryItemCheckedMap = new HashMap();
        this.mSavePrimaryStatusByIdEnabled = false;
        this.mSecondaryItemCheckedMap = new HashMap();
        this.mPrimaryCheckedMapFirstModified = true;
        this.mSecondaryCheckedMapFirstModified = true;
        this.mInflater = LayoutInflater.from(context);
        this.mMenuItemList = this.mPrimaryMenuItems;
        this.mOverflowOnly = z;
        if (menuBuilder != null) {
            buildGroupedMenuItems(menuBuilder);
        }
    }

    private void buildGroupedMenuItems(MenuBuilder menuBuilder) {
        Map map;
        if (menuBuilder == null || (map = this.mSecondaryMenuMap) == null || this.mPrimaryMenuItems == null || this.mGroupedMap == null) {
            return;
        }
        map.clear();
        this.mPrimaryMenuItems.clear();
        this.mGroupedMap.clear();
        HyperMenuUtils.checkPrimaryItemVisibility(menuBuilder);
        ArrayList nonActionItems = this.mOverflowOnly ? menuBuilder.getNonActionItems() : menuBuilder.getVisibleItems();
        if (nonActionItems != null) {
            assembleGroupData(this.mGroupedMap, nonActionItems);
        }
        selectPrimaryMenu(this.mGroupedMap);
        if (nonActionItems != null) {
            assembleSecondaryMenu(this.mGroupedMap, nonActionItems);
        }
        removeInvalidatePrimaryItem(true);
    }

    private void assembleSecondaryMenu(Map map, ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            MenuItemImpl menuItemImpl = (MenuItemImpl) arrayList.get(i2);
            int groupForeignKey = getGroupForeignKey(menuItemImpl);
            if (groupForeignKey != -1 && menuItemImpl != null) {
                ArrayList arrayList3 = (ArrayList) map.get(Integer.valueOf(menuItemImpl.getGroupId()));
                HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItemFindPrimaryItemByForeignKey = findPrimaryItemByForeignKey(this.mPrimaryMenuItems, groupForeignKey);
                if (arrayList3 != null && hyperMenuContract$HyperMenuItemFindPrimaryItemByForeignKey != null && hyperMenuContract$HyperMenuItemFindPrimaryItemByForeignKey.getMenuItem() != null) {
                    if (hyperMenuContract$HyperMenuItemFindPrimaryItemByForeignKey.getMenuItem().isVisible() != HyperMenuUtils.hasAnyVisibleSubMenuItem(arrayList3)) {
                        arrayList2.add(hyperMenuContract$HyperMenuItemFindPrimaryItemByForeignKey.getMenuItem());
                    }
                    buildSecondaryAdapter(arrayList3, hyperMenuContract$HyperMenuItemFindPrimaryItemByForeignKey);
                }
            }
        }
        int size = arrayList2.size();
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            MenuItemImpl menuItemImpl2 = (MenuItemImpl) obj;
            menuItemImpl2.setVisible(!menuItemImpl2.isVisible());
        }
        arrayList2.clear();
    }

    private int getGroupForeignKey(MenuItemImpl menuItemImpl) {
        Intent intent;
        if (menuItemImpl == null || (intent = menuItemImpl.getIntent()) == null) {
            return -1;
        }
        return intent.getIntExtra("miuix:hyperMenu:groupForeignKey", -1);
    }

    private boolean isGroupMultiSelect(ArrayList arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return false;
        }
        HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) arrayList.get(0);
        if (hyperMenuContract$HyperMenuItem.getMenuItem() == null || hyperMenuContract$HyperMenuItem.getMenuItem().getIntent() == null) {
            return false;
        }
        return hyperMenuContract$HyperMenuItem.getMenuItem().getIntent().getBooleanExtra("miuix:hyperMenu:itemMultiSelect", false);
    }

    private void syncPrimarySubtitle(HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem, List list, boolean z) {
        CharSequence checkedItemTitle;
        if ((hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem ? ((HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem).showSubtitle : true) && (checkedItemTitle = getCheckedItemTitle(list, hyperMenuContract$HyperMenuItem.getMenuItem().getItemId(), z)) != null && (hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem)) {
            ((HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem).setSubtitle(checkedItemTitle);
            hyperMenuContract$HyperMenuItem.getMenuItem().setSubtitle(checkedItemTitle);
        }
    }

    private void buildSecondaryAdapter(ArrayList arrayList, HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem) {
        ArrayList arrayList2 = new ArrayList(arrayList);
        handleDefaultCheckedStatus(arrayList2, false, hyperMenuContract$HyperMenuItem.getMenuItem().getItemId());
        boolean zIsGroupMultiSelect = isGroupMultiSelect(arrayList);
        syncPrimarySubtitle(hyperMenuContract$HyperMenuItem, arrayList2, zIsGroupMultiSelect);
        HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem = new HyperMenuContract$HyperMenuTextItem(hyperMenuContract$HyperMenuItem.getMenuItem());
        hyperMenuContract$HyperMenuTextItem.isHeaderItem = true;
        if (hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem) {
            HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem2 = (HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem;
            hyperMenuContract$HyperMenuTextItem.setSubtitle(hyperMenuContract$HyperMenuTextItem2.getSubtitle());
            hyperMenuContract$HyperMenuTextItem.showSubtitle = hyperMenuContract$HyperMenuTextItem2.showSubtitle;
        }
        arrayList2.add(0, hyperMenuContract$HyperMenuTextItem);
        arrayList2.add(1, new HyperMenuContract$HyperMenuDivider());
        HyperSecondaryAdapter hyperSecondaryAdapter = new HyperSecondaryAdapter(this.mInflater, arrayList2, this.mSecondaryItemCheckedMap);
        hyperSecondaryAdapter.setOptionalIconsVisible(getOptionalIconsVisible());
        hyperSecondaryAdapter.setMultiSelectEnabled(zIsGroupMultiSelect);
        this.mSecondaryMenuMap.put(Integer.valueOf(hyperMenuContract$HyperMenuItem.getItemId()), hyperSecondaryAdapter);
    }

    private void selectPrimaryMenu(Map map) {
        for (Integer num : map.keySet()) {
            ArrayList arrayList = (ArrayList) map.get(num);
            boolean zIsPrimaryGroup = isPrimaryGroup(arrayList, num.intValue());
            List listFilterVisibleItems = filterVisibleItems(arrayList);
            if (zIsPrimaryGroup && !listFilterVisibleItems.isEmpty()) {
                List list = this.mPrimaryMenuItems;
                list.addAll(list.size(), listFilterVisibleItems);
                this.mPrimaryMenuItems.add(new HyperMenuContract$HyperMenuDivider());
            }
        }
        if (!this.mPrimaryMenuItems.isEmpty()) {
            List list2 = this.mPrimaryMenuItems;
            list2.remove(list2.size() - 1);
        }
        handleDefaultCheckedStatus(this.mPrimaryMenuItems, true, -1);
    }

    private boolean isPrimaryGroup(ArrayList arrayList, int i) {
        MenuItemImpl menuItem;
        if (arrayList == null || arrayList.isEmpty() || (menuItem = ((HyperMenuContract$HyperMenuItem) arrayList.get(0)).getMenuItem()) == null) {
            return true;
        }
        Intent intent = menuItem.getIntent();
        if (intent == null || !intent.hasExtra("miuix:hyperMenu:groupForeignKey")) {
            return menuItem.getGroupId() == 0 || menuItem.getGroupId() != i;
        }
        return false;
    }

    private void assembleGroupData(Map map, ArrayList arrayList) {
        int iIntValue;
        int intExtra;
        this.mGroupIdOverrideMap.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            MenuItemImpl menuItemImpl = (MenuItemImpl) arrayList.get(i);
            int groupId = menuItemImpl.getGroupId();
            Intent intent = menuItemImpl.getIntent();
            CharSequence subtitle = menuItemImpl.getSubtitle();
            if (intent != null) {
                iIntValue = intent.getIntExtra("miuix:hyperMenu:groupId", -1);
                if (iIntValue != -1) {
                    this.mGroupIdOverrideMap.put(Integer.valueOf(groupId), Integer.valueOf(iIntValue));
                } else {
                    iIntValue = groupId;
                }
                intExtra = intent.getIntExtra("miuix:hyperMenu:itemForeignKey", -1);
                if (subtitle == null) {
                    subtitle = intent.getStringExtra("miuix:hyperMenu:itemSubtitle");
                }
            } else {
                iIntValue = groupId;
                intExtra = -1;
            }
            if (iIntValue == groupId && this.mGroupIdOverrideMap.containsKey(Integer.valueOf(groupId)) && this.mGroupIdOverrideMap.get(Integer.valueOf(groupId)) != null) {
                iIntValue = ((Integer) this.mGroupIdOverrideMap.get(Integer.valueOf(groupId))).intValue();
            }
            ArrayList arrayList2 = (ArrayList) map.get(Integer.valueOf(iIntValue));
            if (arrayList2 == null) {
                arrayList2 = new ArrayList();
            }
            HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem = new HyperMenuContract$HyperMenuTextItem(menuItemImpl);
            if (intExtra != -1) {
                hyperMenuContract$HyperMenuTextItem.isExpandable = true;
                hyperMenuContract$HyperMenuTextItem.itemForeignKey = intExtra;
            } else {
                hyperMenuContract$HyperMenuTextItem.isExpandable = false;
                hyperMenuContract$HyperMenuTextItem.itemForeignKey = -1;
            }
            hyperMenuContract$HyperMenuTextItem.setSubtitle(subtitle);
            if (intent != null) {
                hyperMenuContract$HyperMenuTextItem.showSubtitle = intent.getBooleanExtra("miuix:hyperMenu:itemShowSubtitle", true);
            }
            arrayList2.add(hyperMenuContract$HyperMenuTextItem);
            map.put(Integer.valueOf(iIntValue), arrayList2);
        }
        propagateGroupMultiSelect(map);
    }

    private void propagateGroupMultiSelect(Map map) {
        for (ArrayList arrayList : map.values()) {
            if (!arrayList.isEmpty()) {
                int i = 0;
                HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) arrayList.get(0);
                if ((hyperMenuContract$HyperMenuItem.getMenuItem() == null || hyperMenuContract$HyperMenuItem.getMenuItem().getIntent() == null) ? false : hyperMenuContract$HyperMenuItem.getMenuItem().getIntent().getBooleanExtra("miuix:hyperMenu:itemMultiSelect", false)) {
                    int size = arrayList.size();
                    while (i < size) {
                        Object obj = arrayList.get(i);
                        i++;
                        HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem2 = (HyperMenuContract$HyperMenuItem) obj;
                        if (hyperMenuContract$HyperMenuItem2 instanceof HyperMenuContract$HyperMenuTextItem) {
                            ((HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem2).isMultiSelect = true;
                        }
                    }
                }
            }
        }
    }

    private HyperMenuContract$HyperMenuItem findPrimaryItemByForeignKey(List list, int i) {
        int i2 = 0;
        while (true) {
            if (i2 >= list.size()) {
                return null;
            }
            HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) list.get(i2);
            Intent intent = hyperMenuContract$HyperMenuItem.getMenuItem() != null ? hyperMenuContract$HyperMenuItem.getMenuItem().getIntent() : null;
            int intExtra = intent != null ? intent.getIntExtra("miuix:hyperMenu:itemForeignKey", -1) : -1;
            if (intExtra != -1 && intExtra == i) {
                return hyperMenuContract$HyperMenuItem;
            }
            i2++;
        }
    }

    private void buildMenuItems(MenuBuilder menuBuilder) {
        List list;
        if (menuBuilder == null || this.mSecondaryMenuMap == null || (list = this.mPrimaryMenuItems) == null || this.mGroupedMap == null) {
            return;
        }
        list.clear();
        this.mSecondaryMenuMap.clear();
        this.mGroupedMap.clear();
        HyperMenuUtils.checkPrimaryItemVisibility(menuBuilder);
        ArrayList nonActionItems = this.mOverflowOnly ? menuBuilder.getNonActionItems() : menuBuilder.getVisibleItems();
        ArrayList arrayList = new ArrayList();
        if (nonActionItems != null) {
            buildDefaultSecondaryMenuData(this.mGroupedMap, nonActionItems, arrayList);
        }
        for (int i = 0; i < arrayList.size(); i++) {
            List listFilterVisibleItems = filterVisibleItems((ArrayList) this.mGroupedMap.get(arrayList.get(i)));
            if (!listFilterVisibleItems.isEmpty()) {
                List list2 = this.mPrimaryMenuItems;
                list2.addAll(list2.size(), listFilterVisibleItems);
                this.mPrimaryMenuItems.add(new HyperMenuContract$HyperMenuDivider());
            }
        }
        if (!this.mPrimaryMenuItems.isEmpty()) {
            List list3 = this.mPrimaryMenuItems;
            list3.remove(list3.size() - 1);
        }
        handleDefaultCheckedStatus(this.mPrimaryMenuItems, true, -1);
        removeInvalidatePrimaryItem(false);
    }

    private List filterVisibleItems(List list) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) it.next();
                if (hyperMenuContract$HyperMenuItem.getMenuItem() != null && hyperMenuContract$HyperMenuItem.getMenuItem().isVisible()) {
                    arrayList.add(hyperMenuContract$HyperMenuItem);
                }
            }
        }
        return arrayList;
    }

    private void removeInvalidatePrimaryItem(boolean z) {
        int i;
        if (this.mPrimaryMenuItems == null || this.mSecondaryMenuMap == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = this.mPrimaryMenuItems.iterator();
        while (true) {
            i = 0;
            z = false;
            boolean z2 = false;
            if (!it.hasNext()) {
                break;
            }
            HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) it.next();
            MenuItemImpl menuItem = hyperMenuContract$HyperMenuItem.getMenuItem();
            boolean z3 = hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem ? ((HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem).isExpandable : false;
            if (z) {
                z2 = z3;
            } else if (menuItem != null && menuItem.hasSubMenu()) {
                z2 = true;
            }
            if (menuItem != null && z2 && !this.mSecondaryMenuMap.containsKey(Integer.valueOf(menuItem.getItemId()))) {
                arrayList.add(hyperMenuContract$HyperMenuItem);
            }
        }
        int size = arrayList.size();
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            this.mPrimaryMenuItems.remove((HyperMenuContract$HyperMenuItem) obj);
        }
        arrayList.clear();
    }

    private void handleDefaultCheckedStatus(List list, boolean z, int i) {
        if (list == null || list.isEmpty()) {
            return;
        }
        boolean z2 = true;
        boolean z3 = (z || i == -1) ? false : true;
        Boolean[] boolArr = z3 ? (Boolean[]) this.mSecondaryItemCheckedMap.get(Integer.valueOf(i)) : null;
        if (z3 && boolArr == null) {
            boolArr = new Boolean[list.size()];
        } else {
            z2 = false;
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) list.get(i2);
            HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem = hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem ? (HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem : null;
            MenuItemImpl menuItem = hyperMenuContract$HyperMenuTextItem != null ? hyperMenuContract$HyperMenuTextItem.getMenuItem() : null;
            if (menuItem == null || !menuItem.isCheckable()) {
                if (z) {
                    int itemId = menuItem != null ? menuItem.getItemId() : i2;
                    if (!this.mSavePrimaryStatusByIdEnabled) {
                        itemId = i2;
                    }
                    this.mPrimaryItemCheckedMap.put(Integer.valueOf(itemId), Boolean.FALSE);
                }
            } else if (z) {
                int itemId2 = this.mSavePrimaryStatusByIdEnabled ? menuItem.getItemId() : i2;
                Boolean bool = (Boolean) this.mPrimaryItemCheckedMap.get(Integer.valueOf(itemId2));
                this.mPrimaryItemCheckedMap.put(Integer.valueOf(itemId2), Boolean.valueOf(bool != null ? bool.booleanValue() : menuItem.isChecked()));
                hyperMenuContract$HyperMenuTextItem.checkStatus = Boolean.TRUE.equals(this.mPrimaryItemCheckedMap.get(Integer.valueOf(itemId2))) ? HyperMenuContract$CheckableType.CHECKED : HyperMenuContract$CheckableType.NOT_CHECKED;
                HyperMenuUtils.setMenuItemChecked(menuItem, hyperMenuContract$HyperMenuTextItem.isChecked());
            } else if (z3) {
                if (z2) {
                    boolArr[i2] = Boolean.valueOf(menuItem.isChecked());
                }
                hyperMenuContract$HyperMenuTextItem.checkStatus = Boolean.TRUE.equals(boolArr[i2]) ? HyperMenuContract$CheckableType.CHECKED : HyperMenuContract$CheckableType.NOT_CHECKED;
            }
        }
        if (z3) {
            this.mSecondaryItemCheckedMap.put(Integer.valueOf(i), boolArr);
        }
    }

    private CharSequence getCheckedItemTitle(List list, int i, boolean z) {
        Boolean[] boolArr = (Boolean[]) this.mSecondaryItemCheckedMap.get(Integer.valueOf(i));
        if (boolArr == null || list == null) {
            return null;
        }
        if (z) {
            return HyperMenuUtils.joinCheckedItemTitles(list, boolArr);
        }
        return HyperMenuUtils.findFirstCheckedItemTitle(list, boolArr);
    }

    private void buildDefaultSecondaryMenuData(Map map, ArrayList arrayList, ArrayList arrayList2) {
        Intent intent;
        ArrayList arrayList3 = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            MenuItemImpl menuItemImpl = (MenuItemImpl) arrayList.get(i2);
            int groupId = menuItemImpl.getGroupId();
            if (!arrayList2.contains(Integer.valueOf(groupId))) {
                arrayList2.add(Integer.valueOf(groupId));
            }
            ArrayList arrayList4 = (ArrayList) map.get(Integer.valueOf(groupId));
            if (arrayList4 == null) {
                arrayList4 = new ArrayList();
            }
            boolean zHasSubMenu = menuItemImpl.hasSubMenu();
            HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem = new HyperMenuContract$HyperMenuTextItem(menuItemImpl);
            hyperMenuContract$HyperMenuTextItem.isExpandable = zHasSubMenu;
            CharSequence subtitle = menuItemImpl.getSubtitle();
            if (subtitle == null && (intent = menuItemImpl.getIntent()) != null) {
                subtitle = intent.getStringExtra("miuix:hyperMenu:itemSubtitle");
            }
            hyperMenuContract$HyperMenuTextItem.setSubtitle(subtitle);
            Intent intent2 = menuItemImpl.getIntent();
            if (intent2 != null) {
                hyperMenuContract$HyperMenuTextItem.showSubtitle = intent2.getBooleanExtra("miuix:hyperMenu:itemShowSubtitle", true);
            }
            arrayList4.add(hyperMenuContract$HyperMenuTextItem);
            map.put(Integer.valueOf(groupId), arrayList4);
            ArrayList arrayListBuildDefaultSubMenuData = (zHasSubMenu && (menuItemImpl.getSubMenu() instanceof SubMenuBuilder)) ? buildDefaultSubMenuData((SubMenuBuilder) menuItemImpl.getSubMenu(), menuItemImpl.getItemId()) : null;
            if (zHasSubMenu && menuItemImpl.isVisible() != HyperMenuUtils.hasAnyVisibleSubMenuItem(arrayListBuildDefaultSubMenuData)) {
                arrayList3.add(menuItemImpl);
            }
            if (arrayListBuildDefaultSubMenuData != null) {
                if (hyperMenuContract$HyperMenuTextItem.showSubtitle) {
                    Intent intent3 = menuItemImpl.getIntent();
                    CharSequence checkedItemTitle = getCheckedItemTitle(arrayListBuildDefaultSubMenuData, menuItemImpl.getItemId(), intent3 != null ? intent3.getBooleanExtra("miuix:hyperMenu:itemMultiSelect", false) : false);
                    if (checkedItemTitle != null) {
                        hyperMenuContract$HyperMenuTextItem.setSubtitle(checkedItemTitle);
                        menuItemImpl.setSubtitle(checkedItemTitle);
                    }
                }
                HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem2 = new HyperMenuContract$HyperMenuTextItem(menuItemImpl);
                hyperMenuContract$HyperMenuTextItem2.isHeaderItem = true;
                hyperMenuContract$HyperMenuTextItem2.setSubtitle(hyperMenuContract$HyperMenuTextItem.getSubtitle());
                hyperMenuContract$HyperMenuTextItem2.showSubtitle = hyperMenuContract$HyperMenuTextItem.showSubtitle;
                arrayListBuildDefaultSubMenuData.add(0, hyperMenuContract$HyperMenuTextItem2);
                arrayListBuildDefaultSubMenuData.add(1, new HyperMenuContract$HyperMenuDivider());
                HyperSecondaryAdapter hyperSecondaryAdapter = new HyperSecondaryAdapter(this.mInflater, arrayListBuildDefaultSubMenuData, this.mSecondaryItemCheckedMap);
                hyperSecondaryAdapter.setOptionalIconsVisible(getOptionalIconsVisible());
                Intent intent4 = menuItemImpl.getIntent();
                hyperSecondaryAdapter.setMultiSelectEnabled(intent4 != null ? intent4.getBooleanExtra("miuix:hyperMenu:itemMultiSelect", false) : false);
                this.mSecondaryMenuMap.put(Integer.valueOf(menuItemImpl.getItemId()), hyperSecondaryAdapter);
            }
        }
        int size = arrayList3.size();
        while (i < size) {
            Object obj = arrayList3.get(i);
            i++;
            MenuItemImpl menuItemImpl2 = (MenuItemImpl) obj;
            menuItemImpl2.setVisible(!menuItemImpl2.isVisible());
        }
        arrayList3.clear();
    }

    /* JADX WARN: Code duplicated, block: B:22:0x0048  */
    /* JADX WARN: Code duplicated, block: B:25:0x0051 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:26:0x0053  */
    /* JADX WARN: Code duplicated, block: B:34:0x007d  */
    /* JADX WARN: Code duplicated, block: B:37:0x008b  */
    /* JADX WARN: Code duplicated, block: B:39:0x0095  */
    /* JADX WARN: Code duplicated, block: B:40:0x0098  */
    /* JADX WARN: Code duplicated, block: B:49:0x00a6 A[SYNTHETIC] */
    private ArrayList buildDefaultSubMenuData(SubMenuBuilder subMenuBuilder, int i) {
        boolean z;
        MenuItemImpl menuItemImpl;
        HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem;
        CharSequence subtitle;
        Intent intent;
        HyperMenuContract$CheckableType hyperMenuContract$CheckableType;
        Intent intent2;
        if (subMenuBuilder != null && i != -1) {
            ArrayList arrayList = new ArrayList();
            ArrayList visibleItems = subMenuBuilder.getVisibleItems();
            if (visibleItems != null && visibleItems.size() != 0) {
                Boolean[] boolArr = (Boolean[]) this.mSecondaryItemCheckedMap.get(Integer.valueOf(i));
                if (boolArr == null) {
                    boolArr = new Boolean[visibleItems.size()];
                } else {
                    if (boolArr.length != visibleItems.size()) {
                        boolArr = new Boolean[visibleItems.size()];
                    } else {
                        z = false;
                    }
                    for (int i2 = 0; i2 < visibleItems.size(); i2++) {
                        menuItemImpl = (MenuItemImpl) visibleItems.get(i2);
                        if (menuItemImpl != null) {
                            if (z) {
                                boolArr[i2] = Boolean.valueOf(menuItemImpl.isChecked());
                            }
                            hyperMenuContract$HyperMenuTextItem = new HyperMenuContract$HyperMenuTextItem(menuItemImpl);
                            subtitle = menuItemImpl.getSubtitle();
                            if (subtitle == null && (intent2 = menuItemImpl.getIntent()) != null) {
                                subtitle = intent2.getStringExtra("miuix:hyperMenu:itemSubtitle");
                            }
                            hyperMenuContract$HyperMenuTextItem.setSubtitle(subtitle);
                            intent = menuItemImpl.getIntent();
                            if (intent != null) {
                                hyperMenuContract$HyperMenuTextItem.showSubtitle = intent.getBooleanExtra("miuix:hyperMenu:itemShowSubtitle", true);
                            }
                            if (menuItemImpl.isCheckable()) {
                                if (Boolean.TRUE.equals(boolArr[i2])) {
                                    hyperMenuContract$CheckableType = HyperMenuContract$CheckableType.CHECKED;
                                } else {
                                    hyperMenuContract$CheckableType = HyperMenuContract$CheckableType.NOT_CHECKED;
                                }
                                hyperMenuContract$HyperMenuTextItem.checkStatus = hyperMenuContract$CheckableType;
                                HyperMenuUtils.setMenuItemChecked(menuItemImpl, hyperMenuContract$HyperMenuTextItem.isChecked());
                            }
                            arrayList.add(hyperMenuContract$HyperMenuTextItem);
                        }
                    }
                    this.mSecondaryItemCheckedMap.put(Integer.valueOf(i), boolArr);
                    return arrayList;
                }
                z = true;
                while (i2 < visibleItems.size()) {
                    menuItemImpl = (MenuItemImpl) visibleItems.get(i2);
                    if (menuItemImpl != null) {
                        if (z) {
                            boolArr[i2] = Boolean.valueOf(menuItemImpl.isChecked());
                        }
                        hyperMenuContract$HyperMenuTextItem = new HyperMenuContract$HyperMenuTextItem(menuItemImpl);
                        subtitle = menuItemImpl.getSubtitle();
                        if (subtitle == null) {
                            subtitle = intent2.getStringExtra("miuix:hyperMenu:itemSubtitle");
                        }
                        hyperMenuContract$HyperMenuTextItem.setSubtitle(subtitle);
                        intent = menuItemImpl.getIntent();
                        if (intent != null) {
                            hyperMenuContract$HyperMenuTextItem.showSubtitle = intent.getBooleanExtra("miuix:hyperMenu:itemShowSubtitle", true);
                        }
                        if (menuItemImpl.isCheckable()) {
                            if (Boolean.TRUE.equals(boolArr[i2])) {
                                hyperMenuContract$CheckableType = HyperMenuContract$CheckableType.CHECKED;
                            } else {
                                hyperMenuContract$CheckableType = HyperMenuContract$CheckableType.NOT_CHECKED;
                            }
                            hyperMenuContract$HyperMenuTextItem.checkStatus = hyperMenuContract$CheckableType;
                            HyperMenuUtils.setMenuItemChecked(menuItemImpl, hyperMenuContract$HyperMenuTextItem.isChecked());
                        }
                        arrayList.add(hyperMenuContract$HyperMenuTextItem);
                    }
                }
                this.mSecondaryItemCheckedMap.put(Integer.valueOf(i), boolArr);
                return arrayList;
            }
        }
        return null;
    }

    public void resumePrimaryItemClickStatus(int i, int i2) {
        HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem;
        MenuItemImpl menuItem;
        int groupId;
        List list = this.mPrimaryMenuItems;
        if (list == null || list.size() == 0) {
            return;
        }
        boolean z = false;
        int i3 = 0;
        while (true) {
            if (i3 >= this.mPrimaryMenuItems.size()) {
                hyperMenuContract$HyperMenuItem = null;
                break;
            }
            hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) this.mPrimaryMenuItems.get(i3);
            if (hyperMenuContract$HyperMenuItem.getItemId() == i) {
                break;
            } else {
                i3++;
            }
        }
        if (hyperMenuContract$HyperMenuItem == null || (menuItem = hyperMenuContract$HyperMenuItem.getMenuItem()) == null) {
            return;
        }
        boolean z2 = hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem;
        if (z2 ? ((HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem).isMultiSelect : false) {
            if (menuItem.isCheckable() && z2) {
                HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem = (HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem;
                if (!hyperMenuContract$HyperMenuTextItem.isExpandable) {
                    if (!this.mSavePrimaryStatusByIdEnabled) {
                        i = i2;
                    }
                    Boolean bool = (Boolean) this.mPrimaryItemCheckedMap.get(Integer.valueOf(i));
                    if (bool == null ? !menuItem.isChecked() : !bool.booleanValue()) {
                        z = true;
                    }
                    this.mPrimaryItemCheckedMap.put(Integer.valueOf(i), Boolean.valueOf(z));
                    hyperMenuContract$HyperMenuTextItem.checkStatus = z ? HyperMenuContract$CheckableType.CHECKED : HyperMenuContract$CheckableType.NOT_CHECKED;
                    HyperMenuUtils.setMenuItemChecked(menuItem, z);
                }
            }
            notifyDataSetChanged();
            return;
        }
        Intent intent = menuItem.getIntent();
        if (intent != null) {
            groupId = intent.getIntExtra("miuix:hyperMenu:groupId", menuItem.getGroupId());
        } else {
            groupId = menuItem.getGroupId();
        }
        if (this.mGroupIdOverrideMap.containsKey(Integer.valueOf(groupId)) && this.mGroupIdOverrideMap.get(Integer.valueOf(groupId)) != null) {
            groupId = ((Integer) this.mGroupIdOverrideMap.get(Integer.valueOf(groupId))).intValue();
        }
        ArrayList arrayList = (ArrayList) this.mGroupedMap.get(Integer.valueOf(groupId));
        int groupIndexAndUpdateStatus = arrayList != null ? HyperMenuUtils.getGroupIndexAndUpdateStatus(arrayList, this.mPrimaryItemCheckedMap, i, this.mSavePrimaryStatusByIdEnabled) : -1;
        if (groupIndexAndUpdateStatus != -1 && arrayList != null && !this.mSavePrimaryStatusByIdEnabled) {
            HyperMenuUtils.updatePrimaryGroupStatusByPosition(this.mPrimaryItemCheckedMap, i2, groupIndexAndUpdateStatus, arrayList.size());
        }
        notifyDataSetChanged();
    }

    public void setSavePrimaryStatusByIdEnabled(boolean z) {
        this.mSavePrimaryStatusByIdEnabled = z;
    }

    public void preCheckPrimaryItem(Map map) {
        if (map == null) {
            return;
        }
        this.mUserPreCheckedPrimaryMap = map;
        this.mPrimaryItemCheckedMap.clear();
        for (Integer num : map.keySet()) {
            num.intValue();
            this.mPrimaryItemCheckedMap.put(num, (Boolean) map.get(num));
        }
        this.mPrimaryCheckedMapFirstModified = true;
    }

    public void preCheckSecondaryItem(Map map) {
        if (map == null) {
            return;
        }
        this.mUserPreCheckedSecondaryMap = map;
        this.mSecondaryItemCheckedMap.clear();
        for (Integer num : map.keySet()) {
            num.intValue();
            this.mSecondaryItemCheckedMap.put(num, (Boolean[]) map.get(num));
        }
        this.mSecondaryCheckedMapFirstModified = true;
    }

    public void copyPrimaryCheckedData(Map map) {
        Map map2;
        if (map == null || (map2 = this.mPrimaryItemCheckedMap) == null) {
            return;
        }
        for (Integer num : map2.keySet()) {
            num.intValue();
            Boolean bool = (Boolean) this.mPrimaryItemCheckedMap.get(num);
            if (bool != null) {
                map.put(num, bool);
            }
        }
    }

    public void copySecondaryCheckedData(Map map) {
        Map map2;
        if (map == null || (map2 = this.mSecondaryItemCheckedMap) == null) {
            return;
        }
        for (Integer num : map2.keySet()) {
            num.intValue();
            Boolean[] boolArr = (Boolean[]) this.mSecondaryItemCheckedMap.get(num);
            if (boolArr != null && boolArr.length > 0) {
                Boolean[] boolArr2 = new Boolean[boolArr.length];
                System.arraycopy(boolArr, 0, boolArr2, 0, boolArr.length);
                map.put(num, boolArr2);
            }
        }
    }

    @Override // miuix.appcompat.view.menu.HyperBaseAdapter, android.widget.Adapter
    public MenuItem getItem(int i) {
        return ((HyperMenuContract$HyperMenuItem) this.mPrimaryMenuItems.get(i)).getMenuItem();
    }

    @Override // miuix.appcompat.view.menu.HyperBaseAdapter
    public HyperMenuContract$HyperMenuItem getHyperMenuItem(int i) {
        return (HyperMenuContract$HyperMenuItem) this.mPrimaryMenuItems.get(i);
    }

    @Override // miuix.appcompat.view.menu.HyperBaseAdapter, android.widget.Adapter
    public long getItemId(int i) {
        return ((HyperMenuContract$HyperMenuItem) this.mPrimaryMenuItems.get(i)).getItemId();
    }

    public BaseAdapter getSecondaryAdapterByItemId(long j) {
        return (BaseAdapter) this.mSecondaryMenuMap.get(Integer.valueOf((int) j));
    }

    public boolean hasSubMenu() {
        return !this.mSecondaryMenuMap.isEmpty();
    }

    public void update(MenuBuilder menuBuilder) {
        update(menuBuilder, false);
    }

    public void update(MenuBuilder menuBuilder, boolean z) {
        Map map = this.mUserPreCheckedPrimaryMap;
        if (map != null && this.mPrimaryItemCheckedMap != null && !this.mPrimaryCheckedMapFirstModified) {
            preCheckPrimaryItem(map);
        }
        Map map2 = this.mUserPreCheckedSecondaryMap;
        if (map2 != null && this.mSecondaryItemCheckedMap != null && !this.mSecondaryCheckedMapFirstModified) {
            preCheckSecondaryItem(map2);
        }
        if (z) {
            buildGroupedMenuItems(menuBuilder);
        } else {
            buildMenuItems(menuBuilder);
        }
        this.mPrimaryCheckedMapFirstModified = false;
        this.mSecondaryCheckedMapFirstModified = false;
        notifyDataSetChanged();
    }
}
