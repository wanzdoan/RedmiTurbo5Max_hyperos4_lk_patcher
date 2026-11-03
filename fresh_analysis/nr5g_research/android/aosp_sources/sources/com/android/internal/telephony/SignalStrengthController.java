package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.MiuiSignalStrengthStub;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SignalStrengthUpdateRequest;
import android.telephony.SignalThresholdInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.util.Pair;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.regex.PatternSyntaxException;

/* JADX INFO: loaded from: classes.dex */
public class SignalStrengthController extends Handler {
    protected static final int EVENT_GET_SIGNAL_STRENGTH = 6;
    public static final int EVENT_POLL_SIGNAL_STRENGTH_DONE = 9;
    protected PersistableBundle mCarrierConfig;
    protected final CommandsInterface mCi;
    private final String mLogTag;
    protected final Phone mPhone;
    protected SignalStrength mSignalStrength;
    private long mSignalStrengthUpdatedTime;
    private static final long SIGNAL_STRENGTH_REFRESH_THRESHOLD_IN_MS = 10000;
    private static final long POLL_PERIOD_MILLIS = 20000;
    private final RegistrantList mSignalStrengthChangedRegistrants = new RegistrantList();
    private SignalStrength mLastSignalStrength = null;
    private int mLastSubId = -1;
    private ArrayList mEarfcnPairListForRsrpBoost = null;
    private int mLteRsrpBoost = 0;
    private ArrayList mNrarfcnRangeListForRsrpBoost = null;
    private int[] mNrRsrpBoost = null;
    private final Object mRsrpBoostLock = new Object();
    private final List mSignalRequestRecords = new ArrayList();
    private final LocalLog mLocalLog = new LocalLog(64);
    private final AtomicBoolean mNTNConnected = new AtomicBoolean(false);
    private boolean mNeedRetryNotify = false;

    private static final class AccessNetworkThresholds {
        public static final int[] GERAN = {-109, -103, -97, -89};
        public static final int[] UTRAN = {-114, -104, -94, -84};
        public static final int[] EUTRAN_RSRP = {-128, -118, -108, -98};
        public static final int[] EUTRAN_RSRQ = {-20, -17, -14, -11};
        public static final int[] EUTRAN_RSSNR = {-3, 1, 5, 13};
        public static final int[] CDMA2000 = {-105, -90, -75, -65};
        public static final int[] NGRAN_SSRSRP = {-110, -90, -80, -65};
        public static final int[] NGRAN_SSRSRQ = {-31, -19, -7, 6};
        public static final int[] NGRAN_SSSINR = {-5, 5, 15, 30};
        public static final int[] UTRAN_ECNO = {-24, -14, -6, 1};
    }

    private void log(String str) {
    }

