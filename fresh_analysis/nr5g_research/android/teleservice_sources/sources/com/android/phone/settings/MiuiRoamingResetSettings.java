package com.android.phone.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.RadioAccessFamily;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.ProgressDialog;

/* JADX INFO: loaded from: classes.dex */
public class MiuiRoamingResetSettings extends AppCompatActivity {
    private static int EF_EPSLOCI = 28643;
    private static int EF_FPLMN = 28539;
    private static int EF_LOCI = 28542;
    private static int EF_PSLOCI = 28531;
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private Phone mPhone;
    private AlertDialog mResetRoamingDialog;
    private int mSlotId;
    private int mSubId;
    private TelephonyManager mTelephonyManager;
    protected boolean mIsForeground = false;
    private IccFileHandler mIccFileHandler = null;
    private MoreRoamingHandler mHandler = new MoreRoamingHandler();

    @Override // android.app.Activity
    protected void onPrepareDialog(int i, Dialog dialog) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SimPickerPreference.showSimPicker(this)) {
            return;
        }
        this.mSlotId = SubscriptionManager.getSlotIdExtra(getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
        this.mSubId = SubscriptionManager.getDefault().getSubscriptionIdForSlot(this.mSlotId);
        this.mPhone = MiuiPhoneUtils.getPhone(this.mSlotId);
        this.mTelephonyManager = (TelephonyManager) getApplicationContext().getSystemService("phone");
        if (MiuiKoreaOperatorCustimizedUtils.isKrSim(this.mPhone)) {
            loadIccFileHandler();
            showDialog(100);
        } else {
            Toast.makeText(this, R.string.reset_roaming_not_support, 0).show();
            finish();
        }
    }

    private void loadIccFileHandler() {
        Log.d("MiuiRoamingResetSettings", "loadIccFileHandler()");
        this.mIccFileHandler = this.mPhone.getIccFileHandler();
        Log.d("MiuiRoamingResetSettings", "fh = " + this.mIccFileHandler);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.mIsForeground = true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mIsForeground = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doResetRoamingDialog() {
        int i;
        dismissDialog(100);
        Phone phone = MiuiPhoneUtils.getPhone(this.mSlotId);
        if (MiuiKoreaOperatorCustimizedUtils.isKrLguSim(phone)) {
            i = 0;
        } else if (MiuiKoreaOperatorCustimizedUtils.isKrKtSim(phone)) {
            i = 1;
        } else {
            i = MiuiKoreaOperatorCustimizedUtils.isKrSktSim(phone) ? 2 : -1;
        }
        if (i != -1) {
            if (this.mIsForeground) {
                showDialog(200);
            }
            resetRoaming(i);
            rebootSys();
        }
    }

    private void resetRoaming(int i) {
        Log.d("MiuiRoamingResetSettings", "resetRoaming type:" + i);
        resetNetWorkSelectionMode();
        resetNetWorkType();
        if (i == 0) {
            initLGUEFValue();
        } else if (i == 1) {
            iniKTEFValue();
        } else {
            if (i != 2) {
                return;
            }
            iniSKTEFValue();
        }
    }

    private void resetNetWorkType() {
        if (this.mTelephonyManager != null) {
            new Thread() { // from class: com.android.phone.settings.MiuiRoamingResetSettings.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    int i = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportModes()[MiuiRoamingResetSettings.this.mSlotId][0];
                    Log.d("MiuiRoamingResetSettings", "---resetNetWorkType---mSubId:" + MiuiRoamingResetSettings.this.mSubId + "--defaultMode--:" + i);
                    TelephonyManager telephonyManagerCreateForSubscriptionId = MiuiRoamingResetSettings.this.mTelephonyManager.createForSubscriptionId(MiuiRoamingResetSettings.this.mSubId);
                    if (telephonyManagerCreateForSubscriptionId != null) {
                        telephonyManagerCreateForSubscriptionId.setAllowedNetworkTypesForReason(2, RadioAccessFamily.getRafFromNetworkType(i));
                    }
                }
            }.start();
        }
    }

    private void resetNetWorkSelectionMode() {
        this.mPhone.setNetworkSelectionModeAutomatic((Message) null);
    }

    private void iniSKTEFValue() {
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_FPLMN, "54F08054F060FFFFFFFFFFFF", this.mHandler.obtainMessage(1));
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_PSLOCI, "FFFFFFFFFFFFFFFFFFFF0000FF01", this.mHandler.obtainMessage(1));
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_EPSLOCI, "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF01", this.mHandler.obtainMessage(1));
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_LOCI, "FFFFFFFFFFFFFF0000FF01", this.mHandler.obtainMessage(1));
    }

    private void iniKTEFValue() {
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_FPLMN, "54F05054F06054F09054F001FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", this.mHandler.obtainMessage(1));
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_PSLOCI, "FFFFFFFFFFFFFFFFFFFF0000FF01", this.mHandler.obtainMessage(1));
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_EPSLOCI, "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFF000001", this.mHandler.obtainMessage(1));
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_LOCI, "FFFFFFFFFFFFFF0000FF01", this.mHandler.obtainMessage(1));
    }

    private void initLGUEFValue() {
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_FPLMN, "54F08054F050FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", this.mHandler.obtainMessage(1));
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_LOCI, "FFFFFFFFFFFFFF0000FF01", this.mHandler.obtainMessage(1));
        initUICCEF(this.mPhone, this.mIccFileHandler, EF_PSLOCI, "FFFFFFFFFFFFFFFFFFFF0000FF01", this.mHandler.obtainMessage(1));
    }

    class MoreRoamingHandler extends Handler {
        private MoreRoamingHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                handleGetEF(message);
            } else {
                if (i != 1) {
                    return;
                }
                handleSetEF(message);
            }
        }

        public void handleGetEF(Message message) {
            Log.d("MiuiRoamingResetSettings", "handleGetEF: done");
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                Log.d("MiuiRoamingResetSettings", "handleGetEF with exception = " + asyncResult.exception);
            } else {
                Log.d("MiuiRoamingResetSettings", "---handleGetEF:---" + MiuiRoamingResetSettings.this.bytes2hexStr((byte[]) asyncResult.result));
            }
        }

        public void handleSetEF(Message message) {
            Log.d("MiuiRoamingResetSettings", "handleSetEF: done");
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                Log.d("MiuiRoamingResetSettings", "handleSetEF with exception = " + asyncResult.exception);
                return;
            }
            Log.d("MiuiRoamingResetSettings", "handleSetEF: with OK result!");
        }
    }

    private void initUICCEF(Phone phone, IccFileHandler iccFileHandler, int i, String str, Message message) {
        Log.d("MiuiRoamingResetSettings", "update initUICCEF Start.");
        if (iccFileHandler != null) {
            writeEfToIcc(iccFileHandler, hexToByte(str), i, message);
        } else {
            Log.w("MiuiRoamingResetSettings", "mIccFileHandler is null.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String bytes2hexStr(byte[] bArr) {
        int length = bArr.length;
        if (length == 0) {
            return null;
        }
        char[] cArr = new char[length * 2];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            char[] cArr2 = HEX_CHARS;
            byte b = bArr[i];
            cArr[i2] = cArr2[(b >>> 4) & 15];
            cArr[i2 + 1] = cArr2[b & 15];
        }
        return new String(cArr);
    }

    private byte[] hexToByte(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            int i3 = i2 + 1;
            bArr[i] = Byte.valueOf((byte) Integer.decode("0x" + str.substring(i2, i3) + str.substring(i3, i2 + 2)).intValue()).byteValue();
        }
        return bArr;
    }

    private static void writeEfToIcc(IccFileHandler iccFileHandler, byte[] bArr, int i, Message message) {
        iccFileHandler.updateEFTransparent(i, bArr, message);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        if (i == 100) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.reset_roaming_message).setTitle(R.string.reset_roaming_title).setIconAttribute(android.R.attr.alertDialogIcon).setNegativeButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MiuiRoamingResetSettings.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    MiuiRoamingResetSettings.this.finish();
                }
            }).setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.MiuiRoamingResetSettings.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    MiuiRoamingResetSettings.this.doResetRoamingDialog();
                }
            });
            AlertDialog alertDialogCreate = builder.create();
            this.mResetRoamingDialog = alertDialogCreate;
            return alertDialogCreate;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.reset_roaming_tip));
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    private void rebootSys() {
        new Handler().postDelayed(new Runnable() { // from class: com.android.phone.settings.MiuiRoamingResetSettings.4
            @Override // java.lang.Runnable
            public void run() {
                Log.d("MiuiRoamingResetSettings", "---rebootSys---");
                MiuiRoamingResetSettings.this.removeDialog(200);
                ((PowerManager) MiuiRoamingResetSettings.this.getApplicationContext().getSystemService("power")).reboot(null);
            }
        }, 6000L);
    }
}
