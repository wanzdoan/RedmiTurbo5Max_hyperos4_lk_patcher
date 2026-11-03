package com.android.phone.utils;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.PhoneConstants;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class DcStatMonitor {
    private Context mContext;
    private int mDefaultDataSubId;
    private boolean mDoNotShowAgain;
    private PhoneConstants.DataState mConnectState = PhoneConstants.DataState.DISCONNECTED;
    private AlertDialog mDcAlertDialog = null;
    private CheckBox mCheckbox = null;

    public DcStatMonitor() {
        this.mContext = null;
        this.mDoNotShowAgain = false;
        this.mContext = PhoneGlobals.getInstance();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.intent.action.ANY_DATA_STATE");
        this.mContext.registerReceiver(new TelephonyBroadcastReceiver(), intentFilter, 2);
        this.mDefaultDataSubId = SubscriptionManager.getDefault().getDefaultDataSubscriptionId();
        this.mDoNotShowAgain = PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean("pref_dc_alert_dialog_not_show_again", false);
    }

    class TelephonyBroadcastReceiver extends BroadcastReceiver {
        private TelephonyBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.ANY_DATA_STATE".equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("apnType");
                if (!TextUtils.isEmpty(stringExtra) && stringExtra.contains("default") && intent.getIntExtra("subscription", SubscriptionManager.INVALID_SUBSCRIPTION_ID) == DcStatMonitor.this.mDefaultDataSubId) {
                    PhoneConstants.DataState dataState = null;
                    try {
                        String stringExtra2 = intent.getStringExtra(MEModuleSignalT1s.EXTRA_STATE);
                        if (stringExtra2 != null) {
                            dataState = (PhoneConstants.DataState) Enum.valueOf(PhoneConstants.DataState.class, stringExtra2);
                        }
                    } catch (IllegalArgumentException e) {
                        Log.e("DcStatMonitor", "Exception: " + e);
                    }
                    if (DcStatMonitor.this.mConnectState == dataState) {
                        return;
                    }
                    DcStatMonitor.this.mConnectState = dataState;
                    if (dataState == PhoneConstants.DataState.CONNECTED) {
                        DcStatMonitor.this.showDataConnectionAlertDialog();
                        return;
                    }
                    return;
                }
                return;
            }
            if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(intent.getAction())) {
                DcStatMonitor.this.mDefaultDataSubId = intent.getIntExtra("subscription", SubscriptionManager.INVALID_SUBSCRIPTION_ID);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDataConnectionAlertDialog() {
        boolean zIsCustForMxTelcel;
        boolean zIsWifiConnected = MiuiPhoneUtils.isWifiConnected();
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            zIsCustForMxTelcel = HyperOSCustFeatureResolve.getBoolean("show_first_data_alert", false);
        } else {
            zIsCustForMxTelcel = TelephonyManager.isCustForMxTelcel();
        }
        if (!zIsCustForMxTelcel || this.mDoNotShowAgain || zIsWifiConnected) {
            return;
        }
        dismissDataConnectionAlertDialog();
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.utils.DcStatMonitor.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    Log.i("DcStatMonitor", "click ok");
                } else {
                    android.telephony.TelephonyManager.from(DcStatMonitor.this.mContext).setDataEnabled(false);
                    Log.i("DcStatMonitor", "click cancel, set data enable false");
                }
                if (DcStatMonitor.this.mCheckbox == null || !DcStatMonitor.this.mCheckbox.isChecked()) {
                    return;
                }
                Log.i("DcStatMonitor", "do not show again");
                DcStatMonitor.this.mDoNotShowAgain = true;
                DcStatMonitor.this.storeValues("pref_dc_alert_dialog_not_show_again", true);
            }
        };
        String string = this.mContext.getString(R.string.data_plan);
        SpannableString spannableString = new SpannableString(this.mContext.getString(R.string.dc_alert_dialog_message, string));
        int iIndexOf = spannableString.toString().indexOf(string);
        spannableString.setSpan(new MyURLSpan(this, "http://pqta.telcel.com", this), iIndexOf, string.length() + iIndexOf, 33);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext, R$style.AlertDialog_Theme_DayNight);
        View viewInflate = View.inflate(builder.getContext(), R.layout.data_alert_dialog, null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.data_alert_message);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        CheckBox checkBox = (CheckBox) viewInflate.findViewById(R.id.checkbox);
        this.mCheckbox = checkBox;
        checkBox.setChecked(false);
        AlertDialog alertDialogCreate = builder.setTitle(R.string.dc_alert_dialog_title).setView(viewInflate).setPositiveButton(R.string.ok, onClickListener).setNegativeButton(android.R.string.cancel, onClickListener).create();
        this.mDcAlertDialog = alertDialogCreate;
        alertDialogCreate.getWindow().setType(2003);
        this.mDcAlertDialog.show();
    }

    class MyURLSpan extends URLSpan {
        private DcStatMonitor monitor;

        public MyURLSpan(DcStatMonitor dcStatMonitor, String str, DcStatMonitor dcStatMonitor2) {
            super(str);
            this.monitor = dcStatMonitor2;
        }

        @Override // android.text.style.URLSpan, android.text.style.ClickableSpan
        public void onClick(View view) {
            Uri uri = Uri.parse(getURL());
            Context context = view.getContext();
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            intent.setFlags(268435456);
            intent.putExtra("com.android.browser.application_id", context.getPackageName());
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
            }
            this.monitor.dismissDataConnectionAlertDialog();
            this.monitor = null;
        }
    }

    public void dismissDataConnectionAlertDialog() {
        AlertDialog alertDialog = this.mDcAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDcAlertDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storeValues(String str, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editorEdit.putBoolean(str, z);
        Log.d("DcStatMonitor", "storeValues " + str + "=" + z);
        editorEdit.apply();
    }
}
