package com.android.phone;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class ErrorDialogActivity extends Activity {
    private static final String TAG = "ErrorDialogActivity";

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        showDialog();
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void showDialog() {
        int i;
        final int managedProfileUserId = getManagedProfileUserId(getApplicationContext(), getApplicationContext().getUserId());
        if (managedProfileUserId == -10000) {
            Log.w(TAG, "Error dialog is only applicable to managed profile.");
            finish();
        }
        String smsRoleHolder = ((RoleManager) getBaseContext().getSystemService(RoleManager.class)).getSmsRoleHolder(managedProfileUserId);
        final Intent data = new Intent("android.intent.action.SENDTO").addCategory("android.intent.category.DEFAULT").addFlags(268435456).setData(Uri.parse("smsto:"));
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=messages"));
        boolean z = true;
        if (smsRoleHolder != null || canStartActivityAsUser(data, managedProfileUserId)) {
            i = R.string.send_from_work_profile_action_str;
        } else if (canStartActivityAsUser(intent, managedProfileUserId)) {
            i = R.string.install_messages_on_work_profile_action_str;
            data = intent;
        } else {
            z = false;
            data = null;
            i = 0;
        }
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.ErrorDialogActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                ErrorDialogActivity errorDialogActivity = ErrorDialogActivity.this;
                if (i2 == -1) {
                    errorDialogActivity.switchToManagedProfile(managedProfileUserId, data);
                    ErrorDialogActivity.this.finish();
                } else {
                    errorDialogActivity.finish();
                }
            }
        };
        AlertDialog.Builder onCancelListener = new AlertDialog.Builder(this).setTitle(R.string.send_from_work_profile_title).setMessage(R.string.send_from_work_profile_description).setNegativeButton(R.string.send_from_work_profile_cancel, onClickListener).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.phone.ErrorDialogActivity.2
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                ErrorDialogActivity.this.finish();
            }
        });
        if (z) {
            onCancelListener.setPositiveButton(i, onClickListener);
        }
        onCancelListener.show();
    }

    private boolean canStartActivityAsUser(Intent intent, int i) {
        return !getPackageManager().queryIntentActivitiesAsUser(intent, PackageManager.ResolveInfoFlags.of(0L), i).isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchToManagedProfile(int i, Intent intent) {
        try {
            startActivityAsUser(intent, ActivityOptions.makeOpenCrossProfileAppsAnimation().toBundle(), UserHandle.of(i));
        } catch (Exception e) {
            Log.e(TAG, "Failed to switch to managed profile.", e);
        }
    }

    private static int getManagedProfileUserId(Context context, int i) {
        for (UserInfo userInfo : ((UserManager) context.getSystemService(UserManager.class)).getProfiles(i)) {
            if (userInfo.id != i && userInfo.isManagedProfile()) {
                return userInfo.id;
            }
        }
        return -10000;
    }
}
