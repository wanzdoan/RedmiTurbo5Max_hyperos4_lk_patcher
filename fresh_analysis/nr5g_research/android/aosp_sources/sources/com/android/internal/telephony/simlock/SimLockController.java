package com.android.internal.telephony.simlock;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.android.internal.telephony.UniTelephonyInjectionFactory;

/* JADX INFO: loaded from: classes.dex */
public class SimLockController extends Handler {
    private static final String TAG = "SimLockController";

    public int getPreferredPrimaryCardForSimLock() {
        return -1;
    }

    public int getRestrictedNetTypePhoneId() {
        return -1;
    }

    public long getRestrictedNetworkTypeBitMaskForSimLock() {
        return 0L;
    }

    public boolean isCallAllowedForSlot(int i) {
        return true;
    }

    public boolean isDataAllowedForSlot(int i) {
        return true;
    }

    public boolean isDefaultDataCardSwitchAllowed() {
        return true;
    }

    public boolean isSmsAllowedForSlot(int i) {
        return true;
    }

    public boolean isWhiteListCard(int i) {
        return true;
    }

    public boolean restrictedNetworkTypeNeeded(int i) {
        return false;
    }

    public SimLockController(Context context) {
        Log.d(TAG, "SimLockController created");
    }

    public static SimLockController getInstance() {
        return UniTelephonyInjectionFactory.getInstance().getSimLockController();
    }
}
