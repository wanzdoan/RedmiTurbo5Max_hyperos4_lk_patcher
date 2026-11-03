package com.android.phone.settings;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class DeleteFdnContactScreen extends AppCompatActivity {
    private Handler mHandler = new Handler();
    protected String mName;
    protected String mNumber;
    protected String mPin2;
    protected QueryHandler mQueryHandler;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        resolveIntent();
        authenticatePin2();
        getWindow().requestFeature(5);
        setContentView(R.layout.delete_fdn_contact_screen);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i != 100) {
            return;
        }
        Bundle extras = intent != null ? intent.getExtras() : null;
        if (extras != null) {
            this.mPin2 = extras.getString("pin2");
            showStatus(getResources().getText(R.string.deleting_fdn_contact));
            deleteContact();
        } else {
            displayProgress(false);
            finish();
        }
    }

    protected void resolveIntent() {
        Intent intent = getIntent();
        this.mName = intent.getStringExtra("name");
        String stringExtra = intent.getStringExtra("number");
        this.mNumber = stringExtra;
        if (TextUtils.isEmpty(stringExtra)) {
            finish();
        }
    }

    protected void deleteContact() {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(this.mName)) {
            sb.append("number='");
        } else {
            sb.append("tag='");
            sb.append(this.mName);
            sb.append("' AND number='");
        }
        sb.append(this.mNumber);
        sb.append("' AND pin2='");
        sb.append(this.mPin2);
        sb.append("'");
        Uri fdnUri = MiuiPhoneUtils.getFdnUri(SubscriptionManager.getSlotIdExtra(getIntent(), SubscriptionManager.getDefault().getDefaultSlotId()));
        QueryHandler queryHandler = new QueryHandler(getContentResolver());
        this.mQueryHandler = queryHandler;
        queryHandler.startDelete(0, null, fdnUri, sb.toString(), null);
        displayProgress(true);
    }

    private void authenticatePin2() {
        Intent intent = new Intent();
        intent.setClass(this, GetPin2Screen.class);
        startActivityForResult(intent, 100);
    }

    protected void displayProgress(boolean z) {
        getWindow().setFeatureInt(5, z ? -1 : -2);
    }

    private void showStatus(CharSequence charSequence) {
        if (charSequence != null) {
            Toast.makeText(this, charSequence, 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleResult(boolean z) {
        if (z) {
            showStatus(getResources().getText(R.string.fdn_contact_deleted));
        } else {
            showStatus(getResources().getText(R.string.pin2_invalid));
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.settings.DeleteFdnContactScreen.1
            @Override // java.lang.Runnable
            public void run() {
                DeleteFdnContactScreen.this.finish();
            }
        }, 2000L);
    }

    public class QueryHandler extends AsyncQueryHandler {
        @Override // android.content.AsyncQueryHandler
        protected void onInsertComplete(int i, Object obj, Uri uri) {
        }

        @Override // android.content.AsyncQueryHandler
        protected void onQueryComplete(int i, Object obj, Cursor cursor) {
        }

        @Override // android.content.AsyncQueryHandler
        protected void onUpdateComplete(int i, Object obj, int i2) {
        }

        public QueryHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onDeleteComplete(int i, Object obj, int i2) {
            DeleteFdnContactScreen.this.displayProgress(false);
            DeleteFdnContactScreen.this.handleResult(i2 > 0);
        }
    }
}
