package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.DialerKeyListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.TelephonyCapabilities;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class SmscAddressSettingActivity extends AppCompatActivity {
    private EditText mEditTextSmscAddress;
    private boolean mFirstResume;
    private Phone mPhone;
    private String mSmscAddress;
    private AlertDialog mSmscDialog;
    private final int WAIT_TIMEOUT = 5000;
    private boolean isShowing = false;
    private boolean mNeedAddressType = false;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.SmscAddressSettingActivity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || SmscAddressSettingActivity.this.mPhone == null || !"android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                return;
            }
            int intExtra = intent.getIntExtra("phone", SubscriptionManager.INVALID_PHONE_ID);
            String stringExtra = intent.getStringExtra("ss");
            if (intExtra == SmscAddressSettingActivity.this.mPhone.getPhoneId() && "ABSENT".equals(stringExtra)) {
                SmscAddressSettingActivity.this.mSmscDialog.dismiss();
                SmscAddressSettingActivity.this.finish();
            }
        }
    };
    private Handler mHandler = new Handler() { // from class: com.android.phone.settings.SmscAddressSettingActivity.2
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AlertDialog alertDialogCreateDialog;
            if (SmscAddressSettingActivity.this.isShowing) {
                switch (message.what) {
                    case 1001:
                        removeMessages(1003);
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        Throwable th = asyncResult.exception;
                        SmscAddressSettingActivity smscAddressSettingActivity = SmscAddressSettingActivity.this;
                        if (th != null) {
                            alertDialogCreateDialog = smscAddressSettingActivity.createDialog(R.string.get_smsc_address_error);
                        } else {
                            smscAddressSettingActivity.mSmscAddress = smscAddressSettingActivity.smscToString((String) asyncResult.result);
                            SmscAddressSettingActivity.this.mEditTextSmscAddress.setText(SmscAddressSettingActivity.this.mSmscAddress);
                            if (SmscAddressSettingActivity.this.mSmscAddress != null) {
                                SmscAddressSettingActivity.this.mEditTextSmscAddress.setSelection(SmscAddressSettingActivity.this.mSmscAddress.length());
                            }
                            alertDialogCreateDialog = null;
                        }
                        break;
                    case 1002:
                        removeMessages(1004);
                        Throwable th2 = ((AsyncResult) message.obj).exception;
                        SmscAddressSettingActivity smscAddressSettingActivity2 = SmscAddressSettingActivity.this;
                        if (th2 != null) {
                            alertDialogCreateDialog = smscAddressSettingActivity2.createDialog(R.string.set_smsc_address_error);
                        } else {
                            smscAddressSettingActivity2.finish();
                            alertDialogCreateDialog = null;
                        }
                        break;
                    case 1003:
                        alertDialogCreateDialog = SmscAddressSettingActivity.this.createDialog(R.string.get_smsc_address_timeout);
                        break;
                    case 1004:
                        alertDialogCreateDialog = SmscAddressSettingActivity.this.createDialog(R.string.set_smsc_address_timeout);
                        break;
                    default:
                        alertDialogCreateDialog = null;
                        break;
                }
                if (alertDialogCreateDialog != null) {
                    SmscAddressSettingActivity.this.mSmscDialog.dismiss();
                    alertDialogCreateDialog.show();
                }
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public String smscToString(String str) {
        int iIndexOf = str.indexOf(44);
        int i = 1;
        if (iIndexOf > 0) {
            this.mNeedAddressType = true;
            str = str.substring(0, iIndexOf).trim().replaceAll("\"", "");
        }
        if (str != null && TelephonyCapabilities.isSmscAddressEncoded(this)) {
            try {
                StringBuilder sb = new StringBuilder();
                int smscInt = getSmscInt(str, 0);
                if (smscInt >= 1 && getSmscInt(str, 1) == 91) {
                    sb.append('+');
                    while (i < smscInt) {
                        i++;
                        char[] smscChars = getSmscChars(str, i);
                        if (smscChars != null) {
                            for (char c : smscChars) {
                                if (c != 'f' && c != 'F') {
                                    sb.append(c);
                                }
                            }
                        }
                    }
                    return sb.toString();
                }
            } catch (Exception unused) {
            }
        }
        return str;
    }

    private int getSmscInt(String str, int i) {
        int i2 = (i + 1) * 2;
        if (str.length() >= i2) {
            return Integer.parseInt(str.substring(i * 2, i2));
        }
        return -1;
    }

    private char[] getSmscChars(String str, int i) {
        if (str.length() < (i + 1) * 2) {
            return null;
        }
        int i2 = i * 2;
        return new char[]{str.charAt(i2 + 1), str.charAt(i2)};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String stringToSmsc(String str) {
        PhoneNumberUtils.PhoneNumber phoneNumber;
        if (str != null && TelephonyCapabilities.isSmscAddressEncoded(this) && (phoneNumber = PhoneNumberUtils.PhoneNumber.parse(str)) != null && !TextUtils.isEmpty(phoneNumber.getCountryCode())) {
            String str2 = phoneNumber.getCountryCode() + phoneNumber.getEffectiveNumber();
            StringBuilder sb = new StringBuilder();
            int length = str2.length();
            int i = length / 2;
            for (int i2 = 0; i2 < i; i2++) {
                int i3 = i2 * 2;
                sb.append(str2.charAt(i3 + 1));
                sb.append(str2.charAt(i3));
            }
            if ((length & 1) == 1) {
                sb.append('f');
                sb.append(str2.charAt(length - 1));
            }
            sb.insert(0, "91");
            int length2 = sb.length() / 2;
            if (length2 >= 10) {
                sb.insert(0, length2);
            } else {
                sb.insert(0, length2);
                sb.insert(0, '0');
            }
            str = sb.toString();
        }
        if (!this.mNeedAddressType) {
            return str;
        }
        if ((str.length() > 1 && str.charAt(0) == '+') || (str.length() > 2 && str.charAt(0) == '0' && str.charAt(1) == '0')) {
            return "\"" + str + "\",145";
        }
        return "\"" + str + "\",129";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPhone = MiuiPhoneUtils.getPhone(SubscriptionManager.getSlotIdExtra(getIntent(), SubscriptionManager.getDefault().getDefaultSlotId()));
        EditText editText = new EditText(this);
        this.mEditTextSmscAddress = editText;
        editText.setInputType(3);
        this.mEditTextSmscAddress.setTextDirection(3);
        this.mEditTextSmscAddress.setTextAlignment(5);
        this.mEditTextSmscAddress.setKeyListener(DialerKeyListener.getInstance());
        if (!MiuiPhoneUtils.canSetSmscAddress(this.mPhone.getPhoneId())) {
            this.mEditTextSmscAddress.setEnabled(false);
        }
        FrameLayout frameLayout = new FrameLayout(this);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.preference_dialog_edittext_padding_side);
        frameLayout.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        frameLayout.addView(this.mEditTextSmscAddress);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(getString(R.string.set_smsc_address_title)).setView(frameLayout).setMessage(getString(R.string.smsc_dialog_message)).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.SmscAddressSettingActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                String string = SmscAddressSettingActivity.this.mEditTextSmscAddress.getText().toString();
                boolean zEquals = string.equals(SmscAddressSettingActivity.this.mSmscAddress);
                SmscAddressSettingActivity smscAddressSettingActivity = SmscAddressSettingActivity.this;
                if (!zEquals) {
                    smscAddressSettingActivity.mPhone.setSmscAddress(SmscAddressSettingActivity.this.stringToSmsc(string), SmscAddressSettingActivity.this.mHandler.obtainMessage(1002));
                    SmscAddressSettingActivity.this.mHandler.sendEmptyMessageDelayed(1004, MirrorDesktopHelper.TIMEOUT_MILLIS);
                } else {
                    smscAddressSettingActivity.finish();
                }
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.phone.settings.SmscAddressSettingActivity.4
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                SmscAddressSettingActivity.this.finish();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.SmscAddressSettingActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                SmscAddressSettingActivity.this.finish();
            }
        }).create();
        this.mSmscDialog = alertDialogCreate;
        alertDialogCreate.show();
        this.mFirstResume = true;
        registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"), 2);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.mFirstResume) {
            this.mPhone.getSmscAddress(this.mHandler.obtainMessage(1001));
            this.mHandler.sendEmptyMessageDelayed(1003, MirrorDesktopHelper.TIMEOUT_MILLIS);
            this.mFirstResume = false;
        }
        this.isShowing = true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.isShowing = false;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
        unregisterReceiver(this.mReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AlertDialog createDialog(int i) {
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setIconAttribute(android.R.attr.alertDialogIcon).setTitle(R.string.set_smsc_address_title).setMessage(i).setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null).create();
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.phone.settings.SmscAddressSettingActivity.6
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                SmscAddressSettingActivity.this.finish();
            }
        });
        return alertDialogCreate;
    }
}
