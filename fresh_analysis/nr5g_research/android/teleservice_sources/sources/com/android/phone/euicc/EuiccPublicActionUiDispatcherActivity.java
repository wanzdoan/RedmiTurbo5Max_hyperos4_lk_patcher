package com.android.phone.euicc;

import android.content.Intent;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class EuiccPublicActionUiDispatcherActivity extends EuiccUiDispatcherActivity {
    @Override // com.android.phone.euicc.EuiccUiDispatcherActivity
    protected Intent getEuiccUiIntent() {
        String action = getIntent().getAction();
        if (action == null) {
            Log.w("EuiccPublicActionUiDispatcherActivity", "No action is specified in the intent");
            return null;
        }
        Intent intent = new Intent();
        intent.putExtras(getIntent());
        action.hashCode();
        if (!action.equals("android.telephony.euicc.action.START_EUICC_ACTIVATION")) {
            Log.w("EuiccPublicActionUiDispatcherActivity", "Unsupported action: ".concat(action));
            return null;
        }
        intent.setAction("android.service.euicc.action.START_EUICC_ACTIVATION");
        return intent;
    }
}
