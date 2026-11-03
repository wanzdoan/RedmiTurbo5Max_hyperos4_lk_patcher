package com.nxp.osu.osucore;

import android.content.Context;
import android.util.Log;
import com.nxp.id.tp.common.HexString;
import com.nxp.osu.IModem;
import com.nxp.osu.SecOsuException;
import com.nxp.osu.SecOsuStatus;
import com.nxp.osu.SecOsuStatusCode;
import com.nxp.osu.channel.IAccess;
import com.nxp.osu.channel.OsuOmapiHelper;
import com.nxp.osu.util.OSULogger;
import com.nxp.osu.util.OSUNfcHandler;
import com.nxp.osu.util.UAIQueryParser;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
class OsuEngine {
    protected IAccess mChannel;
    private Context mContext;
    protected boolean mIsWrapRequired;
    private IModem mModem;
    private static final boolean IS_DEBUG = OSULogger.isDebug();
    private static final byte[] ISD_CMD = {0, -92, 4, 0, 0};
    private static final byte[] SUCCESS_RESP = {-112, 0};
    private static final byte[] OS_MUTE_STATUS_RESP = {-97, -1};
    private static final byte[] STATUS_INCORRECT_PARAM = {106, -128};
    private static final byte[] OUT_OF_MEMORY_RESP = {110, 0};
    private static final byte[] OUT_OF_MEMORY_RESP_2 = {111, 0};
    private static final byte[] GP_OSU_COMMUNICATION_ERROR = {111, 0};
    private static final byte[] OUT_OF_MEMORY_GP_SUPPORT = {106, -124};
    private static byte[] sLastErrorCmdRespBuffer = null;
    private static boolean sIsResetRequired = true;
    private static OsuEngine sOsuEngine = null;
    protected int mDERFSN = -1;
    protected int mDERRSN = -1;
    protected UAIQueryParser mUAIInfo = null;
    protected int mOSU = OsuAsnHelper.OS_TYPE_UNKNOWN;
    private int mNumOfStepsInDER = -1;
    protected boolean mIseUICCDomin = false;
    protected String mInputFile = null;
    protected boolean mIsShuttingDown = false;
    private IOsuAsnHelper mAsnHelper = null;

    private static boolean isAMDHBackupInterrupted(short s) {
        return s == 16 || s == 32 || s == 48;
    }

    private static boolean isAMDHBackupRequired(short s) {
        return s == 0 || s == 1 || s == 16 || s == 32 || s == 48;
    }

    private static boolean isAMDHRestoreRequired(short s) {
        return s == 3 || s == 4 || s == 64 || s == 80 || s == 96;
    }

    public static boolean isInvalid(int i) {
        return i == -1;
    }

    public static String getVersion() {
        String str = String.format("Version: NXP_AR_%02X_%05X_%02d.%02X.%02X", 0, 518144, 16, 4, 0);
        Log.i("OSU-OsuEngine", "OSU Engine : ".concat(str));
        return str;
    }

    protected static OsuEngine getInstance(IAccess iAccess, Context context, IModem iModem, boolean z) {
        if (sOsuEngine == null && iAccess != null && context != null && iModem != null) {
            sOsuEngine = new OsuEngine(iAccess, context, iModem, z);
        }
        OsuEngine osuEngine = sOsuEngine;
        if (osuEngine != null) {
            osuEngine.setWrapRequired(z);
        }
        return sOsuEngine;
    }

    public void setWrapRequired(boolean z) {
        this.mIsWrapRequired = z;
    }

    private OsuEngine(IAccess iAccess, Context context, IModem iModem, boolean z) {
        this.mChannel = iAccess;
        this.mContext = context;
        this.mModem = iModem;
        this.mIsWrapRequired = z;
    }

    protected SecOsuStatusCode verifyAndReadDERFile(String str) {
        OsuAsnHelper osuAsnHelper = OsuAsnHelper.getInstance();
        this.mAsnHelper = osuAsnHelper;
        if (osuAsnHelper == null) {
            return SecOsuStatusCode.OSU_STATUS_FAILED;
        }
        int iLoadFile = osuAsnHelper.loadFile(str);
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_FILE_NOT_FOUND;
        if (iLoadFile == secOsuStatusCode.getValue()) {
            return secOsuStatusCode;
        }
        SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_SUCCESS;
        if (iLoadFile != secOsuStatusCode2.getValue()) {
            return SecOsuStatusCode.OSU_STATUS_INVALID_FILE;
        }
        if (!fillOsuEngine()) {
            return SecOsuStatusCode.OSU_STATUS_INVALID_FILE;
        }
        this.mInputFile = str;
        return secOsuStatusCode2;
    }

    protected boolean fillOsuEngine() {
        this.mNumOfStepsInDER = this.mAsnHelper.getNumOfSteps();
        this.mOSU = this.mAsnHelper.getOsuType();
        this.mDERFSN = this.mAsnHelper.getDERFSN();
        int derrsn = this.mAsnHelper.getDERRSN();
        this.mDERRSN = derrsn;
        if (isInvalid(derrsn) || isInvalid(this.mDERFSN)) {
            resetEngineParams();
            Log.e("OSU-OsuEngine", "Invalid Update");
            return false;
        }
        if (!IS_DEBUG) {
            return true;
        }
        Log.d("OSU-OsuEngine", "DERRSN :" + String.format("0x%X", Integer.valueOf(this.mDERRSN)) + " DERFSN : " + String.format("0x%X", Integer.valueOf(this.mDERFSN)));
        return true;
    }

    public SecOsuStatusCode osuDisableNfc(boolean z) {
        if (!OSUNfcHandler.disableNfc(z, this.mContext)) {
            if (IS_DEBUG) {
                if (z) {
                    Log.d("OSU-OsuEngine", "Failed to disable NFC");
                } else {
                    Log.d("OSU-OsuEngine", "Failed to enable NFC");
                }
            }
            return SecOsuStatusCode.OSU_STATUS_FAILED;
        }
        return SecOsuStatusCode.OSU_STATUS_SUCCESS;
    }

