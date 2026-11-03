package com.android.phone.settings;

import android.content.DialogInterface;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.phone.R;
import com.android.phone.utils.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$drawable;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;
import miuix.internal.util.AttributeResolver;
import miuix.view.EditActionMode;
import org.apache.miui.commons.lang3.builder.CompareToBuilder;

/* JADX INFO: loaded from: classes.dex */
public abstract class RecyclerActivity extends AppCompatActivity {
    protected EditableRecyclerView.EditableAdapter mAdapter;
    protected TextView mEmptyText;
    protected EditableRecyclerView mList;
    private ModeCallback mModeCallBack;
    protected HashMap mNumbers = new HashMap();

    protected abstract void saveNumbers();

    protected void setSummary() {
    }

    protected void updateAdapterData() {
        ArrayList arrayList = new ArrayList();
        for (Pair pair : this.mNumbers.values()) {
            HashMap map = new HashMap();
            map.put("number", Utils.localizeNumber((String) pair.first));
            map.put("name", (String) pair.second);
            arrayList.add(map);
        }
        this.mAdapter = new EditableRecyclerView.EditableAdapter(this, this, arrayList, R.layout.auto_record_whitelist_setting_list_item, new String[]{"name", "number"}, new int[]{R.id.name, R.id.number}) { // from class: com.android.phone.settings.RecyclerActivity.1
        };
        ModeCallback modeCallback = new ModeCallback();
        this.mModeCallBack = modeCallback;
        this.mAdapter.setMultiChoiceModeListener(modeCallback);
        this.mList.setAdapter(this.mAdapter);
        this.mAdapter.updateGroupInfo();
        this.mEmptyText.setVisibility(this.mAdapter.getItemCount() > 0 ? 8 : 0);
    }

    public class ModeCallback implements EditableRecyclerView.MultiChoiceModeListener {
        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return true;
        }

