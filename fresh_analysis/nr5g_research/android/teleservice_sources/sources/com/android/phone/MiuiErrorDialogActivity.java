package com.android.phone;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.ims.ImsManager;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.List;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.ProgressDialog;

/* JADX INFO: loaded from: classes.dex */
public class MiuiErrorDialogActivity extends AppCompatActivity implements SubscriptionManager.OnSubscriptionsChangedListener {
    private static String LOG_TAG = "MiuiErrorDialogActivity";
    private static final int[] SMALL_SIM_SLOT_ICON = {R.drawable.small_sim_icon_slot_1, R.drawable.small_sim_icon_slot_2};
    public AppCompatActivity mActivity;
    private boolean mIsProcessing;
    private boolean mIsRoaming;
    private MultiSimAdapter mMultiSimAdapter;
    private AlertDialog mDialog = null;
    private final Handler mHandler = new Handler() { // from class: com.android.phone.MiuiErrorDialogActivity.42
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 100) {
                if (i != 101) {
                    return;
                }
                MiuiErrorDialogActivity.this.showResultDialog(false);
            } else if (MiuiErrorDialogActivity.this.mIsProcessing) {
                MiuiErrorDialogActivity.this.showResultDialog(true);
            }
        }
    };
    private List mSimInfoRecordList = new ArrayList();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setLayout(-1, -1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().addFlags(32);
        getWindow().getAttributes().layoutInDisplayCutoutMode = 3;
        getWindow().setAttributes(getWindow().getAttributes());
        if (getIntent() == null) {
            return;
        }
        this.mActivity = this;
        int intExtra = getIntent().getIntExtra("dialog_type", 0);
        log("dialogType=" + intExtra);
        if (intExtra != 0) {
            showDialog(intExtra);
        } else {
            finish();
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDialog = null;
        }
        final int intExtra = getIntent().getIntExtra("dialog_message_id", 0);
        String stringExtra = getIntent().getStringExtra("dialog_message");
        boolean booleanExtra = getIntent().getBooleanExtra("show_negative", false);
        final boolean booleanExtra2 = getIntent().getBooleanExtra("wfc_state", false);
        getIntent().getBooleanExtra("volte_unavailable_when_call", false);
        final int intExtra2 = getIntent().getIntExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, -1);
        this.mIsRoaming = getIntent().getBooleanExtra("is_roaming", false);
        Phone phone = MiuiPhoneUtils.getPhone(DefaultSimManager.getDefaultDataSlotId());
        final Resources resourcesForSubId = android.telephony.SubscriptionManager.getResourcesForSubId(getApplicationContext(), MiuiPhoneUtils.getPhone(intExtra2) == null ? phone.getSubId() : MiuiPhoneUtils.getPhone(intExtra2).getSubId());
        if (phone != null && phone.mCi != null && MiuiKoreaOperatorCustimizedUtils.isCustForKrLgu()) {
            phone.mCi.registerForIccRefresh(this.mHandler, 100, Integer.valueOf(intExtra2));
        }
        int i2 = android.R.string.ok;
        switch (i) {
            case 1:
                return new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setMessage(R.string.cmcc_ut_data_enable_prompt).setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false).create();
            case 2:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                if (intExtra == 0) {
                    intExtra = R.string.change_data_preference_4g_only_prompt;
                }
                return builder.setMessage(intExtra).setPositiveButton(R.string.config_default_data_sim, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        if (!MiuiErrorDialogActivity.this.isActivityFront("com.android.phone.settings.MobileNetworkSettings")) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.MAIN");
                            intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MobileNetworkSettings"));
                            MiuiErrorDialogActivity.this.startActivity(intent);
                        }
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setNegativeButton(R.string.got_it, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false).create();
            case 3:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                if (MiuiPhoneUtils.isDeviceNotSupportCDMA()) {
                    i2 = R.string.got_it;
                }
                AlertDialog.Builder cancelable = builder2.setPositiveButton(i2, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        int i4 = intExtra;
                        if (i4 == R.string.prompt_to_turnoff_volte_when_supp_service || i4 == R.string.prompt_to_turnoff_volte_when_failed) {
                            boolean z = MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED;
                            String str = z ? "com.android.phone.settings.MultiSimInfoEditorActivity" : "com.android.phone.settings.MobileNetworkSettings";
                            if (!MiuiErrorDialogActivity.this.isActivityFront(str)) {
                                Intent intent = new Intent("android.intent.action.MAIN");
                                intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, str));
                                if (z) {
                                    Bundle bundle = new Bundle();
                                    SubscriptionManager.putSlotId(bundle, intExtra2);
                                    intent.putExtras(bundle);
                                }
                                MiuiErrorDialogActivity.this.startActivity(intent);
                            }
                        }
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setTitle(R.string.telocation_countrycode_alert_title).setCancelable(false);
                if (!TextUtils.isEmpty(stringExtra)) {
                    cancelable.setMessage(stringExtra);
                } else {
                    cancelable.setMessage(intExtra);
                }
                if (booleanExtra) {
                    cancelable.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.5
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i3) {
                            MiuiErrorDialogActivity.this.finish();
                        }
                    });
                }
                return cancelable.create();
            case 4:
                return new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setMessage(R.string.opened_dual_4g_confirm_prompt).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.6
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false).create();
            case 5:
                return new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setMessage(String.format(getApplicationContext().getString(R.string.opened_dual_4g_confirm_prompt_with_sim), Integer.valueOf(intExtra2))).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.7
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false).create();
            case 6:
                final int intExtra3 = getIntent().getIntExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, -1);
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                if (intExtra == 0) {
                    intExtra = R.string.change_data_preference_4g_only_prompt;
                }
                return builder3.setMessage(intExtra).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.9
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        SubscriptionManager.getDefault().setDefaultDataSlotId(intExtra3);
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.8
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false).create();
            case 7:
                final int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(intExtra2);
                AlertDialog alertDialogCreate = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setTitle(R.string.vowifi_emergency_warning_title).setMessage(R.string.vowifi_emergency_warning_message).setCheckBox(MiuiImsPhoneUtils.isWfcEccWarnDialogChecked(subscriptionIdForSlot), getResources().getString(R.string.vowifi_emergency_warning_check)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.12
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiImsPhoneUtils.saveWfcEccWarnDialogCheckedState(subscriptionIdForSlot, MiuiErrorDialogActivity.this.mDialog.isChecked());
                        MiuiImsPhoneUtils.sendWifiEmergencyCallNotification(intExtra2);
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.11
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiImsPhoneUtils.sendWifiEmergencyCallNotification(intExtra2);
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.phone.MiuiErrorDialogActivity.10
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        MiuiImsPhoneUtils.sendWifiEmergencyCallNotification(intExtra2);
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).create();
                this.mDialog = alertDialogCreate;
                return alertDialogCreate;
            case 8:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder4.setTitle(R.string.select_dds_dialog_title);
                this.mMultiSimAdapter = new MultiSimAdapter(this);
                SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this);
                notifySimInfoDataChanged();
                builder4.setSingleChoiceItems(this.mMultiSimAdapter, intExtra2, new DialogInterface.OnClickListener(this) { // from class: com.android.phone.MiuiErrorDialogActivity.13
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        SubscriptionManager.getDefault().setDefaultDataSlotId(i3);
                    }
                });
                builder4.setNegativeButton(R.string.jump_to_mobile_network_setting, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.14
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MobileNetworkSettings"));
                        MiuiErrorDialogActivity.this.startActivity(intent);
                        MiuiErrorDialogActivity.this.finish();
                    }
                });
                builder4.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.15
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                });
                builder4.setCancelable(false);
                AlertDialog alertDialogCreate2 = builder4.create();
                this.mDialog = alertDialogCreate2;
                alertDialogCreate2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.phone.MiuiErrorDialogActivity.16
                    @Override // android.content.DialogInterface.OnDismissListener
                    public void onDismiss(DialogInterface dialogInterface) {
                        MiuiErrorDialogActivity.this.removeOnSubscriptionsChangedListener();
                    }
                });
                return this.mDialog;
            case 9:
                AlertDialog.Builder builder5 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                TextView textView = new TextView(this);
                textView.setAutoLinkMask(1);
                textView.setTextSize(2, 15.0f);
                textView.setText(resourcesForSubId.getString(R.string.wifi_call_notification_message));
                int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.appcompat_dialog_pannel_horizontal_margin);
                textView.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
                builder5.setView(textView).setNegativeButton(resourcesForSubId.getString(R.string.wifi_call_welcome_settings), new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.18
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        boolean z = MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED;
                        String str = z ? "com.android.phone.settings.MultiSimInfoEditorActivity" : "com.android.phone.settings.MobileNetworkSettings";
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, str));
                        if (z) {
                            Bundle bundle = new Bundle();
                            SubscriptionManager.putSlotId(bundle, intExtra2);
                            intent.putExtras(bundle);
                        }
                        MiuiErrorDialogActivity.this.startActivity(intent);
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.17
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate3 = builder5.create();
                this.mDialog = alertDialogCreate3;
                return alertDialogCreate3;
            case 10:
                AlertDialog.Builder builder6 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                ScrollView scrollView = new ScrollView(this);
                TextView textView2 = new TextView(this);
                textView2.setAutoLinkMask(1);
                textView2.setTextSize(2, 15.0f);
                textView2.setText(resourcesForSubId.getString(R.string.wifi_call_available_message));
                int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.appcompat_dialog_pannel_horizontal_margin);
                textView2.setPadding(dimensionPixelSize2, 0, dimensionPixelSize2, 0);
                scrollView.addView(textView2);
                builder6.setView(scrollView).setPositiveButton(resourcesForSubId.getString(R.string.wifi_call_accept), new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.20
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        ImsManager.getInstance(MiuiErrorDialogActivity.this, intExtra2).setWfcSetting(!booleanExtra2);
                        MiuiSettings.Global.putBoolean(MiuiErrorDialogActivity.this.getContentResolver(), "wifi_call_available_dialog_showed", true);
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setNegativeButton(resourcesForSubId.getString(R.string.wifi_call_cancel), new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.19
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.WifiCallHelpActivity"));
                        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, intExtra2);
                        MiuiErrorDialogActivity.this.startActivity(intent);
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setButtonForceVertical(true).setCancelable(false);
                AlertDialog alertDialogCreate4 = builder6.create();
                this.mDialog = alertDialogCreate4;
                alertDialogCreate4.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.phone.MiuiErrorDialogActivity.21
                    @Override // android.content.DialogInterface.OnShowListener
                    public void onShow(DialogInterface dialogInterface) {
                        Button button = MiuiErrorDialogActivity.this.mDialog.getButton(-1);
                        if (button != null) {
                            button.setContentDescription(resourcesForSubId.getString(R.string.wifi_call_accept));
                        }
                        Button button2 = MiuiErrorDialogActivity.this.mDialog.getButton(-2);
                        if (button2 != null) {
                            button2.setContentDescription(resourcesForSubId.getString(R.string.wifi_call_cancel));
                        }
                    }
                });
                return this.mDialog;
            case 11:
                AlertDialog.Builder builder7 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder7.setTitle(R.string.roaming_customer_service_title_kr).setMessage(String.format(getApplicationContext().getResources().getString(R.string.roaming_customer_service_message_kr), 82, 2, 2190, 0, Integer.valueOf(MirrorDesktopHelper.MSG_MIRROR_DEVICE_LIST_UPDATE))).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.23
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiKoreaOperatorCustimizedUtils.dialCustomerService(MiuiErrorDialogActivity.this);
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.22
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate5 = builder7.create();
                this.mDialog = alertDialogCreate5;
                return alertDialogCreate5;
            case 12:
                AlertDialog.Builder builder8 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder8.setMessage(R.string.dialog_ota_change_number_lgu).setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.25
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        AppCompatActivity appCompatActivity = MiuiErrorDialogActivity.this.mActivity;
                        if (appCompatActivity != null) {
                            appCompatActivity.dismissDialog(12);
                            boolean z = MiuiErrorDialogActivity.this.mIsRoaming;
                            MiuiErrorDialogActivity miuiErrorDialogActivity = MiuiErrorDialogActivity.this;
                            if (z) {
                                miuiErrorDialogActivity.mActivity.showDialog(13);
                                MiuiErrorDialogActivity.this.log("ota change number  is roaming");
                                return;
                            }
                            miuiErrorDialogActivity.log("sendDownloadEnvelope");
                            if (MiuiErrorDialogActivity.this.mHandler.hasMessages(101)) {
                                MiuiErrorDialogActivity.this.mHandler.removeMessages(101);
                                MiuiErrorDialogActivity.this.log("removed EVENT_OTA_CHANGE_NUMBER_DELAY msg success");
                            }
                            MiuiErrorDialogActivity.this.mHandler.sendEmptyMessageDelayed(101, 180000L);
                            MiuiErrorDialogActivity.this.mActivity.showDialog(14);
                            MiuiErrorDialogActivity.this.mIsProcessing = true;
                            MiuiErrorDialogActivity.this.sendDownloadEnvelope();
                        }
                    }
                }).setNegativeButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.24
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate6 = builder8.create();
                this.mDialog = alertDialogCreate6;
                return alertDialogCreate6;
            case 13:
                AlertDialog.Builder builder9 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder9.setMessage(R.string.dialog_ota_change_number_lgu_roaming).setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.26
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate7 = builder9.create();
                this.mDialog = alertDialogCreate7;
                return alertDialogCreate7;
            case 14:
                ProgressDialog progressDialog = new ProgressDialog(this);
                this.mDialog = progressDialog;
                progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.dialog_ota_change_number_processing));
                this.mDialog.setCancelable(false);
                return this.mDialog;
            case 15:
                AlertDialog.Builder builder10 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder10.setMessage(R.string.dialog_ota_change_number_fail).setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.27
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        ((PowerManager) MiuiErrorDialogActivity.this.getApplicationContext().getSystemService("power")).reboot("OTA change number failed");
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate8 = builder10.create();
                this.mDialog = alertDialogCreate8;
                return alertDialogCreate8;
            case 16:
                AlertDialog.Builder builder11 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder11.setMessage(R.string.dialog_ota_change_number_success).setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.28
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        ((PowerManager) MiuiErrorDialogActivity.this.getApplicationContext().getSystemService("power")).reboot("OTA change number success");
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate9 = builder11.create();
                this.mDialog = alertDialogCreate9;
                return alertDialogCreate9;
            case 17:
                AlertDialog.Builder builder12 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder12.setMessage(R.string.wifi_call_available_message_for_ATT_MX).setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.29
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate10 = builder12.create();
                this.mDialog = alertDialogCreate10;
                return alertDialogCreate10;
            case 18:
                AlertDialog.Builder builder13 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder13.setTitle(R.string.mi_sim_not_active).setMessage(R.string.mi_sim_not_active_tips).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.31
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.log("mi disable ok");
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MultiSimInfoEditorActivity"));
                        Bundle bundle = new Bundle();
                        SubscriptionManager.putSlotId(bundle, intExtra2);
                        intent.putExtras(bundle);
                        MiuiErrorDialogActivity.this.startActivity(intent);
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.30
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.log("mi disable cancel");
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate11 = builder13.create();
                this.mDialog = alertDialogCreate11;
                return alertDialogCreate11;
            case 19:
                AlertDialog.Builder builder14 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder14.setTitle(R.string.new_arrears_sim_title).setMessage(R.string.new_arrears_sim_summary).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.32
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate12 = builder14.create();
                this.mDialog = alertDialogCreate12;
                return alertDialogCreate12;
            case 20:
                AlertDialog.Builder builder15 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder15.setTitle(R.string.fiveg_interruption_remind_title).setMessage(String.format(getApplicationContext().getString(R.string.fiveg_interruption_remind), Integer.valueOf(intExtra2 + 1))).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.34
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setNegativeButton(R.string.do_not_show_again, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.33
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        PreferenceUtils.getInstance().setBooleanPreference("preferred_no_5g_remind_showAgain", false);
                        MiuiErrorDialogActivity.this.log("no 5G dialog not show again");
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate13 = builder15.create();
                this.mDialog = alertDialogCreate13;
                return alertDialogCreate13;
            case 21:
                AlertDialog.Builder builder16 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder16.setMessage(R.string.satellite_network_error).setPositiveButton(R.string.satellite_knew, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.35
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate14 = builder16.create();
                this.mDialog = alertDialogCreate14;
                return alertDialogCreate14;
            case 22:
                AlertDialog.Builder builder17 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder17.setMessage(R.string.satellite_exit_dialog_message).setPositiveButton(R.string.satellite_knew, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.36
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setCancelable(false);
                AlertDialog alertDialogCreate15 = builder17.create();
                this.mDialog = alertDialogCreate15;
                return alertDialogCreate15;
            case 23:
                AlertDialog.Builder builder18 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                builder18.setTitle(R.string.wifi_call_available_title_DEO2).setMessage(R.string.wifi_call_available_message_DEO2).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.38
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.phone.MiuiErrorDialogActivity.37
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                });
                AlertDialog alertDialogCreate16 = builder18.create();
                this.mDialog = alertDialogCreate16;
                return alertDialogCreate16;
            case 24:
                final int subscriptionIdForSlot2 = SubscriptionManager.getDefault().getSubscriptionIdForSlot(intExtra2);
                AlertDialog.Builder builder19 = new AlertDialog.Builder(this, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(1);
                int dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.appcompat_dialog_pannel_horizontal_margin);
                linearLayout.setPadding(dimensionPixelSize3, 0, dimensionPixelSize3, 0);
                TextView textView3 = new TextView(this);
                textView3.setAutoLinkMask(1);
                textView3.setTextSize(2, 15.0f);
                textView3.setText(resourcesForSubId.getString(R.string.wifi_call_available_message_claro));
                final CheckBox checkBox = new CheckBox(this);
                checkBox.setText(getResources().getString(R.string.wifi_call_checkbox_claro));
                checkBox.setChecked(MiuiImsPhoneUtils.isClaroWifiCallDialogChecked(subscriptionIdForSlot2));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.appcompat_dialog_pannel_horizontal_margin);
                checkBox.setLayoutParams(layoutParams);
                linearLayout.addView(textView3);
                linearLayout.addView(checkBox);
                builder19.setTitle(R.string.wifi_call_available_title_claro).setView(linearLayout).setPositiveButton(R.string.wifi_call_confirm_claro, new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiErrorDialogActivity.40
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        MiuiImsPhoneUtils.saveClaroWifiCallDialogCheckedState(subscriptionIdForSlot2, checkBox.isChecked());
                        ImsManager.getInstance(MiuiErrorDialogActivity.this, intExtra2).setWfcSetting(true);
                        MiuiErrorDialogActivity.this.finish();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.phone.MiuiErrorDialogActivity.39
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                });
                AlertDialog alertDialogCreate17 = builder19.create();
                this.mDialog = alertDialogCreate17;
                alertDialogCreate17.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.phone.MiuiErrorDialogActivity.41
                    @Override // android.content.DialogInterface.OnDismissListener
                    public void onDismiss(DialogInterface dialogInterface) {
                        MiuiErrorDialogActivity.this.finish();
                    }
                });
                return this.mDialog;
            default:
                return super.onCreateDialog(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDownloadEnvelope() {
        CommandsInterface commandsInterface;
        Phone phone = MiuiPhoneUtils.getPhone(DefaultSimManager.getDefaultDataSlotId());
        if (phone != null && (commandsInterface = phone.mCi) != null) {
            commandsInterface.sendEnvelopeWithStatus("D1340202838106069833111111110B26E40A9833111111117F160C01091557323614027000000F0D00012020B0000600000000000005", (Message) null);
        } else {
            log("download envelope failed because phone is null");
            showResultDialog(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showResultDialog(boolean z) {
        CommandsInterface commandsInterface;
        if (this.mActivity == null) {
            log("showResultDialog faile mActivity is null isSuccess=" + z);
            return;
        }
        log("showResultDialog isSuccess = " + z);
        removeDialog(14);
        this.mIsProcessing = false;
        if (this.mHandler.hasMessages(101)) {
            this.mHandler.removeMessages(101);
            log("have result removed EVENT_OTA_CHANGE_NUMBER_DELAY");
        }
        Phone phone = MiuiPhoneUtils.getPhone(DefaultSimManager.getDefaultDataSlotId());
        if (phone != null && (commandsInterface = phone.mCi) != null) {
            commandsInterface.unregisterForIccRefresh(this.mHandler);
        }
        AppCompatActivity appCompatActivity = this.mActivity;
        if (z) {
            appCompatActivity.showDialog(16);
        } else {
            appCompatActivity.showDialog(15);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeOnSubscriptionsChangedListener() {
        SubscriptionManager.getDefault().removeOnSubscriptionsChangedListener(this);
    }

    public void notifySimInfoDataChanged() {
        List subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
        this.mSimInfoRecordList = subscriptionInfoList;
        if ((subscriptionInfoList != null ? subscriptionInfoList.size() : 0) == 0 && this.mMultiSimAdapter != null) {
            finish();
            return;
        }
        MultiSimAdapter multiSimAdapter = this.mMultiSimAdapter;
        if (multiSimAdapter != null) {
            multiSimAdapter.notifyDataSetChanged();
        }
    }

    public void onSubscriptionsChanged() {
        notifySimInfoDataChanged();
    }

    class MultiSimAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        MultiSimAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override // android.widget.Adapter
        public SubscriptionInfo getItem(int i) {
            return (SubscriptionInfo) MiuiErrorDialogActivity.this.mSimInfoRecordList.get(i);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return MiuiErrorDialogActivity.this.mSimInfoRecordList.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.mInflater.inflate(R.layout.msim_list_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.simDisplayName = (CheckedTextView) view.findViewById(R.id.txt_sim_displayname);
                viewHolder.simIcon = (ImageView) view.findViewById(R.id.img_sim_icon);
                viewHolder.simNumber = (TextView) view.findViewById(R.id.txt_sim_number);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            SubscriptionInfo item = getItem(i);
            boolean zIsVirtualSim = MiuiPhoneUtils.isVirtualSim(item.getSlotId());
            viewHolder.simDisplayName.setText(zIsVirtualSim ? MiuiPhoneUtils.getVirtualSimCarrierName() : item.getDisplayName());
            int i2 = zIsVirtualSim ? R.drawable.mi_small_sim_icon : MiuiErrorDialogActivity.SMALL_SIM_SLOT_ICON[item.getSlotId()];
            ImageView imageView = viewHolder.simIcon;
            if (i2 > 0) {
                imageView.setImageResource(i2);
            } else {
                imageView.setVisibility(8);
            }
            String displayNumber = item.getDisplayNumber();
            viewHolder.simNumber.setText(displayNumber);
            viewHolder.simIcon.setVisibility(0);
            viewHolder.simNumber.setSelected(true);
            viewHolder.simNumber.setVisibility(TextUtils.isEmpty(displayNumber) ? 8 : 0);
            return view;
        }

        class ViewHolder {
            CheckedTextView simDisplayName;
            ImageView simIcon;
            TextView simNumber;

            private ViewHolder(MultiSimAdapter multiSimAdapter) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isActivityFront(String str) {
        List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) getSystemService("activity")).getRunningTasks(2);
        return runningTasks != null && runningTasks.size() == 2 && runningTasks.get(0).topActivity.getClassName().equals(MiuiErrorDialogActivity.class.getName()) && runningTasks.get(1).topActivity.getClassName().equals(str);
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
        if (this.mMultiSimAdapter != null) {
            removeOnSubscriptionsChangedListener();
            this.mMultiSimAdapter = null;
        }
    }

    protected void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