    public SecOsuStatusCode osuQueryUAIInfo() {
        if (this.mIsShuttingDown) {
            return SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
        }
        UAIQueryParser uAIQueryParserQueryUAIInfo = queryUAIInfo();
        this.mUAIInfo = uAIQueryParserQueryUAIInfo;
        if (uAIQueryParserQueryUAIInfo == null) {
            Log.e("OSU-OsuEngine", "Failed to get UAI info");
            return SecOsuStatusCode.OSU_STATUS_GET_DATA_FAILED;
        }
        this.mIseUICCDomin = isJCOPinEuiccDomain(uAIQueryParserQueryUAIInfo);
        if (IS_DEBUG) {
            Log.d("OSU-OsuEngine", "is JCOP in eUICC Domain : " + this.mIseUICCDomin);
        }
        if (this.mIsShuttingDown) {
            return SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
        }
        return SecOsuStatusCode.OSU_STATUS_SUCCESS;
    }

    public void osuDoNfcResetIfRequired() {
        OSUNfcHandler.doNfcResetIfRequired(this.mContext);
    }

    public SecOsuStatusCode start() {
        UAIQueryParser uAIQueryParser;
        if (this.mChannel == null) {
            Log.e("OSU-OsuEngine", "Channel is  not Initilized");
            return SecOsuStatusCode.OSU_STATUS_FAILED;
        }
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_FAILED;
        if (IS_DEBUG) {
            Log.d("OSU-OsuEngine", "OsuEngine start DERRSN : " + String.format("0x%X", Integer.valueOf(this.mDERRSN)) + " DERFSN : " + String.format("0x%X", Integer.valueOf(this.mDERFSN)) + " JRSN : " + String.format("0x%X", Integer.valueOf(this.mUAIInfo.RSN)) + " JCSN : " + String.format("0x%X", Integer.valueOf(this.mUAIInfo.CSN)) + " JFSN : " + String.format("0x%X", Integer.valueOf(this.mUAIInfo.FSN)));
        }
        if (this.mDERFSN == -1 || this.mDERRSN == -1 || (uAIQueryParser = this.mUAIInfo) == null || this.mOSU == OsuAsnHelper.OS_TYPE_UNKNOWN) {
            Log.e("OSU-OsuEngine", "OSU-Engine has not filled the Enough IInformation");
            return secOsuStatusCode;
        }
        SecOsuStatus secOsuStatusValidateUpdate = validateUpdate(uAIQueryParser);
        if ("Invalid DER File".equals(secOsuStatusValidateUpdate.info) || ("Up To Date".equals(secOsuStatusValidateUpdate.info) && !isSelfUpdate())) {
            Log.e("OSU-OsuEngine", "Invalid DER for Current OS or Already Updated");
            return secOsuStatusCode;
        }
        return processUpdate();
    }

    private boolean isUpdateSuccess() {
        int i;
        UAIQueryParser uAIQueryParser;
        int i2;
        boolean z = true;
        if (OsuOmapiHelper.getOsVersionId() < 48 || (i2 = (uAIQueryParser = this.mUAIInfo).RSN) != uAIQueryParser.CSN || i2 != uAIQueryParser.FSN || i2 != this.mDERFSN) {
            UAIQueryParser uAIQueryParser2 = this.mUAIInfo;
            if (uAIQueryParser2.UOS_ID != 90 || (i = uAIQueryParser2.RSN) != uAIQueryParser2.CSN || i != uAIQueryParser2.FSN || i != this.mDERFSN) {
                z = false;
            }
        }
        UAIQueryParser uAIQueryParser3 = this.mUAIInfo;
        if (!uAIQueryParser3.isHostResumeSupported || (uAIQueryParser3.amdHState & (-2)) == 0) {
            return z;
        }
        return false;
    }

    protected boolean isSelfUpdate() {
        int i = this.mOSU;
        return i == OsuAsnHelper.OS_TYPE_SELF_UPDATE || i == OsuAsnHelper.OS_TYPE_SELF_UPDATE_WITH_AMDH;
    }

    protected boolean isOsuAmdhType() {
        int i = this.mOSU;
        return i == OsuAsnHelper.OS_TYPE_REAL_UPDATE_WITH_AMDH || i == OsuAsnHelper.OS_TYPE_SELF_UPDATE_WITH_AMDH;
    }

    protected boolean isPatchUpdate() {
        return this.mOSU == OsuAsnHelper.OS_TYPE_PATCH_UPDATE;
    }

    protected boolean isAMDHRestorePending() {
        if (!isOsuAmdhType()) {
            return false;
        }
        UAIQueryParser uAIQueryParser = this.mUAIInfo;
        return uAIQueryParser.UOS_ID == 90 && uAIQueryParser.isHostResumeSupported && isAMDHRestoreRequired(uAIQueryParser.amdHState);
    }

    protected boolean isJCOPinEuiccDomain(UAIQueryParser uAIQueryParser) {
        if (uAIQueryParser == null || !isOsuAmdhType() || uAIQueryParser.UOS_ID != 90) {
            return false;
        }
        if (uAIQueryParser.isHostResumeSupported) {
            Log.d("OSU-OsuEngine", "AMD-H Host resume supported");
            return (uAIQueryParser.amdHState & (-2)) != 0;
        }
        Log.d("OSU-OsuEngine", "AMD-H Auto-resume supported");
        return !sendAndValidate(ISD_CMD);
    }

