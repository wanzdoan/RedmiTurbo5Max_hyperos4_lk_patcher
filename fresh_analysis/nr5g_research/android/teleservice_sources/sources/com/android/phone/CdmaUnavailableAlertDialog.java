package com.android.phone;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.telephony.Rlog;
import com.android.phone.module.ITelephonyModule;
import java.util.List;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public abstract class CdmaUnavailableAlertDialog extends AppCompatActivity {
    private static int sLastDataSlot = SubscriptionManager.INVALID_SLOT_ID;

    public static void showDialogIfNeeded() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (sLastDataSlot == defaultDataSlotId) {
            boolean z = true;
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT && z; i++) {
                z = DefaultSimManager.getInstance().getSimInsertStates(i) == 0;
            }
            if (z) {
                return;
            }
        }
        sLastDataSlot = defaultDataSlotId;
        List subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
        if (subscriptionInfoList == null || subscriptionInfoList.size() <= 1) {
            Rlog.i("CdmaUnavailableAlertDialog", "showCdmaUnavailableDialogIfNeeded return for sub not ready");
            return;
        }
        int i2 = MiuiPhoneUtils.PHONE_COUNT;
        int[] iArr = new int[i2];
        for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
            int phoneType = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getPhoneType(i3);
            iArr[i3] = phoneType;
            if (phoneType == 0) {
                Rlog.i("CdmaUnavailableAlertDialog", "showCdmaUnavailableDialogIfNeeded return for sim application not ready");
                return;
            }
        }
        for (int i4 = 0; i4 < i2; i4++) {
            if (i4 != defaultDataSlotId && iArr[i4] == 2 && ModemProvider.getInstance().getDefaultVoiceSlotId() == i4) {
                Rlog.d("CdmaUnavailableAlertDialog", "showCdmaUnavailableDialogIfNeeded reset preferred voice slot");
                SubscriptionManager.getDefault().setDefaultVoiceSlotId(SubscriptionManager.INVALID_SLOT_ID);
            }
            if (iArr[defaultDataSlotId] == 1 && iArr[i4] == 2) {
                showCdmaUnavailableDialog();
            }
        }
    }

    private static void showCdmaUnavailableDialog() {
        Intent intent = new Intent();
        intent.setClass(PhoneGlobals.getInstance(), CdmaUnavailableAlertDialog.class);
        intent.addFlags(268435456);
        try {
            PhoneGlobals.getInstance().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Rlog.w("CdmaUnavailableAlertDialog", "startActivity failed anfe=" + e);
        }
    }
}
