package com.android.internal.telephony;

import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.view.Display;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.util.ConnectivitySettingsUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class DeviceStateMonitor extends Handler {
    static final int CELL_INFO_INTERVAL_LONG_MS = 10000;
    static final int CELL_INFO_INTERVAL_SHORT_MS = 2000;
    protected static final boolean DBG = false;
    static final int EVENT_CHARGING_STATE_CHANGED = 4;
    static final int EVENT_SCREEN_STATE_CHANGED = 2;
    static final int EVENT_WIFI_CONNECTION_CHANGED = 7;
    private static final int[] LINK_CAPACITY_DOWNLINK_THRESHOLDS = {100, SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD, 1000, GbaManager.REQUEST_TIMEOUT_MS, 10000, 20000, 50000, 75000, 100000, 200000, 500000, 1000000, 1500000, 2000000};
    private static final int[] LINK_CAPACITY_UPLINK_THRESHOLDS = {100, SatelliteController.RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD, 1000, GbaManager.REQUEST_TIMEOUT_MS, 10000, 20000, 50000, 75000, 100000, 200000, 500000};
    protected static final String TAG = "DeviceStateMonitor";
    private final BroadcastReceiver mBroadcastReceiver;
    private boolean mCarrierRequiredModemDisplayNetworkType;
    private int mCellInfoMinInterval;
    private final DisplayManager.DisplayListener mDisplayListener;
    private final FeatureFlags mFeatureFlags;
    private boolean mIsAlwaysSignalStrengthReportingEnabled;
    private boolean mIsAutomotiveProjectionActive;
    protected boolean mIsCharging;
    protected boolean mIsLowDataExpected;
    private boolean mIsPowerSaveOn;
    private boolean mIsRadioOn;
    protected boolean mIsScreenOn;
    protected boolean mIsTetheringOn;
    private boolean mIsWifiConnected;
    private final ConnectivityManager.NetworkCallback mNetworkCallback;
    protected final Phone mPhone;
    private int mUnsolicitedResponseFilter;
    private final NetworkRequest mWifiNetworkRequest;
    private final LocalLog mLocalLog = new LocalLog(64);
    private final RegistrantList mPhysicalChannelConfigRegistrants = new RegistrantList();
    private final RegistrantList mSignalStrengthReportDecisionCallbackRegistrants = new RegistrantList();
    private final RegistrantList mScreenStateRegistrants = new RegistrantList();
    private final RegistrantList mChargingStateRegistrants = new RegistrantList();

    public DeviceStateMonitor(Phone phone, FeatureFlags featureFlags) {
        NetworkRequest networkRequestBuild = new NetworkRequest.Builder().addTransportType(1).addCapability(12).removeCapability(13).build();
        this.mWifiNetworkRequest = networkRequestBuild;
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.internal.telephony.DeviceStateMonitor.1
            Set mWifiNetworks = new HashSet();

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                synchronized (this.mWifiNetworks) {
                    try {
                        if (this.mWifiNetworks.isEmpty()) {
                            DeviceStateMonitor.this.obtainMessage(7, 1, 0).sendToTarget();
                            DeviceStateMonitor.this.log("Wifi (default) connected", true);
                        }
                        this.mWifiNetworks.add(network);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                synchronized (this.mWifiNetworks) {
                    try {
                        this.mWifiNetworks.remove(network);
                        if (this.mWifiNetworks.isEmpty()) {
                            DeviceStateMonitor.this.obtainMessage(7, 0, 0).sendToTarget();
                            DeviceStateMonitor.this.log("Wifi (default) disconnected", true);
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        };
        this.mNetworkCallback = networkCallback;
        this.mCellInfoMinInterval = 2000;
        this.mUnsolicitedResponseFilter = -1;
        DisplayManager.DisplayListener displayListener = new DisplayManager.DisplayListener() { // from class: com.android.internal.telephony.DeviceStateMonitor.2
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i) {
                boolean zIsScreenOn = DeviceStateMonitor.this.isScreenOn();
                Message messageObtainMessage = DeviceStateMonitor.this.obtainMessage(2);
                messageObtainMessage.arg1 = zIsScreenOn ? 1 : 0;
                DeviceStateMonitor.this.sendMessage(messageObtainMessage);
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i) {
                boolean zIsScreenOn = DeviceStateMonitor.this.isScreenOn();
                Message messageObtainMessage = DeviceStateMonitor.this.obtainMessage(2);
                messageObtainMessage.arg1 = zIsScreenOn ? 1 : 0;
                DeviceStateMonitor.this.sendMessage(messageObtainMessage);
            }
        };
        this.mDisplayListener = displayListener;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.DeviceStateMonitor.3
            /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Message messageObtainMessage;
                DeviceStateMonitor.this.log("received: " + intent, true);
                String action = intent.getAction();
                action.hashCode();
                int i = 0;
                byte b = -1;
                switch (action.hashCode()) {
                    case -1754841973:
                        if (action.equals("android.net.conn.TETHER_STATE_CHANGED")) {
                            b = 0;
                        }
                        break;
                    case -54942926:
                        if (action.equals("android.os.action.DISCHARGING")) {
                            b = 1;
                        }
                        break;
                    case 948344062:
                        if (action.equals("android.os.action.CHARGING")) {
                            b = 2;
                        }
                        break;
                    case 1779291251:
                        if (action.equals("android.os.action.POWER_SAVE_MODE_CHANGED")) {
                            b = 3;
                        }
                        break;
                }
                String str = ConnectivitySettingsUtils.PRIVATE_DNS_MODE_OFF_STRING;
                switch (b) {
                    case 0:
                        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("tetherArray");
                        if (stringArrayListExtra != null && stringArrayListExtra.size() > 0) {
                            i = 1;
                        }
                        DeviceStateMonitor deviceStateMonitor = DeviceStateMonitor.this;
                        if (i != 0) {
                            str = "on";
                        }
                        deviceStateMonitor.log("Tethering ".concat(str), true);
                        messageObtainMessage = DeviceStateMonitor.this.obtainMessage(5);
                        messageObtainMessage.arg1 = i;
                        break;
                    case 1:
                        messageObtainMessage = DeviceStateMonitor.this.obtainMessage(4);
                        messageObtainMessage.arg1 = 0;
                        break;
                    case 2:
                        messageObtainMessage = DeviceStateMonitor.this.obtainMessage(4);
                        messageObtainMessage.arg1 = 1;
                        break;
                    case 3:
                        messageObtainMessage = DeviceStateMonitor.this.obtainMessage(3);
                        boolean zIsPowerSaveModeOn = DeviceStateMonitor.this.isPowerSaveModeOn();
                        messageObtainMessage.arg1 = zIsPowerSaveModeOn ? 1 : 0;
                        DeviceStateMonitor deviceStateMonitor2 = DeviceStateMonitor.this;
                        if (zIsPowerSaveModeOn) {
                            str = "on";
                        }
                        deviceStateMonitor2.log("Power Save mode ".concat(str), true);
                        break;
                    default:
                        DeviceStateMonitor.this.log("Unexpected broadcast intent: " + intent, false);
                        return;
                }
                DeviceStateMonitor.this.sendMessage(messageObtainMessage);
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mPhone = phone;
        this.mFeatureFlags = featureFlags;
        ((DisplayManager) phone.getContext().getSystemService("display")).registerDisplayListener(displayListener, null);
        this.mIsPowerSaveOn = isPowerSaveModeOn();
        this.mIsCharging = isDeviceCharging();
        this.mIsScreenOn = isScreenOn();
        this.mIsRadioOn = isRadioOn();
        this.mIsAutomotiveProjectionActive = isAutomotiveProjectionActive();
        this.mIsTetheringOn = false;
        this.mIsLowDataExpected = false;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.internal.telephony.DeviceStateMonitor$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.DeviceStateMonitor$$ExternalSyntheticLambda1
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
        this.mCarrierRequiredModemDisplayNetworkType = isModemDisplayNetworkTypeEnabled();
        log("DeviceStateMonitor mIsTetheringOn=" + this.mIsTetheringOn + ", mIsScreenOn=" + this.mIsScreenOn + ", mIsCharging=" + this.mIsCharging + ", mIsPowerSaveOn=" + this.mIsPowerSaveOn + ", mIsLowDataExpected=" + this.mIsLowDataExpected + ", mIsAutomotiveProjectionActive=" + this.mIsAutomotiveProjectionActive + ", mIsWifiConnected=" + this.mIsWifiConnected + ", mIsAlwaysSignalStrengthReportingEnabled=" + this.mIsAlwaysSignalStrengthReportingEnabled + ", mIsRadioOn=" + this.mIsRadioOn, false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
        intentFilter.addAction("android.os.action.CHARGING");
        intentFilter.addAction("android.os.action.DISCHARGING");
        intentFilter.addAction("android.net.conn.TETHER_STATE_CHANGED");
        phone.getContext().registerReceiver(broadcastReceiver, intentFilter, null, phone);
        phone.mCi.registerForRilConnected(this, 0, null);
        phone.mCi.registerForAvailable(this, 6, null);
        phone.mCi.registerForOn(this, 9, null);
        phone.mCi.registerForOffOrNotAvailable(this, 10, null);
        ((ConnectivityManager) phone.getContext().getSystemService("connectivity")).registerNetworkCallback(networkRequestBuild, networkCallback);
        MiuiPhoneStub.get(phone.getPhoneId()).notifyDeviceStateListeners(phone, this.mIsScreenOn, this.mIsCharging, this.mIsPowerSaveOn);
        ((UiModeManager) phone.getContext().getSystemService("uimode")).addOnProjectionStateChangedListener(1, phone.getContext().getMainExecutor(), new UiModeManager.OnProjectionStateChangedListener() { // from class: com.android.internal.telephony.DeviceStateMonitor$$ExternalSyntheticLambda2
            public final void onProjectionStateChanged(int i, Set set) {
                this.f$0.lambda$new$1(i, set);
            }
        });
        MiuiPhoneStub.get(phone.getPhoneId()).injectDeviceStateMonitor(phone, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        if (i == this.mPhone.getPhoneId()) {
            onUpdateDeviceState(11, isModemDisplayNetworkTypeEnabled());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i, Set set) {
        Message messageObtainMessage = obtainMessage(1);
        messageObtainMessage.arg1 = Math.min(set.size(), 1);
        sendMessage(messageObtainMessage);
    }

    protected boolean isLowDataExpected() {
        return ((this.mIsCharging || this.mIsTetheringOn || this.mIsScreenOn) && this.mIsRadioOn) ? false : true;
    }

    public int computeCellInfoMinInterval() {
        boolean z = this.mIsScreenOn;
        if (!z || this.mIsWifiConnected) {
            return (z && this.mIsCharging) ? 2000 : 10000;
        }
        return 2000;
    }

    private boolean shouldEnableSignalStrengthReports() {
        if (shouldEnableHighPowerConsumptionIndications()) {
            return true;
        }
        return this.mIsAlwaysSignalStrengthReportingEnabled && this.mIsRadioOn;
    }

    private boolean shouldEnableFullNetworkStateReports() {
        return shouldEnableNrTrackingIndications();
    }

    private boolean shouldEnableDataCallDormancyChangedReports() {
        return shouldEnableNrTrackingIndications();
    }

    private boolean shouldEnableLinkCapacityEstimateReports() {
        return shouldEnableHighPowerConsumptionIndications();
    }

    private boolean shouldEnablePhysicalChannelConfigReports() {
        return shouldEnableNrTrackingIndications();
    }

    private boolean shouldEnableDisplayNetworkTypeReports() {
        return this.mCarrierRequiredModemDisplayNetworkType && shouldEnableHighPowerConsumptionIndications();
    }

    private boolean shouldEnableBarringInfoReports() {
        return shouldEnableHighPowerConsumptionIndications();
    }

    public boolean shouldEnableHighPowerConsumptionIndications() {
        if (TelephonyBaseUtilsStub.isAutoDevice()) {
            return (this.mIsScreenOn || this.mIsTetheringOn || this.mIsAutomotiveProjectionActive) && this.mIsRadioOn;
        }
        return (this.mIsCharging || this.mIsScreenOn || this.mIsAutomotiveProjectionActive) && this.mIsRadioOn;
    }

    private boolean shouldEnableNrTrackingIndications() {
        int i = Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), "nr_nsa_tracking_screen_off_mode", 0);
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    return shouldEnableHighPowerConsumptionIndications();
                }
                return true;
            }
            if (this.mPhone.getServiceState().getNrState() == 3) {
                return true;
            }
        }
        return shouldEnableHighPowerConsumptionIndications();
    }

    public void setAlwaysReportSignalStrength(boolean z) {
        Message messageObtainMessage = obtainMessage(8);
        messageObtainMessage.arg1 = z ? 1 : 0;
        sendMessage(messageObtainMessage);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        log("handleMessage msg=" + message, false);
        int i = message.what;
        switch (i) {
            case 0:
            case 6:
                onReset();
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 8:
                onUpdateDeviceState(i, message.arg1 != 0);
                break;
            case 7:
                onUpdateDeviceState(i, message.arg1 != 0);
                break;
            case 9:
                onUpdateDeviceState(i, true);
                break;
            case 10:
                onUpdateDeviceState(i, false);
                break;
            default:
                boolean zIsNeedUpdateDeviceState = MiuiPhoneStub.get(this.mPhone.getPhoneId()).isNeedUpdateDeviceState(this.mPhone, message.what, null);
                int i2 = message.what;
                if (zIsNeedUpdateDeviceState) {
                    onUpdateDeviceState(i2, false);
                } else {
                    MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("Unexpected message arrives. msg = ", i2);
                }
                break;
        }
    }

    private void onUpdateDeviceState(int i, boolean z) {
        boolean zShouldEnableBarringInfoReports = shouldEnableBarringInfoReports();
        boolean zShouldEnableHighPowerConsumptionIndications = shouldEnableHighPowerConsumptionIndications();
        boolean z2 = this.mIsScreenOn;
        boolean z3 = this.mIsCharging;
        switch (i) {
            case 1:
                if (this.mIsAutomotiveProjectionActive == z) {
                    return;
                } else {
                    this.mIsAutomotiveProjectionActive = z;
                }
                break;
            case 2:
                if (z2 == z) {
                    return;
                } else {
                    this.mIsScreenOn = z;
                }
                break;
            case 3:
                if (this.mIsPowerSaveOn == z) {
                    return;
                }
                this.mIsPowerSaveOn = z;
                sendDeviceState(0, z);
                break;
            case 4:
                if (z3 == z) {
                    return;
                }
                this.mIsCharging = z;
                sendDeviceState(1, z);
                break;
            case 5:
                if (this.mIsTetheringOn == z) {
                    return;
                } else {
                    this.mIsTetheringOn = z;
                }
                break;
            case 6:
            default:
                if (!MiuiPhoneStub.get(this.mPhone.getPhoneId()).isNeedUpdateDeviceState(this.mPhone, i, null)) {
                    return;
                }
                break;
            case 7:
                if (this.mIsWifiConnected == z) {
                    return;
                } else {
                    this.mIsWifiConnected = z;
                }
                break;
            case 8:
                if (this.mIsAlwaysSignalStrengthReportingEnabled == z) {
                    return;
                } else {
                    this.mIsAlwaysSignalStrengthReportingEnabled = z;
                }
                break;
            case 9:
            case 10:
                if (this.mIsRadioOn == z) {
                    return;
                } else {
                    this.mIsRadioOn = z;
                }
                break;
            case 11:
                if (this.mCarrierRequiredModemDisplayNetworkType == z) {
                    return;
                } else {
                    this.mCarrierRequiredModemDisplayNetworkType = z;
                }
                break;
        }
        boolean zShouldEnableHighPowerConsumptionIndications2 = shouldEnableHighPowerConsumptionIndications();
        if (zShouldEnableHighPowerConsumptionIndications != zShouldEnableHighPowerConsumptionIndications2) {
            this.mPhone.notifyDeviceIdleStateChanged(!zShouldEnableHighPowerConsumptionIndications2);
        }
        int iComputeCellInfoMinInterval = computeCellInfoMinInterval();
        if (this.mCellInfoMinInterval != iComputeCellInfoMinInterval) {
            this.mCellInfoMinInterval = iComputeCellInfoMinInterval;
            setCellInfoMinInterval(iComputeCellInfoMinInterval);
            log("CellInfo Min Interval Updated to " + iComputeCellInfoMinInterval, true);
        }
        if (this.mIsLowDataExpected != isLowDataExpected()) {
            boolean z4 = !this.mIsLowDataExpected;
            this.mIsLowDataExpected = z4;
            sendDeviceState(2, z4);
        }
        int i2 = 32;
        if (TelephonyBaseUtilsStub.isAutoDevice() && !this.mIsScreenOn) {
            i2 = 0;
        }
        if (((IHyperUiccManager) HyperTelephonyFactory.getInstance().initModule(IHyperUiccManager.DEFAULT, new Object[0])).shouldEnableSignalStrengthReports(this.mIsCharging, this.mIsScreenOn, this.mIsTetheringOn, this.mIsAutomotiveProjectionActive, this.mIsRadioOn, this.mIsAlwaysSignalStrengthReportingEnabled)) {
            i2 |= 1;
        }
        if (shouldEnableFullNetworkStateReports()) {
            i2 |= 2;
        }
        if (shouldEnableDataCallDormancyChangedReports()) {
            i2 |= 4;
        }
        if (shouldEnableLinkCapacityEstimateReports()) {
            i2 |= 8;
        }
        if (shouldEnablePhysicalChannelConfigReports()) {
            i2 |= 16;
        }
        boolean zShouldEnableBarringInfoReports2 = shouldEnableBarringInfoReports();
        if (zShouldEnableBarringInfoReports2) {
            i2 |= 64;
        }
        if (shouldEnableDisplayNetworkTypeReports()) {
            i2 |= 128;
        }
        int i3 = i2 & 16;
        if (i3 != (this.mUnsolicitedResponseFilter & 16)) {
            this.mPhysicalChannelConfigRegistrants.notifyResult(Boolean.valueOf(i3 != 0));
        }
        setUnsolResponseFilter(MiuiPhoneStub.get(this.mPhone.getPhoneId()).generatorDeviceStateNewFilter(this.mPhone, i, i2), false);
        if (zShouldEnableBarringInfoReports2 && !zShouldEnableBarringInfoReports) {
            this.mPhone.mCi.getBarringInfo(null);
        }
        boolean zShouldEnableSignalStrengthReports = shouldEnableSignalStrengthReports();
        RegistrantList registrantList = this.mSignalStrengthReportDecisionCallbackRegistrants;
        if (zShouldEnableSignalStrengthReports) {
            registrantList.notifyResult(Boolean.TRUE);
        } else {
            registrantList.notifyResult(Boolean.FALSE);
        }
        boolean z5 = this.mIsScreenOn;
        if (z2 != z5) {
            this.mScreenStateRegistrants.notifyResult(Boolean.valueOf(z5));
        }
        boolean z6 = this.mIsCharging;
        if (z3 != z6) {
            this.mChargingStateRegistrants.notifyResult(Boolean.valueOf(z6));
        }
        MiuiPhoneStub.get(this.mPhone.getPhoneId()).notifyDeviceStateListeners(this.mPhone, i, this.mIsScreenOn, this.mIsCharging, this.mIsPowerSaveOn);
    }

    private void onReset() {
        log("onReset.", true);
        sendDeviceState(1, this.mIsCharging);
        sendDeviceState(2, this.mIsLowDataExpected);
        sendDeviceState(0, this.mIsPowerSaveOn);
        setUnsolResponseFilter(this.mUnsolicitedResponseFilter, true);
        setLinkCapacityReportingCriteria();
        setCellInfoMinInterval(this.mCellInfoMinInterval);
    }

    private String deviceTypeToString(int i) {
        if (i == 0) {
            return "POWER_SAVE_MODE";
        }
        if (i == 1) {
            return "CHARGING_STATE";
        }
        if (i == 2) {
            return "LOW_DATA_EXPECTED";
        }
        return SatelliteConstants.DEFAULT_PLMN;
    }

    private void sendDeviceState(int i, boolean z) {
        log("send type: " + deviceTypeToString(i) + ", state=" + z, true);
        this.mPhone.mCi.sendDeviceState(i, z, null);
    }

    private void setUnsolResponseFilter(int i, boolean z) {
        if (z || i != this.mUnsolicitedResponseFilter) {
            log("old filter: " + this.mUnsolicitedResponseFilter + ", new filter: " + i, true);
            this.mPhone.mCi.setUnsolResponseFilter(i, null);
            this.mUnsolicitedResponseFilter = i;
        }
    }

    private void setLinkCapacityReportingCriteria() {
        Phone phone = this.mPhone;
        int[] iArr = LINK_CAPACITY_DOWNLINK_THRESHOLDS;
        int[] iArr2 = LINK_CAPACITY_UPLINK_THRESHOLDS;
        phone.setLinkCapacityReportingCriteria(iArr, iArr2, 1);
        this.mPhone.setLinkCapacityReportingCriteria(iArr, iArr2, 2);
        this.mPhone.setLinkCapacityReportingCriteria(iArr, iArr2, 3);
        this.mPhone.setLinkCapacityReportingCriteria(iArr, iArr2, 4);
        if (this.mPhone.getHalVersion(4).greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) {
            this.mPhone.setLinkCapacityReportingCriteria(iArr, iArr2, 6);
        }
    }

    private void setCellInfoMinInterval(int i) {
        this.mPhone.setCellInfoMinInterval(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPowerSaveModeOn() {
        boolean zIsPowerSaveMode = ((PowerManager) this.mPhone.getContext().getSystemService("power")).isPowerSaveMode();
        log("isPowerSaveModeOn=" + zIsPowerSaveMode, true);
        return zIsPowerSaveMode;
    }

    private boolean isDeviceCharging() {
        boolean zIsCharging = ((BatteryManager) this.mPhone.getContext().getSystemService("batterymanager")).isCharging();
        log("isDeviceCharging=" + zIsCharging, true);
        return zIsCharging;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isScreenOn() {
        Display[] displays = ((DisplayManager) this.mPhone.getContext().getSystemService("display")).getDisplays();
        if (displays != null) {
            for (Display display : displays) {
                if (display.getState() == 2) {
                    log("Screen on for display=" + display, true);
                    return true;
                }
            }
            log("Screens all off", true);
            return false;
        }
        log("No displays found", true);
        return false;
    }

    private boolean isRadioOn() {
        return this.mPhone.isRadioOn();
    }

    private boolean isAutomotiveProjectionActive() {
        UiModeManager uiModeManager = (UiModeManager) this.mPhone.getContext().getSystemService("uimode");
        if (uiModeManager == null) {
            return false;
        }
        boolean z = (uiModeManager.getActiveProjectionTypes() & 1) != 0;
        log("isAutomotiveProjectionActive=" + z, true);
        return z;
    }

    private boolean isModemDisplayNetworkTypeEnabled() {
        PersistableBundle configForSubId;
        PersistableBundle defaultConfig = CarrierConfigManager.getDefaultConfig();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager != null && (configForSubId = carrierConfigManager.getConfigForSubId(this.mPhone.getSubId())) != null) {
            defaultConfig = configForSubId;
        }
        return defaultConfig.getBoolean("use_modem_display_network_type_bool");
    }

    public void registerForPhysicalChannelConfigNotifChanged(Handler handler, int i, Object obj) {
        this.mPhysicalChannelConfigRegistrants.add(new Registrant(handler, i, obj));
    }

    public void unregisterForPhysicalChannelConfigNotifChanged(Handler handler) {
        this.mPhysicalChannelConfigRegistrants.remove(handler);
    }

    public void registerForSignalStrengthReportDecision(Handler handler, int i, Object obj) {
        this.mSignalStrengthReportDecisionCallbackRegistrants.add(new Registrant(handler, i, obj));
    }

    public void unregisterForScreenStateChanged(Handler handler) {
        this.mScreenStateRegistrants.remove(handler);
    }

    public void registerForScreenStateChanged(Handler handler, int i, Object obj) {
        this.mScreenStateRegistrants.add(new Registrant(handler, i, obj));
        this.mScreenStateRegistrants.notifyResult(Boolean.valueOf(this.mIsScreenOn));
    }

    public void unregisterForSignalStrengthReportDecision(Handler handler) {
        this.mSignalStrengthReportDecisionCallbackRegistrants.remove(handler);
    }

    public void registerForChargingStateChanged(Handler handler, int i, Object obj) {
        this.mChargingStateRegistrants.add(new Registrant(handler, i, obj));
        this.mChargingStateRegistrants.notifyResult(Boolean.valueOf(this.mIsCharging));
    }

    public void unregisterForChargingStateChanged(Handler handler) {
        this.mChargingStateRegistrants.remove(handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str, boolean z) {
        if (z) {
            this.mLocalLog.log(str);
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("mIsTetheringOn=" + this.mIsTetheringOn);
        indentingPrintWriter.println("mIsScreenOn=" + this.mIsScreenOn);
        indentingPrintWriter.println("mIsCharging=" + this.mIsCharging);
        indentingPrintWriter.println("mIsPowerSaveOn=" + this.mIsPowerSaveOn);
        indentingPrintWriter.println("mIsLowDataExpected=" + this.mIsLowDataExpected);
        indentingPrintWriter.println("mIsAutomotiveProjectionActive=" + this.mIsAutomotiveProjectionActive);
        indentingPrintWriter.println("mUnsolicitedResponseFilter=" + this.mUnsolicitedResponseFilter);
        indentingPrintWriter.println("mIsWifiConnected=" + this.mIsWifiConnected);
        indentingPrintWriter.println("mIsAlwaysSignalStrengthReportingEnabled=" + this.mIsAlwaysSignalStrengthReportingEnabled);
        indentingPrintWriter.println("mIsRadioOn=" + this.mIsRadioOn);
        indentingPrintWriter.println("Local logs:");
        indentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.flush();
    }
}