    private SecOsuStatusCode processUpdate() {
        SecOsuStatusCode secOsuStatusCodeTriggerAMDHRestore;
        SecOsuStatusCode secOsuStatusCodePerformUAIQuery;
        if (IS_DEBUG) {
            Log.d("OSU-OsuEngine", "processUpdate Entry");
        }
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
        SecOsuStatusCode secOsuStatusCodeOsuDisableNfc = osuDisableNfc(true);
        SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_FAILED;
        if (secOsuStatusCodeOsuDisableNfc != secOsuStatusCode2 && this.mModem.setModemDirectMode(true)) {
            if (isOsuAmdhType()) {
                if (isAMDHRestoreRequired(this.mUAIInfo.amdHState)) {
                    return triggerAMDHRestore();
                }
                if (isAMDHBackupRequired(this.mUAIInfo.amdHState)) {
                    IOsuAsnHelper iOsuAsnHelper = this.mAsnHelper;
                    UAIQueryParser uAIQueryParser = this.mUAIInfo;
                    int matchingAction = iOsuAsnHelper.getMatchingAction(uAIQueryParser.UOS_ID, uAIQueryParser.RSN, uAIQueryParser.CSN);
                    if (matchingAction >= 0) {
                        SecOsuStatusCode secOsuStatusCodeReset = reset();
                        SecOsuStatusCode secOsuStatusCode3 = SecOsuStatusCode.OSU_STATUS_SUCCESS;
                        if (secOsuStatusCodeReset != secOsuStatusCode3) {
                            return secOsuStatusCodeReset;
                        }
                        SecOsuStatusCode secOsuStatusCodeTriggerAMDHBackup = triggerAMDHBackup(matchingAction, this.mIseUICCDomin);
                        if (secOsuStatusCodeTriggerAMDHBackup != secOsuStatusCode3) {
                            return secOsuStatusCodeTriggerAMDHBackup;
                        }
                        SecOsuStatusCode secOsuStatusCodePerformUAIQuery2 = performUAIQuery();
                        if (secOsuStatusCodePerformUAIQuery2 != secOsuStatusCode3) {
                            return secOsuStatusCodePerformUAIQuery2;
                        }
                    }
                } else {
                    Log.d("OSU-OsuEngine", "Continue with OSU steps");
                }
            }
            if (isOsuNotTeared()) {
                SecOsuStatusCode secOsuStatusCodeTriggerInitialSteps = triggerInitialSteps();
                SecOsuStatusCode secOsuStatusCode4 = SecOsuStatusCode.OSU_STATUS_SUCCESS;
                if (secOsuStatusCodeTriggerInitialSteps != secOsuStatusCode4) {
                    return secOsuStatusCodeTriggerInitialSteps;
                }
                if (OsuOmapiHelper.getOsVersionId() < 48) {
                    SecOsuStatusCode secOsuStatusCodeTriggerOSU = triggerOSU();
                    if (secOsuStatusCodeTriggerOSU != secOsuStatusCode4) {
                        return secOsuStatusCodeTriggerOSU;
                    }
                    SecOsuStatusCode secOsuStatusCodeReset2 = reset();
                    if (secOsuStatusCodeReset2 != secOsuStatusCode4) {
                        return secOsuStatusCodeReset2;
                    }
                }
                SecOsuStatusCode secOsuStatusCodePerformUAIQuery3 = performUAIQuery();
                if (secOsuStatusCodePerformUAIQuery3 != secOsuStatusCode4) {
                    return secOsuStatusCodePerformUAIQuery3;
                }
            }
            IOsuAsnHelper iOsuAsnHelper2 = this.mAsnHelper;
            UAIQueryParser uAIQueryParser2 = this.mUAIInfo;
            int matchingAction2 = iOsuAsnHelper2.getMatchingAction(uAIQueryParser2.UOS_ID, uAIQueryParser2.RSN, uAIQueryParser2.CSN);
            if (matchingAction2 < 0 && !isSelfUpdate()) {
                Log.d("OSU-OsuEngine", "Not able to get Action Index from DER");
                return SecOsuStatusCode.OSU_STATUS_GETSTEP_FAILED;
            }
            boolean z = false;
            while (matchingAction2 > 0 && !z) {
                if (matchingAction2 == this.mNumOfStepsInDER) {
                    z = true;
                }
                SecOsuStatusCode secOsuStatusCodeTriggerStep = triggerStep(matchingAction2);
                SecOsuStatusCode secOsuStatusCode5 = SecOsuStatusCode.OSU_STATUS_SUCCESS;
                if (secOsuStatusCodeTriggerStep != secOsuStatusCode5) {
                    return secOsuStatusCodeTriggerStep;
                }
                if ((OsuOmapiHelper.getOsVersionId() < 48 || !isPatchUpdate()) && (secOsuStatusCodePerformUAIQuery = performUAIQuery()) != secOsuStatusCode5) {
                    return secOsuStatusCodePerformUAIQuery;
                }
                if (OsuOmapiHelper.getOsVersionId() < 48) {
                    SecOsuStatusCode secOsuStatusCodeHandleTriggerOSU = handleTriggerOSU();
                    if (secOsuStatusCodeHandleTriggerOSU != secOsuStatusCode5) {
                        return secOsuStatusCodeHandleTriggerOSU;
                    }
                } else {
                    SecOsuStatusCode secOsuStatusCodeHandleGPMultiPatchUpdate = handleGPMultiPatchUpdate(matchingAction2);
                    if (secOsuStatusCodeHandleGPMultiPatchUpdate != secOsuStatusCode5) {
                        return secOsuStatusCodeHandleGPMultiPatchUpdate;
                    }
                }
                IOsuAsnHelper iOsuAsnHelper3 = this.mAsnHelper;
                UAIQueryParser uAIQueryParser3 = this.mUAIInfo;
                matchingAction2 = iOsuAsnHelper3.getMatchingAction(uAIQueryParser3.UOS_ID, uAIQueryParser3.RSN, uAIQueryParser3.CSN);
                if (isOsuAmdhType() && isSelfUpdate() && this.mUAIInfo.UOS_ID == 90 && matchingAction2 > 0) {
                    matchingAction2++;
                }
            }
            if (isOsuAmdhType() && isAMDHRestoreRequired(this.mUAIInfo.amdHState) && (secOsuStatusCodeTriggerAMDHRestore = triggerAMDHRestore()) != SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                return secOsuStatusCodeTriggerAMDHRestore;
            }
            if (IS_DEBUG) {
                Log.d("OSU-OsuEngine", "OsuEngine Performing finishUpdate");
            }
            SecOsuStatusCode secOsuStatusCodeFinishUpdate = finishUpdate();
            if (secOsuStatusCodeFinishUpdate != SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                Log.e("OSU-OsuEngine", "OSU Engine Failed to FinishOSUpdate");
            }
            if (isUpdateSuccess()) {
                return secOsuStatusCodeFinishUpdate;
            }
            Log.e("OSU-OsuEngine", "OSU Engine Failed to Update OSU !!!");
            return SecOsuStatusCode.OSU_STATUS_FAILED;
        }
        return secOsuStatusCode2;
    }

    private boolean isOsuNotTeared() {
        if (OsuOmapiHelper.getOsVersionId() >= 48) {
            if (OsuOmapiHelper.isJcopInNormalMode()) {
                UAIQueryParser uAIQueryParser = this.mUAIInfo;
                if (uAIQueryParser.UOS_ID == 1) {
                    int i = uAIQueryParser.RSN;
                    int i2 = uAIQueryParser.FSN;
                    if (i == i2 && i2 == uAIQueryParser.CSN) {
                        return true;
                    }
                }
            }
            return false;
        }
        UAIQueryParser uAIQueryParser2 = this.mUAIInfo;
        return uAIQueryParser2.UOS_ID == 90 && !(uAIQueryParser2.isHostResumeSupported && (isAMDHBackupInterrupted(uAIQueryParser2.amdHState) || isAMDHRestoreRequired(this.mUAIInfo.amdHState)));
    }

