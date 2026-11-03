package com.nxp.osu.osucore;

import android.content.Context;
import android.util.Log;
import com.nxp.id.tp.common.HexString;
import com.nxp.osu.IModem;
import com.nxp.osu.ISecOsuCallback;
import com.nxp.osu.SecOsuConfigType;
import com.nxp.osu.SecOsuConfigValue;
import com.nxp.osu.SecOsuStatusCode;
import com.nxp.osu.channel.IAccess;
import com.nxp.osu.channel.OsuOmapiApduChannel;
import com.nxp.osu.channel.OsuOmapiHelper;
import com.nxp.osu.util.OSULogger;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes.dex */
public abstract class OsuCore {
    protected static final String SEC_OSU_VERSION = OsuEngine.getVersion();
    protected static OsuEngine sOsuEngine = null;
    protected static final boolean IS_DEBUG = OSULogger.isDebug();
    private static final byte[] SUCCESS_RESP = {-112, 0};
    protected IAccess mChannel = null;
    protected IModem mModem = null;
    protected boolean mIsWrapRequired = false;
    protected Context mContext = null;
    protected ISecOsuCallback mCallback = null;
    protected String mLoggerFile = null;
    protected Object mShutDownMutex = new Object();
    private ShutDownTimerTask mTimerTask = new ShutDownTimerTask();
    protected boolean mShutDownComplete = false;