    public SignalStrengthController(Phone phone) {
        this.mPhone = phone;
        CommandsInterface commandsInterface = phone.mCi;
        this.mCi = commandsInterface;
        this.mLogTag = "SSCtr-" + phone.getPhoneId();
        commandsInterface.registerForRilConnected(this, 4, null);
        commandsInterface.registerForAvailable(this, 5, null);
        commandsInterface.setOnSignalStrengthUpdate(this, 8, null);
        setSignalStrengthDefaultValues();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        this.mCarrierConfig = getCarrierConfig();
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.internal.telephony.SignalStrengthController$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.SignalStrengthController$$ExternalSyntheticLambda1
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
        phone.registerForServiceStateChanged(this, 10, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        onCarrierConfigurationChanged(i);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                Pair pair = (Pair) message.obj;
                final SignalRequestRecord signalRequestRecord = (SignalRequestRecord) pair.first;
                Message message2 = (Message) pair.second;
                AsyncResult asyncResultForMessage = AsyncResult.forMessage(message2);
                if (this.mSignalRequestRecords.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.SignalStrengthController$$ExternalSyntheticLambda3
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return SignalStrengthController.m411$r8$lambda$BHo4qyiF2lMWmWtdRmeLKgOJLk(signalRequestRecord, (SignalStrengthController.SignalRequestRecord) obj);
                    }
                })) {
                    asyncResultForMessage.exception = new IllegalStateException("setSignalStrengthUpdateRequest called again with same subId");
                    message2.sendToTarget();
                } else {
                    try {
                        signalRequestRecord.mRequest.getLiveToken().linkToDeath(signalRequestRecord, 0);
                        this.mSignalRequestRecords.add(signalRequestRecord);
                        updateAlwaysReportSignalStrength();
                        updateReportingCriteria();
                        message2.sendToTarget();
                    } catch (RemoteException | NullPointerException unused) {
                        asyncResultForMessage.exception = new IllegalStateException("Signal request client is already dead.");
                        message2.sendToTarget();
                        return;
                    }
                }
                break;
            case 2:
                Pair pair2 = (Pair) message.obj;
                SignalRequestRecord signalRequestRecord2 = (SignalRequestRecord) pair2.first;
                Message message3 = (Message) pair2.second;
                Iterator it = this.mSignalRequestRecords.iterator();
                while (it.hasNext()) {
                    SignalRequestRecord signalRequestRecord3 = (SignalRequestRecord) it.next();
                    if (signalRequestRecord3.mRequest.getLiveToken().equals(signalRequestRecord2.mRequest.getLiveToken())) {
                        try {
                            signalRequestRecord3.mRequest.getLiveToken().unlinkToDeath(signalRequestRecord3, 0);
                            break;
                        } catch (NoSuchElementException unused2) {
                        }
                        it.remove();
                    }
                }
                updateAlwaysReportSignalStrength();
                updateReportingCriteria();
                if (message3 != null) {
                    AsyncResult.forMessage(message3);
                    message3.sendToTarget();
                }
                break;
            case 3:
                updateReportingCriteria();
                break;
            case 4:
            case 5:
                onReset();
                break;
            case 6:
            case 9:
                if (this.mCi.getRadioState() == 1) {
                    onSignalStrengthResult((AsyncResult) message.obj);
                    break;
                }
                break;
            case 7:
                this.mCi.getSignalStrength(obtainMessage(9));
                break;
            case 8:
                onSignalStrengthResult((AsyncResult) message.obj);
                break;
            case 10:
                onServiceStateChanged((ServiceState) ((AsyncResult) message.obj).result);
                break;
            default:
                log("Unhandled message with number: " + message.what);
                break;
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$BHo4qyiF2lMWm-WtdRmeLKgOJLk, reason: not valid java name */
    public static /* synthetic */ boolean m411$r8$lambda$BHo4qyiF2lMWmWtdRmeLKgOJLk(SignalRequestRecord signalRequestRecord, SignalRequestRecord signalRequestRecord2) {
        return signalRequestRecord2.mCallingUid == signalRequestRecord.mCallingUid && signalRequestRecord2.mSubId == signalRequestRecord.mSubId;
    }

    void dispose() {
        this.mCi.unSetOnSignalStrengthUpdate(this);
    }

    private void onReset() {
        setDefaultSignalStrengthReportingCriteria();
    }

    public void getSignalStrengthFromCi() {
        this.mCi.getSignalStrength(obtainMessage(6));
    }

    protected void onSignalStrengthResult(AsyncResult asyncResult) {
        SignalStrength signalStrength;
        Object obj;
        if (asyncResult.exception == null && (obj = asyncResult.result) != null) {
            signalStrength = (SignalStrength) obj;
            if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).updateSignalPrimaryInNsa(signalStrength);
            }
        } else {
            loge("onSignalStrengthResult() Exception from RIL : " + asyncResult.exception);
            signalStrength = new SignalStrength();
        }
        updateSignalStrength(signalStrength);
    }

    protected void updateSignalStrength(SignalStrength signalStrength) {
        this.mSignalStrength = maybeOverrideSignalStrengthForTest(signalStrength);
        ServiceStateTracker serviceStateTracker = this.mPhone.getServiceStateTracker();
        if (serviceStateTracker != null) {
            this.mSignalStrength.updateLevel(this.mCarrierConfig, serviceStateTracker.mSS);
        } else {
            loge("updateSignalStrength: serviceStateTracker is null");
        }
        this.mSignalStrengthUpdatedTime = System.currentTimeMillis();
        notifySignalStrength();
    }

    private SignalStrength maybeOverrideSignalStrengthForTest(SignalStrength signalStrength) {
        return (!TelephonyUtils.IS_DEBUGGABLE || this.mPhone.getTelephonyTester() == null) ? signalStrength : (SignalStrength) Objects.requireNonNullElse(this.mPhone.getTelephonyTester().getOverriddenSignalStrength(), signalStrength);
    }

    public SignalStrength getSignalStrength() {
        if (shouldRefreshSignalStrength()) {
            log("getSignalStrength() refreshing signal strength.");
            obtainMessage(7).sendToTarget();
        }
        return this.mSignalStrength;
    }

    private boolean shouldRefreshSignalStrength() {
        ServiceState serviceState;
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mSignalStrengthUpdatedTime;
        if (j <= jCurrentTimeMillis && jCurrentTimeMillis - j <= SIGNAL_STRENGTH_REFRESH_THRESHOLD_IN_MS) {
            return false;
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = SubscriptionManagerService.getInstance().getActiveSubscriptionInfoList(this.mPhone.getContext().getOpPackageName(), this.mPhone.getContext().getAttributionTag(), true);
        if (!ArrayUtils.isEmpty(activeSubscriptionInfoList)) {
            for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                if (subscriptionInfo.isOpportunistic() && (serviceState = TelephonyManager.from(this.mPhone.getContext()).createForSubscriptionId(subscriptionInfo.getSubscriptionId()).getServiceState()) != null && serviceState.getDataRegistrationState() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateReportingCriteria() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        ArrayList arrayList = new ArrayList();
        if (!isUsingNonTerrestrialNetwork() && MiuiSignalStrengthStub.updateReportingCriteria(this.mPhone.getHalVersion(4).greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5), this.mCarrierConfig, arrayList)) {
            consolidatedAndSetReportingCriteria(arrayList);
            return;
        }
        int[] intArray = this.mCarrierConfig.getIntArray("gsm_rssi_thresholds_int_array");
        if (intArray != null) {
            arrayList.add(validateAndCreateSignalThresholdInfo(1, intArray, AccessNetworkThresholds.GERAN, 1, true));
        }
        int[] intArray2 = this.mCarrierConfig.getIntArray("wcdma_rscp_thresholds_int_array");
        if (intArray2 != null) {
            arrayList.add(validateAndCreateSignalThresholdInfo(2, intArray2, AccessNetworkThresholds.UTRAN, 2, true));
        }
        PersistableBundle persistableBundle = this.mCarrierConfig;
        if (isUsingNonTerrestrialNetwork()) {
            str = "parameters_used_for_ntn_lte_signal_bar_int";
        } else {
            str = "parameters_used_for_lte_signal_bar_int";
        }
        int i = persistableBundle.getInt(str, 1);
        PersistableBundle persistableBundle2 = this.mCarrierConfig;
        if (isUsingNonTerrestrialNetwork()) {
            str2 = "ntn_lte_rsrp_thresholds_int_array";
        } else {
            str2 = "lte_rsrp_thresholds_int_array";
        }
        int[] intArray3 = persistableBundle2.getIntArray(str2);
        if (intArray3 != null) {
            arrayList.add(validateAndCreateSignalThresholdInfo(3, intArray3, AccessNetworkThresholds.EUTRAN_RSRP, 3, (i & 1) != 0));
        }
        if (this.mPhone.getHalVersion(4).greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) {
            PersistableBundle persistableBundle3 = this.mCarrierConfig;
            if (isUsingNonTerrestrialNetwork()) {
                str3 = "ntn_lte_rsrq_thresholds_int_array";
            } else {
                str3 = "lte_rsrq_thresholds_int_array";
            }
            int[] intArray4 = persistableBundle3.getIntArray(str3);
            if (intArray4 != null) {
                arrayList.add(validateAndCreateSignalThresholdInfo(4, intArray4, AccessNetworkThresholds.EUTRAN_RSRQ, 3, (i & 2) != 0));
            }
            PersistableBundle persistableBundle4 = this.mCarrierConfig;
            if (isUsingNonTerrestrialNetwork()) {
                str4 = "ntn_lte_rssnr_thresholds_int_array";
            } else {
                str4 = "lte_rssnr_thresholds_int_array";
            }
            int[] intArray5 = persistableBundle4.getIntArray(str4);
            if (intArray5 != null) {
                arrayList.add(validateAndCreateSignalThresholdInfo(5, intArray5, AccessNetworkThresholds.EUTRAN_RSSNR, 3, (i & 4) != 0));
            }
            PersistableBundle persistableBundle5 = this.mCarrierConfig;
            if (isUsingNonTerrestrialNetwork()) {
                str5 = "parameters_use_for_ntn_5g_nr_signal_bar_int";
            } else {
                str5 = "parameters_use_for_5g_nr_signal_bar_int";
            }
            int i2 = persistableBundle5.getInt(str5, 1);
            PersistableBundle persistableBundle6 = this.mCarrierConfig;
            if (isUsingNonTerrestrialNetwork()) {
                str6 = "ntn_5g_nr_ssrsrp_thresholds_int_array";
            } else {
                str6 = "5g_nr_ssrsrp_thresholds_int_array";
            }
            int[] intArray6 = persistableBundle6.getIntArray(str6);
            if (intArray6 != null) {
                arrayList.add(validateAndCreateSignalThresholdInfo(6, intArray6, AccessNetworkThresholds.NGRAN_SSRSRP, 6, (i2 & 1) != 0));
            }
            PersistableBundle persistableBundle7 = this.mCarrierConfig;
            if (isUsingNonTerrestrialNetwork()) {
                str7 = "ntn_5g_nr_ssrsrq_thresholds_int_array";
            } else {
                str7 = "5g_nr_ssrsrq_thresholds_int_array";
            }
            int[] intArray7 = persistableBundle7.getIntArray(str7);
            if (intArray7 != null) {
                arrayList.add(validateAndCreateSignalThresholdInfo(7, intArray7, AccessNetworkThresholds.NGRAN_SSRSRQ, 6, (i2 & 2) != 0));
            }
            PersistableBundle persistableBundle8 = this.mCarrierConfig;
            if (isUsingNonTerrestrialNetwork()) {
                str8 = "ntn_5g_nr_sssinr_thresholds_int_array";
            } else {
                str8 = "5g_nr_sssinr_thresholds_int_array";
            }
            int[] intArray8 = persistableBundle8.getIntArray(str8);
            if (intArray8 != null) {
                arrayList.add(validateAndCreateSignalThresholdInfo(8, intArray8, AccessNetworkThresholds.NGRAN_SSSINR, 6, (i2 & 4) != 0));
            }
            int[] intArray9 = this.mCarrierConfig.getIntArray("wcdma_ecno_thresholds_int_array");
            if (intArray9 != null) {
                arrayList.add(validateAndCreateSignalThresholdInfo(9, intArray9, AccessNetworkThresholds.UTRAN_ECNO, 2, false));
            }
        }
        consolidatedAndSetReportingCriteria(arrayList);
    }

    private void setDefaultSignalStrengthReportingCriteria() {
        ArrayList arrayList = new ArrayList();
        HalVersion halVersion = this.mPhone.getHalVersion(4);
        HalVersion halVersion2 = RIL.RADIO_HAL_VERSION_1_5;
        if (MiuiSignalStrengthStub.setDefaultSignalStrengthReportingCriteria(halVersion.greaterOrEqual(halVersion2), arrayList)) {
            consolidatedAndSetReportingCriteria(arrayList);
            return;
        }
        arrayList.add(createSignalThresholdsInfo(1, AccessNetworkThresholds.GERAN, 1, true));
        arrayList.add(createSignalThresholdsInfo(2, AccessNetworkThresholds.UTRAN, 2, true));
        arrayList.add(createSignalThresholdsInfo(3, AccessNetworkThresholds.EUTRAN_RSRP, 3, true));
        arrayList.add(createSignalThresholdsInfo(1, AccessNetworkThresholds.CDMA2000, 4, true));
        if (this.mPhone.getHalVersion(4).greaterOrEqual(halVersion2)) {
            arrayList.add(createSignalThresholdsInfo(4, AccessNetworkThresholds.EUTRAN_RSRQ, 3, false));
            arrayList.add(createSignalThresholdsInfo(5, AccessNetworkThresholds.EUTRAN_RSSNR, 3, true));
            arrayList.add(createSignalThresholdsInfo(6, AccessNetworkThresholds.NGRAN_SSRSRP, 6, true));
            arrayList.add(createSignalThresholdsInfo(7, AccessNetworkThresholds.NGRAN_SSRSRQ, 6, false));
            arrayList.add(createSignalThresholdsInfo(8, AccessNetworkThresholds.NGRAN_SSSINR, 6, false));
            arrayList.add(createSignalThresholdsInfo(9, AccessNetworkThresholds.UTRAN_ECNO, 2, false));
        }
        consolidatedAndSetReportingCriteria(arrayList);
    }

    private void consolidatedAndSetReportingCriteria(List list) {
        int[] thresholds;
        ArrayList arrayList = new ArrayList(list.size());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            SignalThresholdInfo signalThresholdInfo = (SignalThresholdInfo) it.next();
            int radioAccessNetworkType = signalThresholdInfo.getRadioAccessNetworkType();
            int signalMeasurementType = signalThresholdInfo.getSignalMeasurementType();
            boolean z = false;
            boolean z2 = signalThresholdInfo.isEnabled() && shouldHonorSystemThresholds();
            if (z2) {
                thresholds = signalThresholdInfo.getThresholds();
            } else {
                thresholds = new int[0];
            }
            int[] consolidatedSignalThresholds = getConsolidatedSignalThresholds(radioAccessNetworkType, signalMeasurementType, thresholds, 1);
            boolean zShouldEnableSignalThresholdForAppRequest = shouldEnableSignalThresholdForAppRequest(radioAccessNetworkType, signalMeasurementType, this.mPhone.getSubId(), this.mPhone.isDeviceIdle());
            int minimumHysteresisDb = getMinimumHysteresisDb(zShouldEnableSignalThresholdForAppRequest, radioAccessNetworkType, signalMeasurementType, consolidatedSignalThresholds);
            if (signalMeasurementType == 3 || signalMeasurementType == 6) {
                consolidatedSignalThresholds = MiuiSignalStrengthStub.getOptimizedThresholds(minimumHysteresisDb, signalMeasurementType, consolidatedSignalThresholds);
            }
            SignalThresholdInfo.Builder thresholds2 = new SignalThresholdInfo.Builder().setRadioAccessNetworkType(radioAccessNetworkType).setSignalMeasurementType(signalMeasurementType).setHysteresisMs(GbaManager.RETRY_TIME_MS).setHysteresisDb(minimumHysteresisDb).setThresholds(consolidatedSignalThresholds, true);
            if (z2 || zShouldEnableSignalThresholdForAppRequest) {
                z = true;
            }
            arrayList.add(thresholds2.setIsEnabled(z).build());
        }
        this.mCi.setSignalStrengthReportingCriteria(arrayList, null);
        localLog("setSignalStrengthReportingCriteria consolidatedSignalThresholdInfos=" + arrayList);
    }

    public int getMinimumHysteresisDb(boolean z, int i, int i2, int[] iArr) {
        int hysteresisDbFromCarrierConfig = getHysteresisDbFromCarrierConfig(i, i2);
        if (z) {
            hysteresisDbFromCarrierConfig = computeHysteresisDbOnSmallestThresholdDelta(Math.min(hysteresisDbFromCarrierConfig, getHysteresisDbFromSignalThresholdInfoRequests(i, i2)), iArr);
        }
        return TelephonyBaseUtilsStub.getHysteresisDb(hysteresisDbFromCarrierConfig);
    }

    private int getHysteresisDbFromSignalThresholdInfoRequests(int i, int i2) {
        Iterator it = this.mSignalRequestRecords.iterator();
        int hysteresisDb = 2;
        while (it.hasNext()) {
            for (SignalThresholdInfo signalThresholdInfo : ((SignalRequestRecord) it.next()).mRequest.getSignalThresholdInfos()) {
                if (isRanAndSignalMeasurementTypeMatch(i, i2, signalThresholdInfo) && signalThresholdInfo.getHysteresisDb() >= 0) {
                    hysteresisDb = signalThresholdInfo.getHysteresisDb();
                }
            }
        }
        return hysteresisDb;
    }

    /* JADX WARN: Code duplicated, block: B:33:0x0046  */
    private int getHysteresisDbFromCarrierConfig(int i, int i2) {
        String str;
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i != 6) {
                        localLog("No matching configuration");
                    } else if (i2 == 6) {
                        str = "ngran_ssrsrp_hysteresis_db_int";
                    } else if (i2 == 7) {
                        str = "ngran_ssrsrq_hysteresis_db_int";
                    } else if (i2 == 8) {
                        str = "ngran_sssinr_hysteresis_db_int";
                    }
                    str = null;
                } else if (i2 == 3) {
                    str = "eutran_rsrp_hysteresis_db_int";
                } else if (i2 == 4) {
                    str = "eutran_rsrq_hysteresis_db_int";
                } else if (i2 == 5) {
                    str = "eutran_rssnr_hysteresis_db_int";
                } else {
                    str = null;
                }
            } else if (i2 == 2) {
                str = "utran_rscp_hysteresis_db_int";
            } else if (i2 == 9) {
                str = "utran_ecno_hysteresis_db_int";
            } else {
                str = null;
            }
        } else if (i2 == 1) {
            str = "geran_rssi_hysteresis_db_int";
        } else {
            str = null;
        }
        int i3 = str != null ? this.mCarrierConfig.getInt(str, 2) : 2;
        if (i3 >= 0) {
            return i3;
        }
        return 2;
    }

    private int computeHysteresisDbOnSmallestThresholdDelta(int i, int[] iArr) {
        if (iArr.length > 1) {
            int i2 = 0;
            while (i2 != iArr.length - 1) {
                int i3 = i2 + 1;
                int i4 = iArr[i3];
                int i5 = iArr[i2];
                if (i4 - i5 < i) {
                    i = i4 - i5;
                }
                i2 = i3;
            }
        }
        return i;
    }

    public void setSignalStrengthDefaultValues() {
        this.mSignalStrength = maybeOverrideSignalStrengthForTest(new SignalStrength());
        this.mSignalStrengthUpdatedTime = System.currentTimeMillis();
    }

    public void notifySignalStrength() {
        int subId = this.mPhone.getSubId();
        if (this.mSignalStrength == null) {
            return;
        }
        log("notifySignalStrength mNeedRetryNotify = " + this.mNeedRetryNotify);
        if (this.mSignalStrength.equals(this.mLastSignalStrength) && subId == this.mLastSubId && !this.mNeedRetryNotify) {
            return;
        }
        try {
            if (!TelephonyCommonUtilsStub.get().isMiuiRom() && !TelephonyBaseUtilsStub.isAutoDevice()) {
                this.mSignalStrengthChangedRegistrants.notifyRegistrants();
                this.mPhone.notifySignalStrength();
                this.mLastSignalStrength = this.mSignalStrength;
                this.mLastSubId = subId;
                return;
            }
            this.mLastSignalStrength = this.mSignalStrength;
            this.mSignalStrengthChangedRegistrants.notifyRegistrants();
            this.mNeedRetryNotify = !MiuiDefaultPhoneNotifierStub.get().notifySignalStrength(this.mPhone);
        } catch (NullPointerException e) {
            loge("updateSignalStrength() Phone already destroyed: " + e + "SignalStrength not notified");
        }
    }

    public void notifySignalStrength(boolean z) {
        if (z || !this.mSignalStrength.equals(this.mLastSignalStrength)) {
            try {
                this.mSignalStrengthChangedRegistrants.notifyRegistrants();
                this.mPhone.notifySignalStrength();
                this.mLastSignalStrength = this.mSignalStrength;
            } catch (NullPointerException e) {
                loge("updateSignalStrength() Phone already destroyed: " + e + "SignalStrength not notified");
            }
        }
    }

    public void registerForSignalStrengthChanged(Handler handler, int i, Object obj) {
        this.mSignalStrengthChangedRegistrants.add(new Registrant(handler, i, obj));
    }

    public void unregisterForSignalStrengthChanged(Handler handler) {
        this.mSignalStrengthChangedRegistrants.remove(handler);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("SignalStrengthController - phoneId: " + this.mPhone.getPhoneId());
        printWriter.println("SignalStrengthController - Log Begin ----");
        this.mLocalLog.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("SignalStrengthController - Log End ----");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.increaseIndent();
        printWriter.println("mSignalRequestRecords=" + this.mSignalRequestRecords);
        printWriter.println(" mLastSignalStrength=" + this.mLastSignalStrength);
        printWriter.println(" mLastSubId=" + this.mLastSubId);
        printWriter.println(" mSignalStrength=" + this.mSignalStrength);
        printWriter.println(" mLteRsrpBoost=" + this.mLteRsrpBoost);
        printWriter.println(" mNrRsrpBoost=" + Arrays.toString(this.mNrRsrpBoost));
        printWriter.println(" mEarfcnPairListForRsrpBoost=" + this.mEarfcnPairListForRsrpBoost);
        printWriter.println(" mNrarfcnRangeListForRsrpBoost=" + this.mNrarfcnRangeListForRsrpBoost);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.flush();
    }

    public void setSignalStrengthUpdateRequest(int i, int i2, SignalStrengthUpdateRequest signalStrengthUpdateRequest, Message message) {
        sendMessage(obtainMessage(1, new Pair(new SignalRequestRecord(i, i2, signalStrengthUpdateRequest), message)));
        localLog("setSignalStrengthUpdateRequest subId=" + i + " callingUid=" + i2 + " request=" + signalStrengthUpdateRequest);
    }

    public void clearSignalStrengthUpdateRequest(int i, int i2, SignalStrengthUpdateRequest signalStrengthUpdateRequest, Message message) {
        sendMessage(obtainMessage(2, new Pair(new SignalRequestRecord(i, i2, signalStrengthUpdateRequest), message)));
        localLog("clearSignalStrengthUpdateRequest subId=" + i + " callingUid=" + i2 + " request=" + signalStrengthUpdateRequest);
    }

    public int[] getConsolidatedSignalThresholds(int i, int i2, int[] iArr, final int i3) {
        TreeSet treeSet = new TreeSet(new Comparator() { // from class: com.android.internal.telephony.SignalStrengthController$$ExternalSyntheticLambda4
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return SignalStrengthController.$r8$lambda$mHsdxhdo_O3rsbeExrHsUbGvWbY(i3, (Integer) obj, (Integer) obj2);
            }
        });
        int i4 = 0;
        if (iArr != null) {
            for (int i5 : iArr) {
                treeSet.add(Integer.valueOf(i5));
            }
        }
        boolean zIsDeviceIdle = this.mPhone.isDeviceIdle();
        int subId = this.mPhone.getSubId();
        for (SignalRequestRecord signalRequestRecord : this.mSignalRequestRecords) {
            if (subId == signalRequestRecord.mSubId && (!zIsDeviceIdle || signalRequestRecord.mRequest.isReportingRequestedWhileIdle())) {
                for (SignalThresholdInfo signalThresholdInfo : signalRequestRecord.mRequest.getSignalThresholdInfos()) {
                    if (isRanAndSignalMeasurementTypeMatch(i, i2, signalThresholdInfo)) {
                        for (int i6 : signalThresholdInfo.getThresholds()) {
                            treeSet.add(Integer.valueOf(i6));
                        }
                    }
                }
            }
        }
        int[] iArr2 = new int[treeSet.size()];
        Iterator it = treeSet.iterator();
        while (it.hasNext()) {
            iArr2[i4] = ((Integer) it.next()).intValue();
            i4++;
        }
        return iArr2;
    }

    public static /* synthetic */ int $r8$lambda$mHsdxhdo_O3rsbeExrHsUbGvWbY(int i, Integer num, Integer num2) {
        if (num2.intValue() < num.intValue() - i || num2.intValue() > num.intValue() + i) {
            return Integer.compare(num.intValue(), num2.intValue());
        }
        return 0;
    }

    public boolean shouldHonorSystemThresholds() {
        if (!this.mPhone.isDeviceIdle()) {
            return true;
        }
        final int subId = this.mPhone.getSubId();
        return this.mSignalRequestRecords.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.SignalStrengthController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SignalStrengthController.$r8$lambda$Pz2aXYJs5pfiFRl_aqMzGKEFgJ0(subId, (SignalStrengthController.SignalRequestRecord) obj);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$Pz2aXYJs5pfiFRl_aqMzGKEFgJ0(int i, SignalRequestRecord signalRequestRecord) {
        return i == signalRequestRecord.mSubId && signalRequestRecord.mRequest.isSystemThresholdReportingRequestedWhileIdle();
    }

    public void onDeviceIdleStateChanged(boolean z) {
        sendMessage(obtainMessage(3, Boolean.valueOf(z)));
        localLog("onDeviceIdleStateChanged isDeviceIdle=" + z);
    }

    public boolean shouldEnableSignalThresholdForAppRequest(int i, int i2, int i3, boolean z) {
        for (SignalRequestRecord signalRequestRecord : this.mSignalRequestRecords) {
            if (i3 == signalRequestRecord.mSubId) {
                Iterator<SignalThresholdInfo> it = signalRequestRecord.mRequest.getSignalThresholdInfos().iterator();
                while (it.hasNext()) {
                    if (isRanAndSignalMeasurementTypeMatch(i, i2, it.next()) && (!z || isSignalReportRequestedWhileIdle(signalRequestRecord.mRequest))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isRanAndSignalMeasurementTypeMatch(int i, int i2, SignalThresholdInfo signalThresholdInfo) {
        return i == signalThresholdInfo.getRadioAccessNetworkType() && i2 == signalThresholdInfo.getSignalMeasurementType();
    }

    private static boolean isSignalReportRequestedWhileIdle(SignalStrengthUpdateRequest signalStrengthUpdateRequest) {
        return signalStrengthUpdateRequest.isSystemThresholdReportingRequestedWhileIdle() || signalStrengthUpdateRequest.isReportingRequestedWhileIdle();
    }

    private PersistableBundle getCarrierConfig() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        return (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(this.mPhone.getSubId())) == null) ? CarrierConfigManager.getDefaultConfig() : configForSubId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SignalRequestRecord implements IBinder.DeathRecipient {
        final int mCallingUid;
        final SignalStrengthUpdateRequest mRequest;
        final int mSubId;

        SignalRequestRecord(int i, int i2, SignalStrengthUpdateRequest signalStrengthUpdateRequest) {
            this.mCallingUid = i2;
            this.mSubId = i;
            this.mRequest = signalStrengthUpdateRequest;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            SignalStrengthController.this.localLog("binderDied record=" + this);
            SignalStrengthController.this.clearSignalStrengthUpdateRequest(this.mSubId, this.mCallingUid, this.mRequest, null);
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer("SignalRequestRecord {mSubId=");
            stringBuffer.append(this.mSubId);
            stringBuffer.append(" mCallingUid=");
            stringBuffer.append(this.mCallingUid);
            stringBuffer.append(" mRequest=");
            stringBuffer.append(this.mRequest);
            stringBuffer.append("}");
            return stringBuffer.toString();
        }
    }

    private void updateAlwaysReportSignalStrength() {
        final int subId = this.mPhone.getSubId();
        this.mPhone.setAlwaysReportSignalStrength(this.mSignalRequestRecords.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.SignalStrengthController$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SignalStrengthController.$r8$lambda$3ab7jdXS7yyfbCNCA9jhrT6c0Z4(subId, (SignalStrengthController.SignalRequestRecord) obj);
            }
        }));
    }

    public static /* synthetic */ boolean $r8$lambda$3ab7jdXS7yyfbCNCA9jhrT6c0Z4(int i, SignalRequestRecord signalRequestRecord) {
        return signalRequestRecord.mSubId == i && isSignalReportRequestedWhileIdle(signalRequestRecord.mRequest);
    }

    void updateArfcnLists() {
        synchronized (this.mRsrpBoostLock) {
            try {
                this.mLteRsrpBoost = this.mCarrierConfig.getInt("lte_earfcns_rsrp_boost_int", 0);
                this.mEarfcnPairListForRsrpBoost = convertEarfcnStringArrayToPairList(this.mCarrierConfig.getStringArray("boosted_lte_earfcns_string_array"));
                this.mNrRsrpBoost = this.mCarrierConfig.getIntArray("nrarfcns_rsrp_boost_int_array");
                ArrayList arrayListConvertEarfcnStringArrayToPairList = convertEarfcnStringArrayToPairList(this.mCarrierConfig.getStringArray("boosted_nrarfcns_string_array"));
                this.mNrarfcnRangeListForRsrpBoost = arrayListConvertEarfcnStringArrayToPairList;
                int[] iArr = this.mNrRsrpBoost;
                if ((iArr == null && arrayListConvertEarfcnStringArrayToPairList != null) || ((iArr != null && arrayListConvertEarfcnStringArrayToPairList == null) || (iArr != null && arrayListConvertEarfcnStringArrayToPairList != null && iArr.length != arrayListConvertEarfcnStringArrayToPairList.size()))) {
                    loge("Invalid parameters for NR RSRP boost");
                    this.mNrRsrpBoost = null;
                    this.mNrarfcnRangeListForRsrpBoost = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    void updateServiceStateArfcnRsrpBoost(ServiceState serviceState, CellIdentity cellIdentity) {
        int i;
        int nrarfcn;
        int iContainsEarfcnInEarfcnRange;
        int[] iArr;
        if (cellIdentity == null) {
            return;
        }
        synchronized (this.mRsrpBoostLock) {
            try {
                int type = cellIdentity.getType();
                i = 0;
                if (type == 3) {
                    int earfcn = ((CellIdentityLte) cellIdentity).getEarfcn();
                    if (earfcn != -1 && containsEarfcnInEarfcnRange(this.mEarfcnPairListForRsrpBoost, earfcn) != -1) {
                        i = this.mLteRsrpBoost;
                    }
                } else if (type == 6 && (nrarfcn = ((CellIdentityNr) cellIdentity).getNrarfcn()) != -1 && (iContainsEarfcnInEarfcnRange = containsEarfcnInEarfcnRange(this.mNrarfcnRangeListForRsrpBoost, nrarfcn)) != -1 && (iArr = this.mNrRsrpBoost) != null) {
                    i = iArr[iContainsEarfcnInEarfcnRange];
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        serviceState.setArfcnRsrpBoost(i);
    }

    private static int containsEarfcnInEarfcnRange(ArrayList arrayList, int i) {
        if (arrayList == null) {
            return -1;
        }
        int size = arrayList.size();
        int i2 = 0;
        int i3 = 0;
        while (i3 < size) {
            Object obj = arrayList.get(i3);
            i3++;
            Pair pair = (Pair) obj;
            if (i >= ((Integer) pair.first).intValue() && i <= ((Integer) pair.second).intValue()) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    private ArrayList convertEarfcnStringArrayToPairList(String[] strArr) {
        int i;
        int i2;
        ArrayList arrayList = new ArrayList();
        if (strArr != null) {
            for (String str : strArr) {
                try {
                    String[] strArrSplit = str.split("-");
                    if (strArrSplit.length != 2 || (i = Integer.parseInt(strArrSplit[0])) > (i2 = Integer.parseInt(strArrSplit[1]))) {
                        return null;
                    }
                    arrayList.add(new Pair(Integer.valueOf(i), Integer.valueOf(i2)));
                } catch (NumberFormatException | PatternSyntaxException unused) {
                    return null;
                }
            }
        }
        return arrayList;
    }

    private void onCarrierConfigurationChanged(int i) {
        if (i != this.mPhone.getPhoneId()) {
            return;
        }
        this.mCarrierConfig = getCarrierConfig();
        log("Carrier Config changed.");
        updateArfcnLists();
        updateReportingCriteria();
        updateSignalStrength(new SignalStrength(this.mSignalStrength));
    }

    private static SignalThresholdInfo createSignalThresholdsInfo(int i, int[] iArr, int i2, boolean z) {
        return new SignalThresholdInfo.Builder().setSignalMeasurementType(i).setThresholds(iArr, true).setRadioAccessNetworkType(i2).setIsEnabled(z).build();
    }

    private static SignalThresholdInfo validateAndCreateSignalThresholdInfo(int i, int[] iArr, int[] iArr2, int i2, boolean z) {
        try {
            return new SignalThresholdInfo.Builder().setSignalMeasurementType(i).setThresholds(iArr, true).setRadioAccessNetworkType(i2).setIsEnabled(z).build();
        } catch (Exception unused) {
            SignalThresholdInfo signalThresholdInfoBuild = new SignalThresholdInfo.Builder().setSignalMeasurementType(i).setThresholds(iArr2).setRadioAccessNetworkType(i2).setIsEnabled(z).build();
            AnomalyReporter.reportAnomaly(UUID.fromString("28232bc4-78ff-447e-b597-7c054c802407"), "Invalid parameter to generate SignalThresholdInfo: measurementType=" + i + ", thresholds=" + Arrays.toString(iArr) + ", RAN=" + i2 + ", isEnabled=" + z + ". Replaced with default thresholds: " + Arrays.toString(iArr2));
            return signalThresholdInfoBuild;
        }
    }

    protected void onServiceStateChanged(ServiceState serviceState) {
        if (serviceState.getState() != 0) {
            boolean zIsAospSatellitePsSignalUpdate = TelephonyBaseUtilsStub.isAospSatellitePsSignalUpdate();
            localLog("isAospSatellitePsSignalUpdate: " + zIsAospSatellitePsSignalUpdate);
            if (serviceState.getDataRegState() != 0 && zIsAospSatellitePsSignalUpdate) {
                this.mNTNConnected.set(serviceState.isUsingNonTerrestrialNetwork());
                updateReportingCriteria();
            }
            if (serviceState.getDataRegState() != 0 || !zIsAospSatellitePsSignalUpdate) {
                return;
            }
        }
        if (this.mNTNConnected.get() != serviceState.isUsingNonTerrestrialNetwork()) {
            log("onServiceStateChanged: update it to " + serviceState.isUsingNonTerrestrialNetwork());
            updateReportingCriteria();
            this.mNTNConnected.set(serviceState.isUsingNonTerrestrialNetwork());
        }
    }

    private boolean isUsingNonTerrestrialNetwork() {
        if (this.mPhone.getServiceState() == null) {
            return false;
        }
        return this.mPhone.getServiceState().isUsingNonTerrestrialNetwork();
    }

    protected void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void localLog(String str) {
        Rlog.d(this.mLogTag, str);
        this.mLocalLog.log(this.mLogTag + ": " + str);
    }
}
