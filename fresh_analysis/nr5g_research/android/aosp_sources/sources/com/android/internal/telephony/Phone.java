package com.android.internal.telephony;

import android.R;
import android.app.BroadcastOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.radio.network.PrioritizedNetworkScanRequest;
import android.hardware.radio.network.SatelliteNetworkInfo;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.WorkSource;
import android.preference.PreferenceManager;
import android.telephony.CarrierConfigManager;
import android.telephony.CarrierRestrictionRules;
import android.telephony.CellBroadcastIdRange;
import android.telephony.CellIdentity;
import android.telephony.CellInfo;
import android.telephony.ClientRequestStats;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.LinkCapacityEstimate;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhysicalChannelConfig;
import android.telephony.PreciseDataConnectionState;
import android.telephony.RadioAccessFamily;
import android.telephony.RadioAccessSpecifier;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.satellite.NtnSignalStrength;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import com.android.ims.ImsCall;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.analytics.TelephonyAnalytics;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataSettingsManager;
import com.android.internal.telephony.data.LinkBandwidthEstimator;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.imsphone.ImsCallInfo;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.internal.telephony.metrics.SmsStats;
import com.android.internal.telephony.metrics.VoiceCallSessionStats;
import com.android.internal.telephony.satellite.metrics.ControllerMetricsStats;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IsimRecords;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UsimServiceTable;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.util.XmlUtils;
import com.android.telephony.Rlog;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public abstract class Phone extends Handler implements PhoneInternalInterface {
    public static final int ALREADY_IN_AUTO_SELECTION = 1;
    public static final String CF_ID = "cf_id_key";
    public static final String CF_STATUS = "cf_status_key";
    public static final String CF_VIDEO = "cf_key_video";
    public static final String CLIR_KEY = "clir_sub_key";
    public static final String CS_FALLBACK = "cs_fallback";
    public static final String CS_FALLBACK_SS = "cs_fallback_ss";
    public static final String DATA_DISABLED_ON_BOOT_KEY = "disabled_on_boot_key";
    public static final String DATA_ROAMING_IS_USER_SETTING_KEY = "data_roaming_is_user_setting_key";
    protected static final int EVENT_BARRING_INFO_CHANGED = 58;
    protected static final int EVENT_CALL_RING = 14;
    protected static final int EVENT_CARRIER_CONFIG_CHANGED = 43;
    protected static final int EVENT_CELL_IDENTIFIER_DISCLOSURE = 72;
    public static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER = 25;
    protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;
    protected static final int EVENT_GET_AVAILABLE_NETWORKS_DONE = 51;
    protected static final int EVENT_GET_BASEBAND_VERSION_DONE = 6;
    protected static final int EVENT_GET_CALL_FORWARD_DONE = 13;
    protected static final int EVENT_GET_DEVICE_IDENTITY_DONE = 21;
    protected static final int EVENT_GET_DEVICE_IMEI_DONE = 67;
    protected static final int EVENT_GET_N1_MODE_ENABLED_DONE = 69;
    protected static final int EVENT_GET_RADIO_CAPABILITY = 35;
    protected static final int EVENT_GET_UICC_APPS_ENABLEMENT_DONE = 55;
    protected static final int EVENT_GET_USAGE_SETTING_DONE = 63;
    protected static final int EVENT_ICC_CHANGED = 30;
    protected static final int EVENT_ICC_RECORD_EVENTS = 29;
    protected static final int EVENT_IMEI_MAPPING_CHANGED = 71;
    protected static final int EVENT_IMS_DEREGISTRATION_TRIGGERED = 65;
    protected static final int EVENT_LAST = 77;
    protected static final int EVENT_LINK_CAPACITY_CHANGED = 59;
    protected static final int EVENT_MODEM_RESET = 45;
    protected static final int EVENT_NETWORK_SECURITY_EVENTS = 76;
    protected static final int EVENT_NV_READY = 23;
    protected static final int EVENT_RADIO_AVAILABLE = 1;
    public static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 8;
    protected static final int EVENT_RADIO_ON = 5;
    protected static final int EVENT_RADIO_STATE_CHANGED = 47;
    protected static final int EVENT_REAPPLY_UICC_APPS_ENABLEMENT_DONE = 56;
    protected static final int EVENT_REGISTERED_TO_NETWORK = 19;
    protected static final int EVENT_REGISTRATION_FAILED = 57;
    protected static final int EVENT_REQUEST_VOICE_RADIO_TECH_DONE = 40;
    protected static final int EVENT_RESET_CARRIER_KEY_IMSI_ENCRYPTION = 60;
    protected static final int EVENT_RUIM_RECORDS_LOADED = 22;
    protected static final int EVENT_SECURITY_ALGORITHM_UPDATE = 74;
    protected static final int EVENT_SET_ALLOWED_NETWORK_TYPES_FOR_2G_DISABLED_DONE = 77;
    protected static final int EVENT_SET_CALL_FORWARD_DONE = 12;
    protected static final int EVENT_SET_CARRIER_DATA_ENABLED = 48;
    protected static final int EVENT_SET_CLIR_COMPLETE = 18;
    protected static final int EVENT_SET_IDENTIFIER_DISCLOSURE_ENABLED_DONE = 73;
    protected static final int EVENT_SET_N1_MODE_ENABLED_DONE = 70;
    protected static final int EVENT_SET_NETWORK_AUTOMATIC = 28;
    protected static final int EVENT_SET_NETWORK_AUTOMATIC_COMPLETE = 17;
    protected static final int EVENT_SET_NETWORK_MANUAL_COMPLETE = 16;
    protected static final int EVENT_SET_NULL_CIPHER_AND_INTEGRITY_DONE = 66;
    protected static final int EVENT_SET_ROAMING_PREFERENCE_DONE = 44;
    protected static final int EVENT_SET_SECURITY_ALGORITHMS_UPDATED_ENABLED_DONE = 75;
    protected static final int EVENT_SET_USAGE_SETTING_DONE = 64;
    protected static final int EVENT_SET_VM_NUMBER_DONE = 20;
    protected static final int EVENT_SET_VONR_ENABLED_DONE = 61;
    protected static final int EVENT_SIM_RECORDS_LOADED = 3;
    protected static final int EVENT_SRVCC_STATE_CHANGED = 31;
    protected static final int EVENT_SS = 36;
    protected static final int EVENT_SSN = 2;
    protected static final int EVENT_SUBSCRIPTIONS_CHANGED = 62;
    protected static final int EVENT_TRIGGER_NOTIFY_ANBR = 68;
    protected static final int EVENT_UICC_APPS_ENABLEMENT_SETTING_CHANGED = 54;
    protected static final int EVENT_UICC_APPS_ENABLEMENT_STATUS_CHANGED = 53;
    protected static final int EVENT_USSD = 7;
    protected static final int EVENT_VOICE_RADIO_TECH_CHANGED = 39;
    protected static final int EVENT_VRS_OR_RAT_CHANGED = 46;

    @Deprecated
    public static final String EXTRA_KEY_ALERT_MESSAGE = "android.telephony.ims.extra.WFC_REGISTRATION_FAILURE_MESSAGE";
    public static final String EXTRA_KEY_ALERT_SHOW = "alertShow";

    @Deprecated
    public static final String EXTRA_KEY_ALERT_TITLE = "android.telephony.ims.extra.WFC_REGISTRATION_FAILURE_TITLE";
    public static final String EXTRA_KEY_NOTIFICATION_MESSAGE = "notificationMessage";
    public static final int IMEI_TYPE_PRIMARY = 1;
    public static final int IMEI_TYPE_SECONDARY = 2;
    public static final int IMEI_TYPE_UNKNOWN = -1;
    private static String LOG_TAG = "Phone";
    public static final String NETWORK_SELECTION_KEY = "network_selection_key";
    public static final String NETWORK_SELECTION_NAME_KEY = "network_selection_name_key";
    public static final String NETWORK_SELECTION_SHORT_KEY = "network_selection_short_key";
    public static final String PREF_IDENTIFIER_DISCLOSURE_NOTIFICATIONS_ENABLED = "pref_identifier_disclosure_notifications_enabled";
    public static final String PREF_NULL_CIPHER_AND_INTEGRITY_ENABLED = "pref_null_cipher_and_integrity_enabled";
    public static final String PREF_NULL_CIPHER_NOTIFICATIONS_ENABLED = "pref_null_cipher_notifications_enabled";
    protected static final Object lockForRadioTechnologyChange = new Object();
    protected final int USSD_MAX_QUEUE;
    private final TelephonyAdminReceiver m2gAdminUpdater;
    protected AccessNetworksManager mAccessNetworksManager;
    private final String mActionAttached;
    private final String mActionDetached;
    private Set mAllowedImsServicesAny;
    private Set mAllowedImsServicesHomeOnly;
    private Map mAllowedNetworkTypesForReasons;
    private final AppSmsManager mAppSmsManager;
    private int mCallRingContinueToken;
    private int mCallRingDelay;
    protected CarrierActionAgent mCarrierActionAgent;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    protected CarrierResolver mCarrierResolver;
    protected CarrierSignalAgent mCarrierSignalAgent;
    private final RegistrantList mCellInfoRegistrants;
    public CommandsInterface mCi;
    protected final Context mContext;
    protected DataNetworkController mDataNetworkController;
    protected DeviceStateMonitor mDeviceStateMonitor;
    protected final RegistrantList mDisconnectRegistrants;
    protected DisplayInfoController mDisplayInfoController;
    private boolean mDoesRilSendMultipleCallRing;
    protected EcbmHandler mEcbmHandler;
    protected boolean mEcmCanceledForEmergency;
    protected final RegistrantList mEmergencyCallToggledRegistrants;
    private boolean mEmergencySmsModeInitialized;
    private volatile int mEmergencySmsModeTimerMs;
    protected final FeatureFlags mFeatureFlags;
    private final RegistrantList mHandoverRegistrants;
    protected final AtomicReference<IccRecords> mIccRecords;
    protected Phone mImsPhone;
    private final RegistrantList mIncomingRingRegistrants;
    protected boolean mIsAllowedNetworkTypesLoadedFromDb;
    protected boolean mIsCarrierNrSupported;
    private boolean mIsNetworkScanStarted;
    protected boolean mIsPhoneInEcmState;
    private boolean mIsUsageSettingSupported;
    protected boolean mIsVideoCapable;
    private boolean mIsVoiceCapable;
    protected LinkBandwidthEstimator mLinkBandwidthEstimator;
    protected final LocalLog mLocalLog;
    private final String mLogTag;
    private Looper mLooper;
    protected final RegistrantList mMmiCompleteRegistrants;
    protected final RegistrantList mMmiRegistrants;
    private String mName;
    private final RegistrantList mNewRingingConnectionRegistrants;
    protected PhoneNotifier mNotifier;
    protected int mPhoneId;
    private final RegistrantList mPhysicalChannelConfigRegistrants;
    protected Registrant mPostDialHandler;
    protected final RegistrantList mPreciseCallStateRegistrants;
    private final RegistrantList mPreferredNetworkTypeRegistrants;
    private int mPreferredUsageSetting;
    private final File mProvisioningUrlFile;
    protected final AtomicReference<RadioCapability> mRadioCapability;
    protected final RegistrantList mRadioOffOrNotAvailableRegistrants;
    private final RegistrantList mRedialRegistrants;
    private final RegistrantList mServiceStateRegistrants;
    protected SignalStrengthController mSignalStrengthController;
    private SimActivationTracker mSimActivationTracker;
    protected final RegistrantList mSimRecordsLoadedRegistrants;
    protected SimulatedRadioControl mSimulatedRadioControl;
    protected boolean mSmartTempDdsSwitchSupported;
    protected SmsStats mSmsStats;
    public SmsStorageMonitor mSmsStorageMonitor;
    public SmsUsageMonitor mSmsUsageMonitor;
    protected SubscriptionManagerService mSubscriptionManagerService;
    protected final RegistrantList mSuppServiceFailedRegistrants;
    protected TelephonyAnalytics mTelephonyAnalytics;
    protected TelephonyComponentFactory mTelephonyComponentFactory;
    protected boolean mTelephonyTempDdsSwitch;
    private TelephonyTester mTelephonyTester;
    private volatile long mTimeLastEmergencySmsSentMs;
    protected AtomicReference<UiccCardApplication> mUiccApplication;
    protected UiccController mUiccController;
    private boolean mUnitTestMode;
    protected final RegistrantList mUnknownConnectionRegistrants;
    private int mUsageSettingFromModem;
    private final RegistrantList mVideoCapabilityChangedRegistrants;
    protected int mVmCount;
    protected VoiceCallSessionStats mVoiceCallSessionStats;

    public static class NetworkSelectMessage {
        public Message message;
        public String operatorAlphaLong;
        public String operatorAlphaShort;
        public String operatorNumeric;
    }

    public void callEndCleanupHandOverCallIfAny() {
    }

    public boolean canDisablePhysicalSubscription() {
        return false;
    }

    public void cancelUSSD(Message message) {
    }

    public void deleteCarrierInfoForImsiEncryption(int i) {
    }

    public void deleteCarrierInfoForImsiEncryption(int i, String str) {
    }

    protected Connection dialInternal(String str, PhoneInternalInterface.DialArgs dialArgs) throws CallStateException {
        return null;
    }

    public void disableDnsCheck(boolean z) {
    }

    public void dispose() {
    }

    public void enableEnhancedVoicePrivacy(boolean z, Message message) {
    }

    public void enableUiccApplications(boolean z, Message message) {
    }

    public void exitEmergencyCallbackMode() {
    }

    public void exitScbm() {
    }

    public AccessNetworksManager getAccessNetworksManager() {
        return null;
    }

    public int getActiveSingleSimSlot() {
        return 0;
    }

    public void getCallForwardingOption(int i, int i2, Message message) {
    }

    public CallTracker getCallTracker() {
        return null;
    }

    public int getCarrierId() {
        return -1;
    }

    public int getCarrierIdListVersion() {
        return -1;
    }

    public ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int i, boolean z) {
        return null;
    }

    public String getCarrierName() {
        return null;
    }

    public CarrierPrivilegesTracker getCarrierPrivilegesTracker() {
        return null;
    }

    public long getCiwlanTimerToDealayDeactivateDataCall() {
        return -1L;
    }

    public Uri[] getCurrentSubscriberUris() {
        return null;
    }

    public Phone getDefaultPhone() {
        return this;
    }

    public DeviceStateMonitor getDeviceStateMonitor() {
        return null;
    }

    public DisplayInfoController getDisplayInfoController() {
        return null;
    }

    public int getEmergencyNumberDbVersion() {
        return -1;
    }

    public EmergencyNumberTracker getEmergencyNumberTracker() {
        return null;
    }

    public void getEnhancedVoicePrivacy(Message message) {
    }

    public Handler getHandler() {
        return this;
    }

    public ArrayList<Connection> getHandoverConnection() {
        return null;
    }

    public IccCard getIccCard() {
        return null;
    }

    public IccSmsInterfaceManager getIccSmsInterfaceManager() {
        return null;
    }

    public InboundSmsHandler getInboundSmsHandler(boolean z) {
        return null;
    }

    public int getMNOCarrierId() {
        return -1;
    }

    public String getManualNetworkSelectionPlmn() {
        return null;
    }

    public String getMsisdn() {
        return null;
    }

    public String getNai() {
        return null;
    }

    public abstract int getPhoneType();

    public List<PhysicalChannelConfig> getPhysicalChannelConfigList() {
        return null;
    }

    public String getPnnHomeNetworkName() {
        return null;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public PreciseDataConnectionState getPreciseDataConnectionState(String str) {
        return null;
    }

    public SIMRecords getSIMRecords() {
        return null;
    }

    public ServiceStateTracker getServiceStateTracker() {
        return null;
    }

    public boolean getSmartTempDdsSwitchSupported() {
        return false;
    }

    public SmsDispatchersController getSmsDispatchersController() {
        return null;
    }

    public int getSpecificCarrierId() {
        return -1;
    }

    public String getSpecificCarrierName() {
        return null;
    }

    public abstract PhoneConstants.State getState();

    public String getSubscriberUriNumber() {
        return null;
    }

    public boolean getTelephonyTempDdsSwitch() {
        return true;
    }

    public int getTerminalBasedCallWaitingState(boolean z) {
        return -1;
    }

    protected void handleExitEmergencyCallbackMode() {
    }

    public void handleIdentifierDisclosureNotificationPreferenceChange() {
    }

    public void handleNullCipherEnabledChange() {
    }

    public void handleNullCipherNotificationPreferenceChanged() {
    }

    public boolean isCspPlmnEnabled() {
        return false;
    }

    public boolean isDnsCheckDisabled() {
        return false;
    }

    public boolean isExitScbmFeatureSupported() {
        return false;
    }

    public boolean isIdentifierDisclosureTransparencySupported() {
        return false;
    }

    public boolean isInCdmaEcm() {
        return false;
    }

    public boolean isInEmergencyCall() {
        return false;
    }

    public boolean isInImsEcm() {
        return false;
    }

    public boolean isInScbm() {
        return false;
    }

    public boolean isInScbm(int i) {
        return false;
    }

    public boolean isNullCipherAndIntegritySupported() {
        return false;
    }

    public boolean isNullCipherNotificationSupported() {
        return false;
    }

    public boolean isScbmTimerCanceledForEmergency() {
        return false;
    }

    protected boolean needNotifySrvccState() {
        return false;
    }

    public boolean needsOtaServiceProvisioning() {
        return false;
    }

    public void notifyCallForwardingIndicator() {
    }

    public void notifyEmergencyDomainSelected(int i) {
    }

    protected void notifyImsPhoneHandoverStateChanged(Connection connection) {
    }

    public void notifySrvccState(int i) {
    }

    public void onCarrierConfigLoadedForEssentialRecords() {
    }

    protected void onSetNetworkSelectionModeCompleted() {
    }

    protected abstract void onUpdateIccAvailability();

    public void queryCLIP(Message message) {
    }

    public void refreshSafetySources(String str) {
    }

    public void registerForCallWaiting(Handler handler, int i, Object obj) {
    }

    public void registerForCdmaOtaStatusChange(Handler handler, int i, Object obj) {
    }

    public void registerForEcmTimerReset(Handler handler, int i, Object obj) {
    }

    public void registerForEmergencyDomainSelected(Handler handler, int i, Object obj) {
    }

    public void registerForLineControlInfo(Handler handler, int i, Object obj) {
    }

    public void registerForOemIndication(Handler handler, int i, Object obj) {
    }

    public void registerForOnHoldTone(Handler handler, int i, Object obj) {
    }

    public void registerForScbmTimerReset(Handler handler, int i, Object obj) {
    }

    public void registerForSilentRedial(Handler handler, int i, Object obj) {
    }

    public void registerForSimRecordsLoaded(Handler handler, int i, Object obj) {
    }

    public void registerForSubscriptionInfoReady(Handler handler, int i, Object obj) {
    }

    public void registerForTtyModeReceived(Handler handler, int i, Object obj) {
    }

    public void registerForVolteSilentRedial(Handler handler, int i, Object obj) {
    }

    public void registerImsEventForConcurrency() {
    }

    public void resetCarrierKeysForImsiEncryption() {
    }

    public void resetCarrierKeysForImsiEncryption(boolean z) {
    }

    public void resolveSubscriptionCarrierId(String str) {
    }

    public void sendBurstDtmf(String str, int i, int i2, Message message) {
    }

    public boolean setActiveSingleSimSlot(int i) {
        return false;
    }

    public void setCallForwardingOption(int i, int i2, String str, int i3, int i4, Message message) {
    }

    public void setCallWaiting(boolean z, int i, Message message) {
    }

    public void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo imsiEncryptionInfo, boolean z) {
    }

    public void setCarrierTestOverride(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
    }

    public void setCiwlanTimerToDealayDeactivateDataCall(long j) {
    }

    public void setEchoSuppressionEnabled() {
    }

    public void setImsRegistrationState(boolean z) {
    }

    protected void setIsInEmergencyCall() {
    }

    public void setLinkCapacityReportingCriteria(int[] iArr, int[] iArr2, int i) {
    }

    public void setNetworkSelectionModeManual(OperatorInfo operatorInfo, String str, Message message) {
    }

    public void setOnEcbModeExitResponse(Handler handler, int i, Object obj) {
    }

    public boolean setOperatorBrandOverride(String str) {
        return false;
    }

    public void setSmartTempDdsSwitchSupported(boolean z) {
    }

    public void setTelephonyTempDdsSwitch(boolean z) {
    }

    public void setTerminalBasedCallWaitingStatus(int i) {
    }

    public void setTerminalBasedCallWaitingSupported(boolean z) {
    }

    public void setVoiceServiceStateOverride(boolean z) {
    }

    public void startRingbackTone() {
    }

    public void stopRingbackTone() {
    }

    public void triggerNotifyAnbr(int i, int i2, int i3) {
    }

    public void unregisterForCallWaiting(Handler handler) {
    }

    public void unregisterForCdmaOtaStatusChange(Handler handler) {
    }

    public void unregisterForEcmTimerReset(Handler handler) {
    }

    public void unregisterForEmergencyDomainSelected(Handler handler) {
    }

    public void unregisterForLineControlInfo(Handler handler) {
    }

    public void unregisterForOemIndication(Handler handler) {
    }

    public void unregisterForOnHoldTone(Handler handler) {
    }

    public void unregisterForScbmTimerReset(Handler handler) {
    }

    public void unregisterForSilentRedial(Handler handler) {
    }

    public void unregisterForSimRecordsLoaded(Handler handler) {
    }

    public void unregisterForSubscriptionInfoReady(Handler handler) {
    }

    public void unregisterForTtyModeReceived(Handler handler) {
    }

    public void unregisterForVolteSilentRedial(Handler handler) {
    }

    public void unsetOnEcbModeExitResponse(Handler handler) {
    }

    public void unsetOnScbmExitResponse(Handler handler) {
    }

    public boolean updateCurrentCarrierInProvider() {
        return false;
    }

    public void updatePhoneObject(int i) {
    }

    public boolean useSsOverIms(Message message) {
        return false;
    }

    public static class SilentRedialParam {
        public int causeCode;
        public PhoneInternalInterface.DialArgs dialArgs;
        public String dialString;

        public SilentRedialParam(String str, int i, PhoneInternalInterface.DialArgs dialArgs) {
            this.dialString = str;
            this.causeCode = i;
            this.dialArgs = dialArgs;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        sendMessage(obtainMessage(43));
    }

    public IccRecords getIccRecords() {
        return this.mIccRecords.get();
    }

    public String getPhoneName() {
        return this.mName;
    }

    protected void setPhoneName(String str) {
        this.mName = str;
    }

    public String getActionDetached() {
        return this.mActionDetached;
    }

    public String getActionAttached() {
        return this.mActionAttached;
    }

    public String getSystemProperty(String str, String str2) {
        if (getUnitTestMode()) {
            return null;
        }
        return SystemProperties.get(str, str2);
    }

    protected Phone(String str, PhoneNotifier phoneNotifier, Context context, CommandsInterface commandsInterface, boolean z, FeatureFlags featureFlags) {
        this(str, phoneNotifier, context, commandsInterface, z, Integer.MAX_VALUE, TelephonyComponentFactory.getInstance(), featureFlags);
    }

    protected Phone(String str, PhoneNotifier phoneNotifier, Context context, CommandsInterface commandsInterface, boolean z, int i, TelephonyComponentFactory telephonyComponentFactory, FeatureFlags featureFlags) {
        this.USSD_MAX_QUEUE = 10;
        this.mSmartTempDdsSwitchSupported = false;
        this.mTelephonyTempDdsSwitch = true;
        this.mVmCount = 0;
        this.mIsVoiceCapable = true;
        this.mIsPhoneInEcmState = false;
        this.mEcmCanceledForEmergency = false;
        this.mTimeLastEmergencySmsSentMs = -1L;
        this.mEmergencySmsModeTimerMs = 0;
        this.mEmergencySmsModeInitialized = false;
        this.mCarrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.Phone$$ExternalSyntheticLambda5
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i2, int i3, int i4, int i5) {
                this.f$0.lambda$new$0(i2, i3, i4, i5);
            }
        };
        this.mIsVideoCapable = false;
        this.mUiccController = null;
        this.mIccRecords = new AtomicReference<>();
        this.mUiccApplication = new AtomicReference<>();
        this.mImsPhone = null;
        this.mRadioCapability = new AtomicReference<>();
        this.mPreferredUsageSetting = -1;
        this.mUsageSettingFromModem = -1;
        this.mIsUsageSettingSupported = true;
        this.mIsNetworkScanStarted = false;
        this.mAllowedImsServicesAny = new HashSet();
        this.mAllowedImsServicesHomeOnly = new HashSet();
        this.mPreciseCallStateRegistrants = new RegistrantList();
        this.mHandoverRegistrants = new RegistrantList();
        this.mNewRingingConnectionRegistrants = new RegistrantList();
        this.mIncomingRingRegistrants = new RegistrantList();
        this.mDisconnectRegistrants = new RegistrantList();
        this.mServiceStateRegistrants = new RegistrantList();
        this.mMmiCompleteRegistrants = new RegistrantList();
        this.mMmiRegistrants = new RegistrantList();
        this.mUnknownConnectionRegistrants = new RegistrantList();
        this.mSuppServiceFailedRegistrants = new RegistrantList();
        this.mRadioOffOrNotAvailableRegistrants = new RegistrantList();
        this.mSimRecordsLoadedRegistrants = new RegistrantList();
        this.mVideoCapabilityChangedRegistrants = new RegistrantList();
        this.mEmergencyCallToggledRegistrants = new RegistrantList();
        this.mCellInfoRegistrants = new RegistrantList();
        this.mRedialRegistrants = new RegistrantList();
        this.mPhysicalChannelConfigRegistrants = new RegistrantList();
        this.mPreferredNetworkTypeRegistrants = new RegistrantList();
        this.mAllowedNetworkTypesForReasons = new ConcurrentHashMap();
        this.mIsCarrierNrSupported = false;
        this.mIsAllowedNetworkTypesLoadedFromDb = false;
        this.mProvisioningUrlFile = new File("/data/misc/radio/provisioning_urls.xml");
        String str2 = "Phone-" + i;
        this.mLogTag = str2;
        this.mPhoneId = i;
        this.mName = str;
        this.mNotifier = phoneNotifier;
        this.mContext = context;
        this.mLooper = Looper.myLooper();
        this.mCi = commandsInterface;
        this.mActionDetached = getClass().getPackage().getName() + ".action_detached";
        this.mActionAttached = getClass().getPackage().getName() + ".action_attached";
        this.mAppSmsManager = telephonyComponentFactory.inject(AppSmsManager.class.getName()).makeAppSmsManager(context);
        this.mLocalLog = new LocalLog(64);
        this.mFeatureFlags = featureFlags;
        setUnitTestMode(z);
        PreferenceManager.getDefaultSharedPreferences(context);
        this.mCi.setOnCallRing(this, 14, null);
        this.mIsVoiceCapable = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).isVoiceCapable();
        this.mDoesRilSendMultipleCallRing = TelephonyProperties.ril_sends_multiple_call_ring().orElse(Boolean.TRUE).booleanValue();
        Rlog.d(str2, "mDoesRilSendMultipleCallRing=" + this.mDoesRilSendMultipleCallRing);
        this.mCallRingDelay = TelephonyProperties.call_ring_delay().orElse(Integer.valueOf(GbaManager.RETRY_TIME_MS)).intValue();
        Rlog.d(str2, "mCallRingDelay=" + this.mCallRingDelay);
        this.mSmsStats = new SmsStats(this);
        this.mSubscriptionManagerService = SubscriptionManagerService.getInstance();
        this.m2gAdminUpdater = new TelephonyAdminReceiver(context, this);
        if (getPhoneType() == 5) {
            return;
        }
        if (TelephonyUtils.IS_DEBUGGABLE && getPhoneType() != 7) {
            this.mTelephonyTester = new TelephonyTester(this);
        }
        this.mTelephonyComponentFactory = telephonyComponentFactory;
        this.mSmsStorageMonitor = telephonyComponentFactory.inject(SmsStorageMonitor.class.getName()).makeSmsStorageMonitor(this, featureFlags);
        this.mSmsUsageMonitor = this.mTelephonyComponentFactory.inject(SmsUsageMonitor.class.getName()).makeSmsUsageMonitor(context, featureFlags);
        UiccController uiccController = UiccController.getInstance();
        this.mUiccController = uiccController;
        uiccController.registerForIccChanged(this, 30, new Object());
        this.mSimActivationTracker = this.mTelephonyComponentFactory.inject(SimActivationTracker.class.getName()).makeSimActivationTracker(this);
        this.mCi.registerForSrvccStateChanged(this, 31, null);
        this.mTelephonyAnalytics = new TelephonyAnalytics(this);
    }

    private void updateEmergencySmsModeTimer() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(getSubId(), "emergency_sms_mode_timer_ms_int")) == null) {
            return;
        }
        this.mEmergencySmsModeTimerMs = configForSubId.getInt("emergency_sms_mode_timer_ms_int", 0);
        this.mEmergencySmsModeInitialized = true;
    }

    public void createImsPhone() {
        synchronized (lockForRadioTechnologyChange) {
            try {
                if (this.mImsPhone == null) {
                    this.mImsPhone = PhoneFactory.makeImsPhone(this.mNotifier, this);
                    CallManager.getInstance(this.mContext).registerPhone(this.mImsPhone);
                    this.mImsPhone.registerForSilentRedial(this, 32, null);
                    registerImsEventForConcurrency();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean supportsConversionOfCdmaCallerIdMmiCodesWhileRoaming() {
        PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId());
        if (configForSubId != null) {
            return configForSubId.getBoolean("convert_cdma_caller_id_mmi_codes_while_roaming_on_3gpp_bool", false);
        }
        return false;
    }

    public boolean isClirActivationAndDeactivationPrevented() {
        PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId());
        if (configForSubId == null) {
            configForSubId = CarrierConfigManager.getDefaultConfig();
        }
        return configForSubId.getBoolean("prevent_clir_activation_and_deactivation_code_bool");
    }

    public boolean shouldPreferInServiceSimForNormalRoutedEmergencyCall() {
        PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId(), "prefer_in_service_sim_for_normal_routed_emergency_calls_bool");
        if (configForSubId != null) {
            return configForSubId.getBoolean("prefer_in_service_sim_for_normal_routed_emergency_calls_bool", false);
        }
        return false;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Object obj;
        int i = message.what;
        if (i == 16 || i == 17) {
            handleSetSelectNetwork((AsyncResult) message.obj);
            onSetNetworkSelectionModeCompleted();
            return;
        }
        if (i == 14) {
            Rlog.d(this.mLogTag, "Event EVENT_CALL_RING Received state=" + getState());
            if (((AsyncResult) message.obj).exception == null) {
                PhoneConstants.State state = getState();
                if (!this.mDoesRilSendMultipleCallRing && (state == PhoneConstants.State.RINGING || state == PhoneConstants.State.IDLE)) {
                    int i2 = this.mCallRingContinueToken + 1;
                    this.mCallRingContinueToken = i2;
                    sendIncomingCallRingNotification(i2);
                    return;
                }
                notifyIncomingRing();
                return;
            }
            return;
        }
        if (i == 15) {
            Rlog.d(this.mLogTag, "Event EVENT_CALL_RING_CONTINUE Received state=" + getState());
            if (getState() == PhoneConstants.State.RINGING) {
                sendIncomingCallRingNotification(message.arg1);
                return;
            }
            return;
        }
        if (i != 34) {
            if (i == 38) {
                onCheckForNetworkSelectionModeAutomatic(message);
                return;
            }
            if (i == 43) {
                updateEmergencySmsModeTimer();
                return;
            }
            if (i == 63) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                Throwable th = asyncResult.exception;
                if (th == null) {
                    try {
                        this.mUsageSettingFromModem = ((int[]) asyncResult.result)[0];
                        logd("Received mUsageSettingFromModem=" + this.mUsageSettingFromModem);
                        if (this.mUsageSettingFromModem != this.mPreferredUsageSetting) {
                            this.mCi.setUsageSetting(obtainMessage(64), this.mPreferredUsageSetting);
                            return;
                        }
                        return;
                    } catch (ClassCastException | NullPointerException unused) {
                        Rlog.e(this.mLogTag, "Invalid response for usage setting " + asyncResult.result);
                        return;
                    }
                }
                try {
                    CommandException commandException = (CommandException) th;
                    if (commandException.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                        this.mIsUsageSettingSupported = false;
                    }
                    Rlog.w(this.mLogTag, "Unexpected failure to retrieve usage setting " + commandException);
                    return;
                } catch (ClassCastException unused2) {
                    Rlog.e(this.mLogTag, "Invalid Exception for usage setting " + asyncResult.exception);
                    return;
                }
            }
            if (i != 64) {
                switch (i) {
                    case 30:
                        onUpdateIccAvailability();
                        return;
                    case 31:
                        AsyncResult asyncResult2 = (AsyncResult) message.obj;
                        if (asyncResult2.exception == null) {
                            handleSrvccStateChanged((int[]) asyncResult2.result);
                            return;
                        }
                        Rlog.e(this.mLogTag, "Srvcc exception: " + asyncResult2.exception);
                        return;
                    case 32:
                        Rlog.d(this.mLogTag, "Event EVENT_INITIATE_SILENT_REDIAL Received");
                        AsyncResult asyncResult3 = (AsyncResult) message.obj;
                        if (asyncResult3.exception != null || (obj = asyncResult3.result) == null) {
                            return;
                        }
                        SilentRedialParam silentRedialParam = (SilentRedialParam) obj;
                        String str = silentRedialParam.dialString;
                        PhoneInternalInterface.DialArgs dialArgs = silentRedialParam.dialArgs;
                        if (TextUtils.isEmpty(str)) {
                            return;
                        }
                        try {
                            Connection connectionDialInternal = dialInternal(str, dialArgs);
                            Rlog.d(this.mLogTag, "Notify redial connection changed cn: " + connectionDialInternal);
                            Phone phone = this.mImsPhone;
                            if (phone != null) {
                                phone.notifyRedialConnectionChanged(connectionDialInternal);
                                return;
                            }
                            return;
                        } catch (CallStateException e) {
                            Rlog.e(this.mLogTag, "silent redial failed: " + e);
                            Phone phone2 = this.mImsPhone;
                            if (phone2 != null) {
                                phone2.notifyRedialConnectionChanged(null);
                                return;
                            }
                            return;
                        }
                    default:
                        throw new RuntimeException("unexpected event not handled, msgId=" + message.what);
                }
            }
            AsyncResult asyncResult4 = (AsyncResult) message.obj;
            Throwable th2 = asyncResult4.exception;
            if (th2 != null) {
                try {
                    CommandException commandException2 = (CommandException) th2;
                    if (commandException2.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                        this.mIsUsageSettingSupported = false;
                    }
                    Rlog.w(this.mLogTag, "Unexpected failure to set usage setting " + commandException2);
                    return;
                } catch (ClassCastException unused3) {
                    Rlog.e(this.mLogTag, "Invalid Exception for usage setting " + asyncResult4.exception);
                    return;
                }
            }
            this.mUsageSettingFromModem = message.arg1;
        }
    }

    private void handleSrvccStateChanged(int[] iArr) {
        Call.SrvccState srvccState;
        Rlog.d(this.mLogTag, "handleSrvccStateChanged");
        Phone phone = this.mImsPhone;
        Call.SrvccState srvccState2 = Call.SrvccState.NONE;
        if (iArr == null || iArr.length == 0) {
            return;
        }
        int i = iArr[0];
        if (phone != null) {
            phone.notifySrvccState(i);
        }
        ArrayList<Connection> handoverConnection = null;
        if (i == 0) {
            Call.SrvccState srvccState3 = Call.SrvccState.STARTED;
            if (phone != null) {
                handoverConnection = phone.getHandoverConnection();
                migrateFrom(phone);
            } else {
                Rlog.d(this.mLogTag, "HANDOVER_STARTED: mImsPhone null");
            }
            srvccState = srvccState3;
        } else if (i == 1) {
            srvccState = Call.SrvccState.COMPLETED;
        } else if (i != 2 && i != 3) {
            return;
        } else {
            srvccState = Call.SrvccState.FAILED;
        }
        getCallTracker().notifySrvccState(srvccState, handoverConnection);
        notifySrvccStateChanged(i);
    }

    public Context getContext() {
        return this.mContext;
    }

    public void registerForPreciseCallStateChanged(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mPreciseCallStateRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForPreciseCallStateChanged(Handler handler) {
        this.mPreciseCallStateRegistrants.remove(handler);
    }

    public void registerForHandoverStateChanged(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mHandoverRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForHandoverStateChanged(Handler handler) {
        this.mHandoverRegistrants.remove(handler);
    }

    public void notifyHandoverStateChanged(Connection connection) {
        this.mHandoverRegistrants.notifyRegistrants(new AsyncResult((Object) null, connection, (Throwable) null));
    }

    public void registerForRedialConnectionChanged(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mRedialRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForRedialConnectionChanged(Handler handler) {
        this.mRedialRegistrants.remove(handler);
    }

    public void notifyRedialConnectionChanged(Connection connection) {
        this.mRedialRegistrants.notifyRegistrants(new AsyncResult((Object) null, connection, (Throwable) null));
    }

    public void notifySmsSent(String str) {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService("phone");
        if (this.mContext.getPackageManager() == null || !this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony.messaging") || telephonyManager == null || !telephonyManager.isEmergencyNumber(str)) {
            return;
        }
        this.mLocalLog.log("Emergency SMS detected, recording time.");
        this.mTimeLastEmergencySmsSentMs = SystemClock.elapsedRealtime();
    }

    public boolean isInEmergencySmsMode() {
        int i;
        if (!this.mEmergencySmsModeInitialized) {
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
            if (carrierConfigManager != null) {
                carrierConfigManager.registerCarrierConfigChangeListener(new HandlerExecutor(this), this.mCarrierConfigChangeListener);
            }
            try {
                updateEmergencySmsModeTimer();
            } catch (IllegalStateException e) {
                Rlog.d(this.mLogTag, "Got exception when loading carrier config, ex=", e);
                return false;
            }
        }
        long j = this.mTimeLastEmergencySmsSentMs;
        if (j == -1 || (i = this.mEmergencySmsModeTimerMs) == 0) {
            return false;
        }
        if (i > 300000) {
            i = 300000;
        }
        boolean z = SystemClock.elapsedRealtime() <= j + ((long) i);
        if (!z) {
            this.mTimeLastEmergencySmsSentMs = -1L;
            return z;
        }
        this.mLocalLog.log("isInEmergencySmsMode: queried while eSMS mode is active.");
        return z;
    }

    public void migrateUssdFrom(Phone phone, String str, ResultReceiver resultReceiver) throws UnsupportedOperationException {
        try {
            notifyMigrateUssd(str, resultReceiver);
            migrate(this.mMmiRegistrants, phone.mMmiRegistrants);
        } catch (UnsupportedOperationException e) {
            Rlog.e(this.mLogTag, "Error: " + e);
            throw e;
        }
    }

    protected void migrateFrom(Phone phone) {
        migrate(this.mHandoverRegistrants, phone.mHandoverRegistrants);
        migrate(this.mPreciseCallStateRegistrants, phone.mPreciseCallStateRegistrants);
        migrate(this.mNewRingingConnectionRegistrants, phone.mNewRingingConnectionRegistrants);
        migrate(this.mIncomingRingRegistrants, phone.mIncomingRingRegistrants);
        migrate(this.mDisconnectRegistrants, phone.mDisconnectRegistrants);
        migrate(this.mServiceStateRegistrants, phone.mServiceStateRegistrants);
        migrate(this.mMmiCompleteRegistrants, phone.mMmiCompleteRegistrants);
        migrate(this.mMmiRegistrants, phone.mMmiRegistrants);
        migrate(this.mUnknownConnectionRegistrants, phone.mUnknownConnectionRegistrants);
        migrate(this.mSuppServiceFailedRegistrants, phone.mSuppServiceFailedRegistrants);
        migrate(this.mCellInfoRegistrants, phone.mCellInfoRegistrants);
        migrate(this.mRedialRegistrants, phone.mRedialRegistrants);
        setEcmCanceledForEmergency(phone.isEcmCanceledForEmergency());
    }

    protected void migrate(RegistrantList registrantList, RegistrantList registrantList2) {
        if (registrantList2 == null) {
            return;
        }
        registrantList2.removeCleared();
        int size = registrantList2.size();
        for (int i = 0; i < size; i++) {
            Registrant registrant = (Registrant) registrantList2.get(i);
            Message messageMessageForRegistrant = registrant.messageForRegistrant();
            if (messageMessageForRegistrant != null) {
                if (messageMessageForRegistrant.obj != CallManager.getInstance(this.mContext).getRegistrantIdentifier()) {
                    int size2 = registrantList.size();
                    int i2 = 0;
                    while (true) {
                        if (i2 < size2) {
                            if (((Registrant) registrantList.get(i2)).getHandler() == registrant.getHandler()) {
                                break;
                            } else {
                                i2++;
                            }
                        } else {
                            registrantList.add((Registrant) registrantList2.get(i));
                            break;
                        }
                    }
                }
            } else {
                Rlog.d(this.mLogTag, "msg is null");
            }
        }
    }

    public void registerForUnknownConnection(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mUnknownConnectionRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForUnknownConnection(Handler handler) {
        this.mUnknownConnectionRegistrants.remove(handler);
    }

    public void registerForNewRingingConnection(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mNewRingingConnectionRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForNewRingingConnection(Handler handler) {
        this.mNewRingingConnectionRegistrants.remove(handler);
    }

    public void registerForVideoCapabilityChanged(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mVideoCapabilityChangedRegistrants.addUnique(handler, i, obj);
        notifyForVideoCapabilityChanged(this.mIsVideoCapable);
    }

    public void unregisterForVideoCapabilityChanged(Handler handler) {
        this.mVideoCapabilityChangedRegistrants.remove(handler);
    }

    public void registerForInCallVoicePrivacyOn(Handler handler, int i, Object obj) {
        this.mCi.registerForInCallVoicePrivacyOn(handler, i, obj);
    }

    public void unregisterForInCallVoicePrivacyOn(Handler handler) {
        this.mCi.unregisterForInCallVoicePrivacyOn(handler);
    }

    public void registerForInCallVoicePrivacyOff(Handler handler, int i, Object obj) {
        this.mCi.registerForInCallVoicePrivacyOff(handler, i, obj);
    }

    public void unregisterForInCallVoicePrivacyOff(Handler handler) {
        this.mCi.unregisterForInCallVoicePrivacyOff(handler);
    }

    public void registerForIncomingRing(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mIncomingRingRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForIncomingRing(Handler handler) {
        this.mIncomingRingRegistrants.remove(handler);
    }

    public void registerForDisconnect(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mDisconnectRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForDisconnect(Handler handler) {
        this.mDisconnectRegistrants.remove(handler);
    }

    public void registerForSuppServiceFailed(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mSuppServiceFailedRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForSuppServiceFailed(Handler handler) {
        this.mSuppServiceFailedRegistrants.remove(handler);
    }

    public void registerForMmiInitiate(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mMmiRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForMmiInitiate(Handler handler) {
        this.mMmiRegistrants.remove(handler);
    }

    public void registerForMmiComplete(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mMmiCompleteRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForMmiComplete(Handler handler) {
        checkCorrectThread(handler);
        this.mMmiCompleteRegistrants.remove(handler);
    }

    public void setNetworkSelectionModeAutomatic(Message message) {
        Rlog.d(this.mLogTag, "setNetworkSelectionModeAutomatic, querying current mode");
        Message messageObtainMessage = obtainMessage(38);
        messageObtainMessage.obj = message;
        this.mCi.getNetworkSelectionMode(messageObtainMessage);
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0019  */
    private void onCheckForNetworkSelectionModeAutomatic(Message message) {
        boolean z;
        Object obj;
        AsyncResult asyncResult = (AsyncResult) message.obj;
        Message message2 = (Message) asyncResult.userObj;
        if (asyncResult.exception == null && (obj = asyncResult.result) != null) {
            try {
                z = ((int[]) obj)[0] != 0;
            } catch (Exception unused) {
            }
        }
        NetworkSelectMessage networkSelectMessage = new NetworkSelectMessage();
        networkSelectMessage.message = message2;
        networkSelectMessage.operatorNumeric = "";
        networkSelectMessage.operatorAlphaLong = "";
        networkSelectMessage.operatorAlphaShort = "";
        String str = this.mLogTag;
        if (z) {
            Rlog.d(str, "setNetworkSelectionModeAutomatic - set network selection auto");
            this.mCi.setNetworkSelectionModeAutomatic(obtainMessage(17, networkSelectMessage));
        } else {
            Rlog.d(str, "setNetworkSelectionModeAutomatic - already auto, ignoring");
            Message message3 = networkSelectMessage.message;
            if (message3 != null) {
                message3.arg1 = 1;
            }
            asyncResult.userObj = networkSelectMessage;
            handleSetSelectNetwork(asyncResult);
        }
        updateSavedNetworkOperator(networkSelectMessage);
        TelephonyCommonUtilsStub.get().restoreSettings(this);
    }

    public void getNetworkSelectionMode(Message message) {
        this.mCi.getNetworkSelectionMode(message);
    }

    public List<ClientRequestStats> getClientRequestStats() {
        return this.mCi.getClientRequestStats();
    }

    public void selectNetworkManually(OperatorInfo operatorInfo, boolean z, Message message) {
        NetworkSelectMessage networkSelectMessage = new NetworkSelectMessage();
        networkSelectMessage.message = message;
        networkSelectMessage.operatorNumeric = operatorInfo.getOperatorNumeric();
        networkSelectMessage.operatorAlphaLong = operatorInfo.getOperatorAlphaLong();
        networkSelectMessage.operatorAlphaShort = operatorInfo.getOperatorAlphaShort();
        Message messageObtainMessage = obtainMessage(16, networkSelectMessage);
        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
            this.mCi.setNetworkSelectionModeManual(MiuiNetworkControllerStub.get(this.mPhoneId).getSelectNetworkManuallyOperatorNumeric(this, operatorInfo), operatorInfo.getRan(), messageObtainMessage);
        } else {
            this.mCi.setNetworkSelectionModeManual(operatorInfo.getOperatorNumeric(), operatorInfo.getRan(), messageObtainMessage);
        }
        if (z) {
            updateSavedNetworkOperator(networkSelectMessage);
        } else {
            clearSavedNetworkSelection();
            updateManualNetworkSelection(networkSelectMessage);
        }
    }

    public void registerForEmergencyCallToggle(Handler handler, int i, Object obj) {
        this.mEmergencyCallToggledRegistrants.add(new Registrant(handler, i, obj));
    }

    public void unregisterForEmergencyCallToggle(Handler handler) {
        this.mEmergencyCallToggledRegistrants.remove(handler);
    }

    public void updateSavedNetworkOperator(NetworkSelectMessage networkSelectMessage) {
        int subId = getSubId();
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            editorEdit.putString(NETWORK_SELECTION_KEY + subId, networkSelectMessage.operatorNumeric);
            editorEdit.putString(NETWORK_SELECTION_NAME_KEY + subId, networkSelectMessage.operatorAlphaLong);
            editorEdit.putString(NETWORK_SELECTION_SHORT_KEY + subId, networkSelectMessage.operatorAlphaShort);
            if (editorEdit.commit()) {
                return;
            }
            Rlog.e(this.mLogTag, "failed to commit network selection preference");
            return;
        }
        Rlog.e(this.mLogTag, "Cannot update network selection preference due to invalid subId " + subId);
    }

    protected void updateManualNetworkSelection(NetworkSelectMessage networkSelectMessage) {
        Rlog.e(this.mLogTag, "updateManualNetworkSelection() should be overridden");
    }

    public void handleSetSelectNetwork(AsyncResult asyncResult) {
        Object obj = asyncResult.userObj;
        if (!(obj instanceof NetworkSelectMessage)) {
            Rlog.e(this.mLogTag, "unexpected result from user object.");
            return;
        }
        NetworkSelectMessage networkSelectMessage = (NetworkSelectMessage) obj;
        Message message = networkSelectMessage.message;
        if (message != null) {
            AsyncResult.forMessage(message, asyncResult.result, asyncResult.exception);
            networkSelectMessage.message.sendToTarget();
        }
    }

    public OperatorInfo getSavedNetworkSelection() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String string = defaultSharedPreferences.getString(NETWORK_SELECTION_KEY + getSubId(), "");
        return new OperatorInfo(defaultSharedPreferences.getString(NETWORK_SELECTION_NAME_KEY + getSubId(), ""), defaultSharedPreferences.getString(NETWORK_SELECTION_SHORT_KEY + getSubId(), ""), string);
    }

    protected void clearSavedNetworkSelection() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().remove(NETWORK_SELECTION_KEY + getSubId()).remove(NETWORK_SELECTION_NAME_KEY + getSubId()).remove(NETWORK_SELECTION_SHORT_KEY + getSubId()).commit();
    }

    private void restoreSavedNetworkSelection(Message message) {
        OperatorInfo savedNetworkSelection = getSavedNetworkSelection();
        if (savedNetworkSelection == null || TextUtils.isEmpty(savedNetworkSelection.getOperatorNumeric())) {
            setNetworkSelectionModeAutomatic(message);
        } else {
            selectNetworkManually(savedNetworkSelection, true, message);
        }
    }

    public void saveClirSetting(int i) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editorEdit.putInt(CLIR_KEY + getSubId(), i);
        Rlog.i(this.mLogTag, "saveClirSetting: clir_sub_key" + getSubId() + "=" + i);
        if (editorEdit.commit()) {
            return;
        }
        Rlog.e(this.mLogTag, "Failed to commit CLIR preference");
    }

    private void setUnitTestMode(boolean z) {
        this.mUnitTestMode = z;
    }

    public boolean getUnitTestMode() {
        return this.mUnitTestMode;
    }

    protected void notifyDisconnectP(Connection connection) {
        this.mDisconnectRegistrants.notifyRegistrants(new AsyncResult((Object) null, connection, (Throwable) null));
    }

    public void registerForServiceStateChanged(Handler handler, int i, Object obj) {
        this.mServiceStateRegistrants.add(handler, i, obj);
    }

    public void unregisterForServiceStateChanged(Handler handler) {
        this.mServiceStateRegistrants.remove(handler);
    }

    public void registerForRingbackTone(Handler handler, int i, Object obj) {
        this.mCi.registerForRingbackTone(handler, i, obj);
    }

    public void unregisterForRingbackTone(Handler handler) {
        this.mCi.unregisterForRingbackTone(handler);
    }

    public void registerForResendIncallMute(Handler handler, int i, Object obj) {
        this.mCi.registerForResendIncallMute(handler, i, obj);
    }

    public void unregisterForResendIncallMute(Handler handler) {
        this.mCi.unregisterForResendIncallMute(handler);
    }

    public void registerForCellInfo(Handler handler, int i, Object obj) {
        this.mCellInfoRegistrants.add(handler, i, obj);
    }

    public void unregisterForCellInfo(Handler handler) {
        this.mCellInfoRegistrants.remove(handler);
    }

    protected void notifyServiceStateChangedP(ServiceState serviceState) {
        this.mServiceStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, new ServiceState(serviceState), (Throwable) null));
        this.mNotifier.notifyServiceState(this);
    }

    protected void notifyServiceStateChangedPForSubId(ServiceState serviceState, int i) {
        this.mServiceStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, serviceState, (Throwable) null));
        this.mNotifier.notifyServiceStateForSubId(this, serviceState, i);
    }

    public SimulatedRadioControl getSimulatedRadioControl() {
        return this.mSimulatedRadioControl;
    }

    private void checkCorrectThread(Handler handler) {
        if (handler.getLooper() == this.mLooper) {
            return;
        }
        Phone$$ExternalSyntheticBUOutline1.m("com.android.internal.telephony.Phone must be used from within one thread");
    }

    Locale getLocaleFromCarrierProperties() {
        String str = SystemProperties.get("ro.carrier");
        if (str != null && str.length() != 0 && !"unknown".equals(str)) {
            CharSequence[] textArray = this.mContext.getResources().getTextArray(R.array.config_allowedGlobalInstantAppSettings);
            for (int i = 0; i < textArray.length; i += 3) {
                if (str.equals(textArray[i].toString())) {
                    return Locale.forLanguageTag(textArray[i + 1].toString().replace('_', '-'));
                }
            }
        }
        return null;
    }

    public IccFileHandler getIccFileHandler() {
        IccFileHandler iccFileHandler;
        UiccCardApplication uiccCardApplication = this.mUiccApplication.get();
        if (uiccCardApplication == null) {
            Rlog.d(this.mLogTag, "getIccFileHandler: uiccApplication == null, return null");
            iccFileHandler = null;
        } else {
            iccFileHandler = uiccCardApplication.getIccFileHandler();
        }
        Rlog.d(this.mLogTag, "getIccFileHandler: fh=" + iccFileHandler);
        return iccFileHandler;
    }

    public boolean isRadioOffForThermalMitigation() {
        ServiceStateTracker serviceStateTracker = getServiceStateTracker();
        return serviceStateTracker != null && serviceStateTracker.getRadioPowerOffReasons().contains(1);
    }

    public boolean hasCalling() {
        return TelephonyCapabilities.supportsTelephonyCalling(this.mFeatureFlags, this.mContext);
    }

    public boolean hasMessaging() {
        return TelephonyCapabilities.supportsTelephonyMessaging(this.mFeatureFlags, this.mContext);
    }

    public SignalStrengthController getSignalStrengthController() {
        Log.wtf(this.mLogTag, "getSignalStrengthController return null.");
        return null;
    }

    public void setVoiceActivationState(int i) {
        this.mSimActivationTracker.setVoiceActivationState(i);
    }

    public void setDataActivationState(int i) {
        this.mSimActivationTracker.setDataActivationState(i);
    }

    public int getVoiceActivationState() {
        return this.mSimActivationTracker.getVoiceActivationState();
    }

    public int getDataActivationState() {
        return this.mSimActivationTracker.getDataActivationState();
    }

    public void updateVoiceMail() {
        Rlog.e(this.mLogTag, "updateVoiceMail() should be overridden");
    }

    public IccCardApplicationStatus.AppType getCurrentUiccAppType() {
        UiccCardApplication uiccCardApplication = this.mUiccApplication.get();
        if (uiccCardApplication != null) {
            return uiccCardApplication.getType();
        }
        return IccCardApplicationStatus.AppType.APPTYPE_UNKNOWN;
    }

    public String getIccSerialNumber() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getIccId();
        }
        return null;
    }

    public String getFullIccSerialNumber() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getFullIccId();
        }
        return null;
    }

    public boolean getIccRecordsLoaded() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getRecordsLoaded();
        }
        return false;
    }

    public void setCellInfoMinInterval(int i) {
        getServiceStateTracker().setCellInfoMinInterval(i);
    }

    public List<CellInfo> getAllCellInfo() {
        return getServiceStateTracker().getAllCellInfo();
    }

    public void requestCellInfoUpdate(WorkSource workSource, Message message) {
        getServiceStateTracker().requestAllCellInfo(workSource, message);
    }

    public CellIdentity getCurrentCellIdentity() {
        return getServiceStateTracker().getCellIdentity();
    }

    public void getCellIdentity(WorkSource workSource, Message message) {
        getServiceStateTracker().requestCellIdentity(workSource, message);
    }

    public void setCellInfoListRate(int i, WorkSource workSource) {
        this.mCi.setCellInfoListRate(i, null, workSource);
    }

    public boolean getMessageWaitingIndicator() {
        return this.mVmCount != 0;
    }

    protected int getCallForwardingIndicatorFromSharedPref() {
        String string;
        int subId = getSubId();
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
            int i = defaultSharedPreferences.getInt(CF_STATUS + subId, -1);
            Rlog.d(this.mLogTag, "getCallForwardingIndicatorFromSharedPref: for subId " + subId + "= " + i);
            if (i == -1 && (string = defaultSharedPreferences.getString(CF_ID, null)) != null) {
                if (string.equals(getSubscriberId())) {
                    i = defaultSharedPreferences.getInt(CF_STATUS, 0);
                    setCallForwardingIndicatorInSharedPref(i == 1);
                    Rlog.d(this.mLogTag, "getCallForwardingIndicatorFromSharedPref: " + i);
                } else {
                    Rlog.d(this.mLogTag, "getCallForwardingIndicatorFromSharedPref: returning DISABLED as status for matching subscriberId not found");
                }
                SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
                editorEdit.remove(CF_ID);
                editorEdit.remove(CF_STATUS);
                editorEdit.apply();
            }
            return i;
        }
        Rlog.e(this.mLogTag, "getCallForwardingIndicatorFromSharedPref: invalid subId " + subId);
        return 0;
    }

    protected void setCallForwardingIndicatorInSharedPref(boolean z) {
        int subId = getSubId();
        Rlog.i(this.mLogTag, "setCallForwardingIndicatorInSharedPref: Storing status = " + (z ? 1 : 0) + " in pref cf_status_key" + subId);
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        StringBuilder sb = new StringBuilder(CF_STATUS);
        sb.append(subId);
        editorEdit.putInt(sb.toString(), z ? 1 : 0);
        editorEdit.apply();
    }

    public void setVoiceCallForwardingFlag(int i, boolean z, String str) {
        setCallForwardingIndicatorInSharedPref(z);
        IccRecords iccRecords = getIccRecords();
        if (iccRecords != null) {
            iccRecords.setVoiceCallForwardingFlag(i, z, str);
        }
        notifyCallForwardingIndicator();
    }

    public void setVoiceCallForwardingFlag(IccRecords iccRecords, int i, boolean z, String str) {
        setCallForwardingIndicatorInSharedPref(z);
        if (iccRecords != null) {
            iccRecords.setVoiceCallForwardingFlag(i, z, str);
        }
        notifyCallForwardingIndicator();
    }

    public boolean getCallForwardingIndicator() {
        IccRecords iccRecords = getIccRecords();
        int voiceCallForwardingFlag = iccRecords != null ? iccRecords.getVoiceCallForwardingFlag() : -1;
        if (voiceCallForwardingFlag == -1) {
            voiceCallForwardingFlag = getCallForwardingIndicatorFromSharedPref();
        }
        String str = this.mLogTag;
        StringBuilder sb = new StringBuilder("getCallForwardingIndicator: iccForwardingFlag=");
        sb.append(iccRecords != null ? Integer.valueOf(iccRecords.getVoiceCallForwardingFlag()) : "null");
        sb.append(", sharedPrefFlag=");
        sb.append(getCallForwardingIndicatorFromSharedPref());
        Rlog.v(str, sb.toString());
        return voiceCallForwardingFlag == 1;
    }

    public void setVideoCallForwardingPreference(boolean z) {
        Rlog.d(this.mLogTag, "Set video call forwarding info to preferences enabled = " + z + "subId = " + getSubId());
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        StringBuilder sb = new StringBuilder(CF_VIDEO);
        sb.append(getSubId());
        editorEdit.putBoolean(sb.toString(), z);
        editorEdit.commit();
    }

    public boolean getVideoCallForwardingPreference() {
        Rlog.d(this.mLogTag, "Get video call forwarding info from preferences");
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean(CF_VIDEO + getSubId(), false);
    }

    public CarrierSignalAgent getCarrierSignalAgent() {
        return this.mCarrierSignalAgent;
    }

    public CarrierActionAgent getCarrierActionAgent() {
        return this.mCarrierActionAgent;
    }

    public SignalStrength getSignalStrength() {
        SignalStrengthController signalStrengthController = getSignalStrengthController();
        if (signalStrengthController == null) {
            return new SignalStrength();
        }
        return signalStrengthController.getSignalStrength();
    }

    public boolean isConcurrentVoiceAndDataAllowed() {
        ServiceStateTracker serviceStateTracker = getServiceStateTracker();
        if (serviceStateTracker == null) {
            return false;
        }
        return serviceStateTracker.isConcurrentVoiceAndDataAllowed();
    }

    public long getEffectiveAllowedNetworkTypes() {
        long allNetworkTypesBitmask = TelephonyManager.getAllNetworkTypesBitmask();
        synchronized (this.mAllowedNetworkTypesForReasons) {
            try {
                Iterator it = this.mAllowedNetworkTypesForReasons.values().iterator();
                while (it.hasNext()) {
                    allNetworkTypesBitmask &= ((Long) it.next()).longValue();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (!this.mIsCarrierNrSupported) {
            allNetworkTypesBitmask &= -524289;
        }
        if (this.m2gAdminUpdater.isCellular2gDisabled()) {
            logd("SubId " + getSubId() + " disabling 2g in getEffectiveAllowedNetworkTypes according to admin user restriction");
            allNetworkTypesBitmask &= -32844;
        }
        logd("SubId" + getSubId() + ",getEffectiveAllowedNetworkTypes: " + TelephonyManager.convertNetworkTypeBitmaskToString(allNetworkTypesBitmask));
        return allNetworkTypesBitmask;
    }

    public void notifyAllowedNetworkTypesChanged(int i) {
        logd("SubId" + getSubId() + ",notifyAllowedNetworkTypesChanged: reason: " + convertAllowedNetworkTypeMapIndexToDbName(i) + " value:" + TelephonyManager.convertNetworkTypeBitmaskToString(getAllowedNetworkTypes(i)));
        this.mNotifier.notifyAllowedNetworkTypesChanged(this, i, getAllowedNetworkTypes(i));
    }

    public void isNrDualConnectivityEnabled(Message message, WorkSource workSource) {
        this.mCi.isNrDualConnectivityEnabled(message, workSource);
    }

    public void setNrDualConnectivityState(int i, Message message, WorkSource workSource) {
        this.mCi.setNrDualConnectivityState(i, message, workSource);
    }

    public long getAllowedNetworkTypes(int i) {
        long jLongValue;
        long rafFromNetworkType = RadioAccessFamily.getRafFromNetworkType(RILConstants.PREFERRED_NETWORK_MODE);
        if (!TelephonyManager.isValidAllowedNetworkTypesReason(i)) {
            Phone$$ExternalSyntheticBUOutline0.m("AllowedNetworkTypes NumberFormat exception");
            return 0L;
        }
        synchronized (this.mAllowedNetworkTypesForReasons) {
            jLongValue = ((Long) this.mAllowedNetworkTypesForReasons.getOrDefault(Integer.valueOf(i), Long.valueOf(rafFromNetworkType))).longValue();
        }
        if (!this.mIsCarrierNrSupported && i == 2) {
            jLongValue = updateAllowedNetworkTypeForCarrierWithCarrierConfig();
        }
        logd("SubId" + getSubId() + ",get allowed network types " + convertAllowedNetworkTypeMapIndexToDbName(i) + ": value = " + TelephonyManager.convertNetworkTypeBitmaskToString(jLongValue));
        return jLongValue;
    }

    public void loadAllowedNetworksFromSubscriptionDatabase() {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(getSubId());
        String allowedNetworkTypesForReasons = subscriptionInfoInternal != null ? subscriptionInfoInternal.getAllowedNetworkTypesForReasons() : null;
        this.mIsAllowedNetworkTypesLoadedFromDb = SubscriptionManager.isValidSubscriptionId(getSubId());
        if (TextUtils.isEmpty(allowedNetworkTypesForReasons)) {
            return;
        }
        logd("SubId" + getSubId() + ",load allowed network types : value = " + allowedNetworkTypesForReasons);
        HashMap map = new HashMap(this.mAllowedNetworkTypesForReasons);
        this.mAllowedNetworkTypesForReasons.clear();
        try {
            for (String str : allowedNetworkTypesForReasons.trim().split(",")) {
                String[] strArrSplit = str.trim().toLowerCase(Locale.ROOT).split("=");
                if (strArrSplit.length != 2) {
                    Rlog.e(this.mLogTag, "Invalid ALLOWED_NETWORK_TYPES from DB, value = " + str);
                } else {
                    int iConvertAllowedNetworkTypeDbNameToMapIndex = convertAllowedNetworkTypeDbNameToMapIndex(strArrSplit[0]);
                    long j = Long.parseLong(strArrSplit[1]);
                    if (TelephonyManager.isValidAllowedNetworkTypesReason(iConvertAllowedNetworkTypeDbNameToMapIndex) && j != -1) {
                        synchronized (this.mAllowedNetworkTypesForReasons) {
                            try {
                                this.mAllowedNetworkTypesForReasons.put(Integer.valueOf(iConvertAllowedNetworkTypeDbNameToMapIndex), Long.valueOf(j));
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                        if (!map.containsKey(Integer.valueOf(iConvertAllowedNetworkTypeDbNameToMapIndex)) || ((Long) map.get(Integer.valueOf(iConvertAllowedNetworkTypeDbNameToMapIndex))).longValue() != j) {
                            if (map.containsKey(Integer.valueOf(iConvertAllowedNetworkTypeDbNameToMapIndex))) {
                                map.remove(Integer.valueOf(iConvertAllowedNetworkTypeDbNameToMapIndex));
                            }
                            notifyAllowedNetworkTypesChanged(iConvertAllowedNetworkTypeDbNameToMapIndex);
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            Rlog.e(this.mLogTag, "allowedNetworkTypes NumberFormat exception" + e);
        }
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            notifyAllowedNetworkTypesChanged(((Integer) it.next()).intValue());
        }
    }

    private int convertAllowedNetworkTypeDbNameToMapIndex(String str) {
        str.hashCode();
        switch (str) {
            case "enable_2g":
                return 3;
            case "test":
                return 4;
            case "user":
                return 0;
            case "power":
                return 1;
            case "carrier":
                return 2;
            default:
                return -1;
        }
    }

    public static String convertAllowedNetworkTypeMapIndexToDbName(int i) {
        if (i == 0) {
            return "user";
        }
        if (i == 1) {
            return "power";
        }
        if (i == 2) {
            return "carrier";
        }
        if (i == 3) {
            return "enable_2g";
        }
        if (i == 4) {
            return "test";
        }
        Phone$$ExternalSyntheticBUOutline3.m("No DB name conversion available for allowed network type reason: ", i, ". Did you forget to add an ALLOWED_NETWORK_TYPE_TEXT entry for a new reason?");
        return null;
    }

    private long updateAllowedNetworkTypeForCarrierWithCarrierConfig() {
        long jLongValue;
        long rafFromNetworkType = RadioAccessFamily.getRafFromNetworkType(RILConstants.PREFERRED_NETWORK_MODE);
        synchronized (this.mAllowedNetworkTypesForReasons) {
            jLongValue = ((Long) this.mAllowedNetworkTypesForReasons.getOrDefault(2, Long.valueOf(rafFromNetworkType))).longValue();
        }
        if (this.mIsCarrierNrSupported) {
            return jLongValue;
        }
        long j = jLongValue & (-524289);
        logd("Allowed network types for 'carrier' reason is changed by carrier config = " + TelephonyManager.convertNetworkTypeBitmaskToString(j));
        return j;
    }

    public void setAllowedNetworkTypes(int i, long j, Message message) {
        String str;
        int subId = getSubId();
        if (!TelephonyManager.isValidAllowedNetworkTypesReason(i)) {
            loge("setAllowedNetworkTypes: Invalid allowed network type reason: " + i);
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.INVALID_ARGUMENTS));
                message.sendToTarget();
                return;
            }
            return;
        }
        if (!SubscriptionManager.isUsableSubscriptionId(subId) || !this.mIsAllowedNetworkTypesLoadedFromDb) {
            loge("setAllowedNetworkTypes: no sim or network type is not loaded. SubscriptionId: " + subId + ", isNetworkTypeLoaded" + this.mIsAllowedNetworkTypesLoadedFromDb);
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.MISSING_RESOURCE));
                message.sendToTarget();
                return;
            }
            return;
        }
        if (MiuiTelephonyManagerStub.get().shouldRestrictNetworkForEuiccBinding(this.mPhoneId) && ((32843 & j) != 0 || (93108 & j) != 0)) {
            loge("setAllowedNetworkTypes: SubId" + subId + ",setAllowedNetworkTypes " + j + " is not allowed by P83X CN esim");
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.INVALID_ARGUMENTS));
                message.sendToTarget();
                return;
            }
            return;
        }
        synchronized (this.mAllowedNetworkTypesForReasons) {
            this.mAllowedNetworkTypesForReasons.put(Integer.valueOf(i), Long.valueOf(j));
            str = (String) this.mAllowedNetworkTypesForReasons.keySet().stream().map(new Function() { // from class: com.android.internal.telephony.Phone$$ExternalSyntheticLambda4
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.lambda$setAllowedNetworkTypes$1((Integer) obj);
                }
            }).collect(Collectors.joining(","));
        }
        SubscriptionManager.setSubscriptionProperty(subId, "allowed_network_types_for_reasons", str);
        logl("setAllowedNetworkTypes: subId=" + subId + ", reason=" + convertAllowedNetworkTypeMapIndexToDbName(i) + ", allowed network types=" + TelephonyManager.convertNetworkTypeBitmaskToString(j) + ", uid=" + Binder.getCallingUid());
        updateAllowedNetworkTypes(message);
        notifyAllowedNetworkTypesChanged(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$setAllowedNetworkTypes$1(Integer num) {
        return convertAllowedNetworkTypeMapIndexToDbName(num.intValue()) + "=" + this.mAllowedNetworkTypesForReasons.get(num);
    }

    public void updateAllowedNetworkTypes(Message message) {
        int radioAccessFamily = getRadioAccessFamily();
        if (radioAccessFamily == 0) {
            Rlog.d(this.mLogTag, "setPreferredNetworkType: Abort, unknown RAF: " + radioAccessFamily);
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.GENERIC_FAILURE));
                message.sendToTarget();
                return;
            }
            return;
        }
        long j = radioAccessFamily;
        int effectiveAllowedNetworkTypes = (int) (getEffectiveAllowedNetworkTypes() & j);
        logd("setAllowedNetworkTypes: modemRafBitMask = " + radioAccessFamily + " ,modemRaf = " + TelephonyManager.convertNetworkTypeBitmaskToString(j) + " ,filteredRafBitMask = " + effectiveAllowedNetworkTypes + " ,filteredRaf = " + TelephonyManager.convertNetworkTypeBitmaskToString(effectiveAllowedNetworkTypes));
        this.mCi.setAllowedNetworkTypesBitmap(effectiveAllowedNetworkTypes, message);
        this.mPreferredNetworkTypeRegistrants.notifyRegistrants();
    }

    public void getAllowedNetworkTypesBitmask(Message message) {
        this.mCi.getAllowedNetworkTypesBitmap(message);
    }

    public void registerForPreferredNetworkTypeChanged(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        this.mPreferredNetworkTypeRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForPreferredNetworkTypeChanged(Handler handler) {
        this.mPreferredNetworkTypeRegistrants.remove(handler);
    }

    public int getCachedAllowedNetworkTypesBitmask() {
        CommandsInterface commandsInterface = this.mCi;
        if (commandsInterface != null && (commandsInterface instanceof BaseCommands)) {
            return ((BaseCommands) commandsInterface).mAllowedNetworkTypesBitmask;
        }
        return RadioAccessFamily.getRafFromNetworkType(RILConstants.PREFERRED_NETWORK_MODE);
    }

    public void getSmscAddress(Message message) {
        this.mCi.getSmscAddress(message);
    }

    public void setSmscAddress(String str, Message message) {
        this.mCi.setSmscAddress(str, message);
    }

    public void setTTYMode(int i, Message message) {
        this.mCi.setTTYMode(i, message);
    }

    public void setUiTTYMode(int i, Message message) {
        Rlog.d(this.mLogTag, "unexpected setUiTTYMode method call");
    }

    public void queryTTYMode(Message message) {
        this.mCi.queryTTYMode(message);
    }

    public void rebootModem(Message message) {
        RadioConfig radioConfig = RadioConfig.getInstance();
        RadioConfigProxy radioConfigProxy = radioConfig.getRadioConfigProxy(null);
        if (radioConfigProxy != null && radioConfigProxy.getVersion().greaterOrEqual(RIL.RADIO_HAL_VERSION_2_4)) {
            radioConfig.rebootModem(message);
        } else {
            this.mCi.nvResetConfig(1, message);
        }
    }

    public boolean eraseDataInSharedPreferences() {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        Rlog.d(this.mLogTag, "Erase all data saved in SharedPreferences");
        editorEdit.clear();
        return editorEdit.commit();
    }

    public void setSystemSelectionChannels(List<RadioAccessSpecifier> list, Message message) {
        this.mCi.setSystemSelectionChannels(list, message);
    }

    public void getSystemSelectionChannels(Message message) {
        this.mCi.getSystemSelectionChannels(message);
    }

    public void notifyDataActivity() {
        this.mNotifier.notifyDataActivity(this);
    }

    private void notifyMessageWaitingIndicator() {
        if (this.mIsVoiceCapable) {
            this.mNotifier.notifyMessageWaitingChanged(this);
        }
    }

    public void notifyDataConnection(PreciseDataConnectionState preciseDataConnectionState) {
        this.mNotifier.notifyDataConnection(this, preciseDataConnectionState);
    }

    public void notifyVoiceActivationStateChanged(int i) {
        this.mNotifier.notifyVoiceActivationStateChanged(this, i);
    }

    public void notifyDataActivationStateChanged(int i) {
        this.mNotifier.notifyDataActivationStateChanged(this, i);
    }

    public void notifyUserMobileDataStateChanged(boolean z) {
        this.mNotifier.notifyUserMobileDataStateChanged(this, z);
    }

    public void notifyDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
        this.mNotifier.notifyDisplayInfoChanged(this, telephonyDisplayInfo);
    }

    public void notifySignalStrength() {
        this.mNotifier.notifySignalStrength(this);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public PhoneConstants.DataState getDataConnectionState(String str) {
        return PhoneConstants.DataState.DISCONNECTED;
    }

    public void notifyCellInfo(List<CellInfo> list) {
        this.mCellInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, list, (Throwable) null));
        this.mNotifier.notifyCellInfo(this, list);
    }

    protected void notifyMigrateUssd(String str, ResultReceiver resultReceiver) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("notifyMigrateUssd: not supported");
    }

    public void registerForPhysicalChannelConfig(Handler handler, int i, Object obj) {
        checkCorrectThread(handler);
        Registrant registrant = new Registrant(handler, i, obj);
        this.mPhysicalChannelConfigRegistrants.add(registrant);
        List<PhysicalChannelConfig> physicalChannelConfigList = getPhysicalChannelConfigList();
        if (physicalChannelConfigList != null) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, physicalChannelConfigList, (Throwable) null));
        }
    }

    public void unregisterForPhysicalChannelConfig(Handler handler) {
        this.mPhysicalChannelConfigRegistrants.remove(handler);
    }

    public void notifyPhysicalChannelConfig(List<PhysicalChannelConfig> list) {
        this.mPhysicalChannelConfigRegistrants.notifyRegistrants(new AsyncResult((Object) null, list, (Throwable) null));
        this.mNotifier.notifyPhysicalChannelConfig(this, list);
    }

    public void notifySrvccStateChanged(int i) {
        this.mNotifier.notifySrvccStateChanged(this, i);
    }

    public void notifyEmergencyNumberList() {
        this.mNotifier.notifyEmergencyNumberList(this);
    }

    public void notifyOutgoingEmergencySms(EmergencyNumber emergencyNumber) {
        this.mNotifier.notifyOutgoingEmergencySms(this, emergencyNumber);
    }

    public void notifyDataEnabled(boolean z, int i) {
        this.mNotifier.notifyDataEnabled(this, z, i);
    }

    public void notifyLinkCapacityEstimateChanged(List<LinkCapacityEstimate> list) {
        this.mNotifier.notifyLinkCapacityEstimateChanged(this, list);
    }

    protected static boolean getInEcmMode() {
        if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
            return EmergencyStateTracker.getInstance().isInEcm();
        }
        return TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE).booleanValue();
    }

    public boolean isInEcm() {
        if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
            return EmergencyStateTracker.getInstance().isInEcm();
        }
        return this.mIsPhoneInEcmState;
    }

    public void setIsInEcm(boolean z) {
        if (!getUnitTestMode()) {
            TelephonyProperties.in_ecm_mode(Boolean.valueOf(z));
        }
        this.mIsPhoneInEcmState = z;
    }

    public boolean isEcmCanceledForEmergency() {
        return this.mEcmCanceledForEmergency;
    }

    public void setEcmCanceledForEmergency(boolean z) {
        this.mEcmCanceledForEmergency = z;
    }

    private static int getVideoState(Call call) {
        Connection earliestConnection = call.getEarliestConnection();
        if (earliestConnection != null) {
            return earliestConnection.getVideoState();
        }
        return 0;
    }

    private boolean isVideoCallOrConference(Call call) {
        ImsCall imsCall;
        if (call.isMultiparty()) {
            return true;
        }
        return (call instanceof ImsPhoneCall) && (imsCall = ((ImsPhoneCall) call).getImsCall()) != null && (imsCall.isVideoCall() || imsCall.wasVideoCall());
    }

    public boolean isImsVideoCallOrConferencePresent() {
        Phone phone = this.mImsPhone;
        boolean z = false;
        if (phone != null && (isVideoCallOrConference(phone.getForegroundCall()) || isVideoCallOrConference(this.mImsPhone.getBackgroundCall()) || isVideoCallOrConference(this.mImsPhone.getRingingCall()))) {
            z = true;
        }
        Rlog.d(this.mLogTag, "isImsVideoCallOrConferencePresent: " + z);
        return z;
    }

    public int getVoiceMessageCount() {
        return this.mVmCount;
    }

    public void setVoiceMessageCount(int i) {
        this.mVmCount = i;
        int subId = getSubId();
        boolean zIsValidSubscriptionId = SubscriptionManager.isValidSubscriptionId(subId);
        String str = this.mLogTag;
        if (zIsValidSubscriptionId) {
            Rlog.d(str, "setVoiceMessageCount: Storing Voice Mail Count = " + i + " for mVmCountKey = vm_count_key" + subId + " in preferences.");
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
            StringBuilder sb = new StringBuilder("vm_count_key");
            sb.append(subId);
            editorEdit.putInt(sb.toString(), i);
            editorEdit.apply();
        } else {
            Rlog.e(str, "setVoiceMessageCount in sharedPreference: invalid subId " + subId);
        }
        IccRecords iccRecords = UiccController.getInstance().getIccRecords(this.mPhoneId, 1);
        String str2 = this.mLogTag;
        if (iccRecords != null) {
            Rlog.d(str2, "setVoiceMessageCount: updating SIM Records");
            iccRecords.setVoiceMessageWaiting(1, i);
        } else {
            Rlog.d(str2, "setVoiceMessageCount: SIM Records not found");
        }
        notifyMessageWaitingIndicator();
    }

    protected int getStoredVoiceMessageCount() {
        int subId = getSubId();
        int i = 0;
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
            int i2 = defaultSharedPreferences.getInt("vm_count_key" + subId, -2);
            if (i2 != -2) {
                Rlog.d(this.mLogTag, "getStoredVoiceMessageCount: from preference for subId " + subId + "= " + i2);
                return i2;
            }
            String string = defaultSharedPreferences.getString("vm_id_key", null);
            if (string != null) {
                String subscriberId = getSubscriberId();
                if (subscriberId != null && subscriberId.equals(string)) {
                    i = defaultSharedPreferences.getInt("vm_count_key", 0);
                    setVoiceMessageCount(i);
                    Rlog.d(this.mLogTag, "getStoredVoiceMessageCount: from preference = " + i);
                } else {
                    Rlog.d(this.mLogTag, "getStoredVoiceMessageCount: returning 0 as count for matching subscriberId not found");
                }
                SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
                editorEdit.remove("vm_id_key");
                editorEdit.remove("vm_count_key");
                editorEdit.apply();
            }
            return i;
        }
        Rlog.e(this.mLogTag, "getStoredVoiceMessageCount: invalid subId " + subId);
        return 0;
    }

    public void sendDialerSpecialCode(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        BroadcastOptions broadcastOptionsMakeBasic = BroadcastOptions.makeBasic();
        broadcastOptionsMakeBasic.setBackgroundActivityStartsAllowed(true);
        Intent intent = new Intent("android.provider.Telephony.SECRET_CODE", Uri.parse("android_secret_code://" + str));
        intent.addFlags(268435488);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT, null, broadcastOptionsMakeBasic.toBundle());
        Intent intent2 = new Intent("android.telephony.action.SECRET_CODE", Uri.parse("android_secret_code://" + str));
        intent2.addFlags(268435456);
        this.mContext.sendBroadcastAsUser(intent2, UserHandle.ALL, null, broadcastOptionsMakeBasic.toBundle());
    }

    public void setOnPostDialCharacter(Handler handler, int i, Object obj) {
        this.mPostDialHandler = new Registrant(handler, i, obj);
    }

    public Registrant getPostDialHandler() {
        return this.mPostDialHandler;
    }

    public void registerForSignalInfo(Handler handler, int i, Object obj) {
        this.mCi.registerForSignalInfo(handler, i, obj);
    }

    public void unregisterForSignalInfo(Handler handler) {
        this.mCi.unregisterForSignalInfo(handler);
    }

    public void registerForDisplayInfo(Handler handler, int i, Object obj) {
        this.mCi.registerForDisplayInfo(handler, i, obj);
    }

    public void unregisterForDisplayInfo(Handler handler) {
        this.mCi.unregisterForDisplayInfo(handler);
    }

    public void registerForRadioOffOrNotAvailable(Handler handler, int i, Object obj) {
        this.mRadioOffOrNotAvailableRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForRadioOffOrNotAvailable(Handler handler) {
        this.mRadioOffOrNotAvailableRegistrants.remove(handler);
    }

    private String getProvisioningUrlBaseFromFile() {
        String attributeValue;
        String attributeValue2;
        Configuration configuration = this.mContext.getResources().getConfiguration();
        try {
            FileReader fileReader = new FileReader(this.mProvisioningUrlFile);
            try {
                XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                xmlPullParserNewPullParser.setInput(fileReader);
                XmlUtils.beginDocument(xmlPullParserNewPullParser, "provisioningUrls");
                while (true) {
                    XmlUtils.nextElement(xmlPullParserNewPullParser);
                    String name = xmlPullParserNewPullParser.getName();
                    if (name != null) {
                        if (name.equals("provisioningUrl") && (attributeValue = xmlPullParserNewPullParser.getAttributeValue(null, "mcc")) != null) {
                            try {
                                if (Integer.parseInt(attributeValue) == configuration.mcc && (attributeValue2 = xmlPullParserNewPullParser.getAttributeValue(null, "mnc")) != null && Integer.parseInt(attributeValue2) == configuration.mnc) {
                                    xmlPullParserNewPullParser.next();
                                    if (xmlPullParserNewPullParser.getEventType() == 4) {
                                        String text = xmlPullParserNewPullParser.getText();
                                        fileReader.close();
                                        return text;
                                    }
                                    continue;
                                }
                            } catch (NumberFormatException e) {
                                Rlog.e(this.mLogTag, "Exception in getProvisioningUrlBaseFromFile: " + e);
                            }
                        }
                    } else {
                        fileReader.close();
                        return null;
                    }
                }
            } catch (Throwable th) {
                try {
                    fileReader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (FileNotFoundException unused) {
            Rlog.e(this.mLogTag, "Carrier Provisioning Urls file not found");
            return null;
        } catch (IOException e2) {
            Rlog.e(this.mLogTag, "I/O exception reading Carrier Provisioning Urls file: " + e2);
            return null;
        } catch (XmlPullParserException e3) {
            Rlog.e(this.mLogTag, "Xml parser exception reading Carrier Provisioning Urls file: " + e3);
            return null;
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getMobileProvisioningUrl() {
        String provisioningUrlBaseFromFile = getProvisioningUrlBaseFromFile();
        if (TextUtils.isEmpty(provisioningUrlBaseFromFile)) {
            provisioningUrlBaseFromFile = this.mContext.getResources().getString(R.string.permdesc_receiveMms);
            Rlog.d(this.mLogTag, "getMobileProvisioningUrl: url from resource =" + provisioningUrlBaseFromFile);
        } else {
            Rlog.d(this.mLogTag, "getMobileProvisioningUrl: url from File =" + provisioningUrlBaseFromFile);
        }
        if (TextUtils.isEmpty(provisioningUrlBaseFromFile)) {
            return provisioningUrlBaseFromFile;
        }
        String line1Number = getLine1Number();
        if (TextUtils.isEmpty(line1Number)) {
            line1Number = "0000000000";
        }
        return String.format(provisioningUrlBaseFromFile, getIccSerialNumber(), getDeviceId(), line1Number);
    }

    public boolean hasMatchedTetherApnSetting() {
        NetworkRegistrationInfo networkRegistrationInfo = getServiceState().getNetworkRegistrationInfo(2, 1);
        if (networkRegistrationInfo != null) {
            return getDataNetworkController().getDataProfileManager().isTetheringDataProfileExisting(networkRegistrationInfo.getAccessNetworkTechnology());
        }
        return false;
    }

    public boolean isDataAllowed() {
        return getDataNetworkController().isInternetDataAllowed(false);
    }

    public void carrierActionSetMeteredApnsEnabled(boolean z) {
        this.mCarrierActionAgent.carrierActionSetMeteredApnsEnabled(z);
    }

    public void carrierActionSetRadioEnabled(boolean z) {
        this.mCarrierActionAgent.carrierActionSetRadioEnabled(z);
    }

    public void carrierActionReportDefaultNetworkStatus(boolean z) {
        this.mCarrierActionAgent.carrierActionReportDefaultNetworkStatus(z);
    }

    public void carrierActionResetAll() {
        this.mCarrierActionAgent.carrierActionReset();
    }

    public void notifyNewRingingConnectionP(Connection connection) {
        Rlog.i(this.mLogTag, String.format("notifyNewRingingConnection: phoneId=[%d], connection=[%s], registrants=[%s]", Integer.valueOf(getPhoneId()), connection, getNewRingingConnectionRegistrantsAsString()));
        if (!this.mIsVoiceCapable) {
            try {
                connection.hangup();
            } catch (CallStateException unused) {
                Rlog.e(LOG_TAG, "hangup call for voice capable is false");
            }
        } else {
            this.mNewRingingConnectionRegistrants.notifyRegistrants(new AsyncResult((Object) null, connection, (Throwable) null));
        }
    }

    private String getNewRingingConnectionRegistrantsAsString() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mNewRingingConnectionRegistrants.size(); i++) {
            arrayList.add(this.mNewRingingConnectionRegistrants.get(i).toString());
        }
        return String.join(", ", arrayList);
    }

    public void notifyUnknownConnectionP(Connection connection) {
        this.mUnknownConnectionRegistrants.notifyResult(connection);
    }

    public void notifyForVideoCapabilityChanged(boolean z) {
        this.mIsVideoCapable = z;
        this.mVideoCapabilityChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, Boolean.valueOf(z), (Throwable) null));
    }

    private void notifyIncomingRing() {
        if (this.mIsVoiceCapable) {
            this.mIncomingRingRegistrants.notifyRegistrants(new AsyncResult((Object) null, this, (Throwable) null));
        }
    }

    private void sendIncomingCallRingNotification(int i) {
        if (this.mIsVoiceCapable && !this.mDoesRilSendMultipleCallRing && i == this.mCallRingContinueToken) {
            Rlog.d(this.mLogTag, "Sending notifyIncomingRing");
            notifyIncomingRing();
            sendMessageDelayed(obtainMessage(15, i, 0), this.mCallRingDelay);
            return;
        }
        Rlog.d(this.mLogTag, "Ignoring ring notification request, mDoesRilSendMultipleCallRing=" + this.mDoesRilSendMultipleCallRing + " token=" + i + " mCallRingContinueToken=" + this.mCallRingContinueToken + " mIsVoiceCapable=" + this.mIsVoiceCapable);
    }

    public void setAlwaysReportSignalStrength(boolean z) {
        DeviceStateMonitor deviceStateMonitor = this.mDeviceStateMonitor;
        if (deviceStateMonitor != null) {
            deviceStateMonitor.setAlwaysReportSignalStrength(z);
        }
    }

    public IsimRecords getIsimRecords() {
        Rlog.e(this.mLogTag, "getIsimRecords() is only supported on LTE devices");
        return null;
    }

    public void setVoiceMessageWaiting(int i, int i2) {
        Rlog.e(this.mLogTag, "Error! This function should never be executed, inactive Phone.");
    }

    public UsimServiceTable getUsimServiceTable() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getUsimServiceTable();
        }
        return null;
    }

    public UiccCard getUiccCard() {
        return this.mUiccController.getUiccCard(this.mPhoneId);
    }

    public UiccPort getUiccPort() {
        return this.mUiccController.getUiccPort(this.mPhoneId);
    }

    public Phone getImsPhone() {
        return this.mImsPhone;
    }

    public void setImsPhone(ImsPhone imsPhone) {
        this.mImsPhone = imsPhone;
    }

    public boolean isOutgoingImsVoiceAllowed() {
        Phone phone = this.mImsPhone;
        if (phone != null) {
            return phone.isOutgoingImsVoiceAllowed();
        }
        return false;
    }

    public boolean isUtEnabled() {
        Phone phone = this.mImsPhone;
        if (phone != null) {
            return phone.isUtEnabled();
        }
        return false;
    }

    public int getSubId() {
        return this.mSubscriptionManagerService.getSubId(this.mPhoneId);
    }

    public int getPhoneId() {
        return this.mPhoneId;
    }

    public void setNetworkScanStarted(boolean z) {
        this.mIsNetworkScanStarted = z;
    }

    public boolean getNetworkScanStarted() {
        return this.mIsNetworkScanStarted;
    }

    public boolean setRoamingOverride(List<String> list, List<String> list2, List<String> list3, List<String> list4) {
        String iccSerialNumber = getIccSerialNumber();
        if (TextUtils.isEmpty(iccSerialNumber)) {
            return false;
        }
        setRoamingOverrideHelper(list, "gsm_roaming_list_", iccSerialNumber);
        setRoamingOverrideHelper(list2, "gsm_non_roaming_list_", iccSerialNumber);
        ServiceStateTracker serviceStateTracker = getServiceStateTracker();
        if (serviceStateTracker == null) {
            return true;
        }
        serviceStateTracker.pollState();
        return true;
    }

    private void setRoamingOverrideHelper(List list, String str, String str2) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        String str3 = str + str2;
        if (list == null || list.isEmpty()) {
            editorEdit.remove(str3).commit();
        } else {
            editorEdit.putStringSet(str3, new HashSet(list)).commit();
        }
    }

    public boolean isMccMncMarkedAsRoaming(String str) {
        return getRoamingOverrideHelper("gsm_roaming_list_", str);
    }

    public boolean isMccMncMarkedAsNonRoaming(String str) {
        return getRoamingOverrideHelper("gsm_non_roaming_list_", str);
    }

    public boolean isSidMarkedAsRoaming(int i) {
        return getRoamingOverrideHelper("cdma_roaming_list_", Integer.toString(i));
    }

    public boolean isSidMarkedAsNonRoaming(int i) {
        return getRoamingOverrideHelper("cdma_non_roaming_list_", Integer.toString(i));
    }

    public boolean isImsRegistered() {
        boolean zIsImsRegistered;
        Phone phone = this.mImsPhone;
        if (phone != null) {
            zIsImsRegistered = phone.isImsRegistered();
        } else {
            ServiceStateTracker serviceStateTracker = getServiceStateTracker();
            zIsImsRegistered = serviceStateTracker != null ? serviceStateTracker.isImsRegistered() : false;
        }
        Rlog.d(this.mLogTag, "isImsRegistered =" + zIsImsRegistered);
        return zIsImsRegistered;
    }

    public boolean isWifiCallingEnabled() {
        Phone phone = this.mImsPhone;
        boolean zIsWifiCallingEnabled = phone != null ? phone.isWifiCallingEnabled() : false;
        Rlog.d(this.mLogTag, "isWifiCallingEnabled =" + zIsWifiCallingEnabled);
        return zIsWifiCallingEnabled;
    }

    public boolean isImsCapabilityAvailable(int i, int i2) throws ImsException {
        Phone phone = this.mImsPhone;
        boolean zIsImsCapabilityAvailable = phone != null ? phone.isImsCapabilityAvailable(i, i2) : false;
        Rlog.d(this.mLogTag, "isImsCapabilityAvailable, capability=" + i + ", regTech=" + i2 + ", isAvailable=" + zIsImsCapabilityAvailable);
        return zIsImsCapabilityAvailable;
    }

    @Deprecated
    public boolean isVolteEnabled() {
        return isVoiceOverCellularImsEnabled();
    }

    public boolean isVoiceOverCellularImsEnabled() {
        Phone phone = this.mImsPhone;
        boolean zIsVoiceOverCellularImsEnabled = phone != null ? phone.isVoiceOverCellularImsEnabled() : false;
        Rlog.d(this.mLogTag, "isVoiceOverCellularImsEnabled=" + zIsVoiceOverCellularImsEnabled);
        return zIsVoiceOverCellularImsEnabled;
    }

    public int getImsRegistrationTech() {
        Phone phone = this.mImsPhone;
        int imsRegistrationTech = phone != null ? phone.getImsRegistrationTech() : -1;
        Rlog.d(this.mLogTag, "getImsRegistrationTechnology =" + imsRegistrationTech);
        return imsRegistrationTech;
    }

    public void getImsRegistrationTech(Consumer<Integer> consumer) {
        Phone phone = this.mImsPhone;
        if (phone != null) {
            phone.getImsRegistrationTech(consumer);
        } else {
            consumer.accept(-1);
        }
    }

    public void getImsRegistrationState(Consumer<Integer> consumer) {
        Phone phone = this.mImsPhone;
        if (phone != null) {
            phone.getImsRegistrationState(consumer);
        } else {
            consumer.accept(0);
        }
    }

    private boolean getRoamingOverrideHelper(String str, String str2) {
        String iccSerialNumber = getIccSerialNumber();
        if (TextUtils.isEmpty(iccSerialNumber) || TextUtils.isEmpty(str2)) {
            return false;
        }
        Set<String> stringSet = PreferenceManager.getDefaultSharedPreferences(this.mContext).getStringSet(str + iccSerialNumber, null);
        if (stringSet == null) {
            return false;
        }
        return stringSet.contains(str2);
    }

    public int getRadioPowerState() {
        return this.mCi.getRadioState();
    }

    public boolean isRadioAvailable() {
        return this.mCi.getRadioState() != 2;
    }

    public boolean isRadioOn() {
        return this.mCi.getRadioState() == 1;
    }

    public void shutdownRadio() {
        getServiceStateTracker().requestShutdown();
    }

    public boolean isShuttingDown() {
        return getServiceStateTracker().isDeviceShuttingDown();
    }

    public void setRadioCapability(RadioCapability radioCapability, Message message) {
        this.mCi.setRadioCapability(radioCapability, message);
    }

    public int getRadioAccessFamily() {
        RadioCapability radioCapability = getRadioCapability();
        if (radioCapability == null) {
            return 0;
        }
        return radioCapability.getRadioAccessFamily();
    }

    public String getModemUuId() {
        RadioCapability radioCapability = getRadioCapability();
        return radioCapability == null ? "" : radioCapability.getLogicalModemUuid();
    }

    public RadioCapability getRadioCapability() {
        return this.mRadioCapability.get();
    }

    public void radioCapabilityUpdated(RadioCapability radioCapability, boolean z) {
        this.mRadioCapability.set(radioCapability);
        if (SubscriptionManager.isValidSubscriptionId(getSubId())) {
            sendSubscriptionSettings(!this.mContext.getResources().getBoolean(17892131));
        }
        if (z) {
            this.mCi.getImei(obtainMessage(67));
            this.mCi.getDeviceIdentity(obtainMessage(21));
        }
    }

    public void sendSubscriptionSettings(boolean z) {
        if (this.mIsAllowedNetworkTypesLoadedFromDb) {
            MiuiPhoneStub.get(this.mPhoneId).updateAllowedNetworkTypesFromDb(this);
        }
        if (z) {
            restoreSavedNetworkSelection(null);
        }
        updateUsageSetting();
    }

    private int getResolvedUsageSetting(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
        SubscriptionInfo subscriptionInfo = subscriptionInfoInternal != null ? subscriptionInfoInternal.toSubscriptionInfo() : null;
        if (subscriptionInfo == null) {
            loge("Failed to get SubscriptionInfo for subId=" + i);
            return -1;
        }
        if (subscriptionInfo.getUsageSetting() == -1) {
            return -1;
        }
        if (subscriptionInfo.getUsageSetting() != 0) {
            return subscriptionInfo.getUsageSetting();
        }
        if (subscriptionInfo.isOpportunistic()) {
            return 2;
        }
        return this.mContext.getResources().getInteger(R.integer.config_drawLockTimeoutMillis);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean updateUsageSetting() {
        if (!this.mIsUsageSettingSupported) {
            return false;
        }
        int subId = getSubId();
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            return false;
        }
        int i = this.mPreferredUsageSetting;
        int resolvedUsageSetting = getResolvedUsageSetting(subId);
        this.mPreferredUsageSetting = resolvedUsageSetting;
        if (resolvedUsageSetting == -1) {
            loge("Usage Setting is Supported but Preferred Setting Unknown!");
            return false;
        }
        if (resolvedUsageSetting == i) {
            return false;
        }
        String str = "mPreferredUsageSetting=" + this.mPreferredUsageSetting + ", lastPreferredUsageSetting=" + i + ", mUsageSettingFromModem=" + this.mUsageSettingFromModem;
        logd(str);
        this.mLocalLog.log(str);
        int i2 = this.mUsageSettingFromModem;
        if (i2 == -1) {
            this.mCi.getUsageSetting(obtainMessage(63));
            return true;
        }
        int i3 = this.mPreferredUsageSetting;
        if (i3 == i2) {
            return true;
        }
        this.mCi.setUsageSetting(obtainMessage(64, i3, 0), this.mPreferredUsageSetting);
        return true;
    }

    public void registerForRadioCapabilityChanged(Handler handler, int i, Object obj) {
        this.mCi.registerForRadioCapabilityChanged(handler, i, obj);
    }

    public void unregisterForRadioCapabilityChanged(Handler handler) {
        this.mCi.unregisterForRadioCapabilityChanged(this);
    }

    public boolean isImsAvailable() {
        Phone phone = this.mImsPhone;
        if (phone == null) {
            return false;
        }
        return phone.isImsAvailable();
    }

    public boolean isVideoEnabled() {
        Phone phone = this.mImsPhone;
        if (phone != null) {
            return phone.isVideoEnabled();
        }
        return false;
    }

    public void getModemActivityInfo(Message message, WorkSource workSource) {
        this.mCi.getModemActivityInfo(message, workSource);
    }

    public void setDataThrottling(Message message, WorkSource workSource, int i, long j) {
        this.mCi.setDataThrottling(message, workSource, i, j);
    }

    public void setAllowedCarriers(CarrierRestrictionRules carrierRestrictionRules, Message message, WorkSource workSource) {
        this.mCi.setAllowedCarriers(carrierRestrictionRules, message, workSource);
    }

    public void getAllowedCarriers(Message message, WorkSource workSource) {
        this.mCi.getAllowedCarriers(message, workSource);
    }

    public Locale getLocaleFromSimAndCarrierPrefs() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null && iccRecords.getSimLanguage() != null) {
            return new Locale(iccRecords.getSimLanguage());
        }
        return getLocaleFromCarrierProperties();
    }

    protected boolean isMatchGid(String str) {
        String groupIdLevel1 = getGroupIdLevel1();
        int length = str.length();
        return !TextUtils.isEmpty(groupIdLevel1) && groupIdLevel1.length() >= length && groupIdLevel1.substring(0, length).equalsIgnoreCase(str);
    }

    public static void checkWfcWifiOnlyModeBeforeDial(Phone phone, int i, Context context) throws CallStateException {
        if (phone == null || !phone.isWifiCallingEnabled()) {
            ImsManager imsManager = ImsManager.getInstance(context, i);
            if (imsManager.isWfcEnabledByPlatform() && imsManager.isWfcEnabledByUser() && imsManager.getWfcMode() == 0) {
                Phone$$ExternalSyntheticBUOutline2.m(1, "WFC Wi-Fi Only Mode: IMS not registered");
            }
        }
    }

    public boolean isImsServiceSimultaneousCallingSupportCapable(Context context) {
        ImsManager imsManager = ImsManager.getInstance(context, this.mPhoneId);
        if (imsManager == null) {
            return false;
        }
        try {
            return imsManager.isCapable(8L);
        } catch (ImsException e) {
            loge("initializeTerminalBasedCallWaiting : exception " + e);
            return false;
        }
    }

    public AppSmsManager getAppSmsManager() {
        return this.mAppSmsManager;
    }

    public void setSimPowerState(int i, Message message, WorkSource workSource) {
        this.mCi.setSimCardPower(i, message, workSource);
    }

    public void setVoNrEnabled(boolean z, Message message, WorkSource workSource) {
        this.mCi.setVoNrEnabled(z, message, workSource);
        setAllowedImsServicesForAny(3, z);
    }

    public void isVoNrEnabled(Message message, WorkSource workSource) {
        this.mCi.isVoNrEnabled(message, workSource);
    }

    public static boolean isEmergencyCallOnly() {
        boolean zIsEmergencyOnly = false;
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone != null) {
                ServiceStateTracker serviceStateTracker = phone.getServiceStateTracker();
                ServiceState serviceState = serviceStateTracker.getServiceState();
                if (serviceStateTracker.getCombinedRegState(serviceState) == 0) {
                    return false;
                }
                zIsEmergencyOnly |= serviceState.isEmergencyOnly();
            }
        }
        return zIsEmergencyOnly;
    }

    @Deprecated
    public HalVersion getHalVersion() {
        return getHalVersion(0);
    }

    public HalVersion getHalVersion(int i) {
        CommandsInterface commandsInterface = this.mCi;
        if (commandsInterface != null && (commandsInterface instanceof RIL)) {
            return ((RIL) commandsInterface).getHalVersion(i);
        }
        return RIL.RADIO_HAL_VERSION_UNKNOWN;
    }

    public String getOperatorNumeric() {
        return "";
    }

    public VoiceCallSessionStats getVoiceCallSessionStats() {
        return this.mVoiceCallSessionStats;
    }

    public void setVoiceCallSessionStats(VoiceCallSessionStats voiceCallSessionStats) {
        this.mVoiceCallSessionStats = voiceCallSessionStats;
    }

    public SmsStats getSmsStats() {
        return this.mSmsStats;
    }

    public void setSmsStats(SmsStats smsStats) {
        this.mSmsStats = smsStats;
    }

    public TelephonyAnalytics getTelephonyAnalytics() {
        return this.mTelephonyAnalytics;
    }

    public boolean isDeviceIdle() {
        DeviceStateMonitor deviceStateMonitor = getDeviceStateMonitor();
        if (deviceStateMonitor == null) {
            Rlog.e(this.mLogTag, "isDeviceIdle: DeviceStateMonitor is null");
            return false;
        }
        return !deviceStateMonitor.shouldEnableHighPowerConsumptionIndications();
    }

    public void notifyDeviceIdleStateChanged(boolean z) {
        SignalStrengthController signalStrengthController = getSignalStrengthController();
        if (signalStrengthController == null) {
            Rlog.e(this.mLogTag, "notifyDeviceIdleStateChanged: SignalStrengthController is null");
        } else {
            signalStrengthController.onDeviceIdleStateChanged(z);
        }
    }

    public List<String> getEquivalentHomePlmns() {
        return Collections.EMPTY_LIST;
    }

    public List<String> getDataServicePackages() {
        return Collections.EMPTY_LIST;
    }

    public LinkBandwidthEstimator getLinkBandwidthEstimator() {
        return this.mLinkBandwidthEstimator;
    }

    public void getSlicingConfig(Message message) {
        this.mCi.getSlicingConfig(message);
    }

    public DataNetworkController getDataNetworkController() {
        return this.mDataNetworkController;
    }

    public DataSettingsManager getDataSettingsManager() {
        return this.mDataNetworkController.getDataSettingsManager();
    }

    public boolean isAllowedNetworkTypesLoadedFromDb() {
        return this.mIsAllowedNetworkTypesLoadedFromDb;
    }

    public void startImsTraffic(int i, int i2, int i3, int i4, Message message) {
        this.mCi.startImsTraffic(i, i2, i3, i4, message);
    }

    public void stopImsTraffic(int i, Message message) {
        this.mCi.stopImsTraffic(i, message);
    }

    public void registerForConnectionSetupFailure(Handler handler, int i, Object obj) {
        this.mCi.registerForConnectionSetupFailure(handler, i, obj);
    }

    public void unregisterForConnectionSetupFailure(Handler handler) {
        this.mCi.unregisterForConnectionSetupFailure(handler);
    }

    public void triggerEpsFallback(int i, Message message) {
        this.mCi.triggerEpsFallback(i, message);
    }

    public void setEmergencyMode(int i, Message message) {
        this.mCi.setEmergencyMode(i, message);
    }

    public void triggerEmergencyNetworkScan(int[] iArr, int i, Message message) {
        this.mCi.triggerEmergencyNetworkScan(iArr, i, message);
    }

    public void cancelEmergencyNetworkScan(boolean z, Message message) {
        this.mCi.cancelEmergencyNetworkScan(z, message);
    }

    public void exitEmergencyMode(Message message) {
        this.mCi.exitEmergencyMode(message);
    }

    public void registerForEmergencyNetworkScan(Handler handler, int i, Object obj) {
        this.mCi.registerForEmergencyNetworkScan(handler, i, obj);
    }

    public void unregisterForEmergencyNetworkScan(Handler handler) {
        this.mCi.unregisterForEmergencyNetworkScan(handler);
    }

    public void triggerImsDeregistration(int i) {
        Phone phone = this.mImsPhone;
        if (phone != null) {
            phone.triggerImsDeregistration(i);
        }
    }

    public TelephonyTester getTelephonyTester() {
        return this.mTelephonyTester;
    }

    public UserHandle getUserHandle() {
        int subId = getSubId();
        try {
            SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
            if (subscriptionManager != null) {
                return subscriptionManager.getSubscriptionUserHandle(subId);
            }
            return null;
        } catch (IllegalArgumentException e) {
            loge("getUserHandle: ex=" + e);
            return null;
        }
    }

    public boolean isManagedProfile() {
        UserHandle userHandle = getUserHandle();
        UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
        if (userHandle == null || userManager == null) {
            return false;
        }
        return userManager.isManagedProfile(userHandle.getIdentifier());
    }

    public boolean getNullCipherAndIntegrityEnabledPreference() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(PREF_NULL_CIPHER_AND_INTEGRITY_ENABLED, true);
    }

    public boolean getIdentifierDisclosureNotificationsPreferenceEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(PREF_IDENTIFIER_DISCLOSURE_NOTIFICATIONS_ENABLED, false);
    }

    public int[] getSupportedNetworkAlertCategories() {
        return new int[0];
    }

    public boolean getNullCipherNotificationsPreferenceEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(PREF_NULL_CIPHER_NOTIFICATIONS_ENABLED, false);
    }

    public void updateImsCallStatus(List<ImsCallInfo> list, Message message) {
        this.mCi.updateImsCallStatus(list, message);
    }

    public void setN1ModeEnabled(boolean z, Message message) {
        this.mCi.setN1ModeEnabled(z, message);
    }

    public void isN1ModeEnabled(Message message) {
        this.mCi.isN1ModeEnabled(message);
    }

    public List<CellBroadcastIdRange> getCellBroadcastIdRanges() {
        return new ArrayList();
    }

    public void setCellBroadcastIdRanges(List<CellBroadcastIdRange> list, Consumer<Integer> consumer) {
        consumer.accept(1);
    }

    public void startEmergencyCallbackMode(int i, long j) {
        Rlog.d(this.mLogTag, "startEmergencyCallbackMode:type=" + i);
        this.mNotifier.notifyCallbackModeStarted(this, i, j);
    }

    public void restartEmergencyCallbackMode(int i, long j) {
        Rlog.d(this.mLogTag, "restartEmergencyCallbackMode:type=" + i);
        this.mNotifier.notifyCallbackModeRestarted(this, i, j);
    }

    public void stopEmergencyCallbackMode(int i, int i2) {
        Rlog.d(this.mLogTag, "stopEmergencyCallbackMode:type=" + i + ", reason=" + i2);
        this.mNotifier.notifyCallbackModeStopped(this, i, i2);
    }

    protected boolean isActiveSubId(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(i);
        return subscriptionInfoInternal != null && subscriptionInfoInternal.isActive();
    }

    public void notifyCarrierRoamingNtnModeChanged(boolean z) {
        logd("notifyCarrierRoamingNtnModeChanged active:" + z);
        this.mNotifier.notifyCarrierRoamingNtnModeChanged(this, z);
    }

    public void notifyCarrierRoamingNtnEligibleStateChanged(boolean z) {
        logd("notifyCarrierRoamingNtnEligibleStateChanged eligible:" + z);
        this.mNotifier.notifyCarrierRoamingNtnEligibleStateChanged(this, z);
        ControllerMetricsStats.getInstance().reportP2PSmsEligibilityNotificationsCount(z);
    }

    public void notifyCarrierRoamingNtnAvailableServicesChanged(int[] iArr) {
        logd("notifyCarrierRoamingNtnAvailableServicesChanged availableServices:" + Arrays.toString(iArr));
        this.mNotifier.notifyCarrierRoamingNtnAvailableServicesChanged(this, iArr);
    }

    public void notifyCarrierRoamingNtnSignalStrengthChanged(NtnSignalStrength ntnSignalStrength) {
        logd("notifyCarrierRoamingNtnSignalStrengthChanged: ntnSignalStrength=" + ntnSignalStrength.getLevel());
        this.mNotifier.notifyCarrierRoamingNtnSignalStrengthChanged(this, ntnSignalStrength);
    }

    public void notifySatellitePurchaseModeChanged(boolean z, int i) {
        logd("notifySatellitePurchaseModeChanged inEnabled:" + z + " purchaseModeState:" + i);
        this.mNotifier.notifySatellitePurchaseModeChanged(this, z, i);
    }

    public void setSatellitePlmn(int i, List<String> list, List<String> list2, Message message) {
        logd("setSatellitePlmn: simSlot=" + i + " carrierPlmnList=" + list.toString() + " allSatellitePlmnList=" + list2.toString());
        this.mCi.setSatellitePlmn(i, list, list2, message);
    }

    public void setSatelliteNetworkInfo(int i, SatelliteNetworkInfo satelliteNetworkInfo, Message message) {
        logd("setSatelliteNetworkInfo: simSlot=" + i + " satelliteNetworkInfo=" + satelliteNetworkInfo);
        this.mCi.setSatelliteNetworkInfo(i, satelliteNetworkInfo, message);
    }

    public void enablePrioritizedNetworkScan(int i, PrioritizedNetworkScanRequest prioritizedNetworkScanRequest, Message message) {
        logd("enablePrioritizedNetworkScan: simSlot=" + i + " prioritizedNetworkScanRequest=" + prioritizedNetworkScanRequest.toString());
        this.mCi.enablePrioritizedNetworkScan(i, prioritizedNetworkScanRequest, message);
    }

    public void disablePrioritizedNetworkScan(int i, Message message) {
        logd("disablePrioritizedNetworkScan: simSlot=" + i);
        this.mCi.disablePrioritizedNetworkScan(i, message);
    }

    public void setSatelliteEnabledForCarrier(int i, boolean z, Message message) {
        logd("setSatelliteEnabledForCarrier: simSlot=" + i + " satelliteEnabled=" + z);
        this.mCi.setSatelliteEnabledForCarrier(i, z, message);
    }

    public void isSatelliteEnabledForCarrier(int i, Message message) {
        this.mCi.isSatelliteEnabledForCarrier(i, message);
    }

    public void setAllowedImsServicesForHomeOnly(int i, boolean z) {
        HashSet hashSet = new HashSet(this.mAllowedImsServicesAny);
        HashSet hashSet2 = new HashSet(this.mAllowedImsServicesHomeOnly);
        Set set = this.mAllowedImsServicesHomeOnly;
        if (z) {
            set.add(Integer.valueOf(i));
        } else {
            set.remove(Integer.valueOf(i));
        }
        this.mAllowedImsServicesAny.remove(Integer.valueOf(i));
        if (hashSet.equals(this.mAllowedImsServicesAny) && hashSet2.equals(this.mAllowedImsServicesHomeOnly)) {
            return;
        }
        this.mCi.updateAllowedImsServices(this.mAllowedImsServicesAny, this.mAllowedImsServicesHomeOnly, null);
    }

    public void setAllowedImsServicesForAny(int i, boolean z) {
        HashSet hashSet = new HashSet(this.mAllowedImsServicesAny);
        HashSet hashSet2 = new HashSet(this.mAllowedImsServicesHomeOnly);
        Set set = this.mAllowedImsServicesAny;
        if (z) {
            set.add(Integer.valueOf(i));
        } else {
            set.remove(Integer.valueOf(i));
        }
        this.mAllowedImsServicesHomeOnly.remove(Integer.valueOf(i));
        if (hashSet.equals(this.mAllowedImsServicesAny) && hashSet2.equals(this.mAllowedImsServicesHomeOnly)) {
            return;
        }
        this.mCi.updateAllowedImsServices(this.mAllowedImsServicesAny, this.mAllowedImsServicesHomeOnly, null);
    }

    public void clearAllowedImsServices() {
        Rlog.d(this.mLogTag, "clearAllowedImsServices");
        this.mAllowedImsServicesAny.clear();
        this.mAllowedImsServicesHomeOnly.clear();
    }

    public void notifyDomainSelectionEmergencyModeEntered(int i) {
        logd("notifyDomainSelectionEmergencyModeEntered: type=" + i);
        this.mNotifier.notifyDomainSelectionEmergencyModeEntered(this, i);
    }

    public void notifyDomainSelectionEmergencyModeExited(int i) {
        logd("notifyDomainSelectionEmergencyModeExited: type=" + i);
        this.mNotifier.notifyDomainSelectionEmergencyModeExited(this, i);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("Phone: subId=" + getSubId());
        printWriter.println(" mPhoneId=" + this.mPhoneId);
        printWriter.println(" mCi=" + this.mCi);
        printWriter.println(" mDoesRilSendMultipleCallRing=" + this.mDoesRilSendMultipleCallRing);
        printWriter.println(" mCallRingContinueToken=" + this.mCallRingContinueToken);
        printWriter.println(" mCallRingDelay=" + this.mCallRingDelay);
        printWriter.println(" mIsVoiceCapable=" + this.mIsVoiceCapable);
        printWriter.println(" mIccRecords=" + this.mIccRecords.get());
        printWriter.println(" mUiccApplication=" + this.mUiccApplication.get());
        printWriter.println(" mSmsStorageMonitor=" + this.mSmsStorageMonitor);
        printWriter.println(" mSmsUsageMonitor=" + this.mSmsUsageMonitor);
        printWriter.flush();
        printWriter.println(" mLooper=" + this.mLooper);
        printWriter.println(" mContext=" + this.mContext);
        printWriter.println(" mNotifier=" + this.mNotifier);
        printWriter.println(" mSimulatedRadioControl=" + this.mSimulatedRadioControl);
        printWriter.println(" mUnitTestMode=" + this.mUnitTestMode);
        printWriter.println(" getUnitTestMode()=" + getUnitTestMode());
        printWriter.println(" getState()=" + getState());
        printWriter.println(" getIccSerialNumber()=" + TelephonyCommonUtilsStub.get().pii(getIccSerialNumber()));
        printWriter.println(" getIccRecordsLoaded()=" + getIccRecordsLoaded());
        printWriter.println(" getMessageWaitingIndicator()=" + getMessageWaitingIndicator());
        printWriter.println(" getCallForwardingIndicator()=" + getCallForwardingIndicator());
        printWriter.println(" isInEmergencyCall()=" + isInEmergencyCall());
        printWriter.flush();
        printWriter.println(" isInEcm()=" + isInEcm());
        printWriter.println(" getPhoneName()=" + getPhoneName());
        printWriter.println(" getPhoneType()=" + getPhoneType());
        printWriter.println(" getVoiceMessageCount()=" + getVoiceMessageCount());
        printWriter.println(" isInEmergencySmsMode=" + isInEmergencySmsMode());
        printWriter.println(" isEcmCanceledForEmergency=" + isEcmCanceledForEmergency());
        printWriter.println(" service state=" + getServiceState());
        printWriter.flush();
        printWriter.println("++++++++++++++++++++++++++++++++");
        Phone phone = this.mImsPhone;
        if (phone != null) {
            try {
                phone.dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        DataNetworkController dataNetworkController = this.mDataNetworkController;
        if (dataNetworkController != null) {
            try {
                dataNetworkController.dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        if (getServiceStateTracker() != null) {
            try {
                getServiceStateTracker().dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        if (getEmergencyNumberTracker() != null) {
            try {
                getEmergencyNumberTracker().dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        if (getDisplayInfoController() != null) {
            try {
                getDisplayInfoController().dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e5) {
                e5.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        CarrierResolver carrierResolver = this.mCarrierResolver;
        if (carrierResolver != null) {
            try {
                carrierResolver.dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e6) {
                e6.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        CarrierActionAgent carrierActionAgent = this.mCarrierActionAgent;
        if (carrierActionAgent != null) {
            try {
                carrierActionAgent.dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e7) {
                e7.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        CarrierSignalAgent carrierSignalAgent = this.mCarrierSignalAgent;
        if (carrierSignalAgent != null) {
            try {
                carrierSignalAgent.dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e8) {
                e8.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        if (getCallTracker() != null) {
            try {
                getCallTracker().dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e9) {
                e9.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        SimActivationTracker simActivationTracker = this.mSimActivationTracker;
        if (simActivationTracker != null) {
            try {
                simActivationTracker.dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e10) {
                e10.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        if (this.mDeviceStateMonitor != null) {
            printWriter.println("DeviceStateMonitor:");
            this.mDeviceStateMonitor.dump(fileDescriptor, printWriter, strArr);
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        if (this.mSignalStrengthController != null) {
            printWriter.println("SignalStrengthController:");
            this.mSignalStrengthController.dump(fileDescriptor, printWriter, strArr);
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        AccessNetworksManager accessNetworksManager = this.mAccessNetworksManager;
        if (accessNetworksManager != null) {
            accessNetworksManager.dump(fileDescriptor, printWriter, strArr);
        }
        CommandsInterface commandsInterface = this.mCi;
        if (commandsInterface != null && (commandsInterface instanceof RIL)) {
            try {
                ((RIL) commandsInterface).dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e11) {
                e11.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        if (getCarrierPrivilegesTracker() != null) {
            printWriter.println("CarrierPrivilegesTracker:");
            getCarrierPrivilegesTracker().dump(fileDescriptor, printWriter, strArr);
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        if (getLinkBandwidthEstimator() != null) {
            printWriter.println("LinkBandwidthEstimator:");
            getLinkBandwidthEstimator().dump(fileDescriptor, printWriter, strArr);
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        printWriter.println("Phone Local Log: ");
        LocalLog localLog = this.mLocalLog;
        if (localLog != null) {
            try {
                localLog.dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e12) {
                e12.printStackTrace();
            }
            printWriter.flush();
            printWriter.println("++++++++++++++++++++++++++++++++");
        }
        TelephonyAnalytics telephonyAnalytics = this.mTelephonyAnalytics;
        if (telephonyAnalytics != null) {
            try {
                telephonyAnalytics.dump(fileDescriptor, printWriter, strArr);
            } catch (Exception e13) {
                e13.printStackTrace();
            }
        }
    }

    private void logd(String str) {
        Rlog.d(this.mLogTag, "[" + this.mPhoneId + "] " + str);
    }

    private void logl(String str) {
        this.mLocalLog.log(str);
        Log.d(this.mLogTag, str);
    }

    private void loge(String str) {
        Rlog.e(this.mLogTag, "[" + this.mPhoneId + "] " + str);
    }

    public boolean isEmergencyNumber(String str) {
        return PhoneNumberUtils.isEmergencyNumber(getSubId(), str);
    }

    public void setOnScbmExitResponse(Handler handler, int i, Object obj) {
        new Registrant(handler, i, obj).notifyRegistrant();
    }

    public void invokeOemRilRequestRaw(byte[] bArr, Message message) {
        if (message != null) {
            AsyncResult.forMessage(message).exception = new RuntimeException("not supported: invokeOemRilRequestRaw");
            message.sendToTarget();
        }
    }

    public void invokeOemRilRequestStrings(String[] strArr, Message message) {
        if (message != null) {
            AsyncResult.forMessage(message).exception = new RuntimeException("not supported: invokeOemRilRequestStrings");
            message.sendToTarget();
        }
    }
}
