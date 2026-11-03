package com.android.phone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import com.android.internal.telephony.CommandException;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public abstract class TimeConsumingPreferenceActivity extends PreferenceActivity implements TimeConsumingPreferenceListener, DialogInterface.OnCancelListener {
    private final DialogInterface.OnClickListener mDismiss;
    private final DialogInterface.OnClickListener mDismissAndFinish;
    private final boolean DBG = true;
    private final ArrayList mBusyList = new ArrayList();
    protected boolean mIsForeground = false;

    public void onUpdated(Preference preference) {
    }

    public TimeConsumingPreferenceActivity() {
        this.mDismiss = new DismissOnClickListener();
        this.mDismissAndFinish = new DismissAndFinishOnClickListener();
    }

    class DismissOnClickListener implements DialogInterface.OnClickListener {
        private DismissOnClickListener(TimeConsumingPreferenceActivity timeConsumingPreferenceActivity) {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    class DismissAndFinishOnClickListener implements DialogInterface.OnClickListener {
        private DismissAndFinishOnClickListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            TimeConsumingPreferenceActivity.this.finish();
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        int i2;
        if (i == 100 || i == 200) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getText(R.string.updating_title));
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
            if (i == 100) {
                progressDialog.setCancelable(true);
                progressDialog.setOnCancelListener(this);
                progressDialog.setMessage(getText(R.string.reading_settings));
                return progressDialog;
            }
            if (i != 200) {
                return null;
            }
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getText(R.string.updating_settings));
            return progressDialog;
        }
        if (i != 400 && i != 500 && i != 300 && i != 600 && i != 700 && i != 800 && i != 900 && i != 1000 && i != 10000) {
            return null;
        }
        AlertDialog.Builder builderMakeAlertDialogBuilder = FrameworksUtils.makeAlertDialogBuilder(this);
        int i3 = R.string.error_updating_title;
        if (i == 400) {
            i2 = R.string.response_error;
            builderMakeAlertDialogBuilder.setPositiveButton(R.string.close_dialog, this.mDismiss);
        } else if (i == 500) {
            i2 = R.string.radio_off_error;
            builderMakeAlertDialogBuilder.setPositiveButton(R.string.close_dialog, this.mDismissAndFinish);
        } else if (i == 600) {
            i2 = R.string.fdn_check_failure;
            builderMakeAlertDialogBuilder.setPositiveButton(R.string.close_dialog, this.mDismiss);
        } else if (i == 700) {
            i2 = R.string.stk_cc_ss_to_dial_error;
            builderMakeAlertDialogBuilder.setPositiveButton(R.string.close_dialog, this.mDismiss);
        } else if (i == 800) {
            i2 = R.string.stk_cc_ss_to_ussd_error;
            builderMakeAlertDialogBuilder.setPositiveButton(R.string.close_dialog, this.mDismiss);
        } else if (i == 900) {
            i2 = R.string.stk_cc_ss_to_ss_error;
            builderMakeAlertDialogBuilder.setPositiveButton(R.string.close_dialog, this.mDismiss);
        } else if (i == 1000) {
            i2 = R.string.stk_cc_ss_to_dial_video_error;
            builderMakeAlertDialogBuilder.setPositiveButton(R.string.close_dialog, this.mDismiss);
        } else if (i == 10000) {
            builderMakeAlertDialogBuilder.setNeutralButton(R.string.close_dialog, this.mDismiss);
            i2 = android.R.string.permlab_getTasks;
        } else {
            i2 = R.string.exception_error;
            builderMakeAlertDialogBuilder.setPositiveButton(R.string.close_dialog, this.mDismiss);
        }
        builderMakeAlertDialogBuilder.setTitle(getText(i3));
        builderMakeAlertDialogBuilder.setMessage(getText(i2));
        builderMakeAlertDialogBuilder.setCancelable(false);
        AlertDialog alertDialogCreate = builderMakeAlertDialogBuilder.create();
        alertDialogCreate.getWindow().addFlags(4);
        return alertDialogCreate;
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        this.mIsForeground = true;
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        this.mIsForeground = false;
    }

    @Override // com.android.phone.TimeConsumingPreferenceListener
    public void onStarted(Preference preference, boolean z) {
        dumpState();
        Log.i("TimeConsumingPreferenceActivity", "onStarted, preference=" + preference.getKey() + ", reading=" + z);
        this.mBusyList.add(preference.getKey());
        if (this.mIsForeground) {
            if (z) {
                showDialog(100);
            } else {
                showDialog(200);
            }
        }
    }

    public void onFinished(Preference preference, boolean z) {
        dumpState();
        Log.i("TimeConsumingPreferenceActivity", "onFinished, preference=" + preference.getKey() + ", reading=" + z);
        this.mBusyList.remove(preference.getKey());
        if (this.mBusyList.isEmpty()) {
            if (z) {
                dismissDialogSafely(100);
            } else {
                dismissDialogSafely(200);
            }
        }
        preference.setEnabled(true);
    }

    @Override // com.android.phone.TimeConsumingPreferenceListener
    public void onError(Preference preference, int i) {
        dumpState();
        Log.i("TimeConsumingPreferenceActivity", "onError, preference=" + preference.getKey() + ", error=" + i);
        if (this.mIsForeground) {
            showDialog(i);
        }
        if (i != 400) {
            preference.setEnabled(false);
        }
    }

    @Override // com.android.phone.TimeConsumingPreferenceListener
    public void onException(Preference preference, CommandException commandException) {
        Log.i("TimeConsumingPreferenceActivity", "onError, preference=" + preference.getKey() + ", exception=" + commandException);
        if (commandException.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE) {
            onError(preference, 600);
            return;
        }
        if (commandException.getCommandError() == CommandException.Error.RADIO_NOT_AVAILABLE) {
            onError(preference, ResponseObject.RELAY_FAILED_CODE);
            return;
        }
        if (commandException.getCommandError() == CommandException.Error.SS_MODIFIED_TO_DIAL) {
            onError(preference, 700);
            return;
        }
        if (commandException.getCommandError() == CommandException.Error.SS_MODIFIED_TO_DIAL_VIDEO) {
            onError(preference, 1000);
            return;
        }
        if (commandException.getCommandError() == CommandException.Error.SS_MODIFIED_TO_USSD) {
            onError(preference, 800);
            return;
        }
        if (commandException.getCommandError() == CommandException.Error.SS_MODIFIED_TO_SS) {
            onError(preference, 900);
            return;
        }
        if (commandException.getCommandError() == CommandException.Error.PASSWORD_INCORRECT) {
            onError(preference, 10000);
        } else if (commandException.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED) {
            preference.setEnabled(false);
            Log.i("TimeConsumingPreferenceActivity", "onError, suppress error dialog as not supported");
        } else {
            onError(preference, MiRilHook.HOOK_ID_START_URC_BY_AT);
        }
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        dumpState();
        finish();
    }

    protected void dismissDialogSafely(int i) {
        try {
            dismissDialog(i);
        } catch (IllegalArgumentException unused) {
        }
    }

    void dumpState() {
        Log.d("TimeConsumingPreferenceActivity", "dumpState begin");
        ArrayList arrayList = this.mBusyList;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            Log.d("TimeConsumingPreferenceActivity", "mBusyList: key=" + ((String) obj));
        }
        Log.d("TimeConsumingPreferenceActivity", "dumpState end");
    }
}
