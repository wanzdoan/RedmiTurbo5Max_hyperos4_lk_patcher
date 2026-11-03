package com.android.internal.telephony.data;

import android.R;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.telephony.CarrierConfigManager;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyDisplayInfo;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.NetworkTypeController$ModemDisplayNetworkType$$ExternalSyntheticRecord0;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.NotificationChannelController;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
public class AutoDataSwitchController extends Handler {
    public static final int EVALUATION_REASON_CARRIER_CONFIG_CHANGED = 9;
    public static final int EVALUATION_REASON_DATA_SETTINGS_CHANGED = 5;
    public static final int EVALUATION_REASON_DEFAULT_NETWORK_CHANGED = 4;
    public static final int EVALUATION_REASON_DISPLAY_INFO_CHANGED = 2;
    public static final int EVALUATION_REASON_REGISTRATION_STATE_CHANGED = 1;
    public static final int EVALUATION_REASON_RETRY_VALIDATION = 6;
    public static final int EVALUATION_REASON_SIGNAL_STRENGTH_CHANGED = 3;
    public static final int EVALUATION_REASON_SIM_LOADED = 7;
    public static final int EVALUATION_REASON_VOICE_CALL_END = 8;
    private final AlarmManager mAlarmManager;
    private int mAutoDataSwitchValidationMaxRetry;
    public boolean mAutoDdsValueAddedEvaluationforDdsRevert;
    private int mAutoSwitchValidationFailedCount;
    private final CarrierConfigManager mCarrierConfigManager;
    private final Context mContext;
    private boolean mDefaultNetworkIsOnNonCellular;
    private boolean mDisplayedNotification;
    private final Map mEventsToAlarmListener;
    private final LocalLog mLocalLog;
    protected final PhoneSwitcher mPhoneSwitcher;
    private final AutoDataSwitchControllerCallback mPhoneSwitcherCallback;
    private PhoneSignalStatus[] mPhonesSignalStatus;
    private boolean mRequirePingTestBeforeSwitch;
    private final Map mScheduledEventsToExtras;
    private int mScoreTolerance;
    protected int mSelectedTargetPhoneId;
    private final SubscriptionManagerService mSubscriptionManagerService;
    private static final Map STABILITY_CHECK_TIMER_MAP = new ArrayMap();
    private static final long RETRY_LONG_DELAY_TIMER_THRESHOLD_MILLIS = 60000;
    private static final String[] OPP_AUTO_DATA_SWITCH_CARRIER_CONFIG_KEYS = {"opp_auto_data_switch_policy_int", "opp_auto_data_switch_availability_stability_millis_long", "opp_auto_data_switch_performance_stability_millis_long", "opp_auto_data_switch_availability_switchback_millis_long", "opp_auto_data_switch_ping_before_switch_bool", "opp_auto_data_switch_validation_max_retries_int"};
    private static FeatureFlags sFeatureFlags = new FeatureFlagsImpl();

    public static abstract class AutoDataSwitchControllerCallback {
        public abstract void onRequireCancelAnyPendingAutoSwitchValidation();

        public abstract void onRequireImmediatelySwitchToPhone(int i, int i2);

        public abstract void onRequireValidation(int i, boolean z);
    }

    private static boolean isHomeService(int i) {
        return i == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isInService(int i) {
        return i == 1 || i == 5;
    }

    protected boolean ignoreCheckingAutoSwitchBack() {
        return false;
    }

    protected boolean isAutoSwitchEnabledByUser(Phone phone) {
        return false;
    }

    protected boolean isUserDataSettingEnabledOnNonDDS(Phone phone) {
        return false;
    }

    private static final class StabilityEventExtra extends Record {
        private final boolean needValidation;
        private final int switchType;
        private final int targetPhoneId;

        private /* synthetic */ boolean $record$equals(Object obj) {
            if (!(obj instanceof StabilityEventExtra)) {
                return false;
            }
            StabilityEventExtra stabilityEventExtra = (StabilityEventExtra) obj;
            return this.needValidation == stabilityEventExtra.needValidation && this.targetPhoneId == stabilityEventExtra.targetPhoneId && this.switchType == stabilityEventExtra.switchType;
        }

        private StabilityEventExtra(int i, int i2, boolean z) {
            this.targetPhoneId = i;
            this.switchType = i2;
            this.needValidation = z;
        }

        @Override // java.lang.Record
        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return AutoDataSwitchController$StabilityEventExtra$$ExternalSyntheticRecord0.m(this.needValidation, this.targetPhoneId, this.switchType);
        }

        public boolean needValidation() {
            return this.needValidation;
        }

        public int switchType() {
            return this.switchType;
        }

        public int targetPhoneId() {
            return this.targetPhoneId;
        }

        @Override // java.lang.Record
        public String toString() {
            return "StabilityEventExtra{targetPhoneId=" + this.targetPhoneId + ", switchType=" + AutoDataSwitchController.switchTypeToString(this.switchType) + ", needValidation=" + this.needValidation + "}";
        }
    }

    private static final class EvaluateEventExtra extends Record {
        private final int evaluateReason;

        private /* synthetic */ boolean $record$equals(Object obj) {
            return (obj instanceof EvaluateEventExtra) && this.evaluateReason == ((EvaluateEventExtra) obj).evaluateReason;
        }

        private /* synthetic */ Object[] $record$getFieldsAsObjects() {
            return new Object[]{Integer.valueOf(this.evaluateReason)};
        }

        private EvaluateEventExtra(int i) {
            this.evaluateReason = i;
        }

        @Override // java.lang.Record
        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        public int evaluateReason() {
            return this.evaluateReason;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return AutoDataSwitchController$EvaluateEventExtra$$ExternalSyntheticRecord0.m(this.evaluateReason);
        }

