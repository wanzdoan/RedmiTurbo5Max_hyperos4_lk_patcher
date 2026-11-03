package com.android.phone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.util.List;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class MiuiEsimRoamingDialogActivity extends AppCompatActivity {
    private long getIntervalForStage(int i) {
        return i < 1 ? 21600000L : 259200000L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (shouldShowDialog()) {
            Log.d("MiuiEsimRoamingDialogActivity", "eSIM roaming dialog should be shown");
            showEsimRoamingDialog();
        } else {
            Log.d("MiuiEsimRoamingDialogActivity", "eSIM roaming dialog should not be shown");
            finish();
        }
    }

    private boolean shouldShowDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("esim_roaming_prefs", 0);
        if (sharedPreferences.getBoolean("dialog_disabled", false)) {
            Log.d("MiuiEsimRoamingDialogActivity", "eSIM roaming dialog is disabled by user");
            return false;
        }
        if (!isEsimRoamingEligible()) {
            Log.d("MiuiEsimRoamingDialogActivity", "not roaming or no matching inactive eSIM");
            return false;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = sharedPreferences.getLong("next_show_time", 0L);
        int i = sharedPreferences.getInt("last_show_stage", 0);
        int i2 = sharedPreferences.getInt("boot_count", -1);
        int i3 = Settings.Global.getInt(getContentResolver(), "boot_count", 0);
        Log.d("MiuiEsimRoamingDialogActivity", "shouldShowDialog: currentTime=" + jElapsedRealtime + ", nextShowTime=" + j + ", lastStage=" + i + ", savedBootCount=" + i2 + ", currentBootCount=" + i3);
        if (j == 0) {
            return true;
        }
        if (i2 == i3) {
            return jElapsedRealtime >= j;
        }
        Log.d("MiuiEsimRoamingDialogActivity", "Device rebooted, rescheduling dialog");
        long intervalForStage = jElapsedRealtime + getIntervalForStage(i);
        sharedPreferences.edit().putLong("next_show_time", intervalForStage).putInt("boot_count", i3).apply();
        rescheduleAlarm(intervalForStage);
        return false;
    }

    private boolean isEsimRoamingEligible() {
        int mcc;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TelephonyManager.class);
        if (telephonyManager == null || !telephonyManager.isNetworkRoaming()) {
            Log.d("MiuiEsimRoamingDialogActivity", "isEsimRoamingEligible: not roaming");
            return false;
        }
        String networkOperator = telephonyManager.getNetworkOperator();
        if (TextUtils.isEmpty(networkOperator) || networkOperator.length() < 3) {
            Log.d("MiuiEsimRoamingDialogActivity", "isEsimRoamingEligible: invalid networkOperator");
            return false;
        }
        try {
            String string = Integer.toString(Integer.parseInt(networkOperator.substring(0, 3)));
            List<SubscriptionInfo> allSubscriptionInfoList = SubscriptionManager.getDefault().getAllSubscriptionInfoList();
            if (allSubscriptionInfoList == null || allSubscriptionInfoList.isEmpty()) {
                Log.d("MiuiEsimRoamingDialogActivity", "isEsimRoamingEligible: no subscriptions");
                return false;
            }
            for (SubscriptionInfo subscriptionInfo : allSubscriptionInfoList) {
                if (subscriptionInfo != null && subscriptionInfo.isEmbedded() && (mcc = subscriptionInfo.getMcc()) != 0 && subscriptionInfo.getSlotId() < 0 && string.equals(Integer.toString(mcc))) {
                    Log.d("MiuiEsimRoamingDialogActivity", "isEsimRoamingEligible: found matching inactive eSIM, mcc=" + mcc);
                    return true;
                }
            }
            Log.d("MiuiEsimRoamingDialogActivity", "isEsimRoamingEligible: no matching inactive eSIM found");
            return false;
        } catch (NumberFormatException unused) {
            Log.e("MiuiEsimRoamingDialogActivity", "isEsimRoamingEligible: failed to parse MCC from ".concat(networkOperator));
            return false;
        }
    }

    private void rescheduleAlarm(long j) {
        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService("alarm");
            if (alarmManager == null) {
                Log.e("MiuiEsimRoamingDialogActivity", "rescheduleAlarm: alarmManager is null");
                return;
            }
            Intent intent = new Intent(this, (Class<?>) MiuiEsimRoamingCheckReceiver.class);
            intent.setAction("miui.intent.action.ESIM_ROAMING_CHECK");
            alarmManager.setExactAndAllowWhileIdle(3, j, PendingIntent.getBroadcast(this, 0, intent, 201326592));
            Log.d("MiuiEsimRoamingDialogActivity", "rescheduleAlarm: nextShowTime=" + j);
        } catch (Exception e) {
            Log.e("MiuiEsimRoamingDialogActivity", "Failed to reschedule alarm", e);
        }
    }

    private void showEsimRoamingDialog() {
        Log.d("MiuiEsimRoamingDialogActivity", "showEsimRoamingDialog -- enter");
        scheduleNextDialog();
        new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setTitle(R.string.esim_roaming_dialog_title).setMessage(R.string.esim_roaming_dialog_msg).setCheckBox(false, getString(R.string.checkbox_do_not_show_again)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiEsimRoamingDialogActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("MiuiEsimRoamingDialogActivity", "User clicked OK, Enter euicc manager");
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.telephony.euicc.action.MANAGE_EMBEDDED_SUBSCRIPTIONS");
                    intent.setFlags(268435456);
                    intent.putExtra("isRoamingDialogEnter", true);
                    MiuiEsimRoamingDialogActivity.this.startActivity(intent);
                } catch (Exception e) {
                    Log.e("MiuiEsimRoamingDialogActivity", "Enter euicc manager failed " + e);
                }
                MiuiEsimRoamingDialogActivity.this.handleUserSelection(dialogInterface);
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiEsimRoamingDialogActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("MiuiEsimRoamingDialogActivity", "User clicked cancel");
                MiuiEsimRoamingDialogActivity.this.handleUserSelection(dialogInterface);
            }
        }).setCancelable(false).create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserSelection(DialogInterface dialogInterface) {
        Log.d("MiuiEsimRoamingDialogActivity", "handleUserSelection -- enter");
        if (((AlertDialog) dialogInterface).isChecked()) {
            Log.d("MiuiEsimRoamingDialogActivity", "User selected 'do not show again', disabling dialog and canceling alarm");
            cancelScheduledDialog();
            getSharedPreferences("esim_roaming_prefs", 0).edit().putBoolean("dialog_disabled", true).putLong("next_show_time", 0L).apply();
        }
        dialogInterface.dismiss();
        finish();
    }

    private void cancelScheduledDialog() {
        Intent intent = new Intent(this, (Class<?>) MiuiEsimRoamingCheckReceiver.class);
        intent.setAction("miui.intent.action.ESIM_ROAMING_CHECK");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, intent, 201326592);
        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService("alarm");
            if (alarmManager == null) {
                return;
            }
            alarmManager.cancel(broadcast);
            Log.d("MiuiEsimRoamingDialogActivity", "Canceled scheduled eSIM roaming dialog alarm");
        } catch (Exception e) {
            Log.e("MiuiEsimRoamingDialogActivity", "Failed to cancel scheduled dialog", e);
        } finally {
            broadcast.cancel();
        }
    }

    private void scheduleNextDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("esim_roaming_prefs", 0);
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        int i = sharedPreferences.getInt("last_show_stage", 0);
        int i2 = i + 1;
        int i3 = Settings.Global.getInt(getContentResolver(), "boot_count", 0);
        Log.d("MiuiEsimRoamingDialogActivity", "scheduleNextDialog: lastStage=" + i + ", newStage=" + i2);
        long j = jElapsedRealtime + (i2 != 1 ? 259200000L : 21600000L);
        editorEdit.putLong("next_show_time", j);
        editorEdit.putInt("last_show_stage", i2);
        editorEdit.putInt("boot_count", i3);
        editorEdit.apply();
        rescheduleAlarm(j);
        Log.d("MiuiEsimRoamingDialogActivity", "scheduleNextDialog: stage=" + i2 + ", nextShowTime=" + j);
    }
}