        protected ModeCallback() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            RecyclerActivity.this.mAdapter.enterEditMode();
            menu.add(0, 2, 0, R.string.remove).setIcon(AttributeResolver.resolveDrawable(RecyclerActivity.this, R$attr.actionBarDeleteIcon)).setShowAsAction(1);
            EditActionMode editActionMode = (EditActionMode) actionMode;
            editActionMode.setButton(android.R.id.button1, null, Utils.isNightMode(RecyclerActivity.this.getApplicationContext()) ? R$drawable.miuix_appcompat_action_mode_title_button_cancel_dark : R$drawable.miuix_appcompat_action_mode_title_button_cancel_light);
            editActionMode.setButton(android.R.id.button2, null, Utils.isNightMode(RecyclerActivity.this.getApplicationContext()) ? R$drawable.miuix_appcompat_action_mode_title_button_select_all_dark : R$drawable.miuix_appcompat_action_mode_title_button_select_all_light);
            return true;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int i;
            if (menuItem.getItemId() == 2) {
                delete(actionMode);
            } else if (menuItem.getItemId() == 16908313) {
                actionMode.finish();
            } else if (menuItem.getItemId() == 16908314) {
                EditableRecyclerView.EditableAdapter editableAdapter = RecyclerActivity.this.mAdapter;
                editableAdapter.setAllCheck(!editableAdapter.isAllChecked());
                RecyclerActivity recyclerActivity = RecyclerActivity.this;
                recyclerActivity.mAdapter.updateActionMenu(recyclerActivity.getApplicationContext(), actionMode);
                EditActionMode editActionMode = (EditActionMode) actionMode;
                boolean zIsAllChecked = RecyclerActivity.this.mAdapter.isAllChecked();
                RecyclerActivity recyclerActivity2 = RecyclerActivity.this;
                if (!zIsAllChecked) {
                    i = Utils.isNightMode(recyclerActivity2.getApplicationContext()) ? R$drawable.miuix_appcompat_action_mode_title_button_select_all_dark : R$drawable.miuix_appcompat_action_mode_title_button_select_all_light;
                } else {
                    i = Utils.isNightMode(recyclerActivity2.getApplicationContext()) ? R$drawable.miuix_appcompat_action_mode_title_button_deselect_all_dark : R$drawable.miuix_appcompat_action_mode_title_button_deselect_all_light;
                }
                editActionMode.setButton(android.R.id.button2, null, i);
            }
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            RecyclerActivity.this.mAdapter.exitEditMode();
        }

        protected void delete(final ActionMode actionMode) {
            final SparseBooleanArray checkedItemPositions = RecyclerActivity.this.mAdapter.getCheckedItemPositions();
            if (checkedItemPositions == null || checkedItemPositions.size() <= 0) {
                return;
            }
            new AlertDialog.Builder(RecyclerActivity.this).setTitle(R.string.remove).setIconAttribute(android.R.attr.alertDialogIcon).setMessage(RecyclerActivity.this.getResources().getQuantityString(R.plurals.call_remove_exception_numbers_confirm, checkedItemPositions.size(), Integer.valueOf(checkedItemPositions.size()))).setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.RecyclerActivity.ModeCallback.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    Iterator it = RecyclerActivity.this.mNumbers.entrySet().iterator();
                    long j = -1;
                    for (int i2 = 0; i2 < checkedItemPositions.size(); i2++) {
                        while (it.hasNext()) {
                            it.next();
                            j++;
                            if (j == checkedItemPositions.keyAt(i2)) {
                                it.remove();
                                break;
                            }
                        }
                    }
                    RecyclerActivity.this.saveNumbers();
                    RecyclerActivity.this.setSummary();
                    RecyclerActivity.this.updateAdapterData();
                    actionMode.finish();
                }
            }).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).show();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.android.phone.settings.EditableRecyclerView.MultiChoiceModeListener
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, boolean z) {
            int i2;
            int i3;
            RecyclerActivity recyclerActivity = RecyclerActivity.this;
            recyclerActivity.mAdapter.updateActionMenu(recyclerActivity.getApplicationContext(), actionMode);
            if (RecyclerActivity.this.mAdapter.getCheckedItemCount() == RecyclerActivity.this.mAdapter.getItemCount()) {
                EditActionMode editActionMode = (EditActionMode) actionMode;
                if (Utils.isNightMode(RecyclerActivity.this.getApplicationContext())) {
                    i3 = R$drawable.miuix_appcompat_action_mode_title_button_deselect_all_dark;
                } else {
                    i3 = R$drawable.miuix_appcompat_action_mode_title_button_deselect_all_light;
                }
                editActionMode.setButton(android.R.id.button2, null, i3);
                return;
            }
            EditActionMode editActionMode2 = (EditActionMode) actionMode;
            if (Utils.isNightMode(RecyclerActivity.this.getApplicationContext())) {
                i2 = R$drawable.miuix_appcompat_action_mode_title_button_select_all_dark;
            } else {
                i2 = R$drawable.miuix_appcompat_action_mode_title_button_select_all_light;
            }
            editActionMode2.setButton(android.R.id.button2, null, i2);
        }
    }

    public class Pair extends android.util.Pair implements Map.Entry, Comparable, Serializable {
        public final Object first;
        public final Object second;

        @Override // java.util.Map.Entry
        public Object setValue(Object obj) {
            return null;
        }

        public Pair(Object obj, Object obj2) {
            super(obj, obj2);
            this.first = obj;
            this.second = obj2;
        }

        @Override // java.util.Map.Entry
        public Object getKey() {
            return this.first;
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            return this.second;
        }

        @Override // android.util.Pair, java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                if (Objects.equals(getKey(), entry.getKey()) && Objects.equals(getValue(), entry.getValue())) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.lang.Comparable
        public int compareTo(Pair pair) {
            return new CompareToBuilder().append(getKey(), pair.getKey()).append(getValue(), pair.getValue()).toComparison();
        }
    }
}
