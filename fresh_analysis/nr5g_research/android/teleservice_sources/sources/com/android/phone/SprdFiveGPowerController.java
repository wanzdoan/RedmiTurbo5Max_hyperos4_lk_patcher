package com.android.phone;

import android.os.Looper;
import android.os.SystemProperties;
import com.android.phone.utils.ATCommandUtils;
import com.android.phone.utils.NrSceneConstants$Scene;

/* JADX INFO: loaded from: classes.dex */
public class SprdFiveGPowerController extends FiveGPowerController {
    private int mModemEndcStatus;

    protected SprdFiveGPowerController(Looper looper) {
        super(looper);
        this.mModemEndcStatus = -1;
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void bindServiceAndEnableEndc(boolean z, int i) {
        try {
            String[] strArr = new String[3];
            if (z) {
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    if (MiuiPhoneUtils.getPhone(i2) != null) {
                        FiveGPowerController.log("bindServiceAndEnableEndc enableEndc: " + z + ", phoneId = " + i2);
                        if (sendSprdAtCommand("AT+SPASENGMD=\"#las_set_vddmodem_thermal_control\",0", strArr, i2) >= 0) {
                            this.mEndcPowerEnabled[i2] = z;
                        }
                    }
                }
                this.mModemEndcStatus = hasEndcPowerEnabled() ? 1 : 0;
            } else {
                if (MiuiPhoneUtils.getPhone(i) == null) {
                    return;
                }
                FiveGPowerController.log("bindServiceAndEnableEndc enableEndc: " + z + ", phoneId = " + i);
                if (sendSprdAtCommand("AT+SPASENGMD=\"#las_set_vddmodem_thermal_control\",2", strArr, i) >= 0) {
                    this.mEndcPowerEnabled[i] = z;
                }
                this.mModemEndcStatus = hasEndcPowerEnabled() ? 1 : 0;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private boolean hasEndcPowerEnabled() {
        for (boolean z : this.mEndcPowerEnabled) {
            if (z) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void enableSaAsync(boolean z, int i, NrSceneConstants$Scene nrSceneConstants$Scene) {
        if (nrSceneConstants$Scene != NrSceneConstants$Scene.SA_WECHAT_VOIP) {
            FiveGPowerController.log("enableSaAsync no support " + nrSceneConstants$Scene + " in Unisoc platform.");
            return;
        }
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
            this.mFiveGOptimizeScene = getSaPowerEnabledScene();
        }
        this.mLastOptScene[i] = nrSceneConstants$Scene;
    }

    private NrSceneConstants$Scene getSaPowerEnabledScene() {
        NrSceneConstants$Scene nrSceneConstants$Scene;
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (this.mSaPowerEnabled[i] && (nrSceneConstants$Scene = this.mLastOptScene[i]) != null && nrSceneConstants$Scene != NrSceneConstants$Scene.UNKNOWN) {
                return nrSceneConstants$Scene;
            }
        }
        return NrSceneConstants$Scene.UNKNOWN;
    }

    @Override // com.android.phone.FiveGPowerController
    protected synchronized void deprioritizeSaAsync(boolean z, int i) {
        if (this.mInCall && !MiuiImsPhoneUtils.isWifiCallingEnabled()) {
            FiveGPowerController.log("deprioritizeSaAsync return because inCall");
            return;
        }
        FiveGPowerController.log("deprioritizeSaAsync enabled = " + z + ", slotId = " + i);
        String[] strArr = new String[3];
        if (z) {
            sendSprdAtCommand("AT+SPASENGMD=\"#nras_dummy_at\",0,0", strArr, i);
            sendSprdAtCommand("AT+SPASENGMD=\"#nras_set_restrict_event_mrt\",0", strArr, i);
        } else {
            sendSprdAtCommand("AT+SPASENGMD=\"#nras_dummy_at\",0,1", strArr, i);
            sendSprdAtCommand("AT+SPASENGMD=\"#nras_dummy_at\",4,-115,-15,-10", strArr, i);
            sendSprdAtCommand("AT+SPASENGMD=\"#nras_set_restrict_event_mrt\",28", strArr, i);
        }
        SystemProperties.set("ril.deprioritize.sa.slot" + i, Boolean.toString(z));
    }

    private int sendSprdAtCommand(String str, String[] strArr, int i) {
        ATCommandUtils aTCommandUtils = ATCommandUtils.getInstance();
        if (aTCommandUtils == null) {
            return -1;
        }
        return aTCommandUtils.sendAtCmdSync(str, strArr, i);
    }
}
