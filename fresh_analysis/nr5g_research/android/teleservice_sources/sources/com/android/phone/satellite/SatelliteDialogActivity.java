package com.android.phone.satellite;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.MiuiSettings;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.phone.PhoneUtils;
import com.android.phone.R;
import com.android.phone.SatelliteManagerBase;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import java.util.ArrayList;
import java.util.List;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.R$id;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteDialogActivity extends AppCompatActivity {
    public static String AGREE_SATELLITE_PRIVACY_STATEMENT = "agree_satellite_privacy_statement";
    private static SatelliteTimer mPrivacyWithdrawalTimer;
    public static SatelliteTimer mTimer;
    private AlertDialog mAlertDialog;
    private Context mContext;
    private int mFlag;
    private LayoutInflater mInflater;
    private SelectorAdapter mSelectorAdapter;
    private final List mSimPhoneAccountHandles = new ArrayList(2);
    private int mSlotId;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        overridePendingTransition(0, 0);
        if (getIntent() == null) {
            Log.e("SatelliteDialogActivity", "onCreate: intent is null");
            return;
        }
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.layoutInDisplayCutoutMode = 1;
        getWindow().setAttributes(attributes);
        this.mContext = this;
        int intExtra = getIntent().getIntExtra("type", 0);
        this.mFlag = getIntent().getIntExtra("flag_id", 0);
        this.mSlotId = getIntent().getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, 0);
        showDialog(intExtra);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(final int i) {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (i == 1) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.satellite_point_title).setMessage(R.string.satellite_user_reminder).setPositiveButton(R.string.satellite_start, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    if (SatelliteDialogActivity.this.mAlertDialog.isChecked()) {
                        SystemProperties.set("persist.radio.satellite.showAgain", "false");
                    }
                    SatelliteDialogActivity.this.finish();
                    SatelliteChecker.getInstance().handleFrom(4);
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).setCheckBox(false, getString(R.string.satellite_not_show_again)).setCancelable(false).create();
        } else if (i == 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
            List<PhoneAccountHandle> callCapablePhoneAccounts = TelecomManager.from(this.mContext).getCallCapablePhoneAccounts(true);
            this.mInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            if (callCapablePhoneAccounts != null) {
                for (PhoneAccountHandle phoneAccountHandle : callCapablePhoneAccounts) {
                    phoneAccountHandle.getId();
                    phoneAccountHandle.getComponentName().getClassName();
                    this.mSimPhoneAccountHandles.add(phoneAccountHandle);
                }
                initAdapter();
                builder.setCustomTitle(getTitleView());
                builder.setSingleChoiceItems(this.mSelectorAdapter, -1, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        if (SatelliteDialogActivity.this.mSelectorAdapter != null) {
                            AccountInfo item = SatelliteDialogActivity.this.mSelectorAdapter.getItem(i2);
                            if (item.isActive()) {
                                SatelliteChecker.getInstance().setEnabledSlotId(PhoneUtils.getPhoneForPhoneAccountHandle(item.getPhoneAccount()).getPhoneId());
                                SatelliteDialogActivity.this.finish();
                                SatelliteChecker.getInstance().handleFrom(5);
                            }
                        }
                    }
                });
            }
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            });
            this.mAlertDialog = builder.create();
        } else if (i == 3) {
            this.mAlertDialog = new AlertDialog.Builder(this.mContext).setTitle(R.string.satellite_low_battery_title).setMessage(this.mContext.getResources().getQuantityString(R.plurals.satellite_low_battery_turn_off_message, 10, 10)).setPositiveButton(R.string.satellite_exit_now_battery, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                    SatelliteManagerBase.getInstance().setSatelliteEnableByUser(SatelliteDialogActivity.this.mContext, false, 1005);
                    SatelliteTimer satelliteTimer = SatelliteDialogActivity.mTimer;
                    if (satelliteTimer != null) {
                        satelliteTimer.destroy();
                        SatelliteDialogActivity.mTimer = null;
                    }
                }
            }).setNegativeButton(R.string.satellite_start_battery, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).create();
            updateDialogMessage(this.mContext);
        } else if (i == 4) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.satellite_privacy_statement_title).setMessage(R.string.satellite_privacy_statement_message).setPositiveButton(R.string.satellite_privacy_statement_positive, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.8
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                    SatelliteChecker.getInstance().handleFrom(2);
                    SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(SatelliteDialogActivity.this.mContext).edit();
                    editorEdit.putBoolean(SatelliteDialogActivity.AGREE_SATELLITE_PRIVACY_STATEMENT, true);
                    editorEdit.commit();
                }
            }).setNegativeButton(R.string.satellite_privacy_statement_negative, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.7
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).setCancelable(false).create();
        } else if (i == 5) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.satellite_point_title).setMessage(R.string.satellite_display_message).setPositiveButton(R.string.satellite_display_search_star, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.10
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                    SatelliteManagerBase.getInstance().satelliteDisplay();
                }
            }).setNegativeButton(R.string.satellite_start_battery, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.9
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).setCancelable(false).create();
        } else if (i == 6) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.satellite_display_insert_support_card_title).setMessage(R.string.satellite_display_insert_support_card_message).setPositiveButton(R.string.satellite_knew, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.11
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).setCancelable(false).create();
        } else if (i == 7) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.satellite_privacy_withdrawal_dialog_title).setMessage(R.string.satellite_privacy_withdrawal_dialog_message).setNegativeButton(this.mContext.getResources().getString(R.string.satellite_privacy_withdrawal_dialog_enable_negative, 10), (DialogInterface.OnClickListener) null).setPositiveButton(R.string.satellite_privacy_withdrawal_dialog_positive, (DialogInterface.OnClickListener) null).setCancelable(false).create();
        } else if (i == 9) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.satellite_low_battery_title).setMessage(R.string.satellite_low_battery_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.12
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).setCancelable(false).create();
        } else if (i == 8) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.satellite_low_battery_title).setMessage(R.string.satellite_low_battery_super_save_power_message).setPositiveButton(R.string.satellite_low_battery_confirm, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.14
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                    SatelliteDialogActivity.this.openSuperSaveMode();
                    SatelliteManagerBase.getInstance().openSatellite(SatelliteDialogActivity.this.mSlotId, SatelliteDialogActivity.this.mFlag);
                }
            }).setNegativeButton(R.string.satellite_low_battery_cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.13
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).setCancelable(false).create();
        } else if (i == 11) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.satellite_pa_megathermal_title).setMessage(this.mContext.getResources().getQuantityString(R.plurals.satellite_pa_megathermal_turn_off_message, 10, 10)).setPositiveButton(R.string.satellite_exit, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.16
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                    SatelliteManagerBase.getInstance().setSatelliteEnableByUser(SatelliteDialogActivity.this.mContext, false, 1010);
                    SatelliteTimer satelliteTimer = SatelliteDialogActivity.mTimer;
                    if (satelliteTimer != null) {
                        satelliteTimer.destroy();
                        SatelliteDialogActivity.mTimer = null;
                    }
                }
            }).setNegativeButton(R.string.satellite_start, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.15
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).create();
            updatePaDialogMessage(this.mContext);
        } else if (i == 12) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.satellite_unable_open_title).setMessage(R.string.satellite_pa_megathermal_message).setPositiveButton(R.string.satellite_start, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.17
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).setCancelable(false).create();
        } else if (i == 13) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setMessage(R.string.satellite_exit_tips).setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.19
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                    SatelliteManagerBase.getInstance().setSatelliteEnableByUser(SatelliteDialogActivity.this.mContext, false, 100101);
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.18
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).setCancelable(false).create();
        } else if (i == 14) {
            this.mAlertDialog = new AlertDialog.Builder(this, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.satellite_flash_time_out_title).setMessage(R.string.satellite_flash_time_out_message).setPositiveButton(R.string.satellite_start, new DialogInterface.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.20
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SatelliteDialogActivity.this.finish();
                }
            }).setCancelable(false).create();
        }
        this.mAlertDialog.setCanceledOnTouchOutside(false);
        this.mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                this.f$0.lambda$onCreateDialog$0(i, dialogInterface);
            }
        });
        clearDialogAnimations(this.mAlertDialog);
        return this.mAlertDialog;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(int i, DialogInterface dialogInterface) {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog == null) {
            return;
        }
        hideDialogDimBg(alertDialog);
        if (i == 4) {
            if (this.mAlertDialog.getMessageView() != null) {
                this.mAlertDialog.getMessageView().setMovementMethod(LinkMovementMethod.getInstance());
            }
        } else if (i == 7) {
            updateNegativeButton(this.mContext);
        }
    }

    public void openSuperSaveMode() {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("POWER_SUPERSAVE_MODE_OPEN", true);
            bundle.putBoolean("POWER_SUPERSAVE_MODE_FROMUSER", false);
            this.mContext.getContentResolver().call(Uri.parse("content://com.miui.powercenter.powersaver"), "changeSuperPowerMode", (String) null, bundle);
        } catch (Exception e) {
            Log.e("SatelliteDialogActivity", "openSuperSaveMode error:" + e);
        }
    }

    public void updateDialogMessage(final Context context) {
        if (this.mAlertDialog != null && mTimer == null) {
            final long jCurrentTimeMillis = System.currentTimeMillis();
            SatelliteTimer satelliteTimer = new SatelliteTimer(new Runnable() { // from class: com.android.phone.satellite.SatelliteDialogActivity$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateDialogMessage$1(jCurrentTimeMillis, context);
                }
            });
            mTimer = satelliteTimer;
            satelliteTimer.start(1000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogMessage$1(long j, Context context) {
        long jCurrentTimeMillis = 10 - ((System.currentTimeMillis() - j) / 1000);
        if (jCurrentTimeMillis < 1) {
            SatelliteManagerBase.getInstance().setSatelliteEnableByUser(this.mContext, false, 1005);
            finish();
            AlertDialog alertDialog = this.mAlertDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            mTimer.destroy();
            mTimer = null;
            return;
        }
        AlertDialog alertDialog2 = this.mAlertDialog;
        if (alertDialog2 != null) {
            alertDialog2.setMessage(context.getResources().getQuantityString(R.plurals.satellite_low_battery_turn_off_message, (int) jCurrentTimeMillis, Long.valueOf(jCurrentTimeMillis)));
        }
    }

    public void updatePaDialogMessage(final Context context) {
        if (this.mAlertDialog != null && mTimer == null) {
            final long jCurrentTimeMillis = System.currentTimeMillis();
            SatelliteTimer satelliteTimer = new SatelliteTimer(new Runnable() { // from class: com.android.phone.satellite.SatelliteDialogActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updatePaDialogMessage$2(jCurrentTimeMillis, context);
                }
            });
            mTimer = satelliteTimer;
            satelliteTimer.start(1000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePaDialogMessage$2(long j, Context context) {
        long jCurrentTimeMillis = 10 - ((System.currentTimeMillis() - j) / 1000);
        if (jCurrentTimeMillis < 1) {
            SatelliteManagerBase.getInstance().setSatelliteEnableByUser(this.mContext, false, 1010);
            finish();
            AlertDialog alertDialog = this.mAlertDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            mTimer.destroy();
            mTimer = null;
            return;
        }
        AlertDialog alertDialog2 = this.mAlertDialog;
        if (alertDialog2 != null) {
            alertDialog2.setMessage(context.getResources().getQuantityString(R.plurals.satellite_pa_megathermal_turn_off_message, (int) jCurrentTimeMillis, Long.valueOf(jCurrentTimeMillis)));
        }
    }

    public void updateNegativeButton(final Context context) {
        AlertDialog alertDialog;
        final SatelliteDialogActivity satelliteDialogActivity;
        if (context == null || (alertDialog = this.mAlertDialog) == null) {
            return;
        }
        final Button button = alertDialog.getButton(-2);
        Button button2 = this.mAlertDialog.getButton(-1);
        if (mPrivacyWithdrawalTimer == null) {
            final long jCurrentTimeMillis = System.currentTimeMillis();
            satelliteDialogActivity = this;
            SatelliteTimer satelliteTimer = satelliteDialogActivity.new SatelliteTimer(new Runnable() { // from class: com.android.phone.satellite.SatelliteDialogActivity$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateNegativeButton$3(jCurrentTimeMillis, button, context);
                }
            });
            mPrivacyWithdrawalTimer = satelliteTimer;
            satelliteTimer.start(1000L);
        } else {
            satelliteDialogActivity = this;
        }
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.22
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SatelliteDialogActivity.mPrivacyWithdrawalTimer != null) {
                    SatelliteDialogActivity.mPrivacyWithdrawalTimer.destroy();
                    SatelliteDialogActivity.mPrivacyWithdrawalTimer = null;
                }
                SatelliteDialogActivity.this.finish();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateNegativeButton$3(long j, Button button, final Context context) {
        long jCurrentTimeMillis = 10 - ((System.currentTimeMillis() - j) / 1000);
        if (jCurrentTimeMillis < 1) {
            button.setText(R.string.satellite_privacy_withdrawal_dialog_enable_negative);
            button.setEnabled(true);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.satellite.SatelliteDialogActivity.21
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    SatelliteDialogActivity.this.finish();
                    if (SatelliteManagerBase.getInstance().isSatelliteEnabled()) {
                        SatelliteManagerBase.getInstance().setSatelliteEnableByUser(context, false, 1009);
                    }
                    SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
                    editorEdit.putBoolean(SatelliteDialogActivity.AGREE_SATELLITE_PRIVACY_STATEMENT, false);
                    editorEdit.commit();
                    SystemProperties.set("persist.radio.satellite.showAgain", "true");
                }
            });
            mPrivacyWithdrawalTimer.destroy();
            mPrivacyWithdrawalTimer = null;
            return;
        }
        button.setText(context.getResources().getQuantityString(R.plurals.satellite_privacy_withdrawal_dialog_disable_negative, (int) jCurrentTimeMillis, Long.valueOf(jCurrentTimeMillis)));
        button.setEnabled(false);
    }

    public class SatelliteTimer extends Handler {
        private Runnable mCallback;
        private long mInterval = 0;
        private long mLastReportedTime = 0;
        private boolean mRunning = false;
        private Runnable mInternalCallback = new SatelliteTimerCallback();

        public SatelliteTimer(Runnable runnable) {
            this.mCallback = runnable;
        }

        public boolean start(long j) {
            if (j <= 0) {
                return false;
            }
            cancel();
            this.mInterval = j;
            this.mLastReportedTime = SystemClock.uptimeMillis();
            this.mRunning = true;
            periodicUpdateTimer();
            return true;
        }

        public void cancel() {
            removeCallbacks(this.mInternalCallback);
            this.mRunning = false;
        }

        public void destroy() {
            cancel();
            SatelliteDialogActivity.this.mAlertDialog = null;
            this.mCallback = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void periodicUpdateTimer() {
            if (!this.mRunning) {
                return;
            }
            long jUptimeMillis = SystemClock.uptimeMillis();
            long j = this.mLastReportedTime;
            long j2 = this.mInterval;
            while (true) {
                j += j2;
                if (jUptimeMillis >= j) {
                    j2 = this.mInterval;
                } else {
                    postAtTime(this.mInternalCallback, j);
                    this.mLastReportedTime = j;
                    this.mCallback.run();
                    return;
                }
            }
        }

        class SatelliteTimerCallback implements Runnable {
            private SatelliteTimerCallback() {
            }

            @Override // java.lang.Runnable
            public void run() {
                SatelliteTimer.this.periodicUpdateTimer();
            }
        }
    }

    private View getTitleView() {
        View viewInflate = this.mInflater.inflate(R.layout.satellite_sim_pick_title, (ViewGroup) null);
        viewInflate.findViewById(R.id.txt_title);
        return viewInflate;
    }

    private void initAdapter() {
        String string;
        SatelliteDialogActivity satelliteDialogActivity;
        this.mSelectorAdapter = new SelectorAdapter(this.mInflater);
        if (MiuiSettings.VirtualSim.isVirtualSimEnabled(this.mContext)) {
            MiuiSettings.VirtualSim.getVirtualSimSlotId(this.mContext);
        } else {
            int i = SubscriptionManager.INVALID_SLOT_ID;
        }
        int size = this.mSimPhoneAccountHandles.size();
        int i2 = 0;
        while (i2 < size) {
            SubscriptionInfo subscriptionInfoForSlot = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(i2);
            if (subscriptionInfoForSlot == null) {
                satelliteDialogActivity = this;
            } else {
                int slotId = subscriptionInfoForSlot.getSlotId();
                String string2 = subscriptionInfoForSlot.getDisplayName().toString();
                String displayNumber = subscriptionInfoForSlot.getDisplayNumber();
                if (displayNumber.length() == 0) {
                    string = "";
                } else {
                    string = displayNumber.toString();
                    if (string.contains("+")) {
                        string = string.substring(0, 3) + " " + string.substring(3, string.length());
                    }
                }
                satelliteDialogActivity = this;
                this.mSelectorAdapter.add(new AccountInfo(satelliteDialogActivity, 1, true, slotId, string2, string, (PhoneAccountHandle) this.mSimPhoneAccountHandles.get(i2)));
            }
            i2++;
            this = satelliteDialogActivity;
        }
    }

    class SelectorAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private List mOptionList = new ArrayList();

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        SelectorAdapter(LayoutInflater layoutInflater) {
            this.mLayoutInflater = layoutInflater;
        }

        public void add(AccountInfo accountInfo) {
            this.mOptionList.add(accountInfo);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mOptionList.size();
        }

        @Override // android.widget.Adapter
        public AccountInfo getItem(int i) {
            return (AccountInfo) this.mOptionList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = SatelliteDialogActivity.this.mInflater.inflate(R.layout.satellite_sim_pick_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.mDisplayName = (TextView) view.findViewById(R.id.txt_displayname);
                viewHolder.mIcon = (ImageView) view.findViewById(R.id.img_icon);
                viewHolder.mDescription = (TextView) view.findViewById(R.id.txt_description);
                viewHolder.mExtra = (TextView) view.findViewById(R.id.txt_extra);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            AccountInfo item = getItem(i);
            item.getType();
            String description = item.getDescription();
            viewHolder.mDisplayName.setText(item.getName());
            viewHolder.mDescription.setText(description);
            viewHolder.mDescription.setVisibility(TextUtils.isEmpty(description) ? 8 : 0);
            viewHolder.mIcon.setVisibility(0);
            viewHolder.mExtra.setVisibility(8);
            viewHolder.setEnabled(item.isActive());
            viewHolder.mIcon.setImageResource(item.mId == 0 ? R.drawable.ic_sim_icon_1 : R.drawable.ic_sim_icon_2);
            viewHolder.mIcon.setImageTintList(getSimColor(SatelliteDialogActivity.this.mContext));
            viewHolder.mIcon.setContentDescription(getSimDes(SatelliteDialogActivity.this.mContext, item.mId));
            if (item.isActive()) {
                if (item.isDefault()) {
                    String string = SatelliteDialogActivity.this.mContext.getResources().getString(R.string.msim_select_default_last);
                    viewHolder.mExtra.setTextSize(SatelliteDialogActivity.this.mContext.getResources().getDimension(R.dimen.select_sim_extra_text_size) / SatelliteDialogActivity.this.mContext.getResources().getDisplayMetrics().density);
                    viewHolder.mExtra.setText(string);
                    viewHolder.mExtra.setVisibility(0);
                }
                return view;
            }
            viewHolder.mExtra.setVisibility(0);
            viewHolder.mExtra.setText(R.string.can_not_use);
            return view;
        }

        public ColorStateList getSimColor(Context context) {
            return new ColorStateList(new int[][]{new int[0]}, new int[]{context.getResources().getColor(R.color.select_sim_color)});
        }

        private String getSimDes(Context context, int i) {
            if (i == 0) {
                return context.getResources().getString(R.string.sim1_description, 1);
            }
            return context.getResources().getString(R.string.sim2_description, 2);
        }

        class ViewHolder {
            public TextView mDescription;
            public TextView mDisplayName;
            public TextView mExtra;
            public ImageView mIcon;

            private ViewHolder(SelectorAdapter selectorAdapter) {
            }

            public void setEnabled(boolean z) {
                this.mDisplayName.setEnabled(z);
                this.mIcon.setEnabled(z);
                this.mDescription.setEnabled(z);
                this.mExtra.setEnabled(z);
            }
        }
    }

    public class AccountInfo {
        private PhoneAccountHandle mAccount;
        private String mDescription;
        private int mId;
        private boolean mIsActive;
        private boolean mIsDefault;
        private String mName;
        private int mType;

        AccountInfo(SatelliteDialogActivity satelliteDialogActivity, int i, boolean z, int i2, CharSequence charSequence, String str, PhoneAccountHandle phoneAccountHandle) {
            this.mType = i;
            this.mId = i2;
            this.mIsActive = z;
            this.mName = charSequence != null ? charSequence.toString() : "";
            this.mDescription = str;
            this.mAccount = phoneAccountHandle;
            this.mIsDefault = false;
        }

        public boolean isDefault() {
            return this.mIsDefault;
        }

        public boolean isActive() {
            return this.mIsActive;
        }

        public int getType() {
            return this.mType;
        }

        public String getName() {
            return this.mName;
        }

        public String getDescription() {
            return this.mDescription;
        }

        public PhoneAccountHandle getPhoneAccount() {
            return this.mAccount;
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mAlertDialog.dismiss();
        }
        SatelliteTimer satelliteTimer = mPrivacyWithdrawalTimer;
        if (satelliteTimer != null) {
            satelliteTimer.destroy();
            mPrivacyWithdrawalTimer = null;
        }
        SatelliteTimer satelliteTimer2 = mTimer;
        if (satelliteTimer2 != null) {
            satelliteTimer2.destroy();
            mTimer = null;
        }
        int intExtra = intent.getIntExtra("type", 0);
        this.mFlag = intent.getIntExtra("flag_id", 0);
        this.mSlotId = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, 0);
        showDialog(intExtra);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        SatelliteTimer satelliteTimer = mPrivacyWithdrawalTimer;
        if (satelliteTimer != null) {
            satelliteTimer.destroy();
            mPrivacyWithdrawalTimer = null;
            finish();
        }
    }

    private void hideDialogDimBg(Dialog dialog) {
        View viewFindViewById;
        if (dialog == null || (viewFindViewById = dialog.findViewById(R$id.dialog_dim_bg)) == null) {
            return;
        }
        viewFindViewById.setVisibility(8);
    }

    private void clearDialogAnimations(Dialog dialog) {
        final View decorView;
        if (dialog == null || dialog.getWindow() == null || (decorView = dialog.getWindow().getDecorView()) == null) {
            return;
        }
        decorView.post(new Runnable() { // from class: com.android.phone.satellite.SatelliteDialogActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$clearDialogAnimations$4(decorView);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: clearViewAnimations, reason: merged with bridge method [inline-methods] */
    public void lambda$clearDialogAnimations$4(View view) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                lambda$clearDialogAnimations$4(viewGroup.getChildAt(i));
            }
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        SatelliteTimer satelliteTimer = mPrivacyWithdrawalTimer;
        if (satelliteTimer != null) {
            satelliteTimer.destroy();
            mPrivacyWithdrawalTimer = null;
        }
        SatelliteTimer satelliteTimer2 = mTimer;
        if (satelliteTimer2 != null) {
            satelliteTimer2.destroy();
            mTimer = null;
        }
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mAlertDialog = null;
        }
    }
}
