package com.android.phone.satellite.accesscontrol;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.FileUtils;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.telecom.TelecomManager;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.PersistentLogger;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.satellite.EarfcnRange;
import android.telephony.satellite.ISatelliteCommunicationAccessStateCallback;
import android.telephony.satellite.ISatelliteDisallowedReasonsCallback;
import android.telephony.satellite.ISatelliteProvisionStateCallback;
import android.telephony.satellite.SatelliteAccessConfiguration;
import android.telephony.satellite.SatelliteInfo;
import android.telephony.satellite.SystemSelectionSpecifier;
import android.text.TextUtils;
import android.util.IntArray;
import android.util.Log;
import android.util.Pair;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.IBooleanConsumer;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SmsApplication;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.TelephonyCountryDetector;
import com.android.internal.telephony.satellite.SatelliteConfig;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.satellite.SatelliteServiceUtils;
import com.android.internal.telephony.satellite.metrics.AccessControllerMetricsStats;
import com.android.internal.telephony.satellite.metrics.ConfigUpdaterMetricsStats;
import com.android.internal.telephony.satellite.metrics.ControllerMetricsStats;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.phone.CarrierConfigLoader$$ExternalSyntheticLambda0;
import com.android.phone.PhoneGlobals;
import com.ot.pubsub.g.f;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteAccessController extends Handler {
    private static SatelliteAccessController sInstance;
    private final AccessControllerMetricsStats mAccessControllerMetricsStats;
    private final Map mCachedAccessRestrictionMap;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private final CarrierConfigManager mCarrierConfigManager;
    private final List mCheckingAllowedStateRequests;
    private final ConfigUpdaterMetricsStats mConfigUpdaterMetricsStats;
    private final Context mContext;
    private final ControllerMetricsStats mControllerMetricsStats;
    private final TelephonyCountryDetector mCountryDetector;
    private Path mCtsSatS2FilePath;
    private Path mCtsSatelliteAccessConfigurationFilePath;
    protected AtomicBoolean mCurrentSatelliteAllowedState;
    protected final BroadcastReceiver mDefaultSmsAppChangedBroadcastReceiver;
    private final FeatureFlags mFeatureFlags;
    private Location mFreshLastKnownLocation;
    protected ResultReceiver mHandlerForSatelliteAllowedResult;
    private final ISatelliteProvisionStateCallback mInternalSatelliteProvisionStateCallback;
    private final ResultReceiver mInternalSatelliteProvisionedResultReceiver;
    private final ResultReceiver mInternalSatelliteSupportedResultReceiver;
    private final IBooleanConsumer mInternalSatelliteSupportedStateCallback;
    private final ResultReceiver mInternalUpdateSystemSelectionChannelsResultReceiver;
    private AtomicBoolean mIsAllowedStateCacheDisabledForCtsTest;
    private AtomicBoolean mIsCurrentLocationEligibleForNotification;
    private AtomicBoolean mIsLocationProviderEnabled;
    private AtomicBoolean mIsLocationSettingsEnabled;
    private AtomicBoolean mIsOverlayConfigOverridden;
    private AtomicBoolean mIsProvisionEligibleForNotification;
    private AtomicBoolean mIsSatelliteAllowAccessControl;
    private AtomicBoolean mIsSatelliteAllowedRegionPossiblyChanged;
    protected AtomicLong mLastLocationQueryForPossibleChangeInAllowedRegionTimeNanos;
    private AtomicBoolean mLatestSatelliteCommunicationAllowed;
    protected AtomicLong mLatestSatelliteCommunicationAllowedSetTime;
    private AtomicLong mLocationFreshDurationNanos;
    private final LocationManager mLocationManager;
    protected BroadcastReceiver mLocationModeChangedBroadcastReceiver;
    private AtomicLong mLocationQueryStartTimeMillis;
    private AtomicLong mLocationQueryThrottleIntervalNanos;
    protected CancellationSignal mLocationRequestCancellationSignal;
    protected final Object mLock;
    private AtomicInteger mMaxRetryCountForValidatingPossibleChangeInAllowedRegion;
    protected AtomicInteger mNewRegionalConfigId;
    private NotificationManager mNotificationManager;
    private AtomicLong mOnDeviceLookupStartTimeMillis;
    private AtomicBoolean mOverriddenIsSatelliteAllowAccessControl;
    private AtomicLong mOverriddenLocationFreshDurationNanos;
    private File mOverriddenSatelliteAccessConfigFile;
    private List mOverriddenSatelliteCountryCodes;
    private File mOverriddenSatelliteS2CellFile;
    private PersistentLogger mPersistentLogger;
    protected AtomicInteger mRegionalConfigId;
    protected AtomicInteger mRetryCountForValidatingPossibleChangeInAllowedRegion;
    private AtomicLong mRetryIntervalToEvaluateUserInSatelliteAllowedRegion;
    private AtomicInteger mS2Level;
    private File mSatelliteAccessConfigFile;
    protected Map mSatelliteAccessConfigMap;
    protected AtomicInteger mSatelliteAccessConfigVersion;
    private Notification mSatelliteAvailableNotification;
    private final ConcurrentHashMap mSatelliteCommunicationAccessStateChangedListeners;
    private final SatelliteController mSatelliteController;
    private List mSatelliteCountryCodes;
    private final List mSatelliteDisallowedReasons;
    private final ConcurrentHashMap mSatelliteDisallowedReasonsChangedListeners;
    private final Object mSatelliteDisallowedReasonsLock;
    protected SatelliteOnDeviceAccessController mSatelliteOnDeviceAccessController;
    private ConcurrentHashMap mSatelliteRegionalConfigPerSubMap;
    private File mSatelliteS2CellFile;
    private final ConcurrentHashMap mSatelliteUnAvailableNotifications;
    private SharedPreferences mSharedPreferences;
    private final TelecomManager mTelecomManager;
    private AtomicLong mTotalCheckingStartTimeMillis;
    private final Set mUpdateSystemSelectionChannelsResultReceivers;
    protected static final long WAIT_FOR_CURRENT_LOCATION_TIMEOUT_MILLIS = 180000;
    protected static final long WAIT_UNTIL_CURRENT_LOCATION_QUERY_IS_DONE_MILLIS = 90000;
    protected static final long KEEP_ON_DEVICE_ACCESS_CONTROLLER_RESOURCES_TIMEOUT_MILLIS = 1800000;
    private static final boolean DEBUG = !"user".equals(Build.TYPE);
    private static final List DISALLOWED_REASONS_TO_BE_RESET = Arrays.asList(2, 4);
    private static final HashMap SATELLITE_SOS_UNAVAILABLE_REASONS = new HashMap(Map.of(0, new Pair(Integer.valueOf(R.string.whichViewApplication), Integer.valueOf(R.string.whichSendToApplicationNamed)), 1, new Pair(Integer.valueOf(R.string.whichSendToApplicationLabel), Integer.valueOf(R.string.whichSendToApplication)), 2, new Pair(Integer.valueOf(R.string.whichSendApplicationNamed), Integer.valueOf(R.string.whichSendApplicationLabel)), 3, new Pair(Integer.valueOf(R.string.whichViewApplicationNamed), Integer.valueOf(R.string.whichViewApplicationLabel)), 4, new Pair(Integer.valueOf(R.string.whichSendApplication), Integer.valueOf(R.string.whichOpenLinksWithApp))));
    private static final HashMap SATELLITE_MESSAGING_UNAVAILABLE_REASONS = new HashMap(Map.of(0, new Pair(Integer.valueOf(R.string.whichEditApplicationLabel), Integer.valueOf(R.string.whichEditApplication)), 1, new Pair(Integer.valueOf(R.string.whichApplicationNamed), Integer.valueOf(R.string.whichApplicationLabel)), 2, new Pair(Integer.valueOf(R.string.whichApplication), Integer.valueOf(R.string.wfc_mode_wifi_preferred_summary)), 3, new Pair(Integer.valueOf(R.string.whichGiveAccessToApplicationLabel), Integer.valueOf(R.string.whichEditApplicationNamed)), 4, new Pair(Integer.valueOf(R.string.wfc_mode_wifi_only_summary), Integer.valueOf(R.string.wfc_mode_cellular_preferred_summary))));
    protected static final long ALLOWED_STATE_CACHE_VALID_DURATION_NANOS = 14400000000000L;

    private int getDisallowedReason(int i, boolean z) {
        if (i == 0) {
            return !z ? 2 : -1;
        }
        return i == 25 ? 4 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldRetryValidatingPossibleChangeInAllowedRegion(int i) {
        return i == 26;
    }

    protected SatelliteAccessController(final Context context, FeatureFlags featureFlags, Looper looper, LocationManager locationManager, TelecomManager telecomManager, SatelliteOnDeviceAccessController satelliteOnDeviceAccessController, File file) {
        super(looper);
        this.mSatelliteRegionalConfigPerSubMap = new ConcurrentHashMap();
        this.mCtsSatS2FilePath = null;
        this.mCtsSatelliteAccessConfigurationFilePath = null;
        this.mPersistentLogger = null;
        this.mSatelliteCommunicationAccessStateChangedListeners = new ConcurrentHashMap();
        this.mSatelliteDisallowedReasonsChangedListeners = new ConcurrentHashMap();
        this.mSatelliteUnAvailableNotifications = new ConcurrentHashMap();
        this.mLocationModeChangedBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.location.MODE_CHANGED")) {
                    SatelliteAccessController.this.plogd("LocationManager mode is changed");
                    boolean zIsLocationEnabled = SatelliteAccessController.this.mLocationManager.isLocationEnabled();
                    SatelliteAccessController satelliteAccessController = SatelliteAccessController.this;
                    if (zIsLocationEnabled) {
                        satelliteAccessController.plogd("Location settings is just enabled");
                        SatelliteAccessController.this.sendRequestAsync(6, null);
                    } else {
                        satelliteAccessController.plogd("Location settings is just disabled");
                        SatelliteAccessController.this.sendRequestAsync(8, null);
                    }
                }
                if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {
                    SatelliteAccessController.this.plogd("mLocationModeChangedBroadcastReceiver: PROVIDERS_CHANGED_ACTION");
                    SatelliteAccessController.this.sendRequestAsync(14, null);
                }
            }
        };
        this.mRegionalConfigId = null;
        this.mNewRegionalConfigId = null;
        this.mIsOverlayConfigOverridden = new AtomicBoolean(false);
        this.mIsSatelliteAllowAccessControl = new AtomicBoolean(false);
        this.mSatelliteAccessConfigVersion = new AtomicInteger(0);
        this.mLocationFreshDurationNanos = new AtomicLong(0L);
        this.mOverriddenIsSatelliteAllowAccessControl = new AtomicBoolean(false);
        this.mOverriddenLocationFreshDurationNanos = new AtomicLong(0L);
        this.mIsSatelliteAllowedRegionPossiblyChanged = new AtomicBoolean(false);
        this.mCurrentSatelliteAllowedState = new AtomicBoolean(false);
        this.mLastLocationQueryForPossibleChangeInAllowedRegionTimeNanos = new AtomicLong(0L);
        this.mRetryCountForValidatingPossibleChangeInAllowedRegion = new AtomicInteger(0);
        this.mRetryIntervalToEvaluateUserInSatelliteAllowedRegion = new AtomicLong(0L);
        this.mMaxRetryCountForValidatingPossibleChangeInAllowedRegion = new AtomicInteger(0);
        this.mLocationQueryThrottleIntervalNanos = new AtomicLong(0L);
        this.mLatestSatelliteCommunicationAllowed = new AtomicBoolean(false);
        this.mLatestSatelliteCommunicationAllowedSetTime = new AtomicLong(0L);
        this.mLocationQueryStartTimeMillis = new AtomicLong(0L);
        this.mOnDeviceLookupStartTimeMillis = new AtomicLong(0L);
        this.mTotalCheckingStartTimeMillis = new AtomicLong(0L);
        this.mIsCurrentLocationEligibleForNotification = new AtomicBoolean(false);
        this.mIsProvisionEligibleForNotification = new AtomicBoolean(false);
        this.mIsAllowedStateCacheDisabledForCtsTest = new AtomicBoolean(false);
        this.mIsLocationSettingsEnabled = new AtomicBoolean(false);
        this.mIsLocationProviderEnabled = new AtomicBoolean(false);
        this.mS2Level = new AtomicInteger(12);
        this.mLock = new Object();
        this.mCachedAccessRestrictionMap = new LinkedHashMap() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.2
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry entry) {
                return size() > 50;
            }
        };
        this.mSatelliteDisallowedReasonsLock = new Object();
        this.mSatelliteDisallowedReasons = new ArrayList();
        this.mLocationRequestCancellationSignal = null;
        this.mFreshLastKnownLocation = null;
        this.mCheckingAllowedStateRequests = new ArrayList();
        this.mUpdateSystemSelectionChannelsResultReceivers = new HashSet();
        this.mDefaultSmsAppChangedBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.12
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent == null || intent.getAction() == null) {
                    return;
                }
                String action = intent.getAction();
                if ("android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED_INTERNAL".equals(action) || "android.intent.action.PACKAGE_CHANGED".equals(action)) {
                    FeatureFlags unused = SatelliteAccessController.this.mFeatureFlags;
                    SatelliteAccessController.this.sendRequestAsync(15, context2);
                }
            }
        };
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        this.mLocationManager = locationManager;
        this.mTelecomManager = telecomManager;
        setOnDeviceAccessController(satelliteOnDeviceAccessController);
        this.mCountryDetector = TelephonyCountryDetector.getInstance(context, featureFlags);
        setIsSatelliteAllowedRegionPossiblyChanged(false);
        SatelliteController satelliteController = SatelliteController.getInstance();
        this.mSatelliteController = satelliteController;
        this.mControllerMetricsStats = ControllerMetricsStats.getInstance();
        this.mAccessControllerMetricsStats = AccessControllerMetricsStats.getInstance();
        this.mInternalSatelliteSupportedResultReceiver = new ResultReceiver(this) { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.3
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                FeatureFlags unused = SatelliteAccessController.this.mFeatureFlags;
                SomeArgs someArgsObtain = SomeArgs.obtain();
                someArgsObtain.arg1 = Integer.valueOf(i);
                someArgsObtain.arg2 = bundle;
                SatelliteAccessController satelliteAccessController = SatelliteAccessController.this;
                satelliteAccessController.sendMessage(satelliteAccessController.obtainMessage(16, someArgsObtain));
            }
        };
        satelliteController.incrementResultReceiverCount("SAC:mInternalSatelliteSupportedResultReceiver");
        this.mInternalSatelliteProvisionedResultReceiver = new ResultReceiver(this) { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.4
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                FeatureFlags unused = SatelliteAccessController.this.mFeatureFlags;
                SomeArgs someArgsObtain = SomeArgs.obtain();
                someArgsObtain.arg1 = Integer.valueOf(i);
                someArgsObtain.arg2 = bundle;
                SatelliteAccessController satelliteAccessController = SatelliteAccessController.this;
                satelliteAccessController.sendMessage(satelliteAccessController.obtainMessage(17, someArgsObtain));
            }
        };
        this.mConfigUpdaterMetricsStats = ConfigUpdaterMetricsStats.getOrCreateInstance();
        this.mInternalSatelliteSupportedStateCallback = new IBooleanConsumer.Stub() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.5
            public void accept(boolean z) {
                SatelliteAccessController.logd("onSatelliteSupportedStateChanged: isSupported=" + z);
                FeatureFlags unused = SatelliteAccessController.this.mFeatureFlags;
                SomeArgs someArgsObtain = SomeArgs.obtain();
                someArgsObtain.arg1 = Boolean.valueOf(z);
                SatelliteAccessController satelliteAccessController = SatelliteAccessController.this;
                satelliteAccessController.sendMessage(satelliteAccessController.obtainMessage(18, someArgsObtain));
            }
        };
        this.mInternalSatelliteProvisionStateCallback = new ISatelliteProvisionStateCallback.Stub() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.6
            public void onSatelliteProvisionStateChanged(boolean z) {
                SatelliteAccessController.logd("onSatelliteProvisionStateChanged: isProvisioned=" + z);
                FeatureFlags unused = SatelliteAccessController.this.mFeatureFlags;
                SomeArgs someArgsObtain = SomeArgs.obtain();
                someArgsObtain.arg1 = Boolean.valueOf(z);
                SatelliteAccessController satelliteAccessController = SatelliteAccessController.this;
                satelliteAccessController.sendMessage(satelliteAccessController.obtainMessage(19, someArgsObtain));
            }

            public void onSatelliteSubscriptionProvisionStateChanged(List list) {
                SatelliteAccessController.logd("onSatelliteSubscriptionProvisionStateChanged: " + list);
            }
        };
        this.mInternalUpdateSystemSelectionChannelsResultReceiver = new ResultReceiver(this) { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.7
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                SatelliteAccessController.this.plogd("UpdateSystemSelectionChannels.onReceiveResult: resultCode=" + i + ", resultData=" + bundle);
                FeatureFlags unused = SatelliteAccessController.this.mFeatureFlags;
                SomeArgs someArgsObtain = SomeArgs.obtain();
                someArgsObtain.arg1 = Integer.valueOf(i);
                someArgsObtain.arg2 = bundle;
                SatelliteAccessController satelliteAccessController = SatelliteAccessController.this;
                satelliteAccessController.sendMessage(satelliteAccessController.obtainMessage(20, someArgsObtain));
            }
        };
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mCarrierConfigManager = carrierConfigManager;
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda1
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                this.f$0.lambda$new$0(context, i, i2, i3, i4);
            }
        };
        this.mCarrierConfigChangeListener = carrierConfigChangeListener;
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new HandlerExecutor(new Handler(looper)), carrierConfigChangeListener);
        }
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = file;
        sendMessage(obtainMessage(22, someArgsObtain));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Context context, int i, int i2, int i3, int i4) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = context;
        someArgsObtain.arg2 = Integer.valueOf(i);
        someArgsObtain.arg3 = Integer.valueOf(i2);
        someArgsObtain.arg4 = Integer.valueOf(i3);
        someArgsObtain.arg5 = Integer.valueOf(i4);
        sendMessage(obtainMessage(21, someArgsObtain));
    }

    private void initSatelliteAccessController(File file) {
        plogd("initSatelliteAccessController: start");
        this.mPersistentLogger = SatelliteServiceUtils.getPersistentLogger(this.mContext);
        this.mCountryDetector.registerForCountryCodeChanged(this, 5, (Object) null);
        initializeHandlerForSatelliteAllowedResult();
        initSharedPreferences(this.mContext);
        checkSharedPreference();
        loadOverlayConfigs(this.mContext);
        loadConfigUpdaterConfigs();
        this.mSatelliteController.registerForConfigUpdateChanged(this, 4, this.mContext);
        this.mSatelliteController.registerForSatelliteSubIdChanged(this, 9, this.mContext);
        if (file != null) {
            logd("setSatelliteS2CellFile during Initialization");
            setSatelliteS2CellFile(file);
        }
        initializeSatelliteSystemNotification(this.mContext);
        registerDefaultSmsAppChangedBroadcastReceiver(this.mContext);
        plogd("registerForSatelliteSupportedStateChanged result: " + this.mSatelliteController.registerForSatelliteSupportedStateChanged(this.mInternalSatelliteSupportedStateCallback));
        plogd("registerForSatelliteProvisionStateChanged result: " + this.mSatelliteController.registerForSatelliteProvisionStateChanged(this.mInternalSatelliteProvisionStateCallback));
        initSatelliteOnDeviceAccessController();
        registerLocationModeChangedBroadcastReceiver(this.mContext);
        plogd("initSatelliteAccessController: end");
    }

    private void updateCurrentSatelliteAllowedState(boolean z, boolean z2) {
        plogd("updateCurrentSatelliteAllowedState");
        if (z != this.mCurrentSatelliteAllowedState.get()) {
            plogd("updateCurrentSatelliteAllowedState: updatedValue = " + z + " | mCurrentSatelliteAllowedState = " + this.mCurrentSatelliteAllowedState.get());
            this.mCurrentSatelliteAllowedState.set(z);
            notifySatelliteCommunicationAllowedStateChanged(z);
            this.mControllerMetricsStats.reportAllowedStateChanged();
            if (!z) {
                plogd("updateCurrentSatelliteAllowedState : set mNewRegionalConfigId null");
                setNewRegionalConfigId(null);
                if (z2) {
                    plogd("updateCurrentSatelliteAllowedState: clear allowed state cache when location is just disabled");
                    this.mLatestSatelliteCommunicationAllowedSetTime.set(0L);
                }
            }
        }
        updateRegionalConfigId();
    }

    public static synchronized SatelliteAccessController getOrCreateInstance(Context context, FeatureFlags featureFlags) {
        try {
            if (sInstance == null) {
                HandlerThread handlerThread = new HandlerThread("SatelliteAccessController");
                handlerThread.start();
                sInstance = new SatelliteAccessController(context, featureFlags, handlerThread.getLooper(), (LocationManager) context.createAttributionContext("telephony").getSystemService(LocationManager.class), (TelecomManager) context.getSystemService(TelecomManager.class), null, null);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        SatelliteAccessController satelliteAccessController;
        switch (message.what) {
            case 1:
                handleCmdIsSatelliteAllowedForCurrentLocation((CheckingAllowedStateRequestArguments) message.obj);
                return;
            case 2:
                handleWaitForCurrentLocationTimedOutEvent();
                return;
            case 3:
                cleanupOnDeviceAccessControllerResources();
                return;
            case 4:
                updateSatelliteAccessDataWithConfigUpdaterData((Context) ((AsyncResult) message.obj).userObj);
                return;
            case 5:
                satelliteAccessController = this;
                satelliteAccessController.plogd("EVENT_COUNTRY_CODE_CHANGED");
                satelliteAccessController.handleSatelliteAllowedRegionPossiblyChanged(message.what);
                return;
            case 6:
                satelliteAccessController = this;
                satelliteAccessController.plogd("EVENT_LOCATION_SETTINGS_ENABLED");
                satelliteAccessController.plogd("EVENT_LOCATION_SETTINGS_DISABLED");
                satelliteAccessController.plogd("EVENT_COUNTRY_CODE_CHANGED");
                satelliteAccessController.handleSatelliteAllowedRegionPossiblyChanged(message.what);
                return;
            case 7:
                handleCmdUpdateSystemSelectionChannels((ResultReceiver) message.obj);
                return;
            case 8:
                satelliteAccessController = this;
                satelliteAccessController.plogd("EVENT_LOCATION_SETTINGS_DISABLED");
                satelliteAccessController.plogd("EVENT_COUNTRY_CODE_CHANGED");
                satelliteAccessController.handleSatelliteAllowedRegionPossiblyChanged(message.what);
                return;
            case 9:
                plogd("Event: EVENT_SATELLITE_SUBSCRIPTION_CHANGED");
                initializeSatelliteSystemNotification(this.mContext);
                handleEventDisallowedReasonsChanged();
                return;
            case 10:
                satelliteAccessController = this;
                satelliteAccessController.plogd("EVENT_CONFIG_DATA_UPDATED");
                satelliteAccessController.plogd("EVENT_LOCATION_SETTINGS_ENABLED");
                satelliteAccessController.plogd("EVENT_LOCATION_SETTINGS_DISABLED");
                satelliteAccessController.plogd("EVENT_COUNTRY_CODE_CHANGED");
                satelliteAccessController.handleSatelliteAllowedRegionPossiblyChanged(message.what);
                return;
            case 11:
                plogd("REQUEST_IS_COMMUNICATION_ALLOWED");
                SomeArgs someArgs = (SomeArgs) message.obj;
                try {
                    handleRequestIsCommunicationAllowed((ResultReceiver) someArgs.arg1, ((Boolean) someArgs.arg2).booleanValue());
                    return;
                } finally {
                    someArgs.recycle();
                }
            case 12:
                plogd("REQUEST_UPDATE_SYSTEM_SELECTION_CHANNELS");
                SomeArgs someArgs2 = (SomeArgs) message.obj;
                try {
                    handleRequestUpdateSystemSelectionChannels((ResultReceiver) someArgs2.arg1);
                    return;
                } finally {
                    someArgs2.recycle();
                }
            case 13:
                plogd("REQUEST_SATELLITE_ACCESS_CONFIGURATION_FOR_CURRENT_LOCATION");
                SomeArgs someArgs3 = (SomeArgs) message.obj;
                try {
                    handleRequestSatelliteAccessConfigurationForCurrentLocation((ResultReceiver) someArgs3.arg1);
                    return;
                } finally {
                    someArgs3.recycle();
                }
            case 14:
                handleEventLocationProvidersChanged();
                return;
            case 15:
                evaluatePossibleChangeInDefaultSmsApp((Context) message.obj);
                return;
            case 16:
                plogd("EVENT_IS_SATELLITE_SUPPORTED_DONE");
                SomeArgs someArgs4 = (SomeArgs) message.obj;
                try {
                    handleIsSatelliteSupportedResult(((Integer) someArgs4.arg1).intValue(), (Bundle) someArgs4.arg2);
                    return;
                } finally {
                    someArgs4.recycle();
                }
            case 17:
                plogd("EVENT_IS_SATELLITE_PROVISIONED_DONE");
                SomeArgs someArgs5 = (SomeArgs) message.obj;
                try {
                    handleIsSatelliteProvisionedResult(((Integer) someArgs5.arg1).intValue(), (Bundle) someArgs5.arg2);
                    return;
                } finally {
                    someArgs5.recycle();
                }
            case 18:
                plogd("EVENT_SATELLITE_SUPPORTED_STATE_CHANGED");
                SomeArgs someArgs6 = (SomeArgs) message.obj;
                try {
                    handleSatelliteSupportedStateChanged(((Boolean) someArgs6.arg1).booleanValue());
                    return;
                } finally {
                    someArgs6.recycle();
                }
            case 19:
                plogd("EVENT_SATELLITE_PROVISIONED_STATE_CHANGED");
                SomeArgs someArgs7 = (SomeArgs) message.obj;
                try {
                    handleSatelliteProvisionedStateChanged(((Boolean) someArgs7.arg1).booleanValue());
                    return;
                } finally {
                    someArgs7.recycle();
                }
            case 20:
                plogd("EVENT_SEND_UPDATE_SYSTEM_SELECTION_CHANNELS_RESULT");
                SomeArgs someArgs8 = (SomeArgs) message.obj;
                try {
                    sendUpdateSystemSelectionChannelsResult(((Integer) someArgs8.arg1).intValue(), (Bundle) someArgs8.arg2);
                    return;
                } finally {
                    someArgs8.recycle();
                }
            case 21:
                plogd("EVENT_CARRIER_CONFIG_CHANGED");
                SomeArgs someArgs9 = (SomeArgs) message.obj;
                try {
                    handleCarrierConfigChanged((Context) someArgs9.arg1, ((Integer) someArgs9.arg2).intValue(), ((Integer) someArgs9.arg3).intValue(), ((Integer) someArgs9.arg4).intValue(), ((Integer) someArgs9.arg5).intValue());
                    return;
                } finally {
                    someArgs9.recycle();
                }
            case 22:
                plogd("CMD_INIT_SATELLITE_ACCESS_CONTROLLER");
                SomeArgs someArgs10 = (SomeArgs) message.obj;
                try {
                    initSatelliteAccessController((File) someArgs10.arg1);
                    return;
                } finally {
                    someArgs10.recycle();
                }
            default:
                plogw("SatelliteAccessControllerHandler: unexpected message code: " + message.what);
                return;
        }
    }

    public void requestIsCommunicationAllowedForCurrentLocation(ResultReceiver resultReceiver, boolean z) {
        plogd("requestIsCommunicationAllowedForCurrentLocation : shouldIgnoreAllowedStateCache= " + z);
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        someArgsObtain.arg2 = Boolean.valueOf(z);
        sendMessage(obtainMessage(11, someArgsObtain));
    }

    private void handleRequestIsCommunicationAllowed(ResultReceiver resultReceiver, boolean z) {
        plogd("handleRequestIsCommunicationAllowed : shouldIgnoreAllowedStateCache = " + z);
        this.mAccessControllerMetricsStats.setTriggeringEvent(1);
        sendRequestAsync(1, new CheckingAllowedStateRequestArguments(this.mSatelliteController.getSelectedSatelliteSubId(), resultReceiver, z));
        this.mSatelliteController.incrementResultReceiverCount("SAC:requestIsCommunicationAllowedForCurrentLocation");
    }

    public void requestSatelliteAccessConfigurationForCurrentLocation(ResultReceiver resultReceiver) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(13, someArgsObtain));
    }

    private void handleRequestSatelliteAccessConfigurationForCurrentLocation(final ResultReceiver resultReceiver) {
        plogd("handleRequestSatelliteAccessConfigurationForCurrentLocation");
        requestIsCommunicationAllowedForCurrentLocation(new ResultReceiver(this) { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.8
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                SatelliteAccessController.this.plogd("requestSatelliteAccessConfigurationForCurrentLocation: resultCode=" + i + ", resultData=" + bundle);
                if (i == 0) {
                    if (!bundle.containsKey("satellite_communication_allowed")) {
                        SatelliteAccessController.loge("KEY_SATELLITE_COMMUNICATION_ALLOWED does not exist.");
                        resultReceiver.send(6, null);
                        return;
                    }
                    SatelliteAccessConfiguration currentSatelliteAccessConfiguration = bundle.getBoolean("satellite_communication_allowed") ? SatelliteAccessController.this.getCurrentSatelliteAccessConfiguration() : null;
                    SatelliteAccessController.this.plogd("requestSatelliteAccessConfigurationForCurrentLocation : satelliteAccessConfig is " + currentSatelliteAccessConfiguration);
                    if (currentSatelliteAccessConfiguration == null) {
                        resultReceiver.send(12, null);
                        return;
                    }
                    Bundle bundle2 = new Bundle();
                    bundle2.putParcelable("satellite_access_configuration", currentSatelliteAccessConfiguration);
                    resultReceiver.send(i, bundle2);
                    return;
                }
                SatelliteAccessController.loge("resultCode is not SATELLITE_RESULT_SUCCESS.");
                resultReceiver.send(i, null);
            }
        }, false);
    }

    public boolean setSatelliteAccessControlOverlayConfigs(boolean z, boolean z2, String str, long j, List list, String str2) {
        if (!isMockModemAllowed()) {
            plogd("setSatelliteAccessControllerOverlayConfigs: mock modem is not allowed");
            return false;
        }
        StringBuilder sb = new StringBuilder("setSatelliteAccessControlOverlayConfigs: reset=");
        sb.append(z);
        sb.append(", isAllowed");
        sb.append(z2);
        sb.append(", s2CellFile=");
        sb.append(str);
        sb.append(", locationFreshDurationNanos=");
        sb.append(j);
        sb.append(", satelliteCountryCodes=");
        sb.append(list != null ? String.join(", ", list) : null);
        sb.append(", satelliteConfigurationFile=");
        sb.append(str2);
        plogd(sb.toString());
        synchronized (this.mLock) {
            AtomicBoolean atomicBoolean = this.mIsOverlayConfigOverridden;
            try {
                if (z) {
                    atomicBoolean.set(false);
                    cleanUpCtsResources();
                    cleanUpTelephonyConfigs();
                    cleanUpSatelliteAccessConfigOtaResources();
                    cleanupSatelliteConfigOtaResources();
                    plogd("reload overly config and configupdater config");
                    loadOverlayConfigs(this.mContext);
                    loadConfigUpdaterConfigs();
                } else {
                    atomicBoolean.set(true);
                    this.mOverriddenIsSatelliteAllowAccessControl.set(z2);
                    if (!TextUtils.isEmpty(str)) {
                        File testSatelliteS2File = getTestSatelliteS2File(str);
                        this.mOverriddenSatelliteS2CellFile = testSatelliteS2File;
                        if (!testSatelliteS2File.exists()) {
                            plogd("The overriding file " + this.mOverriddenSatelliteS2CellFile.getAbsolutePath() + " does not exist");
                            this.mOverriddenSatelliteS2CellFile = null;
                        }
                        this.mCachedAccessRestrictionMap.clear();
                    } else {
                        this.mOverriddenSatelliteS2CellFile = null;
                    }
                    if (!TextUtils.isEmpty(str2)) {
                        File testSatelliteConfiguration = getTestSatelliteConfiguration(str2);
                        this.mOverriddenSatelliteAccessConfigFile = testSatelliteConfiguration;
                        if (!testSatelliteConfiguration.exists()) {
                            plogd("The overriding file " + this.mOverriddenSatelliteAccessConfigFile.getAbsolutePath() + " does not exist");
                            this.mOverriddenSatelliteAccessConfigFile = null;
                        }
                    } else {
                        this.mOverriddenSatelliteAccessConfigFile = null;
                    }
                    this.mOverriddenLocationFreshDurationNanos.set(j);
                    if (list != null) {
                        this.mOverriddenSatelliteCountryCodes = list;
                    } else {
                        this.mOverriddenSatelliteCountryCodes = new ArrayList();
                    }
                }
                cleanupOnDeviceAccessControllerResources();
                initSatelliteOnDeviceAccessController();
            } catch (Throwable th) {
                throw th;
            }
        }
        return true;
    }

    public void updateSystemSelectionChannels(ResultReceiver resultReceiver) {
        plogd("updateSystemSelectionChannels");
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = resultReceiver;
        sendMessage(obtainMessage(12, someArgsObtain));
    }

    private void handleRequestUpdateSystemSelectionChannels(ResultReceiver resultReceiver) {
        plogd("handleRequestUpdateSystemSelectionChannels");
        if (getRegionalConfigId() == null) {
            plogd("updateSystemSelectionChannels: Invalid Regional config ID. System Selection channels can not be passed down to modem");
            resultReceiver.send(16, null);
        } else {
            sendRequestAsync(7, resultReceiver);
        }
    }

    protected File getTestSatelliteS2File(String str) {
        plogd("getTestSatelliteS2File: fileName=" + str);
        if (TextUtils.equals(str, "google_us_san_sat_s2.dat") || TextUtils.equals(str, "google_us_san_mtv_sat_s2.dat")) {
            Path pathCopyTestAssetFileToPhoneDirectory = copyTestAssetFileToPhoneDirectory(str);
            this.mCtsSatS2FilePath = pathCopyTestAssetFileToPhoneDirectory;
            if (pathCopyTestAssetFileToPhoneDirectory != null) {
                return pathCopyTestAssetFileToPhoneDirectory.toFile();
            }
            ploge("getTestSatelliteS2File: mCtsSatS2FilePath is null");
        }
        return new File(str);
    }

    protected File getTestSatelliteConfiguration(String str) {
        plogd("getTestSatelliteConfiguration: fileName=" + str);
        if (TextUtils.equals(str, "satellite_access_config.json")) {
            Path pathCopyTestAssetFileToPhoneDirectory = copyTestAssetFileToPhoneDirectory(str);
            this.mCtsSatelliteAccessConfigurationFilePath = pathCopyTestAssetFileToPhoneDirectory;
            if (pathCopyTestAssetFileToPhoneDirectory != null) {
                return pathCopyTestAssetFileToPhoneDirectory.toFile();
            }
            ploge("getTestSatelliteConfiguration: mCtsSatelliteConfigurationFilePath is null");
        }
        return new File(str);
    }

    private static Path copyTestAssetFileToPhoneDirectory(String str) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        File dir = phoneGlobals.getDir("cts", 0);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Path pathResolve = dir.toPath().resolve(str);
        try {
            AssetManager assets = phoneGlobals.getAssets();
            if (assets == null) {
                loge("copyTestAssetFileToPhoneDirectory: no assets");
                return null;
            }
            InputStream inputStreamOpen = assets.open(str);
            if (inputStreamOpen == null) {
                loge("copyTestAssetFileToPhoneDirectory: Resource=" + str + " not found");
                return null;
            }
            Files.copy(inputStreamOpen, pathResolve, StandardCopyOption.REPLACE_EXISTING);
            return pathResolve;
        } catch (IOException e) {
            loge("copyTestAssetFileToPhoneDirectory: ex=" + e);
            return null;
        }
    }

    private static File copyFileToLocalDirectory(File file, String str) {
        logd("copyFileToLocalDirectory: Copying sourceFile:" + file.getAbsolutePath() + " to targetFileName:" + str);
        File dir = PhoneGlobals.getInstance().getDir("satellite_access_control", 0);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Path pathResolve = dir.toPath().resolve(str);
        logd("copyFileToLocalDirectory: Copying from sourceFile=" + file.getAbsolutePath() + " to targetFilePath=" + pathResolve);
        try {
            Files.copy(new FileInputStream(file), pathResolve, StandardCopyOption.REPLACE_EXISTING);
            File file2 = pathResolve.toFile();
            if (file2 == null || !file2.exists()) {
                loge("copyFileToLocalDirectory: targetFile is null or not exist");
                return null;
            }
            logd("copyFileToLocalDirectory: Copied from sourceFile=" + file.getAbsolutePath() + " to targetFilePath=" + pathResolve);
            return file2;
        } catch (IOException e) {
            loge("copyFileToLocalDirectory: ex=" + e);
            return null;
        }
    }

    private File getConfigUpdaterSatelliteConfigFileFromLocalDirectory(String str) {
        File dir = PhoneGlobals.getInstance().getDir("satellite_access_control", 0);
        if (!dir.exists()) {
            ploge("getConfigUpdaterSatelliteConfigFileFromLocalDirectory: Directory: " + dir.getAbsoluteFile() + " is not exist");
            return null;
        }
        File file = dir.toPath().resolve(str).toFile();
        if (file.exists()) {
            return file;
        }
        ploge("getConfigUpdaterSatelliteConfigFileFromLocalDirectory: File: " + str + " is not exist");
        return null;
    }

    private boolean isS2CellFileValid(File file) {
        try {
            SatelliteOnDeviceAccessController satelliteOnDeviceAccessControllerCreate = SatelliteOnDeviceAccessController.create(file, this.mFeatureFlags);
            int s2Level = satelliteOnDeviceAccessControllerCreate.getS2Level();
            if (s2Level >= 0 && s2Level <= 30) {
                satelliteOnDeviceAccessControllerCreate.close();
                return true;
            }
            ploge("isS2CellFileValid: invalid s2 level = " + s2Level);
            satelliteOnDeviceAccessControllerCreate.close();
            return false;
        } catch (Exception e) {
            ploge("isS2CellFileValid: Got exception in reading the file, ex=" + e);
            return false;
        }
    }

    private void cleanUpCtsResources() {
        Path path = this.mCtsSatS2FilePath;
        if (path != null) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                ploge("cleanUpCtsResources: ex=" + e);
            }
        }
    }

    private void cleanUpTelephonyConfigs() {
        this.mSatelliteController.cleanUpTelephonyConfigs();
    }

    private void cleanUpSatelliteAccessConfigOtaResources() {
        File dir = PhoneGlobals.getInstance().getDir("satellite_access_control", 0);
        if (dir == null || !dir.exists()) {
            plogd("cleanUpSatelliteAccessConfigOtaResources: satellite_access_control does not exist");
        } else {
            plogd("cleanUpSatelliteAccessConfigOtaResources: Deleting contents under satellite_access_control");
            FileUtils.deleteContents(dir);
        }
    }

    private void cleanupSatelliteConfigOtaResources() {
        SatelliteConfig satelliteConfig = this.mSatelliteController.getSatelliteConfig();
        if (satelliteConfig == null) {
            plogd("cleanupSatelliteConfigOtaResources: satelliteConfig is null. Cannot or Not needed to delete satellite config OTA files");
        } else {
            plogd("cleanupSatelliteConfigOtaResources: Deleting satellite config OTA files");
            satelliteConfig.cleanOtaResources(this.mContext);
        }
    }

    protected long getElapsedRealtimeNanos() {
        return SystemClock.elapsedRealtimeNanos();
    }

    private boolean isValidCountryCodes(List list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (!TelephonyUtils.isValidCountryCode(str)) {
                ploge("invalid country code : " + str);
                return false;
            }
        }
        return true;
    }

    private boolean updateSharedPreferencesCountryCodes(Context context, List list) {
        if (this.mSharedPreferences == null) {
            plogd("updateSharedPreferencesCountryCodes: mSharedPreferences is null");
            initSharedPreferences(context);
        }
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            ploge("updateSharedPreferencesCountryCodes: mSharedPreferences is still null");
            return false;
        }
        try {
            sharedPreferences.edit().putStringSet("config_updater_satellite_country_codes", new HashSet(list)).apply();
            return true;
        } catch (Exception e) {
            ploge("updateSharedPreferencesCountryCodes error : " + e);
            return false;
        }
    }

    private void deleteSharedPreferencesbyKey(Context context, String str) {
        plogd("deletedeleteSharedPreferencesbyKey: " + str);
        if (this.mSharedPreferences == null) {
            plogd("deleteSharedPreferencesbyKey: mSharedPreferences is null");
            initSharedPreferences(context);
        }
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            plogd("deleteSharedPreferencesbyKey: mSharedPreferences is still null");
            return;
        }
        try {
            sharedPreferences.edit().remove(str).apply();
        } catch (Exception e) {
            ploge("deleteSharedPreferencesbyKey error : " + e);
        }
    }

    private boolean updateSharedPreferencesIsAllowAccessControl(Context context, boolean z) {
        if (this.mSharedPreferences == null) {
            plogd("updateSharedPreferencesIsAllowAccessControl: mSharedPreferences is null");
            initSharedPreferences(context);
        }
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            ploge("updateSharedPreferencesIsAllowAccessControl: mSharedPreferences is null");
            return false;
        }
        try {
            sharedPreferences.edit().putBoolean("config_updater_satellite_is_allow_access_control", z).apply();
            return true;
        } catch (Exception e) {
            ploge("updateSharedPreferencesIsAllowAccessControl error: " + e);
            return false;
        }
    }

    private boolean updateSharedPreferencesSatelliteAccessConfigVersion(Context context, int i) {
        if (this.mSharedPreferences == null) {
            plogd("updateSharedPreferencesSatelliteAccessConfigVersion: mSharedPreferences is null");
            initSharedPreferences(context);
        }
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            ploge("updateSharedPreferencesSatelliteAccessConfigVersion: mSharedPreferences is null");
            return false;
        }
        try {
            sharedPreferences.edit().putInt("config_updater_satellite_version", i).apply();
            return true;
        } catch (Exception e) {
            ploge("updateSharedPreferencesSatelliteAccessConfigVersion error: " + e);
            return false;
        }
    }

    private void persistLatestSatelliteCommunicationAllowedState() {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            ploge("persistLatestSatelliteCommunicationAllowedState: mSharedPreferences is null");
            return;
        }
        try {
            sharedPreferences.edit().putLong("latest_satellite_communication_allowed_set_time", this.mLatestSatelliteCommunicationAllowedSetTime.get()).apply();
            this.mSharedPreferences.edit().putBoolean("latest_satellite_communication_allowed", this.mLatestSatelliteCommunicationAllowed.get()).apply();
        } catch (Exception e) {
            ploge("persistLatestSatelliteCommunicationAllowedState error : " + e);
        }
    }

    private void updateSatelliteAccessDataWithConfigUpdaterData(Context context) {
        plogd("updateSatelliteAccessDataWithConfigUpdaterData");
        SatelliteConfig satelliteConfig = this.mSatelliteController.getSatelliteConfig();
        if (satelliteConfig == null) {
            ploge("updateSatelliteAccessDataWithConfigUpdaterData: satelliteConfig is null");
            this.mConfigUpdaterMetricsStats.reportOemAndCarrierConfigError(5);
            return;
        }
        int satelliteConfigDataVersion = satelliteConfig.getSatelliteConfigDataVersion();
        if (satelliteConfigDataVersion <= 0) {
            plogd("updateSatelliteAccessDataWithConfigUpdaterData: version is invalid: " + satelliteConfigDataVersion);
            return;
        }
        List deviceSatelliteCountryCodes = satelliteConfig.getDeviceSatelliteCountryCodes();
        if (!isValidCountryCodes(deviceSatelliteCountryCodes)) {
            plogd("updateSatelliteAccessDataWithConfigUpdaterData: country codes is invalid");
            this.mConfigUpdaterMetricsStats.reportOemConfigError(9);
            return;
        }
        Boolean boolIsSatelliteDataForAllowedRegion = satelliteConfig.isSatelliteDataForAllowedRegion();
        if (boolIsSatelliteDataForAllowedRegion == null) {
            ploge("updateSatelliteAccessDataWithConfigUpdaterData: Satellite isSatelliteDataForAllowedRegion is null ");
            this.mConfigUpdaterMetricsStats.reportOemConfigError(10);
            return;
        }
        File satelliteS2CellFile = satelliteConfig.getSatelliteS2CellFile(context);
        if (satelliteS2CellFile == null || !satelliteS2CellFile.exists()) {
            plogd("updateSatelliteAccessDataWithConfigUpdaterData: configUpdaterS2CellFile is not exist");
            this.mConfigUpdaterMetricsStats.reportOemConfigError(10);
            return;
        }
        if (!isS2CellFileValid(satelliteS2CellFile)) {
            ploge("updateSatelliteAccessDataWithConfigUpdaterData: the configUpdaterS2CellFile is not valid");
            this.mConfigUpdaterMetricsStats.reportOemConfigError(10);
            return;
        }
        File satelliteAccessConfigJsonFile = satelliteConfig.getSatelliteAccessConfigJsonFile(context);
        if (satelliteAccessConfigJsonFile == null || !satelliteAccessConfigJsonFile.exists()) {
            plogd("updateSatelliteAccessDataWithConfigUpdaterData: satellite_access_config.json does not exist");
            this.mConfigUpdaterMetricsStats.reportOemConfigError(12);
            return;
        }
        try {
            if (SatelliteAccessConfigurationParser.parse(satelliteAccessConfigJsonFile.getAbsolutePath(), this.mFeatureFlags) == null) {
                ploge("updateSatelliteAccessDataWithConfigUpdaterData: the satellite_access_config.json is not valid");
                this.mConfigUpdaterMetricsStats.reportOemConfigError(12);
                return;
            }
        } catch (Exception e) {
            loge("updateSatelliteAccessDataWithConfigUpdaterData: the satellite_access_config.json parse error " + e);
        }
        File fileCopyFileToLocalDirectory = copyFileToLocalDirectory(satelliteS2CellFile, "config_updater_sat_s2.dat");
        if (fileCopyFileToLocalDirectory == null) {
            ploge("updateSatelliteAccessDataWithConfigUpdaterData: fail to copy localS2CellFile");
            this.mConfigUpdaterMetricsStats.reportOemConfigError(11);
            return;
        }
        File fileCopyFileToLocalDirectory2 = copyFileToLocalDirectory(satelliteAccessConfigJsonFile, "config_updater_satellite_access_config.json");
        if (fileCopyFileToLocalDirectory2 == null) {
            ploge("updateSatelliteAccessDataWithConfigUpdaterData: fail to copy localSatelliteAccessConfigFile");
            this.mConfigUpdaterMetricsStats.reportOemConfigError(11);
            fileCopyFileToLocalDirectory.delete();
            return;
        }
        if (!updateSharedPreferencesCountryCodes(context, deviceSatelliteCountryCodes)) {
            ploge("updateSatelliteAccessDataWithConfigUpdaterData: fail to copy country coeds into shared preferences");
            fileCopyFileToLocalDirectory.delete();
            fileCopyFileToLocalDirectory2.delete();
            this.mConfigUpdaterMetricsStats.reportOemConfigError(11);
            return;
        }
        if (!updateSharedPreferencesIsAllowAccessControl(context, boolIsSatelliteDataForAllowedRegion.booleanValue())) {
            ploge("updateSatelliteAccessDataWithConfigUpdaterData: fail to copy isSatelliteDataForAllowedRegion into shared preferences");
            fileCopyFileToLocalDirectory.delete();
            fileCopyFileToLocalDirectory2.delete();
            deleteSharedPreferencesbyKey(context, "config_updater_satellite_country_codes");
            this.mConfigUpdaterMetricsStats.reportOemConfigError(11);
            return;
        }
        if (!updateSharedPreferencesSatelliteAccessConfigVersion(context, satelliteConfigDataVersion)) {
            ploge("updateSatelliteAccessDataWithConfigUpdaterData: fail to copy satelliteAccessConfigVersion into shared preferences");
            fileCopyFileToLocalDirectory.delete();
            fileCopyFileToLocalDirectory2.delete();
            deleteSharedPreferencesbyKey(context, "config_updater_satellite_country_codes");
            deleteSharedPreferencesbyKey(context, "config_updater_satellite_is_allow_access_control");
            this.mConfigUpdaterMetricsStats.reportOemConfigError(11);
            return;
        }
        this.mSatelliteAccessConfigVersion.set(satelliteConfigDataVersion);
        setSatelliteS2CellFile(fileCopyFileToLocalDirectory);
        setSatelliteAccessConfigFile(fileCopyFileToLocalDirectory2);
        setSatelliteCountryCodes(deviceSatelliteCountryCodes);
        this.mIsSatelliteAllowAccessControl.set(satelliteConfig.isSatelliteDataForAllowedRegion().booleanValue());
        plogd("mSatelliteAccessConfigVersion=" + this.mSatelliteAccessConfigVersion.get() + ", Use s2 cell file=" + getSatelliteS2CellFile().getAbsolutePath() + ", mSatelliteAccessConfigFile=" + getSatelliteAccessConfigFile().getAbsolutePath() + ", country codes=" + getSatelliteCountryCodesString() + ", mIsSatelliteAllowAccessControl=" + this.mIsSatelliteAllowAccessControl.get() + " from ConfigUpdater");
        cleanupOnDeviceAccessControllerResources();
        synchronized (this.mLock) {
            plogd("clear mCachedAccessRestrictionMap");
            this.mCachedAccessRestrictionMap.clear();
        }
        this.mConfigUpdaterMetricsStats.reportConfigUpdateSuccess();
        sendRequestAsync(10, null);
    }

    protected void loadOverlayConfigs(Context context) {
        plogd("loadOverlayConfigs");
        setSatelliteCountryCodes(getSatelliteCountryCodesFromOverlayConfig(context));
        this.mIsSatelliteAllowAccessControl.set(getSatelliteAccessAllowFromOverlayConfig(context));
        String satelliteS2CellFileFromOverlayConfig = getSatelliteS2CellFileFromOverlayConfig(context);
        File file = null;
        File file2 = TextUtils.isEmpty(satelliteS2CellFileFromOverlayConfig) ? null : new File(satelliteS2CellFileFromOverlayConfig);
        if (file2 != null && !file2.exists()) {
            ploge("The satellite S2 cell file " + satelliteS2CellFileFromOverlayConfig + " does not exist");
            file2 = null;
        }
        setSatelliteS2CellFile(file2);
        String satelliteConfigurationFileNameFromOverlayConfig = getSatelliteConfigurationFileNameFromOverlayConfig(context);
        File file3 = TextUtils.isEmpty(satelliteConfigurationFileNameFromOverlayConfig) ? null : new File(satelliteConfigurationFileNameFromOverlayConfig);
        if (file3 == null || file3.exists()) {
            file = file3;
        } else {
            ploge("The satellite access config file " + satelliteConfigurationFileNameFromOverlayConfig + " does not exist");
        }
        setSatelliteAccessConfigFile(file);
        this.mLocationFreshDurationNanos.set(getSatelliteLocationFreshDurationFromOverlayConfig(context));
        this.mAccessControllerMetricsStats.setConfigDataSource(4);
        this.mRetryIntervalToEvaluateUserInSatelliteAllowedRegion.set(getDelayBeforeRetryValidatingPossibleChangeInSatelliteAllowedRegionMillis(context));
        this.mMaxRetryCountForValidatingPossibleChangeInAllowedRegion.set(getMaxRetryCountForValidatingPossibleChangeInAllowedRegion(context));
        this.mLocationQueryThrottleIntervalNanos.set(getLocationQueryThrottleIntervalNanos(context));
    }

    protected void loadSatelliteAccessConfiguration() {
        String satelliteConfigurationFileNameFromOverlayConfig;
        logd("loadSatelliteAccessConfiguration");
        File satelliteAccessConfigFile = getSatelliteAccessConfigFile();
        if (satelliteAccessConfigFile != null) {
            satelliteConfigurationFileNameFromOverlayConfig = satelliteAccessConfigFile.getAbsolutePath();
        } else {
            logd("loadSatelliteAccessConfiguration:");
            satelliteConfigurationFileNameFromOverlayConfig = getSatelliteConfigurationFileNameFromOverlayConfig(this.mContext);
        }
        loadSatelliteAccessConfigurationFileToMap(satelliteConfigurationFileNameFromOverlayConfig);
    }

    protected void loadSatelliteAccessConfigurationFileToMap(String str) {
        logd("loadSatelliteAccessConfigurationFileToMap: " + str);
        if (!TextUtils.isEmpty(str)) {
            try {
                setSatelliteAccessConfigMap(SatelliteAccessConfigurationParser.parse(str, this.mFeatureFlags));
                return;
            } catch (Exception e) {
                loge("loadSatelliteAccessConfigurationFileToMap: failed load json file: " + e);
                return;
            }
        }
        loge("loadSatelliteAccessConfigurationFileToMap: fileName is empty");
    }

    private void loadConfigUpdaterConfigs() {
        plogd("loadConfigUpdaterConfigs");
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            ploge("loadConfigUpdaterConfigs : mSharedPreferences is null");
            return;
        }
        int i = sharedPreferences.getInt("config_updater_satellite_version", 0);
        if (i <= 0) {
            ploge("loadConfigUpdaterConfigs: satelliteConfigVersion is invalid: " + i);
            return;
        }
        Set<String> stringSet = this.mSharedPreferences.getStringSet("config_updater_satellite_country_codes", null);
        if (stringSet == null || stringSet.isEmpty()) {
            ploge("loadConfigUpdaterConfigs: configupdater country codes are either null or empty");
            return;
        }
        boolean z = this.mSharedPreferences.getBoolean("config_updater_satellite_is_allow_access_control", true);
        File configUpdaterSatelliteConfigFileFromLocalDirectory = getConfigUpdaterSatelliteConfigFileFromLocalDirectory("config_updater_sat_s2.dat");
        if (configUpdaterSatelliteConfigFileFromLocalDirectory == null) {
            ploge("loadConfigUpdaterConfigs: s2CellFile is null");
            return;
        }
        File configUpdaterSatelliteConfigFileFromLocalDirectory2 = getConfigUpdaterSatelliteConfigFileFromLocalDirectory("config_updater_satellite_access_config.json");
        if (configUpdaterSatelliteConfigFileFromLocalDirectory2 == null) {
            ploge("satelliteAccessConfigJsonFile is null");
            return;
        }
        this.mSatelliteAccessConfigVersion.set(i);
        setSatelliteS2CellFile(configUpdaterSatelliteConfigFileFromLocalDirectory);
        setSatelliteAccessConfigFile(configUpdaterSatelliteConfigFileFromLocalDirectory2);
        setSatelliteCountryCodes((List) stringSet.stream().collect(Collectors.toList()));
        this.mIsSatelliteAllowAccessControl.set(z);
        plogd("loadConfigUpdaterConfigs: use satellite config data from configupdater:  mSatelliteAccessConfigVersion=" + this.mSatelliteAccessConfigVersion.get() + ", Use s2 cell file=" + getSatelliteS2CellFile().getAbsolutePath() + ", mSatelliteAccessConfigFile=" + getSatelliteAccessConfigFile().getAbsolutePath() + ", country codes=" + getSatelliteCountryCodesString() + ", mIsSatelliteAllowAccessControl=" + this.mIsSatelliteAllowAccessControl.get() + " from ConfigUpdater");
        this.mAccessControllerMetricsStats.setConfigDataSource(2);
    }

    private long getLocationFreshDurationNanos() {
        if (this.mIsOverlayConfigOverridden.get()) {
            return this.mOverriddenLocationFreshDurationNanos.get();
        }
        return this.mLocationFreshDurationNanos.get();
    }

    private void setSatelliteCountryCodes(List list) {
        synchronized (this.mLock) {
            this.mSatelliteCountryCodes = list;
        }
    }

    public List getSatelliteCountryCodes() {
        synchronized (this.mLock) {
            try {
                if (this.mIsOverlayConfigOverridden.get()) {
                    return this.mOverriddenSatelliteCountryCodes;
                }
                return this.mSatelliteCountryCodes;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private String getSatelliteCountryCodesString() {
        String strJoin;
        synchronized (this.mLock) {
            strJoin = String.join(",", getSatelliteCountryCodes());
        }
        return strJoin;
    }

    private void setSatelliteS2CellFile(File file) {
        synchronized (this.mLock) {
            this.mSatelliteS2CellFile = file;
        }
    }

    public File getSatelliteS2CellFile() {
        synchronized (this.mLock) {
            try {
                if (this.mIsOverlayConfigOverridden.get()) {
                    return this.mOverriddenSatelliteS2CellFile;
                }
                return this.mSatelliteS2CellFile;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void setSatelliteAccessConfigFile(File file) {
        synchronized (this.mLock) {
            this.mSatelliteAccessConfigFile = file;
        }
    }

    public File getSatelliteAccessConfigFile() {
        synchronized (this.mLock) {
            try {
                if (this.mIsOverlayConfigOverridden.get()) {
                    logd("mIsOverlayConfigOverridden: " + this.mIsOverlayConfigOverridden.get());
                    return this.mOverriddenSatelliteAccessConfigFile;
                }
                if (this.mSatelliteAccessConfigFile != null) {
                    logd("getSatelliteAccessConfigFile path: " + this.mSatelliteAccessConfigFile.getAbsoluteFile());
                }
                return this.mSatelliteAccessConfigFile;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean isSatelliteAllowAccessControl() {
        if (this.mIsOverlayConfigOverridden.get()) {
            return this.mOverriddenIsSatelliteAllowAccessControl.get();
        }
        return this.mIsSatelliteAllowAccessControl.get();
    }

    private void handleCmdIsSatelliteAllowedForCurrentLocation(CheckingAllowedStateRequestArguments checkingAllowedStateRequestArguments) {
        this.mCheckingAllowedStateRequests.add(checkingAllowedStateRequestArguments);
        if (this.mCheckingAllowedStateRequests.size() > 1) {
            plogd("requestIsCommunicationAllowedForCurrentLocation is already being processed");
        } else {
            this.mTotalCheckingStartTimeMillis.set(System.currentTimeMillis());
            this.mSatelliteController.requestIsSatelliteSupported(this.mInternalSatelliteSupportedResultReceiver);
        }
    }

    private void handleWaitForCurrentLocationTimedOutEvent() {
        plogd("Timed out to wait for current location");
        CancellationSignal cancellationSignal = this.mLocationRequestCancellationSignal;
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            this.mLocationRequestCancellationSignal = null;
            onCurrentLocationAvailable(null);
            return;
        }
        ploge("handleWaitForCurrentLocationTimedOutEvent: mLocationRequestCancellationSignal is null");
    }

    private void registerDefaultSmsAppChangedBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme(f.a.e);
        context.registerReceiver(this.mDefaultSmsAppChangedBroadcastReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED_INTERNAL");
        context.registerReceiver(this.mDefaultSmsAppChangedBroadcastReceiver, intentFilter2);
    }

    private void registerLocationModeChangedBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.MODE_CHANGED");
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");
        context.registerReceiver(this.mLocationModeChangedBroadcastReceiver, intentFilter);
    }

    private boolean isRegionDisallowed(List list) {
        if (list.isEmpty()) {
            plogd("isRegionDisallowed : false : network country code is not available");
            return false;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (isSatelliteAccessAllowedForLocation(List.of(str))) {
                plogd("isRegionDisallowed : false : Country Code " + str + " is allowed but not sure if current location should be allowed.");
                return false;
            }
        }
        plogd("isRegionDisallowed : true : " + list);
        return true;
    }

    protected void handleIsSatelliteSupportedResult(int i, Bundle bundle) {
        plogd("handleIsSatelliteSupportedResult: resultCode=" + i);
        if (i == 0) {
            if (bundle.containsKey("satellite_supported")) {
                if (!bundle.getBoolean("satellite_supported")) {
                    plogd("Satellite is not supported");
                    Bundle bundle2 = new Bundle();
                    bundle2.putBoolean("satellite_communication_allowed", false);
                    sendSatelliteAllowResultToReceivers(20, bundle2, false);
                    return;
                }
                plogd("Satellite is supported");
                List currentNetworkCountryIso = this.mCountryDetector.getCurrentNetworkCountryIso();
                if (isRegionDisallowed(currentNetworkCountryIso)) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putBoolean("satellite_communication_allowed", false);
                    this.mAccessControllerMetricsStats.setAccessControlType(1).setCountryCodes(currentNetworkCountryIso);
                    sendSatelliteAllowResultToReceivers(0, bundle3, false);
                    return;
                }
                checkSatelliteAccessRestrictionUsingGPS();
                return;
            }
            ploge("KEY_SATELLITE_SUPPORTED does not exist.");
            sendSatelliteAllowResultToReceivers(i, bundle, false);
            return;
        }
        sendSatelliteAllowResultToReceivers(i, bundle, false);
    }

    private void handleSatelliteSupportedStateChanged(boolean z) {
        plogd("handleSatelliteSupportedStateChanged: isSupported=" + z);
        if (z) {
            requestIsCommunicationAllowedForCurrentLocation(new ResultReceiver(null) { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.9
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i, Bundle bundle) {
                    SatelliteAccessController.this.mSatelliteController.decrementResultReceiverCount("SAC:onSatelliteSupportedStateChanged");
                }
            }, false);
            this.mSatelliteController.incrementResultReceiverCount("SAC:onSatelliteSupportedStateChanged");
            if (isReasonPresentInSatelliteDisallowedReasons(0)) {
                removeReasonFromSatelliteDisallowedReasons(0);
                handleEventDisallowedReasonsChanged();
                return;
            }
            return;
        }
        if (isReasonPresentInSatelliteDisallowedReasons(0)) {
            return;
        }
        addReasonToSatelliteDisallowedReasons(0);
        handleEventDisallowedReasonsChanged();
    }

    private void handleIsSatelliteProvisionedResult(int i, Bundle bundle) {
        plogd("handleIsSatelliteProvisionedResult: resultCode=" + i);
        if (i == 0) {
            if (bundle.containsKey("satellite_provisioned")) {
                if (!bundle.getBoolean("satellite_provisioned")) {
                    plogd("Satellite is not provisioned");
                    Bundle bundle2 = new Bundle();
                    bundle2.putBoolean("satellite_communication_allowed", false);
                    sendSatelliteAllowResultToReceivers(i, bundle2, false);
                    return;
                }
                plogd("Satellite is provisioned");
                checkSatelliteAccessRestrictionUsingGPS();
                return;
            }
            ploge("KEY_SATELLITE_PROVISIONED does not exist.");
            sendSatelliteAllowResultToReceivers(i, bundle, false);
            return;
        }
        sendSatelliteAllowResultToReceivers(i, bundle, false);
    }

    private void handleSatelliteProvisionedStateChanged(boolean z) {
        plogd("handleSatelliteProvisionedStateChanged: isProvisioned=" + z);
        if (z) {
            this.mIsProvisionEligibleForNotification.set(true);
            requestIsCommunicationAllowedForCurrentLocation(new ResultReceiver(null) { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.10
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i, Bundle bundle) {
                    SatelliteAccessController.this.mSatelliteController.decrementResultReceiverCount("SAC:onSatelliteProvisionStateChanged");
                }
            }, false);
            this.mSatelliteController.incrementResultReceiverCount("SAC:onSatelliteProvisionStateChanged");
            if (isReasonPresentInSatelliteDisallowedReasons(1)) {
                removeReasonFromSatelliteDisallowedReasons(1);
                handleEventDisallowedReasonsChanged();
                return;
            }
            return;
        }
        if (isReasonPresentInSatelliteDisallowedReasons(1)) {
            return;
        }
        addReasonToSatelliteDisallowedReasons(1);
        handleEventDisallowedReasonsChanged();
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0063  */
    private void sendSatelliteAllowResultToReceivers(int i, Bundle bundle, boolean z) {
        plogd("sendSatelliteAllowResultToReceivers : resultCode is " + i);
        boolean z2 = false;
        if (i == 0) {
            updateCurrentSatelliteAllowedState(z, false);
            this.mIsCurrentLocationEligibleForNotification.set(true);
        } else if (i == 25) {
            updateCurrentSatelliteAllowedState(z, true);
        }
        Iterator it = this.mCheckingAllowedStateRequests.iterator();
        while (it.hasNext()) {
            ((CheckingAllowedStateRequestArguments) it.next()).getResultReceiver().send(i, bundle);
            this.mSatelliteController.decrementResultReceiverCount("SAC:requestIsCommunicationAllowedForCurrentLocation");
        }
        this.mCheckingAllowedStateRequests.clear();
        if (!shouldRetryValidatingPossibleChangeInAllowedRegion(i)) {
            setIsSatelliteAllowedRegionPossiblyChanged(false);
        }
        int disallowedReason = getDisallowedReason(i, z);
        if (disallowedReason != -1) {
            if (!isReasonPresentInSatelliteDisallowedReasons(disallowedReason)) {
                z2 = true;
            }
        } else {
            if (isSatelliteDisallowedReasonsEmpty() && !hasAlreadyNotified("available_notification_shown")) {
                z2 = true;
            }
            if (isReasonPresentInSatelliteDisallowedReasons(2) || isReasonPresentInSatelliteDisallowedReasons(4)) {
                z2 = true;
            }
        }
        removeAllReasonsFromSatelliteDisallowedReasons(DISALLOWED_REASONS_TO_BE_RESET);
        if (disallowedReason != -1) {
            addReasonToSatelliteDisallowedReasons(disallowedReason);
        }
        if (z2) {
            handleEventDisallowedReasonsChanged();
        }
        reportMetrics(i, z);
    }

    private void handleEventDisallowedReasonsChanged() {
        if (this.mNotificationManager == null) {
            plogd("showSatelliteSystemNotification: NotificationManager is null");
            return;
        }
        plogd("getSatelliteDisallowedReasons: satelliteDisallowedReasons:" + String.join(", ", getSatelliteDisallowedReasonsCopy().toString()));
        notifySatelliteDisallowedReasonsChanged();
        if (this.mSatelliteController.isSatelliteSystemNotificationsEnabled(1) && this.mIsCurrentLocationEligibleForNotification.get() && this.mIsProvisionEligibleForNotification.get()) {
            showSatelliteSystemNotification();
            return;
        }
        plogd("mSatelliteDisallowedReasons: CurrentLocationAvailable: " + this.mIsCurrentLocationEligibleForNotification.get() + " SatelliteProvision: " + this.mIsProvisionEligibleForNotification.get());
        if (hasAlreadyNotified("unavailable_notification_shown")) {
            this.mNotificationManager.cancel("unavailable_notification_tag", 1);
        }
        if (hasAlreadyNotified("available_notification_shown")) {
            this.mNotificationManager.cancel("available_notification_tag", 1);
        }
    }

    private void showSatelliteSystemNotification() {
        if (this.mNotificationManager == null) {
            logd("showSatelliteSystemNotification: NotificationManager is null");
            return;
        }
        boolean zIsSatelliteDisallowedReasonsEmpty = isSatelliteDisallowedReasonsEmpty();
        NotificationManager notificationManager = this.mNotificationManager;
        if (zIsSatelliteDisallowedReasonsEmpty) {
            notificationManager.cancel("unavailable_notification_tag", 1);
            if (!hasAlreadyNotified("available_notification_shown")) {
                this.mNotificationManager.notifyAsUser("available_notification_tag", 1, this.mSatelliteAvailableNotification, UserHandle.ALL);
                markAsNotified("available_notification_shown", true);
                markAsNotified("unavailable_notification_shown", false);
                logd("showSatelliteSystemNotification: Notification is shown available_notification_shown");
                return;
            }
            logd("showSatelliteSystemNotification: Notification is not shown available_notification_shown = " + hasAlreadyNotified("available_notification_shown"));
            return;
        }
        notificationManager.cancel("available_notification_tag", 1);
        for (Integer num : getSatelliteDisallowedReasonsCopy()) {
            if (!hasAlreadyNotified("unavailable_notification_shown")) {
                this.mNotificationManager.notifyAsUser("unavailable_notification_tag", 1, (Notification) this.mSatelliteUnAvailableNotifications.get(num), UserHandle.ALL);
                markAsNotified("unavailable_notification_shown", true);
                markAsNotified("available_notification_shown", false);
                logd("showSatelliteSystemNotification: Notification is shown unavailable_notification_shown");
                return;
            }
            logd("showSatelliteSystemNotification: Notification is not shown unavailable_notification_shown");
        }
    }

    private boolean hasAlreadyNotified(String str) {
        return this.mSharedPreferences.getBoolean(str, false);
    }

    private void markAsNotified(String str, boolean z) {
        this.mSharedPreferences.edit().putBoolean(str, z).apply();
    }

    private void checkSharedPreference() {
        Arrays.stream(new String[]{"config_updater_satellite_is_allow_access_control", "latest_satellite_communication_allowed", "available_notification_shown", "unavailable_notification_shown"}).forEach(new Consumer() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$checkSharedPreference$1((String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkSharedPreference$1(String str) {
        try {
            this.mSharedPreferences.getBoolean(str, false);
        } catch (ClassCastException unused) {
            this.mSharedPreferences.edit().remove(str).apply();
        }
    }

    private void initializeHandlerForSatelliteAllowedResult() {
        this.mHandlerForSatelliteAllowedResult = new ResultReceiver(null) { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController.11
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                SatelliteAccessController.this.plogd("query satellite allowed for current location, resultCode=" + i + ", resultData=" + bundle);
                if (SatelliteAccessController.this.shouldRetryValidatingPossibleChangeInAllowedRegion(i) && SatelliteAccessController.this.mRetryCountForValidatingPossibleChangeInAllowedRegion.get() < SatelliteAccessController.this.mMaxRetryCountForValidatingPossibleChangeInAllowedRegion.get()) {
                    int iIncrementAndGet = SatelliteAccessController.this.mRetryCountForValidatingPossibleChangeInAllowedRegion.incrementAndGet();
                    SatelliteAccessController.this.plogd("mRetryCountForValidatingPossibleChangeInAllowedRegion is " + iIncrementAndGet);
                    CheckingAllowedStateRequestArguments checkingAllowedStateRequestArguments = new CheckingAllowedStateRequestArguments(Integer.MAX_VALUE, SatelliteAccessController.this.mHandlerForSatelliteAllowedResult, false);
                    SatelliteAccessController satelliteAccessController = SatelliteAccessController.this;
                    satelliteAccessController.sendDelayedRequestAsync(1, checkingAllowedStateRequestArguments, satelliteAccessController.mRetryIntervalToEvaluateUserInSatelliteAllowedRegion.get());
                    return;
                }
                SatelliteAccessController.this.mRetryCountForValidatingPossibleChangeInAllowedRegion.set(0);
                SatelliteAccessController.this.plogd("Stop retry validating the possible change in satellite allowed region");
            }
        };
    }

    private void initializeSatelliteSystemNotification(Context context) {
        NotificationChannel notificationChannel = new NotificationChannel("satellite", "satelliteChannel", 3);
        notificationChannel.setSound(null, null);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mNotificationManager = notificationManager;
        if (notificationManager == null) {
            ploge("initializeSatelliteSystemNotification: notificationManager is null");
            return;
        }
        notificationManager.createNotificationChannel(notificationChannel);
        createAvailableNotifications(context);
        createUnavailableNotifications(context);
    }

    private Notification createNotification(Context context, String str, String str2) {
        return new Notification.Builder(context).setContentTitle(str).setContentText(str2).setSmallIcon(R.drawable.ic_btn_square_browser_zoom_page_overview_normal).setChannelId("satellite").setAutoCancel(true).setColor(context.getColor(R.color.system_notification_accent_color)).setVisibility(1).setLocalOnly(true).build();
    }

    private void createAvailableNotifications(Context context) {
        int i;
        int i2;
        if (this.mSatelliteController.isSatelliteServiceSupportedByCarrier(this.mSatelliteController.getSelectedSatelliteSubId(), 3)) {
            i = R.string.wfcSpnFormat_wlan_call;
            i2 = R.string.wfcSpnFormat_wifi_calling_wo_hyphen;
        } else {
            i = R.string.whichOpenLinksWith;
            i2 = R.string.whichOpenHostLinksWithApp;
        }
        this.mSatelliteAvailableNotification = createNotification(context, context.getResources().getString(i), context.getResources().getString(i2));
    }

    private void createUnavailableNotifications(Context context) {
        HashMap map;
        if (this.mSatelliteController.isSatelliteServiceSupportedByCarrier(this.mSatelliteController.getSelectedSatelliteSubId(), 3)) {
            map = SATELLITE_MESSAGING_UNAVAILABLE_REASONS;
        } else {
            map = SATELLITE_SOS_UNAVAILABLE_REASONS;
        }
        for (Integer num : map.keySet()) {
            num.intValue();
            Pair pair = (Pair) map.getOrDefault(num, null);
            if (pair != null) {
                this.mSatelliteUnAvailableNotifications.put(num, createNotification(context, context.getResources().getString(((Integer) pair.first).intValue()), context.getResources().getString(((Integer) pair.second).intValue())));
            }
        }
    }

    private void evaluatePossibleChangeInDefaultSmsApp(Context context) {
        boolean zContains;
        ComponentName defaultSmsApplicationAsUser = SmsApplication.getDefaultSmsApplicationAsUser(context, true, context.getUser());
        plogd("Current default SMS app:" + defaultSmsApplicationAsUser);
        if (defaultSmsApplicationAsUser != null) {
            String packageName = defaultSmsApplicationAsUser.getPackageName();
            SatelliteController satelliteController = this.mSatelliteController;
            List satelliteSupportedMsgApps = satelliteController.getSatelliteSupportedMsgApps(satelliteController.getSelectedSatelliteSubId());
            plogd("supportedMsgApps:" + String.join(", ", satelliteSupportedMsgApps));
            zContains = satelliteSupportedMsgApps.contains(packageName);
        } else {
            plogd("No default SMS app");
            zContains = false;
        }
        if (zContains) {
            if (isReasonPresentInSatelliteDisallowedReasons(3)) {
                removeReasonFromSatelliteDisallowedReasons(3);
                handleEventDisallowedReasonsChanged();
                return;
            }
            return;
        }
        if (isReasonPresentInSatelliteDisallowedReasons(3)) {
            return;
        }
        addReasonToSatelliteDisallowedReasons(3);
        handleEventDisallowedReasonsChanged();
    }

    private void handleSatelliteAllowedRegionPossiblyChanged(int i) {
        logd("handleSatelliteAllowedRegionPossiblyChanged");
        setIsSatelliteAllowedRegionPossiblyChanged(true);
        int i2 = 0;
        requestIsCommunicationAllowedForCurrentLocation(this.mHandlerForSatelliteAllowedResult, false);
        if (i == 6) {
            i2 = 3;
        } else if (i == 5) {
            i2 = 2;
        } else if (i == 8) {
            i2 = 4;
        } else if (i == 10) {
            i2 = 5;
        }
        this.mAccessControllerMetricsStats.setTriggeringEvent(i2);
    }

    protected boolean allowLocationQueryForSatelliteAllowedCheck() {
        if (!isCommunicationAllowedCacheValid()) {
            logd("allowLocationQueryForSatelliteAllowedCheck: cache is not valid");
            return true;
        }
        if (isSatelliteAllowedRegionPossiblyChanged() && !isLocationQueryThrottled()) {
            logd("allowLocationQueryForSatelliteAllowedCheck: location query is not throttled");
            return true;
        }
        logd("allowLocationQueryForSatelliteAllowedCheck: false");
        return false;
    }

    private boolean isLocationQueryThrottled() {
        if (this.mLastLocationQueryForPossibleChangeInAllowedRegionTimeNanos.get() == 0) {
            plogv("isLocationQueryThrottled: mLastLocationQueryForPossibleChangeInAllowedRegionTimeNanos is 0, return false");
            return false;
        }
        if (getElapsedRealtimeNanos() - this.mLastLocationQueryForPossibleChangeInAllowedRegionTimeNanos.get() > this.mLocationQueryThrottleIntervalNanos.get()) {
            plogv("isLocationQueryThrottled: currentTime - mLastLocationQueryForPossibleChangeInAllowedRegionTimeNanos is bigger than " + this.mLocationQueryThrottleIntervalNanos.get() + " so return false");
            return false;
        }
        plogd("isLocationQueryThrottled : true");
        return true;
    }

    protected void checkSatelliteAccessRestrictionUsingGPS() {
        plogd("checkSatelliteAccessRestrictionUsingGPS:");
        if (SystemProperties.getBoolean("persist.radio.allow_mock_modem", false) && getSatelliteS2CellFile() == null) {
            plogv("Skip satellite access control for mock modem if s2 file not exist");
            Bundle bundle = new Bundle();
            bundle.putBoolean("satellite_communication_allowed", true);
            sendSatelliteAllowResultToReceivers(0, bundle, true);
            return;
        }
        if (isInEmergency()) {
            executeLocationQuery();
            return;
        }
        if (this.mLocationManager.isLocationEnabled()) {
            plogd("location query is allowed");
            if (allowLocationQueryForSatelliteAllowedCheck() || shouldIgnoreAllowedStateCache()) {
                executeLocationQuery();
                return;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putBoolean("satellite_communication_allowed", this.mLatestSatelliteCommunicationAllowed.get());
            sendSatelliteAllowResultToReceivers(0, bundle2, this.mLatestSatelliteCommunicationAllowed.get());
            return;
        }
        plogd("location query is not allowed");
        Bundle bundle3 = new Bundle();
        bundle3.putBoolean("satellite_communication_allowed", false);
        sendSatelliteAllowResultToReceivers(25, bundle3, false);
    }

    private boolean isCommunicationAllowedCacheValid() {
        if (this.mIsAllowedStateCacheDisabledForCtsTest.get()) {
            logd("isCommunicationAllowedCacheValid: cache is disabled for CTS test");
            return false;
        }
        if (this.mLatestSatelliteCommunicationAllowedSetTime.get() > 0 && getElapsedRealtimeNanos() - this.mLatestSatelliteCommunicationAllowedSetTime.get() <= ALLOWED_STATE_CACHE_VALID_DURATION_NANOS) {
            logv("isCommunicationAllowedCacheValid: cache is valid");
            return true;
        }
        logv("isCommunicationAllowedCacheValid: cache is expired");
        return false;
    }

    private void executeLocationQuery() {
        plogd("executeLocationQuery");
        this.mFreshLastKnownLocation = getFreshLastKnownLocation();
        checkSatelliteAccessRestrictionUsingOnDeviceData();
    }

    private void checkSatelliteAccessRestrictionUsingOnDeviceData() {
        this.mOnDeviceLookupStartTimeMillis.set(System.currentTimeMillis());
        plogd("Use on-device data");
        if (this.mFreshLastKnownLocation != null) {
            plogd("Use fresh last known location");
            this.mAccessControllerMetricsStats.setAccessControlType(3);
            checkSatelliteAccessRestrictionForLocation(this.mFreshLastKnownLocation);
            this.mFreshLastKnownLocation = null;
            return;
        }
        Location freshLastKnownLocation = getFreshLastKnownLocation();
        if (freshLastKnownLocation != null) {
            plogd("Use fresh last known location");
            this.mAccessControllerMetricsStats.setAccessControlType(3);
            checkSatelliteAccessRestrictionForLocation(freshLastKnownLocation);
            return;
        }
        queryCurrentLocation();
    }

    private void queryCurrentLocation() {
        if (this.mLocationRequestCancellationSignal != null) {
            plogd("queryCurrentLocation : Request for current location was already sent to LocationManager");
            return;
        }
        if (isSatelliteAllowedRegionPossiblyChanged()) {
            this.mLastLocationQueryForPossibleChangeInAllowedRegionTimeNanos.set(getElapsedRealtimeNanos());
            plogd("mLastLocationQueryForPossibleChangeInAllowedRegionTimeNanos is set " + this.mLastLocationQueryForPossibleChangeInAllowedRegionTimeNanos.get());
        }
        try {
            plogd("Querying current location");
            this.mLocationRequestCancellationSignal = new CancellationSignal();
            this.mLocationManager.getCurrentLocation("fused", new LocationRequest.Builder(0L).setQuality(100).setLocationSettingsIgnored(isInEmergency()).build(), this.mLocationRequestCancellationSignal, new Executor() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda4
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new Consumer() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.onCurrentLocationAvailable((Location) obj);
                }
            });
            this.mLocationQueryStartTimeMillis.set(System.currentTimeMillis());
            startWaitForCurrentLocationTimer();
        } catch (IllegalArgumentException e) {
            plogw("queryCurrentLocation: IllegalArgumentException: ex=" + e + ", mIsLocationProviderEnabled=" + this.mIsLocationProviderEnabled.get());
            onCurrentLocationAvailable(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCurrentLocationAvailable(Location location) {
        StringBuilder sb = new StringBuilder("onCurrentLocationAvailable ");
        sb.append(location != null);
        plogd(sb.toString());
        stopWaitForCurrentLocationTimer();
        this.mLocationRequestCancellationSignal = null;
        this.mAccessControllerMetricsStats.setLocationQueryTime(this.mLocationQueryStartTimeMillis.get());
        Bundle bundle = new Bundle();
        if (location != null) {
            plogd("onCurrentLocationAvailable: lat=" + Rlog.pii("SatelliteAccessController", Double.valueOf(location.getLatitude())) + ", long=" + Rlog.pii("SatelliteAccessController", Double.valueOf(location.getLongitude())));
            if (location.isMock() && !isMockModemAllowed()) {
                logd("location is mock");
                bundle.putBoolean("satellite_communication_allowed", false);
                sendSatelliteAllowResultToReceivers(0, bundle, false);
                return;
            } else {
                this.mAccessControllerMetricsStats.setAccessControlType(2);
                this.mControllerMetricsStats.reportLocationQuerySuccessful(true);
                checkSatelliteAccessRestrictionForLocation(location);
                this.mIsCurrentLocationEligibleForNotification.set(true);
                return;
            }
        }
        plogd("current location is not available");
        if (isCommunicationAllowedCacheValid()) {
            plogd("onCurrentLocationAvailable: cache is still valid, using it");
            bundle.putBoolean("satellite_communication_allowed", this.mLatestSatelliteCommunicationAllowed.get());
            sendSatelliteAllowResultToReceivers(0, bundle, this.mLatestSatelliteCommunicationAllowed.get());
            this.mIsCurrentLocationEligibleForNotification.set(true);
        } else {
            bundle.putBoolean("satellite_communication_allowed", false);
            sendSatelliteAllowResultToReceivers(26, bundle, false);
        }
        this.mControllerMetricsStats.reportLocationQuerySuccessful(false);
    }

    protected void checkSatelliteAccessRestrictionForLocation(Location location) {
        boolean zContainsKey;
        Integer num;
        try {
            plogd("checkSatelliteAccessRestrictionForLocation: checking satellite access restriction for location: lat - " + Rlog.pii("SatelliteAccessController", Double.valueOf(location.getLatitude())) + ", long - " + Rlog.pii("SatelliteAccessController", Double.valueOf(location.getLongitude())) + ", mS2Level - " + this.mS2Level.get());
            SatelliteOnDeviceAccessController.LocationToken locationTokenCreateLocationTokenForLatLng = SatelliteOnDeviceAccessController.createLocationTokenForLatLng(location.getLatitude(), location.getLongitude(), this.mS2Level.get());
            synchronized (this.mLock) {
                zContainsKey = this.mCachedAccessRestrictionMap.containsKey(locationTokenCreateLocationTokenForLatLng);
                num = (Integer) this.mCachedAccessRestrictionMap.get(locationTokenCreateLocationTokenForLatLng);
            }
            boolean z = true;
            if (zContainsKey) {
                setNewRegionalConfigId(num);
                if (getNewRegionalConfigId() == null) {
                    z = false;
                }
                plogd("mNewRegionalConfigId from mCachedAccessRestrictionMap is " + getNewRegionalConfigId());
            } else {
                if (!initSatelliteOnDeviceAccessController()) {
                    ploge("Failed to init SatelliteOnDeviceAccessController");
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("satellite_communication_allowed", false);
                    sendSatelliteAllowResultToReceivers(0, bundle, false);
                    return;
                }
                setNewRegionalConfigId(getSatelliteOnDeviceAccessController().getRegionalConfigIdForLocation(locationTokenCreateLocationTokenForLatLng));
                plogd("mNewRegionalConfigId from geofence file lookup is " + getNewRegionalConfigId());
                if (getNewRegionalConfigId() == null) {
                    z = false;
                }
                updateCachedAccessRestrictionMap(locationTokenCreateLocationTokenForLatLng, getNewRegionalConfigId());
            }
            this.mAccessControllerMetricsStats.setOnDeviceLookupTime(this.mOnDeviceLookupStartTimeMillis.get());
            StringBuilder sb = new StringBuilder("checkSatelliteAccessRestrictionForLocation: ");
            sb.append(z ? "Satellite Allowed" : "Satellite NOT Allowed");
            sb.append(" for location: lat - ");
            sb.append(Rlog.pii("SatelliteAccessController", Double.valueOf(location.getLatitude())));
            sb.append(", long - ");
            sb.append(Rlog.pii("SatelliteAccessController", Double.valueOf(location.getLongitude())));
            sb.append(", mS2Level - ");
            sb.append(this.mS2Level.get());
            plogd(sb.toString());
            Bundle bundle2 = new Bundle();
            bundle2.putBoolean("satellite_communication_allowed", z);
            sendSatelliteAllowResultToReceivers(0, bundle2, z);
            this.mLatestSatelliteCommunicationAllowed.set(z);
            this.mLatestSatelliteCommunicationAllowedSetTime.set(getElapsedRealtimeNanos());
            persistLatestSatelliteCommunicationAllowedState();
        } catch (Exception e) {
            ploge("checkSatelliteAccessRestrictionForLocation: ex=" + e);
            reportAnomaly("dbea1641-630e-4780-9f25-8337ba6c3563", "On-device satellite lookup exception");
            Bundle bundle3 = new Bundle();
            if (isCommunicationAllowedCacheValid()) {
                bundle3.putBoolean("satellite_communication_allowed", this.mLatestSatelliteCommunicationAllowed.get());
                plogd("checkSatelliteAccessRestrictionForLocation: cache is still valid, allowing satellite communication");
            } else {
                bundle3.putBoolean("satellite_communication_allowed", false);
                plogd("satellite communication not allowed");
            }
            sendSatelliteAllowResultToReceivers(0, bundle3, this.mLatestSatelliteCommunicationAllowed.get());
        }
    }

    private void updateRegionalConfigId() {
        Integer regionalConfigId = getRegionalConfigId();
        Integer newRegionalConfigId = getNewRegionalConfigId();
        plogd("mNewRegionalConfigId: updatedValue = " + newRegionalConfigId + " | mRegionalConfigId: beforeValue = " + regionalConfigId);
        if (Objects.equals(regionalConfigId, newRegionalConfigId)) {
            return;
        }
        setRegionalConfigId(newRegionalConfigId);
        notifyRegionalSatelliteConfigurationChanged(getCurrentSatelliteAccessConfiguration());
    }

    private void updateCachedAccessRestrictionMap(SatelliteOnDeviceAccessController.LocationToken locationToken, Integer num) {
        synchronized (this.mLock) {
            this.mCachedAccessRestrictionMap.put(locationToken, num);
        }
    }

    protected boolean isSatelliteAccessAllowedForLocation(List list) {
        if (isSatelliteAllowAccessControl()) {
            if (list.isEmpty()) {
                return false;
            }
            return getSatelliteCountryCodes().containsAll(list);
        }
        if (getSatelliteCountryCodes().isEmpty()) {
            return true;
        }
        if (list.isEmpty()) {
            return false;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (getSatelliteCountryCodes().contains((String) it.next())) {
                return false;
            }
        }
        return true;
    }

    private Location getFreshLastKnownLocation() {
        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation == null || getElapsedRealtimeNanos() - lastKnownLocation.getElapsedRealtimeNanos() > getLocationFreshDurationNanos()) {
            return null;
        }
        plogd("getFreshLastKnownLocation: lat=" + Rlog.pii("SatelliteAccessController", Double.valueOf(lastKnownLocation.getLatitude())) + ", long=" + Rlog.pii("SatelliteAccessController", Double.valueOf(lastKnownLocation.getLongitude())));
        return lastKnownLocation;
    }

    private boolean isInEmergency() {
        if (this.mTelecomManager.isInEmergencyCall()) {
            plogd("In emergency call");
            return true;
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.isInEcm()) {
                plogd("In emergency callback mode");
                return true;
            }
        }
        if (!this.mSatelliteController.isInEmergencyMode()) {
            return false;
        }
        plogd("In satellite emergency mode");
        return true;
    }

    private Location getLastKnownLocation() {
        Iterator<String> it = this.mLocationManager.getProviders(true).iterator();
        Location location = null;
        while (it.hasNext()) {
            Location lastKnownLocation = this.mLocationManager.getLastKnownLocation(it.next());
            if (lastKnownLocation != null && (location == null || location.getElapsedRealtimeNanos() < lastKnownLocation.getElapsedRealtimeNanos())) {
                location = lastKnownLocation;
            }
        }
        if (location == null || isMockModemAllowed() || !location.isMock()) {
            return location;
        }
        return null;
    }

    private void initSharedPreferences(Context context) {
        try {
            this.mSharedPreferences = context.getSharedPreferences("satellite_shared_pref", 0);
        } catch (Exception e) {
            ploge("Cannot get default shared preferences: " + e);
        }
    }

    private boolean initSatelliteOnDeviceAccessController() {
        plogd("initSatelliteOnDeviceAccessController");
        if (getSatelliteS2CellFile() == null) {
            return false;
        }
        if (getSatelliteOnDeviceAccessController() != null) {
            restartKeepOnDeviceAccessControllerResourcesTimer();
            return true;
        }
        try {
            setOnDeviceAccessController(SatelliteOnDeviceAccessController.create(getSatelliteS2CellFile(), this.mFeatureFlags));
            plogd("initSatelliteOnDeviceAccessController: initialized SatelliteOnDeviceAccessController");
            restartKeepOnDeviceAccessControllerResourcesTimer();
            this.mS2Level.set(getSatelliteOnDeviceAccessController().getS2Level());
            plogd("mS2Level=" + this.mS2Level.get());
            loadSatelliteAccessConfiguration();
            return true;
        } catch (Exception e) {
            ploge("Got exception in creating an instance of SatelliteOnDeviceAccessController, ex=" + e + ", sat s2 file=" + getSatelliteS2CellFile().getAbsolutePath());
            reportAnomaly("3ac767d8-2867-4d60-97c2-ae9d378a5521", "Exception in creating on-device satellite access controller");
            setOnDeviceAccessController(null);
            setSatelliteAccessConfigMap(null);
            if (!this.mIsOverlayConfigOverridden.get()) {
                setSatelliteS2CellFile(null);
            }
            return false;
        }
    }

    private void cleanupOnDeviceAccessControllerResources() {
        SatelliteOnDeviceAccessController satelliteOnDeviceAccessController = getSatelliteOnDeviceAccessController();
        StringBuilder sb = new StringBuilder("cleanupOnDeviceAccessControllerResources=");
        sb.append(satelliteOnDeviceAccessController != null);
        plogd(sb.toString());
        if (satelliteOnDeviceAccessController != null) {
            try {
                satelliteOnDeviceAccessController.close();
            } catch (Exception e) {
                ploge("cleanupOnDeviceAccessControllerResources: ex=" + e);
            }
            setOnDeviceAccessController(null);
            stopKeepOnDeviceAccessControllerResourcesTimer();
        }
    }

    private void handleCmdUpdateSystemSelectionChannels(ResultReceiver resultReceiver) {
        this.mUpdateSystemSelectionChannelsResultReceivers.add(resultReceiver);
        if (this.mUpdateSystemSelectionChannelsResultReceivers.size() > 1) {
            plogd("updateSystemSelectionChannels is already being processed");
            return;
        }
        int selectedSatelliteSubId = this.mSatelliteController.getSelectedSatelliteSubId();
        plogd("handleCmdUpdateSystemSelectionChannels: SatellitePhone subId: " + selectedSatelliteSubId);
        if (selectedSatelliteSubId == -1) {
            sendUpdateSystemSelectionChannelsResult(6, null);
            return;
        }
        SubscriptionInfo subscriptionInfo = SubscriptionManagerService.getInstance().getSubscriptionInfo(selectedSatelliteSubId);
        if (subscriptionInfo != null) {
            subscriptionInfo.getMccString();
            subscriptionInfo.getMncString();
        }
        Integer regionalConfigId = getRegionalConfigId();
        Integer[] numArr = {regionalConfigId};
        if (regionalConfigId != null && regionalConfigId.intValue() == -1) {
            numArr[0] = 0;
        }
        if (!SatelliteAccessConfigurationParser.isRegionalConfigIdValid(numArr[0])) {
            plogd("handleCmdUpdateSystemSelectionChannels: mRegionalConfigId is not valid, mRegionalConfig=" + numArr[0]);
            sendUpdateSystemSelectionChannelsResult(16, null);
            return;
        }
        SatelliteAccessConfiguration satelliteAccessConfiguration = getSatelliteAccessConfiguration(numArr[0]);
        if (satelliteAccessConfiguration == null) {
            plogd("handleCmdUpdateSystemSelectionChannels: satelliteAccessConfiguration is not valid");
            sendUpdateSystemSelectionChannelsResult(16, null);
            return;
        }
        List<SatelliteInfo> satelliteInfos = satelliteAccessConfiguration.getSatelliteInfos();
        ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        for (SatelliteInfo satelliteInfo : satelliteInfos) {
            arrayList.addAll(satelliteInfo.getBands());
            satelliteInfo.getEarfcnRanges().stream().flatMapToInt(new Function() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda9
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    EarfcnRange earfcnRange = (EarfcnRange) obj;
                    return IntStream.of(earfcnRange.getStartEarfcn(), earfcnRange.getEndEarfcn());
                }
            }).boxed().forEach(new Consumer() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda10
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    arrayList2.add((Integer) obj);
                }
            });
        }
        IntArray intArray = new IntArray(arrayList.size());
        intArray.addAll(arrayList.stream().mapToInt(new CarrierConfigLoader$$ExternalSyntheticLambda0()).toArray());
        IntArray intArray2 = new IntArray(Math.min(arrayList2.size(), 32));
        for (int i = 0; i < Math.min(arrayList2.size(), 32); i++) {
            intArray2.add(((Integer) arrayList2.get(i)).intValue());
        }
        IntArray intArray3 = new IntArray(satelliteAccessConfiguration.getTagIds().size());
        intArray3.addAll(satelliteAccessConfiguration.getTagIds().stream().mapToInt(new CarrierConfigLoader$$ExternalSyntheticLambda0()).toArray());
        ArrayList arrayList3 = new ArrayList();
        List manualConnectSatellitePlmnsForCarrier = this.mSatelliteController.getManualConnectSatellitePlmnsForCarrier(selectedSatelliteSubId);
        arrayList3.add(new SystemSelectionSpecifier(manualConnectSatellitePlmnsForCarrier.isEmpty() ? "" : (String) manualConnectSatellitePlmnsForCarrier.get(0), intArray, intArray2, (SatelliteInfo[]) satelliteInfos.toArray(new SatelliteInfo[0]), intArray3, subscriptionInfo.getIccId(), (String[]) manualConnectSatellitePlmnsForCarrier.toArray(new String[0])));
        this.mSatelliteController.updateSystemSelectionChannels(arrayList3, this.mInternalUpdateSystemSelectionChannelsResultReceiver);
    }

    private void sendUpdateSystemSelectionChannelsResult(int i, Bundle bundle) {
        plogd("sendUpdateSystemSelectionChannelsResult: resultCode=" + i);
        Iterator it = this.mUpdateSystemSelectionChannelsResultReceivers.iterator();
        while (it.hasNext()) {
            ((ResultReceiver) it.next()).send(i, bundle);
        }
        this.mUpdateSystemSelectionChannelsResultReceivers.clear();
    }

    private static boolean getSatelliteAccessAllowFromOverlayConfig(Context context) {
        Boolean boolValueOf;
        try {
            boolValueOf = Boolean.valueOf(context.getResources().getBoolean(R.bool.config_shortPressEarlyOnStemPrimary));
        } catch (Resources.NotFoundException e) {
            loge("getSatelliteAccessAllowFromOverlayConfig: got ex=" + e);
            boolValueOf = null;
        }
        if (boolValueOf == null && isMockModemAllowed()) {
            logd("getSatelliteAccessAllowFromOverlayConfig: Read config_oem_enabled_satellite_access_allow from device config");
            boolValueOf = Boolean.valueOf(DeviceConfig.getBoolean("telephony", "config_oem_enabled_satellite_access_allow", true));
        }
        if (boolValueOf == null) {
            logd("Use default satellite access allow=true control");
            boolValueOf = Boolean.TRUE;
        }
        return boolValueOf.booleanValue();
    }

    protected String getSatelliteConfigurationFileNameFromOverlayConfig(Context context) {
        String string;
        try {
            string = context.getResources().getString(R.string.wfcSpnFormat_spn_wlan_call);
        } catch (Resources.NotFoundException e) {
            loge("getSatelliteConfigurationFileNameFromOverlayConfig: got ex=" + e);
            string = null;
        }
        logd("satelliteAccessControlInfoFile =" + string);
        return string;
    }

    private static String getSatelliteS2CellFileFromOverlayConfig(Context context) {
        String string;
        try {
            string = context.getResources().getString(R.string.default_audio_route_name_headphones);
        } catch (Resources.NotFoundException e) {
            loge("getSatelliteS2CellFileFromOverlayConfig: got ex=" + e);
            string = null;
        }
        if (TextUtils.isEmpty(string) && isMockModemAllowed()) {
            logd("getSatelliteS2CellFileFromOverlayConfig: Read config_oem_enabled_satellite_s2cell_file from device config");
            string = DeviceConfig.getString("telephony", "config_oem_enabled_satellite_s2cell_file", (String) null);
        }
        logd("s2CellFile=" + string);
        return string;
    }

    private static List getSatelliteCountryCodesFromOverlayConfig(Context context) {
        String[] stringArrayFromOverlayConfig = readStringArrayFromOverlayConfig(context, R.array.config_supportedDreamComplications);
        if (stringArrayFromOverlayConfig.length == 0 && isMockModemAllowed()) {
            logd("getSatelliteCountryCodesFromOverlayConfig: Read config_oem_enabled_satellite_country_codes from device config");
            stringArrayFromOverlayConfig = DeviceConfig.getString("telephony", "config_oem_enabled_satellite_country_codes", "").split(",");
        }
        return (List) Arrays.stream(stringArrayFromOverlayConfig).map(new Function() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((String) obj).toUpperCase(Locale.US);
            }
        }).collect(Collectors.toList());
    }

    private static long getDelayBeforeRetryValidatingPossibleChangeInSatelliteAllowedRegionMillis(Context context) {
        Integer numValueOf;
        try {
            numValueOf = Integer.valueOf(context.getResources().getInteger(R.integer.config_undockedHdmiRotation));
        } catch (Resources.NotFoundException e) {
            loge("getDelayBeforeRetryValidatingPossibleChangeInSatelliteAllowedRegionMillis: got ex=" + e);
            numValueOf = null;
        }
        if (numValueOf == null) {
            logd("Use default retry duration for possible change satellite allowed region =10");
            numValueOf = 10;
        }
        return TimeUnit.MINUTES.toMillis(numValueOf.intValue());
    }

    private static int getMaxRetryCountForValidatingPossibleChangeInAllowedRegion(Context context) {
        Integer numValueOf;
        try {
            numValueOf = Integer.valueOf(context.getResources().getInteger(R.integer.config_veryLongPressOnPowerBehavior));
        } catch (Resources.NotFoundException e) {
            loge("getMaxRetryCountForValidatingPossibleChangeInAllowedRegion: got ex= " + e);
            numValueOf = null;
        }
        if (numValueOf == null) {
            logd("Use default max retry count for possible change satellite allowed region =3");
            numValueOf = 3;
        }
        return numValueOf.intValue();
    }

    private static long getLocationQueryThrottleIntervalNanos(Context context) {
        Integer numValueOf;
        try {
            numValueOf = Integer.valueOf(context.getResources().getInteger(R.integer.config_valid_wappush_index));
        } catch (Resources.NotFoundException e) {
            loge("getLocationQueryThrottleIntervalNanos: got ex=" + e);
            numValueOf = null;
        }
        if (numValueOf == null) {
            logd("Use default location query throttle interval =10");
            numValueOf = 10;
        }
        return TimeUnit.MINUTES.toNanos(numValueOf.intValue());
    }

    private static String[] readStringArrayFromOverlayConfig(Context context, int i) {
        String[] stringArray;
        try {
            stringArray = context.getResources().getStringArray(i);
        } catch (Resources.NotFoundException e) {
            loge("readStringArrayFromOverlayConfig: id= " + i + ", ex=" + e);
            stringArray = null;
        }
        return stringArray == null ? new String[0] : stringArray;
    }

    private static long getSatelliteLocationFreshDurationFromOverlayConfig(Context context) {
        Integer numValueOf;
        try {
            numValueOf = Integer.valueOf(context.getResources().getInteger(R.integer.config_screenBrightnessSettingMinimum));
        } catch (Resources.NotFoundException e) {
            loge("getSatelliteLocationFreshDurationFromOverlayConfig: got ex=" + e);
            numValueOf = null;
        }
        if (numValueOf == null && isMockModemAllowed()) {
            logd("getSatelliteLocationFreshDurationFromOverlayConfig: Read config_oem_enabled_satellite_location_fresh_duration from device config");
            numValueOf = Integer.valueOf(DeviceConfig.getInt("telephony", "config_oem_enabled_satellite_location_fresh_duration", 600));
        }
        if (numValueOf == null) {
            logd("Use default satellite location fresh duration=600");
            numValueOf = 600;
        }
        return TimeUnit.SECONDS.toNanos(numValueOf.intValue());
    }

    private void startWaitForCurrentLocationTimer() {
        if (hasMessages(2)) {
            plogw("WaitForCurrentLocationTimer is already started");
            removeMessages(2);
        }
        sendEmptyMessageDelayed(2, WAIT_FOR_CURRENT_LOCATION_TIMEOUT_MILLIS);
    }

    private void stopWaitForCurrentLocationTimer() {
        removeMessages(2);
    }

    private void restartKeepOnDeviceAccessControllerResourcesTimer() {
        if (hasMessages(3)) {
            plogd("KeepOnDeviceAccessControllerResourcesTimer is already started. Restarting it...");
            removeMessages(3);
        }
        sendEmptyMessageDelayed(3, KEEP_ON_DEVICE_ACCESS_CONTROLLER_RESOURCES_TIMEOUT_MILLIS);
    }

    private void stopKeepOnDeviceAccessControllerResourcesTimer() {
        removeMessages(3);
    }

    private void reportAnomaly(String str, String str2) {
        ploge(str2);
        AnomalyReporter.reportAnomaly(UUID.fromString(str), str2);
    }

    private static boolean isMockModemAllowed() {
        return DEBUG || SystemProperties.getBoolean("persist.radio.allow_mock_modem", true) || SystemProperties.getBoolean("ro.boot.radio.allow_mock_modem", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDelayedRequestAsync(int i, Object obj, long j) {
        sendMessageDelayed(obtainMessage(i, obj), j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendRequestAsync(int i, Object obj) {
        obtainMessage(i, obj).sendToTarget();
    }

    public int registerForCommunicationAccessStateChanged(int i, final ISatelliteCommunicationAccessStateCallback iSatelliteCommunicationAccessStateCallback) {
        this.mSatelliteCommunicationAccessStateChangedListeners.put(iSatelliteCommunicationAccessStateCallback.asBinder(), iSatelliteCommunicationAccessStateCallback);
        post(new Runnable() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$registerForCommunicationAccessStateChanged$4(iSatelliteCommunicationAccessStateCallback);
            }
        });
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerForCommunicationAccessStateChanged$4(ISatelliteCommunicationAccessStateCallback iSatelliteCommunicationAccessStateCallback) {
        try {
            iSatelliteCommunicationAccessStateCallback.onAccessAllowedStateChanged(this.mCurrentSatelliteAllowedState.get());
            logd("registerForCommunicationAccessStateChanged: mCurrentSatelliteAllowedState " + this.mCurrentSatelliteAllowedState.get());
            iSatelliteCommunicationAccessStateCallback.onAccessConfigurationChanged(getCurrentSatelliteAccessConfiguration());
        } catch (RemoteException e) {
            ploge("registerForCommunicationAccessStateChanged: RemoteException ex=" + e);
        }
    }

    public void unregisterForCommunicationAccessStateChanged(int i, ISatelliteCommunicationAccessStateCallback iSatelliteCommunicationAccessStateCallback) {
        this.mSatelliteCommunicationAccessStateChangedListeners.remove(iSatelliteCommunicationAccessStateCallback.asBinder());
    }

    public List getSatelliteDisallowedReasons() {
        List satelliteDisallowedReasonsCopy = getSatelliteDisallowedReasonsCopy();
        plogd("getSatelliteDisallowedReasons: satelliteDisallowedReasons:" + String.join(", ", satelliteDisallowedReasonsCopy.toString()));
        return satelliteDisallowedReasonsCopy;
    }

    public void registerForSatelliteDisallowedReasonsChanged(final ISatelliteDisallowedReasonsCallback iSatelliteDisallowedReasonsCallback) {
        this.mSatelliteDisallowedReasonsChangedListeners.put(iSatelliteDisallowedReasonsCallback.asBinder(), iSatelliteDisallowedReasonsCallback);
        post(new Runnable() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$registerForSatelliteDisallowedReasonsChanged$5(iSatelliteDisallowedReasonsCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerForSatelliteDisallowedReasonsChanged$5(ISatelliteDisallowedReasonsCallback iSatelliteDisallowedReasonsCallback) {
        try {
            List satelliteDisallowedReasonsCopy = getSatelliteDisallowedReasonsCopy();
            iSatelliteDisallowedReasonsCallback.onSatelliteDisallowedReasonsChanged(satelliteDisallowedReasonsCopy.stream().mapToInt(new CarrierConfigLoader$$ExternalSyntheticLambda0()).toArray());
            logd("registerForSatelliteDisallowedReasonsChanged: satelliteDisallowedReasons " + satelliteDisallowedReasonsCopy.size());
        } catch (RemoteException e) {
            ploge("registerForSatelliteDisallowedReasonsChanged: RemoteException ex=" + e);
        }
    }

    public void unregisterForSatelliteDisallowedReasonsChanged(ISatelliteDisallowedReasonsCallback iSatelliteDisallowedReasonsCallback) {
        this.mSatelliteDisallowedReasonsChangedListeners.remove(iSatelliteDisallowedReasonsCallback.asBinder());
    }

    public boolean setIsSatelliteCommunicationAllowedForCurrentLocationCache(String str) {
        if (!isMockModemAllowed()) {
            logd("setIsSatelliteCommunicationAllowedForCurrentLocationCache: mock modem not allowed.");
            return false;
        }
        logd("setIsSatelliteCommunicationAllowedForCurrentLocationCache: state=" + str);
        if ("enable".equalsIgnoreCase(str)) {
            this.mIsAllowedStateCacheDisabledForCtsTest.set(false);
        } else if ("disable".equalsIgnoreCase(str)) {
            this.mIsAllowedStateCacheDisabledForCtsTest.set(true);
        } else {
            loge("setIsSatelliteCommunicationAllowedForCurrentLocationCache: invalid state=" + str);
            return false;
        }
        return true;
    }

    private void notifySatelliteCommunicationAllowedStateChanged(final boolean z) {
        plogd("notifySatelliteCommunicationAllowedStateChanged: allowState=" + z);
        final ArrayList arrayList = new ArrayList();
        this.mSatelliteCommunicationAccessStateChangedListeners.values().forEach(new Consumer() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifySatelliteCommunicationAllowedStateChanged$6(z, arrayList, (ISatelliteCommunicationAccessStateCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifySatelliteCommunicationAllowedStateChanged$7((ISatelliteCommunicationAccessStateCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifySatelliteCommunicationAllowedStateChanged$6(boolean z, List list, ISatelliteCommunicationAccessStateCallback iSatelliteCommunicationAccessStateCallback) {
        try {
            iSatelliteCommunicationAccessStateCallback.onAccessAllowedStateChanged(z);
        } catch (RemoteException e) {
            plogd("notifySatelliteCommunicationAllowedStateChanged RemoteException: " + e);
            list.add(iSatelliteCommunicationAccessStateCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifySatelliteCommunicationAllowedStateChanged$7(ISatelliteCommunicationAccessStateCallback iSatelliteCommunicationAccessStateCallback) {
        this.mSatelliteCommunicationAccessStateChangedListeners.remove(iSatelliteCommunicationAccessStateCallback.asBinder());
    }

    private void notifySatelliteDisallowedReasonsChanged() {
        plogd("notifySatelliteDisallowedReasonsChanged");
        final List satelliteDisallowedReasonsCopy = getSatelliteDisallowedReasonsCopy();
        final ArrayList arrayList = new ArrayList();
        this.mSatelliteDisallowedReasonsChangedListeners.values().forEach(new Consumer() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifySatelliteDisallowedReasonsChanged$8(satelliteDisallowedReasonsCopy, arrayList, (ISatelliteDisallowedReasonsCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifySatelliteDisallowedReasonsChanged$9((ISatelliteDisallowedReasonsCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifySatelliteDisallowedReasonsChanged$8(List list, List list2, ISatelliteDisallowedReasonsCallback iSatelliteDisallowedReasonsCallback) {
        try {
            iSatelliteDisallowedReasonsCallback.onSatelliteDisallowedReasonsChanged(list.stream().mapToInt(new CarrierConfigLoader$$ExternalSyntheticLambda0()).toArray());
        } catch (RemoteException e) {
            plogd("notifySatelliteDisallowedReasonsChanged RemoteException: " + e);
            list2.add(iSatelliteDisallowedReasonsCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifySatelliteDisallowedReasonsChanged$9(ISatelliteDisallowedReasonsCallback iSatelliteDisallowedReasonsCallback) {
        this.mSatelliteDisallowedReasonsChangedListeners.remove(iSatelliteDisallowedReasonsCallback.asBinder());
    }

    protected void notifyRegionalSatelliteConfigurationChanged(final SatelliteAccessConfiguration satelliteAccessConfiguration) {
        plogd("notifyRegionalSatelliteConfigurationChanged : satelliteAccessConfig is " + satelliteAccessConfiguration);
        final ArrayList arrayList = new ArrayList();
        this.mSatelliteCommunicationAccessStateChangedListeners.values().forEach(new Consumer() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifyRegionalSatelliteConfigurationChanged$10(satelliteAccessConfiguration, arrayList, (ISatelliteCommunicationAccessStateCallback) obj);
            }
        });
        arrayList.forEach(new Consumer() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$notifyRegionalSatelliteConfigurationChanged$11((ISatelliteCommunicationAccessStateCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyRegionalSatelliteConfigurationChanged$10(SatelliteAccessConfiguration satelliteAccessConfiguration, List list, ISatelliteCommunicationAccessStateCallback iSatelliteCommunicationAccessStateCallback) {
        try {
            iSatelliteCommunicationAccessStateCallback.onAccessConfigurationChanged(satelliteAccessConfiguration);
        } catch (RemoteException e) {
            plogd("handleEventNtnSignalStrengthChanged RemoteException: " + e);
            list.add(iSatelliteCommunicationAccessStateCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyRegionalSatelliteConfigurationChanged$11(ISatelliteCommunicationAccessStateCallback iSatelliteCommunicationAccessStateCallback) {
        this.mSatelliteCommunicationAccessStateChangedListeners.remove(iSatelliteCommunicationAccessStateCallback.asBinder());
    }

    private void reportMetrics(int i, boolean z) {
        ControllerMetricsStats controllerMetricsStats = this.mControllerMetricsStats;
        if (i == 0) {
            controllerMetricsStats.reportAllowedSatelliteAccessCount(z);
        } else {
            controllerMetricsStats.reportFailedSatelliteAccessCheckCount();
        }
        this.mControllerMetricsStats.reportCurrentVersionOfSatelliteAccessConfig(this.mSatelliteAccessConfigVersion.get());
        this.mAccessControllerMetricsStats.setLocationQueryTime(this.mLocationQueryStartTimeMillis.get()).setTotalCheckingTime(this.mTotalCheckingStartTimeMillis.get()).setIsAllowed(z).setIsEmergency(isInEmergency()).setResult(i).setCarrierId(this.mSatelliteController.getSatelliteCarrierId()).setIsNtnOnlyCarrier(this.mSatelliteController.isNtnOnlyCarrier()).reportAccessControllerMetrics();
        this.mLocationQueryStartTimeMillis.set(0L);
        this.mOnDeviceLookupStartTimeMillis.set(0L);
        this.mTotalCheckingStartTimeMillis.set(0L);
    }

    protected boolean isSatelliteAllowedRegionPossiblyChanged() {
        return this.mIsSatelliteAllowedRegionPossiblyChanged.get();
    }

    protected void setIsSatelliteAllowedRegionPossiblyChanged(boolean z) {
        plogd("setIsSatelliteAllowedRegionPossiblyChanged : " + z);
        this.mIsSatelliteAllowedRegionPossiblyChanged.set(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("SatelliteAccessController", str);
    }

    protected static void loge(String str) {
        Log.e("SatelliteAccessController", str);
    }

    private static void logv(String str) {
        Log.v("SatelliteAccessController", str);
    }

    public boolean overrideCarrierRoamingNtnEligibilityChanged(boolean z, boolean z2) {
        if (!isMockModemAllowed()) {
            logd("overrideCarrierRoamingNtnEligibilityChanged: mock modem not allowed.");
            return false;
        }
        logd("calling overrideCarrierRoamingNtnEligibilityChanged");
        return this.mSatelliteController.overrideCarrierRoamingNtnEligibilityChanged(z, z2);
    }

    final class SatelliteRegionalConfig {
        private final int mConfigId;
        private final Set mEarfcns;

        SatelliteRegionalConfig(int i, Set set) {
            this.mConfigId = i;
            this.mEarfcns = set;
        }
    }

    private void updateSatelliteRegionalConfig(int i) {
        plogd("updateSatelliteRegionalConfig: subId: " + i);
        if (i == -1) {
            return;
        }
        this.mSatelliteController.updateRegionalSatelliteEarfcns(i);
        Map regionalSatelliteEarfcns = this.mSatelliteController.getRegionalSatelliteEarfcns(i);
        if (regionalSatelliteEarfcns.isEmpty()) {
            plogd("updateSatelliteRegionalConfig: Earfcns are not found for subId: " + i);
            return;
        }
        HashMap map = new HashMap();
        for (String str : regionalSatelliteEarfcns.keySet()) {
            HashSet hashSet = new HashSet();
            for (Integer num : (Set) regionalSatelliteEarfcns.get(str)) {
                num.intValue();
                hashSet.add(num);
            }
            map.put(Integer.valueOf(str), new SatelliteRegionalConfig(Integer.valueOf(str).intValue(), hashSet));
        }
        this.mSatelliteRegionalConfigPerSubMap.put(Integer.valueOf(i), map);
    }

    private void handleCarrierConfigChanged(Context context, int i, int i2, int i3, int i4) {
        plogd("handleCarrierConfigChanged: slotIndex=" + i + ", subId=" + i2 + ", carrierId=" + i3 + ", specificCarrierId=" + i4);
        updateSatelliteRegionalConfig(i2);
        evaluatePossibleChangeInDefaultSmsApp(context);
    }

    private boolean isReasonPresentInSatelliteDisallowedReasons(int i) {
        boolean zContains;
        synchronized (this.mSatelliteDisallowedReasonsLock) {
            zContains = this.mSatelliteDisallowedReasons.contains(Integer.valueOf(i));
        }
        return zContains;
    }

    private void addReasonToSatelliteDisallowedReasons(int i) {
        synchronized (this.mSatelliteDisallowedReasonsLock) {
            this.mSatelliteDisallowedReasons.add(Integer.valueOf(i));
        }
    }

    private void removeReasonFromSatelliteDisallowedReasons(int i) {
        synchronized (this.mSatelliteDisallowedReasonsLock) {
            this.mSatelliteDisallowedReasons.remove(Integer.valueOf(i));
        }
    }

    private boolean isSatelliteDisallowedReasonsEmpty() {
        boolean zIsEmpty;
        synchronized (this.mSatelliteDisallowedReasonsLock) {
            zIsEmpty = this.mSatelliteDisallowedReasons.isEmpty();
        }
        return zIsEmpty;
    }

    private void removeAllReasonsFromSatelliteDisallowedReasons(List list) {
        synchronized (this.mSatelliteDisallowedReasonsLock) {
            this.mSatelliteDisallowedReasons.removeAll(list);
        }
    }

    private List getSatelliteDisallowedReasonsCopy() {
        ArrayList arrayList;
        synchronized (this.mSatelliteDisallowedReasonsLock) {
            arrayList = new ArrayList(this.mSatelliteDisallowedReasons);
        }
        return arrayList;
    }

    private void handleEventLocationProvidersChanged() {
        plogd("handleEventLocationProvidersChanged: mIsLocationSettingsEnabled=" + this.mIsLocationSettingsEnabled.get() + ", mIsLocationProviderEnabled=" + this.mIsLocationProviderEnabled.get());
        if (this.mIsLocationSettingsEnabled.get() && this.mIsLocationProviderEnabled.get()) {
            return;
        }
        if (this.mLocationManager.isLocationEnabled()) {
            plogd("Location settings is enabled");
            this.mIsLocationSettingsEnabled.set(true);
            if (this.mLocationManager.isProviderEnabled("fused")) {
                this.mIsLocationProviderEnabled.set(true);
                plogd("fused provider is enabled");
            }
            if (this.mCheckingAllowedStateRequests.isEmpty()) {
                sendRequestAsync(6, null);
                return;
            } else {
                plogd("delayed EVENT_LOCATION_SETTINGS_ENABLED due to requestIsCommunicationAllowedForCurrentLocation is already being processed");
                sendDelayedRequestAsync(6, null, WAIT_UNTIL_CURRENT_LOCATION_QUERY_IS_DONE_MILLIS);
                return;
            }
        }
        plogd("Location settings is still disabled, wait until next enabled event");
    }

    public int getSatelliteAccessConfigVersion() {
        return this.mSatelliteAccessConfigVersion.get();
    }

    private void setRegionalConfigId(Integer num) {
        if (num == null) {
            this.mRegionalConfigId = null;
            return;
        }
        AtomicInteger atomicInteger = this.mRegionalConfigId;
        if (atomicInteger == null) {
            this.mRegionalConfigId = new AtomicInteger(num.intValue());
        } else {
            atomicInteger.set(num.intValue());
        }
    }

    private Integer getRegionalConfigId() {
        AtomicInteger atomicInteger = this.mRegionalConfigId;
        if (atomicInteger == null) {
            return null;
        }
        return Integer.valueOf(atomicInteger.get());
    }

    private void setNewRegionalConfigId(Integer num) {
        if (num == null) {
            this.mNewRegionalConfigId = null;
            return;
        }
        AtomicInteger atomicInteger = this.mNewRegionalConfigId;
        if (atomicInteger == null) {
            this.mNewRegionalConfigId = new AtomicInteger(num.intValue());
        } else {
            atomicInteger.set(num.intValue());
        }
    }

    private Integer getNewRegionalConfigId() {
        AtomicInteger atomicInteger = this.mNewRegionalConfigId;
        if (atomicInteger == null) {
            return null;
        }
        return Integer.valueOf(atomicInteger.get());
    }

    private void setSatelliteAccessConfigMap(Map map) {
        synchronized (this.mLock) {
            this.mSatelliteAccessConfigMap = map;
        }
    }

    private SatelliteAccessConfiguration getSatelliteAccessConfiguration(final Integer num) {
        SatelliteAccessConfiguration satelliteAccessConfiguration;
        synchronized (this.mLock) {
            satelliteAccessConfiguration = (SatelliteAccessConfiguration) Optional.ofNullable(this.mSatelliteAccessConfigMap).map(new Function() { // from class: com.android.phone.satellite.accesscontrol.SatelliteAccessController$$ExternalSyntheticLambda13
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return SatelliteAccessController.$r8$lambda$Af6zbTjSMwZTwunzxfZSSDdcbu8(num, (Map) obj);
                }
            }).orElse(null);
        }
        return satelliteAccessConfiguration;
    }

    public static /* synthetic */ SatelliteAccessConfiguration $r8$lambda$Af6zbTjSMwZTwunzxfZSSDdcbu8(Integer num, Map map) {
        return (SatelliteAccessConfiguration) map.get(num);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SatelliteAccessConfiguration getCurrentSatelliteAccessConfiguration() {
        Integer regionalConfigId = getRegionalConfigId();
        SatelliteAccessConfiguration satelliteAccessConfiguration = SatelliteAccessConfigurationParser.isRegionalConfigIdValid(regionalConfigId) ? getSatelliteAccessConfiguration(regionalConfigId) : null;
        logd("getCurrentSatelliteAccessConfiguration: satelliteAccessConfig: " + satelliteAccessConfiguration + " of mRegionalConfigId: " + regionalConfigId);
        return satelliteAccessConfiguration;
    }

    private void setOnDeviceAccessController(SatelliteOnDeviceAccessController satelliteOnDeviceAccessController) {
        synchronized (this.mLock) {
            this.mSatelliteOnDeviceAccessController = satelliteOnDeviceAccessController;
        }
    }

    private SatelliteOnDeviceAccessController getSatelliteOnDeviceAccessController() {
        SatelliteOnDeviceAccessController satelliteOnDeviceAccessController;
        synchronized (this.mLock) {
            satelliteOnDeviceAccessController = this.mSatelliteOnDeviceAccessController;
        }
        return satelliteOnDeviceAccessController;
    }

    private void plogv(String str) {
        Log.v("SatelliteAccessController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("SatelliteAccessController", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void plogd(String str) {
        Log.d("SatelliteAccessController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("SatelliteAccessController", str);
        }
    }

    private void plogw(String str) {
        Log.w("SatelliteAccessController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.warn("SatelliteAccessController", str);
        }
    }

    private void ploge(String str) {
        Log.e("SatelliteAccessController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.error("SatelliteAccessController", str);
        }
    }

    private boolean shouldIgnoreAllowedStateCache() {
        Iterator it = this.mCheckingAllowedStateRequests.iterator();
        while (it.hasNext()) {
            if (((CheckingAllowedStateRequestArguments) it.next()).shouldIgnoreAllowedStateCache()) {
                return true;
            }
        }
        return false;
    }

    public class CheckingAllowedStateRequestArguments {
        private final ResultReceiver mResultReceiver;
        private final boolean mShouldIgnoreAllowedStateCache;
        private final int mSubId;

        public CheckingAllowedStateRequestArguments(int i, ResultReceiver resultReceiver, boolean z) {
            this.mSubId = i;
            this.mResultReceiver = resultReceiver;
            this.mShouldIgnoreAllowedStateCache = z;
        }

        public ResultReceiver getResultReceiver() {
            return this.mResultReceiver;
        }

        public boolean shouldIgnoreAllowedStateCache() {
            return this.mShouldIgnoreAllowedStateCache;
        }
    }
}
