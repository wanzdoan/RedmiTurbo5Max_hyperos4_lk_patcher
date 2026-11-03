package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.widget.TextView;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class MiuiAisSimLockManager {
    public static final boolean IS_AIS_SIMLOCK_BUILD = "100".equals(SystemProperties.get("persist.radio.ais_sim_lock", ""));
    private static MiuiAisSimLockManager sInstance;
    private Context mContext;
    private final SimLockBroadCastReceiver mReceiver;
    private HashMap mSimDialogs = new HashMap();

    private MiuiAisSimLockManager(Context context) {
        SimLockBroadCastReceiver simLockBroadCastReceiver = new SimLockBroadCastReceiver();
        this.mReceiver = simLockBroadCastReceiver;
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        this.mContext.registerReceiver(simLockBroadCastReceiver, intentFilter, 2);
    }

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new MiuiAisSimLockManager(context);
        }
    }

    public static MiuiAisSimLockManager getInstance() {
        return sInstance;
    }

    public boolean isRestricted(Phone phone) {
        if (!IS_AIS_SIMLOCK_BUILD) {
            Rlog.d("MiuiAisSimLockManager", "isRestricted ais is off");
            return false;
        }
        if (phone != null && phone.getIccCard() != null && phone.getIccCard().getIccRecords() != null) {
            String operatorNumeric = phone.getIccCard().getIccRecords().getOperatorNumeric();
            Rlog.d("MiuiAisSimLockManager", "mccmnc :" + operatorNumeric);
            boolean booleanForPhoneId = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "is_sim_restricted", true);
            if (!TextUtils.isEmpty(operatorNumeric) && booleanForPhoneId) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void autoCheckAndShowSimLockDialog() {
        for (SubscriptionInfo subscriptionInfo : SubscriptionManager.getDefault().getSubscriptionInfoList()) {
            int slotId = subscriptionInfo.getSlotId();
            if (subscriptionInfo.getIccId() != null) {
                Phone phone = PhoneFactory.getPhone(slotId);
                if (isRestricted(phone)) {
                    showSimLockDialog(phone);
                }
            }
        }
    }

    public void showSimLockDialog(Phone phone) {
        if (phone == null) {
            return;
        }
        int phoneId = phone.getPhoneId();
        AlertDialog alertDialog = (AlertDialog) this.mSimDialogs.get(Integer.valueOf(phoneId));
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                return;
            }
            alertDialog.dismiss();
            this.mSimDialogs.remove(Integer.valueOf(phoneId));
        }
        AlertDialog alertDialogCreateDialog = createDialog(phoneId);
        this.mSimDialogs.put(Integer.valueOf(phoneId), alertDialogCreateDialog);
        alertDialogCreateDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSimDialogs(int i) {
        AlertDialog alertDialog = (AlertDialog) this.mSimDialogs.get(Integer.valueOf(i));
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mSimDialogs.remove(Integer.valueOf(i));
        }
    }

    private AlertDialog createDialog(final int i) {
        String str = String.format(this.mContext.getResources().getString(286196565), Integer.valueOf(i + 1));
        TextView textView = new TextView(this.mContext);
        textView.setText(286196564);
        textView.setGravity(3);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this.mContext, miuix.appcompat.R$style.Theme_Light_Dialog_Alert).setTitle(str).setView(textView).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiAisSimLockManager.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                MiuiAisSimLockManager.this.removeSimDialogs(i);
            }
        }).setCancelable(false).create();
        alertDialogCreate.getWindow().setType(2003);
        return alertDialogCreate;
    }

    final class SimLockBroadCastReceiver extends BroadcastReceiver {
        private SimLockBroadCastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                String stringExtra = intent.getStringExtra("ss");
                int intExtra = intent.getIntExtra("slot", -1);
                Rlog.d("MiuiAisSimLockManager", "newIccState:" + stringExtra + ",slotId =" + intExtra);
                if (intExtra < 0 || intExtra >= MiuiPhoneUtils.PHONE_COUNT) {
                    return;
                }
                Phone phone = PhoneFactory.getPhone(intExtra);
                if ("ABSENT".equals(stringExtra)) {
                    if (phone != null) {
                        MiuiAisSimLockManager.this.removeSimDialogs(phone.getPhoneId());
                        return;
                    }
                    return;
                } else {
                    if ("LOADED".equals(stringExtra) && MiuiAisSimLockManager.this.isRestricted(phone)) {
                        MiuiAisSimLockManager.this.showSimLockDialog(phone);
                        return;
                    }
                    return;
                }
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                boolean booleanExtra = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                Rlog.d("MiuiAisSimLockManager", "airplaneModeOn =" + booleanExtra);
                if (booleanExtra) {
                    return;
                }
                MiuiAisSimLockManager.this.autoCheckAndShowSimLockDialog();
            }
        }
    }
}
