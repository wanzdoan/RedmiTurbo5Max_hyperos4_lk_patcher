package com.android.phone.euicc;

import android.content.Intent;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class EuiccPrivilegedActionUiDispatcherActivity extends EuiccUiDispatcherActivity {
    @Override // com.android.phone.euicc.EuiccUiDispatcherActivity
    protected Intent getEuiccUiIntent() {
        String action = getIntent().getAction();
        if (action == null) {
            Log.w("EuiccPrivUiDispatcher", "No action is specified in the intent");
            return null;
        }
        Intent intent = new Intent();
        intent.putExtras(getIntent());
        action.hashCode();
        switch (action) {
            case "android.telephony.euicc.action.RENAME_SUBSCRIPTION_PRIVILEGED":
                intent.setAction("android.service.euicc.action.RENAME_SUBSCRIPTION_PRIVILEGED");
                return intent;
            case "android.telephony.euicc.action.DELETE_SUBSCRIPTION_PRIVILEGED":
                intent.setAction("android.service.euicc.action.DELETE_SUBSCRIPTION_PRIVILEGED");
                return intent;
            case "android.telephony.euicc.action.TOGGLE_SUBSCRIPTION_PRIVILEGED":
                intent.setAction("android.service.euicc.action.TOGGLE_SUBSCRIPTION_PRIVILEGED");
                return intent;
            default:
                Log.w("EuiccPrivUiDispatcher", "Unsupported action: ".concat(action));
                return null;
        }
    }
}
