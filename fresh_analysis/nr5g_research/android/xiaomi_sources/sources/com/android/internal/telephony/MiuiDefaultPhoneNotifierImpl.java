package com.android.internal.telephony;

import android.telephony.CellIdentity;
import android.telephony.PreciseDataConnectionState;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyRegistryManager;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.util.TelephonyUtils;
import java.util.HashMap;
import miui.telephony.CommenUtils;
import miui.telephony.MiuiAisSimLockUtils;
import miui.telephony.MiuiDisplayInfoController;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDefaultPhoneNotifierImpl implements MiuiDefaultPhoneNotifierStub.IMiuiDefaultPhoneNotifier {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "MiuiDefaultPhoneNotifierImpl";
    private TelephonyRegistryManager mTelephonyRegistryMgr;
    private SignalStrength[] mLastSignalStrength = new SignalStrength[MiuiTelephony.PHONE_COUNT];
    private long[] mSignalStrengthBigChangeCount = new long[MiuiTelephony.PHONE_COUNT];
    private long[] mSignalStrengthPoorCount = new long[MiuiTelephony.PHONE_COUNT];
    private HashMap<String, String> mSignalStrengthOptimizationCountMap = new HashMap<>();

    public void init(TelephonyRegistryManager telephonyRegistryMgr) {
        this.mTelephonyRegistryMgr = telephonyRegistryMgr;
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, Integer.MAX_VALUE);
    }

    private static void log(String s) {
        Rlog.d(LOG_TAG, s);
    }

    public void notifyDataConnection(Phone sender, PreciseDataConnectionState preciseState) {
        try {
            handleDataConnectionStateChanged(sender, preciseState);
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "notifyDataConnection exception = " + e);
        }
    }

    public void notifyServiceStateForSubId(Phone sender, final ServiceState ss, final int subId) {
        final int phoneId = sender.getPhoneId();
        MiuiNetworkControllerImpl networkImpl = (MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, phoneId);
        if (networkImpl != null && networkImpl.isDelayUpdateServiceState(sender)) {
            Rlog.d(LOG_TAG, "notifyServiceStateForSubId: isDelayUpdateServiceState phoneId=" + phoneId + " subId=" + subId);
        } else {
            BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.telephony.MiuiDefaultPhoneNotifierImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    MiuiDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr.notifyServiceStateChanged(phoneId, subId, ss);
                    TelephonyStatAdapter.notifyServiceState(phoneId, ss);
                }
            });
        }
    }

    public boolean notifySignalStrength(Phone sender) {
        final int phoneId = sender.getPhoneId();
        final int subId = sender.getSubId();
        final SignalStrength ss = sender.getSignalStrength();
        handleRealSignalStrengthCount(phoneId, ss);
        MiuiNetworkControllerImpl networkImpl = (MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, phoneId);
        if (networkImpl != null) {
            if (sender.getPhoneType() != 7 && networkImpl.isDelayUpdateServiceState(sender)) {
                Rlog.d(LOG_TAG, "notifySignalStrength: isDelayUpdateServiceState phoneId=" + phoneId + " subId=" + subId);
                return DBG;
            }
            if (networkImpl.isDelayNotifySignalStrength(sender)) {
                Rlog.d(LOG_TAG, "notifySignalStrength: isDelayNotifySignalStrength phoneId=" + phoneId + " subId=" + subId);
                return DBG;
            }
            if (networkImpl.isNtnPhone(sender)) {
                int showMiuiLevel = networkImpl.getShowMiuiLevel(ss.getMiuiLevel());
                ss.setMiuiLevel(showMiuiLevel);
            }
        }
        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.telephony.MiuiDefaultPhoneNotifierImpl.2
            @Override // java.lang.Runnable
            public void run() {
                if (MiuiDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr != null) {
                    Rlog.d(MiuiDefaultPhoneNotifierImpl.LOG_TAG, "notifySignalStrength: ss=" + ss + " phoneId=" + phoneId + " subId=" + subId);
                    MiuiDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr.notifySignalStrengthChanged(phoneId, subId, ss);
                    TelephonyStatAdapter.notifySignalStrength(phoneId, ss);
                }
            }
        });
        return true;
    }

    public void notifyCellLocation(Phone sender, final CellIdentity cellIdentity) {
        final int phoneId = sender.getPhoneId();
        final int subId = sender.getSubId();
        MiuiNetworkControllerImpl networkImpl = (MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, phoneId);
        if (networkImpl != null) {
            networkImpl.notifyLocationChanged(sender, cellIdentity);
        }
        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.telephony.MiuiDefaultPhoneNotifierImpl.3
            @Override // java.lang.Runnable
            public void run() {
                MiuiDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr.notifyCellLocation(subId, cellIdentity);
                TelephonyStatAdapter.notifyCellLocation(phoneId, cellIdentity.asCellLocation());
            }
        });
    }

    public void notifyPreciseCallState(Phone sender) {
        int phoneId = sender.getPhoneId();
        Call foregroundCall = sender.getForegroundCall();
        if (foregroundCall != null) {
            TelephonyStatAdapter.notifyPreciseCallState(phoneId, DefaultPhoneNotifier.convertPreciseCallState(foregroundCall.getState()));
        }
    }

    public ServiceState handleServiceStateForAisSim(ServiceState ss) {
        if (ss != null && MiuiAisSimLockUtils.isNonAisSimRestricted()) {
            Rlog.d(LOG_TAG, "handleServiceStateForAisSim setEmergencyOnly ss=" + ss);
            ss.setStateOutOfService();
            ss.setEmergencyOnly(true);
        }
        return ss;
    }

    private void handleDataConnectionStateChanged(final Phone sender, PreciseDataConnectionState preciseState) {
        final int dataConnectionState;
        if (preciseState == null || preciseState.getApnSetting() == null || Integer.MAX_VALUE == (dataConnectionState = MiuiDisplayInfoController.getInstance().convertDataConnectionState(sender, preciseState))) {
            return;
        }
        if (dataConnectionState != preciseState.getState()) {
            preciseState = new PreciseDataConnectionState.Builder().setId(preciseState.getId()).setState(dataConnectionState).setNetworkType(preciseState.getNetworkType()).setTransportType(preciseState.getTransportType()).setLinkProperties(preciseState.getLinkProperties()).setApnSetting(preciseState.getApnSetting()).setFailCause(preciseState.getLastCauseCode()).build();
        }
        final PreciseDataConnectionState newPreciseState = preciseState;
        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.telephony.MiuiDefaultPhoneNotifierImpl.4
            @Override // java.lang.Runnable
            public void run() {
                Rlog.d(MiuiDefaultPhoneNotifierImpl.LOG_TAG, "notifyDataConnectionForSubscriber[" + sender.getPhoneId() + "] state = " + TelephonyUtils.dataStateToString(dataConnectionState));
                MiuiDefaultPhoneNotifierImpl.this.mTelephonyRegistryMgr.notifyDataConnectionForSubscriber(sender.getPhoneId(), sender.getSubId(), newPreciseState);
            }
        });
    }

    private void handleRealSignalStrengthCount(int phoneId, SignalStrength newSignalStrength) {
        if (isValidPhoneId(phoneId) && this.mLastSignalStrength[phoneId] != null) {
            MiuiNetworkControllerImpl networkImpl = (MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, phoneId);
            boolean isScreenon = networkImpl.isScreenOn();
            int lastLevel = this.mLastSignalStrength[phoneId].getMiuiLevel();
            int currentLevel = newSignalStrength.getMiuiLevel();
            if (isScreenon && lastLevel - currentLevel >= 1) {
                long[] jArr = this.mSignalStrengthPoorCount;
                jArr[phoneId] = jArr[phoneId] + 1;
                if (lastLevel - currentLevel >= 2) {
                    long[] jArr2 = this.mSignalStrengthBigChangeCount;
                    jArr2[phoneId] = jArr2[phoneId] + 1;
                }
            }
        }
        this.mLastSignalStrength[phoneId] = newSignalStrength;
    }

    private void updateSignalStrengthOptimizationCountMap() {
        for (int slotId = 0; slotId < MiuiTelephony.PHONE_COUNT; slotId++) {
            MiuiNetworkControllerImpl networkImpl = (MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, slotId);
            this.mSignalStrengthOptimizationCountMap.put(CommenUtils.KEY_REAL_SIGNAL_STRENGTH_POOR_COUNT + slotId, String.valueOf(this.mSignalStrengthPoorCount[slotId]));
            this.mSignalStrengthOptimizationCountMap.put(CommenUtils.KEY_REAL_SIGNAL_STRENGTH_BIG_CHANGE_COUNT + slotId, String.valueOf(this.mSignalStrengthBigChangeCount[slotId]));
            this.mSignalStrengthOptimizationCountMap.putAll(networkImpl.getSignalStrengthOptimizationCountMap());
        }
    }

    public HashMap<String, String> getSignalStrengthOptimizationCountMap() {
        updateSignalStrengthOptimizationCountMap();
        for (int slotId = 0; slotId < MiuiTelephony.PHONE_COUNT; slotId++) {
            this.mSignalStrengthPoorCount[slotId] = 0;
            this.mSignalStrengthBigChangeCount[slotId] = 0;
        }
        return this.mSignalStrengthOptimizationCountMap;
    }

    private static boolean isValidPhoneId(int phoneId) {
        if (phoneId < 0 || phoneId >= MiuiTelephony.PHONE_COUNT) {
            return DBG;
        }
        return true;
    }
}
