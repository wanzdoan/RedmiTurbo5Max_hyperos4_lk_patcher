package com.android.phone.settings.fdn;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import com.android.phone.ADNList;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.SubscriptionInfoHelper;
import com.android.phone.settings.SettingsConstants;
import gov.nist.javax.sip.address.GenericURI;

/* JADX INFO: loaded from: classes.dex */
public class FdnList extends ADNList implements PhoneGlobals.SubInfoUpdateListener {
    private static final Uri FDN_CONTENT_URI = Uri.parse("content://icc/fdn");
    private SelectionPopupMenu mPopup;
    private SubscriptionInfoHelper mSubscriptionInfoHelper;
    private boolean mFdnDialDirectlySupported = false;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.fdn.FdnList.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                FdnList.this.log("AIRPLANE_MODE_CHANGED, so finish FDN Settings");
                FdnList.this.finish();
            }
        }
    };

    class SelectionPopupMenu extends PopupMenu {
        private PopupMenu.OnMenuItemClickListener mMenuItemListener;
        private final int position;

        public SelectionPopupMenu(Context context, View view, int i) {
            super(context, view, 5);
            this.mMenuItemListener = new PopupMenu.OnMenuItemClickListener() { // from class: com.android.phone.settings.fdn.FdnList.SelectionPopupMenu.1
                @Override // android.widget.PopupMenu.OnMenuItemClickListener
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == 2) {
                        SelectionPopupMenu selectionPopupMenu = SelectionPopupMenu.this;
                        FdnList.this.editSelected(selectionPopupMenu.position);
                        return true;
                    }
                    if (menuItem.getItemId() == 3) {
                        SelectionPopupMenu selectionPopupMenu2 = SelectionPopupMenu.this;
                        FdnList.this.deleteSelected(selectionPopupMenu2.position);
                        return true;
                    }
                    if (menuItem.getItemId() != 4) {
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
            getMenu().add(0, 2, 0, FdnList.this.getString(R.string.menu_edit));
            getMenu().add(0, 3, 0, FdnList.this.getString(R.string.menu_delete));
            if (FdnList.this.mFdnDialDirectlySupported) {
                getMenu().add(0, 4, 0, FdnList.this.getString(R.string.menu_dial));
            }
            setOnMenuItemClickListener(this.mMenuItemListener);
            show();
        }
    }

    @Override // com.android.phone.ADNList, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SubscriptionInfoHelper subscriptionInfoHelper = new SubscriptionInfoHelper(this, getIntent());
        this.mSubscriptionInfoHelper = subscriptionInfoHelper;
        subscriptionInfoHelper.setActionBarTitle(getActionBar(), getResources(), R.string.fdn_list_with_label);
        SettingsConstants.setupEdgeToEdge(this);
        if (!getResources().getBoolean(android.R.bool.config_letterboxIsAutomaticReachabilityInBookModeEnabled) && !getPackageManager().hasSystemFeature("android.hardware.telephony")) {
            Log.i("ADNList", "onCreate()... but not defined FEATURE_TELEPHONY, finish");
            finish();
        } else {
            PhoneGlobals.getInstance().addSubInfoUpdateListener(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
            registerReceiver(this.mReceiver, intentFilter);
        }
    }

    @Override // com.android.phone.ADNList, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mFdnDialDirectlySupported = getFdnDialDirectlySupported();
    }

    @Override // com.android.phone.ADNList, android.app.Activity
    protected void onStop() {
        super.onStop();
        SelectionPopupMenu selectionPopupMenu = this.mPopup;
        if (selectionPopupMenu != null) {
            selectionPopupMenu.dismiss();
        }
    }

    @Override // com.android.phone.ADNList
    protected Uri resolveIntent() {
        Intent intent = getIntent();
        intent.setData(getContentUri(this.mSubscriptionInfoHelper));
        return intent.getData();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Resources resources = getResources();
        menu.add(0, 1, 0, resources.getString(R.string.menu_add)).setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, 2, 0, resources.getString(R.string.menu_edit)).setIcon(android.R.drawable.ic_menu_edit);
        menu.add(0, 3, 0, resources.getString(R.string.menu_delete)).setIcon(android.R.drawable.ic_menu_delete);
        menu.add(0, 4, 0, resources.getString(R.string.menu_dial));
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean z = false;
        boolean z2 = getSelectedItemPosition() >= 0;
        menu.findItem(1).setVisible(true);
        menu.findItem(2).setVisible(z2);
        menu.findItem(3).setVisible(z2);
        MenuItem menuItemFindItem = menu.findItem(4);
        if (z2 && this.mFdnDialDirectlySupported) {
            z = true;
        }
        menuItemFindItem.setVisible(z);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            Intent intent = this.mSubscriptionInfoHelper.getIntent(FdnSetting.class);
            intent.setAction("android.intent.action.MAIN");
            intent.addFlags(67108864);
            startActivity(intent);
            finish();
            return true;
        }
        if (itemId == 1) {
            addContact();
            return true;
        }
        if (itemId == 2) {
            editSelected();
            return true;
        }
        if (itemId == 3) {
            deleteSelected();
            return true;
        }
        if (itemId == 4) {
            dialSelected();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.app.ListActivity
    public void onListItemClick(ListView listView, View view, int i, long j) {
        SelectionPopupMenu selectionPopupMenu = new SelectionPopupMenu(this, view, i);
        this.mPopup = selectionPopupMenu;
        selectionPopupMenu.showPopUp();
    }

    private void addContact() {
        Intent intent = this.mSubscriptionInfoHelper.getIntent(EditFdnContactScreen.class);
        intent.putExtra("addContact", true);
        startActivityAsUser(intent, UserHandle.CURRENT);
    }

    private void editSelected() {
        editSelected(getSelectedItemPosition());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void editSelected(int i) {
        if (this.mCursor.moveToPosition(i)) {
            String string = this.mCursor.getString(1);
            String string2 = this.mCursor.getString(2);
            Intent intent = this.mSubscriptionInfoHelper.getIntent(EditFdnContactScreen.class);
            intent.putExtra("name", string);
            intent.putExtra("number", string2);
            intent.putExtra("addContact", false);
            startActivityAsUser(intent, UserHandle.CURRENT);
        }
    }

    private void deleteSelected() {
        deleteSelected(getSelectedItemPosition());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteSelected(int i) {
        if (this.mCursor.moveToPosition(i)) {
            String string = this.mCursor.getString(1);
            String string2 = this.mCursor.getString(2);
            Intent intent = this.mSubscriptionInfoHelper.getIntent(DeleteFdnContactScreen.class);
            intent.putExtra("name", string);
            intent.putExtra("number", string2);
            startActivityAsUser(intent, UserHandle.CURRENT);
        }
    }

    private void dialSelected() {
        dialSelected(getSelectedItemPosition());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dialSelected(int i) {
        if (this.mCursor.moveToPosition(i)) {
            String string = this.mCursor.getString(2);
            if (TextUtils.isEmpty(string)) {
                return;
            }
            startActivityAsUser(new Intent("android.intent.action.CALL_PRIVILEGED", Uri.fromParts(GenericURI.TEL, string, null)), UserHandle.CURRENT);
        }
    }

    public static Uri getContentUri(SubscriptionInfoHelper subscriptionInfoHelper) {
        if (subscriptionInfoHelper.hasSubId()) {
            return Uri.parse("content://icc/fdn/subId/" + subscriptionInfoHelper.getSubId());
        }
        return FDN_CONTENT_URI;
    }

    private boolean getFdnDialDirectlySupported() {
        int defaultSubscriptionId;
        if (this.mSubscriptionInfoHelper.hasSubId()) {
            defaultSubscriptionId = this.mSubscriptionInfoHelper.getSubId();
        } else {
            defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        }
        return PhoneGlobals.getInstance().getCarrierConfigForSubId(defaultSubscriptionId).getBoolean("support_direct_fdn_dialing_bool");
    }

    @Override // com.android.phone.ADNList, android.app.ListActivity, android.app.Activity
    public void onDestroy() {
        unregisterReceiver(this.mReceiver);
        PhoneGlobals.getInstance().removeSubInfoUpdateListener(this);
        super.onDestroy();
    }

    @Override // com.android.phone.PhoneGlobals.SubInfoUpdateListener
    public void handleSubInfoUpdate() {
        finish();
    }
}
