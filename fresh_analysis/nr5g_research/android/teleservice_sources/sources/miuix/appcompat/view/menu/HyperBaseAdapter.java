package miuix.appcompat.view.menu;

import android.R;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.List;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$string;
import miuix.appcompat.internal.view.menu.MenuItemImpl;
import miuix.internal.util.AnimHelper;

/* JADX INFO: loaded from: classes2.dex */
public abstract class HyperBaseAdapter extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected List mMenuItemList;
    private boolean mOptionalIconsVisible = true;
    protected boolean mMultiSelectEnabled = false;
    private boolean mHeightAnimating = false;

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 3;
    }

    public HyperBaseAdapter() {
    }

    public HyperBaseAdapter(LayoutInflater layoutInflater, List list) {
        this.mInflater = layoutInflater;
        this.mMenuItemList = list;
    }

    public HyperMenuContract$HyperMenuItem getHyperMenuItem(int i) {
        return (HyperMenuContract$HyperMenuItem) this.mMenuItemList.get(i);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mMenuItemList.size();
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return ((HyperMenuContract$HyperMenuItem) this.mMenuItemList.get(i)).getItemId();
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i) {
        HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) this.mMenuItemList.get(i);
        if (hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuDivider) {
            return 1;
        }
        return (hyperMenuContract$HyperMenuItem == null || !HyperMenuUtils.isGroupTitleItem(hyperMenuContract$HyperMenuItem.getMenuItem())) ? 0 : 2;
    }

    @Override // android.widget.Adapter
    public MenuItem getItem(int i) {
        return ((HyperMenuContract$HyperMenuItem) this.mMenuItemList.get(i)).getMenuItem();
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == 0) {
            return handleMenuItem(i, view, viewGroup);
        }
        if (itemViewType == 1) {
            return handleDivider(view, viewGroup);
        }
        if (itemViewType == 2) {
            return handleGroupTitleItem(i, view, viewGroup);
        }
        return null;
    }

    private View handleGroupTitleItem(int i, View view, ViewGroup viewGroup) {
        GroupTitleItemViewHolder groupTitleItemViewHolder;
        HyperMenuContract$HyperMenuItem hyperMenuContract$HyperMenuItem = (HyperMenuContract$HyperMenuItem) this.mMenuItemList.get(i);
        HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem = hyperMenuContract$HyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem ? (HyperMenuContract$HyperMenuTextItem) hyperMenuContract$HyperMenuItem : null;
        MenuItemImpl menuItem = hyperMenuContract$HyperMenuTextItem != null ? hyperMenuContract$HyperMenuTextItem.getMenuItem() : null;
        if (view == null || view.getTag().getClass() != GroupTitleItemViewHolder.class) {
            GroupTitleItemViewHolder groupTitleItemViewHolder2 = new GroupTitleItemViewHolder();
            view = this.mInflater.inflate(R$layout.miuix_appcompat_hyper_popup_menu_group_title_item, viewGroup, false);
            groupTitleItemViewHolder2.titleView = (TextView) view.findViewById(R.id.text1);
            view.setTag(groupTitleItemViewHolder2);
            groupTitleItemViewHolder = groupTitleItemViewHolder2;
        } else {
            groupTitleItemViewHolder = (GroupTitleItemViewHolder) view.getTag();
        }
        TextView textView = groupTitleItemViewHolder.titleView;
        if (textView != null && menuItem != null) {
            textView.setText(menuItem.getTitle());
        }
        return view;
    }

    private View handleMenuItem(int i, View view, ViewGroup viewGroup) {
        MenuItemViewHolder menuItemViewHolderInflateMenuItemView;
        HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem = (HyperMenuContract$HyperMenuTextItem) this.mMenuItemList.get(i);
        if (view == null || view.getTag().getClass() != MenuItemViewHolder.class) {
            view = this.mInflater.inflate(R$layout.miuix_appcompat_hyper_popup_menu_item, viewGroup, false);
            menuItemViewHolderInflateMenuItemView = inflateMenuItemView(view);
        } else {
            menuItemViewHolderInflateMenuItemView = (MenuItemViewHolder) view.getTag();
        }
        if (hyperMenuContract$HyperMenuTextItem != null) {
            MenuItemImpl menuItem = hyperMenuContract$HyperMenuTextItem.getMenuItem();
            if (menuItem != null) {
                view.setEnabled(menuItem.isEnabled());
                menuItemViewHolderInflateMenuItemView.titleView.setText(menuItem.getTitle());
                menuItemViewHolderInflateMenuItemView.titleView.setEnabled(menuItem.isEnabled());
            }
            menuItemViewHolderInflateMenuItemView.titleView.setSelected(hyperMenuContract$HyperMenuTextItem.isChecked());
            bindSubtitle(menuItemViewHolderInflateMenuItemView, hyperMenuContract$HyperMenuTextItem);
            bindCheckMark(menuItemViewHolderInflateMenuItemView, hyperMenuContract$HyperMenuTextItem);
            bindIcon(menuItemViewHolderInflateMenuItemView, hyperMenuContract$HyperMenuTextItem);
            menuItemViewHolderInflateMenuItemView.arrow.setVisibility((hyperMenuContract$HyperMenuTextItem.isExpandable || hyperMenuContract$HyperMenuTextItem.isHeaderItem) ? 0 : 8);
            bindAccessibility(view, hyperMenuContract$HyperMenuTextItem);
        }
        return view;
    }

    private MenuItemViewHolder inflateMenuItemView(View view) {
        MenuItemViewHolder menuItemViewHolder = new MenuItemViewHolder();
        menuItemViewHolder.titleView = (TextView) view.findViewById(R.id.text1);
        menuItemViewHolder.subtitleView = (TextView) view.findViewById(R.id.text2);
        menuItemViewHolder.iconView = (ImageView) view.findViewById(R.id.icon);
        menuItemViewHolder.checkMark = (ImageView) view.findViewById(R$id.check_mark);
        menuItemViewHolder.arrow = (ImageView) view.findViewById(R$id.arrow);
        view.setTag(menuItemViewHolder);
        AnimHelper.addItemPressEffect(view, true);
        return menuItemViewHolder;
    }

    private void bindSubtitle(MenuItemViewHolder menuItemViewHolder, HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem) {
        if (!hyperMenuContract$HyperMenuTextItem.showSubtitle) {
            menuItemViewHolder.subtitleView.setVisibility(8);
            return;
        }
        if (this.mHeightAnimating && (hyperMenuContract$HyperMenuTextItem.isHeaderItem || hyperMenuContract$HyperMenuTextItem.isExpandable)) {
            return;
        }
        CharSequence subtitle = hyperMenuContract$HyperMenuTextItem.getSubtitle();
        if (!TextUtils.isEmpty(subtitle)) {
            menuItemViewHolder.subtitleView.setText(subtitle);
            menuItemViewHolder.subtitleView.setVisibility(0);
            if (hyperMenuContract$HyperMenuTextItem.getMenuItem() != null) {
                menuItemViewHolder.subtitleView.setEnabled(hyperMenuContract$HyperMenuTextItem.getMenuItem().isEnabled());
            }
            menuItemViewHolder.subtitleView.setSelected(hyperMenuContract$HyperMenuTextItem.isChecked());
            return;
        }
        menuItemViewHolder.subtitleView.setVisibility(8);
    }

    private void bindCheckMark(MenuItemViewHolder menuItemViewHolder, HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem) {
        if (!HyperMenuContract$CheckableType.NON_SUPPORT.equals(hyperMenuContract$HyperMenuTextItem.checkStatus)) {
            menuItemViewHolder.checkMark.setVisibility(hyperMenuContract$HyperMenuTextItem.isChecked() ? 0 : 4);
        } else {
            menuItemViewHolder.checkMark.setVisibility(8);
        }
    }

    private void bindIcon(MenuItemViewHolder menuItemViewHolder, HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem) {
        if (this.mOptionalIconsVisible && hyperMenuContract$HyperMenuTextItem.getMenuItem() != null && hyperMenuContract$HyperMenuTextItem.getMenuItem().getIcon() != null) {
            menuItemViewHolder.iconView.setSelected(hyperMenuContract$HyperMenuTextItem.getMenuItem().isChecked());
            menuItemViewHolder.iconView.setImageDrawable(hyperMenuContract$HyperMenuTextItem.getMenuItem().getIcon());
            menuItemViewHolder.iconView.setEnabled(hyperMenuContract$HyperMenuTextItem.getMenuItem().isEnabled());
            menuItemViewHolder.iconView.setVisibility(0);
            return;
        }
        menuItemViewHolder.iconView.setVisibility(8);
    }

    private void bindAccessibility(View view, HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem) {
        if (HyperMenuContract$CheckableType.NON_SUPPORT.equals(hyperMenuContract$HyperMenuTextItem.checkStatus)) {
            setAccessibilityDelegateNonCheckable(view, hyperMenuContract$HyperMenuTextItem);
        } else {
            setAccessibilityDelegateCheckable(view, hyperMenuContract$HyperMenuTextItem);
        }
    }

    private void setAccessibilityDelegateCheckable(View view, final HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem) {
        ViewCompat.setAccessibilityDelegate(view, new AccessibilityDelegateCompat() { // from class: miuix.appcompat.view.menu.HyperBaseAdapter.1
            @Override // androidx.core.view.AccessibilityDelegateCompat
            public void onInitializeAccessibilityNodeInfo(View view2, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view2, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCheckable(true);
                accessibilityNodeInfoCompat.setChecked(hyperMenuContract$HyperMenuTextItem.isChecked());
                if (hyperMenuContract$HyperMenuTextItem.getMenuItem() != null) {
                    CharSequence title = hyperMenuContract$HyperMenuTextItem.getMenuItem().getTitle();
                    if (!TextUtils.isEmpty(title)) {
                        accessibilityNodeInfoCompat.setContentDescription(title);
                    }
                }
                if (hyperMenuContract$HyperMenuTextItem.isChecked()) {
                    return;
                }
                accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
            }
        });
    }

    private void setAccessibilityDelegateNonCheckable(View view, final HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem) {
        ViewCompat.setAccessibilityDelegate(view, new AccessibilityDelegateCompat() { // from class: miuix.appcompat.view.menu.HyperBaseAdapter.2
            @Override // androidx.core.view.AccessibilityDelegateCompat
            public void onInitializeAccessibilityNodeInfo(View view2, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view2, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setClickable(true);
                HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem2 = hyperMenuContract$HyperMenuTextItem;
                if (hyperMenuContract$HyperMenuTextItem2 != null) {
                    String string = hyperMenuContract$HyperMenuTextItem2.getMenuItem() != null ? hyperMenuContract$HyperMenuTextItem.getMenuItem().getTitle().toString() : null;
                    if (!TextUtils.isEmpty(string)) {
                        accessibilityNodeInfoCompat.setContentDescription(string);
                    }
                    HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem3 = hyperMenuContract$HyperMenuTextItem;
                    if (hyperMenuContract$HyperMenuTextItem3.isExpandable && !hyperMenuContract$HyperMenuTextItem3.isHeaderItem) {
                        accessibilityNodeInfoCompat.setStateDescription(view2.getContext().getResources().getString(R$string.miuix_appcompat_accessibility_collapse_state));
                    } else if (hyperMenuContract$HyperMenuTextItem3.isHeaderItem) {
                        accessibilityNodeInfoCompat.setStateDescription(view2.getContext().getResources().getString(R$string.miuix_appcompat_accessibility_expand_state));
                    }
                }
            }
        });
    }

    public void setHeightAnimating(boolean z) {
        this.mHeightAnimating = z;
    }

    private View handleDivider(View view, ViewGroup viewGroup) {
        if (view != null && view.getTag().getClass() == MenuDividerHolder.class) {
            return view;
        }
        MenuDividerHolder menuDividerHolder = new MenuDividerHolder();
        View viewInflate = this.mInflater.inflate(R$layout.miuix_appcompat_popup_menu_divider, viewGroup, false);
        viewInflate.setTag(menuDividerHolder);
        return viewInflate;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int i) {
        return getItemViewType(i) == 0;
    }

    public void setOptionalIconsVisible(boolean z) {
        this.mOptionalIconsVisible = z;
    }

    public boolean getOptionalIconsVisible() {
        return this.mOptionalIconsVisible;
    }

    public void setMultiSelectEnabled(boolean z) {
        this.mMultiSelectEnabled = z;
    }

    public boolean isMultiSelectEnabled() {
        return this.mMultiSelectEnabled;
    }

    class MenuItemViewHolder {
        ImageView arrow;
        ImageView checkMark;
        ImageView iconView;
        TextView subtitleView;
        TextView titleView;

        MenuItemViewHolder() {
        }
    }

    class GroupTitleItemViewHolder {
        TextView titleView;

        GroupTitleItemViewHolder() {
        }
    }

    class MenuDividerHolder {
        MenuDividerHolder() {
        }
    }
}
