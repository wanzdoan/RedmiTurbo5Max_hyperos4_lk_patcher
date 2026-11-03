package com.android.internal.telephony;

import android.R;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.hardware.radio.modem.ImeiInfo;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.WorkSource;
import android.preference.PreferenceManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.provider.Telephony;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.telephony.BarringInfo;
import android.telephony.CarrierConfigManager;
import android.telephony.CellBroadcastIdRange;
import android.telephony.CellIdentity;
import android.telephony.CellularIdentifierDisclosure;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.NetworkScanRequest;
import android.telephony.NetworkSecurityEvent;
import android.telephony.PhoneNumberUtils;
import android.telephony.RadioAccessFamily;
import android.telephony.SecurityAlgorithmUpdate;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.telephony.UiccAccessRule;
import android.telephony.UssdResponse;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Pair;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.LinkBandwidthEstimator;
import com.android.internal.telephony.data.TelephonyMultiInternetStub;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.gsm.SsData;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.android.internal.telephony.metrics.VoiceCallSessionStats;
import com.android.internal.telephony.security.CellularIdentifierDisclosureNotifier;
import com.android.internal.telephony.security.CellularNetworkSecuritySafetySource;
import com.android.internal.telephony.security.NullCipherNotifier;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.IccVmNotSupportedException;
import com.android.internal.telephony.uicc.IsimRecords;
import com.android.internal.telephony.uicc.IsimUiccRecords;
import com.android.internal.telephony.uicc.MiuiUiccControllerStub;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.internal.telephony.util.QtiImsUtils;
import com.android.internal.telephony.util.WorkerThread;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class GsmCdmaPhone extends Phone {
    public static final int CANCEL_ECM_TIMER = 1;
    public static final String CURR_SUBID = "curr_subid";
    public static int ENABLE_UICC_APPS_MAX_RETRIES = 3;
    public static final String LOG_TAG = "GsmCdmaPhone";
    public static final String PREF_KEY_HAS_APPLIED_2G_PROTECTION_DEFAULT = "pref_key_has_applied_2g_protection_default";
    protected static final int REAPPLY_UICC_APPS_SETTING_RETRY_TIME_GAP_IN_MS = 5000;
    public static final int RESTART_ECM_TIMER = 0;
    private BroadcastReceiver mBroadcastReceiver;
    private CarrierKeyDownloadManager mCDM;
    private CarrierInfoManager mCIM;
    public GsmCdmaCallTracker mCT;
    private AsyncResult mCacheSsnAr;
    protected final CallWaitingController mCallWaitingController;
    private final CarrierPrivilegesTracker mCarrierPrivilegesTracker;
    public CellBroadcastConfigTracker mCellBroadcastConfigTracker;
    public final List<Message> mCellularEventMessages;
    private PhoneInternalInterface.DialArgs mDialArgs;
    private Registrant mEcmExitRespRegistrant;
    private final RegistrantList mEcmTimerResetRegistrants;
    private final RegistrantList mEmergencyDomainSelectedRegistrants;
    public EmergencyNumberTracker mEmergencyNumberTracker;
    private String mEsn;
    private Runnable mExitEcmRunnable;
    protected IccPhoneBookInterfaceManager mIccPhoneBookIntManager;
    private IccSmsInterfaceManager mIccSmsInterfaceManager;
    private CellularIdentifierDisclosureNotifier mIdentifierDisclosureNotifier;
    protected String mImei;
    protected String mImeiSv;
    protected int mImeiType;
    private final ImsManagerFactory mImsManagerFactory;
    private boolean mIsIdentifierDisclosureTransparencySupported;
    private boolean mIsNullCipherAndIntegritySupported;
    private boolean mIsNullCipherNotificationSupported;
    private boolean mIsTestingEmergencyCallbackMode;
    private IsimUiccRecords mIsimUiccRecords;
    private String mManualNetworkSelectionPlmn;
    private String mMeid;
    private Boolean mModemN1Mode;
    private final Set mN1ModeDisallowedReasons;
    private NullCipherNotifier mNullCipherNotifier;
    protected ArrayList<MmiCode> mPendingMMIs;
    private int mPrecisePhoneType;
    public ServiceStateTracker mSST;
    private CellularNetworkSecuritySafetySource mSafetySource;
    private SIMRecords mSimRecords;
    private int mSimState;
    protected RegistrantList mSsnRegistrants;
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangedListener;
    private int mTelecomVoiceServiceStateOverride;
    protected Boolean mUiccApplicationsEnabled;
    private String mVmNumber;
    private final RegistrantList mVolteSilentRedialRegistrants;
    private PowerManager.WakeLock mWakeLock;
    private static final Object sBlocker = new Object();
    private static final Integer N1_MODE_DISALLOWED_REASON_CARRIER = 1;
    private static final Integer N1_MODE_DISALLOWED_REASON_IMS = 2;
    private static final int[] VOICE_PS_CALL_RADIO_TECHNOLOGY = {14, 19, 18, 20};

    public interface ImsManagerFactory {
        ImsManager create(Context context, int i);
    }

    private void handleEnterEmergencyCallbackMode(Message message) {
    }

    private void handleExitEmergencyCallbackMode(Message message) {
    }

    private static int ttyModeToPhoneMode(int i) {
        return (i == 1 || i == 2 || i == 3) ? 1 : 0;
    }

    protected boolean correctPhoneTypeForCdma(boolean z, int i) {
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public int getPhoneType() {
        return 1;
    }

    public void handleTimerInEmergencyCallbackMode(int i) {
    }

    protected boolean isCfEnable(int i) {
        return i == 1 || i == 3;
    }

    protected boolean isValidCommandInterfaceCFAction(int i) {
        return i == 0 || i == 1 || i == 3 || i == 4;
    }

    protected boolean isValidCommandInterfaceCFReason(int i) {
        return i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5;
    }

    protected boolean needResetPhbIntMgr() {
        return true;
    }

    protected void onCheckEmergencyCall(boolean z) {
    }

    protected boolean onCheckIfForceHandleInCallMmiByCS() {
        return false;
    }

    protected boolean onCheckIfIgnoreWifiOnlyModeCheck() {
        return false;
    }

    protected void onPendingMmiAdded(String str, GsmMmiCode gsmMmiCode) {
    }

    protected boolean onPreHandleSsnEvent(Object obj) {
        return false;
    }

    protected boolean onSetUseImsForCall(boolean z) {
        return z;
    }

    protected boolean onSetUseImsForUt(boolean z) {
        return z;
    }

    protected boolean ssOverGsmPipeIfNeeded(Message message) {
        return true;
    }

    protected boolean uiccProfileUpdateIfNeeded() {
        return true;
    }

    protected static class Cfu {
        final int mAction;
        public final Message mOnComplete;
        final int mReason;
        public final int mServiceClass;
        public final String mSetCfNumber;

        protected Cfu(String str, Message message, int i, int i2) {
            this.mSetCfNumber = str;
            this.mOnComplete = message;
            this.mServiceClass = 0;
            this.mAction = i;
            this.mReason = i2;
        }

        protected Cfu(String str, Message message, int i, int i2, int i3) {
            this.mSetCfNumber = str;
            this.mOnComplete = message;
            this.mServiceClass = i;
            this.mAction = i2;
            this.mReason = i3;
        }
    }

    public GsmCdmaPhone(Context context, CommandsInterface commandsInterface, PhoneNotifier phoneNotifier, int i, int i2, TelephonyComponentFactory telephonyComponentFactory, FeatureFlags featureFlags) {
        this(context, commandsInterface, phoneNotifier, false, i, i2, telephonyComponentFactory, featureFlags);
    }

    public GsmCdmaPhone(Context context, CommandsInterface commandsInterface, PhoneNotifier phoneNotifier, boolean z, int i, int i2, TelephonyComponentFactory telephonyComponentFactory, FeatureFlags featureFlags) {
        this(context, commandsInterface, phoneNotifier, z, i, i2, telephonyComponentFactory, new ImsManagerFactory() { // from class: com.android.internal.telephony.GsmCdmaPhone$$ExternalSyntheticLambda1
            @Override // com.android.internal.telephony.GsmCdmaPhone.ImsManagerFactory
            public final ImsManager create(Context context2, int i3) {
                return ImsManager.getInstance(context2, i3);
            }
        }, featureFlags);
    }

    public GsmCdmaPhone(Context context, CommandsInterface commandsInterface, PhoneNotifier phoneNotifier, boolean z, int i, int i2, TelephonyComponentFactory telephonyComponentFactory, ImsManagerFactory imsManagerFactory, FeatureFlags featureFlags) {
        super(i2 == 1 ? "GSM" : "CDMA", phoneNotifier, context, commandsInterface, z, i, telephonyComponentFactory, featureFlags);
        this.mSsnRegistrants = new RegistrantList();
        this.mUiccApplicationsEnabled = null;
        this.mIsTestingEmergencyCallbackMode = false;
        this.mExitEcmRunnable = new Runnable() { // from class: com.android.internal.telephony.GsmCdmaPhone.1
            @Override // java.lang.Runnable
            public void run() {
                GsmCdmaPhone.this.exitEmergencyCallbackMode();
            }
        };
        this.mPendingMMIs = new ArrayList<>();
        this.mEcmTimerResetRegistrants = new RegistrantList();
        this.mVolteSilentRedialRegistrants = new RegistrantList();
        this.mDialArgs = null;
        this.mEmergencyDomainSelectedRegistrants = new RegistrantList();
        this.mImeiType = -1;
        this.mSimState = 0;
        this.mCellBroadcastConfigTracker = CellBroadcastConfigTracker.make(this, null, true);
        this.mIsNullCipherAndIntegritySupported = false;
        this.mIsIdentifierDisclosureTransparencySupported = false;
        this.mIsNullCipherNotificationSupported = false;
        this.mCellularEventMessages = Collections.synchronizedList(new ArrayList());
        this.mTelecomVoiceServiceStateOverride = 1;
        this.mN1ModeDisallowedReasons = new ArraySet();
        this.mModemN1Mode = null;
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.GsmCdmaPhone.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Rlog.d(GsmCdmaPhone.LOG_TAG, "mBroadcastReceiver: action " + intent.getAction());
                String action = intent.getAction();
                if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                    if (GsmCdmaPhone.this.mPhoneId == intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1)) {
                        GsmCdmaPhone gsmCdmaPhone = GsmCdmaPhone.this;
                        gsmCdmaPhone.sendMessage(gsmCdmaPhone.obtainMessage(43));
                        return;
                    }
                    return;
                }
                if ("android.telecom.action.CURRENT_TTY_MODE_CHANGED".equals(action)) {
                    GsmCdmaPhone.this.updateTtyMode(intent.getIntExtra("android.telecom.extra.CURRENT_TTY_MODE", 0));
                    return;
                }
                if ("android.telecom.action.TTY_PREFERRED_MODE_CHANGED".equals(action)) {
                    GsmCdmaPhone.this.updateUiTtyMode(intent.getIntExtra("android.telecom.extra.TTY_PREFERRED_MODE", 0));
                    return;
                }
                if (("android.telephony.action.SIM_APPLICATION_STATE_CHANGED".equals(action) || "android.telephony.action.SIM_CARD_STATE_CHANGED".equals(action)) && GsmCdmaPhone.this.mPhoneId == intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1)) {
                    GsmCdmaPhone.this.mSimState = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
                    if (GsmCdmaPhone.this.mSimState == 10 && !GsmCdmaPhone.this.mCellularEventMessages.isEmpty()) {
                        synchronized (GsmCdmaPhone.sBlocker) {
                            try {
                                GsmCdmaPhone.this.logd("Executing CellularEventMessages size: " + GsmCdmaPhone.this.mCellularEventMessages.size());
                                Iterator<Message> it = GsmCdmaPhone.this.mCellularEventMessages.iterator();
                                while (it.hasNext()) {
                                    GsmCdmaPhone.this.sendMessage(it.next());
                                    it.remove();
                                }
                                GsmCdmaPhone.sBlocker.notifyAll();
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                    }
                    if (GsmCdmaPhone.this.mSimState == 10 && GsmCdmaPhone.this.currentSlotSubIdChanged()) {
                        GsmCdmaPhone.this.setNetworkSelectionModeAutomatic(null);
                    } else if (GsmCdmaPhone.this.mSimState == 1 || GsmCdmaPhone.this.mSimState == 0) {
                        GsmCdmaPhone.this.clearAllowedImsServices();
                    }
                    if (TelephonyBaseUtilsStub.isAutoDevice()) {
                        GsmCdmaPhone gsmCdmaPhone2 = GsmCdmaPhone.this;
                        if (gsmCdmaPhone2.mImsPhone == null || gsmCdmaPhone2.mSimState != 10) {
                            return;
                        }
                        TelephonyMultiInternetStub.get().setPhoneNumber(GsmCdmaPhone.this.mImsPhone.getSubscriberUriNumber(), GsmCdmaPhone.this.getPhoneId());
                    }
                }
            }
        };
        this.mPrecisePhoneType = i2;
        this.mVoiceCallSessionStats = new VoiceCallSessionStats(this.mPhoneId, this, featureFlags);
        this.mImsManagerFactory = imsManagerFactory;
        initOnce(commandsInterface);
        initRatSpecific(i2);
        this.mCarrierActionAgent = this.mTelephonyComponentFactory.inject(CarrierActionAgent.class.getName()).makeCarrierActionAgent(this);
        this.mCarrierSignalAgent = this.mTelephonyComponentFactory.inject(CarrierSignalAgent.class.getName()).makeCarrierSignalAgent(this);
        this.mAccessNetworksManager = this.mTelephonyComponentFactory.inject(AccessNetworksManager.class.getName()).makeAccessNetworksManager(this, getLooper(), featureFlags);
        this.mSignalStrengthController = this.mTelephonyComponentFactory.inject(SignalStrengthController.class.getName()).makeSignalStrengthController(this);
        this.mSST = this.mTelephonyComponentFactory.inject(ServiceStateTracker.class.getName()).makeServiceStateTracker(this, this.mCi, featureFlags);
        if (hasCalling() || hasMessaging()) {
            this.mEmergencyNumberTracker = this.mTelephonyComponentFactory.inject(EmergencyNumberTracker.class.getName()).makeEmergencyNumberTracker(this, this.mCi, this.mFeatureFlags);
        }
        this.mDeviceStateMonitor = this.mTelephonyComponentFactory.inject(DeviceStateMonitor.class.getName()).makeDeviceStateMonitor(this, this.mFeatureFlags);
        this.mDisplayInfoController = this.mTelephonyComponentFactory.inject(DisplayInfoController.class.getName()).makeDisplayInfoController(this, featureFlags);
        this.mDataNetworkController = this.mTelephonyComponentFactory.inject(DataNetworkController.class.getName()).makeDataNetworkController(this, getLooper(), featureFlags);
        this.mCarrierResolver = this.mTelephonyComponentFactory.inject(CarrierResolver.class.getName()).makeCarrierResolver(this, featureFlags);
        this.mCarrierPrivilegesTracker = new CarrierPrivilegesTracker(Looper.myLooper(), this, context, featureFlags);
        getCarrierActionAgent().registerForCarrierAction(0, this, 48, null, false);
        this.mSST.registerForNetworkAttached(this, 19, null);
        this.mSST.registerForVoiceRegStateOrRatChanged(this, 46, null);
        this.mSST.getServiceStateStats().registerDataNetworkControllerCallback();
        this.mSubscriptionManagerService.registerCallback(new SubscriptionManagerService.SubscriptionManagerServiceCallback(new Executor() { // from class: com.android.internal.telephony.GsmCdmaPhone$$ExternalSyntheticLambda3
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.GsmCdmaPhone.2
            @Override // com.android.internal.telephony.subscription.SubscriptionManagerService.SubscriptionManagerServiceCallback
            public void onUiccApplicationsEnabledChanged(int i3) {
                GsmCdmaPhone.this.reapplyUiccAppsEnablementIfNeeded(GsmCdmaPhone.ENABLE_UICC_APPS_MAX_RETRIES);
            }
        });
        this.mLinkBandwidthEstimator = this.mTelephonyComponentFactory.inject(LinkBandwidthEstimator.class.getName()).makeLinkBandwidthEstimator(this, getLooper());
        this.mCallWaitingController = new CallWaitingController(this);
        if (hasCalling()) {
            post(new Runnable() { // from class: com.android.internal.telephony.GsmCdmaPhone$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0();
                }
            });
            CallManager.getInstance(context).registerPhone(this);
        }
        SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.GsmCdmaPhone.3
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                GsmCdmaPhone.this.sendEmptyMessage(62);
            }
        };
        this.mSubscriptionsChangedListener = onSubscriptionsChangedListener;
        ((SubscriptionManager) context.getSystemService(SubscriptionManager.class)).addOnSubscriptionsChangedListener(new HandlerExecutor(this), onSubscriptionsChangedListener);
        logd("GsmCdmaPhone: constructor: sub = " + this.mPhoneId);
    }

    protected void initOnce(CommandsInterface commandsInterface) {
        if (commandsInterface instanceof SimulatedRadioControl) {
            this.mSimulatedRadioControl = (SimulatedRadioControl) commandsInterface;
        }
        MiuiPhoneStub.get(this.mPhoneId).init(this, new Object[0]);
        if (hasCalling()) {
            this.mCT = this.mTelephonyComponentFactory.inject(GsmCdmaCallTracker.class.getName()).makeGsmCdmaCallTracker(this, this.mFeatureFlags);
        }
        this.mIccPhoneBookIntManager = this.mTelephonyComponentFactory.inject(IccPhoneBookInterfaceManager.class.getName()).makeIccPhoneBookInterfaceManager(this);
        MiuiUiccControllerStub.get(this.mPhoneId).init(this, new Object[0]);
        this.mWakeLock = ((PowerManager) this.mContext.getSystemService("power")).newWakeLock(1, LOG_TAG);
        this.mIccSmsInterfaceManager = this.mTelephonyComponentFactory.inject(IccSmsInterfaceManager.class.getName()).makeIccSmsInterfaceManager(this, this.mFeatureFlags);
        this.mCi.registerForAvailable(this, 1, null);
        this.mCi.registerForOffOrNotAvailable(this, 8, null);
        this.mCi.registerForOn(this, 5, null);
        this.mCi.registerForRadioStateChanged(this, 47, null);
        this.mCi.registerUiccApplicationEnablementChanged(this, 53, null);
        this.mCi.setOnSuppServiceNotification(this, 2, null);
        this.mCi.setOnRegistrationFailed(this, 57, null);
        this.mCi.registerForBarringInfoChanged(this, 58, null);
        this.mCi.setOnUSSD(this, 7, null);
        this.mCi.setOnSs(this, 36, null);
        this.mCi.setEmergencyCallbackMode(this, 25, null);
        this.mCi.registerForExitEmergencyCallbackMode(this, 26, null);
        this.mCi.registerForModemReset(this, 45, null);
        this.mCi.registerForVoiceRadioTechChanged(this, 39, null);
        this.mCi.registerForLceInfo(this, 59, null);
        this.mCi.registerForCarrierInfoForImsiEncryption(this, 60, null);
        this.mCi.registerForTriggerImsDeregistration(this, 65, null);
        this.mCi.registerForNotifyAnbr(this, 68, null);
        IntentFilter intentFilter = new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intentFilter.addAction("android.telecom.action.CURRENT_TTY_MODE_CHANGED");
        intentFilter.addAction("android.telecom.action.TTY_PREFERRED_MODE_CHANGED");
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, "android.permission.MODIFY_PHONE_STATE", null, 2);
        this.mCDM = new CarrierKeyDownloadManager(this, WorkerThread.get().getLooper());
        this.mCIM = this.mTelephonyComponentFactory.inject(CarrierInfoManager.class.getName()).makeCarrierInfoManager(this);
        this.mCi.registerForImeiMappingChanged(this, 71, null);
        this.mSafetySource = this.mTelephonyComponentFactory.makeCellularNetworkSecuritySafetySource(this.mContext);
        logi("enable_identifier_disclosure_transparency_unsol_events is on. Registering for cellular identifier disclosures from phone " + getPhoneId());
        this.mIdentifierDisclosureNotifier = this.mTelephonyComponentFactory.inject(CellularIdentifierDisclosureNotifier.class.getName()).makeIdentifierDisclosureNotifier(this.mSafetySource);
        this.mCi.registerForCellularIdentifierDisclosures(this, 72, null);
        logi("enable_modem_cipher_transparency_unsol_events is on. Registering for security algorithm updates from phone " + getPhoneId());
        this.mNullCipherNotifier = this.mTelephonyComponentFactory.inject(NullCipherNotifier.class.getName()).makeNullCipherNotifier(this.mSafetySource);
        this.mCi.registerForSecurityAlgorithmUpdates(this, 74, null);
        this.mCi.registerForNetworkSecurityEvents(this, 76, null);
        initializeCarrierApps();
    }

    protected void initRatSpecific(int i) {
        this.mPendingMMIs.clear();
        if (needResetPhbIntMgr()) {
            this.mIccPhoneBookIntManager.updateIccRecords(null);
        }
        MiuiUiccControllerStub.get(this.mPhoneId).updateIccRecords(this.mPhoneId, null);
        this.mPrecisePhoneType = i;
        logd("Precise phone type " + this.mPrecisePhoneType);
        TelephonyManager telephonyManagerFrom = TelephonyManager.from(this.mContext);
        UiccProfile uiccProfile = getUiccProfile();
        this.mCi.setPhoneType(1);
        telephonyManagerFrom.setPhoneType(getPhoneId(), 1);
        if (uiccProfile != null) {
            uiccProfile.setVoiceRadioTech(3);
        }
        TelephonyCommonUtilsStub.get().isMiuiRom();
    }

    private void initializeCarrierApps() {
        if (this.mPhoneId != 0) {
            return;
        }
        logd("initializeCarrierApps");
        this.mContext.registerReceiverForAllUsers(new BroadcastReceiver() { // from class: com.android.internal.telephony.GsmCdmaPhone.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("android.intent.action.USER_FOREGROUND".equals(intent.getAction())) {
                    UserHandle userHandle = (UserHandle) intent.getParcelableExtra("android.intent.extra.USER");
                    CarrierAppUtils.disableCarrierAppsUntilPrivileged(GsmCdmaPhone.this.mContext.getOpPackageName(), TelephonyManager.getDefault(), userHandle != null ? userHandle.getIdentifier() : 0, GsmCdmaPhone.this.mContext);
                }
            }
        }, new IntentFilter("android.intent.action.USER_FOREGROUND"), null, null);
        CarrierAppUtils.disableCarrierAppsUntilPrivileged(this.mContext.getOpPackageName(), TelephonyManager.getDefault(), ActivityManager.getCurrentUser(), this.mContext);
    }

    private void updateLinkCapacityEstimate(List list) {
        logd("updateLinkCapacityEstimate: lce list=" + list);
        if (list == null) {
            return;
        }
        notifyLinkCapacityEstimateChanged(list);
    }

    protected void finalize() {
        logd("GsmCdmaPhone finalized");
        PowerManager.WakeLock wakeLock = this.mWakeLock;
        if (wakeLock == null || !wakeLock.isHeld()) {
            return;
        }
        Rlog.e(LOG_TAG, "UNEXPECTED; mWakeLock is held when finalizing.");
        this.mWakeLock.release();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public ServiceState getServiceState() {
        ServiceStateTracker serviceStateTracker = this.mSST;
        ServiceState serviceState = serviceStateTracker != null ? serviceStateTracker.getServiceState() : new ServiceState();
        Phone phone = this.mImsPhone;
        return mergeVoiceServiceStates(serviceState, phone != null ? phone.getServiceState() : new ServiceState(), this.mTelecomVoiceServiceStateOverride);
    }

    @Override // com.android.internal.telephony.Phone
    public void setVoiceServiceStateOverride(boolean z) {
        ServiceStateTracker serviceStateTracker;
        int i = !z ? 1 : 0;
        boolean z2 = i != this.mTelecomVoiceServiceStateOverride;
        this.mTelecomVoiceServiceStateOverride = i;
        if (!z2 || (serviceStateTracker = this.mSST) == null) {
            return;
        }
        serviceStateTracker.onTelecomVoiceServiceStateOverrideChanged();
        this.mSST.getServiceStateStats().onVoiceServiceStateOverrideChanged(z);
    }

    @Override // com.android.internal.telephony.Phone
    public void getCellIdentity(WorkSource workSource, Message message) {
        this.mSST.requestCellIdentity(workSource, message);
    }

    @Override // com.android.internal.telephony.Phone
    public PhoneConstants.State getState() {
        PhoneConstants.State state;
        if (!hasCalling()) {
            return PhoneConstants.State.IDLE;
        }
        Phone phone = this.mImsPhone;
        return (phone == null || (state = phone.getState()) == PhoneConstants.State.IDLE) ? this.mCT.mState : state;
    }

    @Override // com.android.internal.telephony.Phone
    public ServiceStateTracker getServiceStateTracker() {
        return this.mSST;
    }

    @Override // com.android.internal.telephony.Phone
    public EmergencyNumberTracker getEmergencyNumberTracker() {
        return this.mEmergencyNumberTracker;
    }

    @Override // com.android.internal.telephony.Phone
    public CallTracker getCallTracker() {
        return this.mCT;
    }

    @Override // com.android.internal.telephony.Phone
    public AccessNetworksManager getAccessNetworksManager() {
        return this.mAccessNetworksManager;
    }

    @Override // com.android.internal.telephony.Phone
    public DeviceStateMonitor getDeviceStateMonitor() {
        return this.mDeviceStateMonitor;
    }

    @Override // com.android.internal.telephony.Phone
    public DisplayInfoController getDisplayInfoController() {
        return this.mDisplayInfoController;
    }

    @Override // com.android.internal.telephony.Phone
    public SignalStrengthController getSignalStrengthController() {
        return this.mSignalStrengthController;
    }

    @Override // com.android.internal.telephony.Phone
    public void updateVoiceMail() {
        IccRecords iccRecords = this.mIccRecords.get();
        int voiceMessageCount = iccRecords != null ? iccRecords.getVoiceMessageCount() : 0;
        if (voiceMessageCount == -2) {
            voiceMessageCount = getStoredVoiceMessageCount();
        }
        logd("updateVoiceMail countVoiceMessages = " + voiceMessageCount + " subId " + getSubId());
        setVoiceMessageCount(voiceMessageCount);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public List<? extends MmiCode> getPendingMmiCodes() {
        return this.mPendingMMIs;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public int getDataActivityState() {
        return getDataNetworkController().getDataActivity();
    }

    public void notifyPhoneStateChanged() {
        this.mNotifier.notifyPhoneState(this);
    }

    public void notifyPreciseCallStateChanged() {
        this.mPreciseCallStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, this, (Throwable) null));
        this.mNotifier.notifyPreciseCallState(this, null, null, null);
    }

    public void notifyNewRingingConnection(Connection connection) {
        super.notifyNewRingingConnectionP(connection);
    }

    public void notifyDisconnect(Connection connection) {
        this.mDisconnectRegistrants.notifyResult(connection);
        this.mNotifier.notifyDisconnectCause(this, connection.getDisconnectCause(), connection.getPreciseDisconnectCause());
    }

    public void notifyUnknownConnection(Connection connection) {
        super.notifyUnknownConnectionP(connection);
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isInEmergencyCall() {
        if (hasCalling()) {
            return ((TelecomManager) this.mContext.getSystemService(TelecomManager.class)).isInEmergencyCall();
        }
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isInEmergencySmsMode() {
        if (super.isInEmergencySmsMode()) {
            return true;
        }
        Phone phone = this.mImsPhone;
        return phone != null && phone.isInEmergencySmsMode();
    }

    public void notifySuppServiceFailed(PhoneInternalInterface.SuppService suppService) {
        this.mSuppServiceFailedRegistrants.notifyResult(suppService);
    }

    public void notifyServiceStateChanged(ServiceState serviceState) {
        if (MiuiTelephonyManagerStub.get().isSatelliteBox()) {
            logi("notifyServiceStateChanged - skip for ntn mode on satellite box");
        } else if (SystemProperties.getBoolean("ro.radio.noril", false)) {
            logi("notifyServiceStateChanged - skip for Wifi only PAD with NetworkRelay");
        } else {
            super.notifyServiceStateChangedP(serviceState);
        }
    }

    void notifyServiceStateChangedForSubId(ServiceState serviceState, int i) {
        if (MiuiTelephonyManagerStub.get().isSatelliteBox()) {
            logi("notifyServiceStateChangedForSubId - skip for ntn mode on satellite box");
        } else if (SystemProperties.getBoolean("ro.radio.noril", false)) {
            logi("notifyServiceStateChanged - skip for Wifi only PAD with NetworkRelay");
        } else {
            super.notifyServiceStateChangedPForSubId(serviceState, i);
        }
    }

    public void notifyLocationChanged(CellIdentity cellIdentity) {
        this.mNotifier.notifyCellLocation(this, cellIdentity);
    }

    @Override // com.android.internal.telephony.Phone
    public void notifyCallForwardingIndicator() {
        this.mNotifier.notifyCallForwardingChanged(this);
    }

    @Override // com.android.internal.telephony.Phone
    public void notifyMigrateUssd(String str, ResultReceiver resultReceiver) throws UnsupportedOperationException {
        this.mPendingMMIs.add(GsmMmiCode.newFromDialString(str, this, this.mUiccApplication.get(), resultReceiver));
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void registerForSuppServiceNotification(Handler handler, int i, Object obj) {
        this.mSsnRegistrants.addUnique(handler, i, obj);
        if (this.mCacheSsnAr == null || handler == null) {
            return;
        }
        AsyncResult asyncResult = this.mCacheSsnAr;
        handler.handleMessage(handler.obtainMessage(i, new AsyncResult(obj, asyncResult.result, asyncResult.exception)));
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void unregisterForSuppServiceNotification(Handler handler) {
        this.mSsnRegistrants.remove(handler);
        this.mCacheSsnAr = null;
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForSimRecordsLoaded(Handler handler, int i, Object obj) {
        this.mSimRecordsLoadedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForSimRecordsLoaded(Handler handler) {
        this.mSimRecordsLoadedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void acceptCall(int i) throws CallStateException {
        if (!hasCalling()) {
            throw new CallStateException();
        }
        Phone phone = this.mImsPhone;
        if (phone != null && phone.getRingingCall().isRinging()) {
            phone.acceptCall(i);
        } else {
            this.mCT.acceptCall();
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void rejectCall() throws CallStateException {
        if (!hasCalling()) {
            throw new CallStateException();
        }
        this.mCT.rejectCall();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void switchHoldingAndActive() throws CallStateException {
        this.mCT.switchWaitingOrHoldingAndActive();
    }

    @Override // com.android.internal.telephony.Phone
    public String getIccSerialNumber() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getIccId();
        }
        return null;
    }

    @Override // com.android.internal.telephony.Phone
    public String getFullIccSerialNumber() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getFullIccId();
        }
        return null;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean canConference() {
        if (!hasCalling()) {
            return false;
        }
        Phone phone = this.mImsPhone;
        if (phone == null || !phone.canConference()) {
            return this.mCT.canConference();
        }
        return true;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void conference() {
        Phone phone = this.mImsPhone;
        if (phone != null && phone.canConference()) {
            logd("conference() - delegated to IMS phone");
            try {
                this.mImsPhone.conference();
                return;
            } catch (CallStateException e) {
                loge(e.toString());
                return;
            }
        }
        this.mCT.conference();
    }

    @Override // com.android.internal.telephony.Phone
    public void enableEnhancedVoicePrivacy(boolean z, Message message) {
        loge("enableEnhancedVoicePrivacy: not expected on GSM");
    }

    @Override // com.android.internal.telephony.Phone
    public void getEnhancedVoicePrivacy(Message message) {
        loge("getEnhancedVoicePrivacy: not expected on GSM");
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void clearDisconnected() {
        if (hasCalling()) {
            this.mCT.clearDisconnected();
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean canTransfer() {
        if (hasCalling()) {
            return this.mCT.canTransfer();
        }
        loge("canTransfer: not possible in CDMA");
        return false;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void explicitCallTransfer() {
        if (hasCalling()) {
            this.mCT.explicitCallTransfer();
        } else {
            loge("explicitCallTransfer: not possible in CDMA");
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public GsmCdmaCall getForegroundCall() {
        if (hasCalling()) {
            return this.mCT.mForegroundCall;
        }
        return null;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public GsmCdmaCall getBackgroundCall() {
        if (hasCalling()) {
            return this.mCT.mBackgroundCall;
        }
        return null;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public Call getRingingCall() {
        if (!hasCalling()) {
            return null;
        }
        Phone phone = this.mImsPhone;
        if (phone != null && phone.getRingingCall().isRinging()) {
            return phone.getRingingCall();
        }
        if (!this.mCT.mRingingCall.isRinging() && this.mCT.getRingingHandoverConnection() != null && this.mCT.getRingingHandoverConnection().getCall() != null && this.mCT.getRingingHandoverConnection().getCall().isRinging()) {
            return this.mCT.getRingingHandoverConnection().getCall();
        }
        return this.mCT.mRingingCall;
    }

    @Override // com.android.internal.telephony.Phone
    public CarrierPrivilegesTracker getCarrierPrivilegesTracker() {
        return this.mCarrierPrivilegesTracker;
    }

    private static ServiceState mergeVoiceServiceStates(ServiceState serviceState, ServiceState serviceState2, int i) {
        if (serviceState.getState() != 0) {
            if (i != 0) {
                i = serviceState2.getState() == 0 ? serviceState.getDataRegistrationState() : 1;
            }
            if (i == 0) {
                ServiceState serviceState3 = new ServiceState(serviceState);
                serviceState3.setVoiceRegState(i);
                serviceState3.setEmergencyOnly(false);
                return serviceState3;
            }
        }
        return serviceState;
    }

    private boolean handleCallDeflectionIncallSupplementaryService(String str) {
        if (!hasCalling() || str.length() > 1) {
            return false;
        }
        Call.State state = getRingingCall().getState();
        Call.State state2 = Call.State.IDLE;
        if (state != state2) {
            logd("MmiCode 0: rejectCall");
            try {
                this.mCT.rejectCall();
            } catch (CallStateException e) {
                Rlog.d(LOG_TAG, "reject failed", e);
                notifySuppServiceFailed(PhoneInternalInterface.SuppService.REJECT);
            }
        } else if (getBackgroundCall().getState() != state2) {
            logd("MmiCode 0: hangupWaitingOrBackground");
            this.mCT.hangupWaitingOrBackground();
        }
        return true;
    }

    private boolean handleCallWaitingIncallSupplementaryService(String str) {
        int length = str.length();
        if (!hasCalling() || length > 2) {
            return false;
        }
        GsmCdmaCall foregroundCall = getForegroundCall();
        try {
            if (length > 1) {
                int iCharAt = str.charAt(1) - '0';
                if (iCharAt >= 1 && iCharAt <= 19) {
                    logd("MmiCode 1: hangupConnectionByIndex " + iCharAt);
                    this.mCT.hangupConnectionByIndex(foregroundCall, iCharAt);
                }
            } else if (foregroundCall.getState() != Call.State.IDLE) {
                logd("MmiCode 1: hangup foreground");
                this.mCT.hangupForegroundResumeBackground();
            } else {
                logd("MmiCode 1: switchWaitingOrHoldingAndActive");
                this.mCT.switchWaitingOrHoldingAndActive();
            }
        } catch (CallStateException e) {
            Rlog.d(LOG_TAG, "hangup failed", e);
            notifySuppServiceFailed(PhoneInternalInterface.SuppService.HANGUP);
        }
        return true;
    }

    private boolean handleCallHoldIncallSupplementaryService(String str) {
        int length = str.length();
        if (length > 2) {
            return false;
        }
        GsmCdmaCall foregroundCall = getForegroundCall();
        if (length > 1) {
            try {
                int iCharAt = str.charAt(1) - '0';
                GsmCdmaConnection connectionByIndex = this.mCT.getConnectionByIndex(foregroundCall, iCharAt);
                if (connectionByIndex != null && iCharAt >= 1 && iCharAt <= 19) {
                    logd("MmiCode 2: separate call " + iCharAt);
                    this.mCT.separate(connectionByIndex);
                } else {
                    logd("separate: invalid call index " + iCharAt);
                    notifySuppServiceFailed(PhoneInternalInterface.SuppService.SEPARATE);
                }
            } catch (CallStateException e) {
                Rlog.d(LOG_TAG, "separate failed", e);
                notifySuppServiceFailed(PhoneInternalInterface.SuppService.SEPARATE);
            }
        } else {
            try {
                if (getRingingCall().getState() != Call.State.IDLE) {
                    logd("MmiCode 2: accept ringing call");
                    this.mCT.acceptCall();
                } else {
                    logd("MmiCode 2: switchWaitingOrHoldingAndActive");
                    this.mCT.switchWaitingOrHoldingAndActive();
                }
            } catch (CallStateException e2) {
                Rlog.d(LOG_TAG, "switch failed", e2);
                notifySuppServiceFailed(PhoneInternalInterface.SuppService.SWITCH);
            }
        }
        return true;
    }

    private boolean handleMultipartyIncallSupplementaryService(String str) {
        if (str.length() > 1) {
            return false;
        }
        logd("MmiCode 3: merge calls");
        conference();
        return true;
    }

    private boolean handleEctIncallSupplementaryService(String str) {
        if (str.length() != 1) {
            return false;
        }
        logd("MmiCode 4: explicit call transfer");
        explicitCallTransfer();
        return true;
    }

    private boolean handleCcbsIncallSupplementaryService(String str) {
        if (str.length() > 1) {
            return false;
        }
        Rlog.i(LOG_TAG, "MmiCode 5: CCBS not supported!");
        notifySuppServiceFailed(PhoneInternalInterface.SuppService.UNKNOWN);
        return true;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean handleInCallMmiCommands(String str) throws CallStateException {
        Phone phone = this.mImsPhone;
        if (!onCheckIfForceHandleInCallMmiByCS() && phone != null && phone.getServiceState().getState() == 0) {
            return phone.handleInCallMmiCommands(str);
        }
        if (!isInCall() || TextUtils.isEmpty(str)) {
            return false;
        }
        switch (str.charAt(0)) {
            case '0':
                return onHandleInCallMmi0(str);
            case '1':
                return handleCallWaitingIncallSupplementaryService(str);
            case '2':
                return handleCallHoldIncallSupplementaryService(str);
            case '3':
                return handleMultipartyIncallSupplementaryService(str);
            case '4':
                return handleEctIncallSupplementaryService(str);
            case '5':
                return handleCcbsIncallSupplementaryService(str);
            default:
                return false;
        }
    }

    public boolean isInCall() {
        if (hasCalling()) {
            return getForegroundCall().getState().isAlive() || getBackgroundCall().getState().isAlive() || getRingingCall().getState().isAlive();
        }
        return false;
    }

    private boolean useImsForCall(PhoneInternalInterface.DialArgs dialArgs) {
        Phone phone;
        if (!isImsUseEnabled() || (phone = this.mImsPhone) == null) {
            return false;
        }
        return (phone.isVoiceOverCellularImsEnabled() || this.mImsPhone.isWifiCallingEnabled() || (this.mImsPhone.isVideoEnabled() && VideoProfile.isVideo(dialArgs.videoState))) && this.mImsPhone.getServiceState().getState() == 0;
    }

    public boolean useImsForEmergency() {
        return this.mImsPhone != null && ((CarrierConfigManager) this.mContext.getSystemService("carrier_config")).getConfigForSubId(getSubId()).getBoolean("carrier_use_ims_first_for_emergency_bool") && ImsManager.getInstance(this.mContext, this.mPhoneId).isNonTtyOrTtyOnVolteEnabled() && this.mImsPhone.isImsAvailable();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public Connection startConference(String[] strArr, PhoneInternalInterface.DialArgs dialArgs) throws CallStateException {
        Phone phone = this.mImsPhone;
        boolean zUseImsForCall = useImsForCall(dialArgs);
        logd("useImsForCall=" + zUseImsForCall);
        if (zUseImsForCall) {
            try {
                logd("Trying IMS PS Conference call");
                return phone.startConference(strArr, dialArgs);
            } catch (CallStateException e) {
                logd("IMS PS conference call exception " + e + "useImsForCall =" + zUseImsForCall + ", imsPhone =" + phone);
                CallStateException callStateException = new CallStateException(e.getError(), e.getMessage());
                callStateException.setStackTrace(e.getStackTrace());
                throw callStateException;
            }
        }
        Phone$$ExternalSyntheticBUOutline2.m(1, "cannot dial conference call in out of service");
        return null;
    }

    protected boolean shallDialOnCircuitSwitch(Bundle bundle) {
        return bundle != null && bundle.getInt(QtiImsUtils.EXTRA_CALL_DOMAIN, 0) == 1;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public Connection dial(String str, PhoneInternalInterface.DialArgs dialArgs, Consumer<Phone> consumer) throws CallStateException {
        boolean zIsEmergencyNumber;
        boolean z;
        boolean z2;
        Object objValueOf;
        Object objValueOf2;
        String str2;
        PhoneInternalInterface.DialArgs dialArgs2;
        ServiceStateTracker serviceStateTracker;
        ServiceStateTracker serviceStateTracker2;
        ServiceStateTracker serviceStateTracker3;
        boolean z3;
        if (!hasCalling()) {
            CallManager$$ExternalSyntheticBUOutline0.m("Calling feature is not supported!");
            return null;
        }
        String strCheckForTestEmergencyNumber = checkForTestEmergencyNumber(str);
        boolean zEquals = TextUtils.equals(str, strCheckForTestEmergencyNumber);
        if (zEquals) {
            strCheckForTestEmergencyNumber = str;
        } else {
            logi("dialString replaced for possible emergency number: " + str + " -> " + strCheckForTestEmergencyNumber);
        }
        PersistableBundle configForSubId = ((CarrierConfigManager) this.mContext.getSystemService("carrier_config")).getConfigForSubId(getSubId());
        boolean z4 = configForSubId.getBoolean("support_wps_over_ims_bool");
        boolean z5 = configForSubId.getBoolean("use_only_dialed_sim_ecc_list_bool");
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        if (z5) {
            zIsEmergencyNumber = getEmergencyNumberTracker().isEmergencyNumber(strCheckForTestEmergencyNumber);
            logi("dial; isEmergency=" + zIsEmergencyNumber + " (based on this phone only); globalIsEmergency=" + telephonyManager.isEmergencyNumber(strCheckForTestEmergencyNumber));
        } else {
            zIsEmergencyNumber = telephonyManager.isEmergencyNumber(strCheckForTestEmergencyNumber);
            logi("dial; isEmergency=" + zIsEmergencyNumber + " (based on all phones)");
        }
        if (dialArgs.isEmergency) {
            logi("dial; isEmergency=" + zIsEmergencyNumber + " (domain selection module)");
            zIsEmergencyNumber = true;
        }
        onCheckEmergencyCall(zIsEmergencyNumber);
        boolean zIsWpsCallNumber = PhoneNumberUtils.isWpsCallNumber(strCheckForTestEmergencyNumber);
        ImsPhone.ImsDialArgs imsDialArgsBuild = ImsPhone.ImsDialArgs.Builder.from(dialArgs).setIsEmergency(zIsEmergencyNumber).setIsWpsCall(zIsWpsCallNumber).build();
        this.mDialArgs = imsDialArgsBuild;
        Phone phone = this.mImsPhone;
        boolean z6 = zIsEmergencyNumber && useImsForEmergency();
        String strExtractNetworkPortionAlt = PhoneNumberUtils.extractNetworkPortionAlt(PhoneNumberUtils.stripSeparators(strCheckForTestEmergencyNumber));
        boolean z7 = (strExtractNetworkPortionAlt.startsWith("*") || strExtractNetworkPortionAlt.startsWith("#")) && strExtractNetworkPortionAlt.endsWith("#");
        boolean zIsSuppServiceCodes = ImsPhoneMmiCode.isSuppServiceCodes(strExtractNetworkPortionAlt, this);
        boolean z8 = z7 && !zIsSuppServiceCodes;
        boolean z9 = phone != null && phone.isUtEnabled();
        boolean z10 = useImsForCall(imsDialArgsBuild) && (!zIsWpsCallNumber || z4);
        boolean zOnSetUseImsForUt = onSetUseImsForUt(z9);
        boolean zOnSetUseImsForCall = onSetUseImsForCall(z10);
        Bundle bundle = imsDialArgsBuild.intentExtras;
        if (bundle == null || !bundle.containsKey("dial_domain")) {
            z = zOnSetUseImsForCall;
            z2 = zOnSetUseImsForUt;
        } else {
            int i = bundle.getInt("dial_domain");
            logi("dial domain=" + i);
            if (i == 2) {
                if (zIsEmergencyNumber) {
                    z3 = false;
                    z6 = true;
                } else {
                    if (!z7 || z8) {
                        z3 = true;
                    } else {
                        loge("dial unexpected Ut domain selection, ignored");
                        z3 = false;
                    }
                    z6 = false;
                }
                bundle.remove("dial_domain");
                z = z3;
                z2 = false;
            } else {
                if (i == 4) {
                    if (zIsEmergencyNumber) {
                        bundle.putString("CallRadioTech", String.valueOf(18));
                        z3 = false;
                        z6 = true;
                        bundle.remove("dial_domain");
                        z = z3;
                        z2 = false;
                    } else {
                        loge("dial DOMAIN_NON_3GPP_PS should be used only for emergency calls");
                    }
                }
                z3 = false;
                z6 = false;
                bundle.remove("dial_domain");
                z = z3;
                z2 = false;
            }
        }
        StringBuilder sb = new StringBuilder("useImsForCall=");
        sb.append(z);
        sb.append(", useImsForPsAttachedCall=false, useOnlyDialedSimEccList=");
        sb.append(z5);
        sb.append(", isEmergency=");
        sb.append(zIsEmergencyNumber);
        sb.append(", useImsForEmergency=");
        sb.append(z6);
        sb.append(", useImsForUt=");
        sb.append(z2);
        sb.append(", isUt=");
        sb.append(z7);
        sb.append(", isSuppServiceCode=");
        sb.append(zIsSuppServiceCodes);
        sb.append(", isPotentialUssdCode=");
        sb.append(z8);
        sb.append(", isWpsCall=");
        sb.append(zIsWpsCallNumber);
        sb.append(", allowWpsOverIms=");
        sb.append(z4);
        sb.append(", imsPhone=");
        sb.append(phone);
        sb.append(", imsPhone.isVoiceOverCellularImsEnabled()=");
        Object objValueOf3 = "N/A";
        sb.append(phone != null ? Boolean.valueOf(phone.isVoiceOverCellularImsEnabled()) : "N/A");
        sb.append(", imsPhone.isVowifiEnabled()=");
        if (phone == null) {
            objValueOf = "N/A";
        } else {
            objValueOf = Boolean.valueOf(phone.isWifiCallingEnabled());
        }
        sb.append(objValueOf);
        sb.append(", imsPhone.isVideoEnabled()=");
        if (phone == null) {
            objValueOf2 = "N/A";
        } else {
            objValueOf2 = Boolean.valueOf(phone.isVideoEnabled());
        }
        sb.append(objValueOf2);
        sb.append(", imsPhone.getServiceState().getState()=");
        if (phone != null) {
            objValueOf3 = Integer.valueOf(phone.getServiceState().getState());
        }
        sb.append(objValueOf3);
        logi(sb.toString());
        if (!zIsEmergencyNumber && !onCheckIfIgnoreWifiOnlyModeCheck()) {
            Phone.checkWfcWifiOnlyModeBeforeDial(this.mImsPhone, this.mPhoneId, this.mContext);
        }
        if (phone != null && !z4 && !z && zIsWpsCallNumber && (phone.getCallTracker() instanceof ImsPhoneCallTracker)) {
            logi("WPS call placed over CS; disconnecting all IMS calls..");
            ((ImsPhoneCallTracker) phone.getCallTracker()).hangupAllConnections();
        }
        if (!MiuiPhoneStub.get(this.mPhoneId).forceUseCsForCall(this, strExtractNetworkPortionAlt) && (((z && (!z7 || z8)) || ((z7 && z2) || z6)) && !((IHyperCallManager) HyperTelephonyFactory.getInstance().initModule(IHyperCallManager.DEFAULT, new Object[0])).isInCsCall(zIsEmergencyNumber, this.mPhoneId))) {
            try {
                logd("Trying IMS PS call");
                consumer.accept(phone);
                str2 = strCheckForTestEmergencyNumber;
                dialArgs2 = imsDialArgsBuild;
                try {
                    return phone.dial(str2, dialArgs2);
                } catch (CallStateException e) {
                    e = e;
                    logd("IMS PS call exception " + e + "useImsForCall =" + z + ", imsPhone =" + phone);
                    if (Phone.CS_FALLBACK.equals(e.getMessage()) || zIsEmergencyNumber) {
                        logi("IMS call failed with Exception: " + e.getMessage() + ". Falling back to CS.");
                        serviceStateTracker = this.mSST;
                        if (serviceStateTracker == null) {
                        }
                        serviceStateTracker2 = this.mSST;
                        if (serviceStateTracker2 == null) {
                        }
                        serviceStateTracker3 = this.mSST;
                        if (serviceStateTracker3 == null) {
                        }
                        logd("Trying (non-IMS) CS call");
                        if (!zEquals) {
                            this.mIsTestingEmergencyCallbackMode = true;
                            this.mCi.testingEmergencyCall();
                        }
                        consumer.accept(this);
                        return dialInternal(str2, dialArgs2);
                    }
                    CallStateException callStateException = new CallStateException(e.getError(), e.getMessage());
                    callStateException.setStackTrace(e.getStackTrace());
                    throw callStateException;
                }
            } catch (CallStateException e2) {
                e = e2;
                str2 = strCheckForTestEmergencyNumber;
                dialArgs2 = imsDialArgsBuild;
            }
        } else {
            str2 = strCheckForTestEmergencyNumber;
            dialArgs2 = imsDialArgsBuild;
        }
        serviceStateTracker = this.mSST;
        if (serviceStateTracker == null && serviceStateTracker.mSS.getState() == 1 && this.mSST.mSS.getDataRegistrationState() != 0 && !zIsEmergencyNumber) {
            CallManager$$ExternalSyntheticBUOutline0.m("cannot dial in current state");
            return null;
        }
        serviceStateTracker2 = this.mSST;
        if (serviceStateTracker2 == null && serviceStateTracker2.mSS.getState() == 3 && !VideoProfile.isVideo(dialArgs2.videoState) && !zIsEmergencyNumber && ((!z7 || !z2) && !MiuiPhoneStub.get(this.mPhoneId).needSendUssdWhenAPM(this, strExtractNetworkPortionAlt) && !z8)) {
            Phone$$ExternalSyntheticBUOutline2.m(2, "cannot dial voice call in airplane mode");
            return null;
        }
        serviceStateTracker3 = this.mSST;
        if (serviceStateTracker3 == null && serviceStateTracker3.mSS.getState() == 1 && ((this.mSST.mSS.getDataRegistrationState() != 0 || !ServiceState.isPsOnlyTech(this.mSST.mSS.getRilDataRadioTechnology())) && !VideoProfile.isVideo(dialArgs2.videoState) && !zIsEmergencyNumber && !z8)) {
            Phone$$ExternalSyntheticBUOutline2.m(1, "cannot dial voice call in out of service");
            return null;
        }
        logd("Trying (non-IMS) CS call");
        if (!zEquals && zIsEmergencyNumber && !DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
            this.mIsTestingEmergencyCallbackMode = true;
            this.mCi.testingEmergencyCall();
        }
        consumer.accept(this);
        return dialInternal(str2, dialArgs2);
    }

    public boolean isNotificationOfWfcCallRequired(String str) {
        PersistableBundle configForSubId = ((CarrierConfigManager) this.mContext.getSystemService("carrier_config")).getConfigForSubId(getSubId());
        if (configForSubId != null && configForSubId.getBoolean("notify_international_call_on_wfc_bool")) {
            Phone phone = this.mImsPhone;
            boolean zIsEmergencyNumber = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).isEmergencyNumber(str);
            if (isImsUseEnabled() && phone != null && !phone.isVoiceOverCellularImsEnabled() && phone.isWifiCallingEnabled() && !zIsEmergencyNumber && PhoneNumberUtils.isInternationalNumber(str, getCountryIso())) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    protected Connection dialInternal(String str, PhoneInternalInterface.DialArgs dialArgs) throws CallStateException {
        return dialInternal(str, dialArgs, null);
    }

    protected Connection dialInternal(String str, PhoneInternalInterface.DialArgs dialArgs, ResultReceiver resultReceiver) throws CallStateException {
        String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        if (!dialArgs.isEmergency && resultReceiver == null && handleInCallMmiCommands(strStripSeparators)) {
            return null;
        }
        GsmMmiCode gsmMmiCodeNewFromDialString = GsmMmiCode.newFromDialString(PhoneNumberUtils.extractNetworkPortionAlt(strStripSeparators), this, this.mUiccApplication.get(), resultReceiver);
        logd("dialInternal: dialing w/ mmi '" + gsmMmiCodeNewFromDialString + "'...");
        if (gsmMmiCodeNewFromDialString == null) {
            return this.mCT.dialGsm(strStripSeparators, dialArgs);
        }
        if (gsmMmiCodeNewFromDialString.isTemporaryModeCLIR()) {
            return this.mCT.dialGsm(gsmMmiCodeNewFromDialString.mDialingNumber, gsmMmiCodeNewFromDialString.getCLIRMode(), dialArgs.uusInfo, dialArgs.intentExtras);
        }
        this.mPendingMMIs.add(gsmMmiCodeNewFromDialString);
        this.mMmiRegistrants.notifyRegistrants(new AsyncResult((Object) null, gsmMmiCodeNewFromDialString, (Throwable) null));
        gsmMmiCodeNewFromDialString.processCode();
        return null;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean handlePinMmi(String str) {
        GsmMmiCode gsmMmiCodeNewFromDialString = GsmMmiCode.newFromDialString(str, this, this.mUiccApplication.get());
        if (gsmMmiCodeNewFromDialString != null && gsmMmiCodeNewFromDialString.isPinPukCommand()) {
            this.mPendingMMIs.add(gsmMmiCodeNewFromDialString);
            this.mMmiRegistrants.notifyRegistrants(new AsyncResult((Object) null, gsmMmiCodeNewFromDialString, (Throwable) null));
            try {
                gsmMmiCodeNewFromDialString.processCode();
                return true;
            } catch (CallStateException unused) {
                return true;
            }
        }
        loge("Mmi is null or unrecognized!");
        return false;
    }

    protected void sendUssdResponse(String str, CharSequence charSequence, int i, ResultReceiver resultReceiver) {
        UssdResponse ussdResponse = new UssdResponse(str, charSequence);
        Bundle bundle = new Bundle();
        bundle.putParcelable("USSD_RESPONSE", ussdResponse);
        resultReceiver.send(i, bundle);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean handleUssdRequest(String str, ResultReceiver resultReceiver) {
        if (this.mPendingMMIs.size() > 0) {
            sendUssdResponse(str, null, -1, resultReceiver);
            return true;
        }
        if (FdnUtils.isNumberBlockedByFDN(this.mPhoneId, str, getCountryIso())) {
            sendUssdResponse(str, null, -1, resultReceiver);
            return true;
        }
        Phone phone = this.mImsPhone;
        if (phone != null && (phone.getServiceState().getState() == 0 || phone.isUtEnabled())) {
            try {
                logd("handleUssdRequest: attempting over IMS");
                return phone.handleUssdRequest(str, resultReceiver);
            } catch (CallStateException e) {
                if (!Phone.CS_FALLBACK.equals(e.getMessage())) {
                    return false;
                }
                logd("handleUssdRequest: fallback to CS required");
            }
        }
        try {
            dialInternal(str, new PhoneInternalInterface.DialArgs.Builder().build(), resultReceiver);
            return true;
        } catch (Exception e2) {
            logd("handleUssdRequest: exception" + e2);
            return false;
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void sendUssdResponse(String str) {
        GsmMmiCode gsmMmiCodeNewFromUssdUserInput = GsmMmiCode.newFromUssdUserInput(str, this, this.mUiccApplication.get());
        this.mPendingMMIs.add(gsmMmiCodeNewFromUssdUserInput);
        this.mMmiRegistrants.notifyRegistrants(new AsyncResult((Object) null, gsmMmiCodeNewFromUssdUserInput, (Throwable) null));
        gsmMmiCodeNewFromUssdUserInput.sendUssd(str);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void sendDtmf(char c) {
        if (!PhoneNumberUtils.is12Key(c)) {
            loge("sendDtmf called with invalid character '" + c + "'");
            return;
        }
        if (this.mCT.mState == PhoneConstants.State.OFFHOOK) {
            this.mCi.sendDtmf(c, null);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void startDtmf(char c) {
        if (!PhoneNumberUtils.is12Key(c)) {
            loge("startDtmf called with invalid character '" + c + "'");
            return;
        }
        this.mCi.startDtmf(c, null);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void stopDtmf() {
        this.mCi.stopDtmf(null);
    }

    @Override // com.android.internal.telephony.Phone
    public void sendBurstDtmf(String str, int i, int i2, Message message) {
        loge("[GsmCdmaPhone] sendBurstDtmf() is a CDMA method");
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setRadioPowerOnForTestEmergencyCall(boolean z) {
        this.mSST.clearAllRadioOffReasons();
        setRadioPower(true, false, z, false);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setRadioPower(boolean z, boolean z2, boolean z3, boolean z4) {
        setRadioPowerForReason(z, z2, z3, z4, 0);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setRadioPowerForReason(boolean z, boolean z2, boolean z3, boolean z4, int i) {
        this.mSST.setRadioPowerForReason(z, z2, z3, z4, i);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public Set<Integer> getRadioPowerOffReasons() {
        return this.mSST.getRadioPowerOffReasons();
    }

    private void storeVoiceMailNumber(String str) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        setVmSimImsi(getSubscriberId());
        logd("storeVoiceMailNumber: mPrecisePhoneType=" + this.mPrecisePhoneType + " vmNumber=" + Rlog.pii(LOG_TAG, str));
        StringBuilder sb = new StringBuilder("vm_number_key");
        sb.append(getPhoneId());
        editorEdit.putString(sb.toString(), str);
        editorEdit.apply();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getVoiceMailNumber() {
        PersistableBundle configForSubId;
        PersistableBundle configForSubId2;
        IccRecords iccRecords = this.mIccRecords.get();
        String voiceMailNumber = iccRecords != null ? iccRecords.getVoiceMailNumber() : "";
        if (MiuiPhoneStub.get(this.mPhoneId).getVMNumberIgnoreSimRecords(this, voiceMailNumber)) {
            voiceMailNumber = null;
        }
        if (TextUtils.isEmpty(voiceMailNumber)) {
            voiceMailNumber = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("vm_number_key" + getPhoneId(), null);
            logd("getVoiceMailNumber: from vm_number_key number=" + Rlog.piiHandle(voiceMailNumber));
        } else {
            logd("getVoiceMailNumber: from IccRecords number=" + Rlog.piiHandle(voiceMailNumber));
        }
        if (TextUtils.isEmpty(voiceMailNumber) && (configForSubId2 = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId())) != null) {
            String string = configForSubId2.getString("default_vm_number_string");
            String string2 = configForSubId2.getString("default_vm_number_roaming_string");
            String string3 = configForSubId2.getString("default_vm_number_roaming_and_ims_unregistered_string");
            if (!TextUtils.isEmpty(string)) {
                voiceMailNumber = string;
            }
            if (this.mSST.mSS.getRoaming()) {
                if (!TextUtils.isEmpty(string3) && !this.mSST.isImsRegistered()) {
                    logd("getVoiceMailNumber: from defaultVmNumberRoamingAndImsUnregistered number=" + Rlog.piiHandle(string3));
                    voiceMailNumber = string3;
                } else if (!TextUtils.isEmpty(string2)) {
                    logd("getVoiceMailNumber: from defaultVmNumberRoaming number=" + Rlog.piiHandle(string2));
                    voiceMailNumber = string2;
                }
            }
        }
        if (!TextUtils.isEmpty(voiceMailNumber) || (configForSubId = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId())) == null || !configForSubId.getBoolean("config_telephony_use_own_number_for_voicemail_bool")) {
            return voiceMailNumber;
        }
        String line1Number = getLine1Number();
        logd("getVoiceMailNumber: from MSISDN number=" + Rlog.piiHandle(line1Number));
        return line1Number;
    }

    private String getVmSimImsi() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString("vm_sim_imsi_key" + getPhoneId(), null);
    }

    private void setVmSimImsi(String str) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editorEdit.putString("vm_sim_imsi_key" + getPhoneId(), str);
        editorEdit.apply();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getVoiceMailAlphaTag() {
        IccRecords iccRecords = this.mIccRecords.get();
        String voiceMailAlphaTag = iccRecords != null ? iccRecords.getVoiceMailAlphaTag() : "";
        return (voiceMailAlphaTag == null || voiceMailAlphaTag.length() == 0) ? this.mContext.getText(R.string.defaultVoiceMailAlphaTag).toString() : voiceMailAlphaTag;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getDeviceId() {
        return this.mImei;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getDeviceSvn() {
        return this.mImeiSv;
    }

    @Override // com.android.internal.telephony.Phone
    public IsimRecords getIsimRecords() {
        return this.mIsimUiccRecords;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getImei() {
        return this.mImei;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public int getImeiType() {
        return this.mImeiType;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getSubscriberId() {
        IccRecords iccRecords = this.mUiccController.getIccRecords(this.mPhoneId, 1);
        if (iccRecords != null) {
            return iccRecords.getIMSI();
        }
        return null;
    }

    @Override // com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int i, boolean z) {
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(getSubId());
        return CarrierInfoManager.getCarrierInfoForImsiEncryption(i, this.mContext, telephonyManagerCreateForSubscriptionId.getSimOperator(), telephonyManagerCreateForSubscriptionId.getSimCarrierId(), z, getSubId());
    }

    @Override // com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo imsiEncryptionInfo, boolean z) {
        if (z) {
            CarrierInfoManager.setCarrierInfoForImsiEncryption(imsiEncryptionInfo, this.mContext, this.mPhoneId);
        }
        this.mCi.setCarrierInfoForImsiEncryption(imsiEncryptionInfo, null);
    }

    @Override // com.android.internal.telephony.Phone
    public void deleteCarrierInfoForImsiEncryption(int i) {
        CarrierInfoManager.deleteCarrierInfoForImsiEncryption(this.mContext, getSubId(), i);
    }

    @Override // com.android.internal.telephony.Phone
    public void deleteCarrierInfoForImsiEncryption(int i, String str) {
        CarrierInfoManager.deleteCarrierInfoForImsiEncryption(this.mContext, getSubId(), i, str);
    }

    @Override // com.android.internal.telephony.Phone
    public int getCarrierId() {
        CarrierResolver carrierResolver = this.mCarrierResolver;
        return carrierResolver != null ? carrierResolver.getCarrierId() : super.getCarrierId();
    }

    @Override // com.android.internal.telephony.Phone
    public String getCarrierName() {
        CarrierResolver carrierResolver = this.mCarrierResolver;
        return carrierResolver != null ? carrierResolver.getCarrierName() : super.getCarrierName();
    }

    @Override // com.android.internal.telephony.Phone
    public int getMNOCarrierId() {
        CarrierResolver carrierResolver = this.mCarrierResolver;
        return carrierResolver != null ? carrierResolver.getMnoCarrierId() : super.getMNOCarrierId();
    }

    @Override // com.android.internal.telephony.Phone
    public int getSpecificCarrierId() {
        CarrierResolver carrierResolver = this.mCarrierResolver;
        return carrierResolver != null ? carrierResolver.getSpecificCarrierId() : super.getSpecificCarrierId();
    }

    @Override // com.android.internal.telephony.Phone
    public String getSpecificCarrierName() {
        CarrierResolver carrierResolver = this.mCarrierResolver;
        return carrierResolver != null ? carrierResolver.getSpecificCarrierName() : super.getSpecificCarrierName();
    }

    @Override // com.android.internal.telephony.Phone
    public void resolveSubscriptionCarrierId(String str) {
        CarrierResolver carrierResolver = this.mCarrierResolver;
        if (carrierResolver != null) {
            carrierResolver.resolveSubscriptionCarrierId(str);
        }
    }

    @Override // com.android.internal.telephony.Phone
    public int getCarrierIdListVersion() {
        CarrierResolver carrierResolver = this.mCarrierResolver;
        return carrierResolver != null ? carrierResolver.getCarrierListVersion() : super.getCarrierIdListVersion();
    }

    @Override // com.android.internal.telephony.Phone
    public int getEmergencyNumberDbVersion() {
        EmergencyNumberTracker emergencyNumberTracker = getEmergencyNumberTracker();
        if (emergencyNumberTracker == null) {
            return -1;
        }
        return emergencyNumberTracker.getEmergencyNumberDbVersion();
    }

    @Override // com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public void resetCarrierKeysForImsiEncryption() {
        this.mCIM.resetCarrierKeysForImsiEncryption(this.mContext, this.mPhoneId, false);
    }

    @Override // com.android.internal.telephony.Phone
    public void resetCarrierKeysForImsiEncryption(boolean z) {
        this.mCIM.resetCarrierKeysForImsiEncryption(this.mContext, this.mPhoneId, z);
    }

    @Override // com.android.internal.telephony.Phone
    public void setCarrierTestOverride(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        this.mCarrierResolver.setTestOverrideApn(str9);
        UiccProfile uiccProfileForPhone = this.mUiccController.getUiccProfileForPhone(getPhoneId());
        if (uiccProfileForPhone != null) {
            List<UiccAccessRule> listSingletonList = null;
            if (str8 != null) {
                if (str8.isEmpty()) {
                    listSingletonList = Collections.EMPTY_LIST;
                } else {
                    listSingletonList = Collections.singletonList(new UiccAccessRule(IccUtils.hexStringToBytes(str8), (String) null, 0L));
                }
            }
            uiccProfileForPhone.setTestOverrideCarrierPrivilegeRules(listSingletonList);
        } else {
            this.mCarrierResolver.setTestOverrideCarrierPriviledgeRule(str8);
        }
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            iccRecords.setCarrierTestOverride(str, str2, str3, str4, str5, str6, str7);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getGroupIdLevel1() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getGid1();
        }
        return null;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getGroupIdLevel2() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getGid2();
        }
        return null;
    }

    private String getMsisdnNumber() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getMsisdnNumber();
        }
        return null;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getLine1Number() {
        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
            return MiuiPhoneStub.get(this.mPhoneId).getLine1Number(getMsisdnNumber(), this);
        }
        if (TelephonyBaseUtilsStub.isAutoDevice()) {
            return TelephonyMultiInternetStub.get().getPhoneNumber(getMsisdnNumber(), getPhoneId());
        }
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getMsisdnNumber();
        }
        return null;
    }

    @Override // com.android.internal.telephony.Phone
    public String getPnnHomeNetworkName() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getPnnHomeName();
        }
        return null;
    }

    @Override // com.android.internal.telephony.Phone
    protected void updateManualNetworkSelection(Phone.NetworkSelectMessage networkSelectMessage) {
        int subId = getSubId();
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            this.mManualNetworkSelectionPlmn = networkSelectMessage.operatorNumeric;
            return;
        }
        this.mManualNetworkSelectionPlmn = null;
        Rlog.e(LOG_TAG, "Cannot update network selection due to invalid subId " + subId);
    }

    @Override // com.android.internal.telephony.Phone
    public String getManualNetworkSelectionPlmn() {
        String str = this.mManualNetworkSelectionPlmn;
        return str == null ? "" : str;
    }

    @Override // com.android.internal.telephony.Phone
    protected void onSetNetworkSelectionModeCompleted() {
        this.mSST.pollState();
    }

    @Override // com.android.internal.telephony.Phone
    public String getMsisdn() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getMsisdnNumber();
        }
        return null;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getLine1AlphaTag() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.getMsisdnAlphaTag();
        }
        return null;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean setLine1Number(String str, String str2, Message message) {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords == null) {
            return false;
        }
        iccRecords.setMsisdnNumber(str, str2, message);
        return true;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setVoiceMailNumber(String str, String str2, Message message) {
        this.mVmNumber = str2;
        Message messageObtainMessage = obtainMessage(20, 0, 0, message);
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            iccRecords.setVoiceMailNumber(str, this.mVmNumber, messageObtainMessage);
        }
    }

    @Override // com.android.internal.telephony.Phone
    public String getSystemProperty(String str, String str2) {
        if (getUnitTestMode()) {
            return null;
        }
        return TelephonyManager.getTelephonyProperty(this.mPhoneId, str, str2);
    }

    protected boolean isCsRetry(Message message) {
        if (message != null) {
            return message.getData().getBoolean(Phone.CS_FALLBACK_SS, false);
        }
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public void setN1ModeEnabled(final boolean z, final Message message) {
        post(new Runnable() { // from class: com.android.internal.telephony.GsmCdmaPhone$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setN1ModeEnabled$2(z, message);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setN1ModeEnabled$2(boolean z, Message message) {
        Set set = this.mN1ModeDisallowedReasons;
        if (z) {
            set.remove(N1_MODE_DISALLOWED_REASON_IMS);
        } else {
            set.add(N1_MODE_DISALLOWED_REASON_IMS);
        }
        if (this.mModemN1Mode == null) {
            this.mCi.isN1ModeEnabled(obtainMessage(69, message));
        } else {
            maybeUpdateModemN1Mode(message);
        }
    }

    private void maybeUpdateModemN1Mode(Message message) {
        boolean zIsEmpty = this.mN1ModeDisallowedReasons.isEmpty();
        logd("N1 Mode: isModemN1Enabled=" + this.mModemN1Mode + ", wantN1Enabled=" + zIsEmpty);
        if (this.mModemN1Mode.booleanValue() != zIsEmpty) {
            this.mModemN1Mode = Boolean.valueOf(zIsEmpty);
            super.setN1ModeEnabled(zIsEmpty, obtainMessage(70, message));
        } else if (message != null) {
            AsyncResult.forMessage(message);
            message.sendToTarget();
        }
    }

    protected void updateCarrierN1ModeSupported(PersistableBundle persistableBundle) {
        if (MiuiTelephonyManagerStub.get().isUserFiveGSaEnabled(this.mPhoneId) && CarrierConfigManager.isConfigForIdentifiedCarrier(persistableBundle)) {
            boolean zContains = ArrayUtils.contains(persistableBundle.getIntArray("carrier_nr_availabilities_int_array"), 2);
            Set set = this.mN1ModeDisallowedReasons;
            if (zContains) {
                set.remove(N1_MODE_DISALLOWED_REASON_CARRIER);
            } else {
                set.add(N1_MODE_DISALLOWED_REASON_CARRIER);
            }
            if (this.mModemN1Mode == null) {
                this.mCi.isN1ModeEnabled(obtainMessage(69));
            } else {
                maybeUpdateModemN1Mode(null);
            }
        }
    }

    @Override // com.android.internal.telephony.Phone
    public boolean useSsOverIms(Message message) {
        boolean zIsUtEnabled = isUtEnabled();
        Rlog.d(LOG_TAG, "useSsOverIms: isUtEnabled()= " + zIsUtEnabled + " isCsRetry(onComplete))= " + isCsRetry(message));
        return zIsUtEnabled && !isCsRetry(message);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void getCallForwardingOption(int i, Message message) {
        getCallForwardingOption(i, 1, message);
    }

    @Override // com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public void getCallForwardingOption(int i, int i2, Message message) {
        if (isRequestBlockedByFDN(SsData.RequestType.SS_INTERROGATION, GsmMmiCode.cfReasonToServiceType(i))) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.FDN_CHECK_FAILURE));
            message.sendToTarget();
            return;
        }
        Phone phone = this.mImsPhone;
        if (useSsOverIms(message)) {
            phone.getCallForwardingOption(i, i2, message);
        } else if (isValidCommandInterfaceCFReason(i)) {
            logd("requesting call forwarding query.");
            if (i == 0) {
                message = obtainMessage(13, message);
            }
            this.mCi.queryCallForwardStatus(i, i2, null, message);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setCallForwardingOption(int i, int i2, String str, int i3, Message message) {
        setCallForwardingOption(i, i2, str, 1, i3, message);
    }

    @Override // com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public void setCallForwardingOption(int i, int i2, String str, int i3, int i4, Message message) {
        Message messageObtainMessage;
        if (isRequestBlockedByFDN(GsmMmiCode.cfActionToRequestType(i), GsmMmiCode.cfReasonToServiceType(i2))) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.FDN_CHECK_FAILURE));
            message.sendToTarget();
            return;
        }
        Phone phone = this.mImsPhone;
        if (useSsOverIms(message)) {
            phone.setCallForwardingOption(i, i2, str, i3, i4, message);
            return;
        }
        String str2 = str;
        int i5 = i3;
        if (isValidCommandInterfaceCFAction(i) && isValidCommandInterfaceCFReason(i2)) {
            if (i2 == 0) {
                Cfu cfu = new Cfu(str2, message, i5, i, i2);
                i = i;
                i5 = i5;
                str2 = str2;
                i2 = i2;
                messageObtainMessage = obtainMessage(12, isCfEnable(i) ? 1 : 0, 0, cfu);
            } else {
                messageObtainMessage = message;
            }
            this.mCi.setCallForward(i, i2, i5, str2, i4, messageObtainMessage);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void getCallBarring(String str, String str2, Message message, int i) {
        if (isRequestBlockedByFDN(SsData.RequestType.SS_INTERROGATION, GsmMmiCode.cbFacilityToServiceType(str))) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.FDN_CHECK_FAILURE));
            message.sendToTarget();
            return;
        }
        Phone phone = this.mImsPhone;
        if (useSsOverIms(message)) {
            phone.getCallBarring(str, str2, message, i);
        } else {
            this.mCi.queryFacilityLock(str, str2, i, message);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setCallBarring(String str, boolean z, String str2, Message message, int i) {
        SsData.RequestType requestType;
        if (z) {
            requestType = SsData.RequestType.SS_ACTIVATION;
        } else {
            requestType = SsData.RequestType.SS_DEACTIVATION;
        }
        if (isRequestBlockedByFDN(requestType, GsmMmiCode.cbFacilityToServiceType(str))) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.FDN_CHECK_FAILURE));
            message.sendToTarget();
            return;
        }
        Phone phone = this.mImsPhone;
        if (useSsOverIms(message)) {
            phone.setCallBarring(str, z, str2, message, i);
        } else {
            this.mCi.setFacilityLock(str, z, str2, i, message);
        }
    }

    public void changeCallBarringPassword(String str, String str2, String str3, Message message) {
        if (FdnUtils.isSuppServiceRequestBlockedByFdn(this.mPhoneId, GsmMmiCode.getControlStringsForPwd(SsData.RequestType.SS_REGISTRATION, GsmMmiCode.cbFacilityToServiceType(str)), getCountryIso())) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.FDN_CHECK_FAILURE));
            message.sendToTarget();
        } else {
            this.mCi.changeBarringPassword(str, str2, str3, message);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void getOutgoingCallerIdDisplay(Message message) {
        if (isRequestBlockedByFDN(SsData.RequestType.SS_INTERROGATION, SsData.ServiceType.SS_CLIR)) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.FDN_CHECK_FAILURE));
            message.sendToTarget();
            return;
        }
        Phone phone = this.mImsPhone;
        if (useSsOverIms(message)) {
            phone.getOutgoingCallerIdDisplay(message);
        } else {
            this.mCi.getCLIR(message);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setOutgoingCallerIdDisplay(int i, Message message) {
        if (isRequestBlockedByFDN(GsmMmiCode.clirModeToRequestType(i), SsData.ServiceType.SS_CLIR)) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.FDN_CHECK_FAILURE));
            message.sendToTarget();
            return;
        }
        Phone phone = this.mImsPhone;
        if (useSsOverIms(message)) {
            phone.setOutgoingCallerIdDisplay(i, message);
        } else {
            this.mCi.setCLIR(i, obtainMessage(18, i, 0, message));
        }
    }

    @Override // com.android.internal.telephony.Phone
    public void queryCLIP(Message message) {
        if (isRequestBlockedByFDN(SsData.RequestType.SS_INTERROGATION, SsData.ServiceType.SS_CLIP)) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.FDN_CHECK_FAILURE));
            message.sendToTarget();
            return;
        }
        Phone phone = this.mImsPhone;
        if (useSsOverIms(message)) {
            phone.queryCLIP(message);
        } else {
            this.mCi.queryCLIP(message);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void getCallWaiting(Message message) {
        if (isRequestBlockedByFDN(SsData.RequestType.SS_INTERROGATION, SsData.ServiceType.SS_WAIT)) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.FDN_CHECK_FAILURE));
            message.sendToTarget();
        } else {
            if (this.mCallWaitingController.getCallWaiting(message)) {
                return;
            }
            Phone phone = this.mImsPhone;
            if (useSsOverIms(message)) {
                phone.getCallWaiting(message);
            } else {
                this.mCi.queryCallWaiting(0, message);
            }
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setCallWaiting(boolean z, Message message) {
        PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId());
        setCallWaiting(z, configForSubId != null ? configForSubId.getInt("call_waiting_service_class_int", 1) : 1, message);
    }

    @Override // com.android.internal.telephony.Phone
    public void setCallWaiting(boolean z, int i, Message message) {
        SsData.RequestType requestType;
        if (z) {
            requestType = SsData.RequestType.SS_ACTIVATION;
        } else {
            requestType = SsData.RequestType.SS_DEACTIVATION;
        }
        if (isRequestBlockedByFDN(requestType, SsData.ServiceType.SS_WAIT)) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.FDN_CHECK_FAILURE));
            message.sendToTarget();
        } else {
            if (this.mCallWaitingController.setCallWaiting(z, i, message)) {
                return;
            }
            Phone phone = this.mImsPhone;
            if (useSsOverIms(message)) {
                phone.setCallWaiting(z, message);
            } else {
                this.mCi.setCallWaiting(z, i, message);
            }
        }
    }

    @Override // com.android.internal.telephony.Phone
    public int getTerminalBasedCallWaitingState(boolean z) {
        return this.mCallWaitingController.getTerminalBasedCallWaitingState(z);
    }

    @Override // com.android.internal.telephony.Phone
    public void setTerminalBasedCallWaitingStatus(int i) {
        Phone phone = this.mImsPhone;
        if (phone != null) {
            phone.setTerminalBasedCallWaitingStatus(i);
        }
    }

    @Override // com.android.internal.telephony.Phone
    public void setTerminalBasedCallWaitingSupported(boolean z) {
        this.mCallWaitingController.setTerminalBasedCallWaitingSupported(z);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void getAvailableNetworks(Message message) {
        this.mCi.getAvailableNetworks(obtainMessage(51, message));
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void startNetworkScan(NetworkScanRequest networkScanRequest, Message message) {
        this.mCi.startNetworkScan(networkScanRequest, message);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void stopNetworkScan(Message message) {
        this.mCi.stopNetworkScan(message);
    }

    @Override // com.android.internal.telephony.Phone
    public void setTTYMode(int i, Message message) {
        super.setTTYMode(i, message);
        Phone phone = this.mImsPhone;
        if (phone != null) {
            phone.setTTYMode(i, message);
        }
    }

    @Override // com.android.internal.telephony.Phone
    public void setUiTTYMode(int i, Message message) {
        Phone phone = this.mImsPhone;
        if (phone != null) {
            phone.setUiTTYMode(i, message);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setMute(boolean z) {
        this.mCT.setMute(z);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean getMute() {
        return this.mCT.getMute();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void updateServiceLocation(WorkSource workSource) {
        this.mSST.enableSingleLocationUpdate(workSource);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void enableLocationUpdates() {
        this.mSST.enableLocationUpdates();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void disableLocationUpdates() {
        this.mSST.disableLocationUpdates();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean getDataRoamingEnabled() {
        return getDataSettingsManager().isDataRoamingEnabled();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setDataRoamingEnabled(boolean z) {
        getDataSettingsManager().setDataRoamingEnabled(z);
    }

    @Override // com.android.internal.telephony.Phone
    public void setOnEcbModeExitResponse(Handler handler, int i, Object obj) {
        this.mEcmExitRespRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unsetOnEcbModeExitResponse(Handler handler) {
        this.mEcmExitRespRegistrant.clear();
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForCallWaiting(Handler handler, int i, Object obj) {
        GsmCdmaCallTracker gsmCdmaCallTracker = this.mCT;
        if (gsmCdmaCallTracker != null) {
            gsmCdmaCallTracker.registerForCallWaiting(handler, i, obj);
        }
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForCallWaiting(Handler handler) {
        GsmCdmaCallTracker gsmCdmaCallTracker = this.mCT;
        if (gsmCdmaCallTracker != null) {
            gsmCdmaCallTracker.unregisterForCallWaiting(handler);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean isUserDataEnabled() {
        return getDataSettingsManager().isDataEnabledForReason(0);
    }

    public void onMMIDone(MmiCode mmiCode) {
        if (this.mPendingMMIs.remove(mmiCode) || mmiCode.isUssdRequest() || ((GsmMmiCode) mmiCode).isSsInfo()) {
            ResultReceiver ussdCallbackReceiver = mmiCode.getUssdCallbackReceiver();
            if (ussdCallbackReceiver != null) {
                Rlog.i(LOG_TAG, "onMMIDone: invoking callback: " + mmiCode);
                sendUssdResponse(mmiCode.getDialString(), mmiCode.getMessage(), mmiCode.getState() == MmiCode.State.COMPLETE ? 100 : -1, ussdCallbackReceiver);
                return;
            }
            Rlog.i(LOG_TAG, "onMMIDone: notifying registrants: " + mmiCode);
            this.mMmiCompleteRegistrants.notifyRegistrants(new AsyncResult((Object) null, mmiCode, (Throwable) null));
            return;
        }
        Rlog.i(LOG_TAG, "onMMIDone: invalid response or already handled; ignoring: " + mmiCode);
    }

    public boolean supports3gppCallForwardingWhileRoaming() {
        PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId());
        if (configForSubId != null) {
            return configForSubId.getBoolean("support_3gpp_call_forwarding_while_roaming_bool", true);
        }
        return true;
    }

    protected void onNetworkInitiatedUssd(MmiCode mmiCode) {
        Rlog.v(LOG_TAG, "onNetworkInitiatedUssd: mmi=" + mmiCode);
        this.mMmiCompleteRegistrants.notifyRegistrants(new AsyncResult((Object) null, mmiCode, (Throwable) null));
    }

    protected void onIncomingUSSD(int i, String str) {
        GsmMmiCode gsmMmiCode;
        int i2 = 0;
        boolean z = i == 1;
        boolean z2 = (i == 0 || i == 1) ? false : true;
        boolean zIsUssdRelease = MiuiPhoneStub.get(this.mPhoneId).isUssdRelease(this, i) | (i == 2);
        int size = this.mPendingMMIs.size();
        while (true) {
            if (i2 >= size) {
                gsmMmiCode = null;
                break;
            } else {
                if (((GsmMmiCode) this.mPendingMMIs.get(i2)).isPendingUSSD()) {
                    gsmMmiCode = (GsmMmiCode) this.mPendingMMIs.get(i2);
                    break;
                }
                i2++;
            }
        }
        if (gsmMmiCode != null) {
            if (zIsUssdRelease) {
                gsmMmiCode.onUssdRelease();
                return;
            } else if (z2) {
                gsmMmiCode.onUssdFinishedError();
                return;
            } else {
                gsmMmiCode.onUssdFinished(str, z);
                return;
            }
        }
        if (!z2 && !TextUtils.isEmpty(str)) {
            onNetworkInitiatedUssd(GsmMmiCode.newNetworkInitiatedUssd(str, z, this, this.mUiccApplication.get()));
        } else {
            if (!z2 || zIsUssdRelease) {
                return;
            }
            GsmMmiCode.newNetworkInitiatedUssd(str, true, this, this.mUiccApplication.get()).onUssdFinishedError();
        }
    }

    private void syncClirSetting() {
        if (hasCalling()) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            migrateClirSettingIfNeeded(defaultSharedPreferences);
            int i = defaultSharedPreferences.getInt(Phone.CLIR_KEY + getSubId(), -1);
            Rlog.i(LOG_TAG, "syncClirSetting: clir_sub_key" + getSubId() + "=" + i);
            if (i >= 0) {
                this.mCi.setCLIR(i, null);
            } else {
                if (TelephonyCommonUtilsStub.get().isCustForJpKd()) {
                    return;
                }
                this.mCi.setCLIR(0, null);
            }
        }
    }

    private void migrateClirSettingIfNeeded(SharedPreferences sharedPreferences) {
        int i = sharedPreferences.getInt("clir_key" + getPhoneId(), -1);
        if (i >= 0) {
            Rlog.i(LOG_TAG, "Migrate CLIR setting: value=" + i + ", clir_key" + getPhoneId() + " -> clir_sub_key" + getSubId());
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            StringBuilder sb = new StringBuilder(Phone.CLIR_KEY);
            sb.append(getSubId());
            editorEdit.putInt(sb.toString(), i);
            editorEdit.remove("clir_key" + getPhoneId()).commit();
        }
    }

    private void handleRadioAvailable() {
        this.mCi.getBasebandVersion(obtainMessage(6));
        this.mCi.getImei(obtainMessage(67));
        this.mCi.getDeviceIdentity(obtainMessage(21));
        this.mCi.getRadioCapability(obtainMessage(35));
        this.mCi.areUiccApplicationsEnabled(obtainMessage(55));
        handleNullCipherEnabledChange();
        handleIdentifierDisclosureNotificationPreferenceChange();
        handleNullCipherNotificationPreferenceChanged();
    }

    protected void handleRadioOn() {
        this.mCi.getVoiceRadioTechnology(obtainMessage(40));
    }

    protected void handleRadioOffOrNotAvailable() {
        for (int size = this.mPendingMMIs.size() - 1; size >= 0; size--) {
            if (((GsmMmiCode) this.mPendingMMIs.get(size)).isPendingUSSD()) {
                ((GsmMmiCode) this.mPendingMMIs.get(size)).onUssdFinishedError();
            }
        }
        this.mRadioOffOrNotAvailableRegistrants.notifyRegistrants();
    }

    private void handleRadioPowerStateChange() {
        int radioState = this.mCi.getRadioState();
        Rlog.d(LOG_TAG, "handleRadioPowerStateChange, state= " + radioState);
        this.mNotifier.notifyRadioPowerStateChanged(this, radioState);
    }

    @Override // com.android.internal.telephony.Phone, android.os.Handler
    public void handleMessage(Message message) {
        Object obj;
        Pair pair;
        Object obj2;
        Phone phone;
        Object obj3;
        Object obj4;
        int i = message.what;
        switch (i) {
            case 1:
                handleRadioAvailable();
                return;
            case 2:
                logd("Event EVENT_SSN Received");
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (onPreHandleSsnEvent(asyncResult.result)) {
                    return;
                }
                this.mSsnRegistrants.notifyRegistrants(asyncResult);
                return;
            case 3:
                updateCurrentCarrierInProvider();
                String vmSimImsi = getVmSimImsi();
                String subscriberId = getSubscriberId();
                if (vmSimImsi != null && subscriberId != null && !subscriberId.equals(vmSimImsi)) {
                    storeVoiceMailNumber(null);
                    setVmSimImsi(null);
                }
                updateVoiceMail();
                this.mSimRecordsLoadedRegistrants.notifyRegistrants();
                return;
            case 4:
            case 9:
            case 10:
            case 11:
            case 14:
            case 15:
            case 16:
            case 17:
            case 23:
            case 24:
            case 27:
            case 31:
            case 32:
            case 33:
            case 34:
            case 37:
            case 38:
            case 41:
            case 42:
            case 49:
            case 50:
            case 52:
            case 63:
            case 64:
            default:
                super.handleMessage(message);
                return;
            case 5:
                logd("Event EVENT_RADIO_ON Received");
                handleRadioOn();
                return;
            case 6:
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                if (asyncResult2.exception != null) {
                    return;
                }
                logd("Baseband version: " + asyncResult2.result);
                String strSubstring = (String) asyncResult2.result;
                if (strSubstring != null) {
                    int length = strSubstring.length();
                    TelephonyManager telephonyManagerFrom = TelephonyManager.from(this.mContext);
                    int phoneId = getPhoneId();
                    if (length > 45) {
                        strSubstring = strSubstring.substring(length - 45, length);
                    }
                    telephonyManagerFrom.setBasebandVersionForPhone(phoneId, strSubstring);
                    return;
                }
                return;
            case 7:
                String[] strArr = (String[]) ((AsyncResult) message.obj).result;
                if (strArr.length > 1) {
                    try {
                        onIncomingUSSD(Integer.parseInt(strArr[0]), strArr[1]);
                        return;
                    } catch (NumberFormatException unused) {
                        Rlog.w(LOG_TAG, "error parsing USSD");
                        return;
                    }
                }
                return;
            case 8:
                logd("Event EVENT_RADIO_OFF_OR_NOT_AVAILABLE Received");
                handleRadioOffOrNotAvailable();
                return;
            case 12:
                AsyncResult asyncResult3 = (AsyncResult) message.obj;
                Cfu cfu = (Cfu) asyncResult3.userObj;
                if (asyncResult3.exception == null) {
                    setVoiceCallForwardingFlag(1, message.arg1 == 1, cfu.mSetCfNumber);
                    MiuiPhoneStub.get(this.mPhoneId).notifyHandoverProvider(getContext(), cfu.mAction, cfu.mReason);
                }
                Message message2 = cfu.mOnComplete;
                if (message2 != null) {
                    AsyncResult.forMessage(message2, asyncResult3.result, asyncResult3.exception);
                    cfu.mOnComplete.sendToTarget();
                    return;
                }
                return;
            case 13:
                AsyncResult asyncResult4 = (AsyncResult) message.obj;
                if (asyncResult4.exception == null) {
                    handleCfuQueryResult((CallForwardInfo[]) asyncResult4.result);
                }
                Message message3 = (Message) asyncResult4.userObj;
                if (message3 != null) {
                    AsyncResult.forMessage(message3, asyncResult4.result, asyncResult4.exception);
                    message3.sendToTarget();
                    return;
                }
                return;
            case 18:
                AsyncResult asyncResult5 = (AsyncResult) message.obj;
                if (asyncResult5.exception == null) {
                    saveClirSetting(message.arg1);
                }
                Message message4 = (Message) asyncResult5.userObj;
                if (message4 != null) {
                    AsyncResult.forMessage(message4, asyncResult5.result, asyncResult5.exception);
                    message4.sendToTarget();
                    return;
                }
                return;
            case 19:
                logd("Event EVENT_REGISTERED_TO_NETWORK Received");
                syncClirSetting();
                return;
            case 20:
                AsyncResult asyncResult6 = (AsyncResult) message.obj;
                if (asyncResult6.exception instanceof IccVmNotSupportedException) {
                    storeVoiceMailNumber(this.mVmNumber);
                    asyncResult6.exception = null;
                }
                Message message5 = (Message) asyncResult6.userObj;
                if (message5 != null) {
                    AsyncResult.forMessage(message5, asyncResult6.result, asyncResult6.exception);
                    message5.sendToTarget();
                    return;
                }
                return;
            case 21:
                AsyncResult asyncResult7 = (AsyncResult) message.obj;
                if (asyncResult7.exception != null) {
                    return;
                }
                String[] strArr2 = (String[]) asyncResult7.result;
                if (!TextUtils.isEmpty(strArr2[0])) {
                    this.mImei = strArr2[0];
                    if (!TextUtils.isEmpty(strArr2[1])) {
                        this.mImeiSv = strArr2[1];
                    }
                }
                this.mEsn = strArr2[2];
                String str = strArr2[3];
                this.mMeid = str;
                if (TextUtils.isEmpty(str) || !this.mMeid.matches("^0*$")) {
                    return;
                }
                logd("EVENT_GET_DEVICE_IDENTITY_DONE: set mMeid to null");
                this.mMeid = null;
                return;
            case 22:
                logd("Event EVENT_RUIM_RECORDS_LOADED Received");
                updateCurrentCarrierInProvider();
                return;
            case 25:
                handleEnterEmergencyCallbackMode(message);
                return;
            case 26:
                handleExitEmergencyCallbackMode(message);
                return;
            case 28:
                AsyncResult asyncResult8 = (AsyncResult) message.obj;
                if (this.mSST.mSS.getIsManualSelection()) {
                    setNetworkSelectionModeAutomatic((Message) asyncResult8.result);
                    logd("SET_NETWORK_SELECTION_AUTOMATIC: set to automatic");
                    return;
                } else {
                    logd("SET_NETWORK_SELECTION_AUTOMATIC: already automatic, ignore");
                    return;
                }
            case 29:
                processIccRecordEvents(((Integer) ((AsyncResult) message.obj).result).intValue());
                return;
            case 30:
                AsyncResult asyncResult9 = (AsyncResult) message.obj;
                Rlog.d(LOG_TAG, "EVENT_ICC_CHANGED, phone id = " + getPhoneId() + ", result = " + ((Integer) asyncResult9.result));
                if (asyncResult9.userObj != null || (asyncResult9.result != null && getPhoneId() == ((Integer) asyncResult9.result).intValue())) {
                    super.handleMessage(message);
                    return;
                }
                return;
            case 35:
                AsyncResult asyncResult10 = (AsyncResult) message.obj;
                RadioCapability radioCapability = (RadioCapability) asyncResult10.result;
                if (asyncResult10.exception != null) {
                    Rlog.d(LOG_TAG, "get phone radio capability fail, no need to change mRadioCapability");
                } else {
                    radioCapabilityUpdated(radioCapability, false);
                }
                Rlog.d(LOG_TAG, "EVENT_GET_RADIO_CAPABILITY: phone rc: " + radioCapability);
                return;
            case 36:
                AsyncResult asyncResult11 = (AsyncResult) message.obj;
                logd("Event EVENT_SS received");
                new GsmMmiCode(this, this.mUiccApplication.get()).processSsData(asyncResult11);
                return;
            case 39:
            case 40:
                String str2 = i == 39 ? "EVENT_VOICE_RADIO_TECH_CHANGED" : "EVENT_REQUEST_VOICE_RADIO_TECH_DONE";
                AsyncResult asyncResult12 = (AsyncResult) message.obj;
                if (asyncResult12.exception == null) {
                    Object obj5 = asyncResult12.result;
                    if (obj5 != null && ((int[]) obj5).length != 0) {
                        logd(str2 + ": newVoiceTech=" + ((int[]) obj5)[0]);
                        return;
                    }
                    loge(str2.concat(": has no tech!"));
                    return;
                }
                loge(str2 + ": exception=" + asyncResult12.exception);
                return;
            case 43:
                if (!this.mContext.getResources().getBoolean(R.bool.kg_center_small_widgets_vertically)) {
                    this.mCi.getVoiceRadioTechnology(obtainMessage(40));
                }
                PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId());
                if (configForSubId != null) {
                    updateNrSettingsAfterCarrierConfigChanged(configForSubId);
                    if (hasCalling()) {
                        updateVoNrSettings(configForSubId);
                    }
                    updateCarrierN1ModeSupported(configForSubId);
                } else {
                    loge("Failed to retrieve a carrier config bundle for subId=" + getSubId());
                }
                loadAllowedNetworksFromSubscriptionDatabase();
                if (configForSubId != null) {
                    updateDefaultEnable2gSettings(configForSubId);
                }
                this.mCi.getRadioCapability(obtainMessage(35));
                super.handleMessage(message);
                return;
            case 44:
                logd("cdma_roaming_mode change is done");
                return;
            case 45:
                logd("Event EVENT_MODEM_RESET Received isInEcm = " + isInEcm() + " mImsPhone = " + this.mImsPhone);
                if (isInEcm()) {
                    if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
                        EmergencyStateTracker.getInstance().exitEmergencyCallbackMode();
                        return;
                    }
                    Phone phone2 = this.mImsPhone;
                    if (phone2 != null) {
                        phone2.handleExitEmergencyCallbackMode();
                        return;
                    }
                    return;
                }
                return;
            case 46:
                Pair pair2 = (Pair) ((AsyncResult) message.obj).result;
                onVoiceRegStateOrRatChanged(((Integer) pair2.first).intValue(), ((Integer) pair2.second).intValue());
                return;
            case 47:
                logd("EVENT EVENT_RADIO_STATE_CHANGED");
                handleRadioPowerStateChange();
                return;
            case 48:
                getDataSettingsManager().setDataEnabled(2, ((Boolean) ((AsyncResult) message.obj).result).booleanValue(), this.mContext.getOpPackageName());
                return;
            case 51:
                AsyncResult asyncResult13 = (AsyncResult) message.obj;
                if (asyncResult13.exception == null && (obj = asyncResult13.result) != null && this.mSST != null) {
                    ArrayList arrayList = new ArrayList();
                    for (OperatorInfo operatorInfo : (List) obj) {
                        if (OperatorInfo.State.CURRENT == operatorInfo.getState()) {
                            arrayList.add(new OperatorInfo(this.mSST.filterOperatorNameByPattern(operatorInfo.getOperatorAlphaLong()), this.mSST.filterOperatorNameByPattern(operatorInfo.getOperatorAlphaShort()), operatorInfo.getOperatorNumeric() + "+" + operatorInfo.getRadioTech(), operatorInfo.getState()));
                        } else {
                            arrayList.add(operatorInfo);
                        }
                    }
                    asyncResult13.result = arrayList;
                }
                Message message6 = (Message) asyncResult13.userObj;
                if (message6 != null) {
                    AsyncResult.forMessage(message6, asyncResult13.result, asyncResult13.exception);
                    message6.sendToTarget();
                    return;
                }
                return;
            case 53:
            case 55:
                AsyncResult asyncResult14 = (AsyncResult) message.obj;
                if (asyncResult14 == null) {
                    return;
                }
                if (asyncResult14.exception != null) {
                    logd("Received exception on event" + message.what + " : " + asyncResult14.exception);
                    return;
                }
                this.mUiccApplicationsEnabled = (Boolean) asyncResult14.result;
                break;
            case 54:
                break;
            case 56:
                AsyncResult asyncResult15 = (AsyncResult) message.obj;
                if (asyncResult15 == null || asyncResult15.exception == null || (pair = (Pair) asyncResult15.userObj) == null) {
                    return;
                }
                boolean zBooleanValue = ((Boolean) pair.first).booleanValue();
                final int iIntValue = ((Integer) pair.second).intValue();
                CommandException.Error commandError = ((CommandException) asyncResult15.exception).getCommandError();
                loge("Error received when re-applying uicc application setting to " + zBooleanValue + " on phone " + this.mPhoneId + " Error code: " + commandError + " retry count left: " + iIntValue);
                if (iIntValue > 0) {
                    if (commandError == CommandException.Error.GENERIC_FAILURE || commandError == CommandException.Error.SIM_BUSY) {
                        postDelayed(new Runnable() { // from class: com.android.internal.telephony.GsmCdmaPhone$$ExternalSyntheticLambda6
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$handleMessage$3(iIntValue);
                            }
                        }, 5000L);
                        return;
                    }
                    return;
                }
                return;
            case 57:
                logd("Event RegistrationFailed Received");
                RegistrationFailedEvent registrationFailedEvent = (RegistrationFailedEvent) ((AsyncResult) message.obj).result;
                this.mNotifier.notifyRegistrationFailed(this, registrationFailedEvent.cellIdentity, registrationFailedEvent.chosenPlmn, registrationFailedEvent.domain, registrationFailedEvent.causeCode, registrationFailedEvent.additionalCauseCode);
                return;
            case 58:
                logd("Event BarringInfoChanged Received");
                this.mNotifier.notifyBarringInfoChanged(this, (BarringInfo) ((AsyncResult) message.obj).result);
                return;
            case 59:
                AsyncResult asyncResult16 = (AsyncResult) message.obj;
                if (asyncResult16.exception == null && (obj2 = asyncResult16.result) != null) {
                    updateLinkCapacityEstimate((List) obj2);
                    return;
                } else {
                    logd("Unexpected exception on EVENT_LINK_CAPACITY_CHANGED");
                    return;
                }
            case 60:
                resetCarrierKeysForImsiEncryption();
                return;
            case 61:
                logd("EVENT_SET_VONR_ENABLED_DONE is done");
                return;
            case 62:
                logd("EVENT_SUBSCRIPTIONS_CHANGED");
                updateUsageSetting();
                updateNullCipherNotifier();
                return;
            case 65:
                logd("EVENT_IMS_DEREGISTRATION_TRIGGERED");
                AsyncResult asyncResult17 = (AsyncResult) message.obj;
                Throwable th = asyncResult17.exception;
                if (th == null) {
                    this.mImsPhone.triggerImsDeregistration(((int[]) asyncResult17.result)[0]);
                    return;
                } else {
                    Rlog.e(LOG_TAG, "Unexpected unsol with exception", th);
                    return;
                }
            case 66:
                logd("EVENT_SET_NULL_CIPHER_AND_INTEGRITY_DONE");
                this.mIsNullCipherAndIntegritySupported = doesResultIndicateModemSupport((AsyncResult) message.obj);
                return;
            case 67:
                parseImeiInfo(message);
                return;
            case 68:
                logd("EVENT_TRIGGER_NOTIFY_ANBR");
                AsyncResult asyncResult18 = (AsyncResult) message.obj;
                if (asyncResult18.exception != null || (phone = this.mImsPhone) == null) {
                    return;
                }
                Object obj6 = asyncResult18.result;
                phone.triggerNotifyAnbr(((int[]) obj6)[0], ((int[]) obj6)[1], ((int[]) obj6)[2]);
                return;
            case 69:
                logd("EVENT_GET_N1_MODE_ENABLED_DONE");
                AsyncResult asyncResult19 = (AsyncResult) message.obj;
                if (asyncResult19 == null || asyncResult19.exception != null || (obj3 = asyncResult19.result) == null || !(obj3 instanceof Boolean)) {
                    Rlog.e(LOG_TAG, "Failed to Retrieve N1 Mode", asyncResult19.exception);
                    Object obj7 = asyncResult19.userObj;
                    if (obj7 instanceof Message) {
                        Message message7 = (Message) obj7;
                        AsyncResult.forMessage(message7, (Object) null, asyncResult19.exception);
                        message7.sendToTarget();
                        return;
                    }
                    return;
                }
                this.mModemN1Mode = (Boolean) obj3;
                maybeUpdateModemN1Mode((Message) asyncResult19.userObj);
                return;
            case 70:
                logd("EVENT_SET_N1_MODE_ENABLED_DONE");
                AsyncResult asyncResult20 = (AsyncResult) message.obj;
                if (asyncResult20 == null || asyncResult20.exception != null) {
                    Rlog.e(LOG_TAG, "Failed to Set N1 Mode", asyncResult20.exception);
                    this.mModemN1Mode = null;
                }
                if (asyncResult20 != null) {
                    Object obj8 = asyncResult20.userObj;
                    if (obj8 instanceof Message) {
                        Message message8 = (Message) obj8;
                        AsyncResult.forMessage(message8, (Object) null, asyncResult20.exception);
                        message8.sendToTarget();
                        return;
                    }
                    return;
                }
                return;
            case 71:
                logd("EVENT_GET_DEVICE_IMEI_CHANGE_DONE phoneId = " + getPhoneId());
                parseImeiInfo(message);
                return;
            case 72:
                logd("EVENT_CELL_IDENTIFIER_DISCLOSURE phoneId = " + getPhoneId());
                AsyncResult asyncResult21 = (AsyncResult) message.obj;
                if (asyncResult21 == null || (obj4 = asyncResult21.result) == null || asyncResult21.exception != null) {
                    Rlog.e(LOG_TAG, "Failed to process cellular identifier disclosure", asyncResult21.exception);
                    return;
                }
                CellularIdentifierDisclosure cellularIdentifierDisclosure = (CellularIdentifierDisclosure) obj4;
                if (this.mIdentifierDisclosureNotifier == null || cellularIdentifierDisclosure == null) {
                    logd("EVENT_CELL_IDENTIFIER_DISCLOSURE mIdentifierDisclosureNotifier or disclosure is null.");
                    return;
                }
                if (queueCellularEventIfSubIdInvalid(message, "EVENT_CELL_IDENTIFIER_DISCLOSURE")) {
                    return;
                }
                this.mIdentifierDisclosureNotifier.addDisclosure(this.mContext, getSubId(), cellularIdentifierDisclosure);
                logd("EVENT_CELL_IDENTIFIER_DISCLOSURE for non-Safety Center listeners phoneId = " + getPhoneId());
                this.mNotifier.notifyCellularIdentifierDisclosedChanged(this, cellularIdentifierDisclosure);
                return;
            case 73:
                logd("EVENT_SET_IDENTIFIER_DISCLOSURE_ENABLED_DONE");
                this.mIsIdentifierDisclosureTransparencySupported = doesResultIndicateModemSupport((AsyncResult) message.obj);
                return;
            case 74:
                logd("EVENT_SECURITY_ALGORITHM_UPDATE phoneId = " + getPhoneId());
                SecurityAlgorithmUpdate securityAlgorithmUpdate = (SecurityAlgorithmUpdate) ((AsyncResult) message.obj).result;
                if (this.mNullCipherNotifier == null) {
                    logd("EVENT_SECURITY_ALGORITHM_UPDATE mNullCipherNotifier is null.");
                    return;
                }
                if (queueCellularEventIfSubIdInvalid(message, "EVENT_SECURITY_ALGORITHM_UPDATE")) {
                    return;
                }
                this.mNullCipherNotifier.onSecurityAlgorithmUpdate(this.mContext, getPhoneId(), getSubId(), securityAlgorithmUpdate);
                logd("EVENT_SECURITY_ALGORITHM_UPDATE for non-Safety Center listeners phoneId = " + getPhoneId());
                this.mNotifier.notifySecurityAlgorithmsChanged(this, securityAlgorithmUpdate);
                return;
            case 75:
                logd("EVENT_SET_SECURITY_ALGORITHMS_UPDATED_ENABLED_DONE");
                this.mIsNullCipherNotificationSupported = doesResultIndicateModemSupport((AsyncResult) message.obj);
                return;
            case 76:
                logd("EVENT_NETWORK_SECURITY_EVENTS phoneId = " + getPhoneId());
                AsyncResult asyncResult22 = (AsyncResult) message.obj;
                if (asyncResult22 == null) {
                    Rlog.e(LOG_TAG, "EVENT_NETWORK_SECURITY_EVENTS: ar is null");
                    return;
                }
                Object obj9 = asyncResult22.result;
                if (obj9 == null || asyncResult22.exception != null) {
                    Rlog.e(LOG_TAG, "Failed to process network security events", asyncResult22.exception);
                    return;
                }
                Set<NetworkSecurityEvent> set = (Set) obj9;
                if (queueCellularEventIfSubIdInvalid(message, "EVENT_NETWORK_SECURITY_EVENTS")) {
                    return;
                }
                logd("EVENT_NETWORK_SECURITY_EVENTS for non-Safety Center listeners phoneId = " + getPhoneId());
                this.mNotifier.notifyNetworkSecurityEvents(this, set);
                return;
            case 77:
                logd("EVENT_SET_ALLOWED_NETWORK_TYPES_FOR_2G_DISABLED_DONE");
                AsyncResult asyncResult23 = (AsyncResult) message.obj;
                if (asyncResult23.exception == null) {
                    sendNotification2gDisabledByCarrier();
                    return;
                }
                set2gProtectionDefaultUpdated(false);
                loge("Failed to execute setAllowedNetworkTypesForReason:" + asyncResult23.exception);
                return;
        }
        reapplyUiccAppsEnablementIfNeeded(ENABLE_UICC_APPS_MAX_RETRIES);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleMessage$3(int i) {
        reapplyUiccAppsEnablementIfNeeded(i - 1);
    }

    private boolean queueCellularEventIfSubIdInvalid(Message message, String str) {
        if (SubscriptionManager.isValidSubscriptionId(getSubId())) {
            return false;
        }
        logd("Adding event to message queue with event name: " + str);
        this.mCellularEventMessages.add(Message.obtain(message));
        return true;
    }

    private boolean doesResultIndicateModemSupport(AsyncResult asyncResult) {
        return asyncResult == null || asyncResult.exception == null;
    }

    private void parseImeiInfo(Message message) {
        Object obj;
        AsyncResult asyncResult = (AsyncResult) message.obj;
        if (asyncResult.exception != null || (obj = asyncResult.result) == null) {
            loge("parseImeiInfo :: Exception received : " + asyncResult.exception);
            return;
        }
        ImeiInfo imeiInfo = (ImeiInfo) obj;
        if (!TextUtils.isEmpty(imeiInfo.imei)) {
            this.mImeiType = imeiInfo.type;
            this.mImei = imeiInfo.imei;
            if (TextUtils.isEmpty(imeiInfo.svn)) {
                return;
            }
            this.mImeiSv = imeiInfo.svn;
            return;
        }
        loge("parseImeiInfo :: IMEI value is empty");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean currentSlotSubIdChanged() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        StringBuilder sb = new StringBuilder(CURR_SUBID);
        sb.append(this.mPhoneId);
        boolean z = defaultSharedPreferences.getInt(sb.toString(), -1) != getSubId();
        if (z) {
            SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
            editorEdit.putInt(CURR_SUBID + this.mPhoneId, getSubId());
            editorEdit.apply();
        }
        Rlog.d(LOG_TAG, "currentSlotSubIdChanged: changed=" + z);
        return z;
    }

    public UiccCardApplication getUiccCardApplication() {
        return this.mUiccController.getUiccCardApplication(this.mPhoneId, 1);
    }

    @Override // com.android.internal.telephony.Phone
    protected void onUpdateIccAvailability() {
        IsimUiccRecords isimUiccRecords;
        UiccController uiccController = this.mUiccController;
        if (uiccController == null) {
            return;
        }
        UiccCardApplication uiccCardApplication = uiccController.getUiccCardApplication(this.mPhoneId, 3);
        if (uiccCardApplication != null) {
            isimUiccRecords = (IsimUiccRecords) uiccCardApplication.getIccRecords();
            logd("New ISIM application found");
        } else {
            isimUiccRecords = null;
        }
        this.mIsimUiccRecords = isimUiccRecords;
        SIMRecords sIMRecords = this.mSimRecords;
        if (sIMRecords != null) {
            sIMRecords.unregisterForRecordsLoaded(this);
        }
        this.mSimRecords = null;
        UiccCardApplication uiccCardApplication2 = getUiccCardApplication();
        UiccCardApplication uiccCardApplication3 = this.mUiccApplication.get();
        if (uiccCardApplication3 != uiccCardApplication2) {
            if (uiccCardApplication3 != null) {
                logd("Removing stale icc objects.");
                if (this.mIccRecords.get() != null) {
                    unregisterForIccRecordEvents();
                    this.mIccPhoneBookIntManager.updateIccRecords(null);
                    MiuiUiccControllerStub.get(this.mPhoneId).updateIccRecords(this.mPhoneId, null);
                }
                this.mIccRecords.set(null);
                this.mUiccApplication.set(null);
            }
            if (uiccCardApplication2 != null) {
                logd("New Uicc application found. type = " + uiccCardApplication2.getType());
                IccRecords iccRecords = uiccCardApplication2.getIccRecords();
                this.mUiccApplication.set(uiccCardApplication2);
                this.mIccRecords.set(iccRecords);
                logd("mIccRecords = " + this.mIccRecords);
                registerForIccRecordEvents();
                this.mIccPhoneBookIntManager.updateIccRecords(iccRecords);
                MiuiUiccControllerStub.get(this.mPhoneId).updateIccRecords(this.mPhoneId, this.mIccRecords.get());
                if (iccRecords != null && !TelephonyCommonUtilsStub.get().isMiuiRom()) {
                    String operatorNumeric = iccRecords.getOperatorNumeric();
                    logd("New simOperatorNumeric = " + operatorNumeric);
                    if (!TextUtils.isEmpty(operatorNumeric)) {
                        TelephonyManager.from(this.mContext).setSimOperatorNumericForPhone(this.mPhoneId, operatorNumeric);
                    }
                }
                updateCurrentCarrierInProvider();
            }
        }
        reapplyUiccAppsEnablementIfNeeded(ENABLE_UICC_APPS_MAX_RETRIES);
    }

    @Override // com.android.internal.telephony.Phone
    public SIMRecords getSIMRecords() {
        return this.mSimRecords;
    }

    protected void processIccRecordEvents(int i) {
        if (i != 1) {
            return;
        }
        logi("processIccRecordEvents: EVENT_CFI");
        notifyCallForwardingIndicator();
    }

    @Override // com.android.internal.telephony.Phone
    public boolean updateCurrentCarrierInProvider() {
        long defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        String operatorNumeric = getOperatorNumeric();
        logd("updateCurrentCarrierInProvider: mSubId = " + getSubId() + " currentDds = " + defaultDataSubscriptionId + " operatorNumeric = " + operatorNumeric);
        if (TextUtils.isEmpty(operatorNumeric) || getSubId() != defaultDataSubscriptionId) {
            return false;
        }
        try {
            Uri uriWithAppendedPath = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current");
            ContentValues contentValues = new ContentValues();
            contentValues.put("numeric", operatorNumeric);
            this.mContext.getContentResolver().insert(uriWithAppendedPath, contentValues);
            return true;
        } catch (SQLException e) {
            Rlog.e(LOG_TAG, "Can't store current operator", e);
            return false;
        }
    }

    protected void handleCfuQueryResult(CallForwardInfo[] callForwardInfoArr) {
        if (callForwardInfoArr == null || callForwardInfoArr.length == 0) {
            setVoiceCallForwardingFlag(1, false, null);
            return;
        }
        for (CallForwardInfo callForwardInfo : callForwardInfoArr) {
            if ((callForwardInfo.serviceClass & 1) != 0) {
                setVoiceCallForwardingFlag(1, callForwardInfo.status == 1, callForwardInfo.number);
                return;
            }
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public IccPhoneBookInterfaceManager getIccPhoneBookInterfaceManager() {
        return this.mIccPhoneBookIntManager;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void activateCellBroadcastSms(int i, Message message) {
        loge("[GsmCdmaPhone] activateCellBroadcastSms() is obsolete; use SmsManager");
        message.sendToTarget();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void getCellBroadcastSmsConfig(Message message) {
        loge("[GsmCdmaPhone] getCellBroadcastSmsConfig() is obsolete; use SmsManager");
        message.sendToTarget();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setCellBroadcastSmsConfig(int[] iArr, Message message) {
        loge("[GsmCdmaPhone] setCellBroadcastSmsConfig() is obsolete; use SmsManager");
        message.sendToTarget();
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isCspPlmnEnabled() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            return iccRecords.isCspPlmnEnabled();
        }
        return false;
    }

    public boolean shouldForceAutoNetworkSelect() {
        RadioAccessFamily.getRafFromNetworkType(RILConstants.PREFERRED_NETWORK_MODE);
        if (!SubscriptionManager.isValidSubscriptionId(getSubId())) {
            return false;
        }
        int allowedNetworkTypes = (int) getAllowedNetworkTypes(0);
        logd("shouldForceAutoNetworkSelect in mode = " + allowedNetworkTypes);
        if (isManualSelProhibitedInGlobalMode() && (allowedNetworkTypes == RadioAccessFamily.getRafFromNetworkType(10) || allowedNetworkTypes == RadioAccessFamily.getRafFromNetworkType(7))) {
            logd("Should force auto network select mode = " + allowedNetworkTypes);
            return true;
        }
        logd("Should not force auto network select mode = " + allowedNetworkTypes);
        return false;
    }

    private boolean isManualSelProhibitedInGlobalMode() {
        String[] strArrSplit;
        String string = getContext().getResources().getString(R.string.toolbar_collapse_description);
        boolean z = false;
        if (!TextUtils.isEmpty(string) && (strArrSplit = string.split(";")) != null && ((strArrSplit.length == 1 && strArrSplit[0].equalsIgnoreCase("true")) || (strArrSplit.length == 2 && !TextUtils.isEmpty(strArrSplit[1]) && strArrSplit[0].equalsIgnoreCase("true") && isMatchGid(strArrSplit[1])))) {
            z = true;
        }
        logd("isManualNetSelAllowedInGlobal in current carrier is " + z);
        return z;
    }

    protected void registerForIccRecordEvents() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords == null) {
            return;
        }
        iccRecords.registerForNetworkSelectionModeAutomatic(this, 28, null);
        iccRecords.registerForRecordsEvents(this, 29, null);
        iccRecords.registerForRecordsLoaded(this, 3, null);
    }

    protected void unregisterForIccRecordEvents() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords == null) {
            return;
        }
        iccRecords.unregisterForNetworkSelectionModeAutomatic(this);
        iccRecords.unregisterForRecordsEvents(this);
        iccRecords.unregisterForRecordsLoaded(this);
    }

    @Override // com.android.internal.telephony.Phone
    public void exitEmergencyCallbackMode() {
        Rlog.d(LOG_TAG, "exitEmergencyCallbackMode: mImsPhone=" + this.mImsPhone);
        if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
            EmergencyStateTracker.getInstance().exitEmergencyCallbackMode();
            return;
        }
        Phone phone = this.mImsPhone;
        if (phone != null && phone.isInImsEcm()) {
            this.mImsPhone.exitEmergencyCallbackMode();
            return;
        }
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
        this.mCi.exitEmergencyCallbackMode(this.mIsTestingEmergencyCallbackMode ? obtainMessage(26) : null);
    }

    public void notifyEmergencyCallRegistrants(boolean z) {
        this.mEmergencyCallToggledRegistrants.notifyResult(Integer.valueOf(z ? 1 : 0));
    }

    @Override // com.android.internal.telephony.Phone
    public void setLinkCapacityReportingCriteria(int[] iArr, int[] iArr2, int i) {
        this.mCi.setLinkCapacityReportingCriteria(GbaManager.RETRY_TIME_MS, 50, 50, iArr, iArr2, i, null);
    }

    @Override // com.android.internal.telephony.Phone
    public IccSmsInterfaceManager getIccSmsInterfaceManager() {
        return this.mIccSmsInterfaceManager;
    }

    @Override // com.android.internal.telephony.Phone
    public void setImsRegistrationState(boolean z) {
        this.mSST.setImsRegistrationState(z);
        this.mCallWaitingController.setImsRegistrationState(z);
    }

    @Override // com.android.internal.telephony.Phone
    public boolean getIccRecordsLoaded() {
        UiccProfile uiccProfile = getUiccProfile();
        return uiccProfile != null && uiccProfile.getIccRecordsLoaded();
    }

    @Override // com.android.internal.telephony.Phone
    public IccCard getIccCard() {
        UiccProfile uiccProfile = getUiccProfile();
        if (uiccProfile != null) {
            return uiccProfile;
        }
        UiccSlot uiccSlotForPhone = this.mUiccController.getUiccSlotForPhone(this.mPhoneId);
        if (uiccSlotForPhone == null || uiccSlotForPhone.isStateUnknown()) {
            return new IccCard(IccCardConstants.State.UNKNOWN);
        }
        return new IccCard(IccCardConstants.State.ABSENT);
    }

    protected UiccProfile getUiccProfile() {
        return UiccController.getInstance().getUiccProfileForPhone(this.mPhoneId);
    }

    @Override // com.android.internal.telephony.Phone
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("GsmCdmaPhone extends:");
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println(" mPrecisePhoneType=" + this.mPrecisePhoneType);
        printWriter.println(" mSimRecords=" + this.mSimRecords);
        printWriter.println(" mIsimUiccRecords=" + this.mIsimUiccRecords);
        printWriter.println(" mCT=" + this.mCT);
        printWriter.println(" mSST=" + this.mSST);
        printWriter.println(" mPendingMMIs=" + this.mPendingMMIs);
        printWriter.println(" mIccPhoneBookIntManager=" + this.mIccPhoneBookIntManager);
        printWriter.println(" mImei=" + pii(this.mImei));
        printWriter.println(" mImeiSv=" + pii(this.mImeiSv));
        printWriter.println(" mVmNumber=" + pii(this.mVmNumber));
        printWriter.println(" mWakeLock=" + this.mWakeLock);
        printWriter.println(" isInEcm()=" + isInEcm());
        printWriter.println(" mEsn=" + pii(this.mEsn));
        printWriter.println(" mMeid=" + pii(this.mMeid));
        printWriter.println(" isCspPlmnEnabled()=" + isCspPlmnEnabled());
        printWriter.println(" mManualNetworkSelectionPlmn=" + this.mManualNetworkSelectionPlmn);
        printWriter.println(" mTelecomVoiceServiceStateOverride=" + this.mTelecomVoiceServiceStateOverride + "(" + ServiceState.rilServiceStateToString(this.mTelecomVoiceServiceStateOverride) + ")");
        StringBuilder sb = new StringBuilder(" mUiccApplicationsEnabled=");
        sb.append(this.mUiccApplicationsEnabled);
        printWriter.println(sb.toString());
        printWriter.flush();
        try {
            this.mCallWaitingController.dump(printWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        printWriter.flush();
        try {
            this.mCellBroadcastConfigTracker.dump(fileDescriptor, printWriter, strArr);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        printWriter.flush();
    }

    @Override // com.android.internal.telephony.Phone
    public boolean setOperatorBrandOverride(String str) {
        UiccPort uiccPort;
        UiccController uiccController = this.mUiccController;
        if (uiccController == null || (uiccPort = uiccController.getUiccPort(getPhoneId())) == null) {
            return false;
        }
        boolean operatorBrandOverride = uiccPort.setOperatorBrandOverride(str);
        if (operatorBrandOverride) {
            TelephonyManager.from(this.mContext).setSimOperatorNameForPhone(getPhoneId(), MiuiUiccControllerStub.get(this.mPhoneId).onOperatorNumericOrNameSet(getPhoneId(), "gsm.sim.operator.alpha", this.mSST.getServiceProviderName()));
            this.mSST.pollState();
        }
        return operatorBrandOverride;
    }

    private String checkForTestEmergencyNumber(String str) {
        String str2 = SystemProperties.get("ril.test.emergencynumber");
        if (!TextUtils.isEmpty(str2)) {
            String[] strArrSplit = str2.split(":");
            logd("checkForTestEmergencyNumber: values.length=" + strArrSplit.length);
            if (strArrSplit.length == 2 && strArrSplit[0].equals(PhoneNumberUtils.stripSeparators(str))) {
                logd("checkForTestEmergencyNumber: remap " + str + " to " + strArrSplit[1]);
                return strArrSplit[1];
            }
        }
        return str;
    }

    @Override // com.android.internal.telephony.Phone
    public String getOperatorNumeric() {
        IccRecords iccRecords = this.mIccRecords.get();
        return TextUtils.emptyIfNull(iccRecords != null ? iccRecords.getOperatorNumeric() : null);
    }

    public String getCountryIso() {
        SubscriptionInfo activeSubscriptionInfo = SubscriptionManager.from(getContext()).getActiveSubscriptionInfo(getSubId());
        if (activeSubscriptionInfo == null || TextUtils.isEmpty(activeSubscriptionInfo.getCountryIso())) {
            return null;
        }
        return activeSubscriptionInfo.getCountryIso().toUpperCase(Locale.ROOT);
    }

    public void notifyEcbmTimerReset(Boolean bool) {
        this.mEcmTimerResetRegistrants.notifyResult(bool);
    }

    public int getCsCallRadioTech() {
        ServiceStateTracker serviceStateTracker = this.mSST;
        if (serviceStateTracker != null) {
            return getCsCallRadioTech(serviceStateTracker.mSS.getState(), this.mSST.mSS.getRilVoiceRadioTechnology());
        }
        return 0;
    }

    private int getCsCallRadioTech(int i, int i2) {
        logd("getCsCallRadioTech, current vrs=" + i + ", vrat=" + i2);
        if (i != 0 || ArrayUtils.contains(VOICE_PS_CALL_RADIO_TECHNOLOGY, i2)) {
            i2 = 0;
        }
        logd("getCsCallRadioTech, result calcVrat=" + i2);
        return i2;
    }

    private void onVoiceRegStateOrRatChanged(int i, int i2) {
        if (hasCalling()) {
            logd("onVoiceRegStateOrRatChanged");
            this.mCT.dispatchCsCallRadioTech(getCsCallRadioTech(i, i2));
        }
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForEcmTimerReset(Handler handler, int i, Object obj) {
        this.mEcmTimerResetRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForEcmTimerReset(Handler handler) {
        this.mEcmTimerResetRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForVolteSilentRedial(Handler handler, int i, Object obj) {
        this.mVolteSilentRedialRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForVolteSilentRedial(Handler handler) {
        this.mVolteSilentRedialRegistrants.remove(handler);
    }

    public void notifyVolteSilentRedial(String str, int i) {
        logd("notifyVolteSilentRedial: dialString=" + str + " causeCode=" + i);
        this.mVolteSilentRedialRegistrants.notifyRegistrants(new AsyncResult((Object) null, new Phone.SilentRedialParam(str, i, this.mDialArgs), (Throwable) null));
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForEmergencyDomainSelected(Handler handler, int i, Object obj) {
        this.mEmergencyDomainSelectedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForEmergencyDomainSelected(Handler handler) {
        this.mEmergencyDomainSelectedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.Phone
    public void notifyEmergencyDomainSelected(int i) {
        logd("notifyEmergencyDomainSelected transportType=" + i);
        this.mEmergencyDomainSelectedRegistrants.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(i), (Throwable) null));
    }

    @Override // com.android.internal.telephony.Phone
    public void setVoiceMessageWaiting(int i, int i2) {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null) {
            iccRecords.setVoiceMessageWaiting(i, i2);
        } else {
            logd("SIM Records not found, MWI not updated");
        }
    }

    protected void logd(String str) {
        Rlog.d(LOG_TAG, "[" + this.mPhoneId + "] " + str);
    }

    protected void logi(String str) {
        Rlog.i(LOG_TAG, "[" + this.mPhoneId + "] " + str);
    }

    protected void loge(String str) {
        Rlog.e(LOG_TAG, "[" + this.mPhoneId + "] " + str);
    }

    protected static String pii(String str) {
        return Rlog.pii(LOG_TAG, str);
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isUtEnabled() {
        Phone phone = this.mImsPhone;
        if (phone != null) {
            return phone.isUtEnabled();
        }
        logd("isUtEnabled: called for GsmCdma");
        return false;
    }

    public String getDtmfToneDelayKey() {
        return "gsm_dtmf_tone_delay_int";
    }

    public PowerManager.WakeLock getWakeLock() {
        return this.mWakeLock;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTtyMode(int i) {
        logi(String.format("updateTtyMode ttyMode=%d", Integer.valueOf(i)));
        setTTYMode(ttyModeToPhoneMode(i), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUiTtyMode(int i) {
        logi(String.format("updateUiTtyMode ttyMode=%d", Integer.valueOf(i)));
        setUiTTYMode(ttyModeToPhoneMode(i), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: loadTtyMode, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        if (hasCalling()) {
            TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
            updateTtyMode(telephonyManager != null ? telephonyManager.getCurrentTtyMode() : 0);
            updateUiTtyMode(Settings.Secure.getInt(this.mContext.getContentResolver(), "preferred_tty_mode", 0));
        }
    }

    protected void reapplyUiccAppsEnablementIfNeeded(int i) {
        UiccSlot uiccSlotForPhone = this.mUiccController.getUiccSlotForPhone(this.mPhoneId);
        if (uiccSlotForPhone == null || uiccSlotForPhone.getCardState() != IccCardStatus.CardState.CARDSTATE_PRESENT || this.mUiccApplicationsEnabled == null) {
            StringBuilder sb = new StringBuilder("reapplyUiccAppsEnablementIfNeeded: slot state=");
            sb.append(uiccSlotForPhone != null ? uiccSlotForPhone.getCardState() : null);
            loge(sb.toString());
            return;
        }
        final String iccId = uiccSlotForPhone.getIccId(uiccSlotForPhone.getPortIndexFromPhoneId(this.mPhoneId));
        if (iccId == null) {
            loge("reapplyUiccAppsEnablementIfNeeded iccId is null, phoneId: " + this.mPhoneId + " portIndex: " + uiccSlotForPhone.getPortIndexFromPhoneId(this.mPhoneId));
            return;
        }
        SubscriptionInfo subscriptionInfoOrElse = this.mSubscriptionManagerService.getAllSubInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag()).stream().filter(new Predicate() { // from class: com.android.internal.telephony.GsmCdmaPhone$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((SubscriptionInfo) obj).getIccId().equals(IccUtils.stripTrailingFs(iccId));
            }
        }).findFirst().orElse(null);
        logd("reapplyUiccAppsEnablementIfNeeded: retries=" + i + ", subInfo=" + subscriptionInfoOrElse);
        boolean z = subscriptionInfoOrElse == null || subscriptionInfoOrElse.areUiccApplicationsEnabled();
        if (z != this.mUiccApplicationsEnabled.booleanValue()) {
            this.mCi.enableUiccApplications(z, Message.obtain(this, 56, new Pair(Boolean.valueOf(z), Integer.valueOf(i))));
        }
    }

    @Override // com.android.internal.telephony.Phone
    public void enableUiccApplications(boolean z, Message message) {
        UiccSlot uiccSlotForPhone = this.mUiccController.getUiccSlotForPhone(this.mPhoneId);
        if (uiccSlotForPhone != null && uiccSlotForPhone.getCardState() == IccCardStatus.CardState.CARDSTATE_PRESENT) {
            this.mCi.enableUiccApplications(z, message);
        } else if (message != null) {
            AsyncResult.forMessage(message, (Object) null, new IllegalStateException("No SIM card is present"));
            message.sendToTarget();
        }
    }

    @Override // com.android.internal.telephony.Phone
    public boolean canDisablePhysicalSubscription() {
        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
            return super.canDisablePhysicalSubscription();
        }
        return this.mCi.canToggleUiccApplicationsEnablement();
    }

    @Override // com.android.internal.telephony.Phone
    public List<String> getEquivalentHomePlmns() {
        IccRecords iccRecords = this.mIccRecords.get();
        if (iccRecords != null && iccRecords.getEhplmns() != null) {
            return Arrays.asList(iccRecords.getEhplmns());
        }
        return Collections.EMPTY_LIST;
    }

    @Override // com.android.internal.telephony.Phone
    public List<String> getDataServicePackages() {
        return getDataNetworkController().getDataServicePackages();
    }

    protected void updateNrSettingsAfterCarrierConfigChanged(PersistableBundle persistableBundle) {
        this.mIsCarrierNrSupported = !ArrayUtils.isEmpty(persistableBundle.getIntArray("carrier_nr_availabilities_int_array"));
    }

    /* JADX WARN: Code duplicated, block: B:22:0x0071  */
    protected void updateVoNrSettings(PersistableBundle persistableBundle) {
        boolean z;
        if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
            logi("didn't updateVoNrSettings for add on case, will update by ims.");
            return;
        }
        if (CarrierConfigManager.isConfigForIdentifiedCarrier(persistableBundle) && getIccCard().getState() == IccCardConstants.State.LOADED) {
            boolean z2 = persistableBundle.getBoolean("vonr_enabled_bool");
            boolean z3 = persistableBundle.getBoolean("vonr_on_by_default_bool");
            SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(getSubId());
            int nrAdvancedCallingEnabled = subscriptionInfoInternal != null ? subscriptionInfoInternal.getNrAdvancedCallingEnabled() : -1;
            logd("VoNR setting from telephony.db:" + nrAdvancedCallingEnabled + " ,vonr_enabled_bool:" + z2 + " ,vonr_on_by_default_bool:" + z3);
            if (z2) {
                z = true;
                if (nrAdvancedCallingEnabled != 1 && (nrAdvancedCallingEnabled != -1 || !z3)) {
                    z = false;
                }
            } else {
                z = false;
            }
            this.mCi.setVoNrEnabled(z, obtainMessage(61), null);
            super.setAllowedImsServicesForAny(3, z);
        }
    }

    protected void updateDefaultEnable2gSettings(PersistableBundle persistableBundle) {
        if (!SubscriptionManager.isValidSubscriptionId(getSubId())) {
            logd("updateDefaultEnable2gSettings subId is invalid subId:" + getSubId());
        } else if (RadioInterfaceCapabilityController.getInstance().getCapabilities().contains("CAPABILITY_USES_ALLOWED_NETWORK_TYPES_BITMASK") && persistableBundle.getBoolean("carrier_default_2g_protection_enabled_bool") && !is2gProtectionDefaultUpdated()) {
            set2gProtectionDefaultUpdated(true);
            long allowedNetworkTypes = getAllowedNetworkTypes(3);
            if ((32843 & allowedNetworkTypes) != 0) {
                disable2gNetworkType(allowedNetworkTypes);
            }
        }
    }

    private void disable2gNetworkType(long j) {
        setAllowedNetworkTypes(3, j & (-32844), obtainMessage(77));
        logi("2G is disabled by carrier.");
    }

    private void sendNotification2gDisabledByCarrier() {
        ComponentName componentNameUnflattenFromString = ComponentName.unflattenFromString(getContext().getResources().getString(R.string.default_audio_route_id));
        if (componentNameUnflattenFromString != null) {
            Intent intent = new Intent();
            intent.setAction("android.telephony.action.2G_DISABLED_BY_CARRIER");
            intent.setComponent(componentNameUnflattenFromString);
            intent.putExtra("android.telephony.extra.SUBSCRIPTION_ID", getSubId());
            if (this.mContext.getPackageManager().queryBroadcastReceivers(intent, 0).size() > 0) {
                this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
            }
        }
    }

    private boolean is2gProtectionDefaultUpdated() {
        int subId = getSubId();
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            return false;
        }
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean(PREF_KEY_HAS_APPLIED_2G_PROTECTION_DEFAULT + subId, false);
    }

    private void set2gProtectionDefaultUpdated(boolean z) {
        int subId = getSubId();
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
            editorEdit.putBoolean(PREF_KEY_HAS_APPLIED_2G_PROTECTION_DEFAULT + subId, z);
            editorEdit.apply();
        }
    }

    public boolean isImsUseEnabled() {
        ImsManager imsManagerCreate = this.mImsManagerFactory.create(this.mContext, this.mPhoneId);
        if (imsManagerCreate.isVolteEnabledByPlatform() && imsManagerCreate.isEnhanced4gLteModeSettingEnabledByUser()) {
            return true;
        }
        return imsManagerCreate.isWfcEnabledByPlatform() && imsManagerCreate.isWfcEnabledByUser() && imsManagerCreate.isNonTtyOrTtyOnVolteEnabled();
    }

    @Override // com.android.internal.telephony.Phone
    public InboundSmsHandler getInboundSmsHandler(boolean z) {
        return this.mIccSmsInterfaceManager.getInboundSmsHandler(z);
    }

    @Override // com.android.internal.telephony.Phone
    public List<CellBroadcastIdRange> getCellBroadcastIdRanges() {
        return this.mCellBroadcastConfigTracker.getCellBroadcastIdRanges();
    }

    @Override // com.android.internal.telephony.Phone
    public void setCellBroadcastIdRanges(List<CellBroadcastIdRange> list, Consumer<Integer> consumer) {
        this.mCellBroadcastConfigTracker.setCellBroadcastIdRanges(list, consumer);
    }

    private boolean isRequestBlockedByFDN(SsData.RequestType requestType, SsData.ServiceType serviceType) {
        return FdnUtils.isSuppServiceRequestBlockedByFdn(this.mPhoneId, GsmMmiCode.getControlStrings(requestType, serviceType), getCountryIso());
    }

    @Override // com.android.internal.telephony.Phone
    public void handleNullCipherEnabledChange() {
        if (!DeviceConfig.getBoolean("cellular_security", "enable_null_cipher_toggle", true)) {
            logi("Not handling null cipher update. Feature disabled by DeviceConfig.");
        } else {
            this.mCi.setNullCipherAndIntegrityEnabled(getNullCipherAndIntegrityEnabledPreference(), obtainMessage(66));
        }
    }

    @Override // com.android.internal.telephony.Phone
    public void handleIdentifierDisclosureNotificationPreferenceChange() {
        boolean identifierDisclosureNotificationsPreferenceEnabled = getIdentifierDisclosureNotificationsPreferenceEnabled();
        CellularIdentifierDisclosureNotifier cellularIdentifierDisclosureNotifier = this.mIdentifierDisclosureNotifier;
        if (identifierDisclosureNotificationsPreferenceEnabled) {
            cellularIdentifierDisclosureNotifier.enable(this.mContext);
        } else {
            cellularIdentifierDisclosureNotifier.disable(this.mContext);
        }
        this.mCi.setCellularIdentifierTransparencyEnabled(identifierDisclosureNotificationsPreferenceEnabled, obtainMessage(73));
    }

    @Override // com.android.internal.telephony.Phone
    public void handleNullCipherNotificationPreferenceChanged() {
        boolean nullCipherNotificationsPreferenceEnabled = getNullCipherNotificationsPreferenceEnabled();
        NullCipherNotifier nullCipherNotifier = this.mNullCipherNotifier;
        if (nullCipherNotificationsPreferenceEnabled) {
            nullCipherNotifier.enable(this.mContext);
        } else {
            nullCipherNotifier.disable(this.mContext);
        }
        this.mCi.setSecurityAlgorithmsUpdatedEnabled(nullCipherNotificationsPreferenceEnabled, obtainMessage(75));
    }

    public void updateNullCipherNotifier() {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(getSubId());
        this.mNullCipherNotifier.setSubscriptionMapping(this.mContext, getPhoneId(), subscriptionInfoInternal != null ? subscriptionInfoInternal.isActive() : false ? subscriptionInfoInternal.getSubscriptionId() : -1);
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isNullCipherAndIntegritySupported() {
        return this.mIsNullCipherAndIntegritySupported;
    }

    protected boolean onHandleInCallMmi0(String str) {
        return handleCallDeflectionIncallSupplementaryService(str);
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isIdentifierDisclosureTransparencySupported() {
        return this.mIsIdentifierDisclosureTransparencySupported;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isNullCipherNotificationSupported() {
        return this.mIsNullCipherNotificationSupported;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshSafetySources$5(String str) {
        this.mSafetySource.refresh(this.mContext, str);
    }

    @Override // com.android.internal.telephony.Phone
    public void refreshSafetySources(final String str) {
        post(new Runnable() { // from class: com.android.internal.telephony.GsmCdmaPhone$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$refreshSafetySources$5(str);
            }
        });
    }

    @Override // com.android.internal.telephony.Phone
    public SmsDispatchersController getSmsDispatchersController() {
        return this.mIccSmsInterfaceManager.mDispatchersController;
    }
}