    public SecOsuStatus validateUpdate(UAIQueryParser uAIQueryParser) {
        SecOsuStatus secOsuStatus = new SecOsuStatus();
        secOsuStatus.status = SecOsuStatusCode.OSU_STATUS_FAILED.getValue();
        secOsuStatus.info = SecOsuStatusCode.OSU_STATUS_UNKNOWN.getMessage();
        try {
            secOsuStatus.status = SecOsuStatusCode.OSU_STATUS_SUCCESS.getValue();
            if (isOsuNotTeared()) {
                if (this.mAsnHelper.getMatchingAction(1, uAIQueryParser.RSN, uAIQueryParser.CSN) > 0) {
                    secOsuStatus.info = "Update Required";
                }
                if (uAIQueryParser.CSN == this.mDERFSN) {
                    secOsuStatus.info = "Up To Date";
                    return secOsuStatus;
                }
            } else {
                int i = uAIQueryParser.RSN;
                if (isAMDHRestorePending()) {
                    i = uAIQueryParser.PSN;
                }
                if (this.mAsnHelper.getMatchingAction(this.mUAIInfo.UOS_ID, i, uAIQueryParser.CSN) > 0) {
                    secOsuStatus.info = "Incomplete Update";
                    return secOsuStatus;
                }
                secOsuStatus.info = "Invalid DER File";
            }
        } catch (Exception unused) {
        }
        return secOsuStatus;
    }

    public void resetEngineParams() {
        this.mDERFSN = -1;
        this.mDERRSN = -1;
        this.mUAIInfo = null;
        this.mOSU = OsuAsnHelper.OS_TYPE_UNKNOWN;
        this.mIseUICCDomin = false;
        this.mNumOfStepsInDER = -1;
    }

    protected boolean verifyStatus() throws SecOsuException {
        if (IS_DEBUG) {
            Log.d("OSU-OsuEngine", "verifyStatus Entry");
        }
        if (this.mChannel == null) {
            Log.e("OSU-OsuEngine", "Wrong state ");
            throw new SecOsuException("Invalid State.");
        }
        if (OsuOmapiHelper.getOsVersionId() < 48 || !isPatchUpdate()) {
            UAIQueryParser uAIQueryParserQueryUAIInfo = queryUAIInfo();
            this.mUAIInfo = uAIQueryParserQueryUAIInfo;
            if (uAIQueryParserQueryUAIInfo == null) {
                Log.e("OSU-OsuEngine", "Not able to Query UAI");
                return false;
            }
        }
        return isUpdateSuccess();
    }

    private SecOsuStatusCode sendJCIPackets() {
        try {
            if (IS_DEBUG) {
                Log.d("OSU-OsuEngine", "performing JCI.. ");
            }
            SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
            Iterator it = this.mAsnHelper.getJCIApdus(OsuOmapiHelper.getOsVersionId() >= 48).iterator();
            while (it.hasNext()) {
                byte[] byteArray = this.mAsnHelper.toByteArray((String) it.next());
                if (this.mIsWrapRequired) {
                    byteArray = OsuOmapiHelper.checkAndwrapPacket(byteArray);
                }
                SecOsuStatusCode secOsuStatusCodeSendAndGetStatus = sendAndGetStatus(byteArray);
                if (secOsuStatusCodeSendAndGetStatus != SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                    Log.e("OSU-OsuEngine", "JCI APDU Failed");
                    byte[] responseCode = secOsuStatusCodeSendAndGetStatus.getResponseCode();
                    SecOsuStatusCode gpResetRequiredFromResponse = setGpResetRequiredFromResponse(responseCode, true);
                    SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_NO_MEM;
                    if (gpResetRequiredFromResponse != secOsuStatusCode2 && (responseCode == null || !validateResponse(responseCode, OUT_OF_MEMORY_RESP))) {
                        return SecOsuStatusCode.OSU_STATUS_PRECONDITION_FAILED;
                    }
                    return secOsuStatusCode2;
                }
            }
            return SecOsuStatusCode.OSU_STATUS_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return SecOsuStatusCode.OSU_STATUS_UNKNOWN;
        }
    }

    private SecOsuStatusCode sendCCIPackets() {
        try {
            if (IS_DEBUG) {
                Log.d("OSU-OsuEngine", "performing CCI.. ");
            }
            SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
            Iterator it = this.mAsnHelper.getCCIApdus(OsuOmapiHelper.getOsVersionId() >= 48).iterator();
            while (it.hasNext()) {
                byte[] byteArray = this.mAsnHelper.toByteArray((String) it.next());
                if (this.mIsWrapRequired) {
                    byteArray = OsuOmapiHelper.checkAndwrapPacket(byteArray);
                }
                SecOsuStatusCode secOsuStatusCodeSendAndGetStatus = sendAndGetStatus(byteArray);
                if (secOsuStatusCodeSendAndGetStatus != SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                    Log.e("OSU-OsuEngine", "CCI APDU Failed");
                    byte[] responseCode = secOsuStatusCodeSendAndGetStatus.getResponseCode();
                    SecOsuStatusCode gpResetRequiredFromResponse = setGpResetRequiredFromResponse(responseCode, true);
                    SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_NO_MEM;
                    if (gpResetRequiredFromResponse != secOsuStatusCode2 && (responseCode == null || !validateResponse(responseCode, OUT_OF_MEMORY_RESP))) {
                        return SecOsuStatusCode.OSU_STATUS_PRECONDITION_FAILED;
                    }
                    return secOsuStatusCode2;
                }
            }
            return SecOsuStatusCode.OSU_STATUS_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return SecOsuStatusCode.OSU_STATUS_UNKNOWN;
        }
    }

