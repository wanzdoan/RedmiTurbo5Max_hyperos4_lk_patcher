package com.android.internal.telephony.imsphone;

import android.R;
import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.NetworkStats;
import android.net.netstats.provider.NetworkStatsProvider;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.telephony.CallQuality;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyLocalConnection;
import android.telephony.TelephonyManager;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsCallSession;
import android.telephony.ims.ImsConferenceState;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsSuppServiceNotification;
import android.telephony.ims.MediaQualityStatus;
import android.telephony.ims.MediaThreshold;
import android.telephony.ims.ProvisioningManager;
import android.telephony.ims.RtpHeaderExtension;
import android.telephony.ims.SrvccCall;
import android.telephony.ims.aidl.IImsCallSessionListener;
import android.telephony.ims.aidl.IImsTrafficSessionCallback;
import android.telephony.ims.aidl.ISrvccStartedCallback;
import android.telephony.ims.feature.ConnectionFailureInfo;
import android.telephony.ims.feature.ImsFeature;
import android.telephony.ims.feature.MmTelFeature;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsCall;
import com.android.ims.ImsEcbm;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.ims.ImsUtInterface;
import com.android.ims.internal.ConferenceParticipant;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsVideoCallProvider;
import com.android.ims.internal.ImsVideoCallProviderWrapper;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.CallManager$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CallTracker;
import com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.GsmCdmaCallTracker$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperCallManager;
import com.android.internal.telephony.IHyperSsManager;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline2;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.SrvccConnection;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.d2d.RtpTransport;
import com.android.internal.telephony.data.DataSettingsManager;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.LinkPropertiesUtils;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class ImsPhoneCallTracker extends CallTracker implements ImsPullCall {
    protected static final boolean DBG = true;
    protected static final int EVENT_CHECK_FOR_WIFI_HANDOVER = 25;
    protected static final int EVENT_DATA_ENABLED_CHANGED = 23;
    protected static final int EVENT_DIAL_PENDINGMO = 20;
    protected static final int EVENT_HANGUP_PENDINGMO = 18;
    protected static final int EVENT_ON_FEATURE_CAPABILITY_CHANGED = 26;
    protected static final int EVENT_RESUME_NOW_FOREGROUND_CALL = 31;
    protected static final int HANDOVER_TO_WIFI_TIMEOUT_MS = 60000;
    private static final SparseIntArray PRECISE_CAUSE_MAP;
    protected static final int TIMEOUT_HANGUP_PENDINGMO = 500;
    protected boolean mAllowAddCallDuringVideoCall;
    protected boolean mAllowEmergencyVideoCalls;
    private boolean mAllowHoldingCall;
    protected boolean mAllowHoldingVideoCall;
    private boolean mAllowRttWhileRoaming;
    private boolean mAlwaysPlayRemoteHoldTone;
    private boolean mAutoRetryFailedWifiEmergencyCall;
    public ImsPhoneCall mBackgroundCall;
    protected ImsCall mCallExpectedToAccept;
    protected ImsCall mCallExpectedToHold;
    protected ImsCall mCallExpectedToResume;
    protected CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private Pair mCarrierConfigForSubId;
    private boolean mCarrierConfigLoadedForSubscription;
    private CarrierRoamingNtnListener mCarrierRoamingNtnListener;
    protected int mClirMode;
    private Config mConfig;
    private final ProvisioningManager.Callback mConfigCallback;
    protected ArrayList<ImsPhoneConnection> mConnections;
    private final ConnectorFactory mConnectorFactory;
    private Runnable mConnectorRunnable;
    private Optional mCurrentlyConnectedSubId;
    private final AtomicInteger mDefaultDialerUid;
    private boolean mDesiredMute;
    private boolean mDeviceToDeviceForceEnabled;
    private boolean mDropVideoCallWhenAnsweringAudioCall;
    private Executor mExecutor;
    public ImsPhoneCall mForegroundCall;
    public ImsPhoneCall mHandoverCall;
    private boolean mHasAttemptedStartOfCallHandover;
    protected HoldSwapState mHoldSwitchingState;
    private boolean mIgnoreDataEnabledChangedForVideoCalls;
    protected final ImsCallInfoTracker mImsCallInfoTracker;
    protected ImsCall.Listener mImsCallListener;
    private SparseBooleanArray mImsCapability;
    private final ImsMmTelManager.CapabilityCallback mImsCapabilityCallback;
    protected ImsManager mImsManager;
    protected final FeatureConnector<ImsManager> mImsManagerConnector;
    private Map mImsReasonCodeMap;
    private final ImsManager.ImsStatsCallback mImsStatsCallback;
    private final ConcurrentHashMap mImsTrafficSessions;
    protected ImsCall.Listener mImsUssdListener;
    private boolean mIsConferenceEventPackageEnabled;
    protected boolean mIsDataEnabled;
    private boolean mIsInEmergencyCall;
    private boolean mIsMonitoringConnectivity;
    protected boolean mIsViLteDataMetered;
    protected ImsPhone.ImsDialArgs mLastDialArgs;
    protected String mLastDialString;
    private MediaThreshold mMediaThreshold;
    private MmTelFeature.MmTelCapabilities mMmTelCapabilities;
    private final MmTelFeatureListener mMmTelFeatureListener;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private boolean mNotifyHandoverVideoFromLTEToWifi;
    protected boolean mNotifyHandoverVideoFromWifiToLTE;
    protected boolean mNotifyVtHandoverToWifiFail;
    protected int mOnHoldToneId;
    protected boolean mOnHoldToneStarted;
    protected final LocalLog mOperationLocalLog;
    protected int mPendingCallVideoState;
    protected Bundle mPendingIntentExtras;
    protected ImsPhoneConnection mPendingMO;
    private Pair mPendingSilentRedialInfo;
    protected Message mPendingUssd;
    public ImsPhone mPhone;
    private final Map mPhoneNumAndConnTime;
    private List mPhoneStateListeners;
    private final ConcurrentLinkedQueue mProvisioningItemQueue;
    protected final BroadcastReceiver mReceiver;
    private final LocalLog mRegLocalLog;
    public ImsPhoneCall mRingingCall;
    protected DataSettingsManager.DataSettingsManagerCallback mSettingsCallback;
    private SharedPreferenceProxy mSharedPreferenceProxy;
    private boolean mShouldAllowVtCallsInLowBattery;
    protected boolean mShouldUpdateImsConfigOnDisconnect;
    private final SrvccStartedCallback mSrvccStartedCallback;
    protected Call.SrvccState mSrvccState;
    private final List mSrvccTypeSupported;
    protected PhoneConstants.State mState;
    private boolean mSupportCepOnPeer;
    private boolean mSupportD2DUsingRtp;
    private boolean mSupportDowngradeVtToAudio;
    private boolean mSupportPauseVideo;
    private boolean mSupportSdpForRtpHeaderExtensions;
    protected Object mSyncHold;
    private TelephonyManager mTelephonyManager;
    private long mThresholdRtpInactivityTime;
    private int mThresholdRtpJitter;
    private int mThresholdRtpPacketLoss;
    private boolean mTreatDowngradedVideoCallsAsVideoCalls;
    private final Queue mUnknownPeerConnTime;
    private int mUssdMethod;
    protected ImsCall mUssdSession;
    protected ImsUtInterface mUtInterface;
    private RegistrantList mVoiceCallEndedRegistrants;
    private RegistrantList mVoiceCallStartedRegistrants;
    private boolean mVolteRoamingSupported;
    private final HashMap mVtDataUsageMap;
    private final VtDataUsageProvider mVtDataUsageProvider;
    private volatile NetworkStats mVtDataUsageSnapshot;
    private volatile NetworkStats mVtDataUsageUidSnapshot;
    protected int pendingCallClirMode;
    protected boolean pendingCallInEcm;
    private static final boolean VERBOSE_STATE_LOGGING = Rlog.isLoggable("IPCTState", 2);
    private static final int TIMEOUT_REDIAL_WIFI_E911_MS = LinkPropertiesUtils.MAX_MTU;

    public static class Config {
        public boolean isD2DCommunicationSupported;
    }

    public interface ConnectorFactory {
        FeatureConnector<ImsManager> create(Context context, int i, String str, FeatureConnector.Listener<ImsManager> listener, Executor executor);
    }

    protected enum HoldSwapState {
        INACTIVE,
        PENDING_SINGLE_CALL_HOLD,
        PENDING_SINGLE_CALL_UNHOLD,
        SWAPPING_ACTIVE_AND_HELD,
        HOLDING_TO_ANSWER_INCOMING,
        PENDING_RESUME_FOREGROUND_AFTER_FAILURE,
        HOLDING_TO_DIAL_OUTGOING,
        ENDING_TO_DIAL_OUTGOING,
        PENDING_RESUME_FOREGROUND_AFTER_HOLD
    }

    public interface PhoneStateListener {
        void onPhoneStateChanged(PhoneConstants.State state, PhoneConstants.State state2);
    }

    public interface SharedPreferenceProxy {
        SharedPreferences getDefaultSharedPreferences(Context context);
    }

    private static boolean isAlive(int i) {
        if (i != 9) {
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    protected void checkforCsfb() throws CallStateException {
    }

    protected void checkforPendingResume() throws CallStateException {
    }

    protected void doUnregisterWhenDispose() {
    }

    protected int getHangupReasionInfo(int i, boolean z) {
        return z ? NtnRILConstants.MI_NTN_OEM_ERROR_4 : NtnRILConstants.MI_NTN_OEM_ERROR_1;
    }

    @Override // com.android.internal.telephony.CallTracker
    protected void handlePollCalls(AsyncResult asyncResult) {
    }

    protected boolean ignoreCarrierPauseSupport() {
        return false;
    }

    protected boolean ignoreClirWhenEcc() {
        return true;
    }

    protected boolean ignoreConference(ImsCall imsCall, ImsCall imsCall2) {
        return false;
    }

    public boolean ignoreSwitchCallToBackground() {
        return true;
    }

    protected boolean isCarrierPauseAllowed(ImsCall imsCall) {
        return true;
    }

    protected boolean isRoamingOnAndRoamingSettingOff() {
        return false;
    }

    protected void logDebugMessagesWithDumpFormat(String str, ImsPhoneConnection imsPhoneConnection, String str2) {
    }

    protected void logDebugMessagesWithOpFormat(String str, String str2, ImsPhoneConnection imsPhoneConnection, String str3) {
    }

    protected void notifyPhoneStateInSomeCase() {
    }

    protected void releasePendingMOIfRequired() {
    }

    protected void removeAllConnectionsByCall(ImsPhoneCall imsPhoneCall) {
    }

    protected void resetHoldToneWhenHandover(ImsPhoneCall imsPhoneCall) {
    }

    protected void resetRingBackTone(ImsPhoneCall imsPhoneCall) {
    }

    protected void setMultiPartyState(Connection connection) {
    }

    protected void setRedialAsEcc(int i) {
    }

    protected int shouldRedialAsECCCall(int i) {
        return i;
    }

    protected void switchWfcModeIfRequired(ImsManager imsManager, boolean z, boolean z2) {
    }

    protected int updateDisconnectCause(int i, ImsPhoneConnection imsPhoneConnection) {
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ImsTrafficSession {
        private final IImsTrafficSessionCallback mCallback;
        private final int mTrafficDirection;
        private final int mTrafficType;

        ImsTrafficSession(int i, int i2, IImsTrafficSessionCallback iImsTrafficSessionCallback) {
            this.mTrafficType = i;
            this.mTrafficDirection = i2;
            this.mCallback = iImsTrafficSessionCallback;
        }
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        PRECISE_CAUSE_MAP = sparseIntArray;
        sparseIntArray.append(101, CallFailCause.LOCAL_ILLEGAL_ARGUMENT);
        sparseIntArray.append(102, CallFailCause.LOCAL_ILLEGAL_STATE);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING, CallFailCause.LOCAL_INTERNAL_ERROR);
        sparseIntArray.append(106, CallFailCause.LOCAL_IMS_SERVICE_DOWN);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS, CallFailCause.LOCAL_NO_PENDING_CALL);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH, 16);
        sparseIntArray.append(111, CallFailCause.LOCAL_POWER_OFF);
        sparseIntArray.append(112, CallFailCause.LOCAL_LOW_BATTERY);
        sparseIntArray.append(121, CallFailCause.LOCAL_NETWORK_NO_SERVICE);
        sparseIntArray.append(122, CallFailCause.LOCAL_NETWORK_NO_LTE_COVERAGE);
        sparseIntArray.append(123, CallFailCause.LOCAL_NETWORK_ROAMING);
        sparseIntArray.append(124, CallFailCause.LOCAL_NETWORK_IP_CHANGED);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RADIO_CAPABILITY, CallFailCause.LOCAL_SERVICE_UNAVAILABLE);
        sparseIntArray.append(132, CallFailCause.LOCAL_NOT_REGISTERED);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_CARRIER_INFO_IMSI_ENCRYPTION, CallFailCause.LOCAL_MAX_CALL_EXCEEDED);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_NETWORK_SCAN, CallFailCause.LOCAL_CALL_DECLINE);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE, CallFailCause.LOCAL_CALL_VCC_ON_PROGRESSING);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_KEEPALIVE, CallFailCause.LOCAL_CALL_RESOURCE_RESERVATION_FAILED);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ICCID, CallFailCause.LOCAL_CALL_CS_RETRY_REQUIRED);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SAR_MODE, CallFailCause.LOCAL_CALL_VOLTE_RETRY_REQUIRED);
        sparseIntArray.append(148, CallFailCause.LOCAL_CALL_TERMINATED);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMSI, CallFailCause.LOCAL_HO_NOT_FEASIBLE);
        sparseIntArray.append(201, CallFailCause.TIMEOUT_1XX_WAITING);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA, CallFailCause.TIMEOUT_NO_ANSWER);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_LINK_CAPACITY_REPORTING_CRITERIA, CallFailCause.TIMEOUT_NO_ANSWER_CALL_UPDATE);
        sparseIntArray.append(241, 241);
        sparseIntArray.append(321, CallFailCause.SIP_REDIRECTED);
        sparseIntArray.append(331, CallFailCause.SIP_BAD_REQUEST);
        sparseIntArray.append(332, CallFailCause.SIP_FORBIDDEN);
        sparseIntArray.append(333, CallFailCause.SIP_NOT_FOUND);
        sparseIntArray.append(334, CallFailCause.SIP_NOT_SUPPORTED);
        sparseIntArray.append(335, CallFailCause.SIP_REQUEST_TIMEOUT);
        sparseIntArray.append(336, CallFailCause.SIP_TEMPRARILY_UNAVAILABLE);
        sparseIntArray.append(337, CallFailCause.SIP_BAD_ADDRESS);
        sparseIntArray.append(338, CallFailCause.SIP_BUSY);
        sparseIntArray.append(339, CallFailCause.SIP_REQUEST_CANCELLED);
        sparseIntArray.append(340, CallFailCause.SIP_NOT_ACCEPTABLE);
        sparseIntArray.append(341, CallFailCause.SIP_NOT_REACHABLE);
        sparseIntArray.append(342, CallFailCause.SIP_CLIENT_ERROR);
        sparseIntArray.append(343, CallFailCause.SIP_TRANSACTION_DOES_NOT_EXIST);
        sparseIntArray.append(351, CallFailCause.SIP_SERVER_INTERNAL_ERROR);
        sparseIntArray.append(352, CallFailCause.SIP_SERVICE_UNAVAILABLE);
        sparseIntArray.append(353, CallFailCause.SIP_SERVER_TIMEOUT);
        sparseIntArray.append(354, CallFailCause.SIP_SERVER_ERROR);
        sparseIntArray.append(361, CallFailCause.SIP_USER_REJECTED);
        sparseIntArray.append(362, CallFailCause.SIP_GLOBAL_ERROR);
        sparseIntArray.append(363, CallFailCause.IMS_EMERGENCY_TEMP_FAILURE);
        sparseIntArray.append(364, CallFailCause.IMS_EMERGENCY_PERM_FAILURE);
        sparseIntArray.append(401, CallFailCause.MEDIA_INIT_FAILED);
        sparseIntArray.append(402, CallFailCause.MEDIA_NO_DATA);
        sparseIntArray.append(403, CallFailCause.MEDIA_NOT_ACCEPTABLE);
        sparseIntArray.append(404, CallFailCause.MEDIA_UNSPECIFIED);
        sparseIntArray.append(NtnRILConstants.MI_NTN_OEM_ERROR_1, 1500);
        sparseIntArray.append(NtnRILConstants.MI_NTN_OEM_ERROR_2, CallFailCause.USER_NOANSWER);
        sparseIntArray.append(NtnRILConstants.MI_NTN_OEM_ERROR_3, CallFailCause.USER_IGNORE);
        sparseIntArray.append(NtnRILConstants.MI_NTN_OEM_ERROR_4, CallFailCause.USER_DECLINE);
        sparseIntArray.append(NtnRILConstants.MI_NTN_OEM_ERROR_5, CallFailCause.LOW_BATTERY);
        sparseIntArray.append(NtnRILConstants.MI_NTN_OEM_ERROR_6, CallFailCause.BLACKLISTED_CALL_ID);
        sparseIntArray.append(NtnRILConstants.MI_NTN_OEM_ERROR_10, CallFailCause.USER_TERMINATED_BY_REMOTE);
        sparseIntArray.append(801, CallFailCause.UT_NOT_SUPPORTED);
        sparseIntArray.append(802, CallFailCause.UT_SERVICE_UNAVAILABLE);
        sparseIntArray.append(803, CallFailCause.UT_OPERATION_NOT_ALLOWED);
        sparseIntArray.append(804, CallFailCause.UT_NETWORK_ERROR);
        sparseIntArray.append(821, CallFailCause.UT_CB_PASSWORD_MISMATCH);
        sparseIntArray.append(901, 1900);
        sparseIntArray.append(902, 1901);
        sparseIntArray.append(1100, 2000);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_EVENT_NOTIFY, 2100);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_CALL_SETUP, 2101);
        sparseIntArray.append(NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_SMS_STORAGE_FULL, 2102);
        sparseIntArray.append(CallFailCause.LOCAL_ILLEGAL_STATE, CallFailCause.SUPP_SVC_FAILED);
        sparseIntArray.append(CallFailCause.LOCAL_INTERNAL_ERROR, CallFailCause.SUPP_SVC_CANCELLED);
        sparseIntArray.append(CallFailCause.LOCAL_IMS_SERVICE_DOWN, CallFailCause.SUPP_SVC_REINVITE_COLLISION);
        sparseIntArray.append(CallFailCause.SIP_REDIRECTED, CallFailCause.IWLAN_DPD_FAILURE);
        sparseIntArray.append(CallFailCause.MEDIA_INIT_FAILED, CallFailCause.EPDG_TUNNEL_ESTABLISH_FAILURE);
        sparseIntArray.append(CallFailCause.MEDIA_NO_DATA, CallFailCause.EPDG_TUNNEL_REKEY_FAILURE);
        sparseIntArray.append(CallFailCause.MEDIA_NOT_ACCEPTABLE, CallFailCause.EPDG_TUNNEL_LOST_CONNECTION);
        sparseIntArray.append(CallFailCause.MEDIA_UNSPECIFIED, CallFailCause.MAXIMUM_NUMBER_OF_CALLS_REACHED);
        sparseIntArray.append(1404, CallFailCause.REMOTE_CALL_DECLINE);
        sparseIntArray.append(1405, CallFailCause.DATA_LIMIT_REACHED);
        sparseIntArray.append(1406, CallFailCause.DATA_DISABLED);
        sparseIntArray.append(1407, CallFailCause.WIFI_LOST);
        sparseIntArray.append(1500, CallFailCause.RADIO_OFF);
        sparseIntArray.append(CallFailCause.USER_NOANSWER, CallFailCause.NO_VALID_SIM);
        sparseIntArray.append(CallFailCause.USER_IGNORE, CallFailCause.RADIO_INTERNAL_ERROR);
        sparseIntArray.append(CallFailCause.USER_DECLINE, CallFailCause.NETWORK_RESP_TIMEOUT);
        sparseIntArray.append(CallFailCause.LOW_BATTERY, CallFailCause.NETWORK_REJECT);
        sparseIntArray.append(CallFailCause.BLACKLISTED_CALL_ID, CallFailCause.RADIO_ACCESS_FAILURE);
        sparseIntArray.append(1506, CallFailCause.RADIO_LINK_FAILURE);
        sparseIntArray.append(1507, 255);
        sparseIntArray.append(1508, CallFailCause.RADIO_UPLINK_FAILURE);
        sparseIntArray.append(1509, CallFailCause.RADIO_SETUP_FAILURE);
        sparseIntArray.append(CallFailCause.USER_TERMINATED_BY_REMOTE, CallFailCause.RADIO_RELEASE_NORMAL);
        sparseIntArray.append(1511, CallFailCause.RADIO_RELEASE_ABNORMAL);
        sparseIntArray.append(1512, CallFailCause.ACCESS_CLASS_BLOCKED);
        sparseIntArray.append(1513, CallFailCause.NETWORK_DETACH);
        sparseIntArray.append(1515, 1);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_1, CallFailCause.OEM_CAUSE_1);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_2, CallFailCause.OEM_CAUSE_2);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_3, CallFailCause.OEM_CAUSE_3);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_4, CallFailCause.OEM_CAUSE_4);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_5, CallFailCause.OEM_CAUSE_5);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_6, CallFailCause.OEM_CAUSE_6);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_7, CallFailCause.OEM_CAUSE_7);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_8, CallFailCause.OEM_CAUSE_8);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_9, CallFailCause.OEM_CAUSE_9);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_10, CallFailCause.OEM_CAUSE_10);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_11, CallFailCause.OEM_CAUSE_11);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_12, CallFailCause.OEM_CAUSE_12);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_13, CallFailCause.OEM_CAUSE_13);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_14, CallFailCause.OEM_CAUSE_14);
        sparseIntArray.append(CallFailCause.OEM_CAUSE_15, CallFailCause.OEM_CAUSE_15);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class MmTelFeatureListener extends MmTelFeature.Listener {
        private MmTelFeatureListener() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.ims.ImsException */
        /* JADX INFO: renamed from: processIncomingCall, reason: merged with bridge method [inline-methods] */
        public IImsCallSessionListener lambda$onIncomingCall$0(IImsCallSession iImsCallSession, String str, Bundle bundle) throws ImsException {
            String callExtra;
            ImsCall imsCall;
            ImsPhoneCallTracker.this.log("processIncomingCall: incoming call intent");
            if (bundle == null) {
                bundle = new Bundle();
            }
            if (ImsPhoneCallTracker.this.mImsManager == null) {
                return null;
            }
            try {
                boolean z = false;
                if (bundle.getBoolean("android.telephony.ims.feature.extra.IS_USSD", false) | bundle.getBoolean("android:ussd", false)) {
                    ImsPhoneCallTracker.this.log("processIncomingCall: USSD");
                    ImsPhoneCallTracker.this.mOperationLocalLog.log("processIncomingCall: USSD");
                    ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker.mUssdSession = imsPhoneCallTracker.mImsManager.takeCall(iImsCallSession, imsPhoneCallTracker.mImsUssdListener);
                    ImsCall imsCall2 = ImsPhoneCallTracker.this.mUssdSession;
                    if (imsCall2 != null) {
                        imsCall2.accept(2);
                    }
                    if (str != null) {
                        ImsPhoneCallTracker.this.mUssdSession.getCallSession().setCallId(str);
                    }
                    return ImsPhoneCallTracker.this.mUssdSession.getCallSession().getIImsCallSessionListenerProxy();
                }
                boolean z2 = bundle.getBoolean("android.telephony.ims.feature.extra.IS_UNKNOWN_CALL", false) | bundle.getBoolean("android:isUnknown", false);
                ImsPhoneCallTracker.this.log("processIncomingCall: isUnknown = " + z2 + " fg = " + ImsPhoneCallTracker.this.mForegroundCall.getState() + " bg = " + ImsPhoneCallTracker.this.mBackgroundCall.getState());
                ImsCall imsCallTakeCall = ImsPhoneCallTracker.this.takeCall(iImsCallSession, bundle);
                if (str != null) {
                    imsCallTakeCall.getCallSession().setCallId(str);
                }
                ImsCallSession.IImsCallSessionListenerProxy iImsCallSessionListenerProxy = imsCallTakeCall.getCallSession().getIImsCallSessionListenerProxy();
                synchronized (ImsPhoneCallTracker.this) {
                    try {
                        ImsPhoneConnection imsPhoneConnectionMakeImsPhoneConnectionForMT = ImsPhoneCallTracker.this.makeImsPhoneConnectionForMT(imsCallTakeCall, z2);
                        if (ImsPhoneCallTracker.this.mForegroundCall.hasConnections() && (imsCall = ImsPhoneCallTracker.this.mForegroundCall.getFirstConnection().getImsCall()) != null) {
                            imsPhoneConnectionMakeImsPhoneConnectionForMT.setActiveCallDisconnectedOnAnswer(ImsPhoneCallTracker.this.shouldDisconnectActiveCallOnAnswer(imsCall, imsCallTakeCall));
                        }
                        imsPhoneConnectionMakeImsPhoneConnectionForMT.setAllowAddCallDuringVideoCall(ImsPhoneCallTracker.this.mAllowAddCallDuringVideoCall);
                        imsPhoneConnectionMakeImsPhoneConnectionForMT.setAllowHoldingVideoCall(ImsPhoneCallTracker.this.mAllowHoldingVideoCall);
                        if (iImsCallSession != null && iImsCallSession.getCallProfile() != null && iImsCallSession.getCallProfile().getCallExtras() != null && iImsCallSession.getCallProfile().getCallExtras().containsKey("android.telephony.ims.extra.CALL_DISCONNECT_CAUSE") && (callExtra = iImsCallSession.getCallProfile().getCallExtra("android.telephony.ims.extra.CALL_DISCONNECT_CAUSE", (String) null)) != null) {
                            try {
                                int disconnectCauseFromReasonInfo = ImsPhoneCallTracker.this.getDisconnectCauseFromReasonInfo(new ImsReasonInfo(Integer.parseInt(callExtra), 0, null), imsPhoneConnectionMakeImsPhoneConnectionForMT.getState());
                                if (disconnectCauseFromReasonInfo == 81) {
                                    imsPhoneConnectionMakeImsPhoneConnectionForMT.setDisconnectCause(disconnectCauseFromReasonInfo);
                                    ImsPhoneCallTracker.this.log("onIncomingCall : incoming call auto rejected");
                                    ImsPhoneCallTracker.this.mOperationLocalLog.log("processIncomingCall: auto rejected");
                                }
                            } catch (NumberFormatException e) {
                                Rlog.e("ImsPhoneCallTracker", "Exception in parsing Integer Data: " + e);
                            }
                        }
                        if (iImsCallSession != null && iImsCallSession.getCallProfile() != null && iImsCallSession.getCallProfile().getCallExtraBoolean("android.telephony.ims.extra.LOW_BATTERY", false)) {
                            z = true;
                        }
                        if (imsCallTakeCall.isVideoCall() && z && !ImsPhoneCallTracker.this.mShouldAllowVtCallsInLowBattery) {
                            ImsPhoneCallTracker.this.log("onIncomingCall : incoming video call auto rejected due to low battery");
                            imsCallTakeCall.reject(NtnRILConstants.MI_NTN_OEM_ERROR_4);
                            imsPhoneConnectionMakeImsPhoneConnectionForMT.setDisconnectCause(81);
                        }
                        ImsPhoneCallTracker.this.mOperationLocalLog.log("onIncomingCall: isUnknown=" + z2 + ", connId=" + System.identityHashCode(imsPhoneConnectionMakeImsPhoneConnectionForMT));
                        ImsPhoneCallTracker.this.addConnection(imsPhoneConnectionMakeImsPhoneConnectionForMT);
                        ImsPhoneCallTracker.this.setVideoCallProvider(imsPhoneConnectionMakeImsPhoneConnectionForMT, imsCallTakeCall);
                        ImsPhoneCallTracker.this.mPhone.getVoiceCallSessionStats().onImsCallReceived(imsPhoneConnectionMakeImsPhoneConnectionForMT);
                        ImsPhoneCallTracker imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                        if (z2) {
                            ImsPhoneConnection imsPhoneConnection = imsPhoneCallTracker2.mPendingMO;
                            if (imsPhoneConnection != null && Objects.equals(imsPhoneConnection.getAddress(), imsPhoneConnectionMakeImsPhoneConnectionForMT.getAddress())) {
                                ImsPhoneCallTracker.this.mOperationLocalLog.log("onIncomingCall: unknown call " + imsPhoneConnectionMakeImsPhoneConnectionForMT + " replaces " + ImsPhoneCallTracker.this.mPendingMO);
                            }
                            ImsPhoneCallTracker.this.mPhone.notifyUnknownConnection(imsPhoneConnectionMakeImsPhoneConnectionForMT);
                        } else {
                            Call.State state = imsPhoneCallTracker2.mForegroundCall.getState();
                            Call.State state2 = Call.State.IDLE;
                            if (state != state2 || ImsPhoneCallTracker.this.mBackgroundCall.getState() != state2) {
                                imsPhoneConnectionMakeImsPhoneConnectionForMT.update(imsCallTakeCall, Call.State.WAITING);
                            }
                            ImsPhoneCallTracker.this.mPhone.notifyNewRingingConnection(imsPhoneConnectionMakeImsPhoneConnectionForMT);
                            ImsPhoneCallTracker.this.mPhone.notifyIncomingRing();
                        }
                        ImsPhoneCallTracker.this.updatePhoneState();
                        ImsPhoneCallTracker.this.mPhone.notifyPreciseCallStateChanged();
                        if (!imsPhoneConnectionMakeImsPhoneConnectionForMT.isIncomingCallAutoRejected()) {
                            ImsPhoneCallTracker.this.mImsCallInfoTracker.addImsCallStatus(imsPhoneConnectionMakeImsPhoneConnectionForMT);
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                return iImsCallSessionListenerProxy;
            } catch (ImsException | RemoteException e2) {
                ImsPhoneCallTracker.this.loge("processIncomingCall: exception " + e2);
                ImsPhoneCallTracker.this.mOperationLocalLog.log("onIncomingCall: exception processing: " + e2);
                return null;
            }
        }

        public IImsCallSessionListener onIncomingCall(final IImsCallSession iImsCallSession, final String str, final Bundle bundle) {
            return (IImsCallSessionListener) executeAndWaitForReturn(new Supplier() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$MmTelFeatureListener$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final Object get() {
                    return this.f$0.lambda$onIncomingCall$0(iImsCallSession, str, bundle);
                }
            });
        }

        public void onVoiceMessageCountUpdate(final int i) {
            TelephonyUtils.runWithCleanCallingIdentity(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$MmTelFeatureListener$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onVoiceMessageCountUpdate$1(i);
                }
            }, ImsPhoneCallTracker.this.mExecutor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onVoiceMessageCountUpdate$1(int i) {
            ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
            ImsPhone imsPhone = imsPhoneCallTracker.mPhone;
            if (imsPhone != null && imsPhone.mDefaultPhone != null) {
                imsPhoneCallTracker.log("onVoiceMessageCountChanged :: count=" + i);
                ImsPhoneCallTracker.this.mPhone.mDefaultPhone.setVoiceMessageCount(i);
                return;
            }
            imsPhoneCallTracker.loge("onVoiceMessageCountUpdate: null phone");
        }

        public void onRejectedCall(ImsCallProfile imsCallProfile, ImsReasonInfo imsReasonInfo) {
            MiuiImsControllerStub.get(ImsPhoneCallTracker.this.mPhone.getPhoneId()).broadcastCallAutoReject(ImsPhoneCallTracker.this.mPhone, imsCallProfile, imsReasonInfo);
        }

        public void onAudioModeIsVoipChanged(final int i) {
            TelephonyUtils.runWithCleanCallingIdentity(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$MmTelFeatureListener$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAudioModeIsVoipChanged$2(i);
                }
            }, ImsPhoneCallTracker.this.mExecutor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAudioModeIsVoipChanged$2(int i) {
            ImsCall imsCall;
            boolean zHasConnections = ImsPhoneCallTracker.this.mForegroundCall.hasConnections();
            ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
            if (zHasConnections) {
                imsCall = imsPhoneCallTracker.mForegroundCall.getImsCall();
            } else {
                boolean zHasConnections2 = imsPhoneCallTracker.mBackgroundCall.hasConnections();
                ImsPhoneCallTracker imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                if (zHasConnections2) {
                    imsCall = imsPhoneCallTracker2.mBackgroundCall.getImsCall();
                } else {
                    boolean zHasConnections3 = imsPhoneCallTracker2.mRingingCall.hasConnections();
                    ImsPhoneCallTracker imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                    if (zHasConnections3) {
                        imsCall = imsPhoneCallTracker3.mRingingCall.getImsCall();
                    } else if (!imsPhoneCallTracker3.mHandoverCall.hasConnections()) {
                        Rlog.e("ImsPhoneCallTracker", "onAudioModeIsVoipChanged: no Call");
                        imsCall = null;
                    } else {
                        imsCall = ImsPhoneCallTracker.this.mHandoverCall.getImsCall();
                    }
                }
            }
            if (imsCall == null) {
                Rlog.e("ImsPhoneCallTracker", "onAudioModeIsVoipChanged: no ImsCall");
                return;
            }
            ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
            if (imsPhoneConnectionFindConnection != null) {
                imsPhoneConnectionFindConnection.onAudioModeIsVoipChanged(i);
            }
        }

        public void onTriggerEpsFallback(final int i) {
            TelephonyUtils.runWithCleanCallingIdentity(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$MmTelFeatureListener$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTriggerEpsFallback$3(i);
                }
            }, ImsPhoneCallTracker.this.mExecutor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTriggerEpsFallback$3(int i) {
            ImsPhoneCallTracker.this.log("onTriggerEpsFallback reason=" + i);
            ImsPhoneCallTracker.this.mPhone.triggerEpsFallback(i, null);
        }

        public void onStartImsTrafficSession(final int i, final int i2, final int i3, final int i4, final IImsTrafficSessionCallback iImsTrafficSessionCallback) {
            ImsPhoneCallTracker.this.registerImsTrafficSession(i, i2, i4, iImsTrafficSessionCallback);
            TelephonyUtils.runWithCleanCallingIdentity(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$MmTelFeatureListener$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStartImsTrafficSession$4(i, i2, i3, i4, iImsTrafficSessionCallback);
                }
            }, ImsPhoneCallTracker.this.mExecutor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStartImsTrafficSession$4(int i, int i2, int i3, int i4, IImsTrafficSessionCallback iImsTrafficSessionCallback) {
            ImsPhoneCallTracker.this.log("onStartImsTrafficSession token=" + i + ", traffic=" + i2 + ", networkType=" + i3 + ", direction=" + i4);
            ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
            imsPhoneCallTracker.mPhone.startImsTraffic(i, i2, i3, i4, imsPhoneCallTracker.obtainMessage(33, iImsTrafficSessionCallback));
        }

        public void onModifyImsTrafficSession(final int i, final int i2) {
            final ImsTrafficSession imsTrafficSession = ImsPhoneCallTracker.this.getImsTrafficSession(i);
            if (imsTrafficSession == null) {
                ImsPhoneCallTracker.this.loge("onModifyImsTrafficSession unknown session, token=" + i);
                return;
            }
            TelephonyUtils.runWithCleanCallingIdentity(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$MmTelFeatureListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onModifyImsTrafficSession$5(i, i2, imsTrafficSession);
                }
            }, ImsPhoneCallTracker.this.mExecutor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onModifyImsTrafficSession$5(int i, int i2, ImsTrafficSession imsTrafficSession) {
            ImsPhoneCallTracker.this.log("onModifyImsTrafficSession token=" + i + ", networkType=" + i2);
            ImsPhoneCallTracker.this.mPhone.startImsTraffic(i, imsTrafficSession.mTrafficType, i2, imsTrafficSession.mTrafficDirection, ImsPhoneCallTracker.this.obtainMessage(33, imsTrafficSession.mCallback));
        }

        public void onStopImsTrafficSession(final int i) {
            ImsPhoneCallTracker.this.unregisterImsTrafficSession(i);
            if (i == -1) {
                return;
            }
            TelephonyUtils.runWithCleanCallingIdentity(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$MmTelFeatureListener$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStopImsTrafficSession$6(i);
                }
            }, ImsPhoneCallTracker.this.mExecutor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStopImsTrafficSession$6(int i) {
            ImsPhoneCallTracker.this.log("onStopImsTrafficSession token=" + i);
            ImsPhoneCallTracker.this.mPhone.stopImsTraffic(i, null);
        }

        public void onMediaQualityStatusChanged(final MediaQualityStatus mediaQualityStatus) {
            TelephonyUtils.runWithCleanCallingIdentity(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$MmTelFeatureListener$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onMediaQualityStatusChanged$7(mediaQualityStatus);
                }
            }, ImsPhoneCallTracker.this.mExecutor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMediaQualityStatusChanged$7(MediaQualityStatus mediaQualityStatus) {
            ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
            ImsPhone imsPhone = imsPhoneCallTracker.mPhone;
            if (imsPhone != null && imsPhone.mDefaultPhone != null) {
                imsPhoneCallTracker.log("onMediaQualityStatusChanged " + mediaQualityStatus);
                ImsPhoneCallTracker.this.mPhone.onMediaQualityStatusChanged(mediaQualityStatus);
                return;
            }
            imsPhoneCallTracker.loge("onMediaQualityStatusChanged: null phone");
        }

        private Object executeAndWaitForReturn(final Supplier supplier) {
            try {
                return CompletableFuture.supplyAsync(new Supplier() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$MmTelFeatureListener$$ExternalSyntheticLambda8
                    @Override // java.util.function.Supplier
                    public final Object get() {
                        return TelephonyUtils.runWithCleanCallingIdentity(supplier);
                    }
                }, ImsPhoneCallTracker.this.mExecutor).get();
            } catch (InterruptedException | ExecutionException e) {
                Log.w("ImsPhoneCallTracker", "ImsPhoneCallTracker : executeAndWaitForReturn exception: " + e.getMessage());
                return null;
            }
        }
    }

    public class VtDataUsageProvider extends NetworkStatsProvider {
        private int mToken = 0;
        private NetworkStats mIfaceSnapshot = new NetworkStats(0, 0);
        private NetworkStats mUidSnapshot = new NetworkStats(0, 0);

        public void onSetAlert(long j) {
        }

        public void onSetLimit(String str, long j) {
        }

        public VtDataUsageProvider() {
        }

        public void onRequestStatsUpdate(int i) {
            ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
            if (imsPhoneCallTracker.mState != PhoneConstants.State.IDLE) {
                ArrayList<ImsPhoneConnection> arrayList = imsPhoneCallTracker.mConnections;
                int size = arrayList.size();
                int i2 = 0;
                while (i2 < size) {
                    ImsPhoneConnection imsPhoneConnection = arrayList.get(i2);
                    i2++;
                    android.telecom.Connection.VideoProvider videoProvider = imsPhoneConnection.getVideoProvider();
                    if (videoProvider != null) {
                        videoProvider.onRequestConnectionDataUsage();
                    }
                }
            }
            NetworkStats networkStatsSubtract = ImsPhoneCallTracker.this.mVtDataUsageSnapshot.subtract(this.mIfaceSnapshot);
            NetworkStats networkStatsSubtract2 = ImsPhoneCallTracker.this.mVtDataUsageUidSnapshot.subtract(this.mUidSnapshot);
            ImsPhoneCallTracker.this.mVtDataUsageProvider.notifyStatsUpdated(this.mToken, networkStatsSubtract, networkStatsSubtract2);
            this.mIfaceSnapshot = this.mIfaceSnapshot.add(networkStatsSubtract);
            this.mUidSnapshot = this.mUidSnapshot.add(networkStatsSubtract2);
            this.mToken = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class CacheEntry {
        private long mCachedTime;
        private int mCallDirection;
        private long mConnectElapsedTime;
        private long mConnectTime;

        CacheEntry(long j, long j2, long j3, int i) {
            this.mCachedTime = j;
            this.mConnectTime = j2;
            this.mConnectElapsedTime = j3;
            this.mCallDirection = i;
        }
    }

    private class SrvccStartedCallback extends ISrvccStartedCallback.Stub {
        private SrvccStartedCallback() {
        }

        public void onSrvccCallNotified(List<SrvccCall> list) {
            ImsPhoneCallTracker.this.handleSrvccConnectionInfo(list);
        }
    }

    private static class ImsReasonInfoKeyPair extends Pair<Integer, String> {
        private ImsReasonInfoKeyPair(Integer num, String str) {
            super(num, str);
        }

        @Override // android.util.Pair
        public boolean equals(Object obj) {
            if (!(obj instanceof ImsReasonInfoKeyPair)) {
                return false;
            }
            ImsReasonInfoKeyPair imsReasonInfoKeyPair = (ImsReasonInfoKeyPair) obj;
            return Objects.equals(((Pair) imsReasonInfoKeyPair).first, ((Pair) this).first) && Objects.toString(((Pair) this).second).startsWith(Objects.toString(((Pair) imsReasonInfoKeyPair).second));
        }

        @Override // android.util.Pair
        public int hashCode() {
            Object obj = ((Pair) this).first;
            if (obj == null) {
                return 0;
            }
            return ((Integer) obj).hashCode();
        }
    }

    private static class ProvisioningItem {
        final int mItem;
        final Object mValue;

        ProvisioningItem(int i, int i2) {
            this.mItem = i;
            this.mValue = Integer.valueOf(i2);
        }

        ProvisioningItem(int i, String str) {
            this.mItem = i;
            this.mValue = str;
        }
    }

    public ImsPhoneCallTracker(ImsPhone imsPhone, ConnectorFactory connectorFactory, FeatureFlags featureFlags) {
        this(imsPhone, connectorFactory, imsPhone.getContext().getMainExecutor(), featureFlags);
    }

    public ImsPhoneCallTracker(ImsPhone imsPhone, ConnectorFactory connectorFactory, Executor executor, FeatureFlags featureFlags) {
        super(featureFlags);
        this.mMmTelCapabilities = new MmTelFeature.MmTelCapabilities();
        this.mCarrierConfigLoadedForSubscription = false;
        this.mCarrierConfigForSubId = null;
        this.mCurrentlyConnectedSubId = Optional.empty();
        this.mMmTelFeatureListener = new MmTelFeatureListener();
        this.mCarrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.1
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                int phoneId = ImsPhoneCallTracker.this.mPhone.getPhoneId();
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                if (phoneId != i) {
                    imsPhoneCallTracker.log("onReceive: Skipping indication for other phoneId: " + i);
                    return;
                }
                PersistableBundle carrierConfigBundle = imsPhoneCallTracker.getCarrierConfigBundle(i2);
                ImsPhoneCallTracker.this.mCarrierConfigForSubId = new Pair(Integer.valueOf(i2), carrierConfigBundle);
                if (!ImsPhoneCallTracker.this.mCurrentlyConnectedSubId.isEmpty() && i2 == ((Integer) ImsPhoneCallTracker.this.mCurrentlyConnectedSubId.get()).intValue()) {
                    ImsPhoneCallTracker.this.log("onReceive: Applying carrier config for subId: " + i2);
                    ImsPhoneCallTracker.this.updateCarrierConfiguration(i2, carrierConfigBundle);
                    return;
                }
                ImsPhoneCallTracker.this.log("onReceive: caching carrier config until ImsService connects for subId: " + i2);
            }
        };
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("android.telecom.action.DEFAULT_DIALER_CHANGED".equals(intent.getAction())) {
                    ImsPhoneCallTracker.this.mDefaultDialerUid.set(ImsPhoneCallTracker.this.getPackageUid(context, intent.getStringExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME")));
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        this.mIsMonitoringConnectivity = false;
        this.mIsConferenceEventPackageEnabled = true;
        this.mConfig = null;
        this.mDeviceToDeviceForceEnabled = false;
        this.mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.3
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                Rlog.i("ImsPhoneCallTracker", "Network available: " + network);
                ImsPhoneCallTracker.this.scheduleHandoverCheck();
            }
        };
        this.mConnections = new ArrayList<>();
        this.mVoiceCallEndedRegistrants = new RegistrantList();
        this.mVoiceCallStartedRegistrants = new RegistrantList();
        this.mRingingCall = new ImsPhoneCall(this, ImsPhoneCall.CONTEXT_RINGING);
        this.mForegroundCall = new ImsPhoneCall(this, ImsPhoneCall.CONTEXT_FOREGROUND);
        this.mBackgroundCall = new ImsPhoneCall(this, ImsPhoneCall.CONTEXT_BACKGROUND);
        this.mHandoverCall = new ImsPhoneCall(this, ImsPhoneCall.CONTEXT_HANDOVER);
        this.mVtDataUsageMap = new HashMap();
        this.mPhoneNumAndConnTime = new ConcurrentHashMap();
        this.mUnknownPeerConnTime = new LinkedBlockingQueue();
        this.mVtDataUsageSnapshot = null;
        this.mVtDataUsageUidSnapshot = null;
        VtDataUsageProvider vtDataUsageProvider = new VtDataUsageProvider();
        this.mVtDataUsageProvider = vtDataUsageProvider;
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        this.mDefaultDialerUid = atomicInteger;
        this.mClirMode = 0;
        this.mSyncHold = new Object();
        this.mUssdSession = null;
        this.mPendingUssd = null;
        this.mDesiredMute = false;
        this.mOnHoldToneStarted = false;
        this.mOnHoldToneId = -1;
        this.mState = PhoneConstants.State.IDLE;
        this.mSrvccState = Call.SrvccState.NONE;
        this.mIsInEmergencyCall = false;
        this.mIsDataEnabled = false;
        this.pendingCallInEcm = false;
        this.mCallExpectedToResume = null;
        this.mAllowEmergencyVideoCalls = false;
        this.mIgnoreDataEnabledChangedForVideoCalls = false;
        this.mIsViLteDataMetered = false;
        this.mAlwaysPlayRemoteHoldTone = false;
        this.mAutoRetryFailedWifiEmergencyCall = false;
        this.mSupportCepOnPeer = true;
        this.mSupportD2DUsingRtp = false;
        this.mSupportSdpForRtpHeaderExtensions = false;
        this.mVolteRoamingSupported = false;
        this.mSrvccTypeSupported = new ArrayList();
        this.mSrvccStartedCallback = new SrvccStartedCallback();
        this.mHoldSwitchingState = HoldSwapState.INACTIVE;
        this.mImsCapability = new SparseBooleanArray();
        this.mLastDialString = null;
        this.mLastDialArgs = null;
        this.mExecutor = new CarrierPrivilegesTracker$$ExternalSyntheticLambda0();
        this.mCallExpectedToHold = null;
        this.mCallExpectedToAccept = null;
        this.mPhoneStateListeners = new ArrayList();
        this.mTreatDowngradedVideoCallsAsVideoCalls = false;
        this.mDropVideoCallWhenAnsweringAudioCall = false;
        this.mAllowAddCallDuringVideoCall = true;
        this.mAllowHoldingVideoCall = true;
        this.mNotifyVtHandoverToWifiFail = false;
        this.mSupportDowngradeVtToAudio = false;
        this.mNotifyHandoverVideoFromWifiToLTE = false;
        this.mNotifyHandoverVideoFromLTEToWifi = false;
        this.mHasAttemptedStartOfCallHandover = false;
        this.mSupportPauseVideo = false;
        this.mImsReasonCodeMap = new ArrayMap();
        this.mAllowHoldingCall = true;
        this.mUssdMethod = 0;
        this.mShouldAllowVtCallsInLowBattery = true;
        this.mShouldUpdateImsConfigOnDisconnect = false;
        this.mPendingSilentRedialInfo = null;
        this.mAllowRttWhileRoaming = false;
        this.mSharedPreferenceProxy = new SharedPreferenceProxy() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$$ExternalSyntheticLambda1
            @Override // com.android.internal.telephony.imsphone.ImsPhoneCallTracker.SharedPreferenceProxy
            public final SharedPreferences getDefaultSharedPreferences(Context context) {
                return PreferenceManager.getDefaultSharedPreferences(context);
            }
        };
        this.mConnectorRunnable = new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.4
            @Override // java.lang.Runnable
            public void run() {
                ImsPhoneCallTracker.this.mImsManagerConnector.connect();
            }
        };
        this.mRegLocalLog = new LocalLog(64);
        this.mOperationLocalLog = new LocalLog(64);
        this.mImsTrafficSessions = new ConcurrentHashMap();
        this.mProvisioningItemQueue = new ConcurrentLinkedQueue();
        this.mImsCallListener = new ImsCall.Listener() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.8
            public void onCallInitiating(ImsCall imsCall) {
                ImsPhoneCallTracker.this.log("onCallInitiating");
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                imsPhoneCallTracker.mPendingMO = null;
                imsPhoneCallTracker.processCallStateChange(imsCall, Call.State.DIALING, 0, true);
            }

            public void onCallProgressing(ImsCall imsCall) {
                ImsPhoneCallTracker.this.log("onCallProgressing");
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                imsPhoneCallTracker.mPendingMO = null;
                imsPhoneCallTracker.processCallStateChange(imsCall, Call.State.ALERTING, 0);
            }

            public void onCallStarted(ImsCall imsCall) {
                ImsCall imsCall2;
                ImsPhoneCallTracker.this.log("onCallStarted");
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                if (imsPhoneCallTracker.mHoldSwitchingState == HoldSwapState.HOLDING_TO_ANSWER_INCOMING && (imsCall2 = imsPhoneCallTracker.mCallExpectedToResume) != null && imsCall2 == imsCall) {
                    imsPhoneCallTracker.log("onCallStarted: starting a call as a result of a switch.");
                    ImsPhoneCallTracker imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker2.mHoldSwitchingState = HoldSwapState.INACTIVE;
                    imsPhoneCallTracker2.mCallExpectedToResume = null;
                    imsPhoneCallTracker2.logHoldSwapState("onCallStarted");
                }
                ImsPhoneCallTracker imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                imsPhoneCallTracker3.mPendingMO = null;
                imsPhoneCallTracker3.processCallStateChange(imsCall, Call.State.ACTIVE, 0);
                if (ImsPhoneCallTracker.this.mNotifyVtHandoverToWifiFail && imsCall.isVideoCall() && !imsCall.isWifiCall()) {
                    boolean zIsWifiConnected = ImsPhoneCallTracker.this.isWifiConnected();
                    ImsPhoneCallTracker imsPhoneCallTracker4 = ImsPhoneCallTracker.this;
                    if (zIsWifiConnected) {
                        imsPhoneCallTracker4.sendMessageDelayed(imsPhoneCallTracker4.obtainMessage(25, imsCall), 60000L);
                        ImsPhoneCallTracker.this.mHasAttemptedStartOfCallHandover = false;
                    } else {
                        imsPhoneCallTracker4.registerForConnectivityChanges();
                        ImsPhoneCallTracker.this.mHasAttemptedStartOfCallHandover = true;
                    }
                }
            }

            public void onCallUpdated(ImsCall imsCall) {
                ImsPhoneCallTracker.this.log("onCallUpdated");
                if (imsCall == null) {
                    return;
                }
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                ((IHyperCallManager) HyperTelephonyFactory.getInstance().initModule(IHyperCallManager.DEFAULT, new Object[0])).onImsCallUpdated(imsCall, imsPhoneConnectionFindConnection, ImsPhoneCall.isLocalTone(imsCall), ImsPhoneCallTracker.this.mPhone);
                if (imsPhoneConnectionFindConnection != null) {
                    ImsPhoneCallTracker.this.log("onCallUpdated: profile is " + imsCall.getCallProfile());
                    ImsPhoneCallTracker.this.processCallStateChange(imsCall, imsPhoneConnectionFindConnection.getCall().mState, 0, true);
                }
            }

            public void onCallStartFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo) {
                List<String> emergencyUrns;
                int emergencyServiceCategories;
                ImsPhoneConnection imsPhoneConnectionFindConnection;
                ImsCall imsCall2;
                ImsPhoneCallTracker.this.log("onCallStartFailed reasonCode=" + imsReasonInfo.getCode());
                if (((IHyperCallManager) HyperTelephonyFactory.getInstance().initModule(IHyperCallManager.DEFAULT, new Object[0])).onImsCallStartFailed(imsCall, imsReasonInfo, ImsPhoneCallTracker.this.mPhone)) {
                    ImsPhoneCallTracker.this.log("onCallStartFailed handled by onImsCallStartFailed");
                    return;
                }
                ArrayList arrayList = new ArrayList();
                if (imsCall == null || imsCall.getCallProfile() == null) {
                    emergencyUrns = arrayList;
                    emergencyServiceCategories = 0;
                } else {
                    emergencyServiceCategories = imsCall.getCallProfile().getEmergencyServiceCategories();
                    emergencyUrns = imsCall.getCallProfile().getEmergencyUrns();
                }
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                if (imsPhoneCallTracker.mHoldSwitchingState == HoldSwapState.HOLDING_TO_ANSWER_INCOMING && (imsCall2 = imsPhoneCallTracker.mCallExpectedToResume) != null && imsCall2 == imsCall) {
                    imsPhoneCallTracker.log("onCallStarted: starting a call as a result of a switch.");
                    ImsPhoneCallTracker imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker2.mHoldSwitchingState = HoldSwapState.INACTIVE;
                    imsPhoneCallTracker2.mCallExpectedToResume = null;
                    imsPhoneCallTracker2.logHoldSwapState("onCallStartFailed");
                }
                ImsPhoneCallTracker.this.mPhone.getVoiceCallSessionStats().onImsCallStartFailed(ImsPhoneCallTracker.this.findConnection(imsCall), new ImsReasonInfo(ImsPhoneCallTracker.this.maybeRemapReasonCode(imsReasonInfo), imsReasonInfo.mExtraCode, imsReasonInfo.mExtraMessage));
                if (DomainSelectionResolver.getInstance().isDomainSelectionSupported() && (imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall)) != null) {
                    ImsPhoneCallTracker.this.logi("onCallStartFailed eccCategory=" + emergencyServiceCategories + ", emergencyUrns=" + emergencyUrns);
                    int code = imsReasonInfo.getCode();
                    int extraCode = imsReasonInfo.getExtraCode();
                    if ((code == 146 && extraCode == 4) || code == 1514) {
                        imsPhoneConnectionFindConnection.setNonDetectableEmergencyCallInfo(emergencyServiceCategories, emergencyUrns);
                    }
                    imsPhoneConnectionFindConnection.setImsReasonInfo(imsReasonInfo);
                    ImsPhoneCallTracker.this.sendCallStartFailedDisconnect(imsCall, imsReasonInfo);
                    return;
                }
                if (ImsPhoneCallTracker.this.mPendingMO != null) {
                    if (imsReasonInfo.getCode() == 146 && ImsPhoneCallTracker.this.mRingingCall.getState() == Call.State.IDLE && ImsPhoneCallTracker.this.isForegroundHigherPriority()) {
                        ImsPhoneCallTracker imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker3.mForegroundCall.detach(imsPhoneCallTracker3.mPendingMO);
                        ImsPhoneCallTracker imsPhoneCallTracker4 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker4.removeConnection(imsPhoneCallTracker4.mPendingMO);
                        ImsPhoneCallTracker imsPhoneCallTracker5 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker5.mImsCallInfoTracker.updateImsCallStatus(imsPhoneCallTracker5.mPendingMO);
                        ImsPhoneCallTracker.this.mPendingMO.finalize();
                        ImsPhoneCallTracker imsPhoneCallTracker6 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker6.mPendingMO = null;
                        boolean zIsAlive = imsPhoneCallTracker6.mBackgroundCall.getState().isAlive();
                        ImsPhoneCallTracker imsPhoneCallTracker7 = ImsPhoneCallTracker.this;
                        if (zIsAlive) {
                            try {
                                imsPhoneCallTracker7.hangup(imsPhoneCallTracker7.mBackgroundCall);
                                ImsPhoneCallTracker.this.mPendingSilentRedialInfo = new Pair(Boolean.valueOf(imsReasonInfo.getExtraCode() == 4), Integer.valueOf(emergencyServiceCategories));
                                return;
                            } catch (CallStateException unused) {
                                ImsPhoneCallTracker.this.mPendingSilentRedialInfo = null;
                                return;
                            }
                        }
                        imsPhoneCallTracker7.updatePhoneState();
                        ImsPhoneCallTracker.this.mPhone.initiateSilentRedial(imsReasonInfo.getExtraCode() == 4, emergencyServiceCategories);
                        return;
                    }
                    ImsPhoneCallTracker.this.sendCallStartFailedDisconnect(imsCall, imsReasonInfo);
                    return;
                }
                if (imsReasonInfo.getCode() == 146 && ImsPhoneCallTracker.this.mForegroundCall.getState() == Call.State.ALERTING) {
                    ImsPhoneCallTracker.this.log("onCallStartFailed: Initiated call by silent redial under ALERTING state.");
                    ImsPhoneConnection imsPhoneConnectionFindConnection2 = ImsPhoneCallTracker.this.findConnection(imsCall);
                    if (imsPhoneConnectionFindConnection2 != null) {
                        ImsPhoneCallTracker.this.mForegroundCall.detach(imsPhoneConnectionFindConnection2);
                        ImsPhoneCallTracker.this.removeConnection(imsPhoneConnectionFindConnection2);
                        ImsPhoneCallTracker.this.mImsCallInfoTracker.updateImsCallStatus(imsPhoneConnectionFindConnection2);
                    }
                    ImsPhoneCallTracker.this.updatePhoneState();
                    ImsPhoneCallTracker.this.mPhone.initiateSilentRedial(imsReasonInfo.getExtraCode() == 4, emergencyServiceCategories);
                }
            }

            public void onCallTerminated(ImsCall imsCall, ImsReasonInfo imsReasonInfo) {
                Call.State state;
                ImsPhone imsPhone2;
                ImsPhoneCallTracker.this.log("onCallTerminated reasonCode=" + imsReasonInfo.getCode());
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    state = imsPhoneConnectionFindConnection.getState();
                } else {
                    state = Call.State.ACTIVE;
                }
                int disconnectCauseFromReasonInfo = ImsPhoneCallTracker.this.getDisconnectCauseFromReasonInfo(imsReasonInfo, state);
                ImsPhoneCallTracker.this.log("cause = " + disconnectCauseFromReasonInfo + " conn = " + imsPhoneConnectionFindConnection);
                if (imsPhoneConnectionFindConnection != null) {
                    ImsVideoCallProviderWrapper videoProvider = imsPhoneConnectionFindConnection.getVideoProvider();
                    if (videoProvider instanceof ImsVideoCallProviderWrapper) {
                        ImsVideoCallProviderWrapper imsVideoCallProviderWrapper = videoProvider;
                        imsVideoCallProviderWrapper.unregisterForDataUsageUpdate(ImsPhoneCallTracker.this);
                        imsVideoCallProviderWrapper.removeImsVideoProviderCallback(imsPhoneConnectionFindConnection);
                    }
                }
                int emergencyServiceCategories = 0;
                if (ImsPhoneCallTracker.this.mOnHoldToneId == System.identityHashCode(imsPhoneConnectionFindConnection)) {
                    if (imsPhoneConnectionFindConnection != null) {
                        ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                        if (imsPhoneCallTracker.mOnHoldToneStarted) {
                            imsPhoneCallTracker.mPhone.stopOnHoldTone(imsPhoneConnectionFindConnection);
                        }
                    }
                    ImsPhoneCallTracker imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker2.mOnHoldToneStarted = false;
                    imsPhoneCallTracker2.mOnHoldToneId = -1;
                }
                if (imsPhoneConnectionFindConnection != null) {
                    if (imsPhoneConnectionFindConnection.isPulledCall() && ((imsReasonInfo.getCode() == 1015 || imsReasonInfo.getCode() == 336 || imsReasonInfo.getCode() == 332) && (imsPhone2 = ImsPhoneCallTracker.this.mPhone) != null && imsPhone2.getExternalCallTracker() != null)) {
                        ImsPhoneCallTracker.this.log("Call pull failed.");
                        imsPhoneConnectionFindConnection.onCallPullFailed(ImsPhoneCallTracker.this.mPhone.getExternalCallTracker().getConnectionById(imsPhoneConnectionFindConnection.getPulledDialogId()));
                        disconnectCauseFromReasonInfo = 0;
                    } else if (imsPhoneConnectionFindConnection.isIncoming() && imsPhoneConnectionFindConnection.getConnectTime() == 0 && disconnectCauseFromReasonInfo != 52) {
                        disconnectCauseFromReasonInfo = (imsPhoneConnectionFindConnection.getDisconnectCause() == 3 || disconnectCauseFromReasonInfo == 16) ? 16 : 1;
                        ImsPhoneCallTracker.this.log("Incoming connection of 0 connect time detected - translated cause = " + disconnectCauseFromReasonInfo);
                    }
                }
                if (disconnectCauseFromReasonInfo == 2 && imsPhoneConnectionFindConnection != null && imsPhoneConnectionFindConnection.getImsCall().isMerged()) {
                    disconnectCauseFromReasonInfo = 45;
                }
                if (disconnectCauseFromReasonInfo == 45 && imsPhoneConnectionFindConnection != null) {
                    imsPhoneConnectionFindConnection.onConnectionEvent("android.telecom.event.MERGE_COMPLETE", null);
                }
                int iUpdateDisconnectCause = ImsPhoneCallTracker.this.updateDisconnectCause(disconnectCauseFromReasonInfo, imsPhoneConnectionFindConnection);
                ImsPhoneCallTracker.this.setRedialAsEcc(iUpdateDisconnectCause);
                if (imsPhoneConnectionFindConnection != null && imsCall.getSession() != null) {
                    imsCall.getSession().getCallId();
                    imsPhoneConnectionFindConnection.getEmergencyNumberTracker();
                    imsPhoneConnectionFindConnection.getEmergencyNumberInfo();
                    ImsPhoneCallTracker.this.mPhone.getVoiceCallSessionStats().onImsCallTerminated(imsPhoneConnectionFindConnection, new ImsReasonInfo(ImsPhoneCallTracker.this.maybeRemapReasonCode(imsReasonInfo), imsReasonInfo.mExtraCode, imsReasonInfo.mExtraMessage));
                }
                ImsPhoneCallTracker.this.mPhone.notifyImsReason(imsReasonInfo);
                if (imsPhoneConnectionFindConnection != null) {
                    imsPhoneConnectionFindConnection.setPreciseDisconnectCause(ImsPhoneCallTracker.this.getPreciseDisconnectCauseFromReasonInfo(imsReasonInfo));
                    imsPhoneConnectionFindConnection.setImsReasonInfo(imsReasonInfo);
                }
                if (imsReasonInfo.getCode() == 1514 && DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
                    if (imsPhoneConnectionFindConnection != null) {
                        List<String> arrayList = new ArrayList<>();
                        if (imsCall != null && imsCall.getCallProfile() != null) {
                            emergencyServiceCategories = imsCall.getCallProfile().getEmergencyServiceCategories();
                            arrayList = imsCall.getCallProfile().getEmergencyUrns();
                            ImsPhoneCallTracker.this.logi("onCallTerminated eccCategory=" + emergencyServiceCategories);
                        }
                        imsPhoneConnectionFindConnection.setNonDetectableEmergencyCallInfo(emergencyServiceCategories, arrayList);
                    }
                    ImsPhoneCallTracker.this.processCallStateChange(imsCall, Call.State.DISCONNECTED, iUpdateDisconnectCause);
                    return;
                }
                if (imsReasonInfo.getCode() == 1514 && ImsPhoneCallTracker.this.mAutoRetryFailedWifiEmergencyCall) {
                    Pair pair = new Pair(imsCall, imsReasonInfo);
                    ImsPhoneCallTracker.this.mPhone.getDefaultPhone().mCi.registerForOn(ImsPhoneCallTracker.this, 28, pair);
                    ImsPhoneCallTracker imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker3.sendMessageDelayed(imsPhoneCallTracker3.obtainMessage(29, pair), ImsPhoneCallTracker.TIMEOUT_REDIAL_WIFI_E911_MS);
                    ((ConnectivityManager) ImsPhoneCallTracker.this.mPhone.getContext().getSystemService("connectivity")).setAirplaneMode(false);
                    return;
                }
                if (imsReasonInfo.getCode() == 3001) {
                    Pair pair2 = new Pair(imsCall, imsReasonInfo);
                    ImsPhoneCallTracker imsPhoneCallTracker4 = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker4.sendMessage(imsPhoneCallTracker4.obtainMessage(32, pair2));
                    return;
                }
                if (TelephonyBaseUtilsStub.isMiuiRom()) {
                    ImsPhoneCallTracker imsPhoneCallTracker5 = ImsPhoneCallTracker.this;
                    if (imsCall == imsPhoneCallTracker5.mCallExpectedToHold && imsPhoneCallTracker5.mCallExpectedToAccept != null && !imsPhoneCallTracker5.mForegroundCall.getState().isAlive()) {
                        ImsCall imsCall2 = ImsPhoneCallTracker.this.mRingingCall.getImsCall();
                        ImsPhoneCallTracker imsPhoneCallTracker6 = ImsPhoneCallTracker.this;
                        if (imsCall2 == imsPhoneCallTracker6.mCallExpectedToAccept) {
                            imsPhoneCallTracker6.log("Background call was disconnected, continue to accept the incoming call");
                            ImsPhoneCallTracker.this.sendEmptyMessage(30);
                        }
                    }
                }
                ImsPhoneCallTracker.this.processCallStateChange(imsCall, Call.State.DISCONNECTED, iUpdateDisconnectCause);
                if (ImsPhoneCallTracker.this.mForegroundCall.getState() != Call.State.ACTIVE && ImsPhoneCallTracker.this.mRingingCall.getState().isRinging()) {
                    ImsPhoneCallTracker.this.disconnectPendingMo();
                }
                ImsPhoneCallTracker imsPhoneCallTracker7 = ImsPhoneCallTracker.this;
                HoldSwapState holdSwapState = imsPhoneCallTracker7.mHoldSwitchingState;
                if (holdSwapState == HoldSwapState.SWAPPING_ACTIVE_AND_HELD) {
                    imsPhoneCallTracker7.log("onCallTerminated: Call terminated in the midst of Switching Fg and Bg calls.");
                    ImsPhoneCallTracker imsPhoneCallTracker8 = ImsPhoneCallTracker.this;
                    if (imsCall == imsPhoneCallTracker8.mCallExpectedToResume) {
                        imsPhoneCallTracker8.log("onCallTerminated: switching " + ImsPhoneCallTracker.this.mForegroundCall + " with " + ImsPhoneCallTracker.this.mBackgroundCall);
                        ImsPhoneCallTracker imsPhoneCallTracker9 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker9.mForegroundCall.switchWith(imsPhoneCallTracker9.mBackgroundCall);
                    }
                    ImsPhoneCallTracker imsPhoneCallTracker10 = ImsPhoneCallTracker.this;
                    StringBuilder sb = new StringBuilder("onCallTerminated: foreground call in state ");
                    sb.append(ImsPhoneCallTracker.this.mForegroundCall.getState());
                    sb.append(" and ringing call in state ");
                    ImsPhoneCall imsPhoneCall = ImsPhoneCallTracker.this.mRingingCall;
                    sb.append(imsPhoneCall == null ? "null" : imsPhoneCall.getState().toString());
                    imsPhoneCallTracker10.log(sb.toString());
                    ImsPhoneCallTracker.this.sendEmptyMessage(31);
                    ImsPhoneCallTracker imsPhoneCallTracker11 = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker11.mHoldSwitchingState = HoldSwapState.INACTIVE;
                    imsPhoneCallTracker11.mCallExpectedToResume = null;
                    imsPhoneCallTracker11.logHoldSwapState("onCallTerminated swap active and hold case");
                } else if (holdSwapState == HoldSwapState.PENDING_SINGLE_CALL_UNHOLD || holdSwapState == HoldSwapState.PENDING_SINGLE_CALL_HOLD) {
                    imsPhoneCallTracker7.mCallExpectedToResume = null;
                    imsPhoneCallTracker7.mHoldSwitchingState = HoldSwapState.INACTIVE;
                    imsPhoneCallTracker7.logHoldSwapState("onCallTerminated single call case");
                } else if (holdSwapState == HoldSwapState.HOLDING_TO_ANSWER_INCOMING) {
                    if (imsCall == imsPhoneCallTracker7.mCallExpectedToResume) {
                        imsPhoneCallTracker7.mForegroundCall.switchWith(imsPhoneCallTracker7.mBackgroundCall);
                        ImsPhoneCallTracker imsPhoneCallTracker12 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker12.mCallExpectedToResume = null;
                        imsPhoneCallTracker12.mHoldSwitchingState = HoldSwapState.INACTIVE;
                        imsPhoneCallTracker12.logHoldSwapState("onCallTerminated hold to answer case");
                        ImsPhoneCallTracker.this.sendEmptyMessage(31);
                    }
                } else if (holdSwapState == HoldSwapState.HOLDING_TO_DIAL_OUTGOING) {
                    ImsPhoneConnection imsPhoneConnection = imsPhoneCallTracker7.mPendingMO;
                    if (imsPhoneConnection == null || imsPhoneConnection.getDisconnectCause() != 0) {
                        ImsPhoneCallTracker imsPhoneCallTracker13 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker13.mHoldSwitchingState = HoldSwapState.INACTIVE;
                        imsPhoneCallTracker13.logHoldSwapState("onCallTerminated hold/end to dial but no pendingMo");
                    } else if (ImsPhoneCallTracker.this.canDailOnCallTerminated() && imsCall != ImsPhoneCallTracker.this.mPendingMO.getImsCall()) {
                        ImsPhoneCallTracker.this.sendEmptyMessage(20);
                        ImsPhoneCallTracker imsPhoneCallTracker14 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker14.mHoldSwitchingState = HoldSwapState.INACTIVE;
                        imsPhoneCallTracker14.logHoldSwapState("onCallTerminated hold/end to dial, dial pendingMo");
                    }
                }
                ImsPhoneCallTracker imsPhoneCallTracker15 = ImsPhoneCallTracker.this;
                if (imsPhoneCallTracker15.mShouldUpdateImsConfigOnDisconnect) {
                    imsPhoneCallTracker15.updateImsServiceConfig();
                    ImsPhoneCallTracker.this.mShouldUpdateImsConfigOnDisconnect = false;
                }
                if (ImsPhoneCallTracker.this.mPendingSilentRedialInfo != null) {
                    ImsPhoneCallTracker imsPhoneCallTracker16 = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker16.mPhone.initiateSilentRedial(((Boolean) imsPhoneCallTracker16.mPendingSilentRedialInfo.first).booleanValue(), ((Integer) ImsPhoneCallTracker.this.mPendingSilentRedialInfo.second).intValue());
                    ImsPhoneCallTracker.this.mPendingSilentRedialInfo = null;
                }
            }

            /* JADX WARN: Code duplicated, block: B:22:0x0086 A[Catch: all -> 0x006b, TryCatch #0 {all -> 0x006b, blocks: (B:10:0x003e, B:12:0x0059, B:44:0x00ef, B:15:0x006e, B:17:0x0072, B:19:0x007a, B:21:0x0082, B:22:0x0086, B:24:0x0092, B:26:0x009a, B:27:0x00a0, B:29:0x00a6, B:31:0x00ac, B:32:0x00bb, B:33:0x00c5, B:35:0x00c9, B:37:0x00cd, B:39:0x00d1, B:41:0x00d5, B:43:0x00dd), top: B:48:0x003e }] */
            /* JADX WARN: Code duplicated, block: B:24:0x0092 A[Catch: all -> 0x006b, TryCatch #0 {all -> 0x006b, blocks: (B:10:0x003e, B:12:0x0059, B:44:0x00ef, B:15:0x006e, B:17:0x0072, B:19:0x007a, B:21:0x0082, B:22:0x0086, B:24:0x0092, B:26:0x009a, B:27:0x00a0, B:29:0x00a6, B:31:0x00ac, B:32:0x00bb, B:33:0x00c5, B:35:0x00c9, B:37:0x00cd, B:39:0x00d1, B:41:0x00d5, B:43:0x00dd), top: B:48:0x003e }] */
            /* JADX WARN: Code duplicated, block: B:26:0x009a A[Catch: all -> 0x006b, TryCatch #0 {all -> 0x006b, blocks: (B:10:0x003e, B:12:0x0059, B:44:0x00ef, B:15:0x006e, B:17:0x0072, B:19:0x007a, B:21:0x0082, B:22:0x0086, B:24:0x0092, B:26:0x009a, B:27:0x00a0, B:29:0x00a6, B:31:0x00ac, B:32:0x00bb, B:33:0x00c5, B:35:0x00c9, B:37:0x00cd, B:39:0x00d1, B:41:0x00d5, B:43:0x00dd), top: B:48:0x003e }] */
            /* JADX WARN: Code duplicated, block: B:27:0x00a0 A[Catch: all -> 0x006b, TryCatch #0 {all -> 0x006b, blocks: (B:10:0x003e, B:12:0x0059, B:44:0x00ef, B:15:0x006e, B:17:0x0072, B:19:0x007a, B:21:0x0082, B:22:0x0086, B:24:0x0092, B:26:0x009a, B:27:0x00a0, B:29:0x00a6, B:31:0x00ac, B:32:0x00bb, B:33:0x00c5, B:35:0x00c9, B:37:0x00cd, B:39:0x00d1, B:41:0x00d5, B:43:0x00dd), top: B:48:0x003e }] */
            /* JADX WARN: Code duplicated, block: B:29:0x00a6 A[Catch: all -> 0x006b, TryCatch #0 {all -> 0x006b, blocks: (B:10:0x003e, B:12:0x0059, B:44:0x00ef, B:15:0x006e, B:17:0x0072, B:19:0x007a, B:21:0x0082, B:22:0x0086, B:24:0x0092, B:26:0x009a, B:27:0x00a0, B:29:0x00a6, B:31:0x00ac, B:32:0x00bb, B:33:0x00c5, B:35:0x00c9, B:37:0x00cd, B:39:0x00d1, B:41:0x00d5, B:43:0x00dd), top: B:48:0x003e }] */
            /* JADX WARN: Code duplicated, block: B:32:0x00bb A[Catch: all -> 0x006b, TryCatch #0 {all -> 0x006b, blocks: (B:10:0x003e, B:12:0x0059, B:44:0x00ef, B:15:0x006e, B:17:0x0072, B:19:0x007a, B:21:0x0082, B:22:0x0086, B:24:0x0092, B:26:0x009a, B:27:0x00a0, B:29:0x00a6, B:31:0x00ac, B:32:0x00bb, B:33:0x00c5, B:35:0x00c9, B:37:0x00cd, B:39:0x00d1, B:41:0x00d5, B:43:0x00dd), top: B:48:0x003e }] */
            public void onCallHeld(ImsCall imsCall) {
                ImsPhoneCallTracker imsPhoneCallTracker;
                ImsPhoneCallTracker imsPhoneCallTracker2;
                ImsCall imsCall2 = ImsPhoneCallTracker.this.mForegroundCall.getImsCall();
                ImsPhoneCallTracker imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                if (imsCall2 == imsCall) {
                    imsPhoneCallTracker3.log("onCallHeld (fg) " + imsCall);
                } else if (imsPhoneCallTracker3.mBackgroundCall.getImsCall() == imsCall) {
                    ImsPhoneCallTracker.this.log("onCallHeld (bg) " + imsCall);
                }
                synchronized (ImsPhoneCallTracker.this.mSyncHold) {
                    try {
                        Call.State state = ImsPhoneCallTracker.this.mBackgroundCall.getState();
                        ImsPhoneCallTracker imsPhoneCallTracker4 = ImsPhoneCallTracker.this;
                        Call.State state2 = Call.State.HOLDING;
                        imsPhoneCallTracker4.processCallStateChange(imsCall, state2, 0);
                        ImsPhoneCallTracker imsPhoneCallTracker5 = ImsPhoneCallTracker.this;
                        HoldSwapState holdSwapState = imsPhoneCallTracker5.mHoldSwitchingState;
                        if (holdSwapState == HoldSwapState.PENDING_RESUME_FOREGROUND_AFTER_HOLD) {
                            imsPhoneCallTracker5.sendEmptyMessage(31);
                            ImsPhoneCallTracker imsPhoneCallTracker6 = ImsPhoneCallTracker.this;
                            imsPhoneCallTracker6.mHoldSwitchingState = HoldSwapState.INACTIVE;
                            imsPhoneCallTracker6.mCallExpectedToResume = null;
                            imsPhoneCallTracker6.logHoldSwapState("onCallHeld PENDING_RESUME_FOREGROUND_AFTER_HOLD");
                        } else if (state == Call.State.ACTIVE) {
                            if (imsPhoneCallTracker5.mForegroundCall.getState() == state2) {
                                ImsPhoneCallTracker imsPhoneCallTracker7 = ImsPhoneCallTracker.this;
                                if (imsPhoneCallTracker7.mHoldSwitchingState == HoldSwapState.SWAPPING_ACTIVE_AND_HELD) {
                                    imsPhoneCallTracker7.sendEmptyMessage(31);
                                } else if (ImsPhoneCallTracker.this.mRingingCall.getState() == Call.State.WAITING) {
                                    imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                                    if (imsPhoneCallTracker2.mHoldSwitchingState == HoldSwapState.HOLDING_TO_ANSWER_INCOMING) {
                                        imsPhoneCallTracker2.sendEmptyMessage(30);
                                    } else {
                                        imsPhoneCallTracker = ImsPhoneCallTracker.this;
                                        if (imsPhoneCallTracker.mPendingMO == null && imsPhoneCallTracker.mHoldSwitchingState == HoldSwapState.HOLDING_TO_DIAL_OUTGOING) {
                                            imsPhoneCallTracker.dialPendingMO();
                                            ImsPhoneCallTracker imsPhoneCallTracker8 = ImsPhoneCallTracker.this;
                                            imsPhoneCallTracker8.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                            imsPhoneCallTracker8.logHoldSwapState("onCallHeld hold to dial");
                                        } else {
                                            imsPhoneCallTracker.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                            imsPhoneCallTracker.logHoldSwapState("onCallHeld normal case");
                                        }
                                    }
                                } else {
                                    imsPhoneCallTracker = ImsPhoneCallTracker.this;
                                    if (imsPhoneCallTracker.mPendingMO == null) {
                                        imsPhoneCallTracker.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                        imsPhoneCallTracker.logHoldSwapState("onCallHeld normal case");
                                    } else {
                                        imsPhoneCallTracker.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                        imsPhoneCallTracker.logHoldSwapState("onCallHeld normal case");
                                    }
                                }
                            } else if (ImsPhoneCallTracker.this.mRingingCall.getState() == Call.State.WAITING) {
                                imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                                if (imsPhoneCallTracker2.mHoldSwitchingState == HoldSwapState.HOLDING_TO_ANSWER_INCOMING) {
                                    imsPhoneCallTracker2.sendEmptyMessage(30);
                                } else {
                                    imsPhoneCallTracker = ImsPhoneCallTracker.this;
                                    if (imsPhoneCallTracker.mPendingMO == null) {
                                        imsPhoneCallTracker.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                        imsPhoneCallTracker.logHoldSwapState("onCallHeld normal case");
                                    } else {
                                        imsPhoneCallTracker.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                        imsPhoneCallTracker.logHoldSwapState("onCallHeld normal case");
                                    }
                                }
                            } else {
                                imsPhoneCallTracker = ImsPhoneCallTracker.this;
                                if (imsPhoneCallTracker.mPendingMO == null) {
                                    imsPhoneCallTracker.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                    imsPhoneCallTracker.logHoldSwapState("onCallHeld normal case");
                                } else {
                                    imsPhoneCallTracker.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                    imsPhoneCallTracker.logHoldSwapState("onCallHeld normal case");
                                }
                            }
                        } else if (state == Call.State.IDLE && ((holdSwapState == HoldSwapState.SWAPPING_ACTIVE_AND_HELD || holdSwapState == HoldSwapState.HOLDING_TO_ANSWER_INCOMING || holdSwapState == HoldSwapState.PENDING_SINGLE_CALL_HOLD) && imsPhoneCallTracker5.mForegroundCall.getState() == state2)) {
                            ImsPhoneCallTracker.this.sendEmptyMessage(31);
                            ImsPhoneCallTracker imsPhoneCallTracker9 = ImsPhoneCallTracker.this;
                            imsPhoneCallTracker9.mHoldSwitchingState = HoldSwapState.INACTIVE;
                            imsPhoneCallTracker9.mCallExpectedToResume = null;
                            imsPhoneCallTracker9.logHoldSwapState("onCallHeld premature termination of other call");
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }

            /* JADX WARN: Code duplicated, block: B:25:0x0075 A[Catch: all -> 0x0048, TryCatch #0 {all -> 0x0048, blocks: (B:9:0x002d, B:11:0x003d, B:58:0x012f, B:60:0x0137, B:62:0x013f, B:63:0x0144, B:64:0x014d, B:14:0x004b, B:17:0x0055, B:19:0x0059, B:30:0x008e, B:20:0x005d, B:22:0x0067, B:24:0x006f, B:25:0x0075, B:27:0x0081, B:29:0x0089, B:31:0x009b, B:33:0x009f, B:41:0x00c1, B:43:0x00d3, B:35:0x00a5, B:37:0x00ab, B:39:0x00b3, B:44:0x00d6, B:46:0x00e2, B:48:0x00ea, B:49:0x00fd, B:51:0x0101, B:53:0x0110, B:54:0x011e, B:56:0x0124, B:57:0x0126), top: B:68:0x002d }] */
            /* JADX WARN: Code duplicated, block: B:27:0x0081 A[Catch: all -> 0x0048, TryCatch #0 {all -> 0x0048, blocks: (B:9:0x002d, B:11:0x003d, B:58:0x012f, B:60:0x0137, B:62:0x013f, B:63:0x0144, B:64:0x014d, B:14:0x004b, B:17:0x0055, B:19:0x0059, B:30:0x008e, B:20:0x005d, B:22:0x0067, B:24:0x006f, B:25:0x0075, B:27:0x0081, B:29:0x0089, B:31:0x009b, B:33:0x009f, B:41:0x00c1, B:43:0x00d3, B:35:0x00a5, B:37:0x00ab, B:39:0x00b3, B:44:0x00d6, B:46:0x00e2, B:48:0x00ea, B:49:0x00fd, B:51:0x0101, B:53:0x0110, B:54:0x011e, B:56:0x0124, B:57:0x0126), top: B:68:0x002d }] */
            /* JADX WARN: Code duplicated, block: B:29:0x0089 A[Catch: all -> 0x0048, TryCatch #0 {all -> 0x0048, blocks: (B:9:0x002d, B:11:0x003d, B:58:0x012f, B:60:0x0137, B:62:0x013f, B:63:0x0144, B:64:0x014d, B:14:0x004b, B:17:0x0055, B:19:0x0059, B:30:0x008e, B:20:0x005d, B:22:0x0067, B:24:0x006f, B:25:0x0075, B:27:0x0081, B:29:0x0089, B:31:0x009b, B:33:0x009f, B:41:0x00c1, B:43:0x00d3, B:35:0x00a5, B:37:0x00ab, B:39:0x00b3, B:44:0x00d6, B:46:0x00e2, B:48:0x00ea, B:49:0x00fd, B:51:0x0101, B:53:0x0110, B:54:0x011e, B:56:0x0124, B:57:0x0126), top: B:68:0x002d }] */
            /* JADX WARN: Code duplicated, block: B:41:0x00c1 A[Catch: all -> 0x0048, TryCatch #0 {all -> 0x0048, blocks: (B:9:0x002d, B:11:0x003d, B:58:0x012f, B:60:0x0137, B:62:0x013f, B:63:0x0144, B:64:0x014d, B:14:0x004b, B:17:0x0055, B:19:0x0059, B:30:0x008e, B:20:0x005d, B:22:0x0067, B:24:0x006f, B:25:0x0075, B:27:0x0081, B:29:0x0089, B:31:0x009b, B:33:0x009f, B:41:0x00c1, B:43:0x00d3, B:35:0x00a5, B:37:0x00ab, B:39:0x00b3, B:44:0x00d6, B:46:0x00e2, B:48:0x00ea, B:49:0x00fd, B:51:0x0101, B:53:0x0110, B:54:0x011e, B:56:0x0124, B:57:0x0126), top: B:68:0x002d }] */
            /* JADX WARN: Code duplicated, block: B:43:0x00d3 A[Catch: all -> 0x0048, TryCatch #0 {all -> 0x0048, blocks: (B:9:0x002d, B:11:0x003d, B:58:0x012f, B:60:0x0137, B:62:0x013f, B:63:0x0144, B:64:0x014d, B:14:0x004b, B:17:0x0055, B:19:0x0059, B:30:0x008e, B:20:0x005d, B:22:0x0067, B:24:0x006f, B:25:0x0075, B:27:0x0081, B:29:0x0089, B:31:0x009b, B:33:0x009f, B:41:0x00c1, B:43:0x00d3, B:35:0x00a5, B:37:0x00ab, B:39:0x00b3, B:44:0x00d6, B:46:0x00e2, B:48:0x00ea, B:49:0x00fd, B:51:0x0101, B:53:0x0110, B:54:0x011e, B:56:0x0124, B:57:0x0126), top: B:68:0x002d }] */
            /* JADX WARN: Code duplicated, block: B:49:0x00fd A[Catch: all -> 0x0048, TryCatch #0 {all -> 0x0048, blocks: (B:9:0x002d, B:11:0x003d, B:58:0x012f, B:60:0x0137, B:62:0x013f, B:63:0x0144, B:64:0x014d, B:14:0x004b, B:17:0x0055, B:19:0x0059, B:30:0x008e, B:20:0x005d, B:22:0x0067, B:24:0x006f, B:25:0x0075, B:27:0x0081, B:29:0x0089, B:31:0x009b, B:33:0x009f, B:41:0x00c1, B:43:0x00d3, B:35:0x00a5, B:37:0x00ab, B:39:0x00b3, B:44:0x00d6, B:46:0x00e2, B:48:0x00ea, B:49:0x00fd, B:51:0x0101, B:53:0x0110, B:54:0x011e, B:56:0x0124, B:57:0x0126), top: B:68:0x002d }] */
            /* JADX WARN: Code duplicated, block: B:51:0x0101 A[Catch: all -> 0x0048, TryCatch #0 {all -> 0x0048, blocks: (B:9:0x002d, B:11:0x003d, B:58:0x012f, B:60:0x0137, B:62:0x013f, B:63:0x0144, B:64:0x014d, B:14:0x004b, B:17:0x0055, B:19:0x0059, B:30:0x008e, B:20:0x005d, B:22:0x0067, B:24:0x006f, B:25:0x0075, B:27:0x0081, B:29:0x0089, B:31:0x009b, B:33:0x009f, B:41:0x00c1, B:43:0x00d3, B:35:0x00a5, B:37:0x00ab, B:39:0x00b3, B:44:0x00d6, B:46:0x00e2, B:48:0x00ea, B:49:0x00fd, B:51:0x0101, B:53:0x0110, B:54:0x011e, B:56:0x0124, B:57:0x0126), top: B:68:0x002d }] */
            /* JADX WARN: Code duplicated, block: B:53:0x0110 A[Catch: all -> 0x0048, TryCatch #0 {all -> 0x0048, blocks: (B:9:0x002d, B:11:0x003d, B:58:0x012f, B:60:0x0137, B:62:0x013f, B:63:0x0144, B:64:0x014d, B:14:0x004b, B:17:0x0055, B:19:0x0059, B:30:0x008e, B:20:0x005d, B:22:0x0067, B:24:0x006f, B:25:0x0075, B:27:0x0081, B:29:0x0089, B:31:0x009b, B:33:0x009f, B:41:0x00c1, B:43:0x00d3, B:35:0x00a5, B:37:0x00ab, B:39:0x00b3, B:44:0x00d6, B:46:0x00e2, B:48:0x00ea, B:49:0x00fd, B:51:0x0101, B:53:0x0110, B:54:0x011e, B:56:0x0124, B:57:0x0126), top: B:68:0x002d }] */
            /* JADX WARN: Code duplicated, block: B:56:0x0124 A[Catch: all -> 0x0048, TryCatch #0 {all -> 0x0048, blocks: (B:9:0x002d, B:11:0x003d, B:58:0x012f, B:60:0x0137, B:62:0x013f, B:63:0x0144, B:64:0x014d, B:14:0x004b, B:17:0x0055, B:19:0x0059, B:30:0x008e, B:20:0x005d, B:22:0x0067, B:24:0x006f, B:25:0x0075, B:27:0x0081, B:29:0x0089, B:31:0x009b, B:33:0x009f, B:41:0x00c1, B:43:0x00d3, B:35:0x00a5, B:37:0x00ab, B:39:0x00b3, B:44:0x00d6, B:46:0x00e2, B:48:0x00ea, B:49:0x00fd, B:51:0x0101, B:53:0x0110, B:54:0x011e, B:56:0x0124, B:57:0x0126), top: B:68:0x002d }] */
            public void onCallHoldFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo) {
                ImsPhoneConnection imsPhoneConnection;
                ImsPhoneCallTracker imsPhoneCallTracker;
                ImsPhoneCallTracker imsPhoneCallTracker2;
                ImsPhoneCallTracker imsPhoneCallTracker3;
                ImsPhoneCallTracker.this.log("onCallHoldFailed reasonCode=" + imsReasonInfo.getCode());
                if (TelephonyBaseUtilsStub.isMiuiRom()) {
                    ImsPhoneCallTracker imsPhoneCallTracker4 = ImsPhoneCallTracker.this;
                    if (imsCall == imsPhoneCallTracker4.mCallExpectedToHold) {
                        imsPhoneCallTracker4.mCallExpectedToHold = null;
                        imsPhoneCallTracker4.mCallExpectedToAccept = null;
                    }
                }
                synchronized (ImsPhoneCallTracker.this.mSyncHold) {
                    try {
                        Call.State state = ImsPhoneCallTracker.this.mBackgroundCall.getState();
                        ImsPhoneCallTracker imsPhoneCallTracker5 = ImsPhoneCallTracker.this;
                        if (imsPhoneCallTracker5.mHoldSwitchingState == HoldSwapState.PENDING_RESUME_FOREGROUND_AFTER_HOLD) {
                            imsPhoneCallTracker5.mHoldSwitchingState = HoldSwapState.INACTIVE;
                            imsPhoneCallTracker5.logHoldSwapState("onCallHoldFailed PENDING_RESUME_FOREGROUND_AFTER_HOLD");
                        } else {
                            int code = imsReasonInfo.getCode();
                            ImsPhoneCallTracker imsPhoneCallTracker6 = ImsPhoneCallTracker.this;
                            if (code == 148) {
                                if (imsPhoneCallTracker6.mPendingMO != null) {
                                    imsPhoneCallTracker6.dialPendingMO();
                                } else if (imsPhoneCallTracker6.mRingingCall.getState() == Call.State.WAITING) {
                                    ImsPhoneCallTracker imsPhoneCallTracker7 = ImsPhoneCallTracker.this;
                                    if (imsPhoneCallTracker7.mHoldSwitchingState == HoldSwapState.HOLDING_TO_ANSWER_INCOMING) {
                                        imsPhoneCallTracker7.sendEmptyMessage(30);
                                    } else if (ImsPhoneCallTracker.this.mForegroundCall.getState() == Call.State.HOLDING) {
                                        imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                                        if (imsPhoneCallTracker3.mHoldSwitchingState == HoldSwapState.SWAPPING_ACTIVE_AND_HELD) {
                                            imsPhoneCallTracker3.sendEmptyMessage(31);
                                        }
                                    }
                                } else if (ImsPhoneCallTracker.this.mForegroundCall.getState() == Call.State.HOLDING) {
                                    imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                                    if (imsPhoneCallTracker3.mHoldSwitchingState == HoldSwapState.SWAPPING_ACTIVE_AND_HELD) {
                                        imsPhoneCallTracker3.sendEmptyMessage(31);
                                    }
                                }
                                ImsPhoneCallTracker imsPhoneCallTracker8 = ImsPhoneCallTracker.this;
                                imsPhoneCallTracker8.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                imsPhoneCallTracker8.logHoldSwapState("onCallHoldFailed CODE_LOCAL_CALL_TERMINATED");
                            } else {
                                ImsPhoneConnection imsPhoneConnection2 = imsPhoneCallTracker6.mPendingMO;
                                if (imsPhoneConnection2 == null || !imsPhoneConnection2.isEmergency()) {
                                    ImsPhoneCallTracker imsPhoneCallTracker9 = ImsPhoneCallTracker.this;
                                    if (imsPhoneCallTracker9.mPendingMO == null && imsPhoneCallTracker9.mForegroundCall.getFirstConnection() != null && ImsPhoneCallTracker.this.mForegroundCall.getFirstConnection().isEmergency()) {
                                        ImsPhoneCallTracker.this.mBackgroundCall.getImsCall().terminate(0);
                                        imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                                        if (imsCall != imsPhoneCallTracker2.mCallExpectedToResume) {
                                            imsPhoneCallTracker2.mCallExpectedToResume = null;
                                        }
                                    } else if (ImsPhoneCallTracker.this.mRingingCall.getState() == Call.State.WAITING) {
                                        ImsPhoneCallTracker imsPhoneCallTracker10 = ImsPhoneCallTracker.this;
                                        if (imsPhoneCallTracker10.mHoldSwitchingState == HoldSwapState.HOLDING_TO_ANSWER_INCOMING) {
                                            imsPhoneCallTracker10.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                            imsPhoneCallTracker10.mForegroundCall.switchWith(imsPhoneCallTracker10.mBackgroundCall);
                                            ImsPhoneCallTracker.this.logHoldSwapState("onCallHoldFailed unable to answer waiting call");
                                        } else if (state == Call.State.ACTIVE) {
                                            ImsPhoneCallTracker imsPhoneCallTracker11 = ImsPhoneCallTracker.this;
                                            imsPhoneCallTracker11.mForegroundCall.switchWith(imsPhoneCallTracker11.mBackgroundCall);
                                            imsPhoneConnection = ImsPhoneCallTracker.this.mPendingMO;
                                            if (imsPhoneConnection != null) {
                                                imsPhoneConnection.setDisconnectCause(36);
                                                ImsPhoneCallTracker.this.sendEmptyMessageDelayed(18, 500L);
                                            }
                                            imsPhoneCallTracker = ImsPhoneCallTracker.this;
                                            if (imsCall != imsPhoneCallTracker.mCallExpectedToResume) {
                                                imsPhoneCallTracker.mCallExpectedToResume = null;
                                            }
                                            imsPhoneCallTracker.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                            imsPhoneCallTracker.logHoldSwapState("onCallHoldFailed active bg call");
                                        }
                                    } else if (state == Call.State.ACTIVE) {
                                        ImsPhoneCallTracker imsPhoneCallTracker12 = ImsPhoneCallTracker.this;
                                        imsPhoneCallTracker12.mForegroundCall.switchWith(imsPhoneCallTracker12.mBackgroundCall);
                                        imsPhoneConnection = ImsPhoneCallTracker.this.mPendingMO;
                                        if (imsPhoneConnection != null) {
                                            imsPhoneConnection.setDisconnectCause(36);
                                            ImsPhoneCallTracker.this.sendEmptyMessageDelayed(18, 500L);
                                        }
                                        imsPhoneCallTracker = ImsPhoneCallTracker.this;
                                        if (imsCall != imsPhoneCallTracker.mCallExpectedToResume) {
                                            imsPhoneCallTracker.mCallExpectedToResume = null;
                                        }
                                        imsPhoneCallTracker.mHoldSwitchingState = HoldSwapState.INACTIVE;
                                        imsPhoneCallTracker.logHoldSwapState("onCallHoldFailed active bg call");
                                    }
                                } else {
                                    ImsPhoneCallTracker.this.mBackgroundCall.getImsCall().terminate(0);
                                    imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                                    if (imsCall != imsPhoneCallTracker2.mCallExpectedToResume) {
                                        imsPhoneCallTracker2.mCallExpectedToResume = null;
                                    }
                                }
                            }
                        }
                        ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                        if (imsPhoneConnectionFindConnection != null && imsPhoneConnectionFindConnection.getState() != Call.State.DISCONNECTED) {
                            imsPhoneConnectionFindConnection.onConnectionEvent("android.telecom.event.CALL_HOLD_FAILED", null);
                        }
                        ImsPhoneCallTracker.this.mPhone.notifySuppServiceFailed(PhoneInternalInterface.SuppService.HOLD);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }

            public void onCallResumed(ImsCall imsCall) {
                ImsPhoneCallTracker.this.log("onCallResumed");
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                HoldSwapState holdSwapState = imsPhoneCallTracker.mHoldSwitchingState;
                if (holdSwapState == HoldSwapState.SWAPPING_ACTIVE_AND_HELD || holdSwapState == HoldSwapState.PENDING_RESUME_FOREGROUND_AFTER_FAILURE || holdSwapState == HoldSwapState.PENDING_SINGLE_CALL_UNHOLD) {
                    if (imsCall != imsPhoneCallTracker.mCallExpectedToResume) {
                        imsPhoneCallTracker.log("onCallResumed : switching " + ImsPhoneCallTracker.this.mForegroundCall + " with " + ImsPhoneCallTracker.this.mBackgroundCall);
                        ImsPhoneCallTracker imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker2.mForegroundCall.switchWith(imsPhoneCallTracker2.mBackgroundCall);
                    } else {
                        imsPhoneCallTracker.log("onCallResumed : expected call resumed.");
                    }
                    ImsPhoneCallTracker imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker3.mHoldSwitchingState = HoldSwapState.INACTIVE;
                    imsPhoneCallTracker3.mCallExpectedToResume = null;
                    imsPhoneCallTracker3.logHoldSwapState("onCallResumed");
                }
                ImsPhoneCallTracker.this.processCallStateChange(imsCall, Call.State.ACTIVE, 0);
            }

            public void onCallResumeFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo) {
                ImsPhoneCallTracker.this.log("onCallResumeFailed : mHoldSwitchingState = " + ImsPhoneCallTracker.this.mHoldSwitchingState + " fg state = " + ImsPhoneCallTracker.this.mForegroundCall.getState() + " bg state = " + ImsPhoneCallTracker.this.mBackgroundCall.getState());
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                HoldSwapState holdSwapState = imsPhoneCallTracker.mHoldSwitchingState;
                if (holdSwapState == HoldSwapState.SWAPPING_ACTIVE_AND_HELD || holdSwapState == HoldSwapState.PENDING_RESUME_FOREGROUND_AFTER_FAILURE) {
                    if (imsCall == imsPhoneCallTracker.mCallExpectedToResume) {
                        imsPhoneCallTracker.log("onCallResumeFailed : switching " + ImsPhoneCallTracker.this.mForegroundCall + " with " + ImsPhoneCallTracker.this.mBackgroundCall);
                        ImsPhoneCallTracker imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker2.mForegroundCall.switchWith(imsPhoneCallTracker2.mBackgroundCall);
                        if (ImsPhoneCallTracker.this.mForegroundCall.getState() == Call.State.HOLDING) {
                            ImsPhoneCallTracker.this.sendEmptyMessage(31);
                        }
                    }
                    ImsPhoneCallTracker imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker3.mCallExpectedToResume = null;
                    imsPhoneCallTracker3.mHoldSwitchingState = HoldSwapState.INACTIVE;
                    imsPhoneCallTracker3.logHoldSwapState("onCallResumeFailed: multi calls");
                } else if (holdSwapState == HoldSwapState.PENDING_SINGLE_CALL_UNHOLD) {
                    if (imsCall == imsPhoneCallTracker.mCallExpectedToResume) {
                        imsPhoneCallTracker.log("onCallResumeFailed: single call unhold case");
                        ImsPhoneCallTracker imsPhoneCallTracker4 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker4.mForegroundCall.switchWith(imsPhoneCallTracker4.mBackgroundCall);
                        ImsPhoneCallTracker imsPhoneCallTracker5 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker5.mCallExpectedToResume = null;
                        imsPhoneCallTracker5.mHoldSwitchingState = HoldSwapState.INACTIVE;
                        imsPhoneCallTracker5.logHoldSwapState("onCallResumeFailed: single call");
                    } else {
                        Rlog.w("ImsPhoneCallTracker", "onCallResumeFailed: got a resume failed for a different call in the single call unhold case");
                    }
                }
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null && imsPhoneConnectionFindConnection.getState() != Call.State.DISCONNECTED) {
                    imsPhoneConnectionFindConnection.onConnectionEvent("android.telecom.event.CALL_RESUME_FAILED", null);
                }
                ImsPhoneCallTracker.this.mPhone.notifySuppServiceFailed(PhoneInternalInterface.SuppService.RESUME);
            }

            public void onCallResumeReceived(ImsCall imsCall) {
                ImsPhoneCallTracker.this.log("onCallResumeReceived");
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                    if (imsPhoneCallTracker.mOnHoldToneStarted) {
                        imsPhoneCallTracker.mPhone.stopOnHoldTone(imsPhoneConnectionFindConnection);
                        ImsPhoneCallTracker.this.mOnHoldToneStarted = false;
                    }
                    imsPhoneConnectionFindConnection.setRemotelyUnheld();
                    ImsPhoneCallTracker.this.mImsCallInfoTracker.updateImsCallStatus(imsPhoneConnectionFindConnection, false, true);
                }
                if (ImsPhoneCallTracker.this.mPhone.getContext().getResources().getBoolean(17892066) && ImsPhoneCallTracker.this.mSupportPauseVideo && VideoProfile.isVideo(imsPhoneConnectionFindConnection.getVideoState())) {
                    imsPhoneConnectionFindConnection.changeToUnPausedState();
                }
                SuppServiceNotification suppServiceNotification = new SuppServiceNotification();
                suppServiceNotification.notificationType = 1;
                suppServiceNotification.code = 3;
                ImsPhoneCallTracker.this.mPhone.notifySuppSvcNotification(suppServiceNotification);
            }

            public void onCallHoldReceived(ImsCall imsCall) {
                ImsPhoneCallTracker.this.onCallHoldReceived(imsCall);
            }

            public void onCallSuppServiceReceived(ImsCall imsCall, ImsSuppServiceNotification imsSuppServiceNotification) {
                ImsPhoneCallTracker.this.log("onCallSuppServiceReceived: suppServiceInfo=" + imsSuppServiceNotification);
                SuppServiceNotification suppServiceNotification = new SuppServiceNotification();
                suppServiceNotification.notificationType = imsSuppServiceNotification.notificationType;
                suppServiceNotification.code = imsSuppServiceNotification.code;
                suppServiceNotification.index = imsSuppServiceNotification.index;
                suppServiceNotification.number = imsSuppServiceNotification.number;
                suppServiceNotification.history = imsSuppServiceNotification.history;
                ImsPhoneCallTracker.this.mPhone.notifySuppSvcNotification(suppServiceNotification);
            }

            public void onCallMerged(ImsCall imsCall, ImsCall imsCall2, boolean z) {
                ImsPhoneCallTracker.this.log("onCallMerged");
                ImsPhoneCall call = ImsPhoneCallTracker.this.findConnection(imsCall).getCall();
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall2);
                ImsPhoneCall call2 = imsPhoneConnectionFindConnection == null ? null : imsPhoneConnectionFindConnection.getCall();
                if (z) {
                    ImsPhoneCallTracker.this.switchAfterConferenceSuccess();
                }
                call.merge(call2, Call.State.ACTIVE);
                ImsPhoneConnection imsPhoneConnectionFindConnection2 = ImsPhoneCallTracker.this.findConnection(imsCall);
                try {
                    ImsPhoneCallTracker.this.log("onCallMerged: ImsPhoneConnection=" + imsPhoneConnectionFindConnection2);
                    ImsPhoneCallTracker.this.log("onCallMerged: CurrentVideoProvider=" + imsPhoneConnectionFindConnection2.getVideoProvider());
                    ImsPhoneCallTracker.this.setVideoCallProvider(imsPhoneConnectionFindConnection2, imsCall);
                    ImsPhoneCallTracker.this.log("onCallMerged: CurrentVideoProvider=" + imsPhoneConnectionFindConnection2.getVideoProvider());
                } catch (Exception e) {
                    ImsPhoneCallTracker.this.loge("onCallMerged: exception " + e);
                }
                if (imsPhoneConnectionFindConnection2 != null && imsPhoneConnectionFindConnection2.isRttEnabledForCall()) {
                    ImsPhoneCallTracker.this.log("onCallMerged: resetting RTT text handler for conference");
                    imsPhoneConnectionFindConnection2.resetRttTextHandler();
                }
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                imsPhoneCallTracker.processCallStateChange(imsPhoneCallTracker.mForegroundCall.getImsCall(), Call.State.ACTIVE, 0);
                if (imsPhoneConnectionFindConnection != null) {
                    ImsPhoneCallTracker imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker2.processCallStateChange(imsPhoneCallTracker2.mBackgroundCall.getImsCall(), Call.State.HOLDING, 0);
                    imsPhoneConnectionFindConnection.handleMergeComplete();
                }
                boolean zIsMergeRequestedByConf = imsCall.isMergeRequestedByConf();
                ImsPhoneCallTracker imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                if (!zIsMergeRequestedByConf) {
                    imsPhoneCallTracker3.log("onCallMerged :: calling onMultipartyStateChanged()");
                    onMultipartyStateChanged(imsCall, true);
                } else {
                    imsPhoneCallTracker3.log("onCallMerged :: Merge requested by existing conference.");
                    imsCall.resetIsMergeRequestedByConf(false);
                }
                if (imsPhoneConnectionFindConnection2 != null) {
                    imsPhoneConnectionFindConnection2.handleMergeComplete();
                    if (imsPhoneConnectionFindConnection2.isHeldByRemote()) {
                        imsPhoneConnectionFindConnection2.setRemotelyUnheld();
                    }
                }
                ImsPhoneCallTracker.this.logState();
            }

            public void onCallMergeFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo) {
                ImsPhoneCallTracker.this.log("onCallMergeFailed reasonInfo=" + imsReasonInfo);
                ImsPhoneCallTracker.this.mPhone.notifySuppServiceFailed(PhoneInternalInterface.SuppService.CONFERENCE);
                imsCall.resetIsMergeRequestedByConf(false);
                ImsPhoneConnection firstConnection = ImsPhoneCallTracker.this.mForegroundCall.getFirstConnection();
                if (firstConnection != null) {
                    firstConnection.onConferenceMergeFailed();
                    firstConnection.handleMergeComplete();
                }
                ImsPhoneConnection firstConnection2 = ImsPhoneCallTracker.this.mBackgroundCall.getFirstConnection();
                if (firstConnection2 != null) {
                    firstConnection2.onConferenceMergeFailed();
                    firstConnection2.handleMergeComplete();
                }
            }

            private void updateConferenceParticipantsTiming(List list) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    ConferenceParticipant conferenceParticipant = (ConferenceParticipant) it.next();
                    CacheEntry cacheEntryFindConnectionTimeUsePhoneNumber = ImsPhoneCallTracker.this.findConnectionTimeUsePhoneNumber(conferenceParticipant);
                    if (cacheEntryFindConnectionTimeUsePhoneNumber != null) {
                        conferenceParticipant.setConnectTime(cacheEntryFindConnectionTimeUsePhoneNumber.mConnectTime);
                        conferenceParticipant.setConnectElapsedTime(cacheEntryFindConnectionTimeUsePhoneNumber.mConnectElapsedTime);
                        conferenceParticipant.setCallDirection(cacheEntryFindConnectionTimeUsePhoneNumber.mCallDirection);
                    }
                }
            }

            public void onConferenceParticipantsStateChanged(ImsCall imsCall, List<ConferenceParticipant> list) {
                ImsPhoneCallTracker.this.log("onConferenceParticipantsStateChanged");
                boolean z = ImsPhoneCallTracker.this.mIsConferenceEventPackageEnabled;
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                if (!z) {
                    imsPhoneCallTracker.logi("onConferenceParticipantsStateChanged - CEP handling disabled");
                    return;
                }
                if (!imsPhoneCallTracker.mSupportCepOnPeer && !imsCall.isConferenceHost()) {
                    ImsPhoneCallTracker.this.logi("onConferenceParticipantsStateChanged - ignore CEP on peer");
                    return;
                }
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    updateConferenceParticipantsTiming(list);
                    imsPhoneConnectionFindConnection.updateConferenceParticipants(list);
                }
            }

            public void onCallSessionTtyModeReceived(ImsCall imsCall, int i) {
                ImsPhoneCallTracker.this.mPhone.onTtyModeReceived(i);
            }

            public void onCallHandover(ImsCall imsCall, int i, int i2, ImsReasonInfo imsReasonInfo) {
                boolean zIsDataEnabled = ImsPhoneCallTracker.this.mPhone.getDefaultPhone().getDataSettingsManager().isDataEnabled();
                ImsPhoneCallTracker.this.log("onCallHandover ::  srcAccessTech=" + i + ", targetAccessTech=" + i2 + ", reasonInfo=" + imsReasonInfo + ", dataEnabled=" + ImsPhoneCallTracker.this.mIsDataEnabled + "/" + zIsDataEnabled + ", dataMetered=" + ImsPhoneCallTracker.this.mIsViLteDataMetered);
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                if (imsPhoneCallTracker.mIsDataEnabled != zIsDataEnabled) {
                    imsPhoneCallTracker.loge("onCallHandover: data enabled state doesn't match! (was=" + ImsPhoneCallTracker.this.mIsDataEnabled + ", actually=" + zIsDataEnabled);
                    ImsPhoneCallTracker.this.mIsDataEnabled = zIsDataEnabled;
                }
                boolean z = false;
                boolean z2 = (i == 0 || i == 18 || i2 != 18) ? false : true;
                if (i == 18 && i2 != 0 && i2 != 18) {
                    z = true;
                }
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    if (imsPhoneConnectionFindConnection.getDisconnectCause() == 0) {
                        if (z2) {
                            ImsPhoneCallTracker.this.removeMessages(25);
                            if (ImsPhoneCallTracker.this.mNotifyHandoverVideoFromLTEToWifi && ImsPhoneCallTracker.this.mHasAttemptedStartOfCallHandover) {
                                imsPhoneConnectionFindConnection.onConnectionEvent("android.telephony.event.EVENT_HANDOVER_VIDEO_FROM_LTE_TO_WIFI", null);
                            }
                            ImsPhoneCallTracker.this.unregisterForConnectivityChanges();
                        } else if (z && imsCall.isVideoCall()) {
                            ImsPhoneCallTracker.this.registerForConnectivityChanges();
                        }
                    }
                    if (z2 && ImsPhoneCallTracker.this.mIsViLteDataMetered) {
                        imsPhoneConnectionFindConnection.setLocalVideoCapable(true);
                    }
                    if (z && imsCall.isVideoCall()) {
                        ImsPhoneCallTracker imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                        if (imsPhoneCallTracker2.mIsViLteDataMetered) {
                            imsPhoneConnectionFindConnection.setLocalVideoCapable(imsPhoneCallTracker2.mIsDataEnabled);
                        }
                        ImsPhoneCallTracker imsPhoneCallTracker3 = ImsPhoneCallTracker.this;
                        if (imsPhoneCallTracker3.mNotifyHandoverVideoFromWifiToLTE && imsPhoneCallTracker3.mIsDataEnabled) {
                            int disconnectCause = imsPhoneConnectionFindConnection.getDisconnectCause();
                            ImsPhoneCallTracker imsPhoneCallTracker4 = ImsPhoneCallTracker.this;
                            if (disconnectCause == 0) {
                                imsPhoneCallTracker4.log("onCallHandover :: notifying of WIFI to LTE handover.");
                                imsPhoneConnectionFindConnection.onConnectionEvent("android.telephony.event.EVENT_HANDOVER_VIDEO_FROM_WIFI_TO_LTE", null);
                            } else {
                                imsPhoneCallTracker4.log("onCallHandover :: skip notify of WIFI to LTE handover for disconnected call.");
                            }
                        }
                        ImsPhoneCallTracker imsPhoneCallTracker5 = ImsPhoneCallTracker.this;
                        if (!imsPhoneCallTracker5.mIsDataEnabled && imsPhoneCallTracker5.mIsViLteDataMetered) {
                            imsPhoneCallTracker5.log("onCallHandover :: data is not enabled; attempt to downgrade.");
                            ImsPhoneCallTracker.this.downgradeVideoCall(1407, imsPhoneConnectionFindConnection);
                        }
                    }
                } else {
                    ImsPhoneCallTracker.this.loge("onCallHandover :: connection null.");
                }
                if (ImsPhoneCallTracker.this.mHasAttemptedStartOfCallHandover) {
                    return;
                }
                ImsPhoneCallTracker.this.mHasAttemptedStartOfCallHandover = true;
            }

            public void onCallHandoverFailed(ImsCall imsCall, int i, int i2, ImsReasonInfo imsReasonInfo) {
                ImsPhoneCallTracker.this.log("onCallHandoverFailed :: srcAccessTech=" + i + ", targetAccessTech=" + i2 + ", reasonInfo=" + imsReasonInfo);
                boolean z = i != 18 && i2 == 18;
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null && z) {
                    ImsPhoneCallTracker.this.log("onCallHandoverFailed - handover to WIFI Failed");
                    ImsPhoneCallTracker.this.removeMessages(25);
                    if (imsCall.isVideoCall() && imsPhoneConnectionFindConnection.getDisconnectCause() == 0) {
                        ImsPhoneCallTracker.this.registerForConnectivityChanges();
                    }
                    if (ImsPhoneCallTracker.this.mNotifyVtHandoverToWifiFail) {
                        imsPhoneConnectionFindConnection.onHandoverToWifiFailed();
                    }
                }
                if (ImsPhoneCallTracker.this.mHasAttemptedStartOfCallHandover) {
                    return;
                }
                ImsPhoneCallTracker.this.mHasAttemptedStartOfCallHandover = true;
            }

            public void onRttModifyRequestReceived(ImsCall imsCall) {
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    imsPhoneConnectionFindConnection.onRttModifyRequestReceived();
                }
            }

            public void onRttModifyResponseReceived(ImsCall imsCall, int i) {
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    imsPhoneConnectionFindConnection.onRttModifyResponseReceived(i);
                }
            }

            public void onRttMessageReceived(ImsCall imsCall, String str) {
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    imsPhoneConnectionFindConnection.onRttMessageReceived(str);
                }
            }

            public void onRttAudioIndicatorChanged(ImsCall imsCall, ImsStreamMediaProfile imsStreamMediaProfile) {
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    imsPhoneConnectionFindConnection.onRttAudioIndicatorChanged(imsStreamMediaProfile);
                }
            }

            public void onCallSessionTransferred(ImsCall imsCall) {
                ImsPhoneCallTracker.this.log("onCallSessionTransferred success");
            }

            public void onCallSessionTransferFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo) {
                ImsPhoneCallTracker.this.log("onCallSessionTransferFailed reasonInfo=" + imsReasonInfo);
                ImsPhoneCallTracker.this.mPhone.notifySuppServiceFailed(PhoneInternalInterface.SuppService.TRANSFER);
            }

            public void onCallSessionDtmfReceived(ImsCall imsCall, char c) {
                ImsPhoneCallTracker.this.log("onCallSessionDtmfReceived digit=" + c);
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    imsPhoneConnectionFindConnection.receivedDtmfDigit(c);
                }
            }

            public void onMultipartyStateChanged(ImsCall imsCall, boolean z) {
                ImsPhoneCallTracker.this.log("onMultipartyStateChanged to ".concat(z ? "Y" : "N"));
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    imsPhoneConnectionFindConnection.updateMultipartyState(z);
                    ImsPhoneCallTracker.this.mPhone.getVoiceCallSessionStats().onMultipartyChange(imsPhoneConnectionFindConnection, z);
                }
            }

            public void onCallQualityChanged(ImsCall imsCall, CallQuality callQuality) {
                ImsPhoneCallTracker.this.mPhone.onCallQualityChanged(callQuality, imsCall.getNetworkType());
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("android.telecom.extra.CALL_QUALITY_REPORT", callQuality);
                    imsPhoneConnectionFindConnection.onConnectionEvent("android.telecom.event.CALL_QUALITY_REPORT", bundle);
                }
            }

            public void onCallSessionRtpHeaderExtensionsReceived(ImsCall imsCall, Set<RtpHeaderExtension> set) {
                ImsPhoneCallTracker.this.log("onCallSessionRtpHeaderExtensionsReceived numExtensions=" + set.size());
                ImsPhoneConnection imsPhoneConnectionFindConnection = ImsPhoneCallTracker.this.findConnection(imsCall);
                if (imsPhoneConnectionFindConnection != null) {
                    imsPhoneConnectionFindConnection.receivedRtpHeaderExtensions(set);
                }
            }

            public void onCallSessionSendAnbrQuery(ImsCall imsCall, int i, int i2, int i3) {
                ImsPhoneCallTracker.this.log("onCallSessionSendAnbrQuery mediaType=" + i + ", direction=" + i2 + ", bitPerSecond=" + i3);
                ImsPhoneCallTracker.this.handleSendAnbrQuery(i, i2, i3);
            }
        };
        this.mImsUssdListener = new ImsCall.Listener() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.9
            public void onCallStarted(ImsCall imsCall) {
                Message message;
                ImsPhoneCallTracker.this.log("mImsUssdListener onCallStarted");
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                if (imsCall != imsPhoneCallTracker.mUssdSession || (message = imsPhoneCallTracker.mPendingUssd) == null) {
                    return;
                }
                AsyncResult.forMessage(message);
                ImsPhoneCallTracker.this.mPendingUssd.sendToTarget();
                ImsPhoneCallTracker.this.mPendingUssd = null;
            }

            public void onCallStartFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo) {
                ImsPhoneCallTracker.this.log("mImsUssdListener onCallStartFailed reasonCode=" + imsReasonInfo.getCode());
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                if (imsPhoneCallTracker.mUssdSession != null) {
                    imsPhoneCallTracker.log("mUssdSession is not null");
                    if (imsReasonInfo.getCode() == 146 && ImsPhoneCallTracker.this.mUssdMethod != 3) {
                        ImsPhoneCallTracker imsPhoneCallTracker2 = ImsPhoneCallTracker.this;
                        imsPhoneCallTracker2.mUssdSession = null;
                        imsPhoneCallTracker2.mPhone.getPendingMmiCodes().clear();
                        ImsPhoneCallTracker.this.mPhone.initiateSilentRedial();
                        ImsPhoneCallTracker.this.log("Initiated sending ussd by using silent redial.");
                        return;
                    }
                    ImsPhoneCallTracker.this.log("Failed to start sending ussd by using silent resendUssd.!!");
                }
                onCallTerminated(imsCall, imsReasonInfo);
            }

            public void onCallTerminated(ImsCall imsCall, ImsReasonInfo imsReasonInfo) {
                ImsPhoneCallTracker.this.log("mImsUssdListener onCallTerminated reasonCode=" + imsReasonInfo.getCode());
                ImsPhoneCallTracker.this.removeMessages(25);
                ImsPhoneCallTracker.this.mHasAttemptedStartOfCallHandover = false;
                ImsPhoneCallTracker.this.unregisterForConnectivityChanges();
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                if (imsCall == imsPhoneCallTracker.mUssdSession) {
                    imsPhoneCallTracker.mUssdSession = null;
                    if (imsPhoneCallTracker.mPendingUssd != null) {
                        if (imsReasonInfo.getCode() == 801) {
                            CommandException.Error error = CommandException.Error.INVALID_RESPONSE;
                        } else {
                            CommandException.Error error2 = CommandException.Error.INVALID_RESPONSE;
                        }
                        AsyncResult.forMessage(ImsPhoneCallTracker.this.mPendingUssd, (Object) null, ImsPhoneCallTracker.this.getImsUssdCommandException(imsReasonInfo));
                        ImsPhoneCallTracker.this.mPendingUssd.sendToTarget();
                        ImsPhoneCallTracker.this.mPendingUssd = null;
                    }
                }
                imsCall.close();
            }

            public void onCallUssdMessageReceived(ImsCall imsCall, int i, String str) {
                int i2;
                ImsPhoneCallTracker.this.log("mImsUssdListener onCallUssdMessageReceived mode=" + i);
                if (i != 0) {
                    i2 = 1;
                    if (i != 1) {
                        i2 = -1;
                    }
                } else {
                    i2 = 0;
                }
                ImsPhoneCallTracker.this.mPhone.onIncomingUSSD(i2, str);
            }
        };
        this.mImsCapabilityCallback = new ImsMmTelManager.CapabilityCallback() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.10
            @Override // android.telephony.ims.ImsMmTelManager.CapabilityCallback
            public void onCapabilitiesStatusChanged(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
                ImsPhoneCallTracker.this.log("onCapabilitiesStatusChanged: " + mmTelCapabilities);
                SomeArgs someArgsObtain = SomeArgs.obtain();
                someArgsObtain.arg1 = mmTelCapabilities;
                ImsPhoneCallTracker.this.removeMessages(26);
                ImsPhoneCallTracker.this.obtainMessage(26, someArgsObtain).sendToTarget();
            }
        };
        this.mImsStatsCallback = new ImsManager.ImsStatsCallback() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.11
            public void onEnabledMmTelCapabilitiesChanged(int i, int i2, boolean z) {
                ImsPhoneCallTracker.this.mPhone.getImsStats().onSetFeatureResponse(i, i2, z ? 1 : 0);
                FeatureFlags unused = ((CallTracker) ImsPhoneCallTracker.this).mFeatureFlags;
                if (i == 1) {
                    if (i2 == 0 || i2 == 1) {
                        if (ImsPhoneCallTracker.this.mImsCapability.indexOfKey(i2) < 0 || ImsPhoneCallTracker.this.mImsCapability.get(i2) != z) {
                            ImsPhoneCallTracker.this.mImsCapability.put(i2, z);
                            ImsPhoneCallTracker.this.updateAllowedServices(i2);
                        }
                    }
                }
            }
        };
        this.mConfigCallback = new ProvisioningManager.Callback() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.12
            public void onProvisioningIntChanged(int i, int i2) {
                queueAndSendProvisioningChanged(new ProvisioningItem(i, i2));
            }

            public void onProvisioningStringChanged(int i, String str) {
                queueAndSendProvisioningChanged(new ProvisioningItem(i, str));
            }

            private void queueAndSendProvisioningChanged(ProvisioningItem provisioningItem) {
                if (!ImsPhoneCallTracker.this.mProvisioningItemQueue.offer(provisioningItem) || ImsPhoneCallTracker.this.hasMessages(36)) {
                    return;
                }
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                imsPhoneCallTracker.sendMessageDelayed(imsPhoneCallTracker.obtainMessage(36), 50L);
            }
        };
        this.mPhone = imsPhone;
        this.mConnectorFactory = connectorFactory;
        if (executor != null) {
            this.mExecutor = executor;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telecom.action.DEFAULT_DIALER_CHANGED");
        this.mPhone.getContext().registerReceiver(broadcastReceiver, intentFilter, 2);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(this.mPhone.getContext().getMainExecutor(), this.mCarrierConfigChangeListener);
            updateCarrierConfiguration(this.mPhone.getSubId(), getCarrierConfigBundle(this.mPhone.getSubId()));
        } else {
            loge("CarrierConfigManager is not available.");
        }
        MiuiImsControllerStub.get(this.mPhone.getPhoneId()).init(this.mPhone, new Object[0]);
        this.mSettingsCallback = new DataSettingsManager.DataSettingsManagerCallback(new ImsPhoneCallTracker$$ExternalSyntheticLambda0(this)) { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.5
            @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
            public void onDataEnabledChanged(boolean z, int i, String str) {
                ImsPhoneCallTracker.this.onDataEnabledChanged(z, i);
            }
        };
        this.mPhone.getDefaultPhone().getDataSettingsManager().registerCallback(this.mSettingsCallback);
        atomicInteger.set(getPackageUid(this.mPhone.getContext(), ((TelecomManager) this.mPhone.getContext().getSystemService("telecom")).getDefaultDialerPackage()));
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        this.mVtDataUsageSnapshot = new NetworkStats(jElapsedRealtime, 1);
        this.mVtDataUsageUidSnapshot = new NetworkStats(jElapsedRealtime, 1);
        ((NetworkStatsManager) this.mPhone.getContext().getSystemService("netstats")).registerNetworkStatsProvider("ImsPhoneCallTracker", vtDataUsageProvider);
        this.mImsManagerConnector = connectorFactory.create(this.mPhone.getContext(), this.mPhone.getPhoneId(), "ImsPhoneCallTracker", new FeatureConnector.Listener<ImsManager>() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.6
            /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.ims.ImsException */
            public void connectionReady(ImsManager imsManager, int i) throws ImsException {
                ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                imsPhoneCallTracker.mImsManager = imsManager;
                imsPhoneCallTracker.log("connectionReady for subId = " + i);
                TelephonyManager telephonyManager = (TelephonyManager) ImsPhoneCallTracker.this.mPhone.getContext().getSystemService(TelephonyManager.class);
                if (telephonyManager != null) {
                    ImsPhoneCallTracker.this.mTelephonyManager = telephonyManager.createForSubscriptionId(i);
                }
                ImsPhoneCallTracker.this.startListeningForCalls(i);
            }

            public void connectionUnavailable(int i) {
                ImsPhoneCallTracker.this.logi("connectionUnavailable: " + i);
                if (i == 3) {
                    ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                    imsPhoneCallTracker.postDelayed(imsPhoneCallTracker.mConnectorRunnable, 5000L);
                }
                ImsPhoneCallTracker.this.stopListeningForCalls();
                ImsPhoneCallTracker.this.stopAllImsTrafficTypes();
            }
        }, executor);
        post(this.mConnectorRunnable);
        this.mImsCallInfoTracker = new ImsCallInfoTracker(imsPhone);
        this.mPhone.registerForConnectionSetupFailure(this, 34, null);
    }

    public void setSharedPreferenceProxy(SharedPreferenceProxy sharedPreferenceProxy) {
        this.mSharedPreferenceProxy = sharedPreferenceProxy;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPackageUid(Context context, String str) {
        if (str == null) {
            return -1;
        }
        try {
            return context.getPackageManager().getPackageUid(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            loge("Cannot find package uid. pkg = ".concat(str));
            return -1;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.ims.ImsException */
    public void startListeningForCalls(int i) throws ImsException {
        log("startListeningForCalls");
        this.mOperationLocalLog.log("startListeningForCalls - Connecting to ImsService");
        if (this.mTelephonyManager != null && SubscriptionManager.isValidSubscriptionId(i)) {
            this.mCarrierRoamingNtnListener = new CarrierRoamingNtnListener();
            this.mTelephonyManager.registerTelephonyCallback(new ImsPhoneCallTracker$$ExternalSyntheticLambda0(this), this.mCarrierRoamingNtnListener);
        }
        ImsExternalCallTracker externalCallTracker = this.mPhone.getExternalCallTracker();
        this.mImsManager.open(this.mMmTelFeatureListener, this.mPhone.getImsEcbmStateListener(), externalCallTracker != null ? externalCallTracker.getExternalCallStateListener() : null);
        this.mImsManager.addRegistrationCallback(this.mPhone.getImsMmTelRegistrationCallback(), new ImsPhoneCallTracker$$ExternalSyntheticLambda0(this));
        try {
            this.mImsManager.addEmergencyRegistrationCallbackForSubscription(this.mPhone.getImsMmTelEmergencyRegistrationCallback().getBinder(), i);
        } catch (RemoteException unused) {
            throw new ImsException("addEmergencyRegistrationCallbackForSubscription failed", 106);
        } catch (IllegalStateException e) {
            loge("startListeningForCalls - add callback fails due to sub Id changes: " + e);
        }
        this.mImsManager.addCapabilitiesCallback(this.mImsCapabilityCallback, new ImsPhoneCallTracker$$ExternalSyntheticLambda0(this));
        ImsManager.setImsStatsCallback(this.mPhone.getPhoneId(), this.mImsStatsCallback);
        this.mImsManager.getConfigInterface().addConfigCallback(this.mConfigCallback);
        if (this.mPhone.isInEcm()) {
            this.mPhone.exitEmergencyCallbackMode();
        }
        this.mImsManager.setUiTTYMode(this.mPhone.getContext(), Settings.Secure.getInt(this.mPhone.getContext().getContentResolver(), "preferred_tty_mode", 0), (Message) null);
        ImsUtInterface utInterface = getUtInterface();
        this.mUtInterface = utInterface;
        if (utInterface != null) {
            utInterface.registerForSuppServiceIndication(this, 27, (Object) null);
        }
        Pair pair = this.mCarrierConfigForSubId;
        if (pair != null && ((Integer) pair.first).intValue() == i) {
            updateCarrierConfiguration(i, (PersistableBundle) this.mCarrierConfigForSubId.second);
        } else {
            log("startListeningForCalls - waiting for the first carrier config indication for this subscription");
        }
        sendImsServiceStateIntent("com.android.ims.IMS_SERVICE_UP");
        this.mCurrentlyConnectedSubId = Optional.of(Integer.valueOf(i));
        initializeTerminalBasedCallWaiting();
    }

    private void maybeConfigureRtpHeaderExtensions() {
        Config config;
        if (this.mDeviceToDeviceForceEnabled || ((config = this.mConfig) != null && config.isD2DCommunicationSupported && this.mSupportD2DUsingRtp)) {
            ArraySet arraySet = new ArraySet();
            if (this.mSupportSdpForRtpHeaderExtensions) {
                arraySet.add(RtpTransport.CALL_STATE_RTP_HEADER_EXTENSION_TYPE);
                arraySet.add(RtpTransport.DEVICE_STATE_RTP_HEADER_EXTENSION_TYPE);
                logi("maybeConfigureRtpHeaderExtensions: set offered RTP header extension types");
            } else {
                logi("maybeConfigureRtpHeaderExtensions: SDP negotiation not supported; not setting offered RTP header extension types");
            }
            try {
                this.mImsManager.setOfferedRtpHeaderExtensionTypes(arraySet);
            } catch (ImsException e) {
                loge("maybeConfigureRtpHeaderExtensions: failed to set extensions; " + e);
            }
        }
    }

    public void setDeviceToDeviceForceEnabled(boolean z) {
        this.mDeviceToDeviceForceEnabled = z;
        maybeConfigureRtpHeaderExtensions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopListeningForCalls() {
        CarrierRoamingNtnListener carrierRoamingNtnListener;
        log("stopListeningForCalls");
        this.mOperationLocalLog.log("stopListeningForCalls - Disconnecting from ImsService");
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager != null && (carrierRoamingNtnListener = this.mCarrierRoamingNtnListener) != null) {
            telephonyManager.unregisterTelephonyCallback(carrierRoamingNtnListener);
        }
        ImsManager imsManager = this.mImsManager;
        if (imsManager != null) {
            imsManager.removeRegistrationListener(this.mPhone.getImsMmTelRegistrationCallback());
            this.mImsManager.removeEmergencyRegistrationCallbackForSubscription(this.mPhone.getImsMmTelEmergencyRegistrationCallback().getBinder(), this.mPhone.getSubId());
            this.mImsManager.removeCapabilitiesCallback(this.mImsCapabilityCallback);
            try {
                ImsManager.setImsStatsCallback(this.mPhone.getPhoneId(), (ImsManager.ImsStatsCallback) null);
                this.mImsManager.getConfigInterface().removeConfigCallback(this.mConfigCallback.getBinder());
            } catch (ImsException unused) {
                Log.w("ImsPhoneCallTracker", "stopListeningForCalls: unable to remove config callback.");
            }
            this.mImsManager.close();
        }
        ImsUtInterface imsUtInterface = this.mUtInterface;
        if (imsUtInterface != null) {
            imsUtInterface.unregisterForSuppServiceIndication(this);
            this.mUtInterface = null;
        }
        this.mCurrentlyConnectedSubId = Optional.empty();
        this.mMediaThreshold = null;
        clearAllowedServices();
        resetImsCapabilities();
        hangupAllOrphanedConnections(14);
        sendImsServiceStateIntent("com.android.ims.IMS_SERVICE_DOWN");
        ((IHyperSsManager) HyperTelephonyFactory.getInstance().initModule(IHyperSsManager.DEFAULT, new Object[0])).unRegisterForCallBarringPwdRequiredChanged(this.mPhone.getPhoneId());
    }

    public void hangupAllOrphanedConnections(int i) {
        Log.w("ImsPhoneCallTracker", "hangupAllOngoingConnections called for cause " + i);
        int size = getConnections().size();
        while (true) {
            size--;
            if (size <= -1) {
                break;
            }
            try {
                getConnections().get(size).hangup();
            } catch (CallStateException unused) {
                loge("Failed to disconnet call...");
            }
        }
        ArrayList<ImsPhoneConnection> arrayList = this.mConnections;
        int size2 = arrayList.size();
        int i2 = 0;
        while (i2 < size2) {
            ImsPhoneConnection imsPhoneConnection = arrayList.get(i2);
            i2++;
            ImsPhoneConnection imsPhoneConnection2 = imsPhoneConnection;
            imsPhoneConnection2.update(imsPhoneConnection2.getImsCall(), Call.State.DISCONNECTED);
            imsPhoneConnection2.onDisconnect(i);
            imsPhoneConnection2.getCall().detach(imsPhoneConnection2);
        }
        this.mConnections.clear();
        this.mPendingMO = null;
        updatePhoneState();
        this.mImsCallInfoTracker.clearAllOrphanedConnections();
    }

    public void hangupAllConnections() {
        getConnections().stream().forEach(new Consumer() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$hangupAllConnections$1((ImsPhoneConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hangupAllConnections$1(ImsPhoneConnection imsPhoneConnection) {
        logi("Disconnecting callId = " + imsPhoneConnection.getTelecomCallId());
        try {
            imsPhoneConnection.hangup();
        } catch (CallStateException unused) {
            loge("Failed to disconnet call...");
        }
    }

    private void sendImsServiceStateIntent(String str) {
        Intent intent = new Intent(str);
        intent.putExtra("android:phone_id", this.mPhone.getPhoneId());
        ImsPhone imsPhone = this.mPhone;
        if (imsPhone == null || imsPhone.getContext() == null) {
            return;
        }
        this.mPhone.getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    public void dispose() {
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener;
        log("dispose");
        this.mRingingCall.dispose();
        this.mBackgroundCall.dispose();
        this.mForegroundCall.dispose();
        this.mHandoverCall.dispose();
        clearDisconnected();
        this.mPhone.getContext().unregisterReceiver(this.mReceiver);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null && (carrierConfigChangeListener = this.mCarrierConfigChangeListener) != null) {
            carrierConfigManager.unregisterCarrierConfigChangeListener(carrierConfigChangeListener);
        }
        this.mPhone.getDefaultPhone().getDataSettingsManager().unregisterCallback(this.mSettingsCallback);
        this.mImsManagerConnector.disconnect();
        ((NetworkStatsManager) this.mPhone.getContext().getSystemService("netstats")).unregisterNetworkStatsProvider(this.mVtDataUsageProvider);
        this.mPhone.unregisterForConnectionSetupFailure(this);
        doUnregisterWhenDispose();
        MiuiImsControllerStub.get(this.mPhone.getPhoneId()).dispose(this.mPhone);
        MiuiImsControllerStub.get(this.mPhone.getPhoneId()).sendIMSRegistedStateBroadcast(false, this.mPhone.getDefaultPhone());
    }

    protected void finalize() {
        log("ImsPhoneCallTracker finalized");
    }

    @Override // com.android.internal.telephony.CallTracker
    public void registerForVoiceCallStarted(Handler handler, int i, Object obj) {
        this.mVoiceCallStartedRegistrants.add(new Registrant(handler, i, obj));
    }

    @Override // com.android.internal.telephony.CallTracker
    public void unregisterForVoiceCallStarted(Handler handler) {
        this.mVoiceCallStartedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CallTracker
    public void registerForVoiceCallEnded(Handler handler, int i, Object obj) {
        this.mVoiceCallEndedRegistrants.add(new Registrant(handler, i, obj));
    }

    @Override // com.android.internal.telephony.CallTracker
    public void unregisterForVoiceCallEnded(Handler handler) {
        this.mVoiceCallEndedRegistrants.remove(handler);
    }

    public int getClirMode() {
        if (this.mSharedPreferenceProxy != null && this.mPhone.getDefaultPhone() != null) {
            return this.mSharedPreferenceProxy.getDefaultSharedPreferences(this.mPhone.getContext()).getInt(Phone.CLIR_KEY + this.mPhone.getSubId(), 0);
        }
        loge("dial; could not get default CLIR mode.");
        return 0;
    }

    /* JADX WARN: Code duplicated, block: B:26:0x0064  */
    protected boolean prepareForDialing(ImsPhone.ImsDialArgs imsDialArgs) throws CallStateException {
        boolean z;
        boolean z2 = imsDialArgs.isEmergency;
        clearDisconnected();
        boolean z3 = false;
        if (this.mImsManager == null) {
            CallManager$$ExternalSyntheticBUOutline0.m("service not available");
            return false;
        }
        checkForDialIssues(false);
        int i = imsDialArgs.videoState;
        if (!canAddVideoCallDuringImsAudioCall(i)) {
            CallManager$$ExternalSyntheticBUOutline0.m("cannot dial in current state");
            return false;
        }
        checkforCsfb();
        checkforPendingResume();
        Call.State state = this.mForegroundCall.getState();
        Call.State state2 = Call.State.ACTIVE;
        if (state != state2) {
            z = false;
        } else {
            if (this.mBackgroundCall.getState() != Call.State.IDLE) {
                Phone$$ExternalSyntheticBUOutline2.m(6, "Already too many ongoing calls.");
                return false;
            }
            this.mPendingCallVideoState = i;
            this.mPendingIntentExtras = imsDialArgs.intentExtras;
            holdActiveCallForPendingMo();
            z = true;
        }
        synchronized (this.mSyncHold) {
            if (z) {
                try {
                    Call.State state3 = this.mForegroundCall.getState();
                    Call.State state4 = this.mBackgroundCall.getState();
                    if (state3 == state2) {
                        throw new CallStateException("cannot dial in current state");
                    }
                    if (state4 != Call.State.HOLDING) {
                        z3 = z;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                z3 = z;
            }
        }
        return z3;
    }

    public Connection startConference(String[] strArr, ImsPhone.ImsDialArgs imsDialArgs) throws CallStateException {
        ImsPhoneConnection imsPhoneConnectionMakeImsPhoneConnectionForConference;
        int i = imsDialArgs.clirMode;
        int i2 = imsDialArgs.videoState;
        log("dial clirMode=" + i);
        boolean zPrepareForDialing = prepareForDialing(imsDialArgs);
        this.mClirMode = i;
        synchronized (this.mSyncHold) {
            try {
                this.mLastDialArgs = imsDialArgs;
                imsPhoneConnectionMakeImsPhoneConnectionForConference = makeImsPhoneConnectionForConference(strArr);
                this.mPendingMO = imsPhoneConnectionMakeImsPhoneConnectionForConference;
                imsPhoneConnectionMakeImsPhoneConnectionForConference.setVideoState(i2);
                if (imsDialArgs.rttTextStream != null) {
                    log("startConference: setting RTT stream on mPendingMO");
                    imsPhoneConnectionMakeImsPhoneConnectionForConference.setCurrentRttTextStream(imsDialArgs.rttTextStream);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        addConnection(imsPhoneConnectionMakeImsPhoneConnectionForConference);
        if (!zPrepareForDialing) {
            dialInternal(imsPhoneConnectionMakeImsPhoneConnectionForConference, i, i2, imsDialArgs.intentExtras);
        }
        updatePhoneState();
        this.mPhone.notifyPreciseCallStateChanged();
        return imsPhoneConnectionMakeImsPhoneConnectionForConference;
    }

    public Connection dial(String str, int i, Bundle bundle) throws CallStateException {
        return dial(str, new ImsPhone.ImsDialArgs.Builder().setIntentExtras(bundle).setVideoState(i).setClirMode(getClirMode()).build());
    }

    public synchronized Connection dial(String str, final ImsPhone.ImsDialArgs imsDialArgs) throws CallStateException {
        String strConvertNumberIfNecessary;
        ImsPhoneConnection imsPhoneConnection;
        try {
            boolean zIsPhoneInEcbMode = isPhoneInEcbMode();
            boolean zIsEmergencyNumber = imsDialArgs.isEmergency;
            boolean z = imsDialArgs.isWpsCall;
            if (TelephonyBaseUtilsStub.isMiuiRom()) {
                zIsEmergencyNumber = PhoneNumberUtils.isEmergencyNumber(this.mPhone.getSubId(), str);
            }
            boolean z2 = zIsEmergencyNumber;
            if (!shouldNumberBePlacedOnIms(z2, str)) {
                Rlog.i("ImsPhoneCallTracker", "dial: shouldNumberBePlacedOnIms = false");
                this.mOperationLocalLog.log("dial: shouldNumberBePlacedOnIms = false");
                throw new CallStateException(Phone.CS_FALLBACK);
            }
            int i = imsDialArgs.clirMode;
            final int i2 = imsDialArgs.videoState;
            log("dial clirMode=" + i);
            if (z2 && ignoreClirWhenEcc()) {
                log("dial emergency call, set clirModIe=2");
                i = 2;
                strConvertNumberIfNecessary = str;
            } else {
                strConvertNumberIfNecessary = convertNumberIfNecessary(this.mPhone, str);
            }
            final int clirModeForEmergencyCall = MiuiImsControllerStub.get(this.mPhone.getPhoneId()).getClirModeForEmergencyCall(i, this.mPhone, z2, getClirMode());
            this.mClirMode = clirModeForEmergencyCall;
            boolean zPrepareForDialing = prepareForDialing(imsDialArgs);
            if (zIsPhoneInEcbMode && z2) {
                this.mPhone.handleTimerInEmergencyCallbackMode(1);
            }
            if (z2 && VideoProfile.isVideo(i2) && !this.mAllowEmergencyVideoCalls) {
                loge("dial: carrier does not support video emergency calls; downgrade to audio-only");
                i2 = 0;
            }
            this.mPendingCallVideoState = i2;
            synchronized (this.mSyncHold) {
                try {
                    this.mLastDialString = strConvertNumberIfNecessary;
                    this.mLastDialArgs = imsDialArgs;
                    this.mPendingMO = makeImsPhoneConnectionForMO(strConvertNumberIfNecessary, z2, z, imsDialArgs);
                    this.mOperationLocalLog.log("dial requested. connId=" + System.identityHashCode(this.mPendingMO));
                    if (z2 && imsDialArgs.intentExtras != null) {
                        Rlog.i("ImsPhoneCallTracker", "dial ims emergency dialer: " + imsDialArgs.intentExtras.getBoolean("android.telecom.extra.IS_USER_INTENT_EMERGENCY_CALL"));
                        this.mPendingMO.setHasKnownUserIntentEmergency(imsDialArgs.intentExtras.getBoolean("android.telecom.extra.IS_USER_INTENT_EMERGENCY_CALL"));
                    }
                    imsPhoneConnection = this.mPendingMO;
                    imsPhoneConnection.setVideoState(i2);
                    if (imsDialArgs.rttTextStream != null) {
                        log("dial: setting RTT stream on mPendingMO");
                        this.mPendingMO.setCurrentRttTextStream(imsDialArgs.rttTextStream);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            addConnection(this.mPendingMO);
            if (!zPrepareForDialing) {
                if (!zIsPhoneInEcbMode || (zIsPhoneInEcbMode && z2)) {
                    dialInternal(this.mPendingMO, clirModeForEmergencyCall, i2, imsDialArgs.retryCallFailCause, imsDialArgs.retryCallFailNetworkType, imsDialArgs.intentExtras);
                } else if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
                    EmergencyStateTracker.getInstance().exitEmergencyCallbackMode(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker.7
                        @Override // java.lang.Runnable
                        public void run() {
                            ImsPhoneCallTracker imsPhoneCallTracker = ImsPhoneCallTracker.this;
                            ImsPhoneConnection imsPhoneConnection2 = imsPhoneCallTracker.mPendingMO;
                            int i3 = clirModeForEmergencyCall;
                            int i4 = i2;
                            ImsPhone.ImsDialArgs imsDialArgs2 = imsDialArgs;
                            imsPhoneCallTracker.dialInternal(imsPhoneConnection2, i3, i4, imsDialArgs2.retryCallFailCause, imsDialArgs2.retryCallFailNetworkType, imsDialArgs2.intentExtras);
                        }
                    }, 1);
                } else {
                    try {
                        getEcbmInterface().exitEmergencyCallbackMode();
                        this.mPhone.setOnEcbModeExitResponse(this, 14, null);
                        this.pendingCallClirMode = clirModeForEmergencyCall;
                        this.mPendingCallVideoState = i2;
                        this.mPendingIntentExtras = imsDialArgs.intentExtras;
                        this.pendingCallInEcm = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new CallStateException("service not available");
                    }
                }
            }
            if (this.mNumberConverted) {
                this.mPendingMO.restoreDialedNumberAfterConversion(str);
                this.mNumberConverted = false;
            }
            updatePhoneState();
            this.mPhone.notifyPreciseCallStateChanged();
            switchWfcModeIfRequired(this.mImsManager, isVowifiEnabled(), z2);
        } catch (Throwable th2) {
            throw th2;
        }
        return imsPhoneConnection;
    }

    boolean isImsServiceReady() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            return false;
        }
        return imsManager.isServiceReady();
    }

    private boolean shouldNumberBePlacedOnIms(boolean z, String str) {
        try {
            ImsManager imsManager = this.mImsManager;
            if (imsManager == null) {
                Rlog.w("ImsPhoneCallTracker", "ImsManager unavailable, shouldProcessCall returning false.");
                return false;
            }
            int iShouldProcessCall = imsManager.shouldProcessCall(z, new String[]{str});
            Rlog.i("ImsPhoneCallTracker", "shouldProcessCall: number: " + Rlog.pii("ImsPhoneCallTracker", str) + ", result: " + iShouldProcessCall);
            if (iShouldProcessCall == 0) {
                return true;
            }
            if (iShouldProcessCall != 1) {
                Rlog.w("ImsPhoneCallTracker", "shouldProcessCall returned unknown result.");
                return false;
            }
            Rlog.i("ImsPhoneCallTracker", "shouldProcessCall: place over CSFB instead.");
            return false;
        } catch (ImsException unused) {
            Rlog.w("ImsPhoneCallTracker", "ImsService unavailable, shouldProcessCall returning false.");
            return false;
        }
    }

    protected void updateCarrierConfiguration(int i, PersistableBundle persistableBundle) {
        IccCardConstants.State state;
        this.mCarrierConfigLoadedForSubscription = false;
        if (persistableBundle == null) {
            loge("updateCarrierConfiguration: carrier config is null, skipping.");
            return;
        }
        updateCarrierConfigCache(persistableBundle);
        log("updateCarrierConfiguration: Updating mAllowEmergencyVideoCalls = " + this.mAllowEmergencyVideoCalls);
        maybeConfigureRtpHeaderExtensions();
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null || !subscriptionInfoInternal.isActive()) {
            loge("updateCarrierConfiguration: skipping notification to ImsService, nonactive subId = " + i);
            return;
        }
        Phone defaultPhone = getPhone().getDefaultPhone();
        if (defaultPhone != null && defaultPhone.getIccCard() != null && (state = defaultPhone.getIccCard().getState()) != null && (!state.iccCardExist() || state.isPinLocked())) {
            loge("updateCarrierConfiguration: card state is not ready, skipping notification to ImsService. State= " + state);
        } else {
            if (!CarrierConfigManager.isConfigForIdentifiedCarrier(persistableBundle)) {
                logi("updateCarrierConfiguration: Empty or default carrier config, skipping notification to ImsService.");
                return;
            }
            logi("updateCarrierConfiguration: Updating ImsService configs.");
            this.mCarrierConfigLoadedForSubscription = true;
            updateImsServiceConfig();
            updateMediaThreshold(this.mThresholdRtpPacketLoss, this.mThresholdRtpJitter, this.mThresholdRtpInactivityTime);
            updateAllowedServices(0);
            updateAllowedServices(1);
        }
    }

    public void updateCarrierConfigCache(PersistableBundle persistableBundle) {
        this.mAllowEmergencyVideoCalls = persistableBundle.getBoolean("allow_emergency_video_calls_bool");
        this.mTreatDowngradedVideoCallsAsVideoCalls = persistableBundle.getBoolean("treat_downgraded_video_calls_as_video_calls_bool");
        this.mDropVideoCallWhenAnsweringAudioCall = persistableBundle.getBoolean("drop_video_call_when_answering_audio_call_bool");
        this.mAllowAddCallDuringVideoCall = persistableBundle.getBoolean("allow_add_call_during_video_call");
        this.mAllowHoldingVideoCall = persistableBundle.getBoolean("allow_hold_video_call_bool");
        this.mNotifyVtHandoverToWifiFail = persistableBundle.getBoolean("notify_vt_handover_to_wifi_failure_bool");
        this.mSupportDowngradeVtToAudio = persistableBundle.getBoolean("support_downgrade_vt_to_audio_bool");
        this.mNotifyHandoverVideoFromWifiToLTE = persistableBundle.getBoolean("notify_handover_video_from_wifi_to_lte_bool");
        this.mNotifyHandoverVideoFromLTEToWifi = persistableBundle.getBoolean("notify_handover_video_from_lte_to_wifi_bool");
        this.mIgnoreDataEnabledChangedForVideoCalls = persistableBundle.getBoolean("ignore_data_enabled_changed_for_video_calls");
        this.mIsViLteDataMetered = persistableBundle.getBoolean("vilte_data_is_metered_bool");
        this.mSupportPauseVideo = persistableBundle.getBoolean("support_pause_ims_video_calls_bool");
        this.mAlwaysPlayRemoteHoldTone = persistableBundle.getBoolean("always_play_remote_hold_tone_bool");
        this.mAutoRetryFailedWifiEmergencyCall = persistableBundle.getBoolean("auto_retry_failed_wifi_emergency_call");
        this.mSupportCepOnPeer = persistableBundle.getBoolean("support_ims_conference_event_package_on_peer_bool");
        this.mSupportD2DUsingRtp = persistableBundle.getBoolean("supports_device_to_device_communication_using_rtp_bool");
        this.mSupportSdpForRtpHeaderExtensions = persistableBundle.getBoolean("supports_sdp_negotiation_of_d2d_rtp_header_extensions_bool");
        this.mVolteRoamingSupported = persistableBundle.getBoolean("imsvoice.carrier_volte_roaming_available_bool");
        this.mThresholdRtpPacketLoss = persistableBundle.getInt("imsvoice.rtp_packet_loss_rate_threshold_int");
        this.mThresholdRtpInactivityTime = persistableBundle.getLong("imsvoice.rtp_inactivity_time_threshold_millis_long");
        this.mThresholdRtpJitter = persistableBundle.getInt("imsvoice.rtp_jitter_threshold_millis_int");
        this.mShouldAllowVtCallsInLowBattery = persistableBundle.getBoolean("allow_video_call_in_low_battery_bool");
        this.mAllowHoldingCall = persistableBundle.getBoolean("allow_hold_in_ims_call");
        if (this.mPhone.getContext().getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromUnplug)) {
            this.mUssdMethod = persistableBundle.getInt("carrier_ussd_method_int");
        }
        if (!this.mImsReasonCodeMap.isEmpty()) {
            this.mImsReasonCodeMap.clear();
        }
        String[] stringArray = persistableBundle.getStringArray("ims_reasoninfo_mapping_string_array");
        if (stringArray != null && stringArray.length > 0) {
            for (String str : stringArray) {
                String[] strArrSplit = str.split(Pattern.quote("|"));
                if (strArrSplit.length == 3) {
                    try {
                        String str2 = null;
                        Integer numValueOf = strArrSplit[0].equals("*") ? null : Integer.valueOf(Integer.parseInt(strArrSplit[0]));
                        String str3 = strArrSplit[1];
                        if (str3 == null) {
                            str2 = "";
                        } else if (!str3.equals("*")) {
                            str2 = str3;
                        }
                        int i = Integer.parseInt(strArrSplit[2]);
                        addReasonCodeRemapping(numValueOf, str2, Integer.valueOf(i));
                        StringBuilder sb = new StringBuilder();
                        sb.append("Loaded ImsReasonInfo mapping : fromCode = ");
                        Object obj = numValueOf;
                        if (numValueOf == null) {
                            obj = "any";
                        }
                        sb.append(obj);
                        sb.append(" ; message = ");
                        if (str2 == null) {
                            str2 = "any";
                        }
                        sb.append(str2);
                        sb.append(" ; toCode = ");
                        sb.append(i);
                        log(sb.toString());
                    } catch (NumberFormatException unused) {
                        loge("Invalid ImsReasonInfo mapping found: ".concat(str));
                    }
                }
            }
        } else {
            log("No carrier ImsReasonInfo mappings defined.");
        }
        this.mSrvccTypeSupported.clear();
        int[] intArray = persistableBundle.getIntArray("imsvoice.srvcc_type_int_array");
        if (intArray != null && intArray.length > 0) {
            this.mSrvccTypeSupported.addAll((Collection) Arrays.stream(intArray).boxed().collect(Collectors.toList()));
        }
        this.mAllowRttWhileRoaming = persistableBundle.getBoolean("rtt_supported_while_roaming_bool");
    }

    private void updateMediaThreshold(int i, int i2, long j) {
        if (!MediaThreshold.isValidRtpInactivityTimeMillis(j) && !MediaThreshold.isValidJitterMillis(i2) && !MediaThreshold.isValidRtpPacketLossRate(i)) {
            logi("There is no valid RTP threshold value");
            return;
        }
        MediaThreshold mediaThresholdBuild = new MediaThreshold.Builder().setThresholdsRtpPacketLossRate(new int[]{i}).setThresholdsRtpInactivityTimeMillis(new long[]{j}).setThresholdsRtpJitterMillis(new int[]{i2}).build();
        MediaThreshold mediaThreshold = this.mMediaThreshold;
        if (mediaThreshold == null || !mediaThreshold.equals(mediaThresholdBuild)) {
            logi("setMediaThreshold :" + mediaThresholdBuild);
            try {
                this.mImsManager.setMediaThreshold(1, mediaThresholdBuild);
                this.mMediaThreshold = mediaThresholdBuild;
            } catch (ImsException e) {
                loge("setMediaThreshold Failed: " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:13:0x0031  */
    public void updateAllowedServices(int i) {
        if (this.mImsCapability.indexOfKey(i) < 0) {
            return;
        }
        boolean z = this.mImsCapability.get(i);
        boolean z2 = false;
        if (i != 0) {
            if (i == 1) {
                boolean zIsWfcRoamingEnabledByUser = this.mImsManager.isWfcRoamingEnabledByUser();
                if (z && zIsWfcRoamingEnabledByUser) {
                    z2 = true;
                }
            } else {
                logw("Unhandled registration technology = " + i);
                return;
            }
        } else if (z && this.mVolteRoamingSupported) {
            z2 = true;
        }
        log("updateAllowedServices for RAT = " + i + ", enabled = " + z + ", enabledForRoaming = " + z2);
        this.mPhone.setAllowedImsServices(i, z, z2 ^ true);
    }

    private void clearAllowedServices() {
        log("clearAllowedServices");
        this.mImsCapability.clear();
        this.mVolteRoamingSupported = false;
    }

    protected void dialInternal(ImsPhoneConnection imsPhoneConnection, int i, int i2, Bundle bundle) {
        dialInternal(imsPhoneConnection, i, i2, 0, 0, bundle);
    }

    protected void dialInternal(ImsPhoneConnection imsPhoneConnection, int i, int i2, int i3, int i4, Bundle bundle) {
        if (imsPhoneConnection == null) {
            return;
        }
        if (!imsPhoneConnection.isAdhocConference() && (imsPhoneConnection.getAddress() == null || imsPhoneConnection.getAddress().length() == 0 || imsPhoneConnection.getAddress().indexOf(78) >= 0)) {
            imsPhoneConnection.setDisconnectCause(7);
            sendEmptyMessageDelayed(18, 500L);
            return;
        }
        setMute(false);
        boolean zIsEmergencyNumber = PhoneNumberUtils.isEmergencyNumber(this.mPhone.getSubId(), imsPhoneConnection.getAddress());
        int i5 = zIsEmergencyNumber ? 2 : 1;
        int callTypeFromVideoState = ImsCallProfile.getCallTypeFromVideoState(i2);
        imsPhoneConnection.setVideoState(i2);
        try {
            String[] participantsToDial = {imsPhoneConnection.getAddress()};
            ImsCallProfile imsCallProfileCreateCallProfile = this.mImsManager.createCallProfile(i5, callTypeFromVideoState);
            if (imsPhoneConnection.isAdhocConference()) {
                imsCallProfileCreateCallProfile.setCallExtraBoolean("android.telephony.ims.extra.CONFERENCE", true);
                imsCallProfileCreateCallProfile.setCallExtraBoolean("conference", true);
            }
            imsCallProfileCreateCallProfile.setCallExtraInt("oir", i);
            imsCallProfileCreateCallProfile.setCallExtraInt("android.telephony.ims.extra.RETRY_CALL_FAIL_REASON", i3);
            imsCallProfileCreateCallProfile.setCallExtraInt("android.telephony.ims.extra.RETRY_CALL_FAIL_NETWORKTYPE", i4);
            if (zIsEmergencyNumber) {
                setEmergencyCallInfo(imsCallProfileCreateCallProfile, imsPhoneConnection);
            }
            if (bundle != null) {
                if (bundle.containsKey("android.telecom.extra.CALL_SUBJECT")) {
                    bundle.putString("DisplayText", cleanseInstantLetteringMessage(bundle.getString("android.telecom.extra.CALL_SUBJECT")));
                    imsCallProfileCreateCallProfile.setCallExtra("android.telephony.ims.extra.CALL_SUBJECT", bundle.getString("android.telecom.extra.CALL_SUBJECT"));
                }
                log("dialInternal: isSettingStartRttCall = false");
                if (bundle.containsKey("android.telecom.extra.PRIORITY")) {
                    imsCallProfileCreateCallProfile.setCallExtraInt("android.telephony.ims.extra.PRIORITY", bundle.getInt("android.telecom.extra.PRIORITY"));
                }
                if (bundle.containsKey("android.telecom.extra.LOCATION")) {
                    imsCallProfileCreateCallProfile.setCallExtraParcelable("android.telephony.ims.extra.LOCATION", bundle.getParcelable("android.telecom.extra.LOCATION"));
                }
                if (bundle.containsKey("android.telecom.extra.OUTGOING_PICTURE")) {
                    imsCallProfileCreateCallProfile.setCallExtra("android.telephony.ims.extra.PICTURE_URL", TelephonyLocalConnection.getCallComposerServerUrlForHandle(this.mPhone.getSubId(), ((ParcelUuid) bundle.getParcelable("android.telecom.extra.OUTGOING_PICTURE")).getUuid()));
                }
                if (imsPhoneConnection.hasRttTextStream()) {
                    imsCallProfileCreateCallProfile.mMediaProfile.mRttMode = 1;
                }
                if (bundle.containsKey("CallPull")) {
                    imsCallProfileCreateCallProfile.mCallExtras.putBoolean("CallPull", bundle.getBoolean("CallPull"));
                    int i6 = bundle.getInt(ImsExternalCallTracker.EXTRA_IMS_EXTERNAL_CALL_ID);
                    imsPhoneConnection.setIsPulledCall(true);
                    imsPhoneConnection.setPulledDialogId(i6);
                }
                if (bundle.containsKey("CallRadioTech")) {
                    logi("dialInternal containing EXTRA_CALL_RAT_TYPE, " + bundle.getString("CallRadioTech"));
                }
                imsCallProfileCreateCallProfile.mCallExtras.putBundle("android.telephony.ims.extra.OEM_EXTRAS", bundle);
            }
            this.mPhone.getVoiceCallSessionStats().onImsDial(imsPhoneConnection);
            ImsManager imsManager = this.mImsManager;
            if (imsPhoneConnection.isAdhocConference()) {
                participantsToDial = imsPhoneConnection.getParticipantsToDial();
            }
            ImsCall imsCallMakeCall = imsManager.makeCall(imsCallProfileCreateCallProfile, participantsToDial, this.mImsCallListener);
            imsPhoneConnection.setImsCall(imsCallMakeCall);
            setVideoCallProvider(imsPhoneConnection, imsCallMakeCall);
            imsPhoneConnection.setAllowAddCallDuringVideoCall(this.mAllowAddCallDuringVideoCall);
            imsPhoneConnection.setAllowHoldingVideoCall(this.mAllowHoldingVideoCall);
            this.mImsCallInfoTracker.addImsCallStatus(imsPhoneConnection);
        } catch (RemoteException unused) {
        } catch (ImsException e) {
            loge("dialInternal : " + e);
            this.mOperationLocalLog.log("dialInternal exception: " + e);
            imsPhoneConnection.setDisconnectCause(36);
            sendEmptyMessageDelayed(18, 500L);
        }
    }

    public void acceptCall(int i) throws CallStateException {
        log("acceptCall");
        this.mOperationLocalLog.log("accepted incoming call");
        if (this.mForegroundCall.getState().isAlive() && this.mBackgroundCall.getState().isAlive()) {
            CallManager$$ExternalSyntheticBUOutline0.m("cannot accept call");
            return;
        }
        new ImsStreamMediaProfile();
        boolean zShouldDisconnectActiveCallOnAnswer = false;
        if (this.mRingingCall.getState() == Call.State.WAITING && this.mForegroundCall.getState().isAlive()) {
            setMute(false);
            ImsCall imsCall = this.mForegroundCall.getImsCall();
            ImsCall imsCall2 = this.mRingingCall.getImsCall();
            if (this.mForegroundCall.hasConnections() && this.mRingingCall.hasConnections()) {
                zShouldDisconnectActiveCallOnAnswer = shouldDisconnectActiveCallOnAnswer(imsCall, imsCall2);
            }
            this.mPendingCallVideoState = i;
            if (zShouldDisconnectActiveCallOnAnswer) {
                this.mForegroundCall.hangup();
                this.mPhone.getVoiceCallSessionStats().onImsAcceptCall(this.mRingingCall.getConnections());
                try {
                    imsCall2.accept(ImsCallProfile.getCallTypeFromVideoState(i));
                    return;
                } catch (ImsException unused) {
                    CallManager$$ExternalSyntheticBUOutline0.m("cannot accept call");
                    return;
                }
            }
            if (TelephonyBaseUtilsStub.isMiuiRom()) {
                this.mCallExpectedToHold = this.mForegroundCall.getImsCall();
                this.mCallExpectedToAccept = this.mRingingCall.getImsCall();
            }
            holdActiveCallForWaitingCall();
            return;
        }
        if (this.mRingingCall.getState().isRinging()) {
            log("acceptCall: incoming...");
            setMute(false);
            try {
                ImsCall imsCall3 = this.mRingingCall.getImsCall();
                if (imsCall3 != null) {
                    this.mPhone.getVoiceCallSessionStats().onImsAcceptCall(this.mRingingCall.getConnections());
                    imsCall3.accept(ImsCallProfile.getCallTypeFromVideoState(i));
                    return;
                }
                throw new CallStateException("no valid ims call");
            } catch (ImsException unused2) {
                CallManager$$ExternalSyntheticBUOutline0.m("cannot accept call");
                return;
            }
        }
        CallManager$$ExternalSyntheticBUOutline0.m("phone not ringing");
    }

    public void rejectCall() throws CallStateException {
        log("rejectCall");
        this.mOperationLocalLog.log("rejected incoming call");
        if (this.mRingingCall.getState().isRinging()) {
            hangup(this.mRingingCall);
        } else {
            CallManager$$ExternalSyntheticBUOutline0.m("phone not ringing");
        }
    }

    protected void setEmergencyCallInfo(ImsCallProfile imsCallProfile, Connection connection) {
        EmergencyNumber emergencyNumberInfo = connection.getEmergencyNumberInfo();
        if (emergencyNumberInfo != null) {
            imsCallProfile.setEmergencyCallInfo(emergencyNumberInfo, connection.hasKnownUserIntentEmergency());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchAfterConferenceSuccess() {
        log("switchAfterConferenceSuccess fg =" + this.mForegroundCall.getState() + ", bg = " + this.mBackgroundCall.getState());
        if (this.mBackgroundCall.getState() == Call.State.HOLDING) {
            log("switchAfterConferenceSuccess");
            this.mForegroundCall.switchWith(this.mBackgroundCall);
        }
    }

    protected void holdActiveCallForPendingMo() throws CallStateException {
        HoldSwapState holdSwapState = this.mHoldSwitchingState;
        if (holdSwapState == HoldSwapState.PENDING_SINGLE_CALL_HOLD || holdSwapState == HoldSwapState.SWAPPING_ACTIVE_AND_HELD) {
            logi("Ignoring hold request while already holding or swapping");
            return;
        }
        ImsCall imsCall = this.mForegroundCall.getImsCall();
        this.mHoldSwitchingState = HoldSwapState.HOLDING_TO_DIAL_OUTGOING;
        logHoldSwapState("holdActiveCallForPendingMo");
        this.mForegroundCall.switchWith(this.mBackgroundCall);
        try {
            imsCall.hold();
        } catch (ImsException e) {
            this.mForegroundCall.switchWith(this.mBackgroundCall);
            this.mHoldSwitchingState = holdSwapState;
            logHoldSwapState("holdActiveCallForPendingMo - fail");
            throw new CallStateException(e.getMessage());
        }
    }

    public void holdActiveCall() throws CallStateException {
        HoldSwapState holdSwapState;
        if (this.mForegroundCall.getState() == Call.State.ACTIVE) {
            HoldSwapState holdSwapState2 = this.mHoldSwitchingState;
            HoldSwapState holdSwapState3 = HoldSwapState.PENDING_SINGLE_CALL_HOLD;
            if (holdSwapState2 == holdSwapState3 || holdSwapState2 == (holdSwapState = HoldSwapState.SWAPPING_ACTIVE_AND_HELD)) {
                logi("Ignoring hold request while already holding or swapping");
                return;
            }
            ImsCall imsCall = this.mForegroundCall.getImsCall();
            if (this.mBackgroundCall.getState().isAlive()) {
                this.mCallExpectedToResume = this.mBackgroundCall.getImsCall();
                this.mHoldSwitchingState = holdSwapState;
            } else {
                this.mHoldSwitchingState = holdSwapState3;
            }
            logHoldSwapState("holdActiveCall");
            this.mForegroundCall.switchWith(this.mBackgroundCall);
            try {
                imsCall.hold();
            } catch (ImsException | NullPointerException e) {
                this.mForegroundCall.switchWith(this.mBackgroundCall);
                this.mHoldSwitchingState = holdSwapState2;
                logHoldSwapState("holdActiveCall - fail");
                throw new CallStateException(e.getMessage());
            }
        }
        MiuiImsControllerStub.get(this.mPhone.getPhoneId()).holdActiveCall(this, null);
    }

    public void holdActiveCallForWaitingCall() throws CallStateException {
        if (!this.mBackgroundCall.getState().isAlive() && this.mRingingCall.getState() == Call.State.WAITING) {
            ImsCall imsCall = this.mForegroundCall.getImsCall();
            HoldSwapState holdSwapState = this.mHoldSwitchingState;
            this.mHoldSwitchingState = HoldSwapState.HOLDING_TO_ANSWER_INCOMING;
            ImsCall imsCall2 = this.mCallExpectedToResume;
            this.mCallExpectedToResume = this.mRingingCall.getImsCall();
            this.mForegroundCall.switchWith(this.mBackgroundCall);
            logHoldSwapState("holdActiveCallForWaitingCall");
            try {
                imsCall.hold();
            } catch (ImsException e) {
                this.mForegroundCall.switchWith(this.mBackgroundCall);
                this.mHoldSwitchingState = holdSwapState;
                this.mCallExpectedToResume = imsCall2;
                logHoldSwapState("holdActiveCallForWaitingCall - fail");
                throw new CallStateException(e.getMessage());
            }
        }
        MiuiImsControllerStub.get(this.mPhone.getPhoneId()).holdActiveCallForWaitingCall(this, null);
    }

    public void unholdHeldCall() throws CallStateException {
        ImsCall imsCall = this.mBackgroundCall.getImsCall();
        ImsCall imsCall2 = this.mForegroundCall.getImsCall();
        if (imsCall == null && this.mForegroundCall.getState() == Call.State.HOLDING) {
            logi("Held call is in foreground. Unholding to recover from bad state.");
            imsCall = imsCall2;
        }
        HoldSwapState holdSwapState = this.mHoldSwitchingState;
        HoldSwapState holdSwapState2 = HoldSwapState.PENDING_SINGLE_CALL_UNHOLD;
        if (holdSwapState == holdSwapState2 || holdSwapState == HoldSwapState.SWAPPING_ACTIVE_AND_HELD) {
            logi("Ignoring unhold request while already unholding or swapping");
            return;
        }
        if (imsCall != null) {
            this.mCallExpectedToResume = imsCall;
            this.mHoldSwitchingState = holdSwapState2;
            if (!imsCall.equalsTo(imsCall2)) {
                this.mForegroundCall.switchWith(this.mBackgroundCall);
            }
            logHoldSwapState("unholdCurrentCall");
            try {
                imsCall.resume();
            } catch (ImsException e) {
                this.mForegroundCall.switchWith(this.mBackgroundCall);
                this.mHoldSwitchingState = holdSwapState;
                logHoldSwapState("unholdCurrentCall - fail");
                throw new CallStateException(e.getMessage());
            }
        }
    }

    private void resumeForegroundCall() {
        ImsCall imsCall = this.mForegroundCall.getImsCall();
        if (imsCall != null) {
            if (!imsCall.isPendingHold()) {
                imsCall.resume();
            } else {
                this.mHoldSwitchingState = HoldSwapState.PENDING_RESUME_FOREGROUND_AFTER_HOLD;
                logHoldSwapState("resumeForegroundCall - unhold pending; resume request again");
            }
        }
    }

    private void answerWaitingCall() {
        ImsCall imsCall = this.mRingingCall.getImsCall();
        if (imsCall != null) {
            this.mPhone.getVoiceCallSessionStats().onImsAcceptCall(this.mRingingCall.getConnections());
            imsCall.accept(ImsCallProfile.getCallTypeFromVideoState(this.mPendingCallVideoState));
        }
    }

    private void maintainConnectTimeCache() {
        final long jElapsedRealtime = SystemClock.elapsedRealtime() - 60000;
        this.mPhoneNumAndConnTime.entrySet().removeIf(new Predicate() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ImsPhoneCallTracker.$r8$lambda$6kCo2LYXQTEfJDq5cf6UQfhanaU(jElapsedRealtime, (Map.Entry) obj);
            }
        });
        while (!this.mUnknownPeerConnTime.isEmpty() && ((CacheEntry) this.mUnknownPeerConnTime.peek()).mCachedTime < jElapsedRealtime) {
            this.mUnknownPeerConnTime.poll();
        }
    }

    public static /* synthetic */ boolean $r8$lambda$6kCo2LYXQTEfJDq5cf6UQfhanaU(long j, Map.Entry entry) {
        return ((CacheEntry) entry.getValue()).mCachedTime < j;
    }

    private void cacheConnectionTimeWithPhoneNumber(ImsPhoneConnection imsPhoneConnection) {
        CacheEntry cacheEntry = new CacheEntry(SystemClock.elapsedRealtime(), imsPhoneConnection.getConnectTime(), imsPhoneConnection.getConnectTimeReal(), !imsPhoneConnection.isIncoming() ? 1 : 0);
        maintainConnectTimeCache();
        if (1 == imsPhoneConnection.getNumberPresentation()) {
            String formattedPhoneNumber = getFormattedPhoneNumber(imsPhoneConnection.getAddress());
            if (!this.mPhoneNumAndConnTime.containsKey(formattedPhoneNumber) || imsPhoneConnection.getConnectTime() > ((CacheEntry) this.mPhoneNumAndConnTime.get(formattedPhoneNumber)).mConnectTime) {
                this.mPhoneNumAndConnTime.put(formattedPhoneNumber, cacheEntry);
                return;
            }
            return;
        }
        this.mUnknownPeerConnTime.add(cacheEntry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CacheEntry findConnectionTimeUsePhoneNumber(ConferenceParticipant conferenceParticipant) {
        maintainConnectTimeCache();
        if (1 == conferenceParticipant.getParticipantPresentation()) {
            if (conferenceParticipant.getHandle() == null || conferenceParticipant.getHandle().getSchemeSpecificPart() == null) {
                return null;
            }
            String schemeSpecificPart = ConferenceParticipant.getParticipantAddress(conferenceParticipant.getHandle(), getCountryIso()).getSchemeSpecificPart();
            if (TextUtils.isEmpty(schemeSpecificPart)) {
                return null;
            }
            return (CacheEntry) this.mPhoneNumAndConnTime.get(getFormattedPhoneNumber(schemeSpecificPart));
        }
        return (CacheEntry) this.mUnknownPeerConnTime.poll();
    }

    private String getFormattedPhoneNumber(String str) {
        String numberToE164;
        String countryIso = getCountryIso();
        return (countryIso == null || (numberToE164 = PhoneNumberUtils.formatNumberToE164(str, countryIso)) == null) ? str : numberToE164;
    }

    private String getCountryIso() {
        SubscriptionInfo activeSubscriptionInfo = SubscriptionManager.from(this.mPhone.getContext()).getActiveSubscriptionInfo(this.mPhone.getSubId());
        if (activeSubscriptionInfo == null) {
            return null;
        }
        return activeSubscriptionInfo.getCountryIso();
    }

    public void conference() {
        String telecomCallId;
        ImsCall imsCall = this.mForegroundCall.getImsCall();
        if (imsCall == null) {
            log("conference no foreground ims call");
            return;
        }
        ImsCall imsCall2 = this.mBackgroundCall.getImsCall();
        if (imsCall2 == null) {
            log("conference no background ims call");
            return;
        }
        if (imsCall.isCallSessionMergePending()) {
            log("conference: skip; foreground call already in process of merging.");
            return;
        }
        if (imsCall2.isCallSessionMergePending()) {
            log("conference: skip; background call already in process of merging.");
            return;
        }
        if (ignoreConference(imsCall, imsCall2)) {
            return;
        }
        long earliestConnectTime = this.mForegroundCall.getEarliestConnectTime();
        long earliestConnectTime2 = this.mBackgroundCall.getEarliestConnectTime();
        if (earliestConnectTime > 0 && earliestConnectTime2 > 0) {
            earliestConnectTime = Math.min(this.mForegroundCall.getEarliestConnectTime(), this.mBackgroundCall.getEarliestConnectTime());
            log("conference - using connect time = " + earliestConnectTime);
        } else if (earliestConnectTime > 0) {
            log("conference - bg call connect time is 0; using fg = " + earliestConnectTime);
        } else {
            log("conference - fg call connect time is 0; using bg = " + earliestConnectTime2);
            earliestConnectTime = earliestConnectTime2;
        }
        ImsPhoneConnection firstConnection = this.mForegroundCall.getFirstConnection();
        String telecomCallId2 = "";
        if (firstConnection == null) {
            telecomCallId = "";
        } else {
            firstConnection.setConferenceConnectTime(earliestConnectTime);
            firstConnection.handleMergeStart();
            telecomCallId = firstConnection.getTelecomCallId();
            cacheConnectionTimeWithPhoneNumber(firstConnection);
        }
        ImsPhoneConnection imsPhoneConnectionFindConnection = findConnection(imsCall2);
        if (imsPhoneConnectionFindConnection != null) {
            imsPhoneConnectionFindConnection.handleMergeStart();
            telecomCallId2 = imsPhoneConnectionFindConnection.getTelecomCallId();
            cacheConnectionTimeWithPhoneNumber(imsPhoneConnectionFindConnection);
        }
        log("conference: fgCallId=" + telecomCallId + ", bgCallId=" + telecomCallId2);
        this.mOperationLocalLog.log("conference: fgCallId=" + telecomCallId + ", bgCallId=" + telecomCallId2);
        try {
            imsCall.merge(imsCall2);
        } catch (ImsException e) {
            log("conference " + e.getMessage());
            handleConferenceFailed(firstConnection, imsPhoneConnectionFindConnection);
        }
    }

    public void explicitCallTransfer() throws CallStateException {
        ImsCall imsCall = this.mForegroundCall.getImsCall();
        ImsCall imsCall2 = this.mBackgroundCall.getImsCall();
        if (imsCall == null || imsCall2 == null || !canTransfer()) {
            CallManager$$ExternalSyntheticBUOutline0.m("cannot transfer");
            return;
        }
        try {
            imsCall2.consultativeTransfer(imsCall);
        } catch (ImsException e) {
            throw new CallStateException(e.getMessage());
        }
    }

    public void clearDisconnected() {
        log("clearDisconnected");
        internalClearDisconnected();
        updatePhoneState();
        this.mPhone.notifyPreciseCallStateChanged();
    }

    public boolean canConference() {
        return this.mForegroundCall.getState() == Call.State.ACTIVE && this.mBackgroundCall.getState() == Call.State.HOLDING && !this.mBackgroundCall.isFull() && !this.mForegroundCall.isFull();
    }

    private boolean canAddVideoCallDuringImsAudioCall(int i) {
        if (this.mAllowHoldingVideoCall) {
            return true;
        }
        ImsCall imsCall = this.mForegroundCall.getImsCall();
        if (imsCall == null) {
            imsCall = this.mBackgroundCall.getImsCall();
        }
        return !(this.mForegroundCall.getState() == Call.State.ACTIVE || this.mBackgroundCall.getState() == Call.State.HOLDING) || imsCall == null || imsCall.isVideoCall() || !VideoProfile.isVideo(i);
    }

    public void checkForDialIssues() throws CallStateException {
        if (TelephonyProperties.disable_call().orElse(Boolean.FALSE).booleanValue()) {
            Phone$$ExternalSyntheticBUOutline2.m(5, "ro.telephony.disable-call has been used to disable calling.");
            return;
        }
        if (this.mPendingMO != null) {
            Phone$$ExternalSyntheticBUOutline2.m(3, "Another outgoing call is already being dialed.");
            return;
        }
        if (this.mRingingCall.isRinging()) {
            Phone$$ExternalSyntheticBUOutline2.m(4, "Can't place a call while another is ringing.");
            return;
        }
        if (!this.mBackgroundCall.getState().isAlive() || !this.mForegroundCall.getState().isAlive()) {
            return;
        }
        Phone$$ExternalSyntheticBUOutline2.m(6, "Already an active foreground and background call.");
    }

    public void checkForDialIssues(boolean z) throws CallStateException {
        if (TelephonyProperties.disable_call().orElse(Boolean.FALSE).booleanValue()) {
            Phone$$ExternalSyntheticBUOutline2.m(5, "ro.telephony.disable-call has been used to disable calling.");
            return;
        }
        if (this.mPendingMO != null) {
            Phone$$ExternalSyntheticBUOutline2.m(3, "Another outgoing call is already being dialed.");
            return;
        }
        if (this.mRingingCall.isRinging()) {
            Phone$$ExternalSyntheticBUOutline2.m(4, "Can't place a call while another is ringing.");
        } else {
            if (z) {
                return;
            }
            if (!this.mBackgroundCall.getState().isAlive() || !this.mForegroundCall.getState().isAlive()) {
                return;
            }
            Phone$$ExternalSyntheticBUOutline2.m(6, "Already an active foreground and background call.");
        }
    }

    public boolean canTransfer() {
        return this.mForegroundCall.getState() == Call.State.ACTIVE && this.mBackgroundCall.getState() == Call.State.HOLDING;
    }

    private void internalClearDisconnected() {
        this.mRingingCall.clearDisconnected();
        this.mForegroundCall.clearDisconnected();
        this.mBackgroundCall.clearDisconnected();
        this.mHandoverCall.clearDisconnected();
    }

    protected void updatePhoneState() {
        String str;
        PhoneConstants.State state = this.mState;
        ImsPhoneConnection imsPhoneConnection = this.mPendingMO;
        boolean z = imsPhoneConnection == null || !imsPhoneConnection.getState().isAlive();
        if (this.mRingingCall.isRinging()) {
            this.mState = PhoneConstants.State.RINGING;
        } else if (!z || !this.mForegroundCall.isIdle() || !this.mBackgroundCall.isIdle()) {
            this.mState = PhoneConstants.State.OFFHOOK;
        } else {
            this.mState = PhoneConstants.State.IDLE;
            if (isHoldOrSwapInProgress()) {
                this.mHoldSwitchingState = HoldSwapState.INACTIVE;
            }
        }
        PhoneConstants.State state2 = this.mState;
        PhoneConstants.State state3 = PhoneConstants.State.IDLE;
        if (state2 == state3 && state != state2) {
            this.mVoiceCallEndedRegistrants.notifyRegistrants(getCallStateChangeAsyncResult());
        } else if (state == state3 && state != state2) {
            this.mVoiceCallStartedRegistrants.notifyRegistrants(getCallStateChangeAsyncResult());
        }
        StringBuilder sb = new StringBuilder("updatePhoneState pendingMo = ");
        if (this.mPendingMO == null) {
            str = "null";
        } else {
            str = this.mPendingMO.getState() + "(" + this.mPendingMO.getTelecomCallId() + "/objId:" + System.identityHashCode(this.mPendingMO) + ")";
        }
        sb.append(str);
        sb.append(", rng= ");
        sb.append(this.mRingingCall.getState());
        sb.append("(");
        sb.append(this.mRingingCall.getConnectionSummary());
        sb.append("), fg= ");
        sb.append(this.mForegroundCall.getState());
        sb.append("(");
        sb.append(this.mForegroundCall.getConnectionSummary());
        sb.append("), bg= ");
        sb.append(this.mBackgroundCall.getState());
        sb.append("(");
        sb.append(this.mBackgroundCall.getConnectionSummary());
        sb.append(")");
        log(sb.toString());
        log("updatePhoneState oldState=" + state + ", newState=" + this.mState);
        if (this.mState != state) {
            this.mPhone.notifyPhoneStateChanged();
            notifyPhoneStateChanged(state, this.mState);
        }
    }

    public void setTtyMode(int i) {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            Log.w("ImsPhoneCallTracker", "ImsManager is null when setting TTY mode");
            return;
        }
        try {
            imsManager.setTtyMode(i);
        } catch (ImsException e) {
            loge("setTtyMode : " + e);
        }
    }

    public void setUiTTYMode(int i, Message message) {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            this.mPhone.sendErrorResponse(message, getImsManagerIsNullException());
            return;
        }
        try {
            imsManager.setUiTTYMode(this.mPhone.getContext(), i, message);
        } catch (ImsException e) {
            loge("setUITTYMode : " + e);
            this.mPhone.sendErrorResponse(message, e);
        }
    }

    public void setMute(boolean z) {
        this.mDesiredMute = z;
        this.mForegroundCall.setMute(z);
    }

    public boolean getMute() {
        return this.mDesiredMute;
    }

    public void sendDtmf(char c, Message message) {
        log("sendDtmf");
        ImsCall imsCall = this.mForegroundCall.getImsCall();
        if (imsCall != null) {
            imsCall.sendDtmf(c, message);
        }
    }

    public void startDtmf(char c) {
        log("startDtmf");
        ImsCall imsCall = this.mForegroundCall.getImsCall();
        if (imsCall != null) {
            imsCall.startDtmf(c);
        } else {
            loge("startDtmf : no foreground or ringing call");
        }
    }

    public void stopDtmf() {
        log("stopDtmf");
        ImsCall imsCall = this.mForegroundCall.getImsCall();
        if (imsCall != null) {
            imsCall.stopDtmf();
        } else {
            loge("stopDtmf : no foreground or ringing call");
        }
    }

    public void hangup(ImsPhoneConnection imsPhoneConnection) throws CallStateException {
        log("hangup connection");
        if (imsPhoneConnection.getOwner() != this) {
            GsmCdmaCallTracker$$ExternalSyntheticBUOutline0.m("ImsPhoneConnection ", imsPhoneConnection, "does not belong to ImsPhoneCallTracker ", this);
        } else {
            hangup(imsPhoneConnection.getCall());
        }
    }

    public void hangup(ImsPhoneCall imsPhoneCall) throws CallStateException {
        hangup(imsPhoneCall, 1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v5, types: [com.android.internal.telephony.imsphone.ImsPhone] */
    public void hangup(ImsPhoneCall imsPhoneCall, int i) throws CallStateException {
        boolean z;
        String str;
        log("hangup call - reason=" + i);
        if (imsPhoneCall.getConnectionsCount() == 0) {
            CallManager$$ExternalSyntheticBUOutline0.m("no connections");
            return;
        }
        ImsCall imsCall = imsPhoneCall.getImsCall();
        ImsPhoneConnection imsPhoneConnectionFindConnection = findConnection(imsCall);
        if (imsCall != null && imsCall.isCallSessionMergePending()) {
            log("hangup call failed during call merge");
            if (imsPhoneConnectionFindConnection != null) {
                imsPhoneConnectionFindConnection.onConnectionEvent("android.telecom.event.DISCONNECT_FAILED", null);
            }
            CallManager$$ExternalSyntheticBUOutline0.m("can not hangup during call merge");
            return;
        }
        if (imsPhoneCall == this.mRingingCall) {
            str = "(ringing) hangup incoming";
            z = true;
        } else {
            z = false;
            if (imsPhoneCall == this.mForegroundCall) {
                if (imsPhoneCall.isDialingOrAlerting()) {
                    str = "(foregnd) hangup dialing or alerting...";
                } else {
                    str = "(foregnd) hangup foreground";
                }
            } else if (imsPhoneCall == this.mBackgroundCall) {
                str = "(backgnd) hangup waiting or background";
            } else if (imsPhoneCall == this.mHandoverCall) {
                str = "(handover) hangup handover (SRVCC) call";
            } else {
                this.mOperationLocalLog.log("hangup: ImsPhoneCall " + System.identityHashCode(imsPhoneConnectionFindConnection) + " does not belong to ImsPhoneCallTracker " + this);
                GsmCdmaCallTracker$$ExternalSyntheticBUOutline0.m("ImsPhoneCall ", imsPhoneCall, "does not belong to ImsPhoneCallTracker ", this);
                return;
            }
        }
        log(str);
        this.mOperationLocalLog.log("hangup: " + str + ", connId=" + System.identityHashCode(imsPhoneConnectionFindConnection));
        imsPhoneCall.onHangupLocal();
        this.mImsCallInfoTracker.updateImsCallStatus(imsPhoneConnectionFindConnection);
        try {
            if (imsCall == null) {
                ImsPhoneConnection imsPhoneConnection = this.mPendingMO;
                if (imsPhoneConnection != null && imsPhoneCall == this.mForegroundCall) {
                    imsPhoneConnection.update(null, Call.State.DISCONNECTED);
                    this.mPendingMO.onDisconnect();
                    removeConnection(this.mPendingMO);
                    this.mPendingMO = null;
                    updatePhoneState();
                    removeMessages(20);
                }
            } else if (!z) {
                imsCall.terminate(getHangupReasionInfo(i, z));
            } else if (i == 2) {
                imsCall.reject(365);
            } else {
                imsCall.reject(getHangupReasionInfo(i, z));
            }
            this = this.mPhone;
            this.notifyPreciseCallStateChanged();
        } catch (ImsException e) {
            this.mOperationLocalLog.log("hangup: ImsException=" + e);
            throw new CallStateException(e.getMessage());
        }
    }

    protected void callEndCleanupHandOverCallIfAny() {
        if (this.mHandoverCall.getConnections().size() > 0) {
            log("callEndCleanupHandOverCallIfAny, mHandoverCall.mConnections=" + this.mHandoverCall.getConnections());
            removeAllConnectionsByCall(this.mHandoverCall);
            this.mHandoverCall.clearConnections();
            this.mConnections.clear();
            this.mState = PhoneConstants.State.IDLE;
            notifyPhoneStateInSomeCase();
        }
    }

    public void sendUSSD(String str, Message message) {
        log("sendUSSD");
        try {
            ImsCall imsCall = this.mUssdSession;
            if (imsCall != null) {
                this.mPendingUssd = null;
                imsCall.sendUssd(str);
                AsyncResult.forMessage(message, (Object) null, (Throwable) null);
                message.sendToTarget();
                return;
            }
            ImsManager imsManager = this.mImsManager;
            if (imsManager == null) {
                this.mPhone.sendErrorResponse(message, getImsManagerIsNullException());
                return;
            }
            ImsCallProfile imsCallProfileCreateCallProfile = imsManager.createCallProfile(1, 2);
            imsCallProfileCreateCallProfile.setCallExtraInt("dialstring", 2);
            this.mUssdSession = this.mImsManager.makeCall(imsCallProfileCreateCallProfile, new String[]{str}, this.mImsUssdListener);
            this.mPendingUssd = message;
            log("pending ussd updated, " + this.mPendingUssd);
        } catch (ImsException e) {
            loge("sendUSSD : " + e);
            this.mPhone.sendErrorResponse(message, e);
        }
    }

    public void cancelUSSD(Message message) {
        ImsCall imsCall = this.mUssdSession;
        if (imsCall == null) {
            return;
        }
        this.mPendingUssd = message;
        imsCall.terminate(NtnRILConstants.MI_NTN_OEM_ERROR_1);
    }

    protected synchronized ImsPhoneConnection findConnection(ImsCall imsCall) {
        ArrayList<ImsPhoneConnection> arrayList = this.mConnections;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            ImsPhoneConnection imsPhoneConnection = arrayList.get(i);
            i++;
            ImsPhoneConnection imsPhoneConnection2 = imsPhoneConnection;
            if (imsPhoneConnection2.getImsCall() == imsCall) {
                return imsPhoneConnection2;
            }
        }
        return null;
    }

    public synchronized void cleanupAndRemoveConnection(ImsPhoneConnection imsPhoneConnection) {
        try {
            this.mOperationLocalLog.log("cleanupAndRemoveConnection: " + imsPhoneConnection);
            if (imsPhoneConnection.getCall() != null) {
                imsPhoneConnection.getCall().detach(imsPhoneConnection);
            }
            removeConnection(imsPhoneConnection);
            ImsPhoneConnection imsPhoneConnection2 = this.mPendingMO;
            if (imsPhoneConnection == imsPhoneConnection2) {
                imsPhoneConnection2.finalize();
                this.mPendingMO = null;
            }
            updatePhoneState();
            this.mPhone.notifyPreciseCallStateChanged();
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void removeConnection(ImsPhoneConnection imsPhoneConnection) {
        try {
            this.mConnections.remove(imsPhoneConnection);
            if (this.mIsInEmergencyCall) {
                ArrayList<ImsPhoneConnection> arrayList = this.mConnections;
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    ImsPhoneConnection imsPhoneConnection2 = arrayList.get(i);
                    i++;
                    ImsPhoneConnection imsPhoneConnection3 = imsPhoneConnection2;
                    if (imsPhoneConnection3 == null || !imsPhoneConnection3.isEmergency()) {
                    }
                }
                if (this.mPhone.isEcmCanceledForEmergency()) {
                    this.mPhone.handleTimerInEmergencyCallbackMode(0);
                }
                this.mIsInEmergencyCall = false;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    protected synchronized void addConnection(ImsPhoneConnection imsPhoneConnection) {
        this.mConnections.add(imsPhoneConnection);
        if (imsPhoneConnection.isEmergency()) {
            this.mIsInEmergencyCall = true;
        }
    }

    protected void processCallStateChange(ImsCall imsCall, Call.State state, int i) {
        log("processCallStateChange " + imsCall + " state=" + state + " cause=" + i);
        processCallStateChange(imsCall, state, i, false);
    }

    protected void processCallStateChange(ImsCall imsCall, Call.State state, int i, boolean z) {
        log("processCallStateChange state=" + state + " cause=" + i + " ignoreState=" + z);
        if (imsCall == null) {
            return;
        }
        if (TelephonyBaseUtilsStub.isMiuiRom()) {
            if (imsCall == this.mCallExpectedToHold && (state == Call.State.HOLDING || !state.isAlive())) {
                this.mCallExpectedToHold = null;
                this.mCallExpectedToAccept = null;
            } else if (imsCall == this.mCallExpectedToAccept && (state == Call.State.ACTIVE || !state.isAlive())) {
                this.mCallExpectedToAccept = null;
            }
        }
        ImsPhoneConnection imsPhoneConnectionFindConnection = findConnection(imsCall);
        if (imsPhoneConnectionFindConnection == null) {
            return;
        }
        imsPhoneConnectionFindConnection.updateMediaCapabilities(imsCall);
        if (z) {
            imsPhoneConnectionFindConnection.updateAddressDisplay(imsCall);
            imsPhoneConnectionFindConnection.updateExtras(imsCall);
            imsPhoneConnectionFindConnection.maybeChangeRingbackState();
            maybeSetVideoCallProvider(imsPhoneConnectionFindConnection, imsCall);
            this.mImsCallInfoTracker.updateImsCallStatus(imsPhoneConnectionFindConnection);
            this.mPhone.notifyPreciseCallStateToNotifier();
            return;
        }
        this.mOperationLocalLog.log("processCallStateChange: state=" + state + " cause=" + i + " connId=" + System.identityHashCode(imsPhoneConnectionFindConnection));
        Call.State state2 = this.mForegroundCall.getState();
        Call.State state3 = Call.State.ACTIVE;
        boolean z2 = true;
        boolean z3 = (state2 == state3 || this.mBackgroundCall.getState() == state3) ? false : true;
        boolean zUpdate = imsPhoneConnectionFindConnection.update(imsCall, state);
        if (z3 && zUpdate && state == state3) {
            sendMessage(obtainMessage(35));
        }
        if (state == Call.State.DISCONNECTED) {
            if (!imsPhoneConnectionFindConnection.onDisconnect(i) && !zUpdate) {
                z2 = false;
            }
            imsPhoneConnectionFindConnection.getCall().detach(imsPhoneConnectionFindConnection);
            removeConnection(imsPhoneConnectionFindConnection);
            ImsPhoneConnection imsPhoneConnection = this.mPendingMO;
            if (imsPhoneConnection == imsPhoneConnectionFindConnection) {
                imsPhoneConnection.finalize();
                this.mPendingMO = null;
            }
            List conferenceParticipants = imsCall.getConferenceParticipants();
            if (conferenceParticipants != null) {
                Iterator it = conferenceParticipants.iterator();
                while (it.hasNext()) {
                    String schemeSpecificPart = ConferenceParticipant.getParticipantAddress(((ConferenceParticipant) it.next()).getHandle(), getCountryIso()).getSchemeSpecificPart();
                    if (!TextUtils.isEmpty(schemeSpecificPart)) {
                        this.mPhoneNumAndConnTime.remove(getFormattedPhoneNumber(schemeSpecificPart));
                    }
                }
            }
            zUpdate = z2;
        } else {
            this.mPhone.getVoiceCallSessionStats().onCallStateChanged(imsPhoneConnectionFindConnection.getCall());
        }
        if (zUpdate) {
            this.mImsCallInfoTracker.updateImsCallStatus(imsPhoneConnectionFindConnection);
            if (imsPhoneConnectionFindConnection.getCall() == this.mHandoverCall) {
                return;
            }
            updatePhoneState();
            this.mPhone.notifyPreciseCallStateChanged();
        }
    }

    private void maybeSetVideoCallProvider(ImsPhoneConnection imsPhoneConnection, ImsCall imsCall) {
        android.telecom.Connection.VideoProvider videoProvider = imsPhoneConnection.getVideoProvider();
        ImsCallSession callSession = imsCall.getCallSession();
        if (videoProvider != null || callSession == null || callSession.getVideoCallProvider() == null) {
            return;
        }
        try {
            setVideoCallProvider(imsPhoneConnection, imsCall);
        } catch (RemoteException e) {
            loge("maybeSetVideoCallProvider: exception " + e);
        }
    }

    public void addReasonCodeRemapping(Integer num, String str, Integer num2) {
        if (str != null) {
            str = str.toLowerCase(Locale.ROOT);
        }
        this.mImsReasonCodeMap.put(new ImsReasonInfoKeyPair(num, str), num2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int maybeRemapReasonCode(ImsReasonInfo imsReasonInfo) {
        String lowerCase;
        int code = imsReasonInfo.getCode();
        String extraMessage = imsReasonInfo.getExtraMessage();
        if (extraMessage == null) {
            lowerCase = "";
        } else {
            lowerCase = extraMessage.toLowerCase(Locale.ROOT);
        }
        log("maybeRemapReasonCode : fromCode = " + imsReasonInfo.getCode() + " ; message = " + lowerCase);
        ImsReasonInfoKeyPair imsReasonInfoKeyPair = new ImsReasonInfoKeyPair(Integer.valueOf(code), lowerCase);
        ImsReasonInfoKeyPair imsReasonInfoKeyPair2 = new ImsReasonInfoKeyPair(0 == true ? 1 : 0, lowerCase);
        ImsReasonInfoKeyPair imsReasonInfoKeyPair3 = new ImsReasonInfoKeyPair(Integer.valueOf(code), 0 == true ? 1 : 0);
        if (this.mImsReasonCodeMap.containsKey(imsReasonInfoKeyPair)) {
            int iIntValue = ((Integer) this.mImsReasonCodeMap.get(imsReasonInfoKeyPair)).intValue();
            log("maybeRemapReasonCode : fromCode = " + imsReasonInfo.getCode() + " ; message = " + lowerCase + " ; toCode = " + iIntValue);
            return iIntValue;
        }
        if (!lowerCase.isEmpty() && this.mImsReasonCodeMap.containsKey(imsReasonInfoKeyPair2)) {
            int iIntValue2 = ((Integer) this.mImsReasonCodeMap.get(imsReasonInfoKeyPair2)).intValue();
            log("maybeRemapReasonCode : fromCode(wildcard) = " + imsReasonInfo.getCode() + " ; message = " + lowerCase + " ; toCode = " + iIntValue2);
            return iIntValue2;
        }
        if (!this.mImsReasonCodeMap.containsKey(imsReasonInfoKeyPair3)) {
            return code;
        }
        int iIntValue3 = ((Integer) this.mImsReasonCodeMap.get(imsReasonInfoKeyPair3)).intValue();
        log("maybeRemapReasonCode : fromCode = " + imsReasonInfo.getCode() + " ; message(wildcard) = " + lowerCase + " ; toCode = " + iIntValue3);
        return iIntValue3;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:65:0x0085 A[RETURN] */
    public int getDisconnectCauseFromReasonInfo(ImsReasonInfo imsReasonInfo, Call.State state) {
        int iMaybeRemapReasonCode = maybeRemapReasonCode(imsReasonInfo);
        if (iMaybeRemapReasonCode == 339) {
            return 2;
        }
        switch (iMaybeRemapReasonCode) {
            case 0:
            case CallFailCause.USER_IGNORE /* 1502 */:
                if (this.mPhone.getDefaultPhone().getServiceStateTracker().mRestrictedState.isCsRestricted()) {
                    return 22;
                }
                if (this.mPhone.getDefaultPhone().getServiceStateTracker().mRestrictedState.isCsEmergencyRestricted()) {
                    return 24;
                }
                return this.mPhone.getDefaultPhone().getServiceStateTracker().mRestrictedState.isCsNormalRestricted() ? 23 : 36;
            case 106:
            case 121:
            case 122:
            case 123:
            case 124:
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RADIO_CAPABILITY /* 131 */:
            case 132:
            case NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE /* 144 */:
                return 18;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH /* 108 */:
                return 45;
            case 111:
            case 1500:
                return 17;
            case 112:
            case NtnRILConstants.MI_NTN_OEM_ERROR_5 /* 505 */:
                return state == Call.State.DIALING ? 62 : 61;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_NETWORK_SCAN /* 143 */:
            case NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_REFRESH /* 1017 */:
            case 1404:
                return 16;
            case 201:
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA /* 202 */:
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_LINK_CAPACITY_REPORTING_CRITERIA /* 203 */:
            case 335:
                return 13;
            case 240:
                return 20;
            case 241:
                return 21;
            case 243:
                return 58;
            case 244:
                return 46;
            case 245:
                return 47;
            case 246:
                return 48;
            case CallFailCause.RADIO_OFF /* 247 */:
                return 66;
            case 248:
                return 69;
            case CallFailCause.NO_VALID_SIM /* 249 */:
                return 70;
            case CallFailCause.RADIO_INTERNAL_ERROR /* 250 */:
                return 67;
            case CallFailCause.NETWORK_RESP_TIMEOUT /* 251 */:
                return 68;
            case 321:
            case 340:
            case 370:
                if (MiuiImsControllerStub.get(this.mPhone.getPhoneId()).isCarrierConfigedBusyTone(this.mPhone) && (state == Call.State.DIALING || state == Call.State.ALERTING)) {
                    return 4;
                }
                return 12;
            case 332:
                return (MiuiImsControllerStub.get(this.mPhone.getPhoneId()).isCarrierConfigedBusyTone(this.mPhone) && (state == Call.State.DIALING || state == Call.State.ALERTING)) ? 4 : 12;
            case 333:
                return 7;
            case 337:
            case 341:
                return 8;
            case 338:
                return 4;
            case 352:
            case 354:
                return 9;
            case 361:
            case NtnRILConstants.MI_NTN_OEM_ERROR_10 /* 510 */:
                return 2;
            case 362:
                return 12;
            case 363:
                return 63;
            case 364:
                return 64;
            case 402:
                return 77;
            case NtnRILConstants.MI_NTN_OEM_ERROR_1 /* 501 */:
                return 3;
            case NtnRILConstants.MI_NTN_RIL_UNSOL_STK_EVENT_NOTIFY /* 1014 */:
                return 52;
            case NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_SMS_STORAGE_FULL /* 1016 */:
                return 51;
            case CallFailCause.MEDIA_UNSPECIFIED /* 1403 */:
                return 53;
            case 1405:
                return 55;
            case 1406:
                return 54;
            case 1407:
                return 59;
            case 1512:
                return 60;
            case 1514:
                return 71;
            case 1515:
            case 3001:
                return 25;
            case 1602:
            case 1606:
            case 1607:
            case 1608:
            case 1611:
            case 1614:
            case 1616:
            case 1618:
            case 1621:
                return 81;
            case 1622:
                return 78;
            case 1623:
                return 79;
            case CallFailCause.EMC_REDIAL_ON_VOWIFI /* 3002 */:
                return 127;
            default:
                return 36;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPreciseDisconnectCauseFromReasonInfo(ImsReasonInfo imsReasonInfo) {
        return PRECISE_CAUSE_MAP.get(maybeRemapReasonCode(imsReasonInfo), 65535);
    }

    protected boolean isPhoneInEcbMode() {
        ImsPhone imsPhone = this.mPhone;
        return imsPhone != null && imsPhone.isInEcm();
    }

    protected void dialPendingMO() {
        boolean zIsPhoneInEcbMode = isPhoneInEcbMode();
        boolean zIsEmergency = this.mPendingMO.isEmergency();
        if (!zIsPhoneInEcbMode || (zIsPhoneInEcbMode && zIsEmergency)) {
            sendEmptyMessage(20);
        } else {
            sendEmptyMessage(21);
        }
    }

    public void sendCallStartFailedDisconnect(ImsCall imsCall, ImsReasonInfo imsReasonInfo) {
        Call.State state;
        this.mPendingMO = null;
        ImsPhoneConnection imsPhoneConnectionFindConnection = findConnection(imsCall);
        if (imsPhoneConnectionFindConnection != null) {
            state = imsPhoneConnectionFindConnection.getState();
        } else {
            state = Call.State.DIALING;
        }
        processCallStateChange(imsCall, Call.State.DISCONNECTED, getDisconnectCauseFromReasonInfo(imsReasonInfo, state));
        if (imsPhoneConnectionFindConnection != null) {
            imsPhoneConnectionFindConnection.setPreciseDisconnectCause(getPreciseDisconnectCauseFromReasonInfo(imsReasonInfo));
        }
        this.mPhone.notifyImsReason(imsReasonInfo);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.ims.ImsException */
    public ImsUtInterface getUtInterface() throws ImsException {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            throw getImsManagerIsNullException();
        }
        return imsManager.createOrGetSupplementaryServiceConfiguration();
    }

    protected void transferHandoverConnections(ImsPhoneCall imsPhoneCall) {
        resetHoldToneWhenHandover(imsPhoneCall);
        int i = 0;
        if (imsPhoneCall.getConnections() != null) {
            ArrayList<Connection> connections = imsPhoneCall.getConnections();
            int size = connections.size();
            int i2 = 0;
            while (i2 < size) {
                Connection connection = connections.get(i2);
                i2++;
                Connection connection2 = connection;
                connection2.mPreHandoverState = imsPhoneCall.mState;
                log("Connection state before handover is " + connection2.getStateBeforeHandover());
                setMultiPartyState(connection2);
            }
        }
        ArrayList<Connection> connections2 = this.mHandoverCall.getConnections();
        ImsPhoneCall imsPhoneCall2 = this.mHandoverCall;
        if (connections2 == null) {
            imsPhoneCall2.mConnections = imsPhoneCall.getConnections();
        } else {
            imsPhoneCall2.mConnections.addAll(imsPhoneCall.getConnections());
        }
        if (this.mHandoverCall.getConnections() != null) {
            if (imsPhoneCall.getImsCall() != null) {
                imsPhoneCall.getImsCall().close();
            }
            ArrayList<Connection> connections3 = this.mHandoverCall.getConnections();
            int size2 = connections3.size();
            while (i < size2) {
                Connection connection3 = connections3.get(i);
                i++;
                ImsPhoneConnection imsPhoneConnection = (ImsPhoneConnection) connection3;
                imsPhoneConnection.changeParent(this.mHandoverCall);
                imsPhoneConnection.releaseWakeLock();
            }
        }
        if (imsPhoneCall.getState().isAlive()) {
            log("Call is alive and state is " + imsPhoneCall.mState);
            this.mHandoverCall.mState = imsPhoneCall.mState;
        }
        imsPhoneCall.clearConnections();
        imsPhoneCall.mState = Call.State.IDLE;
        if (this.mPendingMO != null) {
            logi("pending MO on handover, clearing...");
            this.mPendingMO = null;
        }
        resetRingBackTone(imsPhoneCall);
    }

    public void notifySrvccState(int i) {
        log("notifySrvccState state=" + i);
        ImsManager imsManager = this.mImsManager;
        if (imsManager != null) {
            try {
                if (i == 0) {
                    imsManager.notifySrvccStarted(this.mSrvccStartedCallback);
                } else if (i == 1) {
                    imsManager.notifySrvccCompleted();
                } else if (i == 2) {
                    imsManager.notifySrvccFailed();
                } else if (i == 3) {
                    imsManager.notifySrvccCanceled();
                }
            } catch (ImsException e) {
                loge("notifySrvccState : exception " + e);
            }
        }
        if (i == 0) {
            this.mSrvccState = Call.SrvccState.STARTED;
            return;
        }
        if (i != 1) {
            if (i == 2) {
                this.mSrvccState = Call.SrvccState.FAILED;
                return;
            } else {
                if (i != 3) {
                    return;
                }
                this.mSrvccState = Call.SrvccState.CANCELED;
                return;
            }
        }
        this.mSrvccState = Call.SrvccState.COMPLETED;
        this.mForegroundCall.maybeStopRingback();
        this.mForegroundCall.maybeClearRemotelyHeldStatus();
        this.mBackgroundCall.maybeClearRemotelyHeldStatus();
        resetState();
        transferHandoverConnections(this.mForegroundCall);
        transferHandoverConnections(this.mBackgroundCall);
        transferHandoverConnections(this.mRingingCall);
        updatePhoneState();
        this.mImsCallInfoTracker.notifySrvccCompleted();
    }

    private void resetState() {
        this.mIsInEmergencyCall = false;
        this.mPhone.setEcmCanceledForEmergency(false);
        this.mHoldSwitchingState = HoldSwapState.INACTIVE;
        logHoldSwapState("resetState");
    }

    public boolean isHoldOrSwapInProgress() {
        return this.mHoldSwitchingState != HoldSwapState.INACTIVE;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.ims.ImsException */
    @Override // com.android.internal.telephony.CallTracker, android.os.Handler
    public void handleMessage(Message message) {
        Object[] objArr;
        ImsTrafficSession imsTrafficSession;
        log("handleMessage what=" + message.what);
        switch (message.what) {
            case 14:
                if (this.pendingCallInEcm) {
                    dialInternal(this.mPendingMO, this.pendingCallClirMode, this.mPendingCallVideoState, this.mPendingIntentExtras);
                    this.mPendingIntentExtras = null;
                    this.pendingCallInEcm = false;
                }
                this.mPhone.unsetOnEcbModeExitResponse(this);
                return;
            case 15:
            case 16:
            case 17:
            case 24:
            default:
                return;
            case 18:
                ImsPhoneConnection imsPhoneConnection = this.mPendingMO;
                if (imsPhoneConnection != null) {
                    imsPhoneConnection.onDisconnect();
                    removeConnection(this.mPendingMO);
                    this.mPendingMO = null;
                }
                this.mPendingIntentExtras = null;
                updatePhoneState();
                this.mPhone.notifyPreciseCallStateChanged();
                return;
            case 19:
            case 22:
                AsyncResult asyncResult = (AsyncResult) message.obj;
                ImsCall imsCall = (ImsCall) asyncResult.userObj;
                Long l = (Long) asyncResult.result;
                l.longValue();
                log("VT data usage update. usage = " + l + ", imsCall = " + imsCall);
                if (l.longValue() > 0) {
                    updateVtDataUsage(imsCall, l.longValue());
                    return;
                }
                return;
            case 20:
                ImsPhoneConnection imsPhoneConnection2 = this.mPendingMO;
                if (imsPhoneConnection2 == null || imsPhoneConnection2.getImsCall() != null) {
                    return;
                }
                dialInternal(this.mPendingMO, this.mClirMode, this.mPendingCallVideoState, this.mPendingIntentExtras);
                this.mPendingIntentExtras = null;
                return;
            case 21:
                if (this.mPendingMO != null) {
                    try {
                        getEcbmInterface().exitEmergencyCallbackMode();
                        this.mPhone.setOnEcbModeExitResponse(this, 14, null);
                        this.pendingCallClirMode = this.mClirMode;
                        this.pendingCallInEcm = true;
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.mPendingMO.setDisconnectCause(36);
                        sendEmptyMessageDelayed(18, 500L);
                        return;
                    }
                }
                return;
            case 23:
                Object obj = ((AsyncResult) message.obj).result;
                if (obj instanceof Pair) {
                    Pair pair = (Pair) obj;
                    onDataEnabledChanged(((Boolean) pair.first).booleanValue(), ((Integer) pair.second).intValue());
                    return;
                }
                return;
            case 25:
                Object obj2 = message.obj;
                if (obj2 instanceof ImsCall) {
                    ImsCall imsCall2 = (ImsCall) obj2;
                    if (imsCall2 != this.mForegroundCall.getImsCall()) {
                        Rlog.i("ImsPhoneCallTracker", "handoverCheck: no longer FG; check skipped.");
                        unregisterForConnectivityChanges();
                        return;
                    }
                    if (!this.mHasAttemptedStartOfCallHandover) {
                        this.mHasAttemptedStartOfCallHandover = true;
                    }
                    if (imsCall2.isWifiCall()) {
                        return;
                    }
                    ImsPhoneConnection imsPhoneConnectionFindConnection = findConnection(imsCall2);
                    if (imsPhoneConnectionFindConnection != null) {
                        Rlog.i("ImsPhoneCallTracker", "handoverCheck: handover failed.");
                        imsPhoneConnectionFindConnection.onHandoverToWifiFailed();
                    }
                    if (imsCall2.isVideoCall() && imsPhoneConnectionFindConnection.getDisconnectCause() == 0) {
                        registerForConnectivityChanges();
                        return;
                    }
                    return;
                }
                return;
            case 26:
                SomeArgs someArgs = (SomeArgs) message.obj;
                try {
                    handleFeatureCapabilityChanged((ImsFeature.Capabilities) someArgs.arg1);
                    updateImsRegistrationInfo();
                    return;
                } finally {
                    someArgs.recycle();
                }
            case 27:
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                ImsPhoneMmiCode imsPhoneMmiCode = new ImsPhoneMmiCode(this.mPhone, this.mFeatureFlags);
                try {
                    imsPhoneMmiCode.setIsSsInfo(true);
                    imsPhoneMmiCode.processImsSsData(asyncResult2);
                    return;
                } catch (ImsException e2) {
                    Rlog.e("ImsPhoneCallTracker", "Exception in parsing SS Data: " + e2);
                    return;
                }
            case 28:
                Pair pair2 = (Pair) ((AsyncResult) message.obj).userObj;
                removeMessages(29);
                this.mPhone.getDefaultPhone().mCi.unregisterForOn(this);
                ImsPhoneConnection imsPhoneConnectionFindConnection2 = findConnection((ImsCall) pair2.first);
                if (imsPhoneConnectionFindConnection2 == null) {
                    sendCallStartFailedDisconnect((ImsCall) pair2.first, (ImsReasonInfo) pair2.second);
                    loge("EVENT_REDIAL_WIFI_E911_CALL: null oldConnection");
                    return;
                }
                this.mForegroundCall.detach(imsPhoneConnectionFindConnection2);
                removeConnection(imsPhoneConnectionFindConnection2);
                try {
                    imsPhoneConnectionFindConnection2.onOriginalConnectionReplaced(this.mPhone.getDefaultPhone().dial(this.mLastDialString, this.mLastDialArgs));
                    ImsCall imsCall3 = this.mForegroundCall.getImsCall();
                    imsCall3.getCallProfile().setCallExtraBoolean("e_call", true);
                    findConnection(imsCall3).updateExtras(imsCall3);
                    return;
                } catch (CallStateException unused) {
                    sendCallStartFailedDisconnect((ImsCall) pair2.first, (ImsReasonInfo) pair2.second);
                    return;
                }
            case 29:
                Pair pair3 = (Pair) message.obj;
                this.mPhone.getDefaultPhone().mCi.unregisterForOn(this);
                removeMessages(28);
                sendCallStartFailedDisconnect((ImsCall) pair3.first, (ImsReasonInfo) pair3.second);
                return;
            case 30:
                try {
                    answerWaitingCall();
                    return;
                } catch (ImsException e3) {
                    loge("handleMessage EVENT_ANSWER_WAITING_CALL exception=" + e3);
                    return;
                }
            case 31:
                try {
                    resumeForegroundCall();
                    return;
                } catch (ImsException e4) {
                    loge("handleMessage EVENT_RESUME_NOW_FOREGROUND_CALL exception=" + e4);
                    return;
                }
            case 32:
                Pair pair4 = (Pair) message.obj;
                removeMessages(32);
                ImsPhoneConnection imsPhoneConnectionFindConnection3 = findConnection((ImsCall) pair4.first);
                if (imsPhoneConnectionFindConnection3 == null) {
                    sendCallStartFailedDisconnect((ImsCall) pair4.first, (ImsReasonInfo) pair4.second);
                    loge("EVENT_REDIAL_WITHOUT_RTT: null oldConnection");
                    return;
                }
                this.mForegroundCall.detach(imsPhoneConnectionFindConnection3);
                removeConnection(imsPhoneConnectionFindConnection3);
                try {
                    this.mPendingMO = null;
                    imsPhoneConnectionFindConnection3.onOriginalConnectionReplaced(this.mPhone.getDefaultPhone().dial(this.mLastDialString, ImsPhone.ImsDialArgs.Builder.from((PhoneInternalInterface.DialArgs) this.mLastDialArgs).setRttTextStream(null).setRetryCallFailCause(3001).setRetryCallFailNetworkType(ServiceState.rilRadioTechnologyToNetworkType(imsPhoneConnectionFindConnection3.getCallRadioTech())).build()));
                    return;
                } catch (CallStateException unused2) {
                    sendCallStartFailedDisconnect((ImsCall) pair4.first, (ImsReasonInfo) pair4.second);
                    return;
                }
            case 33:
            case 34:
                AsyncResult asyncResult3 = (AsyncResult) message.obj;
                IImsTrafficSessionCallback iImsTrafficSessionCallback = (IImsTrafficSessionCallback) asyncResult3.userObj;
                try {
                    if (asyncResult3.exception != null || (objArr = (Object[]) asyncResult3.result) == null || objArr.length <= 1) {
                        if (iImsTrafficSessionCallback != null) {
                            iImsTrafficSessionCallback.onError(new ConnectionFailureInfo(65535, 0, -1));
                            return;
                        }
                        return;
                    }
                    if (iImsTrafficSessionCallback == null && (imsTrafficSession = getImsTrafficSession(((Integer) objArr[0]).intValue())) != null) {
                        iImsTrafficSessionCallback = imsTrafficSession.mCallback;
                    }
                    if (iImsTrafficSessionCallback == null) {
                        return;
                    }
                    Object obj3 = objArr[1];
                    if (obj3 == null) {
                        iImsTrafficSessionCallback.onReady();
                        return;
                    } else {
                        iImsTrafficSessionCallback.onError((ConnectionFailureInfo) obj3);
                        return;
                    }
                } catch (RemoteException e5) {
                    Rlog.e("ImsPhoneCallTracker", "Exception: " + e5);
                    return;
                }
            case 35:
                try {
                    MediaQualityStatus mediaQualityStatusQueryMediaQualityStatus = this.mImsManager.queryMediaQualityStatus(1);
                    if (mediaQualityStatusQueryMediaQualityStatus != null) {
                        ImsPhone imsPhone = this.mPhone;
                        if (imsPhone != null && imsPhone.mDefaultPhone != null) {
                            log("notify media quality status: " + mediaQualityStatusQueryMediaQualityStatus);
                            this.mPhone.onMediaQualityStatusChanged(mediaQualityStatusQueryMediaQualityStatus);
                            return;
                        }
                        loge("onMediaQualityStatusChanged: null phone");
                        return;
                    }
                    return;
                } catch (ImsException e6) {
                    Rlog.e("ImsPhoneCallTracker", "Exception in queryMediaQualityStatus: " + e6);
                    return;
                }
            case 36:
                handleProvisioningChanged();
                return;
        }
    }

    public void updateVtDataUsage(ImsCall imsCall, long j) {
        long jLongValue = j - (this.mVtDataUsageMap.containsKey(Integer.valueOf(imsCall.uniqueId)) ? ((Long) this.mVtDataUsageMap.get(Integer.valueOf(imsCall.uniqueId))).longValue() : 0L);
        this.mVtDataUsageMap.put(Integer.valueOf(imsCall.uniqueId), Long.valueOf(j));
        log("updateVtDataUsage: call=" + imsCall + ", delta=" + jLongValue);
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        boolean dataRoaming = this.mPhone.getServiceState().getDataRoaming();
        long j2 = jLongValue / 2;
        this.mVtDataUsageSnapshot = new NetworkStats(jElapsedRealtime, 1).add(this.mVtDataUsageSnapshot).addEntry(new NetworkStats.Entry(getVtInterface(), -1, 1, 0, 1, dataRoaming ? 1 : 0, 1, j2, 0L, j2, 0L, 0L));
        NetworkStats networkStatsAdd = new NetworkStats(jElapsedRealtime, 1).add(this.mVtDataUsageUidSnapshot);
        if (this.mDefaultDialerUid.get() == -1) {
            this.mDefaultDialerUid.set(getPackageUid(this.mPhone.getContext(), ((TelecomManager) this.mPhone.getContext().getSystemService("telecom")).getDefaultDialerPackage()));
        }
        this.mVtDataUsageUidSnapshot = networkStatsAdd.addEntry(new NetworkStats.Entry(getVtInterface(), this.mDefaultDialerUid.get(), 1, 0, 1, dataRoaming ? 1 : 0, 1, j2, 0L, j2, 0L, 0L));
    }

    public String getVtInterface() {
        return "vt_data0" + this.mPhone.getSubId();
    }

    public ImsManager.ImsStatsCallback getImsStatsCallback() {
        return this.mImsStatsCallback;
    }

    @Override // com.android.internal.telephony.CallTracker
    protected void log(String str) {
        Rlog.d("ImsPhoneCallTracker", "[" + this.mPhone.getPhoneId() + "] " + str);
    }

    protected void loge(String str) {
        Rlog.e("ImsPhoneCallTracker", "[" + this.mPhone.getPhoneId() + "] " + str);
    }

    void logw(String str) {
        Rlog.w("ImsPhoneCallTracker", "[" + this.mPhone.getPhoneId() + "] " + str);
    }

    void logi(String str) {
        Rlog.i("ImsPhoneCallTracker", "[" + this.mPhone.getPhoneId() + "] " + str);
    }

    void logHoldSwapState(String str) {
        String str2;
        switch (this.mHoldSwitchingState) {
            case INACTIVE:
                str2 = "INACTIVE";
                break;
            case PENDING_SINGLE_CALL_HOLD:
                str2 = "PENDING_SINGLE_CALL_HOLD";
                break;
            case PENDING_SINGLE_CALL_UNHOLD:
                str2 = "PENDING_SINGLE_CALL_UNHOLD";
                break;
            case SWAPPING_ACTIVE_AND_HELD:
                str2 = "SWAPPING_ACTIVE_AND_HELD";
                break;
            case HOLDING_TO_ANSWER_INCOMING:
                str2 = "HOLDING_TO_ANSWER_INCOMING";
                break;
            case PENDING_RESUME_FOREGROUND_AFTER_FAILURE:
                str2 = "PENDING_RESUME_FOREGROUND_AFTER_FAILURE";
                break;
            case HOLDING_TO_DIAL_OUTGOING:
                str2 = "HOLDING_TO_DIAL_OUTGOING";
                break;
            case ENDING_TO_DIAL_OUTGOING:
            default:
                str2 = "???";
                break;
            case PENDING_RESUME_FOREGROUND_AFTER_HOLD:
                str2 = "PENDING_RESUME_FOREGROUND_AFTER_HOLD";
                break;
        }
        logi("holdSwapState set to " + str2 + " at " + str);
    }

    public void logState() {
        if (VERBOSE_STATE_LOGGING) {
            Rlog.v("ImsPhoneCallTracker", "Current IMS PhoneCall State:\n Foreground: " + this.mForegroundCall + "\n Background: " + this.mBackgroundCall + "\n Ringing: " + this.mRingingCall + "\n Handover: " + this.mHandoverCall + "\n");
        }
    }

    @Override // com.android.internal.telephony.CallTracker
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("ImsPhoneCallTracker extends:");
        indentingPrintWriter.increaseIndent();
        super.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" mVoiceCallEndedRegistrants=" + this.mVoiceCallEndedRegistrants);
        indentingPrintWriter.println(" mVoiceCallStartedRegistrants=" + this.mVoiceCallStartedRegistrants);
        indentingPrintWriter.println(" mRingingCall=" + this.mRingingCall);
        indentingPrintWriter.println(" mForegroundCall=" + this.mForegroundCall);
        indentingPrintWriter.println(" mBackgroundCall=" + this.mBackgroundCall);
        indentingPrintWriter.println(" mHandoverCall=" + this.mHandoverCall);
        indentingPrintWriter.println(" mPendingMO=" + this.mPendingMO);
        indentingPrintWriter.println(" mPhone=" + this.mPhone);
        indentingPrintWriter.println(" mDesiredMute=" + this.mDesiredMute);
        indentingPrintWriter.println(" mState=" + this.mState);
        indentingPrintWriter.println(" mMmTelCapabilities=" + this.mMmTelCapabilities);
        indentingPrintWriter.println(" mDefaultDialerUid=" + this.mDefaultDialerUid.get());
        indentingPrintWriter.println(" mVtDataUsageSnapshot=" + this.mVtDataUsageSnapshot);
        indentingPrintWriter.println(" mVtDataUsageUidSnapshot=" + this.mVtDataUsageUidSnapshot);
        indentingPrintWriter.println(" mIsConferenceEventPackageHandlingEnabled=" + this.mIsConferenceEventPackageEnabled);
        indentingPrintWriter.println(" mSupportCepOnPeer=" + this.mSupportCepOnPeer);
        if (this.mConfig != null) {
            indentingPrintWriter.print(" isDeviceToDeviceCommsSupported= " + this.mConfig.isD2DCommunicationSupported);
            indentingPrintWriter.println("(forceEnabled=" + this.mDeviceToDeviceForceEnabled + ")");
            if (this.mConfig.isD2DCommunicationSupported) {
                indentingPrintWriter.println(" mSupportD2DUsingRtp= " + this.mSupportD2DUsingRtp);
                indentingPrintWriter.println(" mSupportSdpForRtpHeaderExtensions= " + this.mSupportSdpForRtpHeaderExtensions);
            }
        }
        indentingPrintWriter.println(" mSrvccTypeSupported=" + this.mSrvccTypeSupported);
        indentingPrintWriter.println(" Event Log:");
        indentingPrintWriter.increaseIndent();
        this.mOperationLocalLog.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.flush();
        indentingPrintWriter.println("++++++++++++++++++++++++++++++++");
        try {
            ImsManager imsManager = this.mImsManager;
            if (imsManager != null) {
                imsManager.dump(fileDescriptor, indentingPrintWriter, strArr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<ImsPhoneConnection> arrayList = this.mConnections;
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        indentingPrintWriter.println("mConnections:");
        for (int i = 0; i < this.mConnections.size(); i++) {
            indentingPrintWriter.println("  [" + i + "]: " + this.mConnections.get(i));
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.ims.ImsException */
    public ImsEcbm getEcbmInterface() throws ImsException {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            throw getImsManagerIsNullException();
        }
        return imsManager.getEcbmInterface();
    }

    public boolean isInEmergencyCall() {
        return this.mIsInEmergencyCall;
    }

    public boolean isImsCapabilityAvailable(int i, int i2) throws ImsException {
        ImsManager imsManager = this.mImsManager;
        if (imsManager != null) {
            return imsManager.queryMmTelCapabilityStatus(i, i2);
        }
        return false;
    }

    public boolean isVoiceOverCellularImsEnabled() {
        return isImsCapabilityInCacheAvailable(1, 0) || isImsCapabilityInCacheAvailable(1, 3);
    }

    public boolean isVowifiEnabled() {
        return isImsCapabilityInCacheAvailable(1, 1) || isImsCapabilityInCacheAvailable(1, 2);
    }

    public boolean isVideoCallEnabled() {
        return this.mMmTelCapabilities.isCapable(2);
    }

    private boolean isImsCapabilityInCacheAvailable(int i, int i2) {
        return getImsRegistrationTech() == i2 && this.mMmTelCapabilities.isCapable(i);
    }

    @Override // com.android.internal.telephony.CallTracker
    public PhoneConstants.State getState() {
        return this.mState;
    }

    public int getImsRegistrationTech() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager != null) {
            return imsManager.getRegistrationTech();
        }
        return -1;
    }

    public void getImsRegistrationTech(Consumer<Integer> consumer) {
        ImsManager imsManager = this.mImsManager;
        if (imsManager != null) {
            imsManager.getRegistrationTech(consumer);
        } else {
            consumer.accept(-1);
        }
    }

    protected void setVideoCallProvider(ImsPhoneConnection imsPhoneConnection, ImsCall imsCall) throws RemoteException {
        IImsVideoCallProvider videoCallProvider = imsCall.getCallSession().getVideoCallProvider();
        if (videoCallProvider != null) {
            boolean z = this.mPhone.getContext().getResources().getBoolean(17892066);
            ImsVideoCallProviderWrapper imsVideoCallProviderWrapper = new ImsVideoCallProviderWrapper(videoCallProvider);
            if (z) {
                imsVideoCallProviderWrapper.setUseVideoPauseWorkaround(z);
            }
            imsPhoneConnection.setVideoProvider(imsVideoCallProviderWrapper);
            imsVideoCallProviderWrapper.registerForDataUsageUpdate(this, 22, imsCall);
            imsVideoCallProviderWrapper.addImsVideoProviderCallback(imsPhoneConnection);
        }
    }

    public boolean isUtEnabled() {
        return this.mMmTelCapabilities.isCapable(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PersistableBundle getCarrierConfigBundle(int i) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            loge("getCarrierConfigBundle: No carrier config service found");
            return null;
        }
        PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(i);
        if (configForSubId != null) {
            return configForSubId;
        }
        loge("getCarrierConfigBundle: carrier config is null, skipping.");
        return null;
    }

    protected String cleanseInstantLetteringMessage(String str) {
        PersistableBundle carrierConfigBundle;
        if (TextUtils.isEmpty(str) || (carrierConfigBundle = getCarrierConfigBundle(this.mPhone.getSubId())) == null) {
            return str;
        }
        String string = carrierConfigBundle.getString("carrier_instant_lettering_invalid_chars_string");
        if (!TextUtils.isEmpty(string)) {
            str = str.replaceAll(string, "");
        }
        String string2 = carrierConfigBundle.getString("carrier_instant_lettering_escaped_chars_string");
        return !TextUtils.isEmpty(string2) ? escapeChars(string2, str) : str;
    }

    private String escapeChars(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        for (char c : str2.toCharArray()) {
            if (str.contains(Character.toString(c))) {
                sb.append("\\");
            }
            sb.append(c);
        }
        return sb.toString();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPullCall
    public void pullExternalCall(String str, int i, int i2) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("CallPull", true);
        bundle.putInt(ImsExternalCallTracker.EXTRA_IMS_EXTERNAL_CALL_ID, i2);
        try {
            this.mPhone.notifyUnknownConnection(dial(str, i, bundle));
        } catch (CallStateException e) {
            loge("pullExternalCall failed - " + e);
        }
    }

    protected ImsException getImsManagerIsNullException() {
        return new ImsException("no ims manager", 102);
    }

    protected boolean shouldDisconnectActiveCallOnAnswer(ImsCall imsCall, ImsCall imsCall2) {
        if (imsCall == null || imsCall2 == null || !this.mDropVideoCallWhenAnsweringAudioCall) {
            return false;
        }
        boolean z = imsCall.isVideoCall() || (this.mTreatDowngradedVideoCallsAsVideoCalls && imsCall.wasVideoCall());
        boolean zIsWifiCall = imsCall.isWifiCall();
        boolean z2 = this.mImsManager.isWfcEnabledByPlatform() && this.mImsManager.isWfcEnabledByUser();
        boolean zIsVideoCall = imsCall2.isVideoCall();
        StringBuilder sb = new StringBuilder("shouldDisconnectActiveCallOnAnswer : isActiveCallVideo=");
        sb.append(z);
        sb.append(" isActiveCallOnWifi=");
        sb.append(zIsWifiCall);
        sb.append(" isIncomingCallAudio=");
        sb.append(!zIsVideoCall);
        sb.append(" isVowifiEnabled=");
        sb.append(z2);
        log(sb.toString());
        return z && zIsWifiCall && !zIsVideoCall && !z2;
    }

    public void registerPhoneStateListener(PhoneStateListener phoneStateListener) {
        this.mPhoneStateListeners.add(phoneStateListener);
    }

    public void unregisterPhoneStateListener(PhoneStateListener phoneStateListener) {
        this.mPhoneStateListeners.remove(phoneStateListener);
    }

    private void notifyPhoneStateChanged(PhoneConstants.State state, PhoneConstants.State state2) {
        Iterator it = this.mPhoneStateListeners.iterator();
        while (it.hasNext()) {
            ((PhoneStateListener) it.next()).onPhoneStateChanged(state, state2);
        }
    }

    protected void modifyVideoCall(ImsCall imsCall, int i) {
        ImsPhoneConnection imsPhoneConnectionFindConnection = findConnection(imsCall);
        if (imsPhoneConnectionFindConnection != null) {
            int videoState = imsPhoneConnectionFindConnection.getVideoState();
            if (imsPhoneConnectionFindConnection.getVideoProvider() != null) {
                imsPhoneConnectionFindConnection.getVideoProvider().onSendSessionModifyRequest(new VideoProfile(videoState), new VideoProfile(i));
            }
        }
    }

    public boolean isViLteDataMetered() {
        return this.mIsViLteDataMetered;
    }

    protected void onDataEnabledChanged(boolean z, int i) {
        log("onDataEnabledChanged: enabled=" + z + ", reason=" + i);
        this.mIsDataEnabled = z;
        if (!this.mIsViLteDataMetered) {
            StringBuilder sb = new StringBuilder("Ignore data ");
            sb.append(z ? "enabled" : "disabled");
            sb.append(" - carrier policy indicates that data is not metered for ViLTE calls.");
            log(sb.toString());
            return;
        }
        if (z && isRoamingOnAndRoamingSettingOff()) {
            log("Ignore data on when roaming");
            return;
        }
        ArrayList<ImsPhoneConnection> arrayList = this.mConnections;
        int size = arrayList.size();
        int i2 = 0;
        while (true) {
            boolean z2 = true;
            if (i2 >= size) {
                break;
            }
            ImsPhoneConnection imsPhoneConnection = arrayList.get(i2);
            i2++;
            ImsPhoneConnection imsPhoneConnection2 = imsPhoneConnection;
            ImsCall imsCall = imsPhoneConnection2.getImsCall();
            if (!z && (imsCall == null || !imsCall.isWifiCall())) {
                z2 = false;
            }
            imsPhoneConnection2.setLocalVideoCapable(z2);
        }
        int i3 = i == 1 ? 1405 : 1406;
        maybeNotifyDataDisabled(z, i3);
        handleDataEnabledChange(z, i3);
        if (this.mShouldUpdateImsConfigOnDisconnect || i == -1 || !this.mCarrierConfigLoadedForSubscription) {
            return;
        }
        updateImsServiceConfig();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateImsServiceConfig() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null || !this.mCarrierConfigLoadedForSubscription) {
            return;
        }
        imsManager.updateImsServiceConfig();
    }

    protected void maybeNotifyDataDisabled(boolean z, int i) {
        if (z) {
            return;
        }
        ArrayList<ImsPhoneConnection> arrayList = this.mConnections;
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            ImsPhoneConnection imsPhoneConnection = arrayList.get(i2);
            i2++;
            ImsPhoneConnection imsPhoneConnection2 = imsPhoneConnection;
            ImsCall imsCall = imsPhoneConnection2.getImsCall();
            if (imsCall != null && imsCall.isVideoCall() && !imsCall.isWifiCall() && imsPhoneConnection2.hasCapabilities(3)) {
                if (i == 1406) {
                    imsPhoneConnection2.onConnectionEvent("android.telephony.event.EVENT_DOWNGRADE_DATA_DISABLED", null);
                } else if (i == 1405) {
                    imsPhoneConnection2.onConnectionEvent("android.telephony.event.EVENT_DOWNGRADE_DATA_LIMIT_REACHED", null);
                }
            }
        }
    }

    protected void handleDataEnabledChange(boolean z, int i) {
        int i2 = 0;
        if (!z) {
            ArrayList<ImsPhoneConnection> arrayList = this.mConnections;
            int size = arrayList.size();
            while (i2 < size) {
                ImsPhoneConnection imsPhoneConnection = arrayList.get(i2);
                i2++;
                ImsPhoneConnection imsPhoneConnection2 = imsPhoneConnection;
                ImsCall imsCall = imsPhoneConnection2.getImsCall();
                if (imsCall != null && imsCall.isVideoCall() && !imsCall.isWifiCall()) {
                    log("handleDataEnabledChange - downgrading " + imsPhoneConnection2);
                    downgradeVideoCall(i, imsPhoneConnection2);
                }
            }
            return;
        }
        if (this.mSupportPauseVideo) {
            ArrayList<ImsPhoneConnection> arrayList2 = this.mConnections;
            int size2 = arrayList2.size();
            int i3 = 0;
            while (i3 < size2) {
                ImsPhoneConnection imsPhoneConnection3 = arrayList2.get(i3);
                i3++;
                ImsPhoneConnection imsPhoneConnection4 = imsPhoneConnection3;
                log("handleDataEnabledChange - resuming " + imsPhoneConnection4);
                if (VideoProfile.isPaused(imsPhoneConnection4.getVideoState()) && imsPhoneConnection4.wasVideoPausedFromSource(2)) {
                    imsPhoneConnection4.resumeVideo(2);
                }
            }
            this.mShouldUpdateImsConfigOnDisconnect = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downgradeVideoCall(int i, ImsPhoneConnection imsPhoneConnection) {
        ImsCall imsCall = imsPhoneConnection.getImsCall();
        if (imsCall != null) {
            if (imsPhoneConnection.hasCapabilities(3) && (!this.mSupportPauseVideo || ignoreCarrierPauseSupport())) {
                log("downgradeVideoCall :: callId=" + imsPhoneConnection.getTelecomCallId() + " Downgrade to audio");
                modifyVideoCall(imsCall, 0);
                return;
            }
            if (this.mSupportPauseVideo && i != 1407) {
                log("downgradeVideoCall :: callId=" + imsPhoneConnection.getTelecomCallId() + " Pause audio");
                this.mShouldUpdateImsConfigOnDisconnect = true;
                imsPhoneConnection.pauseVideo(2);
                return;
            }
            log("downgradeVideoCall :: callId=" + imsPhoneConnection.getTelecomCallId() + " Disconnect call.");
            imsCall.terminate(NtnRILConstants.MI_NTN_OEM_ERROR_1, i);
        }
    }

    protected void resetImsCapabilities() {
        log("Resetting Capabilities...");
        boolean zIsVideoCallEnabled = isVideoCallEnabled();
        this.mMmTelCapabilities = new MmTelFeature.MmTelCapabilities();
        this.mPhone.setServiceState(1);
        this.mPhone.resetImsRegistrationState();
        this.mPhone.processDisconnectReason(new ImsReasonInfo(106, 0));
        boolean zIsVideoCallEnabled2 = isVideoCallEnabled();
        if (zIsVideoCallEnabled != zIsVideoCallEnabled2) {
            this.mPhone.notifyForVideoCapabilityChanged(zIsVideoCallEnabled2);
        }
    }

    protected boolean isWifiConnected() {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mPhone.getContext().getSystemService("connectivity");
        return connectivityManager != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerForConnectivityChanges() {
        ConnectivityManager connectivityManager;
        if (this.mIsMonitoringConnectivity || !this.mNotifyVtHandoverToWifiFail || (connectivityManager = (ConnectivityManager) this.mPhone.getContext().getSystemService("connectivity")) == null) {
            return;
        }
        Rlog.i("ImsPhoneCallTracker", "registerForConnectivityChanges");
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(1);
        connectivityManager.registerNetworkCallback(builder.build(), this.mNetworkCallback);
        this.mIsMonitoringConnectivity = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterForConnectivityChanges() {
        ConnectivityManager connectivityManager;
        if (this.mIsMonitoringConnectivity && this.mNotifyVtHandoverToWifiFail && (connectivityManager = (ConnectivityManager) this.mPhone.getContext().getSystemService("connectivity")) != null) {
            Rlog.i("ImsPhoneCallTracker", "unregisterForConnectivityChanges");
            connectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            this.mIsMonitoringConnectivity = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleHandoverCheck() {
        ImsCall imsCall = this.mForegroundCall.getImsCall();
        ImsPhoneConnection firstConnection = this.mForegroundCall.getFirstConnection();
        if (!this.mNotifyVtHandoverToWifiFail || imsCall == null || !imsCall.isVideoCall() || firstConnection == null || firstConnection.getDisconnectCause() != 0 || hasMessages(25)) {
            return;
        }
        Rlog.i("ImsPhoneCallTracker", "scheduleHandoverCheck: schedule");
        sendMessageDelayed(obtainMessage(25, imsCall), 60000L);
    }

    public boolean isCarrierDowngradeOfVtCallSupported() {
        return this.mSupportDowngradeVtToAudio;
    }

    public void setDataEnabled(boolean z) {
        this.mIsDataEnabled = z;
    }

    private void handleFeatureCapabilityChanged(ImsFeature.Capabilities capabilities) {
        boolean zIsVideoCallEnabled = isVideoCallEnabled();
        StringBuilder sb = new StringBuilder(120);
        sb.append("handleFeatureCapabilityChanged: ");
        sb.append(capabilities);
        this.mMmTelCapabilities = new MmTelFeature.MmTelCapabilities(capabilities);
        boolean zIsVideoCallEnabled2 = isVideoCallEnabled();
        boolean z = zIsVideoCallEnabled != zIsVideoCallEnabled2;
        sb.append(" isVideoEnabledStateChanged=");
        sb.append(z);
        if (z) {
            log("handleFeatureCapabilityChanged - notifyForVideoCapabilityChanged=" + zIsVideoCallEnabled2);
            this.mPhone.notifyForVideoCapabilityChanged(zIsVideoCallEnabled2);
        }
        log(sb.toString());
        String str = "handleFeatureCapabilityChanged: isVolteEnabled=" + isVoiceOverCellularImsEnabled() + ", isVideoCallEnabled=" + isVideoCallEnabled() + ", isVowifiEnabled=" + isVowifiEnabled() + ", isUtEnabled=" + isUtEnabled();
        log(str);
        this.mRegLocalLog.log(str);
        this.mPhone.onFeatureCapabilityChanged();
        this.mPhone.getImsStats().onImsCapabilitiesChanged(getImsRegistrationTech(), this.mMmTelCapabilities);
        MiuiImsControllerStub.get(this.mPhone.getPhoneId()).sendIMSRegistedStateBroadcast(isVoiceOverCellularImsEnabled(), this.mPhone.getDefaultPhone());
    }

    public void onCallHoldReceived(ImsCall imsCall) {
        log("onCallHoldReceived");
        ImsPhoneConnection imsPhoneConnectionFindConnection = findConnection(imsCall);
        if (imsPhoneConnectionFindConnection != null) {
            if (!this.mOnHoldToneStarted && ((ImsPhoneCall.isLocalTone(imsCall) || MiuiImsControllerStub.get(this.mPhone.getPhoneId()).isCarrierConfigedLocalTone(this.mPhone) || this.mAlwaysPlayRemoteHoldTone) && imsPhoneConnectionFindConnection.getState() == Call.State.ACTIVE)) {
                this.mPhone.startOnHoldTone(imsPhoneConnectionFindConnection);
                this.mOnHoldToneStarted = true;
                this.mOnHoldToneId = System.identityHashCode(imsPhoneConnectionFindConnection);
            }
            imsPhoneConnectionFindConnection.setRemotelyHeld();
            this.mImsCallInfoTracker.updateImsCallStatus(imsPhoneConnectionFindConnection, true, false);
            if (this.mPhone.getContext().getResources().getBoolean(17892066) && this.mSupportPauseVideo && VideoProfile.isVideo(imsPhoneConnectionFindConnection.getVideoState())) {
                imsPhoneConnectionFindConnection.changeToPausedState();
            }
        }
        SuppServiceNotification suppServiceNotification = new SuppServiceNotification();
        suppServiceNotification.notificationType = 1;
        suppServiceNotification.code = 2;
        this.mPhone.notifySuppSvcNotification(suppServiceNotification);
    }

    public void setAlwaysPlayRemoteHoldTone(boolean z) {
        this.mAlwaysPlayRemoteHoldTone = z;
    }

    @Override // com.android.internal.telephony.CallTracker
    public ImsPhone getPhone() {
        return this.mPhone;
    }

    public void setSupportCepOnPeer(boolean z) {
        this.mSupportCepOnPeer = z;
    }

    public void injectTestConferenceState(ImsConferenceState imsConferenceState) {
        List<ConferenceParticipant> conferenceState = ImsCall.parseConferenceState(imsConferenceState);
        ArrayList<ImsPhoneConnection> connections = getConnections();
        int size = connections.size();
        int i = 0;
        while (i < size) {
            ImsPhoneConnection imsPhoneConnection = connections.get(i);
            i++;
            imsPhoneConnection.updateConferenceParticipants(conferenceState);
        }
    }

    public void setConferenceEventPackageEnabled(boolean z) {
        log("setConferenceEventPackageEnabled isEnabled=" + z);
        this.mIsConferenceEventPackageEnabled = z;
    }

    public boolean isConferenceEventPackageEnabled() {
        return this.mIsConferenceEventPackageEnabled;
    }

    public ImsCall.Listener getImsCallListener() {
        return this.mImsCallListener;
    }

    public ArrayList<ImsPhoneConnection> getConnections() {
        return this.mConnections;
    }

    public ImsPhoneConnection getPendingMO() {
        return this.mPendingMO;
    }

    public void setConfig(Config config) {
        this.mConfig = config;
    }

    private void handleConferenceFailed(ImsPhoneConnection imsPhoneConnection, ImsPhoneConnection imsPhoneConnection2) {
        if (imsPhoneConnection != null) {
            imsPhoneConnection.onConferenceMergeFailed();
            imsPhoneConnection.handleMergeComplete();
        }
        if (imsPhoneConnection2 != null) {
            imsPhoneConnection2.onConferenceMergeFailed();
            imsPhoneConnection2.handleMergeComplete();
        }
        this.mPhone.notifySuppServiceFailed(PhoneInternalInterface.SuppService.CONFERENCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isForegroundHigherPriority() {
        if (this.mBackgroundCall.getState().isAlive()) {
            return this.mForegroundCall.getFirstConnection().getCallPriority() > this.mBackgroundCall.getFirstConnection().getCallPriority();
        }
        return true;
    }

    private void initializeTerminalBasedCallWaiting() {
        boolean zIsCapable;
        ImsManager imsManager = this.mImsManager;
        if (imsManager != null) {
            try {
                zIsCapable = imsManager.isCapable(4L);
            } catch (ImsException e) {
                loge("initializeTerminalBasedCallWaiting : exception " + e);
                zIsCapable = false;
            }
        } else {
            zIsCapable = false;
        }
        logi("initializeTerminalBasedCallWaiting capable=" + zIsCapable);
        this.mPhone.setTerminalBasedCallWaitingSupported(zIsCapable);
        if (zIsCapable) {
            setTerminalBasedCallWaitingStatus(this.mPhone.getTerminalBasedCallWaitingState(false));
        }
    }

    public void setTerminalBasedCallWaitingStatus(int i) {
        if (i == -1 || this.mImsManager == null) {
            return;
        }
        try {
            log("setTerminalBasedCallWaitingStatus state=" + i);
            ImsManager imsManager = this.mImsManager;
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            imsManager.setTerminalBasedCallWaitingStatus(z);
        } catch (ImsException e) {
            loge("setTerminalBasedCallWaitingStatus : exception " + e);
        }
    }

    public void handleSrvccConnectionInfo(List<SrvccCall> list) {
        this.mPhone.getDefaultPhone().mCi.setSrvccCallInfo(convertToSrvccConnectionInfo(list), null);
    }

    public SrvccConnection[] convertToSrvccConnectionInfo(List<SrvccCall> list) {
        if (this.mSrvccTypeSupported.isEmpty() || list == null || list.isEmpty()) {
            loge("convertToSrvccConnectionInfo empty list");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (SrvccCall srvccCall : list) {
            if (isCallProfileSupported(srvccCall)) {
                addConnection(arrayList, srvccCall, findConnection(srvccCall.getCallId()));
            } else {
                logi("convertToSrvccConnectionInfo not supported state=" + srvccCall.getPreciseCallState());
            }
        }
        logi("convertToSrvccConnectionInfo size=" + arrayList.size());
        if (arrayList.isEmpty()) {
            return null;
        }
        return (SrvccConnection[]) arrayList.toArray(new SrvccConnection[0]);
    }

    public void handleSendAnbrQuery(int i, int i2, int i3) {
        log("handleSendAnbrQuery - mediaType=" + i);
        this.mPhone.getDefaultPhone().mCi.sendAnbrQuery(i, i2, i3, null);
    }

    public void triggerNotifyAnbr(int i, int i2, int i3) {
        ImsCall imsCall;
        if (!this.mForegroundCall.hasConnections() || (imsCall = this.mForegroundCall.getFirstConnection().getImsCall()) == null) {
            return;
        }
        log("triggerNotifyAnbr - mediaType=" + i);
        imsCall.callSessionNotifyAnbr(i, i2, i3);
    }

    private boolean isCallProfileSupported(SrvccCall srvccCall) {
        if (srvccCall == null) {
            loge("isCallProfileSupported null profile");
            return false;
        }
        int preciseCallState = srvccCall.getPreciseCallState();
        if (preciseCallState != 9) {
            switch (preciseCallState) {
                case 1:
                    return this.mSrvccTypeSupported.contains(0);
                case 2:
                    return this.mSrvccTypeSupported.contains(3);
                case 3:
                    return this.mSrvccTypeSupported.contains(2);
                case 4:
                    return this.mSrvccTypeSupported.contains(1);
                case 5:
                    return this.mSrvccTypeSupported.contains(1);
                case 6:
                    return this.mSrvccTypeSupported.contains(1);
                default:
                    loge("isCallProfileSupported invalid state=" + srvccCall.getPreciseCallState());
                    return false;
            }
        }
        return this.mSrvccTypeSupported.contains(2);
    }

    private synchronized ImsPhoneConnection findConnection(String str) {
        ImsCallSession callSession;
        ArrayList<ImsPhoneConnection> arrayList = this.mConnections;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            ImsPhoneConnection imsPhoneConnection = arrayList.get(i);
            i++;
            ImsPhoneConnection imsPhoneConnection2 = imsPhoneConnection;
            ImsCall imsCall = imsPhoneConnection2.getImsCall();
            if (imsCall != null && (callSession = imsCall.getCallSession()) != null && TextUtils.equals(callSession.getCallId(), str)) {
                return imsPhoneConnection2;
            }
        }
        return null;
    }

    private void addConnection(List list, SrvccCall srvccCall, ImsPhoneConnection imsPhoneConnection) {
        if (list == null || srvccCall == null) {
            return;
        }
        int preciseCallState = srvccCall.getPreciseCallState();
        if (!isAlive(preciseCallState)) {
            Rlog.i("ImsPhoneCallTracker", "addConnection not alive, " + preciseCallState);
            return;
        }
        List<ConferenceParticipant> conferenceParticipants = getConferenceParticipants(imsPhoneConnection);
        if (conferenceParticipants != null) {
            for (ConferenceParticipant conferenceParticipant : conferenceParticipants) {
                if (conferenceParticipant.getState() == 6) {
                    Rlog.i("ImsPhoneCallTracker", "addConnection participant is disconnected");
                } else {
                    SrvccConnection srvccConnection = new SrvccConnection(conferenceParticipant, preciseCallState);
                    Rlog.i("ImsPhoneCallTracker", "addConnection " + srvccConnection);
                    list.add(srvccConnection);
                }
            }
            return;
        }
        SrvccConnection srvccConnection2 = new SrvccConnection(srvccCall.getImsCallProfile(), imsPhoneConnection, preciseCallState);
        Rlog.i("ImsPhoneCallTracker", "addConnection " + srvccConnection2);
        list.add(srvccConnection2);
    }

    private List getConferenceParticipants(ImsPhoneConnection imsPhoneConnection) {
        ImsCall imsCall;
        List conferenceParticipants;
        if (!this.mSrvccTypeSupported.contains(3) || (imsCall = imsPhoneConnection.getImsCall()) == null || (conferenceParticipants = imsCall.getConferenceParticipants()) == null || conferenceParticipants.isEmpty()) {
            return null;
        }
        return conferenceParticipants;
    }

    public void triggerImsDeregistration(int i) {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            return;
        }
        try {
            imsManager.triggerDeregistration(i);
        } catch (ImsException e) {
            loge("triggerImsDeregistration: exception " + e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void updateImsRegistrationInfo() {
        boolean zIsCapable = this.mMmTelCapabilities.isCapable(1);
        ?? r0 = zIsCapable;
        if (this.mMmTelCapabilities.isCapable(2)) {
            r0 = (zIsCapable ? 1 : 0) | 2;
        }
        int i = r0;
        if (this.mMmTelCapabilities.isCapable(8)) {
            i = (r0 == true ? 1 : 0) | 4;
        }
        this.mPhone.updateImsRegistrationInfo(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerImsTrafficSession(int i, int i2, int i3, IImsTrafficSessionCallback iImsTrafficSessionCallback) {
        this.mImsTrafficSessions.put(Integer.valueOf(i), new ImsTrafficSession(i2, i3, iImsTrafficSessionCallback));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterImsTrafficSession(int i) {
        this.mImsTrafficSessions.remove(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ImsTrafficSession getImsTrafficSession(int i) {
        return (ImsTrafficSession) this.mImsTrafficSessions.get(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAllImsTrafficTypes() {
        boolean zIsEmpty = this.mImsTrafficSessions.isEmpty();
        logi("stopAllImsTrafficTypes empty=" + zIsEmpty);
        if (zIsEmpty) {
            return;
        }
        this.mImsTrafficSessions.forEachKey(1L, new Consumer() { // from class: com.android.internal.telephony.imsphone.ImsPhoneCallTracker$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$stopAllImsTrafficTypes$3((Integer) obj);
            }
        });
        this.mImsTrafficSessions.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopAllImsTrafficTypes$3(Integer num) {
        this.mPhone.stopImsTraffic(num.intValue(), null);
    }

    private void handleProvisioningChanged() {
        boolean z = false;
        while (true) {
            ProvisioningItem provisioningItem = (ProvisioningItem) this.mProvisioningItemQueue.poll();
            if (provisioningItem == null) {
                break;
            }
            int i = provisioningItem.mItem;
            Object obj = provisioningItem.mValue;
            if (obj instanceof Integer) {
                sendConfigChangedIntent(i, obj.toString());
                if (i == 28 || i == 10 || i == 11) {
                    z = true;
                }
            } else if (obj instanceof String) {
                sendConfigChangedIntent(i, obj.toString());
            }
        }
        if (z) {
            updateImsServiceConfig();
        }
    }

    private void sendConfigChangedIntent(int i, String str) {
        log("sendConfigChangedIntent - [" + i + ", " + str + "]");
        Intent intent = new Intent("com.android.intent.action.IMS_CONFIG_CHANGED");
        intent.putExtra("item", i);
        intent.putExtra("value", str);
        ImsPhone imsPhone = this.mPhone;
        if (imsPhone == null || imsPhone.getContext() == null) {
            return;
        }
        this.mPhone.getContext().sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    public CarrierRoamingNtnListener getCarrierRoamingNtnListener() {
        return this.mCarrierRoamingNtnListener;
    }

    public class CarrierRoamingNtnListener extends TelephonyCallback implements TelephonyCallback.CarrierRoamingNtnListener {
        public CarrierRoamingNtnListener() {
        }

        public void onCarrierRoamingNtnModeChanged(boolean z) {
            ImsPhoneCallTracker.this.log("onCarrierRoamingNtnModeChanged: active=" + z);
            if (ImsPhoneCallTracker.this.mTelephonyManager != null) {
                ServiceState serviceState = ImsPhoneCallTracker.this.mTelephonyManager.createForSubscriptionId(ImsPhoneCallTracker.this.mPhone.getSubId()).getServiceState();
                boolean z2 = serviceState != null && serviceState.getRoaming();
                ImsManager imsManager = ImsPhoneCallTracker.this.mImsManager;
                imsManager.setWfcRoamingSettingInternal(imsManager.isWfcRoamingEnabledByUser());
                ImsManager imsManager2 = ImsPhoneCallTracker.this.mImsManager;
                imsManager2.setWfcModeInternal(imsManager2.getWfcMode(z2));
            }
        }
    }

    public void setShouldAllowVtCallsInLowBatteryForTesting(boolean z) {
        this.mShouldAllowVtCallsInLowBattery = z;
    }

    protected CommandException getImsUssdCommandException(ImsReasonInfo imsReasonInfo) {
        return new CommandException(CommandException.Error.GENERIC_FAILURE);
    }

    protected ImsPhoneConnection makeImsPhoneConnectionForMO(String str, boolean z, boolean z2, ImsPhone.ImsDialArgs imsDialArgs) {
        return TelephonyComponentFactory.getInstance().inject(ImsPhoneConnection.class.getName()).makeImsPhoneConnection(this.mPhone, str, this, this.mForegroundCall, z, z2, imsDialArgs);
    }

    protected ImsPhoneConnection makeImsPhoneConnectionForMT(ImsCall imsCall, boolean z) {
        return TelephonyComponentFactory.getInstance().inject(ImsPhoneConnection.class.getName()).makeImsPhoneConnection(this.mPhone, imsCall, this, z ? this.mForegroundCall : this.mRingingCall, z);
    }

    protected ImsPhoneConnection makeImsPhoneConnectionForConference(String[] strArr) {
        return TelephonyComponentFactory.getInstance().inject(ImsPhoneConnection.class.getName()).makeImsPhoneConnection((Phone) this.mPhone, strArr, this, this.mForegroundCall, false);
    }

    protected ImsCall takeCall(IImsCallSession iImsCallSession, Bundle bundle) throws ImsException {
        return this.mImsManager.takeCall(iImsCallSession, this.mImsCallListener);
    }

    protected boolean canDailOnCallTerminated() {
        return this.mPendingMO != null;
    }

    protected AsyncResult getCallStateChangeAsyncResult() {
        return new AsyncResult((Object) null, (Object) null, (Throwable) null);
    }

    protected void disconnectPendingMo() {
        this.mPendingMO = null;
    }

    protected boolean isEmergencyCall(ImsPhoneConnection imsPhoneConnection) {
        return imsPhoneConnection.isEmergency();
    }
}
