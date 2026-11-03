package com.android.phone;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.utils.CountableHandler;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.LinkedHashMap;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class ModemSmartPowerSavingController {
    private static boolean[] isOptL1Enabled = {false, false};
    private static boolean isOptL2Enabled = false;
    private static boolean mCurNormalPowerSaveMode = false;
    private static boolean mCurSuperPowerSaveMode = false;
    private static ModemSmartPowerSavingController mInstance = null;
    private static boolean mIsBatteryLevel1 = false;
    private static boolean mIsBatteryLevel2 = false;
    private BatteryStateManager mBatteryStateManager;
    private int mCurBatterylevel;
    private final Handler mHandler;
    private TelephonyCallBack mTelephonyCallBack;
    private TelephonyManager mTelephonyManager;
    private final int[] mPhoneRegState = {3, 3};
    private int[] mCurrentNrRsrp = {Integer.MAX_VALUE, Integer.MAX_VALUE};
    private int[] mCurrentLteRsrp = {Integer.MAX_VALUE, Integer.MAX_VALUE};
    private int[] mCurRat = {0, 0};
    private Context mContext = PhoneGlobals.getInstance();

    public static synchronized void init() {
        try {
            if (mInstance == null) {
                mInstance = new ModemSmartPowerSavingController();
                logd("ModemSmartPowerSavingController init...");
            } else {
                logd("ModemSmartPowerSavingController is already init");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private ModemSmartPowerSavingController() {
        HandlerThread handlerThread = new HandlerThread("ModemSmartPowerSavingController", 10);
        handlerThread.start();
        this.mHandler = new WorkHandler(handlerThread.getLooper());
        TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
        this.mTelephonyCallBack = telephonyCallBack;
        TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        this.mBatteryStateManager = BatteryStateManager.getInstance();
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        setupBatteryStateListener();
    }

    private void setupBatteryStateListener() {
        BatteryStateManager.BatteryStateListener batteryStateListener = new BatteryStateManager.BatteryStateListener() { // from class: com.android.phone.ModemSmartPowerSavingController.1
            @Override // com.android.phone.BatteryStateManager.BatteryStateListener
            public void notifyBatteryStateChanged(int i) {
                if (i > 10) {
                    return;
                }
                ModemSmartPowerSavingController.this.mCurBatterylevel = i;
                ModemSmartPowerSavingController.logd("batteryLevel = " + i);
                if (i > 3 && i <= 6 && !ModemSmartPowerSavingController.mIsBatteryLevel1) {
                    ModemSmartPowerSavingController.mIsBatteryLevel1 = true;
                    ModemSmartPowerSavingController.logd("enter battery Level 1 range (3,6]");
                    return;
                }
                if (i <= 3 && !ModemSmartPowerSavingController.mIsBatteryLevel2) {
                    ModemSmartPowerSavingController.mIsBatteryLevel2 = true;
                    ModemSmartPowerSavingController.logd("enter battery Level 2 range (0,3]");
                    ModemSmartPowerSavingController.this.mHandler.obtainMessage(3).sendToTarget();
                } else if (i > 3 && ModemSmartPowerSavingController.mIsBatteryLevel2) {
                    ModemSmartPowerSavingController.mIsBatteryLevel2 = false;
                    ModemSmartPowerSavingController.logd("exit battery Level 2");
                    ModemSmartPowerSavingController.this.mHandler.obtainMessage(5).sendToTarget();
                } else {
                    if (i <= 6 || !ModemSmartPowerSavingController.mIsBatteryLevel1) {
                        return;
                    }
                    ModemSmartPowerSavingController.mIsBatteryLevel1 = false;
                    ModemSmartPowerSavingController.logd("exit battery Level 1");
                    ModemSmartPowerSavingController.this.mHandler.obtainMessage(4).sendToTarget();
                }
            }
        };
        BatteryStateManager batteryStateManager = this.mBatteryStateManager;
        if (batteryStateManager != null) {
            batteryStateManager.registerBatteryStateListener(batteryStateListener);
        } else {
            loge("mBatteryStateManager is null");
        }
    }

    private void updatePowerSaveMode() {
        boolean zIsSuperSavePowerMode = MiuiPhoneUtils.isSuperSavePowerMode(this.mContext);
        if (zIsSuperSavePowerMode != mCurSuperPowerSaveMode) {
            mCurSuperPowerSaveMode = zIsSuperSavePowerMode;
            logd(zIsSuperSavePowerMode ? "enter SuperSavePowerMode" : "exit SuperSavePowerMode");
        }
        boolean z = Settings.System.getInt(this.mContext.getContentResolver(), "POWER_SAVE_MODE_OPEN", 0) == 1;
        if (z != mCurNormalPowerSaveMode) {
            mCurNormalPowerSaveMode = z;
            logd(z ? "enter normal power save mode" : "exit normal power save mode");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applySmartEcoModeL1(int i) {
        updatePowerSaveMode();
        if (mCurNormalPowerSaveMode || mCurSuperPowerSaveMode || isOptL1Enabled[i]) {
            return;
        }
        logd("apply strategy L1 for phone " + i);
        notifySmartPowerSavingState(true, i);
        isOptL1Enabled[i] = true;
        recordUpload("L1", i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applySmartEcoModeL2() {
        updatePowerSaveMode();
        if (mCurSuperPowerSaveMode || isOptL2Enabled) {
            return;
        }
        logd("apply strategy L2");
        ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).switchRxMode(true);
        isOptL2Enabled = true;
        recordUpload("L2", -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitSmartEcoModeL1() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (MiuiPhoneUtils.isValidatePhoneId(i) && isOptL1Enabled[i]) {
                logd("exit strategy L1");
                notifySmartPowerSavingState(false, i);
                isOptL1Enabled[i] = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitSmartEcoModeL2() {
        if (isOptL2Enabled) {
            logd("exit strategy L2");
            ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).switchRxMode(false);
            isOptL2Enabled = false;
        }
    }

    class WorkHandler extends CountableHandler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.phone.utils.CountableHandler, android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            ModemSmartPowerSavingController.logd("msg.what = " + message.what);
            int i = message.what;
            if (i == 1) {
                ModemSmartPowerSavingController.this.processSignalStrengthEvent(message.arg1, (SignalStrength) message.obj);
                return;
            }
            if (i == 2) {
                ModemSmartPowerSavingController.this.applySmartEcoModeL1(message.arg1);
                return;
            }
            if (i == 3) {
                ModemSmartPowerSavingController.this.applySmartEcoModeL2();
                return;
            }
            if (i == 4) {
                ModemSmartPowerSavingController.this.exitSmartEcoModeL1();
            } else if (i == 5) {
                ModemSmartPowerSavingController.this.exitSmartEcoModeL2();
            } else {
                ModemSmartPowerSavingController.loge("undefined msg");
            }
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            ModemSmartPowerSavingController.this.mHandler.obtainMessage(1, i, 0, signalStrength).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSignalStrengthEvent(int i, SignalStrength signalStrength) {
        int i2;
        int i3;
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || signalStrength == null) {
            return;
        }
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                this.mCurrentLteRsrp[i] = ((CellSignalStrengthLte) cellSignalStrength).getRsrp();
                logd("mCurrentLteRsrp(phone" + i + ") = " + this.mCurrentLteRsrp[i]);
                if ((mIsBatteryLevel1 || mIsBatteryLevel2) && (i2 = this.mCurrentLteRsrp[i]) > -105 && i2 != Integer.MAX_VALUE && (i3 = this.mCurrentNrRsrp[i]) < -110 && i3 != Integer.MAX_VALUE) {
                    logd("phone" + i + " enter Weak SA Field, Strong LTE Field");
                    this.mHandler.obtainMessage(2, Integer.valueOf(i)).sendToTarget();
                }
                this.mCurrentNrRsrp[i] = Integer.MAX_VALUE;
            } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                this.mCurrentNrRsrp[i] = ((CellSignalStrengthNr) cellSignalStrength).getSsRsrp();
                logd("mCurrentNrRsrp(phone" + i + ") = " + this.mCurrentNrRsrp[i]);
            }
        }
    }

    private void recordUpload(String str, int i) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("TieredPowerSavingScene", str);
        linkedHashMap.put("BatteryLevel", Integer.valueOf(this.mCurBatterylevel));
        if ("L1".equals(str)) {
            linkedHashMap.put("PhoneId", Integer.valueOf(i));
        }
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_smart_tiered_power_saving_statistic", linkedHashMap);
    }

    private void notifySmartPowerSavingState(boolean z, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        bundle.putBoolean("power_save_mode", z);
        EventHubManager.getInstance().postEvent(new TelephonyEvent.PowerEvent("smart_power_saving_state_changed", bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("ModemSmartPowerSavingController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("ModemSmartPowerSavingController", str);
    }
}
