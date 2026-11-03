package com.android.phone.satellite.entitlement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.ExponentialBackoff;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.satellite.SatelliteConfig;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.satellite.metrics.EntitlementMetricsStats;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.libraries.entitlement.ServiceEntitlementException;
import com.android.phone.CarrierConfigLoader$$ExternalSyntheticLambda0;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import miuix.reflect.Reflects$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteEntitlementController extends Handler {
    private static SatelliteEntitlementController sInstance;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private final CarrierConfigManager mCarrierConfigManager;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final EntitlementMetricsStats mEntitlementMetricsStats;
    private ConcurrentHashMap mExponentialBackoffPerSub;
    private final FeatureFlags mFeatureFlags;
    private ConcurrentHashMap mIsEntitlementInProgressPerSub;
    private ConcurrentHashMap mLastQueryTimePerSub;
    private final ConnectivityManager.NetworkCallback mNetworkCallback;
    private String mOverriddenEntilementStatusResponseForCtsTest;
    private final BroadcastReceiver mReceiver;
    private ConcurrentHashMap mRetryCountPerSub;
    private ConcurrentHashMap mSatelliteEntitlementResultPerSub;
    private AtomicBoolean mShouldIgnoreInternetConnectionStateForCtsTest;
    private AtomicBoolean mShouldIgnoreRefreshConditionForCtsTest;
    private AtomicBoolean mShouldThrowExceptionForCtsTest;
    private ConcurrentHashMap mSubIdPerSlot;
    private final SubscriptionManagerService mSubscriptionManagerService;
    private static final boolean IS_DEBUG_BUILD = !"user".equals(Build.TYPE);
    private static final long INITIAL_DELAY_MILLIS = 600000;
    private static final long MAX_DELAY_MILLIS = 432000000;

    public static SatelliteEntitlementController make(Context context, FeatureFlags featureFlags) {
        if (sInstance == null) {
            HandlerThread handlerThread = new HandlerThread("SatelliteEntitlementController");
            handlerThread.start();
            sInstance = new SatelliteEntitlementController(context, handlerThread.getLooper(), featureFlags);
        }
        return sInstance;
    }

    public SatelliteEntitlementController(Context context, Looper looper, FeatureFlags featureFlags) {
        super(looper);
        this.mExponentialBackoffPerSub = new ConcurrentHashMap();
        this.mSatelliteEntitlementResultPerSub = new ConcurrentHashMap();
        this.mLastQueryTimePerSub = new ConcurrentHashMap();
        this.mRetryCountPerSub = new ConcurrentHashMap();
        this.mIsEntitlementInProgressPerSub = new ConcurrentHashMap();
        this.mSubIdPerSlot = new ConcurrentHashMap();
        this.mShouldIgnoreInternetConnectionStateForCtsTest = new AtomicBoolean(false);
        this.mShouldIgnoreRefreshConditionForCtsTest = new AtomicBoolean(false);
        this.mOverriddenEntilementStatusResponseForCtsTest = "";
        this.mShouldThrowExceptionForCtsTest = new AtomicBoolean(false);
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        this.mSubscriptionManagerService = SubscriptionManagerService.getInstance();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mCarrierConfigManager = carrierConfigManager;
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.phone.satellite.entitlement.SatelliteEntitlementController$$ExternalSyntheticLambda3
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                this.f$0.lambda$new$0(i, i2, i3, i4);
            }
        };
        this.mCarrierConfigChangeListener = carrierConfigChangeListener;
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.phone.satellite.entitlement.SatelliteEntitlementController$$ExternalSyntheticLambda4
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, carrierConfigChangeListener);
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        this.mConnectivityManager = connectivityManager;
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.phone.satellite.entitlement.SatelliteEntitlementController.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                SatelliteEntitlementController.this.handleInternetConnected();
            }
        };
        this.mNetworkCallback = networkCallback;
        connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().addCapability(12).build(), networkCallback, this);
        SatelliteEntitlementControllerReceiver satelliteEntitlementControllerReceiver = new SatelliteEntitlementControllerReceiver();
        this.mReceiver = satelliteEntitlementControllerReceiver;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        context.registerReceiver(satelliteEntitlementControllerReceiver, intentFilter);
        this.mEntitlementMetricsStats = EntitlementMetricsStats.getOrCreateInstance();
        SatelliteController.getInstance().registerIccRefresh(this, 3);
        SatelliteController.getInstance().registerForConfigUpdateChanged(this, 6, (Object) null);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                handleCmdStartQueryEntitlement(message.arg2);
                return;
            case 2:
                handleCmdRetryQueryEntitlement(message.arg1, message.arg2);
                return;
            case 3:
                handleSimRefresh();
                return;
            case 4:
                logd("AIRPLANE_MODE_CHANGED");
                if (((Boolean) message.obj).booleanValue()) {
                    return;
                }
                resetEntitlementQueryCounts("android.intent.action.AIRPLANE_MODE");
                return;
            case 5:
                logd("CMD_START_QUERY_ENTITLEMENT_FOR_SUB_ID");
                SomeArgs someArgs = (SomeArgs) message.obj;
                try {
                    int iIntValue = ((Integer) someArgs.arg1).intValue();
                    boolean zBooleanValue = ((Boolean) someArgs.arg2).booleanValue();
                    IIntegerConsumer iIntegerConsumer = (IIntegerConsumer) someArgs.arg3;
                    int iIntValue2 = ((Integer) someArgs.arg4).intValue();
                    logd("handleMessage: subId = " + iIntValue);
                    handleCmdStartQueryEntitlementForSubId(iIntValue, zBooleanValue, iIntegerConsumer, iIntValue2);
                    return;
                } finally {
                    someArgs.recycle();
                }
            case 6:
                logd("CMD_UPDATE_CONFIG_DATA");
                handleCmdUpdateConfigData();
                return;
            default:
                logd("do not used this message");
                return;
        }
    }

    private void handleCmdUpdateConfigData() {
        logd("handleCmdUpdateConfigData: sending CMD_START_QUERY_ENTITLEMENT");
        if (isEntitlementItemExistOnSatelliteConfig()) {
            Message messageObtainMessage = obtainMessage();
            messageObtainMessage.what = 1;
            messageObtainMessage.arg2 = 9;
            sendMessage(messageObtainMessage);
        }
    }

    public boolean overrideEntilementStatusResponseForCtsTest(String str, boolean z) {
        if (!isDebugBuild()) {
            logd("overrideEntilementStatusResponseForCtsTest: not allowed for non-debug build.");
            return false;
        }
        logd("overrideEntilementStatusResponseForCtsTest: overriddenEntilementStatusResponse=" + str);
        this.mOverriddenEntilementStatusResponseForCtsTest = str;
        this.mShouldThrowExceptionForCtsTest.set(z);
        return true;
    }

    public boolean overrideEntilementQueryConditions(boolean z, boolean z2) {
        if (!isDebugBuild()) {
            logd("overrideEntilementQueryConditions: not allowed for non-debug build.");
            return false;
        }
        logd("overrideEntilementQueryConditions: ignoreInternetConnection=" + z + ", ignoreRefreshCondition=" + z2);
        this.mShouldIgnoreInternetConnectionStateForCtsTest.set(z);
        this.mShouldIgnoreRefreshConditionForCtsTest.set(z2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleCarrierConfigChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0(int i, int i2, int i3, int i4) {
        logd("handleCarrierConfigChanged(): slotIndex(" + i + "), subId(" + i2 + "), carrierId(" + i3 + "), specificCarrierId(" + i4 + ")");
        processSimChanged(i, i2);
        if (i2 == -1) {
            return;
        }
        Message messageObtainMessage = obtainMessage();
        messageObtainMessage.what = 1;
        messageObtainMessage.arg1 = i2;
        messageObtainMessage.arg2 = 4;
        sendMessage(messageObtainMessage);
        this.mSubIdPerSlot.put(Integer.valueOf(i), Integer.valueOf(i2));
    }

    private void processSimChanged(int i, int i2) {
        int iIntValue = ((Integer) this.mSubIdPerSlot.getOrDefault(Integer.valueOf(i), -1)).intValue();
        logd("processSimChanged prev subId:" + iIntValue);
        if (iIntValue != i2) {
            this.mSubIdPerSlot.remove(Integer.valueOf(i));
            logd("processSimChanged resetEntitlementQueryPerSubId");
            resetEntitlementQueryPerSubId(iIntValue);
        }
    }

    class SatelliteEntitlementControllerReceiver extends BroadcastReceiver {
        private SatelliteEntitlementControllerReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.AIRPLANE_MODE")) {
                SatelliteEntitlementController.this.handleAirplaneModeChange(intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAirplaneModeChange(boolean z) {
        logd("handleAirplaneModeChange: airplaneMode=" + z);
        sendMessage(obtainMessage(4, Boolean.valueOf(z)));
    }

    private void handleSimRefresh() {
        resetEntitlementQueryCounts(cmdToString(3));
        Message messageObtainMessage = obtainMessage();
        messageObtainMessage.what = 1;
        messageObtainMessage.arg2 = 6;
        sendMessageDelayed(messageObtainMessage, 10000L);
    }

    private boolean isInternetConnected() {
        if (this.mShouldIgnoreInternetConnectionStateForCtsTest.get()) {
            logd("isInternetConnected: ignore internet connection state for CTS test");
            return true;
        }
        NetworkCapabilities networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(this.mConnectivityManager.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(12);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInternetConnected() {
        Message messageObtainMessage = obtainMessage();
        messageObtainMessage.what = 1;
        messageObtainMessage.arg2 = 3;
        sendMessage(messageObtainMessage);
    }

    public void requestEntitlementRefresh(int i, IIntegerConsumer iIntegerConsumer) {
        logd("requestEntitlementRefresh: subId = " + i);
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Integer.valueOf(i);
        someArgsObtain.arg2 = Boolean.TRUE;
        someArgsObtain.arg3 = iIntegerConsumer;
        someArgsObtain.arg4 = 5;
        sendMessage(obtainMessage(5, someArgsObtain));
    }

    private int[] getServiceTypeForEntitlementMetrics(Map map) {
        if (map == null || map.isEmpty()) {
            return new int[0];
        }
        return (int[]) map.entrySet().stream().findFirst().map(new Function() { // from class: com.android.phone.satellite.entitlement.SatelliteEntitlementController$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SatelliteEntitlementController.$r8$lambda$u913ULmeA7pJvfguRiPtaiCBxGc((Map.Entry) obj);
            }
        }).orElse(new int[0]);
    }

    public static /* synthetic */ int[] $r8$lambda$u913ULmeA7pJvfguRiPtaiCBxGc(Map.Entry entry) {
        List list = (List) entry.getValue();
        if (list == null) {
            return new int[0];
        }
        return list.stream().mapToInt(new CarrierConfigLoader$$ExternalSyntheticLambda0()).toArray();
    }

    private int getDataPolicyForEntitlementMetrics(Map map) {
        if (map == null || map.isEmpty()) {
            return -1;
        }
        return ((Integer) map.values().stream().findFirst().orElse(-1)).intValue();
    }

    private void reportSuccessForEntitlement(int i, SatelliteEntitlementResult satelliteEntitlementResult, int i2) {
        boolean zIsEmpty = satelliteEntitlementResult.getAvailableServiceTypeInfoForPlmnList().isEmpty();
        boolean z = !zIsEmpty;
        int iMapDataPolicyForMetrics = 0;
        int[] serviceTypeForEntitlementMetrics = new int[0];
        if (!zIsEmpty) {
            serviceTypeForEntitlementMetrics = getServiceTypeForEntitlementMetrics(satelliteEntitlementResult.getAvailableServiceTypeInfoForPlmnList());
            iMapDataPolicyForMetrics = SatelliteController.getInstance().mapDataPolicyForMetrics(getDataPolicyForEntitlementMetrics(satelliteEntitlementResult.getDataServicePolicyInfoForPlmnList()));
        }
        this.mEntitlementMetricsStats.reportSuccess(i, getEntitlementStatus(satelliteEntitlementResult), true, z, serviceTypeForEntitlementMetrics, iMapDataPolicyForMetrics, i2);
    }

    protected void handleCmdStartQueryEntitlement(int i) {
        for (int i2 : this.mSubscriptionManagerService.getActiveSubIdList(true)) {
            handleCmdStartQueryEntitlementForSubId(i2, false, null, i);
        }
    }

    protected void handleCmdStartQueryEntitlementForSubId(int i, boolean z, IIntegerConsumer iIntegerConsumer, int i2) {
        clearRetryCountForSubId(i);
        if (shouldQueryEntitlementForSubId(i, z, false, iIntegerConsumer)) {
            try {
                this.mIsEntitlementInProgressPerSub.put(Integer.valueOf(i), Boolean.TRUE);
                logd("handleCmdStartQueryEntitlement: checkEntitlementStatus");
                SatelliteEntitlementResult satelliteEntitlementResultCheckEntitlementStatus = checkEntitlementStatus(i);
                this.mSatelliteEntitlementResultPerSub.put(Integer.valueOf(i), satelliteEntitlementResultCheckEntitlementStatus);
                reportSuccessForEntitlement(i, satelliteEntitlementResultCheckEntitlementStatus, i2);
                sendResult(i, iIntegerConsumer, 0);
                queryCompleted(i);
            } catch (ServiceEntitlementException e) {
                loge(e.toString());
                this.mEntitlementMetricsStats.reportError(i, e.getErrorCode(), false, e.getHttpStatus(), i2);
                if (!isInternetConnected()) {
                    logd("StartQuery: disconnected during execution. " + e);
                    this.mIsEntitlementInProgressPerSub.remove(Integer.valueOf(i));
                    sendResult(i, iIntegerConsumer, 5);
                    return;
                }
                if (isPermanentError(e)) {
                    queryCompleted(i);
                    sendResult(i, iIntegerConsumer, 2);
                    return;
                }
                if (isRetryAfterError(e)) {
                    long secondsFromRetryAfter = parseSecondsFromRetryAfter(e.getRetryAfter());
                    StringBuilder sb = new StringBuilder("StartQuery: next retry will be in ");
                    TimeUnit timeUnit = TimeUnit.SECONDS;
                    sb.append(timeUnit.toMillis(secondsFromRetryAfter));
                    sb.append(" sec");
                    logd(sb.toString());
                    Message messageObtainMessage = obtainMessage();
                    messageObtainMessage.what = 2;
                    messageObtainMessage.arg1 = i;
                    messageObtainMessage.arg2 = 8;
                    sendMessageDelayed(messageObtainMessage, timeUnit.toMillis(secondsFromRetryAfter));
                    stopExponentialBackoff(i);
                    sendResult(i, iIntegerConsumer, 1);
                    return;
                }
                startExponentialBackoff(i);
                sendResult(i, iIntegerConsumer, 1);
            }
        }
    }

    private void sendResult(int i, IIntegerConsumer iIntegerConsumer, int i2) {
        if (iIntegerConsumer == null) {
            return;
        }
        try {
            iIntegerConsumer.accept(i2);
        } catch (RemoteException e) {
            loge("sendResult:\nsubId = " + i + "\nresult = " + i2 + "\ne = " + e);
            Reflects$$ExternalSyntheticBUOutline0.m(e);
        }
    }

    private void resetEntitlementQueryCounts(String str) {
        logd("resetEntitlementQueryCounts: " + str);
        this.mLastQueryTimePerSub = new ConcurrentHashMap();
        this.mExponentialBackoffPerSub = new ConcurrentHashMap();
        this.mRetryCountPerSub = new ConcurrentHashMap();
        this.mIsEntitlementInProgressPerSub = new ConcurrentHashMap();
    }

    private void handleCmdRetryQueryEntitlement(int i, int i2) {
        if (shouldQueryEntitlementForSubId(i, false, true, null)) {
            try {
                this.mRetryCountPerSub.put(Integer.valueOf(i), Integer.valueOf(getRetryCount(i) + 1));
                logd("[" + i + "] retry cnt:" + getRetryCount(i));
                logd("handleCmdRetryQueryEntitlement: checkEntitlementStatus");
                SatelliteEntitlementResult satelliteEntitlementResultCheckEntitlementStatus = checkEntitlementStatus(i);
                this.mSatelliteEntitlementResultPerSub.put(Integer.valueOf(i), satelliteEntitlementResultCheckEntitlementStatus);
                reportSuccessForEntitlement(i, satelliteEntitlementResultCheckEntitlementStatus, i2);
                queryCompleted(i);
            } catch (ServiceEntitlementException e) {
                loge(e.toString());
                this.mEntitlementMetricsStats.reportError(i, e.getErrorCode(), true, e.getHttpStatus(), i2);
                if (!isRetryAvailable(i)) {
                    logd("retryQuery: unavailable.");
                    queryCompleted(i);
                    return;
                }
                if (!isInternetConnected()) {
                    logd("retryQuery: Internet disconnected.");
                    stopExponentialBackoff(i);
                    this.mIsEntitlementInProgressPerSub.remove(Integer.valueOf(i));
                    return;
                }
                if (isPermanentError(e)) {
                    logd("retryQuery: shouldPermanentError.");
                    queryCompleted(i);
                    return;
                }
                if (isRetryAfterError(e)) {
                    long secondsFromRetryAfter = parseSecondsFromRetryAfter(e.getRetryAfter());
                    StringBuilder sb = new StringBuilder("retryQuery: next retry will be in ");
                    TimeUnit timeUnit = TimeUnit.SECONDS;
                    sb.append(timeUnit.toMillis(secondsFromRetryAfter));
                    sb.append(" sec");
                    logd(sb.toString());
                    Message messageObtainMessage = obtainMessage();
                    messageObtainMessage.what = 2;
                    messageObtainMessage.arg1 = i;
                    messageObtainMessage.arg2 = 8;
                    sendMessageDelayed(messageObtainMessage, timeUnit.toMillis(secondsFromRetryAfter));
                    stopExponentialBackoff(i);
                    return;
                }
                ExponentialBackoff exponentialBackoff = (ExponentialBackoff) this.mExponentialBackoffPerSub.get(Integer.valueOf(i));
                if (exponentialBackoff == null) {
                    startExponentialBackoff(i);
                    return;
                }
                exponentialBackoff.notifyFailed();
                logd("retryQuery: The next retry will be in " + exponentialBackoff.getCurrentDelay() + " ms.");
            }
        }
    }

    private boolean isPermanentError(ServiceEntitlementException serviceEntitlementException) {
        return serviceEntitlementException.getHttpStatus() == 500;
    }

    private boolean isRetryAfterError(ServiceEntitlementException serviceEntitlementException) {
        int httpStatus = serviceEntitlementException.getHttpStatus();
        logd("shouldRetryAfterError: received the " + httpStatus);
        if (httpStatus != 503 || serviceEntitlementException.getRetryAfter() == null || serviceEntitlementException.getRetryAfter().isEmpty()) {
            return false;
        }
        if (parseSecondsFromRetryAfter(serviceEntitlementException.getRetryAfter()) != -1) {
            return true;
        }
        logd("Unable parsing the retry-after. try to exponential backoff.");
        return false;
    }

    private long parseSecondsFromRetryAfter(String str) {
        try {
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException unused) {
                return ChronoUnit.SECONDS.between(Instant.now(), (Temporal) DateTimeFormatter.RFC_1123_DATE_TIME.parse(str, new TemporalQuery() { // from class: com.android.phone.satellite.entitlement.SatelliteEntitlementController$$ExternalSyntheticLambda0
                    @Override // java.time.temporal.TemporalQuery
                    public final Object queryFrom(TemporalAccessor temporalAccessor) {
                        return Instant.from(temporalAccessor);
                    }
                }));
            }
        } catch (DateTimeParseException unused2) {
            return -1L;
        }
    }

    private void startExponentialBackoff(final int i) {
        stopExponentialBackoff(i);
        this.mExponentialBackoffPerSub.put(Integer.valueOf(i), new ExponentialBackoff(INITIAL_DELAY_MILLIS, MAX_DELAY_MILLIS, 2, getLooper(), new Runnable() { // from class: com.android.phone.satellite.entitlement.SatelliteEntitlementController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startExponentialBackoff$2(i);
            }
        }));
        ExponentialBackoff exponentialBackoff = (ExponentialBackoff) this.mExponentialBackoffPerSub.get(Integer.valueOf(i));
        if (exponentialBackoff != null) {
            exponentialBackoff.start();
            logd("start ExponentialBackoff, cnt: " + getRetryCount(i) + ". Retrying in " + exponentialBackoff.getCurrentDelay() + " ms.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startExponentialBackoff$2(int i) {
        Message messageObtainMessage = obtainMessage();
        messageObtainMessage.what = 2;
        messageObtainMessage.arg1 = i;
        messageObtainMessage.arg2 = 8;
        sendMessage(messageObtainMessage);
    }

    private void stopExponentialBackoff(int i) {
        if (this.mExponentialBackoffPerSub.get(Integer.valueOf(i)) != null) {
            logd("stopExponentialBackoff: reset ExponentialBackoff");
            ((ExponentialBackoff) this.mExponentialBackoffPerSub.get(Integer.valueOf(i))).stop();
            this.mExponentialBackoffPerSub.remove(Integer.valueOf(i));
        }
    }

    private void queryCompleted(int i) {
        if (!this.mSatelliteEntitlementResultPerSub.containsKey(Integer.valueOf(i))) {
            logd("queryCompleted: create default SatelliteEntitlementResult");
            this.mSatelliteEntitlementResultPerSub.put(Integer.valueOf(i), SatelliteEntitlementResult.getDefaultResult());
        }
        SatelliteEntitlementResult satelliteEntitlementResult = (SatelliteEntitlementResult) this.mSatelliteEntitlementResultPerSub.get(Integer.valueOf(i));
        stopExponentialBackoff(i);
        this.mIsEntitlementInProgressPerSub.remove(Integer.valueOf(i));
        clearRetryCountForSubId(i);
        saveLastQueryTime(i);
        Message messageObtainMessage = obtainMessage();
        messageObtainMessage.what = 1;
        messageObtainMessage.arg1 = i;
        messageObtainMessage.arg2 = 2;
        sendMessageDelayed(messageObtainMessage, TimeUnit.DAYS.toMillis(getSatelliteEntitlementStatusRefreshDays(i)));
        logd("queryCompleted: updateSatelliteEntitlementStatus");
        updateSatelliteEntitlementStatus(i, satelliteEntitlementResult.getEntitlementStatus() == 1, satelliteEntitlementResult.getAllowedPLMNList(), satelliteEntitlementResult.getBarredPLMNList(), satelliteEntitlementResult.getDataPlanInfoForPlmnList(), satelliteEntitlementResult.getAvailableServiceTypeInfoForPlmnList(), satelliteEntitlementResult.getDataServicePolicyInfoForPlmnList(), satelliteEntitlementResult.getVoiceServicePolicyInfoForPlmnList());
    }

    private void clearRetryCountForSubId(int i) {
        logd("reset retry count for refresh query for subId = " + i);
        this.mRetryCountPerSub.remove(Integer.valueOf(i));
    }

    private boolean shouldQueryEntitlementForSubId(int i, boolean z, boolean z2, IIntegerConsumer iIntegerConsumer) {
        logd("Checking preconditions for subId = " + i);
        if (!isSatelliteEntitlementSupported(i)) {
            logd("Entitlement not supported by carrier config.");
            resetSatelliteEntitlementRestrictedReason(i);
            sendResult(i, iIntegerConsumer, 11);
            return false;
        }
        if (!z2 && ((Boolean) this.mIsEntitlementInProgressPerSub.getOrDefault(Integer.valueOf(i), Boolean.FALSE)).booleanValue()) {
            logd("Entitlement query already in progress.");
            sendResult(i, iIntegerConsumer, 21);
            return false;
        }
        if (!isInternetConnected()) {
            logd("No internet connection available.");
            stopExponentialBackoff(i);
            this.mIsEntitlementInProgressPerSub.remove(Integer.valueOf(i));
            sendResult(i, iIntegerConsumer, 5);
            return false;
        }
        if (!z && !shouldRefreshEntitlementStatus(i)) {
            logd("Entitlement status is fresh; skipping query due to timeout enforcement.");
            sendResult(i, iIntegerConsumer, 1);
            return false;
        }
        if (isRetryAvailable(i)) {
            return true;
        }
        logd("Retry limit reached for entitlement query.");
        sendResult(i, iIntegerConsumer, 1);
        return false;
    }

    protected void resetSatelliteEntitlementRestrictedReason(int i) {
        SatelliteEntitlementController satelliteEntitlementController;
        int i2;
        logd("resetSatelliteEntitlementRestrictedReason, subId=" + i);
        SatelliteEntitlementResult satelliteEntitlementResult = new SatelliteEntitlementResult(1, new ArrayList(), new ArrayList());
        SatelliteEntitlementResult satelliteEntitlementResult2 = (SatelliteEntitlementResult) this.mSatelliteEntitlementResultPerSub.get(Integer.valueOf(i));
        if (satelliteEntitlementResult2 == null || satelliteEntitlementResult2.getEntitlementStatus() == 1) {
            satelliteEntitlementController = this;
            i2 = i;
        } else {
            logd("set enabled status for removing satellite entitlement restricted reason");
            this.mSatelliteEntitlementResultPerSub.put(Integer.valueOf(i), satelliteEntitlementResult);
            satelliteEntitlementController = this;
            i2 = i;
            satelliteEntitlementController.updateSatelliteEntitlementStatus(i2, true, satelliteEntitlementResult.getAllowedPLMNList(), satelliteEntitlementResult.getBarredPLMNList(), satelliteEntitlementResult.getDataPlanInfoForPlmnList(), satelliteEntitlementResult.getAvailableServiceTypeInfoForPlmnList(), satelliteEntitlementResult.getDataServicePolicyInfoForPlmnList(), satelliteEntitlementResult.getVoiceServicePolicyInfoForPlmnList());
        }
        satelliteEntitlementController.resetEntitlementQueryPerSubId(i2);
    }

    private void resetEntitlementQueryPerSubId(int i) {
        logd("resetEntitlementQueryPerSubId: " + i);
        stopExponentialBackoff(i);
        this.mLastQueryTimePerSub.remove(Integer.valueOf(i));
        this.mRetryCountPerSub.remove(Integer.valueOf(i));
        this.mIsEntitlementInProgressPerSub.remove(Integer.valueOf(i));
        Message messageObtainMessage = obtainMessage();
        messageObtainMessage.what = 2;
        messageObtainMessage.arg1 = i;
        messageObtainMessage.arg2 = 8;
        removeMessages(2, messageObtainMessage);
    }

    private boolean shouldRefreshEntitlementStatus(int i) {
        if (this.mShouldIgnoreRefreshConditionForCtsTest.get()) {
            logd("shouldRefreshEntitlementStatus: allow refreshing entitlement for CTS test");
            return true;
        }
        long lastQueryTime = getLastQueryTime(i);
        long millis = TimeUnit.DAYS.toMillis(getSatelliteEntitlementStatusRefreshDays(i));
        boolean z = System.currentTimeMillis() - lastQueryTime > millis;
        if (!z) {
            logd("query is already done. can query after " + Instant.ofEpochMilli(millis + lastQueryTime));
        }
        return z;
    }

    protected SatelliteEntitlementApi getSatelliteEntitlementApi(int i) {
        return new SatelliteEntitlementApi(this.mContext, getConfigForSubId(i), i);
    }

    private long getLastQueryTime(int i) {
        return ((Long) this.mLastQueryTimePerSub.getOrDefault(Integer.valueOf(i), 0L)).longValue();
    }

    private int getSatelliteEntitlementStatusRefreshDays(int i) {
        return getConfigForSubId(i).getInt("satellite_entitlement_status_refresh_days_int", 7);
    }

    private boolean isRetryAvailable(int i) {
        if (getRetryCount(i) < 5) {
            return true;
        }
        logd("The retry will not be attempted until the next trigger event.");
        return false;
    }

    private boolean isEntitlementItemExistOnSatelliteConfig() {
        SatelliteConfig satelliteConfig = SatelliteController.getInstance().getSatelliteConfig();
        if (satelliteConfig == null) {
            logd("isEntitlementItemExistOnSatelliteConfig: return false (SatelliteConfig is null)");
            return false;
        }
        Iterator it = this.mSubIdPerSlot.keySet().iterator();
        while (it.hasNext()) {
            int iIntValue = ((Integer) this.mSubIdPerSlot.get((Integer) it.next())).intValue();
            if (satelliteConfig.isSatelliteEntitlementSupportedBySubId(iIntValue) != null) {
                logd("isEntitlementItemExistOnSatelliteConfig: entitlement support exist, return true");
                return true;
            }
            if (!TextUtils.isEmpty(satelliteConfig.getSatelliteEntitlementServerUrlBySubId(iIntValue))) {
                logd("isEntitlementItemExistOnSatelliteConfig: entitlement url exist, return true");
                return true;
            }
        }
        logd("isEntitlementItemExistOnSatelliteConfig: entitlement related item is not exist ");
        return false;
    }

    private Boolean getEntitlementSupportedFromSatelliteConfig(int i) {
        SatelliteConfig satelliteConfig = SatelliteController.getInstance().getSatelliteConfig();
        if (satelliteConfig == null) {
            logd("getEntitlementSupportedFromSatelliteConfig: return null (SatelliteConfig is null)");
            return null;
        }
        return satelliteConfig.isSatelliteEntitlementSupportedBySubId(i);
    }

    protected boolean isSatelliteEntitlementSupported(int i) {
        Boolean entitlementSupportedFromSatelliteConfig = getEntitlementSupportedFromSatelliteConfig(i);
        if (entitlementSupportedFromSatelliteConfig != null) {
            logd("isSatelliteEntitlementSupported: using SatelliteConfig for subId=" + i + ", entitlementSupported=" + entitlementSupportedFromSatelliteConfig);
            return entitlementSupportedFromSatelliteConfig.booleanValue();
        }
        return getConfigForSubId(i).getBoolean("satellite_entitlement_supported_bool");
    }

    private PersistableBundle getConfigForSubId(int i) {
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        PersistableBundle configForSubId = carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(i, "imsserviceentitlement.entitlement_server_url_string", "satellite_entitlement_status_refresh_days_int", "satellite_entitlement_supported_bool", "satellite_entitlement_app_name_string") : null;
        return (configForSubId == null || configForSubId.isEmpty()) ? CarrierConfigManager.getDefaultConfig() : configForSubId;
    }

    private void saveLastQueryTime(int i) {
        this.mLastQueryTimePerSub.put(Integer.valueOf(i), Long.valueOf(System.currentTimeMillis()));
    }

    private int getRetryCount(int i) {
        return ((Integer) this.mRetryCountPerSub.getOrDefault(Integer.valueOf(i), 0)).intValue();
    }

    private void updateSatelliteEntitlementStatus(int i, boolean z, List list, List list2, Map map, Map map2, Map map3, Map map4) {
        SatelliteController.getInstance().onSatelliteEntitlementStatusUpdated(i, z, list, list2, map, map2, map3, map4, (IIntegerConsumer) null);
    }

    private int getEntitlementStatus(SatelliteEntitlementResult satelliteEntitlementResult) {
        int entitlementStatus = satelliteEntitlementResult.getEntitlementStatus();
        if (entitlementStatus == 0) {
            return 1;
        }
        if (entitlementStatus == 1) {
            return 2;
        }
        if (entitlementStatus != 2) {
            return entitlementStatus != 3 ? 0 : 4;
        }
        return 3;
    }

    private SatelliteEntitlementResult checkEntitlementStatus(int i) {
        logd("checkEntitlementStatus: subId=" + i);
        SatelliteEntitlementApi satelliteEntitlementApi = getSatelliteEntitlementApi(i);
        satelliteEntitlementApi.overrideEntilementStatusResponseForCtsTest(this.mOverriddenEntilementStatusResponseForCtsTest, this.mShouldThrowExceptionForCtsTest.get());
        return satelliteEntitlementApi.checkEntitlementStatus();
    }

    private static String cmdToString(int i) {
        if (i == 3) {
            return "SIM_REFRESH";
        }
        return "UNKNOWN(" + i + ")";
    }

    private static void logd(String str) {
        Rlog.d("SatelliteEntitlementController", str);
    }

    private static void loge(String str) {
        Rlog.e("SatelliteEntitlementController", str);
    }

    private static boolean isDebugBuild() {
        return IS_DEBUG_BUILD;
    }
}
