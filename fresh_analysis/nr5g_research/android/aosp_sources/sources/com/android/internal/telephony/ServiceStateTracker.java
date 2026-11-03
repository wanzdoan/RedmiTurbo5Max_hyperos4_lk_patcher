package com.android.internal.telephony;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.BaseBundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Message;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.WorkSource;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.DataSpecificRegistrationInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.VoiceSpecificRegistrationInfo;
import android.telephony.satellite.ISatelliteModemStateCallback;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.cdnr.CarrierDisplayNameData;
import com.android.internal.telephony.cdnr.CarrierDisplayNameResolver;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.MiuiImsControllerStub;
import com.android.internal.telephony.metrics.RadioPowerStateStats;
import com.android.internal.telephony.metrics.ServiceStateStats;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.NtnCapabilityResolver;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.MiuiUiccControllerStub;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.internal.telephony.util.LinkPropertiesUtils;
import com.android.internal.telephony.util.NotificationChannelController;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class ServiceStateTracker extends Handler {
    public static final int CARRIER_NAME_DISPLAY_BITMASK_SHOW_PLMN = 2;
    public static final int CARRIER_NAME_DISPLAY_BITMASK_SHOW_SPN = 1;
    public static final int CS_DISABLED = 1004;
    public static final int CS_EMERGENCY_ENABLED = 1006;
    public static final int CS_ENABLED = 1003;
    public static final int CS_NORMAL_ENABLED = 1005;
    public static final int CS_NOTIFICATION = 999;
    public static final int CS_REJECT_CAUSE_DISABLED = 2002;
    public static final int CS_REJECT_CAUSE_ENABLED = 2001;
    public static final int CS_REJECT_CAUSE_NOTIFICATION = 111;
    public static final int DEFAULT_GPRS_CHECK_PERIOD_MILLIS = 60000;
    protected static final int EVENT_ALL_DATA_DISCONNECTED = 49;
    protected static final int EVENT_CELL_LOCATION_RESPONSE = 56;
    protected static final int EVENT_CHANGE_IMS_STATE = 45;
    protected static final int EVENT_CHECK_REPORT_GPRS = 22;
    protected static final int EVENT_GET_ALLOWED_NETWORK_TYPES = 19;
    protected static final int EVENT_GET_CELL_INFO_LIST = 43;
    protected static final int EVENT_GET_LOC_DONE = 15;
    public static final int EVENT_ICC_CHANGED = 42;
    protected static final int EVENT_IMS_CAPABILITY_CHANGED = 48;
    protected static final int EVENT_IMS_SERVICE_STATE_CHANGED = 53;
    protected static final int EVENT_IMS_STATE_CHANGED = 46;
    protected static final int EVENT_IMS_STATE_DONE = 47;
    protected static final int EVENT_LOCATION_UPDATES_ENABLED = 18;
    protected static final int EVENT_NETWORK_STATE_CHANGED = 2;
    protected static final int EVENT_NITZ_TIME = 11;
    protected static final int EVENT_PHYSICAL_CHANNEL_CONFIG = 55;
    protected static final int EVENT_POLL_STATE_CS_CELLULAR_REGISTRATION = 4;
    protected static final int EVENT_POLL_STATE_INITIAL = 50;
    protected static final int EVENT_POLL_STATE_NETWORK_SELECTION_MODE = 14;
    protected static final int EVENT_POLL_STATE_OPERATOR = 7;
    protected static final int EVENT_POLL_STATE_PS_CELLULAR_REGISTRATION = 5;
    protected static final int EVENT_POLL_STATE_PS_IWLAN_REGISTRATION = 6;
    protected static final int EVENT_POWER_OFF_RADIO_IMS_DEREG_TIMEOUT = 62;
    protected static final int EVENT_RADIO_ON = 41;
    protected static final int EVENT_RADIO_POWER_FROM_CARRIER = 51;
    protected static final int EVENT_RADIO_POWER_OFF_DONE = 54;
    protected static final int EVENT_RADIO_STATE_CHANGED = 1;
    protected static final int EVENT_RESET_ALLOWED_NETWORK_TYPES = 21;
    protected static final int EVENT_RESET_LAST_KNOWN_CELL_IDENTITY = 63;
    protected static final int EVENT_RESTRICTED_STATE_CHANGED = 23;
    protected static final int EVENT_SET_ALLOWED_NETWORK_TYPES = 20;
    protected static final int EVENT_SET_RADIO_POWER_OFF = 38;
    protected static final int EVENT_SIM_READY = 17;
    protected static final int EVENT_SIM_RECORDS_LOADED = 16;
    protected static final int EVENT_TELECOM_VOICE_SERVICE_STATE_OVERRIDE_CHANGED = 65;
    protected static final int EVENT_UNSOL_CELL_INFO_LIST = 44;
    public static final String INVALID_MCC = "000";
    private static final long POWER_OFF_ALL_DATA_NETWORKS_DISCONNECTED_TIMEOUT = 10000;
    protected static final String PROP_FORCE_ROAMING = "telephony.test.forceRoaming";
    public static final int PS_DISABLED = 1002;
    public static final int PS_ENABLED = 1001;
    public static final int PS_NOTIFICATION = 888;
    protected final AccessNetworksManager mAccessNetworksManager;
    private AccessNetworksManager.AccessNetworksManagerCallback mAccessNetworksManagerCallback;
    private int mAllowedNetworkTypes;
    private final LocalLog mAttachLog;
    private boolean mCSEmergencyOnly;
    private CarrierServiceStateTracker mCSST;
    protected PersistableBundle mCarrierConfig;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    protected CarrierDisplayNameResolver mCdnr;
    private final LocalLog mCdnrLogs;
    protected CellIdentity mCellIdentity;
    protected CommandsInterface mCi;
    private final ContentResolver mCr;
    private String mCurrentCarrier;
    protected DataNetworkController.DataNetworkControllerCallback mDataDisconnectedCallback;
    protected boolean mDesiredPowerState;
    protected boolean mEmergencyOnly;
    protected final FeatureFlags mFeatureFlags;
    protected boolean mGsmDataRoaming;
    private boolean mGsmVoiceRoaming;
    private BroadcastReceiver mIntentReceiver;
    protected boolean mIsSimReady;
    private boolean mIsSimReadyForDisplay;
    private long mLastCellInfoReqTime;
    private int mLastKnownAreaCode;
    private CellIdentity mLastKnownCellIdentity;
    private String mLastKnownNetworkCountry;
    protected NitzData mLastNitzData;
    private final LocaleTracker mLocaleTracker;
    protected int mMaxDataCalls;
    protected int mNewMaxDataCalls;
    protected int mNewReasonDataDenied;
    protected int mNewRejectCode;
    protected ServiceState mNewSS;
    protected final NitzStateMachine mNitzState;
    private Notification mNotification;
    protected int mNtnMode;
    protected int mOldNtnMode;
    private final SstSubscriptionsChangedListener mOnSubscriptionsChangedListener;
    private Pattern mOperatorNameStringPattern;
    private final ServiceState mOutOfServiceSS;
    private boolean mPSEmergencyOnly;
    protected final GsmCdmaPhone mPhone;
    private final LocalLog mPhoneTypeLog;
    private boolean mPhoneTypeUpdated;
    public int[] mPollingContext;
    protected final LocalLog mRadioPowerLog;
    private final LocalLog mRatLog;
    private final RatRatcheter mRatRatcheter;
    protected int mReasonDataDenied;
    protected final SparseArray<NetworkRegistrationManager> mRegStateManagers;
    protected int mRegistrationState;
    protected int mRejectCode;
    protected boolean mReportedGprsNoReg;
    public RestrictedState mRestrictedState;
    private final LocalLog mRoamingLog;
    public ServiceState mSS;
    private SatelliteModemStateListener mSatelliteModemStateListener;
    protected ServiceStateStats mServiceStateStats;
    protected boolean mStartedGprsRegCheck;
    private SubscriptionManager mSubscriptionManager;
    protected SubscriptionManagerService mSubscriptionManagerService;
    protected UiccController mUiccController;
    protected boolean mVoiceCapable;
    private boolean mWantContinuousLocationUpdates;
    private boolean mWantSingleLocationUpdate;
    protected boolean mManualSelection = false;
    protected UiccCardApplication mUiccApplication = null;
    protected IccRecords mIccRecords = null;
    private int mCellInfoMinIntervalMs = 2000;
    protected List<CellInfo> mLastCellInfoList = null;
    protected List<PhysicalChannelConfig> mLastPhysicalChannelConfigList = new CopyOnWriteArrayList();
    protected final Set<Integer> mRadioPowerOffReasons = new HashSet();
    protected RegistrantList mVoiceRoamingOnRegistrants = new RegistrantList();
    protected RegistrantList mVoiceRoamingOffRegistrants = new RegistrantList();
    protected RegistrantList mDataRoamingOnRegistrants = new RegistrantList();
    protected RegistrantList mDataRoamingOffRegistrants = new RegistrantList();
    protected SparseArray<RegistrantList> mAttachedRegistrants = new SparseArray<>();
    protected SparseArray<RegistrantList> mDetachedRegistrants = new SparseArray<>();
    private final RegistrantList mVoiceRegStateOrRatChangedRegistrants = new RegistrantList();
    private final SparseArray mDataRegStateOrRatChangedRegistrants = new SparseArray();
    protected RegistrantList mNetworkAttachedRegistrants = new RegistrantList();
    protected final RegistrantList mNetworkDetachedRegistrants = new RegistrantList();
    protected final RegistrantList mServiceStateChangedRegistrants = new RegistrantList();
    private final RegistrantList mPsRestrictEnabledRegistrants = new RegistrantList();
    private final RegistrantList mPsRestrictDisabledRegistrants = new RegistrantList();
    private final RegistrantList mImsCapabilityChangedRegistrants = new RegistrantList();
    protected final RegistrantList mNrStateChangedRegistrants = new RegistrantList();
    protected final RegistrantList mNrFrequencyChangedRegistrants = new RegistrantList();
    private final RegistrantList mCssIndicatorChangedRegistrants = new RegistrantList();
    private final RegistrantList mAirplaneModeChangedRegistrants = new RegistrantList();
    private final RegistrantList mAreaCodeChangedRegistrants = new RegistrantList();
    protected volatile boolean mPendingRadioPowerOffAfterDataOff = false;
    protected volatile int mPendingRadioPowerOffReason = 0;
    private List mPendingCellInfoRequests = new LinkedList();
    private boolean mIsPendingCellInfoRequest = false;
    protected boolean mImsRegistrationOnOff = false;
    protected boolean mDeviceShuttingDown = false;
    protected CarrierDisplayNameData mCarrierDisplayNameData = new CarrierDisplayNameData.Builder().build();
    protected String mCurSpn = null;
    protected String mCurDataSpn = null;
    protected String mCurPlmn = null;
    protected boolean mCurShowPlmn = false;
    protected boolean mCurShowSpn = false;
    public int mSubId = -1;
    protected int mPrevSubId = -1;
    private boolean mImsRegistered = false;

    private static boolean isValidLteBandwidthKhz(int i) {
        return i == 1400 || i == 3000 || i == 5000 || i == 10000 || i == 15000 || i == 20000;
    }

    private static boolean isValidNrBandwidthKhz(int i) {
        switch (i) {
            case GbaManager.REQUEST_TIMEOUT_MS /* 5000 */:
            case LinkPropertiesUtils.MAX_MTU /* 10000 */:
            case 15000:
            case 20000:
            case 25000:
            case 30000:
            case 40000:
            case 50000:
            case DEFAULT_GPRS_CHECK_PERIOD_MILLIS /* 60000 */:
            case 70000:
            case 80000:
            case 90000:
            case 100000:
                return true;
            default:
                return false;
        }
    }

    private int selectResourceForRejectCode(int i, boolean z) {
        if (i == 1) {
            return z ? R.string.permdesc_readCellBroadcasts : R.string.permdesc_readCallLog;
        }
        if (i == 2) {
            return z ? R.string.permdesc_readNetworkUsageHistory : R.string.permdesc_readMediaVideo;
        }
        if (i == 3) {
            return z ? R.string.permdesc_readMediaImages : R.string.permdesc_readMediaAudio;
        }
        if (i != 6) {
            return 0;
        }
        return z ? R.string.permdesc_readInstallSessions : R.string.permdesc_readContacts;
    }

    protected void extendSpnDisplayIntent(Intent intent) {
    }

    protected CarrierDisplayNameData extendSpnDisplayNameData(CarrierDisplayNameData carrierDisplayNameData) {
        return carrierDisplayNameData;
    }

    protected boolean hasSetRoamingType(ServiceState serviceState) {
        return false;
    }

    protected boolean isGprsConsistent(int i, int i2) {
        return i2 != 0 || i == 0;
    }

    protected boolean mtkAreAllDataDisconnected(Phone phone) {
        return false;
    }

    protected void mtkHangupAllImsCall() {
    }

    protected boolean needDiscardCurrentPollStateResult(ServiceState serviceState) {
        return false;
    }

    protected void onPollStateResultError(CommandException.Error error) {
    }

    protected void onServiceStateChangeNotified(ServiceState serviceState, boolean z, boolean z2) {
    }

    protected boolean regCodeIsRoaming(int i) {
        return 5 == i;
    }

    protected int regCodeToServiceState(int i) {
        return (i == 1 || i == 5) ? 0 : 1;
    }

    protected boolean setCarrierNameAsync(CarrierDisplayNameData carrierDisplayNameData) {
        return false;
    }

    public void startUpdateState(int i, int i2, int i3, int i4) {
    }

    public void updateLacAndCarrierDisplayName(CellIdentity cellIdentity) {
    }

    public void updateLacAndCarrierDisplayName(NetworkRegistrationInfo networkRegistrationInfo) {
    }

    private class SstSubscriptionsChangedListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        private SstSubscriptionsChangedListener() {
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            ServiceStateTracker.this.log("SubscriptionListener.onSubscriptionInfoChanged");
            int subId = ServiceStateTracker.this.mPhone.getSubId();
            ServiceStateTracker serviceStateTracker = ServiceStateTracker.this;
            int i = serviceStateTracker.mSubId;
            if (i == subId) {
                return;
            }
            serviceStateTracker.mPrevSubId = i;
            serviceStateTracker.mPhone.updateVoiceMail();
            boolean zIsValidSubscriptionId = SubscriptionManager.isValidSubscriptionId(subId);
            ServiceStateTracker serviceStateTracker2 = ServiceStateTracker.this;
            if (!zIsValidSubscriptionId) {
                if (SubscriptionManager.isValidSubscriptionId(serviceStateTracker2.mPrevSubId)) {
                    ServiceStateTracker serviceStateTracker3 = ServiceStateTracker.this;
                    serviceStateTracker3.mPhone.notifyServiceStateChangedForSubId(serviceStateTracker3.mOutOfServiceSS, ServiceStateTracker.this.mPrevSubId);
                    ServiceStateTracker serviceStateTracker4 = ServiceStateTracker.this;
                    if (serviceStateTracker4.getCombinedRegState(serviceStateTracker4.mSS) == 0) {
                        ServiceStateTracker.this.mSS = new ServiceState();
                        ServiceStateTracker serviceStateTracker5 = ServiceStateTracker.this;
                        serviceStateTracker5.mSS.setOutOfService(serviceStateTracker5.mAccessNetworksManager.isInLegacyMode(), false);
                    }
                }
            } else {
                Context context = serviceStateTracker2.mPhone.getContext();
                ServiceStateTracker.this.mPhone.notifyPhoneStateChanged();
                if (!SubscriptionManager.isValidSubscriptionId(ServiceStateTracker.this.mPrevSubId)) {
                    GsmCdmaPhone gsmCdmaPhone = ServiceStateTracker.this.mPhone;
                    gsmCdmaPhone.notifyServiceStateChanged(gsmCdmaPhone.getServiceState());
                    ServiceStateTracker.this.log("Update SS information on moving from invalid to valid sub id");
                    ServiceStateTracker serviceStateTracker6 = ServiceStateTracker.this;
                    serviceStateTracker6.updateServiceStateToDb(serviceStateTracker6.mPhone.getServiceState());
                }
                ServiceStateTracker.this.mPhone.sendSubscriptionSettings(!context.getResources().getBoolean(17892131));
                ServiceStateTracker serviceStateTracker7 = ServiceStateTracker.this;
                serviceStateTracker7.setDataNetworkTypeForPhone(serviceStateTracker7.mSS.getRilDataRadioTechnology());
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                String string = defaultSharedPreferences.getString(Phone.NETWORK_SELECTION_KEY, "");
                String string2 = defaultSharedPreferences.getString(Phone.NETWORK_SELECTION_NAME_KEY, "");
                String string3 = defaultSharedPreferences.getString(Phone.NETWORK_SELECTION_SHORT_KEY, "");
                if (!TextUtils.isEmpty(string) || !TextUtils.isEmpty(string2) || !TextUtils.isEmpty(string3)) {
                    SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
                    editorEdit.putString(Phone.NETWORK_SELECTION_KEY + subId, string);
                    editorEdit.putString(Phone.NETWORK_SELECTION_NAME_KEY + subId, string2);
                    editorEdit.putString(Phone.NETWORK_SELECTION_SHORT_KEY + subId, string3);
                    editorEdit.remove(Phone.NETWORK_SELECTION_KEY);
                    editorEdit.remove(Phone.NETWORK_SELECTION_NAME_KEY);
                    editorEdit.remove(Phone.NETWORK_SELECTION_SHORT_KEY);
                    editorEdit.commit();
                }
                ServiceStateTracker.this.updateCarrierDisplayName();
            }
            ServiceStateTracker.this.mSubId = subId;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        onCarrierConfigurationChanged(i);
    }

    protected class SatelliteModemStateListener extends ISatelliteModemStateCallback.Stub {
        private boolean mSatelliteNbIotConnected = false;
        private boolean mUpdateSatelliteCarrierDisplay = false;

        public void onEmergencyModeChanged(boolean z) {
        }

        public void onRegistrationFailure(int i) {
        }

        public void onTerrestrialNetworkAvailableChanged(boolean z) {
        }

        protected SatelliteModemStateListener() {
        }

        public void onSatelliteModemStateChanged(int i) {
            boolean zIsInConnectedState = isInConnectedState();
            if (zIsInConnectedState != this.mSatelliteNbIotConnected) {
                ServiceStateTracker.this.log("Satellite connection state is changed to " + zIsInConnectedState);
                this.mSatelliteNbIotConnected = zIsInConnectedState;
                this.mUpdateSatelliteCarrierDisplay = true;
                ServiceStateTracker.this.pollState();
            }
        }

        public boolean isInConnectedState() {
            int selectedSatelliteSubId;
            SatelliteController satelliteController = SatelliteController.getInstance();
            if (satelliteController == null || (selectedSatelliteSubId = satelliteController.getSelectedSatelliteSubId()) == -1 || selectedSatelliteSubId != ServiceStateTracker.this.mPhone.getSubId() || selectedSatelliteSubId == satelliteController.getNtnOnlySubscriptionId()) {
                return false;
            }
            return satelliteController.isInConnectedState();
        }

        public boolean needToUpdateSatelliteCarrierDisplay() {
            return this.mUpdateSatelliteCarrierDisplay;
        }

        public void doneForUpdateSatelliteCarrierDisplay() {
            this.mUpdateSatelliteCarrierDisplay = false;
        }
    }

    public ServiceStateTracker(GsmCdmaPhone gsmCdmaPhone, CommandsInterface commandsInterface, FeatureFlags featureFlags) {
        SstSubscriptionsChangedListener sstSubscriptionsChangedListener = new SstSubscriptionsChangedListener();
        this.mOnSubscriptionsChangedListener = sstSubscriptionsChangedListener;
        this.mRoamingLog = new LocalLog(8);
        this.mAttachLog = new LocalLog(8);
        this.mPhoneTypeLog = new LocalLog(8);
        this.mRatLog = new LocalLog(16);
        this.mRadioPowerLog = new LocalLog(16);
        this.mCdnrLogs = new LocalLog(64);
        this.mIsSimReadyForDisplay = false;
        this.mMaxDataCalls = 1;
        this.mNewMaxDataCalls = 1;
        this.mReasonDataDenied = -1;
        this.mNewReasonDataDenied = -1;
        this.mGsmVoiceRoaming = false;
        this.mGsmDataRoaming = false;
        this.mEmergencyOnly = false;
        this.mCSEmergencyOnly = false;
        this.mPSEmergencyOnly = false;
        this.mIsSimReady = false;
        this.mLastKnownNetworkCountry = "";
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.ServiceStateTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.intent.action.LOCALE_CHANGED")) {
                    ServiceStateTracker.this.log("ACTION_LOCALE_CHANGED");
                    ServiceStateTracker.this.pollState();
                    ServiceStateTracker.this.updateCarrierDisplayName();
                } else if (action.equals("android.telephony.action.NETWORK_COUNTRY_CHANGED")) {
                    if (ServiceStateTracker.this.mLastKnownNetworkCountry.equals(intent.getStringExtra("android.telephony.extra.LAST_KNOWN_NETWORK_COUNTRY"))) {
                        return;
                    }
                    ServiceStateTracker.this.updateCarrierDisplayName();
                }
            }
        };
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.ServiceStateTracker$$ExternalSyntheticLambda5
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                this.f$0.lambda$new$0(i, i2, i3, i4);
            }
        };
        this.mCarrierConfigChangeListener = carrierConfigChangeListener;
        this.mRegistrationState = -1;
        this.mCurrentCarrier = null;
        this.mRegStateManagers = new SparseArray<>();
        this.mLastKnownAreaCode = Integer.MAX_VALUE;
        this.mAccessNetworksManagerCallback = null;
        this.mSatelliteModemStateListener = null;
        this.mPhoneTypeUpdated = false;
        this.mNtnMode = 0;
        this.mOldNtnMode = 0;
        this.mNitzState = TelephonyComponentFactory.getInstance().inject(NitzStateMachine.class.getName()).makeNitzStateMachine(gsmCdmaPhone);
        this.mPhone = gsmCdmaPhone;
        this.mCi = commandsInterface;
        this.mFeatureFlags = featureFlags;
        this.mServiceStateStats = new ServiceStateStats(gsmCdmaPhone);
        this.mCdnr = new CarrierDisplayNameResolver(gsmCdmaPhone);
        this.mRatRatcheter = new RatRatcheter(gsmCdmaPhone);
        this.mVoiceCapable = ((TelephonyManager) gsmCdmaPhone.getContext().getSystemService("phone")).isVoiceCapable();
        UiccController uiccController = UiccController.getInstance();
        this.mUiccController = uiccController;
        uiccController.registerForIccChanged(this, 42, null);
        this.mCi.registerForCellInfoList(this, 44, null);
        this.mCi.registerForPhysicalChannelConfiguration(this, 55, null);
        this.mSubscriptionManagerService = SubscriptionManagerService.getInstance();
        SubscriptionManager subscriptionManagerFrom = SubscriptionManager.from(gsmCdmaPhone.getContext());
        this.mSubscriptionManager = subscriptionManagerFrom;
        subscriptionManagerFrom.addOnSubscriptionsChangedListener(new HandlerExecutor(this), sstSubscriptionsChangedListener);
        this.mRestrictedState = new RestrictedState();
        this.mCarrierConfig = getCarrierConfig();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) gsmCdmaPhone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.internal.telephony.ServiceStateTracker$$ExternalSyntheticLambda6
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, carrierConfigChangeListener);
        }
        AccessNetworksManager accessNetworksManager = gsmCdmaPhone.getAccessNetworksManager();
        this.mAccessNetworksManager = accessNetworksManager;
        ServiceState serviceState = new ServiceState();
        this.mOutOfServiceSS = serviceState;
        serviceState.setOutOfService(accessNetworksManager.isInLegacyMode(), false);
        for (int i : accessNetworksManager.getAvailableTransports()) {
            this.mRegStateManagers.append(i, new NetworkRegistrationManager(i, gsmCdmaPhone));
            this.mRegStateManagers.get(i).registerForNetworkRegistrationInfoChanged(this, 2, null);
        }
        this.mLocaleTracker = TelephonyComponentFactory.getInstance().inject(LocaleTracker.class.getName()).makeLocaleTracker(this.mPhone, this.mNitzState, getLooper(), featureFlags);
        this.mCi.registerForImsNetworkStateChanged(this, 46, null);
        this.mCi.registerForRadioStateChanged(this, 1, null);
        this.mCi.setOnNITZTime(this, 11, null);
        ContentResolver contentResolver = gsmCdmaPhone.getContext().getContentResolver();
        this.mCr = contentResolver;
        int i2 = Settings.Global.getInt(contentResolver, "airplane_mode_on", 0);
        int i3 = Settings.Global.getInt(contentResolver, "enable_cellular_on_boot", getDefaultEnableCellularOnBoot());
        boolean z = i3 > 0 && i2 <= 0;
        this.mDesiredPowerState = z;
        if (!z) {
            this.mRadioPowerOffReasons.add(0);
        }
        this.mRadioPowerLog.log("init : airplane mode = " + i2 + " enableCellularOnBoot = " + i3);
        this.mPhone.getCarrierActionAgent().registerForCarrierAction(1, this, 51, null, false);
        Context context = this.mPhone.getContext();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        intentFilter.addAction("android.telephony.action.NETWORK_COUNTRY_CHANGED");
        context.registerReceiver(this.mIntentReceiver, intentFilter);
        this.mCi.setOnRestrictedStateChanged(this, 23, null);
        requestInitialPollState();
        CarrierServiceStateTracker carrierServiceStateTracker = new CarrierServiceStateTracker(gsmCdmaPhone, this, featureFlags);
        this.mCSST = carrierServiceStateTracker;
        registerForNetworkAttached(carrierServiceStateTracker, 101, null);
        registerForNetworkDetached(this.mCSST, 102, null);
        registerForDataConnectionAttached(1, this.mCSST, NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING, null);
        registerForDataConnectionDetached(1, this.mCSST, NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE, null);
        registerForImsCapabilityChanged(this.mCSST, NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION, null);
        this.mDataDisconnectedCallback = new DataNetworkController.DataNetworkControllerCallback(new Executor() { // from class: com.android.internal.telephony.ServiceStateTracker$$ExternalSyntheticLambda6
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.ServiceStateTracker.2
            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onAnyDataNetworkExistingChanged(boolean z2, boolean z3) {
                ServiceStateTracker.this.log("onAnyDataNetworkExistingChanged: anyData=" + z2 + " anyCellularData=" + z3);
                if (z2) {
                    ServiceStateTracker serviceStateTracker = ServiceStateTracker.this;
                    FeatureFlags featureFlags2 = serviceStateTracker.mFeatureFlags;
                    if (serviceStateTracker.mDeviceShuttingDown || z3) {
                        return;
                    }
                }
                ServiceStateTracker.this.sendEmptyMessage(49);
            }
        };
        AccessNetworksManager.AccessNetworksManagerCallback accessNetworksManagerCallback = new AccessNetworksManager.AccessNetworksManagerCallback(new Executor() { // from class: com.android.internal.telephony.ServiceStateTracker$$ExternalSyntheticLambda6
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.ServiceStateTracker.3
            @Override // com.android.internal.telephony.data.AccessNetworksManager.AccessNetworksManagerCallback
            public void onPreferredTransportChanged(int i4, boolean z2) {
                boolean zIsAnyApnOnIwlan = ServiceStateTracker.this.mAccessNetworksManager.isAnyApnOnIwlan();
                if (ServiceStateTracker.this.mSS.isIwlanPreferred() != zIsAnyApnOnIwlan) {
                    ServiceStateTracker.this.log("onPreferredTransportChanged: IwlanPreferred is changed to " + zIsAnyApnOnIwlan);
                    ServiceStateTracker serviceStateTracker = ServiceStateTracker.this;
                    serviceStateTracker.combinePsRegistrationStates(serviceStateTracker.mSS);
                    GsmCdmaPhone gsmCdmaPhone2 = ServiceStateTracker.this.mPhone;
                    gsmCdmaPhone2.notifyServiceStateChanged(gsmCdmaPhone2.getServiceState());
                }
            }
        };
        this.mAccessNetworksManagerCallback = accessNetworksManagerCallback;
        this.mAccessNetworksManager.registerCallback(accessNetworksManagerCallback);
        MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).init(this.mPhone, this);
        MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).needGUTIWork(context, this);
    }

    private int getDefaultEnableCellularOnBoot() {
        return this.mPhone.getContext().getResources().getBoolean(R.bool.config_hideNavBarForKeyboard) ? 1 : 0;
    }

    public void requestInitialPollState() {
        NetworkRegistrationInfo networkRegistrationInfo;
        this.mPhoneTypeUpdated = true;
        ServiceState serviceState = this.mSS;
        if (serviceState != null && serviceState.getVoiceRoaming()) {
            this.mVoiceRoamingOffRegistrants.notifyRegistrants();
        }
        ServiceState serviceState2 = this.mSS;
        if (serviceState2 != null && serviceState2.getDataRoaming()) {
            this.mDataRoamingOffRegistrants.notifyRegistrants();
        }
        ServiceState serviceState3 = this.mSS;
        if (serviceState3 != null && serviceState3.getState() == 0) {
            this.mNetworkDetachedRegistrants.notifyRegistrants();
        }
        for (int i : this.mAccessNetworksManager.getAvailableTransports()) {
            ServiceState serviceState4 = this.mSS;
            if (serviceState4 != null && (networkRegistrationInfo = serviceState4.getNetworkRegistrationInfo(2, i)) != null && networkRegistrationInfo.isInService() && this.mDetachedRegistrants.get(i) != null) {
                this.mDetachedRegistrants.get(i).notifyRegistrants();
            }
        }
        ServiceState serviceState5 = new ServiceState();
        this.mSS = serviceState5;
        serviceState5.setOutOfService(this.mAccessNetworksManager.isInLegacyMode(), false);
        ServiceState serviceState6 = new ServiceState();
        this.mNewSS = serviceState6;
        serviceState6.setOutOfService(this.mAccessNetworksManager.isInLegacyMode(), false);
        this.mLastCellInfoReqTime = 0L;
        this.mLastCellInfoList = null;
        this.mStartedGprsRegCheck = false;
        this.mReportedGprsNoReg = false;
        this.mLastNitzData = null;
        this.mNitzState.handleNetworkUnavailable();
        this.mCellIdentity = null;
        this.mPhone.getSignalStrengthController().setSignalStrengthDefaultValues();
        this.mLastKnownCellIdentity = null;
        cancelPollState();
        this.mPhone.unregisterForSimRecordsLoaded(this);
        onUpdateIccAvailability();
        setDataNetworkTypeForPhone(0);
        this.mPhone.getSignalStrengthController().getSignalStrengthFromCi();
        sendMessage(obtainMessage(50));
        logPhoneType();
        notifyVoiceRegStateRilRadioTechnologyChanged();
        for (int i2 : this.mAccessNetworksManager.getAvailableTransports()) {
            notifyDataRegStateRilRadioTechnologyChanged(i2);
        }
    }

    public void requestShutdown() {
        if (this.mDeviceShuttingDown) {
            return;
        }
        this.mDeviceShuttingDown = true;
        this.mDesiredPowerState = false;
        setPowerStateToDesired();
    }

    public int getRadioPowerOffDelayTimeoutForImsRegistration() {
        return this.mPhone.getContext().getResources().getInteger(R.integer.config_dreamCloseAnimationDuration);
    }

    public void dispose() {
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener;
        this.mPhone.getSignalStrengthController().dispose();
        this.mUiccController.unregisterForIccChanged(this);
        this.mCi.unregisterForCellInfoList(this);
        this.mCi.unregisterForPhysicalChannelConfiguration(this);
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mOnSubscriptionsChangedListener);
        this.mCi.unregisterForImsNetworkStateChanged(this);
        this.mPhone.getCarrierActionAgent().unregisterForCarrierAction(this, 1);
        this.mPhone.getContext().unregisterReceiver(this.mIntentReceiver);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null && (carrierConfigChangeListener = this.mCarrierConfigChangeListener) != null) {
            carrierConfigManager.unregisterCarrierConfigChangeListener(carrierConfigChangeListener);
        }
        CarrierServiceStateTracker carrierServiceStateTracker = this.mCSST;
        if (carrierServiceStateTracker != null) {
            carrierServiceStateTracker.dispose();
            this.mCSST = null;
        }
        AccessNetworksManager.AccessNetworksManagerCallback accessNetworksManagerCallback = this.mAccessNetworksManagerCallback;
        if (accessNetworksManagerCallback != null) {
            this.mAccessNetworksManager.unregisterCallback(accessNetworksManagerCallback);
            this.mAccessNetworksManagerCallback = null;
        }
        MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).dispose(this.mPhone);
    }

    public boolean getDesiredPowerState() {
        return this.mDesiredPowerState;
    }

    public boolean getPowerStateFromCarrier() {
        return !this.mRadioPowerOffReasons.contains(2);
    }

    public List<PhysicalChannelConfig> getPhysicalChannelConfigList() {
        return this.mLastPhysicalChannelConfigList;
    }

    protected void notifyVoiceRegStateRilRadioTechnologyChanged() {
        int rilVoiceRadioTechnology = this.mSS.getRilVoiceRadioTechnology();
        int state = this.mSS.getState();
        log("notifyVoiceRegStateRilRadioTechnologyChanged: vrs=" + state + " rat=" + rilVoiceRadioTechnology);
        this.mVoiceRegStateOrRatChangedRegistrants.notifyResult(new Pair(Integer.valueOf(state), Integer.valueOf(rilVoiceRadioTechnology)));
    }

    private Pair getRegistrationInfo(int i) {
        NetworkRegistrationInfo networkRegistrationInfo = this.mSS.getNetworkRegistrationInfo(2, i);
        if (networkRegistrationInfo == null) {
            return null;
        }
        return new Pair(Integer.valueOf(regCodeToServiceState(networkRegistrationInfo.getNetworkRegistrationState())), Integer.valueOf(ServiceState.networkTypeToRilRadioTechnology(networkRegistrationInfo.getAccessNetworkTechnology())));
    }

    protected void notifyDataRegStateRilRadioTechnologyChanged(int i) {
        Pair registrationInfo;
        RegistrantList registrantList = (RegistrantList) this.mDataRegStateOrRatChangedRegistrants.get(i);
        if (registrantList == null || (registrationInfo = getRegistrationInfo(i)) == null) {
            return;
        }
        registrantList.notifyResult(registrationInfo);
    }

    protected void useDataRegStateForDataOnlyDevices() {
        if (this.mVoiceCapable) {
            return;
        }
        log("useDataRegStateForDataOnlyDevice: VoiceRegState=" + this.mNewSS.getState() + " DataRegState=" + this.mNewSS.getDataRegistrationState());
        ServiceState serviceState = this.mNewSS;
        serviceState.setVoiceRegState(serviceState.getDataRegistrationState());
    }

    public void registerForVoiceRoamingOn(Handler handler, int i, Object obj) {
        Registrant registrant = new Registrant(handler, i, obj);
        this.mVoiceRoamingOnRegistrants.add(registrant);
        if (this.mSS.getVoiceRoaming()) {
            registrant.notifyRegistrant();
        }
    }

    public void unregisterForVoiceRoamingOn(Handler handler) {
        this.mVoiceRoamingOnRegistrants.remove(handler);
    }

    public void registerForVoiceRoamingOff(Handler handler, int i, Object obj) {
        Registrant registrant = new Registrant(handler, i, obj);
        this.mVoiceRoamingOffRegistrants.add(registrant);
        if (this.mSS.getVoiceRoaming()) {
            return;
        }
        registrant.notifyRegistrant();
    }

    public void unregisterForVoiceRoamingOff(Handler handler) {
        this.mVoiceRoamingOffRegistrants.remove(handler);
    }

    public void registerForDataRoamingOn(Handler handler, int i, Object obj) {
        Registrant registrant = new Registrant(handler, i, obj);
        this.mDataRoamingOnRegistrants.add(registrant);
        if (this.mSS.getDataRoaming()) {
            registrant.notifyRegistrant();
        }
    }

    public void unregisterForDataRoamingOn(Handler handler) {
        this.mDataRoamingOnRegistrants.remove(handler);
    }

    public void registerForDataRoamingOff(Handler handler, int i, Object obj, boolean z) {
        Registrant registrant = new Registrant(handler, i, obj);
        this.mDataRoamingOffRegistrants.add(registrant);
        if (!z || this.mSS.getDataRoaming()) {
            return;
        }
        registrant.notifyRegistrant();
    }

    public void unregisterForDataRoamingOff(Handler handler) {
        this.mDataRoamingOffRegistrants.remove(handler);
    }

    public void reRegisterNetwork(Message message) {
        this.mCi.getAllowedNetworkTypesBitmap(obtainMessage(19, message));
    }

    public Set<Integer> getRadioPowerOffReasons() {
        return Set.copyOf(this.mRadioPowerOffReasons);
    }

    public void clearAllRadioOffReasons() {
        this.mRadioPowerOffReasons.clear();
    }

    public final void setRadioPower(boolean z) {
        if (TelephonyCommonUtilsStub.get().isDemoBuild()) {
            log("setRadioPower the current MIUI is demo version.");
            z = false;
        }
        setRadioPower(z, false, false, false);
    }

    public void setRadioPower(boolean z, boolean z2, boolean z3, boolean z4) {
        setRadioPowerForReason(z, z2, z3, z4, 0);
    }

    public void setRadioPowerForReason(boolean z, boolean z2, boolean z3, boolean z4, int i) {
        log("setRadioPower power " + z + " forEmergencyCall " + z2 + " forceApply " + z4 + " reason " + i);
        if (!z) {
            this.mRadioPowerOffReasons.add(Integer.valueOf(i));
        } else if (z2) {
            clearAllRadioOffReasons();
        } else {
            this.mRadioPowerOffReasons.remove(Integer.valueOf(i));
        }
        if (z == this.mDesiredPowerState && !z4) {
            log("setRadioPower mDesiredPowerState is already " + z + " Do nothing.");
            return;
        }
        if (z && !this.mRadioPowerOffReasons.isEmpty()) {
            log("setRadioPowerForReason power: " + z + " forEmergencyCall= " + z2 + " isSelectedPhoneForEmergencyCall: " + z3 + " forceApply " + z4 + " reason: " + i + " will not power on the radio as it is powered off for the following reasons: " + this.mRadioPowerOffReasons + ".");
            return;
        }
        this.mDesiredPowerState = z;
        setPowerStateToDesired(z2, z3, z4);
        if (this.mDesiredPowerState) {
            SatelliteController.getInstance().onSetCellularRadioPowerStateRequested(true);
        }
    }

    public void enableSingleLocationUpdate(WorkSource workSource) {
        if (this.mWantSingleLocationUpdate || this.mWantContinuousLocationUpdates) {
            return;
        }
        this.mWantSingleLocationUpdate = true;
        this.mCi.setLocationUpdates(true, workSource, obtainMessage(18));
    }

    public void enableLocationUpdates() {
        if (this.mWantSingleLocationUpdate || this.mWantContinuousLocationUpdates) {
            return;
        }
        this.mWantContinuousLocationUpdates = true;
        this.mCi.setLocationUpdates(true, null, obtainMessage(18));
    }

    protected void disableSingleLocationUpdate() {
        this.mWantSingleLocationUpdate = false;
        if (this.mWantContinuousLocationUpdates) {
            return;
        }
        this.mCi.setLocationUpdates(false, null, null);
    }

    public void disableLocationUpdates() {
        this.mWantContinuousLocationUpdates = false;
        if (this.mWantSingleLocationUpdate) {
            return;
        }
        this.mCi.setLocationUpdates(false, null, null);
    }

    /* JADX WARN: Code duplicated, block: B:125:0x026e A[Catch: all -> 0x0289, TryCatch #0 {all -> 0x0289, blocks: (B:123:0x026a, B:125:0x026e, B:126:0x0276, B:128:0x027c, B:131:0x028c, B:132:0x0291), top: B:200:0x026a }] */
    /* JADX WARN: Code duplicated, block: B:128:0x027c A[Catch: all -> 0x0289, LOOP:1: B:126:0x0276->B:128:0x027c, LOOP_END, TryCatch #0 {all -> 0x0289, blocks: (B:123:0x026a, B:125:0x026e, B:126:0x0276, B:128:0x027c, B:131:0x028c, B:132:0x0291), top: B:200:0x026a }] */
    /* JADX WARN: Code duplicated, block: B:200:0x026a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Switch 'out' block B:30:0x004c for B:28:0x0036 already processed. Defaulting to fallback option. */
    @Override // android.os.Handler
    public void handleMessage(Message message) {
        ServiceStateTracker serviceStateTracker;
        ServiceStateTracker serviceStateTracker2;
        Throwable th;
        int i = message.what;
        boolean z = true;
        if (i == 1) {
            serviceStateTracker = this;
            RadioPowerStateStats.onRadioStateChanged(serviceStateTracker.mCi.getRadioState());
            serviceStateTracker.setPowerStateToDesired();
            serviceStateTracker.pollStateInternal(true);
        } else if (i == 2) {
            serviceStateTracker = this;
            serviceStateTracker.pollStateInternal(true);
        } else if (i == 4 || i == 5 || i == 6 || i == 7) {
            serviceStateTracker = this;
            serviceStateTracker.handlePollStateResult(i, (AsyncResult) message.obj);
        } else {
            boolean z2 = false;
            if (i != 11) {
                if (i == 38) {
                    serviceStateTracker2 = this;
                    synchronized (serviceStateTracker2) {
                        serviceStateTracker2.mPendingRadioPowerOffReason = 0;
                        serviceStateTracker2.log("Wait for all data networks due to " + DataNetwork.tearDownReasonToString(serviceStateTracker2.mPendingRadioPowerOffReason) + " torn down timed out. Power off now.");
                        serviceStateTracker2.hangupAndPowerOff();
                    }
                } else if (i == 58) {
                    serviceStateTracker2 = this;
                    serviceStateTracker2.pollStateInternal(false);
                } else if (i == 65) {
                    serviceStateTracker2 = this;
                    serviceStateTracker2.log("EVENT_TELECOM_VOICE_SERVICE_STATE_OVERRIDE_CHANGED");
                    if (serviceStateTracker2.mSS.getState() != 0) {
                        GsmCdmaPhone gsmCdmaPhone = serviceStateTracker2.mPhone;
                        gsmCdmaPhone.notifyServiceStateChanged(gsmCdmaPhone.getServiceState());
                    }
                } else if (i != 62) {
                    List<CellInfo> list = null;
                    if (i != 63) {
                        switch (i) {
                            case 14:
                                serviceStateTracker2 = this;
                                serviceStateTracker2.log("EVENT_POLL_STATE_NETWORK_SELECTION_MODE");
                                serviceStateTracker2.handlePollStateResult(message.what, (AsyncResult) message.obj);
                                break;
                            case 15:
                                serviceStateTracker2 = this;
                                AsyncResult asyncResult = (AsyncResult) message.obj;
                                if (asyncResult.exception == null) {
                                    CellIdentity cellIdentity = ((NetworkRegistrationInfo) asyncResult.result).getCellIdentity();
                                    serviceStateTracker2.updateOperatorNameForCellIdentity(cellIdentity);
                                    serviceStateTracker2.mCellIdentity = cellIdentity;
                                    serviceStateTracker2.mPhone.notifyLocationChanged(serviceStateTracker2.getCellIdentity());
                                }
                                serviceStateTracker2.disableSingleLocationUpdate();
                                break;
                            case 16:
                                serviceStateTracker2 = this;
                                serviceStateTracker2.log("EVENT_SIM_RECORDS_LOADED: what=" + message.what);
                                serviceStateTracker2.mCdnr.updateEfFromUsim((SIMRecords) serviceStateTracker2.mIccRecords);
                                serviceStateTracker2.pollState();
                                serviceStateTracker2.updateCarrierDisplayName();
                                if (TelephonyCommonUtilsStub.get().isSupportDomesticRoaming()) {
                                    serviceStateTracker2.setRoamingType(serviceStateTracker2.mSS);
                                }
                                break;
                            case 17:
                                serviceStateTracker2 = this;
                                serviceStateTracker2.mIsSimReadyForDisplay = false;
                                serviceStateTracker2.mPrevSubId = -1;
                                serviceStateTracker2.mIsSimReady = true;
                                serviceStateTracker2.pollStateInternal(false);
                                break;
                            case 18:
                                serviceStateTracker2 = this;
                                if (((AsyncResult) message.obj).exception == null) {
                                    serviceStateTracker2.mRegStateManagers.get(1).requestNetworkRegistrationInfo(1, serviceStateTracker2.obtainMessage(15, null));
                                }
                                break;
                            case 19:
                                serviceStateTracker2 = this;
                                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                                if (asyncResult2.exception == null) {
                                    serviceStateTracker2.mAllowedNetworkTypes = ((int[]) asyncResult2.result)[0];
                                } else {
                                    serviceStateTracker2.mAllowedNetworkTypes = RadioAccessFamily.getRafFromNetworkType(7);
                                }
                                serviceStateTracker2.mCi.setAllowedNetworkTypesBitmap(RadioAccessFamily.getRafFromNetworkType(7), serviceStateTracker2.obtainMessage(20, asyncResult2.userObj));
                                break;
                            case 20:
                                serviceStateTracker2 = this;
                                serviceStateTracker2.mCi.setAllowedNetworkTypesBitmap(serviceStateTracker2.mAllowedNetworkTypes, serviceStateTracker2.obtainMessage(21, ((AsyncResult) message.obj).userObj));
                                break;
                            case 21:
                                serviceStateTracker2 = this;
                                AsyncResult asyncResult3 = (AsyncResult) message.obj;
                                Object obj = asyncResult3.userObj;
                                if (obj != null) {
                                    AsyncResult.forMessage((Message) obj).exception = asyncResult3.exception;
                                    ((Message) asyncResult3.userObj).sendToTarget();
                                }
                                break;
                            case 22:
                                serviceStateTracker2 = this;
                                ServiceState serviceState = serviceStateTracker2.mSS;
                                if (serviceState != null && !serviceStateTracker2.isGprsConsistent(serviceState.getDataRegistrationState(), serviceStateTracker2.mSS.getState())) {
                                    EventLog.writeEvent(EventLogTags.DATA_NETWORK_REGISTRATION_FAIL, serviceStateTracker2.mSS.getOperatorNumeric(), Long.valueOf(getCidFromCellIdentity(serviceStateTracker2.mCellIdentity)));
                                    serviceStateTracker2.mReportedGprsNoReg = true;
                                }
                                serviceStateTracker2.mStartedGprsRegCheck = false;
                                break;
                            case 23:
                                serviceStateTracker2 = this;
                                serviceStateTracker2.log("EVENT_RESTRICTED_STATE_CHANGED");
                                serviceStateTracker2.onRestrictedStateChanged((AsyncResult) message.obj);
                                break;
                            default:
                                switch (i) {
                                    case 42:
                                        serviceStateTracker2 = this;
                                        if (serviceStateTracker2.isSimAbsent()) {
                                            serviceStateTracker2.log("EVENT_ICC_CHANGED: SIM absent");
                                            serviceStateTracker2.cancelAllNotifications();
                                            serviceStateTracker2.mCdnr.updateEfFromUsim(null);
                                        }
                                        serviceStateTracker2.onUpdateIccAvailability();
                                        UiccCardApplication uiccCardApplication = serviceStateTracker2.mUiccApplication;
                                        if (uiccCardApplication == null || uiccCardApplication.getState() != IccCardApplicationStatus.AppState.APPSTATE_READY) {
                                            serviceStateTracker2.mIsSimReady = false;
                                            serviceStateTracker2.updateCarrierDisplayName();
                                        }
                                        break;
                                    case 43:
                                    case 44:
                                        serviceStateTracker2 = this;
                                        Object obj2 = message.obj;
                                        if (obj2 != null) {
                                            AsyncResult asyncResult4 = (AsyncResult) obj2;
                                            if (asyncResult4.exception != null) {
                                                serviceStateTracker2.log("EVENT_GET_CELL_INFO_LIST: error ret null, e=" + asyncResult4.exception);
                                                th = asyncResult4.exception;
                                            } else {
                                                Object obj3 = asyncResult4.result;
                                                if (obj3 == null) {
                                                    serviceStateTracker2.loge("Invalid CellInfo result");
                                                } else {
                                                    List<CellInfo> list2 = (List) obj3;
                                                    serviceStateTracker2.updateOperatorNameForCellInfo(list2);
                                                    serviceStateTracker2.mLastCellInfoList = list2;
                                                    serviceStateTracker2.mPhone.notifyCellInfo(list2);
                                                    list = list2;
                                                    th = null;
                                                }
                                            }
                                            synchronized (serviceStateTracker2.mPendingCellInfoRequests) {
                                                try {
                                                    if (serviceStateTracker2.mIsPendingCellInfoRequest) {
                                                        serviceStateTracker2.mIsPendingCellInfoRequest = false;
                                                        for (Message message2 : serviceStateTracker2.mPendingCellInfoRequests) {
                                                            AsyncResult.forMessage(message2, list, th);
                                                            message2.sendToTarget();
                                                        }
                                                        serviceStateTracker2.mPendingCellInfoRequests.clear();
                                                    }
                                                } catch (Throwable th2) {
                                                    throw th2;
                                                }
                                                break;
                                            }
                                        } else {
                                            synchronized (serviceStateTracker2.mPendingCellInfoRequests) {
                                                try {
                                                    if (serviceStateTracker2.mIsPendingCellInfoRequest) {
                                                        if (SystemClock.elapsedRealtime() - serviceStateTracker2.mLastCellInfoReqTime >= 2000) {
                                                            serviceStateTracker2.loge("Timeout waiting for CellInfo; (everybody panic)!");
                                                            serviceStateTracker2.mLastCellInfoList = null;
                                                        }
                                                    }
                                                } catch (Throwable th3) {
                                                    throw th3;
                                                }
                                            }
                                        }
                                        th = null;
                                        synchronized (serviceStateTracker2.mPendingCellInfoRequests) {
                                            if (serviceStateTracker2.mIsPendingCellInfoRequest) {
                                                serviceStateTracker2.mIsPendingCellInfoRequest = false;
                                                while (r0.hasNext()) {
                                                    AsyncResult.forMessage(message2, list, th);
                                                    message2.sendToTarget();
                                                }
                                                serviceStateTracker2.mPendingCellInfoRequests.clear();
                                            }
                                            break;
                                        }
                                        break;
                                    case 45:
                                        serviceStateTracker2 = this;
                                        serviceStateTracker2.log("EVENT_CHANGE_IMS_STATE:");
                                        serviceStateTracker2.setPowerStateToDesired();
                                        break;
                                    case 46:
                                        serviceStateTracker2 = this;
                                        serviceStateTracker2.mCi.getImsRegistrationState(serviceStateTracker2.obtainMessage(47));
                                        break;
                                    case 47:
                                        serviceStateTracker2 = this;
                                        AsyncResult asyncResult5 = (AsyncResult) message.obj;
                                        if (asyncResult5.exception == null) {
                                            z = ((int[]) asyncResult5.result)[0] == 1;
                                            serviceStateTracker2.mPhone.setImsRegistrationState(z);
                                            serviceStateTracker2.mImsRegistered = z;
                                        }
                                        break;
                                    case 48:
                                        serviceStateTracker2 = this;
                                        serviceStateTracker2.log("EVENT_IMS_CAPABILITY_CHANGED");
                                        serviceStateTracker2.updateCarrierDisplayName();
                                        serviceStateTracker2.mImsCapabilityChangedRegistrants.notifyRegistrants();
                                        break;
                                    case 49:
                                        serviceStateTracker2 = this;
                                        serviceStateTracker2.log("EVENT_ALL_DATA_DISCONNECTED");
                                        synchronized (serviceStateTracker2) {
                                            try {
                                                if (serviceStateTracker2.mPendingRadioPowerOffReason == 0) {
                                                    return;
                                                }
                                                boolean z3 = true;
                                                for (Phone phone : PhoneFactory.getPhones()) {
                                                    if (phone.getDataNetworkController().areAllDataDisconnected(!serviceStateTracker2.mDeviceShuttingDown)) {
                                                        phone.getDataNetworkController().unregisterDataNetworkControllerCallback(serviceStateTracker2.mDataDisconnectedCallback);
                                                    } else {
                                                        serviceStateTracker2.log("Still waiting for all data disconnected on phone: " + phone.getPhoneId());
                                                        z3 = false;
                                                    }
                                                }
                                                if (z3) {
                                                    serviceStateTracker2.mPendingRadioPowerOffReason = 0;
                                                    serviceStateTracker2.removeMessages(38);
                                                    serviceStateTracker2.log("Data disconnected for all phones, turn radio off now.");
                                                    serviceStateTracker2.hangupAndPowerOff();
                                                }
                                            } catch (Throwable th4) {
                                                throw th4;
                                            }
                                            break;
                                        }
                                        break;
                                    case 50:
                                        serviceStateTracker2 = this;
                                        serviceStateTracker2.pollStateInternal(true);
                                        break;
                                    case 51:
                                        AsyncResult asyncResult6 = (AsyncResult) message.obj;
                                        if (asyncResult6.exception == null) {
                                            boolean zBooleanValue = ((Boolean) asyncResult6.result).booleanValue();
                                            log("EVENT_RADIO_POWER_FROM_CARRIER: " + zBooleanValue);
                                            serviceStateTracker2 = this;
                                            serviceStateTracker2.setRadioPowerForReason(zBooleanValue, false, false, false, 2);
                                        } else {
                                            serviceStateTracker = this;
                                        }
                                        break;
                                    default:
                                        switch (i) {
                                            case 53:
                                                log("EVENT_IMS_SERVICE_STATE_CHANGED");
                                                if (this.mSS.getState() != 0) {
                                                    GsmCdmaPhone gsmCdmaPhone2 = this.mPhone;
                                                    gsmCdmaPhone2.notifyServiceStateChanged(gsmCdmaPhone2.getServiceState());
                                                }
                                                break;
                                            case 54:
                                                log("EVENT_RADIO_POWER_OFF_DONE");
                                                if (this.mDeviceShuttingDown && this.mCi.getRadioState() != 2) {
                                                    this.mCi.requestShutdown(null);
                                                }
                                                AsyncResult asyncResult7 = (AsyncResult) message.obj;
                                                if (asyncResult7.exception != null) {
                                                    loge("EVENT_RADIO_POWER_OFF_DONE: exception=" + asyncResult7.exception);
                                                    SatelliteController.getInstance().onPowerOffCellularRadioFailed();
                                                }
                                                break;
                                            case 55:
                                                AsyncResult asyncResult8 = (AsyncResult) message.obj;
                                                if (asyncResult8.exception == null) {
                                                    List<PhysicalChannelConfig> list3 = (List) asyncResult8.result;
                                                    this.mLastPhysicalChannelConfigList = list3;
                                                    if (updateNrStateFromPhysicalChannelConfigs(list3, this.mSS)) {
                                                        this.mNrStateChangedRegistrants.notifyRegistrants();
                                                        z2 = true;
                                                    }
                                                    if (updateNrFrequencyRangeFromPhysicalChannelConfigs(list3, this.mSS)) {
                                                        this.mNrFrequencyChangedRegistrants.notifyRegistrants();
                                                    } else {
                                                        z = z2;
                                                    }
                                                    boolean zUpdateBandwidths = RatRatcheter.updateBandwidths(getBandwidthsFromConfigs(list3), this.mSS) | z;
                                                    this.mPhone.notifyPhysicalChannelConfig(list3);
                                                    if (zUpdateBandwidths) {
                                                        GsmCdmaPhone gsmCdmaPhone3 = this.mPhone;
                                                        gsmCdmaPhone3.notifyServiceStateChanged(gsmCdmaPhone3.getServiceState());
                                                        this.mServiceStateChangedRegistrants.notifyRegistrants();
                                                        this.mPhone.getVoiceCallSessionStats().onServiceStateChanged(this.mSS);
                                                        ImsPhone imsPhone = (ImsPhone) this.mPhone.getImsPhone();
                                                        if (imsPhone != null) {
                                                            imsPhone.getImsStats().onServiceStateChanged(this.mSS);
                                                        }
                                                        this.mServiceStateStats.onServiceStateChanged(this.mSS);
                                                    }
                                                }
                                                break;
                                            case 56:
                                                AsyncResult asyncResult9 = (AsyncResult) message.obj;
                                                if (asyncResult9 == null) {
                                                    loge("Invalid null response to getCellIdentity!");
                                                } else {
                                                    Message message3 = (Message) asyncResult9.userObj;
                                                    AsyncResult.forMessage(message3, getCellIdentity(), asyncResult9.exception);
                                                    message3.sendToTarget();
                                                }
                                                break;
                                            default:
                                                log("Unhandled message with number: " + message.what);
                                                break;
                                        }
                                        serviceStateTracker = this;
                                        break;
                                }
                                break;
                        }
                    } else {
                        serviceStateTracker2 = this;
                        serviceStateTracker2.log("EVENT_RESET_LAST_KNOWN_CELL_IDENTITY triggered");
                        serviceStateTracker2.mLastKnownCellIdentity = null;
                    }
                } else {
                    serviceStateTracker2 = this;
                    serviceStateTracker2.log("EVENT_POWER_OFF_RADIO_IMS_DEREG_TIMEOUT triggered");
                    serviceStateTracker2.powerOffRadioSafely();
                }
                serviceStateTracker = serviceStateTracker2;
            } else {
                Object[] objArr = (Object[]) ((AsyncResult) message.obj).result;
                serviceStateTracker = this;
                serviceStateTracker.setTimeFromNITZString((String) objArr[0], ((Long) objArr[1]).longValue(), objArr.length >= 3 ? ((Long) objArr[2]).longValue() : 0L);
            }
        }
        MiuiNetworkControllerStub.get(serviceStateTracker.mPhone.getPhoneId()).handleMessage(serviceStateTracker.mPhone, message);
    }

    private boolean isSimAbsent() {
        UiccCard uiccCard;
        UiccController uiccController = this.mUiccController;
        return uiccController == null || (uiccCard = uiccController.getUiccCard(this.mPhone.getPhoneId())) == null || uiccCard.getCardState() == IccCardStatus.CardState.CARDSTATE_ABSENT;
    }

    private static int[] getBandwidthsFromConfigs(List list) {
        return list.stream().map(new Function() { // from class: com.android.internal.telephony.ServiceStateTracker$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(((PhysicalChannelConfig) obj).getCellBandwidthDownlinkKhz());
            }
        }).mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray();
    }

    public ServiceState getServiceState() {
        return new ServiceState(this.mSS);
    }

    public void onAirplaneModeChanged(boolean z) {
        this.mLastNitzData = null;
        this.mNitzState.handleAirplaneModeChanged(z);
        this.mAirplaneModeChangedRegistrants.notifyResult(Boolean.valueOf(z));
    }

    protected Phone getPhone() {
        return this.mPhone;
    }

    protected void handlePollStateResult(int i, AsyncResult asyncResult) {
        if (asyncResult.userObj != this.mPollingContext) {
            return;
        }
        Throwable th = asyncResult.exception;
        if (th != null) {
            if (th instanceof IllegalStateException) {
                log("handlePollStateResult exception " + asyncResult.exception);
            }
            Throwable th2 = asyncResult.exception;
            CommandException.Error commandError = th2 instanceof CommandException ? ((CommandException) th2).getCommandError() : null;
            if (commandError == CommandException.Error.RADIO_NOT_AVAILABLE) {
                loge("handlePollStateResult: RIL returned RADIO_NOT_AVAILABLE.");
                if (this.mCi.getRadioState() == 1) {
                    cancelPollState();
                    onPollStateResultError(commandError);
                    return;
                } else {
                    handlePollStateInternalForRadioOffOrUnavailable(this.mCi.getRadioState() == 0);
                    pollStateDone();
                    return;
                }
            }
            if (commandError != CommandException.Error.OP_NOT_ALLOWED_BEFORE_REG_NW) {
                loge("handlePollStateResult: RIL returned an error where it must succeed: " + asyncResult.exception);
            }
        } else {
            try {
                handlePollStateResultMessage(i, asyncResult);
            } catch (RuntimeException e) {
                loge("Exception while polling service state. Probably malformed RIL response." + e);
            }
        }
        int[] iArr = this.mPollingContext;
        int i2 = iArr[0] - 1;
        iArr[0] = i2;
        if (i2 == 0) {
            this.mNewSS.setEmergencyOnly(this.mEmergencyOnly);
            combinePsRegistrationStates(this.mNewSS);
            updateOperatorNameForServiceState(this.mNewSS);
            if (needDiscardCurrentPollStateResult(this.mNewSS)) {
                return;
            }
            updateRoamingState();
            pollStateDone();
        }
    }

    protected boolean updateNrFrequencyRangeFromPhysicalChannelConfigs(List<PhysicalChannelConfig> list, ServiceState serviceState) {
        int iMax;
        if (list != null) {
            iMax = 0;
            for (PhysicalChannelConfig physicalChannelConfig : list) {
                if (isNrPhysicalChannelConfig(physicalChannelConfig) && isInternetPhysicalChannelConfig(physicalChannelConfig)) {
                    iMax = Math.max(iMax, physicalChannelConfig.getFrequencyRange());
                }
            }
        } else {
            iMax = 0;
        }
        boolean z = iMax != serviceState.getNrFrequencyRange();
        if (z) {
            log(String.format("NR frequency range changed from %s to %s.", ServiceState.frequencyRangeToString(serviceState.getNrFrequencyRange()), ServiceState.frequencyRangeToString(iMax)));
        }
        serviceState.setNrFrequencyRange(iMax);
        return z;
    }

    protected boolean updateNrStateFromPhysicalChannelConfigs(List<PhysicalChannelConfig> list, ServiceState serviceState) {
        boolean z;
        int nrState;
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
        if (networkRegistrationInfo == null || list == null) {
            return false;
        }
        Iterator<PhysicalChannelConfig> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            PhysicalChannelConfig next = it.next();
            if (isNrPhysicalChannelConfig(next) && isInternetPhysicalChannelConfig(next) && next.getConnectionStatus() == 2) {
                z = true;
                break;
            }
        }
        int nrState2 = networkRegistrationInfo.getNrState();
        if (networkRegistrationInfo.getAccessNetworkTechnology() == 13 && z) {
            nrState = 3;
        } else {
            networkRegistrationInfo.updateNrState();
            nrState = networkRegistrationInfo.getNrState();
        }
        boolean z2 = nrState != nrState2;
        if (z2) {
            log(String.format("NR state changed from %s to %s.", NetworkRegistrationInfo.nrStateToString(nrState2), NetworkRegistrationInfo.nrStateToString(nrState)));
        }
        networkRegistrationInfo.setNrState(nrState);
        serviceState.addNetworkRegistrationInfo(networkRegistrationInfo);
        return z2;
    }

    protected boolean isNrPhysicalChannelConfig(PhysicalChannelConfig physicalChannelConfig) {
        return physicalChannelConfig.getNetworkType() == 20;
    }

    private boolean isInternetPhysicalChannelConfig(PhysicalChannelConfig physicalChannelConfig) {
        for (int i : physicalChannelConfig.getContextIds()) {
            if (this.mPhone.getDataNetworkController().isInternetNetwork(i)) {
                return true;
            }
        }
        return false;
    }

    protected void combinePsRegistrationStates(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 2);
        NetworkRegistrationInfo networkRegistrationInfo2 = serviceState.getNetworkRegistrationInfo(2, 1);
        boolean zIsAnyApnOnIwlan = this.mAccessNetworksManager.isAnyApnOnIwlan();
        serviceState.setIwlanPreferred(zIsAnyApnOnIwlan);
        if (networkRegistrationInfo != null && networkRegistrationInfo.getAccessNetworkTechnology() == 18 && networkRegistrationInfo.getNetworkRegistrationState() == 1 && zIsAnyApnOnIwlan) {
            serviceState.setDataRegState(0);
        } else if (networkRegistrationInfo2 != null) {
            serviceState.setDataRegState(regCodeToServiceState(networkRegistrationInfo2.getNetworkRegistrationState()));
        }
        log("combinePsRegistrationStates: " + serviceState);
    }

    protected void handlePollStateResultMessage(int i, AsyncResult asyncResult) {
        if (i == 4) {
            NetworkRegistrationInfo networkRegistrationInfo = (NetworkRegistrationInfo) asyncResult.result;
            VoiceSpecificRegistrationInfo voiceSpecificInfo = networkRegistrationInfo.getVoiceSpecificInfo();
            int networkRegistrationState = networkRegistrationInfo.getNetworkRegistrationState();
            boolean z = voiceSpecificInfo.cssSupported;
            this.mNewSS.setVoiceRegState(regCodeToServiceState(networkRegistrationState));
            this.mNewSS.setCssIndicator(z ? 1 : 0);
            this.mNewSS.addNetworkRegistrationInfo(networkRegistrationInfo);
            setPhyCellInfoFromCellIdentity(this.mNewSS, networkRegistrationInfo.getCellIdentity());
            int rejectCause = networkRegistrationInfo.getRejectCause();
            boolean zIsEmergencyEnabled = networkRegistrationInfo.isEmergencyEnabled();
            this.mCSEmergencyOnly = zIsEmergencyEnabled;
            this.mEmergencyOnly = zIsEmergencyEnabled || this.mPSEmergencyOnly;
            this.mGsmVoiceRoaming = regCodeIsRoaming(networkRegistrationState);
            this.mNewRejectCode = rejectCause;
            log("handlePollStateResultMessage: CS cellular. " + networkRegistrationInfo);
            return;
        }
        if (i == 5) {
            NetworkRegistrationInfo networkRegistrationInfo2 = (NetworkRegistrationInfo) asyncResult.result;
            this.mNewSS.addNetworkRegistrationInfo(networkRegistrationInfo2);
            DataSpecificRegistrationInfo dataSpecificInfo = networkRegistrationInfo2.getDataSpecificInfo();
            int networkRegistrationState2 = networkRegistrationInfo2.getNetworkRegistrationState();
            int iRegCodeToServiceState = regCodeToServiceState(networkRegistrationState2);
            log("handlePollStateResultMessage: PS cellular. " + networkRegistrationInfo2);
            MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).convertNetworkType(this.mPhone, ServiceState.networkTypeToRilRadioTechnology(networkRegistrationInfo2.getAccessNetworkTechnology()));
            if (iRegCodeToServiceState == 1) {
                this.mLastPhysicalChannelConfigList = null;
            }
            boolean zIsEmergencyEnabled2 = networkRegistrationInfo2.isEmergencyEnabled();
            this.mPSEmergencyOnly = zIsEmergencyEnabled2;
            this.mEmergencyOnly = this.mCSEmergencyOnly || zIsEmergencyEnabled2;
            this.mNewReasonDataDenied = networkRegistrationInfo2.getRejectCause();
            this.mNewMaxDataCalls = dataSpecificInfo.maxDataCalls;
            boolean zRegCodeIsRoaming = regCodeIsRoaming(networkRegistrationState2);
            this.mGsmDataRoaming = zRegCodeIsRoaming;
            this.mNewSS.setDataRoamingFromRegistration(zRegCodeIsRoaming);
            this.mPhone.getSignalStrengthController().updateServiceStateArfcnRsrpBoost(this.mNewSS, networkRegistrationInfo2.getCellIdentity());
            return;
        }
        if (i == 6) {
            NetworkRegistrationInfo networkRegistrationInfo3 = (NetworkRegistrationInfo) asyncResult.result;
            this.mNewSS.addNetworkRegistrationInfo(networkRegistrationInfo3);
            log("handlePollStateResultMessage: PS IWLAN. " + networkRegistrationInfo3);
            return;
        }
        if (i != 7) {
            if (i == 14) {
                int[] iArr = (int[]) asyncResult.result;
                this.mNewSS.setIsManualSelection(iArr[0] == 1);
                int i2 = iArr[0];
                this.mManualSelection = i2 == 1;
                if (i2 == 1 && this.mPhone.shouldForceAutoNetworkSelect()) {
                    this.mPhone.setNetworkSelectionModeAutomatic(null);
                    log(" Forcing Automatic Network Selection, manual selection is not allowed");
                    return;
                }
                return;
            }
            loge("handlePollStateResultMessage: Unexpected RIL response received: " + i);
            return;
        }
        String[] strArr = (String[]) asyncResult.result;
        if (strArr == null || strArr.length < 3) {
            return;
        }
        if (showOperatorName()) {
            log("EVENT_POLL_STATE_OPERATOR: carrier config loaded, setting network name");
            this.mNewSS.setOperatorAlphaLongRaw(strArr[0]);
            this.mNewSS.setOperatorAlphaShortRaw(strArr[1]);
            String operatorBrandOverride = getOperatorBrandOverride();
            this.mCdnr.updateEfForBrandOverride(operatorBrandOverride);
            if (operatorBrandOverride != null) {
                log("EVENT_POLL_STATE_OPERATOR: use brandOverride=".concat(operatorBrandOverride));
                this.mNewSS.setOperatorName(operatorBrandOverride, operatorBrandOverride, strArr[2]);
                return;
            } else {
                this.mNewSS.setOperatorName(strArr[0], strArr[1], strArr[2]);
                return;
            }
        }
        this.mNewSS.setOperatorName(null, null, strArr[2]);
        log("EVENT_POLL_STATE_OPERATOR: carrier config not loaded, discarding network name");
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0049  */
    /* JADX WARN: Code duplicated, block: B:24:0x004f  */
    /* JADX WARN: Code duplicated, block: B:27:0x0056 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:28:0x0057 A[RETURN] */
    protected static long getCidFromCellIdentity(CellIdentity cellIdentity) {
        int cid;
        long nci;
        long j;
        if (cellIdentity == null) {
            return -1L;
        }
        int type = cellIdentity.getType();
        if (type == 1) {
            cid = ((CellIdentityGsm) cellIdentity).getCid();
        } else if (type == 3) {
            cid = ((CellIdentityLte) cellIdentity).getCi();
        } else if (type == 4) {
            cid = ((CellIdentityWcdma) cellIdentity).getCid();
        } else {
            if (type == 5) {
                cid = ((CellIdentityTdscdma) cellIdentity).getCid();
            } else {
                nci = type != 6 ? -1L : ((CellIdentityNr) cellIdentity).getNci();
            }
            if (cellIdentity.getType() == 6) {
                j = Long.MAX_VALUE;
            } else {
                j = 2147483647L;
            }
            if (nci == j) {
                return -1L;
            }
            return nci;
        }
        nci = cid;
        if (cellIdentity.getType() == 6) {
            j = Long.MAX_VALUE;
        } else {
            j = 2147483647L;
        }
        if (nci == j) {
            return -1L;
        }
        return nci;
    }

    protected static int getAreaCodeFromCellIdentity(CellIdentity cellIdentity) {
        if (cellIdentity == null) {
            return Integer.MAX_VALUE;
        }
        int type = cellIdentity.getType();
        if (type == 1) {
            return ((CellIdentityGsm) cellIdentity).getLac();
        }
        if (type == 3) {
            return ((CellIdentityLte) cellIdentity).getTac();
        }
        if (type == 4) {
            return ((CellIdentityWcdma) cellIdentity).getLac();
        }
        if (type == 5) {
            return ((CellIdentityTdscdma) cellIdentity).getLac();
        }
        if (type != 6) {
            return Integer.MAX_VALUE;
        }
        return ((CellIdentityNr) cellIdentity).getTac();
    }

    protected void setPhyCellInfoFromCellIdentity(ServiceState serviceState, CellIdentity cellIdentity) {
        if (cellIdentity == null) {
            log("Could not set ServiceState channel number. CellIdentity null");
            return;
        }
        serviceState.setChannelNumber(cellIdentity.getChannelNumber());
        PhysicalChannelConfig primaryPhysicalChannelConfigForCell = getPrimaryPhysicalChannelConfigForCell(this.mLastPhysicalChannelConfigList, cellIdentity);
        int[] iArr = null;
        if (cellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
            if (primaryPhysicalChannelConfigForCell != null) {
                int[] bandwidthsFromConfigs = getBandwidthsFromConfigs(this.mLastPhysicalChannelConfigList);
                int length = bandwidthsFromConfigs.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        iArr = bandwidthsFromConfigs;
                        break;
                    }
                    int i2 = bandwidthsFromConfigs[i];
                    if (!isValidLteBandwidthKhz(i2)) {
                        loge("Invalid LTE Bandwidth in RegistrationState, " + i2);
                        break;
                    }
                    i++;
                }
            }
            if (iArr == null || iArr.length == 1) {
                int bandwidth = cellIdentityLte.getBandwidth();
                if (isValidLteBandwidthKhz(bandwidth)) {
                    iArr = new int[]{bandwidth};
                } else if (bandwidth != Integer.MAX_VALUE) {
                    loge("Invalid LTE Bandwidth in RegistrationState, " + bandwidth);
                }
            }
        } else if ((cellIdentity instanceof CellIdentityNr) && primaryPhysicalChannelConfigForCell != null) {
            int[] bandwidthsFromConfigs2 = getBandwidthsFromConfigs(this.mLastPhysicalChannelConfigList);
            int length2 = bandwidthsFromConfigs2.length;
            int i3 = 0;
            while (true) {
                if (i3 >= length2) {
                    iArr = bandwidthsFromConfigs2;
                    break;
                }
                int i4 = bandwidthsFromConfigs2[i3];
                if (!isValidNrBandwidthKhz(i4)) {
                    loge("Invalid NR Bandwidth in RegistrationState, " + i4);
                    break;
                }
                i3++;
            }
        }
        if (iArr == null && primaryPhysicalChannelConfigForCell != null && primaryPhysicalChannelConfigForCell.getCellBandwidthDownlinkKhz() != 0) {
            iArr = new int[]{primaryPhysicalChannelConfigForCell.getCellBandwidthDownlinkKhz()};
        }
        if (iArr != null) {
            serviceState.setCellBandwidths(iArr);
        }
    }

    private static PhysicalChannelConfig getPrimaryPhysicalChannelConfigForCell(List list, CellIdentity cellIdentity) {
        boolean z;
        int pci;
        int i;
        if (!ArrayUtils.isEmpty(list) && (((z = cellIdentity instanceof CellIdentityLte)) || (cellIdentity instanceof CellIdentityNr))) {
            if (z) {
                pci = ((CellIdentityLte) cellIdentity).getPci();
                i = 13;
            } else {
                pci = ((CellIdentityNr) cellIdentity).getPci();
                i = 20;
            }
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PhysicalChannelConfig physicalChannelConfig = (PhysicalChannelConfig) it.next();
                boolean z2 = physicalChannelConfig.getNetworkType() == i || (i == 13 && physicalChannelConfig.getNetworkType() == 19);
                if (physicalChannelConfig.getConnectionStatus() == 1 && z2 && physicalChannelConfig.getPhysicalCellId() == pci) {
                    return physicalChannelConfig;
                }
            }
        }
        return null;
    }

    protected void updateRoamingState() {
        boolean z = true;
        boolean z2 = this.mGsmVoiceRoaming || this.mGsmDataRoaming;
        log("updateRoamingState: roaming = " + z2 + ", mGsmVoiceRoaming = " + this.mGsmVoiceRoaming + ", mGsmDataRoaming = " + this.mGsmDataRoaming);
        if (z2 && !isOperatorConsideredRoaming(this.mNewSS) && (isSameNamedOperators(this.mNewSS) || isOperatorConsideredNonRoaming(this.mNewSS))) {
            log("updateRoamingState: resource override set non roaming.isSameNamedOperators=" + isSameNamedOperators(this.mNewSS) + ",isOperatorConsideredNonRoaming=" + isOperatorConsideredNonRoaming(this.mNewSS));
            z2 = false;
        }
        if (alwaysOnHomeNetwork(this.mCarrierConfig)) {
            log("updateRoamingState: carrier config override always on home network");
        } else {
            if (isNonRoamingInGsmNetwork(this.mCarrierConfig, this.mNewSS.getOperatorNumeric())) {
                log("updateRoamingState: carrier config override set non roaming:" + this.mNewSS.getOperatorNumeric());
            } else if (isRoamingInGsmNetwork(this.mCarrierConfig, this.mNewSS.getOperatorNumeric())) {
                log("updateRoamingState: carrier config override set roaming:" + this.mNewSS.getOperatorNumeric());
            } else {
                z = z2;
            }
            this.mNewSS.setRoaming(z);
        }
        z = false;
        this.mNewSS.setRoaming(z);
    }

    protected void notifyCarrierDisplayNameDataChanged() {
        String str = String.format("notifyCarrierDisplayNameDataChanged: changed sending intent, rule=%d, CarrierDisplayNameData=%s, subId=%d", Integer.valueOf(getCarrierNameDisplayBitmask(this.mSS)), this.mCarrierDisplayNameData, Integer.valueOf(this.mPhone.getSubId()));
        this.mCdnrLogs.log(str);
        log(str);
        Intent intent = new Intent("android.telephony.action.SERVICE_PROVIDERS_UPDATED");
        intent.addFlags(268435456);
        intent.putExtra("android.telephony.extra.SHOW_SPN", this.mCarrierDisplayNameData.shouldShowSpn());
        intent.putExtra("android.telephony.extra.SPN", this.mCarrierDisplayNameData.getSpn());
        intent.putExtra("android.telephony.extra.DATA_SPN", this.mCarrierDisplayNameData.getDataSpn());
        intent.putExtra("android.telephony.extra.SHOW_PLMN", this.mCarrierDisplayNameData.shouldShowPlmn());
        intent.putExtra("android.telephony.extra.PLMN", this.mCarrierDisplayNameData.getPlmn());
        extendSpnDisplayIntent(intent);
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
        this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0049  */
    protected String getCarrierName(CarrierDisplayNameData carrierDisplayNameData) {
        boolean zShouldShowPlmn = carrierDisplayNameData.shouldShowPlmn();
        boolean zShouldShowSpn = carrierDisplayNameData.shouldShowSpn();
        String plmn = carrierDisplayNameData.getPlmn();
        String spn = carrierDisplayNameData.getSpn();
        if (zShouldShowPlmn) {
            if (zShouldShowSpn) {
                if (TextUtils.isEmpty(plmn)) {
                    plmn = spn;
                } else if (!TextUtils.isEmpty(spn) && !Objects.equals(spn, plmn)) {
                    plmn = plmn + this.mPhone.getContext().getString(R.string.miniresolver_open_work) + spn;
                }
            }
        } else if (zShouldShowSpn) {
            plmn = spn;
        } else {
            plmn = "";
        }
        return TextUtils.emptyIfNull(plmn);
    }

    public void updateCarrierDisplayName() {
        CarrierDisplayNameData carrierDisplayNameLegacy;
        if (MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).isDelayUpdateServiceState(this.mPhone)) {
            return;
        }
        if (this.mCarrierConfig.getBoolean("enable_carrier_display_name_resolver_bool")) {
            carrierDisplayNameLegacy = this.mCdnr.getCarrierDisplayNameData();
        } else {
            carrierDisplayNameLegacy = getCarrierDisplayNameLegacy();
        }
        int subId = this.mPhone.getSubId();
        if (subId == this.mSubId && carrierDisplayNameLegacy.equals(this.mCarrierDisplayNameData)) {
            return;
        }
        if (!setCarrierNameAsync(carrierDisplayNameLegacy) && SubscriptionManager.isValidSubscriptionId(subId)) {
            this.mSubscriptionManagerService.setCarrierName(subId, getCarrierName(carrierDisplayNameLegacy));
        }
        this.mCarrierDisplayNameData = carrierDisplayNameLegacy;
        notifyCarrierDisplayNameDataChanged();
    }

    /* JADX WARN: Code duplicated, block: B:135:0x032f  */
    protected CarrierDisplayNameData getCarrierDisplayNameLegacy() {
        String str;
        String str2;
        String str3;
        String str4;
        String string;
        boolean z;
        boolean z2;
        boolean z3;
        logv("getCarrierDisplayNameLegacy+");
        int combinedRegState = getCombinedRegState(this.mSS);
        boolean z4 = false;
        boolean z5 = this.mPhone.getImsPhone() != null && this.mPhone.getImsPhone().isWifiCallingEnabled();
        log("updateCarrierDisplayName: isWifiCallingEnabled=" + z5 + " combinedRegState=" + ServiceState.rilServiceStateToString(combinedRegState) + " " + this.mSS);
        String wfcDisplaySpn = null;
        if (z5 && this.mPhone.isImsRegistered() && combinedRegState == 0 && this.mSS.getDataNetworkType() == 18) {
            int i = this.mCarrierConfig.getInt("wfc_spn_format_idx_int");
            int i2 = this.mCarrierConfig.getInt("wfc_data_spn_format_idx_int");
            int i3 = this.mCarrierConfig.getInt("wfc_flight_mode_spn_format_idx_int");
            String[] stringArray = SubscriptionManager.getResourcesForSubId(this.mPhone.getContext(), this.mPhone.getSubId(), this.mCarrierConfig.getBoolean("wfc_spn_use_root_locale")).getStringArray(17236327);
            if (i < 0 || i >= stringArray.length) {
                loge("updateCarrierDisplayName: KEY_WFC_SPN_FORMAT_IDX_INT out of bounds: " + i);
                i = 0;
            }
            if (i2 < 0 || i2 >= stringArray.length) {
                loge("updateCarrierDisplayName: KEY_WFC_DATA_SPN_FORMAT_IDX_INT out of bounds: " + i2);
                i2 = 0;
            }
            if (i3 < 0 || i3 >= stringArray.length) {
                i3 = i;
            }
            str = stringArray[i];
            str2 = stringArray[i2];
            str3 = stringArray[i3];
        } else {
            str = null;
            str2 = null;
            str3 = null;
        }
        if (getImsRegistrationTech() == 2 && this.mPhone.isImsRegistered()) {
            int i4 = this.mCarrierConfig.getInt("cross_sim_spn_format_int");
            String[] stringArray2 = SubscriptionManager.getResourcesForSubId(this.mPhone.getContext(), this.mPhone.getSubId(), this.mCarrierConfig.getBoolean("wfc_spn_use_root_locale")).getStringArray(17236261);
            if (i4 < 0 || i4 >= stringArray2.length) {
                loge("updateCarrierDisplayName: KEY_CROSS_SIM_SPN_FORMAT_INT out of bounds: " + i4);
                i4 = 0;
            }
            str4 = stringArray2[i4];
        } else {
            str4 = null;
        }
        SatelliteModemStateListener satelliteModemStateListener = getSatelliteModemStateListener();
        String satelliteDisplayName = (SatelliteController.getInstance().isUsingNonTerrestrialNetworkViaCarrier(this.mPhone.getSubId()) || (satelliteModemStateListener != null && satelliteModemStateListener.isInConnectedState())) ? getSatelliteDisplayName() : null;
        log("updateCarrierDisplayName: satellitePlmn=" + satelliteDisplayName);
        int carrierNameDisplayBitmask = getCarrierNameDisplayBitmask(this.mSS);
        if (combinedRegState == 1 || combinedRegState == 2) {
            if ((!shouldForceDisplayNoService() || this.mIsSimReady) && (this.mEmergencyOnly || Phone.isEmergencyCallOnly())) {
                string = Resources.getSystem().getText(R.string.global_action_settings).toString();
                z = false;
            } else {
                string = Resources.getSystem().getText(R.string.network_partial_connectivity).toString();
                z = true;
            }
            log("updateCarrierDisplayName: radio is on but out of service, set plmn='" + string + "'");
            z2 = true;
        } else if (combinedRegState == 0) {
            this.mSS.getOperatorAlpha();
            string = MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).getPlmn(this);
            if (showOperatorName()) {
                z3 = !TextUtils.isEmpty(string) && (carrierNameDisplayBitmask & 2) == 2;
            } else {
                z3 = false;
                string = null;
            }
            log("updateCarrierDisplayName: rawPlmn = " + string);
            z2 = z3;
            z = false;
        } else {
            log("updateCarrierDisplayName: radio is off w/ showPlmn=true plmn=null");
            z = false;
            z2 = true;
            string = null;
        }
        getServiceProviderName();
        String spn = MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).getSpn(this);
        if (!showOperatorName()) {
            spn = null;
        }
        boolean z6 = (z || TextUtils.isEmpty(spn) || (carrierNameDisplayBitmask & 1) != 1) ? false : true;
        log("updateCarrierDisplayName: rawSpn = " + spn);
        if (!TextUtils.isEmpty(satelliteDisplayName)) {
            log("updateCarrierDisplayName: Update satellite network name:" + satelliteDisplayName);
            z2 = true;
        } else {
            if (!TextUtils.isEmpty(str4)) {
                if (!TextUtils.isEmpty(spn)) {
                    wfcDisplaySpn = String.format(str4, spn.trim());
                    satelliteDisplayName = string;
                    z2 = false;
                    z4 = true;
                    spn = wfcDisplaySpn;
                } else {
                    if (TextUtils.isEmpty(string)) {
                        satelliteDisplayName = string;
                    } else {
                        String strTrim = string.trim();
                        if (this.mIccRecords != null && this.mCarrierConfig.getBoolean("wfc_carrier_name_override_by_pnn_bool")) {
                            strTrim = this.mIccRecords.getPnnHomeName();
                        }
                        satelliteDisplayName = String.format(str4, strTrim);
                    }
                    z4 = z6;
                }
            } else if (!TextUtils.isEmpty(spn) && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                if (this.mSS.getState() == 3) {
                    str = str3;
                }
                String strTrim2 = spn.trim();
                wfcDisplaySpn = String.format(str, strTrim2);
                if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                    wfcDisplaySpn = MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).getWfcDisplaySpn(this.mPhone, str, strTrim2);
                }
                spn = String.format(str2, strTrim2);
                satelliteDisplayName = string;
                z2 = false;
                z4 = true;
            } else if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(str)) {
                String strTrim3 = string.trim();
                if (this.mIccRecords != null && this.mCarrierConfig.getBoolean("wfc_carrier_name_override_by_pnn_bool")) {
                    strTrim3 = this.mIccRecords.getPnnHomeName();
                }
                String.format(str, strTrim3);
                satelliteDisplayName = MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).getWfcDisplayPlmn(this.mPhone, str, strTrim3);
                z4 = z6;
            } else if (this.mSS.getState() == 3 || (z2 && TextUtils.equals(spn, string))) {
                satelliteDisplayName = string;
            } else {
                satelliteDisplayName = string;
                z4 = z6;
            }
            CarrierDisplayNameData carrierDisplayNameDataConvertCarrierDisplayName = MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).convertCarrierDisplayName(this.mPhone, new CarrierDisplayNameData.Builder().setSpn(wfcDisplaySpn).setDataSpn(spn).setShowSpn(z4).setPlmn(satelliteDisplayName).setShowPlmn(z2).build());
            logv("getCarrierDisplayNameLegacy-");
            return carrierDisplayNameDataConvertCarrierDisplayName;
        }
        wfcDisplaySpn = spn;
        CarrierDisplayNameData carrierDisplayNameDataConvertCarrierDisplayName2 = MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).convertCarrierDisplayName(this.mPhone, new CarrierDisplayNameData.Builder().setSpn(wfcDisplaySpn).setDataSpn(spn).setShowSpn(z4).setPlmn(satelliteDisplayName).setShowPlmn(z2).build());
        logv("getCarrierDisplayNameLegacy-");
        return carrierDisplayNameDataConvertCarrierDisplayName2;
    }

    protected void updateSatelliteDisplayOverride() {
        String satelliteDisplayName = getSatelliteDisplayName();
        if (TextUtils.isEmpty(satelliteDisplayName)) {
            return;
        }
        SatelliteModemStateListener satelliteModemStateListener = getSatelliteModemStateListener();
        String operatorAlphaLong = this.mNewSS.getOperatorAlphaLong();
        SatelliteController satelliteController = SatelliteController.getInstance();
        if ((satelliteModemStateListener == null || !satelliteModemStateListener.isInConnectedState()) && (TextUtils.isEmpty(operatorAlphaLong) || satelliteController == null || !satelliteController.isInCarrierRoamingNbIotNtn())) {
            return;
        }
        ServiceState serviceState = this.mNewSS;
        serviceState.setOperatorName(satelliteDisplayName, satelliteDisplayName, serviceState.getOperatorNumeric());
        log("updateSatelliteDisplayOverride: Override satellite display name to " + satelliteDisplayName);
    }

    protected SatelliteModemStateListener getSatelliteModemStateListener() {
        SatelliteModemStateListener satelliteModemStateListener = this.mSatelliteModemStateListener;
        if (satelliteModemStateListener != null) {
            return satelliteModemStateListener;
        }
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController != null) {
            SatelliteModemStateListener satelliteModemStateListener2 = new SatelliteModemStateListener();
            if (satelliteController.registerForSatelliteModemStateChanged(satelliteModemStateListener2) == 0) {
                this.mSatelliteModemStateListener = satelliteModemStateListener2;
                log("created SatelliteModemStateListener");
            } else {
                satelliteController.unregisterForModemStateChanged(satelliteModemStateListener2);
            }
        }
        return this.mSatelliteModemStateListener;
    }

    private String getSatelliteDisplayName() {
        return this.mCarrierConfig.getString("satellite_display_name_string");
    }

    public boolean shouldForceDisplayNoService() {
        String[] stringArray = this.mPhone.getContext().getResources().getStringArray(R.array.config_keep_warming_services);
        if (ArrayUtils.isEmpty(stringArray)) {
            return false;
        }
        this.mLastKnownNetworkCountry = this.mLocaleTracker.getLastKnownCountryIso();
        for (String str : stringArray) {
            if (str.equalsIgnoreCase(this.mLastKnownNetworkCountry)) {
                return true;
            }
        }
        return false;
    }

    protected void setPowerStateToDesired() {
        setPowerStateToDesired(false, false, false);
    }

    protected void setPowerStateToDesired(boolean z, boolean z2, boolean z3) {
        String str = "setPowerStateToDesired: mDeviceShuttingDown=" + this.mDeviceShuttingDown + ", mDesiredPowerState=" + this.mDesiredPowerState + ", getRadioState=" + this.mCi.getRadioState() + ", mRadioPowerOffReasons=" + this.mRadioPowerOffReasons + ", IMS reg state=" + this.mImsRegistrationOnOff + ", pending radio off=" + hasMessages(62);
        log(str);
        this.mRadioPowerLog.log(str);
        boolean z4 = this.mDesiredPowerState;
        if (z4 && this.mDeviceShuttingDown) {
            log("setPowerStateToDesired powering on of radio failed because the device is shutting down");
            return;
        }
        if (z4 && this.mRadioPowerOffReasons.isEmpty() && (z3 || this.mCi.getRadioState() == 0 || this.mCi.getRadioState() == 2)) {
            if (MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).needGUTIWork(this.mPhone.getContext(), this)) {
                MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).startGUTITask(this);
            } else {
                this.mCi.setRadioPower(true, z, z2, null);
            }
        } else if ((!this.mDesiredPowerState || !this.mRadioPowerOffReasons.isEmpty()) && this.mCi.getRadioState() == 1) {
            log("setPowerStateToDesired: powerOffRadioSafely()");
            powerOffRadioSafely();
        } else if (this.mDeviceShuttingDown && this.mCi.getRadioState() != 2) {
            this.mCi.requestShutdown(null);
        }
        cancelDelayRadioOffWaitingForImsDeregTimeout();
    }

    protected void cancelDelayRadioOffWaitingForImsDeregTimeout() {
        if (hasMessages(62)) {
            log("cancelDelayRadioOffWaitingForImsDeregTimeout: cancelling.");
            removeMessages(62);
        }
    }

    protected void onUpdateIccAvailability() {
        if (this.mUiccController == null) {
            return;
        }
        TelephonyCommonUtilsStub.get().isMiuiRom();
        UiccCardApplication uiccCardApplication = getUiccCardApplication();
        if (this.mUiccApplication != uiccCardApplication) {
            this.mIsSimReadyForDisplay = false;
            if (this.mIccRecords instanceof SIMRecords) {
                this.mCdnr.updateEfFromUsim(null);
            }
            if (this.mUiccApplication != null) {
                log("Removing stale icc objects.");
                this.mUiccApplication.unregisterForReady(this);
                IccRecords iccRecords = this.mIccRecords;
                if (iccRecords != null) {
                    iccRecords.unregisterForRecordsLoaded(this);
                }
                this.mIccRecords = null;
                this.mUiccApplication = null;
            }
            if (uiccCardApplication != null) {
                log("New card found");
                this.mUiccApplication = uiccCardApplication;
                this.mIccRecords = uiccCardApplication.getIccRecords();
                this.mUiccApplication.registerForReady(this, 17, null);
                IccRecords iccRecords2 = this.mIccRecords;
                if (iccRecords2 != null) {
                    iccRecords2.registerForRecordsLoaded(this, 16, null);
                }
            }
        }
    }

    protected void logRoamingChange() {
        this.mRoamingLog.log(this.mSS.toString());
    }

    protected void logAttachChange() {
        this.mAttachLog.log(this.mSS.toString());
    }

    private void logPhoneType() {
        this.mPhoneTypeLog.log(Integer.toString(this.mPhone.getPhoneType()));
    }

    protected void logRatChange() {
        this.mRatLog.log(this.mSS.toString());
    }

    protected void log(String str) {
        Rlog.d("SST-" + this.mPhone.getPhoneId(), str);
    }

    protected void loge(String str) {
        Rlog.e("SST-" + this.mPhone.getPhoneId(), str);
    }

    public int getCurrentDataConnectionState() {
        return this.mSS.getDataRegistrationState();
    }

    public boolean isConcurrentVoiceAndDataAllowed() {
        if (this.mSS.getCssIndicator() == 1) {
            return true;
        }
        int rilDataRadioTechnology = this.mSS.getRilDataRadioTechnology();
        if (rilDataRadioTechnology == 0 && this.mSS.getDataRegistrationState() != 0) {
            rilDataRadioTechnology = this.mSS.getRilVoiceRadioTechnology();
        }
        return (rilDataRadioTechnology == 0 || ServiceState.rilRadioTechnologyToAccessNetworkType(rilDataRadioTechnology) == 1) ? false : true;
    }

    public void onImsServiceStateChanged() {
        sendMessage(obtainMessage(53));
    }

    public void setImsRegistrationState(boolean z) {
        log("setImsRegistrationState: {registered=" + z + " mImsRegistrationOnOff=" + this.mImsRegistrationOnOff + "}");
        if (this.mImsRegistrationOnOff && !z) {
            if (getRadioPowerOffDelayTimeoutForImsRegistration() > 0) {
                sendMessage(obtainMessage(45));
            } else {
                log("setImsRegistrationState: EVENT_CHANGE_IMS_STATE not sent because power off delay for IMS deregistration is not enabled.");
            }
        }
        this.mImsRegistrationOnOff = z;
        updateCarrierDisplayName();
    }

    public void onImsCapabilityChanged() {
        sendMessage(obtainMessage(48));
    }

    public boolean isRadioOn() {
        return this.mCi.getRadioState() == 1;
    }

    public void pollState() {
        sendEmptyMessage(58);
    }

    protected void pollStateInternal(boolean z) {
        this.mPollingContext = new int[1];
        log("pollState: modemTriggered=" + z + ", radioState=" + this.mCi.getRadioState());
        int radioState = this.mCi.getRadioState();
        if (radioState == 0) {
            handlePollStateInternalForRadioOffOrUnavailable(true);
            if (this.mDeviceShuttingDown || (!z && 18 != this.mSS.getRilDataRadioTechnology())) {
                pollStateDone();
                return;
            }
        } else if (radioState == 2) {
            handlePollStateInternalForRadioOffOrUnavailable(false);
            this.mDeviceShuttingDown = false;
            pollStateDone();
            return;
        }
        issuePollCommands();
    }

    protected void issuePollCommands() {
        log("issuePollCommands");
        int[] iArr = this.mPollingContext;
        iArr[0] = iArr[0] + 1;
        this.mCi.getOperator(obtainMessage(7, iArr));
        int[] iArr2 = this.mPollingContext;
        iArr2[0] = iArr2[0] + 1;
        this.mRegStateManagers.get(1).requestNetworkRegistrationInfo(2, obtainMessage(5, this.mPollingContext));
        int[] iArr3 = this.mPollingContext;
        iArr3[0] = iArr3[0] + 1;
        this.mRegStateManagers.get(1).requestNetworkRegistrationInfo(1, obtainMessage(4, this.mPollingContext));
        if (this.mRegStateManagers.get(2) != null) {
            int[] iArr4 = this.mPollingContext;
            iArr4[0] = iArr4[0] + 1;
            this.mRegStateManagers.get(2).requestNetworkRegistrationInfo(2, obtainMessage(6, this.mPollingContext));
        }
        int[] iArr5 = this.mPollingContext;
        iArr5[0] = iArr5[0] + 1;
        this.mCi.getNetworkSelectionMode(obtainMessage(14, iArr5));
    }

    protected void handlePollStateInternalForRadioOffOrUnavailable(boolean z) {
        NetworkRegistrationInfo networkRegistrationInfo = this.mNewSS.getNetworkRegistrationInfo(2, 2);
        this.mNewSS.setOutOfService(this.mAccessNetworksManager.isInLegacyMode(), z);
        if (networkRegistrationInfo != null) {
            this.mNewSS.addNetworkRegistrationInfo(networkRegistrationInfo);
        }
        this.mPhone.getSignalStrengthController().setSignalStrengthDefaultValues();
        this.mLastNitzData = null;
        this.mNitzState.handleNetworkUnavailable();
    }

    public static List<CellIdentity> getPrioritizedCellIdentities(ServiceState serviceState) {
        List<NetworkRegistrationInfo> networkRegistrationInfoList = serviceState.getNetworkRegistrationInfoList();
        return networkRegistrationInfoList.isEmpty() ? Collections.EMPTY_LIST : (List) networkRegistrationInfoList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.ServiceStateTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ServiceStateTracker.m406$r8$lambda$ct4jHHG4j5eAElnX0vTTnuT8hY((NetworkRegistrationInfo) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.ServiceStateTracker$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ServiceStateTracker.$r8$lambda$4Xc7TrjYFBGcmzwQnfmGf7Gfufk((NetworkRegistrationInfo) obj);
            }
        }).sorted(Comparator.comparing(new Function() { // from class: com.android.internal.telephony.ServiceStateTracker$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((NetworkRegistrationInfo) obj).isRegistered());
            }
        }).thenComparing(new Function() { // from class: com.android.internal.telephony.ServiceStateTracker$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(((NetworkRegistrationInfo) obj).getDomain() & 1);
            }
        }).reversed()).map(new Function() { // from class: com.android.internal.telephony.ServiceStateTracker$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((NetworkRegistrationInfo) obj).getCellIdentity();
            }
        }).distinct().collect(Collectors.toList());
    }

    /* JADX INFO: renamed from: $r8$lambda$ct4jHHG4j5eAElnX0vTT-nuT8hY, reason: not valid java name */
    public static /* synthetic */ boolean m406$r8$lambda$ct4jHHG4j5eAElnX0vTTnuT8hY(NetworkRegistrationInfo networkRegistrationInfo) {
        return networkRegistrationInfo.getCellIdentity() != null;
    }

    public static /* synthetic */ boolean $r8$lambda$4Xc7TrjYFBGcmzwQnfmGf7Gfufk(NetworkRegistrationInfo networkRegistrationInfo) {
        return networkRegistrationInfo.getTransportType() == 1;
    }

    private boolean updateSimReadyForDisplay() {
        IccRecords iccRecords = this.mIccRecords;
        boolean z = iccRecords != null && iccRecords.getRecordsLoaded();
        if (z == this.mIsSimReadyForDisplay) {
            return false;
        }
        log("updateSimReadyForDisplay: IsSimReadyForDisplay changed to " + z);
        this.mIsSimReadyForDisplay = z;
        return true;
    }

    protected void pollStateDone() {
        boolean zNeedToUpdateSatelliteCarrierDisplay;
        boolean z;
        boolean z2;
        if (TelephonyUtils.IS_DEBUGGABLE && SystemProperties.getBoolean(PROP_FORCE_ROAMING, false)) {
            this.mNewSS.setRoaming(true);
        }
        useDataRegStateForDataOnlyDevices();
        processIwlanRegistrationInfo();
        updateNrFrequencyRangeFromPhysicalChannelConfigs(this.mLastPhysicalChannelConfigList, this.mNewSS);
        updateNrStateFromPhysicalChannelConfigs(this.mLastPhysicalChannelConfigList, this.mNewSS);
        updateNtnCapability();
        if (TelephonyUtils.IS_DEBUGGABLE && this.mPhone.getTelephonyTester() != null) {
            this.mPhone.getTelephonyTester().overrideServiceState(this.mNewSS);
        }
        setPhyCellInfoFromCellIdentity(this.mNewSS, this.mNewSS.getNetworkRegistrationInfo(2, 1).getCellIdentity());
        boolean zHasSetRoamingType = hasSetRoamingType(this.mNewSS);
        SatelliteModemStateListener satelliteModemStateListener = getSatelliteModemStateListener();
        if (satelliteModemStateListener != null) {
            zNeedToUpdateSatelliteCarrierDisplay = satelliteModemStateListener.needToUpdateSatelliteCarrierDisplay();
            if (zNeedToUpdateSatelliteCarrierDisplay) {
                log("Poll ServiceState done : hasSatelliteConnectionChanged=" + zNeedToUpdateSatelliteCarrierDisplay);
                satelliteModemStateListener.doneForUpdateSatelliteCarrierDisplay();
                updateSatelliteDisplayOverride();
            }
        } else {
            zNeedToUpdateSatelliteCarrierDisplay = false;
        }
        log("Poll ServiceState done: oldSS=" + this.mSS);
        log("Poll ServiceState done: newSS=" + this.mNewSS);
        log("Poll ServiceState done: oldMaxDataCalls=" + this.mMaxDataCalls + " mNewMaxDataCalls=" + this.mNewMaxDataCalls + " oldReasonDataDenied=" + this.mReasonDataDenied + " mNewReasonDataDenied=" + this.mNewReasonDataDenied);
        boolean z3 = this.mSS.getState() != 0 && this.mNewSS.getState() == 0;
        boolean z4 = this.mSS.getDataRegState() != 0 && this.mNewSS.getDataRegState() == 0;
        boolean z5 = this.mSS.getState() == 0 && this.mNewSS.getState() != 0;
        boolean z6 = this.mSS.getState() != 3 && this.mNewSS.getState() == 3;
        boolean z7 = this.mSS.getState() == 3 && this.mNewSS.getState() != 3;
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray(this.mAccessNetworksManager.getAvailableTransports().length);
        SparseBooleanArray sparseBooleanArray2 = new SparseBooleanArray(this.mAccessNetworksManager.getAvailableTransports().length);
        SparseBooleanArray sparseBooleanArray3 = new SparseBooleanArray(this.mAccessNetworksManager.getAvailableTransports().length);
        SparseBooleanArray sparseBooleanArray4 = new SparseBooleanArray(this.mAccessNetworksManager.getAvailableTransports().length);
        boolean z8 = (TextUtils.equals(this.mSS.getOperatorAlphaLongRaw(), this.mNewSS.getOperatorAlphaLongRaw()) && TextUtils.equals(this.mSS.getOperatorAlphaShortRaw(), this.mNewSS.getOperatorAlphaShortRaw())) ? false : true;
        int[] availableTransports = this.mAccessNetworksManager.getAvailableTransports();
        int length = availableTransports.length;
        boolean z9 = false;
        int i = 0;
        boolean z10 = false;
        while (i < length) {
            int i2 = length;
            int i3 = availableTransports[i];
            boolean z11 = z8;
            int i4 = i;
            NetworkRegistrationInfo networkRegistrationInfo = this.mSS.getNetworkRegistrationInfo(2, i3);
            NetworkRegistrationInfo networkRegistrationInfo2 = this.mNewSS.getNetworkRegistrationInfo(2, i3);
            sparseBooleanArray.put(i3, (networkRegistrationInfo == null || !networkRegistrationInfo.isInService() || z6) && networkRegistrationInfo2 != null && networkRegistrationInfo2.isInService());
            sparseBooleanArray2.put(i3, networkRegistrationInfo != null && networkRegistrationInfo.isInService() && (networkRegistrationInfo2 == null || !networkRegistrationInfo2.isInService()));
            int accessNetworkTechnology = networkRegistrationInfo != null ? networkRegistrationInfo.getAccessNetworkTechnology() : 0;
            int accessNetworkTechnology2 = networkRegistrationInfo2 != null ? networkRegistrationInfo2.getAccessNetworkTechnology() : 0;
            sparseBooleanArray3.put(i3, (accessNetworkTechnology == accessNetworkTechnology2 && (networkRegistrationInfo != null ? networkRegistrationInfo.isUsingCarrierAggregation() : false) == (networkRegistrationInfo2 != 0 ? networkRegistrationInfo2.isUsingCarrierAggregation() : false) && !z11) ? false : true);
            if (accessNetworkTechnology != accessNetworkTechnology2) {
                z10 = true;
            }
            int networkRegistrationState = networkRegistrationInfo != null ? networkRegistrationInfo.getNetworkRegistrationState() : 4;
            int networkRegistrationState2 = networkRegistrationInfo2 != 0 ? networkRegistrationInfo2.getNetworkRegistrationState() : 4;
            sparseBooleanArray4.put(i3, networkRegistrationState != networkRegistrationState2);
            if (networkRegistrationState != networkRegistrationState2) {
                z9 = true;
            }
            i = i4 + 1;
            length = i2;
            z8 = z11;
            zNeedToUpdateSatelliteCarrierDisplay = zNeedToUpdateSatelliteCarrierDisplay;
            z7 = z7;
        }
        boolean z12 = zNeedToUpdateSatelliteCarrierDisplay;
        boolean z13 = z7;
        boolean z14 = (z10 || this.mSS.getRilDataRadioTechnology() == this.mNewSS.getRilDataRadioTechnology()) ? false : true;
        boolean z15 = this.mSS.getState() != this.mNewSS.getState();
        boolean z16 = this.mSS.getNrFrequencyRange() != this.mNewSS.getNrFrequencyRange();
        boolean z17 = this.mSS.getNrState() != this.mNewSS.getNrState();
        List<CellIdentity> prioritizedCellIdentities = getPrioritizedCellIdentities(this.mNewSS);
        String plmn = null;
        CellIdentity cellIdentity = prioritizedCellIdentities.isEmpty() ? null : prioritizedCellIdentities.get(0);
        CellIdentity cellIdentity2 = this.mCellIdentity;
        boolean z18 = cellIdentity2 != null ? !cellIdentity2.isSameCell(cellIdentity) : cellIdentity != null;
        boolean z19 = z9;
        Iterator it = this.mNewSS.getNetworkRegistrationInfoListForTransportType(1).iterator();
        boolean zIsRegistered = false;
        while (it.hasNext()) {
            zIsRegistered |= ((NetworkRegistrationInfo) it.next()).isRegistered();
        }
        getRilDataRadioTechnologyForWwan(this.mSS);
        if (zIsRegistered && !z18) {
            this.mRatRatcheter.ratchet(this.mSS, this.mNewSS);
        }
        boolean z20 = this.mSS.getRilVoiceRadioTechnology() != this.mNewSS.getRilVoiceRadioTechnology();
        boolean zEquals = this.mNewSS.equals(this.mSS);
        boolean z21 = !zEquals;
        boolean z22 = !this.mSS.getVoiceRoaming() && this.mNewSS.getVoiceRoaming();
        CellIdentity cellIdentity3 = cellIdentity;
        boolean z23 = this.mSS.getVoiceRoaming() && !this.mNewSS.getVoiceRoaming();
        boolean z24 = z6;
        boolean z25 = !this.mSS.getDataRoaming() && this.mNewSS.getDataRoaming();
        boolean z26 = z17;
        boolean z27 = this.mSS.getDataRoaming() && !this.mNewSS.getDataRoaming();
        boolean z28 = z16;
        boolean z29 = z18;
        boolean z30 = this.mRejectCode != this.mNewRejectCode;
        boolean z31 = this.mSS.getCssIndicator() != this.mNewSS.getCssIndicator();
        log("pollStateDone: hasRegistered = " + z3 + " hasDeregistered = " + z5 + " hasDataAttached = " + sparseBooleanArray + " hasDataDetached = " + sparseBooleanArray2 + " hasDataRegStateChanged = " + sparseBooleanArray4 + " hasRilVoiceRadioTechnologyChanged = " + z20 + " hasRilDataRadioTechnologyChanged = " + sparseBooleanArray3 + " hasDataTransportPreferenceChanged = " + z14 + " hasChanged = " + z21 + " hasVoiceRoamingOn = " + z22 + " hasVoiceRoamingOff = " + z23 + " hasDataRoamingOn =" + z25 + " hasDataRoamingOff = " + z27 + " hasLocationChanged = " + z29 + " has4gHandoff = false hasMultiApnSupport = false hasLostMultiApnSupport = false hasCssIndicatorChanged = " + z31 + " hasNrFrequencyRangeChanged = " + z28 + " hasNrStateChanged = " + z26 + " hasAirplaneModeOnlChanged = " + z24);
        if (z15 != 0 || z19) {
            EventLog.writeEvent(EventLogTags.GSM_SERVICE_STATE_CHANGE, Integer.valueOf(this.mSS.getState()), Integer.valueOf(this.mSS.getDataRegistrationState()), Integer.valueOf(this.mNewSS.getState()), Integer.valueOf(this.mNewSS.getDataRegistrationState()));
        }
        if (z20 != 0) {
            long cidFromCellIdentity = getCidFromCellIdentity(cellIdentity3);
            EventLog.writeEvent(EventLogTags.GSM_RAT_SWITCHED_NEW, Long.valueOf(cidFromCellIdentity), Integer.valueOf(this.mSS.getRilVoiceRadioTechnology()), Integer.valueOf(this.mNewSS.getRilVoiceRadioTechnology()));
            log("RAT switched " + ServiceState.rilRadioTechnologyToString(this.mSS.getRilVoiceRadioTechnology()) + " -> " + ServiceState.rilRadioTechnologyToString(this.mNewSS.getRilVoiceRadioTechnology()) + " at cell " + cidFromCellIdentity);
        }
        this.mReasonDataDenied = this.mNewReasonDataDenied;
        this.mMaxDataCalls = this.mNewMaxDataCalls;
        this.mRejectCode = this.mNewRejectCode;
        if (!Objects.equals(this.mSS, this.mNewSS)) {
            this.mServiceStateChangedRegistrants.notifyRegistrants();
        }
        ServiceState serviceState = new ServiceState(this.mPhone.getServiceState());
        this.mSS = new ServiceState(this.mNewSS);
        this.mNewSS.setOutOfService(this.mAccessNetworksManager.isInLegacyMode(), false);
        this.mCellIdentity = cellIdentity3;
        boolean zIsNetworkRegistered = this.mSS.getNetworkRegistrationInfo(1, 1).isNetworkRegistered();
        boolean zIsNetworkRegistered2 = this.mSS.getNetworkRegistrationInfo(2, 1).isNetworkRegistered();
        if (zIsNetworkRegistered || zIsNetworkRegistered2) {
            this.mLastKnownCellIdentity = this.mCellIdentity;
            removeMessages(63);
        }
        if (z5 && !hasMessages(63)) {
            sendEmptyMessageDelayed(63, 86400000L);
        }
        int areaCodeFromCellIdentity = getAreaCodeFromCellIdentity(this.mCellIdentity);
        if (areaCodeFromCellIdentity != this.mLastKnownAreaCode && areaCodeFromCellIdentity != Integer.MAX_VALUE) {
            this.mLastKnownAreaCode = areaCodeFromCellIdentity;
            this.mAreaCodeChangedRegistrants.notifyRegistrants();
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.mPhone.getContext().getSystemService("phone");
        if (z10) {
            telephonyManager.setDataNetworkTypeForPhone(this.mPhone.getPhoneId(), this.mSS.getRilDataRadioTechnology());
            TelephonyStatsLog.write(76, ServiceState.rilRadioTechnologyToNetworkType(this.mSS.getRilDataRadioTechnology()), this.mPhone.getPhoneId());
        }
        if (z10 || sparseBooleanArray.get(1)) {
            MiuiImsControllerStub.get(this.mPhone.getPhoneId()).sendIMSRegistedStateBroadcast(this.mPhone.isVolteEnabled(), this.mPhone);
        }
        if (z3) {
            this.mPhone.getSignalStrengthController().getSignalStrengthFromCi();
            this.mNetworkAttachedRegistrants.notifyRegistrants();
            this.mNitzState.handleNetworkAvailable();
        }
        if (z5) {
            this.mNetworkDetachedRegistrants.notifyRegistrants();
            this.mNitzState.handleNetworkUnavailable();
        }
        if (z31) {
            this.mCssIndicatorChangedRegistrants.notifyRegistrants();
        }
        if (z30) {
            setNotification(2001);
        }
        boolean zUpdateSimReadyForDisplay = updateSimReadyForDisplay();
        if (!zEquals || z12 || zUpdateSimReadyForDisplay) {
            if (MiuiPhoneStub.get(this.mPhone.getPhoneId()).disableSecondarySim(this.mPhone.getPhoneId(), z3)) {
                z21 = false;
            }
            updateCarrierDisplayName();
        }
        if (z21) {
            telephonyManager.setNetworkOperatorNameForPhone(this.mPhone.getPhoneId(), MiuiUiccControllerStub.get(this.mPhone.getPhoneId()).onOperatorNumericOrNameSet(this.mPhone.getPhoneId(), "gsm.operator.alpha", this.mSS.getOperatorAlpha()));
            telephonyManager.setNetworkOperatorNumericForPhone(this.mPhone.getPhoneId(), MiuiUiccControllerStub.get(this.mPhone.getPhoneId()).onOperatorNumericOrNameSet(this.mPhone.getPhoneId(), "gsm.operator.numeric", this.mSS.getOperatorNumeric()));
            for (CellIdentity cellIdentity4 : prioritizedCellIdentities) {
                if (!TextUtils.isEmpty(cellIdentity4.getPlmn())) {
                    plmn = cellIdentity4.getPlmn();
                    break;
                }
            }
            String str = plmn;
            if (isInvalidOperatorNumeric(str)) {
                log("localeOperator " + str + " is invalid");
                this.mLocaleTracker.updateOperatorNumeric("");
            } else {
                this.mLocaleTracker.updateOperatorNumeric(str);
            }
            telephonyManager.setNetworkRoamingForPhone(this.mPhone.getPhoneId(), this.mSS.getVoiceRoaming());
            if (!zHasSetRoamingType) {
                setRoamingType(this.mSS);
            }
            log("Broadcasting ServiceState : " + this.mSS);
            if (!serviceState.equals(this.mPhone.getServiceState())) {
                GsmCdmaPhone gsmCdmaPhone = this.mPhone;
                gsmCdmaPhone.notifyServiceStateChanged(gsmCdmaPhone.getServiceState());
            }
            z = z15;
            onServiceStateChangeNotified(this.mSS, z, z19);
            updateServiceStateToDb(this.mPhone.getServiceState());
            this.mPhone.getVoiceCallSessionStats().onServiceStateChanged(this.mSS);
            ImsPhone imsPhone = (ImsPhone) this.mPhone.getImsPhone();
            if (imsPhone != null) {
                imsPhone.getImsStats().onServiceStateChanged(this.mSS);
            }
            this.mServiceStateStats.onServiceStateChanged(this.mSS);
        } else {
            z = z15;
        }
        boolean z32 = z3 || z5;
        if (z20 != 0) {
            if (this.mNewSS.getState() == 0) {
                this.mPhone.getSignalStrengthController().getSignalStrengthFromCi();
            }
            this.mPhone.getSignalStrengthController().notifySignalStrength();
            z2 = true;
        } else {
            z2 = false;
        }
        int[] availableTransports2 = this.mAccessNetworksManager.getAvailableTransports();
        int length2 = availableTransports2.length;
        int i5 = 0;
        while (i5 < length2) {
            boolean z33 = z32;
            int i6 = availableTransports2[i5];
            if (sparseBooleanArray3.get(i6)) {
                this.mPhone.getSignalStrengthController().notifySignalStrength();
                z2 = true;
            }
            if (sparseBooleanArray4.get(i6) || sparseBooleanArray3.get(i6) || z14) {
                setDataNetworkTypeForPhone(this.mSS.getRilDataRadioTechnology());
                notifyDataRegStateRilRadioTechnologyChanged(i6);
            }
            if (sparseBooleanArray.get(i6)) {
                if (this.mAttachedRegistrants.get(i6) != null) {
                    this.mAttachedRegistrants.get(i6).notifyRegistrants();
                }
                z33 = true;
            }
            if (sparseBooleanArray2.get(i6)) {
                if (this.mDetachedRegistrants.get(i6) != null) {
                    this.mDetachedRegistrants.get(i6).notifyRegistrants();
                }
                z32 = true;
            } else {
                z32 = z33;
            }
            i5++;
            z2 = z2;
        }
        boolean z34 = z32;
        if (z13) {
            this.mPhone.getSignalStrengthController().getSignalStrengthFromCi();
        }
        if (z34) {
            logAttachChange();
        }
        if (z2) {
            logRatChange();
        }
        if (z || z20) {
            notifyVoiceRegStateRilRadioTechnologyChanged();
        }
        if (z22 != 0 || z23 || z25 || z27) {
            logRoamingChange();
        }
        if (z22) {
            this.mVoiceRoamingOnRegistrants.notifyRegistrants();
        }
        if (z23) {
            this.mVoiceRoamingOffRegistrants.notifyRegistrants();
        }
        if (z25) {
            this.mDataRoamingOnRegistrants.notifyRegistrants();
        }
        if (z27) {
            this.mDataRoamingOffRegistrants.notifyRegistrants();
        }
        if (z29) {
            this.mPhone.notifyLocationChanged(getCellIdentity());
        }
        if (z26) {
            this.mNrStateChangedRegistrants.notifyRegistrants();
        }
        if (z28) {
            this.mNrFrequencyChangedRegistrants.notifyRegistrants();
        }
        if (!isGprsConsistent(this.mSS.getDataRegistrationState(), this.mSS.getState())) {
            if (!this.mStartedGprsRegCheck && !this.mReportedGprsNoReg) {
                this.mStartedGprsRegCheck = true;
                sendMessageDelayed(obtainMessage(22), Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), "gprs_register_check_period_ms", DEFAULT_GPRS_CHECK_PERIOD_MILLIS));
            }
        } else {
            this.mReportedGprsNoReg = false;
        }
        MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).handleMccMncChange(this.mPhone, this.mSS, this.mNewSS);
        if (z3 || ((z4 && !this.mPhoneTypeUpdated) || (!z5 && this.mSS.getDataRegState() == 0 && sparseBooleanArray3.get(1)))) {
            log("SS optimization: hasRegistered=" + z3 + " hasDeregistered=" + z5 + " hasDataRegistered=" + z4 + " mPhoneTypeUpdated=" + this.mPhoneTypeUpdated);
            MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).resetServiceStateDisplay(this.mPhone);
        }
        this.mPhoneTypeUpdated = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceStateToDb(ServiceState serviceState) {
        this.mPhone.getContext().getContentResolver().insert(Telephony.ServiceStateTable.getUriForSubscriptionId(this.mPhone.getSubId()), getContentValuesForServiceState(serviceState));
    }

    public String getServiceProviderName() {
        String operatorBrandOverride = getOperatorBrandOverride();
        if (!TextUtils.isEmpty(operatorBrandOverride)) {
            return operatorBrandOverride;
        }
        IccRecords iccRecords = this.mIccRecords;
        String serviceProviderName = iccRecords != null ? iccRecords.getServiceProviderName() : "";
        return (this.mCarrierConfig.getBoolean("carrier_name_override_bool") || TextUtils.isEmpty(serviceProviderName)) ? this.mCarrierConfig.getString("carrier_name_string") : serviceProviderName;
    }

    public int getCarrierNameDisplayBitmask(ServiceState serviceState) {
        if (!TextUtils.isEmpty(getOperatorBrandOverride())) {
            return 1;
        }
        if (TextUtils.isEmpty(getServiceProviderName())) {
            return 2;
        }
        boolean z = this.mCarrierConfig.getBoolean("spn_display_rule_use_roaming_from_service_state_bool");
        IccRecords iccRecords = this.mIccRecords;
        boolean roaming = false;
        int carrierNameDisplayCondition = iccRecords == null ? 0 : iccRecords.getCarrierNameDisplayCondition();
        if (z) {
            roaming = serviceState.getRoaming();
        } else {
            IccRecords iccRecords2 = this.mIccRecords;
            boolean z2 = !ArrayUtils.contains(iccRecords2 != null ? iccRecords2.getHomePlmns() : null, serviceState.getOperatorNumeric());
            if (!TextUtils.isEmpty(serviceState.getOperatorNumeric())) {
                roaming = z2;
            }
        }
        if (roaming) {
            return (carrierNameDisplayCondition & 2) == 2 ? 3 : 2;
        }
        return (carrierNameDisplayCondition & 1) == 1 ? 3 : 1;
    }

    private String getOperatorBrandOverride() {
        UiccProfile uiccProfile;
        UiccPort uiccPort = this.mPhone.getUiccPort();
        if (uiccPort == null || (uiccProfile = uiccPort.getUiccProfile()) == null) {
            return null;
        }
        return uiccProfile.getOperatorBrandOverride();
    }

    protected boolean isInvalidOperatorNumeric(String str) {
        return str == null || str.length() < 5 || str.startsWith(INVALID_MCC);
    }

    private boolean isSameOperatorNameFromSimAndSS(ServiceState serviceState) {
        String simOperatorNameForPhone = ((TelephonyManager) this.mPhone.getContext().getSystemService("phone")).getSimOperatorNameForPhone(getPhoneId());
        return (!TextUtils.isEmpty(simOperatorNameForPhone) && simOperatorNameForPhone.equalsIgnoreCase(serviceState.getOperatorAlphaLong())) || (!TextUtils.isEmpty(simOperatorNameForPhone) && simOperatorNameForPhone.equalsIgnoreCase(serviceState.getOperatorAlphaShort()));
    }

    private boolean isSameNamedOperators(ServiceState serviceState) {
        return currentMccEqualsSimMcc(serviceState) && isSameOperatorNameFromSimAndSS(serviceState);
    }

    private boolean currentMccEqualsSimMcc(ServiceState serviceState) {
        try {
            return ((TelephonyManager) this.mPhone.getContext().getSystemService("phone")).getSimOperatorNumericForPhone(getPhoneId()).substring(0, 3).equals(serviceState.getOperatorNumeric().substring(0, 3));
        } catch (Exception unused) {
            return true;
        }
    }

    protected boolean isOperatorConsideredNonRoaming(ServiceState serviceState) {
        String dataOperatorNumeric = MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).getDataOperatorNumeric(this.mPhone, serviceState.getOperatorNumeric());
        if (MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).isOperatorConsideredNonRoaming(this.mPhone, dataOperatorNumeric)) {
            return true;
        }
        String[] stringArray = this.mCarrierConfig.getStringArray("non_roaming_operator_string_array");
        if (!ArrayUtils.isEmpty(stringArray) && dataOperatorNumeric != null) {
            for (String str : stringArray) {
                if (!TextUtils.isEmpty(str) && dataOperatorNumeric.startsWith(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isOperatorConsideredRoaming(ServiceState serviceState) {
        String operatorNumeric = serviceState.getOperatorNumeric();
        if (MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).isOperatorConsideredNonRoaming(this.mPhone, operatorNumeric)) {
            return false;
        }
        String[] stringArray = this.mCarrierConfig.getStringArray("roaming_operator_string_array");
        if (!ArrayUtils.isEmpty(stringArray) && operatorNumeric != null) {
            for (String str : stringArray) {
                if (!TextUtils.isEmpty(str) && operatorNumeric.startsWith(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void onRestrictedStateChanged(AsyncResult asyncResult) {
        Object obj;
        RestrictedState restrictedState = new RestrictedState();
        log("onRestrictedStateChanged: E rs " + this.mRestrictedState);
        if (asyncResult.exception == null && (obj = asyncResult.result) != null) {
            int iIntValue = ((Integer) obj).intValue();
            restrictedState.setCsEmergencyRestricted(((iIntValue & 1) == 0 && (iIntValue & 4) == 0) ? false : true);
            UiccCardApplication uiccCardApplication = this.mUiccApplication;
            if (uiccCardApplication != null && uiccCardApplication.getState() == IccCardApplicationStatus.AppState.APPSTATE_READY) {
                restrictedState.setCsNormalRestricted(((iIntValue & 2) == 0 && (iIntValue & 4) == 0) ? false : true);
                restrictedState.setPsRestricted((iIntValue & 16) != 0);
            }
            log("onRestrictedStateChanged: new rs " + restrictedState);
            if (!this.mRestrictedState.isPsRestricted() && restrictedState.isPsRestricted()) {
                this.mPsRestrictEnabledRegistrants.notifyRegistrants();
                setNotification(1001);
            } else if (this.mRestrictedState.isPsRestricted() && !restrictedState.isPsRestricted()) {
                this.mPsRestrictDisabledRegistrants.notifyRegistrants();
                setNotification(1002);
            }
            if (this.mRestrictedState.isCsRestricted()) {
                if (!restrictedState.isAnyCsRestricted()) {
                    setNotification(1004);
                } else if (!restrictedState.isCsNormalRestricted()) {
                    setNotification(1006);
                } else if (!restrictedState.isCsEmergencyRestricted()) {
                    setNotification(1005);
                }
            } else if (this.mRestrictedState.isCsEmergencyRestricted() && !this.mRestrictedState.isCsNormalRestricted()) {
                if (!restrictedState.isAnyCsRestricted()) {
                    setNotification(1004);
                } else if (restrictedState.isCsRestricted()) {
                    setNotification(1003);
                } else if (restrictedState.isCsNormalRestricted()) {
                    setNotification(1005);
                }
            } else if (!this.mRestrictedState.isCsEmergencyRestricted() && this.mRestrictedState.isCsNormalRestricted()) {
                if (!restrictedState.isAnyCsRestricted()) {
                    setNotification(1004);
                } else if (restrictedState.isCsRestricted()) {
                    setNotification(1003);
                } else if (restrictedState.isCsEmergencyRestricted()) {
                    setNotification(1006);
                }
            } else if (restrictedState.isCsRestricted()) {
                setNotification(1003);
            } else if (restrictedState.isCsEmergencyRestricted()) {
                setNotification(1006);
            } else if (restrictedState.isCsNormalRestricted()) {
                setNotification(1005);
            }
            this.mRestrictedState = restrictedState;
        }
        log("onRestrictedStateChanged: X rs " + this.mRestrictedState);
    }

    public CellIdentity getCellIdentity() {
        CellIdentity cellIdentity = this.mCellIdentity;
        if (cellIdentity != null) {
            return cellIdentity;
        }
        CellIdentity cellIdentityFromCellInfo = getCellIdentityFromCellInfo(getAllCellInfo());
        return cellIdentityFromCellInfo != null ? cellIdentityFromCellInfo : new CellIdentityGsm();
    }

    public void requestCellIdentity(WorkSource workSource, Message message) {
        CellIdentity cellIdentity = this.mCellIdentity;
        if (cellIdentity != null) {
            AsyncResult.forMessage(message, cellIdentity, (Throwable) null);
            message.sendToTarget();
        } else {
            requestAllCellInfo(workSource, obtainMessage(56, message));
        }
    }

    private static CellIdentity getCellIdentityFromCellInfo(List list) {
        CellIdentity cellIdentity = null;
        if (list != null && list.size() > 0) {
            Iterator it = list.iterator();
            CellIdentity cellIdentity2 = null;
            while (it.hasNext()) {
                CellIdentity cellIdentity3 = ((CellInfo) it.next()).getCellIdentity();
                if ((cellIdentity3 instanceof CellIdentityLte) && cellIdentity2 == null) {
                    if (getCidFromCellIdentity(cellIdentity3) != -1) {
                        cellIdentity2 = cellIdentity3;
                    }
                } else if (getCidFromCellIdentity(cellIdentity3) != -1) {
                    cellIdentity = cellIdentity3;
                    break;
                }
            }
            if (cellIdentity == null && cellIdentity2 != null) {
                return cellIdentity2;
            }
        }
        return cellIdentity;
    }

    protected void setTimeFromNITZString(String str, long j, long j2) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        Rlog.d("SST", "NITZ: " + str + "," + j + ", ageMs=" + j2 + " start=" + jElapsedRealtime + " delay=" + (jElapsedRealtime - j));
        NitzData nitzData = NitzData.parse(str);
        this.mLastNitzData = nitzData;
        if (nitzData != null) {
            try {
                MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).notifyNitzTimeChange(nitzData);
                this.mNitzState.handleNitzReceived(new NitzSignal(j, nitzData, j2));
            } finally {
                long jElapsedRealtime2 = SystemClock.elapsedRealtime();
                Rlog.d("SST", "NITZ: end=" + jElapsedRealtime2 + " dur=" + (jElapsedRealtime2 - jElapsedRealtime));
            }
        }
    }

    private void cancelAllNotifications() {
        log("cancelAllNotifications: mPrevSubId=" + this.mPrevSubId);
        NotificationManager notificationManager = (NotificationManager) this.mPhone.getContext().getSystemService("notification");
        if (SubscriptionManager.isValidSubscriptionId(this.mPrevSubId)) {
            notificationManager.cancel(Integer.toString(this.mPrevSubId), PS_NOTIFICATION);
            notificationManager.cancel(Integer.toString(this.mPrevSubId), CS_NOTIFICATION);
            notificationManager.cancel(Integer.toString(this.mPrevSubId), 111);
            notificationManager.cancel(CarrierServiceStateTracker.EMERGENCY_NOTIFICATION_TAG, this.mPrevSubId);
            notificationManager.cancel(CarrierServiceStateTracker.PREF_NETWORK_NOTIFICATION_TAG, this.mPrevSubId);
        }
    }

    public void setNotification(int i) {
        CharSequence text;
        CharSequence string;
        int i2;
        NotificationManager notificationManager;
        int i3 = i;
        log("setNotification: create notification " + i3);
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            loge("cannot setNotification on invalid subid mSubId=" + this.mSubId);
            return;
        }
        Context context = this.mPhone.getContext();
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(this.mPhone.getSubId());
        if (subscriptionInfoInternal == null || !subscriptionInfoInternal.isVisible()) {
            log("cannot setNotification on invisible subid mSubId=" + this.mSubId);
            return;
        }
        if (!context.getResources().getBoolean(17892076)) {
            log("Ignore all the notifications");
            return;
        }
        if (this.mCarrierConfig.getBoolean("disable_voice_barring_notification_bool", false) && (i3 == 1003 || i3 == 1005 || i3 == 1006)) {
            log("Voice/emergency call barred notification disabled");
            return;
        }
        boolean z = this.mCarrierConfig.getBoolean("carrier_auto_cancel_cs_notification", false);
        boolean z2 = ((TelephonyManager) this.mPhone.getContext().getSystemService("phone")).getPhoneCount() > 1;
        int slotIndex = SubscriptionManager.getSlotIndex(this.mSubId) + 1;
        int i4 = CS_NOTIFICATION;
        if (i3 == 1005) {
            text = context.getText(R.string.accessibility_autoclick_scroll_panel_title);
            string = z2 ? context.getString(R.string.accessibility_button_instructional_text, Integer.valueOf(slotIndex)) : context.getText(R.string.accessibility_binding_label);
        } else {
            if (i3 != 1006) {
                text = "";
                if (i3 != 2001) {
                    switch (i3) {
                        case 1001:
                            if (SubscriptionManager.getDefaultDataSubscriptionId() == this.mPhone.getSubId()) {
                                text = context.getText(R.string.accessibility_autoclick_scroll);
                                if (z2) {
                                    string = context.getString(R.string.accessibility_button_instructional_text, Integer.valueOf(slotIndex));
                                } else {
                                    string = context.getText(R.string.accessibility_binding_label);
                                }
                                i4 = PS_NOTIFICATION;
                                break;
                            }
                            break;
                        case 1002:
                            string = "";
                            i4 = PS_NOTIFICATION;
                            break;
                        case 1003:
                            text = context.getText(R.string.accessibility_autoclick_position);
                            string = z2 ? context.getString(R.string.accessibility_button_instructional_text, Integer.valueOf(slotIndex)) : context.getText(R.string.accessibility_binding_label);
                            break;
                        default:
                            string = "";
                            break;
                    }
                    return;
                }
                int iSelectResourceForRejectCode = selectResourceForRejectCode(this.mRejectCode, z2);
                i4 = 111;
                if (iSelectResourceForRejectCode != 0) {
                    text = context.getString(iSelectResourceForRejectCode, Integer.valueOf(slotIndex));
                    i2 = R.drawable.switch_inner_holo_dark;
                    string = null;
                } else if (!z) {
                    loge("setNotification: mRejectCode=" + this.mRejectCode + " is not handled.");
                    return;
                } else {
                    string = "";
                    i3 = 2002;
                }
                log("setNotification, create notification, notifyType: " + i3 + ", title: " + ((Object) text) + ", details: " + ((Object) string) + ", subId: " + this.mSubId);
                this.mNotification = new Notification.Builder(context).setWhen(System.currentTimeMillis()).setAutoCancel(true).setSmallIcon(i2).setTicker(text).setColor(context.getResources().getColor(R.color.system_notification_accent_color)).setContentTitle(text).setStyle(new Notification.BigTextStyle().bigText(string)).setContentText(string).setChannelId(NotificationChannelController.CHANNEL_ID_ALERT).build();
                notificationManager = (NotificationManager) context.getSystemService("notification");
                if (i3 != 1002 || i3 == 1004 || i3 == 2002) {
                    notificationManager.cancel(Integer.toString(this.mSubId), i4);
                } else {
                    if ((this.mSS.isEmergencyOnly() && i3 == 1006) || i3 == 2001 || this.mSS.getState() == 0) {
                        notificationManager.notify(Integer.toString(this.mSubId), i4, this.mNotification);
                        return;
                    }
                    return;
                }
            }
            text = context.getText(R.string.accessibility_autoclick_scroll_exit);
            string = z2 ? context.getString(R.string.accessibility_button_instructional_text, Integer.valueOf(slotIndex)) : context.getText(R.string.accessibility_binding_label);
        }
        i2 = R.drawable.stat_sys_warning;
        log("setNotification, create notification, notifyType: " + i3 + ", title: " + ((Object) text) + ", details: " + ((Object) string) + ", subId: " + this.mSubId);
        this.mNotification = new Notification.Builder(context).setWhen(System.currentTimeMillis()).setAutoCancel(true).setSmallIcon(i2).setTicker(text).setColor(context.getResources().getColor(R.color.system_notification_accent_color)).setContentTitle(text).setStyle(new Notification.BigTextStyle().bigText(string)).setContentText(string).setChannelId(NotificationChannelController.CHANNEL_ID_ALERT).build();
        notificationManager = (NotificationManager) context.getSystemService("notification");
        if (i3 != 1002) {
        }
        notificationManager.cancel(Integer.toString(this.mSubId), i4);
    }

    private UiccCardApplication getUiccCardApplication() {
        return this.mUiccController.getUiccCardApplication(this.mPhone.getPhoneId(), 1);
    }

    public void registerForDataConnectionAttached(int i, Handler handler, int i2, Object obj) {
        Registrant registrant = new Registrant(handler, i2, obj);
        if (this.mAttachedRegistrants.get(i) == null) {
            this.mAttachedRegistrants.put(i, new RegistrantList());
        }
        this.mAttachedRegistrants.get(i).add(registrant);
        ServiceState serviceState = this.mSS;
        if (serviceState != null) {
            NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, i);
            if (networkRegistrationInfo == null || networkRegistrationInfo.isInService()) {
                registrant.notifyRegistrant();
            }
        }
    }

    public void unregisterForDataConnectionAttached(int i, Handler handler) {
        if (this.mAttachedRegistrants.get(i) != null) {
            this.mAttachedRegistrants.get(i).remove(handler);
        }
    }

    public void registerForDataConnectionDetached(int i, Handler handler, int i2, Object obj) {
        NetworkRegistrationInfo networkRegistrationInfo;
        Registrant registrant = new Registrant(handler, i2, obj);
        if (this.mDetachedRegistrants.get(i) == null) {
            this.mDetachedRegistrants.put(i, new RegistrantList());
        }
        this.mDetachedRegistrants.get(i).add(registrant);
        ServiceState serviceState = this.mSS;
        if (serviceState == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, i)) == null || networkRegistrationInfo.isInService()) {
            return;
        }
        registrant.notifyRegistrant();
    }

    public void unregisterForDataConnectionDetached(int i, Handler handler) {
        if (this.mDetachedRegistrants.get(i) != null) {
            this.mDetachedRegistrants.get(i).remove(handler);
        }
    }

    public void registerForVoiceRegStateOrRatChanged(Handler handler, int i, Object obj) {
        this.mVoiceRegStateOrRatChangedRegistrants.add(new Registrant(handler, i, obj));
        notifyVoiceRegStateRilRadioTechnologyChanged();
    }

    public void unregisterForVoiceRegStateOrRatChanged(Handler handler) {
        this.mVoiceRegStateOrRatChangedRegistrants.remove(handler);
    }

    public void registerForDataRegStateOrRatChanged(int i, Handler handler, int i2, Object obj) {
        Registrant registrant = new Registrant(handler, i2, obj);
        if (this.mDataRegStateOrRatChangedRegistrants.get(i) == null) {
            this.mDataRegStateOrRatChangedRegistrants.put(i, new RegistrantList());
        }
        ((RegistrantList) this.mDataRegStateOrRatChangedRegistrants.get(i)).add(registrant);
        Pair registrationInfo = getRegistrationInfo(i);
        if (registrationInfo != null) {
            registrant.notifyResult(registrationInfo);
        }
    }

    public void unregisterForDataRegStateOrRatChanged(int i, Handler handler) {
        if (this.mDataRegStateOrRatChangedRegistrants.get(i) != null) {
            ((RegistrantList) this.mDataRegStateOrRatChangedRegistrants.get(i)).remove(handler);
        }
    }

    public void registerForNetworkAttached(Handler handler, int i, Object obj) {
        Registrant registrant = new Registrant(handler, i, obj);
        this.mNetworkAttachedRegistrants.add(registrant);
        if (this.mSS.getState() == 0) {
            registrant.notifyRegistrant();
        }
    }

    public void unregisterForNetworkAttached(Handler handler) {
        this.mNetworkAttachedRegistrants.remove(handler);
    }

    public void registerForNetworkDetached(Handler handler, int i, Object obj) {
        Registrant registrant = new Registrant(handler, i, obj);
        this.mNetworkDetachedRegistrants.add(registrant);
        if (this.mSS.getState() != 0) {
            registrant.notifyRegistrant();
        }
    }

    public void registerForPsRestrictedEnabled(Handler handler, int i, Object obj) {
        Registrant registrant = new Registrant(handler, i, obj);
        this.mPsRestrictEnabledRegistrants.add(registrant);
        if (this.mRestrictedState.isPsRestricted()) {
            registrant.notifyRegistrant();
        }
    }

    public void registerForPsRestrictedDisabled(Handler handler, int i, Object obj) {
        Registrant registrant = new Registrant(handler, i, obj);
        this.mPsRestrictDisabledRegistrants.add(registrant);
        if (this.mRestrictedState.isPsRestricted()) {
            registrant.notifyRegistrant();
        }
    }

    public void registerForImsCapabilityChanged(Handler handler, int i, Object obj) {
        this.mImsCapabilityChangedRegistrants.add(new Registrant(handler, i, obj));
    }

    public void unregisterForImsCapabilityChanged(Handler handler) {
        this.mImsCapabilityChangedRegistrants.remove(handler);
    }

    public void registerForServiceStateChanged(Handler handler, int i, Object obj) {
        this.mServiceStateChangedRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForServiceStateChanged(Handler handler) {
        this.mServiceStateChangedRegistrants.remove(handler);
    }

    public void powerOffRadioSafely() {
        synchronized (this) {
            try {
                SatelliteController.getInstance().onSetCellularRadioPowerStateRequested(false);
                if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
                    EmergencyStateTracker.getInstance().onCellularRadioPowerOffRequested();
                }
                int i = this.mDeviceShuttingDown ? 33 : 3;
                if (i != this.mPendingRadioPowerOffReason) {
                    if (this.mPendingRadioPowerOffReason == 33) {
                        log("powerOffRadioSafely: Ignore because device is shutting down.");
                        return;
                    }
                    log("powerOffRadioSafely: due to " + DataNetwork.tearDownReasonToString(i));
                    if (this.mPhone.isInCall()) {
                        this.mPhone.mCT.mRingingCall.hangupIfAlive();
                        this.mPhone.mCT.mBackgroundCall.hangupIfAlive();
                        this.mPhone.mCT.mForegroundCall.hangupIfAlive();
                    }
                    mtkHangupAllImsCall();
                    for (Phone phone : PhoneFactory.getPhones()) {
                        if (!phone.getDataNetworkController().areAllDataDisconnected(!this.mDeviceShuttingDown) && !mtkAreAllDataDisconnected(phone)) {
                            log("powerOffRadioSafely: Data is active on phone " + phone.getSubId() + ". Wait for all data disconnect.");
                            this.mPendingRadioPowerOffReason = i;
                            phone.getDataNetworkController().registerDataNetworkControllerCallback(this.mDataDisconnectedCallback);
                        }
                    }
                    this.mPhone.getDataNetworkController().tearDownAllDataNetworks(i);
                    if (this.mPendingRadioPowerOffReason == 0) {
                        log("powerOffRadioSafely: No data is connected, turn off radio now.");
                        hangupAndPowerOff();
                    } else if (!hasMessages(38)) {
                        sendEmptyMessageDelayed(38, POWER_OFF_ALL_DATA_NETWORKS_DISCONNECTED_TIMEOUT);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean isPendingRadioPowerOffAfterDataOff() {
        return this.mPendingRadioPowerOffReason != 0;
    }

    protected void onCarrierConfigurationChanged(int i) {
        if (i != this.mPhone.getPhoneId()) {
            return;
        }
        this.mCarrierConfig = getCarrierConfig();
        log("CarrierConfigChange " + this.mCarrierConfig);
        updateOperatorNamePattern(this.mCarrierConfig);
        this.mCdnr.updateEfFromCarrierConfig(this.mCarrierConfig);
        this.mPhone.notifyCallForwardingIndicator();
        pollStateInternal(false);
        updateCarrierDisplayName();
    }

    protected void hangupAndPowerOff() {
        if (this.mCi.getRadioState() == 0) {
            return;
        }
        if (this.mPhone.isInCall()) {
            this.mPhone.mCT.mRingingCall.hangupIfAlive();
            this.mPhone.mCT.mBackgroundCall.hangupIfAlive();
            this.mPhone.mCT.mForegroundCall.hangupIfAlive();
        }
        MiuiCallControllerStub.get(this.mPhone.getPhoneId()).hangupImsCallIfNeeded(this.mPhone, isDeviceShuttingDown());
        this.mCi.setRadioPower(false, obtainMessage(54));
    }

    protected void cancelPollState() {
        this.mPollingContext = new int[1];
    }

    public String getSystemProperty(String str, String str2) {
        return TelephonyManager.getTelephonyProperty(this.mPhone.getPhoneId(), str, str2);
    }

    public List<CellInfo> getAllCellInfo() {
        return this.mLastCellInfoList;
    }

    public void setCellInfoMinInterval(int i) {
        this.mCellInfoMinIntervalMs = i;
    }

    public void requestAllCellInfo(WorkSource workSource, Message message) {
        if (this.mCi.getRilVersion() < 8) {
            AsyncResult.forMessage(message);
            message.sendToTarget();
            log("SST.requestAllCellInfo(): not implemented");
            return;
        }
        synchronized (this.mPendingCellInfoRequests) {
            try {
                if (this.mIsPendingCellInfoRequest) {
                    if (message != null) {
                        this.mPendingCellInfoRequests.add(message);
                    }
                    return;
                }
                if (MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).processCacheCellInfoList(this.mPhone, message, this.mLastCellInfoReqTime, this.mCellInfoMinIntervalMs, null)) {
                    return;
                }
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                if (jElapsedRealtime - this.mLastCellInfoReqTime < this.mCellInfoMinIntervalMs) {
                    if (message != null) {
                        log("SST.requestAllCellInfo(): return last, back to back calls");
                        AsyncResult.forMessage(message, this.mLastCellInfoList, (Throwable) null);
                        message.sendToTarget();
                    }
                    return;
                }
                if (message != null) {
                    this.mPendingCellInfoRequests.add(message);
                }
                this.mLastCellInfoReqTime = jElapsedRealtime;
                this.mIsPendingCellInfoRequest = true;
                this.mCi.getCellInfoList(obtainMessage(43), workSource);
                sendMessageDelayed(obtainMessage(43), 2000L);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void onTelecomVoiceServiceStateOverrideChanged() {
        sendMessage(obtainMessage(65));
    }

    private void dumpCellInfoList(PrintWriter printWriter) {
        printWriter.print(" mLastCellInfoList={");
        List<CellInfo> list = this.mLastCellInfoList;
        if (list != null) {
            boolean z = true;
            for (CellInfo cellInfo : list) {
                if (!z) {
                    printWriter.print(",");
                }
                printWriter.print(cellInfo.toString());
                z = false;
            }
        }
        printWriter.println("}");
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("ServiceStateTracker:");
        printWriter.println(" mSubId=" + this.mSubId);
        printWriter.println(" mSS=" + this.mSS);
        printWriter.println(" mNewSS=" + this.mNewSS);
        printWriter.println(" mVoiceCapable=" + this.mVoiceCapable);
        printWriter.println(" mRestrictedState=" + this.mRestrictedState);
        printWriter.println(" mPollingContext=" + Arrays.toString(this.mPollingContext));
        printWriter.println(" mDesiredPowerState=" + this.mDesiredPowerState);
        printWriter.println(" mRestrictedState=" + this.mRestrictedState);
        printWriter.println(" mPendingRadioPowerOffAfterDataOff=" + this.mPendingRadioPowerOffAfterDataOff);
        printWriter.println(" mPendingRadioPowerOffReason=" + DataNetwork.tearDownReasonToString(this.mPendingRadioPowerOffReason));
        printWriter.println(" mCellIdentity=" + Rlog.pii(false, this.mCellIdentity));
        printWriter.println(" mLastCellInfoReqTime=" + this.mLastCellInfoReqTime);
        dumpCellInfoList(printWriter);
        printWriter.flush();
        printWriter.println(" mAllowedNetworkTypes=" + this.mAllowedNetworkTypes);
        printWriter.println(" mMaxDataCalls=" + this.mMaxDataCalls);
        printWriter.println(" mNewMaxDataCalls=" + this.mNewMaxDataCalls);
        printWriter.println(" mReasonDataDenied=" + this.mReasonDataDenied);
        printWriter.println(" mNewReasonDataDenied=" + this.mNewReasonDataDenied);
        printWriter.println(" mGsmVoiceRoaming=" + this.mGsmVoiceRoaming);
        printWriter.println(" mGsmDataRoaming=" + this.mGsmDataRoaming);
        printWriter.println(" mEmergencyOnly=" + this.mEmergencyOnly);
        printWriter.println(" mCSEmergencyOnly=" + this.mCSEmergencyOnly);
        printWriter.println(" mPSEmergencyOnly=" + this.mPSEmergencyOnly);
        printWriter.flush();
        this.mNitzState.dumpState(printWriter);
        printWriter.println(" mLastNitzData=" + this.mLastNitzData);
        printWriter.flush();
        printWriter.println(" mStartedGprsRegCheck=" + this.mStartedGprsRegCheck);
        printWriter.println(" mReportedGprsNoReg=" + this.mReportedGprsNoReg);
        printWriter.println(" mNotification=" + this.mNotification);
        printWriter.println(" mCarrierDisplayNameData=" + this.mCarrierDisplayNameData);
        printWriter.flush();
        printWriter.println(" mRegistrationState=" + this.mRegistrationState);
        printWriter.println(" mCurrentCarrier=" + this.mCurrentCarrier);
        printWriter.flush();
        printWriter.println(" mImsRegistered=" + this.mImsRegistered);
        printWriter.println(" mImsRegistrationOnOff=" + this.mImsRegistrationOnOff);
        printWriter.println(" pending radio off event=" + hasMessages(62));
        printWriter.println(" mRadioPowerOffReasons=" + this.mRadioPowerOffReasons);
        printWriter.println(" mDeviceShuttingDown=" + this.mDeviceShuttingDown);
        printWriter.println(" mCellInfoMinIntervalMs=" + this.mCellInfoMinIntervalMs);
        this.mLocaleTracker.dump(fileDescriptor, printWriter, strArr);
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "    ");
        MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).dump(this.mPhone, fileDescriptor, printWriter, strArr);
        this.mCdnr.dump(indentingPrintWriter);
        indentingPrintWriter.println(" Carrier Display Name update records:");
        indentingPrintWriter.increaseIndent();
        this.mCdnrLogs.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" Roaming Log:");
        indentingPrintWriter.increaseIndent();
        this.mRoamingLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" Attach Log:");
        indentingPrintWriter.increaseIndent();
        this.mAttachLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" Phone Change Log:");
        indentingPrintWriter.increaseIndent();
        this.mPhoneTypeLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" Rat Change Log:");
        indentingPrintWriter.increaseIndent();
        this.mRatLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" Radio power Log:");
        indentingPrintWriter.increaseIndent();
        this.mRadioPowerLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        this.mNitzState.dumpLogs(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.flush();
    }

    public boolean isImsRegistered() {
        return this.mImsRegistered;
    }

    protected boolean inSameCountry(String str) {
        if (!TextUtils.isEmpty(str) && str.length() >= 5) {
            String homeOperatorNumeric = getHomeOperatorNumeric();
            if (!TextUtils.isEmpty(homeOperatorNumeric) && homeOperatorNumeric.length() >= 5) {
                String strSubstring = str.substring(0, 3);
                String strSubstring2 = homeOperatorNumeric.substring(0, 3);
                String strCountryCodeForMcc = MccTable.countryCodeForMcc(strSubstring);
                String strCountryCodeForMcc2 = MccTable.countryCodeForMcc(strSubstring2);
                LocaleTracker localeTracker = this.mLocaleTracker;
                if (localeTracker != null && !TextUtils.isEmpty(localeTracker.getCountryOverride())) {
                    log("inSameCountry:  countryOverride var set.  This should only be set for testing purposes to override the device location.");
                    return this.mLocaleTracker.getCountryOverride().equals(strCountryCodeForMcc2);
                }
                if (!strCountryCodeForMcc.isEmpty() && !strCountryCodeForMcc2.isEmpty()) {
                    boolean zEquals = strCountryCodeForMcc2.equals(strCountryCodeForMcc);
                    if (!zEquals) {
                        if ("us".equals(strCountryCodeForMcc2) && "vi".equals(strCountryCodeForMcc)) {
                            return true;
                        }
                        if ("vi".equals(strCountryCodeForMcc2) && "us".equals(strCountryCodeForMcc)) {
                            return true;
                        }
                    }
                    return zEquals;
                }
            }
        }
        return false;
    }

    protected void setRoamingType(ServiceState serviceState) {
        boolean z = serviceState.getState() == 0;
        if (z) {
            if (serviceState.getVoiceRoaming()) {
                if (inSameCountry(serviceState.getOperatorNumeric())) {
                    serviceState.setVoiceRoamingType(2);
                } else {
                    serviceState.setVoiceRoamingType(3);
                }
            } else {
                serviceState.setVoiceRoamingType(0);
            }
        }
        boolean z2 = serviceState.getDataRegistrationState() == 0;
        int rilDataRadioTechnologyForWwan = getRilDataRadioTechnologyForWwan(serviceState);
        if (z2) {
            if (!serviceState.getDataRoaming()) {
                serviceState.setDataRoamingType(0);
                return;
            }
            if (!ServiceState.isGsm(rilDataRadioTechnologyForWwan)) {
                serviceState.setDataRoamingType(1);
            } else if (z) {
                serviceState.setDataRoamingType(serviceState.getVoiceRoamingType());
            } else {
                serviceState.setDataRoamingType(1);
            }
        }
    }

    protected String getHomeOperatorNumeric() {
        String str;
        String str2;
        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
            int phoneId = getPhoneId();
            TelephonyManager.from(this.mPhone.getContext()).getSimOperatorNumericForPhone(phoneId);
            if (phoneId == 0) {
                str2 = "vendor.gsm.ril.uicc.mccmnc";
                str = "vendor.cdma.ril.uicc.mccmnc";
            } else {
                str = "vendor.cdma.ril.uicc.mccmnc." + phoneId;
                str2 = "vendor.gsm.ril.uicc.mccmnc." + phoneId;
            }
            String str3 = SystemProperties.get(str2, "");
            String str4 = SystemProperties.get(str, "");
            if (str4 != "") {
                str3 = str4;
            }
            log("[getHomeOperatorNumeric]PhoneType:2 mccmnc:" + str3);
            return str3;
        }
        return ((TelephonyManager) this.mPhone.getContext().getSystemService("phone")).getSimOperatorNumericForPhone(this.mPhone.getPhoneId());
    }

    protected int getPhoneId() {
        return this.mPhone.getPhoneId();
    }

    private void processIwlanRegistrationInfo() {
        NetworkRegistrationInfo networkRegistrationInfo;
        boolean z;
        if (this.mCi.getRadioState() == 0) {
            log("set service state as POWER_OFF");
            if (18 == this.mNewSS.getRilDataRadioTechnology()) {
                log("pollStateDone: mNewSS = " + this.mNewSS);
                log("pollStateDone: reset iwlan RAT value");
                z = true;
            } else {
                z = false;
            }
            String operatorAlphaLong = this.mNewSS.getOperatorAlphaLong();
            this.mNewSS.setOutOfService(this.mAccessNetworksManager.isInLegacyMode(), true);
            if (z) {
                this.mNewSS.setDataRegState(0);
                this.mNewSS.addNetworkRegistrationInfo(new NetworkRegistrationInfo.Builder().setTransportType(2).setDomain(2).setAccessNetworkTechnology(18).setRegistrationState(1).setAvailableServices(List.of(2)).build());
                if (this.mAccessNetworksManager.isInLegacyMode()) {
                    this.mNewSS.addNetworkRegistrationInfo(new NetworkRegistrationInfo.Builder().setTransportType(1).setDomain(2).setAccessNetworkTechnology(18).setRegistrationState(1).build());
                }
                this.mNewSS.setOperatorAlphaLong(operatorAlphaLong);
                this.mNewSS.setIwlanPreferred(true);
                log("pollStateDone: mNewSS = " + this.mNewSS);
            }
        }
        if (this.mAccessNetworksManager.isInLegacyMode() && (networkRegistrationInfo = this.mNewSS.getNetworkRegistrationInfo(2, 1)) != null && networkRegistrationInfo.getAccessNetworkTechnology() == 18) {
            this.mNewSS.addNetworkRegistrationInfo(new NetworkRegistrationInfo.Builder().setTransportType(2).setDomain(2).setRegistrationState(networkRegistrationInfo.getNetworkRegistrationState()).setAccessNetworkTechnology(18).setRejectCause(networkRegistrationInfo.getRejectCause()).setEmergencyOnly(networkRegistrationInfo.isEmergencyEnabled()).setAvailableServices(networkRegistrationInfo.getAvailableServices()).build());
        }
    }

    private void updateNtnCapability() {
        for (NetworkRegistrationInfo networkRegistrationInfo : this.mNewSS.getNetworkRegistrationInfoListForTransportType(1)) {
            NtnCapabilityResolver.resolveNtnCapability(networkRegistrationInfo, this.mSubId);
            if (networkRegistrationInfo.isNonTerrestrialNetwork()) {
                this.mNewSS.addNetworkRegistrationInfo(networkRegistrationInfo);
            }
        }
    }

    protected final boolean alwaysOnHomeNetwork(BaseBundle baseBundle) {
        return baseBundle.getBoolean("force_home_network_bool");
    }

    private boolean isInNetwork(BaseBundle baseBundle, String str, String str2) {
        String[] stringArray = baseBundle.getStringArray(str2);
        return stringArray != null && Arrays.asList(stringArray).contains(str);
    }

    protected final boolean isRoamingInGsmNetwork(BaseBundle baseBundle, String str) {
        return isInNetwork(baseBundle, str, "gsm_roaming_networks_string_array");
    }

    protected final boolean isNonRoamingInGsmNetwork(BaseBundle baseBundle, String str) {
        return isInNetwork(baseBundle, str, "gsm_nonroaming_networks_string_array");
    }

    public boolean isDeviceShuttingDown() {
        return this.mDeviceShuttingDown;
    }

    public int getCombinedRegState(ServiceState serviceState) {
        int state = serviceState.getState();
        int dataRegistrationState = serviceState.getDataRegistrationState();
        if ((state == 1 || state == 3) && dataRegistrationState == 0) {
            if (serviceState.getDataNetworkType() == 18) {
                if (this.mPhone.getImsPhone() != null && this.mPhone.getImsPhone().isWifiCallingEnabled()) {
                    log("getCombinedRegState: return STATE_IN_SERVICE for IWLAN as Data is in service and WFC is enabled");
                    return dataRegistrationState;
                }
            } else {
                log("getCombinedRegState: return STATE_IN_SERVICE as Data is in service");
                return dataRegistrationState;
            }
        }
        return state;
    }

    public boolean showOperatorName() {
        IccRecords iccRecords;
        if (!this.mPhone.getContext().getResources().getBoolean(17892048)) {
            return true;
        }
        if (!this.mIsSimReadyForDisplay && (iccRecords = this.mIccRecords) != null && iccRecords.getRecordsLoaded()) {
            this.mIsSimReadyForDisplay = true;
        }
        return this.mIsSimReadyForDisplay && isCarrierConfigApplied() && !isSimAbsent();
    }

    private boolean isCarrierConfigApplied() {
        PersistableBundle persistableBundle = this.mCarrierConfig;
        if (persistableBundle == null) {
            return false;
        }
        return persistableBundle.getBoolean("carrier_config_applied_bool", false);
    }

    private PersistableBundle getCarrierConfig() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        return (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(this.mPhone.getSubId())) == null) ? CarrierConfigManager.getDefaultConfig() : configForSubId;
    }

    public LocaleTracker getLocaleTracker() {
        return this.mLocaleTracker;
    }

    private void updateOperatorNamePattern(PersistableBundle persistableBundle) {
        String string = persistableBundle.getString("operator_name_filter_pattern_string");
        if (!TextUtils.isEmpty(string)) {
            this.mOperatorNameStringPattern = Pattern.compile(string);
            log("mOperatorNameStringPattern: " + this.mOperatorNameStringPattern);
            return;
        }
        this.mOperatorNameStringPattern = null;
    }

    protected void updateOperatorNameForServiceState(ServiceState serviceState) {
        if (serviceState == null) {
            return;
        }
        serviceState.setOperatorName(filterOperatorNameByPattern(serviceState.getOperatorAlphaLong()), filterOperatorNameByPattern(serviceState.getOperatorAlphaShort()), serviceState.getOperatorNumeric());
        List<NetworkRegistrationInfo> networkRegistrationInfoList = serviceState.getNetworkRegistrationInfoList();
        for (int i = 0; i < networkRegistrationInfoList.size(); i++) {
            if (networkRegistrationInfoList.get(i) != null) {
                updateOperatorNameForCellIdentity(networkRegistrationInfoList.get(i).getCellIdentity());
                serviceState.addNetworkRegistrationInfo(networkRegistrationInfoList.get(i));
                updateLacAndCarrierDisplayName(networkRegistrationInfoList.get(i));
            }
        }
    }

    protected void updateOperatorNameForCellIdentity(CellIdentity cellIdentity) {
        if (cellIdentity == null) {
            return;
        }
        cellIdentity.setOperatorAlphaLong(filterOperatorNameByPattern((String) cellIdentity.getOperatorAlphaLong()));
        cellIdentity.setOperatorAlphaShort(filterOperatorNameByPattern((String) cellIdentity.getOperatorAlphaShort()));
    }

    public void updateOperatorNameForCellInfo(List<CellInfo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (CellInfo cellInfo : list) {
            if (cellInfo.isRegistered()) {
                updateOperatorNameForCellIdentity(cellInfo.getCellIdentity());
            }
        }
    }

    public String filterOperatorNameByPattern(String str) {
        if (this.mOperatorNameStringPattern != null && !TextUtils.isEmpty(str)) {
            Matcher matcher = this.mOperatorNameStringPattern.matcher(str);
            if (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    return matcher.group(1);
                }
                log("filterOperatorNameByPattern: pattern no group");
            }
        }
        return str;
    }

    protected static int getRilDataRadioTechnologyForWwan(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
        return ServiceState.networkTypeToRilRadioTechnology(networkRegistrationInfo != null ? networkRegistrationInfo.getAccessNetworkTechnology() : 0);
    }

    public void registerForNrStateChanged(Handler handler, int i, Object obj) {
        this.mNrStateChangedRegistrants.add(new Registrant(handler, i, obj));
    }

    public void registerForNrFrequencyChanged(Handler handler, int i, Object obj) {
        this.mNrFrequencyChangedRegistrants.add(new Registrant(handler, i, obj));
    }

    public void registerForCssIndicatorChanged(Handler handler, int i, Object obj) {
        this.mCssIndicatorChangedRegistrants.add(new Registrant(handler, i, obj));
    }

    public void unregisterForCssIndicatorChanged(Handler handler) {
        this.mCssIndicatorChangedRegistrants.remove(handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDataNetworkTypeForPhone(int i) {
        if (this.mPhone.getUnitTestMode()) {
            return;
        }
        ((TelephonyManager) this.mPhone.getContext().getSystemService("phone")).setDataNetworkTypeForPhone(this.mPhone.getPhoneId(), i);
    }

    public ServiceStateStats getServiceStateStats() {
        return this.mServiceStateStats;
    }

    public void setServiceStateStats(ServiceStateStats serviceStateStats) {
        this.mServiceStateStats = serviceStateStats;
    }

    protected ContentValues getContentValuesForServiceState(ServiceState serviceState) {
        ContentValues contentValues = new ContentValues();
        Parcel parcelObtain = Parcel.obtain();
        serviceState.writeToParcel(parcelObtain, 0);
        contentValues.put("service_state", parcelObtain.marshall());
        parcelObtain.recycle();
        return contentValues;
    }

    public void registerForAreaCodeChanged(Handler handler, int i, Object obj) {
        this.mAreaCodeChangedRegistrants.addUnique(handler, i, obj);
    }

    public CellIdentity getLastKnownCellIdentity() {
        return this.mLastKnownCellIdentity;
    }

    private int getImsRegistrationTech() {
        ImsPhone imsPhone = (ImsPhone) this.mPhone.getImsPhone();
        if (imsPhone != null) {
            return imsPhone.getImsRegistrationTech();
        }
        return -1;
    }

    public boolean hasPendingRadioPowerOff() {
        return this.mPendingRadioPowerOffAfterDataOff;
    }

    protected void logv(String str) {
        log(str);
    }

    public NitzStateMachine getNitzStateMachine() {
        return this.mNitzState;
    }

    protected void notifySignalStrength() {
        this.mPhone.getSignalStrengthController().notifySignalStrength();
    }

    protected void getSignalStrengthFromCi() {
        this.mPhone.getSignalStrengthController().getSignalStrengthFromCi();
    }
}
