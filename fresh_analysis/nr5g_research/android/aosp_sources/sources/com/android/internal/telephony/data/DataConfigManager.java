package com.android.internal.telephony.data;

import android.R;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.DeviceConfig;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.data.ApnSetting;
import android.text.TextUtils;
import android.util.ArraySet;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda3;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.configupdate.ConfigParser;
import com.android.internal.telephony.configupdate.ConfigProviderAdaptor;
import com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class DataConfigManager extends Handler {
    private static final long REEVALUATE_BOOTSTRAP_SIM_DATA_USAGE_MILLIS = 60000;
    private final Map mAutoDataSwitchNetworkTypeSignalMap;
    private final Map mBandwidthMap;
    private final Set mCapabilitiesExemptFromSingleDataList;
    protected PersistableBundle mCarrierConfig;
    private final CarrierConfigManager mCarrierConfigManager;
    private DataConfig mDataConfig;
    private final Set mDataConfigManagerCallbacks;
    private final List mDataHandoverRetryRules;
    private final List mDataSetupRetryRules;
    private final List mEnabledVopsNetworkTypesInNonVops;
    private final FeatureFlags mFeatureFlags;
    private final List mHandoverRuleList;
    private EventFrequency mImsReleaseRequestAnomalyReportThreshold;
    private boolean mIsApnConfigAnomalyReportEnabled;
    private boolean mIsInvalidQnsParamAnomalyReportEnabled;
    protected String mLogTag;
    private final Set mMeteredApnTypes;
    private final Map mNetworkCapabilityPriorityMap;
    private int mNetworkConnectingTimeout;
    private int mNetworkDisconnectingTimeout;
    private int mNetworkHandoverTimeout;
    private EventFrequency mNetworkUnwantedAnomalyReportThreshold;
    protected final Phone mPhone;
    protected Resources mResources;
    private final Set mRoamingMeteredApnTypes;
    private final Set mRoamingUnmeteredNetworkTypes;
    private EventFrequency mSetupDataCallAnomalyReportThreshold;
    private boolean mShouldKeepNetworkUpInNonVops;
    private final List mSingleDataNetworkTypeList;
    private final Map mTcpBufferSizeMap;
    private final Set mUnmeteredNetworkTypes;

    private int connectionCapabilityToNetworkCapabilityStatic(int i) {
        if (i == 1) {
            return 4;
        }
        if (i == 2) {
            return 0;
        }
        if (i == 4) {
            return 1;
        }
        if (i == 8) {
            return 12;
        }
        if (i == 163) {
            return 35;
        }
        if (i != 166) {
            return i != 167 ? -1 : 38;
        }
        return 34;
    }

    private int networkCapabilityToConnectionCapabilityStatic(int i) {
        if (i == 0) {
            return 2;
        }
        if (i == 1) {
            return 4;
        }
        if (i == 4) {
            return 1;
        }
        if (i == 12) {
            return 8;
        }
        if (i == 38) {
            return NtnRILConstants.MI_NTN_RIL_SET_SATELLITE_SIM_POWER;
        }
        if (i == 34) {
            return NtnRILConstants.MI_NTN_RIL_QUERY_CURRENT_SATELLITE_CAPABILITY;
        }
        if (i != 35) {
            return 0;
        }
        return NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_VOICE_CTRL_VERSION;
    }

    public void onCarrierConfigLoadedForEssentialRecords() {
    }

    protected void updateMtkExtendConfig(PersistableBundle persistableBundle) {
    }

    public DataConfigManager(Phone phone, Looper looper, FeatureFlags featureFlags) {
        super(looper);
        this.mDataConfigManagerCallbacks = new ArraySet();
        this.mCarrierConfig = null;
        this.mResources = null;
        this.mNetworkCapabilityPriorityMap = new ConcurrentHashMap();
        this.mDataSetupRetryRules = new ArrayList();
        this.mDataHandoverRetryRules = new ArrayList();
        this.mMeteredApnTypes = new HashSet();
        this.mRoamingMeteredApnTypes = new HashSet();
        this.mSingleDataNetworkTypeList = new ArrayList();
        this.mCapabilitiesExemptFromSingleDataList = new HashSet();
        this.mUnmeteredNetworkTypes = new HashSet();
        this.mRoamingUnmeteredNetworkTypes = new HashSet();
        this.mBandwidthMap = new ConcurrentHashMap();
        this.mTcpBufferSizeMap = new ConcurrentHashMap();
        this.mHandoverRuleList = new ArrayList();
        this.mShouldKeepNetworkUpInNonVops = false;
        this.mEnabledVopsNetworkTypesInNonVops = new ArrayList();
        this.mAutoDataSwitchNetworkTypeSignalMap = new ConcurrentHashMap();
        this.mDataConfig = null;
        this.mPhone = phone;
        this.mFeatureFlags = featureFlags;
        this.mLogTag = "DCM-" + phone.getPhoneId();
        log("DataConfigManager created.");
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        this.mCarrierConfigManager = carrierConfigManager;
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda14
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
        DeviceConfig.addOnPropertiesChangedListener("telephony", new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda15
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                this.f$0.lambda$new$1(properties);
            }
        });
        updateCarrierConfig();
        updateDeviceConfig();
        TelephonyConfigUpdateInstallReceiver.getInstance().registerCallback(new Executor() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda16
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }, new AnonymousClass1());
        ConfigParser configParser = TelephonyConfigUpdateInstallReceiver.getInstance().getConfigParser(ConfigProviderAdaptor.DOMAIN_DATA);
        if (configParser != null) {
            this.mDataConfig = (DataConfig) configParser.getConfig();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        if (i == this.mPhone.getPhoneId()) {
            sendEmptyMessage(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(DeviceConfig.Properties properties) {
        if (TextUtils.equals("telephony", properties.getNamespace())) {
            sendEmptyMessage(2);
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.data.DataConfigManager$1, reason: invalid class name */
    class AnonymousClass1 extends ConfigProviderAdaptor.Callback {
        AnonymousClass1() {
        }

        @Override // com.android.internal.telephony.configupdate.ConfigProviderAdaptor.Callback
        public void onChanged(ConfigParser configParser) {
            if (configParser instanceof DataConfigParser) {
                FeatureFlags unused = DataConfigManager.this.mFeatureFlags;
                DataConfig dataConfig = (DataConfig) configParser.getConfig();
                boolean zEquals = Objects.equals(DataConfigManager.this.mDataConfig, dataConfig);
                DataConfigManager dataConfigManager = DataConfigManager.this;
                Object objValueOf = "null";
                if (!zEquals) {
                    final DataConfig.DataConfigDiff dataConfigDiffCalculateDiff = DataConfig.calculateDiff(dataConfigManager.mDataConfig, dataConfig);
                    DataConfigManager.this.mDataConfig = dataConfig;
                    DataConfigManager dataConfigManager2 = DataConfigManager.this;
                    StringBuilder sb = new StringBuilder("DataConfig updated: version=");
                    sb.append(DataConfigManager.this.mDataConfig != null ? Integer.valueOf(DataConfigManager.this.mDataConfig.getVersion()) : "null");
                    sb.append(", diff=");
                    sb.append(dataConfigDiffCalculateDiff);
                    dataConfigManager2.log(sb.toString());
                    DataConfigManager.this.mDataConfigManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataConfigManager$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            DataConfigManager.DataConfigManagerCallback dataConfigManagerCallback = (DataConfigManager.DataConfigManagerCallback) obj;
                            dataConfigManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataConfigManager$1$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    dataConfigManagerCallback.onDynamicConfigChanged(dataConfigDiff);
                                }
                            });
                        }
                    });
                    return;
                }
                StringBuilder sb2 = new StringBuilder("DataConfig update ignored: version=");
                if (DataConfigManager.this.mDataConfig != null) {
                    objValueOf = Integer.valueOf(DataConfigManager.this.mDataConfig.getVersion());
                }
                sb2.append(objValueOf);
                dataConfigManager.log(sb2.toString());
            }
        }
    }

    public static class DataConfigManagerCallback extends DataCallback {
        public void onCarrierConfigChanged() {
        }

        public void onDeviceConfigChanged() {
        }

        public void onDynamicConfigChanged(DataConfig.DataConfigDiff dataConfigDiff) {
        }

        public DataConfigManagerCallback(Executor executor) {
            super(executor);
        }
    }

    public void registerCallback(DataConfigManagerCallback dataConfigManagerCallback) {
        this.mDataConfigManagerCallbacks.add(dataConfigManagerCallback);
    }

    public void unregisterCallback(DataConfigManagerCallback dataConfigManagerCallback) {
        this.mDataConfigManagerCallbacks.remove(dataConfigManagerCallback);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            log("EVENT_CARRIER_CONFIG_CHANGED");
            updateCarrierConfig();
            this.mDataConfigManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda11
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DataConfigManager.$r8$lambda$WP7bw11RDuFOMrH9aIKiUCBVwa8((DataConfigManager.DataConfigManagerCallback) obj);
                }
            });
            MiuiDataControllerStub.get(this.mPhone.getPhoneId()).updateDataConfig(this.mPhone);
            return;
        }
        if (i == 2) {
            log("EVENT_DEVICE_CONFIG_CHANGED");
            updateDeviceConfig();
            this.mDataConfigManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda12
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DataConfigManager.$r8$lambda$C8LuI05QcZrg3eL7bKQPpxorTNE((DataConfigManager.DataConfigManagerCallback) obj);
                }
            });
        } else {
            loge("Unexpected message " + message.what);
        }
    }

    public static /* synthetic */ void $r8$lambda$WP7bw11RDuFOMrH9aIKiUCBVwa8(DataConfigManagerCallback dataConfigManagerCallback) {
        Objects.requireNonNull(dataConfigManagerCallback);
        dataConfigManagerCallback.invokeFromExecutor(new DataConfigManager$$ExternalSyntheticLambda23(dataConfigManagerCallback));
    }

    public static /* synthetic */ void $r8$lambda$C8LuI05QcZrg3eL7bKQPpxorTNE(final DataConfigManagerCallback dataConfigManagerCallback) {
        Objects.requireNonNull(dataConfigManagerCallback);
        dataConfigManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                dataConfigManagerCallback.onDeviceConfigChanged();
            }
        });
    }

    private void updateDeviceConfig() {
        DeviceConfig.Properties properties = DeviceConfig.getProperties("telephony", new String[0]);
        this.mImsReleaseRequestAnomalyReportThreshold = parseSlidingWindowCounterThreshold(properties.getString("anomaly_ims_release_request", (String) null), 0L, 2);
        this.mNetworkUnwantedAnomalyReportThreshold = parseSlidingWindowCounterThreshold(properties.getString("anomaly_network_unwanted", (String) null), 0L, 12);
        this.mSetupDataCallAnomalyReportThreshold = parseSlidingWindowCounterThreshold(properties.getString("anomaly_setup_data_call_failure", (String) null), 0L, 12);
        this.mIsInvalidQnsParamAnomalyReportEnabled = properties.getBoolean("anomaly_qns_param", false);
        this.mNetworkConnectingTimeout = properties.getInt("anomaly_network_connecting_timeout", 300000);
        this.mNetworkDisconnectingTimeout = properties.getInt("anomaly_network_disconnecting_timeout", 300000);
        this.mNetworkHandoverTimeout = properties.getInt("anomaly_network_handover_timeout", 300000);
        this.mIsApnConfigAnomalyReportEnabled = properties.getBoolean("anomaly_apn_config_enabled", false);
    }

    public boolean isConfigCarrierSpecific() {
        return this.mCarrierConfig.getBoolean("carrier_config_applied_bool");
    }

    public void callUpdateCarrierConfig() {
        log("callUpdateCarrierConfig()");
        updateCarrierConfig();
        this.mDataConfigManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda22
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DataConfigManager.$r8$lambda$mAt0EPdjH9w7iTuYNU1jjuecfNs((DataConfigManager.DataConfigManagerCallback) obj);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$mAt0EPdjH9w7iTuYNU1jjuecfNs(DataConfigManagerCallback dataConfigManagerCallback) {
        Objects.requireNonNull(dataConfigManagerCallback);
        dataConfigManagerCallback.invokeFromExecutor(new DataConfigManager$$ExternalSyntheticLambda23(dataConfigManagerCallback));
    }

    protected void updateCarrierConfig() {
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        if (carrierConfigManager != null) {
            this.mCarrierConfig = carrierConfigManager.getConfigForSubId(this.mPhone.getSubId());
        }
        if (this.mCarrierConfig == null) {
            this.mCarrierConfig = CarrierConfigManager.getDefaultConfig();
        }
        this.mResources = SubscriptionManager.getResourcesForSubId(this.mPhone.getContext(), this.mPhone.getSubId());
        updateNetworkCapabilityPriority();
        updateDataRetryRules();
        updateMeteredApnTypes();
        updateSingleDataNetworkTypeAndCapabilityExemption();
        updateVopsConfig();
        updateUnmeteredNetworkTypes();
        updateBandwidths();
        updateTcpBuffers();
        updateHandoverRules();
        updateAutoDataSwitchConfig();
        updateMtkExtendConfig(this.mCarrierConfig);
        StringBuilder sb = new StringBuilder("Carrier config updated. Config is ");
        sb.append(isConfigCarrierSpecific() ? "" : "not ");
        sb.append("carrier specific.");
        log(sb.toString());
    }

    private void updateNetworkCapabilityPriority() {
        synchronized (this) {
            try {
                this.mNetworkCapabilityPriorityMap.clear();
                String[] stringArray = this.mCarrierConfig.getStringArray("telephony_network_capability_priorities_string_array");
                if (stringArray != null) {
                    for (String str : stringArray) {
                        String upperCase = str.trim().toUpperCase(Locale.ROOT);
                        String[] strArrSplit = upperCase.split(":");
                        if (strArrSplit.length != 2) {
                            loge("Invalid config \"" + upperCase + "\"");
                        } else {
                            int networkCapabilityFromString = DataUtils.getNetworkCapabilityFromString(strArrSplit[0]);
                            if (networkCapabilityFromString < 0) {
                                loge("Invalid config \"" + upperCase + "\"");
                            } else {
                                this.mNetworkCapabilityPriorityMap.put(Integer.valueOf(networkCapabilityFromString), Integer.valueOf(Integer.parseInt(strArrSplit[1])));
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int getNetworkCapabilityPriority(int i) {
        return ((Integer) this.mNetworkCapabilityPriorityMap.getOrDefault(Integer.valueOf(i), 0)).intValue();
    }

    /* JADX WARN: Code duplicated, block: B:17:0x004f A[Catch: all -> 0x0022, TryCatch #1 {, blocks: (B:3:0x0001, B:5:0x0011, B:7:0x0015, B:8:0x0017, B:14:0x003d, B:13:0x0025, B:15:0x0040, B:17:0x004f, B:19:0x0052, B:20:0x0054, B:24:0x0078, B:23:0x0060, B:25:0x007b), top: B:31:0x0001, inners: #0, #2 }] */
    /* JADX WARN: Code duplicated, block: B:19:0x0052 A[Catch: all -> 0x0022, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:5:0x0011, B:7:0x0015, B:8:0x0017, B:14:0x003d, B:13:0x0025, B:15:0x0040, B:17:0x004f, B:19:0x0052, B:20:0x0054, B:24:0x0078, B:23:0x0060, B:25:0x007b), top: B:31:0x0001, inners: #0, #2 }] */
    /* JADX WARN: Instruction removed from duplicated block: B:17:0x004f, please report this as an issue */
    private void updateDataRetryRules() {
        String[] stringArray;
        synchronized (this) {
            this.mDataSetupRetryRules.clear();
            String[] stringArray2 = this.mCarrierConfig.getStringArray("telephony_data_setup_retry_rules_string_array");
            if (stringArray2 != null) {
                for (String str : stringArray2) {
                    try {
                        this.mDataSetupRetryRules.add(new DataRetryManager.DataSetupRetryRule(str));
                    } catch (IllegalArgumentException e) {
                        loge("updateDataRetryRules: " + e.getMessage());
                    }
                }
                this.mDataHandoverRetryRules.clear();
                stringArray = this.mCarrierConfig.getStringArray("telephony_data_handover_retry_rules_string_array");
                if (stringArray != null) {
                    for (String str2 : stringArray) {
                        try {
                            this.mDataHandoverRetryRules.add(new DataRetryManager.DataHandoverRetryRule(str2));
                        } catch (IllegalArgumentException e2) {
                            loge("updateDataRetryRules: " + e2.getMessage());
                        }
                    }
                }
            } else {
                this.mDataHandoverRetryRules.clear();
                stringArray = this.mCarrierConfig.getStringArray("telephony_data_handover_retry_rules_string_array");
                if (stringArray != null) {
                    while (i < r2) {
                        this.mDataHandoverRetryRules.add(new DataRetryManager.DataHandoverRetryRule(str2));
                    }
                }
            }
            throw th;
        }
    }

    public List<DataRetryManager.DataSetupRetryRule> getDataSetupRetryRules() {
        return Collections.unmodifiableList(this.mDataSetupRetryRules);
    }

    public List<DataRetryManager.DataHandoverRetryRule> getDataHandoverRetryRules() {
        return Collections.unmodifiableList(this.mDataHandoverRetryRules);
    }

    public boolean isDataRoamingEnabledByDefault() {
        return this.mCarrierConfig.getBoolean("carrier_default_data_roaming_enabled_bool");
    }

    private void updateMeteredApnTypes() {
        synchronized (this) {
            try {
                this.mMeteredApnTypes.clear();
                String[] stringArray = this.mCarrierConfig.getStringArray("carrier_metered_apn_types_strings");
                if (stringArray != null) {
                    Stream map = Arrays.stream(stringArray).map(new Function() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return Integer.valueOf(ApnSetting.getApnTypeInt((String) obj));
                        }
                    });
                    final Set set = this.mMeteredApnTypes;
                    Objects.requireNonNull(set);
                    map.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            set.add((Integer) obj);
                        }
                    });
                }
                this.mRoamingMeteredApnTypes.clear();
                String[] stringArray2 = this.mCarrierConfig.getStringArray("carrier_metered_roaming_apn_types_strings");
                if (stringArray2 != null) {
                    Stream map2 = Arrays.stream(stringArray2).map(new Function() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return Integer.valueOf(ApnSetting.getApnTypeInt((String) obj));
                        }
                    });
                    final Set set2 = this.mRoamingMeteredApnTypes;
                    Objects.requireNonNull(set2);
                    map2.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            set2.add((Integer) obj);
                        }
                    });
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public Set<Integer> getMeteredNetworkCapabilities(boolean z) {
        Set set = (Set) (z ? this.mRoamingMeteredApnTypes : this.mMeteredApnTypes).stream().map(new DataConfigManager$$ExternalSyntheticLambda18()).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda19
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataConfigManager.$r8$lambda$fpXXdX4mKEF1coVghlORw4YY7Z4((Integer) obj);
            }
        }).collect(Collectors.toSet());
        DataConfig dataConfig = this.mDataConfig;
        Set<Integer> meteredNetworkCapabilities = dataConfig != null ? dataConfig.getMeteredNetworkCapabilities(this.mPhone.getCarrierId(), z) : null;
        if (meteredNetworkCapabilities != null) {
            set.addAll(meteredNetworkCapabilities);
        } else {
            set.add(35);
            set.add(34);
            set.add(38);
        }
        return Collections.unmodifiableSet(set);
    }

    public static /* synthetic */ boolean $r8$lambda$fpXXdX4mKEF1coVghlORw4YY7Z4(Integer num) {
        return num.intValue() >= 0;
    }

    public boolean isTetheringProfileDisabledForRoaming() {
        return this.mCarrierConfig.getBoolean("disable_dun_apn_while_roaming_with_preset_apn_bool");
    }

    public boolean isMeteredCapability(int i, boolean z) {
        return getMeteredNetworkCapabilities(z).contains(Integer.valueOf(i));
    }

    public boolean isAnyMeteredCapability(int[] iArr, final boolean z) {
        return Arrays.stream(iArr).boxed().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$isAnyMeteredCapability$6(z, (Integer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$isAnyMeteredCapability$6(boolean z, Integer num) {
        return isMeteredCapability(num.intValue(), z);
    }

    public boolean shouldUseDataActivityForRrcDetection() {
        return this.mCarrierConfig.getBoolean("lte_endc_using_user_data_for_rrc_detection_bool");
    }

    private void updateSingleDataNetworkTypeAndCapabilityExemption() {
        synchronized (this) {
            try {
                this.mSingleDataNetworkTypeList.clear();
                this.mCapabilitiesExemptFromSingleDataList.clear();
                int[] intArray = this.mCarrierConfig.getIntArray("only_single_dc_allowed_int_array");
                if (intArray != null) {
                    IntStream intStreamStream = Arrays.stream(intArray);
                    List list = this.mSingleDataNetworkTypeList;
                    Objects.requireNonNull(list);
                    intStreamStream.forEach(new DataConfigManager$$ExternalSyntheticLambda13(list));
                }
                int[] intArray2 = this.mCarrierConfig.getIntArray("capabilities_exempt_from_single_dc_check_int_array");
                if (intArray2 != null) {
                    IntStream intStreamStream2 = Arrays.stream(intArray2);
                    Set set = this.mCapabilitiesExemptFromSingleDataList;
                    Objects.requireNonNull(set);
                    intStreamStream2.forEach(new NetworkTypeController$$ExternalSyntheticLambda3(set));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private synchronized void updateVopsConfig() {
        this.mShouldKeepNetworkUpInNonVops = this.mCarrierConfig.getBoolean("ims.keep_pdn_up_in_no_vops_bool");
        int[] intArray = this.mCarrierConfig.getIntArray("ims.ims_pdn_enabled_in_no_vops_support_int_array");
        if (intArray != null) {
            IntStream intStreamStream = Arrays.stream(intArray);
            List list = this.mEnabledVopsNetworkTypesInNonVops;
            Objects.requireNonNull(list);
            intStreamStream.forEach(new DataConfigManager$$ExternalSyntheticLambda13(list));
        }
    }

    public List<Integer> getNetworkTypesOnlySupportSingleDataNetwork() {
        return Collections.unmodifiableList(this.mSingleDataNetworkTypeList);
    }

    public Set<Integer> getCapabilitiesExemptFromSingleDataNetwork() {
        return Collections.unmodifiableSet(this.mCapabilitiesExemptFromSingleDataList);
    }

    public boolean shouldKeepNetworkUpInNonVops(int i) {
        return this.mShouldKeepNetworkUpInNonVops || allowBringUpNetworkInNonVops(i);
    }

    public boolean allowBringUpNetworkInNonVops(int i) {
        int i2 = 1;
        if (i == 1) {
            i2 = 0;
        } else if (i != 5) {
            i2 = -1;
        }
        return this.mEnabledVopsNetworkTypesInNonVops.contains(Integer.valueOf(i2));
    }

    public boolean isPingTestBeforeAutoDataSwitchRequired() {
        return this.mResources.getBoolean(R.bool.autofill_dialog_horizontal_space_included);
    }

    public boolean isTempNotMeteredSupportedByCarrier() {
        return this.mCarrierConfig.getBoolean("network_temp_not_metered_supported_bool");
    }

    private void updateUnmeteredNetworkTypes() {
        synchronized (this) {
            try {
                this.mUnmeteredNetworkTypes.clear();
                String[] stringArray = this.mCarrierConfig.getStringArray("unmetered_network_types_string_array");
                if (stringArray != null) {
                    this.mUnmeteredNetworkTypes.addAll(Arrays.asList(stringArray));
                }
                this.mRoamingUnmeteredNetworkTypes.clear();
                String[] stringArray2 = this.mCarrierConfig.getStringArray("roaming_unmetered_network_types_string_array");
                if (stringArray2 != null) {
                    this.mRoamingUnmeteredNetworkTypes.addAll(Arrays.asList(stringArray2));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean isNetworkTypeUnmetered(TelephonyDisplayInfo telephonyDisplayInfo, ServiceState serviceState) {
        String dataConfigNetworkType = getDataConfigNetworkType(telephonyDisplayInfo);
        if (serviceState.getDataRoaming()) {
            return this.mRoamingUnmeteredNetworkTypes.contains(dataConfigNetworkType);
        }
        return this.mUnmeteredNetworkTypes.contains(dataConfigNetworkType);
    }

    private void updateBandwidths() {
        synchronized (this) {
            try {
                this.mBandwidthMap.clear();
                String[] stringArray = this.mCarrierConfig.getStringArray("bandwidth_string_array");
                boolean z = this.mCarrierConfig.getBoolean("bandwidth_nr_nsa_use_lte_value_for_uplink_bool");
                if (stringArray != null) {
                    for (String str : stringArray) {
                        String[] strArrSplit = str.split(":");
                        if (strArrSplit.length != 2) {
                            loge("Invalid bandwidth: " + str);
                        } else {
                            String[] strArrSplit2 = strArrSplit[1].split(",");
                            if (strArrSplit2.length != 2) {
                                loge("Invalid bandwidth values: " + Arrays.toString(strArrSplit2));
                            } else {
                                try {
                                    int i = Integer.parseInt(strArrSplit2[0]);
                                    int i2 = Integer.parseInt(strArrSplit2[1]);
                                    if (z && strArrSplit[0].startsWith("NR")) {
                                        i2 = ((DataNetwork.NetworkBandwidth) this.mBandwidthMap.get("LTE")).uplinkBandwidthKbps;
                                    }
                                    this.mBandwidthMap.put(strArrSplit[0], new DataNetwork.NetworkBandwidth(i, i2));
                                } catch (NumberFormatException e) {
                                    loge("Exception parsing bandwidth values for network type " + strArrSplit[0] + ": " + e);
                                }
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public DataNetwork.NetworkBandwidth getBandwidthForNetworkType(TelephonyDisplayInfo telephonyDisplayInfo) {
        DataNetwork.NetworkBandwidth networkBandwidth = (DataNetwork.NetworkBandwidth) this.mBandwidthMap.get(getDataConfigNetworkType(telephonyDisplayInfo));
        return networkBandwidth != null ? networkBandwidth : new DataNetwork.NetworkBandwidth(14, 14);
    }

    public boolean isDataRoamingAllowedOnSatellite() {
        return this.mCarrierConfig.getBoolean("satellite_ignore_data_roaming_setting_bool");
    }

    public boolean shouldResetDataThrottlingWhenTacChanges() {
        return this.mCarrierConfig.getBoolean("unthrottle_data_retry_when_tac_changes_bool");
    }

    public String getDataServicePackageName() {
        return this.mCarrierConfig.getString("carrier_data_service_wwan_package_override_string");
    }

    public int getDefaultMtu() {
        return this.mCarrierConfig.getInt("default_mtu_int");
    }

    public long getEsimBootStrapMaxDataLimitBytes() {
        return this.mResources.getInteger(R.integer.config_lidOpenRotation);
    }

    public long getReevaluateBootstrapSimDataUsageMillis() {
        long integer = this.mResources.getInteger(R.integer.config_stableDeviceDisplayHeight);
        return integer <= 0 ? REEVALUATE_BOOTSTRAP_SIM_DATA_USAGE_MILLIS : integer;
    }

    private void updateTcpBuffers() {
        synchronized (this) {
            try {
                this.mTcpBufferSizeMap.clear();
                String[] stringArray = this.mResources.getStringArray(R.array.config_securityStatePackages);
                if (stringArray != null) {
                    for (String str : stringArray) {
                        String[] strArrSplit = str.split(":");
                        if (strArrSplit.length != 2) {
                            loge("Invalid TCP buffer sizes entry: " + str);
                        } else if (strArrSplit[1].split(",").length != 6) {
                            loge("Invalid TCP buffer sizes for " + strArrSplit[0] + ": " + strArrSplit[1]);
                        } else {
                            this.mTcpBufferSizeMap.put(strArrSplit[0], strArrSplit[1]);
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public EventFrequency getAnomalySetupDataCallThreshold() {
        return this.mSetupDataCallAnomalyReportThreshold;
    }

    public EventFrequency getAnomalyNetworkUnwantedThreshold() {
        return this.mNetworkUnwantedAnomalyReportThreshold;
    }

    public EventFrequency getAnomalyImsReleaseRequestThreshold() {
        return this.mImsReleaseRequestAnomalyReportThreshold;
    }

    public boolean isInvalidQnsParamAnomalyReportEnabled() {
        return this.mIsInvalidQnsParamAnomalyReportEnabled;
    }

    public int getAnomalyNetworkConnectingTimeoutMs() {
        return this.mNetworkConnectingTimeout;
    }

    public int getAnomalyNetworkDisconnectingTimeoutMs() {
        return this.mNetworkDisconnectingTimeout;
    }

    public int getNetworkHandoverTimeoutMs() {
        return this.mNetworkHandoverTimeout;
    }

    public boolean isApnConfigAnomalyReportEnabled() {
        return this.mIsApnConfigAnomalyReportEnabled;
    }

    private void updateAutoDataSwitchConfig() {
        synchronized (this) {
            try {
                this.mAutoDataSwitchNetworkTypeSignalMap.clear();
                PersistableBundle persistableBundle = this.mCarrierConfig.getPersistableBundle("auto_data_switch_rat_signal_score_string_bundle");
                String[] strArr = {"GPRS", "EDGE", "UMTS", "CDMA", "1xRTT", "EvDo_0", "EvDo_A", "HSDPA", "HSUPA", "HSPA", "EvDo_B", "eHRPD", "iDEN", "LTE", "LTE_CA", "HSPA+", "GSM", "TD_SCDMA", "NR_NSA", "NR_NSA_MMWAVE", "NR_SA", "NR_SA_MMWAVE"};
                if (persistableBundle != null) {
                    for (int i = 0; i < 22; i++) {
                        String str = strArr[i];
                        int[] intArray = persistableBundle.getIntArray(str);
                        if (intArray != null && intArray.length == 5) {
                            for (int i2 = 0; i2 < intArray.length; i2++) {
                                if (intArray[i2] < 0) {
                                    loge("Auto switch score must not < 0 for network type " + str);
                                    break;
                                }
                                if (i2 == intArray.length - 1) {
                                    this.mAutoDataSwitchNetworkTypeSignalMap.put(str, intArray);
                                }
                            }
                        } else {
                            loge("Auto switch score table should specify 5 signal strength for network type " + str);
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int getAutoDataSwitchScore(TelephonyDisplayInfo telephonyDisplayInfo, SignalStrength signalStrength) {
        int[] iArr = (int[]) this.mAutoDataSwitchNetworkTypeSignalMap.get(getDataConfigNetworkType(telephonyDisplayInfo));
        if (iArr != null) {
            return iArr[signalStrength.getLevel()];
        }
        return 0;
    }

    public int getAutoDataSwitchScoreTolerance() {
        return this.mResources.getInteger(R.integer.config_activityShortDur);
    }

    public int getAutoDataSwitchValidationMaxRetry() {
        return this.mResources.getInteger(R.integer.config_aggregatedPowerStatsSpanDuration);
    }

    public long getAutoDataSwitchAvailabilityStabilityTimeThreshold() {
        return this.mResources.getInteger(R.integer.config_accumulatedBatteryUsageStatsSpanSize);
    }

    public long getAutoDataSwitchPerformanceStabilityTimeThreshold() {
        return this.mResources.getInteger(R.integer.config_activityDefaultDur);
    }

    public long getAutoDataSwitchAvailabilitySwitchbackStabilityTimeThreshold() {
        return this.mResources.getInteger(R.integer.config_activeTaskDurationHours);
    }

    public String getTcpConfigString(TelephonyDisplayInfo telephonyDisplayInfo) {
        String str = (String) this.mTcpBufferSizeMap.get(getDataConfigNetworkType(telephonyDisplayInfo));
        return TextUtils.isEmpty(str) ? getDefaultTcpConfigString() : str;
    }

    public String getDefaultTcpConfigString() {
        return this.mResources.getString(R.string.duration_years_medium_future);
    }

    public long getImsDeregistrationDelay() {
        return this.mResources.getInteger(R.integer.config_dreamCloseAnimationDuration);
    }

    public boolean isDefaultImsApnEnabled() {
        return this.mCarrierConfig.getBoolean("use_default_ims_apn_when_absent_bool");
    }

    public boolean shouldPersistIwlanDataNetworksWhenDataServiceRestarted() {
        return this.mResources.getBoolean(17892107);
    }

    public boolean isIwlanHandoverPolicyEnabled() {
        return this.mResources.getBoolean(R.bool.config_hotswapCapable);
    }

    public boolean isImsDelayTearDownUntilVoiceCallEndEnabled() {
        return this.mCarrierConfig.getBoolean("delay_ims_tear_down_until_call_end_bool");
    }

    public boolean isApnMatchedRequired() {
        return this.mCarrierConfig.getBoolean("apn_matched_required");
    }

    public int getBandwidthEstimateSource() {
        String string = this.mResources.getString(R.string.config_mt_sms_polling_text);
        string.hashCode();
        switch (string) {
            case "carrier_config":
                return 2;
            case "modem":
                return 1;
            case "bandwidth_estimator":
                return 3;
            default:
                loge("Invalid bandwidth estimation source config: ".concat(string));
                return 0;
        }
    }

    private static String getDataConfigNetworkType(TelephonyDisplayInfo telephonyDisplayInfo) {
        int networkType = telephonyDisplayInfo.getNetworkType();
        int overrideNetworkType = telephonyDisplayInfo.getOverrideNetworkType();
        if (overrideNetworkType == 1 || overrideNetworkType == 2) {
            return "LTE_CA";
        }
        if (overrideNetworkType == 3) {
            return "NR_NSA";
        }
        if (overrideNetworkType != 5) {
            return networkTypeToDataConfigNetworkType(networkType);
        }
        if (networkType == 20) {
            return "NR_SA_MMWAVE";
        }
        return "NR_NSA_MMWAVE";
    }

    public Set<Integer> getUnsupportedNetworkCapabilities() {
        String[] stringArray = this.mCarrierConfig.getStringArray("telephony_unsupported_network_capability_string_array");
        if (stringArray != null) {
            return (Set) Arrays.stream(stringArray).map(new DataConfigManager$$ExternalSyntheticLambda21()).collect(Collectors.toSet());
        }
        return (Set) Arrays.stream(this.mResources.getStringArray(R.array.vendor_disallowed_apps_managed_device)).map(new DataConfigManager$$ExternalSyntheticLambda21()).collect(Collectors.toSet());
    }

    private void updateHandoverRules() {
        synchronized (this) {
            this.mHandoverRuleList.clear();
            String[] stringArray = this.mCarrierConfig.getStringArray("iwlan_handover_policy_string_array");
            if (stringArray != null) {
                for (String str : stringArray) {
                    try {
                        this.mHandoverRuleList.add(new DataNetworkController.HandoverRule(str, this.mFeatureFlags));
                    } catch (IllegalArgumentException e) {
                        loge("updateHandoverRules: " + e.getMessage());
                    }
                }
            }
        }
    }

    public static class EventFrequency {
        public final int eventNumOccurrence;
        public final long timeWindow;

        public EventFrequency(long j, int i) {
            this.timeWindow = j;
            this.eventNumOccurrence = i;
        }

        public String toString() {
            return String.format("EventFrequency=[timeWindow=%d, eventNumOccurrence=%d]", Long.valueOf(this.timeWindow), Integer.valueOf(this.eventNumOccurrence));
        }
    }

    public EventFrequency parseSlidingWindowCounterThreshold(String str, long j, int i) {
        EventFrequency eventFrequency = new EventFrequency(j, i);
        if (TextUtils.isEmpty(str)) {
            return eventFrequency;
        }
        String[] strArrSplit = str.split(",");
        if (strArrSplit.length != 2) {
            loge("Invalid format: " + str + "Format should be in \"time window in ms,occurrences\". Using default instead.");
            return eventFrequency;
        }
        try {
            try {
                return new EventFrequency(Long.parseLong(strArrSplit[0].trim()), Integer.parseInt(strArrSplit[1].trim()));
            } catch (NumberFormatException e) {
                loge("Exception parsing SlidingWindow occurrence as integer " + strArrSplit[1] + ": " + e);
                return eventFrequency;
            }
        } catch (NumberFormatException e2) {
            loge("Exception parsing SlidingWindow window span " + strArrSplit[0] + ": " + e2);
            return eventFrequency;
        }
    }

    public List<DataNetworkController.HandoverRule> getHandoverRules() {
        return Collections.unmodifiableList(this.mHandoverRuleList);
    }

    public long getRetrySetupAfterDisconnectMillis() {
        return this.mCarrierConfig.getLong("carrier_data_call_apn_retry_after_disconnect_long");
    }

    private static String networkTypeToDataConfigNetworkType(int i) {
        switch (i) {
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "EvDo_0";
            case 6:
                return "EvDo_A";
            case 7:
                return "1xRTT";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 11:
                return "iDEN";
            case 12:
                return "EvDo_B";
            case 13:
                return "LTE";
            case 14:
                return "eHRPD";
            case 15:
                return "HSPA+";
            case 16:
                return "GSM";
            case 17:
                return "TD_SCDMA";
            case 18:
                return "IWLAN";
            case 19:
                return "LTE_CA";
            case 20:
                return "NR_SA";
            default:
                return "";
        }
    }

    public long[] getDataStallRecoveryDelayMillis() {
        return this.mCarrierConfig.getLongArray("data_stall_recovery_timers_long_array");
    }

    public long[] getDataStallRecoveryRandomizationMillis() {
        return this.mCarrierConfig.getLongArray("data_stall_recovery_timers_randomization_millis_long_array");
    }

    public boolean[] getDataStallRecoveryShouldSkipArray() {
        return this.mCarrierConfig.getBooleanArray("data_stall_recovery_should_skip_bool_array");
    }

    public String getDefaultPreferredApn() {
        return TextUtils.emptyIfNull(this.mCarrierConfig.getString("default_preferred_apn_name_string"));
    }

    public int getNrAdvancedCapablePcoId() {
        return this.mCarrierConfig.getInt("nr_advanced_capable_pco_id_int");
    }

    public List<Integer> getAllowedInitialAttachApnTypes() {
        String[] stringArray = this.mCarrierConfig.getStringArray("allowed_initial_attach_apn_types_string_array");
        if (stringArray != null) {
            return (List) Arrays.stream(stringArray).map(new Function() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda20
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return Integer.valueOf(ApnSetting.getApnTypesBitmaskFromString((String) obj));
                }
            }).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    public boolean isEnhancedIwlanHandoverCheckEnabled() {
        return this.mResources.getBoolean(R.bool.config_intrusiveNotificationLed);
    }

    public boolean allowClearInitialAttachDataProfile() {
        return this.mResources.getBoolean(R.bool.allow_test_udfps);
    }

    public boolean shouldHonorRetryTimerForEmergencyNetworkRequest() {
        return this.mResources.getBoolean(R.bool.config_magnification_area);
    }

    public Set<Integer> getForcedCellularTransportCapabilities() {
        return (Set) Arrays.stream(this.mResources.getStringArray(R.array.config_nonPreemptibleInputMethods)).map(new DataConfigManager$$ExternalSyntheticLambda21()).collect(Collectors.toSet());
    }

    public int networkCapabilityToConnectionCapability(int i) {
        Map<Integer, Integer> connectionCapabilities;
        DataConfig dataConfig = this.mDataConfig;
        if (dataConfig != null && (connectionCapabilities = dataConfig.getConnectionCapabilities(this.mPhone.getCarrierId())) != null && connectionCapabilities.containsKey(Integer.valueOf(i))) {
            return connectionCapabilities.get(Integer.valueOf(i)).intValue();
        }
        return networkCapabilityToConnectionCapabilityStatic(i);
    }

    public int connectionCapabilityToNetworkCapability(int i) {
        Map<Integer, Integer> connectionCapabilities;
        DataConfig dataConfig = this.mDataConfig;
        if (dataConfig != null && (connectionCapabilities = dataConfig.getConnectionCapabilities(this.mPhone.getCarrierId())) != null) {
            for (Map.Entry<Integer, Integer> entry : connectionCapabilities.entrySet()) {
                if (entry.getValue().intValue() == i) {
                    return entry.getKey().intValue();
                }
            }
        }
        return connectionCapabilityToNetworkCapabilityStatic(i);
    }

    public boolean isApnMatchedRequired(int i) {
        Map<Integer, Boolean> apnRequired;
        DataConfig dataConfig = this.mDataConfig;
        if (dataConfig != null && (apnRequired = dataConfig.getApnRequired(this.mPhone.getCarrierId())) != null && apnRequired.containsKey(Integer.valueOf(i))) {
            return apnRequired.get(Integer.valueOf(i)).booleanValue();
        }
        return isApnMatchedRequired();
    }

    public boolean isForceIwlanMmsFeatureEnabled() {
        return this.mResources.getBoolean(17892117);
    }

    protected void log(String str) {
        Rlog.d(this.mLogTag, str);
    }

    protected void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        final AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println(DataConfigManager.class.getSimpleName() + "-" + this.mPhone.getPhoneId() + ":");
        androidUtilIndentingPrintWriter.increaseIndent();
        StringBuilder sb = new StringBuilder("isConfigCarrierSpecific=");
        sb.append(isConfigCarrierSpecific());
        androidUtilIndentingPrintWriter.println(sb.toString());
        androidUtilIndentingPrintWriter.println("Network capability priority:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mNetworkCapabilityPriorityMap.forEach(new BiConsumer() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                androidUtilIndentingPrintWriter.print(DataUtils.networkCapabilityToString(((Integer) obj).intValue()) + ":" + ((Integer) obj2) + " ");
            }
        });
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println();
        androidUtilIndentingPrintWriter.println("Data setup retry rules:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mDataSetupRetryRules.forEach(new DataConfigManager$$ExternalSyntheticLambda4(androidUtilIndentingPrintWriter));
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("isIwlanHandoverPolicyEnabled=" + isIwlanHandoverPolicyEnabled());
        androidUtilIndentingPrintWriter.println("Data handover retry rules:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mDataHandoverRetryRules.forEach(new DataConfigManager$$ExternalSyntheticLambda5(androidUtilIndentingPrintWriter));
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("shouldHonorRetryTimerForEmergencyNetworkRequest=" + shouldHonorRetryTimerForEmergencyNetworkRequest());
        androidUtilIndentingPrintWriter.println("mSetupDataCallAnomalyReport=" + this.mSetupDataCallAnomalyReportThreshold);
        androidUtilIndentingPrintWriter.println("mNetworkUnwantedAnomalyReport=" + this.mNetworkUnwantedAnomalyReportThreshold);
        androidUtilIndentingPrintWriter.println("mImsReleaseRequestAnomalyReport=" + this.mImsReleaseRequestAnomalyReportThreshold);
        androidUtilIndentingPrintWriter.println("mIsInvalidQnsParamAnomalyReportEnabled=" + this.mIsInvalidQnsParamAnomalyReportEnabled);
        androidUtilIndentingPrintWriter.println("mNetworkConnectingTimeout=" + this.mNetworkConnectingTimeout);
        androidUtilIndentingPrintWriter.println("mNetworkDisconnectingTimeout=" + this.mNetworkDisconnectingTimeout);
        androidUtilIndentingPrintWriter.println("mNetworkHandoverTimeout=" + this.mNetworkHandoverTimeout);
        androidUtilIndentingPrintWriter.println("mIsApnConfigAnomalyReportEnabled=" + this.mIsApnConfigAnomalyReportEnabled);
        androidUtilIndentingPrintWriter.println("Auto data switch:");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("getAutoDataSwitchScoreTolerance=" + getAutoDataSwitchScoreTolerance());
        this.mAutoDataSwitchNetworkTypeSignalMap.forEach(new BiConsumer() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda6
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                androidUtilIndentingPrintWriter.println(((String) obj) + ":" + Arrays.toString((int[]) obj2));
            }
        });
        androidUtilIndentingPrintWriter.println("getAutoDataSwitchAvailabilityStabilityTimeThreshold=" + getAutoDataSwitchAvailabilityStabilityTimeThreshold());
        androidUtilIndentingPrintWriter.println("getAutoDataSwitchPerformanceStabilityTimeThreshold=" + getAutoDataSwitchPerformanceStabilityTimeThreshold());
        androidUtilIndentingPrintWriter.println("getAutoDataSwitchValidationMaxRetry=" + getAutoDataSwitchValidationMaxRetry());
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Metered APN types=" + ((String) this.mMeteredApnTypes.stream().map(new Function() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ApnSetting.getApnTypeString(((Integer) obj).intValue());
            }
        }).collect(Collectors.joining(","))));
        androidUtilIndentingPrintWriter.println("Roaming metered APN types=" + ((String) this.mRoamingMeteredApnTypes.stream().map(new Function() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ApnSetting.getApnTypeString(((Integer) obj).intValue());
            }
        }).collect(Collectors.joining(","))));
        androidUtilIndentingPrintWriter.println("Single data network types=" + ((String) this.mSingleDataNetworkTypeList.stream().map(new DataConfigManager$$ExternalSyntheticLambda8()).collect(Collectors.joining(","))));
        androidUtilIndentingPrintWriter.println("Capabilities exempt from single PDN=" + ((String) this.mCapabilitiesExemptFromSingleDataList.stream().map(new DataConfigManager$$ExternalSyntheticLambda3()).collect(Collectors.joining(","))));
        androidUtilIndentingPrintWriter.println("mShouldKeepNetworkUpInNonVops=" + this.mShouldKeepNetworkUpInNonVops);
        androidUtilIndentingPrintWriter.println("mEnabledVopsNetworkTypesInNonVops=" + this.mEnabledVopsNetworkTypesInNonVops);
        androidUtilIndentingPrintWriter.println("isPingTestBeforeAutoDataSwitchRequired=" + isPingTestBeforeAutoDataSwitchRequired());
        androidUtilIndentingPrintWriter.println("Unmetered network types=" + String.join(",", this.mUnmeteredNetworkTypes));
        androidUtilIndentingPrintWriter.println("Roaming unmetered network types=" + String.join(",", this.mRoamingUnmeteredNetworkTypes));
        androidUtilIndentingPrintWriter.println("Bandwidths:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mBandwidthMap.forEach(new BiConsumer() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda9
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                androidUtilIndentingPrintWriter.println(((String) obj) + ":" + ((DataNetwork.NetworkBandwidth) obj2));
            }
        });
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("shouldUseDataActivityForRrcDetection=" + shouldUseDataActivityForRrcDetection());
        androidUtilIndentingPrintWriter.println("isTempNotMeteredSupportedByCarrier=" + isTempNotMeteredSupportedByCarrier());
        androidUtilIndentingPrintWriter.println("shouldResetDataThrottlingWhenTacChanges=" + shouldResetDataThrottlingWhenTacChanges());
        androidUtilIndentingPrintWriter.println("Data service package name=" + getDataServicePackageName());
        androidUtilIndentingPrintWriter.println("Default MTU=" + getDefaultMtu());
        androidUtilIndentingPrintWriter.println("TCP buffer sizes by RAT:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mTcpBufferSizeMap.forEach(new BiConsumer() { // from class: com.android.internal.telephony.data.DataConfigManager$$ExternalSyntheticLambda10
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                androidUtilIndentingPrintWriter.println(((String) obj) + ":" + ((String) obj2));
            }
        });
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Default TCP buffer sizes=" + getDefaultTcpConfigString());
        androidUtilIndentingPrintWriter.println("getImsDeregistrationDelay=" + getImsDeregistrationDelay());
        androidUtilIndentingPrintWriter.println("shouldPersistIwlanDataNetworksWhenDataServiceRestarted=" + shouldPersistIwlanDataNetworksWhenDataServiceRestarted());
        androidUtilIndentingPrintWriter.println("Bandwidth estimation source=" + this.mResources.getString(R.string.config_mt_sms_polling_text));
        androidUtilIndentingPrintWriter.println("isImsDelayTearDownUntilVoiceCallEndEnabled=" + isImsDelayTearDownUntilVoiceCallEndEnabled());
        androidUtilIndentingPrintWriter.println("isEnhancedIwlanHandoverCheckEnabled=" + isEnhancedIwlanHandoverCheckEnabled());
        androidUtilIndentingPrintWriter.println("isTetheringProfileDisabledForRoaming=" + isTetheringProfileDisabledForRoaming());
        androidUtilIndentingPrintWriter.println("allowClearInitialAttachDataProfile=" + allowClearInitialAttachDataProfile());
        androidUtilIndentingPrintWriter.println("forcedCellularTransportCapabilities=" + ((String) getForcedCellularTransportCapabilities().stream().map(new DataConfigManager$$ExternalSyntheticLambda3()).collect(Collectors.joining(","))));
        androidUtilIndentingPrintWriter.println("getUnsupportedNetworkCapabilities=" + ((String) getUnsupportedNetworkCapabilities().stream().map(new DataConfigManager$$ExternalSyntheticLambda3()).collect(Collectors.joining(","))));
        androidUtilIndentingPrintWriter.decreaseIndent();
    }
}
