package com.android.internal.telephony.subsidylock;

import android.content.Context;
import android.util.Log;
import com.android.internal.telephony.UniTelephonyInjectionFactory;

/* JADX INFO: loaded from: classes.dex */
public class SubsidyLockController {
    private static final String TAG = "SubsidyLockController";

    public boolean isDataSwitchAllowedForSubsidy() {
        return true;
    }

    public boolean isDisableSimAllowedByIccId(String str) {
        return true;
    }

    public boolean isDisableSimAllowedForSubsidy(int i) {
        return true;
    }

    public boolean isOperatorCardForSubsidy(int i) {
        return false;
    }

    public void popupDataEnabledForSubsidy(int i, int i2) {
    }

    public SubsidyLockController(Context context) {
        Log.d(TAG, "SubsidyLockController created");
    }

    public static SubsidyLockController getInstance() {
        return UniTelephonyInjectionFactory.getInstance().getSubsidyLockController();
    }
}
