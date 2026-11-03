package com.android.phone;

import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import gov.nist.javax.sip.address.GenericURI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class MiuiKrEmergencyListActivity extends AppCompatActivity {
    private AlertDialog mAlertDialog;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.MiuiKrEmergencyListActivity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                MiuiKrEmergencyListActivity.this.finish();
            }
        }
    };
    private StatusBarManager mStatusBarManager;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.kr_emergency_list_item);
        this.mStatusBarManager = (StatusBarManager) getSystemService("statusbar");
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.flags |= 67633152;
        attributes.extraFlags |= 4096;
        getWindow().setAttributes(attributes);
        getWindow().getDecorView().setSystemUiVisibility(768);
        final String[] strArr = {getResources().getString(R.string.kr_emergency_112, 112), getResources().getString(R.string.kr_emergency_119, 119), getResources().getString(R.string.kr_emergency_122, 122), getResources().getString(R.string.kr_emergency_113, Integer.valueOf(ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE)), getResources().getString(R.string.kr_emergency_125, 125), getResources().getString(R.string.kr_emergency_111, 111), getResources().getString(R.string.kr_emergency_117, 117), getResources().getString(R.string.kr_emergency_118, 118), getResources().getString(R.string.kr_emergency_with_number)};
        ListView listView = (ListView) findViewById(R.id.emergency_list_item);
        listView.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.kr_emergency_text, strArr));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.android.phone.MiuiKrEmergencyListActivity.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                String numberFromText = MiuiKrEmergencyListActivity.this.getNumberFromText(strArr[i]);
                MiuiKrEmergencyListActivity.this.logi(numberFromText);
                boolean zIsEmpty = TextUtils.isEmpty(numberFromText);
                MiuiKrEmergencyListActivity miuiKrEmergencyListActivity = MiuiKrEmergencyListActivity.this;
                if (!zIsEmpty) {
                    miuiKrEmergencyListActivity.showAlertDialog(strArr[i], numberFromText);
                } else {
                    miuiKrEmergencyListActivity.startEmergencyDialer();
                }
            }
        });
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar != null) {
            appCompatActionBar.setDisplayHomeAsUpEnabled(false);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mStatusBarManager.disable(65536);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mStatusBarManager.disable(0);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        unregisterReceiver(this.mBroadcastReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getNumberFromText(String str) {
        Matcher matcher = Pattern.compile("\\d+").matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startEmergencyDialer() {
        Intent intent = new Intent();
        intent.setClass(this, EmergencyDialer.class);
        intent.putExtra("EmergencyList", "kr");
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAlertDialog(String str, final String str2) {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mAlertDialog.dismiss();
        }
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiKrEmergencyListActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    ((TelecomManager) MiuiKrEmergencyListActivity.this.getSystemService("telecom")).placeCall(Uri.fromParts(GenericURI.TEL, str2, null), null);
                } else {
                    dialogInterface.dismiss();
                }
            }
        };
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setTitle(R.string.emergency_enable_radio_dialog_title).setMessage(getString(R.string.kr_emergency_message, new Object[]{str})).setPositiveButton(android.R.string.ok, onClickListener).setNegativeButton(android.R.string.cancel, onClickListener).create();
        this.mAlertDialog = alertDialogCreate;
        alertDialogCreate.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String str) {
        Log.i("MiuiKrEmergencyListActivity", str);
    }
}
