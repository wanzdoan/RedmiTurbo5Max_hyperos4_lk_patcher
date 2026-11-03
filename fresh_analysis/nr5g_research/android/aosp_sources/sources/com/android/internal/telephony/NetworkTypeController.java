package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.CarrierConfigManager;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.ServiceState;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.telephony.data.EpsQos;
import android.telephony.data.NrQos;
import android.telephony.data.QosBearerSession;
import android.text.TextUtils;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/* JADX INFO: loaded from: classes.dex */
public class NetworkTypeController extends StateMachine {
    private static final ModemDisplayNetworkType DEFAULT_MODEM_NETWORK_TYPE;
    public static final int EVENT_UPDATE = 0;
    protected final Set<Integer> mAdditionalNrAdvancedBands;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private ConnectivityManager mConnectivityManager;
    private final DataNetworkController.DataNetworkControllerCallback mDataNetworkControllerCallback;
    public final DisplayInfoController mDisplayInfoController;
    protected boolean mDoesPccListIndicateIdle;
    private boolean mEnableNrAdvancedWhileRoaming;
    private boolean mEndcStatus;
    private final FeatureFlags mFeatureFlags;
    private final IdleState mIdleState;
    private boolean mInVoiceCall;
    protected boolean mIncludeLteForNrAdvancedThresholdBandwidth;
    private final BroadcastReceiver mIntentReceiver;
    private boolean mIsDeviceIdleMode;
    private boolean mIsNrAdvancedAllowedByPco;
    private boolean mIsPhysicalChannelConfig16Supported;
    protected boolean mIsPhysicalChannelConfigOn;
    private boolean mIsPrimaryTimerActive;
    private boolean mIsSatelliteConstrainedData;
    private boolean mIsSatelliteNetworkCallbackRegistered;
    private boolean mIsSecondaryTimerActive;
    private boolean mIsTimerResetEnabledForLegacyStateRrcIdle;
    private boolean mIsTimerResetEnabledOnEndcToSaTransit;
    private boolean mIsTimerResetEnabledOnPlmnChanges;
    private boolean mIsTimerResetEnabledOnVoiceQos;
    private boolean mIsUsingUserDataForRrcDetection;
    private int mLastAnchorNrCellId;
    private boolean mLastShownNrDueToAdvancedBand;
    private final LegacyState mLegacyState;
    private final LteConnectedState mLteConnectedState;
    private String mLteEnhancedPattern;
    protected int mLtePlusThresholdBandwidth;
    protected IHyperNetworkTypeManager mManager;
    private ModemDisplayNetworkType mModemOverrideNetworkType;
    private final ConnectivityManager.NetworkCallback mNetworkCallback;
    private int mNrAdvancedBandsSecondaryTimer;
    private int mNrAdvancedCapablePcoId;
    private int mNrAdvancedPciChangeSecondaryTimer;
    private boolean mNrAdvancedRequiresSingleCcAboveBandwidthThreshold;
    protected int mNrAdvancedThresholdBandwidth;
    private final NrConnectedAdvancedState mNrConnectedAdvancedState;
    private final NrConnectedState mNrConnectedState;
    private final NrIdleState mNrIdleState;
    private int mOverrideNetworkType;
    public Map<String, OverrideTimerRule> mOverrideTimerRules;
    private boolean mPciChangedDuringPrimaryTimer;
    public final Phone mPhone;
    protected List<PhysicalChannelConfig> mPhysicalChannelConfigs;
    protected int mPhysicalLinkStatus;
    private String mPreviousState;
    private boolean mPrimaryCellChangedWhileIdle;
    private String mPrimaryTimerState;
    private boolean mRatchetPccFieldsForSameAnchorNrCell;
    private Set mRatchetedNrBands;
    private List mRatchetedNrBandwidths;
    private long mSecondaryTimerExpireTimestamp;
    private String mSecondaryTimerState;
    protected ServiceState mServiceState;
    private boolean mUseModemDisplayNetworkType;
    private static final String[] ALL_STATES = {"connected_mmwave", PhoneInternalInterface.REASON_CONNECTED, "connected_rrc_idle", "not_restricted_rrc_idle", "not_restricted_rrc_con", "restricted", AccessNetworksManager.IWLAN_OPERATION_MODE_LEGACY};
    private static final String[] sEvents = {"EVENT_UPDATE", "EVENT_QUIT", "EVENT_INITIALIZE", "EVENT_SERVICE_STATE_CHANGED", "EVENT_PHYSICAL_LINK_STATUS_CHANGED", "EVENT_PHYSICAL_CHANNEL_CONFIG_NOTIF_CHANGED", "EVENT_CARRIER_CONFIG_CHANGED", "EVENT_PRIMARY_TIMER_EXPIRED", "EVENT_SECONDARY_TIMER_EXPIRED", "EVENT_RADIO_OFF_OR_UNAVAILABLE", "EVENT_PREFERRED_NETWORK_MODE_CHANGED", "EVENT_PHYSICAL_CHANNEL_CONFIGS_CHANGED", "EVENT_DEVICE_IDLE_MODE_CHANGED", "EVENT_QOS_SESSION_CHANGED", "EVENT_MODEM_DISPLAY_NETWORK_TYPE_OVERRIDE", "EVENT_UPDATE_BANDWIDTH_CONSTRAINED_STATUS"};

    protected boolean isLte(int i) {
        return i == 13 || i == 19;
    }

    protected void onParseCarrierConfigs(PersistableBundle persistableBundle) {
    }

    static {
        int i = 0;
        DEFAULT_MODEM_NETWORK_TYPE = new ModemDisplayNetworkType(i, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBandwidthConstrainedCapabilitySupported(NetworkCapabilities networkCapabilities) {
        try {
            return networkCapabilities.hasTransport(10) && !networkCapabilities.hasCapability(37);
        } catch (Exception unused) {
            log("NET_CAPABILITY_NOT_BANDWIDTH_CONSTRAINED not supported ");
            return false;
        }
    }

    private static final class ModemDisplayNetworkType extends Record {
        private final int dataNetworkType;
        private final int overrideNetworkType;

        private /* synthetic */ boolean $record$equals(Object obj) {
            if (!(obj instanceof ModemDisplayNetworkType)) {
                return false;
            }
            ModemDisplayNetworkType modemDisplayNetworkType = (ModemDisplayNetworkType) obj;
            return this.dataNetworkType == modemDisplayNetworkType.dataNetworkType && this.overrideNetworkType == modemDisplayNetworkType.overrideNetworkType;
        }

        private /* synthetic */ Object[] $record$getFieldsAsObjects() {
            return new Object[]{Integer.valueOf(this.dataNetworkType), Integer.valueOf(this.overrideNetworkType)};
        }

        private ModemDisplayNetworkType(int i, int i2) {
            this.dataNetworkType = i;
            this.overrideNetworkType = i2;
        }

        public int dataNetworkType() {
            return this.dataNetworkType;
        }

        @Override // java.lang.Record
        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return NetworkTypeController$ModemDisplayNetworkType$$ExternalSyntheticRecord1.m(this.dataNetworkType, this.overrideNetworkType);
        }

        public int overrideNetworkType() {
            return this.overrideNetworkType;
        }

        @Override // java.lang.Record
        public final String toString() {
            return NetworkTypeController$ModemDisplayNetworkType$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), ModemDisplayNetworkType.class, "dataNetworkType;overrideNetworkType");
        }

