package com.android.internal.telephony.data;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkCapabilities;
import android.net.NetworkPolicyManager;
import android.net.NetworkRequest;
import android.net.NetworkTemplate;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telecom.TelecomManager;
import android.telephony.AccessNetworkConstants;
import android.telephony.AnomalyReporter;
import android.telephony.DataFailCause;
import android.telephony.DataSpecificRegistrationInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PcoData;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionPlan;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.telephony.data.QosBearerSession;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.ImsStateCallback;
import android.telephony.ims.RegistrationManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperDataManager;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.SlidingWindowEventCounter;
import com.android.internal.telephony.TelephonyCapabilities;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.build.UnboundedSdkLevel$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.ims.ImsResolver;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class DataNetworkController extends Handler {
    protected static final int EVENT_REEVALUATE_EXISTING_DATA_NETWORKS = 16;
    public static final int EVENT_REEVALUATE_UNSATISFIED_NETWORK_REQUESTS = 5;
    protected static final int EVENT_SERVICE_STATE_CHANGED = 17;
    protected final AccessNetworksManager mAccessNetworksManager;
    protected final NetworkRequestList mAllNetworkRequestList;
    private boolean mAnyCellularDataNetworkExisting;
    protected boolean mAnyDataNetworkExisting;
    private long mBootStrapSimTotalDataUsageBytes;
    private long mBootstrapSimLastDataUsageQueryTime;
    private final Set mCongestedOverrideNetworkTypes;
    private Set mConnectedInternetNetworks;
    private int mDataActivity;
    protected final DataConfigManager mDataConfigManager;
    private final Set mDataNetworkControllerCallbacks;
    protected final List<DataNetwork> mDataNetworkList;
    protected final DataProfileManager mDataProfileManager;
    protected final DataRetryManager mDataRetryManager;
    private final SparseBooleanArray mDataServiceBound;
    protected final SparseArray<DataServiceManager> mDataServiceManagers;
    protected final DataSettingsManager mDataSettingsManager;
    private final DataStallRecoveryManager mDataStallRecoveryManager;
    private final FeatureFlags mFeatureFlags;
    private IHyperDataManager mIHyperDataManager;
    private int mImsDataNetworkState;
    private final SparseArray mImsFeaturePackageName;
    private final SparseArray mImsFeatureRegistrationCallbacks;
    private final ImsManager mImsManager;
    private final SparseArray mImsStateCallbacks;
    protected SlidingWindowEventCounter mImsThrottleCounter;
    private boolean mInitInternetDataConnectionState;
    private final BroadcastReceiver mIntentReceiver;
    private int mInternetDataNetworkState;
    private int mInternetLinkStatus;
    private boolean mIsSrvccHandoverInProcess;
    private boolean mLastImsOperationIsRelease;
    protected int[] mLastReleasedImsRequestCapabilities;
    protected final LocalLog mLocalLog;
    protected final String mLogTag;
    private final NetworkPolicyManager mNetworkPolicyManager;
    private SlidingWindowEventCounter mNetworkUnwantedCounter;
    private boolean mNrAdvancedCapableByPco;
    private final Map mPendingImsDeregDataNetworks;
    protected final Phone mPhone;
    private final List mPreviousConnectedDataNetworkList;
    private boolean mPsRestricted;
    private final SparseIntArray mRegisteredImsFeaturesTransport;
    protected ServiceState mServiceState;
    private SlidingWindowEventCounter mSetupDataCallWlanFailureCounter;
    private SlidingWindowEventCounter mSetupDataCallWwanFailureCounter;
    protected int mSimState;
    protected int mSubId;
    private final List mSubscriptionPlans;
    private final TelecomManager mTelecomManager;
    private DataNetwork mTempDataNetwork;
    private final Set mUnmeteredOverrideNetworkTypes;
    private static final boolean isTeleOptimizationSupport = SystemProperties.getBoolean("persist.vendor.radio.sim.allfile", false);
    private static final Collection SUPPORTED_IMS_FEATURES = List.of(1, 2);
    private static final long REEVALUATE_PREFERRED_TRANSPORT_DELAY_MILLIS = SatelliteSessionController.DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS;
    private static final long REEVALUATE_UNSATISFIED_NETWORK_REQUESTS_AFTER_CALL_END_DELAY_MILLIS = 500;
    private static final long REEVALUATE_UNSATISFIED_NETWORK_REQUESTS_TAC_CHANGED_DELAY_MILLIS = 100;
    private static final long REEVALUATE_UNSATISFIED_NETWORK_REQUESTS_AFTER_DETACHED_DELAY_MILLIS = 1000;
    private static final long GUARD_TIMER_INTERVAL_TO_QUERY_DATA_USAGE_API_STATS_MILLIS = 1000;

    protected void dataEvaluationforValueAdds(DataEvaluation dataEvaluation, TelephonyNetworkRequest telephonyNetworkRequest) {
    }

    protected int getProfileID(int i) {
        if (i == 0) {
            return 1001;
        }
        int i2 = 1;
        if (i == 1) {
            return 1002;
        }
        if (i != 2) {
            i2 = 3;
            if (i != 3) {
                if (i == 4) {
                    return 2;
                }
                if (i == 5) {
                    return 4;
                }
                if (i == 9) {
                    return 1005;
                }
                if (i == 10) {
                    return 1004;
                }
                if (i == 23) {
                    return 1009;
                }
                if (i != 30) {
                    return i != 31 ? 0 : 1007;
                }
                return 1008;
            }
        }
        return i2;
    }

    protected void logv(String str) {
    }

    protected void mtkEvaluateDataNetwork(DataEvaluation dataEvaluation, DataNetwork dataNetwork, DataEvaluation.DataEvaluationReason dataEvaluationReason, int i) {
    }

    protected void mtkEvaluateNetworkRequest(DataEvaluation dataEvaluation, TelephonyNetworkRequest telephonyNetworkRequest, DataEvaluation.DataEvaluationReason dataEvaluationReason, int i) {
    }

    protected void onDataDuringVoiceCallChanged(boolean z, int i) {
    }

    protected void resetMdDataRetryCount(ApnSetting apnSetting) {
    }

    protected boolean shouldHandRoamingTypeChanged(int i, int i2) {
        return false;
    }

    protected boolean shouldIgnoreHandover(DataNetwork dataNetwork) {
        return false;
    }

    private boolean hasCalling() {
        return TelephonyCapabilities.supportsTelephonyCalling(this.mFeatureFlags, this.mPhone.getContext());
    }

    public static class NetworkRequestList extends LinkedList<TelephonyNetworkRequest> {
        public NetworkRequestList() {
        }

        public NetworkRequestList(NetworkRequestList networkRequestList) {
            addAll(networkRequestList);
        }

        public NetworkRequestList(List<TelephonyNetworkRequest> list) {
            addAll(list);
        }

        public NetworkRequestList(TelephonyNetworkRequest telephonyNetworkRequest) {
            this();
            add(telephonyNetworkRequest);
        }

        @Override // java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque, java.util.Queue
        public boolean add(TelephonyNetworkRequest telephonyNetworkRequest) {
            int i = 0;
            while (i < size()) {
                TelephonyNetworkRequest telephonyNetworkRequest2 = get(i);
                if (telephonyNetworkRequest2.equals(telephonyNetworkRequest)) {
                    return false;
                }
                if (telephonyNetworkRequest.getPriority() > telephonyNetworkRequest2.getPriority()) {
                    break;
                }
                i++;
            }
            super.add(i, telephonyNetworkRequest);
            return true;
        }

        @Override // java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public void add(int i, TelephonyNetworkRequest telephonyNetworkRequest) {
            throw new UnsupportedOperationException("Insertion to certain position is illegal.");
        }

        @Override // java.util.LinkedList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque
        public boolean addAll(Collection<? extends TelephonyNetworkRequest> collection) {
            Iterator<? extends TelephonyNetworkRequest> it = collection.iterator();
            while (it.hasNext()) {
                add(it.next());
            }
            return true;
        }

        public TelephonyNetworkRequest get(int[] iArr) {
            for (int i = 0; i < size(); i++) {
                TelephonyNetworkRequest telephonyNetworkRequest = get(i);
                if (((Set) Arrays.stream(telephonyNetworkRequest.getCapabilities()).boxed().collect(Collectors.toSet())).containsAll(Arrays.stream(iArr).boxed().toList())) {
                    return telephonyNetworkRequest;
                }
            }
            return null;
        }

        public boolean hasNetworkRequestsFromPackage(String str) {
            Iterator<TelephonyNetworkRequest> it = iterator();
            while (it.hasNext()) {
                if (str.equals(it.next().getNativeNetworkRequest().getRequestorPackageName())) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder("[NetworkRequestList: size=");
            sb.append(size());
            if (size() > 0) {
                str = ", leading by " + get(0);
            } else {
                str = "";
            }
            sb.append(str);
            sb.append("]");
            return sb.toString();
        }

        public String toStringSimplified() {
            if (size() > 0) {
                return DataUtils.networkCapabilitiesToString(get(0).getCapabilities()) + "-" + DataUtils.connectivityTransportsToString(get(0).getTransportTypes());
            }
            return "";
        }

        public void dump(AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter) {
            androidUtilIndentingPrintWriter.increaseIndent();
            Iterator<TelephonyNetworkRequest> it = iterator();
            while (it.hasNext()) {
                androidUtilIndentingPrintWriter.println(it.next());
            }
            androidUtilIndentingPrintWriter.decreaseIndent();
        }
    }

    public static class DataNetworkControllerCallback extends DataCallback {
        public void onAnyDataNetworkExistingChanged(boolean z, boolean z2) {
        }

        public void onConnectedInternetDataNetworksChanged(Set<DataNetwork> set) {
        }

        public void onDataNetworkConnected(int i, DataProfile dataProfile) {
        }

        public void onDataServiceBound(int i) {
        }

        public void onInternetDataNetworkValidationStatusChanged(int i) {
        }

        public void onNrAdvancedCapableByPcoChanged(boolean z) {
        }

        public void onPhysicalLinkStatusChanged(int i) {
        }

        public void onQosSessionsChanged(List<QosBearerSession> list) {
        }

        public void onSimStateChanged(int i) {
        }

        public void onSubscriptionPlanOverride() {
        }

        public DataNetworkControllerCallback(Executor executor) {
            super(executor);
        }
    }

    public static class HandoverRule {
        public static final int RULE_TYPE_ALLOWED = 1;
        public static final int RULE_TYPE_DISALLOWED = 2;
        public final boolean isOnlyForIncall;
        public final boolean isOnlyForRoaming;
        public final Set<Integer> networkCapabilities;
        public final Set<Integer> sourceAccessNetworks;
        public final Set<Integer> targetAccessNetworks;
        public final int type;

        public HandoverRule(String str, FeatureFlags featureFlags) {
            if (TextUtils.isEmpty(str)) {
                RILUtils$$ExternalSyntheticBUOutline1.m("illegal rule ", str);
                throw null;
            }
            Set<Integer> networkCapabilitiesFromString = Collections.EMPTY_SET;
            String lowerCase = str.trim().toLowerCase(Locale.ROOT);
            String[] strArrSplit = lowerCase.split("\\s*,\\s*");
            int length = strArrSplit.length;
            int i = 0;
            int i2 = 0;
            boolean z = false;
            boolean z2 = false;
            Set<Integer> set = null;
            Set<Integer> set2 = null;
            while (i < length) {
                String[] strArrSplit2 = strArrSplit[i].trim().split("\\s*=\\s*");
                if (strArrSplit2.length != 2) {
                    throw new IllegalArgumentException("illegal rule " + lowerCase + ", tokens=" + Arrays.toString(strArrSplit2));
                }
                String strTrim = strArrSplit2[0].trim();
                String strTrim2 = strArrSplit2[1].trim();
                try {
                    String[] strArr = strArrSplit;
                    switch (strTrim.hashCode()) {
                        case -1487597642:
                            if (!strTrim.equals("capabilities")) {
                                throw new IllegalArgumentException("unexpected key " + strTrim);
                            }
                            networkCapabilitiesFromString = DataUtils.getNetworkCapabilitiesFromString(strTrim2);
                            i++;
                            strArrSplit = strArr;
                            break;
                            break;
                        case -1184273149:
                            if (!strTrim.equals("incall")) {
                                throw new IllegalArgumentException("unexpected key " + strTrim);
                            }
                            z2 = Boolean.parseBoolean(strTrim2);
                            i++;
                            strArrSplit = strArr;
                            break;
                            break;
                        case -896505829:
                            if (!strTrim.equals("source")) {
                                throw new IllegalArgumentException("unexpected key " + strTrim);
                            }
                            set = (Set) Arrays.stream(strTrim2.split("\\s*\\|\\s*")).map(new DataNetworkController$HandoverRule$$ExternalSyntheticLambda1()).map(new Function() { // from class: com.android.internal.telephony.data.DataNetworkController$HandoverRule$$ExternalSyntheticLambda2
                                @Override // java.util.function.Function
                                public final Object apply(Object obj) {
                                    return Integer.valueOf(AccessNetworkConstants.AccessNetworkType.fromString((String) obj));
                                }
                            }).collect(Collectors.toSet());
                            i++;
                            strArrSplit = strArr;
                            break;
                            break;
                        case -880905839:
                            if (!strTrim.equals("target")) {
                                throw new IllegalArgumentException("unexpected key " + strTrim);
                            }
                            set2 = (Set) Arrays.stream(strTrim2.split("\\s*\\|\\s*")).map(new DataNetworkController$HandoverRule$$ExternalSyntheticLambda1()).map(new Function() { // from class: com.android.internal.telephony.data.DataNetworkController$HandoverRule$$ExternalSyntheticLambda2
                                @Override // java.util.function.Function
                                public final Object apply(Object obj) {
                                    return Integer.valueOf(AccessNetworkConstants.AccessNetworkType.fromString((String) obj));
                                }
                            }).collect(Collectors.toSet());
                            i++;
                            strArrSplit = strArr;
                            break;
                            break;
                        case 3575610:
                            if (!strTrim.equals("type")) {
                                throw new IllegalArgumentException("unexpected key " + strTrim);
                            }
                            Locale locale = Locale.ROOT;
                            if (strTrim2.toLowerCase(locale).equals("allowed")) {
                                i2 = 1;
                            } else {
                                if (!strTrim2.toLowerCase(locale).equals("disallowed")) {
                                    throw new IllegalArgumentException("unexpected rule type " + strTrim2);
                                }
                                i2 = 2;
                            }
                            i++;
                            strArrSplit = strArr;
                            break;
                            break;
                        case 1366973465:
                            if (!strTrim.equals("roaming")) {
                                throw new IllegalArgumentException("unexpected key " + strTrim);
                            }
                            z = Boolean.parseBoolean(strTrim2);
                            i++;
                            strArrSplit = strArr;
                            break;
                            break;
                        default:
                            throw new IllegalArgumentException("unexpected key " + strTrim);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DataRetryManager$DataRetryRule$$ExternalSyntheticBUOutline0.m("illegal rule \"", lowerCase, "\", e=", e);
                    throw null;
                }
            }
            if (set == null || set2 == null || set.isEmpty() || set2.isEmpty()) {
                UnboundedSdkLevel$$ExternalSyntheticBUOutline0.m("Need to specify both source and target. \"", lowerCase, "\"");
                throw null;
            }
            if (set.contains(0) && i2 != 2) {
                UnboundedSdkLevel$$ExternalSyntheticBUOutline0.m("Unknown access network can be only specified in the disallowed rule. \"", lowerCase, "\"");
                throw null;
            }
            if (set2.contains(0)) {
                UnboundedSdkLevel$$ExternalSyntheticBUOutline0.m("Target access networks contains unknown. \"", lowerCase, "\"");
                throw null;
            }
            if (i2 == 0) {
                UnboundedSdkLevel$$ExternalSyntheticBUOutline0.m("Rule type is not specified correctly. \"", lowerCase, "\"");
                throw null;
            }
            if (networkCapabilitiesFromString != null && networkCapabilitiesFromString.contains(-1)) {
                UnboundedSdkLevel$$ExternalSyntheticBUOutline0.m("Network capabilities contains unknown. \"", lowerCase, "\"");
                throw null;
            }
            if (!set.contains(5) && !set2.contains(5)) {
                UnboundedSdkLevel$$ExternalSyntheticBUOutline0.m("IWLAN must be specified in either source or target access networks.\"", lowerCase, "\"");
                throw null;
            }
            this.sourceAccessNetworks = set;
            this.targetAccessNetworks = set2;
            this.type = i2;
            this.networkCapabilities = networkCapabilitiesFromString;
            this.isOnlyForRoaming = z;
            this.isOnlyForIncall = z2;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("[HandoverRule: type=");
            sb.append(this.type == 1 ? "allowed" : "disallowed");
            sb.append(", source=");
            sb.append((String) this.sourceAccessNetworks.stream().map(new Function() { // from class: com.android.internal.telephony.data.DataNetworkController$HandoverRule$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return AccessNetworkConstants.AccessNetworkType.toString(((Integer) obj).intValue());
                }
            }).collect(Collectors.joining("|")));
            sb.append(", target=");
            sb.append((String) this.targetAccessNetworks.stream().map(new Function() { // from class: com.android.internal.telephony.data.DataNetworkController$HandoverRule$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return AccessNetworkConstants.AccessNetworkType.toString(((Integer) obj).intValue());
                }
            }).collect(Collectors.joining("|")));
            sb.append(", isRoaming=");
            sb.append(this.isOnlyForRoaming);
            sb.append(", isIncall=");
            sb.append(this.isOnlyForIncall);
            sb.append(", capabilities=");
            sb.append(DataUtils.networkCapabilitiesToString(this.networkCapabilities));
            sb.append("]");
            return sb.toString();
        }
    }

    protected void onDataEnabledChanged(boolean z, int i) {
        logl("onDataEnabledChanged: enabled=" + z + " reason=" + i);
        sendMessage(obtainMessage(z ? 5 : 16, DataEvaluation.DataEvaluationReason.DATA_ENABLED_CHANGED));
    }

    protected void onDataRoamingEnabledChanged(boolean z) {
        logl("onDataRoamingEnabledChanged: enabled=" + z);
        sendMessage(obtainMessage(z ? 5 : 16, DataEvaluation.DataEvaluationReason.ROAMING_ENABLED_CHANGED));
    }

    public DataNetworkController(Phone phone, Looper looper, FeatureFlags featureFlags) {
        super(looper);
        this.mBootStrapSimTotalDataUsageBytes = 0L;
        this.mBootstrapSimLastDataUsageQueryTime = 0L;
        this.mLocalLog = new LocalLog(128);
        this.mDataServiceManagers = new SparseArray<>();
        this.mSubId = -1;
        this.mSubscriptionPlans = new ArrayList();
        this.mUnmeteredOverrideNetworkTypes = new ArraySet();
        this.mCongestedOverrideNetworkTypes = new ArraySet();
        this.mAllNetworkRequestList = new NetworkRequestList();
        this.mDataNetworkList = new CopyOnWriteArrayList();
        this.mPreviousConnectedDataNetworkList = new ArrayList();
        this.mInternetDataNetworkState = 0;
        this.mConnectedInternetNetworks = new HashSet();
        this.mImsDataNetworkState = 0;
        this.mInternetLinkStatus = -1;
        this.mDataNetworkControllerCallbacks = new ArraySet();
        this.mPsRestricted = false;
        this.mNrAdvancedCapableByPco = false;
        this.mIsSrvccHandoverInProcess = false;
        this.mDataServiceBound = new SparseBooleanArray();
        this.mSimState = 0;
        this.mDataActivity = 0;
        this.mImsStateCallbacks = new SparseArray();
        this.mRegisteredImsFeaturesTransport = new SparseIntArray(2);
        this.mImsFeaturePackageName = new SparseArray();
        this.mPendingImsDeregDataNetworks = new ArrayMap();
        this.mImsFeatureRegistrationCallbacks = new SparseArray();
        this.mTempDataNetwork = null;
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.data.DataNetworkController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                action.hashCode();
                if ((action.equals("android.telephony.action.SIM_APPLICATION_STATE_CHANGED") || action.equals("android.telephony.action.SIM_CARD_STATE_CHANGED")) && DataNetworkController.this.mPhone.getPhoneId() == intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1)) {
                    int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
                    DataNetworkController dataNetworkController = DataNetworkController.this;
                    dataNetworkController.sendMessage(dataNetworkController.obtainMessage(9, intExtra, 0));
                }
            }
        };
        this.mInitInternetDataConnectionState = true;
        this.mPhone = phone;
        this.mFeatureFlags = featureFlags;
        this.mLogTag = "DNC-" + phone.getPhoneId();
        log("DataNetworkController created.");
        AccessNetworksManager accessNetworksManager = phone.getAccessNetworksManager();
        this.mAccessNetworksManager = accessNetworksManager;
        for (int i : accessNetworksManager.getAvailableTransports()) {
            this.mDataServiceManagers.put(i, TelephonyComponentFactory.getInstance().inject(DataServiceManager.class.getName()).makeDataServiceManager(this.mPhone, looper, i, featureFlags));
        }
        DataConfigManager dataConfigManagerMakeDataConfigManager = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeDataConfigManager(this.mPhone, looper, featureFlags);
        this.mDataConfigManager = dataConfigManagerMakeDataConfigManager;
        this.mImsThrottleCounter = new SlidingWindowEventCounter(dataConfigManagerMakeDataConfigManager.getAnomalyImsReleaseRequestThreshold().timeWindow, dataConfigManagerMakeDataConfigManager.getAnomalyImsReleaseRequestThreshold().eventNumOccurrence);
        this.mNetworkUnwantedCounter = new SlidingWindowEventCounter(dataConfigManagerMakeDataConfigManager.getAnomalyNetworkUnwantedThreshold().timeWindow, dataConfigManagerMakeDataConfigManager.getAnomalyNetworkUnwantedThreshold().eventNumOccurrence);
        this.mSetupDataCallWlanFailureCounter = new SlidingWindowEventCounter(dataConfigManagerMakeDataConfigManager.getAnomalySetupDataCallThreshold().timeWindow, dataConfigManagerMakeDataConfigManager.getAnomalySetupDataCallThreshold().eventNumOccurrence);
        this.mSetupDataCallWwanFailureCounter = new SlidingWindowEventCounter(dataConfigManagerMakeDataConfigManager.getAnomalySetupDataCallThreshold().timeWindow, dataConfigManagerMakeDataConfigManager.getAnomalySetupDataCallThreshold().eventNumOccurrence);
        this.mDataSettingsManager = TelephonyComponentFactory.getInstance().inject(DataSettingsManager.class.getName()).makeDataSettingsManager(this.mPhone, this, this.mDataServiceManagers, this.mFeatureFlags, looper, new DataSettingsManager.DataSettingsManagerCallback(new DataNetworkController$$ExternalSyntheticLambda17(this)) { // from class: com.android.internal.telephony.data.DataNetworkController.2
            @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
            public void onDataEnabledChanged(boolean z, int i2, String str) {
                DataNetworkController.this.onDataEnabledChanged(z, i2);
            }

            @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
            public void onDataEnabledOverrideChanged(boolean z, int i2) {
                DataNetworkController.this.logl("onDataEnabledOverrideChanged: enabled=" + z);
                DataNetworkController dataNetworkController = DataNetworkController.this;
                dataNetworkController.sendMessage(dataNetworkController.obtainMessage(z ? 5 : 16, DataEvaluation.DataEvaluationReason.DATA_ENABLED_OVERRIDE_CHANGED));
                DataNetworkController.this.onDataDuringVoiceCallChanged(z, i2);
            }

            @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
            public void onDataRoamingEnabledChanged(boolean z) {
                DataNetworkController.this.onDataRoamingEnabledChanged(z);
            }
        });
        this.mDataProfileManager = TelephonyComponentFactory.getInstance().inject(DataProfileManager.class.getName()).makeDataProfileManager(this.mPhone, this, this.mDataServiceManagers.get(1), looper, this.mFeatureFlags, new DataProfileManager.DataProfileManagerCallback(new DataNetworkController$$ExternalSyntheticLambda17(this)) { // from class: com.android.internal.telephony.data.DataNetworkController.3
            @Override // com.android.internal.telephony.data.DataProfileManager.DataProfileManagerCallback
            public void onDataProfilesChanged() {
                DataNetworkController dataNetworkController = DataNetworkController.this;
                DataEvaluation.DataEvaluationReason dataEvaluationReason = DataEvaluation.DataEvaluationReason.DATA_PROFILES_CHANGED;
                dataNetworkController.sendMessage(dataNetworkController.obtainMessage(16, dataEvaluationReason));
                DataNetworkController dataNetworkController2 = DataNetworkController.this;
                dataNetworkController2.sendMessage(dataNetworkController2.obtainMessage(5, dataEvaluationReason));
                DataNetworkController.this.initiateInternetDataConnectionState();
            }
        });
        DataStallRecoveryManager dataStallRecoveryManagerMakeDataStallRecoveryManager = TelephonyComponentFactory.getInstance().inject(DataStallRecoveryManager.class.getName()).makeDataStallRecoveryManager(this.mPhone, this, this.mDataServiceManagers.get(1), this.mFeatureFlags, looper, new DataStallRecoveryManager.DataStallRecoveryManagerCallback(new DataNetworkController$$ExternalSyntheticLambda17(this)) { // from class: com.android.internal.telephony.data.DataNetworkController.4
            @Override // com.android.internal.telephony.data.DataStallRecoveryManager.DataStallRecoveryManagerCallback
            public void onDataStallReestablishInternet() {
                DataNetworkController.this.onDataStallReestablishInternet();
            }
        });
        this.mDataStallRecoveryManager = dataStallRecoveryManagerMakeDataStallRecoveryManager;
        this.mDataRetryManager = TelephonyComponentFactory.getInstance().inject(DataRetryManager.class.getName()).makeDataRetryManager(this.mPhone, this, this.mDataServiceManagers, looper, this.mFeatureFlags, new DataRetryManager.DataRetryManagerCallback(new DataNetworkController$$ExternalSyntheticLambda17(this)) { // from class: com.android.internal.telephony.data.DataNetworkController.5
            @Override // com.android.internal.telephony.data.DataRetryManager.DataRetryManagerCallback
            public void onDataNetworkSetupRetry(DataRetryManager.DataSetupRetryEntry dataSetupRetryEntry) {
                Objects.requireNonNull(dataSetupRetryEntry);
                DataNetworkController.this.onDataNetworkSetupRetry(dataSetupRetryEntry);
            }

            @Override // com.android.internal.telephony.data.DataRetryManager.DataRetryManagerCallback
            public void onDataNetworkHandoverRetry(DataRetryManager.DataHandoverRetryEntry dataHandoverRetryEntry) {
                Objects.requireNonNull(dataHandoverRetryEntry);
                DataNetworkController.this.onDataNetworkHandoverRetry(dataHandoverRetryEntry);
            }

            @Override // com.android.internal.telephony.data.DataRetryManager.DataRetryManagerCallback
            public void onDataNetworkHandoverRetryStopped(DataNetwork dataNetwork) {
                Objects.requireNonNull(dataNetwork);
                DataNetworkController.this.onDataNetworkHandoverRetryStopped(dataNetwork);
            }
        });
        this.mImsManager = (ImsManager) this.mPhone.getContext().getSystemService(ImsManager.class);
        this.mNetworkPolicyManager = (NetworkPolicyManager) this.mPhone.getContext().getSystemService(NetworkPolicyManager.class);
        this.mTelecomManager = (TelecomManager) this.mPhone.getContext().getSystemService(TelecomManager.class);
        this.mServiceState = this.mPhone.getServiceStateTracker().getServiceState();
        sendEmptyMessage(19);
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).init(this.mPhone, this.mDataNetworkList, this, dataStallRecoveryManagerMakeDataStallRecoveryManager);
        this.mIHyperDataManager = (IHyperDataManager) HyperTelephonyFactory.getInstance().initModule(IHyperDataManager.DEFAULT, this, this.mPhone);
    }

    protected void onRegisterAllEvents() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        this.mPhone.getContext().registerReceiver(this.mIntentReceiver, intentFilter, null, this.mPhone);
        this.mAccessNetworksManager.registerCallback(new AccessNetworksManager.AccessNetworksManagerCallback(new DataNetworkController$$ExternalSyntheticLambda17(this)) { // from class: com.android.internal.telephony.data.DataNetworkController.6
            @Override // com.android.internal.telephony.data.AccessNetworksManager.AccessNetworksManagerCallback
            public void onPreferredTransportChanged(int i, boolean z) {
                int preferredTransportByNetworkCapability = DataNetworkController.this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(i);
                DataNetworkController dataNetworkController = DataNetworkController.this;
                StringBuilder sb = new StringBuilder("onPreferredTransportChanged: ");
                sb.append(DataUtils.networkCapabilityToString(i));
                sb.append(" preferred on ");
                sb.append(AccessNetworkConstants.transportTypeToString(preferredTransportByNetworkCapability));
                sb.append(z ? "forceReconnect:true" : "");
                dataNetworkController.logl(sb.toString());
                DataNetworkController.this.onEvaluatePreferredTransport(i, z);
                boolean zHasMessages = DataNetworkController.this.hasMessages(5);
                DataNetworkController dataNetworkController2 = DataNetworkController.this;
                if (!zHasMessages) {
                    dataNetworkController2.sendMessage(dataNetworkController2.obtainMessage(5, DataEvaluation.DataEvaluationReason.PREFERRED_TRANSPORT_CHANGED));
                } else {
                    dataNetworkController2.log("onPreferredTransportChanged: Skipped evaluating unsatisfied network requests because another evaluation was already scheduled.");
                }
            }
        });
        this.mNetworkPolicyManager.registerSubscriptionCallback(new NetworkPolicyManager.SubscriptionCallback() { // from class: com.android.internal.telephony.data.DataNetworkController.7
            public void onSubscriptionPlansChanged(int i, SubscriptionPlan[] subscriptionPlanArr) {
                DataNetworkController dataNetworkController = DataNetworkController.this;
                if (dataNetworkController.mSubId != i) {
                    return;
                }
                dataNetworkController.obtainMessage(22, subscriptionPlanArr).sendToTarget();
            }

            public void onSubscriptionOverride(int i, int i2, int i3, int[] iArr) {
                DataNetworkController dataNetworkController = DataNetworkController.this;
                if (dataNetworkController.mSubId != i) {
                    return;
                }
                dataNetworkController.obtainMessage(23, i2, i3, iArr).sendToTarget();
            }
        });
        this.mPhone.getServiceStateTracker().registerForServiceStateChanged(this, 17, null);
        this.mDataConfigManager.registerCallback(new DataConfigManager.DataConfigManagerCallback(new DataNetworkController$$ExternalSyntheticLambda17(this)) { // from class: com.android.internal.telephony.data.DataNetworkController.8
            @Override // com.android.internal.telephony.data.DataConfigManager.DataConfigManagerCallback
            public void onCarrierConfigChanged() {
                DataNetworkController.this.onCarrierConfigUpdated();
            }

            @Override // com.android.internal.telephony.data.DataConfigManager.DataConfigManagerCallback
            public void onDeviceConfigChanged() {
                DataNetworkController.this.onDeviceConfigUpdated();
            }

            @Override // com.android.internal.telephony.data.DataConfigManager.DataConfigManagerCallback
            public void onDynamicConfigChanged(DataConfig.DataConfigDiff dataConfigDiff) {
                DataNetworkController.this.onDynamicConfigChanged(dataConfigDiff);
            }
        });
        this.mPhone.getServiceStateTracker().registerForPsRestrictedEnabled(this, 6, null);
        this.mPhone.getServiceStateTracker().registerForPsRestrictedDisabled(this, 7, null);
        this.mPhone.getServiceStateTracker().registerForAreaCodeChanged(this, 25, null);
        this.mPhone.registerForEmergencyCallToggle(this, 20, null);
        this.mDataServiceManagers.get(1).registerForServiceBindingChanged(this, 8);
        if (!this.mAccessNetworksManager.isInLegacyMode()) {
            this.mPhone.getServiceStateTracker().registerForServiceStateChanged(this, 17, null);
            this.mDataServiceManagers.get(2).registerForServiceBindingChanged(this, 8);
        }
        ((TelephonyRegistryManager) this.mPhone.getContext().getSystemService(TelephonyRegistryManager.class)).addOnSubscriptionsChangedListener(new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.data.DataNetworkController.9
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                DataNetworkController.this.sendEmptyMessage(15);
            }
        }, new DataNetworkController$$ExternalSyntheticLambda17(this));
        if (hasCalling()) {
            this.mPhone.getCallTracker().registerForVoiceCallEnded(this, 18, null);
            if (this.mPhone.getImsPhone() != null) {
                this.mPhone.getImsPhone().getCallTracker().registerForVoiceCallEnded(this, 18, null);
            }
        }
        this.mPhone.mCi.registerForSlicingConfigChanged(this, 24, null);
        this.mPhone.mCi.registerForSrvccStateChanged(this, 4, null);
        this.mPhone.getLinkBandwidthEstimator().registerCallback(new LinkBandwidthEstimator.LinkBandwidthEstimatorCallback(new DataNetworkController$$ExternalSyntheticLambda17(this)) { // from class: com.android.internal.telephony.data.DataNetworkController.10
            @Override // com.android.internal.telephony.data.LinkBandwidthEstimator.LinkBandwidthEstimatorCallback
            public void onDataActivityChanged(int i) {
                DataNetworkController.this.updateDataActivity();
            }
        });
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 2:
                onAddNetworkRequest((TelephonyNetworkRequest) message.obj);
                break;
            case 3:
                onRemoveNetworkRequest((TelephonyNetworkRequest) message.obj);
                break;
            case 4:
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception == null) {
                    onSrvccStateChanged((int[]) asyncResult.result);
                }
                break;
            case 5:
                onReevaluateUnsatisfiedNetworkRequests((DataEvaluation.DataEvaluationReason) message.obj);
                break;
            case 6:
                this.mPsRestricted = true;
                break;
            case 7:
                this.mPsRestricted = false;
                sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.DATA_RESTRICTED_CHANGED));
                break;
            case 8:
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                onDataServiceBindingChanged(((Integer) asyncResult2.userObj).intValue(), ((Boolean) asyncResult2.result).booleanValue());
                break;
            case 9:
                onSimStateChanged(message.arg1);
                break;
            case 10:
            case 11:
            default:
                loge("Unexpected event " + message.what);
                break;
            case 12:
                onTearDownAllDataNetworks(message.arg1);
                break;
            case 13:
                DataNetworkControllerCallback dataNetworkControllerCallback = (DataNetworkControllerCallback) message.obj;
                this.mDataNetworkControllerCallbacks.add(dataNetworkControllerCallback);
                boolean z = this.mAnyDataNetworkExisting;
                if (!z || !this.mAnyCellularDataNetworkExisting) {
                    dataNetworkControllerCallback.onAnyDataNetworkExistingChanged(z, this.mAnyCellularDataNetworkExisting);
                }
                break;
            case 14:
                this.mDataNetworkControllerCallbacks.remove((DataNetworkControllerCallback) message.obj);
                break;
            case 15:
                onSubscriptionChanged();
                break;
            case 16:
                onReevaluateExistingDataNetworks((DataEvaluation.DataEvaluationReason) message.obj);
                break;
            case 17:
                onServiceStateChanged();
                break;
            case 18:
                DataEvaluation.DataEvaluationReason dataEvaluationReason = DataEvaluation.DataEvaluationReason.VOICE_CALL_ENDED;
                sendMessage(obtainMessage(16, dataEvaluationReason));
                sendMessageDelayed(obtainMessage(5, dataEvaluationReason), REEVALUATE_UNSATISFIED_NETWORK_REQUESTS_AFTER_CALL_END_DELAY_MILLIS);
                break;
            case 19:
                onRegisterAllEvents();
                break;
            case 20:
                if (this.mPhone.isInEcm()) {
                    sendMessage(obtainMessage(16, DataEvaluation.DataEvaluationReason.EMERGENCY_CALL_CHANGED));
                } else {
                    sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.EMERGENCY_CALL_CHANGED));
                }
                break;
            case 21:
                onEvaluatePreferredTransport(message.arg1, message.arg2 != 0);
                break;
            case 22:
                SubscriptionPlan[] subscriptionPlanArr = (SubscriptionPlan[]) message.obj;
                log("Subscription plans changed: " + Arrays.toString(subscriptionPlanArr));
                this.mSubscriptionPlans.clear();
                this.mSubscriptionPlans.addAll(Arrays.asList(subscriptionPlanArr));
                this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda11
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DataNetworkController.$r8$lambda$EtqVm7l6udOnI7aA_fnTUU1Or8c((DataNetworkController.DataNetworkControllerCallback) obj);
                    }
                });
                break;
            case 23:
                int i = message.arg1;
                boolean z2 = message.arg2 != 0;
                int[] iArr = (int[]) message.obj;
                if (i == 1) {
                    log("Unmetered subscription override: override=" + z2 + ", networkTypes=" + ((String) Arrays.stream(iArr).mapToObj(new IntFunction() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda12
                        @Override // java.util.function.IntFunction
                        public final Object apply(int i2) {
                            return TelephonyManager.getNetworkTypeName(i2);
                        }
                    }).collect(Collectors.joining(","))));
                    for (int i2 : iArr) {
                        Set set = this.mUnmeteredOverrideNetworkTypes;
                        if (z2) {
                            set.add(Integer.valueOf(i2));
                        } else {
                            set.remove(Integer.valueOf(i2));
                        }
                    }
                    this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda13
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            DataNetworkController.$r8$lambda$dPwxTtcY3MnxAEANV2lXQHz9rhQ((DataNetworkController.DataNetworkControllerCallback) obj);
                        }
                    });
                } else if (i != 2) {
                    loge("Unknown override mask: " + i);
                } else {
                    log("Congested subscription override: override=" + z2 + ", networkTypes=" + ((String) Arrays.stream(iArr).mapToObj(new IntFunction() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda12
                        @Override // java.util.function.IntFunction
                        public final Object apply(int i3) {
                            return TelephonyManager.getNetworkTypeName(i3);
                        }
                    }).collect(Collectors.joining(","))));
                    for (int i3 : iArr) {
                        Set set2 = this.mCongestedOverrideNetworkTypes;
                        if (z2) {
                            set2.add(Integer.valueOf(i3));
                        } else {
                            set2.remove(Integer.valueOf(i3));
                        }
                    }
                    this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda14
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            DataNetworkController.m788$r8$lambda$93noZkweF9QHq6ap42TrRG2jOw((DataNetworkController.DataNetworkControllerCallback) obj);
                        }
                    });
                }
                break;
            case 24:
                sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.SLICE_CONFIG_CHANGED));
                break;
            case 25:
                sendMessageDelayed(obtainMessage(5, DataEvaluation.DataEvaluationReason.TAC_CHANGED), REEVALUATE_UNSATISFIED_NETWORK_REQUESTS_TAC_CHANGED_DELAY_MILLIS);
                break;
        }
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).handleMessage(this.mPhone, message);
    }

    public static /* synthetic */ void $r8$lambda$EtqVm7l6udOnI7aA_fnTUU1Or8c(DataNetworkControllerCallback dataNetworkControllerCallback) {
        Objects.requireNonNull(dataNetworkControllerCallback);
        dataNetworkControllerCallback.invokeFromExecutor(new DataNetworkController$$ExternalSyntheticLambda39(dataNetworkControllerCallback));
    }

    public static /* synthetic */ void $r8$lambda$dPwxTtcY3MnxAEANV2lXQHz9rhQ(DataNetworkControllerCallback dataNetworkControllerCallback) {
        Objects.requireNonNull(dataNetworkControllerCallback);
        dataNetworkControllerCallback.invokeFromExecutor(new DataNetworkController$$ExternalSyntheticLambda39(dataNetworkControllerCallback));
    }

    /* JADX INFO: renamed from: $r8$lambda$93noZkweF9QHq6ap42TrRG2-jOw, reason: not valid java name */
    public static /* synthetic */ void m788$r8$lambda$93noZkweF9QHq6ap42TrRG2jOw(DataNetworkControllerCallback dataNetworkControllerCallback) {
        Objects.requireNonNull(dataNetworkControllerCallback);
        dataNetworkControllerCallback.invokeFromExecutor(new DataNetworkController$$ExternalSyntheticLambda39(dataNetworkControllerCallback));
    }

    public void addNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        sendMessage(obtainMessage(2, telephonyNetworkRequest));
    }

    private void onAddNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        telephonyNetworkRequest.updateDataConfig(this.mDataConfigManager);
        if (this.mLastImsOperationIsRelease) {
            this.mLastImsOperationIsRelease = false;
            if (Arrays.equals(this.mLastReleasedImsRequestCapabilities, telephonyNetworkRequest.getCapabilities()) && this.mImsThrottleCounter.addOccurrence()) {
                reportAnomaly(telephonyNetworkRequest.getNativeNetworkRequest().getRequestorPackageName() + " requested with same capabilities " + this.mImsThrottleCounter.getFrequencyString(), "ead6f8db-d2f2-4ed3-8da5-1d8560fe7daf");
            }
        }
        if (!this.mAllNetworkRequestList.add(telephonyNetworkRequest)) {
            loge("onAddNetworkRequest: Duplicate network request. " + telephonyNetworkRequest);
        } else if (isPdpRejectRetryOngoing(telephonyNetworkRequest)) {
            loge("onAddNetworkRequest: Pdp reject retry in progress. " + telephonyNetworkRequest);
        } else {
            log("onAddNetworkRequest: added " + telephonyNetworkRequest);
            onSatisfyNetworkRequest(telephonyNetworkRequest);
        }
    }

    protected void onSatisfyNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        if (telephonyNetworkRequest.getState() == 1) {
            logv("Already satisfied. " + telephonyNetworkRequest);
        } else {
            if (findCompatibleDataNetworkAndAttach(telephonyNetworkRequest)) {
                return;
            }
            DataEvaluation dataEvaluationEvaluateNetworkRequest = evaluateNetworkRequest(telephonyNetworkRequest, DataEvaluation.DataEvaluationReason.NEW_REQUEST);
            if (!dataEvaluationEvaluateNetworkRequest.containsDisallowedReasons()) {
                DataProfile candidateDataProfile = dataEvaluationEvaluateNetworkRequest.getCandidateDataProfile();
                if (candidateDataProfile != null) {
                    MiuiDataControllerStub.get(this.mPhone.getPhoneId()).showDataUsageDialogCustomized(this.mPhone, candidateDataProfile);
                    setupDataNetwork(candidateDataProfile, null, dataEvaluationEvaluateNetworkRequest.getDataAllowedReason());
                    return;
                }
                return;
            }
            if (dataEvaluationEvaluateNetworkRequest.contains(DataEvaluation.DataDisallowedReason.ONLY_ALLOWED_SINGLE_NETWORK)) {
                sendMessage(obtainMessage(16, DataEvaluation.DataEvaluationReason.SINGLE_DATA_NETWORK_ARBITRATION));
            }
        }
    }

    private boolean findCompatibleDataNetworkAndAttach(TelephonyNetworkRequest telephonyNetworkRequest) {
        return findCompatibleDataNetworkAndAttach(new NetworkRequestList(telephonyNetworkRequest));
    }

    private boolean findCompatibleDataNetworkAndAttach(NetworkRequestList networkRequestList) {
        if (networkRequestList.isEmpty()) {
            return false;
        }
        for (final DataNetwork dataNetwork : this.mDataNetworkList) {
            if (((TelephonyNetworkRequest) networkRequestList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda18
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataNetworkController.$r8$lambda$Bfp4u0c0Fpdc3LnR0w_nYS6Qops(dataNetwork, (TelephonyNetworkRequest) obj);
                }
            }).findAny().orElse(null)) == null) {
                logv("Found a compatible data network " + dataNetwork + ". Attaching " + networkRequestList);
                return dataNetwork.attachNetworkRequests(networkRequestList);
            }
        }
        return false;
    }

    public static /* synthetic */ boolean $r8$lambda$Bfp4u0c0Fpdc3LnR0w_nYS6Qops(DataNetwork dataNetwork, TelephonyNetworkRequest telephonyNetworkRequest) {
        return !telephonyNetworkRequest.canBeSatisfiedBy(dataNetwork.getNetworkCapabilities());
    }

    private boolean isPsAttachAllowedForLegacyNetwork(ServiceState serviceState) {
        return serviceState.getVoiceRegState() == 0 && this.mPhone.getPhoneId() != PhoneSwitcher.getInstance().getPreferredDataPhoneId() && isLegacyCs(serviceState.getVoiceNetworkType());
    }

    private boolean serviceStateAllowsPSAttach(ServiceState serviceState, int i) {
        int dataRegistrationState = getDataRegistrationState(serviceState, i);
        if (dataRegistrationState == 1 || dataRegistrationState == 5) {
            return true;
        }
        return isPsAttachAllowedForLegacyNetwork(serviceState);
    }

    private boolean isLegacyCs(int i) {
        int iNetworkTypeToAccessNetworkType = DataUtils.networkTypeToAccessNetworkType(i);
        return iNetworkTypeToAccessNetworkType == 1 || iNetworkTypeToAccessNetworkType == 2 || iNetworkTypeToAccessNetworkType == 4;
    }

    protected boolean isOnlySingleDataNetworkAllowed(int i) {
        if (i == 2) {
            return false;
        }
        return this.mDataConfigManager.getNetworkTypesOnlySupportSingleDataNetwork().contains(Integer.valueOf(getDataNetworkType(i)));
    }

    private boolean hasCapabilityExemptsFromSinglePdnRule(int[] iArr) {
        Set<Integer> capabilitiesExemptFromSingleDataNetwork = this.mDataConfigManager.getCapabilitiesExemptFromSingleDataNetwork();
        IntStream intStreamStream = Arrays.stream(iArr);
        Objects.requireNonNull(capabilitiesExemptFromSingleDataNetwork);
        return intStreamStream.anyMatch(new DataNetworkController$$ExternalSyntheticLambda20(capabilitiesExemptFromSingleDataNetwork));
    }

    public boolean isInternetDataAllowed(boolean z) {
        return !getInternetEvaluation(z).containsDisallowedReasons();
    }

    public DataEvaluation getInternetEvaluation(boolean z) {
        final TelephonyNetworkRequest telephonyNetworkRequest = new TelephonyNetworkRequest(new NetworkRequest.Builder().addCapability(12).build(), this.mPhone, this.mFeatureFlags);
        if (!z && this.mDataNetworkList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda30
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return telephonyNetworkRequest.canBeSatisfiedBy(((DataNetwork) obj).getNetworkCapabilities());
            }
        })) {
            return new DataEvaluation(DataEvaluation.DataEvaluationReason.EXTERNAL_QUERY);
        }
        DataEvaluation dataEvaluationEvaluateNetworkRequest = evaluateNetworkRequest(telephonyNetworkRequest, DataEvaluation.DataEvaluationReason.EXTERNAL_QUERY);
        if (dataEvaluationEvaluateNetworkRequest.containsOnly(DataEvaluation.DataDisallowedReason.ONLY_ALLOWED_SINGLE_NETWORK) && telephonyNetworkRequest.getPriority() > ((Integer) this.mDataNetworkList.stream().map(new Function() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda31
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(((DataNetwork) obj).getPriority());
            }
        }).max(Comparator.comparing(new Function() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda32
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer num = (Integer) obj;
                num.intValue();
                return num;
            }
        })).orElse(0)).intValue()) {
            dataEvaluationEvaluateNetworkRequest.addDataAllowedReason(DataEvaluation.DataAllowedReason.NORMAL);
        }
        return dataEvaluationEvaluateNetworkRequest;
    }

    public boolean isInternetUnmetered() {
        return this.mDataNetworkList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda36
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetworkController.$r8$lambda$uNTk5oU5aPAHGCuR6LN93cjkbww((DataNetwork) obj);
            }
        }).filter(new DataNetworkController$$ExternalSyntheticLambda37()).allMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda38
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetworkController.$r8$lambda$02RaP4VesP9r6O0jTM4hTJd4H1A((DataNetwork) obj);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$uNTk5oU5aPAHGCuR6LN93cjkbww(DataNetwork dataNetwork) {
        return (dataNetwork.isConnecting() || dataNetwork.isDisconnected()) ? false : true;
    }

    public static /* synthetic */ boolean $r8$lambda$02RaP4VesP9r6O0jTM4hTJd4H1A(DataNetwork dataNetwork) {
        return dataNetwork.getNetworkCapabilities().hasCapability(11) || dataNetwork.getNetworkCapabilities().hasCapability(25);
    }

    public boolean areAllDataDisconnected(boolean z) {
        if (z) {
            List cellularDataNetworks = getCellularDataNetworks();
            if (!cellularDataNetworks.isEmpty()) {
                log("areAllCellular DataDisconnected false due to: " + ((String) cellularDataNetworks.stream().map(new Function() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda15
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((DataNetwork) obj).name();
                    }
                }).collect(Collectors.joining(", "))));
            }
            return cellularDataNetworks.isEmpty();
        }
        if (!this.mDataNetworkList.isEmpty()) {
            log("areAllDataDisconnected false due to: " + ((String) this.mDataNetworkList.stream().map(new Function() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda15
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((DataNetwork) obj).name();
                }
            }).collect(Collectors.joining(", "))));
        }
        return this.mDataNetworkList.isEmpty();
    }

    private void updateCellularDataNetworkExistence(boolean z, boolean z2) {
        if (z == this.mAnyDataNetworkExisting && z2 == this.mAnyCellularDataNetworkExisting) {
            return;
        }
        this.mAnyDataNetworkExisting = z;
        this.mAnyCellularDataNetworkExisting = z2;
        this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda60
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$updateCellularDataNetworkExistence$9((DataNetworkController.DataNetworkControllerCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCellularDataNetworkExistence$9(final DataNetworkControllerCallback dataNetworkControllerCallback) {
        dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateCellularDataNetworkExistence$8(dataNetworkControllerCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCellularDataNetworkExistence$8(DataNetworkControllerCallback dataNetworkControllerCallback) {
        dataNetworkControllerCallback.onAnyDataNetworkExistingChanged(this.mAnyDataNetworkExisting, this.mAnyCellularDataNetworkExisting);
    }

    private List getCellularDataNetworks() {
        return this.mDataNetworkList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda59
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetworkController.$r8$lambda$wNQcYz19_jpWMh1dwPYTNbbee3M((DataNetwork) obj);
            }
        }).toList();
    }

    public static /* synthetic */ boolean $r8$lambda$wNQcYz19_jpWMh1dwPYTNbbee3M(DataNetwork dataNetwork) {
        return (dataNetwork.getTransport() == 1 && !dataNetwork.isHandoverInProgress()) || (dataNetwork.getTransport() == 2 && dataNetwork.isHandoverInProgress());
    }

    public List<DataEvaluation.DataDisallowedReason> getInternetDataDisallowedReasons() {
        return evaluateNetworkRequest(new TelephonyNetworkRequest(new NetworkRequest.Builder().addCapability(12).build(), this.mPhone, this.mFeatureFlags), DataEvaluation.DataEvaluationReason.EXTERNAL_QUERY).getDataDisallowedReasons();
    }

    protected DataEvaluation evaluateNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest, DataEvaluation.DataEvaluationReason dataEvaluationReason) {
        NetworkRegistrationInfo networkRegistrationInfo;
        DataSpecificRegistrationInfo dataSpecificInfo;
        NetworkRegistrationInfo networkRegistrationInfo2;
        DataEvaluation dataEvaluation = new DataEvaluation(dataEvaluationReason);
        int preferredTransportByNetworkCapability = this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(telephonyNetworkRequest.getHighestPriorityApnTypeNetworkCapability());
        if (!canConnectivityTransportSatisfyNetworkRequest(telephonyNetworkRequest, preferredTransportByNetworkCapability)) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_NETWORK_TRANSPORT_NOT_ALLOWED);
        }
        IntStream intStreamStream = Arrays.stream(telephonyNetworkRequest.getCapabilities());
        Set<Integer> unsupportedNetworkCapabilities = this.mDataConfigManager.getUnsupportedNetworkCapabilities();
        Objects.requireNonNull(unsupportedNetworkCapabilities);
        if (intStreamStream.anyMatch(new DataNetworkController$$ExternalSyntheticLambda20(unsupportedNetworkCapabilities))) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.UNSUPPORTED_NETWORK_CAPABILITIES);
        }
        if (telephonyNetworkRequest.hasCapability(10)) {
            DataProfile dataProfileForNetworkRequest = this.mDataProfileManager.getDataProfileForNetworkRequest(telephonyNetworkRequest, getDataNetworkType(preferredTransportByNetworkCapability), this.mServiceState.isUsingNonTerrestrialNetwork(), false, true);
            if (this.mDataConfigManager.shouldHonorRetryTimerForEmergencyNetworkRequest() && dataProfileForNetworkRequest != null && this.mDataRetryManager.isDataProfileThrottled(dataProfileForNetworkRequest, preferredTransportByNetworkCapability)) {
                dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_THROTTLED);
                log("Emergency network request is throttled by the previous setup data call response.");
            }
            if (!dataEvaluation.containsDisallowedReasons()) {
                dataEvaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.EMERGENCY_REQUEST);
                if (dataProfileForNetworkRequest != null) {
                    dataEvaluation.setCandidateDataProfile(dataProfileForNetworkRequest);
                }
            }
            telephonyNetworkRequest.setEvaluation(dataEvaluation);
            log(dataEvaluation.toString());
            return dataEvaluation;
        }
        if (!serviceStateAllowsPSAttach(this.mServiceState, preferredTransportByNetworkCapability)) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE);
        }
        if (this.mSimState != 10) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.SIM_NOT_READY);
        }
        dataEvaluationforValueAdds(dataEvaluation, telephonyNetworkRequest);
        if (!this.mDataConfigManager.isConfigCarrierSpecific()) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_CONFIG_NOT_READY);
        }
        if (!isPsAttachAllowedForLegacyNetwork(this.mServiceState) && (networkRegistrationInfo2 = this.mServiceState.getNetworkRegistrationInfo(2, preferredTransportByNetworkCapability)) != null && !networkRegistrationInfo2.getAvailableServices().contains(2) && this.mIHyperDataManager.isDefaultDataSubId()) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.SERVICE_OPTION_NOT_SUPPORTED);
        }
        if (hasCalling() && this.mPhone.getCallTracker().getState() != PhoneConstants.State.IDLE && !this.mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed()) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.CONCURRENT_VOICE_DATA_NOT_ALLOWED);
        }
        if (preferredTransportByNetworkCapability == 1 && telephonyNetworkRequest.hasCapability(33) && (networkRegistrationInfo = this.mServiceState.getNetworkRegistrationInfo(2, 1)) != null && (dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo()) != null && dataSpecificInfo.getVopsSupportInfo() != null && !dataSpecificInfo.getVopsSupportInfo().isVopsSupported() && !this.mDataConfigManager.allowBringUpNetworkInNonVops(networkRegistrationInfo.getNetworkRegistrationState())) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.VOPS_NOT_SUPPORTED);
        }
        if (!SubscriptionManager.isValidSubscriptionId(SubscriptionManager.getDefaultDataSubscriptionId())) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DEFAULT_DATA_UNSELECTED);
        }
        boolean zIsDataRoamingEnabled = this.mDataSettingsManager.isDataRoamingEnabled();
        if (this.mServiceState.getDataRoaming() && !zIsDataRoamingEnabled && (!this.mServiceState.isUsingNonTerrestrialNetwork() || !shouldAllowSatelliteDataWhenRoamingDisabled())) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
        }
        if (this.mPsRestricted && preferredTransportByNetworkCapability == 1) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_RESTRICTED_BY_NETWORK);
        }
        if (this.mPhone.getServiceStateTracker().isPendingRadioPowerOffAfterDataOff()) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.PENDING_TEAR_DOWN_ALL);
        }
        if (preferredTransportByNetworkCapability == 1 && (!this.mPhone.getServiceStateTracker().getDesiredPowerState() || this.mPhone.mCi.getRadioState() != 1)) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.RADIO_POWER_OFF);
        }
        if (!this.mPhone.getServiceStateTracker().getPowerStateFromCarrier()) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.RADIO_DISABLED_BY_CARRIER);
        }
        if (!this.mDataServiceBound.get(preferredTransportByNetworkCapability)) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_SERVICE_NOT_READY);
        }
        if (this.mPhone.isInCdmaEcm()) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.CDMA_EMERGENCY_CALLBACK_MODE);
        }
        if (isOnlySingleDataNetworkAllowed(preferredTransportByNetworkCapability) && !hasCapabilityExemptsFromSinglePdnRule(telephonyNetworkRequest.getCapabilities()) && this.mDataNetworkList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda35
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$evaluateNetworkRequest$11((DataNetwork) obj);
            }
        })) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.ONLY_ALLOWED_SINGLE_NETWORK);
        }
        if (this.mDataSettingsManager.isDataInitialized()) {
            if (!this.mDataSettingsManager.isDataEnabled(DataUtils.networkCapabilityToApnType(telephonyNetworkRequest.getHighestPriorityApnTypeNetworkCapability()))) {
                dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_DISABLED);
            }
        } else {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_SETTINGS_NOT_READY);
        }
        if (!this.mIHyperDataManager.checkVendorDataAllowed()) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.VENDOR_SPECIFIC);
        }
        if (!dataEvaluation.containsDisallowedReasons()) {
            dataEvaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.NORMAL);
            if (!this.mDataSettingsManager.isDataEnabled() && telephonyNetworkRequest.hasCapability(0) && this.mDataSettingsManager.isMobileDataPolicyEnabled(2)) {
                dataEvaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.MMS_REQUEST);
            }
        } else if (!dataEvaluation.containsHardDisallowedReasons()) {
            if ((this.mTelecomManager.isInEmergencyCall() || this.mPhone.isInEcm()) && telephonyNetworkRequest.hasCapability(1)) {
                dataEvaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.EMERGENCY_SUPL);
            } else if (!telephonyNetworkRequest.hasCapability(13) && isValidRestrictedRequest(telephonyNetworkRequest)) {
                dataEvaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.RESTRICTED_REQUEST);
            } else if (preferredTransportByNetworkCapability == 2) {
                dataEvaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.UNMETERED_USAGE);
            } else if (preferredTransportByNetworkCapability == 1 && !TelephonyMultiInternetStub.get().isMultiInternetCapabilities(telephonyNetworkRequest.getCapabilities(), this.mPhone.getPhoneId()) && !this.mDataConfigManager.isAnyMeteredCapability(telephonyNetworkRequest.getCapabilities(), this.mServiceState.getDataRoaming())) {
                dataEvaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.UNMETERED_USAGE);
            }
        }
        int dataNetworkType = getDataNetworkType(preferredTransportByNetworkCapability);
        if (dataNetworkType == 0 && preferredTransportByNetworkCapability == 1) {
            dataNetworkType = this.mServiceState.getVoiceNetworkType();
        }
        DataProfile dataProfileForNetworkRequest2 = this.mDataProfileManager.getDataProfileForNetworkRequest(telephonyNetworkRequest, dataNetworkType, this.mServiceState.isUsingNonTerrestrialNetwork(), isEsimBootStrapProvisioningActivated(), dataEvaluationReason.isConditionBased());
        if (dataProfileForNetworkRequest2 == null) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.NO_SUITABLE_DATA_PROFILE);
        } else if (dataEvaluationReason == DataEvaluation.DataEvaluationReason.NEW_REQUEST && this.mDataRetryManager.isSimilarNetworkRequestRetryScheduled(telephonyNetworkRequest, preferredTransportByNetworkCapability)) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.RETRY_SCHEDULED);
        } else if (this.mDataRetryManager.isDataProfileThrottled(dataProfileForNetworkRequest2, preferredTransportByNetworkCapability)) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_THROTTLED);
        }
        mtkEvaluateNetworkRequest(dataEvaluation, telephonyNetworkRequest, dataEvaluationReason, preferredTransportByNetworkCapability);
        DataEvaluation dataEvaluationEvaluateNetworkRequest = MiuiDataControllerStub.get(this.mPhone.getPhoneId()).evaluateNetworkRequest(this.mPhone, telephonyNetworkRequest, dataEvaluation);
        if (!dataEvaluationEvaluateNetworkRequest.containsDisallowedReasons()) {
            if (preferredTransportByNetworkCapability == 1 && isEsimBootStrapProvisioningActivated() && isEsimBootStrapMaxDataLimitReached()) {
                log("BootStrap Sim Data Usage limit reached");
                dataEvaluationEvaluateNetworkRequest.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_LIMIT_REACHED);
            } else {
                dataEvaluationEvaluateNetworkRequest.setCandidateDataProfile(dataProfileForNetworkRequest2);
            }
        }
        if (dataEvaluationEvaluateNetworkRequest.containsHardDisallowedReasons()) {
            dataEvaluationEvaluateNetworkRequest.removeSoftDisallowedReasons();
        }
        telephonyNetworkRequest.setEvaluation(dataEvaluationEvaluateNetworkRequest);
        if (dataEvaluationReason != DataEvaluation.DataEvaluationReason.EXTERNAL_QUERY) {
            log(dataEvaluationEvaluateNetworkRequest + ", network type=" + TelephonyManager.getNetworkTypeName(getDataNetworkType(preferredTransportByNetworkCapability)) + ", reg state=" + NetworkRegistrationInfo.registrationStateToString(getDataRegistrationState(this.mServiceState, preferredTransportByNetworkCapability)) + ", " + telephonyNetworkRequest);
        }
        if (this.mSimState == 10) {
            MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onDataEvaluationFailed(this.mPhone, telephonyNetworkRequest, dataEvaluationEvaluateNetworkRequest, dataEvaluationReason);
        }
        return dataEvaluationEvaluateNetworkRequest;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$evaluateNetworkRequest$11(DataNetwork dataNetwork) {
        return !hasCapabilityExemptsFromSinglePdnRule(dataNetwork.getNetworkCapabilities().getCapabilities());
    }

    private boolean shouldAllowSatelliteDataWhenRoamingDisabled() {
        return this.mDataConfigManager.isDataRoamingAllowedOnSatellite();
    }

    private boolean isEsimBootStrapMaxDataLimitReached() {
        long esimBootStrapMaxDataLimitBytes = this.mDataConfigManager.getEsimBootStrapMaxDataLimitBytes();
        if (esimBootStrapMaxDataLimitBytes < 0) {
            return false;
        }
        if (this.mBootStrapSimTotalDataUsageBytes < esimBootStrapMaxDataLimitBytes && (this.mBootstrapSimLastDataUsageQueryTime == 0 || SystemClock.elapsedRealtime() - this.mBootstrapSimLastDataUsageQueryTime > GUARD_TIMER_INTERVAL_TO_QUERY_DATA_USAGE_API_STATS_MILLIS)) {
            this.mBootStrapSimTotalDataUsageBytes = getDataUsage();
            log("current bootstrap sim data usage: " + this.mBootStrapSimTotalDataUsageBytes);
            this.mBootstrapSimLastDataUsageQueryTime = SystemClock.elapsedRealtime();
        }
        return this.mBootStrapSimTotalDataUsageBytes >= esimBootStrapMaxDataLimitBytes;
    }

    private long getDataUsage() {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.mPhone.getContext().getSystemService(NetworkStatsManager.class);
        if (networkStatsManager == null) {
            return 0L;
        }
        NetworkTemplate.Builder builder = new NetworkTemplate.Builder(1);
        String subscriberId = this.mPhone.getSubscriberId();
        if (TextUtils.isEmpty(subscriberId)) {
            return 0L;
        }
        builder.setSubscriberIds(Set.of(subscriberId));
        builder.setMeteredness(1);
        NetworkStats.Bucket bucketQuerySummaryForDevice = networkStatsManager.querySummaryForDevice(builder.build(), 0L, System.currentTimeMillis());
        return bucketQuerySummaryForDevice.getRxBytes() + bucketQuerySummaryForDevice.getTxBytes();
    }

    private List getGroupedUnsatisfiedNetworkRequests() {
        NetworkRequestList networkRequestList = new NetworkRequestList();
        for (TelephonyNetworkRequest telephonyNetworkRequest : this.mAllNetworkRequestList) {
            if (telephonyNetworkRequest.getState() == 0) {
                networkRequestList.add(telephonyNetworkRequest);
            }
        }
        return DataUtils.getGroupedNetworkRequestList(networkRequestList, this.mFeatureFlags);
    }

    private void onReevaluateUnsatisfiedNetworkRequests(DataEvaluation.DataEvaluationReason dataEvaluationReason) {
        DataProfile candidateDataProfile;
        List<NetworkRequestList> groupedUnsatisfiedNetworkRequests = getGroupedUnsatisfiedNetworkRequests();
        log("Re-evaluating " + groupedUnsatisfiedNetworkRequests.stream().mapToInt(new ToIntFunction() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda40
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((DataNetworkController.NetworkRequestList) obj).size();
            }
        }).sum() + " unsatisfied network requests in " + groupedUnsatisfiedNetworkRequests.size() + " groups, " + ((String) groupedUnsatisfiedNetworkRequests.stream().map(new Function() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda41
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((DataNetworkController.NetworkRequestList) obj).toStringSimplified();
            }
        }).collect(Collectors.joining(", "))) + " due to " + dataEvaluationReason);
        for (NetworkRequestList networkRequestList : groupedUnsatisfiedNetworkRequests) {
            if (!findCompatibleDataNetworkAndAttach(networkRequestList) && !isPdpRejectRetryOngoing(networkRequestList.get(0))) {
                DataEvaluation dataEvaluationEvaluateNetworkRequest = evaluateNetworkRequest(networkRequestList.get(0), dataEvaluationReason);
                if (!dataEvaluationEvaluateNetworkRequest.containsDisallowedReasons() && (candidateDataProfile = dataEvaluationEvaluateNetworkRequest.getCandidateDataProfile()) != null) {
                    setupDataNetwork(candidateDataProfile, null, dataEvaluationEvaluateNetworkRequest.getDataAllowedReason());
                }
            }
        }
    }

    private boolean isInfrastructureSupported(ApnSetting apnSetting) {
        if (this.mServiceState.isUsingNonTerrestrialNetwork()) {
            return apnSetting.isForInfrastructure(2);
        }
        return apnSetting.isForInfrastructure(1);
    }

    protected boolean isPdpRejectRetryOngoing(TelephonyNetworkRequest telephonyNetworkRequest) {
        log("isPdpRejectRetryOngoing: superclass method");
        return false;
    }

    protected DataEvaluation evaluateDataNetwork(final DataNetwork dataNetwork, DataEvaluation.DataEvaluationReason dataEvaluationReason) {
        NetworkRegistrationInfo networkRegistrationInfo;
        DataSpecificRegistrationInfo dataSpecificInfo;
        DataEvaluation dataEvaluation = new DataEvaluation(dataEvaluationReason);
        if (dataNetwork.getNetworkCapabilities().hasCapability(10)) {
            dataEvaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.EMERGENCY_REQUEST);
            log(dataEvaluation.toString());
            return dataEvaluation;
        }
        IntStream intStreamStream = Arrays.stream(dataNetwork.getNetworkCapabilities().getCapabilities());
        Set<Integer> unsupportedNetworkCapabilities = this.mDataConfigManager.getUnsupportedNetworkCapabilities();
        Objects.requireNonNull(unsupportedNetworkCapabilities);
        if (intStreamStream.anyMatch(new DataNetworkController$$ExternalSyntheticLambda20(unsupportedNetworkCapabilities))) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.UNSUPPORTED_NETWORK_CAPABILITIES);
        }
        boolean z = TelephonyBaseUtilsStub.isAutoDevice() && (dataNetwork.getNetworkCapabilities().hasCapability(55) || dataNetwork.getNetworkCapabilities().hasCapability(57));
        if (this.mSimState != 10) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.SIM_NOT_READY);
        }
        dataEvaluationforValueAdds(dataEvaluation, null);
        if (this.mPhone.isInCdmaEcm()) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.CDMA_EMERGENCY_CALLBACK_MODE);
        }
        NetworkRegistrationInfo networkRegistrationInfo2 = this.mServiceState.getNetworkRegistrationInfo(2, dataNetwork.getTransport());
        if (networkRegistrationInfo2 != null && networkRegistrationInfo2.isInService() && !networkRegistrationInfo2.getAvailableServices().contains(2) && this.mIHyperDataManager.isDefaultDataSubId()) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.SERVICE_OPTION_NOT_SUPPORTED);
        }
        if (isEsimBootStrapProvisioningActivated() && dataNetwork.getTransport() == 1) {
            if (isEsimBootStrapMaxDataLimitReached()) {
                log("BootStrap Sim Data Usage limit reached");
                dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_LIMIT_REACHED);
            } else if (!hasMessages(16)) {
                sendMessageDelayed(obtainMessage(16, DataEvaluation.DataEvaluationReason.CHECK_DATA_USAGE), this.mDataConfigManager.getReevaluateBootstrapSimDataUsageMillis());
            } else {
                log("skip scheduling evaluating existing data networks since alreadyscheduled");
            }
        }
        if (isOnlySingleDataNetworkAllowed(dataNetwork.getTransport()) && !hasCapabilityExemptsFromSinglePdnRule(dataNetwork.getNetworkCapabilities().getCapabilities()) && !z) {
            if (this.mAllNetworkRequestList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda21
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.lambda$evaluateDataNetwork$12(dataNetwork, (TelephonyNetworkRequest) obj);
                }
            }).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda22
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.lambda$evaluateDataNetwork$13((TelephonyNetworkRequest) obj);
                }
            }).anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda23
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataNetworkController.$r8$lambda$kqruXMWtURmivLH8LBht38jANmA(dataNetwork, (TelephonyNetworkRequest) obj);
                }
            })) {
                dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.ONLY_ALLOWED_SINGLE_NETWORK);
            } else {
                log("evaluateDataNetwork: " + dataNetwork + " has the highest priority. No need to tear down");
            }
        }
        boolean zHasNetworkCapabilityInNetworkRequests = dataNetwork.hasNetworkCapabilityInNetworkRequests(33);
        if (!dataNetwork.shouldDelayImsTearDownDueToInCall()) {
            if (zHasNetworkCapabilityInNetworkRequests && dataNetwork.getTransport() == 1 && (networkRegistrationInfo = this.mServiceState.getNetworkRegistrationInfo(2, 1)) != null && (dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo()) != null && dataSpecificInfo.getVopsSupportInfo() != null && !dataSpecificInfo.getVopsSupportInfo().isVopsSupported() && !this.mDataConfigManager.shouldKeepNetworkUpInNonVops(networkRegistrationInfo.getNetworkRegistrationState())) {
                dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.VOPS_NOT_SUPPORTED);
            }
            if (this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(dataNetwork.getApnTypeNetworkCapability()) != dataNetwork.getTransport() && this.mDataRetryManager.isDataNetworkHandoverRetryStopped(dataNetwork)) {
                dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.HANDOVER_RETRY_STOPPED);
            }
        } else if (zHasNetworkCapabilityInNetworkRequests) {
            log("Ignored VoPS check due to delay IMS tear down until call ends.");
        }
        if (dataNetwork.getTransport() == 1 && (!this.mPhone.getServiceStateTracker().getDesiredPowerState() || !this.mPhone.getServiceStateTracker().isRadioOn())) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.RADIO_POWER_OFF);
        }
        boolean z2 = !this.mDataSettingsManager.isDataEnabled();
        boolean zIsDataRoamingEnabled = this.mDataSettingsManager.isDataRoamingEnabled();
        if (this.mServiceState.getDataRoaming() && !zIsDataRoamingEnabled && (!this.mServiceState.isUsingNonTerrestrialNetwork() || !shouldAllowSatelliteDataWhenRoamingDisabled())) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
        }
        int dataNetworkType = getDataNetworkType(dataNetwork.getTransport());
        DataProfile dataProfile = dataNetwork.getDataProfile();
        if (dataProfile.getApnSetting() != null) {
            z2 = !this.mDataSettingsManager.isDataEnabled(DataUtils.networkCapabilityToApnType(dataNetwork.getApnTypeNetworkCapability()));
            ApnSetting apnSetting = dataProfile.getApnSetting();
            if (dataNetworkType != 0 && !apnSetting.canSupportLingeringNetworkType(dataNetworkType)) {
                log("networkType=" + TelephonyManager.getNetworkTypeName(dataNetworkType) + ", networkTypeBitmask=" + TelephonyManager.convertNetworkTypeBitmaskToString(apnSetting.getNetworkTypeBitmask()) + ", lingeringNetworkTypeBitmask=" + TelephonyManager.convertNetworkTypeBitmaskToString(apnSetting.getLingeringNetworkTypeBitmask()));
                dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_NETWORK_TYPE_NOT_ALLOWED);
            }
            if (dataNetwork.getTransport() == 1 && !isInfrastructureSupported(apnSetting)) {
                dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_NETWORK_TRANSPORT_NOT_ALLOWED);
            }
        }
        if (dataEvaluationReason == DataEvaluation.DataEvaluationReason.DATA_DYNAMIC_CONFIG_CHANGED) {
            int connectionCapability = dataNetwork.getDataProfile().getTrafficDescriptor() != null ? dataNetwork.getDataProfile().getTrafficDescriptor().getConnectionCapability() : 0;
            for (TelephonyNetworkRequest telephonyNetworkRequest : dataNetwork.getAttachedNetworkRequestList()) {
                int highestPrioritySupportedNetworkCapability = telephonyNetworkRequest.getHighestPrioritySupportedNetworkCapability();
                int iNetworkCapabilityToConnectionCapability = this.mDataConfigManager.networkCapabilityToConnectionCapability(highestPrioritySupportedNetworkCapability);
                if (connectionCapability != iNetworkCapabilityToConnectionCapability) {
                    log("evaluateDataNetwork: Mismatch for " + dataNetwork + ". Request " + telephonyNetworkRequest + " and highestPriorityCap " + highestPrioritySupportedNetworkCapability + " expects ConnCap " + iNetworkCapabilityToConnectionCapability + " but has " + connectionCapability);
                    dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_PROFILE_INVALID);
                    break;
                }
            }
        }
        if (z2) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_DISABLED);
        }
        DataEvaluation dataEvaluationEvaluateDataNetwork = MiuiDataControllerStub.get(this.mPhone.getPhoneId()).evaluateDataNetwork(this.mPhone, dataEvaluation);
        if (!this.mDataProfileManager.isDataProfileCompatible(dataProfile)) {
            dataEvaluationEvaluateDataNetwork.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_PROFILE_INVALID);
        }
        if (dataNetwork.isInternetSupported() && !this.mDataProfileManager.isDataProfilePreferred(dataProfile) && (this.mDataProfileManager.canPreferredDataProfileSatisfy(dataNetwork.getAttachedNetworkRequestList()) || this.mIHyperDataManager.isPreferredDataProfileEmpty())) {
            dataEvaluationEvaluateDataNetwork.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_PROFILE_NOT_PREFERRED);
        }
        if (!this.mIHyperDataManager.checkVendorDataAllowed()) {
            dataEvaluationEvaluateDataNetwork.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.VENDOR_SPECIFIC);
        }
        mtkEvaluateDataNetwork(dataEvaluationEvaluateDataNetwork, dataNetwork, dataEvaluationReason, dataNetwork.getTransport());
        if (!dataEvaluationEvaluateDataNetwork.containsDisallowedReasons()) {
            dataEvaluationEvaluateDataNetwork.addDataAllowedReason(DataEvaluation.DataAllowedReason.NORMAL);
        } else if (!dataEvaluationEvaluateDataNetwork.containsHardDisallowedReasons()) {
            if ((this.mTelecomManager.isInEmergencyCall() || this.mPhone.isInEcm()) && dataNetwork.isEmergencySupl()) {
                dataEvaluationEvaluateDataNetwork.addDataAllowedReason(DataEvaluation.DataAllowedReason.EMERGENCY_SUPL);
            } else if (!dataNetwork.getNetworkCapabilities().hasCapability(13) && dataNetwork.getAttachedNetworkRequestList().stream().allMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda24
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.isValidRestrictedRequest((TelephonyNetworkRequest) obj);
                }
            })) {
                dataEvaluationEvaluateDataNetwork.addDataAllowedReason(DataEvaluation.DataAllowedReason.RESTRICTED_REQUEST);
            } else if (dataNetwork.getTransport() == 2) {
                dataEvaluationEvaluateDataNetwork.addDataAllowedReason(DataEvaluation.DataAllowedReason.UNMETERED_USAGE);
            } else if (!this.mDataConfigManager.isAnyMeteredCapability(dataNetwork.getNetworkCapabilities().getCapabilities(), this.mServiceState.getDataRoaming()) && !z) {
                dataEvaluationEvaluateDataNetwork.addDataAllowedReason(DataEvaluation.DataAllowedReason.UNMETERED_USAGE);
            }
        } else {
            dataEvaluationEvaluateDataNetwork.removeSoftDisallowedReasons();
        }
        boolean z3 = zHasNetworkCapabilityInNetworkRequests && this.mIsSrvccHandoverInProcess;
        if (dataEvaluationEvaluateDataNetwork.containsOnly(DataEvaluation.DataDisallowedReason.DATA_NETWORK_TYPE_NOT_ALLOWED) && (dataNetwork.shouldDelayImsTearDownDueToInCall() || z3)) {
            dataEvaluationEvaluateDataNetwork.addDataAllowedReason(DataEvaluation.DataAllowedReason.IN_VOICE_CALL);
        }
        log("Evaluated " + dataNetwork + ", " + dataEvaluationEvaluateDataNetwork);
        return dataEvaluationEvaluateDataNetwork;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$evaluateDataNetwork$12(DataNetwork dataNetwork, TelephonyNetworkRequest telephonyNetworkRequest) {
        return dataNetwork.getTransport() == this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(telephonyNetworkRequest.getHighestPriorityApnTypeNetworkCapability());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$evaluateDataNetwork$13(TelephonyNetworkRequest telephonyNetworkRequest) {
        return !hasCapabilityExemptsFromSinglePdnRule(telephonyNetworkRequest.getCapabilities());
    }

    public static /* synthetic */ boolean $r8$lambda$kqruXMWtURmivLH8LBht38jANmA(DataNetwork dataNetwork, TelephonyNetworkRequest telephonyNetworkRequest) {
        return telephonyNetworkRequest.getPriority() > dataNetwork.getPriority();
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0035, code lost:
    
        if (r6.hasCapability(37) != false) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean canConnectivityTransportSatisfyNetworkRequest(com.android.internal.telephony.data.TelephonyNetworkRequest r6, int r7) {
        /*
            r5 = this;
            r0 = 2
            r1 = 1
            if (r7 != r0) goto L5
            return r1
        L5:
            android.telephony.ServiceState r7 = r5.mServiceState
            boolean r7 = r7.isUsingNonTerrestrialNetwork()
            r0 = 0
            if (r7 == 0) goto L38
            r7 = 13
            boolean r7 = r6.hasCapability(r7)
            if (r7 == 0) goto L38
            com.android.internal.telephony.satellite.SatelliteController r7 = com.android.internal.telephony.satellite.SatelliteController.getInstance()
            int r2 = r5.mSubId
            com.android.internal.telephony.Phone r3 = r5.mPhone
            android.telephony.ServiceState r3 = r3.getServiceState()
            java.lang.String r3 = r3.getOperatorNumeric()
            int r7 = r7.getSatelliteDataServicePolicyForPlmn(r2, r3)
            if (r7 == 0) goto L37
            if (r7 == r1) goto L2f
            goto L38
        L2f:
            r7 = 37
            boolean r7 = r6.hasCapability(r7)     // Catch: java.lang.Exception -> L38
            if (r7 == 0) goto L38
        L37:
            return r0
        L38:
            boolean r7 = r6.hasTransport(r0)
            r2 = 10
            if (r7 != 0) goto L47
            boolean r7 = r6.hasTransport(r2)
            if (r7 != 0) goto L47
            return r1
        L47:
            boolean r7 = r6.hasTransport(r0)
            if (r7 == 0) goto L6a
            int[] r7 = r6.getCapabilities()
            java.util.stream.IntStream r7 = java.util.Arrays.stream(r7)
            com.android.internal.telephony.data.DataConfigManager r3 = r5.mDataConfigManager
            java.util.Set r3 = r3.getForcedCellularTransportCapabilities()
            java.util.Objects.requireNonNull(r3)
            com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda20 r4 = new com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda20
            r4.<init>(r3)
            boolean r7 = r7.anyMatch(r4)
            if (r7 == 0) goto L6a
            return r1
        L6a:
            android.telephony.ServiceState r7 = r5.mServiceState
            boolean r7 = r7.isUsingNonTerrestrialNetwork()
            if (r7 == 0) goto L78
            boolean r7 = r6.hasTransport(r2)
            if (r7 != 0) goto L88
        L78:
            android.telephony.ServiceState r5 = r5.mServiceState
            boolean r5 = r5.isUsingNonTerrestrialNetwork()
            if (r5 != 0) goto L87
            boolean r5 = r6.hasTransport(r0)
            if (r5 == 0) goto L87
            goto L88
        L87:
            r1 = r0
        L88:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.data.DataNetworkController.canConnectivityTransportSatisfyNetworkRequest(com.android.internal.telephony.data.TelephonyNetworkRequest, int):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidRestrictedRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        if (telephonyNetworkRequest.hasCapability(2) || telephonyNetworkRequest.hasCapability(29) || telephonyNetworkRequest.hasCapability(0)) {
            return false;
        }
        return (this.mServiceState.isUsingNonTerrestrialNetwork() && telephonyNetworkRequest.hasCapability(12)) ? false : true;
    }

    private void onReevaluateExistingDataNetworks(DataEvaluation.DataEvaluationReason dataEvaluationReason) {
        if (this.mDataNetworkList.isEmpty()) {
            log("onReevaluateExistingDataNetworks: No existing data networks to re-evaluate.");
            return;
        }
        log("Re-evaluating " + this.mDataNetworkList.size() + " existing data networks due to " + dataEvaluationReason);
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            if (dataNetwork.isConnecting() || dataNetwork.isConnected()) {
                DataEvaluation dataEvaluationEvaluateDataNetwork = evaluateDataNetwork(dataNetwork, dataEvaluationReason);
                if (dataEvaluationEvaluateDataNetwork.containsDisallowedReasons()) {
                    tearDownGracefully(dataNetwork, getTearDownReason(dataEvaluationEvaluateDataNetwork));
                    MiuiDataControllerStub.get(this.mPhone.getPhoneId()).tearDownDunDataNetworkIfNeeded(this.mPhone, getTearDownReason(dataEvaluationEvaluateDataNetwork), null);
                }
            }
        }
    }

    private DataEvaluation evaluateDataNetworkHandover(DataNetwork dataNetwork) {
        boolean lastKnownRoamingState;
        HandoverRule next;
        DataSpecificRegistrationInfo dataSpecificInfo;
        DataEvaluation dataEvaluation = new DataEvaluation(DataEvaluation.DataEvaluationReason.DATA_HANDOVER);
        if (!dataNetwork.isConnecting() && !dataNetwork.isConnected()) {
            dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.ILLEGAL_STATE);
            return dataEvaluation;
        }
        boolean z = false;
        if (this.mDataConfigManager.isEnhancedIwlanHandoverCheckEnabled()) {
            NetworkRegistrationInfo networkRegistrationInfo = this.mServiceState.getNetworkRegistrationInfo(2, DataUtils.getTargetTransport(dataNetwork.getTransport()));
            if (networkRegistrationInfo != null) {
                if (!networkRegistrationInfo.isInService()) {
                    dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE);
                }
                if (isMtkImsDataSupport()) {
                    DataEvaluation.DataDisallowedReason dataDisallowedReason = DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE;
                    if (dataEvaluation.contains(dataDisallowedReason) && (dataNetwork.getNetworkCapabilities().hasCapability(4) || dataNetwork.getNetworkCapabilities().hasCapability(0))) {
                        dataEvaluation.removeDataDisallowedReason(dataDisallowedReason);
                    }
                }
                if (dataNetwork.getAttachedNetworkRequestList().stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda62
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return ((TelephonyNetworkRequest) obj).hasCapability(33);
                    }
                }) && (dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo()) != null && dataSpecificInfo.getVopsSupportInfo() != null && !dataSpecificInfo.getVopsSupportInfo().isVopsSupported() && !this.mDataConfigManager.shouldKeepNetworkUpInNonVops(networkRegistrationInfo.getNetworkRegistrationState())) {
                    dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.VOPS_NOT_SUPPORTED);
                }
                if (dataEvaluation.containsDisallowedReasons()) {
                    return dataEvaluation;
                }
            }
        }
        if (this.mDataConfigManager.isIwlanHandoverPolicyEnabled()) {
            List<HandoverRule> handoverRules = this.mDataConfigManager.getHandoverRules();
            int dataNetworkType = getDataNetworkType(dataNetwork.getTransport());
            if (dataNetworkType == 0) {
                dataNetworkType = dataNetwork.getLastKnownDataNetworkType();
            }
            int iNetworkTypeToAccessNetworkType = DataUtils.networkTypeToAccessNetworkType(dataNetworkType);
            NetworkRegistrationInfo networkRegistrationInfo2 = this.mServiceState.getNetworkRegistrationInfo(2, 1);
            if (networkRegistrationInfo2 != null && networkRegistrationInfo2.isInService()) {
                lastKnownRoamingState = this.mServiceState.getDataRoamingFromRegistration();
            } else {
                lastKnownRoamingState = dataNetwork.getLastKnownRoamingState();
            }
            Phone imsPhone = this.mPhone.getImsPhone();
            if (imsPhone != null && imsPhone.getCallTracker().getState() != PhoneConstants.State.IDLE) {
                z = true;
            }
            int iNetworkTypeToAccessNetworkType2 = DataUtils.networkTypeToAccessNetworkType(getDataNetworkType(DataUtils.getTargetTransport(dataNetwork.getTransport())));
            final NetworkCapabilities networkCapabilities = dataNetwork.getNetworkCapabilities();
            log("evaluateDataNetworkHandover: source=" + AccessNetworkConstants.AccessNetworkType.toString(iNetworkTypeToAccessNetworkType) + ", target=" + AccessNetworkConstants.AccessNetworkType.toString(iNetworkTypeToAccessNetworkType2) + ", roaming=" + lastKnownRoamingState + ", incall=" + z + ", ServiceState=" + this.mServiceState + ", capabilities=" + networkCapabilities);
            Iterator<HandoverRule> it = handoverRules.iterator();
            while (true) {
                if (it.hasNext()) {
                    next = it.next();
                    if (!next.isOnlyForRoaming || lastKnownRoamingState) {
                        if (!next.isOnlyForIncall || z) {
                            if (next.sourceAccessNetworks.contains(Integer.valueOf(iNetworkTypeToAccessNetworkType)) && next.targetAccessNetworks.contains(Integer.valueOf(iNetworkTypeToAccessNetworkType2))) {
                                if (next.networkCapabilities.isEmpty()) {
                                    break;
                                }
                                Stream<Integer> stream = next.networkCapabilities.stream();
                                Objects.requireNonNull(networkCapabilities);
                                if (stream.anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda63
                                    @Override // java.util.function.Predicate
                                    public final boolean test(Object obj) {
                                        return networkCapabilities.hasCapability(((Integer) obj).intValue());
                                    }
                                })) {
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    log("evaluateDataNetworkHandover: Did not find matching rule.");
                }
            }
            log("evaluateDataNetworkHandover: Matched " + next);
            if (next.type == 2) {
                dataEvaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.NOT_ALLOWED_BY_POLICY);
            } else {
                dataEvaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.NORMAL);
            }
            log("evaluateDataNetworkHandover: " + dataEvaluation);
            return dataEvaluation;
        }
        log("evaluateDataNetworkHandover: IWLAN handover policy not enabled.");
        dataEvaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.NORMAL);
        return dataEvaluation;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.data.DataNetworkController$14, reason: invalid class name */
    static /* synthetic */ class AnonymousClass14 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason;

        static {
            int[] iArr = new int[DataEvaluation.DataDisallowedReason.values().length];
            $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason = iArr;
            try {
                iArr[DataEvaluation.DataDisallowedReason.DATA_DISABLED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.ROAMING_DISABLED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.DEFAULT_DATA_UNSELECTED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.DATA_CONFIG_NOT_READY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.SIM_NOT_READY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.CONCURRENT_VOICE_DATA_NOT_ALLOWED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.SERVICE_OPTION_NOT_SUPPORTED.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.RADIO_POWER_OFF.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.PENDING_TEAR_DOWN_ALL.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.RADIO_DISABLED_BY_CARRIER.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.DATA_SERVICE_NOT_READY.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.NO_SUITABLE_DATA_PROFILE.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.DATA_NETWORK_TYPE_NOT_ALLOWED.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.CDMA_EMERGENCY_CALLBACK_MODE.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.RETRY_SCHEDULED.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.DATA_THROTTLED.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.DATA_PROFILE_INVALID.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.DATA_PROFILE_NOT_PREFERRED.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.NOT_ALLOWED_BY_POLICY.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.ILLEGAL_STATE.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.VOPS_NOT_SUPPORTED.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.ONLY_ALLOWED_SINGLE_NETWORK.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.HANDOVER_RETRY_STOPPED.ordinal()] = 24;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.DATA_LIMIT_REACHED.ordinal()] = 25;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.DATA_NETWORK_TRANSPORT_NOT_ALLOWED.ordinal()] = 26;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[DataEvaluation.DataDisallowedReason.UNSUPPORTED_NETWORK_CAPABILITIES.ordinal()] = 27;
            } catch (NoSuchFieldError unused27) {
            }
        }
    }

    private static int getTearDownReason(DataEvaluation dataEvaluation) {
        if (dataEvaluation.containsDisallowedReasons()) {
            switch (AnonymousClass14.$SwitchMap$com$android$internal$telephony$data$DataEvaluation$DataDisallowedReason[dataEvaluation.getDataDisallowedReasons().get(0).ordinal()]) {
                case 1:
                    return 4;
                case 2:
                    return 7;
                case 3:
                    return 17;
                case 4:
                    return 18;
                case 5:
                    return 19;
                case 6:
                    return 2;
                case 7:
                    return 8;
                case 8:
                    return 9;
                case 9:
                    return 3;
                case 10:
                    return 20;
                case 11:
                    return 11;
                case 12:
                    return 10;
                case 13:
                    return 21;
                case 14:
                    return 6;
                case 15:
                    return 22;
                case 16:
                    return 23;
                case 17:
                    return 24;
                case 18:
                    return 25;
                case 19:
                    return 26;
                case 20:
                    return 27;
                case 21:
                    return 28;
                case 22:
                    return 16;
                case 23:
                    return 29;
                case 24:
                    return 13;
                case 25:
                    return 31;
                case 26:
                    return 32;
                case 27:
                    return 34;
            }
        }
        return 0;
    }

    public boolean isInternetNetwork(int i) {
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            if (dataNetwork.getId() == i && dataNetwork.isConnected() && dataNetwork.isInternetSupported()) {
                return true;
            }
        }
        return false;
    }

    private boolean isDataDormant() {
        return this.mDataNetworkList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetworkController.m786$r8$lambda$TBLfPfqZ9SUOqjlufYbkBhPu08((DataNetwork) obj);
            }
        }) && this.mDataNetworkList.stream().noneMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetworkController.$r8$lambda$a2cjEL_weP5rOohuZImmeFsJsEA((DataNetwork) obj);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$-TBLfPfqZ9SUOqjlufYbkBhPu08, reason: not valid java name */
    public static /* synthetic */ boolean m786$r8$lambda$TBLfPfqZ9SUOqjlufYbkBhPu08(DataNetwork dataNetwork) {
        return dataNetwork.getLinkStatus() == 1;
    }

    public static /* synthetic */ boolean $r8$lambda$a2cjEL_weP5rOohuZImmeFsJsEA(DataNetwork dataNetwork) {
        return dataNetwork.getLinkStatus() == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDataActivity() {
        int dataActivity;
        if (isDataDormant()) {
            dataActivity = 4;
        } else {
            dataActivity = this.mPhone.getLinkBandwidthEstimator() != null ? this.mPhone.getLinkBandwidthEstimator().getDataActivity() : 0;
        }
        if (this.mDataActivity != dataActivity) {
            logv("updateDataActivity: dataActivity=" + DataUtils.dataActivityToString(dataActivity));
            this.mDataActivity = dataActivity;
            this.mPhone.notifyDataActivity();
        }
    }

    public void updateDataActivity(int i) {
        if (isDataDormant()) {
            i = 4;
        }
        if (this.mDataActivity != i) {
            logv("updateDataActivity: dataActivity=" + DataUtils.dataActivityToString(i));
            this.mDataActivity = i;
            this.mPhone.notifyDataActivity();
        }
    }

    public void removeNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        sendMessage(obtainMessage(3, telephonyNetworkRequest));
    }

    protected void onRemoveNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        if (!this.mAllNetworkRequestList.remove(telephonyNetworkRequest)) {
            loge("onRemoveNetworkRequest: Network request does not exist. " + telephonyNetworkRequest);
            return;
        }
        if (telephonyNetworkRequest.hasCapability(4)) {
            this.mImsThrottleCounter.addOccurrence();
            this.mLastReleasedImsRequestCapabilities = telephonyNetworkRequest.getCapabilities();
            this.mLastImsOperationIsRelease = true;
        }
        if (telephonyNetworkRequest.getAttachedNetwork() != null) {
            telephonyNetworkRequest.getAttachedNetwork().detachNetworkRequest(telephonyNetworkRequest, false);
            log("onRemoveNetworkRequest: Network request Removed: " + telephonyNetworkRequest);
        }
        telephonyNetworkRequest.setState(0);
        telephonyNetworkRequest.setEvaluation(null);
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onRemoveNetworkRequest(telephonyNetworkRequest);
    }

    public boolean isNetworkRequestExisting(TelephonyNetworkRequest telephonyNetworkRequest) {
        return this.mAllNetworkRequestList.contains(telephonyNetworkRequest);
    }

    public DataNetwork getDataNetworkByInterface(final String str) {
        return this.mDataNetworkList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetworkController.m792$r8$lambda$C_QNEWXNq6qSHOacPDwolpXci4((DataNetwork) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return str.equals(((DataNetwork) obj).getLinkProperties().getInterfaceName());
            }
        }).findFirst().orElse(null);
    }

    /* JADX INFO: renamed from: $r8$lambda$C_QNEWXNq6-qSHOacPDwolpXci4, reason: not valid java name */
    public static /* synthetic */ boolean m792$r8$lambda$C_QNEWXNq6qSHOacPDwolpXci4(DataNetwork dataNetwork) {
        return (dataNetwork.isDisconnecting() || dataNetwork.isDisconnected()) ? false : true;
    }

    public DataNetwork getDataNetworkByCid(final int i) {
        return this.mDataNetworkList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda33
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetworkController.m791$r8$lambda$BEhOdrTE3oF_DGyLkxTlSmJDgQ((DataNetwork) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda34
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetworkController.$r8$lambda$AWQkytuA8rUPdyZaXZZ4yMywcME(i, (DataNetwork) obj);
            }
        }).findFirst().orElse(null);
    }

    /* JADX INFO: renamed from: $r8$lambda$BEhO-drTE3oF_DGyLkxTlSmJDgQ, reason: not valid java name */
    public static /* synthetic */ boolean m791$r8$lambda$BEhOdrTE3oF_DGyLkxTlSmJDgQ(DataNetwork dataNetwork) {
        return (dataNetwork.isDisconnecting() || dataNetwork.isDisconnected()) ? false : true;
    }

    public static /* synthetic */ boolean $r8$lambda$AWQkytuA8rUPdyZaXZZ4yMywcME(int i, DataNetwork dataNetwork) {
        return dataNetwork.getId() == i;
    }

    public boolean isEsimBootStrapProvisioningActivated() {
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(this.mPhone.getSubId());
        return subscriptionInfoInternal != null && subscriptionInfoInternal.getProfileClass() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerImsFeatureRegistrationState(int i, final int i2) {
        RegistrationManager.RegistrationCallback registrationCallback = new RegistrationManager.RegistrationCallback() { // from class: com.android.internal.telephony.data.DataNetworkController.11
            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
                DataNetworkController.this.log("IMS " + DataUtils.imsFeatureToString(i2) + " registered. Attributes=" + imsRegistrationAttributes);
                DataNetworkController.this.mRegisteredImsFeaturesTransport.put(i2, imsRegistrationAttributes.getTransportType());
            }

            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onUnregistered(ImsReasonInfo imsReasonInfo) {
                DataNetworkController.this.log("IMS " + DataUtils.imsFeatureToString(i2) + " deregistered. Info=" + imsReasonInfo);
                DataNetworkController.this.mRegisteredImsFeaturesTransport.delete(i2);
                DataNetworkController.this.evaluatePendingImsDeregDataNetworks();
            }
        };
        try {
            if (i2 == 1) {
                this.mImsManager.getImsMmTelManager(i).registerImsRegistrationCallback(new DataNetworkController$$ExternalSyntheticLambda17(this), registrationCallback);
            } else if (i2 == 2) {
                this.mImsManager.getImsRcsManager(i).registerImsRegistrationCallback(new DataNetworkController$$ExternalSyntheticLambda17(this), registrationCallback);
            }
            this.mImsFeatureRegistrationCallbacks.put(i2, registrationCallback);
            log("Successfully register " + DataUtils.imsFeatureToString(i2) + " registration state. subId=" + i);
        } catch (ImsException e) {
            loge("updateImsFeatureRegistrationStateListening: subId=" + i + ", imsFeature=" + DataUtils.imsFeatureToString(i2) + ", " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterImsFeatureRegistrationState(int i, int i2) {
        RegistrationManager.RegistrationCallback registrationCallback = (RegistrationManager.RegistrationCallback) this.mImsFeatureRegistrationCallbacks.get(i2);
        if (registrationCallback != null) {
            if (i2 == 1) {
                this.mImsManager.getImsMmTelManager(i).unregisterImsRegistrationCallback(registrationCallback);
            } else if (i2 == 2) {
                this.mImsManager.getImsRcsManager(i).unregisterImsRegistrationCallback(registrationCallback);
            }
            log("Successfully unregistered " + DataUtils.imsFeatureToString(i2) + " registration state. subId=" + i);
            this.mImsFeatureRegistrationCallbacks.remove(i2);
        }
    }

    private void registerImsStateCallback(final int i) {
        if (isImsSupportedOnDevice()) {
            Function function = new Function() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda19
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.lambda$registerImsStateCallback$22(i, (Integer) obj);
                }
            };
            try {
                ImsStateCallback imsStateCallback = (ImsStateCallback) function.apply(1);
                this.mImsManager.getImsMmTelManager(i).registerImsStateCallback(new DataNetworkController$$ExternalSyntheticLambda17(this), imsStateCallback);
                this.mImsStateCallbacks.put(1, imsStateCallback);
                log("Successfully register MMTEL state on sub " + i);
                ImsStateCallback imsStateCallback2 = (ImsStateCallback) function.apply(2);
                this.mImsManager.getImsRcsManager(i).registerImsStateCallback(new DataNetworkController$$ExternalSyntheticLambda17(this), imsStateCallback2);
                this.mImsStateCallbacks.put(2, imsStateCallback2);
                log("Successfully register RCS state on sub " + i);
                return;
            } catch (ImsException e) {
                loge("Exception when registering IMS state callback. " + e);
                return;
            }
        }
        log("IMS is not supported on this device, skipping registerImsStateCallback");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ImsStateCallback lambda$registerImsStateCallback$22(final int i, final Integer num) {
        return new ImsStateCallback() { // from class: com.android.internal.telephony.data.DataNetworkController.12
            @Override // android.telephony.ims.ImsStateCallback
            public void onError() {
            }

            @Override // android.telephony.ims.ImsStateCallback
            public void onUnavailable(int i2) {
                DataNetworkController.this.unregisterImsFeatureRegistrationState(i, num.intValue());
            }

            @Override // android.telephony.ims.ImsStateCallback
            public void onAvailable() {
                DataNetworkController.this.mImsFeaturePackageName.put(num.intValue(), ImsResolver.getInstance().getConfiguredImsServicePackageName(DataNetworkController.this.mPhone.getPhoneId(), num.intValue()));
                DataNetworkController.this.registerImsFeatureRegistrationState(i, num.intValue());
            }
        };
    }

    private void unregisterImsStateCallbacks(int i) {
        if (isImsSupportedOnDevice()) {
            ImsStateCallback imsStateCallback = (ImsStateCallback) this.mImsStateCallbacks.get(1);
            if (imsStateCallback != null) {
                this.mImsManager.getImsMmTelManager(i).unregisterImsStateCallback(imsStateCallback);
                this.mImsStateCallbacks.remove(1);
                log("Unregister MMTEL state on sub " + i);
            }
            ImsStateCallback imsStateCallback2 = (ImsStateCallback) this.mImsStateCallbacks.get(2);
            if (imsStateCallback2 != null) {
                this.mImsManager.getImsRcsManager(i).unregisterImsStateCallback(imsStateCallback2);
                this.mImsStateCallbacks.remove(2);
                log("Unregister RCS state on sub " + i);
                return;
            }
            return;
        }
        log("IMS is not supported on this device, skipping unregisterImsStateCallbacks");
    }

    private boolean isImsSupportedOnDevice() {
        return this.mPhone.getContext().getPackageManager().hasSystemFeature("android.hardware.telephony.ims");
    }

    protected void initiateInternetDataConnectionState() {
        if (SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            log("initiateInternetDataConnectionState on " + this.mSubId);
            if (this.mInitInternetDataConnectionState) {
                TelephonyNetworkRequest telephonyNetworkRequest = new TelephonyNetworkRequest(new NetworkRequest.Builder().addCapability(12).build(), this.mPhone, this.mFeatureFlags);
                int dataNetworkType = getDataNetworkType(1);
                if (dataNetworkType == 0) {
                    dataNetworkType = this.mServiceState.getVoiceNetworkType();
                }
                DataProfile dataProfileForNetworkRequest = this.mDataProfileManager.getDataProfileForNetworkRequest(telephonyNetworkRequest, dataNetworkType, false, false, false);
                if (dataProfileForNetworkRequest == null || dataProfileForNetworkRequest.getApnSetting() == null) {
                    return;
                }
                this.mPhone.notifyDataConnection(DataNetwork.getPreciseDisconnectedDataConnectionState(dataProfileForNetworkRequest.getApnSetting()));
                this.mInitInternetDataConnectionState = false;
                log("initiateInternetDataConnectionState apnsetting = " + dataProfileForNetworkRequest.getApnSetting());
            }
        }
    }

    protected void onSubscriptionChanged() {
        if (this.mSubId != this.mPhone.getSubId()) {
            log("onDataConfigUpdated: mSubId changed from " + this.mSubId + " to " + this.mPhone.getSubId());
            if (isImsGracefulTearDownSupported()) {
                if (SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId())) {
                    registerImsStateCallback(this.mPhone.getSubId());
                } else {
                    unregisterImsStateCallbacks(this.mSubId);
                }
            }
            this.mSubId = this.mPhone.getSubId();
            updateSubscriptionPlans();
            if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
                this.mInitInternetDataConnectionState = true;
            }
            initiateInternetDataConnectionState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierConfigUpdated() {
        StringBuilder sb = new StringBuilder("onCarrierConfigUpdated: config is ");
        sb.append(this.mDataConfigManager.isConfigCarrierSpecific() ? "" : "not ");
        sb.append("carrier specific. mSimState=");
        sb.append(TelephonyManager.simStateToString(this.mSimState));
        log(sb.toString());
        updateNetworkRequestsPriority();
        DataEvaluation.DataEvaluationReason dataEvaluationReason = DataEvaluation.DataEvaluationReason.DATA_CONFIG_CHANGED;
        sendMessage(obtainMessage(5, dataEvaluationReason));
        sendMessage(obtainMessage(16, dataEvaluationReason));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDeviceConfigUpdated() {
        log("onDeviceConfigUpdated: DeviceConfig updated.");
        updateAnomalySlidingWindowCounters();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDynamicConfigChanged(DataConfig.DataConfigDiff dataConfigDiff) {
        log("onDynamicConfigChanged: Config changed. Re-evaluating affected data networks. diff=" + dataConfigDiff);
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            if (dataNetwork.isConnecting() || dataNetwork.isConnected()) {
                if (dataConfigDiff.isConnectionCapabilityAffected(this.mPhone.getCarrierId(), (Set) Arrays.stream(dataNetwork.getNetworkCapabilities().getCapabilities()).boxed().collect(Collectors.toSet()))) {
                    sendMessage(obtainMessage(16, DataEvaluation.DataEvaluationReason.DATA_DYNAMIC_CONFIG_CHANGED));
                    break;
                }
            }
        }
        sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.DATA_DYNAMIC_CONFIG_CHANGED));
    }

    private void updateNetworkRequestsPriority() {
        Iterator<TelephonyNetworkRequest> it = this.mAllNetworkRequestList.iterator();
        while (it.hasNext()) {
            it.next().updatePriority();
        }
    }

    private void updateAnomalySlidingWindowCounters() {
        this.mImsThrottleCounter = new SlidingWindowEventCounter(this.mDataConfigManager.getAnomalyImsReleaseRequestThreshold().timeWindow, this.mDataConfigManager.getAnomalyImsReleaseRequestThreshold().eventNumOccurrence);
        this.mNetworkUnwantedCounter = new SlidingWindowEventCounter(this.mDataConfigManager.getAnomalyNetworkUnwantedThreshold().timeWindow, this.mDataConfigManager.getAnomalyNetworkUnwantedThreshold().eventNumOccurrence);
        this.mSetupDataCallWwanFailureCounter = new SlidingWindowEventCounter(this.mDataConfigManager.getAnomalySetupDataCallThreshold().timeWindow, this.mDataConfigManager.getAnomalySetupDataCallThreshold().eventNumOccurrence);
        this.mSetupDataCallWlanFailureCounter = new SlidingWindowEventCounter(this.mDataConfigManager.getAnomalySetupDataCallThreshold().timeWindow, this.mDataConfigManager.getAnomalySetupDataCallThreshold().eventNumOccurrence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTrackNetworkUnwanted() {
        if (this.mNetworkUnwantedCounter.addOccurrence()) {
            reportAnomaly("Network Unwanted called " + this.mNetworkUnwantedCounter.getFrequencyString(), "9f3bc55b-bfa6-4e26-afaa-5031426a66d3");
        }
    }

    private NetworkRequestList findSatisfiableNetworkRequests(final DataProfile dataProfile) {
        if (MiuiDataControllerStub.get(this.mPhone.getPhoneId()).findSatisfiableNetworkRequests(this.mAllNetworkRequestList)) {
            log("findSatisfiableNetworkRequests true");
            return new NetworkRequestList((List<TelephonyNetworkRequest>) this.mAllNetworkRequestList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda42
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataNetworkController.$r8$lambda$eyM70UEiB5zW4emwItcPcVezjaE((TelephonyNetworkRequest) obj);
                }
            }).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda43
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((TelephonyNetworkRequest) obj).canBeSatisfiedBy(dataProfile);
                }
            }).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda44
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((TelephonyNetworkRequest) obj).hasTransport(10);
                }
            }).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda45
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return dataProfile.getApnSetting().isForInfrastructure(2);
                }
            }).collect(Collectors.toList()));
        }
        return new NetworkRequestList((List<TelephonyNetworkRequest>) this.mAllNetworkRequestList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda46
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetworkController.$r8$lambda$l6ZN_0KWMOXckqw7Gipib7QARHI((TelephonyNetworkRequest) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda47
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((TelephonyNetworkRequest) obj).canBeSatisfiedBy(dataProfile);
            }
        }).collect(Collectors.toList()));
    }

    public static /* synthetic */ boolean $r8$lambda$eyM70UEiB5zW4emwItcPcVezjaE(TelephonyNetworkRequest telephonyNetworkRequest) {
        return telephonyNetworkRequest.getState() == 0;
    }

    public static /* synthetic */ boolean $r8$lambda$l6ZN_0KWMOXckqw7Gipib7QARHI(TelephonyNetworkRequest telephonyNetworkRequest) {
        return telephonyNetworkRequest.getState() == 0;
    }

    private void setupDataNetwork(DataProfile dataProfile, DataRetryManager.DataSetupRetryEntry dataSetupRetryEntry, DataEvaluation.DataAllowedReason dataAllowedReason) {
        log("onSetupDataNetwork: dataProfile=" + dataProfile + ", retryEntry=" + dataSetupRetryEntry + ", allowed reason=" + dataAllowedReason + ", service state=" + this.mServiceState);
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            DataProfile dataProfile2 = dataNetwork.getDataProfile();
            if (dataProfile.equals(dataProfile2) || this.mDataProfileManager.lambda$isDataProfileCompatible$17(dataProfile, dataProfile2)) {
                log("onSetupDataNetwork: Found existing data network " + dataNetwork + " using the same or a similar data profile.");
                if (dataSetupRetryEntry != null) {
                    dataSetupRetryEntry.setState(4);
                    return;
                }
                return;
            }
        }
        NetworkRequestList networkRequestListFindSatisfiableNetworkRequests = findSatisfiableNetworkRequests(dataProfile);
        if (networkRequestListFindSatisfiableNetworkRequests.isEmpty()) {
            log("Can't find any unsatisfied network requests that can be satisfied by this data profile.");
            if (dataSetupRetryEntry != null) {
                dataSetupRetryEntry.setState(4);
                return;
            }
            return;
        }
        int preferredTransportByNetworkCapability = this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(networkRequestListFindSatisfiableNetworkRequests.get(0).getHighestPriorityApnTypeNetworkCapability());
        logl("Creating data network on " + AccessNetworkConstants.transportTypeToString(preferredTransportByNetworkCapability) + " with " + dataProfile + ", and attaching " + networkRequestListFindSatisfiableNetworkRequests.size() + " network requests to it.");
        ApnSetting apnSetting = dataProfile.getApnSetting();
        if (apnSetting != null && dataSetupRetryEntry == null) {
            resetMdDataRetryCount(apnSetting);
        }
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).setupDataNetwork(this.mPhone, dataProfile);
        NetworkRegistrationInfo networkRegistrationInfo = this.mServiceState.getNetworkRegistrationInfo(2, preferredTransportByNetworkCapability);
        this.mDataNetworkList.add(TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeDataNetwork(this.mPhone, this.mFeatureFlags, getLooper(), this.mDataServiceManagers, dataProfile, networkRequestListFindSatisfiableNetworkRequests, preferredTransportByNetworkCapability, preferredTransportByNetworkCapability == 1 && networkRegistrationInfo != null && networkRegistrationInfo.isNonTerrestrialNetwork(), dataAllowedReason, new AnonymousClass13(new DataNetworkController$$ExternalSyntheticLambda17(this), dataSetupRetryEntry)));
        updateCellularDataNetworkExistence(true, preferredTransportByNetworkCapability == 1 || this.mAnyCellularDataNetworkExisting);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.data.DataNetworkController$13, reason: invalid class name */
    class AnonymousClass13 extends DataNetwork.DataNetworkCallback {
        final /* synthetic */ DataRetryManager.DataSetupRetryEntry val$dataSetupRetryEntry;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass13(Executor executor, DataRetryManager.DataSetupRetryEntry dataSetupRetryEntry) {
            super(executor);
            this.val$dataSetupRetryEntry = dataSetupRetryEntry;
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onSetupDataFailed(DataNetwork dataNetwork, NetworkRequestList networkRequestList, int i, long j) {
            DataRetryManager.DataSetupRetryEntry dataSetupRetryEntry = this.val$dataSetupRetryEntry;
            if (dataSetupRetryEntry != null) {
                dataSetupRetryEntry.setState(2);
            }
            DataNetworkController.this.onDataNetworkSetupFailed(dataNetwork, networkRequestList, i, j);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onConnected(DataNetwork dataNetwork) {
            DataRetryManager.DataSetupRetryEntry dataSetupRetryEntry = this.val$dataSetupRetryEntry;
            if (dataSetupRetryEntry != null) {
                dataSetupRetryEntry.setState(3);
            }
            DataNetworkController.this.onDataNetworkConnected(dataNetwork);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onAttachFailed(DataNetwork dataNetwork, NetworkRequestList networkRequestList) {
            DataNetworkController.this.onAttachNetworkRequestsFailed(dataNetwork, networkRequestList);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onValidationStatusChanged(DataNetwork dataNetwork, int i, Uri uri) {
            DataNetworkController.this.onDataNetworkValidationStatusChanged(dataNetwork, i, uri);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onSuspendedStateChanged(DataNetwork dataNetwork, boolean z) {
            DataNetworkController.this.onDataNetworkSuspendedStateChanged(dataNetwork, z);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onDisconnected(DataNetwork dataNetwork, int i, int i2) {
            DataNetworkController.this.onDataNetworkDisconnected(dataNetwork, i, i2);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onHandoverSucceeded(DataNetwork dataNetwork) {
            DataNetworkController.this.onDataNetworkHandoverSucceeded(dataNetwork);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onHandoverFailed(DataNetwork dataNetwork, int i, long j, int i2) {
            DataNetworkController.this.onDataNetworkHandoverFailed(dataNetwork, i, j, i2);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onLinkStatusChanged(DataNetwork dataNetwork, int i) {
            DataNetworkController.this.onLinkStatusChanged(dataNetwork, i);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onPcoDataChanged(DataNetwork dataNetwork) {
            DataNetworkController.this.onPcoDataChanged(dataNetwork);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onNetworkCapabilitiesChanged(DataNetwork dataNetwork) {
            DataNetworkController.this.onNetworkCapabilitiesChanged(dataNetwork);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onTrackNetworkUnwanted(DataNetwork dataNetwork) {
            DataNetworkController.this.onTrackNetworkUnwanted();
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onRetryUnsatisfiedNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
            DataNetworkController.this.onRetryUnsatisfiedNetworkRequest(telephonyNetworkRequest);
        }

        @Override // com.android.internal.telephony.data.DataNetwork.DataNetworkCallback
        public void onQosSessionsChanged(final List<QosBearerSession> list) {
            DataNetworkController.this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$13$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback = (DataNetworkController.DataNetworkControllerCallback) obj;
                    dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$13$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            dataNetworkControllerCallback.onQosSessionsChanged(list);
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataNetworkSetupFailed(DataNetwork dataNetwork, NetworkRequestList networkRequestList, int i, long j) {
        logl("onDataNetworkSetupDataFailed: " + dataNetwork + ", cause=" + DataFailCause.toString(i) + ", retryDelayMillis=" + j + "ms.");
        long jOnDataNetworkSetupFailed = MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onDataNetworkSetupFailed(this.mPhone, dataNetwork, networkRequestList, i, j);
        this.mDataNetworkList.remove(dataNetwork);
        trackSetupDataCallFailure(dataNetwork.getTransport(), i);
        updateCellularDataNetworkExistence(this.mDataNetworkList.isEmpty() ^ true, getCellularDataNetworks().isEmpty() ^ true);
        networkRequestList.removeIf(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda58
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$onDataNetworkSetupFailed$33((TelephonyNetworkRequest) obj);
            }
        });
        if (networkRequestList.isEmpty()) {
            log("onDataNetworkSetupFailed: All requests have been released. Will not evaluate retry.");
        } else {
            if (MiuiDataControllerStub.get(this.mPhone.getPhoneId()).ignoreDataSetupRetry(this.mPhone, dataNetwork.getDataProfile(), i)) {
                return;
            }
            this.mDataRetryManager.evaluateDataSetupRetry(dataNetwork.getDataProfile(), dataNetwork.getTransport(), networkRequestList, i, jOnDataNetworkSetupFailed);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onDataNetworkSetupFailed$33(TelephonyNetworkRequest telephonyNetworkRequest) {
        return !this.mAllNetworkRequestList.contains(telephonyNetworkRequest);
    }

    private void trackSetupDataCallFailure(int i, int i2) {
        if (i == 1) {
            if (this.mPhone.getSignalStrength().getLevel() <= 1) {
                return;
            }
            if (i2 == 65535 || i2 == 65536) {
                reportAnomaly("RIL set up data call fails: unknown/unspecified error", "ce7d1465-d8e4-404a-b76f-de2c60bee843");
            }
            if (this.mSetupDataCallWwanFailureCounter.addOccurrence()) {
                reportAnomaly("RIL fails setup data call request " + this.mSetupDataCallWwanFailureCounter.getFrequencyString(), "e6a98b97-9e34-4977-9a92-01d52a6691f6");
                return;
            }
            return;
        }
        if (i == 2) {
            if (i2 == 65535 || i2 == 65536) {
                reportAnomaly("IWLAN set up data call fails: unknown/unspecified error", "a16fc15c-815b-4908-b8e6-5f3bc7cbc20b");
            }
            if (this.mSetupDataCallWlanFailureCounter.addOccurrence()) {
                reportAnomaly("IWLAN data service fails setup data call request " + this.mSetupDataCallWlanFailureCounter.getFrequencyString(), "e2248d8b-d55f-42bd-871c-0cfd80c3ddd1");
                return;
            }
            return;
        }
        loge("trackSetupDataCallFailure: INVALID transport.");
    }

    private void reportAnomaly(String str, String str2) {
        logl(str);
        AnomalyReporter.reportAnomaly(UUID.fromString(str2), str, this.mPhone.getCarrierId());
    }

    public void onDataNetworkConnected(final DataNetwork dataNetwork) {
        logl("onDataNetworkConnected: " + dataNetwork);
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onDataNetworkConnected(this.mPhone, dataNetwork);
        this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda27
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback = (DataNetworkController.DataNetworkControllerCallback) obj;
                dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda50
                    @Override // java.lang.Runnable
                    public final void run() {
                        DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback2 = dataNetworkControllerCallback;
                        DataNetwork dataNetwork2 = dataNetwork;
                        dataNetworkControllerCallback2.onDataNetworkConnected(dataNetwork2.getTransport(), dataNetwork2.getDataProfile());
                    }
                });
            }
        });
        this.mPreviousConnectedDataNetworkList.add(0, dataNetwork);
        if (this.mPreviousConnectedDataNetworkList.size() > 10) {
            this.mPreviousConnectedDataNetworkList.remove(10);
        }
        if (dataNetwork.isInternetSupported()) {
            updateOverallInternetDataState();
        }
        if (dataNetwork.getNetworkCapabilities().hasCapability(4)) {
            logl("IMS data state changed from " + TelephonyUtils.dataStateToString(this.mImsDataNetworkState) + " to CONNECTED.");
            this.mImsDataNetworkState = 2;
        }
        if (isEsimBootStrapProvisioningActivated()) {
            sendMessageDelayed(obtainMessage(16, DataEvaluation.DataEvaluationReason.CHECK_DATA_USAGE), this.mDataConfigManager.getReevaluateBootstrapSimDataUsageMillis());
        }
    }

    protected void onDataNetworkSetupRetry(DataRetryManager.DataSetupRetryEntry dataSetupRetryEntry) {
        final NetworkRequestList networkRequestList = new NetworkRequestList(dataSetupRetryEntry.networkRequestList);
        networkRequestList.removeIf(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$onDataNetworkSetupRetry$36((TelephonyNetworkRequest) obj);
            }
        });
        if (networkRequestList.isEmpty()) {
            final List groupedUnsatisfiedNetworkRequests = getGroupedUnsatisfiedNetworkRequests();
            dataSetupRetryEntry.networkRequestList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda9
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return groupedUnsatisfiedNetworkRequests.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda66
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj2) {
                            return DataNetworkController.$r8$lambda$_wYnyt0jMtPuQAUhbcZOhONHk5o(telephonyNetworkRequest, (DataNetworkController.NetworkRequestList) obj2);
                        }
                    });
                }
            }).forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda10
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    networkRequestList.add((TelephonyNetworkRequest) obj);
                }
            });
        }
        if (networkRequestList.isEmpty()) {
            loge("onDataNetworkSetupRetry: Request list is empty. Abort retry.");
            dataSetupRetryEntry.setState(4);
            return;
        }
        log("onDataNetworkSetupRetry: Request list:" + networkRequestList);
        TelephonyNetworkRequest telephonyNetworkRequest = networkRequestList.get(0);
        int preferredTransportByNetworkCapability = this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(telephonyNetworkRequest.getHighestPriorityApnTypeNetworkCapability());
        if (preferredTransportByNetworkCapability != dataSetupRetryEntry.transport) {
            log("Cannot re-satisfy " + telephonyNetworkRequest + " on " + AccessNetworkConstants.transportTypeToString(dataSetupRetryEntry.transport) + ". The preferred transport has switched to " + AccessNetworkConstants.transportTypeToString(preferredTransportByNetworkCapability) + ". " + dataSetupRetryEntry);
            dataSetupRetryEntry.setState(4);
            sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.PREFERRED_TRANSPORT_CHANGED));
            return;
        }
        DataEvaluation dataEvaluationEvaluateNetworkRequest = evaluateNetworkRequest(telephonyNetworkRequest, DataEvaluation.DataEvaluationReason.DATA_RETRY);
        if (!dataEvaluationEvaluateNetworkRequest.containsDisallowedReasons()) {
            DataProfile candidateDataProfile = dataSetupRetryEntry.dataProfile;
            if (candidateDataProfile == null || !this.mDataProfileManager.isDataProfileCompatible(candidateDataProfile)) {
                candidateDataProfile = dataEvaluationEvaluateNetworkRequest.getCandidateDataProfile();
            }
            if (candidateDataProfile != null) {
                setupDataNetwork(candidateDataProfile, dataSetupRetryEntry, dataEvaluationEvaluateNetworkRequest.getDataAllowedReason());
                return;
            } else {
                loge("onDataNetworkSetupRetry: Not able to find a suitable data profile to retry.");
                dataSetupRetryEntry.setState(2);
                return;
            }
        }
        dataSetupRetryEntry.setState(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onDataNetworkSetupRetry$36(TelephonyNetworkRequest telephonyNetworkRequest) {
        return !this.mAllNetworkRequestList.contains(telephonyNetworkRequest);
    }

    public static /* synthetic */ boolean $r8$lambda$_wYnyt0jMtPuQAUhbcZOhONHk5o(TelephonyNetworkRequest telephonyNetworkRequest, NetworkRequestList networkRequestList) {
        if (networkRequestList.get(telephonyNetworkRequest.getCapabilities()) == null) {
            return false;
        }
        final TelephonyNetworkRequest first = networkRequestList.getFirst();
        return first.getTransportTypes().length == 0 || telephonyNetworkRequest.getTransportTypes().length == 0 || Arrays.stream(telephonyNetworkRequest.getTransportTypes()).allMatch(new IntPredicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda68
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return first.hasTransport(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataNetworkHandoverRetry(DataRetryManager.DataHandoverRetryEntry dataHandoverRetryEntry) {
        DataNetwork dataNetwork = dataHandoverRetryEntry.dataNetwork;
        if (!this.mDataNetworkList.contains(dataNetwork)) {
            log("onDataNetworkHandoverRetry: " + dataNetwork + " no longer exists.");
            dataHandoverRetryEntry.setState(4);
            return;
        }
        if (!dataNetwork.isConnected()) {
            log("onDataNetworkHandoverRetry: " + dataNetwork + " is not in the right state.");
            dataHandoverRetryEntry.setState(4);
            return;
        }
        int preferredTransportByNetworkCapability = this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(dataNetwork.getApnTypeNetworkCapability());
        if (dataNetwork.getTransport() == preferredTransportByNetworkCapability) {
            log("onDataNetworkHandoverRetry: " + dataNetwork + " is already on the preferred transport " + AccessNetworkConstants.transportTypeToString(preferredTransportByNetworkCapability) + ".");
            dataHandoverRetryEntry.setState(4);
            return;
        }
        logl("onDataNetworkHandoverRetry: Start handover " + dataNetwork + " to " + AccessNetworkConstants.transportTypeToString(preferredTransportByNetworkCapability) + ", " + dataHandoverRetryEntry);
        tryHandoverDataNetwork(dataNetwork, preferredTransportByNetworkCapability, dataHandoverRetryEntry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataNetworkHandoverRetryStopped(DataNetwork dataNetwork) {
        int preferredTransportByNetworkCapability = this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(dataNetwork.getApnTypeNetworkCapability());
        if (dataNetwork.getTransport() == preferredTransportByNetworkCapability) {
            log("onDataNetworkHandoverRetryStopped: " + dataNetwork + " is already on the preferred transport " + AccessNetworkConstants.transportTypeToString(preferredTransportByNetworkCapability));
            return;
        }
        if (dataNetwork.shouldDelayImsTearDownDueToInCall()) {
            log("onDataNetworkHandoverRetryStopped: Delay IMS tear down until call ends. " + dataNetwork);
            return;
        }
        tearDownGracefully(dataNetwork, 13);
    }

    protected void onDataNetworkValidationStatusChanged(DataNetwork dataNetwork, final int i, Uri uri) {
        String str;
        StringBuilder sb = new StringBuilder("onDataNetworkValidationStatusChanged: ");
        sb.append(dataNetwork);
        sb.append(", validation status=");
        sb.append(DataUtils.validationStatusToString(i));
        if (uri != null) {
            str = ", " + uri;
        } else {
            str = "";
        }
        sb.append(str);
        log(sb.toString());
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onDataNetworkValidationStatusChanged(this.mPhone, dataNetwork, i, uri);
        if (!TextUtils.isEmpty(uri.toString())) {
            Intent intent = new Intent("android.telephony.action.CARRIER_SIGNAL_REDIRECTED");
            intent.putExtra("android.telephony.extra.REDIRECTION_URL", uri.toString());
            this.mPhone.getCarrierSignalAgent().notifyCarrierSignalReceivers(intent);
            log("Notify carrier signal receivers with redirectUri: " + uri);
        }
        if (i != 1 && i != 2) {
            loge("Invalid validation status " + i + " received.");
            return;
        }
        if (!this.mDataSettingsManager.isRecoveryOnBadNetworkEnabled()) {
            log("Ignore data network validation status changed because data stall recovery is disabled.");
            return;
        }
        NetworkCapabilities networkCapabilities = dataNetwork.getNetworkCapabilities();
        if (networkCapabilities.hasCapability(12) && networkCapabilities.hasCapability(13) && networkCapabilities.hasCapability(14) && networkCapabilities.hasCapability(15)) {
            if (i == 2 && (dataNetwork.getCurrentState() == null || dataNetwork.isDisconnected())) {
                log("Ignoring invalid validation status for disconnected DataNetwork");
            } else {
                this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback = (DataNetworkController.DataNetworkControllerCallback) obj;
                        dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda56
                            @Override // java.lang.Runnable
                            public final void run() {
                                dataNetworkControllerCallback.onInternetDataNetworkValidationStatusChanged(i);
                            }
                        });
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataNetworkSuspendedStateChanged(DataNetwork dataNetwork, boolean z) {
        if (dataNetwork.isInternetSupported()) {
            updateOverallInternetDataState();
        }
        if (dataNetwork.getNetworkCapabilities().hasCapability(4)) {
            StringBuilder sb = new StringBuilder("IMS data state changed from ");
            sb.append(TelephonyUtils.dataStateToString(this.mImsDataNetworkState));
            sb.append(" to ");
            sb.append(z ? "SUSPENDED" : "CONNECTED");
            logl(sb.toString());
            this.mImsDataNetworkState = z ? 3 : 2;
        }
    }

    protected void onDataNetworkDisconnected(DataNetwork dataNetwork, int i, int i2) {
        logl("onDataNetworkDisconnected: " + dataNetwork + ", cause=" + DataFailCause.toString(i) + "(" + i + "), tearDownReason=" + DataNetwork.tearDownReasonToString(i2));
        this.mDataNetworkList.remove(dataNetwork);
        this.mPendingImsDeregDataNetworks.remove(dataNetwork);
        this.mDataRetryManager.cancelPendingHandoverRetry(dataNetwork);
        if (dataNetwork.isInternetSupported()) {
            updateOverallInternetDataState();
        }
        onPcoDataChanged(dataNetwork);
        if (dataNetwork.getNetworkCapabilities().hasCapability(4)) {
            logl("IMS data state changed from " + TelephonyUtils.dataStateToString(this.mImsDataNetworkState) + " to DISCONNECTED.");
            this.mImsDataNetworkState = 0;
        }
        updateCellularDataNetworkExistence(!this.mDataNetworkList.isEmpty(), !getCellularDataNetworks().isEmpty());
        if (isMtkImsDataSupport()) {
            boolean zAnyMatch = this.mDataNetworkList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda6
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataNetworkController.$r8$lambda$I4b0gJ3KN2d1T9qAmHgNQF9D7Io((DataNetwork) obj);
                }
            });
            if (!this.mDataNetworkList.isEmpty()) {
                log("data network exist " + this.mAnyDataNetworkExisting + " has ims " + zAnyMatch + " mDataNetworkList.size " + this.mDataNetworkList.size());
            }
            if (zAnyMatch && this.mDataNetworkList.size() == 1) {
                log("all data disconnect except ims, power off radio immediately");
                this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda7
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback = (DataNetworkController.DataNetworkControllerCallback) obj;
                        dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda49
                            @Override // java.lang.Runnable
                            public final void run() {
                                dataNetworkControllerCallback.onAnyDataNetworkExistingChanged(false, false);
                            }
                        });
                    }
                });
            }
        }
        this.mTempDataNetwork = null;
        sendMessageDelayed(obtainMessage(5, DataEvaluation.DataEvaluationReason.RETRY_AFTER_DISCONNECTED), i2 == 14 ? 0L : getRetrySetupAfterDisconnect(dataNetwork, i));
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onDataNetworkDisconnected(this.mPhone, dataNetwork, i);
        if (TelephonyBaseUtilsStub.isAutoDevice()) {
            MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onDataNetworkDisconnected(this.mPhone, dataNetwork, i, i2);
        }
    }

    public static /* synthetic */ boolean $r8$lambda$I4b0gJ3KN2d1T9qAmHgNQF9D7Io(DataNetwork dataNetwork) {
        return dataNetwork.getNetworkCapabilities().hasCapability(4) && dataNetwork.isConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataNetworkHandoverSucceeded(DataNetwork dataNetwork) {
        logl("Handover successfully. " + dataNetwork + " to " + AccessNetworkConstants.transportTypeToString(dataNetwork.getTransport()));
        boolean z = this.mAnyDataNetworkExisting;
        boolean z2 = true;
        if (dataNetwork.getTransport() != 1 && !this.mAnyCellularDataNetworkExisting) {
            z2 = false;
        }
        updateCellularDataNetworkExistence(z, z2);
        sendMessage(obtainMessage(21, dataNetwork.getApnTypeNetworkCapability(), 0));
        sendMessage(obtainMessage(16, DataEvaluation.DataEvaluationReason.DATA_HANDOVER));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataNetworkHandoverFailed(DataNetwork dataNetwork, int i, long j, int i2) {
        logl("Handover failed. " + dataNetwork + ", cause=" + DataFailCause.toString(i) + ", retryDelayMillis=" + j + "ms, handoverFailureMode=" + DataCallResponse.failureModeToString(i2));
        updateCellularDataNetworkExistence(this.mAnyDataNetworkExisting, dataNetwork.getTransport() == 1 || this.mAnyCellularDataNetworkExisting);
        sendMessage(obtainMessage(16, DataEvaluation.DataEvaluationReason.DATA_HANDOVER));
        if (dataNetwork.getAttachedNetworkRequestList().isEmpty()) {
            log("onDataNetworkHandoverFailed: No network requests attached to " + dataNetwork + ". No need to retry since the network will be torn down soon.");
            return;
        }
        if (i2 == 1 || (i2 == 0 && i == 2251)) {
            sendMessageDelayed(obtainMessage(21, dataNetwork.getApnTypeNetworkCapability(), 0), REEVALUATE_PREFERRED_TRANSPORT_DELAY_MILLIS);
            return;
        }
        if (i2 == 3 || i2 == 0) {
            int preferredTransportByNetworkCapability = this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(dataNetwork.getApnTypeNetworkCapability());
            if (dataNetwork.getTransport() == preferredTransportByNetworkCapability) {
                log("onDataNetworkHandoverFailed: Already on preferred transport " + AccessNetworkConstants.transportTypeToString(preferredTransportByNetworkCapability) + ". No further actions needed.");
                return;
            }
            this.mDataRetryManager.evaluateDataSetupRetry(dataNetwork.getDataProfile(), DataUtils.getTargetTransport(dataNetwork.getTransport()), dataNetwork.getAttachedNetworkRequestList(), i, j);
            tearDownGracefully(dataNetwork, 13);
            return;
        }
        this.mDataRetryManager.evaluateDataHandoverRetry(dataNetwork, i, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAttachNetworkRequestsFailed(DataNetwork dataNetwork, NetworkRequestList networkRequestList) {
        log("Failed to attach " + networkRequestList + " to " + dataNetwork);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRetryUnsatisfiedNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        if (this.mAllNetworkRequestList.contains(telephonyNetworkRequest)) {
            sendMessageDelayed(obtainMessage(5, DataEvaluation.DataEvaluationReason.UNSATISFIED_REQUEST_DETACHED), REEVALUATE_UNSATISFIED_NETWORK_REQUESTS_AFTER_DETACHED_DELAY_MILLIS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataStallReestablishInternet() {
        log("onDataStallReestablishInternet: Tear down data networks that support internet.");
        this.mDataNetworkList.stream().filter(new DataNetworkController$$ExternalSyntheticLambda37()).forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda48
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((DataNetwork) obj).lambda$tearDownWhenConditionMet$11(12);
            }
        });
    }

    private void onSrvccStateChanged(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            return;
        }
        log("onSrvccStateChanged: " + TelephonyManager.srvccStateToString(iArr[0]));
        boolean z = iArr[0] == 0;
        this.mIsSrvccHandoverInProcess = z;
        if (z || hasMessages(16)) {
            return;
        }
        sendMessage(obtainMessage(16, DataEvaluation.DataEvaluationReason.SRVCC_STATE_CHANGED));
    }

    private void onDataServiceBindingChanged(final int i, boolean z) {
        StringBuilder sb = new StringBuilder("onDataServiceBindingChanged: ");
        sb.append(AccessNetworkConstants.transportTypeToString(i));
        sb.append(" data service is ");
        sb.append(z ? "bound." : "unbound.");
        log(sb.toString());
        if (z) {
            this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda54
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback = (DataNetworkController.DataNetworkControllerCallback) obj;
                    dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda72
                        @Override // java.lang.Runnable
                        public final void run() {
                            dataNetworkControllerCallback.onDataServiceBound(i);
                        }
                    });
                }
            });
        }
        this.mDataServiceBound.put(i, z);
    }

    private void onSimAbsent() {
        log("onSimAbsent");
        sendMessage(obtainMessage(16, DataEvaluation.DataEvaluationReason.SIM_REMOVAL));
    }

    protected void onSimStateChanged(final int i) {
        log("onSimStateChanged: state=" + TelephonyManager.simStateToString(i));
        if (this.mSimState != i) {
            this.mSimState = i;
            if (i == 1) {
                onSimAbsent();
                this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda25
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback = (DataNetworkController.DataNetworkControllerCallback) obj;
                        dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda57
                            @Override // java.lang.Runnable
                            public final void run() {
                                dataNetworkControllerCallback.onSimStateChanged(i);
                            }
                        });
                    }
                });
            } else if (i == 10) {
                sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.SIM_LOADED));
                this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda26
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback = (DataNetworkController.DataNetworkControllerCallback) obj;
                        dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda65
                            @Override // java.lang.Runnable
                            public final void run() {
                                dataNetworkControllerCallback.onSimStateChanged(i);
                            }
                        });
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onEvaluatePreferredTransport(int i, boolean z) {
        int preferredTransportByNetworkCapability = this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(i);
        log("onEvaluatePreferredTransport: " + DataUtils.networkCapabilityToString(i) + " preferred on " + AccessNetworkConstants.transportTypeToString(preferredTransportByNetworkCapability));
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            if (dataNetwork.getApnTypeNetworkCapability() == i) {
                if (shouldIgnoreHandover(dataNetwork)) {
                    log("ignore handover for " + dataNetwork);
                } else if (dataNetwork.getTransport() == preferredTransportByNetworkCapability) {
                    log("onEvaluatePreferredTransport:" + dataNetwork + " already on " + AccessNetworkConstants.transportTypeToString(preferredTransportByNetworkCapability));
                } else if (dataNetwork.isHandoverInProgress()) {
                    log("onEvaluatePreferredTransport: " + dataNetwork + " handover in progress.");
                } else if (z) {
                    tearDownGracefully(dataNetwork, 14);
                } else {
                    tryHandoverDataNetwork(dataNetwork, preferredTransportByNetworkCapability, null);
                }
            }
        }
    }

    private void tryHandoverDataNetwork(DataNetwork dataNetwork, int i, DataRetryManager.DataHandoverRetryEntry dataHandoverRetryEntry) {
        if (dataHandoverRetryEntry == null && this.mDataRetryManager.isAnyHandoverRetryScheduled(dataNetwork)) {
            log("tryHandoverDataNetwork: retry scheduled for" + dataNetwork + ", ignore this attempt");
            return;
        }
        DataEvaluation dataEvaluationEvaluateDataNetworkHandover = evaluateDataNetworkHandover(dataNetwork);
        log("tryHandoverDataNetwork: " + dataEvaluationEvaluateDataNetworkHandover + ", " + dataNetwork);
        if (!dataEvaluationEvaluateDataNetworkHandover.containsDisallowedReasons()) {
            logl("Start handover " + dataNetwork + " to " + AccessNetworkConstants.transportTypeToString(i));
            dataNetwork.startHandover(i, dataHandoverRetryEntry);
            return;
        }
        if (dataNetwork.shouldDelayImsTearDownDueToInCall()) {
            DataEvaluation.DataDisallowedReason dataDisallowedReason = DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE;
            if (dataEvaluationEvaluateDataNetworkHandover.containsOnly(dataDisallowedReason) || dataEvaluationEvaluateDataNetworkHandover.isSubsetOf(dataDisallowedReason, DataEvaluation.DataDisallowedReason.NOT_ALLOWED_BY_POLICY)) {
                if (dataHandoverRetryEntry != null) {
                    dataHandoverRetryEntry.setState(2);
                }
                this.mDataRetryManager.evaluateDataHandoverRetry(dataNetwork, 65542, -1L);
                logl("tryHandoverDataNetwork: Scheduled retry due to in voice call and target OOS");
                return;
            }
        }
        if (dataEvaluationEvaluateDataNetworkHandover.containsAny(DataEvaluation.DataDisallowedReason.NOT_ALLOWED_BY_POLICY, DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE, DataEvaluation.DataDisallowedReason.VOPS_NOT_SUPPORTED)) {
            logl("tryHandoverDataNetwork: Handover not allowed. Tear down" + dataNetwork + " so a new network can be setup on " + AccessNetworkConstants.transportTypeToString(i));
            tearDownGracefully(dataNetwork, 14);
            return;
        }
        if (dataEvaluationEvaluateDataNetworkHandover.containsAny(DataEvaluation.DataDisallowedReason.ILLEGAL_STATE, DataEvaluation.DataDisallowedReason.RETRY_SCHEDULED)) {
            logl("tryHandoverDataNetwork: Handover not allowed. " + dataNetwork + " will remain on " + AccessNetworkConstants.transportTypeToString(dataNetwork.getTransport()));
            return;
        }
        loge("tryHandoverDataNetwork: Unexpected handover evaluation result.");
    }

    private void updateSubscriptionPlans() {
        SubscriptionPlan[] subscriptionPlans = this.mNetworkPolicyManager.getSubscriptionPlans(this.mSubId, this.mPhone.getContext().getOpPackageName());
        this.mSubscriptionPlans.clear();
        this.mSubscriptionPlans.addAll(subscriptionPlans != null ? Arrays.asList(subscriptionPlans) : Collections.EMPTY_LIST);
        this.mCongestedOverrideNetworkTypes.clear();
        this.mUnmeteredOverrideNetworkTypes.clear();
        log("Subscription plans initialized: " + this.mSubscriptionPlans);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLinkStatusChanged(DataNetwork dataNetwork, int i) {
        int i2;
        if (this.mDataNetworkList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda51
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetworkController.$r8$lambda$0cvWT4e1f4CWz6PNrbISJAWY2Hw((DataNetwork) obj);
            }
        })) {
            i2 = this.mDataNetworkList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda52
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataNetworkController.m803$r8$lambda$sjBHQRsReU0VFYRbd2O7ouZZ3M((DataNetwork) obj);
                }
            }) ? 2 : 1;
        } else {
            i2 = 0;
        }
        if (this.mInternetLinkStatus != i2) {
            log("Internet link status changed to " + DataUtils.linkStatusToString(i2));
            this.mInternetLinkStatus = i2;
            this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda53
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$onLinkStatusChanged$56((DataNetworkController.DataNetworkControllerCallback) obj);
                }
            });
        }
        if (SystemProperties.getInt("ril.multisim.dualdata_status", 0) == 1 && this.mPhone.getPhoneId() != SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId())) {
            log("updateDataActivity: skip data activity notification for vice slot");
        } else {
            updateDataActivity();
        }
    }

    public static /* synthetic */ boolean $r8$lambda$0cvWT4e1f4CWz6PNrbISJAWY2Hw(DataNetwork dataNetwork) {
        return dataNetwork.isInternetSupported() && dataNetwork.isConnected();
    }

    /* JADX INFO: renamed from: $r8$lambda$sjB-HQRsReU0VFYRbd2O7ouZZ3M, reason: not valid java name */
    public static /* synthetic */ boolean m803$r8$lambda$sjBHQRsReU0VFYRbd2O7ouZZ3M(DataNetwork dataNetwork) {
        return dataNetwork.isInternetSupported() && dataNetwork.isConnected() && dataNetwork.getLinkStatus() == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLinkStatusChanged$56(final DataNetworkControllerCallback dataNetworkControllerCallback) {
        dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda73
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onLinkStatusChanged$55(dataNetworkControllerCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLinkStatusChanged$55(DataNetworkControllerCallback dataNetworkControllerCallback) {
        dataNetworkControllerCallback.onPhysicalLinkStatusChanged(this.mInternetLinkStatus);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPcoDataChanged(DataNetwork dataNetwork) {
        boolean z;
        int nrAdvancedCapablePcoId = this.mDataConfigManager.getNrAdvancedCapablePcoId();
        if (nrAdvancedCapablePcoId != 0) {
            Iterator<DataNetwork> it = this.mDataNetworkList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                PcoData pcoData = it.next().getPcoData().get(Integer.valueOf(nrAdvancedCapablePcoId));
                if (pcoData != null) {
                    byte[] bArr = pcoData.contents;
                    if (bArr.length > 0) {
                        z = true;
                        if (bArr[bArr.length - 1] == 1) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
            if (z != this.mNrAdvancedCapableByPco) {
                log("onPcoDataChanged: mNrAdvancedCapableByPco = " + z);
                this.mNrAdvancedCapableByPco = z;
                this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda64
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$onPcoDataChanged$58((DataNetworkController.DataNetworkControllerCallback) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPcoDataChanged$58(final DataNetworkControllerCallback dataNetworkControllerCallback) {
        dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda70
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onPcoDataChanged$57(dataNetworkControllerCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPcoDataChanged$57(DataNetworkControllerCallback dataNetworkControllerCallback) {
        dataNetworkControllerCallback.onNrAdvancedCapableByPcoChanged(this.mNrAdvancedCapableByPco);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNetworkCapabilitiesChanged(DataNetwork dataNetwork) {
        NetworkRequestList networkRequestList = new NetworkRequestList();
        for (TelephonyNetworkRequest telephonyNetworkRequest : this.mAllNetworkRequestList) {
            if (telephonyNetworkRequest.getState() == 0 && telephonyNetworkRequest.canBeSatisfiedBy(dataNetwork.getNetworkCapabilities())) {
                networkRequestList.add(telephonyNetworkRequest);
            }
        }
        if (!networkRequestList.isEmpty()) {
            log("Found more network requests that can be satisfied. " + networkRequestList);
            dataNetwork.attachNetworkRequests(networkRequestList);
        }
        if (dataNetwork.isInternetSupported()) {
            updateOverallInternetDataState();
        }
    }

    private boolean areNetworkAvailableServicesChanged(NetworkRegistrationInfo networkRegistrationInfo, NetworkRegistrationInfo networkRegistrationInfo2) {
        List<Integer> availableServices = networkRegistrationInfo.getAvailableServices();
        final List<Integer> availableServices2 = networkRegistrationInfo2.getAvailableServices();
        return (availableServices.size() == availableServices2.size() && availableServices.stream().allMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return availableServices2.contains((Integer) obj);
            }
        })) ? false : true;
    }

    private boolean shouldReevaluateDataNetworks(NetworkRegistrationInfo networkRegistrationInfo, NetworkRegistrationInfo networkRegistrationInfo2) {
        if (networkRegistrationInfo == null || networkRegistrationInfo2 == null || networkRegistrationInfo2.getAccessNetworkTechnology() == 0) {
            return false;
        }
        if (areNetworkAvailableServicesChanged(networkRegistrationInfo, networkRegistrationInfo2) || networkRegistrationInfo.getAccessNetworkTechnology() != networkRegistrationInfo2.getAccessNetworkTechnology() || networkRegistrationInfo.isRoaming() != networkRegistrationInfo2.isRoaming() || networkRegistrationInfo.isNonTerrestrialNetwork() != networkRegistrationInfo2.isNonTerrestrialNetwork() || shouldHandRoamingTypeChanged(networkRegistrationInfo.getRoamingType(), networkRegistrationInfo2.getRoamingType())) {
            return true;
        }
        DataSpecificRegistrationInfo dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo();
        DataSpecificRegistrationInfo dataSpecificInfo2 = networkRegistrationInfo2.getDataSpecificInfo();
        if (dataSpecificInfo2 == null) {
            return false;
        }
        return ((dataSpecificInfo != null && dataSpecificInfo.getVopsSupportInfo() != null && !dataSpecificInfo.getVopsSupportInfo().isVopsSupported()) || dataSpecificInfo2.getVopsSupportInfo() == null || dataSpecificInfo2.getVopsSupportInfo().isVopsSupported()) ? false : true;
    }

    private boolean shouldReevaluateNetworkRequests(ServiceState serviceState, ServiceState serviceState2, int i) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, i);
        NetworkRegistrationInfo networkRegistrationInfo2 = serviceState2.getNetworkRegistrationInfo(2, i);
        if (networkRegistrationInfo2 == null || networkRegistrationInfo2.getAccessNetworkTechnology() == 0) {
            return false;
        }
        if (networkRegistrationInfo == null || networkRegistrationInfo.getAccessNetworkTechnology() != networkRegistrationInfo2.getAccessNetworkTechnology() || ((!networkRegistrationInfo.isInService() && networkRegistrationInfo2.isInService()) || networkRegistrationInfo.isRoaming() != networkRegistrationInfo2.isRoaming() || areNetworkAvailableServicesChanged(networkRegistrationInfo, networkRegistrationInfo2))) {
            return true;
        }
        if ((!serviceStateAllowsPSAttach(serviceState, i) && serviceStateAllowsPSAttach(serviceState2, i)) || serviceState.isUsingNonTerrestrialNetwork() != serviceState2.isUsingNonTerrestrialNetwork() || shouldHandRoamingTypeChanged(networkRegistrationInfo.getRoamingType(), networkRegistrationInfo2.getRoamingType())) {
            return true;
        }
        DataSpecificRegistrationInfo dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo();
        DataSpecificRegistrationInfo dataSpecificInfo2 = networkRegistrationInfo2.getDataSpecificInfo();
        if (dataSpecificInfo == null) {
            return false;
        }
        return ((dataSpecificInfo2 != null && dataSpecificInfo2.getVopsSupportInfo() != null && !dataSpecificInfo2.getVopsSupportInfo().isVopsSupported()) || dataSpecificInfo.getVopsSupportInfo() == null || dataSpecificInfo.getVopsSupportInfo().isVopsSupported()) ? false : true;
    }

    protected void onServiceStateChanged() {
        boolean z;
        ServiceState serviceState = this.mPhone.getServiceStateTracker().getServiceState();
        StringBuilder sb = new StringBuilder("onServiceStateChanged: ");
        int i = 0;
        if (!this.mServiceState.equals(serviceState)) {
            log("onServiceStateChanged: changed to " + serviceState);
            int[] availableTransports = this.mAccessNetworksManager.getAvailableTransports();
            int length = availableTransports.length;
            int i2 = 0;
            z = false;
            while (i < length) {
                int i3 = availableTransports[i];
                NetworkRegistrationInfo networkRegistrationInfo = this.mServiceState.getNetworkRegistrationInfo(2, i3);
                NetworkRegistrationInfo networkRegistrationInfo2 = serviceState.getNetworkRegistrationInfo(2, i3);
                sb.append("[");
                sb.append(AccessNetworkConstants.transportTypeToString(i3));
                sb.append(": ");
                sb.append(networkRegistrationInfo != null ? TelephonyManager.getNetworkTypeName(networkRegistrationInfo.getAccessNetworkTechnology()) : null);
                sb.append("->");
                sb.append(networkRegistrationInfo2 != null ? TelephonyManager.getNetworkTypeName(networkRegistrationInfo2.getAccessNetworkTechnology()) : null);
                sb.append(", ");
                sb.append(networkRegistrationInfo != null ? NetworkRegistrationInfo.registrationStateToString(networkRegistrationInfo.getRegistrationState()) : null);
                sb.append("->");
                sb.append(networkRegistrationInfo2 != null ? NetworkRegistrationInfo.registrationStateToString(networkRegistrationInfo2.getRegistrationState()) : null);
                sb.append(", ");
                sb.append(networkRegistrationInfo != null ? NetworkRegistrationInfo.isNonTerrestrialNetworkToString(networkRegistrationInfo.isNonTerrestrialNetwork()) : null);
                sb.append("->");
                sb.append(networkRegistrationInfo2 != null ? NetworkRegistrationInfo.isNonTerrestrialNetworkToString(networkRegistrationInfo2.isNonTerrestrialNetwork()) : null);
                sb.append("] ");
                if (shouldReevaluateDataNetworks(networkRegistrationInfo, networkRegistrationInfo2) && !hasMessages(16)) {
                    sendMessage(obtainMessage(16, DataEvaluation.DataEvaluationReason.DATA_SERVICE_STATE_CHANGED));
                    z = true;
                }
                if (shouldReevaluateNetworkRequests(this.mServiceState, serviceState, i3) && !hasMessages(5)) {
                    sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.DATA_SERVICE_STATE_CHANGED));
                    i2 = 1;
                }
                i++;
            }
            this.mServiceState = serviceState;
            initiateInternetDataConnectionState();
            i = i2;
        } else {
            sb.append("not changed");
            z = false;
        }
        sb.append(". Evaluating network requests is ");
        sb.append(i != 0 ? "" : "not ");
        sb.append("needed, evaluating existing data networks is ");
        sb.append(z ? "" : "not ");
        sb.append("needed.");
        log(sb.toString());
    }

    private void updateOverallInternetDataState() {
        final ArraySet arraySet = new ArraySet();
        int i = 0;
        boolean z = false;
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            if (dataNetwork.isInternetSupported() && (dataNetwork.isConnected() || dataNetwork.isHandoverInProgress())) {
                arraySet.add(dataNetwork);
                if (!dataNetwork.isSuspended()) {
                    z = true;
                }
            }
        }
        if (z) {
            i = 2;
        } else if (!arraySet.isEmpty()) {
            i = 3;
        }
        logv("dataNetworkState=" + i + ", newConnectedInternetNetworks=" + arraySet);
        if (this.mInternetDataNetworkState != i) {
            logl("Internet data state changed from " + TelephonyUtils.dataStateToString(this.mInternetDataNetworkState) + " to " + TelephonyUtils.dataStateToString(i) + ".");
            this.mInternetDataNetworkState = i;
        }
        if (this.mConnectedInternetNetworks.equals(arraySet)) {
            return;
        }
        this.mConnectedInternetNetworks = arraySet;
        this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda55
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback = (DataNetworkController.DataNetworkControllerCallback) obj;
                dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda67
                    @Override // java.lang.Runnable
                    public final void run() {
                        dataNetworkControllerCallback.onConnectedInternetDataNetworksChanged(set);
                    }
                });
            }
        });
    }

    public DataConfigManager getDataConfigManager() {
        return this.mDataConfigManager;
    }

    public DataProfileManager getDataProfileManager() {
        return this.mDataProfileManager;
    }

    public DataSettingsManager getDataSettingsManager() {
        return this.mDataSettingsManager;
    }

    public DataRetryManager getDataRetryManager() {
        return this.mDataRetryManager;
    }

    public DataStallRecoveryManager getDataStallRecoveryManager() {
        return this.mDataStallRecoveryManager;
    }

    public List<SubscriptionPlan> getSubscriptionPlans() {
        return this.mSubscriptionPlans;
    }

    public Set<Integer> getUnmeteredOverrideNetworkTypes() {
        return this.mUnmeteredOverrideNetworkTypes;
    }

    public Set<Integer> getCongestedOverrideNetworkTypes() {
        return this.mCongestedOverrideNetworkTypes;
    }

    public int getDataNetworkType(int i) {
        NetworkRegistrationInfo networkRegistrationInfo = this.mServiceState.getNetworkRegistrationInfo(2, i);
        if (networkRegistrationInfo != null) {
            return networkRegistrationInfo.getAccessNetworkTechnology();
        }
        return 0;
    }

    protected int getDataRegistrationState(ServiceState serviceState, int i) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, i);
        if (networkRegistrationInfo != null) {
            return networkRegistrationInfo.getRegistrationState();
        }
        return 4;
    }

    public int getDataActivity() {
        return this.mDataActivity;
    }

    public void registerDataNetworkControllerCallback(DataNetworkControllerCallback dataNetworkControllerCallback) {
        sendMessage(obtainMessage(13, dataNetworkControllerCallback));
    }

    public void unregisterDataNetworkControllerCallback(DataNetworkControllerCallback dataNetworkControllerCallback) {
        sendMessage(obtainMessage(14, dataNetworkControllerCallback));
    }

    public void tearDownAllDataNetworks(int i) {
        sendMessage(obtainMessage(12, i, 0));
    }

    private void onTearDownAllDataNetworks(int i) {
        log("onTearDownAllDataNetworks: reason=" + DataNetwork.tearDownReasonToString(i));
        if (this.mDataNetworkList.isEmpty()) {
            log("tearDownAllDataNetworks: No pending networks. All disconnected now.");
            return;
        }
        List<DataNetwork> cellularDataNetworks = i == 3 ? getCellularDataNetworks() : this.mDataNetworkList;
        for (DataNetwork dataNetwork : cellularDataNetworks) {
            if (isMtkImsDataSupport() && i == 3 && dataNetwork.getNetworkCapabilities().hasCapability(4)) {
                log("onTearDownAllDataNetworks: ignore ims reason=" + DataNetwork.tearDownReasonToString(i));
                if (cellularDataNetworks.size() == 1) {
                    log("all data disconnect except ims, power off radio immediately");
                    this.mDataNetworkControllerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda61
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback = (DataNetworkController.DataNetworkControllerCallback) obj;
                            dataNetworkControllerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda69
                                @Override // java.lang.Runnable
                                public final void run() {
                                    dataNetworkControllerCallback.onAnyDataNetworkExistingChanged(false, false);
                                }
                            });
                        }
                    });
                }
            } else if (!dataNetwork.isDisconnecting()) {
                tearDownGracefully(dataNetwork, i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluatePendingImsDeregDataNetworks() {
        Iterator it = this.mPendingImsDeregDataNetworks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (isSafeToTearDown((DataNetwork) entry.getKey())) {
                log("evaluatePendingImsDeregDataNetworks: Safe to tear down data network " + entry.getKey() + " now.");
                ((Runnable) entry.getValue()).run();
                it.remove();
            } else {
                log("Still not safe to tear down " + entry.getKey() + ".");
            }
        }
    }

    private boolean isSafeToTearDown(DataNetwork dataNetwork) {
        String str;
        if (dataNetwork.hasNetworkCapabilityInNetworkRequests(10)) {
            return true;
        }
        Iterator it = SUPPORTED_IMS_FEATURES.iterator();
        while (it.hasNext()) {
            int iIntValue = ((Integer) it.next()).intValue();
            if (dataNetwork.getTransport() == this.mRegisteredImsFeaturesTransport.get(iIntValue, -1) && (str = (String) this.mImsFeaturePackageName.get(iIntValue)) != null && dataNetwork.getAttachedNetworkRequestList().hasNetworkRequestsFromPackage(str)) {
                return false;
            }
        }
        return true;
    }

    private boolean isImsGracefulTearDownSupported() {
        return this.mDataConfigManager.getImsDeregistrationDelay() > 0;
    }

    private void tearDownGracefully(DataNetwork dataNetwork, int i) {
        String str;
        long imsDeregistrationDelay = this.mDataConfigManager.getImsDeregistrationDelay();
        if (isImsGracefulTearDownSupported() && !isSafeToTearDown(dataNetwork)) {
            int i2 = this.mRegisteredImsFeaturesTransport.get(1, -1);
            int i3 = this.mRegisteredImsFeaturesTransport.get(2, -1);
            StringBuilder sb = new StringBuilder("tearDownGracefully: Not safe to tear down ");
            sb.append(dataNetwork);
            sb.append(" at this point. Wait for IMS de-registration or timeout. MMTEL=");
            String str2 = "not registered";
            if (i2 == -1) {
                str = "not registered";
            } else {
                str = "registered on " + AccessNetworkConstants.transportTypeToString(i2);
            }
            sb.append(str);
            sb.append(", RCS=");
            if (i3 != -1) {
                str2 = "registered on " + AccessNetworkConstants.transportTypeToString(i3);
            }
            sb.append(str2);
            log(sb.toString());
            Runnable runnableTearDownWhenConditionMet = dataNetwork.tearDownWhenConditionMet(i, imsDeregistrationDelay);
            if (runnableTearDownWhenConditionMet != null) {
                this.mPendingImsDeregDataNetworks.put(dataNetwork, runnableTearDownWhenConditionMet);
                return;
            }
            log(dataNetwork + " is being torn down already.");
            return;
        }
        log("tearDownGracefully: Safe to tear down " + dataNetwork);
        dataNetwork.lambda$tearDownWhenConditionMet$11(i);
    }

    public int getInternetDataNetworkState() {
        return this.mInternetDataNetworkState;
    }

    public List<String> getDataServicePackages() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mDataServiceManagers.size(); i++) {
            arrayList.add(this.mDataServiceManagers.valueAt(i).getDataServicePackageName());
        }
        return arrayList;
    }

    public void requestNetworkValidation(final int i, final Consumer<Integer> consumer) {
        if (DataUtils.networkCapabilityToApnType(i) == 0) {
            loge("requestNetworkValidation: the capability is not an apn type based. capability:" + i);
            Objects.requireNonNull(consumer);
            FunctionalUtils.ignoreRemoteException(new DataNetwork$$ExternalSyntheticLambda10(consumer)).accept(2);
            return;
        }
        List<DataNetwork> list = this.mDataNetworkList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda28
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((DataNetwork) obj).getNetworkCapabilities().hasCapability(i);
            }
        }).toList();
        if (!list.isEmpty()) {
            list.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda29
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((DataNetwork) obj).requestNetworkValidation(consumer);
                }
            });
            return;
        }
        loge("requestNetworkValidation: No matching DataNetwork was found");
        Objects.requireNonNull(consumer);
        FunctionalUtils.ignoreRemoteException(new DataNetwork$$ExternalSyntheticLambda10(consumer)).accept(2);
    }

    protected List<DataNetwork> getDataNetworkList() {
        return this.mDataNetworkList;
    }

    public void updateCarrierConfigState(int i, boolean z) {
        if (i != 10 || !z) {
            log("sim not load or carrier config not ready!");
            return;
        }
        this.mDataConfigManager.callUpdateCarrierConfig();
        this.mSimState = 10;
        log("sim load and carrier config ready!");
    }

    protected void log(String str) {
        Rlog.d(this.mLogTag, str);
    }

    protected void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }

    protected void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        final AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println(DataNetworkController.class.getSimpleName() + "-" + this.mPhone.getPhoneId() + ":");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("Current data networks:");
        androidUtilIndentingPrintWriter.increaseIndent();
        Iterator<DataNetwork> it = this.mDataNetworkList.iterator();
        while (it.hasNext()) {
            it.next().dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Pending tear down data networks:");
        androidUtilIndentingPrintWriter.increaseIndent();
        Iterator it2 = this.mPendingImsDeregDataNetworks.keySet().iterator();
        while (it2.hasNext()) {
            ((DataNetwork) it2.next()).dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Previously connected data networks: (up to 10)");
        androidUtilIndentingPrintWriter.increaseIndent();
        for (DataNetwork dataNetwork : this.mPreviousConnectedDataNetworkList) {
            if (!this.mDataNetworkList.contains(dataNetwork)) {
                dataNetwork.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
            }
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("All telephony network requests:");
        androidUtilIndentingPrintWriter.increaseIndent();
        Iterator<TelephonyNetworkRequest> it3 = this.mAllNetworkRequestList.iterator();
        while (it3.hasNext()) {
            androidUtilIndentingPrintWriter.println(it3.next());
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        int i = this.mRegisteredImsFeaturesTransport.get(1, -1);
        int i2 = this.mRegisteredImsFeaturesTransport.get(2, -1);
        StringBuilder sb = new StringBuilder("IMS features registration state: MMTEL=");
        sb.append(i == -1 ? "not registered" : "registered on " + AccessNetworkConstants.transportTypeToString(i));
        sb.append(", RCS=");
        sb.append(i2 != -1 ? "registered on " + AccessNetworkConstants.transportTypeToString(i2) : "not registered");
        androidUtilIndentingPrintWriter.println(sb.toString());
        androidUtilIndentingPrintWriter.println("mServiceState=" + this.mServiceState);
        androidUtilIndentingPrintWriter.println("mPsRestricted=" + this.mPsRestricted);
        androidUtilIndentingPrintWriter.println("mAnyDataNetworkExisting=" + this.mAnyDataNetworkExisting);
        androidUtilIndentingPrintWriter.println("mAnyCellularDataNetworkExisting=" + this.mAnyCellularDataNetworkExisting);
        androidUtilIndentingPrintWriter.println("mInternetDataNetworkState=" + TelephonyUtils.dataStateToString(this.mInternetDataNetworkState));
        androidUtilIndentingPrintWriter.println("mImsDataNetworkState=" + TelephonyUtils.dataStateToString(this.mImsDataNetworkState));
        androidUtilIndentingPrintWriter.println("mDataServiceBound=" + this.mDataServiceBound);
        androidUtilIndentingPrintWriter.println("mIsSrvccHandoverInProcess=" + this.mIsSrvccHandoverInProcess);
        androidUtilIndentingPrintWriter.println("mSimState=" + TelephonyManager.simStateToString(this.mSimState));
        androidUtilIndentingPrintWriter.println("mDataNetworkControllerCallbacks=" + this.mDataNetworkControllerCallbacks);
        androidUtilIndentingPrintWriter.println("Subscription plans:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mSubscriptionPlans.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataNetworkController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                androidUtilIndentingPrintWriter.println((SubscriptionPlan) obj);
            }
        });
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Unmetered override network types=" + ((String) this.mUnmeteredOverrideNetworkTypes.stream().map(new DataConfigManager$$ExternalSyntheticLambda8()).collect(Collectors.joining(","))));
        androidUtilIndentingPrintWriter.println("Congested override network types=" + ((String) this.mCongestedOverrideNetworkTypes.stream().map(new DataConfigManager$$ExternalSyntheticLambda8()).collect(Collectors.joining(","))));
        androidUtilIndentingPrintWriter.println("mImsThrottleCounter=" + this.mImsThrottleCounter);
        androidUtilIndentingPrintWriter.println("mNetworkUnwantedCounter=" + this.mNetworkUnwantedCounter);
        androidUtilIndentingPrintWriter.println("mBootStrapSimTotalDataUsageBytes=" + this.mBootStrapSimTotalDataUsageBytes);
        androidUtilIndentingPrintWriter.println("Local logs:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("-------------------------------------");
        this.mDataProfileManager.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.println("-------------------------------------");
        this.mDataRetryManager.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.println("-------------------------------------");
        this.mDataSettingsManager.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.println("-------------------------------------");
        this.mDataStallRecoveryManager.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.println("-------------------------------------");
        this.mDataConfigManager.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
    }

    protected long getRetrySetupAfterDisconnect(DataNetwork dataNetwork, int i) {
        return this.mDataConfigManager.getRetrySetupAfterDisconnectMillis();
    }

    private boolean isMtkImsDataSupport() {
        return "0".equals(SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0")) || "1".equals(SystemProperties.get("ro.mtk.ims.data.feature_support"));
    }
}
