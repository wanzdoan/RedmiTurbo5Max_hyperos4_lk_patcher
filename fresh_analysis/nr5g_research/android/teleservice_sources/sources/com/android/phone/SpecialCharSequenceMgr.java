package com.android.phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class SpecialCharSequenceMgr {
    static boolean handleChars(Context context, String str) {
        return handleChars(context, str, null);
    }

    static boolean handleChars(Context context, String str, Activity activity) {
        String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        return handleIMEIDisplay(context, strStripSeparators) || handleRegulatoryInfoDisplay(context, strStripSeparators) || handlePinEntry(context, strStripSeparators, activity) || handleAdnEntry(context, strStripSeparators) || handleSecretCode(strStripSeparators);
    }

    static boolean handleCharsForLockedDevice(Context context, String str, Activity activity) {
        return handlePinEntry(context, PhoneNumberUtils.stripSeparators(str), activity);
    }

    private static boolean handleSecretCode(String str) {
        int length = str.length();
        if (length <= 8 || !str.startsWith("*#*#") || !str.endsWith("#*#*")) {
            return false;
        }
        PhoneGlobals.getPhone().sendDialerSpecialCode(str.substring(4, length - 4));
        return true;
    }

    private static boolean handleAdnEntry(Context context, String str) {
        int length;
        if (!PhoneGlobals.getInstance().getKeyguardManager().inKeyguardRestrictedInputMode() && (length = str.length()) > 1 && length < 5 && str.endsWith("#")) {
            try {
                int i = Integer.parseInt(str.substring(0, length - 1));
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.SimContacts");
                intent.setFlags(268435456);
                intent.putExtra("index", i);
                PhoneGlobals.getInstance().startActivityAsUser(intent, UserHandle.CURRENT);
                return true;
            } catch (NumberFormatException unused) {
            }
        }
        return false;
    }

    private static IccCardConstants.State getSimState(int i, Context context) {
        int simState = TelephonyManager.from(context).getSimState(i);
        try {
            return IccCardConstants.State.intToState(simState);
        } catch (IllegalArgumentException unused) {
            Log.w("PhoneGlobals", "Unknown sim state: " + simState);
            return IccCardConstants.State.UNKNOWN;
        }
    }

    private static int getNextSubIdForState(IccCardConstants.State state, Context context) {
        List<SubscriptionInfo> activeSubscriptionInfoList = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null) {
            activeSubscriptionInfoList = new ArrayList<>();
        }
        int i = -1;
        int simSlotIndex = Integer.MAX_VALUE;
        for (int i2 = 0; i2 < activeSubscriptionInfoList.size(); i2++) {
            SubscriptionInfo subscriptionInfo = activeSubscriptionInfoList.get(i2);
            int subscriptionId = subscriptionInfo.getSubscriptionId();
            if (state == getSimState(subscriptionInfo.getSimSlotIndex(), context) && simSlotIndex > subscriptionInfo.getSimSlotIndex()) {
                simSlotIndex = subscriptionInfo.getSimSlotIndex();
                i = subscriptionId;
            }
        }
        return i;
    }

    private static boolean handlePinEntry(Context context, String str, Activity activity) {
        int nextSubIdForState;
        Phone phone;
        if ((!str.startsWith("**04") && !str.startsWith("**05")) || !str.endsWith("#")) {
            return false;
        }
        if (((UserManager) activity.getSystemService("user")).isSystemUser()) {
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            if (str.startsWith("**04")) {
                nextSubIdForState = getNextSubIdForState(IccCardConstants.State.PIN_REQUIRED, context);
            } else {
                nextSubIdForState = getNextSubIdForState(IccCardConstants.State.PUK_REQUIRED, context);
                if (!SubscriptionManager.isValidSubscriptionId(nextSubIdForState)) {
                    nextSubIdForState = getNextSubIdForState(IccCardConstants.State.PIN_REQUIRED, context);
                }
            }
            if (SubscriptionManager.isValidSubscriptionId(nextSubIdForState)) {
                log("get phone with subId: " + nextSubIdForState);
                phone = PhoneGlobals.getPhone(nextSubIdForState);
            } else {
                log("get default phone");
                phone = PhoneGlobals.getPhone();
            }
            boolean zHandlePinMmi = phone.handlePinMmi(str);
            int simState = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getSimState();
            if (zHandlePinMmi && str.startsWith("**05") && simState != 5) {
                phoneGlobals.setPukEntryActivity(activity);
            }
            return zHandlePinMmi;
        }
        AlertDialog alertDialogCreate = FrameworksUtils.makeAlertDialogBuilder(context).setMessage(R.string.pin_puk_system_user_only).setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null).setCancelable(true).create();
        alertDialogCreate.show();
        alertDialogCreate.getWindow().addFlags(4);
        alertDialogCreate.getWindow().addFlags(524288);
        return true;
    }

    private static boolean handleIMEIDisplay(Context context, String str) {
        if (!str.equals("*#06#")) {
            return false;
        }
        showDeviceIdPanel(context);
        return true;
    }

    private static void showDeviceIdPanel(Context context) {
        Phone phone = PhoneGlobals.getPhone();
        int deviceIdLabel = com.android.internal.telephony.TelephonyCapabilities.getDeviceIdLabel(phone);
        AlertDialog alertDialogCreate = FrameworksUtils.makeAlertDialogBuilder(context).setTitle(deviceIdLabel).setMessage(phone.getDeviceId()).setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null).setCancelable(false).create();
        alertDialogCreate.getWindow().setType(2007);
        alertDialogCreate.show();
    }

    private static boolean handleRegulatoryInfoDisplay(Context context, String str) {
        if (!str.equals("*#07#")) {
            return false;
        }
        log("handleRegulatoryInfoDisplay() sending intent to settings app");
        try {
            context.startActivityAsUser(new Intent("android.settings.SHOW_REGULATORY_INFO"), UserHandle.CURRENT);
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e("PhoneGlobals", "startActivityAsUser() failed: " + e);
            return true;
        }
    }

    private static void log(String str) {
        Log.d("PhoneGlobals", "[SpecialCharSequenceMgr] " + str);
    }
}