        public boolean isOverridden() {
            return (this.dataNetworkType == 0 && this.overrideNetworkType == 0) ? false : true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBandwidthConstrainedStatus(boolean z) {
        if (z != this.mIsSatelliteConstrainedData) {
            this.mIsSatelliteConstrainedData = z;
            log("Reset timers because satellite constrained data status changed to " + this.mIsSatelliteConstrainedData);
            resetAllTimers();
            transitionToCurrentState();
        }
    }

    public NetworkTypeController(Phone phone, DisplayInfoController displayInfoController, FeatureFlags featureFlags) {
        super("NetworkTypeController", displayInfoController);
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.NetworkTypeController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                action.hashCode();
                if (action.equals("android.os.action.DEVICE_IDLE_MODE_CHANGED")) {
                    NetworkTypeController.this.sendMessage(12);
                }
            }
        };
        this.mCarrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.NetworkTypeController.2
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                if (i == NetworkTypeController.this.mPhone.getPhoneId()) {
                    NetworkTypeController.this.sendMessage(6);
                }
            }
        };
        Handler handler = getHandler();
        Objects.requireNonNull(handler);
        this.mDataNetworkControllerCallback = new DataNetworkController.DataNetworkControllerCallback(new NetworkTypeController$$ExternalSyntheticLambda2(handler)) { // from class: com.android.internal.telephony.NetworkTypeController.3
            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onQosSessionsChanged(List<QosBearerSession> list) {
                NetworkTypeController networkTypeController = NetworkTypeController.this;
                networkTypeController.sendMessage(networkTypeController.obtainMessage(13, list));
            }

            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onNrAdvancedCapableByPcoChanged(boolean z) {
                if (NetworkTypeController.this.mNrAdvancedCapablePcoId <= 0) {
                    return;
                }
                NetworkTypeController.this.log("mIsNrAdvancedAllowedByPco=" + z);
                NetworkTypeController.this.mIsNrAdvancedAllowedByPco = z;
                NetworkTypeController.this.sendMessage(0);
            }

            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onPhysicalLinkStatusChanged(int i) {
                if (NetworkTypeController.this.isUsingPhysicalChannelConfigForRrcDetection()) {
                    return;
                }
                NetworkTypeController networkTypeController = NetworkTypeController.this;
                networkTypeController.sendMessage(networkTypeController.obtainMessage(4, i));
            }
        };
        this.mOverrideTimerRules = new HashMap();
        this.mLteEnhancedPattern = "";
        this.mAdditionalNrAdvancedBands = new HashSet();
        this.mIsNrAdvancedAllowedByPco = false;
        this.mNrAdvancedCapablePcoId = 0;
        this.mIsUsingUserDataForRrcDetection = false;
        this.mEnableNrAdvancedWhileRoaming = true;
        this.mIsDeviceIdleMode = false;
        this.mPrimaryCellChangedWhileIdle = false;
        this.mPciChangedDuringPrimaryTimer = false;
        this.mModemOverrideNetworkType = DEFAULT_MODEM_NETWORK_TYPE;
        this.mRatchetedNrBands = new HashSet();
        this.mLastShownNrDueToAdvancedBand = false;
        this.mRatchetedNrBandwidths = new ArrayList();
        this.mLastAnchorNrCellId = -1;
        this.mDoesPccListIndicateIdle = false;
        this.mInVoiceCall = false;
        this.mIsSatelliteConstrainedData = false;
        this.mIsSatelliteNetworkCallbackRegistered = false;
        this.mEndcStatus = false;
        this.mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.internal.telephony.NetworkTypeController.4
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                NetworkCapabilities networkCapabilities;
                if (NetworkTypeController.this.mConnectivityManager == null || (networkCapabilities = NetworkTypeController.this.mConnectivityManager.getNetworkCapabilities(network)) == null) {
                    return;
                }
                NetworkTypeController networkTypeController = NetworkTypeController.this;
                networkTypeController.sendMessage(15, Boolean.valueOf(networkTypeController.isBandwidthConstrainedCapabilitySupported(networkCapabilities)));
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                NetworkTypeController networkTypeController = NetworkTypeController.this;
                networkTypeController.sendMessage(15, Boolean.valueOf(networkTypeController.isBandwidthConstrainedCapabilitySupported(networkCapabilities)));
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                NetworkTypeController.this.sendMessage(15, Boolean.FALSE);
            }
        };
        LegacyState legacyState = new LegacyState();
        this.mLegacyState = legacyState;
        IdleState idleState = new IdleState();
        this.mIdleState = idleState;
        LteConnectedState lteConnectedState = new LteConnectedState();
        this.mLteConnectedState = lteConnectedState;
        NrIdleState nrIdleState = new NrIdleState();
        this.mNrIdleState = nrIdleState;
        NrConnectedState nrConnectedState = new NrConnectedState();
        this.mNrConnectedState = nrConnectedState;
        NrConnectedAdvancedState nrConnectedAdvancedState = new NrConnectedAdvancedState();
        this.mNrConnectedAdvancedState = nrConnectedAdvancedState;
        this.mPhone = phone;
        this.mFeatureFlags = featureFlags;
        this.mDisplayInfoController = displayInfoController;
        this.mOverrideNetworkType = 0;
        this.mIsPhysicalChannelConfigOn = true;
        this.mPrimaryTimerState = "";
        this.mSecondaryTimerState = "";
        this.mPreviousState = "";
        State defaultState = new DefaultState();
        addState(defaultState);
        addState(legacyState, defaultState);
        addState(idleState, defaultState);
        addState(lteConnectedState, defaultState);
        addState(nrIdleState, defaultState);
        addState(nrConnectedState, defaultState);
        addState(nrConnectedAdvancedState, defaultState);
        setInitialState(defaultState);
        start();
        this.mServiceState = phone.getServiceStateTracker().getServiceState();
        this.mPhysicalChannelConfigs = phone.getServiceStateTracker().getPhysicalChannelConfigList();
        sendMessage(2);
        this.mManager = (IHyperNetworkTypeManager) HyperTelephonyFactory.getInstance().initModule(IHyperNetworkTypeManager.DEFAULT, phone, this);
    }

    public synchronized void registerForSatelliteNetwork() {
        if (!this.mIsSatelliteNetworkCallbackRegistered) {
            this.mIsSatelliteNetworkCallbackRegistered = true;
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addCapability(12);
            try {
                builder.removeCapability(37);
            } catch (Exception unused) {
                log("NET_CAPABILITY_NOT_BANDWIDTH_CONSTRAINED not supported ");
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mPhone.getContext().getSystemService("connectivity");
            this.mConnectivityManager = connectivityManager;
            if (connectivityManager != null) {
                connectivityManager.registerBestMatchingNetworkCallback(builder.build(), this.mNetworkCallback, getHandler());
            } else {
                loge("network callback not registered");
            }
        }
    }

    public int getOverrideNetworkType() {
        if (this.mModemOverrideNetworkType.isOverridden()) {
            return this.mModemOverrideNetworkType.overrideNetworkType();
        }
        return this.mOverrideNetworkType;
    }

    public int getDataNetworkType() {
        if (this.mModemOverrideNetworkType.isOverridden()) {
            return this.mModemOverrideNetworkType.dataNetworkType();
        }
        return getRawDataNetworkType();
    }

    private int getRawDataNetworkType() {
        NetworkRegistrationInfo networkRegistrationInfo = this.mServiceState.getNetworkRegistrationInfo(2, 1);
        if (networkRegistrationInfo == null) {
            return 0;
        }
        return networkRegistrationInfo.getAccessNetworkTechnology();
    }

    public boolean getSatelliteConstrainedData() {
        return this.mIsSatelliteConstrainedData;
    }

    public boolean areAnyTimersActive() {
        return this.mIsPrimaryTimerActive || this.mIsSecondaryTimerActive;
    }

    protected void registerForAllEvents() {
        this.mPhone.registerForRadioOffOrNotAvailable(getHandler(), 9, null);
        this.mPhone.registerForPreferredNetworkTypeChanged(getHandler(), 10, null);
        this.mPhone.registerForPhysicalChannelConfig(getHandler(), 11, null);
        this.mPhone.mCi.registerForDisplayNetworkTypeChanged(getHandler(), 14, null);
        this.mPhone.getServiceStateTracker().registerForServiceStateChanged(getHandler(), 3, null);
        this.mPhone.getDeviceStateMonitor().registerForPhysicalChannelConfigNotifChanged(getHandler(), 5, null);
        this.mPhone.getDataNetworkController().registerDataNetworkControllerCallback(this.mDataNetworkControllerCallback);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        this.mPhone.getContext().registerReceiver(this.mIntentReceiver, intentFilter, null, this.mPhone);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), this.mCarrierConfigChangeListener);
        }
    }

    protected void unRegisterForAllEvents() {
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener;
        this.mPhone.unregisterForRadioOffOrNotAvailable(getHandler());
        this.mPhone.unregisterForPreferredNetworkTypeChanged(getHandler());
        this.mPhone.getServiceStateTracker().unregisterForServiceStateChanged(getHandler());
        this.mPhone.getDeviceStateMonitor().unregisterForPhysicalChannelConfigNotifChanged(getHandler());
        this.mPhone.mCi.unregisterForDisplayNetworkTypeChanged(getHandler());
        this.mPhone.getDataNetworkController().unregisterDataNetworkControllerCallback(this.mDataNetworkControllerCallback);
        this.mPhone.getContext().unregisterReceiver(this.mIntentReceiver);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager == null || (carrierConfigChangeListener = this.mCarrierConfigChangeListener) == null) {
            return;
        }
        carrierConfigManager.unregisterCarrierConfigChangeListener(carrierConfigChangeListener);
    }

    public void parseCarrierConfigs() {
        PersistableBundle configForSubId;
        PersistableBundle defaultConfig = CarrierConfigManager.getDefaultConfig();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null && (configForSubId = carrierConfigManager.getConfigForSubId(this.mPhone.getSubId())) != null) {
            defaultConfig = configForSubId;
        }
        onParseCarrierConfigs(defaultConfig);
        this.mLteEnhancedPattern = defaultConfig.getString("show_carrier_data_icon_pattern_string");
        this.mIsTimerResetEnabledForLegacyStateRrcIdle = defaultConfig.getBoolean("nr_timers_reset_if_non_endc_and_rrc_idle_bool");
        this.mIsTimerResetEnabledOnPlmnChanges = defaultConfig.getBoolean("nr_timers_reset_on_plmn_change_bool");
        this.mIsTimerResetEnabledOnVoiceQos = defaultConfig.getBoolean("nr_timers_reset_on_voice_qos_bool");
        this.mIsTimerResetEnabledOnEndcToSaTransit = defaultConfig.getBoolean("nr_timers_reset_on_endc_to_sa_transit_bool");
        this.mLtePlusThresholdBandwidth = defaultConfig.getInt("lte_plus_threshold_bandwidth_khz_int");
        this.mNrAdvancedThresholdBandwidth = defaultConfig.getInt("nr_advanced_threshold_bandwidth_khz_int");
        this.mNrAdvancedRequiresSingleCcAboveBandwidthThreshold = defaultConfig.getBoolean("nr_advanced_requires_single_cc_above_bandwidth_threshold");
        this.mIncludeLteForNrAdvancedThresholdBandwidth = defaultConfig.getBoolean("include_lte_for_nr_advanced_threshold_bandwidth_bool");
        this.mRatchetPccFieldsForSameAnchorNrCell = defaultConfig.getBoolean("ratchet_nr_advanced_bandwidth_if_rrc_idle_bool");
        this.mUseModemDisplayNetworkType = defaultConfig.getBoolean("use_modem_display_network_type_bool");
        this.mEnableNrAdvancedWhileRoaming = defaultConfig.getBoolean("enable_nr_advanced_for_roaming_bool");
        this.mAdditionalNrAdvancedBands.clear();
        int[] intArray = defaultConfig.getIntArray("additional_nr_advanced_bands_int_array");
        if (intArray != null) {
            IntStream intStreamStream = Arrays.stream(intArray);
            Set<Integer> set = this.mAdditionalNrAdvancedBands;
            Objects.requireNonNull(set);
            intStreamStream.forEach(new NetworkTypeController$$ExternalSyntheticLambda3(set));
        }
        this.mNrAdvancedCapablePcoId = defaultConfig.getInt("nr_advanced_capable_pco_id_int");
        this.mIsUsingUserDataForRrcDetection = defaultConfig.getBoolean("lte_endc_using_user_data_for_rrc_detection_bool");
        this.mNrAdvancedBandsSecondaryTimer = defaultConfig.getInt("nr_advanced_bands_secondary_timer_seconds_int");
        this.mNrAdvancedPciChangeSecondaryTimer = defaultConfig.getInt("nr_advanced_pci_change_secondary_timer_seconds_int");
        createTimerRules(defaultConfig.getString("5g_icon_configuration_string"), defaultConfig.getString("5g_icon_display_grace_period_string"), defaultConfig.getString("5g_icon_display_secondary_grace_period_string"));
        this.mIsPhysicalChannelConfig16Supported = ((TelephonyManager) this.mPhone.getContext().getSystemService(TelephonyManager.class)).isRadioInterfaceCapabilitySupported("CAPABILITY_PHYSICAL_CHANNEL_CONFIG_1_6_SUPPORTED");
        updatePhysicalChannelConfigs(this.mPhone.getServiceStateTracker().getPhysicalChannelConfigList());
        if (isUsingPhysicalChannelConfigForRrcDetection()) {
            this.mPhysicalLinkStatus = getPhysicalLinkStatusFromPhysicalChannelConfig();
        }
        if (!this.mModemOverrideNetworkType.isOverridden() || this.mUseModemDisplayNetworkType) {
            return;
        }
        this.mModemOverrideNetworkType = DEFAULT_MODEM_NETWORK_TYPE;
        this.mDisplayInfoController.updateTelephonyDisplayInfo();
    }

    private void createTimerRules(String str, String str2, String str3) {
        char c;
        int i;
        int i2;
        HashMap map = new HashMap();
        char c2 = 2;
        char c3 = 1;
        int i3 = 0;
        if (!TextUtils.isEmpty(str)) {
            for (String str4 : str.trim().split(",")) {
                String[] strArrSplit = str4.trim().toLowerCase(Locale.ROOT).split(":");
                if (strArrSplit.length != 2) {
                    loge("Invalid 5G icon configuration, config = ".concat(str4));
                } else {
                    if (strArrSplit[1].equals("5g")) {
                        i2 = 3;
                    } else if (strArrSplit[1].equals("5g_plus")) {
                        i2 = 5;
                    } else {
                        loge("Invalid 5G icon = " + strArrSplit[1]);
                        i2 = 0;
                    }
                    String str5 = strArrSplit[0];
                    map.put(str5, new OverrideTimerRule(str5, i2));
                }
            }
        }
        for (String str6 : ALL_STATES) {
            if (!map.containsKey(str6)) {
                map.put(str6, new OverrideTimerRule(str6, 0));
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            String[] strArrSplit2 = str2.trim().split(";");
            int length = strArrSplit2.length;
            int i4 = 0;
            while (i4 < length) {
                String str7 = strArrSplit2[i4];
                String[] strArrSplit3 = str7.trim().toLowerCase(Locale.ROOT).split(",");
                if (strArrSplit3.length != 3) {
                    loge("Invalid 5G icon timer configuration, config = ".concat(str7));
                } else {
                    try {
                        int i5 = Integer.parseInt(strArrSplit3[c2]);
                        if (strArrSplit3[i3].equals("any")) {
                            String[] strArr = ALL_STATES;
                            int length2 = strArr.length;
                            int i6 = i3;
                            while (true) {
                                c = c3;
                                if (i6 >= length2) {
                                    break;
                                }
                                ((OverrideTimerRule) map.get(strArr[i6])).addTimer(strArrSplit3[c], i5);
                                i6++;
                                c3 = c;
                                i3 = i3;
                            }
                            i = i3;
                        } else {
                            c2 = c2;
                            c = c3;
                            i = i3;
                            ((OverrideTimerRule) map.get(strArrSplit3[i])).addTimer(strArrSplit3[c], i5);
                        }
                    } catch (NumberFormatException unused) {
                        c = c3;
                    }
                    i4++;
                    c2 = c2;
                    c3 = c;
                    i3 = i;
                }
                c = c3;
                i = i3;
                i4++;
                c2 = c2;
                c3 = c;
                i3 = i;
            }
        }
        char c4 = c2;
        char c5 = c3;
        int i7 = i3;
        if (!TextUtils.isEmpty(str3)) {
            String[] strArrSplit4 = str3.trim().split(";");
            int length3 = strArrSplit4.length;
            for (int i8 = i7; i8 < length3; i8++) {
                String str8 = strArrSplit4[i8];
                String[] strArrSplit5 = str8.trim().toLowerCase(Locale.ROOT).split(",");
                if (strArrSplit5.length != 3) {
                    loge("Invalid 5G icon secondary timer configuration, config = ".concat(str8));
                } else {
                    try {
                        int i9 = Integer.parseInt(strArrSplit5[c4]);
                        if (strArrSplit5[i7].equals("any")) {
                            String[] strArr2 = ALL_STATES;
                            int length4 = strArr2.length;
                            for (int i10 = i7; i10 < length4; i10++) {
                                ((OverrideTimerRule) map.get(strArr2[i10])).addSecondaryTimer(strArrSplit5[c5], i9);
                            }
                        } else {
                            ((OverrideTimerRule) map.get(strArrSplit5[i7])).addSecondaryTimer(strArrSplit5[c5], i9);
                        }
                    } catch (NumberFormatException unused2) {
                    }
                }
            }
        }
        OverrideTimerRule overrideTimerRule = (OverrideTimerRule) map.get(PhoneInternalInterface.REASON_CONNECTED);
        if (!((OverrideTimerRule) map.get("connected_rrc_idle")).isDefined() && overrideTimerRule.isDefined()) {
            OverrideTimerRule overrideTimerRule2 = new OverrideTimerRule("connected_rrc_idle", overrideTimerRule.mOverrideType);
            for (Map.Entry entry : overrideTimerRule2.mPrimaryTimers.entrySet()) {
                overrideTimerRule2.addTimer((String) entry.getKey(), ((Integer) entry.getValue()).intValue());
            }
            for (Map.Entry entry2 : overrideTimerRule2.mSecondaryTimers.entrySet()) {
                overrideTimerRule2.addSecondaryTimer((String) entry2.getKey(), ((Integer) entry2.getValue()).intValue());
            }
            map.put("connected_rrc_idle", overrideTimerRule2);
        }
        this.mOverrideTimerRules = map;
        log("mOverrideTimerRules: " + this.mOverrideTimerRules);
    }

    protected void updateOverrideNetworkType() {
        if (this.mIsPrimaryTimerActive || this.mIsSecondaryTimerActive) {
            log("Skip updating override network type since timer is active.");
            return;
        }
        this.mOverrideNetworkType = getCurrentOverrideNetworkType();
        if (MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).isDelayUpdateTelephonyDisplayInfo(this.mDisplayInfoController, this.mOverrideNetworkType)) {
            log("Skip updating override network type since MIUI Rat PingPang policy.");
        } else {
            this.mDisplayInfoController.updateTelephonyDisplayInfo();
        }
    }

    protected int getCurrentOverrideNetworkType() {
        int rawDataNetworkType = getRawDataNetworkType();
        int lteDisplayType = 0;
        boolean z = isLte(rawDataNetworkType) && this.mServiceState.getNrState() != 0;
        boolean z2 = rawDataNetworkType == 20;
        if (this.mIsPhysicalChannelConfigOn && (z || z2)) {
            lteDisplayType = getNrDisplayType(z2);
            if (lteDisplayType == 0 && !z2) {
                lteDisplayType = getLteDisplayType();
            }
        } else if (isLte(rawDataNetworkType)) {
            lteDisplayType = getLteDisplayType();
        }
        IHyperNetworkTypeManager iHyperNetworkTypeManager = this.mManager;
        return iHyperNetworkTypeManager != null ? iHyperNetworkTypeManager.getCurrentOverrideNetworkType(isLte(rawDataNetworkType), z, z2, lteDisplayType) : lteDisplayType;
    }

    protected int getNrDisplayType(boolean z) {
        int i;
        if ((((long) this.mPhone.getCachedAllowedNetworkTypesBitmask()) & 524288) == 0) {
            return 0;
        }
        ArrayList arrayList = new ArrayList();
        if (z) {
            if (isNrAdvanced()) {
                arrayList.add("connected_mmwave");
            }
        } else {
            int nrState = this.mServiceState.getNrState();
            if (nrState == 1) {
                arrayList.add("restricted");
            } else if (nrState == 2) {
                arrayList.add(isPhysicalLinkActive() ? "not_restricted_rrc_con" : "not_restricted_rrc_idle");
            } else if (nrState == 3) {
                if (isNrAdvanced()) {
                    arrayList.add("connected_mmwave");
                }
                arrayList.add(PhoneInternalInterface.REASON_CONNECTED);
            }
        }
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            OverrideTimerRule overrideTimerRule = this.mOverrideTimerRules.get((String) obj);
            if (overrideTimerRule != null && (i = overrideTimerRule.mOverrideType) != 0) {
                return i;
            }
        }
        return 0;
    }

    protected int getLteDisplayType() {
        int i = ((getDataNetworkType() == 19 || this.mServiceState.isUsingCarrierAggregation()) && IntStream.of(this.mServiceState.getCellBandwidths()).sum() > this.mLtePlusThresholdBandwidth) ? 1 : 0;
        if (isLteEnhancedAvailable()) {
            return 2;
        }
        return i;
    }

    protected boolean isLteEnhancedAvailable() {
        if (TextUtils.isEmpty(this.mLteEnhancedPattern)) {
            return false;
        }
        Pattern patternCompile = Pattern.compile(this.mLteEnhancedPattern);
        String[] strArr = {this.mServiceState.getOperatorAlphaLongRaw(), this.mServiceState.getOperatorAlphaShortRaw()};
        for (int i = 0; i < 2; i++) {
            String str = strArr[i];
            if (!TextUtils.isEmpty(str) && patternCompile.matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    private final class DefaultState extends State {
        private DefaultState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            ModemDisplayNetworkType modemDisplayNetworkType;
            NetworkTypeController.this.log("DefaultState: process " + NetworkTypeController.this.getEventName(message.what));
            switch (message.what) {
                case 0:
                    NetworkTypeController.this.resetAllTimers();
                    NetworkTypeController.this.transitionToCurrentState();
                    return true;
                case 1:
                    NetworkTypeController.this.log("Reset timers on state machine quitting.");
                    NetworkTypeController.this.resetAllTimers();
                    NetworkTypeController.this.unRegisterForAllEvents();
                    NetworkTypeController.this.quit();
                    return true;
                case 2:
                    NetworkTypeController.this.registerForAllEvents();
                    NetworkTypeController.this.parseCarrierConfigs();
                    return true;
                case 3:
                    NetworkTypeController.this.onServiceStateChanged();
                    NetworkTypeController.this.transitionToCurrentState();
                    return true;
                case 4:
                    NetworkTypeController.this.mPhysicalLinkStatus = message.arg1;
                    return true;
                case 5:
                    NetworkTypeController.this.mIsPhysicalChannelConfigOn = ((Boolean) ((AsyncResult) message.obj).result).booleanValue();
                    NetworkTypeController.this.log("mIsPhysicalChannelConfigOn changed to: " + NetworkTypeController.this.mIsPhysicalChannelConfigOn);
                    NetworkTypeController networkTypeController = NetworkTypeController.this;
                    if (!networkTypeController.mIsPhysicalChannelConfigOn) {
                        networkTypeController.log("Reset timers since physical channel config indications are off.");
                        NetworkTypeController.this.resetAllTimers();
                    }
                    NetworkTypeController.this.transitionToCurrentState();
                    return true;
                case 6:
                    NetworkTypeController.this.parseCarrierConfigs();
                    NetworkTypeController.this.log("Reset timers since carrier configurations changed.");
                    NetworkTypeController.this.resetAllTimers();
                    NetworkTypeController.this.transitionToCurrentState();
                    return true;
                case 7:
                    NetworkTypeController.this.log("Primary timer expired for state: " + NetworkTypeController.this.mPrimaryTimerState);
                    NetworkTypeController.this.transitionWithSecondaryTimerTo((IState) message.obj);
                    return true;
                case 8:
                    NetworkTypeController.this.log("Secondary timer expired for state: " + NetworkTypeController.this.mSecondaryTimerState);
                    NetworkTypeController.this.mIsSecondaryTimerActive = false;
                    NetworkTypeController.this.mSecondaryTimerExpireTimestamp = 0L;
                    NetworkTypeController.this.mSecondaryTimerState = "";
                    NetworkTypeController.this.updateTimers();
                    NetworkTypeController.this.mLastShownNrDueToAdvancedBand = false;
                    NetworkTypeController.this.updateOverrideNetworkType();
                    return true;
                case 9:
                    NetworkTypeController.this.log("Reset timers since radio is off or unavailable.");
                    NetworkTypeController.this.resetAllTimers();
                    NetworkTypeController.this.mRatchetedNrBands.clear();
                    NetworkTypeController.this.mRatchetedNrBandwidths.clear();
                    NetworkTypeController.this.mLastAnchorNrCellId = -1;
                    NetworkTypeController networkTypeController2 = NetworkTypeController.this;
                    networkTypeController2.mDoesPccListIndicateIdle = false;
                    networkTypeController2.mIsNrAdvancedAllowedByPco = false;
                    NetworkTypeController.this.mInVoiceCall = false;
                    NetworkTypeController networkTypeController3 = NetworkTypeController.this;
                    networkTypeController3.mPhysicalChannelConfigs = null;
                    networkTypeController3.transitionTo(networkTypeController3.mLegacyState);
                    return true;
                case 10:
                    NetworkTypeController.this.log("Reset timers since preferred network mode changed.");
                    NetworkTypeController.this.resetAllTimers();
                    NetworkTypeController.this.transitionToCurrentState();
                    return true;
                case 11:
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    NetworkTypeController networkTypeController4 = NetworkTypeController.this;
                    IHyperNetworkTypeManager iHyperNetworkTypeManager = networkTypeController4.mManager;
                    if (iHyperNetworkTypeManager != null && !iHyperNetworkTypeManager.isNrAdvanceNetworkTypeMatch(networkTypeController4.getDataNetworkType(), (List) asyncResult.result)) {
                        return true;
                    }
                    NetworkTypeController.this.updatePhysicalChannelConfigs((List) asyncResult.result);
                    if (NetworkTypeController.this.isUsingPhysicalChannelConfigForRrcDetection()) {
                        NetworkTypeController networkTypeController5 = NetworkTypeController.this;
                        networkTypeController5.mPhysicalLinkStatus = networkTypeController5.getPhysicalLinkStatusFromPhysicalChannelConfig();
                    }
                    NetworkTypeController.this.transitionToCurrentState();
                    return true;
                case 12:
                    NetworkTypeController.this.mIsDeviceIdleMode = ((PowerManager) NetworkTypeController.this.mPhone.getContext().getSystemService(PowerManager.class)).isDeviceIdleMode();
                    NetworkTypeController.this.log("mIsDeviceIdleMode changed to: " + NetworkTypeController.this.mIsDeviceIdleMode);
                    if (NetworkTypeController.this.mIsDeviceIdleMode) {
                        NetworkTypeController.this.log("Reset timers since device is in idle mode.");
                        NetworkTypeController.this.resetAllTimers();
                    }
                    NetworkTypeController.this.transitionToCurrentState();
                    return true;
                case 13:
                    List<QosBearerSession> list = (List) message.obj;
                    NetworkTypeController.this.mInVoiceCall = false;
                    for (QosBearerSession qosBearerSession : list) {
                        EpsQos qos = qosBearerSession.getQos();
                        if ((qos instanceof EpsQos) && qos.getQci() == 1) {
                            NetworkTypeController.this.mInVoiceCall = true;
                        } else {
                            NrQos qos2 = qosBearerSession.getQos();
                            if ((qos2 instanceof NrQos) && qos2.get5Qi() == 1) {
                                NetworkTypeController.this.mInVoiceCall = true;
                            }
                        }
                        if (NetworkTypeController.this.mIsTimerResetEnabledOnVoiceQos && NetworkTypeController.this.mInVoiceCall) {
                            NetworkTypeController.this.log("Device in voice call, reset all timers");
                            NetworkTypeController.this.resetAllTimers();
                            NetworkTypeController.this.transitionToCurrentState();
                        }
                        return true;
                    }
                    if (NetworkTypeController.this.mIsTimerResetEnabledOnVoiceQos) {
                        NetworkTypeController.this.log("Device in voice call, reset all timers");
                        NetworkTypeController.this.resetAllTimers();
                        NetworkTypeController.this.transitionToCurrentState();
                    }
                    return true;
                case 14:
                    if (NetworkTypeController.this.mUseModemDisplayNetworkType) {
                        AsyncResult asyncResult2 = (AsyncResult) message.obj;
                        NetworkTypeController networkTypeController6 = NetworkTypeController.this;
                        if (((Integer) asyncResult2.result).intValue() == 1) {
                            modemDisplayNetworkType = new ModemDisplayNetworkType(20, 5);
                        } else {
                            modemDisplayNetworkType = NetworkTypeController.DEFAULT_MODEM_NETWORK_TYPE;
                        }
                        networkTypeController6.mModemOverrideNetworkType = modemDisplayNetworkType;
                        NetworkTypeController.this.mDisplayInfoController.updateTelephonyDisplayInfo();
                    }
                    return true;
                case 15:
                    NetworkTypeController.this.updateBandwidthConstrainedStatus(((Boolean) message.obj).booleanValue());
                    return true;
                default:
                    throw new RuntimeException("Received invalid event: " + message.what);
            }
        }
    }

    private final class LegacyState extends State {
        private boolean mIsNrRestricted;

        private LegacyState() {
            this.mIsNrRestricted = false;
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            NetworkTypeController.this.log("Entering LegacyState");
            NetworkTypeController.this.updateTimers();
            NetworkTypeController.this.updateOverrideNetworkType();
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return;
            }
            this.mIsNrRestricted = NetworkTypeController.this.isNrRestricted();
            NetworkTypeController.this.mPreviousState = getName();
        }

        /* JADX WARN: Code duplicated, block: B:45:0x00f7  */
        /* JADX WARN: Code duplicated, block: B:47:0x0101  */
        /* JADX WARN: Code duplicated, block: B:48:0x0109  */
        /* JADX WARN: Code duplicated, block: B:50:0x0111  */
        /* JADX WARN: Code duplicated, block: B:51:0x0116  */
        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            int dataNetworkType;
            boolean zIsNrAdvanced;
            NetworkTypeController networkTypeController;
            boolean zIsPhysicalLinkActive;
            NetworkTypeController networkTypeController2;
            IState iState;
            NetworkTypeController.this.log("LegacyState: process " + NetworkTypeController.this.getEventName(message.what));
            NetworkTypeController.this.updateTimers();
            int i = message.what;
            if (i == 0) {
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20 || (NetworkTypeController.this.isLte(dataNetworkType) && NetworkTypeController.this.isNrConnected())) {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController2.mNrConnectedState;
                        } else {
                            iState = networkTypeController2.mNrIdleState;
                        }
                        networkTypeController.transitionTo(iState);
                    }
                } else if (NetworkTypeController.this.isLte(dataNetworkType) && NetworkTypeController.this.isNrNotRestricted()) {
                    NetworkTypeController networkTypeController3 = NetworkTypeController.this;
                    boolean zIsPhysicalLinkActive2 = networkTypeController3.isPhysicalLinkActive();
                    NetworkTypeController networkTypeController4 = NetworkTypeController.this;
                    networkTypeController3.transitionWithTimerTo(zIsPhysicalLinkActive2 ? networkTypeController4.mLteConnectedState : networkTypeController4.mIdleState);
                } else {
                    if (!NetworkTypeController.this.isLte(dataNetworkType)) {
                        NetworkTypeController.this.log("Reset timers since 2G and 3G don't need NR timers.");
                        NetworkTypeController.this.resetAllTimers();
                    }
                    NetworkTypeController.this.updateOverrideNetworkType();
                }
                this.mIsNrRestricted = NetworkTypeController.this.isNrRestricted();
            } else if (i == 11) {
                NetworkTypeController.this.updatePhysicalChannelConfigs((List) ((AsyncResult) message.obj).result);
                if (NetworkTypeController.this.isUsingPhysicalChannelConfigForRrcDetection()) {
                    NetworkTypeController networkTypeController5 = NetworkTypeController.this;
                    networkTypeController5.mPhysicalLinkStatus = networkTypeController5.getPhysicalLinkStatusFromPhysicalChannelConfig();
                    if (NetworkTypeController.this.mIsTimerResetEnabledForLegacyStateRrcIdle && !NetworkTypeController.this.isPhysicalLinkActive()) {
                        NetworkTypeController.this.log("Reset timers since timer reset is enabled for RRC idle.");
                        NetworkTypeController.this.resetAllTimers();
                        NetworkTypeController.this.updateOverrideNetworkType();
                    }
                }
            } else if (i == 3) {
                NetworkTypeController.this.onServiceStateChanged();
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20) {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController2.mNrConnectedState;
                        } else {
                            iState = networkTypeController2.mNrIdleState;
                        }
                        networkTypeController.transitionTo(iState);
                    }
                } else {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController2.mNrConnectedState;
                        } else {
                            iState = networkTypeController2.mNrIdleState;
                        }
                        networkTypeController.transitionTo(iState);
                    }
                }
                this.mIsNrRestricted = NetworkTypeController.this.isNrRestricted();
            } else {
                if (i != 4) {
                    return false;
                }
                NetworkTypeController networkTypeController6 = NetworkTypeController.this;
                networkTypeController6.mPhysicalLinkStatus = message.arg1;
                if (networkTypeController6.mIsTimerResetEnabledForLegacyStateRrcIdle && !NetworkTypeController.this.isPhysicalLinkActive()) {
                    NetworkTypeController.this.log("Reset timers since timer reset is enabled for RRC idle.");
                    NetworkTypeController.this.resetAllTimers();
                    NetworkTypeController.this.updateOverrideNetworkType();
                }
            }
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return true;
            }
            NetworkTypeController.this.mPreviousState = getName();
            return true;
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public String getName() {
            return this.mIsNrRestricted ? "restricted" : AccessNetworksManager.IWLAN_OPERATION_MODE_LEGACY;
        }
    }

    private final class IdleState extends State {
        private IdleState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            NetworkTypeController.this.log("Entering IdleState");
            NetworkTypeController.this.updateTimers();
            NetworkTypeController.this.updateOverrideNetworkType();
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return;
            }
            NetworkTypeController.this.mPreviousState = getName();
        }

        /* JADX WARN: Code duplicated, block: B:39:0x00df  */
        /* JADX WARN: Code duplicated, block: B:41:0x00e9  */
        /* JADX WARN: Code duplicated, block: B:42:0x00f1  */
        /* JADX WARN: Code duplicated, block: B:44:0x00f9  */
        /* JADX WARN: Code duplicated, block: B:45:0x00fe  */
        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            int dataNetworkType;
            boolean zIsNrAdvanced;
            NetworkTypeController networkTypeController;
            boolean zIsPhysicalLinkActive;
            NetworkTypeController networkTypeController2;
            IState iState;
            NetworkTypeController.this.log("IdleState: process " + NetworkTypeController.this.getEventName(message.what));
            NetworkTypeController.this.updateTimers();
            int i = message.what;
            if (i == 0) {
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20 || (NetworkTypeController.this.isLte(dataNetworkType) && NetworkTypeController.this.isNrConnected())) {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController2.mNrConnectedState;
                        } else {
                            iState = networkTypeController2.mNrIdleState;
                        }
                        networkTypeController.transitionTo(iState);
                    }
                } else if (!NetworkTypeController.this.isLte(dataNetworkType) || !NetworkTypeController.this.isNrNotRestricted()) {
                    NetworkTypeController networkTypeController3 = NetworkTypeController.this;
                    networkTypeController3.transitionWithTimerTo(networkTypeController3.mLegacyState);
                } else {
                    boolean zIsPhysicalLinkActive2 = NetworkTypeController.this.isPhysicalLinkActive();
                    NetworkTypeController networkTypeController4 = NetworkTypeController.this;
                    if (zIsPhysicalLinkActive2) {
                        networkTypeController4.transitionWithTimerTo(networkTypeController4.mLteConnectedState);
                    } else {
                        networkTypeController4.updateOverrideNetworkType();
                    }
                }
            } else if (i == 11) {
                NetworkTypeController.this.updatePhysicalChannelConfigs((List) ((AsyncResult) message.obj).result);
                if (NetworkTypeController.this.isUsingPhysicalChannelConfigForRrcDetection()) {
                    NetworkTypeController networkTypeController5 = NetworkTypeController.this;
                    networkTypeController5.mPhysicalLinkStatus = networkTypeController5.getPhysicalLinkStatusFromPhysicalChannelConfig();
                    boolean zIsPhysicalLinkActive3 = NetworkTypeController.this.isPhysicalLinkActive();
                    NetworkTypeController networkTypeController6 = NetworkTypeController.this;
                    if (zIsPhysicalLinkActive3) {
                        networkTypeController6.transitionWithTimerTo(networkTypeController6.mLteConnectedState);
                    } else {
                        networkTypeController6.log("Reevaluating state due to link status changed.");
                        NetworkTypeController.this.sendMessage(0);
                    }
                }
            } else if (i == 3) {
                NetworkTypeController.this.onServiceStateChanged();
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20) {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController2.mNrConnectedState;
                        } else {
                            iState = networkTypeController2.mNrIdleState;
                        }
                        networkTypeController.transitionTo(iState);
                    }
                } else {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController2.mNrConnectedState;
                        } else {
                            iState = networkTypeController2.mNrIdleState;
                        }
                        networkTypeController.transitionTo(iState);
                    }
                }
            } else {
                if (i != 4) {
                    return false;
                }
                NetworkTypeController networkTypeController7 = NetworkTypeController.this;
                networkTypeController7.mPhysicalLinkStatus = message.arg1;
                boolean zIsPhysicalLinkActive4 = networkTypeController7.isPhysicalLinkActive();
                NetworkTypeController networkTypeController8 = NetworkTypeController.this;
                if (zIsPhysicalLinkActive4) {
                    networkTypeController8.transitionWithTimerTo(networkTypeController8.mLteConnectedState);
                } else {
                    networkTypeController8.log("Reevaluating state due to link status changed.");
                    NetworkTypeController.this.sendMessage(0);
                }
            }
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return true;
            }
            NetworkTypeController.this.mPreviousState = getName();
            return true;
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public String getName() {
            return "not_restricted_rrc_idle";
        }
    }

    private final class LteConnectedState extends State {
        private LteConnectedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            NetworkTypeController.this.log("Entering LteConnectedState");
            NetworkTypeController.this.updateTimers();
            NetworkTypeController.this.updateOverrideNetworkType();
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return;
            }
            NetworkTypeController.this.mPreviousState = getName();
        }

        /* JADX WARN: Code duplicated, block: B:39:0x00df  */
        /* JADX WARN: Code duplicated, block: B:41:0x00e9  */
        /* JADX WARN: Code duplicated, block: B:42:0x00f1  */
        /* JADX WARN: Code duplicated, block: B:44:0x00f9  */
        /* JADX WARN: Code duplicated, block: B:45:0x00fe  */
        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            int dataNetworkType;
            boolean zIsNrAdvanced;
            NetworkTypeController networkTypeController;
            boolean zIsPhysicalLinkActive;
            NetworkTypeController networkTypeController2;
            IState iState;
            NetworkTypeController.this.log("LteConnectedState: process " + NetworkTypeController.this.getEventName(message.what));
            NetworkTypeController.this.updateTimers();
            int i = message.what;
            if (i == 0) {
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20 || (NetworkTypeController.this.isLte(dataNetworkType) && NetworkTypeController.this.isNrConnected())) {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController2.mNrConnectedState;
                        } else {
                            iState = networkTypeController2.mNrIdleState;
                        }
                        networkTypeController.transitionTo(iState);
                    }
                } else if (!NetworkTypeController.this.isLte(dataNetworkType) || !NetworkTypeController.this.isNrNotRestricted()) {
                    NetworkTypeController networkTypeController3 = NetworkTypeController.this;
                    networkTypeController3.transitionWithTimerTo(networkTypeController3.mLegacyState);
                } else {
                    boolean zIsPhysicalLinkActive2 = NetworkTypeController.this.isPhysicalLinkActive();
                    NetworkTypeController networkTypeController4 = NetworkTypeController.this;
                    if (!zIsPhysicalLinkActive2) {
                        networkTypeController4.transitionWithTimerTo(networkTypeController4.mIdleState);
                    } else {
                        networkTypeController4.updateOverrideNetworkType();
                    }
                }
            } else if (i == 11) {
                NetworkTypeController.this.updatePhysicalChannelConfigs((List) ((AsyncResult) message.obj).result);
                if (NetworkTypeController.this.isUsingPhysicalChannelConfigForRrcDetection()) {
                    NetworkTypeController networkTypeController5 = NetworkTypeController.this;
                    networkTypeController5.mPhysicalLinkStatus = networkTypeController5.getPhysicalLinkStatusFromPhysicalChannelConfig();
                    boolean zIsPhysicalLinkActive3 = NetworkTypeController.this.isPhysicalLinkActive();
                    NetworkTypeController networkTypeController6 = NetworkTypeController.this;
                    if (!zIsPhysicalLinkActive3) {
                        networkTypeController6.transitionWithTimerTo(networkTypeController6.mIdleState);
                    } else {
                        networkTypeController6.log("Reevaluating state due to link status changed.");
                        NetworkTypeController.this.sendMessage(0);
                    }
                }
            } else if (i == 3) {
                NetworkTypeController.this.onServiceStateChanged();
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20) {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController2.mNrConnectedState;
                        } else {
                            iState = networkTypeController2.mNrIdleState;
                        }
                        networkTypeController.transitionTo(iState);
                    }
                } else {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController2.mNrConnectedState;
                        } else {
                            iState = networkTypeController2.mNrIdleState;
                        }
                        networkTypeController.transitionTo(iState);
                    }
                }
            } else {
                if (i != 4) {
                    return false;
                }
                NetworkTypeController networkTypeController7 = NetworkTypeController.this;
                networkTypeController7.mPhysicalLinkStatus = message.arg1;
                boolean zIsPhysicalLinkActive4 = networkTypeController7.isPhysicalLinkActive();
                NetworkTypeController networkTypeController8 = NetworkTypeController.this;
                if (!zIsPhysicalLinkActive4) {
                    networkTypeController8.transitionWithTimerTo(networkTypeController8.mIdleState);
                } else {
                    networkTypeController8.log("Reevaluating state due to link status changed.");
                    NetworkTypeController.this.sendMessage(0);
                }
            }
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return true;
            }
            NetworkTypeController.this.mPreviousState = getName();
            return true;
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public String getName() {
            return "not_restricted_rrc_con";
        }
    }

    private final class NrIdleState extends State {
        private NrIdleState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            NetworkTypeController.this.log("Entering NrIdleState");
            NetworkTypeController.this.updateTimers();
            NetworkTypeController.this.updateOverrideNetworkType();
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return;
            }
            NetworkTypeController.this.mPreviousState = getName();
        }

        /* JADX WARN: Code duplicated, block: B:43:0x00df  */
        /* JADX WARN: Code duplicated, block: B:45:0x00e9  */
        /* JADX WARN: Code duplicated, block: B:46:0x00ef  */
        /* JADX WARN: Code duplicated, block: B:48:0x00f7  */
        /* JADX WARN: Code duplicated, block: B:49:0x00ff  */
        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            int dataNetworkType;
            boolean zIsNrAdvanced;
            NetworkTypeController networkTypeController;
            boolean zIsPhysicalLinkActive;
            NetworkTypeController networkTypeController2;
            NetworkTypeController.this.log("NrIdleState: process " + NetworkTypeController.this.getEventName(message.what));
            NetworkTypeController.this.updateTimers();
            int i = message.what;
            if (i == 0) {
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20 || (NetworkTypeController.this.isLte(dataNetworkType) && NetworkTypeController.this.isNrConnected())) {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.log("Ignore NR advanced from cached PCC/RatchetedNrBands while idle");
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            networkTypeController2.transitionWithTimerTo(networkTypeController2.mNrConnectedState);
                        } else {
                            networkTypeController2.updateOverrideNetworkType();
                        }
                    }
                } else if (NetworkTypeController.this.isLte(dataNetworkType) && NetworkTypeController.this.isNrNotRestricted()) {
                    NetworkTypeController networkTypeController3 = NetworkTypeController.this;
                    boolean zIsPhysicalLinkActive2 = networkTypeController3.isPhysicalLinkActive();
                    NetworkTypeController networkTypeController4 = NetworkTypeController.this;
                    networkTypeController3.transitionWithTimerTo(zIsPhysicalLinkActive2 ? networkTypeController4.mLteConnectedState : networkTypeController4.mIdleState);
                } else {
                    NetworkTypeController networkTypeController5 = NetworkTypeController.this;
                    networkTypeController5.transitionWithTimerTo(networkTypeController5.mLegacyState);
                }
            } else if (i == 11) {
                NetworkTypeController.this.updatePhysicalChannelConfigs((List) ((AsyncResult) message.obj).result);
                if (NetworkTypeController.this.isUsingPhysicalChannelConfigForRrcDetection()) {
                    NetworkTypeController networkTypeController6 = NetworkTypeController.this;
                    networkTypeController6.mPhysicalLinkStatus = networkTypeController6.getPhysicalLinkStatusFromPhysicalChannelConfig();
                }
                boolean zIsPhysicalLinkActive3 = NetworkTypeController.this.isPhysicalLinkActive();
                NetworkTypeController networkTypeController7 = NetworkTypeController.this;
                if (zIsPhysicalLinkActive3) {
                    boolean zIsNrAdvanced2 = networkTypeController7.isNrAdvanced();
                    NetworkTypeController networkTypeController8 = NetworkTypeController.this;
                    if (zIsNrAdvanced2) {
                        networkTypeController8.transitionTo(networkTypeController8.mNrConnectedAdvancedState);
                    } else {
                        networkTypeController8.transitionWithTimerTo(networkTypeController8.mNrConnectedState);
                    }
                } else {
                    networkTypeController7.updateOverrideNetworkType();
                }
            } else if (i == 3) {
                NetworkTypeController.this.onServiceStateChanged();
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20) {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.log("Ignore NR advanced from cached PCC/RatchetedNrBands while idle");
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            networkTypeController2.transitionWithTimerTo(networkTypeController2.mNrConnectedState);
                        } else {
                            networkTypeController2.updateOverrideNetworkType();
                        }
                    }
                } else {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.log("Ignore NR advanced from cached PCC/RatchetedNrBands while idle");
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            networkTypeController2.transitionWithTimerTo(networkTypeController2.mNrConnectedState);
                        } else {
                            networkTypeController2.updateOverrideNetworkType();
                        }
                    }
                }
            } else {
                if (i != 4) {
                    return false;
                }
                NetworkTypeController networkTypeController9 = NetworkTypeController.this;
                networkTypeController9.mPhysicalLinkStatus = message.arg1;
                if (networkTypeController9.isPhysicalLinkActive()) {
                    NetworkTypeController networkTypeController10 = NetworkTypeController.this;
                    networkTypeController10.transitionWithTimerTo(networkTypeController10.mNrConnectedState);
                }
            }
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return true;
            }
            NetworkTypeController.this.mPreviousState = getName();
            return true;
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public String getName() {
            return "connected_rrc_idle";
        }
    }

    private final class NrConnectedState extends State {
        private NrConnectedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            NetworkTypeController.this.log("Entering NrConnectedState");
            NetworkTypeController.this.updateTimers();
            NetworkTypeController.this.updateOverrideNetworkType();
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return;
            }
            NetworkTypeController.this.mPreviousState = getName();
        }

        /* JADX WARN: Code duplicated, block: B:42:0x00da  */
        /* JADX WARN: Code duplicated, block: B:44:0x00e4  */
        /* JADX WARN: Code duplicated, block: B:45:0x00ec  */
        /* JADX WARN: Code duplicated, block: B:47:0x00f4  */
        /* JADX WARN: Code duplicated, block: B:48:0x00fc  */
        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            int dataNetworkType;
            boolean zIsNrAdvanced;
            NetworkTypeController networkTypeController;
            boolean zIsPhysicalLinkActive;
            NetworkTypeController networkTypeController2;
            NetworkTypeController.this.log("NrConnectedState: process " + NetworkTypeController.this.getEventName(message.what));
            NetworkTypeController.this.updateTimers();
            int i = message.what;
            if (i == 0) {
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20 || (NetworkTypeController.this.isLte(dataNetworkType) && NetworkTypeController.this.isNrConnected())) {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (!zIsPhysicalLinkActive) {
                            networkTypeController2.transitionWithTimerTo(networkTypeController2.mNrIdleState);
                        } else {
                            networkTypeController2.updateOverrideNetworkType();
                        }
                    }
                } else if (NetworkTypeController.this.isLte(dataNetworkType) && NetworkTypeController.this.isNrNotRestricted()) {
                    NetworkTypeController networkTypeController3 = NetworkTypeController.this;
                    boolean zIsPhysicalLinkActive2 = networkTypeController3.isPhysicalLinkActive();
                    NetworkTypeController networkTypeController4 = NetworkTypeController.this;
                    networkTypeController3.transitionWithTimerTo(zIsPhysicalLinkActive2 ? networkTypeController4.mLteConnectedState : networkTypeController4.mIdleState);
                } else {
                    NetworkTypeController networkTypeController5 = NetworkTypeController.this;
                    networkTypeController5.transitionWithTimerTo(networkTypeController5.mLegacyState);
                }
            } else if (i == 11) {
                NetworkTypeController.this.updatePhysicalChannelConfigs((List) ((AsyncResult) message.obj).result);
                if (NetworkTypeController.this.isUsingPhysicalChannelConfigForRrcDetection()) {
                    NetworkTypeController networkTypeController6 = NetworkTypeController.this;
                    networkTypeController6.mPhysicalLinkStatus = networkTypeController6.getPhysicalLinkStatusFromPhysicalChannelConfig();
                }
                boolean zIsPhysicalLinkActive3 = NetworkTypeController.this.isPhysicalLinkActive();
                NetworkTypeController networkTypeController7 = NetworkTypeController.this;
                if (!zIsPhysicalLinkActive3) {
                    networkTypeController7.transitionWithTimerTo(networkTypeController7.mNrIdleState);
                } else if (networkTypeController7.isNrAdvanced()) {
                    NetworkTypeController networkTypeController8 = NetworkTypeController.this;
                    networkTypeController8.transitionTo(networkTypeController8.mNrConnectedAdvancedState);
                }
            } else if (i == 3) {
                NetworkTypeController.this.onServiceStateChanged();
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20) {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (!zIsPhysicalLinkActive) {
                            networkTypeController2.transitionWithTimerTo(networkTypeController2.mNrIdleState);
                        } else {
                            networkTypeController2.updateOverrideNetworkType();
                        }
                    }
                } else {
                    zIsNrAdvanced = NetworkTypeController.this.isNrAdvanced();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsNrAdvanced) {
                        networkTypeController.transitionTo(networkTypeController.mNrConnectedAdvancedState);
                    } else {
                        zIsPhysicalLinkActive = networkTypeController.isPhysicalLinkActive();
                        networkTypeController2 = NetworkTypeController.this;
                        if (!zIsPhysicalLinkActive) {
                            networkTypeController2.transitionWithTimerTo(networkTypeController2.mNrIdleState);
                        } else {
                            networkTypeController2.updateOverrideNetworkType();
                        }
                    }
                }
            } else {
                if (i != 4) {
                    return false;
                }
                NetworkTypeController networkTypeController9 = NetworkTypeController.this;
                networkTypeController9.mPhysicalLinkStatus = message.arg1;
                if (!networkTypeController9.isPhysicalLinkActive()) {
                    NetworkTypeController networkTypeController10 = NetworkTypeController.this;
                    networkTypeController10.transitionWithTimerTo(networkTypeController10.mNrIdleState);
                }
            }
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return true;
            }
            NetworkTypeController.this.mPreviousState = getName();
            return true;
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public String getName() {
            return PhoneInternalInterface.REASON_CONNECTED;
        }
    }

    private final class NrConnectedAdvancedState extends State {
        private NrConnectedAdvancedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            NetworkTypeController.this.log("Entering NrConnectedAdvancedState");
            NetworkTypeController.this.updateTimers();
            NetworkTypeController.this.updateOverrideNetworkType();
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return;
            }
            NetworkTypeController.this.mPreviousState = getName();
        }

        /* JADX WARN: Code duplicated, block: B:39:0x00e6  */
        /* JADX WARN: Code duplicated, block: B:41:0x00ee  */
        /* JADX WARN: Code duplicated, block: B:42:0x00f4  */
        /* JADX WARN: Code duplicated, block: B:43:0x00f6  */
        /* JADX WARN: Code duplicated, block: B:48:0x010e  */
        /* JADX WARN: Code duplicated, block: B:49:0x0113  */
        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            int dataNetworkType;
            boolean zIsPhysicalLinkActive;
            NetworkTypeController networkTypeController;
            IState iState;
            NetworkTypeController networkTypeController2 = NetworkTypeController.this;
            networkTypeController2.mLastShownNrDueToAdvancedBand = networkTypeController2.isAdditionalNrAdvancedBand(networkTypeController2.mRatchetedNrBands);
            NetworkTypeController.this.log("NrConnectedAdvancedState: process " + NetworkTypeController.this.getEventName(message.what) + ", been using advanced band is " + NetworkTypeController.this.mLastShownNrDueToAdvancedBand);
            NetworkTypeController.this.updateTimers();
            int i = message.what;
            if (i == 0) {
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20 || (NetworkTypeController.this.isLte(dataNetworkType) && NetworkTypeController.this.isNrConnected())) {
                    if (NetworkTypeController.this.isNrAdvanced()) {
                        NetworkTypeController.this.updateOverrideNetworkType();
                    } else {
                        if (dataNetworkType == 20 && NetworkTypeController.this.mOverrideNetworkType != 5) {
                            NetworkTypeController.this.mOverrideNetworkType = 0;
                        }
                        NetworkTypeController networkTypeController3 = NetworkTypeController.this;
                        zIsPhysicalLinkActive = networkTypeController3.isPhysicalLinkActive();
                        networkTypeController = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController.mNrConnectedState;
                        } else {
                            iState = networkTypeController.mNrIdleState;
                        }
                        networkTypeController3.transitionWithTimerTo(iState);
                    }
                } else if (NetworkTypeController.this.isLte(dataNetworkType) && NetworkTypeController.this.isNrNotRestricted()) {
                    NetworkTypeController networkTypeController4 = NetworkTypeController.this;
                    boolean zIsPhysicalLinkActive2 = networkTypeController4.isPhysicalLinkActive();
                    NetworkTypeController networkTypeController5 = NetworkTypeController.this;
                    networkTypeController4.transitionWithTimerTo(zIsPhysicalLinkActive2 ? networkTypeController5.mLteConnectedState : networkTypeController5.mIdleState);
                } else {
                    NetworkTypeController networkTypeController6 = NetworkTypeController.this;
                    networkTypeController6.transitionWithTimerTo(networkTypeController6.mLegacyState);
                }
            } else if (i == 11) {
                NetworkTypeController.this.updatePhysicalChannelConfigs((List) ((AsyncResult) message.obj).result);
                if (NetworkTypeController.this.isUsingPhysicalChannelConfigForRrcDetection()) {
                    NetworkTypeController networkTypeController7 = NetworkTypeController.this;
                    networkTypeController7.mPhysicalLinkStatus = networkTypeController7.getPhysicalLinkStatusFromPhysicalChannelConfig();
                }
                boolean zIsPhysicalLinkActive3 = NetworkTypeController.this.isPhysicalLinkActive();
                NetworkTypeController networkTypeController8 = NetworkTypeController.this;
                if (!zIsPhysicalLinkActive3) {
                    networkTypeController8.transitionWithTimerTo(networkTypeController8.mNrIdleState);
                } else if (!networkTypeController8.isNrAdvanced()) {
                    NetworkTypeController networkTypeController9 = NetworkTypeController.this;
                    networkTypeController9.transitionWithTimerTo(networkTypeController9.mNrConnectedState);
                }
            } else if (i == 3) {
                NetworkTypeController.this.onServiceStateChanged();
                dataNetworkType = NetworkTypeController.this.getDataNetworkType();
                if (dataNetworkType != 20) {
                    if (NetworkTypeController.this.isNrAdvanced()) {
                        NetworkTypeController.this.updateOverrideNetworkType();
                    } else {
                        if (dataNetworkType == 20) {
                            NetworkTypeController.this.mOverrideNetworkType = 0;
                        }
                        NetworkTypeController networkTypeController10 = NetworkTypeController.this;
                        zIsPhysicalLinkActive = networkTypeController10.isPhysicalLinkActive();
                        networkTypeController = NetworkTypeController.this;
                        if (zIsPhysicalLinkActive) {
                            iState = networkTypeController.mNrConnectedState;
                        } else {
                            iState = networkTypeController.mNrIdleState;
                        }
                        networkTypeController10.transitionWithTimerTo(iState);
                    }
                } else if (NetworkTypeController.this.isNrAdvanced()) {
                    NetworkTypeController.this.updateOverrideNetworkType();
                } else {
                    if (dataNetworkType == 20) {
                        NetworkTypeController.this.mOverrideNetworkType = 0;
                    }
                    NetworkTypeController networkTypeController11 = NetworkTypeController.this;
                    zIsPhysicalLinkActive = networkTypeController11.isPhysicalLinkActive();
                    networkTypeController = NetworkTypeController.this;
                    if (zIsPhysicalLinkActive) {
                        iState = networkTypeController.mNrConnectedState;
                    } else {
                        iState = networkTypeController.mNrIdleState;
                    }
                    networkTypeController11.transitionWithTimerTo(iState);
                }
            } else {
                if (i != 4) {
                    return false;
                }
                NetworkTypeController.this.mPhysicalLinkStatus = message.arg1;
            }
            if (NetworkTypeController.this.mIsPrimaryTimerActive || NetworkTypeController.this.mIsSecondaryTimerActive) {
                return true;
            }
            NetworkTypeController.this.mPreviousState = getName();
            return true;
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public String getName() {
            return "connected_mmwave";
        }
    }

    protected void onServiceStateChanged() {
        ServiceState serviceState = this.mPhone.getServiceStateTracker().getServiceState();
        if (this.mIsTimerResetEnabledOnPlmnChanges && !TextUtils.equals(this.mServiceState.getOperatorNumeric(), serviceState.getOperatorNumeric())) {
            log("Reset any timers due to nr_timers_reset_on_plmn_change_bool");
            resetAllTimers();
        }
        this.mServiceState = serviceState;
        log("ServiceState updated: " + this.mServiceState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePhysicalChannelConfigs(List list) {
        int physicalCellId;
        int physicalCellId2;
        boolean z = list == null || list.isEmpty();
        if (z && isUsingPhysicalChannelConfigForRrcDetection()) {
            log("Physical channel configs updated: not updating PCC fields for empty PCC list indicating RRC idle.");
            this.mPrimaryCellChangedWhileIdle = false;
            this.mPhysicalChannelConfigs = list;
            this.mDoesPccListIndicateIdle = true;
            if (this.mRatchetPccFieldsForSameAnchorNrCell) {
                return;
            }
            this.mRatchetedNrBands.clear();
            this.mRatchetedNrBandwidths.clear();
            return;
        }
        List arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        if (list != null) {
            Iterator it = list.iterator();
            physicalCellId = -1;
            physicalCellId2 = -1;
            while (it.hasNext()) {
                PhysicalChannelConfig physicalChannelConfig = (PhysicalChannelConfig) it.next();
                if (physicalChannelConfig.getNetworkType() == 20) {
                    if (physicalChannelConfig.getConnectionStatus() == 1 && physicalCellId == -1) {
                        physicalCellId = physicalChannelConfig.getPhysicalCellId();
                    }
                    arrayList.add(Integer.valueOf(physicalChannelConfig.getCellBandwidthDownlinkKhz()));
                    hashSet.add(Integer.valueOf(physicalChannelConfig.getBand()));
                } else if (physicalChannelConfig.getNetworkType() == 13) {
                    if (physicalChannelConfig.getConnectionStatus() == 1 && physicalCellId2 == -1) {
                        physicalCellId2 = physicalChannelConfig.getPhysicalCellId();
                    }
                    if (this.mIncludeLteForNrAdvancedThresholdBandwidth) {
                        arrayList.add(Integer.valueOf(physicalChannelConfig.getCellBandwidthDownlinkKhz()));
                    }
                }
            }
        } else {
            physicalCellId = -1;
            physicalCellId2 = -1;
        }
        if (physicalCellId == -1 && physicalCellId2 != -1) {
            physicalCellId = physicalCellId2;
        }
        if (physicalCellId == -1) {
            if (!z) {
                log("Ignoring physical channel config fields without an anchor NR cell, either due to LTE-only configs or an unspecified cell ID.");
            }
            this.mRatchetedNrBandwidths.clear();
            this.mRatchetedNrBands.clear();
        } else {
            int i = this.mLastAnchorNrCellId;
            if (physicalCellId == i && this.mRatchetPccFieldsForSameAnchorNrCell) {
                log("Ratchet physical channel config fields since anchor NR cell is the same.");
                if (getNrBandwidth(arrayList) <= getNrBandwidth(this.mRatchetedNrBandwidths)) {
                    arrayList = this.mRatchetedNrBandwidths;
                }
                this.mRatchetedNrBandwidths = arrayList;
                this.mRatchetedNrBands.addAll(hashSet);
            } else {
                this.mRatchetedNrBandwidths = arrayList;
                this.mRatchetedNrBands = hashSet;
                if (this.mDoesPccListIndicateIdle && physicalCellId != i && isUsingPhysicalChannelConfigForRrcDetection() && !this.mPrimaryCellChangedWhileIdle && !isNrAdvancedForPccFields(arrayList, hashSet)) {
                    log("Allow primary cell change once during RRC idle without changing state: " + this.mLastAnchorNrCellId + " -> " + physicalCellId);
                    this.mPrimaryCellChangedWhileIdle = true;
                    this.mLastAnchorNrCellId = physicalCellId;
                    reduceSecondaryTimerIfNeeded();
                    return;
                }
                if (this.mRatchetPccFieldsForSameAnchorNrCell) {
                    log("Not ratcheting physical channel config fields since anchor NR cell changed: " + this.mLastAnchorNrCellId + " -> " + physicalCellId);
                }
            }
        }
        this.mPciChangedDuringPrimaryTimer = this.mLastAnchorNrCellId != physicalCellId && this.mIsPrimaryTimerActive;
        this.mLastAnchorNrCellId = physicalCellId;
        this.mPhysicalChannelConfigs = list;
        this.mDoesPccListIndicateIdle = false;
        log("Physical channel configs updated: anchorNrCell=" + this.mLastAnchorNrCellId + ", nrBandwidths=" + this.mRatchetedNrBandwidths + ", nrBands=" + this.mRatchetedNrBands + ", configs=" + this.mPhysicalChannelConfigs);
    }

    private void reduceSecondaryTimerIfNeeded() {
        if (!this.mIsSecondaryTimerActive || this.mNrAdvancedBandsSecondaryTimer <= 0) {
            return;
        }
        OverrideTimerRule overrideTimerRule = this.mOverrideTimerRules.get(this.mPrimaryTimerState);
        if (overrideTimerRule != null) {
            long secondaryTimer = ((long) overrideTimerRule.getSecondaryTimer(this.mSecondaryTimerState)) * 1000;
            long jUptimeMillis = SystemClock.uptimeMillis();
            if (this.mSecondaryTimerExpireTimestamp - jUptimeMillis > secondaryTimer) {
                log("Due to PCI change, reduce the secondary timer to " + secondaryTimer);
                removeMessages(8);
                sendMessageDelayed(8, this.mSecondaryTimerState, secondaryTimer);
                this.mSecondaryTimerExpireTimestamp = jUptimeMillis + secondaryTimer;
                return;
            }
            return;
        }
        loge("!! Secondary timer is active, but found no rule for " + this.mPrimaryTimerState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transitionWithTimerTo(IState iState) {
        String name = iState.getName();
        if (this.mIsPrimaryTimerActive) {
            log("Transition without timer from " + getCurrentState().getName() + " to " + name + " due to existing " + this.mPrimaryTimerState + " primary timer.");
        } else if (this.mIsTimerResetEnabledOnVoiceQos && this.mInVoiceCall) {
            log("Skip primary timer to " + name + " due to in call");
        } else {
            log("Transition with primary timer from " + this.mPreviousState + " to " + name);
            OverrideTimerRule overrideTimerRule = this.mOverrideTimerRules.get(this.mPreviousState);
            if (!this.mIsDeviceIdleMode && overrideTimerRule != null && overrideTimerRule.getTimer(name) > 0) {
                int timer = overrideTimerRule.getTimer(name);
                log(timer + "s primary timer started for state: " + this.mPreviousState);
                this.mPrimaryTimerState = this.mPreviousState;
                this.mPreviousState = getCurrentState().getName();
                this.mIsPrimaryTimerActive = true;
                sendMessageDelayed(7, iState, ((long) timer) * 1000);
            }
        }
        transitionTo(iState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:25:0x00ae  */
    public void transitionWithSecondaryTimerTo(IState iState) {
        int secondaryTimer;
        int i;
        String name = getCurrentState().getName();
        OverrideTimerRule overrideTimerRule = this.mOverrideTimerRules.get(this.mPrimaryTimerState);
        log("Transition with secondary timer from " + name + " to " + iState.getName());
        if (this.mIsTimerResetEnabledOnVoiceQos && this.mInVoiceCall) {
            log("Skip secondary timer from " + name + " to " + iState.getName() + " due to in call");
        } else {
            if (!this.mIsDeviceIdleMode && overrideTimerRule != null && overrideTimerRule.getSecondaryTimer(name) > 0) {
                secondaryTimer = overrideTimerRule.getSecondaryTimer(name);
                if (this.mLastShownNrDueToAdvancedBand && (i = this.mNrAdvancedBandsSecondaryTimer) > 0) {
                    log("timer adjusted by nr_advanced_bands_secondary_timer_seconds_int");
                    secondaryTimer = i;
                }
                log(secondaryTimer + "s secondary timer started for state: " + name);
            } else {
                secondaryTimer = this.mNrAdvancedPciChangeSecondaryTimer;
                if (secondaryTimer > 0 && this.mPciChangedDuringPrimaryTimer) {
                    log(secondaryTimer + "s secondary timer started for PCI changed");
                }
            }
            if (secondaryTimer > 0) {
                this.mSecondaryTimerState = name;
                this.mPreviousState = name;
                this.mIsSecondaryTimerActive = true;
                long j = ((long) secondaryTimer) * 1000;
                this.mSecondaryTimerExpireTimestamp = SystemClock.uptimeMillis() + j;
                sendMessageDelayed(8, iState, j);
            }
            this.mIsPrimaryTimerActive = false;
            this.mPciChangedDuringPrimaryTimer = false;
            transitionTo(getCurrentState());
        }
        secondaryTimer = -1;
        if (secondaryTimer > 0) {
            this.mSecondaryTimerState = name;
            this.mPreviousState = name;
            this.mIsSecondaryTimerActive = true;
            long j2 = ((long) secondaryTimer) * 1000;
            this.mSecondaryTimerExpireTimestamp = SystemClock.uptimeMillis() + j2;
            sendMessageDelayed(8, iState, j2);
        }
        this.mIsPrimaryTimerActive = false;
        this.mPciChangedDuringPrimaryTimer = false;
        transitionTo(getCurrentState());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transitionToCurrentState() {
        IState iState;
        int dataNetworkType = getDataNetworkType();
        if (dataNetworkType == 20 || (isLte(dataNetworkType) && isNrConnected())) {
            if (!isPhysicalLinkActive()) {
                iState = this.mNrIdleState;
            } else if (isNrAdvanced()) {
                iState = this.mNrConnectedAdvancedState;
            } else {
                iState = this.mNrConnectedState;
            }
        } else if (isLte(dataNetworkType) && isNrNotRestricted()) {
            if (isPhysicalLinkActive()) {
                iState = this.mLteConnectedState;
            } else {
                iState = this.mIdleState;
            }
        } else {
            iState = this.mLegacyState;
        }
        if (!iState.equals(getCurrentState())) {
            this.mPreviousState = getCurrentState().getName();
            transitionTo(iState);
        } else {
            updateOverrideNetworkType();
        }
    }

    private boolean updateEndcStateAndGetPrevious(int i) {
        boolean z = this.mEndcStatus;
        this.mEndcStatus = isLte(i) && this.mServiceState.getNrState() != 0;
        log("endc state currRat: " + i + " (Endc: " + this.mEndcStatus + "), prevIsEndc: " + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTimers() {
        int dataNetworkType = getDataNetworkType();
        boolean zUpdateEndcStateAndGetPrevious = updateEndcStateAndGetPrevious(dataNetworkType);
        if ((((long) this.mPhone.getCachedAllowedNetworkTypesBitmask()) & 524288) == 0) {
            log("Reset timers since NR is not allowed.");
            resetAllTimers();
            return;
        }
        String name = getCurrentState().getName();
        if (this.mIsPrimaryTimerActive && this.mPrimaryTimerState.equals(name)) {
            log("Remove primary timer since primary timer state (" + this.mPrimaryTimerState + ") was reestablished.");
            removeMessages(7);
            this.mIsPrimaryTimerActive = false;
            this.mPciChangedDuringPrimaryTimer = false;
            this.mPrimaryTimerState = "";
            transitionToCurrentState();
            return;
        }
        if (this.mIsSecondaryTimerActive && !this.mSecondaryTimerState.equals(name)) {
            log("Remove secondary timer since current state (" + name + ") is no longer secondary timer state (" + this.mSecondaryTimerState + ").");
            removeMessages(8);
            this.mIsSecondaryTimerActive = false;
            this.mSecondaryTimerExpireTimestamp = 0L;
            this.mSecondaryTimerState = "";
            transitionToCurrentState();
            return;
        }
        if (this.mIsPrimaryTimerActive || this.mIsSecondaryTimerActive) {
            if (name.equals("connected_mmwave")) {
                log("Reset timers since state is NR_ADVANCED.");
                resetAllTimers();
                return;
            }
            if ((name.equals(PhoneInternalInterface.REASON_CONNECTED) || name.equals("connected_rrc_idle")) && !this.mPrimaryTimerState.equals("connected_mmwave") && !this.mSecondaryTimerState.equals("connected_mmwave")) {
                log("Reset non-NR advanced timers since state is NR connected/idle");
                resetAllTimers();
                return;
            }
            if (this.mIsTimerResetEnabledOnEndcToSaTransit && dataNetworkType == 20 && zUpdateEndcStateAndGetPrevious) {
                log("Reset timers since SA transit (from ENDC).");
                resetAllTimers();
                return;
            }
            if (this.mIsTimerResetEnabledForLegacyStateRrcIdle && this.mPrimaryTimerState.equals("connected_mmwave") && name.equals("connected_rrc_idle")) {
                log("Reset NR advanced timers since state is NR idle");
                resetAllTimers();
            } else {
                if (isLte(dataNetworkType) || dataNetworkType == 20) {
                    return;
                }
                log("Reset timers since 2G and 3G don't need NR timers.");
                resetAllTimers();
            }
        }
    }

    public void resetAllTimers() {
        removeMessages(7);
        removeMessages(8);
        this.mIsPrimaryTimerActive = false;
        this.mPciChangedDuringPrimaryTimer = false;
        this.mIsSecondaryTimerActive = false;
        this.mSecondaryTimerExpireTimestamp = 0L;
        this.mPrimaryTimerState = "";
        this.mSecondaryTimerState = "";
        this.mLastShownNrDueToAdvancedBand = false;
    }

    public static class OverrideTimerRule {
        final int mOverrideType;
        final Map mPrimaryTimers = new HashMap();
        final Map mSecondaryTimers = new HashMap();
        final String mState;

        public OverrideTimerRule(String str, int i) {
            this.mState = str;
            this.mOverrideType = i;
        }

        public void addTimer(String str, int i) {
            this.mPrimaryTimers.put(str, Integer.valueOf(i));
        }

        public void addSecondaryTimer(String str, int i) {
            this.mSecondaryTimers.put(str, Integer.valueOf(i));
        }

        public int getTimer(String str) {
            Integer num = (Integer) this.mPrimaryTimers.get(str);
            if (num == null) {
                num = (Integer) this.mPrimaryTimers.get("any");
            }
            if (num == null) {
                return 0;
            }
            return num.intValue();
        }

        public int getSecondaryTimer(String str) {
            Integer num = (Integer) this.mSecondaryTimers.get(str);
            if (num == null) {
                num = (Integer) this.mSecondaryTimers.get("any");
            }
            if (num == null) {
                return 0;
            }
            return num.intValue();
        }

        public boolean isDefined() {
            return (this.mOverrideType == 0 && this.mPrimaryTimers.isEmpty() && this.mSecondaryTimers.isEmpty()) ? false : true;
        }

        public String toString() {
            return "{mState=" + this.mState + ", mOverrideType=" + TelephonyDisplayInfo.overrideNetworkTypeToString(this.mOverrideType) + ", mPrimaryTimers=" + this.mPrimaryTimers + ", mSecondaryTimers=" + this.mSecondaryTimers + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNrConnected() {
        return this.mServiceState.getNrState() == 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNrNotRestricted() {
        return this.mServiceState.getNrState() == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNrRestricted() {
        return this.mServiceState.getNrState() == 1;
    }

    protected boolean isNrAdvanced() {
        IHyperNetworkTypeManager iHyperNetworkTypeManager = this.mManager;
        if (iHyperNetworkTypeManager == null || !iHyperNetworkTypeManager.isNrAdvanced(this.mPhysicalChannelConfigs, isPhysicalLinkActive())) {
            return isNrAdvancedForPccFields(this.mRatchetedNrBandwidths, this.mRatchetedNrBands);
        }
        return true;
    }

    private boolean isNrAdvancedForPccFields(List list, Set set) {
        if (this.mNrAdvancedCapablePcoId > 0 && !this.mIsNrAdvancedAllowedByPco) {
            log("isNrAdvanced: not allowed by PCO for PCO ID " + this.mNrAdvancedCapablePcoId);
            return false;
        }
        if (this.mServiceState.getDataRoaming() && !this.mEnableNrAdvancedWhileRoaming) {
            log("isNrAdvanced: false because NR advanced is unavailable while roaming.");
            return false;
        }
        if (this.mNrAdvancedThresholdBandwidth <= 0 || getNrBandwidth(list) >= this.mNrAdvancedThresholdBandwidth) {
            return isNrMmwave() || isAdditionalNrAdvancedBand(set);
        }
        log("isNrAdvanced: false because bandwidths=" + list + " does not meet the threshold=" + this.mNrAdvancedThresholdBandwidth);
        return false;
    }

    private boolean isNrMmwave() {
        return this.mServiceState.getNrFrequencyRange() == 4;
    }

    protected int getNrBandwidth(List<Integer> list) {
        if (this.mNrAdvancedRequiresSingleCcAboveBandwidthThreshold) {
            return list.stream().max(new NetworkTypeController$$ExternalSyntheticLambda0()).orElse(0).intValue();
        }
        return list.stream().mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).sum();
    }

    protected boolean isAdditionalNrAdvancedBand(Set<Integer> set) {
        if (this.mAdditionalNrAdvancedBands.isEmpty() || set.isEmpty()) {
            if (this.mAdditionalNrAdvancedBands.isEmpty()) {
                return false;
            }
            log("isAdditionalNrAdvancedBand: false because bands are empty; configs=" + this.mAdditionalNrAdvancedBands + ", bands=" + set);
            return false;
        }
        HashSet hashSet = new HashSet(this.mAdditionalNrAdvancedBands);
        hashSet.retainAll(set);
        return !hashSet.isEmpty();
    }

    protected boolean isPhysicalLinkActive() {
        return this.mPhysicalLinkStatus == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPhysicalLinkStatusFromPhysicalChannelConfig() {
        List<PhysicalChannelConfig> list = this.mPhysicalChannelConfigs;
        return (list == null || list.isEmpty() || this.mDoesPccListIndicateIdle) ? 1 : 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getEventName(int i) {
        try {
            return sEvents[i];
        } catch (ArrayIndexOutOfBoundsException unused) {
            return "EVENT_NOT_DEFINED";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUsingPhysicalChannelConfigForRrcDetection() {
        return this.mIsPhysicalChannelConfig16Supported && !this.mIsUsingUserDataForRrcDetection;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.telephony.StateMachine
    public void log(String str) {
        Rlog.d("NetworkTypeController", "[" + this.mPhone.getPhoneId() + "] " + str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.telephony.StateMachine
    public void loge(String str) {
        Rlog.e("NetworkTypeController", "[" + this.mPhone.getPhoneId() + "] " + str);
    }

    @Override // com.android.internal.telephony.StateMachine
    public String toString() {
        return "mOverrideTimerRules=" + this.mOverrideTimerRules.toString() + ", mLteEnhancedPattern=" + this.mLteEnhancedPattern + ", mIsPhysicalChannelConfigOn=" + this.mIsPhysicalChannelConfigOn + ", mIsPrimaryTimerActive=" + this.mIsPrimaryTimerActive + ", mIsSecondaryTimerActive=" + this.mIsSecondaryTimerActive + ", mPrimaryTimerState=" + this.mPrimaryTimerState + ", mSecondaryTimerState=" + this.mSecondaryTimerState + ", mPreviousState=" + this.mPreviousState + ", mIsNrAdvanced=" + isNrAdvanced();
    }

    @Override // com.android.internal.telephony.StateMachine
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, " ");
        indentingPrintWriter.print("NetworkTypeController: ");
        super.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.flush();
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("mSubId=" + this.mPhone.getSubId());
        indentingPrintWriter.println("mOverrideTimerRules=" + this.mOverrideTimerRules);
        indentingPrintWriter.println("mLteEnhancedPattern=" + this.mLteEnhancedPattern);
        indentingPrintWriter.println("mIsPhysicalChannelConfigOn=" + this.mIsPhysicalChannelConfigOn);
        indentingPrintWriter.println("mIsPrimaryTimerActive=" + this.mIsPrimaryTimerActive);
        indentingPrintWriter.println("mPciChangedDuringPrimaryTimer=" + this.mPciChangedDuringPrimaryTimer);
        indentingPrintWriter.println("mIsSecondaryTimerActive=" + this.mIsSecondaryTimerActive);
        indentingPrintWriter.println("mIsTimerResetEnabledForLegacyStateRrcIdle=" + this.mIsTimerResetEnabledForLegacyStateRrcIdle);
        indentingPrintWriter.println("mLtePlusThresholdBandwidth=" + this.mLtePlusThresholdBandwidth);
        indentingPrintWriter.println("mNrAdvancedThresholdBandwidth=" + this.mNrAdvancedThresholdBandwidth);
        indentingPrintWriter.println("mIncludeLteForNrAdvancedThresholdBandwidth=" + this.mIncludeLteForNrAdvancedThresholdBandwidth);
        indentingPrintWriter.println("mRatchetPccFieldsForSameAnchorNrCell=" + this.mRatchetPccFieldsForSameAnchorNrCell);
        indentingPrintWriter.println("mUseModemDisplayNetworkType=" + this.mUseModemDisplayNetworkType);
        indentingPrintWriter.println("mRatchetedNrBandwidths=" + this.mRatchetedNrBandwidths);
        indentingPrintWriter.println("mAdditionalNrAdvancedBandsList=" + this.mAdditionalNrAdvancedBands);
        indentingPrintWriter.println("mRatchetedNrBands=" + this.mRatchetedNrBands);
        indentingPrintWriter.println("mLastAnchorNrCellId=" + this.mLastAnchorNrCellId);
        indentingPrintWriter.println("mDoesPccListIndicateIdle=" + this.mDoesPccListIndicateIdle);
        indentingPrintWriter.println("mPrimaryTimerState=" + this.mPrimaryTimerState);
        indentingPrintWriter.println("mSecondaryTimerState=" + this.mSecondaryTimerState);
        indentingPrintWriter.println("mPreviousState=" + this.mPreviousState);
        indentingPrintWriter.println("mPhysicalLinkStatus=" + DataUtils.linkStatusToString(this.mPhysicalLinkStatus));
        indentingPrintWriter.println("mIsPhysicalChannelConfig16Supported=" + this.mIsPhysicalChannelConfig16Supported);
        indentingPrintWriter.println("mIsNrAdvancedAllowedByPco=" + this.mIsNrAdvancedAllowedByPco);
        indentingPrintWriter.println("mNrAdvancedCapablePcoId=" + this.mNrAdvancedCapablePcoId);
        indentingPrintWriter.println("mIsUsingUserDataForRrcDetection=" + this.mIsUsingUserDataForRrcDetection);
        indentingPrintWriter.println("mPrimaryCellChangedWhileIdle=" + this.mPrimaryCellChangedWhileIdle);
        indentingPrintWriter.println("mEnableNrAdvancedWhileRoaming=" + this.mEnableNrAdvancedWhileRoaming);
        indentingPrintWriter.println("mIsDeviceIdleMode=" + this.mIsDeviceIdleMode);
        indentingPrintWriter.println("mIsTimerResetEnabledOnVoiceQos=" + this.mIsTimerResetEnabledOnVoiceQos);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.flush();
    }
}
