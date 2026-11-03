package com.android.phone.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import gov.nist.javax.sip.address.GenericURI;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miuix.appcompat.widget.PopupMenu;

/* JADX INFO: loaded from: classes.dex */
public class FdnList extends ADNList {
    private static final Intent CONTACT_IMPORT_INTENT;
    private static final String[] NUM_PROJECTION;
    private boolean mFdnDialDirectlySupported = false;
    private SelectionPopupMenu mPopup;

    static {
        if (TelephonyManager.isGoogleCsp() && !TelephonyManagerEx.getDefault().isGoogleDialerDeleted(PhoneGlobals.getInstance())) {
            CONTACT_IMPORT_INTENT = new Intent("android.intent.action.PICK", ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        } else {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            CONTACT_IMPORT_INTENT = intent;
            intent.setPackage("com.android.contacts");
            intent.setType("vnd.android.cursor.item/phone_v2");
        }
        NUM_PROJECTION = new String[]{"display_name", "data1"};
    }

    @Override // com.android.phone.settings.ADNList
    protected Uri resolveIntent() {
        Intent intent = getIntent();
        intent.setData(MiuiPhoneUtils.getFdnUri(this.mSlotId));
        return intent.getData();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.ADNList, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        SelectionPopupMenu selectionPopupMenu = this.mPopup;
        if (selectionPopupMenu != null) {
            selectionPopupMenu.dismiss();
        }
    }

    @Override // com.android.phone.settings.ADNList, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mFdnDialDirectlySupported = getFdnDialDirectlySupported();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Resources resources = getResources();
        menu.add(0, 1, 0, resources.getString(R.string.menu_add)).setIcon(R.drawable.ic_menu_add_circle).setShowAsAction(2);
        menu.add(0, 2, 0, resources.getString(R.string.importToFDNfromContacts)).setIcon(R.drawable.ic_menu_import).setShowAsAction(2);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        }
        if (itemId == 1) {
            addContact();
            return true;
        }
        if (itemId == 2) {
            startActivityForResult(CONTACT_IMPORT_INTENT, 200);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 200 && i2 == -1) {
            Cursor cursor = null;
            try {
                try {
                    Cursor cursorQuery = getContentResolver().query(intent.getData(), NUM_PROJECTION, null, null, null);
                    if (cursorQuery != null && cursorQuery.moveToFirst()) {
                        String string = cursorQuery.getString(0);
                        String string2 = cursorQuery.getString(1);
                        cursorQuery.close();
                        addContact(string, string2);
                        return;
                    }
                    Log.w("FdnList", "onActivityResult: bad contact data, no results found.");
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                } catch (RuntimeException e) {
                    Log.w("FdnList", e.toString());
                    if (0 != 0) {
                        cursor.close();
                    }
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    cursor.close();
                    throw th;
                }
                throw th;
            }
        }
    }

    @Override // com.android.phone.settings.ADNList
    protected void setAdapter() {
        super.setAdapter();
        this.mCursorAdapter.setOnItemClickListener(new EditableRecyclerView.CursorAdapter.OnItemClickListener() { // from class: com.android.phone.settings.FdnList.1
            @Override // com.android.phone.settings.EditableRecyclerView.CursorAdapter.OnItemClickListener
            public void onItemClick(Context context, View view, int i) {
                FdnList fdnList = FdnList.this;
                fdnList.mPopup = fdnList.new SelectionPopupMenu(context, view, i);
                FdnList.this.mPopup.showPopUp();
            }
        });
    }

    @Override // com.android.phone.settings.ADNList
    protected EditableRecyclerView.CursorAdapter newAdapter() {
        return new EditableRecyclerView.CursorAdapter(this, R.layout.simple_list_item_2, this.mCursor, ADNList.COLUMN_NAMES, ADNList.VIEW_NAMES, 2);
    }

    private void addContact() {
        addContact(null, null);
    }

    private void addContact(String str, String str2) {
        startActivity(str, str2, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void editOrDeleteSelected(int i, int i2) {
        if (this.mCursor.moveToPosition(i)) {
            startActivity(this.mCursor.getString(0), this.mCursor.getString(1), i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dialSelected(int i) {
        if (this.mCursor.moveToPosition(i)) {
            String string = this.mCursor.getString(1);
            if (TextUtils.isEmpty(string)) {
                return;
            }
            startActivity(new Intent("android.intent.action.CALL_PRIVILEGED", Uri.fromParts(GenericURI.TEL, string, null)));
        }
    }

    private void startActivity(String str, String str2, int i) {
        Intent intent = new Intent();
        intent.setClass(this, EditFdnContactScreen.class);
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("name", str);
        }
        if (!TextUtils.isEmpty(str2)) {
            intent.putExtra("number", str2);
        }
        intent.putExtra("type", i);
        SubscriptionManager.putSlotIdExtra(intent, this.mSlotId);
        startActivity(intent);
    }

    private boolean getFdnDialDirectlySupported() {
        return CarrierConfigManagerEx.getBooleanConfigForSubId(MiuiPhoneUtils.getPhone(this.mSlotId).getContext(), MiuiPhoneUtils.getPhone(this.mSlotId).getSubId(), "support_direct_fdn_dialing_bool", false);
    }

    class SelectionPopupMenu extends PopupMenu {
        private PopupMenu.OnMenuItemClickListener mMenuItemListener;
        private final int position;

        public SelectionPopupMenu(Context context, View view, int i) {
            super(context, view);
            this.mMenuItemListener = new PopupMenu.OnMenuItemClickListener() { // from class: com.android.phone.settings.FdnList.SelectionPopupMenu.1
                @Override // miuix.appcompat.widget.PopupMenu.OnMenuItemClickListener
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == 3) {
                        SelectionPopupMenu selectionPopupMenu = SelectionPopupMenu.this;
                        FdnList.this.editOrDeleteSelected(selectionPopupMenu.position, 3);
                        return true;
                    }
                    if (menuItem.getItemId() == 4) {
                        SelectionPopupMenu selectionPopupMenu2 = SelectionPopupMenu.this;
                        FdnList.this.editOrDeleteSelected(selectionPopupMenu2.position, 4);
                        return true;
                    }
                    if (menuItem.getItemId() != 5) {
                        return true;
                    }
                    SelectionPopupMenu selectionPopupMenu3 = SelectionPopupMenu.this;
                    FdnList.this.dialSelected(selectionPopupMenu3.position);
                    return true;
                }
            };
            this.position = i;
        }

        public void showPopUp() {
            getMenu().add(0, 3, 0, FdnList.this.getString(R.string.menu_edit));
            getMenu().add(0, 4, 0, FdnList.this.getString(R.string.menu_delete));
            if (FdnList.this.mFdnDialDirectlySupported) {
                getMenu().add(0, 5, 0, FdnList.this.getString(R.string.menu_dial));
            }
            setOnMenuItemClickListener(this.mMenuItemListener);
            show();
        }
    }
}
