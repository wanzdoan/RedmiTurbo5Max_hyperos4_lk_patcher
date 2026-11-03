package com.android.internal.telephony.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.TelephonyNetworkSpecifier;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.ims.RegistrationManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.SparseIntArray;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.ISetOpportunisticDataCallback;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RadioConfig;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.nano.TelephonyProto$TelephonyEvent;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class PhoneSwitcher extends Handler {
    public static final int EVENT_IMS_RADIO_TECH_CHANGED = 120;
    protected static final int EVENT_MODEM_COMMAND_DONE = 112;
    protected static final int EVENT_MODEM_COMMAND_RETRY = 113;
    protected static final int EVENT_OEM_HOOK_SERVICE_READY = 123;
    public static final int EVENT_PRECISE_CALL_STATE_CHANGED = 109;
    protected static final int EVENT_RADIO_ON = 108;
    protected static final int EVENT_REMOVE_DDS_EMERGENCY_OVERRIDE = 116;
    protected static final int EVENT_SUB_INFO_READY = 124;
    protected static final int EVENT_UNSOL_MAX_DATA_ALLOWED_CHANGED = 122;
    protected static final int EVENT_VOICE_CALL_ENDED = 121;
    protected static final int HAL_COMMAND_ALLOW_DATA = 1;
    protected static final int HAL_COMMAND_PREFERRED_DATA = 2;
    protected static final int HAL_COMMAND_UNKNOWN = 0;
    protected static final int MODEM_COMMAND_RETRY_PERIOD_MS = 5000;
    protected static final boolean REQUESTS_CHANGED = true;
    protected static final boolean REQUESTS_UNCHANGED = false;
    protected int mActiveModemCount;
    protected final RegistrantList mActivePhoneRegistrants;
    private final AutoDataSwitchController.AutoDataSwitchControllerCallback mAutoDataSwitchCallback;
    protected final AutoDataSwitchController mAutoDataSwitchController;
    protected int mAutoSelectedDataSubId;
    private final ConnectivityManager mConnectivityManager;
    protected final Context mContext;
    protected final List<Set<CommandException.Error>> mCurrentDdsSwitchFailure;
    private final Map mDataSettingsManagerCallbacks;
    private final DefaultNetworkCallback mDefaultNetworkCallback;
    protected EmergencyOverrideRequest mEmergencyOverride;
    protected final FeatureFlags mFlags;
    protected int mHalCommandToUse;
    public ImsRegTechProvider mImsRegTechProvider;
    public ImsRegisterCallback mImsRegisterCallback;
    public final SparseIntArray mImsRegistrationRadioTechMap;
    private boolean mIsRegisteredForImsRadioTechChange;
    protected int mLastSwitchPreferredDataReason;
    private final LocalLog mLocalLog;
    protected int mMaxDataAttachModemCount;
    private final DataNetworkController.NetworkRequestList mNetworkRequestList;
    private int mOpportunisticSetDataSubId;
    private boolean mPendingSwitchNeedValidation;
    private int mPendingSwitchSubId;
    protected int mPhoneIdInVoiceCall;
    protected PhoneState[] mPhoneStates;
    protected int[] mPhoneSubscriptions;
    protected final Set<PhoneSwitcherCallback> mPhoneSwitcherCallbacks;
    protected int mPreferredDataPhoneId;
    protected SubscriptionManagerService.WatchedInt mPreferredDataSubId;
    protected int mPrimaryDataSubId;
    protected RadioConfig mRadioConfig;
    private ISetOpportunisticDataCallback mSetOpptSubCallback;
    private final BroadcastReceiver mSimStateIntentReceiver;
    protected final SubscriptionManagerService mSubscriptionManagerService;
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangedListener;
    public final CellularNetworkValidator.ValidationCallback mValidationCallback;
    protected final CellularNetworkValidator mValidator;
    protected static final String LOG_TAG = "PhoneSwitcher";
    protected static final boolean VDBG = Rlog.isLoggable(LOG_TAG, 2);
    private static final boolean isTeleOptimizationSupport = SystemProperties.getBoolean("persist.vendor.radio.sim.allfile", false);
    public static int ECBM_DEFAULT_DATA_SWITCH_BASE_TIME_MS = 5000;
    public static int DEFAULT_DATA_OVERRIDE_TIMEOUT_MS = 5000;
    protected static PhoneSwitcher sPhoneSwitcher = null;

    public interface ImsRegTechProvider {
        int get(Context context, int i);
    }

    public interface ImsRegisterCallback {
        void setCallback(Context context, int i, RegistrationManager.RegistrationCallback registrationCallback, Executor executor) throws ImsException;
    }

    protected void handlePrimaryDataSubChanged(int i) {
    }

    public static final class EmergencyOverrideRequest {
        CompletableFuture mOverrideCompleteFuture;
        public int mPhoneId = -1;
        public int mGnssOverrideTimeMs = -1;
        boolean mRequiresEcmFinish = false;
        public boolean mPendingOriginatingCall = true;

        boolean isCallbackAvailable() {
            return this.mOverrideCompleteFuture != null;
        }

        public void sendOverrideCompleteCallbackResultAndClear(boolean z) {
            if (isCallbackAvailable()) {
                this.mOverrideCompleteFuture.complete(Boolean.valueOf(z));
                this.mOverrideCompleteFuture = null;
            }
        }

        public String toString() {
            return String.format("EmergencyOverrideRequest: [phoneId= %d, overrideMs= %d, hasCallback= %b, ecmFinishStatus= %b]", Integer.valueOf(this.mPhoneId), Integer.valueOf(this.mGnssOverrideTimeMs), Boolean.valueOf(isCallbackAvailable()), Boolean.valueOf(this.mRequiresEcmFinish));
        }
    }

    public static class PhoneSwitcherCallback extends DataCallback {
        public void onPreferredDataPhoneIdChanged(int i) {
        }

        public PhoneSwitcherCallback(Executor executor) {
            super(executor);
        }
    }

    private class DefaultNetworkCallback extends ConnectivityManager.NetworkCallback {
        public int mExpectedSubId;
        public int mSwitchReason;

        private DefaultNetworkCallback() {
            this.mExpectedSubId = -1;
            this.mSwitchReason = 0;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            if (networkCapabilities.hasTransport(0) && SubscriptionManager.isValidSubscriptionId(this.mExpectedSubId) && this.mExpectedSubId == PhoneSwitcher.this.getSubIdFromNetworkSpecifier(networkCapabilities.getNetworkSpecifier())) {
                PhoneSwitcher.this.logDataSwitchEvent(this.mExpectedSubId, 2, this.mSwitchReason);
                this.mExpectedSubId = -1;
                this.mSwitchReason = 0;
            }
            PhoneSwitcher.this.mAutoDataSwitchController.updateDefaultNetworkCapabilities(networkCapabilities);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            PhoneSwitcher.this.mAutoDataSwitchController.updateDefaultNetworkCapabilities(null);
        }
    }

    public static PhoneSwitcher getInstance() {
        return sPhoneSwitcher;
    }

    public static PhoneSwitcher make(int i, Context context, Looper looper, FeatureFlags featureFlags) {
        if (sPhoneSwitcher == null) {
            sPhoneSwitcher = new PhoneSwitcher(i, context, looper, featureFlags);
        }
        return sPhoneSwitcher;
    }

    protected boolean updatesIfPhoneInVoiceCallChanged() {
        int i = this.mPhoneIdInVoiceCall;
        this.mPhoneIdInVoiceCall = -1;
        onGetInVoiceCallPhoneId();
        if (this.mPhoneIdInVoiceCall == i) {
            return false;
        }
        logl("isPhoneInVoiceCallChanged from phoneId " + i + " to phoneId " + this.mPhoneIdInVoiceCall);
        return true;
    }

    public void registerCallback(PhoneSwitcherCallback phoneSwitcherCallback) {
        this.mPhoneSwitcherCallbacks.add(phoneSwitcherCallback);
    }

    public void unregisterCallback(PhoneSwitcherCallback phoneSwitcherCallback) {
        this.mPhoneSwitcherCallbacks.remove(phoneSwitcherCallback);
    }

    protected void evaluateIfImmediateDataSwitchIsNeeded(String str, int i) {
        if (onEvaluate(false, str)) {
            logDataSwitchEvent(this.mPreferredDataSubId.get(), 1, i);
            registerDefaultNetworkChangeCallback(this.mPreferredDataSubId.get(), i);
        }
    }

    public PhoneSwitcher(int i, Context context, Looper looper, FeatureFlags featureFlags) {
        int i2;
        super(looper);
        this.mNetworkRequestList = new DataNetworkController.NetworkRequestList();
        this.mPendingSwitchSubId = -1;
        this.mLastSwitchPreferredDataReason = -1;
        this.mValidationCallback = new CellularNetworkValidator.ValidationCallback() { // from class: com.android.internal.telephony.data.PhoneSwitcher.1
            @Override // com.android.internal.telephony.data.CellularNetworkValidator.ValidationCallback
            public void onValidationDone(boolean z, int i3) {
                Message.obtain(PhoneSwitcher.this, NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE, i3, z ? 1 : 0).sendToTarget();
            }

            @Override // com.android.internal.telephony.data.CellularNetworkValidator.ValidationCallback
            public void onNetworkAvailable(Network network, int i3) {
                Message.obtain(PhoneSwitcher.this, 118, i3, 0, network).sendToTarget();
            }
        };
        this.mPrimaryDataSubId = -1;
        this.mAutoSelectedDataSubId = Integer.MAX_VALUE;
        this.mOpportunisticSetDataSubId = -1;
        this.mPhoneIdInVoiceCall = -1;
        this.mPreferredDataPhoneId = -1;
        this.mPreferredDataSubId = new SubscriptionManagerService.WatchedInt(-1);
        this.mPhoneSwitcherCallbacks = new ArraySet();
        this.mHalCommandToUse = 0;
        this.mImsRegistrationRadioTechMap = new SparseIntArray();
        this.mDataSettingsManagerCallbacks = new ArrayMap();
        this.mDefaultNetworkCallback = new DefaultNetworkCallback();
        this.mImsRegTechProvider = new ImsRegTechProvider() { // from class: com.android.internal.telephony.data.PhoneSwitcher$$ExternalSyntheticLambda0
            @Override // com.android.internal.telephony.data.PhoneSwitcher.ImsRegTechProvider
            public final int get(Context context2, int i3) {
                return ImsManager.getInstance(context2, i3).getRegistrationTech();
            }
        };
        this.mImsRegisterCallback = new ImsRegisterCallback() { // from class: com.android.internal.telephony.data.PhoneSwitcher$$ExternalSyntheticLambda1
            @Override // com.android.internal.telephony.data.PhoneSwitcher.ImsRegisterCallback
            public final void setCallback(Context context2, int i3, RegistrationManager.RegistrationCallback registrationCallback, Executor executor) {
                ImsManager.getInstance(context2, i3).addRegistrationCallback(registrationCallback, executor);
            }
        };
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.data.PhoneSwitcher.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.telephony.action.SIM_APPLICATION_STATE_CHANGED")) {
                    int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
                    int intExtra2 = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
                    PhoneSwitcher.this.logl("mSimStateIntentReceiver: slotIndex = " + intExtra2 + " state = " + intExtra);
                    PhoneSwitcher.this.obtainMessage(119, intExtra2, intExtra).sendToTarget();
                }
            }
        };
        this.mSimStateIntentReceiver = broadcastReceiver;
        this.mSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.data.PhoneSwitcher.5
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                PhoneSwitcher.this.obtainMessage(102).sendToTarget();
            }
        };
        this.mContext = context;
        this.mFlags = featureFlags;
        int activeModemCount = getTm().getActiveModemCount();
        this.mActiveModemCount = activeModemCount;
        this.mPhoneSubscriptions = new int[activeModemCount];
        this.mPhoneStates = new PhoneState[activeModemCount];
        this.mMaxDataAttachModemCount = i;
        this.mLocalLog = new LocalLog(CallFailCause.RADIO_UPLINK_FAILURE);
        this.mSubscriptionManagerService = SubscriptionManagerService.getInstance();
        this.mRadioConfig = RadioConfig.getInstance();
        this.mValidator = CellularNetworkValidator.getInstance();
        this.mCurrentDdsSwitchFailure = new ArrayList();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        context.registerReceiver(broadcastReceiver, intentFilter);
        this.mActivePhoneRegistrants = new RegistrantList();
        final int i3 = 0;
        while (true) {
            i2 = this.mActiveModemCount;
            if (i3 >= i2) {
                break;
            }
            this.mPhoneStates[i3] = new PhoneState();
            Phone phone = PhoneFactory.getPhone(i3);
            if (phone != null) {
                phone.registerForEmergencyCallToggle(this, NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION, null);
                phone.registerForPreciseCallStateChanged(this, 109, null);
                if (phone.getImsPhone() != null) {
                    phone.getImsPhone().registerForPreciseCallStateChanged(this, 109, null);
                    this.mImsRegistrationRadioTechMap.put(i3, -1);
                    ((ImsPhone) phone.getImsPhone()).registerForImsRegistrationChanges(this, 120, null);
                    log("register handler to receive IMS registration : " + i3);
                }
                this.mDataSettingsManagerCallbacks.computeIfAbsent(Integer.valueOf(i3), new Function() { // from class: com.android.internal.telephony.data.PhoneSwitcher$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return this.f$0.lambda$new$2(i3, (Integer) obj);
                    }
                });
                phone.getDataSettingsManager().registerCallback((DataSettingsManager.DataSettingsManagerCallback) this.mDataSettingsManagerCallbacks.get(Integer.valueOf(i3)));
            }
            this.mCurrentDdsSwitchFailure.add(new HashSet());
            i3++;
        }
        if (i2 > 0) {
            PhoneFactory.getPhone(0).mCi.registerForOn(this, 108, null);
        }
        ((TelephonyRegistryManager) context.getSystemService("telephony_registry")).addOnSubscriptionsChangedListener(this.mSubscriptionsChangedListener, new PhoneSwitcher$$ExternalSyntheticLambda3(this));
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.mConnectivityManager = connectivityManager;
        AutoDataSwitchController.AutoDataSwitchControllerCallback autoDataSwitchControllerCallback = new AutoDataSwitchController.AutoDataSwitchControllerCallback() { // from class: com.android.internal.telephony.data.PhoneSwitcher.2
            @Override // com.android.internal.telephony.data.AutoDataSwitchController.AutoDataSwitchControllerCallback
            public void onRequireValidation(int i4, boolean z) {
                PhoneSwitcher.this.validate(i4 != Integer.MAX_VALUE ? PhoneSwitcher.this.mSubscriptionManagerService.getSubId(i4) : Integer.MAX_VALUE, z, 4, null);
            }

            @Override // com.android.internal.telephony.data.AutoDataSwitchController.AutoDataSwitchControllerCallback
            public void onRequireImmediatelySwitchToPhone(int i4, int i5) {
                PhoneSwitcher phoneSwitcher = PhoneSwitcher.this;
                phoneSwitcher.mAutoSelectedDataSubId = i4 != Integer.MAX_VALUE ? phoneSwitcher.mSubscriptionManagerService.getSubId(i4) : Integer.MAX_VALUE;
                PhoneSwitcher.this.evaluateIfImmediateDataSwitchIsNeeded(AutoDataSwitchController.evaluationReasonToString(i5), 1);
            }

            @Override // com.android.internal.telephony.data.AutoDataSwitchController.AutoDataSwitchControllerCallback
            public void onRequireCancelAnyPendingAutoSwitchValidation() {
                PhoneSwitcher.this.cancelPendingAutoDataSwitchValidation();
            }
        };
        this.mAutoDataSwitchCallback = autoDataSwitchControllerCallback;
        this.mAutoDataSwitchController = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeAutoDataSwitchController(context, looper, this, this.mFlags, autoDataSwitchControllerCallback);
        this.mSubscriptionManagerService.registerCallback(new SubscriptionManagerService.SubscriptionManagerServiceCallback(new PhoneSwitcher$$ExternalSyntheticLambda3(this)) { // from class: com.android.internal.telephony.data.PhoneSwitcher.3
            @Override // com.android.internal.telephony.subscription.SubscriptionManagerService.SubscriptionManagerServiceCallback
            public void onDefaultDataSubscriptionChanged(int i4) {
                PhoneSwitcher.this.evaluateIfImmediateDataSwitchIsNeeded("default data sub changed to " + i4, 1);
            }
        });
        PhoneConfigurationManager.registerForMultiSimConfigChange(this, 117, null);
        connectivityManager.registerDefaultNetworkCallback(this.mDefaultNetworkCallback, this);
        updateHalCommandToUse();
        MiuiPhoneSwitcherStub.get().init(context, this);
        logl("PhoneSwitcher started");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ DataSettingsManager.DataSettingsManagerCallback lambda$new$2(int i, Integer num) {
        return createDataSettingsManagerCallback(i);
    }

    protected boolean isSimApplicationReady(int i) {
        if (!SubscriptionManager.isValidSlotIndex(i)) {
            return false;
        }
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = this.mSubscriptionManagerService.getActiveSubscriptionInfoForSimSlotIndex(i, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        boolean z = activeSubscriptionInfoForSimSlotIndex != null && activeSubscriptionInfoForSimSlotIndex.areUiccApplicationsEnabled();
        if (PhoneFactory.getPhone(i).getIccCard().isEmptyProfile() || !z) {
            return false;
        }
        logl("isSimApplicationReady: SIM is ready for slotIndex: " + i);
        return true;
    }

    /* JADX WARN: Code duplicated, block: B:42:0x0129  */
    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 101:
                evaluateIfImmediateDataSwitchIsNeeded("primary data sub changed", 1);
                break;
            case 102:
                onEvaluate(false, "subscription changed");
                break;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION /* 105 */:
                boolean zIsInEmergencyCallbackMode = isInEmergencyCallbackMode();
                if (this.mEmergencyOverride != null) {
                    logl("Emergency override - ecbm status = " + zIsInEmergencyCallbackMode);
                    if (zIsInEmergencyCallbackMode) {
                        removeMessages(116);
                        this.mEmergencyOverride.mRequiresEcmFinish = true;
                    } else if (this.mEmergencyOverride.mRequiresEcmFinish) {
                        sendMessageDelayed(obtainMessage(116), this.mEmergencyOverride.mGnssOverrideTimeMs);
                    }
                }
                onEvaluate(true, "emergencyToggle");
                break;
            case 106:
                sendRilCommands(message.arg1);
                break;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS /* 107 */:
                setOpportunisticDataSubscription(message.arg1, message.arg2 == 1, (ISetOpportunisticDataCallback) message.obj);
                break;
            case 108:
                updateHalCommandToUse();
                onEvaluate(false, "EVENT_RADIO_ON");
                break;
            case 109:
                if (updatesIfPhoneInVoiceCallChanged()) {
                    if (!isAnyVoiceCallActiveOnDevice()) {
                        for (int i = 0; i < this.mActiveModemCount; i++) {
                            if (this.mCurrentDdsSwitchFailure.get(i).contains(CommandException.Error.OP_NOT_ALLOWED_DURING_VOICE_CALL) && isPhoneIdValidForRetry(i)) {
                                sendRilCommands(i);
                            }
                        }
                    }
                    EmergencyOverrideRequest emergencyOverrideRequest = this.mEmergencyOverride;
                    if (emergencyOverrideRequest != null && emergencyOverrideRequest.mPendingOriginatingCall) {
                        removeMessages(116);
                        if (this.mPhoneIdInVoiceCall == -1) {
                            sendMessageDelayed(obtainMessage(116), this.mEmergencyOverride.mGnssOverrideTimeMs + ECBM_DEFAULT_DATA_SWITCH_BASE_TIME_MS);
                            this.mEmergencyOverride.mPendingOriginatingCall = false;
                        }
                    }
                    evaluateIfImmediateDataSwitchIsNeeded("precise call state changed", 2);
                    if (!isAnyVoiceCallActiveOnDevice()) {
                        this.mAutoDataSwitchController.evaluateAutoDataSwitch(8);
                    }
                }
                break;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE /* 110 */:
                onValidationDone(message.arg1, message.arg2 == 1);
                break;
            case 112:
                onDdsSwitchResponse((AsyncResult) message.obj);
                break;
            case 113:
                int iIntValue = ((Integer) message.obj).intValue();
                if (this.mActiveModemCount > iIntValue) {
                    if (isPhoneIdValidForRetry(iIntValue)) {
                        logl("EVENT_MODEM_COMMAND_RETRY: resend modem command on phone " + iIntValue);
                        sendRilCommands(iIntValue);
                    } else {
                        logl("EVENT_MODEM_COMMAND_RETRY: skip retry as DDS sub changed");
                        this.mCurrentDdsSwitchFailure.get(iIntValue).clear();
                    }
                }
                break;
            case 115:
                EmergencyOverrideRequest emergencyOverrideRequest2 = (EmergencyOverrideRequest) message.obj;
                EmergencyOverrideRequest emergencyOverrideRequest3 = this.mEmergencyOverride;
                if (emergencyOverrideRequest3 != null) {
                    if (emergencyOverrideRequest3.mPhoneId != emergencyOverrideRequest2.mPhoneId) {
                        logl("emergency override requested for phone id " + emergencyOverrideRequest2.mPhoneId + " when there is already an override in place for phone id " + this.mEmergencyOverride.mPhoneId + ". Ignoring.");
                        if (emergencyOverrideRequest2.isCallbackAvailable()) {
                            emergencyOverrideRequest2.mOverrideCompleteFuture.complete(Boolean.FALSE);
                        }
                    } else {
                        if (emergencyOverrideRequest3.isCallbackAvailable()) {
                            this.mEmergencyOverride.mOverrideCompleteFuture.complete(Boolean.FALSE);
                        }
                        this.mEmergencyOverride = emergencyOverrideRequest2;
                        logl("new emergency override - " + this.mEmergencyOverride);
                        removeMessages(116);
                        sendMessageDelayed(obtainMessage(116), (long) DEFAULT_DATA_OVERRIDE_TIMEOUT_MS);
                        if (!onEvaluate(false, "emer_override_dds")) {
                            this.mEmergencyOverride.sendOverrideCompleteCallbackResultAndClear(true);
                        }
                    }
                } else {
                    this.mEmergencyOverride = emergencyOverrideRequest2;
                    logl("new emergency override - " + this.mEmergencyOverride);
                    removeMessages(116);
                    sendMessageDelayed(obtainMessage(116), (long) DEFAULT_DATA_OVERRIDE_TIMEOUT_MS);
                    if (!onEvaluate(false, "emer_override_dds")) {
                        this.mEmergencyOverride.sendOverrideCompleteCallbackResultAndClear(true);
                    }
                }
                break;
            case 116:
                logl("Emergency override removed - " + this.mEmergencyOverride);
                this.mEmergencyOverride = null;
                onEvaluate(false, "emer_rm_override_dds");
                break;
            case 117:
                onMultiSimConfigChanged(((Integer) ((AsyncResult) message.obj).result).intValue());
                break;
            case 118:
                onNetworkAvailable(message.arg1, (Network) message.obj);
                break;
            case 119:
                int i2 = message.arg1;
                int i3 = message.arg2;
                if (!SubscriptionManager.isValidSlotIndex(i2)) {
                    logl("EVENT_PROCESS_SIM_STATE_CHANGE: skip processing due to invalid slotId: " + i2);
                } else if (10 == i3) {
                    if (this.mCurrentDdsSwitchFailure.get(i2).contains(CommandException.Error.INVALID_SIM_STATE) && isSimApplicationReady(i2)) {
                        sendRilCommands(i2);
                    }
                    this.mAutoDataSwitchController.evaluateAutoDataSwitch(7);
                }
                break;
            case 120:
                Object obj = message.obj;
                if (obj == null) {
                    log("EVENT_IMS_RADIO_TECH_CHANGED but parameter is not available");
                } else if (onImsRadioTechChanged((AsyncResult) obj) && (updatesIfPhoneInVoiceCallChanged() || isAnyVoiceCallActiveOnDevice())) {
                    evaluateIfImmediateDataSwitchIsNeeded("Ims radio tech changed", 2);
                }
                break;
        }
        MiuiPhoneSwitcherStub.get().handleMessage(message);
    }

    protected boolean onImsRadioTechChanged(AsyncResult asyncResult) {
        ImsPhone.ImsRegistrationRadioTechInfo imsRegistrationRadioTechInfo = (ImsPhone.ImsRegistrationRadioTechInfo) asyncResult.result;
        if (imsRegistrationRadioTechInfo == null || imsRegistrationRadioTechInfo.phoneId() == -1 || imsRegistrationRadioTechInfo.imsRegistrationState() == 1) {
            log("onImsRadioTechChanged : result is not available");
            return false;
        }
        int iPhoneId = imsRegistrationRadioTechInfo.phoneId();
        int subscriptionId = SubscriptionManager.getSubscriptionId(iPhoneId);
        int iImsRegistrationTech = imsRegistrationRadioTechInfo.imsRegistrationTech();
        log("onImsRadioTechChanged phoneId : " + iPhoneId + " subId : " + subscriptionId + " old tech : " + this.mImsRegistrationRadioTechMap.get(iPhoneId, -1) + " new tech : " + iImsRegistrationTech);
        if (this.mImsRegistrationRadioTechMap.get(iPhoneId, -1) == iImsRegistrationTech) {
            return false;
        }
        this.mImsRegistrationRadioTechMap.put(iPhoneId, iImsRegistrationTech);
        return subscriptionId != -1;
    }

    private synchronized void onMultiSimConfigChanged(int i) {
        try {
            final int i2 = this.mActiveModemCount;
            if (i2 == i) {
                return;
            }
            this.mActiveModemCount = i;
            this.mPhoneSubscriptions = Arrays.copyOf(this.mPhoneSubscriptions, i);
            this.mPhoneStates = (PhoneState[]) Arrays.copyOf(this.mPhoneStates, this.mActiveModemCount);
            for (int i3 = i2 - 1; i3 >= this.mActiveModemCount; i3--) {
                this.mCurrentDdsSwitchFailure.remove(i3);
            }
            while (i2 < this.mActiveModemCount) {
                this.mPhoneStates[i2] = new PhoneState();
                Phone phone = PhoneFactory.getPhone(i2);
                if (phone != null) {
                    phone.registerForEmergencyCallToggle(this, NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION, null);
                    phone.registerForPreciseCallStateChanged(this, 109, null);
                    if (phone.getImsPhone() != null) {
                        phone.getImsPhone().registerForPreciseCallStateChanged(this, 109, null);
                        this.mImsRegistrationRadioTechMap.put(i2, -1);
                        ((ImsPhone) phone.getImsPhone()).registerForImsRegistrationChanges(this, 120, null);
                        log("register handler to receive IMS registration : " + i2);
                    }
                    this.mDataSettingsManagerCallbacks.computeIfAbsent(Integer.valueOf(i2), new Function() { // from class: com.android.internal.telephony.data.PhoneSwitcher$$ExternalSyntheticLambda4
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return this.f$0.lambda$onMultiSimConfigChanged$3(i2, (Integer) obj);
                        }
                    });
                    phone.getDataSettingsManager().registerCallback((DataSettingsManager.DataSettingsManagerCallback) this.mDataSettingsManagerCallbacks.get(Integer.valueOf(i2)));
                    this.mCurrentDdsSwitchFailure.add(new HashSet());
                }
                i2++;
            }
            this.mAutoDataSwitchController.onMultiSimConfigChanged(i);
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ DataSettingsManager.DataSettingsManagerCallback lambda$onMultiSimConfigChanged$3(int i, Integer num) {
        return createDataSettingsManagerCallback(i);
    }

    private DataSettingsManager.DataSettingsManagerCallback createDataSettingsManagerCallback(final int i) {
        return new DataSettingsManager.DataSettingsManagerCallback(new PhoneSwitcher$$ExternalSyntheticLambda3(this)) { // from class: com.android.internal.telephony.data.PhoneSwitcher.6
            @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
            public void onDataEnabledChanged(boolean z, int i2, String str) {
                PhoneSwitcher.this.onDataEnabledChanged();
            }

            @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
            public void onDataRoamingEnabledChanged(boolean z) {
                PhoneSwitcher.this.mAutoDataSwitchController.evaluateAutoDataSwitch(5);
            }

            @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
            public void onDataEnabledOverrideChanged(boolean z, int i2) {
                PhoneSwitcher.this.onDataEnabledOverrideChanged(i, z, i2);
            }
        };
    }

    protected void onDataEnabledOverrideChanged(int i, boolean z, int i2) {
        if (isStandaloneOpportunistic(i) && i2 == 3) {
            logl("onDataEnabledOverrideChanged: ADS policy changed, phoneId=" + i + " enabled=" + z);
            onDataEnabledChanged();
        }
    }

    private boolean isStandaloneOpportunistic(int i) {
        SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(SubscriptionManager.getSubscriptionId(i));
        return subscriptionInfo != null && subscriptionInfo.isOpportunistic() && subscriptionInfo.getGroupUuid() == null;
    }

    protected void onDataEnabledChanged() {
        if (isAnyVoiceCallActiveOnDevice()) {
            evaluateIfImmediateDataSwitchIsNeeded("user changed data settings during call", 2);
        } else {
            this.mAutoDataSwitchController.evaluateAutoDataSwitch(5);
        }
    }

    private boolean isInEmergencyCallbackMode() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone != null) {
                if (phone.isInEcm()) {
                    return true;
                }
                Phone imsPhone = phone.getImsPhone();
                if (imsPhone != null && imsPhone.isInEcm()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInEmergencyMode() {
        if (isInEmergencyCallbackMode()) {
            return true;
        }
        return DomainSelectionResolver.getInstance().isDomainSelectionSupported() && EmergencyStateTracker.getInstance().isInEmergencyMode() && !isAnyVoiceCallActiveOnDevice();
    }

    public void onRequestNetwork(TelephonyNetworkRequest telephonyNetworkRequest) {
        if (this.mNetworkRequestList.contains(telephonyNetworkRequest)) {
            return;
        }
        this.mNetworkRequestList.add(telephonyNetworkRequest);
        onEvaluate(true, "netRequest");
    }

    public void onReleaseNetwork(TelephonyNetworkRequest telephonyNetworkRequest) {
        if (this.mNetworkRequestList.remove(telephonyNetworkRequest)) {
            onEvaluate(true, "netReleased");
        }
    }

    private void registerDefaultNetworkChangeCallback(int i, int i2) {
        DefaultNetworkCallback defaultNetworkCallback = this.mDefaultNetworkCallback;
        defaultNetworkCallback.mExpectedSubId = i;
        defaultNetworkCallback.mSwitchReason = i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPendingAutoDataSwitchValidation() {
        if (this.mValidator.isValidating()) {
            this.mValidator.stopValidation();
            removeMessages(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE);
            removeMessages(118);
            this.mPendingSwitchSubId = -1;
            this.mPendingSwitchNeedValidation = false;
        }
    }

    private TelephonyManager getTm() {
        return (TelephonyManager) this.mContext.getSystemService("phone");
    }

    protected boolean onEvaluate(boolean z, String str) {
        StringBuilder sb = new StringBuilder(str);
        int i = 0;
        boolean z2 = this.mHalCommandToUse != 2 && z;
        int defaultDataSubId = this.mSubscriptionManagerService.getDefaultDataSubId();
        if (defaultDataSubId != this.mPrimaryDataSubId) {
            sb.append(" mPrimaryDataSubId ");
            sb.append(this.mPrimaryDataSubId);
            sb.append("->");
            sb.append(defaultDataSubId);
            this.mPrimaryDataSubId = defaultDataSubId;
            this.mLastSwitchPreferredDataReason = 1;
            handlePrimaryDataSubChanged(defaultDataSubId);
        }
        boolean z3 = false;
        for (int i2 = 0; i2 < this.mActiveModemCount; i2++) {
            int subscriptionId = SubscriptionManager.getSubscriptionId(i2);
            if (SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
                z3 = true;
            }
            if (subscriptionId != this.mPhoneSubscriptions[i2]) {
                sb.append(" phone[");
                sb.append(i2);
                sb.append("] ");
                sb.append(this.mPhoneSubscriptions[i2]);
                sb.append("->");
                sb.append(subscriptionId);
                if (this.mAutoSelectedDataSubId == this.mPhoneSubscriptions[i2]) {
                    this.mAutoSelectedDataSubId = Integer.MAX_VALUE;
                }
                int i3 = this.mOpportunisticSetDataSubId;
                if (i3 != Integer.MAX_VALUE && this.mSubscriptionManagerService.getSubscriptionInfoInternal(i3) == null) {
                    sb.append(" Opportunistic set data sub is gone, reset to default");
                    this.mOpportunisticSetDataSubId = Integer.MAX_VALUE;
                }
                this.mPhoneSubscriptions[i2] = subscriptionId;
                this.mAutoDataSwitchController.notifySubscriptionsMappingChanged();
                z2 = true;
            }
        }
        if (!z3) {
            transitionToEmergencyPhone();
        } else if (VDBG) {
            log("Found an active subscription");
        }
        int i4 = this.mPreferredDataPhoneId;
        int i5 = this.mPreferredDataSubId.get();
        if (z3) {
            updatePreferredDataPhoneId();
        }
        if (i4 != this.mPreferredDataPhoneId) {
            sb.append(" preferred data phoneId ");
            sb.append(i4);
            sb.append("->");
            sb.append(this.mPreferredDataPhoneId);
            z2 = true;
        } else if (i5 != this.mPreferredDataSubId.get()) {
            logl("SIM refresh, notify dds change");
            notifyPreferredDataSubIdChanged();
        }
        if (z2 || "EVENT_RADIO_ON".equals(str)) {
            logl("evaluating due to " + ((Object) sb));
            if (this.mHalCommandToUse == 2) {
                while (i < this.mActiveModemCount) {
                    this.mPhoneStates[i].active = true;
                    i++;
                }
                sendRilCommands(this.mPreferredDataPhoneId);
                return z2;
            }
            ArrayList arrayList = new ArrayList();
            if (this.mMaxDataAttachModemCount == this.mActiveModemCount) {
                for (int i6 = 0; i6 < this.mMaxDataAttachModemCount; i6++) {
                    arrayList.add(Integer.valueOf(i6));
                }
            } else {
                int i7 = this.mPhoneIdInVoiceCall;
                if (i7 != -1) {
                    arrayList.add(Integer.valueOf(i7));
                }
                if (arrayList.size() < this.mMaxDataAttachModemCount) {
                    Iterator<TelephonyNetworkRequest> it = this.mNetworkRequestList.iterator();
                    while (it.hasNext()) {
                        int iPhoneIdForRequest = phoneIdForRequest(it.next());
                        if (iPhoneIdForRequest != -1 && !arrayList.contains(Integer.valueOf(iPhoneIdForRequest))) {
                            arrayList.add(Integer.valueOf(iPhoneIdForRequest));
                            if (arrayList.size() >= this.mMaxDataAttachModemCount) {
                                break;
                            }
                        }
                    }
                }
                if (arrayList.size() < this.mMaxDataAttachModemCount && !arrayList.contains(Integer.valueOf(this.mPreferredDataPhoneId)) && SubscriptionManager.isUsableSubIdValue(this.mPreferredDataPhoneId)) {
                    arrayList.add(Integer.valueOf(this.mPreferredDataPhoneId));
                }
            }
            if (VDBG) {
                log("mPrimaryDataSubId = " + this.mPrimaryDataSubId);
                log("mAutoSelectedDataSubId = " + this.mAutoSelectedDataSubId);
                for (int i8 = 0; i8 < this.mActiveModemCount; i8++) {
                    log(" phone[" + i8 + "] using sub[" + this.mPhoneSubscriptions[i8] + "]");
                }
                log(" newActivePhones:");
                int size = arrayList.size();
                int i9 = 0;
                while (i9 < size) {
                    Object obj = arrayList.get(i9);
                    i9++;
                    log("  " + ((Integer) obj));
                }
            }
            for (int i10 = 0; i10 < this.mActiveModemCount; i10++) {
                if (!arrayList.contains(Integer.valueOf(i10))) {
                    deactivate(i10);
                }
            }
            int size2 = arrayList.size();
            while (i < size2) {
                Object obj2 = arrayList.get(i);
                i++;
                activate(((Integer) obj2).intValue());
            }
        }
        return z2;
    }

    protected static class PhoneState {
        public volatile boolean active = false;
        public long lastRequested = 0;

        protected PhoneState() {
        }
    }

    protected void activate(int i) {
        switchPhone(i, true);
    }

    protected void deactivate(int i) {
        switchPhone(i, false);
    }

    private void switchPhone(int i, boolean z) {
        PhoneState phoneState = this.mPhoneStates[i];
        if (phoneState.active == z) {
            return;
        }
        phoneState.active = z;
        StringBuilder sb = new StringBuilder();
        sb.append(z ? "activate " : "deactivate ");
        sb.append(i);
        logl(sb.toString());
        phoneState.lastRequested = System.currentTimeMillis();
        sendRilCommands(i);
    }

    public void onRadioCapChanged(int i) {
        if (SubscriptionManager.isValidPhoneId(i)) {
            Message messageObtainMessage = obtainMessage(106);
            messageObtainMessage.arg1 = i;
            messageObtainMessage.sendToTarget();
        }
    }

    public void overrideDefaultDataForEmergency(int i, int i2, CompletableFuture<Boolean> completableFuture) {
        if (SubscriptionManager.isValidPhoneId(i)) {
            Message messageObtainMessage = obtainMessage(115);
            EmergencyOverrideRequest emergencyOverrideRequest = new EmergencyOverrideRequest();
            emergencyOverrideRequest.mPhoneId = i;
            emergencyOverrideRequest.mGnssOverrideTimeMs = i2 * 1000;
            emergencyOverrideRequest.mOverrideCompleteFuture = completableFuture;
            messageObtainMessage.obj = emergencyOverrideRequest;
            messageObtainMessage.sendToTarget();
        }
    }

    protected void sendRilCommands(int i) {
        if (!SubscriptionManager.isValidPhoneId(i)) {
            logl("sendRilCommands: skip dds switch due to invalid phoneId=" + i);
            return;
        }
        Message messageObtain = Message.obtain(this, 112, Integer.valueOf(i));
        int i2 = this.mHalCommandToUse;
        if (i2 == 1 || i2 == 0) {
            if (this.mActiveModemCount > 1) {
                PhoneFactory.getPhone(i).mCi.setDataAllowed(isPhoneActive(i), messageObtain);
            }
        } else if (i == this.mPreferredDataPhoneId) {
            logl("sendRilCommands: setPreferredDataModem - phoneId: " + i);
            this.mRadioConfig.setPreferredDataModem(this.mPreferredDataPhoneId, messageObtain);
        }
    }

    protected int phoneIdForRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        NetworkRequest nativeNetworkRequest = telephonyNetworkRequest.getNativeNetworkRequest();
        int subIdFromNetworkSpecifier = getSubIdFromNetworkSpecifier(nativeNetworkRequest.getNetworkSpecifier());
        if (subIdFromNetworkSpecifier == Integer.MAX_VALUE) {
            return this.mPreferredDataPhoneId;
        }
        if (subIdFromNetworkSpecifier == -1) {
            return -1;
        }
        int i = this.mPreferredDataPhoneId;
        int i2 = (i < 0 || i >= this.mActiveModemCount) ? -1 : this.mPhoneSubscriptions[i];
        boolean z = SystemProperties.getInt("ril.multisim.dualdata_status", 0) == 1;
        log("phoneIdForRequest: isDD" + z);
        if (nativeNetworkRequest.hasCapability(12) && !z && nativeNetworkRequest.hasCapability(13) && subIdFromNetworkSpecifier != i2 && subIdFromNetworkSpecifier != this.mValidator.getSubIdInValidation()) {
            return -1;
        }
        for (int i3 = 0; i3 < this.mActiveModemCount; i3++) {
            if (this.mPhoneSubscriptions[i3] == subIdFromNetworkSpecifier) {
                return i3;
            }
        }
        return -1;
    }

    protected int getSubIdFromNetworkSpecifier(NetworkSpecifier networkSpecifier) {
        if (networkSpecifier == null) {
            return Integer.MAX_VALUE;
        }
        if (networkSpecifier instanceof TelephonyNetworkSpecifier) {
            return ((TelephonyNetworkSpecifier) networkSpecifier).getSubscriptionId();
        }
        return -1;
    }

    private boolean isActiveSubId(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
        return subscriptionInfoInternal != null && subscriptionInfoInternal.isActive();
    }

    protected void updatePreferredDataPhoneId() {
        int i;
        if (MiuiPhoneSwitcherStub.get().updatePreferredDataPhoneId()) {
            return;
        }
        EmergencyOverrideRequest emergencyOverrideRequest = this.mEmergencyOverride;
        if (emergencyOverrideRequest != null && findPhoneById(emergencyOverrideRequest.mPhoneId) != null) {
            logl("updatePreferredDataPhoneId: preferred data overridden for emergency. phoneId = " + this.mEmergencyOverride.mPhoneId);
            this.mPreferredDataPhoneId = this.mEmergencyOverride.mPhoneId;
            this.mLastSwitchPreferredDataReason = 0;
        } else {
            if (isInEmergencyMode()) {
                logl("updatePreferredDataPhoneId: in emergency mode, skip switching data");
                return;
            }
            if (!isAnyVoiceCallActiveOnDevice() || (i = this.mImsRegTechProvider.get(this.mContext, this.mPhoneIdInVoiceCall)) == 1) {
                this.mPreferredDataPhoneId = getFallbackDataPhoneIdForInternetRequests();
            } else if (i != 2) {
                this.mPreferredDataPhoneId = shouldSwitchDataDueToInCall() ? this.mPhoneIdInVoiceCall : getFallbackDataPhoneIdForInternetRequests();
            } else {
                logl("updatePreferredDataPhoneId: cross-SIM call, skipping switching data to phone " + this.mPhoneIdInVoiceCall);
            }
        }
        this.mPreferredDataSubId.set(SubscriptionManager.getSubscriptionId(this.mPreferredDataPhoneId));
    }

    protected int getFallbackDataPhoneIdForInternetRequests() {
        int i = isActiveSubId(this.mAutoSelectedDataSubId) ? this.mAutoSelectedDataSubId : this.mPrimaryDataSubId;
        if (!SubscriptionManager.isUsableSubIdValue(i)) {
            return -1;
        }
        for (int i2 = 0; i2 < this.mActiveModemCount; i2++) {
            if (this.mPhoneSubscriptions[i2] == i) {
                return i2;
            }
        }
        return -1;
    }

    protected boolean shouldSwitchDataDueToInCall() {
        Phone phoneFindPhoneById = findPhoneById(this.mPhoneIdInVoiceCall);
        Phone phoneBySubId = getPhoneBySubId(this.mPrimaryDataSubId);
        return phoneBySubId != null && phoneBySubId.isUserDataEnabled() && phoneFindPhoneById != null && phoneFindPhoneById.getDataSettingsManager().isDataEnabled();
    }

    protected void transitionToEmergencyPhone() {
        if (this.mActiveModemCount <= 0) {
            logl("No phones: unable to reset preferred phone for emergency");
            return;
        }
        if (this.mPreferredDataPhoneId != 0) {
            logl("No active subscriptions: resetting preferred phone to 0 for emergency");
            this.mPreferredDataPhoneId = 0;
        }
        if (this.mPreferredDataSubId.get() != -1) {
            this.mPreferredDataSubId.set(-1);
            notifyPreferredDataSubIdChanged();
        }
    }

    protected Phone getPhoneBySubId(int i) {
        return findPhoneById(this.mSubscriptionManagerService.getPhoneId(i));
    }

    protected Phone findPhoneById(int i) {
        if (SubscriptionManager.isValidPhoneId(i)) {
            return PhoneFactory.getPhone(i);
        }
        return null;
    }

    public synchronized boolean shouldApplyNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest, int i) {
        try {
            if (!SubscriptionManager.isValidPhoneId(i)) {
                return false;
            }
            int subscriptionId = SubscriptionManager.getSubscriptionId(i);
            if (isPhoneActive(i) && (subscriptionId != -1 || isEmergencyNetworkRequest(telephonyNetworkRequest))) {
                int subIdFromNetworkSpecifier = getSubIdFromNetworkSpecifier(telephonyNetworkRequest.getNativeNetworkRequest().getNetworkSpecifier());
                if (isAnyVoiceCallActiveOnDevice() && isEmergencyNetworkRequest(telephonyNetworkRequest) && (subIdFromNetworkSpecifier == Integer.MAX_VALUE || subIdFromNetworkSpecifier == -1)) {
                    return i == this.mPhoneIdInVoiceCall;
                }
                return i == phoneIdForRequest(telephonyNetworkRequest);
            }
            return false;
        } catch (Throwable th) {
            throw th;
        }
    }

    boolean isEmergencyNetworkRequest(TelephonyNetworkRequest telephonyNetworkRequest) {
        return telephonyNetworkRequest.hasCapability(10);
    }

    protected boolean isPhoneActive(int i) {
        if (i >= this.mActiveModemCount) {
            return false;
        }
        return this.mPhoneStates[i].active;
    }

    private void setOpportunisticDataSubscription(int i, boolean z, ISetOpportunisticDataCallback iSetOpportunisticDataCallback) {
        validate(i, z, 3, iSetOpportunisticDataCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void validate(int i, boolean z, int i2, ISetOpportunisticDataCallback iSetOpportunisticDataCallback) {
        int i3 = i == Integer.MAX_VALUE ? this.mPrimaryDataSubId : i;
        logl("Validate subId " + i + " due to " + switchReasonToString(i2) + " needValidation=" + z + " subIdToValidate=" + i3 + " mAutoSelectedDataSubId=" + this.mAutoSelectedDataSubId + " mPreferredDataSubId=" + this.mPreferredDataSubId.get());
        if (!isActiveSubId(i3)) {
            logl("Can't switch data to inactive subId " + i3);
            if (i == Integer.MAX_VALUE) {
                this.mAutoSelectedDataSubId = Integer.MAX_VALUE;
                sendSetOpptCallbackHelper(iSetOpportunisticDataCallback, 0);
                return;
            } else {
                sendSetOpptCallbackHelper(iSetOpportunisticDataCallback, 2);
                return;
            }
        }
        if (this.mValidator.isValidating()) {
            this.mValidator.stopValidation();
            sendSetOpptCallbackHelper(this.mSetOpptSubCallback, 1);
            this.mSetOpptSubCallback = null;
        }
        removeMessages(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE);
        removeMessages(118);
        this.mPendingSwitchSubId = -1;
        if (i2 == 3) {
            logl("mOpportunisticSetDataSubId updated to " + i);
            this.mOpportunisticSetDataSubId = i;
        }
        if (i3 == this.mPreferredDataSubId.get()) {
            if (i == Integer.MAX_VALUE) {
                this.mAutoSelectedDataSubId = Integer.MAX_VALUE;
            }
            sendSetOpptCallbackHelper(iSetOpportunisticDataCallback, 0);
            return;
        }
        this.mLastSwitchPreferredDataReason = i2;
        logDataSwitchEvent(i3, 1, i2);
        registerDefaultNetworkChangeCallback(i3, i2);
        if (!this.mValidator.isValidationFeatureSupported()) {
            setAutoSelectedDataSubIdInternal(i);
            sendSetOpptCallbackHelper(iSetOpportunisticDataCallback, 0);
        } else {
            this.mPendingSwitchSubId = i3;
            this.mPendingSwitchNeedValidation = z;
            this.mSetOpptSubCallback = iSetOpportunisticDataCallback;
            this.mValidator.validate(i3, getValidationTimeout(i3, z), this.mPendingSwitchNeedValidation, this.mValidationCallback);
        }
    }

    private long getValidationTimeout(int i, boolean z) {
        CarrierConfigManager carrierConfigManager;
        PersistableBundle configForSubId;
        if (!z || (carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config")) == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return 2000L;
        }
        return configForSubId.getLong("data_switch_validation_timeout_long");
    }

    private void sendSetOpptCallbackHelper(ISetOpportunisticDataCallback iSetOpportunisticDataCallback, int i) {
        if (iSetOpportunisticDataCallback == null) {
            return;
        }
        try {
            iSetOpportunisticDataCallback.onComplete(i);
        } catch (RemoteException e) {
            logl("RemoteException " + e);
        }
    }

    private void setAutoSelectedDataSubIdInternal(int i) {
        if (this.mAutoSelectedDataSubId != i) {
            this.mAutoSelectedDataSubId = i;
            onEvaluate(false, switchReasonToString(this.mLastSwitchPreferredDataReason));
        }
    }

    private void confirmSwitch(int i, boolean z) {
        int i2;
        StringBuilder sb = new StringBuilder("confirmSwitch: subId ");
        sb.append(i);
        sb.append(z ? " confirmed." : " cancelled.");
        logl(sb.toString());
        if (!isActiveSubId(i)) {
            logl("confirmSwitch: subId " + i + " is no longer active");
            i2 = 2;
        } else if (!z) {
            if (this.mLastSwitchPreferredDataReason == 4) {
                this.mAutoDataSwitchController.evaluateRetryOnValidationFailed();
            }
            i2 = 1;
        } else {
            if (i == this.mPrimaryDataSubId) {
                setAutoSelectedDataSubIdInternal(Integer.MAX_VALUE);
            } else {
                setAutoSelectedDataSubIdInternal(i);
            }
            this.mAutoDataSwitchController.resetFailedCount();
            i2 = 0;
        }
        sendSetOpptCallbackHelper(this.mSetOpptSubCallback, i2);
        this.mSetOpptSubCallback = null;
        this.mPendingSwitchSubId = -1;
    }

    private void onNetworkAvailable(int i, Network network) {
        log("onNetworkAvailable: on subId " + i);
        int i2 = this.mPendingSwitchSubId;
        if (i2 == -1 || i2 != i || this.mPendingSwitchNeedValidation) {
            return;
        }
        confirmSwitch(i, true);
    }

    private void onValidationDone(int i, boolean z) {
        StringBuilder sb = new StringBuilder("onValidationDone: ");
        sb.append(z ? "passed" : "failed");
        sb.append(" on subId ");
        sb.append(i);
        logl(sb.toString());
        int i2 = this.mPendingSwitchSubId;
        if (i2 == -1 || i2 != i) {
            return;
        }
        confirmSwitch(i, z || !this.mPendingSwitchNeedValidation);
    }

    public void trySetOpportunisticDataSubscription(int i, boolean z, ISetOpportunisticDataCallback iSetOpportunisticDataCallback) {
        StringBuilder sb = new StringBuilder("Try set opportunistic data subscription to subId ");
        sb.append(i);
        sb.append(z ? " with " : " without ");
        sb.append("validation");
        logl(sb.toString());
        obtainMessage(NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS, i, z ? 1 : 0, iSetOpportunisticDataCallback).sendToTarget();
    }

    public void notifySubInfoReady() {
        obtainMessage(124).sendToTarget();
    }

    protected boolean isPhoneInVoiceCall(Phone phone) {
        if (phone == null) {
            return false;
        }
        if (TelephonyBaseUtilsStub.isMiuiRom()) {
            boolean zIsPhoneInVoiceCall = MiuiPhoneSwitcherStub.get().isPhoneInVoiceCall(phone, false);
            log("phoneInVoiceCall:" + zIsPhoneInVoiceCall);
            return zIsPhoneInVoiceCall;
        }
        Call backgroundCall = phone.getBackgroundCall();
        Call foregroundCall = phone.getForegroundCall();
        Call ringingCall = phone.getRingingCall();
        if (backgroundCall != null && foregroundCall != null && ringingCall != null) {
            if (!backgroundCall.isIdle() && backgroundCall.getState() != Call.State.DIALING) {
                return true;
            }
            if (!foregroundCall.isIdle() && foregroundCall.getState() != Call.State.DIALING) {
                return true;
            }
            if (!ringingCall.isIdle() && ringingCall.getState() != Call.State.DIALING) {
                return true;
            }
        }
        return false;
    }

    protected boolean updateHalCommandToUse() {
        this.mHalCommandToUse = this.mRadioConfig.isSetPreferredDataCommandSupported() ? 2 : 1;
        return true;
    }

    public int getPreferredDataPhoneId() {
        return this.mPreferredDataPhoneId;
    }

    protected void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    protected void log(String str) {
        Rlog.d(LOG_TAG, str);
    }

    protected void loge(String str) {
        Rlog.e(LOG_TAG, str);
    }

    private static String switchReasonToString(int i) {
        if (i == 0) {
            return SatelliteConstants.DEFAULT_PLMN;
        }
        if (i == 1) {
            return "MANUAL";
        }
        if (i == 2) {
            return "IN_CALL";
        }
        if (i == 3) {
            return "CBRS";
        }
        if (i == 4) {
            return "AUTO";
        }
        return "UNKNOWN(" + i + ")";
    }

    private static String switchStateToString(int i) {
        if (i == 0) {
            return SatelliteConstants.DEFAULT_PLMN;
        }
        if (i == 1) {
            return "START";
        }
        if (i == 2) {
            return "END";
        }
        return "UNKNOWN(" + i + ")";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logDataSwitchEvent(int i, int i2, int i3) {
        logl("Data switch state=" + switchStateToString(i2) + " due to reason=" + switchReasonToString(i3) + " on subId " + i);
        TelephonyProto$TelephonyEvent.DataSwitch dataSwitch = new TelephonyProto$TelephonyEvent.DataSwitch();
        dataSwitch.state = i2;
        dataSwitch.reason = i3;
    }

    protected void notifyPreferredDataSubIdChanged() {
        TelephonyRegistryManager telephonyRegistryManager = (TelephonyRegistryManager) this.mContext.getSystemService("telephony_registry");
        logl("notifyPreferredDataSubIdChanged to " + this.mPreferredDataSubId.get());
        telephonyRegistryManager.notifyActiveDataSubIdChanged(this.mPreferredDataSubId.get());
        MiuiPhoneSwitcherStub.get().onDdsSwitchResponse();
    }

    public int getActiveDataSubId() {
        return this.mPreferredDataSubId.get();
    }

    public int getAutoSelectedDataSubId() {
        return this.mAutoSelectedDataSubId;
    }

    public int getOpportunisticSetDataSubId() {
        return this.mOpportunisticSetDataSubId;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("PhoneSwitcher:");
        indentingPrintWriter.increaseIndent();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < this.mActiveModemCount; i++) {
            PhoneState phoneState = this.mPhoneStates[i];
            calendar.setTimeInMillis(phoneState.lastRequested);
            StringBuilder sb = new StringBuilder("PhoneId(");
            sb.append(i);
            sb.append(") active=");
            sb.append(phoneState.active);
            sb.append(", lastRequest=");
            sb.append(phoneState.lastRequested == 0 ? "never" : String.format("%tm-%td %tH:%tM:%tS.%tL", calendar, calendar, calendar, calendar, calendar, calendar));
            indentingPrintWriter.println(sb.toString());
        }
        indentingPrintWriter.println("mPreferredDataPhoneId=" + this.mPreferredDataPhoneId);
        indentingPrintWriter.println("mPreferredDataSubId=" + this.mPreferredDataSubId.get());
        indentingPrintWriter.println("DefaultDataSubId=" + this.mSubscriptionManagerService.getDefaultDataSubId());
        StringBuilder sb2 = new StringBuilder("DefaultDataPhoneId=");
        SubscriptionManagerService subscriptionManagerService = this.mSubscriptionManagerService;
        sb2.append(subscriptionManagerService.getPhoneId(subscriptionManagerService.getDefaultDataSubId()));
        indentingPrintWriter.println(sb2.toString());
        indentingPrintWriter.println("mPrimaryDataSubId=" + this.mPrimaryDataSubId);
        indentingPrintWriter.println("mAutoSelectedDataSubId=" + this.mAutoSelectedDataSubId);
        indentingPrintWriter.println("mOpportunisticSetDataSubId=" + this.mOpportunisticSetDataSubId);
        indentingPrintWriter.println("mIsRegisteredForImsRadioTechChange=" + this.mIsRegisteredForImsRadioTechChange);
        indentingPrintWriter.println("mPendingSwitchNeedValidation=" + this.mPendingSwitchNeedValidation);
        indentingPrintWriter.println("mMaxDataAttachModemCount=" + this.mMaxDataAttachModemCount);
        indentingPrintWriter.println("mActiveModemCount=" + this.mActiveModemCount);
        indentingPrintWriter.println("mPhoneIdInVoiceCall=" + this.mPhoneIdInVoiceCall);
        indentingPrintWriter.println("mCurrentDdsSwitchFailure=" + this.mCurrentDdsSwitchFailure);
        indentingPrintWriter.println("mLastSwitchPreferredDataReason=" + switchReasonToString(this.mLastSwitchPreferredDataReason));
        indentingPrintWriter.println("Local logs:");
        indentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        this.mAutoDataSwitchController.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
    }

    protected boolean isAnyVoiceCallActiveOnDevice() {
        boolean z = this.mPhoneIdInVoiceCall != -1;
        if (VDBG) {
            log("isAnyVoiceCallActiveOnDevice: " + z);
        }
        return z;
    }

    protected void onDdsSwitchResponse(AsyncResult asyncResult) {
        boolean z = asyncResult != null && asyncResult.exception == null;
        Integer num = (Integer) asyncResult.userObj;
        final int iIntValue = num.intValue();
        boolean zIsValidPhoneId = SubscriptionManager.isValidPhoneId(iIntValue);
        if (!zIsValidPhoneId) {
            loge("onDdsSwitchResponse: invalid phoneid=" + iIntValue);
        }
        if (this.mEmergencyOverride != null) {
            logl("Emergency override result sent = " + z);
            this.mEmergencyOverride.sendOverrideCompleteCallbackResultAndClear(z);
        } else if (!z) {
            logl("onDdsSwitchResponse: DDS switch failed. with exception " + asyncResult.exception);
            Throwable th = asyncResult.exception;
            if (th instanceof CommandException) {
                CommandException.Error commandError = ((CommandException) th).getCommandError();
                if (zIsValidPhoneId) {
                    this.mCurrentDdsSwitchFailure.get(iIntValue).add(commandError);
                }
                if (commandError == CommandException.Error.OP_NOT_ALLOWED_DURING_VOICE_CALL) {
                    logl("onDdsSwitchResponse: Wait for call end indication");
                    return;
                } else if (commandError == CommandException.Error.INVALID_SIM_STATE) {
                    logl("onDdsSwitchResponse: Wait for SIM to get READY");
                    return;
                }
            }
            if (zIsValidPhoneId) {
                logl("onDdsSwitchResponse: Scheduling DDS switch retry");
                sendMessageDelayed(Message.obtain(this, 113, num), 5000L);
                return;
            }
            return;
        }
        if (z) {
            logl("onDdsSwitchResponse: DDS switch success on phoneId = " + iIntValue);
            this.mAutoDataSwitchController.displayAutoDataSwitchNotification(iIntValue, this.mLastSwitchPreferredDataReason == 4);
        }
        if (zIsValidPhoneId) {
            this.mCurrentDdsSwitchFailure.get(iIntValue).clear();
        }
        this.mActivePhoneRegistrants.notifyRegistrants();
        notifyPreferredDataSubIdChanged();
        this.mPhoneSwitcherCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.PhoneSwitcher$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PhoneSwitcher.PhoneSwitcherCallback phoneSwitcherCallback = (PhoneSwitcher.PhoneSwitcherCallback) obj;
                phoneSwitcherCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.PhoneSwitcher$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        phoneSwitcherCallback.onPreferredDataPhoneIdChanged(i);
                    }
                });
            }
        });
    }

    protected boolean isPhoneIdValidForRetry(int i) {
        SubscriptionManagerService subscriptionManagerService = this.mSubscriptionManagerService;
        int phoneId = subscriptionManagerService.getPhoneId(subscriptionManagerService.getDefaultDataSubId());
        if (phoneId != -1 && phoneId == i) {
            return true;
        }
        if (this.mNetworkRequestList.isEmpty()) {
            return false;
        }
        Iterator<TelephonyNetworkRequest> it = this.mNetworkRequestList.iterator();
        while (it.hasNext()) {
            if (phoneIdForRequest(it.next()) == i) {
                return true;
            }
        }
        return false;
    }

    protected void onGetInVoiceCallPhoneId() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (isPhoneInVoiceCall(phone) || isPhoneInVoiceCall(phone.getImsPhone())) {
                this.mPhoneIdInVoiceCall = phone.getPhoneId();
                return;
            }
        }
    }
}
