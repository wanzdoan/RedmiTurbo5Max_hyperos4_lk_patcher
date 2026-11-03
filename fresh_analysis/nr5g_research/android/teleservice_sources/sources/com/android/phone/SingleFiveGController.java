package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import com.android.phone.settings.MultiSimInfoEditorActivity;
import com.android.phone.settings.PreferredNetworkTypeListPreference;
import com.android.phone.utils.FiveGUtils;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class SingleFiveGController implements FiveGManagerBase.IFiveGCarrierConfigChangedListener {
    private static String LOG_TAG = "SingleFiveGController";
    protected static volatile SingleFiveGController sInstance = null;
    private static boolean sIsDeviceProvisioned = false;
    private AlertDialog mDualSimDialog;
    private SingelFiveGHandler mSingelFiveGHandler;
    private Handler mMainHandler = new Handler() { // from class: com.android.phone.SingleFiveGController.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            int insertedSimCount = MiuiPhoneUtils.getInsertedSimCount();
            boolean zIsUserFiveGEnabled = SingleFiveGController.this.isUserFiveGEnabled();
            FiveGManagerBase.getInstance();
            boolean zIs5GSwitchCarrierAvailable = FiveGManagerBase.is5GSwitchCarrierAvailable(DefaultSimManager.getDefaultDataSlotId());
            boolean zIsAllSimActivated = FiveGUtils.isAllSimActivated();
            SingleFiveGController.log("inserted sim count is : " + insertedSimCount + ", isUserFiveGEnabled: " + zIsUserFiveGEnabled + ", is5GSwitchCarrierAvailable: " + zIs5GSwitchCarrierAvailable + ", allSimActivated: " + zIsAllSimActivated + ", sIsDeviceProvisioned: " + SingleFiveGController.sIsDeviceProvisioned);
            if (insertedSimCount < 2 || !zIsUserFiveGEnabled || !zIs5GSwitchCarrierAvailable || !zIsAllSimActivated || !SingleFiveGController.sIsDeviceProvisioned) {
                if (SingleFiveGController.this.mDualSimDialog != null) {
                    SingleFiveGController.this.mDualSimDialog.dismiss();
                    return;
                }
                return;
            }
            SingleFiveGController.this.showSingle5GPromptDialog();
        }
    };
    private Context mContext = PhoneGlobals.getInstance();

    public class SimStatus {
        int slotId = -1;
        int networkType = -1;
        boolean enabled = false;
        boolean isActivated = false;
        int futureDataSlotId = -1;
        boolean successToSwitchDataSlot = false;
        int deactivateSlotId = -1;
        boolean successToDeactivateSim = false;
        boolean needToEnable5G = false;
        boolean successToEnable5G = false;
        public int switchDataSlotRetryCount = 0;
        public int deactivateSimRetryCount = 0;

        public SimStatus(SingleFiveGController singleFiveGController) {
        }

        public SimStatus futureDataSlotId(int i) {
            this.futureDataSlotId = i;
            return this;
        }

        public SimStatus deactivateSlotId(int i) {
            this.deactivateSlotId = i;
            return this;
        }

        public SimStatus needToEnable5G(boolean z) {
            this.needToEnable5G = z;
            return this;
        }

        public String toString() {
            return "SimStatus: {slotId=" + this.slotId + ", networkType=" + this.networkType + ", enabled=" + this.enabled + ", isActivated=" + this.isActivated + ", futureDataSlotId=" + this.futureDataSlotId + ", successToSwitchDataSlot=" + this.successToSwitchDataSlot + ", switchDataSlotRetryCount=" + this.switchDataSlotRetryCount + ", deactivateSlotId=" + this.deactivateSlotId + ", successToDeactivateSim=" + this.successToDeactivateSim + ", deactivateSimRetryCount=" + this.deactivateSimRetryCount + ", needToEnable5G=" + this.needToEnable5G + " }";
        }
    }

    public static SingleFiveGController getInstance() {
        return sInstance;
    }

    private SingleFiveGController() {
        HandlerThread handlerThread = new HandlerThread("SingleFiveG");
        handlerThread.start();
        this.mSingelFiveGHandler = new SingelFiveGHandler(handlerThread.getLooper());
        registerDeviceStateChanged(this.mContext);
    }

    public static SingleFiveGController init() {
        if (sInstance == null) {
            synchronized (SingleFiveGController.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new SingleFiveGController();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public class SingelFiveGHandler extends Handler {
        public SingelFiveGHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                SimStatus simStatus = (SimStatus) ((AsyncResult) message.obj).userObj;
                SingleFiveGController.log("set preferred network type to DB, slotId" + simStatus.slotId + ", networkType: " + simStatus.networkType);
                MiuiPhoneUtils.setNetworkModeInDb(null, simStatus.slotId, simStatus.networkType);
                return;
            }
            switch (i) {
                case 2:
                    int i2 = message.arg1;
                    int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                    int otherSlotId = FiveGUtils.getOtherSlotId(i2);
                    SingleFiveGController.log("deactivateSimAndEnable5G, click ok, currentDataSlotId" + defaultDataSlotId + ", futureDataSlotId: " + otherSlotId + ", deactivateSlotId: " + i2);
                    SimStatus simStatusNeedToEnable5G = new SimStatus(SingleFiveGController.this).futureDataSlotId(otherSlotId).deactivateSlotId(i2).needToEnable5G(true);
                    if (defaultDataSlotId != otherSlotId) {
                        SingleFiveGController.log("switch data slot, " + simStatusNeedToEnable5G);
                        SingleFiveGController.this.switchDataSlot(simStatusNeedToEnable5G);
                    } else {
                        simStatusNeedToEnable5G.successToSwitchDataSlot = true;
                        SingleFiveGController.log("deactivate sim, " + simStatusNeedToEnable5G);
                        SingleFiveGController.this.deactivateSim(simStatusNeedToEnable5G);
                    }
                    break;
                case 3:
                    SimStatus simStatus2 = (SimStatus) message.obj;
                    SingleFiveGController.log("switch data slot " + simStatus2.futureDataSlotId);
                    SubscriptionManager.getDefault().setDefaultDataSlotId(simStatus2.futureDataSlotId);
                    simStatus2.switchDataSlotRetryCount = 0;
                    SingleFiveGController.this.retryToSwitchDataSlot(simStatus2);
                    break;
                case 4:
                    SimStatus simStatus3 = (SimStatus) message.obj;
                    if (simStatus3.futureDataSlotId == DefaultSimManager.getDefaultDataSlotId()) {
                        simStatus3.successToSwitchDataSlot = true;
                        SingleFiveGController.log("switch data slot is sucessful, " + simStatus3);
                        SingleFiveGController.this.deactivateSim(simStatus3);
                    } else {
                        int i3 = simStatus3.switchDataSlotRetryCount;
                        if (i3 >= 3) {
                            SingleFiveGController.log("switch data slot is fail, stop retry, " + simStatus3);
                        } else {
                            simStatus3.switchDataSlotRetryCount = i3 + 1;
                            SingleFiveGController.log("switch data slot is fail, retring, " + simStatus3);
                            SubscriptionManager.getDefault().setDefaultDataSlotId(simStatus3.futureDataSlotId);
                            SingleFiveGController.this.retryToSwitchDataSlot(simStatus3);
                        }
                    }
                    break;
                case 5:
                    SimStatus simStatus4 = (SimStatus) message.obj;
                    SingleFiveGController.log("deactivate sim slot " + simStatus4.deactivateSlotId);
                    ModemProvider.getInstance().setIccCardActivate(simStatus4.deactivateSlotId, false);
                    simStatus4.deactivateSimRetryCount = 0;
                    SingleFiveGController.this.retryToDeactivateSim(simStatus4);
                    break;
                case 6:
                    SimStatus simStatus5 = (SimStatus) message.obj;
                    if (FiveGUtils.isSimDeactivated(simStatus5.deactivateSlotId)) {
                        simStatus5.successToDeactivateSim = true;
                        SingleFiveGController.log("deactivate sim is sucessful, " + simStatus5);
                        SingleFiveGController.this.enableUserFiveG(simStatus5);
                    } else {
                        int i4 = simStatus5.deactivateSimRetryCount;
                        if (i4 >= 3) {
                            SingleFiveGController.log("deactivate sim is fail, stop retry,, " + simStatus5);
                        } else {
                            simStatus5.deactivateSimRetryCount = i4 + 1;
                            SingleFiveGController.log("deactivate sim is fail, retring, " + simStatus5);
                            ModemProvider.getInstance().setIccCardActivate(simStatus5.deactivateSlotId, false);
                            SingleFiveGController.this.retryToDeactivateSim(simStatus5);
                        }
                    }
                    break;
                case 7:
                    SimStatus simStatus6 = (SimStatus) message.obj;
                    SingleFiveGController.log("enable 5G, " + simStatus6);
                    FiveGManagerBase.getInstance().setUserFiveGEnabled(simStatus6.needToEnable5G);
                    break;
            }
        }
    }

    @Override // com.android.phone.FiveGManagerBase.IFiveGCarrierConfigChangedListener
    public void onDataSlotCarrierConfigChanged() {
        this.mMainHandler.removeMessages(1);
        this.mMainHandler.sendEmptyMessageDelayed(1, 2000L);
    }

    public boolean isUserFiveGEnabled() {
        return FiveGManagerBase.getInstance().isUserFiveGEnabled();
    }

    public void enableAllSimCards() {
        FiveGManagerBase.getInstance().setUserFiveGEnabled(false);
    }

    public void deactivateSimAndEnable5G(int i) {
        if (SubscriptionManager.isValidSlotId(i)) {
            Message messageObtainMessage = this.mSingelFiveGHandler.obtainMessage(2);
            messageObtainMessage.arg1 = i;
            this.mSingelFiveGHandler.sendMessage(messageObtainMessage);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchDataSlot(SimStatus simStatus) {
        Message messageObtainMessage = this.mSingelFiveGHandler.obtainMessage(3);
        messageObtainMessage.obj = simStatus;
        this.mSingelFiveGHandler.sendMessage(messageObtainMessage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retryToSwitchDataSlot(SimStatus simStatus) {
        Message messageObtainMessage = this.mSingelFiveGHandler.obtainMessage(4);
        messageObtainMessage.obj = simStatus;
        this.mSingelFiveGHandler.sendMessageDelayed(messageObtainMessage, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deactivateSim(SimStatus simStatus) {
        Message messageObtainMessage = this.mSingelFiveGHandler.obtainMessage(5);
        messageObtainMessage.obj = simStatus;
        this.mSingelFiveGHandler.sendMessage(messageObtainMessage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retryToDeactivateSim(SimStatus simStatus) {
        Message messageObtainMessage = this.mSingelFiveGHandler.obtainMessage(6);
        messageObtainMessage.obj = simStatus;
        this.mSingelFiveGHandler.sendMessageDelayed(messageObtainMessage, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableUserFiveG(SimStatus simStatus) {
        Message messageObtainMessage = this.mSingelFiveGHandler.obtainMessage(7);
        messageObtainMessage.obj = simStatus;
        this.mSingelFiveGHandler.sendMessage(messageObtainMessage);
    }

    public boolean showEnable5GDialog(int i) {
        if (!FiveGUtils.isSupportSingleFiveG() || !FiveGUtils.isViceSlotActivated()) {
            return false;
        }
        final PreferredNetworkTypeListPreference.PreferredNetworkTypeListPreferenceFragment fragment = PreferredNetworkTypeListPreference.getFragment();
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.SingleFiveGController.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                if (i2 == -1) {
                    SingleFiveGController.log("enable5GDialog, click ok");
                    SingleFiveGController.this.deactivateSimAndEnable5G(FiveGUtils.getViceSlotId());
                } else {
                    PreferredNetworkTypeListPreference.PreferredNetworkTypeListPreferenceFragment preferredNetworkTypeListPreferenceFragment = fragment;
                    if (preferredNetworkTypeListPreferenceFragment != null) {
                        preferredNetworkTypeListPreferenceFragment.updateNetworkTypeUI();
                    }
                    SingleFiveGController.log("enable5GDialog, click cancel");
                }
            }
        };
        new AlertDialog.Builder(fragment.getActivity(), miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setTitle(R.string.single_fiveg_dialog_title_remind).setMessage(R.string.single_fiveg_enable_fiveg_dialog_msg_enable_fiveg).setPositiveButton(R.string.single_fivegg_enable_fiveg_dialog_btn_enable_sim, onClickListener).setNegativeButton(android.R.string.cancel, onClickListener).setCancelable(false).create().show();
        return true;
    }

    public boolean showDisable5GDialog() {
        if (!FiveGUtils.isSupportSingleFiveG() || !isUserFiveGEnabled()) {
            return false;
        }
        final MultiSimInfoEditorActivity currentInstance = MultiSimInfoEditorActivity.getCurrentInstance();
        new AlertDialog.Builder(currentInstance, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setTitle(R.string.single_fiveg_dialog_title_prompt).setMessage(R.string.single_fiveg_disable_fiveg_dialog_msg_disable_fiveg).setPositiveButton(R.string.single_fiveg_disable_fiveg_dialog_btn_getit, new DialogInterface.OnClickListener(this) { // from class: com.android.phone.SingleFiveGController.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                SingleFiveGController.log("disable5GDialog click ok");
                MultiSimInfoEditorActivity multiSimInfoEditorActivity = currentInstance;
                if (multiSimInfoEditorActivity != null) {
                    multiSimInfoEditorActivity.finish();
                }
            }
        }).setCancelable(false).create().show();
        return true;
    }

    public boolean showSingle5GPromptDialog() {
        AlertDialog alertDialog;
        if (!FiveGUtils.isSupportSingleFiveG() || !FiveGUtils.isAllSimActivated() || !sIsDeviceProvisioned || ((alertDialog = this.mDualSimDialog) != null && alertDialog.isShowing())) {
            return false;
        }
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.SingleFiveGController.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    SingleFiveGController.log("dualSimDialog, click ok");
                    SingleFiveGController.this.enableAllSimCards();
                } else {
                    SingleFiveGController.log("dualSimDialog, click cancel");
                    dialogInterface.dismiss();
                    SingleFiveGController.this.showSimPickDialog();
                }
            }
        };
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this.mContext, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setTitle(R.string.single_fiveg_dual_sim_dialog_title_important_prompt).setMessage(R.string.single_fiveg_dual_sim_dialog_msg_dual_sim).setPositiveButton(R.string.single_fiveg_dual_sim_dialog_btn_enable_dual_sims, onClickListener).setNegativeButton(R.string.single_fiveg_dual_sim_dialog_btn_deactivate_sim, onClickListener).setCancelable(false).create();
        this.mDualSimDialog = alertDialogCreate;
        alertDialogCreate.getWindow().setType(2003);
        this.mDualSimDialog.show();
        return true;
    }

    private void registerDeviceStateChanged(Context context) {
        registerDataSlotCarrierConfigChanged(context);
        registerDeviceProvisionBroadcast(context);
    }

    private void registerDataSlotCarrierConfigChanged(Context context) {
        FiveGManagerBase.getInstance().registerDataSlotCarrierConfigChangedListener(this);
    }

    private void registerDeviceProvisionBroadcast(Context context) {
        sIsDeviceProvisioned = Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) != 0;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provision.action.PROVISION_COMPLETE");
        context.registerReceiver(new BroadcastReceiver() { // from class: com.android.phone.SingleFiveGController.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.provision.action.PROVISION_COMPLETE".equals(intent.getAction())) {
                    SingleFiveGController.sIsDeviceProvisioned = true;
                    SingleFiveGController.this.mMainHandler.sendEmptyMessageDelayed(1, 2000L);
                }
            }
        }, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSimPickDialog() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent(phoneGlobals, (Class<?>) SingleFiveGDialogActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("dialog_type", 1);
        phoneGlobals.startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
