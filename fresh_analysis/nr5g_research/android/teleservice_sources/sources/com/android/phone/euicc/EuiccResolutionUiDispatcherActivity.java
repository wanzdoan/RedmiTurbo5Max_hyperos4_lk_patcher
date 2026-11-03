package com.android.phone.euicc;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.ArraySet;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class EuiccResolutionUiDispatcherActivity extends EuiccUiDispatcherActivity {
    private static final ArraySet RESOLUTION_ACTIONS;

    static {
        ArraySet arraySet = new ArraySet();
        RESOLUTION_ACTIONS = arraySet;
        arraySet.add("android.service.euicc.action.RESOLVE_DEACTIVATE_SIM");
        arraySet.add("android.service.euicc.action.RESOLVE_NO_PRIVILEGES");
        arraySet.add("android.service.euicc.action.RESOLVE_RESOLVABLE_ERRORS");
    }

    @Override // com.android.phone.euicc.EuiccUiDispatcherActivity
    protected Intent getEuiccUiIntent() {
        String action = getIntent().getAction();
        if (!"android.telephony.euicc.action.RESOLVE_ERROR".equals(action)) {
            Log.w("EuiccResUiDispatcher", "Unsupported action: " + action);
            return null;
        }
        String stringExtra = getIntent().getStringExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_ACTION");
        if (!RESOLUTION_ACTIONS.contains(stringExtra)) {
            Log.w("EuiccResUiDispatcher", "Unknown resolution action: " + stringExtra);
            return null;
        }
        Intent intent = new Intent(stringExtra);
        intent.putExtras(getIntent());
        return intent;
    }

    @Override // com.android.phone.euicc.EuiccUiDispatcherActivity
    protected void onDispatchFailure() {
        PendingIntent pendingIntent = (PendingIntent) getIntent().getParcelableExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_CALLBACK_INTENT");
        if (pendingIntent != null) {
            try {
                pendingIntent.send(2);
            } catch (PendingIntent.CanceledException unused) {
            }
        }
    }
}
