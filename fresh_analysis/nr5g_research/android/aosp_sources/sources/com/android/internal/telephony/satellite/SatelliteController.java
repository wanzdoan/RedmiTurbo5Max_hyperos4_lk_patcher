package com.android.internal.telephony.satellite;

import android.R;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.devicestate.DeviceState;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.DisplayManager;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.ICancellationSignal;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.WorkSource;
import android.provider.Settings;
import android.provider.Telephony;
import android.telecom.TelecomManager;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.DropBoxManagerLoggerBackend;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PersistentLogger;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.satellite.EnableRequestAttributes;
import android.telephony.satellite.INtnSignalStrengthCallback;
import android.telephony.satellite.ISatelliteCapabilitiesCallback;
import android.telephony.satellite.ISatelliteDatagramCallback;
import android.telephony.satellite.ISatelliteModemStateCallback;
import android.telephony.satellite.ISatelliteProvisionStateCallback;
import android.telephony.satellite.ISatelliteTransmissionUpdateCallback;
import android.telephony.satellite.ISelectedNbIotSatelliteSubscriptionCallback;
import android.telephony.satellite.NtnSignalStrength;
import android.telephony.satellite.PlmnSatelliteConfig;
import android.telephony.satellite.PointingUiAppLaunchIntentAttributes;
import android.telephony.satellite.SatelliteAccessConfiguration;
import android.telephony.satellite.SatelliteCapabilities;
import android.telephony.satellite.SatelliteCommunicationAccessStateCallback;
import android.telephony.satellite.SatelliteDatagram;
import android.telephony.satellite.SatelliteManager;
import android.telephony.satellite.SatelliteModemEnableRequestAttributes;
import android.telephony.satellite.SatelliteSubscriberInfo;
import android.telephony.satellite.SatelliteSubscriberProvisionStatus;
import android.telephony.satellite.SatelliteSubscriptionInfo;
import android.telephony.satellite.SystemSelectionSpecifier;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.uwb.UwbManager;
import android.view.Display;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CarrierSignalAgent$$ExternalSyntheticLambda2;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DeviceStateMonitor;
import com.android.internal.telephony.IBooleanConsumer;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.MiuiPhoneStub$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda1;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.SimultaneousCallingTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.TelephonyCountryDetector;
import com.android.internal.telephony.configupdate.ConfigParser;
import com.android.internal.telephony.configupdate.ConfigProviderAdaptor;
import com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver;
import com.android.internal.telephony.data.DataNetwork$$ExternalSyntheticLambda10;
import com.android.internal.telephony.metrics.SmsStats$$ExternalSyntheticLambda0;
import com.android.internal.telephony.metrics.SmsStats$$ExternalSyntheticLambda1;
import com.android.internal.telephony.nano.TelephonyProto$TelephonyEvent;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.metrics.CarrierRoamingSatelliteControllerStats;
import com.android.internal.telephony.satellite.metrics.CarrierRoamingSatelliteSessionStats;
import com.android.internal.telephony.satellite.metrics.ControllerMetricsStats;
import com.android.internal.telephony.satellite.metrics.ProvisionMetricsStats;
import com.android.internal.telephony.satellite.metrics.SessionMetricsStats;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.internal.telephony.util.NetworkStackConstants;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.telephony.util.WorkerThread;
import com.android.internal.util.FunctionalUtils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteController extends Handler {
    public static final String CARRIER_ROAMING_NTN_ALL_SATELLITE_PLMN_SET_KEY = "carrier_roaming_ntn_all_satellite_plmn_set_key";
    protected static final int EVENT_SATELLITE_ACCESS_CONFIGURATION_CHANGED = 89;
    protected static final int EVENT_SATELLITE_REGISTRATION_FAILURE = 54;
    protected static final int EVENT_WAIT_FOR_CELLULAR_MODEM_OFF_TIMED_OUT = 49;
    protected static final int EVENT_WAIT_FOR_SATELLITE_ENABLING_RESPONSE_TIMED_OUT = 39;
    protected static final int EVENT_WAIT_FOR_UPDATE_SATELLITE_ENABLE_ATTRIBUTES_RESPONSE_TIMED_OUT = 52;
    protected static final int EVENT_WAIT_FOR_UPDATE_SYSTEM_SELECTION_CHANNELS_RESPONSE_TIMED_OUT = 63;
    public static final int INVALID_EMERGENCY_CALL_TO_SATELLITE_HANDOVER_TYPE = -1;
    public static final String NTN_SMS_SUPPORTED_BY_MESSAGES_APP_KEY = "ntn_sms_supported_by_messages_app_key";
    public static final int RESULT_RECEIVER_COUNT_ANOMALY_THRESHOLD = 500;
    public static final int SATELLITE_DATA_PLAN_METERED = 0;
    public static final int SATELLITE_DATA_PLAN_UNMETERED = 1;
    public static final int SATELLITE_MODE_ENABLED_FALSE = 0;
    public static final int SATELLITE_MODE_ENABLED_TRUE = 1;
    public static final String SATELLITE_SHARED_PREF = "satellite_shared_pref";
    public static final String SATELLITE_SUBSCRIPTION_ID = "satellite_subscription_id";
    public static final int TIMEOUT_TYPE_DEMO_POINTING_ALIGNED_DURATION_MILLIS = 2;
    public static final int TIMEOUT_TYPE_DEMO_POINTING_NOT_ALIGNED_DURATION_MILLIS = 3;
    public static final int TIMEOUT_TYPE_EMERGENCY_CALL_MONITORING_DURATION_MILLIS = 5;
    public static final int TIMEOUT_TYPE_EVALUATE_ESOS_PROFILES_PRIORITIZATION_DURATION_MILLIS = 4;
    public static final int TIMEOUT_TYPE_LAST_EMERGENCY_CALL_TIME = 6;
    public static final int TIMEOUT_TYPE_WAIT_FOR_SATELLITE_ENABLING_RESPONSE = 1;
    private static SatelliteController sInstance;
    private AlarmManager mAlarmManager;
    private final UUID mAnomalyUnexpectedResultReceiverCountUUID;
    private AtomicBoolean mBTStateEnabled;
    private final BTWifiNFCStateReceiver mBTWifiNFCSateReceiver;
    private final ConcurrentHashMap mCarrierConfigArray;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private final CarrierConfigManager mCarrierConfigManager;
    private Set mCarrierRoamingNtnAllSatellitePlmnSet;
    private final Object mCarrierRoamingNtnAllSatellitePlmnSetLock;
    private CarrierRoamingSatelliteControllerStats mCarrierRoamingSatelliteControllerStats;
    protected final ConcurrentHashMap<Integer, CarrierRoamingSatelliteSessionStats> mCarrierRoamingSatelliteSessionStatsMap;
    private AtomicBoolean mChangeIntentComponent;
    private CommandsInterface mCi;
    private final ConfigProviderAdaptor.Callback mConfigDataUpdatedCallback;
    private String mConfigSatelliteCarrierRoamingEsosProvisionedClass;
    private String mConfigSatelliteGatewayServicePackage;
    private ContentResolver mContentResolver;
    private final Context mContext;
    private final ControllerMetricsStats mControllerMetricsStats;
    private final TelephonyCountryDetector mCountryDetector;
    private List mCtsSatelliteAccessAllowedSubIds;
    protected List<Integer> mCurrentLocationCarrierIds;
    protected List<Integer> mCurrentLocationTagIds;
    private final ConcurrentHashMap mCurrentSatellitePerPlmnConfigurations;
    private final DeviceStateMonitor mDSM;
    private final DatagramController mDatagramController;
    private BroadcastReceiver mDefaultSmsSubscriptionChangedBroadcastReceiver;
    private AtomicInteger mDelayInSendingEventDisplayEmergencyMessage;
    private AtomicLong mDemoPointingAlignedDurationMillis;
    private AtomicLong mDemoPointingNotAlignedDurationMillis;
    private String mDeviceRotationLockToBackupAndRestore;
    private List mDeviceStates;
    private AtomicBoolean mDisableBTOnSatelliteEnabled;
    private AtomicBoolean mDisableNFCOnSatelliteEnabled;
    private AtomicBoolean mDisableUWBOnSatelliteEnabled;
    private AtomicBoolean mDisableWifiOnSatelliteEnabled;
    private DisplayManager mDisplayManager;
    private AtomicLong mEmergencyCallMonitoringDurationMillisForCtsTest;
    private AtomicInteger mEnforcedEmergencyCallToSatelliteHandoverType;
    private ConcurrentHashMap mEntitlementBarredPlmnListPerCarrier;
    ConcurrentHashMap mEntitlementDataPlanMapPerCarrier;
    ConcurrentHashMap mEntitlementDataServicePolicyMapPerCarrier;
    private ConcurrentHashMap mEntitlementPlmnListPerCarrier;
    ConcurrentHashMap mEntitlementServiceTypeMapPerCarrier;
    ConcurrentHashMap mEntitlementVoiceServicePolicyMapPerCarrier;
    private AtomicLong mEvaluateEsosProfilesPrioritizationDurationMillis;
    private final FeatureFlags mFeatureFlags;
    private AtomicBoolean mHasSentBroadcast;
    private AtomicBoolean mIgnorePlmnListFromStorage;
    private final ConcurrentHashMap mInitialized;
    private SubscriptionManager mInjectSubscriptionManager;
    private AtomicBoolean mIsDemoModeEnabled;
    protected AtomicBoolean mIsDeviceProvisioned;
    private AtomicBoolean mIsEmergency;
    private final AtomicBoolean mIsModemEnabledReportingNtnSignalStrength;
    private AtomicBoolean mIsNotificationShowing;
    protected AtomicBoolean mIsRadioOn;
    private final ConcurrentHashMap mIsSatelliteAttachEnabledForCarrierArrayPerSub;
    private AtomicBoolean mIsSatelliteEnabled;
    protected AtomicBoolean mIsSatelliteSupported;
    private AtomicBoolean mIsWifiConnected;
    private ConcurrentHashMap mLastAutoConnectSatelliteNotificationDisplayedTimestamp;
    private String mLastConfiguredIccId;
    private AtomicLong mLastEmergencyCallTime;
    private ConcurrentHashMap mLastEmergencyDialerButtonDisplayedTimestamp;
    private List mLastEvaluatedSubscriberProvisionStatus;
    private ConcurrentHashMap mLastNonEmergencyDialerDialogDisplayedTimestamp;
    private ConcurrentHashMap mLastNotifiedCarrierRoamingNtnSignalStrength;
    private AtomicBoolean mLastNotifiedNtnEligibility;
    private final ConcurrentHashMap mLastNotifiedNtnMode;
    private final ConcurrentHashMap mLastSatelliteDisconnectedTimesMillis;
    private final AtomicBoolean mLatestRequestedStateForNtnSignalStrengthReport;
    protected BroadcastReceiver mLocationServiceStateChangedReceiver;
    private AtomicInteger mMaxAllowedDataModeForCtsTest;
    private final ConcurrentHashMap mMergedPlmnListPerCarrier;
    private AtomicBoolean mNeedsSatellitePointing;
    private AlertDialog mNetworkSelectionModeAutoDialog;
    private AtomicBoolean mNfcStateEnabled;
    private final BroadcastReceiver mNotificationInteractionBroadcastReceiver;
    private NtnSignalStrength mNtnSignalStrength;
    private final ConcurrentHashMap mNtnSignalStrengthChangedListeners;
    private final Object mNtnSignalsStrengthLock;
    private AtomicBoolean mNtnSmsSupportedByMessagesApp;
    private AtomicBoolean mOverriddenDisableSatelliteWhileEnableInProgressSupported;
    private AtomicBoolean mOverriddenIsSatelliteViaOemProvisioned;
    private String mOverriddenSatelliteGatewayServicePackageName;
    private AtomicBoolean mOverrideNtnEligibility;
    private BroadcastReceiver mPackageStateChangedReceiver;
    private PersistentLogger mPersistentLogger;
    private final PointingAppController mPointingAppController;
    private final ProvisionMetricsStats mProvisionMetricsStats;
    protected ConcurrentHashMap<String, Boolean> mProvisionedSubscriberId;
    protected AtomicBoolean mRadioOffRequested;
    private final ConcurrentHashMap mRegionalSatelliteEarfcns;
    private final AtomicBoolean mRegisteredForNtnSignalStrengthChanged;
    private final AtomicBoolean mRegisteredForPendingDatagramCountWithSatelliteService;
    private final AtomicBoolean mRegisteredForSatelliteCapabilitiesChanged;
    private final AtomicBoolean mRegisteredForSatelliteCommunicationAccessStateChanged;
    private final AtomicBoolean mRegisteredForSatelliteModemStateChangedWithSatelliteService;
    private final AtomicBoolean mRegisteredForSatelliteRegistrationFailure;
    private final AtomicBoolean mRegisteredForSatelliteSupportedStateChanged;
    private final AtomicBoolean mRegisteredForTerrestrialNetworkAvailableChanged;
    private final AlarmManager.OnAlarmListener mRegularMetricReportAlarmListener;
    protected ConcurrentHashMap<String, Integer> mResultReceiverCountPerMethodMap;
    protected AtomicInteger mResultReceiverTotalCount;
    private final ConcurrentHashMap mSatModeCapabilitiesForCarrierRoaming;
    protected AtomicBoolean mSatelliteAccessAllowed;
    protected final Object mSatelliteAccessConfigLock;
    protected final ConcurrentHashMap<Integer, Set<Integer>> mSatelliteAttachRestrictionForCarrierArray;
    private SatelliteCapabilities mSatelliteCapabilities;
    private final ConcurrentHashMap mSatelliteCapabilitiesChangedListeners;
    private final Object mSatelliteCapabilitiesLock;
    private final RegistrantList mSatelliteConfigUpdateChangedRegistrants;
    private RequestSatelliteEnabledArgument mSatelliteDisabledRequest;
    ConcurrentHashMap mSatelliteEligibilitySource;
    private AtomicLong mSatelliteEmergencyModeDurationMillis;
    private RequestSatelliteEnabledArgument mSatelliteEnableAttributesUpdateRequest;
    protected final ConcurrentHashMap<Integer, Boolean> mSatelliteEnabledByDefaultForReasonCache;
    private RequestSatelliteEnabledArgument mSatelliteEnabledRequest;
    private final Object mSatelliteEnabledRequestLock;
    private ConcurrentHashMap mSatelliteEntitlementStatusPerCarrier;
    private String mSatelliteGatewayServicePackageName;
    private final SatelliteModemInterface mSatelliteModemInterface;
    private AtomicInteger mSatelliteModemState;
    private SatelliteOptimizedApplicationsTracker mSatelliteOptimizedApplicationsTracker;
    protected Phone mSatellitePhone;
    protected final Object mSatellitePhoneLock;
    private final List mSatellitePlmnListFromOverlayConfig;
    private final ConcurrentHashMap mSatelliteProvisionCallbacks;
    private final ConcurrentHashMap mSatelliteProvisionStateChangedListeners;
    private final ConcurrentHashMap mSatelliteRegistrationFailureListeners;
    private final ConcurrentHashMap mSatelliteServicesSupportedByCarriersFromConfig;
    protected SatelliteSessionController mSatelliteSessionController;
    private final RegistrantList mSatelliteSubIdChangedRegistrants;
    private final ConcurrentHashMap mSatelliteSupportedStateChangedListeners;
    private final ConcurrentHashMap mSatelliteTechPerPlmnForActiveSubId;
    protected final Object mSatelliteTokenProvisionedLock;
    private final ConcurrentHashMap mSelectedNbIotSatelliteSubscriptionChangedListeners;
    protected AtomicInteger mSelectedSatelliteSubId;
    private SessionMetricsStats mSessionMetricsStats;
    private AtomicLong mSessionProcessingTimeStamp;
    private AtomicLong mSessionStartTimeStamp;
    private SharedPreferences mSharedPreferences;
    private AtomicInteger mSimSlotIdForLaunchingT911ConversationThread;
    protected TreeMap<Integer, List<SubscriptionInfo>> mSubsInfoListPerPriority;
    protected ConcurrentHashMap<String, Integer> mSubscriberIdPerSub;
    private final SubscriptionManagerService mSubscriptionManagerService;
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangedListener;
    private final ConcurrentHashMap mSupportedDisasterPlmnsPerCarrierFromConfig;
    private final ConcurrentHashMap mSupportedEmergencyPlmnsPerCarrierFromConfig;
    private final TelecomManager mTelecomManager;
    private final ConcurrentHashMap mTerrestrialNetworkAvailableChangedListeners;
    private AtomicBoolean mUncapMaxAllowedDataMode;
    private final UwbAdapterStateCallback mUwbAdapterStateCallback;
    private AtomicBoolean mUwbStateEnabled;
    private AtomicLong mWaitTimeForSatelliteEnablingResponse;
    private AtomicBoolean mWaitingForDisableSatelliteModemResponse;
    private AtomicBoolean mWaitingForRadioDisabled;
    private AtomicBoolean mWaitingForSatelliteModemOff;
    private final ConcurrentHashMap mWasSatelliteConnectedViaCarrier;
    private AtomicBoolean mWifiStateEnabled;
    private static final boolean DEBUG = !"user".equals(Build.TYPE);
    public static final long DEFAULT_CARRIER_EMERGENCY_CALL_WAIT_FOR_CONNECTION_TIMEOUT_MILLIS = SatelliteSessionController.DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_RECEIVING_MILLIS;
    private static final long REGULAR_METRIC_REPORTING_INTERVAL_MILLIS = 82800000;
    private static final long DELAY_WAITING_SET_NETWORK_SELECTION_AUTO_MILLIS = 1000;
    private static final List DTC_SATELLITE_TECHNOLOGY_LIST = List.of(5, 6);
    private static AtomicLong sNextSatelliteEnableRequestId = new AtomicLong(0);
    private static AtomicLong sNextSystemSelectionChannelsUpdateRequestId = new AtomicLong(0);
    private static final long SATELLITE_MESSAGE_TRIGGER_WINDOW_MS = Duration.ofHours(2).toMillis();

    private static final class ProvisionSatelliteServiceArgument {
        public Consumer<Integer> callback;
        public byte[] provisionData;
        public int subId;
        public String token;
    }

    private long getEvaluateEsosProfilesPrioritizationDurationMillis() {
        return 60000L;
    }

    private boolean isHandoverTypeValid(int i) {
        return i == 1 || i == 2;
    }

    public int mapDataPolicyForMetrics(int i) {
        if (i == 0) {
            return 1;
        }
        if (i != 1) {
            return i != 2 ? 0 : 3;
        }
        return 2;
    }

    public static class SatellitePerPlmnConfiguration {
        public String plmn = "";
        public int handoverType = 0;
        public int connectType = -1;
        public List<Integer> supportedSatelliteTechs = new ArrayList();
        public int currentSatelliteTechnology = 0;

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof SatellitePerPlmnConfiguration) {
                SatellitePerPlmnConfiguration satellitePerPlmnConfiguration = (SatellitePerPlmnConfiguration) obj;
                if (this.plmn.equals(satellitePerPlmnConfiguration.plmn) && this.handoverType == satellitePerPlmnConfiguration.handoverType && this.connectType == satellitePerPlmnConfiguration.connectType && this.supportedSatelliteTechs.equals(satellitePerPlmnConfiguration.supportedSatelliteTechs) && this.currentSatelliteTechnology == satellitePerPlmnConfiguration.currentSatelliteTechnology) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.plmn, Integer.valueOf(this.handoverType), Integer.valueOf(this.connectType), this.supportedSatelliteTechs, Integer.valueOf(this.currentSatelliteTechnology));
        }

        public String toString() {
            return "SatellitePerPlmnConfiguration: {plmn='" + this.plmn + "', handoverType='" + this.handoverType + "', connectType='" + this.connectType + "', supportedSatelliteTechs='" + this.supportedSatelliteTechs + "', currentSatelliteTechnology='" + this.currentSatelliteTechnology + "'}";
        }
    }

    private void handlePackageChangeEvent() {
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (activeSubIdList != null) {
            for (int i : activeSubIdList) {
                plogd("mPackageStateChangedReceiver: activeSubId= " + i);
                handleCarrierRoamingNtnAvailableServicesChanged(i);
            }
            return;
        }
        ploge("mPackageStateChangedReceiver: activeSubIds is null");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        plogd("onAlarm: regular metric report hysteresis timer expired");
        getInstance().obtainMessage(53, null).sendToTarget();
    }

    private UUID generateAnomalyUnexpectedResultReceiverCountUUID(int i, int i2) {
        return new UUID(this.mAnomalyUnexpectedResultReceiverCountUUID.getMostSignificantBits(), this.mAnomalyUnexpectedResultReceiverCountUUID.getLeastSignificantBits() + (((long) i2) << 32) + ((long) i));
    }

    public void incrementResultReceiverCount(String str) {
        int iIncrementAndGet = this.mResultReceiverTotalCount.incrementAndGet();
        logd("[incrementResultReceiverCount] : " + str + " | ResultReceiver total count= " + iIncrementAndGet);
        this.mResultReceiverCountPerMethodMap.compute(str, new BiFunction() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda9
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Integer num = (Integer) obj2;
                return Integer.valueOf(num != null ? 1 + num.intValue() : 1);
            }
        });
        if (iIncrementAndGet > 500) {
            loge("[mResultReceiverTotalCount] is exceeds limits : " + iIncrementAndGet);
            loge("[incrementResultReceiverCount] mResultReceiverCountPerMethodMap is " + this.mResultReceiverCountPerMethodMap);
            AnomalyReporter.reportAnomaly(generateAnomalyUnexpectedResultReceiverCountUUID(0, 0), "Satellite ResultReceiver total count= " + iIncrementAndGet + " exceeds limit.");
        }
    }

    public void decrementResultReceiverCount(String str) {
        if (this.mResultReceiverTotalCount.get() > 0) {
            this.mResultReceiverTotalCount.decrementAndGet();
        }
        logd("[decrementResultReceiverCount] : " + str + " | ResultReceiver total count=" + this.mResultReceiverTotalCount.get());
        this.mResultReceiverCountPerMethodMap.computeIfPresent(str, new BiFunction() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda36
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Integer num = (Integer) obj2;
                return Integer.valueOf(num.intValue() > 0 ? num.intValue() - 1 : num.intValue());
            }
        });
    }

    public static SatelliteController getInstance() {
        if (sInstance == null) {
            loge("SatelliteController was not yet initialized.");
        }
        return sInstance;
    }

    public static void make(Context context, FeatureFlags featureFlags) {
        if (sInstance == null) {
            sInstance = new SatelliteController(context, WorkerThread.get().getLooper(), featureFlags);
        }
    }

    public SatelliteController(Context context, Looper looper, FeatureFlags featureFlags) {
        super(looper);
        this.mBTStateEnabled = new AtomicBoolean(false);
        this.mNfcStateEnabled = new AtomicBoolean(false);
        this.mUwbStateEnabled = new AtomicBoolean(false);
        this.mWifiStateEnabled = new AtomicBoolean(false);
        this.mDisableBTOnSatelliteEnabled = new AtomicBoolean(false);
        this.mDisableNFCOnSatelliteEnabled = new AtomicBoolean(false);
        this.mDisableUWBOnSatelliteEnabled = new AtomicBoolean(false);
        this.mDisableWifiOnSatelliteEnabled = new AtomicBoolean(false);
        this.mIsSatelliteSupported = null;
        this.mNeedsSatellitePointing = new AtomicBoolean(false);
        this.mIsDemoModeEnabled = new AtomicBoolean(false);
        this.mIsEmergency = new AtomicBoolean(false);
        this.mIsSatelliteEnabled = null;
        this.mIsRadioOn = new AtomicBoolean(false);
        this.mRadioOffRequested = new AtomicBoolean(false);
        this.mIsDeviceProvisioned = null;
        this.mOverriddenIsSatelliteViaOemProvisioned = null;
        this.mIgnorePlmnListFromStorage = new AtomicBoolean(false);
        this.mNtnSmsSupportedByMessagesApp = null;
        this.mIsWifiConnected = new AtomicBoolean(false);
        this.mHasSentBroadcast = new AtomicBoolean(false);
        this.mSatelliteModemState = new AtomicInteger(-1);
        this.mWaitingForRadioDisabled = new AtomicBoolean(false);
        this.mWaitingForDisableSatelliteModemResponse = new AtomicBoolean(false);
        this.mWaitingForSatelliteModemOff = new AtomicBoolean(false);
        this.mLastNotifiedNtnEligibility = null;
        this.mChangeIntentComponent = new AtomicBoolean(false);
        this.mIsNotificationShowing = new AtomicBoolean(false);
        this.mSatelliteAccessAllowed = new AtomicBoolean(false);
        this.mOverriddenDisableSatelliteWhileEnableInProgressSupported = null;
        this.mWaitTimeForSatelliteEnablingResponse = new AtomicLong(0L);
        this.mDemoPointingAlignedDurationMillis = new AtomicLong(0L);
        this.mDemoPointingNotAlignedDurationMillis = new AtomicLong(0L);
        this.mEvaluateEsosProfilesPrioritizationDurationMillis = new AtomicLong(0L);
        this.mLastEmergencyCallTime = new AtomicLong(0L);
        this.mSatelliteEmergencyModeDurationMillis = new AtomicLong(0L);
        this.mEmergencyCallMonitoringDurationMillisForCtsTest = new AtomicLong(0L);
        this.mSessionStartTimeStamp = new AtomicLong(0L);
        this.mSessionProcessingTimeStamp = new AtomicLong(0L);
        this.mEnforcedEmergencyCallToSatelliteHandoverType = new AtomicInteger(-1);
        this.mDelayInSendingEventDisplayEmergencyMessage = new AtomicInteger(0);
        this.mSimSlotIdForLaunchingT911ConversationThread = new AtomicInteger(0);
        this.mMaxAllowedDataModeForCtsTest = new AtomicInteger(-1);
        this.mUncapMaxAllowedDataMode = new AtomicBoolean(false);
        this.mSelectedSatelliteSubId = new AtomicInteger(-1);
        this.mResultReceiverTotalCount = new AtomicInteger(0);
        this.mSatelliteEnabledByDefaultForReasonCache = new ConcurrentHashMap<>();
        Object obj = new Object();
        this.mSatellitePhoneLock = obj;
        this.mSatellitePhone = null;
        this.mSatelliteCapabilitiesLock = new Object();
        this.mNtnSignalsStrengthLock = new Object();
        this.mNtnSignalStrength = new NtnSignalStrength(0);
        this.mCarrierRoamingNtnAllSatellitePlmnSetLock = new Object();
        this.mCarrierRoamingNtnAllSatellitePlmnSet = null;
        this.mSatelliteAccessConfigLock = new Object();
        this.mCurrentLocationTagIds = new ArrayList();
        this.mCurrentLocationCarrierIds = new ArrayList();
        this.mSatelliteEnabledRequestLock = new Object();
        this.mSatelliteEnabledRequest = null;
        this.mSatelliteDisabledRequest = null;
        this.mSatelliteEnableAttributesUpdateRequest = null;
        this.mSatelliteTokenProvisionedLock = new Object();
        this.mSubsInfoListPerPriority = new TreeMap<>();
        this.mLastEvaluatedSubscriberProvisionStatus = new ArrayList();
        this.mRegisteredForPendingDatagramCountWithSatelliteService = new AtomicBoolean(false);
        this.mRegisteredForSatelliteModemStateChangedWithSatelliteService = new AtomicBoolean(false);
        this.mRegisteredForNtnSignalStrengthChanged = new AtomicBoolean(false);
        this.mRegisteredForSatelliteCapabilitiesChanged = new AtomicBoolean(false);
        this.mIsModemEnabledReportingNtnSignalStrength = new AtomicBoolean(false);
        this.mLatestRequestedStateForNtnSignalStrengthReport = new AtomicBoolean(false);
        this.mRegisteredForSatelliteSupportedStateChanged = new AtomicBoolean(false);
        this.mRegisteredForSatelliteRegistrationFailure = new AtomicBoolean(false);
        this.mRegisteredForTerrestrialNetworkAvailableChanged = new AtomicBoolean(false);
        this.mRegisteredForSatelliteCommunicationAccessStateChanged = new AtomicBoolean(false);
        this.mCarrierConfigArray = new ConcurrentHashMap();
        this.mSatelliteProvisionCallbacks = new ConcurrentHashMap();
        this.mSatelliteProvisionStateChangedListeners = new ConcurrentHashMap();
        this.mNtnSignalStrengthChangedListeners = new ConcurrentHashMap();
        this.mSatelliteCapabilitiesChangedListeners = new ConcurrentHashMap();
        this.mSatelliteSupportedStateChangedListeners = new ConcurrentHashMap();
        this.mSatelliteRegistrationFailureListeners = new ConcurrentHashMap();
        this.mTerrestrialNetworkAvailableChangedListeners = new ConcurrentHashMap();
        this.mSelectedNbIotSatelliteSubscriptionChangedListeners = new ConcurrentHashMap();
        this.mResultReceiverCountPerMethodMap = new ConcurrentHashMap<>();
        this.mSatelliteServicesSupportedByCarriersFromConfig = new ConcurrentHashMap();
        this.mSupportedEmergencyPlmnsPerCarrierFromConfig = new ConcurrentHashMap();
        this.mSupportedDisasterPlmnsPerCarrierFromConfig = new ConcurrentHashMap();
        this.mCarrierRoamingSatelliteSessionStatsMap = new ConcurrentHashMap<>();
        this.mSatModeCapabilitiesForCarrierRoaming = new ConcurrentHashMap();
        this.mSatelliteAttachRestrictionForCarrierArray = new ConcurrentHashMap<>();
        this.mIsSatelliteAttachEnabledForCarrierArrayPerSub = new ConcurrentHashMap();
        this.mRegionalSatelliteEarfcns = new ConcurrentHashMap();
        this.mProvisionedSubscriberId = new ConcurrentHashMap<>();
        this.mSubscriberIdPerSub = new ConcurrentHashMap<>();
        this.mLastSatelliteDisconnectedTimesMillis = new ConcurrentHashMap();
        this.mWasSatelliteConnectedViaCarrier = new ConcurrentHashMap();
        this.mLastNotifiedNtnMode = new ConcurrentHashMap();
        this.mInitialized = new ConcurrentHashMap();
        this.mLastNotifiedCarrierRoamingNtnSignalStrength = new ConcurrentHashMap();
        this.mLastNonEmergencyDialerDialogDisplayedTimestamp = new ConcurrentHashMap();
        this.mLastEmergencyDialerButtonDisplayedTimestamp = new ConcurrentHashMap();
        this.mLastAutoConnectSatelliteNotificationDisplayedTimestamp = new ConcurrentHashMap();
        this.mSharedPreferences = null;
        this.mPersistentLogger = null;
        this.mCurrentSatellitePerPlmnConfigurations = new ConcurrentHashMap();
        this.mSatelliteTechPerPlmnForActiveSubId = new ConcurrentHashMap();
        this.mSatelliteEntitlementStatusPerCarrier = new ConcurrentHashMap();
        this.mEntitlementPlmnListPerCarrier = new ConcurrentHashMap();
        this.mEntitlementBarredPlmnListPerCarrier = new ConcurrentHashMap();
        this.mMergedPlmnListPerCarrier = new ConcurrentHashMap();
        this.mEntitlementDataPlanMapPerCarrier = new ConcurrentHashMap();
        this.mEntitlementServiceTypeMapPerCarrier = new ConcurrentHashMap();
        this.mEntitlementDataServicePolicyMapPerCarrier = new ConcurrentHashMap();
        this.mEntitlementVoiceServicePolicyMapPerCarrier = new ConcurrentHashMap();
        this.mSatelliteEligibilitySource = new ConcurrentHashMap();
        this.mNetworkSelectionModeAutoDialog = null;
        this.mSatelliteConfigUpdateChangedRegistrants = new RegistrantList();
        this.mSatelliteSubIdChangedRegistrants = new RegistrantList();
        this.mCtsSatelliteAccessAllowedSubIds = null;
        this.mDeviceRotationLockToBackupAndRestore = null;
        this.mInjectSubscriptionManager = null;
        this.mConfigSatelliteGatewayServicePackage = "";
        this.mConfigSatelliteCarrierRoamingEsosProvisionedClass = "";
        this.mSatelliteGatewayServicePackageName = "";
        this.mOverriddenSatelliteGatewayServicePackageName = "";
        this.mDefaultSmsSubscriptionChangedBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.satellite.SatelliteController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.telephony.action.DEFAULT_SMS_SUBSCRIPTION_CHANGED")) {
                    SatelliteController.this.plogd("Default SMS subscription changed");
                    SatelliteController.this.sendRequestAsync(43, null, null);
                }
            }
        };
        this.mPackageStateChangedReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.satellite.SatelliteController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(SatelliteController.this.mContext);
                SatelliteController satelliteController = SatelliteController.this;
                satelliteController.mSatelliteGatewayServicePackageName = satelliteController.getConfigSatelliteGatewayServicePackage();
                if ("android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED_INTERNAL".equals(intent.getAction())) {
                    SatelliteController.this.plogd("Default SMS package changed internally, re-evaluating services.");
                } else {
                    String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
                    SatelliteController.this.plogd("packageStateChanged: " + intent.getData().toString() + " defaultSmsPackageName: " + defaultSmsPackage + " satelliteGatewayServicePackageName: " + SatelliteController.this.mSatelliteGatewayServicePackageName);
                    if (!schemeSpecificPart.equals(SatelliteController.this.mSatelliteGatewayServicePackageName) && !schemeSpecificPart.equals(defaultSmsPackage)) {
                        return;
                    } else {
                        SatelliteController.this.plogd("Changed SMS or SatelliteGateway package");
                    }
                }
                if (SatelliteController.this.hasMessages(94)) {
                    return;
                }
                SatelliteController satelliteController2 = SatelliteController.this;
                satelliteController2.sendMessageDelayed(satelliteController2.obtainMessage(94), 100L);
            }
        };
        this.mLocationServiceStateChangedReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.satellite.SatelliteController.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.location.MODE_CHANGED")) {
                    SatelliteController.this.plogd("mLocationServiceStateChangedReceiver");
                    SatelliteController.this.sendRequestAsync(62, null, null);
                }
            }
        };
        this.mDeviceStates = new ArrayList();
        this.mRegularMetricReportAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda22
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$new$0();
            }
        };
        this.mAnomalyUnexpectedResultReceiverCountUUID = UUID.fromString("e268f22d-9bba-4d27-b76a-1c7f5b42e241");
        this.mNotificationInteractionBroadcastReceiver = new AnonymousClass21();
        this.mPersistentLogger = SatelliteServiceUtils.getPersistentLogger(context);
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        Phone phone = SatelliteServiceUtils.getPhone();
        synchronized (obj) {
            this.mSatellitePhone = phone;
        }
        this.mCi = phone.mCi;
        DeviceStateMonitor deviceStateMonitor = phone.getDeviceStateMonitor();
        this.mDSM = deviceStateMonitor;
        this.mSatelliteModemInterface = SatelliteModemInterface.make(context, this, featureFlags);
        this.mControllerMetricsStats = ControllerMetricsStats.make(context);
        TelephonyCountryDetector telephonyCountryDetector = TelephonyCountryDetector.getInstance(context, featureFlags);
        this.mCountryDetector = telephonyCountryDetector;
        telephonyCountryDetector.registerForWifiConnectivityStateChanged(this, 47, null);
        this.mTelecomManager = (TelecomManager) context.getSystemService(TelecomManager.class);
        PointingAppController pointingAppControllerMake = PointingAppController.make(context, featureFlags);
        this.mPointingAppController = pointingAppControllerMake;
        this.mProvisionMetricsStats = ProvisionMetricsStats.getOrCreateInstance();
        this.mSessionMetricsStats = SessionMetricsStats.getInstance();
        this.mCarrierRoamingSatelliteControllerStats = CarrierRoamingSatelliteControllerStats.getOrCreateInstance();
        this.mSubscriptionManagerService = SubscriptionManagerService.getInstance();
        this.mDatagramController = DatagramController.make(context, looper, featureFlags, pointingAppControllerMake);
        this.mCi.registerForRadioStateChanged(this, 23, null);
        this.mIsRadioOn.set(phone.isRadioOn());
        registerForPendingDatagramCount();
        registerForSatelliteModemStateChanged();
        registerForServiceStateChanged();
        registerForSignalStrengthChanged();
        registerForSatelliteCommunicationAccessStateChanged();
        this.mContentResolver = context.getContentResolver();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mCarrierConfigManager = carrierConfigManager;
        this.mBTWifiNFCSateReceiver = new BTWifiNFCStateReceiver();
        this.mUwbAdapterStateCallback = new UwbAdapterStateCallback();
        initializeSatelliteModeRadios();
        ContentObserver contentObserver = new ContentObserver(this) { // from class: com.android.internal.telephony.satellite.SatelliteController.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                SatelliteController.this.initializeSatelliteModeRadios();
            }
        };
        ContentResolver contentResolver = this.mContentResolver;
        if (contentResolver != null) {
            contentResolver.registerContentObserver(Settings.Global.getUriFor("satellite_mode_radios"), false, contentObserver);
        }
        this.mSatellitePlmnListFromOverlayConfig = readSatellitePlmnsFromOverlayConfig();
        registerApplicationStateChanged();
        registerLocationServiceStateChanged();
        updateSupportedSatelliteServicesForActiveSubscriptions();
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda23
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                this.f$0.lambda$new$3(i, i2, i3, i4);
            }
        };
        this.mCarrierConfigChangeListener = carrierConfigChangeListener;
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new HandlerExecutor(new Handler(looper)), carrierConfigChangeListener);
        }
        ConfigProviderAdaptor.Callback callback = new ConfigProviderAdaptor.Callback() { // from class: com.android.internal.telephony.satellite.SatelliteController.5
            @Override // com.android.internal.telephony.configupdate.ConfigProviderAdaptor.Callback
            public void onChanged(ConfigParser configParser) {
                SatelliteController.this.sendRequestAsync(40, new SatelliteControllerHandlerRequest(Boolean.TRUE, SatelliteServiceUtils.getPhone()), null);
            }
        };
        this.mConfigDataUpdatedCallback = callback;
        TelephonyConfigUpdateInstallReceiver.getInstance().registerCallback(Executors.newSingleThreadExecutor(), callback);
        deviceStateMonitor.registerForSignalStrengthReportDecision(this, 35, null);
        deviceStateMonitor.registerForScreenStateChanged(this, 95, null);
        deviceStateMonitor.registerForChargingStateChanged(this, 98, null);
        loadSatelliteSharedPreferences();
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences != null) {
            try {
                setNtnSmsSupportedByMessagesAppCache(sharedPreferences.getBoolean(NTN_SMS_SUPPORTED_BY_MESSAGES_APP_KEY, false));
                setCarrierRoamingNtnAllSatellitePlmnSet(this.mSharedPreferences.getStringSet(CARRIER_ROAMING_NTN_ALL_SATELLITE_PLMN_SET_KEY, new HashSet()));
            } catch (Exception e) {
                plogd("SatelliteController constructor: cannot get default shared preferences. e" + e);
            }
        }
        this.mWaitTimeForSatelliteEnablingResponse.set(getWaitForSatelliteEnablingResponseTimeoutMillis());
        this.mDemoPointingAlignedDurationMillis.set(getDemoPointingAlignedDurationMillisFromResources());
        this.mDemoPointingNotAlignedDurationMillis.set(getDemoPointingNotAlignedDurationMillisFromResources());
        this.mSatelliteEmergencyModeDurationMillis.set(getSatelliteEmergencyModeDurationFromOverlayConfig(context));
        this.mEvaluateEsosProfilesPrioritizationDurationMillis.set(getEvaluateEsosProfilesPrioritizationDurationMillis());
        sendMessageDelayed(obtainMessage(43), this.mEvaluateEsosProfilesPrioritizationDurationMillis.get());
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        SatelliteSubscriptionsChangedListener satelliteSubscriptionsChangedListener = new SatelliteSubscriptionsChangedListener();
        this.mSubscriptionsChangedListener = satelliteSubscriptionsChangedListener;
        if (subscriptionManager != null) {
            subscriptionManager.addOnSubscriptionsChangedListener(new HandlerExecutor(new Handler(looper)), satelliteSubscriptionsChangedListener);
        }
        registerDefaultSmsSubscriptionChangedBroadcastReceiver();
        updateSatelliteProvisionedStatePerSubscriberId();
        this.mDeviceStates = getSupportedDeviceStates();
        this.mSatelliteOptimizedApplicationsTracker = new SatelliteOptimizedApplicationsTracker(getLooper(), this.mContext);
        for (Phone phone2 : PhoneFactory.getPhones()) {
            sendRequestAsync(64, null, phone2);
        }
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService(AlarmManager.class);
        scheduleRegularMetricReportTimer();
        logd("Satellite Tracker is created");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(int i, int i2, int i3, int i4) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Integer.valueOf(i);
        someArgsObtain.arg2 = Integer.valueOf(i2);
        someArgsObtain.arg3 = Integer.valueOf(i3);
        someArgsObtain.arg4 = Integer.valueOf(i4);
        sendMessage(obtainMessage(92, someArgsObtain));
    }

    class SatelliteSubscriptionsChangedListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        SatelliteSubscriptionsChangedListener() {
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            SatelliteController.this.handleSubscriptionsChanged();
        }
    }

    public void registerForConfigUpdateChanged(Handler handler, int i, Object obj) {
        this.mSatelliteConfigUpdateChangedRegistrants.add(new Registrant(handler, i, obj));
    }

    public void unregisterForConfigUpdateChanged(Handler handler) {
        this.mSatelliteConfigUpdateChangedRegistrants.remove(handler);
    }

    public void registerForSatelliteSubIdChanged(Handler handler, int i, Object obj) {
        this.mSatelliteSubIdChangedRegistrants.add(new Registrant(handler, i, obj));
    }

    public void unregisterForSatelliteSubIdChanged(Handler handler) {
        this.mSatelliteSubIdChangedRegistrants.remove(handler);
    }

    public SatelliteConfig getSatelliteConfig() {
        SatelliteConfigParser satelliteConfigParser = getSatelliteConfigParser();
        if (satelliteConfigParser == null) {
            Log.v("SatelliteController", "satelliteConfigParser is not ready");
            return null;
        }
        SatelliteConfig config = satelliteConfigParser.getConfig();
        logd("getSatelliteConfig: satelliteConfig: " + config);
        return config;
    }

    public void cleanUpTelephonyConfigs() {
        TelephonyConfigUpdateInstallReceiver.getInstance().cleanUpTelephonyConfigs();
    }

    public SatelliteConfigParser getSatelliteConfigParser() {
        return (SatelliteConfigParser) TelephonyConfigUpdateInstallReceiver.getInstance().getConfigParser(ConfigProviderAdaptor.DOMAIN_SATELLITE);
    }

    protected void initializeSatelliteModeRadios() {
        UwbManager uwbManager;
        WifiManager wifiManager;
        BluetoothAdapter defaultAdapter;
        if (this.mContentResolver != null) {
            IntentFilter intentFilter = new IntentFilter();
            this.mDisableBTOnSatelliteEnabled.set(false);
            this.mDisableNFCOnSatelliteEnabled.set(false);
            this.mDisableWifiOnSatelliteEnabled.set(false);
            this.mDisableUWBOnSatelliteEnabled.set(false);
            this.mBTStateEnabled.set(false);
            this.mNfcStateEnabled.set(false);
            this.mWifiStateEnabled.set(false);
            this.mUwbStateEnabled.set(false);
            String string = Settings.Global.getString(this.mContentResolver, "satellite_mode_radios");
            if (string == null) {
                ploge("initializeSatelliteModeRadios: satelliteModeRadios is null");
                return;
            }
            plogd("Radios To be checked when satellite is on: ".concat(string));
            if (string.contains("bluetooth") && (defaultAdapter = BluetoothAdapter.getDefaultAdapter()) != null) {
                this.mDisableBTOnSatelliteEnabled.set(true);
                this.mBTStateEnabled.set(defaultAdapter.isEnabled());
                intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            }
            if (string.contains("nfc")) {
                NfcAdapter defaultAdapter2 = this.mContext.getApplicationContext() != null ? NfcAdapter.getDefaultAdapter(this.mContext.getApplicationContext()) : null;
                if (defaultAdapter2 != null) {
                    this.mDisableNFCOnSatelliteEnabled.set(true);
                    this.mNfcStateEnabled.set(defaultAdapter2.isEnabled());
                    intentFilter.addAction("android.nfc.action.ADAPTER_STATE_CHANGED");
                }
            }
            if (string.contains("wifi") && (wifiManager = (WifiManager) this.mContext.getSystemService(WifiManager.class)) != null) {
                this.mDisableWifiOnSatelliteEnabled.set(true);
                this.mWifiStateEnabled.set(wifiManager.isWifiEnabled());
                intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            }
            try {
                this.mContext.unregisterReceiver(this.mBTWifiNFCSateReceiver);
            } catch (IllegalArgumentException e) {
                plogd("initializeSatelliteModeRadios: unregisterReceiver, e=" + e);
            }
            this.mContext.registerReceiver(this.mBTWifiNFCSateReceiver, intentFilter);
            if (string.contains("uwb") && (uwbManager = (UwbManager) this.mContext.getSystemService(UwbManager.class)) != null) {
                this.mDisableUWBOnSatelliteEnabled.set(true);
                this.mUwbStateEnabled.set(uwbManager.isUwbEnabled());
                long jClearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    uwbManager.unregisterAdapterStateCallback(this.mUwbAdapterStateCallback);
                    uwbManager.registerAdapterStateCallback(this.mContext.getMainExecutor(), this.mUwbAdapterStateCallback);
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    throw th;
                }
            }
            plogd("mDisableBTOnSatelliteEnabled: " + this.mDisableBTOnSatelliteEnabled.get() + " mDisableNFCOnSatelliteEnabled: " + this.mDisableNFCOnSatelliteEnabled.get() + " mDisableWifiOnSatelliteEnabled: " + this.mDisableWifiOnSatelliteEnabled.get() + " mDisableUWBOnSatelliteEnabled: " + this.mDisableUWBOnSatelliteEnabled.get());
            plogd("mBTStateEnabled: " + this.mBTStateEnabled.get() + " mNfcStateEnabled: " + this.mNfcStateEnabled.get() + " mWifiStateEnabled: " + this.mWifiStateEnabled.get() + " mUwbStateEnabled: " + this.mUwbStateEnabled.get());
        }
    }

    protected class UwbAdapterStateCallback implements UwbManager.AdapterStateCallback {
        protected UwbAdapterStateCallback() {
        }

        public String toString(int i) {
            if (i == 0) {
                return "Disabled";
            }
            if (i == 1) {
                return "Inactive";
            }
            if (i == 2) {
                return "Active";
            }
            return "";
        }

        public void onStateChanged(int i, int i2) {
            SatelliteController.this.plogd("UwbAdapterStateCallback#onStateChanged() called, state = " + toString(i));
            SatelliteController.this.plogd("Adapter state changed reason " + String.valueOf(i2));
            FeatureFlags unused = SatelliteController.this.mFeatureFlags;
            SatelliteController satelliteController = SatelliteController.this;
            satelliteController.sendMessage(satelliteController.obtainMessage(91, Integer.valueOf(i)));
        }
    }

    private void handleEventUwbStateChanged(int i) {
        if (i == 0) {
            setUwbEnabledState(false);
            evaluateToSendSatelliteEnabledSuccess();
        } else {
            setUwbEnabledState(true);
        }
        plogd("mUwbStateEnabled: " + getUwbEnabledState());
    }

    protected class BTWifiNFCStateReceiver extends BroadcastReceiver {
        protected BTWifiNFCStateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            FeatureFlags unused = SatelliteController.this.mFeatureFlags;
            SatelliteController satelliteController = SatelliteController.this;
            satelliteController.sendMessage(satelliteController.obtainMessage(90, intent));
        }
    }

    private void handleEventBtWifiNfcStateChanged(Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            plogd("BTWifiNFCStateReceiver NULL action for intent " + intent);
        }
        plogd("handleEventBtWifiNfcStateChanged: action=".concat(action));
        action.hashCode();
        switch (action) {
            case "android.net.wifi.WIFI_STATE_CHANGED":
                int intExtra = intent.getIntExtra("wifi_state", 4);
                boolean wifiEnabledState = getWifiEnabledState();
                if (intExtra == 3) {
                    setWifiEnabledState(true);
                } else if (intExtra == 1) {
                    setWifiEnabledState(false);
                    evaluateToSendSatelliteEnabledSuccess();
                }
                if (wifiEnabledState != getWifiEnabledState()) {
                    plogd("mWifiStateEnabled=" + getWifiEnabledState());
                    break;
                }
                break;
            case "android.bluetooth.adapter.action.STATE_CHANGED":
                int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_ROUTER);
                boolean bTEnabledState = getBTEnabledState();
                if (intExtra2 == 10) {
                    setBTEnabledState(false);
                    evaluateToSendSatelliteEnabledSuccess();
                } else if (intExtra2 == 12) {
                    setBTEnabledState(true);
                }
                if (bTEnabledState != getBTEnabledState()) {
                    plogd("mBTStateEnabled=" + getBTEnabledState());
                    break;
                }
                break;
            case "android.nfc.action.ADAPTER_STATE_CHANGED":
                int intExtra3 = intent.getIntExtra("android.nfc.extra.ADAPTER_STATE", -1);
                boolean nfcEnabledState = getNfcEnabledState();
                if (intExtra3 == 3) {
                    setNfcEnabledState(true);
                } else if (intExtra3 == 1) {
                    setNfcEnabledState(false);
                    evaluateToSendSatelliteEnabledSuccess();
                }
                if (nfcEnabledState != getNfcEnabledState()) {
                    plogd("mNfcStateEnabled=" + getNfcEnabledState());
                    break;
                }
                break;
        }
    }

    public static final class SatelliteControllerHandlerRequest {
        public Object argument;
        public Phone phone;
        public Object result;

        public SatelliteControllerHandlerRequest(Object obj, Phone phone) {
            this.argument = obj;
            this.phone = phone;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class RequestSatelliteEnabledArgument {
        public Consumer<Integer> callback;
        public boolean enableDemoMode;
        public boolean enableSatellite;
        public boolean isEmergency;
        public long requestId = SatelliteController.sNextSatelliteEnableRequestId.getAndUpdate(new LongUnaryOperator() { // from class: com.android.internal.telephony.satellite.SatelliteController$RequestSatelliteEnabledArgument$$ExternalSyntheticLambda0
            @Override // java.util.function.LongUnaryOperator
            public final long applyAsLong(long j) {
                return SatelliteController.RequestSatelliteEnabledArgument.m1590$r8$lambda$pIt52EEe965YwytPo_u_PahKbk(j);
            }
        });

        RequestSatelliteEnabledArgument(boolean z, boolean z2, boolean z3, Consumer consumer) {
            this.enableSatellite = z;
            this.enableDemoMode = z2;
            this.isEmergency = z3;
            this.callback = consumer;
        }

        /* JADX INFO: renamed from: $r8$lambda$pIt52EEe965Yw-ytPo_u_PahKbk, reason: not valid java name */
        public static /* synthetic */ long m1590$r8$lambda$pIt52EEe965YwytPo_u_PahKbk(long j) {
            return (j + 1) % Long.MAX_VALUE;
        }
    }

    private static final class RequestHandleSatelliteAttachRestrictionForCarrierArgument {
        public Consumer<Integer> callback;
        public int reason;
        public int subId;

        RequestHandleSatelliteAttachRestrictionForCarrierArgument(int i, int i2, Consumer consumer) {
            this.subId = i;
            this.reason = i2;
            this.callback = consumer;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class UpdateSystemSelectionChannelsArgument {
        public long requestId = SatelliteController.sNextSystemSelectionChannelsUpdateRequestId.getAndUpdate(new LongUnaryOperator() { // from class: com.android.internal.telephony.satellite.SatelliteController$UpdateSystemSelectionChannelsArgument$$ExternalSyntheticLambda0
            @Override // java.util.function.LongUnaryOperator
            public final long applyAsLong(long j) {
                return SatelliteController.UpdateSystemSelectionChannelsArgument.$r8$lambda$j8PbIkdUpmHEvV79tveIoGt6LuI(j);
            }
        });
        public ResultReceiver result;
        public List<SystemSelectionSpecifier> systemSelectionSpecifiers;

        UpdateSystemSelectionChannelsArgument(List list, ResultReceiver resultReceiver) {
            this.systemSelectionSpecifiers = list;
            this.result = resultReceiver;
        }

        public static /* synthetic */ long $r8$lambda$j8PbIkdUpmHEvV79tveIoGt6LuI(long j) {
            return (j + 1) % Long.MAX_VALUE;
        }
    }

    public static final class SatelliteTransmissionUpdateArgument {
        public ISatelliteTransmissionUpdateCallback callback;
        public Consumer<Integer> errorCallback;
        public int subId;

        SatelliteTransmissionUpdateArgument(Consumer consumer, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback, int i) {
            this.errorCallback = consumer;
            this.callback = iSatelliteTransmissionUpdateCallback;
            this.subId = i;
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = 6;
        switch (message.what) {
            case 1:
                this.mPointingAppController.startSatelliteTransmissionUpdates(obtainMessage(2, (SatelliteControllerHandlerRequest) message.obj));
                return;
            case 2:
                handleStartSatelliteTransmissionUpdatesDone((AsyncResult) message.obj);
                return;
            case 3:
                this.mPointingAppController.stopSatelliteTransmissionUpdates(obtainMessage(4, (SatelliteControllerHandlerRequest) message.obj));
                return;
            case 4:
                AsyncResult asyncResult = (AsyncResult) message.obj;
                ((Consumer) ((SatelliteControllerHandlerRequest) asyncResult.userObj).argument).accept(Integer.valueOf(SatelliteServiceUtils.getSatelliteError(asyncResult, "stopSatelliteTransmissionUpdates")));
                return;
            case 5:
            case 6:
            case 19:
            case 20:
            case 26:
            case 48:
            default:
                Log.w("SatelliteController", "SatelliteControllerHandler: unexpected message code: " + message.what);
                return;
            case 7:
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest = (SatelliteControllerHandlerRequest) message.obj;
                ProvisionSatelliteServiceArgument provisionSatelliteServiceArgument = (ProvisionSatelliteServiceArgument) satelliteControllerHandlerRequest.argument;
                if (this.mSatelliteProvisionCallbacks.containsKey(Integer.valueOf(provisionSatelliteServiceArgument.subId))) {
                    provisionSatelliteServiceArgument.callback.accept(14);
                    notifyRequester(satelliteControllerHandlerRequest);
                    return;
                } else {
                    this.mSatelliteProvisionCallbacks.put(Integer.valueOf(provisionSatelliteServiceArgument.subId), provisionSatelliteServiceArgument.callback);
                    this.mProvisionMetricsStats.setProvisioningStartTime();
                    obtainMessage(8, new AsyncResult(satelliteControllerHandlerRequest, 0, (Throwable) null)).sendToTarget();
                    return;
                }
            case 8:
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest2 = (SatelliteControllerHandlerRequest) asyncResult2.userObj;
                handleEventProvisionSatelliteServiceDone((ProvisionSatelliteServiceArgument) satelliteControllerHandlerRequest2.argument, SatelliteServiceUtils.getSatelliteError(asyncResult2, "provisionSatelliteService"));
                notifyRequester(satelliteControllerHandlerRequest2);
                return;
            case 9:
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest3 = (SatelliteControllerHandlerRequest) message.obj;
                if (((ProvisionSatelliteServiceArgument) satelliteControllerHandlerRequest3.argument).callback != null) {
                    this.mProvisionMetricsStats.setProvisioningStartTime();
                }
                obtainMessage(10, new AsyncResult(satelliteControllerHandlerRequest3, 0, (Throwable) null)).sendToTarget();
                return;
            case 10:
                AsyncResult asyncResult3 = (AsyncResult) message.obj;
                handleEventDeprovisionSatelliteServiceDone((ProvisionSatelliteServiceArgument) ((SatelliteControllerHandlerRequest) asyncResult3.userObj).argument, SatelliteServiceUtils.getSatelliteError(asyncResult3, "deprovisionSatelliteService"));
                return;
            case 11:
                handleSatelliteEnabled((SatelliteControllerHandlerRequest) message.obj);
                return;
            case 12:
                AsyncResult asyncResult4 = (AsyncResult) message.obj;
                RequestSatelliteEnabledArgument requestSatelliteEnabledArgument = (RequestSatelliteEnabledArgument) ((SatelliteControllerHandlerRequest) asyncResult4.userObj).argument;
                int satelliteError = SatelliteServiceUtils.getSatelliteError(asyncResult4, "setSatelliteEnabled");
                plogd("EVENT_SET_SATELLITE_ENABLED_DONE = " + satelliteError);
                if (!shouldProcessEventSetSatelliteEnabledDone(requestSatelliteEnabledArgument)) {
                    plogw("The request ID=" + requestSatelliteEnabledArgument.requestId + ", enableSatellite=" + requestSatelliteEnabledArgument.enableSatellite + " was already processed");
                    return;
                }
                if (shouldStopWaitForEnableResponseTimer(requestSatelliteEnabledArgument)) {
                    stopWaitForSatelliteEnablingResponseTimer(requestSatelliteEnabledArgument);
                } else {
                    plogd("Still waiting for the OFF state from modem");
                }
                if (satelliteError == 0) {
                    if (requestSatelliteEnabledArgument.enableSatellite) {
                        this.mWaitingForRadioDisabled.set(true);
                        setDemoModeEnabled(requestSatelliteEnabledArgument.enableDemoMode);
                        setSettingsKeyForSatelliteMode(1);
                        setSettingsKeyToAllowDeviceRotation(1);
                        evaluateToSendSatelliteEnabledSuccess();
                    } else {
                        if (this.mNeedsSatellitePointing.get()) {
                            this.mPointingAppController.removeListenerForPointingUI();
                        }
                        if (!isWaitingForSatelliteModemOff()) {
                            moveSatelliteToOffStateAndCleanUpResources(0);
                        }
                        this.mWaitingForDisableSatelliteModemResponse.set(false);
                    }
                    this.mLatestRequestedStateForNtnSignalStrengthReport.set(requestSatelliteEnabledArgument.enableSatellite);
                    updateNtnSignalStrengthReporting(requestSatelliteEnabledArgument.enableSatellite);
                } else {
                    if (requestSatelliteEnabledArgument.enableSatellite) {
                        abortSatelliteEnableAttributesUpdateRequest(15);
                        resetSatelliteEnabledRequest();
                    } else {
                        resetSatelliteDisabledRequest();
                    }
                    notifyEnablementFailedToSatelliteSessionController(requestSatelliteEnabledArgument.enableSatellite);
                    requestSatelliteEnabledArgument.callback.accept(Integer.valueOf(satelliteError));
                }
                boolean z = requestSatelliteEnabledArgument.enableSatellite;
                SessionMetricsStats sessionMetricsStats = this.mSessionMetricsStats;
                if (z) {
                    sessionMetricsStats.resetSessionStatsShadowCounters();
                    this.mSessionMetricsStats.setInitializationResult(satelliteError).setSatelliteTechnology(getSupportedNtnRadioTechnology()).setInitializationProcessingTime(getElapsedRealtime() - this.mSessionProcessingTimeStamp.get()).setIsDemoMode(this.mIsDemoModeEnabled.get()).setCarrierId(getSatelliteCarrierId()).setSupportedConnectionMode(getSupportedConnectTypeMetrics()).setSessionConnectionMode(getSessionConnectTypeMetrics()).setIsNtnOnlyCarrier(isNtnOnlyCarrier()).setIsEmergency(requestSatelliteEnabledArgument.isEmergency);
                    this.mSessionProcessingTimeStamp.set(0L);
                    if (satelliteError == 0) {
                        this.mControllerMetricsStats.onSatelliteEnabled();
                        this.mControllerMetricsStats.reportServiceEnablementSuccessCount(requestSatelliteEnabledArgument.enableDemoMode);
                        return;
                    } else {
                        this.mSessionMetricsStats.reportSessionMetrics();
                        this.mSessionStartTimeStamp.set(0L);
                        this.mControllerMetricsStats.reportServiceEnablementFailCount(requestSatelliteEnabledArgument.enableDemoMode);
                        return;
                    }
                }
                sessionMetricsStats.setTerminationResult(satelliteError).setTerminationProcessingTime(getElapsedRealtime() - this.mSessionProcessingTimeStamp.get()).setSessionDurationSec(calculateSessionDurationTimeSec()).reportSessionMetrics();
                this.mSessionStartTimeStamp.set(0L);
                this.mSessionProcessingTimeStamp.set(0L);
                this.mControllerMetricsStats.onSatelliteDisabled();
                handlePersistentLoggingOnSessionEnd(this.mIsEmergency.get());
                this.mWaitingForDisableSatelliteModemResponse.set(false);
                return;
            case 13:
                this.mSatelliteModemInterface.requestIsSatelliteEnabled(obtainMessage(14, (SatelliteControllerHandlerRequest) message.obj));
                return;
            case 14:
                AsyncResult asyncResult5 = (AsyncResult) message.obj;
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest4 = (SatelliteControllerHandlerRequest) asyncResult5.userObj;
                int satelliteError2 = SatelliteServiceUtils.getSatelliteError(asyncResult5, "isSatelliteEnabled");
                Bundle bundle = new Bundle();
                if (satelliteError2 == 0) {
                    Object obj = asyncResult5.result;
                    if (obj == null) {
                        ploge("isSatelliteEnabled: result is null");
                    } else {
                        boolean z2 = ((int[]) obj)[0] == 1;
                        bundle.putBoolean("satellite_enabled", z2);
                        updateSatelliteEnabledState(z2, "EVENT_IS_SATELLITE_ENABLED_DONE");
                    }
                    ((ResultReceiver) satelliteControllerHandlerRequest4.argument).send(i, bundle);
                    decrementResultReceiverCount("SC:requestIsSatelliteEnabled");
                    return;
                }
                if (satelliteError2 == 11) {
                    updateSatelliteSupportedState(false);
                }
                i = satelliteError2;
                ((ResultReceiver) satelliteControllerHandlerRequest4.argument).send(i, bundle);
                decrementResultReceiverCount("SC:requestIsSatelliteEnabled");
                return;
            case 15:
                this.mSatelliteModemInterface.requestIsSatelliteSupported(obtainMessage(16, (SatelliteControllerHandlerRequest) message.obj));
                return;
            case 16:
                AsyncResult asyncResult6 = (AsyncResult) message.obj;
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest5 = (SatelliteControllerHandlerRequest) asyncResult6.userObj;
                int satelliteError3 = SatelliteServiceUtils.getSatelliteError(asyncResult6, "isSatelliteSupported");
                Bundle bundle2 = new Bundle();
                if (satelliteError3 != 0) {
                    i = satelliteError3;
                } else {
                    Object obj2 = asyncResult6.result;
                    if (obj2 == null) {
                        ploge("isSatelliteSupported: result is null");
                    } else {
                        boolean zBooleanValue = ((Boolean) obj2).booleanValue();
                        plogd("isSatelliteSupported: " + zBooleanValue);
                        bundle2.putBoolean("satellite_supported", zBooleanValue);
                        updateSatelliteSupportedState(zBooleanValue);
                        i = satelliteError3;
                    }
                }
                ((ResultReceiver) satelliteControllerHandlerRequest5.argument).send(i, bundle2);
                return;
            case 17:
                this.mSatelliteModemInterface.requestSatelliteCapabilities(obtainMessage(18, (SatelliteControllerHandlerRequest) message.obj));
                return;
            case 18:
                AsyncResult asyncResult7 = (AsyncResult) message.obj;
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest6 = (SatelliteControllerHandlerRequest) asyncResult7.userObj;
                int satelliteError4 = SatelliteServiceUtils.getSatelliteError(asyncResult7, "getSatelliteCapabilities");
                Bundle bundle3 = new Bundle();
                if (satelliteError4 != 0) {
                    i = satelliteError4;
                } else {
                    Object obj3 = asyncResult7.result;
                    if (obj3 == null) {
                        ploge("getSatelliteCapabilities: result is null");
                    } else {
                        SatelliteCapabilities satelliteCapabilities = (SatelliteCapabilities) obj3;
                        this.mNeedsSatellitePointing.set(satelliteCapabilities.isPointingRequired());
                        setSatelliteCapabilities(satelliteCapabilities);
                        overrideSatelliteCapabilitiesIfApplicable();
                        bundle3.putParcelable("satellite_capabilities", getSatelliteCapabilities());
                        i = satelliteError4;
                    }
                }
                ((ResultReceiver) satelliteControllerHandlerRequest6.argument).send(i, bundle3);
                return;
            case 21:
                this.mSatelliteModemInterface.requestTimeForNextSatelliteVisibility(obtainMessage(22, (SatelliteControllerHandlerRequest) message.obj));
                return;
            case 22:
                AsyncResult asyncResult8 = (AsyncResult) message.obj;
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest7 = (SatelliteControllerHandlerRequest) asyncResult8.userObj;
                int satelliteError5 = SatelliteServiceUtils.getSatelliteError(asyncResult8, "requestTimeForNextSatelliteVisibility");
                Bundle bundle4 = new Bundle();
                if (satelliteError5 != 0) {
                    i = satelliteError5;
                } else {
                    Object obj4 = asyncResult8.result;
                    if (obj4 == null) {
                        ploge("requestTimeForNextSatelliteVisibility: result is null");
                    } else {
                        bundle4.putInt("satellite_next_visibility", ((int[]) obj4)[0]);
                        i = satelliteError5;
                    }
                }
                ((ResultReceiver) satelliteControllerHandlerRequest7.argument).send(i, bundle4);
                decrementResultReceiverCount("SC:requestTimeForNextSatelliteVisibility");
                return;
            case 23:
                logd("EVENT_RADIO_STATE_CHANGED: radioState=" + this.mCi.getRadioState());
                if (this.mCi.getRadioState() == 1) {
                    this.mIsRadioOn.set(true);
                } else if (this.mCi.getRadioState() == 0) {
                    resetCarrierRoamingSatelliteModeParams();
                    if (this.mRadioOffRequested.get()) {
                        logd("EVENT_RADIO_STATE_CHANGED: set mIsRadioOn to false");
                        stopWaitForCellularModemOffTimer();
                        this.mIsRadioOn.set(false);
                        this.mRadioOffRequested.set(false);
                    }
                }
                if (this.mCi.getRadioState() == 2 || !this.mSatelliteModemInterface.isSatelliteServiceConnected()) {
                    return;
                }
                Boolean isSatelliteSupported = getIsSatelliteSupported();
                if (isSatelliteSupported == null || !isSatelliteSupported.booleanValue()) {
                    sendRequestAsync(15, new ResultReceiver(this) { // from class: com.android.internal.telephony.satellite.SatelliteController.6
                        @Override // android.os.ResultReceiver
                        protected void onReceiveResult(int i2, Bundle bundle5) {
                            SatelliteController.this.decrementResultReceiverCount("SC:CMD_IS_SATELLITE_SUPPORTED");
                            SatelliteController.this.plogd("onRadioStateChanged.requestIsSatelliteSupported: resultCode=" + i2 + ", resultData=" + bundle5);
                        }
                    }, null);
                    incrementResultReceiverCount("SC:CMD_IS_SATELLITE_SUPPORTED");
                    return;
                }
                return;
            case 24:
                obtainMessage(25, new AsyncResult((SatelliteControllerHandlerRequest) message.obj, 0, (Throwable) null)).sendToTarget();
                return;
            case 25:
                handleIsSatelliteProvisionedDoneEvent((AsyncResult) message.obj);
                return;
            case 27:
                plogd("Received EVENT_PENDING_DATAGRAMS");
                pollPendingDatagrams(new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteController.7
                    public void accept(int i2) {
                        SatelliteController.this.plogd("pollPendingSatelliteDatagram result: " + i2);
                    }
                });
                return;
            case 28:
                Object obj5 = ((AsyncResult) message.obj).result;
                if (obj5 == null) {
                    ploge("EVENT_SATELLITE_MODEM_STATE_CHANGED: result is null");
                    return;
                } else {
                    handleEventSatelliteModemStateChanged(((Integer) obj5).intValue());
                    updateLastNotifiedCarrierRoamingNtnSignalStrengthAndNotify(getSatellitePhone());
                    return;
                }
            case 29:
                handleSetSatellitePlmnInfoDoneEvent(message);
                return;
            case 30:
                plogd("CMD_EVALUATE_SATELLITE_ATTACH_RESTRICTION_CHANGE");
                handleRequestSatelliteAttachRestrictionForCarrierCmd((SatelliteControllerHandlerRequest) message.obj);
                return;
            case 31:
                AsyncResult asyncResult9 = (AsyncResult) message.obj;
                RequestHandleSatelliteAttachRestrictionForCarrierArgument requestHandleSatelliteAttachRestrictionForCarrierArgument = (RequestHandleSatelliteAttachRestrictionForCarrierArgument) ((SatelliteControllerHandlerRequest) asyncResult9.userObj).argument;
                int i2 = requestHandleSatelliteAttachRestrictionForCarrierArgument.subId;
                int satelliteError6 = SatelliteServiceUtils.getSatelliteError(asyncResult9, "requestSetSatelliteEnabledForCarrier");
                plogd("EVENT_EVALUATE_SATELLITE_ATTACH_RESTRICTION_CHANGE_DONE: subId=" + i2 + " error:" + satelliteError6);
                if (satelliteError6 == 0) {
                    boolean zIsEmpty = this.mSatelliteAttachRestrictionForCarrierArray.getOrDefault(Integer.valueOf(requestHandleSatelliteAttachRestrictionForCarrierArgument.subId), Collections.EMPTY_SET).isEmpty();
                    plogd("EVENT_EVALUATE_SATELLITE_ATTACH_RESTRICTION_CHANGE_DONE: satelliteAttachEnabledForCarrier=" + zIsEmpty);
                    this.mIsSatelliteAttachEnabledForCarrierArrayPerSub.put(Integer.valueOf(i2), Boolean.valueOf(zIsEmpty));
                } else {
                    this.mIsSatelliteAttachEnabledForCarrierArrayPerSub.remove(Integer.valueOf(i2));
                }
                requestHandleSatelliteAttachRestrictionForCarrierArgument.callback.accept(Integer.valueOf(satelliteError6));
                configureSatellitePlmnForCarrier(i2);
                return;
            case 32:
                plogd("CMD_REQUEST_NTN_SIGNAL_STRENGTH");
                this.mSatelliteModemInterface.requestNtnSignalStrength(obtainMessage(33, (SatelliteControllerHandlerRequest) message.obj));
                return;
            case 33:
                AsyncResult asyncResult10 = (AsyncResult) message.obj;
                ResultReceiver resultReceiver = (ResultReceiver) ((SatelliteControllerHandlerRequest) asyncResult10.userObj).argument;
                int satelliteError7 = SatelliteServiceUtils.getSatelliteError(asyncResult10, "requestNtnSignalStrength");
                if (satelliteError7 == 0) {
                    NtnSignalStrength ntnSignalStrength = (NtnSignalStrength) asyncResult10.result;
                    if (ntnSignalStrength != null) {
                        setNtnSignalStrength(ntnSignalStrength);
                        Bundle bundle5 = new Bundle();
                        bundle5.putParcelable("ntn_signal_strength", ntnSignalStrength);
                        resultReceiver.send(0, bundle5);
                    } else {
                        if (getNtnSignalStrength().getLevel() != 0) {
                            setNtnSignalStrength(new NtnSignalStrength(0));
                        }
                        ploge("EVENT_REQUEST_NTN_SIGNAL_STRENGTH_DONE: ntnSignalStrength is null");
                        resultReceiver.send(9, null);
                    }
                } else {
                    if (getNtnSignalStrength().getLevel() != 0) {
                        setNtnSignalStrength(new NtnSignalStrength(0));
                    }
                    resultReceiver.send(satelliteError7, null);
                }
                decrementResultReceiverCount("SC:requestNtnSignalStrength");
                return;
            case 34:
                Object obj6 = ((AsyncResult) message.obj).result;
                if (obj6 == null) {
                    ploge("EVENT_NTN_SIGNAL_STRENGTH_CHANGED: result is null");
                    return;
                } else {
                    handleEventNtnSignalStrengthChanged((NtnSignalStrength) obj6);
                    updateLastNotifiedCarrierRoamingNtnSignalStrengthAndNotify(getSatellitePhone());
                    return;
                }
            case 35:
                handleCmdUpdateNtnSignalStrengthReporting(((Boolean) ((AsyncResult) message.obj).result).booleanValue());
                return;
            case 36:
                AsyncResult asyncResult11 = (AsyncResult) message.obj;
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest8 = (SatelliteControllerHandlerRequest) asyncResult11.userObj;
                boolean zBooleanValue2 = ((Boolean) satelliteControllerHandlerRequest8.argument).booleanValue();
                int satelliteError8 = SatelliteServiceUtils.getSatelliteError(asyncResult11, "EVENT_UPDATE_NTN_SIGNAL_STRENGTH_REPORTING_DONE: shouldReport=" + zBooleanValue2);
                if (satelliteError8 == 0) {
                    this.mIsModemEnabledReportingNtnSignalStrength.set(zBooleanValue2);
                    if (this.mLatestRequestedStateForNtnSignalStrengthReport.get() != this.mIsModemEnabledReportingNtnSignalStrength.get()) {
                        logd("mLatestRequestedStateForNtnSignalStrengthReport does not match with mIsModemEnabledReportingNtnSignalStrength");
                        updateNtnSignalStrengthReporting(this.mLatestRequestedStateForNtnSignalStrengthReport.get());
                        return;
                    }
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(((Boolean) satelliteControllerHandlerRequest8.argument).booleanValue() ? "startSendingNtnSignalStrength" : "stopSendingNtnSignalStrength");
                sb.append("returns ");
                sb.append(satelliteError8);
                loge(sb.toString());
                return;
            case 37:
                handleEventServiceStateChanged();
                return;
            case 38:
                Object obj7 = ((AsyncResult) message.obj).result;
                if (obj7 == null) {
                    ploge("EVENT_SATELLITE_CAPABILITIES_CHANGED: result is null");
                    return;
                } else {
                    handleEventSatelliteCapabilitiesChanged((SatelliteCapabilities) obj7);
                    return;
                }
            case 39:
                handleEventWaitForSatelliteEnablingResponseTimedOut((RequestSatelliteEnabledArgument) message.obj);
                return;
            case 40:
                handleEventConfigDataUpdated();
                this.mSatelliteConfigUpdateChangedRegistrants.notifyRegistrants();
                return;
            case 41:
                Object obj8 = ((AsyncResult) message.obj).result;
                if (obj8 == null) {
                    ploge("EVENT_SATELLITE_SUPPORTED_STATE_CHANGED: result is null");
                    return;
                } else {
                    handleEventSatelliteSupportedStateChanged(((Boolean) obj8).booleanValue());
                    return;
                }
            case 42:
                updateLastNotifiedNtnModeAndNotify(PhoneFactory.getPhone(((Integer) message.obj).intValue()));
                return;
            case 43:
                evaluateESOSProfilesPrioritization();
                return;
            case 44:
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest9 = (SatelliteControllerHandlerRequest) message.obj;
                RequestProvisionSatelliteArgument requestProvisionSatelliteArgument = (RequestProvisionSatelliteArgument) satelliteControllerHandlerRequest9.argument;
                Message messageObtainMessage = obtainMessage(45, satelliteControllerHandlerRequest9);
                boolean zUpdateSatelliteSubscriptionProvisionState = updateSatelliteSubscriptionProvisionState(requestProvisionSatelliteArgument.mSatelliteSubscriberInfoList, requestProvisionSatelliteArgument.mProvisioned);
                selectBindingSatelliteSubscription(false);
                int selectedSatelliteSubId = getSelectedSatelliteSubId();
                SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(selectedSatelliteSubId);
                if (subscriptionInfo == null) {
                    logw("updateSatelliteToken subId=" + selectedSatelliteSubId + " is not found");
                } else {
                    String iccId = subscriptionInfo.getIccId();
                    requestProvisionSatelliteArgument.setIccId(iccId);
                    if (!iccId.equals(getLastConfiguredIccId())) {
                        logd("updateSatelliteSubscription subId=" + selectedSatelliteSubId + ", iccId=" + iccId + " to modem");
                        this.mSatelliteModemInterface.updateSatelliteSubscription(iccId, messageObtainMessage);
                    }
                }
                if (zUpdateSatelliteSubscriptionProvisionState) {
                    handleEventSatelliteSubscriptionProvisionStateChanged();
                }
                Bundle bundle6 = new Bundle();
                bundle6.putBoolean(requestProvisionSatelliteArgument.mProvisioned ? "provision_satellite" : "deprovision_satellite", true);
                requestProvisionSatelliteArgument.mResult.send(0, bundle6);
                decrementResultReceiverCount("SC:provisionSatellite");
                return;
            case 45:
                AsyncResult asyncResult12 = (AsyncResult) message.obj;
                RequestProvisionSatelliteArgument requestProvisionSatelliteArgument2 = (RequestProvisionSatelliteArgument) ((SatelliteControllerHandlerRequest) asyncResult12.userObj).argument;
                int satelliteError9 = SatelliteServiceUtils.getSatelliteError(asyncResult12, "updateSatelliteSubscription");
                if (satelliteError9 == 0) {
                    setLastConfiguredIccId(requestProvisionSatelliteArgument2.getIccId());
                }
                this.mProvisionMetricsStats.setResultCode(satelliteError9).setIsProvisionRequest(requestProvisionSatelliteArgument2.mProvisioned).setCarrierId(getSatelliteCarrierId()).setSupportedConnectionMode(getSupportedConnectTypeMetrics()).setIsNtnOnlyCarrier(isNtnOnlyCarrier()).reportProvisionMetrics();
                boolean z3 = requestProvisionSatelliteArgument2.mProvisioned;
                ControllerMetricsStats controllerMetricsStats = this.mControllerMetricsStats;
                if (z3) {
                    controllerMetricsStats.reportProvisionCount(satelliteError9);
                } else {
                    controllerMetricsStats.reportDeprovisionCount(satelliteError9);
                }
                logd("updateSatelliteSubscription result=" + satelliteError9);
                return;
            case 46:
                boolean zIsCarrierRoamingNtnEligible = isCarrierRoamingNtnEligible(getSatellitePhone());
                plogd("EVENT_NOTIFY_NTN_ELIGIBILITY_HYSTERESIS_TIMED_OUT: isCarrierRoamingNtnEligible=" + zIsCarrierRoamingNtnEligible);
                updateLastNotifiedNtnEligibilityAndNotify(zIsCarrierRoamingNtnEligible);
                return;
            case 47:
                boolean zBooleanValue3 = ((Boolean) ((AsyncResult) message.obj).result).booleanValue();
                this.mIsWifiConnected.set(zBooleanValue3);
                plogd("EVENT_WIFI_CONNECTIVITY_STATE_CHANGED: mIsWifiConnected=" + zBooleanValue3);
                evaluateCarrierRoamingNtnEligibilityChange();
                handleEventWifiConnectivityStateChanged(zBooleanValue3);
                return;
            case 49:
                plogw("Timed out to wait for cellular modem OFF state");
                this.mRadioOffRequested.set(false);
                return;
            case 50:
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest10 = (SatelliteControllerHandlerRequest) message.obj;
                RequestSatelliteEnabledArgument requestSatelliteEnabledArgument2 = (RequestSatelliteEnabledArgument) satelliteControllerHandlerRequest10.argument;
                if (getSatelliteEnabledRequest() != null) {
                    plogd("UpdateEnableAttributes: Satellite is being enabled. Need to wait until enable complete before updating attributes");
                    return;
                }
                if (isSatelliteBeingDisabled()) {
                    plogd("UpdateEnableAttributes: Satellite is being disabled. Aborting the enable attributes update request");
                    setSatelliteEnableAttributesUpdateRequest(null);
                    requestSatelliteEnabledArgument2.callback.accept(15);
                    return;
                }
                Message messageObtainMessage2 = obtainMessage(51, satelliteControllerHandlerRequest10);
                SatelliteModemEnableRequestAttributes satelliteModemEnableRequestAttributesCreateModemEnableRequest = createModemEnableRequest(requestSatelliteEnabledArgument2);
                if (satelliteModemEnableRequestAttributesCreateModemEnableRequest == null) {
                    plogw("UpdateEnableAttributes: enableRequestAttributes is null");
                    sendErrorAndReportSessionMetrics(6, requestSatelliteEnabledArgument2.callback);
                    setSatelliteEnableAttributesUpdateRequest(null);
                    return;
                } else {
                    this.mSatelliteModemInterface.requestSatelliteEnabled(satelliteModemEnableRequestAttributesCreateModemEnableRequest, messageObtainMessage2);
                    startWaitForUpdateSatelliteEnableAttributesResponseTimer(requestSatelliteEnabledArgument2);
                    return;
                }
            case 51:
                AsyncResult asyncResult13 = (AsyncResult) message.obj;
                RequestSatelliteEnabledArgument requestSatelliteEnabledArgument3 = (RequestSatelliteEnabledArgument) ((SatelliteControllerHandlerRequest) asyncResult13.userObj).argument;
                int satelliteError10 = SatelliteServiceUtils.getSatelliteError(asyncResult13, "updateSatelliteEnableAttributes");
                plogd("EVENT_UPDATE_SATELLITE_ENABLE_ATTRIBUTES_DONE = " + satelliteError10);
                if (!shouldProcessEventUpdateSatelliteEnableAttributesDone(requestSatelliteEnabledArgument3)) {
                    plogw("The update request ID=" + requestSatelliteEnabledArgument3.requestId + " was already processed");
                    return;
                }
                stopWaitForUpdateSatelliteEnableAttributesResponseTimer(requestSatelliteEnabledArgument3);
                if (satelliteError10 == 0) {
                    setDemoModeEnabled(requestSatelliteEnabledArgument3.enableDemoMode);
                    setEmergencyMode(requestSatelliteEnabledArgument3.isEmergency);
                }
                setSatelliteEnableAttributesUpdateRequest(null);
                requestSatelliteEnabledArgument3.callback.accept(Integer.valueOf(satelliteError10));
                return;
            case 52:
                RequestSatelliteEnabledArgument requestSatelliteEnabledArgument4 = (RequestSatelliteEnabledArgument) message.obj;
                plogw("Timed out to wait for the response from the modem for the request to update satellite enable attributes, request ID = " + requestSatelliteEnabledArgument4.requestId);
                setSatelliteEnableAttributesUpdateRequest(null);
                requestSatelliteEnabledArgument4.callback.accept(24);
                return;
            case 53:
                handleEntireEntitlementMetricReport();
                handleEntireProvisionMetricReport();
                handleCarrierRoamingConfigVersionReport();
                handleMaxAllowedDataMetricsReport();
                scheduleRegularMetricReportTimer();
                handleEntireEligibilityMetricReport();
                return;
            case 54:
                Object obj9 = ((AsyncResult) message.obj).result;
                if (obj9 == null) {
                    loge("EVENT_SATELLITE_REGISTRATION_FAILURE: result is null");
                    return;
                } else {
                    handleEventSatelliteRegistrationFailure(((Integer) obj9).intValue());
                    return;
                }
            case 55:
                Object obj10 = ((AsyncResult) message.obj).result;
                if (obj10 == null) {
                    loge("EVENT_TERRESTRIAL_NETWORK_AVAILABLE_CHANGED: result is null");
                    return;
                } else {
                    handleEventTerrestrialNetworkAvailableChanged(((Boolean) obj10).booleanValue());
                    return;
                }
            case 56:
                logd("EVENT_SET_NETWORK_SELECTION_AUTO_DONE");
                sendRequestAsync(11, (RequestSatelliteEnabledArgument) message.obj, null);
                return;
            case 57:
                updateLastNotifiedCarrierRoamingNtnSignalStrengthAndNotify(PhoneFactory.getPhone(((Integer) ((AsyncResult) message.obj).userObj).intValue()));
                return;
            case 58:
                plogd("CMD_UPDATE_SYSTEM_SELECTION_CHANNELS");
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest11 = (SatelliteControllerHandlerRequest) message.obj;
                Message messageObtainMessage3 = obtainMessage(59, satelliteControllerHandlerRequest11);
                UpdateSystemSelectionChannelsArgument updateSystemSelectionChannelsArgument = (UpdateSystemSelectionChannelsArgument) satelliteControllerHandlerRequest11.argument;
                this.mSatelliteModemInterface.updateSystemSelectionChannels(updateSystemSelectionChannelsArgument.systemSelectionSpecifiers, messageObtainMessage3);
                startWaitForUpdateSystemSelectionChannelsResponseTimer(updateSystemSelectionChannelsArgument);
                return;
            case 59:
                AsyncResult asyncResult14 = (AsyncResult) message.obj;
                SatelliteControllerHandlerRequest satelliteControllerHandlerRequest12 = (SatelliteControllerHandlerRequest) asyncResult14.userObj;
                int satelliteError11 = SatelliteServiceUtils.getSatelliteError(asyncResult14, "updateSystemSelectionChannel");
                plogd("EVENT_UPDATE_SYSTEM_SELECTION_CHANNELS_DONE = " + satelliteError11);
                UpdateSystemSelectionChannelsArgument updateSystemSelectionChannelsArgument2 = (UpdateSystemSelectionChannelsArgument) satelliteControllerHandlerRequest12.argument;
                if (shouldProcessEventUpdateSystemSelectionChannelsDone(updateSystemSelectionChannelsArgument2)) {
                    updateSystemSelectionChannelsArgument2.result.send(satelliteError11, null);
                    stopWaitForUpdateSystemSelectionChannelsResponseTimer(updateSystemSelectionChannelsArgument2);
                    return;
                } else {
                    plogd("EVENT_UPDATE_SYSTEM_SELECTION_CHANNELS_DONE: the timer of the request ID " + updateSystemSelectionChannelsArgument2.requestId + " has already expired.");
                    return;
                }
            case 60:
                Object obj11 = ((AsyncResult) message.obj).result;
                if (obj11 == null) {
                    loge("EVENT_SELECTED_NB_IOT_SATELLITE_SUBSCRIPTION_CHANGED: result is null");
                    return;
                } else {
                    handleEventSelectedNbIotSatelliteSubscriptionChanged(((Integer) obj11).intValue());
                    return;
                }
            case 61:
                break;
            case 62:
                plogd("CMD_LOCATION_SERVICE_STATE_CHANGED");
                break;
            case 63:
                UpdateSystemSelectionChannelsArgument updateSystemSelectionChannelsArgument3 = (UpdateSystemSelectionChannelsArgument) message.obj;
                updateSystemSelectionChannelsArgument3.result.send(24, null);
                plogd("Timed out to wait for response of the system selection channels update request ID " + updateSystemSelectionChannelsArgument3.requestId);
                return;
            case 64:
                Phone phone = ((SatelliteControllerHandlerRequest) message.obj).phone;
                int subId = phone.getSubId();
                Message messageObtainMessage4 = obtainMessage(65, Integer.valueOf(subId));
                int slotIndex = SubscriptionManager.getSlotIndex(subId);
                plogd("CMD_GET_SATELLITE_ENABLED_FOR_CARRIER: subId=" + subId);
                phone.isSatelliteEnabledForCarrier(slotIndex, messageObtainMessage4);
                return;
            case 65:
                AsyncResult asyncResult15 = (AsyncResult) message.obj;
                if (asyncResult15.result == null) {
                    loge("EVENT_GET_SATELLITE_ENABLED_FOR_CARRIER_DONE: result is null");
                    return;
                }
                Integer num = (Integer) asyncResult15.userObj;
                int iIntValue = num.intValue();
                int satelliteError12 = SatelliteServiceUtils.getSatelliteError(asyncResult15, "isSatelliteEnabledForCarrier");
                Boolean bool = (Boolean) asyncResult15.result;
                plogd("EVENT_GET_SATELLITE_ENABLED_FOR_CARRIER_DONE: subId=" + iIntValue + " error=" + satelliteError12 + " satelliteEnabled=" + bool.booleanValue());
                if (satelliteError12 == 0) {
                    this.mIsSatelliteAttachEnabledForCarrierArrayPerSub.put(num, bool);
                    configureSatellitePlmnForCarrier(iIntValue);
                    evaluateEnablingSatelliteForCarrier(iIntValue, 0, null);
                    return;
                }
                return;
            case 66:
                plogd("REQUEST_SATELLITE_ENABLED");
                SomeArgs someArgs = (SomeArgs) message.obj;
                try {
                    handleRequestSatelliteEnabled(((Boolean) someArgs.arg1).booleanValue(), ((Boolean) someArgs.arg2).booleanValue(), ((Boolean) someArgs.arg3).booleanValue(), (IIntegerConsumer) someArgs.arg4);
                    return;
                } finally {
                    someArgs.recycle();
                }
            case 67:
                plogd("REQUEST_IS_SATELLITE_ENABLED");
                SomeArgs someArgs2 = (SomeArgs) message.obj;
                try {
                    handleRequestIsSatelliteEnabled((ResultReceiver) someArgs2.arg1);
                    return;
                } finally {
                    someArgs2.recycle();
                }
            case 68:
                plogd("REQUEST_IS_DEMO_MODE_ENABLED");
                SomeArgs someArgs3 = (SomeArgs) message.obj;
                try {
                    handleRequestIsDemoModeEnabled((ResultReceiver) someArgs3.arg1);
                    return;
                } finally {
                    someArgs3.recycle();
                }
            case 69:
                plogd("REQUEST_IS_EMERGENCY_MODE_ENABLED");
                SomeArgs someArgs4 = (SomeArgs) message.obj;
                try {
                    handleRequestIsEmergencyModeEnabled((ResultReceiver) someArgs4.arg1);
                    return;
                } finally {
                    someArgs4.recycle();
                }
            case 70:
                plogd("REQUEST_IS_SATELLITE_SUPPORTED");
                SomeArgs someArgs5 = (SomeArgs) message.obj;
                try {
                    handleRequestIsSatelliteSupported((ResultReceiver) someArgs5.arg1);
                    return;
                } finally {
                    someArgs5.recycle();
                }
            case 71:
                plogd("REQUEST_SATELLITE_CAPABILITIES");
                SomeArgs someArgs6 = (SomeArgs) message.obj;
                try {
                    handleRequestSatelliteCapabilities((ResultReceiver) someArgs6.arg1);
                    return;
                } finally {
                    someArgs6.recycle();
                }
            case 72:
                plogd("REQUEST_START_SATELLITE_TRANSMISSION_UPDATES");
                SomeArgs someArgs7 = (SomeArgs) message.obj;
                try {
                    handleRequestStartSatelliteTransmissionUpdates((IIntegerConsumer) someArgs7.arg1, (ISatelliteTransmissionUpdateCallback) someArgs7.arg2);
                    return;
                } finally {
                    someArgs7.recycle();
                }
            case 73:
                plogd("REQUEST_STOP_SATELLITE_TRANSMISSION_UPDATES");
                SomeArgs someArgs8 = (SomeArgs) message.obj;
                try {
                    handleRequestStopSatelliteTransmissionUpdates((IIntegerConsumer) someArgs8.arg1, (ISatelliteTransmissionUpdateCallback) someArgs8.arg2);
                    return;
                } finally {
                    someArgs8.recycle();
                }
            case 74:
                plogd("REQUEST_IS_SATELLITE_PROVISIONED");
                SomeArgs someArgs9 = (SomeArgs) message.obj;
                try {
                    handleRequestIsSatelliteProvisioned((ResultReceiver) someArgs9.arg1);
                    return;
                } finally {
                    someArgs9.recycle();
                }
            case 75:
                plogd("REQUEST_POLL_PENDING_DATAGRAMS");
                SomeArgs someArgs10 = (SomeArgs) message.obj;
                try {
                    handleRequestPollPendingDatagrams((IIntegerConsumer) someArgs10.arg1);
                    return;
                } finally {
                    someArgs10.recycle();
                }
            case 76:
                plogd("REQUEST_SEND_DATAGRAM");
                SomeArgs someArgs11 = (SomeArgs) message.obj;
                try {
                    handleRequestSendDatagram(((Integer) someArgs11.arg1).intValue(), (SatelliteDatagram) someArgs11.arg2, ((Boolean) someArgs11.arg3).booleanValue(), (IIntegerConsumer) someArgs11.arg4);
                    return;
                } finally {
                    someArgs11.recycle();
                }
            case 77:
                plogd("REQUEST_TIME_FOR_NEXT_SATELLITE_VISIBILITY");
                SomeArgs someArgs12 = (SomeArgs) message.obj;
                try {
                    handleRequestTimeForNextSatelliteVisibility((ResultReceiver) someArgs12.arg1);
                    return;
                } finally {
                    someArgs12.recycle();
                }
            case 78:
                plogd("REQUEST_SATELLITE_DISPLAY_NAME");
                SomeArgs someArgs13 = (SomeArgs) message.obj;
                try {
                    handleRequestSatelliteDisplayName((ResultReceiver) someArgs13.arg1);
                    return;
                } finally {
                    someArgs13.recycle();
                }
            case 79:
                plogd("REQUEST_SELECTED_NB_IOT_SATELLITE_SUBSCRIPTION_ID");
                SomeArgs someArgs14 = (SomeArgs) message.obj;
                try {
                    handleRequestSelectedNbIotSatelliteSubscriptionId((ResultReceiver) someArgs14.arg1);
                    return;
                } finally {
                    someArgs14.recycle();
                }
            case 80:
                plogd("REQUEST_SET_DEVICE_ALIGNED_WITH_SATELLITE");
                SomeArgs someArgs15 = (SomeArgs) message.obj;
                try {
                    handleRequestSetDeviceAlignedWithSatellite(((Boolean) someArgs15.arg1).booleanValue());
                    return;
                } finally {
                    someArgs15.recycle();
                }
            case 81:
                plogd("REQUEST_ADD_ATTACH_RESTRICTION_FOR_CARRIER");
                SomeArgs someArgs16 = (SomeArgs) message.obj;
                try {
                    handleRequestAddAttachRestrictionForCarrier(((Integer) someArgs16.arg1).intValue(), ((Integer) someArgs16.arg2).intValue(), (IIntegerConsumer) someArgs16.arg3);
                    return;
                } finally {
                    someArgs16.recycle();
                }
            case 82:
                plogd("REQUEST_REMOVE_ATTACH_RESTRICTION_FOR_CARRIER");
                SomeArgs someArgs17 = (SomeArgs) message.obj;
                try {
                    handleRequestRemoveAttachRestrictionForCarrier(((Integer) someArgs17.arg1).intValue(), ((Integer) someArgs17.arg2).intValue(), (IIntegerConsumer) someArgs17.arg3);
                    return;
                } finally {
                    someArgs17.recycle();
                }
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_QUERY_PREFERRED_VOICE_PRIVACY_MODE /* 83 */:
                plogd("REQUEST_NTN_SIGNAL_STRENGTH");
                SomeArgs someArgs18 = (SomeArgs) message.obj;
                try {
                    handleRequestNtnSignalStrength((ResultReceiver) someArgs18.arg1);
                    return;
                } finally {
                    someArgs18.recycle();
                }
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_FLASH /* 84 */:
                plogd("REQUEST_SATELLITE_SUBSCRIBER_PROVISION_STATUS");
                SomeArgs someArgs19 = (SomeArgs) message.obj;
                try {
                    handleRequestSatelliteSubscriberProvisionStatus((ResultReceiver) someArgs19.arg1);
                    return;
                } finally {
                    someArgs19.recycle();
                }
            case 85:
                plogd("REQUEST_SET_NTN_SMS_SUPPORTED_BY_MESSAGES_APP");
                SomeArgs someArgs20 = (SomeArgs) message.obj;
                try {
                    handleRequestSetNtnSmsSupportedByMessagesApp(((Boolean) someArgs20.arg1).booleanValue());
                    return;
                } finally {
                    someArgs20.recycle();
                }
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_VALIDATE_AND_WRITE_AKEY /* 86 */:
                plogd("REQUEST_PROVISION_SATELLITE");
                SomeArgs someArgs21 = (SomeArgs) message.obj;
                try {
                    handleRequestProvisionSatellite((List) someArgs21.arg1, (ResultReceiver) someArgs21.arg2);
                    return;
                } finally {
                    someArgs21.recycle();
                }
            case 87:
                plogd("REQUEST_DEPROVISION_SATELLITE");
                SomeArgs someArgs22 = (SomeArgs) message.obj;
                try {
                    handleRequestDeprovisionSatellite((List) someArgs22.arg1, (ResultReceiver) someArgs22.arg2);
                    return;
                } finally {
                    someArgs22.recycle();
                }
            case 88:
                plogd("EVENT_SATELLITE_ACCESS_ALLOWED_STATE_CHANGED");
                handleSatelliteAccessAllowedStateChanged(((Boolean) message.obj).booleanValue());
                return;
            case 89:
                plogd("EVENT_SATELLITE_ACCESS_CONFIGURATION_CHANGED");
                handleSatelliteAccessConfigUpdateResult((SatelliteAccessConfiguration) message.obj);
                return;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GSM_SET_BROADCAST_CONFIG /* 90 */:
                plogd("EVENT_BT_WIFI_NFC_STATE_CHANGED");
                handleEventBtWifiNfcStateChanged((Intent) message.obj);
                return;
            case 91:
                plogd("EVENT_UWB_STATE_CHANGED");
                handleEventUwbStateChanged(((Integer) message.obj).intValue());
                return;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_BROADCAST_CONFIG /* 92 */:
                plogd("EVENT_CARRIER_CONFIG_CHANGED");
                SomeArgs someArgs23 = (SomeArgs) message.obj;
                try {
                    handleCarrierConfigChanged(((Integer) someArgs23.arg1).intValue(), ((Integer) someArgs23.arg2).intValue(), ((Integer) someArgs23.arg3).intValue(), ((Integer) someArgs23.arg4).intValue());
                    return;
                } finally {
                    someArgs23.recycle();
                }
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_SET_BROADCAST_CONFIG /* 93 */:
                plogd("EVENT_SATELLITE_ENTILEMENT_STATUS_UPDATED");
                SomeArgs someArgs24 = (SomeArgs) message.obj;
                try {
                    handleSatelliteEntitlementStatusUpdated(someArgs24.argi1, someArgs24.argi2 == 1, (List) someArgs24.arg1, (List) someArgs24.arg2, (Map) someArgs24.arg3, (Map) someArgs24.arg4, (Map) someArgs24.arg5, (Map) someArgs24.arg6, (IIntegerConsumer) someArgs24.arg7);
                    return;
                } finally {
                    someArgs24.recycle();
                }
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_BROADCAST_ACTIVATION /* 94 */:
                handlePackageChangeEvent();
                return;
            case 95:
                handleEventScreenStateChanged((AsyncResult) message.obj);
                return;
            case 96:
                handleSetSatellitePlmnNetworkInfoDoneEvent(message);
                return;
            case 97:
                plogd("REQUEST_POINTING_UI_APP_LAUNCH_INTENT");
                SomeArgs someArgs25 = (SomeArgs) message.obj;
                try {
                    handleRequestPointingUiAppLaunchIntent((PointingUiAppLaunchIntentAttributes) someArgs25.arg1, (ResultReceiver) someArgs25.arg2);
                    return;
                } finally {
                    someArgs25.recycle();
                }
            case 98:
                handleEventChargingStateChangedEvent((AsyncResult) message.obj);
                return;
        }
        plogd("CMD_EVALUATE_CARRIER_ROAMING_NTN_ELIGIBILITY_CHANGE");
        evaluateCarrierRoamingNtnEligibilityChange();
        boolean zIsCarrierRoamingNtnEligible2 = isCarrierRoamingNtnEligible(getSatellitePhone());
        plogd("CMD_EVALUATE_CARRIER_ROAMING_NTN_ELIGIBILITY_CHANGE: eligible=" + zIsCarrierRoamingNtnEligible2);
        Phone phone2 = SatelliteServiceUtils.getPhone(getSelectedSatelliteSubId());
        if (zIsCarrierRoamingNtnEligible2) {
            setLastNotifiedNtnEligibility(zIsCarrierRoamingNtnEligible2);
            phone2.notifyCarrierRoamingNtnEligibleStateChanged(zIsCarrierRoamingNtnEligible2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class RequestProvisionSatelliteArgument {
        public String mIccId;
        public boolean mProvisioned;
        public long mRequestId = SatelliteController.sNextSatelliteEnableRequestId.getAndUpdate(new LongUnaryOperator() { // from class: com.android.internal.telephony.satellite.SatelliteController$RequestProvisionSatelliteArgument$$ExternalSyntheticLambda0
            @Override // java.util.function.LongUnaryOperator
            public final long applyAsLong(long j) {
                return SatelliteController.RequestProvisionSatelliteArgument.$r8$lambda$GPPs2Guzg482dk8UecUQye1Vu18(j);
            }
        });
        public ResultReceiver mResult;
        public List<SatelliteSubscriberInfo> mSatelliteSubscriberInfoList;

        RequestProvisionSatelliteArgument(List list, ResultReceiver resultReceiver, boolean z) {
            this.mSatelliteSubscriberInfoList = list;
            this.mResult = resultReceiver;
            this.mProvisioned = z;
        }

        public static /* synthetic */ long $r8$lambda$GPPs2Guzg482dk8UecUQye1Vu18(long j) {
            return (j + 1) % Long.MAX_VALUE;
        }

        public void setIccId(String str) {
            this.mIccId = str;
        }

        public String getIccId() {
            return this.mIccId;
        }
    }

    private void handleEventConfigDataUpdated() {
        updateSupportedSatelliteServicesForActiveSubscriptions();
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (activeSubIdList != null) {
            for (int i : activeSubIdList) {
                processNewCarrierConfigData(i);
            }
            return;
        }
        ploge("updateSupportedSatelliteServicesForActiveSubscriptions: activeSubIds is null");
    }

    private void notifyRequester(SatelliteControllerHandlerRequest satelliteControllerHandlerRequest) {
        synchronized (satelliteControllerHandlerRequest) {
            satelliteControllerHandlerRequest.notifyAll();
        }
    }

    public void requestSatelliteEnabled(boolean z, boolean z2, boolean z3, IIntegerConsumer iIntegerConsumer) {
        plogd("requestSatelliteEnabled enableSatellite: " + z + " enableDemoMode: " + z2 + " isEmergency: " + z3);
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Boolean.valueOf(z);
        someArgsObtain.arg2 = Boolean.valueOf(z2);
        someArgsObtain.arg3 = Boolean.valueOf(z3);
        someArgsObtain.arg4 = iIntegerConsumer;
        sendMessage(obtainMessage(66, someArgsObtain));
    }

    private void handleRequestSatelliteEnabled(boolean z, boolean z2, boolean z3, IIntegerConsumer iIntegerConsumer) {
        SatelliteController satelliteController;
        AlertDialog alertDialog;
        plogd("handleRequestSatelliteEnabled: enableSatellite: " + z + " enableDemoMode: " + z2 + " isEmergency: " + z3);
        Objects.requireNonNull(iIntegerConsumer);
        Consumer consumerIgnoreRemoteException = FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(iIntegerConsumer));
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(true);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            sendErrorAndReportSessionMetrics(iEvaluateOemSatelliteRequestAllowed, consumerIgnoreRemoteException);
            return;
        }
        if (!z) {
            z2 = false;
        } else if (!this.mIsRadioOn.get()) {
            ploge("Radio is not on, can not enable satellite");
            sendErrorAndReportSessionMetrics(7, consumerIgnoreRemoteException);
            return;
        } else if (this.mRadioOffRequested.get()) {
            ploge("Radio is being powering off, can not enable satellite");
            sendErrorAndReportSessionMetrics(7, consumerIgnoreRemoteException);
            return;
        } else if (this.mTelecomManager.isInEmergencyCall()) {
            plogd("requestSatelliteEnabled: reject as emergency call is ongoing.");
            sendErrorAndReportSessionMetrics(27, consumerIgnoreRemoteException);
            return;
        }
        RequestSatelliteEnabledArgument requestSatelliteEnabledArgument = new RequestSatelliteEnabledArgument(z, z2, z3, consumerIgnoreRemoteException);
        Boolean isSatelliteEnabled = getIsSatelliteEnabled();
        RequestSatelliteEnabledArgument satelliteEnabledRequest = getSatelliteEnabledRequest();
        if (satelliteEnabledRequest != null && (alertDialog = this.mNetworkSelectionModeAutoDialog) != null && alertDialog.isShowing() && requestSatelliteEnabledArgument.isEmergency && requestSatelliteEnabledArgument.enableSatellite) {
            Consumer<Integer> consumer = satelliteEnabledRequest.callback;
            Objects.requireNonNull(consumer);
            sendErrorAndReportSessionMetrics(23, FunctionalUtils.ignoreRemoteException(new DataNetwork$$ExternalSyntheticLambda10(consumer)));
            setSatelliteEnabledRequest(null);
            this.mNetworkSelectionModeAutoDialog.dismiss();
            this.mNetworkSelectionModeAutoDialog = null;
        }
        if (!isSatelliteEnabledRequestInProgress()) {
            if (isSatelliteEnabled != null && isSatelliteEnabled.booleanValue() == z) {
                evaluateToUpdateSatelliteEnabledAttributes(consumerIgnoreRemoteException, 0, requestSatelliteEnabledArgument, this.mIsDemoModeEnabled.get(), this.mIsEmergency.get());
                return;
            }
            satelliteController = this;
            if (z) {
                satelliteController.setSatelliteEnabledRequest(requestSatelliteEnabledArgument);
            } else {
                satelliteController.setSatelliteDisabledRequest(requestSatelliteEnabledArgument);
            }
        } else {
            satelliteController = this;
            int i = 21;
            if (satelliteController.isSatelliteBeingDisabled()) {
                if (z) {
                    satelliteController.plogw("requestSatelliteEnabled: The enable request cannot be processed since disable satellite is in progress.");
                    i = 28;
                } else {
                    satelliteController.plogd("requestSatelliteEnabled: Disable is already in progress.");
                }
                satelliteController.sendErrorAndReportSessionMetrics(i, consumerIgnoreRemoteException);
                return;
            }
            if (z) {
                if (satelliteController.getSatelliteEnableAttributesUpdateRequest() == null) {
                    satelliteController.evaluateToUpdateSatelliteEnabledAttributes(consumerIgnoreRemoteException, 21, requestSatelliteEnabledArgument, satelliteController.getSatelliteEnabledRequest().enableDemoMode, satelliteController.getSatelliteEnabledRequest().isEmergency);
                    return;
                } else {
                    satelliteController.plogd("requestSatelliteEnabled: enable attributes update request is already in progress.");
                    satelliteController.sendErrorAndReportSessionMetrics(21, consumerIgnoreRemoteException);
                    return;
                }
            }
            if (!satelliteController.isDisableSatelliteWhileEnableInProgressSupported()) {
                satelliteController.plogd("requestSatelliteEnabled: disable satellite while enable in progress is not supported");
                satelliteController.sendErrorAndReportSessionMetrics(29, consumerIgnoreRemoteException);
                return;
            }
            satelliteController.setSatelliteDisabledRequest(requestSatelliteEnabledArgument);
        }
        Phone satellitePhone = satelliteController.getSatellitePhone();
        if (z && satellitePhone != null && satellitePhone.getServiceStateTracker() != null && satellitePhone.getServiceStateTracker().getServiceState().getIsManualSelection()) {
            satelliteController.checkNetworkSelectionModeAuto(requestSatelliteEnabledArgument);
        } else {
            satelliteController.sendRequestAsync(11, requestSatelliteEnabledArgument, null);
        }
    }

    public void requestEnableSatellite(int i, EnableRequestAttributes enableRequestAttributes, IIntegerConsumer iIntegerConsumer) {
        plogd("requestEnableSatellite: " + enableRequestAttributes);
        Objects.requireNonNull(iIntegerConsumer);
        Consumer consumerIgnoreRemoteException = FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(iIntegerConsumer));
        plogd("requestEnableSatellite: not yet supported");
        consumerIgnoreRemoteException.accept(0);
        plogd("requestEnableSatellite: falling back to manual satellite enablement");
        handleRequestSatelliteEnabled(enableRequestAttributes.isEnabled(), enableRequestAttributes.isDemoMode(), enableRequestAttributes.isEmergencyMode(), iIntegerConsumer);
    }

    private boolean isDisableSatelliteWhileEnableInProgressSupported() {
        AtomicBoolean atomicBoolean = this.mOverriddenDisableSatelliteWhileEnableInProgressSupported;
        if (atomicBoolean != null) {
            return atomicBoolean.get();
        }
        return this.mContext.getResources().getBoolean(R.bool.config_windowIsRound);
    }

    private void checkNetworkSelectionModeAuto(final RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        plogd("checkNetworkSelectionModeAuto");
        if (requestSatelliteEnabledArgument.isEmergency) {
            getSatellitePhone().setNetworkSelectionModeAutomatic(null);
            sendMessageDelayed(obtainMessage(56, requestSatelliteEnabledArgument), DELAY_WAITING_SET_NETWORK_SELECTION_AUTO_MILLIS);
            return;
        }
        AlertDialog alertDialog = this.mNetworkSelectionModeAutoDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            logd("requestSatelliteEnabled: already auto network selection mode popup showing");
            Consumer<Integer> consumer = requestSatelliteEnabledArgument.callback;
            Objects.requireNonNull(consumer);
            sendErrorAndReportSessionMetrics(21, FunctionalUtils.ignoreRemoteException(new DataNetwork$$ExternalSyntheticLambda10(consumer)));
            return;
        }
        logd("requestSatelliteEnabled: auto network selection mode popup");
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext, (Resources.getSystem().getConfiguration().uiMode & 48) == 32 ? 4 : 5);
        String string = this.mContext.getResources().getString(R.string.wfcSpnFormat_wifi_calling_bar_spn);
        String string2 = this.mContext.getResources().getString(R.string.wfcSpnFormat_wifi_call);
        builder.setTitle(string).setMessage(string2).setPositiveButton(this.mContext.getResources().getString(R.string.wfcSpnFormat_wifi_calling), new DialogInterface.OnClickListener() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda49
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.lambda$checkNetworkSelectionModeAuto$4(requestSatelliteEnabledArgument, dialogInterface, i);
            }
        }).setNegativeButton(this.mContext.getResources().getString(R.string.wfcSpnFormat_wifi), new DialogInterface.OnClickListener() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda50
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.lambda$checkNetworkSelectionModeAuto$5(requestSatelliteEnabledArgument, dialogInterface, i);
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda51
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                this.f$0.lambda$checkNetworkSelectionModeAuto$6(requestSatelliteEnabledArgument, dialogInterface);
            }
        });
        AlertDialog alertDialogCreate = builder.create();
        this.mNetworkSelectionModeAutoDialog = alertDialogCreate;
        alertDialogCreate.getWindow().setType(TelephonyProto$TelephonyEvent.RilSetupDataCallResponse.RilDataCallFailCause.PDP_FAIL_MIP_FA_MOBILE_NODE_AUTHENTICATION_FAILURE);
        this.mNetworkSelectionModeAutoDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkNetworkSelectionModeAuto$4(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument, DialogInterface dialogInterface, int i) {
        logd("checkNetworkSelectionModeAuto: setPositiveButton");
        getSatellitePhone().setNetworkSelectionModeAutomatic(null);
        sendMessageDelayed(obtainMessage(56, requestSatelliteEnabledArgument), DELAY_WAITING_SET_NETWORK_SELECTION_AUTO_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkNetworkSelectionModeAuto$5(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument, DialogInterface dialogInterface, int i) {
        logd("checkNetworkSelectionModeAuto: setNegativeButton");
        setSatelliteEnabledRequest(null);
        Consumer<Integer> consumer = requestSatelliteEnabledArgument.callback;
        Objects.requireNonNull(consumer);
        sendErrorAndReportSessionMetrics(23, FunctionalUtils.ignoreRemoteException(new DataNetwork$$ExternalSyntheticLambda10(consumer)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkNetworkSelectionModeAuto$6(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument, DialogInterface dialogInterface) {
        logd("checkNetworkSelectionModeAuto: setOnCancelListener");
        setSatelliteEnabledRequest(null);
        Consumer<Integer> consumer = requestSatelliteEnabledArgument.callback;
        Objects.requireNonNull(consumer);
        sendErrorAndReportSessionMetrics(23, FunctionalUtils.ignoreRemoteException(new DataNetwork$$ExternalSyntheticLambda10(consumer)));
    }

    private void evaluateToUpdateSatelliteEnabledAttributes(Consumer consumer, int i, RequestSatelliteEnabledArgument requestSatelliteEnabledArgument, boolean z, boolean z2) {
        boolean z3 = requestSatelliteEnabledArgument.enableDemoMode;
        if (z3 == z) {
            boolean z4 = requestSatelliteEnabledArgument.isEmergency;
            if (z4 != z2) {
                if (z4) {
                    plogd("Moving from non-emergency to emergency mode. Need to reconfigure modem");
                } else {
                    plogd("Non-emergency requests can be served during an emergency satellite session. No need to reconfigure modem.");
                }
            }
            if (i != 0) {
                plogd("requestSatelliteEnabled enable satellite is already in progress.");
            }
            consumer.accept(Integer.valueOf(i));
            return;
        }
        if (z3) {
            ploge("Moving from real mode to demo mode is rejected");
            sendErrorAndReportSessionMetrics(8, consumer);
            return;
        }
        plogd("Moving from demo mode to real mode. Need to reconfigure modem with real mode");
        setSatelliteEnableAttributesUpdateRequest(requestSatelliteEnabledArgument);
        sendRequestAsync(50, requestSatelliteEnabledArgument, null);
    }

    private boolean isSatelliteEnabledRequestInProgress() {
        RequestSatelliteEnabledArgument satelliteEnabledRequest = getSatelliteEnabledRequest();
        RequestSatelliteEnabledArgument satelliteDisabledRequest = getSatelliteDisabledRequest();
        RequestSatelliteEnabledArgument satelliteEnableAttributesUpdateRequest = getSatelliteEnableAttributesUpdateRequest();
        StringBuilder sb = new StringBuilder("mSatelliteEnabledRequest: ");
        sb.append(satelliteEnabledRequest != null);
        sb.append(", mSatelliteDisabledRequest: ");
        sb.append(satelliteDisabledRequest != null);
        sb.append(", mSatelliteEnableAttributesUpdateRequest: ");
        sb.append(satelliteEnableAttributesUpdateRequest != null);
        plogd(sb.toString());
        return (satelliteEnabledRequest == null && satelliteDisabledRequest == null && satelliteEnableAttributesUpdateRequest == null) ? false : true;
    }

    public void requestIsSatelliteEnabled(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(67, someArgsObtain));
    }

    private void handleRequestIsSatelliteEnabled(ResultReceiver resultReceiver) {
        plogd("handleRequestIsSatelliteEnabled");
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(false);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            resultReceiver.send(iEvaluateOemSatelliteRequestAllowed, null);
            return;
        }
        Boolean isSatelliteEnabled = getIsSatelliteEnabled();
        if (isSatelliteEnabled != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("satellite_enabled", isSatelliteEnabled.booleanValue());
            resultReceiver.send(0, bundle);
        } else {
            sendRequestAsync(13, resultReceiver, null);
            incrementResultReceiverCount("SC:requestIsSatelliteEnabled");
        }
    }

    public void requestEnableSatelliteStatus(int i, int i2, ResultReceiver resultReceiver) {
        plogd("requestEnableSatelliteStatus: subId: " + i + ", connectType: " + i2);
        plogd("requestEnableSatelliteStatus not yet supported");
        resultReceiver.send(0, null);
    }

    public boolean isSatelliteEnabled() {
        AtomicBoolean atomicBoolean = this.mIsSatelliteEnabled;
        if (atomicBoolean == null) {
            return false;
        }
        return atomicBoolean.get();
    }

    public boolean isSatelliteBeingEnabled() {
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        return (satelliteSessionController != null && satelliteSessionController.isInEnablingState()) || getSatelliteEnabledRequest() != null;
    }

    public boolean isSatelliteEnabledOrBeingEnabled() {
        return isSatelliteEnabled() || isSatelliteBeingEnabled();
    }

    public boolean isSatelliteBeingDisabled() {
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        return (satelliteSessionController != null && satelliteSessionController.isInDisablingState()) || getSatelliteDisabledRequest() != null;
    }

    public void requestIsDemoModeEnabled(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(68, someArgsObtain));
    }

    private void handleRequestIsDemoModeEnabled(ResultReceiver resultReceiver) {
        plogd("handleRequestIsDemoModeEnabled");
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(true);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            resultReceiver.send(iEvaluateOemSatelliteRequestAllowed, null);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("demo_mode_enabled", this.mIsDemoModeEnabled.get());
        resultReceiver.send(0, bundle);
    }

    public boolean isDemoModeEnabled() {
        return this.mIsDemoModeEnabled.get();
    }

    public void requestIsEmergencyModeEnabled(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(69, someArgsObtain));
    }

    private void handleRequestIsEmergencyModeEnabled(ResultReceiver resultReceiver) {
        plogd("handleRequestIsEmergencyModeEnabled");
        Bundle bundle = new Bundle();
        bundle.putBoolean("emergency_mode_enabled", getRequestIsEmergency());
        resultReceiver.send(0, bundle);
    }

    public void requestIsSatelliteSupported(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(70, someArgsObtain));
    }

    private void handleRequestIsSatelliteSupported(ResultReceiver resultReceiver) {
        plogd("handleRequestIsSatelliteSupported");
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        Boolean isSatelliteSupported = getIsSatelliteSupported();
        if (isSatelliteSupported != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("satellite_supported", isSatelliteSupported.booleanValue());
            bundle.putInt(SATELLITE_SUBSCRIPTION_ID, selectedSatelliteSubId);
            resultReceiver.send(0, bundle);
            return;
        }
        sendRequestAsync(15, resultReceiver, null);
    }

    public void requestSatelliteCapabilities(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(71, someArgsObtain));
    }

    private void handleRequestSatelliteCapabilities(ResultReceiver resultReceiver) {
        plogd("handleRequestSatelliteCapabilities");
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(false);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            resultReceiver.send(iEvaluateOemSatelliteRequestAllowed, null);
            return;
        }
        if (getSatelliteCapabilities() != null) {
            Bundle bundle = new Bundle();
            overrideSatelliteCapabilitiesIfApplicable();
            bundle.putParcelable("satellite_capabilities", getSatelliteCapabilities());
            resultReceiver.send(0, bundle);
            return;
        }
        sendRequestAsync(17, resultReceiver, null);
    }

    public void startSatelliteTransmissionUpdates(IIntegerConsumer iIntegerConsumer, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = iIntegerConsumer;
        someArgsObtain.arg2 = iSatelliteTransmissionUpdateCallback;
        sendMessage(obtainMessage(72, someArgsObtain));
    }

    private void handleRequestStartSatelliteTransmissionUpdates(IIntegerConsumer iIntegerConsumer, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
        plogd("handleRequestStartSatelliteTransmissionUpdates");
        Objects.requireNonNull(iIntegerConsumer);
        Consumer consumerIgnoreRemoteException = FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(iIntegerConsumer));
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(true);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            consumerIgnoreRemoteException.accept(Integer.valueOf(iEvaluateOemSatelliteRequestAllowed));
            return;
        }
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        this.mPointingAppController.registerForSatelliteTransmissionUpdates(selectedSatelliteSubId, iSatelliteTransmissionUpdateCallback);
        sendRequestAsync(1, new SatelliteTransmissionUpdateArgument(consumerIgnoreRemoteException, iSatelliteTransmissionUpdateCallback, selectedSatelliteSubId), null);
    }

    public void stopSatelliteTransmissionUpdates(IIntegerConsumer iIntegerConsumer, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = iIntegerConsumer;
        someArgsObtain.arg2 = iSatelliteTransmissionUpdateCallback;
        sendMessage(obtainMessage(73, someArgsObtain));
    }

    private void handleRequestStopSatelliteTransmissionUpdates(IIntegerConsumer iIntegerConsumer, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
        plogd("handleRequestStopSatelliteTransmissionUpdates");
        this.mPointingAppController.unregisterForSatelliteTransmissionUpdates(getSelectedSatelliteSubId(), new Consumer<Integer>() { // from class: com.android.internal.telephony.satellite.SatelliteController.8
            @Override // java.util.function.Consumer
            public void accept(Integer num) {
                SatelliteController.this.plogd("handleRequestStopSatelliteTransmissionUpdates: unregisterForSatelliteTransmissionUpdates result=" + num);
            }
        }, iSatelliteTransmissionUpdateCallback);
        Objects.requireNonNull(iIntegerConsumer);
        sendRequestAsync(3, FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(iIntegerConsumer)), null);
    }

    public ICancellationSignal provisionSatelliteService(String str, byte[] bArr, IIntegerConsumer iIntegerConsumer) {
        Objects.requireNonNull(iIntegerConsumer);
        final Consumer consumerIgnoreRemoteException = FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(iIntegerConsumer));
        final List ntnOnlySatelliteSubscriberInfoList = getNtnOnlySatelliteSubscriberInfoList(consumerIgnoreRemoteException);
        if (ntnOnlySatelliteSubscriberInfoList == null) {
            return null;
        }
        final ResultReceiver resultReceiver = new ResultReceiver(this) { // from class: com.android.internal.telephony.satellite.SatelliteController.9
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                SatelliteController.this.plogd("provisionSatelliteService: resultCode=" + i + ", resultData=" + bundle);
                consumerIgnoreRemoteException.accept(Integer.valueOf(i));
            }
        };
        provisionSatellite(ntnOnlySatelliteSubscriberInfoList, resultReceiver);
        ICancellationSignal iCancellationSignalCreateTransport = CancellationSignal.createTransport();
        CancellationSignal.fromTransport(iCancellationSignalCreateTransport).setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda48
            @Override // android.os.CancellationSignal.OnCancelListener
            public final void onCancel() {
                this.f$0.lambda$provisionSatelliteService$7(ntnOnlySatelliteSubscriberInfoList, resultReceiver);
            }
        });
        return iCancellationSignalCreateTransport;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$provisionSatelliteService$7(List list, ResultReceiver resultReceiver) {
        deprovisionSatellite(list, resultReceiver);
        this.mProvisionMetricsStats.setIsCanceled(true);
    }

    public void deprovisionSatelliteService(String str, IIntegerConsumer iIntegerConsumer) {
        Objects.requireNonNull(iIntegerConsumer);
        final Consumer consumerIgnoreRemoteException = FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(iIntegerConsumer));
        List ntnOnlySatelliteSubscriberInfoList = getNtnOnlySatelliteSubscriberInfoList(consumerIgnoreRemoteException);
        if (ntnOnlySatelliteSubscriberInfoList == null) {
            return;
        }
        deprovisionSatellite(ntnOnlySatelliteSubscriberInfoList, new ResultReceiver(this) { // from class: com.android.internal.telephony.satellite.SatelliteController.10
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                SatelliteController.this.plogd("deprovisionSatelliteService: resultCode=" + i + ", resultData=" + bundle);
                consumerIgnoreRemoteException.accept(Integer.valueOf(i));
            }
        });
    }

    public int registerForSatelliteProvisionStateChanged(ISatelliteProvisionStateCallback iSatelliteProvisionStateCallback) {
        this.mSatelliteProvisionStateChangedListeners.put(iSatelliteProvisionStateCallback.asBinder(), iSatelliteProvisionStateCallback);
        boolean zEquals = Boolean.TRUE.equals(isDeviceProvisioned());
        try {
            iSatelliteProvisionStateCallback.onSatelliteProvisionStateChanged(zEquals);
        } catch (RemoteException e) {
            loge("registerForSatelliteProvisionStateChanged: " + e);
        }
        plogd("registerForSatelliteProvisionStateChanged: report current provisioned state, state=" + zEquals);
        return 0;
    }

    public void unregisterForSatelliteProvisionStateChanged(ISatelliteProvisionStateCallback iSatelliteProvisionStateCallback) {
        this.mSatelliteProvisionStateChangedListeners.remove(iSatelliteProvisionStateCallback.asBinder());
    }

    public void requestIsSatelliteProvisioned(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(74, someArgsObtain));
    }

    private void handleRequestIsSatelliteProvisioned(ResultReceiver resultReceiver) {
        plogd("handleRequestIsSatelliteProvisioned");
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(false);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            resultReceiver.send(iEvaluateOemSatelliteRequestAllowed, null);
            return;
        }
        if (this.mIsDeviceProvisioned != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("satellite_provisioned", this.mIsDeviceProvisioned.get());
            resultReceiver.send(0, bundle);
        } else {
            sendRequestAsync(24, resultReceiver, null);
            incrementResultReceiverCount("SC:requestIsSatelliteProvisioned");
        }
    }

    public int registerForSatelliteModemStateChanged(ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        plogd("registerForSatelliteModemStateChanged: add Listeners for ModemState");
        this.mSatelliteRegistrationFailureListeners.put(iSatelliteModemStateCallback.asBinder(), iSatelliteModemStateCallback);
        this.mTerrestrialNetworkAvailableChangedListeners.put(iSatelliteModemStateCallback.asBinder(), iSatelliteModemStateCallback);
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        if (satelliteSessionController != null) {
            satelliteSessionController.registerForSatelliteModemStateChanged(iSatelliteModemStateCallback);
            return 0;
        }
        ploge("registerForSatelliteModemStateChanged: mSatelliteSessionController is not initialized yet");
        return 6;
    }

    public void unregisterForModemStateChanged(ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        if (satelliteSessionController != null) {
            satelliteSessionController.unregisterForSatelliteModemStateChanged(iSatelliteModemStateCallback);
        } else {
            ploge("unregisterForModemStateChanged: mSatelliteSessionController is not initialized yet");
        }
        plogd("unregisterForModemStateChanged: remove Listeners for ModemState");
        this.mSatelliteRegistrationFailureListeners.remove(iSatelliteModemStateCallback.asBinder());
        this.mTerrestrialNetworkAvailableChangedListeners.remove(iSatelliteModemStateCallback.asBinder());
    }

    public int registerForIncomingDatagram(ISatelliteDatagramCallback iSatelliteDatagramCallback) {
        if (!this.mSatelliteModemInterface.isSatelliteServiceSupported()) {
            return 20;
        }
        plogd("registerForIncomingDatagram: callback=" + iSatelliteDatagramCallback);
        return this.mDatagramController.registerForSatelliteDatagram(getSelectedSatelliteSubId(), iSatelliteDatagramCallback);
    }

    public void unregisterForIncomingDatagram(ISatelliteDatagramCallback iSatelliteDatagramCallback) {
        if (this.mSatelliteModemInterface.isSatelliteServiceSupported()) {
            plogd("unregisterForIncomingDatagram: callback=" + iSatelliteDatagramCallback);
            this.mDatagramController.unregisterForSatelliteDatagram(getSelectedSatelliteSubId(), iSatelliteDatagramCallback);
        }
    }

    public void pollPendingDatagrams(IIntegerConsumer iIntegerConsumer) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = iIntegerConsumer;
        sendMessage(obtainMessage(75, someArgsObtain));
    }

    private void handleRequestPollPendingDatagrams(IIntegerConsumer iIntegerConsumer) {
        plogd("handleRequestPollPendingDatagrams");
        Objects.requireNonNull(iIntegerConsumer);
        Consumer<Integer> consumerIgnoreRemoteException = FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(iIntegerConsumer));
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(true);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            consumerIgnoreRemoteException.accept(Integer.valueOf(iEvaluateOemSatelliteRequestAllowed));
        } else {
            this.mDatagramController.pollPendingSatelliteDatagrams(getSelectedSatelliteSubId(), consumerIgnoreRemoteException);
        }
    }

    public void sendDatagram(int i, SatelliteDatagram satelliteDatagram, boolean z, IIntegerConsumer iIntegerConsumer) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Integer.valueOf(i);
        someArgsObtain.arg2 = satelliteDatagram;
        someArgsObtain.arg3 = Boolean.valueOf(z);
        someArgsObtain.arg4 = iIntegerConsumer;
        sendMessage(obtainMessage(76, someArgsObtain));
    }

    private void handleRequestSendDatagram(int i, SatelliteDatagram satelliteDatagram, boolean z, IIntegerConsumer iIntegerConsumer) {
        plogd("sendSatelliteDatagram: datagramType: " + i + " needFullScreenPointingUI: " + z);
        Objects.requireNonNull(iIntegerConsumer);
        Consumer<Integer> consumerIgnoreRemoteException = FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(iIntegerConsumer));
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(true);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            consumerIgnoreRemoteException.accept(Integer.valueOf(iEvaluateOemSatelliteRequestAllowed));
            return;
        }
        if (this.mNeedsSatellitePointing.get()) {
            this.mPointingAppController.startPointingUI(z, this.mIsDemoModeEnabled.get(), this.mIsEmergency.get());
        }
        this.mDatagramController.sendSatelliteDatagram(getSelectedSatelliteSubId(), i, satelliteDatagram, z, consumerIgnoreRemoteException);
    }

    public void requestTimeForNextSatelliteVisibility(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(77, someArgsObtain));
    }

    private void handleRequestTimeForNextSatelliteVisibility(ResultReceiver resultReceiver) {
        plogd("handleRequestTimeForNextSatelliteVisibility");
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(true);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            resultReceiver.send(iEvaluateOemSatelliteRequestAllowed, null);
        } else {
            sendRequestAsync(21, resultReceiver, null);
            incrementResultReceiverCount("SC:requestTimeForNextSatelliteVisibility");
        }
    }

    public void setDeviceAlignedWithSatellite(boolean z) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Boolean.valueOf(z);
        sendMessage(obtainMessage(80, someArgsObtain));
    }

    private void handleRequestSetDeviceAlignedWithSatellite(boolean z) {
        plogd("handleSetDeviceAlignedWithSatellite: isAligned=" + z);
        DemoSimulator.getInstance().setDeviceAlignedWithSatellite(z);
        this.mDatagramController.setDeviceAlignedWithSatellite(z);
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        if (satelliteSessionController != null) {
            satelliteSessionController.setDeviceAlignedWithSatellite(z);
        } else {
            ploge("setDeviceAlignedWithSatellite: mSatelliteSessionController is not initialized yet");
        }
    }

    public void addAttachRestrictionForCarrier(int i, int i2, IIntegerConsumer iIntegerConsumer) {
        logd("addAttachRestrictionForCarrier(" + i + ", " + i2 + ")");
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Integer.valueOf(i);
        someArgsObtain.arg2 = Integer.valueOf(i2);
        someArgsObtain.arg3 = iIntegerConsumer;
        sendMessage(obtainMessage(81, someArgsObtain));
    }

    private void handleRequestAddAttachRestrictionForCarrier(int i, int i2, IIntegerConsumer iIntegerConsumer) {
        plogd("handleRequestAddAttachRestrictionForCarrier: subId=" + i + " reason=" + i2);
        Objects.requireNonNull(iIntegerConsumer);
        Consumer consumerIgnoreRemoteException = FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(iIntegerConsumer));
        boolean zIsEmpty = this.mSatelliteAttachRestrictionForCarrierArray.getOrDefault(Integer.valueOf(i), Collections.EMPTY_SET).isEmpty();
        ConcurrentHashMap<Integer, Set<Integer>> concurrentHashMap = this.mSatelliteAttachRestrictionForCarrierArray;
        if (zIsEmpty) {
            concurrentHashMap.put(Integer.valueOf(i), new HashSet());
        } else if (concurrentHashMap.get(Integer.valueOf(i)).contains(Integer.valueOf(i2))) {
            consumerIgnoreRemoteException.accept(0);
            return;
        }
        this.mSatelliteAttachRestrictionForCarrierArray.get(Integer.valueOf(i)).add(Integer.valueOf(i2));
        plogd("handleRequestAddAttachRestrictionForCarrier: subId=" + i + ", reasons=" + this.mSatelliteAttachRestrictionForCarrierArray.get(Integer.valueOf(i)));
        sendRequestAsync(30, new RequestHandleSatelliteAttachRestrictionForCarrierArgument(i, i2, consumerIgnoreRemoteException), SatelliteServiceUtils.getPhone(i));
    }

    public void removeAttachRestrictionForCarrier(int i, int i2, IIntegerConsumer iIntegerConsumer) {
        logd("removeAttachRestrictionForCarrier(" + i + ", " + i2 + ")");
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Integer.valueOf(i);
        someArgsObtain.arg2 = Integer.valueOf(i2);
        someArgsObtain.arg3 = iIntegerConsumer;
        sendMessage(obtainMessage(82, someArgsObtain));
    }

    private void handleRequestRemoveAttachRestrictionForCarrier(int i, int i2, IIntegerConsumer iIntegerConsumer) {
        plogd("handleRequestRemoveAttachRestrictionForCarrier: subId=" + i + " reason=" + i2);
        Objects.requireNonNull(iIntegerConsumer);
        Consumer consumerIgnoreRemoteException = FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(iIntegerConsumer));
        if (this.mSatelliteAttachRestrictionForCarrierArray.getOrDefault(Integer.valueOf(i), Collections.EMPTY_SET).isEmpty() || !this.mSatelliteAttachRestrictionForCarrierArray.get(Integer.valueOf(i)).contains(Integer.valueOf(i2))) {
            consumerIgnoreRemoteException.accept(0);
            return;
        }
        this.mSatelliteAttachRestrictionForCarrierArray.get(Integer.valueOf(i)).remove(Integer.valueOf(i2));
        plogd("handleRequestRemoveAttachRestrictionForCarrier: subId=" + i + ", reasons=" + this.mSatelliteAttachRestrictionForCarrierArray.get(Integer.valueOf(i)));
        sendRequestAsync(30, new RequestHandleSatelliteAttachRestrictionForCarrierArgument(i, i2, consumerIgnoreRemoteException), SatelliteServiceUtils.getPhone(i));
    }

    public Set<Integer> getAttachRestrictionReasonsForCarrier(int i) {
        Set<Integer> set = this.mSatelliteAttachRestrictionForCarrierArray.get(Integer.valueOf(i));
        if (set == null) {
            return new HashSet();
        }
        return new HashSet(set);
    }

    public void requestNtnSignalStrength(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(83, someArgsObtain));
    }

    private void handleRequestNtnSignalStrength(ResultReceiver resultReceiver) {
        plogd("handleRequestNtnSignalStrength");
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(true);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            resultReceiver.send(iEvaluateOemSatelliteRequestAllowed, null);
            return;
        }
        NtnSignalStrength ntnSignalStrength = getNtnSignalStrength();
        if (ntnSignalStrength.getLevel() != 0) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("ntn_signal_strength", ntnSignalStrength);
            resultReceiver.send(0, bundle);
        } else {
            sendRequestAsync(32, resultReceiver, SatelliteServiceUtils.getPhone());
            incrementResultReceiverCount("SC:requestNtnSignalStrength");
        }
    }

    public void registerForNtnSignalStrengthChanged(INtnSignalStrengthCallback iNtnSignalStrengthCallback) throws RemoteException {
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(false);
        if (iEvaluateOemSatelliteRequestAllowed == 0) {
            this.mNtnSignalStrengthChangedListeners.put(iNtnSignalStrengthCallback.asBinder(), iNtnSignalStrengthCallback);
            NtnSignalStrength ntnSignalStrength = getNtnSignalStrength();
            try {
                iNtnSignalStrengthCallback.onNtnSignalStrengthChanged(ntnSignalStrength);
                plogd("registerForNtnSignalStrengthChanged: " + ntnSignalStrength);
                return;
            } catch (RemoteException e) {
                ploge("registerForNtnSignalStrengthChanged: RemoteException ex=" + e);
                return;
            }
        }
        MiuiPhoneStub$$ExternalSyntheticBUOutline0.m("registration fails: ", iEvaluateOemSatelliteRequestAllowed);
    }

    public void unregisterForNtnSignalStrengthChanged(INtnSignalStrengthCallback iNtnSignalStrengthCallback) {
        this.mNtnSignalStrengthChangedListeners.remove(iNtnSignalStrengthCallback.asBinder());
    }

    public int registerForCapabilitiesChanged(ISatelliteCapabilitiesCallback iSatelliteCapabilitiesCallback) {
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(false);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            return iEvaluateOemSatelliteRequestAllowed;
        }
        this.mSatelliteCapabilitiesChangedListeners.put(iSatelliteCapabilitiesCallback.asBinder(), iSatelliteCapabilitiesCallback);
        SatelliteCapabilities satelliteCapabilities = getSatelliteCapabilities();
        logd("registerForCapabilitiesChanged: capabilities=" + satelliteCapabilities);
        try {
            if (satelliteCapabilities != null) {
                iSatelliteCapabilitiesCallback.onSatelliteCapabilitiesChanged(satelliteCapabilities);
            } else {
                logd("registerForCapabilitiesChanged: capabilities is null");
            }
        } catch (RemoteException e) {
            ploge("registerForCapabilitiesChanged: RemoteException ex=" + e);
        }
        return 0;
    }

    public void unregisterForCapabilitiesChanged(ISatelliteCapabilitiesCallback iSatelliteCapabilitiesCallback) {
        this.mSatelliteCapabilitiesChangedListeners.remove(iSatelliteCapabilitiesCallback.asBinder());
    }

    public int registerForSatelliteSupportedStateChanged(final IBooleanConsumer iBooleanConsumer) {
        this.mSatelliteSupportedStateChangedListeners.put(iBooleanConsumer.asBinder(), iBooleanConsumer);
        Boolean isSatelliteSupported = getIsSatelliteSupported();
        if (isSatelliteSupported != null) {
            final boolean zBooleanValue = isSatelliteSupported.booleanValue();
            post(new Runnable() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$registerForSatelliteSupportedStateChanged$8(iBooleanConsumer, zBooleanValue);
                }
            });
            return 0;
        }
        logd("registerForSatelliteSupportedStateChanged: cached supported state is null");
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerForSatelliteSupportedStateChanged$8(IBooleanConsumer iBooleanConsumer, boolean z) {
        try {
            iBooleanConsumer.accept(z);
        } catch (RemoteException e) {
            ploge("registerForSatelliteSupportedStateChanged: RemoteException ex=" + e);
        }
    }

    public void unregisterForSatelliteSupportedStateChanged(IBooleanConsumer iBooleanConsumer) {
        this.mSatelliteSupportedStateChangedListeners.remove(iBooleanConsumer.asBinder());
    }

    public int registerForSelectedNbIotSatelliteSubscriptionChanged(ISelectedNbIotSatelliteSubscriptionCallback iSelectedNbIotSatelliteSubscriptionCallback) {
        int iEvaluateOemSatelliteRequestAllowed = evaluateOemSatelliteRequestAllowed(false);
        if (iEvaluateOemSatelliteRequestAllowed != 0) {
            return iEvaluateOemSatelliteRequestAllowed;
        }
        this.mSelectedNbIotSatelliteSubscriptionChangedListeners.put(iSelectedNbIotSatelliteSubscriptionCallback.asBinder(), iSelectedNbIotSatelliteSubscriptionCallback);
        try {
            iSelectedNbIotSatelliteSubscriptionCallback.onSelectedNbIotSatelliteSubscriptionChanged(getSelectedSatelliteSubId());
        } catch (RemoteException e) {
            ploge("registerForSelectedNbIotSatelliteSubscriptionChanged: RemoteException ex=" + e);
        }
        return 0;
    }

    public void unregisterForSelectedNbIotSatelliteSubscriptionChanged(ISelectedNbIotSatelliteSubscriptionCallback iSelectedNbIotSatelliteSubscriptionCallback) {
        if (evaluateOemSatelliteRequestAllowed(true) == 0) {
            this.mSelectedNbIotSatelliteSubscriptionChangedListeners.remove(iSelectedNbIotSatelliteSubscriptionCallback.asBinder());
        }
    }

    public boolean setSatelliteServicePackageName(String str, String str2) {
        if (!isMockModemAllowed()) {
            plogd("setSatelliteServicePackageName: mock modem not allowed");
            return false;
        }
        plogd("setSatelliteServicePackageName: Resetting cached states, provisioned=" + str2);
        this.mIsSatelliteSupported = null;
        this.mIsSatelliteEnabled = null;
        setSatelliteCapabilities(null);
        this.mSatelliteModemInterface.setSatelliteServicePackageName(str);
        return true;
    }

    public boolean setSatelliteListeningTimeoutDuration(long j) {
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        if (satelliteSessionController == null) {
            ploge("mSatelliteSessionController is not initialized yet");
            return false;
        }
        return satelliteSessionController.setSatelliteListeningTimeoutDuration(j);
    }

    public boolean setTnScanningSupport(boolean z, boolean z2, boolean z3) {
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        if (satelliteSessionController == null) {
            ploge("setTnScanningSupport: mSatelliteSessionController is not initialized yet");
            return false;
        }
        return satelliteSessionController.setTnScanningSupport(z, z2, z3);
    }

    public boolean setSatelliteIgnoreCellularServiceState(boolean z) {
        plogd("setSatelliteIgnoreCellularServiceState - " + z);
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        if (satelliteSessionController == null) {
            ploge("setSatelliteIgnoreCellularServiceState is not initialized yet");
            return false;
        }
        return satelliteSessionController.setSatelliteIgnoreCellularServiceState(z);
    }

    public boolean setSupportDisableSatelliteWhileEnableInProgress(boolean z, boolean z2) {
        if (!isMockModemAllowed()) {
            plogd("setSupportDisableSatelliteWhileEnableInProgress: mock modem not allowed");
            return false;
        }
        plogd("setSupportDisableSatelliteWhileEnableInProgress - reset=" + z + ", supported=" + z2);
        if (z) {
            this.mOverriddenDisableSatelliteWhileEnableInProgressSupported = null;
            return true;
        }
        this.mOverriddenDisableSatelliteWhileEnableInProgressSupported = new AtomicBoolean(z2);
        return true;
    }

    public boolean setMaxAllowedDataModeForCtsTest(int i) {
        if (!isMockModemAllowed()) {
            plogd("setMaxAllowedDataModeForCtsTest: mock modem not allowed");
            return false;
        }
        plogd("setMaxAllowedDataModeForCtsTest - maxAllowedDataMode=" + i);
        this.mMaxAllowedDataModeForCtsTest.set(i);
        return true;
    }

    public boolean uncapMaxAllowedDataMode() {
        if (!DEBUG) {
            plogd("uncapMaxAllowedDataMode: Cannot uncap max allowed data mode on non debug builds");
            return false;
        }
        plogd("uncapMaxAllowedDataMode: Uncapping max allowed data mode");
        this.mUncapMaxAllowedDataMode.set(true);
        return true;
    }

    public boolean setDatagramControllerTimeoutDuration(boolean z, int i, long j) {
        plogd("setDatagramControllerTimeoutDuration: reset=" + z + ", timeoutType=" + i + ", timeoutMillis=" + j);
        return this.mDatagramController.setDatagramControllerTimeoutDuration(z, i, j);
    }

    public boolean setDatagramControllerBooleanConfig(boolean z, int i, boolean z2) {
        logd("setDatagramControllerBooleanConfig: reset=" + z + ", booleanType=" + i + ", enable=" + z2);
        return this.mDatagramController.setDatagramControllerBooleanConfig(z, i, z2);
    }

    public boolean setSatelliteControllerTimeoutDuration(boolean z, int i, long j) {
        if (!isMockModemAllowed()) {
            plogd("setSatelliteControllerTimeoutDuration: mock modem is not allowed");
            return false;
        }
        plogd("setSatelliteControllerTimeoutDuration: reset=" + z + ", timeoutType=" + i + ", timeoutMillis=" + j);
        if (i == 1) {
            AtomicLong atomicLong = this.mWaitTimeForSatelliteEnablingResponse;
            if (z) {
                atomicLong.set(getWaitForSatelliteEnablingResponseTimeoutMillis());
            } else {
                atomicLong.set(j);
            }
            plogd("mWaitTimeForSatelliteEnablingResponse=" + this.mWaitTimeForSatelliteEnablingResponse.get());
        } else if (i == 2) {
            AtomicLong atomicLong2 = this.mDemoPointingAlignedDurationMillis;
            if (z) {
                atomicLong2.set(getDemoPointingAlignedDurationMillisFromResources());
            } else {
                atomicLong2.set(j);
            }
        } else if (i == 3) {
            AtomicLong atomicLong3 = this.mDemoPointingNotAlignedDurationMillis;
            if (z) {
                atomicLong3.set(getDemoPointingNotAlignedDurationMillisFromResources());
            } else {
                atomicLong3.set(j);
            }
        } else if (i == 4) {
            AtomicLong atomicLong4 = this.mEvaluateEsosProfilesPrioritizationDurationMillis;
            if (z) {
                atomicLong4.set(getEvaluateEsosProfilesPrioritizationDurationMillis());
            } else {
                atomicLong4.set(j);
            }
        } else if (i == 5) {
            AtomicLong atomicLong5 = this.mEmergencyCallMonitoringDurationMillisForCtsTest;
            if (z) {
                atomicLong5.set(0L);
            } else {
                atomicLong5.set(j);
            }
        } else if (i == 6) {
            AtomicLong atomicLong6 = this.mLastEmergencyCallTime;
            if (z) {
                atomicLong6.set(0L);
            } else {
                atomicLong6.set(j);
            }
        } else {
            plogw("Invalid timeoutType=" + i);
            return false;
        }
        return true;
    }

    public boolean setSatelliteGatewayServicePackageName(String str) {
        if (!isMockModemAllowed()) {
            plogd("setSatelliteGatewayServicePackageName: mock modem is not allowed");
            return false;
        }
        if (this.mSatelliteSessionController == null) {
            ploge("mSatelliteSessionController is not initialized yet");
            return false;
        }
        if (str == null || str.equals("null")) {
            this.mOverriddenSatelliteGatewayServicePackageName = null;
        } else {
            this.mOverriddenSatelliteGatewayServicePackageName = str;
        }
        plogd("setSatelliteGatewayServicePackageName: mOverriddenSatelliteGatewayServicePackageName=" + this.mOverriddenSatelliteGatewayServicePackageName);
        return this.mSatelliteSessionController.setSatelliteGatewayServicePackageName(str);
    }

    public boolean setSatellitePointingUiClassName(String str, String str2) {
        return this.mPointingAppController.setSatellitePointingUiClassName(str, str2);
    }

    public boolean setEmergencyCallToSatelliteHandoverType(int i, int i2, int i3) {
        if (!isMockModemAllowed()) {
            ploge("setEmergencyCallToSatelliteHandoverType: mock modem not allowed");
            return false;
        }
        boolean zIsHandoverTypeValid = isHandoverTypeValid(i);
        AtomicInteger atomicInteger = this.mEnforcedEmergencyCallToSatelliteHandoverType;
        if (zIsHandoverTypeValid) {
            atomicInteger.set(i);
            AtomicInteger atomicInteger2 = this.mDelayInSendingEventDisplayEmergencyMessage;
            if (i2 <= 0) {
                i2 = 0;
            }
            atomicInteger2.set(i2);
            this.mSimSlotIdForLaunchingT911ConversationThread.set(i3);
            return true;
        }
        atomicInteger.set(-1);
        this.mDelayInSendingEventDisplayEmergencyMessage.set(0);
        this.mSimSlotIdForLaunchingT911ConversationThread.set(0);
        return true;
    }

    public boolean setOemEnabledSatelliteProvisionStatus(boolean z, boolean z2) {
        if (!isMockModemAllowed()) {
            ploge("setOemEnabledSatelliteProvisionStatus: mock modem not allowed");
            return false;
        }
        if (z) {
            this.mOverriddenIsSatelliteViaOemProvisioned = null;
            return true;
        }
        this.mOverriddenIsSatelliteViaOemProvisioned = new AtomicBoolean(z2);
        return true;
    }

    protected int getEnforcedEmergencyCallToSatelliteHandoverType() {
        return this.mEnforcedEmergencyCallToSatelliteHandoverType.get();
    }

    protected int getDelayInSendingEventDisplayEmergencyMessage() {
        return this.mDelayInSendingEventDisplayEmergencyMessage.get();
    }

    protected int getSimSlotIdForLaunchingT911ConversationThread() {
        return this.mSimSlotIdForLaunchingT911ConversationThread.get();
    }

    protected long getEmergencyCallMonitoringDurationMillisForCtsTests() {
        return this.mEmergencyCallMonitoringDurationMillisForCtsTest.get();
    }

    public void onSatelliteServiceConnected() {
        if (this.mSatelliteModemInterface.isSatelliteServiceSupported()) {
            plogd("onSatelliteServiceConnected");
            moveSatelliteToOffStateAndCleanUpResources(4);
            requestIsSatelliteSupported(new ResultReceiver(this) { // from class: com.android.internal.telephony.satellite.SatelliteController.11
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i, Bundle bundle) {
                    SatelliteController.this.decrementResultReceiverCount("SC:onSatelliteServiceConnected");
                    SatelliteController.this.plogd("onSatelliteServiceConnected.requestIsSatelliteSupported: resultCode=" + i);
                }
            });
            incrementResultReceiverCount("SC:onSatelliteServiceConnected");
            return;
        }
        plogd("onSatelliteServiceConnected: Satellite vendor service is not supported. Ignored the event");
    }

    public void onSetCellularRadioPowerStateRequested(boolean z) {
        logd("onSetCellularRadioPowerStateRequested: powerOn=" + z);
        this.mRadioOffRequested.set(z ^ true);
        if (z) {
            stopWaitForCellularModemOffTimer();
        } else {
            requestSatelliteEnabled(false, false, false, new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteController.12
                public void accept(int i) {
                    SatelliteController.this.plogd("onSetCellularRadioPowerStateRequested: requestSatelliteEnabled result=" + i);
                }
            });
            startWaitForCellularModemOffTimer();
        }
    }

    public void onPowerOffCellularRadioFailed() {
        logd("onPowerOffCellularRadioFailed");
        this.mRadioOffRequested.set(false);
        stopWaitForCellularModemOffTimer();
    }

    public void onSmsReceived(int i) {
        if (!isSatelliteEnabled()) {
            logd("onSmsReceived: satellite is not enabled");
            return;
        }
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        if (i != selectedSatelliteSubId) {
            logd("onSmsReceived: SMS received " + i + ", but not satellite subscription " + selectedSatelliteSubId);
            return;
        }
        DatagramController datagramController = this.mDatagramController;
        if (datagramController != null) {
            datagramController.onSmsReceived(i);
        } else {
            logd("onSmsReceived: DatagramController is not initialized");
        }
        this.mControllerMetricsStats.reportIncomingNtnSmsCount(0);
    }

    public boolean isSatelliteSupportedViaOem() {
        Boolean boolIsSatelliteSupportedViaOemInternal = isSatelliteSupportedViaOemInternal();
        if (boolIsSatelliteSupportedViaOemInternal != null) {
            return boolIsSatelliteSupportedViaOemInternal.booleanValue();
        }
        return false;
    }

    public List<String> getSatellitePlmnsForCarrier(int i) {
        if (!isSatelliteSupportedViaCarrier(i)) {
            logd("Satellite for carrier is not supported.");
            return new ArrayList();
        }
        return getCarrierPlmnList(i);
    }

    private int getCarrierRoamingNtnConnectTypeForPlmn(int i, String str) {
        plogd("getCarrierRoamingNtnConnectTypeForPlmn: subId=" + i + ", plmn=" + str);
        Integer ntnConnectTypeViaConfigupdater = getNtnConnectTypeViaConfigupdater(i, str);
        if (ntnConnectTypeViaConfigupdater != null) {
            logd("getCarrierRoamingNtnConnectTypeForPlmn: ntnConnectTypePerPlmnViaConfigUpdater=" + ntnConnectTypeViaConfigupdater);
            return ntnConnectTypeViaConfigupdater.intValue();
        }
        PersistableBundle persistableBundle = getPersistableBundle(i).getPersistableBundle("satellite_configs_per_plmn_bundle");
        if (persistableBundle == null) {
            plogd("getCarrierRoamingNtnConnectTypeForPlmn: perPlmnConfigs is null");
            return -1;
        }
        PersistableBundle persistableBundle2 = persistableBundle.getPersistableBundle(str);
        if (persistableBundle2 == null) {
            plogd("getCarrierRoamingNtnConnectTypeForPlmn: plmnSpecificConfig is null");
            return -1;
        }
        return persistableBundle2.getInt("carrier_roaming_ntn_connect_type_int", -1);
    }

    public List<String> getManualConnectSatellitePlmnsForCarrier(int i) {
        ArrayList arrayList = new ArrayList();
        List<String> satellitePlmnsForCarrier = getSatellitePlmnsForCarrier(i);
        int carrierRoamingNtnConnectType = getCarrierRoamingNtnConnectType(i);
        if (carrierRoamingNtnConnectType == 1) {
            return satellitePlmnsForCarrier;
        }
        if (carrierRoamingNtnConnectType == 2) {
            for (String str : satellitePlmnsForCarrier) {
                if (getCarrierRoamingNtnConnectTypeForPlmn(i, str) == 1 || getSupportedSatelliteTechnologies(i, str).contains(1)) {
                    arrayList.add(str);
                }
            }
        }
        return arrayList;
    }

    private boolean isDataServiceUpdateRequired(int i, String str, List list) {
        if (list.contains(2) || getCarrierSatelliteDataSupportedModeFromConfig(i) != 0) {
            return false;
        }
        return getSatelliteSupportedServicesFromConfig(i, str).contains(2);
    }

    private boolean isMmsServiceUpdateRequired(int i, String str, List list) {
        if (list.contains(6)) {
            return false;
        }
        return getSatelliteSupportedServicesFromConfig(i, str).contains(6);
    }

    private List getSatelliteSupportedServicesFromConfig(int i, String str) {
        if (str != null && !str.isEmpty()) {
            if (this.mSatelliteServicesSupportedByCarriersFromConfig.containsKey(Integer.valueOf(i))) {
                Map map = (Map) this.mSatelliteServicesSupportedByCarriersFromConfig.get(Integer.valueOf(i));
                if (map != null && map.containsKey(str)) {
                    logd("getSupportedSatelliteServices: returning supported services from config " + map.get(str));
                    return new ArrayList((Collection) map.get(str));
                }
                loge("getSupportedSatelliteServices: subId=" + i + ", supportedServices does not contain key plmn=" + str);
            } else {
                loge("getSupportedSatelliteServices: mSatelliteServicesSupportedByCarriersFromConfig does not contain key subId=" + i);
            }
        }
        int[] intArray = getPersistableBundle(i).getIntArray("carrier_roaming_satellite_default_services_int_array");
        if (intArray == null) {
            logd("getSupportedSatelliteServices: defaultCapabilities is null");
            return new ArrayList();
        }
        List list = (List) Arrays.stream(intArray).boxed().collect(Collectors.toList());
        logd("getSupportedSatelliteServices: subId=" + i + ", supportedServices does not contain key plmn=" + str + ", return default values " + list);
        return list;
    }

    public List<Integer> getSupportedSatelliteServicesForPlmn(int i, String str) {
        Map map;
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            logd("getSupportedSatelliteServices: invalid sub id");
            return new ArrayList();
        }
        if (str != null && !str.isEmpty() && (map = (Map) this.mEntitlementServiceTypeMapPerCarrier.get(Integer.valueOf(i))) != null && map.containsKey(str)) {
            ArrayList arrayList = new ArrayList((Collection) map.get(str));
            if (!arrayList.isEmpty()) {
                if (isDataServiceUpdateRequired(i, str, arrayList)) {
                    logd("getSupportedSatelliteServices: data service added to satellite plmn");
                    arrayList.add(2);
                }
                if (arrayList.contains(2) && isMmsServiceUpdateRequired(i, str, arrayList)) {
                    arrayList.add(6);
                }
                logd("getSupportedSatelliteServices: allowed services=" + arrayList);
                return arrayList;
            }
        }
        return getSatelliteSupportedServicesFromConfig(i, str);
    }

    public boolean isSatelliteAttachRequired() {
        SatelliteCapabilities satelliteCapabilities = getSatelliteCapabilities();
        if (satelliteCapabilities != null) {
            return satelliteCapabilities.getSupportedRadioTechnologies().contains(1);
        }
        ploge("isSatelliteAttachRequired: mSatelliteCapabilities is null");
        return false;
    }

    public boolean isSatelliteSupportedViaCarrier() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (isSatelliteSupportedViaCarrier(phone.getSubId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isSatelliteEmergencyMessagingSupportedViaCarrier() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (isSatelliteEmergencyMessagingSupportedViaCarrier(phone.getSubId())) {
                return true;
            }
        }
        return false;
    }

    private Boolean isSatelliteEmergencyMessagingSupportedViaConfigupdater(int i) {
        SatelliteConfig satelliteConfig = getSatelliteConfig();
        if (satelliteConfig == null) {
            logd("isSatelliteEmergencyMessagingSupportedViaConfigupdater: return null (satelliteConfig is null)");
            return null;
        }
        return satelliteConfig.isEmergencyMessagingSupportedBySubId(i);
    }

    private boolean isSatelliteEmergencyMessagingSupportedViaCarrier(int i) {
        if (!isSatelliteSupportedViaCarrier(i)) {
            return false;
        }
        Boolean boolIsSatelliteEmergencyMessagingSupportedViaConfigupdater = isSatelliteEmergencyMessagingSupportedViaConfigupdater(i);
        if (boolIsSatelliteEmergencyMessagingSupportedViaConfigupdater != null) {
            plogd("isSatelliteEmergencyMessagingSupportedViaCarrier: using SatelliteConfig for subId=" + i + ", isEmergencyMsgSupportViaConfigupdater=" + boolIsSatelliteEmergencyMessagingSupportedViaConfigupdater);
            return boolIsSatelliteEmergencyMessagingSupportedViaConfigupdater.booleanValue();
        }
        if (getPersistableBundle(i).getBoolean("emergency_messaging_supported_bool")) {
            return isSatelliteEmergencyMessagingProviderSupportedInCurrentRegion(i);
        }
        return false;
    }

    public Pair<Boolean, Integer> isUsingNonTerrestrialNetworkViaCarrier() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (isUsingNonTerrestrialNetworkViaCarrier(phone.getSubId())) {
                return new Pair<>(Boolean.TRUE, Integer.valueOf(phone.getSubId()));
            }
        }
        return new Pair<>(Boolean.FALSE, null);
    }

    public boolean isUsingNonTerrestrialNetworkViaCarrier(int i) {
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
        boolean z = false;
        if (phone == null) {
            logd("isUsingNonTerrestrialNetworkViaCarrier: phone is null for subId=" + i);
            return false;
        }
        ServiceState serviceState = phone.getServiceState();
        if (serviceState != null && serviceState.isUsingNonTerrestrialNetwork()) {
            z = true;
        }
        logd("isUsingNonTerrestrialNetworkViaCarrier: subId=" + i + " isUsingNtn=" + z);
        return z;
    }

    public Pair<Boolean, Integer> isSatelliteConnectedViaCarrierWithinHysteresisTime() {
        Pair<Boolean, Integer> pairIsUsingNonTerrestrialNetworkViaCarrier = isUsingNonTerrestrialNetworkViaCarrier();
        if (((Boolean) pairIsUsingNonTerrestrialNetworkViaCarrier.first).booleanValue()) {
            return pairIsUsingNonTerrestrialNetworkViaCarrier;
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            if (isInSatelliteModeForCarrierRoaming(phone)) {
                logd("isSatelliteConnectedViaCarrierWithinHysteresisTime: subId:" + phone.getSubId() + " is connected to satellite within hysteresis time");
                return new Pair<>(Boolean.TRUE, Integer.valueOf(phone.getSubId()));
            }
        }
        return new Pair<>(Boolean.FALSE, null);
    }

    public boolean isInSatelliteModeForCarrierRoaming(Phone phone) {
        ServiceState serviceState;
        if (phone == null) {
            return false;
        }
        int subId = phone.getSubId();
        int carrierRoamingNtnConnectType = getCarrierRoamingNtnConnectType(subId);
        if (carrierRoamingNtnConnectType == 1 || carrierRoamingNtnConnectType == 2) {
            return isInCarrierRoamingNbIotNtn(phone);
        }
        if (!isSatelliteSupportedViaCarrier(subId) || (serviceState = phone.getServiceState()) == null) {
            return false;
        }
        if (serviceState.isUsingNonTerrestrialNetwork()) {
            return true;
        }
        if (getWwanIsInService(serviceState) || serviceState.getState() == 3) {
            resetCarrierRoamingSatelliteModeParams(subId);
            return false;
        }
        Long l = (Long) this.mLastSatelliteDisconnectedTimesMillis.get(Integer.valueOf(subId));
        long satelliteConnectionHysteresisTimeMillis = getSatelliteConnectionHysteresisTimeMillis(subId);
        if (l != null && getElapsedRealtime() - l.longValue() <= satelliteConnectionHysteresisTimeMillis) {
            logd("isInSatelliteModeForCarrierRoaming: subId:" + subId + " is connected to satellite within hysteresis time");
            return true;
        }
        logd("isInSatelliteModeForCarrierRoaming: subId:" + subId + " is not connected to satellite within hysteresis time. Resetting carrier roaming satellite mode params");
        resetCarrierRoamingSatelliteModeParams(subId);
        return false;
    }

    public boolean shouldTurnOffCarrierSatelliteForEmergencyCall() {
        return !this.mDatagramController.isEmergencyCommunicationEstablished() && getConfigForSubId(getSelectedSatelliteSubId()).getBoolean("satellite_roaming_turn_off_session_for_emergency_call_bool");
    }

    public boolean getRequestIsEmergency() {
        return this.mIsEmergency.get();
    }

    public boolean isInCarrierRoamingNbIotNtn() {
        return isInCarrierRoamingNbIotNtn(getSatellitePhone());
    }

    public boolean isInCarrierRoamingNbIotNtn(Phone phone) {
        if (!isSatelliteEnabled()) {
            plogd("isInCarrierRoamingNbIotNtn: satellite is disabled");
            return false;
        }
        if (phone == null) {
            plogd("isInCarrierRoamingNbIotNtn: phone is null");
            return false;
        }
        int subId = phone.getSubId();
        if (!isSatelliteSupportedViaCarrier(subId)) {
            plogd("isInCarrierRoamingNbIotNtn[phoneId=" + phone.getPhoneId() + "]: satellite is not supported via carrier");
            return false;
        }
        int carrierRoamingNtnConnectType = getCarrierRoamingNtnConnectType(subId);
        if (carrierRoamingNtnConnectType != 1 && carrierRoamingNtnConnectType != 2) {
            plogd("isInCarrierRoamingNbIotNtn[phoneId=" + phone.getPhoneId() + "]: not manual nor hybrid connect. connectType = " + carrierRoamingNtnConnectType);
            return false;
        }
        if (subId != getSelectedSatelliteSubId()) {
            plogd("isInCarrierRoamingNbIotNtn: subId=" + subId + " does not match satellite subId=" + getSelectedSatelliteSubId());
            return false;
        }
        plogd("isInCarrierRoamingNbIotNtn: carrier roaming ntn eligible for phone associated with subId " + phone.getSubId());
        return true;
    }

    public List<Integer> getCapabilitiesForCarrierRoamingSatelliteMode(Phone phone) {
        int subId = phone.getSubId();
        if (this.mSatModeCapabilitiesForCarrierRoaming.containsKey(Integer.valueOf(subId))) {
            plogd("getCapabilitiesForCarrierRoamingSatelliteMode: subId=" + subId + " capabilities=" + this.mSatModeCapabilitiesForCarrierRoaming.get(Integer.valueOf(subId)));
            return (List) this.mSatModeCapabilitiesForCarrierRoaming.get(Integer.valueOf(subId));
        }
        plogd("getCapabilitiesForCarrierRoamingSatelliteMode: subId=" + subId + " returning empty");
        return new ArrayList();
    }

    public int getSatelliteTechnologyForCarrierRoaming(Phone phone) {
        int subId = phone.getSubId();
        if (this.mCurrentSatellitePerPlmnConfigurations.containsKey(Integer.valueOf(subId))) {
            return ((SatellitePerPlmnConfiguration) this.mCurrentSatellitePerPlmnConfigurations.get(Integer.valueOf(subId))).currentSatelliteTechnology;
        }
        return 0;
    }

    public void requestSatelliteSessionStats(int i, ResultReceiver resultReceiver) {
        this.mSessionMetricsStats.requestSatelliteSessionStats(i, resultReceiver);
    }

    public long getCarrierEmergencyCallWaitForConnectionTimeoutMillis() {
        long j = 0;
        for (Phone phone : PhoneFactory.getPhones()) {
            if (isSatelliteEmergencyMessagingSupportedViaCarrier(phone.getSubId())) {
                int carrierEmergencyCallWaitForConnectionTimeoutMillis = getCarrierEmergencyCallWaitForConnectionTimeoutMillis(phone.getSubId());
                if (isInSatelliteModeForCarrierRoaming(phone)) {
                    return carrierEmergencyCallWaitForConnectionTimeoutMillis;
                }
                long j2 = carrierEmergencyCallWaitForConnectionTimeoutMillis;
                if (j < j2) {
                    j = j2;
                }
            }
        }
        return j != 0 ? j : DEFAULT_CARRIER_EMERGENCY_CALL_WAIT_FOR_CONNECTION_TIMEOUT_MILLIS;
    }

    public int getCarrierEmergencyCallWaitForConnectionTimeoutMillis(int i) {
        return getPersistableBundle(i).getInt("emergency_call_to_satellite_t911_handover_timeout_millis_int");
    }

    protected long getElapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    public void registerIccRefresh(Handler handler, int i) {
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.mCi.registerForIccRefresh(handler, i, null);
        }
    }

    public void unRegisterIccRefresh(Handler handler) {
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.mCi.unregisterForIccRefresh(handler);
        }
    }

    public void onSatelliteEntitlementStatusUpdated(int i, boolean z, List<String> list, List<String> list2, Map<String, Integer> map, Map<String, List<Integer>> map2, Map<String, Integer> map3, Map<String, Integer> map4, IIntegerConsumer iIntegerConsumer) {
        plogd("onSatelliteEntitlementStatusUpdated: subId=" + i + ", entitlementEnabled=" + z);
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.argi1 = i;
        someArgsObtain.argi2 = z ? 1 : 0;
        someArgsObtain.arg1 = list;
        someArgsObtain.arg2 = list2;
        someArgsObtain.arg3 = map;
        someArgsObtain.arg4 = map2;
        someArgsObtain.arg5 = map3;
        someArgsObtain.arg6 = map4;
        someArgsObtain.arg7 = iIntegerConsumer;
        sendMessage(obtainMessage(93, someArgsObtain));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void handleSatelliteEntitlementStatusUpdated(int i, boolean z, List list, List list2, Map map, Map map2, Map map3, Map map4, IIntegerConsumer iIntegerConsumer) {
        IIntegerConsumer iIntegerConsumer2 = iIntegerConsumer == null ? new IIntegerConsumer.Stub(this) { // from class: com.android.internal.telephony.satellite.SatelliteController.13
            public void accept(int i2) {
                SatelliteController.logd("updateSatelliteEntitlementStatus:" + i2);
            }
        } : iIntegerConsumer;
        if (list == null) {
            list = new ArrayList();
        }
        List list3 = list;
        if (list2 == null) {
            list2 = new ArrayList();
        }
        List list4 = list2;
        Map map5 = map == null ? new HashMap() : map;
        Map map6 = map2 == null ? new HashMap() : map2;
        Map map7 = map3 == null ? new HashMap() : map3;
        Map map8 = map4 == null ? new HashMap() : map4;
        logd("handleSatelliteEntitlementStatusUpdated: subId=" + i + ", entitlementEnabled=" + z + ", allowedPlmnList=[" + String.join(",", list3) + "], barredPlmnList=[" + String.join(",", list4) + "], plmnDataPlanMap =" + map5.toString() + ", plmnServiceTypeMap =" + map6.toString() + ", plmnDataServicePolicyMap=" + map7.toString() + ", plmnVoiceServicePolicyMap=" + map8.toString());
        if (((Boolean) this.mSatelliteEntitlementStatusPerCarrier.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.FALSE;
            }
        })).booleanValue() != z) {
            logd("update the carrier satellite enabled to " + z);
            handleIndividualEntitlementMetricReport(i, z);
            try {
                this.mSubscriptionManagerService.setSubscriptionProperty(i, "satellite_entitlement_status", z ? "1" : "0");
            } catch (IllegalArgumentException | SecurityException e) {
                loge("handleSatelliteEntitlementStatusUpdated: setSubscriptionProperty, e=" + e);
            }
        }
        if (isValidPlmnList(list3) && isValidPlmnList(list4)) {
            this.mMergedPlmnListPerCarrier.remove(Integer.valueOf(i));
            this.mEntitlementPlmnListPerCarrier.put(Integer.valueOf(i), list3);
            this.mEntitlementBarredPlmnListPerCarrier.put(Integer.valueOf(i), list4);
            this.mEntitlementDataPlanMapPerCarrier.put(Integer.valueOf(i), map5);
            this.mEntitlementDataServicePolicyMapPerCarrier.put(Integer.valueOf(i), map7);
            this.mEntitlementVoiceServicePolicyMapPerCarrier.put(Integer.valueOf(i), map8);
            updateAndNotifyChangesInCarrierRoamingNtnAvailableServices(i, map6);
            updatePlmnListPerCarrier(i);
            configureSatellitePlmnForCarrier(i);
            evaluateEnablingSatelliteForCarrier(i, 0, null);
            this.mSubscriptionManagerService.setSatelliteEntitlementInfo(i, list3, list4, map5, map6, map7, map8);
        } else {
            loge("handleSatelliteEntitlementStatusUpdated: either invalid allowedPlmnList or invalid barredPlmnList");
        }
        if (((Boolean) this.mSatelliteEntitlementStatusPerCarrier.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda16
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.FALSE;
            }
        })).booleanValue()) {
            removeAttachRestrictionForCarrier(i, 2, iIntegerConsumer2);
        } else {
            addAttachRestrictionForCarrier(i, 2, iIntegerConsumer2);
        }
    }

    private boolean isValidPlmnList(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (!TelephonyUtils.isValidPlmn(str)) {
                ploge("Invalid PLMN = " + str);
                return false;
            }
        }
        return true;
    }

    private Boolean isSatelliteSupportedViaOemInternal() {
        Boolean isSatelliteSupported = getIsSatelliteSupported();
        if (isSatelliteSupported != null) {
            return isSatelliteSupported;
        }
        requestIsSatelliteSupported(new ResultReceiver(this) { // from class: com.android.internal.telephony.satellite.SatelliteController.14
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                SatelliteController.this.decrementResultReceiverCount("SC:isSatelliteSupportedViaOemInternal");
                SatelliteController.this.plogd("isSatelliteSupportedViaOemInternal.requestIsSatelliteSupported: resultCode=" + i);
            }
        });
        incrementResultReceiverCount("SC:isSatelliteSupportedViaOemInternal");
        return null;
    }

    private void handleEventProvisionSatelliteServiceDone(ProvisionSatelliteServiceArgument provisionSatelliteServiceArgument, int i) {
        plogd("handleEventProvisionSatelliteServiceDone: result=" + i + ", subId=" + provisionSatelliteServiceArgument.subId);
        Consumer consumer = (Consumer) this.mSatelliteProvisionCallbacks.remove(Integer.valueOf(provisionSatelliteServiceArgument.subId));
        if (consumer == null) {
            ploge("handleEventProvisionSatelliteServiceDone: callback is null for subId=" + provisionSatelliteServiceArgument.subId);
            this.mProvisionMetricsStats.setResultCode(6).setIsProvisionRequest(true).setCarrierId(getSatelliteCarrierId()).setSupportedConnectionMode(getSupportedConnectTypeMetrics()).setIsNtnOnlyCarrier(isNtnOnlyCarrier()).reportProvisionMetrics();
            this.mControllerMetricsStats.reportProvisionCount(6);
            return;
        }
        if (i == 0 || i == 11) {
            persistOemEnabledSatelliteProvisionStatus(true);
            consumer.accept(0);
            updateCachedDeviceProvisionStatus();
        } else {
            consumer.accept(Integer.valueOf(i));
        }
        this.mProvisionMetricsStats.setResultCode(i).setIsProvisionRequest(true).setCarrierId(getSatelliteCarrierId()).setSupportedConnectionMode(getSupportedConnectTypeMetrics()).setIsNtnOnlyCarrier(isNtnOnlyCarrier()).reportProvisionMetrics();
        this.mControllerMetricsStats.reportProvisionCount(i);
    }

    private void handleEventDeprovisionSatelliteServiceDone(ProvisionSatelliteServiceArgument provisionSatelliteServiceArgument, int i) {
        if (provisionSatelliteServiceArgument == null) {
            ploge("handleEventDeprovisionSatelliteServiceDone: arg is null");
            return;
        }
        plogd("handleEventDeprovisionSatelliteServiceDone: result=" + i + ", subId=" + provisionSatelliteServiceArgument.subId);
        if (i == 0 || i == 11) {
            persistOemEnabledSatelliteProvisionStatus(false);
            Consumer<Integer> consumer = provisionSatelliteServiceArgument.callback;
            if (consumer != null) {
                consumer.accept(0);
            }
            updateCachedDeviceProvisionStatus();
        } else {
            Consumer<Integer> consumer2 = provisionSatelliteServiceArgument.callback;
            if (consumer2 != null) {
                consumer2.accept(Integer.valueOf(i));
            }
        }
        this.mProvisionMetricsStats.setResultCode(i).setIsProvisionRequest(false).setCarrierId(getSatelliteCarrierId()).setSupportedConnectionMode(getSupportedConnectTypeMetrics()).setIsNtnOnlyCarrier(isNtnOnlyCarrier()).reportProvisionMetrics();
        this.mControllerMetricsStats.reportDeprovisionCount(i);
    }

    private void handleStartSatelliteTransmissionUpdatesDone(AsyncResult asyncResult) {
        SatelliteTransmissionUpdateArgument satelliteTransmissionUpdateArgument = (SatelliteTransmissionUpdateArgument) ((SatelliteControllerHandlerRequest) asyncResult.userObj).argument;
        int satelliteError = SatelliteServiceUtils.getSatelliteError(asyncResult, "handleStartSatelliteTransmissionUpdatesDone");
        satelliteTransmissionUpdateArgument.errorCallback.accept(Integer.valueOf(satelliteError));
        PointingAppController pointingAppController = this.mPointingAppController;
        if (satelliteError != 0) {
            pointingAppController.setStartedSatelliteTransmissionUpdates(false);
            this.mPointingAppController.unregisterForSatelliteTransmissionUpdates(satelliteTransmissionUpdateArgument.subId, new Consumer<Integer>() { // from class: com.android.internal.telephony.satellite.SatelliteController.15
                @Override // java.util.function.Consumer
                public void accept(Integer num) {
                    SatelliteController.this.plogd("handleStartSatelliteTransmissionUpdatesDone: unregisterForSatelliteTransmissionUpdates result=" + num);
                }
            }, satelliteTransmissionUpdateArgument.callback);
            return;
        }
        pointingAppController.setStartedSatelliteTransmissionUpdates(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendRequestAsync(int i, Object obj, Phone phone) {
        obtainMessage(i, new SatelliteControllerHandlerRequest(obj, phone)).sendToTarget();
    }

    protected Boolean isDeviceProvisioned() {
        AtomicBoolean atomicBoolean = this.mOverriddenIsSatelliteViaOemProvisioned;
        if (atomicBoolean != null) {
            return Boolean.valueOf(atomicBoolean.get());
        }
        if (this.mIsDeviceProvisioned == null) {
            this.mIsDeviceProvisioned = new AtomicBoolean(getPersistedDeviceProvisionStatus());
        }
        return Boolean.valueOf(this.mIsDeviceProvisioned.get());
    }

    private void handleSatelliteEnabled(SatelliteControllerHandlerRequest satelliteControllerHandlerRequest) {
        RequestSatelliteEnabledArgument requestSatelliteEnabledArgument = (RequestSatelliteEnabledArgument) satelliteControllerHandlerRequest.argument;
        handlePersistentLoggingOnSessionStart(requestSatelliteEnabledArgument);
        selectBindingSatelliteSubscription(false);
        SatelliteModemEnableRequestAttributes satelliteModemEnableRequestAttributesCreateModemEnableRequest = createModemEnableRequest(requestSatelliteEnabledArgument);
        if (satelliteModemEnableRequestAttributesCreateModemEnableRequest == null) {
            plogw("handleSatelliteEnabled: enableRequestAttributes is null");
            sendErrorAndReportSessionMetrics(6, requestSatelliteEnabledArgument.callback);
            if (requestSatelliteEnabledArgument.enableSatellite) {
                setSatelliteEnabledRequest(null);
                return;
            } else {
                setSatelliteDisabledRequest(null);
                return;
            }
        }
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        if (satelliteSessionController != null) {
            satelliteSessionController.onSatelliteEnablementStarted(requestSatelliteEnabledArgument.enableSatellite);
        } else {
            ploge("handleSatelliteEnabled: mSatelliteSessionController is not initialized yet");
        }
        if (!requestSatelliteEnabledArgument.enableSatellite && this.mSatelliteModemInterface.isSatelliteServiceSupported()) {
            this.mWaitingForDisableSatelliteModemResponse.set(true);
            if (!isSatelliteDisabled()) {
                this.mWaitingForSatelliteModemOff.set(true);
            }
        }
        this.mSatelliteModemInterface.requestSatelliteEnabled(satelliteModemEnableRequestAttributesCreateModemEnableRequest, obtainMessage(12, satelliteControllerHandlerRequest));
        startWaitForSatelliteEnablingResponseTimer(requestSatelliteEnabledArgument);
        if (requestSatelliteEnabledArgument.enableSatellite) {
            this.mSessionStartTimeStamp.set(getElapsedRealtime());
        }
        this.mSessionProcessingTimeStamp.set(getElapsedRealtime());
    }

    private SatelliteModemEnableRequestAttributes createModemEnableRequest(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(selectedSatelliteSubId);
        if (subscriptionInfo == null) {
            loge("createModemEnableRequest: no SubscriptionInfo found for subId=" + selectedSatelliteSubId);
            return null;
        }
        return new SatelliteModemEnableRequestAttributes(requestSatelliteEnabledArgument.enableSatellite, requestSatelliteEnabledArgument.enableDemoMode, requestSatelliteEnabledArgument.isEmergency, new SatelliteSubscriptionInfo(subscriptionInfo.getIccId(), getNiddApnName(selectedSatelliteSubId)));
    }

    private String getNiddApnName(int i) {
        if (SatelliteServiceUtils.isNtnOnlySubscriptionId(i)) {
            String string = this.mContext.getResources().getString(R.string.dump_heap_notification);
            if (!TextUtils.isEmpty(string)) {
                return string;
            }
        }
        return getConfigForSubId(i).getString("satellite_nidd_apn_name_string", "");
    }

    private void handleRequestSatelliteAttachRestrictionForCarrierCmd(SatelliteControllerHandlerRequest satelliteControllerHandlerRequest) {
        RequestHandleSatelliteAttachRestrictionForCarrierArgument requestHandleSatelliteAttachRestrictionForCarrierArgument = (RequestHandleSatelliteAttachRestrictionForCarrierArgument) satelliteControllerHandlerRequest.argument;
        if (requestHandleSatelliteAttachRestrictionForCarrierArgument.reason == 0 && !persistSatelliteAttachEnabledForCarrierSetting(requestHandleSatelliteAttachRestrictionForCarrierArgument.subId)) {
            requestHandleSatelliteAttachRestrictionForCarrierArgument.callback.accept(6);
        } else {
            configureSatellitePlmnForCarrier(requestHandleSatelliteAttachRestrictionForCarrierArgument.subId);
            evaluateEnablingSatelliteForCarrier(requestHandleSatelliteAttachRestrictionForCarrierArgument.subId, requestHandleSatelliteAttachRestrictionForCarrierArgument.reason, requestHandleSatelliteAttachRestrictionForCarrierArgument.callback);
        }
    }

    private boolean updateSatelliteSupportedState(boolean z) {
        Boolean isSatelliteSupported = getIsSatelliteSupported();
        if (isSatelliteSupported != null && isSatelliteSupported.booleanValue() == z) {
            plogd("updateSatelliteSupportedState: current satellite support state and new supported state are matched, ignore update.");
            return false;
        }
        setIsSatelliteSupported(z);
        this.mSatelliteSessionController = SatelliteSessionController.make(this.mContext, getLooper(), this.mFeatureFlags, z);
        plogd("updateSatelliteSupportedState: create a new SatelliteSessionController because satellite supported state has changed to " + z);
        if (z) {
            registerForPendingDatagramCount();
            registerForSatelliteModemStateChanged();
            registerForNtnSignalStrengthChanged();
            registerForCapabilitiesChanged();
            registerForSatelliteRegistrationFailure();
            registerForTerrestrialNetworkAvailableChanged();
            requestIsSatelliteProvisioned(new ResultReceiver(this) { // from class: com.android.internal.telephony.satellite.SatelliteController.16
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i, Bundle bundle) {
                    SatelliteController.this.plogd("updateSatelliteSupportedState.requestIsSatelliteProvisioned: resultCode=" + i + ", resultData=" + bundle);
                    SatelliteController.this.decrementResultReceiverCount("SC:requestIsSatelliteProvisioned");
                    SatelliteController.this.requestSatelliteEnabled(false, false, false, new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteController.16.1
                        public void accept(int i2) {
                            SatelliteController.this.plogd("updateSatelliteSupportedState.requestSatelliteEnabled: result=" + i2);
                        }
                    });
                }
            });
            incrementResultReceiverCount("SC:requestIsSatelliteProvisioned");
            requestSatelliteCapabilities(new ResultReceiver(this) { // from class: com.android.internal.telephony.satellite.SatelliteController.17
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i, Bundle bundle) {
                    SatelliteController.this.plogd("updateSatelliteSupportedState.requestSatelliteCapabilities: resultCode=" + i + ", resultData=" + bundle);
                    SatelliteController.this.decrementResultReceiverCount("SC:requestSatelliteCapabilities");
                }
            });
            incrementResultReceiverCount("SC:requestSatelliteCapabilities");
        }
        registerForSatelliteSupportedStateChanged();
        selectBindingSatelliteSubscription(false);
        notifySatelliteSupportedStateChanged(z);
        return true;
    }

    private void updateSatelliteEnabledState(boolean z, String str) {
        setIsSatelliteEnabled(z);
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        if (satelliteSessionController != null) {
            satelliteSessionController.onSatelliteEnabledStateChanged(z);
            this.mSatelliteSessionController.setDemoMode(this.mIsDemoModeEnabled.get());
        } else {
            ploge(str + ": mSatelliteSessionController is not initialized yet");
        }
        if (!z) {
            this.mIsModemEnabledReportingNtnSignalStrength.set(false);
        }
        notifyEnabledStateChanged(z);
    }

    private void registerForPendingDatagramCount() {
        if (!this.mSatelliteModemInterface.isSatelliteServiceSupported() || this.mRegisteredForPendingDatagramCountWithSatelliteService.get()) {
            return;
        }
        this.mSatelliteModemInterface.registerForPendingDatagrams(this, 27, null);
        this.mRegisteredForPendingDatagramCountWithSatelliteService.set(true);
    }

    private void registerForSatelliteModemStateChanged() {
        if (!this.mSatelliteModemInterface.isSatelliteServiceSupported() || this.mRegisteredForSatelliteModemStateChangedWithSatelliteService.get()) {
            return;
        }
        this.mSatelliteModemInterface.registerForSatelliteModemStateChanged(this, 28, null);
        this.mRegisteredForSatelliteModemStateChangedWithSatelliteService.set(true);
    }

    private void registerForNtnSignalStrengthChanged() {
        if (!this.mSatelliteModemInterface.isSatelliteServiceSupported() || this.mRegisteredForNtnSignalStrengthChanged.get()) {
            return;
        }
        this.mSatelliteModemInterface.registerForNtnSignalStrengthChanged(this, 34, null);
        this.mRegisteredForNtnSignalStrengthChanged.set(true);
    }

    private void registerForCapabilitiesChanged() {
        if (!this.mSatelliteModemInterface.isSatelliteServiceSupported() || this.mRegisteredForSatelliteCapabilitiesChanged.get()) {
            return;
        }
        this.mSatelliteModemInterface.registerForSatelliteCapabilitiesChanged(this, 38, null);
        this.mRegisteredForSatelliteCapabilitiesChanged.set(true);
    }

    private void registerForSatelliteSupportedStateChanged() {
        if (!this.mSatelliteModemInterface.isSatelliteServiceSupported() || this.mRegisteredForSatelliteSupportedStateChanged.get()) {
            return;
        }
        this.mSatelliteModemInterface.registerForSatelliteSupportedStateChanged(this, 41, null);
        this.mRegisteredForSatelliteSupportedStateChanged.set(true);
    }

    private void registerForSatelliteRegistrationFailure() {
        if (this.mRegisteredForSatelliteRegistrationFailure.get()) {
            return;
        }
        this.mSatelliteModemInterface.registerForSatelliteRegistrationFailure(this, 54, null);
        this.mRegisteredForSatelliteRegistrationFailure.set(true);
    }

    private void registerForTerrestrialNetworkAvailableChanged() {
        if (this.mRegisteredForTerrestrialNetworkAvailableChanged.get()) {
            return;
        }
        this.mSatelliteModemInterface.registerForTerrestrialNetworkAvailableChanged(this, 55, null);
        this.mRegisteredForTerrestrialNetworkAvailableChanged.set(true);
    }

    private void notifyDeviceProvisionStateChanged(final boolean z) {
        final ArrayList arrayList = new ArrayList();
        this.mSatelliteProvisionStateChangedListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifyDeviceProvisionStateChanged$11(z, arrayList, (ISatelliteProvisionStateCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifyDeviceProvisionStateChanged$12((ISatelliteProvisionStateCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDeviceProvisionStateChanged$11(boolean z, List list, ISatelliteProvisionStateCallback iSatelliteProvisionStateCallback) {
        try {
            iSatelliteProvisionStateCallback.onSatelliteProvisionStateChanged(z);
        } catch (RemoteException e) {
            plogd("notifyDeviceProvisionStateChanged RemoteException: " + e);
            list.add(iSatelliteProvisionStateCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDeviceProvisionStateChanged$12(ISatelliteProvisionStateCallback iSatelliteProvisionStateCallback) {
        this.mSatelliteProvisionStateChangedListeners.remove(iSatelliteProvisionStateCallback.asBinder());
    }

    private boolean updateSatelliteSubscriptionProvisionState(List list, boolean z) {
        logd("updateSatelliteSubscriptionProvisionState: List=" + list + " , provisioned=" + z);
        Iterator it = list.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            SatelliteSubscriberInfo satelliteSubscriberInfo = (SatelliteSubscriberInfo) it.next();
            int subscriptionId = satelliteSubscriberInfo.getSubscriptionId();
            Boolean bool = this.mProvisionedSubscriberId.get(satelliteSubscriberInfo.getSubscriberId());
            if (bool == null) {
                bool = Boolean.FALSE;
            }
            Boolean boolValueOf = Boolean.FALSE;
            try {
                boolValueOf = Boolean.valueOf(this.mSubscriptionManagerService.isSatelliteProvisionedForNonIpDatagram(subscriptionId));
            } catch (IllegalArgumentException | SecurityException e) {
                ploge("isSatelliteProvisionedForNonIpDatagram: subId=" + subscriptionId + ", ex=" + e);
            }
            if (bool.booleanValue() != z || boolValueOf.booleanValue() != z) {
                this.mProvisionedSubscriberId.put(satelliteSubscriberInfo.getSubscriberId(), Boolean.valueOf(z));
                try {
                    this.mSubscriptionManagerService.setIsSatelliteProvisionedForNonIpDatagram(subscriptionId, z);
                    plogd("updateSatelliteSubscriptionProvisionState: set Provision state to db subId=" + subscriptionId);
                } catch (IllegalArgumentException | SecurityException e2) {
                    ploge("setIsSatelliteProvisionedForNonIpDatagram: subId=" + subscriptionId + ", ex=" + e2);
                }
                z2 = true;
            }
        }
        return z2;
    }

    private void handleEventSatelliteSubscriptionProvisionStateChanged() {
        List prioritizedSatelliteSubscriberProvisionStatusList = getPrioritizedSatelliteSubscriberProvisionStatusList();
        plogd("handleEventSatelliteSubscriptionProvisionStateChanged: " + prioritizedSatelliteSubscriberProvisionStatusList);
        notifySatelliteSubscriptionProvisionStateChanged(prioritizedSatelliteSubscriberProvisionStatusList);
        updateCachedDeviceProvisionStatus();
        handleEntireProvisionMetricReport();
        scheduleRegularMetricReportTimer();
        selectBindingSatelliteSubscription(false);
        handleCarrierRoamingNtnAvailableServicesChanged();
    }

    private void updateCachedDeviceProvisionStatus() {
        boolean persistedDeviceProvisionStatus = getPersistedDeviceProvisionStatus();
        plogd("updateCachedDeviceProvisionStatus: isProvisioned=" + persistedDeviceProvisionStatus);
        AtomicBoolean atomicBoolean = this.mIsDeviceProvisioned;
        if (atomicBoolean == null) {
            this.mIsDeviceProvisioned = new AtomicBoolean(persistedDeviceProvisionStatus);
            notifyDeviceProvisionStateChanged(persistedDeviceProvisionStatus);
        } else if (atomicBoolean.get() != persistedDeviceProvisionStatus) {
            this.mIsDeviceProvisioned.set(persistedDeviceProvisionStatus);
            notifyDeviceProvisionStateChanged(persistedDeviceProvisionStatus);
        }
    }

    private void notifySatelliteSubscriptionProvisionStateChanged(final List list) {
        final ArrayList arrayList = new ArrayList();
        this.mSatelliteProvisionStateChangedListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda31
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifySatelliteSubscriptionProvisionStateChanged$13(list, arrayList, (ISatelliteProvisionStateCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda32
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifySatelliteSubscriptionProvisionStateChanged$14((ISatelliteProvisionStateCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifySatelliteSubscriptionProvisionStateChanged$13(List list, List list2, ISatelliteProvisionStateCallback iSatelliteProvisionStateCallback) {
        try {
            iSatelliteProvisionStateCallback.onSatelliteSubscriptionProvisionStateChanged(list);
        } catch (RemoteException e) {
            plogd("notifySatelliteSubscriptionProvisionStateChanged: " + e);
            list2.add(iSatelliteProvisionStateCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifySatelliteSubscriptionProvisionStateChanged$14(ISatelliteProvisionStateCallback iSatelliteProvisionStateCallback) {
        this.mSatelliteProvisionStateChangedListeners.remove(iSatelliteProvisionStateCallback.asBinder());
    }

    private void handleEventSatelliteModemStateChanged(int i) {
        plogd("handleEventSatelliteModemStateChanged: state=" + i);
        this.mSatelliteModemState.set(i);
        if (i == 5 || i == 4) {
            if (!isWaitingForDisableSatelliteModemResponse()) {
                moveSatelliteToOffStateAndCleanUpResources(0);
            } else {
                notifyModemStateChangedToSessionController(4);
            }
            this.mWaitingForSatelliteModemOff.set(false);
            return;
        }
        if (isSatelliteEnabledOrBeingEnabled() || isSatelliteBeingDisabled()) {
            notifyModemStateChangedToSessionController(i);
            return;
        }
        plogw("Satellite modem is in a bad state. Disabling satellite modem now ...");
        RequestSatelliteEnabledArgument requestSatelliteEnabledArgument = new RequestSatelliteEnabledArgument(false, false, false, new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$handleEventSatelliteModemStateChanged$15((Integer) obj);
            }
        });
        setSatelliteDisabledRequest(requestSatelliteEnabledArgument);
        sendRequestAsync(11, requestSatelliteEnabledArgument, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEventSatelliteModemStateChanged$15(Integer num) {
        plogd("handleEventSatelliteModemStateChanged: disabling satellite result=" + num);
    }

    private void notifyModemStateChangedToSessionController(int i) {
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        if (satelliteSessionController != null) {
            satelliteSessionController.onSatelliteModemStateChanged(i);
        } else {
            ploge("notifyModemStateChangedToSessionController: mSatelliteSessionController is null");
        }
    }

    private void handleEventNtnSignalStrengthChanged(final NtnSignalStrength ntnSignalStrength) {
        logd("handleEventNtnSignalStrengthChanged: ntnSignalStrength=" + ntnSignalStrength);
        setNtnSignalStrength(ntnSignalStrength);
        this.mSessionMetricsStats.updateMaxNtnSignalStrengthLevel(ntnSignalStrength.getLevel());
        final ArrayList arrayList = new ArrayList();
        this.mNtnSignalStrengthChangedListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda41
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$handleEventNtnSignalStrengthChanged$16(ntnSignalStrength, arrayList, (INtnSignalStrengthCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda42
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$handleEventNtnSignalStrengthChanged$17((INtnSignalStrengthCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEventNtnSignalStrengthChanged$16(NtnSignalStrength ntnSignalStrength, List list, INtnSignalStrengthCallback iNtnSignalStrengthCallback) {
        try {
            iNtnSignalStrengthCallback.onNtnSignalStrengthChanged(ntnSignalStrength);
        } catch (RemoteException e) {
            plogd("handleEventNtnSignalStrengthChanged RemoteException: " + e);
            list.add(iNtnSignalStrengthCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEventNtnSignalStrengthChanged$17(INtnSignalStrengthCallback iNtnSignalStrengthCallback) {
        this.mNtnSignalStrengthChangedListeners.remove(iNtnSignalStrengthCallback.asBinder());
    }

    private void handleEventSatelliteCapabilitiesChanged(SatelliteCapabilities satelliteCapabilities) {
        plogd("handleEventSatelliteCapabilitiesChanged()");
        setSatelliteCapabilities(satelliteCapabilities);
        overrideSatelliteCapabilitiesIfApplicable();
        final SatelliteCapabilities satelliteCapabilities2 = getSatelliteCapabilities();
        final ArrayList arrayList = new ArrayList();
        this.mSatelliteCapabilitiesChangedListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda44
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$handleEventSatelliteCapabilitiesChanged$18(satelliteCapabilities2, arrayList, (ISatelliteCapabilitiesCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda45
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$handleEventSatelliteCapabilitiesChanged$19((ISatelliteCapabilitiesCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEventSatelliteCapabilitiesChanged$18(SatelliteCapabilities satelliteCapabilities, List list, ISatelliteCapabilitiesCallback iSatelliteCapabilitiesCallback) {
        try {
            iSatelliteCapabilitiesCallback.onSatelliteCapabilitiesChanged(satelliteCapabilities);
        } catch (RemoteException e) {
            plogd("handleEventSatelliteCapabilitiesChanged RemoteException: " + e);
            list.add(iSatelliteCapabilitiesCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEventSatelliteCapabilitiesChanged$19(ISatelliteCapabilitiesCallback iSatelliteCapabilitiesCallback) {
        this.mSatelliteCapabilitiesChangedListeners.remove(iSatelliteCapabilitiesCallback.asBinder());
    }

    private void handleEventSatelliteSupportedStateChanged(boolean z) {
        plogd("handleSatelliteSupportedStateChangedEvent: supported=" + z);
        if (!updateSatelliteSupportedState(z)) {
            plogd("handleSatelliteSupportedStateChangedEvent: supported state does not change");
            return;
        }
        Boolean isSatelliteEnabled = getIsSatelliteEnabled();
        if (z || isSatelliteEnabled == null || !isSatelliteEnabled.booleanValue()) {
            return;
        }
        plogd("Invoke requestSatelliteEnabled(), supported=false, mIsSatelliteEnabled=true");
        requestSatelliteEnabled(false, false, false, new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteController.18
            public void accept(int i) {
                SatelliteController.this.plogd("handleSatelliteSupportedStateChangedEvent: request satellite disable, result=" + i);
            }
        });
    }

    private void handleEventSelectedNbIotSatelliteSubscriptionChanged(final int i) {
        plogd("handleEventSelectedNbIotSatelliteSubscriptionChanged: " + i);
        final ArrayList arrayList = new ArrayList();
        this.mSelectedNbIotSatelliteSubscriptionChangedListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SatelliteController.$r8$lambda$z8ry7aCvVPSccndJwPw_rgTa8Lw(i, arrayList, (ISelectedNbIotSatelliteSubscriptionCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$handleEventSelectedNbIotSatelliteSubscriptionChanged$21((ISelectedNbIotSatelliteSubscriptionCallback) obj);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$z8ry7aCvVPSccndJwPw_rgTa8Lw(int i, List list, ISelectedNbIotSatelliteSubscriptionCallback iSelectedNbIotSatelliteSubscriptionCallback) {
        try {
            iSelectedNbIotSatelliteSubscriptionCallback.onSelectedNbIotSatelliteSubscriptionChanged(i);
        } catch (RemoteException e) {
            logd("handleEventSelectedNbIotSatelliteSubscriptionChanged RemoteException: " + e);
            list.add(iSelectedNbIotSatelliteSubscriptionCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEventSelectedNbIotSatelliteSubscriptionChanged$21(ISelectedNbIotSatelliteSubscriptionCallback iSelectedNbIotSatelliteSubscriptionCallback) {
        this.mSelectedNbIotSatelliteSubscriptionChangedListeners.remove(iSelectedNbIotSatelliteSubscriptionCallback.asBinder());
    }

    private void notifySatelliteSupportedStateChanged(final boolean z) {
        final ArrayList arrayList = new ArrayList();
        this.mSatelliteSupportedStateChangedListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda26
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifySatelliteSupportedStateChanged$22(z, arrayList, (IBooleanConsumer) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda27
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifySatelliteSupportedStateChanged$23((IBooleanConsumer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifySatelliteSupportedStateChanged$22(boolean z, List list, IBooleanConsumer iBooleanConsumer) {
        try {
            iBooleanConsumer.accept(z);
        } catch (RemoteException e) {
            plogd("handleSatelliteSupportedStateChangedEvent RemoteException: " + e);
            list.add(iBooleanConsumer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifySatelliteSupportedStateChanged$23(IBooleanConsumer iBooleanConsumer) {
        this.mSatelliteSupportedStateChangedListeners.remove(iBooleanConsumer.asBinder());
    }

    protected void setSettingsKeyForSatelliteMode(int i) {
        plogd("setSettingsKeyForSatelliteMode val: " + i);
        Settings.Global.putInt(this.mContext.getContentResolver(), "satellite_mode_enabled", i);
    }

    protected void setSettingsKeyToAllowDeviceRotation(int i) {
        if (!isFoldable(this.mContext, this.mDeviceStates)) {
            logd("setSettingsKeyToAllowDeviceRotation(" + i + "), device was not a foldable");
            return;
        }
        if (i == 0) {
            String str = this.mDeviceRotationLockToBackupAndRestore;
            if (str == null) {
                return;
            }
            Settings.Secure.putString(this.mContentResolver, "device_state_rotation_lock", str);
            logd("setSettingsKeyToAllowDeviceRotation(FALSE), RotationSettings is restored to" + this.mDeviceRotationLockToBackupAndRestore);
            this.mDeviceRotationLockToBackupAndRestore = "";
            return;
        }
        if (i == 1) {
            String string = Settings.Secure.getString(this.mContentResolver, "device_state_rotation_lock");
            this.mDeviceRotationLockToBackupAndRestore = string;
            String strReplaceDeviceRotationValue = replaceDeviceRotationValue(string != null ? string : "", 2, 2);
            Settings.Secure.putString(this.mContentResolver, "device_state_rotation_lock", strReplaceDeviceRotationValue);
            logd("setSettingsKeyToAllowDeviceRotation(TRUE), RotationSettings is changed from " + this.mDeviceRotationLockToBackupAndRestore + " to " + strReplaceDeviceRotationValue);
            return;
        }
        loge("setSettingsKeyToAllowDeviceRotation(" + i + "), never reach here.");
    }

    public static /* synthetic */ boolean $r8$lambda$vCmr8N5eo2Znt2qscMru99BUw1g(DeviceState deviceState) {
        return deviceState.hasProperty(12) || deviceState.hasProperty(11);
    }

    public boolean isFoldable(Context context, List<DeviceState> list) {
        return list.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SatelliteController.$r8$lambda$vCmr8N5eo2Znt2qscMru99BUw1g((DeviceState) obj);
            }
        });
    }

    private static String replaceDeviceRotationValue(String str, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        String[] strArrSplit = str.split(":");
        if (strArrSplit.length % 2 != 0) {
            loge("The length of key-value pair do not match. Return without modification.");
            return str;
        }
        for (int i3 = 0; i3 < strArrSplit.length; i3 += 2) {
            try {
                int i4 = Integer.parseInt(strArrSplit[i3]);
                int i5 = Integer.parseInt(strArrSplit[i3 + 1]);
                Integer numValueOf = Integer.valueOf(i4);
                if (i4 == i) {
                    i5 = i2;
                }
                arrayList.add(new Pair(numValueOf, Integer.valueOf(i5)));
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                loge("got error while parsing key-value. Return without modification. e:" + e);
                return str;
            }
        }
        return (String) arrayList.stream().map(new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda34
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SatelliteController.$r8$lambda$6SWKCBq_USiKYK6uSJB3l2eXObA((Pair) obj);
            }
        }).collect(Collectors.joining(":"));
    }

    public static /* synthetic */ String $r8$lambda$6SWKCBq_USiKYK6uSJB3l2eXObA(Pair pair) {
        return pair.first + ":" + pair.second;
    }

    protected boolean areAllRadiosDisabled() {
        if ((this.mDisableBTOnSatelliteEnabled.get() && this.mBTStateEnabled.get()) || ((this.mDisableNFCOnSatelliteEnabled.get() && this.mNfcStateEnabled.get()) || ((this.mDisableWifiOnSatelliteEnabled.get() && this.mWifiStateEnabled.get()) || (this.mDisableUWBOnSatelliteEnabled.get() && this.mUwbStateEnabled.get())))) {
            plogd("All radios are not disabled yet.");
            return false;
        }
        plogd("All radios are disabled.");
        return true;
    }

    private void evaluateToSendSatelliteEnabledSuccess() {
        plogd("evaluateToSendSatelliteEnabledSuccess");
        RequestSatelliteEnabledArgument satelliteEnabledRequest = getSatelliteEnabledRequest();
        if (areAllRadiosDisabled() && satelliteEnabledRequest != null && this.mWaitingForRadioDisabled.get()) {
            plogd("Sending success to callback that sent enable satellite request");
            setIsSatelliteEnabled(satelliteEnabledRequest.enableSatellite);
            setEmergencyMode(satelliteEnabledRequest.isEmergency);
            updateSatelliteEnabledState(satelliteEnabledRequest.enableSatellite, "EVENT_SET_SATELLITE_ENABLED_DONE");
            satelliteEnabledRequest.callback.accept(0);
            if (satelliteEnabledRequest.enableSatellite && !satelliteEnabledRequest.isEmergency) {
                plogd("Starting pointingUI needFullscreenPointingUI=truemIsDemoModeEnabled=" + this.mIsDemoModeEnabled.get() + ", isEmergency=" + satelliteEnabledRequest.isEmergency);
                this.mPointingAppController.startPointingUI(true, this.mIsDemoModeEnabled.get(), false);
            }
            setSatelliteEnabledRequest(null);
            this.mWaitingForRadioDisabled.set(false);
            if (getSatelliteEnableAttributesUpdateRequest() != null) {
                sendRequestAsync(50, getSatelliteEnableAttributesUpdateRequest(), null);
            }
            updateLastNotifiedNtnModeAndNotify(getSatellitePhone());
            evaluateDisablingP2pSession();
        }
    }

    private void resetSatelliteEnabledRequest() {
        plogd("resetSatelliteEnabledRequest");
        setSatelliteEnabledRequest(null);
        this.mWaitingForRadioDisabled.set(false);
    }

    private void resetSatelliteDisabledRequest() {
        plogd("resetSatelliteDisabledRequest");
        setSatelliteDisabledRequest(null);
        this.mWaitingForDisableSatelliteModemResponse.set(false);
        this.mWaitingForSatelliteModemOff.set(false);
    }

    public void moveSatelliteToOffStateAndCleanUpResources(int i) {
        plogd("moveSatelliteToOffStateAndCleanUpResources");
        setDemoModeEnabled(false);
        handlePersistentLoggingOnSessionEnd(this.mIsEmergency.get());
        setEmergencyMode(false);
        setIsSatelliteEnabled(false);
        setSettingsKeyForSatelliteMode(0);
        setSettingsKeyToAllowDeviceRotation(0);
        abortSatelliteDisableRequest(i);
        abortSatelliteEnableRequest(i);
        abortSatelliteEnableAttributesUpdateRequest(i);
        resetSatelliteEnabledRequest();
        resetSatelliteDisabledRequest();
        updateSatelliteEnabledState(false, "moveSatelliteToOffStateAndCleanUpResources");
        selectBindingSatelliteSubscription(false);
        updateLastNotifiedNtnModeAndNotify(getSatellitePhone());
        sendMessage(obtainMessage(43));
        sendMessage(obtainMessage(61));
    }

    private void setDemoModeEnabled(boolean z) {
        this.mIsDemoModeEnabled.set(z);
        this.mDatagramController.setDemoMode(z);
        plogd("setDemoModeEnabled: mIsDemoModeEnabled=" + z);
    }

    private void setEmergencyMode(boolean z) {
        plogd("setEmergencyMode: mIsEmergency=" + this.mIsEmergency.get() + ", isEmergency=" + z);
        if (this.mIsEmergency.get() != z) {
            this.mIsEmergency.set(z);
            SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
            if (satelliteSessionController != null) {
                satelliteSessionController.onEmergencyModeChanged(z);
            } else {
                plogw("setEmergencyMode: mSatelliteSessionController is null");
            }
        }
    }

    private boolean isMockModemAllowed() {
        return DEBUG || SystemProperties.getBoolean("persist.radio.allow_mock_modem", true);
    }

    private void configureSatellitePlmnForCarrier(int i) {
        logd("configureSatellitePlmnForCarrier");
        Phone phone = SatelliteServiceUtils.getPhone(i);
        if (phone == null) {
            ploge("configureSatellitePlmnForCarrier: phone is null for subId=" + i);
            return;
        }
        ArrayList arrayList = new ArrayList(getAllPlmnSet());
        List<String> allowedCarrierPlmnListForModem = getAllowedCarrierPlmnListForModem(i);
        phone.setSatellitePlmn(phone.getPhoneId(), allowedCarrierPlmnListForModem, arrayList, obtainMessage(29));
        final HashSet hashSet = new HashSet(allowedCarrierPlmnListForModem);
        SatellitePlmnNetworkInfo satellitePlmnNetworkInfoFromPlmn = SatellitePlmnNetworkInfo.fromPlmn(i, allowedCarrierPlmnListForModem, (List) arrayList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda28
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SatelliteController.$r8$lambda$qBehDF4o1uF4ub3i0InNTbYjrXA(hashSet, (String) obj);
            }
        }).collect(Collectors.toList()));
        plogd("configureSatellitePlmnForCarrier: satellitePlmnNetworkInfo=" + satellitePlmnNetworkInfoFromPlmn);
        phone.setSatelliteNetworkInfo(phone.getPhoneId(), satellitePlmnNetworkInfoFromPlmn.toHalSatelliteNetworkInfo(), obtainMessage(96));
    }

    public static /* synthetic */ boolean $r8$lambda$qBehDF4o1uF4ub3i0InNTbYjrXA(Set set, String str) {
        return !set.contains(str);
    }

    private List getDeviceSatellitePlmnListFromConfigUpdater() {
        SatelliteConfig satelliteConfig = getSatelliteConfig();
        if (satelliteConfig != null) {
            plogd("getDeviceSatellitePlmnListFromConfigUpdater: return = " + String.join(",", satelliteConfig.getDeviceSatelliteProviderList()));
            return satelliteConfig.getDeviceSatelliteProviderList();
        }
        plogd("getDeviceSatellitePlmnListFromConfigUpdater: return empty list");
        return new ArrayList();
    }

    public Set<String> getAllPlmnSet() {
        HashSet hashSet = new HashSet();
        for (int i : this.mSubscriptionManagerService.getActiveSubIdList(true)) {
            hashSet.addAll(getCarrierPlmnList(i));
            hashSet.addAll(getBarredPlmnList(i));
        }
        hashSet.addAll(this.mSatellitePlmnListFromOverlayConfig);
        hashSet.addAll(getDeviceSatellitePlmnListFromConfigUpdater());
        if (this.mIgnorePlmnListFromStorage.get()) {
            plogd("getAllPlmnList: allPlmnSetFromSubInfo=" + hashSet);
            return hashSet;
        }
        Set carrierRoamingNtnAllSatellitePlmnSetFromStorage = getCarrierRoamingNtnAllSatellitePlmnSetFromStorage();
        if (!carrierRoamingNtnAllSatellitePlmnSetFromStorage.containsAll(hashSet)) {
            carrierRoamingNtnAllSatellitePlmnSetFromStorage.addAll(hashSet);
            persistCarrierRoamingNtnAllSatellitePlmnSet(carrierRoamingNtnAllSatellitePlmnSetFromStorage);
        }
        plogd("getAllPlmnList: " + carrierRoamingNtnAllSatellitePlmnSetFromStorage);
        return carrierRoamingNtnAllSatellitePlmnSetFromStorage;
    }

    public List<String> getCarrierPlmnList(int i) {
        return ((List) this.mMergedPlmnListPerCarrier.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SatelliteController.$r8$lambda$JZ9xdOPWHi7U3K5SWQhapzVO4fQ((Integer) obj);
            }
        })).stream().toList();
    }

    public static /* synthetic */ List $r8$lambda$JZ9xdOPWHi7U3K5SWQhapzVO4fQ(Integer num) {
        return new ArrayList();
    }

    private List getBarredPlmnList(int i) {
        return ((List) this.mEntitlementBarredPlmnListPerCarrier.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda35
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SatelliteController.$r8$lambda$PoV3WZ_kLhBVU8Lk3knjH23_GLI((Integer) obj);
            }
        })).stream().toList();
    }

    public static /* synthetic */ List $r8$lambda$PoV3WZ_kLhBVU8Lk3knjH23_GLI(Integer num) {
        return new ArrayList();
    }

    private void persistCarrierRoamingNtnAllSatellitePlmnSet(Set set) {
        plogd("persistCarrierRoamingNtnAllSatellitePlmnSet");
        if (loadSatelliteSharedPreferences()) {
            SharedPreferences sharedPreferences = this.mSharedPreferences;
            if (sharedPreferences == null) {
                ploge("persistCarrierRoamingNtnAllSatellitePlmnSet: mSharedPreferences is null");
                return;
            }
            try {
                sharedPreferences.edit().putStringSet(CARRIER_ROAMING_NTN_ALL_SATELLITE_PLMN_SET_KEY, set).apply();
            } catch (Exception e) {
                plogd("persistCarrierRoamingNtnAllSatellitePlmnSet: ex=" + e);
            }
            setCarrierRoamingNtnAllSatellitePlmnSet(set);
        }
    }

    private void setCarrierRoamingNtnAllSatellitePlmnSet(Set set) {
        synchronized (this.mCarrierRoamingNtnAllSatellitePlmnSetLock) {
            this.mCarrierRoamingNtnAllSatellitePlmnSet = set;
        }
    }

    private Set getCarrierRoamingNtnAllSatellitePlmnSetFromStorage() {
        synchronized (this.mCarrierRoamingNtnAllSatellitePlmnSetLock) {
            try {
                if (this.mCarrierRoamingNtnAllSatellitePlmnSet != null) {
                    plogd("getCarrierRoamingNtnAllSatellitePlmnSetFromStorage: " + this.mCarrierRoamingNtnAllSatellitePlmnSet);
                    return this.mCarrierRoamingNtnAllSatellitePlmnSet;
                }
                if (!loadSatelliteSharedPreferences()) {
                    return new HashSet();
                }
                if (this.mSharedPreferences == null) {
                    ploge("getCarrierRoamingNtnAllSatellitePlmnSetFromStorage: mSharedPreferences is null");
                    return new HashSet();
                }
                Set<String> hashSet = new HashSet<>();
                try {
                    hashSet = this.mSharedPreferences.getStringSet(CARRIER_ROAMING_NTN_ALL_SATELLITE_PLMN_SET_KEY, new HashSet());
                } catch (Exception e) {
                    plogd("getCarrierRoamingNtnAllSatellitePlmnSetFromStorage: ex=" + e);
                }
                setCarrierRoamingNtnAllSatellitePlmnSet(hashSet);
                plogd("getCarrierRoamingNtnAllSatellitePlmnSetFromStorage: " + hashSet);
                return hashSet;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void handleSetSatellitePlmnNetworkInfoDoneEvent(Message message) {
        plogd("handleSetSatellitePlmnNetworkInfoDoneEvent");
        SatelliteServiceUtils.getSatelliteError((AsyncResult) message.obj, "handleSetSatellitePlmnNetworkInfoCmd");
    }

    private void handleSetSatellitePlmnInfoDoneEvent(Message message) {
        plogd("handleSetSatellitePlmnInfoDoneEvent");
        SatelliteServiceUtils.getSatelliteError((AsyncResult) message.obj, "handleSetSatellitePlmnInfoCmd");
    }

    private void updateSupportedSatelliteServicesForActiveSubscriptions() {
        plogd("updateSupportedSatelliteServicesForActiveSubscriptions");
        this.mSatelliteServicesSupportedByCarriersFromConfig.clear();
        this.mMergedPlmnListPerCarrier.clear();
        this.mSupportedEmergencyPlmnsPerCarrierFromConfig.clear();
        this.mSupportedDisasterPlmnsPerCarrierFromConfig.clear();
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (activeSubIdList != null) {
            for (int i : activeSubIdList) {
                updateSupportedSatelliteServices(i);
                updateSupportedEmergencyAndDisasterPlmns(i);
                handleCarrierRoamingNtnAvailableServicesChanged(i);
            }
            return;
        }
        loge("updateSupportedSatelliteServicesForActiveSubscriptions: activeSubIds is null");
    }

    private void updatePlmnListPerCarrier(int i) {
        List arrayList;
        plogd("updatePlmnListPerCarrier: subId=" + i);
        if (getConfigForSubId(i).getBoolean("satellite_entitlement_supported_bool", false)) {
            List list = ((List) this.mEntitlementPlmnListPerCarrier.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda11
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return SatelliteController.$r8$lambda$WMnwCc6jN3CM2UJRJHHGQsdBUrs((Integer) obj);
                }
            })).stream().toList();
            plogd("updatePlmnListPerCarrier: entitlementPlmnList=" + String.join(",", list) + " size=" + list.size());
            if (!list.isEmpty()) {
                this.mMergedPlmnListPerCarrier.put(Integer.valueOf(i), list);
                plogd("mMergedPlmnListPerCarrier is updated by Entitlement");
                this.mCarrierRoamingSatelliteControllerStats.reportConfigDataSource(i, 1);
                int iMapDataPolicyForMetrics = mapDataPolicyForMetrics(getSatelliteDataSupportMode(i));
                plogd("supported satellite data mode reported: " + iMapDataPolicyForMetrics);
                this.mCarrierRoamingSatelliteControllerStats.reportServiceDataPolicy(i, iMapDataPolicyForMetrics);
                return;
            }
        }
        SatelliteConfig satelliteConfig = getSatelliteConfig();
        if (satelliteConfig != null) {
            List<String> allSatellitePlmnsForCarrier = satelliteConfig.getAllSatellitePlmnsForCarrier(((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(i).getSimCarrierId());
            if (!allSatellitePlmnsForCarrier.isEmpty()) {
                plogd("mMergedPlmnListPerCarrier is updated by ConfigUpdater : " + String.join(",", allSatellitePlmnsForCarrier));
                this.mMergedPlmnListPerCarrier.put(Integer.valueOf(i), allSatellitePlmnsForCarrier);
                this.mCarrierRoamingSatelliteControllerStats.reportConfigDataSource(i, 2);
                int iMapDataPolicyForMetrics2 = mapDataPolicyForMetrics(getSatelliteDataSupportMode(i));
                plogd("supported satellite data mode reported: " + iMapDataPolicyForMetrics2);
                this.mCarrierRoamingSatelliteControllerStats.reportServiceDataPolicy(i, iMapDataPolicyForMetrics2);
                return;
            }
        }
        if (this.mSatelliteServicesSupportedByCarriersFromConfig.containsKey(Integer.valueOf(i)) && this.mSatelliteServicesSupportedByCarriersFromConfig.get(Integer.valueOf(i)) != null) {
            arrayList = ((Map) this.mSatelliteServicesSupportedByCarriersFromConfig.get(Integer.valueOf(i))).keySet().stream().toList();
            plogd("mMergedPlmnListPerCarrier is updated by carrier config: " + String.join(",", arrayList));
            this.mCarrierRoamingSatelliteControllerStats.reportConfigDataSource(i, 3);
            int iMapDataPolicyForMetrics3 = mapDataPolicyForMetrics(getSatelliteDataSupportMode(i));
            plogd("supported satellite data mode reported: " + iMapDataPolicyForMetrics3);
            this.mCarrierRoamingSatelliteControllerStats.reportServiceDataPolicy(i, iMapDataPolicyForMetrics3);
        } else {
            arrayList = new ArrayList();
            plogd("Empty mMergedPlmnListPerCarrier");
        }
        this.mMergedPlmnListPerCarrier.put(Integer.valueOf(i), arrayList);
    }

    public static /* synthetic */ List $r8$lambda$WMnwCc6jN3CM2UJRJHHGQsdBUrs(Integer num) {
        return new ArrayList();
    }

    private void updateSupportedSatelliteServices(int i) {
        plogd("updateSupportedSatelliteServices with subId " + i);
        SatelliteConfig satelliteConfig = getSatelliteConfig();
        int simCarrierId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(i).getSimCarrierId();
        if (satelliteConfig != null) {
            Map<String, Set<Integer>> supportedSatelliteServices = satelliteConfig.getSupportedSatelliteServices(simCarrierId);
            if (!supportedSatelliteServices.isEmpty()) {
                this.mSatelliteServicesSupportedByCarriersFromConfig.put(Integer.valueOf(i), supportedSatelliteServices);
                plogd("updateSupportedSatelliteServices using ConfigUpdater, PLMN list = " + supportedSatelliteServices.keySet());
                updatePlmnListPerCarrier(i);
                return;
            }
            plogd("supportedServicesPerPlmn is empty");
        }
        Map supportedSatelliteServicesFromCarrierConfig = readSupportedSatelliteServicesFromCarrierConfig(i);
        this.mSatelliteServicesSupportedByCarriersFromConfig.put(Integer.valueOf(i), supportedSatelliteServicesFromCarrierConfig);
        updatePlmnListPerCarrier(i);
        plogd("updateSupportedSatelliteServices using carrier config, PLMN list = " + supportedSatelliteServicesFromCarrierConfig.keySet());
    }

    private void updateSupportedEmergencyAndDisasterPlmns(int i) {
        List supportedEmergencyPlmnsFromCarrierConfig = readSupportedEmergencyPlmnsFromCarrierConfig(i);
        List supportedDisasterPlmnsFromCarrierConfig = readSupportedDisasterPlmnsFromCarrierConfig(i);
        this.mSupportedEmergencyPlmnsPerCarrierFromConfig.put(Integer.valueOf(i), supportedEmergencyPlmnsFromCarrierConfig);
        this.mSupportedDisasterPlmnsPerCarrierFromConfig.put(Integer.valueOf(i), supportedDisasterPlmnsFromCarrierConfig);
        plogd("updateSupportedEmergencyAndDisasterPlmns: subId=" + i + ", emergencyPlmns=" + String.join(",", supportedEmergencyPlmnsFromCarrierConfig) + ", disasterPlmns=" + String.join(",", supportedDisasterPlmnsFromCarrierConfig));
    }

    private int getMaxAllowedDataModeDeviceConfigOverlay() {
        int integer;
        try {
            integer = this.mContext.getResources().getInteger(17695219);
        } catch (Resources.NotFoundException e) {
            ploge("getMaxAllowedDataModeDeviceConfigOverlay: max_allowed_data_mode resource not found. ex=" + e);
            integer = 1;
        }
        logd("getMaxAllowedDataModeDeviceConfigOverlay: " + integer);
        return integer;
    }

    public int getMaxAllowedDataMode() {
        if (this.mMaxAllowedDataModeForCtsTest.get() >= 0) {
            logd("getMaxAllowedDataMode: using the overridden value for CTS test=" + this.mMaxAllowedDataModeForCtsTest.get());
            return this.mMaxAllowedDataModeForCtsTest.get();
        }
        if (this.mUncapMaxAllowedDataMode.get()) {
            logd("getMaxAllowedDataMode: max allowed data mode is uncapped, so return 2");
            return 2;
        }
        int maxAllowedDataModeDeviceConfigOverlay = getMaxAllowedDataModeDeviceConfigOverlay();
        logd("getMaxAllowedDataMode: device config=" + maxAllowedDataModeDeviceConfigOverlay);
        SatelliteConfig satelliteConfig = getSatelliteConfig();
        if (satelliteConfig == null) {
            logd("getMaxAllowedDataMode: satelliteConfig is null: return " + maxAllowedDataModeDeviceConfigOverlay);
            return maxAllowedDataModeDeviceConfigOverlay;
        }
        if (satelliteConfig.getSatelliteMaxAllowedDataMode() == null) {
            logd("getMaxAllowedDataMode: getSatelliteMaxAllowedDataMode() is null: return " + maxAllowedDataModeDeviceConfigOverlay);
            return maxAllowedDataModeDeviceConfigOverlay;
        }
        int iIntValue = satelliteConfig.getSatelliteMaxAllowedDataMode().intValue();
        logd("getMaxAllowedDataMode: satelliteConfig.getSatelliteMaxAllowedDataMode()=" + iIntValue);
        return iIntValue;
    }

    private List readSatellitePlmnsFromOverlayConfig() {
        return Arrays.stream(readStringArrayFromOverlayConfig(R.array.device_state_notification_active_titles)).toList();
    }

    private Map readSupportedSatelliteServicesFromCarrierConfig(int i) {
        return SatelliteServiceUtils.parseSupportedSatelliteServices(getPersistableBundle(i).getPersistableBundle("carrier_supported_satellite_services_per_provider_bundle"));
    }

    private List readSupportedEmergencyPlmnsFromCarrierConfig(int i) {
        String[] stringArray = getConfigForSubId(i).getStringArray("satellite_supported_emergency_plmn_string_array");
        return stringArray != null ? List.of((Object[]) stringArray) : Collections.EMPTY_LIST;
    }

    private List readSupportedDisasterPlmnsFromCarrierConfig(int i) {
        String[] stringArray = getConfigForSubId(i).getStringArray("satellite_supported_disaster_plmn_string_array");
        return stringArray != null ? List.of((Object[]) stringArray) : Collections.EMPTY_LIST;
    }

    private Map readRegionalSatelliteEarfcnsFromCarrierConfig(int i) {
        return SatelliteServiceUtils.parseRegionalSatelliteEarfcns(getPersistableBundle(i).getPersistableBundle("regional_satellite_earfcn_bundle"));
    }

    private PersistableBundle getConfigForSubId(int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        if (carrierConfigManager != null) {
            try {
                configForSubId = carrierConfigManager.getConfigForSubId(i, "carrier_supported_satellite_services_per_provider_bundle", "satellite_attach_supported_bool", "satellite_display_name_string", "satellite_roaming_turn_off_session_for_emergency_call_bool", "satellite_connection_hysteresis_sec_int", "satellite_entitlement_supported_bool", "carrier_roaming_satellite_default_services_int_array", "emergency_messaging_supported_bool", "emergency_call_to_satellite_t911_handover_timeout_millis_int", "satellite_esos_supported_bool", "satellite_roaming_p2p_sms_supported_bool", "satellite_nidd_apn_name_string", "carrier_roaming_ntn_connect_type_int", "carrier_supported_satellite_notification_hysteresis_sec_int", "carrier_roaming_ntn_emergency_call_to_satellite_handover_type_int", "satellite_roaming_screen_off_inactivity_timeout_sec_int", "satellite_roaming_p2p_sms_inactivity_timeout_sec_int", "satellite_roaming_esos_inactivity_timeout_sec_int", "satellite_sos_max_datagram_size_bytes_int", "satellite_supported_msg_apps_string_array", "regional_satellite_earfcn_bundle", "satellite_data_support_mode_int", "satellite_connected_notification_throttle_millis_int", "satellite_configs_per_plmn_bundle", "satellite_supported_emergency_plmn_string_array", "satellite_supported_disaster_plmn_string_array", "carrier_roaming_satellite_emergency_messaging_provider_per_country_bundle", "carrier_roaming_satellite_emergency_messaging_redirection_destination_string", "imsserviceentitlement.entitlement_server_url_string");
            } catch (Exception e) {
                logw("getConfigForSubId: " + e);
                configForSubId = null;
            }
        } else {
            configForSubId = null;
        }
        return (configForSubId == null || configForSubId.isEmpty()) ? CarrierConfigManager.getDefaultConfig() : configForSubId;
    }

    private void handleCarrierConfigChanged(int i, int i2, int i3, int i4) {
        plogd("handleCarrierConfigChanged(): slotIndex(" + i + "), subId(" + i2 + "), carrierId(" + i3 + "), specificCarrierId(" + i4 + ")");
        if (i2 == -1) {
            ConcurrentHashMap concurrentHashMap = this.mInitialized;
            Integer numValueOf = Integer.valueOf(i);
            Boolean bool = Boolean.FALSE;
            concurrentHashMap.put(numValueOf, bool);
            this.mLastNotifiedNtnMode.put(Integer.valueOf(i), bool);
            return;
        }
        getSatelliteEnabledForCarrierAtModem(i2);
        updateCarrierConfig(i2);
        updateSatelliteESOSSupported(i2);
        updateSatelliteProvisionedStatePerSubscriberId();
        updateEntitlementPlmnListPerCarrier(i2);
        updateSupportedSatelliteServicesForActiveSubscriptions();
        processNewCarrierConfigData(i2);
        resetCarrierRoamingSatelliteModeParams(i2);
        evaluateCarrierRoamingNtnEligibilityChange();
        sendMessageDelayed(obtainMessage(43), this.mEvaluateEsosProfilesPrioritizationDurationMillis.get());
        updateRegionalSatelliteEarfcns(i2);
        updateAndReportEligibilitySource(i2);
    }

    private void getSatelliteEnabledForCarrierAtModem(int i) {
        Phone phone = SatelliteServiceUtils.getPhone(i);
        if (this.mIsSatelliteAttachEnabledForCarrierArrayPerSub.containsKey(Integer.valueOf(i))) {
            return;
        }
        sendRequestAsync(64, null, phone);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSubscriptionsChanged() {
        sendMessageDelayed(obtainMessage(43), this.mEvaluateEsosProfilesPrioritizationDurationMillis.get());
    }

    private void processNewCarrierConfigData(int i) {
        updateRestrictReasonForEntitlementPerCarrier(i);
        updateSatelliteTechPerPlmnForActiveSubscriptions();
        configureSatellitePlmnForCarrier(i);
        evaluateEnablingSatelliteForCarrier(i, 0, null);
    }

    protected void updateCarrierConfig(int i) {
        this.mCarrierConfigArray.put(Integer.valueOf(i), getConfigForSubId(i));
    }

    private void updateEntitlementPlmnListPerCarrier(int i) {
        if (!getConfigForSubId(i).getBoolean("satellite_entitlement_supported_bool", false)) {
            plogd("don't support entitlement");
            this.mEntitlementPlmnListPerCarrier.remove(Integer.valueOf(i));
            this.mEntitlementBarredPlmnListPerCarrier.remove(Integer.valueOf(i));
            this.mEntitlementDataPlanMapPerCarrier.remove(Integer.valueOf(i));
            this.mEntitlementServiceTypeMapPerCarrier.remove(Integer.valueOf(i));
            this.mEntitlementDataServicePolicyMapPerCarrier.remove(Integer.valueOf(i));
            this.mEntitlementVoiceServicePolicyMapPerCarrier.remove(Integer.valueOf(i));
            return;
        }
        if (!this.mEntitlementPlmnListPerCarrier.containsKey(Integer.valueOf(i))) {
            plogd("updateEntitlementPlmnListPerCarrier: no correspondent cache, load from persist storage");
            List<String> satelliteEntitlementPlmnList = this.mSubscriptionManagerService.getSatelliteEntitlementPlmnList(i);
            if (satelliteEntitlementPlmnList.isEmpty()) {
                plogd("updateEntitlementPlmnListPerCarrier: read empty list");
                return;
            }
            plogd("updateEntitlementPlmnListPerCarrier: entitlementPlmnList=" + String.join(",", satelliteEntitlementPlmnList));
            this.mEntitlementPlmnListPerCarrier.put(Integer.valueOf(i), satelliteEntitlementPlmnList);
        }
        if (!this.mEntitlementBarredPlmnListPerCarrier.containsKey(Integer.valueOf(i))) {
            plogd("updateEntitlementBarredPlmnList: no correspondent cache, load from persist storage");
            List<String> satelliteEntitlementBarredPlmnList = this.mSubscriptionManagerService.getSatelliteEntitlementBarredPlmnList(i);
            if (satelliteEntitlementBarredPlmnList.isEmpty()) {
                plogd("updateEntitlementBarredPlmnList: read empty list");
            }
            plogd("updateEntitlementBarredPlmnList: entitlementBarredPlmnList=" + String.join(",", satelliteEntitlementBarredPlmnList));
            this.mEntitlementBarredPlmnListPerCarrier.put(Integer.valueOf(i), satelliteEntitlementBarredPlmnList);
        }
        if (!this.mEntitlementDataPlanMapPerCarrier.containsKey(Integer.valueOf(i))) {
            plogd("updateEntitlementDataPlanForPlmns: no correspondent cache, load from persist storage");
            Map<String, Integer> satelliteEntitlementDataPlanForPlmns = this.mSubscriptionManagerService.getSatelliteEntitlementDataPlanForPlmns(i);
            if (satelliteEntitlementDataPlanForPlmns.isEmpty()) {
                plogd("updateEntitlementBarredPlmnList: read empty list");
            }
            plogd("updateEntitlementDataPlanForPlmns: entitlementDataPlanForPlmns=" + satelliteEntitlementDataPlanForPlmns);
            this.mEntitlementDataPlanMapPerCarrier.put(Integer.valueOf(i), satelliteEntitlementDataPlanForPlmns);
        }
        if (!this.mEntitlementServiceTypeMapPerCarrier.containsKey(Integer.valueOf(i))) {
            plogd("updateEntitlementTypeMapPerCarrier: no correspondent cache, load from persist storage");
            Map<String, List<Integer>> satelliteEntitlementPlmnServiceTypeMap = this.mSubscriptionManagerService.getSatelliteEntitlementPlmnServiceTypeMap(i);
            if (satelliteEntitlementPlmnServiceTypeMap.isEmpty()) {
                plogd("updateEntitlementTypeMapPerCarrier: read empty list");
            }
            plogd("updateEntitlementTypeMapPerCarrier: entitlementTypeMapPerCarrier=" + satelliteEntitlementPlmnServiceTypeMap);
            this.mEntitlementServiceTypeMapPerCarrier.put(Integer.valueOf(i), satelliteEntitlementPlmnServiceTypeMap);
        }
        if (!this.mEntitlementDataServicePolicyMapPerCarrier.containsKey(Integer.valueOf(i))) {
            plogd("updateEntitlementDataServicePolicy: no correspondent cache, load from persist storage");
            Map<String, Integer> satelliteEntitlementPlmnDataServicePolicy = this.mSubscriptionManagerService.getSatelliteEntitlementPlmnDataServicePolicy(i);
            if (satelliteEntitlementPlmnDataServicePolicy.isEmpty()) {
                plogd("updateEntitlementDataServicePolicy: read empty list");
            }
            plogd("updateEntitlementDataServicePolicy: entitlementDataServicePolicy=" + satelliteEntitlementPlmnDataServicePolicy);
            this.mEntitlementDataServicePolicyMapPerCarrier.put(Integer.valueOf(i), satelliteEntitlementPlmnDataServicePolicy);
        }
        if (this.mEntitlementVoiceServicePolicyMapPerCarrier.containsKey(Integer.valueOf(i))) {
            return;
        }
        plogd("updateEntitlementVoiceServicePolicy: no correspondent cache, load from persist storage");
        Map<String, Integer> satelliteEntitlementPlmnVoiceServicePolicy = this.mSubscriptionManagerService.getSatelliteEntitlementPlmnVoiceServicePolicy(i);
        if (satelliteEntitlementPlmnVoiceServicePolicy.isEmpty()) {
            plogd("updateEntitlementVoiceServicePolicy: read empty list");
        }
        plogd("updateEntitlementVoiceServicePolicy: entitlementVoiceServicePolicy=" + satelliteEntitlementPlmnVoiceServicePolicy);
        this.mEntitlementVoiceServicePolicyMapPerCarrier.put(Integer.valueOf(i), satelliteEntitlementPlmnVoiceServicePolicy);
    }

    private void updateSatelliteESOSSupported(int i) {
        boolean satelliteESOSSupported = this.mSubscriptionManagerService.getSatelliteESOSSupported(i);
        boolean z = getConfigForSubId(i).getBoolean("satellite_esos_supported_bool", false);
        if (satelliteESOSSupported != z) {
            this.mSubscriptionManagerService.setSatelliteESOSSupported(i, z);
            logd("updateSatelliteESOSSupported: " + z);
        }
    }

    protected void updateSatelliteProvisionedStatePerSubscriberId() {
        Iterator<SubscriptionInfo> it = this.mSubscriptionManagerService.getAllSubInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag()).iterator();
        while (it.hasNext()) {
            int subscriptionId = it.next().getSubscriptionId();
            String str = (String) getSubscriberIdAndType(this.mSubscriptionManagerService.getSubscriptionInfo(subscriptionId)).first;
            if (this.mProvisionedSubscriberId.get(str) == null && this.mSubscriptionManagerService.isSatelliteProvisionedForNonIpDatagram(subscriptionId)) {
                this.mProvisionedSubscriberId.put(str, Boolean.TRUE);
                logd("updateSatelliteProvisionStatePerSubscriberId: " + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, str) + " set true");
            }
        }
        updateCachedDeviceProvisionStatus();
    }

    private String[] readStringArrayFromOverlayConfig(int i) {
        String[] stringArray;
        try {
            stringArray = this.mContext.getResources().getStringArray(i);
        } catch (Resources.NotFoundException e) {
            ploge("readStringArrayFromOverlayConfig: id= " + i + ", ex=" + e);
            stringArray = null;
        }
        return stringArray == null ? new String[0] : stringArray;
    }

    private Boolean isSatelliteAttachSupportedViaConfigupdater(int i) {
        SatelliteConfig satelliteConfig = getSatelliteConfig();
        if (satelliteConfig == null) {
            logd("isSatelliteAttachSupportedViaConfigupdater: return null (satelliteConfig is null)");
            return null;
        }
        return satelliteConfig.isSatelliteAttachSupportedBySubId(i);
    }

    public boolean isSatelliteSupportedViaCarrier(int i) {
        Boolean boolIsSatelliteAttachSupportedViaConfigupdater = isSatelliteAttachSupportedViaConfigupdater(i);
        if (boolIsSatelliteAttachSupportedViaConfigupdater != null) {
            plogd("isSatelliteSupportedViaCarrier: using SatelliteConfig for subId=" + i + ", isAttachSupported=" + boolIsSatelliteAttachSupportedViaConfigupdater);
            return boolIsSatelliteAttachSupportedViaConfigupdater.booleanValue();
        }
        return getConfigForSubId(i).getBoolean("satellite_attach_supported_bool");
    }

    private Boolean isSatelliteEntitlementSupportedViaConfigupdater(int i) {
        SatelliteConfig satelliteConfig = getSatelliteConfig();
        if (satelliteConfig == null) {
            logd("isSatelliteEntitlementSupportedViaConfigupdater: return null (satelliteConfig is null)");
            return null;
        }
        return satelliteConfig.isSatelliteEntitlementSupportedBySubId(i);
    }

    public boolean isSatelliteEntitlementSupported(int i) {
        Boolean boolIsSatelliteEntitlementSupportedViaConfigupdater = isSatelliteEntitlementSupportedViaConfigupdater(i);
        if (boolIsSatelliteEntitlementSupportedViaConfigupdater != null) {
            logd("isSatelliteEntitlementSupported: using SatelliteConfig for subId=" + i + ", entitlementSupported=" + boolIsSatelliteEntitlementSupportedViaConfigupdater);
            return boolIsSatelliteEntitlementSupportedViaConfigupdater.booleanValue();
        }
        return getConfigForSubId(i).getBoolean("satellite_entitlement_supported_bool");
    }

    private String getEntitlementServerUrlFromSatelliteConfig(int i) {
        SatelliteConfig satelliteConfig = getInstance().getSatelliteConfig();
        if (satelliteConfig == null) {
            Log.d("SatelliteController", "getEntitlementServerUrlFromSatelliteConfig return null (SatelliteConfig is null)");
            return null;
        }
        return satelliteConfig.getSatelliteEntitlementServerUrlBySubId(i);
    }

    public String getSatelliteEntitlementServerUrl(int i) {
        String entitlementServerUrlFromSatelliteConfig = getEntitlementServerUrlFromSatelliteConfig(i);
        if (!TextUtils.isEmpty(entitlementServerUrlFromSatelliteConfig)) {
            Log.d("SatelliteController", "getSatelliteEntitlementServerUrl: using SatelliteConfig for subId=" + i + ", entitlementServerUrl=" + entitlementServerUrlFromSatelliteConfig);
            return entitlementServerUrlFromSatelliteConfig;
        }
        return getConfigForSubId(i).getString("imsserviceentitlement.entitlement_server_url_string", "");
    }

    private boolean isOnlyEmergencyServiceSupported(int i) {
        if (getCarrierRoamingNtnConnectType(i) == 1) {
            plogd("isEmergencyServiceSupported: connect type of subId: " + i + " is manual, returning false");
            return false;
        }
        if (!isSatelliteRestrictedForCarrier(i)) {
            plogd("isEmergencyServiceSupported: satellite is not restricted for carrier for subId: " + i + ", returning false");
            return false;
        }
        if (getEmergencyPlmnList(i).isEmpty()) {
            plogd("isEmergencyServiceSupported: plmnList is empty for subId: " + i + ", returning false");
            return false;
        }
        plogd("isEmergencyServiceSupported: return true, subId=" + i);
        return true;
    }

    private boolean isDisasterServiceSupported(int i) {
        if (getCarrierRoamingNtnConnectType(i) == 1) {
            plogd("isDisasterServiceSupported: connect type of subId: " + i + " is manual, returning false");
            return false;
        }
        if (getDisasterPlmnList(i).isEmpty()) {
            plogd("isDisasterServiceSupported: plmnList is empty for subId: " + i + ", returning false");
            return false;
        }
        plogd("isDisasterServiceSupported: return true, subId=" + i);
        return true;
    }

    private List getEmergencyPlmnList(int i) {
        return (List) this.mSupportedEmergencyPlmnsPerCarrierFromConfig.getOrDefault(Integer.valueOf(i), new ArrayList());
    }

    private List getDisasterPlmnList(int i) {
        return (List) this.mSupportedDisasterPlmnsPerCarrierFromConfig.getOrDefault(Integer.valueOf(i), new ArrayList());
    }

    private List getAllowedCarrierPlmnListForModem(int i) {
        ArrayList arrayList = new ArrayList();
        boolean zIsSatelliteRestrictedForCarrier = isSatelliteRestrictedForCarrier(i);
        plogd("getAllowedCarrierPlmnListForModem: subId=" + i + ", isSatelliteRestrictedForCarrier=" + zIsSatelliteRestrictedForCarrier);
        if (isOnlyEmergencyServiceSupported(i)) {
            arrayList.addAll(getEmergencyPlmnList(i));
            arrayList.addAll(getDisasterPlmnList(i));
        } else if (!zIsSatelliteRestrictedForCarrier) {
            arrayList.addAll(getCarrierPlmnList(i));
            arrayList.removeAll(getEmergencyPlmnList(i));
        }
        plogd("getAllowedCarrierPlmnListForModem: plmnList=" + String.join(",", arrayList));
        return arrayList;
    }

    public boolean isSatelliteRoamingP2pSmSSupported(int i) {
        return getConfigForSubId(i).getBoolean("satellite_roaming_p2p_sms_supported_bool");
    }

    public boolean isSatelliteEsosSupported(int i) {
        return getConfigForSubId(i).getBoolean("satellite_esos_supported_bool");
    }

    public boolean turnOffSatelliteSessionForEmergencyCall(int i) {
        return getConfigForSubId(i).getBoolean("satellite_roaming_turn_off_session_for_emergency_call_bool");
    }

    private Integer getCarrierRoamingNtnConnectTypeViaConfigUpdater(int i) {
        SatelliteConfig satelliteConfig = getSatelliteConfig();
        if (satelliteConfig == null) {
            logd("getCarrierRoamingNtnConnectTypeViaConfigUpdater: return null (satelliteConfig is null)");
            return null;
        }
        return satelliteConfig.getSatelliteNtnConnectTypeByCarrierId(SatelliteServiceUtils.getCarrierIdFromSubscription(i));
    }

    public int getCarrierRoamingNtnConnectType(int i) {
        plogd("getCarrierRoamingNtnConnectType: Checking connect type from PLMN config for subId: " + i);
        SatellitePerPlmnConfiguration satellitePerPlmnConfiguration = getSatellitePerPlmnConfiguration(i);
        plogd("getCarrierRoamingNtnConnectType: config: " + satellitePerPlmnConfiguration);
        if (satellitePerPlmnConfiguration != null && satellitePerPlmnConfiguration.connectType != -1) {
            plogd("getCarrierRoamingNtnConnectType: Connect type from config: " + satellitePerPlmnConfiguration.connectType);
            return satellitePerPlmnConfiguration.connectType;
        }
        Integer carrierRoamingNtnConnectTypeViaConfigUpdater = getCarrierRoamingNtnConnectTypeViaConfigUpdater(i);
        if (carrierRoamingNtnConnectTypeViaConfigUpdater != null) {
            plogd("getCarrierRoamingNtnConnectType: using SatelliteConfig for subId=" + i + ", ntnConnectType=" + carrierRoamingNtnConnectTypeViaConfigUpdater);
            return carrierRoamingNtnConnectTypeViaConfigUpdater.intValue();
        }
        int i2 = getConfigForSubId(i).getInt("carrier_roaming_ntn_connect_type_int");
        plogd("getCarrierRoamingNtnConnectType: Falling back to global carrier config connect type: " + i2);
        return i2;
    }

    public int getSupportedConnectTypeMetrics() {
        return getSupportedConnectTypeMetrics(this.mSelectedSatelliteSubId.get());
    }

    public int getSupportedConnectTypeMetrics(int i) {
        if (i == -1 || !isSatelliteSupportedViaCarrier(i)) {
            return 0;
        }
        Integer carrierRoamingNtnConnectTypeViaConfigUpdater = getCarrierRoamingNtnConnectTypeViaConfigUpdater(i);
        if (carrierRoamingNtnConnectTypeViaConfigUpdater != null) {
            plogd("getSupportedConnectTypeMetrics: using SatelliteConfig for subId=" + i + ", ntnConnectType=" + carrierRoamingNtnConnectTypeViaConfigUpdater);
            return SatelliteServiceUtils.fromSupportedConnectionMode(carrierRoamingNtnConnectTypeViaConfigUpdater.intValue());
        }
        return SatelliteServiceUtils.fromSupportedConnectionMode(getConfigForSubId(i).getInt("carrier_roaming_ntn_connect_type_int"));
    }

    public int getSessionConnectTypeMetrics() {
        return getSessionConnectTypeMetrics(this.mSelectedSatelliteSubId.get());
    }

    public int getSessionConnectTypeMetrics(int i) {
        if (i == -1 || !isSatelliteSupportedViaCarrier(i)) {
            return 0;
        }
        return SatelliteServiceUtils.fromSessionConnectionMode(getCarrierRoamingNtnConnectType(i));
    }

    protected int getCarrierRoamingNtnEmergencyCallToSatelliteHandoverType(int i) {
        plogd("getCarrierRoamingNtnEmergencyCallToSatelliteHandoverType: Checking handover type from PLMN config for subId: " + i);
        SatellitePerPlmnConfiguration satellitePerPlmnConfiguration = getSatellitePerPlmnConfiguration(i);
        plogd("getCarrierRoamingNtnEmergencyCallToSatelliteHandoverType: config: " + satellitePerPlmnConfiguration);
        if (satellitePerPlmnConfiguration != null && satellitePerPlmnConfiguration.handoverType != 0) {
            plogd("getCarrierRoamingNtnEmergencyCallToSatelliteHandoverType: Handover type from config: " + satellitePerPlmnConfiguration.handoverType);
            return satellitePerPlmnConfiguration.handoverType;
        }
        int i2 = getConfigForSubId(i).getInt("carrier_roaming_ntn_emergency_call_to_satellite_handover_type_int");
        plogd("getCarrierRoamingNtnEmergencyCallToSatelliteHandoverType: Falling back to global carrier config handover type: " + i2);
        return i2;
    }

    private Integer getCarrierSatelliteDataSupportedModeViaConfigUpdater(int i) {
        SatelliteConfig satelliteConfig = getSatelliteConfig();
        if (satelliteConfig == null) {
            logd("getCarrierSatelliteDataSupportedModeViaConfigUpdater: return null (satelliteConfig is null)");
            return null;
        }
        Integer satelliteDataSupportModeBySubId = satelliteConfig.getSatelliteDataSupportModeBySubId(i);
        plogd("getCarrierSatelliteDataSupportedModeFromConfig: using SatelliteConfig for subId=" + i + ", dataSupportMode=" + satelliteDataSupportModeBySubId);
        return satelliteDataSupportModeBySubId;
    }

    private int getCarrierSatelliteDataSupportedModeFromConfig(int i) {
        Integer carrierSatelliteDataSupportedModeViaConfigUpdater = getCarrierSatelliteDataSupportedModeViaConfigUpdater(i);
        if (carrierSatelliteDataSupportedModeViaConfigUpdater != null) {
            plogd("getCarrierSatelliteDataSupportedModeFromConfig: using SatelliteConfig for subId=" + i + ", dataSupportMode=" + carrierSatelliteDataSupportedModeViaConfigUpdater);
            return carrierSatelliteDataSupportedModeViaConfigUpdater.intValue();
        }
        int i2 = getConfigForSubId(i).getInt("satellite_data_support_mode_int");
        plogd("getCarrierSatelliteDataSupportedModeFromConfig: using CarrierConfig for subId=" + i + ", dataSupportMode=" + i2);
        return i2;
    }

    private long getNotificationDisplayThrottleTimeout(int i) {
        return getConfigForSubId(i).getLong("satellite_connected_notification_throttle_millis_int");
    }

    private boolean isSatelliteAttachEnabledForCarrierByUser(int i) {
        boolean zEquals;
        Set<Integer> set = this.mSatelliteAttachRestrictionForCarrierArray.get(Integer.valueOf(i));
        if (set != null) {
            boolean z = !set.contains(0);
            plogd("isSatelliteAttachEnabledForCarrierByUser: subId=" + i + " returning from cache, isEnabled=" + z);
            return z;
        }
        plogd("isSatelliteAttachEnabledForCarrierByUser: subId=" + i + " no cache found, loading from persistent storage");
        try {
            String subscriptionProperty = this.mSubscriptionManagerService.getSubscriptionProperty(i, "satellite_attach_enabled_for_carrier", this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
            plogd("isSatelliteAttachEnabledForCarrierByUser: subId=" + i + ", enabled=" + subscriptionProperty);
            if (subscriptionProperty == null) {
                plogd("isSatelliteAttachEnabledForCarrierByUser: db value is null for subId=" + i);
                zEquals = isSatelliteEnabledByDefaultForReason(2);
            } else if (subscriptionProperty.isEmpty()) {
                plogd("isSatelliteAttachEnabledForCarrierByUser: db value is empty for subId=" + i);
                zEquals = isSatelliteEnabledByDefaultForReason(2);
            } else if (subscriptionProperty.equals("-1")) {
                plogd("isSatelliteAttachEnabledForCarrierByUser: db value is unset for subId=" + i);
                zEquals = isSatelliteEnabledByDefaultForReason(2);
            } else {
                zEquals = subscriptionProperty.equals("1");
            }
            if (!zEquals) {
                this.mSatelliteAttachRestrictionForCarrierArray.put(Integer.valueOf(i), new HashSet());
                this.mSatelliteAttachRestrictionForCarrierArray.get(Integer.valueOf(i)).add(0);
                plogd("isSatelliteAttachEnabledForCarrierByUser: satellite restricted due to user reason for subId=" + i);
            }
            return zEquals;
        } catch (IllegalArgumentException | SecurityException e) {
            ploge("isSatelliteAttachEnabledForCarrierByUser: subId=" + i + " ex=" + e);
            return false;
        }
    }

    private boolean hasReasonToRestrictSatelliteCommunicationForCarrier(int i) {
        Set<Integer> orDefault = this.mSatelliteAttachRestrictionForCarrierArray.getOrDefault(Integer.valueOf(i), Collections.EMPTY_SET);
        boolean zIsEmpty = orDefault.isEmpty();
        boolean z = !zIsEmpty;
        if (!zIsEmpty) {
            plogd("hasReasonToRestrictSatelliteCommunicationForCarrier: subId=" + i + " is restricted. restriction=" + orDefault);
        }
        return z;
    }

    private void updateRestrictReasonForEntitlementPerCarrier(int i) {
        String subscriptionProperty;
        if (!isSatelliteEntitlementSupported(i)) {
            plogd("don't support entitlement");
            return;
        }
        IIntegerConsumer.Stub stub = new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteController.19
            public void accept(int i2) {
                SatelliteController.this.plogd("updateRestrictReasonForEntitlementPerCarrier:" + i2);
            }
        };
        if (!this.mSatelliteEntitlementStatusPerCarrier.containsKey(Integer.valueOf(i))) {
            plogd("updateRestrictReasonForEntitlementPerCarrier: no correspondent cache, load from persist storage");
            try {
                subscriptionProperty = this.mSubscriptionManagerService.getSubscriptionProperty(i, "satellite_entitlement_status", this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
            } catch (IllegalArgumentException | SecurityException e) {
                ploge("updateRestrictReasonForEntitlementPerCarrier, e=" + e);
                subscriptionProperty = null;
            }
            String str = "0";
            if (subscriptionProperty == null) {
                ploge("updateRestrictReasonForEntitlementPerCarrier: invalid subId, subId=" + i + " set to default value");
                subscriptionProperty = "0";
            }
            if (subscriptionProperty.isEmpty()) {
                ploge("updateRestrictReasonForEntitlementPerCarrier: no data for subId(" + i + "). set to default value");
            } else {
                str = subscriptionProperty;
            }
            handleIndividualEntitlementMetricReport(i, str.equals("1"));
        }
        if (((Boolean) this.mSatelliteEntitlementStatusPerCarrier.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda38
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.FALSE;
            }
        })).booleanValue()) {
            return;
        }
        addAttachRestrictionForCarrier(i, 2, stub);
    }

    protected boolean persistSatelliteAttachEnabledForCarrierSetting(int i) {
        plogd("persistSatelliteAttachEnabledForCarrierSetting");
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            ploge("persistSatelliteAttachEnabledForCarrierSetting: subId is not valid, subId=" + i);
            return false;
        }
        try {
            this.mSubscriptionManagerService.setSubscriptionProperty(i, "satellite_attach_enabled_for_carrier", this.mSatelliteAttachRestrictionForCarrierArray.get(Integer.valueOf(i)).contains(0) ? "0" : "1");
            return true;
        } catch (IllegalArgumentException | SecurityException e) {
            ploge("persistSatelliteAttachEnabledForCarrierSetting, ex=" + e);
            return false;
        }
    }

    protected boolean isSatelliteRestrictedForCarrier(int i) {
        return (isSatelliteAttachEnabledForCarrierByUser(i) && isSatelliteSupportedViaCarrier(i) && !hasReasonToRestrictSatelliteCommunicationForCarrier(i)) ? false : true;
    }

    public boolean isSatelliteEnabledForCarrierAtModem(int i) {
        return ((Boolean) this.mIsSatelliteAttachEnabledForCarrierArrayPerSub.getOrDefault(Integer.valueOf(i), Boolean.FALSE)).booleanValue();
    }

    public boolean isSatelliteEnabledByDefaultForReason(int i) {
        plogd("isSatelliteEnabledByDefaultForReason: requesting default enablement status for reason=" + i);
        Boolean bool = this.mSatelliteEnabledByDefaultForReasonCache.get(Integer.valueOf(i));
        if (bool != null) {
            plogd("isSatelliteEnabledByDefaultForReason: found cached value=" + bool + " for reason=" + i);
            return bool.booleanValue();
        }
        if (i != 2) {
            plogd("isSatelliteEnabledByDefaultForReason: unknown satellite enablement request reason=" + i + ". Returning false.");
            return false;
        }
        try {
            boolean z = this.mContext.getResources().getBoolean(R.bool.config_supportSystemNavigationKeys);
            this.mSatelliteEnabledByDefaultForReasonCache.put(Integer.valueOf(i), Boolean.valueOf(z));
            plogd("isSatelliteEnabledByDefaultForReason: loaded from resources isSatelliteEnabledByDefault=" + z + " for reason=" + i);
            return z;
        } catch (Resources.NotFoundException e) {
            ploge("isSatelliteEnabledByDefaultForReason: Resource not found for reason=" + i + ", e=" + e + ". Returning false.");
            return false;
        }
    }

    private void evaluateEnablingSatelliteForCarrier(int i, int i2, Consumer consumer) {
        if (consumer == null) {
            consumer = new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda43
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$evaluateEnablingSatelliteForCarrier$31((Integer) obj);
                }
            };
        }
        Phone phone = SatelliteServiceUtils.getPhone(i);
        if (phone == null) {
            ploge("evaluateEnablingSatelliteForCarrier: phone is null for subId=" + i);
            consumer.accept(6);
            return;
        }
        boolean z = (!isSatelliteRestrictedForCarrier(i) || isOnlyEmergencyServiceSupported(i) || isDisasterServiceSupported(i)) && isSatelliteSupportedViaCarrier(i) && getCarrierRoamingNtnConnectType(i) == 0;
        plogd("evaluateEnablingSatelliteForCarrier: subId=" + i + " reason=" + i2 + " isSatelliteExpectedToBeEnabled=" + z + " isSatelliteEnabledForCarrierAtModem=" + isSatelliteEnabledForCarrierAtModem(phone.getSubId()));
        phone.setSatelliteEnabledForCarrier(SubscriptionManager.getSlotIndex(i), z, obtainMessage(31, new SatelliteControllerHandlerRequest(new RequestHandleSatelliteAttachRestrictionForCarrierArgument(i, i2, consumer), SatelliteServiceUtils.getPhone(i))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$evaluateEnablingSatelliteForCarrier$31(Integer num) {
        plogd("evaluateEnablingSatelliteForCarrier: SetSatelliteAttachEnableForCarrier error code =" + num);
    }

    private int evaluateOemSatelliteRequestAllowed(boolean z) {
        if (!this.mSatelliteModemInterface.isSatelliteServiceSupported()) {
            plogd("evaluateOemSatelliteRequestAllowed: satellite service is not supported");
            return 11;
        }
        Boolean boolIsSatelliteSupportedViaOemInternal = isSatelliteSupportedViaOemInternal();
        if (boolIsSatelliteSupportedViaOemInternal == null) {
            plogd("evaluateOemSatelliteRequestAllowed: satelliteSupported is null");
            return 6;
        }
        if (!boolIsSatelliteSupportedViaOemInternal.booleanValue()) {
            return 20;
        }
        if (!z) {
            return 0;
        }
        Boolean boolIsDeviceProvisioned = isDeviceProvisioned();
        if (boolIsDeviceProvisioned == null) {
            plogd("evaluateOemSatelliteRequestAllowed: satelliteProvisioned is null");
            return 6;
        }
        if (boolIsDeviceProvisioned.booleanValue()) {
            return 0;
        }
        plogd("evaluateOemSatelliteRequestAllowed: satellite service is not provisioned");
        return 13;
    }

    protected int getSupportedNtnRadioTechnology() {
        SatelliteCapabilities satelliteCapabilities = getSatelliteCapabilities();
        if (satelliteCapabilities != null) {
            return ((Integer) satelliteCapabilities.getSupportedRadioTechnologies().stream().findFirst().orElse(0)).intValue();
        }
        return 0;
    }

    public List<String> getSatelliteSupportedMsgApps(int i) {
        String[] stringArray = getConfigForSubId(i).getStringArray("satellite_supported_msg_apps_string_array");
        return stringArray != null ? List.of((Object[]) stringArray) : Collections.EMPTY_LIST;
    }

    private void sendErrorAndReportSessionMetrics(int i, Consumer consumer) {
        consumer.accept(Integer.valueOf(i));
        this.mSessionMetricsStats.setInitializationResult(i).setSatelliteTechnology(getSupportedNtnRadioTechnology()).setIsDemoMode(this.mIsDemoModeEnabled.get()).setCarrierId(getSatelliteCarrierId()).setSupportedConnectionMode(getSupportedConnectTypeMetrics()).setSessionConnectionMode(getSessionConnectTypeMetrics()).setIsNtnOnlyCarrier(isNtnOnlyCarrier()).reportSessionMetrics();
        this.mSessionStartTimeStamp.set(0L);
        this.mSessionProcessingTimeStamp.set(0L);
    }

    public boolean isNtnOnlyCarrier() {
        int i = this.mSelectedSatelliteSubId.get();
        return i != -1 && i == getNtnOnlySubscriptionId();
    }

    private void registerForServiceStateChanged() {
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.registerForServiceStateChanged(this, 37, null);
        }
    }

    private void registerForSignalStrengthChanged() {
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.getSignalStrengthController().registerForSignalStrengthChanged(this, 57, Integer.valueOf(phone.getPhoneId()));
        }
    }

    private void handleEventServiceStateChanged() {
        evaluateCarrierRoamingNtnEligibilityChange();
        handleServiceStateForSatelliteConnectionViaCarrier();
    }

    private void handleServiceStateForSatelliteConnectionViaCarrier() {
        for (Phone phone : PhoneFactory.getPhones()) {
            int subId = phone.getSubId();
            ServiceState serviceState = phone.getServiceState();
            if (serviceState != null && subId != -1) {
                CarrierRoamingSatelliteSessionStats carrierRoamingSatelliteSessionStats = this.mCarrierRoamingSatelliteSessionStatsMap.get(Integer.valueOf(subId));
                if (DEBUG) {
                    plogd("handleServiceStateForSatelliteConnectionViaCarrier : SubId = " + subId + "  isUsingNonTerrestrialNetwork = " + serviceState.isUsingNonTerrestrialNetwork());
                }
                if (serviceState.isUsingNonTerrestrialNetwork()) {
                    if (carrierRoamingSatelliteSessionStats != null) {
                        carrierRoamingSatelliteSessionStats.onSignalStrength(phone);
                        if (!((Boolean) this.mWasSatelliteConnectedViaCarrier.computeIfAbsent(Integer.valueOf(subId), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda46
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return Boolean.FALSE;
                            }
                        })).booleanValue()) {
                            carrierRoamingSatelliteSessionStats.onConnectionStart(phone);
                        }
                    }
                    resetCarrierRoamingSatelliteModeParams(subId);
                    this.mWasSatelliteConnectedViaCarrier.put(Integer.valueOf(subId), Boolean.TRUE);
                    int satelliteTechnology = 0;
                    for (NetworkRegistrationInfo networkRegistrationInfo : serviceState.getNetworkRegistrationInfoList()) {
                        if (networkRegistrationInfo.isNonTerrestrialNetwork()) {
                            if (DEBUG) {
                                plogd("handleServiceStateForSatelliteConnectionViaCarrier: nri.getAvailableServices() = " + networkRegistrationInfo.getAvailableServices());
                            }
                            this.mSatModeCapabilitiesForCarrierRoaming.put(Integer.valueOf(subId), networkRegistrationInfo.getAvailableServices());
                            satelliteTechnology = networkRegistrationInfo.getSatelliteTechnology();
                        }
                    }
                    updateCurrentSatellitePerPlmnConfiguration(subId, serviceState.getOperatorNumeric(), satelliteTechnology);
                } else {
                    Boolean bool = (Boolean) this.mWasSatelliteConnectedViaCarrier.computeIfAbsent(Integer.valueOf(subId), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda47
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return Boolean.FALSE;
                        }
                    });
                    if (getWwanIsInService(serviceState) || serviceState.getState() == 3) {
                        plogd("handleServiceStateForSatelliteConnectionViaCarrier: resetCarrierRoamingSatelliteModeParams: device is connected to terrestrial service or radio is off");
                        resetCarrierRoamingSatelliteModeParams(subId);
                        plogd("handleServiceStateForSatelliteConnectionViaCarrier: clearing  current satellite per plmn configs for subId: " + subId);
                        this.mCurrentSatellitePerPlmnConfigurations.remove(Integer.valueOf(subId));
                    } else if (bool != null && bool.booleanValue()) {
                        this.mLastSatelliteDisconnectedTimesMillis.put(Integer.valueOf(subId), Long.valueOf(getElapsedRealtime()));
                        plogd("handleServiceStateForSatelliteConnectionViaCarrier: sendMessageDelayed subId:" + subId + " phoneId:" + phone.getPhoneId() + " time:" + getSatelliteConnectionHysteresisTimeMillis(subId));
                        sendMessageDelayed(obtainMessage(42, Integer.valueOf(phone.getPhoneId())), getSatelliteConnectionHysteresisTimeMillis(subId));
                        if (carrierRoamingSatelliteSessionStats != null) {
                            carrierRoamingSatelliteSessionStats.onConnectionEnd();
                        }
                    }
                    this.mWasSatelliteConnectedViaCarrier.put(Integer.valueOf(subId), Boolean.FALSE);
                }
                updateLastNotifiedNtnModeAndNotify(phone);
                updateLastNotifiedCarrierRoamingNtnSignalStrengthAndNotify(phone);
            }
        }
        determineAutoConnectSystemNotification();
    }

    public boolean isDtcSatelliteTechnologySupported(int i, String str) {
        logd("isDtcSatelliteTechnologySupported: subId=" + i + ", plmn=" + str);
        if (!getAllPlmnSet().contains(str)) {
            logd("isDtcSatelliteTechnologySupported: the plmn=" + str + " is not contained in satellite provider");
            return false;
        }
        List<Integer> supportedSatelliteTechnologies = getSupportedSatelliteTechnologies(i, str);
        if (supportedSatelliteTechnologies == null || supportedSatelliteTechnologies.isEmpty()) {
            logd("isDtcSatelliteTechnologySupported: supportedSatelliteTechList=" + supportedSatelliteTechnologies + " null or empty, return true.");
            return true;
        }
        logd("isDtcSatelliteTechnologySupported: supportedSatelliteTechList=" + supportedSatelliteTechnologies);
        return !Collections.disjoint(supportedSatelliteTechnologies, DTC_SATELLITE_TECHNOLOGY_LIST);
    }

    public List<Integer> getSupportedSatelliteTechnologies(int i, String str) {
        Map plmnSatelliteTechForCarrier = getPlmnSatelliteTechForCarrier(i);
        if (plmnSatelliteTechForCarrier == null || plmnSatelliteTechForCarrier.isEmpty()) {
            plogd("getSupportedSatelliteTechnologies: plmnSatelliteTechMap is empty");
            return new ArrayList();
        }
        List<Integer> list = (List) plmnSatelliteTechForCarrier.getOrDefault(str, new ArrayList());
        logd("getSupportedSatelliteTechnologies: subId=" + i + ", plmn=" + str + ", satelliteTechList=" + list);
        return list;
    }

    private void updateSatelliteTechPerPlmnForActiveSubscriptions() {
        this.mSatelliteTechPerPlmnForActiveSubId.clear();
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (activeSubIdList == null) {
            plogd("updateSatelliteTechPerPlmnForActiveSubscriptions: activeSubIds is null.");
            return;
        }
        for (int i : activeSubIdList) {
            PersistableBundle persistableBundle = getPersistableBundle(i);
            if (persistableBundle == null) {
                logd("updateSatelliteTechPerPlmnForActiveSubscriptions: no carrier config found for subId: " + i);
            } else {
                PersistableBundle persistableBundle2 = persistableBundle.getPersistableBundle("satellite_configs_per_plmn_bundle");
                if (persistableBundle2 == null || persistableBundle2.isEmpty()) {
                    logd("updateSatelliteTechPerPlmnForActiveSubscriptions: no carrier config found for KEY_SATELLITE_CONFIGS_PER_PLMN_BUNDLE");
                } else {
                    HashMap map = new HashMap();
                    for (String str : persistableBundle2.keySet()) {
                        PersistableBundle persistableBundle3 = persistableBundle2.getPersistableBundle(str);
                        if (persistableBundle3 != null) {
                            int[] intArray = persistableBundle3.getIntArray("satellite_technology_type_int_array");
                            if (intArray != null) {
                                ArrayList arrayList = new ArrayList();
                                for (int i2 : intArray) {
                                    if (SatelliteServiceUtils.isSatelliteTechSupported(i2)) {
                                        arrayList.add(Integer.valueOf(i2));
                                    } else {
                                        logw("updateSatelliteTechPerPlmnForActiveSubscriptions: unsupported satellite tech=" + i2);
                                    }
                                }
                                if (!arrayList.isEmpty()) {
                                    map.put(str, arrayList);
                                }
                            } else {
                                logw("updateSatelliteTechPerPlmnForActiveSubscriptions: satelliteTechs is null");
                            }
                        }
                    }
                    logd("updateSatelliteTechPerPlmnForActiveSubscriptions: subId=" + i + ", plmnTechMap=" + map);
                    this.mSatelliteTechPerPlmnForActiveSubId.put(Integer.valueOf(i), map);
                }
            }
        }
    }

    private Map getPlmnSatelliteTechForCarrier(int i) {
        return (Map) this.mSatelliteTechPerPlmnForActiveSubId.getOrDefault(Integer.valueOf(i), new HashMap());
    }

    private Integer getNtnConnectTypeViaConfigupdater(int i, String str) {
        if (TextUtils.isEmpty(str)) {
            logd("getNtnConnectTypeViaConfigupdater: return null (empty plmn)");
            return null;
        }
        SatelliteConfig satelliteConfig = getSatelliteConfig();
        if (satelliteConfig == null) {
            logd("getNtnConnectTypeViaConfigupdater: return null (satelliteConfig is null)");
            return null;
        }
        SatelliteConfig.PlmnConfig satellitePlmnConfigBySubId = satelliteConfig.getSatellitePlmnConfigBySubId(i, str);
        if (satellitePlmnConfigBySubId == null) {
            logd("getNtnConnectTypeViaConfigupdater: return null (plmnConfig is null)");
            return null;
        }
        plogd("getNtnConnectTypeViaConfigupdater: connectType=" + satellitePlmnConfigBySubId.getNtnConnectType());
        return Integer.valueOf(satellitePlmnConfigBySubId.getNtnConnectType());
    }

    private void updateCurrentSatellitePerPlmnConfiguration(int i, String str, int i2) {
        plogd("updateCurrentSatellitePerPlmnConfiguration: device is in NTN mode, with subId: " + i + " with plmn: " + str);
        SatellitePerPlmnConfiguration satellitePerPlmnConfiguration = new SatellitePerPlmnConfiguration();
        satellitePerPlmnConfiguration.plmn = str;
        satellitePerPlmnConfiguration.currentSatelliteTechnology = i2;
        PersistableBundle persistableBundle = getPersistableBundle(i).getPersistableBundle("satellite_configs_per_plmn_bundle").getPersistableBundle(str);
        if (persistableBundle == null) {
            plogd("updateCurrentSatellitePerPlmnConfiguration: plmnSpecificConfig is null");
            Integer ntnConnectTypeViaConfigupdater = getNtnConnectTypeViaConfigupdater(i, str);
            if (ntnConnectTypeViaConfigupdater != null) {
                logd("updateCurrentSatellitePerPlmnConfiguration: ntnConnectTypePerPlmnViaConfigupdater=" + ntnConnectTypeViaConfigupdater);
                satellitePerPlmnConfiguration.connectType = ntnConnectTypeViaConfigupdater.intValue();
            } else {
                satellitePerPlmnConfiguration.connectType = -1;
            }
            this.mCurrentSatellitePerPlmnConfigurations.put(Integer.valueOf(i), satellitePerPlmnConfiguration);
            return;
        }
        satellitePerPlmnConfiguration.handoverType = persistableBundle.getInt("carrier_roaming_ntn_emergency_call_to_satellite_handover_type_int", 0);
        if (satellitePerPlmnConfiguration.connectType == -1) {
            satellitePerPlmnConfiguration.connectType = getCarrierRoamingNtnConnectTypeForPlmn(i, str);
            plogd("updateCurrentSatellitePerPlmnConfiguration: using CarrierConfig for subId=" + i + ", plmn=" + str + ", connectType=" + satellitePerPlmnConfiguration.connectType);
        }
        int[] intArray = persistableBundle.getIntArray("satellite_technology_type_int_array");
        if (intArray != null) {
            for (int i3 : intArray) {
                if (SatelliteServiceUtils.isSatelliteTechSupported(i3)) {
                    satellitePerPlmnConfiguration.supportedSatelliteTechs.add(Integer.valueOf(i3));
                }
            }
        }
        this.mCurrentSatellitePerPlmnConfigurations.put(Integer.valueOf(i), satellitePerPlmnConfiguration);
        plogd("updateCurrentSatellitePerPlmnConfiguration: set up satellite configs for subId: " + i + " plmn: " + str + ", configs: " + satellitePerPlmnConfiguration);
    }

    private void updateLastNotifiedNtnModeAndNotify(Phone phone) {
        if (phone == null) {
            plogd("updateLastNotifiedNtnModeAndNotify: phone is null");
            return;
        }
        int subId = phone.getSubId();
        boolean zBooleanValue = ((Boolean) this.mInitialized.computeIfAbsent(Integer.valueOf(subId), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda39
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.FALSE;
            }
        })).booleanValue();
        boolean zBooleanValue2 = ((Boolean) this.mLastNotifiedNtnMode.computeIfAbsent(Integer.valueOf(subId), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda40
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.FALSE;
            }
        })).booleanValue();
        boolean zIsInSatelliteModeForCarrierRoaming = isInSatelliteModeForCarrierRoaming(phone);
        plogd("updateLastNotifiedNtnModeAndNotify: subId=" + subId + " initialized=" + zBooleanValue + " lastNotifiedNtnMode=" + zBooleanValue2 + " currNtnMode=" + zIsInSatelliteModeForCarrierRoaming);
        if (!zIsInSatelliteModeForCarrierRoaming) {
            plogd("updateLastNotifiedNtnModeAndNotify: not in NTN mode, clear per plmn configs");
            this.mCurrentSatellitePerPlmnConfigurations.remove(Integer.valueOf(subId));
        }
        if (zBooleanValue && zBooleanValue2 == zIsInSatelliteModeForCarrierRoaming) {
            return;
        }
        if (!zBooleanValue) {
            this.mInitialized.put(Integer.valueOf(subId), Boolean.TRUE);
        }
        this.mLastNotifiedNtnMode.put(Integer.valueOf(subId), Boolean.valueOf(zIsInSatelliteModeForCarrierRoaming));
        phone.notifyCarrierRoamingNtnModeChanged(zIsInSatelliteModeForCarrierRoaming);
        updateLastNotifiedCarrierRoamingNtnSignalStrengthAndNotify(phone);
        logCarrierRoamingSatelliteSessionStats(phone, zBooleanValue2, zIsInSatelliteModeForCarrierRoaming);
        if (!this.mIsNotificationShowing.get() || zIsInSatelliteModeForCarrierRoaming) {
            return;
        }
        dismissSatelliteNotification();
    }

    private int[] getSupportedSatelliteServicesOnSessionStart(List list) {
        if (list == null || list.isEmpty()) {
            return new int[0];
        }
        return list.stream().mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray();
    }

    private void logCarrierRoamingSatelliteSessionStats(Phone phone, boolean z, boolean z2) {
        if (this.mIsDemoModeEnabled.get()) {
            plogd("logCarrierRoamingSatelliteSessionStats: return, demo mode is enabled");
            return;
        }
        int subId = phone.getSubId();
        int identifier = Binder.getCallingUserHandle().getIdentifier();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (!z && z2) {
            CarrierRoamingSatelliteSessionStats carrierRoamingSatelliteSessionStats = CarrierRoamingSatelliteSessionStats.getInstance(subId);
            String str = (String) Optional.ofNullable(phone).map(new SmsStats$$ExternalSyntheticLambda0()).map(new SmsStats$$ExternalSyntheticLambda1()).orElse("");
            carrierRoamingSatelliteSessionStats.onSessionStart(phone.getCarrierId(), phone, getSupportedSatelliteServicesOnSessionStart(getSupportedSatelliteServicesForPlmn(subId, str)), mapDataPolicyForMetrics(getSatelliteDataServicePolicyForPlmn(subId, str)), getSatelliteDataOptimizedApps(identifier), getSupportedConnectTypeMetrics(subId), getSessionConnectTypeMetrics(subId), str, this.mFeatureFlags, isScreenOn(), this.mIsWifiConnected.get());
            this.mCarrierRoamingSatelliteSessionStatsMap.put(Integer.valueOf(subId), carrierRoamingSatelliteSessionStats);
            this.mCarrierRoamingSatelliteControllerStats.onSessionStart(subId);
        } else if (z && !z2) {
            CarrierRoamingSatelliteSessionStats carrierRoamingSatelliteSessionStats2 = this.mCarrierRoamingSatelliteSessionStatsMap.get(Integer.valueOf(subId));
            if (carrierRoamingSatelliteSessionStats2 != null) {
                carrierRoamingSatelliteSessionStats2.onSessionEnd(subId, getSatelliteDataOptimizedApps(identifier));
                this.mCarrierRoamingSatelliteSessionStatsMap.remove(Integer.valueOf(subId));
            } else {
                plogd("logCarrierRoamingSatelliteSessionStats: sessionStats is null");
            }
            this.mCarrierRoamingSatelliteControllerStats.onSessionEnd(subId);
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
    }

    private void evaluateCarrierRoamingNtnEligibilityChange() {
        registerForSatelliteCommunicationAccessStateChanged();
        if (isSatelliteEnabledOrBeingEnabled()) {
            plogd("evaluateCarrierRoamingNtnEligibilityChange: Skip eligibility check as satellite is enabled or being enabled");
            return;
        }
        boolean zIsCarrierRoamingNtnEligible = isCarrierRoamingNtnEligible(getSatellitePhone());
        plogd("evaluateCarrierRoamingNtnEligibilityChange: isCarrierRoamingNtnEligible=" + zIsCarrierRoamingNtnEligible);
        if (zIsCarrierRoamingNtnEligible) {
            if (shouldStartNtnEligibilityHysteresisTimer(zIsCarrierRoamingNtnEligible)) {
                startNtnEligibilityHysteresisTimer();
            }
        } else {
            stopNtnEligibilityHysteresisTimer();
            updateLastNotifiedNtnEligibilityAndNotify(false);
        }
    }

    private boolean shouldStartNtnEligibilityHysteresisTimer(boolean z) {
        if (!z) {
            return false;
        }
        if (hasMessages(46)) {
            plogd("shouldStartNtnEligibilityHysteresisTimer: Timer is already running.");
            return false;
        }
        Boolean lastNotifiedNtnEligibility = getLastNotifiedNtnEligibility();
        return lastNotifiedNtnEligibility == null || !lastNotifiedNtnEligibility.booleanValue();
    }

    private void startNtnEligibilityHysteresisTimer() {
        Phone satellitePhone = getSatellitePhone();
        if (satellitePhone == null) {
            ploge("startNtnEligibilityHysteresisTimer: mSatellitePhone is null.");
            return;
        }
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        long carrierSupportedSatelliteNotificationHysteresisTimeMillis = getCarrierSupportedSatelliteNotificationHysteresisTimeMillis(selectedSatelliteSubId);
        plogd("startNtnEligibilityHysteresisTimer: sendMessageDelayed subId=" + selectedSatelliteSubId + ", phoneId=" + satellitePhone.getPhoneId() + ", timeout=" + carrierSupportedSatelliteNotificationHysteresisTimeMillis);
        sendMessageDelayed(obtainMessage(46), carrierSupportedSatelliteNotificationHysteresisTimeMillis);
    }

    private void stopNtnEligibilityHysteresisTimer() {
        if (hasMessages(46)) {
            removeMessages(46);
        }
    }

    private void updateLastNotifiedNtnEligibilityAndNotify(boolean z) {
        Phone satellitePhone = getSatellitePhone();
        if (satellitePhone == null) {
            ploge("notifyNtnEligibility: mSatellitePhone is null");
            return;
        }
        if (this.mOverrideNtnEligibility != null) {
            satellitePhone.notifyCarrierRoamingNtnEligibleStateChanged(z);
            return;
        }
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        plogd("notifyNtnEligibility: phoneId=" + satellitePhone.getPhoneId() + " currentNtnEligibility=" + z);
        Boolean lastNotifiedNtnEligibility = getLastNotifiedNtnEligibility();
        if (lastNotifiedNtnEligibility == null || lastNotifiedNtnEligibility.booleanValue() != z) {
            setLastNotifiedNtnEligibility(z);
            satellitePhone.notifyCarrierRoamingNtnEligibleStateChanged(z);
            updateSatelliteSystemNotification(selectedSatelliteSubId, 1, z);
        }
    }

    public boolean getLastNotifiedNtnEligibility(Phone phone) {
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        int subId = phone.getSubId();
        if (subId != selectedSatelliteSubId) {
            plogd("getLastNotifiedNtnEligibility: subId=" + subId + " does not match selectedSatelliteSubId=" + selectedSatelliteSubId);
            return false;
        }
        Boolean lastNotifiedNtnEligibility = getLastNotifiedNtnEligibility();
        plogd("getLastNotifiedNtnEligibility: return " + lastNotifiedNtnEligibility);
        if (lastNotifiedNtnEligibility == null) {
            return false;
        }
        return lastNotifiedNtnEligibility.booleanValue();
    }

    private void setLastNotifiedNtnEligibility(boolean z) {
        AtomicBoolean atomicBoolean = this.mLastNotifiedNtnEligibility;
        if (atomicBoolean == null) {
            this.mLastNotifiedNtnEligibility = new AtomicBoolean(z);
        } else {
            atomicBoolean.set(z);
        }
    }

    private Boolean getLastNotifiedNtnEligibility() {
        AtomicBoolean atomicBoolean = this.mLastNotifiedNtnEligibility;
        if (atomicBoolean == null) {
            return null;
        }
        return Boolean.valueOf(atomicBoolean.get());
    }

    private long getSatelliteConnectionHysteresisTimeMillis(int i) {
        return ((long) getPersistableBundle(i).getInt("satellite_connection_hysteresis_sec_int")) * 1000;
    }

    private long getCarrierSupportedSatelliteNotificationHysteresisTimeMillis(int i) {
        return ((long) getPersistableBundle(i).getInt("carrier_supported_satellite_notification_hysteresis_sec_int")) * 1000;
    }

    private void persistOemEnabledSatelliteProvisionStatus(boolean z) {
        plogd("persistOemEnabledSatelliteProvisionStatus: isProvisioned=" + z);
        int ntnOnlySubscriptionId = getNtnOnlySubscriptionId();
        if (ntnOnlySubscriptionId != -1) {
            try {
                this.mSubscriptionManagerService.setIsSatelliteProvisionedForNonIpDatagram(ntnOnlySubscriptionId, z);
                plogd("persistOemEnabledSatelliteProvisionStatus: subId=" + ntnOnlySubscriptionId);
                return;
            } catch (IllegalArgumentException | SecurityException e) {
                ploge("setIsSatelliteProvisionedForNonIpDatagram: subId=" + ntnOnlySubscriptionId + ", ex=" + e);
                return;
            }
        }
        plogd("persistOemEnabledSatelliteProvisionStatus: INVALID_SUBSCRIPTION_ID");
    }

    private boolean getPersistedDeviceProvisionStatus() {
        plogd("getPersistedDeviceProvisionStatus");
        int ntnOnlySubscriptionId = getNtnOnlySubscriptionId();
        if (ntnOnlySubscriptionId != -1 && this.mSubscriptionManagerService.isSatelliteProvisionedForNonIpDatagram(ntnOnlySubscriptionId)) {
            return true;
        }
        for (SubscriptionInfo subscriptionInfo : this.mSubscriptionManagerService.getActiveSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), true)) {
            if (subscriptionInfo.isSatelliteESOSSupported() && this.mSubscriptionManagerService.isSatelliteProvisionedForNonIpDatagram(subscriptionInfo.getSubscriptionId())) {
                this.mProvisionedSubscriberId.put((String) getSubscriberIdAndType(this.mSubscriptionManagerService.getSubscriptionInfo(ntnOnlySubscriptionId)).first, Boolean.TRUE);
                return true;
            }
        }
        return false;
    }

    private boolean loadSatelliteSharedPreferences() {
        if (this.mSharedPreferences != null) {
            return true;
        }
        try {
            this.mSharedPreferences = this.mContext.getSharedPreferences(SATELLITE_SHARED_PREF, 0);
            return true;
        } catch (Exception e) {
            ploge("loadSatelliteSharedPreferences: Cannot get default shared preferences, e=" + e);
            return false;
        }
    }

    private void handleIsSatelliteProvisionedDoneEvent(AsyncResult asyncResult) {
        logd("handleIsSatelliteProvisionedDoneEvent:");
        SatelliteControllerHandlerRequest satelliteControllerHandlerRequest = (SatelliteControllerHandlerRequest) asyncResult.userObj;
        Bundle bundle = new Bundle();
        bundle.putBoolean("satellite_provisioned", Boolean.TRUE.equals(isDeviceProvisioned()));
        ((ResultReceiver) satelliteControllerHandlerRequest.argument).send(0, bundle);
        decrementResultReceiverCount("SC:requestIsSatelliteProvisioned");
    }

    private long getWaitForSatelliteEnablingResponseTimeoutMillis() {
        return this.mContext.getResources().getInteger(R.integer.leanback_setup_alpha_forward_out_content_delay);
    }

    private long getWaitForCellularModemOffTimeoutMillis() {
        return this.mContext.getResources().getInteger(R.integer.config_vibratorControlServiceDumpSizeLimit);
    }

    private void startWaitForCellularModemOffTimer() {
        if (hasMessages(49)) {
            plogd("startWaitForCellularModemOffTimer: the timer was already started");
            return;
        }
        long waitForCellularModemOffTimeoutMillis = getWaitForCellularModemOffTimeoutMillis();
        plogd("Start timer to wait for cellular modem OFF state, timeoutMillis=" + waitForCellularModemOffTimeoutMillis);
        sendMessageDelayed(obtainMessage(49), waitForCellularModemOffTimeoutMillis);
    }

    private void stopWaitForCellularModemOffTimer() {
        plogd("Stop timer to wait for cellular modem OFF state");
        removeMessages(49);
    }

    private void startWaitForSatelliteEnablingResponseTimer(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        if (hasMessages(39, requestSatelliteEnabledArgument)) {
            plogd("WaitForSatelliteEnablingResponseTimer of request ID " + requestSatelliteEnabledArgument.requestId + " was already started");
            return;
        }
        plogd("Start timer to wait for response of the satellite enabling request ID=" + requestSatelliteEnabledArgument.requestId + ", enableSatellite=" + requestSatelliteEnabledArgument.enableSatellite + ", mWaitTimeForSatelliteEnablingResponse=" + this.mWaitTimeForSatelliteEnablingResponse.get());
        sendMessageDelayed(obtainMessage(39, requestSatelliteEnabledArgument), this.mWaitTimeForSatelliteEnablingResponse.get());
    }

    private void stopWaitForSatelliteEnablingResponseTimer(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        plogd("Stop timer to wait for response of the satellite enabling request ID=" + requestSatelliteEnabledArgument.requestId + ", enableSatellite=" + requestSatelliteEnabledArgument.enableSatellite);
        removeMessages(39, requestSatelliteEnabledArgument);
    }

    private boolean shouldProcessEventSetSatelliteEnabledDone(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        return hasMessages(39, requestSatelliteEnabledArgument);
    }

    private void startWaitForUpdateSystemSelectionChannelsResponseTimer(UpdateSystemSelectionChannelsArgument updateSystemSelectionChannelsArgument) {
        if (hasMessages(63, updateSystemSelectionChannelsArgument)) {
            plogd("WaitForUpdateSystemSelectionChannelsResponseTimer of request ID " + updateSystemSelectionChannelsArgument.requestId + " was already started");
            return;
        }
        plogd("Start timer to wait for response of the system selection channels update request ID=" + updateSystemSelectionChannelsArgument.requestId + ", mWaitTimeForSatelliteEnablingResponse=" + this.mWaitTimeForSatelliteEnablingResponse.get());
        sendMessageDelayed(obtainMessage(63, updateSystemSelectionChannelsArgument), this.mWaitTimeForSatelliteEnablingResponse.get());
    }

    private void stopWaitForUpdateSystemSelectionChannelsResponseTimer(UpdateSystemSelectionChannelsArgument updateSystemSelectionChannelsArgument) {
        plogd("Stop timer to wait for response of the system selection channels update request ID=" + updateSystemSelectionChannelsArgument.requestId);
        removeMessages(63, updateSystemSelectionChannelsArgument);
    }

    private boolean shouldProcessEventUpdateSystemSelectionChannelsDone(UpdateSystemSelectionChannelsArgument updateSystemSelectionChannelsArgument) {
        return hasMessages(63, updateSystemSelectionChannelsArgument);
    }

    private void startWaitForUpdateSatelliteEnableAttributesResponseTimer(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        if (hasMessages(52, requestSatelliteEnabledArgument)) {
            plogd("WaitForUpdateSatelliteEnableAttributesResponseTimer of request ID " + requestSatelliteEnabledArgument.requestId + " was already started");
            return;
        }
        plogd("Start timer to wait for response of the update satellite enable attributes request ID=" + requestSatelliteEnabledArgument.requestId + ", enableSatellite=" + requestSatelliteEnabledArgument.enableSatellite + ", mWaitTimeForSatelliteEnablingResponse=" + this.mWaitTimeForSatelliteEnablingResponse.get());
        sendMessageDelayed(obtainMessage(52, requestSatelliteEnabledArgument), this.mWaitTimeForSatelliteEnablingResponse.get());
    }

    private void stopWaitForUpdateSatelliteEnableAttributesResponseTimer(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        plogd("Stop timer to wait for response of the enable attributes update request ID=" + requestSatelliteEnabledArgument.requestId + ", enableSatellite=" + requestSatelliteEnabledArgument.enableSatellite);
        removeMessages(52, requestSatelliteEnabledArgument);
    }

    private boolean shouldProcessEventUpdateSatelliteEnableAttributesDone(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        return hasMessages(52, requestSatelliteEnabledArgument);
    }

    private void handleEventWaitForSatelliteEnablingResponseTimedOut(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        plogw("Timed out to wait for response of the satellite enabling request ID=" + requestSatelliteEnabledArgument.requestId + ", enableSatellite=" + requestSatelliteEnabledArgument.enableSatellite);
        requestSatelliteEnabledArgument.callback.accept(24);
        if (requestSatelliteEnabledArgument.enableSatellite) {
            resetSatelliteEnabledRequest();
            abortSatelliteEnableAttributesUpdateRequest(15);
            if (getSatelliteDisabledRequest() == null) {
                RequestSatelliteEnabledArgument requestSatelliteEnabledArgument2 = new RequestSatelliteEnabledArgument(false, false, false, FunctionalUtils.ignoreRemoteException(new DatagramReceiver$SatelliteDatagramListenerHandler$$ExternalSyntheticLambda1(new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteController.20
                    public void accept(int i) {
                        SatelliteController.this.plogd("handleEventWaitForSatelliteEnablingResponseTimedOut: disable satellite result=" + i);
                    }
                })));
                setSatelliteDisabledRequest(requestSatelliteEnabledArgument2);
                sendRequestAsync(11, requestSatelliteEnabledArgument2, null);
            }
            this.mControllerMetricsStats.reportServiceEnablementFailCount(requestSatelliteEnabledArgument.enableDemoMode);
            this.mSessionMetricsStats.setInitializationResult(24).setSatelliteTechnology(getSupportedNtnRadioTechnology()).setInitializationProcessingTime(getElapsedRealtime() - this.mSessionProcessingTimeStamp.get()).setIsDemoMode(this.mIsDemoModeEnabled.get()).setCarrierId(getSatelliteCarrierId()).setSupportedConnectionMode(getSupportedConnectTypeMetrics()).setSessionConnectionMode(getSessionConnectTypeMetrics()).setIsNtnOnlyCarrier(isNtnOnlyCarrier()).reportSessionMetrics();
        } else {
            resetSatelliteDisabledRequest();
            this.mControllerMetricsStats.onSatelliteDisabled();
            this.mSessionMetricsStats.setTerminationResult(24).setSatelliteTechnology(getSupportedNtnRadioTechnology()).setTerminationProcessingTime(getElapsedRealtime() - this.mSessionProcessingTimeStamp.get()).setSessionDurationSec(calculateSessionDurationTimeSec()).reportSessionMetrics();
        }
        notifyEnablementFailedToSatelliteSessionController(requestSatelliteEnabledArgument.enableSatellite);
        this.mSessionStartTimeStamp.set(0L);
        this.mSessionProcessingTimeStamp.set(0L);
    }

    private void handleCmdUpdateNtnSignalStrengthReporting(boolean z) {
        if (!isSatelliteEnabledOrBeingEnabled()) {
            plogd("handleCmdUpdateNtnSignalStrengthReporting: ignore request, satellite is disabled");
            return;
        }
        this.mLatestRequestedStateForNtnSignalStrengthReport.set(z);
        if (this.mIsModemEnabledReportingNtnSignalStrength.get() == z) {
            plogd("handleCmdUpdateNtnSignalStrengthReporting: ignore request. mIsModemEnabledReportingNtnSignalStrength=" + this.mIsModemEnabledReportingNtnSignalStrength.get());
            return;
        }
        updateNtnSignalStrengthReporting(z);
    }

    private void updateNtnSignalStrengthReporting(boolean z) {
        Message messageObtainMessage = obtainMessage(36, new SatelliteControllerHandlerRequest(Boolean.valueOf(z), SatelliteServiceUtils.getPhone()));
        if (z) {
            plogd("updateNtnSignalStrengthReporting: startSendingNtnSignalStrength");
            this.mSatelliteModemInterface.startSendingNtnSignalStrength(messageObtainMessage);
        } else {
            plogd("updateNtnSignalStrengthReporting: stopSendingNtnSignalStrength");
            this.mSatelliteModemInterface.stopSendingNtnSignalStrength(messageObtainMessage);
        }
    }

    public boolean setShouldSendDatagramToModemInDemoMode(boolean z) {
        if (!isMockModemAllowed()) {
            plogd("setShouldSendDatagramToModemInDemoMode: mock modem not allowed.");
            return false;
        }
        this.mDatagramController.setShouldSendDatagramToModemInDemoMode(z);
        return true;
    }

    private void determineAutoConnectSystemNotification() {
        Pair<Boolean, Integer> pairIsUsingNonTerrestrialNetworkViaCarrier = isUsingNonTerrestrialNetworkViaCarrier();
        boolean z = this.mSharedPreferences.getBoolean("satellite_system_notification_done_key", false);
        if (z) {
            long j = this.mSharedPreferences.getLong("satellite_system_notification_time", 0L);
            logv("determineAutoConnectSystemNotification lastSetTimestamp = " + j);
            long jCurrentTimeMillis = System.currentTimeMillis();
            long notificationDisplayThrottleTimeout = getNotificationDisplayThrottleTimeout(getSelectedSatelliteSubId());
            if (j == 0 || jCurrentTimeMillis - j >= notificationDisplayThrottleTimeout) {
                logd("determineAutoConnectSystemNotification: reset preference data");
                this.mSharedPreferences.edit().putBoolean("satellite_system_notification_done_key", false).remove("satellite_system_notification_time").apply();
                z = false;
            }
        }
        if (DEBUG) {
            StringBuilder sb = new StringBuilder("determineAutoConnectSystemNotification: isNtn.first = ");
            sb.append(pairIsUsingNonTerrestrialNetworkViaCarrier.first);
            sb.append(" IsNotiToShow = ");
            sb.append(!z);
            sb.append(" mIsNotificationShowing = ");
            sb.append(this.mIsNotificationShowing.get());
            logd(sb.toString());
        }
        if (((Boolean) pairIsUsingNonTerrestrialNetworkViaCarrier.first).booleanValue()) {
            if (z || getCarrierRoamingNtnConnectType(((Integer) pairIsUsingNonTerrestrialNetworkViaCarrier.second).intValue()) != 0) {
                return;
            }
            logd("determineAutoConnectSystemNotification: updating notification as we are inCARRIER_ROAMING_NTN_CONNECT_AUTOMATIC session");
            updateSatelliteSystemNotification(((Integer) pairIsUsingNonTerrestrialNetworkViaCarrier.second).intValue(), 0, true);
            return;
        }
        if (!this.mIsNotificationShowing.get() || ((Boolean) isSatelliteConnectedViaCarrierWithinHysteresisTime().first).booleanValue()) {
            return;
        }
        dismissSatelliteNotification();
    }

    private void dismissSatelliteNotification() {
        this.mIsNotificationShowing.set(false);
        updateSatelliteSystemNotification(-1, -1, false);
    }

    public boolean isSatelliteSystemNotificationsEnabled(int i) {
        if (i == 0) {
            logd("isSatelliteSystemNotificationsEnabled: automatic connect type. Therefore, notification is enabled.");
            return true;
        }
        boolean z = this.mContext.getResources().getBoolean(R.bool.config_support_disable_satellite_while_enable_in_progress);
        Boolean isSatelliteSupported = getIsSatelliteSupported();
        if (isSatelliteSupported == null) {
            return false;
        }
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(selectedSatelliteSubId);
        logd("isSatelliteSystemNotificationsEnabled: SatelliteSubId = " + selectedSatelliteSubId);
        return z && isSatelliteSupported.booleanValue() && SubscriptionManager.isValidSubscriptionId(selectedSatelliteSubId) && ((isSatelliteSupportedViaCarrier(selectedSatelliteSubId) && (getCarrierRoamingNtnConnectType(selectedSatelliteSubId) == 1 || getCarrierRoamingNtnConnectType(selectedSatelliteSubId) == 2)) || subscriptionInfo.isOnlyNonTerrestrialNetwork());
    }

    private boolean isDataServiceSupported(int i) {
        return ArrayUtils.contains(getSupportedServicesOnCarrierRoamingNtn(i), 2);
    }

    private void updateSatelliteSystemNotification(int i, int i2, boolean z) {
        int i3;
        int i4;
        int i5;
        if (i2 != -1 && !isSatelliteSystemNotificationsEnabled(i2)) {
            plogd("updateSatelliteSystemNotification: satellite notifications are not enabled.");
            return;
        }
        plogd("updateSatelliteSystemNotification subId=" + i + ", carrierRoamingNtnConnectType=" + SatelliteServiceUtils.carrierRoamingNtnConnectTypeToString(i2) + ", visible=" + z);
        NotificationChannel notificationChannel = new NotificationChannel(ConfigProviderAdaptor.DOMAIN_SATELLITE, "satelliteChannel", 3);
        notificationChannel.setSound(null, null);
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        if (notificationManager == null) {
            ploge("updateSatelliteSystemNotification: notificationManager is null");
            return;
        }
        if (!z) {
            notificationManager.cancelAsUser("SatelliteController", 1, UserHandle.ALL);
            return;
        }
        notificationManager.createNotificationChannel(notificationChannel);
        if (i2 == 1) {
            plogd("updateSatelliteSystemNotification: carrierRoamingNtnConnectType is manual");
            i5 = R.string.whichHomeApplicationNamed;
            i4 = R.string.whichHomeApplicationLabel;
        } else {
            if (i2 == 0 && isDataServiceSupported(i)) {
                plogd("updateSatelliteSystemNotification: carrierRoamingNtnConnectType is automatic and data service is supported");
                i3 = R.string.whichImageCaptureApplicationNamed;
            } else {
                i3 = R.string.whichImageCaptureApplicationLabel;
            }
            i4 = i3;
            i5 = 17042120;
        }
        Notification.Builder visibility = new Notification.Builder(this.mContext, ConfigProviderAdaptor.DOMAIN_SATELLITE).setContentTitle(this.mContext.getResources().getString(i5)).setContentText(this.mContext.getResources().getString(i4)).setSmallIcon(R.drawable.ic_btn_square_browser_zoom_page_overview_normal).setAutoCancel(true).setColor(this.mContext.getColor(R.color.system_notification_accent_color)).setVisibility(1);
        Intent intent = new Intent();
        intent.setAction("open_message_button");
        visibility.addAction(new Notification.Action.Builder(0, this.mContext.getResources().getString(R.string.whichImageCaptureApplication), PendingIntent.getBroadcast(this.mContext, 0, intent, 67108864)).setAuthenticationRequired(true).build());
        Intent intent2 = new Intent();
        intent2.setAction("how_it_works_button");
        intent2.putExtra("SUBID", i);
        visibility.addAction(new Notification.Action.Builder(0, this.mContext.getResources().getString(R.string.whichHomeApplication), PendingIntent.getBroadcast(this.mContext, 0, intent2, 67108864)).setAuthenticationRequired(true).build());
        visibility.setContentIntent(PendingIntent.getBroadcast(this.mContext, 0, new Intent("action_notification_click"), 67108864));
        visibility.setDeleteIntent(PendingIntent.getBroadcast(this.mContext, 0, new Intent("action_notification_dismiss"), 67108864));
        notificationManager.notifyAsUser("SatelliteController", 1, visibility.build(), UserHandle.ALL);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("open_message_button");
        intentFilter.addAction("how_it_works_button");
        intentFilter.addAction("action_notification_click");
        intentFilter.addAction("action_notification_dismiss");
        this.mContext.registerReceiver(this.mNotificationInteractionBroadcastReceiver, intentFilter, 2);
        this.mIsNotificationShowing.set(true);
        this.mCarrierRoamingSatelliteControllerStats.reportCountOfSatelliteNotificationDisplayed(i);
        this.mCarrierRoamingSatelliteControllerStats.reportCarrierId(i);
        this.mSessionMetricsStats.addCountOfSatelliteNotificationDisplayed();
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteController$21, reason: invalid class name */
    class AnonymousClass21 extends BroadcastReceiver {
        AnonymousClass21() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.content.BroadcastReceiver
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                SatelliteController.loge("Received empty action from the notification");
                return;
            }
            action.hashCode();
            boolean z = false;
            switch (action) {
                case "how_it_works_button":
                    int intExtra = intent.getIntExtra("SUBID", -1);
                    Intent intent2 = new Intent("android.settings.SATELLITE_SETTING");
                    intent2.putExtra("sub_id", intExtra);
                    intent2.addFlags(268435456);
                    context.startActivity(intent2);
                    z = true;
                    break;
                case "action_notification_dismiss":
                    break;
                case "open_message_button":
                    context.startActivity((Intent) Optional.ofNullable(Telephony.Sms.getDefaultSmsPackage(context)).flatMap(new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$21$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return Optional.ofNullable(context.getPackageManager().getLaunchIntentForPackage((String) obj));
                        }
                    }).map(new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$21$$ExternalSyntheticLambda1
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return SatelliteController.AnonymousClass21.$r8$lambda$Vwcc1LltPLebMWxbNw7_hSvOXuo((Intent) obj);
                        }
                    }).orElseGet(new Supplier() { // from class: com.android.internal.telephony.satellite.SatelliteController$21$$ExternalSyntheticLambda2
                        @Override // java.util.function.Supplier
                        public final Object get() {
                            return this.f$0.lambda$onReceive$2();
                        }
                    }));
                    z = true;
                    break;
                default:
                    z = true;
                    break;
            }
            SatelliteController.this.dismissNotificationAndUpdatePref(z);
        }

        public static /* synthetic */ Intent $r8$lambda$Vwcc1LltPLebMWxbNw7_hSvOXuo(Intent intent) {
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            return intent;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Intent lambda$onReceive$2() {
            SatelliteController.this.ploge("showSatelliteSystemNotification: no default sms package name, Invoke default sms compose window instead");
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("sms:"));
            return intent;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissNotificationAndUpdatePref(boolean z) {
        StatusBarManager statusBarManager;
        dismissSatelliteNotification();
        if (z && (statusBarManager = (StatusBarManager) this.mContext.getSystemService(StatusBarManager.class)) != null) {
            statusBarManager.collapsePanels();
        }
        this.mSharedPreferences.edit().putBoolean("satellite_system_notification_done_key", true).apply();
        this.mSharedPreferences.edit().putLong("satellite_system_notification_time", System.currentTimeMillis()).apply();
        this.mContext.unregisterReceiver(this.mNotificationInteractionBroadcastReceiver);
    }

    private void resetCarrierRoamingSatelliteModeParams() {
        for (Phone phone : PhoneFactory.getPhones()) {
            resetCarrierRoamingSatelliteModeParams(phone.getSubId());
        }
    }

    private void resetCarrierRoamingSatelliteModeParams(int i) {
        this.mLastSatelliteDisconnectedTimesMillis.remove(Integer.valueOf(i));
        this.mSatModeCapabilitiesForCarrierRoaming.remove(Integer.valueOf(i));
        this.mWasSatelliteConnectedViaCarrier.put(Integer.valueOf(i), Boolean.FALSE);
    }

    protected PersistableBundle getPersistableBundle(int i) {
        PersistableBundle persistableBundle = (PersistableBundle) this.mCarrierConfigArray.get(Integer.valueOf(i));
        if (persistableBundle != null) {
            return persistableBundle;
        }
        PersistableBundle configForSubId = getConfigForSubId(i);
        this.mCarrierConfigArray.put(Integer.valueOf(i), configForSubId);
        return configForSubId;
    }

    private int calculateSessionDurationTimeSec() {
        return (int) ((((getElapsedRealtime() - this.mSessionStartTimeStamp.get()) - this.mSessionMetricsStats.getSessionInitializationProcessingTimeMillis()) - this.mSessionMetricsStats.getSessionTerminationProcessingTimeMillis()) / 1000);
    }

    private void notifyEnablementFailedToSatelliteSessionController(boolean z) {
        SatelliteSessionController satelliteSessionController = this.mSatelliteSessionController;
        if (satelliteSessionController != null) {
            satelliteSessionController.onSatelliteEnablementFailed(z);
        } else {
            ploge("notifyEnablementFailedToSatelliteSessionController: mSatelliteSessionController is not initialized yet");
        }
    }

    private void abortSatelliteEnableRequest(int i) {
        RequestSatelliteEnabledArgument satelliteEnabledRequest = getSatelliteEnabledRequest();
        if (satelliteEnabledRequest != null) {
            plogw("abortSatelliteEnableRequest");
            if (i == 0) {
                i = 15;
            }
            satelliteEnabledRequest.callback.accept(Integer.valueOf(i));
            stopWaitForSatelliteEnablingResponseTimer(satelliteEnabledRequest);
            setSatelliteEnabledRequest(null);
        }
    }

    private void abortSatelliteDisableRequest(int i) {
        RequestSatelliteEnabledArgument satelliteDisabledRequest = getSatelliteDisabledRequest();
        if (satelliteDisabledRequest != null) {
            plogd("abortSatelliteDisableRequest");
            satelliteDisabledRequest.callback.accept(Integer.valueOf(i));
            stopWaitForSatelliteEnablingResponseTimer(satelliteDisabledRequest);
            setSatelliteDisabledRequest(null);
        }
    }

    private void abortSatelliteEnableAttributesUpdateRequest(int i) {
        RequestSatelliteEnabledArgument satelliteEnableAttributesUpdateRequest = getSatelliteEnableAttributesUpdateRequest();
        if (satelliteEnableAttributesUpdateRequest != null) {
            plogd("abortSatelliteEnableAttributesUpdateRequest");
            if (i == 0) {
                i = 15;
            }
            satelliteEnableAttributesUpdateRequest.callback.accept(Integer.valueOf(i));
            stopWaitForUpdateSatelliteEnableAttributesResponseTimer(satelliteEnableAttributesUpdateRequest);
            setSatelliteEnableAttributesUpdateRequest(null);
        }
    }

    private long getDemoPointingAlignedDurationMillisFromResources() {
        try {
            return this.mContext.getResources().getInteger(R.integer.config_dreamOpenAnimationDuration);
        } catch (Resources.NotFoundException e) {
            loge("getPointingAlignedDurationMillis: ex=" + e);
            return 15000L;
        }
    }

    public long getDemoPointingAlignedDurationMillis() {
        return this.mDemoPointingAlignedDurationMillis.get();
    }

    private long getDemoPointingNotAlignedDurationMillisFromResources() {
        try {
            return this.mContext.getResources().getInteger(R.integer.config_dreamsBatteryLevelDrainCutoff);
        } catch (Resources.NotFoundException e) {
            loge("getPointingNotAlignedDurationMillis: ex=" + e);
            return SatelliteSessionController.DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_RECEIVING_MILLIS;
        }
    }

    public long getDemoPointingNotAlignedDurationMillis() {
        return this.mDemoPointingNotAlignedDurationMillis.get();
    }

    public boolean getWwanIsInService(ServiceState serviceState) {
        Iterator it = serviceState.getNetworkRegistrationInfoListForTransportType(1).iterator();
        while (it.hasNext()) {
            if (((NetworkRegistrationInfo) it.next()).isInService()) {
                logv("getWwanIsInService: return true");
                return true;
            }
        }
        logv("getWwanIsInService: return false");
        return false;
    }

    private static void logv(String str) {
        Log.v("SatelliteController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("SatelliteController", str);
    }

    private static void logw(String str) {
        Log.w("SatelliteController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("SatelliteController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void plogd(String str) {
        Log.d("SatelliteController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("SatelliteController", str);
        }
    }

    private void plogw(String str) {
        Log.w("SatelliteController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.warn("SatelliteController", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ploge(String str) {
        Log.e("SatelliteController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.error("SatelliteController", str);
        }
    }

    private void handlePersistentLoggingOnSessionStart(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        if (this.mPersistentLogger != null && requestSatelliteEnabledArgument.isEmergency) {
            DropBoxManagerLoggerBackend.getInstance(this.mContext).setLoggingEnabled(true);
        }
    }

    private void handlePersistentLoggingOnSessionEnd(boolean z) {
        if (this.mPersistentLogger == null) {
            return;
        }
        DropBoxManagerLoggerBackend dropBoxManagerLoggerBackend = DropBoxManagerLoggerBackend.getInstance(this.mContext);
        if (z) {
            dropBoxManagerLoggerBackend.flushAsync();
        }
        dropBoxManagerLoggerBackend.setLoggingEnabled(false);
    }

    public void setLastEmergencyCallTime() {
        this.mLastEmergencyCallTime.set(getElapsedRealtime());
        plogd("mLastEmergencyCallTime=" + this.mLastEmergencyCallTime.get());
    }

    public boolean isInEmergencyMode() {
        if (this.mLastEmergencyCallTime.get() == 0) {
            plogd("mLastEmergencyCallTime is 0, isInEmergencyMode() return false");
            return false;
        }
        if (getElapsedRealtime() - this.mLastEmergencyCallTime.get() <= this.mSatelliteEmergencyModeDurationMillis.get()) {
            plogd("Satellite is in emergency mode");
            return true;
        }
        plogd("isInEmergencyMode() return false");
        return false;
    }

    private long getSatelliteEmergencyModeDurationFromOverlayConfig(Context context) {
        Integer numValueOf = 300;
        try {
            numValueOf = Integer.valueOf(context.getResources().getInteger(R.integer.config_userTypePackageWhitelistMode));
        } catch (Resources.NotFoundException e) {
            ploge("getSatelliteEmergencyModeDurationFromOverlayConfig: got ex=" + e);
        }
        return TimeUnit.SECONDS.toMillis(numValueOf.intValue());
    }

    protected void evaluateESOSProfilesPrioritization() {
        int i;
        if (isSatelliteEnabledOrBeingEnabled()) {
            plogd("evaluateESOSProfilesPrioritization: Skip evaluation as satellite is enabled or being enabled");
            return;
        }
        List<SubscriptionInfo> allSubInfoList = this.mSubscriptionManagerService.getAllSubInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        TreeMap treeMap = new TreeMap();
        plogd("evaluateESOSProfilesPrioritization: allSubInfos.size()=" + allSubInfoList.size());
        boolean z = false;
        for (SubscriptionInfo subscriptionInfo : allSubInfoList) {
            final int subscriptionId = subscriptionInfo.getSubscriptionId();
            boolean zIsActive = subscriptionInfo.isActive();
            boolean z2 = this.mSubscriptionManagerService.getDefaultSmsSubId() == subscriptionId;
            boolean zIsOnlyNonTerrestrialNetwork = subscriptionInfo.isOnlyNonTerrestrialNetwork();
            boolean zIsSatelliteESOSSupported = subscriptionInfo.isSatelliteESOSSupported();
            boolean zIsCarrierSatelliteHigherPriority = isCarrierSatelliteHigherPriority(subscriptionInfo);
            if (zIsOnlyNonTerrestrialNetwork || zIsSatelliteESOSSupported) {
                if (zIsActive || zIsOnlyNonTerrestrialNetwork) {
                    if (zIsOnlyNonTerrestrialNetwork || isCarrierConfigLoaded(subscriptionId).booleanValue()) {
                        if (zIsSatelliteESOSSupported && zIsActive && z2 && zIsCarrierSatelliteHigherPriority) {
                            i = 0;
                        } else if (zIsSatelliteESOSSupported && zIsActive && zIsCarrierSatelliteHigherPriority) {
                            i = 1;
                        } else if (zIsOnlyNonTerrestrialNetwork) {
                            i = 2;
                        } else {
                            i = zIsSatelliteESOSSupported ? 3 : -1;
                        }
                        if (i != -1) {
                            ((List) treeMap.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda13
                                @Override // java.util.function.Function
                                public final Object apply(Object obj) {
                                    return SatelliteController.$r8$lambda$2ZYX_bmnQ0R5dYsb6h3sO7FhaR4((Integer) obj);
                                }
                            })).add(subscriptionInfo);
                        } else {
                            plogw("evaluateESOSProfilesPrioritization: Got -1 keyPriority for subId=" + subscriptionInfo.getSubscriptionId());
                        }
                        String str = (String) getSubscriberIdAndType(subscriptionInfo).first;
                        Optional optionalFindFirst = this.mSubscriberIdPerSub.entrySet().stream().filter(new Predicate() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda14
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return ((Integer) ((Map.Entry) obj).getValue()).equals(Integer.valueOf(subscriptionId));
                            }
                        }).map(new CarrierSignalAgent$$ExternalSyntheticLambda2()).findFirst();
                        if (optionalFindFirst.isPresent() && !str.equals(optionalFindFirst.get())) {
                            this.mSubscriberIdPerSub.remove(optionalFindFirst.get());
                            this.mProvisionedSubscriberId.remove(optionalFindFirst.get());
                            plogw("Old phone number is removed: id = " + subscriptionId + ", oldSubscriberId = " + ((String) optionalFindFirst.get()) + ", newSubscriberId = " + str);
                            z = true;
                        }
                        if (!str.isEmpty()) {
                            this.mSubscriberIdPerSub.put(str, Integer.valueOf(subscriptionId));
                        }
                    }
                }
            }
        }
        plogd("evaluateESOSProfilesPrioritization: newSubsInfoListPerPriority.size()=" + treeMap.size());
        if (!this.mHasSentBroadcast.get() && treeMap.size() == 0) {
            logd("evaluateESOSProfilesPrioritization: no satellite subscription available");
            return;
        }
        List prioritizedSatelliteSubscriberProvisionStatusList = getPrioritizedSatelliteSubscriberProvisionStatusList(treeMap);
        if (isPriorityChanged(getSubsInfoListPerPriority(), treeMap) || isSubscriberContentChanged(getLastEvaluatedSubscriberProvisionStatus(), prioritizedSatelliteSubscriberProvisionStatusList) || z) {
            setSubsInfoListPerPriority(treeMap);
            setLastEvaluatedSubscriberProvisionStatus(prioritizedSatelliteSubscriberProvisionStatusList);
            sendBroadCastForProvisionedESOSSubs();
            this.mHasSentBroadcast.set(true);
            selectBindingSatelliteSubscription(false);
        }
        updateCachedDeviceProvisionStatus();
    }

    public static /* synthetic */ List $r8$lambda$2ZYX_bmnQ0R5dYsb6h3sO7FhaR4(Integer num) {
        return new ArrayList();
    }

    private Boolean isCarrierConfigLoaded(int i) {
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(i, "carrier_config_applied_bool");
        return Boolean.valueOf(configForSubId != null ? configForSubId.getBoolean("carrier_config_applied_bool") : false);
    }

    private Pair getSubscriberIdAndType(SubscriptionInfo subscriptionInfo) {
        String phoneNumberBasedCarrier = "";
        int i = 0;
        if (subscriptionInfo == null) {
            logd("getSubscriberIdAndType: subscription info is null");
            return new Pair("", 0);
        }
        if (subscriptionInfo.isOnlyNonTerrestrialNetwork()) {
            phoneNumberBasedCarrier = subscriptionInfo.getIccId();
        } else if (subscriptionInfo.isSatelliteESOSSupported()) {
            phoneNumberBasedCarrier = getPhoneNumberBasedCarrier(subscriptionInfo.getSubscriptionId());
            i = 1;
        }
        logd("getSubscriberIdAndType: subscriberId=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, phoneNumberBasedCarrier) + ", subscriberIdType=" + i);
        return new Pair(phoneNumberBasedCarrier, Integer.valueOf(i));
    }

    public String getPhoneNumberBasedCarrier(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null) {
            plogd("getPhoneNumberBasedCarrier: subscriptionInfoInternal is null.");
            return "";
        }
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        if (this.mInjectSubscriptionManager != null) {
            plogd("getPhoneNumberBasedCarrier: InjectSubscriptionManager");
            subscriptionManager = this.mInjectSubscriptionManager;
        }
        if (subscriptionManager == null) {
            plogd("getPhoneNumberBasedCarrier: subscriptionManager is null");
            return "";
        }
        String lastKnownPhoneNumber = subscriptionManager.getLastKnownPhoneNumber(i);
        if (TextUtils.isEmpty(lastKnownPhoneNumber)) {
            plogd("getPhoneNumberBasedCarrier: phoneNumber is empty.");
            return "";
        }
        String imsi = subscriptionInfoInternal.getImsi();
        if (TextUtils.isEmpty(imsi)) {
            plogd("getPhoneNumberBasedCarrier: imsi is empty");
            return "";
        }
        if (imsi.length() < 6) {
            plogd("getPhoneNumberBasedCarrier: imsi length is less than 6");
            return "";
        }
        String str = subscriptionInfoInternal.getImsi().substring(0, 6) + lastKnownPhoneNumber.replaceFirst("^\\+", "");
        plogd("getPhoneNumberBasedCarrier: subscriberId=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, str));
        return str;
    }

    private boolean isPriorityChanged(Map map, Map map2) {
        if (map.size() == 0 || map.size() != map2.size()) {
            return true;
        }
        Iterator it = map.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                return false;
            }
            Map.Entry entry = (Map.Entry) it.next();
            List list = (List) entry.getValue();
            List list2 = (List) map2.get(entry.getKey());
            if (list2 == null || list == null || list.size() != list2.size()) {
                return true;
            }
            for (int i = 0; i < list.size(); i++) {
                if (((SubscriptionInfo) list.get(i)).getSubscriptionId() != ((SubscriptionInfo) list2.get(i)).getSubscriptionId()) {
                    logd("isPriorityChanged: cur=" + list.get(i) + " , new=" + list2.get(i));
                    return true;
                }
            }
        }
    }

    private boolean isSubscriberContentChanged(List list, List list2) {
        if (list.size() != list2.size()) {
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            SatelliteSubscriberProvisionStatus satelliteSubscriberProvisionStatus = (SatelliteSubscriberProvisionStatus) list.get(i);
            SatelliteSubscriberProvisionStatus satelliteSubscriberProvisionStatus2 = (SatelliteSubscriberProvisionStatus) list2.get(i);
            if (!satelliteSubscriberProvisionStatus.getSatelliteSubscriberInfo().equals(satelliteSubscriberProvisionStatus2.getSatelliteSubscriberInfo())) {
                logd("isSubscriberContentChanged: cur=" + satelliteSubscriberProvisionStatus + " , new=" + satelliteSubscriberProvisionStatus2);
                return true;
            }
        }
        return false;
    }

    private void sendBroadCastForProvisionedESOSSubs() {
        String configSatelliteGatewayServicePackage = getConfigSatelliteGatewayServicePackage();
        String configSatelliteCarrierRoamingEsosProvisionedClass = getConfigSatelliteCarrierRoamingEsosProvisionedClass();
        if (configSatelliteGatewayServicePackage == null || configSatelliteCarrierRoamingEsosProvisionedClass == null || configSatelliteGatewayServicePackage.isEmpty() || configSatelliteCarrierRoamingEsosProvisionedClass.isEmpty()) {
            logd("sendBroadCastForProvisionedESOSSubs: packageName or className is null or empty.");
            return;
        }
        Intent intent = new Intent("android.telephony.satellite.action.SATELLITE_SUBSCRIBER_ID_LIST_CHANGED");
        intent.setComponent(new ComponentName(configSatelliteGatewayServicePackage, configSatelliteCarrierRoamingEsosProvisionedClass));
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        logd("sendBroadCastForProvisionedESOSSubs" + intent);
    }

    protected String getStringFromOverlayConfig(int i) {
        try {
            return this.mContext.getResources().getString(i);
        } catch (Resources.NotFoundException e) {
            loge("getStringFromOverlayConfig: ex=" + e);
            return null;
        }
    }

    public void requestSatelliteDisplayName(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(78, someArgsObtain));
    }

    private void handleRequestSatelliteDisplayName(ResultReceiver resultReceiver) {
        plogd("handleRequestSatelliteDisplayName");
        String string = getConfigForSubId(getSelectedSatelliteSubId()).getString("satellite_display_name_string", "Satellite");
        plogd("requestSatelliteDisplayName: " + string);
        Bundle bundle = new Bundle();
        bundle.putString("satellite_display_name", string);
        resultReceiver.send(0, bundle);
    }

    public void requestPointingUiAppLaunchIntent(PointingUiAppLaunchIntentAttributes pointingUiAppLaunchIntentAttributes, ResultReceiver resultReceiver) {
        logd("requestPointingUiAppLaunchIntent: launchIntentAttributes=" + pointingUiAppLaunchIntentAttributes);
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = pointingUiAppLaunchIntentAttributes;
        someArgsObtain.arg2 = resultReceiver;
        sendMessage(obtainMessage(97, someArgsObtain));
    }

    public void requestSatelliteSubscriberProvisionStatus(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(84, someArgsObtain));
    }

    private void handleRequestSatelliteSubscriberProvisionStatus(ResultReceiver resultReceiver) {
        List prioritizedSatelliteSubscriberProvisionStatusList = getPrioritizedSatelliteSubscriberProvisionStatusList();
        logd("requestSatelliteSubscriberProvisionStatus: " + prioritizedSatelliteSubscriberProvisionStatusList);
        Bundle bundle = new Bundle();
        bundle.putParcelableList("request_provision_subscriber_id", prioritizedSatelliteSubscriberProvisionStatusList);
        resultReceiver.send(0, bundle);
    }

    private List getPrioritizedSatelliteSubscriberProvisionStatusList() {
        return getPrioritizedSatelliteSubscriberProvisionStatusList(getSubsInfoListPerPriority());
    }

    private List getPrioritizedSatelliteSubscriberProvisionStatusList(Map map) {
        ArrayList arrayList = new ArrayList();
        for (Integer num : map.keySet()) {
            int iIntValue = num.intValue();
            List<SubscriptionInfo> list = (List) map.get(num);
            if (list == null) {
                logd("getPrioritySatelliteSubscriberProvisionStatusList: no exist this priority " + iIntValue);
            } else {
                for (SubscriptionInfo subscriptionInfo : list) {
                    Pair subscriberIdAndType = getSubscriberIdAndType(subscriptionInfo);
                    String str = (String) subscriberIdAndType.first;
                    int carrierId = subscriptionInfo.getCarrierId();
                    String string = getConfigForSubId(subscriptionInfo.getSubscriptionId()).getString("satellite_nidd_apn_name_string", "");
                    logd("getPrioritySatelliteSubscriberProvisionStatusList: subscriberId:" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, str) + " , carrierId=" + carrierId + " , apn=" + string);
                    if (str.isEmpty()) {
                        logd("getPrioritySatelliteSubscriberProvisionStatusList: getSubscriberId failed skip this subscriberId.");
                    } else {
                        SatelliteSubscriberInfo satelliteSubscriberInfoBuild = new SatelliteSubscriberInfo.Builder().setSubscriberId(str).setCarrierId(carrierId).setNiddApn(string).setSubscriptionId(subscriptionInfo.getSubscriptionId()).setSubscriberIdType(((Integer) subscriberIdAndType.second).intValue()).build();
                        boolean zBooleanValue = this.mProvisionedSubscriberId.getOrDefault(str, Boolean.FALSE).booleanValue();
                        logd("getPrioritySatelliteSubscriberProvisionStatusList: satelliteSubscriberInfo=" + satelliteSubscriberInfoBuild + ", provisioned=" + zBooleanValue);
                        arrayList.add(new SatelliteSubscriberProvisionStatus.Builder().setSatelliteSubscriberInfo(satelliteSubscriberInfoBuild).setProvisioned(zBooleanValue).build());
                        this.mSubscriberIdPerSub.put(str, Integer.valueOf(subscriptionInfo.getSubscriptionId()));
                    }
                }
            }
        }
        return arrayList;
    }

    public int getSelectedSatelliteSubId() {
        plogd("getSelectedSatelliteSubId: subId=" + this.mSelectedSatelliteSubId.get());
        return this.mSelectedSatelliteSubId.get();
    }

    public void requestSelectedNbIotSatelliteSubscriptionId(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(79, someArgsObtain));
    }

    private void handleRequestSelectedNbIotSatelliteSubscriptionId(ResultReceiver resultReceiver) {
        plogd("handleRequestSelectedNbIotSatelliteSubscriptionId");
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        plogd("requestSelectedNbIotSatelliteSubscriptionId: " + selectedSatelliteSubId);
        if (selectedSatelliteSubId == -1) {
            resultReceiver.send(30, null);
            logd("requestSelectedNbIotSatelliteSubscriptionId: selectedSatelliteSubId is invalid");
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("selected_nb_iot_satellite_subscription_id", selectedSatelliteSubId);
            resultReceiver.send(0, bundle);
        }
    }

    private void selectBindingSatelliteSubscription(boolean z) {
        int ntnOnlySubscriptionId;
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        plogd("selectBindingSatelliteSubscription: preSelectedSatelliteSubId=" + selectedSatelliteSubId);
        if (selectedSatelliteSubId == -1) {
            plogd("selectBindingSatelliteSubscription: binding satellite subscription is not selected yet");
        } else if ((isSatelliteEnabled() || isSatelliteBeingEnabled()) && !z) {
            plogd("selectBindingSatelliteSubscription: satellite subscription will be selected once the satellite session ends");
            return;
        }
        Iterator it = getPrioritizedSatelliteSubscriberProvisionStatusList().iterator();
        while (true) {
            if (!it.hasNext()) {
                ntnOnlySubscriptionId = -1;
                break;
            }
            SatelliteSubscriberProvisionStatus satelliteSubscriberProvisionStatus = (SatelliteSubscriberProvisionStatus) it.next();
            ntnOnlySubscriptionId = getSubIdFromSubscriberId(satelliteSubscriberProvisionStatus.getSatelliteSubscriberInfo().getSubscriberId());
            if (satelliteSubscriberProvisionStatus.isProvisioned() && isActiveSubId(ntnOnlySubscriptionId) && isSatelliteAvailableAtCurrentLocation(this.mSubscriptionManagerService.getSubscriptionInfo(ntnOnlySubscriptionId))) {
                break;
            }
        }
        if (ntnOnlySubscriptionId == -1 && isSatelliteSupportedViaOem()) {
            ntnOnlySubscriptionId = getNtnOnlySubscriptionId();
        }
        setSelectedSatelliteSubId(ntnOnlySubscriptionId);
        if (selectedSatelliteSubId != getSelectedSatelliteSubId()) {
            plogd("selectBindingSatelliteSubscription: SelectedSatelliteSubId changed");
            this.mSatelliteSubIdChangedRegistrants.notifyRegistrants();
            handleEventSelectedNbIotSatelliteSubscriptionChanged(ntnOnlySubscriptionId);
            handleCarrierRoamingNtnAvailableServicesChanged();
            evaluateCarrierRoamingNtnEligibilityChange();
        }
        setSatellitePhone(ntnOnlySubscriptionId);
        if (ntnOnlySubscriptionId != -1) {
            this.mControllerMetricsStats.setCarrierIdInfo(getSatelliteCarrierId(), isNtnOnlyCarrier(), getSupportedConnectTypeMetrics(ntnOnlySubscriptionId));
        }
        plogd("selectBindingSatelliteSubscription: SelectedSatelliteSubId=" + ntnOnlySubscriptionId);
    }

    protected boolean isCarrierSatelliteHigherPriority(SubscriptionInfo subscriptionInfo) {
        return !isSatelliteAccessAllowedAtCurrentLocation() || isSatelliteAvailableAtCurrentLocation(subscriptionInfo);
    }

    protected boolean isSatelliteAvailableAtCurrentLocation(SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo == null) {
            plogd("isSatelliteAvailableAtCurrentLocation: subscriptionInfo is null");
            return false;
        }
        List list = this.mCtsSatelliteAccessAllowedSubIds;
        if (list != null) {
            if (list.contains(Integer.valueOf(subscriptionInfo.getSubscriptionId()))) {
                plogd("isSatelliteAvailableAtCurrentLocation: subscriptionId=" + subscriptionInfo.getSubscriptionId() + " is allowed for CTS testing");
                return true;
            }
            plogd("isSatelliteAvailableAtCurrentLocation: subscriptionId=" + subscriptionInfo.getSubscriptionId() + " is not allowed for CTS testing");
            return false;
        }
        if (!isSatelliteAccessAllowedAtCurrentLocation()) {
            plogd("isSatelliteAvailableAtCurrentLocation: satellite access is not allowed at current location");
            return false;
        }
        if (subscriptionInfo.isOnlyNonTerrestrialNetwork()) {
            return true;
        }
        synchronized (this.mSatelliteAccessConfigLock) {
            try {
                if (!this.mCurrentLocationCarrierIds.isEmpty()) {
                    plogd("isSatelliteAvailableAtCurrentLocation: mCurrentLocationCarrierIds=" + ((String) this.mCurrentLocationCarrierIds.stream().map(new SimultaneousCallingTracker$$ExternalSyntheticLambda0()).collect(Collectors.joining(", "))) + " info.getCarrierId()=" + subscriptionInfo.getCarrierId());
                    return this.mCurrentLocationCarrierIds.contains(Integer.valueOf(subscriptionInfo.getCarrierId()));
                }
                int[] intArray = this.mContext.getResources().getIntArray(R.array.vendor_disallowed_apps_managed_user);
                List list2 = (intArray == null || intArray.length <= 0) ? null : (List) Arrays.stream(intArray).boxed().collect(Collectors.toList());
                if (list2 == null) {
                    if (TextUtils.isEmpty(getSatelliteAccessConfigurationFileFromOverlayConfig())) {
                        plogd("isSatelliteAvailableAtCurrentLocation: device does not support custom satellite access configuration per location");
                        return true;
                    }
                    plogd("isSatelliteAvailableAtCurrentLocation: tagids for carrier " + ((Object) subscriptionInfo.getCarrierName()) + ", subId=" + subscriptionInfo.getSubscriptionId() + " are not available");
                    return false;
                }
                return isCarrierSatelliteAvailableAtCurrentLocation(list2);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private String getSatelliteAccessConfigurationFileFromOverlayConfig() {
        String string;
        try {
            string = this.mContext.getResources().getString(R.string.wfcSpnFormat_spn_wlan_call);
        } catch (Resources.NotFoundException e) {
            loge("getSatelliteAccessConfigurationFileFromOverlayConfig: got ex=" + e);
            string = null;
        }
        logd("satelliteAccessConfigFile =" + string);
        return string;
    }

    private boolean isCarrierSatelliteAvailableAtCurrentLocation(List list) {
        return !Collections.disjoint(list, getCurrentLocationTagIds());
    }

    private int getSubIdFromSubscriberId(String str) {
        return this.mSubscriberIdPerSub.getOrDefault(str, -1).intValue();
    }

    private boolean isActiveSubId(int i) {
        SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(i);
        if (subscriptionInfo == null) {
            logd("isActiveSubId: subscription associated with subId=" + i + " not found");
            return false;
        }
        return subscriptionInfo.isActive();
    }

    protected boolean isSubscriptionProvisioned(int i) {
        plogd("isSubscriptionProvisioned: subId=" + i);
        String str = (String) getSubscriberIdAndType(this.mSubscriptionManagerService.getSubscriptionInfo(i)).first;
        if (str.isEmpty()) {
            plogd("isSubscriptionProvisioned: subId=" + i + " subscriberId is empty.");
            return false;
        }
        return this.mProvisionedSubscriberId.getOrDefault(str, Boolean.FALSE).booleanValue();
    }

    public void provisionSatellite(List<SatelliteSubscriberInfo> list, ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = list;
        someArgsObtain.arg2 = resultReceiver;
        sendMessage(obtainMessage(86, someArgsObtain));
    }

    private void handleRequestProvisionSatellite(List list, ResultReceiver resultReceiver) {
        if (list.isEmpty()) {
            resultReceiver.send(8, null);
            logd("provisionSatellite: SatelliteSubscriberInfo list is empty");
            return;
        }
        logd("provisionSatellite:" + list);
        sendRequestAsync(44, new RequestProvisionSatelliteArgument(list, resultReceiver, true), null);
        incrementResultReceiverCount("SC:provisionSatellite");
    }

    public void updateSystemSelectionChannels(List<SystemSelectionSpecifier> list, ResultReceiver resultReceiver) {
        sendRequestAsync(58, new UpdateSystemSelectionChannelsArgument(list, resultReceiver), null);
    }

    public Map<String, Set<Integer>> getRegionalSatelliteEarfcns(int i) {
        if (this.mRegionalSatelliteEarfcns.containsKey(Integer.valueOf(i))) {
            return (Map) this.mRegionalSatelliteEarfcns.get(Integer.valueOf(i));
        }
        logd("getRegionalSatelliteEarfcns: Earfcns for subId: " + i + " not found");
        return new HashMap();
    }

    public void updateRegionalSatelliteEarfcns(int i) {
        plogd("updateRegionalSatelliteEarfcns with subId " + i);
        this.mRegionalSatelliteEarfcns.put(Integer.valueOf(i), readRegionalSatelliteEarfcnsFromCarrierConfig(i));
    }

    public void deprovisionSatellite(List<SatelliteSubscriberInfo> list, ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = list;
        someArgsObtain.arg2 = resultReceiver;
        sendMessage(obtainMessage(87, someArgsObtain));
    }

    private void handleRequestDeprovisionSatellite(List list, ResultReceiver resultReceiver) {
        if (list.isEmpty()) {
            resultReceiver.send(8, null);
            logd("deprovisionSatellite: SatelliteSubscriberInfo list is empty");
            return;
        }
        logd("deprovisionSatellite:" + list);
        sendRequestAsync(44, new RequestProvisionSatelliteArgument(list, resultReceiver, false), null);
        incrementResultReceiverCount("SC:provisionSatellite");
    }

    public void setNtnSmsSupportedByMessagesApp(boolean z) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Boolean.valueOf(z);
        sendMessage(obtainMessage(85, someArgsObtain));
    }

    private void handleRequestSetNtnSmsSupportedByMessagesApp(boolean z) {
        persistNtnSmsSupportedByMessagesApp(z);
        handleCarrierRoamingNtnAvailableServicesChanged();
    }

    private void persistNtnSmsSupportedByMessagesApp(boolean z) {
        plogd("persistNtnSmsSupportedByMessagesApp: ntnSmsSupported=" + z);
        if (loadSatelliteSharedPreferences()) {
            SharedPreferences sharedPreferences = this.mSharedPreferences;
            if (sharedPreferences == null) {
                ploge("persistNtnSmsSupportedByMessagesApp: mSharedPreferences is null");
            } else {
                sharedPreferences.edit().putBoolean(NTN_SMS_SUPPORTED_BY_MESSAGES_APP_KEY, z).apply();
                setNtnSmsSupportedByMessagesAppCache(z);
            }
        }
    }

    private void setNtnSmsSupportedByMessagesAppCache(boolean z) {
        AtomicBoolean atomicBoolean = this.mNtnSmsSupportedByMessagesApp;
        if (atomicBoolean == null) {
            this.mNtnSmsSupportedByMessagesApp = new AtomicBoolean(z);
        } else {
            atomicBoolean.set(z);
        }
    }

    private boolean isNtnSmsSupportedByMessagesApp() {
        if (this.mNtnSmsSupportedByMessagesApp != null) {
            plogd("isNtnSmsSupportedByMessagesApp:" + this.mNtnSmsSupportedByMessagesApp.get());
            return this.mNtnSmsSupportedByMessagesApp.get();
        }
        if (!loadSatelliteSharedPreferences()) {
            return false;
        }
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            ploge("isNtnSmsSupportedByMessagesApp: mSharedPreferences is null");
            return false;
        }
        boolean z = sharedPreferences.getBoolean(NTN_SMS_SUPPORTED_BY_MESSAGES_APP_KEY, false);
        setNtnSmsSupportedByMessagesAppCache(z);
        plogd("isNtnSmsSupportedByMessagesApp:" + z);
        return z;
    }

    protected void setSatellitePhone(int i) {
        Phone phone = SatelliteServiceUtils.getPhone(i);
        if (phone != null && phone != this.mSatellitePhone) {
            CommandsInterface commandsInterface = this.mCi;
            if (commandsInterface != null) {
                commandsInterface.unregisterForRadioStateChanged(this);
            }
            this.mCi = phone.mCi;
            this.mIsRadioOn.set(phone.isRadioOn());
            CommandsInterface commandsInterface2 = this.mCi;
            if (commandsInterface2 != null) {
                commandsInterface2.registerForRadioStateChanged(this, 23, null);
            }
        }
        setSatellitePhone(phone);
        StringBuilder sb = new StringBuilder("setSatellitePhone: phoneId=");
        sb.append(phone != null ? Integer.valueOf(phone.getPhoneId()) : "null");
        sb.append(", subId=");
        sb.append(i);
        plogd(sb.toString());
    }

    private void setSatellitePhone(Phone phone) {
        synchronized (this.mSatellitePhoneLock) {
            this.mSatellitePhone = phone;
        }
    }

    public Phone getSatellitePhone() {
        Phone phone;
        synchronized (this.mSatellitePhoneLock) {
            phone = this.mSatellitePhone;
        }
        return phone;
    }

    public String getSatellitePlmnForMetrics() {
        return (String) Optional.ofNullable(getSatellitePhone()).map(new SmsStats$$ExternalSyntheticLambda0()).map(new SmsStats$$ExternalSyntheticLambda1()).orElse(SatelliteConstants.DEFAULT_PLMN);
    }

    public String getSatellitePlmnForMetrics(Phone phone) {
        return (String) Optional.ofNullable(phone).map(new SmsStats$$ExternalSyntheticLambda0()).map(new SmsStats$$ExternalSyntheticLambda1()).orElse(SatelliteConstants.DEFAULT_PLMN);
    }

    protected void setSelectedSatelliteSubId(int i) {
        plogd("setSelectedSatelliteSubId: subId=" + i);
        this.mSelectedSatelliteSubId.set(i);
    }

    public int getSatelliteCarrierId() {
        SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(this.mSelectedSatelliteSubId.get());
        if (subscriptionInfo == null) {
            logd("getSatelliteCarrierId: returns UNKNOWN_CARRIER_ID");
            return -1;
        }
        return subscriptionInfo.getCarrierId();
    }

    public boolean isCarrierRoamingNtnEligible(Phone phone) {
        if (!this.mIsRadioOn.get()) {
            plogd("isCarrierRoamingNtnEligible: radio is off");
            return false;
        }
        if (!isSatelliteAccessAllowedAtCurrentLocation()) {
            plogd("isCarrierRoamingNtnEligible: satellite access is not allowed");
            return false;
        }
        if (!isLocationServiceEnabled()) {
            plogd("isCarrierRoamingNtnEligible: Location service is off");
            return false;
        }
        if (phone == null) {
            plogd("isCarrierRoamingNtnEligible: phone is null");
            return false;
        }
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        if (!isSatelliteRoamingP2pSmSSupported(selectedSatelliteSubId)) {
            plogd("isCarrierRoamingNtnEligible(" + selectedSatelliteSubId + "): doesn't support manual-connect P2P SMS");
            return false;
        }
        if (!isSatelliteSupportedViaCarrier(selectedSatelliteSubId)) {
            plogd("isCarrierRoamingNtnEligible[phoneId=" + phone.getPhoneId() + "]: satellite is not supported via carrier");
            return false;
        }
        if (!isSubscriptionProvisioned(selectedSatelliteSubId)) {
            plogd("isCarrierRoamingNtnEligible[phoneId=" + phone.getPhoneId() + "]: subscription is not provisioned to use satellite.");
            return false;
        }
        if (!ArrayUtils.contains(getSupportedServicesOnCarrierRoamingNtn(selectedSatelliteSubId), 3)) {
            plogd("isCarrierRoamingNtnEligible[phoneId=" + phone.getPhoneId() + "]: SMS is not supported by carrier");
            return false;
        }
        int carrierRoamingNtnConnectType = getCarrierRoamingNtnConnectType(selectedSatelliteSubId);
        if (carrierRoamingNtnConnectType != 1 && carrierRoamingNtnConnectType != 2) {
            plogd("isCarrierRoamingNtnEligible[phoneId=" + phone.getPhoneId() + "]: not manual  nor hybrid connect. connectType = " + carrierRoamingNtnConnectType);
            return false;
        }
        if (isInCarrierRoamingNbIotNtn(phone) && getCarrierRoamingNtnConnectType(selectedSatelliteSubId) == 0) {
            plogd("isCarrierRoamingNtnEligible[phoneId=" + phone.getPhoneId() + "]: in NTN mode with automatic connection.");
            return false;
        }
        if (this.mOverrideNtnEligibility != null) {
            return true;
        }
        if (SatelliteServiceUtils.isCellularAvailable()) {
            plogd("isCarrierRoamingNtnEligible[phoneId=" + phone.getPhoneId() + "]: cellular is available");
            return false;
        }
        if (!this.mIsWifiConnected.get()) {
            return true;
        }
        plogd("isCarrierRoamingNtnEligible[phoneId=" + phone.getPhoneId() + "]: Wi-Fi is connected");
        return false;
    }

    public boolean isSatelliteServiceSupportedByCarrier(int i, int i2) {
        Iterator<String> it = getSatellitePlmnsForCarrier(i).iterator();
        while (it.hasNext()) {
            if (getSupportedSatelliteServicesForPlmn(i, it.next()).contains(Integer.valueOf(i2))) {
                return true;
            }
        }
        return false;
    }

    public void startPointingUI() {
        plogd("startPointingUI: mNeedsSatellitePointing=" + this.mNeedsSatellitePointing.get() + ", mIsDemoModeEnabled=" + this.mIsDemoModeEnabled.get() + ", mIsEmergency=" + this.mIsEmergency.get());
        if (this.mNeedsSatellitePointing.get()) {
            this.mPointingAppController.startPointingUI(false, this.mIsDemoModeEnabled.get(), this.mIsEmergency.get());
        }
    }

    protected void registerForSatelliteCommunicationAccessStateChanged() {
        if (this.mRegisteredForSatelliteCommunicationAccessStateChanged.get()) {
            if (DEBUG) {
                plogd("registerForSatelliteCommunicationAccessStateChanged: already registered.");
                return;
            }
            return;
        }
        SatelliteManager satelliteManager = (SatelliteManager) this.mContext.getSystemService(SatelliteManager.class);
        if (satelliteManager == null) {
            ploge("registerForSatelliteCommunicationAccessStateChanged: SatelliteManager is null");
            return;
        }
        try {
            satelliteManager.registerForCommunicationAccessStateChanged(new Executor() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda21
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new SatelliteCommunicationAccessStateCallback() { // from class: com.android.internal.telephony.satellite.SatelliteController.22
                public void onAccessAllowedStateChanged(boolean z) {
                    SatelliteController.this.plogd("onAccessStateChanged: isAllowed=" + z);
                    FeatureFlags unused = SatelliteController.this.mFeatureFlags;
                    SatelliteController satelliteController = SatelliteController.this;
                    satelliteController.sendMessage(satelliteController.obtainMessage(88, Boolean.valueOf(z)));
                }

                public void onAccessConfigurationChanged(SatelliteAccessConfiguration satelliteAccessConfiguration) {
                    SatelliteController.this.plogd("onAccessConfigurationChanged: satelliteAccessConfiguration=" + satelliteAccessConfiguration);
                    FeatureFlags unused = SatelliteController.this.mFeatureFlags;
                    SatelliteController satelliteController = SatelliteController.this;
                    satelliteController.sendMessage(satelliteController.obtainMessage(89, satelliteAccessConfiguration));
                }
            });
            this.mRegisteredForSatelliteCommunicationAccessStateChanged.set(true);
        } catch (RuntimeException e) {
            plogd("registerForSatelliteCommunicationAccessStateChanged: satelliteManager.registerForCommunicationAccessStateChanged() failed, e=" + e);
        }
    }

    public void handleSatelliteAccessAllowedStateChanged(boolean z) {
        plogd("handleSatelliteAccessAllowedStateChanged: isAllowed=" + z);
        this.mSatelliteAccessAllowed.set(z);
        evaluateESOSProfilesPrioritization();
        selectBindingSatelliteSubscription(false);
        evaluateCarrierRoamingNtnEligibilityChange();
        handleCarrierRoamingNtnAvailableServicesChanged();
        evaluateDisablingP2pSession();
    }

    private void evaluateDisablingP2pSession() {
        boolean zIsSatelliteAccessAllowedAtCurrentLocation = isSatelliteAccessAllowedAtCurrentLocation();
        boolean zIsSatelliteEnabled = isSatelliteEnabled();
        boolean requestIsEmergency = getRequestIsEmergency();
        plogd("evaluateDisablingP2pSession: isAllowed=" + zIsSatelliteAccessAllowedAtCurrentLocation + " isEmergency=" + requestIsEmergency + " isSatelliteEnabled:" + zIsSatelliteEnabled);
        if (zIsSatelliteAccessAllowedAtCurrentLocation || !zIsSatelliteEnabled || requestIsEmergency) {
            return;
        }
        disableSatelliteSession(requestIsEmergency);
    }

    private void disableSatelliteSession(boolean z) {
        plogd("disableSatelliteSession: isEmergency=" + z);
        requestSatelliteEnabled(false, false, z, new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteController.23
            public void accept(int i) {
                SatelliteController.this.plogd("disableSatelliteSession: requestSatelliteEnabled result=" + i);
            }
        });
    }

    private void handleSatelliteAccessConfigUpdateResult(SatelliteAccessConfiguration satelliteAccessConfiguration) {
        List arrayList;
        List arrayList2;
        plogd("handleSatelliteAccessConfigUpdateResult: satelliteAccessConfig=" + satelliteAccessConfiguration);
        if (satelliteAccessConfiguration != null) {
            arrayList = satelliteAccessConfiguration.getTagIds();
            arrayList2 = satelliteAccessConfiguration.getCarrierIds();
        } else {
            arrayList = new ArrayList();
            arrayList2 = new ArrayList();
        }
        if (!getCurrentLocationCarrierIds().equals(arrayList2) || !getCurrentLocationTagIds().equals(arrayList)) {
            setCurrentLocationCarrierIds(arrayList2);
            setCurrentLocationTagIds(arrayList);
            selectBindingSatelliteSubscription(false);
            sendMessageDelayed(obtainMessage(43), this.mEvaluateEsosProfilesPrioritizationDurationMillis.get());
            return;
        }
        plogd("handleSatelliteAccessConfigUpdateResult: carrierIds and tagIds do not change");
    }

    private void setCurrentLocationTagIds(List list) {
        synchronized (this.mSatelliteAccessConfigLock) {
            this.mCurrentLocationTagIds = list;
        }
    }

    private void setCurrentLocationCarrierIds(List list) {
        synchronized (this.mSatelliteAccessConfigLock) {
            this.mCurrentLocationCarrierIds = list;
        }
    }

    private List getCurrentLocationTagIds() {
        List<Integer> list;
        synchronized (this.mSatelliteAccessConfigLock) {
            list = this.mCurrentLocationTagIds;
        }
        return list;
    }

    private List getCurrentLocationCarrierIds() {
        List<Integer> list;
        synchronized (this.mSatelliteAccessConfigLock) {
            list = this.mCurrentLocationCarrierIds;
        }
        return list;
    }

    private void handleEventSatelliteRegistrationFailure(final int i) {
        plogd("handleEventSatelliteRegistrationFailure: " + i);
        final ArrayList arrayList = new ArrayList();
        this.mSatelliteRegistrationFailureListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda24
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SatelliteController.$r8$lambda$geWLx6HZN_WNS9SGZCk77JnUPco(i, arrayList, (ISatelliteModemStateCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda25
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$handleEventSatelliteRegistrationFailure$39((ISatelliteModemStateCallback) obj);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$geWLx6HZN_WNS9SGZCk77JnUPco(int i, List list, ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        try {
            iSatelliteModemStateCallback.onRegistrationFailure(i);
        } catch (RemoteException e) {
            logd("handleEventSatelliteRegistrationFailure RemoteException: " + e);
            list.add(iSatelliteModemStateCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEventSatelliteRegistrationFailure$39(ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        this.mSatelliteRegistrationFailureListeners.remove(iSatelliteModemStateCallback.asBinder());
    }

    private void handleEventTerrestrialNetworkAvailableChanged(final boolean z) {
        plogd("handleEventTerrestrialNetworkAvailableChanged: " + z);
        final ArrayList arrayList = new ArrayList();
        this.mTerrestrialNetworkAvailableChangedListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda29
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SatelliteController.$r8$lambda$Kn0u0OOf9uSbUD7mbcA4AUHu9Oo(z, arrayList, (ISatelliteModemStateCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda30
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$handleEventTerrestrialNetworkAvailableChanged$41((ISatelliteModemStateCallback) obj);
            }
        });
        if (!z || this.mIsEmergency.get()) {
            return;
        }
        requestSatelliteEnabled(false, false, false, new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteController.24
            public void accept(int i) {
                SatelliteController.this.plogd("handleEventTerrestrialNetworkAvailableChanged: requestSatelliteEnabled result=" + i);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$Kn0u0OOf9uSbUD7mbcA4AUHu9Oo(boolean z, List list, ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        try {
            iSatelliteModemStateCallback.onTerrestrialNetworkAvailableChanged(z);
        } catch (RemoteException e) {
            logd("handleEventTerrestrialNetworkAvailableChanged RemoteException: " + e);
            list.add(iSatelliteModemStateCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEventTerrestrialNetworkAvailableChanged$41(ISatelliteModemStateCallback iSatelliteModemStateCallback) {
        this.mTerrestrialNetworkAvailableChangedListeners.remove(iSatelliteModemStateCallback.asBinder());
    }

    public boolean setSatelliteSubscriberIdListChangedIntentComponent(String str) {
        if (!isMockModemAllowed()) {
            logd("setSatelliteSubscriberIdListChangedIntentComponent: mock modem is not allowed");
            return false;
        }
        logd("setSatelliteSubscriberIdListChangedIntentComponent:" + str);
        boolean zContains = str.contains("/");
        AtomicBoolean atomicBoolean = this.mChangeIntentComponent;
        if (zContains) {
            atomicBoolean.set(true);
            String[] strArrSplit = str.split("/");
            String str2 = strArrSplit[0];
            str2.hashCode();
            if (str2.equals("-c")) {
                this.mConfigSatelliteCarrierRoamingEsosProvisionedClass = strArrSplit[1];
                return true;
            }
            if (str2.equals("-p")) {
                this.mConfigSatelliteGatewayServicePackage = strArrSplit[1];
                return true;
            }
            logd("setSatelliteSubscriberIdListChangedIntentComponent: invalid name ".concat(str));
            return false;
        }
        atomicBoolean.set(false);
        return true;
    }

    public boolean setSatelliteAccessAllowedForSubscriptions(boolean z, String str) {
        if (!isMockModemAllowed()) {
            plogd("setSatelliteAccessAllowedForSubscriptions: mock modem not allowed");
            return false;
        }
        plogd("setSatelliteAccessAllowedForSubscriptions: subIdListStr=" + str + " reset=" + z);
        boolean z2 = true;
        if (z) {
            this.mCtsSatelliteAccessAllowedSubIds = null;
        } else {
            this.mCtsSatelliteAccessAllowedSubIds = new ArrayList();
            if (!TextUtils.isEmpty(str)) {
                for (String str2 : str.split(",")) {
                    try {
                        this.mCtsSatelliteAccessAllowedSubIds.add(Integer.valueOf(Integer.parseInt(str2)));
                    } catch (NumberFormatException unused) {
                        plogd("setSatelliteAccessAllowedForSubscriptions: invalid subIdStr=" + str2);
                        this.mCtsSatelliteAccessAllowedSubIds = null;
                        z2 = false;
                    }
                }
            }
        }
        boolean z3 = this.mSatelliteAccessAllowed.get();
        plogd("setSatelliteAccessAllowedForSubscriptions: isAllowed=" + z3);
        sendMessage(obtainMessage(88, Boolean.valueOf(z3)));
        return z2;
    }

    protected String getConfigSatelliteGatewayServicePackage() {
        if (!this.mChangeIntentComponent.get()) {
            return getStringFromOverlayConfig(R.string.dream_preview_title);
        }
        logd("getConfigSatelliteGatewayServicePackage: " + this.mConfigSatelliteGatewayServicePackage);
        return this.mConfigSatelliteGatewayServicePackage;
    }

    private String getConfigSatelliteCarrierRoamingEsosProvisionedClass() {
        if (!this.mChangeIntentComponent.get()) {
            return getStringFromOverlayConfig(R.string.dlg_ok);
        }
        logd("getConfigSatelliteCarrierRoamingEsosProvisionedClass: " + this.mConfigSatelliteCarrierRoamingEsosProvisionedClass);
        return this.mConfigSatelliteCarrierRoamingEsosProvisionedClass;
    }

    private void registerDefaultSmsSubscriptionChangedBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.DEFAULT_SMS_SUBSCRIPTION_CHANGED");
        this.mContext.registerReceiver(this.mDefaultSmsSubscriptionChangedBroadcastReceiver, intentFilter);
    }

    protected List<DeviceState> getSupportedDeviceStates() {
        return ((DeviceStateManager) this.mContext.getSystemService(DeviceStateManager.class)).getSupportedDeviceStates();
    }

    FeatureFlags getFeatureFlags() {
        return this.mFeatureFlags;
    }

    public boolean isSatelliteDisabled() {
        AtomicBoolean atomicBoolean = this.mIsSatelliteEnabled;
        return (atomicBoolean == null || atomicBoolean.get()) ? false : true;
    }

    private boolean shouldStopWaitForEnableResponseTimer(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        if (requestSatelliteEnabledArgument.enableSatellite) {
            return true;
        }
        return !this.mWaitingForSatelliteModemOff.get();
    }

    public boolean overrideCarrierRoamingNtnEligibilityChanged(boolean z, boolean z2) {
        Log.d("SatelliteController", "overrideCarrierRoamingNtnEligibilityChanged state = " + z + "  resetRequired = " + z2);
        if (z2) {
            this.mOverrideNtnEligibility = null;
            return true;
        }
        AtomicBoolean atomicBoolean = this.mOverrideNtnEligibility;
        if (atomicBoolean == null) {
            this.mOverrideNtnEligibility = new AtomicBoolean(z);
        } else {
            atomicBoolean.set(z);
        }
        if (getSatellitePhone() == null) {
            return true;
        }
        updateLastNotifiedNtnEligibilityAndNotify(z);
        return true;
    }

    private void overrideSatelliteCapabilitiesIfApplicable() {
        int selectedSatelliteSubId = getSelectedSatelliteSubId();
        PersistableBundle persistableBundle = getPersistableBundle(selectedSatelliteSubId);
        if (persistableBundle.containsKey("satellite_sos_max_datagram_size_bytes_int")) {
            int i = persistableBundle.getInt("satellite_sos_max_datagram_size_bytes_int");
            SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(selectedSatelliteSubId);
            if (subscriptionInfo == null || subscriptionInfo.isOnlyNonTerrestrialNetwork()) {
                return;
            }
            synchronized (this.mSatelliteCapabilitiesLock) {
                this.mSatelliteCapabilities.setMaxBytesPerOutgoingDatagram(i);
            }
        }
    }

    public int getNtnOnlySubscriptionId() {
        int iOrElse = this.mSubscriptionManagerService.getAllSubInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag()).stream().filter(new Predicate() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((SubscriptionInfo) obj).isOnlyNonTerrestrialNetwork();
            }
        }).mapToInt(new SatelliteController$$ExternalSyntheticLambda1()).findFirst().orElse(-1);
        logd("getNtnOnlySubscriptionId: subId=" + iOrElse);
        return iOrElse;
    }

    private List getNtnOnlySatelliteSubscriberInfoList(Consumer consumer) {
        SatelliteSubscriberInfo ntnOnlySatelliteSubscriberInfo = getNtnOnlySatelliteSubscriberInfo();
        if (ntnOnlySatelliteSubscriberInfo == null) {
            consumer.accept(11);
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(ntnOnlySatelliteSubscriberInfo);
        return arrayList;
    }

    private SatelliteSubscriberInfo getNtnOnlySatelliteSubscriberInfo() {
        int ntnOnlySubscriptionId = getNtnOnlySubscriptionId();
        if (ntnOnlySubscriptionId == -1) {
            logw("getNtnOnlySatelliteSubscriberInfo: no ntn only subscription found");
            return null;
        }
        SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(ntnOnlySubscriptionId);
        if (subscriptionInfo == null) {
            logw("getNtnOnlySatelliteSubscriberInfo: no subscription info found for subId=" + ntnOnlySubscriptionId);
            return null;
        }
        return getSatelliteSubscriberInfo(subscriptionInfo);
    }

    private SatelliteSubscriberInfo getSatelliteSubscriberInfo(SubscriptionInfo subscriptionInfo) {
        Pair subscriberIdAndType = getSubscriberIdAndType(subscriptionInfo);
        String str = (String) subscriberIdAndType.first;
        int carrierId = subscriptionInfo.getCarrierId();
        String string = getConfigForSubId(subscriptionInfo.getSubscriptionId()).getString("satellite_nidd_apn_name_string", "");
        logd("getSatelliteSubscriberInfo: subInfo: " + subscriptionInfo + ", subscriberId:" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, str) + " , carrierId=" + carrierId + " , apn=" + string);
        if (str.isEmpty()) {
            logw("getSatelliteSubscriberInfo: not a satellite subscription.");
            return null;
        }
        return new SatelliteSubscriberInfo.Builder().setSubscriberId(str).setCarrierId(carrierId).setNiddApn(string).setSubscriptionId(subscriptionInfo.getSubscriptionId()).setSubscriberIdType(((Integer) subscriberIdAndType.second).intValue()).build();
    }

    private void updateAndNotifyChangesInCarrierRoamingNtnAvailableServices(int i, Map map) {
        int[] supportedServicesOnCarrierRoamingNtn = getSupportedServicesOnCarrierRoamingNtn(i);
        this.mEntitlementServiceTypeMapPerCarrier.put(Integer.valueOf(i), map);
        int[] supportedServicesOnCarrierRoamingNtn2 = getSupportedServicesOnCarrierRoamingNtn(i);
        if (supportedServicesOnCarrierRoamingNtn.length > 0 && Arrays.equals(supportedServicesOnCarrierRoamingNtn, supportedServicesOnCarrierRoamingNtn2)) {
            plogd("No change in Entitlement service support data");
        } else {
            updateLastNotifiedNtnAvailableServicesAndNotify(i);
            evaluateCarrierRoamingNtnEligibilityChange();
        }
    }

    private void handleCarrierRoamingNtnAvailableServicesChanged() {
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (activeSubIdList == null) {
            plogd("handleCarrierRoamingNtnAvailableServicesChanged: activeSubIds is null.");
            return;
        }
        plogd("handleCarrierRoamingNtnAvailableServicesChanged: activeSubIds size=" + activeSubIdList.length);
        for (int i : activeSubIdList) {
            handleCarrierRoamingNtnAvailableServicesChanged(i);
        }
    }

    protected void handleCarrierRoamingNtnAvailableServicesChanged(int i) {
        plogd("handleCarrierRoamingNtnAvailableServicesChanged: subId=" + i);
        updateLastNotifiedNtnAvailableServicesAndNotify(i);
        evaluateCarrierRoamingNtnEligibilityChange();
    }

    private void updateLastNotifiedNtnAvailableServicesAndNotify(int i) {
        Phone phone = SatelliteServiceUtils.getPhone(i);
        if (phone == null) {
            plogd("notifyNtnAvailableServices: phone is null.");
            return;
        }
        plogd("updateLastNotifiedNtnAvailableServicesAndNotify: phoneId= " + phone.getPhoneId());
        phone.notifyCarrierRoamingNtnAvailableServicesChanged(getSupportedServicesOnCarrierRoamingNtn(i));
    }

    private int[] getAvailableServicesWithEntitlementForSubId(int i) {
        Map map = (Map) this.mEntitlementServiceTypeMapPerCarrier.get(Integer.valueOf(i));
        int i2 = 0;
        if (map != null && !map.isEmpty()) {
            HashSet hashSet = new HashSet();
            Iterator it = map.values().iterator();
            while (it.hasNext()) {
                hashSet.addAll((List) it.next());
            }
            int[] iArr = new int[hashSet.size()];
            Iterator it2 = hashSet.iterator();
            while (it2.hasNext()) {
                iArr[i2] = ((Integer) it2.next()).intValue();
                i2++;
            }
            return iArr;
        }
        return new int[0];
    }

    private int[] getSupportedSatelliteServicesFromConfig(int i) {
        Map map = (Map) this.mSatelliteServicesSupportedByCarriersFromConfig.get(Integer.valueOf(i));
        if (map == null || map.isEmpty()) {
            return new int[0];
        }
        HashSet hashSet = new HashSet();
        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            hashSet.addAll((Set) it.next());
        }
        return hashSet.stream().mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray();
    }

    public int[] getSupportedServicesOnCarrierRoamingNtn(int i) {
        if (SubscriptionManager.isValidSubscriptionId(i) && isSatelliteSupportedViaCarrier(i)) {
            int[] satelliteDefaultServicesFromCarrierConfig = new int[0];
            if (isSatelliteEntitlementSupported(i)) {
                satelliteDefaultServicesFromCarrierConfig = getAvailableServicesWithEntitlementForSubId(i);
                plogd("getSupportedServicesOnCarrierRoamingNtn[DataSource=Entitlement]: subId=" + i + " services=" + Arrays.toString(satelliteDefaultServicesFromCarrierConfig));
            }
            if (satelliteDefaultServicesFromCarrierConfig.length == 0) {
                satelliteDefaultServicesFromCarrierConfig = getSupportedSatelliteServicesFromConfig(i);
                plogd("getSupportedServicesOnCarrierRoamingNtn[DataSource=Config]: subId=" + i + " services=" + Arrays.toString(satelliteDefaultServicesFromCarrierConfig));
            }
            if (satelliteDefaultServicesFromCarrierConfig.length == 0) {
                satelliteDefaultServicesFromCarrierConfig = getSatelliteDefaultServicesFromCarrierConfig(i);
                plogd("getSupportedServicesOnCarrierRoamingNtn[DataSource=Default Services]: subId=" + i + " services=" + Arrays.toString(satelliteDefaultServicesFromCarrierConfig));
            }
            return isP2PSmsDisallowedOnCarrierRoamingNtn(i) ? Arrays.stream(satelliteDefaultServicesFromCarrierConfig).filter(new IntPredicate() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda20
                @Override // java.util.function.IntPredicate
                public final boolean test(int i2) {
                    return SatelliteController.$r8$lambda$dre8SzdwfW3Dqx9epZO2VY2P8rk(i2);
                }
            }).toArray() : satelliteDefaultServicesFromCarrierConfig;
        }
        return new int[0];
    }

    public static /* synthetic */ boolean $r8$lambda$dre8SzdwfW3Dqx9epZO2VY2P8rk(int i) {
        return i != 3;
    }

    public SatellitePerPlmnConfiguration getSatellitePerPlmnConfiguration(int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i) || !isSatelliteSupportedViaCarrier(i)) {
            plogd("getSatellitePerPlmnConfiguration: invalid subId or not supported via carrier.");
            return null;
        }
        SatellitePerPlmnConfiguration satellitePerPlmnConfiguration = (SatellitePerPlmnConfiguration) this.mCurrentSatellitePerPlmnConfigurations.getOrDefault(Integer.valueOf(i), null);
        plogd("getSatellitePerPlmnConfiguration: subId=" + i + " return " + satellitePerPlmnConfiguration);
        return satellitePerPlmnConfiguration;
    }

    public boolean isP2PSmsDisallowedOnCarrierRoamingNtn(int i) {
        int carrierRoamingNtnConnectType = getCarrierRoamingNtnConnectType(i);
        if (carrierRoamingNtnConnectType == 1 || carrierRoamingNtnConnectType == 2) {
            plogd("isP2PSmsDisallowedOnCarrierRoamingNtn: manual connect or hybrid. connectType=" + carrierRoamingNtnConnectType);
            if (!isNtnSmsSupportedByMessagesApp() || !isApplicationSupportsP2P(getSatelliteGatewayServicePackageName())) {
                plogd("isP2PSmsDisallowedOnCarrierRoamingNtn: APKs do not supports P2P");
                return true;
            }
            if (!isSubscriptionProvisioned(i)) {
                plogd("isP2PSmsDisallowedOnCarrierRoamingNtn: subId=" + i + " is not provisioned.");
                return true;
            }
        }
        plogd("isP2PSmsDisallowedOnCarrierRoamingNtn: P2P is supported");
        return false;
    }

    private String getSatelliteGatewayServicePackageName() {
        if (!TextUtils.isEmpty(this.mOverriddenSatelliteGatewayServicePackageName)) {
            logd("getSatelliteGatewayServicePackageName: return overridden package name for CTS test " + this.mOverriddenSatelliteGatewayServicePackageName);
            return this.mOverriddenSatelliteGatewayServicePackageName;
        }
        return this.mSatelliteGatewayServicePackageName;
    }

    private int[] getSatelliteDefaultServicesFromCarrierConfig(int i) {
        int[] intArray = getPersistableBundle(i).getIntArray("carrier_roaming_satellite_default_services_int_array");
        if (intArray == null) {
            logd("getSatelliteDefaultServicesFromCarrierConfig: defaultCapabilities is null");
            return new int[0];
        }
        logd("getSatelliteDefaultServicesFromCarrierConfig: subId=" + i + ", return default values " + Arrays.toString(intArray));
        return intArray;
    }

    public boolean isApplicationSupportsP2P(String str) {
        Bundle bundle;
        PackageManager packageManager = this.mContext.getPackageManager();
        new ApplicationInfo();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 128);
            if (applicationInfo == null || (bundle = applicationInfo.metaData) == null) {
                logd("isApplicationSupportsP2P pkgName: " + str + " meta-data info is empty.");
                return false;
            }
            return bundle.getBoolean("android.telephony.METADATA_SATELLITE_MANUAL_CONNECT_P2P_SUPPORT");
        } catch (PackageManager.NameNotFoundException unused) {
            logd("isApplicationSupportsP2P pkgName: " + str + " is not installed.");
            return false;
        }
    }

    public void registerApplicationStateChanged() {
        this.mSatelliteGatewayServicePackageName = getConfigSatelliteGatewayServicePackage();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiver(this.mPackageStateChangedReceiver, intentFilter, 4);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED_INTERNAL");
        this.mContext.registerReceiver(this.mPackageStateChangedReceiver, intentFilter2, 4);
    }

    private void registerLocationServiceStateChanged() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.MODE_CHANGED");
        this.mContext.registerReceiver(this.mLocationServiceStateChangedReceiver, intentFilter);
    }

    private void notifyEnabledStateChanged(boolean z) {
        TelephonyRegistryManager telephonyRegistryManager = (TelephonyRegistryManager) this.mContext.getSystemService(TelephonyRegistryManager.class);
        if (telephonyRegistryManager == null) {
            loge("Telephony registry service is down!");
            return;
        }
        telephonyRegistryManager.notifySatelliteStateChanged(z);
        logd("notifyEnabledStateChanged to " + z);
    }

    private void setNtnSignalStrength(NtnSignalStrength ntnSignalStrength) {
        synchronized (this.mNtnSignalsStrengthLock) {
            this.mNtnSignalStrength = ntnSignalStrength;
        }
    }

    private NtnSignalStrength getNtnSignalStrength() {
        NtnSignalStrength ntnSignalStrength;
        synchronized (this.mNtnSignalsStrengthLock) {
            ntnSignalStrength = this.mNtnSignalStrength;
        }
        return ntnSignalStrength;
    }

    private NtnSignalStrength getCarrierRoamingNtnSignalStrength(Phone phone) {
        NtnSignalStrength ntnSignalStrength = new NtnSignalStrength(0);
        if (isInCarrierRoamingNbIotNtn(phone)) {
            if (!isInConnectedState()) {
                return ntnSignalStrength;
            }
            NtnSignalStrength ntnSignalStrength2 = getNtnSignalStrength();
            plogd("getCarrierRoamingNtnSignalStrength[phoneId=" + phone.getPhoneId() + "]: in carrier roaming nb iot ntn mode.");
            return ntnSignalStrength2;
        }
        if (!isInSatelliteModeForCarrierRoaming(phone)) {
            return ntnSignalStrength;
        }
        ServiceState serviceState = phone.getServiceState();
        if (serviceState.getState() == 1 && serviceState.getDataRegState() == 1) {
            return ntnSignalStrength;
        }
        NtnSignalStrength ntnSignalStrength3 = new NtnSignalStrength(phone.getSignalStrength().getLevel());
        plogd("getCarrierRoamingNtnSignalStrength[phoneId=" + phone.getPhoneId() + "]: is in satellite mode for carrier roaming.");
        return ntnSignalStrength3;
    }

    public boolean isInConnectedState() {
        int i = this.mSatelliteModemState.get();
        if (i == 0 || i == 7 || i == 2 || i == 3) {
            plogd("isInConnectedState: return true");
            return true;
        }
        plogd("isInConnectedState: return false");
        return false;
    }

    protected void updateLastNotifiedCarrierRoamingNtnSignalStrengthAndNotify(Phone phone) {
        if (phone == null) {
            return;
        }
        NtnSignalStrength carrierRoamingNtnSignalStrength = getCarrierRoamingNtnSignalStrength(phone);
        int subId = phone.getSubId();
        NtnSignalStrength ntnSignalStrength = (NtnSignalStrength) this.mLastNotifiedCarrierRoamingNtnSignalStrength.get(Integer.valueOf(subId));
        if (isInSatelliteModeForCarrierRoaming(phone)) {
            plogd("updateLastNotifiedCarrierRoamingNtnSignalStrengthAndNotify(" + subId + ")");
            if (ntnSignalStrength != null) {
                plogd("updateLastNotifiedCarrierRoamingNtnSignalStrengthAndNotify(" + subId + ") lastNotifiedSignalStrength level: " + ntnSignalStrength.getLevel());
            }
            if (carrierRoamingNtnSignalStrength != null) {
                plogd("updateLastNotifiedCarrierRoamingNtnSignalStrengthAndNotify(" + subId + ") currSignalStrength level: " + carrierRoamingNtnSignalStrength.getLevel());
            }
        }
        if (ntnSignalStrength == null || ntnSignalStrength.getLevel() != carrierRoamingNtnSignalStrength.getLevel()) {
            this.mLastNotifiedCarrierRoamingNtnSignalStrength.put(Integer.valueOf(subId), carrierRoamingNtnSignalStrength);
            phone.notifyCarrierRoamingNtnSignalStrengthChanged(carrierRoamingNtnSignalStrength);
        }
    }

    public boolean shouldSendSmsToDatagramDispatcher(Phone phone) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (!isInCarrierRoamingNbIotNtn(phone)) {
                return false;
            }
            if (isDemoModeEnabled()) {
                return false;
            }
            return ArrayUtils.contains(getSupportedServicesOnCarrierRoamingNtn(phone.getSubId()), 3);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean shouldDropSms(Phone phone) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (!isInCarrierRoamingNbIotNtn(phone)) {
                return false;
            }
            return !ArrayUtils.contains(getSupportedServicesOnCarrierRoamingNtn(phone.getSubId()), 3);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private boolean isWaitingForSatelliteModemOff() {
        return this.mWaitingForSatelliteModemOff.get();
    }

    protected void setIsSatelliteSupported(boolean z) {
        AtomicBoolean atomicBoolean = this.mIsSatelliteSupported;
        if (atomicBoolean == null) {
            this.mIsSatelliteSupported = new AtomicBoolean(z);
        } else {
            atomicBoolean.set(z);
        }
    }

    private Boolean getIsSatelliteSupported() {
        AtomicBoolean atomicBoolean = this.mIsSatelliteSupported;
        if (atomicBoolean == null) {
            return null;
        }
        return Boolean.valueOf(atomicBoolean.get());
    }

    private boolean isWaitingForDisableSatelliteModemResponse() {
        return this.mWaitingForDisableSatelliteModemResponse.get();
    }

    private boolean isSatelliteAccessAllowedAtCurrentLocation() {
        return this.mSatelliteAccessAllowed.get();
    }

    private void setIsSatelliteEnabled(boolean z) {
        AtomicBoolean atomicBoolean = this.mIsSatelliteEnabled;
        if (atomicBoolean == null) {
            this.mIsSatelliteEnabled = new AtomicBoolean(z);
        } else {
            atomicBoolean.set(z);
        }
    }

    private Boolean getIsSatelliteEnabled() {
        AtomicBoolean atomicBoolean = this.mIsSatelliteEnabled;
        if (atomicBoolean == null) {
            return null;
        }
        return Boolean.valueOf(atomicBoolean.get());
    }

    private void setSatelliteCapabilities(SatelliteCapabilities satelliteCapabilities) {
        synchronized (this.mSatelliteCapabilitiesLock) {
            this.mSatelliteCapabilities = satelliteCapabilities;
        }
    }

    private SatelliteCapabilities getSatelliteCapabilities() {
        SatelliteCapabilities satelliteCapabilities;
        synchronized (this.mSatelliteCapabilitiesLock) {
            satelliteCapabilities = this.mSatelliteCapabilities;
        }
        return satelliteCapabilities;
    }

    private void setBTEnabledState(boolean z) {
        this.mBTStateEnabled.set(z);
    }

    private boolean getBTEnabledState() {
        return this.mBTStateEnabled.get();
    }

    private void setNfcEnabledState(boolean z) {
        this.mNfcStateEnabled.set(z);
    }

    private boolean getNfcEnabledState() {
        return this.mNfcStateEnabled.get();
    }

    private void setUwbEnabledState(boolean z) {
        this.mUwbStateEnabled.set(z);
    }

    private boolean getUwbEnabledState() {
        return this.mUwbStateEnabled.get();
    }

    private void setWifiEnabledState(boolean z) {
        this.mWifiStateEnabled.set(z);
    }

    private boolean getWifiEnabledState() {
        return this.mWifiStateEnabled.get();
    }

    private void handleCarrierRoamingConfigVersionReport() {
        logd("handleCarrierRoamingConfigVersionReport");
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (activeSubIdList != null && activeSubIdList.length > 0) {
            for (int i : activeSubIdList) {
                SatelliteConfig satelliteConfig = getSatelliteConfig();
                if (satelliteConfig != null) {
                    this.mControllerMetricsStats.reportCurrentVersionOfCarrierRoamingSatelliteConfig(SatelliteServiceUtils.getCarrierIdFromSubscription(i), satelliteConfig.getSatelliteConfigDataVersion(), getSupportedConnectTypeMetrics(i));
                } else {
                    loge("handleCarrierRoamingConfigVersionReport: no satellite config by configupdater");
                }
            }
            return;
        }
        loge("handleMaxAllowedDataMetricsReport: no active subId");
    }

    private void handleMaxAllowedDataMetricsReport() {
        logd("handleMaxAllowedDataMetricsReport");
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (activeSubIdList != null && activeSubIdList.length > 0) {
            for (int i : activeSubIdList) {
                this.mControllerMetricsStats.reportCurrentMaxAllowedDataMode(SatelliteServiceUtils.getCarrierIdFromSubscription(i), getMaxAllowedDataMode(), getSupportedConnectTypeMetrics(i));
            }
            return;
        }
        loge("handleMaxAllowedDataMetricsReport: no active subId");
    }

    private void handleEntireEntitlementMetricReport() {
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (activeSubIdList != null && activeSubIdList.length > 0) {
            for (int i : activeSubIdList) {
                boolean zBooleanValue = ((Boolean) this.mSatelliteEntitlementStatusPerCarrier.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda7
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return Boolean.FALSE;
                    }
                })).booleanValue();
                plogd("handleEntitlementMetricReport: subId=" + i + ", isSubEntitled=" + zBooleanValue);
                reportEntitlementStatusAndEligibility(i, zBooleanValue);
            }
            return;
        }
        loge("handleEntireEntitlementMetricReport: no active subId");
    }

    private void handleIndividualEntitlementMetricReport(int i, boolean z) {
        this.mSatelliteEntitlementStatusPerCarrier.put(Integer.valueOf(i), Boolean.valueOf(z));
        reportEntitlementStatusAndEligibility(i, z);
    }

    private void reportEntitlementStatusAndEligibility(int i, boolean z) {
        this.mCarrierRoamingSatelliteControllerStats.reportIsDeviceEntitled(i, z);
        updateAndReportEligibilitySource(i);
    }

    private void handleEntireProvisionMetricReport() {
        logd("handleEntireProvisionMetricReport:");
        HashMap map = new HashMap();
        for (SubscriptionInfo subscriptionInfo : this.mSubscriptionManagerService.getAllSubInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag())) {
            int subscriptionId = subscriptionInfo.getSubscriptionId();
            boolean zIsOnlyNonTerrestrialNetwork = subscriptionInfo.isOnlyNonTerrestrialNetwork();
            boolean zIsActive = subscriptionInfo.isActive();
            if (zIsOnlyNonTerrestrialNetwork || zIsActive) {
                int carrierIdFromSubscription = SatelliteServiceUtils.getCarrierIdFromSubscription(subscriptionId);
                if (carrierIdFromSubscription == -1 && !zIsOnlyNonTerrestrialNetwork) {
                    plogd("handleEntireProvisionMetricReport: neither valid carrierId nor NTN-only, subId=" + subscriptionId + ". Skipping.");
                } else {
                    ((CarrierReportInfo) map.computeIfAbsent(Integer.valueOf(carrierIdFromSubscription), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda4
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return SatelliteController.m1571$r8$lambda$fGDG9gvH45bW66OvOWfbIpcQiw((Integer) obj);
                        }
                    })).aggregate(this.mProvisionedSubscriberId.getOrDefault((String) getSubscriberIdAndType(subscriptionInfo).first, Boolean.FALSE).booleanValue(), zIsOnlyNonTerrestrialNetwork, getSupportedConnectTypeMetrics(subscriptionId));
                }
            } else {
                plogd("handleEntireProvisionMetricReport: subId=" + subscriptionId + " is neither NTN-only nor active. Skipping.");
            }
        }
        if (map.isEmpty()) {
            plogd("handleEntireProvisionMetricReport: No reportable data found");
            return;
        }
        plogd("handleEntireProvisionMetricReport: Reporting final aggregated status for " + map.size() + " carrier(s).");
        for (Map.Entry entry : map.entrySet()) {
            int iIntValue = ((Integer) entry.getKey()).intValue();
            CarrierReportInfo carrierReportInfo = (CarrierReportInfo) entry.getValue();
            plogd("handleEntireProvisionMetricReport: Final report for carrierId=" + iIntValue + ", isProvisioned=" + carrierReportInfo.mIsAnySubProvisioned + ", isNtnOnlyCarrier=" + carrierReportInfo.mIsNtnOnlyCarrier);
            this.mControllerMetricsStats.setIsProvisioned(iIntValue, carrierReportInfo.mIsAnySubProvisioned, carrierReportInfo.mIsNtnOnlyCarrier, carrierReportInfo.mSupportedConnectionMode);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$fGDG9gvH-45bW66OvOWfbIpcQiw, reason: not valid java name */
    public static /* synthetic */ CarrierReportInfo m1571$r8$lambda$fGDG9gvH45bW66OvOWfbIpcQiw(Integer num) {
        return new CarrierReportInfo();
    }

    private void handleEntireEligibilityMetricReport() {
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (activeSubIdList.length > 0) {
            for (int i : activeSubIdList) {
                updateAndReportEligibilitySource(i);
            }
            return;
        }
        loge("handleEntireEligibilityMetricReport: no active subId");
    }

    private void updateAndReportEligibilitySource(int i) {
        int i2;
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            plogw("updateAndReportEligibilitySource: Invalid subId=" + i);
            return;
        }
        boolean zIsSatelliteSupportedViaCarrier = isSatelliteSupportedViaCarrier(i);
        boolean zIsSatelliteEntitlementSupported = isSatelliteEntitlementSupported(i);
        boolean zEquals = Boolean.TRUE.equals(isSatelliteEntitlementSupportedViaConfigupdater(i));
        if (!zIsSatelliteSupportedViaCarrier) {
            i2 = 0;
        } else if (zIsSatelliteEntitlementSupported) {
            i2 = 1;
        } else {
            i2 = zEquals ? 3 : 2;
        }
        this.mSatelliteEligibilitySource.put(Integer.valueOf(i), Integer.valueOf(i2));
        this.mCarrierRoamingSatelliteControllerStats.reportDeviceEligibilitySource(i, zIsSatelliteSupportedViaCarrier, i2);
        plogd("updateEligibilitySource: subId=" + i + ", attachSupported=" + zIsSatelliteSupportedViaCarrier + ", entitlementSupported=" + zIsSatelliteEntitlementSupported + ", reportedSource=" + i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class CarrierReportInfo {
        boolean mIsAnySubProvisioned;
        boolean mIsNtnOnlyCarrier;
        int mSupportedConnectionMode;

        private CarrierReportInfo() {
            this.mIsAnySubProvisioned = false;
            this.mIsNtnOnlyCarrier = false;
            this.mSupportedConnectionMode = 0;
        }

        void aggregate(boolean z, boolean z2, int i) {
            if (z) {
                this.mIsAnySubProvisioned = true;
            }
            this.mIsNtnOnlyCarrier = z2;
            this.mSupportedConnectionMode = i;
        }
    }

    private void scheduleRegularMetricReportTimer() {
        AlarmManager alarmManager = this.mAlarmManager;
        if (alarmManager == null) {
            plogd("scheduleRegularMetricReportTimer: AlarmManager is null");
        } else {
            alarmManager.cancel(this.mRegularMetricReportAlarmListener);
            this.mAlarmManager.setExact(2, getElapsedRealtime() + REGULAR_METRIC_REPORTING_INTERVAL_MILLIS, "SatelliteController", new HandlerExecutor(this), new WorkSource(), this.mRegularMetricReportAlarmListener);
        }
    }

    public void setAlarmManager(AlarmManager alarmManager) {
        this.mAlarmManager = alarmManager;
    }

    public int getSatelliteDataPlanForPlmn(int i, String str) {
        if (str == null) {
            return 0;
        }
        Map map = (Map) this.mEntitlementDataPlanMapPerCarrier.get(Integer.valueOf(i));
        logd("data plan available for sub id:" + map);
        if (map == null || !map.containsKey(str)) {
            return 0;
        }
        return ((Integer) map.get(str)).intValue();
    }

    private boolean isSatelliteDataServicesAllowed(int i, String str) {
        return getSupportedSatelliteServicesForPlmn(i, str).stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda19
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SatelliteController.$r8$lambda$27nSB_e62fy9KXbsLphXCTPDQ6o((Integer) obj);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$27nSB_e62fy9KXbsLphXCTPDQ6o(Integer num) {
        return num.intValue() == 2;
    }

    protected boolean isLocationServiceEnabled() {
        LocationManager locationManager = (LocationManager) this.mContext.createAttributionContext("telephony").getSystemService(LocationManager.class);
        if (locationManager != null) {
            return locationManager.isLocationEnabled();
        }
        return true;
    }

    public int getSatelliteDataServicePolicyForPlmn(int i, String str) {
        plogd("getSatelliteDataServicePolicyForPlmn: subId=" + i + " plmn=" + str);
        int maxAllowedDataMode = getMaxAllowedDataMode();
        StringBuilder sb = new StringBuilder("getSatelliteDataServicePolicyForPlmn: maxAllowedDataMode=");
        sb.append(maxAllowedDataMode);
        plogd(sb.toString());
        if (maxAllowedDataMode == 0) {
            plogd("maxAllowedDataMode is 0, the least possible value. Data service policy cannot go below that. Therefore, returning this");
            return maxAllowedDataMode;
        }
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            Map map = isSatelliteEntitlementSupported(i) ? (Map) this.mEntitlementDataServicePolicyMapPerCarrier.get(Integer.valueOf(i)) : null;
            plogd("getSatelliteDataServicePolicyForPlmn: dataServicePolicy=" + map);
            if (map != null && !map.isEmpty()) {
                if (!TextUtils.isEmpty(str) && map.containsKey(str)) {
                    plogd("getSatelliteDataServicePolicyForPlmn: return policy using dataServicePolicy map");
                    if (((Integer) map.get(str)).intValue() > maxAllowedDataMode) {
                        plogd("getSatelliteDataServicePolicyForPlmn: dataServicePolicy shouldn't be bigger than maxAllowedDataMode: " + maxAllowedDataMode);
                        return maxAllowedDataMode;
                    }
                    plogd("getSatelliteDataServicePolicyForPlmn: return policy using dataServicePolicy map: " + map.get(str));
                    return ((Integer) map.get(str)).intValue();
                }
                if (TextUtils.isEmpty(str)) {
                    Iterator it = map.keySet().iterator();
                    int i2 = 0;
                    while (it.hasNext()) {
                        int iIntValue = ((Integer) map.get((String) it.next())).intValue();
                        if (iIntValue > i2) {
                            i2 = iIntValue;
                        }
                    }
                    if (i2 > 0) {
                        plogd("getSatelliteDataServicePolicyForPlmn: preferredPolicy=" + i2);
                        if (i2 > maxAllowedDataMode) {
                            plogd("getSatelliteDataServicePolicyForPlmn: preferredPolicy shouldn't be bigger than maxAllowedDataMode: " + maxAllowedDataMode);
                            return maxAllowedDataMode;
                        }
                        plogd("getSatelliteDataServicePolicyForPlmn: returning preferredPolicy=" + i2);
                        return i2;
                    }
                }
            }
            if (isSatelliteDataServicesAllowed(i, str)) {
                plogd("getSatelliteDataServicePolicyForPlmn: return data support mode from config");
                int carrierSatelliteDataSupportedModeFromConfig = getCarrierSatelliteDataSupportedModeFromConfig(i);
                if (carrierSatelliteDataSupportedModeFromConfig > maxAllowedDataMode) {
                    plogd("getSatelliteDataServicePolicyForPlmn: carrierSatelliteDataSupportedMode shouldn't be bigger than maxAllowedDataMode: " + maxAllowedDataMode);
                    return maxAllowedDataMode;
                }
                plogd("getSatelliteDataServicePolicyForPlmn: return data support mode from config: " + carrierSatelliteDataSupportedModeFromConfig);
                return carrierSatelliteDataSupportedModeFromConfig;
            }
        }
        plogd("getSatelliteDataServicePolicyForPlmn: return data support only restricted");
        return 0;
    }

    public int getSatelliteVoiceServicePolicyForPlmn(int i, String str) {
        if (str == null) {
            return 0;
        }
        Map map = (Map) this.mEntitlementVoiceServicePolicyMapPerCarrier.get(Integer.valueOf(i));
        logd("voice policy available for sub id:" + map);
        if (map == null || !map.containsKey(str) || str.isEmpty()) {
            return 0;
        }
        return ((Integer) map.get(str)).intValue();
    }

    public List<String> getSatelliteDataOptimizedApps(int i) {
        return this.mSatelliteOptimizedApplicationsTracker.getSatelliteOptimizedApplications(i);
    }

    public int getSatelliteDataSupportMode(int i) {
        return getSatelliteDataServicePolicyForPlmn(i, "");
    }

    private void handleEventScreenStateChanged(AsyncResult asyncResult) {
        if (asyncResult == null) {
            ploge("handleEventScreenStateChanged: asyncResult is null");
            return;
        }
        final boolean zBooleanValue = ((Boolean) asyncResult.result).booleanValue();
        plogd("handleEventScreenStateChanged: " + zBooleanValue);
        this.mCarrierRoamingSatelliteSessionStatsMap.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda33
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((CarrierRoamingSatelliteSessionStats) obj).onScreenStateChanged(zBooleanValue);
            }
        });
    }

    private void handleEventWifiConnectivityStateChanged(final boolean z) {
        this.mCarrierRoamingSatelliteSessionStatsMap.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((CarrierRoamingSatelliteSessionStats) obj).onWifiConnectivityStateChanged(z);
            }
        });
    }

    private void handleEventChargingStateChangedEvent(AsyncResult asyncResult) {
        if (asyncResult == null) {
            ploge("handleEventChargingStateChangedEvent: asyncResult is null");
            return;
        }
        boolean zBooleanValue = ((Boolean) asyncResult.result).booleanValue();
        plogd("handleEventChargingStateChangedEvent: " + zBooleanValue);
        if (zBooleanValue) {
            this.mCarrierRoamingSatelliteSessionStatsMap.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.SatelliteController$$ExternalSyntheticLambda37
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((CarrierRoamingSatelliteSessionStats) obj).setWasChargingDuringSession();
                }
            });
        }
    }

    protected boolean isScreenOn() {
        if (this.mDisplayManager == null) {
            this.mDisplayManager = (DisplayManager) this.mContext.getSystemService(DisplayManager.class);
        }
        Display[] displays = this.mDisplayManager.getDisplays();
        if (displays != null) {
            for (Display display : displays) {
                if (display.getState() == 2) {
                    plogd("isScreenOn: Screen on for display=" + display);
                    return true;
                }
            }
            plogd("isScreenOn: Screens all off");
            return false;
        }
        plogd("No displays found");
        return false;
    }

    public PlmnSatelliteConfig getPlmnSatelliteConfig(int i, String str) {
        return new PlmnSatelliteConfig(new HashSet(getSupportedSatelliteServicesForPlmn(i, str)));
    }

    public boolean setSatelliteIgnorePlmnListFromStorage(boolean z) {
        plogd("setSatelliteIgnorePlmnListFromStorage - " + z);
        this.mIgnorePlmnListFromStorage.set(z);
        return true;
    }

    private void setSatelliteEnabledRequest(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        synchronized (this.mSatelliteEnabledRequestLock) {
            this.mSatelliteEnabledRequest = requestSatelliteEnabledArgument;
        }
    }

    private RequestSatelliteEnabledArgument getSatelliteEnabledRequest() {
        RequestSatelliteEnabledArgument requestSatelliteEnabledArgument;
        synchronized (this.mSatelliteEnabledRequestLock) {
            requestSatelliteEnabledArgument = this.mSatelliteEnabledRequest;
        }
        return requestSatelliteEnabledArgument;
    }

    private void setSatelliteDisabledRequest(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        synchronized (this.mSatelliteEnabledRequestLock) {
            this.mSatelliteDisabledRequest = requestSatelliteEnabledArgument;
        }
    }

    private RequestSatelliteEnabledArgument getSatelliteDisabledRequest() {
        RequestSatelliteEnabledArgument requestSatelliteEnabledArgument;
        synchronized (this.mSatelliteEnabledRequestLock) {
            requestSatelliteEnabledArgument = this.mSatelliteDisabledRequest;
        }
        return requestSatelliteEnabledArgument;
    }

    private void setSatelliteEnableAttributesUpdateRequest(RequestSatelliteEnabledArgument requestSatelliteEnabledArgument) {
        synchronized (this.mSatelliteEnabledRequestLock) {
            this.mSatelliteEnableAttributesUpdateRequest = requestSatelliteEnabledArgument;
        }
    }

    private RequestSatelliteEnabledArgument getSatelliteEnableAttributesUpdateRequest() {
        RequestSatelliteEnabledArgument requestSatelliteEnabledArgument;
        synchronized (this.mSatelliteEnabledRequestLock) {
            requestSatelliteEnabledArgument = this.mSatelliteEnableAttributesUpdateRequest;
        }
        return requestSatelliteEnabledArgument;
    }

    private void setSubsInfoListPerPriority(TreeMap treeMap) {
        synchronized (this.mSatelliteTokenProvisionedLock) {
            this.mSubsInfoListPerPriority = treeMap;
        }
    }

    private TreeMap getSubsInfoListPerPriority() {
        TreeMap<Integer, List<SubscriptionInfo>> treeMap;
        synchronized (this.mSatelliteTokenProvisionedLock) {
            treeMap = this.mSubsInfoListPerPriority;
        }
        return treeMap;
    }

    private void setLastEvaluatedSubscriberProvisionStatus(List list) {
        synchronized (this.mSatelliteTokenProvisionedLock) {
            this.mLastEvaluatedSubscriberProvisionStatus = list;
        }
    }

    private List getLastEvaluatedSubscriberProvisionStatus() {
        List list;
        synchronized (this.mSatelliteTokenProvisionedLock) {
            list = this.mLastEvaluatedSubscriberProvisionStatus;
        }
        return list;
    }

    private void setLastConfiguredIccId(String str) {
        synchronized (this.mSatelliteTokenProvisionedLock) {
            this.mLastConfiguredIccId = str;
        }
    }

    private String getLastConfiguredIccId() {
        String str;
        synchronized (this.mSatelliteTokenProvisionedLock) {
            str = this.mLastConfiguredIccId;
        }
        return str;
    }

    public boolean isWifiConnected() {
        return this.mIsWifiConnected.get();
    }

    public boolean isDeviceEntitledForSubscription(int i) {
        if (!isSatelliteSupportedViaCarrier(i)) {
            logd("isDeviceEntitledForSubscription: subId=" + i + ", satellite attach not supported, returning false");
            return false;
        }
        if (isSatelliteEntitlementSupported(i)) {
            boolean zBooleanValue = ((Boolean) this.mSatelliteEntitlementStatusPerCarrier.getOrDefault(Integer.valueOf(i), Boolean.FALSE)).booleanValue();
            logd("isDeviceEntitledForSubscription: subId=" + i + ", entitlement supported, returning map status=" + zBooleanValue);
            return zBooleanValue;
        }
        logd("isDeviceEntitledForSubscription: subId=" + i + ", entitlement not required, returning true");
        return true;
    }

    public int getSatelliteEligibilitySource(int i) {
        return ((Integer) this.mSatelliteEligibilitySource.getOrDefault(Integer.valueOf(i), 0)).intValue();
    }

    private void handleRequestPointingUiAppLaunchIntent(PointingUiAppLaunchIntentAttributes pointingUiAppLaunchIntentAttributes, ResultReceiver resultReceiver) {
        plogd("handleRequestPointingUiAppLaunchIntent: launchIntentAttributes=" + pointingUiAppLaunchIntentAttributes);
        PendingIntent pendingIntentCreatePointingUiAppPendingIntent = this.mPointingAppController.createPointingUiAppPendingIntent(pointingUiAppLaunchIntentAttributes);
        Bundle bundle = new Bundle();
        if (pendingIntentCreatePointingUiAppPendingIntent != null) {
            bundle.putParcelable("pointing_ui_app_launch_intent", pendingIntentCreatePointingUiAppPendingIntent);
            resultReceiver.send(0, bundle);
        } else {
            resultReceiver.send(9, null);
        }
    }

    protected String getCarrierRoamingSatelliteEmergencyMessagingRedirectionDestination(int i) {
        String string = getConfigForSubId(i).getString("carrier_roaming_satellite_emergency_messaging_redirection_destination_string", "");
        plogd("getCarrierRoamingSatelliteEmergencyMessagingRedirectionDestination: destination=" + string);
        return string;
    }

    private boolean isSatelliteEmergencyMessagingProviderSupportedInCurrentRegion(int i) {
        boolean z = getCarrierRoamingSatelliteEmergencyMessagingProviderForCurrentRegion(i) != 1;
        plogd("isSatelliteEmergencyMessagingProviderSupportedInCurrentRegion: supported=" + z);
        return z;
    }

    protected int getCarrierRoamingSatelliteEmergencyMessagingProviderForCurrentRegion(int i) {
        SatellitePerPlmnConfiguration satellitePerPlmnConfiguration = getSatellitePerPlmnConfiguration(i);
        if (satellitePerPlmnConfiguration == null) {
            plogd("getCarrierRoamingSatelliteEmergencyMessagingProvider: perPlmnConfig is null");
            return 0;
        }
        String str = satellitePerPlmnConfiguration.plmn;
        if (TextUtils.isEmpty(str) || str.length() < 3) {
            plogd("getCarrierRoamingSatelliteEmergencyMessagingProvider: plmn is empty or too short. plmn=" + str);
            return 0;
        }
        String strSubstring = str.substring(0, 3);
        int i2 = getConfigForSubId(i).getPersistableBundle("carrier_roaming_satellite_emergency_messaging_provider_per_country_bundle").getInt(strSubstring, 0);
        plogd("getCarrierRoamingSatelliteEmergencyMessagingProvider: mcc=" + strSubstring + ", provider=" + i2);
        return i2;
    }

    public void requestEnableSatelliteForCarrier(int i, boolean z, int i2, IIntegerConsumer iIntegerConsumer) {
        plogd("requestEnableSatelliteForCarrier: subId=" + i + ", enable=" + z + ", reason=" + i2);
        if (z) {
            plogd("requestEnableSatelliteForCarrier: enabling satellite for carrier, removing restriction: " + i2 + " for subId=" + i);
            removeAttachRestrictionForCarrier(i, i2, iIntegerConsumer);
            return;
        }
        plogd("requestEnableSatelliteForCarrier: disabling satellite for carrier, adding restriction: " + i2 + " for subId=" + i);
        addAttachRestrictionForCarrier(i, i2, iIntegerConsumer);
    }

    public void onNonEmergencyDialerDialogDisplayed(Phone phone) {
        if (phone == null) {
            return;
        }
        int subId = phone.getSubId();
        CarrierRoamingSatelliteSessionStats.getInstance(subId).onNonEmergencyDialerDialogDisplayed();
        this.mLastNonEmergencyDialerDialogDisplayedTimestamp.put(Integer.valueOf(subId), Long.valueOf(getElapsedRealtime()));
    }

    public void onEmergencyDialerButtonDisplayed(int i) {
        CarrierRoamingSatelliteSessionStats.getInstance(i).onEmergencyDialerButtonDisplayed();
        this.mLastEmergencyDialerButtonDisplayedTimestamp.put(Integer.valueOf(i), Long.valueOf(getElapsedRealtime()));
    }

    public void onAutoConnectSatelliteNotificationDisplayed(int i) {
        CarrierRoamingSatelliteSessionStats.getInstance(i).onSatelliteNotificationDisplayed();
        this.mLastAutoConnectSatelliteNotificationDisplayedTimestamp.put(Integer.valueOf(i), Long.valueOf(getElapsedRealtime()));
    }

    public int getSatelliteMessageTrigger(Phone phone) {
        if (phone == null) {
            return 0;
        }
        int subId = phone.getSubId();
        long elapsedRealtime = getElapsedRealtime();
        Long l = (Long) this.mLastEmergencyDialerButtonDisplayedTimestamp.get(Integer.valueOf(subId));
        if (l != null && elapsedRealtime - l.longValue() <= SATELLITE_MESSAGE_TRIGGER_WINDOW_MS) {
            return 3;
        }
        Long l2 = (Long) this.mLastNonEmergencyDialerDialogDisplayedTimestamp.get(Integer.valueOf(subId));
        if (l2 != null && elapsedRealtime - l2.longValue() <= SATELLITE_MESSAGE_TRIGGER_WINDOW_MS) {
            return 2;
        }
        Long l3 = (Long) this.mLastAutoConnectSatelliteNotificationDisplayedTimestamp.get(Integer.valueOf(subId));
        return (l3 == null || elapsedRealtime - l3.longValue() > SATELLITE_MESSAGE_TRIGGER_WINDOW_MS) ? 1 : 4;
    }
}
