package com.android.internal.telephony.data;

import android.app.AlarmManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.AccessNetworkConstants;
import android.telephony.AnomalyReporter;
import android.telephony.DataFailCause;
import android.telephony.data.DataProfile;
import android.telephony.data.ThrottleStatus;
import android.telephony.data.TrafficDescriptor;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.build.UnboundedSdkLevel$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.euicc.EuiccConnector$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.util.Inet4AddressUtils$$ExternalSyntheticBUOutline0;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class DataRetryManager extends Handler {
    protected static final int EVENT_DATA_HANDOVER_RETRY = 4;
    protected static final int EVENT_DATA_SETUP_RETRY = 3;
    protected static final int RESET_REASON_DATA_CONFIG_CHANGED = 5;
    protected static final int RESET_REASON_DATA_ENABLED_CHANGED = 7;
    protected static final int RESET_REASON_DATA_PROFILES_CHANGED = 1;
    public static final int RESET_REASON_MODEM_RESTART = 3;
    private static final long RETRY_LONG_DELAY_TIMER_THRESHOLD_MILLIS = 60000;
    private final AlarmManager mAlarmManager;
    protected final DataConfigManager mDataConfigManager;
    private List mDataHandoverRetryRuleList;
    protected final DataNetworkController mDataNetworkController;
    private final DataProfileManager mDataProfileManager;
    private final List mDataRetryEntries;
    private final Set mDataRetryManagerCallbacks;
    private final SparseArray mDataServiceManagers;
    private List mDataSetupRetryRuleList;
    protected final List<DataThrottlingEntry> mDataThrottlingEntries;
    private final FeatureFlags mFlags;
    private final LocalLog mLocalLog;
    protected final String mLogTag;
    protected final Phone mPhone;
    private final CommandsInterface mRil;

    public void handlePdpRejectCauseSuccess() {
    }

    protected void logv(String str) {
    }

    public List<DataRetryEntry> getDataRetryEntries() {
        return this.mDataRetryEntries;
    }

    public static class DataThrottlingEntry {
        public final DataNetwork dataNetwork;
        public final DataProfile dataProfile;
        public final long expirationTimeMillis;
        public final DataNetworkController.NetworkRequestList networkRequestList;
        public final int retryType;
        public final int transport;

        public DataThrottlingEntry(DataProfile dataProfile, DataNetworkController.NetworkRequestList networkRequestList, DataNetwork dataNetwork, int i, int i2, long j) {
            this.dataProfile = dataProfile;
            this.networkRequestList = networkRequestList;
            this.dataNetwork = dataNetwork;
            this.transport = i;
            this.retryType = i2;
            this.expirationTimeMillis = j;
        }

        public String toString() {
            return "[DataThrottlingEntry: dataProfile=" + this.dataProfile + ", request list=" + this.networkRequestList + ", dataNetwork=" + this.dataNetwork + ", transport=" + AccessNetworkConstants.transportTypeToString(this.transport) + ", expiration time=" + DataUtils.elapsedTimeToString(this.expirationTimeMillis) + "]";
        }
    }

    public static class DataRetryRule {
        protected Set<Integer> mFailCauses;
        protected int mMaxRetries;
        protected Set<Integer> mNetworkCapabilities = new ArraySet();
        protected List<Long> mRetryIntervalsMillis;

        public DataRetryRule(String str) {
            this.mRetryIntervalsMillis = List.of(5000L);
            this.mMaxRetries = 10;
            this.mFailCauses = new ArraySet();
            if (TextUtils.isEmpty(str)) {
                RILUtils$$ExternalSyntheticBUOutline1.m("illegal rule ", str);
                throw null;
            }
            String lowerCase = str.trim().toLowerCase(Locale.ROOT);
            for (String str2 : lowerCase.split("\\s*,\\s*")) {
                String[] strArrSplit = str2.trim().split("\\s*=\\s*");
                if (strArrSplit.length != 2) {
                    Phone$$ExternalSyntheticBUOutline0.m("illegal rule ".concat(lowerCase));
                    throw null;
                }
                String strTrim = strArrSplit[0].trim();
                String strTrim2 = strArrSplit[1].trim();
                try {
                    int iHashCode = strTrim.hashCode();
                    if (iHashCode != -958847449) {
                        if (iHashCode != -705617557) {
                            if (iHashCode == 384266364 && strTrim.equals("retry_interval")) {
                                this.mRetryIntervalsMillis = (List) Arrays.stream(strTrim2.split("\\s*\\|\\s*")).map(new DataNetworkController$HandoverRule$$ExternalSyntheticLambda1()).map(new Function() { // from class: com.android.internal.telephony.data.DataRetryManager$DataRetryRule$$ExternalSyntheticLambda2
                                    @Override // java.util.function.Function
                                    public final Object apply(Object obj) {
                                        return Long.valueOf((String) obj);
                                    }
                                }).collect(Collectors.toList());
                            }
                        } else if (strTrim.equals("fail_causes")) {
                            this.mFailCauses = (Set) Arrays.stream(strTrim2.split("\\s*\\|\\s*")).map(new DataNetworkController$HandoverRule$$ExternalSyntheticLambda1()).map(new DataRetryManager$DataRetryRule$$ExternalSyntheticLambda1()).collect(Collectors.toSet());
                        }
                    } else if (strTrim.equals("maximum_retries")) {
                        this.mMaxRetries = Integer.parseInt(strTrim2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DataRetryManager$DataRetryRule$$ExternalSyntheticBUOutline0.m("illegal rule ", lowerCase, ", e=", e);
                    throw null;
                }
            }
            if (this.mMaxRetries < 0) {
                EuiccConnector$$ExternalSyntheticBUOutline0.m("Max retries should not be less than 0. mMaxRetries=", this.mMaxRetries);
                throw null;
            }
            if (this.mRetryIntervalsMillis.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$DataRetryRule$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataRetryManager.DataRetryRule.$r8$lambda$aq96Sg2xelk7__4Ohs5B0326jPU((Long) obj);
                }
            })) {
                Inet4AddressUtils$$ExternalSyntheticBUOutline0.m("Retry interval should not be less than 0. mRetryIntervalsMillis=", this.mRetryIntervalsMillis);
                throw null;
            }
        }

        public static /* synthetic */ boolean $r8$lambda$aq96Sg2xelk7__4Ohs5B0326jPU(Long l) {
            return l.longValue() <= 0;
        }

        public List<Long> getRetryIntervalsMillis() {
            return this.mRetryIntervalsMillis;
        }

        public int getMaxRetries() {
            return this.mMaxRetries;
        }

        public Set<Integer> getFailCauses() {
            return this.mFailCauses;
        }
    }

    public static class DataSetupRetryRule extends DataRetryRule {
        private boolean mIsPermanentFailCauseRule;

        public DataSetupRetryRule(String str) {
            super(str);
            String lowerCase = str.trim().toLowerCase(Locale.ROOT);
            for (String str2 : lowerCase.split("\\s*,\\s*")) {
                String[] strArrSplit = str2.trim().split("\\s*=\\s*");
                if (strArrSplit.length != 2) {
                    Phone$$ExternalSyntheticBUOutline0.m("illegal rule ".concat(lowerCase));
                    throw null;
                }
                String strTrim = strArrSplit[0].trim();
                String strTrim2 = strArrSplit[1].trim();
                try {
                    int iHashCode = strTrim.hashCode();
                    if (iHashCode != -1487597642) {
                        if (iHashCode == -270123174 && strTrim.equals("permanent_fail_causes")) {
                            this.mFailCauses = (Set) Arrays.stream(strTrim2.split("\\s*\\|\\s*")).map(new DataNetworkController$HandoverRule$$ExternalSyntheticLambda1()).map(new DataRetryManager$DataRetryRule$$ExternalSyntheticLambda1()).collect(Collectors.toSet());
                            this.mIsPermanentFailCauseRule = true;
                        }
                    } else if (strTrim.equals("capabilities")) {
                        this.mNetworkCapabilities = DataUtils.getNetworkCapabilitiesFromString(strTrim2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DataRetryManager$DataRetryRule$$ExternalSyntheticBUOutline0.m("illegal rule ", lowerCase, ", e=", e);
                    throw null;
                }
            }
            if (this.mFailCauses.isEmpty()) {
                if (this.mNetworkCapabilities.isEmpty() || this.mNetworkCapabilities.contains(-1)) {
                    UnboundedSdkLevel$$ExternalSyntheticBUOutline0.m("illegal rule ", lowerCase, ". Should have either valid network capabilities or fail causes.");
                    throw null;
                }
            }
        }

        public Set<Integer> getNetworkCapabilities() {
            return this.mNetworkCapabilities;
        }

        public boolean isPermanentFailCauseRule() {
            return this.mIsPermanentFailCauseRule;
        }

        public boolean canBeMatched(int i, int i2) {
            if (this.mFailCauses.isEmpty() || this.mFailCauses.contains(Integer.valueOf(i2))) {
                return this.mNetworkCapabilities.isEmpty() || this.mNetworkCapabilities.contains(Integer.valueOf(i));
            }
            return false;
        }

        public String toString() {
            return "[DataSetupRetryRule: Network capabilities:" + DataUtils.networkCapabilitiesToString(this.mNetworkCapabilities.stream().mapToInt(new ToIntFunction() { // from class: com.android.internal.telephony.data.DataRetryManager$DataSetupRetryRule$$ExternalSyntheticLambda0
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((Integer) obj).intValue();
                }
            }).toArray()) + ", Fail causes=" + this.mFailCauses + ", Retry intervals=" + this.mRetryIntervalsMillis + ", Maximum retries=" + this.mMaxRetries + "]";
        }
    }

    public static class DataHandoverRetryRule extends DataRetryRule {
        public DataHandoverRetryRule(String str) {
            super(str);
        }

        public String toString() {
            return "[DataHandoverRetryRule: Retry intervals=" + this.mRetryIntervalsMillis + ", Fail causes=" + this.mFailCauses + ", Maximum retries=" + this.mMaxRetries + "]";
        }
    }

    public static class DataRetryEntry {
        public static final int RETRY_STATE_CANCELLED = 4;
        public static final int RETRY_STATE_FAILED = 2;
        public static final int RETRY_STATE_NOT_RETRIED = 1;
        public static final int RETRY_STATE_SUCCEEDED = 3;
        public final DataRetryRule appliedDataRetryRule;
        protected int mRetryState = 1;
        protected long mRetryStateTimestamp;
        public final long retryDelayMillis;
        public final long retryElapsedTime;

        public DataRetryEntry(DataRetryRule dataRetryRule, long j) {
            this.appliedDataRetryRule = dataRetryRule;
            this.retryDelayMillis = j;
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            this.mRetryStateTimestamp = jElapsedRealtime;
            this.retryElapsedTime = jElapsedRealtime + j;
        }

        public void setState(int i) {
            this.mRetryState = i;
            this.mRetryStateTimestamp = SystemClock.elapsedRealtime();
        }

        public int getState() {
            return this.mRetryState;
        }

        public static String retryStateToString(int i) {
            if (i == 1) {
                return "NOT_RETRIED";
            }
            if (i == 2) {
                return "FAILED";
            }
            if (i == 3) {
                return "SUCCEEDED";
            }
            if (i == 4) {
                return "CANCELLED";
            }
            return "Unknown(" + i + ")";
        }

        public static class Builder<T extends Builder<T>> {
            protected DataRetryRule mAppliedDataRetryRule;
            protected long mRetryDelayMillis = 5000;

            public T setRetryDelay(long j) {
                this.mRetryDelayMillis = j;
                return this;
            }

            public T setAppliedRetryRule(DataRetryRule dataRetryRule) {
                this.mAppliedDataRetryRule = dataRetryRule;
                return this;
            }
        }
    }

    public static class DataSetupRetryEntry extends DataRetryEntry {
        public static final int RETRY_TYPE_DATA_PROFILE = 1;
        public static final int RETRY_TYPE_NETWORK_REQUESTS = 2;
        public static final int RETRY_TYPE_UNKNOWN = 0;
        public final DataProfile dataProfile;
        public final DataNetworkController.NetworkRequestList networkRequestList;
        public final int setupRetryType;
        public final int transport;

        private DataSetupRetryEntry(int i, DataNetworkController.NetworkRequestList networkRequestList, DataProfile dataProfile, int i2, DataSetupRetryRule dataSetupRetryRule, long j) {
            super(dataSetupRetryRule, j);
            this.setupRetryType = i;
            this.networkRequestList = networkRequestList;
            this.dataProfile = dataProfile;
            this.transport = i2;
        }

        private static String retryTypeToString(int i) {
            if (i == 0) {
                return SatelliteConstants.DEFAULT_PLMN;
            }
            if (i == 1) {
                return "BY_PROFILE";
            }
            if (i == 2) {
                return "BY_NETWORK_REQUESTS";
            }
            return "Unknown(" + i + ")";
        }

        public String toString() {
            return "[DataSetupRetryEntry: delay=" + this.retryDelayMillis + "ms, retry time:" + DataUtils.elapsedTimeToString(this.retryElapsedTime) + ", " + this.dataProfile + ", transport=" + AccessNetworkConstants.transportTypeToString(this.transport) + ", retry type=" + retryTypeToString(this.setupRetryType) + ", retry requests=" + this.networkRequestList + ", applied rule=" + this.appliedDataRetryRule + ", state=" + DataRetryEntry.retryStateToString(this.mRetryState) + ", timestamp=" + DataUtils.elapsedTimeToString(this.mRetryStateTimestamp) + "]";
        }

        public static class Builder<T extends Builder<T>> extends DataRetryEntry.Builder<T> {
            private DataProfile mDataProfile;
            private DataNetworkController.NetworkRequestList mNetworkRequestList;
            private int mSetupRetryType = 0;
            private int mTransport = -1;

            public Builder<T> setSetupRetryType(int i) {
                this.mSetupRetryType = i;
                return this;
            }

            public Builder<T> setNetworkRequestList(DataNetworkController.NetworkRequestList networkRequestList) {
                this.mNetworkRequestList = networkRequestList;
                return this;
            }

            public Builder<T> setDataProfile(DataProfile dataProfile) {
                this.mDataProfile = dataProfile;
                return this;
            }

            public Builder<T> setTransport(int i) {
                this.mTransport = i;
                return this;
            }

            public DataSetupRetryEntry build() {
                DataNetworkController.NetworkRequestList networkRequestList = this.mNetworkRequestList;
                if (networkRequestList == null) {
                    Phone$$ExternalSyntheticBUOutline0.m("network request list is not specified.");
                    return null;
                }
                int i = this.mTransport;
                if (i != 1 && i != 2) {
                    EuiccConnector$$ExternalSyntheticBUOutline0.m("Invalid transport type ", this.mTransport);
                    return null;
                }
                int i2 = this.mSetupRetryType;
                if (i2 != 1 && i2 != 2) {
                    EuiccConnector$$ExternalSyntheticBUOutline0.m("Invalid setup retry type ", this.mSetupRetryType);
                    return null;
                }
                return new DataSetupRetryEntry(i2, networkRequestList, this.mDataProfile, i, (DataSetupRetryRule) this.mAppliedDataRetryRule, this.mRetryDelayMillis);
            }
        }
    }

    public static class DataHandoverRetryEntry extends DataRetryEntry {
        public final DataNetwork dataNetwork;

        public DataHandoverRetryEntry(DataNetwork dataNetwork, DataHandoverRetryRule dataHandoverRetryRule, long j) {
            super(dataHandoverRetryRule, j);
            this.dataNetwork = dataNetwork;
        }

        public String toString() {
            return "[DataHandoverRetryEntry: delay=" + this.retryDelayMillis + "ms, retry time:" + DataUtils.elapsedTimeToString(this.retryElapsedTime) + ", " + this.dataNetwork + ", applied rule=" + this.appliedDataRetryRule + ", state=" + DataRetryEntry.retryStateToString(this.mRetryState) + ", timestamp=" + DataUtils.elapsedTimeToString(this.mRetryStateTimestamp) + "]";
        }

        public static class Builder<T extends Builder<T>> extends DataRetryEntry.Builder<T> {
            public DataNetwork mDataNetwork;

            public Builder<T> setDataNetwork(DataNetwork dataNetwork) {
                this.mDataNetwork = dataNetwork;
                return this;
            }

            public DataHandoverRetryEntry build() {
                return new DataHandoverRetryEntry(this.mDataNetwork, (DataHandoverRetryRule) this.mAppliedDataRetryRule, this.mRetryDelayMillis);
            }
        }
    }

    public static class DataRetryManagerCallback extends DataCallback {
        public void onDataNetworkHandoverRetry(DataHandoverRetryEntry dataHandoverRetryEntry) {
        }

        public void onDataNetworkHandoverRetryStopped(DataNetwork dataNetwork) {
        }

        public void onDataNetworkSetupRetry(DataSetupRetryEntry dataSetupRetryEntry) {
        }

        public void onThrottleStatusChanged(List<ThrottleStatus> list) {
        }

        public DataRetryManagerCallback(Executor executor) {
            super(executor);
        }
    }

    public DataRetryManager(Phone phone, DataNetworkController dataNetworkController, SparseArray<DataServiceManager> sparseArray, Looper looper, FeatureFlags featureFlags, DataRetryManagerCallback dataRetryManagerCallback) {
        super(looper);
        this.mLocalLog = new LocalLog(128);
        ArraySet arraySet = new ArraySet();
        this.mDataRetryManagerCallbacks = arraySet;
        this.mDataSetupRetryRuleList = new ArrayList();
        this.mDataHandoverRetryRuleList = new ArrayList();
        this.mDataRetryEntries = new ArrayList();
        this.mDataThrottlingEntries = new ArrayList();
        this.mPhone = phone;
        this.mRil = phone.mCi;
        this.mFlags = featureFlags;
        this.mLogTag = "DRM-" + phone.getPhoneId();
        arraySet.add(dataRetryManagerCallback);
        this.mDataServiceManagers = sparseArray;
        this.mDataNetworkController = dataNetworkController;
        DataConfigManager dataConfigManager = dataNetworkController.getDataConfigManager();
        this.mDataConfigManager = dataConfigManager;
        this.mDataProfileManager = dataNetworkController.getDataProfileManager();
        this.mAlarmManager = (AlarmManager) phone.getContext().getSystemService(AlarmManager.class);
        dataConfigManager.registerCallback(new DataConfigManager.DataConfigManagerCallback(new Executor() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda2
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.DataRetryManager.1
            @Override // com.android.internal.telephony.data.DataConfigManager.DataConfigManagerCallback
            public void onCarrierConfigChanged() {
                DataRetryManager.this.onCarrierConfigUpdated();
            }
        });
        for (int i : phone.getAccessNetworksManager().getAvailableTransports()) {
            ((DataServiceManager) this.mDataServiceManagers.get(i)).registerForApnUnthrottled(this, 6);
        }
        this.mDataProfileManager.registerCallback(new DataProfileManager.DataProfileManagerCallback(new Executor() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda2
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.DataRetryManager.2
            @Override // com.android.internal.telephony.data.DataProfileManager.DataProfileManagerCallback
            public void onDataProfilesChanged() {
                DataRetryManager.this.onReset(1);
            }
        });
        dataNetworkController.registerDataNetworkControllerCallback(new DataNetworkController.DataNetworkControllerCallback(new Executor() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda2
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.DataRetryManager.3
            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onDataServiceBound(int i2) {
                DataRetryManager.this.onReset(4);
            }

            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onDataNetworkConnected(int i2, DataProfile dataProfile) {
                DataRetryManager.this.onDataNetworkConnected(i2, dataProfile);
            }
        });
        this.mRil.registerForOn(this, 8, null);
        this.mRil.registerForModemReset(this, 9, null);
        if (this.mDataConfigManager.shouldResetDataThrottlingWhenTacChanges()) {
            this.mPhone.getServiceStateTracker().registerForAreaCodeChanged(this, 10, null);
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        DataProfile dataProfile;
        String str;
        switch (message.what) {
            case 3:
                final DataSetupRetryEntry dataSetupRetryEntry = (DataSetupRetryEntry) message.obj;
                if (!isRetryCancelled(dataSetupRetryEntry)) {
                    this.mDataRetryManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            DataRetryManager.DataRetryManagerCallback dataRetryManagerCallback = (DataRetryManager.DataRetryManagerCallback) obj;
                            dataRetryManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda22
                                @Override // java.lang.Runnable
                                public final void run() {
                                    dataRetryManagerCallback.onDataNetworkSetupRetry(dataSetupRetryEntry);
                                }
                            });
                        }
                    });
                }
                break;
            case 4:
                final DataHandoverRetryEntry dataHandoverRetryEntry = (DataHandoverRetryEntry) message.obj;
                if (!isRetryCancelled(dataHandoverRetryEntry)) {
                    this.mDataRetryManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda4
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            DataRetryManager.DataRetryManagerCallback dataRetryManagerCallback = (DataRetryManager.DataRetryManagerCallback) obj;
                            dataRetryManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda26
                                @Override // java.lang.Runnable
                                public final void run() {
                                    dataRetryManagerCallback.onDataNetworkHandoverRetry(dataHandoverRetryEntry);
                                }
                            });
                        }
                    });
                }
                break;
            case 5:
            default:
                loge("Unexpected message " + message.what);
                break;
            case 6:
                AsyncResult asyncResult = (AsyncResult) message.obj;
                int iIntValue = ((Integer) asyncResult.userObj).intValue();
                Object obj = asyncResult.result;
                if (obj instanceof String) {
                    str = (String) obj;
                    dataProfile = null;
                } else if (obj instanceof DataProfile) {
                    dataProfile = (DataProfile) obj;
                    str = null;
                } else {
                    dataProfile = null;
                    str = null;
                }
                onDataProfileUnthrottled(dataProfile, str, iIntValue, true, true);
                break;
            case 7:
                onCancelPendingHandoverRetry((DataNetwork) message.obj);
                break;
            case 8:
                onReset(2);
                break;
            case 9:
                onReset(3);
                break;
            case 10:
                onReset(6);
                break;
        }
    }

    private boolean isRetryCancelled(DataRetryEntry dataRetryEntry) {
        if (dataRetryEntry != null && dataRetryEntry.getState() == 1) {
            return false;
        }
        log("Retry was removed earlier. " + dataRetryEntry);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierConfigUpdated() {
        onReset(5);
        this.mDataSetupRetryRuleList = this.mDataConfigManager.getDataSetupRetryRules();
        this.mDataHandoverRetryRuleList = this.mDataConfigManager.getDataHandoverRetryRules();
        log("onDataConfigUpdated: mDataSetupRetryRuleList=" + this.mDataSetupRetryRuleList + ", mDataHandoverRetryRuleList=" + this.mDataHandoverRetryRuleList);
    }

    public void onDataNetworkConnected(int i, DataProfile dataProfile) {
        if (dataProfile.getApnSetting() != null) {
            dataProfile.getApnSetting().setPermanentFailed(false);
        }
        onDataProfileUnthrottled(dataProfile, null, i, true, false);
    }

    public void evaluateDataSetupRetry(final DataProfile dataProfile, final int i, final DataNetworkController.NetworkRequestList networkRequestList, final int i2, final long j) {
        if (getLooper().isCurrentThread()) {
            lambda$evaluateDataSetupRetry$4(dataProfile, i, networkRequestList, i2, j);
        } else {
            post(new Runnable() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$evaluateDataSetupRetry$4(dataProfile, i, networkRequestList, i2, j);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v7 */
    /* JADX WARN: Type inference failed for: r7v8, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r7v9 */
    /* JADX INFO: renamed from: onEvaluateDataSetupRetry, reason: merged with bridge method [inline-methods] */
    public void lambda$evaluateDataSetupRetry$4(DataProfile dataProfile, int i, DataNetworkController.NetworkRequestList networkRequestList, int i2, long j) {
        logl("onEvaluateDataSetupRetry: " + dataProfile + ", transport=" + AccessNetworkConstants.transportTypeToString(i) + ", cause=" + DataFailCause.toString(i2) + ", retryDelayMillis=" + j + "ms, " + networkRequestList);
        if (j == Long.MAX_VALUE || j == 2147483647L) {
            logl("Network suggested never retry for " + dataProfile);
            throttleDataProfile(dataProfile, networkRequestList, null, 2, i, Long.MAX_VALUE);
            return;
        }
        ?? r7 = 1;
        if (j != -1) {
            DataSetupRetryEntry dataSetupRetryEntryBuild = ((DataSetupRetryEntry.Builder) new DataSetupRetryEntry.Builder().setRetryDelay(j)).setSetupRetryType(1).setNetworkRequestList(networkRequestList).setDataProfile(dataProfile).setTransport(i).build();
            throttleDataProfile(dataProfile, networkRequestList, null, 2, i, dataSetupRetryEntryBuild.retryElapsedTime);
            schedule(dataSetupRetryEntryBuild);
            return;
        }
        logv("mDataSetupRetryRuleList=" + this.mDataSetupRetryRuleList);
        List<DataNetworkController.NetworkRequestList> groupedNetworkRequestList = DataUtils.getGroupedNetworkRequestList(networkRequestList, this.mFlags);
        int i3 = 0;
        boolean z = false;
        for (DataSetupRetryRule dataSetupRetryRule : this.mDataSetupRetryRuleList) {
            if (dataSetupRetryRule.isPermanentFailCauseRule() && dataSetupRetryRule.getFailCauses().contains(Integer.valueOf(i2))) {
                if (dataProfile.getApnSetting() != null) {
                    dataProfile.getApnSetting().setPermanentFailed(r7);
                    log("Marked " + dataProfile.getApnSetting().getApnName() + " permanently failed, but still schedule retry to see if another data profile can be used for setup data.");
                    schedule(((DataSetupRetryEntry.Builder) ((DataSetupRetryEntry.Builder) new DataSetupRetryEntry.Builder().setRetryDelay(dataSetupRetryRule.getRetryIntervalsMillis().get(i3).longValue())).setAppliedRetryRule(dataSetupRetryRule)).setSetupRetryType(2).setTransport(i).setNetworkRequestList(networkRequestList).build());
                    return;
                }
                log("Stopped timer-based retry for TD-based data profile. Will retry only when environment changes.");
                return;
            }
            for (DataNetworkController.NetworkRequestList networkRequestList2 : groupedNetworkRequestList) {
                int highestPrioritySupportedNetworkCapability = networkRequestList2.get(i3).getHighestPrioritySupportedNetworkCapability();
                if (dataSetupRetryRule.canBeMatched(highestPrioritySupportedNetworkCapability, i2)) {
                    if (isSimilarNetworkRequestRetryScheduled(networkRequestList2.get(i3), i)) {
                        log(networkRequestList2.get(i3) + " already had similar retry scheduled.");
                        return;
                    }
                    int retryFailedCount = getRetryFailedCount(highestPrioritySupportedNetworkCapability, dataSetupRetryRule, i);
                    log("For capability " + DataUtils.networkCapabilityToString(highestPrioritySupportedNetworkCapability) + ", found matching rule " + dataSetupRetryRule + ", failed count=" + retryFailedCount);
                    if (retryFailedCount == dataSetupRetryRule.getMaxRetries()) {
                        log("Data retry failed for " + retryFailedCount + " times on " + AccessNetworkConstants.transportTypeToString(i) + ". Stopped timer-based data retry for " + DataUtils.networkCapabilityToString(highestPrioritySupportedNetworkCapability) + ". Condition-based retry will still happen when condition changes.");
                        return;
                    }
                    schedule(((DataSetupRetryEntry.Builder) ((DataSetupRetryEntry.Builder) new DataSetupRetryEntry.Builder().setRetryDelay(dataSetupRetryRule.getRetryIntervalsMillis().get(Math.min(retryFailedCount, dataSetupRetryRule.getRetryIntervalsMillis().size() - r7)).longValue())).setAppliedRetryRule(dataSetupRetryRule)).setSetupRetryType(2).setTransport(i).setNetworkRequestList(networkRequestList2).build());
                    z = true;
                    break;
                }
            }
            i3 = 0;
            r7 = 1;
        }
        if (z) {
            return;
        }
        log("onEvaluateDataSetupRetry: Did not match any retry rule. Stop timer-based retry.");
    }

    public void evaluateDataHandoverRetry(final DataNetwork dataNetwork, final int i, final long j) {
        post(new Runnable() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$evaluateDataHandoverRetry$5(dataNetwork, i, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: renamed from: onEvaluateDataHandoverRetry, reason: merged with bridge method [inline-methods] */
    public void lambda$evaluateDataHandoverRetry$5(final DataNetwork dataNetwork, int i, long j) {
        logl("onEvaluateDataHandoverRetry: " + dataNetwork + ", cause=" + DataFailCause.toString(i) + ", retryDelayMillis=" + j + "ms");
        int targetTransport = DataUtils.getTargetTransport(dataNetwork.getTransport());
        if (j == Long.MAX_VALUE || j == 2147483647L) {
            logl("Network suggested never retry handover for " + dataNetwork);
            throttleDataProfile(dataNetwork.getDataProfile(), dataNetwork.getAttachedNetworkRequestList(), dataNetwork, 3, targetTransport, Long.MAX_VALUE);
            return;
        }
        if (j != -1) {
            DataHandoverRetryEntry dataHandoverRetryEntryBuild = ((DataHandoverRetryEntry.Builder) new DataHandoverRetryEntry.Builder().setRetryDelay(j)).setDataNetwork(dataNetwork).build();
            throttleDataProfile(dataNetwork.getDataProfile(), dataNetwork.getAttachedNetworkRequestList(), dataNetwork, 3, targetTransport, dataHandoverRetryEntryBuild.retryElapsedTime);
            schedule(dataHandoverRetryEntryBuild);
            return;
        }
        for (DataHandoverRetryRule dataHandoverRetryRule : this.mDataHandoverRetryRuleList) {
            if (dataHandoverRetryRule.getFailCauses().isEmpty() || dataHandoverRetryRule.getFailCauses().contains(Integer.valueOf(i))) {
                int retryFailedCount = getRetryFailedCount(dataNetwork, dataHandoverRetryRule);
                log("Found matching rule " + dataHandoverRetryRule + ", failed count=" + retryFailedCount);
                if (retryFailedCount == dataHandoverRetryRule.getMaxRetries()) {
                    log("Data handover retry failed for " + retryFailedCount + " times. Stopped handover retry.");
                    this.mDataRetryManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda25
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            DataRetryManager.DataRetryManagerCallback dataRetryManagerCallback = (DataRetryManager.DataRetryManagerCallback) obj;
                            dataRetryManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda30
                                @Override // java.lang.Runnable
                                public final void run() {
                                    dataRetryManagerCallback.onDataNetworkHandoverRetryStopped(dataNetwork);
                                }
                            });
                        }
                    });
                    return;
                }
                schedule(((DataHandoverRetryEntry.Builder) ((DataHandoverRetryEntry.Builder) new DataHandoverRetryEntry.Builder().setRetryDelay(dataHandoverRetryRule.getRetryIntervalsMillis().get(Math.min(retryFailedCount, dataHandoverRetryRule.getRetryIntervalsMillis().size() - 1)).longValue())).setDataNetwork(dataNetwork).setAppliedRetryRule(dataHandoverRetryRule)).build());
            }
        }
    }

    public boolean isDataNetworkHandoverRetryStopped(DataNetwork dataNetwork) {
        for (DataHandoverRetryRule dataHandoverRetryRule : this.mDataHandoverRetryRuleList) {
            int retryFailedCount = getRetryFailedCount(dataNetwork, dataHandoverRetryRule);
            if (retryFailedCount == dataHandoverRetryRule.getMaxRetries()) {
                log("Data handover retry failed for " + retryFailedCount + " times. Stopped handover retry.");
                return true;
            }
        }
        return false;
    }

    public void onReset(int i) {
        logl("Remove all retry and throttling entries, reason=" + resetReasonToString(i));
        removeMessages(3);
        removeMessages(4);
        this.mDataProfileManager.clearAllDataProfilePermanentFailures();
        this.mDataRetryEntries.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataRetryManager.$r8$lambda$tGG2lOZKLOrOa2aQt2vPKQBRbCM((DataRetryManager.DataRetryEntry) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((DataRetryManager.DataRetryEntry) obj).setState(4);
            }
        });
        for (DataThrottlingEntry dataThrottlingEntry : this.mDataThrottlingEntries) {
            DataProfile dataProfile = dataThrottlingEntry.dataProfile;
            onDataProfileUnthrottled(dataProfile, dataProfile.getApnSetting() != null ? dataProfile.getApnSetting().getApnName() : null, dataThrottlingEntry.transport, false, true);
        }
        this.mDataThrottlingEntries.clear();
    }

    public static /* synthetic */ boolean $r8$lambda$tGG2lOZKLOrOa2aQt2vPKQBRbCM(DataRetryEntry dataRetryEntry) {
        return dataRetryEntry.getState() == 1;
    }

    private int getRetryFailedCount(DataNetwork dataNetwork, DataHandoverRetryRule dataHandoverRetryRule) {
        int i = 0;
        for (int size = this.mDataRetryEntries.size() - 1; size >= 0; size--) {
            if (this.mDataRetryEntries.get(size) instanceof DataHandoverRetryEntry) {
                DataHandoverRetryEntry dataHandoverRetryEntry = (DataHandoverRetryEntry) this.mDataRetryEntries.get(size);
                if (dataHandoverRetryEntry.dataNetwork == dataNetwork && dataHandoverRetryRule.equals(dataHandoverRetryEntry.appliedDataRetryRule)) {
                    if (dataHandoverRetryEntry.getState() == 3 || dataHandoverRetryEntry.getState() == 4) {
                        break;
                    }
                    i++;
                }
            }
        }
        return i;
    }

    private int getRetryFailedCount(int i, DataSetupRetryRule dataSetupRetryRule, int i2) {
        int i3 = 0;
        for (int size = this.mDataRetryEntries.size() - 1; size >= 0; size--) {
            if (this.mDataRetryEntries.get(size) instanceof DataSetupRetryEntry) {
                DataSetupRetryEntry dataSetupRetryEntry = (DataSetupRetryEntry) this.mDataRetryEntries.get(size);
                if (!dataSetupRetryEntry.networkRequestList.isEmpty() && dataSetupRetryEntry.setupRetryType == 2 && dataSetupRetryEntry.transport == i2) {
                    if (dataSetupRetryEntry.networkRequestList.isEmpty()) {
                        logl("Invalid data retry entry detected");
                        loge("mDataRetryEntries=" + this.mDataRetryEntries);
                        AnomalyReporter.reportAnomaly(UUID.fromString("afeab78c-c0b0-49fc-a51f-f766814d7aa6"), "Invalid data retry entry detected", this.mPhone.getCarrierId());
                    } else if (dataSetupRetryEntry.networkRequestList.get(0).getHighestPrioritySupportedNetworkCapability() == i && dataSetupRetryEntry.appliedDataRetryRule.equals(dataSetupRetryRule)) {
                        if (dataSetupRetryEntry.getState() == 3 || dataSetupRetryEntry.getState() == 4) {
                            break;
                        }
                        i3++;
                    }
                }
            }
        }
        return i3;
    }

    private void schedule(final DataRetryEntry dataRetryEntry) {
        logl("Scheduled data retry " + dataRetryEntry + " hashcode=" + dataRetryEntry.hashCode());
        this.mDataRetryEntries.add(dataRetryEntry);
        if (this.mDataRetryEntries.size() >= 100) {
            this.mDataRetryEntries.remove(0);
        }
        if (dataRetryEntry.retryDelayMillis <= RETRY_LONG_DELAY_TIMER_THRESHOLD_MILLIS) {
            sendMessageDelayed(obtainMessage(dataRetryEntry instanceof DataSetupRetryEntry ? 3 : 4, dataRetryEntry), dataRetryEntry.retryDelayMillis);
            return;
        }
        this.mAlarmManager.setExactAndAllowWhileIdle(3, dataRetryEntry.retryElapsedTime, "dataRetryHash-" + dataRetryEntry.hashCode(), new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), null, new AlarmManager.OnAlarmListener() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda5
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$schedule$10(dataRetryEntry);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$schedule$10(DataRetryEntry dataRetryEntry) {
        logl("onAlarm retry " + dataRetryEntry);
        sendMessage(obtainMessage(dataRetryEntry instanceof DataSetupRetryEntry ? 3 : 4, dataRetryEntry));
    }

    private void throttleDataProfile(final DataProfile dataProfile, DataNetworkController.NetworkRequestList networkRequestList, DataNetwork dataNetwork, int i, final int i2, long j) {
        DataThrottlingEntry dataThrottlingEntry = new DataThrottlingEntry(dataProfile, networkRequestList, dataNetwork, i2, i, j);
        this.mDataThrottlingEntries.removeIf(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataRetryManager.m843$r8$lambda$DlkXOvaCbr0ADSNJff1LJYNAS4(dataProfile, i2, (DataRetryManager.DataThrottlingEntry) obj);
            }
        });
        if (this.mDataThrottlingEntries.size() >= 100) {
            AnomalyReporter.reportAnomaly(UUID.fromString("24fd4d46-1d0f-4b13-b7d6-7bad70b8289b"), "DataRetryManager throttling more than 100 data profiles", this.mPhone.getCarrierId());
            this.mDataThrottlingEntries.remove(0);
        }
        logl("Add throttling entry " + dataThrottlingEntry);
        this.mDataThrottlingEntries.add(dataThrottlingEntry);
        notifyThrottleStatus(dataProfile, j, j == Long.MAX_VALUE ? 1 : i, i2);
    }

    /* JADX INFO: renamed from: $r8$lambda$DlkXOvaCbr0ADSN-Jff1LJYNAS4, reason: not valid java name */
    public static /* synthetic */ boolean m843$r8$lambda$DlkXOvaCbr0ADSNJff1LJYNAS4(DataProfile dataProfile, int i, DataThrottlingEntry dataThrottlingEntry) {
        return dataProfile.equals(dataThrottlingEntry.dataProfile) && dataThrottlingEntry.transport == i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onDataProfileUnthrottled(final DataProfile dataProfile, final String str, final int i, boolean z, boolean z2) {
        DataProfile dataProfile2;
        int i2;
        DataRetryManager dataRetryManager;
        log("onDataProfileUnthrottled: dataProfile=" + dataProfile + ", apn=" + str + ", transport=" + AccessNetworkConstants.transportTypeToString(i) + ", remove=" + z);
        final long jElapsedRealtime = SystemClock.elapsedRealtime();
        List<DataThrottlingEntry> arrayList = new ArrayList();
        if (dataProfile != null) {
            Stream<DataThrottlingEntry> streamFilter = this.mDataThrottlingEntries.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda6
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataRetryManager.$r8$lambda$pLyXSUbj7eZw2Fv299coS_Jxbj8(jElapsedRealtime, i, (DataRetryManager.DataThrottlingEntry) obj);
                }
            });
            if (dataProfile.getApnSetting() != null) {
                streamFilter = streamFilter.filter(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda7
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return DataRetryManager.$r8$lambda$Riz2CVeZjColYAJbZ5MpZU0YxMs((DataRetryManager.DataThrottlingEntry) obj);
                    }
                }).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda8
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return ((DataRetryManager.DataThrottlingEntry) obj).dataProfile.getApnSetting().getApnName().equals(dataProfile.getApnSetting().getApnName());
                    }
                });
            }
            arrayList = (List) streamFilter.filter(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda9
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.lambda$onDataProfileUnthrottled$16(dataProfile, (DataRetryManager.DataThrottlingEntry) obj);
                }
            }).collect(Collectors.toList());
        } else if (str != null) {
            arrayList = (List) this.mDataThrottlingEntries.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda10
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataRetryManager.m841$r8$lambda$rbfxZG7yG1rz1FDi7sHRQyuFE(jElapsedRealtime, str, i, (DataRetryManager.DataThrottlingEntry) obj);
                }
            }).collect(Collectors.toList());
        }
        if (arrayList.isEmpty()) {
            log("onDataProfileUnthrottled: Nothing to unthrottle.");
            return;
        }
        new ArrayList();
        if (((DataThrottlingEntry) arrayList.get(0)).retryType == 2) {
            dataProfile2 = ((DataThrottlingEntry) arrayList.get(0)).dataProfile;
            i2 = 2;
        } else if (((DataThrottlingEntry) arrayList.get(0)).retryType == 3) {
            dataProfile2 = ((DataThrottlingEntry) arrayList.get(0)).dataNetwork.getDataProfile();
            i2 = 3;
        } else {
            dataProfile2 = null;
            i2 = 1;
        }
        if (dataProfile2 != null) {
            dataRetryManager = this;
            dataRetryManager.notifyThrottleStatus(dataProfile2, -1L, i2, i);
            dataRetryManager.cancelRetriesForDataProfile(dataProfile2, i);
        } else {
            dataRetryManager = this;
        }
        dataRetryManager.logl("onDataProfileUnthrottled: Removing the following throttling entries. " + arrayList);
        if (z2) {
            for (DataThrottlingEntry dataThrottlingEntry : arrayList) {
                int i3 = dataThrottlingEntry.retryType;
                if (i3 == 2) {
                    dataRetryManager.schedule(((DataSetupRetryEntry.Builder) new DataSetupRetryEntry.Builder().setDataProfile(dataThrottlingEntry.dataProfile).setTransport(dataThrottlingEntry.transport).setSetupRetryType(1).setNetworkRequestList(dataThrottlingEntry.networkRequestList).setRetryDelay(0L)).build());
                } else if (i3 == 3) {
                    dataRetryManager.schedule(((DataHandoverRetryEntry.Builder) new DataHandoverRetryEntry.Builder().setDataNetwork(dataThrottlingEntry.dataNetwork).setRetryDelay(0L)).build());
                }
            }
        }
        if (z) {
            dataRetryManager.mDataThrottlingEntries.removeAll(arrayList);
        }
    }

    public static /* synthetic */ boolean $r8$lambda$pLyXSUbj7eZw2Fv299coS_Jxbj8(long j, int i, DataThrottlingEntry dataThrottlingEntry) {
        return dataThrottlingEntry.expirationTimeMillis > j && dataThrottlingEntry.transport == i;
    }

    public static /* synthetic */ boolean $r8$lambda$Riz2CVeZjColYAJbZ5MpZU0YxMs(DataThrottlingEntry dataThrottlingEntry) {
        return dataThrottlingEntry.dataProfile.getApnSetting() != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onDataProfileUnthrottled$16(DataProfile dataProfile, DataThrottlingEntry dataThrottlingEntry) {
        return areTrafficDescriptorsEqual(dataThrottlingEntry.dataProfile.getTrafficDescriptor(), dataProfile.getTrafficDescriptor());
    }

    /* JADX INFO: renamed from: $r8$lambda$-rbfxZG7yG1rz-1FDi7sHRQyuFE, reason: not valid java name */
    public static /* synthetic */ boolean m841$r8$lambda$rbfxZG7yG1rz1FDi7sHRQyuFE(long j, String str, int i, DataThrottlingEntry dataThrottlingEntry) {
        return dataThrottlingEntry.expirationTimeMillis > j && dataThrottlingEntry.dataProfile.getApnSetting() != null && str.equals(dataThrottlingEntry.dataProfile.getApnSetting().getApnName()) && dataThrottlingEntry.transport == i;
    }

    private boolean areTrafficDescriptorsEqual(TrafficDescriptor trafficDescriptor, TrafficDescriptor trafficDescriptor2) {
        if (Objects.equals(trafficDescriptor, trafficDescriptor2)) {
            return true;
        }
        HalVersion halVersion = this.mRil.getHalVersion(1);
        HalVersion halVersion2 = RIL.RADIO_HAL_VERSION_2_4;
        if (halVersion.greater(halVersion2) || trafficDescriptor == null || trafficDescriptor2 == null || !Objects.equals(trafficDescriptor.getDataNetworkName(), trafficDescriptor2.getDataNetworkName()) || !Arrays.equals(trafficDescriptor.getOsAppId(), trafficDescriptor2.getOsAppId())) {
            return false;
        }
        return !halVersion.equals(halVersion2) || trafficDescriptor2.getConnectionCapability() == 0;
    }

    private void cancelRetriesForDataProfile(final DataProfile dataProfile, final int i) {
        logl("cancelRetriesForDataProfile: Canceling pending retries for " + dataProfile);
        this.mDataRetryEntries.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataRetryManager.$r8$lambda$cSvEBxwQN_CNpUcZwAn5UkAVs2A(dataProfile, i, (DataRetryManager.DataRetryEntry) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda24
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((DataRetryManager.DataRetryEntry) obj).setState(4);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$cSvEBxwQN_CNpUcZwAn5UkAVs2A(DataProfile dataProfile, int i, DataRetryEntry dataRetryEntry) {
        if (dataRetryEntry.getState() != 1) {
            return false;
        }
        if (dataRetryEntry instanceof DataSetupRetryEntry) {
            DataSetupRetryEntry dataSetupRetryEntry = (DataSetupRetryEntry) dataRetryEntry;
            return dataProfile.equals(dataSetupRetryEntry.dataProfile) && i == dataSetupRetryEntry.transport;
        }
        if (dataRetryEntry instanceof DataHandoverRetryEntry) {
            return dataProfile.equals(((DataHandoverRetryEntry) dataRetryEntry).dataNetwork.getDataProfile());
        }
        return false;
    }

    public boolean isSimilarNetworkRequestRetryScheduled(TelephonyNetworkRequest telephonyNetworkRequest, int i) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        for (int size = this.mDataRetryEntries.size() - 1; size >= 0; size--) {
            if (this.mDataRetryEntries.get(size) instanceof DataSetupRetryEntry) {
                DataSetupRetryEntry dataSetupRetryEntry = (DataSetupRetryEntry) this.mDataRetryEntries.get(size);
                if (!dataSetupRetryEntry.networkRequestList.isEmpty() && dataSetupRetryEntry.getState() == 1 && dataSetupRetryEntry.setupRetryType == 2 && dataSetupRetryEntry.retryElapsedTime > jElapsedRealtime) {
                    if (dataSetupRetryEntry.networkRequestList.isEmpty()) {
                        logl("Invalid data retry entry detected");
                        loge("mDataRetryEntries=" + this.mDataRetryEntries);
                        AnomalyReporter.reportAnomaly(UUID.fromString("781af571-f55d-476d-b510-7a5381f633dc"), "Invalid data retry entry detected", this.mPhone.getCarrierId());
                    } else {
                        TelephonyNetworkRequest telephonyNetworkRequest2 = dataSetupRetryEntry.networkRequestList.get(0);
                        int highestPrioritySupportedNetworkCapability = telephonyNetworkRequest2.getHighestPrioritySupportedNetworkCapability();
                        if (highestPrioritySupportedNetworkCapability == telephonyNetworkRequest.getHighestPrioritySupportedNetworkCapability() && dataSetupRetryEntry.transport == i && ((highestPrioritySupportedNetworkCapability != 29 && (highestPrioritySupportedNetworkCapability != 12 || !telephonyNetworkRequest2.hasCapability(29) || !telephonyNetworkRequest.hasCapability(29))) || Arrays.equals(telephonyNetworkRequest2.getNativeNetworkRequest().getEnterpriseIds(), telephonyNetworkRequest.getNativeNetworkRequest().getEnterpriseIds()))) {
                            log("Found similar request to be retried. " + telephonyNetworkRequest2);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isDataProfileThrottled(final DataProfile dataProfile, final int i) {
        final long jElapsedRealtime = SystemClock.elapsedRealtime();
        return this.mDataThrottlingEntries.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataRetryManager.$r8$lambda$slM8ajPNRoBoKO_Qors9g58BiKQ(dataProfile, jElapsedRealtime, i, (DataRetryManager.DataThrottlingEntry) obj);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$slM8ajPNRoBoKO_Qors9g58BiKQ(DataProfile dataProfile, long j, int i, DataThrottlingEntry dataThrottlingEntry) {
        return dataThrottlingEntry.dataProfile.equals(dataProfile) && dataThrottlingEntry.expirationTimeMillis > j && dataThrottlingEntry.transport == i;
    }

    public void cancelPendingHandoverRetry(DataNetwork dataNetwork) {
        sendMessage(obtainMessage(7, dataNetwork));
    }

    private void onCancelPendingHandoverRetry(final DataNetwork dataNetwork) {
        this.mDataRetryEntries.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataRetryManager.$r8$lambda$RRkrEdaCQoZ9Q8R5vBZaZA1xkxE(dataNetwork, (DataRetryManager.DataRetryEntry) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((DataRetryManager.DataRetryEntry) obj).setState(4);
            }
        });
        final long jElapsedRealtime = SystemClock.elapsedRealtime();
        DataThrottlingEntry dataThrottlingEntryOrElse = this.mDataThrottlingEntries.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda20
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataRetryManager.$r8$lambda$b9yHI4KKiKVH8AbSzldYqImAClU(dataNetwork, jElapsedRealtime, (DataRetryManager.DataThrottlingEntry) obj);
            }
        }).findAny().orElse(null);
        if (dataThrottlingEntryOrElse == null) {
            return;
        }
        log("onCancelPendingHandoverRetry removed throttling entry:" + dataThrottlingEntryOrElse);
        notifyThrottleStatus(dataThrottlingEntryOrElse.dataNetwork.getDataProfile(), -1L, 3, dataThrottlingEntryOrElse.transport);
        this.mDataThrottlingEntries.removeIf(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda21
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataRetryManager.$r8$lambda$mUfRy3mX3HoaSQZ6Q8aOwpJ5T6o(dataNetwork, (DataRetryManager.DataThrottlingEntry) obj);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$RRkrEdaCQoZ9Q8R5vBZaZA1xkxE(DataNetwork dataNetwork, DataRetryEntry dataRetryEntry) {
        return (dataRetryEntry instanceof DataHandoverRetryEntry) && ((DataHandoverRetryEntry) dataRetryEntry).dataNetwork == dataNetwork && dataRetryEntry.getState() == 1;
    }

    public static /* synthetic */ boolean $r8$lambda$b9yHI4KKiKVH8AbSzldYqImAClU(DataNetwork dataNetwork, long j, DataThrottlingEntry dataThrottlingEntry) {
        return dataNetwork == dataThrottlingEntry.dataNetwork && dataThrottlingEntry.expirationTimeMillis > j;
    }

    public static /* synthetic */ boolean $r8$lambda$mUfRy3mX3HoaSQZ6Q8aOwpJ5T6o(DataNetwork dataNetwork, DataThrottlingEntry dataThrottlingEntry) {
        return dataNetwork == dataThrottlingEntry.dataNetwork;
    }

    private void notifyThrottleStatus(DataProfile dataProfile, final long j, final int i, final int i2) {
        if (dataProfile.getApnSetting() != null) {
            final boolean z = j == -1;
            if (z) {
                dataProfile.getApnSetting().setPermanentFailed(false);
            }
            final ArrayList arrayList = new ArrayList(dataProfile.getApnSetting().getApnTypes().stream().map(new Function() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda27
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.lambda$notifyThrottleStatus$25(i, i2, z, j, (Integer) obj);
                }
            }).toList());
            this.mDataRetryManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda28
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DataRetryManager.DataRetryManagerCallback dataRetryManagerCallback = (DataRetryManager.DataRetryManagerCallback) obj;
                    dataRetryManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda29
                        @Override // java.lang.Runnable
                        public final void run() {
                            dataRetryManagerCallback.onThrottleStatusChanged(list);
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ThrottleStatus lambda$notifyThrottleStatus$25(int i, int i2, boolean z, long j, Integer num) {
        ThrottleStatus.Builder transportType = new ThrottleStatus.Builder().setApnType(num.intValue()).setSlotIndex(this.mPhone.getPhoneId()).setRetryType(i).setTransportType(i2);
        if (z) {
            transportType.setNoThrottle();
        } else {
            transportType.setThrottleExpiryTimeMillis(j);
        }
        return transportType.build();
    }

    public boolean isAnyHandoverRetryScheduled(final DataNetwork dataNetwork) {
        final Class<DataHandoverRetryEntry> cls = DataHandoverRetryEntry.class;
        return this.mDataRetryEntries.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return cls.isInstance((DataRetryManager.DataRetryEntry) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda16
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return (DataRetryManager.DataHandoverRetryEntry) cls.cast((DataRetryManager.DataRetryEntry) obj);
            }
        }).anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataRetryManager$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataRetryManager.$r8$lambda$fJtMTiH2N6l0Sp51IEiZ2yd6eac(dataNetwork, (DataRetryManager.DataHandoverRetryEntry) obj);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$fJtMTiH2N6l0Sp51IEiZ2yd6eac(DataNetwork dataNetwork, DataHandoverRetryEntry dataHandoverRetryEntry) {
        return dataHandoverRetryEntry.getState() == 1 && dataHandoverRetryEntry.dataNetwork == dataNetwork;
    }

    public void registerCallback(DataRetryManagerCallback dataRetryManagerCallback) {
        this.mDataRetryManagerCallbacks.add(dataRetryManagerCallback);
    }

    public void unregisterCallback(DataRetryManagerCallback dataRetryManagerCallback) {
        this.mDataRetryManagerCallbacks.remove(dataRetryManagerCallback);
    }

    protected static String resetReasonToString(int i) {
        switch (i) {
            case 1:
                return "DATA_PROFILES_CHANGED";
            case 2:
                return "RADIO_ON";
            case 3:
                return "MODEM_RESTART";
            case 4:
                return "DATA_SERVICE_BOUND";
            case 5:
                return "DATA_CONFIG_CHANGED";
            case 6:
                return "TAC_CHANGED";
            case 7:
                return "DATA_ENABLED_CHANGED";
            default:
                return "UNKNOWN(" + i + ")";
        }
    }

    protected void log(String str) {
        Rlog.d(this.mLogTag, str);
    }

    protected void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }

    protected void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println(DataRetryManager.class.getSimpleName() + "-" + this.mPhone.getPhoneId() + ":");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("Data Setup Retry rules:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mDataSetupRetryRuleList.forEach(new DataConfigManager$$ExternalSyntheticLambda4(androidUtilIndentingPrintWriter));
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Data Handover Retry rules:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mDataHandoverRetryRuleList.forEach(new DataConfigManager$$ExternalSyntheticLambda5(androidUtilIndentingPrintWriter));
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Retry entries:");
        androidUtilIndentingPrintWriter.increaseIndent();
        Iterator it = this.mDataRetryEntries.iterator();
        while (it.hasNext()) {
            androidUtilIndentingPrintWriter.println((DataRetryEntry) it.next());
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Throttling entries:");
        androidUtilIndentingPrintWriter.increaseIndent();
        Iterator<DataThrottlingEntry> it2 = this.mDataThrottlingEntries.iterator();
        while (it2.hasNext()) {
            androidUtilIndentingPrintWriter.println(it2.next());
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Local logs:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
    }
}
