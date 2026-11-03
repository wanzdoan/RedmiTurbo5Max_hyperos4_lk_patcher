package com.android.phone;

import android.app.ListActivity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.android.phone.settings.SettingsConstants;
import com.mediatek.settings.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class ADNList extends ListActivity {
    private static final String[] COLUMN_NAMES = {"name", "number", "emails"};
    private static final int[] VIEW_NAMES = {android.R.id.text1, android.R.id.text2};
    protected CursorAdapter mCursorAdapter;
    private TextView mEmptyText;
    protected QueryHandler mQueryHandler;
    protected Cursor mCursor = null;
    protected int mInitialSelection = -1;
    protected int mSubId = -1;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        log("onCreate");
        super.onCreate(bundle);
        SettingsConstants.setupEdgeToEdge(this);
        getWindow().requestFeature(5);
        getWindow().addSystemFlags(524288);
        setContentView(R.layout.adn_list);
        this.mEmptyText = (TextView) findViewById(android.R.id.empty);
        this.mQueryHandler = new QueryHandler(getContentResolver());
        this.mSubId = getIntent().getIntExtra("subscription", -1);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        query();
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        if (this.mCursor == null || isInMultiWindowMode()) {
            return;
        }
        log("onStop deactivate cursor");
        this.mCursor.deactivate();
    }

    protected Uri resolveIntent() {
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(Uri.parse("content://icc/adn/subId/" + this.mSubId));
        }
        return intent.getData();
    }

    private void query() {
        Uri uriResolveIntent = resolveIntent();
        log("query: starting an async query");
        this.mQueryHandler.startQuery(0, null, uriResolveIntent, COLUMN_NAMES, null, null, null);
        displayProgress(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reQuery() {
        query();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAdapter() {
        CursorAdapter cursorAdapter = this.mCursorAdapter;
        if (cursorAdapter == null) {
            CursorAdapter cursorAdapterNewAdapter = newAdapter();
            this.mCursorAdapter = cursorAdapterNewAdapter;
            setListAdapter(cursorAdapterNewAdapter);
        } else {
            cursorAdapter.changeCursor(this.mCursor);
        }
        int i = this.mInitialSelection;
        if (i < 0 || i >= this.mCursorAdapter.getCount()) {
            return;
        }
        setSelection(this.mInitialSelection);
        getListView().setFocusableInTouchMode(true);
        getListView().requestFocus();
    }

    protected CursorAdapter newAdapter() {
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, this.mCursor, COLUMN_NAMES, VIEW_NAMES);
        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(this) { // from class: com.android.phone.ADNList.1
            @Override // android.widget.SimpleCursorAdapter.ViewBinder
            public boolean setViewValue(View view, Cursor cursor, int i) {
                view.setTextAlignment(5);
                if (i != 2) {
                    return false;
                }
                String string = cursor.getString(2);
                if (string != null) {
                    string = BidiFormatter.getInstance().unicodeWrap(string, TextDirectionHeuristics.LTR, true);
                }
                if (view instanceof TextView) {
                    ((TextView) view).setText(string);
                }
                return true;
            }
        });
        return simpleCursorAdapter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayProgress(boolean z) {
        int i;
        log("displayProgress: " + z);
        TextView textView = this.mEmptyText;
        if (z) {
            i = R.string.simContacts_emptyLoading;
        } else {
            i = TelephonyUtils.isAirplaneModeOn(this) ? R.string.simContacts_airplaneMode : R.string.simContacts_empty;
        }
        textView.setText(i);
        getWindow().setFeatureInt(5, z ? -1 : -2);
    }

    class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onQueryComplete(int i, Object obj, Cursor cursor) {
            ADNList.this.log("onQueryComplete: cursor.count=" + cursor.getCount());
            ADNList aDNList = ADNList.this;
            aDNList.mCursor = cursor;
            aDNList.setAdapter();
            ADNList.this.displayProgress(false);
            ADNList.this.invalidateOptionsMenu();
        }

        @Override // android.content.AsyncQueryHandler
        protected void onInsertComplete(int i, Object obj, Uri uri) {
            ADNList.this.log("onInsertComplete: requery");
            ADNList.this.reQuery();
        }

        @Override // android.content.AsyncQueryHandler
        protected void onUpdateComplete(int i, Object obj, int i2) {
            ADNList.this.log("onUpdateComplete: requery");
            ADNList.this.reQuery();
        }

        @Override // android.content.AsyncQueryHandler
        protected void onDeleteComplete(int i, Object obj, int i2) {
            ADNList.this.log("onDeleteComplete: requery");
            ADNList.this.reQuery();
        }
    }

    protected void log(String str) {
        Log.d("ADNList", "[ADNList] " + str);
    }

    @Override // android.app.ListActivity, android.app.Activity
    public void onDestroy() {
        log("onDestroy");
        Cursor cursor = this.mCursor;
        if (cursor != null && !cursor.isClosed()) {
            this.mCursor.close();
        }
        super.onDestroy();
    }
}
