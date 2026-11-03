package com.android.phone.settings;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.phone.R;
import miui.provider.Telocation;
import miuix.appcompat.R$attr;
import miuix.appcompat.app.AppCompatActivity;
import miuix.internal.util.AttributeResolver;

/* JADX INFO: loaded from: classes.dex */
public class CustomLocationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private LayoutInflater mInflater;
    private ListView mList;
    private CustomLocationAdapter mListAdapter;
    private QueryHandler mQueryHandler;

    interface LocationQuery {
        public static final String[] COLUMNS = {"_id", "number", "location", "type"};
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.custom_location_list);
        this.mInflater = (LayoutInflater) getSystemService("layout_inflater");
        ListView listView = (ListView) findViewById(R.id.custom_location_list);
        this.mList = listView;
        listView.setOnItemClickListener(this);
        this.mList.setEmptyView(findViewById(android.R.id.empty));
        this.mQueryHandler = new QueryHandler(this);
        startQuery();
    }

    public class ItemView {
        public final TextView location;
        public final TextView number;
        public final TextView type;

        public ItemView(View view) {
            this.location = (TextView) view.findViewById(R.id.location);
            this.number = (TextView) view.findViewById(R.id.number);
            this.type = (TextView) view.findViewById(R.id.type);
        }
    }

    public class CustomLocationAdapter extends CursorAdapter {
        public CustomLocationAdapter(Context context, Cursor cursor) {
            super(context, cursor, true);
        }

        @Override // android.widget.CursorAdapter
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View viewInflate = CustomLocationActivity.this.mInflater.inflate(R.layout.custom_location_listitem, (ViewGroup) null);
            viewInflate.setTag(new ItemView(viewInflate));
            return viewInflate;
        }

        @Override // android.widget.CursorAdapter
        public void bindView(View view, Context context, Cursor cursor) {
            ItemView itemView = (ItemView) view.getTag();
            itemView.location.setText(cursor.getString(2));
            itemView.number.setText(cursor.getString(1));
            int i = cursor.getInt(3);
            if (i == 0) {
                itemView.type.setText(R.string.telocation_number_type_tel);
            } else if (i == 1) {
                itemView.type.setText(R.string.telocation_number_type_mob);
            } else {
                if (i != 2) {
                    return;
                }
                itemView.type.setText(R.string.telocation_number_type_jituan);
            }
        }
    }

    private void startQuery() {
        this.mQueryHandler.startQuery(0, null, Telocation.CONTENT_CUSTOM_LOCATION_URI, LocationQuery.COLUMNS, null, null, null);
    }

    class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(Context context) {
            super(context.getContentResolver());
        }

        @Override // android.content.AsyncQueryHandler
        protected void onQueryComplete(int i, Object obj, Cursor cursor) {
            if (cursor != null) {
                CustomLocationActivity.this.changeCursor(cursor);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeCursor(Cursor cursor) {
        CustomLocationAdapter customLocationAdapter = new CustomLocationAdapter(this, cursor);
        this.mListAdapter = customLocationAdapter;
        this.mList.setAdapter((ListAdapter) customLocationAdapter);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 10, 0, R.string.add).setIcon(AttributeResolver.resolveDrawable(this, R$attr.actionBarNewIcon)).setShowAsAction(1);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
            return true;
        }
        if (menuItem.getItemId() == 10) {
            Intent intent = new Intent(this, (Class<?>) EditCustomLocationActivity.class);
            intent.setAction("action_add_custom_location");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        Intent intent = new Intent(this, (Class<?>) EditCustomLocationActivity.class);
        intent.setAction("action_edit_custom_location");
        Cursor cursor = this.mListAdapter.getCursor();
        if (cursor != null && cursor.moveToPosition(i)) {
            intent.putExtra("_id", cursor.getInt(0));
            intent.putExtra("number", cursor.getString(1));
            intent.putExtra("location", cursor.getString(2));
            intent.putExtra("type", cursor.getInt(3));
        }
        startActivity(intent);
    }
}
