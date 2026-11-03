package com.android.internal.telephony;

import android.content.Context;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class QtiCallTracker {
    public static boolean isPseudoDsdaCall(Phone phone) {
        Context context;
        Phone otherOffHookPhone;
        if (phone == null || (context = phone.getContext()) == null || TelephonyManager.from(context).getActiveModemCount() <= 1 || (otherOffHookPhone = getOtherOffHookPhone(phone)) == null) {
            return false;
        }
        if (containedInSimultaneousRestriction(phone.getDefaultPhone(), otherOffHookPhone)) {
            Log.d("QtiCallTracker", "containsPhoneAccount in simultaneous restriction, so do not treat as pseudo dsda");
            return false;
        }
        if (phone.getDefaultPhone().getState() == PhoneConstants.State.OFFHOOK) {
            return false;
        }
        Log.d("QtiCallTracker", "This is a pseudo dsda call");
        return true;
    }

    private static Phone getOtherOffHookPhone(Phone phone) {
        if (phone == null) {
            return null;
        }
        for (Phone phone2 : PhoneFactory.getPhones()) {
            if (phone2.getSubId() != phone.getSubId() && phone2.getState() == PhoneConstants.State.OFFHOOK) {
                return phone2;
            }
        }
        return null;
    }

    private static boolean containedInSimultaneousRestriction(Phone phone, Phone phone2) {
        if (phone2 == null || phone == null) {
            Log.d("QtiCallTracker", "Either offhook or ringing call is null, which is not expected");
            return false;
        }
        Context context = phone.getContext();
        if (context == null) {
            return false;
        }
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(TelecomManager.class);
        TelephonyManager telephonyManagerFrom = TelephonyManager.from(context);
        PhoneAccountHandle phoneAccountHandleForSubscriptionId = telephonyManagerFrom != null ? telephonyManagerFrom.getPhoneAccountHandleForSubscriptionId(phone.getSubId()) : null;
        PhoneAccountHandle phoneAccountHandleForSubscriptionId2 = telephonyManagerFrom != null ? telephonyManagerFrom.getPhoneAccountHandleForSubscriptionId(phone2.getSubId()) : null;
        if (phoneAccountHandleForSubscriptionId == null || phoneAccountHandleForSubscriptionId2 == null) {
            Log.d("QtiCallTracker", "Null phone account handle for ringing phone");
            return false;
        }
        PhoneAccount phoneAccount = telecomManager != null ? telecomManager.getPhoneAccount(phoneAccountHandleForSubscriptionId2) : null;
        if (phoneAccount == null || !phoneAccount.hasSimultaneousCallingRestriction() || !phoneAccount.getSimultaneousCallingRestriction().contains(phoneAccountHandleForSubscriptionId)) {
            return false;
        }
        Log.d("QtiCallTracker", "contains phone account in simultaneous calling restriction");
        return true;
    }
}
