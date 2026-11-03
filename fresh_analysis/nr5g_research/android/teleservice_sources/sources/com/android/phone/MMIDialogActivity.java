package com.android.phone;

import android.app.ActivityManager;
import android.app.Dialog;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.phone.statistics.MiuiSuppServiceStatistic;
import java.util.ArrayList;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class MMIDialogActivity extends AppCompatActivity {
    private Handler mHandler;
    private Dialog mMMIDialog;
    private CallManager mCM = null;
    private Phone mPhone = null;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRequestedOrientation(3);
        if (ActivityManager.getCurrentUser() == 0) {
            this.mCM = PhoneGlobals.getInstance().getCallManager();
            Phone phone = PhoneGlobals.getPhone(getIntent().getIntExtra("subscription", Integer.MAX_VALUE));
            this.mPhone = phone;
            if (phone == null) {
                this.mPhone = PhoneGlobals.getPhone();
            }
            Handler handler = new Handler() { // from class: com.android.phone.MMIDialogActivity.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    int i = message.what;
                    if (i == 52) {
                        MMIDialogActivity.this.onMMIComplete((MmiCode) ((AsyncResult) message.obj).result);
                    } else {
                        if (i != 53) {
                            return;
                        }
                        MMIDialogActivity.this.onMMICancel();
                    }
                }
            };
            this.mHandler = handler;
            this.mCM.registerForMmiComplete(handler, 52, (Object) null);
            if (this.mCM.getState() == PhoneConstants.State.OFFHOOK) {
                Toast.makeText(this, R.string.incall_status_dialed_mmi, 0).show();
            }
            showMMIDialog();
            return;
        }
        Toast.makeText(this, R.string.ussdRunning, 0).show();
        finish();
    }

    @Override // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public void setTheme(int i) {
        super.setTheme(R.style.Miui_Theme_Settings_Dialog);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        Dialog dialog = this.mMMIDialog;
        if (dialog != null) {
            dialog.dismiss();
            this.mMMIDialog = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            this.mCM.unregisterForMmiComplete(handler);
            this.mHandler = null;
        }
    }

    private void showMMIDialog() {
        ArrayList arrayList = new ArrayList(this.mPhone.getPendingMmiCodes());
        if (this.mPhone.getImsPhone() != null) {
            arrayList.addAll(this.mPhone.getImsPhone().getPendingMmiCodes());
        }
        if (!arrayList.isEmpty()) {
            MmiCode mmiCode = (MmiCode) arrayList.get(0);
            Message messageObtain = Message.obtain(this.mHandler, 53);
            Log.d("MiuiMMIDialogActivity", "showMMIDialog: mmiCode = " + mmiCode);
            this.mMMIDialog = MiuiPhoneUtils.displayMMIInitiate(this.mPhone, this, mmiCode, messageObtain, this.mMMIDialog);
            return;
        }
        Log.d("MiuiMMIDialogActivity", "showMMIDialog: no pending MMIs; finishing");
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMMIComplete(MmiCode mmiCode) {
        int phoneType = this.mPhone.getPhoneType();
        if (phoneType == 2) {
            PhoneUtils.displayMMIComplete(this.mPhone, this, mmiCode, null, null);
        } else if (phoneType == 1 && mmiCode.getState() != MmiCode.State.PENDING) {
            Log.d("MiuiMMIDialogActivity", "Got MMI_COMPLETE, finishing dialog activity...");
            dismissDialogsAndFinish();
        }
        MiuiSuppServiceStatistic.getInstance(this).recordMmiExceptionScene(mmiCode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMMICancel() {
        Log.v("MiuiMMIDialogActivity", "onMMICancel()...");
        PhoneUtils.cancelMmiCode(this.mPhone);
        Log.d("MiuiMMIDialogActivity", "onMMICancel: finishing InCallScreen...");
        dismissDialogsAndFinish();
    }

    private void dismissDialogsAndFinish() {
        Dialog dialog = this.mMMIDialog;
        if (dialog != null) {
            dialog.dismiss();
            this.mMMIDialog = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            this.mCM.unregisterForMmiComplete(handler);
            this.mHandler = null;
        }
        finish();
    }
}