    class ShutDownTimerTask extends TimerTask {
        ShutDownTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            synchronized (OsuCore.this.mShutDownMutex) {
                OsuCore osuCore = OsuCore.this;
                osuCore.mShutDownComplete = true;
                osuCore.mShutDownMutex.notifyAll();
            }
        }
    }

    protected OsuCore() {
    }

    protected SecOsuStatusCode verifyAndReadDERFile(String str) {
        OsuEngine osuEngine = OsuEngine.getInstance(this.mChannel, this.mContext, this.mModem, this.mIsWrapRequired);
        sOsuEngine = osuEngine;
        return osuEngine.verifyAndReadDERFile(str);
    }

    protected boolean prepareOsuSetup() {
        boolean z = IS_DEBUG;
        if (z) {
            Log.d("OSU-OSUCore", "prepareOsuSetup Entry");
        }
        if (this.mChannel == null || this.mModem == null || this.mContext == null) {
            Log.e("OSU-OSUCore", "Wrong State");
            return false;
        }
        try {
            if (!IAROperation.getInstance().doPreCondition()) {
                Log.e("OSU-OSUCore", "IAR specific precondition failed");
                return false;
            }
            if (!openChannel()) {
                Log.e("OSU-OSUCore", "Not able to Open Channel");
                return false;
            }
            sOsuEngine = OsuEngine.getInstance(this.mChannel, this.mContext, this.mModem, this.mIsWrapRequired);
            if (!OSULogger.isOpened()) {
                OSULogger.open(this.mContext);
            }
            this.mLoggerFile = OSULogger.getLoggerPath();
            OSULogger.write(sOsuEngine.mInputFile, false);
            if (!z) {
                return true;
            }
            Log.d("OSU-OSUCore", "prepareOsuSetup Success");
            return true;
        } catch (Exception e) {
            Log.e("OSU-OSUCore", e.getMessage());
            return false;
        }
    }

    private boolean checkDERIntegrity() {
        OsuEngine osuEngine;
        byte[] sHA256Hash;
        byte[] signaturePlain;
        if (this.mChannel == null || (osuEngine = sOsuEngine) == null || osuEngine.mInputFile == null) {
            Log.e("OSU-OSUCore", "Wrong State");
            return false;
        }
        OsuAsnHelper osuAsnHelper = OsuAsnHelper.getInstance();
        if (osuAsnHelper == null) {
            return false;
        }
        try {
            if (OsuOmapiHelper.getOsVersionId() >= 48) {
                sHA256Hash = osuAsnHelper.getSHA384Hash();
                signaturePlain = osuAsnHelper.getSignaturePlainECC384();
            } else {
                sHA256Hash = osuAsnHelper.getSHA256Hash();
                signaturePlain = osuAsnHelper.getSignaturePlain();
            }
            byte[] bArrCreateIntegrityCheckCmd = OsuOmapiHelper.createIntegrityCheckCmd(sHA256Hash, signaturePlain);
            if (bArrCreateIntegrityCheckCmd != null) {
                return sOsuEngine.sendAndValidate(bArrCreateIntegrityCheckCmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected SecOsuStatusCode performOsuSteps() {
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_FAILED;
        IAccess iAccess = this.mChannel;
        if (iAccess == null || sOsuEngine == null) {
            Log.e("OSU-OSUCore", "Wrong State");
            return secOsuStatusCode;
        }
        try {
            OsuOmapiHelper.parseATR(iAccess.getAtr());
            if (OsuOmapiHelper.getOsVersionId() < 48) {
                SecOsuStatusCode secOsuStatusCodeReset = sOsuEngine.reset();
                if (secOsuStatusCodeReset != SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                    if (!this.mModem.setModemDirectMode(false)) {
                        Log.w("OSU-OSUCore", "Not able to Disable Modem Direct mode");
                    }
                    Log.d("OSU-OSUCore", "OS Update completed with status : " + secOsuStatusCodeReset + " Re-enabling NFC");
                    sOsuEngine.osuDisableNfc(false);
                    sOsuEngine.osuDoNfcResetIfRequired();
                    OSULogger.close();
                    return secOsuStatusCodeReset;
                }
            } else {
                sOsuEngine.setWrapRequired(false);
            }
            SecOsuStatusCode secOsuStatusCodePerformGPUAITrigger = sOsuEngine.performGPUAITrigger();
            if (secOsuStatusCodePerformGPUAITrigger != SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                Log.e("OSU-OSUCore", "performGPUAITrigger failed");
                if (!this.mModem.setModemDirectMode(false)) {
                    Log.w("OSU-OSUCore", "Not able to Disable Modem Direct mode");
                }
                Log.d("OSU-OSUCore", "OS Update completed with status : " + secOsuStatusCodePerformGPUAITrigger + " Re-enabling NFC");
                sOsuEngine.osuDisableNfc(false);
                sOsuEngine.osuDoNfcResetIfRequired();
                OSULogger.close();
                return secOsuStatusCodePerformGPUAITrigger;
            }
            if (OsuOmapiHelper.isIntegrityCheckrequired() && !checkDERIntegrity()) {
                SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_INVALID_UPDATE;
                if (!this.mModem.setModemDirectMode(false)) {
                    Log.w("OSU-OSUCore", "Not able to Disable Modem Direct mode");
                }
                Log.d("OSU-OSUCore", "OS Update completed with status : " + secOsuStatusCodePerformGPUAITrigger + " Re-enabling NFC");
                sOsuEngine.osuDisableNfc(false);
                sOsuEngine.osuDoNfcResetIfRequired();
                OSULogger.close();
                return secOsuStatusCode2;
            }
            for (int i = 1; i <= 3; i++) {
                SecOsuStatusCode secOsuStatusCodeOsuQueryUAIInfo = sOsuEngine.osuQueryUAIInfo();
                SecOsuStatusCode secOsuStatusCode3 = SecOsuStatusCode.OSU_STATUS_SUCCESS;
                if (secOsuStatusCodeOsuQueryUAIInfo != secOsuStatusCode3) {
                    if (!this.mModem.setModemDirectMode(false)) {
                        Log.w("OSU-OSUCore", "Not able to Disable Modem Direct mode");
                    }
                    Log.d("OSU-OSUCore", "OS Update completed with status : " + secOsuStatusCodeOsuQueryUAIInfo + " Re-enabling NFC");
                    sOsuEngine.osuDisableNfc(false);
                    sOsuEngine.osuDoNfcResetIfRequired();
                    OSULogger.close();
                    return secOsuStatusCodeOsuQueryUAIInfo;
                }
                secOsuStatusCodePerformGPUAITrigger = sOsuEngine.start();
                OSULogger.write("********************************************************", false);
                OSULogger.write("Status : " + secOsuStatusCodePerformGPUAITrigger.getMessage(), true);
                if (secOsuStatusCodePerformGPUAITrigger != SecOsuStatusCode.OSU_STATUS_FAILED) {
                    break;
                }
                if (i != 3) {
                    if (OsuOmapiHelper.getOsVersionId() < 48) {
                        SecOsuStatusCode secOsuStatusCodeReset2 = sOsuEngine.reset();
                        if (secOsuStatusCodeReset2 == SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN) {
                            if (!this.mModem.setModemDirectMode(false)) {
                                Log.w("OSU-OSUCore", "Not able to Disable Modem Direct mode");
                            }
                            Log.d("OSU-OSUCore", "OS Update completed with status : " + secOsuStatusCodePerformGPUAITrigger + " Re-enabling NFC");
                            sOsuEngine.osuDisableNfc(false);
                            sOsuEngine.osuDoNfcResetIfRequired();
                            OSULogger.close();
                            return secOsuStatusCodeReset2;
                        }
                    } else {
                        secOsuStatusCodePerformGPUAITrigger = sOsuEngine.performGPUAITrigger();
                        if (secOsuStatusCodePerformGPUAITrigger != secOsuStatusCode3) {
                            Log.e("OSU-OSUCore", "performGPUAITrigger failed");
                            if (!this.mModem.setModemDirectMode(false)) {
                                Log.w("OSU-OSUCore", "Not able to Disable Modem Direct mode");
                            }
                            Log.d("OSU-OSUCore", "OS Update completed with status : " + secOsuStatusCodePerformGPUAITrigger + " Re-enabling NFC");
                            sOsuEngine.osuDisableNfc(false);
                            sOsuEngine.osuDoNfcResetIfRequired();
                            OSULogger.close();
                            return secOsuStatusCodePerformGPUAITrigger;
                        }
                    }
                }
            }
            if (!this.mModem.setModemDirectMode(false)) {
                Log.w("OSU-OSUCore", "Not able to Disable Modem Direct mode");
            }
            Log.d("OSU-OSUCore", "OS Update completed with status : " + secOsuStatusCodePerformGPUAITrigger + " Re-enabling NFC");
            sOsuEngine.osuDisableNfc(false);
            sOsuEngine.osuDoNfcResetIfRequired();
            OSULogger.close();
            if (secOsuStatusCodePerformGPUAITrigger == SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                OsuOmapiApduChannel.sIsRefreshRequired = true;
            }
            return secOsuStatusCodePerformGPUAITrigger;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (!this.mModem.setModemDirectMode(false)) {
                    Log.w("OSU-OSUCore", "Not able to Disable Modem Direct mode");
                }
                Log.d("OSU-OSUCore", "OS Update completed with status : " + secOsuStatusCode + " Re-enabling NFC");
                sOsuEngine.osuDisableNfc(false);
                sOsuEngine.osuDoNfcResetIfRequired();
                OSULogger.close();
                throw th2;
            }
        }
    }

    protected boolean finishOsuSteps() {
        IAccess iAccess = this.mChannel;
        if (iAccess != null) {
            iAccess.close();
        }
        sOsuEngine.resetEngineParams();
        OsuOmapiApduChannel.sIsRefreshRequired = false;
        if (IAROperation.getInstance().doPostCondition()) {
            return true;
        }
        Log.e("OSU-OSUCore", "IAR specific postcondition failed");
        return true;
    }

    private boolean openChannel() {
        byte[] bArrOpenChannel;
        IAccess iAccess = this.mChannel;
        if (iAccess == null) {
            Log.e("OSU-OSUCore", "Wrong State");
            return false;
        }
        byte[] bArrOpen = iAccess.open();
        byte[] bArr = SUCCESS_RESP;
        if (!OsuEngine.validateResponse(bArrOpen, bArr)) {
            Log.e("OSU-OSUCore", "Not able to Open Session");
            return false;
        }
        OsuOmapiHelper.parseATR(this.mChannel.getAtr());
        if (OsuOmapiHelper.getOsVersionId() >= 48) {
            bArrOpenChannel = this.mChannel.openChannel(OsuOmapiHelper.OSU_AID_SN3XX);
            this.mIsWrapRequired = false;
        } else {
            if (!this.mIsWrapRequired) {
                return true;
            }
            bArrOpenChannel = this.mChannel.openChannel(OsuOmapiHelper.OSUTrigger);
        }
        if (IS_DEBUG) {
            Log.d("OSU-OSUCore", "Openbasic channel Response " + HexString.fromByteArray(bArrOpenChannel, 0));
        }
        if (bArrOpenChannel != null && OsuEngine.validateResponse(bArrOpenChannel, bArr)) {
            return true;
        }
        Log.e("OSU-OSUCore", "Not able to Open Access Channel");
        return false;
    }

    protected boolean setOsuConfig(SecOsuConfigType secOsuConfigType, SecOsuConfigValue secOsuConfigValue) {
        boolean z = secOsuConfigValue == SecOsuConfigValue.ENABLED;
        int i = AnonymousClass1.$SwitchMap$com$nxp$osu$SecOsuConfigType[secOsuConfigType.ordinal()];
        if (i == 1) {
            OsuOmapiHelper.setIntegrityFeatureSupported(z);
            return true;
        }
        if (i == 2) {
            OSULogger.setNFCDisableRequired(z);
            return true;
        }
        if (i == 3) {
            OsuOmapiHelper.setWrapApduSupported(z);
            return true;
        }
        if (i == 4) {
            OSULogger.setNFCRestartSupported(z);
            return true;
        }
        Log.e("OSU-OSUCore", "Invalid ConfigType");
        return false;
    }

    /* JADX INFO: renamed from: com.nxp.osu.osucore.OsuCore$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$nxp$osu$SecOsuConfigType;

        static {
            int[] iArr = new int[SecOsuConfigType.values().length];
            $SwitchMap$com$nxp$osu$SecOsuConfigType = iArr;
            try {
                iArr[SecOsuConfigType.INTEGRITY_CHECK_SUPPORT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$nxp$osu$SecOsuConfigType[SecOsuConfigType.NFC_DISABLE_SUPPORT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$nxp$osu$SecOsuConfigType[SecOsuConfigType.APDU_WRAP_SUPPORT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$nxp$osu$SecOsuConfigType[SecOsuConfigType.NFC_RESTART_SUPPORT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }
}
