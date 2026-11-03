package com.android.phone.settings.fdn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class DeleteFdnContactScreen extends BaseFdnContactScreen {
    @Override // com.android.phone.settings.fdn.BaseFdnContactScreen, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        if (bundle == null) {
            authenticatePin2();
        }
        setContentView(R.layout.delete_fdn_contact_screen);
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        log("onActivityResult");
        if (i != 100) {
            return;
        }
        Bundle extras = intent != null ? intent.getExtras() : null;
        if (extras != null) {
            String string = extras.getString("pin2");
            this.mPin2 = string;
            processPin2(string);
        } else {
            log("onActivityResult: CANCELLED");
            displayProgress(false);
            finish();
        }
    }

    @Override // com.android.phone.settings.fdn.BaseFdnContactScreen
    protected void resolveIntent() {
        super.resolveIntent();
    }

    private void deleteContact() {
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
        Uri contentUri = FdnList.getContentUri(this.mSubscriptionInfoHelper);
        BaseFdnContactScreen.QueryHandler queryHandler = new BaseFdnContactScreen.QueryHandler(getContentResolver());
        this.mQueryHandler = queryHandler;
        queryHandler.startDelete(0, null, contentUri, sb.toString(), null);
        displayProgress(true);
    }

    @Override // com.android.phone.settings.fdn.BaseFdnContactScreen
    protected void handleResult(boolean z) {
        if (z) {
            log("handleResult: success!");
            showStatus(getResources().getText(R.string.fdn_contact_deleted));
        } else {
            log("handleResult: failed!");
            showStatus(getResources().getText(R.string.pin2_invalid));
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.settings.fdn.DeleteFdnContactScreen$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleResult$0();
            }
        }, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleResult$0() {
        finish();
    }

    @Override // com.android.phone.settings.fdn.BaseFdnContactScreen
    protected void pin2AuthenticationSucceed() {
        showStatus(getResources().getText(R.string.deleting_fdn_contact));
        deleteContact();
    }
}
