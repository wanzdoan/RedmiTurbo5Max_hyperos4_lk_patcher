package com.nxp.osu;

import android.content.Context;
import android.util.Log;
import com.nxp.osu.channel.OsuOmapiApduChannel;
import com.nxp.osu.modem.ModemImp;
import com.nxp.osu.osucore.IAROperation;
import com.nxp.osu.osucore.OsuCore;
import com.nxp.osu.util.OSUNfcHandler;

/* JADX INFO: loaded from: classes.dex */
public final class SecOsuAgent extends OsuCore {
    private static boolean sIsBusy = false;
    private static SecOsuAgent sSecOsuInstance = new SecOsuAgent();

    private static synchronized void setBusy(boolean z) {
        sIsBusy = z;
    }

    public static SecOsuAgent getInstance(Context context, ISecOsuInterface iSecOsuInterface) throws SecOsuException {
        IModem iModem;
        ISecOsuAccess iSecOsuAccess;
        Log.i("OSU-SecOsuAgent", "OSU JAR Version " + OsuCore.SEC_OSU_VERSION);
        if (context == null) {
            throw new SecOsuException("Application Context can't be null");
        }
        if (!OSUNfcHandler.checkRequiredPermission(context)) {
            Log.e("OSU-SecOsuAgent", "Permission Denied");
            throw new SecOsuException("Permission Denied to Access OSU-Jar");
        }
        SecOsuAgent secOsuAgent = sSecOsuInstance;
        if (secOsuAgent != null && secOsuAgent.mChannel != null) {
            Log.w("OSU-SecOsuAgent", "Access and Terminal Already defined cann't redefine Access");
            return sSecOsuInstance;
        }
        if (secOsuAgent != null) {
            if (iSecOsuInterface != null && (iSecOsuAccess = iSecOsuInterface.mAccess) != null) {
                secOsuAgent.mChannel = iSecOsuAccess;
            } else {
                ISecOsuInterface.TERMINAL_TYPE terminal_type = ISecOsuInterface.TERMINAL_TYPE.SPI;
                int value = terminal_type.getValue();
                boolean z = false;
                if (iSecOsuInterface != null) {
                    ISecOsuInterface.TERMINAL_TYPE terminal_type2 = iSecOsuInterface.mTerminal;
                    z = terminal_type2 != terminal_type;
                    value = terminal_type2.getValue();
                }
                sSecOsuInstance.mChannel = OsuOmapiApduChannel.getInstance(context, z, value);
                sSecOsuInstance.mIsWrapRequired = true;
            }
            if (iSecOsuInterface != null && (iModem = iSecOsuInterface.mModem) != null) {
                sSecOsuInstance.mModem = iModem;
            } else {
                sSecOsuInstance.mModem = new ModemImp();
            }
            sSecOsuInstance.mContext = context;
        }
        return sSecOsuInstance;
    }

    private SecOsuAgent() {
    }

    public synchronized int SecOsuExecute(String str, ISecOsuCallback iSecOsuCallback) {
        return SecOsuExecute(str, iSecOsuCallback, null);
    }

    public synchronized int SecOsuExecute(String str, ISecOsuCallback iSecOsuCallback, IOsuAuthCallback iOsuAuthCallback) {
        try {
            boolean z = OsuCore.IS_DEBUG;
            if (z) {
                Log.d("OSU-SecOsuAgent", "SecOsuExecute Entry Version " + OsuCore.SEC_OSU_VERSION);
            }
            if (sIsBusy) {
                Log.e("OSU-SecOsuAgent", "OSU is Busy");
                return SecOsuStatusCode.OSU_STATUS_BUSY.getValue();
            }
            if (iSecOsuCallback == null) {
                Log.e("OSU-SecOsuAgent", "OSU callback NULL not allowed");
                return SecOsuStatusCode.OSU_STATUS_FAILED.getValue();
            }
            setBusy(true);
            if (!OSUNfcHandler.isNFCInitCompleted(this.mContext)) {
                Log.e("OSU-SecOsuAgent", "NFC INIT is in progress");
                setBusy(false);
                return SecOsuStatusCode.OSU_STATUS_BUSY.getValue();
            }
            this.mCallback = iSecOsuCallback;
            SecOsuStatusCode secOsuStatusCodeVerifyAndReadDERFile = verifyAndReadDERFile(str);
            if (secOsuStatusCodeVerifyAndReadDERFile == SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                IAROperation.getInstance().setOsuAuthCallback(iOsuAuthCallback);
                new ExecuteOsu().start();
            } else {
                Log.e("OSU-SecOsuAgent", "Reading DER FAILED");
                setBusy(false);
            }
            if (z) {
                Log.d("OSU-SecOsuAgent", "SecOsuExecute Exit");
            }
            return secOsuStatusCodeVerifyAndReadDERFile.getValue();
        } catch (Throwable th) {
            throw th;
        }
    }

