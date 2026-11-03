package com.android.internal.telephony.ntnphone;

import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class PowerManagerTracker extends Handler {
    protected static final int EVENT_BBIC_POWER_CHANGED = 1;
    protected static final int EVENT_BBIC_SIM_POWER_CHANGED = 2;
    protected static final int EVENT_DELAY_BBIC_POWER_OFF = 3;
    public static final int EVENT_NTN_HYPER_WARMBOOT_STATISTIC_ID = 100800001;
    protected static final int EVENT_POWER_SWITCH_TIMEOUT = 4;
    protected static final int ONE_SECOND_MS = 1000;
    protected static final int POWER_SWITCH_TIMEOUT_MS = 10000;
    protected static final String PROP_SAT_RESTART_RILD_DISABLED = "0";
    protected static final String PROP_SAT_RESTART_RILD_ENABLED = "5";
    protected static final String PROP_SAT_RESTART_RILD_PROP = "persist.radio.ntn_rild_slot";
    protected static final String PROP_SAT_RIL_NTN_BBIC_STATE = "ril.ntn_bbic_state";
    protected static final int PROP_SAT_RIL_NTN_BBIC_STATE_OFF = 0;
    protected static final int PROP_SAT_RIL_NTN_BBIC_STATE_ON = 1;
    private NtnPhone mPhone;
    private boolean mIsSeparatePower = true;
    private Status mCurrentStatus = Status.POWER_OFF;

    public enum Status {
        POWER_OFF("Power Off"),
        POWER_ON("Power On"),
        POWER_OFF_DELAY("Power Off Delay");

        private final String mDescription;

        Status(String str) {
            this.mDescription = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.mDescription;
        }
    }

    public PowerManagerTracker(NtnPhone ntnPhone) {
        this.mPhone = ntnPhone;
        Rlog.d("PowerManagerTracker", "Initialized with status: " + this.mCurrentStatus);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Rlog.d("PowerManagerTracker", "handleMessage: " + message.what);
        int i = message.what;
        if (i == 1) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                Rlog.e("PowerManagerTracker", "EVENT_BBIC_POWER_CHANGED exception: " + asyncResult.exception.getMessage());
                if (hasMessages(4)) {
                    handlePowerSwitchException();
                    return;
                }
                return;
            }
            removeMessages(4);
            processAfterPowerSwitch(this.mCurrentStatus);
            return;
        }
        if (i != 2) {
            if (i == 3) {
                removeMessages(4);
                setBBICPowerSwitch(false);
                updateStatus(Status.POWER_OFF);
                return;
            } else {
                if (i == 4) {
                    handlePowerSwitchException();
                    return;
                }
                Rlog.d("PowerManagerTracker", "PowerManagerTracker: not support Event msg.what=" + message.what);
                return;
            }
        }
        AsyncResult asyncResult2 = (AsyncResult) message.obj;
        if (asyncResult2.exception != null) {
            Rlog.e("PowerManagerTracker", "EVENT_BBIC_SIM_POWER_CHANGED exception: " + asyncResult2.exception.getMessage());
            if (hasMessages(4)) {
                handlePowerSwitchException();
                return;
            }
            return;
        }
        removeMessages(4);
        processAfterSimPowerSwitch(this.mCurrentStatus);
    }

    public void setPowerSwitch(boolean z, int i) {
        if (this.mPhone.mNtnRil == null) {
            Rlog.e("PowerManagerTracker", "setPowerSwitch: mNtnRil is null");
            return;
        }
        StringBuilder sb = new StringBuilder("Request power ");
        sb.append(z ? "ON" : "OFF");
        sb.append(", current status=");
        sb.append(this.mCurrentStatus);
        sb.append(", delay=");
        sb.append(i);
        sb.append("ms");
        Rlog.d("PowerManagerTracker", sb.toString());
        if (z) {
            handleUserPowerOn();
        } else {
            handleUserPowerOff(i);
        }
    }

    private void handleUserPowerOn() {
        int iOrdinal = this.mCurrentStatus.ordinal();
        if (iOrdinal == 0) {
            Rlog.d("PowerManagerTracker", "handleUserPowerOn setprop: ril.ntn_bbic_state=1");
            SystemProperties.set(PROP_SAT_RIL_NTN_BBIC_STATE, Integer.toString(1));
            SystemProperties.set(PROP_SAT_RESTART_RILD_PROP, PROP_SAT_RESTART_RILD_DISABLED);
            reportDataToCdc(false);
            setBBICPowerSwitch(true);
            updateStatus(Status.POWER_ON);
            return;
        }
        if (iOrdinal == 1) {
            Rlog.d("PowerManagerTracker", "Already in POWER_ON state, no action");
        } else {
            if (iOrdinal != 2) {
                return;
            }
            cancelDelayPowerOff();
            reportDataToCdc(true);
            setSimPowerSwitch(true);
            updateStatus(Status.POWER_ON);
        }
    }

    private void handleUserPowerOff(int i) {
        int iOrdinal = this.mCurrentStatus.ordinal();
        if (iOrdinal == 0) {
            Rlog.d("PowerManagerTracker", "Already in " + this.mCurrentStatus + " state, no action");
            return;
        }
        if (iOrdinal != 1) {
            if (iOrdinal == 2 && i == 0 && this.mPhone.mNtnRil != null) {
                cancelDelayPowerOff();
                this.mPhone.mNtnRil.setPowerSwitch(false, null);
                updateStatus(Status.POWER_OFF);
                return;
            }
            return;
        }
        if (i > 0) {
            Rlog.d("PowerManagerTracker", "handleUserPowerOff setprop: ril.ntn_bbic_state=0");
            SystemProperties.set(PROP_SAT_RIL_NTN_BBIC_STATE, Integer.toString(0));
            TelephonyCommonUtils.resetNtnStackPropertyValue();
            setSimPowerSwitch(false);
            scheduleDelayPowerOff(i);
            updateStatus(Status.POWER_OFF_DELAY);
            return;
        }
        setSimPowerSwitch(false);
        updateStatus(Status.POWER_OFF);
    }

    private void startPowerOperation() {
        Rlog.d("PowerManagerTracker", "Starting power operation");
        schedulePowerSwitchTimeout();
    }

    private void restartRild() {
        SystemProperties.set(PROP_SAT_RESTART_RILD_PROP, PROP_SAT_RESTART_RILD_ENABLED);
        Rlog.d("PowerManagerTracker", "restartRild: Set property persist.radio.ntn_rild_slot=5");
    }

    private void endSatelliteModeAndSwitchToCellular(Status status) {
        if (status == Status.POWER_OFF || status == Status.POWER_OFF_DELAY) {
            this.mPhone.mNtnRil.shutDownNtnProxyAndRequestList();
            NtnPhoneFactory.BackToCellular();
            Rlog.d("PowerManagerTracker", "Successfully switched back to cellular mode");
        }
    }

    private void schedulePowerSwitchTimeout() {
        removeMessages(4);
        sendMessageDelayed(obtainMessage(4), 10000L);
        Rlog.d("PowerManagerTracker", "Scheduled power switch timeout in 10000ms");
    }

    private void handlePowerSwitchException() {
        Rlog.d("PowerManagerTracker", "handle power switch exception!");
        removeMessages(4);
        if (this.mCurrentStatus == Status.POWER_OFF_DELAY) {
            cancelDelayPowerOff();
            updateStatus(Status.POWER_OFF);
        }
        restartRild();
        endSatelliteModeAndSwitchToCellular(this.mCurrentStatus);
    }

    private void scheduleDelayPowerOff(int i) {
        removeMessages(3);
        sendMessageDelayed(obtainMessage(3), i);
        Rlog.d("PowerManagerTracker", "Scheduled BBIC power off after " + i + "ms");
    }

    private void cancelDelayPowerOff() {
        removeMessages(3);
        Rlog.d("PowerManagerTracker", "Cancelled delayed BBIC power off");
    }

    private void updateStatus(Status status) {
        if (this.mCurrentStatus != status) {
            Rlog.d("PowerManagerTracker", "Status changed: " + this.mCurrentStatus + " -> " + status);
            this.mCurrentStatus = status;
        }
    }

    private void processAfterPowerSwitch(Status status) {
        if (!isSeparatePower()) {
            Rlog.d("PowerManagerTracker", "Sim already powered on after BBIC power on");
            return;
        }
        if (status == Status.POWER_OFF_DELAY || status == Status.POWER_OFF) {
            TelephonyCommonUtils.resetNtnStackPropertyValue();
        }
        if (status != Status.POWER_ON) {
            Rlog.d("PowerManagerTracker", "set bbic Power off");
        } else {
            setSimPowerSwitch(true);
        }
    }

    private void processAfterSimPowerSwitch(Status status) {
        if (!isSeparatePower()) {
            Rlog.d("PowerManagerTracker", "sim power is already powered on after BBIC power on");
            return;
        }
        endSatelliteModeAndSwitchToCellular(status);
        if (status != Status.POWER_OFF) {
            Rlog.d("PowerManagerTracker", "current is not power off, not need to process;");
        } else {
            setBBICPowerSwitch(false);
        }
    }

    public void setBBICPowerSwitch(boolean z) {
        if (this.mPhone.mNtnRil == null) {
            Rlog.e("PowerManagerTracker", "setBBICPowerSwitch: mNtnRil is null");
        } else {
            startPowerOperation();
            this.mPhone.mNtnRil.setPowerSwitch(z, obtainMessage(1));
        }
    }

    public void setSimPowerSwitch(boolean z) {
        if (this.mPhone.mNtnRil == null) {
            Rlog.e("PowerManagerTracker", "setSimPowerSwitch: mNtnRil is null");
        } else {
            startPowerOperation();
            this.mPhone.mNtnRil.setSimCardPower(z, obtainMessage(2));
        }
    }

    public boolean isSeparatePower() {
        return this.mIsSeparatePower;
    }

    public void setSeparatePower(boolean z) {
        this.mIsSeparatePower = z;
    }

    public boolean isSatellitePowerOn() {
        return this.mCurrentStatus == Status.POWER_ON;
    }

    public boolean isSatelliteDelayPowerOff() {
        return this.mCurrentStatus == Status.POWER_OFF_DELAY;
    }

    public Status getCurrentStatus() {
        return this.mCurrentStatus;
    }

    private void reportDataToCdc(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putInt("is_warmboot", z ? 1 : 0);
        TelephonyCommonUtilsStub.get().reportDataToCdc(EVENT_NTN_HYPER_WARMBOOT_STATISTIC_ID, bundle);
    }
}
