package com.android.phone;

import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import com.android.internal.telephony.PhoneConstants;
import com.android.phone.module.INetworkModule;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.NrSceneConstants$Scene;
import com.qti.extphone.Client;
import com.qti.extphone.ExtTelephonyManager;
import java.util.ArrayList;
import miui.process.ForegroundInfo;
import miui.process.ProcessManager;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManagerEx;
import vendor.xiaomi.hardware.misys.V2_0.IMiSys;

/* JADX INFO: loaded from: classes.dex */
public class QcFiveGPowerController extends FiveGPowerController implements QcPhoneConnector.IQcPhoneConnectionListener {
    private Client mClient;
    private ExtTelephonyManager mExtTelephonyManager;
    private volatile boolean mIsServiceBound;

    protected QcFiveGPowerController(Looper looper) {
        super(looper);
        QcPhoneConnector.get().addListener(this);
    }

    @Override // com.android.phone.FiveGPowerController, android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == 0) {
            FiveGPowerController.log("EVENT_QCRIL_HOOK_READY");
            if (FiveGUtils.isXmRadioSupported()) {
                return;
            }
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                deprioritizeSaAsync(false, i);
            }
            return;
        }
        super.handleMessage(message);
    }

    @Override // com.android.phone.QcPhoneConnector.IQcPhoneConnectionListener
    public void onConnected(ExtTelephonyManager extTelephonyManager, Client client) {
        FiveGPowerController.log("QcPhone Service onConnected");
        this.mIsServiceBound = true;
        this.mExtTelephonyManager = extTelephonyManager;
        this.mClient = client;
    }

    @Override // com.android.phone.QcPhoneConnector.IQcPhoneConnectionListener
    public void onDisconnected() {
        FiveGPowerController.log("QcPhone Service onDisconnected");
        this.mIsServiceBound = false;
        this.mExtTelephonyManager = null;
        this.mClient = null;
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void bindServiceAndEnableEndc(boolean z) {
        bindServiceAndEnableEndc(z, DefaultSimManager.getDefaultDataSlotId());
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void bindServiceAndEnableEndc(boolean z, int i) {
        try {
            FiveGPowerController.log("bindServiceAndEnableEndc mExtTelephonyManager: " + this.mExtTelephonyManager + ", mClient: " + this.mClient + ", mIsServiceBound: " + this.mIsServiceBound);
            if (this.mIsServiceBound && this.mExtTelephonyManager != null && this.mClient != null) {
                if (z) {
                    for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                        FiveGPowerController.log("bindServiceAndEnableEndc enableEndc: " + z + ", phoneId = " + i2);
                        this.mExtTelephonyManager.enableEndc(i2, z, this.mClient);
                    }
                } else {
                    FiveGPowerController.log("bindServiceAndEnableEndc enableEndc: " + z + ", phoneId = " + i);
                    this.mExtTelephonyManager.enableEndc(i, z, this.mClient);
                }
                for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                    this.mEndcPowerEnabled[i3] = z;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void enableSaAsync(boolean z, int i, NrSceneConstants$Scene nrSceneConstants$Scene) {
        if (!this.mIsRilHookReady) {
            FiveGPowerController.log("enableSaAsync return because mIsRilHookReady not ready");
            return;
        }
        if (this.mInCall && !MiuiImsPhoneUtils.isWifiCallingEnabled() && MiuiPhoneUtils.getPhone(i) != null && MiuiPhoneUtils.getPhone(i).getState() != PhoneConstants.State.IDLE) {
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
                deprioritizeSaAsync(!z, i2);
                this.mSaPowerEnabled[i2] = z;
            }
            this.mFiveGOptimizeScene = NrSceneConstants$Scene.UNKNOWN;
        } else {
            if (FiveGManagerBase.getInstance().getFiveGNetworkMode() == 2) {
                this.mIsSilentSaEndcDisabled = true;
            }
            deprioritizeSaAsync(!z, i);
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
        if (this.mInCall && !MiuiImsPhoneUtils.isWifiCallingEnabled() && MiuiPhoneUtils.getPhone(i) != null && MiuiPhoneUtils.getPhone(i).getState() != PhoneConstants.State.IDLE) {
            FiveGPowerController.log("deprioritizeSaAsync return because inCall");
            return;
        }
        FiveGPowerController.log("deprioritizeSaAsync enabled: " + z + "; slotId: " + i);
        if (this.mIsRilHookReady) {
            SystemProperties.set("ril.deprioritize.sa.slot" + i, Boolean.toString(z));
            localLog("deprioritizeSaAsync", "Phone[" + i + "]: enabled = " + z);
            RilHook.get(this.mContext).qcRilDeprioritizeNR5gAsync(z, i);
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void switchRxMode(boolean z) {
        IMiSys service;
        FiveGPowerController.log("switchRxMode changed = " + z);
        FiveGPowerController.COOLONG_DEVICE_PATH = SystemProperties.get("persist.radio.modem_nr_dsc_path", "");
        ArrayList arrayList = new ArrayList();
        arrayList.add(Byte.valueOf(z ? (byte) 55 : (byte) 48));
        try {
            service = IMiSys.getService(true);
        } catch (Exception e) {
            FiveGPowerController.log("mIMiSys: Exception = " + e);
            service = null;
        }
        IMiSys iMiSys = service;
        if (iMiSys != null) {
            boolean zIsExists = false;
            try {
                String str = FiveGPowerController.COOLONG_DEVICE_PATH;
                zIsExists = str != null ? iMiSys.IsExists(str, "cur_state") : false;
                FiveGPowerController.log("mIsExist = " + zIsExists + " , COOLONG_DEVICE_PATH = " + FiveGPowerController.COOLONG_DEVICE_PATH.toString());
            } catch (Exception e2) {
                FiveGPowerController.log("mIMiSys: Exception = " + e2);
            }
            if (zIsExists) {
                try {
                    FiveGPowerController.log("writeResult = " + iMiSys.MiSysWriteBuffer(FiveGPowerController.COOLONG_DEVICE_PATH, "cur_state", arrayList, arrayList.size()));
                    this.mMimoSceneChangeRxCount = this.mMimoSceneChangeRxCount + 1;
                } catch (Exception e3) {
                    FiveGPowerController.log("MiSysWriteBuffer: Exception = " + e3);
                }
            }
        }
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void updateThrottleLevel(int i) {
        IMiSys service;
        FiveGPowerController.log("updateThrottleLevel throttleLevel = " + i);
        if (!checkThrottleLevelValid(i)) {
            FiveGPowerController.log("Invalid throttleLevel : " + i);
            return;
        }
        if (FiveGPowerController.mIsCurrentHstState || FiveGPowerController.mIsInSubway) {
            updateHstSubwayThermalThrottleRecord(i);
            if (i != 0) {
                localLog("updateThrottleLevelInterrupt", "level=" + i + ", hst=" + FiveGPowerController.mIsCurrentHstState + ", subway=" + FiveGPowerController.mIsInSubway);
                return;
            }
        }
        FiveGPowerController.COOLONG_DEVICE_PATH = SystemProperties.get("persist.radio.modem_nr_dsc_path", "");
        ArrayList arrayList = new ArrayList();
        if (i == 0) {
            arrayList.add((byte) 48);
        } else if (i == 253) {
            this.mThermalThrottleDSDACount++;
        } else if (i == 7) {
            arrayList.add((byte) 55);
            this.mThermalThrottle2RXCount++;
        } else if (i == 8) {
            arrayList.add((byte) 56);
            this.mThermalThrottleDL1Count++;
        } else if (i == 9) {
            arrayList.add((byte) 57);
            this.mThermalThrottleDL2Count++;
        }
        ForegroundInfo foregroundInfo = ProcessManager.getForegroundInfo();
        String str = "";
        if (foregroundInfo != null) {
            str = foregroundInfo.mForegroundPackageName;
        }
        FiveGPowerController.sThermalThrottleFGAppMap.put(str, Integer.valueOf(FiveGPowerController.sThermalThrottleFGAppMap.containsKey(str) ? ((Integer) FiveGPowerController.sThermalThrottleFGAppMap.get(str)).intValue() : 1));
        FiveGPowerController.sThermalThrottleSASnrMap.put(Integer.valueOf(this.mSASnr), Integer.valueOf(FiveGPowerController.sThermalThrottleSASnrMap.containsKey(Integer.valueOf(this.mSASnr)) ? ((Integer) FiveGPowerController.sThermalThrottleSASnrMap.get(Integer.valueOf(this.mSASnr))).intValue() : 1));
        if (MiuiPhoneUtils.isThermalThrottleQmiApiSupported()) {
            FiveGManagerBase.getInstance().setModemFeature(107, i, DefaultSimManager.getDefaultDataSlotId());
        } else {
            try {
                service = IMiSys.getService(true);
            } catch (Exception e) {
                FiveGPowerController.log("mIMiSys: Exception = " + e);
                service = null;
            }
            IMiSys iMiSys = service;
            if (iMiSys != null) {
                boolean zIsExists = false;
                try {
                    String str2 = FiveGPowerController.COOLONG_DEVICE_PATH;
                    zIsExists = str2 != null ? iMiSys.IsExists(str2, "cur_state") : false;
                    FiveGPowerController.log("mIsExist = " + zIsExists + " , COOLONG_DEVICE_PATH = " + FiveGPowerController.COOLONG_DEVICE_PATH.toString());
                } catch (Exception e2) {
                    FiveGPowerController.log("mIMiSys: Exception = " + e2);
                }
                if (zIsExists) {
                    try {
                        FiveGPowerController.log("writeResult = " + iMiSys.MiSysWriteBuffer(FiveGPowerController.COOLONG_DEVICE_PATH, "cur_state", arrayList, arrayList.size()));
                    } catch (Exception e3) {
                        FiveGPowerController.log("MiSysWriteBuffer: Exception = " + e3);
                    }
                }
            }
        }
    }

    private boolean checkThrottleLevelValid(int i) {
        return (i != 253 || (MiuiPhoneUtils.isThermalThrottleQmiApiSupported() && MiuiPhoneUtils.isLicenseDualDataSupported())) && TelephonyManagerEx.getDefault().isDsdaSupported();
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void setDatConfig(boolean z, int i) {
        FiveGPowerController.log("setDatConfig will be implemented...");
    }

    @Override // com.android.phone.FiveGPowerController
    protected void notifyModemReset() {
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).handleModemResetEvent();
    }
}
