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
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class LockGSMAntService {
    private static final int NR_RAT = 100;
    private static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    private static final String PROPERTY_USB_LOCK_ANT = "persist.vendor.radio.usblockant";
    private static final String TAG = "LockGSMAntService";
    private static final int TXFORCECONFIG_SIZE = 3;
    private static volatile LockGSMAntService mInstance;
    private static CommandsInterface[] sCi;
    private static LockGSMAntService sIntance;
    private Context mContext;
    private boolean mUsbFREnable;
    private static List mTxForceConfig = new ArrayList();
    private static List mTxForceCommand = new ArrayList();
    protected static OemHookAgent mHookAgent = null;
    private static final Object mLock = new Object();
    private String mDeviceName = null;
    private boolean USBInsertFlag = true;
    private boolean USBUnInsertFlag = true;
    MEGestureModeConfig configGsmTxForce = null;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.LockGSMAntService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("plugged", -1);
            if ((intExtra == 2 || intExtra == 1) && LockGSMAntService.this.USBInsertFlag) {
                Log.d(LockGSMAntService.TAG, "Receive USB charge plugType =  " + intExtra);
                LockGSMAntService.this.sendUsbConnectCmd(true, LockGSMAntService.mTxForceCommand);
                LockGSMAntService.this.USBInsertFlag = false;
                LockGSMAntService.this.USBUnInsertFlag = true;
                return;
            }
            if (intExtra == 2 || intExtra == 1 || !LockGSMAntService.this.USBUnInsertFlag) {
                return;
            }
            Log.d(LockGSMAntService.TAG, "Receive broadcast USB NOt charge plugType =  " + intExtra);
            LockGSMAntService.this.sendUsbConnectCmd(false, LockGSMAntService.mTxForceCommand);
            LockGSMAntService.this.USBUnInsertFlag = false;
            LockGSMAntService.this.USBInsertFlag = true;
        }
    };

    public static LockGSMAntService getInstance(Context context, OemHookAgent oemHookAgent) {
        LockGSMAntService lockGSMAntService;
        if (context == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Context must not be null");
            return null;
        }
        if (oemHookAgent == null) {
            Folme$$ExternalSyntheticBUOutline0.m("OemHookAgent must not be null");
            return null;
        }
        mHookAgent = oemHookAgent;
        LockGSMAntService lockGSMAntService2 = mInstance;
        if (lockGSMAntService2 != null) {
            return lockGSMAntService2;
        }
        synchronized (mLock) {
            try {
                lockGSMAntService = mInstance;
                if (lockGSMAntService == null) {
                    lockGSMAntService = new LockGSMAntService(context);
                    mInstance = lockGSMAntService;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return lockGSMAntService;
    }

    private boolean initGsmTxForce() {
        MEGestureModeConfig mEGestureModeConfig = new MEGestureModeConfig();
        this.configGsmTxForce = mEGestureModeConfig;
        if (MEGestureModeConfig.initialize("/odm/etc/me_config.xml", mEGestureModeConfig)) {
            mTxForceConfig = this.configGsmTxForce.getGsmTxForceSettings();
            return true;
        }
        Log.d(TAG, "initGsmTxForce failed");
        return false;
    }

    private LockGSMAntService(Context context) {
        this.mUsbFREnable = false;
        Log.d(TAG, "LockGSMAntService init...");
        boolean zInitGsmTxForce = initGsmTxForce();
        this.mContext = context;
        this.mUsbFREnable = SystemProperties.getBoolean(PROPERTY_USB_LOCK_ANT, true);
        Log.d(TAG, " mUsbFREnable = " + this.mUsbFREnable);
        if (this.mUsbFREnable && zInitGsmTxForce) {
            mTxForceCommand = createAtCommand();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
            this.mContext.registerReceiver(this.mReceiver, intentFilter);
            Log.d(TAG, "Broadcast init done");
        }
        Log.d(TAG, "LockGSMAntService init done");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUsbConnectCmd(boolean z, List list) {
        if (list == null) {
            Log.d(TAG, "atCommand is empty");
            return;
        }
        if (((List) list.get(0)).size() != ((List) list.get(1)).size()) {
            Log.d(TAG, "Force Tx command is not match with Disable command");
            return;
        }
        if (z) {
            for (int i = 0; i < ((List) list.get(0)).size(); i++) {
                sendATCommand((String) ((List) list.get(0)).get(i));
                Log.d(TAG, "send Force TX AT Command = " + ((String) ((List) list.get(0)).get(i)));
            }
            return;
        }
        for (int i2 = 0; i2 < ((List) list.get(1)).size(); i2++) {
            sendATCommand((String) ((List) list.get(1)).get(i2));
            Log.d(TAG, "send Disable TX AT Command = " + ((String) ((List) list.get(1)).get(i2)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSendAtCommandResult(String str) {
        if (str != null) {
            Log.d(TAG, "try again sendat success and result = ".concat(str));
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

    private void sendATCommand(final String str) {
        Log.d(TAG, "sendATCommand0 = " + str);
        new Thread() { // from class: com.xiaomi.internal.telephony.LockGSMAntService.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    OemHookAgent oemHookAgent = LockGSMAntService.mHookAgent;
                    if (oemHookAgent == null) {
                        Log.d(LockGSMAntService.TAG, "mHookAgent is null");
                        return;
                    }
                    String strOnSendAtCommandByMtkTelephonyManagerEx = oemHookAgent.onSendAtCommandByMtkTelephonyManagerEx(str);
                    if (strOnSendAtCommandByMtkTelephonyManagerEx == null) {
                        Log.d(LockGSMAntService.TAG, "result is null try again sendat");
                        LockGSMAntService.this.handleSendAtCommandResult(LockGSMAntService.mHookAgent.onSendAtCommandByMtkTelephonyManagerEx(str));
                    } else {
                        Log.d(LockGSMAntService.TAG, "result = ".concat(strOnSendAtCommandByMtkTelephonyManagerEx));
                        LockGSMAntService.this.handleSleepWithInterrupt();
                    }
                } catch (InterruptedException e) {
                    Log.e(LockGSMAntService.TAG, "Thread interrupted", e);
                    Thread.currentThread().interrupt();
                } catch (Exception e2) {
                    Log.e(LockGSMAntService.TAG, "Error", e2);
                }
            }
        }.start();
    }

    private List createAtCommand() {
        String str;
        String str2;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        List list = mTxForceConfig;
        if (list == null || list.isEmpty()) {
            Log.d(TAG, "mTxForceConfig is empty");
            return arrayList;
        }
        for (int i = 0; i < mTxForceConfig.size(); i++) {
            if (3 == ((List) mTxForceConfig.get(i)).size()) {
                int iIntValue = ((Integer) ((List) mTxForceConfig.get(i)).get(0)).intValue();
                int iIntValue2 = ((Integer) ((List) mTxForceConfig.get(i)).get(1)).intValue();
                int iIntValue3 = ((Integer) ((List) mTxForceConfig.get(i)).get(2)).intValue();
                if (iIntValue >= 0 && iIntValue2 >= 0 && iIntValue3 >= 0) {
                    Log.d(TAG, "rat = " + iIntValue + ", band = " + iIntValue2 + ", tas_state = " + iIntValue3);
                    if (100 == iIntValue) {
                        str2 = "AT+EGMC=1,\"NrForceTxRx\",4," + Integer.toString(iIntValue3) + "," + Integer.toString(iIntValue3) + "," + Integer.toString(iIntValue2);
                        str = "AT+EGMC=1,\"NrForceTxRx\",3,,," + Integer.toString(iIntValue2);
                    } else {
                        String str3 = "AT+ETXANT=4," + Integer.toString(iIntValue) + "," + Integer.toString(iIntValue3) + "," + Integer.toString(iIntValue2);
                        str = "AT+ETXANT=3," + Integer.toString(iIntValue) + ",," + Integer.toString(iIntValue2);
                        str2 = str3;
                    }
                    arrayList2.add(str2);
                    arrayList3.add(str);
                    Log.d(TAG, "txForceCommand = ".concat(str2));
                    Log.d(TAG, "txForceDisableCommand = ".concat(str));
                }
            }
        }
        arrayList.add(arrayList2);
        arrayList.add(arrayList3);
        return arrayList;
    }
}