    private SecOsuStatusCode triggerStep(int i) {
        SecOsuStatusCode secOsuStatusCodePerformUAIQuery;
        SecOsuStatusCode secOsuStatusCodeSendAndGetStatus = SecOsuStatusCode.OSU_STATUS_FAILED;
        if (IS_DEBUG) {
            Log.d("OSU-OsuEngine", "Triggering Step " + i);
        }
        try {
            try {
                if (OsuOmapiHelper.getOsVersionId() >= 48) {
                    sIsResetRequired = this.mAsnHelper.isResetRequiredAfterAction(i);
                }
                List apdus = this.mAsnHelper.getApdus(i);
                OSULogger.write("STEP : " + i, true);
                Iterator it = apdus.iterator();
                boolean z = true;
                while (it.hasNext()) {
                    secOsuStatusCodeSendAndGetStatus = sendAndGetStatus(this.mAsnHelper.toByteArray((String) it.next()));
                    if (secOsuStatusCodeSendAndGetStatus != SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                        Log.e("OSU-OsuEngine", "Step " + i + " Failed");
                        byte[] responseCode = secOsuStatusCodeSendAndGetStatus.getResponseCode();
                        SecOsuStatusCode gpResetRequiredFromResponse = setGpResetRequiredFromResponse(responseCode, false);
                        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_NO_MEM;
                        if (gpResetRequiredFromResponse == secOsuStatusCode) {
                            secOsuStatusCodeSendAndGetStatus = secOsuStatusCode;
                        }
                        if (i != 1 || !z || responseCode == null || !validateResponse(responseCode, OUT_OF_MEMORY_RESP_2)) {
                            secOsuStatusCode = secOsuStatusCodeSendAndGetStatus;
                        }
                        try {
                            doReset();
                            return secOsuStatusCode;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return secOsuStatusCode;
                        }
                    }
                    z = false;
                }
                try {
                    if (OsuOmapiHelper.getOsVersionId() < 48 || !isPatchUpdate() || (secOsuStatusCodePerformUAIQuery = performUAIQuery()) == SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                        return doReset();
                    }
                    doReset();
                    return secOsuStatusCodePerformUAIQuery;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return secOsuStatusCodeSendAndGetStatus;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_UNKNOWN;
                try {
                    doReset();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                return secOsuStatusCode2;
            }
        } catch (Throwable th) {
            try {
                doReset();
            } catch (IOException e5) {
                e5.printStackTrace();
            }
            throw th;
        }
    }

    private SecOsuStatusCode triggerOSU() {
        try {
            SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
            SecOsuStatusCode secOsuStatusCodeDoReset = doReset();
            SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_SUCCESS;
            if (secOsuStatusCodeDoReset != secOsuStatusCode2) {
                return secOsuStatusCodeDoReset;
            }
            byte[] bArrCheckAndwrapPacket = {79, 112, -128, 19, 4, -34, -83, -66, -17};
            if (this.mIsWrapRequired) {
                bArrCheckAndwrapPacket = OsuOmapiHelper.checkAndwrapPacket(bArrCheckAndwrapPacket);
            }
            SecOsuStatusCode secOsuStatusCodeSendApdu = sendApdu(bArrCheckAndwrapPacket);
            if (secOsuStatusCodeSendApdu == secOsuStatusCode2) {
                return secOsuStatusCode2;
            }
            if (IS_DEBUG) {
                Log.d("OSU-OsuEngine", "Trigger OSU Failed");
            }
            return secOsuStatusCodeSendApdu;
        } catch (Exception e) {
            e.printStackTrace();
            return SecOsuStatusCode.OSU_STATUS_UNKNOWN;
        }
    }

    private SecOsuStatusCode triggerInitialSteps() {
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
        try {
            if (OsuOmapiHelper.getOsVersionId() < 48) {
                SecOsuStatusCode secOsuStatusCodeDoReset = doReset();
                SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_SUCCESS;
                if (secOsuStatusCodeDoReset != secOsuStatusCode2) {
                    return secOsuStatusCodeDoReset;
                }
                byte[] bArrCheckAndwrapPacket = {79, 112, -128, 19, 4, 74, 85, 65, 73};
                if (this.mIsWrapRequired) {
                    bArrCheckAndwrapPacket = OsuOmapiHelper.checkAndwrapPacket(bArrCheckAndwrapPacket);
                }
                OSULogger.write("INIT", true);
                SecOsuStatusCode secOsuStatusCodeSendApdu = sendApdu(bArrCheckAndwrapPacket);
                if (secOsuStatusCodeSendApdu != secOsuStatusCode2) {
                    Log.e("OSU-OsuEngine", "Trigger UAI Failed");
                    return secOsuStatusCodeSendApdu;
                }
            }
            SecOsuStatusCode secOsuStatusCodeSendCCIPackets = sendCCIPackets();
            return secOsuStatusCodeSendCCIPackets != SecOsuStatusCode.OSU_STATUS_SUCCESS ? secOsuStatusCodeSendCCIPackets : sendJCIPackets();
        } catch (Exception e) {
            e.printStackTrace();
            return SecOsuStatusCode.OSU_STATUS_UNKNOWN;
        }
    }

    private boolean doIARSpecificOperation() {
        byte[] bArrOpenChannel;
        if (this.mChannel == null) {
            Log.e("OSU-OsuEngine", "Channel is  not Initilized");
            return false;
        }
        IAROperation iAROperation = IAROperation.getInstance();
        if (!iAROperation.isAuthCallBackSet()) {
            return true;
        }
        try {
            this.mChannel.close();
            if (!iAROperation.doPreCondition()) {
                Log.e("OSU-OsuEngine", "IAR specific precondition failed");
                return false;
            }
            if (this.mChannel.open() == null) {
                Log.e("OSU-OsuEngine", "Not able to Access (Channel open Failed) ");
                return false;
            }
            if (OsuOmapiHelper.getOsVersionId() >= 48) {
                bArrOpenChannel = this.mChannel.openChannel(OsuOmapiHelper.OSU_AID_SN3XX);
                this.mIsWrapRequired = false;
            } else {
                if (!this.mIsWrapRequired) {
                    return true;
                }
                bArrOpenChannel = this.mChannel.openChannel(OsuOmapiHelper.OSUTrigger);
            }
            if (bArrOpenChannel != null && validateResponse(bArrOpenChannel, SUCCESS_RESP)) {
                return true;
            }
            Log.e("OSU-OsuEngine", "Not able to Open Channel");
            return false;
        } catch (Exception e) {
            Log.e("OSU-OsuEngine", e.getMessage());
            return false;
        }
    }

    private SecOsuStatusCode handleTriggerOSU() {
        Log.d("OSU-OsuEngine", "handleTriggerOSU Entry");
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
        if (this.mUAIInfo.UOS_ID == 90 && !isSelfUpdate()) {
            IOsuAsnHelper iOsuAsnHelper = this.mAsnHelper;
            UAIQueryParser uAIQueryParser = this.mUAIInfo;
            if (iOsuAsnHelper.getMatchingAction(1, uAIQueryParser.RSN, uAIQueryParser.CSN) >= 0) {
                if (!doIARSpecificOperation()) {
                    Log.e("OSU-OsuEngine", "IAR specific precondition failed.");
                    return SecOsuStatusCode.OSU_STATUS_FAILED;
                }
                SecOsuStatusCode secOsuStatusCodeTriggerOSU = triggerOSU();
                if (secOsuStatusCodeTriggerOSU != secOsuStatusCode) {
                    return secOsuStatusCodeTriggerOSU;
                }
                SecOsuStatusCode secOsuStatusCodeReset = reset();
                return secOsuStatusCodeReset != secOsuStatusCode ? secOsuStatusCodeReset : performUAIQuery();
            }
        }
        return secOsuStatusCode;
    }

    protected SecOsuStatusCode reset() {
        Log.d("OSU-OsuEngine", "reset Entry");
        try {
            return doReset();
        } catch (IOException unused) {
            return SecOsuStatusCode.OSU_STATUS_SUCCESS;
        }
    }

    private SecOsuStatusCode performUAIQuery() {
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_GET_DATA_FAILED;
        try {
            UAIQueryParser uAIQueryParserQueryUAIInfo = queryUAIInfo();
            this.mUAIInfo = uAIQueryParserQueryUAIInfo;
            if (uAIQueryParserQueryUAIInfo == null) {
                Log.e("OSU-OsuEngine", "Not able to Query UAI");
                return secOsuStatusCode;
            }
            return SecOsuStatusCode.OSU_STATUS_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_UNKNOWN;
            Log.e("OSU-OsuEngine", "Exception while UAI Query or Reset");
            return secOsuStatusCode2;
        }
    }

    private SecOsuStatusCode finishUpdate() {
        try {
            return verifyStatus() ? SecOsuStatusCode.OSU_STATUS_SUCCESS : SecOsuStatusCode.OSU_STATUS_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            return SecOsuStatusCode.OSU_STATUS_UNKNOWN;
        }
    }

    private SecOsuStatusCode triggerAMDHBackup(int i, boolean z) {
        boolean z2 = IS_DEBUG;
        if (z2) {
            Log.d("OSU-OsuEngine", "Triggering AmdH Backup at Index : " + i);
        }
        try {
            try {
                List apdus = this.mAsnHelper.getApdus(i);
                OSULogger.write("eUICC Backup", true);
                UAIQueryParser uAIQueryParser = this.mUAIInfo;
                if (uAIQueryParser.isHostResumeSupported && isAMDHBackupInterrupted(uAIQueryParser.amdHState)) {
                    if (z2) {
                        Log.d("OSU-OsuEngine", " AmdH Backup Interrupted ");
                    }
                    SecOsuStatusCode secOsuStatusCodeResumeAMDH = resumeAMDH(i);
                    try {
                        doReset();
                    } catch (IOException unused) {
                    }
                    return secOsuStatusCodeResumeAMDH;
                }
                Iterator it = apdus.iterator();
                while (it.hasNext()) {
                    byte[] byteArray = this.mAsnHelper.toByteArray((String) it.next());
                    if (!checkAndSkipUiccSwitch(byteArray, z)) {
                        boolean z3 = IS_DEBUG;
                        if (z3) {
                            Log.d("OSU-OsuEngine", "Sending : " + HexString.fromByteArray(byteArray, 0));
                        }
                        if (this.mIsShuttingDown) {
                            SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
                            try {
                                doReset();
                            } catch (IOException unused2) {
                            }
                            return secOsuStatusCode;
                        }
                        byte[] bArrSend = this.mChannel.send(byteArray);
                        if (this.mIsShuttingDown) {
                            SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
                            try {
                                doReset();
                            } catch (IOException unused3) {
                            }
                            return secOsuStatusCode2;
                        }
                        if (z3 && bArrSend != null) {
                            Log.d("OSU-OsuEngine", "Response : " + HexString.fromByteArray(bArrSend, 0));
                        }
                        OSULogger.write(bArrSend, false);
                        if (validateResponse(bArrSend, new byte[]{105, 0})) {
                            if (z3) {
                                Log.d("OSU-OsuEngine", "Save already Done skipping save");
                            }
                            SecOsuStatusCode secOsuStatusCode3 = SecOsuStatusCode.OSU_STATUS_SUCCESS;
                            try {
                                doReset();
                            } catch (IOException unused4) {
                            }
                            return secOsuStatusCode3;
                        }
                        if (!validateResponse(bArrSend, new byte[]{-112, 0})) {
                            SecOsuStatusCode secOsuStatusCode4 = SecOsuStatusCode.OSU_STATUS_FAILED;
                            try {
                                doReset();
                            } catch (IOException unused5) {
                            }
                            return secOsuStatusCode4;
                        }
                    }
                }
                try {
                    doReset();
                } catch (IOException unused6) {
                }
                return SecOsuStatusCode.OSU_STATUS_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                SecOsuStatusCode secOsuStatusCode5 = SecOsuStatusCode.OSU_STATUS_UNKNOWN;
                try {
                    doReset();
                } catch (IOException unused7) {
                }
                return secOsuStatusCode5;
            }
        } catch (Throwable th) {
            try {
                doReset();
            } catch (IOException unused8) {
            }
            throw th;
        }
    }

    private SecOsuStatusCode triggerAMDHRestore() {
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
        IOsuAsnHelper iOsuAsnHelper = this.mAsnHelper;
        UAIQueryParser uAIQueryParser = this.mUAIInfo;
        int matchingAction = iOsuAsnHelper.getMatchingAction(uAIQueryParser.UOS_ID, uAIQueryParser.PSN, uAIQueryParser.CSN);
        if (matchingAction < 0) {
            return SecOsuStatusCode.OSU_STATUS_FAILED;
        }
        if (IS_DEBUG) {
            Log.d("OSU-OsuEngine", "Triggering AmdH Restore at Index " + matchingAction);
        }
        if (isSelfUpdate()) {
            matchingAction++;
        }
        SecOsuStatusCode secOsuStatusCodeReset = reset();
        SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_SUCCESS;
        if (secOsuStatusCodeReset != secOsuStatusCode2) {
            return secOsuStatusCodeReset;
        }
        SecOsuStatusCode secOsuStatusCodeTriggerStep = triggerStep(matchingAction);
        if (secOsuStatusCodeTriggerStep != secOsuStatusCode2) {
            return secOsuStatusCodeTriggerStep;
        }
        SecOsuStatusCode secOsuStatusCodePerformUAIQuery = performUAIQuery();
        if (secOsuStatusCodePerformUAIQuery != secOsuStatusCode2) {
            Log.e("OSU-OsuEngine", "Not able to Query UAI");
        }
        return secOsuStatusCodePerformUAIQuery;
    }

    private boolean checkAndSkipUiccSwitch(byte[] bArr, boolean z) {
        return !this.mUAIInfo.isHostResumeSupported && z && (((bArr[1] & TransitionInfo.INIT) == 234 && (bArr[2] & TransitionInfo.INIT) == 18) || Arrays.equals(bArr, ISD_CMD));
    }

    private SecOsuStatusCode resumeAMDH(int i) {
        SecOsuStatusCode secOsuStatusCodeSendApdu;
        int amdhResumeIdx = this.mAsnHelper.getAmdhResumeIdx(i);
        List apdus = this.mAsnHelper.getApdus(i);
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
        for (int i2 = 0; i2 < 3; i2++) {
            byte[] byteArray = this.mAsnHelper.toByteArray((String) apdus.get(i2));
            byte b = byteArray[1];
            if (((b == -22 && byteArray[2] == 18) || (b == -92 && byteArray[2] == 4)) && (secOsuStatusCodeSendApdu = sendApdu(byteArray)) != SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                return secOsuStatusCodeSendApdu;
            }
        }
        return sendApdu(this.mAsnHelper.toByteArray((String) apdus.get(amdhResumeIdx)));
    }

    private SecOsuStatusCode sendApdu(byte[] bArr) {
        if (this.mIsShuttingDown) {
            return SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
        }
        boolean zSendAndValidate = sendAndValidate(bArr);
        if (this.mIsShuttingDown) {
            return SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
        }
        if (zSendAndValidate) {
            return SecOsuStatusCode.OSU_STATUS_SUCCESS;
        }
        return SecOsuStatusCode.OSU_STATUS_FAILED;
    }

    private SecOsuStatusCode sendAndGetStatus(byte[] bArr) {
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_FAILED;
        if (this.mIsShuttingDown) {
            return SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
        }
        IAccess iAccess = this.mChannel;
        if (iAccess != null) {
            byte[] bArrSend = iAccess.send(bArr);
            if (this.mIsShuttingDown) {
                return SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
            }
            OSULogger.write(bArrSend, false);
            if (validateResponse(bArrSend, SUCCESS_RESP)) {
                secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
            } else {
                constructCmdRespErrorBuffer(bArr, bArrSend);
            }
            secOsuStatusCode.setResponseCode(bArrSend);
        }
        return secOsuStatusCode;
    }

    private SecOsuStatusCode doReset() {
        if (this.mIsShuttingDown) {
            return SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
        }
        if (this.mChannel == null) {
            return SecOsuStatusCode.OSU_STATUS_FAILED;
        }
        if (OsuOmapiHelper.getOsVersionId() >= 48) {
            if (sIsResetRequired) {
                Log.d("OSU-OsuEngine", "Performing GP-APDU based reset ");
                IAccess iAccess = this.mChannel;
                byte[] bArr = OsuOmapiHelper.RESET_APDU;
                byte[] bArrSend = iAccess.send(bArr);
                if (!validateResponse(bArrSend, SUCCESS_RESP)) {
                    constructCmdRespErrorBuffer(bArr, bArrSend);
                    return SecOsuStatusCode.OSU_STATUS_FAILED;
                }
                try {
                    Thread.sleep(10L);
                } catch (Exception e) {
                    Log.d("OSU-OsuEngine", "sleep interrupted " + e);
                }
                sIsResetRequired = false;
            } else {
                Log.d("OSU-OsuEngine", "Skipping reset After this Step ......... ");
            }
        } else {
            Log.d("OSU-OsuEngine", "Performing reset ");
            this.mChannel.reset();
        }
        if (this.mIsShuttingDown) {
            return SecOsuStatusCode.OSU_STATUS_SHUTTING_DOWN;
        }
        return SecOsuStatusCode.OSU_STATUS_SUCCESS;
    }

    protected boolean sendAndValidate(byte[] bArr) {
        byte[] bArrSend = this.mChannel.send(bArr);
        OSULogger.write(bArrSend, false);
        boolean zValidateResponse = validateResponse(bArrSend, SUCCESS_RESP);
        if (!zValidateResponse) {
            constructCmdRespErrorBuffer(bArr, bArrSend);
        }
        return zValidateResponse;
    }

    public static boolean validateResponse(byte[] bArr, byte[] bArr2) {
        if (bArr != null && bArr.length >= 2 && bArr2 != null && bArr2.length == 2) {
            return bArr2[1] == bArr[bArr.length - 1] && bArr2[0] == bArr[bArr.length - 2];
        }
        Log.e("OSU-OsuEngine", "Invalid Response to Check");
        return false;
    }

    protected UAIQueryParser queryUAIInfo() {
        IAccess iAccess = this.mChannel;
        if (iAccess == null) {
            Log.e("OSU-OsuEngine", "queryUAIInfo : Wrong state ");
            return null;
        }
        byte[] bArr = {-128, -54, 0, -2, 2, -33, 67};
        byte[] bArrSend = iAccess.send(bArr);
        if (bArrSend == null) {
            Log.e("OSU-OsuEngine", "Failed to get UAIquery response");
            return null;
        }
        byte[] bArr2 = SUCCESS_RESP;
        if (!validateResponse(bArrSend, bArr2)) {
            Log.e("OSU-OsuEngine", "UAIquery response code not success ");
            if (OsuOmapiHelper.getOsVersionId() < 48) {
                constructCmdRespErrorBuffer(bArr, bArrSend);
                return null;
            }
            this.mChannel.close();
            if (!validateResponse(this.mChannel.open(), bArr2)) {
                Log.e("OSU-OsuEngine", "Not able to Open Session");
                return null;
            }
            OsuOmapiHelper.parseATR(this.mChannel.getAtr());
            if (!validateResponse(this.mChannel.openChannel(OsuOmapiHelper.OSU_AID_SN3XX), bArr2)) {
                Log.e("OSU-OsuEngine", "Not able to Open-Channel Again");
                return null;
            }
            bArrSend = this.mChannel.send(bArr);
            if (bArrSend == null) {
                Log.e("OSU-OsuEngine", "Failed to get UAIquery response after reselection");
                return null;
            }
            if (!validateResponse(bArrSend, bArr2)) {
                constructCmdRespErrorBuffer(bArr, bArrSend);
                return null;
            }
        }
        return UAIQueryParser.parse(bArrSend);
    }

    /* JADX WARN: Code duplicated, block: B:25:0x005e A[PHI: r3
  0x005e: PHI (r3v2 byte[]) = (r3v1 byte[]), (r3v1 byte[]), (r3v3 byte[]) binds: [B:17:0x003c, B:19:0x0047, B:23:0x005a] A[DONT_GENERATE, DONT_INLINE]] */
    public SecOsuStatusCode performGPUAITrigger() {
        SecOsuStatusCode secOsuStatusCode;
        if (OsuOmapiHelper.getOsVersionId() < 48) {
            return SecOsuStatusCode.OSU_STATUS_SUCCESS;
        }
        SecOsuStatusCode secOsuStatusCode2 = SecOsuStatusCode.OSU_STATUS_FAILED;
        if (this.mChannel == null) {
            Log.e("OSU-OsuEngine", " performGPUAITrigger : Wrong state ");
            return secOsuStatusCode2;
        }
        try {
            boolean z = IS_DEBUG;
            if (z) {
                Log.d("OSU-OsuEngine", "perform GP UAI Trigger");
            }
            IAccess iAccess = this.mChannel;
            byte[] bArr = OsuOmapiHelper.GP_UAITrigger;
            byte[] bArrSend = iAccess.send(bArr);
            byte[] bArr2 = SUCCESS_RESP;
            if (validateResponse(bArrSend, bArr2)) {
                secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
            } else if (validateResponse(bArrSend, OS_MUTE_STATUS_RESP)) {
                sIsResetRequired = true;
                SecOsuStatusCode secOsuStatusCodeDoReset = doReset();
                SecOsuStatusCode secOsuStatusCode3 = SecOsuStatusCode.OSU_STATUS_SUCCESS;
                if (secOsuStatusCodeDoReset != secOsuStatusCode3) {
                    secOsuStatusCode = secOsuStatusCode2;
                } else {
                    if (z) {
                        Log.d("OSU-OsuEngine", "Retry GP-UAI Trigger");
                    }
                    bArrSend = this.mChannel.send(bArr);
                    if (validateResponse(bArrSend, bArr2)) {
                        secOsuStatusCode = secOsuStatusCode3;
                    } else {
                        secOsuStatusCode = secOsuStatusCode2;
                    }
                }
            } else {
                secOsuStatusCode = secOsuStatusCode2;
            }
            if (secOsuStatusCode == secOsuStatusCode2) {
                constructCmdRespErrorBuffer(bArr, bArrSend);
            }
            return secOsuStatusCode;
        } catch (Exception e) {
            e.printStackTrace();
            return SecOsuStatusCode.OSU_STATUS_UNKNOWN;
        }
    }

    private SecOsuStatusCode setGpResetRequiredFromResponse(byte[] bArr, boolean z) {
        if (OsuOmapiHelper.getOsVersionId() < 48) {
            return SecOsuStatusCode.OSU_STATUS_SUCCESS;
        }
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_FAILED;
        if (bArr != null && validateResponse(bArr, OUT_OF_MEMORY_GP_SUPPORT)) {
            sIsResetRequired = true;
            secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_NO_MEM;
        } else if (bArr != null && validateResponse(bArr, GP_OSU_COMMUNICATION_ERROR)) {
            sIsResetRequired = true;
        }
        if (z && sIsResetRequired) {
            try {
                if (doReset() != SecOsuStatusCode.OSU_STATUS_SUCCESS) {
                    Log.e("OSU-OsuEngine", "Failed to perform reset");
                    return secOsuStatusCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return secOsuStatusCode;
    }

    private SecOsuStatusCode handleGPMultiPatchUpdate(int i) {
        boolean z;
        SecOsuStatusCode secOsuStatusCode = SecOsuStatusCode.OSU_STATUS_SUCCESS;
        if (isPatchUpdate() && OsuOmapiHelper.getOsVersionId() >= 48) {
            Log.d("OSU-OsuEngine", "handle GP MultiPatch Update");
            IOsuAsnHelper iOsuAsnHelper = this.mAsnHelper;
            UAIQueryParser uAIQueryParser = this.mUAIInfo;
            if (iOsuAsnHelper.getMatchingAction(uAIQueryParser.UOS_ID, uAIQueryParser.RSN, uAIQueryParser.CSN) < 0) {
                Log.d("OSU-OsuEngine", "Done with Last Patch");
                z = true;
            } else {
                z = false;
            }
            try {
                boolean zIsResetRequiredAfterAction = this.mAsnHelper.isResetRequiredAfterAction(i);
                if ((!IAROperation.getInstance().isAuthCallBackSet() || z) && zIsResetRequiredAfterAction) {
                    Log.d("OSU-OsuEngine", "re-open channel for next patch");
                    this.mChannel.close();
                    byte[] bArrOpen = this.mChannel.open();
                    if (bArrOpen != null) {
                        bArrOpen = this.mChannel.openChannel(OsuOmapiHelper.OSU_AID_SN3XX);
                    }
                    if (bArrOpen == null || !validateResponse(bArrOpen, SUCCESS_RESP)) {
                        Log.e("OSU-OsuEngine", "Not able to Open Channel");
                        return SecOsuStatusCode.OSU_STATUS_FAILED;
                    }
                } else if (!doIARSpecificOperation()) {
                    Log.e("OSU-OsuEngine", "IAR specific precondition failed.");
                    return SecOsuStatusCode.OSU_STATUS_FAILED;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return SecOsuStatusCode.OSU_STATUS_UNKNOWN;
            }
        }
        return secOsuStatusCode;
    }

    private void constructCmdRespErrorBuffer(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length < 2) {
            Log.e("OSU-OsuEngine", "Invalid command response buffer");
            return;
        }
        byte[] bArr3 = new byte[10];
        sLastErrorCmdRespBuffer = bArr3;
        if (bArr.length > 8) {
            System.arraycopy(bArr, 0, bArr3, 0, 4);
            System.arraycopy(bArr, bArr.length - 4, sLastErrorCmdRespBuffer, 4, 4);
        } else {
            System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        }
        System.arraycopy(bArr2, bArr2.length - 2, sLastErrorCmdRespBuffer, 8, 2);
    }
}
