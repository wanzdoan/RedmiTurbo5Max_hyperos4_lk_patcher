package com.android.internal.telephony.data;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.metrics.DataStallRecoveryStats;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class DataStallRecoveryManager extends Handler {
    public static final int RECOVERY_ACTION_CLEANUP = 1;
    public static final int RECOVERY_ACTION_GET_DATA_CALL_LIST = 0;
    public static final int RECOVERY_ACTION_RADIO_RESTART = 3;
    public static final int RECOVERY_ACTION_RESET_MODEM = 4;
    private int mActionValidationCount;
    private int mActiveDataSubId;
    private ActiveDataSubscriptionIdChangedCallback mActiveDataSubscriptionIdChangedCallback;
    private final ContentObserver mContentObserver;
    private final DataConfigManager mDataConfigManager;
    private final DataNetworkController mDataNetworkController;
    private long[] mDataStallRecoveryDelayMillisArray;
    private final DataStallRecoveryManagerCallback mDataStallRecoveryManagerCallback;
    private long[] mDataStallRecoveryRandomizedMillisArray;
    public long mDataStallStartMs;
    private boolean mDataStalled;
    private final FeatureFlags mFeatureFlags;
    private boolean mIsAirPlaneModeEnableDuringDataStall;
    private boolean mIsAttemptedAllSteps;
    private boolean mIsInternetNetworkConnected;
    private boolean mIsValidNetwork;
    private int mLastAction;
    private boolean mLastActionReported;
    private final LocalLog mLocalLog;
    private final String mLogTag;
    private boolean mMobileDataChangedToEnabledDuringDataStall;
    private boolean mNetworkCheckTimerStarted;
    private final Phone mPhone;
    private long mPredictWaitingMillis;
    private int mRadioPowerState;
    private boolean mRadioStateChangedDuringDataStall;
    private int mRecoveryAction;
    private boolean mRecoveryTriggered;
    private boolean[] mSkipRecoveryActionArray;
    private final DataStallRecoveryStats mStats;
    private final TelephonyManager mTelephonyManager;
    private long mTimeElapsedOfCurrentAction;
    private long mTimeLastRecoveryStartMs;
    private int mValidationCount;
    private final DataServiceManager mWwanDataServiceManager;
    private static final Uri CONTENT_URL_DSRM_ENABLED_ACTIONS = Settings.Global.getUriFor("dsrm_enabled_actions");
    private static final Uri CONTENT_URL_DSRM_DURATION_MILLIS = Settings.Global.getUriFor("dsrm_duration_millis");

    private void logv(String str) {
    }

    public static abstract class DataStallRecoveryManagerCallback extends DataCallback {
        public abstract void onDataStallReestablishInternet();

        public DataStallRecoveryManagerCallback(Executor executor) {
            super(executor);
        }
    }

    private static abstract class ActiveDataSubscriptionIdChangedCallback extends TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener {
        public abstract void onActiveDataSubscriptionIdChanged(int i);

        private ActiveDataSubscriptionIdChangedCallback() {
        }
    }

    public DataStallRecoveryManager(Phone phone, DataNetworkController dataNetworkController, DataServiceManager dataServiceManager, FeatureFlags featureFlags, Looper looper, DataStallRecoveryManagerCallback dataStallRecoveryManagerCallback) {
        super(looper);
        this.mLocalLog = new LocalLog(128);
        this.mRecoveryTriggered = false;
        this.mNetworkCheckTimerStarted = false;
        this.mActiveDataSubId = -1;
        this.mPredictWaitingMillis = 0L;
        this.mContentObserver = new ContentObserver(this) { // from class: com.android.internal.telephony.data.DataStallRecoveryManager.5
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                super.onChange(z, uri);
                if (DataStallRecoveryManager.CONTENT_URL_DSRM_ENABLED_ACTIONS.equals(uri)) {
                    DataStallRecoveryManager.this.log("onChange URI: " + uri);
                    DataStallRecoveryManager dataStallRecoveryManager = DataStallRecoveryManager.this;
                    dataStallRecoveryManager.sendMessage(dataStallRecoveryManager.obtainMessage(4));
                    return;
                }
                if (DataStallRecoveryManager.CONTENT_URL_DSRM_DURATION_MILLIS.equals(uri)) {
                    DataStallRecoveryManager.this.log("onChange URI: " + uri);
                    DataStallRecoveryManager dataStallRecoveryManager2 = DataStallRecoveryManager.this;
                    dataStallRecoveryManager2.sendMessage(dataStallRecoveryManager2.obtainMessage(5));
                }
            }
        };
        this.mPhone = phone;
        this.mLogTag = "DSRM-" + phone.getPhoneId();
        log("DataStallRecoveryManager created.");
        this.mDataNetworkController = dataNetworkController;
        this.mWwanDataServiceManager = dataServiceManager;
        this.mFeatureFlags = featureFlags;
        this.mDataConfigManager = dataNetworkController.getDataConfigManager();
        dataNetworkController.getDataSettingsManager().registerCallback(new DataSettingsManager.DataSettingsManagerCallback(new DataStallRecoveryManager$$ExternalSyntheticLambda0(this)) { // from class: com.android.internal.telephony.data.DataStallRecoveryManager.1
            @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
            public void onDataEnabledChanged(boolean z, int i, String str) {
                DataStallRecoveryManager.this.onMobileDataEnabledChanged(z);
            }
        });
        this.mDataStallRecoveryManagerCallback = dataStallRecoveryManagerCallback;
        this.mRadioPowerState = phone.getRadioPowerState();
        this.mTelephonyManager = (TelephonyManager) phone.getContext().getSystemService(TelephonyManager.class);
        updateDataStallRecoveryConfigs();
        registerAllEvents();
        this.mStats = new DataStallRecoveryStats(phone, featureFlags, dataNetworkController);
    }

    private void registerAllEvents() {
        this.mDataConfigManager.registerCallback(new DataConfigManager.DataConfigManagerCallback(new DataStallRecoveryManager$$ExternalSyntheticLambda0(this)) { // from class: com.android.internal.telephony.data.DataStallRecoveryManager.2
            @Override // com.android.internal.telephony.data.DataConfigManager.DataConfigManagerCallback
            public void onCarrierConfigChanged() {
                DataStallRecoveryManager.this.onCarrierConfigUpdated();
            }
        });
        this.mDataNetworkController.registerDataNetworkControllerCallback(new AnonymousClass3(new DataStallRecoveryManager$$ExternalSyntheticLambda0(this)));
        this.mPhone.mCi.registerForRadioStateChanged(this, 3, null);
        this.mPhone.getContext().getContentResolver().registerContentObserver(CONTENT_URL_DSRM_ENABLED_ACTIONS, false, this.mContentObserver);
        this.mPhone.getContext().getContentResolver().registerContentObserver(CONTENT_URL_DSRM_DURATION_MILLIS, false, this.mContentObserver);
        this.mActiveDataSubscriptionIdChangedCallback = new ActiveDataSubscriptionIdChangedCallback() { // from class: com.android.internal.telephony.data.DataStallRecoveryManager.4
            @Override // com.android.internal.telephony.data.DataStallRecoveryManager.ActiveDataSubscriptionIdChangedCallback, android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
            public void onActiveDataSubscriptionIdChanged(int i) {
                DataStallRecoveryManager.this.mActiveDataSubId = i;
            }
        };
        this.mTelephonyManager.registerTelephonyCallback(new DataStallRecoveryManager$$ExternalSyntheticLambda0(this), this.mActiveDataSubscriptionIdChangedCallback);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.data.DataStallRecoveryManager$3, reason: invalid class name */
    class AnonymousClass3 extends DataNetworkController.DataNetworkControllerCallback {
        AnonymousClass3(Executor executor) {
            super(executor);
        }

        @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
        public void onInternetDataNetworkValidationStatusChanged(int i) {
            DataStallRecoveryManager.this.onInternetValidationStatusChanged(i);
        }

        @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
        public void onConnectedInternetDataNetworksChanged(Set<DataNetwork> set) {
            String str;
            boolean zAnyMatch = set.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataStallRecoveryManager$3$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataStallRecoveryManager.AnonymousClass3.$r8$lambda$BD4TExe6Z7O6h1RW2NGtIOJ1ZJQ((DataNetwork) obj);
                }
            });
            if (DataStallRecoveryManager.this.mIsInternetNetworkConnected != zAnyMatch) {
                DataStallRecoveryManager.this.mIsInternetNetworkConnected = zAnyMatch;
                DataStallRecoveryManager dataStallRecoveryManager = DataStallRecoveryManager.this;
                if (dataStallRecoveryManager.mIsInternetNetworkConnected) {
                    str = "At Least One InternetDataNetwork Connected";
                } else {
                    str = "All InternetDataNetwork Disconnected";
                }
                dataStallRecoveryManager.logl(str);
            }
        }

        public static /* synthetic */ boolean $r8$lambda$BD4TExe6Z7O6h1RW2NGtIOJ1ZJQ(DataNetwork dataNetwork) {
            NetworkCapabilities networkCapabilities = dataNetwork.getNetworkCapabilities();
            return networkCapabilities.hasCapability(13) && networkCapabilities.hasCapability(14) && networkCapabilities.hasCapability(15);
        }

        @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
        public void onSimStateChanged(int i) {
            if (i == 1) {
                DataStallRecoveryManager.this.log("SIM state is ABSENT, reset DSRM.");
                DataStallRecoveryManager.this.reset(true);
            }
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        logv("handleMessage = " + message);
        int i = message.what;
        if (i == 1) {
            this.mRecoveryTriggered = true;
            if (!isRecoveryNeeded(false)) {
                cancelNetworkCheckTimer();
                startNetworkCheckTimer(getRecoveryAction());
                return;
            } else {
                broadcastDataStallDetected(getRecoveryAction());
                sendMessageDelayed(obtainMessage(2), this.mPredictWaitingMillis);
                return;
            }
        }
        if (i == 2) {
            doRecovery();
            return;
        }
        if (i == 3) {
            this.mRadioPowerState = this.mPhone.getRadioPowerState();
            if (this.mDataStalled) {
                this.mRadioStateChangedDuringDataStall = true;
                if (Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), "airplane_mode_on", 0) != 0) {
                    this.mIsAirPlaneModeEnableDuringDataStall = true;
                }
                setRecoveryAction(this.mLastAction);
                return;
            }
            return;
        }
        if (i == 4) {
            updateGlobalConfigActions();
        } else {
            if (i == 5) {
                updateGlobalConfigDurations();
                return;
            }
            loge("Unexpected message = " + message);
        }
    }

    public ContentObserver getContentObserver() {
        return this.mContentObserver;
    }

    private void updateGlobalConfigActions() {
        String string = Settings.Global.getString(this.mPhone.getContext().getContentResolver(), "dsrm_enabled_actions");
        if (!TextUtils.isEmpty(string)) {
            String[] strArrSplit = string.split(",");
            int length = strArrSplit.length;
            boolean[] zArr = new boolean[length];
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                zArr[i2] = Boolean.parseBoolean(strArrSplit[i2].trim());
            }
            int iMin = Math.min(length, this.mSkipRecoveryActionArray.length);
            while (true) {
                boolean[] zArr2 = this.mSkipRecoveryActionArray;
                if (i < iMin) {
                    zArr2[i] = !zArr[i];
                    i++;
                } else {
                    log("SkipRecoveryAction: " + Arrays.toString(zArr2));
                    this.mPredictWaitingMillis = 1000L;
                    return;
                }
            }
        } else {
            this.mPredictWaitingMillis = 0L;
            log("Enabled actions is null");
        }
    }

    private void updateGlobalConfigDurations() {
        String string = Settings.Global.getString(this.mPhone.getContext().getContentResolver(), "dsrm_duration_millis");
        if (!TextUtils.isEmpty(string)) {
            String[] strArrSplit = string.split(",");
            int length = strArrSplit.length;
            long[] jArr = new long[length];
            for (int i = 0; i < length; i++) {
                try {
                    jArr[i] = Long.parseLong(strArrSplit[i].trim());
                } catch (NumberFormatException unused) {
                    this.mPredictWaitingMillis = 0L;
                    loge("Parsing duration millis error");
                    return;
                }
            }
            System.arraycopy(jArr, 0, this.mDataStallRecoveryDelayMillisArray, 0, Math.min(length, this.mDataStallRecoveryDelayMillisArray.length));
            log("DataStallRecoveryDelayMillis: " + Arrays.toString(this.mDataStallRecoveryDelayMillisArray));
            this.mPredictWaitingMillis = 1000L;
            return;
        }
        this.mPredictWaitingMillis = 0L;
        log("Duration millis is null");
    }

    private void updateDataStallRecoveryConfigs() {
        this.mDataStallRecoveryDelayMillisArray = this.mDataConfigManager.getDataStallRecoveryDelayMillis();
        this.mSkipRecoveryActionArray = this.mDataConfigManager.getDataStallRecoveryShouldSkipArray();
        this.mDataStallRecoveryRandomizedMillisArray = createDataStallRecoveryRandomOffsetsMillis();
        updateGlobalConfigActions();
        updateGlobalConfigDurations();
    }

    protected long getRandomOffsetsMillis(long j) {
        return ThreadLocalRandom.current().nextLong(j);
    }

    private long[] createDataStallRecoveryRandomOffsetsMillis() {
        int length = this.mDataStallRecoveryDelayMillisArray.length;
        long[] jArr = new long[length];
        long[] dataStallRecoveryRandomizationMillis = this.mDataConfigManager.getDataStallRecoveryRandomizationMillis();
        int iMin = Math.min(length, dataStallRecoveryRandomizationMillis.length);
        for (int i = 0; i < iMin; i++) {
            long j = dataStallRecoveryRandomizationMillis[i];
            if (j > 0) {
                jArr[i] = getRandomOffsetsMillis(j);
            }
        }
        log("createDataStallRecoveryRandomOffsetsMillis(): " + Arrays.toString(jArr));
        return jArr;
    }

    public long getDataStallRecoveryDelayMillis(int i) {
        if (i == 4 && !this.mIsAttemptedAllSteps) {
            i = 3;
        }
        return this.mDataStallRecoveryDelayMillisArray[i] + this.mDataStallRecoveryRandomizedMillisArray[i];
    }

    public boolean shouldSkipRecoveryAction(int i) {
        return this.mSkipRecoveryActionArray[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierConfigUpdated() {
        updateDataStallRecoveryConfigs();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMobileDataEnabledChanged(boolean z) {
        logl("onMobileDataEnabledChanged: DataEnabled:" + z + ",DataStalled:" + this.mDataStalled);
        if (this.mDataStalled && z) {
            this.mMobileDataChangedToEnabledDuringDataStall = true;
            setRecoveryAction(this.mLastAction);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset(boolean z) {
        if (z) {
            this.mIsValidNetwork = false;
        } else {
            this.mIsValidNetwork = true;
        }
        this.mRecoveryTriggered = false;
        this.mIsAttemptedAllSteps = false;
        this.mRadioStateChangedDuringDataStall = false;
        this.mIsAirPlaneModeEnableDuringDataStall = false;
        this.mMobileDataChangedToEnabledDuringDataStall = false;
        cancelNetworkCheckTimer();
        this.mTimeLastRecoveryStartMs = 0L;
        this.mLastAction = 0;
        this.mRecoveryAction = 0;
        this.mValidationCount = 0;
        this.mActionValidationCount = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInternetValidationStatusChanged(int i) {
        logl("onInternetValidationStatusChanged: " + DataUtils.validationStatusToString(i));
        boolean z = i == 1;
        if (z && !this.mDataStalled) {
            reset(false);
            MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onInternetValidationStatusChanged(this.mPhone, this, i);
            return;
        }
        this.mValidationCount++;
        this.mActionValidationCount++;
        setNetworkValidationState(z);
        if (z) {
            broadcastDataStallDetected(this.mLastAction);
            reset(false);
        } else if (this.mRecoveryTriggered) {
            logl("Validation failed, but recovery has already started.");
        } else if (isRecoveryNeeded(true)) {
            this.mIsValidNetwork = false;
            log("trigger data stall recovery");
            this.mTimeLastRecoveryStartMs = SystemClock.elapsedRealtime();
            sendMessage(obtainMessage(1));
        }
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onInternetValidationStatusChanged(this.mPhone, this, i);
    }

    private void resetAction() {
        this.mTimeLastRecoveryStartMs = 0L;
        this.mMobileDataChangedToEnabledDuringDataStall = false;
        this.mRadioStateChangedDuringDataStall = false;
        this.mIsAirPlaneModeEnableDuringDataStall = false;
        setRecoveryAction(0);
    }

    public int getRecoveryAction() {
        log("getRecoveryAction: " + recoveryActionToString(this.mRecoveryAction));
        return this.mRecoveryAction;
    }

    public void setRecoveryAction(int i) {
        if (!this.mIsValidNetwork && !isRecoveryAlreadyStarted()) {
            log("Skip set recovery action because the network still remains invalid and recovery was not started yet.");
            return;
        }
        if (this.mRecoveryAction != i) {
            this.mActionValidationCount = 0;
        }
        this.mRecoveryAction = i;
        if (this.mMobileDataChangedToEnabledDuringDataStall && i < 3 && isRecoveryAlreadyStarted()) {
            this.mRecoveryAction = 3;
        }
        if (this.mRadioStateChangedDuringDataStall && this.mRadioPowerState == 1 && isRecoveryAlreadyStarted()) {
            this.mRecoveryAction = 4;
        }
        if (shouldSkipRecoveryAction(this.mRecoveryAction)) {
            int i2 = this.mRecoveryAction;
            if (i2 == 0) {
                setRecoveryAction(1);
            } else if (i2 == 1) {
                setRecoveryAction(3);
            } else if (i2 == 3) {
                setRecoveryAction(4);
            } else if (i2 == 4) {
                resetAction();
            }
        }
        log("setRecoveryAction: " + recoveryActionToString(this.mRecoveryAction));
    }

    private boolean isRecoveryAlreadyStarted() {
        return getRecoveryAction() != 0 || this.mRecoveryTriggered;
    }

    private long getElapsedTimeSinceRecoveryMs() {
        return SystemClock.elapsedRealtime() - this.mTimeLastRecoveryStartMs;
    }

    private long getDurationOfCurrentRecoveryMs() {
        return SystemClock.elapsedRealtime() - this.mTimeElapsedOfCurrentAction;
    }

    private void broadcastDataStallDetected(int i) {
        log("broadcastDataStallDetected recoveryAction: " + i);
        Intent intent = new Intent("android.intent.action.DATA_STALL_DETECTED");
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
        intent.putExtra("recoveryAction", i);
        boolean z = !this.mDataStalled;
        int iElapsedRealtime = (int) (SystemClock.elapsedRealtime() - this.mDataStallStartMs);
        int recoveredReason = getRecoveredReason(this.mIsValidNetwork);
        int durationOfCurrentRecoveryMs = (int) getDurationOfCurrentRecoveryMs();
        log("mValidationCount=" + this.mValidationCount + ", mActionValidationCount=" + this.mActionValidationCount);
        intent.putExtra("EXTRA_DSRS_STATS_BUNDLE", this.mStats.getDataStallRecoveryMetricsData(i, z, iElapsedRealtime, recoveredReason, this.mValidationCount, this.mActionValidationCount, durationOfCurrentRecoveryMs));
        this.mPhone.getContext().sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    private void getDataCallList() {
        log("getDataCallList: request data call list");
        this.mWwanDataServiceManager.requestDataCallList(null);
    }

    private void cleanUpDataNetwork() {
        log("cleanUpDataNetwork: notify clean up data network");
        final DataStallRecoveryManagerCallback dataStallRecoveryManagerCallback = this.mDataStallRecoveryManagerCallback;
        Objects.requireNonNull(dataStallRecoveryManagerCallback);
        dataStallRecoveryManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataStallRecoveryManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                dataStallRecoveryManagerCallback.onDataStallReestablishInternet();
            }
        });
    }

    private void powerOffRadio() {
        log("powerOffRadio: Restart radio");
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.getServiceStateTracker().powerOffRadioSafely();
        }
    }

    private void rebootModem() {
        log("rebootModem: reboot modem");
        if (MiuiDataControllerStub.get(this.mPhone.getPhoneId()).isRebootModem()) {
            this.mPhone.rebootModem(null);
        }
    }

    private void startNetworkCheckTimer(int i) {
        if (i == 4 && this.mIsAttemptedAllSteps) {
            log("startNetworkCheckTimer: RESET_MODEM was the last action and all steps completed, no further timer scheduled.");
            return;
        }
        if (i == 4) {
            i = 3;
        }
        long j = this.mDataStallRecoveryDelayMillisArray[i] + this.mDataStallRecoveryRandomizedMillisArray[i];
        log("startNetworkCheckTimer(): Scheduling in " + j + "ms");
        if (this.mNetworkCheckTimerStarted) {
            return;
        }
        this.mNetworkCheckTimerStarted = true;
        this.mTimeLastRecoveryStartMs = SystemClock.elapsedRealtime();
        sendMessageDelayed(obtainMessage(1), j);
    }

    private void cancelNetworkCheckTimer() {
        log("cancelNetworkCheckTimer()");
        if (this.mNetworkCheckTimerStarted) {
            this.mNetworkCheckTimerStarted = false;
            removeMessages(1);
        }
    }

    private boolean isRecoveryNeeded(boolean z) {
        logv("enter: isRecoveryNeeded()");
        if (MiuiDataControllerStub.get(this.mPhone.getPhoneId()).isRecoveryNotNeeded(this.mPhone, z, null)) {
            return false;
        }
        if (!this.mIsValidNetwork && !isRecoveryAlreadyStarted()) {
            logl("skip when network still remains invalid and recovery was not started yet");
            return false;
        }
        if (this.mPhone.getSubId() != this.mActiveDataSubId) {
            logl("skip data stall recovery for non-active data connection");
            return false;
        }
        if (this.mIsAttemptedAllSteps) {
            logl("skip retrying continue recovery action");
            return false;
        }
        if (getElapsedTimeSinceRecoveryMs() < getDataStallRecoveryDelayMillis(this.mLastAction) && z) {
            logl("skip back to back data stall recovery");
            return false;
        }
        if (isInEmergencyCall()) {
            logl("skip data stall recovery as there is in an emergency call");
            return false;
        }
        if (!isPhoneStateIdle() && getRecoveryAction() > 1) {
            logl("skip data stall recovery as there is an active call");
            return false;
        }
        if (isAnyPhoneNetworkScanStarted()) {
            logl("skip data stall recovery as network scan started");
            return false;
        }
        if (this.mPhone.getSignalStrength().getLevel() <= 1) {
            logl("skip data stall recovery as in poor signal condition");
            return false;
        }
        if (!this.mDataNetworkController.isInternetDataAllowed(true)) {
            logl("skip data stall recovery as data not allowed.");
            return false;
        }
        if (this.mIsInternetNetworkConnected) {
            return true;
        }
        logl("skip data stall recovery as data not connected");
        return false;
    }

    private boolean isAnyPhoneNetworkScanStarted() {
        for (Phone phone : PhoneFactory.getPhones()) {
            logl("NetworkScanStarted: " + phone.getNetworkScanStarted() + " on phone" + phone.getPhoneId());
            if (phone.getNetworkScanStarted()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPhoneStateIdle() {
        for (Phone phone : PhoneFactory.getPhones()) {
            logl("Phone State: " + phone.getState() + " on phone" + phone.getPhoneId());
            if (phone.getState() != PhoneConstants.State.IDLE) {
                return false;
            }
        }
        return true;
    }

    private boolean isInEmergencyCall() {
        TelecomManager telecomManager = (TelecomManager) this.mPhone.getContext().getSystemService(TelecomManager.class);
        if (telecomManager != null && telecomManager.isInEmergencyCall()) {
            logl("Emergency call detected on the device");
            return true;
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.isInEcm()) {
                logl("ECM detected on phone" + phone.getPhoneId());
                return true;
            }
        }
        return false;
    }

    private void setNetworkValidationState(boolean z) {
        int iElapsedRealtime;
        boolean z2;
        boolean z3;
        boolean z4;
        String str;
        int recoveredReason = getRecoveredReason(z);
        boolean z5 = true;
        if (!this.mDataStalled) {
            this.mDataStalled = true;
            this.mDataStallStartMs = SystemClock.elapsedRealtime();
            logl("data stall: start time = " + DataUtils.elapsedTimeToString(this.mDataStallStartMs));
            z2 = true;
            z4 = true;
            iElapsedRealtime = 0;
            z3 = false;
        } else if (this.mLastActionReported) {
            iElapsedRealtime = 0;
            z2 = false;
            z3 = false;
            z4 = false;
        } else {
            iElapsedRealtime = (int) (SystemClock.elapsedRealtime() - this.mDataStallStartMs);
            this.mLastActionReported = true;
            z2 = true;
            z3 = true;
            z4 = false;
        }
        if (z) {
            iElapsedRealtime = (int) (SystemClock.elapsedRealtime() - this.mDataStallStartMs);
            this.mLastActionReported = false;
            this.mDataStalled = false;
        } else {
            z5 = z2;
        }
        int i = iElapsedRealtime;
        if (z5) {
            int durationOfCurrentRecoveryMs = ((getRecoveryAction() > 0 && !this.mIsAttemptedAllSteps) || this.mLastAction == 4) ? (int) getDurationOfCurrentRecoveryMs() : 0;
            this.mStats.uploadMetrics(this.mLastAction, z, i, recoveredReason, z3, durationOfCurrentRecoveryMs);
            StringBuilder sb = new StringBuilder("data stall: ");
            if (z4) {
                str = "start";
            } else {
                str = !z ? "in process" : "end";
            }
            sb.append(str);
            sb.append(", lastaction=");
            sb.append(recoveryActionToString(this.mLastAction));
            sb.append(", isRecovered=");
            sb.append(z);
            sb.append(", reason=");
            sb.append(recoveredReasonToString(recoveredReason));
            sb.append(", isFirstValidationAfterDoRecovery=");
            sb.append(z3);
            sb.append(", TimeDuration=");
            sb.append(i);
            sb.append(", TimeDurationForCurrentRecoveryAction=");
            sb.append(durationOfCurrentRecoveryMs);
            logl(sb.toString());
            MiuiDataControllerStub.get(this.mPhone.getPhoneId()).recordDataInterruptionRecoveryMeasures(this.mPhone, z, recoveryActionToString(this.mLastAction), i, durationOfCurrentRecoveryMs);
        }
    }

    private int getRecoveredReason(boolean z) {
        if (!z) {
            return 0;
        }
        if (!this.mRadioStateChangedDuringDataStall) {
            return this.mMobileDataChangedToEnabledDuringDataStall ? 3 : 1;
        }
        int i = this.mLastAction <= 1 ? 2 : 1;
        if (this.mIsAirPlaneModeEnableDuringDataStall) {
            return 3;
        }
        return i;
    }

    private void doRecovery() {
        int recoveryAction = getRecoveryAction();
        this.mLastAction = recoveryAction;
        this.mLastActionReported = false;
        this.mNetworkCheckTimerStarted = false;
        this.mTimeElapsedOfCurrentAction = SystemClock.elapsedRealtime();
        if (!isRecoveryNeeded(false)) {
            logl("doRecovery: Action " + recoveryActionToString(recoveryAction) + " skipped because isRecoveryNeeded failed.");
            startNetworkCheckTimer(recoveryAction);
            return;
        }
        if (recoveryAction == 0) {
            logl("doRecovery(): get data call list");
            getDataCallList();
            setRecoveryAction(1);
        } else if (recoveryAction == 1) {
            logl("doRecovery(): cleanup all connections");
            cleanUpDataNetwork();
            setRecoveryAction(3);
        } else if (recoveryAction == 3) {
            logl("doRecovery(): restarting radio");
            if (TelephonyBaseUtilsStub.isAutoDevice() && MiuiDataControllerStub.get(this.mPhone.getPhoneId()).validateRadioToggleCondition(this.mPhone)) {
                logl("doRecovery(): is auto-device, skip restarting radio for ndds phone");
                return;
            } else {
                setRecoveryAction(4);
                powerOffRadio();
            }
        } else if (recoveryAction == 4) {
            logl("doRecovery(): modem reset");
            if (TelephonyBaseUtilsStub.isAutoDevice() && MiuiDataControllerStub.get(this.mPhone.getPhoneId()).validateResetModemCondition(this.mPhone)) {
                resetAction();
                this.mIsAttemptedAllSteps = true;
                logl("doRecovery(): is auto-device, skip reseting modem for all phones");
                return;
            } else {
                rebootModem();
                resetAction();
                this.mIsAttemptedAllSteps = true;
            }
        } else {
            throw new RuntimeException("doRecovery: Invalid recoveryAction = " + recoveryActionToString(recoveryAction));
        }
        startNetworkCheckTimer(this.mLastAction);
    }

    private static String recoveredReasonToString(int i) {
        if (i == 0) {
            return "RECOVERED_REASON_NONE";
        }
        if (i == 1) {
            return "RECOVERED_REASON_DSRM";
        }
        if (i == 2) {
            return "RECOVERED_REASON_MODEM";
        }
        if (i == 3) {
            return "RECOVERED_REASON_USER";
        }
        return "Unknown(" + i + ")";
    }

    private static String radioPowerStateToString(int i) {
        if (i == 0) {
            return "RADIO_POWER_OFF";
        }
        if (i == 1) {
            return "RADIO_POWER_ON";
        }
        if (i == 2) {
            return "RADIO_POWER_UNAVAILABLE";
        }
        return "Unknown(" + i + ")";
    }

    private static String recoveryActionToString(int i) {
        if (i == 0) {
            return "RECOVERY_ACTION_GET_DATA_CALL_LIST";
        }
        if (i == 1) {
            return "RECOVERY_ACTION_CLEANUP";
        }
        if (i == 3) {
            return "RECOVERY_ACTION_RADIO_RESTART";
        }
        if (i == 4) {
            return "RECOVERY_ACTION_RESET_MODEM";
        }
        return "Unknown(" + i + ")";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d(this.mLogTag, str);
    }

    private void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println(DataStallRecoveryManager.class.getSimpleName() + "-" + this.mPhone.getPhoneId() + ":");
        androidUtilIndentingPrintWriter.increaseIndent();
        StringBuilder sb = new StringBuilder("mIsValidNetwork=");
        sb.append(this.mIsValidNetwork);
        androidUtilIndentingPrintWriter.println(sb.toString());
        androidUtilIndentingPrintWriter.println("mIsInternetNetworkConnected=" + this.mIsInternetNetworkConnected);
        androidUtilIndentingPrintWriter.println("mIsAirPlaneModeEnableDuringDataStall=" + this.mIsAirPlaneModeEnableDuringDataStall);
        androidUtilIndentingPrintWriter.println("mDataStalled=" + this.mDataStalled);
        androidUtilIndentingPrintWriter.println("mLastAction=" + recoveryActionToString(this.mLastAction));
        androidUtilIndentingPrintWriter.println("mIsAttemptedAllSteps=" + this.mIsAttemptedAllSteps);
        androidUtilIndentingPrintWriter.println("mDataStallStartMs=" + DataUtils.elapsedTimeToString(this.mDataStallStartMs));
        androidUtilIndentingPrintWriter.println("mRadioPowerState=" + radioPowerStateToString(this.mRadioPowerState));
        androidUtilIndentingPrintWriter.println("mLastActionReported=" + this.mLastActionReported);
        androidUtilIndentingPrintWriter.println("mTimeLastRecoveryStartMs=" + DataUtils.elapsedTimeToString(this.mTimeLastRecoveryStartMs));
        androidUtilIndentingPrintWriter.println("getRecoveryAction()=" + recoveryActionToString(getRecoveryAction()));
        androidUtilIndentingPrintWriter.println("mRadioStateChangedDuringDataStall=" + this.mRadioStateChangedDuringDataStall);
        androidUtilIndentingPrintWriter.println("mMobileDataChangedToEnabledDuringDataStall=" + this.mMobileDataChangedToEnabledDuringDataStall);
        androidUtilIndentingPrintWriter.println("mPredictWaitingMillis=" + this.mPredictWaitingMillis);
        androidUtilIndentingPrintWriter.println("DataStallRecoveryDelayMillisArray=" + Arrays.toString(this.mDataStallRecoveryDelayMillisArray));
        androidUtilIndentingPrintWriter.println("SkipRecoveryActionArray=" + Arrays.toString(this.mSkipRecoveryActionArray));
        androidUtilIndentingPrintWriter.println("mDataStallRecoveryRandomizedMillisArray=" + Arrays.toString(this.mDataStallRecoveryRandomizedMillisArray));
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("");
        androidUtilIndentingPrintWriter.println("Local logs:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
    }
}
