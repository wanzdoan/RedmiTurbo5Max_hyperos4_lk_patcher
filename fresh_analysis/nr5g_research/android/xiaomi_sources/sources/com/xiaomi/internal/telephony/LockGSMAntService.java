package com.xiaomi.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.xiaomi.modem.OemHookAgent;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class LockGSMAntService {
    private static final int NR_RAT = 100;
    private static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    private static final String PROPERTY_USB_LOCK_ANT = "persist.vendor.radio.usblockant";
    private static final String TAG = "LockGSMAntService";
    private static final int TXFORCECONFIG_SIZE = 3;
    private static volatile LockGSMAntService mInstance;
    private static LockGSMAntService sIntance;
    private Context mContext;
    private boolean mUsbFREnable;
    private static CommandsInterface[] sCi = null;
    private static List<List<Integer>> mTxForceConfig = new ArrayList();
    private static List<List<String>> mTxForceCommand = new ArrayList();
    protected static OemHookAgent mHookAgent = null;
    private static final Object mLock = new Object();
    private String mDeviceName = null;
    private boolean USBInsertFlag = true;
    private boolean USBUnInsertFlag = true;
    MEGestureModeConfig configGsmTxForce = null;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.LockGSMAntService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int plugType = intent.getIntExtra("plugged", -1);
            if ((plugType == 2 || plugType == 1) && LockGSMAntService.this.USBInsertFlag) {
                Log.d(LockGSMAntService.TAG, "Receive USB charge plugType =  " + plugType);
                LockGSMAntService.this.sendUsbConnectCmd(true, LockGSMAntService.mTxForceCommand);
                LockGSMAntService.this.USBInsertFlag = false;
                LockGSMAntService.this.USBUnInsertFlag = true;
                return;
            }
            if (plugType != 2 && plugType != 1 && LockGSMAntService.this.USBUnInsertFlag) {
                Log.d(LockGSMAntService.TAG, "Receive broadcast USB NOt charge plugType =  " + plugType);
                LockGSMAntService.this.sendUsbConnectCmd(false, LockGSMAntService.mTxForceCommand);
                LockGSMAntService.this.USBUnInsertFlag = false;
                LockGSMAntService.this.USBInsertFlag = true;
            }
        }
    };

    public static LockGSMAntService getInstance(Context context, OemHookAgent hookAgent) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        if (hookAgent == null) {
            throw new IllegalArgumentException("OemHookAgent must not be null");
        }
        mHookAgent = hookAgent;
        LockGSMAntService instance = mInstance;
        if (instance == null) {
            synchronized (mLock) {
                instance = mInstance;
                if (instance == null) {
                    instance = new LockGSMAntService(context);
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    private boolean initGsmTxForce() {
        this.configGsmTxForce = new MEGestureModeConfig();
        if (MEGestureModeConfig.initialize("/odm/etc/me_config.xml", this.configGsmTxForce)) {
            mTxForceConfig = this.configGsmTxForce.getGsmTxForceSettings();
            return true;
        }
        Log.d(TAG, "initGsmTxForce failed");
        return false;
    }

    private LockGSMAntService(Context context) {
        this.mUsbFREnable = false;
        Log.d(TAG, "LockGSMAntService init...");
        boolean initGsmTxForceflag = initGsmTxForce();
        this.mContext = context;
        this.mUsbFREnable = SystemProperties.getBoolean(PROPERTY_USB_LOCK_ANT, true);
        Log.d(TAG, " mUsbFREnable = " + this.mUsbFREnable);
        if (this.mUsbFREnable && initGsmTxForceflag) {
            mTxForceCommand = createAtCommand();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.BATTERY_CHANGED");
            this.mContext.registerReceiver(this.mReceiver, filter);
            Log.d(TAG, "Broadcast init done");
        }
        Log.d(TAG, "LockGSMAntService init done");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUsbConnectCmd(boolean mUsbState, List<List<String>> atCommand) {
        if (atCommand == null) {
            Log.d(TAG, "atCommand is empty");
            return;
        }
        if (atCommand.get(0).size() != atCommand.get(1).size()) {
            Log.d(TAG, "Force Tx command is not match with Disable command");
            return;
        }
        if (mUsbState) {
            for (int i = 0; i < atCommand.get(0).size(); i++) {
                sendATCommand(atCommand.get(0).get(i));
                Log.d(TAG, "send Force TX AT Command = " + atCommand.get(0).get(i));
            }
            return;
        }
        for (int i2 = 0; i2 < atCommand.get(1).size(); i2++) {
            sendATCommand(atCommand.get(1).get(i2));
            Log.d(TAG, "send Disable TX AT Command = " + atCommand.get(1).get(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSendAtCommandResult(String result) {
        if (result != null) {
            Log.d(TAG, "try again sendat success and result = " + result);
        } else {
            Log.d(TAG, "try again sendat failed and result is null");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSleepWithInterrupt() throws InterruptedException {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            Log.e(TAG, "Sleep interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    private void sendATCommand(final String atCommand) {
        Log.d(TAG, "sendATCommand0 = " + atCommand);
        Thread mCmdThread = new Thread() { // from class: com.xiaomi.internal.telephony.LockGSMAntService.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    if (LockGSMAntService.mHookAgent == null) {
                        Log.d(LockGSMAntService.TAG, "mHookAgent is null");
                        return;
                    }
                    String result = LockGSMAntService.mHookAgent.onSendAtCommandByMtkTelephonyManagerEx(atCommand);
                    if (result == null) {
                        Log.d(LockGSMAntService.TAG, "result is null try again sendat");
                        LockGSMAntService.this.handleSendAtCommandResult(LockGSMAntService.mHookAgent.onSendAtCommandByMtkTelephonyManagerEx(atCommand));
                    } else {
                        Log.d(LockGSMAntService.TAG, "result = " + result);
                        LockGSMAntService.this.handleSleepWithInterrupt();
                    }
                } catch (InterruptedException e) {
                    Log.e(LockGSMAntService.TAG, "Thread interrupted", e);
                    Thread.currentThread().interrupt();
                } catch (Exception e2) {
                    Log.e(LockGSMAntService.TAG, "Error", e2);
                }
            }
        };
        mCmdThread.start();
    }

    private List<List<String>> createAtCommand() {
        String txForceCommand;
        String txForceDisableCommand;
        List<List<String>> atCommand = new ArrayList<>();
        List<String> txForce = new ArrayList<>();
        List<String> txForceDisable = new ArrayList<>();
        if (mTxForceConfig == null || mTxForceConfig.isEmpty()) {
            Log.d(TAG, "mTxForceConfig is empty");
            return atCommand;
        }
        for (int i = 0; i < mTxForceConfig.size(); i++) {
            if (3 == mTxForceConfig.get(i).size()) {
                int rat = mTxForceConfig.get(i).get(0).intValue();
                int band = mTxForceConfig.get(i).get(1).intValue();
                int tas_state = mTxForceConfig.get(i).get(2).intValue();
                if (rat >= 0 && band >= 0 && tas_state >= 0) {
                    Log.d(TAG, "rat = " + rat + ", band = " + band + ", tas_state = " + tas_state);
                    if (100 == rat) {
                        txForceCommand = "AT+EGMC=1,\"NrForceTxRx\",4," + Integer.toString(tas_state) + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + Integer.toString(tas_state) + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + Integer.toString(band);
                        txForceDisableCommand = "AT+EGMC=1,\"NrForceTxRx\",3,,," + Integer.toString(band);
                    } else {
                        txForceCommand = "AT+ETXANT=4," + Integer.toString(rat) + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + Integer.toString(tas_state) + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + Integer.toString(band);
                        txForceDisableCommand = "AT+ETXANT=3," + Integer.toString(rat) + ",," + Integer.toString(band);
                    }
                    txForce.add(txForceCommand);
                    txForceDisable.add(txForceDisableCommand);
                    Log.d(TAG, "txForceCommand = " + txForceCommand);
                    Log.d(TAG, "txForceDisableCommand = " + txForceDisableCommand);
                }
            }
        }
        atCommand.add(txForce);
        atCommand.add(txForceDisable);
        return atCommand;
    }
}
