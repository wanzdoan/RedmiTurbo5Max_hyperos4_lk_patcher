package com.android.phone.settings;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.R;
import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import miuix.appcompat.R$plurals;
import miuix.appcompat.R$string;
import miuix.recyclerview.card.CardGroupAdapter;
import miuix.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes.dex */
public class EditableRecyclerView extends RecyclerView {

    public interface MultiChoiceModeListener extends ActionMode.Callback {
        void onItemCheckedStateChanged(ActionMode actionMode, int i, boolean z);
    }

    public EditableRecyclerView(Context context) {
        super(context);
    }

    public EditableRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public EditableRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public abstract class EditableAdapter extends CardGroupAdapter {
        private ActionMode mActionMode;
        private Activity mActivity;
        private MultiChoiceModeListener mCallback;
        ArrayList mData;
        private int[] mId;
        private String[] mKey;
        private int mResId;
        private SparseBooleanArray mItemCheckState = new SparseBooleanArray();
        public boolean isEditMode = false;

        @Override // miuix.recyclerview.card.CardGroupAdapter
        public int getItemViewGroup(int i) {
            return 1;
        }

        public EditableAdapter(Activity activity, ArrayList arrayList, int i, String[] strArr, int[] iArr) {
            new ArrayList();
            this.mActivity = activity;
            this.mData = arrayList;
            this.mResId = i;
            this.mKey = strArr;
            this.mId = iArr;
        }

        public void setMultiChoiceModeListener(MultiChoiceModeListener multiChoiceModeListener) {
            this.mCallback = multiChoiceModeListener;
        }

        public ActionMode.Callback getCallback() {
            return this.mCallback;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public EditableViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new EditableViewHolder(LayoutInflater.from(this.mActivity).inflate(this.mResId, viewGroup, false), this.mId);
        }

        public void enterEditMode() {
            this.isEditMode = true;
            setEditMode(true);
        }

        public void exitEditMode() {
            this.isEditMode = false;
            this.mItemCheckState.clear();
            setEditMode(false);
        }

        private void setEditMode(boolean z) {
            this.isEditMode = z;
            notifyDataSetChanged();
        }

        @Override // miuix.recyclerview.card.CardGroupAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(final EditableViewHolder editableViewHolder, final int i) {
            super.onBindViewHolder((androidx.recyclerview.widget.RecyclerView.ViewHolder) editableViewHolder, i);
            HashMap map = (HashMap) this.mData.get(i);
            editableViewHolder.mCheckbox.setVisibility(this.isEditMode ? 0 : 8);
            editableViewHolder.mName.setText((CharSequence) map.get(this.mKey[0]));
            editableViewHolder.mNumber.setText((CharSequence) map.get(this.mKey[1]));
            editableViewHolder.itemView.setLongClickable(true);
            editableViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.phone.settings.EditableRecyclerView.EditableAdapter.1
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    EditableAdapter editableAdapter = EditableAdapter.this;
                    if (!editableAdapter.isEditMode) {
                        editableAdapter.isEditMode = true;
                        editableAdapter.mActionMode = view.startActionMode(editableAdapter.getCallback());
                        EditableAdapter.this.setCheck(i, true, true);
                    }
                    return true;
                }
            });
            editableViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.EditableRecyclerView.EditableAdapter.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (!EditableAdapter.this.isEditMode) {
                        view.showContextMenu();
                        return;
                    }
                    CheckBox checkBox = editableViewHolder.mCheckbox;
                    checkBox.setChecked(!checkBox.isChecked());
                    EditableAdapter.this.setCheck(i, editableViewHolder.mCheckbox.isChecked(), false);
                }
            });
            editableViewHolder.mCheckbox.setChecked(isItemChecked(i));
        }

        @Override // miuix.recyclerview.card.CardGroupAdapter
        public void setHasStableIds() {
            setHasStableIds(true);
        }

        public void setCheck(int i, boolean z, boolean z2) {
            SparseBooleanArray sparseBooleanArray = this.mItemCheckState;
            if (z) {
                sparseBooleanArray.put(i, true);
            } else {
                sparseBooleanArray.delete(i);
            }
            this.mCallback.onItemCheckedStateChanged(this.mActionMode, i, z);
            if (z2) {
                notifyDataSetChanged();
            }
        }

        public void setAllCheck(boolean z) {
            if (z) {
                int itemCount = getItemCount();
                for (int i = 0; i < itemCount; i++) {
                    this.mItemCheckState.put(i, z);
                }
            } else {
                this.mItemCheckState.clear();
            }
            notifyDataSetChanged();
        }

        public boolean isAllChecked() {
            return getCheckedItemCount() == getItemCount();
        }

        public boolean isItemChecked(int i) {
            return this.mItemCheckState.get(i);
        }

        public SparseBooleanArray getCheckedItemPositions() {
            return this.mItemCheckState.clone();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mData.size();
        }

        public int getCheckedItemCount() {
            return this.mItemCheckState.size();
        }

        public void updateActionMenu(Context context, ActionMode actionMode) {
            String quantityString;
            int checkedItemCount = getCheckedItemCount();
            if (checkedItemCount == 0) {
                quantityString = context.getString(R$string.miuix_appcompat_action_mode_title_empty);
                actionMode.getMenu().findItem(2).setEnabled(false);
            } else {
                quantityString = context.getResources().getQuantityString(R$plurals.miuix_appcompat_items_selected, checkedItemCount, Integer.valueOf(checkedItemCount));
                actionMode.getMenu().findItem(2).setEnabled(true);
            }
            actionMode.setTitle(quantityString);
        }

        class EditableViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            CheckBox mCheckbox;
            TextView mName;
            TextView mNumber;

            public EditableViewHolder(View view, int[] iArr) {
                super(view);
                this.mName = (TextView) view.findViewById(iArr[0]);
                this.mNumber = (TextView) view.findViewById(iArr[1]);
                this.mCheckbox = (CheckBox) view.findViewById(R.id.checkbox);
            }
        }
    }

    public class CursorAdapter extends CardGroupAdapter implements Filterable, CursorFilter.CursorFilterClient {
        private static int[] mVnames;
        private ChangeObserver mChangeObserver;
        private String[] mCnames;
        protected Context mContext;
        private Cursor mCursor;
        private CursorFilter mCursorFilter;
        private DataSetObserver mDataSetObserver;
        private boolean mDataValid;
        private FilterQueryProvider mFilterQueryProvider;
        private int mResId;
        private int mRowIDColumn;
        private OnItemClickListener onItemClickListener;

        public interface OnItemClickListener {
            void onItemClick(Context context, View view, int i);
        }

        @Override // miuix.recyclerview.card.CardGroupAdapter
        public int getItemViewGroup(int i) {
            return 1;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public CursorAdapter(Context context, int i, Cursor cursor, String[] strArr, int[] iArr, int i2) {
            init(context, i, cursor, strArr, iArr, i2);
        }

        private void init(Context context, int i, Cursor cursor, String[] strArr, int[] iArr, int i2) {
            boolean z = cursor != null;
            this.mCursor = cursor;
            this.mDataValid = z;
            this.mContext = context;
            this.mResId = i;
            this.mCnames = strArr;
            mVnames = iArr;
            this.mRowIDColumn = z ? cursor.getColumnIndexOrThrow("_id") : -1;
            if ((i2 & 2) == 2) {
                this.mChangeObserver = new ChangeObserver();
                this.mDataSetObserver = new MyDataSetObserver();
            } else {
                this.mChangeObserver = null;
                this.mDataSetObserver = null;
            }
            if (z) {
                ChangeObserver changeObserver = this.mChangeObserver;
                if (changeObserver != null) {
                    cursor.registerContentObserver(changeObserver);
                }
                DataSetObserver dataSetObserver = this.mDataSetObserver;
                if (dataSetObserver != null) {
                    cursor.registerDataSetObserver(dataSetObserver);
                }
            }
            setHasStableIds(true);
        }

        private void setTextView(final TextView textView, final CharSequence charSequence) {
            textView.post(new Runnable(this) { // from class: com.android.phone.settings.EditableRecyclerView.CursorAdapter.1
                @Override // java.lang.Runnable
                public void run() {
                    textView.setVisibility(0);
                    textView.setText(charSequence);
                }
            });
        }

        @Override // com.android.phone.settings.EditableRecyclerView.CursorFilter.CursorFilterClient
        public Cursor getCursor() {
            return this.mCursor;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            Cursor cursor;
            if (!this.mDataValid || (cursor = this.mCursor) == null) {
                return 0;
            }
            return cursor.getCount();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            Cursor cursor;
            if (this.mDataValid && (cursor = this.mCursor) != null && cursor.moveToPosition(i)) {
                return this.mCursor.getLong(this.mRowIDColumn);
            }
            return 0L;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(this.mContext).inflate(this.mResId, viewGroup, false), mVnames);
        }

        @Override // miuix.recyclerview.card.CardGroupAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            super.onBindViewHolder((androidx.recyclerview.widget.RecyclerView.ViewHolder) viewHolder, i);
            viewHolder.setGone();
            if (!this.mDataValid) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("this should only be called when the cursor is valid");
                return;
            }
            if (!this.mCursor.moveToPosition(i)) {
                CommonOptions$$ExternalSyntheticBUOutline0.m("couldn't move cursor to position ", i);
                return;
            }
            Cursor cursor = this.mCursor;
            String string = cursor.getString(cursor.getColumnIndex(this.mCnames[0]));
            Cursor cursor2 = this.mCursor;
            String string2 = cursor2.getString(cursor2.getColumnIndex(this.mCnames[1]));
            setTextView(viewHolder.mName, string);
            setTextView(viewHolder.mNumber, string2);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.EditableRecyclerView.CursorAdapter.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    CursorAdapter.this.onItemClickListener.onItemClick(CursorAdapter.this.mContext, view, i);
                }
            });
        }

        @Override // miuix.recyclerview.card.CardGroupAdapter
        public void setHasStableIds() {
            setHasStableIds(true);
        }

        @Override // com.android.phone.settings.EditableRecyclerView.CursorFilter.CursorFilterClient
        public void changeCursor(Cursor cursor) {
            Cursor cursorSwapCursor = swapCursor(cursor);
            if (cursorSwapCursor != null) {
                cursorSwapCursor.close();
            }
        }

        public Cursor swapCursor(Cursor cursor) {
            Cursor cursor2 = this.mCursor;
            if (cursor == cursor2) {
                return null;
            }
            if (cursor2 != null) {
                ChangeObserver changeObserver = this.mChangeObserver;
                if (changeObserver != null) {
                    cursor2.unregisterContentObserver(changeObserver);
                }
                DataSetObserver dataSetObserver = this.mDataSetObserver;
                if (dataSetObserver != null) {
                    cursor2.unregisterDataSetObserver(dataSetObserver);
                }
            }
            this.mCursor = cursor;
            if (cursor != null) {
                ChangeObserver changeObserver2 = this.mChangeObserver;
                if (changeObserver2 != null) {
                    cursor.registerContentObserver(changeObserver2);
                }
                DataSetObserver dataSetObserver2 = this.mDataSetObserver;
                if (dataSetObserver2 != null) {
                    cursor.registerDataSetObserver(dataSetObserver2);
                }
                this.mRowIDColumn = cursor.getColumnIndexOrThrow("_id");
                this.mDataValid = true;
                notifyDataSetChanged();
                return cursor2;
            }
            this.mRowIDColumn = -1;
            this.mDataValid = false;
            notifyDataSetChanged();
            return cursor2;
        }

        @Override // com.android.phone.settings.EditableRecyclerView.CursorFilter.CursorFilterClient
        public CharSequence convertToString(Cursor cursor) {
            return cursor == null ? "" : cursor.toString();
        }

        @Override // com.android.phone.settings.EditableRecyclerView.CursorFilter.CursorFilterClient
        public Cursor runQueryOnBackgroundThread(CharSequence charSequence) {
            FilterQueryProvider filterQueryProvider = this.mFilterQueryProvider;
            if (filterQueryProvider != null) {
                return filterQueryProvider.runQuery(charSequence);
            }
            return this.mCursor;
        }

        @Override // android.widget.Filterable
        public Filter getFilter() {
            if (this.mCursorFilter == null) {
                this.mCursorFilter = new CursorFilter(this);
            }
            return this.mCursorFilter;
        }

        class ChangeObserver extends ContentObserver {
            @Override // android.database.ContentObserver
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
            }

            private ChangeObserver(CursorAdapter cursorAdapter) {
                super(new Handler());
            }
        }

        class MyDataSetObserver extends DataSetObserver {
            private MyDataSetObserver() {
            }

            @Override // android.database.DataSetObserver
            public void onChanged() {
                CursorAdapter.this.mDataValid = true;
                CursorAdapter.this.notifyDataSetChanged();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                CursorAdapter.this.mDataValid = false;
                CursorAdapter.this.notifyDataSetChanged();
            }
        }

        public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            TextView mName;
            TextView mNumber;

            private ViewHolder(View view, int[] iArr) {
                super(view);
                this.mName = (TextView) view.findViewById(iArr[0]);
                this.mNumber = (TextView) view.findViewById(iArr[1]);
            }

            protected void setGone() {
                this.mName.setVisibility(8);
                this.mNumber.setVisibility(8);
            }
        }
    }

    class CursorFilter extends Filter {
        CursorFilterClient mClient;

        interface CursorFilterClient {
            void changeCursor(Cursor cursor);

            CharSequence convertToString(Cursor cursor);

            Cursor getCursor();

            Cursor runQueryOnBackgroundThread(CharSequence charSequence);
        }

        CursorFilter(CursorFilterClient cursorFilterClient) {
            this.mClient = cursorFilterClient;
        }

        @Override // android.widget.Filter
        public CharSequence convertResultToString(Object obj) {
            return this.mClient.convertToString((Cursor) obj);
        }

        @Override // android.widget.Filter
        protected Filter.FilterResults performFiltering(CharSequence charSequence) {
            Cursor cursorRunQueryOnBackgroundThread = this.mClient.runQueryOnBackgroundThread(charSequence);
            Filter.FilterResults filterResults = new Filter.FilterResults();
            if (cursorRunQueryOnBackgroundThread != null) {
                filterResults.count = cursorRunQueryOnBackgroundThread.getCount();
                filterResults.values = cursorRunQueryOnBackgroundThread;
                return filterResults;
            }
            filterResults.count = 0;
            filterResults.values = null;
            return filterResults;
        }

        @Override // android.widget.Filter
        protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            Cursor cursor = this.mClient.getCursor();
            Object obj = filterResults.values;
            if (obj == null || obj == cursor) {
                return;
            }
            this.mClient.changeCursor((Cursor) obj);
        }
    }
}
