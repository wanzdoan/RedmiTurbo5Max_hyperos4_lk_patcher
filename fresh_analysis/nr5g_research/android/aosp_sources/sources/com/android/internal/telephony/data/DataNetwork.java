package com.android.internal.telephony.data;

import android.content.Intent;
import android.net.IpPrefix;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkAgentConfig;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkScore;
import android.net.ProxyInfo;
import android.net.RouteInfo;
import android.net.TelephonyNetworkSpecifier;
import android.net.Uri;
import android.net.vcn.VcnManager;
import android.net.vcn.VcnNetworkPolicyResult;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.AccessNetworkConstants;
import android.telephony.AnomalyReporter;
import android.telephony.DataFailCause;
import android.telephony.DataSpecificRegistrationInfo;
import android.telephony.LinkCapacityEstimate;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PcoData;
import android.telephony.PreciseDataConnectionState;
import android.telephony.ServiceState;
import android.telephony.SubscriptionPlan;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.telephony.data.DataServiceCallback;
import android.telephony.data.NetworkSliceInfo;
import android.telephony.data.Qos;
import android.telephony.data.QosBearerSession;
import android.telephony.data.TrafficDescriptor;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperDataManager;
import com.android.internal.telephony.IState;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda0;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda2;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.metrics.DataCallSessionStats;
import com.android.internal.telephony.metrics.DataNetworkValidationStats;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.util.LinkPropertiesUtils;
import com.android.internal.telephony.util.NetUtils;
import com.android.internal.telephony.util.NetworkCapabilitiesUtils;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class DataNetwork extends StateMachine {
    public static final int BANDWIDTH_SOURCE_BANDWIDTH_ESTIMATOR = 3;
    public static final int BANDWIDTH_SOURCE_CARRIER_CONFIG = 2;
    public static final int BANDWIDTH_SOURCE_MODEM = 1;
    public static final int BANDWIDTH_SOURCE_UNKNOWN = 0;
    protected static final int EVENT_DEACTIVATE_DATA_NETWORK_RESPONSE = 19;
    protected static final int EVENT_DETACH_ALL_NETWORK_REQUESTS = 10;
    protected static final int EVENT_DETACH_NETWORK_REQUEST = 3;
    protected static final int EVENT_SETUP_DATA_NETWORK_RESPONSE = 6;
    protected static final int EVENT_TEAR_DOWN_NETWORK = 7;
    private static final List MUTABLE_CAPABILITIES = List.of((Object[]) new Integer[]{14, 16, 17, 18, 19, 20, 21, 24, 25, 28, 32, 11, 33, 0, 9});
    public static final int TEAR_DOWN_REASON_AIRPLANE_MODE_ON = 3;
    public static final int TEAR_DOWN_REASON_CDMA_EMERGENCY_CALLBACK_MODE = 22;
    public static final int TEAR_DOWN_REASON_CONCURRENT_VOICE_DATA_NOT_ALLOWED = 8;
    public static final int TEAR_DOWN_REASON_CONNECTIVITY_SERVICE_UNWANTED = 1;
    public static final int TEAR_DOWN_REASON_DATA_CONFIG_NOT_READY = 19;
    public static final int TEAR_DOWN_REASON_DATA_DISABLED = 4;
    public static final int TEAR_DOWN_REASON_DATA_LIMIT_REACHED = 31;
    public static final int TEAR_DOWN_REASON_DATA_NETWORK_TRANSPORT_NOT_ALLOWED = 32;
    public static final int TEAR_DOWN_REASON_DATA_PROFILE_INVALID = 25;
    public static final int TEAR_DOWN_REASON_DATA_PROFILE_NOT_PREFERRED = 26;
    public static final int TEAR_DOWN_REASON_DATA_SERVICE_NOT_READY = 10;
    public static final int TEAR_DOWN_REASON_DATA_STALL = 12;
    public static final int TEAR_DOWN_REASON_DATA_THROTTLED = 24;
    public static final int TEAR_DOWN_REASON_DEFAULT_DATA_UNSELECTED = 17;
    public static final int TEAR_DOWN_REASON_DEVICE_SHUT_DOWN = 33;
    public static final int TEAR_DOWN_REASON_HANDOVER_FAILED = 13;
    public static final int TEAR_DOWN_REASON_HANDOVER_NOT_ALLOWED = 14;
    public static final int TEAR_DOWN_REASON_ILLEGAL_STATE = 28;
    public static final int TEAR_DOWN_REASON_NONE = 0;
    public static final int TEAR_DOWN_REASON_NOT_ALLOWED_BY_POLICY = 27;
    public static final int TEAR_DOWN_REASON_NOT_IN_SERVICE = 18;
    public static final int TEAR_DOWN_REASON_NO_LIVE_REQUEST = 5;
    public static final int TEAR_DOWN_REASON_NO_SUITABLE_DATA_PROFILE = 21;
    public static final int TEAR_DOWN_REASON_ONLY_ALLOWED_SINGLE_NETWORK = 29;
    public static final int TEAR_DOWN_REASON_PENDING_TEAR_DOWN_ALL = 20;
    public static final int TEAR_DOWN_REASON_POWER_OFF_BY_CARRIER = 11;
    public static final int TEAR_DOWN_REASON_PREFERRED_DATA_SWITCHED = 30;
    public static final int TEAR_DOWN_REASON_RAT_NOT_ALLOWED = 6;
    public static final int TEAR_DOWN_REASON_RETRY_SCHEDULED = 23;
    public static final int TEAR_DOWN_REASON_ROAMING_DISABLED = 7;
    public static final int TEAR_DOWN_REASON_SERVICE_OPTION_NOT_SUPPORTED = 9;
    public static final int TEAR_DOWN_REASON_SIM_REMOVAL = 2;
    public static final int TEAR_DOWN_REASON_UNSUPPORTED_NETWORK_CAPABILITIES = 34;
    public static final int TEAR_DOWN_REASON_VCN_REQUESTED = 15;
    public static final int TEAR_DOWN_REASON_VOPS_NOT_SUPPORTED = 16;
    private final AccessNetworksManager mAccessNetworksManager;
    private AccessNetworksManager.AccessNetworksManagerCallback mAccessNetworksManagerCallback;
    private int[] mAdministratorUids;
    protected final DataNetworkController.NetworkRequestList mAttachedNetworkRequestList;
    private TelephonyManager.CarrierPrivilegesCallback mCarrierPrivilegesCallback;
    private int mCarrierServicePackageUid;
    protected final SparseIntArray mCid;
    private boolean mCongested;
    private final ConnectedState mConnectedState;
    private final ConnectingState mConnectingState;
    private final DataEvaluation.DataAllowedReason mDataAllowedReason;
    private DataCallResponse mDataCallResponse;
    protected final DataCallSessionStats mDataCallSessionStats;
    protected final DataConfigManager mDataConfigManager;
    private DataConfigManager.DataConfigManagerCallback mDataConfigManagerCallback;
    protected final DataNetworkCallback mDataNetworkCallback;
    protected final DataNetworkController mDataNetworkController;
    private final DataNetworkController.DataNetworkControllerCallback mDataNetworkControllerCallback;
    private final DataNetworkValidationStats mDataNetworkValidationStats;
    protected DataProfile mDataProfile;
    private final SparseArray mDataServiceManagers;
    private DataSettingsManager.DataSettingsManagerCallback mDataSettingsManagerCallback;
    private Qos mDefaultQos;
    private final DefaultState mDefaultState;
    private final DisconnectedState mDisconnectedState;
    private final DisconnectingState mDisconnectingState;
    private boolean mEverConnected;
    private int mFailCause;
    private final FeatureFlags mFlags;
    private DataProfile mHandoverDataProfile;
    private final HandoverState mHandoverState;
    private IHyperDataManager mIHyperDataManager;
    private int mInitialNetworkAgentId;
    protected boolean mInvokedDataDeactivation;
    private boolean mIsCidReuseTeardown;
    private KeepaliveTracker mKeepaliveTracker;
    private int mLastKnownDataNetworkType;
    private boolean mLastKnownRoamingState;
    private int mLastNotifiedAccessNetwork;
    private LinkBandwidthEstimator.LinkBandwidthEstimatorCallback mLinkBandwidthEstimatorCallback;
    private LinkProperties mLinkProperties;
    private int mLinkStatus;
    private final LocalLog mLocalLog;
    protected String mLogTag;
    protected TelephonyNetworkAgent mNetworkAgent;
    private NetworkBandwidth mNetworkBandwidth;
    protected NetworkCapabilities mNetworkCapabilities;
    private NetworkScore mNetworkScore;
    private NetworkSliceInfo mNetworkSliceInfo;
    private Consumer mNetworkValidationResultCodeCallback;
    private int mNetworkValidationStatus;
    private boolean mOnPreferredDataPhone;
    private final Map mPcoData;
    private int mPduSessionId;
    protected final Phone mPhone;
    private final PhoneSwitcher mPhoneSwitcher;
    private PhoneSwitcher.PhoneSwitcherCallback mPhoneSwitcherCallback;
    private int mPhysicalNetworkSlotIndex;
    private int mPhysicalNetworkTransportType;
    private PreciseDataConnectionState mPreciseDataConnectionState;
    private final List mQosBearerSessions;
    private QosCallbackTracker mQosCallbackTracker;
    private long mRetryDelayMillis;
    private final CommandsInterface mRil;
    private boolean mSatellite;
    private final int mSubId;
    protected boolean mSuspended;
    private String mTcpBufferSizes;
    private int mTearDownReason;
    private TelephonyDisplayInfo mTelephonyDisplayInfo;
    private boolean mTempNotMetered;
    private boolean mTempNotMeteredSupported;
    private final List mTrafficDescriptors;
    protected int mTransport;
    private final VcnManager mVcnManager;
    private VcnManager.VcnNetworkPolicyChangeListener mVcnPolicyChangeListener;

    protected NetworkCapabilities adjustNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        return networkCapabilities;
    }

    protected void enableVolteIotFirewall(boolean z) {
    }

    protected boolean handleEventsOfConnectedState(int i) {
        return false;
    }

    @Override // com.android.internal.telephony.StateMachine
    protected void logv(String str) {
    }

    protected int mtkGetCustomizedMtu(ApnSetting apnSetting, int i) {
        return 0;
    }

    protected boolean mtkSkipDataStallAlarm(String str) {
        return false;
    }

    protected void mtkUpdateNetworkCapabilities(NetworkCapabilities.Builder builder) {
    }

    protected int replaceTearDownReason(int i) {
        return (i == 3 || i == 33) ? 2 : 1;
    }

    protected boolean reviseSuspendedState(boolean z, NetworkRegistrationInfo networkRegistrationInfo) {
        return z;
    }

    protected void updateLingerAndTearDownDelayTimers(int i) {
    }

    public static class NetworkBandwidth {
        public final int downlinkBandwidthKbps;
        public final int uplinkBandwidthKbps;

        public NetworkBandwidth(int i, int i2) {
            this.downlinkBandwidthKbps = i;
            this.uplinkBandwidthKbps = i2;
        }

        public String toString() {
            return String.format("NetworkBandwidth=[downlink=%d, uplink=%d]", Integer.valueOf(this.downlinkBandwidthKbps), Integer.valueOf(this.uplinkBandwidthKbps));
        }
    }

    public static abstract class DataNetworkCallback extends DataCallback {
        public abstract void onAttachFailed(DataNetwork dataNetwork, DataNetworkController.NetworkRequestList networkRequestList);

        public abstract void onConnected(DataNetwork dataNetwork);

        public abstract void onDisconnected(DataNetwork dataNetwork, int i, int i2);

        public abstract void onHandoverFailed(DataNetwork dataNetwork, int i, long j, int i2);

        public abstract void onHandoverSucceeded(DataNetwork dataNetwork);

        public abstract void onLinkStatusChanged(DataNetwork dataNetwork, int i);

        public abstract void onNetworkCapabilitiesChanged(DataNetwork dataNetwork);

        public abstract void onPcoDataChanged(DataNetwork dataNetwork);

        public abstract void onQosSessionsChanged(List<QosBearerSession> list);

        public abstract void onRetryUnsatisfiedNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest);

        public abstract void onSetupDataFailed(DataNetwork dataNetwork, DataNetworkController.NetworkRequestList networkRequestList, int i, long j);

        public abstract void onSuspendedStateChanged(DataNetwork dataNetwork, boolean z);

        public abstract void onTrackNetworkUnwanted(DataNetwork dataNetwork);

        public abstract void onValidationStatusChanged(DataNetwork dataNetwork, int i, Uri uri);

        public DataNetworkCallback(Executor executor) {
            super(executor);
        }
    }

    public DataNetwork(Phone phone, FeatureFlags featureFlags, Looper looper, SparseArray<DataServiceManager> sparseArray, DataProfile dataProfile, DataNetworkController.NetworkRequestList networkRequestList, int i, boolean z, DataEvaluation.DataAllowedReason dataAllowedReason, DataNetworkCallback dataNetworkCallback) {
        super("DataNetwork", looper);
        this.mDefaultState = new DefaultState();
        this.mConnectingState = new ConnectingState();
        this.mConnectedState = new ConnectedState();
        this.mHandoverState = new HandoverState();
        this.mDisconnectingState = new DisconnectingState();
        this.mDisconnectedState = new DisconnectedState();
        this.mInvokedDataDeactivation = false;
        this.mEverConnected = false;
        this.mLocalLog = new LocalLog(128);
        this.mCid = new SparseIntArray(2);
        this.mPduSessionId = 0;
        this.mPhysicalNetworkTransportType = -1;
        this.mPhysicalNetworkSlotIndex = -1;
        this.mLastNotifiedAccessNetwork = 0;
        ArrayList arrayList = new ArrayList();
        this.mTrafficDescriptors = arrayList;
        this.mLinkStatus = -1;
        this.mNetworkBandwidth = new NetworkBandwidth(14, 14);
        this.mTempNotMeteredSupported = false;
        this.mTempNotMetered = false;
        this.mCongested = false;
        this.mAttachedNetworkRequestList = new DataNetworkController.NetworkRequestList();
        this.mDataCallResponse = null;
        this.mFailCause = 0;
        this.mTearDownReason = 0;
        this.mRetryDelayMillis = -1L;
        this.mSuspended = false;
        this.mPcoData = new ArrayMap();
        this.mQosBearerSessions = new ArrayList();
        this.mAdministratorUids = new int[0];
        this.mCarrierServicePackageUid = -1;
        this.mNetworkValidationStatus = 0;
        this.mIsCidReuseTeardown = false;
        initializeStateMachine();
        this.mPhone = phone;
        this.mFlags = featureFlags;
        this.mSubId = phone.getSubId();
        this.mRil = phone.mCi;
        this.mLinkProperties = new LinkProperties();
        this.mDataServiceManagers = sparseArray;
        AccessNetworksManager accessNetworksManager = phone.getAccessNetworksManager();
        this.mAccessNetworksManager = accessNetworksManager;
        this.mVcnManager = (VcnManager) phone.getContext().getSystemService(VcnManager.class);
        DataNetworkController dataNetworkController = phone.getDataNetworkController();
        this.mDataNetworkController = dataNetworkController;
        PhoneSwitcher phoneSwitcher = PhoneSwitcher.getInstance();
        this.mPhoneSwitcher = phoneSwitcher;
        this.mOnPreferredDataPhone = phone.getPhoneId() == phoneSwitcher.getPreferredDataPhoneId();
        Handler handler = getHandler();
        Objects.requireNonNull(handler);
        DataNetworkController.DataNetworkControllerCallback dataNetworkControllerCallback = new DataNetworkController.DataNetworkControllerCallback(new NetworkTypeController$$ExternalSyntheticLambda2(handler)) { // from class: com.android.internal.telephony.data.DataNetwork.1
            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onSubscriptionPlanOverride() {
                DataNetwork.this.sendMessage(16);
            }
        };
        this.mDataNetworkControllerCallback = dataNetworkControllerCallback;
        dataNetworkController.registerDataNetworkControllerCallback(dataNetworkControllerCallback);
        this.mDataConfigManager = dataNetworkController.getDataConfigManager();
        this.mDataCallSessionStats = new DataCallSessionStats(phone);
        this.mDataNetworkValidationStats = new DataNetworkValidationStats(phone);
        this.mDataNetworkCallback = dataNetworkCallback;
        this.mDataProfile = dataProfile;
        if (dataProfile.getTrafficDescriptor() != null) {
            arrayList.add(dataProfile.getTrafficDescriptor());
        }
        this.mTransport = i;
        this.mPhysicalNetworkTransportType = i;
        if (i == 1) {
            this.mPhysicalNetworkSlotIndex = phone.getPhoneId();
        }
        this.mLastKnownDataNetworkType = getDataNetworkType();
        this.mLastKnownRoamingState = phone.getServiceState().getDataRoamingFromRegistration();
        this.mSatellite = z;
        this.mDataAllowedReason = dataAllowedReason;
        dataNetworkController.getDataProfileManager().setDataProfileUsedTime(dataProfile, SystemClock.elapsedRealtime());
        for (int i2 : accessNetworksManager.getAvailableTransports()) {
            this.mCid.put(i2, -1);
        }
        TelephonyDisplayInfo telephonyDisplayInfo = this.mPhone.getDisplayInfoController().getTelephonyDisplayInfo();
        this.mTelephonyDisplayInfo = telephonyDisplayInfo;
        this.mTcpBufferSizes = this.mDataConfigManager.getTcpConfigString(telephonyDisplayInfo);
        this.mIHyperDataManager = (IHyperDataManager) HyperTelephonyFactory.getModuleFromCache(this.mPhone.getPhoneId(), IHyperDataManager.DEFAULT);
        this.mAttachedNetworkRequestList.addAll(networkRequestList);
        updateNetworkCapabilities();
        ListIterator<TelephonyNetworkRequest> listIterator = this.mAttachedNetworkRequestList.listIterator();
        while (listIterator.hasNext()) {
            TelephonyNetworkRequest next = listIterator.next();
            if (next.canBeSatisfiedBy(this.mNetworkCapabilities)) {
                next.setAttachedNetwork(this);
                next.setState(1);
            } else {
                listIterator.remove();
            }
        }
    }

    private void initializeStateMachine() {
        addState(this.mDefaultState);
        addState(this.mConnectingState, this.mDefaultState);
        addState(this.mConnectedState, this.mDefaultState);
        addState(this.mHandoverState, this.mDefaultState);
        addState(this.mDisconnectingState, this.mDefaultState);
        addState(this.mDisconnectedState, this.mDefaultState);
        setInitialState(this.mConnectingState);
        start();
    }

    private boolean shouldSkip464Xlat() {
        if (this.mDataProfile.getApnSetting() != null) {
            int skip464Xlat = this.mDataProfile.getApnSetting().getSkip464Xlat();
            if (skip464Xlat == 0) {
                return false;
            }
            if (skip464Xlat == 1) {
                return true;
            }
        }
        NetworkCapabilities networkCapabilities = getNetworkCapabilities();
        return networkCapabilities.hasCapability(4) && !networkCapabilities.hasCapability(12);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TelephonyNetworkAgent createNetworkAgent() {
        NetworkAgentConfig.Builder builder = new NetworkAgentConfig.Builder();
        builder.setLegacyType(0);
        builder.setLegacyTypeName("MOBILE");
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).createNetworkAgent(this, builder);
        int dataNetworkType = getDataNetworkType();
        builder.setLegacySubType(dataNetworkType);
        builder.setLegacySubTypeName(TelephonyManager.getNetworkTypeName(dataNetworkType));
        if (this.mDataProfile.getApnSetting() != null) {
            builder.setLegacyExtraInfo(this.mDataProfile.getApnSetting().getApnName());
        }
        if (this.mPhone.getCarrierSignalAgent().hasRegisteredReceivers("android.telephony.action.CARRIER_SIGNAL_REDIRECTED")) {
            builder.setProvisioningNotificationEnabled(false);
        }
        String subscriberId = this.mPhone.getSubscriberId();
        if (!TextUtils.isEmpty(subscriberId)) {
            builder.setSubscriberId(subscriberId);
        }
        if (shouldSkip464Xlat()) {
            builder.setNat64DetectionEnabled(false);
        }
        TelephonyNetworkProvider networkProvider = PhoneFactory.getNetworkProvider();
        NetworkScore.Builder keepConnectedReason = new NetworkScore.Builder().setKeepConnectedReason(isHandoverInProgress() ? 1 : 0);
        keepConnectedReason.setTransportPrimary(this.mOnPreferredDataPhone);
        this.mNetworkScore = keepConnectedReason.build();
        logl("mNetworkScore: isPrimary=" + this.mNetworkScore.isTransportPrimary() + ", keepConnectedReason=" + this.mNetworkScore.getKeepConnectedReason());
        TelephonyComponentFactory telephonyComponentFactoryInject = TelephonyComponentFactory.getInstance().inject(TelephonyNetworkAgent.class.getName());
        Phone phone = this.mPhone;
        Looper looper = getHandler().getLooper();
        NetworkScore networkScore = this.mNetworkScore;
        NetworkAgentConfig networkAgentConfigBuild = builder.build();
        Handler handler = getHandler();
        Objects.requireNonNull(handler);
        return telephonyComponentFactoryInject.makeTelephonyNetworkAgent(phone, looper, this, networkScore, networkAgentConfigBuild, networkProvider, new AnonymousClass2(new NetworkTypeController$$ExternalSyntheticLambda2(handler)));
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.data.DataNetwork$2, reason: invalid class name */
    class AnonymousClass2 extends TelephonyNetworkAgent.TelephonyNetworkAgentCallback {
        AnonymousClass2(Executor executor) {
            super(executor);
        }

        @Override // com.android.internal.telephony.data.TelephonyNetworkAgent.TelephonyNetworkAgentCallback
        public void onValidationStatus(final int i, final Uri uri) {
            if (DataNetwork.this.mDataProfile.getApnSetting() != null) {
                DataNetwork dataNetwork = DataNetwork.this;
                if (dataNetwork.mtkSkipDataStallAlarm(dataNetwork.mDataProfile.getApnSetting().getApnName())) {
                    DataNetwork.this.log("configured skip data stall.");
                    return;
                }
            }
            DataNetwork.this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onValidationStatus$0(i, uri);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onValidationStatus$0(int i, Uri uri) {
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.mDataNetworkCallback.onValidationStatusChanged(dataNetwork, i, uri);
        }
    }

    protected class DefaultState extends State {
        protected DefaultState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            DataNetwork.this.logv("Registering all events.");
            DataNetwork dataNetwork = DataNetwork.this;
            Handler handler = dataNetwork.getHandler();
            Objects.requireNonNull(handler);
            dataNetwork.mDataConfigManagerCallback = new DataConfigManager.DataConfigManagerCallback(new NetworkTypeController$$ExternalSyntheticLambda2(handler)) { // from class: com.android.internal.telephony.data.DataNetwork.DefaultState.1
                @Override // com.android.internal.telephony.data.DataConfigManager.DataConfigManagerCallback
                public void onCarrierConfigChanged() {
                    DataNetwork.this.sendMessage(1);
                }
            };
            DataNetwork.this.mRil.registerForPcoData(DataNetwork.this.getHandler(), 17, null);
            DataNetwork dataNetwork2 = DataNetwork.this;
            dataNetwork2.mDataConfigManager.registerCallback(dataNetwork2.mDataConfigManagerCallback);
            DataNetwork dataNetwork3 = DataNetwork.this;
            Handler handler2 = dataNetwork3.getHandler();
            Objects.requireNonNull(handler2);
            dataNetwork3.mDataSettingsManagerCallback = new DataSettingsManager.DataSettingsManagerCallback(new NetworkTypeController$$ExternalSyntheticLambda2(handler2)) { // from class: com.android.internal.telephony.data.DataNetwork.DefaultState.2
                @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
                public void onDataEnabledChanged(boolean z, int i, String str) {
                    if (z) {
                        DataNetwork.this.log("Data enabled. update network capabilities.");
                        DataNetwork.this.updateNetworkCapabilities();
                    }
                }

                @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
                public void onDataRoamingEnabledChanged(boolean z) {
                    if (z) {
                        DataNetwork.this.log("Data roaming enabled. update network capabilities.");
                        DataNetwork.this.updateNetworkCapabilities();
                    }
                }
            };
            DataNetwork.this.mDataNetworkController.getDataSettingsManager().registerCallback(DataNetwork.this.mDataSettingsManagerCallback);
            DataNetwork.this.mPhoneSwitcherCallback = new PhoneSwitcher.PhoneSwitcherCallback(new CarrierPrivilegesTracker$$ExternalSyntheticLambda0()) { // from class: com.android.internal.telephony.data.DataNetwork.DefaultState.3
                @Override // com.android.internal.telephony.data.PhoneSwitcher.PhoneSwitcherCallback
                public void onPreferredDataPhoneIdChanged(int i) {
                    DataNetwork.this.sendMessage(30, i, 0);
                }
            };
            DataNetwork.this.mPhoneSwitcher.registerCallback(DataNetwork.this.mPhoneSwitcherCallback);
            DataNetwork.this.mPhone.getDisplayInfoController().registerForTelephonyDisplayInfoChanged(DataNetwork.this.getHandler(), 13, null);
            DataNetwork.this.mPhone.getServiceStateTracker().registerForServiceStateChanged(DataNetwork.this.getHandler(), 9, null);
            for (int i : DataNetwork.this.mAccessNetworksManager.getAvailableTransports()) {
                ((DataServiceManager) DataNetwork.this.mDataServiceManagers.get(i)).registerForDataCallListChanged(DataNetwork.this.getHandler(), 8);
            }
            DataNetwork.this.mCarrierPrivilegesCallback = new TelephonyManager.CarrierPrivilegesCallback() { // from class: com.android.internal.telephony.data.DataNetwork$DefaultState$$ExternalSyntheticLambda0
                public final void onCarrierPrivilegesChanged(Set set, Set set2) {
                    this.f$0.lambda$enter$1(set, set2);
                }
            };
            TelephonyManager telephonyManager = (TelephonyManager) DataNetwork.this.mPhone.getContext().getSystemService(TelephonyManager.class);
            if (telephonyManager != null) {
                int phoneId = DataNetwork.this.mPhone.getPhoneId();
                Handler handler3 = DataNetwork.this.getHandler();
                Objects.requireNonNull(handler3);
                telephonyManager.registerCarrierPrivilegesCallback(phoneId, new NetworkTypeController$$ExternalSyntheticLambda2(handler3), DataNetwork.this.mCarrierPrivilegesCallback);
            }
            DataNetwork.this.mPhone.getServiceStateTracker().registerForCssIndicatorChanged(DataNetwork.this.getHandler(), 24, null);
            if (DataNetwork.this.mPhone.getCallTracker() != null) {
                DataNetwork.this.mPhone.getCallTracker().registerForVoiceCallStarted(DataNetwork.this.getHandler(), 22, null);
                DataNetwork.this.mPhone.getCallTracker().registerForVoiceCallEnded(DataNetwork.this.getHandler(), 23, null);
            }
            if (DataNetwork.this.mPhone.getImsPhone() != null && DataNetwork.this.mPhone.getImsPhone().getCallTracker() != null) {
                DataNetwork.this.mPhone.getImsPhone().getCallTracker().registerForVoiceCallStarted(DataNetwork.this.getHandler(), 22, null);
                DataNetwork.this.mPhone.getImsPhone().getCallTracker().registerForVoiceCallEnded(DataNetwork.this.getHandler(), 23, null);
            }
            if (DataNetwork.this.mDataProfile.canSatisfy(0) || DataNetwork.this.mDataProfile.canSatisfy(9)) {
                DataNetwork dataNetwork4 = DataNetwork.this;
                Handler handler4 = dataNetwork4.getHandler();
                Objects.requireNonNull(handler4);
                dataNetwork4.mAccessNetworksManagerCallback = new AccessNetworksManager.AccessNetworksManagerCallback(new NetworkTypeController$$ExternalSyntheticLambda2(handler4)) { // from class: com.android.internal.telephony.data.DataNetwork.DefaultState.4
                    @Override // com.android.internal.telephony.data.AccessNetworksManager.AccessNetworksManagerCallback
                    public void onPreferredTransportChanged(int i2, boolean z) {
                        if (i2 == 0 || i2 == 9) {
                            DataNetwork.this.log(DataUtils.networkCapabilityToString(i2) + " preference changed.");
                            DataNetwork.this.updateNetworkCapabilities();
                        }
                    }
                };
                DataNetwork.this.mAccessNetworksManager.registerCallback(DataNetwork.this.mAccessNetworksManagerCallback);
            }
            DataNetwork.this.mRil.registerForNotAvailable(DataNetwork.this.getHandler(), 4, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$enter$1(Set set, Set set2) {
            DataNetwork.this.log("onCarrierPrivilegesChanged, Uids=" + set2);
            Message messageObtainMessage = DataNetwork.this.obtainMessage(18);
            AsyncResult.forMessage(messageObtainMessage, set2.stream().mapToInt(new ToIntFunction() { // from class: com.android.internal.telephony.data.DataNetwork$DefaultState$$ExternalSyntheticLambda1
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Integer) obj).intValue();
                }
            }).toArray(), (Throwable) null);
            DataNetwork.this.sendMessage(messageObtainMessage);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            DataNetwork.this.logv("Unregistering all events.");
            if (DataNetwork.this.mAccessNetworksManagerCallback != null) {
                DataNetwork.this.mAccessNetworksManager.unregisterCallback(DataNetwork.this.mAccessNetworksManagerCallback);
            }
            if (DataNetwork.this.mPhone.getImsPhone() != null && DataNetwork.this.mPhone.getImsPhone().getCallTracker() != null) {
                DataNetwork.this.mPhone.getImsPhone().getCallTracker().unregisterForVoiceCallStarted(DataNetwork.this.getHandler());
                DataNetwork.this.mPhone.getImsPhone().getCallTracker().unregisterForVoiceCallEnded(DataNetwork.this.getHandler());
            }
            if (DataNetwork.this.mPhone.getCallTracker() != null) {
                DataNetwork.this.mPhone.getCallTracker().unregisterForVoiceCallStarted(DataNetwork.this.getHandler());
                DataNetwork.this.mPhone.getCallTracker().unregisterForVoiceCallEnded(DataNetwork.this.getHandler());
            }
            DataNetwork.this.mPhone.getServiceStateTracker().unregisterForCssIndicatorChanged(DataNetwork.this.getHandler());
            TelephonyManager telephonyManager = (TelephonyManager) DataNetwork.this.mPhone.getContext().getSystemService(TelephonyManager.class);
            if (telephonyManager != null && DataNetwork.this.mCarrierPrivilegesCallback != null) {
                telephonyManager.unregisterCarrierPrivilegesCallback(DataNetwork.this.mCarrierPrivilegesCallback);
            }
            for (int i : DataNetwork.this.mAccessNetworksManager.getAvailableTransports()) {
                ((DataServiceManager) DataNetwork.this.mDataServiceManagers.get(i)).unregisterForDataCallListChanged(DataNetwork.this.getHandler());
            }
            DataNetwork.this.mPhone.getServiceStateTracker().unregisterForServiceStateChanged(DataNetwork.this.getHandler());
            DataNetwork.this.mPhone.getDisplayInfoController().unregisterForTelephonyDisplayInfoChanged(DataNetwork.this.getHandler());
            DataNetwork.this.mPhoneSwitcher.unregisterCallback(DataNetwork.this.mPhoneSwitcherCallback);
            DataNetwork.this.mDataNetworkController.getDataSettingsManager().unregisterCallback(DataNetwork.this.mDataSettingsManagerCallback);
            DataNetwork.this.mRil.unregisterForPcoData(DataNetwork.this.getHandler());
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.mDataConfigManager.unregisterCallback(dataNetwork.mDataConfigManagerCallback);
            DataNetwork.this.mRil.unregisterForNotAvailable(DataNetwork.this.getHandler());
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            boolean z = false;
            switch (message.what) {
                case 1:
                    DataNetwork.this.onCarrierConfigUpdated();
                    return true;
                case 2:
                    DataNetwork.this.onAttachNetworkRequests((DataNetworkController.NetworkRequestList) message.obj);
                    DataNetwork.this.updateNetworkScore();
                    return true;
                case 3:
                    DataNetwork.this.onDetachNetworkRequest((TelephonyNetworkRequest) message.obj, message.arg1 != 0);
                    DataNetwork.this.updateNetworkScore();
                    return true;
                case 4:
                    DataNetwork dataNetwork = DataNetwork.this;
                    if (dataNetwork.mTransport == 2) {
                        dataNetwork.loge(DataNetwork.eventToString(message.what) + ": ignored on IWLAN");
                    } else {
                        dataNetwork.mFailCause = 65537;
                        DataNetwork.this.loge(DataNetwork.eventToString(message.what) + ": transition to disconnected state");
                        DataNetwork dataNetwork2 = DataNetwork.this;
                        dataNetwork2.transitionTo(dataNetwork2.mDisconnectedState);
                    }
                    return true;
                case 5:
                case 6:
                case 14:
                case 15:
                case 16:
                case 19:
                default:
                    DataNetwork.this.loge("Unhandled event " + DataNetwork.eventToString(message.what));
                    return true;
                case 7:
                case 11:
                case 12:
                case 13:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                    DataNetwork.this.log("Ignored " + DataNetwork.eventToString(message.what));
                    return true;
                case 8:
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    int iIntValue = ((Integer) asyncResult.userObj).intValue();
                    Pair pair = (Pair) asyncResult.result;
                    DataNetwork.this.onDataStateChanged(iIntValue, (List) pair.first, ((Boolean) pair.second).booleanValue());
                    return true;
                case 9:
                    DataNetwork.this.log("Service state changed. " + DataNetwork.this.getNetworkRegistrationInfo());
                    int dataNetworkType = DataNetwork.this.getDataNetworkType();
                    DataNetwork.this.mDataCallSessionStats.onDrsOrRatChanged(dataNetworkType);
                    if (dataNetworkType != 0) {
                        if (dataNetworkType != DataNetwork.this.mLastKnownDataNetworkType) {
                            DataNetwork.this.updateLingerAndTearDownDelayTimers(dataNetworkType);
                        }
                        DataNetwork.this.mLastKnownDataNetworkType = dataNetworkType;
                    }
                    NetworkRegistrationInfo networkRegistrationInfo = DataNetwork.this.mPhone.getServiceState().getNetworkRegistrationInfo(2, 1);
                    if (networkRegistrationInfo != null && networkRegistrationInfo.isInService()) {
                        DataNetwork.this.mLastKnownRoamingState = networkRegistrationInfo.getNetworkRegistrationState() == 5;
                    }
                    if (networkRegistrationInfo != null && (networkRegistrationInfo.isInService() || networkRegistrationInfo.getRegistrationState() == 6)) {
                        if (DataNetwork.this.mTransport == 1 && networkRegistrationInfo.isNonTerrestrialNetwork()) {
                            z = true;
                        }
                        if (DataNetwork.this.mSatellite != z) {
                            DataNetwork.this.mSatellite = z;
                            DataNetwork dataNetwork3 = DataNetwork.this;
                            StringBuilder sb = new StringBuilder("Switched to ");
                            sb.append(DataNetwork.this.mSatellite ? "Satellite" : "Cellular");
                            sb.append(".");
                            dataNetwork3.logl(sb.toString());
                        }
                    }
                    DataNetwork.this.updateSuspendState();
                    DataNetwork.this.updateNetworkCapabilities();
                    int iNetworkTypeToAccessNetworkType = DataUtils.networkTypeToAccessNetworkType(dataNetworkType);
                    if (iNetworkTypeToAccessNetworkType != 0 && DataNetwork.this.mLastNotifiedAccessNetwork != iNetworkTypeToAccessNetworkType) {
                        DataNetwork.this.notifyImsDataNetwork();
                    }
                    return true;
                case 10:
                    for (TelephonyNetworkRequest telephonyNetworkRequest : DataNetwork.this.mAttachedNetworkRequestList) {
                        if (telephonyNetworkRequest.getAttachedNetwork() == DataNetwork.this) {
                            telephonyNetworkRequest.setState(0);
                            telephonyNetworkRequest.setAttachedNetwork(null);
                        }
                    }
                    DataNetwork.this.log("All network requests detached.");
                    DataNetwork.this.mAttachedNetworkRequestList.clear();
                    return true;
                case 17:
                    DataNetwork.this.onPcoDataReceived((PcoData) ((AsyncResult) message.obj).result);
                    return true;
                case 18:
                    int[] iArr = (int[]) ((AsyncResult) message.obj).result;
                    DataNetwork.this.mAdministratorUids = Arrays.copyOf(iArr, iArr.length);
                    DataNetwork.this.updateNetworkCapabilities();
                    return true;
                case 25:
                case 26:
                    Object obj = message.obj;
                    if (obj != null) {
                        ((DataRetryManager.DataHandoverRetryEntry) obj).setState(4);
                    }
                    DataNetwork.this.log("Ignore handover to " + AccessNetworkConstants.transportTypeToString(message.arg1) + " request.");
                    return true;
                case 27:
                    DataNetwork.this.removeMessages(12);
                    DataNetwork.this.log("Notified handover cancelled.");
                    return true;
                case 28:
                    DataNetwork.this.handleErrorDataNetworkValidationRequest((Consumer) message.obj);
                    return true;
                case 29:
                    DataNetwork.this.handleDataNetworkValidationRequestResultCode(message.arg1);
                    return true;
                case 30:
                    DataNetwork dataNetwork4 = DataNetwork.this;
                    dataNetwork4.mOnPreferredDataPhone = dataNetwork4.mPhone.getPhoneId() == message.arg1;
                    DataNetwork.this.logl("Preferred data phone id changed to " + message.arg1 + ", mOnPreferredDataPhone=" + DataNetwork.this.mOnPreferredDataPhone);
                    DataNetwork.this.updateNetworkScore();
                    DataNetwork dataNetwork5 = DataNetwork.this;
                    dataNetwork5.updateLingerAndTearDownDelayTimers(dataNetwork5.mLastKnownDataNetworkType);
                    return true;
            }
        }
    }

    protected class ConnectingState extends State {
        private int mRegStateWhenSetup;

        protected ConnectingState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.sendMessageDelayed(20, dataNetwork.mDataConfigManager.getAnomalyNetworkConnectingTimeoutMs());
            DataNetwork dataNetwork2 = DataNetwork.this;
            dataNetwork2.mNetworkAgent = dataNetwork2.createNetworkAgent();
            DataNetwork dataNetwork3 = DataNetwork.this;
            dataNetwork3.mInitialNetworkAgentId = dataNetwork3.mNetworkAgent.getId();
            DataNetwork dataNetwork4 = DataNetwork.this;
            StringBuilder sb = new StringBuilder("DN-");
            sb.append(DataNetwork.this.mInitialNetworkAgentId);
            sb.append("-");
            sb.append(DataNetwork.this.mTransport == 1 ? "C" : "I");
            dataNetwork4.mLogTag = sb.toString();
            DataNetwork dataNetwork5 = DataNetwork.this;
            dataNetwork5.mCarrierServicePackageUid = dataNetwork5.mPhone.getCarrierPrivilegesTracker().getCarrierServicePackageUid();
            DataNetwork.this.notifyPreciseDataConnectionState();
            DataNetwork.this.notifyImsDataNetwork();
            DataNetwork dataNetwork6 = DataNetwork.this;
            dataNetwork6.updateLingerAndTearDownDelayTimers(dataNetwork6.mLastKnownDataNetworkType);
            DataNetwork dataNetwork7 = DataNetwork.this;
            if (dataNetwork7.mTransport == 2) {
                dataNetwork7.allocatePduSessionId();
            } else {
                setupData();
            }
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            DataNetwork.this.removeMessages(20);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            DataNetwork.this.logv("event=" + DataNetwork.eventToString(message.what));
            int i = message.what;
            if (i == 5) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                Throwable th = asyncResult.exception;
                DataNetwork dataNetwork = DataNetwork.this;
                if (th == null) {
                    dataNetwork.mPduSessionId = ((Integer) asyncResult.result).intValue();
                    DataNetwork.this.log("Set PDU session id to " + DataNetwork.this.mPduSessionId);
                } else {
                    dataNetwork.loge("Failed to allocate PDU session id. e=" + asyncResult.exception);
                }
                if (!DataNetwork.this.mAttachedNetworkRequestList.isEmpty()) {
                    setupData();
                    return true;
                }
                DataNetwork.this.mRetryDelayMillis = -1L;
                DataNetwork dataNetwork2 = DataNetwork.this;
                dataNetwork2.transitionTo(dataNetwork2.mDisconnectedState);
                return true;
            }
            if (i == 6) {
                onSetupResponse(message.arg1, (DataCallResponse) message.getData().getParcelable("data_call_response"));
                return true;
            }
            if (i != 7 && i != 25) {
                switch (i) {
                    case 19:
                        DataNetwork.this.onDeactivateResponse(message.arg1);
                        return true;
                    case 20:
                        DataNetwork.this.reportAnomaly("Data network stuck in connecting state for " + (((long) DataNetwork.this.mDataConfigManager.getAnomalyNetworkConnectingTimeoutMs()) / 1000) + " seconds.", "58c56403-7ea7-4e56-a0c7-e467114d09b8");
                        DataNetwork.this.mRetryDelayMillis = -1L;
                        DataNetwork.this.mFailCause = 65547;
                        DataNetwork dataNetwork3 = DataNetwork.this;
                        dataNetwork3.transitionTo(dataNetwork3.mDisconnectedState);
                        return true;
                    case 21:
                        break;
                    default:
                        return false;
                }
            }
            DataNetwork.this.log("Defer message " + DataNetwork.eventToString(message.what));
            DataNetwork.this.deferMessage(message);
            return true;
        }

        private void setupData() {
            int dataNetworkType = DataNetwork.this.getDataNetworkType();
            NetworkRegistrationInfo networkRegistrationInfo = DataNetwork.this.getNetworkRegistrationInfo();
            this.mRegStateWhenSetup = networkRegistrationInfo != null ? networkRegistrationInfo.getNetworkRegistrationState() : 4;
            ServiceState serviceState = DataNetwork.this.mPhone.getServiceState();
            boolean dataRoamingFromRegistration = serviceState.getDataRoamingFromRegistration();
            boolean zIsAllowedOnRoaming = MiuiDataControllerStub.get(DataNetwork.this.mPhone.getPhoneId()).isAllowedOnRoaming(DataNetwork.this.mPhone, DataNetwork.this.mPhone.getDataRoamingEnabled() || (dataRoamingFromRegistration && (!serviceState.getDataRoaming() || (serviceState.getDataRoaming() && !DataNetwork.this.mPhone.getDataRoamingEnabled() && serviceState.isUsingNonTerrestrialNetwork() && DataNetwork.this.mDataConfigManager.isDataRoamingAllowedOnSatellite() && DataNetwork.this.mSatellite))));
            TrafficDescriptor trafficDescriptor = DataNetwork.this.mDataProfile.getTrafficDescriptor();
            boolean z = trafficDescriptor == null || !TextUtils.isEmpty(trafficDescriptor.getDataNetworkName()) || (TextUtils.isEmpty(trafficDescriptor.getDataNetworkName()) && trafficDescriptor.getOsAppId() == null);
            int iNetworkTypeToAccessNetworkType = DataUtils.networkTypeToAccessNetworkType(dataNetworkType);
            DataNetwork dataNetwork = DataNetwork.this;
            DataProfile dataProfileMtkReplaceDataProfileIfNeeded = dataNetwork.mtkReplaceDataProfileIfNeeded(dataNetwork.mDataProfile);
            IHyperDataManager iHyperDataManager = DataNetwork.this.mIHyperDataManager;
            DataNetwork dataNetwork2 = DataNetwork.this;
            ((DataServiceManager) DataNetwork.this.mDataServiceManagers.get(DataNetwork.this.mTransport)).setupDataCall(iNetworkTypeToAccessNetworkType, dataProfileMtkReplaceDataProfileIfNeeded, dataRoamingFromRegistration, zIsAllowedOnRoaming, iHyperDataManager.getRequestReason(1, dataNetwork2.mDataProfile, dataNetwork2.mAttachedNetworkRequestList), null, DataNetwork.this.mPduSessionId, null, trafficDescriptor, z, DataNetwork.this.obtainMessage(6));
            int apnTypeBitmask = DataNetwork.this.mDataProfile.getApnSetting() != null ? DataNetwork.this.mDataProfile.getApnSetting().getApnTypeBitmask() : 0;
            int sliceCapability = getSliceCapability(DataNetwork.this.getNetworkCapabilities());
            int connectionCapability = trafficDescriptor != null ? trafficDescriptor.getConnectionCapability() : 0;
            DataNetwork dataNetwork3 = DataNetwork.this;
            dataNetwork3.mDataCallSessionStats.onSetupDataCall(apnTypeBitmask, dataNetwork3.mSatellite, sliceCapability, connectionCapability);
            DataNetwork.this.logl("setupData: accessNetwork=" + AccessNetworkConstants.AccessNetworkType.toString(iNetworkTypeToAccessNetworkType) + ", isSatellite=" + DataNetwork.this.mSatellite + ", " + DataNetwork.this.mDataProfile + ", isModemRoaming=" + dataRoamingFromRegistration + ", allowRoaming=" + zIsAllowedOnRoaming + ", PDU session id=" + DataNetwork.this.mPduSessionId + ", matchAllRuleAllowed=" + z);
        }

        private int getSliceCapability(NetworkCapabilities networkCapabilities) {
            if (networkCapabilities == null) {
                return 0;
            }
            if (networkCapabilities.hasCapability(38)) {
                return 38;
            }
            if (networkCapabilities.hasCapability(34)) {
                return 34;
            }
            return networkCapabilities.hasCapability(35) ? 35 : 0;
        }

        private void onSetupResponse(int i, DataCallResponse dataCallResponse) {
            int apnTypeBitmask;
            DataNetwork.this.logl("onSetupResponse: resultCode=" + DataServiceCallback.resultCodeToString(i) + ", response=" + dataCallResponse);
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.mFailCause = dataNetwork.getFailCauseFromDataCallResponse(i, dataCallResponse);
            DataNetwork.this.validateDataCallResponse(dataCallResponse, this.mRegStateWhenSetup);
            int i2 = DataNetwork.this.mFailCause;
            DataNetwork dataNetwork2 = DataNetwork.this;
            int protocol = -1;
            if (i2 == 0) {
                DataNetwork dataNetworkByInterface = dataNetwork2.mDataNetworkController.getDataNetworkByInterface(dataCallResponse.getInterfaceName());
                DataNetwork dataNetwork3 = DataNetwork.this;
                if (dataNetworkByInterface != null) {
                    dataNetwork3.logl("Interface " + dataCallResponse.getInterfaceName() + " has been already used by " + dataNetworkByInterface + ". Silently tear down now.");
                    if (dataCallResponse.getTrafficDescriptors().isEmpty() && dataNetworkByInterface.isConnected()) {
                        DataNetwork.this.reportAnomaly("Duplicate network interface " + dataCallResponse.getInterfaceName() + " detected.", "62f66e7e-8d71-45de-a57b-dc5c78223fd5");
                    }
                    FeatureFlags unused = DataNetwork.this.mFlags;
                    if (dataNetworkByInterface.getId() == dataCallResponse.getId()) {
                        DataNetwork.this.mIsCidReuseTeardown = true;
                    }
                    DataNetwork.this.mFailCause = 65547;
                    DataNetwork.this.mRetryDelayMillis = -1L;
                    DataNetwork dataNetwork4 = DataNetwork.this;
                    dataNetwork4.transitionTo(dataNetwork4.mDisconnectedState);
                    return;
                }
                MiuiDataControllerStub.get(dataNetwork3.mPhone.getPhoneId()).onSetupResponse(DataNetwork.this, i, dataCallResponse);
                DataNetwork.this.updateDataNetwork(dataCallResponse);
                if (DataNetwork.this.mVcnManager != null) {
                    VcnManager vcnManager = DataNetwork.this.mVcnManager;
                    DataNetwork dataNetwork5 = DataNetwork.this;
                    if (vcnManager.applyVcnNetworkPolicy(dataNetwork5.mNetworkCapabilities, dataNetwork5.mLinkProperties).isTeardownRequested()) {
                        DataNetwork.this.log("VCN service requested to tear down the network.");
                        DataNetwork.this.onTearDown(15);
                        return;
                    }
                }
                DataNetwork dataNetwork6 = DataNetwork.this;
                dataNetwork6.transitionTo(dataNetwork6.mConnectedState);
            } else {
                FeatureFlags unused2 = dataNetwork2.mFlags;
                if (DataNetwork.this.mFailCause == 65543) {
                    int id = dataCallResponse == null ? -1 : dataCallResponse.getId();
                    DataNetwork dataNetworkByCid = id == -1 ? null : DataNetwork.this.mDataNetworkController.getDataNetworkByCid(id);
                    if (dataNetworkByCid != null && TextUtils.equals(dataNetworkByCid.getLinkProperties().getInterfaceName(), dataCallResponse.getInterfaceName())) {
                        DataNetwork.this.logl("onSetupResponse: Intentional CID reuse for cid=" + id + " on existing network " + dataNetworkByCid.getName());
                        DataNetwork.this.mIsCidReuseTeardown = true;
                        DataNetwork.this.mFailCause = 65547;
                        DataNetwork.this.mRetryDelayMillis = -1L;
                        DataNetwork dataNetwork7 = DataNetwork.this;
                        dataNetwork7.transitionTo(dataNetwork7.mDisconnectedState);
                        return;
                    }
                } else {
                    DataNetwork.this.mRetryDelayMillis = dataCallResponse != null ? dataCallResponse.getRetryDurationMillis() : -1L;
                    DataNetwork dataNetwork8 = DataNetwork.this;
                    dataNetwork8.transitionTo(dataNetwork8.mDisconnectedState);
                }
            }
            if (DataNetwork.this.mDataProfile.getApnSetting() != null) {
                apnTypeBitmask = DataNetwork.this.mDataProfile.getApnSetting().getApnTypeBitmask();
                protocol = DataNetwork.this.mDataProfile.getApnSetting().getProtocol();
            } else {
                apnTypeBitmask = 0;
            }
            int i3 = apnTypeBitmask;
            int i4 = protocol;
            DataNetwork dataNetwork9 = DataNetwork.this;
            dataNetwork9.mDataCallSessionStats.onSetupDataCallResponse(dataCallResponse, dataNetwork9.getDataNetworkType(), i3, i4, dataCallResponse != null ? dataCallResponse.getCause() : DataNetwork.this.mFailCause);
        }
    }

    protected class ConnectedState extends State {
        protected ConnectedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            if (!DataNetwork.this.mEverConnected) {
                DataNetwork.this.log("network connected.");
                DataNetwork.this.mEverConnected = true;
                DataNetwork.this.mNetworkAgent.markConnected();
                if (TelephonyBaseUtilsStub.isAutoDevice()) {
                    MiuiDataControllerStub.IMiuiDataController iMiuiDataController = MiuiDataControllerStub.get(DataNetwork.this.mPhone.getPhoneId());
                    DataNetwork dataNetwork = DataNetwork.this;
                    iMiuiDataController.updateNetworkAgent(dataNetwork.mPhone, dataNetwork.mDataProfile, dataNetwork.mNetworkAgent);
                }
                DataNetwork.this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$ConnectedState$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$enter$0();
                    }
                });
                DataNetwork dataNetwork2 = DataNetwork.this;
                DataNetwork dataNetwork3 = DataNetwork.this;
                dataNetwork2.mQosCallbackTracker = new QosCallbackTracker(dataNetwork3.mNetworkAgent, dataNetwork3.mPhone);
                DataNetwork.this.mQosCallbackTracker.updateSessions(DataNetwork.this.mQosBearerSessions);
                DataNetwork dataNetwork4 = DataNetwork.this;
                DataNetwork dataNetwork5 = DataNetwork.this;
                Phone phone = dataNetwork5.mPhone;
                Looper looper = dataNetwork5.getHandler().getLooper();
                DataNetwork dataNetwork6 = DataNetwork.this;
                dataNetwork4.mKeepaliveTracker = new KeepaliveTracker(phone, looper, dataNetwork6, dataNetwork6.mNetworkAgent);
                DataNetwork dataNetwork7 = DataNetwork.this;
                if (dataNetwork7.mTransport == 1) {
                    dataNetwork7.registerForWwanEvents();
                }
                if (DataNetwork.this.mVcnManager != null) {
                    DataNetwork.this.mVcnPolicyChangeListener = new VcnManager.VcnNetworkPolicyChangeListener() { // from class: com.android.internal.telephony.data.DataNetwork$ConnectedState$$ExternalSyntheticLambda1
                        public final void onPolicyChanged() {
                            this.f$0.lambda$enter$1();
                        }
                    };
                    VcnManager vcnManager = DataNetwork.this.mVcnManager;
                    Handler handler = DataNetwork.this.getHandler();
                    Objects.requireNonNull(handler);
                    vcnManager.addVcnNetworkPolicyChangeListener(new NetworkTypeController$$ExternalSyntheticLambda2(handler), DataNetwork.this.mVcnPolicyChangeListener);
                }
            }
            Map map = DataNetwork.this.mPcoData;
            DataNetwork dataNetwork8 = DataNetwork.this;
            ((Map) map.getOrDefault(Integer.valueOf(dataNetwork8.mCid.get(dataNetwork8.mTransport)), Collections.EMPTY_MAP)).forEach(new BiConsumer() { // from class: com.android.internal.telephony.data.DataNetwork$ConnectedState$$ExternalSyntheticLambda2
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    this.f$0.lambda$enter$2((Integer) obj, (PcoData) obj2);
                }
            });
            DataNetwork.this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$ConnectedState$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$enter$3();
                }
            });
            DataNetwork.this.notifyPreciseDataConnectionState();
            DataNetwork.this.updateSuspendState();
            DataNetwork.this.enableVolteIotFirewall(true);
            DataNetwork.this.notifyImsDataNetwork();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$enter$0() {
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.mDataNetworkCallback.onConnected(dataNetwork);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$enter$1() {
            DataNetwork.this.log("VCN policy changed.");
            VcnManager vcnManager = DataNetwork.this.mVcnManager;
            DataNetwork dataNetwork = DataNetwork.this;
            boolean zIsTeardownRequested = vcnManager.applyVcnNetworkPolicy(dataNetwork.mNetworkCapabilities, dataNetwork.mLinkProperties).isTeardownRequested();
            DataNetwork dataNetwork2 = DataNetwork.this;
            if (zIsTeardownRequested) {
                dataNetwork2.lambda$tearDownWhenConditionMet$11(15);
            } else {
                dataNetwork2.updateNetworkCapabilities();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$enter$2(Integer num, PcoData pcoData) {
            DataNetwork.this.onPcoDataChanged(pcoData);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$enter$3() {
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.mDataNetworkCallback.onLinkStatusChanged(dataNetwork, dataNetwork.mLinkStatus);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            DataNetwork.this.enableVolteIotFirewall(false);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            DataNetwork.this.logv("event=" + DataNetwork.eventToString(message.what));
            int i = message.what;
            if (i != 7) {
                if (i != 16) {
                    if (i != 19) {
                        if (i != 28) {
                            if (i != 100) {
                                switch (i) {
                                    case 11:
                                        AsyncResult asyncResult = (AsyncResult) message.obj;
                                        Throwable th = asyncResult.exception;
                                        DataNetwork dataNetwork = DataNetwork.this;
                                        if (th != null) {
                                            dataNetwork.log("EVENT_BANDWIDTH_ESTIMATE_FROM_MODEM_CHANGED: error ignoring, e=" + asyncResult.exception);
                                        } else {
                                            dataNetwork.onBandwidthUpdatedFromModem((List) asyncResult.result);
                                        }
                                        break;
                                    case 12:
                                        DataNetwork.this.reportAnomaly("Cancel handover no response within " + (((long) DataNetwork.this.mDataConfigManager.getNetworkHandoverTimeoutMs()) / 1000) + " seconds.", "ad320988-0601-4955-836a-e6b67289c294");
                                        break;
                                    case 13:
                                        DataNetwork.this.onDisplayInfoChanged();
                                        break;
                                    default:
                                        switch (i) {
                                            case 21:
                                                DataNetwork dataNetwork2 = DataNetwork.this;
                                                dataNetwork2.transitionTo(dataNetwork2.mDisconnectingState);
                                                DataNetwork.this.sendMessageDelayed(7, message.arg1, message.arg2);
                                                break;
                                            case 22:
                                            case 23:
                                            case 24:
                                                DataNetwork.this.updateSuspendState();
                                                DataNetwork.this.updateNetworkCapabilities();
                                                break;
                                            case 25:
                                                DataNetwork.this.log("Notifying source transport " + AccessNetworkConstants.transportTypeToString(DataNetwork.this.mTransport) + " that handover is about to start.");
                                                DataServiceManager dataServiceManager = (DataServiceManager) DataNetwork.this.mDataServiceManagers.get(DataNetwork.this.mTransport);
                                                DataNetwork dataNetwork3 = DataNetwork.this;
                                                dataServiceManager.startHandover(dataNetwork3.mCid.get(dataNetwork3.mTransport), DataNetwork.this.obtainMessage(26, 0, message.arg2, message.obj));
                                                DataNetwork dataNetwork4 = DataNetwork.this;
                                                dataNetwork4.transitionTo(dataNetwork4.mHandoverState);
                                                break;
                                            default:
                                                return DataNetwork.this.handleEventsOfConnectedState(i);
                                        }
                                        break;
                                }
                            }
                        } else {
                            DataNetwork.this.handleDataNetworkValidationRequest((Consumer) message.obj);
                        }
                    } else {
                        DataNetwork.this.onDeactivateResponse(message.arg1);
                    }
                } else {
                    DataNetwork.this.updateMeteredAndCongested();
                }
            } else {
                DataNetwork dataNetwork5 = DataNetwork.this;
                if (dataNetwork5.mInvokedDataDeactivation) {
                    dataNetwork5.log("Ignore tear down request because network is being torn down.");
                } else {
                    int i2 = message.arg1;
                    dataNetwork5.removeMessages(7);
                    DataNetwork.this.removeDeferredMessages(7);
                    DataNetwork dataNetwork6 = DataNetwork.this;
                    dataNetwork6.transitionTo(dataNetwork6.mDisconnectingState);
                    DataNetwork.this.onTearDown(i2);
                }
            }
            return true;
        }
    }

    protected class HandoverState extends State {
        protected HandoverState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            DataNetwork.this.removeMessages(12);
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.sendMessageDelayed(20, dataNetwork.mDataConfigManager.getNetworkHandoverTimeoutMs());
            DataNetwork.this.notifyPreciseDataConnectionState();
            DataNetwork.this.updateNetworkScore(true);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            DataNetwork.this.removeMessages(20);
            DataNetwork.this.updateNetworkScore(false);
        }

        /* JADX WARN: Code duplicated, block: B:26:0x0107  */
        /* JADX WARN: Instruction removed from duplicated block: B:26:0x0107, please report this as an issue */
        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            DataNetwork.this.logv("event=" + DataNetwork.eventToString(message.what));
            int i = message.what;
            if (i == 4) {
                DataNetwork dataNetwork = DataNetwork.this;
                if (dataNetwork.mTransport == 1) {
                    dataNetwork.log("Defer message " + DataNetwork.eventToString(message.what));
                    DataNetwork.this.deferMessage(message);
                } else {
                    dataNetwork.mFailCause = 65537;
                    DataNetwork.this.loge(DataNetwork.eventToString(message.what) + ": transition to disconnected state");
                    DataNetwork dataNetwork2 = DataNetwork.this;
                    dataNetwork2.transitionTo(dataNetwork2.mDisconnectedState);
                }
            } else if (i == 13) {
                DataNetwork.this.log("Defer message " + DataNetwork.eventToString(message.what));
                DataNetwork.this.deferMessage(message);
            } else if (i == 15) {
                DataNetwork.this.onHandoverResponse(message.arg1, message.getData().getParcelable("data_call_response"), (DataRetryManager.DataHandoverRetryEntry) message.obj);
            } else if (i == 26) {
                DataNetwork.this.onStartHandover(message.arg2, (DataRetryManager.DataHandoverRetryEntry) message.obj);
            } else if (i == 28 || i == 7) {
                DataNetwork.this.log("Defer message " + DataNetwork.eventToString(message.what));
                DataNetwork.this.deferMessage(message);
            } else if (i == 8) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                int iIntValue = ((Integer) asyncResult.userObj).intValue();
                List list = (List) ((Pair) asyncResult.result).first;
                DataNetwork dataNetwork3 = DataNetwork.this;
                if (iIntValue != dataNetwork3.mTransport) {
                    dataNetwork3.log("Dropped unrelated " + AccessNetworkConstants.transportTypeToString(iIntValue) + " data call list changed event. " + list);
                } else {
                    dataNetwork3.log("Defer message " + DataNetwork.eventToString(message.what) + ":" + list);
                    DataNetwork.this.deferMessage(message);
                }
            } else {
                switch (i) {
                    case 20:
                        DataNetwork.this.reportAnomaly("Data service did not respond the handover request within " + (((long) DataNetwork.this.mDataConfigManager.getNetworkHandoverTimeoutMs()) / 1000) + " seconds.", "1afe68cb-8b41-4964-a737-4f34372429ea");
                        DataNetwork.this.mFailCause = 65535;
                        final long j = -1;
                        final int i2 = 3;
                        DataNetwork.this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$HandoverState$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$processMessage$0(j, i2);
                            }
                        });
                        DataNetwork dataNetwork4 = DataNetwork.this;
                        dataNetwork4.transitionTo(dataNetwork4.mConnectedState);
                        break;
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                        DataNetwork.this.log("Defer message " + DataNetwork.eventToString(message.what));
                        DataNetwork.this.deferMessage(message);
                        break;
                    default:
                        return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processMessage$0(long j, int i) {
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.mDataNetworkCallback.onHandoverFailed(dataNetwork, dataNetwork.mFailCause, j, i);
        }
    }

    private final class DisconnectingState extends State {
        private DisconnectingState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.sendMessageDelayed(20, dataNetwork.mDataConfigManager.getAnomalyNetworkDisconnectingTimeoutMs());
            DataNetwork.this.notifyPreciseDataConnectionState();
            DataNetwork.this.notifyImsDataNetwork();
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            DataNetwork.this.removeMessages(20);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            DataNetwork.this.logv("event=" + DataNetwork.eventToString(message.what));
            int i = message.what;
            if (i == 7) {
                DataNetwork dataNetwork = DataNetwork.this;
                if (!dataNetwork.mInvokedDataDeactivation) {
                    dataNetwork.removeMessages(7);
                    DataNetwork.this.removeDeferredMessages(7);
                    DataNetwork.this.onTearDown(message.arg1);
                    return true;
                }
                dataNetwork.log("Ignore tear down request because network is being torn down.");
                return true;
            }
            if (i == 13) {
                DataNetwork.this.onDisplayInfoChanged();
                return true;
            }
            if (i == 19) {
                DataNetwork.this.onDeactivateResponse(message.arg1);
                return true;
            }
            if (i == 20) {
                DataNetwork.this.reportAnomaly("RIL did not send data call list changed event after deactivate data call request within " + (((long) DataNetwork.this.mDataConfigManager.getAnomalyNetworkDisconnectingTimeoutMs()) / 1000) + " seconds.", "d0e4fa1c-c57b-4ba5-b4b6-8955487012cc");
                DataNetwork.this.mFailCause = 65540;
                DataNetwork dataNetwork2 = DataNetwork.this;
                dataNetwork2.transitionTo(dataNetwork2.mDisconnectedState);
                return true;
            }
            switch (i) {
                case 22:
                case 23:
                case 24:
                    DataNetwork.this.updateSuspendState();
                    DataNetwork.this.updateNetworkCapabilities();
                    return true;
                default:
                    return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class DisconnectedState extends State {
        private DisconnectedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            DataNetwork.this.logl("Data network disconnected. mEverConnected=" + DataNetwork.this.mEverConnected);
            final DataNetworkController.NetworkRequestList networkRequestList = new DataNetworkController.NetworkRequestList(DataNetwork.this.mAttachedNetworkRequestList);
            DataNetwork.this.sendMessage(10);
            DataNetwork.this.quit();
            boolean z = DataNetwork.this.mEverConnected;
            DataNetwork dataNetwork = DataNetwork.this;
            if (z) {
                dataNetwork.mLinkStatus = 0;
                DataNetwork.this.mNetworkValidationStatus = 0;
                DataNetwork.this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$DisconnectedState$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$enter$0();
                    }
                });
                DataNetwork.this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$DisconnectedState$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$enter$1();
                    }
                });
                DataNetwork dataNetwork2 = DataNetwork.this;
                if (dataNetwork2.mTransport == 1) {
                    dataNetwork2.unregisterForWwanEvents();
                }
                DataNetwork.this.mDataNetworkValidationStats.onDataNetworkDisconnected(DataNetwork.this.getDataNetworkType());
            } else {
                dataNetwork.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$DisconnectedState$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$enter$2(networkRequestList);
                    }
                });
            }
            DataNetwork.this.notifyPreciseDataConnectionState();
            DataNetwork.this.notifyImsDataNetwork();
            if (TelephonyBaseUtilsStub.isAutoDevice()) {
                MiuiDataControllerStub.IMiuiDataController iMiuiDataController = MiuiDataControllerStub.get(DataNetwork.this.mPhone.getPhoneId());
                DataNetwork dataNetwork3 = DataNetwork.this;
                iMiuiDataController.updateNetworkAgent(dataNetwork3.mPhone, dataNetwork3.mDataProfile, null);
            }
            DataNetwork.this.mNetworkAgent.unregister();
            DataNetwork dataNetwork4 = DataNetwork.this;
            dataNetwork4.mDataNetworkController.unregisterDataNetworkControllerCallback(dataNetwork4.mDataNetworkControllerCallback);
            boolean z2 = DataNetwork.this.mIsCidReuseTeardown;
            DataNetwork dataNetwork5 = DataNetwork.this;
            if (z2) {
                dataNetwork5.logl("Skipping Stats.onDataCallDisconnected for CID reuse.");
            } else {
                dataNetwork5.mDataCallSessionStats.onDataCallDisconnected(dataNetwork5.mFailCause);
            }
            DataNetwork dataNetwork6 = DataNetwork.this;
            if (dataNetwork6.mTransport == 2 && dataNetwork6.mPduSessionId != 0) {
                DataNetwork.this.mRil.releasePduSessionId(null, DataNetwork.this.mPduSessionId);
            }
            if (DataNetwork.this.mVcnManager == null || DataNetwork.this.mVcnPolicyChangeListener == null) {
                return;
            }
            DataNetwork.this.mVcnManager.removeVcnNetworkPolicyChangeListener(DataNetwork.this.mVcnPolicyChangeListener);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$enter$0() {
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.mDataNetworkCallback.onLinkStatusChanged(dataNetwork, dataNetwork.mLinkStatus);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$enter$1() {
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.mDataNetworkCallback.onDisconnected(dataNetwork, dataNetwork.mFailCause, DataNetwork.this.mTearDownReason);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$enter$2(DataNetworkController.NetworkRequestList networkRequestList) {
            DataNetwork dataNetwork = DataNetwork.this;
            dataNetwork.mDataNetworkCallback.onSetupDataFailed(dataNetwork, networkRequestList, dataNetwork.mFailCause, DataNetwork.this.mRetryDelayMillis);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            DataNetwork.this.logv("event=" + DataNetwork.eventToString(message.what));
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerForWwanEvents() {
        registerForBandwidthUpdate();
        this.mKeepaliveTracker.registerForKeepaliveStatus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterForWwanEvents() {
        unregisterForBandwidthUpdate();
        this.mKeepaliveTracker.unregisterForKeepaliveStatus();
    }

    @Override // com.android.internal.telephony.StateMachine
    protected void unhandledMessage(Message message) {
        IState currentState = getCurrentState();
        StringBuilder sb = new StringBuilder("Unhandled message ");
        sb.append(message.what);
        sb.append(" in state ");
        sb.append(currentState == null ? "null" : currentState.getName());
        loge(sb.toString());
    }

    public boolean attachNetworkRequests(DataNetworkController.NetworkRequestList networkRequestList) {
        if (getCurrentState() == null || isDisconnected()) {
            return false;
        }
        if (MiuiDataControllerStub.get(this.mPhone.getPhoneId()).attachNetworkRequests(networkRequestList)) {
            log("attachNetworkRequests return false");
            return false;
        }
        sendMessage(obtainMessage(2, networkRequestList));
        return true;
    }

    public void onAttachNetworkRequests(DataNetworkController.NetworkRequestList networkRequestList) {
        final DataNetworkController.NetworkRequestList networkRequestList2 = new DataNetworkController.NetworkRequestList();
        for (TelephonyNetworkRequest telephonyNetworkRequest : networkRequestList) {
            if (!this.mDataNetworkController.isNetworkRequestExisting(telephonyNetworkRequest)) {
                networkRequestList2.add(telephonyNetworkRequest);
                log("Attached failed. Network request was already removed. " + telephonyNetworkRequest);
            } else if (!telephonyNetworkRequest.canBeSatisfiedBy(getNetworkCapabilities())) {
                networkRequestList2.add(telephonyNetworkRequest);
                log("Attached failed. Cannot satisfy the network request " + telephonyNetworkRequest);
            } else {
                this.mAttachedNetworkRequestList.add(telephonyNetworkRequest);
                telephonyNetworkRequest.setAttachedNetwork(this);
                telephonyNetworkRequest.setState(1);
                log("Successfully attached network request " + telephonyNetworkRequest);
            }
        }
        if (networkRequestList2.isEmpty()) {
            return;
        }
        this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onAttachNetworkRequests$0(networkRequestList2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAttachNetworkRequests$0(DataNetworkController.NetworkRequestList networkRequestList) {
        this.mDataNetworkCallback.onAttachFailed(this, networkRequestList);
    }

    protected void onDetachNetworkRequest(final TelephonyNetworkRequest telephonyNetworkRequest, boolean z) {
        this.mAttachedNetworkRequestList.remove(telephonyNetworkRequest);
        if (telephonyNetworkRequest.getAttachedNetwork() == this) {
            telephonyNetworkRequest.setState(0);
            telephonyNetworkRequest.setAttachedNetwork(null);
            if (z) {
                this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onDetachNetworkRequest$1(telephonyNetworkRequest);
                    }
                });
            }
        }
        if (this.mAttachedNetworkRequestList.isEmpty()) {
            log("All network requests are detached.");
            int preferredDataPhoneId = PhoneSwitcher.getInstance().getPreferredDataPhoneId();
            if (preferredDataPhoneId != -1 && preferredDataPhoneId != this.mPhone.getPhoneId()) {
                lambda$tearDownWhenConditionMet$11(30);
            }
            MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onDetachNetworkRequest(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDetachNetworkRequest$1(TelephonyNetworkRequest telephonyNetworkRequest) {
        this.mDataNetworkCallback.onRetryUnsatisfiedNetworkRequest(telephonyNetworkRequest);
    }

    public void detachNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest, boolean z) {
        if (getCurrentState() == null || isDisconnected()) {
            return;
        }
        sendMessage(obtainMessage(3, z ? 1 : 0, 0, telephonyNetworkRequest));
    }

    private void registerForBandwidthUpdate() {
        int bandwidthEstimateSource = this.mDataConfigManager.getBandwidthEstimateSource();
        if (bandwidthEstimateSource == 1) {
            this.mPhone.mCi.registerForLceInfo(getHandler(), 11, null);
            return;
        }
        if (bandwidthEstimateSource == 3) {
            if (this.mLinkBandwidthEstimatorCallback == null) {
                Handler handler = getHandler();
                Objects.requireNonNull(handler);
                this.mLinkBandwidthEstimatorCallback = new LinkBandwidthEstimator.LinkBandwidthEstimatorCallback(new NetworkTypeController$$ExternalSyntheticLambda2(handler)) { // from class: com.android.internal.telephony.data.DataNetwork.3
                    @Override // com.android.internal.telephony.data.LinkBandwidthEstimator.LinkBandwidthEstimatorCallback
                    public void onBandwidthChanged(int i, int i2) {
                        if (DataNetwork.this.isConnected()) {
                            DataNetwork.this.onBandwidthUpdated(i, i2);
                        }
                    }
                };
                this.mPhone.getLinkBandwidthEstimator().registerCallback(this.mLinkBandwidthEstimatorCallback);
                return;
            }
            return;
        }
        loge("Invalid bandwidth source configuration: " + bandwidthEstimateSource);
    }

    private void unregisterForBandwidthUpdate() {
        int bandwidthEstimateSource = this.mDataConfigManager.getBandwidthEstimateSource();
        if (bandwidthEstimateSource == 1) {
            this.mPhone.mCi.unregisterForLceInfo(getHandler());
            return;
        }
        if (bandwidthEstimateSource == 3) {
            if (this.mLinkBandwidthEstimatorCallback != null) {
                this.mPhone.getLinkBandwidthEstimator().unregisterCallback(this.mLinkBandwidthEstimatorCallback);
                this.mLinkBandwidthEstimatorCallback = null;
                return;
            }
            return;
        }
        loge("Invalid bandwidth source configuration: " + bandwidthEstimateSource);
    }

    private void removeUnsatisfiedNetworkRequests(boolean z) {
        for (TelephonyNetworkRequest telephonyNetworkRequest : this.mAttachedNetworkRequestList) {
            if (!telephonyNetworkRequest.canBeSatisfiedBy(this.mNetworkCapabilities)) {
                log("removeUnsatisfiedNetworkRequests: " + telephonyNetworkRequest + " can't be satisfied anymore. Will be detached.");
                detachNetworkRequest(telephonyNetworkRequest, z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class LinkAddressKey {
        private final InetAddress mAddress;
        private final int mPrefixLength;
        private final int mScope;

        LinkAddressKey(LinkAddress linkAddress) {
            this.mAddress = linkAddress.getAddress();
            this.mPrefixLength = linkAddress.getPrefixLength();
            this.mScope = linkAddress.getScope();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof LinkAddressKey)) {
                return false;
            }
            LinkAddressKey linkAddressKey = (LinkAddressKey) obj;
            return this.mAddress.equals(linkAddressKey.mAddress) && this.mPrefixLength == linkAddressKey.mPrefixLength && this.mScope == linkAddressKey.mScope;
        }

        public int hashCode() {
            return Objects.hash(this.mAddress, Integer.valueOf(this.mPrefixLength), Integer.valueOf(this.mScope));
        }
    }

    private boolean isLinkPropertiesCompatible(LinkProperties linkProperties, LinkProperties linkProperties2) {
        if (!Objects.equals(linkProperties, linkProperties2) && !LinkPropertiesUtils.isIdenticalAddresses(linkProperties, linkProperties2)) {
            LinkPropertiesUtils.CompareOrUpdateResult compareOrUpdateResult = new LinkPropertiesUtils.CompareOrUpdateResult(linkProperties.getLinkAddresses(), linkProperties2.getLinkAddresses(), new Function() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda23
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return DataNetwork.$r8$lambda$rEArn3bOr0qWIAUh4EUFWbSfVZg((LinkAddress) obj);
                }
            });
            log("isLinkPropertiesCompatible: old=" + linkProperties + " new=" + linkProperties2 + " result=" + compareOrUpdateResult);
            ArrayList arrayList = new ArrayList(compareOrUpdateResult.added);
            final ArrayList arrayList2 = new ArrayList(compareOrUpdateResult.removed);
            arrayList.removeIf(new Predicate() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda24
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataNetwork.$r8$lambda$C61G5dBbAkYn32c0LFHfTyfzQ7U(arrayList2, (LinkAddress) obj);
                }
            });
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                LinkAddress linkAddress = (LinkAddress) obj;
                int size2 = arrayList2.size();
                int i2 = 0;
                while (i2 < size2) {
                    Object obj2 = arrayList2.get(i2);
                    i2++;
                    if (NetUtils.addressTypeMatches(((LinkAddress) obj2).getAddress(), linkAddress.getAddress())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static /* synthetic */ LinkAddressKey $r8$lambda$rEArn3bOr0qWIAUh4EUFWbSfVZg(LinkAddress linkAddress) {
        return new LinkAddressKey(linkAddress);
    }

    public static /* synthetic */ boolean $r8$lambda$C61G5dBbAkYn32c0LFHfTyfzQ7U(List list, LinkAddress linkAddress) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (isSameIpv6Prefix((LinkAddress) it.next(), linkAddress)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    private static boolean isSameIpv6Prefix(LinkAddress linkAddress, LinkAddress linkAddress2) {
        if (linkAddress.isIpv6() && linkAddress2.isIpv6()) {
            return new IpPrefix(linkAddress.getAddress(), linkAddress.getPrefixLength()).equals(new IpPrefix(linkAddress2.getAddress(), linkAddress2.getPrefixLength()));
        }
        return false;
    }

    private void recreateNetworkAgent() {
        if (isConnecting() || isDisconnected() || isDisconnecting()) {
            loge("Incorrect state for re-creating the network agent.");
            return;
        }
        this.mNetworkAgent.abandon();
        TelephonyNetworkAgent telephonyNetworkAgentCreateNetworkAgent = createNetworkAgent();
        this.mNetworkAgent = telephonyNetworkAgentCreateNetworkAgent;
        telephonyNetworkAgentCreateNetworkAgent.markConnected();
        QosCallbackTracker qosCallbackTracker = this.mQosCallbackTracker;
        if (qosCallbackTracker != null) {
            qosCallbackTracker.updateNetworkAgent(this.mNetworkAgent);
        }
        notifyPreciseDataConnectionState();
        if (this.mSuspended) {
            log("recreateNetworkAgent: The network is in suspended state. Update the network capability again. nc=" + this.mNetworkCapabilities);
            this.mNetworkAgent.sendNetworkCapabilities(this.mNetworkCapabilities);
        }
        updateLingerAndTearDownDelayTimers(this.mLastKnownDataNetworkType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkCapabilities() {
        NetworkRegistrationInfo networkRegistrationInfo;
        DataProfile dataProfileForNetworkRequest;
        NetworkCapabilities.Builder builder = new NetworkCapabilities.Builder();
        if (this.mSatellite && this.mDataConfigManager.getForcedCellularTransportCapabilities().stream().noneMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.hasNetworkCapabilityInNetworkRequests(((Integer) obj).intValue());
            }
        })) {
            logd("transport satellite is set");
            builder.addTransportType(10);
        } else {
            builder.addTransportType(0);
        }
        boolean dataRoaming = this.mPhone.getServiceState().getDataRoaming();
        builder.setNetworkSpecifier(new TelephonyNetworkSpecifier.Builder().setSubscriptionId(this.mSubId).build());
        builder.setSubscriptionIds(Collections.singleton(Integer.valueOf(this.mSubId)));
        ApnSetting apnSetting = this.mDataProfile.getApnSetting();
        if (apnSetting != null) {
            apnSetting.getApnTypes().stream().map(new DataConfigManager$$ExternalSyntheticLambda18()).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda12
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataNetwork.m696$r8$lambda$7TUk9kloVeLTmeDGV8MnLw0A6I((Integer) obj);
                }
            }).forEach(new DataNetwork$$ExternalSyntheticLambda13(builder));
            if (apnSetting.getApnTypes().contains(16384)) {
                builder.addCapability(12);
            }
        }
        if (hasNetworkCapabilityInNetworkRequests(33)) {
            builder.addCapability(33);
        } else if (this.mDataProfile.canSatisfy(4)) {
            builder.addCapability(33);
            if (this.mTransport == 1 && (networkRegistrationInfo = getNetworkRegistrationInfo()) != null) {
                DataSpecificRegistrationInfo dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo();
                if (dataSpecificInfo != null && dataSpecificInfo.getVopsSupportInfo() != null && !dataSpecificInfo.getVopsSupportInfo().isVopsSupported()) {
                    builder.removeCapability(33);
                }
                log("updateNetworkCapabilities: dsri=" + dataSpecificInfo);
            }
        }
        Iterator it = this.mTrafficDescriptors.iterator();
        while (it.hasNext()) {
            int iConnectionCapabilityToNetworkCapability = this.mDataConfigManager.connectionCapabilityToNetworkCapability(((TrafficDescriptor) it.next()).getConnectionCapability());
            if (iConnectionCapabilityToNetworkCapability != -1) {
                builder.addCapability(iConnectionCapabilityToNetworkCapability);
            }
        }
        for (TrafficDescriptor trafficDescriptor : this.mTrafficDescriptors) {
            try {
                if (trafficDescriptor.getOsAppId() != null) {
                    TrafficDescriptor.OsAppId osAppId = new TrafficDescriptor.OsAppId(trafficDescriptor.getOsAppId());
                    if (osAppId.getOsId().equals(TrafficDescriptor.OsAppId.ANDROID_OS_ID)) {
                        int networkCapabilityFromString = DataUtils.getNetworkCapabilityFromString(osAppId.getAppId());
                        if (networkCapabilityFromString != 5) {
                            if (networkCapabilityFromString == 29) {
                                builder.addCapability(networkCapabilityFromString);
                                builder.addCapability(12);
                                builder.addEnterpriseId(osAppId.getDifferentiator());
                            } else if (networkCapabilityFromString != 38 && networkCapabilityFromString != 34 && networkCapabilityFromString != 35) {
                                loge("Invalid app id " + osAppId.getAppId());
                            }
                        }
                        builder.addCapability(networkCapabilityFromString);
                    } else {
                        loge("Received non-Android OS id " + osAppId.getOsId());
                    }
                }
            } catch (Exception e) {
                loge("Exception: " + e + ". Failed to create osAppId from " + new BigInteger(1, trafficDescriptor.getOsAppId()).toString(16));
            }
        }
        if (!this.mCongested) {
            builder.addCapability(20);
        }
        if (this.mTempNotMeteredSupported && this.mTempNotMetered) {
            builder.addCapability(25);
        }
        builder.addCapability(28);
        VcnNetworkPolicyResult vcnPolicy = getVcnPolicy(builder.build());
        if (vcnPolicy != null && !vcnPolicy.getNetworkCapabilities().hasCapability(28)) {
            builder.removeCapability(28);
        }
        if (!dataRoaming) {
            builder.addCapability(18);
        }
        if (!this.mSuspended) {
            builder.addCapability(21);
        }
        int i = this.mCarrierServicePackageUid;
        if (i != -1 && ArrayUtils.contains(this.mAdministratorUids, i)) {
            builder.setOwnerUid(this.mCarrierServicePackageUid);
            builder.setAllowedUids(Collections.singleton(Integer.valueOf(this.mCarrierServicePackageUid)));
        }
        builder.setAdministratorUids(this.mAdministratorUids);
        Set set = (Set) this.mDataConfigManager.getMeteredNetworkCapabilities(dataRoaming).stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$updateNetworkCapabilities$5((Integer) obj);
            }
        }).collect(Collectors.toSet());
        Stream stream = set.stream();
        final Set set2 = (Set) Arrays.stream(builder.build().getCapabilities()).boxed().collect(Collectors.toSet());
        Objects.requireNonNull(set2);
        if (stream.noneMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return set2.contains((Integer) obj);
            }
        })) {
            builder.addCapability(11);
        }
        builder.addCapability(13);
        if (!this.mDataNetworkController.getDataSettingsManager().isDataEnabled() || (this.mPhone.getServiceState().getDataRoaming() && !this.mDataNetworkController.getDataSettingsManager().isDataRoamingEnabled())) {
            DataEvaluation.DataAllowedReason dataAllowedReason = this.mDataAllowedReason;
            if (dataAllowedReason == DataEvaluation.DataAllowedReason.RESTRICTED_REQUEST) {
                builder.removeCapability(13);
            } else if (dataAllowedReason == DataEvaluation.DataAllowedReason.UNMETERED_USAGE || dataAllowedReason == DataEvaluation.DataAllowedReason.MMS_REQUEST || dataAllowedReason == DataEvaluation.DataAllowedReason.EMERGENCY_SUPL) {
                Iterator it2 = set.iterator();
                while (it2.hasNext()) {
                    int iIntValue = ((Integer) it2.next()).intValue();
                    if (iIntValue != 0 || this.mDataAllowedReason != DataEvaluation.DataAllowedReason.MMS_REQUEST) {
                        if (iIntValue != 1 || this.mDataAllowedReason != DataEvaluation.DataAllowedReason.EMERGENCY_SUPL) {
                            builder.removeCapability(iIntValue);
                        }
                    }
                }
            }
        }
        if (this.mIHyperDataManager.isVowifiMmsDataNetwork(this.mTransport)) {
            builder.removeCapability(12);
        }
        if (this.mDataNetworkController.isEsimBootStrapProvisioningActivated()) {
            builder.removeCapability(13);
        }
        int[] iArr = {0, 9};
        for (int i2 = 0; i2 < 2; i2++) {
            int i3 = iArr[i2];
            if (builder.build().hasCapability(i3) && this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(i3) == 2 && this.mTransport == 1 && (dataProfileForNetworkRequest = this.mDataNetworkController.getDataProfileManager().getDataProfileForNetworkRequest(new TelephonyNetworkRequest(new NetworkRequest.Builder().addCapability(i3).build(), this.mPhone, this.mFlags), 18, false, false, false)) != null && !dataProfileForNetworkRequest.getApn().equals(this.mDataProfile.getApn())) {
                log("Found a different apn name " + dataProfileForNetworkRequest.getApn() + " that can serve " + DataUtils.networkCapabilityToString(i3) + " on IWLAN. Current data profile " + this.mDataProfile.getApn());
                builder.removeCapability(i3);
            }
        }
        if (NetworkCapabilitiesUtils.inferRestrictedCapability(builder.build()) || (vcnPolicy != null && !vcnPolicy.getNetworkCapabilities().hasCapability(13))) {
            builder.removeCapability(13);
        }
        builder.setLinkDownstreamBandwidthKbps(this.mNetworkBandwidth.downlinkBandwidthKbps);
        builder.setLinkUpstreamBandwidthKbps(this.mNetworkBandwidth.uplinkBandwidthKbps);
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).updateNetworkCapabilities(this.mPhone, builder, this);
        if (this.mSatellite && builder.build().hasCapability(13)) {
            int satelliteDataServicePolicyForPlmn = SatelliteController.getInstance().getSatelliteDataServicePolicyForPlmn(this.mSubId, this.mPhone.getServiceState().getOperatorNumeric());
            if (satelliteDataServicePolicyForPlmn == 0) {
                builder.removeCapability(13);
            } else if (satelliteDataServicePolicyForPlmn == 1) {
                try {
                    builder.removeCapability(37);
                } catch (Exception unused) {
                }
            }
        }
        mtkUpdateNetworkCapabilities(builder);
        NetworkCapabilities networkCapabilitiesAdjustNetworkCapabilities = adjustNetworkCapabilities(builder.build());
        NetworkCapabilities networkCapabilities = this.mNetworkCapabilities;
        if (networkCapabilities == null || this.mNetworkAgent == null) {
            this.mNetworkCapabilities = networkCapabilitiesAdjustNetworkCapabilities;
            logl("Initial capabilities " + this.mNetworkCapabilities);
            return;
        }
        if (!networkCapabilitiesAdjustNetworkCapabilities.equals(networkCapabilities)) {
            this.mNetworkCapabilities = networkCapabilitiesAdjustNetworkCapabilities;
            log("Capabilities changed to " + this.mNetworkCapabilities);
            this.mNetworkAgent.sendNetworkCapabilities(this.mNetworkCapabilities);
            removeUnsatisfiedNetworkRequests(isConnected() || isHandoverInProgress());
            this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateNetworkCapabilities$6();
                }
            });
            return;
        }
        log("updateNetworkCapabilities: Capabilities not changed.");
    }

    /* JADX INFO: renamed from: $r8$lambda$7TUk9kloVeLT-meDGV8MnLw0A6I, reason: not valid java name */
    public static /* synthetic */ boolean m696$r8$lambda$7TUk9kloVeLTmeDGV8MnLw0A6I(Integer num) {
        return num.intValue() >= 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateNetworkCapabilities$5(Integer num) {
        return this.mAccessNetworksManager.getPreferredTransportByNetworkCapability(num.intValue()) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateNetworkCapabilities$6() {
        this.mDataNetworkCallback.onNetworkCapabilitiesChanged(this);
    }

    public NetworkCapabilities getNetworkCapabilities() {
        return this.mNetworkCapabilities;
    }

    public LinkProperties getLinkProperties() {
        return this.mLinkProperties;
    }

    public DataProfile getDataProfile() {
        return this.mDataProfile;
    }

    protected void updateSuspendState() {
        NetworkRegistrationInfo networkRegistrationInfo;
        if (isConnecting() || isDisconnected() || (networkRegistrationInfo = getNetworkRegistrationInfo()) == null) {
            return;
        }
        boolean z = false;
        if (!this.mNetworkCapabilities.hasCapability(10) && ((networkRegistrationInfo.getRegistrationState() != 1 && networkRegistrationInfo.getRegistrationState() != 5) || (!this.mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed() && this.mTransport == 1 && this.mPhone.getCallTracker() != null && this.mPhone.getCallTracker().getState() != PhoneConstants.State.IDLE))) {
            z = true;
        }
        boolean zReviseSuspendedState = reviseSuspendedState(z, networkRegistrationInfo);
        if (this.mSuspended != zReviseSuspendedState) {
            this.mSuspended = zReviseSuspendedState;
            logl("Network becomes ".concat(zReviseSuspendedState ? "suspended" : "unsuspended"));
            updateNetworkCapabilities();
            if (!this.mSuspended) {
                this.mNetworkAgent.markConnected();
            }
            notifyPreciseDataConnectionState();
            this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateSuspendState$7();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSuspendState$7() {
        this.mDataNetworkCallback.onSuspendedStateChanged(this, this.mSuspended);
    }

    public void sendSuspendMessage(boolean z) {
        if (z) {
            log("send message: EVENT_VOICE_CALL_STARTED");
            sendMessage(22);
        } else {
            log("send message: EVENT_VOICE_CALL_ENDED");
            sendMessage(23);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void allocatePduSessionId() {
        this.mRil.allocatePduSessionId(obtainMessage(5));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getFailCauseFromDataCallResponse(int i, DataCallResponse dataCallResponse) {
        if (i == 0) {
            if (dataCallResponse != null) {
                return DataFailCause.getFailCause(dataCallResponse.getCause());
            }
            return 0;
        }
        if (i == 1) {
            return 65546;
        }
        if (i == 2) {
            return 65538;
        }
        if (i == 3) {
            return 65545;
        }
        if (i != 4) {
            return i != 5 ? 0 : 65545;
        }
        return 65537;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDataNetwork(DataCallResponse dataCallResponse) {
        if (dataCallResponse == null) {
            return;
        }
        this.mCid.put(this.mTransport, dataCallResponse.getId());
        LinkProperties linkProperties = new LinkProperties();
        linkProperties.setInterfaceName(dataCallResponse.getInterfaceName());
        if (this.mPduSessionId != dataCallResponse.getPduSessionId()) {
            this.mPduSessionId = dataCallResponse.getPduSessionId();
            log("PDU session id updated to " + this.mPduSessionId);
        }
        if (this.mLinkStatus != dataCallResponse.getLinkStatus()) {
            this.mLinkStatus = dataCallResponse.getLinkStatus();
            log("Link status updated to " + DataUtils.linkStatusToString(this.mLinkStatus));
            if (isConnected()) {
                this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda20
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$updateDataNetwork$8();
                    }
                });
            }
        }
        if (dataCallResponse.getAddresses().isEmpty()) {
            loge("no address for ifname=" + dataCallResponse.getInterfaceName());
        } else {
            for (LinkAddress linkAddress : dataCallResponse.getAddresses()) {
                if (!linkAddress.getAddress().isAnyLocalAddress()) {
                    logv("addr/pl=" + linkAddress.getAddress() + "/" + linkAddress.getPrefixLength());
                    linkProperties.addLinkAddress(linkAddress);
                }
            }
        }
        if (!dataCallResponse.getDnsAddresses().isEmpty()) {
            for (InetAddress inetAddress : dataCallResponse.getDnsAddresses()) {
                if (!inetAddress.isAnyLocalAddress()) {
                    linkProperties.addDnsServer(inetAddress);
                }
            }
        } else {
            if (dataCallResponse.getDnsAddresses().size() == 0) {
                MiuiDataControllerStub.get(this.mPhone.getPhoneId()).fillDnsByDefault(this.mPhone, linkProperties);
            }
            loge("Empty dns response");
        }
        if (!dataCallResponse.getPcscfAddresses().isEmpty()) {
            Iterator it = dataCallResponse.getPcscfAddresses().iterator();
            while (it.hasNext()) {
                linkProperties.addPcscfServer((InetAddress) it.next());
            }
        }
        int mtuV4 = dataCallResponse.getMtuV4() > 0 ? dataCallResponse.getMtuV4() : dataCallResponse.getMtu();
        if (mtuV4 <= 0) {
            int iMtkGetCustomizedMtu = mtkGetCustomizedMtu(this.mDataProfile.getApnSetting(), 0);
            if (iMtkGetCustomizedMtu > 0) {
                mtuV4 = iMtkGetCustomizedMtu;
            } else {
                if (this.mDataProfile.getApnSetting() != null) {
                    mtuV4 = this.mDataProfile.getApnSetting().getMtuV4();
                }
                if (mtuV4 <= 0) {
                    mtuV4 = this.mDataConfigManager.getDefaultMtu();
                }
            }
        }
        int mtuV6 = dataCallResponse.getMtuV6() > 0 ? dataCallResponse.getMtuV6() : dataCallResponse.getMtu();
        if (mtuV6 <= 0) {
            int iMtkGetCustomizedMtu2 = mtkGetCustomizedMtu(this.mDataProfile.getApnSetting(), 1);
            if (iMtkGetCustomizedMtu2 > 0) {
                mtuV6 = iMtkGetCustomizedMtu2;
            } else {
                if (this.mDataProfile.getApnSetting() != null) {
                    mtuV6 = this.mDataProfile.getApnSetting().getMtuV6();
                }
                if (mtuV6 <= 0) {
                    mtuV6 = this.mDataConfigManager.getDefaultMtu();
                }
            }
        }
        for (InetAddress inetAddress2 : dataCallResponse.getGatewayAddresses()) {
            linkProperties.addRoute(new RouteInfo(null, inetAddress2, null, 1, MiuiDataControllerStub.get(this.mPhone.getPhoneId()).updatePreferredMtu(this.mPhone, inetAddress2, ServiceState.networkTypeToRilRadioTechnology(getDataNetworkType()), inetAddress2 instanceof Inet6Address ? mtuV6 : mtuV4)));
        }
        linkProperties.setMtu(Math.max(mtuV4, mtuV6));
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).updateLinkProperties(this.mPhone, linkProperties);
        if (this.mDataProfile.getApnSetting() != null && !TextUtils.isEmpty(this.mDataProfile.getApnSetting().getProxyAddressAsString())) {
            int proxyPort = this.mDataProfile.getApnSetting().getProxyPort();
            if (proxyPort == -1) {
                proxyPort = 8080;
            }
            linkProperties.setHttpProxy(ProxyInfo.buildDirectProxy(this.mDataProfile.getApnSetting().getProxyAddressAsString(), proxyPort));
        }
        linkProperties.setTcpBufferSizes(this.mTcpBufferSizes);
        this.mNetworkSliceInfo = dataCallResponse.getSliceInfo();
        this.mTrafficDescriptors.clear();
        this.mTrafficDescriptors.addAll(dataCallResponse.getTrafficDescriptors());
        this.mDefaultQos = dataCallResponse.getDefaultQos();
        HashSet hashSet = new HashSet(dataCallResponse.getQosBearerSessions());
        if (hashSet.size() != this.mQosBearerSessions.size() || !hashSet.containsAll(this.mQosBearerSessions)) {
            this.mDataNetworkCallback.onQosSessionsChanged(dataCallResponse.getQosBearerSessions());
        }
        updateQosBearerSessions(dataCallResponse.getQosBearerSessions());
        int physicalNetworkTransportType = dataCallResponse.getPhysicalNetworkTransportType() == -1 ? this.mTransport : dataCallResponse.getPhysicalNetworkTransportType();
        int physicalNetworkSlotIndex = dataCallResponse.getPhysicalNetworkSlotIndex();
        if (physicalNetworkTransportType == 1 && physicalNetworkSlotIndex == -1) {
            physicalNetworkSlotIndex = this.mPhone.getPhoneId();
        }
        if (this.mPhysicalNetworkTransportType != physicalNetworkTransportType || this.mPhysicalNetworkSlotIndex != physicalNetworkSlotIndex) {
            this.mPhysicalNetworkTransportType = physicalNetworkTransportType;
            this.mPhysicalNetworkSlotIndex = physicalNetworkSlotIndex;
            notifyImsDataNetwork();
        }
        if (!linkProperties.equals(this.mLinkProperties)) {
            if ((isConnected() || isHandoverInProgress()) && !isLinkPropertiesCompatible(this.mLinkProperties, linkProperties)) {
                logl("updateDataNetwork: Incompatible link properties detected. Re-create the network agent. Changed from " + this.mLinkProperties + " to " + linkProperties);
                this.mLinkProperties = linkProperties;
                recreateNetworkAgent();
            } else {
                this.mLinkProperties = linkProperties;
                log("sendLinkProperties " + this.mLinkProperties);
                this.mNetworkAgent.sendLinkProperties(this.mLinkProperties);
            }
        }
        updateNetworkCapabilities();
        updateValidationStatus(dataCallResponse.getNetworkValidationStatus());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDataNetwork$8() {
        this.mDataNetworkCallback.onLinkStatusChanged(this, this.mLinkStatus);
    }

    public void updateQosBearerSessions(List<QosBearerSession> list) {
        log("updateQosBearerSessions: " + list);
        this.mQosBearerSessions.clear();
        this.mQosBearerSessions.addAll(list);
        QosCallbackTracker qosCallbackTracker = this.mQosCallbackTracker;
        if (qosCallbackTracker != null) {
            qosCallbackTracker.updateSessions(this.mQosBearerSessions);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void validateDataCallResponse(DataCallResponse dataCallResponse, int i) {
        int protocol;
        if (dataCallResponse == null || dataCallResponse.getLinkStatus() == 0) {
            return;
        }
        int cause = dataCallResponse.getCause();
        if (cause == 0) {
            if (TextUtils.isEmpty(dataCallResponse.getInterfaceName()) || dataCallResponse.getAddresses().isEmpty() || dataCallResponse.getId() == -1 || dataCallResponse.getLinkStatus() < -1 || dataCallResponse.getLinkStatus() > 2 || dataCallResponse.getProtocolType() < -1 || dataCallResponse.getProtocolType() > 5 || dataCallResponse.getHandoverFailureMode() < -1 || dataCallResponse.getHandoverFailureMode() > 3) {
                loge("Invalid DataCallResponse:" + dataCallResponse);
                reportAnomaly("Invalid DataCallResponse detected", "1f273e9d-b09c-46eb-ad1c-421d01f61164");
            }
            NetworkRegistrationInfo networkRegistrationInfo = getNetworkRegistrationInfo();
            if (i == -1 || this.mDataProfile.getApnSetting() == null || networkRegistrationInfo == null || !networkRegistrationInfo.isInService()) {
                return;
            }
            boolean z = i == 5;
            boolean z2 = networkRegistrationInfo.getNetworkRegistrationState() == 5;
            if (z == z2) {
                DataProfile dataProfile = this.mDataProfile;
                if (z2) {
                    protocol = dataProfile.getApnSetting().getRoamingProtocol();
                } else {
                    protocol = dataProfile.getApnSetting().getProtocol();
                }
                String str = this.mTransport == 1 ? "RIL" : "IWLAN data service";
                if (protocol == 0) {
                    if (dataCallResponse.getAddresses().stream().noneMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda21
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return DataNetwork.$r8$lambda$ETmF1tnWWhjnFyVZo5XLYRxK7wY((LinkAddress) obj);
                        }
                    })) {
                        loge("Invalid DataCallResponse. Requested IPv4 but didn't get an IPv4 address." + dataCallResponse);
                        reportAnomaly(str.concat(" reported mismatched IP type. Requested IPv4 but didn't get an IPv4 address."), "7744f920-fb64-4db0-ba47-de0eae485a82");
                        return;
                    }
                    return;
                }
                if (protocol == 1 && dataCallResponse.getAddresses().stream().noneMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda22
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return DataNetwork.m695$r8$lambda$6TEVcocLhSCuffMVtLED5gdqiA((LinkAddress) obj);
                    }
                })) {
                    loge("Invalid DataCallResponse. Requested IPv6 but didn't get an IPv6 address." + dataCallResponse);
                    reportAnomaly(str.concat(" reported mismatched IP type. Requested IPv6 but didn't get an IPv6 address."), "7744f920-fb64-4db0-ba47-de0eae485a82");
                    return;
                }
                return;
            }
            return;
        }
        if (DataFailCause.isFailCauseExisting(cause)) {
            return;
        }
        loge("Invalid DataFailCause in " + dataCallResponse);
        reportAnomaly("Invalid DataFailCause: (0x" + Integer.toHexString(cause) + ")", "6b264f28-9f58-4cbd-9e0e-d7624ba30879");
    }

    public static /* synthetic */ boolean $r8$lambda$ETmF1tnWWhjnFyVZo5XLYRxK7wY(LinkAddress linkAddress) {
        return linkAddress.getAddress() instanceof Inet4Address;
    }

    /* JADX INFO: renamed from: $r8$lambda$6TEVcocLhS-CuffMVtLED5gdqiA, reason: not valid java name */
    public static /* synthetic */ boolean m695$r8$lambda$6TEVcocLhSCuffMVtLED5gdqiA(LinkAddress linkAddress) {
        return linkAddress.getAddress() instanceof Inet6Address;
    }

    protected void onDeactivateResponse(int i) {
        logl("onDeactivateResponse: resultCode=" + DataServiceCallback.resultCodeToString(i));
        if (i == 4) {
            log("Remove network since deactivate request returned an error.");
            this.mFailCause = 65537;
            transitionTo(this.mDisconnectedState);
        } else if (this.mPhone.getHalVersion(1).less(RIL.RADIO_HAL_VERSION_2_0) || MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onDeactivateResponse(this)) {
            log("Remove network on deactivate data response on old HAL " + this.mPhone.getHalVersion(1));
            this.mFailCause = 65540;
            transitionTo(this.mDisconnectedState);
        }
    }

    /* JADX INFO: renamed from: tearDown, reason: merged with bridge method [inline-methods] */
    public void lambda$tearDownWhenConditionMet$11(int i) {
        if (getCurrentState() == null || isDisconnected()) {
            return;
        }
        this.mTearDownReason = i;
        sendMessage(obtainMessage(7, i));
    }

    protected void onTearDown(int i) {
        logl("onTearDown: reason=" + tearDownReasonToString(i));
        if (i == 1 && isConnected() && (this.mNetworkCapabilities.hasCapability(4) || this.mNetworkCapabilities.hasCapability(12))) {
            this.mDataNetworkCallback.onTrackNetworkUnwanted(this);
        }
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onTearDown(this, i);
        ((DataServiceManager) this.mDataServiceManagers.get(this.mTransport)).deactivateDataCall(this.mCid.get(this.mTransport), replaceTearDownReason(i), obtainMessage(19));
        this.mDataCallSessionStats.setDeactivateDataCallReason(i);
        this.mInvokedDataDeactivation = true;
    }

    public boolean shouldDelayImsTearDownDueToInCall() {
        NetworkCapabilities networkCapabilities;
        return (!this.mDataConfigManager.isImsDelayTearDownUntilVoiceCallEndEnabled() || (networkCapabilities = this.mNetworkCapabilities) == null || !networkCapabilities.hasCapability(4) || this.mPhone.getImsPhone() == null || this.mPhone.getImsPhone().getCallTracker().getState() == PhoneConstants.State.IDLE) ? false : true;
    }

    public Runnable tearDownWhenConditionMet(final int i, long j) {
        if (getCurrentState() == null || isDisconnected() || isDisconnecting()) {
            loge("tearDownWhenConditionMet: Not in the right state. State=" + getCurrentState());
            return null;
        }
        logl("tearDownWhenConditionMet: reason=" + tearDownReasonToString(i) + ", timeout=" + j + "ms.");
        sendMessage(21, i, (int) j);
        return new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$tearDownWhenConditionMet$11(i);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataStateChanged(int i, List list, boolean z) {
        logv("onDataStateChanged: " + list);
        int i2 = this.mTransport;
        if (i != i2 || this.mCid.get(i2) == -1 || isDisconnected()) {
            return;
        }
        DataCallResponse dataCallResponse = (DataCallResponse) list.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$onDataStateChanged$12((DataCallResponse) obj);
            }
        }).findFirst().orElse(null);
        if (dataCallResponse != null) {
            if (dataCallResponse.equals(this.mDataCallResponse)) {
                return;
            }
            log("onDataStateChanged: " + dataCallResponse);
            validateDataCallResponse(dataCallResponse, -1);
            this.mDataCallResponse = dataCallResponse;
            if (dataCallResponse.getLinkStatus() != 0) {
                updateDataNetwork(dataCallResponse);
                notifyPreciseDataConnectionState();
                return;
            }
            log("onDataStateChanged: PDN inactive reported by " + AccessNetworkConstants.transportTypeToString(this.mTransport) + " data service.");
            this.mFailCause = this.mEverConnected ? dataCallResponse.getCause() : 65547;
            this.mRetryDelayMillis = -1L;
            transitionTo(this.mDisconnectedState);
            if (this.mFailCause != 65547) {
                notifyUnsolError(dataCallResponse);
                return;
            }
            return;
        }
        ((DataServiceManager) this.mDataServiceManagers.get(this.mTransport)).deactivateDataCall(this.mCid.get(this.mTransport), 1, null);
        log("onDataStateChanged: PDN disconnected reported by " + AccessNetworkConstants.transportTypeToString(this.mTransport) + " data service. requireExplicitDisconnect " + z);
        this.mFailCause = 65540;
        notifyUnsolError(null);
        this.mRetryDelayMillis = -1L;
        transitionTo(this.mDisconnectedState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onDataStateChanged$12(DataCallResponse dataCallResponse) {
        return this.mCid.get(this.mTransport) == dataCallResponse.getId();
    }

    private void notifyUnsolError(DataCallResponse dataCallResponse) {
        String apnName = this.mDataProfile.getApnSetting() != null ? this.mDataProfile.getApnSetting().getApnName() : "";
        if (TextUtils.isEmpty(apnName)) {
            return;
        }
        Phone phone = this.mPhone;
        if (dataCallResponse != null) {
            MiuiDataControllerStub.get(phone.getPhoneId()).onDataCallListChanged(this.mPhone, dataCallResponse.getCause(), apnName);
        } else {
            MiuiDataControllerStub.get(phone.getPhoneId()).onDataCallListChanged(this.mPhone, 65540, apnName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierConfigUpdated() {
        log("onCarrierConfigUpdated");
        updateBandwidthFromDataConfig();
        updateTcpBufferSizes();
        updateMeteredAndCongested();
        updateLingerAndTearDownDelayTimers(this.mLastKnownDataNetworkType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBandwidthUpdatedFromModem(List list) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            return;
        }
        Iterator it = list.iterator();
        int uplinkCapacityKbps = 0;
        int downlinkCapacityKbps = 0;
        while (it.hasNext()) {
            LinkCapacityEstimate linkCapacityEstimate = (LinkCapacityEstimate) it.next();
            if (linkCapacityEstimate.getType() == 2) {
                uplinkCapacityKbps = linkCapacityEstimate.getUplinkCapacityKbps();
                downlinkCapacityKbps = linkCapacityEstimate.getDownlinkCapacityKbps();
                break;
            } else if (linkCapacityEstimate.getType() == 0 || linkCapacityEstimate.getType() == 1) {
                uplinkCapacityKbps += linkCapacityEstimate.getUplinkCapacityKbps();
                downlinkCapacityKbps += linkCapacityEstimate.getDownlinkCapacityKbps();
            } else {
                loge("Invalid LinkCapacityEstimate type " + linkCapacityEstimate.getType());
            }
        }
        onBandwidthUpdated(uplinkCapacityKbps, downlinkCapacityKbps);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBandwidthUpdated(int i, int i2) {
        log("onBandwidthUpdated: downlinkBandwidthKbps=" + i2 + ", uplinkBandwidthKbps=" + i);
        NetworkBandwidth bandwidthForNetworkType = this.mDataConfigManager.getBandwidthForNetworkType(this.mTelephonyDisplayInfo);
        if (i2 == -1) {
            i2 = bandwidthForNetworkType.downlinkBandwidthKbps;
        }
        if (i == -1) {
            i = bandwidthForNetworkType.uplinkBandwidthKbps;
        }
        this.mNetworkBandwidth = new NetworkBandwidth(i2, Math.min(i, i2));
        updateNetworkCapabilities();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDisplayInfoChanged() {
        this.mTelephonyDisplayInfo = this.mPhone.getDisplayInfoController().getTelephonyDisplayInfo();
        updateBandwidthFromDataConfig();
        updateTcpBufferSizes();
        updateMeteredAndCongested();
    }

    private void updateBandwidthFromDataConfig() {
        if (this.mDataConfigManager.getBandwidthEstimateSource() != 2) {
            return;
        }
        log("updateBandwidthFromDataConfig");
        this.mNetworkBandwidth = this.mDataConfigManager.getBandwidthForNetworkType(this.mTelephonyDisplayInfo);
        updateNetworkCapabilities();
    }

    private void updateTcpBufferSizes() {
        log("updateTcpBufferSizes");
        this.mTcpBufferSizes = this.mDataConfigManager.getTcpConfigString(this.mTelephonyDisplayInfo);
        LinkProperties linkProperties = new LinkProperties(this.mLinkProperties);
        linkProperties.setTcpBufferSizes(this.mTcpBufferSizes);
        if (linkProperties.equals(this.mLinkProperties)) {
            return;
        }
        this.mLinkProperties = linkProperties;
        log("sendLinkProperties " + this.mLinkProperties);
        this.mNetworkAgent.sendLinkProperties(this.mLinkProperties);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMeteredAndCongested() {
        boolean z;
        int networkType = this.mTelephonyDisplayInfo.getNetworkType();
        int overrideNetworkType = this.mTelephonyDisplayInfo.getOverrideNetworkType();
        boolean z2 = true;
        if (overrideNetworkType == 1 || overrideNetworkType == 2) {
            networkType = 19;
        } else if (overrideNetworkType == 3 || overrideNetworkType == 5) {
            networkType = 20;
        }
        log("updateMeteredAndCongested: networkType=" + TelephonyManager.getNetworkTypeName(networkType));
        boolean zIsTempNotMeteredSupportedByCarrier = this.mDataConfigManager.isTempNotMeteredSupportedByCarrier();
        boolean z3 = this.mTempNotMeteredSupported;
        boolean z4 = false;
        if (zIsTempNotMeteredSupportedByCarrier != z3) {
            this.mTempNotMeteredSupported = !z3;
            log("updateMeteredAndCongested: mTempNotMeteredSupported changed to " + this.mTempNotMeteredSupported);
            z = true;
        } else {
            z = false;
        }
        if (this.mDataConfigManager.isNetworkTypeUnmetered(this.mTelephonyDisplayInfo, this.mPhone.getServiceState()) && (this.mDataNetworkController.getUnmeteredOverrideNetworkTypes().contains(Integer.valueOf(networkType)) || isNetworkTypeUnmetered(networkType))) {
            z4 = true;
        }
        if (z4 != this.mTempNotMetered) {
            this.mTempNotMetered = z4;
            log("updateMeteredAndCongested: mTempNotMetered changed to " + this.mTempNotMetered);
            z = true;
        }
        boolean zContains = this.mDataNetworkController.getCongestedOverrideNetworkTypes().contains(Integer.valueOf(networkType));
        boolean z5 = this.mCongested;
        if (zContains != z5) {
            this.mCongested = !z5;
            log("updateMeteredAndCongested: mCongested changed to " + this.mCongested);
        } else {
            z2 = z;
        }
        if (z2) {
            updateNetworkCapabilities();
        }
        if (this.mTempNotMetered && isInternetSupported()) {
            this.mDataCallSessionStats.onUnmeteredUpdate(networkType);
        }
    }

    private boolean isNetworkTypeUnmetered(int i) {
        List<SubscriptionPlan> subscriptionPlans = this.mDataNetworkController.getSubscriptionPlans();
        if (subscriptionPlans.isEmpty()) {
            return false;
        }
        Set set = (Set) Arrays.stream(TelephonyManager.getAllNetworkTypes()).boxed().collect(Collectors.toSet());
        boolean z = true;
        for (SubscriptionPlan subscriptionPlan : subscriptionPlans) {
            if (((Set) Arrays.stream(subscriptionPlan.getNetworkTypes()).boxed().collect(Collectors.toSet())).containsAll(set)) {
                if (subscriptionPlan.getDataLimitBytes() != Long.MAX_VALUE) {
                    z = false;
                }
            } else if (i != 0) {
                for (int i2 : subscriptionPlan.getNetworkTypes()) {
                    if (i2 == i) {
                        return subscriptionPlan.getDataLimitBytes() == Long.MAX_VALUE;
                    }
                }
            } else {
                continue;
            }
        }
        return z;
    }

    public int getId() {
        return this.mCid.get(this.mTransport);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDataNetworkType() {
        return getDataNetworkType(this.mTransport);
    }

    protected int getDataNetworkType(int i) {
        if (i == 2) {
            return 18;
        }
        NetworkRegistrationInfo networkRegistrationInfo = this.mPhone.getServiceState().getNetworkRegistrationInfo(2, i);
        if (networkRegistrationInfo != null) {
            return networkRegistrationInfo.getAccessNetworkTechnology();
        }
        return 0;
    }

    public int getLinkStatus() {
        return this.mLinkStatus;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkScore() {
        updateNetworkScore(isHandoverInProgress());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkScore(boolean z) {
        if (this.mNetworkScore.getKeepConnectedReason() == z && this.mNetworkScore.isTransportPrimary() == this.mOnPreferredDataPhone) {
            return;
        }
        NetworkScore.Builder keepConnectedReason = new NetworkScore.Builder().setKeepConnectedReason(z ? 1 : 0);
        keepConnectedReason.setTransportPrimary(this.mOnPreferredDataPhone);
        NetworkScore networkScoreBuild = keepConnectedReason.build();
        this.mNetworkScore = networkScoreBuild;
        this.mNetworkAgent.sendNetworkScore(networkScoreBuild);
        logl("updateNetworkScore: isPrimary=" + this.mNetworkScore.isTransportPrimary() + ", keepConnectedForHandover=" + z);
    }

    protected NetworkRegistrationInfo getNetworkRegistrationInfo() {
        NetworkRegistrationInfo networkRegistrationInfo = this.mPhone.getServiceStateTracker().getServiceState().getNetworkRegistrationInfo(2, this.mTransport);
        if (networkRegistrationInfo != null) {
            return networkRegistrationInfo;
        }
        loge("Can't get network registration info for " + AccessNetworkConstants.transportTypeToString(this.mTransport));
        return null;
    }

    public int getApnTypeNetworkCapability() {
        if (!this.mAttachedNetworkRequestList.isEmpty()) {
            return this.mAttachedNetworkRequestList.get(0).getHighestPriorityApnTypeNetworkCapability();
        }
        Stream<Integer> streamFilter = Arrays.stream(getNetworkCapabilities().getCapabilities()).boxed().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataNetwork.$r8$lambda$BI4HB_FzNuvaHm3UrT0LSA7uMWQ((Integer) obj);
            }
        });
        DataConfigManager dataConfigManager = this.mDataConfigManager;
        Objects.requireNonNull(dataConfigManager);
        return streamFilter.max(Comparator.comparingInt(new DataNetwork$$ExternalSyntheticLambda1(dataConfigManager))).orElse(-1).intValue();
    }

    public static /* synthetic */ boolean $r8$lambda$BI4HB_FzNuvaHm3UrT0LSA7uMWQ(Integer num) {
        return DataUtils.networkCapabilityToApnType(num.intValue()) != 0;
    }

    public int getPriority() {
        if (!this.mAttachedNetworkRequestList.isEmpty()) {
            return this.mAttachedNetworkRequestList.get(0).getPriority();
        }
        Stream<Integer> streamBoxed = Arrays.stream(getNetworkCapabilities().getCapabilities()).boxed();
        final DataConfigManager dataConfigManager = this.mDataConfigManager;
        Objects.requireNonNull(dataConfigManager);
        return ((Integer) streamBoxed.map(new Function() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(dataConfigManager.getNetworkCapabilityPriority(((Integer) obj).intValue()));
            }
        }).max(new NetworkTypeController$$ExternalSyntheticLambda0()).orElse(0)).intValue();
    }

    public DataNetworkController.NetworkRequestList getAttachedNetworkRequestList() {
        return this.mAttachedNetworkRequestList;
    }

    public boolean isConnecting() {
        return getCurrentState() == this.mConnectingState;
    }

    public boolean isConnected() {
        return getCurrentState() == this.mConnectedState;
    }

    public boolean isDisconnecting() {
        return getCurrentState() == this.mDisconnectingState;
    }

    public boolean isDisconnected() {
        return getCurrentState() == this.mDisconnectedState;
    }

    public boolean isHandoverInProgress() {
        return getCurrentState() == this.mHandoverState;
    }

    public boolean isSuspended() {
        return getState() == 3;
    }

    public int getTransport() {
        return this.mTransport;
    }

    private int getState() {
        if (getCurrentState() == null || isDisconnected()) {
            return 0;
        }
        if (isConnecting()) {
            return 1;
        }
        if (isConnected()) {
            return this.mSuspended ? 3 : 2;
        }
        if (isDisconnecting()) {
            return 4;
        }
        return isHandoverInProgress() ? 5 : -1;
    }

    public boolean isInternetSupported() {
        return this.mNetworkCapabilities.hasCapability(12);
    }

    public boolean isEmergencySupl() {
        return this.mDataAllowedReason == DataEvaluation.DataAllowedReason.EMERGENCY_SUPL;
    }

    private PreciseDataConnectionState getPreciseDataConnectionState() {
        return new PreciseDataConnectionState.Builder().setTransportType(this.mTransport).setId(this.mCid.get(this.mTransport)).setNetworkAgentId(this.mNetworkAgent.getId()).setState(getState()).setApnSetting(this.mDataProfile.getApnSetting()).setLinkProperties(this.mLinkProperties).setNetworkType(getDataNetworkType()).setFailCause(this.mFailCause).setDefaultQos(this.mDefaultQos).setNetworkValidationStatus(this.mNetworkValidationStatus).build();
    }

    static PreciseDataConnectionState getPreciseDisconnectedDataConnectionState(ApnSetting apnSetting) {
        return new PreciseDataConnectionState.Builder().setTransportType(1).setId(-1).setState(0).setApnSetting(apnSetting).build();
    }

    protected void notifyPreciseDataConnectionState() {
        PreciseDataConnectionState preciseDataConnectionState = getPreciseDataConnectionState();
        PreciseDataConnectionState preciseDataConnectionState2 = this.mPreciseDataConnectionState;
        if (preciseDataConnectionState2 != null && preciseDataConnectionState2.getState() == preciseDataConnectionState.getState() && this.mPreciseDataConnectionState.getNetworkValidationStatus() == preciseDataConnectionState.getNetworkValidationStatus() && this.mPreciseDataConnectionState.getNetId() == preciseDataConnectionState.getNetId()) {
            return;
        }
        this.mPreciseDataConnectionState = preciseDataConnectionState;
        logv("notifyPreciseDataConnectionState=" + preciseDataConnectionState);
        this.mPhone.notifyDataConnection(preciseDataConnectionState);
        this.mIHyperDataManager.onDataNetworkStateChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyImsDataNetwork() {
        int state;
        if (!this.mNetworkCapabilities.hasCapability(4) || (state = getState()) == 5 || state == 3 || state == -1) {
            return;
        }
        int dataNetworkType = getDataNetworkType();
        if (dataNetworkType == 0) {
            dataNetworkType = getLastKnownDataNetworkType();
        }
        int iNetworkTypeToAccessNetworkType = DataUtils.networkTypeToAccessNetworkType(dataNetworkType);
        for (int i : this.mAccessNetworksManager.getAvailableTransports()) {
            ((DataServiceManager) this.mDataServiceManagers.get(i)).notifyImsDataNetwork(iNetworkTypeToAccessNetworkType, state, this.mPhysicalNetworkTransportType, this.mPhysicalNetworkSlotIndex, null);
        }
        this.mLastNotifiedAccessNetwork = iNetworkTypeToAccessNetworkType;
    }

    public boolean startHandover(int i, DataRetryManager.DataHandoverRetryEntry dataHandoverRetryEntry) {
        if (getCurrentState() != null && !isDisconnected() && !isDisconnecting()) {
            sendMessage(obtainMessage(25, 0, i, dataHandoverRetryEntry));
            return true;
        }
        if (dataHandoverRetryEntry != null) {
            dataHandoverRetryEntry.setState(4);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStartHandover(int i, DataRetryManager.DataHandoverRetryEntry dataHandoverRetryEntry) {
        if (this.mTransport == i) {
            log("onStartHandover: The network is already on " + AccessNetworkConstants.transportTypeToString(this.mTransport) + ", handover is not needed.");
            if (dataHandoverRetryEntry != null) {
                dataHandoverRetryEntry.setState(4);
                return;
            }
            return;
        }
        boolean dataRoamingFromRegistration = this.mPhone.getServiceState().getDataRoamingFromRegistration();
        boolean zIsAllowedOnRoaming = MiuiDataControllerStub.get(this.mPhone.getPhoneId()).isAllowedOnRoaming(this.mPhone, this.mPhone.getDataRoamingEnabled() || (dataRoamingFromRegistration && !this.mPhone.getServiceState().getDataRoaming()));
        this.mHandoverDataProfile = this.mDataProfile;
        int dataNetworkType = getDataNetworkType(i);
        if (dataNetworkType != 0 && !this.mAttachedNetworkRequestList.isEmpty()) {
            DataProfile dataProfileForNetworkRequest = this.mDataNetworkController.getDataProfileManager().getDataProfileForNetworkRequest(this.mAttachedNetworkRequestList.get(0), dataNetworkType, this.mSatellite, this.mDataNetworkController.isEsimBootStrapProvisioningActivated(), false);
            if (dataProfileForNetworkRequest != null && dataProfileForNetworkRequest.getApnSetting() != null && this.mDataProfile.getApnSetting() != null && TextUtils.equals(dataProfileForNetworkRequest.getApnSetting().getApnName(), this.mDataProfile.getApnSetting().getApnName()) && !dataProfileForNetworkRequest.equals(this.mDataProfile)) {
                this.mHandoverDataProfile = dataProfileForNetworkRequest;
                log("Used different data profile for handover. " + this.mDataProfile);
            }
        }
        DataProfile dataProfileMtkReplaceDataProfileIfNeeded = mtkReplaceDataProfileIfNeeded(this.mHandoverDataProfile);
        logl("Start handover from " + AccessNetworkConstants.transportTypeToString(this.mTransport) + " to " + AccessNetworkConstants.transportTypeToString(i));
        ((DataServiceManager) this.mDataServiceManagers.get(i)).setupDataCall(DataUtils.networkTypeToAccessNetworkType(getDataNetworkType(i)), dataProfileMtkReplaceDataProfileIfNeeded, dataRoamingFromRegistration, zIsAllowedOnRoaming, 3, this.mLinkProperties, this.mPduSessionId, this.mNetworkSliceInfo, this.mHandoverDataProfile.getTrafficDescriptor(), true, obtainMessage(15, dataHandoverRetryEntry));
        this.mDataNetworkValidationStats.onHandoverAttempted();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onHandoverResponse(int i, DataCallResponse dataCallResponse, DataRetryManager.DataHandoverRetryEntry dataHandoverRetryEntry) {
        logl("onHandoverResponse: resultCode=" + DataServiceCallback.resultCodeToString(i) + ", response=" + dataCallResponse);
        this.mFailCause = getFailCauseFromDataCallResponse(i, dataCallResponse);
        validateDataCallResponse(dataCallResponse, -1);
        int i2 = this.mFailCause;
        SparseArray sparseArray = this.mDataServiceManagers;
        if (i2 == 0) {
            ((DataServiceManager) sparseArray.get(this.mTransport)).deactivateDataCall(this.mCid.get(this.mTransport), 3, null);
            this.mTransport = DataUtils.getTargetTransport(this.mTransport);
            StringBuilder sb = new StringBuilder("DN-");
            sb.append(this.mInitialNetworkAgentId);
            sb.append("-");
            sb.append(this.mTransport == 1 ? "C" : "I");
            this.mLogTag = sb.toString();
            this.mDataProfile = this.mHandoverDataProfile;
            updateDataNetwork(dataCallResponse);
            if (this.mTransport != 1) {
                unregisterForWwanEvents();
            } else {
                registerForWwanEvents();
            }
            if (dataHandoverRetryEntry != null) {
                dataHandoverRetryEntry.setState(3);
            }
            this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onHandoverResponse$14();
                }
            });
        } else {
            ((DataServiceManager) sparseArray.get(this.mTransport)).cancelHandover(this.mCid.get(this.mTransport), obtainMessage(27));
            sendMessageDelayed(12, this.mDataConfigManager.getNetworkHandoverTimeoutMs());
            final long retryDurationMillis = dataCallResponse != null ? dataCallResponse.getRetryDurationMillis() : -1L;
            final int handoverFailureMode = dataCallResponse != null ? dataCallResponse.getHandoverFailureMode() : -1;
            if (dataHandoverRetryEntry != null) {
                dataHandoverRetryEntry.setState(2);
            }
            this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onHandoverResponse$15(retryDurationMillis, handoverFailureMode);
                }
            });
            trackHandoverFailure(dataCallResponse != null ? dataCallResponse.getCause() : this.mFailCause);
        }
        transitionTo(this.mConnectedState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onHandoverResponse$14() {
        this.mDataNetworkCallback.onHandoverSucceeded(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onHandoverResponse$15(long j, int i) {
        this.mDataNetworkCallback.onHandoverFailed(this, this.mFailCause, j, i);
    }

    private void trackHandoverFailure(int i) {
        this.mDataCallSessionStats.onHandoverFailure(i, getDataNetworkType(), getDataNetworkType(DataUtils.getTargetTransport(this.mTransport)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPcoDataChanged(PcoData pcoData) {
        log("onPcoDataChanged: " + pcoData);
        this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onPcoDataChanged$16();
            }
        });
        if (this.mDataProfile.getApnSetting() != null) {
            Iterator it = this.mDataProfile.getApnSetting().getApnTypes().iterator();
            while (it.hasNext()) {
                int iIntValue = ((Integer) it.next()).intValue();
                Intent intent = new Intent("android.telephony.action.CARRIER_SIGNAL_PCO_VALUE");
                intent.putExtra("android.telephony.extra.APN_TYPE", iIntValue);
                intent.putExtra("android.telephony.extra.APN_PROTOCOL", ApnSetting.getProtocolIntFromString(pcoData.bearerProto));
                intent.putExtra("android.telephony.extra.PCO_ID", pcoData.pcoId);
                intent.putExtra("android.telephony.extra.PCO_VALUE", pcoData.contents);
                this.mPhone.getCarrierSignalAgent().notifyCarrierSignalReceivers(intent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPcoDataChanged$16() {
        this.mDataNetworkCallback.onPcoDataChanged(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPcoDataReceived(PcoData pcoData) {
        log("onPcoDataReceived: " + pcoData);
        PcoData pcoData2 = (PcoData) ((Map) this.mPcoData.computeIfAbsent(Integer.valueOf(pcoData.cid), new Function() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return DataNetwork.$r8$lambda$TdtIeibF1W8PMb9YrceHrmYQoWY((Integer) obj);
            }
        })).put(Integer.valueOf(pcoData.pcoId), pcoData);
        if (getId() == -1 || pcoData.cid != getId() || Objects.equals(pcoData2, pcoData)) {
            return;
        }
        onPcoDataChanged(pcoData);
    }

    public static /* synthetic */ Map $r8$lambda$TdtIeibF1W8PMb9YrceHrmYQoWY(Integer num) {
        return new ArrayMap();
    }

    public int getLastKnownDataNetworkType() {
        return this.mLastKnownDataNetworkType;
    }

    public boolean getLastKnownRoamingState() {
        return this.mLastKnownRoamingState;
    }

    public Map<Integer, PcoData> getPcoData() {
        int i = this.mTransport;
        if (i == 2 || this.mCid.get(i) == -1) {
            return Collections.EMPTY_MAP;
        }
        return (Map) this.mPcoData.getOrDefault(Integer.valueOf(this.mCid.get(this.mTransport)), Collections.EMPTY_MAP);
    }

    private VcnNetworkPolicyResult getVcnPolicy(NetworkCapabilities networkCapabilities) {
        VcnManager vcnManager = this.mVcnManager;
        if (vcnManager == null) {
            return null;
        }
        return vcnManager.applyVcnNetworkPolicy(networkCapabilities, getLinkProperties());
    }

    public boolean hasNetworkCapabilityInNetworkRequests(final int i) {
        return this.mAttachedNetworkRequestList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((TelephonyNetworkRequest) obj).hasCapability(i);
            }
        });
    }

    public void requestNetworkValidation(Consumer<Integer> consumer) {
        sendMessage(28, consumer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDataNetworkValidationRequest(Consumer consumer) {
        if (this.mNetworkValidationResultCodeCallback != null) {
            loge("requestNetworkValidation: previous networkValidationRequest is in progress.");
            Objects.requireNonNull(consumer);
            FunctionalUtils.ignoreRemoteException(new DataNetwork$$ExternalSyntheticLambda10(consumer)).accept(3);
        } else {
            this.mNetworkValidationResultCodeCallback = consumer;
            ((DataServiceManager) this.mDataServiceManagers.get(this.mTransport)).requestNetworkValidation(this.mCid.get(this.mTransport), obtainMessage(29));
            this.mDataNetworkValidationStats.onRequestNetworkValidation(this.mDataProfile.getApnSetting() != null ? this.mDataProfile.getApnSetting().getApnTypeBitmask() : 0);
            log("handleDataNetworkValidationRequest, network validation requested");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleErrorDataNetworkValidationRequest(Consumer consumer) {
        loge("handleErrorDataNetworkValidationRequest: DataNetwork is not in Connected state");
        Objects.requireNonNull(consumer);
        FunctionalUtils.ignoreRemoteException(new DataNetwork$$ExternalSyntheticLambda10(consumer)).accept(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDataNetworkValidationRequestResultCode(int i) {
        if (this.mNetworkValidationResultCodeCallback != null) {
            log("handleDataNetworkValidationRequestResultCode, resultCode:" + DataServiceCallback.resultCodeToString(i));
            Consumer consumer = this.mNetworkValidationResultCodeCallback;
            Objects.requireNonNull(consumer);
            FunctionalUtils.ignoreRemoteException(new DataNetwork$$ExternalSyntheticLambda10(consumer)).accept(Integer.valueOf(i));
            this.mNetworkValidationResultCodeCallback = null;
        }
    }

    private void updateValidationStatus(int i) {
        if (this.mNetworkValidationStatus != i) {
            log("updateValidationStatus:" + PreciseDataConnectionState.networkValidationStatusToString(i));
            this.mNetworkValidationStatus = i;
        }
        this.mDataNetworkValidationStats.onUpdateNetworkValidationState(this.mNetworkValidationStatus, getDataNetworkType());
    }

    public static String tearDownReasonToString(int i) {
        switch (i) {
            case 0:
                return "NONE";
            case 1:
                return "CONNECTIVITY_SERVICE_UNWANTED";
            case 2:
                return "SIM_REMOVAL";
            case 3:
                return "AIRPLANE_MODE_ON";
            case 4:
                return "DATA_DISABLED";
            case 5:
                return "NO_LIVE_REQUEST";
            case 6:
                return "RAT_NOT_ALLOWED";
            case 7:
                return "ROAMING_DISABLED";
            case 8:
                return "CONCURRENT_VOICE_DATA_NOT_ALLOWED";
            case 9:
                return "SERVICE_OPTION_NOT_SUPPORTED";
            case 10:
                return "DATA_SERVICE_NOT_READY";
            case 11:
                return "POWER_OFF_BY_CARRIER";
            case 12:
                return "DATA_STALL";
            case 13:
                return "HANDOVER_FAILED";
            case 14:
                return "HANDOVER_NOT_ALLOWED";
            case 15:
                return "VCN_REQUESTED";
            case 16:
                return "VOPS_NOT_SUPPORTED";
            case 17:
                return "DEFAULT_DATA_UNSELECTED";
            case 18:
                return "NOT_IN_SERVICE";
            case 19:
                return "DATA_CONFIG_NOT_READY";
            case 20:
                return "PENDING_TEAR_DOWN_ALL";
            case 21:
                return "NO_SUITABLE_DATA_PROFILE";
            case 22:
                return "CDMA_EMERGENCY_CALLBACK_MODE";
            case 23:
                return "RETRY_SCHEDULED";
            case 24:
                return "DATA_THROTTLED";
            case 25:
                return "DATA_PROFILE_INVALID";
            case 26:
                return "DATA_PROFILE_NOT_PREFERRED";
            case 27:
                return "NOT_ALLOWED_BY_POLICY";
            case 28:
                return "ILLEGAL_STATE";
            case 29:
                return "ONLY_ALLOWED_SINGLE_NETWORK";
            case 30:
                return "PREFERRED_DATA_SWITCHED";
            case 31:
                return "DATA_LIMIT_REACHED";
            case 32:
                return "TRANSPORT_NOT_ALLOWED";
            case 33:
                return "DEVICE_SHUT_DOWN";
            case 34:
                return "UNSUPPORTED_NETWORK_CAPABILITIES";
            default:
                return "UNKNOWN(" + i + ")";
        }
    }

    protected static String eventToString(int i) {
        switch (i) {
            case 1:
                return "EVENT_DATA_CONFIG_UPDATED";
            case 2:
                return "EVENT_ATTACH_NETWORK_REQUEST";
            case 3:
                return "EVENT_DETACH_NETWORK_REQUEST";
            case 4:
                return "EVENT_RADIO_NOT_AVAILABLE";
            case 5:
                return "EVENT_ALLOCATE_PDU_SESSION_ID_RESPONSE";
            case 6:
                return "EVENT_SETUP_DATA_NETWORK_RESPONSE";
            case 7:
                return "EVENT_TEAR_DOWN_NETWORK";
            case 8:
                return "EVENT_DATA_STATE_CHANGED";
            case 9:
                return "EVENT_DATA_NETWORK_TYPE_REG_STATE_CHANGED";
            case 10:
                return "EVENT_DETACH_ALL_NETWORK_REQUESTS";
            case 11:
                return "EVENT_BANDWIDTH_ESTIMATE_FROM_MODEM_CHANGED";
            case 12:
                return "EVENT_CANCEL_HANDOVER_NO_RESPONSE";
            case 13:
                return "EVENT_DISPLAY_INFO_CHANGED";
            case 14:
            default:
                return "Unknown(" + i + ")";
            case 15:
                return "EVENT_HANDOVER_RESPONSE";
            case 16:
                return "EVENT_SUBSCRIPTION_PLAN_OVERRIDE";
            case 17:
                return "EVENT_PCO_DATA_RECEIVED";
            case 18:
                return "EVENT_CARRIER_PRIVILEGED_UIDS_CHANGED";
            case 19:
                return "EVENT_DEACTIVATE_DATA_NETWORK_RESPONSE";
            case 20:
                return "EVENT_STUCK_IN_TRANSIENT_STATE";
            case 21:
                return "EVENT_WAITING_FOR_TEARING_DOWN_CONDITION_MET";
            case 22:
                return "EVENT_VOICE_CALL_STARTED";
            case 23:
                return "EVENT_VOICE_CALL_ENDED";
            case 24:
                return "EVENT_CSS_INDICATOR_CHANGED";
            case 25:
                return "EVENT_NOTIFY_HANDOVER_STARTED";
            case 26:
                return "EVENT_NOTIFY_HANDOVER_STARTED_RESPONSE";
            case 27:
                return "EVENT_NOTIFY_HANDOVER_CANCELLED_RESPONSE";
            case 28:
                return "EVENT_DATA_NETWORK_VALIDATION_REQUESTED";
            case 29:
                return "EVENT_DATA_NETWORK_VALIDATION_RESPONSE";
        }
    }

    @Override // com.android.internal.telephony.StateMachine
    public String toString() {
        StringBuilder sb = new StringBuilder("[DataNetwork: ");
        sb.append(this.mLogTag);
        sb.append(", ");
        sb.append(this.mDataProfile.getApnSetting() != null ? this.mDataProfile.getApnSetting().getApnName() : null);
        sb.append(", state=");
        sb.append(getCurrentState() != null ? getCurrentState().getName() : null);
        sb.append("]");
        return sb.toString();
    }

    public String name() {
        return this.mLogTag;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportAnomaly(String str, String str2) {
        logl(str);
        AnomalyReporter.reportAnomaly(UUID.fromString(str2), str, this.mPhone.getCarrierId());
    }

    @Override // com.android.internal.telephony.StateMachine
    protected void log(String str) {
        String str2;
        String str3 = this.mLogTag;
        StringBuilder sb = new StringBuilder();
        if (getCurrentState() != null) {
            str2 = getCurrentState().getName() + ": ";
        } else {
            str2 = "";
        }
        sb.append(str2);
        sb.append(str);
        Rlog.d(str3, sb.toString());
    }

    @Override // com.android.internal.telephony.StateMachine
    protected void loge(String str) {
        String str2;
        String str3 = this.mLogTag;
        StringBuilder sb = new StringBuilder();
        if (getCurrentState() != null) {
            str2 = getCurrentState().getName() + ": ";
        } else {
            str2 = "";
        }
        sb.append(str2);
        sb.append(str);
        Rlog.e(str3, sb.toString());
    }

    protected void logl(String str) {
        String str2;
        log(str);
        LocalLog localLog = this.mLocalLog;
        StringBuilder sb = new StringBuilder();
        if (getCurrentState() != null) {
            str2 = getCurrentState().getName() + ": ";
        } else {
            str2 = "";
        }
        sb.append(str2);
        sb.append(str);
        localLog.log(sb.toString());
    }

    @Override // com.android.internal.telephony.StateMachine
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        super.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.println("Tag: " + name());
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("mSubId=" + this.mSubId);
        androidUtilIndentingPrintWriter.println("mOnPreferredDataPhone=" + this.mOnPreferredDataPhone);
        androidUtilIndentingPrintWriter.println("mTransport=" + AccessNetworkConstants.transportTypeToString(this.mTransport));
        androidUtilIndentingPrintWriter.println("isSatellite=" + this.mSatellite);
        androidUtilIndentingPrintWriter.println("mLastKnownDataNetworkType=" + TelephonyManager.getNetworkTypeName(this.mLastKnownDataNetworkType));
        androidUtilIndentingPrintWriter.println("WWAN cid=" + this.mCid.get(1));
        androidUtilIndentingPrintWriter.println("WLAN cid=" + this.mCid.get(2));
        androidUtilIndentingPrintWriter.println("mNetworkScore=" + this.mNetworkScore);
        androidUtilIndentingPrintWriter.println("keepConnectedReason=" + this.mNetworkScore.getKeepConnectedReason());
        androidUtilIndentingPrintWriter.println("mDataAllowedReason=" + this.mDataAllowedReason);
        androidUtilIndentingPrintWriter.println("mPduSessionId=" + this.mPduSessionId);
        androidUtilIndentingPrintWriter.println("mDataProfile=" + this.mDataProfile);
        androidUtilIndentingPrintWriter.println("mNetworkCapabilities=" + this.mNetworkCapabilities);
        androidUtilIndentingPrintWriter.println("mLinkProperties=" + this.mLinkProperties);
        androidUtilIndentingPrintWriter.println("mNetworkSliceInfo=" + this.mNetworkSliceInfo);
        androidUtilIndentingPrintWriter.println("mNetworkBandwidth=" + this.mNetworkBandwidth);
        androidUtilIndentingPrintWriter.println("mTcpBufferSizes=" + this.mTcpBufferSizes);
        androidUtilIndentingPrintWriter.println("mTelephonyDisplayInfo=" + this.mTelephonyDisplayInfo);
        androidUtilIndentingPrintWriter.println("mTempNotMeteredSupported=" + this.mTempNotMeteredSupported);
        androidUtilIndentingPrintWriter.println("mTempNotMetered=" + this.mTempNotMetered);
        androidUtilIndentingPrintWriter.println("mCongested=" + this.mCongested);
        androidUtilIndentingPrintWriter.println("mSuspended=" + this.mSuspended);
        androidUtilIndentingPrintWriter.println("mDataCallResponse=" + this.mDataCallResponse);
        androidUtilIndentingPrintWriter.println("mFailCause=" + DataFailCause.toString(this.mFailCause));
        androidUtilIndentingPrintWriter.println("mAdministratorUids=" + Arrays.toString(this.mAdministratorUids));
        androidUtilIndentingPrintWriter.println("mCarrierServicePackageUid=" + this.mCarrierServicePackageUid);
        androidUtilIndentingPrintWriter.println("mEverConnected=" + this.mEverConnected);
        androidUtilIndentingPrintWriter.println("mInvokedDataDeactivation=" + this.mInvokedDataDeactivation);
        androidUtilIndentingPrintWriter.println("Attached network requests:");
        androidUtilIndentingPrintWriter.increaseIndent();
        Iterator<TelephonyNetworkRequest> it = this.mAttachedNetworkRequestList.iterator();
        while (it.hasNext()) {
            androidUtilIndentingPrintWriter.println(it.next());
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("mQosBearerSessions=" + this.mQosBearerSessions);
        this.mNetworkAgent.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.println("Local logs:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("---------------");
    }

    protected DataProfile mtkReplaceDataProfileIfNeeded(DataProfile dataProfile) {
        ApnSetting apnSetting = dataProfile.getApnSetting();
        if (apnSetting != null) {
            int profileId = dataProfile.getProfileId();
            int profileID = this.mAttachedNetworkRequestList.isEmpty() ? 0 : this.mDataNetworkController.getProfileID(this.mAttachedNetworkRequestList.get(0).getHighestPriorityApnTypeNetworkCapability());
            if (profileID != profileId) {
                logl("replaceDataProfileIfNeeded: update profileId from " + profileId + " to " + profileID);
                ApnSetting apnSettingMakeApnSetting = ApnSetting.makeApnSetting(apnSetting);
                apnSettingMakeApnSetting.setProfileId(profileID);
                return new DataProfile.Builder().setApnSetting(apnSettingMakeApnSetting).setTrafficDescriptor(this.mDataProfile.getTrafficDescriptor()).build();
            }
        }
        return dataProfile;
    }
}
