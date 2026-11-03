package com.android.phone;

import android.os.AsyncResult;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.CellIdentityNr;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.module.INetworkModule;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.NrSceneConstants$Scene;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MiuiSynergySdk;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;
import miui.process.ProcessManager;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public class MtkFiveGPowerController extends FiveGPowerController {
    private static final String[] DISABLE_LIMIT_NW_CA;
    private static final String[] ENABLE_LIMIT_NW_CA;
    private static final String[] ENABLE_LIMIT_NW_CA_NSA_SCG_POOR;
    private static final String[] REGISTER_NR_BG_SEARCH_ICD_CMD;
    private static final String[] UNREGISTER_NR_BG_SEARCH_ICD_CMD;
    private static boolean mIsNewNwCaCommand;
    private boolean is4Rx;
    private int m4RXTo2RXNum;
    private boolean mLimitCaForNsaScgPoor;
    private int mModemEndcStatus;
    private static final String[] ENABLE_ENDC = {"AT+EGMC=1,\"endc_deactivation\",0,1", "+EGMC:"};
    private static final String[] DISABLE_ENDC = {"AT+EGMC=1,\"endc_deactivation\",1,1", "+EGMC:"};
    private static final String[] ENABLE_SA = {"AT+EGMC=1,\"sa_silence\",0", ""};
    private static final String[] DISABLE_SA = {"AT+EGMC=1,\"sa_silence\",1", ""};
    private static final String[] ENABLE_LTE_PREFER = {"AT+ESBP=5,\"SBP_LTE_PREFER\",1,1", ""};
    private static final String[] DISABLE_LTE = {"AT+ESBP=5,\"SBP_LTE_PREFER\",0,1", ""};
    private static final String[] ENABLE_4G5 = {"AT+ESBP=5,\"SBP_BG_SEARCH_4G5\",1,1", ""};
    private static final String[] DISABLE_4G5 = {"AT+ESBP=5,\"SBP_BG_SEARCH_4G5\",0,1", ""};
    private static final String[] ENABLE_3G5_FDD = {"AT+ESBP=5,\"SBP_BG_SEARCH_3G5_FDD\",1,1", ""};
    private static final String[] DISABLE_3G5_FDD = {"AT+ESBP=5,\"SBP_BG_SEARCH_3G5_FDD\",0,1", ""};
    private static final String[] ENABLE_3G5_TDD = {"AT+ESBP=5,\"SBP_BG_SEARCH_3G5_TDD\",0,1", ""};
    private static final String[] DISABLE_3G5_TDD = {"AT+ESBP=5,\"SBP_BG_SEARCH_3G5_TDD\",1,1", ""};
    private static final String[] ENABLE_2G5_FDD = {"AT+ESBP=5,\"SBP_BG_SEARCH_2G5_FDD\",0,1", ""};
    private static final String[] DISABLE_2G5_FDD = {"AT+ESBP=5,\"SBP_BG_SEARCH_2G5_FDD\",1,1", ""};
    private static final String[] ENABLE_2G5_TDD = {"AT+ESBP=5,\"SBP_BG_SEARCH_2G5_TDD\",0,1", ""};
    private static final String[] DISABLE_2G5_TDD = {"AT+ESBP=5,\"SBP_BG_SEARCH_2G5_TDD\",1,1", ""};
    private static final String[] ENABLE_SA_CAP = {"AT+E5GOPT=7", ""};
    private static final String[] DISABLE_SA_CAP = {"AT+E5GOPT=5", ""};
    private static final String[] ENABLE_DAT_LANDSCAPE_MODE_USB_RIGHT = {"AT+ERFIDX=0,0", ""};
    private static final String[] ENABLE_DAT_LANDSCAPE_MODE_USB_LEFT = {"AT+ERFIDX=0,1", ""};
    private static final String[] DISABLE_DAT_LANDSCAPE_MODE = {"AT+ERFIDX=0,-1", ""};
    private static final ArrayList fakeStationLists = new ArrayList();
    public static int[] mNsaScgPoorThreshold = {MiuiSynergySdk.SEND_APP_FAIL_REMOTE_BUSY_WORLD, 8, -6};
    private static final String[] REGISTER_NSA_SCG_POOR_ICD_CMD = {"AT+EDMFAPP=20107,100,1,0,0,0,0", "+EDMFAPP"};

    static {
        boolean z = MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported() || MiuiPhoneUtils.isSupportFivegaOptimization();
        mIsNewNwCaCommand = z;
        ENABLE_LIMIT_NW_CA = z ? new String[]{"MIPC_SET_THERMAL_ACTUATOR", "3", "273"} : new String[]{"MIPC_SET_THERMAL_ACTUATOR", "3", "257"};
        ENABLE_LIMIT_NW_CA_NSA_SCG_POOR = MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported() ? new String[]{"MIPC_SET_THERMAL_ACTUATOR", "3", "16776993"} : new String[]{"MIPC_SET_THERMAL_ACTUATOR", "3", "16776962"};
        DISABLE_LIMIT_NW_CA = new String[]{"MIPC_SET_THERMAL_ACTUATOR", "3", "16777215"};
        REGISTER_NR_BG_SEARCH_ICD_CMD = new String[]{"AT+EDMFAPP=20107,108,1,0,0,0,0", "+EDMFAPP"};
        UNREGISTER_NR_BG_SEARCH_ICD_CMD = new String[]{"AT+EDMFAPP=20107,108,0,0,0,0,0", "+EDMFAPP"};
    }

    protected MtkFiveGPowerController(Looper looper) {
        super(looper);
        this.is4Rx = false;
        this.m4RXTo2RXNum = 0;
        this.mModemEndcStatus = -1;
        post(new Runnable() { // from class: com.android.phone.MtkFiveGPowerController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mIsNsaScgPoorOptimizationSupported || MiuiPhoneUtils.isFakeSaStationDetectSupported()) {
            regUnRegisterOemAtUrcInd(true);
        }
    }

    @Override // com.android.phone.FiveGPowerController
    public void reportStatistics() {
        HashMap map = new HashMap();
        map.put("4RX_to_2RX_num", Integer.valueOf(this.m4RXTo2RXNum));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "thermal_optomize_stastic", map);
        clearThermalOptimizeStatistics();
    }

    public void clearThermalOptimizeStatistics() {
        FiveGPowerController.log("clearThermalOptimizeStatistics");
        this.m4RXTo2RXNum = 0;
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void bindServiceAndEnableEndc(boolean z) {
        bindServiceAndEnableEndc(z, DefaultSimManager.getDefaultDataSlotId());
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void bindServiceAndEnableEndc(boolean z, int i) {
        try {
            this.mModemEndcStatus = z ? 1 : 0;
            if (z) {
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    Phone phone = MiuiPhoneUtils.getPhone(i2);
                    if (phone != null) {
                        FiveGPowerController.log("bindServiceAndEnableEndc enableEndc: " + z + ", phoneId = " + i2);
                        sendAtCommand(phone, ENABLE_ENDC);
                        this.mEndcPowerEnabled[i2] = z;
                    }
                }
            } else {
                Phone phone2 = MiuiPhoneUtils.getPhone(i);
                if (phone2 != null) {
                    FiveGPowerController.log("bindServiceAndEnableEndc enableEndc: " + z + ", phoneId = " + i);
                    sendAtCommand(phone2, DISABLE_ENDC);
                    this.mEndcPowerEnabled[i] = z;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void enableSaAsync(boolean z, int i, NrSceneConstants$Scene nrSceneConstants$Scene) {
        if (this.mInCall && !MiuiImsPhoneUtils.isWifiCallingEnabled()) {
            FiveGPowerController.log("enableSaAsync return because inCall");
            return;
        }
        NrSceneConstants$Scene nrSceneConstants$Scene2 = this.mLastOptScene[i];
        if (nrSceneConstants$Scene2 != null && nrSceneConstants$Scene2 != NrSceneConstants$Scene.UNKNOWN && nrSceneConstants$Scene2 != nrSceneConstants$Scene) {
            int[] iArr = this.mSwitchSceneCount;
            iArr[i] = iArr[i] + 1;
        }
        if (z) {
            this.mIsSilentSaEndcDisabled = false;
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                deprioritizeSaAsync(z, i2);
                this.mSaPowerEnabled[i2] = z;
            }
            this.mFiveGOptimizeScene = NrSceneConstants$Scene.UNKNOWN;
        } else {
            if (FiveGManagerBase.getInstance().getFiveGNetworkMode() == 2) {
                this.mIsSilentSaEndcDisabled = true;
            }
            deprioritizeSaAsync(z, i);
            this.mSaPowerEnabled[i] = z;
            this.mFiveGOptimizeScene = nrSceneConstants$Scene;
        }
        this.mLastOptScene[i] = nrSceneConstants$Scene;
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void enableSaAsync(boolean z, NrSceneConstants$Scene nrSceneConstants$Scene) {
        enableSaAsync(z, DefaultSimManager.getDefaultDataSlotId(), nrSceneConstants$Scene);
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void deprioritizeSaAsync(boolean z, int i) {
        if (this.mInCall && !MiuiImsPhoneUtils.isWifiCallingEnabled()) {
            FiveGPowerController.log("deprioritizeSaAsync return because inCall");
            return;
        }
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return;
        }
        if (MiuiPhoneUtils.isDeprioritizeSaNotSupported()) {
            if (MiuiPhoneUtils.isCloudCloseWlanDisableSaFeature()) {
                z = true;
            }
            if (this.mSaPowerEnabled[i] != z) {
                FiveGManagerBase.getInstance().sendAtCommand(i, z ? ENABLE_SA_CAP : DISABLE_SA_CAP, (Message) null);
                FiveGPowerController.log("enableSaAsync enableSa: " + z + " phone" + i);
            }
        } else {
            sendAtCommand(phone, z ? ENABLE_SA : DISABLE_SA);
            sendAtCommand(phone, z ? ENABLE_ENDC : DISABLE_ENDC);
            sendAtCommand(phone, z ? DISABLE_LTE : ENABLE_LTE_PREFER);
            if (!HyperOSCustFeatureResolve.getBoolean("support_close_4G5_search", false)) {
                sendAtCommand(phone, z ? ENABLE_4G5 : DISABLE_4G5);
            } else {
                FiveGPowerController.log("deprioritizeSaAsync skip 4G5");
            }
            sendAtCommand(phone, z ? ENABLE_3G5_FDD : DISABLE_3G5_FDD);
            sendAtCommand(phone, z ? ENABLE_3G5_TDD : DISABLE_3G5_TDD);
            sendAtCommand(phone, z ? ENABLE_2G5_FDD : DISABLE_2G5_FDD);
            sendAtCommand(phone, z ? ENABLE_2G5_TDD : DISABLE_2G5_TDD);
            SystemProperties.set("ril.deprioritize.sa.slot" + i, Boolean.toString(z));
            localLog("deprioritizeSaAsync", "Phone[" + i + "]: enabled = " + z);
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void switchRxMode(boolean z) {
        FiveGPowerController.log("switchRxMode changed = " + z);
        String[] strArr = {"AT+EGMC = 1, \"nr_rx_path\", 1, 1, 3, 3, 3, 3", "+EGMC"};
        String[] strArr2 = {"AT+EGMC = 1, \"nr_rx_path\", 0", "+EGMC"};
        if (!z) {
            strArr = strArr2;
        }
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            return;
        }
        sendAtCommand(activeDataPhone, strArr);
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void updateThrottleLevel(int i) {
        String[] strArr;
        String[] strArr2;
        String str;
        try {
            FiveGPowerController.log("updateThrottleLevel throttleLevel = " + i);
            if (MiuiPhoneUtils.isUseMipcForThrottleLevel()) {
                if (i == 1) {
                    str = "0";
                } else if (i == 4) {
                    str = "2";
                } else {
                    str = "1";
                }
                FiveGPowerController.log("updateThrottleLevel rx rank = ".concat(str));
                if (str.equals("1")) {
                    this.m4RXTo2RXNum++;
                }
                strArr = new String[]{"MIPC_SET_FAKE_RI", "1", "0", "2", str};
                strArr2 = new String[]{"MIPC_SET_FAKE_RI", "0", "0", "2", "0"};
            } else {
                strArr = new String[]{"AT+EGMC = 1, \"nr_rx_path\", 1, 1, 3, 3, 3, 3", "+EGMC"};
                strArr2 = new String[]{"AT+EGMC = 1, \"nr_rx_path\", 0", "+EGMC"};
            }
            int intPreference = PreferenceUtils.getInstance().getIntPreference("ThrottleLevelSwitch", -1);
            FiveGPowerController.log("updateThrottleLevel:  throttleLevelStatus=" + intPreference);
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone == null) {
                return;
            }
            if (i == 0) {
                if (intPreference != 0 || !this.is4Rx) {
                    sendAtCommand(activeDataPhone, strArr2);
                    this.is4Rx = true;
                } else {
                    FiveGPowerController.log("cloud control throttle level switch is closed, is4Rx=" + this.is4Rx);
                }
            } else if (intPreference != 0) {
                sendAtCommand(activeDataPhone, strArr);
                this.is4Rx = false;
            } else {
                FiveGPowerController.log("cloud control throttle level switch  is closed");
            }
            ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
            String str2 = "";
            if (foregroundInfo != null) {
                str2 = foregroundInfo.mForegroundPackageName;
            }
            FiveGPowerController.sThermalThrottleFGAppMap.put(str2, Integer.valueOf(FiveGPowerController.sThermalThrottleFGAppMap.containsKey(str2) ? ((Integer) FiveGPowerController.sThermalThrottleFGAppMap.get(str2)).intValue() : 1));
            FiveGPowerController.sThermalThrottleSASnrMap.put(Integer.valueOf(this.mSASnr), Integer.valueOf(FiveGPowerController.sThermalThrottleSASnrMap.containsKey(Integer.valueOf(this.mSASnr)) ? ((Integer) FiveGPowerController.sThermalThrottleSASnrMap.get(Integer.valueOf(this.mSASnr))).intValue() : 1));
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void updateULThrottleState(int i) {
        if (i >= 0 && i <= 8) {
            if (this.mULThrottleState != i) {
                FiveGPowerController.log("updateULThrottleState throttleState = " + i);
                String[] strArr = {"MIPC_SET_THERMAL_ACTUATOR", "1", String.valueOf(i)};
                Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
                if (activeDataPhone == null) {
                    return;
                }
                this.mULThrottleState = i;
                sendAtCommand(activeDataPhone, strArr);
                this.mULThrottleOptCount++;
            }
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void reduceMaxTxPower(int i) {
        if (i >= 0 && i <= 21) {
            if (this.mTxPower != i) {
                FiveGPowerController.log("reduceMaxTxPower txPower = " + i);
                String[] strArr = {"MIPC_SET_THERMAL_ACTUATOR", "9", String.valueOf(i)};
                Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
                if (activeDataPhone == null) {
                    return;
                }
                this.mTxPower = i;
                sendAtCommand(activeDataPhone, strArr);
                this.mMaxTxPowerOptCount++;
            }
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void execSmartIdle(int i) {
        if (i >= 0) {
            if (this.mSmartIdleTimer != i) {
                FiveGPowerController.log("execSmartIdle inactiveTimer = " + i);
                String[] strArr = {"AT+EGMC = 1, \"nrrc_data_inactive_timer\", 1, " + i, "+EGMC"};
                Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
                if (activeDataPhone == null) {
                    return;
                }
                this.mSmartIdleTimer = i;
                sendAtCommand(activeDataPhone, strArr);
                this.mSmartIdleOptCount++;
            }
        }
    }

    @Override // com.android.phone.FiveGPowerController
    public synchronized void reduceIdleNeighborMea(boolean z, int i) {
        if (this.mIdleNeiMea == z) {
            return;
        }
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            return;
        }
        FiveGPowerController.log("reduceIdleNeighborMea enable=" + z + " multiplier=" + i);
        String[] strArrBuildIdleNeiMeaCommand = buildIdleNeiMeaCommand(z, HyperOSCustFeatureResolve.getBoolean("support_measure_relax_mode3", false), String.valueOf(i));
        this.mIdleNeiMea = z;
        sendAtCommand(activeDataPhone, strArrBuildIdleNeiMeaCommand);
        this.mIdleNeiMeaOptCount = this.mIdleNeiMeaOptCount + 1;
    }

    private String[] buildIdleNeiMeaCommand(boolean z, boolean z2, String str) {
        if (!z) {
            return new String[]{"MIPC_SET_IDLE_SRV_MEAS_REQ", "0", "0", "0", "0", str};
        }
        if (z2) {
            return new String[]{"MIPC_SET_IDLE_SRV_MEAS_REQ", "3", "0", "0", "0", str};
        }
        return new String[]{"MIPC_SET_IDLE_SRV_MEAS_REQ", "2", "0", "0", "0", str};
    }

    public String[] constructionAtCmd(int i, int i2) {
        String[] strArr = {"", ""};
        strArr[0] = "AT+EDMFAPP=20107,107," + i + "," + i2 + ",0,0,0";
        strArr[1] = "+EDMFAPP";
        FiveGPowerController.log("constructionAtCmd atCmd is " + strArr[0] + ", " + strArr[1]);
        return strArr;
    }

    @Override // com.android.phone.FiveGPowerController
    public synchronized void limitNwCA(boolean z, int i) {
        String[] strArr;
        if (i == FiveGPowerController.LimitCaScene.NSA_SCG_POOR_MODE.value) {
            limitNwCAForNsaScgPoor(z);
            return;
        }
        if (i != FiveGPowerController.LimitCaScene.POWER_SAVE.value || this.mIsLimitCASupported) {
            int i2 = this.mLimitCaBitMask;
            if (z) {
                this.mLimitCaBitMask = i | i2;
            } else {
                this.mLimitCaBitMask = (~i) & i2;
            }
            if (this.mLimitCA == z) {
                return;
            }
            if (z || this.mLimitCaBitMask == 0) {
                Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
                if (activeDataPhone == null) {
                    return;
                }
                FiveGPowerController.log("limitNwCA enable = " + z);
                if (z) {
                    strArr = ENABLE_LIMIT_NW_CA;
                } else {
                    strArr = DISABLE_LIMIT_NW_CA;
                }
                this.mLimitCA = z;
                sendAtCommand(activeDataPhone, strArr);
                this.mNwCaLimitOptCount++;
            }
        }
    }

    private void limitNwCAForNsaScgPoor(boolean z) {
        Phone activeDataPhone;
        String[] strArr;
        if (this.mLimitCaForNsaScgPoor == z || !this.mPowerOptimizationEnabled) {
            return;
        }
        FiveGPowerController.log("mLimitCaBitMask = " + this.mLimitCaBitMask);
        if (this.mLimitCaBitMask == 0 && (activeDataPhone = MiuiPhoneUtils.getActiveDataPhone()) != null) {
            FiveGPowerController.log("limitNwCAForNsaScgPoor enable = " + z);
            if (z) {
                strArr = ENABLE_LIMIT_NW_CA_NSA_SCG_POOR;
                this.mNsaScgPoorOptimizationCounts++;
            } else {
                strArr = DISABLE_LIMIT_NW_CA;
            }
            this.mLimitCaForNsaScgPoor = z;
            sendAtCommand(activeDataPhone, strArr);
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected void resetLimitNwCA() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null) {
                sendAtCommand(phone, DISABLE_LIMIT_NW_CA);
                this.mLimitCA = false;
                this.mLimitCaForNsaScgPoor = false;
                this.mLimitCaBitMask = 0;
            }
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected boolean isResidentCell() {
        if (MiuiPhoneUtils.isSelfStudySupported()) {
            return ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isCurrentCellResident();
        }
        return false;
    }

    private void sendAtCommand(Phone phone, String[] strArr) {
        FiveGManagerBase.getInstance().sendAtCommand(phone.getPhoneId(), strArr, (Message) null);
    }

    private void sendAtCommands(Phone phone, String... strArr) {
        if (phone == null) {
            FiveGPowerController.log("phone is null ...");
            return;
        }
        for (String str : strArr) {
            sendAtCommand(phone, new String[]{str, ""});
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void setDatConfig(boolean z, int i) {
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        if (dataPhone == null) {
            FiveGPowerController.log("setDatConfig phone is null ...");
            return;
        }
        FiveGPowerController.log("setDatConfig enable dat landscape mode : " + z + " orientationType=" + i);
        if (i == 1) {
            sendAtCommand(dataPhone, z ? ENABLE_DAT_LANDSCAPE_MODE_USB_RIGHT : DISABLE_DAT_LANDSCAPE_MODE);
        } else {
            sendAtCommand(dataPhone, DISABLE_DAT_LANDSCAPE_MODE);
        }
    }

    private void regUnRegisterOemAtUrcInd(boolean z) {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                if (z) {
                    phone.registerForOemIndication(this, 54, phone);
                } else {
                    phone.unregisterForOemIndication(this);
                }
            }
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected void handleDmfUrcInd(Message message) {
        String str;
        try {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (((Phone) asyncResult.userObj).getPhoneId() != MiuiPhoneUtils.getActiveDataPhone().getPhoneId()) {
                return;
            } else {
                str = (String) asyncResult.result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            str = "";
        }
        if (TextUtils.isEmpty(str) || !str.startsWith("+EDMFAPP:")) {
            return;
        }
        try {
            String[] strArrSplit = str.split(",");
            if (strArrSplit.length < 2) {
                FiveGPowerController.log("handleDmfUrcInd invalid data length=" + strArrSplit.length);
            } else {
                String[] strArrSplit2 = strArrSplit[0].split(":");
                if (strArrSplit2.length < 2) {
                    FiveGPowerController.log("handleDmfUrcInd invalid atCmd format");
                } else {
                    int i = Integer.parseInt(strArrSplit2[1].trim());
                    FiveGPowerController.log("handleDmfUrcInd appId=" + i + " data=" + Arrays.toString(strArrSplit));
                    if (i == 20107) {
                        int i2 = Integer.parseInt(strArrSplit[1].trim());
                        if (i2 == 101) {
                            handleNsaScgPoorStatusInd(strArrSplit);
                        } else if (i2 == 106) {
                            handleFakeSaStatusInd(strArrSplit);
                        } else if (i2 == 108) {
                            handleBGSearchStatusInd(strArrSplit);
                        }
                    } else if (i == 20006 && Integer.parseInt(strArrSplit[1].trim()) == 402) {
                        handleRecFreezeChangeFromModem(strArrSplit);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e2) {
            FiveGPowerController.log("handleDmfUrcInd ArrayIndexOutOfBoundsException: " + e2);
        } catch (NumberFormatException e3) {
            FiveGPowerController.log("handleDmfUrcInd NumberFormatException: " + e3);
        }
    }

    private void handleRecFreezeChangeFromModem(String[] strArr) {
        if (strArr == null || strArr.length < 8) {
            StringBuilder sb = new StringBuilder("handleRecFreezeChangeFromModem invalid data length=");
            sb.append(strArr != null ? strArr.length : 0);
            FiveGPowerController.log(sb.toString());
            return;
        }
        try {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            Integer.parseInt(strArr[2].trim());
            int i = Integer.parseInt(strArr[3].trim());
            Integer.parseInt(strArr[5].trim());
            Integer.parseInt(strArr[6].trim());
            Integer.parseInt(strArr[7].trim());
            if (i == 1) {
                this.startDataStallTime = jElapsedRealtime;
                this.mIsRecFreeze = true;
                if (this.mIsNsaScgPoorStatusFlag) {
                    sendEmptyMessageDelayed(59, 10000L);
                    this.mNsaScgPoorFreezeRecoverCounts++;
                    return;
                }
                return;
            }
            if (i == 0) {
                long j = this.startDataStallTime;
                if (j != 0 && jElapsedRealtime - j < 10000 && hasMessages(59)) {
                    removeMessages(59);
                    this.mNsaScgPoorFreezeRecoverCounts--;
                }
                this.mIsRecFreeze = false;
            }
        } catch (NumberFormatException e) {
            FiveGPowerController.log("handleRecFreezeChangeFromModem NumberFormatException: " + e);
        }
    }

    private void handleFakeSaStatusInd(String[] strArr) {
        String[] strArr2;
        if (strArr == null || strArr.length < 4) {
            StringBuilder sb = new StringBuilder("handleFakeSaStatusInd invalid data length=");
            sb.append(strArr != null ? strArr.length : 0);
            FiveGPowerController.log(sb.toString());
            return;
        }
        try {
            FiveGPowerController.log("resp " + Arrays.toString(strArr));
            int i = Integer.parseInt(strArr[3].trim());
            if (i == 1) {
                strArr2 = new String[]{"subId", "version", "IssaFake", "mcc", "mnc", "dlarfcn", "pci", "tac1", "tac2", "tac3", "cid1", "cid2", "cid3"};
            } else if (i == 2) {
                strArr2 = new String[]{"subId", "version", "IssaFake", "mcc", "mnc", "dlarfcn", "pci", "cause1", "cause2", "cause3", "tac1", "tac2", "tac3", "cid1", "cid2", "cid3"};
            } else {
                FiveGPowerController.log("fakeSaStationInfo version is not supported");
                return;
            }
            if (strArr2.length > strArr.length - 2) {
                FiveGPowerController.log("fakeSaStationInfo data length is not equal to keyNames length");
                return;
            }
            HashMap map = new HashMap();
            for (int i2 = 0; i2 < strArr2.length; i2++) {
                map.put(strArr2[i2], strArr[i2 + 2]);
            }
            FiveGPowerController.log("fakeSaStationInfo=" + map.toString());
            if (this.isSupportFakeSaAction && !TelephonyCloudController.getCloudControllerFakeStationOptDisable()) {
                takeActionforFakesaStation(map);
            }
            MiStatInterfaceUtil.recordObjectEvent("modem_ue_statistic", "fakeStation", map);
        } catch (NumberFormatException e) {
            FiveGPowerController.log("handleFakeSaStatusInd NumberFormatException: " + e);
        }
    }

    private void takeActionforFakesaStation(HashMap map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        try {
            Object obj = map.get("subId");
            Object obj2 = map.get("mcc");
            Object obj3 = map.get("mnc");
            Object obj4 = map.get("dlarfcn");
            Object obj5 = map.get("pci");
            if (obj != null && obj2 != null && obj3 != null && obj4 != null && obj5 != null) {
                int i = Integer.parseInt((String) obj);
                FiveGFakeStation fiveGFakeStation = new FiveGFakeStation(Integer.parseInt((String) obj2), Integer.parseInt((String) obj3), Integer.parseInt((String) obj4), Integer.parseInt((String) obj5));
                prioritizeCell(i, true, fiveGFakeStation, String.valueOf(-40));
                ArrayList arrayList = fakeStationLists;
                arrayList.add(fiveGFakeStation);
                if (arrayList.size() > 10) {
                    FiveGPowerController.log("stationlist is over size");
                    prioritizeCell(i, false, (FiveGFakeStation) arrayList.get(0), "");
                    arrayList.remove(0);
                    return;
                }
                return;
            }
            FiveGPowerController.log("takeActionforFakesaStation missing required fields");
        } catch (Exception e) {
            FiveGPowerController.log("takeActionforFakesaStation error: " + e);
        }
    }

    private void prioritizeCell(int i, boolean z, FiveGFakeStation fiveGFakeStation, String str) {
        if (fiveGFakeStation == null) {
            return;
        }
        try {
            String str2 = fiveGFakeStation.getMcc() + tranzMnc(fiveGFakeStation.getMnc());
            StringBuilder sb = new StringBuilder("AT+EGMC=1,\"NR_high_priority_cell\",");
            sb.append(z ? "1," : "2,");
            sb.append(str2.concat(","));
            sb.append(fiveGFakeStation.getDlarfcn() + ",");
            sb.append(fiveGFakeStation.getPci());
            if (z) {
                sb.append("," + str);
            }
            FiveGPowerController.log("fakeSaStation prioritizeCell");
            sendAtCommand(MiuiPhoneUtils.getPhone(i), new String[]{sb.toString(), "+EGMC"});
        } catch (Exception unused) {
            FiveGPowerController.log("prioritizeCell error");
        }
    }

    private String tranzMnc(int i) {
        if (i == 0) {
            return "00f";
        }
        if (i == 2) {
            return "02f";
        }
        if (i == 4) {
            return "04f";
        }
        if (i == 7) {
            return "07f";
        }
        if (i == 8) {
            return "08f";
        }
        return "";
    }

    private void handleNsaScgPoorStatusInd(String[] strArr) {
        if (strArr == null || strArr.length < 3) {
            StringBuilder sb = new StringBuilder("handleNsaScgPoorStatusInd invalid data length=");
            sb.append(strArr != null ? strArr.length : 0);
            FiveGPowerController.log(sb.toString());
            return;
        }
        if (MiuiPhoneUtils.getDataPhone() == null) {
            FiveGPowerController.log("phone is null ...");
            return;
        }
        int i = Integer.parseInt(strArr[2].trim());
        FiveGPowerController.log("handleNsaScgPoorStatusInd status=" + i);
        if (i == 1) {
            if (this.mIsNsaScgPoorStatusFlag || this.mInCall || (this.mIsCharging && this.mBatteryLevel > 25)) {
                if (this.mIsNsaScgPoorStatusFlag || !MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported()) {
                    return;
                }
                notifyModemNsaScgPoorSuccess(false);
                return;
            }
            if (this.mIsRecFreeze) {
                if (MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported()) {
                    notifyModemNsaScgPoorSuccess(false);
                    return;
                }
                return;
            }
            this.mIsNsaScgPoorStatusFlag = true;
            if (FiveGUtils.isXmRadioSupported()) {
                changeEnableEndcStatus(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
            } else {
                execNsaOptimization(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
            }
            limitNwCA(true, FiveGPowerController.LimitCaScene.NSA_SCG_POOR_MODE.value);
            if (MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported()) {
                notifyModemNsaScgPoorSuccess(true);
                return;
            }
            return;
        }
        if (i == 0 && this.mIsNsaScgPoorStatusFlag) {
            this.mIsNsaScgPoorStatusFlag = false;
            if (FiveGUtils.isXmRadioSupported()) {
                changeEnableEndcStatus(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
            } else {
                execNsaOptimization(true, NrSceneConstants$Scene.NSA_SCG_POOR_MODE);
            }
            limitNwCA(false, FiveGPowerController.LimitCaScene.NSA_SCG_POOR_MODE.value);
            if (MiuiPhoneUtils.isNsaScgPoorOptimizationV2Supported()) {
                notifyModemNsaScgPoorSuccess(false);
            }
        }
    }

    private void handleBGSearchStatusInd(String[] strArr) {
        if (strArr != null) {
            try {
                if (strArr.length < 5) {
                    return;
                }
                int i = Integer.parseInt(strArr[2].trim());
                if (Integer.parseInt(strArr[4].trim()) == 1) {
                    deprioritizeSaAsync(false, i);
                    this.mIsNrBGSearch[i] = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected void notifyModemNeedReportNsaScgPoorStatus(boolean z, int[] iArr) {
        StringBuilder sb = new StringBuilder("AT+EDMFAPP=20107,101,1");
        if (z) {
            sb.append(",1");
        } else {
            sb.append(",0");
        }
        for (int i : iArr) {
            sb.append(",");
            sb.append(i);
        }
        String[] strArr = {sb.toString(), "+EDMFAPP"};
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        if (dataPhone == null) {
            FiveGPowerController.log("phone is null ...");
        } else {
            sendAtCommand(dataPhone, strArr);
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected void notifyModemRegisterIcdForNsaScgPoor() {
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        if (dataPhone == null) {
            FiveGPowerController.log("phone is null ...");
        } else {
            sendAtCommand(dataPhone, REGISTER_NSA_SCG_POOR_ICD_CMD);
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected void notifyModemRegisterIcdForNrBgSearch(boolean z) {
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        if (dataPhone == null) {
            FiveGPowerController.log("phone is null ...");
        } else {
            sendAtCommand(dataPhone, z ? REGISTER_NR_BG_SEARCH_ICD_CMD : UNREGISTER_NR_BG_SEARCH_ICD_CMD);
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected void notifyModemNsaScgPoorSuccess(boolean z) {
        StringBuilder sb = new StringBuilder("AT+EDMFAPP=20107,101,1");
        if (z) {
            sb.append(",3");
        } else {
            sb.append(",4");
        }
        for (int i = 0; i < mNsaScgPoorThreshold.length; i++) {
            sb.append(",");
            sb.append(mNsaScgPoorThreshold[i]);
        }
        String[] strArr = {sb.toString(), "+EDMFAPP"};
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        if (dataPhone == null) {
            FiveGPowerController.log("phone is null ...");
        } else {
            sendAtCommand(dataPhone, strArr);
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected void notifyModemIdelNeiMea(int i, int i2) {
        String[] strArrConstructionAtCmd = constructionAtCmd(i, i2);
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        if (dataPhone == null) {
            FiveGPowerController.log("phone is null ...");
        } else {
            sendAtCommand(dataPhone, strArrConstructionAtCmd);
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected void recoverFakestation(int i) {
        CellIdentityNr cellIdentityNr;
        ArrayList arrayList = fakeStationLists;
        if (arrayList.isEmpty()) {
            return;
        }
        try {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone == null || (cellIdentityNr = (CellIdentityNr) phone.getCurrentCellIdentity()) == null) {
                return;
            }
            String mccString = cellIdentityNr.getMccString();
            String mncString = cellIdentityNr.getMncString();
            if (mccString != null && mncString != null) {
                FiveGFakeStation fiveGFakeStation = new FiveGFakeStation(Integer.parseInt(mccString), Integer.parseInt(mncString), cellIdentityNr.getNrarfcn(), cellIdentityNr.getPci());
                if (existFakeStaion(arrayList, fiveGFakeStation)) {
                    prioritizeCell(i, false, fiveGFakeStation, "");
                    arrayList.remove(fiveGFakeStation);
                    FiveGPowerController.log("recoverFakestation");
                    return;
                }
                return;
            }
            FiveGPowerController.log("recoverFakestation mcc or mnc is null");
        } catch (Exception e) {
            FiveGPowerController.log("recoverFakestation() exception: " + e);
        }
    }

    @Override // com.android.phone.FiveGPowerController
    public void resetFakeStationList() {
        Phone dataPhone;
        ArrayList arrayList = fakeStationLists;
        if (arrayList.isEmpty() || (dataPhone = MiuiPhoneUtils.getDataPhone()) == null) {
            return;
        }
        int phoneId = dataPhone.getPhoneId();
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            prioritizeCell(phoneId, false, (FiveGFakeStation) obj, "");
        }
        fakeStationLists.clear();
    }

    private boolean existFakeStaion(ArrayList arrayList, FiveGFakeStation fiveGFakeStation) {
        if (!arrayList.isEmpty()) {
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                if (((FiveGFakeStation) obj).equals(fiveGFakeStation)) {
                    FiveGPowerController.log("exist fakeStation");
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.android.phone.FiveGPowerController
    protected void adjustNetworkSearchTimer(boolean z) {
        Phone dataPhone = MiuiPhoneUtils.getDataPhone();
        if (z) {
            sendAtCommands(dataPhone, "AT+ERSCFG=120,300,300,300,300,600,600,600,600,1800,1800,1800,1800,3600,3600,600,600,1800,1800,1800,3600,3600,3600", "AT+ESRVREC= 2,7200,100,1, 180, 600, 180, 600", "AT+ESBP = 5,\"SBP_GMSS_NO_SIM_STOP_PERIODIC_LIMITED_RECOVERY_SEARCH\", 1");
        } else {
            sendAtCommands(dataPhone, "AT+ERSCFG=60,120,300,300,300,600,600,600,600,600,600,600,600,600,600,600,600,1800,600,600,600,1800,600", "AT+ESRVREC= 2,7200,100,1, 180, 600, 180, 600", "AT+ESBP = 5,\"SBP_GMSS_NO_SIM_STOP_PERIODIC_LIMITED_RECOVERY_SEARCH\", 1");
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected void resetNetworkSearchTimer() {
        sendAtCommands(MiuiPhoneUtils.getDataPhone(), "AT+ERSCFG=20,20,60,60,60,60,90,90,90,90,90,90,180,180,180,180,180,180,360,360,360,360,360", "AT+ESRVREC= 5,200,30,0, 600, 180,600,180", "AT+ESBP = 5,\"SBP_GMSS_NO_SIM_STOP_PERIODIC_LIMITED_RECOVERY_SEARCH\", 0");
    }

    @Override // com.android.phone.FiveGPowerController
    protected void resetBGSearch() {
        if (MiuiPhoneUtils.getDataPhone() == null) {
            FiveGPowerController.log("phone is null ...");
            return;
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (this.mIsNrBGSearch[i]) {
                deprioritizeSaAsync(true, i);
                this.mIsNrBGSearch[i] = false;
            }
        }
    }
}
