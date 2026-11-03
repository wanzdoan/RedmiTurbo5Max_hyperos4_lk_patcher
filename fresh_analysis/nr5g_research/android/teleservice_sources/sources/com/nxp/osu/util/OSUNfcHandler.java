package com.nxp.osu.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.SystemProperties;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes.dex */
public abstract class OSUNfcHandler {
    private static boolean sFlagNfcDisableMutex = false;
    private static Timer sNFCTimer = null;
    private static int sNfcState = 1;
    private static Object sNFCDisableMutex = new Object();
    private static final boolean IS_DEBUG = OSULogger.isDebug();
    private static final BroadcastReceiver sReceiver = new BroadcastReceiver() { // from class: com.nxp.osu.util.OSUNfcHandler.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.nfc.action.ADAPTER_STATE_CHANGED".equals(intent.getAction())) {
                OSUNfcHandler.sNfcState = intent.getIntExtra("android.nfc.extra.ADAPTER_STATE", 1);
                if (OSUNfcHandler.IS_DEBUG) {
                    Log.d("OSU-NfcHandler", "Recived NFC State Change as " + OSUNfcHandler.sNfcState);
                }
                if (OSUNfcHandler.sNfcState == 1) {
                    OSUNfcHandler.sFlagNfcDisableMutex = true;
                    synchronized (OSUNfcHandler.sNFCDisableMutex) {
                        OSUNfcHandler.sNFCDisableMutex.notifyAll();
                    }
                }
            }
        }
    };

    class NFCDisableTimerTask extends TimerTask {
        NFCDisableTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            synchronized (OSUNfcHandler.sNFCDisableMutex) {
                OSUNfcHandler.sFlagNfcDisableMutex = true;
                OSUNfcHandler.sNFCDisableMutex.notifyAll();
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0037 A[Catch: all -> 0x0013, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0009, B:8:0x000d, B:14:0x0031, B:13:0x0016, B:15:0x0033, B:17:0x0037, B:18:0x003f, B:20:0x0043, B:21:0x0046), top: B:27:0x0003, inners: #0 }] */
    /* JADX WARN: Code duplicated, block: B:20:0x0043 A[Catch: all -> 0x0013, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0009, B:8:0x000d, B:14:0x0031, B:13:0x0016, B:15:0x0033, B:17:0x0037, B:18:0x003f, B:20:0x0043, B:21:0x0046), top: B:27:0x0003, inners: #0 }] */
    private static boolean waitForNFCDisable() {
        boolean z;
        Timer timer;
        synchronized (sNFCDisableMutex) {
            z = true;
            if (sNfcState != 1) {
                while (!sFlagNfcDisableMutex) {
                    try {
                        sNFCDisableMutex.wait();
                    } catch (InterruptedException e) {
                        Log.w("OSU-NfcHandler", "Disabling NFC Failed : " + e.getMessage());
                    }
                }
                sFlagNfcDisableMutex = false;
                if (sNfcState != 1) {
                    Log.w("OSU-NfcHandler", "Not able to disable NFC in 4000 ms");
                    z = false;
                }
                timer = sNFCTimer;
                if (timer != null) {
                    timer.cancel();
                }
            } else {
                if (sNfcState != 1) {
                    Log.w("OSU-NfcHandler", "Not able to disable NFC in 4000 ms");
                    z = false;
                }
                timer = sNFCTimer;
                if (timer != null) {
                    timer.cancel();
                }
            }
            throw th;
        }
        return z;
    }

    public static void doNfcResetIfRequired(Context context) {
        if (OSULogger.isNfcRestartRequired()) {
            boolean z = IS_DEBUG;
            if (z) {
                Log.d("OSU-NfcHandler", "NFC OFF & then ON Enter");
            }
            disableNfc(true, true, context);
            disableNfc(false, true, context);
            if (z) {
                Log.d("OSU-NfcHandler", "NFC OFF & then ON Exit");
            }
        }
    }

    public static boolean disableNfc(boolean z, Context context) {
        return disableNfc(z, false, context);
    }

    public static boolean disableNfc(boolean z, boolean z2, Context context) {
        if (context == null) {
            Log.e("OSU-NfcHandler", "disableNfc:: Context not defined");
            return false;
        }
        boolean zEnable = true;
        if (!OSULogger.isNFCDisableRequired() && !z2) {
            return true;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(OSULogger.getSharedPreferenceKey(), 0);
        boolean z3 = sharedPreferences.getBoolean("isNfcDisabled", false);
        NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(context);
        if (defaultAdapter == null) {
            Log.e("OSU-NfcHandler", "NfcAdapter is null");
            return true;
        }
        boolean zIsEnabled = defaultAdapter.isEnabled();
        if (!zIsEnabled) {
            zIsEnabled = defaultAdapter.isEnabled();
        }
        if (!z || !zIsEnabled) {
            if (!z && z3) {
                if (IS_DEBUG) {
                    Log.d("OSU-NfcHandler", "Enable NFC");
                }
                int i = 0;
                while (!defaultAdapter.isEnabled()) {
                    int i2 = i + 1;
                    if (i >= 3 || (zEnable = defaultAdapter.enable())) {
                        break;
                    }
                    if (IS_DEBUG) {
                        Log.d("OSU-NfcHandler", "attemptDeadServiceRecovery");
                    }
                    try {
                        Thread.sleep(500L);
                    } catch (Exception e) {
                        if (IS_DEBUG) {
                            Log.d("OSU-NfcHandler", "Exception is " + e);
                        }
                    }
                    i = i2;
                }
                if (zEnable) {
                    SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                    editorEdit.putBoolean("isNfcDisabled", false);
                    editorEdit.commit();
                }
            }
            return zEnable;
        }
        synchronized (sNFCDisableMutex) {
            try {
                boolean z4 = IS_DEBUG;
                if (z4) {
                    Log.d("OSU-NfcHandler", "Disable NFC");
                }
                sNfcState = 3;
                IntentFilter intentFilter = new IntentFilter("android.nfc.action.ADAPTER_STATE_CHANGED");
                BroadcastReceiver broadcastReceiver = sReceiver;
                context.registerReceiver(broadcastReceiver, intentFilter);
                if (!defaultAdapter.disable()) {
                    if (z4) {
                        Log.d("OSU-NfcHandler", "Not Able to Disable NFC");
                    }
                    return false;
                }
                sNFCTimer = new Timer();
                sNFCTimer.schedule(new NFCDisableTimerTask(), 4000L);
                boolean zWaitForNFCDisable = waitForNFCDisable();
                if (zWaitForNFCDisable) {
                    if (z4) {
                        Log.d("OSU-NfcHandler", "NFC Disabled");
                    }
                    SharedPreferences.Editor editorEdit2 = sharedPreferences.edit();
                    editorEdit2.putBoolean("isNfcDisabled", true);
                    editorEdit2.commit();
                } else {
                    Log.e("OSU-NfcHandler", "Not able to disable NFC");
                }
                context.unregisterReceiver(broadcastReceiver);
                return zWaitForNFCDisable;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static boolean checkRequiredPermission(Context context) {
        if (context == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        if (checkPermission(packageName, "android.permission.SECURE_ELEMENT_PRIVILEGED_OPERATION", packageManager) && checkPermission(packageName, "android.permission.WRITE_SECURE_SETTINGS", packageManager) && checkPermission(packageName, "android.permission.NFC", packageManager)) {
            return true;
        }
        Log.e("OSU-NfcHandler", "Permission denied");
        return false;
    }

    private static boolean checkPermission(String str, String str2, PackageManager packageManager) {
        if (packageManager != null && packageManager.checkPermission(str2, str) == 0) {
            return true;
        }
        Log.e("OSU-NfcHandler", "Permission denied  " + str2);
        return false;
    }

    public static boolean isNFCSupported(Context context) {
        return (context == null || NfcAdapter.getDefaultAdapter(context) == null) ? false : true;
    }

    public static boolean isNFCInitCompleted(Context context) {
        if (!isNFCSupported(context)) {
            return true;
        }
        for (int i = 0; i < 15; i++) {
            String str = SystemProperties.get("nfc.initialized");
            if (str != null && str.equals("true")) {
                return true;
            }
            try {
                Thread.sleep(1000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
