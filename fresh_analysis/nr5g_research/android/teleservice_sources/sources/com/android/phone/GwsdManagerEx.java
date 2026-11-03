package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.ims.ProvisioningManager;
import android.text.Html;
import com.android.internal.os.BackgroundThread;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class GwsdManagerEx extends GwsdManagerBase {
    private static String LOG_TAG = "GwsdManagerQc";
    public static final String VICE_SLOT_VOLTE_DATA_ENABLED;
    private ContentObserver mGwsdContentObserver;
    private boolean mIsGameBoosting;
    private AlertDialog mDialog = null;
    final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.GwsdManagerEx.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            GwsdManagerEx.this.logd("onReceive: intentAction = " + action);
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                GwsdManagerEx.this.onConnectivityChanged();
                return;
            }
            if ("miui.intent.action.GWSD_AUTO_REJECT".equals(intent.getAction())) {
                if (GwsdManagerEx.this.mIsGameBoosting) {
                    int phoneIdExtra = SubscriptionManager.getPhoneIdExtra(intent, -1);
                    int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                    GwsdManagerEx.this.logd("onReceive gwsd auto reject action, phoneId:" + phoneIdExtra + ", cur dds: " + defaultDataSlotId);
                    if (phoneIdExtra == defaultDataSlotId) {
                        return;
                    }
                    GwsdManagerEx.this.maybeUpdateGwsd(false);
                    MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "telephony_gwsd_call");
                    return;
                }
                int intExtra = intent.getIntExtra("extra_failCause", 0);
                HashMap map = new HashMap();
                map.put("telephony_epsfb_auto_rejected_call_reason", String.valueOf(intExtra));
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "telephony_epsfb_auto_rejected_call", map);
            }
        }
    };
    final BroadcastReceiver mDsdaStateChangedReceiver = new BroadcastReceiver() { // from class: com.android.phone.GwsdManagerEx.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            GwsdManagerEx.this.logd("receive msim voice capability changed");
            if (GwsdManagerEx.this.mHandler.hasMessages(3)) {
                GwsdManagerEx.this.mHandler.removeMessages(3);
            }
            GwsdManagerEx.this.mHandler.sendEmptyMessageDelayed(3, 1500L);
        }
    };
    private Context mContext = PhoneGlobals.getInstance();
    private Handler mHandler = new AutoRejectImsHandler(BackgroundThread.get().getLooper());
    private boolean mHasShownViceSlotVolteDataDialog = false;
    private boolean mIsEnhancedDsdaSupport = TelephonyManagerEx.getDefault().isEnhancedDsdaSupported();

    static {
        VICE_SLOT_VOLTE_DATA_ENABLED = (ModemProvider.IS_MTK || ModemProvider.IS_XRING) ? "data_service_enabled" : "vice_slot_volte_data_enabled";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateGameBoosting() {
        this.mIsGameBoosting = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "gb_gwsd", 0, -2) == 1;
        logd("updateGameBoosting mIsGameBoosting:" + this.mIsGameBoosting);
        if (!this.mIsGameBoosting) {
            dismissAlertDialog();
        }
        maybeUpdateGwsd(true);
    }

    protected GwsdManagerEx() {
        this.mGwsdContentObserver = null;
        logd("enhanced dsda support : " + this.mIsEnhancedDsdaSupport);
        updateGameBoosting();
        this.mGwsdContentObserver = new ContentObserver(new Handler(this.mContext.getMainLooper())) { // from class: com.android.phone.GwsdManagerEx.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                GwsdManagerEx.this.logd("GwsdContent changed!");
                GwsdManagerEx.this.updateGameBoosting();
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("gb_gwsd"), false, this.mGwsdContentObserver);
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("miui.intent.action.GWSD_AUTO_REJECT");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        if (this.mIsEnhancedDsdaSupport) {
            this.mContext.registerReceiver(this.mDsdaStateChangedReceiver, new IntentFilter("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED"), "com.qti.permission.RECEIVE_MSIM_VOICE_CAPABILITY_CHANGED", null, 2);
        }
    }

    public void onDsdaStateChanged() {
        if (!this.mIsGameBoosting || TelephonyManagerEx.getDefault().isConcurrentCallsPossible()) {
            return;
        }
        if (!this.mHasShownViceSlotVolteDataDialog) {
            showViceSlotVolteDataDialogIfNeeded();
        }
        maybeUpdateGwsd(false);
    }

    public class AutoRejectImsHandler extends Handler {
        public AutoRejectImsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                GwsdManagerEx.this.updateGwsd(message.arg1 == 1);
            } else {
                if (i != 3) {
                    return;
                }
                GwsdManagerEx.this.onDsdaStateChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateGwsd(boolean z) {
        int i = 0;
        while (i < MiuiPhoneUtils.PHONE_COUNT) {
            int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
            if (!SubscriptionManager.isValidSubscriptionId(subscriptionIdForSlot)) {
                logd("updateGwsd invalid subid!");
            } else {
                int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                boolean zIsWifiConnected = MiuiPhoneUtils.isWifiConnected();
                ProvisioningManager provisionManager = getProvisionManager(subscriptionIdForSlot);
                int i2 = (!this.mIsGameBoosting || zIsWifiConnected || i == defaultDataSlotId) ? 0 : 1;
                logd("updateGwsd mIsGameBoosting : " + this.mIsGameBoosting + " isWifiConnected : " + zIsWifiConnected + " isIncludingDsda :" + z);
                if (z && this.mIsEnhancedDsdaSupport) {
                    boolean zIsConcurrentCallsPossible = TelephonyManagerEx.getDefault().isConcurrentCallsPossible();
                    logd("updateGwsd isDsda : " + zIsConcurrentCallsPossible);
                    i2 = (i2 == 0 || zIsConcurrentCallsPossible) ? 0 : 1;
                }
                if (provisionManager != null) {
                    try {
                        int provisioningIntValue = provisionManager.getProvisioningIntValue(1000);
                        logd("updateGwsd provisioningManager slotId=" + i + " dds=" + defaultDataSlotId + " oldProvision= " + provisioningIntValue + " newValue:" + i2);
                        if (provisioningIntValue != i2 && (provisioningIntValue != -1 || i2 != 0)) {
                            provisionManager.setProvisioningIntValue(1000, i2);
                            if (i2 != 0) {
                                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "telephony_gwsd_option");
                            }
                        }
                    } catch (RuntimeException e) {
                        loge("setAutoRejectCall failed. Exception = " + e);
                    }
                }
            }
            i++;
        }
    }

    private ProvisioningManager getProvisionManager(int i) {
        try {
            return ProvisioningManager.createForSubscriptionId(i);
        } catch (IllegalArgumentException unused) {
            loge("ProvisioningManager is not available");
            return null;
        }
    }

    public void onConnectivityChanged() {
        if (this.mIsGameBoosting) {
            maybeUpdateGwsd(false);
        }
    }

    public boolean showViceSlotVolteDataDialogIfNeeded() {
        boolean zIsWifiConnected = MiuiPhoneUtils.isWifiConnected();
        boolean zIsViceSlotPhoneInCall = MiuiPhoneUtils.isViceSlotPhoneInCall();
        boolean z = false;
        boolean z2 = Settings.Global.getInt(this.mContext.getContentResolver(), VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 1;
        if (!this.mHasShownViceSlotVolteDataDialog && !zIsWifiConnected && zIsViceSlotPhoneInCall && !z2) {
            z = true;
        }
        logd("showViceSlotVolteDataDialogIfNeeded needShow : " + z + " hasShown : " + this.mHasShownViceSlotVolteDataDialog + " isWifiConnected : " + zIsWifiConnected + " isViceSlotPhoneInCall : " + zIsViceSlotPhoneInCall + " isViceSlotVolteDataEnabled : " + z2);
        if (z) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.phone.GwsdManagerEx.4
                @Override // java.lang.Runnable
                public void run() {
                    GwsdManagerEx.this.showViceSlotVolteDataDialog();
                }
            });
        }
        return z;
    }

    public void showViceSlotVolteDataDialog() {
        String string;
        dismissAlertDialog();
        final boolean zShouldHideIntelligentDualSimButton = MiuiPhoneUtils.shouldHideIntelligentDualSimButton();
        Context context = this.mContext;
        if (zShouldHideIntelligentDualSimButton) {
            string = context.getResources().getString(R.string.vice_slot_volte_data_dialog_message);
        } else {
            string = context.getResources().getString(R.string.vice_slot_volte_data_dialog_message_ext);
        }
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this.mContext, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setMessage(Html.fromHtml(string)).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).setPositiveButton(R.string.vice_slot_volte_data_dialog_open, new DialogInterface.OnClickListener() { // from class: com.android.phone.GwsdManagerEx.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Settings.Global.putInt(GwsdManagerEx.this.mContext.getContentResolver(), "smart_dual_sim", 1);
                Settings.Global.putInt(GwsdManagerEx.this.mContext.getContentResolver(), GwsdManagerEx.VICE_SLOT_VOLTE_DATA_ENABLED, 1);
                if (!zShouldHideIntelligentDualSimButton) {
                    Settings.Global.putInt(GwsdManagerEx.this.mContext.getContentResolver(), "dual_mobile_data", 1);
                }
                GwsdManagerEx.this.logd("showViceSlotVolteDataDialog ok, set ViceSlotVolteData enable.");
            }
        }).create();
        this.mDialog = alertDialogCreate;
        alertDialogCreate.getWindow().setType(2003);
        this.mDialog.getWindow().addFlags(131072);
        this.mDialog.getWindow().addPrivateFlags(524288);
        this.mDialog.show();
        this.mHasShownViceSlotVolteDataDialog = true;
    }

    private void dismissAlertDialog() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeUpdateGwsd(boolean z) {
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1, z ? 1 : 0, 0), 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Rlog.d(LOG_TAG, str);
    }

    private void loge(String str) {
        Rlog.e(LOG_TAG, str);
    }
}
