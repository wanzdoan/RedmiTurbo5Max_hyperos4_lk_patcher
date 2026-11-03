package com.android.internal.telephony.satellite;

import android.R;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.OutcomeReceiver;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.telecom.Connection;
import android.telephony.PersistentLogger;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.RegistrationManager;
import android.telephony.satellite.ISatelliteProvisionStateCallback;
import android.telephony.satellite.SatelliteManager;
import android.telephony.satellite.SatelliteSubscriberProvisionStatus;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SmsApplication;
import com.android.internal.telephony.TelephonyCountryDetector;
import com.android.internal.telephony.metrics.SatelliteStats;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntPredicate;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteSOSMessageRecommender extends Handler {
    protected static final int EVENT_SERVICE_STATE_CHANGED = 2;
    protected static final int EVENT_TIME_OUT = 3;
    private AtomicBoolean mCheckingAccessRestrictionInProgress;
    private final Context mContext;
    protected AtomicInteger mCountOfTimerStarted;
    private final TelephonyCountryDetector mCountryDetector;
    private Connection mEmergencyConnection;
    private AtomicInteger mEmergencyNumberSourceUsedInHandoverIntent;
    private final FeatureFlags mFeatureFlags;
    private final ISatelliteProvisionStateCallback mISatelliteProvisionStateCallback;
    private ImsManager mImsManager;
    private SparseArray mImsRegistrationCallbacks;
    private AtomicBoolean mIsSatelliteAllowedForCurrentLocation;
    protected final AtomicBoolean mIsSatelliteConnectedViaCarrierWithinHysteresisTime;
    private AtomicBoolean mIsTestEmergencyNumber;
    private AtomicBoolean mIsTimerTimedOut;
    private final AtomicLong mOemEnabledTimeoutMillis;
    private PersistentLogger mPersistentLogger;
    private final SatelliteController mSatelliteController;
    protected final AtomicInteger mSubIdOfSatelliteConnectedViaCarrierWithinHysteresisTime;
    private SubscriptionManager mSubscriptionManager;
    protected AtomicLong mTimeoutMillis;

    private boolean shouldTrackCall(int i) {
        return (i == 4 || i == 6) ? false : true;
    }

    public SatelliteSOSMessageRecommender(Context context, Looper looper) {
        this(context, looper, SatelliteController.getInstance(), null);
    }

    protected SatelliteSOSMessageRecommender(Context context, Looper looper, SatelliteController satelliteController, ImsManager imsManager) {
        super(looper);
        this.mPersistentLogger = null;
        this.mIsSatelliteAllowedForCurrentLocation = new AtomicBoolean(false);
        this.mCheckingAccessRestrictionInProgress = new AtomicBoolean(false);
        this.mIsSatelliteConnectedViaCarrierWithinHysteresisTime = new AtomicBoolean(false);
        this.mSubIdOfSatelliteConnectedViaCarrierWithinHysteresisTime = new AtomicInteger(-1);
        this.mTimeoutMillis = new AtomicLong(0L);
        AtomicLong atomicLong = new AtomicLong(0L);
        this.mOemEnabledTimeoutMillis = atomicLong;
        this.mIsTimerTimedOut = new AtomicBoolean(false);
        this.mCountOfTimerStarted = new AtomicInteger(0);
        this.mIsTestEmergencyNumber = new AtomicBoolean(false);
        this.mEmergencyNumberSourceUsedInHandoverIntent = new AtomicInteger(0);
        this.mEmergencyConnection = null;
        this.mImsRegistrationCallbacks = new SparseArray();
        this.mPersistentLogger = SatelliteServiceUtils.getPersistentLogger(context);
        this.mContext = context;
        this.mSatelliteController = satelliteController;
        FeatureFlags featureFlags = satelliteController.getFeatureFlags();
        this.mFeatureFlags = featureFlags;
        this.mCountryDetector = TelephonyCountryDetector.getInstance(context, featureFlags);
        this.mImsManager = imsManager;
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        atomicLong.set(getOemEnabledEmergencyCallWaitForConnectionTimeoutMillis(context));
        this.mISatelliteProvisionStateCallback = new ISatelliteProvisionStateCallback.Stub() { // from class: com.android.internal.telephony.satellite.SatelliteSOSMessageRecommender.1
            public void onSatelliteProvisionStateChanged(boolean z) {
                SatelliteSOSMessageRecommender.this.plogd("onSatelliteProvisionStateChanged: provisioned=" + z);
                SatelliteSOSMessageRecommender satelliteSOSMessageRecommender = SatelliteSOSMessageRecommender.this;
                satelliteSOSMessageRecommender.sendMessage(satelliteSOSMessageRecommender.obtainMessage(4, Boolean.valueOf(z)));
            }

            public void onSatelliteSubscriptionProvisionStateChanged(List<SatelliteSubscriberProvisionStatus> list) {
                SatelliteSOSMessageRecommender.this.plogd("onSatelliteSubscriptionProvisionStateChanged: " + list);
            }
        };
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                handleEmergencyCallStartedEvent((Connection) message.obj);
                break;
            case 2:
                handleStateChangedEventForHysteresisTimer();
                break;
            case 3:
                handleTimeoutEvent();
                break;
            case 4:
                handleSatelliteProvisionStateChangedEvent(((Boolean) message.obj).booleanValue());
                break;
            case 5:
                handleEmergencyCallConnectionStateChangedEvent((Pair) message.obj);
                break;
            case 6:
                handleCmdSendEventDisplayEmergencyMessageForcefully((Connection) message.obj);
                break;
            case 7:
                handleSatelliteAccessRestrictionCheckingResult(((Boolean) message.obj).booleanValue());
                break;
            default:
                plogd("handleMessage: unexpected message code: " + message.what);
                break;
        }
    }

    public void onEmergencyCallStarted(Connection connection, boolean z) {
        if (!isSatelliteSupported()) {
            plogd("onEmergencyCallStarted: satellite is not supported");
            return;
        }
        this.mIsTestEmergencyNumber.set(z);
        if (hasMessages(1)) {
            logd("onEmergencyCallStarted: Ignoring due to ongoing event:");
        } else {
            updateSatelliteConnectedViaCarrierWithinHysteresisTimeState();
            sendMessage(obtainMessage(1, connection));
        }
    }

    public void onEmergencyCallConnectionStateChanged(String str, int i) {
        plogd("callId=" + str + ", state=" + i);
        if (!isSatelliteSupported()) {
            plogd("onEmergencyCallConnectionStateChanged: satellite is not supported");
        } else {
            sendMessage(obtainMessage(5, new Pair(str, Integer.valueOf(i))));
        }
    }

    protected ComponentName getDefaultSmsApp() {
        return SmsApplication.getDefaultSendToApplication(this.mContext, false);
    }

    protected boolean updateAndGetProvisionState() {
        this.mSatelliteController.updateSatelliteProvisionedStatePerSubscriberId();
        return isDeviceProvisioned();
    }

    protected boolean isSatelliteAllowedByReasons() {
        int[] satelliteDisallowedReasons = ((SatelliteManager) this.mContext.getSystemService(SatelliteManager.class)).getSatelliteDisallowedReasons();
        if (!Arrays.stream(satelliteDisallowedReasons).anyMatch(new IntPredicate() { // from class: com.android.internal.telephony.satellite.SatelliteSOSMessageRecommender$$ExternalSyntheticLambda0
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return SatelliteSOSMessageRecommender.$r8$lambda$toKpsHS4Qch5xJf8TS919lktfcg(i);
            }
        })) {
            return true;
        }
        plogd("isAllowedForDefaultMessageApp:false, disallowedReasons=" + Arrays.toString(satelliteDisallowedReasons));
        return false;
    }

    public static /* synthetic */ boolean $r8$lambda$toKpsHS4Qch5xJf8TS919lktfcg(int i) {
        return i == 3 || i == 1 || i == 0;
    }

    private void handleEmergencyCallStartedEvent(Connection connection) {
        plogd("handleEmergencyCallStartedEvent: connection=" + connection);
        this.mSatelliteController.setLastEmergencyCallTime();
        if (sendEventDisplayEmergencyMessageForcefully(connection)) {
            return;
        }
        selectEmergencyCallWaitForConnectionTimeoutDuration();
        if (this.mEmergencyConnection == null) {
            registerForInterestedStateChangedEvents();
        }
        this.mEmergencyConnection = connection;
        handleStateChangedEventForHysteresisTimer();
        this.mCheckingAccessRestrictionInProgress.set(false);
        this.mIsSatelliteAllowedForCurrentLocation.set(false);
    }

    private void handleSatelliteProvisionStateChangedEvent(boolean z) {
        if (z || isSatelliteEmergencyMessagingViaCarrierAvailable()) {
            return;
        }
        cleanUpResources(false);
    }

    private void handleTimeoutEvent() {
        this.mIsTimerTimedOut.set(true);
        evaluateSendingConnectionEventDisplayEmergencyMessage();
    }

    private void evaluateSendingConnectionEventDisplayEmergencyMessage() {
        boolean z;
        if (this.mEmergencyConnection == null) {
            ploge("No emergency call is ongoing...");
            return;
        }
        if (!this.mIsTimerTimedOut.get() || this.mCheckingAccessRestrictionInProgress.get()) {
            plogd("mIsTimerTimedOut=" + this.mIsTimerTimedOut.get() + ", mCheckingAccessRestrictionInProgress=" + this.mCheckingAccessRestrictionInProgress.get());
            return;
        }
        updateAndGetProvisionState();
        updateSatelliteViaCarrierAvailability();
        boolean zIsCellularAvailable = SatelliteServiceUtils.isCellularAvailable();
        boolean zCanMakeWifiCall = canMakeWifiCall();
        plogd("handleTimeoutEvent: isImsRegisteredOverIwlan=" + isImsRegisteredOverIwlan() + ", isCellularAvailable=" + zIsCellularAvailable + ", canMakeWifiCall=" + zCanMakeWifiCall + ", isSatelliteAllowed=" + isSatelliteAllowed() + ", shouldTrackCall=" + shouldTrackCall(this.mEmergencyConnection.getState()));
        if (zIsCellularAvailable || zCanMakeWifiCall || !isSatelliteAllowed() || !(((isDeviceProvisioned() && isSatelliteAllowedByReasons()) || isSatelliteEmergencyMessagingViaCarrierAvailable()) && shouldTrackCall(this.mEmergencyConnection.getState()))) {
            z = false;
        } else {
            plogd("handleTimeoutEvent: Sent EVENT_DISPLAY_EMERGENCY_MESSAGE to Dialer");
            this.mEmergencyConnection.sendConnectionEvent("android.telephony.event.DISPLAY_EMERGENCY_MESSAGE", createExtraBundleForEventDisplayEmergencyMessage(this.mIsTestEmergencyNumber.get()));
            z = true;
        }
        cleanUpResources(z);
    }

    private boolean isSatelliteAllowed() {
        if (isSatelliteEmergencyMessagingViaCarrierAvailable()) {
            return true;
        }
        return this.mIsSatelliteAllowedForCurrentLocation.get();
    }

    private void updateSatelliteViaCarrierAvailability() {
        if (this.mIsSatelliteConnectedViaCarrierWithinHysteresisTime.get()) {
            return;
        }
        updateSatelliteConnectedViaCarrierWithinHysteresisTimeState();
    }

    protected boolean isDeviceProvisioned() {
        Boolean boolIsDeviceProvisioned = this.mSatelliteController.isDeviceProvisioned();
        if (boolIsDeviceProvisioned != null) {
            return boolIsDeviceProvisioned.booleanValue();
        }
        return false;
    }

    private boolean isSatelliteEmergencyMessagingViaCarrierAvailable() {
        if (!this.mSatelliteController.isSatelliteEmergencyMessagingSupportedViaCarrier()) {
            plogd("isSatelliteEmergencyMessagingViaCarrierAvailable: false, device does not support satellite emergency messaging via carrier");
            return false;
        }
        return this.mIsSatelliteConnectedViaCarrierWithinHysteresisTime.get();
    }

    private void handleEmergencyCallConnectionStateChangedEvent(Pair pair) {
        this.mSatelliteController.setLastEmergencyCallTime();
        if (this.mEmergencyConnection == null) {
            return;
        }
        String str = (String) pair.first;
        int iIntValue = ((Integer) pair.second).intValue();
        if (!this.mEmergencyConnection.getTelecomCallId().equals(str)) {
            ploge("handleEmergencyCallConnectionStateChangedEvent: unexpected state changed event , mEmergencyConnection=" + this.mEmergencyConnection + ", callId=" + str + ", state=" + iIntValue);
            cleanUpResources(false);
            return;
        }
        if (!shouldTrackCall(iIntValue)) {
            cleanUpResources(false);
        } else if (iIntValue == 3 && this.mSatelliteController.isSatelliteSupportedViaOem()) {
            requestIsSatelliteAllowedForCurrentLocation();
        }
    }

    protected void reportESosRecommenderDecision(boolean z) {
        Pair<Integer, Integer> emergencyCallToSatelliteHandoverType = getEmergencyCallToSatelliteHandoverType();
        int iIntValue = ((Integer) emergencyCallToSatelliteHandoverType.first).intValue();
        SatelliteStats.getInstance().onSatelliteSosMessageRecommender(new SatelliteStats.SatelliteSosMessageRecommenderParams.Builder().setDisplaySosMessageSent(z).setCountOfTimerStarted(this.mCountOfTimerStarted.get()).setImsRegistered(isImsRegisteredOverIwlan()).setCellularServiceState(getBestCellularServiceState()).setIsMultiSim(isMultiSim()).setRecommendingHandoverType(((Integer) emergencyCallToSatelliteHandoverType.second).intValue()).setIsSatelliteAllowedInCurrentLocation(isSatelliteAllowed()).setIsWifiConnected(this.mCountryDetector.isWifiNetworkConnected()).setCarrierId(SatelliteServiceUtils.getCarrierIdFromSubscription(iIntValue)).setSupportedConnectionMode(this.mSatelliteController.getSupportedConnectTypeMetrics()).setSessionConnectionMode(this.mSatelliteController.getSessionConnectTypeMetrics()).setPlmn(this.mSatelliteController.getSatellitePlmnForMetrics()).setIsNtnOnlyCarrier(SatelliteServiceUtils.isNtnOnlySubscriptionId(iIntValue)).setIsInCarrierRoamingNtnMode(isSatelliteEmergencyMessagingViaCarrierAvailable()).setCarrierRoamingSatelliteEmergencyMessagingProvider(this.mSatelliteController.getCarrierRoamingSatelliteEmergencyMessagingProviderForCurrentRegion(iIntValue)).setEmergencyNumberSourceUsedInHandoverIntent(this.mEmergencyNumberSourceUsedInHandoverIntent.get()).build());
        if (z) {
            this.mSatelliteController.onEmergencyDialerButtonDisplayed(iIntValue);
        }
    }

    private void cleanUpResources(boolean z) {
        plogd("cleanUpResources");
        reportESosRecommenderDecision(z);
        stopTimer();
        if (this.mEmergencyConnection != null) {
            unregisterForInterestedStateChangedEvents();
        }
        this.mEmergencyConnection = null;
        this.mCountOfTimerStarted.set(0);
        this.mIsTimerTimedOut.set(false);
        this.mCheckingAccessRestrictionInProgress.set(false);
        this.mIsSatelliteAllowedForCurrentLocation.set(false);
        this.mIsTestEmergencyNumber.set(false);
        this.mEmergencyNumberSourceUsedInHandoverIntent.set(0);
    }

    private void registerForInterestedStateChangedEvents() {
        this.mSatelliteController.registerForSatelliteProvisionStateChanged(this.mISatelliteProvisionStateCallback);
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.registerForServiceStateChanged(this, 2, null);
        }
    }

    private void unregisterForInterestedStateChangedEvents() {
        this.mSatelliteController.unregisterForSatelliteProvisionStateChanged(this.mISatelliteProvisionStateCallback);
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.unregisterForServiceStateChanged(this);
        }
    }

    private int getBestCellularServiceState() {
        boolean z = true;
        for (Phone phone : PhoneFactory.getPhones()) {
            ServiceState serviceState = phone.getServiceState();
            if (serviceState != null) {
                int state = serviceState.getState();
                if (serviceState.isUsingNonTerrestrialNetwork()) {
                    continue;
                } else {
                    if (state == 0) {
                        return 0;
                    }
                    if (state == 2) {
                        z = false;
                    }
                }
            }
        }
        return z ? 1 : 2;
    }

    private boolean isImsRegisteredOverIwlan() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.isImsRegistered() && phone.getImsRegistrationTech() == 1) {
                return true;
            }
        }
        return false;
    }

    private synchronized void handleStateChangedEventForHysteresisTimer() {
        try {
            if (!SatelliteServiceUtils.isCellularAvailable() && this.mEmergencyConnection != null) {
                startTimer();
            } else {
                plogd("handleStateChangedEventForHysteresisTimer stopTimer, mEmergencyConnection=" + this.mEmergencyConnection);
                stopTimer();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void startTimer() {
        if (hasMessages(3)) {
            return;
        }
        sendMessageDelayed(obtainMessage(3), this.mTimeoutMillis.get());
        int iIncrementAndGet = this.mCountOfTimerStarted.incrementAndGet();
        this.mIsTimerTimedOut.set(false);
        plogd("startTimer mCountOfTimerStarted=" + iIncrementAndGet);
    }

    private void stopTimer() {
        removeMessages(3);
    }

    private void handleSatelliteAccessRestrictionCheckingResult(boolean z) {
        this.mIsSatelliteAllowedForCurrentLocation.set(z);
        this.mCheckingAccessRestrictionInProgress.set(false);
        evaluateSendingConnectionEventDisplayEmergencyMessage();
    }

    private void selectEmergencyCallWaitForConnectionTimeoutDuration() {
        if (this.mSatelliteController.getEmergencyCallMonitoringDurationMillisForCtsTests() > 0) {
            this.mTimeoutMillis.set(this.mSatelliteController.getEmergencyCallMonitoringDurationMillisForCtsTests());
            plogd("Use the overridden timeout value for CTS tests");
        } else if (isSatelliteEmergencyMessagingViaCarrierAvailable()) {
            this.mTimeoutMillis.set(this.mSatelliteController.getCarrierEmergencyCallWaitForConnectionTimeoutMillis(this.mSubIdOfSatelliteConnectedViaCarrierWithinHysteresisTime.get()));
        } else {
            int selectedSatelliteSubId = this.mSatelliteController.getSelectedSatelliteSubId();
            boolean zIsNtnOnlySubscriptionId = SatelliteServiceUtils.isNtnOnlySubscriptionId(selectedSatelliteSubId);
            AtomicLong atomicLong = this.mTimeoutMillis;
            if (!zIsNtnOnlySubscriptionId) {
                atomicLong.set(this.mSatelliteController.getCarrierEmergencyCallWaitForConnectionTimeoutMillis(selectedSatelliteSubId));
            } else {
                atomicLong.set(this.mOemEnabledTimeoutMillis.get());
            }
        }
        plogd("mTimeoutMillis = " + this.mTimeoutMillis.get());
    }

    private static long getOemEnabledEmergencyCallWaitForConnectionTimeoutMillis(Context context) {
        return context.getResources().getInteger(R.integer.config_lidNavigationAccessibility);
    }

    private static Pair getOemEnabledSatelliteHandoverAppFromOverlayConfig(Context context) {
        String string;
        try {
            string = context.getResources().getString(R.string.default_audio_route_name_usb);
        } catch (Resources.NotFoundException e) {
            loge("getOemEnabledSatelliteHandoverAppFromOverlayConfig: ex=" + e);
            string = null;
        }
        if (TextUtils.isEmpty(string) && isMockModemAllowed()) {
            logd("getOemEnabledSatelliteHandoverAppFromOverlayConfig: Read config_oem_enabled_satellite_sos_handover_app from device config");
            string = DeviceConfig.getString("telephony", "config_oem_enabled_satellite_sos_handover_app", "");
        }
        if (TextUtils.isEmpty(string)) {
            return new Pair("", "");
        }
        String[] strArrSplit = string.split(";");
        if (strArrSplit.length == 2) {
            return new Pair(strArrSplit[0], strArrSplit[1]);
        }
        loge("getOemEnabledSatelliteHandoverAppFromOverlayConfig: invalid configured app=".concat(string));
        return new Pair("", "");
    }

    private static String getSatelliteEmergencyHandoverIntentActionFromOverlayConfig(Context context, boolean z) {
        String string;
        try {
            string = context.getResources().getString(z ? R.string.dump_heap_system_text : R.string.dream_accessibility_action_click);
        } catch (Resources.NotFoundException e) {
            loge("getSatelliteEmergencyHandoverIntentFilterActionFromOverlayConfig: ex=" + e);
            string = null;
        }
        if (!TextUtils.isEmpty(string) || !isMockModemAllowed()) {
            return string;
        }
        logd("getSatelliteEmergencyHandoverIntentActionFromOverlayConfig: Read config_satellite_emergency_handover_intent_action from device config");
        return DeviceConfig.getString("telephony", "config_satellite_emergency_handover_intent_action", (String) null);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteSOSMessageRecommender$2, reason: invalid class name */
    class AnonymousClass2 extends RegistrationManager.RegistrationCallback {
        final /* synthetic */ SatelliteSOSMessageRecommender this$0;

        @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
        public void onRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
            SatelliteSOSMessageRecommender satelliteSOSMessageRecommender = this.this$0;
            satelliteSOSMessageRecommender.sendMessage(satelliteSOSMessageRecommender.obtainMessage(2));
        }

        @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
        public void onUnregistered(ImsReasonInfo imsReasonInfo) {
            SatelliteSOSMessageRecommender satelliteSOSMessageRecommender = this.this$0;
            satelliteSOSMessageRecommender.sendMessage(satelliteSOSMessageRecommender.obtainMessage(2));
        }
    }

    private Bundle createExtraBundleForEventDisplayEmergencyMessage(boolean z) {
        int iIntValue = ((Integer) getEmergencyCallToSatelliteHandoverType().second).intValue();
        Pair oemEnabledSatelliteHandoverAppFromOverlayConfig = getOemEnabledSatelliteHandoverAppFromOverlayConfig(this.mContext);
        String packageName = (String) oemEnabledSatelliteHandoverAppFromOverlayConfig.first;
        String className = (String) oemEnabledSatelliteHandoverAppFromOverlayConfig.second;
        String satelliteEmergencyHandoverIntentActionFromOverlayConfig = getSatelliteEmergencyHandoverIntentActionFromOverlayConfig(this.mContext, z);
        int enforcedEmergencyCallToSatelliteHandoverType = this.mSatelliteController.getEnforcedEmergencyCallToSatelliteHandoverType();
        if (enforcedEmergencyCallToSatelliteHandoverType != -1) {
            plogd("createExtraBundleForEventDisplayEmergencyMessage: use the overridden handoverType=" + enforcedEmergencyCallToSatelliteHandoverType);
            iIntValue = enforcedEmergencyCallToSatelliteHandoverType;
        }
        if (iIntValue == 2) {
            ComponentName defaultSmsApp = getDefaultSmsApp();
            packageName = defaultSmsApp.getPackageName();
            className = defaultSmsApp.getClassName();
        }
        plogd("EVENT_DISPLAY_EMERGENCY_MESSAGE: handoverType=" + iIntValue + ", packageName=" + packageName + ", className=" + className + ", action=" + satelliteEmergencyHandoverIntentActionFromOverlayConfig);
        Bundle bundle = new Bundle();
        bundle.putInt("android.telephony.extra.EMERGENCY_CALL_TO_SATELLITE_HANDOVER_TYPE", iIntValue);
        if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(className)) {
            bundle.putParcelable("android.telephony.extra.EMERGENCY_CALL_TO_SATELLITE_LAUNCH_INTENT", createHandoverAppLaunchPendingIntent(iIntValue, packageName, className, satelliteEmergencyHandoverIntentActionFromOverlayConfig));
        }
        return bundle;
    }

    private PendingIntent createHandoverAppLaunchPendingIntent(int i, String str, String str2, String str3) {
        Intent intent;
        int slotIndex;
        if (i == 2) {
            String emergencyRedirectionNumberForCurrentRegion = getEmergencyRedirectionNumberForCurrentRegion(this.mSubIdOfSatelliteConnectedViaCarrierWithinHysteresisTime.get());
            if (!TextUtils.isEmpty(emergencyRedirectionNumberForCurrentRegion)) {
                plogd("Text to emergencyRedirectionNumber=" + emergencyRedirectionNumberForCurrentRegion);
                this.mEmergencyNumberSourceUsedInHandoverIntent.set(2);
            } else {
                Connection connection = this.mEmergencyConnection;
                if (connection != null && connection.getAddress() != null) {
                    plogd("Text to user dialed emergency number");
                    emergencyRedirectionNumberForCurrentRegion = this.mEmergencyConnection.getAddress().getSchemeSpecificPart();
                    this.mEmergencyNumberSourceUsedInHandoverIntent.set(1);
                } else {
                    emergencyRedirectionNumberForCurrentRegion = "911";
                }
            }
            intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + emergencyRedirectionNumberForCurrentRegion));
            if (this.mSatelliteController.getEnforcedEmergencyCallToSatelliteHandoverType() == 2) {
                slotIndex = this.mSatelliteController.getSimSlotIdForLaunchingT911ConversationThread();
                plogd("Use the overridden simSlotId=" + slotIndex);
            } else {
                slotIndex = SubscriptionManager.getSlotIndex(this.mSubIdOfSatelliteConnectedViaCarrierWithinHysteresisTime.get());
            }
            if (slotIndex != -1) {
                intent.putExtra("simSlot", slotIndex);
                plogd("emergencyNumber=" + emergencyRedirectionNumberForCurrentRegion + ", EXTRA_SIM_SLOT_ID=" + intent.getIntExtra("simSlot", -1));
            } else {
                plogd("emergencyNumber=" + emergencyRedirectionNumberForCurrentRegion);
            }
        } else {
            intent = new Intent(str3);
            intent.addFlags(67108864);
        }
        Bundle bundle = ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(1).toBundle();
        intent.setComponent(new ComponentName(str, str2));
        plogd("Launching intent: intent=" + intent);
        return PendingIntent.getActivity(this.mContext, 0, intent, 1140850688, bundle);
    }

    private boolean sendEventDisplayEmergencyMessageForcefully(Connection connection) {
        if (this.mSatelliteController.getEnforcedEmergencyCallToSatelliteHandoverType() == -1) {
            return false;
        }
        sendMessageDelayed(obtainMessage(6, connection), ((long) this.mSatelliteController.getDelayInSendingEventDisplayEmergencyMessage()) * 1000);
        return true;
    }

    private void handleCmdSendEventDisplayEmergencyMessageForcefully(Connection connection) {
        plogd("Sent EVENT_DISPLAY_EMERGENCY_MESSAGE to Dialer forcefully.");
        this.mEmergencyConnection = connection;
        connection.sendConnectionEvent("android.telephony.event.DISPLAY_EMERGENCY_MESSAGE", createExtraBundleForEventDisplayEmergencyMessage(true));
        this.mEmergencyConnection = null;
    }

    private boolean isMultiSim() {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        if (telephonyManager == null) {
            ploge("isMultiSim: telephonyManager is null");
            return false;
        }
        return telephonyManager.isMultiSimEnabled();
    }

    protected Pair<Integer, Integer> getEmergencyCallToSatelliteHandoverType() {
        int selectedSatelliteSubId;
        int carrierRoamingNtnEmergencyCallToSatelliteHandoverType;
        if (isSatelliteEmergencyMessagingViaCarrierAvailable()) {
            selectedSatelliteSubId = this.mSubIdOfSatelliteConnectedViaCarrierWithinHysteresisTime.get();
            carrierRoamingNtnEmergencyCallToSatelliteHandoverType = this.mSatelliteController.getCarrierRoamingNtnEmergencyCallToSatelliteHandoverType(selectedSatelliteSubId);
        } else {
            selectedSatelliteSubId = this.mSatelliteController.getSelectedSatelliteSubId();
            carrierRoamingNtnEmergencyCallToSatelliteHandoverType = !SatelliteServiceUtils.isNtnOnlySubscriptionId(selectedSatelliteSubId) ? this.mSatelliteController.getCarrierRoamingNtnEmergencyCallToSatelliteHandoverType(selectedSatelliteSubId) : 1;
        }
        plogd("getEmergencyCallToSatelliteHandoverType: satelliteSubId=" + selectedSatelliteSubId + " handoverType=" + carrierRoamingNtnEmergencyCallToSatelliteHandoverType);
        return new Pair<>(Integer.valueOf(selectedSatelliteSubId), Integer.valueOf(carrierRoamingNtnEmergencyCallToSatelliteHandoverType));
    }

    private void requestIsSatelliteAllowedForCurrentLocation() {
        if (this.mCheckingAccessRestrictionInProgress.get()) {
            plogd("requestIsSatelliteCommunicationAllowedForCurrentLocation was already sent");
        } else {
            this.mCheckingAccessRestrictionInProgress.set(true);
            requestIsSatelliteCommunicationAllowedForCurrentLocation(new OutcomeReceiver<Boolean, SatelliteManager.SatelliteException>() { // from class: com.android.internal.telephony.satellite.SatelliteSOSMessageRecommender.3
                @Override // android.os.OutcomeReceiver
                public void onResult(Boolean bool) {
                    SatelliteSOSMessageRecommender.this.plogd("requestIsSatelliteAllowedForCurrentLocation: result=" + bool);
                    SatelliteSOSMessageRecommender satelliteSOSMessageRecommender = SatelliteSOSMessageRecommender.this;
                    satelliteSOSMessageRecommender.sendMessage(satelliteSOSMessageRecommender.obtainMessage(7, bool));
                }

                @Override // android.os.OutcomeReceiver
                public void onError(SatelliteManager.SatelliteException satelliteException) {
                    SatelliteSOSMessageRecommender.this.plogd("requestIsSatelliteAllowedForCurrentLocation: onError, ex=" + satelliteException);
                    SatelliteSOSMessageRecommender satelliteSOSMessageRecommender = SatelliteSOSMessageRecommender.this;
                    satelliteSOSMessageRecommender.sendMessage(satelliteSOSMessageRecommender.obtainMessage(7, Boolean.FALSE));
                }
            });
        }
    }

    protected void requestIsSatelliteCommunicationAllowedForCurrentLocation(OutcomeReceiver<Boolean, SatelliteManager.SatelliteException> outcomeReceiver) {
        ((SatelliteManager) this.mContext.getSystemService(SatelliteManager.class)).requestIsCommunicationAllowedForCurrentLocation(new Executor() { // from class: com.android.internal.telephony.satellite.SatelliteSOSMessageRecommender$$ExternalSyntheticLambda1
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }, outcomeReceiver);
    }

    private static boolean isMockModemAllowed() {
        return SystemProperties.getBoolean("persist.radio.allow_mock_modem", true) || SystemProperties.getBoolean("ro.boot.radio.allow_mock_modem", false);
    }

    private boolean isSatelliteSupported() {
        if (this.mSatelliteController.isSatelliteEmergencyMessagingSupportedViaCarrier()) {
            return true;
        }
        return this.mSatelliteController.isSatelliteSupportedViaOem() && isSatelliteViaOemProvisioned();
    }

    private boolean isSatelliteViaOemProvisioned() {
        Boolean boolIsDeviceProvisioned = this.mSatelliteController.isDeviceProvisioned();
        return boolIsDeviceProvisioned != null && boolIsDeviceProvisioned.booleanValue();
    }

    private void updateSatelliteConnectedViaCarrierWithinHysteresisTimeState() {
        Pair<Boolean, Integer> pairIsSatelliteConnectedViaCarrierWithinHysteresisTime = this.mSatelliteController.isSatelliteConnectedViaCarrierWithinHysteresisTime();
        plogd("updateSatelliteConnectedViaCarrierWithinHysteresisTimeState: subId=" + pairIsSatelliteConnectedViaCarrierWithinHysteresisTime.second + ", connected=" + pairIsSatelliteConnectedViaCarrierWithinHysteresisTime.first);
        this.mIsSatelliteConnectedViaCarrierWithinHysteresisTime.set(((Boolean) pairIsSatelliteConnectedViaCarrierWithinHysteresisTime.first).booleanValue());
        boolean zBooleanValue = ((Boolean) pairIsSatelliteConnectedViaCarrierWithinHysteresisTime.first).booleanValue();
        AtomicInteger atomicInteger = this.mSubIdOfSatelliteConnectedViaCarrierWithinHysteresisTime;
        if (zBooleanValue) {
            atomicInteger.set(((Integer) pairIsSatelliteConnectedViaCarrierWithinHysteresisTime.second).intValue());
        } else {
            atomicInteger.set(-1);
        }
    }

    private boolean canMakeWifiCall() {
        return isImsRegisteredOverIwlan() && this.mSatelliteController.isWifiConnected();
    }

    private String getEmergencyRedirectionNumberForCurrentRegion(int i) {
        String carrierRoamingSatelliteEmergencyMessagingRedirectionDestination;
        int carrierRoamingSatelliteEmergencyMessagingProviderForCurrentRegion = this.mSatelliteController.getCarrierRoamingSatelliteEmergencyMessagingProviderForCurrentRegion(i);
        if (carrierRoamingSatelliteEmergencyMessagingProviderForCurrentRegion != 0 && carrierRoamingSatelliteEmergencyMessagingProviderForCurrentRegion != 3) {
            carrierRoamingSatelliteEmergencyMessagingRedirectionDestination = "";
        } else {
            carrierRoamingSatelliteEmergencyMessagingRedirectionDestination = this.mSatelliteController.getCarrierRoamingSatelliteEmergencyMessagingRedirectionDestination(i);
        }
        plogd("getEmergencyRedirectionNumberForCurrentRegion: subId=" + i + ", emergencyMessagingProvider=" + carrierRoamingSatelliteEmergencyMessagingProviderForCurrentRegion + ", emergencyRedirectionNumber=" + carrierRoamingSatelliteEmergencyMessagingRedirectionDestination);
        return carrierRoamingSatelliteEmergencyMessagingRedirectionDestination;
    }

    private static void logd(String str) {
        Log.d("SatelliteSOSMessageRecommender", str);
    }

    private static void loge(String str) {
        Log.e("SatelliteSOSMessageRecommender", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void plogd(String str) {
        Log.d("SatelliteSOSMessageRecommender", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("SatelliteSOSMessageRecommender", str);
        }
    }

    private void ploge(String str) {
        Log.e("SatelliteSOSMessageRecommender", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.error("SatelliteSOSMessageRecommender", str);
        }
    }
}
