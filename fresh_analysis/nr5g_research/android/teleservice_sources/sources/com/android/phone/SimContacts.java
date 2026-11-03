package com.android.phone;

import android.accounts.Account;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.android.phone.settings.SettingsConstants;
import gov.nist.javax.sip.address.GenericURI;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class SimContacts extends ADNList {
    static final ContentValues sEmptyContentValues = new ContentValues();
    private Account mAccount;
    private ProgressDialog mProgressDialog;

    class NamePhoneTypePair {
        final String name;
        final int phoneType;

        public NamePhoneTypePair(String str) {
            int length = str.length();
            int i = length - 2;
            if (i >= 0 && str.charAt(i) == '/') {
                char upperCase = Character.toUpperCase(str.charAt(length - 1));
                if (upperCase == 'W') {
                    this.phoneType = 3;
                } else if (upperCase == 'M' || upperCase == 'O') {
                    this.phoneType = 2;
                } else if (upperCase == 'H') {
                    this.phoneType = 1;
                } else {
                    this.phoneType = 7;
                }
                this.name = str.substring(0, i);
                return;
            }
            this.phoneType = 7;
            this.name = str;
        }
    }

    class ImportAllSimContactsThread extends Thread implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener {
        boolean mCanceled;

        public ImportAllSimContactsThread() {
            super("ImportAllSimContactsThread");
            this.mCanceled = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            new ContentValues();
            ContentResolver contentResolver = SimContacts.this.getContentResolver();
            SimContacts.this.mCursor.moveToPosition(-1);
            while (!this.mCanceled && SimContacts.this.mCursor.moveToNext()) {
                SimContacts simContacts = SimContacts.this;
                SimContacts.actuallyImportOneSimContact(simContacts.mCursor, contentResolver, simContacts.mAccount);
                SimContacts.this.mProgressDialog.incrementProgressBy(1);
            }
            SimContacts.this.mProgressDialog.dismiss();
            SimContacts.this.finish();
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            this.mCanceled = true;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == -2) {
                this.mCanceled = true;
                SimContacts.this.mProgressDialog.dismiss();
            } else {
                Log.e("SimContacts", "Unknown button event has come: " + dialogInterface.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean actuallyImportOneSimContact(Cursor cursor, ContentResolver contentResolver, Account account) {
        NamePhoneTypePair namePhoneTypePair = new NamePhoneTypePair(cursor.getString(1));
        String str = namePhoneTypePair.name;
        int i = namePhoneTypePair.phoneType;
        String string = cursor.getString(2);
        String string2 = cursor.getString(3);
        String[] strArrSplit = !TextUtils.isEmpty(string2) ? string2.split(",") : null;
        ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
        ContentProviderOperation.Builder builderNewInsert = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
        if (account != null) {
            builderNewInsert.withValue("account_name", account.name);
            builderNewInsert.withValue("account_type", account.type);
        } else {
            builderNewInsert.withValues(sEmptyContentValues);
        }
        arrayList.add(builderNewInsert.build());
        Uri uri = ContactsContract.Data.CONTENT_URI;
        ContentProviderOperation.Builder builderNewInsert2 = ContentProviderOperation.newInsert(uri);
        builderNewInsert2.withValueBackReference("raw_contact_id", 0);
        builderNewInsert2.withValue("mimetype", "vnd.android.cursor.item/name");
        builderNewInsert2.withValue("data1", str);
        arrayList.add(builderNewInsert2.build());
        ContentProviderOperation.Builder builderNewInsert3 = ContentProviderOperation.newInsert(uri);
        builderNewInsert3.withValueBackReference("raw_contact_id", 0);
        builderNewInsert3.withValue("mimetype", "vnd.android.cursor.item/phone_v2");
        builderNewInsert3.withValue("data2", Integer.valueOf(i));
        builderNewInsert3.withValue("data1", string);
        builderNewInsert3.withValue("is_primary", 1);
        arrayList.add(builderNewInsert3.build());
        if (strArrSplit != null) {
            for (String str2 : strArrSplit) {
                ContentProviderOperation.Builder builderNewInsert4 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                builderNewInsert4.withValueBackReference("raw_contact_id", 0);
                builderNewInsert4.withValue("mimetype", "vnd.android.cursor.item/email_v2");
                builderNewInsert4.withValue("data2", 4);
                builderNewInsert4.withValue("data1", str2);
                arrayList.add(builderNewInsert4.build());
            }
        }
        try {
            return contentResolver.applyBatch("com.android.contacts", arrayList).length > 0;
        } catch (OperationApplicationException e) {
            Log.e("SimContacts", String.format("%s: %s", e.toString(), e.getMessage()));
            return false;
        } catch (RemoteException e2) {
            Log.e("SimContacts", String.format("%s: %s", e2.toString(), e2.getMessage()));
            return false;
        }
    }

    private void importOneSimContact(int i) {
        ContentResolver contentResolver = getContentResolver();
        Context applicationContext = getApplicationContext();
        if (this.mCursor.moveToPosition(i)) {
            if (actuallyImportOneSimContact(this.mCursor, contentResolver, this.mAccount)) {
                Toast.makeText(applicationContext, R.string.singleContactImportedMsg, 0).show();
                return;
            } else {
                Toast.makeText(applicationContext, R.string.failedToImportSingleContactMsg, 0).show();
                return;
            }
        }
        Log.e("SimContacts", "Failed to move the cursor to the position \"" + i + "\"");
        Toast.makeText(applicationContext, R.string.failedToImportSingleContactMsg, 0).show();
    }

    @Override // com.android.phone.ADNList, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SettingsConstants.setupEdgeToEdge(this);
        getWindow().addSystemFlags(524288);
        Intent intent = getIntent();
        if (intent != null) {
            String stringExtra = intent.getStringExtra("account_name");
            String stringExtra2 = intent.getStringExtra("account_type");
            if (!TextUtils.isEmpty(stringExtra) && !TextUtils.isEmpty(stringExtra2)) {
                this.mAccount = new Account(stringExtra, stringExtra2);
            }
        }
        registerForContextMenu(getListView());
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override // com.android.phone.ADNList
    protected CursorAdapter newAdapter() {
        return new SimpleCursorAdapter(this, R.layout.sim_import_list_entry, this.mCursor, new String[]{"name"}, new int[]{android.R.id.text1});
    }

    @Override // com.android.phone.ADNList
    protected Uri resolveIntent() {
        Intent intent = getIntent();
        int intExtra = intent.hasExtra("subscription_id") ? intent.getIntExtra("subscription_id", -1) : -1;
        if (intExtra != -1) {
            intent.setData(Uri.parse("content://icc/adn/subId/" + intExtra));
        } else {
            intent.setData(Uri.parse("content://icc/adn"));
        }
        if ("android.intent.action.PICK".equals(intent.getAction())) {
            this.mInitialSelection = intent.getIntExtra("index", 0) - 1;
        }
        return intent.getData();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 2, 0, R.string.importAllSimEntries);
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemFindItem = menu.findItem(2);
        if (menuItemFindItem != null) {
            Cursor cursor = this.mCursor;
            menuItemFindItem.setVisible(cursor != null && cursor.getCount() > 0);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
            return true;
        }
        if (itemId == 2) {
            String string = getString(R.string.importAllSimEntries);
            String string2 = getString(R.string.importingSimContacts);
            ImportAllSimContactsThread importAllSimContactsThread = new ImportAllSimContactsThread();
            if (this.mCursor == null) {
                Log.e("SimContacts", "cursor is null. Ignore silently.");
                return true;
            }
            ProgressDialog progressDialog = new ProgressDialog(this);
            this.mProgressDialog = progressDialog;
            progressDialog.setTitle(string);
            this.mProgressDialog.setMessage(string2);
            this.mProgressDialog.setProgressStyle(1);
            this.mProgressDialog.setButton(-2, getString(R.string.cancel), importAllSimContactsThread);
            this.mProgressDialog.setProgress(0);
            this.mProgressDialog.setMax(this.mCursor.getCount());
            this.mProgressDialog.show();
            importAllSimContactsThread.start();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.app.Activity
    public boolean onContextItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 1) {
            ContextMenu.ContextMenuInfo menuInfo = menuItem.getMenuInfo();
            if (menuInfo instanceof AdapterView.AdapterContextMenuInfo) {
                importOneSimContact(((AdapterView.AdapterContextMenuInfo) menuInfo).position);
                return true;
            }
        }
        return super.onContextItemSelected(menuItem);
    }

    @Override // android.app.Activity, android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        if (contextMenuInfo instanceof AdapterView.AdapterContextMenuInfo) {
            TextView textView = (TextView) ((AdapterView.AdapterContextMenuInfo) contextMenuInfo).targetView.findViewById(android.R.id.text1);
            if (textView != null) {
                contextMenu.setHeaderTitle(textView.getText());
            }
            contextMenu.add(0, 1, 0, R.string.importSimEntry);
        }
    }

    @Override // android.app.ListActivity
    public void onListItemClick(ListView listView, View view, int i, long j) {
        importOneSimContact(i);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        Cursor cursor;
        if (i == 5 && (cursor = this.mCursor) != null && cursor.moveToPosition(getSelectedItemPosition())) {
            String string = this.mCursor.getString(2);
            if (string != null && TextUtils.isGraphic(string)) {
                Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED", Uri.fromParts(GenericURI.TEL, string, null));
                intent.setFlags(276824064);
                startActivityAsUser(intent, UserHandle.CURRENT);
                finish();
            }
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }
}
