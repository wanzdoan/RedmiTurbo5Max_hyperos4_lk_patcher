package com.android.phone.settings;

import android.R;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.LayoutUiModeHelper;
import miuix.recyclerview.card.CardItemDecoration;
import miuix.theme.token.ContainerToken;

/* JADX INFO: loaded from: classes.dex */
public abstract class ADNList extends AppCompatActivity {
    protected static final String[] COLUMN_NAMES = {"name", "number", "emails"};
    protected static final int[] VIEW_NAMES = {R.id.text1, R.id.text2};
    private ActionBar mActionBar;
    protected EditableRecyclerView.CursorAdapter mCursorAdapter;
    private TextView mEmptyText;
    private EditableRecyclerView mList;
    protected QueryHandler mQueryHandler;
    protected Cursor mCursor = null;
    protected int mSlotId = 0;

    protected abstract EditableRecyclerView.CursorAdapter newAdapter();

    protected abstract Uri resolveIntent();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LayoutUiModeHelper.autoSetLayoutUiMode(this);
        getWindow().addFlags(134217728);
        this.mSlotId = SubscriptionManager.getSlotIdExtra(getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
        setContentView(com.android.phone.R.layout.miui_adn_list);
        this.mEmptyText = (TextView) findViewById(R.id.empty);
        this.mQueryHandler = new QueryHandler(getContentResolver());
        this.mList = (EditableRecyclerView) findViewById(R.id.list);
        this.mList.setLayoutManager(new LinearLayoutManager(this));
        this.mList.setClipToPadding(false);
        registerCoordinateScrollView(this.mList);
        requestDispatchContentInset();
        ActionBar appCompatActionBar = getAppCompatActionBar();
        this.mActionBar = appCompatActionBar;
        if (appCompatActionBar != null) {
            appCompatActionBar.setHomeButtonEnabled(true);
        }
        setExtraHorizontalPaddingEnable(true);
        this.mList.addItemDecoration(new CardItemDecoration(this));
    }

    @Override // miuix.appcompat.app.AppCompatActivity, miuix.container.ExtraPaddingObserver
    public void onExtraPaddingChanged(int i) {
        super.onExtraPaddingChanged(i);
        RecyclerView.ItemDecoration itemDecorationAt = this.mList.getItemDecorationAt(0);
        if (itemDecorationAt instanceof CardItemDecoration) {
            int i2 = (int) (i + (ContainerToken.PADDING_BASE_DP * 3 * getResources().getDisplayMetrics().density));
            CardItemDecoration cardItemDecoration = (CardItemDecoration) itemDecorationAt;
            cardItemDecoration.setCardMarginStart(i2);
            cardItemDecoration.setCardMarginEnd(i2);
            if (this.mList.getAdapter() != null) {
                this.mList.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        query();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        Cursor cursor = this.mCursor;
        if (cursor != null) {
            cursor.deactivate();
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        unregisterCoordinateScrollView(this.mList);
        super.onDestroy();
    }

    private void query() {
        this.mQueryHandler.startQuery(0, null, resolveIntent(), COLUMN_NAMES, null, null, null);
        displayProgress(true);
    }

    protected void reQuery() {
        query();
    }

    protected void setAdapter() {
        EditableRecyclerView.CursorAdapter cursorAdapter = this.mCursorAdapter;
        if (cursorAdapter == null) {
            EditableRecyclerView.CursorAdapter cursorAdapterNewAdapter = newAdapter();
            this.mCursorAdapter = cursorAdapterNewAdapter;
            this.mList.setAdapter(cursorAdapterNewAdapter);
        } else {
            cursorAdapter.changeCursor(this.mCursor);
        }
        this.mCursorAdapter.updateGroupInfo();
        this.mEmptyText.setVisibility(this.mCursorAdapter.getItemCount() > 0 ? 8 : 0);
    }

    protected void displayProgress(boolean z) {
        int i;
        TextView textView = this.mEmptyText;
        if (z) {
            i = com.android.phone.R.string.simContacts_emptyLoading;
        } else {
            i = isAirplaneModeOn(this) ? com.android.phone.R.string.simContacts_airplaneMode : com.android.phone.R.string.simFdnContacts_empty;
        }
        textView.setText(i);
        this.mActionBar.setProgressBarIndeterminateVisibility(z);
    }

    private static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    public class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onQueryComplete(int i, Object obj, Cursor cursor) {
            ADNList aDNList = ADNList.this;
            aDNList.mCursor = cursor;
            aDNList.setAdapter();
            ADNList.this.displayProgress(false);
            ADNList.this.invalidateOptionsMenu();
        }

        @Override // android.content.AsyncQueryHandler
        protected void onInsertComplete(int i, Object obj, Uri uri) {
            ADNList.this.displayProgress(false);
            ADNList aDNList = ADNList.this;
            if (uri != null) {
                aDNList.showAlertDialog(aDNList.getString(com.android.phone.R.string.contactAddSuccess));
            } else {
                aDNList.showAlertDialog(aDNList.getString(com.android.phone.R.string.contactAddFailed));
            }
            ADNList.this.reQuery();
        }

        @Override // android.content.AsyncQueryHandler
        protected void onUpdateComplete(int i, Object obj, int i2) {
            ADNList.this.displayProgress(false);
            ADNList aDNList = ADNList.this;
            if (i2 == 1) {
                aDNList.showAlertDialog(aDNList.getString(com.android.phone.R.string.contactUpdateSuccess));
            } else {
                aDNList.showAlertDialog(aDNList.getString(com.android.phone.R.string.contactUpdateFailed));
            }
            ADNList.this.reQuery();
        }

        @Override // android.content.AsyncQueryHandler
        protected void onDeleteComplete(int i, Object obj, int i2) {
            ADNList.this.displayProgress(false);
            ADNList aDNList = ADNList.this;
            if (i2 == 1) {
                aDNList.showAlertDialog(aDNList.getString(com.android.phone.R.string.contactdeleteSuccess));
            } else {
                aDNList.showAlertDialog(aDNList.getString(com.android.phone.R.string.contactdeleteFailed));
            }
            ADNList.this.reQuery();
        }
    }

    protected void showAlertDialog(String str) {
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).create();
        alertDialogCreate.setTitle(getText(com.android.phone.R.string.alertdialogtitle));
        alertDialogCreate.setMessage(str);
        alertDialogCreate.setButton("OK", new DialogInterface.OnClickListener(this) { // from class: com.android.phone.settings.ADNList.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialogCreate.show();
    }
}
