package com.android.internal.telephony;

import android.app.ActivityThread;
import android.os.Binder;
import android.os.ServiceManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import miui.telephony.SubscriptionInfo;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class IccCardActivateHelper {
    private static final int INVALID_SUBSCRIPTION_ID = -1;
    private static final String LOG_TAG = "IccCardActivateHelper";

    public static boolean isActivate(int slotId) {
        long token = Binder.clearCallingIdentity();
        boolean ret = false;
        try {
            try {
                if (TelephonyManager.getDefault().canReadPhoneState(ActivityThread.currentPackageName(), "isActivate")) {
                    Phone phone = PhoneFactory.getPhone(slotId);
                    SubscriptionManager subMgr = SubscriptionManager.from(phone.getContext());
                    if (subMgr == null) {
                        return false;
                    }
                    int subId = getSubIdFromSlotId(slotId);
                    if (subId != -1) {
                        ret = subMgr.isActiveSubscriptionId(subId);
                    }
                    Binder.restoreCallingIdentity(token);
                }
                return ret;
            } catch (Exception e) {
                Rlog.w(LOG_TAG, "Failed to get activated state for slot" + slotId, e);
                return true;
            }
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    public static void setIccCardActivate(int slotId, boolean isActivate) {
        try {
            ISub iSub = getSubController();
            if (iSub == null) {
                return;
            }
            int subId = getSubIdFromSlotId(slotId);
            iSub.setUiccApplicationsEnabled(isActivate, subId);
        } catch (Exception e) {
            Rlog.w(LOG_TAG, "setIccCardActivate of slot " + slotId + " as " + isActivate, e);
        }
    }

    public static int getSubIdFromSlotId(int slotId) {
        for (SubscriptionInfo subInfo : miui.telephony.SubscriptionManager.getDefault().getAvailableSubscriptionInfoList()) {
            if (subInfo.getSlotId() == slotId) {
                return subInfo.getSubscriptionId();
            }
        }
        return -1;
    }

    private static ISub getSubController() {
        return ISub.Stub.asInterface(ServiceManager.getService("isub"));
    }
}
