package com.android.phone;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import com.xiaomi.telephony.EuiccOsuManagerBase;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.ProgressDialog;

/* JADX INFO: loaded from: classes.dex */
public class MiuiOsuDialogActivity extends AppCompatActivity implements EuiccOsuManagerBase.OsuCompleteCallback {
    private static String LOG_TAG = "MiuiOsuDialogActivity";
    public AppCompatActivity mActivity;
    private OsuUpdateTask mOsuUpdateTask;
    private AlertDialog mDialog = null;
    private ProgressDialog mOsuProgressDialog = null;
    private boolean mIsFromNotification = false;
    private int updateResult = Integer.MIN_VALUE;
    private final Object mLock = new Object();
    private final Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.phone.MiuiOsuDialogActivity.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                if (MiuiOsuDialogActivity.this.mOsuProgressDialog != null) {
                    MiuiOsuDialogActivity.this.mOsuProgressDialog.setProgress(message.arg1);
                }
            } else {
                if (i != 2 || MiuiOsuDialogActivity.this.isFinishing() || MiuiOsuDialogActivity.this.isDestroyed()) {
                    return;
                }
                String str = (String) message.obj;
                MiuiOsuDialogActivity.this.dismissOsuProgressDialog();
                Toast.makeText(MiuiOsuDialogActivity.this, str, 0).show();
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent() == null) {
            return;
        }
        if (EuiccOsuManagerBase.getInstance() != null) {
            EuiccOsuManagerBase.getInstance().setOsuCompleteCallback(this);
        }
        this.mActivity = this;
        boolean booleanExtra = getIntent().getBooleanExtra("esim_os_update", false);
        this.mIsFromNotification = booleanExtra;
        if (booleanExtra) {
            log("triggered from notification");
            NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
            if (notificationManager != null) {
                notificationManager.cancel(6);
            }
        }
        showDialog(-1);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDialog = null;
        }
        String string = getResources().getString(R.string.osu_dialog_title);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setTitle(string).setMessage(getResources().getString(R.string.osu_dialog_message)).setCancelable(false).setPositiveButton(R.string.osu_dialog_ok_btn_text, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiOsuDialogActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                MiuiOsuDialogActivity.this.mOsuUpdateTask = new OsuUpdateTask();
                MiuiOsuDialogActivity.this.mOsuUpdateTask.execute(null, null);
            }
        }).setNegativeButton(R.string.osu_dialog_cancel_btn_text, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiOsuDialogActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    if (!MiuiOsuDialogActivity.this.mIsFromNotification) {
                        MiuiPhoneUtils.enterEsimManager(true);
                    }
                } finally {
                    MiuiOsuDialogActivity.this.finish();
                }
            }
        }).create();
        this.mDialog = alertDialogCreate;
        return alertDialogCreate;
    }

    class OsuUpdateTask extends AsyncTask {
        private OsuUpdateTask() {
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            MiuiOsuDialogActivity.this.showOsuProgressDialog();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voidArr) {
            int i;
            MiuiOsuDialogActivity.this.log("doInBackground...");
            if (EuiccOsuManagerBase.getInstance() == null) {
                return Boolean.FALSE;
            }
            int i2 = EuiccOsuManagerBase.getInstance().isEsimEnabledOnSlot(0) ? 7000 : 6000;
            MiuiOsuDialogActivity.this.log("waitTime = " + i2);
            EuiccOsuManagerBase.getInstance().executeUpdate();
            int[] iArrGenerateRandomProgressArray = MiuiOsuDialogActivity.this.generateRandomProgressArray();
            int i3 = 0;
            while (true) {
                if (i3 >= 3) {
                    break;
                }
                publishProgress(Integer.valueOf(iArrGenerateRandomProgressArray[i3]));
                SystemClock.sleep(500L);
                i3++;
            }
            synchronized (MiuiOsuDialogActivity.this.mLock) {
                try {
                    MiuiOsuDialogActivity.this.mLock.wait(i2);
                } catch (InterruptedException e) {
                    MiuiOsuDialogActivity.this.log("Exception = " + e);
                    Thread.currentThread().interrupt();
                }
            }
            MiuiOsuDialogActivity.this.log("updateResult = " + MiuiOsuDialogActivity.this.updateResult);
            boolean z = MiuiOsuDialogActivity.this.updateResult == 0 || MiuiOsuDialogActivity.this.updateResult == 4;
            if (z) {
                for (i = 3; i < iArrGenerateRandomProgressArray.length; i++) {
                    publishProgress(Integer.valueOf(iArrGenerateRandomProgressArray[i]));
                    SystemClock.sleep(500L);
                }
            }
            return Boolean.valueOf(z);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... numArr) {
            if (MiuiOsuDialogActivity.this.mOsuProgressDialog != null) {
                MiuiOsuDialogActivity.this.mOsuProgressDialog.setProgress(numArr[0].intValue());
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            String string;
            super.onPostExecute(bool);
            MiuiOsuDialogActivity.this.log("onPostExecute... aBoolean = " + bool);
            if (MiuiOsuDialogActivity.this.mOsuProgressDialog != null && MiuiOsuDialogActivity.this.mOsuProgressDialog.isShowing()) {
                MiuiOsuDialogActivity.this.mOsuProgressDialog.dismiss();
            }
            boolean zBooleanValue = bool.booleanValue();
            MiuiOsuDialogActivity miuiOsuDialogActivity = MiuiOsuDialogActivity.this;
            if (zBooleanValue) {
                string = miuiOsuDialogActivity.getResources().getString(R.string.osu_success_toast_message);
            } else {
                string = miuiOsuDialogActivity.getResources().getString(R.string.osu_error_toast_message);
            }
            Toast.makeText(MiuiOsuDialogActivity.this, string, 0).show();
            try {
                if (!MiuiOsuDialogActivity.this.mIsFromNotification) {
                    MiuiPhoneUtils.enterEsimManager(true);
                }
            } finally {
                MiuiOsuDialogActivity.this.finish();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showOsuProgressDialog() {
        dismissOsuProgressDialog();
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.mOsuProgressDialog = progressDialog;
        progressDialog.setMessage(getResources().getString(R.string.osu_progress_dialog_message));
        this.mOsuProgressDialog.setProgressStyle(1);
        this.mOsuProgressDialog.setMax(100);
        this.mOsuProgressDialog.setCancelable(false);
        this.mOsuProgressDialog.show();
        log("showOsuProgressDialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissOsuProgressDialog() {
        ProgressDialog progressDialog = this.mOsuProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            log("dismissOsuProgressDialog");
        }
    }

    @Override // com.xiaomi.telephony.EuiccOsuManagerBase.OsuCompleteCallback
    public void onComplete(int i, String str) {
        log("onComplete status = " + i + ", info = " + str);
        this.updateResult = i;
        synchronized (this.mLock) {
            this.mLock.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] generateRandomProgressArray() {
        int[] iArr = new int[5];
        iArr[0] = (int) ((Math.random() * 100.0d) / 5.0d);
        for (int i = 1; i < 4; i++) {
            int i2 = i - 1;
            iArr[i] = iArr[i2] + ((int) ((Math.random() * ((double) (100 - iArr[i2]))) / ((double) (5 - i))));
        }
        iArr[4] = 100;
        return iArr;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDialog = null;
        }
        dismissOsuProgressDialog();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (EuiccOsuManagerBase.getInstance() != null) {
            EuiccOsuManagerBase.getInstance().setOsuCompleteCallback(null);
        }
        OsuUpdateTask osuUpdateTask = this.mOsuUpdateTask;
        if (osuUpdateTask != null) {
            osuUpdateTask.cancel(true);
        }
    }

    protected void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
