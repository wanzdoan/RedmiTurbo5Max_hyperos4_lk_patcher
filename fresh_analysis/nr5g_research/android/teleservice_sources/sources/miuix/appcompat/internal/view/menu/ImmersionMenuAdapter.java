package miuix.appcompat.internal.view.menu;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import miuix.appcompat.R$layout;
import miuix.internal.util.AnimHelper;
import miuix.internal.util.TaggingDrawableUtil;

/* JADX INFO: loaded from: classes2.dex */
public class ImmersionMenuAdapter extends BaseAdapter {
    private ArrayList mAvailableItems;
    private Context mContext;
    private LayoutInflater mInflater;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    protected ImmersionMenuAdapter(Context context, Menu menu) {
        this.mInflater = LayoutInflater.from(context);
        ArrayList arrayList = new ArrayList();
        this.mAvailableItems = arrayList;
        buildVisibleItems(menu, arrayList);
        this.mContext = context;
    }

    private void buildVisibleItems(Menu menu, ArrayList arrayList) {
        arrayList.clear();
        if (menu != null) {
            int size = menu.size();
            for (int i = 0; i < size; i++) {
                MenuItem item = menu.getItem(i);
                if (checkMenuItem(item)) {
                    arrayList.add(item);
                }
            }
        }
    }

    protected boolean checkMenuItem(MenuItem menuItem) {
        return menuItem.isVisible();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mAvailableItems.size();
    }

    @Override // android.widget.Adapter
    public MenuItem getItem(int i) {
        return (MenuItem) this.mAvailableItems.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mInflater.inflate(R$layout.miuix_appcompat_immersion_popup_menu_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.title = (TextView) view.findViewById(R.id.text1);
            view.setTag(viewHolder);
            AnimHelper.addItemPressEffect(view, true);
        }
        TaggingDrawableUtil.updateItemPadding(view, i, getCount());
        Object tag = view.getTag();
        if (tag != null) {
            ViewHolder viewHolder2 = (ViewHolder) tag;
            MenuItem item = getItem(i);
            Drawable icon = item.getIcon();
            ImageView imageView = viewHolder2.icon;
            if (icon != null) {
                imageView.setImageDrawable(item.getIcon());
                viewHolder2.icon.setVisibility(0);
            } else {
                imageView.setVisibility(8);
            }
            viewHolder2.title.setText(item.getTitle());
        }
        return view;
    }

    public void update(Menu menu) {
        buildVisibleItems(menu, this.mAvailableItems);
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView icon;
        TextView title;

        private ViewHolder() {
        }
    }
}