    class ExecuteOsu extends Thread {
        ExecuteOsu() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            SecOsuAgent.this.execute();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void execute() {
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_FAILED;
        try {
            try {
                if (!prepareOsuSetup()) {
                    Log.e("OSU-SecOsuAgent", "prepare OSU Failed");
                    SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
                    if (secOsuStatusCode != secOsuStatusCode2) {
                        finishOsuSteps();
                    }
                    synchronized (this) {
                        try {
                            ISecOsuCallback iSecOsuCallback = this.mCallback;
                            if (iSecOsuCallback != null) {
                                iSecOsuCallback.onOsuComplete(secOsuStatusCode.getValue(), this.mLoggerFile);
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                    setBusy(false);
                    if (secOsuStatusCode == secOsuStatusCode2) {
                        if (OsuCore.IS_DEBUG) {
                            Log.d("OSU-SecOsuAgent", "ShutDown Complete");
                        }
                        synchronized (this.mShutDownMutex) {
                            this.mShutDownComplete = true;
                            this.mShutDownMutex.notify();
                        }
                        return;
                    }
                    return;
                }
                SecOsuStatusCode secOsuStatusCodePerformOsuSteps = performOsuSteps();
                if (secOsuStatusCodePerformOsuSteps != SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                    Log.e("OSU-SecOsuAgent", "OS Update Failed");
                } else if (OsuCore.IS_DEBUG) {
                    Log.d("OSU-SecOsuAgent", "OS Update SuccessFully Completed");
                }
                SecOsuStatusCode secOsuStatusCode3 = SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
                if (secOsuStatusCodePerformOsuSteps != secOsuStatusCode3) {
                    finishOsuSteps();
                }
                synchronized (this) {
                    try {
                        ISecOsuCallback iSecOsuCallback2 = this.mCallback;
                        if (iSecOsuCallback2 != null) {
                            iSecOsuCallback2.onOsuComplete(secOsuStatusCodePerformOsuSteps.getValue(), this.mLoggerFile);
                        }
                    } catch (Throwable th2) {
                        throw th2;
                    }
                }
                setBusy(false);
                if (secOsuStatusCodePerformOsuSteps == secOsuStatusCode3) {
                    if (OsuCore.IS_DEBUG) {
                        Log.d("OSU-SecOsuAgent", "ShutDown Complete");
                    }
                    synchronized (this.mShutDownMutex) {
                        this.mShutDownComplete = true;
                        this.mShutDownMutex.notify();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                SecOsuStatusCode secOsuStatusCode4 = SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
                if (secOsuStatusCode != secOsuStatusCode4) {
                    finishOsuSteps();
                }
                synchronized (this) {
                    try {
                        ISecOsuCallback iSecOsuCallback3 = this.mCallback;
                        if (iSecOsuCallback3 != null) {
                            iSecOsuCallback3.onOsuComplete(secOsuStatusCode.getValue(), this.mLoggerFile);
                        }
                        setBusy(false);
                        if (secOsuStatusCode == secOsuStatusCode4) {
                            if (OsuCore.IS_DEBUG) {
                                Log.d("OSU-SecOsuAgent", "ShutDown Complete");
                            }
                            synchronized (this.mShutDownMutex) {
                                this.mShutDownComplete = true;
                                this.mShutDownMutex.notify();
                            }
                        }
                    } catch (Throwable th3) {
                        throw th3;
                    }
                }
            }
        } catch (Throwable th4) {
            SecOsuStatusCode secOsuStatusCode5 = SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
            if (secOsuStatusCode != secOsuStatusCode5) {
                finishOsuSteps();
            }
            synchronized (this) {
                try {
                    ISecOsuCallback iSecOsuCallback4 = this.mCallback;
                    if (iSecOsuCallback4 != null) {
                        iSecOsuCallback4.onOsuComplete(secOsuStatusCode.getValue(), this.mLoggerFile);
                    }
                    setBusy(false);
                    if (secOsuStatusCode == secOsuStatusCode5) {
                        if (OsuCore.IS_DEBUG) {
                            Log.d("OSU-SecOsuAgent", "ShutDown Complete");
                        }
                        synchronized (this.mShutDownMutex) {
                            this.mShutDownComplete = true;
                            this.mShutDownMutex.notify();
                        }
                    }
                    throw th4;
                } catch (Throwable th5) {
                    throw th5;
                }
            }
        }
    }

    public synchronized boolean secOsuSetConfig(SecOsuConfigType secOsuConfigType, SecOsuConfigValue secOsuConfigValue) {
        if (sIsBusy) {
            Log.e("OSU-SecOsuAgent", "Invalid State : Can not set configuration during osu is ongoing");
            return false;
        }
        return setOsuConfig(secOsuConfigType, secOsuConfigValue);
    }
}