        @Override // java.lang.Record
        public final String toString() {
            return NetworkTypeController$ModemDisplayNetworkType$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), EvaluateEventExtra.class, "evaluateReason");
        }
    }

    private static class PhoneSignalStatus {
        private int mDataRegState;
        private TelephonyDisplayInfo mDisplayInfo;
        private boolean mListeningForEvents;
        private final Phone mPhone;
        private SignalStrength mSignalStrength;

        enum UsableState {
            HOME(2),
            ROAMING_ENABLED(1),
            NON_TERRESTRIAL(0),
            NOT_USABLE(-1);

            final int mScore;

            UsableState(int i) {
                this.mScore = i;
            }
        }

        private PhoneSignalStatus(Phone phone) {
            this.mPhone = phone;
            this.mDataRegState = phone.getServiceState().getNetworkRegistrationInfo(2, 1).getRegistrationState();
            this.mDisplayInfo = phone.getDisplayInfoController().getTelephonyDisplayInfo();
            this.mSignalStrength = phone.getSignalStrength();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getRatSignalScore() {
            if (AutoDataSwitchController.isInService(this.mDataRegState)) {
                return this.mPhone.getDataNetworkController().getDataConfigManager().getAutoDataSwitchScore(this.mDisplayInfo, this.mSignalStrength);
            }
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public UsableState getUsableState() {
            ServiceState serviceState = this.mPhone.getServiceState();
            boolean z = serviceState != null && serviceState.isUsingNonTerrestrialNetwork();
            int i = this.mDataRegState;
            if (i == 1) {
                return z ? UsableState.NON_TERRESTRIAL : UsableState.HOME;
            }
            if (i == 5) {
                if (z && this.mPhone.getDataNetworkController().getDataConfigManager().isDataRoamingAllowedOnSatellite()) {
                    return UsableState.NON_TERRESTRIAL;
                }
                if (this.mPhone.getDataRoamingEnabled()) {
                    return z ? UsableState.NON_TERRESTRIAL : UsableState.ROAMING_ENABLED;
                }
                return UsableState.NOT_USABLE;
            }
            return UsableState.NOT_USABLE;
        }

        public String toString() {
            return "{phone " + this.mPhone.getPhoneId() + " score=" + getRatSignalScore() + " dataRegState=" + NetworkRegistrationInfo.registrationStateToString(this.mDataRegState) + " " + getUsableState() + " " + this.mDisplayInfo + " signalStrength=" + this.mSignalStrength.getLevel() + " listeningForEvents=" + this.mListeningForEvents + "}";
        }
    }

    public AutoDataSwitchController(Context context, Looper looper, PhoneSwitcher phoneSwitcher, FeatureFlags featureFlags, AutoDataSwitchControllerCallback autoDataSwitchControllerCallback) {
        super(looper);
        this.mLocalLog = new LocalLog(128);
        this.mDefaultNetworkIsOnNonCellular = false;
        this.mDisplayedNotification = false;
        this.mScoreTolerance = -1;
        this.mRequirePingTestBeforeSwitch = true;
        this.mAutoSwitchValidationFailedCount = 0;
        this.mSelectedTargetPhoneId = -1;
        this.mAutoDdsValueAddedEvaluationforDdsRevert = false;
        this.mContext = context;
        sFeatureFlags = featureFlags;
        this.mPhoneSwitcherCallback = autoDataSwitchControllerCallback;
        this.mAlarmManager = (AlarmManager) context.getSystemService(AlarmManager.class);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mCarrierConfigManager = carrierConfigManager;
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.internal.telephony.data.AutoDataSwitchController$$ExternalSyntheticLambda1
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.data.AutoDataSwitchController$$ExternalSyntheticLambda2
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
        this.mScheduledEventsToExtras = new HashMap();
        this.mEventsToAlarmListener = new HashMap();
        this.mSubscriptionManagerService = SubscriptionManagerService.getInstance();
        this.mPhoneSwitcher = phoneSwitcher;
        readDeviceResourceAndCarrierConfigIfNeeded();
        int length = PhoneFactory.getPhones().length;
        this.mPhonesSignalStatus = new PhoneSignalStatus[length];
        for (int i = 0; i < length; i++) {
            registerAllEventsForPhone(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        if (shouldExcludeOpportunisticForSwitch() || !isActiveVisibleSubId(i2)) {
            return;
        }
        logl("onCarrierConfigChanged: slot=" + i + ", sub=" + i2 + ", carrierId=" + i3);
        evaluateAutoDataSwitch(9);
    }

    public synchronized void onMultiSimConfigChanged(int i) {
        try {
            int length = this.mPhonesSignalStatus.length;
            if (length == i) {
                return;
            }
            for (int i2 = i; i2 < length; i2++) {
                unregisterAllEventsForPhone(i2);
            }
            this.mPhonesSignalStatus = (PhoneSignalStatus[]) Arrays.copyOf(this.mPhonesSignalStatus, i);
            while (length < i) {
                registerAllEventsForPhone(length);
                length++;
            }
            logl("onMultiSimConfigChanged: " + Arrays.toString(this.mPhonesSignalStatus));
        } catch (Throwable th) {
            throw th;
        }
    }

    public void notifySubscriptionsMappingChanged() {
        sendEmptyMessage(6);
    }

    private void onSubscriptionsChanged() {
        if (updateListenerRegistrations()) {
            logl("onSubscriptionChanged: " + Arrays.toString(this.mPhonesSignalStatus));
        }
    }

    /* JADX WARN: Code duplicated, block: B:24:0x004e  */
    /* JADX WARN: Code duplicated, block: B:28:0x005d  */
    /* JADX WARN: Code duplicated, block: B:33:0x006c A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:34:0x006e  */
    /* JADX WARN: Code duplicated, block: B:39:0x007f A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:41:0x007c A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    private boolean updateListenerRegistrations() {
        String str;
        boolean z;
        boolean z2;
        PhoneSignalStatus[] phoneSignalStatusArr;
        int i = 0;
        if (this.mSubscriptionManagerService.getActiveSubIdList(shouldExcludeOpportunisticForSwitch()).length < 2) {
            str = "only have one active subscription";
        } else if (this.mDefaultNetworkIsOnNonCellular) {
            str = "default network is on non-cellular network";
        } else {
            SubscriptionManagerService subscriptionManagerService = this.mSubscriptionManagerService;
            int phoneId = subscriptionManagerService.getPhoneId(subscriptionManagerService.getDefaultDataSubId());
            Phone phone = PhoneFactory.getPhone(phoneId);
            if (phone == null || phone.isUserDataEnabled()) {
                int i2 = 0;
                while (true) {
                    if (i2 < this.mPhonesSignalStatus.length) {
                        if (i2 != phoneId && isDataEnabledForAutoDataSwitch(i2)) {
                            str = "";
                            z = false;
                            break;
                        }
                        i2++;
                    } else {
                        str = "no candidate enabled auto data switch feature";
                    }
                }
                if (z) {
                    logl("updateListenerRegistrations: ".concat(str));
                }
                z2 = false;
                while (true) {
                    phoneSignalStatusArr = this.mPhonesSignalStatus;
                    if (i < phoneSignalStatusArr.length) {
                        return z2;
                    }
                    if (z && phoneSignalStatusArr[i].mListeningForEvents) {
                        unregisterAllEventsForPhone(i);
                    } else {
                        if (z && !this.mPhonesSignalStatus[i].mListeningForEvents) {
                            registerAllEventsForPhone(i);
                        }
                        i++;
                    }
                    z2 = true;
                    i++;
                }
            } else {
                str = "default data is disabled";
            }
        }
        z = true;
        if (z) {
            logl("updateListenerRegistrations: ".concat(str));
        }
        z2 = false;
        while (true) {
            phoneSignalStatusArr = this.mPhonesSignalStatus;
            if (i < phoneSignalStatusArr.length) {
                return z2;
            }
            if (z) {
                if (z) {
                }
            } else if (z) {
            }
            i++;
        }
    }

    private boolean isDataEnabledForAutoDataSwitch(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || !phone.getDataSettingsManager().isDataEnabled()) {
            return false;
        }
        if (isStandaloneOpportunistic(i)) {
            return phone.getDataSettingsManager().isMobileDataPolicyEnabled(3);
        }
        return true;
    }

    private boolean isStandaloneOpportunistic(int i) {
        SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(this.mSubscriptionManagerService.getSubId(i));
        return subscriptionInfo != null && subscriptionInfo.isOpportunistic() && subscriptionInfo.getGroupUuid() == null;
    }

    private void registerAllEventsForPhone(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone != null && isActiveModemPhone(i)) {
            this.mPhonesSignalStatus[i] = new PhoneSignalStatus(phone);
            phone.getDisplayInfoController().registerForTelephonyDisplayInfoChanged(this, 2, Integer.valueOf(i));
            phone.getSignalStrengthController().registerForSignalStrengthChanged(this, 4, Integer.valueOf(i));
            phone.getServiceStateTracker().registerForServiceStateChanged(this, 1, Integer.valueOf(i));
            this.mPhonesSignalStatus[i].mListeningForEvents = true;
            logl("registerAllEventsForPhone: registered listeners for phone " + i);
            return;
        }
        logle("Unexpected null phone " + i + " when register all events");
    }

    private void unregisterAllEventsForPhone(int i) {
        if (isActiveModemPhone(i)) {
            Phone phone = this.mPhonesSignalStatus[i].mPhone;
            phone.getDisplayInfoController().unregisterForTelephonyDisplayInfoChanged(this);
            phone.getSignalStrengthController().unregisterForSignalStrengthChanged(this);
            phone.getServiceStateTracker().unregisterForServiceStateChanged(this);
            this.mPhonesSignalStatus[i].mListeningForEvents = false;
            logl("unregisterAllEventsForPhone: unregistered listeners for phone " + i);
            return;
        }
        logle("Unexpected out of bound phone " + i + " when unregister all events");
    }

    public void readDeviceResourceConfig() {
        long autoDataSwitchAvailabilityStabilityTimeThreshold;
        DataConfigManager dataConfigManager = PhoneFactory.getDefaultPhone().getDataNetworkController().getDataConfigManager();
        this.mScoreTolerance = dataConfigManager.getAutoDataSwitchScoreTolerance();
        this.mRequirePingTestBeforeSwitch = dataConfigManager.isPingTestBeforeAutoDataSwitchRequired();
        Map map = STABILITY_CHECK_TIMER_MAP;
        map.put(0, Long.valueOf(dataConfigManager.getAutoDataSwitchAvailabilityStabilityTimeThreshold()));
        map.put(1, Long.valueOf(dataConfigManager.getAutoDataSwitchPerformanceStabilityTimeThreshold()));
        if (dataConfigManager.getAutoDataSwitchAvailabilitySwitchbackStabilityTimeThreshold() >= 0) {
            autoDataSwitchAvailabilityStabilityTimeThreshold = dataConfigManager.getAutoDataSwitchAvailabilitySwitchbackStabilityTimeThreshold();
        } else {
            autoDataSwitchAvailabilityStabilityTimeThreshold = dataConfigManager.getAutoDataSwitchAvailabilityStabilityTimeThreshold();
        }
        map.put(2, Long.valueOf(autoDataSwitchAvailabilityStabilityTimeThreshold));
        this.mAutoDataSwitchValidationMaxRetry = dataConfigManager.getAutoDataSwitchValidationMaxRetry();
    }

    private void readCarrierConfigIfNeeded() {
        if (shouldExcludeOpportunisticForSwitch()) {
            logl("OPPT switch excluded, ignore carrier config.");
            return;
        }
        PersistableBundle carrierConfigForPrimaryPhone = getCarrierConfigForPrimaryPhone();
        this.mRequirePingTestBeforeSwitch = carrierConfigForPrimaryPhone.getBoolean("opp_auto_data_switch_ping_before_switch_bool", this.mRequirePingTestBeforeSwitch);
        Map map = STABILITY_CHECK_TIMER_MAP;
        map.put(0, Long.valueOf(carrierConfigForPrimaryPhone.getLong("opp_auto_data_switch_availability_stability_millis_long", ((Long) map.get(0)).longValue())));
        map.put(1, Long.valueOf(carrierConfigForPrimaryPhone.getLong("opp_auto_data_switch_performance_stability_millis_long", ((Long) map.get(1)).longValue())));
        map.put(2, Long.valueOf(carrierConfigForPrimaryPhone.getLong("opp_auto_data_switch_availability_switchback_millis_long", ((Long) map.get(2)).longValue())));
        this.mAutoDataSwitchValidationMaxRetry = carrierConfigForPrimaryPhone.getInt("opp_auto_data_switch_validation_max_retries_int", this.mAutoDataSwitchValidationMaxRetry);
        logl("Parameters loaded after carrier config: PING_TEST_BEFORE_SWITCH=" + this.mRequirePingTestBeforeSwitch + ", AVAILABILITY_STABILITY_TIME_THRESHOLD=" + map.get(0) + ", PERFORMANCE_STABILITY_TIME_THRESHOLD=" + map.get(1) + ", SWITCHBACK_STABILITY_TIME_THRESHOLD=" + map.get(2) + ", SWITCH_VALIDATION_MAX_RETRY=" + this.mAutoDataSwitchValidationMaxRetry);
    }

    private void readDeviceResourceAndCarrierConfigIfNeeded() {
        readDeviceResourceConfig();
        readCarrierConfigIfNeeded();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                onServiceStateChanged(((Integer) ((AsyncResult) message.obj).userObj).intValue());
                break;
            case 2:
                onDisplayInfoChanged(((Integer) ((AsyncResult) message.obj).userObj).intValue());
                break;
            case 3:
                Object obj = this.mScheduledEventsToExtras.get(3);
                if (obj instanceof EvaluateEventExtra) {
                    this.mScheduledEventsToExtras.remove(3);
                    onEvaluateAutoDataSwitch(((EvaluateEventExtra) obj).evaluateReason);
                }
                break;
            case 4:
                onSignalStrengthChanged(((Integer) ((AsyncResult) message.obj).userObj).intValue());
                break;
            case 5:
                Object obj2 = this.mScheduledEventsToExtras.get(5);
                if (obj2 instanceof StabilityEventExtra) {
                    StabilityEventExtra stabilityEventExtra = (StabilityEventExtra) obj2;
                    int i = stabilityEventExtra.targetPhoneId;
                    boolean z = stabilityEventExtra.needValidation;
                    StringBuilder sb = new StringBuilder("require validation on phone ");
                    sb.append(i);
                    sb.append(z ? "" : " no");
                    sb.append(" need to pass");
                    logl(sb.toString());
                    this.mScheduledEventsToExtras.remove(5);
                    this.mPhoneSwitcherCallback.onRequireValidation(i, z);
                }
                break;
            case 6:
                onSubscriptionsChanged();
                break;
            default:
                loge("Unexpected event " + message.what);
                break;
        }
    }

    private void onServiceStateChanged(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone != null && isActiveModemPhone(i)) {
            int i2 = this.mPhonesSignalStatus[i].mDataRegState;
            int registrationState = phone.getServiceState().getNetworkRegistrationInfo(2, 1).getRegistrationState();
            if (registrationState != i2) {
                this.mPhonesSignalStatus[i].mDataRegState = registrationState;
                if (isInService(i2) == isInService(registrationState) && isHomeService(i2) == isHomeService(registrationState)) {
                    return;
                }
                logl("onServiceStateChanged: phone " + i + " " + NetworkRegistrationInfo.registrationStateToString(i2) + " -> " + NetworkRegistrationInfo.registrationStateToString(registrationState));
                evaluateAutoDataSwitch(1);
                return;
            }
            return;
        }
        logle("Unexpected null phone " + i + " upon its registration state changed");
    }

    private void onDisplayInfoChanged(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone != null && isActiveModemPhone(i)) {
            TelephonyDisplayInfo telephonyDisplayInfo = phone.getDisplayInfoController().getTelephonyDisplayInfo();
            this.mPhonesSignalStatus[i].mDisplayInfo = telephonyDisplayInfo;
            if (getBetterCandidatePhoneIdBasedOnScore() != this.mSelectedTargetPhoneId) {
                logl("onDisplayInfoChanged: phone " + i + " " + telephonyDisplayInfo);
                evaluateAutoDataSwitch(2);
                return;
            }
            return;
        }
        logle("Unexpected null phone " + i + " upon its display info changed");
    }

    private void onSignalStrengthChanged(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone != null && isActiveModemPhone(i)) {
            SignalStrength signalStrength = phone.getSignalStrength();
            SignalStrength signalStrength2 = this.mPhonesSignalStatus[i].mSignalStrength;
            if (signalStrength2.getLevel() != signalStrength.getLevel()) {
                this.mPhonesSignalStatus[i].mSignalStrength = signalStrength;
                if (getBetterCandidatePhoneIdBasedOnScore() != this.mSelectedTargetPhoneId) {
                    logl("onSignalStrengthChanged: phone " + i + " " + signalStrength2.getLevel() + "->" + signalStrength.getLevel());
                    evaluateAutoDataSwitch(3);
                    return;
                }
                return;
            }
            return;
        }
        logle("Unexpected null phone " + i + " upon its signal strength changed");
    }

    private int getBetterCandidatePhoneIdBasedOnScore() {
        int preferredDataPhoneId = this.mPhoneSwitcher.getPreferredDataPhoneId();
        SubscriptionManagerService subscriptionManagerService = this.mSubscriptionManagerService;
        int phoneId = subscriptionManagerService.getPhoneId(subscriptionManagerService.getDefaultDataSubId());
        if (isActiveModemPhone(preferredDataPhoneId) && isActiveModemPhone(phoneId)) {
            int ratSignalScore = this.mPhonesSignalStatus[phoneId].getRatSignalScore();
            int ratSignalScore2 = this.mPhonesSignalStatus[preferredDataPhoneId].getRatSignalScore();
            if (phoneId == preferredDataPhoneId) {
                int i = 0;
                while (true) {
                    PhoneSignalStatus[] phoneSignalStatusArr = this.mPhonesSignalStatus;
                    if (i < phoneSignalStatusArr.length) {
                        if (i != preferredDataPhoneId && isHomeService(phoneSignalStatusArr[i].mDataRegState) && this.mPhonesSignalStatus[i].getRatSignalScore() - ratSignalScore > this.mScoreTolerance) {
                            return i;
                        }
                        i++;
                    }
                }
            } else if (ratSignalScore2 - ratSignalScore <= this.mScoreTolerance) {
                return phoneId;
            }
        }
        return -1;
    }

    public void evaluateAutoDataSwitch(int i) {
        readDeviceResourceAndCarrierConfigIfNeeded();
        long jLongValue = i == 6 ? ((Long) STABILITY_CHECK_TIMER_MAP.get(0)).longValue() << this.mAutoSwitchValidationFailedCount : 0L;
        if (i == 5 || i == 4 || i == 9) {
            updateListenerRegistrations();
            scheduleEventWithTimer(3, new EvaluateEventExtra(i), jLongValue);
        } else {
            if (this.mScheduledEventsToExtras.containsKey(3)) {
                return;
            }
            scheduleEventWithTimer(3, new EvaluateEventExtra(i), jLongValue);
        }
    }

    private void onEvaluateAutoDataSwitch(int i) {
        if (i == 9 && shouldExcludeOpportunisticForSwitch() && this.mScheduledEventsToExtras.containsKey(5)) {
            log("onEvaluateAutoDataSwitch: opportunistic policy disabled, cancelling pending switch");
            cancelAnyPendingSwitch();
            return;
        }
        if (!isAvailabilityBasedSwitchEnabled()) {
            logle("onEvaluateAutoDataSwitch: auto data switch feature is disabled.");
            return;
        }
        int defaultDataSubId = this.mSubscriptionManagerService.getDefaultDataSubId();
        if (this.mSubscriptionManagerService.getActiveSubIdList(shouldExcludeOpportunisticForSwitch()).length < 2) {
            logle("onEvaluateAutoDataSwitch: switch requires two active subscriptions.");
            return;
        }
        int phoneId = this.mSubscriptionManagerService.getPhoneId(defaultDataSubId);
        if (PhoneFactory.getPhone(phoneId) == null) {
            logle("onEvaluateAutoDataSwitch: cannot find the phone associated with default data subscription " + defaultDataSubId);
            return;
        }
        int preferredDataPhoneId = this.mPhoneSwitcher.getPreferredDataPhoneId();
        int opportunisticSetDataSubId = this.mPhoneSwitcher.getOpportunisticSetDataSubId();
        int phoneId2 = opportunisticSetDataSubId == Integer.MAX_VALUE ? -1 : this.mSubscriptionManagerService.getPhoneId(opportunisticSetDataSubId);
        if (!isActiveModemPhone(phoneId2)) {
            phoneId2 = phoneId;
        }
        StringBuilder sb = new StringBuilder("onEvaluateAutoDataSwitch:");
        sb.append(" defaultPhoneId: ");
        sb.append(phoneId);
        sb.append(" currentPreferredPhoneId: ");
        sb.append(preferredDataPhoneId);
        sb.append(" stickyTargetPhoneId: ");
        sb.append(phoneId2);
        sb.append(", reason: ");
        sb.append(evaluationReasonToString(i));
        if (preferredDataPhoneId == phoneId2) {
            onEvaluateAutoDataSwitchOutOfTarget(phoneId2, phoneId, sb);
        } else {
            onEvaluateAutoDataSwitchToTarget(phoneId2, preferredDataPhoneId, phoneId, sb);
        }
    }

    private void onEvaluateAutoDataSwitchOutOfTarget(int i, int i2, StringBuilder sb) {
        StabilityEventExtra stabilityEventExtraEvaluateSwitchOutOfTarget = evaluateSwitchOutOfTarget(i, sb);
        logl(sb.toString());
        if (stabilityEventExtraEvaluateSwitchOutOfTarget.targetPhoneId != -1) {
            int i3 = stabilityEventExtraEvaluateSwitchOutOfTarget.targetPhoneId == i2 ? Integer.MAX_VALUE : stabilityEventExtraEvaluateSwitchOutOfTarget.targetPhoneId;
            this.mSelectedTargetPhoneId = stabilityEventExtraEvaluateSwitchOutOfTarget.targetPhoneId;
            startStabilityCheck(i3, stabilityEventExtraEvaluateSwitchOutOfTarget.switchType, stabilityEventExtraEvaluateSwitchOutOfTarget.needValidation);
            return;
        }
        cancelAnyPendingSwitch();
    }

    private void onEvaluateAutoDataSwitchToTarget(int i, int i2, int i3, StringBuilder sb) {
        Phone phone = PhoneFactory.getPhone(i2);
        if (phone == null || !isActiveModemPhone(i2)) {
            sb.append(" Unexpected null phone ");
            sb.append(i2);
            sb.append(" as the current active data phone");
            logle(sb.toString());
            return;
        }
        if (ignoreCheckingAutoSwitchBack()) {
            return;
        }
        if (!PhoneFactory.getPhone(i3).isUserDataEnabled()) {
            this.mSelectedTargetPhoneId = -1;
            this.mPhoneSwitcherCallback.onRequireImmediatelySwitchToPhone(Integer.MAX_VALUE, 5);
            cancelAnyPendingSwitch();
            sb.append(", immediately back to default as user turns off default data");
            logl(sb.toString());
            return;
        }
        if (autoDdsValueAddedEvaluationforDdsRevert()) {
            this.mSelectedTargetPhoneId = -1;
            this.mPhoneSwitcherCallback.onRequireImmediatelySwitchToPhone(Integer.MAX_VALUE, 5);
            cancelAnyPendingSwitch();
            sb.append(", immediately back to default as additional value added evaluation");
            log(sb.toString());
        } else {
            if (isUserDataSettingEnabledOnNonDDS(phone) && !isAutoSwitchEnabledByUser(phone)) {
                this.mSelectedTargetPhoneId = -1;
                if (i == i3) {
                    i = Integer.MAX_VALUE;
                }
                this.mPhoneSwitcherCallback.onRequireImmediatelySwitchToPhone(i, 5);
                cancelAnyPendingSwitch();
                sb.append(", immediately back to target as auto switch disabled on backup");
                logl(sb.toString());
                return;
            }
            DataEvaluation internetEvaluation = getInternetEvaluation(phone);
            if (!internetEvaluation.isSubsetOf(DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE)) {
                this.mSelectedTargetPhoneId = -1;
                if (i == i3) {
                    i = Integer.MAX_VALUE;
                }
                this.mPhoneSwitcherCallback.onRequireImmediatelySwitchToPhone(i, 5);
                cancelAnyPendingSwitch();
                sb.append(", immediately back to target because backup ");
                sb.append(internetEvaluation);
                logl(sb.toString());
                return;
            }
        }
        StabilityEventExtra stabilityEventExtraEvaluateSwitchToTarget = evaluateSwitchToTarget(i, i2, sb);
        logl(sb.toString());
        if (stabilityEventExtraEvaluateSwitchToTarget != null) {
            int i4 = i != i3 ? i : Integer.MAX_VALUE;
            this.mSelectedTargetPhoneId = i;
            startStabilityCheck(i4, stabilityEventExtraEvaluateSwitchToTarget.switchType, stabilityEventExtraEvaluateSwitchToTarget.needValidation);
            return;
        }
        cancelAnyPendingSwitch();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private StabilityEventExtra evaluateSwitchToTarget(int i, int i2, StringBuilder sb) {
        int i3 = 0;
        Object[] objArr = 0;
        Object[] objArr2 = 0;
        Object[] objArr3 = 0;
        Object[] objArr4 = 0;
        if (this.mDefaultNetworkIsOnNonCellular) {
            sb.append(", back to target as default network");
            sb.append(" is active on nonCellular transport");
            return new StabilityEventExtra(i, i3, objArr4 == true ? 1 : 0);
        }
        PhoneSignalStatus.UsableState usableState = this.mPhonesSignalStatus[i].getUsableState();
        PhoneSignalStatus.UsableState usableState2 = this.mPhonesSignalStatus[i2].getUsableState();
        sb.append(", target phone ");
        sb.append(i);
        sb.append(" : ");
        sb.append(usableState);
        sb.append(" , current phone: ");
        sb.append(usableState2);
        int iComparePhones = comparePhones(i2, i, sb);
        if (iComparePhones > 0) {
            return null;
        }
        char c = 1;
        Object[] objArr5 = usableState2.mScore > PhoneSignalStatus.UsableState.NOT_USABLE.mScore;
        if (iComparePhones < 0) {
            return new StabilityEventExtra(i, objArr3 == true ? 1 : 0, objArr5 == true && this.mRequirePingTestBeforeSwitch);
        }
        if (objArr5 == false) {
            sb.append(", back to target as both phones are unusable.");
            return new StabilityEventExtra(i, 2, objArr2 == true ? 1 : 0);
        }
        if (isRatSignalStrengthBaseSwitchQualified(usableState2, usableState)) {
            return new StabilityEventExtra(i, c == true ? 1 : 0, this.mRequirePingTestBeforeSwitch);
        }
        sb.append(", back to target as it's usable. ");
        return new StabilityEventExtra(i, objArr == true ? 1 : 0, this.mRequirePingTestBeforeSwitch);
    }

    protected boolean autoDdsValueAddedEvaluationforDdsRevert() {
        log("autoDdsValueAddedEvaluationforDdsRevert: false");
        return this.mAutoDdsValueAddedEvaluationforDdsRevert;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v22 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7, types: [int] */
    private StabilityEventExtra evaluateSwitchOutOfTarget(int i, StringBuilder sb) {
        ?? r2;
        Phone phone;
        boolean zIsRatSignalStrengthBaseSwitchQualified;
        Phone phone2 = PhoneFactory.getPhone(i);
        int i2 = 0;
        StabilityEventExtra stabilityEventExtra = new StabilityEventExtra(-1, i2, this.mRequirePingTestBeforeSwitch);
        if (phone2 == null) {
            sb.append(", no candidate as no sim loaded");
            return stabilityEventExtra;
        }
        if (!phone2.isUserDataEnabled()) {
            sb.append(", no candidate as user disabled mobile data");
            return stabilityEventExtra;
        }
        if (this.mDefaultNetworkIsOnNonCellular) {
            sb.append(", no candidate as default network is active");
            sb.append(" on non-cellular transport");
            return stabilityEventExtra;
        }
        if (isStandaloneOpportunistic(i) && !this.mPhonesSignalStatus[i].mListeningForEvents) {
            sb.append(", no candidate as target phone is not listening for events");
            return stabilityEventExtra;
        }
        if ((!isRatSignalStrengthBasedSwitchEnabled() || isPreferredStandaloneOpportunistic(i)) && isHomeService(this.mPhonesSignalStatus[i].mDataRegState)) {
            sb.append(", no candidate as target phone is in HOME service");
            return stabilityEventExtra;
        }
        ?? r0 = 0;
        while (i2 < this.mPhonesSignalStatus.length) {
            if (i2 != i) {
                sb.append(", found phone ");
                sb.append(i2);
                if (comparePhones(i2, i, sb) > 0) {
                    phone = PhoneFactory.getPhone(i2);
                    zIsRatSignalStrengthBaseSwitchQualified = isRatSignalStrengthBaseSwitchQualified(this.mPhonesSignalStatus[i].getUsableState(), this.mPhonesSignalStatus[i2].getUsableState());
                } else {
                    sb.append(" not better than target");
                    r2 = r0 == true ? 1 : 0;
                    phone = null;
                }
                if (phone != null && (!isUserDataSettingEnabledOnNonDDS(phone) || isAutoSwitchEnabledByUser(phone))) {
                    r2 = zIsRatSignalStrengthBaseSwitchQualified;
                    r2 = zIsRatSignalStrengthBaseSwitchQualified;
                    DataEvaluation internetEvaluation = getInternetEvaluation(phone);
                    if (!internetEvaluation.containsDisallowedReasons()) {
                        return new StabilityEventExtra(i2, r2, this.mRequirePingTestBeforeSwitch);
                    }
                    sb.append(", but candidate's data is not allowed ");
                    sb.append(internetEvaluation);
                }
                r2 = zIsRatSignalStrengthBaseSwitchQualified;
                r2 = zIsRatSignalStrengthBaseSwitchQualified;
                r0 = r2;
            }
            i2++;
            r0 = r0;
        }
        sb.append(", found no qualified candidate.");
        return stabilityEventExtra;
    }

    private boolean isPreferredStandaloneOpportunistic(int i) {
        int opportunisticSetDataSubId;
        return isStandaloneOpportunistic(i) && (opportunisticSetDataSubId = this.mPhoneSwitcher.getOpportunisticSetDataSubId()) != Integer.MAX_VALUE && i == this.mSubscriptionManagerService.getPhoneId(opportunisticSetDataSubId);
    }

    private int comparePhones(int i, int i2, StringBuilder sb) {
        PhoneSignalStatus.UsableState usableState = this.mPhonesSignalStatus[i].getUsableState();
        PhoneSignalStatus.UsableState usableState2 = this.mPhonesSignalStatus[i2].getUsableState();
        int i3 = usableState.mScore;
        int i4 = usableState2.mScore;
        if (i3 > i4) {
            return 1;
        }
        if (i3 < i4) {
            return -1;
        }
        if (isRatSignalStrengthBaseSwitchQualified(usableState2, usableState)) {
            int ratSignalScore = this.mPhonesSignalStatus[i2].getRatSignalScore();
            int ratSignalScore2 = this.mPhonesSignalStatus[i].getRatSignalScore();
            int i5 = ratSignalScore2 - ratSignalScore;
            int i6 = this.mScoreTolerance;
            if (i5 > i6) {
                sb.append(" candidate score ");
                sb.append(ratSignalScore2);
                sb.append(" > baseline ");
                sb.append(ratSignalScore);
                return 1;
            }
            if (i5 < (-i6)) {
                sb.append(" candidate score ");
                sb.append(ratSignalScore2);
                sb.append(" < baseline ");
                sb.append(ratSignalScore);
                return -1;
            }
            sb.append(" candidate score ");
            sb.append(ratSignalScore2);
            sb.append(" within tolerance of baseline ");
            sb.append(ratSignalScore);
        }
        return 0;
    }

    private DataEvaluation getInternetEvaluation(Phone phone) {
        NetworkRequest.Builder builderAddCapability = new NetworkRequest.Builder().addCapability(12);
        if (phone.getServiceState().isUsingNonTerrestrialNetwork()) {
            builderAddCapability.removeCapability(13);
        }
        return phone.getDataNetworkController().evaluateNetworkRequest(new TelephonyNetworkRequest(builderAddCapability.build(), phone, sFeatureFlags), DataEvaluation.DataEvaluationReason.EXTERNAL_QUERY);
    }

    private boolean isAvailabilityBasedSwitchEnabled() {
        if (((Long) STABILITY_CHECK_TIMER_MAP.get(0)).longValue() >= 0) {
            return shouldExcludeOpportunisticForSwitch() || isAvailabilityBasedSwitchEnabledForOppt();
        }
        return false;
    }

    private boolean isRatSignalStrengthBasedSwitchEnabled() {
        if (this.mScoreTolerance < 0 || ((Long) STABILITY_CHECK_TIMER_MAP.get(1)).longValue() < 0) {
            return false;
        }
        return shouldExcludeOpportunisticForSwitch() || isPerformanceBasedSwitchEnabledForOppt();
    }

    private boolean isRatSignalStrengthBaseSwitchQualified(PhoneSignalStatus.UsableState usableState, PhoneSignalStatus.UsableState usableState2) {
        PhoneSignalStatus.UsableState usableState3;
        return isRatSignalStrengthBasedSwitchEnabled() && usableState == (usableState3 = PhoneSignalStatus.UsableState.HOME) && usableState2 == usableState3;
    }

    private void startStabilityCheck(int i, int i2, boolean z) {
        long jLongValue;
        int i3 = -1;
        StabilityEventExtra stabilityEventExtra = (StabilityEventExtra) this.mScheduledEventsToExtras.getOrDefault(5, new StabilityEventExtra(i3, i3, false));
        if (stabilityEventExtra.targetPhoneId == i && stabilityEventExtra.needValidation == z && stabilityEventExtra.switchType == i2) {
            jLongValue = -1;
        } else {
            stabilityEventExtra = new StabilityEventExtra(i, i2, z);
            jLongValue = ((Long) STABILITY_CHECK_TIMER_MAP.get(Integer.valueOf(i2))).longValue();
            scheduleEventWithTimer(5, stabilityEventExtra, jLongValue);
        }
        StringBuilder sb = new StringBuilder("startStabilityCheck: ");
        sb.append(jLongValue != -1 ? "scheduling " : "already scheduled ");
        sb.append(stabilityEventExtra);
        logl(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleEventWithTimer$1(int i) {
        sendEmptyMessage(i);
    }

    private void scheduleEventWithTimer(final int i, Object obj, long j) {
        this.mEventsToAlarmListener.putIfAbsent(Integer.valueOf(i), new AlarmManager.OnAlarmListener() { // from class: com.android.internal.telephony.data.AutoDataSwitchController$$ExternalSyntheticLambda3
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$scheduleEventWithTimer$1(i);
            }
        });
        AlarmManager.OnAlarmListener onAlarmListener = (AlarmManager.OnAlarmListener) this.mEventsToAlarmListener.get(Integer.valueOf(i));
        removeMessages(i);
        this.mAlarmManager.cancel(onAlarmListener);
        this.mScheduledEventsToExtras.put(Integer.valueOf(i), obj);
        if (j < RETRY_LONG_DELAY_TIMER_THRESHOLD_MILLIS) {
            sendEmptyMessageDelayed(i, j);
        } else {
            this.mAlarmManager.setExact(3, SystemClock.elapsedRealtime() + j, "ADSC", onAlarmListener, this);
        }
    }

    public static String evaluationReasonToString(int i) {
        switch (i) {
            case 1:
                return "REGISTRATION_STATE_CHANGED";
            case 2:
                return "DISPLAY_INFO_CHANGED";
            case 3:
                return "SIGNAL_STRENGTH_CHANGED";
            case 4:
                return "DEFAULT_NETWORK_CHANGED";
            case 5:
                return "DATA_SETTINGS_CHANGED";
            case 6:
                return "RETRY_VALIDATION";
            case 7:
                return "SIM_LOADED";
            case 8:
                return "VOICE_CALL_END";
            case 9:
                return "CARRIER_CONFIG_CHANGED";
            default:
                return "Unknown(" + i + ")";
        }
    }

    public static String opportunisticNetworkSwitchPolicyToString(int i) {
        if (i == 0) {
            return "DISABLED";
        }
        if (i == 1) {
            return "AVAILABILITY";
        }
        if (i == 2) {
            return "PERFORMANCE";
        }
        if (i == Integer.MAX_VALUE) {
            return "FOLLOW_SYSTEM";
        }
        return "Unknown(" + i + ")";
    }

    private boolean isActiveSubId(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
        return subscriptionInfoInternal != null && subscriptionInfoInternal.isActive();
    }

    private boolean isActiveVisibleSubId(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
        return subscriptionInfoInternal != null && subscriptionInfoInternal.isActive() && subscriptionInfoInternal.isVisible();
    }

    public void updateDefaultNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities != null) {
            boolean zHasTransport = networkCapabilities.hasTransport(0);
            this.mDefaultNetworkIsOnNonCellular = !zHasTransport;
            if (zHasTransport || !isActiveSubId(this.mPhoneSwitcher.getAutoSelectedDataSubId())) {
                return;
            }
            logl("default network is active on non cellular, switch back to default");
            evaluateAutoDataSwitch(4);
            return;
        }
        logl("default network is lost, try to find another active sub to switch to");
        this.mDefaultNetworkIsOnNonCellular = false;
        evaluateAutoDataSwitch(4);
    }

    protected void cancelAnyPendingSwitch() {
        this.mSelectedTargetPhoneId = -1;
        resetFailedCount();
        if (this.mScheduledEventsToExtras.containsKey(5)) {
            if (this.mEventsToAlarmListener.containsKey(5)) {
                this.mAlarmManager.cancel((AlarmManager.OnAlarmListener) this.mEventsToAlarmListener.get(5));
            } else {
                logle("cancelAnyPendingSwitch: EVENT_STABILITY_CHECK_PASSED listener is null");
            }
            removeMessages(5);
            this.mScheduledEventsToExtras.remove(5);
        }
        this.mPhoneSwitcherCallback.onRequireCancelAnyPendingAutoSwitchValidation();
    }

    public void displayAutoDataSwitchNotification(int i, boolean z) {
        NotificationManager notificationManager;
        if (shouldExcludeOpportunisticForSwitch() && (notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class)) != null) {
            if (this.mDisplayedNotification) {
                notificationManager.cancel("auto_data_switch", 1);
                return;
            }
            if (z) {
                SubscriptionManagerService subscriptionManagerService = this.mSubscriptionManagerService;
                SubscriptionInfo subscriptionInfo = subscriptionManagerService.getSubscriptionInfo(subscriptionManagerService.getSubId(i));
                if (subscriptionInfo == null || subscriptionInfo.isOpportunistic()) {
                    logle("displayAutoDataSwitchNotification: phoneId=" + i + " unexpected subInfo " + subscriptionInfo);
                    return;
                }
                int subscriptionId = subscriptionInfo.getSubscriptionId();
                logl("displayAutoDataSwitchNotification: display for subId=" + subscriptionId);
                Intent intent = new Intent("android.settings.MANAGE_ALL_SIM_PROFILES_SETTINGS");
                Bundle bundle = new Bundle();
                bundle.putString(":settings:fragment_args_key", "auto_data_switch");
                intent.putExtra("android.provider.extra.SUB_ID", subscriptionId);
                intent.putExtra(":settings:show_fragment_args", bundle);
                PendingIntent activity = PendingIntent.getActivity(this.mContext, subscriptionId, intent, 67108864);
                String string = this.mContext.getString(R.string.bugreport_status, subscriptionInfo.getDisplayName());
                CharSequence text = this.mContext.getText(R.string.bugreport_screenshot_success_toast);
                notificationManager.notify("auto_data_switch", 1, new Notification.Builder(this.mContext).setContentTitle(string).setContentText(text).setSmallIcon(R.drawable.stat_sys_warning).setColor(this.mContext.getResources().getColor(R.color.system_notification_accent_color)).setChannelId(NotificationChannelController.CHANNEL_ID_MOBILE_DATA_STATUS).setContentIntent(activity).setStyle(new Notification.BigTextStyle().bigText(text)).build());
                this.mDisplayedNotification = true;
            }
        }
    }

    public void resetFailedCount() {
        this.mAutoSwitchValidationFailedCount = 0;
    }

    public void evaluateRetryOnValidationFailed() {
        if (this.mAutoSwitchValidationFailedCount < this.mAutoDataSwitchValidationMaxRetry) {
            evaluateAutoDataSwitch(6);
            this.mAutoSwitchValidationFailedCount++;
        } else {
            logl("evaluateRetryOnValidationFailed: reached max auto switch retry count " + this.mAutoDataSwitchValidationMaxRetry);
            this.mAutoSwitchValidationFailedCount = 0;
        }
    }

    private boolean isActiveModemPhone(int i) {
        return i >= 0 && i < this.mPhonesSignalStatus.length;
    }

    public static String switchTypeToString(int i) {
        if (i == 0) {
            return "AVAILABILITY_SWITCH";
        }
        if (i == 1) {
            return "PERFORMANCE_SWITCH";
        }
        if (i == 2) {
            return "AVAILABILITY_SWITCH_BACK";
        }
        return "Unknown(" + i + ")";
    }

    private boolean shouldExcludeOpportunisticForSwitch() {
        if (getOpptSwitchPolicyForPrimaryPhone() == 0) {
            log("OPPT switch excluded: primary phone doesn't enable the feature!");
            return true;
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManagerService.getActiveSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), true);
        ArraySet arraySet = new ArraySet();
        ArraySet arraySet2 = new ArraySet();
        for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
            if (subscriptionInfo.getGroupUuid() != null) {
                if (subscriptionInfo.isOpportunistic()) {
                    arraySet2.add(subscriptionInfo.getGroupUuid());
                } else {
                    arraySet.add(subscriptionInfo.getGroupUuid());
                }
            }
        }
        arraySet.retainAll(arraySet2);
        return arraySet.isEmpty();
    }

    private PersistableBundle getCarrierConfigForPrimaryPhone() {
        int[] activeSubIdList = this.mSubscriptionManagerService.getActiveSubIdList(true);
        if (this.mCarrierConfigManager == null || activeSubIdList.length != 1) {
            return new PersistableBundle();
        }
        return CarrierConfigManager.getCarrierConfigSubset(this.mContext, activeSubIdList[0], OPP_AUTO_DATA_SWITCH_CARRIER_CONFIG_KEYS);
    }

    private int getOpptSwitchPolicyForPrimaryPhone() {
        return getCarrierConfigForPrimaryPhone().getInt("opp_auto_data_switch_policy_int", 0);
    }

    private boolean isOpptSwitchPolicyEnabled(int i) {
        return (getOpptSwitchPolicyForPrimaryPhone() & i) != 0;
    }

    private boolean isAvailabilityBasedSwitchEnabledForOppt() {
        return isOpptSwitchPolicyEnabled(1);
    }

    private boolean isPerformanceBasedSwitchEnabledForOppt() {
        return isOpptSwitchPolicyEnabled(2);
    }

    private void log(String str) {
        Rlog.d("ADSC", str);
    }

    private void loge(String str) {
        Rlog.e("ADSC", str);
    }

    private void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    private void logle(String str) {
        loge(str);
        this.mLocalLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        final AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("AutoDataSwitchController:");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("mScoreTolerance=" + this.mScoreTolerance);
        androidUtilIndentingPrintWriter.println("mAutoDataSwitchValidationMaxRetry=" + this.mAutoDataSwitchValidationMaxRetry + " mAutoSwitchValidationFailedCount=" + this.mAutoSwitchValidationFailedCount);
        StringBuilder sb = new StringBuilder("mRequirePingTestBeforeDataSwitch=");
        sb.append(this.mRequirePingTestBeforeSwitch);
        androidUtilIndentingPrintWriter.println(sb.toString());
        androidUtilIndentingPrintWriter.println("STABILITY_CHECK_TIMER_MAP:");
        STABILITY_CHECK_TIMER_MAP.forEach(new BiConsumer() { // from class: com.android.internal.telephony.data.AutoDataSwitchController$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                androidUtilIndentingPrintWriter.println(AutoDataSwitchController.switchTypeToString(((Integer) obj).intValue()) + ": " + ((Long) obj2));
            }
        });
        androidUtilIndentingPrintWriter.println("mSelectedTargetPhoneId=" + this.mSelectedTargetPhoneId);
        androidUtilIndentingPrintWriter.println("autoDataSwitchPolicyForOppt=" + opportunisticNetworkSwitchPolicyToString(getOpptSwitchPolicyForPrimaryPhone()));
        androidUtilIndentingPrintWriter.increaseIndent();
        for (PhoneSignalStatus phoneSignalStatus : this.mPhonesSignalStatus) {
            androidUtilIndentingPrintWriter.println(phoneSignalStatus);
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
    }
}
